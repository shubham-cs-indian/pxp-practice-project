import CS from '../../../libraries/cs';
import LogFactory from '../../../libraries/logger/log-factory';

import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import ActionInterceptor from '../../../libraries/actioninterceptor/action-interceptor.js';
import { events as KeydownEvents } from '../../../libraries/keydownhandler/keydownhandler';
import { events as ContentEditToolbarViewEvents } from '../../../screens/homescreen/screens/contentscreen/view/content-edit-toolbar-view';
import { events as FileAttachmentsViewEvents } from '../../../viewlibraries/fileattachmentsview/file-attachments-view';
import { events as NotificationViewEvents } from './../view/notifications-view';
import { events as PropertyTaskDialogEvents } from './../view/property-task-dialog-view';
import { events as TaskEntityViewEvents } from '../../../viewlibraries/taskentityview/task-entity-view';
import { events as TasksViewEvents } from './../view/task-view';
import { events as TaskListViewEvents } from './../view/task-list-view';
import { events as TaskDetailViewWrapperViewEvents } from './../view/task-detail-view-wrapper';
import { events as TaskDetailViewEvents } from './../view/task-detail-view';
import { events as TaskDetailDialogEvents } from './../view/task-detail-dialog-view';
import TaskScreenStore from './../store/task-screen-store';
import {events as NewMultiSelectSearchViewEvents} from "../../../viewlibraries/multiselectview/multi-select-search-view";
import {events as CommonConfigSectionViewEvents} from "../../../viewlibraries/commonconfigsectionview/common-config-section-view";
import {events as ProcessFrequencySummaryViewEvents} from "../../../viewlibraries/processFrequencySummaryView/process-frequency-summary-view";
import {events as WorkflowViewEvents} from "./../view/workflows-view";
import {events as EntityTasksFormEvents } from '../../../viewlibraries/entitytasksformview/entity-tasks-form-view';

const logger = LogFactory.getLogger('task-action');

