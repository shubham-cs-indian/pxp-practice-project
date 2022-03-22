import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import TooltipView from './../tooltipview/tooltip-view';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {};

const oPropTypes = {
  items: ReactPropTypes.array,
};
/**
 * @class ChipsView
 * @memberOf Views
 * @property {array} items - Items.
 */

// @CS.SafeComponent
class ChipsView extends React.Component {

  constructor(props) {
    super(props);
  }

  getIconView = (oItem) => {
    let sSrcURL = ViewUtils.getIconUrl(oItem.iconKey);
    let oIconDom = <ImageFitToContainerView imageSrc={sSrcURL}/>;
    if (CS.isEmpty(oItem.icon) && oItem.customIconClassName) {
      oIconDom = <div className={"selectedItemIcon " + oItem.customIconClassName} key="classIcon"></div>;
    }
    return oIconDom;
  };

  render() {
    var _this = this;
    var _props = _this.props;
    var aChipsView = [];
    var aItems = _props.items;
    var sSplitter = ViewUtils.getSplitter();
    var indicatorHTML = '';

    CS.forEach(aItems, function (oItem, sKey) {
      let sLabelClass = "itemLabel ";
      let oIconDom = !oItem.hideIcon  && _this.getIconView(oItem);
      indicatorHTML = (oItem.showIndicator) ? (<div className={"coloredIndicator"}>
        <span className="relevanceFlag"></span>
      </div>) : '';

      let sItemClassName = oItem.className ? "items "+ oItem.className : "items";

      aChipsView.push(
        <div className={sItemClassName} ref={oItem.id + sSplitter + "selected"} key={"items" + oItem.id} style={oItem.style}>
          {indicatorHTML}
          {oIconDom}
          <TooltipView label={CS.getLabelOrCode(oItem)} placement="bottom">
            <div className={sLabelClass}>{CS.getLabelOrCode(oItem)}</div>
          </TooltipView>
        </div>
      );
    });

    return (
        <div className="chipsView">
          {aChipsView}
        </div>
    );
  }
}

ChipsView.propTypes = oPropTypes;

export const view = ChipsView;
export const events = oEvents;
