
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';

import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../viewlibraries/tack/view-library-constants'; //todo: find alternative

let BulkAssetDownloadGridViewSkeleton = function () {
  return {
    fixedColumns: [],
    scrollableColumns: [{
      id: "asset",
      label: getTranslation().ASSET,
      type: oGridViewPropertyTypes.TEXT,
      width: 250,
    }],
    selectedContentIds: [],
  }
};

export default BulkAssetDownloadGridViewSkeleton;
