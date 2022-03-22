import CS from '../libraries/cs';
import fs from 'fs';
import path from 'path';

let srcPath = path.resolve('../screens/homescreen/controller');

var aRootFilePath = [];
var getAllFilePath = (sRootFolder) => {
  var aFolderOrFile = fs.readdirSync(sRootFolder);

  CS.forEach(aFolderOrFile, (sFolderOrFile) => {
    var sFileName = sRootFolder + "/" + sFolderOrFile;
    if (sFolderOrFile.includes('.')) {
      aRootFilePath.push(sFileName);
    } else {
      if (!sFileName.includes('/themes/'))
        getAllFilePath(sFileName + "/");
    }
  });
};

let changeFileForCS = (srcPath) => {
  fs.readFile(srcPath, 'utf8', (err, data) => {
              if (err) throw err;
              let sFileData = data;

              //Change React.getSafeComponent
              let regexGetSafeComponent = /React.getSafeComponent\((\S+?)\)/g;
              sFileData = sFileData.replace(regexGetSafeComponent, '$1');

              //Add HOC for SafeComponent
              let regexClass = /(class \S+? extends React)/g;
              sFileData = sFileData.replace(regexClass, '@CS.SafeComponent\n$1');

              //ADD CS require
              let CSPath = path.relative(srcPath, '/Users/cs194/Documents/CS-Refactored/REST/src/main/webapp/gui/app/libraries/cs');
              CSPath = CSPath.replace(/^(\.\.\/)/, '');
              let regexReactRequire = /((var|const|let)\s+React)/;
              sFileData = sFileData.replace(regexReactRequire, "const CS = require('" + CSPath + "');\n$1");


              fs.writeFile(srcPath, sFileData, 'utf8', (err) => {
                if (err) throw err;
                console.log('Saved!');
              });


  });
}

let changeFilesWithContext = (srcPath) => {
  fs.readFile(srcPath, 'utf8', (err, data) => {
              if (err) throw err;
              let sFileData = data;
              let bAnyChanged = false;

              //Change childContextTypes
              let regexChildContextTypes = /(\S+?.childContextTypes\s*=\s*(\{([^}]+)\};?))/g;
              let aExecutedChildContextTypes = regexChildContextTypes.exec(sFileData);
              if(aExecutedChildContextTypes) {
                let sChildContextData = aExecutedChildContextTypes[2];
                sFileData = sFileData.replace(regexChildContextTypes, "");

                let regexClass = /(extends\s+React.Component\s*{)/g;
                sFileData = sFileData.replace(regexClass, '$1\n\n static childContextTypes = ' + sChildContextData);
                bAnyChanged = true;
              }


              //Change contextTypes
              let regexContextTypes = /(\S+?.contextTypes\s*=\s*(\{([^}]+)\};?))/g;
              let aExecutedContextTypes = regexContextTypes.exec(sFileData);
              if(aExecutedContextTypes) {
                let sContextTypeData = aExecutedContextTypes[2];
                sFileData = sFileData.replace(regexContextTypes, "");

                let regexClass = /(extends\s+React.Component\s*{)/g;
                sFileData = sFileData.replace(regexClass, '$1\n\n static contextTypes = ' + sContextTypeData);
                // console.log(sFileData);
                bAnyChanged = true;
              }

              if(bAnyChanged) {
                fs.writeFile(srcPath, sFileData, 'utf8', (err) => {
                  if (err) throw err;
                  console.log(srcPath);
                });
              }

              // //Change propTypes
              // let regexPropTypes = /(\S+?.propTypes\s+=\s+\S+?)/g;
              // let aExecutedPropTypes = regexPropTypes.exec(sFileData);
              // if(aExecutedPropTypes) {
              //   let sPropTypeData = aExecutedPropTypes[2];
              //   sFileData = sFileData.replace(regexPropTypes, "");
              //
              //   let regexClass = /(extends\s+React.Component\s*{)/g;
              //   sFileData = sFileData.replace(regexClass, '$1\n\n static propTypes = ' + sPropTypeData);
              //   console.log(sFileData);
              // }

  });
}

getAllFilePath(srcPath);

aRootFilePath.forEach(changeFileForCS);
