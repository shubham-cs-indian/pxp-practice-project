import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../viewlibraries/tooltipview/tooltip-view';
import { view as TasksListView } from './task-list-view';
import { view as CustomTextFieldView } from './../../../viewlibraries/customtextfieldview/custom-text-field-view';
import { view as NotificationsView } from './notifications-view';
import ContentTaskViewListNodeContexts from './../tack/task-view-context-constants';
import { view as CustomMaterialButtonView } from '../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import {view as CustomDialogView} from "../../../viewlibraries/customdialogview/custom-dialog-view";
import {view as CommonConfigSectionView} from "../../../viewlibraries/commonconfigsectionview/common-config-section-view";
import SectionLayoutForWorkflows from "../../../screens/homescreen/screens/settingscreen/tack/process-config-layout-data";
import {view as LazyContextMenuView} from '../../../viewlibraries/lazycontextmenuview/lazy-context-menu-view';
import MockDataForFrequencyTypesDictionary
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-frequency-dictionary";
import MockDataForProcessFrequencyDayList
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-process-frequency-day-list";
import MockDataForfrequencyMonthListDictionary
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-process-frequency-month-list";
import TabHeaderDataForProcess
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-frequency-dictionary";
import ContentUtils from "../../../screens/homescreen/screens/contentscreen/store/helper/content-utils";
import { view as WorkflowView } from './workflows-view';

var oEvents = {
  HANDLE_TASK_LIST_BY_LEVEL_CLICKED: "handle_task_list_by_level_clicked",
  HANDLE_TASK_LIST_BY_HEADER_CLICKED: "handle_task_list_by_header_clicked",
  HANDLE_TASK_LIST_LOAD_MORE_CLICKED: "handle_task_list_load_more_clicked",
  HANDLE_TASK_LIST_SEARCH: "handle_task_list_search",
  HANDLE_TASK_LIST_REFRESH_NOTIFICATION: "handle_task_list_refresh_notification",
  HANDLE_WORKFLOW_TEMPLATE_DIALOG_BUTTON_CLICKED : "handle_workflow_template_dialog_button_clicked",
  HANDLE_WORKFLOW_TEMPLATE_APPLY_BUTTON_CLICKED : "handle_workdlow_template_apply_button_clicked"
};

const oPropTypes = {
  taskListGroupByType: ReactPropTypes.object,
  taskLevelsList: ReactPropTypes.array,
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
  referencedRoles: ReactPropTypes.object,
  context: ReactPropTypes.string,
  notificationsData: ReactPropTypes.object,
  notificationsCount: ReactPropTypes.number,
  productListData: ReactPropTypes.object,
  activeProductListId: ReactPropTypes.string,
  selectedLevelListIds: ReactPropTypes.array,
  paginationDataByListGroups: ReactPropTypes.object,
  viewContext: ReactPropTypes.string,
  taskList:ReactPropTypes.array,
  workflowTemplateData:ReactPropTypes.object,
};
/**
 * @class TasksView
 * @description - Used to show task on contents.
 * @memberOf Views
 * @property {object} [taskListGroupByType] - List of tasks grouped by type, due date, priority & status.
 * @property {array} [taskLevelsList] - Grouping of tasks data e.g. content level task, property level task & all tasks
 * @property {object} [activeTask] -  Task which is active
 * @property {array} [activeTaskFormViewModels]
 * @property {object} [activeSubTask] - Active sub task
 * @property {bool} [isTaskDetailViewOpen] - To show Task Detail View.
 * @property {object} [taskViewProps] - Props related to tasks i.e isSelected.
 * @property {array} [usersList] - User List.
 * @property {object} [currentUser]- Current User.
 * @property {object} [activeContent]
 * @property {object} [toolBarVisibility] - Contains toolBar buttons visibility data.
 * @property {array} [priorityTags] - Priority tags data associated with tasks in config.
 * @property {array} [statusTags] - Status tags data associated with tasks in config.
 * @property {string} [selectedGroupByType] - Selected grouping type for filtering (i.e.  type, due date, priority & status).
 * @property {bool} [isDisabled] -
 * @property {object} [referencedRoles]
 * @property {string} [context] -
 * @property {object} [notificationsData]
 * @property {number} [notificationsCount]
 * @property {object} [productListData]
 * @property {string} [activeProductListId]
 * @property {array} [selectedLevelListIds]
 * @property {object} [paginationDataByListGroups]
 * @property {string} [viewContext]
 * @property {object} [taskList] All task list.
 */

