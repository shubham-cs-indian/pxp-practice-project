import {isEmpty, isArray, values, isObject, keyBy, assign, concat, noop, find, isFunction} from '../cs/cs-lodash';
import ExceptionLogger from '../exceptionhandling/exception-logger';
import AjaxPromise from '../ajaxpromise/ajax-promise.js';
import RequestMapping from '../requestmappingparser/request-mapping-parser.js';
import SessionStorageManager from './../../libraries/sessionstoragemanager/session-storage-manager.js';
import SharableUrlConstants from '../../commonmodule/tack/sharable-url-constants.js';
import CommonModuleRequestMapping from '../../commonmodule/tack/common-module-request-mapping.js';
import SessionProps from '../../commonmodule/props/session-props.js';
import SessionStorageConstants from '../../commonmodule/tack/session-storage-constants.js';
import MomentUtils from '../../commonmodule/util/moment-utils.js';
import NumberUtils from '../../commonmodule/util/number-util.js';
/**********
 * Do not Require any other module here
 * Only Generic handling should be done
 *******/

export const NAVIGATION_TIMEOUT = 5;

/**Checks if value passed as argument is not an empty object, collection, map, or set.*/
export const isNotEmpty = function (argument) {
  return !isEmpty(argument);
};

export const combine = function (oArgument1, oArgument2, sKeyToAppendObject = "id") {
  try {
    if (isArray(oArgument1)) {
      let aArrayToConcat = isArray(oArgument2) ? oArgument2 : values(oArgument2);
      return concat(oArgument1, aArrayToConcat);
    }
    else if (isObject(oArgument1)) {
      let oObjectToAssign = isArray(oArgument2) ? keyBy(oArgument2, sKeyToAppendObject) : oArgument2;
      return assign({}, oArgument1, oObjectToAssign)
    }
    else {
      return oArgument1;
    }
  } catch (e) {
    ExceptionLogger.error(e)
  }
};

export const getFunctionToExecute = function (oCallback) {
  return oCallback && oCallback.functionToExecute || noop;
};

export const escapeRegexCharacters = function (sString) {
  return sString.replace(new RegExp('[.\\\\+*?\\[\\^\\]$(){}=!<>|:\\-]', 'g'), '\\$&');
};

export const putRequest = function (sUrl, oQueryString, oRequestData, fSuccess, fFailure, oExtraData) {
  return AjaxPromise
    .put(RequestMapping.getRequestUrl(sUrl, oQueryString), JSON.stringify(oRequestData), null, oExtraData, fSuccess, fFailure);
};

