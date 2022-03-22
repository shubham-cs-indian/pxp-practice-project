import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../viewlibraries/tooltipview/tooltip-view';
import TaskViewData from './../tack/tasks-grouping-constants';
import { view as ContextMenuViewNew } from './../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from './../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import { view as SimpleSearchBarView } from './../../../viewlibraries/simplesearchbarview/simple-search-bar-view';
import { view as NothingFoundView } from './../../../viewlibraries/nothingfoundview/nothing-found-view';
import { view as ContentTaskSubTaskDetailView } from './task-detail-view-wrapper';
import { view as ImageFitToContainerView } from './../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
import { view as EditModeToolbarView } from '../../../viewlibraries/editmodetoolbarview/edit-mode-toolbar-view';
import ViewUtils from '../../../viewlibraries/utils/view-library-utils';
import TaskViewContextConstants from './../tack/task-view-context-constants';

var oEvents = {
  CONTENT_TASK_LIST_TASK_CHECKBOX_CLICKED: "handle_task_list_checkbox_clicked",
  CONTENT_TASK_LIST_NODE_CLICKED: "handle_task_list_clicked",
  CONTENT_TASK_LIST_ADD_NEW_TASK_CLICKED: "handle_add_new_task_clicked",
  CONTENT_TASK_LABEL_VALUE_CHANGED: "handle_task_label_changed",
  CONTENT_TASK_LIST_TASK_NAME_ON_BLUR: "handle_task_name_on_blur",
  CONTENT_TASK_LIST_GROUP_BY_DROP_DOWN_CLICKED: "handle_group_by_drop_down_clicked",
  CONTENT_TASK_LIST_DELETE_TASK_CLICKED: "handle_delete_task_clicked",
  CONTENT_TASK_LIST_TASK_SAVE_EVENT: "content_task_save_clicked",
  CONTENT_TASK_LIST_SELECT_ALL_CHECKBOX_CLICKED: "handle_task_detail_all_task_list_checkbox_clicked",
  CONTENT_TASK_LIST_LOAD_MORE_BTN_CLICKED: "content_task_list_load_more_btn_clicked",
  CONTENT_TASK_LIST_SEARCH_TEXT_CHANGED: "handle search text changed",
  CONTENT_TASK_LIST_TOOLBAR_CLICKED: "content_task_list_toolbar_clicked",
  CONTENT_TASK_LIST_COLLAPSED_STATE_CHANGED: "content_task_list_collapsed_state_changed"
};

const oPropTypes = {
  taskListGroupByType: ReactPropTypes.object,
  activeTask: ReactPropTypes.object,
  activeTaskFormViewModels: ReactPropTypes.array,
  activeSubTask: ReactPropTypes.object,
  isTaskDetailViewOpen: ReactPropTypes.bool,
  taskViewProps: ReactPropTypes.object,
  usersList: ReactPropTypes.array,
  currentUser: ReactPropTypes.object,
  activeContent: ReactPropTypes.object,
  toolBarVisibility: ReactPropTypes.object,
  priorityTags: ReactPropTypes.array,
  statusTags: ReactPropTypes.array,
  selectedGroupByType: ReactPropTypes.string,
  isDisabled: ReactPropTypes.bool,
  isDialogOpen: ReactPropTypes.bool,
  taskOrEvent: ReactPropTypes.string,
  hideSaveIcon: ReactPropTypes.bool,
  referencedRoles: ReactPropTypes.object,
  context: ReactPropTypes.string,
  viewContext: ReactPropTypes.string,
  taskRolesData: ReactPropTypes.array,
  referencedData: ReactPropTypes.object
};
/**
 * @class ContentTaskListView
 * @description Used to show task list.
 * @memberOf Views
 * @property {object} [taskListGroupByType] - List of task grouped by type.
 * @property {object} [activeTask] - Active task details.
 * @property {array} [activeTaskFormViewModels]
 * @property {object} [activeSubTask] - Active sub task details.
 * @property {bool} [isTaskDetailViewOpen] - To show Task Detail View.
 * @property {object} [taskViewProps] - Props related to tasks i.e isSelected.
 * @property {array} [usersList] - User list.
 * @property {object} [currentUser] - Current user details.
 * @property {object} [activeContent] - Active content details.
 * @property {object} [toolBarVisibility] - Contains toolBar buttons visibility data.
 * @property {array} [priorityTags]
 * @property {array} [statusTags]
 * @property {string} [selectedGroupByType]
 * @property {bool} [isDisabled] - To disable ContentTaskSubTaskDetailView.
 * @property {bool} [isDialogOpen] - Deprecated
 * @property {string} [taskOrEvent] - To determined task or event is will be render.
 * @property {bool} [hideSaveIcon] - Deprecated
 * @property {object} [referencedRoles] - Referenced roles.
 * @property {string} [context] -
 * @property {string} [viewContext]
 */

