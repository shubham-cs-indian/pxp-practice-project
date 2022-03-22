import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../tooltipview/tooltip-view';

const oEvents = {
  ENTITY_SNACK_BAR_STOP_SHAKING: "entity_snack_bar_stop_shaking"
};
const oPropTypes = {
  message: ReactPropTypes.string,
  open: ReactPropTypes.bool,
  onSave: ReactPropTypes.func,
  onDiscard: ReactPropTypes.func,
  onComment: ReactPropTypes.func,
  shake: ReactPropTypes.bool,
  saveButtonLabel: ReactPropTypes.string,
  onCompleteButtonClick: ReactPropTypes.func
};
/**
 * @class EntitySnackBarView
 * @memberOf Views
 * @property {string} [message]
 * @property {bool} [open]
 * @property {func} [onSave]
 * @property {func} [onDiscard]
 * @property {func} [onComment]
 * @property {bool} [shake]
 * @property {string} [saveButtonLabel]
 * @property {func} [onCompleteButtonClick]
 */

// @CS.SafeComponent
class EntitySnackBarView extends React.Component {

  constructor(props) {
    super(props);

    this.commentBox = React.createRef();
    this.contentSnackBarWrapper = React.createRef();
  }

  componentDidUpdate(){
    var oCommonBoxDom = this.commentBox.current;
    var oContentSnackBarWrapper = this.contentSnackBarWrapper.current;
    if(!CS.isEmpty(oCommonBoxDom) && !this.props.open) {
      oCommonBoxDom.value = "";
    }
    if(!CS.isEmpty(oContentSnackBarWrapper) && this.props.open && this.props.shake) {
      // this.shakeEffect(oContentSnackBarWrapper);
      EventBus.dispatch(oEvents.ENTITY_SNACK_BAR_STOP_SHAKING);
    }
  }

/*  TODO: Remove shaking feature as it is no more used
    shakeEffect =(oDemo)=> {
    CS.times (3, function () {
      $(oDemo).animate({left: -25}, 10).animate({left: 0}, 50).animate({left: 25}, 10).animate({left: 0}, 50);
    });
  }*/

  render() {
    var __props = this.props;
    var oStyle = {};
    if(__props.open) {
      oStyle.bottom = "0";
    } else {
      oStyle.bottom = "-40px";
    }

    var sSaveButtonLabel = getTranslation().SAVE;
    var sSaveClassName = "handleSave snackBarButton ";
    if(this.props.saveButtonLabel){
      sSaveButtonLabel = this.props.saveButtonLabel;
      sSaveClassName += "customLabel";
    }

    var oDiscardButton = null;
    if(CS.isFunction(__props.onDiscard)) {
      oDiscardButton = (
          <TooltipView placement="top" label={"CTRL + D"}>
            <div className="handleDiscard snackBarButton"
                 onClick={__props.onDiscard}>{getTranslation().DISCARD}</div>
          </TooltipView>
      );

    }

    let oCompleteButton = null;
    if (CS.isFunction(__props.onCompleteButtonClick)) {
      oCompleteButton = (
          <TooltipView placement="top">
            <div className="handleDiscard snackBarButton"
                 onClick={__props.onCompleteButtonClick}>{getTranslation().COMPLETE}</div>
          </TooltipView>
      );
    }

    return (
      <div className="contentSnackBarContainer" style={oStyle}>
        <div className="contentSnackBarWrapper" ref={this.contentSnackBarWrapper}>
          {/*<div className="message">{__props.message}</div>*/}
          <div className="commentWrapper">
            <div className="commentBox">
              <input type="text" ref={this.commentBox} onBlur={__props.onComment} placeholder={getTranslation().PLEASE_ENTER_SAVE_COMMENT + "..."}/>
            </div>
            <div className="snackBarButtons">
              {oDiscardButton}
              {oCompleteButton}
            <TooltipView placement="top" label={"CTRL + S"}>
              <div className={sSaveClassName} onClick={__props.onSave}>{sSaveButtonLabel}</div>
            </TooltipView>
            </div>
          </div>
        </div>
      </div>
    );
  }

}

EntitySnackBarView.propTypes = oPropTypes;

export const view = EntitySnackBarView;
export const events = oEvents;
