import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const dataGovernanceRulesConfigData = function () {
  return {
    dataGovernanceConfigInformation: [
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
            label: oTranslations().CODE,
            key: "code",
            type: "singleText",
            width: 50,
            isSortable: true
          },
          {
            id: "3",
            label: oTranslations().PARTNERS,
            key: 'partners',
            type: 'lazyMSS',
            width: 50
          },
          {
            id: "4",
            label: oTranslations().RULE_TYPE,
            key: 'ruleType',
            type: 'singleText',
            width: 50
          },
          {
            id: '5',
            label: oTranslations().PHYSICAL_CATALOG,
            key: 'physicalCatalogId',
            type: 'selectionToggle',
            width: 50
          },
          {
            id: '6',
            label: oTranslations().LANGUAGE_DEPENDENT,
            key: 'isLanguageDependent',
            type: 'yesNo',
            width: 50
          },
          {
            id: '7',
            label: oTranslations().LANGUAGES,
            key: 'dataLanguages',
            type: 'mssNew',
            width: 50
          },

          /*,
          {
            id: '6',
            label: oTranslations().ENDPOINTS,
            key: 'endpoints',
            type: 'lazyMSS',
            width: 100
          }*/
        ]
      }
    ],
  };
};
export default dataGovernanceRulesConfigData;