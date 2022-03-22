import alertify from '../../../commonmodule/store/custom-alertify-store';
import CS from '../../../libraries/cs';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import RequestMapping from '../../../libraries/requestmappingparser/request-mapping-parser.js';
import TranslationsStore from '../../../commonmodule/store/translation-store';
import SessionStorageManager from '../../../libraries/sessionstoragemanager/session-storage-manager';
import {getTranslations, getLanguageInfo} from '../../../commonmodule/store/helper/translation-manager.js';
import ExceptionLogger from '../../../libraries/exceptionhandling/exception-logger';
import LoginScreenRequestMapping from './../tack/login-screen-request-mapping';
import ShareableUrl from './../../../commonmodule/store/helper/sharable-url-store';
import SessionStorageConstants from '../../../commonmodule/tack/session-storage-constants';
import SharableUrlConstants from '../../../commonmodule/tack/sharable-url-constants';
import ThemeLoader from '../../../libraries/themeloader/theme-loader.js';
import SessionProps from "../../../commonmodule/props/session-props";

var LoginScreenStore = (function () {

  var sUserName = '';
  var sPassword = '';
  var sRedirectURL = '';
  var aLanguages = [];
  var sSelectedLanguage = "";
  let oLogoConfig = {
    primaryLogoId: null,
    faviconId: null,
    title: "",
  };
  let bDisplay = false;

  var _triggerChange = function () {
    LoginScreenStore.trigger('change');
  };

  var _getTranslations = function (sTranslationKey) {
    return getTranslations('LoginScreenTranslations')[sTranslationKey];
  };

  var _redirectPage = function () {
    var aVars = [];
    var query = window.location.search.substring(1);
    if(!CS.isEmpty(query)){
      aVars = query.split("&");
      if(CS.includes(aVars[0], "login=")) {
        aVars.splice(0, 1);
      }
      CS.remove(aVars, function (sStr) {
         return CS.startsWith(sStr, 'theme=');
     });
    }

    if(!CS.isEmpty(aVars)) {
      sRedirectURL += '&' + aVars.join('&');
    }

    window.location.replace(sRedirectURL);
  };

  var successLoginCallback = function (oResponse) {
    sRedirectURL = oResponse.url;
    let oHistoryState = CS.getHistoryState();

    SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.IS_AFTER_LOGIN, true);

    if (oHistoryState && oHistoryState.id) {
      ShareableUrl.setIsEntityNavigation(false);
      CS.navigateBack(_redirectPage);
    } else {
      _redirectPage();
    }
    return true;
  };

  var failureLoginCallback = function (oResponse) {
    // TODO: Refactor: Use generic fallback as in ContentUtils
    if (oResponse.failure && oResponse.failure.exceptionDetails) {
      if (oResponse.failure.exceptionDetails[0].key == 'AssetServerAuthenticationFailedException') {
        alertify.error(_getTranslations('AssetServerAuthenticationFailedException'));
      } else if (oResponse.failure.exceptionDetails[0].key == 'InvalidCredentialsException') {
        alertify.error(_getTranslations('InvalidCredentialsException'));
      } else if (oResponse.failure.exceptionDetails[0].key == 'EndpointNotFoundException') {
        alertify.error(_getTranslations('EndpointNotFoundException'));
      } else if (oResponse.failure.exceptionDetails[0].key == 'UserDoesntExistInAnyRoleException') {
        alertify.error(_getTranslations('UserDoesntExistInAnyRoleException'));
      } else if (oResponse.failure.exceptionDetails[0].key == 'IncorrectResultSizeDataAccessException') {
        alertify.error(_getTranslations('IncorrectResultSizeDataAccessException'));
      }
      else {
        alertify.error(_getTranslations('ERROR_CONTACT_ADMINISTRATOR'));
      }
    }
    ExceptionLogger.error(oResponse);
    return false;
  };

  var _fetchLanguageInfo = function (oCallbackData) {
    let sSelectedUILanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    let oData = {
      isGetDataLanguages: true,
      isGetUILanguages: true,
      dataLanguage:  sSelectedUILanguageCode || _getQueryVariable(SharableUrlConstants.UI_LANG) || "",
      uiLanguage: sSelectedDataLanguageCode || _getQueryVariable(SharableUrlConstants.DATA_LANG) || ""
    };
    return CS.postRequest(LoginScreenRequestMapping.GetLanguageInfo, {}, oData, successFetchLanguageInfoCallback, failureFetchLanguageInfoCallback);
  };

  let _getQueryVariable = function (variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
      var pair = vars[i].split("=");
      if (pair[0] == variable) {
        return pair[1];
      }
    }
  };

  var successFetchLanguageInfoCallback = function (oResponse) {
    let oSuccess = oResponse.success;
    let sDefaultLanguage = oSuccess.defaultLanguage;
    let aDataLanguages = oSuccess.dataLanguages;
    let aUILanguages = oSuccess.userInterfaceLanguages;
    aLanguages = oSuccess.userInterfaceLanguages;

    SessionProps.setLanguageInfoData(oSuccess);
    let sUILanguageFromURL = _getQueryVariable(SharableUrlConstants.UI_LANG);
    if(sUILanguageFromURL && CS.isEmpty(CS.find(aUILanguages, {code: sUILanguageFromURL}))) {
      sUILanguageFromURL = ""; // If user modifies URL incorrectly
    }
    let sUILanguage = CS.isNotEmpty(sUILanguageFromURL) ? sUILanguageFromURL : sDefaultLanguage;

    let sDataLanguageFromURL = _getQueryVariable(SharableUrlConstants.DATA_LANG);
    if(sDataLanguageFromURL && CS.isEmpty(CS.find(aDataLanguages, {code: sDataLanguageFromURL}))) {
      sDataLanguageFromURL = ""; // If user modifies URL incorrectly
    }
    let sDataLanguage = sDataLanguageFromURL || sDefaultLanguage;
    CS.setSelectedDataLanguage(sDataLanguage);

    SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE, sUILanguage);
    SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE, sDataLanguage);
    ShareableUrl.addLanguageParamsInWindowURL(sUILanguage, sDataLanguage);

    return TranslationsStore.fetchTranslations(['login'], sUILanguage, {});
  };

  var failureFetchLanguageInfoCallback = function (oResponse) {
    return false;
  };

  let _setLogoConfig = function (oNewLogoConfig) {
    // if(!oNewLogoConfig.faviconId){
    //   return;
    // }
    oLogoConfig = oNewLogoConfig;
    if(oLogoConfig.faviconId){
      let oFavicon = CS.getIconUrl(oNewLogoConfig.faviconId);
      ThemeLoader.changeFavicon(oFavicon, "fromConfig");
    }
    ThemeLoader.changeTitle(oNewLogoConfig.logoTitle);
  };

  let _fetchLogoConfig = function () {
    return CS.getRequest(LoginScreenRequestMapping.GetAdminConfiguration, {}, successFetchLogoConfigCallback, failureFetchLogoConfigCallback);
  };

  let successFetchLogoConfigCallback = function (oResponse) {
    let oLogoModel = oResponse.success;
    _setLogoConfig(oLogoModel);
  };

  let failureFetchLogoConfigCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      ExceptionLogger.error("failureFetchLogoConfigCallback: Something went wrong" + oResponse);
    } else {
      ExceptionLogger.error("failureFetchLogoConfigCallback: Something went wrong" + oResponse);
    }
  };

  let successCheckUserAvailabilityCallback = function (oResponse) {
    var oUrl = oResponse.success.url;
    if(oUrl != null){
        window.location.replace(oUrl);
    }else{
        bDisplay = true;
    }

      _triggerChange();
  };

  let failureCheckUserAvailabilityCallback = function (oResponse) {
    if (CS.isNotEmpty(oResponse.failure.exceptionDetails) && oResponse.failure.exceptionDetails[0].key == "BackgroundUserCannotLoggedInException") {
      alertify.error(_getTranslations('BACKGROUND_USER_CANNOT_LOGIN'));
    } else {
      alertify.error(_getTranslations('WRONG_USERNAME'));
    }
    _triggerChange();
  };

  return {

    getData: function () {
      return {
        userName: sUserName,
        password: sPassword,
        languages: aLanguages,
        selectedLanguage: SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE),
        display: bDisplay
      }
    },

    getLogoConfig(){
      return oLogoConfig;
    },

    handleUserNameChanged: function (_sUserName) {
      sUserName = _sUserName;
      _triggerChange();
    },

    handlePasswordChanged: function (_sPassword) {
      sPassword = _sPassword;
      _triggerChange();
    },

    handleLoginButtonClicked: function () {
      var oData = {};
      oData.username = sUserName;
      var sEncryptedPassword = btoa(sUserName + "::" + sPassword);
      oData.password = sEncryptedPassword;

      if(!CS.isEmpty(sUserName) && !CS.isEmpty(sPassword)) {
        CS.customPostRequest(RequestMapping.getRequestUrl(LoginScreenRequestMapping.Login), oData, successLoginCallback, failureLoginCallback, "application/x-www-form-urlencoded");

      } else {
        alertify.error(_getTranslations('INVALID_USERNAME_OR_PASSWORD'));
        return false;
      }
    },

    handleNextButtonClicked: function () {
      let oUserNameData = {};
      oUserNameData.userName = sUserName;
      if (!CS.isEmpty(sUserName)) {
        CS.customPostRequest(LoginScreenRequestMapping.checkUserAvailability, oUserNameData, successCheckUserAvailabilityCallback, failureCheckUserAvailabilityCallback);
      } else {
        alertify.error(_getTranslations('WRONG_USERNAME'));
        return false;
      }
    },

    handleEnableInputField: function () {
      bDisplay = false;
      _triggerChange();
    },

    loadDataOnScreenLoad: function () {
      return _fetchLanguageInfo();
    },

    triggerChange: function () {
      _triggerChange();
    },

    fetchLogoConfig: function () {
      return _fetchLogoConfig();
    }
  }
})();

MicroEvent.mixin(LoginScreenStore);

TranslationsStore.bind('translation-changed', LoginScreenStore.triggerChange);

export default LoginScreenStore;
