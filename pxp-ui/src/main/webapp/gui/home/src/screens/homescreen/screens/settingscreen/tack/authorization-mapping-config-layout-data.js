/**
 * Created by Vinay Gaikwad on 17-04-2019.
 */
let oTranslations = require('../../../../../commonmodule/store/helper/translation-manager.js').getTranslations;

const mappingConfigLayoutData = function () {
  return {
    mappingBasicInformation: [
      {
        id: "1",
        label: oTranslations().NAME,
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
          }
        ]
      },
      {
        id: "4",
        label: oTranslations().DATA_INTEGRATION_AUTHORIZATION +" "+ oTranslations().SUMMARY,
        elements: [
          {
            id: "4",
            label: oTranslations().DATA_INTEGRATION_AUTHORIZATION +" "+ oTranslations().SUMMARY,
            key: "tabSummary",
            type: "tabSummary",
            width: 100
          }
        ]
      }
    ]
  };
};

module.exports = mappingConfigLayoutData;