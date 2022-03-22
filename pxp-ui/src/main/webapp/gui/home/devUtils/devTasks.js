const fs = require('fs');
const walkSync = require('walk-sync');
const BASE_PATH = '../src/';
const paths = walkSync(BASE_PATH);

const getPropsFromString = (propString) => {
  const props = [];
  const strings = propString.split('\n');
  strings.map((str) => {
    if(str.indexOf(":") !== -1){
      props.push(str.slice(0, str.indexOf(":")).trim());
    }
  });
  return props;
};

const getEventsFromString = (eventsString) => {
  return Function('"use strict";return (' + eventsString + ')')();
};

const getUnunsedProps = () => {

  paths.map((path) => {
    if (path.indexOf('.js') !== -1 || path.indexOf('.jsx') !== -1) {
      const fullPath = BASE_PATH + path;
      const fileContents = fs.readFileSync(fullPath, 'utf8');

      const propStrings = fileContents.match(/const oPropTypes[ ]*=[ ]*{[a-z A-Z 0-9:,\n\.(){}\[\]\/=-]*};/g);

      if(propStrings && propStrings.length > 0){
        const props = getPropsFromString(propStrings[0]);

        props.map((prop) => {
          if(fileContents.indexOf(`.${prop}`) === -1){
            console.log(`${prop} usage missing in ${path}`);
          }
        });
      } else {
        console.log(`-----------Props not found in ${path}---------`);
      }
    }
  });
};

const getUnunsedEvents = () => {

  paths.map((path) => {
    if (path.indexOf('.js') !== -1 || path.indexOf('.jsx') !== -1) {
      const fullPath = BASE_PATH + path;
      const fileContents = fs.readFileSync(fullPath, 'utf8');

      const eventsStrings = fileContents.match(/const oEvents[ ]*=[ ]*{[a-z A-Z 0-9:,_"\n\.\/=-]*}/g);

      if(eventsStrings && eventsStrings.length > 0){
        const events = getEventsFromString(eventsStrings[0].replace('const oEvents = ', ''));

        Object.keys(events).map((event) => {
          if(fileContents.indexOf(`.${event}`) === -1){
            console.log(`${event} usage missing in ${path}`);
          }
        });
      } else {
        console.log(`-----------Events not found in ${path}---------`);
      }
    }
  });
};

const renameJsxToJs = () => {

  paths.map((path) => {
    if (path.indexOf('.jsx') !== -1) {
      console.log("Converting " + path);

      const fullPath = BASE_PATH + path;
      fs.rename(fullPath, fullPath.substr(0, fullPath.length-1), () => {});
    }
  });
};


renameJsxToJs();
