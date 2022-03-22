import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

var TranslationsConfigGridViewSkeleton = function () {
  return {
    fixedColumns: [
      /*{
        id: "code",
        label: "",
        type: oGridViewPropertyTypes.TEXT,
        width: 300,
        isSortable: false
      },*/
      {
        id: "languageKey",
        label: "Key",
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: false
      },
      {
        id: "#default#",
        label: "Default Translation",
        type: oGridViewPropertyTypes.TEXT,
        width: 300,
        isSortable: false
      },
      {
        id: "label",
        label: "Label",
        type: oGridViewPropertyTypes.TEXT,
        width: 300,
        isSortable: true,
        hideColumn: true
      },
      {
        id: "defaultLanguage",
        label: "DEFAULT LANGUAGE",
        type: oGridViewPropertyTypes.TEXT,
        width: 300,
        isSortable: true
      }
    ],
    scrollableColumns: [],
    actionItems: [],
    selectedContentIds: []
  }
};

export default TranslationsConfigGridViewSkeleton;