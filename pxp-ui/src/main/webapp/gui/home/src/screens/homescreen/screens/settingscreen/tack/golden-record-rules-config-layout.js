import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const goldenRecordsRulesConfigData = function () {
  return {
    goldenRecordsRulesConfigMatchInformation: [
      {
        id: "1",
        label: oTranslations().MATCH,
        elements: [
          {
            id: "1",
            label: oTranslations().TAGS,
            key: "tags",
            type: "lazyMSS",
            width: 50
          },
          {
            id: "2",
            label: oTranslations().ATTRIBUTES,
            key: "attributes",
            type: "lazyMSS",
            width: 50
          },
          {
            id: "3",
            label: oTranslations().NATURE_CLASS,
            key: 'natureKlasses',
            type: 'lazyMSS',
            width: 50
          },
          {
            id: "4",
            label: oTranslations().NON_NATURE_CLASSES,
            key: 'nonNatureKlasses',
            type: 'lazyMSS',
            width: 50
          },
          {
            id: "5",
            label: oTranslations().TAXONOMIES,
            key: "taxonomies",
            type: "customView",
            width: 50
          },
          {
            id: "6",
            label: 'Auto Create',
            key: 'isAutoCreate',
            type: 'yesNo',
            width: 50
          },

        ]
      }
    ],

    goldenRecordsRulesConfigMergeInformation: [
      {
        id: "1",
        label: oTranslations().MERGE,
        elements: [
          {
            id: "1",
            label: "",
            key: "attributes",
            type: "customView",
            width: 100
          },
          {
            id: "2",
            label: "",
            key: "tags",
            type: "customView",
            width: 100
          },
          {
            id: "3",
            label: "",
            key: 'relationships',
            type: 'customView',
            width: 100
          },
          {
            id: "4",
            label: "",
            key: 'natureRelationships',
            type: 'customView',
            width: 100
          },
        ]
      }
    ],
    goldenRecordsRulesConfigMergeDetailsInformation: [
      {
        id: "1",
        label: "",
        elements: [
          {
            id: "1",
            label: "",
            key: "latest",
            type: "customView",
            width: 100
          },
          {
            id: "2",
            label: "",
            key: "suppliers",
            type: "customView",
            width: 100
          },
        ]
      }
    ],
  };
};
export default goldenRecordsRulesConfigData;