import {getTranslations as oTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';

let MultiClassificationDialogToolbarLayoutData = function () {
  return {
    multiClassificationToolbar: [
      {
        id: "classes",
        isActive: true,
        label: oTranslations().CLASSES,
        showLabel: true
      },
      {
        id: "taxonomies",
        isActive: false,
        label: oTranslations().TAXONOMIES,
        showLabel: true
      }
    ],

    multiClassificationViewTypesIds: {
      CLASSES: "classes",
      TAXONOMIES: "taxonomies"
    }
  };
};

export default MultiClassificationDialogToolbarLayoutData;