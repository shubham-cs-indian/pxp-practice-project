import {getTranslations as oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';

let TaxonomyPermissionTabLayoutData = function () {
  return {
    tabList: [
      {
        id: "majorTaxonomy",
        label: oTranslations().ATTRIBUTIONTAXONOMY,
        className: "masterTaxonomiesTabIcon"
      },
      {
        id: "minorTaxonomy",
        label: oTranslations().MINOR_TAXONOMY,
        className: "minorTaxonomiesTabIcon"
      }
    ]
  };
};

export default TaxonomyPermissionTabLayoutData;