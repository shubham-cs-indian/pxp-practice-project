import CS from '../../libraries/cs';
import React from 'react';
import DateRangePicker from 'react-bootstrap-daterangepicker';
import ReactPropTypes from "prop-types";
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-daterangepicker/daterangepicker.css';

var oEvents = {
  CUSTOM_DATE_RANGE_PICKER_HANDLE_APPLY_BUTTON_CLICKED: "custom_date_range_picker_handle_apply_button_clicked",
  CUSTOM_DATE_RANGE_PICKER_HANDLE_CANCEL_BUTTON_CLICKED:"custom_date_range_picker_handle_cancel_button_clicked"
};

const oPropTypes = {
  context: ReactPropTypes.string,
  startDate: ReactPropTypes.string,
  endDate: ReactPropTypes.string,
  ranges: ReactPropTypes.object,
  dateRangeLocale: ReactPropTypes.object,
  timePicker24Hour: ReactPropTypes.bool,
  timePicker: ReactPropTypes.bool,
  timePickerSeconds: ReactPropTypes.bool,
  showDropdowns: ReactPropTypes.bool,
  calendarOpenDirection: ReactPropTypes.string,
  alwaysShowCalendars: ReactPropTypes.bool,
  singleDatePicker: ReactPropTypes.bool,
  onShowCalendar: ReactPropTypes.func,
  onApply: ReactPropTypes.func,
  onCancel: ReactPropTypes.func,
  labelVsIdMap: ReactPropTypes.object
};


// @CS.SafeComponent
class DateRangePickerView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);
    this.state = {
      startDate: this.props.startDate,
      endDate: this.props.endDate
    };
  }

  handleSelect = (oEvent, oPicker) => {
    if (oPicker.startDate && oPicker.endDate) {
      let oLabelVsIdMap = this.props.labelVsIdMap || {};
      let sId = oPicker.chosenLabel === getTranslation().DEFINE_PERIOD ? "custom" : oLabelVsIdMap[oPicker.chosenLabel];
      let oSelectedTimeRange = {
        startDate: oPicker.startDate,
        endDate: oPicker.endDate
      };

    if (CS.isFunction(this.props.onApply)) {
      this.props.onApply(oSelectedTimeRange);
    } else {
      EventBus.dispatch(oEvents.CUSTOM_DATE_RANGE_PICKER_HANDLE_APPLY_BUTTON_CLICKED, this.props.context, oSelectedTimeRange, sId);
    }
    }
  };

  handleCanceled () {
    if (CS.isFunction(this.props.onCancel)) {
      this.props.onCancel();
    } else {
      EventBus.dispatch(oEvents.CUSTOM_DATE_RANGE_PICKER_HANDLE_CANCEL_BUTTON_CLICKED, this.props.context);
    }
  }

  render () {
    return (
        <DateRangePicker startDate={this.props.startDate}
                         endDate={this.props.endDate}
                         ranges={this.props.ranges}
                         timePicker24Hour={this.props.timePicker24Hour || false}
                         timePicker={this.props.timePicker}
                         timePickerSeconds={this.props.timePickerSeconds || false}
                         onApply={this.handleSelect.bind(this)}
                         locale={this.props.dateRangeLocale}
                         showDropdowns={this.props.showDropdowns}
                         opens={this.props.calendarOpenDirection}
                         alwaysShowCalendars={this.props.alwaysShowCalendars}
                         singleDatePicker={this.props.singleDatePicker}
                         linkedCalendars= {false}
                         onCancel={this.handleCanceled.bind(this)}>
          {this.props.children}
        </DateRangePicker>
    );
  }
}
DateRangePickerView.propType =oPropTypes;

export const view = DateRangePickerView;
export const events = oEvents;
