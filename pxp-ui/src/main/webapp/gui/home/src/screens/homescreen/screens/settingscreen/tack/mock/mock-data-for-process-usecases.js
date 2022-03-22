import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import MockDataForProcessUsecasesDictionary from '../../tack/mock/mock-data-for-process-usecases-dictionary';

export default function () {
  return [
    {
      id: MockDataForProcessUsecasesDictionary.IMPORT,
      label: oTranslations().IMPORT,
    },
    {
      id: MockDataForProcessUsecasesDictionary.TRANSFER,
      label: oTranslations().TRANSFER,
    },
    {
      id: MockDataForProcessUsecasesDictionary.ALL_OTHERS,
      label: oTranslations().ALL_OTHERS,
    },
  ];
};
