import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const ListViewActionItemButtonList = function () {
  return [
   {
      id: "addRole",
      className: "create",
      label: oTranslations().ADD_ROLE,
    },
  ]
};

export default ListViewActionItemButtonList;