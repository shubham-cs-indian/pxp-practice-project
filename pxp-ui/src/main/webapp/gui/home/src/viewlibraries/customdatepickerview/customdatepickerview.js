import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import {blue} from '@material-ui/core/colors';
import TooltipView from './../tooltipview/tooltip-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ViewLibraryUtils from '../utils/view-library-utils';
import IDatePickerView from './i-date-picker-view';
import Constants from '../../commonmodule/tack/constants';
import { DatePicker, MuiPickersUtilsProvider } from "@material-ui/pickers";
import MomentUtils from '@date-io/moment';

const oPropTypes = {
  value: ReactPropTypes.object,
  floatingLabelText: ReactPropTypes.string,
  ref: ReactPropTypes.string,
  key: ReactPropTypes.string,
  className: ReactPropTypes.string,
  defaultDate: ReactPropTypes.object,
  disabled: ReactPropTypes.bool,
  minDate: ReactPropTypes.object,
  maxDate: ReactPropTypes.object,
  allowInfinity: ReactPropTypes.bool,
  onChange: ReactPropTypes.func,
  style: ReactPropTypes.object,
  inputStyle: ReactPropTypes.object,
  textFieldStyle: ReactPropTypes.object,
  underlineStyle: ReactPropTypes.object,
  onFocus: ReactPropTypes.func,
  hintText: ReactPropTypes.string,
  errorText: ReactPropTypes.string,
  tooltip: ReactPropTypes.string,
  /*hideTooltip: ReactPropTypes.bool,*/
  endOfDay: ReactPropTypes.bool,
  disableClearDate: ReactPropTypes.bool,
  hideRemoveButton: ReactPropTypes.bool
};
/**
 * @class CustomDatePickerView - use for display datepicker view to Application.
 * @memberOf Views
 * @property {object} [value] -  value for selected date.
 * @property {string} [floatingLabelText] -  sting which contain value of floatingLabelText.
 * @property {string} [ref] -  ref.
 * @property {string} [key] -  string which contain key.
 * @property {string} [className] -  classname as a string.
 * @property {object} [defaultDate] -  defaultdate (ex. today's date).
 * @property {bool} [disabled] -  boolean which is used for show or hide disabled.
 * @property {object} [minDate] - htis prop pass min date.
 * @property {object} [maxDate] -  max date.
 * @property {bool} [allowInfinity] -  boolean which is used for alloInfinity true or false.
 * @property {func} [onChange] -  function which is used for onChange event.
 * @property {object} [style] -  style object which is used for style.
 * @property {object} [inputStyle] -  style object which is used for input style.
 * @property {object} [textFieldStyle] -  style object which is used for textFiled style.
 * @property {object} [underlineStyle] -  style object which is used for giving underline to any element.
 * @property {func} [onFocus] -  function which is used for on focus event.
 * @property {string} [hintText] -   string of hint text.
 * @property {string} [errorText] -  string of error text.
 * @property {string} [tooltip] -  classname as a string.
 * @property {bool} [hideTooltip] -  boolean which is used for hide tooltip or not.
 * @property {bool} [endOfDay] -  boolean which is used for show or hide days.
 * @property {bool} [disableClearDate] -  boolean which is used for show or hide color.
 * @property {bool} [hideRemoveButton] -  boolean which is used for show or hide Remove button.
 */

// @CS.SafeComponent
class CustomDatePickerView extends IDatePickerView {

  isClose = false;
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.updateDOM();
  }

  componentDidUpdate() {
    // this.updateDOM();
  }

  shouldComponentUpdate(oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  }

  updateDOM() {
    var oDOM = ReactDOM.findDOMNode(this);
    //$(oDOM).find('input').attr('readonly', 'readonly');
  }

  getCustomMaterialStyles =()=>{

    return {
      inputStyles: {
        "fontSize": "14px"
      },

      floatingLabelStyles: {
        "fontSize": "14px",
        "color": "#777"
      },

      underlineStyle: {
        color: '#ccc',
        borderWidth: '1px'
      },

      underlineFocusStyle: {
        color: blue["300"],
        borderWidth: '1px'
      }
    }
  }

  formatDate =(date)=>{
    date = Date.parse(date) ? new Date(date) : '';
    return CS.isDate(date) ? ViewLibraryUtils.getDateAttributeInTimeFormat(date): '';
    //return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate();
  }


  getFormat = () => {
    return ViewLibraryUtils.getStandardDateTimeFormat()
  }

  onChange =(oMoment)=> {
    let bIsEndOfDay = this.props.endOfDay;
    let oDate = oMoment.toDate();
    oDate.setHours(0, 0, 0, 0);
    if (bIsEndOfDay) {
      oDate.setHours(23, 59, 59, 999);
    }

    if (this.props.onChange) {
      this.props.onChange(null, oDate);
    }
  };

  handleInfinityClicked =()=> {
    var bInfinitySelected = this.isInfinitySelected();
    if(bInfinitySelected) {
      if(this.props.onChange) {
        this.props.onChange("", null);
      }
    } else {
      if(this.props.onChange) {
        this.props.onChange("", new Date(Constants.INFINITE_DATE));
      }
    }
  }

  clearDateField =()=> {
    this.props.onChange("", "");
  }

  isInfinitySelected =()=> {
    var oValue = this.props.value;
    return Date.parse(oValue) === Constants.INFINITE_DATE;
  }

