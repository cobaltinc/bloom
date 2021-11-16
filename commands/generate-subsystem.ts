import chalk from 'chalk';
import prompts from 'prompts';
import path from 'path';
import cpy from 'cpy';
import fs from 'fs';
import glob from 'glob';
import { makeDir } from '../helpers/make-dir';
import { isWriteable } from '../helpers/is-writeable';
import { isFolderEmpty } from '../helpers/is-folder-empty';

export const runGenerateSubsystem = async (subsystemName: string) => {
  if (typeof subsystemName === 'string') {
    subsystemName = subsystemName.trim();
  }
  if (!subsystemName) {
    const res = await prompts({
      type: 'text',
      name: 'path',
      message: 'What is new subsystem named?',
      validate: (name: string) => {
        if (name === '') {
          return 'Invalid subsystem name: Not blank';
        }
        return true;
      },
    });

    if (typeof res.path === 'string') {
      subsystemName = res.path.trim();
    }
  }

  if (!subsystemName) {
    console.log();
    console.log('Please specify the subsystem name:');
    console.log(`  ${chalk.cyan('bloom generate subsystem')} ${chalk.green('<subsystem-name>')}`);
    console.log();
    console.log('For example:');
    console.log(`  ${chalk.cyan('bloom generate subsystem')} ${chalk.green('payment')}`);
    console.log();
    console.log(`Run ${chalk.cyan(`${'bloom'} --help`)} to see all options.`);
    process.exit(1);
  }

  await generateSubsystem({
    subsystemName,
  });
};

const generateSubsystem = async ({ subsystemName }: { subsystemName: string }) => {
  const language = fs.existsSync('./build.gradle.kts') ? 'kotlin' : fs.existsSync('./build.gradle') ? 'java' : null;

  if (!language) {
    console.error('It does not seem to be the Spring project, please check folder and try again.');
    process.exit(1);
  }

  const root = path.resolve('subsystem', subsystemName);

  if (!(await isWriteable(path.dirname(root)))) {
    console.error('The subsystem path is not writable, please check folder permissions and try again.');
    console.error('It is likely you do not have write permissions for this folder.');
    process.exit(1);
  }

  await makeDir(root);
  if (!isFolderEmpty(root, subsystemName)) {
    process.exit(1);
  }

  const originalDirectory = process.cwd();

  console.log(`Creating a new Subsystem in ${chalk.green(root)}.`);
  console.log();

  process.chdir(root);

  await makeDir('component');
  await makeDir('component/src');
  await makeDir('component/src/main');
  await makeDir('component/src/test');

  await makeDir('interface');
  await makeDir('interface/src');
  await makeDir('interface/src/main');

  const data = `dependencies {
  implementation(project(":subsystem:${subsystemName}:${subsystemName}-interface"))
}`;

  if (language === 'kotlin') {
    fs.writeFileSync(`component/build.gradle.kts`, data);
    await makeDir('component/src/main/kotlin');
    await makeDir('component/src/test/kotlin');
    await makeDir('interface/src/main/kotlin');
  } else {
    fs.writeFileSync(`component/build.gradle`, data);
    await makeDir('component/src/main/java');
    await makeDir('component/src/test/java');
    await makeDir('interface/src/main/java');
  }

  let cdpath: string;
  if (path.join(originalDirectory, subsystemName) === root) {
    cdpath = subsystemName;
  } else {
    cdpath = root;
  }

  console.log(`${chalk.green('Success!')} Created ${subsystemName} at ${cdpath}`);
  console.log();
};
