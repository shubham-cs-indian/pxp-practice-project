import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const taxonomyLayoutData = function () {
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
          width: 50
        },
        {
          id: "2",
          label: oTranslations().ICON,
          key: "icon",
          type: "image",
          width: 50
        },
        {
          id: "3",
          label: oTranslations().FILTER_BY,
          key: "filterBy",
          type: "mss",
          width: 50
        },
        {
          id: "4",
          label: oTranslations().SORT_BY,
          key: "sortBy",
          type: "mss",
          width: 50
        },
        {
          id: "5",
          label: oTranslations().CODE,
          key: "code",
          type: "singleText",
          width: 100
        },
        /*{
          id: "6",
          label: oTranslations().RULES,
          key: "dataRules",
          type: "lazyMSS",
          width: 33
        },*/
        {
          id: "8",
          label: oTranslations().TASKS,
          key: "tasks",
          type: "lazyMSS",
          width: 50
        },
        {
          id: "9",
          label: oTranslations().EMBEDDED_CLASS,
          key: "embeddedKlass",
          type: "customView",
          width: 100
        },
        {
          id: "10",
          label: oTranslations().ADD_CLASS,
          key: "embeddedKlassDropdown",
          type: "lazyMSS",
          width: 100
        },
      ]
    }
  ];
};

export default taxonomyLayoutData;