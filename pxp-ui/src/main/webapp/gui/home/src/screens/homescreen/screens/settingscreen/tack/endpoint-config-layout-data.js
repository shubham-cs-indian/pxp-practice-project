let oTranslations = require('../../../../../commonmodule/store/helper/translation-manager.js').getTranslations;

const endpointConfigLayoutData = function () {
  return {
    endpointBasicInformation: [
      {
        id: "1",
        label: oTranslations().BASIC_INFORMATION,
        elements: [
          {
            id: "1.1",
            label: oTranslations().NAME,
            key: "label",
            type: "singleText",
            width: 33
          },
          {
            id: "1.2",
            label: oTranslations().CODE,
            key: "code",
            type: "singleText",
            width: 33,
            isSortable: true
          },
          {
            id: "1.3",
            label: oTranslations().TYPE,
            key: "endpointType",
            type: "mss",
            width: 33
          },
          {
            id: "1.4",
            label: oTranslations().REALTIME_MAPPING,
            key: "isRuntimeMappingEnabled",
            type: "yesNo",
            width: 33
          },
          {
            id: "1.5",
            label: oTranslations().SYSTEM,
            key: "systemId",
            type: "mss",
            width: 33
          },
          {
            id: "1.6",
            label: oTranslations().INTEGRATION_WORKFLOW,
            key: "processes",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "2.7",
            label: oTranslations().JMS_WORKFLOW,
            key: "jmsProcesses",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "1.7",
            label: oTranslations().MAPPING,
            key: "mapping",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "1.8",
            label: oTranslations().DASHBOARD_TAB,
            key: "dashboardTab",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "1.9",
            label: oTranslations().PHYSICAL_CATALOG,
            key: "physicalCatalog",
            type: "mss",
            width: 33
          },
          {
            id: "1.10",
            label: oTranslations().NATURE_TYPE,
            key: "klassTypes",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "2.1",
            label: oTranslations().ATTRIBUTES,
            key: "attributes",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "2.2",
            label: oTranslations().TAGS,
            key: "tags",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "2.3",
            label: oTranslations().TAXONOMY,
            key: "taxonomy",
            type: "customView",
            width: 33
          },
          {
            id: "2.4",
            label: oTranslations().AUTHORIZATION,
            key: "authorizationMapping",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "2.5",
            label: oTranslations().IMPORT_WRITE_CATALOGUE,
            key: "inboundDataCatalog",
            type: "mss",
            width: 33
          },
          {
            id: "2.6",
            label: oTranslations().EXPORT_READ_CATALOGUE,
            key: "outboundDataCatalog",
            type: "mss",
            width: 33
          },
        ]
      }
    ],
  }
};

module.exports = endpointConfigLayoutData;