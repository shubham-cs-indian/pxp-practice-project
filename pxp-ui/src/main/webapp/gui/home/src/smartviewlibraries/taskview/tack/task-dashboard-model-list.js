import TaskDashBoardRolesList from './task-dashboard-roles-list';
import TaskDashBoardNotification from './task-dashboard-notification';


export default function (sContext) {
  return TaskDashBoardRolesList(sContext);
};
/*
module.exports = function (sContext) {
  return TaskDashBoardNotification(sContext).concat(TaskDashBoardRolesList(sContext));
};*/
