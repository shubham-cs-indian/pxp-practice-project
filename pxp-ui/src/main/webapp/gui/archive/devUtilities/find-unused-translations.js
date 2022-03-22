/**
 * Created by CS159 on 05-04-2018.
 */
import CS from '../libraries/cs';
import fs from 'fs';
import path from 'path';
import ContentScreenUSTranslations from '../../../../resources/UITranslations/content__en_US';
import DashboardScreenUSTranslations from '../../../../resources/UITranslations/dashboard__en_US';
import HomeScreenUSTranslations from '../../../../resources/UITranslations/home__en_US';
import LoginScreenUSTranslations from '../../../../resources/UITranslations/login__en_US';
import SettingScreenUSTranslations from '../../../../resources/UITranslations/setting__en_US';
import ViewLibrariesUSTranslations from '../../../../resources/UITranslations/view__en_US';


let aRootFilePath = [];
let aTranslationKeys = [];


let getAllFilePath = function (sRootFolder) {
  let aFolderOrFile = fs.readdirSync(sRootFolder);
  CS.forEach(aFolderOrFile, function (sFolderOrFile) {
    let sFileName = sRootFolder + sFolderOrFile;
    if (sFolderOrFile.includes('.')) {
      aRootFilePath.push(sFileName);
    } else if ( sFolderOrFile !== "devUtilities" && sFolderOrFile !== "themes" && sFolderOrFile !== "styleguide" /* && sFolderOrFile !== "contentscreen" && sFolderOrFile !== "settingscreen" && sFolderOrFile !=="dashboardscreen"*/) {
      getAllFilePath(sFileName + "/");
    }
  });
};

/* Remove used Translation Keys From 'aTranslationKeys' array*/
let findUnusedTranslations = function (aRootFilePath) {
  CS.forEach(aRootFilePath, function (sFilepath) {
    let sFileData = fs.readFileSync(sFilepath, "utf8");
    //let sFileName = path.basename(sFilepath);
    CS.remove(aTranslationKeys, function (key) {

      return (checkTranslation(key, sFileData));
    })

  });
};

/* Return true when translation is used in file*/
function checkTranslation (key, sFileData) {
  let myStartReg = "[.|[|[\"|[']";
  let myEndReg = "[^a-z|A-Z|_|0-9]";
  return RegExp(myStartReg + key + myEndReg).test(sFileData)
}

function findTranslationKeys (oUSTranslations) {
  for (let sKey in oUSTranslations) {
    aTranslationKeys.push(sKey);
  }
}

let getAllUnusedTranslation = function (sRootFolder) {
  aRootFilePath = [];
  getAllFilePath(sRootFolder);
  findUnusedTranslations(aRootFilePath);
};
/*
console.log("---------------------- Home Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(HomeScreenUSTranslations);
getAllUnusedTranslation("../../app/");
console.log(aTranslationKeys);*/

console.log("---------------------- Content Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(ContentScreenUSTranslations);
getAllUnusedTranslation("../../app/");
console.log(aTranslationKeys);

//console.log(aTranslationKeys);

/*
console.log("---------------------- Setting Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(SettingScreenUSTranslations);
getAllUnusedTranslation("../../app/");
console.log(aTranslationKeys);

console.log("---------------------- Login Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(LoginScreenUSTranslations);
getAllUnusedTranslation("../../app/");
console.log(aTranslationKeys);

console.log("---------------------- Dashboard Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(DashboardScreenUSTranslations);
getAllUnusedTranslation("../../app/");
console.log(aTranslationKeys);

console.log("---------------------- ViewLibraries Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(ViewLibrariesUSTranslations);
getAllUnusedTranslation("../../app/");
console.log(aTranslationKeys);
*/

/*
// Search translations Screen wise

console.log("---------------------- ContentScreen Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(ContentScreenUSTranslations);
getAllUnusedTranslation("../screens/homescreen/screens/contentscreen/");
getAllUnusedTranslation("../commonmodule/");
console.log(aTranslationKeys);


console.log("---------------------- SettingScreen Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(SettingScreenUSTranslations);
getAllUnusedTranslation("../screens/homescreen/screens/settingscreen/");
getAllUnusedTranslation("../commonmodule/");
console.log(aTranslationKeys);


console.log("---------------------- LoginScreen Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(LoginScreenUSTranslations);
getAllUnusedTranslation("../screens/loginscreen/");
getAllUnusedTranslation("../commonmodule/");
console.log(aTranslationKeys);


console.log("---------------------- Dashboard Screen Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(DashboardScreenUSTranslations);
getAllUnusedTranslation("../screens/homescreen/screens/contentscreen/screens/dashboardscreen/");
getAllUnusedTranslation("../commonmodule/");
console.log(aTranslationKeys);


console.log("---------------------- View Libraries Unused Translations------------------------");
aTranslationKeys = [];
findTranslationKeys(ViewLibrariesUSTranslations);
getAllUnusedTranslation("../viewlibraries/");
getAllUnusedTranslation("../commonmodule/");
console.log(aTranslationKeys);
*/
