import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager';
import ContentScreenConstants from "../../store/model/content-screen-constants";

let getAdditionalFilterSkeleton = (sId = "", sLabel = "", aChildren = [], bIsMultiSelect = true, bIsFilterItemSearchHidden = true, bIsHiddenInAdvancedFilters = true) => {
  return {
    id: sId,
    type: sId,
    label: sLabel,
    children: aChildren,
    isMultiSelect: bIsMultiSelect,
    isFilterItemSearchHidden: bIsFilterItemSearchHidden,
    isHiddenInAdvancedFilters: bIsHiddenInAdvancedFilters,
  }
};

let getAssetAdditionalFilters = function () {
  return [
    getAdditionalFilterSkeleton(
        ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY,
        getTranslation().ASSET_EXPIRY,
        [
          {
            id: "expiredAsset",
            label: getTranslation().EXPIRED_ASSET,
            ignoreCount: true
          },
          {
            id: "nonExpiredAsset",
            label: getTranslation().NON_EXPIRED_ASSET,
            ignoreCount: true
          }
        ],
    ),
    getAdditionalFilterSkeleton(
        ContentScreenConstants.FILTER_FOR_DUPLICATE_ASSETS,
        getTranslation().DUPLICATE,
        [
          {
            id: "duplicateAssets",
            label: getTranslation().DUPLICATE_ASSETS,
            ignoreCount: true
          }
        ],
    )
  ]
};

export default {
  assetAdditionalFilters: getAssetAdditionalFilters
}