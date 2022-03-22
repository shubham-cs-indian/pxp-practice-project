import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import propertiesPanelModule from 'bpmn-js-properties-panel';
import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import magicModdleDescriptor from './libraries/bpmn/magic.json';
import customReplaceMenuProvider from './libraries/bpmn/custom-replace-menu-provider';
import { customDiagramReplaceProvider } from './libraries/bpmn/custom-diagram-replace-provider';
import customZoomScrollProvider from './libraries/bpmn/custom-zoom-scroll-provider';
import { view as CustomTabsProvider } from './libraries/bpmn/custom-tabs-provider';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as ConfigUploadDialogView } from './config-upload-dialog-view';
import { view as CustomMaterialButtonView } from './../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import SettingUtils from "../store/helper/setting-utils";
import alertify from "../../../../../commonmodule/store/custom-alertify-store";

const oEvents = {
  CONFIG_BPMN_WRAPPER_VIEW_VALUE_CHANGED: "config_bpmn_wrapper_view_value_changed",
  CONFIG_BPMN_WRAPPER_VIEW_SET_DEFINITIONS: "config_bpmn_wrapper_view_set_definitions",
  CONFIG_BPMN_WRAPPER_VIEW_CHANGE_UPLOAD_DIALOG_STATUS: "config_bpmn_wrapper_view_upload_button_clicked",
  CONFIG_BPMN_WRAPPER_VIEW_HANDLE_XML_UPLOAD: "config_bpmn_wrapper_view_handle_xml_upload",
  CONFIG_BPMN_WRAPPER_VIEW_ACTION_BUTTON_HANDLER: "config_bpmn_wrapper_view_action_button_handler"
};

const oPropTypes = {
  xmlData: ReactPropTypes.string,
  activeProcess: ReactPropTypes.object,
  customPropertiesData: ReactPropTypes.object,
  isUploadWindowActive: ReactPropTypes.bool,
  shouldImportBPMNXML: ReactPropTypes.bool,
  isFullScreenMode:ReactPropTypes.bool,
  taxonomyData:ReactPropTypes.object,
  validationInfo:ReactPropTypes.object
};

// @CS.SafeComponent
class BPNMWrapperView extends React.Component {
  static propTypes = oPropTypes;

  constructor () {
    super();
    this.generateId = 'bpmnContainer' + Date.now();
    this.jsCanvas = React.createRef();
    this.jsDropZone = React.createRef();
    this.jsPropertiesPanel = React.createRef();
  }

  componentDidMount () {
    let __props = this.props;
    CustomTabsProvider.setBPMNPropertiesViewData(__props.activeProcess.configDetails, __props.customPropertiesData, __props.activeProcess, __props.taxonomyData, __props.validationInfo);

    var canvas = this.jsCanvas.current; // eslint-disable-line

    /** Modeler is getting preserved in store also for property value changes
     * (Without context !!!!!!!!!!) so, the instance in store will get replaced as soon as another instance is
     * initialised using this view
     **/
    this.modeler = new BpmnModeler(
        {
          container: canvas,
          propertiesPanel: {
            parent: '#js-properties-panel'
          },
          additionalModules: [
            propertiesPanelModule,
            CustomTabsProvider.exportingObject,
            customReplaceMenuProvider(),
            customDiagramReplaceProvider(),
            customZoomScrollProvider
          ],
          moddleExtensions: {
            camunda: camundaModdleDescriptor,
            magic: magicModdleDescriptor
          }
        }
    );

    this.importXMLData();

    let exportArtifacts = () => {
      this.handleProcessChanged();
    };

    this.modeler.on('commandStack.changed', exportArtifacts);

  }

  componentDidUpdate () {
    let __props = this.props;
    CustomTabsProvider.setBPMNPropertiesViewData(__props.activeProcess.configDetails, __props.customPropertiesData,__props.activeProcess, __props.taxonomyData, __props.validationInfo);
    __props.shouldImportBPMNXML && this.importXMLData();
  }

