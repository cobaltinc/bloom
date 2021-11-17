<h1 align='center'>
  Bloom 🌼
</h1>

<p align="center"><strong>Create Spring application with one command built by <a href="https://cobalt.run">Cobalt, Inc.</a></strong></p>

<p align='center'>
  <a href="https://cobalt.run">
    <img src="https://badgen.net/badge/icon/Made%20by%20Cobalt?icon=https://caple-static.s3.ap-northeast-2.amazonaws.com/cobalt-badge.svg&label&color=5B69C3&labelColor=414C9A" />
  </a>
  <a href='https://www.npmjs.com/package/@cobaltinc/bloom'>
    <img src='https://img.shields.io/npm/v/@cobaltinc/bloom.svg' alt='Latest npm version'>
  </a>
  <a href="https://github.com/cobaltinc/bloom/blob/master/.github/CONTRIBUTING.md">
    <img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" alt="PRs welcome" />
  </a>
</p>

## :rocket: Getting started

```bash
npm i -g @cobaltinc/bloom
```

<img src="https://user-images.githubusercontent.com/3623695/141818097-1890328d-56a0-4717-852a-546e9153ed11.gif" width="500px" />

## :sparkles: Commands

### `new <project-name>`
> Create new spring application.

#### Options
| Option | Type | Description |
| ------ | ---- | ----------- |
| -r, --remote | string | GitHub repository url for custom template. |
| -t, --token | string | To use the private repository, you need to set up GitHub Personal access token. |

### `generate subsystem <subsystem-name>`
> Generate new subsystem in application.

Make directories and build file in subsystem:
```
- subsystem
  - <subsystem-name>
    - component
      - src
        - main
        - test
      - build.gradle[.kts]
    - interface
      - src
        - main
        - test
```

## :bookmark: Custom Template

You can create custom templates for new projects. For help creating a new template, see the [templates]('./templates) directory.

### Usage

```bash
bloom new <project-name> --remote https://github.com/kciter/sample-spring-template.git
```

### Interpolation

#### Default

| Params | Type | Description |
| ------ | ---- | ----------- |
| PROJECT_NAME | string | The project name you entered. |
| PACKAGE_NAME | string | Convert the project name to lower case. |
| APPLICATION_NAME | string | Convert the project name to PascalCase. Typically used for `Application` class. |

### Extension



## :page_facing_up: License

Bloom is made available under the [MIT License](./LICENSE).
