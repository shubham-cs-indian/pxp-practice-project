/**
 * Created by CS80 on 4-4-2017.
 */

import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import {MappingRequestMapping} from '../../tack/setting-screen-request-mapping';
import MappingProps from './../model/mapping-config-view-props';
import SettingUtils from './../helper/setting-utils';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import MappingConfigGridViewSkeleton from './../../tack/mapping-config-grid-view-skeleton';
import SettingScreenProps from './../model/setting-screen-props';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import SaveAsDialogGridViewSkeleton from "../../tack/save-as-dialog-grid-view-skeleton";
import MockDataForMappingTypes from "../../../../../../commonmodule/tack/mock-data-for-mapping-types";
import MappingTypeDictionary from "../../../../../../commonmodule/tack/mapping-type-dictionary";
import MockDataForOutboundMapSummaryHeader from '../../tack/mock/mock-data-for-outbound-map-summary-header';
import TabHeaderData from "../../tack/mock/mock-data-for-map-summery-header";
import GridViewStore from "../../../../../../viewlibraries/contextualgridview/store/grid-view-store";

var MappingStore = (function () {

  var _triggerChange = function () {
    MappingStore.trigger('mapping-changed');
  };

  var _getSelectedMapping = function () {
    return MappingProps.getSelectedMapping();
  };

  var _makeActiveMappingDirty = function () {
    var oActiveMapping = MappingProps.getSelectedMapping();
    if (!oActiveMapping.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveMapping);
    }
    return oActiveMapping.clonedObject;
  };

  /**Server Callbacks **/
  var successFetchMappingListCallback = function (oResponse) {
    let aMappingDataList = oResponse.success.list;
    GridViewStore.preProcessDataForGridView(GridViewContexts.MAPPING, aMappingDataList, oResponse.success.count);
    SettingUtils.getAppData().setMappingList(aMappingDataList);
    MappingProps.setMappingGridData(aMappingDataList);
    _triggerChange();
  };

  var failureFetchMappingListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchMappingListCallback", getTranslation());
  };

  var successSaveMappingCallback = function (oCallbackData, oResponse) {
    successGetMappingCallback(oCallbackData, oResponse);
  };

  var failureSaveMappingCallback = function (oCallbackData, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveMappingCallback", getTranslation());
  };

  var successDeleteMappingsCallback = function (oResponse) {
    var oMasterMappingList = SettingUtils.getAppData().getMappingList();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.MAPPING);

    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oGridViewSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
    let aSuccessIds = oResponse.success;
    _handleBeforeDeleteMappings(oResponse.success);
    CS.forEach(oResponse.success, function (id) {
      CS.remove(aGridViewData, {id: id});
      CS.pull(oGridViewSkeleton.selectedContentIds, id);
      delete oMasterMappingList[id];
    });

    //let aProcessedGridViewData = _preProcessMappingDataForGridView(oMasterMappingList);
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.MAPPING, oMasterMappingList);
    oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - aSuccessIds.length);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().MAPPING}));

    if (oResponse.failure) {
      var oSelectedMapping = _getSelectedMapping();
      handleDeleteMappingFailure(oResponse.failure.exceptionDetails, oMasterMappingList, oSelectedMapping);
    }
  };

  var failureDeleteMappingsCallback = function (oCallback, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
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
      var oMasterMappingList = SettingUtils.getAppData().getMappingList();
      var oSelectedMapping = _getSelectedMapping();
      handleDeleteMappingFailure(oResponse.failure.exceptionDetails, oMasterMappingList, oSelectedMapping);
    } else {
      SettingUtils.failureCallback(oResponse, "failureDeleteMappingsCallback", getTranslation());
    }
    _triggerChange();
  };

  var successGetMappingCallback = function (oCallback, oResponse) {
    var oMappingFromServer = oResponse.success;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.MAPPING);

    /** Active process pushed in grid data**/
    let aMappingGridViewData = MappingProps.getMappingGridData();
    let iIndex = CS.findIndex(aMappingGridViewData, {id: oMappingFromServer.id});
    if (iIndex != -1) {
      aMappingGridViewData[iIndex] = oMappingFromServer;
    } else {
      aMappingGridViewData.push(oMappingFromServer);
    }

    let oScreenProps = SettingScreenProps.screen;
    if(oCallback.isCreated){
      //let oProcessedGridViewData = _preProcessMappingDataForGridView([oMappingFromServer])[0];
      let oProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.MAPPING, [oMappingFromServer])[0];
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
      aGridViewData.unshift(oProcessedGridViewData);
      oGridViewPropsByContext.setGridViewTotalItems(oScreenProps.getGridViewTotalItems() + 1 );
      let oMappingList = SettingUtils.getAppData().getMappingList();
      oMappingList[oMappingFromServer.id] = oMappingFromServer;
    }else{
      //let aProcessedGridViewData = _preProcessMappingDataForGridView(oMasterMappingList);
      let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.MAPPING, aMappingGridViewData);
      oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);

    }

    MappingProps.setAttributeMappings(oMappingFromServer.attributeMappings);
    MappingProps.setTagMappings(oMappingFromServer.tagMappings);
    _setSelectedMapping(oMappingFromServer);
    _updateReferencedDataForMapping(oMappingFromServer.configDetails);

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    if (oCallback.isCreated) {
      /** To open edit view immediately after create */
      MappingProps.setIsMappingDialogActive(true);
    }

    _triggerChange();
  };

  var failureGetMappingCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureGetMappingCallback', getTranslation());
  };

  let _setUpMappingConfigGridView = function () {
    let oMappingConfigGridViewSkeleton = new MappingConfigGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.MAPPING, {skeleton: oMappingConfigGridViewSkeleton});
    _fetchMappingList();
  }

/*
  let _getMSSObject = function (sLabel, bIsDisabled, aItems, aSelectedItems, sContext, oRegResponseInfo, sPropertyType, bIsSingleSelect) {
    let oReferencedData = {};
    switch (sPropertyType) {
      case "mappings":
        oReferencedData = MappingProps.getReferencedMappings();
        break;
      case "mappingType":
        let aMockDataForMappingTypes = new MockDataForMappingTypes();
        let oMappingType = CS.find(aMockDataForMappingTypes, function (oMappingType) {
          return aSelectedItems[0] == oMappingType.id
        });
        oReferencedData[oMappingType.id] = oMappingType;
        break;
    }

    return {
      label: sLabel,
      disabled: bIsDisabled,
      items: aItems,
      selectedItems: aSelectedItems,
      singleSelect: bIsSingleSelect,
      context: sContext,
      requestResponseInfo: oRegResponseInfo,
      referencedData: oReferencedData
    };
  };
*/

