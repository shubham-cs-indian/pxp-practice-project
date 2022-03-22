import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

var LanguageTagConfigGridViewSkeleton = function () {
  return {
    fixedColumns: [
      {
        id: "label",
        label: getTranslations().NAME_DETAIL,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      },
      {
        id: "code",
        label: getTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200
      }
    ],
    scrollableColumns: [
      {
        id: "icon",
        label: getTranslations().ICON,
        type: oGridViewPropertyTypes.IMAGE,
        isVisible: true,
        width: 80
      },
      {
        id: "description",
        label: getTranslations().DESCRIPTION,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        width: 200,
        isSortable: true
      },
      {
        id: "color",
        label: getTranslations().COLOR,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 120
      },
      {
        id: "tooltip",
        label: getTranslations().TOOLTIP,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        width: 200,
        isSortable: true
      },
      {
        id: "isFilterable",
        label: getTranslations().IS_FILTERABLE,
        type: oGridViewPropertyTypes.YES_NO,
        isVisible: true,
        width: 150
      },
      {
        id: "availability",
        label: getTranslations().AVAILABILITY,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
      {
        id: "isGridEditable",
        label: getTranslations().GRID_EDITABLE,
        type: oGridViewPropertyTypes.YES_NO,
        isVisible: true,
        width: 150
      },
      {
        id: "isVersionable",
        label: getTranslations().VERSIONABLE,
        type: oGridViewPropertyTypes.YES_NO,
        isVisible: true,
        width: 150
      },
    ],
    actionItems: [
      {
        id: "delete",
        label: getTranslations().TAG_DELETE_MENU_ITEM_TITLE,
        class: "deleteIcon"
      },
      {
        id: "deleteValues",
        label: getTranslations().TAG_DELETE_VALUE_ITEM_TITLE,
        class: "deleteIcon"
      },
      // {
      //   id: "refresh",
      //   label: oTranslations().REFRESH,
      //   class: "refreshIcon"
      // },
      {
        id: "create",
        label: getTranslations().CREATE_TAG_VALUE,
        class: "createChildIcon"
      },
      {
        id: "moveUp",
        label: getTranslations().MOVE_UP,
        class: "moveUpIcon"
      },
      {
        id: "dummy",
        label: '',
        class: ''
      },
      {
        id: "moveDown",
        label: getTranslations().MOVE_DOWN,
        class: "moveDownIcon"
      }
    ],
    selectedContentIds: []
  }
};

export default LanguageTagConfigGridViewSkeleton;