// @CS.SafeComponent
class ContentTaskListView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);
    this.state = {
      searchString: "",
      showGroupByTask: false,
    };
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    if (CS.has(oNextProps, 'searchString')) {
      return {
        searchString: oNextProps.searchString,
      }
    } else {
      return {
        searchString: oState.searchString,
      }
    }
  }

  handleListCollapsedStateChanged = (sTypeId, sContext) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_COLLAPSED_STATE_CHANGED, sTypeId, sContext);
  };

  handleTaskListToolBarClicked = (sEvent) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_TOOLBAR_CLICKED, sEvent, this.props.taskOrEvent);
  };

  handleTaskListNameOnBlur = (sTaskId) => {
    var sTaskOrEven = this.props.taskOrEvent;
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_TASK_NAME_ON_BLUR, sTaskOrEven, sTaskId);
  };

  handleSearchChanged = (sNewValue) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_SEARCH_TEXT_CHANGED, sNewValue, this.props.taskOrEvent);
    this.setState({searchString: sNewValue});
  };

  handleTaskLabelValueChanged = (sKey, sId, oEvent) => {
    var sTaskOrEvent = this.props.taskOrEvent;
    var sValue = oEvent.target.value;
    EventBus.dispatch(oEvents.CONTENT_TASK_LABEL_VALUE_CHANGED, sTaskOrEvent, sKey, sId, sValue);
  };

  handleKeyDownClicked = (sId, oEvent) => {
    var sTaskOrEvent = this.props.taskOrEvent;
    if(oEvent.which == 13 || oEvent.keyCode == 13){
      this.handleTaskListNameOnBlur(sId);
    }
  };

  handleTaskListCheckBoxClicked = (bIsChecked, sId, oEvent) => {
    var sTaskOrEvent = this.props.taskOrEvent;
    oEvent.nativeEvent.dontRaise = true;
    bIsChecked = !bIsChecked;
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_TASK_CHECKBOX_CLICKED, sTaskOrEvent, bIsChecked, sId);
  };

  handleAllTaskListCheckBoxClicked = () => {
    var sTaskOrEvent = this.props.taskOrEvent;
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_SELECT_ALL_CHECKBOX_CLICKED, sTaskOrEvent, this.state.searchString);
  };

  handleTaskListClicked = (sId, oEvent) => {
    var sTaskOrEvent = this.props.taskOrEvent;
    if(!oEvent.nativeEvent.dontRaise) {
      EventBus.dispatch(oEvents.CONTENT_TASK_LIST_NODE_CLICKED, sTaskOrEvent, sId);
    }
  };

  handleAddNewTaskClicked = (sTypeId) => {
    let oProps = this.props;
    let oActiveContent = oProps.activeContent;
    var sTaskOrEvent = oProps.taskOrEvent;
    if(this.state.searchString) {
      this.setState({searchString: ""});
    }
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_ADD_NEW_TASK_CLICKED, sTaskOrEvent, sTypeId, oActiveContent);
  };

  handleLoadMoreClicked = (sTypeId) => {
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_LOAD_MORE_BTN_CLICKED, sTypeId);
  };

  handleGroupByNodeClicked = (oModel) => {
    var sTaskOrEvent = this.props.taskOrEvent;
    var sId = oModel.id;
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_GROUP_BY_DROP_DOWN_CLICKED, sTaskOrEvent, sId);
    this.setState({
      showGroupByTask: false
    });
  };

  handlePopoverVisibility = (sContext) => {
    if(sContext == "groupBy") {
      this.setState({
        showGroupByTask: true
      });
    }
  };

  handleDeleteTaskClicked = (sId, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let oProps = this.props;
    let oActiveContent = oProps.activeContent;
    var sTaskOrEvent = oProps.taskOrEvent;
    EventBus.dispatch(oEvents.CONTENT_TASK_LIST_DELETE_TASK_CLICKED, sTaskOrEvent, sId, oActiveContent);
  };

  getContextMenuModelList = (aList, sContext, sSelectedItem) => {

    var aModels = [];
    CS.forEach(aList, function (oListItem) {
      if(sSelectedItem != oListItem.id) {
        aModels.push(new ContextMenuViewModel(
            oListItem.id,
            CS.getLabelOrCode(oListItem),
            false,
            "",
            {context: sContext}
        ));
      }
    });
    return aModels;
  };

  getGroupByView = () => {
    var aList;
    var sTaskOrEvent = this.props.taskOrEvent;

    let oTaskViewData = new TaskViewData();
    if(sTaskOrEvent == 'task') {
      aList = oTaskViewData.taskGroupByOptions;
    }
    var sSelectedGroupByType = this.props.selectedGroupByType;
    var oType = CS.find(aList, {id: sSelectedGroupByType}) || {};

    var oGroupByDom = (
        <div className="groupBy" onClick={this.handlePopoverVisibility.bind(this, "groupBy")}>
          <span className="groupByLabel">{getTranslation().GROUP_BY} : {CS.getLabelOrCode(oType)}</span>
          <div className="dropDownIcon"></div>
        </div>
    );
    var aGroupByContextMenuModelList = this.getContextMenuModelList(aList, "groupBy", sSelectedGroupByType);

    return (
        <div className="groupByContainer">
          <ContextMenuViewNew
              contextMenuViewModel={aGroupByContextMenuModelList}
              onClickHandler={this.handleGroupByNodeClicked}>
            {oGroupByDom}
          </ContextMenuViewNew>
        </div>);
  };

  getUpperToolBarView = () => {
    var oToolBarVisibility = this.props.toolBarVisibility;
    var oGroupByView = this.getGroupByView();

    var bIsAllTaskUnChecked = this.props.taskViewProps.bIsAllTaskChecked;
    var sCheckBoxClassName = "allTaskCheckbox";
    sCheckBoxClassName += bIsAllTaskUnChecked ? " checked" : "";
    var sCheckboxTooltip = this.props.taskViewProps.bIsAllTaskChecked ? getTranslation().DESELECT_ALL : getTranslation().SELECT_ALL;
    var oDeleteButtonView = (oToolBarVisibility.bIsDeleteVisible) ? (<TooltipView label={getTranslation().DELETE}>
                                                                    <div className="deleteTasks" onClick={this.handleDeleteTaskClicked.bind(this, "")}></div>
                                                                  </TooltipView>) : null;
    let oSelectAllCheckBoxView = (oToolBarVisibility.bIsSelectAllVisible) ? (
        <TooltipView placement="bottom" label={sCheckboxTooltip}>
          <div className={sCheckBoxClassName} onClick={this.handleAllTaskListCheckBoxClicked}></div>
        </TooltipView>) : null;

    var oSearchViewProps = {};
    oSearchViewProps.onChange = this.handleSearchChanged;

    return(
        <div className="toolBarContainer">
          <div className="leftToolBar">
            {oSelectAllCheckBoxView}
            <div className="searchBar">
              <SimpleSearchBarView {...oSearchViewProps} searchText={this.state.searchString}/>
            </div>
            {oDeleteButtonView}
          </div>
          <div className="rightToolBar">
            {this.getTaskToolBar()}
            {oGroupByView}
          </div>
        </div>
    );
  };

  escapeRegexCharacters = (sString) => {
    return sString.replace(new RegExp('[.\\\\+*?\\[\\^\\]$(){}=!<>|:\\-]', 'g'), '\\$&');
  };

  getLoadMoreView = (sTypeId) => {
    return (<div className={"listLoadMore"}
                 onClick={this.handleLoadMoreClicked.bind(this, sTypeId)}>{CS.upperFirst(getTranslation().LOAD_MORE)}</div>)
  };

  getAddNewTaskView = (sTypeId) => {
    var sTaskOrEven = this.props.taskOrEvent;
    var sAddNewLabel;

    if(sTaskOrEven == 'task'){
      sAddNewLabel = getTranslation().ADD_NEW_TASK;
    }
    return (
        <div className="addNewTask" onClick={this.handleAddNewTaskClicked.bind(this, sTypeId)}>
          <div className="addNewTaskIcon">
          </div>
          <div className="addNewTaskLabel">{sAddNewLabel}</div>
        </div>
    );
  };

  getTaskListLabelView = (oTask) => {
    var sEditableTaskId = this.props.taskViewProps.editableTaskId;
    var sActiveTaskLabel = "";
    if (oTask.contentClone) {
      sActiveTaskLabel = oTask.contentClone.name;
    } else {
      sActiveTaskLabel = oTask.name;
    }


    if(oTask.id == sEditableTaskId){
      // sActiveTaskLabel = CommonUtils.getFilteredName(sActiveTaskLabel);
      return(<input type="text" key={"taskListLabelInput" + oTask.id}
                    className="taskListLabelInput"
                    value={sActiveTaskLabel}
                    onChange={this.handleTaskLabelValueChanged.bind(this, 'name', oTask.id)}
                    onKeyDown={this.handleKeyDownClicked.bind(this, oTask.id)}
                    onBlur={this.handleTaskListNameOnBlur.bind(this, oTask.id)} autoFocus/>);
    } else {
      return (<div className="taskListLabel" key={"taskListLabel" + oTask.id}>{sActiveTaskLabel}</div>);
    }
  };

  getTasksStatusTagView = (oTag) => {
    if (CS.isEmpty(oTag)) {
      return null;
    }

    var oIconView = null;
    if(oTag.icon){
      var sIcon = ViewUtils.getIconUrl(oTag.icon);
      oIconView = (<div className="tasksTagIcon">
        <ImageFitToContainerView imageSrc={sIcon}/>
      </div>);
    }
    var oStyle = {};
    if (oTag.color) {
      oStyle.backgroundColor = oTag.color;
      oStyle.color = ViewUtils.getTextColorBasedOnBackgroundColor(oTag.color)
    }

    var sLabelClass = (oIconView) ? "tasksTagLabel iconVisible" : "tasksTagLabel ";
    return (
        <div className="tasksTagDetails" style={oStyle} key={"tasksTagDetails" + oTag.id}>
          {oIconView}
          <TooltipView label={CS.getLabelOrCode(oTag)} placement="bottom">
            <div className={sLabelClass}>
              {CS.getLabelOrCode(oTag)}
            </div>
          </TooltipView>
        </div>
    );
  };

  getTasksPriorityTagView = (oTag) => {
    if (CS.isEmpty(oTag.color)) {
      return null;
    }

    var oStyle = {
      backgroundColor: oTag.color
    };

    return (
        <TooltipView label={CS.getLabelOrCode(oTag)} placement="bottom" key={"tasksPriorityTag" + oTag.id}>
          <div className="tasksPriorityTag" style={oStyle}>
          </div>
        </TooltipView>
    );
  };

  getCurrentTime = () => {
    var oCurrentDate = new Date();
    oCurrentDate.setHours(0);
    oCurrentDate.setMinutes(0);
    oCurrentDate.setSeconds(0);
    return oCurrentDate.getTime();
  };

  getListNodeView = (oTask) => {
    var _this = this;
    var oToolBarVisibility = _this.props.toolBarVisibility;
    var oActiveTask = _this.props.activeTask;
    var aListItemView = [];
    var sClassName = "taskListItem ";
    let oActiveContent = _this.props.activeContent;
    var sContentId = !CS.isEmpty(oActiveContent) ? oActiveContent.id : "";
    var oLinkedEntity = CS.find(oTask.linkedEntities, {contentId: sContentId});
    var bIsVariantsTask = (oLinkedEntity && !CS.isEmpty(oLinkedEntity.variantId) && !CS.isEmpty(oLinkedEntity.elementId));

    var bIsChecked = CS.includes(_this.props.taskViewProps.checkedTasks, oTask.id);
    var sCheckBoxClassName = "taskListCheck";
    sCheckBoxClassName += bIsChecked ? " checked" : "";
    var sCheckboxTooltip = bIsChecked ? getTranslation().DESELECT : getTranslation().SELECT;
    aListItemView.push(<TooltipView label={sCheckboxTooltip} placement="bottom" key={sCheckboxTooltip + oTask.id}>
      <div className={sCheckBoxClassName}
           onClick={_this.handleTaskListCheckBoxClicked.bind(_this, bIsChecked, oTask.id)}></div>
    </TooltipView>);

    var oTaskLabelView = _this.getTaskListLabelView(oTask);
    aListItemView.push(oTaskLabelView);
    var oExpiredView = null;
    var iCurrentTime = this.getCurrentTime();
    if (oTask.dueDate && (oTask.dueDate < iCurrentTime)) {
      oExpiredView = (<TooltipView label={getTranslation().OVER_DUE} placement="top" key={"expiredView"}>
        <div className="expiredView"></div>
      </TooltipView>);
    }
    aListItemView.push(oExpiredView);


    aListItemView.push(<TooltipView label={getTranslation().DELETE} key={"deleteTask"}>
      <div className="deleteTask"
           onClick={_this.handleDeleteTaskClicked.bind(_this, oTask.id)}></div>
    </TooltipView>);


    if (bIsVariantsTask) {
      aListItemView.push(<TooltipView label={getTranslation().VARIANT} key={"variantIndicator"}>
        <div className="variantIndicator"></div>
      </TooltipView>);
    }

    if (oTask.priorityTagForList) {
      aListItemView.push(_this.getTasksPriorityTagView(oTask.priorityTagForList));
    }

    if (oTask.tagForList) {
      aListItemView.push(_this.getTasksStatusTagView(oTask.tagForList));
    }

    sClassName += (oActiveTask.id == oTask.id) ? "selected " : "";

    return (<div className={sClassName} key={"taskListItem_" + oTask.id}
                 onClick={_this.handleTaskListClicked.bind(_this, oTask.id)}>{aListItemView}</div>);
  };

  getTaskListView = () => {
    var _this = this;
    var sContext = this.props.taskOrEvent;
    var oActiveTask = _this.props.activeTask;
    var sSearchString = this.state.searchString;
    sSearchString = this.escapeRegexCharacters(sSearchString);
    var rPattern = sSearchString ? new RegExp(sSearchString, "i") : null;
    var oGroupByTypeTaskListMap = this.props.taskListGroupByType;

    var aGroupOfList = [];

    if (oGroupByTypeTaskListMap) {
      var iIndex = 0;
      CS.forEach(oGroupByTypeTaskListMap, function (oValue, sGroupId) {
        var aTaskListView = [];
        if (!CS.isEmpty(oValue)) {
          var aTasks = oValue.tasks;
          var iLocalTaskCount = oValue.localCount ? oValue.localCount : aTasks.length;
          var activeElementIndicator = !!(CS.find(aTasks, {id: oActiveTask.id}));
          var sGroupHeaderTooltip = (oValue.isCollapsed) ? getTranslation().EXPAND : getTranslation().COLLAPSE;

          var sAddNewElementTooltipLabel = getTranslation().ADD_NEW_TASK;
          var addNewElementView = oValue.bShowAddNewTask ?
                                  <TooltipView label={sAddNewElementTooltipLabel} placement="bottom">
                                    <div className="addNewElement"
                                         onClick={_this.handleAddNewTaskClicked.bind(_this, oValue.type)}></div>
                                  </TooltipView> : null;

          var activeElementIndicatorView = activeElementIndicator ? <div className="activeElementIndicator"></div> : null;

          aTaskListView.push(<div className="taskListHeader" key={"taskListHeader_" + iIndex++}>
            <div className="taskListHeaderText" onClick={_this.handleListCollapsedStateChanged.bind(_this, sGroupId, sContext)}>
              <span className="listHeader">{CS.getLabelOrCode(oValue)}</span>
              <span className="itemCount">{"(" + iLocalTaskCount + ")"}</span>
              {activeElementIndicatorView}
            </div>
            <TooltipView label={sGroupHeaderTooltip} placement="bottom">
              <div className="collapsedIcon"
                   onClick={_this.handleListCollapsedStateChanged.bind(_this, sGroupId, sContext)}></div>
            </TooltipView>
            {addNewElementView}
          </div>);

          var aTaskListNode = [];
          CS.forEach(aTasks, function (oTask) {
            if (rPattern) {
              if (!rPattern.test(oTask.name)) {
                return;
              }
            }
            aTaskListNode.push(_this.getListNodeView(oTask));
          });

          if (CS.isEmpty(aTaskListNode) && rPattern) {
            var sMessage = getTranslation().NO_TASK_FOUND;
            aTaskListView.push(<div className="nothingFound">{sMessage}</div>);
          } else {
            aTaskListView.push(aTaskListNode);
          }

          if (oValue.bShowLoadMore) {
            aTaskListView.push(_this.getLoadMoreView(oValue.type));
          } else if (oValue.bShowAddNewTask) {
            aTaskListView.push(_this.getAddNewTaskView(oValue.type));
          }
          var sGroupHeaderClassName = (oValue.isCollapsed) ? "groupSection collapsed " : "groupSection ";
          if(oValue.status === "taskplanned"){
            sGroupHeaderClassName = sGroupHeaderClassName + " plannedTaskHighlight";
          }

          aGroupOfList.push(<div className={sGroupHeaderClassName}
                                 key={"groupSection_" + sGroupId}>{aTaskListView}</div>);
        }
      });
    }

    var sClassName = !CS.isEmpty(oActiveTask) ? "taskListWrapper detailViewVisible" : "taskListWrapper ";
    CS.isEmpty(aGroupOfList) && aGroupOfList.push(<NothingFoundView key="nothingFound"/>);

    return (
        <div className={sClassName}>
          {aGroupOfList}
        </div>
    );
  };

  getTaskListForDashboardView = () => {
    var _this = this;
    var sContext = this.props.taskOrEvent;
    var oActiveTask = _this.props.activeTask;
    var sSearchString = this.state.searchString;
    sSearchString = this.escapeRegexCharacters(sSearchString);
    var rPattern = sSearchString ? new RegExp(sSearchString, "i") : null;
    var oGroupByTypeTaskListMap = this.props.taskListGroupByType;

    var aGroupOfList = [];

    if (oGroupByTypeTaskListMap) {
      var iIndex = 0;
      CS.forEach(oGroupByTypeTaskListMap, function (oValue, sGroupId) {
        var aTaskListView = [];
        if (!CS.isEmpty(oValue)) {
          var aTasks = oValue.tasks;
          var activeElementIndicator = !!(CS.find(aTasks, {id: oActiveTask.id}));
          var sGroupHeaderTooltip = (oValue.isCollapsed) ? getTranslation().EXPAND : getTranslation().COLLAPSE;
          var activeElementIndicatorView = activeElementIndicator ? <div className="activeElementIndicator"></div> : null;

          var aTaskListNode = [];
          CS.forEach(aTasks, function (oTask) {
            if (rPattern) {
              let sLabel = CS.getLabelOrCode(oTask);
              if (!rPattern.test(sLabel)) {
                return;
              }
            }
            aTaskListNode.push(_this.getListNodeView(oTask));
          });

          if (!CS.isEmpty(aTaskListNode)) {
            aTaskListView.push(<div className="taskListHeader" key={"taskListHeader_" + iIndex++}>
              <div className="taskListHeaderText"
                   onClick={_this.handleListCollapsedStateChanged.bind(_this, sGroupId, sContext)}>
                <span className="listHeader">{CS.getLabelOrCode(oValue)}</span>
                <span className="itemCount">{"(" + aTaskListNode.length + ")"}</span>
                {activeElementIndicatorView}
              </div>
              <TooltipView label={sGroupHeaderTooltip} placement="bottom">
                <div className="collapsedIcon"
                     onClick={_this.handleListCollapsedStateChanged.bind(_this, sGroupId, sContext)}></div>
              </TooltipView>
            </div>);
            aTaskListView.push(aTaskListNode);
          }
          if (!CS.isEmpty(aTaskListView)) {
            var sGroupHeaderClassName = (oValue.isCollapsed) ? "groupSection collapsed " : "groupSection ";
            aGroupOfList.push(<div className={sGroupHeaderClassName}
                                   key={"groupSection_" + sGroupId}>{aTaskListView}</div>);
          }
        }
      });
    }
    if(CS.isEmpty(aGroupOfList)){
      var sMessage = getTranslation().NO_TASK_FOUND;
      aGroupOfList.push(<div className="nothingFound" key={"groupSection_nothingFound"}>{sMessage}</div>);
    }

    var sClassName = !CS.isEmpty(oActiveTask) ? "taskListWrapper detailViewVisible" : "taskListWrapper ";

    return (
        <div className={sClassName}>
          {aGroupOfList}
        </div>
    );
  };

  getTaskToolBar = () => {
    let oView = null;
    let __props = this.props;
    let aCheckedTaskList = __props.taskViewProps.checkedTasks;
    let aTaskList = __props.taskList;

    let aListOfDoneTasks = [];
    CS.forEach(aTaskList, (oTask) => {
      if (oTask.isCamundaCreated && (oTask.tagForList && oTask.tagForList.id != "taskdone" && aCheckedTaskList.includes(oTask.id))) {
        aListOfDoneTasks.push(oTask.id);
      }
    });

    if (this.props.context == "taskDashboard") {
      let oActiveTask = __props.activeTask;
      let aMoreButtons = [];
      let aTextButtons = [];

      if (__props.activeTask.isDirty) {
        aMoreButtons.push(new ContextMenuViewModel("discard", getTranslation().DISCARD_CHANGES, false, "", {}));
        aTextButtons.push({id: 'save', label: getTranslation().SAVE})
      }

      // if((!CS.isEmpty(aListOfDoneTasks) && !CS.isEmpty(aCheckedTaskList) && !(aCheckedTaskList.length == aListOfDoneTasks.length)) ||
      if((!CS.isEmpty(aListOfDoneTasks) && !CS.isEmpty(aCheckedTaskList)) ||
          (
              oActiveTask && oActiveTask.isCamundaCreated &&
              (oActiveTask.status && oActiveTask.status.tagValues[0] && oActiveTask.status.tagValues[0].tagId != "taskdone")
          )
      ){
        aTextButtons.push(new ContextMenuViewModel("complete", getTranslation().COMPLETE, false, "", {}));
      }

      // if (oActiveTask.isCamundaCreated && (oActiveTask.status && oActiveTask.status.tagValues[0] && oActiveTask.status.tagValues[0].tagId != "taskdone")) {
      //   aTextButtons.push(new ContextMenuViewModel("complete", getTranslation().COMPLETE, false, "", {}));
      // }
      if (!CS.isEmpty(aTextButtons)) {
        let oToolBarData = {
          moreButtons: aMoreButtons,
          textButtons: aTextButtons,
        };
        oView = (
            <EditModeToolbarView
                onClickHandler={this.handleTaskListToolBarClicked}
                toolbarItems={oToolBarData}
                isSaveCommentDisabled={true}
            />);
      }
    }

    return oView;
  };

  render() {
    var sTaskOrEvent = this.props.taskOrEvent;
    var bIsCloseVisible = sTaskOrEvent == 'task';
    var sTaskDashboardConstant = TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK;
    var sDashboardClass = this.props.context === sTaskDashboardConstant ? "dashboard" : "";
    let oTaskLinkedInstanceData = {};
    let oActiveTask = this.props.activeTask;
    if(this.props.context === "taskDashboard"){
      oTaskLinkedInstanceData = oActiveTask.linkedInstanceData;
    }
    return (
        <div className="taskListViewContainer">
          {this.getUpperToolBarView()}
          <div className={"taskListAndDetailWrapper " + sDashboardClass}>
            {this.props.context === sTaskDashboardConstant ? this.getTaskListForDashboardView() : this.getTaskListView()}
            <ContentTaskSubTaskDetailView activeTask={this.props.activeTask}
                                          activeContent={this.props.activeContent}
                                          activeTaskFormViewModels={this.props.activeTaskFormViewModels}
                                          activeSubTask={this.props.activeSubTask}
                                          isTaskDetailViewOpen={this.props.isTaskDetailViewOpen}
                                          toolBarVisibility={this.props.toolBarVisibility}
                                          taskViewProps={this.props.taskViewProps}
                                          usersList={this.props.usersList}
                                          currentUser={this.props.currentUser}
                                          priorityTags={this.props.priorityTags}
                                          statusTags={this.props.statusTags}
                                          isDisabled={this.props.isDisabled}
                                          isCloseDetailViewVisible={bIsCloseVisible}
                                          isImageAnnotationView={false}
                                          editableRoles={this.props.editableRoles}
                                          taskOrEvent={sTaskOrEvent}
                                          referencedRoles={this.props.referencedRoles}
                                          taskLinkedInstanceData={oTaskLinkedInstanceData}
                                          taskRolesData={this.props.taskRolesData}
                                          referencedData={this.props.referencedData}/>
          </div>
        </div>
    );
  }
}

export const view = ContentTaskListView;
export const events = oEvents;
