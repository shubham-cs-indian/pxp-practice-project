import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';

export default function () {
  return [
    {
      id: 'timeDate',
      label: oTranslations().DATE
    },
    {
      id: 'timeDuration',
      label: oTranslations().DURATION
    },
    {
      id: 'timeCycle',
      label: oTranslations().CYCLE
    }
  ];
};