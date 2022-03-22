import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../viewlibraries/tack/view-library-constants';

var UserConfigGridSkeleton = function () {
  return{
    fixedColumns: [
      {
        id: "icon",
        label: oTranslations().PROFILE_PICTURE,
        type: oGridViewPropertyTypes.IMAGE,
        isVisible: true,
        width: 125
      },
      {
        id: "userName",
        label: oTranslations().USER_NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true,
        isDisabled: true,
        enablePointerEvent: true
      },
    ],
    scrollableColumns: [
      {
        id: "firstName",
        label: oTranslations().FIRST_NAME,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        isSortable: true,
        width: 150
      },
      {
        id: "lastName",
        label: oTranslations().LAST_NAME,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        isSortable: true,
        width: 150
      },
      {
        id: "email",
        label: oTranslations().EMAIL,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        isSortable: true,
        width: 150
      },
      {
        id: "contact",
        label: oTranslations().MOBILE_NUMBER,
        type: oGridViewPropertyTypes.MOBILE_NUMBER,
        isVisible: true,
        width: 150
      },
      {
        id: "gender",
        label: oTranslations().GENDER,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
      {
        id: "roleId",
        label: oTranslations().ROLE,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        width: 150
      },
      {
        id: "organizationName",
        label: oTranslations().ORGANIZATION_NAME,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        width: 150
      },
      {
        id: "organizationType",
        label: oTranslations().ORGANIZATION_TYPE,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        width: 150
      },
      {
        id: "isEmailLog",
        label: "Email Log File",
        type: oGridViewPropertyTypes.YES_NO,
        isVisible: true,
        width: 150
      }
    ],
    actionItems: [
      {
        id: "delete",
        label: oTranslations().USER_DELETE_MENU_ITEM_TITLE,// TODO: change
															// translation
        class: "deleteIcon"
      },
      {
        id: "edit",
        label: oTranslations().CHANGE_PASSWORD,
        class: "changePasswordIcon"
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

export default UserConfigGridSkeleton;