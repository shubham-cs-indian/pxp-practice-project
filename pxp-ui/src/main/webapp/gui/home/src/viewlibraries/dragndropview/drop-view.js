import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import alertify from '../../commonmodule/store/custom-alertify-store';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import DropViewModel from './model/drop-view-model';

const oEvents = {
  DROP_VIEW_ON_DROP_EVENT: 'drop_view_on_drop_event'
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(DropViewModel),
  onDropEventHandler: ReactPropTypes.func
};
/**
 * @class DropView - Use for drag and drop content in the Application.
 * @memberOf Views
 * @property {custom} [model] - Model name.
 */

// @CS.SafeComponent
class DropView extends React.Component {

  constructor(props) {
    super(props);

    this.dropViewContainer = React.createRef();
  }

  handleOnDragOverEvent =(oEvent)=> {
    var oModel = this.props.model;

    if (oEvent.preventDefault) {
      oEvent.preventDefault(); // Necessary. Allows us to drop.
    }

    if(oModel.properties['dragStatus']){
      var oDropViewContainerDom = this.dropViewContainer.current;
      oDropViewContainerDom.classList.add('hoverActive');
    }
  }

  handleOnDropEvent =(oEvent)=> {
    var oModel = this.props.model;
    var oDropViewContainerDom = this.dropViewContainer.current;
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
        /**@note: dropIndex can be used in scenario where we have multiple
         * drop container and catch on which drop container element has been dropped */
        oProperties.dropIndex = oModel.properties['dropIndex'];
        var oNewModel = new DropViewModel(oModel.id, oModel.isDroppable, oModel.validItems, oProperties);
        if (CS.isFunction(this.props.onDropEventHandler)) {
          /** Reset the hoverActive style effect */
          oDropViewContainerDom.classList.remove('hoverActive');
          this.props.onDropEventHandler(oNewModel);
        } else {
          EventBus.dispatch(oEvents.DROP_VIEW_ON_DROP_EVENT, this, oNewModel, this.props.filterContext);
        }

      } else {
        alertify.error(getTranslation().DROP_NOT_ALLOWED)
      }

    } catch (oError) {
      ExceptionLogger.error(oError);
      if (oModel.properties['dragStatus'] && oDropViewContainerDom.classList.contains('hoverActive')) {
        oDropViewContainerDom.classList.remove('hoverActive');
      }
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

  handleOnDragLeaveEvent =()=>{
    var oDropViewContainerDom = this.dropViewContainer.current;
    oDropViewContainerDom.classList.remove('hoverActive');
  }

  render() {

    var oModel = this.props.model;
    var oStyle = oModel.properties['style'];
    if(CS.isEmpty(oStyle)) {
      oStyle = {};
    }

    var oDroppableElement = (<div className="dropViewContainer" style={oStyle}>{this.props.children}</div>);

    if(oModel.isDroppable) {
      var sVisibility = oModel.properties['dragStatus'] ? 'dragHereVisible':'';
      oDroppableElement = (
          <div className={"dropViewContainer " + sVisibility}
               ref={this.dropViewContainer}
               style={oStyle}
               data-valid-items={JSON.stringify(oModel.validItems)}
               onDragOver={this.handleOnDragOverEvent}
               onDragLeave={this.handleOnDragLeaveEvent}
               onDrop={this.handleOnDropEvent}>
            {this.props.children}
            <div className="dragHere"></div>
          </div>
      );
    }

    return (oDroppableElement);

  }
}

DropView.propTypes = oPropTypes;

export const view = DropView;
export const events = oEvents;
