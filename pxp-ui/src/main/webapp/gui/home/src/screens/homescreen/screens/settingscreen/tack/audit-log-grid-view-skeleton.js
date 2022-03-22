import {getTranslations} from "../../../../../commonmodule/store/helper/translation-manager";
import {gridViewPropertyTypes as oGridViewPropertyTypes} from "../../../../../viewlibraries/tack/view-library-constants";

/** id is Searchable filter **/
let AuditLogGridSkeleton = function () {
  return {
    fixedColumns: [],
    scrollableColumns: [
      {
        id: "activityId",
        label: getTranslations().ACTIVITY_NUMBER,
        type: oGridViewPropertyTypes.NUMBER,
        width: 150,
        isSortable: true,
        isFilterable: false,
        isSearchable: true,
        requestId: "activityId"
      },
      {
        id: "userName",
        label: getTranslations().USERNAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 150,
        isSortable: true,
        isFilterable: true,
        isSearchable: false,
        requestId: "userName",
      },
      {
        id: "activity",
        label: getTranslations().ACTIVITY,
        type: oGridViewPropertyTypes.TEXT,
        width: 150,
        isSortable: true,
        isFilterable: true,
        isSearchable: false,
        requestId: "activities",
      },
      {
        id: "entityType",
        label: getTranslations().ENTITY,
        type: oGridViewPropertyTypes.TEXT,
        width: 150,
        isSortable: true,
        isFilterable: true,
        isSearchable: false,
        requestId: "entityTypes",
      },
      {
        id: "element",
        label: getTranslations().ELEMENT,
        type: oGridViewPropertyTypes.TEXT,
        width: 150,
        isSortable: true,
        isFilterable: true,
        isSearchable: false,
        requestId: "elements",
      },
      {
        id: "elementType",
        label: getTranslations().TYPE,
        type: oGridViewPropertyTypes.TEXT,
        width: 150,
        isSortable: true,
        isFilterable: true,
        isSearchable: false,
        requestId: "elementTypes",
      },
      {
        id: "elementName",
        label: getTranslations().ELEMENT_NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true,
        isFilterable: false,
        isSearchable: true,
        requestId: "elementName",
      },
      {
        id: "elementCode",
        label: getTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 300,
        isSortable: true,
        isFilterable: false,
        isSearchable: true,
        requestId: "elementCode",
      },
      {
        id: "date",
        label: getTranslations().TIMESTAMP,
        type: oGridViewPropertyTypes.DATETIME,
        width: 200,
        isSortable: true,
        isFilterable: true,
        isSearchable: false,
        requestId: "date",
      },
      {
        id: "ipAddress",
        label: getTranslations().IP_ADDRESS,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true,
        isFilterable: false,
        isSearchable: true,
        requestId: "ipAddress",
      }
    ],
    actionItems: [],
    selectedContentIds: []
  }
};

export default AuditLogGridSkeleton;