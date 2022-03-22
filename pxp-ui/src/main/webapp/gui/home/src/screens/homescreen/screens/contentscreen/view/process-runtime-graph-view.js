import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as RuntimeBPMNWrapperView } from '../../../../../viewlibraries/bpmnwrapperview/runtime-bpmn-wrapper-view';
import { view as CustomDailogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as JobLogView } from './job-log-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;


const oEvents = {
  PROCESS_GRAPH_REFRESH_BUTTON_CLICKED: "process_graph_refresh_button_clicked"
};
const oPropTypes = {
  activeJobGraphData: ReactPropTypes.object,
  masterTag: ReactPropTypes.object,
  isInProgress: ReactPropTypes.bool,
  activeJob:ReactPropTypes.object
};

// @CS.SafeComponent
class ProcessRuntimeGraphView extends React.Component {
  static propTypes = oPropTypes;

  componentDidMount () {
    this.autoRefresh();
  }

  componentDidUpdate () {
    this.autoRefresh();
  }

  autoRefresh = () => {
    if (this.props.isInProgress) {
      setTimeout(this.processGraphRefreshButtonClicked.bind(this, false), 1000);
    }
  };

  state = {
    isCustomDialogOpened : false
  };

  getTagLegendView = () => {
    var oMasterTag = this.props.masterTag;
    var aChildrenViews = [];
    CS.forEach(oMasterTag.children, function (oChildTag) {
      var sChildColor = oChildTag.color;
      if (!sChildColor) {
        sChildColor = '#717171';
      }

      var oStyle = {
        backgroundColor: sChildColor
      };

      aChildrenViews.push(
          <div className="tagLegendContainer">
            <div className="sTagColor" style={oStyle}></div>
            <div className="sTagLabel">{CS.getLabelOrCode(oChildTag)}</div>
          </div>
      );
    });

    var oStyle = {
      backgroundColor: "#ccc"
    };

    aChildrenViews.push(
        <div className="tagLegendContainer">
          <div className="sTagColor" style={oStyle}></div>
          <div className="sTagLabel">{getTranslation().NOT_STARTED}</div>
        </div>
    );

    return (
        <div className="legendGroupContainer">
          <div className="legendLabel">{getTranslation().STATUS_LEGEND + " :"}</div>
          {aChildrenViews}
        </div>
    );
  };

  processGraphRefreshButtonClicked = (bShowLoading) => {
    EventBus.dispatch(oEvents.PROCESS_GRAPH_REFRESH_BUTTON_CLICKED, bShowLoading);
  };

  getLegendView = () => {

    return (
        <div className="legendContainer">
          {this.getTagLegendView()}
          <TooltipView placement="top" label={getTranslation().REFRESH}>
            <div className="refreshGraph" onClick={this.processGraphRefreshButtonClicked.bind(this, true)}></div>
          </TooltipView>
        </div>
    );
  };

  getRuntimeBPMNWrapperView = () => {
    return (<RuntimeBPMNWrapperView {...this.props.activeJobGraphData}/>);
  };

  disableCustomDialog = (aFailedComponents,sButtonId) => {
    if(sButtonId == "download"){
      let aErrorLogToFile = [];
     CS.forEach(aFailedComponents,(oFailedComponent)=>{
      let oTempObj = {};
      oTempObj.componentName = oFailedComponent.componentId +"_"+oFailedComponent.componentLabel;
      oTempObj.errorLogs = oFailedComponent.status.failedIds;
      oTempObj.ignoredProperties = oFailedComponent.status.ignoredProperties;
       aErrorLogToFile.push(oTempObj);
     });
      var element = document.createElement('a');
      element.setAttribute('href', 'data:application/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(aErrorLogToFile, null,2)));
      element.setAttribute('download', "ErrorLogs.json");

      element.style.display = 'none';
      document.body.appendChild(element);

      element.click();

      document.body.removeChild(element);
    }else{
      this.setState({isCustomDialogOpened:false});
    }
  }

  render () {
    let aComponents = this.props.activeJob.components;
    let aFailedComponents = [];
    CS.forEach(aComponents, function (oComponent) {
      if((!CS.isEmpty(oComponent.status)) && (oComponent.status.failedCount > 0)){
        aFailedComponents.push(oComponent);
      }
    });

    let oJobLogView = <JobLogView failedComponents={aFailedComponents}/> ;
    let  aButtonData = [
      {
        id: "download",
        label: getTranslation().DOWNLOAD,
        isDisabled: false,
        isFlat: false,
      },
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isDisabled: false,
        isFlat: false,
      }];

    let oBodyStyle = {
      "overflow" : "auto"
    };

    let oCustomDialogView = this.state.isCustomDialogOpened ? (<CustomDailogView modal={true} open={true}
                                                                                 title={getTranslation().ERROR_LOGS}
                                                                                 buttonData={aButtonData}
                                                                                 onRequestClose={this.disableCustomDialog.bind(this, aComponents, aButtonData[1].id)}
                                                                                 buttonClickHandler={this.disableCustomDialog.bind(this, aComponents)}
                                                                                 bodyStyle={oBodyStyle}>
      {oJobLogView}</CustomDailogView>) : null;

    return (
        <div className="processConfigDesignViewContainer processRuntimeGraphView">
              {this.getLegendView()}
              <div className="paper-container">
                {this.getRuntimeBPMNWrapperView()}
              </div>
          {oCustomDialogView}
            </div>
    );
  }
}

export const view = ProcessRuntimeGraphView;
export const events = oEvents;
