import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

let SaveAsDialogGridViewSkeletonForMappings = function () {
  return{
    fixedColumns: [],
    scrollableColumns: [
      {
        id: "label",
        label: oTranslations().FROM_MAPPING,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
      },
      {
        id: "code",
        label: oTranslations().FROM_CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200
      },
      {
        id: "newLabel",
        label: oTranslations().TO_MAPPING,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
      },
      {
        id: "newCode",
        label: oTranslations().TO_CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200
      },
    ],
    actionItems: [],
    selectedContentIds: []
  }
};
export default SaveAsDialogGridViewSkeletonForMappings;