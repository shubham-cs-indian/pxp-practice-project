import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';

const oScreens = function () {
  return [
    {
      id: 'dashboard',
      label: oTranslations().DASHBOARD_TITLE
    },
    {
      id: 'explore',
      label: oTranslations().EXPLORE
    }
  ];
};

export default oScreens;