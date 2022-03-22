import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../viewlibraries/tack/view-library-constants';

var ProcessConfigGridViewSkeleton = function () {
  return{
    fixedColumns: [
      {
        id: "label",
        label: oTranslations().NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: true,
      },
      {
        id: "code",
        label: oTranslations().CODE,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
      },
    ],
    scrollableColumns: [
      {
        id: "eventType",
        label: oTranslations().EVENT_TYPE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        width: 200,
        isSortable: false,
        isFilterable: true,
        isSearchable: false,
        requestId: "eventType",
      },
      {
        id: "workflowType",
        label: oTranslations().WORKFLOW_TYPE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        width: 200,
        isSortable: false,
        isFilterable: true,
        isSearchable: false,
        requestId: "workflowType",
      },
      {
        id: "triggeringType",
        label: oTranslations().TRIGGERING_EVENT,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        width: 200,
        isSortable: false,
        isFilterable: true,
        isSearchable: false,
        requestId: "triggeringType",
      },
      {
        id: "activation",
        label: oTranslations().ACTIVATION,
        type: oGridViewPropertyTypes.YES_NO,
        width: 100,
        isSortable: false,
        isFilterable: true,
        isSearchable: false,
        requestId: "activation",
      },
      {
        id: "natureType",
        label: oTranslations().NATURE_TYPE,
        type: oGridViewPropertyTypes.LAZY_MSS,
        width: 200,
      },
      {
        id: "attributes",
        label: oTranslations().ATTRIBUTES,
        type: oGridViewPropertyTypes.LAZY_MSS,
        width: 200,
      },
      {
        id: "tags",
        label: oTranslations().TAGS,
        type: oGridViewPropertyTypes.LAZY_MSS,
        width: 200,
      },
      {
        id: "taxonomy",
        label: oTranslations().TAXONOMY,
        type: oGridViewPropertyTypes.TAXONOMY,
        width: 200,
      },
      {
        id: "physicalCatalogIds",
        label: oTranslations().PHYSICAL_CATALOG,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        width: 200,
        isSortable: false,
        isFilterable: true,
        isSearchable: false,
        requestId: "physicalCatalogIds"
      },
      {
        id: "organizations",
        label: oTranslations().ORGANISATION,
        type: oGridViewPropertyTypes.LAZY_MSS,
        width: 200,
      },
      {
        id: "ip",
        label: oTranslations().IP_ADDRESS,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
      },
      {
        id: "port",
        label: oTranslations().PORT,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
      },
      {
        id: "queue",
        label: oTranslations().QUEUE_NAME,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
      },
      /*{
        id: "timerDefinitionType",
        label: oTranslations().TIME_DEFINITION_TYPE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        width: 200,
        isSortable: false,
        isFilterable: true,
        isSearchable: false,
        requestId: "timerDefinitionType"
      },
      {
        id: "timerStartExpression",
        label: oTranslations().TIME_START_EXPRESSION,
        type: oGridViewPropertyTypes.TEXT,
        width: 200,
        isSortable: false,
        isFilterable: false,
        isSearchable: true,
        requestId: "timerStartExpression",
      },*/
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
export default ProcessConfigGridViewSkeleton;
