import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import SettingScreenProps from './../model/setting-screen-props';
import SettingUtils from './../helper/setting-utils';
import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import {ViewConfigurationRequestMapping as oViewConfigurationRequestMapping} from '../../tack/setting-screen-request-mapping';
import ThemeConfigurationConstants from '../../tack/mock/theme-configuration-constants';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import {communicator as HomeScreenCommunicator} from '../../../../store/home-screen-communicator';
let ViewConfigurationProps = SettingScreenProps.viewConfigurationProps;
let ViewConfigurationStore = (function () {

  let _triggerChange = function () {
    ViewConfigurationStore.trigger('view-config-changed');
  };


  let _makeActiveThemeConfigurationScreenDirty = function () {
    let oViewConfigurationModel = ViewConfigurationProps.getViewConfigurationData();
    SettingUtils.makeObjectDirty(oViewConfigurationModel);
    return oViewConfigurationModel.clonedObject;
  };

  let _handleSaveAction = function (bResetConfiguration = false) {
    let sUrl = oViewConfigurationRequestMapping.SaveViewConfigurations;
    let oViewConfigurationModel = ViewConfigurationProps.getViewConfigurationData();
    oViewConfigurationModel = oViewConfigurationModel.clonedObject;
    if (bResetConfiguration) {
      sUrl = oViewConfigurationRequestMapping.ResetViewConfigurations;
      oViewConfigurationModel = {};
    }
    SettingUtils.csPostRequest(sUrl, {}, oViewConfigurationModel, successSaveViewConfiguration, failureSaveViewConfiguration);
  };

  let _handleDiscardAction = function () {
    let oViewConfigurationModel = ViewConfigurationProps.getViewConfigurationData();
    delete  oViewConfigurationModel.clonedObject;
    delete oViewConfigurationModel.isDirty;
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    _triggerChange();
  }

  let successSaveViewConfiguration = function (oResponse) {
    let oViewConfigurationModel = oResponse.success;
    ViewConfigurationProps.setViewConfigurationData(oViewConfigurationModel);
    alertify.success(getTranslation().SUCCESSFULLY_SAVED);
    _triggerChange();
  };

  let failureSaveViewConfiguration = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureViewConfigurationCallback", getTranslation());
  };

  let _handleViewConfigurationRadioButtonClicked = function (sContext, sRadioKey) {
    let oThemeConfigurationData = _makeActiveThemeConfigurationScreenDirty();

    switch (sContext) {
      case ThemeConfigurationConstants.LANDING_PAGE:
        oThemeConfigurationData.isLandingPageExpanded = (sRadioKey === "expand");
        break;

      case ThemeConfigurationConstants.PRODUCT_INFORMATION_PAGE:
        oThemeConfigurationData.isProductInfoPageExpanded = (sRadioKey === "expand");
        break;
    }
  };

  let _fetchViewConfigurationScreen = function () {
    return SettingUtils.csGetRequest(oViewConfigurationRequestMapping.GetViewConfigurations, {}, successCallbackForFetchViewConfigurationScreen, failureCallbackForFetchViewConfigurationScreen);
  };

  let successCallbackForFetchViewConfigurationScreen = function (oResponse) {
    let oViewConfigurationModel = oResponse.success;
    ViewConfigurationProps.setViewConfigurationData(oViewConfigurationModel);
  };

  let failureCallbackForFetchViewConfigurationScreen = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureThemeConfigurationCallback", getTranslation());
  };

  let _handleViewConfigurationHeaderActionClicked = function (sButtonId) {
    switch (sButtonId) {
      case "reset_view_configuration":
        CustomActionDialogStore.showConfirmDialog(getTranslation().VIEW_CONFIGURATION_RESET_WARNING, '', _handleSaveAction.bind(this, true));
        break;
    }
  };

  return {
    handleSaveAction: function () {
      _handleSaveAction();
    },

    handleDiscardAction: function () {
      _handleDiscardAction();
    },

    fetchViewConfigurationScreen: function () {
      _fetchViewConfigurationScreen().then(_triggerChange);
    },

    handleViewConfigurationSnackBarButtonClicked: function (sButtonId) {
      if (sButtonId === "save") {
        _handleSaveAction();
      } else if (sButtonId === "discard") {
        _handleDiscardAction();
      }
    },

    handleViewConfigurationRadioButtonClicked: function (sContext, sRadioKey) {
      _handleViewConfigurationRadioButtonClicked(sContext, sRadioKey);
    },

    handleViewConfigurationHeaderActionClicked: function (sButtonId) {
      _handleViewConfigurationHeaderActionClicked(sButtonId);
    }
  }
})();

MicroEvent.mixin(ViewConfigurationStore);

export default ViewConfigurationStore;
