import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';

export default function () {
  return ([
    {
      id: "admin",
      label: getTranslation().ADMIN
    },
    {
      id: "user",
      label: getTranslation().USER
    }/*,
    {
      id: "SystemAdmin",
      label: getTranslation().SYSTEM_ADMIN
    }*/
  ])
};