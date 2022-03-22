import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager';
const dialogLayoutData = function () {
  return [
    {
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
          id: "10",
          label: oTranslations().ICON,
          key: "icon",
          type: "image",
          width: 50
        },
        {
          id: "3",
          label: oTranslations().LOCALE_ID,
          key: "localeId",
          type: "mss",
          width: 50
        },
        {
          id: "4",
          label: oTranslations().ABBREVIATION,
          key: "abbreviation",
          type: "singleText",
          width: 50
        },
        {
          id: "5",
          label: oTranslations().NUMBER_FORMAT,
          key: "numberFormat",
          type: "mss",
          width: 50
        },
        {
          id: "6",
          label: oTranslations().DATE_FORMAT,
          key: "dateFormat",
          type: "mss",
          width: 50
        },
        {
          id: "7",
          label: oTranslations().IS_DATA_LANGUAGE,
          key: "isDataLanguage",
          type: "yesNo",
          width: 50
        },
        {
          id: "8",
          label: oTranslations().IS_USER_INTERFACE_LANGUAGE,
          key: "isUserInterfaceLanguage",
          type: "yesNo",
          width: 50
        },
        {
          id: "9",
          label: oTranslations().IS_DEFAULT_LANGUAGE,
          key: "isDefaultLanguage",
          type: "yesNo",
          width: 50
        }

      ]}
  ]};

export default dialogLayoutData;