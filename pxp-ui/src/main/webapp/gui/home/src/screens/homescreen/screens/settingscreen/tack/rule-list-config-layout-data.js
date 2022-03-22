import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const configLayoutData = function () {
  return {
    ruleListConfigData: [
      {
        id: "1",
        label: getTranslations().WORD_LIST,
        elements: [
          {
            id: "2",
            label: getTranslations().NAME,
            key: "label",
            type: "singleText",
            width: 50
          },
          {
            id: "3",
            label: getTranslations().CODE,
            key: "code",
            type: "singleText",
            width: 50,
            isSortable: true
          },
          {
            id: "4",
            label: getTranslations().BLACK_LISTED_WORDS,
            key: "blackListedWords",
            type: "customView",
            width: 100
          },
        ]
      }
    ]
  }
};
export default configLayoutData;
