/**
 * Created by CS88 on 12-04-2017.
 */
import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {gridViewPropertyTypes as oGridViewPropertyTypes} from '../../../../../viewlibraries/tack/view-library-constants';
import {gridViewFilterTypes as oGridViewFilterTypes} from '../../../../../viewlibraries/tack/view-library-constants';

let DownloadTrackerGridViewSkeleton = function () {
  return {
    fixedColumns: [
      {
        id: "assetInstanceName",
        label: getTranslations().ASSET_NAME,
        type: oGridViewPropertyTypes.TEXT,
        isSortable: true,
        isSearchable: true,
        filterType: oGridViewFilterTypes.TEXT,
        width: 200
      },
    ],
    scrollableColumns: [
      {
        id: "assetInstanceClassName",
        label: getTranslations().NATIVE_CLASS,
        type: oGridViewPropertyTypes.TEXT,
        isFilterable: true,
        filterType: oGridViewFilterTypes.DROP_DOWN,
        width: 200

      },
      {
        id: "assetFileName",
        label:getTranslations().ASSET_FILE_NAME,
        type: oGridViewPropertyTypes.TEXT,
        isSortable: true,
        isSearchable: true,
        filterType: oGridViewFilterTypes.TEXT,
        width: 200
      },
      {
        id: "downloadedBy",
        label: getTranslations().DOWNLOADED_BY,
        type: oGridViewPropertyTypes.TEXT,
        isSortable: true,
        isFilterable: true,
        filterType: oGridViewFilterTypes.DROP_DOWN,
        width: 200
      },
      {
        id: "date",
        label: getTranslations().TIMESTAMP,
        type: oGridViewPropertyTypes.DATETIME,
        width: 200,
        isSortable: true,
        isFilterable: true,
        isSearchable: false,
      },
      {
        id: "renditionInstanceName",
        label: getTranslations().RENDITION_NAME,
        type: oGridViewPropertyTypes.TEXT,
        isSortable: true,
        isSearchable: true,
        filterType: oGridViewFilterTypes.TEXT,
        width: 200
      },
      {
        id: "renditionInstanceClassName",
        label: getTranslations().RENDITION_CLASS_NAME,
        type: oGridViewPropertyTypes.TEXT,
        isFilterable: true,
        filterType: oGridViewPropertyTypes.DROP_DOWN,
        width: 200
      },
      {
        id: "renditionFileName",
        label: getTranslations().RENDITION_FILE_NAME,
        isSortable: true,
        type: oGridViewPropertyTypes.TEXT,
        isSearchable: true,
        filterType: oGridViewFilterTypes.TEXT,
        width: 200

      },
      {
        id: "comment",
        label: getTranslations().COMMENTS,
        isSortable: true,
        type: oGridViewPropertyTypes.TEXT,
        isSearchable: true,
        filterType: oGridViewFilterTypes.TEXT,
        width: 200
      }
    ],
    selectedContentIds: [],
  }
};

export default DownloadTrackerGridViewSkeleton;
