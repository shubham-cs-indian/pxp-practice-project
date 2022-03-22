import React from 'react';

import ReactPropTypes from 'prop-types';
import CS from './../../../libraries/cs';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../../viewlibraries/tooltipview/tooltip-view';
import { view as CustomDatePicker } from '../../../viewlibraries/customdatepickerview/customdatepickerview.js';
import { view as MultiSelectView } from '../../../viewlibraries/multiselectview/multi-select-search-view';
import { view as FileAttachmentsView } from '../../../viewlibraries/fileattachmentsview/file-attachments-view.js';
import { view as ImageSimpleView } from '../../../viewlibraries/imagesimpleview/image-simple-view';
import { view as CustomTextFieldView } from '../../../viewlibraries/customtextfieldview/custom-text-field-view';
import RoleIdDictionary from '../../../commonmodule/tack/role-id-dictionary';
import { view as ImageFitToContainerView } from '../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
import { view as FroalaWrapperView } from '../../../viewlibraries/customfroalaview/froala-wrapper-view';
import MockDataIdsForFroalaView from '../../../commonmodule/tack/mock-data-ids-for-froala-view';
import { view as DisconnectedHTMLView } from '../../../viewlibraries/disconnectedhtmlview/disconnected-html-view.js';
import { view as EntityTasksFormView } from '../../../viewlibraries/entitytasksformview/entity-tasks-form-view';
import { view as CustomDialogView } from '../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as CustomMaterialButtonView } from '../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import { view as BPNMWrapperView } from '../../../viewlibraries/bpmnwrapperview/runtime-bpmn-wrapper-view';
import ViewLibraryUtils from '../../../viewlibraries/utils/view-library-utils';
import CommonUtils from '../../../commonmodule/util/common-utils';
import { view as TaskEntityView } from '../../../viewlibraries/taskentityview/task-entity-view';
import { view as GroupMssWrapperView } from '../../../viewlibraries/filter/group-mss-wrapper-view';
import MockDataForTaskRolesMSS from "../tack/mock/mock-data-for-task-roles-mss";
import UniqueIdentifierGenerator from "../../../libraries/uniqueidentifiergenerator/unique-identifier-generator";

var oEvents = {
  CONTENT_TASK_DETAIL_SUB_TASK_EDIT_CLICKED: "handle_sub_task_edit_button_clicked",
  CONTENT_TASK_DETAIL_SUB_TASK_DELETE_BUTTON_CLICKED: "handle_sub_task_delete_button_clicked",
  CONTENT_TASK_DETAIL_NEW_SUB_TASK_LABEL_VALUE_CHANGED: "handle_new_sub_task_value_changed",
  CONTENT_TASK_DETAIL_SAVE_SUBTASK_VALUE: "handle_save_subtask_value",
  CONTENT_TASK_DETAIL_ADD_NEW_SUB_TASK_CLICKED: "handle_add_new_sub_task_clicked",
  CONTENT_TASK_DETAIL_SUB_TASK_CHECKBOX_CLICKED: "handle_sub_task_list_checkbox_clicked",
  CONTENT_TASK_DETAIL_CLOSE_TASK_DETAIL_CLICKED: "handle_close_task_detail_clicked",
  CONTENT_TASK_DETAIL_DESCRIPTION_CHANGED: "handle_task_detail_description_change",
  CONTENT_TASK_DETAIL_TASK_VALUE_CHANGED: "handle_task_value_changed",
  CONTENT_TASK_DETAIL_DATE_FIELD_CHANGE: "handle_task_date_field_changed",
  CONTENT_TASK_DETAIL_CREATE_COMMENT: "content_task_detail_view_create_comment",
  CONTENT_TASK_DETAIL_COMMENT_TEXT_CHANGED: "content_task_detail_view_comment_text_changed",
  CONTENT_TASK_DETAIL_TASK_LABEL_VALUE_CHANGED: "handle_task_label_changed",
  CONTENT_TASK_DETAIL_TASK_VISIBILITY_MODE_CHANGED: "handle_task_visibility_mode_changed",
  CONTENT_TASK_DETAIL_TASK_SAVE_CLICKED: "content_task_save_clicked",
  CONTENT_TASK_DETAIL_TOGGLE_COLLAPSED_STATE: "toggle_collapsed_state",
  CONTENT_TASK_DETAIL_TASK_ROLE_MSS_VALUE_CHANGED: "task_role_mss_value_changed",
  CONTENT_TASK_DETAIL_SUBTASK_CLICKED: "handle_subtask_clicked",
  CONTENT_TASK_DETAIL_FORM_VALUE_CHANGED: "content_task_detail_form_value_changed",
  BPNM_DIALOG_BUTTON_CLICKED: "bpnm_dialog_button_clicked",
};

const oPropTypes = {
  task: ReactPropTypes.object,
  parentTask: ReactPropTypes.object,
  isTaskDetailViewOpen: ReactPropTypes.bool,
  taskViewProps: ReactPropTypes.object,
  usersList: ReactPropTypes.array,
  currentUser: ReactPropTypes.object,
  priorityTags: ReactPropTypes.array,
  statusTags: ReactPropTypes.array,
  isDisabled: ReactPropTypes.bool,
  isCloseDetailViewVisible: ReactPropTypes.bool,
  toolBarVisibility: ReactPropTypes.object,
  context: ReactPropTypes.string,
  taskOrEvent: ReactPropTypes.string,
  isImageAnnotationView: ReactPropTypes.bool,
  referencedTask: ReactPropTypes.object,
  editableRoles: ReactPropTypes.array,
  referencedRoles: ReactPropTypes.object,
  activeTaskFormViewModels: ReactPropTypes.array,
  taskLinkedInstanceData: ReactPropTypes.object,
  taskRolesData: ReactPropTypes.array,
  referencedData: ReactPropTypes.object
};

