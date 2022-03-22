import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const classContextDialogLayoutData = function () {
  return [
    {
      id: "1",
      label: oTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "1",
          label: oTranslations().NAME,
          key: "label",
          type: "singleText",
          width: 33
        },
        {
          id: "2",
          label: oTranslations().TYPE,
          key: "type",
          type: "mss",
          width: 33
        },
        {
          id: "31",
          label: oTranslations().TAB,
          key: "tab",
          type: "lazyMSS",
          width: 33
        },
        {
          id: "12",
          label: oTranslations().ICON,
          key: "icon",
          type: "image",
          width: 33
        },
        {
          id: "11",
          label: oTranslations().LIMITED_OBJECT,
          key: "isLimitedObject",
          type: "yesNo",
          width: 33
        },
        {
          id: "3",
          label: oTranslations().ENABLE_TIME,
          key: "isTimeEnabled",
          type: "yesNo",
          width: 33
        },
        {
          id: "4",
          label: oTranslations().FROM + " ( " + oTranslations().DEFAULT + " )",
          key: "defaultFromDate",
          type: "customView",
          width: 33
        },
        {
          id: "5",
          label: oTranslations().TO + " ( " + oTranslations().DEFAULT + " )",
          key: "defaultToDate",
          type: "customView",
          width: 33
        },
        {
          id: "6",
          label: oTranslations().ALLOW_DUPLICATE,
          key: "isDuplicateVariantAllowed",
          type: "yesNo",
          width: 50
        },
        {
          id: "7",
          label: oTranslations().AUTO_CREATE,
          key: "isAutoCreate",
          type: "yesNo",
          width: 50
        },
        {
          id: "8",
          label: oTranslations().ENTITIES,
          key: "entities",
          type: "selectionToggle",
          width: 50
        },
        {
          id: "9",
          label: oTranslations().CONTEXT_SELECTED_TAG_GROUP,
          key: "tagSelector",
          type: "customView",
          width: 100
        },
        {
          id: "10",
          label: oTranslations().UNIQUE_SELECTOR_HEADER,
          key: "tagCombinationView",
          type: "customView",
          width: 100
        }
      ]
    },
  ];
};
export default classContextDialogLayoutData;