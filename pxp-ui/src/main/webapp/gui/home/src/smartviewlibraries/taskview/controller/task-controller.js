import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as TaskView } from './../view/task-view';
import { view as TaskListView } from './../view/task-list-view';
import { view as PropertyTaskDialog } from './../view/property-task-dialog-view';
import { view as TaskDetailDialog } from './../view/task-detail-dialog-view';
import TaskViewContextConstants from './../tack/task-view-context-constants';
import TaskProps from './../store/model/task-props';
import CommonNotificationProps from './../../../commonmodule/props/notification-props';
import NotificationProps from './../store/model/notifications-props';
import TaskStore from './../store/task-screen-store';
import TaskAction from './../action/task-action';
import TaskTypeDictionary from './../../../commonmodule/tack/task-type-dictionary';

// @CS.SafeComponent
class TaskController extends React.Component {

  static propTypes = {
    data: ReactPropTypes.object,
    context: ReactPropTypes.string,
    isForceUpdate: ReactPropTypes.bool
  };

  constructor (props, context) {
    super(props, context);
    TaskController.preProcessTaskData(props);

    this.state = {
      componentProps: this.getStore().getData().componentProps
    };
  }

  static getDerivedStateFromProps(oNextProps, oState) {
    if(oNextProps.isForceUpdate) {
      TaskController.preProcessTaskData(oNextProps);
    }

    if(oNextProps.context === TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK){
      if(CommonNotificationProps.getIsNotificationButtonSelected()) {
        TaskStore.setTaskViewForDashboard();
      }
    }
  }

  static preProcessTaskData = (oProps) => {
    let oData = oProps.data;
    let sContext = oProps.context;

    TaskStore.updateTaskProps(oData);
    TaskStore.setTaskMode(sContext);

    switch (sContext) {
      case TaskViewContextConstants.CONTENT_TASK:
        TaskStore.setTaskViewForContent("all", "all");
        break;

      case TaskViewContextConstants.PROPERTY_TASK:
        TaskStore.setTaskViewForContent(oData.activeProperty.id, "property");
        break;

      case TaskViewContextConstants.IMAGE_ANNOTATION_TASK:
        let oAnnotationData = oData.annotationData;
        if(!CS.isEmpty(oAnnotationData.activeAnnotationId)) {
          TaskStore.openAnnotation(oAnnotationData.activeAnnotationId);
        } else {
          TaskStore.createAnnotation(oAnnotationData.xPosition, oAnnotationData.yPosition, oData.activeContent);
        }
        break;

      case TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK:
        TaskStore.setTaskViewForDashboard();
        break;
    }
  };

  componentDidMount () {
    this.debouncedStateChanged = CS.debounce(this.stateChanged, 10);
    this.getStore().bind('change', this.debouncedStateChanged);
    TaskAction.registerEvent();
  }

  componentWillUnmount () {
    let oStore = this.getStore();
    oStore.unbind('change', this.debouncedStateChanged);
    TaskAction.deRegisterEvent();

    let sPreviousContext = TaskProps.getTaskMode();
    let sContext = this.props.context;
    if(sContext === sPreviousContext) {
      //Clears task related data from parent controller and from self also.
      oStore.clearAllTaskProps();
    }
  }

  stateChanged = () => {
    this.setState({
      componentProps: this.getStore().getData().componentProps,
    })
  };

  getStore = () => {
    return TaskStore;
  };

  getComponentProps = () => {
    return this.state.componentProps;
  };

  isCurrentUserAccountable = (oTask) => {
    let oProps = this.props;
    let oData = oProps.data;
    var aRoles = oTask.roles;
    var oCurrentUser = oData.currentUser;
/*    var oAccountable = CS.find(aRoles, {roleId: RoleIdDictionary.AccountableRoleId});
    var oCandidate = null;
    oAccountable && (oCandidate = CS.find(oAccountable.candidates, {id: oCurrentUser.id}));

    return !CS.isEmpty(oCandidate);*/
    let bRole = oTask.accountable.roleIds.includes(oCurrentUser.roleId);
    let bUser = oTask.accountable.userIds.includes(oCurrentUser.id);
    let bIsAccountable = bUser || bRole ;
    if(bIsAccountable){
      bIsAccountable = oTask.globalPermission && oTask.globalPermission.canEdit;
    }
    return bIsAccountable;
  };

  getSelectedTagValue = (oTag) => {
    var aTagValues = oTag ? oTag.tagValues : [];
    var sId = "";
    var oSelectedTag = CS.find(aTagValues, {relevance: 100});
    if(oSelectedTag){
      sId = oSelectedTag.tagId;
    }
    return sId;
  };

