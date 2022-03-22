/**
 * Created by CS108 on 20-07-2017.
 */
import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

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
            width: 33
        },
        {
          id: "2",
          label: oTranslations().CODE,
          key: "code",
          type: "singleText",
          width: 33,
          isSortable: true
        },
        {
          id: "3",
          label: "Type",
          key: "mappingType",
          type: "mssNew",
          width: 33,
          isSortable: true
        }
        ]
      },
      {
        id: "4",
        label: oTranslations().MAPPING_SUMMARY,
        elements: [
          {
            id: "4",
            label: oTranslations().MAPPING_SUMMARY,
            key: "tabSummary",
            type: "tabSummary",
            width: 100
          }
        ]
      }
    ]
  };
};

export default mappingConfigLayoutData;