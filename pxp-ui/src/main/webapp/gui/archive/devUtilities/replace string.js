/*
  This script will replace all occurances of a string from all files in the provided folder

  Required - path/of/the/folder
*/
import CS from '../libraries/cs';
import fs from 'fs';
import path from 'path';

let srcPath = path.resolve('../screens/homescreen/screens/settingscreen/store/helper');

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

/*
* Steps
*
* 1. Find the string according to regex
* 2. Copy the internal string - using '$1'
* 3. Replace the outer string by concatenating with internal string
* */

let changeFileForCS = (srcPath) => {
    fs.readFile(srcPath, 'utf8', (err, data) => {
        if (err) throw err;
        let sFileData = data;

        let regexStringToReplace = /_\.csCustom(Get|Post|Put|Delete)Request\(/g;
        sFileData = sFileData.replace(regexStringToReplace, 'SettingUtils.csCustom'+'$1'+'Request(');

        fs.writeFile(srcPath, sFileData, 'utf8', (err) => {
            if (err) throw err;
            console.log('Saved!');
        });


    });
};

getAllFilePath(srcPath);

aRootFilePath.forEach(changeFileForCS);
