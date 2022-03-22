import CS from '../libraries/cs';
import fs from 'fs';
import path from 'path';

let aAllFilesPath = [];

let getAllFilePath = function (sRootFolder) {
  let aFolderOrFile = fs.readdirSync(sRootFolder);
  CS.forEach(aFolderOrFile, function (sFolderOrFile) {
    let sFileName = sRootFolder + sFolderOrFile;
    if (sFolderOrFile.includes('.')) {
      aAllFilesPath.push(sFileName);
    } else if (sFolderOrFile !== "devUtilities" && sFolderOrFile !== "themes" && sFolderOrFile !== "styleguide") {
      getAllFilePath(sFileName + "/");
    }
  });
};

let findUsageCount = function (aRootFilePath, sFilePath) {
  let iCount = 0;
  CS.forEach(aRootFilePath, function (sFilePathForSearch) {
    let sFileData = fs.readFileSync(sFilePathForSearch, "utf8");
    //let sFileName = path.basename(sFilepath);
    if (CS.includes(sFileData, (sFilePath + "').view;")) || CS.includes(sFileData, (sFilePath + "');"))) {
      iCount++;
    }
  });
  return iCount;
};
let getFilesPathForFindMaximumUse = function (sRootFolder) {
  let aFolderOrFile = fs.readdirSync(sRootFolder);
  CS.forEach(aFolderOrFile, function (sFolderOrFile) {
    if (sFolderOrFile.includes('.')) {
      aFilesPathForFindMaximumUse.push((sFolderOrFile.split('.')[0]));
    } else {
      let sFileName = sRootFolder + sFolderOrFile;
      getFilesPathForFindMaximumUse(sFileName + "/");
    }
  });
};

let aFilesPathForFindMaximumUse = []
getFilesPathForFindMaximumUse('../../app/viewlibraries/');
getAllFilePath('../../app/');
console.log(aFilesPathForFindMaximumUse.length);
let aFileData = [];
CS.forEach(aFilesPathForFindMaximumUse, function (sFilePath) {
  let iCount = findUsageCount(aAllFilesPath, sFilePath);
  let sFileName = path.basename(sFilePath);
  console.log("File Name: " + sFileName + "  Count: " + iCount);
  aFileData[sFileName] = iCount;
});

