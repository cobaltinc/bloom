import fs from 'fs';
import path from 'path';

export const renameRecursive = (dir: string, from: string, to: string) => {
  fs.readdirSync(dir).forEach((it) => {
    const itsPath = path.resolve(dir, it);
    const itsStat = fs.statSync(itsPath);

    if (itsPath.search(from) > -1) {
      fs.renameSync(itsPath, itsPath.replace(from, to));
    }

    if (itsStat.isDirectory()) {
      renameRecursive(itsPath.replace(from, to), from, to);
    }
  });
};
