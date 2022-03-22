import CS from '../../../libraries/cs';
import React from 'react';
import alertify from '../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import LogFactory from '../../../libraries/logger/log-factory';
import MethodTracker from '../../../libraries/methodtracker/method-tracker';
import { homeScreenRequestMapping as oHomeScreenRequestMapping } from '../tack/home-screen-request-mapping';
import RequestMapping from '../../../libraries/requestmappingparser/request-mapping-parser.js';
import HomeScreenAppData from './model/home-screen-app-data';
import HomeScreenProps from './model/home-screen-props';
import GlobalStore from './global-store';
import CommonUtils from '../../../commonmodule/util/common-utils';

import SessionStorageManager from '../../../libraries/sessionstoragemanager/session-storage-manager';
import LocalStorageManager from '../../../libraries/localstoragemanager/local-storage-manager';
import SessionProps from '../../../commonmodule/props/session-props';
import {
  getTranslations as oTranslations,
  getLanguageFromServer as oLanguageFromServer,
  getTranslations as getTranslation
} from '../../../commonmodule/store/helper/translation-manager';
import Exception from '../../../libraries/exceptionhandling/exception.js';
import ContentUtils from '../../homescreen/screens/contentscreen/store/helper/content-utils';
import TranslationStore from '../../../commonmodule/store/translation-store';
import MenuDictionary from '../tack/menu-dictionary';
import CustomActionDialogStore from '../../../commonmodule/store/custom-action-dialog-store';
import SharableURLStore from '../../../commonmodule/store/helper/sharable-url-store';
import HelpButtonStore from '../../../commonmodule/store/helper/help-button-store';
import oPhysicalCatalogDictionary from '../../../commonmodule/tack/physical-catalog-dictionary';
import ExceptionLogger from '../../../libraries/exceptionhandling/exception-logger';
import ModuleDictionary from '../../../commonmodule/tack/module-dictionary';
import CommonProps from '../../../commonmodule/props/common-props';
import NumberUtils from '../../../commonmodule/util/number-util';
import MomentUtils from '../../../commonmodule/util/moment-utils';
import BreadCrumbProps from '../../../commonmodule/props/breadcrumb-props';
import { communicator as HomeScreenCommunicator } from '../store/home-screen-communicator';
import SessionStorageConstants from '../../../commonmodule/tack/session-storage-constants';
import ActionDialogProps from './../../../commonmodule/props/action-dialog-props';
import NotificationProps from '../../../commonmodule/props/notification-props';
import ThemeLoader from '../../../libraries/themeloader/theme-loader.js';
import RefactoringStore from "../../../commonmodule/store/refactoring-store";

var logger = LogFactory.getLogger('home-screen-store');
var trackMe = MethodTracker.getTracker('HomeScreenStore');

/**
 * @class HomeScreenStore
 * @memberOf Stores
 */
