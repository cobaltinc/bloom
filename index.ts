#!/usr/bin/env node
/* eslint-disable import/no-extraneous-dependencies */
import chalk from 'chalk';
import { Command } from 'commander';
import checkForUpdate from 'update-check';
import { runNewApp } from './commands/create-spring-app';
import { runGenerateSubsystem } from './commands/generate-subsystem';
import packageJson from './package.json';

const program = new Command('bloom').version(packageJson.version);

program
  .command('new')
  .description('create new spring application')
  .arguments('<project-directory>')
  .usage(`${chalk.green('<project-directory>')} [options]`)
  .action(async (name) => {
    await runNewApp(name);
  });

const generate = program.command('generate').alias('g').description('generate [command]');

generate
  .command('subsystem')
  .alias('s')
  .description('generate new subsystem')
  .usage(`${chalk.green('<subsystem-name>')} [options]`)
  .arguments('<subsystem-name>')
  .action(async (name) => {
    await runGenerateSubsystem(name);
  });

const update = checkForUpdate(packageJson).catch(() => null);

async function run() {
  await program.parseAsync(process.argv);
}

async function notifyUpdate(): Promise<void> {
  try {
    const res = await update;
    if (res?.latest) {
      console.log();
      console.log(chalk.yellow.bold('A new version of `bloom` is available!'));
      console.log('You can update by running: `yarn global add bloom` or `npm i -g bloom`');
      console.log();
    }
    process.exit();
  } catch {
    // ignore error
  }
}

run()
  .then(notifyUpdate)
  .catch(async (reason) => {
    console.log();
    console.log('Aborting execution.');
    if (reason.command) {
      console.log(`  ${chalk.cyan(reason.command)} has failed.`);
    } else {
      console.log(chalk.red('Unexpected error. Please report it as a bug:'));
      console.log(reason);
    }
    console.log();

    await notifyUpdate();

    process.exit(1);
  });
