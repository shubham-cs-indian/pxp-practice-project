/**
 * Created by CS99 on 07/09/2017.
 */
import fs from 'fs';
import readline from 'readline';
import google from 'googleapis';
import googleApiAuthenticate from './google-api-authentication';

import ContentScreenEnglishTranslation from '../../../../../resources/UITranslations/content__en_US';
import ContentScreenEnglish_UKTranslation from '../../../../../resources/UITranslations/content__en_UK';
import ContentScreenDutchTranslation from '../../../../../resources/UITranslations/content__de_DE';
import ContentScreenSpanishTranslation from '../../../../../resources/UITranslations/content__es_ES';
import ContentScreenFrenchTranslation from '../../../../../resources/UITranslations/content__fr_FR';

import SettingScreenEnglishTranslation from '../../../../../resources/UITranslations/setting__en_US';
import SettingScreenEnglish_UKTranslation from '../../../../../resources/UITranslations/setting__en_UK';
import SettingScreenDutchTranslation from '../../../../../resources/UITranslations/setting__de_DE';
import SettingScreenSpanishTranslation from '../../../../../resources/UITranslations/setting__es_ES';
import SettingScreenFrenchTranslation from '../../../../../resources/UITranslations/setting__fr_FR';

import HomeScreenEnglishTranslation from '../../../../../resources/UITranslations/home__en_US';
import HomeScreenFrenchUKTranslation from '../../../../../resources/UITranslations/home__en_UK';
import HomeScreenDutchTranslation from '../../../../../resources/UITranslations/home__de_DE';
import HomeScreenSpanishTranslation from '../../../../../resources/UITranslations/home__es_ES';
import HomeScreenFrenchTranslation from '../../../../../resources/UITranslations/home__fr_FR';

import ViewLibraryEnglishTranslation from '../../../../../resources/UITranslations/view__en_US';
import ViewLibraryEnglish_UKTranslation from '../../../../../resources/UITranslations/view__en_UK';
import ViewLibraryDutchTranslation from '../../../../../resources/UITranslations/view__de_DE';
import ViewLibrarySpanishTranslation from '../../../../../resources/UITranslations/view__es_ES';
import ViewLibraryFrenchTranslation from '../../../../../resources/UITranslations/view__fr_FR';

import DashboardEnglishTranslation from '../../../../../resources/UITranslations/dashboard__en_US';
import DashboardEnglish_UKTranslation from '../../../../../resources/UITranslations/dashboard__en_UK';
import DashboardDutchTranslation from '../../../../../resources/UITranslations/dashboard__de_DE';
import DashboardSpanishTranslation from '../../../../../resources/UITranslations/dashboard__es_ES';
import DashboardFrenchTranslation from '../../../../../resources/UITranslations/dashboard__fr_FR';

function exportTranslations (auth) {
  // var sSpreadsheetId = '1KN54j_1NOWtbHhXYPYVPNelGCjIH9Alzel-yWC63vzA';
  var sSpreadsheetId = '17P5UA8pxfxLQ8Lpl-2Qek47CRX8E_LyFvGqdcfoYUQ4';//original
  readSpreadsheet(auth, sSpreadsheetId, 'HomeScreen', HomeScreenEnglishTranslation, HomeScreenFrenchUKTranslation, HomeScreenDutchTranslation, HomeScreenSpanishTranslation, HomeScreenFrenchTranslation);
  readSpreadsheet(auth, sSpreadsheetId, 'ContentScreen', ContentScreenEnglishTranslation, ContentScreenEnglish_UKTranslation,  ContentScreenDutchTranslation, ContentScreenSpanishTranslation, ContentScreenFrenchTranslation);
  readSpreadsheet(auth, sSpreadsheetId, 'SettingScreen', SettingScreenEnglishTranslation, SettingScreenEnglish_UKTranslation, SettingScreenDutchTranslation, SettingScreenSpanishTranslation, SettingScreenFrenchTranslation);
  readSpreadsheet(auth, sSpreadsheetId, 'ViewLibrary', ViewLibraryEnglishTranslation, ViewLibraryEnglish_UKTranslation, ViewLibraryDutchTranslation, ViewLibrarySpanishTranslation, ViewLibraryFrenchTranslation);
  readSpreadsheet(auth, sSpreadsheetId, 'dashboard', DashboardEnglishTranslation, DashboardEnglish_UKTranslation, DashboardDutchTranslation, DashboardSpanishTranslation, DashboardFrenchTranslation);
}

var readSpreadsheet = function (auth, spreadsheetId, sWorkSheet, oEnglish, oEnglish_Uk, oGerman, oSpanish, oFrench) {
  var sheets = google.sheets('v4');
  sheets.spreadsheets.values.get({
    auth: auth,
    spreadsheetId: spreadsheetId,
    range: sWorkSheet + '!A2:F'
  }, function (err, response) {
    if (err) {
      console.log('The API returned an error: ' + err);
      return;
    }
    var rows = response.values ? response.values : [];
      var aSpreadSheetData = [];
      aSpreadSheetData.push(['Key', 'English_US', 'English_UK', 'German', 'Spanish', 'French']);
      for (var sKey in oEnglish) {
        var flag = false;
        for (var iIndex = 0; iIndex < rows.length; iIndex++) {
          var aRow = rows[iIndex];
          if (aRow[0] == sKey) {
            aSpreadSheetData.push(aRow);
            flag = true;
            break;
          }
        }
        if (!flag) {
          var aTranslationData = [];
          aTranslationData[0] = sKey;
          aTranslationData[1] = oEnglish[sKey];
          aTranslationData[2] = oEnglish_Uk[sKey];
          aTranslationData[3] = oGerman[sKey];
          aTranslationData[4] = oSpanish[sKey];
          aTranslationData[5] = oFrench[sKey];
          aSpreadSheetData.push(aTranslationData);
        }
      }
      console.log(aSpreadSheetData.length);
      clearSheet(auth, spreadsheetId, sWorkSheet, aSpreadSheetData);
  });
};

var clearSheet = function (auth, spreadsheetId, sWorkSheet, aSpreadSheetData) {
  var sheets = google.sheets('v4');
  sheets.spreadsheets.values.clear({
    auth: auth,
    spreadsheetId: spreadsheetId,
    range: sWorkSheet + '!A1:E', //Change Sheet1 if your worksheet's name is something else
    resource: {}
  }, function (err, response) {
    if (err) {
      console.error(err);
      return;
    }
    writeToSheet(auth, spreadsheetId, sWorkSheet, aSpreadSheetData)
  });
};

var writeToSheet = function (auth, spreadsheetId, sWorkSheet, aSpreadSheetData) {

  var sheets = google.sheets('v4');
  //sheets.getRows();
  sheets.spreadsheets.values.append({
    auth: auth,
    spreadsheetId: spreadsheetId,
    range: sWorkSheet + '!A1:B', //Change Sheet1 if your worksheet's name is something else
    valueInputOption: "USER_ENTERED",
    resource: {
      values: aSpreadSheetData
    }
  }, (err, response) => {
    if (err) {
      console.log('The API returned an error: ' + err);
      return;
    } else {
      console.log(aSpreadSheetData.length + " rows Appended for " + sWorkSheet);
    }
  });
};

googleApiAuthenticate(exportTranslations);