import {getTranslations as oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {gridViewPropertyTypes as oGridViewPropertyTypes} from './../../../../../viewlibraries/tack/view-library-constants';

let ShowExportStatusGridViewSkeleton = function () {
  return {
    fixedColumns: [],
    scrollableColumns: [
      {
        id: "fileName",
        label: "File Name",
        type: oGridViewPropertyTypes.TEXT,
        width: 100
      },
      {
        id: "startTime",
        label: "Start Time",
        type: oGridViewPropertyTypes.FLEXIBLE,
        width: 100
      },
      {
        id: "endTime",
        label: "End Time",
        type: oGridViewPropertyTypes.FLEXIBLE,
        width: 100
      },
      {
        id: "status",
        label: "Status",
        type: oGridViewPropertyTypes.CHIPS,
        width: 100
      }
    ],
    actionItems: [
      {
        id: "download",
        label: oTranslations().DOWNLOAD,
        class: "downloadIcon"
      },
      {
        id: "delete",
        label: oTranslations().DELETE,
        class: "deleteIcon"
      }
    ],
    selectedContentIds: []
  }
};
export default ShowExportStatusGridViewSkeleton;