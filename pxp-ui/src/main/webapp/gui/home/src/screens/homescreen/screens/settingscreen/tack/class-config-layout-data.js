import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';


const configLayoutData = function () {
  return {
    classBasicInformation: [
      {
        id: "1",
        label: oTranslations().BASIC_INFORMATION,
        elements: [
          {
            id: "11",
            label: oTranslations().NAME,
            key: "label",
            type: "singleText",
            width: 33
          },
          {
            id: "13.1",
            label: oTranslations().CODE,
            key: "code",
            type: "singleText",
            width: 33,
            isSortable: true
          },
          {
            id: "13",
            label: oTranslations().TYPE,
            key: "natureType",
            type: "mss",
            width: 33
          },
          {
            id: "14",
            label: oTranslations().MAX_VERSIONS,
            key: "numberOfVersionsToMaintain",
            type: "singleTextNumber",
            width: 33
          },
          {
            id: "21",
            label: oTranslations().RULES,
            key: "dataRules",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "24",
            label: oTranslations().LIFECYCLE_STATUS,
            key: "lifeCycleStatusTags",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "22",
            label: oTranslations().DEFAULT_OPTIONS,
            key: "defaultOptions",
            type: "selectionToggle",
            width: 33
          },
          {
            id: "7",
            label: oTranslations().ICON,
            key: "icon",
            type: "image",
            width: 33
          },
          {
            id: "8",
            label: oTranslations().PREVIEW_IMAGE,
            key: "previewImage",
            type: "image",
            width: 33
          },
          {
            id: "1",
            label: oTranslations().SOURCE_PRICE,
            key: "sourcePriceId",
            type: "lazyMSS",
            width: 50
          },
          {
            id: "2",
            label: oTranslations().TARGET_PRICE,
            key: "targetPriceId",
            type: "lazyMSS",
            width: 50
          },
          {
            id: "51",
            label: oTranslations().COLOR,
            key: "color",
            type: "colorPicker",
            width: 50
          },
          {
            id: "5",
            label: oTranslations().PRICE_CONTEXT_CONFIGURATION,
            key: "shouldCheck",
            type: "customView",
            width: 100
          },
          {
            id: "6",
            label: oTranslations().TARGET_PRICE_CONTEXT_CONFIGURATION,
            key: "targetContextSource",
            type: "customView",
            width: 100
          },
          {
            id: "19",
            label: oTranslations().RELATIONSHIPS,
            key: "natureRelationship",
            type: "customView",
            width: 100
          },
          {
            id: "44",
            label: oTranslations().TAXONOMY_TO_TRANSFER,
            key: "taxonomyTransfer",
            type: "lazyMSS",
            width: 33
          },
          {
            id: "21",
            label: oTranslations().CONTEXT_TYPES_PRODUCT_VARIANT,
            key: "productVariantContexts",
            type: "customView",
            width: 100
          },
          // {
          //   id: "20",
          //   label: oTranslations().CONTEXT_TYPES_CONTEXTUAL_VARIANT,
          //   key: "embeddedVariantContexts",
          //   type: "customView",
          //   width: 100
          // },
        /*  {
            id: "22",
            label: oTranslations().CONTEXT_TYPES_LANGUAGE_VARIANT,
            key: "languageVariantContexts",
            type: "customView",
            width: 100
          },*/
          {
            id: "23",
            label: oTranslations().CLONE_CONTEXT,
            key: "promotionalVersionContexts",
            type: "customView",
            width: 100
          },
          {
            id: "52",
            label: oTranslations().TRACK_DOWNLOADS,
            key: "trackDownloads",
            type: "yesNo",
            width: 100
          },
          {
            id: "23",
            label: oTranslations().TASKS,
            key: "tasks",
            type: "lazyMSS",
            width: 50
          },
          {
            id: "33",
            label: oTranslations().CONTEXT,
            key: "classContext",
            type: "customView",
            width: 50
          },
          {
            id: "35",
            label: oTranslations().GTIN,
            key: "gtinKlass",
            type: "customView",
            width: 100
          },
          {
            id: "36",
            label: oTranslations().EMBEDDED_CLASS,
            key: "embeddedKlass",
            type: "customView",
            width: 100
          },
          {
            id: "37",
            label: oTranslations().TECHNICAL_IMAGE,
            key: "technicalImageKlass",
            type: "lazyMSS",
            width: 100
          },
/*          {
            id: "44",
            label: oTranslations().LANGUAGE_CLASS,
            key: "languageKlass",
            type: "customView",
            width: 100
          },*/
          {
            id: "37",
            label: oTranslations().ADD_CLASS,
            key: "embeddedKlassDropdown",
            type: "lazyMSS",
            width: 100
          },
          {
            id: "40",
            label: oTranslations().ADD_CLASS,
            key: "unitKlassDropdown",
            type: "lazyMSS",
            width: 100
          },
          {
            id: "41",
            label: oTranslations().ADD_CLASS,
            key: "gtinKlassDropdown",
            type: "lazyMSS",
            width: 100
          },
          {
            id: "45",
            label: oTranslations().ADD_CLASS,
            key: "languageKlassDropdown",
            type: "lazyMSS",
            width: 100
          },
          {
            id: "42",
            label: oTranslations().CLASS,
            key: "classWrapper",
            type: "customView",
            width: 100
          },
          {
            id: "43",
            label: oTranslations().DATA_TRANSFER_PROPERTIES,
            key: "dataTransferProperties",
            type: "customView",
            width: 100
          },
          {
            id: "46",
            label: oTranslations().DETECT_DUPLICATE,
            key: "detectDuplicate",
            type: "yesNo",
            width: 50
          },
          {
            id: "47",
            label: oTranslations().EXTRACT_ZIP,
            key: "uploadZip",
            type: "yesNo",
            width: 50
          },
          {
            id: "48",
            label: oTranslations().VARIANT,
            key: "embeddedVariant",
            type: "customView",
            width: 100
          },
          {
            id: "51",
            label: oTranslations().INDESIGN_SERVER,
            key: "indesignServer",
            type: "yesNo",
            width: 100
          },
          {
            id: "52",
            label: "Target Configuration",
            key: "marketKlassTags",
            type: "customView",
            width: 100
          },
          {
            id: "53",
            label: "Add Market Class",
            key: "marketKlassDropdown",
            type: "lazyMSS",
            width: 100
          },
          {
            id: "54",
            label: "Market Name",
            key: "marketLabel",
            type: "singleText",
            width: 100
          },
          {
            id: "55",
            label: "Add Tags",
            key: "tagTransferProperties",
            type: "customView",
            width: 100
          },
        ]
      }
    ],
    classRelationship: [
      {
        id: "1",
        label: oTranslations().RELATIONSHIP,
        elements: [
          {
            id: "2",
            label: oTranslations().OTHER_SIDE,
            key: "side2klass",
            type: "customView",
            width: 50
          },
          {
            id: "2.5",
            label: oTranslations().CODE,
            key: "code",
            type: "singleText",
            width: 50
          },
          {
            id: "3.1",
            label: oTranslations().ENABLE_AFTER_SAVE,
            key: "enableAfterSave",
            type: "yesNo",
            width: 50
          },
          {
            id: "4",
            label: oTranslations().MAX_NUMBER_OF_ITEMS,
            key: "maxNoOfItems",
            type: "singleTextNumber",
            width: 50,
            isDisabled: false //Extra necessary property
          },
          {
            id: "5",
            label: oTranslations().PROPERTY_COLLECTION,
            key: "sections",
            type: "customView",
            width: 100
          },
          {
            id: "6",
            label: oTranslations().DATA_TRANSFER_PROPERTIES,
            key: "dataTransferView",
            type: "customView",
            width: 100
          },
          {
            id: "7",
            label: oTranslations().TAB,
            key: "tab",
            type: "lazyMSS",
            width: 50
          }
        ]
      }
    ],
    productVariantRelationship: [
      {
        id: "1",
        label: oTranslations().RELATIONSHIP,
        elements: [
          {
            id: "2",
            label: oTranslations().OTHER_SIDE,
            key: "side2klass",
            type: "customView",
            width: 50
          },
          {
            id: "2.5",
            label: oTranslations().CODE,
            key: "code",
            type: "singleText",
            width: 50
          },
          {
            id: "3.1",
            label: oTranslations().ENABLE_AFTER_SAVE,
            key: "enableAfterSave",
            type: "yesNo",
            width: 50
          },
          {
            id: "4",
            label: oTranslations().MAX_NUMBER_OF_ITEMS,
            key: "maxNoOfItems",
            type: "singleTextNumber",
            width: 50,
            isDisabled: false //Extra necessary property
          },
          {
            id: "5",
            label: oTranslations().PROPERTY_COLLECTION,
            key: "sections",
            type: "customView",
            width: 100
          },
          {
            id: "6",
            label: oTranslations().DATA_TRANSFER_PROPERTIES,
            key: "dataTransferView",
            type: "customView",
            width: 100
          },
          {
            id: "7",
            label: oTranslations().TAXONOMY_INHERITANCE,
            key: "taxonomyInheritanceSetting",
            type: "mss",
            width: 100
          },
          {
            id: "8",
            label: oTranslations().TAB,
            key: "tab",
            type: "lazyMSS",
            width: 100
          },
          {
            id: "9",
            label: oTranslations().RELATIONSHIP_TRANSFER,
            key: "relationshipTransferView",
            type: "customView",
            width: 100
          }
        ]
      }
    ],
    relationshipOtherSide: [
      {
        id: "1",
        label: "",
        elements: [
          {
            id: "2",
            label: oTranslations().ADD_OTHER_SIDE,
            key: "relationshipOtherSide",
            type: "lazyMSS",
            width: 50
          }
        ]
      }
    ]
  }
};

export default configLayoutData;