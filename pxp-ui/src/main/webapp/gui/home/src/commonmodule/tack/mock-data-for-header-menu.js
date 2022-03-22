import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';

const headerMenus = function () {
  return [
   /* {
      id: "notification",
      containerClassName: "notificationButtonViewContainer",
      menuClassName: "notificationButton",
      label: "Notification",
    },*/
    {
      id: "taskPlanned",
      containerClassName: "taskPlannedButtonViewContainer",
      menuClassName: "taskPlannedButton",
      label: oTranslations().PLANNED_TASKS,
    }
  ]
};
export default headerMenus;