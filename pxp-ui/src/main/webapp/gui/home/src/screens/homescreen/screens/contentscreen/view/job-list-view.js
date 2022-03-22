import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as JobItemView } from './job-item-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
};

const oPropTypes = {
  jobList: ReactPropTypes.array,
  activeJob: ReactPropTypes.object,
  activeJobGraphData: ReactPropTypes.object,
  instanceIID: ReactPropTypes.number
};

// @CS.SafeComponent
class JobListView extends React.Component {
  static propTypes = oPropTypes;

  getMasterTags = () => {
    return {
      id: "PROCESS_STATUS_TAG",
      label: "Status",
      children: [
        {
          id: "PROCESS_STATUS_INPROGRESS",//PROCESS_STATUS_INPROGRESS
          label: getTranslation().IN_PROGRESS,
          color: "#ffa634"
        },
        {
          id: "PROCESS_STATUS_COMPLETED",
          label:  getTranslation().COMPLETED,
          color: "#2a672d"
        },
        {
          id: "PROCESS_STATUS_FAILED",
          label:  getTranslation().FAILED,
          color: "#ff3d3c"
        },
        {
            id: "PROCESS_STATUS_NOT_APPLICABLE",
            label:  getTranslation().NOT_APPLICABLE,
            color: "#7D7D7D"
        },
        {
          id: "PROCESS_STATUS_DISCARDED_ON_RESTART",
          label: getTranslation().DISCARDED_ON_RESTART,
          color: "#a6b1f7"
        }
      ]
    }
  };

  getSelectedStatusTag = (oJob) => {
    let oMasterTag = this.getMasterTags();
    let iProcessStatusCode = oJob.status;
    let sProcessStatusId;

    switch (iProcessStatusCode) {
      case 0:              // UNDEFINED
        sProcessStatusId = "PROCESS_STATUS_NOT_APPLICABLE";
        break;
      case 1:              // PENDING
      case 2:              // RUNNING
      case 6:              // PAUSED
        sProcessStatusId = "PROCESS_STATUS_INPROGRESS";
        break;
      case 3:              // ENDED_SUCCESS
        sProcessStatusId = "PROCESS_STATUS_COMPLETED";
        break;
      case 4:              // ENDED_ERRORS
      case 5:              // ENDED_EXCEPTION
      case 7:              // INTERRUPTED
      case 8:              // CANCELED
        sProcessStatusId = "PROCESS_STATUS_FAILED";
        break;
      case 9:              // DISCARDED_ON_RESTART
        sProcessStatusId = "PROCESS_STATUS_DISCARDED_ON_RESTART";
        break;
      default :
        sProcessStatusId = "PROCESS_STATUS_NOT_APPLICABLE";
        break;
    }
    let oSelectedTagValue = CS.find(oMasterTag.children, {id: sProcessStatusId});
    return oSelectedTagValue;
  };

  getJobListView = () => {
    var _this = this;
    var aViews = [];
    var aJobListViews = [];
    var aInprogressJobs = [];
    var oActiveJob = _this.props.activeJob;
    aViews.push(
        <div className="jobItemContainer jobColumnContainer">
          <div className="jogRowItem">
            <div className="jobCell jobLabel">{getTranslation().TITLE}</div>
            <div className="jobCell jobDate">{getTranslation().WORKFLOW_START_TIME}</div>
            <div className="jobCell jobDate">{getTranslation().WORKFLOW_END_TIME}</div>
            <div className="jobCell jobStatusSymbol">{getTranslation().STATUS}</div>
            <div className="jobCell jobExecutionStatus">{getTranslation().DOWNLOAD}</div>
          </div>
        </div>
    );

    CS.forEach(this.props.jobList, function (oJob) {
      let oSelectedTag = _this.getSelectedStatusTag(oJob);
      let oView = (
          <JobItemView
              selectedTag={oSelectedTag}
              masterTag={_this.getMasterTags()}
              jobItem={oJob}
              activeJobGraphData={_this.props.activeJobGraphData}
              activeJob={oActiveJob}
              isProcessInstanceDialogOpen={_this.props.isProcessInstanceDialogOpen}
              instanceIID={_this.props.instanceIID}/>
      );

      if(oSelectedTag.id === "PROCESS_STATUS_INPROGRESS") {
        aInprogressJobs.push(oView);
      } else {
        aJobListViews.push(oView);
      }

    });

    if(!CS.isEmpty(aInprogressJobs)) {
      aViews.push(
          <div className="listGroup">
            <div className="listGroupLabel">{getTranslation().IN_PROGRESS}</div>
            {aInprogressJobs}
          </div>
      );
    }

    if(!CS.isEmpty(aJobListViews)) {

      var oLabelView = CS.isEmpty(aInprogressJobs) ? null : (<div className="listGroupLabel">{getTranslation().OTHER}</div>);

      aViews.push(
          <div className="listGroup">
            {oLabelView}
            {aJobListViews}
          </div>
      );
    }

    return aViews;

  };

  render() {
    return (
        <div className="jobListViewContainer">
          {this.getJobListView()}
        </div>
    );
  }
}

export const view = JobListView;
export const events = oEvents;
