import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import {CompactPicker} from 'react-color'
import {getTranslations as getTranslation} from '../../commonmodule/store/helper/translation-manager.js';
import ColorUtils from '../../commonmodule/util/color-utils';
import {view as CustomPopoverView} from '../customPopoverView/custom-popover-view';
import {view as CustomMaterialButtonView} from '../custommaterialbuttonview/custom-material-button-view';
import colorList from '../tack/color-picker-color-list';

const oEvents = {
  COLOR_PICKER_COLOR_CHANGED: "color_picker_color_changed",
};

const oPropTypes = {
  color: ReactPropTypes.string,
  context: ReactPropTypes.string,
};

// @CS.SafeComponent
class ColorPickerView extends React.Component {

  constructor (props) {
    super(props);
    this.state = {
      color: props.color,
      showPopOver: false,
    };

    this.triggerElement = React.createRef();
  };

  static getDerivedStateFromProps (oNextProps, oState) {
    if (oState.showPopOver && oState.color !== oNextProps.color) {
      return {
        color: oState.color,
      };
    } else {
      return {
        color: oNextProps.color,
      };
    }
  }

  handleChangeComplete = (color) => {
    this.setState({color: color.hex});
  };

  handlePopoverEvent = (oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      let oDOM = this.triggerElement.current;
      this.setState({
        showPopOver: true,
        anchorElement: oDOM
      });
    }
  };

  handleSnackBarBtnClicked = (sAction) => {
    if (sAction === "apply") {
      this.setState({
        showPopOver: false
      });
      EventBus.dispatch(oEvents.COLOR_PICKER_COLOR_CHANGED, this.state.color, this.props.context);
    } else {
      this.closePopover();
    }
  };

  closePopover = () => {
    window.dontRaise = true; //Do not remove
    this.setState({
      color: this.props.color !== this.state.color ? this.props.color : this.state.color,
      showPopOver: false
    });
  };

  getSnackBarContainer = () => {
    let aButtonData = [
      {
        id: "apply",
        label: getTranslation().APPLY,
        isRaised: true,
        isDisabled: false
      },
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isRaised: false,
        isDisabled: false
      }
    ];
    let aButtonViews = [];
    CS.forEach(aButtonData, (oButtonData) => {
      aButtonViews.push(<div className={`snackBarButton ${oButtonData.id}`}>
        <CustomMaterialButtonView
            onButtonClick={this.handleSnackBarBtnClicked.bind(this, oButtonData.id)}
            label={oButtonData.label}
            isRaisedButton={oButtonData.isRaised}
            isDisabled={false}/>
      </div>);
    });
    return <div className="colorPickerSnackBarContainer">{aButtonViews}</div>;
  };

  getColorInfoSection (sLabel, sColorCode) {
    return (
        <div className="currentColorInfoSection">
          <span className="currentColorLabel">{sLabel} : </span>
          <span className="currentColorValue">{sColorCode}</span>
        </div>
    );
  }

  getColorInfoView () {
    return (
        <div className="currentColorInfo">
          {this.getColorInfoSection("Hex", this.state.color)}
          {this.getColorInfoSection("RGB", `(${CS.join(ColorUtils.hexToRGBConversion(this.state.color), ", ")})`)}
        </div>
    );
  };

  getColorPopOverChildrenView = () => {
    if (CS.isNotEmpty(this.state.color)) {
      return (
          <div className="colorPickerChildrenView">
            <div className="currentColor" style={{backgroundColor: this.state.color}}
                 onClick={this.handlePopoverEvent.bind(this)} title={getTranslation().CHANGE_COLOR}/>
            {this.getColorInfoView()}
          </div>);
    } else {
      return (<div className="colorPickerNothingSelected"
                   onClick={this.handlePopoverEvent.bind(this)}>{getTranslation().NOTHING_IS_SELECTED}</div>);
    }
  };

  getCompactPickerView = () => {
    return (<CompactPicker
        className="compactColorPickerView"
        color={ this.state.color || ""}
        colors={colorList}
        onChangeComplete={ this.handleChangeComplete.bind(this)}/>);
  };


  render () {
    let oPopoverStyle = {maxWidth: "270px"};
    return (
        <div className="colorPickerView">
          <CustomPopoverView className="popover-root"
                             open={this.state.showPopOver}
                             style={oPopoverStyle}
                             anchorEl={this.state.anchorElement}
                             anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
                             transformOrigin={{horizontal: 'left', vertical: 'top'}}
                             onClose={this.closePopover}>
            {this.getCompactPickerView()}
            {this.getSnackBarContainer()}
          </CustomPopoverView>
          <div className="triggerElement" ref={this.triggerElement}>
            {this.getColorPopOverChildrenView()}
          </div>
        </div>
    );
  }
}

ColorPickerView.propTypes = oPropTypes;

export const view = ColorPickerView;
export const events = oEvents;
