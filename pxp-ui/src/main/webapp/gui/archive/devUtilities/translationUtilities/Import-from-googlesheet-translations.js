/**
 * Created by CS99 on 06/09/2017.
 */
import fs from 'fs';
import readline from 'readline';
import google from 'googleapis';
import googleApiAuthenticate from './google-api-authentication';

import { ContentScreenTranslations as ContentScreenEnglishTranslation } from '../../screens/homescreen/screens/contentscreen/tack/content-screen-translations-en';
import { ContentScreenTranslations as ContentScreenDutchTranslation } from '../../screens/homescreen/screens/contentscreen/tack/content-screen-translations-de';
import { ContentScreenTranslations as ContentScreenSpanishTranslation } from '../../screens/homescreen/screens/contentscreen/tack/content-screen-translations-es';
import { ContentScreenTranslations as ContentScreenFrenchTranslation } from '../../screens/homescreen/screens/contentscreen/tack/content-screen-translations-fr';

import { SettingScreenTranslations as SettingScreenEnglishTranslation } from '../../screens/homescreen/screens/settingscreen/tack/setting-screen-translations-en';
import { SettingScreenTranslations as SettingScreenDutchTranslation } from '../../screens/homescreen/screens/settingscreen/tack/setting-screen-translations-de';
import { SettingScreenTranslations as SettingScreenSpanishTranslation } from '../../screens/homescreen/screens/settingscreen/tack/setting-screen-translations-es';
import { SettingScreenTranslations as SettingScreenFrenchTranslation } from '../../screens/homescreen/screens/settingscreen/tack/setting-screen-translations-fr';

import { HomeScreenTranslations as HomeScreenEnglishTranslation } from '../../screens/homescreen/tack/home-screen-translations-en';
import { HomeScreenTranslations as HomeScreenDutchTranslation } from '../../screens/homescreen/tack/home-screen-translations-de';
import { HomeScreenTranslations as HomeScreenSpanishTranslation } from '../../screens/homescreen/tack/home-screen-translations-es';
import { HomeScreenTranslations as HomeScreenFrenchTranslation } from '../../screens/homescreen/tack/home-screen-translations-fr';

import { ViewLibraryTranslations as ViewLibraryEnglishTranslation } from '../../viewlibraries/viewlibrarytranslations/view-library-translations-en';
import { ViewLibraryTranslations as ViewLibraryDutchTranslation } from '../../viewlibraries/viewlibrarytranslations/view-library-translations-de';
import { ViewLibraryTranslations as ViewLibrarySpanishTranslation } from '../../viewlibraries/viewlibrarytranslations/view-library-translations-es';
import { ViewLibraryTranslations as ViewLibraryFrenchTranslation } from '../../viewlibraries/viewlibrarytranslations/view-library-translations-fr';

function importTranslation (auth) {
  readSpreadsheet(auth, '17P5UA8pxfxLQ8Lpl-2Qek47CRX8E_LyFvGqdcfoYUQ4', 'HomeScreen', HomeScreenEnglishTranslation, HomeScreenDutchTranslation, HomeScreenSpanishTranslation, HomeScreenFrenchTranslation);
  readSpreadsheet(auth, '17P5UA8pxfxLQ8Lpl-2Qek47CRX8E_LyFvGqdcfoYUQ4', 'ContentScreen', ContentScreenEnglishTranslation, ContentScreenDutchTranslation, ContentScreenSpanishTranslation, ContentScreenFrenchTranslation);
  readSpreadsheet(auth, '17P5UA8pxfxLQ8Lpl-2Qek47CRX8E_LyFvGqdcfoYUQ4', 'SettingScreen', SettingScreenEnglishTranslation, SettingScreenDutchTranslation, SettingScreenSpanishTranslation, SettingScreenFrenchTranslation);
  readSpreadsheet(auth, '17P5UA8pxfxLQ8Lpl-2Qek47CRX8E_LyFvGqdcfoYUQ4', 'ViewLibrary', ViewLibraryEnglishTranslation, ViewLibraryDutchTranslation, ViewLibrarySpanishTranslation, ViewLibraryFrenchTranslation);
}