const TaskAction = (function () {
  const oEventHandler = {};

  let handleTaskLevelListNodeClicked = function (oTaskListItem, iListLevelIndex) {
    TaskScreenStore.handleTaskLevelListNodeClicked(oTaskListItem, iListLevelIndex);
  };

  let handleTaskLevelListHeaderClicked = function (oTaskListItem, iListLevelIndex) {
    TaskScreenStore.handleTaskLevelListHeaderClicked(oTaskListItem, iListLevelIndex);
  };

  let handleTaskListSearch = function (oTaskListItem, iListLevelIndex, sSearchText) {
    TaskScreenStore.handleTaskListSearch(oTaskListItem, iListLevelIndex, sSearchText);
  };

  let handleTaskListLoadMore = function (oTaskListItem, iListLevelIndex) {
    TaskScreenStore.handleTaskListLoadMore(oTaskListItem, iListLevelIndex);
  };

  let handleWorkflowTemplateDialogButtonClicked = function (sButton){
    TaskScreenStore.handleWorkflowTemplateDialogButtonClicked(sButton);
  };

  let handleWorkflowTemplateApplyButtonClicked = function (sContext, sSelectedTemplateWorkflow){
    TaskScreenStore.handleWorkflowTemplateApplyButtonClicked(sContext, sSelectedTemplateWorkflow);
  };

  let handleWorkflowTemplateMSSChanged = function (aSelectedItems, sContext, oReferencedData){
    TaskScreenStore.handleWorkflowTemplateMSSChanged(aSelectedItems, sContext, oReferencedData);
  };

  let handleWorkflowTemplateCommonConfigSectionSingleTextChanged = function (oContext, sContext, sKey, sVal){
    TaskScreenStore.handleWorkflowTemplateCommonConfigSectionSingleTextChanged(sContext, sKey, sVal);
  };

  let handleWorkflowTemplateTabLayoutTabChanged = function (sTabId){
    TaskScreenStore.handleWorkflowTemplateTabLayoutTabChanged(sTabId);
  };

  let handleProcessFrequencySummaryDateButtonClicked = function (sDate, sContext) {
    TaskScreenStore.handleProcessFrequencySummaryDateButtonClicked(sDate, sContext);
  };

  let handleScheduledWorkflowToggled = function (oData, sValue) {
    TaskScreenStore.handleScheduledWorkflowToggled(oData, sValue);
  };

  let handleScheduledWorkflowDeleteButtonClicked = function (sWorkflowId) {
    TaskScreenStore.handleScheduledWorkflowDeleteButtonClicked(sWorkflowId);
  };

  let handleScheduledWorkflowBulkDeleteButtonClicked = function () {
    TaskScreenStore.handleScheduledWorkflowBulkDeleteButtonClicked();
  };

  const handleTaskListClicked = function (sContext, sId) {
    TaskScreenStore.handleTaskListClicked(sContext, sId);
  };

  const handleTaskListCheckClicked = function (sContext, bIsChecked, sId) {
    TaskScreenStore.handleTaskListCheckClicked(sContext, bIsChecked, sId);
  };

  const handleAllTaskListCheckClicked = function (sContext, sSearchText) {
    TaskScreenStore.handleAllTaskListCheckClicked(sContext, sSearchText);
  };

  const handleAddNewTaskClicked = function (sContext, sTypeId, oContent) {
    TaskScreenStore.handleAddNewTaskClicked(sContext, sTypeId, oContent);
  };

  const handleLoadMoreTaskClicked = function (sTypeId) {
  };

  const handleTaskLabelValueChanged = function (sContext, sKey, sId, sValue) {
    TaskScreenStore.handleTaskLabelValueChanged(sContext, sKey, sId, sValue);
  };

  const handleTaskNameOnBlur = function (sContext, sId) {
    TaskScreenStore.handleTaskNameOnBlur(sContext, sId);
  };

  const handleGroupByDropDownClicked = function (sContext, sId) {
    TaskScreenStore.handleGroupByDropDownClicked(sContext, sId);
  };

  const handleDeleteTaskClicked = function (sContext, sId, oActiveContent) {
    TaskScreenStore.handleDeleteTaskClicked(sContext, sId, oActiveContent);
  };

  const handleSaveTaskClicked = function (sContext) {
    TaskScreenStore.handleSaveTaskClicked(sContext);
  };

  const handleTasksSearchTextChanged = function (sSearchString, sContext) {
    TaskScreenStore.handleTasksSearchTextChanged(sSearchString, sContext);
  };

  const handleListCollapsedStateChanged = function (sTypeId, sContext) {
    TaskScreenStore.handleListCollapsedStateChanged(sTypeId, sContext);
  };

  const handleSubtaskBackButtonClicked = function (sId) {
    TaskScreenStore.handleSubtaskBackButtonClicked(sId);
  };

  const handleResetScrollState = function () {
    TaskScreenStore.handleResetScrollState();
  };

  const handleTaskVisibilityModeChanged = function (sId, sIsPublic) {
    TaskScreenStore.handleTaskVisibilityModeChanged(sId, sIsPublic);
  };

  const handleCloseTaskDetailClicked = function (sContext) {
    TaskScreenStore.handleCloseTaskDetailClicked(sContext);
  };

  const handleSubTaskListCheckBoxClicked = function (sSubtaskId, bSubTaskChecked) {
    TaskScreenStore.handleSubTaskListCheckBoxClicked(sSubtaskId, bSubTaskChecked);
  };

  const handleAddNewSubTaskClicked = function (oContent) {
    TaskScreenStore.handleAddNewSubTaskClicked(oContent);
  };

  const handleSaveSubTaskValue = function () {
    TaskScreenStore.handleSaveSubTaskValue();
  };

  const handleNewSubTaskLabelValueChanged = function (sLabel) {
    TaskScreenStore.handleNewSubTaskLabelValueChanged(sLabel);
  };

  const handleSubTaskEditButtonClicked = function (sSubTaskId) {
    TaskScreenStore.handleSubTaskEditButtonClicked(sSubTaskId);
  };

  const handleSubTaskDeleteButtonClicked = function (sSubTaskId) {
    TaskScreenStore.handleSubTaskDeleteButtonClicked(sSubTaskId);
  };

  const createTaskComment = function () {
    TaskScreenStore.createTaskComment();
  };

  const handleCommentTextChanged = function (sValue) {
    TaskScreenStore.handleCommentTextChanged(sValue);
  };

  const handleTaskValueChanged = function (aNewValue, sContext) {
    TaskScreenStore.handleTaskValueChanged(aNewValue, sContext);
  };

  const handleTaskDescriptionChanged = function (sContext, sKey, sValue) {
    TaskScreenStore.handleTaskDescriptionChanged(sContext, sKey, sValue);
  };

  const handleTaskDueDateChanged = function (sValue, sContext) {
    TaskScreenStore.handleTaskDueDateChanged(sValue, sContext);
  };

  const handleSubTaskClicked = function (sId) {
    TaskScreenStore.handleSubTaskClicked(sId);
  };

  const handleToggleCollapsedState = function (sContext) {
    TaskScreenStore.handleToggleCollapsedState(sContext);
  };

  const handleTasksRoleMSSValueChanged = function (sContext, sAction, aSelectedRoles) {
    TaskScreenStore.handleTasksRoleMSSValueChanged(sContext, sAction, aSelectedRoles);
  };

  let handleContentTaskDetailFormValueChanged = function (sId, sNewValue) {
    TaskScreenStore.handleContentTaskDetailFormValueChanged(sId, sNewValue);
  };

  let handleContentTaskBPNMDialogButtonClicked = function (sId) {
    TaskScreenStore.handleContentTaskBPNMDialogButtonClicked(sId);
  };

  /** Todo : Will handle later **/

  let handleOpenProductFromTaskDashboard = function (sContentId, sBaseType) {
    TaskScreenStore.openProductFromDashboard(sContentId, sBaseType);
  };

  const handleTaskListToolBarClicked = function (sEvent, sContext) {
    TaskScreenStore.handleTaskListToolBarClicked(sEvent, sContext);
  };

  const handleTaskDetailDialogButtonClicked = function (oExtraData) {
    TaskScreenStore.handleTaskDetailDialogButtonClicked(oExtraData)
  };

  const handleDiscardTaskClicked = function () {
    TaskScreenStore.handleDiscardTaskClicked();
  };

  const handleContentEditToolbarButtonClicked = function (sId) {
    TaskScreenStore.handleContentEditToolbarButtonClicked(sId);
  };

  const handleFileAttachmentUploadClicked = function (sContext, aFiles) {
    TaskScreenStore.handleFileAttachmentUploadClicked(sContext, aFiles);
  };

  const handleFileAttachmentRemoveClicked = function (sContext, sId) {
    TaskScreenStore.handleFileAttachmentRemoveClicked(sContext, sId);
  };

  const handleFileAttachmentDetailViewOpen = function (sAttachmentId) {
    TaskScreenStore.handleFileAttachmentDetailViewOpen(sAttachmentId);
  };

  const handleFileAttachmentDetailViewClose = function () {
    TaskScreenStore.handleFileAttachmentDetailViewClose();
  };

  const handleFileAttachmentGetAllAssetExtensions = function (oRefDom) {
    TaskScreenStore.handleFileAttachmentGetAllAssetExtensions(oRefDom);
  };

  const handleNotificationClicked = function (sNotificationId) {
    TaskScreenStore.handleNotificationClicked(sNotificationId);
  };

  const handleNotificationLoadMoreClicked = function () {
    TaskScreenStore.handleNotificationLoadMoreClicked();
  };

  const handleClearNotificationClicked = function (sId) {
    TaskScreenStore.handleClearNotificationClicked(sId);
  };

  const handleClearAllNotificationsClicked = function () {
    TaskScreenStore.handleClearAllNotificationsClicked();
  };

  const handleRefreshNotificationsClicked = function () {
    TaskScreenStore.handleRefreshNotificationsClicked();
  };

  const handleTaskDialogCloseClicked = function () {
    TaskScreenStore.handleTaskDialogCloseClicked();
  };

  const handleTaskFormValueChanged = function (sDate, sContext, oModel) {
    TaskScreenStore.handleTaskFormValueChanged(sDate, sContext, oModel);
  };

  const handleControlD = function () {
    TaskScreenStore.handleControlD();
  };

  const handleControlS = function () {
    TaskScreenStore.handleControlS();
  };

  const handleControlESC = function () {
    TaskScreenStore.handleControlESC();
  };

  /**
   * Binding Events into EventHandler
   */
  (() => {
    //Kew down events
    oEventHandler[KeydownEvents.CTRL_D] = ActionInterceptor('Control + D', handleControlD);

    oEventHandler[KeydownEvents.CTRL_S] = ActionInterceptor('Control + S', handleControlS);
    oEventHandler[KeydownEvents.ESC] = ActionInterceptor('Control + Esc', handleControlESC);

    //Image Annotation View Events
    oEventHandler[TaskDetailDialogEvents.TASK_DETAIL_DIALOG_BUTTON_CLICKED] = ActionInterceptor('', handleTaskDetailDialogButtonClicked);


    oEventHandler[PropertyTaskDialogEvents.HANDLE_PROPERTY_TASK_CLOSE_CLICKED] = ActionInterceptor('Task Dialog Close Clicked', handleTaskDialogCloseClicked);
    oEventHandler[PropertyTaskDialogEvents.HANDLE_PROPERTY_TASK_SAVE_EVENT] = ActionInterceptor('Handle save task clicked', handleSaveTaskClicked);
    oEventHandler[PropertyTaskDialogEvents.HANDLE_PROPERTY_TASK_DISCARD_CLICKED] = ActionInterceptor('Handle save task clicked', handleDiscardTaskClicked);

    oEventHandler[NotificationViewEvents.NOTIFICATION_CLICKED] = ActionInterceptor('handle notification clicked', handleNotificationClicked);
    oEventHandler[NotificationViewEvents.NOTIFICATIONS_LOAD_MORE_CLICKED] = ActionInterceptor('handle notification load more clicked', handleNotificationLoadMoreClicked);
    oEventHandler[NotificationViewEvents.NOTIFICATIONS_CLEAR_NOTIFICATION_CLICKED] = ActionInterceptor('handle clear notification clicked', handleClearNotificationClicked);
    oEventHandler[NotificationViewEvents.NOTIFICATIONS_CLEAR_ALL_NOTIFICATIONS_CLICKED] = ActionInterceptor('handle clear all notification clicked', handleClearAllNotificationsClicked);
    oEventHandler[NotificationViewEvents.NOTIFICATIONS_REFRESH_CLICKED] = ActionInterceptor('handle refresh notification clicked', handleRefreshNotificationsClicked);

    //for file attachment view in task screen
    oEventHandler[FileAttachmentsViewEvents.FILE_ATTACHMENT_VIEW_FILE_UPLOAD_CLICKED] = ActionInterceptor('Handle file attachment upload clicked', handleFileAttachmentUploadClicked);
    oEventHandler[FileAttachmentsViewEvents.FILE_ATTACHMENT_VIEW_REMOVE_FILE] = ActionInterceptor('Handle file attachment remove clicked', handleFileAttachmentRemoveClicked);
    oEventHandler[FileAttachmentsViewEvents.FILE_ATTACHMENTS_DETAIL_VIEW_OPEN] = ActionInterceptor('Handle file attachment remove clicked', handleFileAttachmentDetailViewOpen);
    oEventHandler[FileAttachmentsViewEvents.FILE_ATTACHMENTS_DETAIL_VIEW_CLOSE] = ActionInterceptor('Handle file attachment remove clicked', handleFileAttachmentDetailViewClose);
    oEventHandler[FileAttachmentsViewEvents.FILE_ATTACHMENT_VIEW_GET_ALL_ASSET_EXTENSIONS] = ActionInterceptor('Handle file attachment get all asset extensions', handleFileAttachmentGetAllAssetExtensions);


    oEventHandler[ContentEditToolbarViewEvents.CONTENT_EDIT_TOOLBAR_HANDLE_BUTTON_CLICKED] = ActionInterceptor('handle content edit toolbar button clicked', handleContentEditToolbarButtonClicked);

    //TasksViewEvents
    oEventHandler[TasksViewEvents.HANDLE_TASK_LIST_BY_LEVEL_CLICKED] = ActionInterceptor('Handle Task List By Level Clicked', handleTaskLevelListNodeClicked);
    oEventHandler[TasksViewEvents.HANDLE_TASK_LIST_BY_HEADER_CLICKED] = ActionInterceptor('Handle Task List By Level Clicked', handleTaskLevelListHeaderClicked);
    oEventHandler[TasksViewEvents.HANDLE_TASK_LIST_SEARCH] = ActionInterceptor('Handle Tasks List Search', handleTaskListSearch);
    oEventHandler[TasksViewEvents.HANDLE_TASK_LIST_REFRESH_NOTIFICATION] = ActionInterceptor('Refresh Notification', handleRefreshNotificationsClicked);
    oEventHandler[TasksViewEvents.HANDLE_TASK_LIST_LOAD_MORE_CLICKED] = ActionInterceptor('Handle Tasks Load More', handleTaskListLoadMore);
    oEventHandler[TasksViewEvents.HANDLE_WORKFLOW_TEMPLATE_DIALOG_BUTTON_CLICKED] = ActionInterceptor('Handle Workflow Template Dialog Button Clicked', handleWorkflowTemplateDialogButtonClicked);
    oEventHandler[TasksViewEvents.HANDLE_WORKFLOW_TEMPLATE_APPLY_BUTTON_CLICKED] = ActionInterceptor('handle workflow Template Apply Button Clicked', handleWorkflowTemplateApplyButtonClicked);

    //TaskListViewEvents
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_NODE_CLICKED] = ActionInterceptor('Handle Task List Clicked', handleTaskListClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_TASK_CHECKBOX_CLICKED] = ActionInterceptor('Handle Task List Checkbox Clicked', handleTaskListCheckClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_SELECT_ALL_CHECKBOX_CLICKED] = ActionInterceptor('Handle Task List Checkbox Clicked', handleAllTaskListCheckClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_ADD_NEW_TASK_CLICKED] = ActionInterceptor('Handle add new task clicked', handleAddNewTaskClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_LOAD_MORE_BTN_CLICKED] = ActionInterceptor('Handle add load more clicked', handleLoadMoreTaskClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LABEL_VALUE_CHANGED] = ActionInterceptor('Handle task label changed', handleTaskLabelValueChanged);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_TASK_NAME_ON_BLUR] = ActionInterceptor('Handle task name edit clicked', handleTaskNameOnBlur);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_GROUP_BY_DROP_DOWN_CLICKED] = ActionInterceptor('Handle task group by dropdown node clicked', handleGroupByDropDownClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_DELETE_TASK_CLICKED] = ActionInterceptor('Handle delete task clicked', handleDeleteTaskClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_TASK_SAVE_EVENT] = ActionInterceptor('Handle save task clicked', handleSaveTaskClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_SEARCH_TEXT_CHANGED] = ActionInterceptor('Handle search text changed', handleTasksSearchTextChanged);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_TOOLBAR_CLICKED] = ActionInterceptor('Handle Task List ToolBar Clicked', handleTaskListToolBarClicked);
    oEventHandler[TaskListViewEvents.CONTENT_TASK_LIST_COLLAPSED_STATE_CHANGED] = ActionInterceptor('Content list collapsed state changed', handleListCollapsedStateChanged);

    //TaskDetailViewWrapperViewEvents
    oEventHandler[TaskDetailViewWrapperViewEvents.CONTENT_TASK_DETAIL_TASK_SAVE_CLICKED] = ActionInterceptor('Handle task save clicked', handleSaveTaskClicked);
    oEventHandler[TaskDetailViewWrapperViewEvents.CONTENT_TASK_DETAIL_SUBTASK_BACK_BUTTON_CLICKED] = ActionInterceptor('Handle task save clicked', handleSubtaskBackButtonClicked);
    oEventHandler[TaskDetailViewWrapperViewEvents.CONTENT_TASK_RESET_SCROLL_EVENT] = ActionInterceptor('Handle reset scroll event', handleResetScrollState);

    //TaskDetailViewEvents
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_TASK_LABEL_VALUE_CHANGED] = ActionInterceptor('Handle task label changed', handleTaskLabelValueChanged);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_TASK_VISIBILITY_MODE_CHANGED] = ActionInterceptor('Handle task visibility mode changed', handleTaskVisibilityModeChanged);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_CLOSE_TASK_DETAIL_CLICKED] = ActionInterceptor('Handle Close Task Detail Clicked', handleCloseTaskDetailClicked);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_SUB_TASK_CHECKBOX_CLICKED] = ActionInterceptor('Handle SubTask List Checkbox Clicked', handleSubTaskListCheckBoxClicked);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_ADD_NEW_SUB_TASK_CLICKED] = ActionInterceptor('Handle Add New Sub Task Clicked', handleAddNewSubTaskClicked);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_SAVE_SUBTASK_VALUE] = ActionInterceptor('Handle Save Subtask Value', handleSaveSubTaskValue);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_NEW_SUB_TASK_LABEL_VALUE_CHANGED] = ActionInterceptor('Handle New Sub Task Label Value Changed', handleNewSubTaskLabelValueChanged);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_SUB_TASK_DELETE_BUTTON_CLICKED] = ActionInterceptor('Handle Sub task delete button clicked', handleSubTaskDeleteButtonClicked);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_SUB_TASK_EDIT_CLICKED] = ActionInterceptor('Handle Sub task edit button clicked', handleSubTaskEditButtonClicked);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_CREATE_COMMENT] = ActionInterceptor('Create task comment', createTaskComment);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_COMMENT_TEXT_CHANGED] = ActionInterceptor('Handle Comment Text Changed', handleCommentTextChanged);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_TASK_VALUE_CHANGED] = ActionInterceptor('Handle Task Value Changed', handleTaskValueChanged);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_DESCRIPTION_CHANGED] = ActionInterceptor('Handle Task Value Changed', handleTaskDescriptionChanged);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_DATE_FIELD_CHANGE] = ActionInterceptor('Handle task due date changed', handleTaskDueDateChanged);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_SUBTASK_CLICKED] = ActionInterceptor('Handle subtask clicked', handleSubTaskClicked);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_TOGGLE_COLLAPSED_STATE] = ActionInterceptor('Toggle collapsed state', handleToggleCollapsedState);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_TASK_ROLE_MSS_VALUE_CHANGED] = ActionInterceptor('Task Role value changed', handleTasksRoleMSSValueChanged);
    oEventHandler[TaskDetailViewEvents.CONTENT_TASK_DETAIL_FORM_VALUE_CHANGED] = ActionInterceptor('Content task detail form value changed', handleContentTaskDetailFormValueChanged);
    oEventHandler[TaskDetailViewEvents.BPNM_DIALOG_BUTTON_CLICKED] = ActionInterceptor('Content task detail form value changed', handleContentTaskBPNMDialogButtonClicked);

    //EntityTaskFormViewEvents
    oEventHandler[EntityTasksFormEvents.ENTITY_TASK_FORM_VALUE_CHANGED] = ActionInterceptor('Content task detail form value changed', handleTaskFormValueChanged);

    //TaskEntityViewEvents
    oEventHandler[TaskEntityViewEvents.CONTENT_TASK_DETAIL_CONTENT_NAME_CLICKED] = ActionInterceptor('Handle task visibility mode changed', handleOpenProductFromTaskDashboard);

    // Common Config View Events for WorkflowWorkbench Template
    oEventHandler[NewMultiSelectSearchViewEvents.MULTI_SELECT_POPOVER_CLOSED] = ActionInterceptor('WorkflowWorkbench: handleNewMSSViewPopOverClosed', handleWorkflowTemplateMSSChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_SINGLE_TEXT_CHANGED] = ActionInterceptor(
        'WorkflowWorkbench: Handle Common Config Section Single Text Changed', handleWorkflowTemplateCommonConfigSectionSingleTextChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_MAPPING_SUMMARY_TAB_CHANGED] = ActionInterceptor(
        'WorkflowWorkbench:  Handle Common Config Section Mapping Summary Tab Changed', handleWorkflowTemplateTabLayoutTabChanged);
    oEventHandler[ProcessFrequencySummaryViewEvents.PROCESS_FREQUENCY_SUMMARY_DATE_BUTTON_CLICKED] = ActionInterceptor('WorkflowWorkbench: process_frequency_summary_date_button_clicked', handleProcessFrequencySummaryDateButtonClicked);
    oEventHandler[WorkflowViewEvents.WORKFLOW_SCHEDULED_BUTTON_CLICKED] = ActionInterceptor('workflowworkbench: workflow_scheduled_button_clicked', handleScheduledWorkflowToggled);
    oEventHandler[WorkflowViewEvents.WORKFLOW_DELETE_BUTTON_CLICKED] = ActionInterceptor('workflowworkbench: workflow_delete_button_clicked', handleScheduledWorkflowDeleteButtonClicked);
    oEventHandler[WorkflowViewEvents.WORKFLOW_SEARCH_TEXT_CHANGED] = ActionInterceptor('workflowworkbench : Handle search text changed', handleTasksSearchTextChanged);
    oEventHandler[WorkflowViewEvents.WORKFLOW_LIST_SELECT_ALL_CHECKBOX_CLICKED] = ActionInterceptor('workflowworkbench : Handle List All Checkbox Clicked', handleAllTaskListCheckClicked);
    oEventHandler[WorkflowViewEvents.WORKFLOW_LIST_SELECT_CHECKBOX_CLICKED] = ActionInterceptor('workflowworkbench : Handle List Checkbox Clicked', handleTaskListCheckClicked);
    oEventHandler[WorkflowViewEvents.WORKFLOW_DELETE_ALL_BUTTON_CLICKED] = ActionInterceptor('workflowworkbench : workflow_delete_all_button_clicked', handleScheduledWorkflowBulkDeleteButtonClicked);
  })();

  return {

    registerEvent: function () {
      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        EventBus.addEventListener(sEventName, oHandler);
      });
    },

    deRegisterEvent: function () {
      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        EventBus.removeEventListener(sEventName, oHandler);
      });
    }
  }
})();

export default TaskAction;
