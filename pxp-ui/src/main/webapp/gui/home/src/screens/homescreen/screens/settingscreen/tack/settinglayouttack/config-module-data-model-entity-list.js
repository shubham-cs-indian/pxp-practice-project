import CS from '../../../../../../libraries/cs';
import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import aConfigPropertiesList from './config-module-data-model-property-grouping-list';
import aConfigPropertyGroupsList from './config-module-data-model-property-group-grouping-list';
import aConfigClassGroupList from './config-module-data-model-class-grouping-list';
import aConfigRelationshipGroupList from './config-module-data-model-relationship-grouping-list';
import oModulesDictionary from "./config-modules-dictionary";

let groupedSort = function (aList, sGroupBy) {
  let aListToReturn = [];
  let oGroupedList = CS.groupBy(aList, sGroupBy);
  CS.forEach(oGroupedList, function (aValue, sKey) {
    let aSortedList = CS.sortBy(aValue, "label");
    aListToReturn = CS.concat(aListToReturn, aSortedList);
  });
  return aListToReturn;
};

let aDataModelEntityList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTIES,
      label: getTranslations().PROPERTIES,
      children: groupedSort(new aConfigPropertiesList(), "entityType"),
      className: "actionItemProperties",
      parentId: oModulesDictionary.DATA_MODEL
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT,
      label: getTranslations().CONTEXTS,
      children: [/**No Children*/],
      className: "actionItemContext",
      parentId: oModulesDictionary.DATA_MODEL
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYGROUPS,
      label: getTranslations().PROPERTY_GROUPS_MENU_ITEM_TITLE,
      children: CS.sortBy(new aConfigPropertyGroupsList(), "label"),
      className: "actionItemPropertyGroups",
      parentId: oModulesDictionary.DATA_MODEL
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_CLASS,
      label: getTranslations().CLASSES,
      children: CS.sortBy(new aConfigClassGroupList(), "label"),
      className: "actionItemClass_clone",
      parentId: oModulesDictionary.DATA_MODEL
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY,
      label: getTranslations().TAXONOMIES,
      children: [/**No Children*/],
      className: "actionItemAttributionTaxonomy",
      parentId: oModulesDictionary.DATA_MODEL
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_TEMPLATE,
      label: getTranslations().TEMPLATES,
      children: [/**No Children*/],
      className: "actionItemTemplate",
      parentId: oModulesDictionary.DATA_MODEL
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP,
      label: getTranslations().RELATIONSHIPS,
      children: [/**No children*/],
      className: "actionItemRelationships",
      parentId: oModulesDictionary.DATA_MODEL
    },

    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_SMARTDOCUMENT,
      label: getTranslations().SMART_DOCUMENT,
      children: [/**No Children*/],
      className: "actionItemSmartDocument",
      parentId: oModulesDictionary.DATA_MODEL
    }
  ];
};

export default aDataModelEntityList;
