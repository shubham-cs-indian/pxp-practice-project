/**
 * Created by DEV on 07-01-2016.
 */
// var oTranslations = require('./../libraries/translations/logintranslations');
import './../libraries/globalloader/global-loader';
import UniqueIdentifierGenerator from './../libraries/uniqueidentifiergenerator/unique-identifier-generator.js';
//require('../commonmodule/store/helper/translation-manager.js').init();
import alertify from './../commonmodule/store/custom-alertify-store';
import {getTranslations} from "./../commonmodule/store/helper/translation-manager.js";

import LogFactory from './../libraries/logger/log-factory';
import LoginScreenController from "./../screens/loginscreen/controller/login-screen-controller.jsx";
import LoginAction from "./../screens/loginscreen/action/login-screen-action";
var logger = LogFactory.getLogger('login-start');

import React from 'react';
import ReactDOM from 'react-dom';
import ThemeLoader from './../libraries/themeloader/theme-loader.js';
import LocalStorageManager from './../libraries/localstoragemanager/local-storage-manager';
import SessionStorageConstants from '../commonmodule/tack/session-storage-constants';
import SessionStorageManager from './../libraries/sessionstoragemanager/session-storage-manager';
SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SESSION_ID, UniqueIdentifierGenerator.generateUUID());
LocalStorageManager.setPropertyInLocalStorage('requestId', UniqueIdentifierGenerator.generateUUID());

import LoginStore from './../screens/loginscreen/store/login-screen-store';
import SharableURLStore from '../commonmodule/store/helper/sharable-url-store';

window.addEventListener('popstate', function (event) {
  let sSelectedUILanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
  let sSelectedDataLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
  SharableURLStore.addLanguageParamsInWindowURL(sSelectedUILanguage, sSelectedDataLanguage);
});

let renderLoginScreen = function () {
  ReactDOM.render(<LoginScreenController
          store={LoginStore}
          action={LoginAction}/>,
      document.getElementById('mainContainer'));

  let oLogoConfig = LoginStore.getLogoConfig();
  ThemeLoader.loadTheme(oLogoConfig);
};

async  function executePreLoadtimeFunction() {
  	return await Promise.all([LoginStore.loadDataOnScreenLoad(), LoginStore.fetchLogoConfig()]);
}

let sException = document.currentScript.getAttribute('exceptionMessage') || "";
let showErrorMessage = function() {
    let oLoginScreenTranslations = getTranslations("LoginScreenTranslations");
    let sTranslationMsg = oLoginScreenTranslations[sException];
	if(sException){
        if(sTranslationMsg) {
            alertify.error(sTranslationMsg);
        } else {
            alertify.error(oLoginScreenTranslations['ERROR_CONTACT_ADMINISTRATOR']);
        }
    }
};

executePreLoadtimeFunction().then(renderLoginScreen).then(showErrorMessage);