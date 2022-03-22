import alertify from '../../../commonmodule/store/custom-alertify-store';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import MethodTracker from '../../../libraries/methodtracker/method-tracker';
import { store as TaskStore } from './helper/task-store';
import TaskProps from './model/task-props';
import NotificationStore from './helper/notifications-store';
import TaskViewConstants from './../tack/task-view-context-constants';

var TaskScreenStore = (function () {
  var _triggerChange = function () {
    MethodTracker.emptyCallTrace();
    TaskScreenStore.trigger('change');
  };

  let _handleTaskListToolBarClicked = function (sEvent) {
    switch (sEvent) {
      case "save":
        TaskStore.handleSaveTaskClicked();
        break;
      case "complete":
        TaskStore.completeTask();
        break;
      case "discard":
        TaskStore.discardTask();
        break;
    }
  };

  let _handleControlD = function () {
    let sTaskMode = TaskProps.getTaskMode();
    let oActiveTask = TaskProps.getActiveTask();
    let bIsTaskDirty = (oActiveTask && oActiveTask.isDirty);

    if(bIsTaskDirty) {
      switch (sTaskMode) {
        case TaskViewConstants.CONTENT_TASK:
        case TaskViewConstants.PROPERTY_TASK:
        case TaskViewConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK:
        case TaskViewConstants.IMAGE_ANNOTATION_TASK:
          TaskStore.discardTask();
          break;
      }
    }
    _triggerChange();
  };

  let _handleControlS = function() {
    let sTaskMode = TaskProps.getTaskMode();
    switch (sTaskMode) {
      case TaskViewConstants.CONTENT_TASK:
      case TaskViewConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK:
        _handleTaskListToolBarClicked('save');
        break;
      }
  };

  let _handleControlESC = function () {
    let sTaskMode = TaskProps.getTaskMode();
    if (sTaskMode === TaskViewConstants.PROPERTY_TASK || sTaskMode === TaskViewConstants.IMAGE_ANNOTATION_TASK) {
      TaskStore.handleTaskDialogCloseClicked();
    }
    TaskStore.handleFileAttachmentDetailViewClose();
  };

  let _handleTaskDetailDialogButtonClicked = (oExtraData) => {
    let sId = oExtraData.id;
    let sTaskOrEvent = oExtraData.taskOrEvent;
    let sContext = oExtraData.context;
    let sButtonId = oExtraData.buttonId;
    let sActiveTaskId = oExtraData.activeTaskId;

    switch (sButtonId) {
      case "save":
        switch (sTaskOrEvent) {
          case  'task':
            TaskStore.handleSaveTaskClicked();
            break;
        }
        break;

      case  "discard":
        TaskStore.discardTask();
        break;

      default:
        TaskStore.handleImageSimpleAnnotationViewCloseTaskDetail(sActiveTaskId);
        break;

    }
  };

  //*********************** Public API's ***********************//

  return {

    getData: function () {
      return {
        componentProps: TaskProps
      }
    },

    handleTaskLevelListNodeClicked: function (oTaskListItem, iListLevelIndex) {
      TaskStore.handleTaskListByLevelClicked(oTaskListItem, iListLevelIndex);
    },

    handleTaskLevelListHeaderClicked: function (oTaskListItem, iListLevelIndex) {
      TaskStore.handleTaskLevelListHeaderClicked(oTaskListItem, iListLevelIndex);
    },

    handleTaskListSearch: function (oTaskListItem, iListLevelIndex, sSearchText) {
      TaskStore.handleTaskListSearch(oTaskListItem, iListLevelIndex, sSearchText);
    },

    handleTaskListLoadMore: function (oTaskListItem, iListLevelIndex) {
      TaskStore.handleTaskListLoadMore(oTaskListItem, iListLevelIndex);
    },

    handleTaskListClicked: function (sContext, sId) {
      switch (sContext){
        case  'task':
          TaskStore.handleTaskListClicked(sId);
          break;
      }
    },

    handleTaskListCheckClicked: function (sContext, bIsChecked, sId) {
      switch (sContext){
        case  'task':
          TaskStore.handleTaskListCheckClicked(bIsChecked, sId);
          break;

        case  'workflow':
          TaskStore.handleWorkflowListCheckClicked(bIsChecked, sId);
          break;
      }
    },

    handleAllTaskListCheckClicked: function (sContext, sSearchText) {
      switch (sContext){
        case  'task':
          TaskStore.handleAllTaskListCheckClicked(sSearchText);
          break;
        case  'workflow':
          TaskStore.handleAllWorkflowListCheckClicked(sSearchText);
          break;
      }
    },

    handleAddNewTaskClicked: function (sContext, sTypeId, oContent) {
      switch (sContext){
        case  'task':
          TaskStore.handleAddNewTaskClicked(sTypeId, oContent);
          break;
      }
    },

    handleTaskLabelValueChanged: function (sContext, sKey, sId, sValue) {
      switch (sContext){
        case  'task':
          TaskStore.handleTaskLabelValueChanged(sId, sValue);
          break;
      }
    },

    handleTaskNameOnBlur: function (sContext, sId) {
      switch (sContext){
        case  'task':
          TaskStore.handleTaskNameOnBlur(sId);
          break;
      }
    },

    handleGroupByDropDownClicked: function (sContext, sId) {
      switch (sContext){
        case  'task':
          TaskStore.handleGroupByDropDownClicked(sId);
          break;
      }
    },

    handleDeleteTaskClicked: function (sContext, sId, oActiveContent) {
      if(TaskStore.activeTaskSafetyCheck() && TaskStore.activeTaskCommentSafetyCheck()) {
        switch (sContext) {
          case  'task':
            TaskStore.handleDeleteTaskClicked(sId, oActiveContent);
            break;
        }
      }
    },

    handleTasksSearchTextChanged: function (sSearchString, sContext) {
      switch (sContext) {
        case "task":
          TaskStore.handleTasksSearchTextChanged(sSearchString);
          break;

        case "workflow":
          TaskStore.handleWorkflowSearchTextChanged(sSearchString);
          break;
      }
    },

    handleListCollapsedStateChanged: function (sTypeId, sContext) {
      switch (sContext){
        case "task":
          TaskStore.handleListCollapsedStateChanged(sTypeId);
          break;
      }
    },

    handleSubtaskBackButtonClicked: function (sId) {
      TaskStore.handleSubtaskBackButtonClicked(sId);
    },

    handleResetScrollState: function () {
      TaskStore.handleResetScrollState();
    },

    handleTaskVisibilityModeChanged: function (sId, sIsPublic) {
      TaskStore.handleTaskVisibilityModeChanged(sId, sIsPublic);
    },

    handleCloseTaskDetailClicked: function (sContext) {
      switch (sContext){
        case  'task':
          TaskStore.handleCloseTaskDetailClicked();
          break;
      }
    },

    handleSubTaskListCheckBoxClicked: function (sSubtaskId, bSubTaskChecked) {
      TaskStore.handleSubTaskListCheckBoxClicked(sSubtaskId, bSubTaskChecked);
    },

    handleAddNewSubTaskClicked: function (oContent) {
      TaskStore.handleAddNewSubTaskClicked(oContent);
    },

    handleSaveSubTaskValue: function () {
      TaskStore.handleSaveSubTaskValue();
    },

    handleNewSubTaskLabelValueChanged: function (sLabel) {
      TaskStore.handleNewSubTaskLabelValueChanged(sLabel);
    },

    handleSubTaskEditButtonClicked: function (sSubTaskId) {
      TaskStore.handleSubTaskEditButtonClicked(sSubTaskId);
    },

    handleSubTaskDeleteButtonClicked: function (sSubTaskId) {
      TaskStore.handleSubTaskDeleteButtonClicked(sSubTaskId);
    },

    createTaskComment: function () {
      TaskStore.createTaskComment();
    },

    handleCommentTextChanged: function (sValue) {
      TaskStore.handleCommentTextChanged(sValue);
    },

    handleTaskValueChanged: function (aNewValue, sContext) {
      TaskStore.handleTaskValueChanged(aNewValue, sContext);
    },

    handleTaskDescriptionChanged: function (sContext, sKey, sValue) {
      switch (sContext){
        case  'task':
          TaskStore.handleTaskDescriptionChanged(sValue);
          break;
      }
    },

    handleTaskDueDateChanged: function (sValue, sContext) {
      TaskStore.handleTaskDueDateChanged(sValue, sContext);
    },

    handleSubTaskClicked: function (sId) {
      TaskStore.handleSubTaskClicked(sId);
    },

    handleToggleCollapsedState: function (sContext) {
      TaskStore.handleToggleCollapsedState(sContext);
    },

    handleTasksRoleMSSValueChanged: function (sContext, sAction, aSelectedRoles) {
      TaskStore.handleTasksRoleMSSValueChanged(sContext, sAction, aSelectedRoles);
    },

    handleContentTaskDetailFormValueChanged: function (sId, sNewValue) {
      TaskStore.handleContentTaskDetailFormValueChanged(sId, sNewValue);
    },

    handleContentTaskBPNMDialogButtonClicked: function (sId) {
      TaskStore.handleContentTaskBPNMDialogButtonClicked(sId);
    },

    openProductFromDashboard: function (sContentId, sBaseType) {
      TaskStore.openProductFromDashboard(sContentId, sBaseType);
    },

    handleTaskListToolBarClicked: function (sEvent, sContext) {
      switch (sContext) {
        case "task":
          _handleTaskListToolBarClicked(sEvent);
          break;
      }
    },

    createAnnotation: function (iXPosition, iYPosition, oContent) {
      TaskStore.handleImageSimpleAnnotationViewCreateAnnotationClicked(iXPosition, iYPosition, oContent);
    },

    openAnnotation: function (sTaskId) {
      TaskStore.handleImageSimpleAnnotationViewOpenTaskDetail(sTaskId);
    },

    handleImageAnnotationSaveTaskClicked: function () {
      TaskStore.handleSaveTaskClicked();
    },

    handleSaveTaskClicked:function(){
      TaskStore.handleSaveTaskClicked();
    },

    handleDiscardTaskClicked: function () {
      TaskStore.discardTask();
    },

    handleCoverflowDetailViewShowAnnotationButtonClicked: function () {
      TaskStore.handleCoverflowDetailViewShowAnnotationButtonClicked();
    },

    handleContentEditToolbarButtonClicked: function (sId) {
      let sTaskMode = TaskProps.getTaskMode();

      switch (sTaskMode) {
        case TaskViewConstants.CONTENT_TASK:
          TaskStore.handleContentEditToolbarButtonClicked(sId);
          break;
      }
    },

    handleFileAttachmentUploadClicked: function (sContext, aFiles) {
      TaskStore.handleFileAttachmentUploadClicked(sContext, aFiles);
    },

    handleFileAttachmentRemoveClicked: function (sContext, sId) {
      TaskStore.handleTaskFileAttachmentRemoveClicked(sId);
    },

    handleFileAttachmentDetailViewOpen: function (sAttachmentId) {
      TaskStore.handleFileAttachmentDetailViewOpen(sAttachmentId);
    },

    handleFileAttachmentDetailViewClose: function () {
      TaskStore.handleFileAttachmentDetailViewClose();
    },

    handleFileAttachmentGetAllAssetExtensions: function (oRefDom){
      TaskStore.handleFileAttachmentGetAllAssetExtensions(oRefDom);
    },

    handleTaskDetailDialogButtonClicked: function (oExtraData) {
      _handleTaskDetailDialogButtonClicked(oExtraData)
    },

    handleNotificationClicked: function (sNotificationId) {
      NotificationStore.handleNotificationClicked(sNotificationId);
    },

    handleNotificationLoadMoreClicked: function () {
      NotificationStore.handleNotificationLoadMoreClicked();
    },

    handleClearNotificationClicked: function (sId) {
      NotificationStore.handleClearNotificationsClicked(sId);
    },

    handleClearAllNotificationsClicked: function () {
      NotificationStore.handleClearAllNotificationsClicked();
    },

    handleRefreshNotificationsClicked: function () {
      NotificationStore.fetchNotifications();
    },

    updateTaskProps: function (oData) {
      TaskStore.updateTaskProps(oData);
    },

    setTaskViewForContent: function (sId, sContext) {
      TaskStore.setTaskViewForContent(sId, sContext);
    },

    setTaskViewForDashboard: function (oCallback) {
      TaskStore.setTaskViewForDashboard(oCallback);
    },

    handleTaskDialogCloseClicked: function () {
      TaskStore.handleTaskDialogCloseClicked();
    },

    clearAllTaskProps: function () {
      TaskStore.clearAllTaskProps();
    },

    fetchAnnotationData: function (oProperty) {
      TaskStore.fetchAnnotationData(oProperty);
    },

    handleControlD: function () {
      _handleControlD();
    },

    setTaskMode: function (_sMode) {
      TaskStore.setTaskMode(_sMode);
    },

    handleControlS: function() {
      _handleControlS();
    },

    handleControlESC: function() {
      _handleControlESC();
    },

    handleWorkflowTemplateDialogButtonClicked: function (sButton){
      TaskStore.handleWorkflowTemplateDialogButtonClicked(sButton);
    },

    handleWorkflowTemplateApplyButtonClicked: function (sContext, sSelectedTemplateWorkflow) {
      TaskStore.handleWorkflowTemplateApplyButtonClicked(sContext, sSelectedTemplateWorkflow);
    },

    handleWorkflowTemplateMSSChanged: function (aSelectedItems, sContext, oReferencedData) {
      TaskStore.handleWorkflowTemplateMSSChanged(aSelectedItems, sContext, oReferencedData);
    },

    handleWorkflowTemplateCommonConfigSectionSingleTextChanged: function (sContext, sKey, sVal) {
      TaskStore.handleWorkflowTemplateCommonConfigSectionSingleTextChanged(sContext, sKey, sVal);
    },

    handleWorkflowTemplateTabLayoutTabChanged: function (sTabId) {
      TaskStore.handleWorkflowTemplateTabLayoutTabChanged(sTabId);
    },

    handleProcessFrequencySummaryDateButtonClicked: function (sDate, sContext) {
      TaskStore.handleProcessFrequencySummaryDateButtonClicked(sDate, sContext);
    },

    handleScheduledWorkflowToggled: function (oData, sValue) {
      TaskStore.handleScheduledWorkflowToggled(oData, sValue);
    },

    handleScheduledWorkflowDeleteButtonClicked: function (sWorkflowId) {
      TaskStore.handleScheduledWorkflowDeleteButtonClicked(sWorkflowId);
    },

    handleScheduledWorkflowBulkDeleteButtonClicked: function () {
      TaskStore.handleScheduledWorkflowBulkDeleteButtonClicked();
    },

    handleProcessFrequencyDaysCrossIconClicked: function (aContextKey, sId) {
      TaskStore.handleProcessFrequencyDaysCrossIconClicked(aContextKey, sId);
    },

    handleTaskFormValueChanged: function(sDate, sContext, oModel){
      TaskStore.handleTaskFormValueChanged(sDate, sContext, oModel);
    },

    triggerChange: function () {
      _triggerChange();
    },
  };

})();

MicroEvent.mixin(TaskScreenStore);

TaskStore.bind('task-change', TaskScreenStore.triggerChange);
NotificationStore.bind('notification-change', TaskScreenStore.triggerChange);

export default TaskScreenStore;
