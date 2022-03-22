import CS from '../../libraries/cs/index';
import React from 'react';
import ReactPropTypes from 'prop-types';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import { getTranslations } from "../../commonmodule/store/helper/translation-manager";

const oEvents = {
  BPMN_WRAPPER_VIEW_VALUE_CHANGED: "bpmn_wrapper_view_value_changed"
};

const oPropTypes = {
  xmlData: ReactPropTypes.string,
  completedActivityIds: ReactPropTypes.array,
  currentActivityIds: ReactPropTypes.array,
  failedActivityIds: ReactPropTypes.array,
  inProgressActivityIds: ReactPropTypes.array,
  notApplicableActivityIds: ReactPropTypes.array,
  showLegend: ReactPropTypes.bool
};
// @CS.SafeComponent
class BPMNWrapperView extends React.PureComponent{
  static propTypes = oPropTypes;

  constructor(){
    super();

    this.jsCanvas = React.createRef();
    this.jsDropZone = React.createRef();
  }

  componentDidMount (){

    var canvas = this.jsCanvas.current;
    //palette and properties form wont be shown in runtime hence need to override it
    let PaletteOverider = function () {
    };
    let InteractionEventsOverider = function() {
    };

    InteractionEventsOverider.$inject = [ 'eventBus', 'elementRegistry', 'styles' ];
    PaletteOverider.$inject = [];
    let oOveridingModules = {
      paletteProvider: ['type', PaletteOverider],
      interactionEvents: [ 'type', InteractionEventsOverider ],
    };

    this.modeler = new BpmnModeler(
        {
          container: canvas,
          additionalModules: [
            oOveridingModules,
          ],
        }
    );


    this.importXMLData();

    let exportArtifacts = CS.debounce(() => {
      this.handleProcessChanged();
    }, 500);

    this.modeler.on('commandStack.changed', exportArtifacts);

  }

 /* shouldComponentUpdate (oNextProps) {
    let _props = this.props;
    return (
        oNextProps.xmlData !== _props.xmlData ||
        !CS.isEqual(oNextProps.completedActivityIds, _props.completedActivityIds) ||
        !CS.isEqual(oNextProps.currentActivityIds, _props.currentActivityIds) ||
        !CS.isEqual(oNextProps.failedActiveIds, _props.failedActiveIds) ||
        !CS.isEqual(oNextProps.inProgressActivityIds, _props.inProgressActivityIds));
  }*/

  modifyAllShapes = (sFunctionToCall, __props) => {
    let oModeler = this.modeler;
    let oCanvas = oModeler.get('canvas');
    this.markUnMarkShapes(oCanvas, sFunctionToCall, __props.completedActivityIds, "highlightGreen");
    this.markUnMarkShapes(oCanvas, sFunctionToCall, __props.currentActivityIds, "highlightBlue");
    this.markUnMarkShapes(oCanvas, sFunctionToCall, __props.failedActivityIds, "highlightRed");
    this.markUnMarkShapes(oCanvas, sFunctionToCall, __props.inProgressActivityIds, "highlightYellow");
    this.markUnMarkShapes(oCanvas, sFunctionToCall, __props.notApplicableActivityIds, "highlightGrey");

  };

 /* componentWillUpdate (oNextProps) {
    if (oNextProps.xmlData !== this.props.xmlData) {
      this.importXMLFlag = true;
    } else {
      this.importXMLFlag = false;
      this.modifyAllShapes("removeMarker");
    }
  }*/

  getSnapshotBeforeUpdate(oPreProps, oPrevState) {
    let bImportXMLFlag = false;
    if (oPreProps.xmlData !== this.props.xmlData) {
      bImportXMLFlag = true;
    } else {
      bImportXMLFlag = false;
      this.modifyAllShapes("removeMarker", oPreProps);
    }

    return bImportXMLFlag;
  }

  componentDidUpdate (oPreProps, oPreState, bImportXMLFlag) {
    if (bImportXMLFlag) {
      this.importXMLData();
    } else {
      this.modifyAllShapes("addMarker", this.props);
    }
  }

  markUnMarkShapes = (oCanvas,sFunctionToCall, aIds, sClassName) => {
    CS.forEach(aIds, function (sId) {
      try {
        oCanvas[sFunctionToCall](sId, sClassName);
      } catch (e) {
        ExceptionLogger.error(`${sId} Not Found in Canvas`)
      }
    });
  };

