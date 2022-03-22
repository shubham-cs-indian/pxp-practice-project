import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oModulesDictionary from "./config-modules-dictionary";

let aDataCollaborationEntityList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_TASK,
      label: getTranslations().TASKS,
      children: [/**No Children*/],
      className: "actionItemTask",
      parentId: oModulesDictionary.COLLABORATION
    }
  ];
};

export default aDataCollaborationEntityList;