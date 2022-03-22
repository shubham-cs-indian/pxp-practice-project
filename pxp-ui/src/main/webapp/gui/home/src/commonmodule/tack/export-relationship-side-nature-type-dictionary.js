import NatureTypeDictionary from "./nature-type-dictionary";

const oExportRelationshipSideNatureTypeDictionary = function () {
  return {
    natureTypeDictionary: [
      NatureTypeDictionary.PID_SKU,
      NatureTypeDictionary.IMAGE_ASSET,
      NatureTypeDictionary.DOCUMENT_ASSET,
      NatureTypeDictionary.VIDEO_ASSET,
      NatureTypeDictionary.FIXED_BUNDLE,
      NatureTypeDictionary.SET_OF_PRODUCTS,
      NatureTypeDictionary.SINGLE_ARTICLE,
      NatureTypeDictionary.FILE_ASSET,
    ]
  };
};

export default oExportRelationshipSideNatureTypeDictionary;