/**
 * Created by CS49 on 10/24/2016.
 */

var oUploadRequestMapping = {
  'GetAssetImage': 'asset/<%=type%>/<%=id%>',
  'GetAssetStatus': 'asset/<%=type%>/<%=id%>',
  'UploadImage': 'asset/upload/',
  'GetAssetUploadStatus': 'asset/upload/status/<%=type%>/<%=id%>',
  'DownloadFile': 'getimportedfile/<%=id%>?organizationId=<%=organizationId%>&endpointId=<%=endpointId%>',
};

var oLazyDataRequestMapping = {
  'GetConfigData': 'config/configdata',
};

var oTaskRequestMapping = {
  'GetAllTasks': 'runtime/<%=contentType%>/tasktab/<%=id%>?getAll=<%=getAll%>',
  'GetTask': 'runtime/taskinstances/<%=id%>',
  'CreateTask': 'runtime/taskinstances',
  'SaveTask': 'runtime/taskinstances',
  'CompleteTask': 'runtime/taskinstancescomplete',
  'CompleteTaskBulk': 'runtime/taskinstancescomplete/bulk',
  'DeleteTasks': 'runtime/taskinstances',
  'fetchAllLazyTaskDetailsList': 'runtime/taskinstances/getall/<%=roleId%>',
  'GetEntityById': 'runtime/klassinstances/<%=tab%>/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>',
  'GetAllAssetExtensions': 'config/getExtensions',
  'CreateWorkflow': 'config/processevents/clone',
  'GetAllUserScheduledWorkflow': 'config/processevents/grid',
  'DeleteUserScheduledWorkflow': 'config/processevents',
  'BulkSave': 'config/processevents/bulksave',
  'BulkCodeCheck': 'config/bulkcheckentitycode',
  'ValidateUserAndRolesForTask' : 'runtime/authorizeuserandrolefortask'
};

var oWorkflowRequestMapping = {
  'GetProductsByRoleId': "runtime/instancebasedontask"
};

var oNotificationRequestMapping = {
  'GetAllNotifications': "runtime/notifications",
  'ClearNotification': "runtime/notificationbyid",
  'ClearAllNotification': "runtime/notificationsforuser",
  'ChangeStatus': "runtime/notificationstatus",
};

export const UploadRequestMapping = oUploadRequestMapping;
export const LazyDataRequestMapping = oLazyDataRequestMapping;
export const TaskRequestMapping = oTaskRequestMapping;
export const NotificationRequestMapping = oNotificationRequestMapping;
export const WorkflowRequestMapping = oWorkflowRequestMapping;