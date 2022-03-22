/**
 * Created by CS99 on 08/12/2017.
 */
import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';

import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oModulesDictionary from "./config-modules-dictionary";

let aDataIntegrationEntityList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS,
      label: getTranslations().WORKFLOW_MENU_ITEM_TITLE,
      children: [/**No Children*/],
      className: "actionItemProcess",
      parentId: oModulesDictionary.DATA_INTEGRATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_MAPPING,
      label: getTranslations().MAPPINGS,
      children: [/**No Children*/],
      className: "actionItemMapping",
      parentId: oModulesDictionary.DATA_INTEGRATION

    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_PROFILE,
      label: getTranslations().ENDPOINTS,//Endpoints === Profile
      children: [/**No Children*/],
      className: "actionItemProfile",
      parentId: oModulesDictionary.DATA_INTEGRATION
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_AUTHORIZATION,
      label: getTranslations().AUTHORIZATION,
      children: [/**No Children*/],
      className: "actionItemAuthorization",
      parentId: oModulesDictionary.DATA_INTEGRATION
    },
  ];
};

export default aDataIntegrationEntityList;