  importXMLData = () => {
    var container = this.jsDropZone.current;
    let __props = this.props;

    var diagramXML = '<?xml version="1.0" encoding="UTF-8"?>' +
        '<bpmn:definitions xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" ' +
        'xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" ' +
        'xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" ' +
        'xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" ' +
        'targetNamespace="http://bpmn.io/schema/bpmn" ' +
        'id="Definitions_1">' +
        '<bpmn:process id="Process_1" isExecutable="false">' +
        '</bpmn:process>' +
        '<bpmndi:BPMNDiagram id="BPMNDiagram_1">' +
        '<bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">' +
        '</bpmndi:BPMNPlane>' +
        '</bpmndi:BPMNDiagram>' +
        '</bpmn:definitions>';

    let oModeler = this.modeler;


    let xmlData = this.props.xmlData || diagramXML;

    oModeler.importXML(xmlData, (err) => {

      if (err) {
        container.classList.remove('with-diagram');
        container.classList.add('with-error');
        let oErrorDOM = container.getElementsByClassName('.error pre')[0];
        oErrorDOM && (oErrorDOM.text = err.message);
        ExceptionLogger.error(err);
      } else {
        container.classList.remove('with-error');
        container.classList.add('with-diagram');
        let oCanvas = oModeler.get('canvas');
        this.modifyAllShapes("addMarker", this.props);
        oCanvas.zoom('fit-viewport');
      }

    });
  };

  downloadXML = (name, data) => {
    var encodedData = encodeURIComponent(data);


    var sHref = 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData;

    if (data) {

      var oTag = document.createElement("a");
      oTag.setAttribute("target", "_blank");
      oTag.setAttribute("href", sHref);

      document.body.appendChild(oTag);
      oTag.click();
      document.body.removeChild(oTag);
    } else {
    }
  };

  saveDiagram(fdone) {
    this.modeler.saveXML({ format: true }, function(err, xml) {
      fdone(err, xml);
    });
  }

  handleProcessChanged = () => {
      // this.saveDiagram((err, xml) => {
      //   // this.downloadXML('diagram.bpmn', err ? null : xml);
      //   EventBus.dispatch(oEvents.BPMN_WRAPPER_VIEW_VALUE_CHANGED, xml);
      // })
  };

  getLegend () {
    let oMasterTag = {
      children: [{
        color: "#ffa634",
        id: "PROCESS_STATUS_INPROGRESS",
        label: "In progress"
      },
        {
          color: "#2a672d",
          id: "PROCESS_STATUS_COMPLETED",
          label: "Completed"
        },
        {
          color: "#ff3d3c",
          id: "PROCESS_STATUS_FAILED",
          label: "Failed"
        },
        {
          color: "#7D7D7D",
          id: "PROCESS_STATUS_NOT_APPLICABLE",
          label: "N.A."
        }
      ],
      id: "PROCESS_STATUS_TAG",
      label: "Status"
    }

    let aChildrenViews = [];
    CS.forEach(oMasterTag.children, function (oChildTag) {
      let sChildColor = oChildTag.color;
      if (!sChildColor) {
        sChildColor = '#717171';
      }

      let oStyle = {
        backgroundColor: sChildColor
      };

      aChildrenViews.push(
          <div className="tagLegendContainer">
            <div className="sTagColor" style={oStyle}></div>
            <div className="sTagLabel">{CS.getLabelOrCode(oChildTag)}</div>
          </div>
      );
    });

    let oStyle = {
      backgroundColor: "#ccc"
    };

    aChildrenViews.push(
        <div className="tagLegendContainer">
          <div className="sTagColor" style={oStyle}></div>
          <div className="sTagLabel">{getTranslations().NOT_STARTED}</div>
        </div>
    );

    return (
        <div className="legendGroupContainer">
          <div className="legendLabel">{getTranslations().STATUS_LEGEND + " :"}</div>
          {aChildrenViews}
        </div>
    );
  }

  render(){
    return <div className="bpnmWrapperContainer">
      {this.props.showLegend ? this.getLegend() : null}
      <div className="content" ref={this.jsDropZone}>
        <div className="canvas" ref={this.jsCanvas}></div>
      </div>
    </div>;
  }
}


export const view = BPMNWrapperView;
export const events = oEvents;
