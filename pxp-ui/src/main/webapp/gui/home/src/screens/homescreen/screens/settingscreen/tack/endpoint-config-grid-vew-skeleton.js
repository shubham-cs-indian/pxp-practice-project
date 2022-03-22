import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../viewlibraries/tack/view-library-constants';

var EndpointConfigGridSkeleton = function () {
  return{
    fixedColumns: [
      {
        id: "icon",
        label: oTranslations().ICON,
        type: oGridViewPropertyTypes.IMAGE,
        isVisible: true,
        width: 80
      },
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
    ],
    scrollableColumns: [
      {
        id: "endpointType",
        label: oTranslations().TYPE,
        type: oGridViewPropertyTypes.DROP_DOWN,
        width: 200
      },
      {
        id: "isRuntimeMappingEnabled",
        label: oTranslations().REALTIME_MAPPING,
        type: oGridViewPropertyTypes.YES_NO,
        isVisible: true,
        width: 150
      },
      /*{
        id: "dataRules",
        label: oTranslations().RULES,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 150
      },*/
      {
        id: "systemId",
        label: oTranslations().SYSTEM,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
      {
        id: "processes",
        label: oTranslations().INTEGRATION_WORKFLOW,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 150
      },
      {
        id: "jmsProcesses",
        label: oTranslations().JMS_WORKFLOW,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 150
      },
      {
        id: "mappings",
        label: oTranslations().MAPPING,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 150
      },
      {
        id: "authorizationMapping",
        label: oTranslations().AUTHORIZATION,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 150
      },
      {
        id: "dashboardTab",
        label: oTranslations().DASHBOARD_TAB,
        type: oGridViewPropertyTypes.LAZY_MSS,
        isVisible: true,
        width: 150
      },
      {
        id: "physicalCatalogs",
        label: oTranslations().PHYSICAL_CATALOG,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
/*
      {
        id: "inboundDataCatalogIds",
        label: oTranslations().IMPORT_WRITE_CATALOGUE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
      {
        id: "outboundDataCatalogIds",
        label: oTranslations().EXPORT_READ_CATALOGUE,
        type: oGridViewPropertyTypes.NATIVE_DROP_DOWN,
        isVisible: true,
        width: 150
      },
      {
        id: "natureType",
        label: oTranslations().NATURE_TYPE,
        type: oGridViewPropertyTypes.LAZY_MSS,
        width: 200
      },
      {
        id: "attributes",
        label: oTranslations().ATTRIBUTES,
        type: oGridViewPropertyTypes.LAZY_MSS,
        width: 200
      },
      {
        id: "tags",
        label: oTranslations().TAGS,
        type: oGridViewPropertyTypes.LAZY_MSS,
        width: 200
      },
      {
        id: "taxonomy",
        label: oTranslations().TAXONOMY,
        type: oGridViewPropertyTypes.TAXONOMY,
        width: 200
      }*/
    ],
    actionItems: [
      {
        id: "edit",
        label: oTranslations().EDIT,
        class: "editIcon"
      },
      {
        id: "delete",
        label: oTranslations().END_POINT_DELETE_MENU_ITEM_TITLE,//TODO: change translation
        class: "deleteIcon"
      },
      {
        id: "manageEntity",
        label: oTranslations().MANAGE_ENTITY_USAGE,
        class: "manageEntity"
      },
    ],
    selectedContentIds: []
  }
};

export default EndpointConfigGridSkeleton;