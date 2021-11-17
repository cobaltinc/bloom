import got from 'got';
import tar from 'tar';
import { Stream } from 'stream';
import { promisify } from 'util';

const pipeline = promisify(Stream.pipeline);

export type RepoInfo = {
  username: string;
  name: string;
  branch: string;
  filePath: string;
};

export async function isUrlOk(url: string): Promise<boolean> {
  const res = await got.head(url).catch((e) => e);
  return res.statusCode === 200;
}

export async function getRepoInfo(url: URL, token?: string, examplePath?: string): Promise<RepoInfo | undefined> {
  const [, username, name, t, _branch, ...file] = url.pathname.split('/');
  const filePath = examplePath ? examplePath.replace(/^\//, '') : file.join('/');

  // Support repos whose entire purpose is to be a NextJS example, e.g.
  // https://github.com/:username/:my-cool-nextjs-example-repo-name.
  if (t === undefined) {
    const infoResponse = await got(`https://api.github.com/repos/${username}/${name}`, {
      headers: { Authorization: token || process.env.GITHUB_TOKEN ? `Bearer ${token || process.env.GITHUB_TOKEN}` : undefined },
    }).catch((e) => e);
    console.log(token);
    if (infoResponse.statusCode !== 200) {
      return;
    }
    const info = JSON.parse(infoResponse.body);
    return { username, name, branch: info['default_branch'], filePath };
  }

  // If examplePath is available, the branch name takes the entire path
  const branch = examplePath ? `${_branch}/${file.join('/')}`.replace(new RegExp(`/${filePath}|/$`), '') : _branch;

  if (username && name && branch && t === 'tree') {
    return { username, name, branch, filePath };
  }
}

export function downloadAndExtractRepo(root: string, { username, name, branch, filePath }: RepoInfo): Promise<void> {
  return pipeline(
    got.stream(`https://codeload.github.com/${username}/${name}/tar.gz/${branch}`),
    tar.extract({ cwd: root, strip: filePath ? filePath.split('/').length + 1 : 1 }, [`${name}-${branch}${filePath ? `/${filePath}` : ''}`]),
  );
}
