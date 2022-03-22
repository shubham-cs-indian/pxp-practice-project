import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';

var TaskGroupingConstants = function () {
  return {
    "taskGroupByOptions" : [
      {id: "type", label: getTranslation().TYPE},
      {id: "dueDate", label: getTranslation().DUE_DATE},
      {id: "priority", label: getTranslation().PRIORITY},
      {id: "status", label: getTranslation().STATUS}
    ],

    "eventGroupByOptions" : [
      {id: "types", label: getTranslation().TYPES},
      {id: "startDate", label: getTranslation().START_DATE}
    ]
  };
};
export default TaskGroupingConstants;