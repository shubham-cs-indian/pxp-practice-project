import {isEmpty, find, isFunction} from '../cs/cs-lodash';
import AjaxPromise from '../ajaxpromise/ajax-promise.js';
import RequestMapping from '../requestmappingparser/request-mapping-parser.js';
import SessionStorageManager from './../../libraries/sessionstoragemanager/session-storage-manager.js';
import SharableUrlConstants from '../../commonmodule/tack/sharable-url-constants.js';
import CommonModuleRequestMapping from '../../commonmodule/tack/common-module-request-mapping.js';
import SessionProps from '../../commonmodule/props/session-props.js';
import SessionStorageConstants from '../../commonmodule/tack/session-storage-constants.js';

export const NAVIGATION_TIMEOUT = 5;

export const isNotEmpty = function (argument) {
  return !isEmpty(argument);
};

export const escapeRegexCharacters = function (sString) {
  return sString.replace(new RegExp('[.\\\\+*?\\[\\^\\]$(){}=!<>|:\\-]', 'g'), '\\$&');
};

export const postRequest = function (sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader, oExtraData) {
  return customPostRequest(RequestMapping.getRequestUrl(sUrl, oQueryString), JSON.stringify(oRequestData), fSuccess, fFailure, null, bDisableLoader, oExtraData);
};

export const customPostRequest = function (sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData) {
  return AjaxPromise.post(sUrl, oRequestData, contentType, bDisableLoader, oExtraData, fSuccess, fFailure);
};

export const getRequest = function (sUrl, oParameters, fSuccess, fFailure, oExtraData) {
  return customGetRequest(RequestMapping.getRequestUrl(sUrl, oParameters), null, fSuccess, fFailure, null, null, oExtraData);
};

export const customGetRequest = function (sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData) {
  return AjaxPromise
    .get(sUrl, oRequestData, contentType, oExtraData, bDisableLoader, fSuccess, fFailure);
};

export const getLabelOrCode = function (oItem) {
  return oItem.label || oItem.name || "[" + oItem.code + "]";
};

export const navigateBack = function (fCallback) {
  let oHistoryState = getHistoryState();
  window.history.back();
  checkHistoryStateAndExecuteCallback(oHistoryState, fCallback);
};

const checkHistoryStateAndExecuteCallback = function (oPreviousHistoryState, fCallback) {
  isFunction(fCallback) && (() => {
    let oInterval = setInterval(() => {
      let oHistoryState = getHistoryState();
      if (oPreviousHistoryState && oHistoryState && oPreviousHistoryState.id === oHistoryState.id) {
        console.log("oHistoryState: " + oHistoryState.id);
        oPreviousHistoryState && console.log("oPreviousHistoryState: " + oPreviousHistoryState.id);
        return;
      }
      clearInterval(oInterval);
      fCallback();
    }, NAVIGATION_TIMEOUT);
  })();
};

export const replaceNavigation = function _replaceNavigation (oStateObj, sTitle = "", sURL) {
  window.history.replaceState(oStateObj, sTitle, sURL)
};

export const getHistoryState = function () {
  return window.history.state;
};

export const setSelectedDataLanguage = function(sSelectedLanguageCode) {
  SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE, sSelectedLanguageCode);
};

export const getQueryVariable = function (variable) {
  let query = window.location.search.substring(1);
  let vars = query.split("&");
  for (var i = 0; i < vars.length; i++) {
    let pair = vars[i].split("=");
    if (pair[0] == variable) {
      return pair[1];
    }
  }
};

export const getIconUrl = function (sKey) {
  return RequestMapping.getRequestUrl(CommonModuleRequestMapping.GetAssetImage, {type: "Icons", id: sKey}) + "/";
};

export const processGetLanguageInfoResponse = function (oResponse) {
  let oSuccess = oResponse.success;
  const aDataLanguages = oSuccess.dataLanguages;
  const aUILanguages = oSuccess.userInterfaceLanguages;
  const sDefaultLanguage = oSuccess.defaultLanguage;
  SessionProps.setLanguageInfoData(oSuccess);

  /** get UI language from URL or from session storage or set default language as UI language **/
  let oExtraData = {};
  let sUILanguageFromURL = getQueryVariable(SharableUrlConstants.UI_LANG);
  if(sUILanguageFromURL && isEmpty(find(aUILanguages, {code: sUILanguageFromURL}))) {
    sUILanguageFromURL = ""; // If user modifies URL incorrectly
    oExtraData.setDefaultLanguageAsUILanguage = true;
  }

  /** If user delete the language from other tab which is selected as UI language in current tab then it should get reset to default language**/
  let sUILanguageFromSession = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
  if(sUILanguageFromSession && isEmpty(find(aUILanguages, {code: sUILanguageFromSession}))) {
    sUILanguageFromSession = "";
    oExtraData.setDefaultLanguageAsUILanguage = true;
  }
  let sUILanguage = "";
  if (isEmpty(sUILanguageFromURL) && isEmpty(sUILanguageFromSession)) {
    sUILanguage = sDefaultLanguage;
  }
  else {
    oExtraData.setDefaultLanguageAsUILanguage = false;
    sUILanguage = sUILanguageFromURL || sUILanguageFromSession;
  }
  SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE, sUILanguage);

  /** get data language from URL or from session storage **/
  let sDataLanguageFromURL = getQueryVariable(SharableUrlConstants.DATA_LANG);
  if(sDataLanguageFromURL && isEmpty(find(aDataLanguages, {code: sDataLanguageFromURL}))) {
    sDataLanguageFromURL = ""; // If user modifies URL incorrectly
    oExtraData.setDefaultLanguageAsDataLanguage = true;
  }

  /** If user delete the language from other tab which is selected as Data language in current tab then it should get reset to default language **/
  let sDataLanguageFromSession = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
  if(sDataLanguageFromSession && isEmpty(find(aDataLanguages, {code: sDataLanguageFromSession}))) {
    sDataLanguageFromSession = "";
    oExtraData.setDefaultLanguageAsDataLanguage = true;
  }
  let sDataLanguage = "";
  if(isEmpty(sDataLanguageFromURL) && isEmpty(sDataLanguageFromSession)) {
    sDataLanguage = sDefaultLanguage;
  }
  else {
    oExtraData.setDefaultLanguageAsDataLanguage = false;
    sDataLanguage = sDataLanguageFromURL || sDataLanguageFromSession;
  }
  /** Update data language related data while setting data language for the first time**/
  setSelectedDataLanguage(sDataLanguage);

  return  oExtraData;
};



