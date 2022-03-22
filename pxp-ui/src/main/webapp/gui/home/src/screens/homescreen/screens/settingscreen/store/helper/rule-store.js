import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import LogFactory from '../../../../../../libraries/logger/log-factory';
import MethodTracker from '../../../../../../libraries/methodtracker/method-tracker';
import ColorConstantDictionary from './../../tack/mock/color-constants';
import { RuleRequestMapping as oRuleRequestMapping } from '../../tack/setting-screen-request-mapping';
import ViewUtils from '../../view/utils/view-utils.js';
import RuleProps from './../model/rule-config-view-props';
import SettingUtils from './../helper/setting-utils';
import RuleConstantDictionary from '../../tack/mock/rule-constants';
import TagTypeConstants from './../../../../../../commonmodule/tack/tag-type-constants';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import TaxonomyTypeDictionary from '../../../../../../commonmodule/tack/taxonomy-type-dictionary';
import CommonUtils from '../../../../../../commonmodule/util/common-utils';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import MockDataForEntityBaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import NormalizationTypeDictionary from '../../../../../../commonmodule/tack/normalization-type-dictionary';
import RuleTypeDictionary from '../../../../../../commonmodule/tack/rule-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import SettingScreenProps from './../model/setting-screen-props';
import RuleConfigGridViewSkeleton from '../../tack/rules-config-grid-view-skeleton';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import assetTypes from '../../tack/coverflow-asset-type-list';
import { getTranslations as getTranslation, getLanguageFromServer } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import SessionProps from '../../../../../../commonmodule/props/session-props';
import ClassTypeDictionary from '../../../../../../commonmodule/tack/class-type-dictionary';

var logger = LogFactory.getLogger('role-store');
var trackMe = MethodTracker.getTracker('role-store');
const oNormalizationTypeDictionary = new NormalizationTypeDictionary();

