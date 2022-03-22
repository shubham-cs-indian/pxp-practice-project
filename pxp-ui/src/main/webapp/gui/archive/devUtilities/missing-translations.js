//To find missing translations, run this file in node terminal

import ContentUS from '../../../../resources/UITranslations/content__en_US';
import ContentDE from '../../../../resources/UITranslations/content__de_DE';
import ContentUK from '../../../../resources/UITranslations/content__en_UK';
import ContentES from '../../../../resources/UITranslations/content__es_ES';
import ContentFR from '../../../../resources/UITranslations/content__fr_FR';

import DashboardUS from '../../../../resources/UITranslations/dashboard__en_US';
import DashboardDE from '../../../../resources/UITranslations/dashboard__de_DE';
import DashboardUK from '../../../../resources/UITranslations/dashboard__en_UK';
import DashboardES from '../../../../resources/UITranslations/dashboard__es_ES';
import DashboardFR from '../../../../resources/UITranslations/dashboard__fr_FR';

import HomeUS from '../../../../resources/UITranslations/home__en_US';
import HomeDE from '../../../../resources/UITranslations/home__de_DE';
import HomeUK from '../../../../resources/UITranslations/home__en_UK';
import HomeES from '../../../../resources/UITranslations/home__es_ES';
import HomeFR from '../../../../resources/UITranslations/home__fr_FR';

import LoginUS from '../../../../resources/UITranslations/login__en_US';
import LoginDE from '../../../../resources/UITranslations/login__de_DE';
import LoginUK from '../../../../resources/UITranslations/login__en_UK';
import LoginES from '../../../../resources/UITranslations/login__es_ES';
import LoginFR from '../../../../resources/UITranslations/login__fr_FR';

import SettingUS from '../../../../resources/UITranslations/setting__en_US';
import SettingDE from '../../../../resources/UITranslations/setting__de_DE';
import SettingUK from '../../../../resources/UITranslations/setting__en_UK';
import SettingES from '../../../../resources/UITranslations/setting__es_ES';
import SettingFR from '../../../../resources/UITranslations/setting__fr_FR';

import ViewUS from '../../../../resources/UITranslations/view__en_US';
import ViewDE from '../../../../resources/UITranslations/view__de_DE';
import ViewUK from '../../../../resources/UITranslations/view__en_UK';
import ViewES from '../../../../resources/UITranslations/view__es_ES';
import ViewFR from '../../../../resources/UITranslations/view__fr_FR';


let findMissingTranslationContentScreen = function (oUSTranslation, oGermanTranslation, oSpanishTranslation, oFrenchTranslation, oUKTranslation, sScreenName) {
  console.log();
  console.log("--------------------------------------------------" + sScreenName + " screen German (de): -----------------------------------------\n");
  for (let sKey in oUSTranslation) {
    // console.log(sKey);
    if (!oGermanTranslation.hasOwnProperty(sKey)) {
      console.log("\"" + sKey + "\"" + " : " + "\"" + oUSTranslation[sKey] + "_de\",");
    }
  }

  console.log();
  console.log("---------------------------------------------------" + sScreenName + " screen Spanish (es): -------------------------------------\n");
  for (let sKey in oUSTranslation) {
    if (!oSpanishTranslation.hasOwnProperty(sKey)) {
      console.log("\"" + sKey + "\"" + " : " + "\"" + oUSTranslation[sKey] + "_es\",");
    }
  }

  console.log();
  console.log("-------------------------------------------------" + sScreenName + " screen French (fr): -------------------------------------\n");
  for (let sKey in oUSTranslation) {
    if (!oFrenchTranslation.hasOwnProperty(sKey)) {
      console.log("\"" + sKey + "\"" + " : " + "\"" + oUSTranslation[sKey] + "_fr\",");
    }
  }

  console.log();
  console.log("-------------------------------------------------" + sScreenName + " screen UK : -------------------------------------\n");
  for (let sKey in oUSTranslation) {
    if (!oUKTranslation.hasOwnProperty(sKey)) {
      console.log("\"" + sKey + "\"" + " : " + "\"" + oUSTranslation[sKey] + "\",");
    }
  }

};

findMissingTranslationContentScreen(ContentUS, ContentDE, ContentES, ContentFR, ContentUK, "Content");
findMissingTranslationContentScreen(DashboardUS, DashboardDE, DashboardES, DashboardFR, DashboardUK, "Dashboard");
findMissingTranslationContentScreen(HomeUS, HomeDE, HomeES, HomeFR, HomeUK, "Home");
findMissingTranslationContentScreen(LoginUS, LoginDE, LoginES, LoginFR, LoginUK, "Login");
findMissingTranslationContentScreen(SettingUS, SettingDE, SettingES, SettingFR, SettingUK, "Setting");
findMissingTranslationContentScreen(ViewUS, ViewDE, ViewES, ViewFR, ViewUK, "View");

