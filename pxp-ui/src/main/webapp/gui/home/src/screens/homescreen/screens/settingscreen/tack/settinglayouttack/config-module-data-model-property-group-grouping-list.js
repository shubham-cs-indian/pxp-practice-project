import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';

let aDataModelPropertyGroupGroupingList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION,
      label: getTranslations().PROPERTY_COLLECTIONS,
      children: [/**No Children*/],
      className: "actionItemPropertyCollection",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYGROUPS
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_TABS,
      label: getTranslations().TABS_MENU_ITEM_TILE,
      children: [/**No Children*/],
      className: "actionItemTabs",
      parentId: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYGROUPS
    }
  ];
};

export default aDataModelPropertyGroupGroupingList;
