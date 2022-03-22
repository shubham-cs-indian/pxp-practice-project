import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../viewlibraries/tack/view-library-constants';

let RelationshipConfigGridSkeleton = function () {
  return {
    fixedColumns: [
      {
        id: "icon",
        label: oTranslations().ICON,
        type: oGridViewPropertyTypes.IMAGE,
        isVisible: true,
        width: 80
      }
    ],
    scrollableColumns: [
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
      },
      {
        id: "tabId",
        label: oTranslations().TAB,
        type: oGridViewPropertyTypes.LAZY_MSS,
        width: 200
      },
      {
        id: "type",
        label: oTranslations().TYPE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        width: 200,
        isSortable: true
      },
    ],
    actionItems: [
      {
        id: "edit",
        label: oTranslations().EDIT,
        class: "editIcon"
      },{
        id: "delete",
        label: oTranslations().DELETE,
        class: "deleteIcon"
      },
    ],
    selectedContentIds: []
  }
};

export default RelationshipConfigGridSkeleton;