
import ContentScreenConstants from '../model/content-screen-constants';

import ContentScreenRequestMapping from '../../tack/content-screen-request-mapping';
import ArticleRequestMapping from '../../tack/article-request-mapping';
import CollectionRequest from '../../tack/collection-request-mapping';
import AssetRequestMapping from '../../tack/asset-request-mapping';
import MarketRequestMapping from '../../tack/market-request-mapping';
import SupplierRequestMapping from '../../tack/supplier-request-mapping';
import TextAssetRequestMapping from '../../tack/text-asset-request-mapping';
import {getTranslations} from '../../../../../../commonmodule/store/helper/translation-manager';

var ScreenModeUtils = (function () {

  var _getScreenRequestMapping = function (sScreenMode) {
    switch (sScreenMode) {
      case ContentScreenConstants.entityModes.ARTICLE_MODE:
        return ArticleRequestMapping;

      case ContentScreenConstants.entityModes.ASSET_MODE:
        return AssetRequestMapping;

      case ContentScreenConstants.entityModes.MARKET_MODE:
        return MarketRequestMapping;

      case ContentScreenConstants.entityModes.STATIC_COLLECTION_MODE:
        return CollectionRequest;

      case ContentScreenConstants.entityModes.DYNAMIC_COLLECTION_MODE:
        return CollectionRequest;

      case ContentScreenConstants.entityModes.SUPPLIER_MODE:
        return SupplierRequestMapping;

      case ContentScreenConstants.entityModes.TEXTASSET_MODE:
        return TextAssetRequestMapping;

      default:
        return ContentScreenRequestMapping;
    }
  };

  var _getTranslationDictionary = function () {
    return getTranslations();
  };

  return {
    getScreenRequestMapping: function (sScreenMode) {
      return _getScreenRequestMapping(sScreenMode);
    },

    getTranslationDictionary: function () {
      return _getTranslationDictionary();
    },
  }
})();

export default ScreenModeUtils;
