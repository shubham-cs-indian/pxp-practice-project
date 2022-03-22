import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import oModulesDictionary from "./config-modules-dictionary";

let aAdminConfigurationEntityList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_VIEW_CONFIGURATION,
      label: getTranslations().VIEW_CONFIGURATION,
      children: [/**No Children*/],
      className: "actionItemViewConfiguration",
      parentId: oModulesDictionary.ADMIN_CONFIGURATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_THEME_CONFIGURATION,
      label: getTranslations().THEME_CONFIGURATION,
      children: [/**No Children*/],
      className: "actionItemThemeConfiguration",
      parentId: oModulesDictionary.ADMIN_CONFIGURATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_PDFREACTORSERVER,
      label: getTranslations().PDF_REACTOR_SERVER_CONFIGURATION,
      children: [/**No Children*/],
      className: "actionItemPdfReactorServer",
      parentId: oModulesDictionary.ADMIN_CONFIGURATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_GRIDEDITVIEW,
      label: getTranslations().GRID_EDIT_VIEW,
      children: [/**No Children*/],
      className: "actionItemGridEditView",
      parentId: oModulesDictionary.ADMIN_CONFIGURATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_ICON_LIBRARY,
      label: getTranslations().ICON_LIBRARY,
      children: [/**No Children*/],
      className: "actionItemIconLibrary",
      parentId: oModulesDictionary.ADMIN_CONFIGURATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_INDESIGN_SERVER_CONFIGURATION,
      label: getTranslations().INDESIGN_SERVER_CONFIGURATION, //getTranslations().INDESIGN_SERVER_CONFIGURATION
      children: [/**No Children*/],
      className: "actionItemRelationship",
      parentId: oModulesDictionary.ADMIN_CONFIGURATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_DAM_CONFIGURATION,
      label: getTranslations().DAM_CONFIGURATION, //getTranslations().DAM_CONFIGURATION
      children: [/**No Children*/],
      className: "actionItemRelationship",
      parentId: oModulesDictionary.ADMIN_CONFIGURATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_VARIANT_CONFIGURATION,
      label: getTranslations().VARIANT_CONFIGURATION,
      children: [/**No Children*/],
      className: "actionItemVariantConfiguration",
      parentId: oModulesDictionary.ADMIN_CONFIGURATION
    }
  ];
};

export default aAdminConfigurationEntityList;
