import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const kpiDetailConfigLayoutData = function () {
  return {
    kpiInformation: [
      {
        id: "1",
        label: oTranslations().BASIC_INFORMATION,
        elements: [
          {
            id: "1",
            label: oTranslations().NAME,
            key: "label",
            type: "singleText",
            width: 100
          },
          {
            id: "2",
            label: oTranslations().DASHBOARD_TAB,
            key: "dashboardTab",
            type: "lazyMSS",
            width: 100
          },
          {
            id: '3',
            label: oTranslations().PHYSICAL_CATALOG,
            key: 'physicalCatalogId',
            type: 'selectionToggle',
            width: 50
          },
          {
            id: '5',
            label: oTranslations().PORTAL,
            key: 'portalId',
            type: 'selectionToggle',
            width: 50
          },
          {
            id: '4',
            label: oTranslations().PARTNERS,
            key: 'partners',
            type: 'lazyMSS',
            width: 50 //50
          },
          {
            id: '4',
            label: oTranslations().ENDPOINTS,
            key: 'endpoints',
            type: 'lazyMSS',
            width: 50
          },
          // TODO: UnComment after Feature Implementation From Backend
          // {
          //   id: "2",
          //   label: oTranslations().TAGS,
          //   key: "tags",
          //   type: "customView",
          //   width: 100
          // },
        ]
      }
    ],
    target: [
      {
        id: "1",
        label: "Target",
        elements: [
          {
            id: "3",
            label: oTranslations().CLASSES,
            key: "klassIds",
            type: "lazyMSS",
            width: 100
          },
          {
            id: "4",
            label: oTranslations().TAXONOMIES,
            key: "taxonomy",
            type: "customView",
            width: 100
          }
        ]
      }
    ],

    // TODO: UnComment after Feature Implementation From Backend
    // frequency: [
    //   {
    //     id: "1",
    //     label: oTranslations().ATTRIBUTE_TYPES_FREQUENCY,
    //     elements: [
    //       {
    //         id: "3",
    //         label: oTranslations().ATTRIBUTE_TYPES_FREQUENCY,
    //         key: "frequency",
    //         type: "mss",
    //         width: 100
    //       }
    //     ]
    //   }
    // ],

    // TODO: UnComment after Feature Implementation From Backend
    // roles:
    //     [
    //       {
    //         id: "4",
    //         label: oTranslations().ROLES,
    //         elements: [
    //           {
    //             id: "41",
    //             label: oTranslations().RESPONSIBLE,
    //             key: "responsiblerole",
    //             type: "mss",
    //             width: 50
    //           },
    //           {
    //             id: "42",
    //             label: oTranslations().ACCOUNTABLE,
    //             key: "accountablerole",
    //             type: "mss",
    //             width: 50
    //           },
    //           {
    //             id: "43",
    //             label: oTranslations().INFORMED,
    //             key: "informedrole",
    //             type: "mss",
    //             width: 50
    //           },
    //           {
    //             id: "44",
    //             label: oTranslations().VERIFIED,
    //             key: "verifyrole",
    //             type: "mss",
    //             width: 50
    //           },
    //           {
    //             id: "45",
    //             label: oTranslations().SIGNOFF,
    //             key: "signoffrole",
    //             type: "mss",
    //             width: 50
    //           },
    //           {
    //             id: "46",
    //             label: oTranslations().CONSULTED,
    //             key: "consultedrole",
    //             type: "mss",
    //             width: 50
    //           }
    //         ]
    //       }
    //     ],

    levels: [
      {
        id: "1",
        label: oTranslations().DRILL_DOWN_LEVEL_CONFIGURATION,
        elements: [
          {
            id: "3",
            label: oTranslations().DRILL_DOWN_LEVELS,
            key: "drillDowns",
            type: "customView",
            width: 100
          }
        ]
      }
    ],

    completenessBlock: [
      {
        id: "1",
        label: oTranslations().COMPLETENESS,
        elements: [
          // TODO: UnComment after Feature Implementation From Backend
          // {
          //   id: '2',
          //   label: oTranslations().THRESHOLD,
          //   key: 'threshold',
          //   type: 'customView',
          //   width: 25
          // },
          // {
          //   id: '3',
          //   label: oTranslations().COLOR,
          //   key: 'color',
          //   type: 'mss',
          //   width: 25
          // },
          // {
          //   id: '3',
          //   label: oTranslations().TASK,
          //   key: 'task',
          //   type: 'mss',
          //   width: 25
          // },
          {
            id: '4',
            label: oTranslations().RULES,
            key: 'rules',
            type: 'customView',
            width: 100
          }
        ]
      }
    ],
    completeness: [
      {
        id: "1",
        label: oTranslations().COMPLETENESS,
        elements: [
          {
            id: '1',
            label: oTranslations().LABEL,
            key: 'label',
            type: 'singleText',
            width: 100 //50
          }, {
            id: '2',
            label: oTranslations().ATTRIBUTES,
            key: 'attributes',
            type: 'lazyMSS',
            width: 100 //50
          }, {
            id: '3',
            label: oTranslations().TAGS,
            key: 'tags',
            type: 'lazyMSS',
            width: 100 //50
          },
          // TODO: UnComment after Feature Implementation From Backend
          // {
          //   id: '4',
          //   label: oTranslations().RELATIONSHIP,
          //   key: 'relationships',
          //   type: 'mss',
          //   width: 50
          // }
        ]
      }
    ],
    accuracyBlock: [
      {
        id: "1",
        label: oTranslations().ACCURACY,
        elements: [
          // TODO: UnComment after Feature Implementation From Backend
          // {
          //   id: '2',
          //   label: oTranslations().THRESHOLD,
          //   key: 'threshold',
          //   type: 'customView',
          //   width: 25
          // },
          // {
          //   id: '3',
          //   label: oTranslations().COLOR,
          //   key: 'color',
          //   type: 'mss',
          //   width: 25
          // },
          // {
          //   id: '3',
          //   label: oTranslations().TASK,
          //   key: 'task',
          //   type: 'mss',
          //   width: 25
          // },
          {
            id: '4',
            label: oTranslations().RULES,
            key: 'rules',
            type: 'customView',
            width: 100
          }
        ]
      }
    ],
    accuracy: [
      {
        id: "1",
        label: oTranslations().ACCURACY,
        elements: [
          {
            id: '1',
            label: oTranslations().LABEL,
            key: 'label',
            type: 'singleText',
            width: 100
          },
          {
            id: '2',
            label: oTranslations().RULES,
            key: 'rules',
            type: 'customView',
            width: 100
          }
        ]
      }
    ],
    conformityBlock: [
      {
        id: "1",
        label: oTranslations().CONFORMITY,
        elements: [
          // TODO: UnComment after Feature Implementation From Backend
          // {
          //   id: '2',
          //   label: oTranslations().THRESHOLD,
          //   key: 'threshold',
          //   type: 'customView',
          //   width: 25
          // },
          // {
          //   id: '3',
          //   label: oTranslations().COLOR,
          //   key: 'color',
          //   type: 'mss',
          //   width: 25
          // },
          // {
          //   id: '3',
          //   label: oTranslations().TASK,
          //   key: 'task',
          //   type: 'mss',
          //   width: 25
          // },
          {
            id: '4',
            label: oTranslations().RULES,
            key: 'rules',
            type: 'customView',
            width: 100
          }
        ]
      }
    ],
    conformity: [
      {
        id: "1",
        label: oTranslations().CONFORMITY,
        elements: [
          {
            id: '1',
            label: oTranslations().LABEL,
            key: 'label',
            type: 'singleText',
            width: 100
          },
          {
            id: '@',
            label: oTranslations().RULES,
            key: 'rules',
            type: 'customView',
            width: 100
          }
        ]
      }
    ],
    uniquenessBlock: [
      {
        id: "1",
        label: oTranslations().UNIQUENESS,
        elements: [
          // TODO: UnComment after Feature Implementation From Backend
          // {
          //   id: '2',
          //   label: oTranslations().THRESHOLD,
          //   key: 'threshold',
          //   type: 'customView',
          //   width: 25
          // },
          // {
          //   id: '3',
          //   label: oTranslations().COLOR,
          //   key: 'color',
          //   type: 'mss',
          //   width: 25
          // },
          // {
          //   id: '3',
          //   label: oTranslations().TASK,
          //   key: 'task',
          //   type: 'mss',
          //   width: 25
          // },
          {
            id: '4',
            label: oTranslations().RULES,
            key: 'rules',
            type: 'customView',
            width: 100
          }
        ]
      }
    ],
    uniqueness: [
      {
        id: "1",
        label: oTranslations().UNIQUENESS,
        elements: [
          {
            id: '1',
            label: oTranslations().LABEL,
            key: 'label',
            type: 'singleText',
            width: 100 //50
          }, {
            id: '2',
            label: oTranslations().ATTRIBUTES,
            key: 'attributes',
            type: 'lazyMSS',
            width: 100 //50
          }, {
            id: '3',
            label: oTranslations().TAGS,
            key: 'tags',
            type: 'lazyMSS',
            width: 100 //50
          },
          // TODO: UnComment after Feature Implementation From Backend
          // {
          //   id: '4',
          //   label: oTranslations().RELATIONSHIP,
          //   key: 'relationships',
          //   type: 'mss',
          //   width: 50
          // }
        ]
      }
    ],
  };
};
export default kpiDetailConfigLayoutData;