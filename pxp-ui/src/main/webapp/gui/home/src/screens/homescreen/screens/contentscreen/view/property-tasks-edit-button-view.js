import React, { Suspense } from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import CS from '../../../../../libraries/cs'
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const TaskController = React.lazy(()=>import('../../../../../smartviewlibraries/taskview/controller/task-controller'));

const oEvents = {
  HANDLE_PROPERTY_TASK_OPEN_CLICKED: "handle_property_task_open_clicked",
};

const oPropTypes = {
  taskDialogData: ReactPropTypes.object,
  property: ReactPropTypes.object,
  isTasksExists: ReactPropTypes.bool,
  isUnmasked: ReactPropTypes.bool
};

// @CS.SafeComponent
class PropertyTasksEditButtonView extends React.Component {

  handleEditButtonClicked = () => {
    var oProperty = this.props.property;
    EventBus.dispatch(oEvents.HANDLE_PROPERTY_TASK_OPEN_CLICKED, oProperty);
  };

  getPropertyTasksView = () => {
    var oTaskDialogData = this.props.taskDialogData;

    return (
      <Suspense fallback={<div></div>}>
        <TaskController
            data={oTaskDialogData}
            context={"propertyTask"}
        />
      </Suspense>
    )
  };

  render() {
    var oTaskDialogData = this.props.taskDialogData;
    var bIsTaskExist = this.props.isTasksExists;
    var sClassName = bIsTaskExist ? "editTasksButton tasksExists" : "editTasksButton ";

    let fHandler = this.props.isUnmasked ? CS.noop() : this.handleEditButtonClicked;
    var oEditViewDOM = (<TooltipView placement="bottom" label={getTranslation().EDIT_TASK}>
          <div className={sClassName} onClick={fHandler}></div>
        </TooltipView>);

    var oPropertyTaskDetailView = oTaskDialogData && oTaskDialogData.isDialogOpen && (this.props.property.id ===
    oTaskDialogData.activeProperty.id) ? this.getPropertyTasksView() : null;

    return (
        <div className="propertyTaskEditButtonContainer">
          {oEditViewDOM}
          {oPropertyTaskDetailView}
        </div>
    );
  }
}

export const view = PropertyTasksEditButtonView;
export const events = oEvents;
