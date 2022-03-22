/**
 * Created by CS99 on 13/09/2017.
 */
/**
 * Created by CS99 on 12/09/2017.
 */
import CS from '../libraries/cs';
import fs from 'fs';
import path from 'path';
var aFiles = [];
var aRootFilePath = [];
var aFilePath = [];
var oIncludedFileMap = {};
var aUnUsedFilesPath = [];
var aUnUsedFiles = [];

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

var getFilesToSearch = function (sRootFolder) {
  var aFolderOrFile = fs.readdirSync(sRootFolder);
  CS.forEach(aFolderOrFile, function (sFolderOrFile) {
    var sFileName = sRootFolder + sFolderOrFile;
    if (sFolderOrFile.includes('.')) {
      if (!sFolderOrFile.includes('.html')) {
        aFiles.push(sFolderOrFile);
        aFilePath.push(sFileName);
      }
    } else {
      getFilesToSearch(sFileName + "/");
    }
  });

};

var createIncludedFileMap = function (filePath) {

  var sFileContent = fs.readFileSync(filePath, 'utf8');
  var aLines = sFileContent.split('\n');
  var aRequiredFiles = [];
  var sFile = path.basename(filePath);
  sFile = sFile.includes('.jsx') ? sFile.slice(0, sFile.length - 4)
      : sFile.includes('.js') ? sFile.slice(0, sFile.length - 3) : sFile;
  aRequiredFiles.push(sFile);
  CS.forEach(aLines, function (sLine) {
    if ((sLine.includes('require(') || sLine.includes('Path')) && !sLine.trim().startsWith('//') && !sLine.trim().startsWith('/*')) {
      var sRequireFile;
      if (sLine.indexOf("'") != -1) {
        sRequireFile = path.basename(sLine.slice(sLine.indexOf("'") + 1, sLine.indexOf("')")));
        sRequireFile = sRequireFile.includes('.jsx') ? sRequireFile.slice(0, sRequireFile.length - 4)
            : sRequireFile.includes('.js') ? sRequireFile.slice(0, sRequireFile.length - 3) : sRequireFile;
        aRequiredFiles.push(sRequireFile);
        //console.log(sRequireFile);
      } else if (sLine.indexOf("\"") != -1) {
        sRequireFile = path.basename(sLine.slice(sLine.indexOf("\"") + 1, sLine.indexOf("\")")));
        sRequireFile = sRequireFile.includes('.jsx') ? sRequireFile.slice(0, sRequireFile.length - 4)
            : sRequireFile.includes('.js') ? sRequireFile.slice(0, sRequireFile.length - 3) : sRequireFile;
        aRequiredFiles.push(sRequireFile);
        //console.log(sRequireFile);
      }
    }
  });
  oIncludedFileMap[filePath] = aRequiredFiles;

};

var searchFiles = function () {

  getFilesToSearch('../screens/homescreen/');
  getFilesToSearch('../screens/loginscreen/');
  //getFilesToSearch('../viewlibraries/');
  getFilesToSearch('../libraries/');
  getFilesToSearch('../html/');
  getFilesToSearch('../commonmodule/');

  getAllFilePath('../');
  getAllFilePath('../../appconfig/');

  CS.forEach(aRootFilePath, function (sRootFile) {
    createIncludedFileMap(sRootFile);
  });

  for (var iIndex = 0; iIndex < aFiles.length; iIndex++) {
    var sFile = aFiles[iIndex];
    var sFileToSearch = '';
    if (sFile.includes('.jsx') || sFile.includes('.css')) {
      sFileToSearch = sFile.slice(0, sFile.length - 4)
    } else if (sFile.includes('.js')) {
      sFileToSearch = sFile.slice(0, sFile.length - 3);
    } else {
      sFileToSearch = sFile;
    }
    var isFileIncluded = false;

    CS.forEach(oIncludedFileMap, function (aIncludedFiles, sKey) {
      var aIncludedRootFiles = [];
      for (var iIndex = 1; iIndex < aIncludedFiles.length; iIndex++) {
        //console.log(aIncludedFiles[iIndex]);
        if (aIncludedFiles[iIndex].match(sFileToSearch)) {
          if (CS.indexOf(aIncludedRootFiles, aIncludedFiles[0]) == -1) {
            aIncludedRootFiles.push(aIncludedFiles[0]);
            isFileIncluded = true;
            break;
          }
        }
      }
    });
    if (!isFileIncluded) {
      if (!aFilePath[iIndex].includes('translations') && !aFilePath[iIndex].includes('.json')) {
        //console.log(sFileToSearch);
        aUnUsedFiles.push(sFileToSearch);
        aUnUsedFilesPath.push(aFilePath[iIndex]);
      }
    }
  }
  var aSearchedFile = [];
  CS.forEach(oIncludedFileMap, function (aIncludedFiles, sKey) {
    for (var iIndex = 1; iIndex < aIncludedFiles.length; iIndex++) {
      var aRootFiles = [];
      var sIncludedFile = aIncludedFiles[iIndex];
      if(CS.indexOf(aSearchedFile, sIncludedFile) == -1) {
        CS.forEach(oIncludedFileMap, function (aIncludedFiles, sKey) {
          for (var iIndex = 1; iIndex < aIncludedFiles.length; iIndex++) {
            if (aIncludedFiles[iIndex] == sIncludedFile) {
              aRootFiles.push(aIncludedFiles[0]);
            }
          }
        });
      }
      var isUnusedFile = false;
      if(aRootFiles.length<0)
        isUnusedFile = true;
      var count = 0;
      CS.forEach(aRootFiles, function (sRootFile) {
        if(CS.indexOf(aUnUsedFiles, sRootFile) != -1){
          count++;
        }
      });

      if(aRootFiles.length == count && aRootFiles.length!=0){
        CS.forEach(oIncludedFileMap, function (aIncludedFiles, sKey) {
          if(sKey.includes(sIncludedFile+'.')){
            //console.log(sKey);
            if(CS.indexOf(aUnUsedFilesPath, sKey) == -1) {
              aUnUsedFiles.push(sIncludedFile);
              aUnUsedFilesPath.push(sKey);
            }
          }
        });
      }
      aSearchedFile.push(sIncludedFile);
    }
  });
  CS.forEach(aUnUsedFilesPath, function (sRootFile) {
    console.log(sRootFile.slice(3,sRootFile.length));

    /**this logic is used to move the unused files to the destination path specified*/

    let aDirs = (path.dirname(sRootFile.slice(3,sRootFile.length))).split('/');
    let sDirPath = "../unusedbin/unusedFiles";
    CS.forEach(aDirs, function (sDirName) {
      sDirPath = sDirPath + "/" +sDirName;
      try{
        fs.mkdirSync(sDirPath);
      } catch(e) {};
    });
    fs.renameSync(sRootFile, '../unusedbin/unusedFiles/' + sRootFile.slice(3,sRootFile.length));
  });
};

searchFiles();