/*
  let _preProcessMappingDataForGridView = function (aMappingDataList) {
    let oGridSkeleton = {} //SettingScreenProps.screen.getGridViewSkeleton();
    let aGridViewData = [];

    CS.forEach(aMappingDataList, function (oMapping) {
      let oProcessedMapping = {};
      oProcessedMapping.id = oMapping.id;
      oProcessedMapping.isExpanded = false;
      oProcessedMapping.children = [];
      oProcessedMapping.actionItemsToShow = ["manageEntity", "edit","delete"];
      oProcessedMapping.properties = {};
      CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
        switch (oColumn.id) {

          case "label":
            if (oMapping.hasOwnProperty(oColumn.id)) {
              oProcessedMapping.properties[oColumn.id] = {
                value: oMapping[oColumn.id],
                isDisabled: false,
                bIsMultiLine: false,
                placeholder: oMapping["code"]
              };
            }
            break;
          case "code":
            if (oMapping.hasOwnProperty(oColumn.id)) {
              oProcessedMapping.properties[oColumn.id] = {
                value: oMapping[oColumn.id],
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;
          case "mappingType":
            let aMockDataForMappingTypes = new MockDataForMappingTypes();
            let oMappingTypeMSSObject = _getMSSObject("", true, aMockDataForMappingTypes, [oMapping.mappingType], "mappings", {}, "mappingType");
            if (oMapping.hasOwnProperty(oColumn.id)) {
              oProcessedMapping.properties[oColumn.id] = oMappingTypeMSSObject;
              oProcessedMapping.properties[oColumn.id].value = oMappingTypeMSSObject.selectedItems;
              oProcessedMapping.properties[oColumn.id].isDisabled = true
            }
            break;

        }
      });

      aGridViewData.push(oProcessedMapping);
    });

    return aGridViewData;
  };
*/


  let _processProcessMappingListAndSave = function (oCallBackData) {
    let aMappingToSave = [];
    let bStopSaveFlag = false;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.MAPPING);
    let aGridData = oGridViewPropsByContext.getGridViewData();
    CS.forEach(aGridData, function (oProcessedMapping) {
      if(oProcessedMapping.isDirty){
        let oMappingToSave = {};
        oMappingToSave.id = oProcessedMapping.id;
        let oProperties = oProcessedMapping.properties;
        oMappingToSave.label = CS.trim(oProperties.label.value);

        if(CS.isEmpty(oMappingToSave.label)){
          bStopSaveFlag = true;
          return false;
        }
        aMappingToSave.push(oMappingToSave);
      }
    });

    if(CS.isEmpty(aMappingToSave)){
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
    } else if (bStopSaveFlag) {
      alertify.message(getTranslation().STORE_ALERT_CONTENT_NAME_INPUT_MESSAGE);
    } else {
      let oPostRequest = [];
      let oData = {};
      oData.codes = [];
      oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTY_MAPPING;
      oData.names = [];
      CS.forEach(aMappingToSave, function (oMappingToSave) {
        oData.names.push(oMappingToSave.label);
      });
      oPostRequest.push(oData);
      oCallBackData.serverCallback = {
        requestURL: MappingRequestMapping.BulkSave,
        mappingToSave: aMappingToSave,
        isBulkSave: true,
      }
      let aUniqueNameList = CS.uniq(oData.names);
      if (aUniqueNameList.length !== oData.names.length) {
        alertify.error(getTranslation().MAPPING_NAME_ALREADY_EXISTS);
      } else {
        SettingUtils.csPostRequest(MappingRequestMapping.BulkCodeCheck, {}, oPostRequest, successBulkCodeCheckList.bind(this, oCallBackData), failureBulkCodeCheckList);
      }
    }
  };

  let successBulkSaveMappingList = function (oCallBackData, oResponse) {
    let oScreenProps = SettingScreenProps.screen;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.MAPPING);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let aMappingList = oResponse.success.list;
    let aMappingGridData = MappingProps.getMappingGridData();
    let aMappingListMap = SettingUtils.getAppData().getMappingList();
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.MAPPING, aMappingList);
    CS.forEach(aGridViewData, function (oData) {
      oData.isDirty = false;
    });

    CS.forEach(aMappingList, function (oMapping) {
      var mappingId = oMapping.id;
      var iIndex = CS.findIndex(aMappingGridData, {id:  mappingId});
      if(iIndex != -1) {
        aMappingGridData[iIndex] = oMapping;
        aMappingListMap[mappingId] = oMapping;
      }else if(oCallBackData.isSaveAsDialogActive && (iIndex === -1)){
        aMappingGridData.unshift(oMapping);
        //let aProcessedGridViewData = _preProcessMappingDataForGridView([oMapping])[0];
        let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.MAPPING, [oMapping])[0];
        aGridViewData.unshift(aProcessedGridViewData);
        aMappingListMap[mappingId] = oMapping;
      }
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedMapping) {
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedMapping.id});
      aGridViewData[iIndex] = oProcessedMapping;
    });

    /**Deselecting the checkboxes after saving **/
    if(oCallBackData && oCallBackData.isSaveAsDialogActive){
      let oGridSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
      oGridSkeleton.selectedContentIds = [];
    }
    if (oCallBackData && oCallBackData.isSaveAsCall) {
      alertify.success(getTranslation().MAPPING_CLONED_SUCCESSFULLY);
    }else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().MAPPING } ) );
    }

    oGridViewPropsByContext.setIsGridDataDirty(false);
    oCallBackData && oCallBackData.functionToExecute && oCallBackData.functionToExecute();
    _triggerChange();
  };

  let failureBulkSaveMappingList = function (oResponse) {
    SettingUtils.failureCallback(oResponse,"failureBulkSaveMappingList",getTranslation());
  };


  let _discardMappingListChanges = function (oCallbackData) {

    var aMappingGridData =  MappingProps.getMappingGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.MAPPING);
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    var bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedAttribute, iIndex) {
      if (oOldProcessedAttribute.isDirty) {
        var oMapping = CS.find(aMappingGridData, {id: oOldProcessedAttribute.id});
        //aGridViewData[iIndex] = _preProcessMappingDataForGridView([oMapping])[0];
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.MAPPING, [oMapping])[0];
        bShowDiscardMessage = true;
      }
    });

    bShowDiscardMessage && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };


  /**Private API's ***/

  var handleDeleteMappingFailure = function (List, oMasterMappingList, oSelectedMapping) {
    var aAlreadyDeletedMappings = [];
    var aUnhandledMappings = [];
    CS.forEach(List, function (oItem) {
      if (oItem.key == "MappingNotFoundException") {
        aAlreadyDeletedMappings.push(oMasterMappingList[oItem.itemId].firstName);


        if (oSelectedMapping.id && oSelectedMapping.id == oItem.itemId) {
          _setSelectedMapping({});
        }

        delete oMasterMappingList[oItem.itemId];
      } else {
        aUnhandledMappings.push(oMasterMappingList[oItem.itemId].firstName);
      }
    });

    if (aAlreadyDeletedMappings.length > 0) {
      var sMappingAlreadyDeleted = aAlreadyDeletedMappings.join(',');
      alertify.error(Exception.getCustomMessage("Mapping_already_deleted", getTranslation(), sMappingAlreadyDeleted), 0);
    }
    if (aUnhandledMappings.length > 0) {
      var sUnhandledMapping = aUnhandledMappings.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_Mapping", getTranslation(), sUnhandledMapping), 0);
    }
  };


  var _handleBeforeDeleteMappings = function (aMappingIds) {
    var sSelectedMappingId = _getSelectedMapping().id;
    if (CS.indexOf(aMappingIds, sSelectedMappingId) >= 0) {
      _setSelectedMapping({});
    }
  };

  var _setSelectedMapping = function (oMapping) {
    MappingProps.setSelectedMapping(oMapping);
  };

  var _fetchMappingList = function (bLoadMore) {
    //bLoadMore = bLoadMore || false;
    // let oPostData = SettingUtils.getEntityListViewLoadMorePostData(MappingProps, bLoadMore);
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.MAPPING);
    SettingUtils.csPostRequest(MappingRequestMapping.GetAllMappings, {}, oPostData, successFetchMappingListCallback, failureFetchMappingListCallback);
  };

  var _createDefaultMappingObject = function () {
    // var sId = UniqueIdentifierGenerator.generateUUID();

    var oNewMappingToCreate = {};
    oNewMappingToCreate.id = "";
    oNewMappingToCreate.label = UniqueIdentifierGenerator.generateUntitledName();
    oNewMappingToCreate.isDefault = false;
    oNewMappingToCreate.code = "";
    oNewMappingToCreate.isCreated = true;
    oNewMappingToCreate.mappingType = MappingTypeDictionary.INBOUND_MAPPING;

    // var oMasterMappingObject = SettingUtils.getAppData().getMappingList();
    // oMasterMappingObject[sId] = oNewMappingToCreate;

    _setSelectedMapping(oNewMappingToCreate);
    SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);
    _triggerChange();
  };

  var _createMapping = function () {
    var oNewMappingToCreate = _getSelectedMapping();
    oNewMappingToCreate = oNewMappingToCreate.clonedObject || oNewMappingToCreate;
    if(CS.isEmpty(oNewMappingToCreate.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    var oCodeToVerifyUniqueness = {
      id: oNewMappingToCreate.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_MAPPING
    };

    var oSuccessCreateCallback = {};
    oSuccessCreateCallback.isCreated = true;
    oSuccessCreateCallback.functionToExecute = function () {
      alertify.success(getTranslation().MAPPING_CREATED_SUCCESSFULLY);
    };

    var oCallbackData = {};
    oCallbackData.functionToExecute = _createMappingCall.bind(this, oNewMappingToCreate, oSuccessCreateCallback);
    var sURL = MappingRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackData);
  };

  var _createMappingCall = function (oNewMappingToCreate, oSuccessCreateCallback) {
    let sMappingRequestMappingType = MappingRequestMapping.CreateMapping;
    if(oNewMappingToCreate.mappingType == MappingTypeDictionary.OUTBOUND_MAPPING){
      sMappingRequestMappingType = MappingRequestMapping.CreateOutBoundMapping;
    }
    MappingProps.setSelectedId(TabHeaderData.class);
    let oPostRequest = [];
    let oData = {};
    oData.codes = [];
    oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTY_MAPPING;
    oData.names = [oNewMappingToCreate.label];
    oPostRequest.push(oData);
    oSuccessCreateCallback.serverCallback = {
      requestURL : sMappingRequestMappingType,
      mappingToSave : oNewMappingToCreate,
      isCreated : oSuccessCreateCallback.isCreated,
      functionToExecute : oSuccessCreateCallback.functionToExecute,
    }
    SettingUtils.csPostRequest(MappingRequestMapping.BulkCodeCheck, {}, oPostRequest, successBulkCodeCheckList.bind(this, oSuccessCreateCallback), failureBulkCodeCheckList);
  };


  var _cancelMappingCreation = function () {
    var oMasterMappingObject = SettingUtils.getAppData().getMappingList();
    var oNewMappingToCreate = _getSelectedMapping();

    delete oMasterMappingObject[oNewMappingToCreate.id];
    _setSelectedMapping({});
    _triggerChange();
  };

  var _getMappingDetails = function (sSelectedMappingId, oCallBackData) {
    let aMappingGrid = MappingProps.getMappingGridData();
    let oActiveMapping = CS.find(aMappingGrid, {id: sSelectedMappingId});
    MappingProps.setSearchViewText("");
    MappingProps.setAttributeMappings([]);
    MappingProps.setTagMappings([]);
    if (CS.isEmpty(oCallBackData)) {
      oCallBackData = {};
    }
    MappingProps.setSelectedId(TabHeaderData.class);

    let sMappingRequestMappingType = MappingRequestMapping.GetOutBoundMapping;
    if (oActiveMapping.mappingType === MappingTypeDictionary.INBOUND_MAPPING) {
      sMappingRequestMappingType = MappingRequestMapping.SaveInboundMapping;
    }
    let oPostData = {
      selectedPropertyCollectionId: null,
      tabId: MockDataForOutboundMapSummaryHeader.CLASS_MAP,
      id: sSelectedMappingId
    };
    SettingUtils.csPostRequest(sMappingRequestMappingType, {}, oPostData, successGetMappingCallback.bind(this, oCallBackData), failureGetMappingCallback);

  };

  let _createADMForMapping = function (oActiveMapping) {
    let oClonedMapping = oActiveMapping.clonedObject;
    let oMappingToSave = CS.cloneDeep(oClonedMapping);

    oMappingToSave.addedAttributeMappings = [];
    oMappingToSave.addedTagMappings = [];
    oMappingToSave.addedTaxonomyMappings = [];
    oMappingToSave.addedClassMappings = [];
    oMappingToSave.addedPropertyCollectionIds = [];
    oMappingToSave.addedRelationshipMappings = [];
    oMappingToSave.modifiedAttributeMappings = [];
    oMappingToSave.modifiedTagMappings = [];
    oMappingToSave.modifiedTaxonomyMappings = [];
    oMappingToSave.modifiedClassMappings = [];
    oMappingToSave.modifiedPropertyCollectionIds = [];
    oMappingToSave.modifiedRelationshipMappings = [];
    oMappingToSave.deletedAttributeMappings = [];
    oMappingToSave.deletedTagMappings = [];
    oMappingToSave.deletedTaxonomyMappings = [];
    oMappingToSave.deletedClassMappings = [];
    oMappingToSave.deletedPropertyCollectionIds = [];
    oMappingToSave.deletedRelationshipMappings = [];

    // For Process
    oMappingToSave.addedProcesses = CS.difference(oClonedMapping.processes, oActiveMapping.processes);
    oMappingToSave.deletedProcesses = CS.difference(oActiveMapping.processes, oClonedMapping.processes);
    delete oMappingToSave.processes;

    // For Taxonomy
    let aOldTaxonomyMappings = oActiveMapping.taxonomyMappings;
    let aNewTaxonomyMappings = oClonedMapping.taxonomyMappings;
    oMappingToSave.addedTaxonomyMappings = CS.differenceBy(aNewTaxonomyMappings, aOldTaxonomyMappings, 'id');
    oMappingToSave.deletedTaxonomyMappings = CS.map(CS.differenceBy(aOldTaxonomyMappings, aNewTaxonomyMappings, 'id'), 'id');
    oMappingToSave.modifiedTaxonomyMappings = CS.intersectionWith(aNewTaxonomyMappings, aOldTaxonomyMappings, function (oOld, oNew) {
      return (oOld.id === oNew.id) && !(oOld.isIgnored === oNew.isIgnored && CS.isEqual(oOld.columnNames, oNew.columnNames));
    });

    delete oMappingToSave.taxonomyMappings;

    // For Classes
    let aOldClassMappings = oActiveMapping.classMappings;
    let aNewClassMappings = oClonedMapping.classMappings;
    oMappingToSave.addedClassMappings = CS.differenceBy(aNewClassMappings, aOldClassMappings, 'id');
    oMappingToSave.deletedClassMappings = CS.map(CS.differenceBy(aOldClassMappings, aNewClassMappings, 'id'), 'id');
    oMappingToSave.modifiedClassMappings = CS.intersectionWith(aNewClassMappings, aOldClassMappings, function (oOld, oNew) {
      return (oOld.id === oNew.id) && !(oOld.isIgnored === oNew.isIgnored && CS.isEqual(oOld.columnNames, oNew.columnNames));
    });

    delete oMappingToSave.classMappings;

    // For Relationship
    let aOldRelationshipMappings = oActiveMapping.relationshipMappings;
    let aNewRelationshipMappings = oClonedMapping.relationshipMappings;
    oMappingToSave.addedRelationshipMappings = CS.differenceBy(aNewRelationshipMappings, aOldRelationshipMappings, 'id');
    oMappingToSave.deletedRelationshipMappings = CS.map(CS.differenceBy(aOldRelationshipMappings, aNewRelationshipMappings, 'id'), 'id');
    oMappingToSave.modifiedRelationshipMappings = CS.intersectionWith(aNewRelationshipMappings, aOldRelationshipMappings, function (oOld, oNew) {
      return (oOld.id === oNew.id) && !(oOld.isIgnored === oNew.isIgnored && CS.isEqual(oOld.columnNames, oNew.columnNames));
    });

    delete oMappingToSave.relationshipMappings;

    // For Property Collection
    oMappingToSave.tabId = MockDataForOutboundMapSummaryHeader[MappingProps.getSelectedId()];
    let aOldPropertyCollections = oActiveMapping.propertyCollectionIds || [];
    let aNewPropertyCollections = oClonedMapping.propertyCollectionIds || [];
    oMappingToSave.addedPropertyCollectionIds = aNewPropertyCollections.filter(x => !aOldPropertyCollections.includes(x));
    oMappingToSave.deletedPropertyCollectionIds = aOldPropertyCollections.filter(x => !aNewPropertyCollections.includes(x));
    oMappingToSave.modifiedPropertyCollectionIds = [];
    oMappingToSave.selectedPropertyCollectionId = oMappingToSave.selectedPropertyCollectionId;
    let aAttributeMappings = MappingProps.getAttributeMappings();
    let aTagMappings = MappingProps.getTagMappings();
    if (CS.isEmpty(oMappingToSave.deletedPropertyCollectionIds)) {

      CS.forEach(oMappingToSave.attributeMappings, function (oAttributeMapping) {
        let oFoundAttribute = CS.find(aAttributeMappings, {mappedElementId: oAttributeMapping.mappedElementId});
        if (CS.isNotEmpty(oFoundAttribute) && ((oFoundAttribute.columnNames[0] != oAttributeMapping.columnNames[0]) || (oFoundAttribute.isIgnored != oAttributeMapping.isIgnored))) {
          oMappingToSave.modifiedAttributeMappings.push(oAttributeMapping);
        } else if (CS.isEmpty(oFoundAttribute)) {
          oMappingToSave.addedAttributeMappings.push(oAttributeMapping);
        }
      });

      CS.forEach(oMappingToSave.tagMappings, function (oTagMapping) {
        let oFoundTag = CS.find(aTagMappings, {mappedElementId: oTagMapping.mappedElementId});
        if (CS.isNotEmpty(oFoundTag)) {
          if (oFoundTag.columnNames[0] != oTagMapping.columnNames[0] || oFoundTag.isIgnored != oTagMapping.isIgnored) {
            oMappingToSave.modifiedTagMappings.push(oTagMapping);
          } else {
            if (CS.isNotEmpty(oFoundTag.tagValueMappings)) {
              let bAlreadyPushed = false;
              CS.forEach(oFoundTag.tagValueMappings[0].mappings, function (oSubTag) {
                let oSubTagToCompare = CS.find(oTagMapping.tagValueMappings[0].mappings, {mappedTagValueId: oSubTag.mappedTagValueId});
                if (oSubTag.tagValue != oSubTagToCompare.tagValue) {
                  if (!bAlreadyPushed) {
                    oMappingToSave.modifiedTagMappings.push(oTagMapping);
                    bAlreadyPushed = true;
                  }
                }
              })
            }
          }

        } else if (CS.isEmpty(oFoundTag)) {
          oMappingToSave.addedTagMappings.push(oTagMapping);
        }
      });
    } else {
      oMappingToSave.selectedPropertyCollectionId = null;
    }


      delete oMappingToSave.propertyCollectionIds;
      delete oMappingToSave.propertyCollections;
      delete oMappingToSave.attributeMappings;
      delete oMappingToSave.tagMappings;

    return oMappingToSave;
  };

  var _isElementMappingIdsExists = function (aMappingList) {
    var bIsExists = true;
    CS.forEach(aMappingList, function (oMapping) {
        if(!oMapping.isIgnored) {
          if(CS.isEmpty(oMapping.columnNames[0]) || !oMapping.mappedElementId) {
              bIsExists = false;
              return bIsExists;
          }
        } else if(CS.isEmpty(oMapping.columnNames[0])) {
            bIsExists = false;
            return bIsExists;
        }
    });
    return bIsExists;
  };

  let _isElementMappingIdsExistsForOutboundMapping = function (aMappingList) {
    let bIsExists = true;
    CS.forEach(aMappingList, function (oMapping) {
      if (CS.isEmpty(oMapping.columnNames[0]) || !oMapping.mappedElementId) {
        bIsExists = false;
        return bIsExists;
      }
    });
    return bIsExists;
  };

  let _isTagValueMappedToSomeElement = function (aTagValueMappings) {
    let bIsTagValueMapped = false;
    CS.forEach(aTagValueMappings, function (oTagValueMapping) {
      let aMappings = oTagValueMapping.mappings;
      let bIsMappedTagValueIdExists = true;
      CS.forEach(aMappings, function (oMapping) {
        if(CS.isEmpty(oMapping.mappedTagValueId) || CS.isEmpty(oMapping.tagValue)) {
          bIsMappedTagValueIdExists = false;
        }
      });

      bIsTagValueMapped = !CS.isEmpty(oTagValueMapping.columnName) && bIsMappedTagValueIdExists;
    });

    return bIsTagValueMapped;
  };

  let _areAllTagValuesMapped = function (aTagMapping) {
    let bAreAllTagValuesMapped = true;
    CS.forEach(aTagMapping, function (oTagMapping) {
      let aTagValueMappings = oTagMapping.tagValueMappings;
      if (!CS.isEmpty(aTagValueMappings) && !_isTagValueMappedToSomeElement(aTagValueMappings)) {
        bAreAllTagValuesMapped = false;
        return bAreAllTagValuesMapped;
      }
    });
    return bAreAllTagValuesMapped;
  };

  var _isMappingValid = function (oActiveMapping) {
    var aAttributeMapping = oActiveMapping.attributeMappings;
    var aClassMapping = oActiveMapping.classMappings;
    var aTaxonomyMapping = oActiveMapping.taxonomyMappings;
    var aTagMapping = oActiveMapping.tagMappings;
    if (oActiveMapping.mappingType == MappingTypeDictionary.INBOUND_MAPPING) {
      return _isElementMappingIdsExists(aAttributeMapping) && _isElementMappingIdsExists(aClassMapping) &&
          _isElementMappingIdsExists(aTaxonomyMapping) && _isElementMappingIdsExists(aTagMapping) &&
          _areAllTagValuesMapped(aTagMapping);
    } else {
      return _isElementMappingIdsExistsForOutboundMapping(aClassMapping) && _isElementMappingIdsExistsForOutboundMapping(aTaxonomyMapping);
    }

  };

  var _saveMapping = function (oCallbackData) {
    var oActiveMapping = _getSelectedMapping();
    let oClonedMapping = oActiveMapping.clonedObject;
    if(!oClonedMapping.label) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    if(oClonedMapping.selectedPropertyCollectionId === "attributes" || oClonedMapping.selectedPropertyCollectionId === "tags"){
      oClonedMapping.selectedPropertyCollectionId = null;
    };

    if (oClonedMapping.mappingType == MappingTypeDictionary.OUTBOUND_MAPPING && MappingProps.getSelectedId() == TabHeaderData.propertyCollection) {
      if(!_isPropertyCollectionValid(oActiveMapping.clonedObject)){
        alertify.message(getTranslation().EXCEL_COLUMN_NAME_CAN_NOT_BE_EMPTY);
        return;
      }
    } else if (!_isMappingValid(oActiveMapping.clonedObject)) {
      alertify.message(getTranslation().SOME_PROPERTIES_ARE_UNMAPPED);
      return;
    }

    if (!oActiveMapping.isDirty) {
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }

    var oMappingToSave = _createADMForMapping(oActiveMapping);

    var oServerCallback = {};
    oServerCallback.functionToExecute = function () {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().MAPPING } ));
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    };

    let sRequestMappingType = MappingRequestMapping.SaveOutBoundMapping;
    if (oActiveMapping.mappingType == MappingTypeDictionary.INBOUND_MAPPING) {
      sRequestMappingType = MappingRequestMapping.SaveMapping;
    }
    oServerCallback.mappingToSave = oMappingToSave;
    oServerCallback.requestURL = sRequestMappingType;
    oCallbackData.serverCallback = oServerCallback;
    let aMappingGridData = MappingProps.getMappingGridData();
    let oPostRequest = [];
    let oData = {};
    oData.codes = [];
    oData.names = [];
    oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTY_MAPPING;
    let oMappingGridData = CS.find(aMappingGridData, {id: oMappingToSave.id});
    if (oMappingGridData.label !== oMappingToSave.label) {
      oData.names.push(oMappingToSave.label);
    }
    oPostRequest.push(oData);
    SettingUtils.csPostRequest(MappingRequestMapping.BulkCodeCheck, {}, oPostRequest, successBulkCodeCheckList.bind(this, oCallbackData), failureBulkCodeCheckList);
    _triggerChange();
  };

  var _deleteUnSavedMappings = function (aList) {
    var oMasterList = SettingUtils.getAppData().getMappingList();
    var aSavedMappingIds = [];
    var aDeletedMappingIds = [];

    CS.forEach(aList, function (uid) {
      if (oMasterList[uid].isCreated) {
        aDeletedMappingIds.push(uid);
        delete oMasterList[uid];
      } //else {
      aSavedMappingIds.push(uid);
      //}
    });

    var sSelectedMappingId = _getSelectedMapping().id;
    if (CS.indexOf(aDeletedMappingIds, sSelectedMappingId) >= 0) {
      _setSelectedMapping({});
    }

    return aSavedMappingIds;
  };

  var _deleteMappings = function (aBulkDeleteList, oCallBack) {
    var aFilteredMappingIds = _deleteUnSavedMappings(aBulkDeleteList);
    if (!CS.isEmpty(aFilteredMappingIds)) {
      return SettingUtils.csDeleteRequest(MappingRequestMapping.DeleteMapping, {}, {ids: aFilteredMappingIds}, successDeleteMappingsCallback, failureDeleteMappingsCallback.bind(this, oCallBack));
    } else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().USERS}));
      _triggerChange();
    }
  };

  var _getMappingRowsByType = function (oActiveMapping, sSummaryType) {
    var aRows = [];
    switch (sSummaryType) {
      case "taxonomies":
        if (!oActiveMapping.taxonomyMappings) {
          oActiveMapping.taxonomyMappings = [];
        }
        aRows = oActiveMapping.taxonomyMappings;

        break;
      case "classes":
        if (!oActiveMapping.classMappings) {
          oActiveMapping.classMappings = [];
        }
        aRows = oActiveMapping.classMappings;

        break;
      case "tags":
        if (!oActiveMapping.tagMappings) {
          oActiveMapping.tagMappings = [];
        }
        aRows = oActiveMapping.tagMappings;

        break;
      case "attributes":
        if (!oActiveMapping.attributeMappings) {
          oActiveMapping.attributeMappings = [];
        }
        aRows = oActiveMapping.attributeMappings;
        break;
      case "propertyCollections":
        if (!oActiveMapping.propertyCollections) {
          oActiveMapping.propertyCollections = [];
        }
        aRows = oActiveMapping.propertyCollections;
        break;

      case "relationship":
        if (!oActiveMapping.relationshipMappings) {
          oActiveMapping.relationshipMappings = [];
        }
        aRows = oActiveMapping.relationshipMappings;
        break;

      case "contexts":
        if (!oActiveMapping.contextMappings) {
          oActiveMapping.contextMappings = [];
        }
        aRows = oActiveMapping.contextMappings;
        break;

      default:
        aRows = oActiveMapping.classMappings;
        break;
    }
    return aRows;
  };

  /** PropertyCollection Properties Validation **/
  let _isPropertyCollectionValid = function (oActiveMapping) {
    let aAttributeMapping = oActiveMapping.attributeMappings;
    let aTagMapping = oActiveMapping.tagMappings;
    let bIsValid = true;
    CS.forEach(aAttributeMapping, function (oMapping) {
      if (CS.isEmpty(oMapping.columnNames[0])) {
        bIsValid = false;
        return bIsValid;
      }
    });
    CS.forEach(aTagMapping, function (oMapping) {
      if (CS.isEmpty(oMapping.columnNames[0])) {
        bIsValid = false;
        return bIsValid;
      }
      if(CS.isNotEmpty(oMapping.tagValueMappings)){
        CS.forEach(oMapping.tagValueMappings[0].mappings, function (oMappingChild) {
          if (CS.isEmpty(oMappingChild.tagValue)) {
            bIsValid = false;
            return bIsValid;
          }
        });
      }
    });
    return bIsValid;
  }

  var _handleMappingAddNewMappingRow = function (sSummaryType) {
    var oActiveMapping = _makeActiveMappingDirty();
    var aRows = _getMappingRowsByType(oActiveMapping, sSummaryType);
    aRows.push({
      id: UniqueIdentifierGenerator.generateUUID(),
      columnNames: [""],
      mappedElementId: null,
      isIgnored: false
    });
    _triggerChange();
  };

  var _handleColumnNameChanged = function (sId, sName, sSummaryType) {
    var oActiveMapping = _makeActiveMappingDirty();
    var aRows = _getMappingRowsByType(oActiveMapping, sSummaryType);
    var oElement = CS.find(aRows, {id: sId}) || {};
    if (!CS.isEmpty(oElement)) {
      var aTagValueMappings = oElement.tagValueMappings;
      var sColumnName = oElement.columnNames[0] || "";
      var oTagValue = CS.find(aTagValueMappings, {columnName: sColumnName});
      if (oTagValue) {
        oTagValue.columnName = sName;
      }
      oElement.columnNames = !CS.isEmpty(sName) ? [sName] : [""];
      _triggerChange();
    }
  };

  var _handleMappingConfigTagValueChanged = function (sId, sMappedTagValueId, sNewValue) {
    var oActiveMapping = _makeActiveMappingDirty();
    var oElement = CS.find(oActiveMapping.tagMappings, {id: sId});
    var sColumnName = oElement.columnNames[0];
    //oElement.id = UniqueIdentifierGenerator.generateUUID();
    var oTagValueMapping = CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    var aTagValueMappingValueList = (oTagValueMapping && oTagValueMapping.mappings) || [];
    if (!CS.isEmpty(aTagValueMappingValueList)) {
      var oTagValueMappingData = CS.find(aTagValueMappingValueList, {id: sMappedTagValueId});
      oTagValueMappingData.tagValue = sNewValue;
    }
    _triggerChange();
  };

  var _handleMappingConfigTagValueIgnoreCaseToggled = function (sId, sMappedTagValueId) {
    var oActiveMapping = _makeActiveMappingDirty();
    var oElement = CS.find(oActiveMapping.tagMappings, {id: sId});
    oElement.id = UniqueIdentifierGenerator.generateUUID();
    var sColumnName = oElement.columnNames[0];
    var oTagValueMapping = CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    var aTagValueMappingValueList = (oTagValueMapping && oTagValueMapping.mappings) || [];
    if (!CS.isEmpty(aTagValueMappingValueList)) {
      var oTagValueMappingData = CS.find(aTagValueMappingValueList, {id: sMappedTagValueId});
      oTagValueMappingData.isIgnoreCase = !oTagValueMappingData.isIgnoreCase;
    }
    _triggerChange();
  };

  let _handleMappingConfigMappedTagValueChanged = function (sId, sMappedTagValueId, sMappedElementId, oReferencedData) {
    let oActiveMapping = _makeActiveMappingDirty();
    let oElement = CS.find(oActiveMapping.tagMappings, {id: sId});

    if(!oElement.id) {
      oElement.id = UniqueIdentifierGenerator.generateUUID();
    }

    let sColumnName = oElement.columnNames[0];
    let oTagValueMapping = CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    let aTagValueMappingValueList = (oTagValueMapping && oTagValueMapping.mappings) || [];

    if (!CS.isEmpty(aTagValueMappingValueList)) {
      let oMappingObj = CS.find(aTagValueMappingValueList, {id: sMappedTagValueId});

      if (oMappingObj) {
        oMappingObj.mappedTagValueId = sMappedElementId[0];
      } else {
        aTagValueMappingValueList.push({
          id: UniqueIdentifierGenerator.generateUUID(),
          tagValue: UniqueIdentifierGenerator.generateUntitledName(),
          mappedTagValueId: sMappedElementId
        });
      }
    }

    try {
      let oConfigData = MappingProps.getConfigData();
      let oReferencedTag = oConfigData.referencedTags[sMappedElementId];
      CS.forEach(oReferencedData, function (oData) {
        oReferencedTag.children.push(oData);
      });
    } catch (oException) {
      ExceptionLogger.error(oException);
    }

    _triggerChange();
  };

  var _handleMappingConfigMappedTagValueRowDeleteClicked = function (sId, sMappedTagValueId) {
    var oActiveMapping = _makeActiveMappingDirty();
    var oElement = CS.find(oActiveMapping.tagMappings, {id: sId});
    var sColumnName = oElement.columnNames[0];
    var oTagValueMapping = CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    var aTagValueMappingValueList = (oTagValueMapping && oTagValueMapping.mappings) || [];
    if (!CS.isEmpty(aTagValueMappingValueList)) {
      CS.remove(aTagValueMappingValueList, {id: sMappedTagValueId});
    }
    _triggerChange();
  };

  var _handleAddTagValueClicked = function (sId, sMappedElementId) {
    var oActiveMapping = _makeActiveMappingDirty();
    var oElement = CS.find(oActiveMapping.tagMappings, {id: sId});
    if (!oElement.tagValueMappings) {
      oElement.tagValueMappings = [];
    }

    var sColumnName = oElement.columnNames[0];
    var oTagValueMapping = CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    if (oTagValueMapping) {
      if (CS.isEmpty(oTagValueMapping.mappings)) {
        oTagValueMapping.mappings = [];
      }
      oTagValueMapping.mappings.push({
        id: UniqueIdentifierGenerator.generateUUID(),
        tagValue: UniqueIdentifierGenerator.generateUntitledName(),
        mappedTagValueId: ""
      });
    } else {
      var oNewObject = {};
      oNewObject.columnName = sColumnName;
      oNewObject.mappings = [];
      oNewObject.mappings.push({
        id: UniqueIdentifierGenerator.generateUUID(),
        tagValue: UniqueIdentifierGenerator.generateUntitledName(),
        mappedTagValueId: ""
      });
      oElement.tagValueMappings.push(oNewObject);
    }

    _triggerChange();
  };