/**
 * @class ContentTasksDetailView
 * @description Will be used to show Tasks details if any task is active
 * @memberOf Views
 * @property {object} [task]  - Contains Active task.
 * @property {object} [parentTask] - Contains Parent task details.
 * @property {bool} [isTaskDetailViewOpen] - To show Task Detail View.
 * @property {object} [taskViewProps] - Props related to tasks i.e isSelected.
 * @property {array} [usersList] - User List.
 * @property {object} [currentUser] - Current User details.
 * @property {array} [priorityTags] -
 * @property {array} [statusTags]
 * @property {bool} [isDisabled] - To disable Task Detail View.
 * @property {bool} [isCloseDetailViewVisible] - Used to show close button for Task Detail View.
 * @property {object} [toolBarVisibility] - Contains toolBar buttons visibility data.
 * @property {string} [context] - Used to differentiate  screens.
 * @property {string} [taskOrEvent] - To determined task or event is will be render.
 * @property {bool} [isImageAnnotationView] -
 * @property {object} [referencedTask] - Referenced Task.
 * @property {array} [editableRoles] - Contains editable roles data.
 * @property {object} [referencedRoles] - Referenced Roles.
 * @property {array} [activeTaskFormViewModels] -
 * @property {object} [taskLinkedInstanceData]
 */

class ContentTasksDetailView extends React.Component {
  static propTypes = oPropTypes;

  constructor(props) {
    super(props);
    this.state = {
      isEditableName: false,
      isDirty: false
    };
  }

