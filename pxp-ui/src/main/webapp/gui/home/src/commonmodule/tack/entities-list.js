import oEntityBaseTypeDictionary from './mock-data-for-entity-base-types-dictionary';
import { getTranslations } from '../store/helper/translation-manager.js';
import CS from "../../libraries/cs";

let aEntityList = [];

let getAllowedEntities = function () {
  return aEntityList;
};

let setAllowedEntities = function (aAllowedEntityIds) {

  const aPreviousEntities = [
    {
      id: "ArticleInstance",
      label: getTranslations().ARTICLE,
      baseType: oEntityBaseTypeDictionary.contentBaseType
    },
    {
      id: "AssetInstance",
      label: getTranslations().ASSET,
      baseType: oEntityBaseTypeDictionary.assetBaseType
    },
    {
      id: "MarketInstance",
      label: getTranslations().MARKET,
      baseType: oEntityBaseTypeDictionary.marketBaseType
    },
    {
      id: "SupplierInstance",
      label: getTranslations().SUPPLIER,
      baseType: oEntityBaseTypeDictionary.supplierBaseType
    },
    {
      id: "TextAssetInstance",
      label: getTranslations().TEXT_ASSET,
      baseType: oEntityBaseTypeDictionary.textAssetBaseType
    },

  ];

  aEntityList = CS.filter(aPreviousEntities, function (oEntity) {
    return CS.includes(aAllowedEntityIds, oEntity.id);
  });
};

export default getAllowedEntities;
export const setAllowedEntitiesList = setAllowedEntities;