import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import OrganisationConfigViewProps from './../model/organisation-config-view-props';
import SSOSettingConfigViewProps from './../model/sso-setting-config-view-props';
import SettingUtils from './../helper/setting-utils';
import UniqueIdentifierGenerator
  from "../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator";
import GridViewContexts from "../../../../../../commonmodule/tack/grid-view-contexts";
import SSOSettingGridViewSkeleton from './../../tack/sso-setting-grid-view-skeleton';
import ConfigEntityTypeDictionary from "../../../../../../commonmodule/tack/config-entity-type-dictionary";
import {
  SSOSettingRequestMapping as oSSOSettingConfigRequestMapping,
  TagRequestMapping as oTagRequestMapping
} from "../../tack/setting-screen-request-mapping";
import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';

const SSOSettingStore = (function () {

  let _triggerChange = function () {
    SSOSettingStore.trigger('sso-setting-changed');
  };

  let _createDefaultSSOMasterObject = function () {
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      code: "",
      domain: UniqueIdentifierGenerator.generateUntitledName(),
      idp: "",
      isCreated: true,
      organizationId: OrganisationConfigViewProps.getActiveOrganisation().id,
      type: ""
    }
  };

  let _createNewSSOSetting = function () {
    let oCreatedSSOMaster = _createDefaultSSOMasterObject();
    SSOSettingConfigViewProps.setActiveSSOSetting(oCreatedSSOMaster);
  };

  let successCreateSSOCallback = function (oResponse) {
    let oSavedSSO = oResponse.success;
    SSOSettingConfigViewProps.setActiveSSOSetting(oSavedSSO);
    let oProcessedSSO = GridViewStore.getProcessedGridViewData(GridViewContexts.SSO_SETTING, [oSavedSSO], {})[0];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.SSO_SETTING);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedSSO);

    let oMasterSSOSettingList = SSOSettingConfigViewProps.getSSOSettingsList(); // get master SSO list
    oMasterSSOSettingList.push(oSavedSSO);

    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1); //increase total count when new SSO created
    alertify.success(getTranslation().SSO_SETTING_CREATED_SUCCESSFULLY);
    _triggerChange();
  };

  let failureCreateSSOCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveSSOCallback", getTranslation());
  };

  let _createSSOSettingDialogClick = function () {
    let oSelectedSSO = SSOSettingConfigViewProps.getActiveSSOSetting();
    oSelectedSSO = oSelectedSSO.clonedObject || oSelectedSSO;
    let oCodeToVerifyUniqueness = {
      id: oSelectedSSO.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_SSO_SETTING
    };

    if(!oSelectedSSO.idp) {
      alertify.error(getTranslation().SSO_SETTING_SHOULD_NOT_BE_EMPTY);
      return;
    }

    let oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = function() {
      _createSSOCall(oSelectedSSO);
    };

    let sURL = oTagRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  let _createSSOCall = function (oSelectedSSO) {
    SettingUtils.csPutRequest(oSSOSettingConfigRequestMapping.Create, {}, oSelectedSSO, successCreateSSOCallback, failureCreateSSOCallback);
  };

  let _saveSSOInBulk = function (aSSOListToSave, oCallBack = {}) {
      return SettingUtils.csPostRequest(oSSOSettingConfigRequestMapping.Save, {}, aSSOListToSave, successSaveSSOInBulk.bind(this, oCallBack), failureSaveSSOInBulk);
  };

  let _discardSSOGridViewChanges = function (oCallbackData = {}) {
    let aSSOSettingList = SSOSettingConfigViewProps.getSSOSettingsList();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.SSO_SETTING);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedSSO, iIndex) { //add the successfully saved SSOs into stored data:
      if (oOldProcessedSSO.isDirty) {
        // get the original SSO object and reprocess it -
        let oSSO = CS.find(aSSOSettingList, {id: oOldProcessedSSO.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.SSO_SETTING, [oSSO])[0];
        bShowDiscardMessage = true;
      }
    });

    bShowDiscardMessage && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (CS.isFunction(oCallbackData.functionToExecute)) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let failureSaveSSOInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveSSOInBulk", getTranslation());
  };

  let successSaveSSOInBulk = function (oCallBack, oResponse) {
    let aSSOList = oResponse.success.ssoList;
    let oConfigDetails = oResponse.success.configDetails;
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.SSO_SETTING, aSSOList, "", oConfigDetails);
    let aSSOSettingList = SSOSettingConfigViewProps.getSSOSettingsList();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.SSO_SETTING);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();

    CS.forEach(aSSOList, function (oSSOList) { //add the successfully saved SSOs into stored data:
      let SSOId = oSSOList.id;
      let iIndex = CS.findIndex(aSSOSettingList, {id: SSOId});
      aSSOSettingList[iIndex] = oSSOList;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedSSO) { //add the processed SSOs into processed data :
      let iIndex = CS.findIndex(aGridViewData, {id: oProcessedSSO.id});
      aGridViewData[iIndex] = oProcessedSSO;
    });

    alertify.success(getTranslation().SSO_SETTING_SAVE_SUCCESSFULLY);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (CS.isFunction(oCallBack.functionToExecute)) {
      oCallBack.functionToExecute();
    }
  };

  let _getTypeForSSOSetting = (sId) => {
    let aSSOSettingConfigurationList = SSOSettingConfigViewProps.getSSOSettingsConfigurationList();
    let oSSOSetting = CS.find(aSSOSettingConfigurationList, {id: sId});
    return oSSOSetting.type;
  };

  let _postProcessSSOListAndSave = function (oCallBack) {
    let aSSOSettingList = SSOSettingConfigViewProps.getSSOSettingsList();
    let aSSOListToSave = [];

    GridViewStore.processGridDataToSave(aSSOListToSave, GridViewContexts.SSO_SETTING, aSSOSettingList);

    return _saveSSOInBulk(aSSOListToSave, oCallBack);
  };

  let _deleteSSOSetting = function (aBulkDeleteList) {
    return SettingUtils.csDeleteRequest(oSSOSettingConfigRequestMapping.BulkDelete, {}, {ids: aBulkDeleteList}, successDeleteSSOCallback, failureDeleteSSOCallback);
  };

  let _deleteSSO = function (aSSOIdsListToDelete) {
    let oSSOSetting = SSOSettingConfigViewProps.getSSOSettingsList();

    if (!CS.isEmpty(aSSOIdsListToDelete)) {
      let aBulkDeleteSSO = [];
      CS.forEach(aSSOIdsListToDelete, function (sSSO) {
        let oMasterSSOSetting = CS.find(oSSOSetting, {id: sSSO});
        aBulkDeleteSSO.push(oMasterSSOSetting.domain);
      });
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteSSO,
          function () {
            _deleteSSOSetting(aSSOIdsListToDelete)
            .then(_fetchSSOListForGridView);
          }, function (oEvent) {
          });
    } else {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
    }
  };

  let successDeleteSSOCallback = function (oResponse) {
    let aSuccessIds = oResponse.success;
    let aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];

    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.SSO_SETTING);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let aMasterSSOSettingList = SSOSettingConfigViewProps.getSSOSettingsList(); // get master SSO list
    let oSkeleton = oGridViewPropsByContext.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      CS.remove(aMasterSSOSettingList, {id: sId});
      CS.remove(oSkeleton.selectedContentIds, function (oSelectedId) {
        return oSelectedId == sId;
      });
    });
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - aSuccessIds.length); //remove the number of deleted SSO from total count

    alertify.success(getTranslation().SSO_SETTING_DELETE_SUCCESSFULLY);

    if (aFailureIds && aFailureIds.length > 0) {
      handleDeleteSSOFailure(aFailureIds);
    }
  };

  let handleDeleteSSOFailure = function (List) {
    let aSSOAlreadyDeleted = [];
    let aUnhandledSSO = [];
    let aSSOGridData = SSOSettingConfigViewProps.getSSOSettingsList();
    CS.forEach(List, function (oItem) {
      let oSSO = CS.find(aSSOGridData, {id: oItem.id});
      if (oItem.key == "SSONotFoundException") {
        aSSOAlreadyDeleted.push(oSSO.domain);
      } else {
        aUnhandledSSO.push(oSSO.domain);
      }
    });

    if (aSSOAlreadyDeleted.length > 0) {
      let sSSOAlreadyDeleted = aSSOAlreadyDeleted.join(',');
      alertify.error(Exception.getCustomMessage("ERROR_SSO_IS_DELETED", getTranslation(), sSSOAlreadyDeleted), 0); // eslint-disable-line
    }
    if (aUnhandledSSO.length > 0) {
      let sUnhandledSSO = aUnhandledSSO.join(',');
      alertify.error(Exception.getCustomMessage("ERROR_SSO_IS_UNHANDLED", getTranslation(), sUnhandledSSO), 0); // eslint-disable-line
    }
  };

  let failureDeleteSSOCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      handleDeleteSSOFailure(oResponse.failure.exceptionDetails);
    } else {
      SettingUtils.failureCallback(oResponse, "failureDeleteSSOCallback", getTranslation());
    }
    _triggerChange();
  };

  let _cancelSSOSettingDialogClick = function () {
    let oActiveSSO = SSOSettingConfigViewProps.getActiveSSOSetting();
    delete oActiveSSO.clonedObject;
    delete oActiveSSO.isDirty;
    delete oActiveSSO.isCreated;
    _triggerChange();
  };

  let _handleCreateSSODialogButtonClicked = function (sButtonId) {
    if (sButtonId === "create") {
      _createSSOSettingDialogClick();
    } else {
      _cancelSSOSettingDialogClick();
    }
  };

  let _fetchSSOListForGridView = function (oCallbackData) {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.SSO_SETTING);
    oPostData.organizationId = OrganisationConfigViewProps.getActiveOrganisation().id;
    SettingUtils.csPostRequest(oSSOSettingConfigRequestMapping.BulkGetSSO, {}, oPostData, successFetchSSOListForGridView.bind(this, oCallbackData), failureFetchSSOListForGridView);
  };

  let successFetchSSOListForGridView = function (oCallback, oResponse) {
    let oResponseData = oResponse.success;
    let ssoList = oResponseData.ssoList;
    let oConfigDetails = oResponseData.configDetails;
    SSOSettingConfigViewProps.setSSOSettingsList(ssoList);
    GridViewStore.preProcessDataForGridView(GridViewContexts.SSO_SETTING, ssoList, oResponseData.count, oConfigDetails);
    oCallback && oCallback.functionToExecute();

    _triggerChange();
  };

  let failureFetchSSOListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchSSOListForGridView", getTranslation());
  };

  let _setUpSSOSettingConfigGridView = function (oCallbackData) {
    let oData = {
      sortBy: "domain",
      searchBy: "domain",
      skeleton: SSOSettingGridViewSkeleton()
    };
    GridViewStore.createGridViewPropsByContext(GridViewContexts.SSO_SETTING, oData);
    _fetchSSOListForGridView(oCallbackData);
  };

  let _makeActiveSSOSettingDirty = function () {
    let oActiveSSO = SSOSettingConfigViewProps.getActiveSSOSetting();
    SettingUtils.makeObjectDirty(oActiveSSO);
    return oActiveSSO.clonedObject;
  };

  let _handleSSOSingleTextChanged = function (sKey, sValue) {
    let oActiveSSO = _makeActiveSSOSettingDirty();
    oActiveSSO[sKey] = sValue;
    _triggerChange();
  };

  let _fetchSSOSettingsConfigurationList = function () {
    SettingUtils.csGetRequest(oSSOSettingConfigRequestMapping.GetIDPConfiguration, {}, successFetchSSOSettingsConfigurationList, failureFetchSSOSettingsConfigurationList);
  };

  let successFetchSSOSettingsConfigurationList = function (oResponse) {
    let oSSOSettingsList = oResponse.success.idpConfiguration;
    SSOSettingConfigViewProps.setSSOSettingsConfigurationList(CS.values(oSSOSettingsList));
    _triggerChange();
  };

  let failureFetchSSOSettingsConfigurationList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchSSOSettingsConfigurationList", getTranslation);
  };

  let _handleSSOSettingSelected = function (aSelectedItems) {
    let oActiveSSOSetting = _makeActiveSSOSettingDirty();
    oActiveSSOSetting.idp = aSelectedItems[0];
    oActiveSSOSetting.type = _getTypeForSSOSetting(aSelectedItems[0]);
    _triggerChange();
  };

  return {

    setUpSSOSettingConfigGridView: function (oCallbackData) {
      _setUpSSOSettingConfigGridView(oCallbackData);
    },

    createSSOSetting: function () {
      _createNewSSOSetting();
      _triggerChange();
    },

    handleCreateSSODialogButtonClicked: function (sButtonId) {
      _handleCreateSSODialogButtonClicked(sButtonId);
      _triggerChange();
    },

    handleSSOSingleTextChanged: function (sKey, sValue) {
      _handleSSOSingleTextChanged(sKey, sValue);
    },

    deleteSSO: function (aSSOIdsListToDelete) {
      _deleteSSO(aSSOIdsListToDelete);
    },

    postProcessSSOListAndSave: function (oCallBack) {
      _postProcessSSOListAndSave(oCallBack)
          .then(_triggerChange);
    },

    discardSSOGridViewChanges: function (oCallbackData) {
      _discardSSOGridViewChanges(oCallbackData);
    },

    fetchSSOListForGridView: function () {
      _fetchSSOListForGridView();
    },

    fetchSSOSettingsConfigurationList: function () {
      _fetchSSOSettingsConfigurationList();
    },

    handleSSOSettingSelected: function (aSelectedItems) {
      _handleSSOSettingSelected(aSelectedItems);
  }

  }
})();

MicroEvent.mixin(SSOSettingStore);

export default SSOSettingStore;
