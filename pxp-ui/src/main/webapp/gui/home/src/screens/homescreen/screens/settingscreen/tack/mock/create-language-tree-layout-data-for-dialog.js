import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager';
const dialogLayoutData = function () {
  return [
    {
      id: "11",
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
          id: "4",
          label: oTranslations().CODE,
          key: "code",
          type: "singleText",
          width: 50
        },
        {
          id: "5",
          label: oTranslations().LOCALE_ID,
          key: "localeId",
          type: "mss",
          width: 50
        },
        {
          id: "6",
          label: oTranslations().ABBREVIATION,
          key: "abbreviation",
          type: "singleText",
          width: 50
        }
      ]}
  ]};

export default dialogLayoutData;