var RuleStore = (function () {

  var _triggerChange = function () {
    RuleStore.trigger('rule-changed');
  };

  var _getDefaultNormalizationTagValues = function (oRule) {
    var aNormalizationDefaultTagValues = [];
    if(oRule.type == "tag") {
      var aTagList = SettingUtils.getAppData().getTagList()[0].children;
      var oMasterTagData = CS.find(aTagList, {id: oRule.entityId});
      if(!CS.isEmpty(oMasterTagData)) {
        var aTagValues = oMasterTagData.children;
        CS.forEach(aTagValues, function (oTagValue) {
          var oNewMissingObj = {
            from: 0,
            id: oTagValue.id,
            innerTagId: oTagValue.id,
            to: 0,
          };
          aNormalizationDefaultTagValues.push(oNewMissingObj);
        });
      }
    }
    return aNormalizationDefaultTagValues;
  };

  var _initializeRuleViolationAndNormalizationProps = function (oNewRule) {
    var aRuleEffect = [] ;//RuleProps.getRuleViolationsAndNormalizationProps();
    var aRuleViolation = oNewRule.ruleViolations;
    var aNormalization = oNewRule.normalizations;

    var oIdMap = {};

    CS.forEach(aRuleViolation, function (oRule) {
      var oNormalizedData = CS.find(aNormalization, {entityId: oRule.entityId});

      oIdMap[oRule.id] = true;
      var oDefaultNormalization = {
        values: [],
        tagValues: _getDefaultNormalizationTagValues(oRule),
        isEnabled: false,
        attributeOperatorList: [],
        calculatedAttributeUnit: "",
        calculatedAttributeUnitAsHTML: "",
        baseType: oNormalizationTypeDictionary['normalization'],
        rules: {
          type: 'value',
          from: '',
          to: ''
        }
      };

      if(oNormalizedData) {
        oDefaultNormalization.values = CS.cloneDeep(oNormalizedData.values);
        oDefaultNormalization.tagValues = CS.cloneDeep(oNormalizedData.tagValues);
        oDefaultNormalization.isEnabled = true;
        oDefaultNormalization.attributeOperatorList = CS.cloneDeep(oNormalizedData.attributeOperatorList);
        oDefaultNormalization.calculatedAttributeUnit = oNormalizedData.calculatedAttributeUnit;
        oDefaultNormalization.calculatedAttributeUnitAsHTML = oNormalizedData.calculatedAttributeUnitAsHTML;
        oDefaultNormalization.attributeConcatenatedList = CS.cloneDeep(oNormalizedData.attributeConcatenatedList);
        oDefaultNormalization.transformationType = oNormalizedData.transformationType;
        oDefaultNormalization.valueAttributeId = oNormalizedData.valueAttributeId;
      }

      aRuleEffect.push({
        id: oRule.id,
        entityId: oRule.entityId,
        type: oRule.type,
        ruleViolation: {
          description: oRule.description,
          color: oRule.color,
          isEnabled: true
        },
        normalization: oDefaultNormalization
      });
    });

    CS.forEach(aNormalization, function (oNormalizedData) {
      var oRule = CS.find(aRuleViolation, {entityId: oNormalizedData.entityId});

      if(!oIdMap[oNormalizedData.id]) {
        oIdMap[oNormalizedData.id] = true;
        var oDefaultRuleData = {
          color: ColorConstantDictionary.RED,
          description: '',
          isEnabled: false
        };
        let sFrom;
        let sTo;

        if(oNormalizedData.baseType == oNormalizationTypeDictionary['findReplaceNormalization']){
          sFrom = oNormalizedData.findText;
          sTo = oNormalizedData.replaceText;
        }
        else if(oNormalizedData.baseType == oNormalizationTypeDictionary['subStringNormalization']){
          sFrom = oNormalizedData.startIndex + 1;
          sTo = oNormalizedData.endIndex + 1;
        }
        let oNormalizationRule = {
          type: oNormalizedData.transformationType,
          from: sFrom,
          to: sTo
        };

        if(oRule) {
          oDefaultRuleData.color = oRule.color;
          oDefaultRuleData.description = oRule.description;
          oDefaultRuleData.isEnabled = true;
        }

        aRuleEffect.push({
          id: oNormalizedData.id,
          entityId: oNormalizedData.entityId,
          type: oNormalizedData.type,
          ruleViolation: oDefaultRuleData,
          normalization: {
            baseType:oNormalizedData.baseType,
            values: CS.cloneDeep(oNormalizedData.values),
            valueAsHTML : oNormalizedData.valueAsHTML,
            tagValues: CS.cloneDeep(oNormalizedData.tagValues),
            isEnabled: true,
            attributeOperatorList: CS.cloneDeep(oNormalizedData.attributeOperatorList),
            calculatedAttributeUnit: oNormalizedData.calculatedAttributeUnit,
            calculatedAttributeUnitAsHTML: oNormalizedData.calculatedAttributeUnitAsHTML,
            rules: oNormalizationRule,
            attributeConcatenatedList: CS.cloneDeep(oNormalizedData.attributeConcatenatedList),
            transformationType: oNormalizedData.transformationType,
            valueAttributeId: oNormalizedData.valueAttributeId
          }
        });
      }
    });

    RuleProps.setRuleViolationsAndNormalizationProps(aRuleEffect);

  };

  var successCreateRuleCallback = function (oResponse) {
    var oAppData = SettingUtils.getAppData();
    var oRuleMap = oAppData.getRuleList();
    var oRuleValueProps = RuleProps.getRuleValuesList();
    var oNewRule = oResponse.success;
    oRuleValueProps[oNewRule.id] = _getDefaultListProps(oNewRule);
    let oSavedRule = oResponse.success;
    let aRuleGridData = RuleProps.getRuleGridData();
    aRuleGridData.push(oSavedRule);
    let oProcessedRule = GridViewStore.getProcessedGridViewData(GridViewContexts.RULE, [oSavedRule])[0];

    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.RULE);
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedRule);

    oRuleMap[oNewRule.id] = {
      id: oNewRule.id,
      label: oNewRule.label,
      code: oNewRule.code
    };

    _initializeRuleViolationAndNormalizationProps(oNewRule);

    _selectRuleById(oNewRule.id);
    RuleProps.setActiveRule(oNewRule);
    RuleProps.setRuleScreenLockStatus(false);
    alertify.success(getTranslation().RULE_CREATED_SUCCESSFULLY);

    /** To open edit view immediately after create */
    RuleProps.setKlassTypeId("");
    successFetchRuleByIdCallback({}, oResponse)
  };

  var failureCreateRuleCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCreateRuleCallback", getTranslation());
  };

  var successFetchAllRulesCallback = function (bLoadMore, oResponse) {
    var oAppData = SettingUtils.getAppData();
    var aListFromServer = oResponse.success.list;
    var iCount = oResponse.success.count; // Count is currently unused.

    if(bLoadMore){
      var oRuleMap = oAppData.getRuleList();
      var aRuleList = CS.values(oRuleMap);
      aListFromServer = aRuleList.concat(aListFromServer);
    }

    RuleProps.setRuleValuesList({});
    var oRuleValueList = RuleProps.getRuleValuesList();
    oAppData.setRuleList(CS.keyBy(aListFromServer, 'id'));
    CS.forEach(aListFromServer, function (oRule) {
      oRuleValueList[oRule.id] = _getDefaultListProps(oRule);
    });

    var oSelectedRule = RuleProps.getActiveRule();
    if(!CS.isEmpty(oSelectedRule)){
      if(!CS.find(aListFromServer, {id:oSelectedRule.id})){
        //TODO: handle more for dirty check.
        RuleProps.setActiveRule({})
      }else {
        oRuleValueList[oSelectedRule.id].isSelected = true;
      }
    }

    _triggerChange();
  };

  var checkAndDeleteRuleFromList = function (oResponse, sRuleId) {
    var oRuleValueList = RuleProps.getRuleValuesList();
    var oRuleList = SettingUtils.getAppData().getRuleList();
    var oSelectedRule = RuleProps.getActiveRule();

    if(oResponse.success.message.indexOf('NOT_FOUND') >= 0 && (oSelectedRule || sRuleId)){
      var sRuleName = oRuleList[sRuleId || oSelectedRule.id].label;
      delete oRuleValueList[sRuleId || oSelectedRule.id];
      delete oRuleList[sRuleId || oSelectedRule.id];
      //delete oMasterAttributes[sAttributeId || oSelectedAttribute.id];
      RuleProps.setActiveRule({});
      return sRuleName;
    }
    return null;

  };

  var failureFetchAllRulesCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchRoleListCallback", getTranslation());
  };

  var _updateAttributeListMap = function (oReferencedAttributes) {
    var oAppData = SettingUtils.getAppData();
    var oAttributeListMap = oAppData.getAttributeList();
    CS.forEach(oReferencedAttributes, function (oAttribute) {
      if(!oAttributeListMap[oAttribute.id]) {
        oAttributeListMap[oAttribute.id] = {
          id: oAttribute.id,
          label: oAttribute.label,
          type: oAttribute.type,
          icon: oAttribute.icon
        };
      }
    });
  };

  var successFetchRuleByIdCallback = function (oCallbackData, oResponse) {
    var oAppData = SettingUtils.getAppData();
    var oRuleMap = oAppData.getRuleList();

    var oRule = oResponse.success;

    var oConfigDetails = oRule.configDetails;
    var oComponentProps = SettingUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    oScreenProps.setReferencedRoles(oConfigDetails.referencedRoles);
    oScreenProps.setReferencedTags(oConfigDetails.referencedTags);
    oScreenProps.setLoadedTagsData(oConfigDetails.referencedTags);
    oScreenProps.setReferencedAttributes(oConfigDetails.referencedAttributes);
    var oReferencedClasses = oConfigDetails.referencedKlasses;

    CS.forEach(oReferencedClasses, function (oReferencedClass) {
      if(oReferencedClass.type == MockDataForEntityBaseTypesDictionary.articleKlassBaseType){
        RuleProps.setKlassTypeId(ClassTypeDictionary.ARTICLE);
      }
      else if(oReferencedClass.type == MockDataForEntityBaseTypesDictionary.assetKlassBaseType){
        RuleProps.setKlassTypeId(ClassTypeDictionary.ASSET);
      }else if(oReferencedClass.type == MockDataForEntityBaseTypesDictionary.marketKlassBaseType){
        RuleProps.setKlassTypeId(ClassTypeDictionary.MARKET);
      }else if(oReferencedClass.type == MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType){
        RuleProps.setKlassTypeId(ClassTypeDictionary.TEXT_ASSET);
      }else if(oReferencedClass.type == MockDataForEntityBaseTypesDictionary.supplierKlassBaseType){
        RuleProps.setKlassTypeId(ClassTypeDictionary.SUPPLIER);
      }
      return false;
    });

    _updateAttributeListMap(oScreenProps.getReferencedAttributes());

    _updateNormalizationTagsData(oRule);
    var sRuleId = oRule.id;
   // _selectRuleById(sRuleId);
    RuleProps.setActiveRule(oRule);
    RuleProps.setRightPanelVisibility(false);
    let oRightBarIconClickMap = RuleProps.getRightBarIconClickMap();
    oRightBarIconClickMap.ruleList = false;

    _initializeRuleViolationAndNormalizationProps(oRule);

    //TODO: Remove after fix. Support for previously created rules
    CS.forEach(oRule.attributes, function (oAttribute) {
      CS.forEach(oAttribute.rules, function (oAttribRule) {
        if(oAttribRule.ruleListLinkId == null) {
          oAttribRule.ruleListLinkId = "";
        }
      });
    });

    oRuleMap[sRuleId].label = oRule.label;

    if(oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    RuleProps.setIsRuleDialogActive(true);

    _triggerChange();
  };

  var failureFetchRuleByIdCallback = function (sRuleId,oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var sRuleName = checkAndDeleteRuleFromList(oResponse, sRuleId);
      alertify.error("[" + sRuleName +"] "+getTranslation().ERROR_ALREADY_DELETED, 0);
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchRuleByIdCallback", getTranslation());
    }
    _triggerChange();
  };

  var _updateNormalizationTagsData = function (oRule) {
    var oComponentProps = SettingUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oTagListMap = oScreenProps.getReferencedTags();

    var aNormalization = oRule.normalizations;
    CS.forEach(aNormalization, function (oNormalization) {
      var oMasterTagData = oTagListMap[oNormalization.entityId];
      if(!CS.isEmpty(oMasterTagData)) {
        var aTagValues = oMasterTagData.children;
        CS.forEach(aTagValues, function (oTagValue) {
          var oNewTagValue = CS.find(oNormalization.tagValues, {id: oTagValue.id});

          if(CS.isEmpty(oNewTagValue)) {
            var oNewMissingObj = {
              from: 0,
              id: oTagValue.id,
              innerTagId: oTagValue.id,
              lastModifiedBy: null,
              to: 0,
              versionId: null,
              versionTimestamp: null
            };
            oNormalization.tagValues.push(oNewMissingObj);
          }

        });
      }
    });
  };

  var successSaveRuleCallback = function (oCallback, oResponse) {
    var oAppData = SettingUtils.getAppData();
    var oRuleMap = oAppData.getRuleList();
    var oComponentProps = SettingUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;

    var oRule = oResponse.success;

    var sRuleId = oRule.id;
    CS.assign(oRuleMap[sRuleId], oRule);

    let aRuleList = CS.values(oRuleMap);
    RuleProps.setRuleGridData(aRuleList);
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.RULE, aRuleList);
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.RULE);
    oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);

    var oConfigDetails = oRule.configDetails;
    oScreenProps.setReferencedRoles(oConfigDetails.referencedRoles);
    oScreenProps.setReferencedTags(oConfigDetails.referencedTags);
    oScreenProps.setReferencedAttributes(oConfigDetails.referencedAttributes);

    _updateNormalizationTagsData(oRule);

    RuleProps.setActiveRule(oRule);
    _initializeRuleViolationAndNormalizationProps(oRule);

    oRuleMap[sRuleId].label = oRule.label;

    if(oCallback && oCallback.functionToExecute){
      oCallback.functionToExecute();
    }

    RuleProps.setRuleScreenLockStatus(false);
    alertify.success(getTranslation().RULE_SUCCESSFULLY_SAVED);

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

  var successDeleteRuleButtonClicked = function (oResponse) {
    oResponse = oResponse.success;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.RULE);

    let aRuleGridData = RuleProps.getRuleGridData();
    let oSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
    oSkeleton.selectedContentIds = CS.difference(oSkeleton.selectedContentIds, oResponse);

    let aProcessedRuleData = GridViewStore.getProcessedGridViewData(GridViewContexts.RULE, aRuleGridData);
    oGridViewPropsByContext.setGridViewData(aProcessedRuleData);
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - oResponse.length);

    var oRuleMap = SettingUtils.getAppData().getRuleList();
    var oRuleValueList = RuleProps.getRuleValuesList();

    var aSuccessFullyDeleted = oResponse;
    CS.forEach(aSuccessFullyDeleted, function (sDeletedId) {
      delete oRuleMap[sDeletedId];
      delete oRuleValueList[sDeletedId];
    });

    RuleProps.setActiveRule({});
    RuleProps.setRuleScreenLockStatus(false);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().RULE}));
  };

  var failureDeleteRuleButtonClicked = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureDeleteRuleButtonClicked", getTranslation());
  };

  var _makeActiveRuleDirty = function () {
    RuleProps.setRuleScreenLockStatus(true);

    var oActiveRule = RuleProps.getActiveRule();
    if(!oActiveRule.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveRule);
    }

    return oActiveRule.clonedObject;
  };

  var _isActiveRuleDirty = function () {
    var oActiveRule = RuleProps.getActiveRule();
    if(!CS.isEmpty(oActiveRule)) {
      return !!oActiveRule.clonedObject;
    }

    return false;
  };

  var _getRuleScreenLockStatus = function () {
    return RuleProps.getRuleScreenLockStatus();
  };

  var _getDefaultListProps = function (oRule) {
    return {
      isSelected: false,
      isChecked: false,
      isEditable: false
    }
  };

  var _selectRuleById = function (sRuleId) {
    var oRuleValueMap = RuleProps.getRuleValuesList();
    CS.forEach(oRuleValueMap, function (oValue, sId) {
      oValue.isChecked = false;
      oValue.isSelected = (sId == sRuleId);
    })
  };

  var  _generateActiveRuleADMForRuleViolations = function (aOldRuleViolations, aNewRuleViolations, oADM) {

    for(var iIndex = 0; iIndex < aNewRuleViolations.length; iIndex++) { //This loop is used to find newly added and modified attributes.
      var oNewRuleViolations = aNewRuleViolations[iIndex];
      var oOldRuleViolations = CS.find(aOldRuleViolations, {id: oNewRuleViolations.id});
      if(CS.isEmpty(oOldRuleViolations)) {
        oADM.addedRuleViolations.push(oNewRuleViolations);
      } else {
        if(!CS.isEqual(oOldRuleViolations.color, oNewRuleViolations.color) ||
                !CS.isEqual(oOldRuleViolations.description, oNewRuleViolations.description)) {
          oADM.modifiedRuleViolations.push(oNewRuleViolations);
        }
      }
    }

    for(var iCount = 0; iCount < aOldRuleViolations.length; iCount++) {
      var oOldAttribute = aOldRuleViolations[iCount];
      var oNewAttribute = CS.find(aNewRuleViolations, {id: oOldAttribute.id});
      if(CS.isEmpty(oNewAttribute)) {
        oADM.deletedRuleViolations.push(oOldAttribute.id);
      }
    }
  };

  var _generateActiveRuleADMForNormalization = function (aOldNormalization, aNewNormalization, oADM) {

    CS.forEach(aNewNormalization, function (oNewNormalization) {
      var oOldNormalization = CS.find(aOldNormalization, {id: oNewNormalization.id});
      if (CS.isEmpty(oOldNormalization)) {
        oADM.addedNormalizations.push(oNewNormalization);
      } else {
        if (!CS.isEqual(oOldNormalization.values, oNewNormalization.values) ||
            !CS.isEqual(oOldNormalization.tagValues, oNewNormalization.tagValues) ||
            oOldNormalization.transformationType !== oNewNormalization.transformationType ||
            !CS.isEqual(oOldNormalization.attributeConcatenatedList, oNewNormalization.attributeConcatenatedList) ||
            !CS.isEqual(oOldNormalization.findText,oNewNormalization.findText )||
            !CS.isEqual(oOldNormalization.replaceText, oNewNormalization.replaceText)||
            !CS.isEqual(oOldNormalization.valueAttributeId, oNewNormalization.valueAttributeId)
            ) {
          oADM.modifiedNormalizations.push(oNewNormalization);
        }
      }
    });

    CS.forEach(aOldNormalization, function (oOldNormalization) {
      var oNewNormalization = CS.find(aNewNormalization, {id: oOldNormalization.id});
      if (CS.isEmpty(oNewNormalization)) {
        oADM.deletedNormalizations.push(oOldNormalization.id);
      }
    });

  };

  var _generateActiveRuleADMForAttribute = function (aOldRuleAttribute, aNewRuleAttribute, oADM) {

    CS.forEach(aNewRuleAttribute, function (oNewAttribute) {
      var oRuleADM = {
        id: '',
        addedRules: [],
        modifiedRules: [],
        deletedRules: []
      };
      var oOldAttribute = CS.find(aOldRuleAttribute, {id: oNewAttribute.id});
      if (CS.isEmpty(oOldAttribute)) {
        oADM.addedAttributeRules.push(oNewAttribute);
      } else {
        oRuleADM.id = oNewAttribute.id;
        var bIsDataModified = false;
        CS.forEach(oNewAttribute.rules, function (oRule) {
          var oOldRule = CS.find(oOldAttribute.rules, {id: oRule.id});
          if (CS.isEmpty(oOldRule)) {
            oRuleADM.addedRules.push(oRule);
            bIsDataModified = true;
          } else {
            if (!CS.isEqual(oOldRule, oRule)) {
              oRuleADM.modifiedRules.push(oRule);
              bIsDataModified = true;
            }
          }
        });

        CS.forEach(oOldAttribute.rules, function (oRule) {
          var oNewRule = CS.find(oNewAttribute.rules, {id: oRule.id});
          if (CS.isEmpty(oNewRule)) {
            oRuleADM.deletedRules.push(oRule.id);
            bIsDataModified = true;
          }
        });

        if (bIsDataModified) {
          oADM.modifiedAttributeRules.push(oRuleADM);
        }
      }
    });


    CS.forEach(aOldRuleAttribute, function (oOldAttribute) {
      var oNewAttribute = CS.find(aNewRuleAttribute, {id: oOldAttribute.id});
      if (CS.isEmpty(oNewAttribute)) {
        oADM.deletedAttributeRules.push(oOldAttribute.id);
      }
    });

  };

  var _generateActiveRuleADMForTags = function (aOldRuleTags, aNewRuleTags, oADM) {

    CS.forEach(aNewRuleTags, function (oNewAttribute) {
      var oRuleADM = {
        id: '',
        addedRules: [],
        modifiedRules: [],
        deletedRules: []
      };
      var oOldAttribute = CS.find(aOldRuleTags, {id: oNewAttribute.id});
      if (CS.isEmpty(oOldAttribute)) {
        oADM.addedTagRules.push(oNewAttribute);
      } else {
        oRuleADM.id = oNewAttribute.id;
        var bIsDataModified = false;
        CS.forEach(oNewAttribute.rules, function (oRule) {
          var oOldRule = CS.find(oOldAttribute.rules, {id: oRule.id});
          if (CS.isEmpty(oOldRule)) {
            oRuleADM.addedRules.push(oRule);
            bIsDataModified = true;
          } else {
            if (!CS.isEqual(oOldRule, oRule)) {
              oRuleADM.modifiedRules.push(oRule);
              bIsDataModified = true;
            }
          }
        });

        CS.forEach(oOldAttribute.rules, function (oRule) {
          var oNewRule = CS.find(oNewAttribute.rules, {id: oRule.id});
          if (CS.isEmpty(oNewRule)) {
            oRuleADM.deletedRules.push(oRule.id);
            bIsDataModified = true;
          }
        });

        if (bIsDataModified) {
          oADM.modifiedTagRules.push(oRuleADM);
        }
      }
    });


    CS.forEach(aOldRuleTags, function (oOldAttribute) {
      var oNewAttribute = CS.find(aNewRuleTags, {id: oOldAttribute.id});
      if (CS.isEmpty(oNewAttribute)) {
        oADM.deletedTagRules.push(oOldAttribute.id);
      }
    });

  };

  var _generateActiveRuleADMForRoles = function (aOldRoles, aNewRoles, oADM) {

    CS.forEach(aNewRoles, function (oNewAttribute) {
      var oRuleADM = {
        id: '',
        addedRules: [],
        modifiedRules: [],
        deletedRules: []
      };
      var oOldAttribute = CS.find(aOldRoles, {id: oNewAttribute.id});
      if (CS.isEmpty(oOldAttribute)) {
        oADM.addedRoleRules.push(oNewAttribute);
      } else {
        oRuleADM.id = oNewAttribute.id;
        var bIsDataModified = false;
        CS.forEach(oNewAttribute.rules, function (oRule) {
          var oOldRule = CS.find(oOldAttribute.rules, {id: oRule.id});
          if (CS.isEmpty(oOldRule)) {
            oRuleADM.addedRules.push(oRule);
            bIsDataModified = true;
          } else {
            if (!CS.isEqual(oOldRule, oRule)) {
              oRuleADM.modifiedRules.push(oRule);
              bIsDataModified = true;
            }
          }
        });

        CS.forEach(oOldAttribute.rules, function (oRule) {
          var oNewRule = CS.find(oNewAttribute.rules, {id: oRule.id});
          if (CS.isEmpty(oNewRule)) {
            oRuleADM.deletedRules.push(oRule.id);
            bIsDataModified = true;
          }
        });

        if (bIsDataModified) {
          oADM.modifiedRoleRules.push(oRuleADM);
        }
      }
    });


    CS.forEach(aOldRoles, function (oOldAttribute) {
      var oNewAttribute = CS.find(aNewRoles, {id: oOldAttribute.id});
      if (CS.isEmpty(oNewAttribute)) {
        oADM.deletedRoleRules.push(oOldAttribute.id);
      }
    });
  };

  var _generateActiveRuleADMForRelationship = function (aOldRelationship, aNewRelationship, oADM) {

    CS.forEach(aNewRelationship, function (oNewAttribute) {
      var oRuleADM = {
        id: '',
        addedRules: [],
        modifiedRules: [],
        deletedRules: []
      };
      var oOldAttribute = CS.find(aOldRelationship, {entityId: oNewAttribute.entityId});
      if (CS.isEmpty(oOldAttribute)) {
        oADM.addedRelationshipRules.push(oNewAttribute);
      } else {
        oRuleADM.id = oNewAttribute.id;
        var bIsDataModified = false;
        CS.forEach(oNewAttribute.rules, function (oRule) {
          var oOldRule = CS.find(oOldAttribute.rules, {id: oRule.id});
          if (CS.isEmpty(oOldRule)) {
            oRuleADM.addedRules.push(oRule);
            bIsDataModified = true;
          } else {
            if (!CS.isEqual(oOldRule, oRule)) {
              oRuleADM.modifiedRules.push(oRule);
              bIsDataModified = true;
            }
          }
        });

        CS.forEach(oOldAttribute.rules, function (oRule) {
          var oNewRule = CS.find(oNewAttribute.rules, {id: oRule.id});
          if (CS.isEmpty(oNewRule)) {
            oRuleADM.deletedRules.push(oRule.id);
            bIsDataModified = true;
          }
        });

        if (bIsDataModified) {
          oADM.modifiedRelationshipRules.push(oRuleADM);
        }
      }
    });


    CS.forEach(aOldRelationship, function (oOldAttribute) {
      var oNewAttribute = CS.find(aNewRelationship, {entityId: oOldAttribute.entityId});
      if (CS.isEmpty(oNewAttribute)) {
        oADM.deletedRelationshipRules.push(oOldAttribute.id);
      }
    });

  };

  let _generateActiveRuleADMForTaxonomies = function (aOldTaxonomies, aNewTaxonomies, oADM) {

    CS.forEach(aNewTaxonomies, function (sNewTaxonomyId) {
      if (!CS.includes(aOldTaxonomies, sNewTaxonomyId)) {
        oADM.addedTaxonomies.push(sNewTaxonomyId);
      }
    });
    CS.forEach(aOldTaxonomies, function (sOldTaxonomyId) {
      if (!CS.includes(aNewTaxonomies, sOldTaxonomyId)) {
        oADM.deletedTaxonomies.push(sOldTaxonomyId);
      }
    });

  };

  var _generateActiveRuleADMForTypes = function (aOldTypes, aNewTypes, oADM) {

    CS.forEach(aNewTypes, function (sNewTypeId) {
      if (!CS.includes(aOldTypes, sNewTypeId)) {
        oADM.addedTypes.push(sNewTypeId);
      }
    });
    CS.forEach(aOldTypes, function (sOldTypeIds) {
      if (!CS.includes(aNewTypes, sOldTypeIds)) {
        oADM.deletedTypes.push(sOldTypeIds);
      }
    });
  };

  let _generateActiveRuleADMForLanguages = function (aOldLanguages, aNewLanguages, oADM) {
    CS.forEach(aNewLanguages, function (sLanguageCode) {
      if (!CS.includes(aOldLanguages, sLanguageCode)) {
        oADM.addedLanguages.push(sLanguageCode);
      }
    });
    CS.forEach(aOldLanguages, function (sOldLanguagesCode) {
      if (!CS.includes(aNewLanguages, sOldLanguagesCode)) {
        oADM.deletedLanguages.push(sOldLanguagesCode);
      }
    });
  };

  var _getADMForActiveRule =  function (oOldActiveRule, oNewActiveRule) {
    var oADMForAttribute = {
      addedAttributeRules: [],
      modifiedAttributeRules: [],
      deletedAttributeRules: []
    };
    _generateActiveRuleADMForAttribute(oOldActiveRule.attributes, oNewActiveRule.attributes, oADMForAttribute);

    var oADMForRuleViolations = {
      addedRuleViolations: [],
      modifiedRuleViolations: [],
      deletedRuleViolations: []
    };
    _generateActiveRuleADMForRuleViolations(oOldActiveRule.ruleViolations, oNewActiveRule.ruleViolations, oADMForRuleViolations);

    var oADMForTags = {
      addedTagRules: [],
      modifiedTagRules: [],
      deletedTagRules: []
    };

    _generateActiveRuleADMForTags(oOldActiveRule.tags, oNewActiveRule.tags, oADMForTags);

    var oADMForRoles = {
      addedRoleRules: [],
      modifiedRoleRules: [],
      deletedRoleRules: []
    };

    _generateActiveRuleADMForRoles(oOldActiveRule.roles, oNewActiveRule.roles, oADMForRoles);

    var oADMForRelationships = {
      addedRelationshipRules: [],
      modifiedRelationshipRules: [],
      deletedRelationshipRules: []
    };

    _generateActiveRuleADMForRelationship(oOldActiveRule.relationships, oNewActiveRule.relationships, oADMForRelationships);

    var oADMForTypes = {
      addedTypes: [],
      deletedTypes: []
    };

    _generateActiveRuleADMForTypes(oOldActiveRule.types, oNewActiveRule.types, oADMForTypes);

    let oADMForTaxonomies = {
      addedTaxonomies: [],
      deletedTaxonomies: []
    };

    _generateActiveRuleADMForTaxonomies(oOldActiveRule.taxonomies, oNewActiveRule.taxonomies, oADMForTaxonomies);

    var oADMForNormalizations = {
      addedNormalizations: [],
      modifiedNormalizations: [],
      deletedNormalizations: []
    };

    _generateActiveRuleADMForNormalization (oOldActiveRule.normalizations, oNewActiveRule.normalizations, oADMForNormalizations);

    let oADMForLanguages = {
      addedLanguages: [],
      deletedLanguages: []
    };

    _generateActiveRuleADMForLanguages(oOldActiveRule.languages, oNewActiveRule.languages, oADMForLanguages);

    var oDataToSave = {
      id: oNewActiveRule.id,
      label: oNewActiveRule.label
    };

    oDataToSave.physicalCatalogIds = oNewActiveRule.physicalCatalogIds;
    oDataToSave.addedOrganizationIds = CS.difference(oNewActiveRule.organizations, oOldActiveRule.organizations);
    oDataToSave.deletedOrganizationIds = CS.difference(oOldActiveRule.organizations, oNewActiveRule.organizations);

    oDataToSave.addedEndpoints = CS.difference(oNewActiveRule.endpoints, oOldActiveRule.endpoints);
    oDataToSave.deletedEndpoints = CS.difference(oOldActiveRule.endpoints, oNewActiveRule.endpoints);

    oDataToSave = CS.assign(oDataToSave, oADMForRuleViolations, oADMForAttribute, oADMForTags, oADMForRoles, oADMForRelationships,
        oADMForTypes, oADMForTaxonomies, oADMForNormalizations, oADMForLanguages);

    return oDataToSave;
  };

  var _assignRuleViolationAndNormalizationToActiveRuleCloneObject = function (oClonedActiveRule) {
    var aRuleViolation = [];
    var aNormalization = [];
    var bFlag = true;

    var aRuleEffects = RuleProps.getRuleViolationsAndNormalizationProps();

    CS.forEach(aRuleEffects, function (oEffect) {
      var oRuleViolation = oEffect.ruleViolation;
      var oNormalization = oEffect.normalization;

      if(oRuleViolation.isEnabled) {
        aRuleViolation.push({
          id: oEffect.id,
          entityId: oEffect.entityId,
          type: oEffect.type,
          color: oRuleViolation.color,
          description: oRuleViolation.description,
        });
      }

      if(oNormalization.isEnabled) {
        let oNormalizationData = {
          id: oEffect.id,
          entityId: oEffect.entityId,
          type: oEffect.type,
          values: oNormalization.values,
          valueAsHTML : oNormalization.valueAsHTML,
          tagValues: oNormalization.tagValues,
          attributeOperatorList: oNormalization.attributeOperatorList,
          calculatedAttributeUnit: oNormalization.calculatedAttributeUnit,
          calculatedAttributeUnitAsHTML: oNormalization.calculatedAttributeUnitAsHTML,
          transformationType: !CS.isEmpty(oNormalization.rules) ? oNormalization.rules.type : "",
          baseType: oNormalization.baseType,
          valueAttributeId: oNormalization.valueAttributeId,
          attributeConcatenatedList: oNormalization.attributeConcatenatedList
        };

        if (!SettingUtils.checkFormulaValidity(oNormalizationData.attributeOperatorList)) {
          alertify.error(getTranslation().ERROR_INVALID_EXPRESSION);
          bFlag = false;
          return false;
        }

        if (!CS.isEmpty(oNormalization.rules) && oNormalization.rules.type === 'replace') {
          if(CS.isEmpty(oNormalization.rules.from)){
            alertify.error(getTranslation().FIND_VALUE_SHOLD_NOT_EMPTY);
            bFlag = false;
            return false;
          }
          oNormalizationData.findText = oNormalization.rules.from;
          oNormalizationData.replaceText = oNormalization.rules.to;
        } else if (!CS.isEmpty(oNormalization.rules) && oNormalization.rules.type === 'substring') {
          if (CS.isNaN(oNormalization.rules.from) || CS.isNaN(oNormalization.rules.to)) {
            alertify.error(getTranslation().FROM_AND_TO_VALUE_IS_EMPTY);
            bFlag = false;
            return false;
          }
          else if (oNormalization.rules.from <= 0 || oNormalization.rules.to <= 0) {
            alertify.error(getTranslation().VALUE_SHOULD_NOT_BE_LESS_THEN_ONE);
            bFlag = false;
            return false;
          }
          else if (parseInt(oNormalization.rules.from) > parseInt(oNormalization.rules.to)) {
            alertify.error(getTranslation().VALIDATION_MESSAGE_FOR_SUBSTRING);
            bFlag = false;
            return false;
          }
          oNormalizationData.startIndex = oNormalization.rules.from - 1;
          oNormalizationData.endIndex = oNormalization.rules.to - 1;
        }
        aNormalization.push(oNormalizationData);
      }
    });

    oClonedActiveRule.ruleViolations = aRuleViolation;
    oClonedActiveRule.normalizations = aNormalization;
    return bFlag;
  };

  var isAttributeToAttributeMappingValidInCause = function (oCurrentRule) {
    var aRuleList = CS.filter(oCurrentRule.attributes, function (oAttr) {
      var aRule = CS.filter(oAttr.rules, {attributeLinkId: "0"});
      return aRule.length;
    });
    return CS.isEmpty(aRuleList);
  };

  var isAttributeToAttributeMappingValidInEffect = function (oCurrentRule) {
    let validMapping = true;
    CS.forEach(oCurrentRule.normalizations, function (oNormalization) {
      if(oNormalization.transformationType == "attributeValue" && oNormalization.valueAttributeId === '0') {
        validMapping = false;
        return false;
      }
    });
    return validMapping;
  };

  var _saveRule = function (oCallback = {}) {
    if(_isActiveRuleDirty()) {

      var oActiveRule = RuleProps.getActiveRule();
      var oCurrentRule = {};
      if(!CS.isEmpty(oActiveRule)) {
        oCurrentRule = oActiveRule.clonedObject ? oActiveRule.clonedObject : oActiveRule;
      }

      if(!CS.isEmpty(RuleProps.getKlassTypeId()) && CS.isEmpty(oCurrentRule.types)){
        alertify.error(getTranslation().SELECT_CLASSES_IN_CAUSE_SECTION, 0);
        return;
      }

      if(!isAttributeToAttributeMappingValidInCause(oCurrentRule)) {
        alertify.error(getTranslation().SELECT_ATTRIBUTE_TO_LINK, 0);
        return;
      }

      var oCurrentRuleToSave = CS.cloneDeep(oCurrentRule);
      if(!_assignRuleViolationAndNormalizationToActiveRuleCloneObject(oCurrentRuleToSave)){
        return;
      }

      if (!isAttributeToAttributeMappingValidInEffect(oCurrentRuleToSave)) {
        alertify.error(getTranslation().SELECT_ATTRIBUTE_TO_LINK, 0);
        return;
      }

      var oEntityValidation = _validateEntities([oCurrentRuleToSave]);
      if(!oEntityValidation.isTagValid) {
        alertify.error(getTranslation().TAG_CANNOT_BE_EMPTY);
        return;
      }

      if (!oEntityValidation.nameValidation) {
        alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
        return;
      } else if (!oEntityValidation.duplicateNameValidation) {
        logger.error('_saveRuleList: Content with the same name exists', {});
        alertify.error(getTranslation().STORE_ALERT_CONTENT_SAME_NAME, 0);
        return;
      }

      if(_checkWetherMeasurementFieldIsEmpty(oCurrentRuleToSave, SettingUtils.getAppData().getAttributeList()) == true){
        alertify.error(getTranslation().FROM_AND_TO_VALUE_IS_EMPTY);
        return;
      }

      if ( _checkWhetherUsersInRolesAreEmpty(oCurrentRuleToSave.roles)) {
        alertify.error(getTranslation().ROLE_USER_FIELD_EMPTY);
        return;
      }

      if (_checkFormulaValidityForCalculatedAttribute(oCurrentRuleToSave.normalizations)) {
        alertify.error(getTranslation().ERROR_INVALID_EXPRESSION);
        return;
      }

      if (!regexValidation(oCurrentRuleToSave.attributes)) {
        alertify.error(getTranslation().ERROR_INVALID_EXPRESSION);
        return;
      }

      //test comment
      var oRuleToSave = _getADMForActiveRule(oActiveRule, oCurrentRuleToSave);

      SettingUtils.csPostRequest(oRuleRequestMapping.SaveRule, {}, oRuleToSave, successSaveRuleCallback.bind(this, oCallback), failureSaveRuleCallback);

    } else {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
    }
  };

  var regexValidation = function (aAttributes) {
    var isValidRegex = true;
    CS.forEach(aAttributes, function (oAttribute) {
      CS.forEach(oAttribute.rules, function (oRule) {
        if(oRule.type == 'regex'){
          isValidRegex = CommonUtils.isValidRegex(oRule.values[0]);
          if(!isValidRegex){
            return false;
          }
        }
      });
      if(!isValidRegex){
        return false;
      }
    });

    return isValidRegex;
  };

  var _checkFormulaValidityForCalculatedAttribute = function (aNormalizations) {
    var bIsFormulaInvalid = false;
    CS.forEach(aNormalizations, function (oNormalization) {
      var aAttributeOperatorList = oNormalization.attributeOperatorList;
      if (CS.isEmpty(aAttributeOperatorList)) {
        return;
      }
      var oExpression = _getExpression(aAttributeOperatorList);
      if (!oExpression.validExpression) {
        bIsFormulaInvalid = true;
        return false;
      }
      var sExpression = oExpression.expression;
      try {
        var iResult = eval(sExpression);
        var bIsResultValid = !CS.isNaN(iResult) && CS.isNumber(iResult);
        if (!bIsResultValid) {
          bIsFormulaInvalid = true;
          return false;
        }

      } catch (err) {
        bIsFormulaInvalid = true;
        return false;
      }
    });
    return bIsFormulaInvalid;
  };

  var _getExpression = function (aAttributeOperatorList) {

    var sExpression = "";
    var sPrevType = "";
    var bValidExpression = true;
    CS.forEach(aAttributeOperatorList, function (oAttributeOperator, iIndex) {
      if (iIndex > 0) {
        sPrevType = aAttributeOperatorList[iIndex - 1].type;
      }
      var sNewType = oAttributeOperator.type;
      bValidExpression = _checkCalculatedExpressionManualCases(sPrevType, sNewType);
      if (!bValidExpression) {
        return false;
      }

      switch (sNewType) {
        case "ADD":
          sExpression = sExpression + "+";
          break;
        case "SUBTRACT":
          sExpression = sExpression + "-";
          break;
        case "MULTIPLY":
          sExpression = sExpression + "*";
          break;
        case "DIVIDE":
          sExpression = sExpression + "/";
          break;
        case "OPENING_BRACKET":
          sExpression = sExpression + "(";
          break;
        case "CLOSING_BRACKET":
          sExpression = sExpression + ")";
          break;
        case "ATTRIBUTE":
          if (oAttributeOperator.attributeId == null) {
            sExpression = "";
            return false;
          }
          sExpression = sExpression + "(2)";
          break;
        case "VALUE":
          if (oAttributeOperator.value == null) {
            sExpression = "";
            return false;
          }
          sExpression = sExpression + "(" + oAttributeOperator.value + ")";
          break;
      }

      sPrevType = oAttributeOperator.type

    });

    return {
      expression: sExpression,
      validExpression: bValidExpression
    }
  };

  var _checkCalculatedExpressionManualCases = function (sPrevType, sNewType) {
    var bValidExpression = true;

    var aSignOperators = ["ADD", "SUBTRACT", "MULTIPLY", "DIVIDE"];

    if (sPrevType != "") {
      if ((sPrevType == "ATTRIBUTE" && sNewType == "ATTRIBUTE") || (sPrevType == "VALUE" && sNewType == "VALUE")) {
        bValidExpression = false;
      }

      if (
          (sPrevType != "ATTRIBUTE" && sPrevType != "VALUE" && sPrevType != "OPENING_BRACKET" && sPrevType != "CLOSING_BRACKET") &&
          (sNewType != "ATTRIBUTE" && sNewType != "VALUE" && sNewType != "OPENING_BRACKET" && sNewType != "CLOSING_BRACKET")) {
        bValidExpression = false;
      }

      if (CS.includes(aSignOperators, sPrevType) && CS.includes(aSignOperators, sNewType)) {
        bValidExpression = false;
      }
    } else {
      if (CS.includes(aSignOperators, sNewType)) {
        bValidExpression = false;
      }
    }
    return bValidExpression;
  };

  var _checkWhetherUsersInRolesAreEmpty = function (oRoles) {
    var bIsRoleEmpty = false;
    CS.forEach(oRoles, function (oRole) {
      CS.forEach(oRole.rules, function (oRule) {
        if ((oRule.type == "exact" || oRule.type == "contains") && oRule.values.length == 0) {
          bIsRoleEmpty = true;
          return false;
        }
      });
      if (bIsRoleEmpty)
        return false;
    });
    return bIsRoleEmpty;
  };

  var _checkWetherMeasurementFieldIsEmpty = function(oCurrentRule, oAttributeList){
    var bIsAnyMeasurementInputEmpty = false;
    var aRuleAttributes = oCurrentRule.attributes;
    var oConfigDetails = oCurrentRule.configDetails || {};
    var iRuleAttributeLength = aRuleAttributes.length;
    var sAttributeType = "";
    var sTypeForVisual = "";
    var oLoadedAttributesData = SettingUtils.getLoadedAttributesData();
    for(var iRuleCount = 0 ; iRuleCount < iRuleAttributeLength ; iRuleCount++){
      var oRuleAttribute = aRuleAttributes[iRuleCount];
      var sEntityId = oRuleAttribute.entityId;
      sAttributeType = (oAttributeList[sEntityId] && oAttributeList[sEntityId].type) ||
          (oLoadedAttributesData[sEntityId] && oLoadedAttributesData[sEntityId].type) ||
          (oConfigDetails.referencedAttributes && oConfigDetails.referencedAttributes[sEntityId] &&
           oConfigDetails.referencedAttributes[sEntityId].type);
      sTypeForVisual = ViewUtils.getAttributeTypeForVisual(sAttributeType);
      if(sTypeForVisual == "number" || sTypeForVisual == "date" || sTypeForVisual == "measurementMetrics"){
        var aRules = aRuleAttributes[iRuleCount].rules;
        var iRuleSize = aRules.length;
        for(var iCount = 0 ; iCount < iRuleSize ; iCount++){
          var oRule = aRules[iCount];
          switch(oRule.type){
            case "exact" :
            case "contains" :
            case "lt" :
            case "gt" :
            case "start" :
            case "end" :
              if(oRule.values.length == 0 || oRule.values[0] == ""){
                if(oRule.attributeLinkId == "") {
                  bIsAnyMeasurementInputEmpty = true;
                  return bIsAnyMeasurementInputEmpty;
                }
              }
              break;
            case "range" :
              var sTo = oRule.to + "";
              var sFrom = oRule.from + "";
              if(sTypeForVisual == "date"){
                if (sTo == "0" || sFrom == "0") {
                  bIsAnyMeasurementInputEmpty = true;
                  return bIsAnyMeasurementInputEmpty;
                }
              }
              if (sTo == "" || sFrom == "") {
                bIsAnyMeasurementInputEmpty = true;
                return bIsAnyMeasurementInputEmpty;
              }
              break;
            default : break;
          }
        }
      }
    }
    return bIsAnyMeasurementInputEmpty;
  }

  let _setUpRuleConfigGridView = function () {
    //set skeleton
    let oRuleConfigGridViewSkeleton = new RuleConfigGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.RULE, {skeleton: oRuleConfigGridViewSkeleton});
    _fetchRuleListForGridView();
  };

  let _fetchRuleListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.RULE);
    SettingUtils.csPostRequest(oRuleRequestMapping.GetAll, {}, oPostData, successFetchRuleListForGridView, failureFetchRuleListForGridView);
  };

  let failureFetchRuleListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchRuleListForGridView", getTranslation());
  };

  let successFetchRuleListForGridView = function (oResponse) {
    SettingUtils.getAppData().setRuleData(oResponse.success);
    let oSuccess = oResponse.success;
    let aRuleList = oSuccess.list;
    SettingUtils.getAppData().setRuleList(aRuleList);
    RuleProps.setRuleGridData(aRuleList);
    let oLanguagesInfo = oSuccess.languagesInfo;
    RuleProps.setDataLanguages(oLanguagesInfo);
    GridViewStore.preProcessDataForGridView(GridViewContexts.RULE, aRuleList, oResponse.success.count);
    _triggerChange();
  };

  var _discardUnsavedRule = function (oCallbackData) {
    var oActiveRule = RuleProps.getActiveRule();
    _fetchRuleById(oActiveRule.id, oCallbackData);
    var bScreenLockStatus = _getRuleScreenLockStatus();
    if(!bScreenLockStatus) {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      return;
    }
    if (!CS.isEmpty(oActiveRule)) {
      if (oActiveRule.isDirty || oActiveRule.clonedObject) {
        delete oActiveRule.clonedObject;
        delete oActiveRule.isDirty;
        alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
      }
      RuleProps.setRuleScreenLockStatus(false);
      if (oCallbackData.functionToExecute) {
      }
      _triggerChange();

    } else {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
    }
  };

  var _fetchRuleList = function (bLoadMore) {
    bLoadMore = bLoadMore || false;
    let oPostData = SettingUtils.getEntityListViewLoadMorePostData(RuleProps, bLoadMore);

    SettingUtils.csPostRequest(oRuleRequestMapping.GetAll, {}, oPostData, successFetchAllRulesCallback.bind(this, bLoadMore), failureFetchAllRulesCallback);
  };

  var _cancelRuleCreation = function () {
    var oRuleValueList = RuleProps.getRuleValuesList();
    var oRuleMappingObject = SettingUtils.getAppData().getRuleList();
    var oNewRuleToCreate = RuleProps.getActiveRule();

    delete oRuleValueList[oNewRuleToCreate.id];
    delete oRuleMappingObject[oNewRuleToCreate.id];
    RuleProps.setActiveRule({});
    RuleProps.setRuleScreenLockStatus(false);
    _triggerChange();
  };

  var _createDefaultRuleObject = function () {
    let oRuleTypeDictionary = new RuleTypeDictionary();
    var oObj = {
      label: UniqueIdentifierGenerator.generateUntitledName(),
      code: "",
      isCreated: true,
      type: oRuleTypeDictionary.CLASSIFICATION_RULE,
      isLanguageDependent: false
    };

    RuleProps.setActiveRule(oObj);
    _triggerChange();
  };

  var _createNewRule = function () {
    var oRuleToCreate = RuleProps.getActiveRule();
    oRuleToCreate = oRuleToCreate.clonedObject || oRuleToCreate;

    if(CS.isEmpty(oRuleToCreate.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    var oCodeToVerifyUniqueness = {
      id: oRuleToCreate.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_RULE
    };

    var oCallbackData = {};
    oCallbackData.functionToExecute = _createRuleCall.bind(this, oRuleToCreate);
    var sURL = oRuleRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackData);
  };

  var _createRuleCall = function (oRuleToCreate) {
    SettingUtils.csPutRequest(oRuleRequestMapping.CreateRule, {}, oRuleToCreate, successCreateRuleCallback, failureCreateRuleCallback);
  };

  var _deleteRule = function (aSelectedRuleIds) {
    var oRuleMap = SettingUtils.getAppData().getRuleList();
    if(CS.isEmpty(aSelectedRuleIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return;
    }

    var isStandardRuleSelected = false;
    var aRuleNames = [];
    CS.forEach(aSelectedRuleIds, function (sRuleId) {
      var oMasterRule = oRuleMap[sRuleId];
      let sMasterRuleLabel = CS.getLabelOrCode(oMasterRule);
      aRuleNames.push(sMasterRuleLabel);

      if(oMasterRule.isStandard){
        isStandardRuleSelected = true;
        return true;
      }

    });

    if(isStandardRuleSelected){
      alertify.message(getTranslation().STANDARD_ATTRIBUTE_DELETEION);
    }
    else {
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION
          , aRuleNames,
          function () {

            var oObj = {
              ids: aSelectedRuleIds
            };

            SettingUtils.csDeleteRequest(oRuleRequestMapping.DeleteRule, {}, oObj, successDeleteRuleButtonClicked, failureDeleteRuleButtonClicked)
            .then(_fetchRuleListForGridView);

          }, function (oEvent) {
          });
    }

  };

  var _getRuleDetails = function () {

  };

  var _handleRuleRefreshMenuClicked = function () {
    var oActiveRule = RuleProps.getActiveRule();
    if(_isActiveRuleDirty()) {
      var oCallbackData = {};
      oCallbackData.functionToExecute = _fetchRuleById.bind(this, oActiveRule.id);

      CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveRule.bind(this, oCallbackData),
          _discardUnsavedRule,
          function () {
          }
      );
    } else {
      if(CS.isEmpty(oActiveRule)) {
        _fetchRuleList();
      } else {
        _fetchRuleById(oActiveRule.id);
      }
    }
  };

  var _fetchRuleById = function (sRuleId, oCallbackData) {
    var oCallback = {};
    if(oCallbackData) {
      oCallback.functionToExecute = oCallbackData.functionToExecute
    }
    SettingUtils.csGetRequest(oRuleRequestMapping.GetRule, {id: sRuleId}, successFetchRuleByIdCallback.bind(this, oCallback), failureFetchRuleByIdCallback.bind(this,sRuleId));

  };

  var _handleRuleListNodeClicked = function (sRuleId) {
    RuleProps.setKlassTypeId('');
    if(_isActiveRuleDirty()) {
      var oCallbackData = {};
      oCallbackData.functionToExecute = _fetchRuleById.bind(this, sRuleId);
      _saveRule(oCallbackData);
    } else {
      _fetchRuleById(sRuleId);
    }
  };

  var _handleRuleNameChanged = function (sValue) {
    var oAppData = SettingUtils.getAppData();
    var oRuleMap = oAppData.getRuleList();
    var oActiveRule = _makeActiveRuleDirty();
    oActiveRule.label = sValue;
    oRuleMap[oActiveRule.id].label = sValue;
  };

  var _getAttributeObjectToPushIntoFilterProps = function (sAttributeId) {
    var oAttributeMap = SettingUtils.getAppData().getAttributeList();
    var oMasterAttribute = oAttributeMap[sAttributeId];

    let oComponentProps = SettingUtils.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oReferencedAttributes = oScreenProps.getReferencedAttributes();
    oReferencedAttributes[oMasterAttribute.id] = oMasterAttribute;

    var sDefaultUnit = oMasterAttribute.defaultUnit || "";
    var defaultValue = [];
    var sType = oMasterAttribute.type;
    var sVisualType = SettingUtils.getAttributeTypeForVisual(sType, sAttributeId);
    if(sVisualType == "number" || sVisualType == "measurementMetrics"){
      defaultValue = ["0"];
    }

    var sAttributeRuleType = "contains";

    if(sVisualType == "measurementMetrics" || sVisualType == "calculated" ){
      sAttributeRuleType = "exact";
    }

    if(sVisualType == "date"){
      sAttributeRuleType = "empty";
    }

    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: sAttributeRuleType,
      values: defaultValue,
      to: 0,
      from: 0,
      ruleListLinkId: "",
      attributeLinkId: null
    }
  };

  var _getRelationshipObjectToPushIntoFilterProps = function () {
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: "max",
      values: [],
      to: 0,
      from: 0,
    }
  };

  var _getRoleObjectToPushIntoFilterProps = function () {

    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: "exact",
      values : [],
      to: 0,
      from: 0,
      ruleListLinkId: ""
    }
  };

  var _getTypeObjectToPushIntoFilterProps = function () {

    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: "is",
      values : [],
      to: 0,
      from: 0,
    }
  };

  var _getTagObjectToPushIntoFilterProps = function (sAttributeId,sType, oTagRuleValue) {

    var aTagValues = [];

    var oCurrentTag = SettingUtils.getLoadedTagsData()[sAttributeId] || {};

    if (sType != "notempty" && sType != "empty") {
      CS.forEach(oCurrentTag.children, function (oTagValues) {
        var oTempTagValueObject = {};
        oTempTagValueObject.id = oTagValues.id;
        oTempTagValueObject.innerTagId = oTagValues.id;
        oTempTagValueObject.from = 0;
        oTempTagValueObject.to = 0;
        aTagValues.push(oTempTagValueObject);
      });
    }

    if(CS.isEmpty(oTagRuleValue)){
      oTagRuleValue = {};
      oTagRuleValue.id = UniqueIdentifierGenerator.generateUUID();
    }

    return {
      id: oTagRuleValue.id,
      type: sType,
      tagValues : aTagValues
    }
  };

  var _getRuleCauses = function (aDataList, sActiveRuleId, sEntityId) {
    var oRuleAttribute = CS.find(aDataList, {entityId: sEntityId});
    if(CS.isEmpty(oRuleAttribute)) {
      oRuleAttribute = {
        id: UniqueIdentifierGenerator.generateUUID(),
        entityId: sEntityId,
        rules: []
      };
      aDataList.push(oRuleAttribute);
    }
    return oRuleAttribute;
  };

  var _getDummyEffectObject = function (sEntityId, sContext) {
    let oRuleTypeDictionary = new RuleTypeDictionary();
    var aTagValues = [];
    var oActiveRule = RuleProps.getActiveRule();
    let sRuleType = oActiveRule.type;
    let sNormalizationType = 'value';
    if(sContext === 'tag') {
      sNormalizationType = 'replacewith';
      var oConfigDetails = oActiveRule.configDetails;
      var oReferencedTags = oConfigDetails && oConfigDetails.referencedTags || {};
      var oLoadedTags = SettingUtils.getLoadedTagsData();
      var oMasterTag = oReferencedTags[sEntityId] || oLoadedTags[sEntityId];
      CS.forEach(oMasterTag.children, function (oChild) {
          var oTagValues = {};
          oTagValues.id = oChild.id;
          oTagValues.innerTagId = oChild.id;
          oTagValues.to = 0;
          oTagValues.from = 0;
          aTagValues.push(oTagValues);
      });
    }
    let oNormalizationRule = {
      type: sNormalizationType,
      from: '',
      to: '',
    };
    let oObjectToReturn = {
      id: UniqueIdentifierGenerator.generateUUID(),
      entityId: sEntityId,
      type: sContext,
      ruleViolation: {
        description: "",
        color: ColorConstantDictionary.RED,
      },
      normalization: {
        values: [],
        tagValues: aTagValues,
        attributeOperatorList: [],
        calculatedAttributeUnit: "",
        calculatedAttributeUnitAsHTML: "",
        baseType:oNormalizationTypeDictionary['normalization'],
        rules: oNormalizationRule
      }
    };
    if (sRuleType === oRuleTypeDictionary.VIOLATION_RULE) {
      oObjectToReturn["ruleViolation"].isEnabled = true;
    } else {
      oObjectToReturn["normalization"].isEnabled = true;
    }
    return oObjectToReturn;
  };

  var _addCauseEffects = function (aDataList, oActiveRule, sEntityId, sContext) {
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {entityId: sEntityId});
    if(CS.isEmpty(oEffect)) {
      var oDummyObject = _getDummyEffectObject(sEntityId, sContext);
      aRuleEffect.push(oDummyObject);
    }
  };

  var _handleAttributeAdded = function (sAttributeId, sAttributeContext) {
    var oActiveRule = _makeActiveRuleDirty();
    var aAttributes = [];
    if(sAttributeContext == "CAUSE") {
      aAttributes = oActiveRule.attributes;
      var oRuleAttribute = _getRuleCauses(aAttributes, oActiveRule.id, sAttributeId);
      var oAttributeRule = _getAttributeObjectToPushIntoFilterProps(sAttributeId);
      oRuleAttribute.rules.push(oAttributeRule);
    } else {
      _addCauseEffects(aAttributes, oActiveRule, sAttributeId, 'attribute');
    }
  };

  var _handleRelationshipAdded = function (sAttributeId, sAttributeContext) {
    var oActiveRule = _makeActiveRuleDirty();
    var aAttributes = [];
    if(sAttributeContext == "CAUSE") {
      aAttributes = oActiveRule.relationships;
      var oRuleAttribute = _getRuleCauses(aAttributes, oActiveRule.id, sAttributeId);
      var oAttributeRule = _getRelationshipObjectToPushIntoFilterProps();
      oRuleAttribute.rules.push(oAttributeRule);
    } else {
      aAttributes = oActiveRule.ruleViolations;
      _addCauseEffects(aAttributes, oActiveRule.id, sAttributeId, 'relationship');
    }
  };

  var _handleRoleAdded = function (sAttributeId, sAttributeContext) {
    var oActiveRule = _makeActiveRuleDirty();
    var aAttributes = [];
    if(sAttributeContext == "CAUSE") {
      aAttributes = oActiveRule.roles;
      var oRuleAttribute = _getRuleCauses(aAttributes, oActiveRule.id, sAttributeId);
      var oAttributeRule = _getRoleObjectToPushIntoFilterProps();
      oRuleAttribute.rules.push(oAttributeRule);
    } else {
      _addCauseEffects(aAttributes, oActiveRule, sAttributeId, 'role');
    }
  };

  var _handleTypeAdded = function (sAttributeId, sAttributeContext) {
    var oActiveRule = _makeActiveRuleDirty();
    var aAttributes = [];
    if(sAttributeContext == "CAUSE") {
      aAttributes = oActiveRule.types;
      var oRuleAttribute = _getRuleCauses(aAttributes, oActiveRule.id, sAttributeId);
      var oAttributeRule = _getTypeObjectToPushIntoFilterProps();
      oRuleAttribute.rules.push(oAttributeRule);
    } else {
      aAttributes = oActiveRule.ruleViolations;
      _addCauseEffects(aAttributes, oActiveRule.id, sAttributeId, 'type');
    }
  };

  var _handleTagAdded = function (sTagId, sTagContext) {
    var oActiveRule = _makeActiveRuleDirty();
    var aTags = [];
    if(sTagContext == "CAUSE") {
      aTags = oActiveRule.tags;
      var oRuleTag = _getRuleCauses(aTags, oActiveRule.id, sTagId);
      var oAttributeRule = _getTagObjectToPushIntoFilterProps(sTagId, "exact");
      oRuleTag.rules.push(oAttributeRule);
    } else {
      aTags = oActiveRule.ruleViolations;
      _addCauseEffects(aTags, oActiveRule, sTagId, 'tag');
    }
  };

  var _handleRuleElementDeleteButtonClicked = function (sElementId, sContext, sHandlerContext) {
    var oActiveRule = _makeActiveRuleDirty();

    if(sHandlerContext == "EFFECT") {
      var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
      CS.remove(aRuleEffect, {id: sElementId});
      CS.remove(oActiveRule.ruleViolations, {id: sElementId});
      CS.remove(oActiveRule.normalizations, {id: sElementId});
    } else {
      if(sContext == "attribute") {
        CS.remove(oActiveRule.attributes, {entityId: sElementId});
      }
      else if(sContext == "tag") {
        CS.remove(oActiveRule.tags, {entityId: sElementId});
      }else if(sContext == "relationship") {
        CS.remove(oActiveRule.relationships, {entityId: sElementId});
      }else if(sContext == "role") {
        CS.remove(oActiveRule.roles, {entityId: sElementId});
      }else if(sContext == "type") {
        CS.remove(oActiveRule.types, {entityId: sElementId});
      }
    }
  };

  var _handleRuleAttributeValueChanged = function (sElementId, sValueId, aValueList, sContext) {
    var oActiveRule = _makeActiveRuleDirty();
    if (sContext == "attribute") {
      var oAttribute = CS.find(oActiveRule.attributes, {entityId: sElementId});
      var oAttributeRuleValue = CS.find(oAttribute.rules, {id: sValueId});
      oAttributeRuleValue.values = aValueList;

    } else if (sContext == "relationship") {
      var oRelationship = CS.find(oActiveRule.relationships, {entityId: sElementId});
      var oRelationshipRuleValue = CS.find(oRelationship.rules, {id: sValueId});
      oRelationshipRuleValue.values = aValueList;
    } else if (sContext == "role") {
      var oRole = CS.find(oActiveRule.roles, {entityId: sElementId});
      var oRoleRuleValue = CS.find(oRole.rules, {id: sValueId});
      oRoleRuleValue.values = aValueList;
    }

  };

  var _handleSelectionToggleButtonClicked = function (sKey, sId) {
    let oActiveRule = _makeActiveRuleDirty();
    let aPhysicalCatalogs = oActiveRule.physicalCatalogIds || [];
    let aRemovedCatalogIds = CS.remove(aPhysicalCatalogs, function (sCatalogId) {
      return sCatalogId == sId
    });
    if (CS.isEmpty(aRemovedCatalogIds)) {
      aPhysicalCatalogs.push(sId);
    }
    oActiveRule.physicalCatalogIds = aPhysicalCatalogs;
  };

  var _handleRuleAttributeValueChangedForNormalization = function (sRoleId, aUsers) {
    _makeActiveRuleDirty();
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {entityId: sRoleId});
    var oNormalization = oEffect.normalization;
    oNormalization.values = aUsers;
  };

  var _handleRuleAttributeColorValueChanged = function (sElementId, sValueId, sColor) {
    _makeActiveRuleDirty();
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {entityId: sElementId});
    var oRuleViolations = oEffect.ruleViolation;
    oRuleViolations.color = sColor;
  };

  var _handleRuleAttributeDescriptionValueChanged = function (sElementId, sValueId, sDescription) {
    _makeActiveRuleDirty();
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {entityId: sElementId});
    var oRuleViolations = oEffect.ruleViolation;
    oRuleViolations.description = sDescription;
  };

  var _handleRuleAttributeDescriptionValueChangedInNormalization = function (sAttrId, oAttrVal, sContext, sValue, sValAsHtml) {
    _makeActiveRuleDirty();
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {entityId: sAttrId});
    var oNormalization = oEffect.normalization;
    oNormalization.values[0] = sValue;
    oNormalization.valueAsHTML = sValAsHtml;
  };

  var _handleRuleAttributeValueChangedInNormalization = function (sAttrId, oAttrVal, sContext, sValue) {
    _makeActiveRuleDirty();
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {entityId: sAttrId});
    var oNormalization = oEffect.normalization;
    oNormalization.values[0] = sValue;
  };

  var _handleRuleAddAttributeClicked = function (sElementId,sContext) {
    var oActiveRule = _makeActiveRuleDirty();
    if(sContext == "attribute"){
    var oAttribute = CS.find(oActiveRule.attributes, {entityId: sElementId});
    var oAttributeRule = _getAttributeObjectToPushIntoFilterProps(sElementId);
    oAttribute.rules.push(oAttributeRule);

    }else if(sContext == "relationship"){
      var oRelationship = CS.find(oActiveRule.relationships, {entityId: sElementId});

      var oRelationshipRule = _getRelationshipObjectToPushIntoFilterProps();
      oRelationship.rules.push(oRelationshipRule);
    }else if(sContext == "role"){
      var oRole = CS.find(oActiveRule.roles, {entityId: sElementId});

      var oRoleRule = _getRoleObjectToPushIntoFilterProps();
      oRole.rules.push(oRoleRule);
    }else if(sContext == "tag"){
      var oTag = CS.find(oActiveRule.tags, {entityId: sElementId});

      var oTagRule = _getTagObjectToPushIntoFilterProps(sElementId,"exact");
      oTag.rules.push(oTagRule);
    }
  };

  let _updateNormalizationData = function (oEffect, sTypeId) {
    let oEffectNormalization = oEffect.normalization;

    switch (sTypeId) {
      case "attributeValue":
        /** If transformation type is attribute value & attribute for mapping is not selected then set it to "0" **/
        oEffectNormalization.valueAttributeId = "0";

        oEffectNormalization.attributeConcatenatedList = [];
        oEffectNormalization.baseType = oNormalizationTypeDictionary['attributeValueNormalization'];
        break;

      case "value":
        oEffectNormalization.attributeConcatenatedList = [];

        /** If transformation type is other than attribute value then it should be set to null **/
        oEffectNormalization.valueAttributeId = null;
        break;

      case "concat":
        oEffectNormalization.valueAttributeId = null;
        oEffectNormalization.baseType = oNormalizationTypeDictionary['concatenatedNormalization'];
        break;
    }

    oEffectNormalization.transformationType = sTypeId;
  };

  var _handleRuleAttributeValueTypeChanged = function (sAttributeId, sValueId, sTypeId,sContext, isCause) {
    var oActiveRule = _makeActiveRuleDirty();
    var bRemoveAttributeLink = (sTypeId == "empty" || sTypeId == "notempty" || sTypeId == "range"
                                || sTypeId == "length_range");
    if(isCause) {
      if (sContext == "attribute") {
        var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttributeId});
        var oAttributeRuleValue = CS.find(oAttribute.rules, {id: sValueId});
        oAttributeRuleValue.type = sTypeId;
        oAttributeRuleValue.ruleListLinkId = "";
        oAttributeRuleValue.klassLinkIds = [];
        oAttributeRuleValue.values = [];

        oAttributeRuleValue.from = "";
        oAttributeRuleValue.to = "";

        bRemoveAttributeLink && (oAttributeRuleValue.attributeLinkId = "");
        bRemoveAttributeLink && (oAttributeRuleValue.shouldCompareWithSystemDate = false);

        var oAttributeMap = SettingUtils.getAppData().getAttributeList();
        var oMasterAttribute = oAttributeMap[sAttributeId];
        if (CS.isEmpty(oMasterAttribute)) {
          let oComponentProps = SettingUtils.getComponentProps();
          let oScreenProps = oComponentProps.screen;
          let oReferencedAttributes = oScreenProps.getReferencedAttributes();
          oMasterAttribute = oReferencedAttributes[sAttributeId];
        }
        var defaultValue = "";
        var sType = oMasterAttribute.type;
        var sVisualType = SettingUtils.getAttributeTypeForVisual(sType, sAttributeId);
        if (sVisualType == "number" || sVisualType == "measurementMetrics") {
          defaultValue = "0";
        }
        oAttributeRuleValue.values = oAttributeRuleValue.values || defaultValue;
      }
      else if (sContext == "relationship") {
        var oRelationship = CS.find(oActiveRule.relationships, {entityId: sAttributeId});
        var oRelationshipRuleValue = CS.find(oRelationship.rules, {id: sValueId});
        oRelationshipRuleValue.type = sTypeId;
        oRelationshipRuleValue.values = ["0"];
      }
      else if (sContext == "role") {
        var oRole = CS.find(oActiveRule.roles, {entityId: sAttributeId});
        var oRoleRuleValue = CS.find(oRole.rules, {id: sValueId});
        oRoleRuleValue.type = sTypeId;
        oRoleRuleValue.values = [];
        bRemoveAttributeLink && (oRoleRuleValue.attributeLinkId = "");
      }
      else if (sContext == "type") {
        var oType = CS.find(oActiveRule.types, {entityId: sAttributeId});
        var oTypeRuleValue = CS.find(oType.rules, {id: sValueId});
        oTypeRuleValue.type = sTypeId;
      }
      else if (sContext == "tag") {
        var oTag = CS.find(oActiveRule.tags, {entityId: sAttributeId});
        var iIndexOfTagRule = CS.findIndex(oTag.rules, {id: sValueId});
        var oTagRuleValue = oTag.rules[iIndexOfTagRule];

        var oNewTagRuleValue = _getTagObjectToPushIntoFilterProps(sAttributeId, sTypeId, oTagRuleValue);
        oTag.rules.splice(iIndexOfTagRule, 1, oNewTagRuleValue);
      }
    }
    else {
      var oOriginalNormalization = CS.find(oActiveRule.normalizations, {id: sValueId});
      var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
      var oEffect = CS.find(aRuleEffect, {id: sValueId});
      var sFrom = '';
      var sTo = '';

      if (sContext === 'tag') {
        let oldTagVlaues = oOriginalNormalization ? oOriginalNormalization.tagValues : oEffect.normalization.tagValues;
        oEffect.normalization.tagValues = _getTagObjectToPushIntoFilterProps(sAttributeId, sTypeId, {tagValues: CS.cloneDeep(oldTagVlaues)}).tagValues;
      } else {
        if (sTypeId == 'substring') {
          oEffect.normalization.baseType = oNormalizationTypeDictionary['subStringNormalization'];
          sFrom = NaN;
          sTo = NaN;
        } else if (sTypeId == 'replace') {
          oEffect.normalization.baseType = oNormalizationTypeDictionary['findReplaceNormalization'];
        } else {
          oEffect.normalization.baseType = oNormalizationTypeDictionary['normalization'];
        }

        if (oOriginalNormalization) {
          if (oOriginalNormalization.transformationType == 'substring' && sTypeId == 'substring') {
            sFrom = oOriginalNormalization.startIndex + 1;
            sTo = oOriginalNormalization.endIndex + 1;
          }
          else if (oOriginalNormalization.transformationType == 'replace' && sTypeId == 'replace') {
            sFrom = oOriginalNormalization.findText;
            sTo = oOriginalNormalization.replaceText;
          }
        }

        if (sTypeId === "attributeValue" || sTypeId === "value" || sTypeId === "concat") {
          _updateNormalizationData(oEffect, sTypeId);
        }
      }
      oEffect.normalization.transformationType = sTypeId;
      /* On transformation changed previous selected value should get clear */
      oEffect.normalization.values = [];
      oEffect.normalization.valueAsHTML = "";
      oEffect.normalization.rules = {
        type: sTypeId,
        from: sFrom,
        to: sTo
      };
    }
  };

  var _handleRuleAttributeValueDeleteClicked = function (sAttributeId, sValueId,sContext) {
    var oActiveRule = _makeActiveRuleDirty();
    if(sContext == "attribute"){
    var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttributeId});
    CS.remove(oAttribute.rules, {id: sValueId});
    }else if(sContext == "relationship"){
      var oRelationship = CS.find(oActiveRule.relationships, {entityId: sAttributeId});
      CS.remove(oRelationship.rules, {id: sValueId});
    }else if(sContext == "role"){
      var oRole = CS.find(oActiveRule.roles, {entityId: sAttributeId});
      CS.remove(oRole.rules, {id: sValueId});
    }else if(sContext == "type"){
      var oType = CS.find(oActiveRule.types, {entityId: sAttributeId});
      CS.remove(oType.rules, {id: sValueId});
    }else if(sContext == "tag"){
      var oTag = CS.find(oActiveRule.tags, {entityId: sAttributeId});
      CS.remove(oTag.rules, {id: sValueId});
    }
  };

  var _handleRuleAttributeRangeValueChanged = function (sAttributeId, sValueId, sValue, sRange) {
    var oActiveRule = _makeActiveRuleDirty();
    var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttributeId});
    var oRuleValue = CS.find(oAttribute.rules, {id: sValueId});

    if(sRange === 'from' && CS.toNumber(sValue) > CS.toNumber(oRuleValue.to)) {
      oRuleValue.to = sValue;
    } else if(sRange === 'to' && CS.toNumber(sValue) < CS.toNumber(oRuleValue.from)) {
      oRuleValue.from = sValue;
    }

    oRuleValue[sRange] = sValue;
  };

  var _handleAttributeValueChangedForRangeInNormalization = function (sAttributeId, sValueId, sValue, sType, sRange) {
    _makeActiveRuleDirty();
    if(sType == 'substring'){
      sValue =CS.parseInt(sValue);
    }
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {id: sValueId});
    oEffect.normalization.rules[sRange] = sValue;
    _triggerChange();
  };

  let _handleRuleDetailMSSValueChanged = (sKey, aSelectedItems) => {
    let oActiveRule = _makeActiveRuleDirty();
    oActiveRule[sKey] = aSelectedItems;
  };

  let _handleRuleDetailPartnerApplyClicked = (aSelectedItems)=> {
    _handleRuleDetailMSSValueChanged("organizations", aSelectedItems);
  };

  let _handleRuleDetailEndpointsChanged = (aSelectedItems) => {
    _handleRuleDetailMSSValueChanged("endpoints", aSelectedItems);
  };

 var _handleTagNodeClicked = function (oModel) {
  };

  var _handleRuleFilterInnerTagAdded = function (sTagGroupId, aTagValueRelevanceData, sRuleId) {
    let oActiveRule = _makeActiveRuleDirty();
    let oTag = CS.find(oActiveRule.tags, {entityId: sTagGroupId});
    let oTagRuleValue = CS.find(oTag.rules, {id: sRuleId});

    CS.forEach(oTagRuleValue.tagValues, function (oTagRuleTagValue) {
      let oData = CS.find(aTagValueRelevanceData, {tagId: oTagRuleTagValue.id});
      SettingUtils.updateTagValueRelevanceOrRange(oTagRuleTagValue, oData, true);
    });
  };

  var _handleRuleFilterInnerTagForNormalizationAdded = function (sTagGroupId, aTagValueRelevanceData) {
    _makeActiveRuleDirty();
    let aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    let oEffect = CS.find(aRuleEffect, {entityId: sTagGroupId});
    let oNormalization = oEffect.normalization;

    CS.forEach(oNormalization.tagValues, function (oTagValue) {
      let oData = CS.find(aTagValueRelevanceData, {tagId: oTagValue.id});
      SettingUtils.updateTagValueRelevanceOrRange(oTagValue, oData, true);
    });
  };

  var _handleYesNoViewToggled = function (sSectionId, sElementId, bValue) {
    var oActiveRule = _makeActiveRuleDirty();
    var oTag = CS.find(oActiveRule.tags, {entityId: sSectionId});
    var oTagRuleValue = CS.find(oTag.rules, {id: sElementId});
    oTagRuleValue.tagValues[0].from = bValue ? 100: -100;
    oTagRuleValue.tagValues[0].to = bValue ? 100: -100;
    _triggerChange();
  };

  var _handleYesNoViewToggledForNormalization = function (sSectionId, bValue) {
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {entityId: sSectionId});
    var oNormalization = oEffect.normalization;
    oNormalization.tagValues[0].from = bValue ? 100: -100;
    oNormalization.tagValues[0].to = bValue ? 100: -100;
    _triggerChange();
  };

  var _handleContentFilterInnerTagAdded = function (sTagGroupId, aTagItemList, sRuleId, iRelevance, sViewContext, bIsDeleted) {
    var oActiveRule = _makeActiveRuleDirty();
    var aTagList = SettingUtils.getTagList();
    var oMasterTag = CS.find(aTagList, {id: sTagGroupId});
    CS.forEach(aTagItemList, function (sTagId) {
      if (sViewContext == "ruleFilterTagsTagValuesForNormalization") {
        var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
        var oEffect = CS.find(aRuleEffect, {entityId: sTagGroupId});
        var oNormalization = oEffect.normalization;
        if (!oNormalization['tagValues']) {
          oNormalization['tagValues'] = [];
        }
        if (!bIsDeleted) {
          var oTagValue = CS.find(oNormalization.tagValues, {id: sTagId});
          oTagValue.to = iRelevance;
          oTagValue.from = iRelevance;
          if (!CS.isEmpty(oMasterTag) && oMasterTag.isMultiselect) {
            oTagValue.to = iRelevance;
            oTagValue.from = iRelevance;
          } else {
            CS.forEach(oNormalization.tagValues, function (oTagValues) {
              oTagValues.to = 0;
              oTagValues.from = 0;
            });
            oTagValue.to = iRelevance;
            oTagValue.from = iRelevance;
          }
        } else {
          var oSelectedTagValue = CS.find(oNormalization.tagValues, {id: sTagId});
          oSelectedTagValue.to = 0;
          oSelectedTagValue.from = 0;
        }
      } else {
        var oTag = CS.find(oActiveRule.tags, {entityId: sTagGroupId});
        var oTagRuleValue = CS.find(oTag.rules, {id: sRuleId});
        var oTagElement = CS.find(oTagRuleValue.tagValues, {id: sTagId});
        if (!CS.isEmpty(oMasterTag) && oMasterTag.isMultiselect) {
          oTagElement.from = iRelevance;
          oTagElement.to = iRelevance;
        } else {
          CS.forEach(oTagRuleValue.tagValues, function (oTagValue) {
            oTagValue.from = 0;
            oTagValue.to = 0;
          });
          oTagElement.from = iRelevance;
          oTagElement.to = iRelevance;
        }
      }
    });
  };

  var _handleAddNewBlackListItem = function (sValue) {
    var oActiveRule = _makeActiveRuleDirty();
    var aBlackList = oActiveRule.blackList || [];
    aBlackList.push(sValue);
    oActiveRule.blackList = aBlackList;
  };

  var _handleRemoveBlackListItem = function (iIndex) {
    var oActiveRule = _makeActiveRuleDirty();
    var aBlackList = oActiveRule.blackList || [];
    aBlackList.splice(iIndex, 1);
  };

  var _handleEditBlackListItem = function (iIndex, sValue) {
    var oActiveRule = _makeActiveRuleDirty();
    var aBlackList = oActiveRule.blackList;
    aBlackList[iIndex] = sValue;
  };

  var _handleRuleRightPanelBarItemClicked = function(sIconContext){
    switch (sIconContext){
      case "ruleList":
        var oBarIconClickMap = RuleProps.getRightBarIconClickMap();
        RuleProps.setRightPanelVisibility(!oBarIconClickMap[sIconContext]);
        oBarIconClickMap[sIconContext] = !oBarIconClickMap[sIconContext];
        break;
    }
  };

  var _handleRuleListDropOnInput = function(oDropModel){
    var sSplitter = SettingUtils.getSplitter();
    var sDropModelId = oDropModel.id;
    var oDraggedItem = oDropModel.properties["draggedItem"];
    var sDraggedId = oDraggedItem.id;
    var aIds = sDropModelId.split(sSplitter);

    var oActiveRule = _makeActiveRuleDirty();
    var aOuterEntity = oActiveRule[aIds[0]];

    var oEntity = CS.find(aOuterEntity, {entityId:aIds[1]});
    if(!CS.isEmpty(oEntity)){
      var aRules = oEntity.rules;
      var oRule = CS.find(aRules, {id: aIds[2]});
      oRule.ruleListLinkId = sDraggedId;
      oRule.values = [];
    }
    _saveRule();

  };

  var _handleRuleRemoveBlackListIconClicked = function (sDropId) {
    var sSplitter = SettingUtils.getSplitter();
    var aIds = sDropId.split(sSplitter);

    var oActiveRule = _makeActiveRuleDirty();
    var aOuterEntity = oActiveRule[aIds[0]];

    var oEntity = CS.find(aOuterEntity, {entityId:aIds[1]});
    if(!CS.isEmpty(oEntity)){
      var aRules = oEntity.rules;
      var oRule = CS.find(aRules, {id: aIds[2]});
      oRule.ruleListLinkId = "";
      oRule.values = [];
    }
  };

  var _handleAttributeVisibilityButtonClicked = function (sAttrId, oAttrRuleCondition) {
    var oActiveRule = _makeActiveRuleDirty();
      var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttrId});
      var oRule = CS.find(oAttribute.rules, {id: oAttrRuleCondition.id});
    if(!CS.isEmpty(oRule)) {
      if(CS.isEmpty(oRule.attributeLinkId)) {
        oRule.attributeLinkId = "0";
      } else {
        oRule.attributeLinkId = null;
      }
      oRule.values = [];
      oRule.ruleListLinkId = "";
      oRule.shouldCompareWithSystemDate = false;
    }
    _triggerChange();
  };

  var _handleCompareWithSystemDateButtonClicked = function (sAttrId, oAttrRuleCondition) {
    var oActiveRule = _makeActiveRuleDirty();
    var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttrId});
    var oRule = CS.find(oAttribute.rules, {id: oAttrRuleCondition.id});
    if (!CS.isEmpty(oRule)) {
      !CS.isEmpty(oRule.attributeLinkId) && (oRule.attributeLinkId = null);
      oRule.shouldCompareWithSystemDate = !oRule.shouldCompareWithSystemDate;
    }
    _triggerChange();
  };

  var _handleAttributeViewTypeChanged = function (sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId){
    var oActiveRule = _makeActiveRuleDirty();
    var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttrId});
    var oAttributeRule = CS.find(oAttribute.rules, {id: oAttributeCondition.id});
    oAttributeRule.attributeLinkId = sSelectedAttrId;
    oAttributeRule.values = [];
    _triggerChange();
  };

  let _handleAttributeLinkedInRuleEffectSection = function (sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId) {
    _makeActiveRuleDirty();
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {entityId: sAttrId });
    var oNormalization = oEffect.normalization;
    oNormalization.valueAttributeId = sSelectedAttrId;
    oNormalization.values = [];
    _triggerChange();
  };

  let _handleConcatenatedFormulaChanged = function (oNormalization, aAttributeConcatenatedList, oReferencedData) {
    let oActiveRule = _makeActiveRuleDirty();
    oNormalization.attributeConcatenatedList = aAttributeConcatenatedList;
    if(!CS.isEmpty(oReferencedData)) {
      var oConfigDetails = oActiveRule.configDetails;
      CS.assign(oConfigDetails.referencedTags, oReferencedData.referencedTags);
      CS.assign(oConfigDetails.referencedAttributes, oReferencedData.referencedAttributes);
    }
    _triggerChange();
  };

  var _validateEntity = function (oEntity) {
    trackMe('_validateEntity');
    var oAppData = SettingUtils.getAppData();
    var oMapRuleList = oAppData.getRuleList();
    var aEntityList = [];
    CS.forEach(oMapRuleList, function (oMasterRule) {
      aEntityList.push(oMasterRule);
    });

    var sEntityName = oEntity.label;

    var bNameValidation = sEntityName.trim() != "";
    var bDuplicateNameValidation = true;

    return {
      nameValidation: bNameValidation,
      duplicateNameValidation: bDuplicateNameValidation
    };
  };

  var _validateTagsInCause = function (aTags) {
    var bFlag = true;
    var oActiveRule = RuleProps.getActiveRule();
    var oConfigDetails = oActiveRule.configDetails || {};
    var oReferencedTags = oConfigDetails.referencedTags || {};
    var oLoadedTags = SettingUtils.getLoadedTagsData();
    CS.forEach(aTags, function (oTag) {
      var oMasterTag = oReferencedTags[oTag.entityId] || oLoadedTags[oTag.entityId];//SettingUtils.getTagById(oTag.entityId);
      var sTagType = oMasterTag.tagType;
      if(sTagType == TagTypeConstants.YES_NEUTRAL_TAG_TYPE || sTagType == TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE || sTagType == TagTypeConstants.STATUS_TAG_TYPE) {
        var aRules = oTag.rules;
        CS.forEach(aRules, function (oRule) {
          var sType = oRule.type;
          var aTagValues = oRule.tagValues;
          var aTypeToExclude = [RuleConstantDictionary.TAG.TYPE.EMPTY, RuleConstantDictionary.TAG.TYPE.NOTEMPTY];
          if(!CS.includes(aTypeToExclude,sType)) {
            bFlag = false;
            CS.forEach(aTagValues, function (oTagValue) {
              if((oTagValue.from == oTagValue.to) && oTagValue.from != 0) {
                bFlag = true;
              }
            });
          }
          if(!bFlag) {
            return false;
          }
        });
      }
      if(!bFlag) {
        return false;
      }
    });
    return bFlag;
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
      oRes.isTagValid = _validateTagsInCause(oEntity.tags);
    });

    return oRes;
  };

  var _setSelectedRule = function (oRule) {
    RuleProps.setActiveRule(oRule);
  };

  let _handleRuleDetailsKlassMSSChanged = function (sContext, aSelectedKlasses) {
    let oActiveRule = _makeActiveRuleDirty();
    var oEntityValidation = _validateEntities([oActiveRule]);
    if (!oEntityValidation.isTagValid) {
      alertify.error(getTranslation().TAG_CANNOT_BE_EMPTY);
      return;
    }

    let aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    let oEffect = CS.find(aRuleEffect, {type: 'type'});

    if(sContext === 'CAUSE') {
      oActiveRule.types = aSelectedKlasses;
      if(CS.isEmpty(aSelectedKlasses)){
        /** On Remove of class from cause, remove klass from effect side and save rule also **/
        RuleProps.setKlassTypeId('');
        if (!CS.isEmpty(oEffect)) {
          oEffect.normalization.values = [];
        }
      }
    }
    else if(sContext == 'EFFECT'){

      /** If class is not selected in cause side then don't allow to add class in effect side**/
      if (!CS.isEmpty(RuleProps.getKlassTypeId()) && CS.isEmpty(oActiveRule.types)) {
        alertify.error(getTranslation().SELECT_CLASSES_IN_CAUSE_SECTION, 0);
        return;
      }

      /** after adding class update normalization props **/
      if(CS.isEmpty(oEffect)){
        oEffect = {
          id:UniqueIdentifierGenerator.generateUUID(),
          type: 'type',
          ruleViolation:{
            color: "red",
            description: "",
            isEnabled: false
          },
          normalization: {
            attributeOperatorList: [],
            calculatedAttributeUnit: "",
            calculatedAttributeUnitAsHTML: "",
            isEnabled: true,
            tagValues: [],
            values: aSelectedKlasses,
            rules: {
              type: '',
              from: "",
              to: ""
            },
            baseType: oNormalizationTypeDictionary['normalization']
          }
        };
        oActiveRule.normalizations.push(oEffect);
        aRuleEffect.push(oEffect);
      }else {
        oEffect.normalization.values = aSelectedKlasses;
      }
    }

    /** update active rule object using normalization props **/
    _assignRuleViolationAndNormalizationToActiveRuleCloneObject(oActiveRule);

    _triggerChange();
  };

  let _handleRuleDetailsKlassTypeMSSChanged = function (aNewKlassType) {
    let oActiveRule = _makeActiveRuleDirty();
    RuleProps.setKlassTypeId(aNewKlassType[0]);
    oActiveRule.types = [];
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {type: 'type'});
    if(oEffect && !CS.isEmpty(oEffect.normalization.values)){
      oEffect.normalization.values = [];
    }
    _triggerChange();
  };

  let _handleTaxonomyAdded = function (sTaxonomyId, sParentTaxonomyId, sViewContext) {
    let oActiveRule = _makeActiveRuleDirty();

    if (sParentTaxonomyId === "addItemHandlerforMultiTaxonomy") {
      sParentTaxonomyId = SettingUtils.getTreeRootId();
    }

    var oEntityValidation = _validateEntities([oActiveRule]);
    if (!oEntityValidation.isTagValid) {
      alertify.error(getTranslation().TAG_CANNOT_BE_EMPTY);
      return;
    }

    if (!CS.isEmpty(RuleProps.getKlassTypeId()) && CS.isEmpty(oActiveRule.types)) {
      alertify.error(getTranslation().SELECT_CLASSES_IN_CAUSE_SECTION);
      return;
    }

    if(sViewContext == 'dataRules') {
      CS.pull(oActiveRule.taxonomies, sParentTaxonomyId);
      oActiveRule.taxonomies.push(sTaxonomyId);
    }else {
      var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
      var oEffect = CS.find(aRuleEffect, {type: 'taxonomy'});
      if(CS.isEmpty(oEffect)){
        oEffect = {
          id:UniqueIdentifierGenerator.generateUUID(),
          type: 'taxonomy',
          ruleViolation:{
            color: "red",
            description: "",
            isEnabled: false
          },
          normalization: {
            attributeOperatorList: [],
            calculatedAttributeUnit: "",
            calculatedAttributeUnitAsHTML: "",
            isEnabled: true,
            tagValues: [],
            values: [sTaxonomyId],
            rules: {
              type: '',
              from: "",
              to: ""
            },
            baseType: oNormalizationTypeDictionary['normalization']
          }
        };
        aRuleEffect.push(oEffect);
      }else {
        CS.pull(oEffect.normalization.values, sParentTaxonomyId);
        if(!CS.includes(oEffect.normalization.values, sTaxonomyId)) {
          oEffect.normalization.values.push(sTaxonomyId);
        }
      }
    }

    /** update active rule using normalization props **/
    _assignRuleViolationAndNormalizationToActiveRuleCloneObject(oActiveRule);

    /** update referenced taxonomies in active rule **/
   SettingUtils.getTaxonomyHierarchyForSelectedTaxonomy(sTaxonomyId, sParentTaxonomyId, {fSuccessHandler: _successGetTaxonomies});

  };

  let _successGetTaxonomies = function (sTaxonomyId, sParentTaxonomyId, oResponse) {
    oResponse = oResponse.success;
    let oReferencedTaxonomy = oResponse.referencedTaxonomies;

    let oActiveRule = _makeActiveRuleDirty();
    oActiveRule.configDetails = oActiveRule.configDetails || {referencedTaxonomies: {}};
    let oReferencedTaxonomies = oActiveRule.configDetails.referencedTaxonomies;

    oReferencedTaxonomies[sTaxonomyId] = oReferencedTaxonomy[sTaxonomyId];
    _triggerChange();
  };

  let _fetchTaxonomyById = function (sContext, sTaxonomyId) {
    let oParameters = {};
    if (sContext === TaxonomyTypeDictionary.MAJOR_TAXONOMY) {
      if (sTaxonomyId === "addItemHandlerforMultiTaxonomy") {
        sTaxonomyId = SettingUtils.getTreeRootId();
      } else {
        sTaxonomyId = sTaxonomyId;
      }
    }
    oParameters.id = sTaxonomyId;

    let oTaxonomyPaginationData = RuleProps.getTaxonomyPaginationData();
    let oPostData = CS.assign({taxonomyId: sTaxonomyId}, oTaxonomyPaginationData);

    let oNewEntityVsSearchTextMapping = {
      "taxonomies": oTaxonomyPaginationData.searchText
    };
    let oOldEntityVsSearchTextMapping = SettingScreenProps.screen.getEntityVsSearchTextMapping();
    CS.assign(oOldEntityVsSearchTextMapping, oNewEntityVsSearchTextMapping);


    SettingUtils.csPostRequest(oRuleRequestMapping.GetTaxonomy, {}, oPostData, successFetchFetchTaxonomy, failureFetchFetchTaxonomy);
  };

  let successFetchFetchTaxonomy = function (oResponse) {
    let oSuccess = oResponse.success;
    let aTaxonomyListFromServer = oSuccess.list;
    let oConfigDetails = oSuccess.configDetails;

    let aTaxonomyList = RuleProps.getAllowedTaxonomies();
    RuleProps.setAllowedTaxonomies(CS.concat(aTaxonomyList, aTaxonomyListFromServer));
    RuleProps.setAllowedTaxonomyConfigDetails(CS.assign(RuleProps.getAllowedTaxonomyConfigDetails(), oConfigDetails));
    let oAllowedTaxonomyHierarchyListData = CommonUtils.prepareTaxonomyHierarchyData(RuleProps.getAllowedTaxonomies(), RuleProps.getAllowedTaxonomyConfigDetails());
    RuleProps.setAllowedTaxonomyHierarchyList(oAllowedTaxonomyHierarchyListData);
    _triggerChange();
  };

  let failureFetchFetchTaxonomy = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchFetchTaxonomy", getTranslation());
  };

  let _removedTaxonomyFromCauseSectionOfRule = function (oTaxonomy, sActiveTaxonomyId) {
    let oActiveRule = _makeActiveRuleDirty();
    let sTaxonomyParentId = oTaxonomy.parent.id;
    let aTaxonomyIds = oActiveRule.taxonomies;
    let iActiveTaxonomyIndex = aTaxonomyIds.indexOf(sActiveTaxonomyId);

    if (sTaxonomyParentId == SettingUtils.getTreeRootId()) {
      CS.remove(aTaxonomyIds, function (sTaxonomyId) {
        return sTaxonomyId == sActiveTaxonomyId;
      });
      _assignRuleViolationAndNormalizationToActiveRuleCloneObject(oActiveRule);
      _triggerChange();
    } else {
      aTaxonomyIds[iActiveTaxonomyIndex] = sTaxonomyParentId;
      oActiveRule.taxonomies = CS.uniq(aTaxonomyIds);
      SettingUtils.getTaxonomyHierarchyForSelectedTaxonomy(sTaxonomyParentId, sActiveTaxonomyId, {fSuccessHandler: _successGetReferencedTaxonomyDataOnRemoveOfTaxonomy});
    }
  };

  let _removedTaxonomyFromEffectSectionOfRule = function (oTaxonomy, sActiveTaxonomyId) {
    let oActiveRule = _makeActiveRuleDirty();
    let sTaxonomyParentId = oTaxonomy.parent.id;
    var aRuleEffect = RuleProps.getRuleViolationsAndNormalizationProps();
    var oEffect = CS.find(aRuleEffect, {type: 'taxonomy'});
    let aTaxonomyIds = oEffect.normalization.values;
    let iActiveTaxonomyIndex = aTaxonomyIds.indexOf(sActiveTaxonomyId);

    if (sTaxonomyParentId == SettingUtils.getTreeRootId()) {
      CS.remove(aTaxonomyIds, function (sTaxonomyId) {
        return sTaxonomyId == sActiveTaxonomyId;
      });
      _assignRuleViolationAndNormalizationToActiveRuleCloneObject(oActiveRule);
      _triggerChange();
    } else {
      aTaxonomyIds[iActiveTaxonomyIndex] = sTaxonomyParentId;
      oEffect.normalization.values = CS.uniq(aTaxonomyIds);
      SettingUtils.getTaxonomyHierarchyForSelectedTaxonomy(sTaxonomyParentId, sActiveTaxonomyId, {fSuccessHandler: _successGetReferencedTaxonomyDataOnRemoveOfTaxonomy});
    }
  };

  let _handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oTaxonomy, sActiveTaxonomyId, sViewContext) {
    if (sViewContext == 'dataRules') {
       _removedTaxonomyFromCauseSectionOfRule(oTaxonomy, sActiveTaxonomyId);
    }
    else {
       _removedTaxonomyFromEffectSectionOfRule(oTaxonomy, sActiveTaxonomyId);
    }
  };

  let _successGetReferencedTaxonomyDataOnRemoveOfTaxonomy = function (sTaxonomyId, sParentTaxonomyId, oResponse) {
    oResponse = oResponse.success;
    let oReferencedTaxonomy = oResponse.referencedTaxonomies;

    let oActiveRule = _makeActiveRuleDirty();
    oActiveRule.configDetails = oActiveRule.configDetails || {referencedTaxonomies: {}};
    let oReferencedTaxonomies = oActiveRule.configDetails.referencedTaxonomies;

    oReferencedTaxonomies[sTaxonomyId] = oReferencedTaxonomy[sTaxonomyId];

    _assignRuleViolationAndNormalizationToActiveRuleCloneObject(oActiveRule);
    _triggerChange();
  };

  let _handleRuleTypeSelected = function (aSelectedRuleType) {
    let oActiveRule = _makeActiveRuleDirty();
    oActiveRule.type = aSelectedRuleType[0];

    /** If language dependent set to true & changed rule type from S&D or Violation to clasification, then language
     *  dependancy should get reset **/
    let oRuleTypeDictionary = new RuleTypeDictionary();
    if(oActiveRule.type === oRuleTypeDictionary.CLASSIFICATION_RULE) {
      oActiveRule.isLanguageDependent = false;
      oActiveRule.languages = [];
    }
  };

  let _closeRuleDialog = function () {
    RuleProps.setIsRuleDialogActive(false);
    _triggerChange();
  };

  let _handleRuleConfigDialogActionButtonClicked = function (sButtonId) {
    if (sButtonId == "save") {
      _saveRule({});
    } else if (sButtonId == "cancel") {
      _discardUnsavedRule({});
    } else {
      if(RuleProps.getIsRuleDialogActive()) {
        _closeRuleDialog();
      }
      else {return}
    }
  };

  let _editButtonClicked = function (sRuleId) {
    RuleProps.setKlassTypeId("");
    _fetchRuleById(sRuleId)
  };

  let _postProcessRuleListAndSave = function (oCallbackData) {
    let aRuleGridData = RuleProps.getRuleGridData();
    let aRuleListToSave = [];

    let bSafeToSave = GridViewStore.processGridDataToSave(aRuleListToSave, GridViewContexts.RULE, aRuleGridData);
    if (bSafeToSave) {
      return _saveRulesInBulk(aRuleListToSave, oCallbackData);
    }
    return CommonUtils.rejectEmptyPromise();
  };

  let _saveRulesInBulk = function (aRuleListToSave, oCallbackData) {
      return SettingUtils.csPostRequest(oRuleRequestMapping.SaveBulkRule, {}, aRuleListToSave,
          successSaveRulesInBulk.bind(this, oCallbackData), failureSaveRulesInBulk);
  };

  let successSaveRulesInBulk = function (oCallbackData, oResponse) {
    let aRulesList = oResponse.success;
    var aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.RULE, aRulesList);
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.RULE);

    var aRulesGridData = RuleProps.getRuleGridData();
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    var oMasterRulesMap = SettingUtils.getAppData().getRuleList();

    CS.forEach(aRulesList, function (oRules) {
      var sRuleId = oRules.id;
      var iIndex = CS.findIndex(aRulesGridData, {id: sRuleId});
      aRulesGridData[iIndex] = oRules;
      oMasterRulesMap[sRuleId] = oRules;
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
    SettingUtils.failureCallback(oResponse, "failureSaveRulesInBulk", getTranslation());
  };

  var _discardRuleGridViewChanges = function (oCallbackData) {
    var aRulesGridData = RuleProps.getRuleGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.RULE);
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    var bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedRule, iIndex) {
      if (oOldProcessedRule.isDirty) {
        var oEvent = CS.find(aRulesGridData, {id: oOldProcessedRule.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.RULE, [oEvent])[0];
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

  var _handleExportRule = function (oSelectiveExportDetails) {
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

  var uploadFileImport = function (oImportExcel, oCallback) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  var successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
  };

  var _handleRuleFileUploaded = function (aFiles,oImportExcel) {
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

  let _handleRuleLanguagesSelected = function (aSelectedItems) {
    let oActiveRule = _makeActiveRuleDirty();
    let aDataLanguages = RuleProps.getDataLanguages();
    let aSelectedLanguagesCode = CommonUtils.getCodesById(aDataLanguages, aSelectedItems);
    oActiveRule.languages = aSelectedLanguagesCode;
    _triggerChange();
  };

  let _handleDataLanguagesMSSCrossIconClicked = function (sId) {
    let oActiveRule = _makeActiveRuleDirty();
    let aDataLanguages = RuleProps.getDataLanguages();
    let aDeletedDataLanguagesCode = CommonUtils.getCodesById(aDataLanguages, [sId]);
    CS.remove(oActiveRule.languages, (sDataLanguageCode) => {
      return sDataLanguageCode === aDeletedDataLanguagesCode[0];
    });
    _triggerChange();
  };

  let _handleRuleDataLanguagesPopoverOpen = function () {
    let oCallback = {
      functionToExecute : () => {
        let oLanguagesInfo = SessionProps.getLanguageInfoData();
        let aDataLanguages = oLanguagesInfo.dataLanguages;
        RuleProps.setDataLanguages(aDataLanguages);
        _triggerChange();
      }
    };
    getLanguageFromServer(oCallback);
  };

  /***************** PUBLIC API's **************/
  return {
    setSelectedRule: function (oRule) {
      _setSelectedRule(oRule);
    },

    getRuleScreenLockStatus: function () {
      return _getRuleScreenLockStatus();
    },

    saveRule: function (oCallbackData) {
      _saveRule(oCallbackData);
    },

    discardUnsavedRule: function (oCallbackData) {
      _discardUnsavedRule(oCallbackData);
    },

    createDefaultRuleObject: function () {
      _createDefaultRuleObject();
    },

    cancelRuleCreation: function () {
      _cancelRuleCreation();
    },

    handleRuleSingleValueChanged: function (sKey, sValue) {
      var oActiveRule = _makeActiveRuleDirty();
      oActiveRule[sKey] = sValue;
      if (sKey === "isLanguageDependent" && !sValue) {
        oActiveRule.languages = [];
      }
      _triggerChange();
    },

    createRule: function () {
      _createNewRule();
    },

    deleteRule: function (aSelectedRules) {
      _deleteRule(aSelectedRules);
    },

    getRuleDetails: function () {
      _getRuleDetails();
    },

    handleRuleRefreshMenuClicked: function () {
      _handleRuleRefreshMenuClicked();
    },

    handleRuleListNodeClicked: function (oModel) {
      _handleRuleListNodeClicked(oModel.id);
    },

    handleRuleNameChanged: function (sValue) {
      _handleRuleNameChanged(sValue);
      _triggerChange();
    },

    handleAttributeAdded: function (sAttributeId, sAttributeContext) {
      _handleAttributeAdded(sAttributeId, sAttributeContext);
      _triggerChange();
    },

    handleRelationshipAdded: function (sAttributeId, sAttributeContext) {
      _handleRelationshipAdded(sAttributeId, sAttributeContext);
      _triggerChange();
    },

    handleRoleAdded: function (sAttributeId, sAttributeContext) {
      _handleRoleAdded(sAttributeId, sAttributeContext);
      _triggerChange();
    },

    handleTypeAdded: function (sAttributeId, sAttributeContext) {
      _handleTypeAdded(sAttributeId, sAttributeContext);
      _triggerChange();
    },

    handleTagAdded: function (sTagId, sTagContext) {
      _handleTagAdded(sTagId, sTagContext);
      _triggerChange();
    },

    handleRuleElementDeleteButtonClicked: function (sElementId, sContext, sHandlerContext) {
      _handleRuleElementDeleteButtonClicked(sElementId, sContext, sHandlerContext);
      _triggerChange();
    },

    handleRuleAttributeValueChanged: function (sElementId, sValueId, aValueList,sContext) {
      _handleRuleAttributeValueChanged(sElementId, sValueId, aValueList,sContext);
      _triggerChange();
    },

    handleSelectionToggleButtonClicked: function (sKey, sId) {
      _handleSelectionToggleButtonClicked(sKey, sId);
      _triggerChange();
    },

    handleRuleAttributeValueChangedForNormalization: function (sRoleId, aUsers) {
      _handleRuleAttributeValueChangedForNormalization(sRoleId, aUsers);
      _triggerChange();
    },

    handleRuleAttributeColorValueChanged: function (sElementId, sValueId, sValue) {
      _handleRuleAttributeColorValueChanged(sElementId, sValueId, sValue);
      _triggerChange();
    },

    handleRuleAttributeDescriptionValueChanged: function (sElementId, sValueId, sValue) {
      _handleRuleAttributeDescriptionValueChanged(sElementId, sValueId, sValue);
      _triggerChange();
    },

    handleRuleAttributeDescriptionValueChangedInNormalization: function (sAttrId, oAttrVal, sContext, sVal, sValAsHtml) {
      _handleRuleAttributeDescriptionValueChangedInNormalization(sAttrId, oAttrVal, sContext, sVal, sValAsHtml);
      _triggerChange();
    },

    handleRuleAttributeValueChangedInNormalization: function (sAttrId, oAttrVal, sContext, sVal) {
      _handleRuleAttributeValueChangedInNormalization(sAttrId, oAttrVal, sContext, sVal);
      _triggerChange();
    },

    handleRuleAddAttributeClicked: function (sElementId, sContext) {
      _handleRuleAddAttributeClicked(sElementId, sContext);
      _triggerChange();
    },

    handleRuleAttributeValueTypeChanged: function (sAttributeId, sValueId, sTypeId,sContext, isCause) {
      _handleRuleAttributeValueTypeChanged(sAttributeId, sValueId, sTypeId,sContext, isCause);
      _triggerChange();
    },

    handleRuleAttributeValueDeleteClicked: function (sAttributeId, sValueId,sContext) {
      _handleRuleAttributeValueDeleteClicked(sAttributeId, sValueId,sContext);
      _triggerChange();
    },

    handleRuleAttributeRangeValueChanged: function (sAttributeId, sValueId, sValue, sRange) {
      _handleRuleAttributeRangeValueChanged(sAttributeId, sValueId, sValue, sRange);
      _triggerChange();
    },

    handleAttributeValueChangedForRangeInNormalization: function (sAttributeId, sValueId, sValue, sType, sRange) {
      _handleAttributeValueChangedForRangeInNormalization(sAttributeId, sValueId, sValue, sType, sRange);
    },

    handleRuleDetailPartnerApplyClicked: function(aSelectedItems){
      _handleRuleDetailPartnerApplyClicked(aSelectedItems);
      _triggerChange();
    },

    handleRuleDetailEndpointsChanged: function (aSelectedItems) {
      _handleRuleDetailEndpointsChanged(aSelectedItems);
      _triggerChange();
    },

    handleTagNodeClicked: function (oModel) {
      _handleTagNodeClicked(oModel);
      _triggerChange();
    },

    handleContentFilterInnerTagAdded:function(sTagGroupId, aTagItemList, sRuleId,iRelevance, sViewContext, bIsDeleted){
      _handleContentFilterInnerTagAdded(sTagGroupId, aTagItemList, sRuleId,iRelevance, sViewContext, bIsDeleted);
      _triggerChange();
    },

    handleRuleFilterInnerTagAdded: function (sTagId, aTagValueRelevanceData, sAttributeValId) {
      _handleRuleFilterInnerTagAdded(sTagId, aTagValueRelevanceData, sAttributeValId);
    },

    handleRuleFilterInnerTagForNormalizationAdded: function (sTagId, aTagValueRelevanceData) {
      _handleRuleFilterInnerTagForNormalizationAdded(sTagId, aTagValueRelevanceData);
    },

    handleYesNoViewToggled: function (sSectionId, sElementId, bValue) {
      _handleYesNoViewToggled(sSectionId, sElementId, bValue);
    },

    handleYesNoViewToggledForNormalization: function (sSectionId, bValue) {
      _handleYesNoViewToggledForNormalization(sSectionId, bValue);
    },

    handleAddNewBlackListItem: function (sValue) {
      _handleAddNewBlackListItem(sValue);
      _triggerChange();
    },

    handleRemoveBlackListItem: function (sIndex) {
      _handleRemoveBlackListItem(sIndex);
      _triggerChange();
    },

    handleEditBlackListItem: function (iIndex, sValue) {
      _handleEditBlackListItem(iIndex, sValue);
      _triggerChange();
    },

    handleRuleRightPanelBarItemClicked: function(sIconContext){
      _handleRuleRightPanelBarItemClicked(sIconContext);
      _triggerChange();
    },

    handleRuleListDropOnInput: function(oDropModel){
      _handleRuleListDropOnInput(oDropModel);
    },

    handleRuleRemoveBlackListIconClicked: function(sDropId){
      _handleRuleRemoveBlackListIconClicked(sDropId);
      _triggerChange();
    },

    handleAttributeVisibilityButtonClicked: function(sAttrId, oAttrCondition){
      _handleAttributeVisibilityButtonClicked(sAttrId, oAttrCondition);
    },

    handleCompareWithSystemDateButtonClicked: function(sAttrId, oAttrCondition){
      _handleCompareWithSystemDateButtonClicked(sAttrId, oAttrCondition);
    },

    handleAttributeViewTypeChanged: function(sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId){
      _handleAttributeViewTypeChanged(sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId);
    },

    handleAttributeLinkedInRuleEffectSection: function (sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId) {
      _handleAttributeLinkedInRuleEffectSection(sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId);
    },

    handleConcatenatedFormulaChanged: function(oNormalization, aAttributeConcatenatedList, oReferencedData) {
      _handleConcatenatedFormulaChanged(oNormalization, aAttributeConcatenatedList, oReferencedData);
    },

    handleRuleCalcAttrAddOperator: function (sAttrId, sId, sOperatorType) {
      var oRule = _makeActiveRuleDirty();
      var oNormalization = CS.find(oRule.normalizations, {entityId: sAttrId, id: sId});
      if (!oNormalization) {
        oNormalization = {entityId: sAttrId, id: sId, attributeOperatorList: [], values: [], baseType: oNormalizationTypeDictionary["normalization"]};
        oRule.normalizations.push(oNormalization);
      }
      var aAttributeOperatorListToModify = oNormalization.attributeOperatorList ? oNormalization.attributeOperatorList : [];

      var iAttributeOperatorListLength = aAttributeOperatorListToModify.length;
      var sOperator = null;
      var uuid = UniqueIdentifierGenerator.generateUUID();

      if (sOperatorType != "ATTRIBUTE" && sOperatorType != "VALUE") {
        sOperator = sOperatorType;
      }

      var oAttributeOperator = {
        id: uuid,
        attributeId: null,
        operator: sOperator,
        type: sOperatorType,
        value: null,
        order: iAttributeOperatorListLength
      };

      aAttributeOperatorListToModify.push(oAttributeOperator);
      oNormalization.attributeOperatorList = aAttributeOperatorListToModify;

      var aRuleEffects = RuleProps.getRuleViolationsAndNormalizationProps();
      var oRuleEffect = CS.find(aRuleEffects, {entityId: sAttrId, id: sId});
      if (!oRuleEffect) {
        oRuleEffect = {entityId: sAttrId, id: sId, normalization: {}, ruleViolation: {}};
        aRuleEffects.push(oRuleEffect);
      }
      oNormalization.isEnabled = true;
      oRuleEffect.normalization = oNormalization;
    },

    handleRuleCalcAttrOperatorAttributeValueChanged: function (sAttrId, sId, sType, sAttributeOperatorId, sValue) {
      var oRule = _makeActiveRuleDirty();
      var oNormalization = CS.find(oRule.normalizations, {entityId: sAttrId, id: sId});

      var oAttributeOperator = CS.find(oNormalization.attributeOperatorList, {id: sAttributeOperatorId});

      if(oAttributeOperator) {
        var sOperator = null;
        var sVal = null;
        var sAttributeId = null;

        if(sType == "ATTRIBUTE"){
          sAttributeId = sValue;
        }else if(sType == "VALUE"){
          sVal = sValue;
        }else{
          sOperator = sValue;
          sType = sValue;
        }

        oAttributeOperator.attributeId = sAttributeId;
        oAttributeOperator.operator = sOperator;
        oAttributeOperator.value = sVal;
        oAttributeOperator.type = sType;
      }

      var aRuleEffects = RuleProps.getRuleViolationsAndNormalizationProps();
      var oRuleEffect = CS.find(aRuleEffects, {entityId: sAttrId, id: sId});

      oNormalization.isEnabled = true;
      oRuleEffect.normalization = oNormalization;

      let oComponentProps = SettingUtils.getComponentProps();
      let oReferencedAttributesMap = oComponentProps.screen.getReferencedAttributes();
      if (CS.isEmpty(oReferencedAttributesMap[sAttrId])) {
        let aAllowedAttributes = oComponentProps.calculatedAttributeConfigView.getAllowedAttributes();
        let oReferencedAttribute = CS.find(aAllowedAttributes, {"id": sAttrId});
        if (!CS.isEmpty(oReferencedAttribute)) {
          oReferencedAttributesMap[sAttrId] = oReferencedAttribute;
        }
      }
    },

    handleRuleCalcAttrDeleteOperatorAttributeValue: function (sAttrId, sId, sAttributeOperatorId) {
      var oRule = _makeActiveRuleDirty();
      var oNormalization = CS.find(oRule.normalizations, {entityId: sAttrId, id: sId});

      var iCount = 0;
      CS.remove(oNormalization.attributeOperatorList, function (oAttributeOperator) {
        if(oAttributeOperator.id == sAttributeOperatorId) {
          return true;
        }
        oAttributeOperator.order = iCount++;
      });
      var aRuleEffects = RuleProps.getRuleViolationsAndNormalizationProps();
      var oRuleEffect = CS.find(aRuleEffects, {entityId: sAttrId, id: sId});

      oNormalization.isEnabled = true;
      oRuleEffect.normalization = oNormalization;
    },

    handleRuleCalcAttrCustomUnitChanged: function (sAttrId, sId, sUnit, sUnitAsHTML) {
      var oRule = _makeActiveRuleDirty();
      var oNormalization = CS.find(oRule.normalizations, {entityId: sAttrId, id: sId});

      if (!oNormalization) {
        oNormalization = {entityId: sAttrId, id: sId, attributeOperatorList: [], values: []};
        oRule.normalizations.push(oNormalization);
      }

      oNormalization.calculatedAttributeUnit = sUnit;
      oNormalization.calculatedAttributeUnitAsHTML = sUnitAsHTML;

      var aRuleEffects = RuleProps.getRuleViolationsAndNormalizationProps();
      var oRuleEffect = CS.find(aRuleEffects, {entityId: sAttrId, id: sId});

      oNormalization.isEnabled = true;
      oRuleEffect.normalization = oNormalization;
    },

    handleRuleConfigDialogButtonClicked: function (sButtonId) {
      if(sButtonId === "create") {
        RuleStore.createRule();
      } else {
        RuleStore.cancelRuleCreation();
      }
    },

    handleRuleDetailsKlassTypeMSSChanged: function (aNewKlassType) {
      _handleRuleDetailsKlassTypeMSSChanged(aNewKlassType);
    },

    handleRuleDetailsKlassMSSChanged: function (sContext, aSelectedKlasses) {
      _handleRuleDetailsKlassMSSChanged(sContext, aSelectedKlasses);
    },

    handleTaxonomyAdded: function (sTaxonomyId, sParentTaxonomyId, sViewContext) {
      _handleTaxonomyAdded(sTaxonomyId, sParentTaxonomyId, sViewContext);
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId) {
      RuleProps.setAllowedTaxonomies([]);
      let oTaxonomyPaginationData = RuleProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.searchText = "";
      oTaxonomyPaginationData.from = 0;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sActiveTaxonomyId, sViewContext) {
      _handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sActiveTaxonomyId, sViewContext);
    },

    handleRuleTypeSelected: function (aSelectedItems) {
      _handleRuleTypeSelected(aSelectedItems);
      _triggerChange();
    },

    handleTaxonomySearchClicked: function (sContext, sTaxonomyId, sSearchText) {
      RuleProps.setAllowedTaxonomies([]);
      let oTaxonomyPaginationData = RuleProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.from = 0;
      oTaxonomyPaginationData.searchText = sSearchText;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleTaxonomyLoadMoreClicked: function (sContext, sTaxonomyId) {
      let oTaxonomyPaginationData = RuleProps.getTaxonomyPaginationData();
      let aTaxonomyList = RuleProps.getAllowedTaxonomies();
      oTaxonomyPaginationData.from = aTaxonomyList.length;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleListViewSearchOrLoadMoreClicked: function (sSearchText, bLoadMore) {
      var oAppData = SettingUtils.getAppData();
      var oRuleMap = oAppData.getRuleList();
      SettingUtils.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, oRuleMap, RuleProps, _fetchRuleList);
    },

    fetchRuleListForGridView: function () {
      _fetchRuleListForGridView();
    },

    handleRuleConfigDialogActionButtonClicked: function (sTabId) {
      _handleRuleConfigDialogActionButtonClicked(sTabId);
    },

    setUpRuleConfigGridView: function (bIsTreeItemClicked) {
      _setUpRuleConfigGridView(bIsTreeItemClicked);
    },

    ruleEditButtonClicked: function (sRuleId) {
      _editButtonClicked(sRuleId);
    },

    resetPaginationData: function () {
      RuleProps.resetPaginationData();
    },

    postProcessRuleListAndSave: function (oCallbackData) {
      _postProcessRuleListAndSave(oCallbackData)
          .then(_triggerChange)
          .catch(CS.noop);
    },

    discardRuleListChanges: function (oCallBack) {
      _discardRuleGridViewChanges(oCallBack);
      _triggerChange();
    },

    handleExportRule: function (oSelectiveExportDetails) {
      _handleExportRule(oSelectiveExportDetails);
    },

    handleRuleFileUploaded: function (aFiles,oImportExcel) {
      _handleRuleFileUploaded(aFiles,oImportExcel);
    },

    handleRuleLanguagesSelected: function (aSelectedItems) {
      _handleRuleLanguagesSelected(aSelectedItems);
    },

    handleDataLanguagesMSSCrossIconClicked: function (sId) {
      _handleDataLanguagesMSSCrossIconClicked(sId);
    },

    handleRuleDataLanguagesPopoverOpen: function () {
     _handleRuleDataLanguagesPopoverOpen();
    },
  }
})();

MicroEvent.mixin(RuleStore);

export default RuleStore;