var readSpreadsheet = function (auth, spreadsheetId, sWorkSheet, oEnglish, oGerman, oSpanish, oFrench) {
  var sheets = google.sheets('v4');
  sheets.spreadsheets.values.get({
    auth: auth,
    spreadsheetId: spreadsheetId,
    range: sWorkSheet + '!A1:E'
  }, function (err, response) {
    var oEnglishTranslation = {};
    var oGermanTranslation = {};
    var oSpanishTranslation = {};
    var oFrenchTranslation = {};
    if (err) {
      console.log('The API returned an error: ' + err);
      return;
    }
    var rows = response.values;
    if (rows.length == 0) {
      console.log('No data found.');
    } else {
      var aHeaders = [];
      for (var iIndex = 0; iIndex < rows.length; iIndex++) {
        var row = rows[iIndex];
        if (iIndex == 0) {
          aHeaders = rows[iIndex];
        } else {
          oEnglishTranslation[row[0]] = row[1];
          oGermanTranslation[row[0]] = row[2];
          oSpanishTranslation[row[0]] = row[3];
          oFrenchTranslation[row[0]] = row[4];
        }
      }
      CS.assign(oEnglish, oEnglishTranslation);
      console.log("--------------------------------------------------" + sWorkSheet + " English (en): -----------------------------------------\n");
      for (var sKey in oEnglish) {
        // console.log("\"" + sKey + "\"" + " : " + "\"" + oEnglish[sKey] + "\",");
      }
      console.log(JSON.stringify(oEnglish, null, 2));
      writeOnTranslationFile(JSON.stringify(oEnglish, null, 2), sWorkSheet, 'en');

      CS.assign(oGerman, oGermanTranslation);
      console.log("--------------------------------------------------" + sWorkSheet + " German (de): -----------------------------------------\n");
      for (sKey in oGerman) {
        //console.log("\"" + sKey + "\"" + " : " + "\"" + oGerman[sKey] + "\",");
      }
      console.log(JSON.stringify(oGerman, null, 2));
      writeOnTranslationFile(JSON.stringify(oGerman, null, 2), sWorkSheet, 'de');
      CS.assign(oSpanish, oSpanishTranslation);
      console.log("--------------------------------------------------" + sWorkSheet + " Spanish (es): -----------------------------------------\n");
      for (sKey in oSpanish) {
        // console.log("\"" + sKey + "\"" + " : " + "\"" + oSpanish[sKey] + "\",");
      }
      console.log(JSON.stringify(oSpanish, null, 2));
      writeOnTranslationFile(JSON.stringify(oSpanish, null, 2), sWorkSheet, 'es');
      CS.assign(oFrench, oFrenchTranslation);
      console.log("--------------------------------------------------" + sWorkSheet + " French (fr): -----------------------------------------\n");
      for (sKey in oFrench) {
        //  console.log("\"" + sKey + "\"" + " : " + "\"" + oFrench[sKey] + "\",");
      }
      console.log(JSON.stringify(oFrench, null, 2));
      writeOnTranslationFile(JSON.stringify(oFrench, null, 2), sWorkSheet, 'fr');
    }
  });
};

var writeOnTranslationFile = function (sTranslationData, sWorkSheet, sLanguage) {
  var sPrefix = '';
  var sPath = '';
  if (sWorkSheet == 'HomeScreen') {
    sPrefix = 'exports.HomeScreenTranslations = ';
    sPath = '../../screens/homescreen/tack/home-screen-translations-';
  }
  else if (sWorkSheet == 'ContentScreen') {
    sPrefix = 'exports.ContentScreenTranslations = ';
    sPath = '../../screens/homescreen/screens/contentscreen/tack/content-screen-translations-';
  }
  else if (sWorkSheet == 'SettingScreen') {
    sPrefix = 'exports.SettingScreenTranslations = ';
    sPath = '../../screens/homescreen/screens/settingscreen/tack/setting-screen-translations-';
  }
  else if (sWorkSheet == 'ViewLibrary') {
    sPrefix = 'exports.ViewLibraryTranslations = ';
    sPath = '../../viewlibraries/viewlibrarytranslations/view-library-translations-';
  }

  sPath = sPath + sLanguage + '.js';
  var sUpdateTranslation = sPrefix + sTranslationData;
  console.log(sUpdateTranslation);
  fs.writeFile(sPath, sUpdateTranslation, function (err) {
    if (err) throw err;
  });
};

googleApiAuthenticate(importTranslation);