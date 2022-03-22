import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

var KpiConfigGridViewSkeleton = function () {
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
        id: "dashboardTab",
        label: oTranslations().DASHBOARD_TAB,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 200
      },
      // {
      //   id: "emptySection",
      //   label: "",
      //   type: oGridViewPropertyTypes.TEXT,
      //   isDisabled: true,
      //   isVisible: true,
      //   width: 100
      // }
    //   {
    //     id: "frequency",
    //     label: oTranslations().FREQUENCY,
    //     type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
    //     isVisible: true,
    //     width: 200
    //   },
    //   {
    //     id: "responsible",
    //     label: oTranslations().RESPONSIBLE,
    //     type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
    //     isVisible: true,
    //     width: 200
    //   },
    //   {
    //     id: "accountable",
    //     label: oTranslations().ACCOUNTABLE,
    //     type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
    //     isVisible: true,
    //     width: 200
    //   },
    //   {
    //     id: "consulted",
    //     label: oTranslations().CONSULTED,
    //     type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
    //     isVisible: true,
    //     width: 200
    //   },
    //   {
    //     id: "informed",
    //     label: oTranslations().INFORMED,
    //     type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
    //     isVisible: true,
    //     width: 200
    //   },
    //   {
    //     id: "verifier",
    //     label: oTranslations().VERIFIED,
    //     type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
    //     isVisible: true,
    //     width: 200
    //   },
    //   {
    //     id: "signOff",
    //     label: oTranslations().SIGNOFF,
    //     type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
    //     isVisible: true,
    //     width: 200//,
    //   }
    ],
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
export default KpiConfigGridViewSkeleton;