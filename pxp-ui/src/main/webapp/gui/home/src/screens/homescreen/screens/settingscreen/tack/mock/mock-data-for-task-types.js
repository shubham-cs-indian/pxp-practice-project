import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import TaskTypeDictionary from '../../../../../../commonmodule/tack/task-type-dictionary.js';
let oTaskTypeDictionary = new TaskTypeDictionary();
const taskTypes = function () {
  return [
    {
      id: oTaskTypeDictionary.PERSONAL,
      label: oTranslations().PERSONAL,
    },
    {
      id: oTaskTypeDictionary.SHARED,
      label: oTranslations().SHARED,
    }
  ];
};

export default taskTypes;