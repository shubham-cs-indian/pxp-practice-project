import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

var RulesConfigGridViewSkeleton = function () {
  return{
    fixedColumns: [],
    scrollableColumns: [
      {
        id: "label",
        label: oTranslations().NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      },
      {
        id: "code",
        label: oTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true
      },
      {
        id: "type",
        label: oTranslations().RULE_TYPE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        width: 100
      },
      {
        id: "isLanguageDependent",
        label: oTranslations().LANGUAGE_DEPENDENT,
        type: oGridViewPropertyTypes.YES_NO,
        width: 100
      },
      {
        id: "languages",
        label: oTranslations().LANGUAGES,
        type: oGridViewPropertyTypes.DROP_DOWN,
        width: 100
      },
      {
        id: "physicalCatalogIds",
        label: oTranslations().APPLIED_ON,
        type: oGridViewPropertyTypes.TEXT,
        width: 100
      }
    ],
   /* scrollableColumns: [
      {
        id: "partners",
        label: oTranslations().PARTNERS,
        type: oGridViewPropertyTypes.TEXT,
        width: 100
      }
      ,
      {
        id: "rule type",
        label: oTranslations().RULE_TYPE,
        type: oGridViewPropertyTypes.TEXT,
        width: 100
      }
      ,
      {
        id: "physical catalog",
        label: oTranslations().PHYSICAL_CATALOG,
        type: oGridViewPropertyTypes.TEXT,
        width: 100
      },
      {
        id: "endpoints",
        label: oTranslations().ENDPOINTS,
        type: oGridViewPropertyTypes.TEXT,
        width: 100
      }
    ],*/
    actionItems: [
      {
        id: "delete",
        label: oTranslations().DELETE,
        class: "deleteIcon"
      },
      {
        id: "edit",
        label: oTranslations().EDIT,
        class: "editIcon"
      }
    ],
    selectedContentIds: []
  }
};
export default RulesConfigGridViewSkeleton;