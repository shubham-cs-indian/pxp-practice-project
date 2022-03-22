/**
 * Created by CS80 on 14/06/2017.
 */
import TemplateTabsDictionary from './template-tabs-dictionary';
import BaseTypeDictionary from './mock-data-for-entity-base-types-dictionary';

export default {
  INFINITE_DATE: 253402194600000,
  //Config entity constants
  ENTITY_TYPE_EVENT:"event",
  ENTITY_TYPE_TASK: "task",
  ENTITY_TYPE_TAXONOMY: "taxonomy",

  //Button Types
  BUTTON_TYPE_MENU: "menu",
  BUTTON_TYPE_SIMPLE: "simple",

  //Tabs Constants
  TAB_TIMELINE: TemplateTabsDictionary.TIMELINE_TAB,
  TAB_TASKS: TemplateTabsDictionary.TASKS_TAB,
  TAB_DASHBOARD: "dashboard",
  TAB_CUSTOM: TemplateTabsDictionary.CUSTOM_TAB,
  TAB_OVERVIEW: TemplateTabsDictionary.OVERVIEW_TAB,
  TAB_RENDITION: TemplateTabsDictionary.RENDITION_TAB,
  TAB_DUPLICATE_ASSETS: TemplateTabsDictionary.DUPLICATE_TAB,
  TAB_CONTEXT: TemplateTabsDictionary.CONTEXT_TAB,
  ONBOARDING_FILE_UPLOAD_EVENT:"BUSINESS_PROCESS_EVENT",
  OFFBOARDING_EXPORT_EVENT:"exportevent",
  DARK_THEME: "dark_theme",

  TREE_ROOT_ID: -1,
  TAXONOMY_HIERARCHY_ALL_DUMMY_NODE: "all_entity_dummy_node",
  NO_ID: "no_id",

  ORGANISATION_CONFIG_INFORMATION: "organisationConfigInformation",
  ORGANISATION_CONFIG_ROLES: "organisationConfigRoles",
  ORGANISATION_CONFIG_SSO_SETTING: "organisationConfigSSOSetting",

  GERMAN_CODE: "de_DE",
  FRENCH_CODE: "fr_FR",
  ENTITY_TYPE_SMARTDOCUMENT: "smartdocument",
};