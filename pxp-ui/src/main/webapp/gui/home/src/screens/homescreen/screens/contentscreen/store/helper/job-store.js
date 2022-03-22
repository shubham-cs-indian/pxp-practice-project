import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ScreenModeUtils from './screen-mode-utils';

import ContentUtils from './content-utils';
import JobProps from './../model/job-screen-view-props';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import GlobalStore from './../../../../store/global-store';
import BreadcrumbStore from '../../../../../../commonmodule/store/helper/breadcrumb-store';
import DashboardScreenProps from "../../screens/dashboardscreen/store/model/dashboard-screen-props";
import alertify from "../../../../../../commonmodule/store/custom-alertify-store";
import DashboardScreenRequestMapping from "../../screens/dashboardscreen/tack/dashboard-screen-request-mapping";
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
var getTranslation = ScreenModeUtils.getTranslationDictionary;

var JobStore = (function () {

  var _triggerChange = function () {
    JobStore.trigger('job-change');
  };

  var successFetchAllJobsCallback = function (oCallbackData, oResponse) {
    let oAppData = ContentUtils.getAppData();

    let oSelectedModule = GlobalStore.getSelectedModule();
    let oActiveEndpoint = ContentUtils.getActiveEndpointData();

    let sLabel = getTranslation()[oSelectedModule.label] || oSelectedModule.label;
    let sBreadcrumbLabel = getTranslation().ENDPOINT + " " + oActiveEndpoint.label + " : " + sLabel;
    let sType = BreadCrumbModuleAndHelpScreenIdDictionary.DIMODULE;
    let sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.ENDPOINT_SCREEN;
    let oBreadcrumbItem = BreadcrumbStore.createBreadcrumbItem(oSelectedModule.id, sBreadcrumbLabel, sType, sHelpScreenId);

    CS.assign(oCallbackData.breadCrumbData, oBreadcrumbItem);
    BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);

    oAppData.setJobList(oResponse.success);
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    oDataIntegrationLogsViewData.jobList = oResponse.success;

    _triggerChange();
  };

  var failureFetchAllJobsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse,'failureFetchAllJobsCallback',getTranslation());
  };

  var successFetchJobDetailCallback = function (oResponse) {
    let oJobFromServer = oResponse.success;
    let aJobList = DashboardScreenProps.getDataIntegrationLogsViewData().jobList;
    let oJob = CS.find(aJobList, {instanceIID: oJobFromServer.instanceIID});
    CS.assign(oJob, oJobFromServer);
    _processJobGraphWithStatus(oJobFromServer);
    JobProps.setActiveJob(oJobFromServer);

    _triggerChange();
  };

  var failureFetchJobDetailCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse,'failureFetchJobDetailCallback',getTranslation());
  };

  let successFetchProcessData = function (oResponse) {
    if (CS.isEmpty(oResponse.success.fileStream) || CS.isEmpty(oResponse.success.fileName)) {
      alertify.message(getTranslation().NOTHING_TO_DOWNLOAD)
    } else {
      alertify.success(getTranslation().DOWNLOADING);
      ContentUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
    }
  };

  let failureFetchProcessData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchProcessData', getTranslation());
  };

  /********************************* PRIVATE API's *********************************/

  var _processJobGraphWithStatus = function (oJob) {
    let aInProgressComponentIds = [];
    let aCompletedComponentIds = [];
    let aFailedComponentIds = [];
    let aNotApplicableComponentIds = [];
    let oGraphData = JobProps.getActiveJobGraphData();
    CS.forEach(oJob.components, function (oComponent) {
      let sComponentId = oComponent.componentId;
      switch (oComponent.taskStatus) {
        case 0:              // UNDEFINED
          aNotApplicableComponentIds.push(sComponentId);
          break;
        case 1:              // PENDING
        case 2:              // RUNNING
        case 6:              // PAUSED
          aInProgressComponentIds.push(sComponentId);
          break;
        case 3:              // ENDED_SUCCESS
          aCompletedComponentIds.push(sComponentId);
          break;
        case 4:              // ENDED_ERRORS
        case 5:              // ENDED_EXCEPTION
        case 7:              // INTERRUPTED
        case 8:              // CANCELED
          aFailedComponentIds.push(sComponentId);
          break;
        default :
          aNotApplicableComponentIds.push(sComponentId);
          break;
      }
    });
    oGraphData.completedActivityIds = aCompletedComponentIds;
    oGraphData.failedActivityIds = aFailedComponentIds;
    oGraphData.inProgressActivityIds = aInProgressComponentIds;
    oGraphData.notApplicableActivityIds = aNotApplicableComponentIds;
  };

  let _fetchAllJobs = function () {
   let oCallbackData = {};
   oCallbackData.breadCrumbData = {
     payloadData: [oCallbackData],
     functionToSet: _fetchAllJobsCall
   };
   _fetchAllJobsCall(oCallbackData);
  };

  let _fetchAllJobsCall = function (oCallbackData) {
    let oActiveEndpoint = ContentUtils.getActiveEndpointData();
    let oReqBody = {
      endpointIds: [oActiveEndpoint.id],
      processEventIds: [],
      userIds: [],
      messageTypes: [],
      from: "",
      to: ""
    };
    CS.postRequest(DashboardScreenRequestMapping.GetProcessInstanceSearchData, "", oReqBody, successFetchAllJobsCallback.bind(this, oCallbackData), failureFetchAllJobsCallback);
  };

  var _fetchJobByJobId = function (oJob, bShowLoading) {
    let oReqBody = {
      instanceIIDs: [oJob.instanceIID + ""],
      processDefinition: oJob.processDefinition
    };
    CS.postRequest(getRequestMapping().GetJob, "", oReqBody, successFetchJobDetailCallback.bind(this), failureFetchJobDetailCallback);
  };


  var _handleJobItemClicked = function (sJobId) {
    var oActiveJob = JobProps.getActiveJob();
    if (oActiveJob.instanceIID === sJobId) {
      delete oActiveJob.graphData;
      JobProps.setActiveJob({});
      _triggerChange();

    } else {
      let aJobList = DashboardScreenProps.getDataIntegrationLogsViewData().jobList;
      let oJob = CS.find(aJobList, {instanceIID: sJobId});
      if (oJob) {
        JobProps.setActiveJobGraphData({
          xmlData: oJob.processDefinition,
        });
        _fetchJobByJobId(oJob);
      }
    }
  };

  var _handleProcessGraphRefreshButtonClicked = function (bShowLoading) {
    var oActiveJob = JobProps.getActiveJob();
    if(!CS.isEmpty(oActiveJob)) {
      _fetchJobByJobId(oActiveJob, bShowLoading);
    }
  };
  /** To download Process related data **/
  let _handleProcessDataDownloadButtonClicked = function (sButtonId) {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    let iProcessInstanceId = oDataIntegrationLogsViewData.processInstanceId;
    let iInstanceIID = oDataIntegrationLogsViewData.instanceIID;

    oDataIntegrationLogsViewData.isProcessInstanceDialogOpen = false;
    if (sButtonId === "downloadExecutionStatus") {
      let oReqBody = {
        "instanceIIDs": [iInstanceIID]
      };
      CS.postRequest(getRequestMapping().GetExecutionStatus, "", oReqBody, successFetchProcessData.bind(this), failureFetchProcessData);
    }else{
      CS.getRequest(DashboardScreenRequestMapping.GetprocessInstanceFilesDownload, {id: iProcessInstanceId}, successFetchProcessData.bind(this), failureFetchProcessData);
    }
  };

  /** To hndle Process Instance Download Button Clicked **/
  let _handleProcessInstanceDownloadButtonClicked = function (iProcessInstanceId, iInstanceIID) {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    oDataIntegrationLogsViewData.isProcessInstanceDialogOpen = true;
    oDataIntegrationLogsViewData.processInstanceId = iProcessInstanceId;
    oDataIntegrationLogsViewData.instanceIID = iInstanceIID;

  };

  /** To hndle Process Instance Dialog Button Clicked **/
  let _handleProcessInstanceDialogButtonClicked = function (sButtonId) {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    oDataIntegrationLogsViewData.isProcessInstanceDialogOpen = false;
  };

  /********************************* PUBLIC API's *********************************/
  return {

    fetAllJobs: function () {
      return _fetchAllJobs();
    },

    handleJobItemClicked: function (sJobId) {
      _handleJobItemClicked(sJobId);
    },

    handleProcessGraphRefreshButtonClicked: function (bShowLoading) {
      _handleProcessGraphRefreshButtonClicked(bShowLoading);
    },

    handleProcessDataDownloadButtonClicked: function (sButtonId) {
      _handleProcessDataDownloadButtonClicked(sButtonId);
      _triggerChange();
    },

    handleProcessInstanceDownloadButtonClicked: function (iProcessInstanceId, iInstanceIID) {
      _handleProcessInstanceDownloadButtonClicked(iProcessInstanceId, iInstanceIID);
      _triggerChange();
    },

    handleProcessInstanceDialogButtonClicked: function (sButtonId) {
      _handleProcessInstanceDialogButtonClicked(sButtonId);
      _triggerChange();
    },

    resetData: function () {
      JobProps.reset();
    }
  }
})();

MicroEvent.mixin(JobStore);

export default JobStore;
