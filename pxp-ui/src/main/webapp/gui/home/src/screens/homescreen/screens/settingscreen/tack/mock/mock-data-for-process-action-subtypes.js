import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import MockDataForProcessActionTypesDictionary from '../../tack/mock/mock-data-for-action-subtypes-dictionary';
export default function () {
  return [
    {
      id: MockDataForProcessActionTypesDictionary.AFTER_PROPERTIES_SAVE,
      label: oTranslations().AFTER_PROPERTIES_SAVE,
    },
    {
      id: MockDataForProcessActionTypesDictionary.AFTER_CLASSIFICATION_SAVE,
      label: oTranslations().AFTER_CLASSIFICATION_SAVE,
    },
    {
      id: MockDataForProcessActionTypesDictionary.AFTER_RELATIONSHIP_SAVE,
      label: oTranslations().AFTER_RELATIONSHIP_SAVE,
    },
  ];
};
