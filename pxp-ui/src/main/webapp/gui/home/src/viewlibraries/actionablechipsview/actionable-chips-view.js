import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import TooltipView from './../tooltipview/tooltip-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {
  ACTIONABLE_CHIPS_VIEW_CROSS_ICON_CLICKED: "actionable_chips_view_cross_icon_clicked"
};

const oPropTypes = {
  items: ReactPropTypes.array,
  removeHandler: ReactPropTypes.func,
  context: ReactPropTypes.string,
  showLink: ReactPropTypes.bool,
};

/**
 * @class ActionableChipsView
 * @memberOf Views
 * @property {array} items - Items.
 */

// @CS.SafeComponent
class ActionableChipsView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleCrossIconClicked = (sId) => {
    EventBus.dispatch(oEvents.ACTIONABLE_CHIPS_VIEW_CROSS_ICON_CLICKED, sId, this.props.context) ;
  };

  getIconView = (oItem) => {
    let sSrcURL = ViewUtils.getIconUrl(oItem.iconKey);
    let oIconDom = null;

    if (CS.isEmpty(oItem.iconKey) && oItem.customIconClassName) {
      oIconDom = <div className={"selectedItemIcon " + oItem.customIconClassName} key="classIcon"></div>;
    }
    else {
      oIconDom = <ImageFitToContainerView imageSrc={sSrcURL}/>;
    }

    return oIconDom;
  };

  getCrossIconDOM = (oItem) => {
    let _this = this;
    let _props = _this.props;
    let oRemoveItemDOM = null;

    if (oItem.canRemove) {
      let fRemoveHandler = CS.noop;
      if(CS.isFunction(_props.removeHandler)) {
        fRemoveHandler = (_props.removeHandler).bind(this, oItem.id);
      }
      else {
        fRemoveHandler = _this.handleCrossIconClicked.bind(this, oItem.id);
      }
      oRemoveItemDOM = (<div className="removeItem" onClick={fRemoveHandler}/>);
    }

    return oRemoveItemDOM;
  };

  render () {
    var _this = this;
    var _props = _this.props;
    var aChipsView = [];
    var aItems = _props.items;
    var sSplitter = ViewUtils.getSplitter();

    CS.forEach(aItems, function (oItem) {
      let sItemClassName = oItem.className ? "items "+ oItem.className : "items";
      let oIconDom = _this.getIconView(oItem);
      let oConnectionDOM = null;
      let sClassName = "itemWrapper ";
      if (_props.showLink) {
        oConnectionDOM = <div className={"connectionLink"}></div>;
        sClassName += "withLink";
      }

      let sTooltipLabel = oItem.toolTip || CS.getLabelOrCode(oItem);

      aChipsView.push(
          <div className={sClassName}>
            {oConnectionDOM}
            <TooltipView label={sTooltipLabel} placement="bottom">
            <div className={sItemClassName} key={"items" + oItem.id}>
              {oIconDom}
                <div className="itemLabel">{CS.getLabelOrCode(oItem)}</div>
              {_this.getCrossIconDOM(oItem)}
            </div>
            </TooltipView>
          </div>
      );
    });

    return (
        <div className="chipsView actionable" style={_props.chipStyle || {}}>
          {aChipsView}
        </div>
    );
  }
}

ActionableChipsView.propTypes = oPropTypes;

export const view = ActionableChipsView;
export const events = oEvents;
