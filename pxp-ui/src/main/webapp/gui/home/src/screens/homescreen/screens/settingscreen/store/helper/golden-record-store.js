import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import GoldenRecordsProps from './../model/golden-records-view-props';
import SettingUtils from './setting-utils';
import CommonUtils from './../../../../../../commonmodule/util/common-utils';
import { GoldenRecordsMapping as oGoldenRecordsMapping } from '../../tack/setting-screen-request-mapping';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import MockDataForEntityBaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import LogFactory from '../../../../../../libraries/logger/log-factory';
import MethodTracker from '../../../../../../libraries/methodtracker/method-tracker';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import GoldenRecordRuleConfigGridViewSkeleton from './../../tack/golden-record-rules-config-grid-view-skeleton';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import assetTypes from '../../tack/coverflow-asset-type-list';
import ClassTypeDictionary from '../../../../../../commonmodule/tack/class-type-dictionary';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
var logger = LogFactory.getLogger('role-store');
var trackMe = MethodTracker.getTracker('role-store');

var GoldenRecordStore = (function () {

  var _triggerChange = function () {
    GoldenRecordStore.trigger('goldenRecord-changed');
  };

  var _refreshGoldenRecordRule = function () {
    var oActiveRule = GoldenRecordsProps.getActiveGoldenRecordRule();
    _fetchGoldenRecordRuleById(oActiveRule.id);
  };

  var _createGoldenRecordRule = function () {
    var oObj = {
      label: UniqueIdentifierGenerator.generateUntitledName(),
      code: "",
      isCreated: true,
      klassIds: ''
    };
    GoldenRecordsProps.setActiveGoldenRecordRule(oObj);
    SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);
    _triggerChange();

  };

  var _deleteGoldenRecordRule = function (aSelectedIds) {
    var oRuleMap = SettingUtils.getAppData().getGoldenRecordRuleList();
    var aSelectedAndCheckedIds = aSelectedIds;
    if (CS.isEmpty(aSelectedAndCheckedIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return;
    }

    var isStandardRuleSelected = false;
    var aRuleNames = [];
    CS.forEach(aSelectedAndCheckedIds, function (sRuleId) {
      var oMasterRule = oRuleMap[sRuleId];
      let sMasterRuleLabel = CS.getLabelOrCode(oMasterRule);
      aRuleNames.push(sMasterRuleLabel);

      if (oMasterRule.isStandard) {
        isStandardRuleSelected = true;
        return true;
      }

    });

    if (isStandardRuleSelected) {
      alertify.message(getTranslation().STANDARD_ATTRIBUTE_DELETEION);
    }
    else {
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION
          , aRuleNames,
          function () {

            var oObj = {
              ids: aSelectedAndCheckedIds
            };

            SettingUtils.csDeleteRequest(oGoldenRecordsMapping.DeleteRule, {}, oObj, successDeleteRuleButtonClicked, failureDeleteRuleButtonClicked)
            .then(_fetchGoldenRecordRuleListForGridView);

          }, function (oEvent) {
          });
    }
  };

  var successDeleteRuleButtonClicked = function (oResponse) {
    let aSuccessIds = oResponse.success;

    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.GOLDEN_RECORD_RULE);
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - oResponse.success.length);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oMasterGoldenRecordRulesMap = SettingUtils.getAppData().getGoldenRecordRuleList();
    let oSkeleton = oGridViewPropsByContext.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      delete oMasterGoldenRecordRulesMap[sId];
      CS.remove(oSkeleton.selectedContentIds, function (oSelectedId) {
        return oSelectedId == sId;
      });
    });

    oResponse = oResponse.success;
    var oRuleMap = SettingUtils.getAppData().getGoldenRecordRuleList();
    var oRuleValueList = GoldenRecordsProps.getGoldenRecordRuleValuesList();

    var aSuccessFullyDeleted = oResponse;
    CS.forEach(aSuccessFullyDeleted, function (sDeletedId) {
      delete oRuleMap[sDeletedId];
      delete oRuleValueList[sDeletedId];
    });

    GoldenRecordsProps.setActiveGoldenRecordRule({});
    GoldenRecordsProps.setGoldenRecordRuleScreenLockStatus(false);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().GOLDEN_RECORD_RULE}));
  };

  var failureDeleteRuleButtonClicked = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureDeleteRuleButtonClicked", getTranslation());
  };

  var _createNewGoldenRecordRule = function () {
    var oRuleToCreate = GoldenRecordsProps.getActiveGoldenRecordRule();
    oRuleToCreate = oRuleToCreate.clonedObject || oRuleToCreate;

    if (CS.isEmpty(oRuleToCreate.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    if (CS.isEmpty(oRuleToCreate.klassIds)) {
      alertify.error(getTranslation().NATURE_CLASS_SHOULD_BE_SELECTED);
      return;
    }

    var oCodeToVerifyUniqueness = {
      id: oRuleToCreate.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_GOLDEN_RECORDS
    };

    var oCallbackData = {};
    oCallbackData.functionToExecute = _createGoldenRecordRuleCall.bind(this, oRuleToCreate);
    var sURL = oGoldenRecordsMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackData);
  };

  var _createGoldenRecordRuleCall = function (oRuleToCreate) {
    SettingUtils.csPutRequest(oGoldenRecordsMapping.CreateRule, {}, oRuleToCreate, successCreateRuleCallback, failureCreateRuleCallback);
  };

  var _selectRuleById = function (sRuleId) {
    var oRuleValueMap = GoldenRecordsProps.getGoldenRecordRuleValuesList();
    CS.forEach(oRuleValueMap, function (oValue, sId) {
      oValue.isChecked = false;
      oValue.isSelected = (sId == sRuleId);
    })
  };

  var successCreateRuleCallback = function (oResponse) {
    let oResponseData = oResponse.success;
    var oNewRule = oResponseData.goldenRecordRule;
    let aGoldenRuleGridData = GoldenRecordsProps.getRuleGridData();
    aGoldenRuleGridData.push(oNewRule);
    let oProcessedGoldenRule = GridViewStore.getProcessedGridViewData(GridViewContexts.GOLDEN_RECORD_RULE, [oNewRule])[0];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.GOLDEN_RECORD_RULE);

    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedGoldenRule);
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);
    var oAppData = SettingUtils.getAppData();
    var oRuleMap = oAppData.getGoldenRecordRuleList();

    var oRuleValueProps = GoldenRecordsProps.getGoldenRecordRuleValuesList();

    oRuleValueProps[oNewRule.id] = _getDefaultListProps(oNewRule);
    oRuleMap[oNewRule.id] = {
      id: oNewRule.id,
      label: oNewRule.label,
      code: oNewRule.code
    };

    oNewRule.configDetails = {
      referencedAttributes: oResponseData.referencedAttributes,
      referencedEndpoints: oResponseData.referencedEndpoints,
      referencedKlasses: oResponseData.referencedKlasses,
      referencedOrganizations: oResponseData.referencedOrganizations,
      referencedTags: oResponseData.referencedTags,
      referencedTaxonomies: oResponseData.referencedTaxonomies,
      referencedRelationships: oResponseData.referencedRelationships,
      referencedNatureRelationships: oResponseData.referencedNatureRelationships
    };

    oNewRule.natureKlassIds = oNewRule.klassIds;

    _selectRuleById(oNewRule.id);
    GoldenRecordsProps.setActiveGoldenRecordRule(oNewRule);
    GoldenRecordsProps.setGoldenRecordRuleScreenLockStatus(false);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_CREATED_SUCCESSFULLY,{ entity : getTranslation().GOLDEN_RECORD_RULE}));

    /** To open edit view immediately after create */
    successFetchRuleByIdCallback({}, oResponse);
  };

  var failureCreateRuleCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchRoleListCallback", getTranslation());
  };

  var _getDefaultListProps = function () {
    return {
      isSelected: false,
      isChecked: false,
      isEditable: false
    }
  };

  var _handleCreateDialogButtonClicked = function (sButtonId) {
    if (sButtonId === "create") {
      _createNewGoldenRecordRule();
    } else {
      _cancelGoldenRecordRuleCreation();
    }
  };

  var _getRuleScreenLockStatus = function () {
    return GoldenRecordsProps.getGoldenRecordRuleScreenLockStatus();
  };

  var _cancelGoldenRecordRuleCreation = function () {
    var oRuleValueList = GoldenRecordsProps.getGoldenRecordRuleValuesList();
    var oRuleMappingObject = SettingUtils.getAppData().getRuleList();
    var oNewRuleToCreate = GoldenRecordsProps.getActiveGoldenRecordRule();

    delete oRuleValueList[oNewRuleToCreate.id];
    delete oRuleMappingObject[oNewRuleToCreate.id];
    GoldenRecordsProps.setActiveGoldenRecordRule({});
    GoldenRecordsProps.setGoldenRecordRuleScreenLockStatus(false);
    _triggerChange();
  };

  var checkAndDeleteRuleFromList = function (oResponse, sRuleId) {
    var oRuleValueList = GoldenRecordsProps.getGoldenRecordRuleValuesList();
    var oRuleList = SettingUtils.getAppData().getRuleList();
    var oSelectedRule = GoldenRecordsProps.getActiveGoldenRecordRule();

    if (oResponse.success.message.indexOf('NOT_FOUND') >= 0 && (oSelectedRule || sRuleId)) {
      var sRuleName = oRuleList[sRuleId || oSelectedRule.id].label;
      delete oRuleValueList[sRuleId || oSelectedRule.id];
      delete oRuleList[sRuleId || oSelectedRule.id];
      //delete oMasterAttributes[sAttributeId || oSelectedAttribute.id];
      GoldenRecordsProps.setActiveGoldenRecordRule({});
      return sRuleName;
    }
    return null;

  };

  var _fetchGoldenRecordRuleById = function (sRuleId, oCallbackData) {
    var oCallback = {};
    if (oCallbackData) {
      oCallback.functionToExecute = oCallbackData.functionToExecute
    }
    SettingUtils.csGetRequest(oGoldenRecordsMapping.GetRule, {id: sRuleId}, successFetchRuleByIdCallback.bind(this, oCallback), failureFetchRuleByIdCallback.bind(this, sRuleId));

  };

  var successFetchRuleByIdCallback = function (oCallbackData, oResponse) {
    var oAppData = SettingUtils.getAppData();
    var oRuleMap = oAppData.getGoldenRecordRuleList();

    var oResponseData = oResponse.success;

    var oComponentProps = SettingUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    oScreenProps.setReferencedRoles(oResponseData.referencedRoles);
    oScreenProps.setReferencedTags(oResponseData.referencedTags);
    oScreenProps.setReferencedAttributes(oResponseData.referencedAttributes);
    var oReferencedClasses = oResponseData.referencedKlasses;

    CS.forEach(oReferencedClasses, function (oReferencedClass) {
      if (oReferencedClass.type == MockDataForEntityBaseTypesDictionary.articleKlassBaseType) {
        GoldenRecordsProps.setKlassTypeId(ClassTypeDictionary.ARTICLE);
      }
      else if (oReferencedClass.type == MockDataForEntityBaseTypesDictionary.assetKlassBaseType) {
        GoldenRecordsProps.setKlassTypeId(ClassTypeDictionary.ASSET);
      } else if (oReferencedClass.type == MockDataForEntityBaseTypesDictionary.marketKlassBaseType) {
        GoldenRecordsProps.setKlassTypeId(ClassTypeDictionary.MARKET);
      } else if (oReferencedClass.type == MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType) {
        GoldenRecordsProps.setKlassTypeId(ClassTypeDictionary.TEXT_ASSET);
      } else if (oReferencedClass.type == MockDataForEntityBaseTypesDictionary.supplierKlassBaseType) {
        GoldenRecordsProps.setKlassTypeId(ClassTypeDictionary.SUPPLIER);
      }
      return false;
    });

    //_updateAttributeListMap(oScreenProps.getReferencedAttributes());

    //_updateNormalizationTagsData(oRule);
    let oActiveRule = oResponseData.goldenRecordRule;
    var sRuleId = oActiveRule.id;
    oActiveRule.configDetails = {
      referencedAttributes: oResponseData.referencedAttributes,
      referencedEndpoints: oResponseData.referencedEndpoints,
      referencedKlasses: oResponseData.referencedKlasses,
      referencedOrganizations: oResponseData.referencedOrganizations,
      referencedTags: oResponseData.referencedTags,
      referencedTaxonomies: oResponseData.referencedTaxonomies,
      referencedRelationships: oResponseData.referencedRelationships,
      referencedNatureRelationships: oResponseData.referencedNatureRelationships
    };

    if (!CS.isEmpty(oActiveRule) && oActiveRule.configDetails) {
      let aReferencedKlasses = oResponseData.referencedKlasses;
      let aSelectedKlassIds = oActiveRule.klassIds;
      let aNatureKlassIds = [];
      let aNonNatureKlassIds = [];
      CS.forEach(aSelectedKlassIds, function (sSelectedKlassId) {
        let oKlass = CS.find(aReferencedKlasses, {id: sSelectedKlassId});
        if (oKlass.isNature) {
          aNatureKlassIds.push(sSelectedKlassId)
        }
        else {
          aNonNatureKlassIds.push(sSelectedKlassId)
        }
      });

      oActiveRule.natureKlassIds = aNatureKlassIds;
      oActiveRule.nonNatureKlassIds = aNonNatureKlassIds;

    }
    _selectRuleById(sRuleId);
    GoldenRecordsProps.setActiveGoldenRecordRule(oActiveRule);
    GoldenRecordsProps.setRightPanelVisibility(false);
    let oRightBarIconClickMap = GoldenRecordsProps.getRightBarIconClickMap();
    oRightBarIconClickMap.ruleList = false;

    // _initializeRuleViolationAndNormalizationProps(oRule);

    //TODO: Remove after fix. Support for previously created rules
    CS.forEach(oActiveRule.attributes, function (oAttribute) {
      CS.forEach(oAttribute.rules, function (oAttribRule) {
        if (oAttribRule.ruleListLinkId == null) {
          oAttribRule.ruleListLinkId = "";
        }
      });
    });

    oRuleMap[sRuleId].label = oActiveRule.label;

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    GoldenRecordsProps.setIsRuleDialogActive(true);
    _triggerChange();
  };

  var failureFetchRuleByIdCallback = function (sRuleId, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var sRuleName = checkAndDeleteRuleFromList(oResponse, sRuleId);
      alertify.error("[" + sRuleName + "] " + getTranslation().ERROR_ALREADY_DELETED, 0);
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchRuleByIdCallback", getTranslation());
    }
    _triggerChange();
  };

  var _isActiveRuleDirty = function () {
    var oActiveRule = GoldenRecordsProps.getActiveGoldenRecordRule();
    if(!CS.isEmpty(oActiveRule)) {
      return !!oActiveRule.clonedObject;
    }

    return false;
  };

  var _handleGoldenRecordRuleListNodeClicked = function (sRuleId) {

    if(_isActiveRuleDirty()) {
      var oCallbackData = {};
      oCallbackData.functionToExecute = _fetchGoldenRecordRuleById.bind(this, sRuleId);
      _saveRule(oCallbackData);
    } else {
      _fetchGoldenRecordRuleById(sRuleId);
    }
  };

  var _makeActiveGoldenRecordRuleDirty = function () {
    GoldenRecordsProps.setGoldenRecordRuleScreenLockStatus(true);

    var oActiveRule = GoldenRecordsProps.getActiveGoldenRecordRule();
    if (!oActiveRule.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveRule);
    }

    return oActiveRule.clonedObject;
  };

  var _handleRuleNameChanged = function (sValue) {
    var oAppData = SettingUtils.getAppData();
    var oRuleMap = oAppData.getGoldenRecordRuleList();
    var oActiveRule = _makeActiveGoldenRecordRuleDirty();
    oActiveRule.label = sValue;
    oRuleMap[oActiveRule.id].label = sValue;
  };

  let _handleRuleDetailMSSValueChanged = (sKey, aSelectedItems) => {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    oActiveRule[sKey] = aSelectedItems;
    if (sKey == 'natureKlassIds' || sKey == 'nonNatureKlassIds') {
      oActiveRule.klassIds = CS.concat(oActiveRule.natureKlassIds, oActiveRule.nonNatureKlassIds);
         }
  };

  let _handleRuleDetailPartnerApplyClicked = (aSelectedItems) => {
    _handleRuleDetailMSSValueChanged("organizations", aSelectedItems);
  };

  let _handleRuleDetailEndpointsChanged = (aSelectedItems) => {
    _handleRuleDetailMSSValueChanged("endpoints", aSelectedItems);
  };

  var _isActiveGoldenRecordRuleDirty = function () {
    var oActiveRule = GoldenRecordsProps.getActiveGoldenRecordRule();
    if (!CS.isEmpty(oActiveRule)) {
      return !!oActiveRule.clonedObject;
    }

    return false;
  };

  var _validateEntity = function (oEntity) {
    trackMe('_validateEntity');
    var oAppData = SettingUtils.getAppData();
    var oMapRuleList = oAppData.getGoldenRecordRuleList();
    var aEntityList = [];
    CS.forEach(oMapRuleList, function (oMasterRule) {
      aEntityList.push(oMasterRule);
    });

    var sEntityName = oEntity.label;

    var bNameValidation = sEntityName.trim() != "";
    var bDuplicateNameValidation = true;

    var sContentId = oEntity.id;
    var aEntityWithSameName = CS.filter(aEntityList, {'label': oEntity.label});

    CS.forEach(aEntityWithSameName, function (oContentWithSameName) {
      if (oContentWithSameName.id != sContentId) {
        bDuplicateNameValidation = false;
      }
    });

    return {
      nameValidation: bNameValidation,
      duplicateNameValidation: bDuplicateNameValidation
    };
  };

  var _validateEntities = function (aEntity) {
    trackMe('_validateContents');

    var oRes = {
      entityWithBlankNames: [],
      entityWithDuplicateNames: [],
      nameValidation: true,
      duplicateNameValidation: true,
      isTagValid: true
    };

    CS.forEach(aEntity, function (oEntity) {
      var sContentId = oEntity.id;
      var oValidation = oEntity.clonedObject ? _validateEntity(oEntity.clonedObject) : _validateEntity(oEntity);
      oRes.nameValidation = oRes.nameValidation && oValidation.nameValidation;
      oRes.duplicateNameValidation = oRes.duplicateNameValidation && oValidation.duplicateNameValidation;

      if (!oValidation.nameValidation) {
        oRes.entityWithBlankNames.push(sContentId);
      }

    });

    return oRes;
  };

  var _getADMForActiveRule = function (oOldActiveRule, oNewActiveRule) {
    let oDataToSave = {
      id: oNewActiveRule.id,
      label: oNewActiveRule.label,
      icon: '',
      isAutoCreate: oNewActiveRule.isAutoCreate,
    };

    oDataToSave.modifiedMergeEffect = {};

    oDataToSave.physicalCatalogIds = oNewActiveRule.physicalCatalogIds;
    oDataToSave.addedOrganizations = CS.difference(oNewActiveRule.organizations, oOldActiveRule.organizations);
    oDataToSave.deletedOrganizations = CS.difference(oOldActiveRule.organizations, oNewActiveRule.organizations);

    oDataToSave.addedEndpoints = CS.difference(oNewActiveRule.endpoints, oOldActiveRule.endpoints);
    oDataToSave.deletedEndpoints = CS.difference(oOldActiveRule.endpoints, oNewActiveRule.endpoints);

    oDataToSave.addedAttributes = CS.difference(oNewActiveRule.attributes, oOldActiveRule.attributes);
    oDataToSave.deletedAttributes = CS.difference(oOldActiveRule.attributes, oNewActiveRule.attributes);

    oDataToSave.addedTags = CS.difference(oNewActiveRule.tags, oOldActiveRule.tags);
    oDataToSave.deletedTags = CS.difference(oOldActiveRule.tags, oNewActiveRule.tags);

    oDataToSave.addedKlasses = CS.difference(oNewActiveRule.klassIds, oOldActiveRule.klassIds);
    oDataToSave.deletedKlasses = CS.difference(oOldActiveRule.klassIds, oNewActiveRule.klassIds);

    oDataToSave.addedTaxonomies = CS.difference(oNewActiveRule.taxonomyIds, oOldActiveRule.taxonomyIds);
    oDataToSave.deletedTaxonomies = CS.difference(oOldActiveRule.taxonomyIds, oNewActiveRule.taxonomyIds);

    oDataToSave.modifiedMergeEffect.addedEffectAttributes = CS.differenceBy(oNewActiveRule.mergeEffect.attributes, oOldActiveRule.mergeEffect.attributes, "entityId");
    oDataToSave.modifiedMergeEffect.deletedEffectAttributes = CS.map(CS.differenceBy(oOldActiveRule.mergeEffect.attributes, oNewActiveRule.mergeEffect.attributes, "entityId"), "entityId");

    let aModifiedEffectAttributes = [];  //key for merge is modifiedMergeEffect
    CS.forEach(oNewActiveRule.mergeEffect.attributes, function (oNewAttribute) {
      let oOldAttribute = CS.find(oOldActiveRule.mergeEffect.attributes, {entityId: oNewAttribute.entityId});
      if (oOldAttribute) {
        if (oNewAttribute.type !== oOldAttribute.type) {
          aModifiedEffectAttributes.push(oNewAttribute)
        } else if (!CS.isEqual(oNewAttribute.supplierIds, oOldAttribute.supplierIds)) {
          aModifiedEffectAttributes.push(oNewAttribute)
        }
      }
    });

    oDataToSave.modifiedMergeEffect.modifiedEffectAttributes = aModifiedEffectAttributes;


    oDataToSave.modifiedMergeEffect.addedEffectTags = CS.differenceBy(oNewActiveRule.mergeEffect.tags, oOldActiveRule.mergeEffect.tags, "entityId");
    oDataToSave.modifiedMergeEffect.deletedEffectTags = CS.map(CS.differenceBy(oOldActiveRule.mergeEffect.tags, oNewActiveRule.mergeEffect.tags, "entityId"),"entityId");

    let aModifiedEffectTags = [];
    CS.forEach(oNewActiveRule.mergeEffect.tags, function (oNewTag) {
      let oOldTag = CS.find(oOldActiveRule.mergeEffect.tags, {entityId: oNewTag.entityId});
      if (oOldTag) {
        if (oNewTag.type !== oOldTag.type) {
          aModifiedEffectTags.push(oNewTag)
        } else if (!CS.isEqual(oNewTag.supplierIds, oOldTag.supplierIds)) {
          aModifiedEffectTags.push(oNewTag)
        }
      }
    });

    oDataToSave.modifiedMergeEffect.modifiedEffectTags = aModifiedEffectTags;

    oDataToSave.modifiedMergeEffect.addedEffectRelationships = CS.differenceBy(oNewActiveRule.mergeEffect.relationships, oOldActiveRule.mergeEffect.relationships, "entityId");
    oDataToSave.modifiedMergeEffect.deletedEffectRelationships = CS.map(CS.differenceBy(oOldActiveRule.mergeEffect.relationships, oNewActiveRule.mergeEffect.relationships, "entityId"),"entityId");

    let aModifiedEffectRelationships = [];
    CS.forEach(oNewActiveRule.mergeEffect.relationships, function (oNewRelationship) {
      let oOldRelationship = CS.find(oOldActiveRule.mergeEffect.relationships, {entityId: oNewRelationship.entityId});
      if (oOldRelationship) {
        if (oNewRelationship.type !== oOldRelationship.type) {
          aModifiedEffectRelationships.push(oNewRelationship)
        } else if (!CS.isEqual(oNewRelationship.supplierIds, oOldRelationship.supplierIds)) {
          aModifiedEffectRelationships.push(oNewRelationship)
        }
      }
    });

    oDataToSave.modifiedMergeEffect.modifiedEffectRelationships = aModifiedEffectRelationships;


    oDataToSave.modifiedMergeEffect.addedEffectNatureRelationships = CS.differenceBy(oNewActiveRule.mergeEffect.natureRelationships, oOldActiveRule.mergeEffect.natureRelationships, "entityId");
    oDataToSave.modifiedMergeEffect.deletedEffectNatureRelationships = CS.map(CS.differenceBy(oOldActiveRule.mergeEffect.natureRelationships, oNewActiveRule.mergeEffect.natureRelationships, "entityId"),"entityId");

    let aModifiedEffectNatureRelationships = [];
    CS.forEach(oNewActiveRule.mergeEffect.natureRelationships, function (oNewNatureRelationship) {
      let oOldNatureRelationship = CS.find(oOldActiveRule.mergeEffect.natureRelationships, {entityId: oNewNatureRelationship.entityId});
      if (oOldNatureRelationship) {
        if (oNewNatureRelationship.type !== oOldNatureRelationship.type) {
          aModifiedEffectNatureRelationships.push(oNewNatureRelationship)
        } else if (!CS.isEqual(oNewNatureRelationship.supplierIds, oOldNatureRelationship.supplierIds)) {
          aModifiedEffectNatureRelationships.push(oNewNatureRelationship)
        }
      }
    });

    oDataToSave.modifiedMergeEffect.modifiedEffectNatureRelationships = aModifiedEffectNatureRelationships;



    oDataToSave = CS.assign(oDataToSave);

    return oDataToSave;
  };

  var _saveRule = function (oCallback) {

    if (_isActiveGoldenRecordRuleDirty()) {

      var oActiveRule = GoldenRecordsProps.getActiveGoldenRecordRule();
      var oCurrentRule = {};
      if (!CS.isEmpty(oActiveRule)) {
        oCurrentRule = oActiveRule.clonedObject ? oActiveRule.clonedObject : oActiveRule;
      }

      var oCurrentRuleToSave = CS.cloneDeep(oCurrentRule);
      var oEntityValidation = _validateEntities([oCurrentRuleToSave]);

      GoldenRecordsProps.setGoldenRecordRuleScreenLockStatus(false);

      if (!oEntityValidation.nameValidation) {
        alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
        return;
      } else if (!oEntityValidation.duplicateNameValidation) {
        logger.error('_saveRuleList: Content with the same name exists', {});
        alertify.error(getTranslation().STORE_ALERT_CONTENT_SAME_NAME, 0);
        return;
      }

      var oRuleToSave = _getADMForActiveRule(oActiveRule, oCurrentRuleToSave);

      SettingUtils.csPostRequest(oGoldenRecordsMapping.SaveRule, {}, oRuleToSave, successSaveRuleCallback.bind(this, oCallback), failureSaveRuleCallback);

    } else {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
    }
  };

  var successSaveRuleCallback = function (oCallback, oResponse) {

    var oComponentProps = SettingUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    let oResponseData = oResponse.success;
    let oActiveRule = oResponseData.goldenRecordRule;
    var sRuleId = oActiveRule.id;
    let oRuleList = SettingUtils.getAppData().getGoldenRecordRuleList();
    CS.assign(oRuleList[sRuleId], oActiveRule);
    let aGoldenRuleGridData = GoldenRecordsProps.getRuleGridData();
    aGoldenRuleGridData.push(oActiveRule);
    let aRuleList = CS.values(oRuleList);
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.GOLDEN_RECORD_RULE, aRuleList);
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.GOLDEN_RECORD_RULE);
    oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);
    GoldenRecordsProps.setGoldenRecordRuleGridData(aRuleList);

    oActiveRule.configDetails = {
      referencedAttributes: oResponseData.referencedAttributes,
      referencedEndpoints: oResponseData.referencedEndpoints,
      referencedKlasses: oResponseData.referencedKlasses,
      referencedOrganizations: oResponseData.referencedOrganizations,
      referencedTags: oResponseData.referencedTags,
      referencedTaxonomies: oResponseData.referencedTaxonomies,
      referencedRelationships: oResponseData.referencedRelationships,
      referencedNatureRelationships: oResponseData.referencedNatureRelationships
    };

    if (!CS.isEmpty(oActiveRule) && oActiveRule.configDetails) {
      let aReferencedKlasses = oResponseData.referencedKlasses;
      let aSelectedKlassIds = oActiveRule.klassIds;
      let aNatureKlassIds = [];
      let aNonNatureKlassIds = [];
      CS.forEach(aSelectedKlassIds, function (sSelectedKlassId) {
        let oKlass = CS.find(aReferencedKlasses, {id: sSelectedKlassId});
        if (oKlass.isNature) {
          aNatureKlassIds.push(sSelectedKlassId)
        }
        else {
          aNonNatureKlassIds.push(sSelectedKlassId)
        }
      });

      oActiveRule.natureKlassIds = aNatureKlassIds;
      oActiveRule.nonNatureKlassIds = aNonNatureKlassIds;
    }

    oScreenProps.setReferencedRoles(oResponseData.referencedRoles);
    oScreenProps.setReferencedTags(oResponseData.referencedTags);
    oScreenProps.setReferencedAttributes(oResponseData.referencedAttributes);

    GoldenRecordsProps.setActiveGoldenRecordRule(oActiveRule);
    //_initializeRuleViolationAndNormalizationProps(oRule);

    oRuleList[sRuleId].label = oActiveRule.label;

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().GOLDEN_RECORD_RULE}));

    _triggerChange();
  };

  var failureSaveRuleCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      SettingUtils.failureCallback(oResponse, "failureFetchClassListCallback", getTranslation());
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchRoleListCallback", getTranslation());
    }
    ExceptionLogger.log(oResponse);
    _triggerChange();
  };

  var _handleSelectionToggleButtonClicked = function (sKey, sId) {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    let aPhysicalCatalogs = oActiveRule.physicalCatalogIds || [];
    let aRemovedCatalogIds = CS.remove(aPhysicalCatalogs, function (sCatalogId) {
      return sCatalogId == sId
    });
    if (CS.isEmpty(aRemovedCatalogIds)) {
      aPhysicalCatalogs.push(sId);
    }
    oActiveRule.physicalCatalogIds = aPhysicalCatalogs;
  };

  var _discardUnsavedRule = function (oCallbackData) {
    var oActiveRule = GoldenRecordsProps.getActiveGoldenRecordRule();
    _fetchGoldenRecordRuleById(oActiveRule.id, oCallbackData);
    var bScreenLockStatus = _getRuleScreenLockStatus();
    if (!bScreenLockStatus) {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      return;
    }
    if (!CS.isEmpty(oActiveRule)) {
      if (oActiveRule.isDirty || oActiveRule.clonedObject) {
        delete oActiveRule.clonedObject;
        delete oActiveRule.isDirty;
        alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
      }
      GoldenRecordsProps.setGoldenRecordRuleScreenLockStatus(false);
      if (oCallbackData.functionToExecute) {
        //oCallbackData.functionToExecute();
      }
      _triggerChange();

    } else {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
    }
  };

  var _handleRuleDetailMssValueChanged = function (aNewValue, sKey) {
    _handleRuleDetailMSSValueChanged(sKey, aNewValue);
  };

  let _fetchTaxonomyById = function (sContext, sTaxonomyId) {
    let oParameters = {};
    if (sContext == "majorTaxonomy") {
      if (sTaxonomyId == "addItemHandlerforMultiTaxonomy") {
        sTaxonomyId = SettingUtils.getTreeRootId();
      } else {
        sTaxonomyId = sTaxonomyId;
      }
    }
    oParameters.id = sTaxonomyId;

    let oTaxonomyPaginationData = GoldenRecordsProps.getTaxonomyPaginationData();
    let oPostData = CS.assign({taxonomyId: sTaxonomyId}, oTaxonomyPaginationData);


    SettingUtils.csPostRequest(oGoldenRecordsMapping.GetTaxonomy, {}, oPostData, successFetchFetchTaxonomy, failureFetchFetchTaxonomy);
  };

  let successFetchFetchTaxonomy = function (oResponse) {
    let oSuccess = oResponse.success;
    let aTaxonomyListFromServer = oSuccess.list;
    let oConfigDetails = oSuccess.configDetails;
    let aTaxonomyList = GoldenRecordsProps.getAllowedTaxonomies();
    GoldenRecordsProps.setAllowedTaxonomies(CS.concat(aTaxonomyList, aTaxonomyListFromServer));
    let oAllowedTaxonomyHierarchyListData = CommonUtils.prepareTaxonomyHierarchyData(GoldenRecordsProps.getAllowedTaxonomies(), oConfigDetails);
    GoldenRecordsProps.setAllowedTaxonomyHierarchyList(oAllowedTaxonomyHierarchyListData);
    _triggerChange();
  };

  let failureFetchFetchTaxonomy = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchFetchTaxonomy", getTranslation());
  };

  let _handleTaxonomyAdded = function (sTaxonomyId, sParentTaxonomyId) {
    SettingUtils.getTaxonomyHierarchyForSelectedTaxonomy(sTaxonomyId, sParentTaxonomyId, {fSuccessHandler: _successGetTaxonomies});
  };

  let _successGetTaxonomies = function (sTaxonomyId, sParentTaxonomyId, oResponse) {
    oResponse = oResponse.success;
    let oReferencedTaxonomy = oResponse.referencedTaxonomies;

    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    let aTaxonomyIds = oActiveRule.taxonomyIds;

    if (sParentTaxonomyId === "addItemHandlerforMultiTaxonomy") {
      sParentTaxonomyId = SettingUtils.getTreeRootId();
    }
    sParentTaxonomyId && CS.pull(aTaxonomyIds, sParentTaxonomyId);
    if (!CS.includes(aTaxonomyIds, sTaxonomyId)) {
      aTaxonomyIds.push(sTaxonomyId);
    }
    oActiveRule.configDetails = oActiveRule.configDetails || {referencedTaxonomies: {}};
    let oReferencedTaxonomies = oActiveRule.configDetails.referencedTaxonomies;

    oReferencedTaxonomies[sTaxonomyId] = oReferencedTaxonomy[sTaxonomyId];
    delete oReferencedTaxonomies[sParentTaxonomyId];
    _triggerChange();
  };

  let _handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oTaxonomy, sActiveTaxonomyId, sViewContext) {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    let aTaxonomyIds = oActiveRule.taxonomyIds;
    let oReferencedTaxonomies = oActiveRule.configDetails.referencedTaxonomies;
    let sParentTaxonomyId = oTaxonomy.parent.id;

    if (sParentTaxonomyId == SettingUtils.getTreeRootId()) {
      CS.remove(aTaxonomyIds, function (sTaxonomyId) {
        return sTaxonomyId == sActiveTaxonomyId;
      });
      _triggerChange();
    } else {
      SettingUtils.getTaxonomyHierarchyForSelectedTaxonomy(sParentTaxonomyId, sActiveTaxonomyId, {fSuccessHandler: _successGetTaxonomies});
    }
  };

  let _handleEntitiesAddedInMergeSection = function (sContext, aSelectedIds, sSelectedEntityType, sSelectedEntity, oReferencedData) {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    switch (sContext) {
      case 'attributes':
      case 'tags':
      case 'relationships':
      case 'natureRelationships':
        let aNewSelectedEntities = [];
        CS.forEach(aSelectedIds, function (sSelectedId) {
          let oAddedEntity = CS.find(oActiveRule.mergeEffect[sContext], {entityId: sSelectedId});
          if (!oAddedEntity) {
            let oEntity = {
              entityId: sSelectedId,
              type: sContext == "relationships" || sContext == "natureRelationships" ? 'supplierPriority' : 'latest',
              entityType: sContext,
              supplierIds: []
            };
            aNewSelectedEntities.push(oEntity);
          }
          else {
            aNewSelectedEntities.push(oAddedEntity)
          }

        });
        oActiveRule.mergeEffect[sContext] = aNewSelectedEntities;
        //_saveRule();
        break;

      case 'organizations':
        let aSelectedEntities = oActiveRule.mergeEffect[sSelectedEntityType];
        let oSelectedEntity = CS.find(aSelectedEntities, {entityId: sSelectedEntity});
        oSelectedEntity.supplierIds = aSelectedIds;
        oSelectedEntity.type = 'supplierPriority';
        //_saveRule();
        break;
    }

    /**Set Referenced Data on merge properties selection*/
    let sEntity = "";
    switch (sContext) {
      case 'attributes':
        sEntity = 'referencedAttributes';
        break;
      case 'tags':
        sEntity = 'referencedTags';
        break;
      case 'relationships':
        sEntity = 'referencedRelationships';
        break;
      case 'natureRelationships':
        sEntity = 'referencedNatureRelationships';
        break;
      case 'organizations':
        sEntity = 'referencedOrganizations';
        break;
    }

    oActiveRule.configDetails = oActiveRule.configDetails || {};
    CS.assign(oActiveRule.configDetails[sEntity],oReferencedData);
  };

  let _handleLatestEntityValueSelectionToggled = function (sContext, sSelectedEntity) {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    let oSelectedEntity = CS.find(oActiveRule.mergeEffect[sContext], {entityId: sSelectedEntity});
    if (oSelectedEntity) {
      if (oSelectedEntity.type == 'latest') {
        oSelectedEntity.type = 'supplierPriority';
      } else {
        oSelectedEntity.supplierIds = [];
        oSelectedEntity.type = 'latest'
      }
    }

  };

  let _handleGoldenRecordSelectedEntityRemoved = function (sContext,sEntityId) {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    CS.remove(oActiveRule.mergeEffect[sContext], {entityId: sEntityId});
  };

  let _handleGoldenRecordSelectedSupplierRemoved = function (sSelectedSupplierId, sContext, sSelectedEntityId) {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    let oSelectedEntity = CS.find(oActiveRule.mergeEffect[sContext], {entityId: sSelectedEntityId});
    CS.pull(oSelectedEntity.supplierIds, sSelectedSupplierId)
  };

  let _handleRuleDetailSupplierSequenceChanged = function (aNewSupplierSequence, sContext, sEntityId) {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    let oSelectedEntity = CS.find(oActiveRule.mergeEffect[sContext], {entityId: sEntityId});
    oSelectedEntity.supplierIds = aNewSupplierSequence;
  };

  let _handleRuleConfigNatureClassAddedInGoldenRecord = function (aSelectedIds) {
    let oActiveRule = _makeActiveGoldenRecordRuleDirty();
    oActiveRule.klassIds = aSelectedIds;
  };

  var _getGoldenRecordRuleScreenLockStatus = function () {
    return GoldenRecordsProps.getGoldenRecordRuleScreenLockStatus();
  };

  let _setUpGoldenRecordRuleConfigGridView = function (bIsTreeItemClicked) {
    let oGoldenRecordRuleConfigGridViewSkeleton = new GoldenRecordRuleConfigGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.GOLDEN_RECORD_RULE, {skeleton: oGoldenRecordRuleConfigGridViewSkeleton});
    _fetchGoldenRecordRuleListForGridView();
  };

  let _fetchGoldenRecordRuleListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.GOLDEN_RECORD_RULE);
    SettingUtils.csPostRequest(oGoldenRecordsMapping.GetAllGoldenRecordsRules, {}, oPostData, successFetchGoldenRecordRuleListForGridView, failureFetchGoldenRecordRuleListForGridView);

  };

  let failureFetchGoldenRecordRuleListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchRuleListForGridView", getTranslation());
  };

  let successFetchGoldenRecordRuleListForGridView = function (oResponse) {
    SettingUtils.getAppData().setGoldenRuleData(oResponse.success);
    let aGoldenRecordRuleList = oResponse.success.list;
    GoldenRecordsProps.setRuleList(aGoldenRecordRuleList);
    let oAppData = SettingUtils.getAppData();
    oAppData.setGoldenRecordRuleList(CS.keyBy(aGoldenRecordRuleList, 'id'));

    SettingUtils.getAppData().setRuleList(aGoldenRecordRuleList);
    GoldenRecordsProps.setGoldenRecordRuleGridData(aGoldenRecordRuleList);
    GridViewStore.preProcessDataForGridView(GridViewContexts.GOLDEN_RECORD_RULE, aGoldenRecordRuleList,
        oResponse.success.count);
    _triggerChange();
  };

  let _handleRuleConfigDialogActionButtonClicked = function (sButtonId) {
    if (sButtonId == "save") {
      _saveRule({});
    } else if (sButtonId == "cancel") {
      _discardUnsavedRule({});
    } else {
      _closeRuleDialog();
    }
  };

  let _closeRuleDialog = function () {
    GoldenRecordsProps.setIsRuleDialogActive(false);
    _triggerChange();
  };

  let _postProcessGoldenRuleListAndSave = function (oCallbackData) {
    let aGoldenRuleGridData = GoldenRecordsProps.getRuleGridData();
    let aRuleListToSave = [];

    let bSafeToSave = GridViewStore.processGridDataToSave(aRuleListToSave, GridViewContexts.GOLDEN_RECORD_RULE, aGoldenRuleGridData);
    if (bSafeToSave) {
      return _saveGoldenRulesInBulk(aRuleListToSave, oCallbackData);
    }
    return CommonUtils.rejectEmptyPromise();
  };

  let _saveGoldenRulesInBulk = function (aGoldenRuleListToSave, oCallbackData) {
      return SettingUtils.csPostRequest(oGoldenRecordsMapping.SaveBulkRule, {}, aGoldenRuleListToSave, successSaveRulesInBulk.bind(this, oCallbackData), failureSaveRulesInBulk);
  };

  let successSaveRulesInBulk = function (oCallbackData, oResponse) {
    let aGoldenRulesList = oResponse.success;
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.GOLDEN_RECORD_RULE, aGoldenRulesList);
    let aGoldenRulesGridData = GoldenRecordsProps.getRuleGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.GOLDEN_RECORD_RULE);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    var oMasterGoldenRulesMap = SettingUtils.getAppData().getRuleList();

    CS.forEach(aGoldenRulesList, function (oRules) {
      var sRuleId = oRules.id;
      var iIndex = CS.findIndex(aGoldenRulesGridData, {id: sRuleId});
      aGoldenRulesGridData[iIndex] = oRules;
      oMasterGoldenRulesMap[sRuleId] = oRules;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedRules) {
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedRules.id});
      aGridViewData[iIndex] = oProcessedRules;
    });

    alertify.success(getTranslation().RULE_SUCCESSFULLY_SAVED);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  let failureSaveRulesInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveGoldenRulesInBulk", getTranslation());
  };

  var _discardGoldenRuleListChanges = function (oCallbackData) {
    var aGoldenRulesGridData = GoldenRecordsProps.getRuleGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.GOLDEN_RECORD_RULE);
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    var bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedRule, iIndex) {
      if (oOldProcessedRule.isDirty) {
        var oEvent = CS.find(aGoldenRulesGridData, {id: oOldProcessedRule.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.GOLDEN_RECORD_RULE, [oEvent])[0];
        bShowDiscardMessage = true;
      }
    });

    bShowDiscardMessage && alertify.message(getTranslation().DISCARDED_SUCCESSFULLY);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var _handleGoldenRecordExportRule = function (oSelectiveExportDetails) {
    SettingUtils.csPostRequest(oSelectiveExportDetails.sUrl, {}, oSelectiveExportDetails.oPostRequest, successGoldenRecordExportFile, failureGoldenRecordExportFile);
  };

  var successGoldenRecordExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
  };

  var failureGoldenRecordExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGoldenRecordExportFile", getTranslation());
  };

  var uploadGoldenRecordFileImport = function (oImportExcel, oCallback) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
      successUploadGoldenRecordFileImport.bind(this, oCallback), failureUploadGoldenRecordFileImport, false);
  };

  var successUploadGoldenRecordFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  var failureUploadGoldenRecordFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadGoldenRecordFileImport", getTranslation());
  };

  var _getIsValidFileTypes = function (oFile) {
    // var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var _handleGoldenRecordFileUploaded = function (aFiles,oImportExcel) {
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
              uploadGoldenRecordFileImport(oImportExcel, {});
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

    handleRuleConfigDialogActionButtonClicked: function (sTabId) {
      _handleRuleConfigDialogActionButtonClicked(sTabId);
    },

    setUpGoldenRecordRuleConfigGridView: function (bIsTreeItemClicked) {
      _setUpGoldenRecordRuleConfigGridView(bIsTreeItemClicked);
    },

    fetchGoldenRecordRuleListForGridView: function () {
      _fetchGoldenRecordRuleListForGridView();
    },

    refreshGoldenRecordRule: function () {
      _refreshGoldenRecordRule();
    },

    deleteGoldenRecordRule: function (aSelectedIds) {
      _deleteGoldenRecordRule(aSelectedIds);
    },

    createGoldenRecordRule: function () {
      _createGoldenRecordRule();
    },

    handleCreateDialogButtonClicked: function (sButtonId) {
      _handleCreateDialogButtonClicked(sButtonId);
    },

    handleGoldenRecordRuleListNodeClicked: function (oModel) {
      _handleGoldenRecordRuleListNodeClicked(oModel);
    },

    handleRuleNameChanged: function (sValue) {
      _handleRuleNameChanged(sValue);
      _triggerChange();
    },

    handleRuleDetailPartnerApplyClicked: function (aSelectedItems) {
      _handleRuleDetailPartnerApplyClicked(aSelectedItems);
      _triggerChange();
    },

    handleRuleDetailEndpointsChanged: function (aSelectedItems) {
      _handleRuleDetailEndpointsChanged(aSelectedItems);
      _triggerChange();
    },

    saveRule: function (oCallbackData) {
      _saveRule(oCallbackData);
    },

    handleSelectionToggleButtonClicked: function (sKey, sId) {
      _handleSelectionToggleButtonClicked(sKey, sId);
      _triggerChange();
    },

    discardUnsavedRule: function (oCallbackData) {
      _discardUnsavedRule(oCallbackData);
    },

    handleRuleSingleValueChanged: function (sKey, sValue) {
      var oActiveRule = _makeActiveGoldenRecordRuleDirty();
      oActiveRule[sKey] = sValue;
      _triggerChange();
    },

    handleRuleDetailMssValueChanged: function (aNewValue, sKey) {
      _handleRuleDetailMssValueChanged(aNewValue, sKey);
      _triggerChange();
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId) {
      GoldenRecordsProps.setAllowedTaxonomies([]);
      let oTaxonomyPaginationData = GoldenRecordsProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.searchText = "";
      oTaxonomyPaginationData.from = 0;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleTaxonomyAdded: function (sTaxonomyId, sParentTaxonomyId, sViewContext) {
      _handleTaxonomyAdded(sTaxonomyId, sParentTaxonomyId, sViewContext);
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sActiveTaxonomyId, sViewContext) {
      _handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sActiveTaxonomyId, sViewContext);
    },

    handleEntitiesAddedInMergeSection: function (sContext, aSelectedEntities, sSelectedEntityType, sSelectedEntity, oReferencedData) {
      _handleEntitiesAddedInMergeSection(sContext, aSelectedEntities, sSelectedEntityType, sSelectedEntity, oReferencedData);
      _triggerChange();
    },

    handleLatestEntityValueSelectionToggled: function (sContext, sSelectedEntity) {
      _handleLatestEntityValueSelectionToggled(sContext, sSelectedEntity);
      _triggerChange();
    },

    handleGoldenRecordSelectedEntityRemoved: function (sContext, sEntityId) {
      _handleGoldenRecordSelectedEntityRemoved(sContext, sEntityId);
      _triggerChange();
    },

    handleGoldenRecordSelectedSupplierRemoved: function (sSelectedSupplierId, sContext, sSelectedEntityId) {
      _handleGoldenRecordSelectedSupplierRemoved(sSelectedSupplierId, sContext, sSelectedEntityId);
      _triggerChange();
    },

    handleRuleDetailSupplierSequenceChanged: function (aNewSupplierSequence, sContext, sEntityId) {
      _handleRuleDetailSupplierSequenceChanged(aNewSupplierSequence, sContext, sEntityId);
      _triggerChange();
    },

    handleRuleConfigNatureClassAddedInGoldenRecord: function (aSelectedIds) {
      _handleRuleConfigNatureClassAddedInGoldenRecord(aSelectedIds);
      _triggerChange()
    },

    postProcessGoldenRuleListAndSave: function (oCallbackData) {
      _postProcessGoldenRuleListAndSave(oCallbackData)
          .then(_triggerChange)
          .catch(CS.noop);
    },

    discardGoldenRuleListChanges: function (oCallBack) {
      _discardGoldenRuleListChanges(oCallBack);
      _triggerChange();
    },

    getGoldenRecordRuleScreenLockStatus: function () {
      return _getGoldenRecordRuleScreenLockStatus();
    },

    handleGoldenRecordFileUploaded: function (aFiles,oImportExcel) {
      _handleGoldenRecordFileUploaded(aFiles,oImportExcel);
    },

    handleGoldenRecordExportRule: function (oSelectiveExportDetails) {
      _handleGoldenRecordExportRule(oSelectiveExportDetails);
    },
  };
})();

MicroEvent.mixin(GoldenRecordStore);

export default GoldenRecordStore;
