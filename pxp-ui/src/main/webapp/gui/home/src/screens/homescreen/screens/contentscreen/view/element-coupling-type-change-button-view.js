import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../../../../viewlibraries/customPopoverView/custom-popover-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { defaultCouplingTypes as fDefaultCouplingTypes } from '../../../../../commonmodule/tack/version-variant-coupling-types';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_ELEMENT_COUPLING_TYPE_CHANGED: "handle_element_coupling_type_changed"
};

const oPropTypes = {
  element: ReactPropTypes.object,
  sectionId: ReactPropTypes.string,
  context: ReactPropTypes.string
};

// @CS.SafeComponent
class ElementCouplingTypeChangeButtonView extends React.Component {

  constructor (oProps) {
    super(oProps);

    this.couplingTypeIcon = React.createRef();
  }

  state = {
    showPopover: false
  };

  handleCouplingTypeNodeClicked = (sTypeId) => {
    var sProperty = "couplingType";
    var sElementId = this.props.element.id;
    var sSectionId = this.props.sectionId;
    var sCallingContext = this.props.context;
    EventBus.dispatch(oEvents.HANDLE_ELEMENT_COUPLING_TYPE_CHANGED, sCallingContext, sSectionId, sElementId, sProperty, sTypeId);
    this.handlePopoverClose();
  };

  handlePopoverClose = () => {
    this.setState({
      showPopover: false
    });
  };

  handleCouplingIconClicked = () => {
    var sContext = this.props.context;
    if(sContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
      return;
    }

    this.setState({
      showPopover: true
    });
  };

  getCouplingTypeIcon = () => {
    var sContext = this.props.context;
    var sClickClassName = sContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY ? " clickDisable" : " clickEnable";
    var oElement = this.props.element;
    if(oElement.couplingType == "dynamicCoupled") {
      return (
          <TooltipView placement="bottom" label={getTranslation().DYNAMIC_COUPLED}>
            <div className={"dynamicCoupled upperIcon" + sClickClassName} ref={this.couplingTypeIcon} onClick={this.handleCouplingIconClicked}></div>
          </TooltipView>);
    }
    else if (oElement.couplingType == "tightlyCoupled") {
      return (
          <TooltipView placement="bottom" label={getTranslation().TIGHT_COUPLED}>
            <div className={"tightlyCoupled upperIcon" + sClickClassName} ref={this.couplingTypeIcon} onClick={this.handleCouplingIconClicked}></div>
          </TooltipView>);
    }
    else if (oElement.couplingType == "looselyCoupled") {
      return (
          <TooltipView placement="bottom" label={"No Coupling"}>
            <div className={"noCoupled upperIcon" + sClickClassName} ref={this.couplingTypeIcon} onClick={this.handleCouplingIconClicked}></div>
          </TooltipView>);
    }
  };

  getPopoverChildrenView = () => {
    var _this = this;
    var oElement = this.props.element;
    var sCurrentCouplingType = oElement.couplingType;
    var aTypeNodeDOMs = [];
    var aDefaultCouplingTypes = new fDefaultCouplingTypes();
    CS.forEach(aDefaultCouplingTypes, function (oType) {
      if(sCurrentCouplingType != oType.id){
        aTypeNodeDOMs.push(
            <div className="couplingTypeNode" key={oType.id}
                 onClick={_this.handleCouplingTypeNodeClicked.bind(_this, oType.id)}>
              {CS.getLabelOrCode(oType)}
            </div>
        );
      }
    });
    return <div className="couplingTypesPopoverContainer">{aTypeNodeDOMs}</div>
  };

  render() {
    var oCouplingTypeIcon = this.getCouplingTypeIcon();
    var oPopoverChildrenView = this.getPopoverChildrenView();
    return (
        <div className="elementCouplingTypeChangeButtonViewContainer">
          {oCouplingTypeIcon}
          <CustomPopoverView
              className="couplingTypeChangePopover popover-root"
              open={this.state.showPopover}
              anchorEl={this.couplingTypeIcon.current}
              anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
              transformOrigin={{horizontal: 'left', vertical: 'top'}}
              onClose={this.handlePopoverClose}
          >
            {oPopoverChildrenView}
          </CustomPopoverView>
        </div>
    );
  }
}

export const view = ElementCouplingTypeChangeButtonView;
export const events = oEvents;
