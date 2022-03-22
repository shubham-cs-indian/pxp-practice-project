import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

export default function () {

  return [
    {
      id: "kpiInformation",
      label: oTranslations().KPI_INFORMATION
    },

    {
      id: "completenessBlock",
      label: oTranslations().COMPLETENESS
    },

    {
      id: "conformityBlock",
      label: oTranslations().CONFORMITY
    },

    {
      id: "uniquenessBlock",
      label: oTranslations().UNIQUENESS
    },

    {
      id: "accuracyBlock",
      label: oTranslations().ACCURACY
    },
  ]
};
