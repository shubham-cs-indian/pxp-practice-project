
import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import { ProfileRequestMapping } from '../../tack/setting-screen-request-mapping';
import ProfileProps from './../model/profile-config-view-props';
import SettingUtils from './../helper/setting-utils';
import EndpointDictionary from '../../../../../../commonmodule/tack/endpoint-type-dictionary';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import EndpointConfigGridViewSkeleton from '../../tack/endpoint-config-grid-vew-skeleton';
import GridViewContexts from '../../../../../../commonmodule/tack/grid-view-contexts';
import oPhysicalCatalogDictionary from '../../../../../../commonmodule/tack/physical-catalog-dictionary';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import { SettingsRequestMapping as oSettingScreenRequestMapping } from '../../tack/setting-screen-request-mapping';
import SaveAsDialogGridViewSkeletonForEndpoints from "../../tack/save-as-dialog-grid-view-skeleton-for-endpoints";
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import SaveAsDialogGridViewSkeletonForEndpointsWorkflow from "../../tack/save-as-dialog-grid-view-skeleton-for-endpoints-workflow";

var ProfileStore = (function () {

  var _triggerChange = function () {
    ProfileStore.trigger('profile-changed');
  };

  var _getSelectedProfile = function () {
    return ProfileProps.getSelectedProfile();
  };

  var _makeActiveProfileDirty = function () {
    var oActiveProfile = ProfileProps.getSelectedProfile();
    if (!oActiveProfile.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveProfile);
    }
    ProfileProps.setProfileScreenLockStatus(true);
    return oActiveProfile.clonedObject;
  };

  var successFetchProfileListCallback = function (oCallback, oResponse) {
    SettingUtils.getAppData().setProfileList(oResponse.success);

    if(oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var failureFetchProfileListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchProfileListCallback", getTranslation);
  };

  var failureDeleteProfilesCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var oMasterProfileList = SettingUtils.getAppData().getProfileList();
      var oSelectedProfile = _getSelectedProfile();
      handleDeleteProfileFailure(oResponse.failure.exceptionDetails, oMasterProfileList, oSelectedProfile);
    } else {
      SettingUtils.failureCallback(oResponse, "failureDeleteProfilesCallback", getTranslation);
    }
    _triggerChange();
  };

  var failureGetProfileCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGetProfileCallback", getTranslation);
  };

  var handleDeleteProfileFailure = function (List, oMasterProfileList, oSelectedProfile) {
    var aAlreadyDeletedProfiles = [];
    var aUnhandledProfiles = [];
    CS.forEach(List, function (oItem) {
      if (oItem.key == "ProfileNotFoundException") {
        aAlreadyDeletedProfiles.push(oMasterProfileList[oItem.itemId].firstName);


        if (oSelectedProfile.id && oSelectedProfile.id == oItem.itemId) {
          _setSelectedProfile({});
        }

        delete oMasterProfileList[oItem.itemId];
      } else {
        aUnhandledProfiles.push(oMasterProfileList[oItem.itemId].firstName);
      }
    });

    if (aAlreadyDeletedProfiles.length > 0) {
      var sProfileAlreadyDeleted = aAlreadyDeletedProfiles.join(',');
      alertify.error(Exception.getCustomMessage("Profile_already_deleted",getTranslation(), sProfileAlreadyDeleted), 0);
    }
    if (aUnhandledProfiles.length > 0) {
      var sUnhandledProfile = aUnhandledProfiles.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_Profile",getTranslation(), sUnhandledProfile), 0);
    }
  };

  var _setSelectedProfile = function (oProfile) {
    ProfileProps.setSelectedProfile(oProfile);
  };

  var _fetchProfileList = function (oCallback) {
    SettingUtils.csGetRequest(ProfileRequestMapping.GetAllProfiles, {}, successFetchProfileListCallback.bind(this, oCallback), failureFetchProfileListCallback);
  };

  var _createDummyProfile = function () {
    let oNewProfileToCreate = {};
    oNewProfileToCreate.id = UniqueIdentifierGenerator.generateUUID();
    oNewProfileToCreate.label = UniqueIdentifierGenerator.generateUntitledName();
    oNewProfileToCreate.isDefault = false;
    oNewProfileToCreate.workSpace = ProfileProps.getEndpointIndexList()[0].id;
    oNewProfileToCreate.endpointType = EndpointDictionary.INBOUND_ENDPOINT;
    oNewProfileToCreate.systemId = SettingUtils.getTreeRootId().toString();
    oNewProfileToCreate.code = "";
    oNewProfileToCreate.isCreated = true;
    if (SettingUtils.isOffboarding()) {
      oNewProfileToCreate.endpointType = EndpointDictionary.OUTBOUND_ENDPOINT;
    }
    return oNewProfileToCreate;
  };

  var _cancelProfileCreation = function () {
    var oMasterProfileObject = SettingUtils.getAppData().getProfileList();
    var oProfile = _getSelectedProfile();

    delete oMasterProfileObject[oProfile.id];
    _setSelectedProfile({});
    _triggerChange();
  };

  let _createProfileCall = function (oNewProfileToCreate, oCallback) {
    oNewProfileToCreate.physicalCatalogs = [oPhysicalCatalogDictionary.DATAINTEGRATION];
    let oPostRequest = [];
    let oData = {};
    oData.codes = [oNewProfileToCreate.code];
    oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_ENDPOINT;
    oData.names = [oNewProfileToCreate.label];
    oPostRequest.push(oData);

    oCallback.serverCallback = {
      requestURL: ProfileRequestMapping.CreateProfile,
      endpointToCreate: oNewProfileToCreate,
      isCreated: true,
    };
    SettingUtils.csPostRequest(ProfileRequestMapping.BulkCodeCheck, {}, oPostRequest, successBulkCodeCheckList.bind(this, oCallback), failureBulkCodeCheckList);
  };

  var _getMappingRowsByType = function (oActiveProfile, sSummaryType) {
    var aRows = [];
    switch (sSummaryType) {
      case "taxonomies":
        if (!oActiveProfile.taxonomyMappings) {
          oActiveProfile.taxonomyMappings = [];
        }
        aRows = oActiveProfile.taxonomyMappings;

        break;
      case "classes":
        if (!oActiveProfile.classMappings) {
          oActiveProfile.classMappings = [];
        }
        aRows = oActiveProfile.classMappings;
        break;
      case "tags":
        if (!oActiveProfile.tagMappings) {
          oActiveProfile.tagMappings = [];
        }
        aRows = oActiveProfile.tagMappings;

        break;
      case "attributes":
        if (!oActiveProfile.attributeMappings) {
          oActiveProfile.attributeMappings = [];
        }
        aRows = oActiveProfile.attributeMappings;

        break;

      default:
        break;
    }
    return aRows;
  };

  var _handleProfileAddNewMappingRow = function (sSummaryType) {
    var oActiveProfile = _makeActiveProfileDirty();
    var aRows = _getMappingRowsByType(oActiveProfile, sSummaryType);
    aRows.push({
      id: UniqueIdentifierGenerator.generateUUID(),
      columnNames: [""],
      mappedElementId: "",
      isIgnored: false
    });
    _triggerChange();
  };

  var _handleColumnNameChanged = function (sId, sName, sSummaryType) {
    var oActiveProfile = _makeActiveProfileDirty();
    var aRows = _getMappingRowsByType(oActiveProfile, sSummaryType);
    var oElement = CS.find(aRows, {id: sId}) || {};
    if(!CS.isEmpty(oElement)) {
      var aTagValueMappings = oElement.tagValueMappings;
      var sColumnName = oElement.columnNames[0];
      var oTagValue = CS.find(aTagValueMappings, {columnName: sColumnName});
      if(oTagValue) {
        oTagValue.columnName = sName;
      }
      oElement.columnNames = [sName];
      _triggerChange();
    }
  };

  var _handleProfileConfigTagValueChanged = function (sId, sMappedTagValueId, sNewValue) {
    var oActiveProfile = _makeActiveProfileDirty();
    var oElement = CS.find(oActiveProfile.tagMappings, {id: sId});
    var sColumnName = oElement.columnNames[0];
    oElement.id = UniqueIdentifierGenerator.generateUUID();
    var oTagValueMapping =  CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    var aTagValueMappingValueList =  (oTagValueMapping && oTagValueMapping.mappings) || [];
    if(!CS.isEmpty(aTagValueMappingValueList)) {
      var oTagValueMappingData = CS.find(aTagValueMappingValueList, {id: sMappedTagValueId});
      oTagValueMappingData.tagValue = sNewValue;
    }
    _triggerChange();
  };

  var _handleProfileConfigTagValueIgnoreCaseToggled = function (sId, sMappedTagValueId) {
    var oActiveProfile = _makeActiveProfileDirty();
    var oElement = CS.find(oActiveProfile.tagMappings, {id: sId});
    oElement.id = UniqueIdentifierGenerator.generateUUID();
    var sColumnName = oElement.columnNames[0];
    var oTagValueMapping =  CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    var aTagValueMappingValueList =  (oTagValueMapping && oTagValueMapping.mappings) || [];
    if(!CS.isEmpty(aTagValueMappingValueList)) {
      var oTagValueMappingData = CS.find(aTagValueMappingValueList, {id: sMappedTagValueId});
      oTagValueMappingData.isIgnoreCase = !oTagValueMappingData.isIgnoreCase;
    }
    oElement.isDirty = true;
    _triggerChange();
  };

  var _handleProfileConfigMappedTagValueChanged = function (sId, sMappedTagValueId, sMappedElementId) {
    var oActiveProfile = _makeActiveProfileDirty();
    var oElement = CS.find(oActiveProfile.tagMappings, {id: sId});
    oElement.id = UniqueIdentifierGenerator.generateUUID();
    var sColumnName = oElement.columnNames[0];
    var oTagValueMapping =  CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    var aTagValueMappingValueList =  (oTagValueMapping && oTagValueMapping.mappings) || [];
    if(!CS.isEmpty(aTagValueMappingValueList)) {
      var oMappingObj = CS.find(aTagValueMappingValueList, {id: sMappedTagValueId});
      if(oMappingObj) {
        oMappingObj.mappedTagValueId = sMappedElementId[0];
        oMappingObj.id = UniqueIdentifierGenerator.generateUUID();
      }
      else {
        aTagValueMappingValueList.push({
          id: UniqueIdentifierGenerator.generateUUID(),
          tagValue: UniqueIdentifierGenerator.generateUntitledName(),
          mappedTagValueId: sMappedElementId
        });
      }
      oElement.isDirty = true;
    }
    _triggerChange();
  };

  var _handleProfileConfigMappedTagValueRowDeleteClicked = function (sId, sMappedTagValueId) {
    var oActiveProfile = _makeActiveProfileDirty();
    var oElement = CS.find(oActiveProfile.tagMappings, {id: sId});
    var sColumnName = oElement.columnNames[0];
    var oTagValueMapping =  CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    var aTagValueMappingValueList =  (oTagValueMapping && oTagValueMapping.mappings) || [];
    if(!CS.isEmpty(aTagValueMappingValueList)) {
      CS.remove(aTagValueMappingValueList, {id: sMappedTagValueId});
      oElement.isDirty = true;
    }
    _triggerChange();
  };

  var _handleAddTagValueClicked = function (sId, sMappedElementId) {
    var oActiveProfile = _makeActiveProfileDirty();
    var oAppData = SettingUtils.getAppData();
    var oTagMap = oAppData.getTagMap();
    var oTag = oTagMap[sMappedElementId];
    var oTagChildren = oTag.children;
    var oFirstChild = oTagChildren[0];
    var oElement = CS.find(oActiveProfile.tagMappings, {id: sId});
    if(!oElement.tagValueMappings) {
      oElement.tagValueMappings = [];
    }

    var sColumnName = oElement.columnNames[0];
    var oTagValueMapping = CS.find(oElement.tagValueMappings, {columnName: sColumnName});
    if(oTagValueMapping) {
      if(CS.isEmpty(oTagValueMapping.mappings)) {
        oTagValueMapping.mappings = [];
      }
      oTagValueMapping.mappings.push({
        id: UniqueIdentifierGenerator.generateUUID(),
        tagValue: UniqueIdentifierGenerator.generateUntitledName(),
        mappedTagValueId: oFirstChild.id
      });
    } else {
      var oNewObject = {};
      oNewObject.columnName =  sColumnName;
      oNewObject.mappings =  [];
      oNewObject.mappings.push({
        id: UniqueIdentifierGenerator.generateUUID(),
        tagValue: UniqueIdentifierGenerator.generateUntitledName(),
        mappedTagValueId:oFirstChild.id
      });
      oElement.tagValueMappings.push(oNewObject);
    }
    oElement.isDirty = true;

    _triggerChange();
  };

  var _handleProfileMappedElementChanged = function (sId, sMappedElementId, sSummaryType) {
    var oActiveProfile = _makeActiveProfileDirty();
    var aRows = _getMappingRowsByType(oActiveProfile, sSummaryType);
    var oRow = CS.find(aRows, {id: sId});
    oRow.mappedElementId = sMappedElementId[0];
    oRow.id = UniqueIdentifierGenerator.generateUUID();

    CS.forEach(oRow.tagValueMappings, function (oTagValueMapping ) {
        oTagValueMapping.mappings = [];
    });

    _triggerChange();
  };

  var _handleProfileMappingRowSelected = function (sId) {
    var aSelectedMappingRows = ProfileProps.getSelectedMappingRows();
    if (CS.includes(aSelectedMappingRows, sId)) {
      CS.remove(aSelectedMappingRows, function (sRowId) {
        return sRowId == sId;
      });
    } else {
      aSelectedMappingRows.push(sId);
    }
    _triggerChange();
  };

  var _handleProfileIsIgnoredToggled = function (sId, sSummaryType) {
    var oActiveProfile = _makeActiveProfileDirty();
    var aRows = _getMappingRowsByType(oActiveProfile, sSummaryType);
    var oElement = CS.find(aRows, {id: sId}) || {};
    oElement.isIgnored = !oElement.isIgnored;
    _triggerChange();
  };

  var _handleProfileMappingRowDeleted = function (sId, sSummaryType) {
    var oActiveProfile = _makeActiveProfileDirty();
    var aRows = _getMappingRowsByType(oActiveProfile, sSummaryType);
    CS.remove(aRows, {id: sId});
    _triggerChange();
  };

  var _handleSelectionToggleButtonClicked = function(sKey, sId, bSingleSelect){
    var oActiveProfile = _makeActiveProfileDirty();
    switch(sKey){

      case 'processes':
        var aProcesses = CS.isEmpty(oActiveProfile.processes) ? [] : oActiveProfile.processes;
        var aRemovedProcesses = CS.remove(aProcesses,function (sProcessId) {
          if(sProcessId === sId){
            return true;
          }
        });

        if(CS.isEmpty(aRemovedProcesses)) {
          aProcesses = [sId];
        }

        oActiveProfile.processes = aProcesses;
        break;

      case 'mappings':
        var aMappings = CS.isEmpty(oActiveProfile.mappings) ? [] : oActiveProfile.mappings;
        var aRemovedMappings = CS.remove(aMappings, function (sMappingId) {
          if (sMappingId === sId) {
            return true;
          }
        });

        if (CS.isEmpty(aRemovedMappings)) {
          aMappings = [sId];
        }

        oActiveProfile.mappings = aMappings;
        break;

    }

    _triggerChange();
  };

  var _createSystem = function (sSearchText, sEndpointId) {
    let oNewSystem = {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: sSearchText,
      endpointId: sEndpointId
    };

    SettingUtils.csPutRequest(ProfileRequestMapping.CreateSystem, {}, oNewSystem, successCreateSystemCallback.bind(this, sEndpointId), failureCreateSystemCallback);
  };

  var successCreateSystemCallback = function (sEndpointId, oResponse) {
    let aSystemsList = ProfileProps.getSystemsList();
    let oSystem = oResponse.success;
    aSystemsList.push(oSystem);
    ProfileProps.setSystemsList(aSystemsList);
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oGridRow = CS.find(aGridViewData, {id: sEndpointId});
    oGridRow.properties.systemId.value = [oSystem.id];
    oGridRow.isDirty = true;
    _postProcessEndpointListAndSave({dirty: false});
  };

  var failureCreateSystemCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureCreateSystemCallback', oTranslations); // eslint-disable-line
  };

  var _handleSystemsMSSLoadMoreClicked = function (sEndpointId) {
    let oSystemsListPaginationData = ProfileProps.getSystemsListPaginationData();
    let iNewFrom = oSystemsListPaginationData.from + oSystemsListPaginationData.size;
    let oPaginationData = {
      from: iNewFrom,
      size: 20
    };
    _loadSystemList(sEndpointId, true, oPaginationData)
  };

  var _handleSystemsMSSSearchClicked = function (sSearchText, sEndpointId) {
    ProfileProps.setSystemsListSearchText(sSearchText);
    _loadSystemList(sEndpointId, false);
  };

  var _loadSystemList = function (sEndpointId, bAppend, oPaginationData) {
    let sSearchText = ProfileProps.getSystemsListSearchText();
    let oSystemsListPaginationData = CS.isEmpty(oPaginationData) ? ProfileProps.getSystemsListPaginationData() : oPaginationData;
    let oPostData = {
      searchText: sSearchText,
      from: oSystemsListPaginationData.from,
      size: oSystemsListPaginationData.size
    };

    SettingUtils.csPostRequest(ProfileRequestMapping.GetSystems, {}, oPostData, successLoadSystemListCallback.bind(this, bAppend, sEndpointId, sSearchText), failureLoadSystemListCallback);
  };

  var successLoadSystemListCallback = function (bAppend, sEndpointId, sSearchText, oResponse) {
    let aSystems = oResponse.success;
    let aSystemsList = ProfileProps.getSystemsList();

    let aReferencedSystems = ProfileProps.getReferencedSystems();
    let aSelectedSystem = ProfileProps.getSelectedSystem(sEndpointId);
    let aSelectedSystemsObjects = [];
    CS.forEach(aSystemsList, function (oSystem) {
      if(CS.includes(aSelectedSystem, oSystem.id)) {
        aSelectedSystemsObjects.push(oSystem);
        if (!CS.find(aReferencedSystems, {id: oSystem.id})) {
          aReferencedSystems.push(oSystem);
        }
      }
    });


    if (bAppend) {
      if (CS.isEmpty(aSystems)) {
        alertify.message(getTranslation().REACHED_END_OF_THE_LIST);
      } else {
        aSystemsList = CS.concat(aSystemsList, aSystems);
        let oSystemsListPaginationData = ProfileProps.getSystemsListPaginationData();
        oSystemsListPaginationData.from = oSystemsListPaginationData.from + aSystemsList.length;
      }
    } else {
      aSystemsList = aSystems;
    }


    aSystemsList = aSystemsList.concat(aSelectedSystemsObjects);
    //aSystemsList = aSystemsList.concat(aReferencedSystems);
    aSystemsList = CS.uniqBy(aSystemsList, "id");

    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oProcessesGridData = CS.find(aGridViewData, {id: sEndpointId});
    oProcessesGridData.properties.systemId.items = aSystemsList;
    oProcessesGridData.properties.systemId.searchText = sSearchText;
    if (sSearchText === "") {
      oProcessesGridData.properties.systemId.selectedItems = aSelectedSystem;
      oProcessesGridData.properties.systemId.value = aSelectedSystem;
    } else if (CS.isEmpty(aSystemsList) || CS.isEmpty(CS.find(aSystemsList, {id: oProcessesGridData.properties.systemId.selectedItems[0]}))) {
      oProcessesGridData.properties.systemId.selectedItems = [];
      oProcessesGridData.properties.systemId.value = [];
    }

    ProfileProps.setSystemsList(aSystemsList);
    _triggerChange();
  };

  var failureLoadSystemListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureLoadSystemListCallback', oTranslations); // eslint-disable-line
  };

  var _handleSystemsMSSApplyClicked = function (aSelectedItems) {
    let oActiveProfile = _makeActiveProfileDirty();
    if (CS.isEmpty(aSelectedItems)) {
      oActiveProfile.systemId = "";
    } else {
      oActiveProfile.systemId = aSelectedItems[0]; //single select
    }
    _triggerChange();
  };

  let _createADMForProfile = function (oActiveProfile, oClonedProfile) {
    let oProfileToSave = CS.cloneDeep(oClonedProfile);

    oProfileToSave.addedProcesses = CS.difference(oClonedProfile.processes, oActiveProfile.processes);
    oProfileToSave.deletedProcesses = CS.difference(oActiveProfile.processes, oClonedProfile.processes);
    delete oProfileToSave.processes;

    oProfileToSave.addedMappings = CS.difference(oClonedProfile.mappings, oActiveProfile.mappings);
    oProfileToSave.deletedMappings = CS.difference(oActiveProfile.mappings, oClonedProfile.mappings);
    delete oProfileToSave.mappings;

    oProfileToSave.addedAuthorizationMappings =  CS.difference(oClonedProfile.authorizationMapping , oActiveProfile.authorizationMapping);
    oProfileToSave.deletedAuthorizationMappings = CS.difference(oActiveProfile.authorizationMapping , oClonedProfile.authorizationMapping);
    delete oProfileToSave.authorizationMapping;

    oProfileToSave.addedJmsProcesses = CS.difference(oClonedProfile.jmsProcesses, oActiveProfile.jmsProcesses);
    oProfileToSave.deletedJmsProcesses = CS.difference(oActiveProfile.jmsProcesses, oClonedProfile.jmsProcesses);
    delete oProfileToSave.jmsProcesses;


    oProfileToSave.physicalCatalogs = oClonedProfile.physicalCatalogs;
    // oProfileToSave.dataCatalogIds = oClonedProfile.dataCatalogIds;

    if (oClonedProfile.systemId !== oActiveProfile.systemId) {
      oProfileToSave.addedSystemId = oClonedProfile.systemId;
      oProfileToSave.deletedSystemId = oActiveProfile.systemId;
      delete oProfileToSave.systemId;
    }

    if (oClonedProfile.dashboardTabId !== oActiveProfile.dashboardTabId) {
      oProfileToSave.addedDashboardTabId = oClonedProfile.dashboardTabId;
      oProfileToSave.deletedDashboardTabId = oActiveProfile.dashboardTabId;
      delete oProfileToSave.dashboardTabId;
    }
    return oProfileToSave;
  };

  let _handleDeleteEndpointFailure = function (aFailureIds) {
    let aEndpointsAlreadyDeleted = [];
    let aUnhandleEndpoints = [];
    let aEndpointGridData = ProfileProps.getEndpointGridData();
    CS.forEach(aFailureIds, function (oItem) {
      let oEvent = CS.find(aEndpointGridData, {id: oItem.itemId});
      if (oItem.key == "EndpointNotFoundException") {
        aEndpointsAlreadyDeleted.push(oEvent.label);
      } else {
        aUnhandleEndpoints.push(oEvent.label);
      }
    });

    if (aEndpointsAlreadyDeleted.length > 0) {
      let sEventsAlreadyDeleted = aEndpointsAlreadyDeleted.join(',');
      alertify.error(Exception.getCustomMessage("Endpoint_already_deleted", getTranslation(), sEventsAlreadyDeleted), 0);
    }
    if (aUnhandleEndpoints.length > 0) {
      let sUnhandleEvents = aUnhandleEndpoints.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_Endpoint", getTranslation(), sUnhandleEvents), 0);
    }
  };

  let successDeleteProfilesCallback = function (oResponse) {
    let aSuccessIds = oResponse.success;
    let aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oEndpointMap = SettingUtils.getAppData().getEndpointMap();
    let oGridViewSkeleton = oGridViewPropsByContext.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      CS.pull(oGridViewSkeleton.selectedContentIds, sId);
      delete oEndpointMap[sId];
    });
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - aSuccessIds.length);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().ENDPOINT}));

    if (aFailureIds && aFailureIds.length > 0) {
      _handleDeleteEndpointFailure(aFailureIds);
    }
  };

  let successGetProfileCallback = function (oCallback, oResponse) {
    let oActiveEndpoint = ProfileProps.getSelectedProfile();
    delete oActiveEndpoint.isCreated;
    let oSavedEndpoint = oResponse.success.endpoint;
    let aEndpointsGridData = ProfileProps.getEndpointGridData();
    aEndpointsGridData.push(oSavedEndpoint);
    let oConfigDetails = oResponse.success.configDetails; // changed the response for create call.
    //add the referenced data into the list (for systems mss):
    let oReferencedSystems = CS.isEmpty(oConfigDetails) ? oResponse.success.referencedSystems : oConfigDetails.referencedSystems ;
    let oReferencedDashboardTabs = CS.isEmpty(oConfigDetails) ? oResponse.success.referencedDashboardTabs : oConfigDetails.referencedDashboardTabs ;
    let aSystemsList = ProfileProps.getSystemsList();
    ProfileProps.setSystemsList(CS.concat(aSystemsList, CS.values(oReferencedSystems)));
    ProfileProps.setReferencedDashboardTabs(oReferencedDashboardTabs);
    ProfileProps.setReferencedSystems(oReferencedSystems);

    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let oProcessedEndpoint = GridViewStore.getProcessedGridViewData(GridViewContexts.END_POINT, [oSavedEndpoint], "", oConfigDetails)[0];
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedEndpoint);
    let oEndpointMap = SettingUtils.getAppData().getEndpointMap();
    oEndpointMap[oSavedEndpoint.id] = oSavedEndpoint;
    _setSelectedProfile(oSavedEndpoint);
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);

    SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    if(oCallback.isCreated){
      delete oEndpointMap.isCreated;
      alertify.success(getTranslation().ENDPOINT_CREATED_SUCCESSFULLY);
    }
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    _triggerChange();
  };

  let _createProfile = function (bIsMDMCreateClicked) {
    if (bIsMDMCreateClicked) {
      let oSelectedProfile = _makeActiveProfileDirty();
      _processAndCreateProfile(oSelectedProfile);
    } else {
      let oNewProfileToCreate = _createDummyProfile();
      _setSelectedProfile(oNewProfileToCreate);
      _triggerChange();
    }
  };

  let _processAndCreateProfile = function (oNewProfileToCreate) {
    if (CS.isEmpty(oNewProfileToCreate.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    if (!SettingUtils.isValidEntityCode(oNewProfileToCreate.code)) {
      alertify.error(getTranslation().PLEASE_ENTER_VALID_CODE);
      return;
    }
    let oCallback = {};
    oCallback.isCreated = true;
    _createProfileCall(oNewProfileToCreate, oCallback);
  };

  let _deleteProfiles = function (aBulkDeleteList) {
    if (!CS.isEmpty(aBulkDeleteList)) {
      return SettingUtils.csDeleteRequest(ProfileRequestMapping.DeleteProfile, {}, {ids: aBulkDeleteList}, successDeleteProfilesCallback, failureDeleteProfilesCallback)
    } else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().ENDPOINTS}));
      _triggerChange();
    }
  };

  let _getAllEndpointsForGridView = function () {
    let oEndpointConfigGridViewSkeleton = new EndpointConfigGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.END_POINT, {skeleton: oEndpointConfigGridViewSkeleton});
    _fetchEndpointListForGridView();
  };

  let _fetchEndpointListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.END_POINT);
    SettingUtils.csPostRequest(ProfileRequestMapping.Grid, {}, oPostData, successGetAllEndpointsGrid, failureGetAllEndpointsGrid);
  };

  let successGetAllEndpointsGrid = function (oResponse) {
    let aEndpointsListFromServer = oResponse.success.endpointsList;
    let oConfigDetails = oResponse.success.configDetails;
    ProfileProps.setReferencedDataRules(oConfigDetails.referencedDataRules);
    ProfileProps.setReferencedMappings(oConfigDetails.referencedMappings);
    ProfileProps.setReferencedProcesses(oConfigDetails.referencedProcesses);
    ProfileProps.setReferencedDashboardTabs(oConfigDetails.referencedDashboardTabs);
    ProfileProps.setReferencedAuthorizationMappings(oConfigDetails.referencedAuthorizationMappings);
    ProfileProps.setReferencedJmsProcesses(oConfigDetails.referencedJmsProcesses);
    ProfileProps.setConfigDetails(oConfigDetails);

    let aEndpointsList = [];
    let aReferencedSystems = [];
    let aMasterSystemList = [];
    CS.forEach(aEndpointsListFromServer, function (oEndpoint) {
      aEndpointsList.push(oEndpoint.endpoint);
    });

    CS.forEach(oConfigDetails.referencedSystems, function (oSystem) {
      aMasterSystemList = CS.concat(aMasterSystemList, oSystem);
      aReferencedSystems.push(oSystem);
    });
    let aSystemsList = ProfileProps.getSystemsList();
    ProfileProps.setSystemsList(aSystemsList.concat(aMasterSystemList));
    ProfileProps.setReferencedSystems(aReferencedSystems);

    GridViewStore.preProcessDataForGridView(GridViewContexts.END_POINT, aEndpointsList,
        oResponse.success.count, oConfigDetails);
    SettingUtils.getAppData().setEndpointMap(aEndpointsList);
    ProfileProps.setEndpointGridData(aEndpointsList);
    _triggerChange();
  };

  let failureGetAllEndpointsGrid = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGetAllEndpointsGrid", getTranslation());
  };

  let _postProcessEndpointListAndSave = function (oCallbackData) {
    let aEndpointsGridData = ProfileProps.getEndpointGridData();
    let aEndpointListToSave = [];

    GridViewStore.processGridDataToSave(aEndpointListToSave, GridViewContexts.END_POINT, aEndpointsGridData);
    let aListOfEndpointADMs = [];
    CS.forEach(aEndpointListToSave, (oEndpointToSave) => {
      let oOriginalEndPoint = CS.find(aEndpointsGridData, {id: oEndpointToSave.id});
      aListOfEndpointADMs.push(_createADMForProfile(oOriginalEndPoint, oEndpointToSave));
    });
    _saveEventsInBulk(aListOfEndpointADMs, oCallbackData);
  };

  let _saveEventsInBulk = function (aEndpointListToSave, oCallBackData) {
    if (_safeToSave(aEndpointListToSave)) {
      let aEndpointGridData = ProfileProps.getEndpointGridData();
      let oPostRequest = [];
      let oData = {};
      oData.codes = [];
      oData.names = [];
      oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_ENDPOINT;
      CS.forEach(aEndpointListToSave, function (oEndpointToSave) {
        let oEndpointGridData = CS.find(aEndpointGridData, {id: oEndpointToSave.id});
        if (oEndpointGridData.label !== oEndpointToSave.label) {
          oData.names.push(oEndpointToSave.label);
        }
      });
      oPostRequest.push(oData);
      oCallBackData.serverCallback = {
        requestURL: ProfileRequestMapping.SaveProfile,
        endpointToSave: aEndpointListToSave,
        isSave: true,
      };
      let aUniqueNameList = CS.uniq(oData.names);
      if (aUniqueNameList.length !== oData.names.length) {
        alertify.error(getTranslation().ENDPOINT_NAME_ALREADY_EXISTS);
      } else {
        SettingUtils.csPostRequest(ProfileRequestMapping.BulkCodeCheck, {}, oPostRequest, successBulkCodeCheckList.bind(this, oCallBackData), failureBulkCodeCheckList);
      }
    }
  };

  let _safeToSave = function (aEndpointListToSave) {
    var bSafeToSave = true;

    if (CS.isEmpty(aEndpointListToSave)) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return false;
    }

    CS.forEach(aEndpointListToSave, function (oEndpoint) {
      if(CS.trim(oEndpoint.label) == "") {
        bSafeToSave = false;
        alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
        return false;
      }
    });

    return bSafeToSave;
  };

  let successSaveEndpointsInBulk = function (oCallbackData, oResponse) {
    let aEndpointsListFromServer = oResponse.success.endpointsList;
    let oConfigDetails = oResponse.success.configDetails;
    ProfileProps.setConfigDetails(oConfigDetails);
    let aEndpointsList = [];
    let aSystemsList = ProfileProps.getSystemsList();
    CS.forEach(aEndpointsListFromServer, function (oEndpoint) {
      aEndpointsList.push(oEndpoint.endpoint);
    });
    CS.forEach(oConfigDetails.referencedSystems, function (oData, sKey) {
      if (CS.isEmpty(CS.find(aSystemsList, {id: sKey}))) {
        aSystemsList.push(oData);
      }
    });

    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.END_POINT, aEndpointsList, "", oConfigDetails);
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let aEndpointGridData = ProfileProps.getEndpointGridData();
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oMasterEndpointsMap = SettingUtils.getAppData().getEndpointMap();

    CS.forEach(aEndpointsList, function (oEndpoint) {
      var sEndpointId = oEndpoint.id;
      var iIndex = CS.findIndex(aEndpointGridData, {id: sEndpointId});
      aEndpointGridData[iIndex] = oEndpoint;
      oMasterEndpointsMap[sEndpointId] = oEndpoint;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedEndpoint) {
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedEndpoint.id});
      aGridViewData[iIndex] = oProcessedEndpoint;
    });

    let oActiveEndpoint = ProfileProps.getSelectedProfile();
    if(!CS.isEmpty(oActiveEndpoint)) {
      ProfileProps.setSelectedProfile(aEndpointsListFromServer[0].endpoint);
    }

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().ENDPOINTS}));
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _fetchEndpointListForGridView();
    _triggerChange();
  };

  let failureSaveEndpointsInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveEndpointsInBulk", getTranslation());
  };

  let _handleAttributeValueChanged = function (sKey, sVal) {
    var oActiveEndpoint = _makeActiveProfileDirty();
    oActiveEndpoint[sKey] = sVal;
    _triggerChange();
  };

  let _discardEndpointGridViewChanges = function (oCallbackData) {
    var aEndpointsGridData = ProfileProps.getEndpointGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    var bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedEndpoint, iIndex) {
      if (oOldProcessedEndpoint.isDirty) {
        var oEndpoint = CS.find(aEndpointsGridData, {id: oOldProcessedEndpoint.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.END_POINT, [oEndpoint])[0];
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

  let _getEndpointData = function(sEntity , oCallback){
    oCallback = {};
    oCallback.isCreated = false;
    SettingUtils.csGetRequest(ProfileRequestMapping.GetProfile,  {id: sEntity}, successFetchProfileDetailsCallback.bind(this, oCallback), failureFetchProfileDetailsCallback);
  };

  let successFetchProfileDetailsCallback = function(oCallback, oResponse){
    let oEndpoint = oResponse.success;
    let oEndpointFromServer = oEndpoint.endpoint;
    let oConfigDetailsFromServer = oEndpoint.configDetails;
    let oConfigDetails = ProfileProps.getConfigDetails();
    CS.assign(oConfigDetails.referencedProcesses, oConfigDetailsFromServer.referencedProcesses);
    ProfileProps.setConfigDetails(oConfigDetails);
    ProfileProps.setSelectedProfile(oEndpointFromServer);
    let oMasterProfileList = SettingUtils.getAppData().getEndpointMap();
    let oEndpointFromList = oMasterProfileList[oEndpointFromServer.id];
    oEndpointFromList.label = oEndpointFromServer.label;
    oEndpointFromList.code = oEndpointFromServer.code;

    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    if(oCallback.isCreated){
      let oProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.END_POINT, [oEndpointFromServer], "", oConfigDetails)[0];
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
      aGridViewData.unshift(oProcessedGridViewData);
      SettingUtils.getAppData().setProcessList(oMasterProfileList);
      let aEndpointData = ProfileProps.getEndpointGridData();
      aEndpointData.push(oEndpointFromList);
      oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1 );
    }else{
      let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.END_POINT, oMasterProfileList, "", oConfigDetails);
      oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);
      let aEndpointData = ProfileProps.getEndpointGridData();
      aEndpointData.push(oEndpointFromList);

    }
    delete oEndpointFromList.clonedObject;
    delete oEndpointFromList.isDirty;
    delete oEndpointFromList.isCreated;

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    if (oCallback.isCreated) {
      /** To open edit view immediately after create */
      ProfileProps.setIsProfileDialogActive(true);
    }

    _triggerChange();
  };

  let failureFetchProfileDetailsCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchProfileDetailsCallback", getTranslation);
  };

  let _editButtonClicked = function (sEntityId) {
    ProfileProps.setIsProfileDialogActive(true);
    let oCallback = {};
    _getEndpointData(sEntityId, oCallback);
  };

  let _discardUnsavedEndpoint = function (oCallbackData) {
    let oActiveProcess = ProfileProps.getSelectedProfile();
    let bScreenLockStatus = ProfileProps.getProfileScreenLockStatus();
    if(!bScreenLockStatus) {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      return;
    }

    if (!CS.isEmpty(oActiveProcess)) {
      if (oActiveProcess.clonedObject) {
        delete oActiveProcess.clonedObject;
        delete oActiveProcess.isDirty;
        alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
      }
      ProfileProps.setProfileScreenLockStatus(false);
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      _triggerChange();

    } else {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
    }
  };

  let _saveEndpoint = function (oCallback) {
    let oActiveEndpoint = ProfileProps.getSelectedProfile();
    let aListOfEndpointADMs = [];
    aListOfEndpointADMs.push(_createADMForProfile(oActiveEndpoint, oActiveEndpoint.clonedObject));
    oCallback = {};
    oCallback.functionToExecute = function(){
      delete oActiveEndpoint.clonedObject;
      delete oActiveEndpoint.isDirty;
    };
    _saveEventsInBulk(aListOfEndpointADMs, oCallback);
  };

  let _closeEndpointDialog = () => {
    let oProcessValueMap = ProfileProps.getEndpointsList();
    let oActiveEndpoint = ProfileProps.getSelectedProfile();
    let oListObject = SettingUtils.getDefaultListProps();
    oListObject.id = oActiveEndpoint.id;
    oProcessValueMap[oActiveEndpoint.id] = oListObject;
    ProfileProps.setSelectedProfile({});
    _triggerChange();
  };

  let _fetchTaxonomyById = function (sContext, sTaxonomyId) {
    if (sContext == "majorTaxonomy") {
      if (sTaxonomyId == "addItemHandlerforMultiTaxonomy") {
        sTaxonomyId = SettingUtils.getTreeRootId();
      } else {
        sTaxonomyId = sTaxonomyId;
      }
    }
    let oTaxonomyPaginationData = {};
    oTaxonomyPaginationData = ProfileProps.getTaxonomyPaginationData();
    oTaxonomyPaginationData.taxonomyTypes = ["majorTaxonomy", "minorTaxonomy"];
    oTaxonomyPaginationData.taxonomyId = sTaxonomyId;

    let oPostData = CS.assign({taxonomyId: sTaxonomyId}, oTaxonomyPaginationData);

    SettingUtils.csPostRequest(oSettingScreenRequestMapping.GetMajorTaxonomy, {}, oPostData, successFetchTaxonomy, failureFetchTaxonomyCallback);

  };

  let successFetchTaxonomy = function (oResponse) {
    let aTaxonomyListFromServer = oResponse.success.list;
    ProfileProps.setParentTaxonomyList(aTaxonomyListFromServer);
    let aTaxonomyList = ProfileProps.getAllowedTaxonomies();
    ProfileProps.setAllowedTaxonomies(CS.concat(aTaxonomyList, aTaxonomyListFromServer));
    _triggerChange();
  };

  let failureFetchTaxonomyCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTaxonomyCallback", getTranslation);
  };

  let _handleTaxonomyAdded = function (oModel, sParentTaxonomyId, sViewContext) {
    let oActiveEndpoint = _makeActiveProfileDirty();
    let oConfigDetails = ProfileProps.getConfigDetails();
    let oReferencedTaxonomies = CS.isEmpty(oConfigDetails) ? ProfileProps.getReferencedTaxonomies() : oConfigDetails.referencedTaxonomies;
    let aSelectedTaxonomies = oActiveEndpoint.taxonomyIds;
    let bIsParentTaxonomyPresent = CS.includes(aSelectedTaxonomies,sParentTaxonomyId);
    aSelectedTaxonomies.push(oModel.id);
    if (!bIsParentTaxonomyPresent) {
      let temp = {};
      temp.id = oModel.id;
      temp.parent = {};
      temp.parent.id = "-1";
      temp.parent.label = null;
      temp.parent.parent = null;
      temp.label = oModel.label;
      oReferencedTaxonomies[oModel.id] = temp;
      ProfileProps.setReferencedTaxonomies(oReferencedTaxonomies);
    } else {
      let temp = {};
      temp.id = oModel.id;
      temp.label = oModel.label;
      temp.parent = oReferencedTaxonomies[sParentTaxonomyId];
      oReferencedTaxonomies[oModel.id] = temp;
      delete oReferencedTaxonomies[sParentTaxonomyId];
      let iParentIndex = aSelectedTaxonomies.indexOf(temp.parent.id);
      aSelectedTaxonomies.splice(iParentIndex, 1);
      ProfileProps.setReferencedTaxonomies(oReferencedTaxonomies);
    }

    _triggerChange();
  };

  let _handleLazyMSSValueRemoved = function (sOldContextKey,sId) {
    let oActiveEndpoint = _makeActiveProfileDirty();
    switch (sOldContextKey) {
      case "klassTypes" :
        let aNatureClassType = oActiveEndpoint['klassIds'];
        let iIndex = CS.indexOf(aNatureClassType, sId);
        aNatureClassType.splice(iIndex, 1);
        break;

      case "attributes" :
        let aAttributes = oActiveEndpoint['attributeIds'];
        let iIndexForAttribute = CS.indexOf(aAttributes, sId);
        aAttributes.splice(iIndexForAttribute, 1);
        break;

      case "tags" :
        let aTags = oActiveEndpoint['tagIds'];
        let iIndexForTag = CS.indexOf(aTags, sId);
        aTags.splice(iIndexForTag, 1);
        break;

      case "processes" :
        let aProcesses = oActiveEndpoint['processes'];
        let iIndexForProcesses = CS.indexOf(aProcesses, sId);
        aProcesses.splice(iIndexForProcesses, 1);
        break;

      case "mappings" :
        let aMappings = oActiveEndpoint['mappings'];
        let iIndexForMappings = CS.indexOf(aMappings, sId);
        aMappings.splice(iIndexForMappings, 1);
        break;

      case "authorizationMappings" :
        let aAuthorizationMapping = oActiveEndpoint['authorizationMapping'];
        let iIndexForAuthorizationMappings = CS.indexOf(aAuthorizationMapping, sId);
        aAuthorizationMapping.splice(iIndexForAuthorizationMappings, 1);
        break;

      case "jmsProcesses" :
        let aJmsProcesses = oActiveEndpoint['jmsProcesses'];
        let iIndexForJmsProcesses = CS.indexOf(aJmsProcesses, sId);
        aJmsProcesses.splice(iIndexForJmsProcesses, 1);
        break;
    }
    _triggerChange();

  };

  let _handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oTaxonomy, sParentTaxonomyId, sViewContext) {

    let oActiveEndpoint = _makeActiveProfileDirty();
    let oConfigDetails = ProfileProps.getConfigDetails();
    let oReferencedTaxonomies = oConfigDetails.referencedTaxonomies;
    let aSelectedTaxonomies = oActiveEndpoint.taxonomyIds;
    let iActiveTaxonomyIndex = aSelectedTaxonomies.indexOf(sParentTaxonomyId);
    aSelectedTaxonomies.splice(iActiveTaxonomyIndex, 1);
    let sParentId = oTaxonomy.parent.id;
    if (sParentId !== "-1") {
      if (!CS.includes(aSelectedTaxonomies, sParentId)) {
        aSelectedTaxonomies.push(sParentId);
        oReferencedTaxonomies[sParentId] = oTaxonomy.parent;
      }
    }
    delete oReferencedTaxonomies[sParentTaxonomyId];

    _triggerChange();
  };

  let _handleSaveAsDialogCloseButtonClicked = function () {
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let oGridSkeleton = oGridViewProps.getGridViewSkeleton();
    oGridSkeleton.selectedContentIds = [];
    ProfileProps.setIsSaveAsDialogActive(false);
    ProfileProps.setDuplicates();
    _triggerChange();
  };

  let _handleSaveAsDialogSaveButtonClicked = function () {
    let aGridViewData = ProfileProps.getDataForSaveAsDialog();
    let aGridViewWFData = ProfileProps.getDataForWorkflowSaveAsDialog();
    let aDataToSave = _prepareDataForSaveAs(aGridViewData, aGridViewWFData);
    let oCallbackData = {};
    let aListOfCodesToCheckForEndpoint = [];
    let aListOfNamesToCheckForEndpoint = [];
    let aListOfCodesToCheckForWorkflow = [];
    let aListOfNamesToCheckForWorkflow = [];
    let aInvalidCodesForEndpoint = [];
    let aInvalidCodesForWorkflow = [];
    let aInvalidLabelsForEndpoint = [];
    let aInvalidLabelsForWorkflow = [];
    let oPostData = [];
    oCallbackData.serverCallback = {
      endpointToClone: aDataToSave,
      isClone: true,
    };
    oCallbackData.isSaveAsDialogActive = true;
    oCallbackData.isSaveAsCall = true;

    /** To Validate Code and Name **/
    CS.forEach(aDataToSave, function (oElement) {
      let bValidEntityCodeForEndpoint = SettingUtils.isValidEntityCode(oElement.toEndPointCode);
      bValidEntityCodeForEndpoint ? aListOfCodesToCheckForEndpoint.push(oElement.toEndPointCode) : aInvalidCodesForEndpoint.push(oElement.toEndPointCode);

      let bValidEndpointName = CS.isNotEmpty(CS.trim(oElement.toEndPointName)) ? true : false;
      bValidEndpointName ? aListOfNamesToCheckForEndpoint.push(oElement.toEndPointName) : aInvalidLabelsForEndpoint.push(oElement.toEndPointName);

      CS.forEach(oElement.workflowsToCopy, oWFElement => {
        let bValidEntityCodeForWorkflow = SettingUtils.isValidEntityCode(oWFElement.toWorkflowCode);
        bValidEntityCodeForWorkflow ? aListOfCodesToCheckForWorkflow.push(oWFElement.toWorkflowCode) : aInvalidCodesForWorkflow.push(oWFElement.toWorkflowCode);

        let bValidWorkflowName = CS.isNotEmpty(CS.trim(oWFElement.toWorkflowName)) ? true : false;
        bValidWorkflowName ? aListOfNamesToCheckForWorkflow.push(oWFElement.toWorkflowName) : aInvalidLabelsForWorkflow.push(oWFElement.toWorkflowName);
      })
    });

    if (CS.isNotEmpty(aInvalidCodesForEndpoint) || CS.isNotEmpty(aInvalidCodesForWorkflow)
        || CS.isNotEmpty(aInvalidLabelsForEndpoint) || CS.isNotEmpty(aInvalidLabelsForWorkflow)) {
      alertify.error(getTranslation().PLEASE_ENTER_VALID_NAME_AND_CODE);
      ProfileProps.setDuplicates(aInvalidCodesForEndpoint, aInvalidCodesForWorkflow, aInvalidLabelsForEndpoint, aInvalidLabelsForWorkflow);
      return null;
    }

    let aUniqueListForWorkflowCode = CS.uniq(aListOfCodesToCheckForWorkflow);
    let aUniqueListForWorkflowLabel = CS.uniq(aListOfNamesToCheckForWorkflow);

    if ((aUniqueListForWorkflowCode.length !== aListOfCodesToCheckForWorkflow.length) || (aUniqueListForWorkflowLabel.length !== aListOfNamesToCheckForWorkflow.length)) {
      let aDuplicateCodesForWorkflows = CS.filter(aListOfCodesToCheckForWorkflow, (val, i, iteratee) => CS.includes(iteratee, val, i + 1));
      let aDuplicateLabelsForWorkflows = CS.filter(aListOfNamesToCheckForWorkflow, (val, i, iteratee) => CS.includes(iteratee, val, i + 1));
      ProfileProps.setDuplicateCodesForWorkflows(aDuplicateCodesForWorkflows);
      ProfileProps.setDuplicateLabelsForWorkflows(aDuplicateLabelsForWorkflows);
      alertify.error(getTranslation().PLEASE_ENTER_UNIQUE_NAME_AND_CODE);
      return null;
    }

    /** data preperation for Bulk Code check Call**/
    let aListOfEntityType = [ConfigEntityTypeDictionary.ENTITY_TYPE_ENDPOINT, ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS_EVENT];
    CS.forEach(aListOfEntityType, function (sEntityType) {
      let oData = {};
      oData.codes = (sEntityType === ConfigEntityTypeDictionary.ENTITY_TYPE_ENDPOINT) ? aListOfCodesToCheckForEndpoint : aListOfCodesToCheckForWorkflow;
      oData.entityType = sEntityType;
      oData.names = (sEntityType === ConfigEntityTypeDictionary.ENTITY_TYPE_ENDPOINT) ? aListOfNamesToCheckForEndpoint : aListOfNamesToCheckForWorkflow;
      !CS.includes(oData.names, null) && oPostData.push(oData);
    });
    SettingUtils.csPostRequest(ProfileRequestMapping.BulkCodeCheck, {}, oPostData, successBulkCodeCheckList.bind(this, oCallbackData), failureBulkCodeCheckList);
  };

  /** data preperation for Save Call**/
  let _prepareDataForSaveAs = function (aGridViewData, aGridViewWFData) {
    let aDataToSave = [];
    CS.forEach(aGridViewData, function (oData) {
      let oNewDataToSave = {};
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT_COPY_PROCESS);
      let oWorkflowSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
      let aSelectedContentIds = oWorkflowSkeleton.selectedContentIds;
      let oProperties = oData.properties;
      oNewDataToSave.fromEndPointId = oData.id;
      oNewDataToSave.toEndPointName = oProperties["newLabel"].value;
      oNewDataToSave.toEndPointCode = oProperties["newCode"].value;
      oNewDataToSave.workflowsToCopy = [];
      CS.forEach(aGridViewWFData, (oWFData) => {
        if (CS.includes(aSelectedContentIds, oWFData.id)) {
          let oWFProperties = oWFData.properties;
          let oNewWFDataToSave = {};
          oNewWFDataToSave.fromWorkflowId = oWFData.id;
          oNewWFDataToSave.toWorkflowName = oWFProperties["newLabel"].value;
          oNewWFDataToSave.toWorkflowCode = oWFProperties["newCode"].value;
          oNewDataToSave.workflowsToCopy.push(oNewWFDataToSave);
        }
      });
      aDataToSave.push(oNewDataToSave);
    });
    return aDataToSave;
  };

  let _saveAsEndpointCall = function (aDataToSave, oCallbackData) {
    SettingUtils.csPostRequest(ProfileRequestMapping.SaveAs, {}, aDataToSave, successBulkSaveEndpointList.bind(this, oCallbackData), failureSaveAs);
  };

  /** To Check Duplicate Name and Code received from backend **/
  let _checkDuplicate = function (oNameOrCodeToCheck, aDuplicateList) {
    CS.forEach(oNameOrCodeToCheck, (bValue, skey) => {
      if (!bValue) {
        aDuplicateList.push(skey);
      }
    });
  };

  let successBulkCodeCheckList = function (oCallBackData, oResponse) {
    let oSuccess = oResponse.success;
    let oCodeEndpoint = oSuccess.codeCheck.Endpoint || {};
    let oNameEndpoint = oSuccess.nameCheck.Endpoint || {};
    let aCodeDuplicationForEndpoints = [];
    let aNameDuplicationForEndpoints = [];
    let oCodeWorkflow = oSuccess.codeCheck.Process_Event || {};
    let oNameWorkflow = oSuccess.nameCheck.Process_Event || {};
    let aCodeDuplicationForWorkflows = [];
    let aNameDuplicationForWorkflows = [];

    _checkDuplicate(oCodeEndpoint, aCodeDuplicationForEndpoints);
    _checkDuplicate(oNameEndpoint, aNameDuplicationForEndpoints);
    _checkDuplicate(oCodeWorkflow, aCodeDuplicationForWorkflows);
    _checkDuplicate(oNameWorkflow, aNameDuplicationForWorkflows);

    if (CS.isEmpty(aCodeDuplicationForEndpoints) && CS.isEmpty(aNameDuplicationForEndpoints)
        && CS.isEmpty(aCodeDuplicationForWorkflows) && CS.isEmpty(aNameDuplicationForWorkflows)) {
      if (oCallBackData.serverCallback) {
        let oServerCallback = oCallBackData.serverCallback;
        if (oServerCallback.isSave) {
          SettingUtils.csPostRequest(oServerCallback.requestURL, {}, oServerCallback.endpointToSave, successSaveEndpointsInBulk.bind(this, oCallBackData), failureSaveEndpointsInBulk);
        } else if (oServerCallback.isCreated) {
          delete oServerCallback.endpointToCreate.isCreated;
          SettingUtils.csPutRequest(oServerCallback.requestURL, {}, oServerCallback.endpointToCreate, successGetProfileCallback.bind(this, oCallBackData), failureGetProfileCallback);
        } else if (oServerCallback.isClone) {
          _saveAsEndpointCall(oServerCallback.endpointToClone, oCallBackData);
        }
      }
    } else {
      if (oCallBackData.isSaveAsCall) {
        ProfileProps.setDuplicates(aCodeDuplicationForEndpoints, aCodeDuplicationForWorkflows, aNameDuplicationForEndpoints, aNameDuplicationForWorkflows);
        alertify.error(getTranslation().CODE_OR_NAME_ALREADY_EXISTS);
      } else {
        CS.isNotEmpty(aNameDuplicationForEndpoints) && alertify.error(getTranslation().ENDPOINT_NAME_ALREADY_EXISTS);
        CS.isNotEmpty(aCodeDuplicationForEndpoints) && alertify.error(getTranslation().ENDPOINT_CODE_ALREADY_EXISTS);
      }
    }
    _triggerChange();
  };

  let failureBulkCodeCheckList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureBulkCodeCheckList", getTranslation());
  };

  let successBulkSaveEndpointList = function (oCallBackData, oResponse) {
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let aEndpointList = oResponse.success.endpointsList;
    let oConfigDetails = ProfileProps.getConfigDetails();
    CS.assign(oConfigDetails.referencedProcesses, oResponse.success.configDetails.referencedProcesses);
    CS.assign(oConfigDetails.referencedJmsProcesses, oResponse.success.configDetails.referencedJmsProcesses);
    ProfileProps.setConfigDetails(oConfigDetails);
    let aEndpointGridData = ProfileProps.getEndpointGridData();
    let aEndpointListMap = SettingUtils.getAppData().getEndpointMap();
    let aProcessedGridViewData = [];

    CS.forEach(aGridViewData, function (oData) {
      oData.isDirty = false;
    });

    CS.forEach(aEndpointList, function (oEndpoint) {
      let oProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.END_POINT, [oEndpoint.endpoint], "", oConfigDetails)[0];
      aProcessedGridViewData.push(oProcessedGridViewData);
    });

    CS.forEach(aEndpointList, function (oEndpoint) {
      oEndpoint = oEndpoint.endpoint;
      let endpointId = oEndpoint.id;
      let iIndex = CS.findIndex(aEndpointGridData, {id: endpointId});
      if (iIndex != -1) {
        aEndpointGridData[iIndex] = oEndpoint;
        aEndpointListMap[endpointId] = oEndpoint;
      } else if (oCallBackData.isSaveAsDialogActive && (iIndex === -1)) {
        aEndpointGridData.unshift(oEndpoint);
        //let aProcessedGridViewData = _preProcessEndpointDataForGridView([oEndpoint])[0];
        let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.END_POINT, [oEndpoint], "", oConfigDetails)[0];
        aGridViewData.unshift(aProcessedGridViewData);
        aEndpointListMap[endpointId] = oEndpoint;
      }
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedEndpoint) {
      let iIndex = CS.findIndex(aGridViewData, {id: oProcessedEndpoint.id});
      aGridViewData[iIndex] = oProcessedEndpoint;
    });

    /**Deselecting the checkboxes after saving **/
    if (oCallBackData && oCallBackData.isSaveAsDialogActive) {
      let oGridSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
      oGridSkeleton.selectedContentIds = [];
    }
    if (oCallBackData && oCallBackData.isSaveAsCall) {
      alertify.success(getTranslation().ENDPOINT_CLONED_SUCCESSFULLY);
      ProfileProps.setDuplicates();
    }else {
      alertify.success(getTranslation().ENDPOINT_CREATED_SUCCESSFULLY);
    }
    // oScreenProps.setIsGridDataDirty(false);
    oCallBackData && oCallBackData.functionToExecute && oCallBackData.functionToExecute();
    ProfileProps.setIsSaveAsDialogActive(false);
    _fetchEndpointListForGridView();
    _triggerChange();
  };

  let failureSaveAs = function (oResponse) {
    alertify.error(getTranslation().ERROR_DURING_COPY_OPERATION);
  };

  let _saveAsEndpoint = function () {
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.END_POINT);
    let oGridSkeleton = oGridViewProps.getGridViewSkeleton();
    let aSelectedContentIds = oGridSkeleton.selectedContentIds;
    let aEndpointData = ProfileProps.getEndpointGridData();
    let aDataForSaveAsDialog = [];
    let aDataForWorkflowSaveAsDialog = [];
    if (CS.isEmpty(aSelectedContentIds)) {
      alertify.error(getTranslation().TARGET_PLEASE_SELECT);
    } else if (aSelectedContentIds.length === 1) {
      CS.forEach(aSelectedContentIds, function (sSelectedId) {
        let oEndpoint = CS.find(aEndpointData, {id: sSelectedId});
        let aProcessIds = CS.concat(oEndpoint.processes, oEndpoint.jmsProcesses);
        let oReferencedProcesses = CS.merge(ProfileProps.getConfigDetails().referencedProcesses, ProfileProps.getConfigDetails().referencedJmsProcesses);
        if (!CS.isEmpty(oEndpoint)) {
          let oNewEndpointDataForCopyDialog = {};
          oNewEndpointDataForCopyDialog.id = oEndpoint.id;
          oNewEndpointDataForCopyDialog.label = oEndpoint.label;
          oNewEndpointDataForCopyDialog.code = oEndpoint.code;
          aDataForSaveAsDialog.push(oNewEndpointDataForCopyDialog);
          if (CS.isNotEmpty(aProcessIds)) {
            CS.forEach(aProcessIds, (sProcessId) => {
              let oNewWFDataForCopyDialog = {};
              oNewWFDataForCopyDialog.workflowLabel = oReferencedProcesses[sProcessId].label;
              oNewWFDataForCopyDialog.workflowCode = oReferencedProcesses[sProcessId].code;
              oNewWFDataForCopyDialog.workflowId = sProcessId;
              oNewWFDataForCopyDialog.isIntegrationProcess = oEndpoint.processes.includes(sProcessId);
              aDataForWorkflowSaveAsDialog.push(oNewWFDataForCopyDialog);
            });
          }
        }
      });
      let oProcessedSaveAsDataForGrid = _preProcessSaveAsDialogForGridView(aDataForSaveAsDialog);
      let oProcessedSaveAsDataFoWorkflowGrid = _preProcessSaveAsDialogForWorkflowGridView(aDataForWorkflowSaveAsDialog);
      ProfileProps.setDataForSaveAsDialog(oProcessedSaveAsDataForGrid);
      ProfileProps.setDataForWorkflowSaveAsDialog(oProcessedSaveAsDataFoWorkflowGrid);
      ProfileProps.setIsSaveAsDialogActive(true);
    } else {
      alertify.error(getTranslation().SELECT_ONLY_ONE_ENTITY);
    }
    _triggerChange();
  };

  /** preProcess data For SaveAsDialog for Endpoint Grid View */
  let _preProcessSaveAsDialogForGridView = function (aSaveAsList) {
    let oGridSkeleton = new SaveAsDialogGridViewSkeletonForEndpoints();
    let aGridViewData = [];

    CS.forEach(aSaveAsList, function (oEndpoint) {
      let oProcessedData = {};
      oProcessedData.id = oEndpoint.id;
      oProcessedData.isExpanded = false;
      oProcessedData.children = [];
      oProcessedData.actionItemsToShow = [];
      oProcessedData.properties = {};

      CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
        oProcessedData.properties.workflowId = oEndpoint["workflowId"];
        switch (oColumn.id) {

          case "label":
            if (oEndpoint.hasOwnProperty(oColumn.id)) {
              oProcessedData.properties[oColumn.id] = {
                value: oEndpoint[oColumn.id],
                placeholder: oEndpoint.code,
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          case "code":
            if (oEndpoint.hasOwnProperty(oColumn.id)) {
              oProcessedData.properties[oColumn.id] = {
                value: oEndpoint[oColumn.id],
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          case "newLabel":
            let sLabelValue = _preprocessLabelAndCode(oEndpoint["label"]);
            oProcessedData.properties[oColumn.id] = {
              value: sLabelValue,
              placeholder: oEndpoint.code,
              isDisabled: false,
              bIsMultiLine: false
            };
            break;

          case "newCode":
            let sCodeValue = _preprocessLabelAndCode(oEndpoint["code"]);
            oProcessedData.properties[oColumn.id] = {
              value: sCodeValue,
              isDisabled: false,
              bIsMultiLine: false
            };
            break;

        }
      });
      aGridViewData.push(oProcessedData);
    });
    GridViewStore.createGridViewPropsByContext(GridViewContexts.END_POINT_COPY, {skeleton: oGridSkeleton});
    return aGridViewData;
  };

  /** preProcess data For SaveAsDialog for Workflow Grid View */
  let _preProcessSaveAsDialogForWorkflowGridView = function (aSaveAsList) {
    let oGridSkeleton = new SaveAsDialogGridViewSkeletonForEndpointsWorkflow();
    let aGridViewData = [];

    CS.forEach(aSaveAsList, function (oEndpoint) {
      let oProcessedData = {};
      oProcessedData.id = oEndpoint.workflowId;
      oProcessedData.isExpanded = false;
      oProcessedData.children = [];
      oProcessedData.actionItemsToShow = [];
      oProcessedData.properties = {};

      CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
        oProcessedData.properties.workflowId = oEndpoint["workflowId"];
        switch (oColumn.id) {

          case "label":
            if (oEndpoint.hasOwnProperty("workflowLabel")) {
              oProcessedData.properties[oColumn.id] = {
                value: oEndpoint["workflowLabel"],
                placeholder: oEndpoint.workflowCode,
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          case "code":
            if (oEndpoint.hasOwnProperty("workflowCode")) {
              oProcessedData.properties[oColumn.id] = {
                value: oEndpoint["workflowCode"],
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          case "newLabel":
            let sLabelValue = _preprocessLabelAndCode(oEndpoint["workflowLabel"]);
            oProcessedData.properties[oColumn.id] = {
              value: sLabelValue,
              placeholder: oEndpoint.code,
              isDisabled: false,
              bIsMultiLine: false
            };
            break;

          case "newCode":
            let sCodeValue = _preprocessLabelAndCode(oEndpoint["workflowCode"]);
            oProcessedData.properties[oColumn.id] = {
              value: sCodeValue,
              isDisabled: false,
              bIsMultiLine: false
            };
            break;

          case "type":
            let sWorkflowType = oEndpoint.isIntegrationProcess ? getTranslation().INTEGRATION : getTranslation().JMS;
            oProcessedData.properties[oColumn.id] = {
              value: sWorkflowType,
              isDisabled: true,
              bIsMultiLine: false
            };
            break;
        }
      });

      aGridViewData.push(oProcessedData);
    });
    GridViewStore.createGridViewPropsByContext(GridViewContexts.END_POINT_COPY_PROCESS, {skeleton: oGridSkeleton});
    return aGridViewData;
  };

  /** Add "Copy_" prefix for the respective code/label  **/
  let _preprocessLabelAndCode = function (sValue) {
    let sUpdatedValue = null ;
    if(!CS.isEmpty(sValue)){
      let aSplitedValue = sValue.split("_");
      let bIsCopyIncluded = aSplitedValue[0].includes("Copy");
      let aCopyIncluded = bIsCopyIncluded ? aSplitedValue.splice(0, 1) : aSplitedValue;
      let sContainsNumber = /\d/;
      let bIsNumberIncluded = sContainsNumber.test(aCopyIncluded[0]);
      let iCount = bIsNumberIncluded ? parseInt(aCopyIncluded[0].slice(4)) : parseInt(0);
      iCount = iCount + 1;
      sUpdatedValue = bIsCopyIncluded ? "Copy" + iCount + "_" + aSplitedValue.join("_") : "Copy" + "_" + aSplitedValue.join("_");
    }
    return sUpdatedValue;
  };

  let _handleSaveAsDialogValueChanged = function (sContentId, sPropertyId, sValue, sSubType) {
    let aGridViewData = sSubType === GridViewContexts.END_POINT_COPY ? ProfileProps.getDataForSaveAsDialog() : ProfileProps.getDataForWorkflowSaveAsDialog();
    let oGridRow = CS.find(aGridViewData, {id: sContentId});
    let oProperty = oGridRow.properties[sPropertyId];
    if (!CS.isEmpty(oProperty) && !CS.isEqual(oProperty.value, sValue)) {
      oProperty.value = sValue;
    }
    _triggerChange();
  };

  let _handleTaxonomySearchClicked = function(sContext, sTaxonomyId, sSearchText){
    ProfileProps.setAllowedTaxonomies([]);
    let oTaxonomyPaginationData = ProfileProps.getTaxonomyPaginationData();
    oTaxonomyPaginationData.from = 0;
    oTaxonomyPaginationData.searchText = sSearchText;
    _fetchTaxonomyById(sContext, sTaxonomyId);
  };

  let _handleTaxonomyLoadMoreClicked = function(sContext, sTaxonomyId){
    let oTaxonomyPaginationData = ProfileProps.getTaxonomyPaginationData();
    let aTaxonomyList = ProfileProps.getAllowedTaxonomies();
    oTaxonomyPaginationData.from = aTaxonomyList.length;
    _fetchTaxonomyById(sContext, sTaxonomyId);
  };

  let _createDashboardTab = function (oResponse) {
    let oActiveProfile = _makeActiveProfileDirty();
    let oReferencedDashboardTabs = ProfileProps.getReferencedDashboardTabs();
    oReferencedDashboardTabs[oResponse.id] = oResponse;
    oActiveProfile.dashboardTabId = oResponse.id;
    _triggerChange();
  };

  return {

    fetchEndpointListForGridView: function () {
      _fetchEndpointListForGridView();
    },

    getAllEndpointsForGridView: function () {
      _getAllEndpointsForGridView();
    },

    postProcessEndpointListAndSave: function (oCallBackData) {
      _postProcessEndpointListAndSave(oCallBackData);
    },

    fetchProfileList: function (oCallback) {
      _fetchProfileList(oCallback);
    },

    handleAttributeValueChanged: function (sKey, sVal) {
      _handleAttributeValueChanged(sKey, sVal);
    },

    createProfile: function () {
      _createProfile();
    },

    generateADMForEndpoint: function (oEndpoint, oNewEndpoint) {
      return _createADMForProfile(oEndpoint, oNewEndpoint);
    },

    deleteProfiles: function (aSelectedIds) {
      let oEndpointMap = SettingUtils.getAppData().getEndpointMap();
      CS.remove(aSelectedIds, function (sEndpointId) {
        let oMasterEndpoint = oEndpointMap[sEndpointId];
        if (sEndpointId === "admin" || oMasterEndpoint.isDefault) {
          return true;
        }
      });
      if (!CS.isEmpty(aSelectedIds)) {

        var aBulkDeleteEndpoints = [];
        CS.forEach(aSelectedIds, function (sEndpointId) {
          let sSelectedEndpointLabel = CS.getLabelOrCode(oEndpointMap[sEndpointId]);
          aBulkDeleteEndpoints.push(sSelectedEndpointLabel);
        });

        CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteEndpoints,
            function () {
              _deleteProfiles(aSelectedIds)
              .then(_fetchEndpointListForGridView);
            }, function (oEvent) {
            });
      } else {
        alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      }
    },

    editButtonClicked: function (sEntityId) {
      _editButtonClicked(sEntityId);
    },

    handleMappingSummaryViewAddNewMappingRow: function (sSummaryType) {
      _handleProfileAddNewMappingRow(sSummaryType);
    },

    handleMappingSummaryViewColumnNameChanged: function (sId, sName, sSummaryType) {
      _handleColumnNameChanged(sId, sName, sSummaryType);
    },

    handleAddTagValueClicked: function (sId, sMappedElementId) {
      _handleAddTagValueClicked(sId, sMappedElementId);
    },

    handleMappingSummaryViewConfigTagValueChanged: function (sId, sMappedTagValueId, sNewValue) {
      _handleProfileConfigTagValueChanged(sId, sMappedTagValueId, sNewValue);
    },

    handleMappingSummaryViewConfigTagValueIgnoreCaseToggled: function (sId, sMappedTagValueId) {
      _handleProfileConfigTagValueIgnoreCaseToggled(sId, sMappedTagValueId);
    },

    handleMappingSummaryViewConfigMappedTagValueChanged: function (sId, sMappedTagValueId, sMappedElementId) {
      _handleProfileConfigMappedTagValueChanged(sId, sMappedTagValueId, sMappedElementId);
    },

    handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked: function (sId, sMappedTagValueId) {
      _handleProfileConfigMappedTagValueRowDeleteClicked(sId, sMappedTagValueId);
    },

    handleMappingSummaryViewMappedElementChanged: function (sId, sMappedElementId, sSummaryType) {
      _handleProfileMappedElementChanged(sId, sMappedElementId, sSummaryType);
    },

    handleMappingSummaryViewMappingRowSelected: function (sId) {
      _handleProfileMappingRowSelected(sId);
    },

    handleMappingSummaryViewIsIgnoredToggled: function (sId, sSummaryType) {
      _handleProfileIsIgnoredToggled(sId, sSummaryType);
    },

    handleMappingSummaryViewMappingRowDeleted: function (sId, sSummaryType) {
      _handleProfileMappingRowDeleted(sId, sSummaryType);
    },

    handleSelectionToggleButtonClicked: function (sKey, sId, bSingleSelect){
      _handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
    },

    handleProfileConfigFieldValueChanged: function (sKey, aSelectedItems) {
      let oActiveProfile = _makeActiveProfileDirty();
      let sValue = aSelectedItems[0];
      switch (sKey) {
        case "endpointType" :
        case "dashboardTabId" :
          oActiveProfile[sKey] = sValue;
          break;

        case "label":
        case "code":
        case "processes" :
        case "jmsProcesses":
        case "mappings" :
        case "physicalCatalogs" :
        case "isRuntimeMappingEnabled" :
          oActiveProfile[sKey] = aSelectedItems;
          break;
        case "dashboardTab" :
          oActiveProfile["dashboardTabId"] = sValue;
          break;
        case "authorizationMappings" :
          oActiveProfile["authorizationMapping"] = aSelectedItems;
          break;
      }
      _triggerChange();
    },

    handleProfileConfigDialogButtonClicked: function (sButtonId) {
      if (sButtonId == "endpoint_creation_create") {
        _createProfile(true);
      } else {
        _cancelProfileCreation();
      }
    },

    handleProfileConfigDataSelectionToggled : function (sKey, sValue) {
      var oActiveProfile = _makeActiveProfileDirty();
      var aSelectedItems = oActiveProfile[sKey] || [];
      var aRemovedIds = CS.remove(aSelectedItems, function (sRuleId) {
        return sRuleId === sValue;
      });

      if(CS.isEmpty(aRemovedIds)) {
        aSelectedItems.push(sValue);
      }

      oActiveProfile[sKey] = aSelectedItems;

      _triggerChange()
    },

    handleSystemsMSSSearchClicked: function (sSearchText, sEndpointId) {
      _handleSystemsMSSSearchClicked(sSearchText, sEndpointId);
    },

    handleSystemsMSSLoadMoreClicked: function (sEndpointId) {
      _handleSystemsMSSLoadMoreClicked(sEndpointId);
    },

    handleSystemsMSSApplyClicked: function (aSelectedItems) {
      _handleSystemsMSSApplyClicked(aSelectedItems);
    },

    handleSystemsMSSCreateClicked: function (sSearchText, sEndpointId) {
      _createSystem(sSearchText, sEndpointId);
    },

    loadSystemList: function (sEndpointId) {
      _loadSystemList(sEndpointId);
    },

    discardEndpointGridViewChanges: function (oCallbackData) {
      _discardEndpointGridViewChanges(oCallbackData);
    },

    discardUnsavedEndpoint: function (oElement) {
      _discardUnsavedEndpoint(oElement);
    },

    saveEndpoint: function (oElement) {
      _saveEndpoint(oElement);
    },

    closeEndpointDialog: function () {
      _closeEndpointDialog();
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId) {
      ProfileProps.setAllowedTaxonomies([]);
      let oTaxonomyPaginationData = ProfileProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.searchText = "";
      oTaxonomyPaginationData.from = 0;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleTaxonomyAdded: function (oModel, sParentTaxonomyId, sViewContext) {
      _handleTaxonomyAdded(oModel, sParentTaxonomyId, sViewContext);
    },

    handleLazyMSSValueRemoved: function (sOldContextKey,sId) {
      _handleLazyMSSValueRemoved(sOldContextKey,sId);
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sParentTaxonomyId, sViewContext) {
      _handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sViewContext);
    },

    handleSaveAsDialogCloseButtonClicked: function () {
      _handleSaveAsDialogCloseButtonClicked()
    },

    handleSaveAsDialogSaveButtonClicked: function () {
      _handleSaveAsDialogSaveButtonClicked();
      _triggerChange();
    },

    saveAsEndpoint: function () {
      _saveAsEndpoint();
    },

    handleSaveAsDialogValueChanged: function (sContentId, sPropertyId, sValue, sSubType) {
      _handleSaveAsDialogValueChanged(sContentId, sPropertyId, sValue, sSubType);
    },

    handleTaxonomySearchClicked: function (sContext, sTaxonomyId, sSearchText) {
      _handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
    },

    handleTaxonomyLoadMoreClicked: function (sContext, sTaxonomyId) {
      _handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
    },

    createDashboardTab : function (oResponse) {
      _createDashboardTab(oResponse)
    },

    reset: function () {
      SettingUtils.getAppData().setProfileList([]);
      ProfileProps.reset();
    }

  }
})();

MicroEvent.mixin(ProfileStore);
export default ProfileStore;
