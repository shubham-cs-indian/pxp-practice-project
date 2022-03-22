import CS from '../../libraries/cs';
import React from 'react';
import alertify from '../../commonmodule/store/custom-alertify-store';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../tooltipview/tooltip-view';
import ViewLibraryUtils from '../utils/view-library-utils';
import { DateTimePicker, MuiPickersUtilsProvider } from "@material-ui/pickers";
import MomentUtils from '@date-io/moment';

const oPropTypes = {
  minDate: ReactPropTypes.object,
  maxDate: ReactPropTypes.object,
  textFieldStyle: ReactPropTypes.object,
  underlineStyle: ReactPropTypes.object,
  disabled: ReactPropTypes.bool,
  endOfDay: ReactPropTypes.bool,
  value: ReactPropTypes.object,
  datetimeDefaultValue: ReactPropTypes.object,
  onChange: ReactPropTypes.func,
  timeFormat: ReactPropTypes.string,
  hintText : ReactPropTypes.string,
  hideRemoveButton: ReactPropTypes.bool
};
/**
 * @class CustomDateTimePickerView - use for display datepicker and timepicker view to Application.
 * @memberOf Views
 * @property {object} [minDate] - htis prop min date.
 * @property {object} [maxDate] -  max date.
 * @property {object} [textFieldStyle] -  style object which is used for textFiled style.
 * @property {object} [underlineStyle] -  style object which is used for giving underline to any element.
 * @property {bool} [disabled] -  boolean which is used for show or hide disabled.
 * @property {bool} [endOfDay] -  boolean which is used for show or hide days.
 * @property {object} [value] -  value for selected date.
 * @property {object} [datetimeDefaultValue] - n object of datetimeDefaultValue.
 * @property {func} [onChange] -  function which is used for onChange event.
 * @property {string} [timeFormat] -   string of timeformat.
 * @property {string} [hintText] -   string of hint text.
 * @property {bool} [hideRemoveButton] -  boolean which is used for show or hide Remove button.
 */

// @CS.SafeComponent
class CustomDateTimePickerView extends React.Component {

  constructor(props) {
    super(props);
  }

  getValidDateTime =(oDate)=> {
    if(oDate=="Invalid Date") {
      return oDate;
    }
    var iSelectedDateTime = oDate.getTime();
    var iMinDate = this.props.minDate;
    var iMaxDate = this.props.maxDate;
    var sMinDate = iMinDate ? iMinDate.getTime() : iMinDate;
    var sMaxDate = iMaxDate ? iMaxDate.getTime() : iMaxDate;
    if (sMinDate <= iSelectedDateTime && !sMaxDate) {
      return oDate;
    }
    else if (sMaxDate >= iSelectedDateTime && !sMinDate) {
      return oDate;
    }
    else if (sMaxDate >= iSelectedDateTime && sMinDate <= iSelectedDateTime) {
      return oDate;
    }
    else if (!sMaxDate && !sMinDate) {
      return oDate;
    }
    else if (sMinDate > iSelectedDateTime && sMaxDate > iSelectedDateTime){
      alertify.message(getTranslation().START_DATE_MUST_BE_GREATER_THAN_CURRENT_DATE);
      return iMinDate;
    }
    else if (sMaxDate <= iSelectedDateTime && sMinDate <= iSelectedDateTime){
      alertify.message(getTranslation().START_DATE_MUST_BE_LESS_THAN_END_DATE);
      return iMaxDate;
    }
    else if (sMaxDate <= iSelectedDateTime && !iMinDate) {
      alertify.message(getTranslation().START_DATE_MUST_BE_LESS_THAN_END_DATE);
      return iMaxDate;
    }
    else {
      alertify.message(getTranslation().START_DATE_MUST_BE_LESS_THAN_END_DATE);
      return iMinDate;
    }
  }

  handleDateTimeValueChanged =(oMoment)=> {
    let oDate = oMoment.toDate();
    if (this.props.onChange) {
      this.props.onChange(this.getValidDateTime(oDate));
    }
  };

  getFormat = () => {
    return ViewLibraryUtils.getStandardDateTimeFormat()
  }

  clearDateField =()=> {
    this.props.onChange("", "");

  }

  formatDateTime =(date)=>{
    date = Date.parse(date) ? new Date(date) : '';
    let oDateTime = CS.isDate(date) ? ViewLibraryUtils.getDateAttributeInDateTimeFormat(date): '';
    let sDateTime = '';
    if(CS.isNotEmpty(oDateTime)){
      sDateTime = oDateTime.date + " " + oDateTime.time;
    }
    return sDateTime;
  }

  render() {
    var __props = this.props;
    var oValue = __props.value;
    let oDateTimeFormat = this.getFormat();
    let sClassName = (oValue && !CS.isEmpty(oValue.toString()))? "customDateTimePicker" : "customDateTimePicker emptyDate";
    var sDatePickerEmptyButtonClassName = "customDatePickerEmptyButton ";
    var sHintText = !CS.isEmpty(__props.hintText) ? __props.hintText : getTranslation().DATE_AND_TIME;
    let sPlaceholder = CS.isNull(oValue) ? sHintText : null;
    sDatePickerEmptyButtonClassName += ((oValue ==  null || this.props.disableClearDate || this.props.hideRemoveButton) ? "noShow " : "");

    if(__props.disabled){
      let sValue = this.formatDateTime(oValue) || sHintText;
      return (
        <TooltipView placement="bottom" label={sValue}>
          <div className="customDateTimePickerWrapper disabled">
            <div className="customDateTimePickerDisabledValue">{sValue}</div>
          </div>
        </TooltipView>
      )
    }

    /* used custom_placeholder classname to resolve browser issue(library issue) for cursor in placeholder */
    return (
        <div className="dateTimePickerContainer custom_placeholder" data-placeholder={sPlaceholder}>
          <MuiPickersUtilsProvider utils={MomentUtils}>
            <DateTimePicker
                variant="inline"
                value={oValue}
                autoOk
                onChange={this.handleDateTimeValueChanged}
                //onClose={this.onClose}
                className={sClassName}
                defaultTime={oValue}
                disabled={__props.disabled}
                minDate={__props.minDate}
                maxDate={__props.maxDate}
                format={oDateTimeFormat.dateFormat + " " +oDateTimeFormat.timeFormat}
                InputProps={{
                  disableUnderline: true,
                }}
            />
          </MuiPickersUtilsProvider>
          <TooltipView placement="bottom" label={getTranslation().REMOVE}>
            <div className={sDatePickerEmptyButtonClassName} onClick={this.clearDateField}></div>
          </TooltipView>
        </div>
    );
  }

}

CustomDateTimePickerView.propTypes = oPropTypes;

export const view = CustomDateTimePickerView;
export const events = {};
