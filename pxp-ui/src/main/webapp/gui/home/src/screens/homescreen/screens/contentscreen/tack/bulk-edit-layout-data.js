import {getTranslations as oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';

let oBulkEditTabTypesConstants = {
  PROPERTIES: "bulk_edit_properties",
  TAXONOMIES: "bulk_edit_taxonomies",
  CLASSES: "bulk_edit_classes"
};

let oBulkEditToolbarData = function () {
  return {
    bulkEditToolbar: [
      {
        id: oBulkEditTabTypesConstants.PROPERTIES,
        isActive: false,
        label: oTranslations().PROPERTIES,
        showLabel: true
      },
      {
        id: oBulkEditTabTypesConstants.TAXONOMIES,
        isActive: false,
        label: oTranslations().TAXONOMIES,
        showLabel: true
      },
      {
        id: oBulkEditTabTypesConstants.CLASSES,
        isActive: false,
        label: oTranslations().CLASSES,
        showLabel: true
      }
    ],
  }
};

export let bulkEditToolbarData = oBulkEditToolbarData;
export const bulkEditTabTypesConstants = oBulkEditTabTypesConstants;