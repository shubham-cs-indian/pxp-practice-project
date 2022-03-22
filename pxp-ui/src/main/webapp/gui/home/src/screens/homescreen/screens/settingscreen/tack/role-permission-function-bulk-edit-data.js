import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';

let aBulkEditList = function () {
  return {
    "canBulkEditProperties":
        {
          id: 'canBulkEditProperties',
          label: getTranslations().PROPERTIES,
          permission: {
            type: 'properties'
          }
        },
    "canBulkEditTaxonomies":
        {
          id: 'canBulkEditTaxonomies',
          label: getTranslations().TAXONOMIES,
          permission: {
            type: 'taxonomies',
          }
        },
    "canBulkEditClasses":
        {
          id: 'canBulkEditClasses',
          label: getTranslations().CLASSES,
          permission: {
            type: 'classes',
          }
        },
  };
};

export default aBulkEditList;