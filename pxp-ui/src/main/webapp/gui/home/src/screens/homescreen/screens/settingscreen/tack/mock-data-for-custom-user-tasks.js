import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

export default function () {
  return [
    {
      id:"customUserTask",
      label:getTranslations().CUSTOM_USER_TASK,
    }
  ]
};
