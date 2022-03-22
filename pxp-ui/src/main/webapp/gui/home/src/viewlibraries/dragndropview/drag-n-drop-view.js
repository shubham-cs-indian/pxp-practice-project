
import CS from '../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import DragViewModel from './model/drag-view-model';
import DropViewModel from './model/drop-view-model';
import DragNDropViewModel from './model/drag-n-drop-view-model';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

const oEvents = {
  DRAG_N_DROP_VIEW_ON_DRAG_START_EVENT: "drag_n_drop_view_on_drag_start_event",
  DRAG_N_DROP_VIEW_ON_DRAG_END_EVENT: "drag_n_drop_view_on_drag_end_event",
  DRAG_N_DROP_VIEW_ON_DROP_EVENT: "drag_n_drop_view_on_drop_event",
  DRAG_N_DROP_VIEW_ON_DRAG_ENTER_EVENT: "drag_n_drop_view_on_drag_enter_event"
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(DragNDropViewModel),
  onDragEnter: ReactPropTypes.func,
  onDragEnd: ReactPropTypes.func
};
/**
 * @class DragNDropView
 * @memberOf Views
 * @property {custom} [model] - Storing information about item which is used for drag and drop.
 * @property {func} [onDragEnter] - Executes when a dragged element or text selection enters a valid drop target.
 * @property {func} [onDragEnd] - Executes when a drag operation is being ended (for example, by releasing a mouse button or hitting the escape key).
 */

var oCurrentDraggingItem  = null;

// @CS.SafeComponent
class DragNDropView extends React.Component {

  constructor (props) {
    super(props);

    this.dragNDropViewContainer = React.createRef();
  }

