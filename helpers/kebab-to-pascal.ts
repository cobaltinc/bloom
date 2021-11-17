export const kebabToPascal = (kebab: string) => kebab.replace(/(^\w|-\w)/g, (s) => s.replace(/-/, '').toUpperCase());
