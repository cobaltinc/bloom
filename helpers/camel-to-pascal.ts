export const camelToPascal = (camel: string) =>
  camel.replace(/\w+/g, (w) => {
    return w[0].toUpperCase() + w.slice(1).toLowerCase();
  });
