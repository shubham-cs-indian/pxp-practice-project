import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import SettingScreenModuleDictionary from './../../../../../../commonmodule/tack/setting-screen-module-dictionary';
import oClassCategoryConstants from '../class-category-constants-dictionary';
import CS from "../../../../../../libraries/cs";
import EntityProps from "../../../../../../commonmodule/props/entity-props";
const TRANSLATIONS = "translations";


let getDataModelTranslationClassGroupingList = function () {
  const sSplitter = require('../../store/helper/setting-utils').default.getSplitter();

  let aDataModelClassGroupingList = [
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_ARTICLE,
      label: getTranslations().ARTICLE,
      entityType: TRANSLATIONS,
      children:[/**No children*/],
      className: 'actionItemContentClasses',
      allowedTypes: [oClassCategoryConstants.ARTICLE_CLASS],
      parentId: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS
    },
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_ASSET,
      label: getTranslations().ASSET,
      entityType: TRANSLATIONS,
      children:[/**No children*/],
      className: 'actionItemAssetClasses',
      allowedTypes: [oClassCategoryConstants.ASSET_CLASS],
      parentId: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS
    },
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_TARGET,
      label: getTranslations().MARKET,
      entityType: TRANSLATIONS,
      children:[/**No children*/],
      className: 'actionItemTargetClasses',
      allowedTypes: [oClassCategoryConstants.TARGET_CLASS],
      parentId: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS
    },
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_TEXTASSET,
      label: getTranslations().TEXT_ASSET,
      entityType: TRANSLATIONS,
      children:[/**No children*/],
      className: 'actionItemTextAssetClasses',
      allowedTypes: [oClassCategoryConstants.TEXTASSET_CLASS],
      parentId: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS
    },
    {
      id: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS_SUPPLIER,
      label: getTranslations().SUPPLIER,
      entityType: TRANSLATIONS,
      children:[/**No children*/],
      className: 'actionItemSupplierClasses',
      allowedTypes: [oClassCategoryConstants.SUPPLIER_CLASS],
      parentId: TRANSLATIONS + sSplitter + SettingScreenModuleDictionary.CLASS
    },
  ];

  let aAllowedEntityIds = EntityProps.getAllEntityIds();

  return( CS.filter(aDataModelClassGroupingList, function (oClass) {
    return (CS.intersection(aAllowedEntityIds, oClass.allowedTypes).length > 0);
  }));
};

export default getDataModelTranslationClassGroupingList;
