import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../viewlibraries/tack/view-library-constants';

var EventConfigGridSkeleton = function () {
  return{
    fixedColumns: [
      {
        id: "icon",
        label: oTranslations().ICON,
        type: oGridViewPropertyTypes.IMAGE,
        isVisible: true,
        width: 80
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
        id: "type",
        label: oTranslations().TYPE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
      {
        id: "color",
        label: oTranslations().COLOR,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
      {
        id: "priorityTag",
        label: oTranslations().TASK_PRIORITY_TAG,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 150
      },
      {
        id: "statusTag",
        label: oTranslations().TASK_STATUS_TAG,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 150
      }
    ],
    actionItems: [
      {
        id: "delete",
        label: oTranslations().TASKS_DELETE_MENU_ITEM_TITLE,
        class: "deleteIcon"
      },
      {
        id: "manageEntity",
        label: oTranslations().MANAGE_ENTITY_USAGE,
        class: "manageEntity"
      }
    ],
    selectedContentIds: []
  }
};

export default EventConfigGridSkeleton;