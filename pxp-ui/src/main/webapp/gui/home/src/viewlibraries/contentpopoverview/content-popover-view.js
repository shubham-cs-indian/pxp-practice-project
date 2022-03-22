import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ContentPopoverModel from './model/content-popover-view-model';

const oEvents = {
  HANDLE_POPOVER_CLICKED: "handle_popover_clicked",
  HANDLE_POPOVER_ON_HIDE: "handle_popover_on_hide"
};

const oPropTypes = {
  onContextMenuItemClick: ReactPropTypes.func.isRequired,
  action: ReactPropTypes.string.isRequired,
  model: ReactPropTypes.instanceOf(ContentPopoverModel).isRequired
};
/**
 * @class ContentPopoverModel - Not in use.
 * @memberOf Views
 * @property {func} onContextMenuItemClick
 * @property {string} action
 * @property {custom} model
 */

// @CS.SafeComponent
class ContentPopOverView extends React.Component {
  constructor(props) {
    super(props);
  }

  documentListenerCallback =(oEvent)=> {
    if(oEvent.context == "MyView" || oEvent.target.id == 'loaderContainer') {
      return;
    }
    this.closePopover();
    EventBus.dispatch(oEvents.HANDLE_POPOVER_ON_HIDE);
  }

  closePopover  =()=> {
    var aSelectedDiv = document.getElementsByClassName('selectedContext');
    if(aSelectedDiv.length > 0) {
      aSelectedDiv[0].classList.remove('selectedContext');
    }
    ReactDOM.unmountComponentAtNode(document.getElementById('contextContainer'));
    document.removeEventListener('click', this.documentListenerCallback);
  }

  componentDidUpdate =()=> {
    var oModel = this.props.model;
    if(!oModel.isVisible) {
      var oContextContainer = document.getElementById('contextContainer');
      if (oContextContainer.hasChildNodes()) {
        this.closePopover();
      }
    }
  }

  handleContextContainerOnClick =(oEvent)=> {
    oEvent.context = "MyView";
  }

  handleContextViewClicked =(oEvent)=> {
    oEvent.preventDefault();
    document.addEventListener('click', this.documentListenerCallback );
    var oContextContainerDom = document.getElementById('contextContainer');
    oContextContainerDom.addEventListener('click', this.handleContextContainerOnClick);

    var style = {};
    /*var aModelList = this.props.contextMenuViewModel;*/
    /*if(aModelList && aModelList.length > 0){*/
      /*var contextMenuHeight = (/!*aModelList.length*!/1 * 23) + oEvent.clientY ;
      var windowHeight = window.innerHeight;*/

      var contextMenuWidth = oEvent.clientX + 200;
      var windowWidth = window.innerWidth;

      style.position = 'absolute';
      style['zIndex'] = 999;
      /*style.background = '#678dbd';*/
      style.top = 48;

      /*if(contextMenuHeight > windowHeight) {
        style.top = oEvent.clientY - (/!*aModelList.length*!/1 * 25);
      } else {
        style.top = oEvent.clientY;
      }*/

      if(contextMenuWidth > windowWidth) {
        style.left = oEvent.clientX - 240;
      } else {
        style.left = oEvent.clientX;
      }

   /* }*/
    var oModel = this.props.model;
    var aSelectedDiv = document.getElementsByClassName('selectedContext');
    if(aSelectedDiv.length > 0) {
      aSelectedDiv[0].classList.remove('selectedContext');
    } else {
      oEvent.currentTarget.classList.add('selectedContext');
    }
    var oProperties = oModel.properties;
    oProperties['popoverstyle'] = style;
    EventBus.dispatch(oEvents.HANDLE_POPOVER_CLICKED, oModel);
  }

  showPopover =()=> {
    var oModel = this.props.model;
    var oPopoverStyle = oModel.properties['popoverstyle'];
    ReactDOM.render(<div style={oPopoverStyle}>{oModel.view}</div>,
        document.getElementById('contextContainer'));
  }

  render() {
    var oModel = this.props.model;
    var handleOnClick = '';
    var handleOnContextMenu = '';
    if(this.props.type == "contextmenu") {
      handleOnContextMenu = this.handleContextViewClicked;
    } else {
      handleOnClick =  this.handleContextViewClicked;
    }

    if(oModel.isVisible) {
      this.showPopover();
    }

    return (
        <div className="contentPopoverViewContainer" onClick={handleOnClick} onContextMenu={handleOnContextMenu} >
          {this.props.children}
        </div>
    );
  }
}

export const view = ContentPopOverView;
export const events = oEvents;
