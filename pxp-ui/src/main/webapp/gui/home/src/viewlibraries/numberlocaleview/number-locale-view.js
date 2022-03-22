import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import alertify from '../../commonmodule/store/custom-alertify-store';
import {numberFormatIds} from "../../commonmodule/tack/number-and-date-format-ids-dictionary";
import ViewUtils from './../utils/view-library-utils';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import INumberLocaleView from './i-number-locale-view';
import MetricCheckConstants from '../../commonmodule/tack/measurement-metrics-constants';
const oNumberFormatIdDictionary = numberFormatIds();

const oEvents = {
};

const oPropTypes = {
  label: ReactPropTypes.string,
  value: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
  onChange: ReactPropTypes.func,
  handleEnterKeyPress: ReactPropTypes.bool,
  onBlur: ReactPropTypes.func, //only called when value is changed
  onForceBlur: ReactPropTypes.func, //always called on input blur
  onKeyDown: ReactPropTypes.func,
  hintText: ReactPropTypes.string,
  errorText: ReactPropTypes.string,
  errorStyle: ReactPropTypes.object,
  isDisabled: ReactPropTypes.bool,
  isAutoFocus: ReactPropTypes.bool,
  isOnlyInteger: ReactPropTypes.bool,
  negativeAllowed: ReactPropTypes.bool,
  isTelePhone: ReactPropTypes.bool,
  hideTooltip: ReactPropTypes.bool,
  precision: ReactPropTypes.number,
  minValue: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
  maxValue: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
  disableNumberLocaleFormatting: ReactPropTypes.bool
};
/**
 * @class NumberLocaleView - use for min max price display.
 * @memberOf Views
 * @property {string} [label] -  label of textField.
 * @property {custom} [value] -  string of textField value.
 * @property {func} [onChange] -  function which is used for onChange textField event.
 * @property {bool} [handleEnterKeyPress] -  boolean value for handleEnterKeyPress true or not.
 * @property {func} [onBlur] -  function which is used for onBlur textField event.
 * @property {func} [onForceBlur] -  function which is used for onForceBlur textField event.
 * @property {func} [onKeyDown] -  function which is used for onKeyDown textField event.
 * @property {string} [hintText] -  hintText of textField.
 * @property {string} [errorText] -  label of errorText.
 * @property {object} [errorStyle] - pass css for error message.
 * @property {bool} [isDisabled] -  boolean value for isDisabled textField input or not.
 * @property {bool} [isAutoFocus] -  boolean value for isAutoFocus number input or not.
 * @property {bool} [isOnlyInteger] -  boolean value for isOnlyInteger number input or not.
 * @property {bool} [negativeAllowed] -  boolean value for negative number input allowed or not.
 * @property {bool} [isTelePhone] -  boolean value for isTelePhone number input or not.
 * @property {bool} [hideTooltip] -  boolean value for hideTooltip for number input or not.
 * @property {number} [precision] - prop pass 2 digit of precision value.
 * @property {custom} [minValue] - propa pass minvalue for price.
 * @property {custom} [maxValue] - pass maxValue for price.
 */

// @CS.SafeComponent
class NumberLocaleView extends INumberLocaleView {

  constructor(props) {
    super(props);

    this.state = {
      prevVal: NumberLocaleView.validatedValue(this.props.value),
      value: NumberLocaleView.validatedValue(this.props.value),
      isEditable: false
    }
  }

  shouldComponentUpdate =(oNextProps, oNextState)=> {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  }

  //TODO: Review -> Add Comment
  static getDerivedStateFromProps (oNextProps, oState) {
    let sVal = NumberLocaleView.validatedValue(oNextProps.value);

    if (sVal !== oState.prevVal) {
      return {
        prevVal: sVal,
        value: sVal
      };
    }

    return null;
  }

  /*componentWillReceiveProps =(oNextProps)=> {
    if(oNextProps.value !== this.props.value) {
      this.setState({
        value: this.validatedValue(oNextProps.value)
      });
    }
  };
*/
  handleInputOnFocus = () => {
    var bDisabled = this.props.isDisabled;

    if(!bDisabled) {
      this.setState({
        isEditable: true
      });
    }
  };

  static validatedValue = (sValue) => {
    if(CS.isNull(sValue)) {
      return "";
    }

    return sValue;
  };

