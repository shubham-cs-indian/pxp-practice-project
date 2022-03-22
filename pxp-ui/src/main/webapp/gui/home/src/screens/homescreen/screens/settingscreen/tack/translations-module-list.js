import CS from '../../../../../libraries/cs';
import SettingScreenModuleDictionary from './../../../../../commonmodule/tack/setting-screen-module-dictionary';
import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import aConfigTranslationTabGroupList from './settinglayouttack/config-module-translation-tab-grouping-list';
import aConfigTranslationClassGroupList from './settinglayouttack/config-module-translations-class-grouping-list';
import aConfigTranslationSmartDocumentGroupList from './settinglayouttack/config-module-translations-smartDocument-grouping-list';
import oConfigEntityTypeDictionary from "../../../../../commonmodule/tack/config-entity-type-dictionary";

const TRANSLATIONS = "translations";

const modules = function () {

  var sSplitter = require('../store/helper/setting-utils').default.getSplitter();

  return {
    SettingScreenModules: [
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.STATIC_TRANSLATION,
        entityType: TRANSLATIONS,
        className: 'actionItemStaticTranslation',
        label: oTranslations().STATIC_TRANSLATION,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.TAG,
        className: 'actionItemTag',
        entityType: TRANSLATIONS,
        label: oTranslations().TAGS,
        properties: ['label', 'description', 'tooltip'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.TAG_VALUES,
        className: 'actionItemTagValues',
        entityType: TRANSLATIONS,
        label: oTranslations().TAG_VALUES,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.ATTRIBUTE,
        entityType: TRANSLATIONS,
        className: 'actionItemAttribute',
        label: oTranslations().ATTRIBUTES,
        properties: ['label', 'placeholder', 'description', 'tooltip'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CONTEXT,
        entityType: TRANSLATIONS,
        className: 'actionItemContext',
        label: oTranslations().CONTEXT,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.TASK,
        entityType: TRANSLATIONS,
        className: 'actionItemTask',
        label: oTranslations().TASKS,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.PROPERTY_COLLECTION,
        entityType: TRANSLATIONS,
        className: 'actionItemPropertyCollection',
        label: oTranslations().PROPERTY_COLLECTIONS,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.TABS,
        entityType: TRANSLATIONS,
        className: 'actionItemTab',
        label: oTranslations().TABS_MENU_ITEM_TILE,
        properties: ['label'],
        children: CS.sortBy( new aConfigTranslationTabGroupList(), "label"),
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS,
        entityType: TRANSLATIONS,
        className: 'actionItemClass_clone',
        label: oTranslations().CLASSES,
        properties: ['label'],
        children: CS.sortBy(new aConfigTranslationClassGroupList(), "label"),
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.TEMPLATE,
        entityType: TRANSLATIONS,
        className: 'actionItemTemplate',
        label: oTranslations().TEMPLATES,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.ATTRIBUTION_TAXONOMY,
        entityType: TRANSLATIONS,
        className: 'actionItemAttributionTaxonomy',
        label: oTranslations().TAXONOMIES,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.ORGANIZATION,
        entityType: TRANSLATIONS,
        className: 'actionItemOrganization',
        label: oTranslations().PARTNERS,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.KPI,
        entityType: TRANSLATIONS,
        className: 'actionItemKpi',
        label: oTranslations().KPI_MENU_ITEM_TILE,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.SYSTEM,
        entityType: TRANSLATIONS,
        className: 'actionItemSystem',
        label: oTranslations().SYSTEMS,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      /*{
        id: TRANSLATIONS+ sSplitter + SettingScreenModuleDictionary.PERMISSION,
        entityType: TRANSLATIONS,
        className: 'actionItemPermission',
        label: oTranslations().PERMISSIONS
      },*/
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.RELATIONSHIP,
        entityType: TRANSLATIONS,
        className: 'actionItemRelationship',
        label: oTranslations().RELATIONSHIPS,
        properties: ['label', 'side1Label', 'side2Label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      /*{
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.USER,
        entityType: TRANSLATIONS,
        className: 'actionItemUser',
        label: oTranslations().USERS,
        properties: ['label']
      },*/
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.ROLE,
        entityType: TRANSLATIONS,
        className: 'actionItemRole',
        label: oTranslations().ROLES,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.PROFILE,
        entityType: TRANSLATIONS,
        className: 'actionItemProfile',
        label: oTranslations().ENDPOINTS,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.RULE,
        entityType: TRANSLATIONS,
        className: 'actionItemRule',
        label: oTranslations().RULE_MENU_ITEM_TITLE,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.RULE_LIST,
        entityType: TRANSLATIONS,
        className: 'actionItemRuleList',
        label: oTranslations().RULELIST,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.PROCESS,
        entityType: TRANSLATIONS,
        className: 'actionItemProcess',
        label: oTranslations().PROCESS,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.MAPPING,
        entityType: TRANSLATIONS,
        className: 'actionItemMapping',
        label: oTranslations().MAPPINGS,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.TRANSLATION_TREE,
        entityType: TRANSLATIONS,
        className: 'translationTree',
        label: oTranslations().TRANSLATION_TREE,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      }/*,
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.MASTER_LIST,
        entityType: TRANSLATIONS,
        className: 'actionItemMasterList',
        label: oTranslations().MASTERLIST,
        properties: ['label']
      },
      {
        id: TRANSLATIONS+ sSplitter + SettingScreenModuleDictionary.TRANSLATION,
        entityType: TRANSLATIONS,
        className: 'actionItemTranslations',
        label: oTranslations().TRANSLATIONS
      }*/,
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.GOLDEN_RECORD_RULE,
        entityType: TRANSLATIONS,
        className: 'actionItemGoldenRecord',
        label: oTranslations().GOLDEN_RECORD_RULE,
        properties: ['label'],
        parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS
      },
      /*{
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.SMART_DOCUMENT,
        entityType: TRANSLATIONS,
        className: 'actionItemSmartDocument',
        label: oTranslations().SMART_DOCUMENT,
        properties: ['label'],
        children: CS.sortBy( new aConfigTranslationSmartDocumentGroupList, "label"),
      }*/
    ],
    ClassModules: [
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_ARTICLE,
        label: oTranslations().CLASS_CATEGORY_CONTENT_TITLE
      },
      /*{
        id: TRANSLATIONS+ sSplitter + SettingScreenModuleDictionary.CLASS_TASK,
        label: oTranslations().TASK
      },*/
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_ASSET,
        label: oTranslations().ASSET
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_TARGET,
        label: oTranslations().MARKET
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_TEXTASSET,
        label: oTranslations().TEXT_ASSET
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_SUPPLIER,
        label: oTranslations().SUPPLIER
      },/*,
      {
        id: TRANSLATIONS+ sSplitter + SettingScreenModuleDictionary.CLASS_EDITORIAL,
        label: oTranslations().CLASS_CATEGORY_EDITORIAL_CONTENT_TITLE
      }*/
    ],
    TabsModules: [
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CONTENT_TAB,
        label: oTranslations().CONTENTS
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.DASHBOARD_TAB,
        label: oTranslations().DASHBOARD_MENU_TITLE
      }
    ],
    SmartDocumentModules: [
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.SMART_DOCUMENT_TEMPLATE,
        label: oTranslations().TEMPLATES
      },
      {
        id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.SMART_DOCUMENT_PRESET,
        label: oTranslations().PRESETS
      }
    ]
  }
};

export default modules;