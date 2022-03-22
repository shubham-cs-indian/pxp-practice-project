import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const processConfigLayoutData = function () {
  return {
    processBasicInformation: [
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
            label: oTranslations().WORKFLOW_TYPE,
            key: "workflowType",
            type: "mss",
            width: 33
          },
          {
            id: "1.4",
            label: oTranslations().EVENT_TYPE,
            key: "eventType",
            type: "mss",
            width: 25
          },
          {
            id: "1.7",
            label: oTranslations().NATURE_TYPE,
            key: "klassTypes",
            type: "lazyMSS",
            width: 25
          },
          {
            id: "1.5",
            label: oTranslations().ACTION_TYPE,
            key: "triggeringEventType",
            type: "mss",
            width: 25
          },
          {
            id: "1.6",
            label: oTranslations().ACTION_SUBTYPE,
            key: "actionSubType",
            type: "mss",
            width: 25
          },
          {
            id: "1.9",
            label: oTranslations().ATTRIBUTES,
            key: "attributes",
            type: "lazyMSS",
            width: 25
          },
          {
            id: "1.10",
            label: oTranslations().TAGS,
            key: "tags",
            type: "lazyMSS",
            width: 25
          },
          {
            id: "1.8",
            label: oTranslations().NON_NATURE_TYPE,
            key: "nonNatureklassTypes",
            type: "lazyMSS",
            width: 25
          },
          {
            id: "1.11",
            label: oTranslations().TAXONOMY,
            key: "taxonomy",
            type: "customView",
            width: 25
          },
          {
            id: "1.14",
            label: oTranslations().IP_ADDRESS,
            key: "ip",
            type: "singleText",
            width: 25,
            hintText: "Enter IP Address"
          },
          {
            id: "1.15",
            label: oTranslations().PORT,
            key: "port",
            type: "singleText",
            width: 25,
            hintText: "Enter Port"
          },
          {
            id: "1.16",
            label: oTranslations().QUEUE_NAME,
            key: "queue",
            type: "singleText",
            width: 25,
            hintText: "Enter Queue"
          },
          {
            id: "1.18",
            label: oTranslations().TEMPLATE,
            key: "isTemplate",
            type: "yesNo",
            width: 25,
          },
          {
            id: "1.19",
            label: oTranslations().PHYSICAL_CATALOG,
            key: "physicalCatalogIds",
            type: "mss",
            width: 25,
          },
          {
            id: "1.20",
            label: oTranslations().ORGANISATION,
            key: "organizations",
            type: "lazyMSS",
            width: 25,
          },
          {
            id: "1.21",
            label: "Usecases",
            key: "usecases",
            type: "mss",
            width: 25,
          },
        ]
      },
      {
        id: "2",
        label:  oTranslations().FREQUENCY,
        elements: [
          {
            id: "2.1",
            label: oTranslations().FREQUENCY,
            key: "tabSummary",
            type: "frequencyTabSummary",
            width: 100
          }
        ]
      },
      {
        id: "3",
        label: oTranslations().GRAPH,
        elements: [
          {
            id: "3.1",
            label: oTranslations().GRAPH,
            key: "graph",
            type: "customView",
            width: 100
          }
        ]
      }
    ],
  }
};

export default processConfigLayoutData;