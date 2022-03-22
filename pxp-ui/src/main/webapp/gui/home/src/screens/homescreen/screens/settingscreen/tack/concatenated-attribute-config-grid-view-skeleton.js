import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

var ConcatenatedAttributeConfigGridSkeleton = function () {
  return{
    fixedColumns: [
      {
        id: "icon",
        label: getTranslations().ICON,
        type: oGridViewPropertyTypes.IMAGE,
        isVisible: true,
        width: 80
      },
      {
        id: "label",
        label: getTranslations().NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      },
      {
        id: "code",
        label: getTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      }
    ],
    scrollableColumns: [
      /*{
        id: "placeholder",
        label: getTranslations().PLACEHOLDER,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        width: 200,
        isSortable: true
      },*/
      {
        id: "description",
        label: getTranslations().DESCRIPTION,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        width: 200,
        isSortable: true
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
        id: "attributeConcatenatedList",
        label: getTranslations().CONCATENATION_FORMULA,
        type: oGridViewPropertyTypes.CONCATENATED_FORMULA,
        isVisible: true,
        width: 200,
        extraData: {}
      },
      {
        id: "isSearchable",
        label: getTranslations().IS_SEARCHABLE,
        type: oGridViewPropertyTypes.YES_NO,
        isVisible: true,
        width: 80
      },
      {
        id: "availability",
        label: getTranslations().AVAILABILITY,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
      {
        id: "isDisabled",
        label: getTranslations().READ_ONLY,
        type: oGridViewPropertyTypes.YES_NO,
        isVisible: true,
        width: 100
      },
      {
        id: "isCodeVisible",
        label: getTranslations().SHOW_TAG_CODE,
        type: oGridViewPropertyTypes.YES_NO,
        isVisible: true,
        width: 100
      },
      {
        id: "isTranslatable",
        label: getTranslations().LANGUAGE_DEPENDENT,
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
        label: getTranslations().ATTRIBUTE_DELETE_MENU_ITEM_TITLE,
        class: "deleteIcon"
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
        label: getTranslations().MANAGE_ENTITY_USAGE,
        class: "manageEntity"
      },
    ],
    selectedContentIds: []
  }
};

export default ConcatenatedAttributeConfigGridSkeleton;