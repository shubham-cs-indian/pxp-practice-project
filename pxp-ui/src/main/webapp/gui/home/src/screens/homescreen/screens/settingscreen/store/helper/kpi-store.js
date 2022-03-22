import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import { KpiRequestMapping as oKpiRequestMapping, SettingsRequestMapping } from '../../tack/setting-screen-request-mapping';
import KpiProps from './../model/kpi-config-view-props';
import KpiConfigGridViewSkeleton from './../../tack/kpi-config-grid-view-skeleton';
import SettingUtils from '../helper/setting-utils';
import CommonUtils from './../../../../../../commonmodule/util/common-utils';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import KpiConfigDictionary from '../../../../../../commonmodule/tack/kpi-config-dictionary';
import Constants from '../../../../../../commonmodule/tack/constants';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';

var KpiStore = (function () {

  var _triggerChange = function () {
    KpiStore.trigger('kpi-changed');
  };

  var _makeActiveDetailKPIDirty = function () {
    let oActiveDetailKPI = KpiProps.getActiveKPI();
    SettingUtils.makeObjectDirty(oActiveDetailKPI);
    // return oActiveDetailKPI.clonedObject;
    let oActiveBlock = KpiProps.getActiveBlock();
    SettingUtils.makeObjectDirty(oActiveBlock);
    return oActiveBlock.clonedObject;
  };

  var _makeActiveKPIDirty = function () {
    let oActiveDetailKPI = KpiProps.getActiveKPI();
    SettingUtils.makeObjectDirty(oActiveDetailKPI);
    return oActiveDetailKPI.clonedObject;
  };

  let _createDummyRule = function (sRuleType, sRuleBlockId) {
    return ({
      klassIds: [],
      id: UniqueIdentifierGenerator.generateUUID(),
      attributes: [],
      label: UniqueIdentifierGenerator.generateUntitledName(),
      relationships: [],
      tags: [],
      type: sRuleType,
    });
  };

  var _getActiveBlock = function () {
    let oActiveBlock = KpiProps.getActiveBlock();
    oActiveBlock = oActiveBlock.clonedObject || oActiveBlock;
    return oActiveBlock;
  };

  var _generateADMForTags = function (aOldTags, aNewTags) {
    let oADM = {};
    let aAddedTags = [];
    let aModifiedTags = [];
    let aDeletedTags = [];
    CS.forEach(aNewTags, function (oNewTag) {
      let oFoundTag = CS.find(aOldTags, {tagId: oNewTag.tagId});
      if (CS.isEmpty(oFoundTag)) {
        //added
        if (!CS.isEmpty(oNewTag.tagValues)) {
          aAddedTags.push({tagId: oNewTag.tagId, tagValueIds: oNewTag.tagValues})
        }
      } else {
        //modified
        if (CS.isEmpty(oNewTag.tagValues)) {
          aDeletedTags.push(oNewTag.tagId);
        } else {
          let oOldTagValueIds = oFoundTag.tagValues;
          let oNewTagValueIds = oNewTag.tagValues;
          let aAddedTagValueIds = CS.difference(oNewTagValueIds, oOldTagValueIds);
          let aDeletedTagValueIds = CS.difference(oOldTagValueIds, oNewTagValueIds);
          if (!CS.isEmpty(aAddedTagValueIds) || !CS.isEmpty(aDeletedTagValueIds))
            aModifiedTags.push({
              tagId: oFoundTag.tagId,
              addedTagValueIds: aAddedTagValueIds,
              aDeletedTagValueIds: aDeletedTagValueIds
            });
        }
      }
    });

    CS.forEach(aOldTags, function (aOldTag) {
      let oFoundTag = CS.find(aNewTags, {tagId: aOldTag.tagId});
      if (CS.isEmpty(oFoundTag)) {
        aDeletedTags.push(aOldTag.tagId);
      }
    });
    oADM.added = aAddedTags;
    oADM.modified = aModifiedTags;
    oADM.deleted = aDeletedTags;

    return oADM;
  };

  let _generateADMForDrillDowns = function (aOldDrilldowns, aNewDrilldowns) {
    var oADM = {
      added: [],
      modified: [],
      deleted: []
    };

    CS.forEach(aOldDrilldowns, function (oOldDrilldown) {
      var oNewDrilldown = CS.find(aNewDrilldowns, {id: oOldDrilldown.id});
      if(CS.isEmpty(oNewDrilldown)) {
        oADM.deleted.push(oOldDrilldown.id);
      }
      else {
        if(!CS.isEqual(oOldDrilldown, oNewDrilldown)) {
          // oADM.modified.push(oNewDrilldown);
          if (oNewDrilldown.type == "tag" && CS.isEmpty(oNewDrilldown.typeIds)) {
            oADM.drillDownsTagNotSelected = true;
            return false;
          }
          let oModifiedDrillDown = {
            id: oNewDrilldown.id,
            type: oNewDrilldown.type,
            addedTypes: oNewDrilldown.typeIds,
            deletedTypes: oOldDrilldown.typeIds,
          }
          oADM.modified.push(oModifiedDrillDown);
        }
      }
    });

    if (!oADM.drillDownsTagNotSelected) {
      oADM.added = CS.differenceBy(aNewDrilldowns, aOldDrilldowns, 'id') || [];
      CS.forEach(oADM.added, function (oAddedDrillDown) {
        if (oAddedDrillDown.type == "tag" && CS.isEmpty(oAddedDrillDown.typeIds)) {
          oADM.drillDownsTagNotSelected = true;
          return false;
        }
      });
    }

    return oADM;
  };

  let _getModifiedTargetFiltersADM = function (oOldTargetFilters, oNewTargetFilters) {
    let oTargetFiltersADM = {};
    oTargetFiltersADM.addedKlassIds = CS.difference(oNewTargetFilters.klassIds, oOldTargetFilters.klassIds);
    oTargetFiltersADM.deletedKlassIds = CS.difference(oOldTargetFilters.klassIds, oNewTargetFilters.klassIds);
    oTargetFiltersADM.addedTaxonomyIds = CS.difference(oNewTargetFilters.taxonomyIds, oOldTargetFilters.taxonomyIds);
    oTargetFiltersADM.deletedTaxonomyIds = CS.difference(oOldTargetFilters.taxonomyIds, oNewTargetFilters.taxonomyIds);
    return oTargetFiltersADM;
  };

  let _generateADMForRoles = function (oOldRoles, oNewRoles) {
    let oModifiedRoles = [];
    let oAddedRoles = [];
    CS.forEach(oNewRoles, function (aRolesIds, sRoleId) {
      let oADM = {};
      oADM.roleId = sRoleId;
      oADM.addedCandidates = CS.difference(aRolesIds, oOldRoles[sRoleId]);
      oADM.deletedCandidates = CS.difference(oOldRoles[sRoleId], aRolesIds);
      if(!CS.isEmpty(oADM.addedCandidates)|| !CS.isEmpty(oADM.deletedCandidates)){
        oModifiedRoles.push(oADM);
      }

    });
    return {modifiedRoles: oModifiedRoles, addedRoles: []};
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

  let _generateADMForRules = function (oNewRuleBlock, oOldRuleBlock, oADM) {
    let aOldRules = oOldRuleBlock.rules;
    let aNewRules = oNewRuleBlock.rules;
    if (!CS.isEqual(aNewRules, aOldRules)) {
      let aOldRules = oOldRuleBlock.rules;
      let aNewRules = oNewRuleBlock.rules;
      let sRuleType = oNewRuleBlock.type;
      CS.forEach(aNewRules, function (oNewRule) {

        let oFoundRule = CS.find(aOldRules, {id: oNewRule.id});
        if (!CS.isEqual(oFoundRule, oNewRule)) {

          if (!(sRuleType == KpiConfigDictionary.COMPLETENESS_BLOCK || sRuleType == KpiConfigDictionary.UNIQUENESS_BLOCK)) {
            let bIsRuleInvalid = !SettingUtils.validateRule(oNewRule);
            if (bIsRuleInvalid) {
              oADM.stopSave = bIsRuleInvalid;
              return false;
            }
          }

          if (CS.isEmpty(oFoundRule)) {
            let oAddedRule = CS.cloneDeep(oNewRule);
            /**KlassIds TaxonomyIds Blank Until Backed Impl **/
            oAddedRule.klassIds = [];
            oAddedRule.taxonomyIds = [];
            if (sRuleType == KpiConfigDictionary.COMPLETENESS_BLOCK || sRuleType == KpiConfigDictionary.UNIQUENESS_BLOCK) {
              CS.forEach(oAddedRule.attributes, function (oAddedRule) {
                oAddedRule.rules = [];
              });
              CS.forEach(oAddedRule.tags, function (oAddedRule) {
                oAddedRule.rules = [];
              });
              CS.forEach(oAddedRule.relationships, function (oAddedRule) {
                oAddedRule.rules = [];
              })
            }
            oADM.addedRules.push(oAddedRule);
          } else {
            let oModifiedRuleObject = {};
            oModifiedRuleObject.id = oNewRule.id;
            oModifiedRuleObject.label = oNewRule.label;
            oModifiedRuleObject.type = oNewRule.type;
            oModifiedRuleObject.addedKlassIds = [];
            oModifiedRuleObject.deletedKlassIds = [];
            oModifiedRuleObject.addedTaxonomyIds = [];
            oModifiedRuleObject.deletedTaxonomyIds = [];
            var oADMForAttribute = {
              addedAttributeRules: [],
              modifiedAttributeRules: [],
              deletedAttributeRules: []
            };
            _generateActiveRuleADMForAttribute(oFoundRule.attributes, oNewRule.attributes, oADMForAttribute);

            var oADMForTags = {
              addedTagRules: [],
              modifiedTagRules: [],
              deletedTagRules: []
            };

            _generateActiveRuleADMForTags(oFoundRule.tags, oNewRule.tags, oADMForTags);

            var oADMForRelationships = {
              addedRelationshipRules: [],
              modifiedRelationshipRules: [],
              deletedRelationshipRules: []
            };

            _generateActiveRuleADMForRelationship(oFoundRule.relationships, oNewRule.relationships, oADMForRelationships);

            oModifiedRuleObject = CS.assign(oModifiedRuleObject, oADMForAttribute, oADMForTags, oADMForRelationships);
            oADM.modifiedRules.push(oModifiedRuleObject);
          }
        }
      });
      CS.forEach(oOldRuleBlock.rules, function (oOldRule) {
        let oFoundRule = CS.find(aNewRules, {id: oOldRule.id});
        if (CS.isEmpty(oFoundRule)) {
          oADM.deletedRules.push(oOldRule.id);
        }
      });
    }
  };

  let _checkIsRuleBlockUpdated = function (oNewRuleBlock, oOldRuleBlock) {
    if (!CS.isEqual(oNewRuleBlock.unit, oOldRuleBlock.unit)) {
      return true;
    } else if (!CS.isEqual(oNewRuleBlock.threshold, oOldRuleBlock.threshold)) {
      return true;
    } else if (!CS.isEqual(oNewRuleBlock.task, oOldRuleBlock.task)) {
      return true;
    } else if (!CS.isEqual(oNewRuleBlock.color, oOldRuleBlock.color)) {
      return true;
    } else {
      return false;
    }
  };

  let _validateNewRuleBlock = function (oRuleBlock, oADM, oCallBack) {
    if (!CS.isEmpty(oRuleBlock.threshold)) {
      if (!CS.isInteger(oRuleBlock.threshold.upper) || !CS.isInteger(oRuleBlock.threshold.lower) || (oRuleBlock.threshold.upper < oRuleBlock.threshold.lower)) {
        oADM.stopSave = true;
        oCallBack && oCallBack.functionToExecute && oCallBack.functionToExecute();
        return true;
      }
    }
    return false;
  };

  let _generateADMForRuleBlocks = function (oADM, oCallBack) {
    let oReferencedData = KpiProps.getReferencedData();
    let oReferencedRuleBlocks = oReferencedData.referencedRules;
    CS.forEach(oReferencedRuleBlocks, function (oRuleBlock) {
      let oNewRuleBlock = oRuleBlock.clonedObject;
      let oOldRuleBlock = oRuleBlock;
      if (oNewRuleBlock) {
        if (_validateNewRuleBlock(oNewRuleBlock, oADM, oCallBack)) {
          return ;
        }
        _generateADMForRules(oNewRuleBlock, oOldRuleBlock, oADM);
        if (_checkIsRuleBlockUpdated(oNewRuleBlock, oOldRuleBlock)) {
          let oModifiedGovernanceRuleBlock = {};
          oModifiedGovernanceRuleBlock.id = oNewRuleBlock.id;//oRuleBlock.id;
          oModifiedGovernanceRuleBlock.unit = oNewRuleBlock.unit;//oRuleBlock.unit;
          oModifiedGovernanceRuleBlock.threshold = oNewRuleBlock.threshold;//oRuleBlock.threshold;
          oModifiedGovernanceRuleBlock.task = oNewRuleBlock.task;//oRuleBlock.task;
          oModifiedGovernanceRuleBlock.color = oNewRuleBlock.color || "";
          oADM.modifiedGovernanceRuleBlocks.push(oModifiedGovernanceRuleBlock);
        }
      }
    });
  };

  let _generatedADMToSave = function (oActiveKPI, oCallBack) {
    let oADM = {};
    let oOldKPI = oActiveKPI;
    let oNewKPI = oActiveKPI.clonedObject;

    oADM.id = oNewKPI.id;
    oADM.label = oNewKPI.label;
    oADM.frequency = oNewKPI.frequency;
    oADM.addedDashboardTabId = null;
    oADM.deletedDashboardTabId = null;
    if (oNewKPI.dashboardTabId != oOldKPI.dashboardTabId) {
      oADM.addedDashboardTabId = oNewKPI.dashboardTabId;
      oADM.deletedDashboardTabId = oOldKPI.dashboardTabId;
    }
    oADM.modifiedTargetFilters = _getModifiedTargetFiltersADM(oOldKPI.targetFilters, oNewKPI.targetFilters);
    oADM.modifiedGovernanceRuleBlocks = [];
    oADM.modifiedRules = [];
    oADM.addedRules = [];
    oADM.deletedRules = [];
    let oADMForRoles = _generateADMForRoles(oOldKPI.roles, oNewKPI.roles);
    oADM.addedRoles = oADMForRoles.addedRoles;
    oADM.modifiedRoles = oADMForRoles.modifiedRoles;
    let oADMForTags = _generateADMForTags(oOldKPI.kpiTags, oNewKPI.kpiTags);
    oADM.addedTags = oADMForTags.added.length ? oADMForTags.added: []; //oADMForTags.added;
    oADM.deletedTags = oADMForTags.deleted.length ? oADMForTags.deleted: []; //oADMForTags.deleted;
    oADM.modifiedTags = oADMForTags.modified.length ? oADMForTags.modified: []; //oADMForTags.modified;

    oADM.addedOrganizationIds = CS.difference(oNewKPI.organizations, oOldKPI.organizations);
    oADM.deletedOrganizationIds = CS.difference(oOldKPI.organizations, oNewKPI.organizations);
    oADM.addedEndpoints = CS.difference(oNewKPI.endpoints, oOldKPI.endpoints);
    oADM.deletedEndpoints = CS.difference(oOldKPI.endpoints, oNewKPI.endpoints);

    // oADM.addedDrillDowns = [];
    // oADM.modifiedDrillDowns = [];
    // oADM.deletedDrillDowns = [];

    // if (oADMForTags.added.length) {
    //   oADM.addedTags = oADMForTags.added;
    // }
    // if (oADMForTags.deleted.length) {
    //   oADM.deletedTags = oADMForTags.deleted
    // }
    // if (oADMForTags.modified.length) {
    //   oADM.modifiedTags = oADMForTags.modified;
    // }
    // oADMForTags.addedTags = oADMForTags.added||[];
    // oADMForTags.deletedTags = oADMForTags.deleted||[];
    // oADMForTags.modifiedTags = oADMForTags.modified||[];

    let oADMForDrillDowns = _generateADMForDrillDowns(oOldKPI.drillDowns, oNewKPI.drillDowns);
    oADM.addedDrillDowns = oADMForDrillDowns.added;
    oADM.modifiedDrillDowns = oADMForDrillDowns.modified;
    oADM.deletedDrillDowns = oADMForDrillDowns.deleted;

    oADM.physicalCatalogIds = oNewKPI.physicalCatalogIds;
    oADM.portalIds = oNewKPI.portalIds;

    if (oADMForDrillDowns.drillDownsTagNotSelected) {
      oADM.drillDownsTagNotSelected = true;
    }

    _generateADMForRuleBlocks(oADM, oCallBack);
    return oADM;
  };

  var _saveActiveDetailKPI = function (oCallBack) {
    let oActiveKPI = KpiProps.getActiveKPI();
    let oClonedKPI = oActiveKPI.clonedObject;
    if(!oClonedKPI.label) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    if (CS.isEmpty(oActiveKPI.clonedObject)) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }
    let oADM = _generatedADMToSave(oActiveKPI, oCallBack);
    if (!oADM.stopSave && !oADM.drillDownsTagNotSelected) {
      SettingUtils.csPostRequest(oKpiRequestMapping.SaveKPI, {}, oADM, successSaveKPIDetails.bind(this, oCallBack), failureSaveKPIDetails);
    } else if (oADM.drillDownsTagNotSelected) {
      alertify.message(getTranslation().DRILL_DOWN_TAG_NOT_SELECTED);
    } else if (oADM.stopSave)  {
      alertify.error(getTranslation().INVALID_KPI_CONFIGURATION);
    }
  };

  let successSaveKPIDetails = function (oCallBack, oResponse) {
    // Update Grid Data after save call
    let oSuccess = oResponse.success;
    let oActiveKPI = oSuccess.keyPerformanceIndex;
    let oKpiGridData = KpiProps.getKpiGridData();
    let aKpiList = SettingUtils.getAppData().getKpiList();
    let oKpiId = oActiveKPI.id;
    CS.assign(aKpiList[oKpiId], oActiveKPI);
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.KPI, aKpiList);
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.KPI);
    oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);

    KpiProps.setActiveKPI(oSuccess.keyPerformanceIndex);
    KpiProps.setReferencedData(oSuccess);
    KpiProps.setSelectedTagMap({});
    _handleKPIConfigDialogTabChanged(KpiProps.getActiveTabId());

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().KPI})); // todo: add translations
    _triggerChange();
  };

  let _addRuleIntoRuleBlock = function (sRuleType, sRuleBlockId) {
    let oRuleBlock = _makeActiveDetailKPIDirty();
    let oDummyRule = _createDummyRule(sRuleType, sRuleBlockId);
    oRuleBlock.rules.push(oDummyRule);
    _triggerChange();
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

  var _getRoleObjectToPushIntoFilterProps = function (sAttributeId) {

    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: "exact",
      values : [],
      to: 0,
      from: 0,
      /*defaultUnit: "",*/
      ruleListLinkId: ""
    }
  };

  let _getRuleFromRuleBlock = function (sRuleId) {
    var oActiveBlock = _makeActiveDetailKPIDirty();
    let oActiveRule = CS.find(oActiveBlock.rules, {id: sRuleId});
    return oActiveRule;
  };

  var _getAttributeObjectToPushIntoFilterProps = function (sAttributeId) {
    var oAttributeMap = SettingUtils.getAppData().getAttributeList();
    var oMasterAttribute = oAttributeMap[sAttributeId];

    var sDefaultUnit = oMasterAttribute.defaultUnit || "";
    var defaultValue = [];
    var sType = oMasterAttribute.type;
    var sVisualType = SettingUtils.getAttributeTypeForVisual(sType, sAttributeId);
    if(sVisualType == "number" || sVisualType == "measurementMetrics"){
      defaultValue = ["0"];
    }

    var sAttributeRuleType = "contains";

    if(sVisualType == "measurementMetrics"){
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
      defaultUnit: sDefaultUnit,
      ruleListLinkId: "",
      attributeLinkId: null
    }
  };

  var _getTagObjectToPushIntoFilterProps = function (sAttributeId,sType, oTagRuleValue) {

    var aTagValues = [];

    var oCurrentTag = SettingUtils.getLoadedTagsData()[sAttributeId] || {};

    if (sType != "notempty" && sType != "empty") {
      CS.forEach(oCurrentTag.children, function (oTagValues) {
        var oTempTagValueObject = {};
        oTempTagValueObject.id = oTagValues.id;
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

  var _getRelationshipObjectToPushIntoFilterProps = function (sAttributeId) {
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: "max",
      values: [],
      to: 0,
      from: 0,
      /*defaultUnit: ""*/
    }
  };

  var _handleRoleAdded = function (sAttributeId, sAttributeContext, sRuleId) {
    let oActiveRule = _getRuleFromRuleBlock(sRuleId);
    var aRoles = [];
    aRoles = oActiveRule.roles;
    var oRuleAttribute = _getRuleCauses(aRoles, oActiveRule.id, sAttributeId);
    var oAttributeRule = _getRoleObjectToPushIntoFilterProps(sAttributeId);
    oRuleAttribute.rules.push(oAttributeRule);

    // Commit Test
  };

  var _handleAttributeAdded = function (sAttributeId, sAttributeContext, sRuleId, oAttributeFromServer) {
    let oActiveRule = _getRuleFromRuleBlock(sRuleId);
    var aAttributes = [];
    aAttributes = oActiveRule.attributes;
    var oRuleAttribute = _getRuleCauses(aAttributes, oActiveRule.id, sAttributeId);
    var oAttributeRule = _getAttributeObjectToPushIntoFilterProps(sAttributeId);
    oRuleAttribute.rules.push(oAttributeRule);
    let oReferencedAttributes = KpiProps.getReferencedAttributes();
    oReferencedAttributes[sAttributeId] = oAttributeFromServer;
    let oReferencedData = KpiProps.getReferencedData();
    oReferencedData.referencedAttributes[sAttributeId] = oAttributeFromServer;
    oActiveRule.configDetails.referencedAttributes[sAttributeId] = oAttributeFromServer;
  };

  var _handleTagAdded = function (sTagId, sTagContext, sRuleId) {
    let oActiveRule = _getRuleFromRuleBlock(sRuleId);
    let aTags = [];
    aTags = oActiveRule.tags;
    var oRuleTag = _getRuleCauses(aTags, oActiveRule.id, sTagId);
    var oAttributeRule = _getTagObjectToPushIntoFilterProps(sTagId, "exact");
    oRuleTag.rules.push(oAttributeRule);

  };

  var _handleRuleAttributeValueChanged = function (sElementId, sValueId, aValueList, sContext, bIsDelete, sRuleId) {
    let oActiveRule = _getRuleFromRuleBlock(sRuleId);
    if (sContext == "attribute") {
      var oAttribute = CS.find(oActiveRule.attributes, {entityId: sElementId});
      var oAttributeRuleValue = CS.find(oAttribute.rules, {id: sValueId});
      oAttributeRuleValue.values = aValueList;
      // oAttributeRuleValue.values.push(sValue);

    } else if (sContext == "relationship") {
      var oRelationship = CS.find(oActiveRule.relationships, {entityId: sElementId});
      var oRelationshipRuleValue = CS.find(oRelationship.rules, {id: sValueId});
      oRelationshipRuleValue.values = aValueList;
    }
    else if (sContext == "role") {
      var oRole = CS.find(oActiveRule.roles, {entityId: sElementId});
      var oRoleRuleValue = CS.find(oRole.rules, {id: sValueId});
      oRoleRuleValue.values = aValueList;
    }

  };

  var _handleRuleFilterInnerTagAdded = function (sTagGroupId, aTagValueRelevanceData, sRuleId, sOuterRuleId) {
    var oActiveRule = _getRuleFromRuleBlock(sOuterRuleId);
    var oTag = CS.find(oActiveRule.tags, {entityId: sTagGroupId});
    var oTagRuleValue = CS.find(oTag.rules, {id: sRuleId});

    CS.forEach(oTagRuleValue.tagValues, function (oTagRuleTagValue) {
      let oData = CS.find(aTagValueRelevanceData, {tagId: oTagRuleTagValue.id});
      SettingUtils.updateTagValueRelevanceOrRange(oTagRuleTagValue, oData, true);
    });
  };

  var _handleAttributeVisibilityButtonClicked = function (sAttrId, oAttrRuleCondition, sRuleId) {
    var oActiveRule = _getRuleFromRuleBlock(sRuleId);
    var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttrId});
    var oRule = CS.find(oAttribute.rules, {id: oAttrRuleCondition.id});
    if(!CS.isEmpty(oRule)) {
      if(CS.isEmpty(oRule.attributeLinkId)) {
        oRule.attributeLinkId = "0";
      } else {
        oRule.attributeLinkId = null;
      }
      oRule.shouldCompareWithSystemDate = false;
    }
    _triggerChange();
  };

  var _handleRuleElementDeleteButtonClicked = function (sElementId, sContext, sHandlerContext, sRuleId) {
    var oActiveRule = _getRuleFromRuleBlock(sRuleId);
    if (sContext == "attribute") {
      CS.remove(oActiveRule.attributes, {entityId: sElementId});
    }
    else if (sContext == "tag") {
      CS.remove(oActiveRule.tags, {entityId: sElementId});
    } else if (sContext == "relationship") {
      CS.remove(oActiveRule.relationships, {entityId: sElementId});
    } else if (sContext == "role") {
      CS.remove(oActiveRule.roles, {entityId: sElementId});
    } else if (sContext == "type") {
      CS.remove(oActiveRule.types, {entityId: sElementId});
    }
  };

  var _handleRuleAttributeValueDeleteClicked = function (sAttributeId, sValueId,sContext, sRuleId) {
    var oActiveRule = _getRuleFromRuleBlock(sRuleId);
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

  var _handleRuleAddAttributeClicked = function (sElementId, sContext, sRoleId) {
    var oActiveRule = _getRuleFromRuleBlock(sRoleId);
    if (sContext == "attribute") {
      var oAttribute = CS.find(oActiveRule.attributes, {entityId: sElementId});
      var oAttributeRule = _getAttributeObjectToPushIntoFilterProps(sElementId);
      oAttribute.rules.push(oAttributeRule);

    } else if (sContext == "relationship") {
      var oRelationship = CS.find(oActiveRule.relationships, {entityId: sElementId});

      var oRelationshipRule = _getRelationshipObjectToPushIntoFilterProps(sElementId);
      oRelationship.rules.push(oRelationshipRule);
    }
    else if (sContext == "role") {
      var oRole = CS.find(oActiveRule.roles, {entityId: sElementId});

      var oRoleRule = _getRoleObjectToPushIntoFilterProps(sElementId);
      oRole.rules.push(oRoleRule);
    } else if (sContext == "tag") {
      var oTag = CS.find(oActiveRule.tags, {entityId: sElementId});

      var oTagRule = _getTagObjectToPushIntoFilterProps(sElementId, "exact");
      oTag.rules.push(oTagRule);
    }
  };

  var _handleAttributeViewTypeChanged = function (sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId, sRuleId, oReferencedAttributes){
    var oActiveRule = _getRuleFromRuleBlock(sRuleId);
    var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttrId});
    var oAttributeRule = CS.find(oAttribute.rules, {id: oAttributeCondition.id});
    oAttributeRule.attributeLinkId = sSelectedAttrId;
    oAttributeRule.values = [];
    let oReferencedData = KpiProps.getReferencedData();
    CS.assign(oReferencedData.referencedAttributes, oReferencedAttributes);
    _triggerChange();
  };

  var _handleRuleAttributeValueTypeChanged = function (sAttributeId, sValueId, sTypeId,sContext, sRuleId) {
    var oActiveRule = _getRuleFromRuleBlock(sRuleId);
    var bRemoveAttributeLink = (sTypeId == "empty" || sTypeId == "notempty" || sTypeId == "range"
        || sTypeId == "length_range");

    if (sContext == "attribute") {
      var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttributeId});
      var oAttributeRuleValue = CS.find(oAttribute.rules, {id: sValueId});
      oAttributeRuleValue.type = sTypeId;
      oAttributeRuleValue.ruleListLinkId = "";
      oAttributeRuleValue.klassLinkIds = [];
      oAttributeRuleValue.values = [];

      bRemoveAttributeLink && (oAttributeRuleValue.attributeLinkId = "");
      bRemoveAttributeLink && (oAttributeRuleValue.shouldCompareWithSystemDate = false);

      var oAttributeMap = KpiProps.getReferencedAttributes();
      var oMasterAttribute = oAttributeMap[sAttributeId];
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
    else if(sContext == "type"){
      var oType = CS.find(oActiveRule.types, {entityId: sAttributeId});
      var oTypeRuleValue = CS.find(oType.rules, {id: sValueId});
      oTypeRuleValue.type = sTypeId;
    }
    else if(sContext == "tag"){
      var oTag = CS.find(oActiveRule.tags, {entityId: sAttributeId});
      var iIndexOfTagRule =CS.findIndex(oTag.rules,{id : sValueId});
      var oTagRuleValue = oTag.rules[iIndexOfTagRule];

      var oNewTagRuleValue = _getTagObjectToPushIntoFilterProps(sAttributeId,sTypeId, oTagRuleValue);
      /*CS.remove(oTag.rules, {id: sValueId});
      oTag.rules.push(oNewTagRuleValue);*/
      oTag.rules.splice(iIndexOfTagRule,1,oNewTagRuleValue);
    }
  };

  let failureSaveKPIDetails = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveKPIDetails", getTranslation());
  };

  var _discardActiveDetailKPI = function (oCallBack) {
    let oReferencedData = KpiProps.getReferencedData();
    let oActiveBlock = KpiProps.getActiveBlock();
    let oActiveKPI = KpiProps.getActiveKPI();
    KpiProps.setSelectedTagMap({});
    delete oActiveKPI.clonedObject;
    delete oActiveKPI.isDirty;
    let oReferencedRules = oReferencedData.referencedRules;
    CS.forEach(oReferencedRules, function (oReferencedRule) {
      delete oReferencedRule.clonedObject
    });
    alertify.success(getTranslation().KPI_DISCARD_SUCCESSFUL);
    _triggerChange();
  };

  let _closeKPIDialog = function () {
      KpiProps.setIsKPIDialogActive(false);
      KpiProps.setSelectedDrillDownTags([]);
      _triggerChange();
  };

  let _handleKPIConfigDialogTabChanged = function (sTabId) {
    let oReferencedData = KpiProps.getReferencedData();
    let oActiveKpi = _getActiveBlock();
    let isDrillDownsTagNotSelected = false;
    CS.forEach(oActiveKpi.drillDowns, function (oAddedDrillDown) {
      if (oAddedDrillDown.type == "tag" && CS.isEmpty(oAddedDrillDown.typeIds)) {
        isDrillDownsTagNotSelected = true;
        return false;
      }
    });

    if(isDrillDownsTagNotSelected) {
      alertify.message(getTranslation().DRILL_DOWN_TAG_NOT_SELECTED);
      return;
    }

    if (sTabId == KpiConfigDictionary.KPI_INFORMATION) {
      let oActiveKpi = KpiProps.getActiveKPI();
      KpiProps.setActiveBlock(oActiveKpi);
    } else {
      let oReferencedRules = oReferencedData.referencedRules;
      let oActiveRuleBlock = CS.find(oReferencedRules, {type: sTabId}) || {};
      KpiProps.setActiveBlock(oActiveRuleBlock);
    }

    KpiProps.setActiveTabId(sTabId);
    _triggerChange();
  };

  let _fetchKPIDetails = function (sId) {
    let oParameters = {};
    oParameters.id = sId;
    SettingUtils.csGetRequest(oKpiRequestMapping.GetKPI, oParameters, successFetchKPIDetails, failureFetchKPIDetails);
  };

  let successFetchKPIDetails = function (oResponse) {
    let oSuccess = oResponse.success;
    KpiProps.setActiveKPI(oSuccess.keyPerformanceIndex);
    let oReferencedRules = oSuccess.referencedRules;
    KpiProps.setReferencedData(oSuccess);
    KpiProps.setReferencedRules(oSuccess.referencedRules);
    KpiProps.setReferencedDashboardTabs(oSuccess.referencedDashboardTabs);
    KpiProps.setReferencedAttributes(oSuccess.referencedAttributes);
    KpiProps.setReferencedTags(oSuccess.referencedTags);
    KpiProps.setIsKPIDialogActive(true);
    _handleKPIConfigDialogTabChanged(KpiConfigDictionary.KPI_INFORMATION);

    let aSelectedDrillDownTags = [];
    let aDrillDowns = oSuccess.keyPerformanceIndex.drillDowns;
    CS.forEach(aDrillDowns, function (oDrillDown) {
      if (oDrillDown.type == 'tag') {
        aSelectedDrillDownTags.push(oDrillDown.typeIds[0]);
      }
    });

    KpiProps.setSelectedDrillDownTags(aSelectedDrillDownTags);
    _triggerChange();
  };

  let failureFetchKPIDetails = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchKPIDetails", getTranslation());
  };

  var _fetchKpiListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.KPI);
    SettingUtils.csPostRequest(oKpiRequestMapping.GetAllKpi, {}, oPostData, successFetchKpiListForGridView, failureFetchKpiListForGridView);

  };

  let _deleteKpis = function (aSelectedIdsToDelete) {

    if (!CS.isEmpty(aSelectedIdsToDelete)) {
      return SettingUtils.csDeleteRequest(oKpiRequestMapping.BulkDelete, {}, {ids: aSelectedIdsToDelete}, successDeleteKpiCallback, failureDeleteKpiCallback);
    } else {
      // _setKpiScreenLockStatus(false);
      alertify.success(getTranslation().KPI_DELETE_SUCCESSFUL);
      _triggerChange();
    }
  };

  let handleDeleteKpiFailure = function (aFailureIds) {
    var aKpisAlreadyDeleted = [];
    var aUnhandleKpi = [];
    var aKpiGridData = KpiProps.getKpiGridData();
    CS.forEach(aFailureIds, function (oItem) {
      var oKpi = CS.find(aKpiGridData, {id: oItem.itemId});
      if (oItem.key == "KpiNotFoundException") {  // TODO: Handle Exception ??
        aKpisAlreadyDeleted.push(oKpi.label);
      } else {
        aUnhandleKpi.push(oKpi.label);
      }
    });

    // TODO: handle the failure part
    if (aKpisAlreadyDeleted.length > 0) {
      var sKpiAlreadyDeleted = aKpisAlreadyDeleted.join(',');
      alertify.error(Exception.getCustomMessage("Kpi_already_deleted", getTranslation(), sKpiAlreadyDeleted), 0);
    }
    if (aUnhandleKpi.length > 0) {
      var sUnhandledKpi = aUnhandleKpi.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_Kpi", getTranslation(), sUnhandledKpi), 0);
    }
  };

  let successDeleteKpiCallback = function (oResponse) {
    let aSuccessIds = oResponse.success;
    let aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.KPI);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oMasterKpisMap = SettingUtils.getAppData().getKpiList();
    let oSkeleton = oGridViewPropsByContext.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      delete oMasterKpisMap[sId];
      CS.remove(oSkeleton.selectedContentIds, function (oSelectedId) {
        return oSelectedId == sId;
      });
    });
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - aSuccessIds.length);
    alertify.success(getTranslation().KPI_DELETE_SUCCESSFUL);
    if (aFailureIds && aFailureIds.length > 0) {
      handleDeleteKpiFailure(aFailureIds);
    }
  };

  let failureDeleteKpiCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var oMasterKpiList = SettingUtils.getAppData().getKpiList();
      var oKpiValueList = KpiProps.getKpiValueList();
      handleDeleteKpiFailure(oResponse.failure.exceptionDetails, oMasterKpiList, oKpiValueList);
    } else {
      SettingUtils.failureCallback(oResponse, "faliureDeleteKpiCallback", getTranslation());
    }
    _triggerChange();
  };

  let successFetchKpiListForGridView = function (oResponse) {
    SettingUtils.getAppData().setKpiData(oResponse.success);
    KpiProps.setReferencedDashboardTabs(oResponse.success.configDetails.referencedDashboardTabs);
    let aKpiList = oResponse.success.kpiList;
    SettingUtils.getAppData().setKpiList(aKpiList);
    KpiProps.setKpiGridData(aKpiList);
    GridViewStore.preProcessDataForGridView(GridViewContexts.KPI, aKpiList,oResponse.success.count);
    _triggerChange();
  };

  var failureFetchKpiListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchKpiListForGridView", getTranslation());
  };

  var _setUpKpiConfigGridView = function () {
    let oKpiConfigGridViewSkeleton = new KpiConfigGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.KPI, {skeleton: oKpiConfigGridViewSkeleton});
    _fetchKpiListForGridView();
  };

  let _handleKPIDetailSingleValueChanged = function (sRuleId, sKey, sValue, oReferencedData) {
    let oActiveBlock = _makeActiveDetailKPIDirty();
    if (sKey == 'unit') {
      // if (sValue == KpiConfigDictionary.PERCENTAGE) {
      let oThreshold = oActiveBlock.threshold;
      if ((+oThreshold.upper < +oThreshold.lower) ||
          (sValue == KpiConfigDictionary.PERCENTAGE && +oThreshold.upper > 100) ||
          (sValue == KpiConfigDictionary.PERCENTAGE && +oThreshold.lower > 100)) {
        oThreshold.upper = 100;
        oThreshold.lower = 0;
      }
      // }
      oActiveBlock[sKey] = sValue;
      return ;
    }
    else if (sKey == 'task' || sKey == 'frequency' || sKey == 'color') {
      oActiveBlock[sKey] = sValue;
      return ;
    } else if (sKey == "dashboardTab") {
      let oReferencedTabs = KpiProps.getReferencedDashboardTabs();
      CS.assign(oReferencedTabs, oReferencedData);
      oActiveBlock["dashboardTabId"] = sValue;
      return;
    }
    let oRule = sRuleId ? CS.find(oActiveBlock.rules, {id: sRuleId}): oActiveBlock;
    oRule[sKey] = sValue;
  };

  let _handleKPIConfigSingleValueChanged = function (sKey, sValue) {
    var oActiveKpi = _makeActiveKPIDirty();
    oActiveKpi[sKey] = sValue;
  };

  let _updateKPIReferencedData = function (sKey, oReferencedData) {
    let oKPIReferencedData = KpiProps.getReferencedData();

    switch (sKey) {
      case "klassIds":
        CS.assign(oKPIReferencedData.referencedKlasses, oReferencedData);
        break;

      case "endpoints":
        CS.assign(oKPIReferencedData.referencedEndpoints, oReferencedData);
        break;
    }
  };

  let _handleKPIDetailMultipleValueChanged = function (sKey, sInnerKey, aValues, oReferencedData) {
    let oActiveKPI = _makeActiveDetailKPIDirty();

    if (!CS.isEmpty(sInnerKey)) {
      oActiveKPI[sKey][sInnerKey] = aValues;
      _updateKPIReferencedData(sInnerKey, oReferencedData);
    } else {
      oActiveKPI[sKey] = aValues;
      _updateKPIReferencedData(sKey, oReferencedData);
    }
    _triggerChange();
  };

  let _handleMSSCrossIconClicked = function (sKey, sInnerKey, sId) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    if (!CS.isEmpty(sInnerKey)) {
      CS.pull(oActiveKPI[sKey][sInnerKey], sId);
    } else {
      if(CS.isArray(oActiveKPI[sKey])) {
        CS.pull(oActiveKPI[sKey], sId);
      } else {
        oActiveKPI[sKey] = null; // todo: if some issue from backend change to empty string ("")
      }
    }
    _triggerChange();
  };

  let _handleSelectionToggleButtonClicked = function (sKey, sId) {
    let oActiveKPI = _makeActiveKPIDirty();
    if (sKey === "physicalCatalogId") {
      let aPhysicalCatalogs = oActiveKPI.physicalCatalogIds || [];
      let aRemovedCatalogIds = CS.remove(aPhysicalCatalogs, function (sCatalogId) {
        return sCatalogId == sId
      });
      if (CS.isEmpty(aRemovedCatalogIds)) {
        aPhysicalCatalogs.push(sId);
      }
      oActiveKPI.physicalCatalogIds = aPhysicalCatalogs;
    } else if (sKey === "portalId") {
      let aPortals = oActiveKPI.portalIds || [];
      let aRemovedPortalIds = CS.remove(aPortals, function (sPortalId) {
        return sPortalId == sId
      });
      if (CS.isEmpty(aRemovedPortalIds)) {
        aPortals.push(sId);
      }
      oActiveKPI.portalIds = aPortals;
    }
  };

  let _fetchTagById = function (sTagId, oCallbackData) {
    let oParameters = {};
    oParameters.id = sTagId;
    oParameters.mode = "all";

    SettingUtils.csGetRequest(SettingsRequestMapping.GetTag, oParameters, successFetchTagById.bind(this, oCallbackData),
        failureFetchTagById);
  };

  let failureFetchTagById = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTagById", getTranslation());
  };

  let successFetchTagById = function (oCallbackData, oResponse) {
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute(oResponse.success);
    }
    _triggerChange();
  };

  let _addTagGroupInActiveKPI = function (oTag) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    let aKPITags = oActiveKPI.kpiTags || [];
    let sTagId = oTag.id;
    let aTagValueIds = [];
    let oSelectedTags = KpiProps.getSelectedTagMap();
    oSelectedTags[oTag.id] = oTag;
    CS.forEach(oTag.children, function (oTagValue) {
      aTagValueIds.push(oTagValue.id);
    });
    if (!CS.find(aKPITags, {tagId: sTagId})) {
      aKPITags.push({
        tagId: sTagId,
        tagValues: aTagValueIds
      });
    }
  };

  let _handleKpiConfigDialogTagGroupSelected = function (sTagId) {
    let oCallbackData = {};
    oCallbackData.functionToExecute = _addTagGroupInActiveKPI;
    _fetchTagById(sTagId, oCallbackData);
  };

  let _updateKPITagValuesSelection = function (sTagGroupId, aCheckedItems) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    let oTag = CS.find(oActiveKPI.kpiTags, {tagId: sTagGroupId});
    oTag.tagValues = aCheckedItems;
  };

  let _handleRemoveSelectedTagGroupClicked = function (sTagGroupId) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    let oSelectedTags = KpiProps.getSelectedTagMap();
    delete  oSelectedTags[sTagGroupId];
    CS.remove(oActiveKPI.kpiTags, function (oTagGroup) {
      return oTagGroup.tagId == sTagGroupId;
    });
  };

  let _createKpi = function () {
    var oUntitledKpiMaster = {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      frequency: 'daily',
      code: "",
      isCreated: true
    };
    KpiProps.setActiveKPI(oUntitledKpiMaster);
    _triggerChange();
  };

  var _createKpiCall = function (oUntitledKpiMaster) {
    var oCallbackData = {
      functionToExecute: _triggerChange
    };
    SettingUtils.csPutRequest(oKpiRequestMapping.CreateKpi, {}, oUntitledKpiMaster, successCreateKpiCallback.bind(this, oCallbackData), failureCreateKpiCallback.bind(this, oCallbackData));
  };

  let successCreateKpiCallback = function (oCallbackData, oResponse) {
    let oSavedKpi = oResponse.success.keyPerformanceIndex;
    KpiProps.setReferencedDashboardTabs(oResponse.success.referencedDashboardTabs);
    let aKpiGridData = KpiProps.getKpiGridData();
    aKpiGridData.push(oSavedKpi);
    let oProcessedKpi = GridViewStore.getProcessedGridViewData(GridViewContexts.KPI, [oSavedKpi])[0];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.KPI);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedKpi);

    let oMasterKpiMap = SettingUtils.getAppData().getKpiList(); // get master attribute list
    oMasterKpiMap[oSavedKpi.id] = oSavedKpi;

    KpiProps.setReferencedData(oResponse.success);
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1); //increase total count when new attribute created
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    alertify.success(getTranslation().KPI_CREATED_SUCCESSFULLY);

    /** To open edit view immediately after create */
    successFetchKPIDetails(oResponse);
  };

  let failureCreateKpiCallback = function (oCallbackData, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      let sKpi = checkAndDeleteKpiFromList(oResponse);
      alertify.error("[" + sKpi +"] "+getTranslation().ERROR_ALREADY_DELETED, 0);
    } else {
      SettingUtils.failureCallback(oResponse, "failureCreateKpiCallback", getTranslation());
    }

    _triggerChange();
  };

  // TODO: Re-Write for KPI
  let checkAndDeleteKpiFromList = function (oResponse, sKpiId) {
    let oMasterKpi = SettingUtils.getAppData().getKpiList();
    let oKpiValues = KpiProps.getKpiValueList();
    let oSelectedKpi = KpiProps.getSelectedKpi();

    if(oResponse.success.message.indexOf('NOT_FOUND') >= 0 && (oSelectedKpi || sKpiId)){
      var sKpiName = oMasterKpi[sKpiId || oSelectedKpi.id].label;
      delete oKpiValues[sKpiId || oSelectedKpi.id];
      delete oMasterKpi[sKpiId || oSelectedKpi.id];
      KpiProps.setSelectedKpi({});
      return sKpiName;
    }

    return null;
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


    let oTaxonomyPaginationData = KpiProps.getTaxonomyPaginationData();
    let oPostData = CS.assign({taxonomyId: sTaxonomyId}, oTaxonomyPaginationData);

    SettingUtils.csPostRequest(oKpiRequestMapping.GetTaxonomy, {}, oPostData, successFetchFetchTaxonomy, failureFetchClassListCallback);
  };

  let successFetchFetchTaxonomy = function (oResponse) {
    let oSuccess = oResponse.success;
    let aTaxonomyListFromServer = oSuccess.list;
    let oConfigDetails = oSuccess.configDetails;
    let aTaxonomyList = KpiProps.getAllowedTaxonomies();

    KpiProps.setAllowedTaxonomies(CS.concat(aTaxonomyList, aTaxonomyListFromServer));
    let oAllowedTaxonomyHierarchyListData = CommonUtils.prepareTaxonomyHierarchyData(KpiProps.getAllowedTaxonomies(), oConfigDetails);
    KpiProps.setAllowedTaxonomyHierarchyList(oAllowedTaxonomyHierarchyListData);
    _triggerChange();
  };

  let failureFetchClassListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchClassListCallback", getTranslation());
  };

  var _handleTaxonomyAdded = function (sTaxonomyId,sParentTaxonomyId) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    CS.pull(oActiveKPI.targetFilters.taxonomyIds,sParentTaxonomyId);
    oActiveKPI.targetFilters.taxonomyIds.push(sTaxonomyId);
    let oCallback = {};
    oCallback.retainDialog = true;
    _saveActiveDetailKPI(oCallback);
    _triggerChange();
  };

  let _handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oTaxonomy, sActiveTaxonomyId) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    let aTaxonomyIds = oActiveKPI.targetFilters.taxonomyIds;
    let iActiveTaxonomyIndex = aTaxonomyIds.indexOf(sActiveTaxonomyId);

    if (oTaxonomy.parent.id == Constants.TREE_ROOT_ID) {
      // aTaxonomyIds.splice(iActiveTaxonomyIndex, 1);
      CS.remove(aTaxonomyIds, function (sTaxonomyId) {
        return sTaxonomyId == sActiveTaxonomyId;
      })
    } else {
      aTaxonomyIds[iActiveTaxonomyIndex] = oTaxonomy.parent.id;
    }

    _saveActiveDetailKPI({retainDialog: true});
  };

  let _createNewDrilldownLevel = function (sLevelType) {
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: sLevelType,
      typeIds: []
    }
  };

  let _addNewKPIDrilldownLevel = function (sLevelType) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    var oNewDrilldownLevel = _createNewDrilldownLevel(sLevelType);
    oActiveKPI.drillDowns.push(oNewDrilldownLevel);
  };

  let _removeKPIDrilldownLevel = function (sDrillDownLevelId) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    let oDrillDownLevelToRemove = CS.find(oActiveKPI.drillDowns, {id: sDrillDownLevelId});
    let aTagsToRemove = oDrillDownLevelToRemove.typeIds;
    let aSelectedDrillDownTags = KpiProps.getSelectedDrillDownTags();

    //To update selected drill down tags after remove of levels
    CS.remove(aSelectedDrillDownTags, function (sSelectedTagInDrillDownLevel) {
      return CS.includes(aTagsToRemove, sSelectedTagInDrillDownLevel);
    });

    CS.remove(oActiveKPI.drillDowns, {id: sDrillDownLevelId})
  };

  let _handleKPIDrilldownMssValueChanged = function (sDrillDownLevelId, aSelectedIds, oReferencedTags) {
    let oActiveKPI = _makeActiveDetailKPIDirty();
    let aSelectedDrillDownTags = KpiProps.getSelectedDrillDownTags();
    let oDrilldownLevel = CS.find(oActiveKPI.drillDowns, {id: sDrillDownLevelId});
    if(CS.isEmpty(aSelectedIds)) {
      CS.remove(aSelectedDrillDownTags, function (sTagGrpId) {
        return sTagGrpId == oDrilldownLevel.typeIds[0];
      });
    }
    else {
      aSelectedDrillDownTags.push(aSelectedIds[0]);
    }

    oDrilldownLevel.typeIds = aSelectedIds;
    KpiProps.setReferencedTags(oReferencedTags);
  };

  var _getDefaultRuleCauses = function (sEntityId) {
    let oRuleAttribute = {
      id: UniqueIdentifierGenerator.generateUUID(),
      entityId: sEntityId,
      rules: []
    };
    return oRuleAttribute;
  };

  let _handleKPIPartnerApplyClicked = function (aSelectedList, oReferencedData) {
    let oActiveRuleBlock = _makeActiveDetailKPIDirty();
    oActiveRuleBlock.organizations = aSelectedList;
    let oKPIReferencedData = KpiProps.getReferencedData();
    CS.assign(oKPIReferencedData.referencedOraganizations, oReferencedData);
  };

  let _handleKPIRuleMSSValueChanged = function (sRuleId, sKey, aSelectedItems) {
    try {
      // let oActiveKPI = _makeActiveDetailKPIDirty();
      let oActiveRuleBlock = _makeActiveDetailKPIDirty();//_getActiveRuleBlock();
      let oRule = CS.find(oActiveRuleBlock.rules, {id: sRuleId});
      let aEntityRules = [];
      // oRule.isDirty = true;

      CS.forEach(aSelectedItems, function (sEntityId) {
        aEntityRules.push(_getDefaultRuleCauses(sEntityId));
      });

      oRule[sKey] = aEntityRules;
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }

  };

  let _handleKPIAddRuleClicked = function (sRuleBlockType, sRuleBlockId) {
    _addRuleIntoRuleBlock(sRuleBlockType, sRuleBlockId);
  };

  let _handleKPIThresholdValueChanged = function (sActiveBlockId, sThresholdType, sUnitType, sThresholdValue) {
    let oActiveBlock = _makeActiveDetailKPIDirty();
    let oThreshold = oActiveBlock.threshold;
    // todo: confirm about threshol
    if (+sThresholdValue < 0 ||
        (sUnitType == KpiConfigDictionary.PERCENTAGE && +sThresholdValue > 100) ||
        (sThresholdType == 'lower' && (sThresholdValue >= oThreshold.upper)) ||
        (sThresholdType == 'upper' && (sThresholdValue <= oThreshold.lower))) {
      if (sThresholdType == 'lower') {
        oThreshold.lower = 0;
      } else {
        oThreshold.upper = 100;
      }
      alertify.message(getTranslation().INVALID_THRESHOLD_CONFIGURATION);
      return;
    }

    oActiveBlock.threshold = oActiveBlock.threshold || {upper: 100, lower: 0};
    oActiveBlock.threshold[sThresholdType] = +sThresholdValue;
  };

  let _handleDeleteRuleClicked = function (sRuleId) {
    let oActiveBlock = _makeActiveDetailKPIDirty();
    CS.remove(oActiveBlock.rules, {id: sRuleId});
  };

  let  _handleYesNoViewToggled = function (sSectionId, sElementId, bValue, sRuleId) {
    var oActiveRule = _getRuleFromRuleBlock(sRuleId);
    var oTag = CS.find(oActiveRule.tags, {entityId: sSectionId});
    var oTagRuleValue = CS.find(oTag.rules, {id: sElementId});
    oTagRuleValue.tagValues[0].from = bValue ? 100: -100;
    oTagRuleValue.tagValues[0].to = bValue ? 100: -100;
    _triggerChange();
  };

  let _handleRuleAttributeRangeValueChanged = function (sAttributeId, sValueId, sValue, sRange, sRuleId) {
    let oActiveBlock = _makeActiveDetailKPIDirty();
    let oActiveRule = CS.find(oActiveBlock.rules, {id: sRuleId});
    let oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttributeId});
    let oRuleValue = CS.find(oAttribute.rules, {id: sValueId});
    oRuleValue[sRange] = sValue;
  };

  let _handleCompareWithSystemDateButtonClicked = function (sRuleId, sAttrId, oAttrCondition) {
    let oActiveRuleBlock = _makeActiveDetailKPIDirty();
    var oActiveRule = CS.find(oActiveRuleBlock.rules, {id: sRuleId});
    var oAttribute = CS.find(oActiveRule.attributes, {entityId: sAttrId});
    var oRule = CS.find(oAttribute.rules, {id: oAttrCondition.id});
    if (!CS.isEmpty(oRule)) {
      !CS.isEmpty(oRule.attributeLinkId) && (oRule.attributeLinkId = null);
      oRule.shouldCompareWithSystemDate = !oRule.shouldCompareWithSystemDate;
    }
  };

  let _createKpiDialogClick = function () {
    var oActiveKpi = KpiProps.getActiveKPI();
    oActiveKpi = oActiveKpi.clonedObject || oActiveKpi;
    var oCodeToVerifyUniqueness = {
      id: oActiveKpi.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_KPI
    };

    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = function(){
      delete oActiveKpi.isCreated;
      _createKpiCall(oActiveKpi);
    };
    var sURL = oKpiRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  let _cancelKpiDialogClicked = function () {
    KpiProps.setActiveKPI({});
    _triggerChange();
  };

  let _handleKpiConfigDialogButtonClick = function (sButtonId) {
    if (sButtonId == "create") {
      _createKpiDialogClick();
    } else {
      _cancelKpiDialogClicked();
    }
  };

  let _updateViewProps = function (sEntityType, oUpdatedMssProps) {
    switch (sEntityType) {
      case "attributes":
        KpiProps.setReferencedAttributes(oUpdatedMssProps);
        break;
      case "tags":
        KpiProps.setReferencedTags(oUpdatedMssProps);
        break;
    }
  };

  let _postProcessKpiListAndSave = function (oCallbackData) {
    let aKpiGridData = KpiProps.getKpiGridData();
    let aKpiListToSave = [];

    let bSafeToSave = GridViewStore.processGridDataToSave(aKpiListToSave, GridViewContexts.KPI, aKpiGridData);
    if (bSafeToSave) {
      return _saveKpiInBulk(aKpiListToSave, oCallbackData);
    }
    return CommonUtils.rejectEmptyPromise();
  };

  let _saveKpiInBulk = function (aKpiListToSave, oCallbackData) {
      return SettingUtils.csPostRequest(oKpiRequestMapping.SaveBulkKPI, {}, aKpiListToSave, successSaveKpiInBulk.bind(this, oCallbackData), failureSaveKpiInBulk);
  };

  let successSaveKpiInBulk = function (oCallbackData, oResponse) {
    let aKpiList = oResponse.success.kpiRuleList;
    var aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.KPI, aKpiList);
    var aKpiGridData = KpiProps.getKpiGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.KPI);
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    var oMasterKpiMap = SettingUtils.getAppData().getKpiList();

    CS.forEach(aKpiList, function (oKpi) {
      var sKpiId = oKpi.id;
      var iIndex = CS.findIndex(aKpiGridData, {id: sKpiId});
      aKpiGridData[iIndex] = oKpi;
      oMasterKpiMap[sKpiId] = oKpi;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedKpi) {
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedKpi.id});
      aGridViewData[iIndex] = oProcessedKpi;
    });

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().KPI}));
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  let failureSaveKpiInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveKpiInBulk", getTranslation());
  };

  var _discardKpiGridViewChanges = function (oCallbackData) {
    var aKpiGridData = KpiProps.getKpiGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.KPI);
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    var bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedKpi, iIndex) {
      if (oOldProcessedKpi.isDirty) {
        var oEvent = CS.find(aKpiGridData, {id: oOldProcessedKpi.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.KPI, [oEvent])[0];
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

  /************************************** PUBLIC API's **************************************/

  return {

    setUpKpiConfigGridView: function () {
      _setUpKpiConfigGridView();
    },

    editButtonClicked: function (sEntityId) {
      _fetchKPIDetails(sEntityId);
    },

    handleKPIConfigDialogButtonClicked: function (sButtonId) {
      if (sButtonId == "save") {
        _saveActiveDetailKPI({});
      } else if (sButtonId == "cancel") {
        _discardActiveDetailKPI({});
      } else {
        _closeKPIDialog();
      }
    },

    handleKPIConfigDialogTabChanged: function (sTabId) {
      _handleKPIConfigDialogTabChanged(sTabId);
    },

    handleKPIDetailSingleValueChanged: function (sRuleId, sKey, sVal) {
      if(!CS.isEmpty(sRuleId)) {
        _handleKPIDetailSingleValueChanged(sRuleId, sKey, sVal);
      }
      else{
        _handleKPIConfigSingleValueChanged(sKey, sVal);
      }
      _triggerChange();
    },

    handleKPIDetailSinglePropertyValueChanged: function (sRuleId, sKey, sVal) {
      _handleKPIDetailSingleValueChanged(sRuleId, sKey, sVal);
    },

    handleKpiConfigDialogTagGroupSelected: function (sTagGroupId) {
      _handleKpiConfigDialogTagGroupSelected(sTagGroupId);
      _triggerChange();
    },

    updateKPITagValuesSelection: function (sTagGroupId, aCheckedItems) {
      _updateKPITagValuesSelection(sTagGroupId, aCheckedItems);
      _triggerChange();
    },

    handleRemoveSelectedTagGroupClicked: function (sTagGroupId) {
      _handleRemoveSelectedTagGroupClicked(sTagGroupId);
      _triggerChange();
    },

    fetchKpiListForGridView: function () {
      _fetchKpiListForGridView();
    },

    deleteKpis: function (aSelectedIdsToDelete) {
      let oMasterKpis = KpiProps.getKpiGridData();

      if (!CS.isEmpty(aSelectedIdsToDelete)) {
        let aBulkDeleteKpis = [];
        CS.forEach(aSelectedIdsToDelete, function (sKpiId) {
          let oMasterKpi = CS.find(oMasterKpis, {id: sKpiId});
          let sMasterKpiLabel = CS.getLabelOrCode(oMasterKpi);
          aBulkDeleteKpis.push(sMasterKpiLabel);
        });
        //sBulkDeleteKpis = CS.trimEnd(sBulkDeleteKpis, ', ');
        CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteKpis,
            function () {
              _deleteKpis(aSelectedIdsToDelete)
              .then(_fetchKpiListForGridView);
            }, function (oEvent) {
            });
      } else {
        alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      }
    },

    createKpi: function () {
      _createKpi();
    },

    handleKPIDetailMultipleValueChanged: function (sKey, sInnerKey, aSelectedItems, oReferencedData) {
      _handleKPIDetailMultipleValueChanged(sKey, sInnerKey, aSelectedItems, oReferencedData);
    },

    handleMSSCrossIconClicked: function (sKey, sInnerKey, sId) {
      _handleMSSCrossIconClicked(sKey, sInnerKey, sId);
    },

    handleSelectionToggleButtonClicked: function (sKey, sId) {
      _handleSelectionToggleButtonClicked(sKey, sId);
      _triggerChange();
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId) {
      KpiProps.setAllowedTaxonomies([]);
      let oTaxonomyPaginationData = KpiProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.searchText = "";
      oTaxonomyPaginationData.from = 0;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleTaxonomyAdded: function (sTaxonomyId,sParentTaxonomyId) {
      _handleTaxonomyAdded(sTaxonomyId,sParentTaxonomyId);
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sActiveTaxonomyId) {
      _handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sActiveTaxonomyId);
      _triggerChange();
    },

    handleKPIRuleMSSValueChanged: function (sRuleId, sKey, aSelectedItems, oReferencedData) {
      if (!CS.isEmpty(sRuleId)) {
        _handleKPIRuleMSSValueChanged(sRuleId, sKey, aSelectedItems);
      } else {
        _handleKPIDetailSingleValueChanged(sRuleId, sKey, aSelectedItems[0], oReferencedData);
      }
      _triggerChange();
    },

    handleKPIPartnerApplyClicked: function(aSelectedList, oReferencedData){
      _handleKPIPartnerApplyClicked(aSelectedList, oReferencedData);
      _triggerChange();
    },

    handleAddNewKPIDrilldownLevelClicked: function (sLevelType) {
      _addNewKPIDrilldownLevel(sLevelType);
      _triggerChange();
    },

    handleRemoveKPIDrilldownLevelClicked: function (sDrillDownLevelId) {
      _removeKPIDrilldownLevel(sDrillDownLevelId);
      _triggerChange();
    },

    handleKPIDrilldownMssValueChanged: function (sDrillDownLevelId, aSelectedIds, oReferencedTags) {
      _handleKPIDrilldownMssValueChanged(sDrillDownLevelId, aSelectedIds, oReferencedTags);
      _triggerChange();
    },

    handleKPIAddRuleClicked: function (sRuleBlockType, sRuleBlockId) {
      _handleKPIAddRuleClicked(sRuleBlockType, sRuleBlockId);
    },

    handleRoleAdded: function (sAttributeId, sAttributeContext, sRuleId) {
      _handleRoleAdded(sAttributeId, sAttributeContext, sRuleId);
      _triggerChange();
    },

    handleAttributeAdded: function (sAttributeId, sAttributeContext, sRuleId, oAttributeFromServer) {
      _handleAttributeAdded(sAttributeId, sAttributeContext, sRuleId, oAttributeFromServer);
      _triggerChange();
    },

    handleTagAdded: function (sTagId, sTagContext, sRuleId) {
      _handleTagAdded(sTagId, sTagContext, sRuleId);
      _triggerChange();
    },

    handleRuleAttributeValueChanged: function (sElementId, sValueId, aValueList,sContext,bIsDelete, sRuleId) {
      _handleRuleAttributeValueChanged(sElementId, sValueId, aValueList,sContext,bIsDelete, sRuleId);
      _triggerChange();
    },

    handleRuleFilterInnerTagAdded: function (sTagId, aTagValueRelevanceData, sAttributeValId, sRuleId) {
      _handleRuleFilterInnerTagAdded(sTagId, aTagValueRelevanceData, sAttributeValId, sRuleId);
    },

    handleAttributeVisibilityButtonClicked: function(sAttrId, oAttrCondition, sRuleId) {
      _handleAttributeVisibilityButtonClicked(sAttrId, oAttrCondition, sRuleId);
    },

    handleRuleElementDeleteButtonClicked: function (sElementId, sContext, sHandlerContext, sRuleId) {
      _handleRuleElementDeleteButtonClicked(sElementId, sContext, sHandlerContext, sRuleId);
      _triggerChange();
    },

    handleRuleAttributeValueDeleteClicked: function (sAttributeId, sValueId,sContext, sRuleId) {
      _handleRuleAttributeValueDeleteClicked(sAttributeId, sValueId,sContext, sRuleId);
      _triggerChange();
    },

    handleRuleAddAttributeClicked: function (sElementId, sContext, sRoleId) {
      _handleRuleAddAttributeClicked(sElementId, sContext, sRoleId);
      _triggerChange();
    },

    handleAttributeViewTypeChanged: function(sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId, sRuleId, oReferencedAttributes){
      _handleAttributeViewTypeChanged(sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId, sRuleId, oReferencedAttributes);
    },

    handleRuleAttributeValueTypeChanged: function (sAttributeId, sValueId, sTypeId,sContext, sRuleId) {
      _handleRuleAttributeValueTypeChanged(sAttributeId, sValueId, sTypeId,sContext, sRuleId);
      _triggerChange();
    },

    handleKPIThresholdValueChanged: function (sActiveBlockId, sThresholdType, sUnitType, sThresholdValue) {
      _handleKPIThresholdValueChanged(sActiveBlockId, sThresholdType, sUnitType, sThresholdValue);
      _triggerChange();
    },

    handleDeleteRuleClicked: function (sRuleId) {
      _handleDeleteRuleClicked(sRuleId);
      _triggerChange();
    },

    handleYesNoViewToggled: function (sSectionId, sElementId, bValue, sRuleId) {
      _handleYesNoViewToggled(sSectionId, sElementId, bValue, sRuleId);
    },

    handleRuleAttributeRangeValueChanged: function (sAttributeId, sValueId, sValue, sRange, sRuleId) {
      _handleRuleAttributeRangeValueChanged(sAttributeId, sValueId, sValue, sRange, sRuleId);
      _triggerChange();
    },

    handleCompareWithSystemDateButtonClicked: function (sRuleId, sAttrId, oAttrCondition) {
      _handleCompareWithSystemDateButtonClicked(sRuleId, sAttrId, oAttrCondition);
      _triggerChange()
    },

    handleKpiConfigDialogButtonClick: function (sButtonId) {
      _handleKpiConfigDialogButtonClick(sButtonId);
      _triggerChange();
    },

    handleTaxonomySearchClicked: function (sContext, sTaxonomyId, sSearchText) {
      KpiProps.setAllowedTaxonomies([]);
      let oTaxonomyPaginationData = KpiProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.from = 0;
      oTaxonomyPaginationData.searchText = sSearchText;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleTaxonomyLoadMoreClicked: function (sContext, sTaxonomyId) {
      let oTaxonomyPaginationData = KpiProps.getTaxonomyPaginationData();
      let aTaxonomyList = KpiProps.getAllowedTaxonomies();
      oTaxonomyPaginationData.from = aTaxonomyList.length;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    updateViewProps: function (sEntityType, oUpdatedMssProps) {
      _updateViewProps(sEntityType, oUpdatedMssProps);
    },

    postProcessKpiListAndSave: function (oCallbackData) {
      _postProcessKpiListAndSave(oCallbackData)
          .then(_triggerChange)
          .catch(CS.noop);
    },

    discardKpiListChanges: function (oCallBack) {
      _discardKpiGridViewChanges(oCallBack);
      _triggerChange();
    },
  };
})();

MicroEvent.mixin(KpiStore);

export default KpiStore;
