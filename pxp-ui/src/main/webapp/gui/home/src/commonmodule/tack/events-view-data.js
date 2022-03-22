import {getTranslations as getTranslation} from '../../commonmodule/store/helper/translation-manager.js';

var fEventViewData = function () {
  return {
    "daysOfWeek": [
      {id: "MON", label: getTranslation().MONDAY_SHORT , title: getTranslation().MONDAY_FULL},
      {id: "TUE", label: getTranslation().TUESDAY_SHORT , title: getTranslation().TUESDAY_FULL},
      {id: "WED", label: getTranslation().WEDNESDAY_SHORT , title: getTranslation().WEDNESDAY_FULL},
      {id: "THU", label: getTranslation().THURSDAY_SHORT , title: getTranslation().THURSDAY_FULL},
      {id: "FRI", label: getTranslation().FRIDAY_SHORT , title: getTranslation().FRIDAY_FULL},
      {id: "SAT", label: getTranslation().SATURDAY_SHORT , title: getTranslation().SATURDAY_FULL},
      {id: "SUN", label: getTranslation().SUNDAY_SHORT , title: getTranslation().SUNDAY_FULL}
    ],
    "repeatType": [
      {id: "NONE", label: getTranslation().NONE},
      {id: "DAILY", label: getTranslation().DAILY},
      {id: "WEEKLY", label: getTranslation().WEEKLY},
      {id: "MONTHLY", label: getTranslation().MONTHLY},
      {id: "YEARLY", label: getTranslation().YEARLY},
    ]
  };
};

export default fEventViewData;
