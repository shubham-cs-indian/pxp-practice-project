import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager';

export default function () {

  return [
    {
      id: "daily",
      label: oTranslations().DAILY
    },
    {
      id: "weekly",
      label: oTranslations().WEEKLY
    },
    {
      id: "manual",
      label: oTranslations().manual
    },
    {
      id: "realtime",
      label: oTranslations().realtime
    }
  ];
};
