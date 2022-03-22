import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';
import SettingScreenProps from './../model/setting-screen-props';
import SettingUtils from './../helper/setting-utils';
import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import {ThemeConfigurationRequestMapping as oThemeConfigurationRequestMapping} from '../../tack/setting-screen-request-mapping';
import ThemeConfigurationConstants from '../../tack/mock/theme-configuration-constants';
import {communicator as HomeScreenCommunicator} from '../../../../store/home-screen-communicator';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
let ThemeConfigurationProps = SettingScreenProps.themeConfigurationProps;

let ThemeConfigurationStore = (function () {
  let _triggerChange = function () {
    ThemeConfigurationStore.trigger('theme-config-changed');
  };

  let _makeActiveThemeConfigurationScreenDirty = function () {
    let oThemeConfigurationModel = ThemeConfigurationProps.getThemeConfigurationData();
    SettingUtils.makeObjectDirty(oThemeConfigurationModel);
    return oThemeConfigurationModel.clonedObject;
  };

  let _handleThemeConfigurationSectionValueChanged = function (sContextKey, sValue = "") {
    let oThemeConfigurationData = _makeActiveThemeConfigurationScreenDirty();

    switch (sContextKey) {
      case ThemeConfigurationConstants.TITLE:
      case ThemeConfigurationConstants.WELCOME_MESSAGE:
      case ThemeConfigurationConstants.GENERAL_THEME_COLOR:
      case ThemeConfigurationConstants.GENERAL_SELECTION_COLOR:
      case ThemeConfigurationConstants.GENERAL_FONT_COLOR:
      case ThemeConfigurationConstants.GENERAL_SELECTION_COLOR:
      sValue = CS.trim(sValue);
      break;
    }
     oThemeConfigurationData[sContextKey] = sValue;
    _triggerChange();
  };

  let _handleThemeConfigurationSectionIconChanged = function (sContextKey, oValue = {}) {
    let oThemeConfigurationData = _makeActiveThemeConfigurationScreenDirty();
    if (sContextKey === ThemeConfigurationConstants.LOGIN_SCREEN_BACKGROUND_THUMB_KEY) {
      oThemeConfigurationData[ThemeConfigurationConstants.LOGIN_SCREEN_BACKGROUND_IMAGE_KEY] = oValue.imageKey || "";
      oThemeConfigurationData[ThemeConfigurationConstants.LOGIN_SCREEN_BACKGROUND_THUMB_KEY] = oValue.thumbKey || "";
    } else {
      oThemeConfigurationData[sContextKey] = oValue.thumbKey || "";
    }
  };

  let _handleThemeConfigurationHeaderActionClicked = function (sButtonId) {
    switch (sButtonId) {
      case "reset_theme_configuration":
        CustomActionDialogStore.showConfirmDialog(getTranslation().THEME_CONFIGURATION_RESET_WARNING, '', _handleSaveAction.bind(this, true));
        break;
    }
  };

  let _handleSaveAction = function (bResetConfiguration = false, oCallbackData = {}) {
    let sUrl = oThemeConfigurationRequestMapping.SaveThemeConfigurations;
    let oThemeConfigurationModel = ThemeConfigurationProps.getThemeConfigurationData();
    oThemeConfigurationModel = oThemeConfigurationModel.clonedObject;
    if (bResetConfiguration) {
      sUrl = oThemeConfigurationRequestMapping.ResetThemeConfigurations;
      oThemeConfigurationModel = {};
    }
    SettingUtils.csPostRequest(sUrl, {}, oThemeConfigurationModel, successSaveThemeConfiguration.bind(this, oCallbackData), failureSaveThemeConfiguration);
  };

  let _handleDiscardAction = function (oCallbackData = {}) {
    let oThemeConfigurationModel = ThemeConfigurationProps.getThemeConfigurationData();
    delete  oThemeConfigurationModel.clonedObject;
    delete oThemeConfigurationModel.isDirty;
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    if (CS.isFunction(oCallbackData.functionToExecute)) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  }

  let _fetchThemeConfigurationScreen = function () {
    SettingUtils.csGetRequest(oThemeConfigurationRequestMapping.GetThemeConfigurations, {}, successCallbackForFetchThemeConfigurationScreen, failureCallbackForFetchThemeConfigurationScreen);
  };

  let successSaveThemeConfiguration = function (oCallbackData, oResponse) {
    let oThemeConfigurationModel = oResponse.success;

    ThemeConfigurationProps.setThemeConfigurationData(oThemeConfigurationModel);

    alertify.success(getTranslation().SUCCESSFULLY_SAVED);
    HomeScreenCommunicator.handleThemeConfigurationChange(oThemeConfigurationModel);
    if (CS.isFunction(oCallbackData.functionToExecute)) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };


  let failureSaveThemeConfiguration = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureThemeConfigurationCallback", getTranslation());
  };


  let successCallbackForFetchThemeConfigurationScreen = function (oResponse) {
    let oThemeConfigurationModel = oResponse.success;
    ThemeConfigurationProps.setThemeConfigurationData(oThemeConfigurationModel);
    _triggerChange();
  };


  let failureCallbackForFetchThemeConfigurationScreen = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureThemeConfigurationCallback", getTranslation());
  };


  return {
    handleSaveAction: function (bResetConfiguration, oCallbackData) {
      _handleSaveAction(bResetConfiguration, oCallbackData);
    },

    handleDiscardAction: function (oCallbackData) {
      _handleDiscardAction(oCallbackData);
    },

    fetchThemeConfigurationScreen: function () {
      _fetchThemeConfigurationScreen();
    },

    handleThemeConfigurationSectionValueChanged: function (sContextKey, sValue) {
      _handleThemeConfigurationSectionValueChanged(sContextKey,sValue);
    },

    handleThemeConfigurationSectionIconChanged: function (sContextKey, oValue) {
      _handleThemeConfigurationSectionIconChanged(sContextKey, oValue);
    },

    handleThemeConfigurationSnackBarButtonClicked: function (sButtonId) {
      if (sButtonId === "save") {
        _handleSaveAction();
      } else if (sButtonId === "discard") {
        _handleDiscardAction();
      }
    },

    handleThemeConfigurationHeaderActionClicked: function (sButtonId) {
      _handleThemeConfigurationHeaderActionClicked(sButtonId);
    }
  }
})();

MicroEvent.mixin(ThemeConfigurationStore);

export default ThemeConfigurationStore;