/*
  let _getRefDataFromIdLabelMap = (oEntities) => {
    let oReferencedData = {};

    CS.forEach(oEntities, function (sVal, sKey) {
      oReferencedData[sKey] = {
        id: sKey,
        label: sVal
      }
    });

    return oReferencedData;
  };
*/


  let _updateReferencedDataForMapping = (oConfigDetails) => {
    if (CS.isEmpty(oConfigDetails)) {
      return;
    }

    let oConfigData = MappingProps.getConfigData();
    oConfigData.referencedAttributes = oConfigDetails.attributes; //_getRefDataFromIdLabelMap(oConfigDetails.attributes);
    oConfigData.referencedTags = oConfigDetails.tags;
    oConfigData.referencedKlasses = oConfigDetails.klasses; //_getRefDataFromIdLabelMap(oConfigDetails.klasses);
    oConfigData.referencedTaxonomies = oConfigDetails.taxonomy; //_getRefDataFromIdLabelMap(oConfigDetails.taxonomy);
    oConfigData.referencedPropertyCollections = oConfigDetails.propertyCollections;
  };

  let _handleMappingMappedElementChanged = (sId, sMappedElementId, sSummaryType, oReferencedData) => {
    let oActiveMapping = _makeActiveMappingDirty();
    let aRows = _getMappingRowsByType(oActiveMapping, sSummaryType);
    let oRow = CS.find(aRows, {id: sId});
    oRow.mappedElementId = sMappedElementId[0];
    oRow.id = UniqueIdentifierGenerator.generateUUID();
    oRow.isAutomapped = false;


    CS.forEach(oRow.tagValueMappings, function (oTagValueMapping) {
      oTagValueMapping.mappings = [];
      /* var aTagValueMappingValueList = (oTagValueMapping && oTagValueMapping.mappings) || [];
       CS.forEach(aTagValueMappingValueList, function (oMappingObj) {
       oMappingObj.mappedTagValueId = "";
       oMappingObj.id = UniqueIdentifierGenerator.generateUUID();
       });*/
    });

    let oConfigData = MappingProps.getConfigData();
    switch (sSummaryType) {
      case "attributes":
        CS.assign(oConfigData.referencedAttributes, oReferencedData);
        break;

      case "tags":
        CS.assign(oConfigData.referencedTags, oReferencedData);
        break;

      case "taxonomies":
        CS.assign(oConfigData.referencedTaxonomies, oReferencedData);
        break;

      case "classes":
        CS.assign(oConfigData.referencedKlasses, oReferencedData);
        break;

      case "propertyCollections":
        CS.assign(oConfigData.referencedPropertyCollections, oReferencedData);
        break;

      case "relationship":
        CS.assign(oConfigData.referencedRelationships, oReferencedData);
        break;

      case "contexts":
        CS.assign(oConfigData.referencedContexts, oReferencedData);
        break;
    }

    /*var oRemovedElement = CS.remove(aRows, {id: sId})[0];
     aRows.push({
     id: UniqueIdentifierGenerator.generateUUID(),
     columnNames: oRemovedElement.columnNames,
     mappedElementId: sMappedElementId,
     isIgnored: oRemovedElement.isIgnored
     });*/

    _triggerChange();
  };

  var _handleMappingMappingRowSelected = function (sId) {
    var aSelectedMappingRows = MappingProps.getSelectedMappingRows();
    if (CS.includes(aSelectedMappingRows, sId)) {
      CS.remove(aSelectedMappingRows, function (sRowId) {
        return sRowId == sId;
      });
    } else {
      aSelectedMappingRows.push(sId);
    }
    _triggerChange();
  };

  var _handleMappingIsIgnoredToggled = function (sId, sSummaryType) {
    var oActiveMapping = _makeActiveMappingDirty();
    var aRows = _getMappingRowsByType(oActiveMapping, sSummaryType);
    var oElement = CS.find(aRows, {id: sId}) || {};
    oElement.isIgnored = !oElement.isIgnored;
    _triggerChange();
  };

  var _handleMappingMappingRowDeleted = function (sId, sSummaryType) {
    var oActiveMapping = _makeActiveMappingDirty();
    var aRows = _getMappingRowsByType(oActiveMapping, sSummaryType);
    CS.remove(aRows, {id: sId});
    _triggerChange();
  };

  var _handleTabLayoutTabChanged = function (sTabId) {
    var oActiveMapping = MappingProps.getSelectedMapping();
    if (oActiveMapping.clonedObject) {
      alertify.message(getTranslation().SAVE_OR_DISCARD_THE_CHANGES);
      return;
    }
    let oCallBackData = {};
    MappingProps.setSelectedId(sTabId);
    sTabId = MockDataForOutboundMapSummaryHeader[sTabId];
    let oPostData = {selectedPropertyCollectionId: null, tabId: sTabId, id: oActiveMapping.id};
    let sMappingRequestMappingType = MappingRequestMapping.SaveInboundMapping;
    if (oActiveMapping.mappingType === MappingTypeDictionary.OUTBOUND_MAPPING) {
      sMappingRequestMappingType = MappingRequestMapping.GetOutBoundMapping;
    }
    SettingUtils.csPostRequest(sMappingRequestMappingType, {}, oPostData, successGetMappingCallback.bind(this, oCallBackData), failureGetMappingCallback);


    _triggerChange();
  };

  let _closeMappingDialog = function () {
    MappingProps.setIsMappingDialogActive(false);
    _triggerChange();
  };

  let _handleSaveAsDialogCloseButtonClicked = function () {
    let oGridViewProps =GridViewStore.getGridViewPropsByContext(GridViewContexts.MAPPING);
    let oGridSkeleton = oGridViewProps.getGridViewSkeleton();
    oGridSkeleton.selectedContentIds = [];
    MappingProps.setIsSaveAsDialogActive(false);
    MappingProps.setCodeDuplicates([]);
    _triggerChange();
  };

  let _handleSaveAsDialogSaveButtonClicked = function () {
    let aGridViewData = MappingProps.getDataForSaveAsDialog();
    let aDataToSave = prepareDataForSaveAs(aGridViewData);
    if (CS.isEmpty(aDataToSave)) {
      alertify.error(getTranslation().NAME_IS_EMPTY);
    } else {
      let oCallbackData = {};
      let aListOfCodesToCheck = [];
      let aListOfNamesToCheck = [];
      let oPostData = [];
      oCallbackData.isSaveAsDialogActive = true;
      oCallbackData.isSaveAsCall = true;
      oCallbackData.functionToExecuteAfterCodeCheck = _saveAsMappingCall.bind(this, aDataToSave, oCallbackData);
      CS.forEach(aDataToSave, function (oElement) {
        let bValidEntityCode = SettingUtils.isValidEntityCode(oElement.code);
        if (bValidEntityCode) {
          aListOfCodesToCheck.push(oElement.code);
        }
        let bValidEntityName = CS.isNotEmpty(CS.trim(oElement.label)) ? true : false;
        if (bValidEntityName) {
          aListOfNamesToCheck.push(oElement.label);
        }
      });
      //TODO : REMOVE if PART IN CODE CLEANUP AS NO LONGER REQUIRED
      let aUniqueList = CS.uniq(aListOfCodesToCheck);
      if (aUniqueList.length !== aListOfCodesToCheck.length) {
        let aDuplicateCodes = _getDuplicateCodes(aListOfCodesToCheck);
        MappingProps.setCodeDuplicates(aDuplicateCodes);
        alertify.error(getTranslation().PLEASE_ENTER_UNIQUE_CODE);
      }else{
        let oData = {};
        oData.codes = aListOfCodesToCheck;
        oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTY_MAPPING;
        oData.names = aListOfNamesToCheck;
        oPostData.push(oData);
        if (CS.isNotEmpty(aListOfNamesToCheck) && CS.isNotEmpty(aListOfCodesToCheck)) {
          SettingUtils.csPostRequest(MappingRequestMapping.BulkCodeCheck, {}, oPostData, successBulkCodeCheckList.bind(this, oCallbackData), failureBulkCodeCheckList);
        } else {
          if (CS.isEmpty(aListOfNamesToCheck)) {
            MappingProps.setNameDuplicates(aListOfNamesToCheck);
            alertify.error(getTranslation().NAME_IS_EMPTY);
          }
          if (CS.isEmpty(aListOfCodesToCheck)) {
            MappingProps.setCodeDuplicates(aListOfCodesToCheck);
            alertify.error(getTranslation().PLEASE_ENTER_VALID_CODE);
          }
        }
      }
    }
    _triggerChange();
  };

  let prepareDataForSaveAs = function (aGridViewData) {
    let aDataToSave = [];
    let sLabelEmptyCheck = false;
    CS.forEach(aGridViewData, function (oData) {
      let oNewDataToSave = {};
      let oProperties = oData.properties;
      if (CS.isEmpty(oProperties["newLabel"].value)) {
        sLabelEmptyCheck = true;
      } else {
        oNewDataToSave.id = UniqueIdentifierGenerator.generateUUID();
        oNewDataToSave.label = oProperties["newLabel"].value;
        oNewDataToSave.code = oProperties["newCode"].value;
        oNewDataToSave.originalEntityId = oData.id;
        aDataToSave.push(oNewDataToSave);
      }

    });
    if(sLabelEmptyCheck){
      return [];
    }else {
      return aDataToSave
    }
  };

  let _saveAsMappingCall = function(aDataToSave,oCallbackData){
    SettingUtils.csPostRequest(MappingRequestMapping.SaveAs, {}, aDataToSave, successBulkSaveMappingList.bind(this, oCallbackData), failureSaveAs);
    MappingProps.setIsSaveAsDialogActive(false);
  };

  let _getDuplicateCodes = function(aListOfCodesToCheck){
    let aSortedArray = aListOfCodesToCheck.slice().sort();
    let results = [];
    for (let i = 0; i < aSortedArray.length - 1; i++) {
      if (aSortedArray[i + 1] == aSortedArray[i]) {
        results.push(aSortedArray[i]);
      }
    }
    return results;
  };

  let successBulkCodeCheckList = function (oCallBackData, oResponse) {
    let oSuccess = oResponse.success;
    let oCodePropertyMapping = oSuccess.codeCheck.Property_Mapping;
    let oNamePropertyMapping = oSuccess.nameCheck.Property_Mapping;
    let aCodeDuplication = [];
    let aDuplicateNames = [];
    CS.forEach(oCodePropertyMapping, function (bValue, skey) {
      if (!bValue) {
        aCodeDuplication.push(skey)
      }
    });

    CS.forEach(oNamePropertyMapping, function (bValue, skey) {
      if (!bValue) {
        aDuplicateNames.push(skey)
      }
    });
    MappingProps.setCodeDuplicates(aCodeDuplication);
    MappingProps.setNameDuplicates(aDuplicateNames);
    if (CS.isEmpty(aCodeDuplication) && CS.isEmpty(aDuplicateNames)) {
      oCallBackData && oCallBackData.functionToExecuteAfterCodeCheck && oCallBackData.functionToExecuteAfterCodeCheck();
      if (oCallBackData.serverCallback) {
        let oServerCallback = oCallBackData.serverCallback;
        if (oServerCallback.isBulkSave) {
          SettingUtils.csPostRequest(oServerCallback.requestURL, {}, oServerCallback.mappingToSave, successBulkSaveMappingList.bind(this, oServerCallback), failureBulkSaveMappingList);
        } else if (oServerCallback.isCreated) {
          SettingUtils.csPutRequest(oServerCallback.requestURL, {}, oServerCallback.mappingToSave, successGetMappingCallback.bind(this, oServerCallback), failureGetMappingCallback);
        } else {
          SettingUtils.csPostRequest(oServerCallback.requestURL, {}, oServerCallback.mappingToSave, successSaveMappingCallback.bind(this, oServerCallback), failureSaveMappingCallback.bind(this, oServerCallback));
        }
      }
    } else {
      if (CS.isNotEmpty(aCodeDuplication)) {
        alertify.error(getTranslation().MAPPING_CODE_ALREADY_EXISTS);
      }
      if (CS.isNotEmpty(aDuplicateNames)) {
        alertify.error(getTranslation().MAPPING_NAME_ALREADY_EXISTS);
      }
    }
    _triggerChange();
  };

  let failureSaveAs = function (oResponse) {
    alertify.error(getTranslation().ERROR_DURING_COPY_OPERATION);
  };

  let failureBulkCodeCheckList = function (oResponse) {
    SettingUtils.failureCallback(oResponse,"failureBulkCodeCheckList",getTranslation());
  };

  let _saveAsMapping = function () {
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.MAPPING);
    let oGridSkeleton = oGridViewProps.getGridViewSkeleton();
    let aSelectedContentIds = oGridSkeleton.selectedContentIds;
    let aProcessData = MappingProps.getMappingGridData();
    let aDataForSaveAsDialog = [];
    if (CS.isEmpty(aSelectedContentIds)) {
      alertify.error(getTranslation().PLEASE_SELECT_ATLEAST_1_ROW);
    } else if (aSelectedContentIds.length === 1) {
      CS.forEach(aSelectedContentIds, function (sSelectedId) {
        let oProcess = CS.find(aProcessData, {id: sSelectedId});
        if (!CS.isEmpty(oProcess)) {
          let oNewProcessDataForCopyDialog = {};
          oNewProcessDataForCopyDialog.id = oProcess.id;
          oNewProcessDataForCopyDialog.label = oProcess.label;
          oNewProcessDataForCopyDialog.code = oProcess.code;
          aDataForSaveAsDialog.push(oNewProcessDataForCopyDialog);
        }
      });
      let oProcessedSaveAsDataForGrid = _preProcessSaveAsDialogForGridView(aDataForSaveAsDialog);
      MappingProps.setDataForSaveAsDialog(oProcessedSaveAsDataForGrid);
      MappingProps.setIsSaveAsDialogActive(true);
    } else {
      alertify.error(getTranslation().SELECT_ONLY_ONE_ENTITY);
    }
    _triggerChange();
  };

  let _preProcessSaveAsDialogForGridView = function (aSaveAsList) {
    let oGridSkeleton = new SaveAsDialogGridViewSkeleton();
    let aGridViewData = [];

    CS.forEach(aSaveAsList, function (oProcess) {
      let oProcessedData = {};
      oProcessedData.id = oProcess.id;
      oProcessedData.isExpanded = false;
      oProcessedData.children = [];
      oProcessedData.actionItemsToShow = [];
      oProcessedData.properties = {};

      CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
        switch (oColumn.id) {

          case "label":
            if (oProcess.hasOwnProperty(oColumn.id)) {
              oProcessedData.properties[oColumn.id] = {
                value: oProcess[oColumn.id],
                placeholder: oProcess.code,
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          case "code":
            if (oProcess.hasOwnProperty(oColumn.id)) {
              oProcessedData.properties[oColumn.id] = {
                value: oProcess[oColumn.id],
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          case "newLabel":
            let sLabelValue = _preprocessLabelAndCode(oProcess["label"]);
            oProcessedData.properties[oColumn.id] = {
              value: sLabelValue,
              placeholder: oProcess.code,
              isDisabled: false,
              bIsMultiLine: false
            };

            break;

          case "newCode":
            let sCodeValue = _preprocessLabelAndCode(oProcess["code"]);
            oProcessedData.properties[oColumn.id] = {
              value: sCodeValue,
              isDisabled: false,
              bIsMultiLine: false
            };

            break;
        }
      });
      oProcessedData.properties.mappingType = MockDataForMappingTypes.INBOUND_MAPPING;
      aGridViewData.push(oProcessedData);
    });

    return aGridViewData;
  };


  let _preprocessLabelAndCode = function(sValue){
    let aSplitedValue = sValue.split("_");
    let bIsCopyIncluded = aSplitedValue[0].includes("Copy");
    let aCopyIncluded = bIsCopyIncluded ? aSplitedValue.splice(0,1) : aSplitedValue;
    let sContainsNumber = /\d/;
    let bIsNumberIncluded = sContainsNumber.test(aCopyIncluded[0]);
    let iCount = bIsNumberIncluded ? parseInt(aCopyIncluded[0].slice(4)) : parseInt(0);
    iCount = iCount+1 ;
    let sUpdatedValue = bIsCopyIncluded ? "Copy"+ iCount +"_" + aSplitedValue.join("_") : "Copy"+ "_" + aSplitedValue.join("_");

    return sUpdatedValue;
  };

  let _handleSaveAsDialogValueChanged = function (sContentId, sPropertyId, sValue) {
    let aGridViewData = MappingProps.getDataForSaveAsDialog();
    let oGridRow = CS.find(aGridViewData, {id: sContentId});
    let oProperty = oGridRow.properties[sPropertyId];
    if (!CS.isEmpty(oProperty) && !CS.isEqual(oProperty.value, sValue)) {
      oProperty.value = sValue;
    }
  };

  /** Deleting PropertyGroup **/
  let _handlePropertyGroupDeleteButtonClicked = function (sSelectedElement, sContext) {
    let oActiveMapping = _getSelectedMapping();
    let bIsDirty = (typeof oActiveMapping.isDirty=="undefined")?false:oActiveMapping.isDirty;
    if(!bIsDirty) {
      oActiveMapping = _makeActiveMappingDirty();
      let iIndex = 0;
      let aPropertyCollectionForMapping = oActiveMapping.propertyCollectionIds;
      iIndex = aPropertyCollectionForMapping.indexOf(sSelectedElement);
      aPropertyCollectionForMapping.splice(iIndex, 1);
      oActiveMapping.propertyCollectionIds = aPropertyCollectionForMapping;
      _saveMapping({});
    }else{
      alertify.message(getTranslation().SAVE_OR_DISCARD_THE_CHANGES);
    }
    _triggerChange()
  };

  /** Adding PropertyGroup **/
  let _handlePropertyGroupApplyButtonClicked = function (sContext, oSelectedItems, oReferencedData) {
    if (_getSelectedMapping().isDirty) {
      alertify.message(getTranslation().SAVE_OR_DISCARD_THE_CHANGES);
    } else {
      let oActiveMapping = _makeActiveMappingDirty();
      let sSelectedItemId = oSelectedItems.id;
      oActiveMapping.configDetails.propertyCollections[sSelectedItemId] = oSelectedItems;
      if (oActiveMapping.propertyCollectionIds == null) {
        oActiveMapping.propertyCollections = [];
        oActiveMapping.propertyCollectionIds = [];
      }
      oActiveMapping.propertyCollectionIds.push(oSelectedItems.id);
      oActiveMapping.selectedPropertyCollectionId = oSelectedItems.id;
      MappingProps.setSearchViewText("");
      let oPostData = {
        "propertyCollectionId": oSelectedItems.id,
        "mappingId": oActiveMapping.id
      };
      SettingUtils.csPostRequest(MappingRequestMapping.GetAttributeTagInfoForKlass, {}, oPostData, oSuccessPropertyCollection, failurePropertyCollection);
    }
    _triggerChange();
  };

  /** ExportAll Checkbox handling **/
  let _handlePropertyGroupCheckboxClicked = function (bIsCheckboxClicked, sContext) {
    let oActiveMapping = _makeActiveMappingDirty();
    switch (sContext) {
      case "classes" :
        oActiveMapping.isAllClassesSelected = !bIsCheckboxClicked;
        break;
      case "taxonomies" :
        oActiveMapping.isAllTaxonomiesSelected = !bIsCheckboxClicked;
        break;
      case "propertyCollections" :
        oActiveMapping.isAllPropertyCollectionSelected = !bIsCheckboxClicked;
        break;
      case "relationship" :
        oActiveMapping.isAllRelationshipsSelected = !bIsCheckboxClicked;
        break;
      case "contexts" :
        oActiveMapping.isAllContextsSelected = !bIsCheckboxClicked;
        break;
    }
    _triggerChange();
  };

  /** Checkbox handling for Attributes & Tags **/
  let _handlePropertyGroupCheckboxClickedForAttributeAndTag = function (bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag) {
    let oActiveMapping = _makeActiveMappingDirty();
    bIsCheckboxClicked = !bIsCheckboxClicked;
    switch (sContext) {
      case "attribute":
        let aSelectedAttributesForPropertyGroup = oActiveMapping.attributeMappings;
        let oAttributeClicked = CS.find(aSelectedAttributesForPropertyGroup, {id: sSelectedElement});
        oAttributeClicked.isIgnored = bIsCheckboxClicked;
        break;
      case "tag":
        let aSelectedTagsForPropertyGroup = oActiveMapping.tagMappings;
        let oTagClicked = CS.find(aSelectedTagsForPropertyGroup, {id: sSelectedElement});
        oTagClicked.isIgnored = bIsCheckboxClicked;
        break;
      case "subTag":
        let aSelectedTagsForPropertyGroups = oActiveMapping.tagMappings;
        let oSubTagClicked;
        CS.forEach(aSelectedTagsForPropertyGroups, function (oSelectedTag) {
          if (CS.isNotEmpty(CS.find(oSelectedTag.tagValueMappings[0].mappings, {id: sSelectedElement}))) {
            oSubTagClicked = CS.find(oSelectedTag.tagValueMappings[0].mappings, {id: sSelectedElement});
          }
        });
        oSubTagClicked.isIgnored = bIsCheckboxClicked;
        break;
    }
    _triggerChange();
  };

  /** Switching of Property Collections **/
  let _handlePropertyGroupPropertyCollectionListItemClicked = function (sSelectedElement, sContext) {
    let oActiveMapping = MappingProps.getSelectedMapping();
    let bIsPropertyCollectionToggleFlag = MappingProps.getPropertyCollectionToggleFlag();
    if(bIsPropertyCollectionToggleFlag){
      if (oActiveMapping.clonedObject) {
        alertify.message(getTranslation().SAVE_OR_DISCARD_THE_CHANGES);
      } else {
        let oPostData;
        if (sContext == "contexts") {
          oActiveMapping.selectedContextId = sSelectedElement;
          oPostData = {
            selectedContextId: sSelectedElement,
            tabId: MockDataForOutboundMapSummaryHeader.CONTEXT_TAG_MAP,
            id: oActiveMapping.id,
            selectedPropertyCollectionId: null
          };
        } else {
          oActiveMapping.selectedPropertyCollectionId = sSelectedElement;
          oPostData = {
            selectedPropertyCollectionId: sSelectedElement,
            tabId: MockDataForOutboundMapSummaryHeader.PROPERTY_COLLECTION_MAP,
            id: oActiveMapping.id
          };
        }
        let sMappingRequestMappingType = MappingRequestMapping.GetOutBoundMapping;
        let oCallBackData = {};
        SettingUtils.csPostRequest(sMappingRequestMappingType, {}, oPostData, successGetMappingCallback.bind(this, oCallBackData), failureGetMappingCallback);
      }
    }else if(sSelectedElement === "attributes" ){
      MappingProps.setAttributeMappings(oActiveMapping.attributeMappings);
      oActiveMapping.selectedPropertyCollectionId = sSelectedElement;
      _setSelectedMapping(oActiveMapping);
      _updateReferencedDataForMapping(oActiveMapping.configDetails);
    }else if(sSelectedElement === "tags" ){
      MappingProps.setTagMappings(oActiveMapping.tagMappings);
      oActiveMapping.selectedPropertyCollectionId = sSelectedElement;
      _setSelectedMapping(oActiveMapping);
      _updateReferencedDataForMapping(oActiveMapping.configDetails);
    }
    _triggerChange()
  };

  /** Property Collection Search handling **/
  let _handlePropertyGroupSearchViewChanged = function (sSearchValue) {
    MappingProps.setSearchViewText(sSearchValue);
    _triggerChange();
  };

  /** Column Name Changed handing for Attributes & Tags **/
  let _handlePropertyGroupColumnNameChanged = function (sId, sName, sSummaryType) {
    var oActiveMapping = _makeActiveMappingDirty();
    var aRows = (sSummaryType == "attribute") ? oActiveMapping.attributeMappings : oActiveMapping.tagMappings;

    if (sSummaryType == "attribute" || sSummaryType == "tag") {
      let oElement = CS.find(aRows, {id: sId}) || {};
      if (!CS.isEmpty(oElement)) {
        oElement.columnNames[0] = sName;
        if (sSummaryType == "tag" && CS.isNotEmpty(oElement.tagValueMappings)) {
          oElement.tagValueMappings[0].columnName = sName;
        }
      }
    } else {
      let oSubTagClicked;
      CS.forEach(aRows, function (oSelectedTag) {
        if (CS.isNotEmpty(oSelectedTag.tagValueMappings)) {
          if (CS.isNotEmpty(CS.find(oSelectedTag.tagValueMappings[0].mappings, {id: sId}))) {
            oSubTagClicked = CS.find(oSelectedTag.tagValueMappings[0].mappings, {id: sId});
          }
        }
      })
      oSubTagClicked.tagValue = sName;
    }

    _triggerChange();

  };

  let oSuccessPropertyCollection = function (oResponse) {
    let oActiveMapping = _makeActiveMappingDirty();
    let aAttributeList = oResponse.success.attributeList;
    let aTagList = oResponse.success.tagList;
    let oConfigDetails = {};
    /** Temporary Fix , Don't use CloneDeep **/
    let aAttributeMappingsForProps = CS.cloneDeep(oResponse.success.attributeMappings);
    let aTagMappingsForProps =  CS.cloneDeep(oResponse.success.tagMappings);
    MappingProps.setAttributeMappings(aAttributeMappingsForProps);
    MappingProps.setTagMappings(aTagMappingsForProps);
    oConfigDetails.attributes = oResponse.success.configDetails.attributes;
    oConfigDetails.tags =  oResponse.success.configDetails.tags;
    let aAttributeMappings = oResponse.success.attributeMappings;
    let aTagMappings =  oResponse.success.tagMappings;

    let aAttributeReq = [];
    let aTagReq = [];
    oActiveMapping.configRuleIdsForAttribute = [];
    oActiveMapping.configRuleIdsForTag = [];
    CS.forEach(aAttributeMappings,function (oAttribute) {
      oActiveMapping.configRuleIdsForAttribute.push(oAttribute.id);
    });

    CS.forEach(aTagMappings,function (oTag) {
      oActiveMapping.configRuleIdsForTag.push(oTag.id);
    });

    CS.forEach(aAttributeList, function (oAttribute) {
      let oFoundAttributeMapping = CS.find(aAttributeMappings, {mappedElementId : oAttribute.id});
      if(CS.isNotEmpty(oFoundAttributeMapping)){
        aAttributeReq.push(oFoundAttributeMapping);
      }else {
        aAttributeReq.push({
          id: UniqueIdentifierGenerator.generateUUID(),
          columnNames: [oAttribute.code],
          mappedElementId: oAttribute.id,
          isIgnored: false
        });
      }
      oActiveMapping.configDetails.attributes[oAttribute.id] = oAttribute;
    });

    oActiveMapping.attributeMappings = aAttributeReq;

    /** TODO : Recursion New Logic to implement **/
    let count = -1;
    CS.forEach(aTagList, function (oTag) {
      let oFoundTagMapping = CS.find(aTagMappings, {mappedElementId : oTag.id});

      if (CS.isNotEmpty(oFoundTagMapping)) {
        aTagReq.push(oFoundTagMapping);
        oActiveMapping.configDetails.tags[oTag.id] = oConfigDetails.tags[oTag.id];
        count++;
      }
      else{
        oActiveMapping.configDetails.tags[oTag.id] = {};
        oActiveMapping.configDetails.tags[oTag.id].children = [];
        oActiveMapping.configDetails.tags[oTag.id].label = oTag.label;
        let aChildTags = oTag.childTag;
        aTagReq.push({
          id: UniqueIdentifierGenerator.generateUUID(),
          columnNames: [oTag.code],
          mappedElementId: oTag.id,
          isIgnored: false,
          tagValueMappings: [
            {
              columnName: oTag.code,
              mappings: []
            }
          ]
        });

        count++;
        let iCountForSubTag = 0;
        CS.forEach(aChildTags, function (oChildTag) {
          oActiveMapping.configDetails.tags[oTag.id].children[iCountForSubTag]={};
          oActiveMapping.configDetails.tags[oTag.id].children[iCountForSubTag].id= oChildTag.id;
          oActiveMapping.configDetails.tags[oTag.id].children[iCountForSubTag].code= oChildTag.code;
          oActiveMapping.configDetails.tags[oTag.id].children[iCountForSubTag].label = oChildTag.label;
          iCountForSubTag++;
          aTagReq[count].tagValueMappings[0].mappings.push({
            id: UniqueIdentifierGenerator.generateUUID(),
            tagValue: oChildTag.code,
            mappedTagValueId: oChildTag.id,
            isIgnored: false
          })
        });
      }

    });
    oActiveMapping.tagMappings = aTagReq;
    _triggerChange();
  };

  let failurePropertyCollection = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failurePropertyCollection", getTranslation());
  };
  
  let _handlePropertyCollectionToggleButtonClicked = function (sContext, bIsPropertyCollectionToggleButtonClicked) {
    MappingProps.setPropertyCollectionToggleFlag(bIsPropertyCollectionToggleButtonClicked);
    if(!bIsPropertyCollectionToggleButtonClicked){
      _handleTabLayoutTabChanged(TabHeaderData.propertyCollection);
    }
    _triggerChange();
  };


  return {

    fetchMappingList: function () {
      _fetchMappingList();
    },

    createMappingDialogButtonClicked: function () {
      _createDefaultMappingObject();
    },

    createMapping: function () {
      _createMapping();
    },

    cancelMappingCreation: function () {
      _cancelMappingCreation();
    },

    discardUnsavedMapping: function (oCallbackData) {
      var oSelectedMapping = _getSelectedMapping();
      var oMasterMappingList = SettingUtils.getAppData().getMappingList();

      if (!CS.isEmpty(oSelectedMapping)) {
        if (oSelectedMapping.isCreated) {

          delete oMasterMappingList[oSelectedMapping.id];
          _setSelectedMapping({});
          alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        }
        else if (oSelectedMapping.isDirty) {
          MappingProps.setAttributeMappings(oSelectedMapping.attributeMappings);
          MappingProps.setTagMappings(oSelectedMapping.tagMappings);
          delete oSelectedMapping.clonedObject;
          delete oSelectedMapping.isDirty;
          alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        }
        if (oCallbackData && oCallbackData.functionToExecute) {
          oCallbackData.functionToExecute();
        }
        _triggerChange();

      } else {
        alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      }
    },

    saveMapping: function (oCallbackData) {
      _saveMapping(oCallbackData);
    },

    deleteMappings: function (aSelectedIds, oCallBack) {
      let aMappingIdsToDelete = aSelectedIds;
      var oMasterMappingList = SettingUtils.getAppData().getMappingList();
      CS.remove(aMappingIdsToDelete, function (sMappingId) {
        var oMasterMapping = oMasterMappingList[sMappingId];
        if (sMappingId === "admin" || oMasterMapping.isDefault) {
          return true;
        }
      });
      if (!CS.isEmpty(aMappingIdsToDelete)) {

        var aBulkDeleteMappings = [];
        CS.forEach(aMappingIdsToDelete, function (sMappingId) {
          let sSelectedMappingLabel = CS.getLabelOrCode(oMasterMappingList[sMappingId]);
          aBulkDeleteMappings.push(sSelectedMappingLabel);
        });

        //var sBulkDeleteMappingLastNames = aBulkDeleteMappings.join(', ');
        CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteMappings,
            function () {
              _deleteMappings(aMappingIdsToDelete, oCallBack)
              .then(_fetchMappingList);
            }, function (oEvent) {
            }, true);
      } else {
        alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      }
    },

/*
    getMappingDetails: function (_sSelectedMappingId) {
      var sSelectedMappingId = _sSelectedMappingId || _getSelectedMapping().id;
      _getMappingDetails(sSelectedMappingId);
    },
*/

    handleMappingSummaryViewAddNewMappingRow: function (sSummaryType) {
      _handleMappingAddNewMappingRow(sSummaryType);
    },

    handleMappingSummaryViewColumnNameChanged: function (sId, sName, sSummaryType) {
      _handleColumnNameChanged(sId, sName, sSummaryType);
    },

    handleAddTagValueClicked: function (sId, sMappedElementId) {
      _handleAddTagValueClicked(sId, sMappedElementId);
    },

    handleMappingSummaryViewConfigTagValueChanged: function (sId, sMappedTagValueId, sNewValue) {
      _handleMappingConfigTagValueChanged(sId, sMappedTagValueId, sNewValue);
    },

    handleMappingSummaryViewConfigTagValueIgnoreCaseToggled: function (sId, sMappedTagValueId) {
      _handleMappingConfigTagValueIgnoreCaseToggled(sId, sMappedTagValueId);
    },

    handleMappingSummaryViewConfigMappedTagValueChanged: function (sId, sMappedTagValueId, sMappedElementId, oReferencedData) {
      _handleMappingConfigMappedTagValueChanged(sId, sMappedTagValueId, sMappedElementId, oReferencedData);
    },

    handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked: function (sId, sMappedTagValueId) {
      _handleMappingConfigMappedTagValueRowDeleteClicked(sId, sMappedTagValueId);
    },

    handleMappingSummaryViewMappedElementChanged: function (sId, sMappedElementId, sSummaryType, oReferencedData) {
      _handleMappingMappedElementChanged(sId, sMappedElementId, sSummaryType, oReferencedData);
    },

    handleMappingSummaryViewMappingRowSelected: function (sId) {
      _handleMappingMappingRowSelected(sId);
    },

    handleMappingSummaryViewIsIgnoredToggled: function (sId, sSummaryType) {
      _handleMappingIsIgnoredToggled(sId, sSummaryType);
    },

    handleMappingSummaryViewMappingRowDeleted: function (sId, sSummaryType) {
      _handleMappingMappingRowDeleted(sId, sSummaryType);
    },

    handleTabLayoutTabChanged: function (sTabId) {
      _handleTabLayoutTabChanged(sTabId);
    },

    handleMappingConfigFieldValueChanged: function (sKey, aSelectedItems) {
      let oActiveMapping = _makeActiveMappingDirty();
      let sValue;
      if (Array.isArray(aSelectedItems)) {
        sValue = aSelectedItems[0];
      } else {
        sValue = aSelectedItems;
      }
      MappingProps.setSelectedId("TAXONOMY_MAP");

      oActiveMapping[sKey] = sValue;
      _triggerChange()
    },

    handleMappingDialogButtonClicked: function (sButtonId) {
      if(sButtonId === "create") {
        MappingStore.createMapping();
      } else {
        MappingStore.cancelMappingCreation();
      }
    },

    handleMappingConfigDialogButtonClicked: function (sButtonId) {
      if (sButtonId == "save") {
        _saveMapping({});
      } else if (sButtonId == "cancel") {
        this.discardUnsavedMapping({});
      } else {
        _closeMappingDialog();
      }
    },

    setUpMappingConfigGridView: function () {
      _setUpMappingConfigGridView();
    },

    editButtonClicked: function (sEntityId) {
      MappingProps.setIsMappingDialogActive(true);
      _getMappingDetails(sEntityId);
    },

    processProcessMappingListAndSave: function(oCallbackData){
      _processProcessMappingListAndSave(oCallbackData);
    },

    discardMappingListChanges: function (oCallbackData) {
      _discardMappingListChanges(oCallbackData);

    },

    handleSaveAsDialogCloseButtonClicked: function () {
      _handleSaveAsDialogCloseButtonClicked()
    },

    handleSaveAsDialogSaveButtonClicked: function () {
      _handleSaveAsDialogSaveButtonClicked()
    },

    saveAsMapping: function () {
      _saveAsMapping();
    },

    handleSaveAsDialogValueChanged: function (sContentId, sPropertyId, sValue) {
      _handleSaveAsDialogValueChanged(sContentId, sPropertyId, sValue);
    },

    handlePropertyGroupCheckboxClicked: function (bIsCheckboxClicked, sContext) {
      _handlePropertyGroupCheckboxClicked(bIsCheckboxClicked, sContext);
    },

    handlePropertyGroupApplyButtonClicked: function (sContext, oSelectedItems, oReferencedData) {
      _handlePropertyGroupApplyButtonClicked(sContext, oSelectedItems, oReferencedData);
    },

    handlePropertyGroupDeleteButtonClicked: function (sSelectedElement, sContext) {
      _handlePropertyGroupDeleteButtonClicked(sSelectedElement, sContext);
    },

    handlePropertyGroupCheckboxClickedForAttributeAndTag: function (bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag) {
      _handlePropertyGroupCheckboxClickedForAttributeAndTag(bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag);
    },

    handlePropertyGroupPropertyCollectionListItemClicked: function (sSelectedElement, sContext) {
      _handlePropertyGroupPropertyCollectionListItemClicked(sSelectedElement, sContext);
    },

    handlePropertyGroupColumnNameChanged: function (sId, sName, sSummaryType) {
      _handlePropertyGroupColumnNameChanged(sId, sName, sSummaryType);
    },

    handlePropertyGroupSearchViewChanged: function (sSearchValue) {
      _handlePropertyGroupSearchViewChanged(sSearchValue);
    },

    handlePropertyCollectionToggleButtonClicked: function (sContext, bIsPropertyCollectionToggleButtonClicked) {
      _handlePropertyCollectionToggleButtonClicked(sContext, bIsPropertyCollectionToggleButtonClicked);
    },

    reset: function () {
      SettingUtils.getAppData().setMappingList([]);
      MappingProps.reset();
    }

  }
})();

MicroEvent.mixin(MappingStore);
export default MappingStore;
