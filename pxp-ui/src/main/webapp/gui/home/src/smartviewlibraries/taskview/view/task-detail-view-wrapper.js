import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ContentTasksDetailView } from './task-detail-view';

var oEvents = {
  CONTENT_TASK_DETAIL_TASK_SAVE_CLICKED: "content_task_save_clicked",
  CONTENT_TASK_DETAIL_SUBTASK_BACK_BUTTON_CLICKED: "content_task_subtask_back_button_clicked",
  CONTENT_TASK_RESET_SCROLL_EVENT: "content_task_reset_scroll_event"
};

const oPropTypes = {
  activeTask: ReactPropTypes.object,
  activeTaskFormViewModels: ReactPropTypes.array,
  activeSubTask: ReactPropTypes.object,
  isTaskDetailViewOpen: ReactPropTypes.bool,
  taskViewProps: ReactPropTypes.object,
  usersList: ReactPropTypes.array,
  currentUser: ReactPropTypes.object,
  priorityTags: ReactPropTypes.array,
  statusTags: ReactPropTypes.array,
  isDisabled: ReactPropTypes.bool,
  isCloseDetailViewVisible: ReactPropTypes.bool,
  toolBarVisibility: ReactPropTypes.object,
  isImageAnnotationView: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  taskOrEvent: ReactPropTypes.string,
  referencedRoles: ReactPropTypes.object,
  taskLinkedInstanceData: ReactPropTypes.object,
  taskRolesData: ReactPropTypes.array,
  referencedData: ReactPropTypes.object
};
/**
 * @class ContentTaskSubtaskDetailView
 * @description This is wrapper view which combines task detail view & subtask detail view
 * @memberOf Views
 * @property {object} [activeTask] - Contains parent task.
 * @property {array} [activeTaskFormViewModels] -
 * @property {object} [activeSubTask] - Contains sub task details.
 * @property {bool} [isTaskDetailViewOpen] - To show Task Detail View.
 * @property {object} [taskViewProps] - Props related to tasks i.e isSelected.
 * @property {array} [usersList] - User List.
 * @property {object} [currentUser] - Contains current user details.
 * @property {array} [priorityTags]
 * @property {array} [statusTags]
 * @property {bool} [isDisabled] - To disable ContentTaskSubtaskDetailView
 * @property {bool} [isCloseDetailViewVisible] - Used to show close button for Task Detail View.
 * @property {object} [toolBarVisibility] - Contains toolBar buttons visibility data.
 * @property {bool} [isImageAnnotationView]
 * @property {string} [context] - To differentiate screen.
 * @property {string} [taskOrEvent] - To determined task or event is will be render.
 * @property {object} [referencedRoles] - Referenced Roles.
 * @property {object} [taskLinkedInstanceData]
 */

// @CS.SafeComponent
class ContentTaskSubtaskDetailView extends React.Component {
  static propTypes = oPropTypes;

  constructor(props) {
    super(props);
    this.taskAndSubTaskDetailViewContainer = React.createRef();
    this.taskDetailElementView = React.createRef();
    this.taskAndSubTaskDetailView = React.createRef();
  }

  componentDidUpdate () {
    var oTaskViewProps = this.props.taskViewProps;
    var bIsResetScroll = oTaskViewProps.resetScrollStatus;
    var iScrollLeft = 0;
    var oTasksDetailContainerDOM = this.taskAndSubTaskDetailView.current;
    var oTaskDetailElementDOM = this.taskDetailElementView.current;

    if (!CS.isEmpty(this.props.activeSubTask)) {
      if (bIsResetScroll) {
        this.resetScroll();
      }
      iScrollLeft = oTaskDetailElementDOM.offsetWidth;
    } else {
      iScrollLeft = 0;
    }
    // $(oTasksDetailContainerDOM).animate({scrollLeft: iScrollLeft}, {
    //   duration: 'fast',
    //   easing: 'swing'
    // });
      oTasksDetailContainerDOM.scrollBy({
          left: iScrollLeft,
          behavior: 'smooth'
      });
  }

  resetScroll = () => {
    var oTaskAndSubTaskDetailViewDOM = this.taskAndSubTaskDetailViewContainer.current;
    // $(oTaskAndSubTaskDetailViewDOM).animate({scrollTop: 0}, {
    //   duration: 'fast',
    //   easing: 'swing'
    // });
      oTaskAndSubTaskDetailViewDOM.scrollBy({
          top: 0,
          behavior: 'smooth'
      });
    EventBus.dispatch(oEvents.CONTENT_TASK_RESET_SCROLL_EVENT);
  };

