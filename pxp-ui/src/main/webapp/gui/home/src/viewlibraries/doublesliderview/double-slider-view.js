import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
};
const oPropTypes = {
  minValue: ReactPropTypes.number.isRequired,
  maxValue: ReactPropTypes.number.isRequired,
  from: ReactPropTypes.number.isRequired,
  to: ReactPropTypes.number.isRequired,
  sliderModel: ReactPropTypes.object.isRequired,
  onApply: ReactPropTypes.func
};
/**
 * @class DoubleSliderView
 * @memberOf Views
 * @property {number} minValue
 * @property {number} maxValue
 * @property {number} from
 * @property {number} to
 * @property {object} sliderModel
 * @property {func} [onApply]
 */

// @CS.SafeComponent
class DoubleSliderView extends React.Component {
  constructor(props) {
    super(props);

    this.down = false;
    this.draggerContext = "";
    this.iFromLocalSliderValue = 0;
    this.iToLocalSliderValue = 0;

    this.setRef = ( sRef, element) => {
      this[sRef] = element;
    };
  }

  componentDidUpdate() {
    this.handleComponentUpdate();
  }

  componentDidMount() {
    this.handleComponentUpdate();
  }

  updateDragger =(oEvent)=> {
    var sContext = this.draggerContext;
    var oGrabberDOM = this['grabber' + sContext];
    var iRangeWidth = this.range_slider.offsetWidth;
    var iRangeLeft = (this.range_slider).getBoundingClientRect().left;
    var iGrabberWidth = oGrabberDOM.offsetWidth;
    var iMinValue = this.props.minValue;
    var iMaxValue = this.props.maxValue;
    var iTotalRange    = iMaxValue - iMinValue;
    var bFromDragger = sContext == "From";
    var bToDragger = sContext == "To";

    if (this.down) {

      if (oEvent.pageX >= iRangeLeft && oEvent.pageX <= (iRangeLeft + iRangeWidth)) {
        var iNewValue = (Math.round(((oEvent.pageX - iRangeLeft) / (iRangeWidth)) * iTotalRange) + iMinValue);

        if (bFromDragger) {
          if (iNewValue > this.iToLocalSliderValue) {
            return;
          }
        } else {
          if (iNewValue < this.iFromLocalSliderValue) {
            return;
          }
        }

        oGrabberDOM.style.left = (oEvent.pageX - iRangeLeft - iGrabberWidth) + "px";
        if (bFromDragger) {
          this.iFromLocalSliderValue = iNewValue;
          this['fromRelevance'].innerHTML = iNewValue;
        } else {
          this.iToLocalSliderValue = iNewValue;
          this['toRelevance'].innerHTML = iNewValue;
        }
      } else if(oEvent.pageX < iRangeLeft && bFromDragger) {
        this.iFromLocalSliderValue = iMinValue;
        this['fromRelevance'].innerHTML = iMinValue;
        var iFromLeft = this.calculateDraggerOffset(iMinValue);
        oGrabberDOM.style.left = iFromLeft + "px";
      } else if(oEvent.pageX > (iRangeLeft + iRangeWidth) && bToDragger){
        this.iToLocalSliderValue = iMaxValue;
        this['toRelevance'].innerHTML = iMaxValue;
        var iToLeft = this.calculateDraggerOffset(iMaxValue);
        oGrabberDOM.style.left = iToLeft + "px";
      }
    }

    this.highlightRange();
  }

  calculateDraggerOffset =(iValue)=>{
    var iRangeWidth = this['range_slider'].offsetWidth;
    var iGrabberWidth = this['grabberFrom'].offsetWidth;
    var iMinValue = this.props.minValue;
    var iMaxValue = this.props.maxValue;
    var iTotalRange = iMaxValue - iMinValue;

    return ((iValue - iMinValue)/iTotalRange * iRangeWidth) - iGrabberWidth;

  }

  getMod =(iNum)=> {
    return iNum > 0 ? iNum : -iNum;
  }

  highlightRange =()=> {
    var oRangeSliderDOM = this['range_slider'];
    var iRangeLeft = (this.range_slider).getBoundingClientRect().left;
    var iRangeWidth = oRangeSliderDOM.offsetWidth;
    var iFromLeft = this['grabberFrom'].offsetLeft;
    var iToLeft = this['grabberTo'].offsetLeft;

    var iFromPercent = iFromLeft / iRangeWidth * 100;
    var iToPercent = iToLeft / iRangeWidth * 100;

    var oHighlighterDOM = this['highlighter'];
    oHighlighterDOM.style.left = iFromLeft + 'px';
    oHighlighterDOM.style.width = (iToLeft - iFromLeft) + 10 + 'px';

    // var sGradient = "linear-gradient(90deg, #fff " + iFromPercent + "%, #333 " + iFromPercent + "%, #333 " + iToPercent + "%, #fff " + iToPercent + "%)";
    //
    // oRangeSliderDOM.style['background-image'] = sGradient;
  }

