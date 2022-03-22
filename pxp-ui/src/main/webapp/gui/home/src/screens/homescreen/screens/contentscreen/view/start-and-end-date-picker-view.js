import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as CustomDateTimePickerView } from '../../../../../viewlibraries/customdatetimepickerview/custom-datetime-picker-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;
const oEvents = {
  CONTENT_EVENTS_FIELD_CHANGED: "content_events_field_changed",
};

const oPropTypes = {
  context: ReactPropTypes.string,
  eventModel: ReactPropTypes.object,
  isDisabled: ReactPropTypes.bool,
};

// @CS.SafeComponent
class StartAndEndDatePickerView extends React.Component {
  static propTypes = oPropTypes;

  constructor(props) {
    super(props);
    let oEventModel = props.eventModel;

    this.state = {
      startTime: oEventModel.startTime,
      endTime: oEventModel.endTime
    };
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    let oEventModel = oNextProps.eventModel;
    return({
      startTime: oEventModel.startTime,
      endTime: oEventModel.endTime
    });
  }

  handleEventFieldChanged = (sKey, sValue) => {
    EventBus.dispatch(oEvents.CONTENT_EVENTS_FIELD_CHANGED, this.props.context, sKey, sValue);
  };

  handleEventDateTimeFieldChanged = (sKey, oDate) => {
    var sValue = oDate ? oDate.getTime() : "";
    sValue = sValue === "" || sValue === NaN ? null : sValue;

    var oTemp = {};
    oTemp[sKey] = sValue;
    this.setState(oTemp);
    this.handleEventFieldChanged(sKey, sValue);
  };

  getDateTimeSelectorBlock = () => {
    let bIsDisabled = this.props.isDisabled;
    let oEventModel = this.props.eventModel;
    let sTimeFormat = "24hr";
    var oTextFieldStyle = {
      "width": "100%",
      "height": "30px"
    };

    let oFromMinMaxDate = {
      minDate: this.state.startTime ? new Date(this.state.startTime) : new Date(),
      maxDate: this.state.endTime ? new Date(this.state.endTime) : undefined,
    };
    let oToMinMaxDate = {
      minDate: this.state.startTime ? new Date(this.state.startTime) : new Date(),
      maxDate: undefined
    };

    var oUnderlineStyle = {};
    return (<div className="repeatHandlerWrapper">
      <div className="startEndDateFieldGroup">
        <div className="eventField">
          <div className="fieldLabel">{getTranslation().FROM}</div>
          <div className="value">
            <CustomDateTimePickerView
                value={oEventModel.startTime}
                minDate={oFromMinMaxDate.minDate}
                maxDate={oFromMinMaxDate.maxDate}
                textFieldStyle={oTextFieldStyle}
                underlineStyle={oUnderlineStyle}
                inputStyle={oTextFieldStyle}
                onChange={this.handleEventDateTimeFieldChanged.bind(this, "startTime")}
                timeFormat={sTimeFormat}
                disabled={bIsDisabled}
                hintText={getTranslation().START_DATE}
                hideRemoveButton={oEventModel.hideRemoveButton}/>
          </div>
        </div>
        <div className="eventField">
          <div className="fieldLabel">{getTranslation().TO}</div>
          <div className="value">
            <CustomDateTimePickerView
                value={oEventModel.endTime}
                minDate={oToMinMaxDate.minDate}
                maxDate={oToMinMaxDate.maxDate}
                textFieldStyle={oTextFieldStyle}
                underlineStyle={oUnderlineStyle}
                inputStyle={oTextFieldStyle}
                onChange={this.handleEventDateTimeFieldChanged.bind(this, "endTime")}
                endOfDay={true}
                timeFormat={sTimeFormat}
                disabled={bIsDisabled}
                hintText={getTranslation().END_DATE}
                hideRemoveButton={oEventModel.hideRemoveButton}/>
          </div>
        </div>
      </div>
    </div>)
  }

  render() {
    var sSectionTitle = '';

    if (this.props.context == "asset") {
      sSectionTitle = getTranslation().VALIDITY_INFO;
    }

    var oDateTimeBlock = this.getDateTimeSelectorBlock();
    return (
        <div className="startAndEndDatePickerView">
          <div className="sectionTitle">{sSectionTitle}</div>
          <div className="startAndEndDateViewWrapper">
            {oDateTimeBlock}
          </div>
        </div>
    );
  }
}

export const view = StartAndEndDatePickerView;
export const events = oEvents;
