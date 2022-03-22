import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import { view as TasksListView } from './task-list-view';
import { view as CustomDialogView } from '../../../viewlibraries/customdialogview/custom-dialog-view';

var oEvents = {
  HANDLE_PROPERTY_TASK_CLOSE_CLICKED: "handle_property_task_close_clicked",
  HANDLE_PROPERTY_TASK_SAVE_EVENT: "content_task_save_clicked",
  HANDLE_PROPERTY_TASK_DISCARD_CLICKED: "handle_property_task_discard_clicked",
};

const oPropTypes = {
  data: ReactPropTypes.object,
};
/**
 * @class PropertyTasksEditButtonView
 * @description - To show tasks on property in dialog.(All property level tasks will be shown here)
 * @memberOf Views
 * @property {object} [data] -
 */

// @CS.SafeComponent
class PropertyTasksEditButtonView extends React.Component {
  static propTypes = oPropTypes;


  state = {};

  handleDiscardTaskClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_PROPERTY_TASK_DISCARD_CLICKED);
  };

  closeDialog = () => {
    EventBus.dispatch(oEvents.HANDLE_PROPERTY_TASK_CLOSE_CLICKED);
  };

  handleSaveTaskClicked = () => {
    var sTaskOrEvent = 'task';
    EventBus.dispatch(oEvents.HANDLE_PROPERTY_TASK_SAVE_EVENT, sTaskOrEvent);
  };

  handleButtonClick = (sButtonId) => {
    if (sButtonId === "save") {
      this.handleSaveTaskClicked();
    }
    else if (sButtonId === "discard") {
      this.handleDiscardTaskClicked();
    }
    else {
      this.closeDialog();
    }
  };

  render () {
    var oData = this.props.data || {};
    var bIsTaskDialogActive = oData.isDialogOpen;
    var aTaskDetailView = [];
    let aButtonData = [];
    let fButtonHandler = this.handleButtonClick;
    if (bIsTaskDialogActive) {
      let oActiveTask = oData.activeTask;
      aTaskDetailView.push(<TasksListView taskListGroupByType={oData.taskListGroupByType}
                                                 activeTask={oActiveTask}
                                                 activeSubTask={oData.activeSubTask}
                                                 isTaskDetailViewOpen={oData.isTaskDetailViewOpen}
                                                 taskViewProps={oData.taskViewProps}
                                                 usersList={oData.usersList}
                                                 currentUser={oData.currentUser}
                                                 toolBarVisibility={oData.toolBarVisibility}
                                                 activeContent={oData.activeContent}
                                                 priorityTags={oData.priorityTags}
                                                 statusTags={oData.statusTags}
                                                 selectedGroupByType={oData.selectedGroupByType}
                                                 isDisabled={oData.isDisabled}
                                                 editableRoles={oData.editableRoles}
                                                 isDialogOpen={oData.isDialogOpen}
                                                 taskOrEvent='task'
                                                 referencedRoles={oData.referencedRoles}
                                                 taskRolesData={oData.taskRolesData}
                                                 referencedData={oData.referencedData}/>);


      if (oActiveTask && oActiveTask.isDirty) {
        aButtonData = [
          {
            id: "discard",
            label: getTranslation().DISCARD,
            isDisabled: false,
            isFlat: true,
          },
          {
            id: "save",
            label: getTranslation().SAVE,
            isDisabled: !oData.isTaskDirty,
            isFlat: false,
          }
        ];
      } else {
        aButtonData = [
          {
            id: "ok",
            label: getTranslation().OK,
            isDisabled: false,
            isFlat: false,
          }
        ];
      }
    }

    let oContentStyle = {
      height:'80%',
      maxWidth:'none',
      width:'80%'
    };

    return (
          <CustomDialogView
              open={bIsTaskDialogActive}
              className="propertyTaskDialog"
              contentClassName="propertyTaskDialogContent"
              bodyClassName="propertyTaskDialogBody"
              buttonData={aButtonData}
              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
              buttonClickHandler={fButtonHandler}
              contentStyle={oContentStyle}
          >
            <div className="propertyTaskEditSectionContainer">
              <div className="propertyTaskDialogTitle">
                {getTranslation().TASKS}
              </div>
              <div className="propertyTaskEditSection">
                {aTaskDetailView}
              </div>
            </div>
          </CustomDialogView>
    );
  }
}

export const view = PropertyTasksEditButtonView;
export const events = oEvents;
