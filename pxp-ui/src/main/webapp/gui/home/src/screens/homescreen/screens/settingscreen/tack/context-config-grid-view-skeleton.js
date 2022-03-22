import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

var ContextConfigGridViewSkeleton = function () {
  return{
    fixedColumns: [
      {
        id: "icon",
        label: oTranslations().ICON,
        type: oGridViewPropertyTypes.IMAGE,
        width: 80,
        isVisible: true,
      }
    ],
    scrollableColumns: [

      {
        id: "label",
        label: oTranslations().NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 100,
        isSortable: true
      },
      {
        id: "code",
        label: oTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      },
      {
        id: "type",
        label: oTranslations().TYPE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        width: 100
      }
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
      {
        id: "manageEntity",
        label: oTranslations().MANAGE_ENTITY_USAGE,
        class: "manageEntity"
      },
    ],
    selectedContentIds: []
  }
};
export default ContextConfigGridViewSkeleton;