import {getTranslations as oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {gridViewPropertyTypes as oGridViewPropertyTypes} from './../../../../../viewlibraries/tack/view-library-constants';

var SSOSettingGridViewSkeleton = function () {
  return {
    fixedColumns: [],
    scrollableColumns: [
      {
        id: "domain",
        label: "Domain",
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
      },
      {
        id: "code",
        label: oTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      },
      {
        id: "idp",
        label: "SSO Setting",
        type: oGridViewPropertyTypes.DROP_DOWN,
        width: 200,
      }
    ],
    actionItems: [
      {
        id: "delete",
        label: oTranslations().DELETE,
        class: "deleteIcon"
      },
    ],
    selectedContentIds: []
  }
};
export default SSOSettingGridViewSkeleton;