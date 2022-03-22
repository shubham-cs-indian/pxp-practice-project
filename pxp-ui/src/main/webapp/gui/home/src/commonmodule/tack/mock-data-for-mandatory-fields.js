import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';

const mandatoryFields = function () {
  return [
    {
      id: "can",
      label: oTranslations().CAN
    },
    {
      id: "should",
      label: oTranslations().SHOULD
    },
    {
      id: "must",
      label: oTranslations().MUST
    }
  ];
};
export default mandatoryFields;