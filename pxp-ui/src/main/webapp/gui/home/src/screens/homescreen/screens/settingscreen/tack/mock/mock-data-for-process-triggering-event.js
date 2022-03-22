import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager';

export default function () {
  return [
    {
      id: "afterSave",
      label: oTranslations().AFTER_SAVE
    },
    {
      id: "afterCreate",
      label: oTranslations().AFTER_CREATE
    },
    {
      id: "import",
      label: oTranslations().IMPORT
    },
    {
      id: "export",
      label: oTranslations().EXPORT
    },
  ];
};