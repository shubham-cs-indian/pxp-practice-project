import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import TextField from '@material-ui/core/TextField';
import { getTranslations as oTranslations } from '../../commonmodule/store/helper/translation-manager.js';
import alertify from '../../commonmodule/store/custom-alertify-store';

const oEvents = {};

const oPropTypes = {
  defaultValue : ReactPropTypes.string,
  onBlurHandler: ReactPropTypes.func,
  disableUnderline: ReactPropTypes.bool,
  doNotValidateTheNumber: ReactPropTypes.bool,
  label: ReactPropTypes.string
};

// @CS.SafeComponent
class CustomMobileNumberView extends React.Component {
  static typing = false;

  constructor (props) {
    super(props);
    this.state = {
      value: this.props.defaultValue
    }
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    if(CustomMobileNumberView.typing){
      return {
        value: oState.value
      };
    } else if(oNextProps.defaultValue !== oState.value) {
      return {
        value: oNextProps.defaultValue || "",
      };
    }
    return null;
  }


  handleOnKeyDown = (oEvent) => {
    let regEx = /^(\+?\d*\s?\-?\(?\)?)*$/;
    let aEscapeList = ["Backspace", "Delete", "ArrowLeft", "ArrowRight"];
    if (!regEx.test(oEvent.key) && !CS.includes(aEscapeList, oEvent.key) && !(oEvent.ctrlKey || oEvent.shiftKey)) {
      oEvent.preventDefault();
    }
  };

  handleOnChange = (oEvent) => {
    let sNewValue = oEvent.target.value;
    this.setState({
      value: sNewValue
    });
    CustomMobileNumberView.typing = true;
  }

  handleOnPaste = (oEvent) => {
    let sPrevValue = oEvent.target.value;
    let oTarget = oEvent.target;
    setTimeout(function () {
      let sNewValue = oTarget.value;
      if (!this.props.doNotValidateTheNumber && !CS.isMobileNumberValid(sNewValue)) {
        alertify.error(oTranslations().PLEASE_ENTER_VALID_MOBILE_NUMBER);
        oTarget.value = sPrevValue;
      }
    }, 0);

    CustomMobileNumberView.typing = false;
  };

  handleOnBlur = (oEvent) => {
    let sNewValue = oEvent.target.value;
    if(sNewValue === this.props.defaultValue) {
      return;
    }

    if (!this.props.doNotValidateTheNumber && !CS.isMobileNumberValid(sNewValue)) {
      alertify.error(oTranslations().PLEASE_ENTER_VALID_MOBILE_NUMBER);
    }

    CustomMobileNumberView.typing = false;
    this.props.onBlurHandler(sNewValue);
  };

  render () {
    let sDefaultValue = this.state.value;
    let bDisableUnderline = this.props.disableUnderline;

    return (
        <div className="userInputFieldContainer">
          <TextField key={'mobileNumber'}
                     type={"text"}
                     value={sDefaultValue}
                     InputProps = {{disableUnderline: bDisableUnderline}}
                     label={this.props.label}
                     fullWidth={true}
                     onKeyDown = {this.handleOnKeyDown}
                     onPaste = {this.handleOnPaste}
                     onBlur={this.handleOnBlur}
                     onChange = {this.handleOnChange}
                     autoComplete={"off"}/>
        </div>
    )
  }
}

CustomMobileNumberView.propTypes = oPropTypes;

export const view = CustomMobileNumberView;
export const event = oEvents;
