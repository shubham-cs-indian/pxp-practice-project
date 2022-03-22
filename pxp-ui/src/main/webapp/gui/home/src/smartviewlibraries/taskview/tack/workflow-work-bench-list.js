import TaskModelList from './task-dashboard-model-list';
import TaskDashBoardRoleList from './task-dashboard-roles-list';
import ContentTaskListContext from './task-view-list-node-contexts';
import TaskDictionary from '../../../commonmodule/tack/task-dictionary';

export default function () {
  return [
    {
      id:  TaskDictionary.notifications,
      labelKey: "NOTIFICATIONS",
      context: ContentTaskListContext.TASK_DASHBOARD_LIST_ITEM,
      listItemKey: TaskDictionary.notifications,
      count: 0,
    },
    {
      id:  "workflow",
      labelKey: "WORKFLOW",
      context: ContentTaskListContext.USER_SCHEDULED_WORKFLOW_LIST,
      listItemKey: "workflow",
    },
    {
      id: "workFlowWorkBenchTasksHeader",
      labelKey: "TASKS",
      isExpanded: false,
      isExpandable: true,
      context: ContentTaskListContext.LIST_HEADER,
      children: TaskModelList(ContentTaskListContext.TASK_DASHBOARD_LIST_ITEM)
    },
    {
      id: "workFlowWorkBenchProductsHeader",
      labelKey: "PRODUCTS",
      context: ContentTaskListContext.LIST_HEADER,
      isExpanded: false,
      isExpandable: true,
      children: TaskDashBoardRoleList(ContentTaskListContext.WORKFLOW_LIST_ITEM)
    }
  ]
};