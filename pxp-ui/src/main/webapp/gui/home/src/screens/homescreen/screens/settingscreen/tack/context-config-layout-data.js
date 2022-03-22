import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const contextConfigDialogData =function () {
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
          id: "10",
          label: oTranslations().CODE,
          key: "code",
          type: "singleText",
          width: 33,
          isSortable: true
        },
        {
          id: "2",
          label: oTranslations().TYPE,
          key: "type",
          type: "mss",
          width: 33
        },
        {
          id: "3",
          label: oTranslations().LIMITED_OBJECT,
          key: "isLimitedObject",
          type: "yesNo",
          width: 25
        },
        {
          id: "9",
          label: oTranslations().DEFAULT_VIEW,
          key: "defaultView",
          type: "mss",
          width: 33
        },
        /*
        {
          id: "4",
          label: oTranslations().STATUS_TAGS,
          key: "statusTags",
          type: "selectionToggle",
          width: 50
        },
        */
        {
          id: "3",
          label: oTranslations().ICON,
          key: "icon",
          type: "image",
          width: 25
        },
        {
          id: "7",
          label: oTranslations().AUTO_CREATE,
          key: "isAutoCreate",
          type: "yesNo",
          width: 25
        },
        {
          id: "7",
          label: oTranslations().ENABLE_TIME,
          key: "isTimeEnabled",
          type: "yesNo",
          width: 33
        },
        {
          id: "7",
          label: oTranslations().ALLOW_DUPLICATE,
          key: "isDuplicateVariantAllowed",
          type: "yesNo",
          width: 33
        },
        {
          id: "8",
          label: oTranslations().FROM + " ( " + oTranslations().DEFAULT + " )",
          key: "defaultFromDate",
          type: "customView",
          width: 33
        },
        {
          id: "9",
          label: oTranslations().TO + " ( " + oTranslations().DEFAULT + " )",
          key: "defaultToDate",
          type: "customView",
          width: 33
        },
        {
          id: "6",
          label: oTranslations().TAB,
          key: "tab",
          type: "lazyMSS",
          width: 33
        },
        {
          id: "4",
          label: oTranslations().SELECTED_TAGS,
          key: "tagGroups",
          type: "customView",
          width: 100
        },
        {
          id: "5",
          label: "",
          key: "subContexts",
          type: "customView",
          width: 100
        },
        {
          id: "6",
          label: "",
          key: "entities",
          type: "customView",
          width: 100
        },
        {
          id: "7",
          label: oTranslations().UNIQUE_SELECTOR_HEADER,
          key: "tagCombinationView",
          type: "customView",
          width: 100
        }
      ]
    },
  ];
};

export default contextConfigDialogData;