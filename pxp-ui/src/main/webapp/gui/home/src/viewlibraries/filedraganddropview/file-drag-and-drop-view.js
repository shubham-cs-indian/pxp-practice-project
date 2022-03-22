import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import MIMETypeMap from '../tack/mime-type-dictionary.js';
import ViewUtils from '../utils/view-library-utils';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

const oEvents = {
  FILE_DRAG_DROP_HANDLE_FILE_DROP: "file_drag_drop_handle_file_drop",
  FILE_DRAG_DROP_DRAGGED_HANDLER: "file_drag_drop_is_dragged_true",
};

const oPropTypes = {
  id: ReactPropTypes.string.isRequired,
  context: ReactPropTypes.string,
  allowedFileTypes: ReactPropTypes.arrayOf(ReactPropTypes.string),
  extraData: ReactPropTypes.object,
  isDragging: ReactPropTypes.bool
};

/**
 * @class FileDragAndDropView
 * @memberOf Views
 * @property {string} id - Id
 * @property {string} [context] - Describe the screen or which operation will be performed(for example: endpoints screen).
 * @property {array} [allowedFileTypes] - Contains allowed file types.
 * @property {object} [extraData] - Contains extraData ex. endpoint id, endpoint type etc.
 */

// @CS.SafeComponent
class FileDragAndDropView extends React.Component {

  constructor (props) {
    super(props);

    let aAllowedMIMETypes = [];
    CS.forEach(props.allowedFileTypes, function (sType) {
      aAllowedMIMETypes.push(MIMETypeMap[sType]);
    });

    this.allowedMIMETypes = aAllowedMIMETypes;
    this.counter = 0;

    this.setRef = (sRef, element) => {
      this[sRef] = element;
    };
  }

  /** 'items' key is available only in chrome, not firefox. This function basically checks the browser */
  checkEvent = (oEvent) => {
    return !CS.isEmpty(oEvent.dataTransfer.items);
  };

  /** This function checks if there is exactly one file and its type matches the allowed types */
  validateFile = (aItems, aFiles) => {
    let sContext = this.props.context;
    switch (sContext) {
      case "dashboardEndpoint" :
        return CS.size(aItems) === 1 && aFiles && CS.includes(this.allowedMIMETypes, aFiles[0].type);
        break;
      case "relationshipSectionDrop" :
      case "dashboardBulkUpload" :
        return true;
        break;
      default:
        let bIsAllowedType = false;
        CS.forEach(aFiles, (oFile) => {
          if (CS.includes(this.allowedMIMETypes, oFile.type)) {
            bIsAllowedType = true;
          } else {
            bIsAllowedType = false;
            return false;
          }
        });

        return bIsAllowedType;
    }
  };

  /** This function validates the file and dispatches the event */
  dispatchEvent = (aItems, aFiles) => {
    if (this.validateFile(aItems, aFiles)) {
      EventBus.dispatch(oEvents.FILE_DRAG_DROP_HANDLE_FILE_DROP, this.props.context, aFiles, this.props.extraData);
    }
  };

  onDropHandler = (oEvent) => {
    try {
      oEvent.preventDefault();
      if (this.props.context == "dashboardEndpoint") {
        if (this.checkEvent(oEvent)) {
          let aItems = oEvent.dataTransfer.items;
          let oFile = aItems[0].getAsFile();
          this.dispatchEvent(aItems, [oFile]);
        } else {
          let aFiles = oEvent.dataTransfer.files;
          let oFile = aFiles[0];
          this.dispatchEvent(aFiles, [oFile]);
        }
      } else {
        let aFiles = oEvent.dataTransfer.files;
        if (this.props.context == "iconLibrarySectionDrop") {
          CS.forEach(aFiles, function (oFile) {
            oFile.src = URL.createObjectURL(oFile);
          });
        }
        this.dispatchEvent(aFiles, aFiles);
      }
      this.counter = 0;
      EventBus.dispatch(oEvents.FILE_DRAG_DROP_DRAGGED_HANDLER);
      this[this.props.id].classList.remove('border');
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }

  };

  onDragEnterHandler = (oEvent) => {
    if (!ViewUtils.isFirefox()) {
      this.counter++;
    }
    EventBus.dispatch(oEvents.FILE_DRAG_DROP_DRAGGED_HANDLER);
    this[this.props.id].classList.add('border');
  };

  onDragLeaveHandler = (oEvent) => {
    if (!ViewUtils.isFirefox()) {
      this.counter--;
    }
    if (this.counter === 0) {
      EventBus.dispatch(oEvents.FILE_DRAG_DROP_DRAGGED_HANDLER);
      this[this.props.id].classList.remove('border');
    }
  };

  onDragOverHandler = (oEvent) => {
    oEvent.preventDefault();
    if (!this.checkEvent(oEvent)) {
      return;
    }
    let aItems = oEvent.dataTransfer.items;
    if (this.validateFile(aItems, aItems)) {
      oEvent.dataTransfer.dropEffect = "link";
    } else {
      oEvent.dataTransfer.dropEffect = "none";
    }
  };

  getTextDom = () => {
    if (this.props.isDragging) {
      return (<div className="customDropMessage"> {this.props.customMessage} </div>);
    }
    return null;
  };

  render () {
    let oTextDom = this.props.customMessage && this.getTextDom();
    let bIsDragging = this.props.isDragging;
    let sFileDragDropViewContainerWrapperClassName = "fileDragDropViewContainerWrapper ";
    bIsDragging ? sFileDragDropViewContainerWrapperClassName += "isDragging" : null; // eslint-disable-line

    return (
        <div className={sFileDragDropViewContainerWrapperClassName}>
          <div className="fileDragDropViewContainer"
               ref={this.setRef.bind(this, this.props.id)}
               onDrop={this.onDropHandler}
               onDragOver={this.onDragOverHandler}
               onDragEnter={this.onDragEnterHandler}
               onDragLeave={this.onDragLeaveHandler}>
            {this.props.children}
          </div>
          {oTextDom}
        </div>
    );
  }
}

FileDragAndDropView.propTypes = oPropTypes;

export const view = FileDragAndDropView;
export const events = oEvents;
