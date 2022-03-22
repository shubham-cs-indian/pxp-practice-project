import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import TabsConfigGridViewSkeleton from './../../tack/tabs-config-grid-view-skeleton-data';
import { SettingsRequestMapping as SettingScreenRequestMapping, TabsRequestMapping as oTabsRequestMapping }
  from '../../tack/setting-screen-request-mapping';
import TabsProps from './../model/tabs-config-view-props';
import SettingScreenProps from './../model/setting-screen-props';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import SettingUtils from '../helper/setting-utils';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import assetTypes from '../../tack/coverflow-asset-type-list';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import CommonUtils from "../../../../../../commonmodule/util/common-utils";

const TabsStore = (function () {

  let _triggerChange = function () {
    TabsStore.trigger('tabs-changed');
  };

  let _setUpTabsGrid = function () {
    let oTabsConfigGridViewSkeleton = new TabsConfigGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.TABS_CONFIG, {skeleton: oTabsConfigGridViewSkeleton});
    _fetchTabsListForGrid();
  };

  let _fetchTabsListForGrid = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.TABS_CONFIG);
    SettingUtils.csPostRequest(oTabsRequestMapping.GetAll, {}, oPostData, successFetchTabsListForGrid, failureFetchTabsListForGrid);
  };

  let successFetchTabsListForGrid = function (oResponse) {
    let oSuccess = oResponse.success;
    let aTabList = oSuccess.tabList;
    let oScreenProps = SettingScreenProps.screen;

    TabsProps.setTabList(aTabList);
    GridViewStore.preProcessDataForGridView(GridViewContexts.TABS_CONFIG, aTabList, oSuccess.count);
    //let aProcessedGridViewData = _preProcessTabsListForGrid(aTabList);
    //oScreenProps.setGridViewData(aProcessedGridViewData);
    //oScreenProps.setGridViewTotalItems(oSuccess.count);
    //oScreenProps.setIsGridDataDirty(false);
    _triggerChange();
  };

  let failureFetchTabsListForGrid = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTabsListForGrid", getTranslation());
  };

  let _discardSingleTabChanges = function (sId, aTabGridData) {
    let oTab = CS.find(aTabGridData, {id: sId});
    let oDiscardedProcessedTab = {};
    if (!CS.isEmpty(oTab)) {
      //oDiscardedProcessedTab = _preProcessSingleTabForGrid(oTab, oGridSkeleton);
      oDiscardedProcessedTab = GridViewStore.getProcessedGridViewData(GridViewContexts.TABS_CONFIG, [oTab])[0];
    }
    return oDiscardedProcessedTab;
  };

  let _discardTabListChanges = function (oCallBackData) {
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TABS_CONFIG);
    let aGridData = oGridViewPropsByContext.getGridViewData();
    let aTabList = TabsProps.getTabList();
    let bDiscardedFlag = false;
    CS.forEach(aGridData, function (oProcessedTab, iIndex) {
      if (oProcessedTab.isDirty) {
        aGridData[iIndex] = _discardSingleTabChanges(oProcessedTab.id, aTabList);
        bDiscardedFlag = true;
      }
    });

    bDiscardedFlag && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    oCallBackData && oCallBackData.functionToExecute && oCallBackData.functionToExecute();
  };

  let _deleteTabs = function (aSelectedTabIds, oCallBack) {
    if (CS.isEmpty(aSelectedTabIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return;
    }
    let aTabList = TabsProps.getTabList();
    let bIsAnyStandardTabSelected = false;
    CS.forEach(aSelectedTabIds, function (sId) {
      let oSelectedTab = CS.find(aTabList, {id: sId});
      bIsAnyStandardTabSelected = oSelectedTab.isStandard;
      return !bIsAnyStandardTabSelected;
    });

    if (bIsAnyStandardTabSelected) {
      alertify.message(getTranslation().STANDARD_TAB_DELETE);
      return;
    }

    let oSelectedTabsId = {};
    let aSelectedTabLabels = [];
    CS.forEach(aSelectedTabIds, function (sTabId) {
      oSelectedTabsId[sTabId] = true;
    });
    CS.forEach(aTabList, function (oTab) {
      if (oSelectedTabsId[oTab.id]) {
        let sTabLabel = CS.getLabelOrCode(oTab);
        aSelectedTabLabels.push(sTabLabel);
      }
    });
    CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aSelectedTabLabels,
        function (oEvent) {
          _deleteTabsCall(aSelectedTabIds, oCallBack)
          .then(_fetchTabsListForGrid);
        }, function (oEvent) {
        }, true);
  };

  let _deleteTabsCall = function (aSelectedIds,oCallBack) {
    return SettingUtils.csDeleteRequest(oTabsRequestMapping.BulkDelete, {}, {ids: aSelectedIds}, successDeleteTabs.bind(this, aSelectedIds), failureDeleteTabs.bind(this, oCallBack));

  };

  let successDeleteTabs = function (aSelectedIds, oResponse) {
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TABS_CONFIG);
    if (!CS.isEmpty(oResponse.failure.devExceptionDetails)) {
      failureDeleteTabs(oResponse);
    } else if (oResponse.success.length) {
      let aTabList = TabsProps.getTabList();
      let aGridData = oGridViewPropsByContext.getGridViewData();
      CS.forEach(aSelectedIds, function (sId) {
        CS.remove(aGridData, {id: sId});
        CS.remove(aTabList, {id: sId});
      });
      oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - aSelectedIds.length);
    }
    oGridViewPropsByContext.setIsGridDataDirty(false);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().TABS_MENU_ITEM_TILE}));
  };

  let failureDeleteTabs = function (oCallback, oResponse) {
    let configError = CS.reduce(oResponse.failure.exceptionDetails, (isConfigError, error) => {
      if (error.key === "EntityConfigurationDependencyException") {
        isConfigError = true;
      }
      return isConfigError;
    }, false);
    if (configError) {
      if (oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute();
        return;
      }
    }
    SettingUtils.failureCallback(oResponse, "failureDeleteTabs", getTranslation());
  };

  let _createDummyTab = function () {
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      code: "",
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true
    }
  };

  let _createTab = function () {
    TabsProps.setActiveTab(_createDummyTab());
  };

  let _handleSort = function () {
    TabsProps.setSortTabsDialogActive(true);
  };

  let _createTabCall = function () {
    let oActiveTabToCreate = TabsProps.getActiveTab();
    delete oActiveTabToCreate.isCreated;
    SettingUtils.csPutRequest(oTabsRequestMapping.Create, {}, oActiveTabToCreate, successCreateTabCall, failureCreateTabCall);
  };

  let successCreateTabCall = function (oResponse) {
    let oScreenProps = SettingScreenProps.screen;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TABS_CONFIG);
    let aGridData = oGridViewPropsByContext.getGridViewData();
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);
    let aTabList = TabsProps.getTabList();
    let oTabFromServer = oResponse.success.tab;
    aTabList.push(oTabFromServer);
    let oProcessedTab = GridViewStore.getProcessedGridViewData(GridViewContexts.TABS_CONFIG, [oTabFromServer])[0];
    aGridData.unshift(oProcessedTab);
    TabsProps.setActiveTab({});
    oScreenProps.setIsGridDataDirty(false);
    alertify.success(getTranslation().TAB_SUCCESSFULLY_CREATED)
    _triggerChange();
  };

  let failureCreateTabCall = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCreateTabCall", getTranslation());
  };

  let _checkUniquenessAndCallCreate = function () {
    let oActiveTabToCreate = TabsProps.getActiveTab();
    oActiveTabToCreate.label = CS.trim(oActiveTabToCreate.label);
    if (CS.isEmpty(oActiveTabToCreate.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }


    var oCodeToVerifyUniqueness = {
      id: oActiveTabToCreate.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_TABS
    };

    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = _createTabCall.bind(this);
    var sURL = SettingScreenRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  let _processTabsListAndSave = function (oCallBackData) {
    let aTabsToSave = [];
    let bSafeToSave = GridViewStore.processGridDataToSave(aTabsToSave, GridViewContexts.TABS_CONFIG, TabsProps.getTabList());
    if (bSafeToSave) {
      return SettingUtils.csPostRequest(oTabsRequestMapping.BulkSave, {}, aTabsToSave, successBulkSaveTabsList.bind(this, oCallBackData), failureBulkSaveTabsList);
    }
    return CommonUtils.rejectEmptyPromise();
  };

  let successBulkSaveTabsList = function (oCallBackData, oResponse) {
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TABS_CONFIG);
    let aGridData = oGridViewPropsByContext.getGridViewData();
    let aTabGridData = TabsProps.getTabList();
    let aTabList = oResponse.success.tabList;
    CS.forEach(aTabList,function(oTabData){
      let iIndex = CS.findIndex(aTabGridData, {id: oTabData.id});
      aTabGridData[iIndex] = oTabData;
    });
    TabsProps.setTabList(aTabGridData);
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.TABS_CONFIG, aTabList);

    /*------------------Updating Context Prop from Response---------------------*/
    CS.forEach(aTabList, function (oData) {
      let tabId = oData.id;
      let iIndex = CS.findIndex(aTabGridData, {id: tabId});
      aTabGridData[iIndex] = oData;
    });

    /*------------------Updating Grid Prop---------------------*/
    CS.forEach(aProcessedGridViewData, function (oProcessedTab) { //add the processed tab into processed data :
      let iIndex = CS.findIndex(aGridData, {id: oProcessedTab.id});
      aGridData[iIndex] = oProcessedTab;
    });

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().TABS}));
    oGridViewPropsByContext.setIsGridDataDirty(false);
    oCallBackData && oCallBackData.functionToExecute && oCallBackData.functionToExecute();

  };

  let failureBulkSaveTabsList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureBulkSaveTabsList", getTranslation());
  };

  let _handleSingleValueChanged = function (sKey, sVal) {
    let oActiveTab = TabsProps.getActiveTab();
    oActiveTab[sKey] = sVal;
  };

  let _fetchTabDetails = function (sEntityId) {
    SettingUtils.csGetRequest(oTabsRequestMapping.Get, {id: sEntityId}, successGetTabDetails, failureGetTabDetails);
  };

  let successGetTabDetails = function (oResponse) {
    let oSuccess = oResponse.success;
    TabsProps.setActiveTab(oSuccess.tab);
    TabsProps.setReferencedProperties(oSuccess.referencedProperties);
    _triggerChange();
  };

  let failureGetTabDetails = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGetTabDetails", getTranslation());
  };

  let _closeDialog = function () {
    TabsProps.setActiveTab({});
    _triggerChange();
  };

  let _closeSortDialog = function () {
    TabsProps.setSortTabsDialogActive(false);
    _triggerChange();
  };

  let _handleTabsConfigListItemShuffled = function (sId, iNewPosition) {
    let oActiveTab = TabsProps.getActiveTab();
    let oTabToSave = {
      id: oActiveTab.id,
      label: oActiveTab.label,
      icon: oActiveTab.icon,
      modifiedPropertySequence: [
        {
          id: sId,
          newSequence: iNewPosition
        }
      ]
    };
    SettingUtils.csPostRequest(oTabsRequestMapping.SaveShuffledSequence, {}, oTabToSave, successSaveTabsList, failureSaveTabsList);
  };

  let _handleTabsSortListItemShuffled = function (sId, iNewPosition, oModifiedTab) {
    let oTabToSave = {};
    oTabToSave.id = oModifiedTab.id;
    oTabToSave.modifiedTabSequence = iNewPosition + 1;
    oTabToSave.label = CS.trim(oModifiedTab.label);
    oTabToSave.code = oModifiedTab.code;
    oTabToSave.icon = oModifiedTab.icon;

    let oCallback = {functionToExecute: _fetchTabsListForGrid};
    return SettingUtils.csPostRequest(oTabsRequestMapping.BulkSave, {}, [oTabToSave], successBulkSaveTabsList.bind(this, oCallback), failureBulkSaveTabsList);
  };

  let failureSaveTabsList =  function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveTabsList", getTranslation());
  };

  let successSaveTabsList = function (Response) {
    let oResponse = Response.success;
    let oActiveTab = oResponse.tab;
    TabsProps.setActiveTab(oActiveTab);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().TABS}));
    _triggerChange();
  };

  let _handleExportTabs = function (oSelectiveExportDetails) {
    SettingUtils.csPostRequest(oSelectiveExportDetails.sUrl, {},oSelectiveExportDetails.oPostRequest, successExportFile, failureExportFile);
  };

  let successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
  };

  let failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
  };

  let _getIsValidFileTypes = function (oFile) {
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  let uploadFileImport = function (oImportExcel, oCallback) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  let successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
  };

  let _handleTabsFileUploaded = function (aFiles,oImportExcel) {
    var bIsAnyValidImage = false;
    var bIsAnyInvalidImage = false;
    if (aFiles.length) {

      var iFilesInProcessCount = 0;
      var count = 0;

      var data = new FormData();
      CS.forEach(aFiles, function (file, index) {
        if (_getIsValidFileTypes(file)) {
          bIsAnyValidImage = true;
          var reader = new FileReader();
          iFilesInProcessCount++;
          // Closure to capture the file information.
          reader.onload = (function (theFile) {
            return function (event) {
              count += 1;
              var filekey = UniqueIdentifierGenerator.generateUUID();
              data.append(filekey, theFile);
            };
          })(file);

          reader.onloadend = function () {
            iFilesInProcessCount--;
            if (iFilesInProcessCount == 0) {
              data.append("entityType", oImportExcel.entityType);
              oImportExcel.data = data;
              uploadFileImport(oImportExcel, {});
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsAnyInvalidImage = true;
        }
      });
    }
  };

  /************************************** PUBLIC API's **************************************/
  return {

    setUpTabsGrid: function () {
      _setUpTabsGrid();
    },

    fetchTabListForGrid: function () {
      _fetchTabsListForGrid();
    },

    createTab: function () {
      _createTab();
      _triggerChange();
    },

    handleSortButtonClicked: function () {
      _handleSort();
      _triggerChange();
    },

    handleCreateDialogButtonClicked: function (sButtonId) {
      if (sButtonId == "create") {
        _checkUniquenessAndCallCreate();
      } else {
        TabsProps.setActiveTab({});
        _triggerChange();
      }
    },

    processTabsListAndSave: function (oCallBack) {
      _processTabsListAndSave(oCallBack)
          .then(_triggerChange);
    },

    discardTabListChanges: function (oCallBack) {
      _discardTabListChanges(oCallBack);
      _triggerChange();
    },

    deleteTabs: function (aSelectedIds, oCallBack) {
      _deleteTabs(aSelectedIds, oCallBack);
    },

    handleSingleValueChanged: function (sKey, sVal) {
      _handleSingleValueChanged(sKey, sVal);
      _triggerChange();
    },

    fetchTabDetails: function (sEntityId) {
      _fetchTabDetails(sEntityId);
    },

    handleTabsConfigDialogButtonClicked: function (sButtonId) {
      _closeDialog();
    },

    handleTabsSortDialogButtonClicked: function () {
      _closeSortDialog();
    },

    handleTabsConfigListItemShuffled: function (sId, iNewPosition) {
      _handleTabsConfigListItemShuffled(sId, iNewPosition);
    },

    handleTabsSortListItemShuffled: function (sId, iNewPosition, changedTab) {
      _handleTabsSortListItemShuffled(sId, iNewPosition, changedTab)
          .then(_triggerChange);
    },

    handleExportTabs: function (oSelectiveExportDetails) {
      _handleExportTabs(oSelectiveExportDetails);
    },

    handleTabsFileUploaded: function (aFiles,oImportExcel) {
      _handleTabsFileUploaded(aFiles,oImportExcel);
    },

  };

})();

MicroEvent.mixin(TabsStore);
export default TabsStore;
