import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';

const aDataModelTypeRelationshipList = function () {
  return [
    {
      id: "relationship",
      label: oTranslations().RELATIONSHIP
    },
    {
      id: "liteRelationship",
      label: oTranslations().LITE_RELATIONSHIP
    },
  ];
};

export default aDataModelTypeRelationshipList;
