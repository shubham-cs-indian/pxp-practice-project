import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import oFilterItemsDictioanry from './content-edit-filter-items-dictionary';

let aFilterItems = function () {
  return [
    {
      id: oFilterItemsDictioanry.ALL,
      label: getTranslations().ALL,
      iconClassName: "allPropertyIcon"
    },
    {
      id: oFilterItemsDictioanry.COMPLETE,
      label: getTranslations().CONTENT_FILTER_COMPLETE,
      iconClassName: "completePropertyIcon"
    },
    {
      id: oFilterItemsDictioanry.INCOMPLETE,
      label: getTranslations().CONTENT_FILTER_INCOMPLETE,
      iconClassName: "incompletePropertyIcon"
    },
    {
      id: oFilterItemsDictioanry.VIOLATED,
      label: getTranslations().CONTENT_FILTER_VIOLATED,
      iconClassName: "violatedPropertyIcon"
    },
    {
      id: oFilterItemsDictioanry.MANDATORY,
      label: getTranslations().MANDATORY,
      iconClassName: "mandatoryPropertyIcon"
    },
    {
      id: oFilterItemsDictioanry.DEPENDENT,
      label: getTranslations().LANGUAGE_DEPENDENT,
      iconClassName: "dependentPropertyIcon"
    }
  ];
};

export default aFilterItems;