  importXMLData = () => {
    var container = this.jsDropZone.current; // eslint-disable-line
    let xmlData = this.props.xmlData;
    let oModeler = this.modeler;
    let _this = this;
    oModeler.importXML(xmlData, function (err) {
      if (err) {
        container.classList.remove('with-diagram');
        container.classList.add('with-error');
        container.querySelector('.error pre').textContent = err.message;
        // container.find('.error pre').text(err.message);
        ExceptionLogger.error(err);
      } else {
        container.classList.remove('with-error');
        container.classList.add('with-diagram');
        let oCanvas = oModeler.get('canvas');
        _this.validateWorkflow(oCanvas, oModeler, _this.props.validationInfo);
        oCanvas.zoom('fit-viewport');
        oModeler && EventBus.dispatch(oEvents.CONFIG_BPMN_WRAPPER_VIEW_SET_DEFINITIONS, oModeler);
      }
    });
  };

  /** Function to Validate & Mark Invalid Task in a Workflow as Red **/
  validateWorkflow = (oCanvas, oModeler, oValidationInfo) => {
    if (CS.isNotEmpty(oValidationInfo) && !oValidationInfo.isWorkflowValid) {
      if (CS.isNotEmpty(oValidationInfo.validationDetails)) {
        var oRootElement = oCanvas.getRootElement();
        var aBusinessObject = oRootElement.businessObject;
        var aFlowElements = aBusinessObject.flowElements;
        var oInvalidElements = oValidationInfo.validationDetails;
        var aInvalidTaskIds = [];
        CS.forEach(aFlowElements, function (oElement) {
          if ((oElement.$type == "bpmn:ServiceTask" || oElement.$type == "bpmn:UserTask") && !CS.isEmpty(SettingUtils.getBPMNCustomElementIDFromBusinessObject(oElement))) {
            {
              if (oInvalidElements && oInvalidElements.hasOwnProperty(oElement.id)) {
                aInvalidTaskIds.push(oElement.id);
              }
            }
          }
        });
        this.markUnMarkShapes(oCanvas, "addMarker", aInvalidTaskIds, 'highlightTask');
      }
      else {
        alertify.error("No or Multiple BPMN Processes/Pools found in the Workflow");
      }
    }
  };


    downloadXML = () => {
    let _props = this.props;
    let sXMLData = _props.xmlData;
    let oActiveProcess = _props.activeProcess;
    oActiveProcess = oActiveProcess.clonedObject || oActiveProcess;
    var encodedData = encodeURIComponent(sXMLData);
    var sHref = 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData;

    if (sXMLData) {
      var oTag = document.createElement("a");
      oTag.setAttribute("target", "_blank");
      oTag.setAttribute("href", sHref);
      oTag.download = `${oActiveProcess.label}.bpmn`;
      document.body.appendChild(oTag);
      oTag.click();
      document.body.removeChild(oTag);
    } else {
    }
  };

  saveDiagram (fdone) {
    this.modeler.saveXML({format: true}, function (err, xml) {
      fdone(err, xml);
    });
  }

  handleProcessChanged = () => {
    this.saveDiagram((err, xml) => {
      EventBus.dispatch(oEvents.CONFIG_BPMN_WRAPPER_VIEW_VALUE_CHANGED, xml);
    })

  };

  uploadXMLButtonClicked = () => {
    EventBus.dispatch(oEvents.CONFIG_BPMN_WRAPPER_VIEW_CHANGE_UPLOAD_DIALOG_STATUS, true);
  };

  handleXMLUpload = (aFiles) => {
    try {
      let oFile = aFiles[0];
      let oFileReader = new FileReader();
      oFileReader.onload = oEvent => (EventBus.dispatch(oEvents.CONFIG_BPMN_WRAPPER_VIEW_HANDLE_XML_UPLOAD, oEvent.target.result));
      oFileReader.readAsText(oFile);
    } catch (oError) {
      ExceptionLogger.error("Invalid File Type Uploaded")
    }
  };

