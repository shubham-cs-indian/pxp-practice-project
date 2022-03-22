import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import SettingUtils from './../helper/setting-utils';
import CommonUtils from './../../../../../../commonmodule/util/common-utils';
import ClassNameFromBaseTypeDictionary from '../../../../../../commonmodule/tack/class-name-base-types-dictionary';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import { AttributionTaxonomyRequestMapping as oAttributionTaxonomyRequestMapping } from '../../tack/setting-screen-request-mapping';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import AttributionTaxonomyListProps from './../model/attribution-taxonomy-config-view-props';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import assetTypes from '../../tack/coverflow-asset-type-list';
import ManageEntityStore from "./config-manage-entity-store";
import ManageEntityConfigProps from "../model/manage-entity-config-props";
import AttributionTaxonomyTabLayoutData from "../../tack/attribution-taxonomy-tab-layout-data";
import TaxonomyMasterListProps from "../model/taxonomy-master-list-config-view-props";

var AttributionTaxonomyStore = (function () {

  var _triggerChange = function () {
    AttributionTaxonomyStore.trigger('attribution-taxonomy-list-changed');
  };

  var _getDataModelForSave = function () {
    return {
      "addedLevel": {
        "id": "",
        "addedTag": {
          "label": "",
          "id": "",
          "isNewlyCreated": false,
          "tagValues": []
        }
      },
      "deletedLevel": null,
      "id": "",
      "addedDataRules": [],
      "deletedDataRules": [],
      "addedTasks": [],
      "deletedTasks": [],
      "addedContextKlasses": [],
      "deletedContextKlasses": [],
      "addedAppliedKlasses": [],
      "deletedAppliedKlasses": [],
      "appliedFilterData": {
        "addedTags": [],
        "deletedTags": [],
        "addedAttributes": [],
        "deletedAttributes": []
      },
      "appliedSortData": {
        "addedAttributes": [],
        "deletedAttributes": []
      },
      "addedAppliedDefaultFilters": [],
      "deletedAppliedDefaultFilters": [],
      "modifiedAppliedDefaultFilters": [],
      "modifiedElements": [],
      "addedSections": [],
      "deletedSections": [],
      "modifiedSections": [],
      "label": "",
      "icon": null,
      "parent": null,
      "code": ""
    }
  };

  var _makeActiveDetailedAttributionTaxonomyDirty = function () {
    let oActiveTaxonomy = AttributionTaxonomyListProps.getActiveDetailedTaxonomy();
    SettingUtils.makeObjectDirty(oActiveTaxonomy);
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(true);
    return (oActiveTaxonomy.clonedObject);
  };

  var _getCurrentDetailedAttributionTaxonomy = function () {
    let oActiveTaxonomy = AttributionTaxonomyListProps.getActiveDetailedTaxonomy();
    oActiveTaxonomy = oActiveTaxonomy.clonedObject || oActiveTaxonomy;
    return oActiveTaxonomy;
  };

  var _fetchAttributionTaxonomyList = function (bDoNotShowDetailedTaxonomy) {
    _fetchAttributionTaxonomy(SettingUtils.getTreeRootId(), null, null, bDoNotShowDetailedTaxonomy);
  };

  var _fetchAttributionTaxonomy = function (sId, iLevel, sActionId, bDoNotShowDetailedTaxonomy) {
    let sSelectedTabId = AttributionTaxonomyListProps.getAttributionTaxonomyListActiveTabId();
    let sTaxonomyType = "";
    if(sSelectedTabId === "Master_Taxonomies"){
      sTaxonomyType = "majorTaxonomy";
    }else if(sSelectedTabId === "Minor_Taxonomies"){
      sTaxonomyType = "minorTaxonomy";
    }

    let oCallbackData = {};
    oCallbackData.functionToExecute = _fetchAttributionTaxonomy.bind(this, sId, iLevel, sActionId);
    if (_getAttributionTaxonomyScreenLockStatus()) {
      CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveTaxonomyDetails.bind(this, oCallbackData),
          _discardTaxonomyDetails.bind(this, oCallbackData),
          function () {
          });
    } else {
      var oParameters = {};
      oParameters.id = sId;

      let oRequestData = {
        id: sId,
        taxonomyType: sTaxonomyType,
        getChildren :true,
      };

      iLevel = iLevel || 0;
      let sURL = oAttributionTaxonomyRequestMapping.GetTaxonomy;

      SettingUtils.csCustomPostRequest(RequestMapping.getRequestUrl(sURL,oParameters),  oRequestData,
          successFetchAttributionTaxonomyListCallback.bind(this, iLevel, sActionId, bDoNotShowDetailedTaxonomy), failureAttributionTaxonomyListCallback);
    }
  };

  var successFetchAttributionTaxonomyListCallback = function (iLevel, sActionId, bDoNotShowDetailedTaxonomy, oResponse) {
    let oSuccess  = oResponse.success;
    var oTaxonomy = oSuccess.entity;
    let oConfigDetails = oSuccess.configDetails || {};
    let oDetailedTaxonomy = {};
    if (!oTaxonomy.id) {
      oTaxonomy.id = -1;
      let oRootChildren = oTaxonomy.children;
      if (!CS.isEmpty(oRootChildren) && !bDoNotShowDetailedTaxonomy) {
        oDetailedTaxonomy = oRootChildren[0];
        _fetchAttributionTaxonomy(oDetailedTaxonomy.id, iLevel + 1);
      }
    } else {
      SettingUtils.setInheritanceAndCutOffUIProperties(oTaxonomy.sections);
      AttributionTaxonomyListProps.setActiveDetailedTaxonomy(oTaxonomy);
      _updateReferencedItems(oConfigDetails, oTaxonomy);
    }
    _prepareTaxonomyHierarchyData(oConfigDetails.referencedTaxonomies, oTaxonomy);

    var oAppData = SettingUtils.getAppData();
    var aAttributionTaxonomyList = oAppData.getAttributionTaxonomyList();
    var oExistingTaxonomy = SettingUtils.getNodeFromTreeListById(aAttributionTaxonomyList, oTaxonomy.id || -1);
    if (oExistingTaxonomy) {
      var oLinkedMasterTypeTags = AttributionTaxonomyListProps.getLinkedMasterTypeTags();
      CS.forEach(oTaxonomy.tagLevels, function (oTag) {
        var sTagId = oTag.tag.id;
        var oReferencedTag = oConfigDetails.referencedTags[sTagId];
        oLinkedMasterTypeTags[sTagId] = {
          id: sTagId,
          icon: oReferencedTag.icon,
          label: oReferencedTag.label,
          type: oReferencedTag.type,
          children: oReferencedTag.children
        };
      });
    }
    TaxonomyMasterListProps.setActiveTaxonomy(oTaxonomy);
    CS.assign(oExistingTaxonomy, oTaxonomy);

    var aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    aSelectedTaxonomyLevels.splice(iLevel, aSelectedTaxonomyLevels.length - iLevel, oTaxonomy.id);
    _triggerChange();
  };

  var failureAttributionTaxonomyListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureAttributionTaxonomyListCallback", getTranslation());
  };

  var _handleAttributionTaxonomyLevelActionItemClicked = function (iIndex, sItemId) {
    switch (sItemId) {
      case "create":
        if (_getAttributionTaxonomyScreenLockStatus()) {
          let oCallbackData = {};
          oCallbackData.functionToExecute = _createAttributionTaxonomy.bind(this,-1);
            CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
                _saveTaxonomyDetails.bind(this, oCallbackData),
                _discardTaxonomyDetails.bind(this, oCallbackData),
                function () {
                });
          }
          else {
          _createAttributionTaxonomy(-1);
        }
        iIndex += 1;
        AttributionTaxonomyListProps.setLevel(iIndex);
        break;
      case "delete":
        if (_getAttributionTaxonomyScreenLockStatus()) {
          CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
              _saveTaxonomyDetails.bind(this, {}),
              _discardTaxonomyDetails.bind(this, {}),
              function () {
              });
        } else {
          CustomActionDialogStore.showConfirmDialog(getTranslation().DELETE_CONFIRMATION, '',
              function () {
                _deleteLevel(iIndex, sItemId);
              }, function (oEvent) {
              });
        }
        break;
    }
  };

  var _createAttributionTaxonomy = function (sParentId) {
    var oDummyTaxonomyNode = _generateDummyTaxonomy(sParentId);

    if (sParentId == SettingUtils.getTreeRootId()) {
      _setActiveAttributionTaxonomy(oDummyTaxonomyNode);
      _triggerChange();
    } else {
      _createNewAttributionTaxonomy(oDummyTaxonomyNode);
    }
  };

  var _deleteLevel = function (iIndex) {
    var oTaxonomy = _getActiveAttributionTaxonomy();
    var oSavePostModel = _getDataModelForSave();

    oSavePostModel.id = oTaxonomy.id;
    oSavePostModel.label = oTaxonomy.label;
    oSavePostModel.parent = oTaxonomy.parent;
    oSavePostModel.icon = oTaxonomy.icon;
    oSavePostModel.code = oTaxonomy.code;
    oSavePostModel.addedLevel = null;

    var sLevelId = oTaxonomy.tagLevels[iIndex - 1].id;

    oSavePostModel.deletedLevel = sLevelId;

    _saveAttributionTaxonomy(oSavePostModel, iIndex);
  };

  var _setActiveAttributionTaxonomy = function (oActiveTaxonomy) {
    AttributionTaxonomyListProps.setActiveTaxonomy(oActiveTaxonomy)
  };

  var _getActiveAttributionTaxonomy = function () {
    var oActiveTaxonomy = {};
    var aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    if (aSelectedTaxonomyLevels[1]) {
      var oAppData = SettingUtils.getAppData();
      var aAttributionTaxonomyList = oAppData.getAttributionTaxonomyList();
      if (aAttributionTaxonomyList[0]) {
        oActiveTaxonomy = CS.find(aAttributionTaxonomyList[0].children, {id: aSelectedTaxonomyLevels[1]});
      }
    }
    return oActiveTaxonomy;
  };

  var _createNewAttributionTaxonomy = function (oDummyTaxonomyNode) {
    var oActiveTaxonomy = AttributionTaxonomyListProps.getActiveTaxonomy();
    if (CS.isEmpty(oDummyTaxonomyNode)) {
      oDummyTaxonomyNode = oActiveTaxonomy;
    }

    var oCodeToVerifyUniqueness = {
      id: oActiveTaxonomy.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY
    };
    if (CS.isEmpty(oActiveTaxonomy.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    var oServerCallback = {};
    var oCallbackData = {};
    oCallbackData.functionToExecute = _createAttributionTaxonomyCall.bind(this, oDummyTaxonomyNode, oServerCallback);

    var sURL = oAttributionTaxonomyRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackData);
  };

  var _createAttributionTaxonomyCall = function (oDummyTaxonomyNode, oServerCallback, sTaxonomyType) {
        let sURL = oAttributionTaxonomyRequestMapping.CreateTaxonomy;
    SettingUtils.csPutRequest(sURL, {}, oDummyTaxonomyNode, successCreateAttributionTaxonomy.bind(this, oServerCallback), failureCreateAttributionTaxonomyCallback.bind(this, oServerCallback));
  };

  var successCreateAttributionTaxonomy = function (oServerCallback, oResponse) {
    let oSuccess = oResponse.success;
    var oTaxonomy = oSuccess.entity;
    let oConfigDetails = oSuccess.configDetails;
    var oAppData = SettingUtils.getAppData();
    var sTaxonomyTab = AttributionTaxonomyListProps.getAttributionTaxonomyListActiveTabId();
    var aAttributionTaxonomyList = oAppData.getAttributionTaxonomyList();
    let sTaxonomyType = "majorTaxonomy";

    if(sTaxonomyTab === "Minor_Taxonomies" ){
      sTaxonomyType = "minorTaxonomy";
    }
    if(oTaxonomy.taxonomyType === sTaxonomyType){
      aAttributionTaxonomyList[0].children.unshift(oTaxonomy);
      _updateReferencedItems(oConfigDetails, oTaxonomy);//Used cloneDeep to avoid changes made by reference , As same object is being used.
      AttributionTaxonomyListProps.setActiveDetailedTaxonomy(CS.cloneDeep(oTaxonomy));
    }
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(false);
    _prepareTaxonomyHierarchyData(oConfigDetails.referencedTaxonomies, oTaxonomy);
    let oActiveTaxonomy = AttributionTaxonomyListProps.getActiveTaxonomy();
    delete oActiveTaxonomy.isNewlyCreated;
    _setActiveAttributionTaxonomy(oActiveTaxonomy)
    let sMessage = getTranslation().TAXONOMY_CREATED_SUCCESSFULLY;
    SettingUtils.showSuccess(sMessage);
    let iLevel = AttributionTaxonomyListProps.getLevel();
    let aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    aSelectedTaxonomyLevels.splice(iLevel, aSelectedTaxonomyLevels.length - iLevel, oTaxonomy.id);
    _triggerChange();
  };

  var failureCreateAttributionTaxonomyCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCreateAttributionTaxonomyCallback", getTranslation());
  };

  var _generateDummyTaxonomy = function (sParentId, sLabel) {

    return {
      taxonomyId: null,
      label: sLabel || UniqueIdentifierGenerator.generateUntitledName(),
      parentTaxonomyId: sParentId,
      parentTagId: null,
      tagValueId: null,
      isNewlyCreated: true,
      taxonomyType: sParentId == -1 ? "majorTaxonomy" : null,
      code: ""
    };
  };

  var _handleTaxonomyNameChanged = function (sKey, sNewValue) {
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(true);
    var oActiveTaxonomy = AttributionTaxonomyListProps.getActiveTaxonomy();
    if (sKey == "taxonomyType") {
      if (sNewValue == true) {
        oActiveTaxonomy[sKey] = "majorTaxonomy";
      } else {
        oActiveTaxonomy[sKey] = "minorTaxonomy";
      }
      return
    }
    oActiveTaxonomy[sKey] = sNewValue;
  };

  var _handleTaxonomyLevelSingleValueChanged = function (sKey, sNewValue, sContext) {
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(true);
    if(sContext == "attributionTaxonomyLevelChildren") {
      var oActiveTaxonomyLevelChildren = AttributionTaxonomyListProps.getActiveTaxonomyLevelChildren();
      oActiveTaxonomyLevelChildren[sKey] = sNewValue;
    }
    else{
      var oActiveTaxonomyLevel = AttributionTaxonomyListProps.getActiveTaxonomyLevel();
      var oActiveTag = oActiveTaxonomyLevel.addedLevel.addedTag;
      oActiveTag[sKey]=sNewValue;
    }
  };

  var _updateTreeField = function (sId, sKey, sValue) {
    let aAttributionTaxonomyList = SettingUtils.getAppData().getAttributionTaxonomyList();
    let aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    let oAllowedTaxonomyConfigDetails = AttributionTaxonomyListProps.getAllowedTaxonomyConfigDetails();
    let oAttributionTaxonomy = {};
    CS.forEach(aSelectedTaxonomyLevels, function (sTaxonomyLevel) {
      oAttributionTaxonomy = CS.find(aAttributionTaxonomyList, {id: sTaxonomyLevel});
      if (oAttributionTaxonomy.id == sId) {
        return;
      }
      aAttributionTaxonomyList = oAttributionTaxonomy.children;
    });
    if (oAttributionTaxonomy) {
      oAttributionTaxonomy[sKey] = sValue;
      let oTaxonomy = CS.find(oAllowedTaxonomyConfigDetails, {id: oAttributionTaxonomy.id});
      oTaxonomy[sKey] = sValue;
      _prepareTaxonomyHierarchyData(oAllowedTaxonomyConfigDetails, oAttributionTaxonomy);
    }
  };

  var _handleDetailedTaxonomySingleValueChanged = function (sKey, sNewValue) {
    let oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    if (sKey == "taxonomyType") {
      if (sNewValue == true) {
        oActiveTaxonomy[sKey] = "majorTaxonomy";
      } else {
        oActiveTaxonomy[sKey] = "minorTaxonomy";
      }
      return
    } else if (sKey == "label") {
      _updateTreeField(oActiveTaxonomy.id, sKey, sNewValue);
    }
    oActiveTaxonomy[sKey] = sNewValue;
    _triggerChange();
  };

  var _handleDetailedTaxonomyMultipleValueChanged = function (sKey, aSelectedItems, oReferencedData) {
    let oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    if (sKey == "appliedSortData") {
      oActiveTaxonomy.appliedSortData.attributes = aSelectedItems;
    } else {
      oActiveTaxonomy[sKey] = aSelectedItems;
      if (sKey === "embeddedKlassIds") {
        CS.forEach(oReferencedData, function (oData, sId) {
          let oReferencedKlasses = oActiveTaxonomy.referencedKlasses;
          if (!oReferencedKlasses[sId]) {
            oReferencedKlasses[sId] = oData;
            oReferencedKlasses[sId].propagableAttributes = [];
            oReferencedKlasses[sId].propagableTags = [];
          }
        });
      }
    }
    _triggerChange();
  };

  var _handleAttributionTaxonomyCreateDialogButtonClicked = function (sContext, sIsMajorMinor, sLabel) {
    var oActiveTaxonomy = AttributionTaxonomyListProps.getActiveTaxonomy();
    switch (sContext) {
      case "create":
        oActiveTaxonomy.taxonomyType = sIsMajorMinor;
        oActiveTaxonomy.label = sLabel;
        _createNewAttributionTaxonomy({});
        break;
      case "cancel":
        _setActiveAttributionTaxonomy({});
        AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(false);
        break;
    }
    _triggerChange();
  };

  var _handleMSSValueChanged = function (bIsSingleSelect, sKey, aSelectedItems, oReferencedData) {
    if (bIsSingleSelect === "true") {
      _handleDetailedTaxonomySingleValueChanged(sKey, aSelectedItems[0]);
    } else {
      _handleDetailedTaxonomyMultipleValueChanged(sKey, aSelectedItems, oReferencedData);
    }
  };

  var _handleMSSVCrossIconClicked = function (bIsSingleSelect, sKey, sId) {
    if (bIsSingleSelect === "true") {
      _handleDetailedTaxonomySingleValueChanged(sKey, "");
    } else {
      let oActiveDetailedTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
      CS.pull(oActiveDetailedTaxonomy[sKey], sId);
      _triggerChange();
    }
  };

  var _handleSectionsUpdated = function (aSectionIds, bIsAdded) {
    var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    if (bIsAdded) {
      SettingUtils.handleSectionAdded(oActiveTaxonomy, aSectionIds);
    } else {
      SettingUtils.handleSectionDeleted(oActiveTaxonomy, aSectionIds);
    }
    _saveTaxonomyDetails();
  };

  let _getClassConfigEmbeddedKlassesADM = function (oTaxonomy) {
    var aOldEmbeddedKlasses = oTaxonomy.embeddedKlassIds;
    var aNewEmbeddedKlasses = oTaxonomy.clonedObject.embeddedKlassIds;

    let aAddedEmbeddedKlasses = CS.difference(aNewEmbeddedKlasses, aOldEmbeddedKlasses);
    let aAddedKlasses = [];
    CS.forEach(aAddedEmbeddedKlasses, function (sId) {
      let oAddedKlass = {
        contextKlassId: sId,
        attributes: oTaxonomy.clonedObject.referencedKlasses[sId].propagableAttributes,
        tags: oTaxonomy.clonedObject.referencedKlasses[sId].propagableTags,
      };
      aAddedKlasses.push(oAddedKlass);
    });

    let aModifiedEmbeddedKlasses = CS.intersection(aOldEmbeddedKlasses, aNewEmbeddedKlasses);
    let aModifiedKlasses = [];
    CS.forEach(aModifiedEmbeddedKlasses, function (sId) {
      let bIsModified = false;
      let oModifiedKlass = {
        contextKlassId: sId,
        addedAttributes: [],
        deletedAttributes: [],
        modifiedAttributes: [],
        addedTags: [],
        deletedTags: [],
        modifiedTags: []
      };
      let oNewEmbeddedKlass = oTaxonomy.clonedObject.referencedKlasses[sId];
      let oOldEmbeddedKlass = oTaxonomy.referencedKlasses[sId];
      if (!CS.isEqual(oNewEmbeddedKlass.propagableAttributes, oOldEmbeddedKlass.propagableAttributes)) {
        oModifiedKlass.addedAttributes = CS.differenceBy(oNewEmbeddedKlass.propagableAttributes, oOldEmbeddedKlass.propagableAttributes, "id");
        oModifiedKlass.deletedAttributes = CS.map(CS.differenceBy(oOldEmbeddedKlass.propagableAttributes, oNewEmbeddedKlass.propagableAttributes, "id"), "id");
        oModifiedKlass.modifiedAttributes = CS.filter(oNewEmbeddedKlass.propagableAttributes, function (oAttribute) {
              var oOldValue = CS.find(oOldEmbeddedKlass.propagableAttributes, {id: oAttribute.id});
              return oOldValue && oOldValue.couplingType != oAttribute.couplingType;
            }
        );
        bIsModified = true;
      }
      if (!CS.isEqual(oNewEmbeddedKlass.propagableTags, oOldEmbeddedKlass.propagableTags)) {
        oModifiedKlass.addedTags = CS.differenceBy(oNewEmbeddedKlass.propagableTags, oOldEmbeddedKlass.propagableTags, "id");
        oModifiedKlass.deletedTags = CS.map(CS.differenceBy(oOldEmbeddedKlass.propagableTags, oNewEmbeddedKlass.propagableTags, "id"), "id");
        oModifiedKlass.modifiedTags = CS.filter(oNewEmbeddedKlass.propagableTags, function (oTag) {
              var oOldValue = CS.find(oOldEmbeddedKlass.propagableTags, {id: oTag.id});
              return oOldValue && oOldValue.couplingType != oTag.couplingType;
            }
        );
        bIsModified = true;
      }
      if (bIsModified) {
        aModifiedKlasses.push(oModifiedKlass);
      }
    });

    delete oTaxonomy.clonedObject.referencedKlasses;
    return {
      addedContextKlasses: aAddedKlasses,
      modifiedContextKlasses: aModifiedKlasses,
      deletedContextKlasses: CS.map(CS.difference(aOldEmbeddedKlasses, aNewEmbeddedKlasses))
    }
  };

  var _generateADMForSaveDetailedTaxonomy = function () {
    var oADM = {};
    var oOld = AttributionTaxonomyListProps.getActiveDetailedTaxonomy();
    var oNew = oOld.clonedObject;

    oADM.addedAppliedKlasses = CS.difference(oNew.appliedKlasses, oOld.appliedKlasses);
    oADM.deletedAppliedKlasses = CS.difference(oOld.appliedKlasses, oNew.appliedKlasses);

    oADM.addedTasks = CS.difference(oNew.tasks, oOld.tasks);
    oADM.deletedTasks = CS.difference(oOld.tasks, oNew.tasks);
    oADM.addedDataRules = CS.difference(oNew.dataRules, oOld.dataRules);
    oADM.deletedDataRules = CS.difference(oOld.dataRules, oNew.dataRules);

    var oEmbeddedKlassesADM = _getClassConfigEmbeddedKlassesADM(oOld);
    oADM.addedContextKlasses = oEmbeddedKlassesADM.addedContextKlasses;
    oADM.deletedContextKlasses = oEmbeddedKlassesADM.deletedContextKlasses;
    oADM.modifiedContextKlasses = oEmbeddedKlassesADM.modifiedContextKlasses;

    oADM.addedAppliedDefaultFilters = CS.differenceBy(oNew.appliedDefaultFilters, oOld.appliedDefaultFilters, 'id');
    oADM.deletedAppliedDefaultFilters = CS.map(CS.differenceBy(oOld.appliedDefaultFilters, oNew.appliedDefaultFilters, 'id'), 'id');
    var oModifiedDefaultFilters = [];
    var oNewCommonElements = CS.intersectionBy(oNew.appliedDefaultFilters, oOld.appliedDefaultFilters, 'id');
    CS.forEach(oNewCommonElements, function (oNewFilter) {
      var oOldFilter = CS.find(oOld.appliedDefaultFilters, {id: oNewFilter.id});

      var oModifiedElement = {};
      oModifiedElement.id = oNewFilter.id;
      oModifiedElement.addedTagValues = [];
      oModifiedElement.modifiedTagValues = [];
      oModifiedElement.deletedTagValues = [];
      var aUncommonElementsInNewFilter = CS.differenceWith(oNewFilter.tagValues, oOldFilter.tagValues, CS.isEqual);
      var aUncommonElementsInOldFilter = CS.differenceWith(oOldFilter.tagValues, oNewFilter.tagValues, CS.isEqual);
      var aListOfModifiedElements = CS.concat(aUncommonElementsInNewFilter, aUncommonElementsInOldFilter);
      aListOfModifiedElements = CS.uniq(aListOfModifiedElements, 'tagId');
      CS.forEach(aListOfModifiedElements, function (oModifiedElementValue) {
        var bIsPresentInNewFilter = CS.includes(oNewFilter.tagValues, oModifiedElementValue);
        var bIsPresentInOldFilter = CS.includes(oOldFilter.tagValues, oModifiedElementValue);
        if (bIsPresentInOldFilter && bIsPresentInNewFilter) {
          oModifiedElement.modifiedTagValues.push(oModifiedElementValue);
        } else if (!bIsPresentInNewFilter && bIsPresentInOldFilter) {
          var sDeletedElementId = oModifiedElementValue.tagId;
          oModifiedElement.deletedTagValues.push(sDeletedElementId);
        } else if (bIsPresentInNewFilter && !bIsPresentInOldFilter) {
          oModifiedElement.addedTagValues.push(oModifiedElementValue);
        }
      });
      if (!(CS.isEmpty(oModifiedElement.addedTagValues) && CS.isEmpty(oModifiedElement.deletedTagValues)
              && CS.isEmpty(oModifiedElement.modifiedTagValues))) {
        oModifiedDefaultFilters.push(oModifiedElement);
      }
    });
    oADM.modifiedAppliedDefaultFilters = oModifiedDefaultFilters;


    var sSplitter = SettingUtils.getSplitter();
    var oClassClone = CS.cloneDeep(oOld);

    var aOldSections = oClassClone.sections;
    var aNewSections = oClassClone.clonedObject.sections;
    oADM.modifiedElements = [];
    var oSectionADMObject = {
      added: [],
      deleted: [],
      modified: []
    };


    CS.forEach(aNewSections, function (oNewSection) {
      var oOldSection = CS.remove(aOldSections, function (oSection) {
        return oSection.id == oNewSection.id
      });
      oOldSection = oOldSection[0];

      //if section found in old version
      if (oOldSection) {
        var oElementADMObject = {
          added: [],
          deleted: [],
          modified: []
        };
        var aOldSectionElements = oOldSection.elements;
        var aNewSectionElements = oNewSection.elements;

        var bIsSectionModified = (!CS.isEqual(oNewSection.sequence, oOldSection.sequence) || !CS.isEqual(oNewSection.isSkipped, oOldSection.isSkipped));
        oNewSection.isModified = bIsSectionModified;

        //iterating on new section elements
        CS.forEach(aNewSectionElements, function (oNewSectionElement) {
          var oOldSectionElement = CS.remove(aOldSectionElements, {id: oNewSectionElement.id});

          //if element found in old version
          if (oOldSectionElement.length > 0) {

            //if element is not modified
            var bIsElementModified = !CS.isEqual(oOldSectionElement[0], oNewSectionElement);
            if (bIsElementModified) {
              if (oNewSectionElement.type == "tag") {
                oNewSectionElement.addedDefaultValues = CS.differenceBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId");
                oNewSectionElement.deletedDefaultValues = CS.map(CS.differenceBy(oOldSectionElement[0].defaultValue, oNewSectionElement.defaultValue, "tagId"), "tagId");
                oNewSectionElement.modifiedDefaultValues = CS.filter(
                    CS.intersectionBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId"),
                    function (oDefaultValue) {
                      var oOldDefaultValue = CS.find(oOldSectionElement[0].defaultValue, {tagId: oDefaultValue.tagId});
                      return oOldDefaultValue && oOldDefaultValue.relevance != oDefaultValue.relevance;
                    }
                );
                delete oNewSectionElement.defaultValue;

                oNewSectionElement.addedSelectedTagValues = [];
                oNewSectionElement.deletedSelectedTagValues = [];
                let aAddedSelectedTagValues = CS.differenceBy(oNewSectionElement.selectedTagValues, oOldSectionElement[0].selectedTagValues, "tagId");
                CS.forEach(aAddedSelectedTagValues, function (oValue) {
                  oNewSectionElement.addedSelectedTagValues.push(oValue.tagId);
                });
                oNewSectionElement.deletedSelectedTagValues = CS.map(CS.differenceBy(oOldSectionElement[0].selectedTagValues, oNewSectionElement.selectedTagValues, "tagId"), "tagId");

                delete oNewSectionElement.selectedTagValues;
              }

              oElementADMObject.modified.push(oNewSectionElement);
              oADM.modifiedElements.push(oNewSectionElement);
              oNewSectionElement.isModified = bIsElementModified;
              // bIsSectionModified = true;
            }
          } else {
            //not found in old so add to added list
            oNewSectionElement.id = oNewSectionElement.id.split(sSplitter)[0];
            oElementADMObject.added.push(oNewSectionElement);
            // bIsSectionModified = true;
          }
        });

        var aDeletedElements = CS.map(aOldSectionElements, 'id');
        // bIsSectionModified = bIsSectionModified || aDeletedElements.length > 0;
        CS.merge(oElementADMObject.deleted, aDeletedElements);

        oNewSection.addedElements = oElementADMObject.added;
        oNewSection.deletedElements = oElementADMObject.deleted;
        oNewSection.modifiedElements = oElementADMObject.modified;
        // oADM.modifiedElements = oADM.modifiedElements.concat(oElementADMObject.modified);
        delete oNewSection.elements;

        if (bIsSectionModified) {
          oSectionADMObject.modified.push(oNewSection);
        }

      } else {
        // oNewSection.propertyCollectionId = oNewSection.id.split(sSplitter)[0];
        // oNewSection.id = null;
        oSectionADMObject.added.push(oNewSection);
      }
    });

    var aDeletedSections = CS.map(aOldSections, 'id');
    CS.merge(oSectionADMObject.deleted, aDeletedSections);

    oADM.addedSections = oSectionADMObject.added;
    oADM.deletedSections = oSectionADMObject.deleted;
    oADM.modifiedSections = [];
    CS.forEach(oSectionADMObject.modified, function (oModifiedSection) {
      oADM.modifiedSections.push({
        id: oModifiedSection.id,
        sequence: oModifiedSection.sequence,
        isSkipped: oModifiedSection.isSkipped,
        isModified: oModifiedSection.isModified
      });
      oADM.modifiedElements = oADM.modifiedElements.concat(oModifiedSection.modifiedElements);
    });

    oADM.id = oNew.id;
    oADM.label = oNew.label;
    oADM.icon = oNew.icon;
    oADM.parent = oNew.parent;
    oADM.code = oNew.code;

    return oADM;
  };

  var _saveTaxonomyDetails = function (oCallBack) {
    var oActiveTaxonomy = AttributionTaxonomyListProps.getActiveDetailedTaxonomy();
    if (!oActiveTaxonomy.clonedObject) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      _triggerChange();
      return;
    }
    var oClonedActiveTaxonomy = oActiveTaxonomy.clonedObject;
    if (CS.isEmpty(oClonedActiveTaxonomy.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    var oADMToSave = _generateADMForSaveDetailedTaxonomy();

    SettingUtils.csPostRequest(oAttributionTaxonomyRequestMapping.SaveTaxonomy, {}, oADMToSave, successSaveAttributionTaxonomyDetails.bind(this, oCallBack), failureSaveAttributionTaxonomyDetails);
  };

  var successSaveAttributionTaxonomyDetails = function (oCallBack, oResponse) {
    let oSuccess  = oResponse.success;
    var oTaxonomyFromServer = oSuccess.entity;
    let oConfigDetails = oSuccess.configDetails;

    AttributionTaxonomyListProps.setActiveDetailedTaxonomy(oTaxonomyFromServer);
    SettingUtils.setInheritanceAndCutOffUIProperties(oTaxonomyFromServer.sections);
    _updateReferencedItems(oConfigDetails, oTaxonomyFromServer);
    _prepareTaxonomyHierarchyData(oConfigDetails.referencedTaxonomies, oTaxonomyFromServer);
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(false);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TAXONOMY}));
    if (oCallBack && oCallBack.functionToExecute) {
      oCallBack.functionToExecute();
    }
    _triggerChange()
  };

  var failureSaveAttributionTaxonomyDetails = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveAttributionTaxonomyDetails", getTranslation());
  };

  var _updateReferencedItems = function (oConfigDetails, oActiveTaxonomy) {
    let oReferencedObject = {
      referencedTasks: oConfigDetails.referencedTasks,
      referencedDataRules: SettingUtils.convertReferencedInObjectFormat(oConfigDetails.referencedDataRules),
      referencedKlasses: oConfigDetails.referencedKlasses,
      referencedTags: oConfigDetails.referencedTags,
      referencedAttributes: oConfigDetails.referencedAttributes,
      referencedContexts: oConfigDetails.referencedContexts
    };

    //to update the referenced classes for contextual data transfer
    CS.forEach(oReferencedObject.referencedKlasses, function (oReferencedKlass) {
      oReferencedKlass.propagableAttributes = CS.values(oReferencedKlass.propagableAttributes);
      oReferencedKlass.propagableTags = CS.values(oReferencedKlass.propagableTags);
    });

    oActiveTaxonomy.referencedKlasses = {};
    CS.assign(oActiveTaxonomy.referencedKlasses, oReferencedObject.referencedKlasses);

    AttributionTaxonomyListProps.setReferencedData(oReferencedObject);
  };

  let _discardTaxonomyDetails = function (oCallbackData) {
    var oActiveTaxonomy = AttributionTaxonomyListProps.getActiveDetailedTaxonomy();
    if (oActiveTaxonomy.clonedObject) {
      delete oActiveTaxonomy.clonedObject;
      delete oActiveTaxonomy.isDirty;
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    }
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(false);
    _discardTreeNodeChanges(oActiveTaxonomy);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let _discardTreeNodeChanges = function (oActiveTaxonomy) {
    let aAttributionTaxonomyList = SettingUtils.getAppData().getAttributionTaxonomyList();
    let aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    let oAllowedTaxonomyConfigDetails = AttributionTaxonomyListProps.getAllowedTaxonomyConfigDetails();
    let oAttributionTaxonomy = {};
    CS.forEach(aSelectedTaxonomyLevels, function (sTaxonomyLevel) {
      oAttributionTaxonomy = CS.find(aAttributionTaxonomyList, {id: sTaxonomyLevel});
      if (oAttributionTaxonomy.id == oActiveTaxonomy.id) {
        return;
      }
      aAttributionTaxonomyList = oAttributionTaxonomy.children;
    });
    if (oAttributionTaxonomy) {
      CS.assign(oAttributionTaxonomy, oActiveTaxonomy);
      let oTaxonomy = CS.find(oAllowedTaxonomyConfigDetails, {id: oAttributionTaxonomy.id});
      oTaxonomy['label'] = oAttributionTaxonomy.label;
      _prepareTaxonomyHierarchyData(oAllowedTaxonomyConfigDetails, oAttributionTaxonomy);
    }
  };

  var _handleDetailedTaxonomyUploadIconChangeEvent = function (sIconKey, sIconObjectKey) {
    var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    oActiveTaxonomy.icon = sIconKey;
    oActiveTaxonomy.iconKey = sIconObjectKey;
    oActiveTaxonomy.showSelectIconDialog = false;
    _updateTreeField(oActiveTaxonomy.id, "iconKey", sIconObjectKey);
  };

  var _handleDetailedTaxonomyActionItemClicked = function (sContext) {
    if (sContext === "save") {
      _saveTaxonomyDetails();
    }
    else {
      _discardTaxonomyDetails()
    }
  };

  var _handleSelectionToggleButtonClicked = function (sKey, sId, bSingleSelect) {
    var oActiveDetailTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    if (bSingleSelect) {
      if (oActiveDetailTaxonomy[sKey]) {
        oActiveDetailTaxonomy[sKey] = "";
      } else {
        oActiveDetailTaxonomy[sKey] = sId;
      }
    } else {
      var aSelectedItems = oActiveDetailTaxonomy[sKey] || [];
      var aRemovedIds = CS.remove(aSelectedItems, function (sSelectedId) {
        return sSelectedId === sId;
      });

      if (CS.isEmpty(aRemovedIds)) {
        aSelectedItems.push(sId);
      }
      oActiveDetailTaxonomy[sKey] = aSelectedItems;
    }
    _triggerChange()
  };

  var _handleAttributionTaxonomyListLevelAdded = function (sSelectedId, bIsNewlyCreated, sLabel) {
    var oTaxonomy = _getActiveAttributionTaxonomy();
    var oSavePostModel = _getDataModelForSave();

    oSavePostModel.id = oTaxonomy.id;
    oSavePostModel.label = oTaxonomy.label;
    oSavePostModel.parent = oTaxonomy.parent;
    oSavePostModel.icon = oTaxonomy.icon;
    oSavePostModel.code = oTaxonomy.code;
    oSavePostModel.addedLevel.id = null;
    if (bIsNewlyCreated) {
      oSavePostModel.addedLevel.addedTag.id = null;
      oSavePostModel.addedLevel.addedTag.label = sLabel;
      oSavePostModel.addedLevel.addedTag.isNewlyCreated = true;
      oSavePostModel.addedLevel.addedTag.code = "";
      AttributionTaxonomyListProps.setActiveTaxonomyLevel(oSavePostModel);
      AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(true);
      _triggerChange();
    } else {
      oSavePostModel.addedLevel.addedTag.id = sSelectedId;
      _saveAttributionTaxonomy(oSavePostModel, oSavePostModel.addedLevel.id);
    }
  };

  var _saveAttributionTaxonomy = function (oPostObj, iIndex) {

    let sURL = oAttributionTaxonomyRequestMapping.SaveTaxonomy;
    SettingUtils.csPostRequest(sURL, {}, oPostObj, successSaveAttributionTaxonomy.bind(this, oPostObj, iIndex), failureSaveAttributionTaxonomy);
  };

  var successSaveAttributionTaxonomy = function (oPostObj, iIndex, oResponse) {
    var oTaxonomy = _getActiveAttributionTaxonomy();
    var oTagLevels = oTaxonomy.tagLevels;
    var oSuccess = oResponse.success;
    let oTaxonomyFromServer = oSuccess.entity;
    let oConfigDetails = oSuccess.configDetails;

    if (oPostObj.deletedLevel == null && oPostObj.addedLevel == null) {
      var aTaxonomyList = _getLevelChildrenByIndex(iIndex);
      var oFoundTaxonomy = CS.find(aTaxonomyList, {id: oTaxonomyFromServer.id});
      oTaxonomyFromServer = CS.omit(oTaxonomyFromServer, "children");
      CS.assign(oFoundTaxonomy, oTaxonomyFromServer);

    } else {
      if (oPostObj.deletedLevel) {
        var iLevel = null;

        CS.forEach(oTagLevels, function (oTagLevel, iLvl) {
          if (oTagLevel.id == oPostObj.deletedLevel) {
            iLevel = iLvl + 1;
            return false;
          }
        });

        if (iLevel) {
          var oLastSelectedTaxonomyNode = _getSelectedAttributionTaxonomyByLevel(iLevel);
          AttributionTaxonomyListProps.setActiveDetailedTaxonomy(oLastSelectedTaxonomyNode);
          oLastSelectedTaxonomyNode.children = [];
        }

        oTaxonomy.tagLevels = oTaxonomyFromServer.tagLevels;
        let sMessage = getTranslation().ATTRIBUTION_TAXONOMY_LEVEL_DELETED_SUCCESSFULLY;
        SettingUtils.showSuccess(sMessage);
      } else if (oPostObj.addedLevel) {
        var oLinkedMasterTypeTags = AttributionTaxonomyListProps.getLinkedMasterTypeTags();
        oTaxonomy.tagLevels = oTaxonomyFromServer.tagLevels;
        if (oPostObj.addedLevel.addedTag.isNewlyCreated) {
          var iLevel = oTaxonomy.tagLevels.length;
          var sNewTagId = oTaxonomy.tagLevels[iLevel - 1].tag.id;
          oLinkedMasterTypeTags[sNewTagId] = {
            id: sNewTagId,
            type: oConfigDetails.referencedTags[sNewTagId].type,
            icon: oConfigDetails.referencedTags[sNewTagId].icon,
            label: oConfigDetails.referencedTags[sNewTagId].label
          }
        }

        CS.forEach(oTaxonomy.tagLevels, function (oTag) {
          var sTagId = oTag.tag.id;
          var oReferencedTag = oConfigDetails.referencedTags[sTagId];
          oLinkedMasterTypeTags[sTagId] = {
            id: sTagId,
            icon: oReferencedTag.icon,
            label: oReferencedTag.label,
            type: oReferencedTag.type,
            children: oReferencedTag.children
          };
        });
      }
    }
    AttributionTaxonomyListProps.setIsAttributionTaxonomyHierarchyScrollAutomaticallyEnabled(true);
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(false);
    AttributionTaxonomyListProps.setActiveTaxonomyLevel({});
    _triggerChange();
  };

  var failureSaveAttributionTaxonomy = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureAddDeleteTaxonomyLevel", getTranslation());
  };

  var _getSelectedAttributionTaxonomyByLevel = function (iLevel) {
    var oAppData = SettingUtils.getAppData();
    var aAttributionTaxonomyList = oAppData.getAttributionTaxonomyList();
    var aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    var oFound = null;
    for (var i = 0; i <= iLevel; i++) {
      oFound = CS.find(aAttributionTaxonomyList, {id: aSelectedTaxonomyLevels[i]}) || {};
      aAttributionTaxonomyList = oFound.children || [];

    }
    return oFound;
  };

  var _handleAttributionTaxonomyLevelItemClicked = function (iIndex, sItemId, sActionId) {
    iIndex += 1;
    _fetchAttributionTaxonomy(sItemId, iIndex, sActionId);
  };

  var _handleAttributionTaxonomyLevelChildActionItemClicked = function (iIndex, sActionId, sChildId) {
    let sType = ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY;
    ManageEntityConfigProps.setIsDelete(true);
    deleteTaxonomy(iIndex, sActionId, sChildId, { functionToExecute: ManageEntityStore.handleManageEntityDialogOpenButtonClicked.bind(this, sChildId, sType) });
  };

  let deleteTaxonomy = function (iIndex,sActionId,sChildId, oCallback) {
    //let bCanDeleteEntity = ManageEntityConfigProps.getDataForDeleteEntity();
    switch (sActionId) {
      case "delete":
        if (_getAttributionTaxonomyScreenLockStatus()) {
          let oCallbackData = {
            functionToExecute: deleteTaxonomy.bind(this, iIndex,sActionId,sChildId, oCallback)
          };
          CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
              _saveTaxonomyDetails.bind(this, oCallbackData),
              _discardTaxonomyDetails.bind(this, oCallbackData),
              function () {
              });
        } else {
          CustomActionDialogStore.showConfirmDialog(getTranslation().DELETE_CONFIRMATION, '',
              function () {
            oCallback.index = iIndex;
                _deleteAttributionTaxonomy([sChildId], oCallback);
              }, function (oEvent) {
              }, "", true);
        }
        break;
      case "view":
        _handleAttributionTaxonomyLevelItemClicked(iIndex, sChildId, sActionId);
        break;
    }
  }

  var _deleteAttributionTaxonomy = function (aTaxonomiesToDelete, oCallback) {
    var oDataToDelete = {};
    oDataToDelete.ids = aTaxonomiesToDelete;
    let sURL = oAttributionTaxonomyRequestMapping.DeleteTaxonomy;
    SettingUtils.csDeleteRequest(sURL, {}, oDataToDelete, successDeleteAttributionTaxonomy.bind(this, oCallback), failureDeleteAttributionTaxonomyCallback.bind(this, oCallback));
  };

  var successDeleteAttributionTaxonomy = function (oCallback,oResponse) {
    var aDeletedIds = oResponse.success;
    var oAppData = SettingUtils.getAppData();
    let oActiveTaxonomy = AttributionTaxonomyListProps.getActiveDetailedTaxonomy();
    var aAttributionTaxonomyList = oAppData.getAttributionTaxonomyList();
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnTreeNodeDelete(aAttributionTaxonomyList, oActiveTaxonomy.id, aDeletedIds[0]);

    CS.forEach(aDeletedIds, function (sDeletedId) {
      var aSelectedTaxonomies = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
      CS.forEach(aSelectedTaxonomies, function (sSelectedId) {
        var oTaxonomy = CS.find(aAttributionTaxonomyList, {id: sSelectedId});
        if (!oTaxonomy) {
          return false;
        }
        var aRemoved = CS.remove(oTaxonomy.children, {id: sDeletedId});
        if (CS.isEmpty(aRemoved)) {
          aAttributionTaxonomyList = oTaxonomy.children;
        } else {
          var iIndex = CS.findIndex(aSelectedTaxonomies, function (sId) {
            return sId == sDeletedId
          });
          if (iIndex != -1) {
            aSelectedTaxonomies.splice(iIndex);
          }
          return false;
        }
      });
    });
    CS.isNotEmpty(oNewActiveNode) && _fetchAttributionTaxonomy(oNewActiveNode.id, oCallback.index + 1);
    (CS.isEmpty(aAttributionTaxonomyList[0].children)) && AttributionTaxonomyListProps.setActiveDetailedTaxonomy("");
    let sMessage = SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().ATTRIBUTIONTAXONOMY});
    SettingUtils.showSuccess(sMessage);
    _triggerChange();
  };

  var failureDeleteAttributionTaxonomyCallback = function (oCallback, oResponse) {
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
    SettingUtils.failureCallback(oResponse, "failureDeleteAttributionTaxonomyCallback", getTranslation());
  };

  var _handleTaxonomyLevelMasterListChildrenAdded = function (iIndex, aCheckedItems, sNewLabel) {
    var oActiveTaxonomy = _getActiveAttributionTaxonomy();
    var aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    var oTagLevels = oActiveTaxonomy.tagLevels;
    var sMasterListParentId = oTagLevels[iIndex - 1].tag.id;
    var sParentId = aSelectedTaxonomyLevels[iIndex];
    var oMasterListMap = AttributionTaxonomyListProps.getLinkedMasterTypeTags();
    var oMasterListParent = oMasterListMap[sMasterListParentId];
    var aTaxonomiesToSave = [];
    if (aCheckedItems.length) {
      CS.forEach(aCheckedItems, function (sCheckedId) {
        var oChild = CS.find(oMasterListParent.children, {id: sCheckedId}) || {};
        var oTaxonomy = _generateDummyTaxonomy(sParentId, oChild.label);
        oTaxonomy.parentTagId = sMasterListParentId;
        oTaxonomy.tagValueId = sCheckedId;
        oTaxonomy.isNewlyCreated = false;
        aTaxonomiesToSave.push(oTaxonomy);
      });
      _createAttributionTaxonomyChildrenCall(aTaxonomiesToSave, iIndex);
    } else {
      //case: Newly created tag value
      var oTaxonomy = _generateDummyTaxonomy(sParentId, sNewLabel);
      oTaxonomy.parentTagId = sMasterListParentId;
      oTaxonomy.tagValueId = "";
      oTaxonomy.isNewlyCreated = true;
      oTaxonomy.iIndex = iIndex;
      aTaxonomiesToSave.push(oTaxonomy);
      AttributionTaxonomyListProps.setActiveTaxonomyLevelChildren(aTaxonomiesToSave[0]);
      AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(true);
      _triggerChange();
    }
  };

  var _createBulkTaxonomies = function (aTaxonomies, iIndex) {
    if (CS.isEmpty(aTaxonomies)) {
      return;
    }
    var oCodeToVerifyUniqueness = {
      id: aTaxonomies[0].code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY
    };
    if (CS.isEmpty(aTaxonomies[0].label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    var oCallbackData = {};
    oCallbackData.functionToExecute = _createAttributionTaxonomyChildrenCall.bind(this, aTaxonomies, iIndex);
    var sURL = oAttributionTaxonomyRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackData);
  };

  var _createAttributionTaxonomyChildrenCall = function (aTaxonomies, iIndex) {
    let sURL = oAttributionTaxonomyRequestMapping.CreateBulkTaxonomies;
    SettingUtils.csPutRequest(sURL, {}, aTaxonomies, successCreateBulkTaxonomies.bind(this, iIndex), failureTaxonomyMasterListCallback);
  };

  var successCreateBulkTaxonomies = function (iLevel, oResponse) {
    let oSuccess = oResponse.success;
    let aTaxonomies = oSuccess.list;
    let oReferencedTaxonomies = oSuccess.referencedTaxonomies;
    let oPropReferencedTaxonomies = AttributionTaxonomyListProps.getAllowedTaxonomyConfigDetails();
    CS.assign(oPropReferencedTaxonomies, oReferencedTaxonomies);

    var aTaxonomyMasterList = _getLevelChildrenByIndex(iLevel);
    CS.forEach(aTaxonomies, function (oTaxonomy) {
      aTaxonomyMasterList.push(oTaxonomy);
      _prepareTaxonomyHierarchyData(oReferencedTaxonomies, oTaxonomy);
    });

    AttributionTaxonomyListProps.setActiveTaxonomyLevelChildren({});
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(false);
    let sMessage = SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TAXONOMY});
    SettingUtils.showSuccess(sMessage);

    /** select default active taxonomy if it exists**/
    aTaxonomies[0] && _fetchAttributionTaxonomy(aTaxonomies[0].id, iLevel + 1, null, false);
  };

  var failureTaxonomyMasterListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureTaxonomyMasterListCallback", getTranslation());
  };

  var _getLevelChildrenByIndex = function (iIndex) {
    var aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    var oAppData = SettingUtils.getAppData();
    var aTaxonomyMasterList = oAppData.getAttributionTaxonomyList();
    for (var i = 0; i <= iIndex; i++) {
      var sSelectedId = aSelectedTaxonomyLevels[i];
      var oTaxonomy = CS.find(aTaxonomyMasterList, {id: sSelectedId}) || {};
      aTaxonomyMasterList = oTaxonomy.children || [];
    }
    return aTaxonomyMasterList;
  };

  var _toggleAddLevelPopoverVisibility = function (bPopoverStatus) {
    AttributionTaxonomyListProps.setPopoverVisibilityStatus(bPopoverStatus);
  };

  var _handleTaxonomyLevelItemLabelChanged = function (iIndex, sItemId, sLabel) {
    var oSavePostModel = _getDataModelForSave();
    var aTaxonomyList = _getLevelChildrenByIndex(iIndex);
    var oFoundTaxonomy = CS.find(aTaxonomyList, {id: sItemId});
    if (oFoundTaxonomy) {
      oSavePostModel.id = oFoundTaxonomy.id;
      oSavePostModel.label = sLabel;
      oSavePostModel.parent = oFoundTaxonomy.parent;
      oSavePostModel.icon = oFoundTaxonomy.icon;
      oSavePostModel.code = oFoundTaxonomy.code;
      oSavePostModel.addedLevel = null;
      oSavePostModel.deletedLevel = null;
    }

    _saveAttributionTaxonomy(oSavePostModel, iIndex);
  };

  var _getAttributionTaxonomyScreenLockStatus = function () {
    return AttributionTaxonomyListProps.getAttributionTaxonomyScreenLockedStatus();
  };

  var _handleTaxonomyAddChildButtonClicked = function (bPopoverVisible, sTagGroupId) {
    if (!bPopoverVisible) {
      AttributionTaxonomyListProps.setAddChildPopoverVisibleLevelId("");
      _triggerChange();
    } else {
      var oParameters = {};
      oParameters.id = sTagGroupId;
      let sURL = oAttributionTaxonomyRequestMapping.GetAllowedTagValues;
      SettingUtils.csGetRequest(sURL, oParameters, successFetchAllowedTagValuesCallback.bind(this, sTagGroupId), failureFetchAllowedTagValuesCallback);
    }
  };

  var successFetchAllowedTagValuesCallback = function (sTagGroupId, oResponse) {
    var aAllowedTagValues = oResponse.success;
    AttributionTaxonomyListProps.setAllowedTagValues(aAllowedTagValues);
    AttributionTaxonomyListProps.setAddChildPopoverVisibleLevelId(sTagGroupId);
    _triggerChange();
  };

  var failureFetchAllowedTagValuesCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAllowedTagValuesCallback", getTranslation());
  };

  var _toggleAttributionHierarchyScrollEnableDisableProp = function () {
    var bCurrentVal =  AttributionTaxonomyListProps.getIsAttributionTaxonomyHierarchyScrollAutomaticallyEnabled();
    AttributionTaxonomyListProps.setIsAttributionTaxonomyHierarchyScrollAutomaticallyEnabled(!bCurrentVal);
    _triggerChange();
  };

  var _createAttributionTaxonomyLevelChildrenDialogClick = function () {
    var oTaxonomyLevelToBeCreated = AttributionTaxonomyListProps.getActiveTaxonomyLevelChildren();
    var aLevelToAdded = [];
    aLevelToAdded.push(oTaxonomyLevelToBeCreated);
    _createBulkTaxonomies(aLevelToAdded, oTaxonomyLevelToBeCreated.iIndex);
  };

  var _cancelAttributionTaxonomyLevelChildrenDialogClick = function () {
    AttributionTaxonomyListProps.setActiveTaxonomyLevelChildren({});
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(false);
    _triggerChange();
  };

  var _createAttributionTaxonomyLevelDialogClick = function () {
    var oTaxonomyLevelToBeCreated = AttributionTaxonomyListProps.getActiveTaxonomyLevel();
    if (CS.isEmpty(oTaxonomyLevelToBeCreated)) {
      return;
    }
    var oCodeToVerifyUniqueness = {
      id: oTaxonomyLevelToBeCreated.addedLevel.addedTag.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY,
    };
    if (CS.isEmpty(oTaxonomyLevelToBeCreated.addedLevel.addedTag.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    var oCallbackData = {};
    oCallbackData.functionToExecute = _saveAttributionTaxonomy.bind(this, oTaxonomyLevelToBeCreated, oTaxonomyLevelToBeCreated.addedLevel.id);
    var sURL = oAttributionTaxonomyRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackData);
  };

  var _cancelAttributionTaxonomyLevelDialogClick = function () {
    AttributionTaxonomyListProps.setActiveTaxonomyLevel({});
    AttributionTaxonomyListProps.setAttributionTaxonomyScreenLockedStatus(false);
    _triggerChange();
  };

  var _handleAttributionTaxonomyConfigDialogButtonClicked = function (sButtonId, sEntityType) {
    if (sButtonId == "create") {
      if(sEntityType == "attributionTaxonomyLevelChildren") {
        _createAttributionTaxonomyLevelChildrenDialogClick();
      }
      else{
        _createAttributionTaxonomyLevelDialogClick();
      }
    } else {
      if(sEntityType == "attributionTaxonomyLevelChildren") {
        _cancelAttributionTaxonomyLevelChildrenDialogClick();
      }
      else{
        _cancelAttributionTaxonomyLevelDialogClick();
      }

    }
  };

  let _assignReferencedItems = function (oConfigDetails) {
    let oReferencedObject = AttributionTaxonomyListProps.getReferencedData();
    CS.assign(oReferencedObject, oConfigDetails);
    AttributionTaxonomyListProps.setReferencedData(oReferencedObject);
  };

  let _handleSectionToggleButtonClicked = function (sSectionId) {
    let oActiveDetailedTaxonomy = AttributionTaxonomyListProps.getActiveDetailedTaxonomy();
    let oCallBackData = {
      functionToExecute: _triggerChange,
      preFunctionToExecute: _assignReferencedItems
    };

    let sURL = oAttributionTaxonomyRequestMapping.GetTaxonomyPropertyCollection;
    SettingUtils.handleSectionToggleButtonClicked(sSectionId, oActiveDetailedTaxonomy, sURL, oCallBackData);
  };

  let _handleRemoveKlassClicked = function (sKey, sId) {
    let oActiveAttributionTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    if (sKey === 'embeddedKlassIds') {
      let aIds = oActiveAttributionTaxonomy[sKey];
      let iIndex = CS.indexOf(aIds, sId);
      aIds.splice(iIndex, 1);
    } else {
      oActiveAttributionTaxonomy[sKey] = "";
    }
    delete oActiveAttributionTaxonomy.referencedKlasses[sId];
    _triggerChange();
  };

  let _handleClassDataTransferPropertiesAdded = function (sEntity, aSelectedIds, oReferencedData, sContext) {
    let oReferencedDataFromProps = AttributionTaxonomyListProps.getReferencedData();
    let oActiveAttributionTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    let sSplitter = SettingUtils.getSplitter();
    let sId = sContext.split(sSplitter)[1];
    let oReferencedClass = oActiveAttributionTaxonomy.referencedKlasses[sId];
    if (sEntity == "attributes") {
      CS.forEach(aSelectedIds, function (sId) {
        let iIndex = CS.findIndex(oReferencedClass.propagableAttributes, {id: sId});
        if (iIndex == -1) {
          let oSelectedProperty = {
            id: sId,
            couplingType: "noCoupling"
          };
          oReferencedClass.propagableAttributes.push(oSelectedProperty);
        }
      });
      CS.assign(oReferencedDataFromProps.referencedAttributes, oReferencedData);
    } else {
      CS.forEach(aSelectedIds, function (sId) {
        let iIndex = CS.findIndex(oReferencedClass.propagableTags, {id: sId});
        if (iIndex == -1) {
          let oSelectedProperty = {
            id: sId,
            couplingType: "noCoupling"
          };
          oReferencedClass.propagableTags.push(oSelectedProperty);
        }
      });
      CS.assign(oReferencedDataFromProps.referencedTags, oReferencedData);
    }
    _triggerChange();
  };

  let _handleClassDataTransferPropertiesRemoved = function (sClassId, sPropertyId, sContext) {
    let oActiveAttributionTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    let oReferencedClass = oActiveAttributionTaxonomy.referencedKlasses[sClassId];
    if (sContext == "attributes") {
      CS.remove(oReferencedClass.propagableAttributes, {id: sPropertyId});
    } else {
      CS.remove(oReferencedClass.propagableTags, {id: sPropertyId});
    }
    _triggerChange();
  };

  let _handleClassDataTransferPropertiesCouplingChanged = function (sClassId, sPropertyId, sNewValue, sContext) {
    let oActiveAttributionTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
    let oReferencedClass = oActiveAttributionTaxonomy.referencedKlasses[sClassId];
    let oProperty = {};
    if (sContext == "attributes") {
      oProperty = CS.find(oReferencedClass.propagableAttributes, {id: sPropertyId});
    } else {
      oProperty = CS.find(oReferencedClass.propagableTags, {id: sPropertyId});
    }
    oProperty.couplingType = sNewValue;
    _triggerChange();
  };

  let _handleExportAttributionTaxonomy = function (oSelectiveExport) {
    return SettingUtils.csPostRequest(oSelectiveExport.sUrl, {},oSelectiveExport.oPostRequest, successExportFile, failureExportFile);
  };

  var successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
    return true;
  };

  var failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
    return false;
  };

  var _getIsValidFileTypes = function (oFile) {
    // var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var uploadFileImport = function (oImportExcel, oCallback) {
    return SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  var successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
    return true;
  };

  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
    return false;
  };

  var _handleAttributionTaxonomyTabChanged = function (sTabId) {
    let bIsAttributionTaxonomyScreenLocked = _getAttributionTaxonomyScreenLockStatus();
    let oCallback = {};
    oCallback.functionToExecute = () => {
      AttributionTaxonomyListProps.setAttributionTaxonomyListActiveTabId(sTabId);
      AttributionTaxonomyListProps.setActiveDetailedTaxonomy({});
      _fetchAttributionTaxonomyList();
    };
    if (bIsAttributionTaxonomyScreenLocked) {
      CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveTaxonomyDetails.bind(this, oCallback),
          _discardTaxonomyDetails.bind(this, oCallback),
          function () {
          });
    }
    else {
      oCallback.functionToExecute();
    }

  };

  let _handleAttributionTaxonomyFileUploaded = function (aFiles,oImportExcel) {
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
  }

  let _handleRefreshAttributionTaxonomyConfig = function () {
    let aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    let sTaxonomyId = CS.last(aSelectedTaxonomyLevels);
    let iIndex = CS.indexOf(aSelectedTaxonomyLevels, sTaxonomyId);
    _handleAttributionTaxonomyLevelItemClicked((iIndex - 1), sTaxonomyId);
  };

  let _handleTaxonomyManageEntityButtonClicked = function () {
    let aSelectedTaxonomyLevels = AttributionTaxonomyListProps.getSelectedTaxonomyLevels();
    let aSelectedIds = CS.last(aSelectedTaxonomyLevels);
    let sType = ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY;

    if (CS.isEmpty(aSelectedIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return
    }
    else {
      ManageEntityConfigProps.setIsDelete(false);
      ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedIds, sType);
    }
  };

  let _prepareTaxonomyHierarchyData = function (oReferencedTaxonomies, oActiveTaxonomy) {
    let oAllowedTaxonomyConfigDetails = AttributionTaxonomyListProps.getAllowedTaxonomyConfigDetails();
    CS.assign(oAllowedTaxonomyConfigDetails, oReferencedTaxonomies);
    /** assign -1 to parentId in parent level to prepare path*/
    let sParentId = CS.isNotEmpty(oActiveTaxonomy.parent) ? oActiveTaxonomy.parent.id : '-1';
    let oAllowedTaxonomyHierarchyList = AttributionTaxonomyListProps.getAllowedTaxonomyHierarchyList();
    let oTaxonomyPath = {};
    _prepareAllowedTaxonomyHierarchyListRecursive(oActiveTaxonomy, oAllowedTaxonomyConfigDetails, sParentId, oTaxonomyPath);
    let oTaxonomyHierarchyList = CS.assign(oAllowedTaxonomyHierarchyList, oTaxonomyPath);
    AttributionTaxonomyListProps.setAllowedTaxonomyHierarchyList(oTaxonomyHierarchyList);
  };

  let _prepareAllowedTaxonomyHierarchyListRecursive = function (oTaxonomy, oReferencedTaxonomies, sParentId, oTaxonomyPath) {
    let oPreparedTaxonomyHierarchyData = CommonUtils.prepareTaxonomyHierarchyData([oTaxonomy], oReferencedTaxonomies, sParentId);
    CS.assign(oTaxonomyPath, oPreparedTaxonomyHierarchyData);
    oTaxonomy.customIconClassName = ClassNameFromBaseTypeDictionary[oTaxonomy.baseType];

    let aChildren = oTaxonomy.children || [];
    CS.forEach(aChildren, function (oChild) {
      _prepareAllowedTaxonomyHierarchyListRecursive(oChild, oReferencedTaxonomies, oTaxonomy.id, oTaxonomyPath);
    });
  };

  /*********************** PUBLIC *****************/

  return {
    fetchAttributionTaxonomyList: function (bDoNotShowDetailedTaxonomy) {
      _fetchAttributionTaxonomyList(bDoNotShowDetailedTaxonomy);
    },

    handleAttributionTaxonomyLevelActionItemClicked: function (iIndex, sItemId) {
      _handleAttributionTaxonomyLevelActionItemClicked(iIndex, sItemId);
    },

    handleTaxonomyNameChanged: function (sKey, sNewValue) {
      _handleTaxonomyNameChanged(sKey, sNewValue);
      _triggerChange();
    },

    handleTaxonomyLevelSingleValueChanged: function (sKey, sNewValue, sContext) {
      _handleTaxonomyLevelSingleValueChanged(sKey, sNewValue, sContext);
      _triggerChange();
    },

    handleAttributionTaxonomyCreateDialogButtonClicked: function (sContext, sIsMajorMinor, sLabel) {
      _handleAttributionTaxonomyCreateDialogButtonClicked(sContext, sIsMajorMinor, sLabel);
    },

    handleAttributionTaxonomyListLevelAdded: function (sSelectedId, bIsNewlyCreated, sLabel) {
      _handleAttributionTaxonomyListLevelAdded(sSelectedId, bIsNewlyCreated, sLabel);
    },

    handleAttributionTaxonomyLevelItemClicked: function (iIndex, sItemId) {
      _handleAttributionTaxonomyLevelItemClicked(iIndex, sItemId);
    },

    handleAttributionTaxonomyLevelChildActionItemClicked: function (iIndex, sActionId, sChildId) {
      _handleAttributionTaxonomyLevelChildActionItemClicked(iIndex, sActionId, sChildId)
    },

    handleTaxonomyLevelMasterListChildrenAdded: function (iIndex, aCheckedItems, sNewLabel) {
      _handleTaxonomyLevelMasterListChildrenAdded(iIndex, aCheckedItems, sNewLabel);
    },

    handleTaxonomyAddChildButtonClicked: function (bPopoverVisible, sTagGroupId) {
      _handleTaxonomyAddChildButtonClicked(bPopoverVisible, sTagGroupId);
    },

    handleDetailedTaxonomySingleValueChanged: function (sKey, sValue) {
      _handleDetailedTaxonomySingleValueChanged(sKey, sValue);
    },

    handleMSSValueChanged: function (biSingleSelect, sKey, aSelectedItems, oReferencedData) {
      _handleMSSValueChanged(biSingleSelect, sKey, aSelectedItems, oReferencedData);
    },

    handleMSSCrossIconClicked: function (biSingleSelect, sKey, sId) {
      _handleMSSVCrossIconClicked(biSingleSelect, sKey, sId);
    },

    handleSectionsUpdated: function (aSelectionItems, bIsAdded) {
      _handleSectionsUpdated(aSelectionItems, bIsAdded);
    },

    handleSectionElementInputChanged: function (sSectionId, sElementId, sProperty, sNewValue) {
      var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
      SettingUtils.handleSectionElementInputChanged(oActiveTaxonomy, sSectionId, sElementId, sProperty, sNewValue);
      _triggerChange();
    },

    handleSectionElementCheckboxToggled: function (sSectionId, sElementId, sProperty) {
      var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
      SettingUtils.handleSectionElementCheckboxToggled(oActiveTaxonomy, sSectionId, sElementId, sProperty);
      _triggerChange();
    },

    handleSectionMSSValueChanged: function (sSectionId, sElementId, sProperty, aNewValue, sScreenName) {
      var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
      SettingUtils.handleSectionMSSValueChanged(oActiveTaxonomy, sSectionId, sElementId, sProperty, aNewValue, sScreenName);
      _triggerChange();
    },

    handleTaxonomyUploadIconChangeEvent: function (sIconKey, sIconCode) {
      _handleDetailedTaxonomyUploadIconChangeEvent(sIconKey, sIconCode);
      _triggerChange();
    },

    handleDetailedTaxonomyDialogClose: function (sContext) {
      _handleDetailedTaxonomyActionItemClicked(sContext);
    },

    saveTaxonomyDetails: function (oCallbackData) {
      _saveTaxonomyDetails(oCallbackData);
    },

    discardTaxonomyDetails: function (oCallbackData) {
      _discardTaxonomyDetails(oCallbackData);
    },

    addDefaultTagValueForConfigTabularView: function (sSectionId, sElementId, sTagGroupId, aTagValueIds) {
      var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
      var oSection = CS.find(oActiveTaxonomy.sections, {id: sSectionId});
      var oElement = CS.find(oSection.elements, {id: sElementId});
      var aDefaultValue = oElement.defaultValue;
      CS.forEach(aDefaultValue, function (oTagValue) {
        if (!CS.includes(aTagValueIds, oTagValue.tagId)) {
          oTagValue.relevance = 0;
        }
      });

      CS.forEach(aTagValueIds, function (sTagValueId) {
        var oTagValue = CS.find(aDefaultValue, {tagId: sTagValueId});
        if (oTagValue) {
          oTagValue.relevance = 100;
        } else {
          aDefaultValue.push({
            relevance: 100,
            tagId: sTagValueId
          });
        }
      });
      _triggerChange();
    },

    removeDefaultTagValue: function (sSectionId, sElementId, sTagGroupId, sTagValueId) {
      var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
      var oSection = CS.find(oActiveTaxonomy.sections, {id: sSectionId});
      var oElement = CS.find(oSection.elements, {id: sElementId});
      var aDefaultValue = oElement.defaultValue;
      CS.remove(aDefaultValue, {tagId: sTagValueId});
      _triggerChange();
    },

    handleSelectionToggleButtonClicked: function (sKey, sId, bSingleSelect) {
      _handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
    },

    toggleAddLevelPopoverVisibility: function (bPopoverStatus) {
      _toggleAddLevelPopoverVisibility(bPopoverStatus);
    },

    handleTaxonomyLevelItemLabelChanged: function (iIndex, sItemId, sLabel) {
      _handleTaxonomyLevelItemLabelChanged(iIndex, sItemId, sLabel);
    },

    handleVisualElementBlockerClicked: function (oInfo) {
      var oActiveTaxonomy = _getCurrentDetailedAttributionTaxonomy();
      SettingUtils.handleVisualElementBlockerClicked(oActiveTaxonomy, oInfo, _triggerChange);
    },

    addTagInContentFromUniqueSearch: function (sSectionId, sElementId, sTagValueId) {
      var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
      var oSection = CS.find(oActiveTaxonomy.sections, {id: sSectionId});
      var oElement = CS.find(oSection.elements, {id: sElementId});
      oElement.defaultValue = [{
        tagId: sTagValueId,
        relevance: 100
      }];
      _triggerChange();
    },

    handleYesNoViewToggled: function(sSectionId, sElementId, bValue){
      try {
        var oActiveTaxonomy = _makeActiveDetailedAttributionTaxonomyDirty();
        var oSection = CS.find(oActiveTaxonomy.sections, {id: sSectionId});
        var oDefaultValue = {};
        var oElement = CS.find(oSection.elements, {id: sElementId});
        oDefaultValue.tagId = oElement.tag.children[0].id;
        oDefaultValue.relevance = bValue ? 100: -100;
        oElement.defaultValue = [oDefaultValue];
      }
      catch (oException) {
        ExceptionLogger.error(oException);
      }

      _triggerChange();
    },

    getAttributionTaxonomyScreenLockStatus: function () {
      return _getAttributionTaxonomyScreenLockStatus();
    },

    toggleAttributionHierarchyScrollEnableDisableProp: function () {
      _toggleAttributionHierarchyScrollEnableDisableProp();
    },

    handleAttributionTaxonomyConfigDialogButtonClicked: function (sButtonId, sEntityType) {
      _handleAttributionTaxonomyConfigDialogButtonClicked(sButtonId, sEntityType);
    },

    makeActiveDetailedAttributionTaxonomyDirty: function () {
      return _makeActiveDetailedAttributionTaxonomyDirty();
    },

    handleRemoveKlassClicked: function(sKey, sId){
      _handleRemoveKlassClicked(sKey, sId);
    },

    handleClassDataTransferPropertiesAdded: function(sEntity, aSelectedIds, oReferencedData, sContext){
      _handleClassDataTransferPropertiesAdded(sEntity, aSelectedIds, oReferencedData, sContext);
    },

    handleAttributionTaxonomyTabChanged : function(sTabId){
      _handleAttributionTaxonomyTabChanged(sTabId);
    },

    handleClassDataTransferPropertiesRemoved: function (sClassId, sPropertyId, sContext) {
      _handleClassDataTransferPropertiesRemoved(sClassId, sPropertyId, sContext);
    },

    handleClassDataTransferPropertiesCouplingChanged: function (sClassId, sPropertyId, sNewValue, sContext) {
      _handleClassDataTransferPropertiesCouplingChanged(sClassId, sPropertyId, sNewValue, sContext);
    },

    handleSectionToggleButtonClicked: function (sSectionId) {
      _handleSectionToggleButtonClicked(sSectionId);
    },

    handleExportAttributionTaxonomy: function (oSelectiveExport) {
      return _handleExportAttributionTaxonomy(oSelectiveExport);
    },

    handleAttributionTaxonomyFileUploaded: function (aFiles,oImportExcel) {
      _handleAttributionTaxonomyFileUploaded(aFiles,oImportExcel);
    },

    handleRefreshAttributionTaxonomyConfig: function () {
      _handleRefreshAttributionTaxonomyConfig();
    },

    handleTaxonomyManageEntityButtonClicked: function () {
      _handleTaxonomyManageEntityButtonClicked();
    }

  };
})();

MicroEvent.mixin(AttributionTaxonomyStore);

export default AttributionTaxonomyStore;
