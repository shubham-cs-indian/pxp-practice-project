import {getTranslations} from './../../../../../../../commonmodule/store/helper/translation-manager.js';
import {gridViewPropertyTypes as oGridViewPropertyTypes} from './../../../../../../../viewlibraries/tack/view-library-constants';
import ConfigDataEntitiesDictionary from "../../../../../../../commonmodule/tack/config-data-entities-dictionary";
import {getTranslations as oTranslations} from "../../../../../../../commonmodule/store/helper/translation-manager";
import {gridViewFilterTypes as oGridViewFilterTypes} from "../../../../../../../viewlibraries/tack/view-library-constants";

let BackgroundProcessGridSkeleton = function () {
  return {
    fixedColumns: [],
    scrollableColumns: [
      {
        id: "jobId",
        label: getTranslations().JOB_ID,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        isDisabled: true,
        width: 200,
      },
      {
        id: "service",
        label: getTranslations().SERVICE,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        isDisabled: true,
        width: 200,
      },
      {
        id: "progress",
        label: getTranslations().PROGRESS_BAR,
        type: oGridViewPropertyTypes.BAR,
        isVisible: true,
        isDisabled: true,
        width: 200,
      },
      {
        id: "status",
        label: getTranslations().STATUS,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        isDisabled: true,
        width: 250
      },
      {
        id: "created",
        label: getTranslations().CREATED_DATE,
        isSortable: true,
        type: oGridViewPropertyTypes.DATE,
        isVisible: true,
        isDisabled: true,
        width: 200
      },
      {
        id: "createdBy",
        label: getTranslations().CREATED_BY,
        type: oGridViewPropertyTypes.TEXT,
        isVisible: true,
        isFilterable: true,
        filterType: oGridViewFilterTypes.DROP_DOWN,
        filterData: {
          id: "createdBy",
          customPlaceHolder: getTranslations().SELECT,
          isMultiSelect: true,
          selectedItems: [],
          disabled: false,
          cannotRemove: false,
          anchorOrigin: {horizontal: 'left', vertical: 'bottom'},
          targetOrigin: {horizontal: 'left', vertical: 'top'},
          showColor: true,
          referencedData: [],
          isFilterApplied: false,
          requestResponseInfo: {
            requestType: "configData",
            entityName: ConfigDataEntitiesDictionary.USERS,
          },
        },
        isDisabled: true,
        width: 200
      },

    ],
    actionItems: [
      {
        id: "view",
        label: oTranslations().VIEW,
        class: "viewIcon"
      },
    ],
    selectedContentIds: []
  }
};

export default BackgroundProcessGridSkeleton;