  getValueRequiredInEditMode = (sValue) => {
    let oNumberFormat = ViewUtils.getSelectedNumberFormatByDataLanguage();
    let sDecimalSeparator = oNumberFormat.decimalSeparator;
    sValue = ViewUtils.getNumberAccordingToPrecision(sValue, this.props.precision, MetricCheckConstants.DEFAULT_DECIMAL_SEPARATOR);

    return sValue.replace(MetricCheckConstants.DEFAULT_DECIMAL_SEPARATOR, sDecimalSeparator);
  };

  convertNumberToStandardFormat = (sNewVal) => {
    let oNumberFormat = ViewUtils.getSelectedNumberFormatByDataLanguage();

    switch (oNumberFormat.id) {
      case oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_DOT:
        sNewVal = sNewVal.replace(/,/g, '');
        break;
      case oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_DOT_COMMA:
        sNewVal = sNewVal.replace(/\./g, '');
        sNewVal = sNewVal.replace(/,/g, '.');
        break;
      case oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_COMMA:
        sNewVal = sNewVal.replace(/\s/g, '');
        sNewVal = sNewVal.replace(/,/g, '.');
        break;
      case oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_APOSTROPHE_DOT:
        sNewVal = sNewVal.replace(/’/g, '');
        break;
      case oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_DOT:
        sNewVal = sNewVal.replace(/\s/g, '');
        break;
      case oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_DOT:
        /** Already in standard format. No need of processing **/
        break;
      case oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_COMMA:
        sNewVal = sNewVal.replace(/,/g, '.');
        break;
      case oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_SLASH:
        sNewVal = sNewVal.replace(/,/g, '');
        sNewVal = sNewVal.replace(/\//g, '.');
        break;
      case oNumberFormatIdDictionary.NUMBER_FORMAT_TWO_DIGIT_GROUPING_WITH_COMMA_DOT:
        sNewVal = sNewVal.replace(/,/g, '');
        break;
    }

    return sNewVal;
  };

  isOnlyNegativeSign = (sValue) => {
    return this.props.negativeAllowed ? (sValue === "-") : false;
  };

  handleOnChange =(oEvent)=>{
    let sValue = oEvent.target.value;
    let bIsOnlyInteger = this.props.isOnlyInteger;
    let oNumberFormat = ViewUtils.getSelectedNumberFormatByDataLanguage();
    let sSplitter = oNumberFormat.decimalSeparator;
    let iPrecision = this.props.precision;

    /** Don't allow decimal separator if only integer value is allowed **/
    if ((bIsOnlyInteger || iPrecision < 1) && CS.includes(sValue, sSplitter)) {
      return sValue.replace(sSplitter, "");
    } else if(iPrecision > 0 && CS.includes(sValue, sSplitter)){
      /** If digits before decimal removed then it should get replaced with 0 **/
      /** e.g. 1.23 => .23 => 0.23 **/
      let iValue =  sValue.split(sSplitter)[0];
      if(!iValue) {
        sValue = '0' + sSplitter + sValue.split(sSplitter)[1];
      }else if(iValue == "-"){
        sValue = '-0' + sSplitter + sValue.split(sSplitter)[1];
      }
    }

    let bDisableNumberLocaleFormatting = this.props.disableNumberLocaleFormatting;
    if (sValue === "" || this.testNumber(sValue) || this.isOnlyNegativeSign(sValue)) {
      sValue = ViewUtils.getNumberAccordingToPrecision(sValue, iPrecision);
      let sValueToSave = !bDisableNumberLocaleFormatting ? this.convertNumberToStandardFormat(sValue) : sValue;
      this.props.onChange ? this.props.onChange.call(this, sValueToSave) : this.setState({value: sValueToSave})
    }
  };

  handleOnKeyPress = (oEvent) => {
    if (this.props.handleEnterKeyPress && oEvent.key === 'Enter') {
      oEvent.target.blur();
    }
  };

  handleOnBlur =(oEvent)=> {
    var sOldValue = NumberLocaleView.validatedValue(this.props.value);
    var sValue = this.state.value;

    // CS.isNotEmpty(sOldValue) && (sOldValue = CS.toNumber(sOldValue));
    // CS.isNotEmpty(sValue) && (sValue = CS.toNumber(sValue));

    if (sValue === "-") {
      sValue = "";
    } else if (sValue === "-0") {
      sValue = "0";
    }

    let oNumberFormat = ViewUtils.getSelectedNumberFormatByDataLanguage();
    let sDecimalSeparator = oNumberFormat.decimalSeparator;

    /** Remove trailing decimal separator which doesn't followed by any digit **/
    sValue = CS.trimEnd(sValue, sDecimalSeparator);

    /** If minimum value is provided and it satisfies the condition then don't change the value */
    if ((this.props.minValue || this.props.minValue === 0) && sValue < this.props.minValue) {
      let sMessage = getTranslation().VALUE_MUST_BE_GREATER_THAN_OR_EQUAL_TO;
      alertify.error(ViewUtils.getDecodedTranslation(sMessage,{minValue: this.props.minValue}));
      sValue = sOldValue;
    }

    /** If maximum value is provided and it satisfies the condition then don't change the value */
    if ((this.props.maxValue || this.props.maxValue === 0) && sValue > this.props.maxValue) {
      let sMessage = getTranslation().VALUE_MUST_BE_LESS_THAN_OR_EQUAL_TO;
      alertify.error(ViewUtils.getDecodedTranslation(sMessage,{maxValue: this.props.maxValue}));
      sValue = sOldValue;
    }


    let oStateToChange = {
      isEditable: false
    };

    if(sOldValue !== sValue) {
      if (this.props.onBlur) {
        this.props.onBlur(sValue);
      } else {
        this.props.onChange && this.props.onChange(sValue);
      }
    } else if (this.state.value !== sValue) {
      //eg. case : sOldValue is "1", this.state.value is "1." and sValue is "1";
      //Blur is not called since value is not changed, but we need to change state by removing the "."
      oStateToChange.value = sValue;
    }

    if (CS.isFunction(this.props.onForceBlur)) {
      this.props.onForceBlur(sValue); //always call forceBlur when present
    }

    this.setState(oStateToChange);
  }

  testNumber =(sNumber)=>{
    let oNumberFormat = ViewUtils.getSelectedNumberFormatByDataLanguage();
    let sRegex = this.props.negativeAllowed ? oNumberFormat.negativeNumberRegex : oNumberFormat.positiveNumberRegex;

    return sRegex.test(sNumber);
  }

  /*getNumberAccordingToPrecision =(sValue)=> {
    let iPrecision = this.props.precision;
    let oNumberFormat = ViewLibraryUtils.getSelectedNumberFormatByDataLanguage();
    let sSplitter = oNumberFormat.decimalSeparator;

    if (sSplitter != null && (CS.isNumber(iPrecision) || iPrecision === 0)) {
      var aSplits = sValue.split(sSplitter);
      if (aSplits[1] && (aSplits[1].length > iPrecision)) {
        if(iPrecision === 0) {
          sValue = aSplits[0];
        } else {
          let sTruncatedValue = aSplits[1].substring(0, iPrecision);
          sValue = aSplits[0] + sSplitter + sTruncatedValue;
        }
      }
    }

    return sValue;
  };*/

  getView =()=> {
    var sValue = (this.state.value || this.state.value === 0) ? (this.state.value + "") : "";
    var __props = this.props;
    var sPlaceholder = __props.hintText || "";
    var bDisabled = __props.isDisabled;
    let bIsEditable = this.state.isEditable;
    var sInputClass = "numberLocaleInput ";
    let iPrecision = +this.props.precision;
    let bDisableNumberLocaleFormatting = this.props.disableNumberLocaleFormatting;

    if (bIsEditable) {
      sValue = this.getValueRequiredInEditMode(sValue); /** 1234.52 => 1234,52 **/
    }
    else {
      bDisabled && (sInputClass += "isDisabled ");
      sValue = ViewUtils.getValueToShowAccordingToNumberFormat(sValue, iPrecision, {}, bDisableNumberLocaleFormatting);
    }

    return (
          <input
            disabled={bDisabled}
            className={sInputClass}
            placeholder={sPlaceholder} value={sValue}
            onChange={this.handleOnChange} onBlur={this.handleOnBlur} onKeyPress={this.handleOnKeyPress}
            onFocus={this.handleInputOnFocus}
            autoFocus={this.props.isAutoFocus}
            onKeyDown={this.props.onKeyDown}
          />);
  };

  render() {

    return (<div className="numberLocaleViewContainer">{this.getView()}</div>);
  }
}

NumberLocaleView.propTypes = oPropTypes;

export const view = NumberLocaleView;
export const event = oEvents;
