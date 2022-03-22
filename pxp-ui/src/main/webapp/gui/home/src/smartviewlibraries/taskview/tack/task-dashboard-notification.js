import TaskDictionary from './../../../commonmodule/tack/task-dictionary';

export default function (sContext) {
  return [
    {
      "id":  TaskDictionary.notifications,
      "labelKey": "NOTIFICATIONS",
      "context": sContext,
      "listItemKey": TaskDictionary.notifications,
      "count": 0,
    },
  ]
};