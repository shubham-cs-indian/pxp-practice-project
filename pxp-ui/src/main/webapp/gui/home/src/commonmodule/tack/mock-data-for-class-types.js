import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';
import MockDataForEntityBaseTypesDictionary from './mock-data-for-entity-base-types-dictionary';
import ClassTypeDictionary from './class-type-dictionary';
import CS from "../../libraries/cs";


let aDataModelForClassType = [];

let getDataModelForClassType = function () {
  return aDataModelForClassType;
};

let setDataModelForClassType = function (aAllowedEntityIds) {
  const aPreviousDataModelForClassType = [
    {
      "id": ClassTypeDictionary.ARTICLE,
      "label": oTranslations().ARTICLES_LABEL,
      "value": MockDataForEntityBaseTypesDictionary.articleKlassBaseType
    },
    {
      "id": ClassTypeDictionary.ASSET,
      "label": oTranslations().ASSETS,
      "value": MockDataForEntityBaseTypesDictionary.assetKlassBaseType
    },
    {
      "id": ClassTypeDictionary.MARKET,
      "label": oTranslations().MARKET,
      "value": MockDataForEntityBaseTypesDictionary.marketKlassBaseType
    },
    {
      "id": ClassTypeDictionary.TEXT_ASSET,
      "label": oTranslations().TEXT_ASSET,
      "value": MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType
    },
    {
      "id": ClassTypeDictionary.SUPPLIER,
      "label": oTranslations().SUPPLIER,
      "value": MockDataForEntityBaseTypesDictionary.supplierKlassBaseType
    },
  ];

  aDataModelForClassType = CS.filter(aPreviousDataModelForClassType, function (oClassType) {
    return CS.includes(aAllowedEntityIds, oClassType.id);
  });
};

export default getDataModelForClassType;
export const setDataModelForClassTypeList = setDataModelForClassType;