  cancelXMLUpload = () => {
    EventBus.dispatch(oEvents.CONFIG_BPMN_WRAPPER_VIEW_CHANGE_UPLOAD_DIALOG_STATUS, false);
  };

  getUploadDialog = () => {
    let oView = null;
    if (this.props.isUploadWindowActive) {
      oView = <ConfigUploadDialogView uploadHandler={this.handleXMLUpload} cancelHandler={this.cancelXMLUpload}
                                      acceptedFileTypes={[".bpmn"]}
                                      title={getTranslation().UPLOAD_UNSAVED_CHANGES_CONFIRMATION}/>
    }
    return oView;
  };

  getDownloadButtonDOM = () => {
    let oActiveProcess = this.props.activeProcess;
    oActiveProcess = oActiveProcess.clonedObject || oActiveProcess;
    let oDownloadButton = null;
    if (!CS.isEmpty(oActiveProcess.processDefinition)) {
      oDownloadButton = <TooltipView label={getTranslation().BPMN_MODELER_DOWNLOAD}>
        <div className="downloadBPMNButton" onClick={this.downloadXML}></div>
      </TooltipView>
    }
    return oDownloadButton;
  };

  getActionButtonDOM = () => {
    let _this = this;
    let oActiveProcess = this.props.activeProcess;
    let aButtonData = [];
    let aButtonView = [];
    let bIsProcessDirty = !!oActiveProcess.clonedObject;
    if (bIsProcessDirty) {
      aButtonData = [
        {
          id: "detailed_process_discard",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "detailed_process_save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }];
    } else {
      aButtonData = [{
        id: "detailed_process_close",
        label: getTranslation().OK,
        isFlat: false,
        isDisabled: false
      }];
    }

    if(this.props.isFullScreenMode){
      CS.forEach(aButtonData,function (oButton) {
        aButtonView.push(<CustomMaterialButtonView
            label={oButton.label}
            isRaisedButton={!oButton.isFlat}
            isDisabled={false}
            onButtonClick={_this.handleButtonClick.bind(_this, oButton.id)}/>);
      });
    }

    return (<div className="actionButtonWrapper">
              <div className="actionButtonView">{aButtonView}</div>
            </div>)
  };

  getUploadButtonDOM = () => {
    return (<TooltipView label={getTranslation().BPMN_MODELER_UPLOAD}>
      <div className="uploadBPMNButton"
           onClick={this.uploadXMLButtonClicked}></div>
    </TooltipView>);
  };

  handleButtonClick =(sButtonId)=> {
    EventBus.dispatch(oEvents.CONFIG_BPMN_WRAPPER_VIEW_ACTION_BUTTON_HANDLER, sButtonId);
  };

  /** Function to Mark passed Ids as Red **/
  markUnMarkShapes = (oCanvas, sFunctionToCall, aIds, sClassName) => {
    CS.forEach(aIds, function (sId) {
      try {
        oCanvas[sFunctionToCall](sId, sClassName);
      } catch (e) {
        ExceptionLogger.error(`${sId} Not Found in Canvas`)
      }
    });
  };

  render () {

    return <div id={this.generateId} className="bpnmWrapperContainer">
      {this.getUploadButtonDOM()}
      {this.getDownloadButtonDOM()}
      {this.getUploadDialog()}
      <div className="content" id="js-drop-zone" ref={this.jsDropZone}>
        <div className="canvas" id="js-canvas" ref={this.jsCanvas}></div>
        <div className="properties" id="js-properties-panel" ref={this.jsPropertiesPanel}></div>
      </div>
      {this.getActionButtonDOM()}
    </div>;
  }
}


export const view = BPNMWrapperView;
export const events = oEvents;