  getStatusTagNodesForTask = (oTagMaster, sSelectedStatusTagId) => {
    var oAllowedStatusTagMap = TaskProps.getWorkFlowStatusPossibleTagValues();
    var aAllowedTags = oAllowedStatusTagMap[sSelectedStatusTagId] || [];

    var aTags = [];

    if(!CS.isEmpty(oTagMaster)){
      CS.forEach(oTagMaster.children, function (oChild) {
        if(CS.includes(aAllowedTags, oChild.id) || sSelectedStatusTagId === oChild.id){
          var oTag = {
            id: oChild.id,
            label: CS.getLabelOrCode(oChild)
          };
          aTags.push(oTag);
        }
      });
    }

    return aTags;
  };

  getTagNodesForTask = (oTagDetails) => {
    var aTags = [];

    if(!CS.isEmpty(oTagDetails)){
      CS.forEach(oTagDetails.children, function (oChild) {
        var oTag = {
          id: oChild.id,
          label: CS.getLabelOrCode(oChild)
        };
        aTags.push(oTag);
      });
    }

    return aTags;
  };

  getStatusTagDetailsForTask = () => {
    let oData = this.props.data;
    let oConfigDetails = oData.configDetails;
    var oStatusTagDetails = TaskProps.getStatusTagDetails();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var oActiveTask = TaskProps.getActiveTask();
    var oReferencedTasks = oConfigDetails.referencedTasks;
    var sType = !CS.isEmpty(oActiveTask) && oActiveTask.types[0];
    var oActiveType = oReferencedTasks[sType];
    let oTaskTypeDictionary = new TaskTypeDictionary();
    if(oActiveType && oActiveType.type === oTaskTypeDictionary.SHARED) {
      var oOriginalTask = oActiveTask.pureObject || oActiveTask;
      if(!CS.isEmpty(oActiveSubTask)){
        return this.getStatusTagNodesForTask(oStatusTagDetails, this.getSelectedTagValue(oActiveSubTask.status));
      } else {
        return this.getStatusTagNodesForTask(oStatusTagDetails, this.getSelectedTagValue(oOriginalTask.status));
      }
    } else {
      return this.getTagNodesForTask(oStatusTagDetails);
    }

  };

  getToolBarVisibility = (bIsDisabled) => {
    let oData = this.props.data;
    let oReferencedData = TaskProps.getReferencedData();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var oReferencedPermissions = oReferencedData.referencedPermissions || {};
    var oTabPermission = oReferencedPermissions.tabPermission || {};
    var oActiveTask = TaskProps.getActiveTask();
    let aTaskList =TaskProps.getTaskList();

    /** Todo : change mode **/
    if(TaskProps.getTaskMode() === "dashboardTask") {
      oTabPermission = oActiveTask.globalPermission || {};
    }

    var aSelectedTasks = TaskProps.getCheckedTaskList();
    var aCheckedSubTasks = TaskProps.getCheckedSubTaskList();
    var oReferencedTasks = oReferencedData.referencedTasks;
    let oTaskTypeDictionary = new TaskTypeDictionary();
    var oMasterTask = !CS.isEmpty(oActiveTask) ? oReferencedTasks[oActiveTask.types[0]] : null;
    var bShowRoles = oMasterTask && (oMasterTask.type === oTaskTypeDictionary.SHARED);
    var bShowPublicPrivateIcon = oMasterTask && (oMasterTask.type === oTaskTypeDictionary.SHARED) && CS.isEmpty(oActiveSubTask) ;

    var bCanEditDates = false;
    if(oMasterTask && (oMasterTask.type === oTaskTypeDictionary.SHARED)) {
      var oTask = !CS.isEmpty(oActiveSubTask) ? oActiveSubTask : oActiveTask;
      bCanEditDates = !oActiveTask.isCreated && !bIsDisabled && this.isCurrentUserAccountable(oTask);
    } else {
      bCanEditDates = !oActiveTask.isCreated && !bIsDisabled;
    }
    var bIsSaveVisible = !!oActiveTask.isDirty;
    let sSelectedModule = oData.selectedModuleId;
    if(sSelectedModule !== "dashboard" && bIsSaveVisible) {
      bIsSaveVisible = !!oTabPermission.canEdit;
    }

    return {
      bIsSelectAllVisible: !CS.isEmpty(aTaskList),
      bIsDeleteVisible: !CS.isEmpty(aSelectedTasks),
      bIsSaveVisible: bIsSaveVisible,
      bIsSubTaskDeleteVisible: !CS.isEmpty(aCheckedSubTasks),
      showRoles: bShowRoles,
      showPublicPrivateIcon: bShowPublicPrivateIcon,
      canEditDates: bCanEditDates
    };
  };