  getStringToFitInCanvas = (oCanvasContext, sCanvasString, iCanvasWidth) => {
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

  handleOnDragStartEvent =(oEvent)=> {
    var oModel = this.props.model;
    var oProperties = oModel.properties || {};
    oProperties.dragContext = oModel.context;
    oProperties.dragStatus = true;
    var sContentName = CS.getLabelOrCode(oModel);
    var oNewModel = new DragViewModel(oModel.id, sContentName, oModel.isDraggable, oModel.context, oProperties);
    oCurrentDraggingItem = oNewModel;

    var oCanvas = document.createElement('canvas');
    var oContext;
    if(oCanvas.getContext('2d')) {
      oContext = oCanvas.getContext('2d');
      oCanvas.height = '50';
      oCanvas.width = '200';

      oContext.shadowColor = "rgba(0, 0, 0, 0.5)";
      oContext.shadowBlur = 6;
      oContext.shadowOffsetX = 0;
      oContext.shadowOffsetY = 1;
      oContext.fillStyle = "#fff";
      var iRectWidth = oCanvas.width - 20;
      var iRectHeight = oCanvas.height - 20;
      var iRectOffsetX = 8;
      var iRectOffsetY = 12;
      oContext.fillRect(iRectOffsetX, iRectOffsetY, iRectWidth, iRectHeight);

      oContext.shadowBlur = 0;
      oContext.shadowOffsetX = 0;
      oContext.shadowOffsetY = 0;
      oContext.font = '14px Open Sans' || '14px sans-serif';
      oContext.fillStyle = 'black';
      var iTextOffsetX = 20;
      var iTextOffsetY = 32;
      var sDraggingContentName = this.getStringToFitInCanvas(oContext, sContentName, oCanvas.width - 35);
      oContext.fillText(sDraggingContentName, iTextOffsetX, iTextOffsetY);

      var img = new Image();
      img.src = oCanvas.toDataURL();
      oEvent.dataTransfer.setDragImage(img, 5, 20);
    }

    //changed from "model" to "text" for IE, IE only supports "text"
    oEvent.dataTransfer.setData("Text", JSON.stringify(oNewModel));

    EventBus.dispatch(oEvents.DRAG_N_DROP_VIEW_ON_DRAG_START_EVENT, this, oNewModel);
  }

  handleOnDragEndEvent =(oEvent)=> {
    var oModel = this.props.model;
    var oProperties = {};
    oProperties.dragContext = oModel.context;
    oProperties.dragStatus = false;
    var oNewModel = new DragViewModel(oModel.id, CS.getLabelOrCode(oModel), oModel.isDraggable, oModel.context, oProperties);
    oCurrentDraggingItem = null;

    if(this.props.onDragEnd) {
      try {
        this.props.onDragEnd(oNewModel);
      } catch(oException) {
        ExceptionLogger.error(oException);
      }
    }

    EventBus.dispatch(oEvents.DRAG_N_DROP_VIEW_ON_DRAG_END_EVENT, this, oNewModel);
  }

  handleOnDragOverEvent =(oEvent)=> {
    var oModel = this.props.model;

    if (oEvent.preventDefault) {
      oEvent.preventDefault(); // Necessary. Allows us to drop.
    }

    if(oModel.properties['dragStatus']){
      var oDropViewContainerDom = this.dragNDropViewContainer.current;
      oDropViewContainerDom.classList.add('hoverActive');
    }
  }

  handleDragEnterEvent =(oEvent)=> {
    var oDropModel = this.props.model;
    if(oCurrentDraggingItem) {
      var sDraggedContext = oCurrentDraggingItem.context || "";
      var aValidItems = oDropModel.validItems;
      if(CS.includes(aValidItems, sDraggedContext)) {
        if(oDropModel.id != oCurrentDraggingItem.id) {
          if(this.props.onDragEnter) {
            try {
              this.props.onDragEnter(oDropModel, oCurrentDraggingItem);
            } catch(oException) {
              ExceptionLogger.error(oException);
            }
          }
          EventBus.dispatch(oEvents.DRAG_N_DROP_VIEW_ON_DRAG_ENTER_EVENT, oDropModel, oCurrentDraggingItem);
        }
      }
    }
  }

  handleOnDropEvent =(oEvent)=> {
    var oModel = this.props.model;
    oEvent.stopPropagation();
    if(oEvent.preventDefault){
      oEvent.preventDefault();
    }

    //changed from "model" to "text" for IE, IE only supports "text"
    var sTransferredData =  oEvent.dataTransfer.getData("Text");

    try {
      sTransferredData = sTransferredData || '{}';
      var oDraggedData = JSON.parse(sTransferredData);
      var sDraggedContext = oDraggedData.context;
      var aValidItems = oModel.validItems;

      if(CS.includes(aValidItems, sDraggedContext)) {
        var oProperties = {};
        oProperties.dragStatus = false;
        oProperties.draggedItem = oDraggedData;
        oProperties.sectionId = oModel.properties['sectionId'];
        var oNewModel = new DropViewModel(oModel.id, oModel.isDroppable, oModel.validItems, oProperties);
        oCurrentDraggingItem = null;
        EventBus.dispatch(oEvents.DRAG_N_DROP_VIEW_ON_DROP_EVENT, this, oNewModel);
      }

    } catch (oError) {
      ExceptionLogger.error(oError);
      /*var oErrorObject = {
       required: 'Object',
       found: typeof sTransferredData,
       error: "Invalid dragged Data",
       methodName: "handleOnDropEvent",
       draggedData: sTransferredData
       };
       ExceptionLogger.log(oErrorObject);
       ExceptionLogger.log(oError);*/
    }


  }

  handleOnDragLeaveEvent =()=> {
    var oDropViewContainerDom = this.dragNDropViewContainer.current;
    oDropViewContainerDom.classList.remove('hoverActive');
  }

  render() {
    var _this = this;
    var oModel = _this.props.model;
    var oStyle = oModel.properties['style'];
    if(CS.isEmpty(oStyle)) {
      oStyle = {};
    }
    var oDragNDropElement = (<div className="dragNDropViewContainer" style={oStyle}>{this.props.children}</div>);
    var sClassName = oModel.properties['className'];
    var sContainerClassName = sClassName ? ("dragNDropViewContainer " + sClassName + " ") : "dragNDropViewContainer ";
    sContainerClassName += bIsDraggable ? " draggableContainer "  : "droppableContainer ";
    var sVisibility = oModel.properties['dragStatus'] ? 'dragHereVisible':'';

    var bIsDraggable = oModel.isDraggable;
    var fOnDragStart = bIsDraggable ? this.handleOnDragStartEvent : CS.noop;
    var fOnDragEnd = bIsDraggable ? this.handleOnDragEndEvent : CS.noop;

    var bIsDroppable = oModel.isDroppable;
    var fOnDragOver = bIsDroppable ? this.handleOnDragOverEvent : CS.noop;
    var fOnDragEnter = bIsDroppable ? CS.debounce(_this.handleDragEnterEvent, 200, {leading: true}) : CS.noop;
    var fOnDragLeave = bIsDroppable ? this.handleOnDragLeaveEvent : CS.noop;
    var fOnDrop = bIsDroppable ? this.handleOnDropEvent : CS.noop;


    if(bIsDraggable || bIsDroppable) {
      oDragNDropElement = (
          <div className={sContainerClassName + sVisibility}
               ref={this.dragNDropViewContainer}
               draggable="true"
               data-valid-items={JSON.stringify(oModel.validItems)}

               onDragStart={fOnDragStart}
               onDragEnd={fOnDragEnd}

               onDragOver={fOnDragOver}
               onDragEnter={fOnDragEnter}
               onDragLeave={fOnDragLeave}
               onDrop={fOnDrop}
          >
            {this.props.children}
            <div className="dragHere"></div>
          </div>
      );
    }

    return (oDragNDropElement);

  }
}

DragNDropView.propTypes = oPropTypes;

export const view = DragNDropView;
export const events = oEvents;
