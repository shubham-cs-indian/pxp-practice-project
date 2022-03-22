import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import DragViewModel from './model/drag-view-model';

const oEvents = {
    DRAG_VIEW_ON_DRAG_START_EVENT: 'drag_view_on_drag_start_event',
    DRAG_VIEW_ON_DRAG_END_EVENT: 'drag_view_on_drag_end_event'
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(DragViewModel)
};
/**
 * @class DragView - use to Display product tile view.
 * @memberOf Views
 * @property {custom} [model] - model name.
 */

// @CS.SafeComponent
class DragView extends React.Component {

  constructor(props) {
    super(props);
  }

  getStringToFitInCanvas =(oCanvasContext, sCanvasString, iCanvasWidth)=> {
    var sOriginalStringWidth = oCanvasContext.measureText(sCanvasString).width;
    var sEllipsis = "..";
    var iEllipsisWidth = oCanvasContext.measureText(sEllipsis).width;
    if(sOriginalStringWidth <= iCanvasWidth) {
      return sCanvasString;
    } else {
      var iLength = sCanvasString.length;
      while(sOriginalStringWidth >= iCanvasWidth-iEllipsisWidth && iLength-- > 0) {
        sCanvasString = sCanvasString.substring(0, iLength);
        sOriginalStringWidth = oCanvasContext.measureText(sCanvasString).width;
      }
      return sCanvasString+sEllipsis;
    }
  }

  getShadowEffect =(oContext, iShadowOffsetX, iShadowOffsetY, iShadowBlur, sShadowColor)=>{
    oContext.shadowColor = sShadowColor;
    oContext.shadowBlur = iShadowBlur;
    oContext.shadowOffsetX = iShadowOffsetX;
    oContext.shadowOffsetY = iShadowOffsetY;
  }

  getArcForMultipleSelectedContent =(oContext, iArcOffsetX, iArcOffsetY, iArcRadius, iArcStartAngle, iArcEndAngle)=> {
    oContext.arc(iArcOffsetX, iArcOffsetY, iArcRadius, iArcStartAngle, iArcEndAngle, false);
    //style of arc
    oContext.fillStyle = '#0b86eb';
    oContext.fill();

    //style of text in Arc
    oContext.font = '8px';
    oContext.textAlign = "center";
    oContext.fillStyle = 'white';
  }

  handleOnDragStartEvent =(oEvent)=> {
    var oModel = this.props.model;
    var oProperties = oModel.properties || {};
    oProperties.dragContext = oModel.context;
    oProperties.dragStatus = true;
    var sContentName = CS.getLabelOrCode(oModel);
    var iNoOfSelectedContents = oProperties.noOfSelectedContents || 1;
    var oNewModel = new DragViewModel(oModel.id, CS.getLabelOrCode(oModel), oModel.isDraggable, oModel.context, oProperties);

    var oCanvas = document.createElement('canvas');
    oCanvas.style.opacity = 1;
    var oContext;

    if(oCanvas.getContext('2d')) {
      oContext = oCanvas.getContext('2d');
      oCanvas.height = '70';
      oCanvas.width = '210';

      oContext.globalCompositeOpertion = 'source-over';
      this.getShadowEffect(oContext, 0, 1, 5, "rgba(0, 0, 0, 0.8)");
      oContext.fillStyle = "#fff";
      var iRectWidth = oCanvas.width - 30;
      var iRectHeight = oCanvas.height - 40;
      var iRectOffsetX = 8;
      var iRectOffsetY = 12;
      oContext.fillRect(iRectOffsetX, iRectOffsetY, iRectWidth, iRectHeight);

      this.getShadowEffect(oContext, 0, 0, 0, "rgba(0, 0, 0, 0.8)");
      oContext.font = '14px Open Sans' || '14px sans-serif';
      oContext.fillStyle = 'black';
      var iTextOffsetX = 15;
      var iTextOffsetY = 32;
      var sDraggingContentName = this.getStringToFitInCanvas(oContext, sContentName, oCanvas.width - 40);
      oContext.fillText(sDraggingContentName, iTextOffsetX, iTextOffsetY);

      if(iNoOfSelectedContents > 1) {
        oContext.globalCompositeOperation = 'destination-over';
        var iLowerRectOffsetX = iRectOffsetX + 5;
        var iLowerRectOffsetY = iRectOffsetY + iRectHeight-2;
        var iLowerRectWidth = iRectWidth-10;
        var iLowerRectHeight = 7;
        oContext.fillStyle = "#fff";
        oContext.fillRect(iLowerRectOffsetX + 1, iLowerRectOffsetY, iLowerRectWidth - 2, iLowerRectHeight - 1);

        this.getShadowEffect(oContext, 0, 0, 1, "rgba(0, 0, 0, 0.4)");
        oContext.fillStyle = "#fff";
        oContext.fillRect(iLowerRectOffsetX, iLowerRectOffsetY, iLowerRectWidth, iLowerRectHeight);

        oContext.globalCompositeOperation = 'source-over';
        this.getShadowEffect(oContext, 0, 1, 3, "rgba(0, 0, 0, 0.4)");
        this.getArcForMultipleSelectedContent(oContext, iRectOffsetX+iRectWidth, 12, 12, 0, 2 * Math.PI);
        oContext.fillText(iNoOfSelectedContents, 188, 17);
      }

      var img = new Image();
      img.src = oCanvas.toDataURL();
      oEvent.dataTransfer.setDragImage(img, 5, 10);
    }

    //changed from "model" to "text" for IE, IE only supports "text"
    oEvent.dataTransfer.setData("Text", JSON.stringify(oNewModel));

    EventBus.dispatch(oEvents.DRAG_VIEW_ON_DRAG_START_EVENT, this, oNewModel);
  }

  handleOnDragEndEvent =(oEvent)=> {
    var oModel = this.props.model;
    var oProperties = {};
    oProperties.dragContext = oModel.context;
    oProperties.dragStatus = false;
    var oNewModel = new DragViewModel(oModel.id, CS.getLabelOrCode(oModel), oModel.isDraggable, oModel.context, oProperties);

    EventBus.dispatch(oEvents.DRAG_VIEW_ON_DRAG_END_EVENT, this, oNewModel);
  }

  render() {

    var oModel = this.props.model;
    var sClassName = oModel.properties['className'];
    var sContainerClassName = sClassName ? ("dragViewContainer " + sClassName) : "dragViewContainer";
    var oDraggableElement = (<div className={sContainerClassName}>{this.props.children}</div>);

    if(oModel.isDraggable) {
      oDraggableElement = (<div className={sContainerClassName}
                                    draggable="true"
                                    onDragStart={this.handleOnDragStartEvent}
                                    onDragEnd={this.handleOnDragEndEvent}>{this.props.children}</div>);
    }

    return (oDraggableElement);
  }
}

DragView.propTypes = oPropTypes;

export const view = DragView;
export const events = oEvents;
