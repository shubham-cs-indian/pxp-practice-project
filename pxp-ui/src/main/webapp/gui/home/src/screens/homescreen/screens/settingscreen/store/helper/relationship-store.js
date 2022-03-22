import CS from '../../../../../../libraries/cs';
import {RelationshipRequestMapping as oRelationshipRequestMapping }from '../../tack/setting-screen-request-mapping';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import { relAndContextCouplingTypes as RelationAndContextCouplingTypes } from '../../../../../../commonmodule/tack/version-variant-coupling-types';
import RelationshipProps from './../model/relationship-config-view-props';
import SettingUtils from './../helper/setting-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../../commonmodule/tack/config-data-entities-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import RelationshipConfigGridSkeleton from './../../tack/relationship-config-grid-view-skeleton';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import SettingScreenProps from './../model/setting-screen-props';
import assetTypes from '../../tack/coverflow-asset-type-list';
import {getTranslations as oTranslations} from "../../../../../../commonmodule/store/helper/translation-manager";
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import CommonUtils from "../../../../../../commonmodule/util/common-utils";

var RelationshipStore = (function () {

  var _triggerChange = function () {
    RelationshipStore.trigger('relationship-changed');
  };

  var successFetchRelationshipListCallback = function (sSelectedConfigEntity, oResponse) {
    let oSuccess = oResponse.success;
    if(oResponse.success) {
      let aRelationshipListFromServer = (oResponse.success) ? oResponse.success.list : undefined;
      var oRelationshipMasterListMap = {};
      var oRelationshipValueList = {};

      RelationshipProps.setSelectedRelationshipConfigDetails(oResponse.success.configDetails);

      CS.forEach(aRelationshipListFromServer, function (oRelationship) {
        oRelationshipMasterListMap[oRelationship.id] = oRelationship;

        var oListItemProperties = {};
        oListItemProperties.id = oRelationship.id;
        oListItemProperties.isChecked = false;
        oListItemProperties.isSelected = false;

        oRelationshipValueList[oRelationship.id] = oListItemProperties;
      });
      SettingUtils.getAppData().setRelationshipMasterList(oRelationshipMasterListMap);
      RelationshipProps.setRelationshipValuesList(oRelationshipValueList);
      GridViewStore.preProcessDataForGridView(sSelectedConfigEntity, aRelationshipListFromServer, oSuccess.count);
      RelationshipProps.setRelationshipGridData(aRelationshipListFromServer);
    }
    _triggerChange();
  };

  var failureFetchRelationshipListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchRelationshipListCallback", getTranslation());
  };

  var successDeleteRelationshipCallback = function (sContext, oResponse) {

    let aSuccessIds = (oResponse.success) ? oResponse.success : [];
    var oMasterList = SettingUtils.getAppData().getRelationshipMasterList();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(sContext);

    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
    var aSuccessFullyDeletedRelationships = [];
    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      aSuccessFullyDeletedRelationships.push(oMasterList[sId].label);
      delete oMasterList[sId];
      CS.remove(oSkeleton.selectedContentIds, function (oSelectedId) {
        return oSelectedId == sId;
      });
    });
    oGridViewPropsByContext.setGridViewTotalItems(SettingScreenProps.screen.getGridViewTotalItems() - aSuccessIds.length);
    RelationshipProps.setRelationshipScreenLockStatus(false);
    if (aSuccessFullyDeletedRelationships.length > 0) {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().RELATIONSHIP}));
    }

    _failedDeleteRelationship(oResponse.failure);
  };

  var failureDeleteRelationshipCallback = function (oCallback, oResponse) {
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
      if(oResponse.errorCode == "com.cs.config.interactor.incoming.InteractorException-com.cs.config.interactor.outgoing.database.NoRelationshipDeletedException") {
        _failedDeleteRelationship(oResponse.success.data);
        RelationshipProps.setRelationshipScreenLockStatus(false);
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureDeleteRelationshipCallback", getTranslation());
    }

    _triggerChange();
  };

  var _resetAllKlassesForRelationship = function () {
    var oAllKlassesForRelationship = RelationshipProps.getAllKlassesForRelationships();
    var oRelationship = RelationshipProps.getKlassesForRelationships();
    oRelationship.side1 = oAllKlassesForRelationship;
    oRelationship.side2 = oAllKlassesForRelationship;
  };

  var successFetchRelationshipCallback = function (oResponse) {
    var oSuccessResponse = oResponse.success;
    var oSelectedRelationship = oSuccessResponse.entity;
    var oConfigDetails = oSuccessResponse.configDetails;


    RelationshipProps.setSelectedRelationship(oSelectedRelationship);
    RelationshipProps.setSelectedRelationshipConfigDetails(oConfigDetails);

    var oScreenProps = SettingUtils.getComponentProps().screen;
    oScreenProps.setReferencedAttributes(oConfigDetails.referencedAttributes || {});
    oScreenProps.setReferencedTags(oConfigDetails.referencedTags || {});
    RelationshipProps.setContextData(oConfigDetails.referencedContexts);

    RelationshipProps.setIsRelationshipDialogActive(true);

      _applyRelationshipValueToAllListNodes('isSelected', false);
      _applyRelationshipValueToAllListNodes('isChecked', false);
      var oListValuesInList = _getRelationshipValuesList();
      var oSelectedValueList = oListValuesInList[oSelectedRelationship.id];
      oSelectedValueList.isSelected = true;
    _resetAllKlassesForRelationship();
    _triggerChange();
  };

  var failureFetchRelationshipCallback = function (sRelationshipId,oResponse) {

    if (oResponse.failure && !CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "RelationshipNotFoundException"}))) {
      var sRelationshipName = _removeRelationshipsFromListOnFailure(oResponse,sRelationshipId);
      alertify.error("[" + sRelationshipName +"] "+getTranslation().ERROR_ALREADY_DELETED, 0);
      RelationshipProps.setRelationshipScreenLockStatus(false);
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchRelationshipCallback", getTranslation());
    }

    _triggerChange();
  };

  var successSaveRelationshipCallback = function (oSelectedRelationship, oCallback, oResponse) {

      var oRelationshipMasterList = SettingUtils.getAppData().getRelationshipMasterList();
      var oRelationshipValueList = RelationshipProps.getRelationshipValuesList();
      let oSuccessResponse = oResponse.success;
      let oSavedRelationship = oSuccessResponse.entity;
      let oConfigDetails = oSuccessResponse.configDetails;
      let sContext = ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP;

      RelationshipProps.setSelectedRelationshipConfigDetails(oConfigDetails);

      oRelationshipMasterList[oSavedRelationship.id] = oSavedRelationship;
      RelationshipProps.setSelectedRelationship(oSavedRelationship);

      RelationshipProps.setRelationshipScreenLockStatus(false);

    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(sContext);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();

    if (oCallback.isCreated) {

      oRelationshipValueList[oSavedRelationship.id] = oRelationshipValueList[oSelectedRelationship.id];
      delete oRelationshipValueList[oSelectedRelationship.id];
      delete oRelationshipMasterList[oSelectedRelationship.id];

      let oProcessedRelationship = GridViewStore.getProcessedGridViewData(sContext, [oSavedRelationship])[0];
      aGridViewData.unshift(oProcessedRelationship);
      let aRelationshipGridData = RelationshipProps.getRelationshipGridData();
      aRelationshipGridData.push(oSavedRelationship);
      oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);
      alertify.success(getTranslation().RELATIONSHIP_CREATED_SUCCESSFULLY);
    } else {
      let oProcessedRelationship = GridViewStore.getProcessedGridViewData(sContext, [oSavedRelationship])[0];
      let aRelationshipGridData = RelationshipProps.getRelationshipGridData();
      let iRelIndex = CS.findIndex(aRelationshipGridData, {id: oSavedRelationship.id});
      aRelationshipGridData[iRelIndex] = oProcessedRelationship;
      let iGridIndex = CS.findIndex(aGridViewData, {id: oSavedRelationship.id});
      aGridViewData[iGridIndex] = oProcessedRelationship;
      oGridViewPropsByContext.setGridViewData(aGridViewData);
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().RELATIONSHIP}));
    }

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var failureSaveRelationshipCallback = function (oResponse) {
    if(CS.find(oResponse.failure.exceptionDetails, {key: "ParentChildRelationshipException"})) {
        SettingUtils.failureCallback(oResponse, "failureSaveRelationshipCallback", getTranslation());
    }
    else if(CS.find(oResponse.failure.exceptionDetails, {key: "RelationshipCreationNotAllowedException"})) {
      SettingUtils.failureCallback(oResponse, "failureSaveRelationshipCallback", getTranslation());
    } else if (!CS.isEmpty(oResponse.failure)) {
      var sRelationshipName = _removeRelationshipsFromListOnFailure(oResponse);
      alertify.error("[" + sRelationshipName +"] "+getTranslation().ERROR_ALREADY_DELETED, 0);
      RelationshipProps.setRelationshipScreenLockStatus(false);
    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveRelationshipCallback", getTranslation());
    }

    _triggerChange();
  };

  var _removeRelationshipsFromListOnFailure = function(oResponse,sRelationshipId){
      var oMasterRelationship = SettingUtils.getAppData().getRelationshipMasterList();
      var oRelationshipValues = RelationshipProps.getRelationshipValuesList();

      var oSelectedRelationship = RelationshipProps.getSelectedRelationship();
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "RelationshipNotFoundException"}))
          && (sRelationshipId || oSelectedRelationship.id)) {
        var sRelationshipName = oMasterRelationship[sRelationshipId || oSelectedRelationship.id].label;
        delete oRelationshipValues[sRelationshipId || oSelectedRelationship.id];
        delete oMasterRelationship[sRelationshipId || oSelectedRelationship.id];
        RelationshipProps.setSelectedRelationship({});
        return sRelationshipName;
      }
    return null;
  };

  var _failedDeleteRelationship = function (aList) {

    var oMasterList = SettingUtils.getAppData().getRelationshipMasterList();
    var oValueList = RelationshipProps.getRelationshipValuesList();
    var aAlreadyDeletedRelationships = [];
    var aNotDeletedRelationships = [];
    var aLinkedRelationship = [];

    CS.forEach(aList, function (oFailedRelationshipObject) {
      var sRelationshipId = oFailedRelationshipObject.id;
      var oRelationshipFromMaster = oMasterList[sRelationshipId];
      if(oFailedRelationshipObject.errorCode == "RelationshipNotFoundException") {
        aAlreadyDeletedRelationships.push(oRelationshipFromMaster.label);
        delete oMasterList[sRelationshipId];
        delete oValueList[sRelationshipId];

      } else if (oFailedRelationshipObject.errorCode == "LinkedRelationshipException") {
        aLinkedRelationship.push(oRelationshipFromMaster.label);

      } else {
        aNotDeletedRelationships.push(oRelationshipFromMaster.label);
      }

      if (aAlreadyDeletedRelationships.length > 0) {
        var sAlreadyDeletedRelationships = aAlreadyDeletedRelationships.join(', ');
        alertify.error(Exception.getCustomMessage("Partial_Success_Delete_Relationship", getTranslation(), sAlreadyDeletedRelationships), 0);
      }

      if(aNotDeletedRelationships.length > 0) {
        var sNotDeletedRelationships = aNotDeletedRelationships.join(', ');
        alertify.error(Exception.getCustomMessage("Partial_Failure_Delete_Relationship", getTranslation(), sNotDeletedRelationships), 0);
      }

      if(aLinkedRelationship.length > 0) {
        var sLinkedRelationship = aNotDeletedRelationships.join(', ');
        alertify.error(Exception.getCustomMessage("Partial_Failure_Linked_Relationship", getTranslation(), sLinkedRelationship), 0);
      }
    });
  };

  let _fetchRelationshipsList = function () {
    let sSearchText = RelationshipProps.getSearchText();
    let iFrom = RelationshipProps.getFrom();
    let oPostConstantData = RelationshipProps.getSearchConstantData();
    let oPostData = {
      searchText: sSearchText,
      from: iFrom,
      size: oPostConstantData.size,
      searchColumn: oPostConstantData.searchColumn,
      sortOrder: oPostConstantData.sortOrder,
      sortBy: oPostConstantData.sortBy,
    };

    SettingUtils.csPostRequest(oRelationshipRequestMapping.GetRelationshipList,
        {},
        oPostData,
        successFetchRelationshipListCallback.bind(this, ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP),
        failureFetchRelationshipListCallback);
  };

  var _deleteUnSavedRelationship = function (aList) {
    var oValueList = RelationshipProps.getRelationshipValuesList();
    var oMasterList = SettingUtils.getAppData().getRelationshipMasterList();
    var aSavedRelationshipIds = [];
    CS.forEach(aList, function (uid) {
      if (oMasterList[uid].isCreated /*|| oMasterList[uid].isDirty*/) {
        delete oValueList[uid];
        delete oMasterList[uid];
      } else {
        aSavedRelationshipIds.push(uid);
      }
    });

    return aSavedRelationshipIds;
  };

  var _deleteRelationship = function (aRelationshipListToDelete, oCallBack) {
    //To avoid the detailed view of deleted nodes (newly created and old relationships)
    var oSelectedRelationship = RelationshipProps.getSelectedRelationship();

    if (CS.indexOf(aRelationshipListToDelete, oSelectedRelationship.id) >= 0) {
      RelationshipProps.setSelectedRelationship({});
    }

    //to delete dirty relationships
    var aFilteredRelationshipIds = _deleteUnSavedRelationship(aRelationshipListToDelete);

    if (!CS.isEmpty(aFilteredRelationshipIds)) {
      let sGridViewContext = SettingScreenProps.screen.getGridViewContext();
      let sUrl = oRelationshipRequestMapping.BulkDelete;
      return SettingUtils.csDeleteRequest(sUrl, {}, {ids: aFilteredRelationshipIds},
          successDeleteRelationshipCallback.bind(this, sGridViewContext), failureDeleteRelationshipCallback.bind(this, oCallBack));
    } else {
      RelationshipProps.setRelationshipScreenLockStatus(false);
      alertify.success(getTranslation().DELETED_SUCCESSFULLY_ONLY_NEWLY_CREATED);
      _triggerChange();
    }
  };

  var _fetchRelationshipById = function (sRelationshipId) {

    let sUrl = oRelationshipRequestMapping.GetRelationshipById;
    var oParameters = {};
    oParameters.id = sRelationshipId;
    SettingUtils.csGetRequest(sUrl, oParameters, successFetchRelationshipCallback, failureFetchRelationshipCallback.bind(this,sRelationshipId));
  };

  var _getADMOfPropertyList = function (aPreviousList, aNewList, sSide) {
    var oADM = {
      added: [],
      deleted: [],
      modified: []
    };

    CS.forEach(aPreviousList, function (oPrevAttr) {
      var oNewAttr = CS.find(aNewList, {id: oPrevAttr.id});
      if(oNewAttr) {
        //Modified
        if(oNewAttr.couplingType != oPrevAttr.couplingType) {
          oNewAttr.side = sSide;
          oADM.modified.push(oNewAttr);
        }
      } else {
        //Deleted
        oPrevAttr.side = sSide;
        oADM.deleted.push(oPrevAttr);
      }
    });


    CS.forEach(aNewList, function (oNewAttr) {
      if(!CS.find(aPreviousList, {id: oNewAttr.id})) {
        //Added
        oNewAttr.side = sSide;
        oADM.added.push(oNewAttr);
      }

    });

    return oADM;

  };

  var _generateRelationshipPropertyADM = function (oClonedRelationshipObject, oPreviousRelationship) {
    let oClonedRelationship = CS.cloneDeep(oClonedRelationshipObject);
    var oADM = {
      addedAttributes: [],
      modifiedAttributes: [],
      deletedAttributes: [],
      addedTags: [],
      modifiedTags: [],
      deletedTags: [],
    };

    var oClonedSide1 = oClonedRelationship.side1;
    var oClonedSide2 = oClonedRelationship.side2;

    var oPreviousSide1 = oPreviousRelationship.side1;
    var oPreviousSide2 = oPreviousRelationship.side2;

    //Side1 Attributes
    var oAttrADM = _getADMOfPropertyList(oPreviousSide1.attributes, oClonedSide1.attributes, "side1");
    oADM.addedAttributes = oADM.addedAttributes.concat(oAttrADM.added);
    oADM.modifiedAttributes = oADM.modifiedAttributes.concat(oAttrADM.modified);
    oADM.deletedAttributes = oADM.deletedAttributes.concat(oAttrADM.deleted);

    //Side2 Attributes
    oAttrADM = _getADMOfPropertyList(oPreviousSide2.attributes, oClonedSide2.attributes, "side2");
    oADM.addedAttributes = oADM.addedAttributes.concat(oAttrADM.added);
    oADM.modifiedAttributes = oADM.modifiedAttributes.concat(oAttrADM.modified);
    oADM.deletedAttributes = oADM.deletedAttributes.concat(oAttrADM.deleted);

    //Side1 Tags
    var oTagADM = _getADMOfPropertyList(oPreviousSide1.tags, oClonedSide1.tags, "side1");
    oADM.addedTags = oADM.addedTags.concat(oTagADM.added);
    oADM.modifiedTags = oADM.modifiedTags.concat(oTagADM.modified);
    oADM.deletedTags = oADM.deletedTags.concat(oTagADM.deleted);

    //Side2 Tags
    oTagADM = _getADMOfPropertyList(oPreviousSide2.tags, oClonedSide2.tags, "side2");
    oADM.addedTags = oADM.addedTags.concat(oTagADM.added);
    oADM.modifiedTags = oADM.modifiedTags.concat(oTagADM.modified);
    oADM.deletedTags = oADM.deletedTags.concat(oTagADM.deleted);

    delete oClonedSide1.attributes;
    delete oClonedSide1.tags;
    delete oClonedSide2.attributes;
    delete oClonedSide2.tags;

    CS.assign(oClonedRelationship, oADM);

    if(!oClonedRelationship.isCreated){
      if(oClonedSide1.contextId != oPreviousSide1.contextId){
        oClonedSide1.addedContext = oClonedSide1.contextId;
        oClonedSide1.deletedContext = oPreviousSide1.contextId;
        delete oClonedSide1.contextId;
      }

      if(oClonedSide2.contextId != oPreviousSide2.contextId){
        oClonedSide2.addedContext = oClonedSide2.contextId;
        oClonedSide2.deletedContext = oPreviousSide2.contextId;
        delete oClonedSide2.contextId;
      }
    }
   return oClonedRelationship;
  };

  var _saveRelationship = function (oCallback) {
    let sContext = SettingScreenProps.screen.getGridViewContext();
    let oSelectedRelationship = RelationshipProps.getSelectedRelationship();

    let oCurrentRelationship = oSelectedRelationship.clonedObject ? oSelectedRelationship.clonedObject: oSelectedRelationship;

    let sSide1KlassId = !CS.isEmpty(oCurrentRelationship.side1) ? oCurrentRelationship.side1.klassId : "";
    let sSide2KlassId = !CS.isEmpty(oCurrentRelationship.side2) ? oCurrentRelationship.side2.klassId : "";
    if (CS.isEmpty(sSide1KlassId) || CS.isEmpty(sSide2KlassId)) {
      alertify.error(getTranslation().ERROR_RELATIONSHIP_SIDE_NOT_SELECTED);
      return;
    }
    if (CS.isEmpty(oCurrentRelationship.label)) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    } else if (CS.isEmpty(oCurrentRelationship.side1.label) || (CS.isEmpty(oCurrentRelationship.side2.label))) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_RELATIONSHIP_LABEL);
      return;
    }

    var oCallbackData = {};
    oCallbackData.functionToExecute = oCallback.functionToExecute;
    var oSelectedRelationshipnew = oSelectedRelationship;


    if(oSelectedRelationship.isCreated) {
      //Just to send ADM for properties used already written ADM generation which expects a previous object
      let oTemp = _createUntitledRelationshipMasterObject(sContext);
      if (sContext == GridViewContexts.RELATIONSHIP) {
        oSelectedRelationshipnew = _generateRelationshipPropertyADM(oCurrentRelationship, oTemp);
      }

      var oCodeToVerifyUniqueness = {
        id: oSelectedRelationshipnew.code,
        entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP
      };
      oCallbackData.isCreated = true;
      var oCallbackDataForUniqueness = {};
      let sCreateUrl = oRelationshipRequestMapping.CreateRelationship;

      oCallbackDataForUniqueness.functionToExecute = function() {
        delete oSelectedRelationshipnew.isCreated;
        _createRelationshipCall(oSelectedRelationshipnew, oCallbackData, sCreateUrl);
      }
      var sURL = oRelationshipRequestMapping.CheckEntityCode;
      SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
      return;
    }

    if (oSelectedRelationshipnew.isDirty) {
      var oOriginalRelatiohship = oSelectedRelationshipnew;
      oSelectedRelationshipnew = oSelectedRelationshipnew.clonedObject;

      var oADMRelationship = CS.cloneDeep(CS.omit(oSelectedRelationshipnew, ["sections", "configDetails"]));
      oADMRelationship.addedSections = CS.differenceBy(oSelectedRelationshipnew.sections, oOriginalRelatiohship.sections, "id");
      oADMRelationship.deletedSections = CS.map(CS.differenceBy(oOriginalRelatiohship.sections, oSelectedRelationshipnew.sections, "id"), "id");
      oADMRelationship.modifiedSections = [];

      let oADMRelationshipnew =  _generateRelationshipPropertyADM(oADMRelationship, oOriginalRelatiohship);
      let oCustomTabsADM = SettingUtils.generateADMForCustomTabs(oOriginalRelatiohship.tab, oSelectedRelationshipnew.tab);
      oADMRelationshipnew.addedTab = oCustomTabsADM.addedTab;
      oADMRelationshipnew.deletedTab = oCustomTabsADM.deletedTab;
      let sSaveURL = oRelationshipRequestMapping.SaveRelationship;

      oCallbackData.isDeletedAnyContext = CS.isNotEmpty(oADMRelationshipnew.side1.deletedContext) || CS.isNotEmpty(oADMRelationshipnew.side2.deletedContext);

      SettingUtils.csPostRequest(sSaveURL, {}, oADMRelationshipnew,
          successSaveRelationshipCallback.bind(this, oADMRelationshipnew, oCallbackData), failureSaveRelationshipCallback.bind(this));
    } else {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      if (oCallback.functionToExecute) {
        oCallback.functionToExecute();
      }
    }
  };

  var _createRelationshipCall = function(oSelectedRelationship, oCallbackData, sUrl) {
    SettingUtils.csPutRequest(sUrl, {}, oSelectedRelationship, successSaveRelationshipCallback.bind(this, oSelectedRelationship, oCallbackData),
        failureSaveRelationshipCallback.bind(this));
  };

  var _getSelectedRelationship = function () {
    return RelationshipProps.getSelectedRelationship();
  };

  var _setSelectedRelationship = function (oRelationship) {
    RelationshipProps.setSelectedRelationship(oRelationship);
  };

  var _makeActiveRelationshipDirty = function () {
    var oSelectedRelationship = _getSelectedRelationship();
    if (!oSelectedRelationship.isCreated && !oSelectedRelationship.isDirty) {
      SettingUtils.makeObjectDirty(oSelectedRelationship);
    }
    oSelectedRelationship = oSelectedRelationship.clonedObject ? oSelectedRelationship.clonedObject: oSelectedRelationship;
    return oSelectedRelationship;
  };

  var _getRelationshipScreenLockStatus = function () {
    return RelationshipProps.getRelationshipScreenLockStatus();
  };

  var _setRelationshipScreenLockStatus = function (bLockStatus) {
    RelationshipProps.setRelationshipScreenLockStatus(bLockStatus);
  };

  var _getRelationshipValuesList = function () {
    return RelationshipProps.getRelationshipValuesList();
  };

  //TODO: remove this function (since list view does not exist)
  var _applyRelationshipValueToAllListNodes = function (sAttributeName, oValue) {
    var oListValuesInList = _getRelationshipValuesList();
    CS.forEach(oListValuesInList, function (oListValue) {
      oListValue[sAttributeName] = oValue;
    });
  };

  var _createUntitledRelationshipMasterObject = function (sGridViewContext) {
    var side1 = {};
    side1.label = UniqueIdentifierGenerator.generateUntitledName();
    side1.klassId = '';
    side1.type = 'Klass';
    side1.cardinality = 'Many';
    side1.isVisible = true;

    var side2 = {};
    side2.label = UniqueIdentifierGenerator.generateUntitledName();
    side2.klassId = '';
    side2.type = 'Klass';
    side2.cardinality = 'Many';
    side2.isVisible = true;

    let sType= "com.cs.core.config.interactor.entity.relationship.Relationship";

    if (sGridViewContext == GridViewContexts.RELATIONSHIP) {
      side1.attributes = [];
      side1.tags = [];

      side2.attributes = [];
      side2.tags = [];
    }

    return {
      "id": '',
      "label": UniqueIdentifierGenerator.generateUntitledName(),
      "tooltip": "",
      "side1":side1,
      "side2":side2,
      "sections": [],
      "type": sType,
      "isLite": false,
      "isCreated": true,
      "code": ""
    }
  };

  var _createUntitledRelationshipValueObject = function () {
    return {
      id: "",
      isChecked: false,
      isSelected: true,
      isCreated: true,
      isEditable: true
    };
  };

  var _createNewRelationship = function (sGridViewContext) {

    var oSelectedRelationship = _getSelectedRelationship();

    if (oSelectedRelationship.isDirty || oSelectedRelationship.isCreated) {
      var oCallbackData = {};
      oCallbackData.functionToExecute = _createNewRelationship;
      CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveRelationship.bind(this, oCallbackData),
          _discardUnsavedRelationship.bind(this, oCallbackData),
          function () {
          });
    } else {
      var uuid = UniqueIdentifierGenerator.generateUUID();
      var oUntitledRelationshipValueObj = _createUntitledRelationshipValueObject();
      var oUntitledRelationshipMasterObj = _createUntitledRelationshipMasterObject(sGridViewContext);
      _resetAllKlassesForRelationship();

      oUntitledRelationshipValueObj.id = uuid;
      oUntitledRelationshipMasterObj.id = uuid;

      _applyRelationshipValueToAllListNodes('isSelected', false);
      _applyRelationshipValueToAllListNodes('isChecked', false);

      var oRelationshipValues = _getRelationshipValuesList();
      var oMasterRelationships = SettingUtils.getAppData().getRelationshipMasterList();

      oRelationshipValues[uuid] = oUntitledRelationshipValueObj;
      oMasterRelationships[uuid] = oUntitledRelationshipMasterObj;

      _setSelectedRelationship(oUntitledRelationshipMasterObj);
      _setRelationshipScreenLockStatus(true);
      SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);

      RelationshipProps.setIsRelationshipDialogActive(true);
      _triggerChange();
    }
  };

  var _discardUnsavedRelationship = function (oCallback) {

    var oSelectedRelationship = _getSelectedRelationship();
    var oValueList = _getRelationshipValuesList();
    var oMasterList = SettingUtils.getAppData().getRelationshipMasterList();
    var bScreenLockStatus = _getRelationshipScreenLockStatus();
    if(!bScreenLockStatus) {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      return;
    }

    if(oSelectedRelationship.isCreated) {
      delete oValueList[oSelectedRelationship.id];
      delete oMasterList[oSelectedRelationship.id];
      _setSelectedRelationship({});
      _setRelationshipScreenLockStatus(false);
      RelationshipProps.setIsRelationshipDialogActive(false);
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    }
    else if (oSelectedRelationship.isDirty) {
      delete oSelectedRelationship.clonedObject;
      delete oSelectedRelationship.isDirty;

      _setRelationshipScreenLockStatus(false);
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    } else {
      alertify.message(NO_CHANGE_FOUND_TO_DISCARD) // eslint-disable-line
    }

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    _setRelationshipScreenLockStatus(false);

    _triggerChange();
  };

  var _handleRelationshipListNodeClicked = function (oModel) {

    var bRelationshipScreenLockStatus = _getRelationshipScreenLockStatus();

    if (!oModel.isSelected) {

      if (!bRelationshipScreenLockStatus) {
        _fetchRelationshipById(oModel.id);
      } else {
        var oCallbackData = {};
        oCallbackData.functionToExecute = _handleRelationshipListNodeClicked.bind(this, oModel);
        CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            _saveRelationship.bind(this, oCallbackData),
            _discardUnsavedRelationship.bind(this, oCallbackData),
            function () {
            }
        );
      }
    } else {
      _applyRelationshipValueToAllListNodes('isChecked', false);
      _triggerChange();
    }

  };

  var _handleRelationshipDataChangeCallback = function (oNewModel, oReferencedKlasses, bClassChanged) {

    var oSelectedRelationship = _getSelectedRelationship();
    let oSelectedRelationshipConfigDetails = RelationshipProps.getSelectedRelationshipConfigDetails();
    if (!oSelectedRelationshipConfigDetails || !oSelectedRelationshipConfigDetails.referencedKlasses) {
      oSelectedRelationshipConfigDetails.referencedKlasses = {};
    }
    CS.assign(oSelectedRelationshipConfigDetails.referencedKlasses, oReferencedKlasses);
    if (!oSelectedRelationship.isCreated && !oSelectedRelationship.isDirty) {
      SettingUtils.makeObjectDirty(oSelectedRelationship);
    }

    oSelectedRelationship = oSelectedRelationship.clonedObject ? oSelectedRelationship.clonedObject : oSelectedRelationship;
    oSelectedRelationship.label = CS.trim(oNewModel.label);
    oSelectedRelationship.tooltip = oNewModel.tooltip;
    oSelectedRelationship.code = oNewModel.properties['code'];

    var oSides = oNewModel.side;
    oSelectedRelationship.side1 = oSides.side1;
    oSelectedRelationship.side2 = oSides.side2;

    var oValueList = _getRelationshipValuesList();

    if (oValueList.hasOwnProperty(oSelectedRelationship.id)) {
      oValueList[oSelectedRelationship.id].isEditable = false;
    }

    _setRelationshipScreenLockStatus(true);
    _triggerChange();
  };

  var _handleRelationshipDataChanged = function (oNewModel, oReferencedKlasses) {
    _handleRelationshipDataChangeCallback(oNewModel, oReferencedKlasses);
  };

  var _handleRelationshipSingleValueChanged = function (sKey, oVal, oReferencedTabs) {
    var oActiveRelationship = _makeActiveRelationshipDirty();
    let oReferencedData = RelationshipProps.getSelectedRelationshipConfigDetails();
    CS.assign(oReferencedData.referencedTabs, oReferencedTabs);
    oActiveRelationship[sKey] = oVal;
    _setRelationshipScreenLockStatus(true);
    _triggerChange();
  };

  let _handleRelationshipTypeChanged = function (sTypeId) {
    let oActiveRelationship = _getSelectedRelationship();
    let oCloneRelationship = oActiveRelationship.clonedObject ? CS.cloneDeep(oActiveRelationship.clonedObject) : oActiveRelationship;
    oActiveRelationship.clonedObject = _createUntitledRelationshipMasterObject(GridViewContexts.RELATIONSHIP);
    oActiveRelationship.isLite = (sTypeId === "liteRelationship");

    oCloneRelationship.isLite = oActiveRelationship.isLite;
    oActiveRelationship.clonedObject.isLite = oActiveRelationship.isLite;
    oActiveRelationship.clonedObject.id = oActiveRelationship.id;
    oActiveRelationship.clonedObject.label = oCloneRelationship.label;
    oActiveRelationship.clonedObject.icon = oCloneRelationship.icon;
    oActiveRelationship.clonedObject.code = oCloneRelationship.code;
    oActiveRelationship.clonedObject.tab = oCloneRelationship.tab;

    _setRelationshipScreenLockStatus(true);
    _triggerChange();
  };

  let _addPropertyInRelationshipSide = (sEntity, sSide, aSelectedIds, oReferencedData, bForExtension) => {
    let oComponentProps = SettingUtils.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oActiveRelationship = _makeActiveRelationshipDirty();
    let oSide = oActiveRelationship[sSide];
    let oRelationAndContextCouplingTypes = new RelationAndContextCouplingTypes();

    let aList = [];
    if (sEntity === ConfigDataEntitiesDictionary.ATTRIBUTES) {
      aList = oSide.attributes;
      CS.assign(oScreenProps.getReferencedAttributes(), oReferencedData);
    } else if (sEntity === ConfigDataEntitiesDictionary.TAGS) {
      aList = oSide.tags;
      CS.assign(oScreenProps.getReferencedTags(), oReferencedData);
    }

    let aNewItems = [];
    CS.forEach(aSelectedIds, function (iItemID) {
      var oProperty = {
        id: iItemID,
        couplingType: oRelationAndContextCouplingTypes[0].id
      };
      let oItem = CS.find(aList, {id: iItemID}) || oProperty;
      aNewItems.push(oItem);
    });

    if (sEntity === ConfigDataEntitiesDictionary.ATTRIBUTES) {
      oSide.attributes = aNewItems;
    } else if (sEntity === ConfigDataEntitiesDictionary.TAGS) {
      oSide.tags = aNewItems;
    }

    _setRelationshipScreenLockStatus(true);
  };

  var _handleSelectionToggleButtonClicked = function (sKey, sId) {
    var sSide = (sKey == "relationshipSide1") ? "side1" : "side2";
    var oActiveRelationship = _makeActiveRelationshipDirty();
    var oSide = oActiveRelationship[sSide];
    oSide.contextId = (oSide.contextId == sId) ? "" : sId;
    _setRelationshipScreenLockStatus(true);
  };

  var _removePropertyInRelationshipSide = function (sSide, iItemID, sContext) {
    var oActiveRelationship = _makeActiveRelationshipDirty();
    var oSide = oActiveRelationship[sSide];
    CS.remove(oSide[sContext], {id: iItemID});
    _setRelationshipScreenLockStatus(true);
  };

  var _changePropertyCoupling = function (sSide, sPropertyId, sNewValue, sContext) {
    var oActiveRelationship = _makeActiveRelationshipDirty();
    var oSide = oActiveRelationship[sSide];
    var oItem = CS.find(oSide[sContext], {id: sPropertyId});

    oItem && (oItem.couplingType = sNewValue);

    _setRelationshipScreenLockStatus(true);
  };

  let _setUpRelationshipConfigGridView = function (sSelectedConfigEntity) {
    let oRelationshipConfigGridSkeleton = new RelationshipConfigGridSkeleton();
    oRelationshipConfigGridSkeleton.actionItems.push({
        id: "manageEntity",
        label: oTranslations().MANAGE_ENTITY_USAGE,
        class: "manageEntity"
      })

    GridViewStore.createGridViewPropsByContext(sSelectedConfigEntity, {skeleton: oRelationshipConfigGridSkeleton});
    SettingScreenProps.screen.setGridViewContext(sSelectedConfigEntity);
    _fetchRelationshipListForGridView(sSelectedConfigEntity);
  };

  var _fetchRelationshipListForGridView = function (sSelectedConfigEntity) {
    let oPostData = GridViewStore.getPostDataToFetchList(sSelectedConfigEntity);
    let sURL = oRelationshipRequestMapping.GetRelationshipList;

    /* Relationship Type is stored as a isLite boolean value in backend*/
    if (oPostData.sortBy === "type") {
      oPostData.sortBy = "isLite";
    }
    SettingUtils.csPostRequest(sURL, {}, oPostData, successFetchRelationshipListCallback.bind(this, sSelectedConfigEntity), failureFetchRelationshipListCallback);

  };

  let _closeRelationshipDialog = function () {
    RelationshipProps.setIsRelationshipDialogActive(false);
    _triggerChange();
  };

  let _postProcessRelationshipListAndSave = function (oCallBackData) {
    let oSelectedRelationship = RelationshipProps.getSelectedRelationship();
    let oClonedRelationship = oSelectedRelationship.clonedObject;

    /** In case of newly created relationship refer selected relationship object**/
    /** In case of Existing relationship refer Cloned relationship object**/
    let oTab = oClonedRelationship && oClonedRelationship.tab || oSelectedRelationship && oSelectedRelationship.tab;
    let bIsNewlyCreatedTab = oTab && oTab.isNewlyCreated || false;
    if (bIsNewlyCreatedTab) {
      _saveRelationship(oCallBackData);
      return;
    }

    var oConfigDetails = RelationshipProps.getSelectedRelationshipConfigDetails();

    var aRelationshipGridData = RelationshipProps.getRelationshipGridData();
    var aRelationshipListToSave = [];

    let oExtraData = {
      referencedTabs: oConfigDetails.referencedTabs
    };
    let sUrl = oRelationshipRequestMapping.BulkSave;
    let sContext = ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP;

    let bSafeToSave = GridViewStore.processGridDataToSave(aRelationshipListToSave, sContext, aRelationshipGridData, oExtraData);
    if (bSafeToSave) {
      return SettingUtils.csPostRequest(sUrl, {}, aRelationshipListToSave,
          successBulkSaveRelationshipList.bind(this, sContext, oCallBackData), failureBulkSaveRelationshipList);
    }
     return CommonUtils.rejectEmptyPromise();
  };

  let successBulkSaveRelationshipList = function (sContext, oCallbackData,oResponse) {
    let aRelationshipList = oResponse.success.list;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(sContext);
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(sContext, aRelationshipList);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let aRelationshipGridData = RelationshipProps.getRelationshipGridData();

    CS.forEach(aRelationshipList, function (oRelationship) {
      let sRelationshipId = oRelationship.id;
      let iIndex = CS.findIndex(aRelationshipGridData, {id: sRelationshipId});
      aRelationshipGridData[iIndex] = oRelationship;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedRelationship) {
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedRelationship.id});
      aGridViewData[iIndex] = oProcessedRelationship;
    });

    let bIsAnyRelationshipDirty = false;
    CS.forEach(aGridViewData, function (oRelationship) {
      if (oRelationship.isDirty) {
        bIsAnyRelationshipDirty = true;
      }
    });
    !bIsAnyRelationshipDirty && oGridViewPropsByContext.setIsGridDataDirty(false);

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().RELATIONSHIP}));
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  let failureBulkSaveRelationshipList = function (oResponse) {
    SettingUtils.failureCallback(oResponse,"failureBulkSaveRelationshipList",getTranslation());
  };

  let _discardRelationshipGridViewChanges = function (oCallbackData) {
    var aRelationshipGridData = RelationshipProps.getRelationshipGridData();
    let sContext = ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(sContext);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedRelationship, iIndex) {
      if (oOldProcessedRelationship.isDirty) {
        var oRelationship = CS.find(aRelationshipGridData, {id: oOldProcessedRelationship.id});
        aGridViewData[iIndex] =  GridViewStore.getProcessedGridViewData(sContext, [oRelationship])[0];
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

  var _handleExportRelationship = function (oSelectiveExportDetails) {
    SettingUtils.csPostRequest(oSelectiveExportDetails.sUrl, {}, oSelectiveExportDetails.oPostRequest, successExportFile, failureExportFile);
  };

  var successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
  };

  var failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
  };

  var _getIsValidFileTypes = function (oFile) {
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var uploadFileImport = function (oImportExcel, oCallback, sContext) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  var successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
  };

  var _handleRelationshipFileUploaded = function (aFiles, sContext, oImportExcel) {
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
                uploadFileImport(oImportExcel, {}, "relationship");
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsAnyInvalidImage = true;
        }
      });
    }
  };

  return {

    getSelectedRelationship: function () {
      return _getSelectedRelationship();
    },

    setSelectedRelationship: function (oRelationship) {
      _setSelectedRelationship(oRelationship);
    },

    getRelationshipScreenLockStatus: function () {
      return _getRelationshipScreenLockStatus();
    },

    fetchRelationshipsList: function () {
      _fetchRelationshipsList();
    },

    deleteRelationship: function (aSelectedIds, oCallBack) {
      let appData = SettingUtils.getAppData();
      let oMasterRelationships = appData.getRelationshipMasterList();
      let aBulkDeleteList =  aSelectedIds;
      let aBulkDeleteListLabels = [];
      let sGridViewContext = SettingScreenProps.screen.getGridViewContext();


      if (!CS.isEmpty(aBulkDeleteList)) {
        var bIsStandardRelationshipSelected = false;
        CS.forEach(aBulkDeleteList, function (RelId) {
          if (oMasterRelationships.hasOwnProperty(RelId) && oMasterRelationships[RelId].isStandard) {
            bIsStandardRelationshipSelected = true;
            return false;
          }
          let sMasterRelationshipLabel = CS.getLabelOrCode(oMasterRelationships[RelId]);
          aBulkDeleteListLabels.push(sMasterRelationshipLabel);
        });

        if (bIsStandardRelationshipSelected) {
          alertify.message(getTranslation().STANDARD_RELATIONSHIP_DELETION);
          return;
        }

        CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteListLabels,
            function () {
              _deleteRelationship(aBulkDeleteList, oCallBack)
              .then(_fetchRelationshipListForGridView.bind(this, sGridViewContext));
            }, function (oEvent) {
            }, true);
      } else {
        alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      }
    },

    saveRelationship: function (oCallback) {
      _saveRelationship(oCallback);
    },

    createNewRelationship: function (sGridViewContext) {
      _createNewRelationship(sGridViewContext);
    },

    discardUnsavedRelationship: function (oCallback) {
      _discardUnsavedRelationship(oCallback);
    },

    handleRelationshipListNodeClicked: function (oModel) {
      _handleRelationshipListNodeClicked(oModel);
    },

    handleRelationshipDataChanged: function (oNewModel, oReferencedKlasses) {
      _handleRelationshipDataChanged(oNewModel, oReferencedKlasses);
    },

    handleRelationshipConfigDialogButtonClicked: function (sButtonId) {
      if (sButtonId == "save") {
        _saveRelationship({});
      } else if (sButtonId == "cancel") {
        _discardUnsavedRelationship({})
      } else {
        _closeRelationshipDialog();
      }
    },

    handleRelationshipIconChanged: function (sIconKey, sIconObjectKey) {
      var oActivePropertyCollection = _makeActiveRelationshipDirty();
      RelationshipProps.setRelationshipScreenLockStatus(true);
      oActivePropertyCollection.icon = sIconKey ? sIconKey : "";
      oActivePropertyCollection.iconKey = sIconObjectKey;
      oActivePropertyCollection.showSelectIconDialog = false;
      _triggerChange();
    },

    handleRelationshipRefreshMenuClicked: function () {

      var oSelectedRelationship = _getSelectedRelationship();

      if (oSelectedRelationship.isDirty || oSelectedRelationship.isCreated) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = this.handleRelationshipRefreshMenuClicked;
        CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            _saveRelationship.bind(this, oCallbackData),
            _discardUnsavedRelationship.bind(this, oCallbackData),
            function () {
            });
      } else {
        var sSelectedRelationshipId = oSelectedRelationship.id;
        if (sSelectedRelationshipId) {
          _fetchRelationshipById(sSelectedRelationshipId);
        }
      }
    },

    handleSectionAdded: function (aSectionIds) {
      var oActiveRelationship = _makeActiveRelationshipDirty();
      oActiveRelationship.sections = [];
      SettingUtils.handleSectionAdded(oActiveRelationship, aSectionIds);
      _saveRelationship({});
    },

    handleSectionDeleted: function (sSectionId) {
      var oActiveRelationship = _makeActiveRelationshipDirty();
      SettingUtils.handleSectionDeleted(oActiveRelationship, sSectionId);
      _saveRelationship({});
    },

    addPropertyInRelationshipSide: function (sEntity, sSide, aItemIds, oReferencedData) {
      _addPropertyInRelationshipSide(sEntity, sSide, aItemIds, oReferencedData);
      _triggerChange();
    },

    handleRelationshipPropertyCouplingChanged: function (sSide, sPropertyId, sNewValue, sContext) {
      _changePropertyCoupling(sSide, sPropertyId, sNewValue, sContext);
      _triggerChange();
    },

    removePropertyInRelationshipSide: function (sSide, iItemID, sContext) {
      _removePropertyInRelationshipSide(sSide, iItemID, sContext);
      _triggerChange();
    },

    handleSelectionToggleButtonClicked: function(sKey, sId){
      _handleSelectionToggleButtonClicked(sKey, sId);
      _triggerChange();
    },

    handleRelationshipSingleValueChanged: function (sKey, oVal, oReferencedTabs) {
      _handleRelationshipSingleValueChanged(sKey, oVal, oReferencedTabs)
    },

    handleRelationshipTypeChanged: function (sTypeId) {
      _handleRelationshipTypeChanged(sTypeId)
    },

    handleListViewSearchOrLoadMoreClicked: function (sSearchText, bLoadMore) {
    },

    fetchRelationshipListForGridView: function (sSelectedConfigEntity){
      _fetchRelationshipListForGridView(sSelectedConfigEntity);
    },

    editButtonClicked: function (sEntityId) {
      _fetchRelationshipById(sEntityId);
    },

    setUpRelationshipConfigGridView: function (sSelectedConfigEntity) {
      return _setUpRelationshipConfigGridView(sSelectedConfigEntity);
    },

    postProcessRelationshipListAndSave: function(oCallBackData){
      _postProcessRelationshipListAndSave(oCallBackData)
          .then(_triggerChange);
    },

    discardRelationshipGridViewChanges: function(oCallBackData){
      _discardRelationshipGridViewChanges(oCallBackData);
    },

    resetPaginationData: function () {
      RelationshipProps.resetPaginationData();
    },

    handleExportRelationship: function (oSelectiveExportDetails) {
      _handleExportRelationship(oSelectiveExportDetails);
    },

    handleRelationshipFileUploaded: function (aFiles , sContext, oImportExcel) {
      _handleRelationshipFileUploaded(aFiles, sContext, oImportExcel);
    },
  }
})();

MicroEvent.mixin(RelationshipStore);

export default RelationshipStore;
