import retry from 'async-retry';
import chalk from 'chalk';
import prompts from 'prompts';
import path from 'path';
import cpy from 'cpy';
import fs from 'fs';
import glob from 'glob';
import Mustache from 'mustache';
import { makeDir } from '../helpers/make-dir';
import { tryGitInit } from '../helpers/git';
import { isWriteable } from '../helpers/is-writeable';
import { isFolderEmpty } from '../helpers/is-folder-empty';
import { renameRecursive } from '../helpers/rename-recursive';
import { kebabToPascal } from '../helpers/kebab-to-pascal';
import { camelToPascal } from '../helpers/camel-to-pascal';
import { downloadAndExtractRepo, getRepoInfo, RepoInfo } from '../helpers/github-remote';

export const runNewApp = async (projectPath: string, remoteUrl?: string, token?: string) => {
  if (typeof projectPath === 'string') {
    projectPath = projectPath.trim();
  }
  if (!projectPath) {
    const res = await prompts({
      type: 'text',
      name: 'path',
      message: 'What is your project named?',
      validate: (name: string) => {
        if (name === '') {
          return 'Invalid project name: Not blank';
        }
        return true;
      },
    });

    if (typeof res.path === 'string') {
      projectPath = res.path.trim();
    }
  }

  if (!projectPath) {
    console.log();
    console.log('Please specify the project directory:');
    console.log(`  ${chalk.cyan('bloom new')} ${chalk.green('<project-directory>')}`);
    console.log();
    console.log('For example:');
    console.log(`  ${chalk.cyan('bloom new')} ${chalk.green('my-spring-app')}`);
    console.log();
    console.log(`Run ${chalk.cyan(`${'bloom'} --help`)} to see all options.`);
    process.exit(1);
  }

  const resolvedProjectPath = path.resolve(projectPath);
  await createApp({
    appPath: resolvedProjectPath,
    remoteUrl,
    token,
  });
};

const createApp = async ({ appPath, remoteUrl, token }: { appPath: string; remoteUrl?: string; token?: string }) => {
  let template: string = '';
  let repoInfo: RepoInfo | undefined;

  if (remoteUrl) {
    template = remoteUrl;
    let repoUrl: URL | undefined;

    try {
      repoUrl = new URL(remoteUrl);
    } catch (error: any) {
      if (error.code !== 'ERR_INVALID_URL') {
        console.error(error);
        process.exit(1);
      }
    }

    if (repoUrl) {
      if (repoUrl.origin !== 'https://github.com') {
        console.error(`Invalid URL: ${chalk.red(`"${remoteUrl}"`)}. Only GitHub repositories are supported. Please use a GitHub URL and try again.`);
        process.exit(1);
      }

      repoInfo = await getRepoInfo(repoUrl, token);

      if (!repoInfo) {
        console.error(`Found invalid GitHub URL: ${chalk.red(`"${remoteUrl}"`)}. Please fix the URL and try again.`);
        process.exit(1);
      }
    } else {
      process.exit(1);
    }
  } else {
    const { language, type } = await prompts([
      {
        type: 'select',
        name: 'language',
        message: 'Choose project language',
        choices: [
          { title: 'Kotlin', value: 'kotlin' },
          { title: 'Java', value: 'java' },
        ],
      },
      {
        type: 'select',
        name: 'type',
        message: 'Choose starter type',
        choices: [
          { title: 'WebFlux', value: 'webflux' },
          { title: 'WebMvc', value: 'webmvc' },
        ],
      },
    ]);

    if (!language || !type) {
      process.exit(1);
    }

    template = `${language}-${type}`;
  }

  const root = path.resolve(appPath);

  if (!(await isWriteable(path.dirname(root)))) {
    console.error('The application path is not writable, please check folder permissions and try again.');
    console.error('It is likely you do not have write permissions for this folder.');
    process.exit(1);
  }

  const projectName = path.basename(root);
  await makeDir(root);
  if (!isFolderEmpty(root, projectName)) {
    process.exit(1);
  }

  const originalDirectory = process.cwd();

  console.log(`Creating a new Spring application in ${chalk.green(root)}.`);
  console.log();

  process.chdir(root);

  if (remoteUrl) {
    try {
      console.log(`Downloading files from repo ${chalk.cyan(remoteUrl)}. This might take a moment.`);
      console.log();
      await retry(() => downloadAndExtractRepo(root, repoInfo!), {
        retries: 3,
      });
    } catch (reason) {
      function isErrorLike(err: unknown): err is { message: string } {
        return typeof err === 'object' && err !== null && typeof (err as { message?: unknown }).message === 'string';
      }
      console.error(isErrorLike(reason) ? reason.message : reason + '');
      process.exit(1);
    }
    // Copy our default `.gitignore` if the application did not provide one
    const ignorePath = path.join(root, '.gitignore');
    if (!fs.existsSync(ignorePath)) {
      fs.copyFileSync(path.join(__dirname, 'templates', 'kotlin-webflux', 'gitignore'), ignorePath);
    }
  } else {
    await cpy('**', root, {
      parents: true,
      cwd: path.join(__dirname, '..', 'templates', template),
      rename: (name) => {
        switch (name) {
          case 'gitignore': {
            return '.'.concat(name);
          }
          default: {
            return name;
          }
        }
      },
    });
  }

  const packageName = kebabToPascal(projectName).toLowerCase();
  const applicationName = kebabToPascal(camelToPascal(projectName));
  renameRecursive(root, '{{PROJECT_NAME}}', projectName);
  renameRecursive(root, '{{PACKAGE_NAME}}', packageName);
  renameRecursive(root, '{{APPLICATION_NAME}}', applicationName);

  const files = glob.sync(`${root}/**`, { nodir: true });
  files.forEach((file) => {
    if (file.indexOf('.jar') !== -1) {
      return;
    }
    const data = fs.readFileSync(file, 'utf8');
    const result = Mustache.render(data, { PROJECT_NAME: projectName, PACKAGE_NAME: packageName, APPLICATION_NAME: applicationName });
    fs.writeFileSync(file, result, 'utf8');
  });

  if (tryGitInit(root)) {
    console.log('Initialized a git repository.');
    console.log();
  }

  let cdpath: string;
  if (path.join(originalDirectory, projectName) === appPath) {
    cdpath = projectName;
  } else {
    cdpath = appPath;
  }

  console.log(`${chalk.green('Success!')} Created ${projectName} at ${cdpath}`);
  console.log('Inside that directory, you can run several commands:');
  console.log();
  console.log(chalk.cyan(`  bloom generate subsystem <subsystem-name>`));
  console.log('    Generate new empty subsystem.');
  console.log(chalk.cyan(`  ./gradlew :app:bootRun`));
  console.log('    Starts the development server.');
  console.log(chalk.cyan(`  ./gradlew :app:bootJar`));
  console.log('    Builds the application for production.');
  console.log();
  console.log('We suggest that you begin by typing:');
  console.log();
  console.log(chalk.cyan('  cd'), cdpath);
  console.log(`  ${chalk.cyan(`./gradlew :app:bootRun`)}`);
  console.log();
};