  handleSaveTaskClicked = () => {
    var sTaskOrEvent = this.props.taskOrEvent;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_TASK_SAVE_CLICKED, sTaskOrEvent);
  };

  handleSubtaskDetailViewBackButtonClicked = () => {
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_SUBTASK_BACK_BUTTON_CLICKED);
  };

  getTaskDetailView = (oTask, sContext) => {
    var oParentTask = null;
    if (sContext === "subtask") {
      oParentTask = this.props.activeTask;
    }
    return (
        <div className="taskDetailElementView" ref={this.taskDetailElementView} key={"taskDetailElementView_" + sContext}>
          <ContentTasksDetailView task={oTask}
                                  activeContent={this.props.activeContent}
                                  activeTaskFormViewModels={this.props.activeTaskFormViewModels}
                                  parentTask={oParentTask}
                                  isTaskDetailViewOpen={this.props.isTaskDetailViewOpen}
                                  toolBarVisibility={this.props.toolBarVisibility}
                                  taskViewProps={this.props.taskViewProps}
                                  usersList={this.props.usersList}
                                  currentUser={this.props.currentUser}
                                  priorityTags={this.props.priorityTags}
                                  statusTags={this.props.statusTags}
                                  isDisabled={this.props.isDisabled}
                                  isCloseDetailViewVisible={this.props.isCloseDetailViewVisible}
                                  taskOrEvent={this.props.taskOrEvent}
                                  context={sContext}
                                  isImageAnnotationView={this.props.isImageAnnotationView}
                                  referencedTask={this.props.referencedTasks}
                                  editableRoles={this.props.editableRoles}
                                  referencedRoles={this.props.referencedRoles}
                                  taskLinkedInstanceData={this.props.taskLinkedInstanceData}
                                  taskRolesData={this.props.taskRolesData}
                                  referencedData={this.props.referencedData}/>
        </div>
    );
  };

/*
  getSaveButtonView = () => {
    var oToolBarVisibility = this.props.toolBarVisibility;
    var oSaveButtonView = this.props.isImageAnnotationView && (oToolBarVisibility && oToolBarVisibility.bIsSaveVisible) ?
                          (<TooltipView
                              label={getTranslation().SAVE_TASK}>
                            <div className="saveButton"
                                onClick={this.handleSaveTaskClicked}></div>
                          </TooltipView>) : null;
    var sClassName = oSaveButtonView ? "buttonContainer visible " : "buttonContainer ";
    return (
        <div className={sClassName}>
          {oSaveButtonView}
        </div>
    );
  };
*/

  getBackButtonView = () => {
    var oToolBarVisibility = this.props.toolBarVisibility;
    var bIsSaveVisible = (oToolBarVisibility && oToolBarVisibility.bIsSaveVisible);
    var sBackButtonClassName = "backButton ";
    (bIsSaveVisible && this.props.isImageAnnotationView) && (sBackButtonClassName += "saveVisible");
    var oBackButtonView = null;
    if (!CS.isEmpty(this.props.activeSubTask)) {
      oBackButtonView =
          <div className={sBackButtonClassName} onClick={this.handleSubtaskDetailViewBackButtonClicked}></div>;
    }

    return oBackButtonView;
  };

  render () {
    var sContext = this.props.taskOrEvent;
    var oTaskDetailView = (this.props.activeTask) ? this.getTaskDetailView(this.props.activeTask, sContext) : null;
    var oSubTaskDetailView = (!CS.isEmpty(this.props.activeSubTask)) ? this.getTaskDetailView(this.props.activeSubTask, "subtask") : null;
    var oBackButtonView = this.getBackButtonView();

    return (
        <div className="taskAndSubTaskDetailViewContainer" ref={this.taskAndSubTaskDetailViewContainer}>
          {oBackButtonView}
          <div className="taskAndSubTaskDetailView" ref={this.taskAndSubTaskDetailView}>
            <div className="taskSubtaskDetails">
              {oTaskDetailView}
              {oSubTaskDetailView}
            </div>
          </div>
        </div>
    );
  }
}

export const view = ContentTaskSubtaskDetailView;
export const events = oEvents;