/*  openCalendar = (e) => {
    if(this.datePicker) {
      this.datePicker.open(e);
    }
  }

  handleRef = (e) => {
    if(e) {
      this.datePicker = e;
    }
  };*/

  render() {
    var __props = this.props;
    // var sLabel = __props.floatingLabelText || "";
    // var sRef = __props.ref;
    // var sKey = __props.key;
    var sClassName = __props.className;
    // var sDefaultValue = __props.defaultDate;
    var bIsDisabled = __props.disabled;
    var bIsPastDisabled = __props.disablePast;
    var oChangeHandler = this.onChange;
    // var oStyle = __props.style;
    // var oInputStyle = __props.inputStyle;
    // var oTextFieldStyle = __props.textFieldStyle;
    var oValue = __props.value;
    var fOnFocus = __props.onFocus;
    var oMinDate = __props.minDate ? ViewLibraryUtils.getMomentOfDate(__props.minDate).startOf('day') : undefined;
    var oMaxDate = __props.maxDate;
    var sHintText = !CS.isEmpty(__props.hintText) ? __props.hintText : "";
    // var sErrorText = __props.errorText || "";
    var sDatePickerEmptyButtonClassName = "customDatePickerEmptyButton ";
    // var sTextType = __props.textType || "text";
    sDatePickerEmptyButtonClassName += ((oValue ==  null || this.props.disableClearDate || this.props.hideRemoveButton) ? "noShow " : "");

    var sTitle = "";
    if(__props.tooltip){
      sTitle = __props.tooltip;
    }else{
      sTitle += oValue ==  null ? "" : /*" : " + */this.formatDate(oValue);
    }

    // var oCustomStyles = this.getCustomMaterialStyles();
    //
    // var oErrorStyle = {color: '#555', borderWidth: '1px', fontStyle: 'italic'};
    // var oUnderlineStyle = this.props.underlineStyle ? this.props.underlineStyle : {color: '#ccc', borderWidth: '1px'};
    // var oUnderlineFocusStyle = {color: blue["300"], borderWidth: '1px'};

    // var oDatePickerLabel = null;
    var oInfinityButton = null;
    var bInfinitySelected = this.isInfinitySelected();
    if (this.props.allowInfinity && !bIsDisabled) {
      var sInfinityClassName = bInfinitySelected ? "infinityButton selected " : "infinityButton";
      oInfinityButton = (
          <TooltipView placement="bottom" label={getTranslation().INFINITE}>
            <div className={sInfinityClassName} onClick={this.handleInfinityClicked}></div>
          </TooltipView>
      );
    }

    bInfinitySelected && (bIsDisabled = true);

    if(bIsDisabled){
      /*oDatePickerLabel = <div className="customDatePickerDisabledLabel">{sLabel}</div>;
      oTooltipDOM = (!this.props.hideTooltip) ?
          <TooltipView placement="bottom" label={sLabel}>
            {oDatePickerLabel}
          </TooltipView> : oDatePickerLabel;*/

      return (
          <div className="customDatePickerWrapper disabled" title={sTitle}>
            {/*{oTooltipDOM}*/}
            <div className="customDatePickerDisabledValue">{this.formatDate(oValue) || sHintText}</div>
            {oInfinityButton}
          </div>
      )

    } else {
        /**
        * local contact archana
*/



      sClassName = (oValue && !CS.isEmpty(oValue.toString()))? "customDatePicker " + sClassName : "customDatePicker emptyDate " + sClassName;
      /* used custom_placeholder classname to resolve browser issue(library issue) for cursor in placeholder */
      let sPlaceholder = (oValue && !CS.isEmpty(oValue.toString()))? "" :sHintText;
      const oDatePicker = (
        <div className="customDatePickerWrapper custom_placeholder" data-placeholder={sPlaceholder}>
          <MuiPickersUtilsProvider utils={MomentUtils}>
            <DatePicker
                variant="inline"
                // ref={this.handleRef}
                className={sClassName}
                //defaultDate={sDefaultValue}
                disabled={bIsDisabled}
                disablePast={bIsPastDisabled}
                //floatingLabelStyle={oCustomStyles.floatingLabelStyles}
                onChange={oChangeHandler}
                // onClose={this.onClose}
                format={this.getFormat().dateFormat}
                // style={oStyle}
                // showyearselector="true"
                // DateTimeFormat={Intl.DateTimeFormat}
                // locale={ViewLibraryUtils.getCurrentLocale()}
                value={oValue}
                autoOk
                minDate={oMinDate}
                minDateMessage= ""
                maxDate={oMaxDate}
                onOpen={fOnFocus}
                // errorText={sErrorText}
                // errorStyle={oErrorStyle}
                // inputStyle={oInputStyle || oCustomStyles.inputStyles}
                // underlineStyle={oUnderlineStyle}
                // underlineFocusStyle={oUnderlineFocusStyle}
                // textFieldStyle={oTextFieldStyle}
                // InputProps={{
                //   disableUnderline: true,
                //   type: sTextType
                // }}
            />
          </MuiPickersUtilsProvider>
          <TooltipView placement="bottom" label={getTranslation().REMOVE}>
            <div className={sDatePickerEmptyButtonClassName} onClick={this.clearDateField}></div>
          </TooltipView>
          {oInfinityButton}
        </div>
      );

      return (oDatePicker);
    }
  }
}



CustomDatePickerView.propTypes = oPropTypes;

export const view = CustomDatePickerView;
export const events = {};
