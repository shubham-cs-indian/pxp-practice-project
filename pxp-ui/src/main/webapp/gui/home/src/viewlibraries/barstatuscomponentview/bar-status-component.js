import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from '../utils/view-library-utils';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oPropTypes = {
  barLabel: ReactPropTypes.oneOfType([ReactPropTypes.string, ReactPropTypes.object, ReactPropTypes.array]).isRequired,
  totalCount: ReactPropTypes.number,
  failedCount: ReactPropTypes.number,
  successCount: ReactPropTypes.number,
  notApplicableCount: ReactPropTypes.number,
  inProgressCount: ReactPropTypes.number,
  key: ReactPropTypes.number,
};
/**
 * @class BarStatusComponent
 * @memberOf Views
 * @property {custom} barLabel - Name of the component.
 * @property {number} [totalCount] - Count of products.
 * @property {number} [failedCount] - Count of failed products.
 * @property {number} [notApplicableCount] - Count of not applicable products.
 * @property {number} [successCount] - Count of successful products.
 * @property {number} [inProgressCount] - Count of inProgress products.
 * @property {number} [key] - To identify DOM uniquely.
 */

// @CS.SafeComponent
class BarStatusComponent extends React.Component {

  constructor(props) {
    super(props);

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };
  }

  handleBarBlockMouseHoverAction = (sContext, sMouseAction, oEvent) => {
    var sKey = this.props.key;
    var sSplitter = ViewUtils.getSplitter();
    var oDesiredDom = this[sContext + sSplitter + sKey];
    var oTotalDom = this["total" + sSplitter + sKey];

    if(sMouseAction === "enter"){
      oDesiredDom.classList.add("visible");
      oTotalDom.classList.remove("visible");
    }else {
      oDesiredDom.classList.remove("visible");
      oTotalDom.classList.add("visible");
    }
  }

  getBarComponentBody = () => {
    var __props = this.props;
    var iTotal = __props.totalCount;

    /*if(!iTotal || iTotal === 0){
      return <div className="nothingToDisplay">{getTranslation().NOTHING_TO_DISPLAY}</div>
    }*/


    var iSuccess = __props.successCount;
    var iFailed = __props.failedCount;
    let iNotApplicable = __props.notApplicableCount;
    var iInProgress = __props.inProgressCount;
    var sKey = __props.key;



    var iSuccessLengthInPerc = iSuccess/iTotal*100;
    var iInProgressLengthInPerc = iInProgress/iTotal*100;
    var iFailedLengthInPerc = iFailed/iTotal*100;
    let iNotApplicablePerc = iNotApplicable/iTotal*100;

    var oSuccessStyle = {width: iSuccessLengthInPerc + "%"};
    var oInProgressStyle = {width: iInProgressLengthInPerc + "%"};
    var oFailedStyle = {width: iFailedLengthInPerc + "%"};
    var oNotApplicableStyle = {width: iNotApplicablePerc + "%"};

    var sSplitter = ViewUtils.getSplitter();

    return (
        <div className="barComponentBodyWrapper">
          <div className="informationContainer">
            <div className="innerInfoWrapper total visible" ref={this.setRef.bind(this, "total"+ sSplitter + sKey)}>
              <div className="infoCount">{iTotal}</div>
              <div className="infoLabel">{getTranslation().TOTAL}</div>
            </div>
            <div className="innerInfoWrapper success" ref={this.setRef.bind(this, "success"+ sSplitter + sKey)}>
              <div className="infoCount">{iSuccess}</div>
              <div className="infoLabel">{getTranslation().SUCCESS}</div>
            </div>
            <div className="innerInfoWrapper inProgress" ref={this.setRef.bind(this, "inProgress"+ sSplitter + sKey)}>
              <div className="infoCount">{iInProgress}</div>
              <div className="infoLabel">{getTranslation().IN_PROGRESS}</div>
            </div>
            <div className="innerInfoWrapper failed" ref={this.setRef.bind(this, "failed"+ sSplitter + sKey)}>
              <div className="infoCount">{iFailed}</div>
              <div className="infoLabel">{getTranslation().FAILED}</div>
            </div>
            <div className="innerInfoWrapper notApplicable" ref={this.setRef.bind(this, "notApplicable"+ sSplitter + sKey)}>
              <div className="infoCount">{iNotApplicable}</div>
              <div className="infoLabel">{"NotApplicable"}</div>
            </div>
          </div>

          <div className="barContainer">
            <div className="barBlock successBlock"
                 style={oSuccessStyle}
                 onMouseEnter={this.handleBarBlockMouseHoverAction.bind(this, "success", "enter")}
                 onMouseLeave={this.handleBarBlockMouseHoverAction.bind(this, "success", "leave")}></div>
            <div className="barBlock inProgressBlock"
                 style={oInProgressStyle}
                 onMouseOver={this.handleBarBlockMouseHoverAction.bind(this, "inProgress", "enter")}
                 onMouseLeave={this.handleBarBlockMouseHoverAction.bind(this, "inProgress", "leave")}></div>
            <div className="barBlock failedBlock"
                 style={oFailedStyle}
                 onMouseOver={this.handleBarBlockMouseHoverAction.bind(this, "failed", "enter")}
                 onMouseLeave={this.handleBarBlockMouseHoverAction.bind(this, "failed", "leave")}></div>
              <div className="barBlock notApplicableBlock"
                   style={oNotApplicableStyle}
                   onMouseOver={this.handleBarBlockMouseHoverAction.bind(this, "notApplicable", "enter")}
                   onMouseLeave={this.handleBarBlockMouseHoverAction.bind(this, "notApplicable", "leave")}></div>
          </div>
        </div>
    )

  }

  render() {
    return (
        <div className="barComponentContainer">
          <div className="barComponentHeaderLabel">
            {this.props.barLabel}
          </div>
          <div className="barComponentBody">
            {this.getBarComponentBody()}
          </div>
        </div>
    )
  }
}

BarStatusComponent.propTypes = oPropTypes;

export default BarStatusComponent;