// @CS.SafeComponent
class TasksView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);
  }

  componentWillUnmount () {
  }

  handleTaskListByLevelClicked = (oTaskListItem, iListLevelIndex) => {
    EventBus.dispatch(oEvents.HANDLE_TASK_LIST_BY_LEVEL_CLICKED, oTaskListItem, iListLevelIndex);
  };
  handleTaskListByLevelHeaderClicked = (oTaskListItem, iListLevelIndex) => {
    EventBus.dispatch(oEvents.HANDLE_TASK_LIST_BY_HEADER_CLICKED, oTaskListItem, iListLevelIndex);
  };

  handleTaskListLoadMoreClicked = (oTaskListItem, iListLevelIndex) => {
    EventBus.dispatch(oEvents.HANDLE_TASK_LIST_LOAD_MORE_CLICKED, oTaskListItem, iListLevelIndex);
  };

  handleTaskListSearch = (oTaskListItem, iListLevelIndex, sSearchText) => {
    EventBus.dispatch(oEvents.HANDLE_TASK_LIST_SEARCH, oTaskListItem, iListLevelIndex, sSearchText);
  };

  handleWorkflowTemplateDialogButtonClicked = (sButton) => {
    EventBus.dispatch(oEvents.HANDLE_WORKFLOW_TEMPLATE_DIALOG_BUTTON_CLICKED,sButton);
  };

  handleTemplateApplyButtonClicked = (sContext, sSelectedTemplateWorkflow) => {
    EventBus.dispatch(oEvents.HANDLE_WORKFLOW_TEMPLATE_APPLY_BUTTON_CLICKED, sContext, sSelectedTemplateWorkflow);
  };

  getTaskCountView = (oListItem) => {
    var iCount = (oListItem.id === "notifications") ? this.props.notificationsCount : oListItem.tasksCount;
    return (iCount > 0) ? <div className="taskCount">{iCount}</div> : null;
  };

  getExpansionDOM = (oListItem) => {
    let oExpansionStatusDOM = null;
    if (!CS.isEmpty(oListItem.children)) {
      let sExpansionStatusDOMClassName = oListItem.isExpanded ? "expansionStatus expanded" : "expansionStatus";
      oExpansionStatusDOM = (<div className={sExpansionStatusDOMClassName}></div>)
    }
    return oExpansionStatusDOM;
  };

  getTaskNode = (oGroupItem, bIsChild, iListLevelIndex) => {
    let _this = this;
    let _props = _this.props;
    let oExpansionStatusDOM = null;
    let oCountView = null;
    let sClassName = "taskByLevelList";
    let fOnClickHandler;

    if (bIsChild) {
      fOnClickHandler = _this.handleTaskListByLevelClicked.bind(_this, oGroupItem, iListLevelIndex);
    } else {
      sClassName += ` header ${oGroupItem.context}`;
      oGroupItem.isExpandable && (oExpansionStatusDOM = this.getExpansionDOM(oGroupItem));
      oCountView = this.getTaskCountView(oGroupItem);
      fOnClickHandler = _this.handleTaskListByLevelHeaderClicked.bind(_this, oGroupItem, iListLevelIndex);
    }

    (CS.includes(_props.selectedLevelListIds, oGroupItem.id)) && (sClassName += " selected");

    let sTaskLabel = oGroupItem.label || getTranslation()[oGroupItem.labelKey] || "";

    return (
        <div className={sClassName} onClick={fOnClickHandler} key={sClassName + oGroupItem.id + iListLevelIndex}>
          <TooltipView label={sTaskLabel} placement="bottom">
            <div className="taskByLevelListItem">{sTaskLabel}</div>
          </TooltipView>
          {oExpansionStatusDOM}
          {oCountView}
        </div>
    );
  };


  getTaskGroupView = (oGroupItem, iListLevelIndex) => {
    var _this = this;
    var aTaskListItemsView = [];
    let bIsChildrenEmpty = CS.isEmpty(oGroupItem.children);
    let oPaginationGroups = this.props.paginationDataByListGroups || {};
    let oGroupPagination = oPaginationGroups[oGroupItem.id];
    let sSearchText = CS.isEmpty(oGroupPagination) ? "" : oGroupPagination.searchText;

    let bShowSearchLoadMore = oGroupItem.isPaginated && !bIsChildrenEmpty;
    aTaskListItemsView.push(this.getTaskNode(oGroupItem,null,iListLevelIndex));

    let bShowSearchBar = (oGroupItem.context === ContentTaskViewListNodeContexts.PRODUCT_LIST_HEADER);
    bShowSearchBar && aTaskListItemsView.push(
        <div className="searchBar" key={"searchBar" + oGroupItem.id + iListLevelIndex}>
          {/*<SimpleSearchBarView onBlur={this.handleTaskListSearch.bind(this, oGroupItem,iListLevelIndex)}*/}
          {/*searchText={sSearchText}/>*/}
          <div className="searchIcon"></div>
          <CustomTextFieldView
              hintText={getTranslation().SEARCH}
              value={sSearchText}
              onBlur={this.handleTaskListSearch.bind(this, oGroupItem,iListLevelIndex)}
          />
        </div>
    );

    oGroupItem.isExpanded && bIsChildrenEmpty && aTaskListItemsView.push(
        <div className="nothingFoundMessage" key={"nothingFoundMessage" + oGroupItem.id + iListLevelIndex}>
          {getTranslation().NOTHING_FOUND}
        </div>);

    if (oGroupItem.isExpanded) {
      CS.forEach(oGroupItem.children, function (oTask) {
        aTaskListItemsView.push(_this.getTaskNode(oTask, true, iListLevelIndex))
      });
    }

    bShowSearchLoadMore && aTaskListItemsView.push(<div className="loadMoreMessage" key={"loadMoreMessage" + oGroupItem.id + iListLevelIndex}
                                                        onClick={this.handleTaskListLoadMoreClicked.bind(this, oGroupItem)}>
      {getTranslation().LOAD_MORE}
    </div>);

    return aTaskListItemsView;
  };

  /** generate number records **/
  _generateNumberRecords = (iNumber, sType) => {
    let aItems = [];
    if (sType === "days") {
      for (let i = 1; i <= iNumber; i++) {
        let oTemp = {};
        oTemp["id"] = i;
        oTemp["label"] = i.toString();
        aItems.push(oTemp);
      }
    } else if (sType === "hours") {
      for (let i = 0; i < iNumber; i++) {
        let oTemp = {};
        oTemp["id"] = i;
        oTemp["label"] = i.toString();
        aItems.push(oTemp);
      }
    } else if (sType === "mins") {
      for (let i = 0; i < iNumber; i++) {
        let oTemp = {};
        oTemp["id"] = i;
        oTemp["label"] = i.toString();
        aItems.push(oTemp);
      }
    } else if (sType === "months") {
      for (let i = 1; i <= iNumber; i++) {
        if (i != 5) {
          let oTemp = {};
          oTemp["id"] = i;
          oTemp["label"] = i.toString();
          aItems.push(oTemp);
        }
      }
    }

    return aItems;
  };

  /** Generate Date Data**/
  _generateDateData = (sFrequencyType, sType, oWorkflowTemplateData) => {
    let oFrequency = oWorkflowTemplateData.frequencyData;
    let sDate = oFrequency[sFrequencyType][sType];
    let sNewDate = CS.isEmpty(sDate) ? new Date() : new Date(sDate);

    return sNewDate;
  };

  /** Generate Data for Frequency Tab **/
  _generateData = (sFrequencyType, sType, oWorkflowTemplateData) => {
    let sSelectedItems = "";
    let aItems = [];
    let sContext = "";
    let sContextType = "process";
    let sSubContextType = "frequency";
    let bIsMultiselect = false;
    let bIsDisabled = false;
    let bIsCannotRemove = true;
    let sSplitter = ContentUtils.getSplitter();
    let oFrequency = oWorkflowTemplateData.frequencyData;
    switch (sFrequencyType) {
      case MockDataForFrequencyTypesDictionary.DURATION :
      case MockDataForFrequencyTypesDictionary.DAILY:
      case MockDataForFrequencyTypesDictionary.HOURMIN :
        if(sType === "hours"){
          aItems = this._generateNumberRecords(24, "hours");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "hours";
        }else if(sType === "mins"){
          aItems = this._generateNumberRecords(60, "mins");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "mins"
        }
        break;
      case MockDataForFrequencyTypesDictionary.DATE:
        if(sType === "hours"){
          aItems = this._generateNumberRecords(24, "hours");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "hours";
        }else if(sType === "mins"){
          aItems = this._generateNumberRecords(60, "mins");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "mins"
        }
        break;
      case MockDataForFrequencyTypesDictionary.WEEKLY :
        if(sType === "hours"){
          aItems =this._generateNumberRecords(24, "hours");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "hours";
        }else if(sType === "mins"){
          aItems = this._generateNumberRecords(60, "mins");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "mins"
        }else if(sType === "daysOfWeeks"){
          aItems = MockDataForProcessFrequencyDayList();
          sSelectedItems = oFrequency[sFrequencyType][sType];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "daysOfWeeks";
          bIsMultiselect = true;
          bIsCannotRemove = false;
        }
        break;

      case MockDataForFrequencyTypesDictionary.MONTHLY :
        if(sType === "days"){
          aItems = this._generateNumberRecords(31, "days");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "days";
          bIsMultiselect = false;
          bIsCannotRemove = true;
        }else if(sType === "months"){
          aItems = this._generateNumberRecords(6, "months");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "months";
          bIsMultiselect = false;
          bIsCannotRemove = true;
        }
        break;

      case MockDataForFrequencyTypesDictionary.YEARLY :
        if(sType === "days"){
          aItems = this._generateNumberRecords(31, "days");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "days";
          bIsMultiselect = false;
          bIsCannotRemove = true;
        }else if(sType === "monthsOfYear"){
          aItems = MockDataForfrequencyMonthListDictionary();
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "monthsOfYear";
          bIsMultiselect = false;
          bIsCannotRemove = true;
        }
        break;
    }

    let oData = {
      items: aItems,
      selectedItems: sSelectedItems,
      cannotRemove: bIsCannotRemove,
      context: sContext,
      disabled: bIsDisabled,
      label: "",
      isMultiSelect: bIsMultiselect,
    };

    return oData;
  };

  /** Tab Data for Cron Expression Generator**/
  _generateTabSummaryData = (oModel,oWorkflowTemplateData) => {
    let oTabSummaryData = {};
    let sSelectedTabId = oWorkflowTemplateData.selectedTabId;
    let oTabHeaderData = oWorkflowTemplateData.tabHeaderData;
    if (sSelectedTabId == TabHeaderDataForProcess.DURATION) {
      let oData = {};
      oData.hours = this._generateData( TabHeaderDataForProcess.DURATION, "hours",oWorkflowTemplateData);
      oData.mins = this._generateData(TabHeaderDataForProcess.DURATION, "mins",oWorkflowTemplateData);
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.DURATION,
        selectedTabId: sSelectedTabId,
        tabHeaderData: oTabHeaderData,
        data: oData
      }
    }else if (sSelectedTabId == TabHeaderDataForProcess.DATE) {
      let oData = {};
      oData.date = this._generateDateData(TabHeaderDataForProcess.DATE, "date",oWorkflowTemplateData);
      oData.hours = this._generateData(TabHeaderDataForProcess.DATE, "hours",oWorkflowTemplateData);
      oData.mins = this._generateData(TabHeaderDataForProcess.DATE, "mins",oWorkflowTemplateData);
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.DATE,
        selectedTabId: sSelectedTabId,
        tabHeaderData: oTabHeaderData,
        data: oData
      }
    }else if (sSelectedTabId == TabHeaderDataForProcess.DAILY) {
      let oData = {};
      oData.hours = this._generateData( TabHeaderDataForProcess.DAILY, "hours",oWorkflowTemplateData);
      oData.mins = this._generateData(TabHeaderDataForProcess.DAILY, "mins",oWorkflowTemplateData);
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.DAILY,
        selectedTabId: sSelectedTabId,
        tabHeaderData: oTabHeaderData,
        data: oData
      }
    } else if (sSelectedTabId == TabHeaderDataForProcess.HOURMIN) {
      let oData = {};
      oData.hours = this._generateData( TabHeaderDataForProcess.HOURMIN, "hours",oWorkflowTemplateData);
      oData.mins = this._generateData(TabHeaderDataForProcess.HOURMIN, "mins",oWorkflowTemplateData);
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.HOURMIN,
        selectedTabId: sSelectedTabId,
        tabHeaderData: oTabHeaderData,
        data: oData
      }
    } else if (sSelectedTabId == TabHeaderDataForProcess.WEEKLY) {
      let oData = {};
      oData.daysOfWeeks = this._generateData(TabHeaderDataForProcess.WEEKLY, "daysOfWeeks",oWorkflowTemplateData);
      oData.hours = this._generateData(TabHeaderDataForProcess.WEEKLY, "hours",oWorkflowTemplateData);
      oData.mins = this._generateData(TabHeaderDataForProcess.WEEKLY, "mins",oWorkflowTemplateData);
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.WEEKLY,
        selectedTabId: sSelectedTabId,
        tabHeaderData: oTabHeaderData,
        data: oData
      }
    } else if (sSelectedTabId == TabHeaderDataForProcess.MONTHLY) {
      let oData = {};
      oData.days = this._generateData( TabHeaderDataForProcess.MONTHLY, "days",oWorkflowTemplateData);
      oData.months = this._generateData( TabHeaderDataForProcess.MONTHLY, "months",oWorkflowTemplateData);
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.MONTHLY,
        selectedTabId: sSelectedTabId,
        tabHeaderData: oTabHeaderData,
        data: oData
      }
    } else if (sSelectedTabId == TabHeaderDataForProcess.YEARLY) {
      let oData = {};
      oData.monthsOfYear = this._generateData( TabHeaderDataForProcess.YEARLY, "monthsOfYear",oWorkflowTemplateData);
      oData.days = this._generateData( TabHeaderDataForProcess.YEARLY, "days",oWorkflowTemplateData);
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.YEARLY,
        selectedTabId: sSelectedTabId,
        tabHeaderData: oTabHeaderData,
        data: oData
      }
    }

    oModel.tabSummary = oTabSummaryData;
  };

  /** Function to Create User Schedule Worflow Dialog **/
  getWorkflowDialogView = (oWorkflowTemplateData) => {

    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isDisabled: false,
        isFlat: true,
      },
      {
        id: "create",
        label: getTranslation().CREATE,
        isDisabled: false,
        isFlat: false,
      }
    ];

    let oSectionLayoutModel = {};
    let oWorkflowData = oWorkflowTemplateData.workflowData;
    oSectionLayoutModel.label = oWorkflowData.label;
    this._generateTabSummaryData(oSectionLayoutModel,oWorkflowTemplateData);

    let oSectionLayout = new SectionLayoutForWorkflows();
    let fButtonHandler = this.handleWorkflowTemplateDialogButtonClicked;
    let oWorkflowCreateDialog = (<CustomDialogView modal={true}
                                    open={oWorkflowTemplateData.bIsWorkflowDialogOpen}
                                    title={getTranslation().CREATE_USER_SCHEDULED_WORKFLOW}
                                    buttonData={aButtonData}
                                    onRequestClose={fButtonHandler.bind(this,aButtonData[0].id)}
                                    buttonClickHandler={fButtonHandler}>
      <div className="processDetailView">
        <CommonConfigSectionView context="process"
                                 sectionLayout={oSectionLayout.processBasicInformation}
                                 data={oSectionLayoutModel}/>

      </div>
    </CustomDialogView>);

    return oWorkflowCreateDialog;
  };

  getTaskLevelsListView = () => {
    let aTaskLevelsList = this.props.taskLevelsList;
    let aLevels = [];
    CS.forEach(aTaskLevelsList, (aTaskLevelListGroup, iIndex) => {
      var _this = this;
      var aListGroups = [];
      CS.forEach(aTaskLevelListGroup, (oListGroup) => {
        aListGroups.push(_this.getTaskGroupView(oListGroup, iIndex));
      });
      aLevels.push((
          <div className="taskListByLevelContainer" key={iIndex}>
            {aTaskLevelListGroup.showSelectTempleteButton && this.getSelectTempleteButtonView()}
            {aListGroups}
          </div>));
    });
    return aLevels;
  };

  /** Function to add Select Templete Button View in Workflow Workbench Tab **/
  getSelectTempleteButtonView = () => {
    let oCurrentUser =  ContentUtils.getCurrentUser();
    let oReqResInfo = {
      requestType: "configData",
      entityName: "processes",
      types: [],
      typesToExclude: [],
      customRequestModel: {
        eventType: [],
        physicalCatalogId : ContentUtils.getSelectedPhysicalCatalogId(),
        isTemplate:true,
        organizationId: oCurrentUser.organizationId
      }
    };

    let oStyle = {
      "box-shadow": "none",
      "color": "rgb(255, 255, 255)",
      "height": "auto"
    };
    return (
        <LazyContextMenuView
            isMultiselect={false}
            onClickHandler={this.handleTemplateApplyButtonClicked.bind(this, "template")}
            requestResponseInfo={oReqResInfo}
            className={""}
        >
          <div className="templateButton">
            <CustomMaterialButtonView
                style={oStyle}
                label={getTranslation().SELECT_TEMPLATE}
                isRaisedButton={true}
                isDisabled={false}
            />
          </div>
        </LazyContextMenuView>
    )
  };

  getListView = () => {
    return (<TasksListView taskListGroupByType={this.props.taskListGroupByType}
                                  activeTask={this.props.activeTask}
                                  activeTaskFormViewModels={this.props.activeTaskFormViewModels}
                                  activeSubTask={this.props.activeSubTask}
                                  isTaskDetailViewOpen={this.props.isTaskDetailViewOpen}
                                  taskViewProps={this.props.taskViewProps}
                                  usersList={this.props.usersList}
                                  currentUser={this.props.currentUser}
                                  activeContent={this.props.activeContent}
                                  toolBarVisibility={this.props.toolBarVisibility}
                                  priorityTags={this.props.priorityTags}
                                  statusTags={this.props.statusTags}
                                  selectedGroupByType={this.props.selectedGroupByType}
                                  isDisabled={this.props.isDisabled}
                                  editableRoles={this.props.editableRoles}
                                  taskOrEvent={this.props.taskOrEvent}
                                  hideSaveIcon={true}
                                  referencedRoles={this.props.referencedRoles}
                                  viewContext={this.props.viewContext}
                                  context={this.props.context}
                                  taskList={this.props.taskList}
                                  taskRolesData={this.props.taskRolesData}
                                  referencedData={this.props.referencedData}

    />);
  };

  getNotificationsView = () => {
    var oNotificationsData = this.props.notificationsData;
    var aNotifications = oNotificationsData && oNotificationsData.notifications;
    var bShowLoadMore = oNotificationsData && oNotificationsData.showLoadMore;
    var bIsRefreshing = oNotificationsData && oNotificationsData.isRefreshing;

    return (<NotificationsView
        notifications={aNotifications}
        showLoadMore={bShowLoadMore}
        isRefreshing={bIsRefreshing}
    />)

  };

  getUserScheduledWorkflowListView = (oUserScheduledWorkflowList, bIsAllWorkflowChecked, aCheckedWorkflowList) => {
    var oUserScheduledWorkflowListData = oUserScheduledWorkflowList.userScheduledWorkflowList;

    return (
       <WorkflowView
           workflowList={oUserScheduledWorkflowListData}
           bIsAllWorkflowChecked={bIsAllWorkflowChecked}
           checkedWorkflowList={aCheckedWorkflowList}
       />
    )

  };

  render() {
    let __props = this.props;
    var oTaskByLevelListView = this.getTaskLevelsListView();
    let oNotificationData = __props.notificationsData;
    let aTaskLevelsList = this.props.taskLevelsList;
    let bIsLeftPanelPresent = !CS.isEmpty(aTaskLevelsList);
    let sClassName = bIsLeftPanelPresent ? "tasksBody " : "tasksBody withoutLeftPanel ";
    var oListView = (oNotificationData && oNotificationData.isNotificationViewActive) ? this.getNotificationsView() : this.getListView();
    let oWorkflowTemplateData = this.props.workflowTemplateData;
    let oWorkflowDialogView = null;
    if(CS.isNotEmpty(oWorkflowTemplateData)){
      let bIsAllWorkflowChecked = oWorkflowTemplateData.bIsAllWorkflowChecked;
      let aCheckedWorkflowList = oWorkflowTemplateData.checkedWorkflowList;
      let oUserScheduledWorkflowList = oWorkflowTemplateData.userScheduledWorkflowList;
      oListView = (oUserScheduledWorkflowList && oUserScheduledWorkflowList.isUserScheduledWorkflowListViewActive) ? this.getUserScheduledWorkflowListView(oUserScheduledWorkflowList,bIsAllWorkflowChecked,aCheckedWorkflowList) : oListView;
      oWorkflowDialogView = this.getWorkflowDialogView(oWorkflowTemplateData);
    }

    return (
        <div className="contentTasksViewContainer">
          <div className="contentTasksView">
            <div className="tasksHeader">
              <div className="tasksTitle">{getTranslation().TASKS}</div>
            </div>
            <div className={sClassName}>
              {oTaskByLevelListView}
              {oListView}
              {oWorkflowDialogView}
            </div>
          </div>
        </div>
    );
  }
}

export const view = TasksView;
export const events = oEvents;
