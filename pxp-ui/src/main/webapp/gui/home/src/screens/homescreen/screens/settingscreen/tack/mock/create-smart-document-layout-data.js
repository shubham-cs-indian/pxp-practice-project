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
    }
  ]
};

export default dialogLayoutData;