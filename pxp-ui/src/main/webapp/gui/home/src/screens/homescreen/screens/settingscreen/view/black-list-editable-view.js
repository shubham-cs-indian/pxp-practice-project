
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';

const oEvents = {
  ADD_NEW_ITEM: "add_new_item",
  REMOVE_ITEM: "remove_item",
  EDIT_ITEM: "edit_item"
};

const oPropTypes = {
  list: ReactPropTypes.array,
  label: ReactPropTypes.string,
  context: ReactPropTypes.string,
  isDisabled: ReactPropTypes.bool
};

// @CS.SafeComponent
class BlackListEditableView extends React.Component {
  constructor (props){
    super(props);

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };
  }
  static propTypes = oPropTypes;

  componentDidMount() {
    this.updateFields();
  }

  componentDidUpdate() {
    this.updateFields();
  }

  updateFields = () => {
    var _this = this;
    var bIsDisabled = _this.props.isDisabled;
    if(!bIsDisabled){
      _this["mainInput"].value = "";
      CS.forEach(this.props.list, function (sItem, iIndex) {
        _this["itemInput_" + iIndex].value = sItem;
      });
    }
  };

  handleItemBlur = (iIndex, oEvent) => {
    var sValue = oEvent.target.value;
    EventBus.dispatch(oEvents.EDIT_ITEM, null, iIndex, sValue, this.props.context);
  };

  handleRemoveItem = (iIndex, oEvent) => {
    EventBus.dispatch(oEvents.REMOVE_ITEM, null, iIndex, this.props.context);
  };

  getListView = () => {
    var _this = this;
    var __props = _this.props;
    var bIsDisabled = !!__props.isDisabled;
    return CS.map(__props.list, function (sItem, iIndex) {
      var oInputDOM = (
          <TooltipView placement="bottom" label={sItem}>
            <input
                className="itemInput"
                ref={_this.setRef.bind(_this,"itemInput_" + iIndex)}
                type="text"
                onBlur={_this.handleItemBlur.bind(_this, iIndex)}
            /></TooltipView>);

      var oRemoveDOM = (<div className="removeItem" onClick={_this.handleRemoveItem.bind(_this, iIndex)}></div>);

      if(bIsDisabled){
        oInputDOM = (<div className="disabledItemInput">{sItem}</div>);
        oRemoveDOM = null;
      }

      return (
          <div className="itemInputContainer" key={iIndex}>
            <div className="itemInputDiv">
              {oInputDOM}
            </div>
            {oRemoveDOM}
          </div>
      );
    });
  };

  addNewElement = (oEvent) => {
    var sValue = oEvent.target.value;
    if(CS.trim(sValue)) {
      EventBus.dispatch(oEvents.ADD_NEW_ITEM, null, sValue, this.props.context);
    }
  };

  handleMainInputBlur = (oEvent) => {
    this.addNewElement(oEvent);
  };

  handleMainInputKeyDown = (oEvent) => {
    if(oEvent.keyCode == 13) {
      this.addNewElement(oEvent);
    }
  };

  getInputView = () => {
    var bIsDisabled = this.props.isDisabled;
    if(bIsDisabled){
      return null;
    }

    return (
        <input className="mainInput"
               ref={this.setRef.bind(this,"mainInput")}
               placeholder={getTranslation().TYPE_HERE_TO_ADD_WORDS}
               onBlur={this.handleMainInputBlur}
               onKeyDown={this.handleMainInputKeyDown}
        />
    );
  };

  render() {
    return (
        <div className="blackListEditableViewContainer">
          <div className="listLabel">{this.props.label}</div>
          <div className="itemListContainer">
            {this.getListView()}
          </div>
          <div className="itemMainInputContainer">
            {this.getInputView()}
          </div>
        </div>
    );
  }
}

export const view = BlackListEditableView;
export const events = oEvents;
