import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager';
const dialogLayoutData = function () {
  return {
    smartDocumentEntity: [{
      id: "22",
      label: oTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "1",
          label: oTranslations().NAME,
          key: "label",
          type: "singleText",
          width: 50
        },
        {
          id: "2",
          label: oTranslations().CODE,
          key: "code",
          type: "singleText",
          width: 50
        },
        {
          id: "4",
          label: oTranslations().PORTAL,
          key: "physicalCatalogIds",
          type: "selectionToggle",
          width: 50
        },
        /*{
          id: "34",
          label:  "Filter Configuration",// oTranslations().FILTERS_CONFIGURATIONS,
          key: "smartDocumentPresetFilterConfiguration",
          type: "customView",
          width: 100
        },*/
      ]
    }],
    smartDocumentTemplate: [{
      id: "22",
      label: oTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "1",
          label: oTranslations().NAME,
          key: "label",
          type: "singleText",
          width: 50
        },
        {
          id: "2",
          label: oTranslations().CODE,
          key: "code",
          type: "singleText",
          width: 50
        },
        {
          id: "3",
          label: oTranslations().TEMPLATES,
          key: "zipTemplateId",
          type: "zipTemplateId",
          width: 100
        },
      ]
    }],
    smartDocumentPreset: [{
      id: "22",
      label: oTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "1",
          label: oTranslations().NAME,
          key: "label",
          type: "singleText",
          width: 50
        },
        {
          id: "2",
          label: oTranslations().CODE,
          key: "code",
          type: "singleText",
          width: 50
        },
        {
          id: "4",
          label: oTranslations().LANGUAGE,
          key: "languageCode",
          type: "mss",
          width: 50
        },
        {
          id: "3",
          label: oTranslations().SPLIT_DOCUMENT,
          key: "splitDocument",
          type: "yesNo",
          width: 50
        },
        {
          id: "44",
          label: oTranslations().SAVE_DOCUMENT,
          key: "saveDocument",
          type: "yesNo",
          width: 50
        },
        {
          id: "33",
          label: oTranslations().VIEW,
          key: "showPreview",
          type: "yesNo",
          width: 50
        },
        {
          id: "33",
          label: oTranslations().PDF_CONFIGURATIONS,
          key: "smartDocumentPresetPdfConfiguration",
          type: "customView",
          width: 100
        },
        {
          id: "35",
          label: oTranslations().DATA_FILTERS,
          key: "smartDocumentPresetSelectionConfiguration",
          type: "customView",
          width: 100
        },
        {
          id: "34",
          label: oTranslations().SELECTION_CONDITIONS,
          key: "smartDocumentPresetFilterConfiguration",
          type: "customView",
          width: 100
        },
      ]
    }],
    smartDocumentPresetPdfConfiguration: [{
      id: "22",
      label: oTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "9",
          label: oTranslations().AUTHOR,
          key: "pdfAuthor",
          type: "singleText",
          width: 50
        },
        {
          id: "10",
          label: oTranslations().KEYWORDS,
          key: "pdfKeywords",
          type: "singleText",
          width: 50
        },
        {
          id: "12",
          label: oTranslations().TITLE,
          key: "pdfTitle",
          type: "singleText",
          width: 50
        },
        {
          id: "11",
          label: oTranslations().SUBJECT,
          key: "pdfSubject",
          type: "singleText",
          width: 50
        },
        {
          id: "13",
          label: oTranslations().USER_PASSWORD,
          key: "pdfUserPassword",
          type: "singleText",
          width: 50
        },
        {
          id: "14",
          label: oTranslations().OWNER_PASSWORD,
          key: "pdfOwnerPassword",
          type: "singleText",
          width: 50
        },
        {
          id: "5",
          label: oTranslations().ALLOW_ANNOTATIONS,
          key: "pdfAllowAnnotations",
          type: "yesNo",
          width: 50
        },
        {
          id: "6",
          label: oTranslations().ALLOW_COPY_CONTENT,
          key: "pdfAllowCopyContent",
          type: "yesNo",
          width: 50
        },
        {
          id: "7",
          label: oTranslations().ALLOW_MODIFICATIONS,
          key: "pdfAllowModifications",
          type: "yesNo",
          width: 50
        },
        {
          id: "8",
          label: oTranslations().ALLOW_PRINTING,
          key: "pdfAllowPrinting",
          type: "yesNo",
          width: 50
        },
        {
          id: "15",
          label: oTranslations().COLOR_SPACE,
          key: "pdfColorSpace",
          type: "mss",
          width: 50
        },
        {
          id: "16",
          label: oTranslations().MARKS_BLEEDS,
          key: "pdfMarksBleeds",
          type: "mss",
          width: 50
        }
        ]
    }],

    smartDocumentPresetSelectionConfiguration: [{
      id: "22",
      label: "Selection Configurations",
      elements: [
         {
        id: '2',
        label: oTranslations().ATTRIBUTES,
        key: 'attributes',
        type: 'lazyMSS',
        width: 100
      }, {
        id: '3',
        label: oTranslations().TAGS,
        key: 'tags',
        type: 'lazyMSS',
        width: 100
      }]
    }],
    smartDocumentPresetFilterConfiguration: [{
      id: "22",
      label: oTranslations().BASIC_INFORMATION,
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
        }]
    }],
  }
};

export default dialogLayoutData;