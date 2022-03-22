import {getTranslations as oTranslations} from '../store/helper/translation-manager.js';

const oTaxonomyInheritanceList = function () {
  return [
    {
      id: "off",
      label: oTranslations().OFF
    },
    {
      id: "manual",
      label: oTranslations().MANUAL_TAXONOMY_INHERITANCE
    },
    {
      id: "auto",
      label: oTranslations().AUTO
    }
  ];
};

export default oTaxonomyInheritanceList;