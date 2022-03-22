import CS from '../libraries/cs';

/*var _ = require("lodash");
var fs = require('graceful-fs');
const aAllFileTypeNames = ['__en_US.json'];
let aFileType = ['content', 'dashboard', 'home', 'login', 'setting', 'view'];
var sUITranslationFolderPath = '../../../../resources/UITranslations/';

var startProcess = function () {
  let aValues = [];

  CS.forEach(aFileType, function (sFile) {
    console.log(sFile);
    CS.forEach(aAllFileTypeNames, function (sFileNameSuccessor) {
      var sFileName = sUITranslationFolderPath + sFile + sFileNameSuccessor;
      var oFileData = JSON.parse(fs.readFileSync(sFileName, 'utf8'));
      CS.forEach(oFileData, function (sValue, sKey) {
        aValues.push({
          key: sFile+ " " + sKey,
          value: sValue
        });
      });
    });
    });


  var aGroupped = CS.groupBy(aValues, 'value');
  let aDuplicates = CS.filter(aGroupped, function (oVal) {return CS.uniq(oVal.value)});
  var aDuplicateValues = CS.uniq(CS.flatten(CS.filter(aGroupped, function (sVal) {return sVal.length > 1})));

  CS.forEach(aDuplicateValues, function (oData) {
    console.log(oData.key + ":" + oData.value);
  });
  console.log(aDuplicateValues.length);

};

startProcess();*/

import fs from 'fs';
import path from 'path';

var aRootFilePath = [];

var getAllFilePath = function (sRootFolder) {
  var aFolderOrFile = fs.readdirSync(sRootFolder);

  CS.forEach(aFolderOrFile, function (sFolderOrFile) {
    var sFileName = sRootFolder + sFolderOrFile;
    if (sFolderOrFile.includes('.')) {
      aRootFilePath.push(sFileName);
    } else {
      if (!sFileName.includes('/themes/'))
        getAllFilePath(sFileName + "/");
    }
  });
};

var replaceDuplicateTranslation = function (sLine) {
  let sStringToFind = "PROPERTY_COLLECTIONS";
  let sStringToReplaceWith = "PROPERTY_COLLECTIONS";
  if (CS.includes(sLine, sStringToFind)) {
    let iPosition = sLine.search(sStringToFind);
    let iLength = sStringToFind.length;
    let iTotalLength = iPosition + iLength;
    let aToFind = [";", ",", "#", "'", ")", "}", "\""];
    let bIsReplace = false;
    CS.forEach(aToFind, function (iVal) {
      if (sLine[iTotalLength] === iVal) {
        bIsReplace = true;
      }
    });
    if (sLine.length === iTotalLength || bIsReplace) {
      return CS.replace(sLine, sStringToFind, sStringToReplaceWith);
    }
  }
  return sLine;
};

getAllFilePath('../');

CS.forEach(aRootFilePath, function (sFilePath) {
  let sLineToBeSaved = "";
  var sFileContent = fs.readFileSync(sFilePath, 'utf8');
  var aLines = sFileContent.split('\r\n');
  CS.forEach(aLines, function (sLine, iIndex) {
    if (iIndex === 0) {
      sLineToBeSaved += replaceDuplicateTranslation(sLine);
    }
    else {
      sLineToBeSaved += "\r\n" + replaceDuplicateTranslation(sLine);
    }
  });
  fs.writeFileSync(sFilePath, sLineToBeSaved, 'utf8');
});


