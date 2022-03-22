import ContentTaskViewListNodeContext from './task-view-list-node-contexts';
const getTranslation = require('../../../commonmodule/store/helper/translation-manager.js').getTranslations;

export default function () {
  return [
    {
      "id": "all",
      "label": getTranslation().ALL,
      "labelKey": "ALL",
      "context": ContentTaskViewListNodeContext.CONTENT_TASK,
      "listItemKey": "all",
      "tasksCount": 0,
      "bShowCount": false,
      "children": []
    },
    {
      "id": "content-level-task",
      "label": getTranslation().CONTENT_LEVEL_TASK,
      "labelKey": "CONTENT_LEVEL_TASK",
      "context": ContentTaskViewListNodeContext.CONTENT_TASK_LIST_HEADER,
      "isExpanded": true,
      "listItemKey": "contentAll",
      "bShowCount": false,
      "tasksCount": 0,
      "children": []
    },
    {
      "id": "property-level-task",
      "label": getTranslation().PROPERTY_LEVEL_TASK,
      "labelKey": "PROPERTY_LEVEL_TASK",
      "context": ContentTaskViewListNodeContext.CONTENT_TASK_LIST_HEADER,
      "isExpanded": true,
      "listItemKey": "propertyAll",
      "tasksCount": 0,
      "bShowCount": false,
      "children": []
    }
  ]
};