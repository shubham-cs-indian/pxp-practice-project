import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import NatureTypeDictionary from '../../../../../../commonmodule/tack/nature-type-dictionary.js';

var aArticleNatureTypes = function () {
  return [
    {
      id: NatureTypeDictionary.FIXED_BUNDLE,
      label: oTranslations().FIXED_BUNDLE,
      maxNoOfItems: 10
    },
    {
      id: NatureTypeDictionary.SET_OF_PRODUCTS,
      label: oTranslations().SET_OF_PRODUCTS,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.PID_SKU,
      label: oTranslations().BASE_ARTICLE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.SINGLE_ARTICLE,
      label: oTranslations().SINGLE_ARTICLE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.GTIN,
      label: oTranslations().GTIN,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.EMBEDDED,
      label: oTranslations().EMBEDDED,
      maxNoOfItems: 0
    },
/*    {
      id: NatureTypeDictionary.LANGUAGE,
      label: oTranslations().LANGUAGE,
      maxNoOfItems: 0
    },*/
  ]
};

var aAssetNatureTypes = function () {
  return [
    {
      id: NatureTypeDictionary.IMAGE_ASSET,
      label: oTranslations().IMAGE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.VIDEO_ASSET,
      label: oTranslations().VIDEO,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.DOCUMENT_ASSET,
      label: oTranslations().DOCUMENT,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.FILE_ASSET,
      label: oTranslations().FILE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.TECHNICAL_IMAGE,
      label: oTranslations().TECHNICAL_IMAGE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.EMBEDDED,
      label: oTranslations().EMBEDDED,
      maxNoOfItems: 0
    },
/*    {
      id: NatureTypeDictionary.LANGUAGE,
      label: oTranslations().LANGUAGE,
      maxNoOfItems: 0
    },*/
  ]
};


let aImageNatureTypes = function () {
  return [
    {
      id: NatureTypeDictionary.IMAGE_ASSET,
      label: oTranslations().IMAGE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.FILE_ASSET,
      label: oTranslations().FILE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.TECHNICAL_IMAGE,
      label: oTranslations().TECHNICAL_IMAGE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.EMBEDDED,
      label: oTranslations().EMBEDDED,
      maxNoOfItems: 0
    }
  ]
};

let aMultimediaNatureTypes = function () {
  return [
    {
      id: NatureTypeDictionary.VIDEO_ASSET,
      label: oTranslations().VIDEO,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.FILE_ASSET,
      label: oTranslations().FILE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.EMBEDDED,
      label: oTranslations().EMBEDDED,
      maxNoOfItems: 0
    }
  ]
};

let aDocumentNatureTypes = function () {
  return [
    {
      id: NatureTypeDictionary.DOCUMENT_ASSET,
      label: oTranslations().DOCUMENT,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.FILE_ASSET,
      label: oTranslations().FILE,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.EMBEDDED,
      label: oTranslations().EMBEDDED,
      maxNoOfItems: 0
    }
  ]
};

var aMarketNatureTypes = function () {
  return [
    {
      id: NatureTypeDictionary.MARKET,
      label: oTranslations().MARKET,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.EMBEDDED,
      label: oTranslations().EMBEDDED,
      maxNoOfItems: 0
    }
  ]
};

var aTextAssetNatureTypes = function () {
  return [
    {
      id: NatureTypeDictionary.TEXT_ASSET,
      label: oTranslations().TEXT_ASSET,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.EMBEDDED,
      label: oTranslations().EMBEDDED,
      maxNoOfItems: 0
    },
/*    {
      id: NatureTypeDictionary.LANGUAGE,
      label: oTranslations().LANGUAGE,
      maxNoOfItems: 0
    },*/
  ];
};

var aSupplierNatureTypes = function () {
  return [
    {
      id: NatureTypeDictionary.SUPPLIER,
      label: oTranslations().SUPPLIER,
      maxNoOfItems: 0
    },
    {
      id: NatureTypeDictionary.EMBEDDED,
      label: oTranslations().EMBEDDED,
      maxNoOfItems: 0
    },
/*    {
      id: NatureTypeDictionary.LANGUAGE,
      label: oTranslations().LANGUAGE,
      maxNoOfItems: 0
    },*/
  ]
};

export default {
  articleNatureTypes: aArticleNatureTypes,
  assetNatureTypes: aAssetNatureTypes,
  marketNatureTypes: aMarketNatureTypes,
  textAssetNatureTypes: aTextAssetNatureTypes,
  supplierNatureTypes: aSupplierNatureTypes,
  imageNatureTypes: aImageNatureTypes,
  multimediaNatureTypes: aMultimediaNatureTypes,
  documentNatureTypes: aDocumentNatureTypes
};
