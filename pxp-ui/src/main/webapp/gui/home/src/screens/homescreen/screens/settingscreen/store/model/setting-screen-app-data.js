/**
 * Created by DEV on 27-07-2015.
 */
import CS from '../../../../../../libraries/cs';

import actionBarItemList from '../../tack/action-bar-item-list';
import oModules from '../../tack/translations-module-list';
import MockDataForEntityBaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';

var SettingScreenAppData = (function () {

  let Props = function () {

    return {
      aTagList: [
        {
          children: [],
          description: "",
          icon: null,
          id: -1,
          label: 'TAGS'
        }
      ],
      aClassList: [
        {
          children: [],
          icon: null,
          id: -1,
          label: 'CLASSES',
          type: MockDataForEntityBaseTypesDictionary.articleKlassBaseType
        }
      ],
      aTaskList: [
        {
          children: [],
          icon: null,
          id: -1,
          label: 'TASKS',
          type: MockDataForEntityBaseTypesDictionary.taskKlassBaseType
        }
      ],
      aAssetList: [
        {
          children: [],
          icon: null,
          id: -1,
          label: 'ASSETS',
          type: MockDataForEntityBaseTypesDictionary.assetKlassBaseType
        }
      ],
      aTargetList: [
        {
          children: [],
          icon: null,
          id: -1,
          label: 'MARKET',
          type: MockDataForEntityBaseTypesDictionary.marketKlassBaseType
        }
      ],
      // aEditorialList: [
      //   {
      //     children: [],
      //     icon: null,
      //     id: -1,
      //     label: 'EDITORIALS',
      //     type: _oClassTypeDictionary["12"]
      //   }
      // ],
      aTextAssetList: [
        {
          children: [],
          icon: null,
          id: -1,
          label: 'TEXT_ASSET',
          type: MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType
        }
      ],
      aSupplierList: [
        {
          children: [],
          icon: null,
          id: -1,
          label: 'SUPPLIER',
          type: MockDataForEntityBaseTypesDictionary.supplierKlassBaseType
        }
      ],
      aPropertyCollectionList: [],
      aContextList: [],
      aNatureRelationshipList: [],
      aClassTree: [{
        id: "class_taxonomy",
        label: "Class Taxonomy",
        type: "klass",
        children: []
      }],
      aTaxonomyTree: [],
      aTaxonomyMasterList: [{
        id: -1,
        label: "Taxonomies",
        children: []
      }],
      aAttributionTaxonomyList: [{
        id: -1,
        label: "Attribution Taxonomies",
        children: []
      }],
      oAttributeListMap: {},
      oTagListMap: {},
      oContextListMap: {},
      oProfileListMap: {},
      oMappingListMap: {},
      oAuthorizationMappingListMap: {},
      oTasksListMap: {},
      oDataGovernanceTasksListMap: {},
      oTemplatesListMap: {},
      oMandatoryAttributeListMap: {},
      oTaskMandatoryAttributeListMap: {},
      oAssetMandatoryAttributeListMap: {},
      oTargetMandatoryAttributeListMap: {},
      oEditorialMandatoryAttributeListMap: {},
      oRoleListMap: {},
      oTaxonomyListMap: {},
      oRuleListMap: {},
      oMasterTypeTagMap: {},
      aListOfRuleListMap: [],
      oUserListMap: {},
      aAllClassesFlatList: [],
      aAssetLazyList: [],
      oRelationshipMasterListMap: {},
      aAttributeTypes: [],
      aTagTypes: [],
      aTagColors: {},
      aClassViewSettingList: [],
      aTagTypeList: [],
      aAllCollectionClassesFlatList: [],
      aAllSetClassesFlatList: [],
      aAllTaskClassesFlatList: [],
      aAllAssetClassesFlatList: [],
      aAllAssetCollectionClassesFlatList: [],
      aAllMarketTargetClassesFlatList: [],
      aAllTargetCollectionClassesFlatList: [],
      aAllEditorialClassesFlatList: [],
      aAllEditorialCollectionClassesFlatList: [],
      aAllTextAssetClassesFlatList: [],
      aAllSupplierClassesFlatList: [],
      aAllAssetCollectionClassList: [],
      oClassMap: {},
      oCalculatedAttributeMapping: {},
      oProcessList: {},
      aGtinKlass: [],
      aEmbeddedKlass: [],
      aTechnicalImageKlass: [],
      oKpiData: {},
      oRuleData:{},
      oGoldenRuleData: {},
      oKpiListMap: {},
      aSystemList: [],
      oSystemMap: {},
      oEndpointMap: {},
      aCustomTabList: [],
      aTemplates: [],
      aSystemsForRoles: [],
      oSystemsForRoles: {},
      aDashboardTabs: [],
      aAllTalendJobList: [],
      aCustomComponentList: [],
      oGoldenRecordRuleListMap: []
    };
  };

  let oProperties = new Props();

  return {

    setClassMapFromList: function (aList) {
      oProperties.oClassMap = aList;
      // CS.forEach(aList, function (oItem) {
      //   oClassMap[oItem.id] = oItem;
      // });
    },

    getClassMap: function () {
      return oProperties.oClassMap;
    },

    getActionBarList: function () {
      return new actionBarItemList();
    },

    getTranslationsModuleList: function () {
      return new oModules();
    },

    getAttributeList: function () {
      return oProperties.oAttributeListMap;
    },

    setAttributeList: function (_aAttributeList, bAppend) {
      if (!bAppend) {
        oProperties.oAttributeListMap = {};
      }
      CS.forEach(_aAttributeList, function (oAttribute) {
        oProperties.oAttributeListMap[oAttribute.id] = oAttribute;
      });
    },

    appendAttributeList: function (_aAttributeList) {
      this.setAttributeList(_aAttributeList, true);
    },

    getContextList: function () {
      return oProperties.oContextListMap;
    },

    setContextList: function (_aContextList) {
      oProperties.oContextListMap = {};
      CS.forEach(_aContextList, function (oContext) {
        oProperties.oContextListMap[oContext.id] = oContext;
      });
    },

    getProfileList: function () {
      return oProperties.oProfileListMap;
    },

    setProfileList: function (_aProfileList) {
      oProperties.oProfileListMap = {};
      CS.forEach(_aProfileList, function (oProfile) {
        oProperties.oProfileListMap[oProfile.id] = oProfile;
      });
    },

    getMappingList: function () {
      return oProperties.oMappingListMap;
    },

    setMappingList: function (_aMappingList) {
      oProperties.oMappingListMap = {};
      CS.forEach(_aMappingList, function (oMapping) {
        oProperties.oMappingListMap[oMapping.id] = oMapping;
      });
    },

    getAuthorizationMappingList: function () {
      return oProperties.oAuthorizationMappingListMap;
    },

    setAuthorizationMappingList: function (_aAuthorizationMappingList) {
      oProperties.oAuthorizationMappingListMap = {};
      CS.forEach(_aAuthorizationMappingList, function (oMapping) {
        oProperties.oAuthorizationMappingListMap[oMapping.id] = oMapping;
      });
    },

    getTasksList: function () {
      return oProperties.oTasksListMap;
    },

    setTasksList: function (_aTaskList) {
      oProperties.oTasksListMap = {};
      CS.forEach(_aTaskList, function (oTask) {
        oProperties.oTasksListMap[oTask.id] = oTask;
      });
    },

    getDataGovernanceTasksList: function () {
      return oProperties.oDataGovernanceTasksListMap;
    },

    setDataGovernanceTasksList: function (_aDataGovernanceTaskList) {
      oProperties.oDataGovernanceTasksListMap = {};
      CS.forEach(_aDataGovernanceTaskList, function (oDataGovernanceTask) {
        oProperties.oDataGovernanceTasksListMap[oDataGovernanceTask.id] = oDataGovernanceTask;
      });
    },

    getTemplatesList: function () {
      return oProperties.oTemplatesListMap;
    },

    setTemplatesList: function (_aTemplateList) {
      oProperties.oTemplatesListMap = {};
      CS.forEach(_aTemplateList, function (oTemplate) {
        oProperties.oTemplatesListMap[oTemplate.id] = oTemplate;
      });
    },

    getMandatoryAttributeList: function () {
      return oProperties.oMandatoryAttributeListMap;
    },

    setMandatoryAttributeList: function (_aMandatoryAttributeList) {
      CS.forEach(_aMandatoryAttributeList, function (oMandatoryAttribute) {
        oProperties.oMandatoryAttributeListMap[oMandatoryAttribute.type] = oMandatoryAttribute;
      });
    },

    getTaskMandatoryAttributeList: function () {
      return oProperties.oTaskMandatoryAttributeListMap;
    },

    setTaskMandatoryAttributeList: function (_aTaskMandatoryAttributeList) {
      CS.forEach(_aTaskMandatoryAttributeList, function (oTaslMandatoryAttribute) {
        oProperties.oTaskMandatoryAttributeListMap[oTaslMandatoryAttribute.type] = oTaslMandatoryAttribute;
      });
    },

    getAssetMandatoryAttributeList: function () {
      return oProperties.oAssetMandatoryAttributeListMap;
    },

    setAssetMandatoryAttributeList: function (_aAssetMandatoryAttributeList) {
      CS.forEach(_aAssetMandatoryAttributeList, function (oAssetMandatoryAttributeList) {
        oProperties.oAssetMandatoryAttributeListMap[oAssetMandatoryAttributeList.type] = oAssetMandatoryAttributeList;
      });
    },

    getTargetMandatoryAttributeList: function () {
      return oProperties.oTargetMandatoryAttributeListMap;
    },

    setTargetMandatoryAttributeList: function (_aTargetMandatoryAttributeList) {
      CS.forEach(_aTargetMandatoryAttributeList, function (oTargetMandatoryAttributeList) {
        oProperties.oTargetMandatoryAttributeListMap[oTargetMandatoryAttributeList.type] = oTargetMandatoryAttributeList;
      });
    },

    getEditorialMandatoryAttributeList: function () {
      return oProperties.oEditorialMandatoryAttributeListMap;
    },

    setEditorialMandatoryAttributeList: function (_aEditorialMandatoryAttributeList) {
      CS.forEach(_aEditorialMandatoryAttributeList, function (oEditorialMandatoryAttributeList) {
        oProperties.oEditorialMandatoryAttributeListMap[oEditorialMandatoryAttributeList.type] = oEditorialMandatoryAttributeList;
      });
    },

    getRoleList: function () {
      return oProperties.oRoleListMap;
    },

    setRoleList: function (_aRoleList, bAppend) {
      if (!bAppend) {
        oProperties.oRoleListMap = {};
      }
      CS.forEach(_aRoleList, function (oRole) {
        oProperties.oRoleListMap[oRole.id] = oRole;
      });
    },

    setTaxonomyList: function (_aTaxonomyList, bAppend) {
      if (!bAppend) {
        oProperties.oTaxonomyListMap = {};
      }
      CS.forEach(_aTaxonomyList, function (oTaxonomy) {
        oProperties.oTaxonomyListMap[oTaxonomy.id] = oTaxonomy;
      });
    },

    getTaxonomyListMap: function () {
      return oProperties.oTaxonomyListMap;
    },

    getRuleData: function () {
      return oProperties.oRuleData;
    },

    setRuleData: function (_oRuleData) {
      oProperties.oRuleData = _oRuleData;
    },

    getGoldenRuleData: function () {
      return oProperties.oGoldenRuleData;
    },

    setGoldenRuleData: function (_oRuleData) {
      oProperties.oGoldenRuleData = _oRuleData;
    },
    getRuleList: function () {
      return oProperties.oRuleListMap;
    },

    setRuleList: function (_aRuleList) {
      oProperties.oRuleListMap = {};
      CS.forEach(_aRuleList, function (oRule) {
        oProperties.oRuleListMap[oRule.id] = oRule;
      });
    },

    getGoldenRecordRuleList: function () {
      return oProperties.oGoldenRecordRuleListMap;
    },

    setGoldenRecordRuleList: function (_aRuleList) {
      oProperties.oGoldenRecordRuleListMap = {};
      CS.forEach(_aRuleList, function (oRule) {
        oProperties.oGoldenRecordRuleListMap[oRule.id] = oRule;
      });
    },

    getMasterList: function () {
      return oProperties.oMasterTypeTagMap;
    },

    setMasterList: function (_MasterListMap) {
      oProperties.oMasterTypeTagMap = {};
      CS.forEach(_MasterListMap, function (oMasterItem) {
        oProperties.oMasterTypeTagMap[oMasterItem.id] = oMasterItem;
      });
    },

    getListOfRuleList: function () {
      return oProperties.aListOfRuleListMap;
    },

    setListOfRuleList: function (_aRuleList) {
      oProperties.aListOfRuleListMap = _aRuleList;
    },

    getUserList: function () {
      return oProperties.oUserListMap;
    },

    setUserList: function (_aUserList) {
      oProperties.oUserListMap = {};
      CS.forEach(_aUserList, function (oUser) {
        oProperties.oUserListMap[oUser.id] = oUser;
      });
    },

    getKpiData: function () {
      return oProperties.oKpiData;
    },

    setKpiData: function (_oKpiData) {
      oProperties.oKpiData = _oKpiData;
    },

    getKpiList: function () {
      return oProperties.oKpiListMap;
    },

    setKpiList: function (_aKpiList) {
      oProperties.oKpiListMap = {};
      CS.forEach(_aKpiList, function (oKpi) {
        oProperties.oKpiListMap[oKpi.id] = oKpi;
      });
    },

    getAttributeTypes: function () {
      return oProperties.aAttributeTypes;
    },

    setAttributeTypes: function (_aAttributeTypes) {
      oProperties.aAttributeTypes = _aAttributeTypes;
    },

    setAllClassesFlatList: function (_aClassListForRelationship) {
      oProperties.aAllClassesFlatList = _aClassListForRelationship;
    },

    getAllClassesFlatList: function () {
      return oProperties.aAllClassesFlatList;
    },

    setAllCollectionClassesFlatList: function (_aClassListForRelationship) {
      oProperties.aAllCollectionClassesFlatList = _aClassListForRelationship;
    },

    getAllCollectionClassesFlatList: function () {
      return oProperties.aAllCollectionClassesFlatList;
    },

    setAllSetClassesFlatList: function (_aClassListForRelationship) {
      oProperties.aAllSetClassesFlatList = _aClassListForRelationship;
    },

    getAllSetClassesFlatList: function () {
      return oProperties.aAllSetClassesFlatList;
    },

    setAllTaskClassesFlatList: function (_aAllTaskClassesFlatList) {
      oProperties.aAllTaskClassesFlatList = _aAllTaskClassesFlatList;
    },

    getAllTaskClassesFlatList: function () {
      return oProperties.aAllTaskClassesFlatList;
    },

    setAllAssetClassesFlatList: function (_aAllAssetClassesFlatList) {
      oProperties.aAllAssetClassesFlatList = _aAllAssetClassesFlatList;
    },

    getAllAssetClassesFlatList: function () {
      return oProperties.aAllAssetClassesFlatList;
    },

    setAllAssetCollectionClassesFlatList: function (_aAllAssetCollectionClassesFlatList) {
      oProperties.aAllAssetCollectionClassesFlatList = _aAllAssetCollectionClassesFlatList;
    },

    getAllAssetCollectionClassesFlatList: function () {
      return oProperties.aAllAssetCollectionClassesFlatList;
    },

    setAllTextAssetClassesFlatList: function (_aAllTextAssetClassesFlatList) {
      oProperties.aAllTextAssetClassesFlatList = _aAllTextAssetClassesFlatList;
    },

    getAllTextAssetClassesFlatList: function () {
      return oProperties.aAllTextAssetClassesFlatList;
    },

    setAllSupplierClassesFlatList: function (_aAllSupplierClassesFlatList) {
      oProperties.aAllSupplierClassesFlatList = _aAllSupplierClassesFlatList;
    },

    getAllSupplierClassesFlatList: function () {
      return oProperties.aAllSupplierClassesFlatList;
    },

    setAllMarketTargetClassesFlatList: function (_aAllMarketTargetClassesFlatList) {
      oProperties.aAllMarketTargetClassesFlatList = _aAllMarketTargetClassesFlatList;
    },

    getAllMarketTargetClassesFlatList: function () {
      return oProperties.aAllMarketTargetClassesFlatList;
    },
    setAllTargetCollectionClassesFlatList: function (_aAllTargetCollectionClassesFlatList) {
      oProperties.aAllTargetCollectionClassesFlatList = _aAllTargetCollectionClassesFlatList;
    },

    getAllTargetCollectionClassesFlatList: function () {
      return oProperties.aAllTargetCollectionClassesFlatList;
    },

    setAllAssetCollectionClassList: function (_aClassListForRelationship) {
      oProperties.aAllAssetCollectionClassList = _aClassListForRelationship;
    },

    setAllEditorialClassesFlatList: function (_aAllEditorialClassesFlatList) {
      oProperties.aAllEditorialClassesFlatList = _aAllEditorialClassesFlatList;
    },

    getAllEditorialClassesFlatList: function () {
      return oProperties.aAllEditorialClassesFlatList;
    },

    setAllEditorialCollectionClassesFlatList: function (_aAllEditorialCollectionClassesFlatList) {
      oProperties.aAllEditorialCollectionClassesFlatList = _aAllEditorialCollectionClassesFlatList;
    },

    getAllEditorialCollectionClassesFlatList: function () {
      return oProperties.aAllEditorialCollectionClassesFlatList;
    },

    getAllAssetCollectionClassList: function () {
      return oProperties.aAllAssetCollectionClassList;
    },

    getAssetLazyList: function () {
      return oProperties.aAssetLazyList;
    },

    setAssetLazyList: function (_aAssetLazyList) {
      oProperties.aAssetLazyList = _aAssetLazyList;
    },

    getTagList: function () {
      oProperties.aTagList[0].label = oTranslations().TAGS;
      return oProperties.aTagList;
    },

    setTagList: function (_aTagList) {
      oProperties.aTagList.children = _aTagList;
    },

    createTagMap: function (_aTagList, bAppend) {
      if (!bAppend) {
        oProperties.oTagListMap = {};
      }
      CS.forEach(_aTagList, function (oTag) {
          oProperties.oTagListMap[oTag.id] = oTag;
      });
    },

    getTagMap: function () {
      return oProperties.oTagListMap;
    },

    setTagMap: function (_oTagListMap) {
      oProperties.oTagListMap = _oTagListMap;
    },

    setTagTypes : function(_aTagTypes){
      oProperties.aTagTypes = _aTagTypes;
    },

    setTagColors : function(_aTagColors){
      oProperties.aTagColors = _aTagColors;
    },

    getTagTypes : function(){
      return oProperties.aTagTypes;
    },

    getTagColors : function(){
      return oProperties.aTagColors;
    },

    getClassList: function () {
      oProperties.aClassList[0].label = oTranslations().CLASSES;
      return oProperties.aClassList;
    },

    setClassList: function (_aClassList) {
      oProperties.aClassList[0].children = _aClassList
    },

    getPropertyCollectionList: function () {
      return oProperties.aPropertyCollectionList;
    },

    setPropertyCollectionList: function (_aPropertyCollectionList) {
      oProperties.aPropertyCollectionList = _aPropertyCollectionList
    },

    getAllContextList: function () {
      return oProperties.aContextList;
    },

    setAllContextList: function (_aContextList, bAppend) {
      if (!bAppend) {
        oProperties.aContextList = [];
      }
      oProperties.aContextList = oProperties.aContextList.concat(_aContextList);
    },

    getNatureRelationshipList: function () {
      return oProperties.aNatureRelationshipList;
    },

    setNatureRelationshipList: function (_aNatureRelationshipList, bAppend) {
      if (!bAppend) {
        oProperties.aNatureRelationshipList = [];
      }
      oProperties.aNatureRelationshipList = oProperties.aNatureRelationshipList.concat(_aNatureRelationshipList);
    },

    getClassTree: function () {
      return oProperties.aClassTree;
    },

    setClassTree: function (_aClassTree) {
      oProperties.aClassTree = _aClassTree
    },

    getTaskList: function () {
      oProperties.aTaskList[0].label = oTranslations().TASKS;
      return oProperties.aTaskList;
    },

    setTaskList: function (_aTagList) {
      oProperties.aTagList[0].children = _aTagList
    },

    getTaxonomyMasterList: function () {
      return oProperties.aTaxonomyMasterList;
    },

    getAttributionTaxonomyList: function () {
      return oProperties.aAttributionTaxonomyList;
    },

    setAttributionTaxonomyList: function () {
      return oProperties.aAttributionTaxonomyList;
    },

    setTaxonomyMasterList: function (_aTaxonomyMasterList) {
      oProperties.aTaxonomyMasterList = _aTaxonomyMasterList;
    },

    getAssetList: function () {
      oProperties.aAssetList[0].label = oTranslations().ASSETS;
      return oProperties.aAssetList;
    },

    setAssetList: function (_aAssetList) {
      oProperties.aAssetList[0].children = _aAssetList
    },

    getTargetList: function () {
      oProperties.aTargetList[0].label = oTranslations().MARKET;
      return oProperties.aTargetList;
    },

    setTargetList: function (_aTargetList) {
      oProperties.aTargetList[0].children = _aTargetList
    },

    getEditorialList: function () {
      oProperties.aEditorialList[0].label = oTranslations().EDITORIALS;
      return oProperties.aEditorialList;
    },

    setEditorialList: function (_aEditorialList) {
      oProperties.aEditorialList[0].children = _aEditorialList;
    },



    getTextAssetList: function () {
      oProperties.aTextAssetList[0].label = oTranslations().TEXT_ASSET;
      return oProperties.aTextAssetList;
    },

    setTextAssetList: function (_aTextAssetList) {
      oProperties.aTextAssetList[0].children = _aTextAssetList;
    },

    getSupplierList: function () {
      oProperties.aSupplierList[0].label = oTranslations().SUPPLIER;
      return oProperties.aSupplierList;
    },

    setSupplierList: function (_aSupplierList) {
      oProperties.aSupplierList[0].children = _aSupplierList;
    },

    getClassListByTypeGeneric: function (sType) {

      switch (sType) {
        case 'class':
          oProperties.aClassList[0].label = oTranslations().CLASSES;
          return oProperties.aClassList;
          break;

        case 'task':
          oProperties.aTaskList[0].label = oTranslations().TASKS;
          return oProperties.aTaskList;
          break;

        case 'asset':
          oProperties.aAssetList[0].label = oTranslations().ASSETS;
          return oProperties.aAssetList;
          break;

        case 'target':
          oProperties.aTargetList[0].label = oTranslations().MARKET;
          return oProperties.aTargetList;
          break;

        case 'editorial':
          oProperties.aEditorialList[0].label = oTranslations().EDITORIALS;
          return oProperties.aEditorialList;
          break;

        case 'textasset':
          oProperties.aTextAssetList[0].label = oTranslations().TEXT_ASSET;
          return oProperties.aTextAssetList;
          break;

        case 'supplier':
          oProperties.aSupplierList[0].label = oTranslations().SUPPLIER;
          return oProperties.aSupplierList;
          break;
      }
      return null;
    },

    getTaxonomyList: function () {
      oProperties.aPIMTaxonomyList.label = oTranslations().TAXONOMY;
      return oProperties.aPIMTaxonomyList;
    },

    getRelationshipMasterList: function () {
      return oProperties.oRelationshipMasterListMap;
    },

    setRelationshipMasterList: function (_oRelationshipMasterListMap) {
      oProperties.oRelationshipMasterListMap = _oRelationshipMasterListMap;
    },

    setRelationshipList: function (_aRelationshipList, bAppend) {
      if (!bAppend) {
        oProperties.oRelationshipMasterListMap = {};
      }
      CS.forEach(_aRelationshipList, function (oRelationship) {
        oProperties.oRelationshipMasterListMap[oRelationship.id] = oRelationship;
      });
    },

    appendOrSetPropertyCollectionList: function (_aPropertyCollectionList, bAppend) {
      if (!bAppend) {
        oProperties.aPropertyCollectionList = [];
      }
      oProperties.aPropertyCollectionList = oProperties.aPropertyCollectionList.concat(_aPropertyCollectionList);
    },

    getClassViewSettingList: function () {
      return oProperties.aClassViewSettingList;
    },

    setClassViewSettingList: function (_aClassViewSettingList) {
      oProperties.aClassViewSettingList = _aClassViewSettingList;
    },

    getTagTypeList: function () {
      return oProperties.aTagTypeList;
    },

    setTagTypeList: function (_aTagTypeList) {
      oProperties.aTagTypeList = _aTagTypeList;
    },

    getCalculatedAttributeMapping: function () {
      return oProperties.oCalculatedAttributeMapping;
    },

    setCalculatedAttributeMapping: function (oMapping) {
      oProperties.oCalculatedAttributeMapping = oMapping;
    },

    getProcessList: function () {
      return oProperties.oProcessList;
    },

    setProcessList: function (aProcessList) {
      oProperties.oProcessList = CS.keyBy(aProcessList, 'id');
    },

    getGtinKlassList: function () {
      return oProperties.aGtinKlass;
    },

    setGtinKlassList: function (_aGtinKlass) {
      oProperties.aGtinKlass = _aGtinKlass;
    },

    getMasterTypeTagList: function () {
      return oProperties.oMasterTypeTagMap;
    },

    setMasterTypeTagList: function (_aMasterTypeTags) {
      oProperties.oMasterTypeTagMap = {};
      CS.forEach(_aMasterTypeTags, function (oMasterItem) {
        oProperties.oMasterTypeTagMap[oMasterItem.id] = oMasterItem;
      });
    },

    getEmbeddedKlassList: function () {
      return oProperties.aEmbeddedKlass;
    },

    setEmbeddedKlassList: function (_aEmbeddedKlass) {
      oProperties.aEmbeddedKlass = _aEmbeddedKlass;
    },

    getTechnicalImageKlassList: function () {
      return oProperties.aTechnicalImageKlass;
    },

    setTechnicalImageKlassList: function (_aTechnicalImageKlass) {
      oProperties.aTechnicalImageKlass = _aTechnicalImageKlass;
    },

    getSystemsList() {
      return oProperties.aSystemList;
    },

    setSystemsList(_systemList) {
      oProperties.aSystemList = _systemList;
    },

    getSystemsMap: function() {
      return oProperties.oSystemMap;
    },

    setSystemsMap: function (_aSystemList) {
      oProperties.oSystemMap = CS.keyBy(_aSystemList, "id");
    },

    getEndpointMap: function () {
      return oProperties.oEndpointMap;
    },

    setEndpointMap: function (_aEndpointList) {
      oProperties.oEndpointMap = CS.keyBy(_aEndpointList, "id");
    },

    getCustomTabList () {
      return oProperties.aCustomTabList;
    },

    setCustomTabList (_aCustomTabList, bAppend) {
      if (!bAppend) {
        oProperties.aCustomTabList = [];
      }
      oProperties.aCustomTabList = oProperties.aCustomTabList.concat(_aCustomTabList);
    },

    getTemplates(){
      return oProperties.aTemplates;
    },

    setTemplates(_aAllowedTemplates, bAppend){
      if (!bAppend) {
        oProperties.aTemplates = [];
      }
      oProperties.aTemplates = oProperties.aTemplates.concat(_aAllowedTemplates);
    },

    getSystemsForRoles: function () {
      return oProperties.aSystemsForRoles;
    },

    setSystemsForRoles: function (_aSystemsForRoles) {
      oProperties.aSystemsForRoles = _aSystemsForRoles;
    },

    getSystemsMapForRoles: function () {
      return oProperties.oSystemsForRoles;
    },

    setSystemsMapForRoles: function (_aSystemsForRoles) {
      oProperties.oSystemsForRoles = {};
      CS.forEach(_aSystemsForRoles, function (oSystem) {
        oProperties.oSystemsForRoles[oSystem.id] = oSystem;
        oProperties.oSystemsForRoles[oSystem.id].list = oSystem.endpointIds;
        delete oProperties.oSystemsForRoles[oSystem.id].endpointIds;
      });
    },

    getDashboardTabsList: function () {
      return oProperties.aDashboardTabs;
    },

    setDashboardTabsList (_aDashboardTabList, bAppend) {
      if (!bAppend) {
        oProperties.aDashboardTabs = [];
      }
      oProperties.aDashboardTabs = oProperties.aDashboardTabs.concat(_aDashboardTabList);
    },

    getAllTalendJobList: function () {
      return oProperties.aAllTalendJobList;
    },

    setAllTalendJobList: function (_aAllTalendJobList) {
      oProperties.aAllTalendJobList = _aAllTalendJobList;
    },

    getAllCustomComponentList: function () {
      return oProperties.aCustomComponentList;
    },

    setAllCustomComponentList: function (_aCustomComponentList) {
        oProperties.aCustomComponentList = _aCustomComponentList;
    },

    reset: function () {
      oProperties = new Props();
    },

  };

})();

export default SettingScreenAppData;