  getTasksCommonData = () => {
    let oData = this.props.data;
    let oConfigDetails = oData.configDetails;
    let oReferencedData = TaskProps.getReferencedData();
    let oTaskData = TaskProps.getTaskData();
    var oPriorityDetails = TaskProps.getPriorityTagDetails();
    var oActiveTask = TaskProps.getActiveTask();
    var oStatusTags = this.getStatusTagDetailsForTask();

    var oReferencedPermissions = oConfigDetails.referencedPermissions;
    var oTabPermission = oReferencedPermissions.tabPermission;
    var bIsDisabled = !oTabPermission.canEdit;
    if(!bIsDisabled){
      bIsDisabled = !(oActiveTask && oActiveTask.globalPermission && oActiveTask.globalPermission.canEdit);
    }
    return {
      taskViewProps : TaskProps.getTaskViewProps(),
      isTaskDetailViewOpen : TaskProps.getIsTaskDetailViewOpen(),
      taskListGroupByType : TaskProps.getTaskListGroupByType(),
      activeTask : TaskProps.getActiveTask(),
      activeSubTask : TaskProps.getActiveSubTask(),
      taskList : TaskProps.getTaskList(),
      toolBarVisibility : this.getToolBarVisibility(false),
      priorityTags: this.getTagNodesForTask(oPriorityDetails),
      statusTags: oStatusTags,
      selectedGroupByType: TaskProps.getActiveGroupByType(),
      isDisabled: bIsDisabled,
      editableRoles: TaskProps.getEditableRoles(),
      taskOrEvent: 'task',
      referencedRoles: oReferencedData.referencedRoles,
      activeContent: oData.activeContent,
      taskRolesData: oData.taskRolesData,
      referencedData: oTaskData.referencedData
    };
  };

  getTaskDashboardData = () => {
    var oGroupByTypeTaskListMap = TaskProps.getTaskListGroupByType();
    var bIsTaskDetailViewOpen = TaskProps.getIsTaskDetailViewOpen();
    var oTaskViewProps = TaskProps.getTaskViewProps();
    var oPriorityDetails = TaskProps.getPriorityTagDetails();
    var oStatusTagDetails = TaskProps.getStatusTagDetails();
    var iTotalGroupedByTasks = 0;
    var aCheckedTaskList = TaskProps.getCheckedTaskList();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var oActiveTask = TaskProps.getActiveTask();
    var oStatusTags = null;
    let aModelList = TaskProps.getTaskLevelsList();

    if(!CS.isEmpty(oActiveSubTask)){
      oStatusTags = this.getStatusTagNodesForTask(oStatusTagDetails, this.getSelectedTagValue(oActiveSubTask.status));
    } else {
      oStatusTags = this.getStatusTagNodesForTask(oStatusTagDetails, this.getSelectedTagValue(oActiveTask.status));
    }

    CS.map(oGroupByTypeTaskListMap, function (oTaskGroup) {
      iTotalGroupedByTasks += oTaskGroup.tasks.length;
    });

    if(iTotalGroupedByTasks == aCheckedTaskList.length && iTotalGroupedByTasks){
      TaskProps.setIsAllTaskChecked(true);
    }else {
      TaskProps.setIsAllTaskChecked(false);
    }

    let oData = this.props.data;
    // let oReferencedData = TaskProps.getReferencedData();
    let oReferencedData = TaskProps.getTaskData();
    var iNotificationCount = NotificationProps.getUnreadNotificationsCount();
    var oNotificationData = {};

    var aNotificationsList = NotificationProps.getNotificationsList();
    let aSelectedLevelListIds = TaskProps.getSelectedLevelListIds();
    if (CS.includes(aSelectedLevelListIds, "notifications")) {
      oNotificationData.notifications = NotificationProps.getNotificationModel();
      oNotificationData.showLoadMore = aNotificationsList.length >= 20;
      oNotificationData.isRefreshing = NotificationProps.getRefreshingState();
      oNotificationData.isNotificationViewActive = true
    }

    let oUserScheduledWorkflowList = {};
    if (CS.includes(aSelectedLevelListIds, "workflow")) {
      oUserScheduledWorkflowList.isUserScheduledWorkflowListViewActive = true;
      oUserScheduledWorkflowList.userScheduledWorkflowList = TaskProps.getIsSearchFilterApplied() ? TaskProps.getFilteredWorkflowList() : TaskProps.getUserScheduledWorkflowList();
    };

    /** Workflow Template data for Dialog Box **/
    let oWorkflowTemplateData = {
      bIsWorkflowDialogOpen: TaskProps.getIsWorkflowDialogOpen(),
      workflowData: TaskProps.getWorkflowData(),
      frequencyData: TaskProps.getFrequencyData(),
      selectedTabId: TaskProps.getSelectedTabId(),
      tabHeaderData: TaskProps.getTabHeaderData(),
      userScheduledWorkflowList: oUserScheduledWorkflowList,
      bIsAllWorkflowChecked: TaskProps.getIsAllWorkflowChecked(),
      checkedWorkflowList: TaskProps.getCheckedWorkflowList(),
    };

    return {
      taskListGroupByType: oGroupByTypeTaskListMap,
      activeTask: TaskProps.getActiveTask(),
      activeSubTask: TaskProps.getActiveSubTask(),
      taskLevelsList: aModelList,
      isTaskDetailViewOpen: bIsTaskDetailViewOpen,
      taskViewProps: oTaskViewProps,
      usersList: oData.usersList,
      currentUser: oData.currentUser,
      activeContent: oData.activeContent,
      toolBarVisibility: this.getToolBarVisibility(false),
      priorityTags: this.getTagNodesForTask(oPriorityDetails),
      statusTags: oStatusTags,
      selectedGroupByType: TaskProps.getActiveGroupByType(),
      isDisabled: false,
      editableRoles: TaskProps.getEditableRoles(),
      referencedRoles: oReferencedData && oReferencedData.referencedRoles || {},
      context: "taskDashboard",
      activeTaskFormViewModels: TaskProps.getActiveTaskFormViewModels(),
      selectedLevelListIds: aSelectedLevelListIds,
      paginationDataByListGroups: TaskProps.getPaginationDataByListGroups(),
      taskList: TaskProps.getTaskList(),
      notificationsData: oNotificationData,
      notificationsCount: iNotificationCount,
      taskOrEvent: "task",
      taskRolesData: oData.taskRolesData,
      referencedData: oReferencedData.referencedData,
      workflowTemplateData : oWorkflowTemplateData
    };
  };