  handleContentTaskFormValueChanged = (oModel, sNewValue) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_FORM_VALUE_CHANGED, oModel.id, sNewValue);
  };

  handleBPNMDialogButtonClicked = (sId) => {
    EventBus.dispatch(oEvents.BPNM_DIALOG_BUTTON_CLICKED, sId);
  };

  handleToggleCollapsedState = (sContext) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_TOGGLE_COLLAPSED_STATE, sContext);
  };

  handleSubTaskClicked = (sId, oEvent) => {
    var oSubTaskProps = this.props.taskViewProps.oSubTaskProps;
    if (!oEvent.nativeEvent.dontRaise && oSubTaskProps.editableSubTaskId !== sId) {
      EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_SUBTASK_CLICKED, sId);
    }
  };

  handleTaskNameOnFocus = () => {
    this.setState({
      isEditableName: true
    });
  };

  handleTaskNameOnBlur = () => {
    this.setState({
      isEditableName: false
    });
  };

  handleCommentTextAreaValueChanged = (oValue) => {
    var sValue = oValue.htmlValue;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_COMMENT_TEXT_CHANGED, sValue);
  };

  handleTaskDetailLabelChanged = (sKey, sId, sValue) => {
    var sTaskOrEvent = this.props.taskOrEvent;
    var oEntity = this.props.task;
    if(oEntity.name !== sValue) {
      EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_TASK_LABEL_VALUE_CHANGED, sTaskOrEvent, sKey, sId, sValue);
    }
  };

  handleVisibilityModeChanged = (sId, sIsPublic) => {
    if(!this.props.isDisabled) {
      EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_TASK_VISIBILITY_MODE_CHANGED, sId, sIsPublic);
    }
  };

  handleSubTaskDeleteButtonClicked = (sSubTaskId, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_SUB_TASK_DELETE_BUTTON_CLICKED, sSubTaskId);
  };

  handleSubTaskEditButtonClicked = (sSubTaskId, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_SUB_TASK_EDIT_CLICKED, sSubTaskId);
  };

  handleSubTaskListCheckBoxClicked = (bSubTaskChecked, sSubTaskId, oEvent) => {
    bSubTaskChecked = !bSubTaskChecked;
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_SUB_TASK_CHECKBOX_CLICKED, sSubTaskId, bSubTaskChecked);
  };

  handleSaveSubTaskLabel = (sSubTaskId) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_SAVE_SUBTASK_VALUE, sSubTaskId);
  };

  handleKeyDownClicked = (sId, oEvent) => {
    if (oEvent.which == 13 || oEvent.keyCode == 13) {
      this.handleSaveSubTaskLabel(sId);
    }
  };

  handleSubTaskLabelValueChanged = (oEvent) => {
    var sInputValue = oEvent.target.value;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_NEW_SUB_TASK_LABEL_VALUE_CHANGED, sInputValue);
  };

  handleCloseTaskDetailClicked = () => {
    var sTaskOrEvent = this.props.taskOrEvent;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_CLOSE_TASK_DETAIL_CLICKED, sTaskOrEvent);
  };

  handleTaskDescriptionChanged = (sKey, sValue) => {
    var sTaskOrEvent = this.props.taskOrEvent;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_DESCRIPTION_CHANGED, sTaskOrEvent, sKey, sValue);
  };

  handleTaskStatusChanged = (aNewValue) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_TASK_VALUE_CHANGED, aNewValue, 'status');
  };

  handleTaskTypeChanged = (aNewValue) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_TASK_VALUE_CHANGED, aNewValue, 'taskType');
  };

  handleTasksRoleMSSValueChanged = (sContext,sAction ,aNewValue) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_TASK_ROLE_MSS_VALUE_CHANGED,sContext,sAction, aNewValue);
    this.setState({isDirty: false});
  };

  handleTaskPriorityChanged = (aNewValue) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_TASK_VALUE_CHANGED, aNewValue, "priority");
  };

  handleTaskDateFieldChanged = (sKey, oNull, oDate) => {
    var sValue = oDate ? oDate.getTime() : "";
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_DATE_FIELD_CHANGE, sValue, sKey);
  };

  handleAddNewSubTaskClicked = () => {
    let oActiveContent = this.props.activeContent;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_ADD_NEW_SUB_TASK_CLICKED, oActiveContent);
  };

  getTasksPriorityTagView = (oTag) => {
    var oStyle = {
      backgroundColor: oTag.color
    };

    return (
        <TooltipView label={CS.getLabelOrCode(oTag)} placement="bottom">
          <div className="tasksPriorityTag" style={oStyle}>
          </div>
        </TooltipView>
    );
  };

  getTasksStatusTagView = (oTag) => {
    if (CS.isEmpty(oTag)) {
      return null;
    }

    var oIconView = null;
    if (oTag.icon) {
      var sIcon = ViewLibraryUtils.getIconUrl(oTag.icon);
      oIconView = (<div className="tasksTagIcon">
        <ImageFitToContainerView imageSrc={sIcon}/>
      </div>);
    }
    var oStyle = {};
    if (oTag.color) {
      oStyle.backgroundColor = oTag.color;
      oStyle.color = CommonUtils.getTextColorBasedOnBackgroundColor(oTag.color)
    }

    var sLabelClass = (oIconView) ? "tasksTagLabel iconVisible" : "tasksTagLabel ";
    return (
        <div className="tasksTagDetails" style={oStyle}>
          {oIconView}
          <TooltipView label={CS.getLabelOrCode(oTag)} placement="bottom">
            <div className={sLabelClass}>
              {CS.getLabelOrCode(oTag)}
            </div>
          </TooltipView>
        </div>
    );
  };

  getSubtaskView = (bIsDisabled) => {
    var _this = this;
    var __props = this.props;
    var oToolBarVisibility = __props.toolBarVisibility;
    var aSubTasksList = this.props.task.subTasks;
    var oSubTaskProps = this.props.taskViewProps.oSubTaskProps;
    var aSubTaskListView = [];

    CS.forEach(aSubTasksList, function (oSubTask) {
      var aListItemView = [];

      var bIsChecked = CS.includes(oSubTaskProps.checkedSubTasks, oSubTask.id);
      var sCheckBoxClassName = "taskListCheck";
      sCheckBoxClassName += bIsChecked ? " checked" : "";
      var sCheckboxTooltip = bIsChecked ? getTranslation().DESELECT : getTranslation().SELECT;

      if (!bIsDisabled) {
        aListItemView.push(<TooltipView label={sCheckboxTooltip}>
          <div className={sCheckBoxClassName}
  onClick={_this.handleSubTaskListCheckBoxClicked.bind(_this, bIsChecked, oSubTask.id)}
  disabled={bIsDisabled}/>
        </TooltipView>);
      }

      if (oSubTask.id == oSubTaskProps.editableSubTaskId) {
        var sSubTaskLabel = oSubTask.name;
        aListItemView.push(<input type="text"
                                  className="taskListLabelInput"
                                  value={sSubTaskLabel}
                                  onChange={_this.handleSubTaskLabelValueChanged}
                                  onKeyDown={_this.handleKeyDownClicked.bind(_this, oSubTask.id)}
                                  onBlur={_this.handleSaveSubTaskLabel.bind(_this, oSubTask.id)} autoFocus/>);
      } else {
        aListItemView.push(<div className="taskListLabel">{oSubTask.name}</div>);
      }

      if (!bIsDisabled) {
        aListItemView.push(<div className="deleteTask"
  onClick={_this.handleSubTaskDeleteButtonClicked.bind(_this, oSubTask.id)}/>);
        if (oSubTask.id !== oSubTaskProps.editableSubTaskId) {
          aListItemView.push(<TooltipView label={getTranslation().EDIT} placement="bottom">
            <div className="editTask" onClick={_this.handleSubTaskEditButtonClicked.bind(_this, oSubTask.id)}/>
          </TooltipView>);
        }
      }

      var oPriorityTag = oSubTask.priorityTagForList;
      if (oPriorityTag && oPriorityTag.color) {
        aListItemView.push(_this.getTasksPriorityTagView(oPriorityTag));
      }

      if (oSubTask.tagForList) {
        aListItemView.push(_this.getTasksStatusTagView(oSubTask.tagForList));
      }

      aSubTaskListView.push(<div className="taskListItem " key="taskListItem"
                                 onClick={_this.handleSubTaskClicked.bind(_this, oSubTask.id)}>{aListItemView}</div>);
    });

    if (!bIsDisabled) {
      aSubTaskListView.push(<div className="addNewTask" onClick={this.handleAddNewSubTaskClicked} key="addNewTask">
        <div className="addNewTaskIcon"/>
        <div className="addNewTaskLabel">{getTranslation().ADD_NEW_SUB_TASK}</div>
      </div>);
    }

    var oView = (<div className="subTasksContainer">
      {aSubTaskListView}
    </div>);

    return this.getTaskCollapsibleSectionFilledWithHeaderAndBody("subTasks", getTranslation().SUBTASKS, oView);
  };

  getBulkDeleteView = () => {
    var isEditable = !this.props.isDisabled;

    var oSubTaskProps = this.props.taskViewProps.oSubTaskProps;
    var isChecked = (oSubTaskProps.checkedSubTasks.length);

    if (!isEditable || !isChecked) {
      return null;
    } else {
      return (<TooltipView label={getTranslation().DELETE} placement="bottom">
        <div className="bulkDeleteIcon" onClick={this.handleSubTaskDeleteButtonClicked.bind(this, "")}>
        </div>
      </TooltipView>);
    }
  };

  getTaskCollapsibleSectionFilledWithHeaderAndBody = (sKey, sHeaderLabel, oBodyView, oBPNMData) => {

    var __props = this.props;
    var oTaskViewProps = __props.taskViewProps;
    var oSectionVisualState = oTaskViewProps.sectionVisualState[sKey] || {};
    var sClassName = (oSectionVisualState.isCollapsed) ? "taskSection collapsed " : "taskSection ";
    var sTooltip = (oSectionVisualState.isCollapsed) ? getTranslation().EXPAND : getTranslation().COLLAPSE;
    var oBulkDeleteView = (sKey == "subTasks") ? this.getBulkDeleteView() : null;
    let sWarningMessage = getTranslation().WORKFLOW_STATUS_WARNING_MESSAGE;
    if(sKey === "workflowStatus" && CS.isEmpty(oBPNMData.processDefination)){
      oBodyView = <div className="workflowModifiedStatus">{sWarningMessage}</div>;
    }

    return (
        <div className={sClassName} key={"taskSection" + sKey}>
          <div className="taskDetailSeparator"></div>
          <div className="taskSectionHeader">
            <div className="taskSectionLabel"
                 onClick={this.handleToggleCollapsedState.bind(this, sKey)}>{sHeaderLabel}</div>
            <TooltipView label={sTooltip} placement="bottom">
              <div className="collapsedIcon" onClick={this.handleToggleCollapsedState.bind(this, sKey)}></div>
            </TooltipView>
            {oBulkDeleteView}
          </div>
          <div className="taskSectionDetails">
            {oBodyView}
          </div>
        </div>
    )
  };

  getUserIconView = (sIconKey) => {
    if (sIconKey) {
      var sIconUrl = ViewLibraryUtils.getIconUrl(sIconKey);
      return (<ImageSimpleView classLabel="commentIcon" imageSrc={sIconUrl}/>);
    } else {
      return (<div className="defaultCommentUserIcon"></div>);
    }
  };

  getCommentAttachmentsView = (oAttachmentsData, isUploadDisabled, sOpenAttachmentId) => {
    return (<div className="commentAttachmentViewContainer">
      <FileAttachmentsView
          attachmentsData={oAttachmentsData}
          context="commentFileAttachment"
          isUploadDisabled={isUploadDisabled}
          openAttachmentId={sOpenAttachmentId}/>
    </div>);
  };

  getCommentsList = () => {
    var _this = this;
    var aCommentsData = this.props.task.comments;
    var aSortedCommentData = CS.sortBy(aCommentsData, "timestamp");
    var aCommentsViewData = [];

    CS.forEach(aSortedCommentData, function (oCommentData) {
      var oCommentView = _this.getCommentListNodeView(oCommentData, false);
      aCommentsViewData.push(oCommentView);
    });

    return aCommentsViewData;
  };

  getCommentsView = () => {
    var oView = (
        <div className="commentsContainer">
          {this.getCommentsList()}
          {this.getCommentListNodeView({}, true)}
        </div>
    );

    return this.getTaskCollapsibleSectionFilledWithHeaderAndBody("comments", getTranslation().COMMENTS, oView);
  };

  getCommentListNodeView = (oCommentData, bIsNewComment) => {
    var oTask = this.props.task;
    var oTaskViewProps = this.props.taskViewProps;
    var oCommentAttachmentDataMap = oTaskViewProps.commentAttachmentDataMap;
    var sCommentedOn = ViewLibraryUtils.getDateAttributeInTimeFormat(oCommentData.timestamp);
    var oUserData = !bIsNewComment ? ViewLibraryUtils.getUserById(oCommentData.postedBy, this.props.usersList) : "";
    var oTemporaryCommentData = oTaskViewProps.commentTemporaryData;
    var aAttachmentsData = oCommentAttachmentDataMap[oCommentData.id];
    var oCommentButtonView = null;
    var bIsDisabled = oTask.isCreated || !bIsNewComment;

    var oCommentOnView = sCommentedOn ? <div className="commentDetails">{sCommentedOn}</div> : null;

    var oCommentTextView = <DisconnectedHTMLView content={oCommentData.text}
                                                 className="userCommentTextContainer"
                                                 dataId={oTask.id}/>;

    if (bIsNewComment) {
      let sKey = UniqueIdentifierGenerator.generateUUID();
      aAttachmentsData = oTaskViewProps.commentTemporaryData.commentAttachmentData;
      oCommentTextView = (<div className="userCommentTextBoxContainer">
        <FroalaWrapperView dataId={oTask.id}
                           activeFroalaId={bIsDisabled ? "" : oTask.id}
                           content={oTemporaryCommentData.commentText}
                           className="userCommentTextBox"
                           placeholder={getTranslation().COMMENT}
                           toolbarIcons={MockDataIdsForFroalaView}
                           fixedToolbar={true}
                           handler={this.handleCommentTextAreaValueChanged}
                           key={sKey}
        /></div>);
    }

    return (
        <div className="commentTaskEditContainer">
          <div className="commentTaskIconWrapper">
            <div className="commentTaskIconContainer">
              {this.getUserIconView(oUserData.icon)}
            </div>
          </div>
          <div className="commentsFormWrapper">
            <div className="userName">{oUserData.userName}</div>
            {oCommentOnView}
            {oCommentTextView}
            {this.getCommentAttachmentsView(aAttachmentsData, bIsDisabled, oTaskViewProps.sOpenAttachmentId)}
            {oCommentButtonView}
          </div>
        </div>
    );
  };

  getMultiSelectView = (
      aItems,
      aSelectedItems,
      isMultiSelect,
      bIsShowIcon,
      sContext,
      bIsDisabled,
      fCallback,
      sLabel,
      bCanNotRemove,
  ) => {
    var oTask = this.props.task;
    var sClassName = "taskMSSViewContainer ";
    sClassName += sContext;

    return (<div className={sClassName} key={"taskMSSView" + sContext + oTask.id}>
      <div className="taskMSSLabel">{sLabel} :</div>
      <div className="taskMSSDetail">
        <MultiSelectView
            items={aItems}
            selectedItems={aSelectedItems}
            isMultiSelect={isMultiSelect}
            bShowIcon={bIsShowIcon}
            context={sContext}
            disabled={bIsDisabled}
            onApply={fCallback}
            cannotRemove={bCanNotRemove}
        />
      </div>
    </div>);
  };

  getFileAttachmentView = (bIsDisabled) => {
    return (<div className="taskAttachmentContainer">
      <FileAttachmentsView
          attachmentsData={this.props.taskViewProps.attachmentData}
          context="taskFileAttachment"
          isUploadDisabled={bIsDisabled}
          openAttachmentId={this.props.taskViewProps.sOpenAttachmentId}/>
    </div>);
  };

  getCurrentTime = () => {
    var dt = new Date();
    dt.setHours(0);
    dt.setMinutes(0);
    dt.setSeconds(0);
    return dt.getTime();
  };

  getDateView = (sLabel, bIsDisabled, sKey, fOnChange) => {
    var oTask = this.props.task;
    var dDateToDisplay = oTask[sKey];
    var oMinDate = null;
    var oMaxDate = null;
    var oTextFieldStyle = {
      "width": "100%",
      "height": "30px"
    };

    var oUnderlineStyle = {
      "borderBottom": "none"
    };
    var oParentTask = this.props.parentTask;
    var sContext = this.props.context;
    var oDueDate = (dDateToDisplay) ? new Date(dDateToDisplay) : null;
    var bIsEndOfDay = false;
    if(sKey === "overDueDate"){
      oMinDate = (oTask.dueDate) ? new Date(oTask.dueDate) : new Date(oTask.startDate);
      if (sContext === "subtask") {
        oMaxDate = (oParentTask.overDueDate) ? new Date(oParentTask.overDueDate) : null;
      }
      bIsEndOfDay = true;
    } else if (sKey === "endTime"){
      oMinDate = (oTask.startTime) ? new Date(oTask.startTime) : null;
      bIsEndOfDay = true;
    } else if(sKey === "dueDate") {
      oMinDate = (oTask.startDate) ? new Date(oTask.startDate) : null;
      oMaxDate = (oTask.overDueDate) ? new Date(oTask.overDueDate) : null;
      bIsEndOfDay = true;
      if (sContext === "subtask") {
        oMaxDate = (oParentTask.dueDate) ? new Date(oParentTask.dueDate) : null;
      }
    } else if(sKey === "startDate") {
      var iCurrentTime = this.getCurrentTime();
      oMinDate = (oTask.createdOn < iCurrentTime) ? new Date(iCurrentTime) : new Date(oTask.createdOn);
      if(sContext === "subtask") {
        oMinDate = (oParentTask.startDate < iCurrentTime) ? new Date(iCurrentTime) : new Date(oParentTask.startDate);
      }
      oMaxDate = (oTask.dueDate) ? new Date(oTask.dueDate) : null;
    }
    return (
        <div className="taskDatePickerViewContainer" key={"taskMSSViewContainer" + sKey + oTask.id}>
          <div className="taskDatePickerLabel">{sLabel} : </div>
          <div className="taskDatePickerDetail">
            <CustomDatePicker
                value={oDueDate}
                minDate={oMinDate ? oMinDate : undefined}
                maxDate={oMaxDate ? oMaxDate : undefined}
                textFieldStyle={oTextFieldStyle}
                underlineStyle={oUnderlineStyle}
                disabled = {bIsDisabled}
                onChange={fOnChange}
                endOfDay={bIsEndOfDay}
            />
          </div>
        </div>
    );
  };

  getDescriptionView = () => {
    var oTask = this.props.task;
    var bIsDisabled = this.props.isDisabled;
    var sActiveTaskLabel = oTask.name;
    var oToolbarVisibility = this.props.toolBarVisibility;
    var oPublicButtonDom = (oToolbarVisibility.showPublicPrivateIcon) ? this.getPublicModeButtonDom() : null;
    if (this.state.isEditableName) {
      sActiveTaskLabel = CommonUtils.getFilteredName(sActiveTaskLabel);
    }

    var oAutoResizeStyle = {
      maxWidth: "calc(100% - 50px)"
    };

    return (
        <div className="descriptionContainer" key={"descriptionContainer" + oTask.id}>
          <div className="descriptionHeaderContainer">
            {oPublicButtonDom}
            <CustomTextFieldView value={sActiveTaskLabel}
                                 className="descriptionHeader"
                                 isMultiLine={true}
                                 onChange={this.handleTaskDetailLabelChanged.bind(this, 'name', oTask.id)}
                                 onBlur={this.handleTaskNameOnBlur}
                                 forceBlur={true}
                                 onFocus={this.handleTaskNameOnFocus}
                                 autosizeInput={true}
                                 autoSizeWrapperStyle={oAutoResizeStyle}
                                 isDisabled={bIsDisabled}/>
          </div>
          <div className="descriptionDetails" key={"descriptionDetails" + oTask.id}>
            <CustomTextFieldView value={oTask.longDescription} className="taskTextField"
                                 hintText={getTranslation().DESCRIPTION}
                                 isMultiLine={true}
                                 onBlur={this.handleTaskDescriptionChanged.bind(this, 'longDescription')}
                                 isDisabled={bIsDisabled}/>

          </div>
        </div>
    );
  };

  getSelectedRoles = (aUserIds, aRoleIds) => {
    let aSelectedRoles = [];
    let _this = this;
    CS.forEach(aUserIds, function (sUserId) {
      let oUser = CS.find(_this.props.usersList, {id: sUserId});
      aSelectedRoles.push({
            id: sUserId,
            label: oUser.label,
            groupType: "users"
          }
      )
    });
    CS.forEach(aRoleIds, function (sRoleId) {
      aSelectedRoles.push({
            id: sRoleId,
            groupType: "roles"
          }
      )
    });
    return aSelectedRoles;
  };

  getSingleRoleView = (sRoleId, sContext, sLabel, bIsMultiSelect) => {
    let oActiveTask = this.props.task;
    let aEditableRoles = this.props.editableRoles;
    let oRole = oActiveTask[sContext];
    let aSelectedRole = this.getSelectedRoles(oRole.userIds, oRole.roleIds);
    let bIsDisabled = this.props.isDisabled || !(CS.includes(aEditableRoles, sRoleId));
    let sClassName = "taskMSSViewContainer ";
    sClassName += sContext;
    let aTaskRolesData = this.props.taskRolesData;
    let aGroupOptionsToShow = MockDataForTaskRolesMSS[sContext];

    aTaskRolesData = CS.filter(aTaskRolesData, function (oRoles) {
        return CS.includes(aGroupOptionsToShow, oRoles.id);
    });

    return (
        <div className={sClassName} key={"taskMSSView" + sContext + oActiveTask.id}>
          <div className="taskMSSLabel">{sLabel} :</div>
          <div className="taskMSSDetail">
            <GroupMssWrapperView
                groupsData={aTaskRolesData}
                handleApplyButton={this.handleTasksRoleMSSValueChanged.bind(this, sContext, "add")}
                removeOption={this.handleTasksRoleMSSValueChanged.bind(this, sContext, "remove")}
                activeOptions={aSelectedRole}
                hideChips={false} // Chips is hidden always in xray view
                showPopup={true}
                showApply={true}
                isMultiSelect={bIsMultiSelect}
                ref={this.groupMss}
                disabled={bIsDisabled}
                referencedData={this.props.referencedData}
                handleOptionClicked={this.handleOptionClicked}
                isDirty={this.state.isDirty}
            />
          </div>
        </div>
    );
  };

  handleOptionClicked = () => {
    this.setState({
        isDirty: true
    });
  }

  getRolesView = () => {
    var oResponsibleView = this.getSingleRoleView(RoleIdDictionary.ResponsibleRoleId, "responsible", getTranslation().RESPONSIBLE, true);
    var oAccountableView = this.getSingleRoleView(RoleIdDictionary.AccountableRoleId, "accountable", getTranslation().ACCOUNTABLE, false);
    var oConsultedView = this.getSingleRoleView(RoleIdDictionary.ConsultedRoleId, "consulted", getTranslation().CONSULTED, true);
    var oInformedView = this.getSingleRoleView(RoleIdDictionary.InformedRoleId, "informed", getTranslation().INFORMED, true);
    var oVerifyView = this.getSingleRoleView(RoleIdDictionary.VerifyRoleId, "verify", getTranslation().VERIFIED, false);
    var oSignOffView = this.getSingleRoleView(RoleIdDictionary.SignOffRoleId, "signoff", getTranslation().SIGN_OFF, false);

    var oView = (
        <div className="rolesContainer">
          {oResponsibleView}
          {oAccountableView}
          {oConsultedView}
          {oInformedView}
          {oVerifyView}
          {oSignOffView}
        </div>
    );

    return this.getTaskCollapsibleSectionFilledWithHeaderAndBody("roles", getTranslation().RESPONSIBILITY_ASSIGNMENT, oView)
  };

  getMSSAnnotationTypeData = (oReferencedTasks) => {
    var aMSSList = [];

    CS.forEach(oReferencedTasks, function (oTask) {
      var oTaskListObject = {
        id: oTask.id,
        label: CS.getLabelOrCode(oTask)
      };
      aMSSList.push(oTaskListObject);
    });

    return aMSSList;
  };

  getSelectedTagValue = (oTag) => {
    var aTagValues = oTag ? oTag.tagValues : [];
    var aSelectedTagValues = [];
    var oSelectedTag = CS.find(aTagValues, {relevance: 100});
    if (oSelectedTag) {
      aSelectedTagValues.push(oSelectedTag.tagId);
    }
    return aSelectedTagValues;
  };

  getTaskTypeView = () => {
    var oTask = this.props.task;
    var aSelectedType = (!CS.isEmpty(oTask.types)) ? oTask.types : [];
    var aTaskType = this.getMSSAnnotationTypeData(this.props.referencedTask);
    return (this.getMultiSelectView(aTaskType, aSelectedType, false, false, "taskType", !oTask.isCreated,
        this.handleTaskTypeChanged, getTranslation().TYPE, true));
  };

  getTagView = (oTag, aTagListForMSS, sContext, sLabel, fCallback) => {
    let oTask = this.props.task;
    //TODO: oTask.isCreated??(is condition reqd)
    let bIsDisabled = this.props.isDisabled || oTask.isCamundaCreated;
    if (CS.isNotEmpty(oTask.accountable) && CS.isNotEmpty(oTask.responsible) && CS.isNotEmpty(oTask.signoff) && CS.isNotEmpty(oTask.verify)) {
      let oCurrentUser = CommonUtils.getCurrentUser();
      let bRole = oTask.accountable.roleIds.includes(oCurrentUser.roleId) ||
          oTask.responsible.roleIds.includes(oCurrentUser.roleId) || oTask.signoff.roleIds.includes(oCurrentUser.roleId) ||
          oTask.verify.roleIds.includes(oCurrentUser.roleId);
      let bUser = oTask.accountable.userIds.includes(oCurrentUser.id) ||
          oTask.responsible.userIds.includes(oCurrentUser.id) ||
          oTask.signoff.userIds.includes(oCurrentUser.id) ||
          oTask.verify.userIds.includes(oCurrentUser.id);
      if (bRole || bUser) {
        bIsDisabled = this.props.isDisabled;
      }
    }

    let aSelectedStatus = this.getSelectedTagValue(oTag);
    return (this.getMultiSelectView(aTagListForMSS, aSelectedStatus, false, false, sContext, bIsDisabled,
        fCallback, sLabel, true));
  };

  getTasksDateView = () => {
    var oVisibility = this.props.toolBarVisibility;
    var bIsDisabled = !oVisibility.canEditDates;
    var aDateView = [];

    var oStartDateView = this.getDateView(getTranslation().START_DATE, bIsDisabled,
        "startDate", this.handleTaskDateFieldChanged.bind(this, "startDate"));
    var oDueDateView = this.getDateView(getTranslation().DUE_DATE, bIsDisabled,
        "dueDate", this.handleTaskDateFieldChanged.bind(this, "dueDate"));
    var oEndDateView = this.getDateView(getTranslation().CREATED_ON, true, "createdOn");

    aDateView.push(oStartDateView);
    aDateView.push(oDueDateView);
    aDateView.push(oEndDateView);

    return aDateView;
  };

  getTaskFormView = () => {
    let __props = this.props;
    let oActiveTask = __props.task;
    if (oActiveTask.isCamundaCreated) {
      let oFormDOM = (<EntityTasksFormView models={this.props.activeTaskFormViewModels}
                                           onChangeHandler={this.handleContentTaskFormValueChanged}/>);
      return this.getTaskCollapsibleSectionFilledWithHeaderAndBody("taskForm", getTranslation().TASK_ACTIONS, oFormDOM);
    } else {
      return null;
    }
  };

  getBPNMDialog = (oBPNMData) => {
    let aButtonData = [{id: "bpnm_toggle_dialog", label: getTranslation().OK, isFlat: false}];
    let fButtonHandler = this.handleBPNMDialogButtonClicked
    var oBodyStyle = {
      padding: 0,
      maxHeight: 'none'
    };
    var oContentStyle = {
      position: 'absolute',
      transform: 'none',
      width: '94%',
      maxWidth: 'none'
    };
    return (<CustomDialogView buttonData={aButtonData} open={true} modal={true}
                              bodyClassName={"taskBpnmDialogBody"}
                              contentClassName={"taskBpnmDialogContent"}
                              contentStyle={oContentStyle}
                              bodyStyle={oBodyStyle}
                              title={getTranslation().WORKFLOW_STATUS}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler}>
      {this.getBPNMView(oBPNMData)}
    </CustomDialogView>)
  };

  getBPNMView = (oBPNMData) => {
    return (
        <BPNMWrapperView xmlData={oBPNMData.processDefination} completedActivityIds={oBPNMData.completedActivityIds}
                         currentActivityIds={oBPNMData.currentActivityIds}/>);
  };

  getBPNMWrapperView = () => {
    let __props = this.props;
    let oActiveTask = __props.task;
    let aViewsToReturn = [];
    if (oActiveTask.isCamundaCreated && oActiveTask.referencedProcessDefination) {
      let oBPNMData = oActiveTask.referencedProcessDefination;

      let oOpenInViewerButton = (<div className={"bpnmZoomButton"}><CustomMaterialButtonView label={getTranslation().OPEN_IN_VIEWER}
                                                                                             isRaisedButton={true}
                                                                                             onButtonClick={this.handleBPNMDialogButtonClicked.bind(this, "bpnm_toggle_dialog")}/>
      </div>);

      aViewsToReturn.push(this.getTaskCollapsibleSectionFilledWithHeaderAndBody("workflowStatus", getTranslation().WORKFLOW_STATUS, [this.getBPNMView(oBPNMData), oOpenInViewerButton],oBPNMData));
      __props.taskViewProps.isBPNMDialogOpen && (aViewsToReturn.push(this.getBPNMDialog(oBPNMData)));
    }
    return aViewsToReturn;
  };

  getTaskEntityView = () => {
    let oTaskLinkedInstanceData = this.props.taskLinkedInstanceData;
    let oContentDetailView = null;
    if(!CS.isEmpty(oTaskLinkedInstanceData)) {
      oContentDetailView = <TaskEntityView taskLinkedInstanceData={oTaskLinkedInstanceData}/>
      return this.getTaskCollapsibleSectionFilledWithHeaderAndBody("linkedEntity", getTranslation().OBJECT_DETAILS , oContentDetailView);
    }
    return null;
  };

  getTaskDetailView = () => {
    var oTask = this.props.task || {};
    var bIsDisabled = oTask.isCreated || this.props.isDisabled;
    var oToolbarVisibility = this.props.toolBarVisibility;
    var sContext = this.props.context;
    var sTaskOrEvent = this.props.taskOrEvent;

    if (CS.isEmpty(oTask)) {
      return null;
    }
    var oRepeatView = null;
    var oRepeatTypeView = null;

    var oTaskTypeView = (this.props.isImageAnnotationView && sContext === "task") ? this.getTaskTypeView() : null;

    var fCallback = this.handleTaskStatusChanged;
    var oStatusView = (this.props.statusTags) ?
                      this.getTagView(oTask.status, this.props.statusTags, "status", getTranslation().STATUS, fCallback) : null;

    fCallback = this.handleTaskPriorityChanged;
    var oPriorityView = (this.props.priorityTags) ?
                        this.getTagView(oTask.priority, this.props.priorityTags, "priority", getTranslation().PRIORITY, fCallback) : null;

    var oAttachmentView = this.props.taskViewProps.attachmentData ? this.getFileAttachmentView(bIsDisabled) : null;
    var oRolesView = (oTask.roles && oToolbarVisibility.showRoles) ? this.getRolesView() : null;
    var oSubTaskView = (oTask.subTasks && this.props.context !== "subtask") ? this.getSubtaskView(bIsDisabled) : null;
    var oCommentsView = oTask.comments ? this.getCommentsView() : null;


    var aDateView = (sTaskOrEvent === "task") ? this.getTasksDateView() : null;
    var bIsCloseVisible = (this.props.isCloseDetailViewVisible && this.props.context !== "subtask") ? (
                                                                                                        <TooltipView placement="bottom" label={getTranslation().CLOSE}>
                                                                                                          <div className="closeTaskDetailView" onClick={this.handleCloseTaskDetailClicked}/>
                                                                                                        </TooltipView>) : null;

    let oTaskFormView = this.getTaskFormView();
    let oBPNMView = this.getBPNMWrapperView();
    let oContentDetailView = this.getTaskEntityView();

    return (
        <div className="taskDetailView">
          {bIsCloseVisible}
          {this.getDescriptionView()}
          {oTaskTypeView}
          {oStatusView}
          {oPriorityView}
          {aDateView}
          {oRepeatTypeView}
          {oAttachmentView}
          {oContentDetailView}
          {oRolesView}
          {oTaskFormView}
          {oBPNMView}
          {oSubTaskView}
          {oCommentsView}
          {oRepeatView}
        </div>
    )
  };

  getPublicModeButtonDom = () => {
    if (this.props.task.createdBy === CommonUtils.getCurrentUser().id) {
      var sButtonClassName = "publicModeButton";
      var bIsPublicMode = this.props.task.isPublic;
      var sLabel = "";
      if (bIsPublicMode) {
        sLabel = getTranslation().COLLECTION_PUBLIC_MODE;
        sButtonClassName = sButtonClassName + " isPublicButton";
      }
      else {
        sLabel = getTranslation().COLLECTION_PRIVATE_MODE;
      }
      return (<TooltipView placement="top" label={sLabel}>
        <div className={sButtonClassName}
  onClick={this.handleVisibilityModeChanged.bind(this, this.props.task.id, bIsPublicMode)}/>
      </TooltipView>);
    }
    else {
      return null;
    }
  };

  render() {
    var oTaskDetailView = (this.props.isTaskDetailViewOpen) ? this.getTaskDetailView() : null;

    return (<div className="taskDetailViewWrapper" key={"taskDetailViewWrapper_" + this.props.context}>
      {oTaskDetailView}
    </div>);
  }
}

export const view = ContentTasksDetailView;
export const events = oEvents;
