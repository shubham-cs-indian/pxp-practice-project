import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as ProcessRuntimeGraphView } from './process-runtime-graph-view';
import { view as JobComponentStatusView } from './job-component-status-view';
import MomentUtils from '../../../../../commonmodule/util/moment-utils';
import {view as CustomDialogView} from "../../../../../viewlibraries/customdialogview/custom-dialog-view";
import {view as CustomMaterialButtonView} from "../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view";
import RequestMapping from "../../../../../libraries/requestmappingparser/request-mapping-parser";
import DashboardScreenRequestMapping from "../screens/dashboardscreen/tack/dashboard-screen-request-mapping";
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  JOB_ITEM_CLICKED: "job_item_clicked",
  PROCESS_DATA_DOWNLOAD_BUTTON_CLICKED: "process_data_download_button_clicked",
  PROCESS_INSTANCE_DIALOG_BUTTON_CLICKED: "process_instance_dialog_button_clicked",
  PROCESS_INSTANCE_DOWNLOAD_BUTTON_CLICKED: "process_instance_download_button_clicked"
};
const oPropTypes = {
  jobItem: ReactPropTypes.object,
  activeJob: ReactPropTypes.object,
  selectedTag: ReactPropTypes.object,
  masterTag: ReactPropTypes.object,
  activeJobGraphData: ReactPropTypes.object,
  instanceIID: ReactPropTypes.number
};

// @CS.SafeComponent
class JobItemView extends React.Component {
  static propTypes = oPropTypes;

  getJobDateFormat = (iDateValue) => {
    if(iDateValue == null || iDateValue == 0) {
      return '-';
    }
    let sDateAndTime = MomentUtils.getDateAttributeInDateTimeFormat(iDateValue);
    let sDate=sDateAndTime.date +" "+ sDateAndTime.time;
    return sDate;
  };

  handleJobNodeClicked = (oEvent) => {
    var oJob = this.props.jobItem;
    EventBus.dispatch(oEvents.JOB_ITEM_CLICKED, oJob.instanceIID);
  };

  handleProcessDataDownloadButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.PROCESS_DATA_DOWNLOAD_BUTTON_CLICKED, sButtonId);
  };

  handleProcessInstanceDownloadButtonClicked = (iProcessInstanceId, iInstanceIID) => {
    EventBus.dispatch(oEvents.PROCESS_INSTANCE_DOWNLOAD_BUTTON_CLICKED, iProcessInstanceId, iInstanceIID);
  };

  handleProcessInstanceDialogButtonClicked = (sButton) => {
    EventBus.dispatch(oEvents.PROCESS_INSTANCE_DIALOG_BUTTON_CLICKED, sButton);
  };

  /** To get children for Process Instance dialog view **/
  getDialogViewDOM = () => {
    let oDownloadExecutionStatusButton = (<CustomMaterialButtonView
        label={getTranslation().DOWNLOAD_EXECUTION_STATUS}
        isRaisedButton={true}
        isDisabled={false}
        onButtonClick={this.handleProcessDataDownloadButtonClicked.bind(this, "downloadExecutionStatus")}/>);

    let oDownloadFilesButton = (
        <CustomMaterialButtonView
            label={getTranslation().DOWNLOAD_FILES}
            isRaisedButton={true}
            isDisabled={false}
            onButtonClick={this.handleProcessDataDownloadButtonClicked.bind(this, "downloadFiles")}/>);

    return (
        <div>
          {oDownloadExecutionStatusButton}
          {oDownloadFilesButton}
        </div>
    )
  };

  /** To get Process Instance Dialog View**/
  getProcessInstanceDialogView = () => {
    let oBodyStyle = {
      maxWidth: "500px",
      minWidth: "500px",
      overflowY: "auto",
      width: "50%"
    };

    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isDisabled: false,
        isFlat: true,
      }
    ];

    let oJob = this.props.jobItem;
    let isProcessInstanceDialogOpen = oJob.instanceIID == this.props.instanceIID && this.props.isProcessInstanceDialogOpen;
    let fButtonHandler = this.handleProcessInstanceDialogButtonClicked;
    let oDialogViewDOM = this.getDialogViewDOM();

    return (<CustomDialogView modal={true}
                              open={isProcessInstanceDialogOpen}
                              title={getTranslation().DOWNLOAD_PROCESS_DATA}
                              bodyStyle={oBodyStyle}
                              contentStyle={oBodyStyle}
                              bodyClassName=""
                              contentClassName=""
                              buttonData={aButtonData}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler.bind()}
                              children={oDialogViewDOM}>
    </CustomDialogView>)
  };

  render() {
    var oJob = this.props.jobItem;
    var oActiveJob = this.props.activeJob;
    var oSelectedTag = this.props.selectedTag;
    var oStyle = {};
    var sTagLabel = '';
    if(!CS.isEmpty(oSelectedTag)) {
      oStyle.backgroundColor = oSelectedTag.color;
      sTagLabel = CS.getLabelOrCode(oSelectedTag);
    }

    var sClassName = "jobItemViewContainer jobItemContainer ";
    var aJobChildrenDOMS = [];

    if(oActiveJob.instanceIID === oJob.instanceIID) {
      aJobChildrenDOMS.push(
          <JobComponentStatusView job={oActiveJob}/>
      );

      var bIsInProgress = oSelectedTag.id === "PROCESS_STATUS_INPROGRESS";
      aJobChildrenDOMS.push(
          <ProcessRuntimeGraphView isInProgress={bIsInProgress} masterTag={this.props.masterTag} activeJobGraphData={this.props.activeJobGraphData} activeJob={this.props.activeJob}/>
      );
      sClassName += "activeItem"
    }

    return (
        <div className={sClassName}>
          <div className="jogRowItem">
            <div className="jogRowItemCollapse" onClick={this.handleJobNodeClicked}>
              <div className="jobStatus" style={oStyle}></div>
              <div className="jobCell jobLabel">{CS.getLabelOrCode(oJob)}</div>
              <div className="jobCell jobDate">{this.getJobDateFormat(oJob.startTime)}</div>
              <div className="jobCell jobDate">{this.getJobDateFormat(oJob.endTime)}</div>
              <div className="jobCell jobStatusSymbol">{sTagLabel}</div>
            </div>
              <div className="filterSummaryRowClearLabel">
                <div className="clearFilterLabel" onClick={this.handleProcessInstanceDownloadButtonClicked.bind(this, oJob.processInstanceId, oJob.instanceIID)}>
                  {getTranslation().DOWNLOAD}
                </div>
              </div>
          </div>
          <div className="jobChildrenContainer">{aJobChildrenDOMS}</div>
          {this.getProcessInstanceDialogView()}
        </div>
    );
  }
}

export const view = JobItemView;
export const events = oEvents;