  getTaskData = () => {
    let oProps = this.props;
    var oGroupByTypeTaskListMap = TaskProps.getTaskListGroupByType();
    var oActiveTaskLevel = TaskProps.getActiveTaskLevel() || {};
    var iTotalGroupedByTasks = 0;
    var aCheckedTaskList = TaskProps.getCheckedTaskList();
    var aTaskByLevelModelsList = TaskProps.getTaskLevelsList();
    var oTaskData = this.getTasksCommonData();
    let sContext = oProps.context;

    let oDataFromProps = oProps.data;
    oTaskData = CS.combine(oDataFromProps, oTaskData);
    oTaskData = CS.combine(oDataFromProps.configDetails, oTaskData);
    oTaskData.selectedLevelListIds = [oActiveTaskLevel.id];
    oTaskData.taskLevelsList = aTaskByLevelModelsList;

    CS.map(oGroupByTypeTaskListMap, function (oTaskGroup) {
      iTotalGroupedByTasks += oTaskGroup.tasks.length;
    });

    let aFilteredTaskList = TaskProps.getFilteredTaskList();
    if (aFilteredTaskList && aFilteredTaskList.length === aCheckedTaskList.length && aCheckedTaskList.length > 0) {
      TaskProps.setIsAllTaskChecked(true);
    } else {
      TaskProps.setIsAllTaskChecked(false);
    }

    oTaskData.activeTaskFormViewModels = TaskProps.getActiveTaskFormViewModels();

    return oTaskData;
  };

  getData = () => {
    let oProps = this.props;
    let sContext = oProps.context;

    switch (sContext) {
      case TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK:
        return this.getTaskDashboardData();

      default:
        return this.getTaskData();
    }
  };

  getView = () => {
    let oProps = this.props;
    let sContext = oProps.context;
    let oData = this.getData();

    switch (sContext) {
      case TaskViewContextConstants.CONTENT_TASK:
      case TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK:
        return <TaskView {...oData}/>;

      case TaskViewContextConstants.PROPERTY_TASK:
        return <PropertyTaskDialog data={oData}/>;

      case TaskViewContextConstants.EVENTS:
        return <TaskListView {...oData}/>;

      case TaskViewContextConstants.IMAGE_ANNOTATION_TASK:
        return <TaskDetailDialog data={oData}/>;
    }
  };

  render = () => {
    return this.getView();
  }
}

export default TaskController;
