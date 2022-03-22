import { getTranslations as  oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

let AuthorizationConfigGridViewSkeleton = function () {
  return {
    fixedColumns: [],
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
        width: 200
      },],
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
      }
    ],
    selectedContentIds: []
  }
};
export default AuthorizationConfigGridViewSkeleton;