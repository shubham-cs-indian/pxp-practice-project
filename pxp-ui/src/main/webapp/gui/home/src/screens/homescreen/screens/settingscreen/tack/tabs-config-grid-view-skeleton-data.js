import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';
var TabsConfigGridSkeleton = function () {
  return{
    fixedColumns: [
      {
        id: "sequence",
        label: "Sequence",
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        width: 107,
        isSortable: true
      },
      {
        id: "label",
        label: oTranslations().NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      },
      {
        id: "code",
        label: oTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      }
    ],
    scrollableColumns: [
      {
        id: "icon",
        label: oTranslations().ICON,
        type: oGridViewPropertyTypes.IMAGE,
        isVisible: true,
        width: 80
      },
    ],
    actionItems: [
      {
        id: "delete",
        label: oTranslations().DELETE,
        class: "deleteIcon"
      },
      {
        id: "edit",
        label: oTranslations().EDIT,
        class: "editIcon"
      },
      // {
      //   id: "refresh",
      //   label: oTranslations().REFRESH,
      //   class: "refreshIcon"
      // },
      // {
      //   id: "create",
      //   label: oTranslations().TAG_CREATE_MENU_ITEM_TITLE,
      //   class: "createChildIcon"
      // }
      {
        id: "manageEntity",
        label: oTranslations().MANAGE_ENTITY_USAGE,
        class: "manageEntity"
      }
    ],
    selectedContentIds: []
  }
};

export default TabsConfigGridSkeleton;