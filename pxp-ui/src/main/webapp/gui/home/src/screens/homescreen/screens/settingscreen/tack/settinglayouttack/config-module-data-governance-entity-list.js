import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oModulesDictionary from "./config-modules-dictionary";

let aDataGovernanceEntityList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_RULE,
      label: getTranslations().RULE_MENU_ITEM_TITLE,
      children: [/**No Children*/],
      className: "actionItemRule",
      parentId: oModulesDictionary.DATA_GOVERNANCE
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST,
      label: getTranslations().RULE_LIST_MENU_ITEM_TITLE,
      children: [/**No Children*/],
      className: "actionItemRuleList",
      parentId: oModulesDictionary.DATA_GOVERNANCE
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_KPI,
      label: getTranslations().KPI_MENU_ITEM_TILE,
      children: [/**No Children*/],
      className: "actionItemKpi",
      parentId: oModulesDictionary.DATA_GOVERNANCE
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_GOLDEN_RECORDS,
      label: getTranslations().GOLDEN_RECORD_MENU_ITEM_TILE,
      children: [/**No Children*/],
      className: "actionItemGoldenRecord",
      parentId: oModulesDictionary.DATA_GOVERNANCE
    },

    /*{
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_DATAGOVERNANCE,
      label: getTranslations().DATA_GOVERNANCE_TASKS_MENU_ITEM_TILE,
      children: [/!**No Children*!/],
      className: "actionItemDataGovernanceTasks"
    }*/
  ]
};

export default aDataGovernanceEntityList;