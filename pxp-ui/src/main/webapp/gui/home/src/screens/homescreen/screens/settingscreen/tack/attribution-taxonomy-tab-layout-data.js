import {getTranslations as oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';

let AttributionTaxonomiesTabLayoutData = function () {
  return {
    tabList: [
      {
        id: "Master_Taxonomies",
        label: oTranslations().MASTER_TAXONOMY_CONFIGURATION_TITLE,
        className: "MasterTaxonomiesTabIcon"
      },
      {
        id: "Minor_Taxonomies",
        label: oTranslations().MINOR_TAXONOMIES,
        className: "MinorTaxonomiesTabIcon"
      }
    ]
  };
};

export default AttributionTaxonomiesTabLayoutData;