import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import { view as TaskDetailViewWrapper } from './task-detail-view-wrapper';
import { view as CustomDialogView } from '../../../viewlibraries/customdialogview/custom-dialog-view';

var oEvents = {
  TASK_DETAIL_DIALOG_BUTTON_CLICKED: "task_detail_dialog_button_clicked",
};

const oPropTypes = {
  data: ReactPropTypes.object,
};
/**
 * @class TaskDetailDialogView
 * @memberOf Views
 * @property {object} [data]
 */

// @CS.SafeComponent
class TaskDetailDialogView extends React.Component {
  static propTypes = oPropTypes;

  handleButtonClick =(bIsDialogExit, sButtonId)=> {
    let oData = this.props.data;
    let sId = oData.activeTask.type && oData.activeTask.type.selectedItem;
    let sActiveTaskId = oData.activeTask.id;
    let oExtraData = {
      id: sId,
      taskOrEvent: oData.taskOrEvent,
      activeTaskId: sActiveTaskId,
      context: oData.context,
      buttonId: sButtonId,
      isDialogExit: bIsDialogExit
    };
      EventBus.dispatch(oEvents.TASK_DETAIL_DIALOG_BUTTON_CLICKED, oExtraData)
  };

  getButtonData = (oData) => {
    let oActiveTask = oData.activeTask;
    let aButtonData = [];
    let iCancelButtonIndex = 0;
    if(CS.isNotEmpty(oActiveTask)) {
      let bIsActiveTaskDirty = oActiveTask.isDirty;
      if (oData.taskOrEvent == 'event') {
        let sDataContext = oData.context || "";
        if (sDataContext === "createEvent") {
          aButtonData.push({
                id: "cancel",
                isFlat: true,
                label: getTranslation().CANCEL
              },
              {
                id: "createGlobalEvent",
                isFlat: false,
                label: getTranslation().CREATE
              });
        } else if (sDataContext === "editEvent") {
          iCancelButtonIndex = 1;
          aButtonData.push({
                id: "deleteGlobalEvent",
                label: getTranslation().DELETE,
                isFlat: false,
                className: "deleteButtonWithRedColor"
              },
              {
                id: "cancel",
                isFlat: true,
                label: getTranslation().CANCEL
              },
              {
                id: "ok",
                label: getTranslation().OK,
                isDisabled: false,
                isFlat: false
              });
          if (oData.toolBarVisibility.canDelete) {
            iCancelButtonIndex = 2;
            aButtonData.unshift({
              id: "deleteGlobalEvent",
              label: getTranslation().DELETE,
              isFlat: false,
              className: "deleteButtonWithRedColor"
            })
          }
          if (bIsActiveTaskDirty) {
            CS.remove(aButtonData, {id: "ok"});
            CS.remove(aButtonData, {id: "cancel"});
            aButtonData.push({
                  id: "discard",
                  label: getTranslation().DISCARD,
                  isDisabled: false,
                  isFlat: true
                },
                {
                  id: "save",
                  label: getTranslation().SAVE,
                  isFlat: false
                })
          }
        }
      } else if (bIsActiveTaskDirty) {
        aButtonData = [
          {
            id: "save",
            label: getTranslation().SAVE,
            isDisabled: !oData.toolBarVisibility.bIsSaveVisible,
            isFlat: false,
          }
        ];
        if (oActiveTask.isCreated) {
          aButtonData.unshift(
              {
                id: "cancel",
                label: getTranslation().CANCEL,
                isDisabled: false,
                isFlat: true
              });
        } else {
          aButtonData.unshift(
              {
                id: "discard",
                label: getTranslation().DISCARD,
                isDisabled: false,
                isFlat: true,
              }
          );
        }
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
    return {
      buttonData: aButtonData,
      cancelButtonIndex: iCancelButtonIndex,
    }
  };

  render () {
    let oData = this.props.data;
    let { buttonData : aButtonData , cancelButtonIndex : iCancelButtonIndex } = this.getButtonData(oData);
    let fButtonHandler = this.handleButtonClick;
    let bIsOpen = CS.has(oData, "isTaskDetailViewOpen") ? oData.isTaskDetailViewOpen : true;
    let bIsImageAnnotationView =  CS.has(oData, "isImageAnnotationView") ? oData.isImageAnnotationView : true;
    let sHeading  = oData.heading ? oData.heading : getTranslation().ANNOTATION;
    let oContentStyle = oData.contentStyle;

    return (
        <CustomDialogView
            open={bIsOpen}
            className="propertyTaskDialog"
            contentClassName="propertyTaskDialogContent imageAnnotationDialog"
            bodyClassName="propertyTaskDialogBody"
            buttonData={aButtonData}
            onRequestClose={fButtonHandler.bind(this, true, aButtonData[iCancelButtonIndex].id)}
            buttonClickHandler={fButtonHandler.bind(this, false)}
            contentStyle = {oContentStyle}
            title= {sHeading}
        >
          <div className="propertyTaskEditSectionContainer">
            <div className="propertyTaskEditSection imageAnnotationEditSection">
              <TaskDetailViewWrapper activeTask={oData.activeTask}
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
                                            taskOrEvent= {oData.taskOrEvent}
                                            isCloseDetailViewVisible={false}
                                            isImageAnnotationView={bIsImageAnnotationView}
                                            referencedTasks={oData.referencedTasks}
                                            editableRoles={oData.editableRoles}
                                            referencedRoles={oData.referencedRoles}
                                            taskRolesData={oData.taskRolesData}/>
            </div>
          </div>
        </CustomDialogView>
    );
  }
}

export const view = TaskDetailDialogView;
export const events = oEvents;