export const deleteRequest = function (sUrl, oQueryString, oRequestData, fSuccess, fFailure) {
  return AjaxPromise
    .delete(RequestMapping.getRequestUrl(sUrl, oQueryString), JSON.stringify(oRequestData), null, null, fSuccess, fFailure);
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

/***
 * @UsageFileName ['user-store','home-screen-store']
 * @param sUsername
 * @returns {boolean}
 */
export const isUsernameValid = function (sUsername) {
  if (sUsername != "") {
    //1. [a-zA-Z0-9._] allows characters and 0 or more '.' & '_' and umlouts character
    var regExp = /^[a-zA-Z0-9._\u00c4\u00d6\u00dc\u00df\u00e4\u00f6\u00fc]+$/;
    return regExp.test(sUsername);
  }
  return false;
};

/***
 * @UsageFileName ['user-store','home-screen-store', 'user-group-store']
 * @param sEmail
 * @returns {boolean}
 */
export const isEmailValid = function (sEmail) {
  if (sEmail != "") {
    var sRegExp = /^([a-zA-Z0-9_\u00c4\u00d6\u00dc\u00df\u00e4\u00f6\u00fc\-\.]+)@([a-zA-Z0-9_\u00c4\u00d6\u00dc\u00df\u00e4\u00f6\u00fc\-\.]+)\.([a-zA-Z\u00c4\u00d6\u00dc\u00df\u00e4\u00f6\u00fc]+)$/;
    return sRegExp.test(sEmail);
  }
  return false;
};

/***
 * @UsageFileName ['content-section-attribute-view','content-grid-table-view']
 * @param iCharLimit
 * @param iDataLength
 * @returns {boolean}
 */
export const isCharLimitValid = function (iCharLimit, iDataLength) {

  try {
    var sErrorMsg = "";
    if (!(typeof iCharLimit === 'number')) {
      // sErrorMsg = iCharLimit + " is not a number";
      // throw _generateError('number', typeof iCharLimit, sErrorMsg, "checkCharLimit");
    } else if (!(typeof iDataLength === 'number')) {
      // sErrorMsg = iDataLength + " is not a number";
      // throw _generateError('number', typeof iDataLength, sErrorMsg, "checkCharLimit");
    }
    return !(iDataLength > iCharLimit);
  } catch (oError) {
    console.error(oError);
    return false;
  }

};

export const generateError = function (required, found, message, methodName) {
  return {
    required: required,
    found: found,
    error: message,
    methodName: methodName
  };
};

export const getLabelOrCode = function (oItem) {
  return oItem.label || oItem.name || "[" + oItem.code + "]";
};

/**
 * @function _navigateTo
 * @description - _navigateTo() method loads a specific State (URL) from the history list.
 * @param {number} iIndex - This parameter is used to go to the State (URL) within the specific position (-1 goes back one page, 1 goes forward one page)
 * @param {func} fCallback - Callback function which will be executed after the specific State gets load.
 */
export const navigateTo = function (iIndex, fCallback) {
  let oHistoryState = getHistoryState();
  window.history.go(iIndex);
  checkHistoryStateAndExecuteCallback(oHistoryState, fCallback);
};

/**
 * @function _navigateBack
 * @description - Method loads the previous State (URL) from the history list(Same as clicking the Back button in the browser).
 * @param {func} fCallback - Callback function which will be execute after the previous State gets load.
 */
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

/**
 * @function _navigateForward
 * @description  Method loads the Next State (URL) from the history list(Same as clicking the Forward button in the browser).
 * @param {func} fCallback - Callback function which will be execute after the next State gets load..
 */
export const navigateForward = function (fCallback) {
  let oHistoryState = getHistoryState();
  window.history.forward();
  checkHistoryStateAndExecuteCallback(oHistoryState, fCallback);
};

/**
 * @function _addNavigation
 * @description - The pushState method inserts an entry into the history of the browsers which allows us to go back and forth using the browser’s forward and back buttons.
 * @param {object} oStateObj - The navigation object is associated with the new history entry created by pushState().
 * @param {string} sTitle - Not in use
 * @param {string} sURL - The new history entry's URL is given by this parameter.
 */
export const addNavigation = function (oStateObj, sTitle = "", sURL) {
  window.history.pushState(oStateObj, sTitle, sURL);
};

/**
 * @function _replaceNavigation
 * @description - replaceState() modifies the current history entry instead of creating a new one.
 * @param {object} oStateObj - The navigation object is associated with the history entry replaced by replaceState().
 * @param {string} sTitle - Not in use
 * @param {string} sURL - The replaced history URL is given by this parameter.
 */

export const replaceNavigation = function _replaceNavigation (oStateObj, sTitle = "", sURL) {
  window.history.replaceState(oStateObj, sTitle, sURL)
};

export const getHistoryState = function () {
  return window.history.state;
};

export const isMobileNumberValid = (sMobileNumber) => {
  if(!sMobileNumber) {
    sMobileNumber = "";
  }
  let regEx = /^\+?\d*(\s?\((\d+[\s\-]*\d+)\)\s?)*(\d+[\s\-]\d+)*$/;
  return regEx.test(sMobileNumber)
};

export const setSelectedDataLanguage = function(sSelectedLanguageCode) {
  SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE, sSelectedLanguageCode);
  MomentUtils.setMomentLocale();
  MomentUtils.setCurrentDateFormat();
  NumberUtils.setCurrentNumberFormat();
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

export const isNumberExponential= (sNumber) => {
  if(!sNumber) {
    sNumber = "";
  }
  let regEx = /[-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)/g;
  return regEx.test(sNumber)
};



