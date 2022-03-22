import CS from '../../../../../../../libraries/cs';
import React from 'react';
import EventBus from '../../../../../../../libraries/eventdispatcher/EventDispatcher';
import {
  getTranslations as getTranslation,
  getTranslations
} from '../../../../../../../commonmodule/store/helper/translation-manager';
import {view as CustomDialogView} from '../../../../../../../viewlibraries/customdialogview/custom-dialog-view';
import ReactPropTypes from "prop-types";
import {view as ButtonView} from "../../../../../../../viewlibraries/buttonview/button-view";
import {view as CustomMaterialButtonView} from '../../../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import {view as RuntimeBPNMWrapperView} from "../../../../../../../viewlibraries/bpmnwrapperview/runtime-bpmn-wrapper-view";
import RequestMapping from "../../../../../../../libraries/requestmappingparser/request-mapping-parser"
import DashboardScreenRequestMapping from "../tack/dashboard-screen-request-mapping";

let Events = {
  BACKGROUND_PROCESS_DIALOG_VIEW_BUTTON_CLICKED: 'background_process_dialog_view_button_clicked'
};

const oPropTypes = {
  backgroundProcessDialogData: ReactPropTypes.object
};

// @CS.SafeComponent
class BackgroundProcessDialogView extends React.Component {
  static propTypes = oPropTypes;

  handleButtonClicked = function (sId) {
    EventBus.dispatch(Events.BACKGROUND_PROCESS_DIALOG_VIEW_BUTTON_CLICKED, sId)
  };

  getBackgroundProcessDialogView = function () {
    let oData = this.props.backgroundProcessDialogData;
    let aButtonData = [{
      id: "ok",
      label: getTranslation().OK,
      isDisabled: false,
      isFlat: false,
    }];

    let aUpperSectionButtonData = [
      {
        id: 'downloadLogs',
        label: getTranslations().DOWNLOAD_LOGS,
        downloadLink: RequestMapping.getRequestUrl(DashboardScreenRequestMapping.downloadLogs + oData.jobId)
      }]

    if (oData.service === "PXON_EXPORT") {
      aUpperSectionButtonData.push({
        id: 'downloadPXON',
        label: getTranslations().DOWNLOAD_PXON,
        downloadLink: RequestMapping.getRequestUrl(DashboardScreenRequestMapping.downloadpxon + oData.jobId)
      })
    }

    let aButtonDataDoms = [<ButtonView id={'refresh'}
                                       showLabel={false}
                                       tooltip={getTranslations().REFRESH}
                                       isDisabled={false}
                                       className={"toolRefresh"}
                                       type={"simple"}
                                       onChange={this.handleButtonClicked.bind(this, 'refresh')}
                                       theme={"light"}/>];
    let _this = this;
    CS.forEach(aUpperSectionButtonData, function (oUpperSectionButtonData) {
      aButtonDataDoms.push(<a href={oUpperSectionButtonData.downloadLink}
                              target="_blank">
        <CustomMaterialButtonView
            label={oUpperSectionButtonData.label}
            isRaisedButton={true}
            isDisabled={false}/>
      </a>)
    });

    let oContentStyle = {
      //height: '70%',
      maxWidth: 'none',
      width: '60%'
    };

    let aLogData = oData.logData.split("\n");

    let aLogs = [];

    CS.forEach(aLogData, function (oLog) {
      let sClassName;
      if (CS.startsWith(oLog, "INFO")) {
        sClassName = "infoLog"
      } else if (CS.startsWith(oLog, "PROGRESS")) {
        sClassName = "progressLog"
      } else if (CS.startsWith(oLog, "SUCCESS")) {
        sClassName = "successLog"
      } else if (CS.startsWith(oLog, "ERROR")) {
        sClassName = "errorLog"
      }
      aLogs.push(<div className={sClassName}>{oLog}</div>)
    })

    let oLogData = <div className={"logDataWrapper"}>
      {aLogs}
    </div>;

    let aCompletedActivityIds = [];
    let aCurrentActivityIds = [];
    let aFailedActiveIds = [];
    let aInProgressActivityIds = [];
    let aNotApplicableActivityIds = [];

    switch (oData.status) {
      case "UNDEFINED":
        break;

      case "PENDING":
      case "RUNNING":
      case "PAUSED":
        aInProgressActivityIds.push("customId");
        break;

      case "ENDED_SUCCESS":
        aCompletedActivityIds.push("customId");
        break;

      case"ENDED_EXCEPTION":
      case"ENDED_ERRORS":
      case "CANCELED":
      case"INTERRUPTED":
        aFailedActiveIds.push("customId");
        break;
    }

    return (<CustomDialogView
        open={true}
        className="backgroundProcessDialog"
        contentClassName="backgroundProcessDialogContent"
        bodyClassName="backgroundProcessDialogBody"
        buttonData={aButtonData}
        title={getTranslations().JOB_DETAILS}
        contentStyle={oContentStyle}
        onRequestClose={_this.handleButtonClicked.bind(_this, "ok")}
        buttonClickHandler={_this.handleButtonClicked}
    >
      <div className={"backgroundProcessDialogViewUpperSection"}>
        {aButtonDataDoms}
      </div>
      <div className={"backgroundProcessDialogViewWrapper"}>
        <div className={"backgroundProcessTableWrapper"}>
          <div className={"leftColumn bgpServiceLabel"}>{getTranslations().BGP_SERVICE}</div>
          <div className={"rightColumn bgpService"}>{getTranslation()[oData.service]}</div>
          <div className={"leftColumn jobIdLabel"}>{getTranslations().JOB_ID}</div>
          <div className={"rightColumn jobId"}>{oData.jobId}</div>
          <div className={"leftColumn statusLabel"}>{getTranslations().STATUS}</div>
          <div className={"rightColumn status"}>{getTranslation()[oData.status]}</div>
          <div className={"leftColumn workflowStatusLabel"}>{getTranslations().WORKFLOW_STATUS}</div>
          <div className={"rightColumn workflowStatusWrapper"}>
            <RuntimeBPNMWrapperView
                xmlData={oData.processDefination}
                completedActivityIds={aCompletedActivityIds}
                currentActivityIds={aCurrentActivityIds}
                failedActivityIds={aFailedActiveIds}
                inProgressActivityIds={aInProgressActivityIds}
                notApplicableActivityIds={aNotApplicableActivityIds}
                showLegend/>
          </div>
          <div className={"leftColumn logsLabel"}>{getTranslations().LOGS}</div>
          <div className={"rightColumn logs"}>{oLogData}</div>
        </div>
      </div>
    </CustomDialogView>)
  };

  render () {

    return (
        this.getBackgroundProcessDialogView()
    )
  }
}

export const view = BackgroundProcessDialogView;
export const events = Events;
