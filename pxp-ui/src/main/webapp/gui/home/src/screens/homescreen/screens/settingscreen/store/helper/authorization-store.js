import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import AuthorizationGridViewSkeleton from '../../tack/authorization-config-grid-view-skeleton';
import AuthorizationConfigViewProps from '../../store/model/authorization-config-view-props';
import SettingUtils from './../helper/setting-utils';
import { AuthorizationRequestMapping } from '../../tack/setting-screen-request-mapping';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import MockDataForMapSummaryHeader from '../../tack/mock/mock-data-for-map-summery-header';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import CommonUtils from "../../../../../../commonmodule/util/common-utils";

var AuthorizationStore = (function () {

  let _triggerChange = function () {
    AuthorizationStore.trigger('authorization-changed');
  };

  let _makeActiveAuthorizationMappingDirty = function () {
    let oActiveMapping = AuthorizationConfigViewProps.getSelectedMapping();
    if (!oActiveMapping.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveMapping);
    }
    return oActiveMapping.clonedObject;
  };

  /** To setup the data needed to be displayed on GridView **/
  let _setUpAuthorizationConfigGridView = function () {
    let oAuthorizationConfigGridViewSkeleton = new AuthorizationGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.AUTHORIZATION_MAPPING, {skeleton: oAuthorizationConfigGridViewSkeleton});
    _fetchAuthorizationListForGridView();
  };

  let _fetchAuthorizationListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.AUTHORIZATION_MAPPING);
    SettingUtils.csPostRequest(AuthorizationRequestMapping.GetAllAuthorizationMappings, {}, oPostData, successFetchAuthorizationMappingListCallback, failureFetchAuthorizationMappingListCallback);
  };

  /**Server Callbacks **/
  let successFetchAuthorizationMappingListCallback = function (oResponse) {
    let aAuthorizationMappingDataList = oResponse.success.list;
    GridViewStore.preProcessDataForGridView(GridViewContexts.AUTHORIZATION_MAPPING, aAuthorizationMappingDataList, oResponse.success.count);
    SettingUtils.getAppData().setAuthorizationMappingList(aAuthorizationMappingDataList);
    AuthorizationConfigViewProps.setAuthorizationMappingGridData(aAuthorizationMappingDataList);

    _triggerChange();
  };

  let failureFetchAuthorizationMappingListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAuthorizationMappingListCallback", getTranslation());
  };

  let _createAuthorizationMapping = function () {
    let oNewAuthorizationMappingToCreate = AuthorizationConfigViewProps.getSelectedMapping();
    oNewAuthorizationMappingToCreate = oNewAuthorizationMappingToCreate.clonedObject || oNewAuthorizationMappingToCreate;
    if (CS.isEmpty(oNewAuthorizationMappingToCreate.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    let oCodeToVerifyUniqueness = {
      id: oNewAuthorizationMappingToCreate.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_AUTHORIZATION
    };

    let oSuccessCreateCallback = {};
    oSuccessCreateCallback.isCreated = true;
    oSuccessCreateCallback.functionToExecute = function () {
      alertify.success(getTranslation().AUTHORIZATION_MAPPING_CREATED_SUCCESSFULLY);
    };

    let oCallbackData = {};
    oCallbackData.functionToExecute = _createAuthorizationMappingCall.bind(this, oNewAuthorizationMappingToCreate, oSuccessCreateCallback);
    let sURL = AuthorizationRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackData);
  };

  let _createAuthorizationMappingCall = function (oNewAuthorizationMappingToCreate, oSuccessCreateCallback) {
    SettingUtils.csPutRequest(AuthorizationRequestMapping.CreateAuthorizationMapping, {}, oNewAuthorizationMappingToCreate, successGetAuthorizationMappingCallback.bind(this, oSuccessCreateCallback), failureGetAuthorizationMappingCallback);
  };

  let successGetAuthorizationMappingCallback = function (oCallback, oResponse) {
    let oAuthorizationMappingFromServer = oResponse.success;
    let oEntity = oAuthorizationMappingFromServer.entity;
    let oConfigDetails = oAuthorizationMappingFromServer.configDetails;

    let aAuthorizationMappingGridViewData = AuthorizationConfigViewProps.getAuthorizationMappingGridData();
    let iIndex = CS.findIndex(aAuthorizationMappingGridViewData, {id: oAuthorizationMappingFromServer.entity.id});
    if (iIndex != -1) {
      aAuthorizationMappingGridViewData[iIndex] = oAuthorizationMappingFromServer.entity;
    } else {
      aAuthorizationMappingGridViewData.push(oAuthorizationMappingFromServer.entity);
    }

    let sContext = GridViewContexts.AUTHORIZATION_MAPPING;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(sContext);
    if (oCallback.isCreated) {
      let oProcessedGridViewData = GridViewStore.getProcessedGridViewData(sContext, [oEntity])[0];
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
      aGridViewData.unshift(oProcessedGridViewData);
      oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);
      let oAuthorizationMappingMap = SettingUtils.getAppData().getAuthorizationMappingList();
      oAuthorizationMappingMap[oEntity.id] = oEntity;
    } else {
      let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(sContext, aAuthorizationMappingGridViewData);
      oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);
    }
    _setSelectedMapping(oEntity);
    _updateReferencedDataForAuthorizationMapping(oConfigDetails);
    _setBooleanEntities(oEntity);

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    AuthorizationConfigViewProps.setIsAuthorizationMappingDialogActive(true);
    AuthorizationConfigViewProps.setSelectedId(MockDataForMapSummaryHeader.class);
    _triggerChange();
  };

  let failureGetAuthorizationMappingCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureGetAuthorizationMappingCallback', getTranslation());
  };

  let _updateReferencedDataForAuthorizationMapping = (oConfigDetails) => {
    if (CS.isEmpty(oConfigDetails)) {
      return;
    }

    let oConfigData = AuthorizationConfigViewProps.getConfigData();
    oConfigData.referencedAttributes = oConfigDetails.referencedAttributes;
    oConfigData.referencedTags = oConfigDetails.referencedTags;
    oConfigData.referencedKlasses = oConfigDetails.referencedKlasses;
    oConfigData.referencedTaxonomies = oConfigDetails.referencedTaxonomies;
    oConfigData.referencedContexts = oConfigDetails.referencedContexts;
    oConfigData.referencedRelationships = oConfigDetails.referencedRelationships;
  };

  let _createDefaultAuthorizationMappingObject = function () {
    let sId = UniqueIdentifierGenerator.generateUUID();

    let oNewAuthorizationMappingToCreate = {};
    oNewAuthorizationMappingToCreate.id = sId;
    oNewAuthorizationMappingToCreate.label = UniqueIdentifierGenerator.generateUntitledName();
    oNewAuthorizationMappingToCreate.isDefault = false;
    oNewAuthorizationMappingToCreate.code = "";
    oNewAuthorizationMappingToCreate.isCreated = true;

    let oMasterAuthorizationMappingObject = SettingUtils.getAppData().getAuthorizationMappingList();
    oMasterAuthorizationMappingObject[sId] = oNewAuthorizationMappingToCreate;

    _setSelectedMapping(oNewAuthorizationMappingToCreate);
    SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);
    _triggerChange();
  };

  let _setSelectedMapping = function (oSelectedMapping) {
    AuthorizationConfigViewProps.setSelectedMapping(oSelectedMapping);
  };

  let _setBooleanEntities = function (oEntity) {
    let oCheckboxDetails = AuthorizationConfigViewProps.getCheckboxClickedDetails();
    let oToggleSelectionDetails = AuthorizationConfigViewProps.getToggleSelectionClickedDetails();

    oCheckboxDetails.attributes = oEntity.isAllAttributesSelected;
    oCheckboxDetails.tags = oEntity.isAllTagsSelected;
    oCheckboxDetails.classes = oEntity.isAllClassesSelected;
    oCheckboxDetails.taxonomies = oEntity.isAllTaxonomiesSelected;
    oCheckboxDetails.contexts = oEntity.isAllContextsSelected;
    oCheckboxDetails.relationships = oEntity.isAllRelationshipsSelected;

    oToggleSelectionDetails.attributes = oEntity.isBlankValueAcceptedForAttributes;
    oToggleSelectionDetails.tags = oEntity.isBlankValueAcceptedForTags;
    oToggleSelectionDetails.classes = oEntity.isBlankValueAcceptedForClasses;
    oToggleSelectionDetails.taxonomies = oEntity.isBlankValueAcceptedForTaxonomies;
    oToggleSelectionDetails.contexts = oEntity.isBlankValueAcceptedForContexts;
    oToggleSelectionDetails.relationships = oEntity.isBlankValueAcceptedForRelationships;

    AuthorizationConfigViewProps.setCheckboxClickedDetails(oCheckboxDetails);
    AuthorizationConfigViewProps.setToggleSelectionClickedDetails(oToggleSelectionDetails);
  };

  let _getSelectedMapping = function () {
    return AuthorizationConfigViewProps.getSelectedMapping();
  };

  let _handleAuthorizationMappingDialogButtonClicked = function (sButtonId) {
    if (sButtonId === "create") {
      AuthorizationStore.createAuthorizationMapping();
    } else {
      AuthorizationStore.cancelAuthorizationMappingCreation();
    }
  };

  let _cancelAuthorizationMappingCreation = function () {
    let oMasterMappingObject = SettingUtils.getAppData().getAuthorizationMappingList();
    let oNewMappingToCreate = _getSelectedMapping();

    delete oMasterMappingObject[oNewMappingToCreate.id];
    _setSelectedMapping({});
    _triggerChange();
  };

  let _handleAuthorizationMappingConfigDialogButtonClicked = function (sButtonId) {
    if (sButtonId == "save") {
      _saveAuthorizationMapping({});
    } else if (sButtonId == "cancel") {
      _discardUnsavedAuthorizationMapping({});
    } else {
      _closeAuthorizationMappingDialog();
    }
  };

  let _saveAuthorizationMapping = function (oCallbackData) {
    let oActiveMapping = _getSelectedMapping();
    let oClonedMapping = oActiveMapping.clonedObject;
    if (!oClonedMapping.label) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    if (!oActiveMapping.isDirty) {
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }

    let oAuthorizationMappingToSave = _createADMForMapping(oActiveMapping);

    let oServerCallback = {};
    oServerCallback.functionToExecute = function () {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().AUTHORIZATION_MAPPING}));
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    };

    SettingUtils.csPostRequest(AuthorizationRequestMapping.SaveAuthorizationMapping, {}, oAuthorizationMappingToSave, successSaveAuthorizationMappingCallback.bind(this, oServerCallback), failureSaveAuthorizationMappingCallback.bind(this, oServerCallback));
    _triggerChange();
  };

  let successSaveAuthorizationMappingCallback = function (oCallback, oResponse) {
    let oAuthorizationMappingFromServer = oResponse.success;
    let oConfigDetails = oAuthorizationMappingFromServer.configDetails;
    let oEntityDetails = oAuthorizationMappingFromServer.entity;

    let aAuthorizationMappingGridViewData = AuthorizationConfigViewProps.getAuthorizationMappingGridData();
    let iIndex = CS.findIndex(aAuthorizationMappingGridViewData, {id: oAuthorizationMappingFromServer.entity.id});
    if (iIndex != -1) {
      aAuthorizationMappingGridViewData[iIndex] = oAuthorizationMappingFromServer.entity;
    } else {
      aAuthorizationMappingGridViewData.push(oAuthorizationMappingFromServer.entity);
    }

    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUTHORIZATION_MAPPING);
    if (oCallback.isCreated) {
      let oProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.AUTHORIZATION_MAPPING, [oEntityDetails])[0];
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
      aGridViewData.unshift(oProcessedGridViewData);
      oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);
    } else {
      let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.AUTHORIZATION_MAPPING,
          aAuthorizationMappingGridViewData);
      oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);
    }
    _setSelectedMapping(oEntityDetails);
    _updateReferencedDataForAuthorizationMapping(oConfigDetails);

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    if (oCallback.isCreated) {
      /** To open edit view immediately after create */
      AuthorizationConfigViewProps.setIsAuthorizationMappingDialogActive(true);
    }
    _triggerChange();
  };

  let failureSaveAuthorizationMappingCallback = function (oCallbackData, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveAuthorizationMappingCallback", getTranslation());
  };

  let _createADMForMapping = function (oActiveMapping) {
    let oClonedMapping = oActiveMapping.clonedObject;
    let oMappingToSave = CS.cloneDeep(oClonedMapping);

    let aKeysForSelectedElements = ["attributeMappings", "tagMappings", "classMappings", "contextMappings", "relationshipMappings", "taxonomyMappings"];
    CS.forEach(aKeysForSelectedElements, function (sKey) {
      _fillSelectedElementADMByKey(oMappingToSave, sKey, oActiveMapping, oClonedMapping);
    })

    let oCheckboxDetails = AuthorizationConfigViewProps.getCheckboxClickedDetails();
    _setCheckboxDetails(oMappingToSave, oCheckboxDetails);

    let oToggleSelectionDetails = AuthorizationConfigViewProps.getToggleSelectionClickedDetails();
    _setToggleSelectionDetails(oMappingToSave, oToggleSelectionDetails);

    _removeUnwantedData(oMappingToSave, aKeysForSelectedElements);

    return oMappingToSave;
  };

  let _removeUnwantedData = function (oMappingToSave, aKeysForSelectedElements) {
    CS.forEach(aKeysForSelectedElements, function (sKey) {
      delete oMappingToSave[sKey];
    })

    delete oMappingToSave.configDetails;
  };

  let _fillSelectedElementADMByKey = function (oMappingToSave, sKey, oOldData, oNewData) {
    let aNewElements = oNewData[sKey] || [];
    let aOldElements = oOldData[sKey] || [];

    let sUpdatedKey = CS.upperFirst(sKey);
    oMappingToSave[`added${sUpdatedKey}`] = CS.difference(aNewElements, aOldElements);
    oMappingToSave[`deleted${sUpdatedKey}`] = CS.difference(aOldElements, aNewElements);
  };

  let _fillSelectedElement = function (oMappingToSave, oCheckboxDetails, sKey) {
    let sUpdatedKey = CS.capitalize(sKey);
    oMappingToSave[`isAll${sUpdatedKey}Selected`] = oCheckboxDetails[sKey];
  };

  let _fillSelectedElementForToggleSelection = function (oMappingToSave, oToggleSelectionDetails, sKey) {
    let sUpdatedKey = CS.capitalize(sKey);
    oMappingToSave[`isBlankValueAcceptedFor${sUpdatedKey}`] = oToggleSelectionDetails[sKey];
  };

  let _setCheckboxDetails = function (oMappingToSave, oCheckboxDetails) {
    let aKeys = ["attributes", "tags", "classes", "contexts", "relationships", "taxonomies"];
    CS.forEach(aKeys, function (sKey) {
      _fillSelectedElement(oMappingToSave, oCheckboxDetails, sKey)
    })
  };

  let _setToggleSelectionDetails = function (oMappingToSave, oToggleSelectionDetails) {
    let aKeys = ["attributes", "tags", "classes", "contexts", "relationships", "taxonomies"];
    CS.forEach(aKeys, function (sKey) {
      _fillSelectedElementForToggleSelection(oMappingToSave, oToggleSelectionDetails, sKey)
    })
  };

  let _closeAuthorizationMappingDialog = function () {
    AuthorizationConfigViewProps.setIsAuthorizationMappingDialogActive(false);
    _triggerChange();
  };

  let _discardUnsavedAuthorizationMapping = function (oCallbackData) {
    let oSelectedMapping = _getSelectedMapping();
    let oMasterAuthorizationMappingList = SettingUtils.getAppData().getAuthorizationMappingList();
    let oCheckboxDetails = AuthorizationConfigViewProps.getCheckboxClickedDetails();
    let oToggleSelectionClickedDetails = AuthorizationConfigViewProps.getToggleSelectionClickedDetails();

    if (!CS.isEmpty(oSelectedMapping)) {
      if (oSelectedMapping.isCreated) {

        delete oMasterAuthorizationMappingList[oSelectedMapping.id];
        _setSelectedMapping({});
        alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
      } else if (oSelectedMapping.isDirty) {
        _clearCheckBoxElements(oCheckboxDetails,oSelectedMapping);
        _clearToggleSelectionClickedDetails(oToggleSelectionClickedDetails,oSelectedMapping);
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
  };

  let _clearCheckBoxElements = function(oCheckboxDetails,oSelectedMapping){
    oCheckboxDetails.attributes = oSelectedMapping.isAllAttributesSelected;
    oCheckboxDetails.classes = oSelectedMapping.isAllClassesSelected;
    oCheckboxDetails.contexts = oSelectedMapping.isAllContextsSelected;
    oCheckboxDetails.relationships = oSelectedMapping.isAllRelationshipsSelected;
    oCheckboxDetails.tags = oSelectedMapping.isAllTagsSelected;
    oCheckboxDetails.taxonomies = oSelectedMapping.isAllTaxonomiesSelected;
  };

  let _clearToggleSelectionClickedDetails = function(oToggleSelectionClickedDetails,oSelectedMapping){
    oToggleSelectionClickedDetails.attributes = oSelectedMapping.isBlankValueAcceptedForAttributes;
    oToggleSelectionClickedDetails.classes = oSelectedMapping.isBlankValueAcceptedForClasses;
    oToggleSelectionClickedDetails.contexts = oSelectedMapping.isBlankValueAcceptedForContexts;
    oToggleSelectionClickedDetails.relationships = oSelectedMapping.isBlankValueAcceptedForRelationships;
    oToggleSelectionClickedDetails.tags = oSelectedMapping.isBlankValueAcceptedForTags;
    oToggleSelectionClickedDetails.taxonomies = oSelectedMapping.isBlankValueAcceptedForTaxonomies;
  };

  let _deleteAuthorizationMapping = function (aSelectedIds) {
    let aAuthorizationMappingIdsToDelete = aSelectedIds;
    let oMasterAuthorizationMappingList = SettingUtils.getAppData().getAuthorizationMappingList();
    CS.remove(aAuthorizationMappingIdsToDelete, function (sMappingId) {
      let oMasterAuthorizationMapping = oMasterAuthorizationMappingList[sMappingId];
      if (sMappingId === "admin" || oMasterAuthorizationMapping.isDefault) {
        return true;
      }
    });
    if (!CS.isEmpty(aAuthorizationMappingIdsToDelete)) {

      let aBulkDeleteAuthorizationMappings = [];
      CS.forEach(aAuthorizationMappingIdsToDelete, function (sMappingId) {
        aBulkDeleteAuthorizationMappings.push(oMasterAuthorizationMappingList[sMappingId].label);
      });

      //var sBulkDeleteMappingLastNames = aBulkDeleteMappings.join(', ');
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteAuthorizationMappings,
          function () {
            _deleteMappings(aAuthorizationMappingIdsToDelete)
            .then(_fetchAuthorizationListForGridView);
          }, function (oEvent) {
          });
    } else {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
    }
  };

  let _deleteMappings = function (aBulkDeleteList) {
    let aFilteredMappingIds = _deleteUnSavedMappings(aBulkDeleteList);
    if (!CS.isEmpty(aFilteredMappingIds)) {
      return SettingUtils.csDeleteRequest(AuthorizationRequestMapping.DeleteAuthorizationMapping, {}, {ids: aFilteredMappingIds}, successDeleteAuthorizationMappingsCallback, failureDeleteAuthorizationMappingsCallback);
    } else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().USERS}));
      _triggerChange();
    }
  };

  let successDeleteAuthorizationMappingsCallback = function (oResponse) {
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUTHORIZATION_MAPPING);
    let oMasterMappingList = SettingUtils.getAppData().getAuthorizationMappingList();
    let aSuccessIds = oResponse.success;
    let aGridViewData = oGridViewProps.getGridViewData();
    let oGridViewSkeleton = oGridViewProps.getGridViewSkeleton();
    _handleBeforeDeleteMappings(oResponse.success);
    CS.forEach(oResponse.success, function (id) {
      CS.remove(aGridViewData, {id: id});
      CS.pull(oGridViewSkeleton.selectedContentIds, id);
      delete oMasterMappingList[id];
    });

    let iCount = oGridViewProps.getGridViewTotalItems() - aSuccessIds.length;
     GridViewStore.preProcessDataForGridView(GridViewContexts.AUTHORIZATION_MAPPING, oMasterMappingList, iCount);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().AUTHORIZATION_MAPPING}));

    if (oResponse.failure) {
      let oSelectedMapping = _getSelectedMapping();
      handleDeleteMappingFailure(oResponse.failure.exceptionDetails, oMasterMappingList, oSelectedMapping);
    }
  };

  let failureDeleteAuthorizationMappingsCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      let oMasterMappingList = SettingUtils.getAppData().getAuthorizationMappingList();
      let oSelectedMapping = _getSelectedMapping();
      handleDeleteMappingFailure(oResponse.failure.exceptionDetails, oMasterMappingList, oSelectedMapping);
    } else {
      SettingUtils.failureCallback(oResponse, "failureDeleteAuthorizationMappingsCallback", getTranslation());
    }
    _triggerChange();
  };

  let _handleBeforeDeleteMappings = function (aMappingIds) {
    let sSelectedMappingId = _getSelectedMapping().id;
    if (CS.indexOf(aMappingIds, sSelectedMappingId) >= 0) {
      _setSelectedMapping({});
    }
  };

  let handleDeleteMappingFailure = function (List, oMasterMappingList, oSelectedMapping) {
    let aAlreadyDeletedMappings = [];
    let aUnhandledMappings = [];
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
      let sMappingAlreadyDeleted = aAlreadyDeletedMappings.join(',');
      alertify.error(Exception.getCustomMessage("Mapping_already_deleted", getTranslation(), sMappingAlreadyDeleted), 0);
    }
    if (aUnhandledMappings.length > 0) {
      let sUnhandledMapping = aUnhandledMappings.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_Mapping", getTranslation(), sUnhandledMapping), 0);
    }
  };

  let _deleteUnSavedMappings = function (aList) {
    let oMasterList = SettingUtils.getAppData().getAuthorizationMappingList();
    let aSavedAuthorizationMappingIds = [];
    let aDeletedAuthorizationMappingIds = [];

    CS.forEach(aList, function (uid) {
      if (oMasterList[uid].isCreated) {
        aDeletedAuthorizationMappingIds.push(uid);
        delete oMasterList[uid];
      } //else {
      aSavedAuthorizationMappingIds.push(uid);
      //}
    });

    let sSelectedMappingId = _getSelectedMapping().id;
    if (CS.indexOf(aDeletedAuthorizationMappingIds, sSelectedMappingId) >= 0) {
      _setSelectedMapping({});
    }

    return aSavedAuthorizationMappingIds;
  };

  let _handleTabLayoutTabChanged = function (sTabId) {
    AuthorizationConfigViewProps.setSelectedId(sTabId);
    _triggerChange();
  };

  let _handleAuthorizationMappingApplyButtonClicked = function (sContext, aSelectedItems, oReferencedData) {
    let oActiveAuthorizationMapping = _makeActiveAuthorizationMappingDirty();
    let oConfigDetails = AuthorizationConfigViewProps.getConfigData();
    switch (sContext) {

      case "attributes":
        aSelectedItems = CS.union(oActiveAuthorizationMapping.attributeMappings,aSelectedItems);
        oActiveAuthorizationMapping.attributeMappings = aSelectedItems;
        oReferencedData = CS.assign(oConfigDetails.referencedAttributes,oReferencedData);
        oConfigDetails.referencedAttributes = oReferencedData;
        break;

      case "tags":
        aSelectedItems = CS.union(oActiveAuthorizationMapping.tagMappings,aSelectedItems);
        oActiveAuthorizationMapping.tagMappings = aSelectedItems;
        oReferencedData = CS.assign(oConfigDetails.referencedTags,oReferencedData);
        oConfigDetails.referencedTags = oReferencedData;
        break;

      case "taxonomies":
        aSelectedItems = CS.union(oActiveAuthorizationMapping.taxonomyMappings,aSelectedItems);
        oActiveAuthorizationMapping.taxonomyMappings = aSelectedItems;
        oReferencedData = CS.assign(oConfigDetails.referencedTaxonomies,oReferencedData);
        oConfigDetails.referencedTaxonomies = oReferencedData;
        break;

      case "classes":
        aSelectedItems = CS.union(oActiveAuthorizationMapping.classMappings,aSelectedItems);
        oActiveAuthorizationMapping.classMappings = aSelectedItems;
        oReferencedData = CS.assign(oConfigDetails.referencedKlasses,oReferencedData);
        oConfigDetails.referencedKlasses = oReferencedData;
        break;

      case "contexts":
        aSelectedItems = CS.union(oActiveAuthorizationMapping.contextMappings,aSelectedItems);
        oActiveAuthorizationMapping.contextMappings = aSelectedItems;
        oReferencedData = CS.assign(oConfigDetails.referencedContexts,oReferencedData);
        oConfigDetails.referencedContexts = oReferencedData;
        break;

      case "relationships":
        aSelectedItems = CS.union(oActiveAuthorizationMapping.relationshipMappings,aSelectedItems);
        oActiveAuthorizationMapping.relationshipMappings = aSelectedItems;
        oReferencedData = CS.assign(oConfigDetails.referencedRelationships,oReferencedData);
        oConfigDetails.referencedRelationships = oReferencedData;
        break;
    }
    // AuthorizationConfigViewProps.setSelectedMapping(oActiveAuthorizationMapping);

    AuthorizationConfigViewProps.setConfigData(oConfigDetails);

    _triggerChange();
  };

  let selectAllCheckboxClicked = function (sContext, oCheckboxClicked) {
    let oActiveAuthorizationMapping = _makeActiveAuthorizationMappingDirty();

    switch (sContext) {

      case "attributes":
        oActiveAuthorizationMapping.attributeMappings = [];
        break;

      case "tags":
        oActiveAuthorizationMapping.tagMappings = [];
        break;

      case "taxonomies":
        oActiveAuthorizationMapping.taxonomyMappings = [];
        break;

      case "classes":
        oActiveAuthorizationMapping.classMappings = [];
        break;

      case "contexts":
        oActiveAuthorizationMapping.contextMappings = [];
        break;

      case "relationships":
        oActiveAuthorizationMapping.relationshipMappings = [];
        break;
    }
    // AuthorizationConfigViewProps.setSelectedMapping(oActiveAuthorizationMapping);
    _triggerChange();
  };

  let _handleAuthorizationMappingCheckboxButtonClicked = function (bIsCheckboxClicked, sContext) {
    let oActiveAuthorizationMapping = _makeActiveAuthorizationMappingDirty();
    let oCheckboxClicked = AuthorizationConfigViewProps.getCheckboxClickedDetails();
    oCheckboxClicked[sContext] = !bIsCheckboxClicked;
    AuthorizationConfigViewProps.setCheckboxClickedDetails(oCheckboxClicked);
    selectAllCheckboxClicked(sContext, oCheckboxClicked);
    _triggerChange();
  };

  let _handleAuthorizationMappingDeleteButtonClicked = function (sSelectedElement, sContext) {
    let oActiveAuthorizationMapping = _makeActiveAuthorizationMappingDirty();
    let iIndex = 0;
    switch (sContext) {

      case "attributes":
        iIndex = oActiveAuthorizationMapping.attributeMappings.indexOf(sSelectedElement);
        oActiveAuthorizationMapping.attributeMappings.splice(iIndex, 1);
        break;

      case "tags":
        iIndex = oActiveAuthorizationMapping.tagMappings.indexOf(sSelectedElement);
        oActiveAuthorizationMapping.tagMappings.splice(iIndex, 1);
        break;

      case "taxonomies":
        iIndex = oActiveAuthorizationMapping.taxonomyMappings.indexOf(sSelectedElement);
        oActiveAuthorizationMapping.taxonomyMappings.splice(iIndex, 1);
        break;

      case "classes":
        iIndex = oActiveAuthorizationMapping.classMappings.indexOf(sSelectedElement);
        oActiveAuthorizationMapping.classMappings.splice(iIndex, 1);
        break;

      case "contexts":
        iIndex = oActiveAuthorizationMapping.contextMappings.indexOf(sSelectedElement);
        oActiveAuthorizationMapping.contextMappings.splice(iIndex, 1);
        break;

      case "relationships":
        iIndex = oActiveAuthorizationMapping.relationshipMappings.indexOf(sSelectedElement);
        oActiveAuthorizationMapping.relationshipMappings.splice(iIndex, 1);
        break;
    }

    _triggerChange();
  };

  let _getAuthorizationMappingDetails = function (sSelectedMappingId, oCallBackData) {
    if (CS.isEmpty(oCallBackData)) {
      oCallBackData = {};
    }
    SettingUtils.csGetRequest(AuthorizationRequestMapping.GetAuthorizationMapping, {id: sSelectedMappingId}, successGetAuthorizationMappingCallback.bind(this, oCallBackData), failureGetAuthorizationMappingCallback);
  };

  let _editButtonClicked = (sEntityId) => {
    AuthorizationConfigViewProps.setIsAuthorizationMappingDialogActive(true);
    _getAuthorizationMappingDetails(sEntityId);
  };

  let _handleAuthorizationMappingToggleButtonClicked = function (bIsActivationEnable, oElementData) {
    let oActiveAuthorizationMapping = _makeActiveAuthorizationMappingDirty();
    let sContext = oElementData.summaryType;
    let oToggleSelectionDetails = AuthorizationConfigViewProps.getToggleSelectionClickedDetails();
    oToggleSelectionDetails[sContext] = !bIsActivationEnable;
    _triggerChange();
  };

  let _processProcessAuthorizationMappingListAndSave = function (oCallBackData) {
    let aAuthorizationMappingToSave = [];
    let bSafeToSave = GridViewStore.processGridDataToSave(aAuthorizationMappingToSave, GridViewContexts.AUTHORIZATION_MAPPING, AuthorizationConfigViewProps.getAuthorizationMappingGridData());

    if (bSafeToSave) {
      return SettingUtils.csPostRequest(AuthorizationRequestMapping.BulkSaveAuthorizationMapping, {}, aAuthorizationMappingToSave, successBulkSaveAuthorizationMappingList.bind(this, oCallBackData),
          failureBulkSaveAuthorizationMappingList);
    }
    return CommonUtils.rejectEmptyPromise();
  };

  let successBulkSaveAuthorizationMappingList = function (oCallBackData, oResponse) {
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUTHORIZATION_MAPPING);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let aAuthorizationMappingList = oResponse.success;
    let aAuthorizationMappingGridData = AuthorizationConfigViewProps.getAuthorizationMappingGridData();
    let aAuthorizationMappingListMap = SettingUtils.getAppData().getAuthorizationMappingList();
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.AUTHORIZATION_MAPPING,
        aAuthorizationMappingList);
    CS.forEach(aGridViewData, function (oData) {
      oData.isDirty = false;
    });

    CS.forEach(aAuthorizationMappingList, function (oAuthorizationMapping) {
      let authorizationMappingId = oAuthorizationMapping.id;
      let iIndex = CS.findIndex(aAuthorizationMappingGridData, {id: authorizationMappingId});
      aAuthorizationMappingGridData[iIndex] = oAuthorizationMapping;
      aAuthorizationMappingListMap[authorizationMappingId] = oAuthorizationMapping;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedMapping) {
      let iIndex = CS.findIndex(aGridViewData, {id: oProcessedMapping.id});
      aGridViewData[iIndex] = oProcessedMapping;
    });

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().AUTHORIZATION_MAPPING}));
    oGridViewPropsByContext.setIsGridDataDirty(false);
    //oCallBackData && oCallBackData.functionToExecute && oCallBackData.functionToExecute();
  };

  let failureBulkSaveAuthorizationMappingList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureBulkSaveAuthorizationMappingList", getTranslation());
  };

  let _discardAuthorizationMappingListChanges = function (oCallbackData) {

    let aAuthorizationMappingGridData = AuthorizationConfigViewProps.getAuthorizationMappingGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUTHORIZATION_MAPPING);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedAttribute, iIndex) {
      if (oOldProcessedAttribute.isDirty) {
        let oAuthorizationMapping = CS.find(aAuthorizationMappingGridData, {id: oOldProcessedAttribute.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.AUTHORIZATION_MAPPING,
            [oAuthorizationMapping])[0];
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

  /**********************************  PUBLIC API's   ************************************/
  return {

    setUpAuthorizationConfigGridView: function () {
      _setUpAuthorizationConfigGridView();
    },

    createAuthorizationMappingDialogButtonClicked: function () {
      _createDefaultAuthorizationMappingObject();
    },

    createAuthorizationMapping: function () {
      _createAuthorizationMapping();
    },

    cancelAuthorizationMappingCreation: function () {
      _cancelAuthorizationMappingCreation();
    },

    handleAuthorizationMappingDialogButtonClicked: function (sButtonId) {
      _handleAuthorizationMappingDialogButtonClicked(sButtonId);
    },

    handleAuthorizationMappingConfigDialogButtonClicked: function (sButtonId) {
      _handleAuthorizationMappingConfigDialogButtonClicked(sButtonId);
    },

    handleAuthorizationMappingApplyButtonClicked: function (sContext, aSelectedItems, oReferencedData) {
      _handleAuthorizationMappingApplyButtonClicked(sContext, aSelectedItems, oReferencedData);
    },

    handleAuthorizationMappingCheckboxButtonClicked: function (bIsCheckboxClicked, sContext) {
      _handleAuthorizationMappingCheckboxButtonClicked(bIsCheckboxClicked, sContext);
    },

    handleAuthorizationMappingDeleteButtonClicked: function (sSelectedElement, sContext) {
      _handleAuthorizationMappingDeleteButtonClicked(sSelectedElement, sContext);
    },

    handleAuthorizationMappingToggleButtonClicked: function (bIsActivationEnable, oElementData) {
      _handleAuthorizationMappingToggleButtonClicked(bIsActivationEnable, oElementData);
    },

    handleAuthorizationMappingConfigFieldValueChanged: function (sKey, sValue) {
      let oActiveMapping = _makeActiveAuthorizationMappingDirty();
      oActiveMapping[sKey] = sValue;
      _triggerChange()
    },

    deleteAuthorizationMapping: function (aSelectedIds) {
      _deleteAuthorizationMapping(aSelectedIds);
    },

    handleTabLayoutTabChanged: function (sTabId) {
      _handleTabLayoutTabChanged(sTabId);
    },

    editButtonClicked: function (sEntityId) {
      _editButtonClicked(sEntityId);
    },

    fetchAuthorizationMappingList: function () {
      _fetchAuthorizationListForGridView();
    },

    processProcessAuthorizationMappingListAndSave: function (oCallbackData) {
      _processProcessAuthorizationMappingListAndSave(oCallbackData)
      .then(_triggerChange);
    },

    discardAuthorizationMappingListChanges: function (oCallbackData) {
      _discardAuthorizationMappingListChanges(oCallbackData);
    },

    saveAuthorizationMapping: function (oCallbackData) {
      _saveAuthorizationMapping(oCallbackData);
    },

    reset: function () {
      SettingUtils.getAppData().setAuthorizationMappingList([]);
      AuthorizationConfigViewProps.reset();
    },
  }
})();

MicroEvent.mixin(AuthorizationStore);
export default AuthorizationStore;