  mouseDown =(oEvent)=> {
    if(!this.down) {
      this.down = true;

      var iRangeLeft = (this.range_slider).getBoundingClientRect().left;
      var iRangeWidth = this['range_slider'].offsetWidth;
      var iMouseLeftPosition = oEvent.pageX - iRangeLeft;

      var iFromLeft = this['grabberFrom'].offsetLeft;
      var iToLeft = this['grabberTo'].offsetLeft;

      var iFromDiff  = this.getMod(iFromLeft - iMouseLeftPosition);
      var iToDiff  = this.getMod(iToLeft - iMouseLeftPosition);

      if (iFromDiff == iToDiff){
        if(iRangeWidth/2 > iMouseLeftPosition) {
          this.draggerContext = "From";
        } else {
          this.draggerContext = "To";
        }
      } else if(iFromDiff < iToDiff) {
        this.draggerContext = "From";
      } else {
        this.draggerContext = "To";
      }

      document.body.addEventListener("mouseup", this.mouseUp);
      this.updateDragger(oEvent);
    }
  }

  mouseDownOnGrabber =(sContext)=> {
    if(!this.down) {
      this.down = true;
      this.draggerContext = sContext;
      document.body.addEventListener("mousemove", this.updateDragger);
      document.body.addEventListener("mouseup", this.mouseUp);
    }
  }

  mouseUp =()=> {
    if (this.down) {
      var iNewValue = this.iFromLocalSliderValue;
      var sContext = CS.toLower(this.draggerContext);
      if (sContext == "to") {
        iNewValue = this.iToLocalSliderValue;
      }
      this.props.sliderModel.properties['iNewValue'] = iNewValue;
      this.props.sliderModel.properties['key'] = sContext;
      if (CS.isFunction(this.props.onApply)) {
        this.props.onApply(this.props.sliderModel);
      }
    }

    this.down = false;
    this.draggerContext = "";
    document.body.removeEventListener("mouseup", this.mouseUp);
    document.body.removeEventListener("mousemove", this.updateDragger);
  }

 /* handleRangeChange =(oEvent)=> {
    //todo check and re-route when needed
      /!*var iNewValue = parseInt(oEvent.target.value);
    if(iNewValue >= this.props.minValue && iNewValue <= this.props.maxValue) {
      var iLeft = this.calculateDraggerOffset(iNewValue);
      this.refs.grabber.style.left = iLeft + "px";
      this.props.sliderModel.properties['iNewValue']   = iNewValue;
      EventBus.dispatch(oEvents.SLIDER_VALUE_CHANGED, this, this.props.sliderModel);
    }*!/
  }*/

  handleComponentUpdate =()=> {

    this.updateComponentWithNewValues(this.props.from, this.props.to);
  }

  updateComponentWithNewValues =(iFrom, iTo)=>{
    var iFromLeft = this.calculateDraggerOffset(iFrom);
    this['grabberFrom'].style.left = iFromLeft + "px";

    var iToLeft = this.calculateDraggerOffset(iTo);
    this['grabberTo'].style.left = iToLeft + "px";
    this.highlightRange();

    this.draggerContext = "";
    this.down = false;
    this.iFromLocalSliderValue = iFrom;
    this.iToLocalSliderValue = iTo;
    this['fromRelevance'].innerHTML = iFrom;
    this['toRelevance'].innerHTML = iTo;
  }


  handleClearSliderClicked =()=> {
    this.updateComponentWithNewValues(this.props.minValue, this.props.maxValue);
    this.props.sliderModel.properties['iNewValue'] = 0;
    if (CS.isFunction(this.props.onApply)) {
      this.props.onApply(this.props.sliderModel);
    }
  }

  render() {

    return (
        <div className="doubleSliderViewContainer" ref= {this.setRef.bind(this,"sliderContainer")}>
          <div className="doubleSliderClearButton" onClick={this.handleClearSliderClicked}>{getTranslation().CLEAR}</div>
          <div className="range-slider"  ref= {this.setRef.bind(this,"range_slider")} onMouseDown={this.mouseDown}>
            <div className="sliderHandleWrapper" ref= {this.setRef.bind(this,"grabberFrom")}>
              <div className="sliderHandle" onMouseDown={this.mouseDownOnGrabber.bind(this, 'From')}></div>
              <div className="sliderHandleBase" ref={this.setRef.bind(this,"fromRelevance")}></div>
            </div>
            <span className="doubleSliderHighlight" ref={this.setRef.bind(this,"highlighter")} />
            <div className="sliderHandleWrapper" ref={this.setRef.bind(this,"grabberTo")}>
              <div className="sliderHandle" onMouseDown={this.mouseDownOnGrabber.bind(this, 'To')}></div>
              <div className="sliderHandleBase" ref={this.setRef.bind(this,"toRelevance")}></div>
            </div>
          </div>
        </div>
    );
  }
}

DoubleSliderView.propTypes = oPropTypes;

export const view = DoubleSliderView;
export const events = oEvents;
