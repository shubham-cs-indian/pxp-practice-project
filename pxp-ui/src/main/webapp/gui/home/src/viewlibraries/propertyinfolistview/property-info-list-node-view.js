import React from 'react';
import ReactPropTypes from 'prop-types';
import TooltipView from './../tooltipview/tooltip-view';
import copy from "copy-to-clipboard";
import alertify from "../../commonmodule/store/custom-alertify-store";
import {getTranslations} from "../../commonmodule/store/helper/translation-manager";
import CS from '../../libraries/cs';


const oPropTypes = {
  model: ReactPropTypes.object.isRequired
};

/**
 * @class PropertyInfoListNodeView - PropertyInfoListNodeView used to present list of items.
 * @memberOf Views
 * @property {object} model - Contains view which will render inside PropertyInfoListNodeView.
 */

class PropertyInfoListNodeView extends React.Component {

  constructor(props) {
    super(props);
  }


  getLabelDOM = (oModel) => {
    let sLabelClass = "label ";
    sLabelClass = sLabelClass + (oModel.labelClassName || "");
    let sIconClassName = "listViewIcon " + oModel.labelIconClassName;
    let oLabelIcon = (<div className={sIconClassName}></div>);

    let sLabel = oModel.label;
    let oLabelDOM = (
        <div className="listNodeViewLabelWrapper">
          {oLabelIcon}
          <TooltipView placement={"top"} label={sLabel}>
          <div className={sLabelClass}>{sLabel}</div>
          </TooltipView>
        </div>
    );
    return oLabelDOM
  };

  getValueDOM = (oModel) => {
    let aButtonsDOMs = this.getValueActionButtons(oModel);
    let sValue = oModel.value;
    let oValueDOM = (
        <div className="listNodeViewValueWrapper">
          <TooltipView placement={"top"} label={sValue}>
            <div className="value">{sValue}</div>
          </TooltipView>
          {aButtonsDOMs}
        </div>
    );
    return oValueDOM;
  };

  getValueActionButtons = (oModel) => {
    let oProperties = oModel.properties || {};
    let aActionButtonsForValue = oProperties.actionButtonsForValue || [];
    let aButtons = [];
    CS.forEach(aActionButtonsForValue, (oButtonData) => {
      switch (oButtonData.id) {
        case "copyValue":
          aButtons.push(this.getCopyValueButtonDOM(oButtonData, oModel.value));
          break;
      }
    });
    return aButtons;
  };

  getCopyValueButtonDOM = (oButtonData, sValue) => {
    return (
        <TooltipView placement={"top"} label={oButtonData.toolTip}>
          <div className={"copyToClipboard"}
               onClick={this.handleCopyToClipboardClick.bind(this, sValue)}/>
        </TooltipView>
    );
  };


  handleCopyToClipboardClick = (sValue, oEvent) => {
    oEvent.stopPropagation();
    copy(sValue) ? alertify.success(getTranslations().CODE_COPIED)
        : alertify.error(getTranslations().COPY_TO_CLIPBOARD_FAILED);
  };


  render() {

    let oListModel = this.props.model;
    return (
        <div className={"propertyInfoListNodeViewContainer "}>
          {this.getLabelDOM(oListModel)}
          {this.getValueDOM(oListModel)}
        </div>
    );
  }
}

PropertyInfoListNodeView.propTypes = oPropTypes;
export const view = PropertyInfoListNodeView;