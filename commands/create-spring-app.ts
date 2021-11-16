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

export const runNewApp = async (projectPath: string) => {
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
  });
};

const createApp = async ({ appPath }: { appPath: string }) => {
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

  const template = `${language}-${type}`;

  const root = path.resolve(appPath);

  if (!(await isWriteable(path.dirname(root)))) {
    console.error('The application path is not writable, please check folder permissions and try again.');
    console.error('It is likely you do not have write permissions for this folder.');
    process.exit(1);
  }

  const appName = path.basename(root);
  await makeDir(root);
  if (!isFolderEmpty(root, appName)) {
    process.exit(1);
  }

  const originalDirectory = process.cwd();

  console.log(`Creating a new Spring application in ${chalk.green(root)}.`);
  console.log();

  process.chdir(root);

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

  const files = glob.sync(`${root}/**`, { nodir: true });
  files.forEach((file) => {
    const data = fs.readFileSync(file, 'utf8');
    const result = Mustache.render(data, { PROJECT_NAME: appName });
    fs.writeFileSync(file, result, 'utf8');
  });

  if (tryGitInit(root)) {
    console.log('Initialized a git repository.');
    console.log();
  }

  let cdpath: string;
  if (path.join(originalDirectory, appName) === appPath) {
    cdpath = appName;
  } else {
    cdpath = appPath;
  }

  console.log(`${chalk.green('Success!')} Created ${appName} at ${cdpath}`);
  console.log('Inside that directory, you can run several commands:');
  console.log();
  // console.log(chalk.cyan(`  bloom generate subsystem <subsystem-name>`));
  // console.log('    Generate empty subsystem.');
  console.log();
  console.log(chalk.cyan(`  ./gradlew :app:bootRun`));
  console.log('    Starts the development server.');
  console.log(chalk.cyan(`  ./gradlew :app:bootJar`));
  console.log('    Builds the application for production.');
  console.log();
};
