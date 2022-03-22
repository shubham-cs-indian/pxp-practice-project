

import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../viewlibraries/tack/view-library-constants'; //todo: find alternative

export default () => ({
  fixedColumns: [
    {
      id: "language",
      label: oTranslations().LANGUAGE_AVAILABLE,
      type: oGridViewPropertyTypes.TEXT,
      width: 250,
      isDisabled: true
    },
    {
      id: "defaultLanguage",
      label: oTranslations().DEFAULT_LANGUAGE,
      type: oGridViewPropertyTypes.YES_NO,
      width: 250,
      isDisabled: false
    }
  ],
  scrollableColumns: [],
  selectedContentIds: [],
  actionItems: [],
  showSubHeader: true
});
