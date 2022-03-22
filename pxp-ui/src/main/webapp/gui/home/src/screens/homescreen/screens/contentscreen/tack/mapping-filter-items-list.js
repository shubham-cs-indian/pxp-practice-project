import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import oFilterItemsDictioanry from './mapping-filter-items-dictionary';

let aFilterItems = function () {
  return [
    {
      id: oFilterItemsDictioanry.ALL,
      label: getTranslations().ALL,
      iconClassName: "allPropertyIcon"
    },
    {
      id: oFilterItemsDictioanry.MAPPED,
      label: getTranslations().MAPPED,
      iconClassName: "mappedPropertyIcon"
    },
    {
      id: oFilterItemsDictioanry.UNMAPPED,
      label: getTranslations().UNMAPPED,
      iconClassName: "unmappedPropertyIcon"
    },
    {
      id: oFilterItemsDictioanry.IGNORED,
      label: getTranslations().IGNORED,
      iconClassName: "ignorePropertyIcon"
    }
  ];
};

export default aFilterItems;