import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as NumberLocaleView } from '../numberlocaleview/number-locale-view';
import ReactDOM from 'react-dom';

const oEvents = {
};

const oPropTypes = {
  minValue: ReactPropTypes.number.isRequired,
  maxValue: ReactPropTypes.number.isRequired,
  value: ReactPropTypes.number.isRequired,
  sliderModel: ReactPropTypes.object.isRequired,
  onApply: ReactPropTypes.func,
};
/**
 * @class SliderView - Use for display slider view for select range in tags.
 * @memberOf Views
 * @property {number} minValue - Default set -100 as a minvalue.
 * @property {number} maxValue - Default set 100 as a max value.
 * @property {number} value - Pass set value for tags.
 * @property {object} sliderModel - Pass properties like childrenModels, depth, id, label, relevence, tagId
 * @property {func} [onApply] - when change in tag range this func onApply call.
 */

let down;

let iLocalSliderValue;

// @CS.SafeComponent
class SliderView extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value : this.props.value,
      isSliding: false
    }

    down = false;
    iLocalSliderValue = 0;

    this.range_slider = React.createRef();
    this.grabber = React.createRef();
  }

  componentDidUpdate() {
    this.handleComponentUpdate();
  }

  componentDidMount() {
    this.handleComponentUpdate();
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    /**
     * On every update this method called
     * There fore we maintaining isSliding state on change of sliding isSliding state changed with true,
     * after state changed this method called and according to this we making decision value need to pick from prop or state.
     */
    if(oState.isSliding){
      return {
        isSliding : false
      }
    } else {
      return {
        value : oNextProps.value
      }
    }
  }

  /*componentWillReceiveProps(oNewProps) {
    this.updateValueInState(oNewProps.value);
  }*/

  updateDragger =(oEvent)=> {
    var iRangeWidth = this.range_slider.current.offsetWidth;
    let oDom = ReactDOM.findDOMNode(this.range_slider.current);
    var iRangeLeft =  oDom.getBoundingClientRect().left;
    var iMinValue = this.props.minValue;
    var iMaxValue = this.props.maxValue;
    var iTotalRange = iMaxValue - iMinValue;

    if (down) {

      if(oEvent.pageX >= iRangeLeft && oEvent.pageX <= (iRangeLeft + iRangeWidth)){
        var iNewValue = (Math.round(((oEvent.pageX - iRangeLeft) / (iRangeWidth)) * iTotalRange) + iMinValue);
        this.updateValueInState(iNewValue);
      } else if(oEvent.pageX < iRangeLeft){
        this.updateValueInState(iMinValue);
      } else if(oEvent.pageX > (iRangeLeft + iRangeWidth)){
        this.updateValueInState(iMaxValue);
      }
    }
  }

  calculateDraggerOffset =(iValue)=>{
    var iRangeWidth = this.range_slider.current.offsetWidth;
    var iGrabberWidth = this.grabber.current.offsetWidth;
    var iMinValue = this.props.minValue;
    var iMaxValue = this.props.maxValue;
    var iTotalRange = iMaxValue - iMinValue;

    return ((iValue - iMinValue)/iTotalRange * iRangeWidth) - iGrabberWidth/2 - 2;
  }

  mouseDown =(oEvent)=> {
    //Used oEvent values evaporates after re-render so had to store the required value
    var event = {
      pageX: oEvent.pageX
    };
    down = true;
    document.body.addEventListener("mousemove", this.updateDragger);
    document.body.addEventListener("mouseup", this.mouseUp);
    this.updateDragger(event);
    oEvent.stopPropagation();
  }

  mouseUp =()=> {
    down = false;
    document.body.removeEventListener("mousemove", this.updateDragger);
    document.body.removeEventListener("mouseup", this.mouseUp);
    this.handleSliderValueChanged(this.state.value);
  }

  updateValueInState =(sValue)=> {
    this.setState({
      value : sValue,
      isSliding: true
    });
  }

  handleRangeChange =(sValue)=> {
    if (!(sValue == "" || sValue == "-")) {
      var iNewValue = +sValue;
      var iMinAllowed = this.props.minValue;
      var iMaxAllowed = this.props.maxValue;

      if(iNewValue < iMinAllowed){
        iNewValue = iMinAllowed;
      }

      if(iNewValue > iMaxAllowed){
        iNewValue = iMaxAllowed;
      }

      if(iNewValue >= this.props.minValue && iNewValue <= this.props.maxValue) {
        var iLeft = this.calculateDraggerOffset(iNewValue);
        this.grabber.current.style.left = iLeft + "px";
        this.props.sliderModel.properties['iNewValue'] =  iNewValue;
      }

      sValue = iNewValue;
    }
    this.updateValueInState(sValue);
  }

  handleSliderValueChanged =(iNewValue)=> {
    this.props.sliderModel.properties['iNewValue'] =  iNewValue;
    if (CS.isFunction(this.props.onApply)) {
      this.props.onApply(this.props.sliderModel);
    }
  }

  handleOnBlur =(sValue)=> {
    var iNewValue = +sValue;

    if(iNewValue >= this.props.minValue && iNewValue <= this.props.maxValue) {
      var iLeft = this.calculateDraggerOffset(iNewValue);
      this.grabber.current.style.left = iLeft + "px";
      var sOldValue = this.props.value;
      if(sOldValue !== iNewValue) {
        this.handleSliderValueChanged(iNewValue);
      }
    }
  }

  handleComponentUpdate =()=>{
    var iLeft = this.calculateDraggerOffset(this.state.value);
    this.grabber.current.style.left = iLeft + "px";
  }

  getClass =()=> {
    var iValue = this.state.value;
    if(iValue < 0) {
      return "negativeTagRelevance";
    } else if (iValue > 0) {
      return "positiveTagRelevance";
    }
    return '';
  }

  render() {
    var sClass = "sliderViewNumberLocaleContainer " + this.getClass();

    return (

      <div className="sliderContainer">
        <div className="range-slider"  ref={this.range_slider} onMouseDown={this.mouseDown}>
          <span className="sliderHandle" ref={this.grabber}/>
        </div>
        <div className={sClass}>
          <NumberLocaleView value={this.state.value} negativeAllowed={true} isOnlyInteger={true} onChange={this.handleRangeChange} onForceBlur={this.handleOnBlur}/>
        </div>
      </div>
    )
  }
}

SliderView.propTypes = oPropTypes;

export const view = SliderView;
export const events = oEvents;
