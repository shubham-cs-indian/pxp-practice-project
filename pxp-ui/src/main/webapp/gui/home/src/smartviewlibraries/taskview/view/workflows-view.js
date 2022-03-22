import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import {getTranslations as getTranslation} from '../../../commonmodule/store/helper/translation-manager.js';
import {view as NothingFoundView} from "../../../viewlibraries/nothingfoundview/nothing-found-view";
import {view as GridYesNoPropertyView} from "../../../viewlibraries/gridview/grid-yes-no-property-view";
import {view as SimpleSearchBarView} from "../../../viewlibraries/simplesearchbarview/simple-search-bar-view";
import TooltipView from "../../../viewlibraries/tooltipview/tooltip-view";

var oEvents = {
  WORKFLOW_SCHEDULED_BUTTON_CLICKED: "workflow_scheduled_button_clicked",
  WORKFLOW_DELETE_BUTTON_CLICKED: "workflow_delete_button_clicked",
  WORKFLOW_SEARCH_TEXT_CHANGED: "workflow_search_text_changed",
  WORKFLOW_LIST_SELECT_ALL_CHECKBOX_CLICKED: "workflow_list_select_all_checkbox_clicked",
  WORKFLOW_LIST_SELECT_CHECKBOX_CLICKED: "workflow_list_select_checkbox_clicked",
  WORKFLOW_DELETE_ALL_BUTTON_CLICKED: "workflow_delete_all_button_clicked",
};

const oPropTypes = {
  workflowList: ReactPropTypes.array,
  checkedWorkflowList: ReactPropTypes.array,
  bIsAllWorkflowChecked: ReactPropTypes.bool,
};

// @CS.SafeComponent
class WorkflowView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);
    this.state = {
      searchString: "",
    }
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

  handleScheduledWorkflowToggleButtonClicked = (oData, sValue) => {
    EventBus.dispatch(oEvents.WORKFLOW_SCHEDULED_BUTTON_CLICKED, oData, sValue);
  };

  handleScheduledWorkflowDeleteButtonClicked = (sWorkflowId) => {
    EventBus.dispatch(oEvents.WORKFLOW_DELETE_BUTTON_CLICKED, sWorkflowId);
  };

  handleSearchChanged = (sContext, sNewValue) => {
    EventBus.dispatch(oEvents.WORKFLOW_SEARCH_TEXT_CHANGED, sNewValue, sContext);
    this.setState({searchString: sNewValue});
  };

  handleAllWorkflowListCheckBoxClicked = (sContext, sValue) => {
    EventBus.dispatch(oEvents.WORKFLOW_LIST_SELECT_ALL_CHECKBOX_CLICKED, sContext, this.state.searchString, sValue);
  };

  handleScheduledWorkflowCheckboxButtonClicked = (sContext, bIsChecked, sWorkflowId) => {
    EventBus.dispatch(oEvents.WORKFLOW_LIST_SELECT_CHECKBOX_CLICKED, sContext, bIsChecked, sWorkflowId);
  };

  handleScheduledWorkflowBulkDeleteButtonClicked = () => {
    EventBus.dispatch(oEvents.WORKFLOW_DELETE_ALL_BUTTON_CLICKED);
  };

  getWorkflowNotFoundDOM = () => {
    return (<NothingFoundView message={getTranslation().NOTHING_FOUND}/>)
  };

  getWorkflowView = (aWorkflowList) => {
    let aGroupView = [];
    let aWorkflowGroupView = [];
    let fHandlerToggle = this.handleScheduledWorkflowToggleButtonClicked;
    let fHandlerDelete = this.handleScheduledWorkflowDeleteButtonClicked;
    let fHandlerCheckboxClicked = this.handleScheduledWorkflowCheckboxButtonClicked;
    let aCheckedWorkflowList = this.props.checkedWorkflowList;
    let bIsAllWorkflowChecked = this.props.bIsAllWorkflowChecked;
    let sClassName = "workflowListCheck ";
    sClassName += bIsAllWorkflowChecked ? "checked" : "";

    CS.forEach(aWorkflowList, function (oData) {
      if (!CS.isEmpty(oData)) {
        let bIsChecked = CS.includes(aCheckedWorkflowList, oData.id);
        let sNewClassName = "workflowListCheck ";
        sNewClassName += bIsChecked ? "checked" : "";
        let sClass = !bIsAllWorkflowChecked ? sNewClassName : sClassName;
        let oRow = <div className="workflowDetails">
          <div className={sClass} onClick={fHandlerCheckboxClicked.bind(this, "workflow", !bIsChecked, oData.id)}></div>
          <div className="workflowListLabel">{oData.label}</div>
          <div className="workflowActionDetails">
            <div className="scheduleWorkflow">
              <GridYesNoPropertyView
                  isDisabled={false}
                  value={oData.isExecutable}
                  onChange={fHandlerToggle.bind(this, oData)}
              />
            </div>
            <div className="deleteWorkflow" onClick={fHandlerDelete.bind(this, oData.id)}></div>
          </div>
        </div>
        aWorkflowGroupView.push(oRow);
      }
    });

    let oDeleteButtonView = CS.isNotEmpty(aCheckedWorkflowList) ?
        (<TooltipView label={getTranslation().DELETE}>
          <div className="deleteTasks"
               onClick={this.handleScheduledWorkflowBulkDeleteButtonClicked}></div>
        </TooltipView>) : null;

    let sCheckBoxClassName = "allTaskCheckbox";
    sCheckBoxClassName += this.props.bIsAllWorkflowChecked ? " checked" : "";

    let oSelectAllCheckBoxView =
        <TooltipView placement="bottom" label={"Select All"}>
          <div className={sCheckBoxClassName}
               onClick={this.handleAllWorkflowListCheckBoxClicked.bind(this, "workflow")}></div>
        </TooltipView>

    let oHeaderRow =
        <div className="toolBarContainer">
          <div className="leftToolBar">
            {oSelectAllCheckBoxView}
            <div className="searchBar">
              <SimpleSearchBarView onChange={this.handleSearchChanged.bind(this, "workflow")}
                                   searchText={this.state.searchString}/>
            </div>
            {oDeleteButtonView}
          </div>
        </div>

    let oView = CS.isEmpty(aWorkflowGroupView) ? this.getWorkflowNotFoundDOM() : aWorkflowGroupView;
    aGroupView.push(
        <div className="workflowWrapper">
          <div className="workflowWrapperHeaderRow">
            {oHeaderRow}
          </div>
          <div className="workflowWrapperView">
            {oView}
          </div>
        </div>

    )
    return aGroupView;
  };


  render () {
    let aWorkflowList = this.props.workflowList;
    let oWorkflowView = this.getWorkflowView(aWorkflowList);

    return (
        <div className="workflowViewContainer">
          <div className="workflowToolbar">
            <div className="workflowTitle">{getTranslation().USER_SCHEDULED_WORKFLOWS}</div>
          </div>
          <div className="workflowView">
            {oWorkflowView}
          </div>
        </div>
    );
  }
}

export const view = WorkflowView;
export const events = oEvents;
