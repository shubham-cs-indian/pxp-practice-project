import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
let mockDataForBulkAssetDownload = function () {

  return ([
        {
          id: "mainAssets",
          label: getTranslation().MAIN_ASSETS
        },
        {
          id: "variantAssets",
          label: getTranslation().VARIANT_ASSETS
        }
      ]
  );
};

export default mockDataForBulkAssetDownload;