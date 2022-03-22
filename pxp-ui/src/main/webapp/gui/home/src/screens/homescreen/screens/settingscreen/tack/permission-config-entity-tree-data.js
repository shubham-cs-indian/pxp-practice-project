import CS from '../../../../../libraries/cs';
import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import oConfigEntityTypeDictionary from './../../../../../commonmodule/tack/config-entity-type-dictionary';
import oPermissionEntityTypeDictionary from './../../../../../commonmodule/tack/permission-entity-type-dictionary';
import aConfigClassGroupList from './settinglayouttack/config-module-data-model-class-grouping-list';

let aEntityList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_CLASS,
      label: getTranslations().CLASSES,
      children: CS.sortBy(new aConfigClassGroupList(), "label"),
      className: "actionItemClass_clone"
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY,
      label: getTranslations().TAXONOMIES,
      children: [/**No Children*/],
      className: "actionItemAttributionTaxonomy"
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_TASK,
      label: getTranslations().TASKS,
      children: [/**No Children*/],
      className: "actionItemTask"
    },
    {
      id: oPermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION,
      label: getTranslations().FUNCTIONS,
      children: [/**No Children*/],
      className: "actionItemFunction"
    }
  ];
};

export default aEntityList;