var HomeScreenStore = (function () {

  var oAppData = HomeScreenAppData;
  var oComponentProps = HomeScreenProps;

  var triggerChange = function () {
    logger.info('triggerChange: HomeScreenStore',
      {
        'callTrace': MethodTracker.getTrace().join(', ')
      }
    );
    MethodTracker.emptyCallTrace();
    HomeScreenStore.trigger('change');
  };

  /**
   * @function _handleUserLogout
   * @description Executes when logout button is clicked.
   * @memberOf Stores.HomeScreenStore
   * @param {object} oResponse - Response
   */
  var _handleUserLogout = function () {
    _removeWindowURL(true);
    let oModule = GlobalStore.getSelectedMenu();
    GlobalStore.closeWebsocketConnection();
    let fCallback = () => {
      SharableURLStore.addParamsInWindowURL('login');
      CS.getRequest(oHomeScreenRequestMapping.Logout, {}, successUserLogoutCallback, failureUserLogoutCallback);
    };
    let oHistoryState = CS.getHistoryState();

    if (oModule.id === MenuDictionary.setting && oHistoryState) {
      CS.navigateBack(fCallback);
    } else {
      _resetWindowHistoryState(fCallback);
    }
  };

  var successUserLogoutCallback = function (oResponse) {
    location.reload(); // eslint-disable-line
    ExceptionLogger.log("User Logout Response");
    ExceptionLogger.log(oResponse);
    SessionProps.reset();
  };

  var failureUserLogoutCallback = function (oResponse) {
    //REFRESH After logout
    location.reload(); // eslint-disable-line
    if (!CS.isEmpty(oResponse.failure)) {
      ExceptionLogger.error("Logout Failed in failureUserLogoutCallback");
      ExceptionLogger.log(oResponse);
    } else {
      ExceptionLogger.error("Something went wrong in failureUserLogoutCallback");
      ExceptionLogger.error(oResponse);
    }
  };

  var _handlePoveroverClicked = function (oModel) {
    var oPopoverDetails = oComponentProps.getPopoverDetails();
    oPopoverDetails.isVisible = !oPopoverDetails.isVisible;
    oPopoverDetails.popoverstyle = {};
    if(oPopoverDetails.isVisible) {
      oPopoverDetails.popoverstyle = oModel.properties['popoverstyle'];
    }
    var oCurrentUser = GlobalStore.getCurrentUser();
    _getUserDetails(oCurrentUser.id);
  };

  let _handleEditProfileClicked = function () {
    HomeScreenProps.setShowUserInformationView(true);
    if (HomeScreenProps.getShowUserInformationView()) {
      HomeScreenProps.setShowUserLangaugesView(false);
    }

    oLanguageFromServer({functionToExecute: triggerChange});
  };

  /**
   * @function _handleUserMenuItemClicked
   * @description Executes when menu item is clicked.
   * @memberOf Stores.HomeScreenStore
   * @param {string}sSelectedActionId - Contains selected action id.
   * @example _handleUserMenuItemClicked('logout');
   */
  var _handleUserMenuItemClicked = function (sSelectedActionId) {
    switch (sSelectedActionId) {
      case "logout":
        _handleLogoutClicked();
        break;
      case "edit_profile":
        _handleEditProfileClicked();
        break;
      case "change_language":
        HomeScreenProps.setShowUserLangaugesView(true);
        if (HomeScreenProps.getShowUserInformationView()) {
          HomeScreenProps.setShowUserInformationView(false);
        }
        triggerChange();
        break;
      case "helpMenu":
        _handleHelpButtonClicked();
        break;

      case "about":
        _handleAboutButtonClicked();
        break;
    }
  };

  var _handleLogoutClicked = function (oEvent) {
    var oActiveEntity = ContentUtils.getActiveEntity();
    var isActiveEntityDirty = oActiveEntity.hasOwnProperty('contentClone');

    if (isActiveEntityDirty) {
      var sMessage = oTranslations().DO_YOU_REALLY_WANT_TO_LOG_OUT + " " + oTranslations().ALL_UNSAVED_CHANGES_WILL_BE_LOST;
      CustomActionDialogStore.showConfirmDialog(sMessage, '',
        function () {
          LocalStorageManager.setPropertyInLocalStorage('disableConfirmOnUnload', "true");
          _handleUserLogout()
        }, function (oEvent) {
        });
    }
    else {
      LocalStorageManager.setPropertyInLocalStorage('disableConfirmOnUnload', "true");
      _handleUserLogout();
    }
  };

  var _handleUserMenuClosed = function () {
    var oCurrentUser = GlobalStore.getCurrentUser();
    delete oCurrentUser.clonedObject;
    HomeScreenProps.setShowUserInformationView(false);
    HomeScreenProps.setShowUserLangaugesView(false);
    HomeScreenProps.setChangePasswordEnabled(false);
    HomeScreenProps.emptyErroFields({});
    HomeScreenProps.setSelectedUserPreferredUILanguage("");
    HomeScreenProps.setSelectedUserPreferredDataLanguage("");
    triggerChange();
  };

  var _handlePoveroverOnHide = function () {
    var oCurrentUser = GlobalStore.getCurrentUser();
    delete oCurrentUser.clonedObject;
    oComponentProps.emptyPopoverDetails();
    triggerChange();
  };

  var _handleUserDataChanged = function (oModel, sContext, sNewValue) {
    var oActiveUser = GlobalStore.getCurrentUser();
    var oClonedUser = _makeCurrentUserDirty();
    var oErrors = oComponentProps.getErrorFields();
    if(sContext == 'lastName') {
      sNewValue = sNewValue.trim();
      oClonedUser[sContext] = sNewValue;
      if(!CS.isEmpty(sNewValue)) {
        delete oErrors['lastName'];
      } else {
        oErrors['lastName'] = oTranslations().LAST_NAME_NOT_EMPTY;
      }
    } else if(sContext == 'userName') {
      sNewValue = sNewValue.trim();
      oClonedUser[sContext] = sNewValue;
      if(!CS.isEmpty(sNewValue)) {
        if(CS.isUsernameValid(sNewValue)) {
          if(oClonedUser.userName != oActiveUser.userName) {
            _checkUsernameAvailability(oClonedUser);
          } else {
            delete oErrors['userName'];
          }
        } else {
          oErrors['userName'] = oTranslations().ERROR_USER_INVALID_USERNAME;
        }
      } else {
        oErrors['userName'] = oTranslations().ERROR_USERNAME_NOT_EMPTY;
      }
    } else if(sContext == 'password') {

      sNewValue = sNewValue.trim();
      oClonedUser[sContext] = sNewValue;
    } else if(sContext == 'confirmPassword') {

      sNewValue = sNewValue.trim();
      oClonedUser[sContext] = sNewValue;
    } else if(sContext == 'birthDate') {
      oClonedUser[sContext] = isNaN(Date.parse(sNewValue)) ? "" : new Date(sNewValue);
    } else if(sContext == 'email') {
      sNewValue = sNewValue.trim();
      oClonedUser[sContext] = sNewValue;
      if(!CS.isEmailValid(sNewValue)) {
        oErrors['email'] = oTranslations().EMAIL_VALIDATE;
      } else {
        delete oErrors['email'];
      }
    } else if (sContext === 'mobileNumber') {
      sContext = "contact";
      oClonedUser[sContext] = sNewValue;
      if (!CS.isMobileNumberValid(sNewValue)) {
        oErrors['contact'] = oTranslations().PLEASE_ENTER_VALID_MOBILE_NUMBER;
      } else {
        delete oErrors['contact'];
      }
    } else {
      oClonedUser[sContext] = sNewValue;
    }
    triggerChange();
  };

  var _handleUserChangePasswordClicked = function () {
    oComponentProps.setChangePasswordEnabled(true);
    triggerChange();
  };

  var _handleUserPasswordSubmit = function (sPassword) {
    var oCurrentUser = GlobalStore.getCurrentUser();
    var oDataToSave = {};
    oDataToSave.id = oCurrentUser.id;
    if (oComponentProps.getChangePasswordEnabled()) {
      var sUsername = oCurrentUser.userName;
      if (sPassword == "") {
        alertify.error(oTranslations().ERROR_PASSWORD_NOT_EMPTY);
        return;
      }
      sPassword = btoa(sUsername + "::" + sPassword);
      oDataToSave.password = sPassword;
      CS.postRequest(oHomeScreenRequestMapping.ResetPassword,{},oDataToSave,successSavePasswordCallback,failureSavePasswordCallback);
    }
  };

  let _hideDataLanguageOptionsOnHeader = function (bDoNotTrigger) {
    HomeScreenProps.setIsHideDataLanguageOptions(true);
    !bDoNotTrigger && triggerChange();
  };

  let _showDataLanguageOptionsOnHeader = function (bDoNotTrigger) {
    HomeScreenProps.setIsHideDataLanguageOptions(false);
    !bDoNotTrigger && triggerChange();
  };

  let _hideUILanguageOptionsOnHeader = function (bDoNotTrigger) {
    HomeScreenProps.setIsHideUILanguageOptions(true);
    !bDoNotTrigger && triggerChange();
  };

  let _showUILanguageOptionsOnHeader = function (bDoNotTrigger) {
    HomeScreenProps.setIsHideUILanguageOptions(false);
    !bDoNotTrigger && triggerChange();
  };

  let _getLogoConfig = function () {
    return HomeScreenProps.getLogoConfig();
  };

  let _setLogoConfig = function (oNewLogoConfig) {
    HomeScreenProps.setLogoConfig(oNewLogoConfig);
    if(oNewLogoConfig.faviconId){
      let oFavicon = ContentUtils.getIconUrl(oNewLogoConfig.faviconId);
      ThemeLoader.changeFavicon(oFavicon, "fromConfig");
    }else{
      // No favicon available/removed
      ThemeLoader.changeFavicon(null);
    }
    ThemeLoader.changeThemeConfiguration(oNewLogoConfig);
    ThemeLoader.changeTitle(oNewLogoConfig.logoTitle);
    //triggerChange();
  };

  let _getViewConfig = function () {
    return HomeScreenProps.getViewConfig();
  };

  let _setViewConfig = function (oNewViewConfig) {
    HomeScreenProps.setViewConfig(oNewViewConfig);
    //triggerChange();
  };

  let _fetchViewConfig = function () {
    CS.getRequest(oHomeScreenRequestMapping.GetViewConfigurations, {}, successFetchViewConfigCallback, failureFetchViewConfigCallback);
  };

  let _fetchLogoConfig = function () {
    CS.getRequest(oHomeScreenRequestMapping.GetThemeConfigurations, {}, successFetchLogoConfigCallback, failureFetchLogoConfigCallback);
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

  let successFetchViewConfigCallback = function (oResponse) {
    let oViewModel = oResponse.success;
    _setViewConfig(oViewModel);
  };

  let failureFetchViewConfigCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      ExceptionLogger.error("failureFetchViewConfigCallback: Something went wrong" + oResponse);
    } else {
      ExceptionLogger.error("failureFetchViewConfigCallback: Something went wrong" + oResponse);
    }
  };

  var _handleUserPasswordCancel = function () {
    oComponentProps.setChangePasswordEnabled(false);
    triggerChange();
  };

  var _checkUsernameAvailability = function (oActiveUser) {
    CS.postRequest(oHomeScreenRequestMapping.checkUserAvailability, {}, oActiveUser, successCheckUserAvailabilityCallback.bind(this, oActiveUser), failureCheckUserAvailabilityCallback.bind(this, oActiveUser));
  };

  var successCheckUserAvailabilityCallback = function () {
      var oErrors = oComponentProps.getErrorFields();
      delete oErrors['userName'];
      triggerChange();
  };

  var failureCheckUserAvailabilityCallback = function (oActiveUser, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var oErrors = oComponentProps.getErrorFields();
      oErrors['userName'] = oTranslations().USER_ERROR_MESSAGE;
      ExceptionLogger.error("Something went wrong in 'failureCheckUserAvailabilityCallback': ");
      ExceptionLogger.error(oResponse);
      triggerChange();
    } else {
      ExceptionLogger.error("Something went wrong in 'failureCheckUserAvailabilityCallback': ");
      ExceptionLogger.error(oResponse);
    }
  };

  var _getUserDetails = function (sSelectedUserId) {
    CS.getRequest(oHomeScreenRequestMapping.getUserById, {id: sSelectedUserId}, successGetUserCallback, failureGetUserCallback);
  };

  var successGetUserCallback = function (oResponse) {
    var oUser = oResponse.success;
    oUser.password = "";
    oComponentProps.setChangePasswordEnabled(false);
    var oCurrectUser = GlobalStore.getCurrentUser();
    CS.assign(oCurrectUser, oUser);
    triggerChange();
  };

  var failureGetUserCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      ExceptionLogger.error("failureGetUserCallback: Something went wrong" + oResponse);
    } else {
      ExceptionLogger.error("failureGetUserCallback: Something went wrong" + oResponse);
    }
    triggerChange();
  };

  let _checkAllUserInfoFieldsBeforeSave = function (oUserToSave) {
    let oErrors = {};
    if (CS.isEmpty(oUserToSave.firstName)) {
      oErrors["firstName"] = oTranslations().FIRST_NAME_SHOULD_NOT_BE_EMPTY;
    }
    if (CS.isEmpty(oUserToSave.lastName)) {
      oErrors["lastName"] = oTranslations().LAST_NAME_NOT_EMPTY;
    }
    if (CS.isEmpty(oUserToSave.userName)) {
      oErrors["userName"] = oTranslations().ERROR_USERNAME_NOT_EMPTY;
    }

    return oErrors;
  };

  var _handleUserDetailsSaveClicked = function () {
    let oCallbackData = {};
    var oUserToSave = _makeCurrentUserDirty();
    let oErrorMessages = _checkAllUserInfoFieldsBeforeSave(oUserToSave);
    let sSelectedPreferredUILanguage = oComponentProps.getSelectedUserPreferredUILanguage();
    let sSelectedPreferredDataLanguage = oComponentProps.getSelectedUserPreferredDataLanguage();
    var oErrors = oComponentProps.getErrorFields();

    if (CS.isNotEmpty(oErrorMessages)) {
      _showErrors(CS.assign(oErrorMessages, oErrors));
      return;
    }
    if (CS.isEmpty(oErrors)) {
      if (HomeScreenProps.getChangePasswordEnabled()) {
        if (CS.isNaN(oUserToSave.password) && CS.isEmpty(oUserToSave.password) && CS.isNaN(oUserToSave.confirmPassword) && CS.isEmpty(oUserToSave.confirmPassword)) {
          return alertify.error(oTranslations().ERROR_PASSWORD_NOT_EMPTY);
        }
        else if (CS.isEmpty(CS.trim(oUserToSave.password))) {
          return alertify.error(oTranslations().ERROR_PASSWORD_CONTAIN_ONLY_WHITE_SPACES);
        }
        else if (oUserToSave.password !== oUserToSave.confirmPassword) {
          return alertify.error(oTranslations().ERROR_PASSWORD_AND_CONFIRM_PASSWORD);
        }
        else {
          _handleUserPasswordSubmit(oUserToSave.password);
        }
      }
      if (CS.isNotEmpty(sSelectedPreferredUILanguage)) {
        oUserToSave.preferredUILanguage = sSelectedPreferredUILanguage;
        oCallbackData.preferredUILanguage = sSelectedPreferredUILanguage;
      }
      if (CS.isNotEmpty(sSelectedPreferredDataLanguage)) {
        oUserToSave.preferredDataLanguage = sSelectedPreferredDataLanguage;
        oCallbackData.preferredDataLanguage = sSelectedPreferredDataLanguage;
      }

      oUserToSave.password = "";
      oUserToSave.confirmPassword = "";

      CS.postRequest(oHomeScreenRequestMapping.saveUser, {}, [oUserToSave], successSaveUserCallback.bind(this, oCallbackData), failureSaveUserCallback);
    } else {
      _showErrors(oErrors);
    }
  };

  let _handleUserPreferredLanguageChanged = function (sId, sContext) {
    switch (sContext) {
      case "uiLanguage":
        HomeScreenProps.setSelectedUserPreferredUILanguage(sId);
        break;
      case "dataLanguage":
        HomeScreenProps.setSelectedUserPreferredDataLanguage(sId);
        break;
    }
  };

  let _handleRemoveUserImageClicked = function () {
    let oCurrentUser = GlobalStore.getCurrentUser();
    if (oCurrentUser.hasOwnProperty("clonedObject")) {
      let oClonedObject = oCurrentUser.clonedObject;
      if (CS.isNotEmpty(oClonedObject.icon)) {
        oClonedObject.icon = "";
      }
    } else {
      if (CS.isNotEmpty(oCurrentUser.icon)) {
        var oUserToSave = _makeCurrentUserDirty();
        oUserToSave.icon = "";
      }
    }
  };

  var _showErrors = function (oErrors) {
    var aErrors = [];
    var iCount = 1;
    CS.forEach(oErrors, function (sError) {
      aErrors.push(iCount+ ". " +sError);
      iCount++;
    });
    alertify.error(aErrors, 0);
  };

  var successSaveUserCallback = function (oCallbackData, oResponse) {
    let oSuccess = oResponse.success;
    let oUserFromServer = oSuccess.usersList[0];
    oUserFromServer.password = "";
    oComponentProps.setChangePasswordEnabled(false);
    var oCurrentUser = GlobalStore.getCurrentUser();
    delete oCurrentUser.clonedObject;
    CS.assign(oCurrentUser, oUserFromServer);
    HomeScreenProps.setPreferredUILanguage(oUserFromServer.preferredUILanguage);
    HomeScreenProps.setPreferredDataLanguage(oUserFromServer.preferredDataLanguage);
    _handleUserMenuClosed();
    alertify.success(oTranslations().USER_SUCCESSFULLY_SAVED);
    triggerChange();
  };

  var failureSaveUserCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      alertify.error(Exception.getMessage(oResponse, GlobalStore.getTranslations('SettingScreenTranslations'), '' ), 0);
      ExceptionLogger.log(oResponse);
    } else {
      ExceptionLogger.error("failureSaveUserCallback: Something went wrong" + oResponse);
    }

    triggerChange();
  };

  var successSavePasswordCallback = function (oResponse) {
    var oUserFromServer = oResponse.success;
    oUserFromServer.password = "";
    oComponentProps.setChangePasswordEnabled(false);
    alertify.success(oTranslations().PASSWORD_SUCCESSFULLY_SAVED);
    triggerChange();
  };

  var failureSavePasswordCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      alertify.error(Exception.getMessage(oResponse, GlobalStore.getTranslations('SettingScreenTranslations'), '' ), 0);
      ExceptionLogger.log(oResponse);
    } else {
      ExceptionLogger.error("failureSaveUserCallback: Something went wrong" + oResponse);
    }

    triggerChange();
  };

  var _makeCurrentUserDirty = function () {
    var oCurrentUser = GlobalStore.getCurrentUser();
    if (!oCurrentUser.clonedObject) {
      oCurrentUser.clonedObject = CS.cloneDeep(oCurrentUser);
    }

    return oCurrentUser.clonedObject;
  };

  var _handleAboutButtonClicked = function () {
    HomeScreenProps.setAboutDialogVisibility(true);
    triggerChange();
  };

  var _handleAboutDialogClosed = function () {
    HomeScreenProps.setAboutDialogVisibility(false);
    triggerChange();
  };

  var _handleNotificationCall = function () {
    let sUrl = oHomeScreenRequestMapping.GetAllNotifications;
    let oParameters = {taskId : "taskplanned"};
    CS.customGetRequest(RequestMapping.getRequestUrl(sUrl, oParameters), {}, successFetchAllNotifications, failureFetchAllNotifications,"",true,{});
  };

  var successFetchAllNotifications = function (oResponse) {
    let prevUnreadNotificationCount = HomeScreenProps.getUnreadNotificationCount();
    if(prevUnreadNotificationCount !== oResponse.success.id){
      HomeScreenProps.setUnreadNotificationCount(oResponse.success.id);
      HomeScreenProps.setIsNotificatioinChanged(true);
      triggerChange();
    }
  };

  var failureFetchAllNotifications = function (oResponse) {
    ExceptionLogger.log(oResponse);
  };

  var _handleModuleItemClicked = function (sModuleId, sContext) {

    if (sContext === "dashboardScreen") {

      var aModuleList = HomeScreenProps.getDashboardModuleList();
      CS.forEach(aModuleList, function (oModule) {
        oModule.isSelected = false;
        if (oModule.id == sModuleId) {
          oModule.isSelected = true;
        }
      });
      triggerChange();
    }
  };

  var _handlePhysicalCatalogMenuButtonVisibility = function (bShowPhysicalCatalogMenu) {
    HomeScreenProps.setShowPhysicalCatalogMenu(bShowPhysicalCatalogMenu);
    triggerChange();
  };

  var _disablePhysicalCatalog = function (bIsPhysicalCatalogDisabled) {
    let bIsPhysicalCatalogDisabledLocal = HomeScreenProps.getIsPhysicalCatalogDisabled();
    if(bIsPhysicalCatalogDisabledLocal !== bIsPhysicalCatalogDisabled){
      HomeScreenProps.setIsPhysicalCatalogDisabled(bIsPhysicalCatalogDisabled);
      triggerChange();
    }
  };

  var _removeWindowURL = function (bUpdateSessionProps) {
    //Handle URL update.
    SharableURLStore.removeWindowURL(bUpdateSessionProps);
  };

  var _handleHelpButtonClicked = function () {
    HelpButtonStore.handleHelpButtonClicked();
  };

  var _handleNotificationButtonClicked = function () {
    let isArchive = ContentUtils.getIsArchive();
    if(isArchive) {
      return;
    }

    NotificationProps.setIsNotificationButtonSelected(true);
    /**
     * Reset widow history state and props data and call for workflow workbench tab.
     */
    let oSelectedMenu = GlobalStore.getSelectedMenu();
    let fResetData = () => {
      BreadCrumbProps.setBreadCrumbData([]);
      ContentUtils.resetAll();
    };
    if (oSelectedMenu.id === "runtime") {
      let fCallback = () => {
        fResetData();
        HomeScreenCommunicator.handleNotificationButtonClicked();
      };
      CommonUtils.setHistoryStateToNull(fCallback);
    }
    else {
      let fCallback = () => {
        HomeScreenStore.setSelectedMenu("runtime");
        HomeScreenCommunicator.handleNotificationButtonClicked();
      };
      CS.navigateBack(fCallback);
    }
    _disablePhysicalCatalog(false);
  };

  let _handleBrowserBackOrForwardButtonClicked = (oBreadcrumbItem) => {
    //To Close Custom dialog on browser button navigation
    let bIsCustomDialogOpen = ActionDialogProps.getIsCustomDialogOpen();
    if (bIsCustomDialogOpen) {
      CustomActionDialogStore.resetCustomDialog();
    }
    HomeScreenCommunicator.handleBrowserBackOrForwardButtonClicked(oBreadcrumbItem);
  };

  let _resetWindowHistoryState = (fCallback) => {
    // On Logo or setting clicked reset state to -1
    SharableURLStore.setIsPushHistoryState(true);
    let aBreadcrumb = BreadCrumbProps.getBreadCrumbData();
    let oHistoryState = CS.getHistoryState();

    if (aBreadcrumb.length && oHistoryState) {
      SharableURLStore.setIsEntityNavigation(false);
      let iStateValue = aBreadcrumb.length;
      if (ContentUtils.getAvailableEntityViewStatus()) {
        iStateValue = iStateValue - 1;
      }
      CS.navigateTo(-iStateValue, fCallback);
    } else {
      fCallback();
    }
  };

  let _languageInfoDialogOpened = function () {
    let oCallBackData = {
      functionToExecute: function () {
        HomeScreenProps.setIsUIAndDataLanguageSelectionDialogOpen(true);
        triggerChange();
      },
      functionToExecuteAfterSwitchedToDefaultLanguage: () => {
        HomeScreenProps.setIsUIAndDataLanguageSelectionDialogOpen(false);
        let sSelectedUILanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
        _handleUILanguageChanged(sSelectedUILanguageCode);
      },
    };
    oLanguageFromServer(oCallBackData);
  };

  const checkSettingsSafetyAndExecute = function (callback, functionToExecute) {
    import('../../homescreen/screens/settingscreen/store/setting-screen-store')
      .then((SettingScreenStore) => {
        if(SettingScreenStore.default.settingScreenSafetyCheck(callback)){
          functionToExecute();
        }
      })
      .catch(err => {
        console.error("Failed to load setting-screen-store")
        console.log(err);
      });
  };

  let _getLanguageInfoFromServer = async function () {
    if (ContentUtils.isActiveContentDirty()) {
      alertify.message(oTranslations().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return;
    }

    let bIsUIAndDataLanguageSelectionDialogOpen = HomeScreenProps.getIsUIAndDataLanguageSelectionDialogOpen();

    if (bIsUIAndDataLanguageSelectionDialogOpen === false) {
      let oModule = GlobalStore.getSelectedMenu();
      let fFunctionToExecute = _languageInfoDialogOpened;
      if (oModule.id === MenuDictionary.setting && HomeScreenCommunicator.getSettingScreenLoaded()) {
        checkSettingsSafetyAndExecute({functionToExecute: fFunctionToExecute}, fFunctionToExecute);
      } else {
        fFunctionToExecute();
      }
    } else {
      HomeScreenProps.setIsUIAndDataLanguageSelectionDialogOpen(false);
      triggerChange();
    }
  };

  let _handleSelectedUIOrDataLanguageInfoChangedFromConfig = function (oActiveLanguage) {
    let oLanguageData = SessionProps.getLanguageInfoData();
    let aLanguages = [];
    let bShouldUpdateUILanguage = oActiveLanguage.isUserInterfaceLanguage && SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE) === oActiveLanguage.code;
    let bShouldUpdateDataLanguage = oActiveLanguage.isDataLanguage && SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE) === oActiveLanguage.code;

    if(bShouldUpdateUILanguage){
      aLanguages = oLanguageData.userInterfaceLanguages;
      let oLanguage = CS.find(aLanguages, {code: oActiveLanguage.code});
      oLanguage.iconKey = oActiveLanguage.iconKey;
    }

    //if icon, number or date format of selected data language changed from config, it should get reflected on runtime screen
    if (bShouldUpdateDataLanguage) {
      aLanguages = oLanguageData.dataLanguages;
      let oLanguage = CS.find(aLanguages, {code: oActiveLanguage.code});
      if (oLanguage.iconKey !== oActiveLanguage.iconKey) {
        oLanguage.iconKey = oActiveLanguage.iconKey;
      }

      if (oLanguage.numberFormat !== oActiveLanguage.numberFormat) {
        oLanguage.numberFormat = oActiveLanguage.numberFormat;
        NumberUtils.setCurrentNumberFormat(oLanguage);
      }
      if (oLanguage.dateFormat !== oActiveLanguage.dateFormat) {
        oLanguage.dateFormat = oActiveLanguage.dateFormat;
        MomentUtils.setCurrentDateFormat(oLanguage);
      }
    }
    if(bShouldUpdateUILanguage || bShouldUpdateDataLanguage){
      triggerChange();
    }
  };

  let _redirectToHomeModule = function(isFromArchive) {
    let oDashboardScreenStore = require("../screens/contentscreen/screens/dashboardscreen/store/dashboard-screen-store").default;
      oDashboardScreenStore.resetAll();

      let fCallback = () => {
        //Reset Content screen props
        ContentUtils.resetAll();

        let sPortalId = CommonUtils.getSelectedPortalId();
        let sPhysicalCatalogId = SessionProps.getSessionPhysicalCatalogId() !==  oPhysicalCatalogDictionary.DATAINTEGRATION ?
            SessionProps.getSessionPhysicalCatalogId() : "";
        //Reset Common modules props
        CommonProps.reset();

        _removeWindowURL();

        let oQuickViewData = {
          functionToExecute: function () {
            HomeScreenProps.setIsResetRequired(true);
          },
          portalIdToSet: sPortalId,
          physicalCatalogId: sPhysicalCatalogId
        };
        let oCallBackData = {
          functionToExecute: async () => {
            await GlobalStore.fetchMasterData(oQuickViewData, false);
            HomeScreenProps.setShowPhysicalCatalogMenu(true);
            _disablePhysicalCatalog(false);
            SessionProps.setIsArchive(!!isFromArchive);
          }
        };
        /**
         * Required to switch in default UI language when selected UI language is not found in language info data
         */
        oCallBackData.functionToExecuteAfterSwitchedToDefaultLanguage = () => {
          /** send call again to load translations with new UI langauge **/
          oLanguageFromServer(oCallBackData);
        };

        oLanguageFromServer(oCallBackData);
      };
      //Reset Window history state to null
      CommonUtils.setHistoryStateToNull(fCallback);
  }

  let _handleHomeModuleClicked = function (isFromArchive) {
    let oCallback = {
      functionToExecute: _redirectToHomeModule.bind(this, isFromArchive)
    };
    if(HomeScreenCommunicator.getSettingScreenLoaded()){
      checkSettingsSafetyAndExecute(oCallback, _redirectToHomeModule.bind(this, isFromArchive));
    } else {
      _redirectToHomeModule(isFromArchive);
    }
  };

  let _handleUILanguageChanged = function (sUILanguageCode) {
    let oModule = GlobalStore.getSelectedMenu();
    SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE, sUILanguageCode);

    if(oModule.id === MenuDictionary.setting) {
      let fFunctionToExecute = function () {
        HomeScreenCommunicator.handleUILanguageChangedFromConfig();

        /**
         * To update language label in current UI language.
         * */
        oLanguageFromServer({functionToExecute: triggerChange});
      };
      let aKeys = ['home', 'view', 'setting'];
      let sSelectedUILanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
      TranslationStore.fetchTranslations(aKeys, sSelectedUILanguageCode, {functionToExecute: fFunctionToExecute});
    } else {
      return _handleHomeModuleClicked();
    }
  };

  let _handleDataLanguageChanged = function (sDataLanguageCode, oCallback) {
    CommonUtils.setSelectedDataLanguage(sDataLanguageCode);

    if(oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
  };

  let _handleLanguageChanged = function (sUILanguageCode, sDataLanguageCode) {
    HomeScreenProps.setIsUIAndDataLanguageSelectionDialogOpen(false);
    SharableURLStore.addLanguageParamsInWindowURL(sUILanguageCode, sDataLanguageCode);

    if (sUILanguageCode && sDataLanguageCode) {
      _handleDataLanguageChanged(sDataLanguageCode);
      _handleUILanguageChanged(sUILanguageCode);
    } else if (sUILanguageCode) {
      _handleUILanguageChanged(sUILanguageCode);
    } else if (sDataLanguageCode) {
      HomeScreenCommunicator.handleDataLanguageChanged(sDataLanguageCode);
      triggerChange(); // to re-render home screen
    } else {
      /** If no UI & data Language is changed, do trigger change to close dialog **/
      triggerChange();
    }
  };

  let _handleArchivalButtonClicked = function () {
    let oSelectedMenu = GlobalStore.getSelectedMenu();
    if(oSelectedMenu.id === MenuDictionary.setting){
      _handleHomeModuleClicked(true);
    } else {
      let aAllMenus = HomeScreenAppData.getAllMenus();
      SessionProps.setIsArchive(true);
      CS.remove(aAllMenus, {id: ModuleDictionary.DASHBOARD});
      aAllMenus[0].isSelected = true;
      CommonUtils.setHistoryStateToNull(HomeScreenCommunicator.handleArchivalButtonClicked);
      triggerChange();
    }
  };

  let _handleSidePanelToggle = function (bIsLandingPage) {
    let oViewConfigData = _getViewConfig();
    if(bIsLandingPage) {
      oViewConfigData.isLandingPageExpanded = !oViewConfigData.isLandingPageExpanded;
    } else {
      oViewConfigData.isProductInfoPageExpanded = !oViewConfigData.isProductInfoPageExpanded;
    }
  };

  return {
    getData: function () {
      var data = {
        appData: oAppData,
        componentProps: oComponentProps
      };

      return data;
    },

    setSelectedMenu: function (sId) {
      trackMe('setSelectedMenu');
      var oSelectedMenu = GlobalStore.getSelectedMenu();

      if(oSelectedMenu.id !== sId) {

        let fCallback = () => {
          _removeWindowURL();
          ContentUtils.resetModuleSelection();
          let oCallbackData = {
            functionToExecute: function(){
              GlobalStore.setSelectedMenu(sId);
              HomeScreenProps.setShowPhysicalCatalogMenu(true);
              sId == "runtime" && GlobalStore.fetchMasterData({});
            }
          };
          SessionProps.setIsArchive(false);
          if(HomeScreenCommunicator.getSettingScreenLoaded()){
            checkSettingsSafetyAndExecute(oCallbackData, GlobalStore.setSelectedMenu.bind(this, sId));
          } else {
            GlobalStore.setSelectedMenu(sId);
          }
        };

        if (sId === MenuDictionary.setting) {
          let fCallbackFunction = () =>{
            SharableURLStore.addParamsInWindowURL(sId);
            HomeScreenProps.setShowPhysicalCatalogMenu(false);
            this.fetchLogoConfig();
            fCallback();
          };
          _resetWindowHistoryState(fCallbackFunction);
        } else {
          //When Runtime menu clicked
          SharableURLStore.setIsEntityNavigation(false);
          let oHistoryState = CS.getHistoryState();

          if (oHistoryState) {
            CS.navigateBack(fCallback);
          } else {
            fCallback();
          }
        }

      }
    },

    handleHomeModuleClicked: async function () {
      await this.fetchLogoConfig();
      await this.fetchViewConfig();
      _handleHomeModuleClicked();
    },

    fetchGlobalData: function (oQuickViewData, bDontResetPhysicalCatalog, oCallbackData) {
      trackMe('fetchGlobalData');
      logger.debug('Fetching global data like tags, content types', '');
      return GlobalStore.fetchMasterData(oQuickViewData, bDontResetPhysicalCatalog, oCallbackData);
    },

    getCurrentUser: function () {
      return GlobalStore.getCurrentUser();
    },

    getLanguageInfo: function () {
      return GlobalStore.getLanguageInfo();
    },

    handleLogoutClicked: function (oEvent) {
      return _handleLogoutClicked(oEvent);
    },

    getLanguageSelectionData: function () {
      return HomeScreenProps.getLanguageSelectionData();
    },

    handlePhysicalCatalogSelectionChanged: function (sContext, aSelectedItems) {
      SharableURLStore.setIsPushHistoryState(true);
      if (sContext == "portal") {
        SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.PORTAL, aSelectedItems[0])
        SharableURLStore.addPortalIdInWindowURL(aSelectedItems[0]);
      } else {
        SessionProps.setSessionPhysicalCatalogId(aSelectedItems[0]);
      }
      GlobalStore.fetchCurrentUserInformation({});

    },

    handleRefreshCurrentUserDetails: function () {
      var oCurrentUser = GlobalStore.getCurrentUser();
      _getUserDetails(oCurrentUser.id);
    },

    handlePoveroverClicked: function (oModel) {
      _handlePoveroverClicked(oModel);
    },

    handleUserMenuItemClicked: function (sSelectedMenuLabel) {
      _handleUserMenuItemClicked(sSelectedMenuLabel);
    },

    handleUserMenuClosed: function () {
      _handleUserMenuClosed();
    },

    handlePoveroverOnHide: function () {
      _handlePoveroverOnHide();
    },

    handleUserDataChanged: function (oModel, sContext, sNewValue) {
      _handleUserDataChanged(oModel, sContext, sNewValue);
    },

    handleSelectedUIOrDataLanguageInfoChangedFromConfig: function(oActiveLanguage) {
      _handleSelectedUIOrDataLanguageInfoChangedFromConfig(oActiveLanguage);
    },

    handleLanguageChanged: function (sUILanguageCode, sDataLanguageCode) {
      _handleLanguageChanged(sUILanguageCode, sDataLanguageCode);
    },

    handleUserChangePasswordClicked: function () {
      _handleUserChangePasswordClicked();
    },

    handleUserPasswordSubmit: function (sPassword) {
      _handleUserPasswordSubmit(sPassword);
    },

    handleUserPasswordCancel: function () {
      _handleUserPasswordCancel();
    },

    handleUserDetailsSaveClicked: function (oModel) {
      _handleUserDetailsSaveClicked();
    },

    handleUserPreferredLanguageChanged: function (sId, sContext) {
      _handleUserPreferredLanguageChanged(sId, sContext);
      triggerChange();
    },

    handleRemoveUserImageClicked: function () {
      _handleRemoveUserImageClicked();
      triggerChange();
    },

    handleUploadImageChangeEvent: function (sIconUrl) {
      var oCurrentUser = _makeCurrentUserDirty();
      oCurrentUser.icon = sIconUrl;
      triggerChange();
    },

    handleAboutDialogClosed: function () {
      _handleAboutDialogClosed();
    },

    handleNotificationCall: function(){
      _handleNotificationCall();
    },

    handleModuleItemClicked: function (sModuleId, sContext) {
      _handleModuleItemClicked(sModuleId, sContext);
    },

    handlePhysicalCatalogMenuButtonVisibility: function (bShowPhysicalCatalogMenu) {
      _handlePhysicalCatalogMenuButtonVisibility(bShowPhysicalCatalogMenu)
    },

    disablePhysicalCatalog: function (bIsPhysicalCatalogDisabled) {
      _disablePhysicalCatalog(bIsPhysicalCatalogDisabled)
    },

    handleNotificationButtonClicked: function () {
      _handleNotificationButtonClicked();
    },

    handleBrowserBackOrForwardButtonClicked: function (oBreadcrumbItem) {
      _handleBrowserBackOrForwardButtonClicked(oBreadcrumbItem);
    },

    handleDataLanguageChanged: function (oCallbackData) {
      triggerChange();
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    },

    getLanguageInfoFromServer: function () {
      _getLanguageInfoFromServer();
    },

    triggerChange: function () {
      triggerChange();
    },

    hideDataLanguageOptionsOnHeader: function (bDoNotTrigger) {
      _hideDataLanguageOptionsOnHeader(bDoNotTrigger);
    },

    showDataLanguageOptionsOnHeader: function (bDoNotTrigger) {
      _showDataLanguageOptionsOnHeader(bDoNotTrigger);
    },

    hideUILanguageOptionsOnHeader: function (bDoNotTrigger) {
      _hideUILanguageOptionsOnHeader(bDoNotTrigger);
    },

    showUILanguageOptionsOnHeader: function (bDoNotTrigger) {
      _showUILanguageOptionsOnHeader(bDoNotTrigger);
    },

    getLogoConfig: function(){
      return _getLogoConfig();
    },

    setLogoConfig: function (oNewLogoConfig) {
      _setLogoConfig(oNewLogoConfig);
    },

    getViewConfig: function(){
      return _getViewConfig();
    },

    setViewConfig: function (oNewViewConfig) {
      _setViewConfig(oNewViewConfig);
    },

    handleArchivalButtonClicked: function(sId) {
      _handleArchivalButtonClicked(sId);
    },

    fetchLogoConfig: function () {
      _fetchLogoConfig();
    },

    fetchViewConfig: function () {
      _fetchViewConfig();
    },

    handleSidePanelToggle: function (bIsLandingPage) {
      _handleSidePanelToggle(bIsLandingPage);
    }
  }
})();

MicroEvent.mixin(HomeScreenStore);

GlobalStore.bind('global-change', HomeScreenStore.triggerChange);
TranslationStore.bind('translation-changed', HomeScreenStore.triggerChange);
RefactoringStore.bind('store-changed', HomeScreenStore.triggerChange);

export default HomeScreenStore;
