import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oClassCategoryConstants from '../class-category-constants-dictionary';
import CS from "../../../../../../libraries/cs";
import EntityProps from "../../../../../../commonmodule/props/entity-props";
import oConfigEntityTypeDictionary from "../../../../../../commonmodule/tack/config-entity-type-dictionary";

let getDataModelClassGroupingList = function () {
  let aDataModelClassGroupingList = [
    {
      id: oClassCategoryConstants.ARTICLE_CLASS,
      label: getTranslations().ARTICLE,
      children:[/**No children*/],
      className: 'actionItemContentClasses',
      allowedTypes: [],
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_CLASS
    },
    {
      id: oClassCategoryConstants.ASSET_CLASS,
      label: getTranslations().ASSET,
      children:[/**No children*/],
      className: 'actionItemAssetClasses',
      allowedTypes: [],
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_CLASS
    },
    {
      id: oClassCategoryConstants.TARGET_CLASS,
      label: getTranslations().MARKET,
      children:[/**No children*/],
      className: 'actionItemTargetClasses',
      allowedTypes: [oClassCategoryConstants.TARGET_CLASS],
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_CLASS
    },
    {
      id: oClassCategoryConstants.TEXTASSET_CLASS,
      label: getTranslations().TEXT_ASSET,
      children:[/**No children*/],
      className: 'actionItemTextAssetClasses',
      allowedTypes: [],
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_CLASS
    },
    {
      id: oClassCategoryConstants.SUPPLIER_CLASS,
      label: getTranslations().SUPPLIER,
      children:[/**No children*/],
      className: 'actionItemSupplierClasses',
      allowedTypes: [],
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_CLASS
    },
  ];

  let aAllowedEntityIds = EntityProps.getAllEntityIds();
  return (CS.filter(aDataModelClassGroupingList, function (oClass) {
    return CS.includes(aAllowedEntityIds, oClass.id) || (CS.intersection(aAllowedEntityIds, oClass.allowedTypes).length > 0);
  }));
};

export default getDataModelClassGroupingList;