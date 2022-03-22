import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import MockDataForProcessFrequencyDictionary from '../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-frequency-dictionary'
import {view as NewMultiSelectSearchView} from "../multiselectview/multi-select-search-view";
import {view as CustomDatePicker} from "../customdatepickerview/customdatepickerview";

const oEvents = {
  PROCESS_FREQUENCY_SUMMARY_DATE_BUTTON_CLICKED: "process_frequency_summary_date_button_clicked"
};

const oPropTypes = {
  selectedTabId: ReactPropTypes.string,
  data: ReactPropTypes.object,
};

// @CS.SafeComponent
class ProcessFrequencySummaryView extends React.Component {
  constructor(props) {
    super(props);
  }

  handleDateChanged = (sContext, oEvent, sValue) => {
    let sDate = (sValue.getMonth() + 1) + "/" + sValue.getDate() + "/" + sValue.getFullYear();
    EventBus.dispatch(oEvents.PROCESS_FREQUENCY_SUMMARY_DATE_BUTTON_CLICKED, sDate, sContext);
  }

  getDurationView = (oData) => {
    let oDurationView = null;
    let oHoursData = oData.hours;
    let oMinsData = oData.mins;
    oDurationView =
        <div className="frequency">
          <label className="frequencyLabel">After</label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oHoursData.isMultiSelect}
                disabled={oHoursData.disabled}
                label={CS.getLabelOrCode(oHoursData)}
                items={oHoursData.items}
                selectedItems={oHoursData.selectedItems}
                context={oHoursData.context}
                cannotRemove={oHoursData.cannotRemove}
                onApply={oHoursData.onApply}
            />
          </div>
          <label className="frequencyLabel">hour(s)</label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oMinsData.isMultiSelect}
                disabled={oMinsData.disabled}
                label={CS.getLabelOrCode(oMinsData)}
                items={oMinsData.items}
                selectedItems={oMinsData.selectedItems}
                context={oMinsData.context}
                cannotRemove={oMinsData.cannotRemove}
                onApply={oMinsData.onApply}
            />
          </div>
          <label className="frequencyLabel">minute(s)</label>
        </div>
    return oDurationView;
  };

  getDateView = (oData) => {
    let oDateView = null;
    let oDateData = oData.date;
    let oHoursData = oData.hours;
    let oMinsData = oData.mins;
    oDateView =
        <div className="frequency">
          <label className="frequencyLabel">Date</label>
          <div className="frequencyMSSView date">
            <CustomDatePicker
                value={oDateData}
                className=" datePickerCustomInProcessFrequency "
                disabled={false}
                hideRemoveButton={true}
                hintText=""
                onChange={this.handleDateChanged.bind(this,"date")}
                endOfDay={false}
                minDate={new Date()}/>
          </div>
          <label className="frequencyLabel">Time</label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oHoursData.isMultiSelect}
                disabled={oHoursData.disabled}
                label={CS.getLabelOrCode(oHoursData)}
                items={oHoursData.items}
                selectedItems={oHoursData.selectedItems}
                context={oHoursData.context}
                cannotRemove={oHoursData.cannotRemove}
                onApply={oHoursData.onApply}
            />
          </div>
          <label className="frequencyLabel">hour(s)</label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oMinsData.isMultiSelect}
                disabled={oMinsData.disabled}
                label={CS.getLabelOrCode(oMinsData)}
                items={oMinsData.items}
                selectedItems={oMinsData.selectedItems}
                context={oMinsData.context}
                cannotRemove={oMinsData.cannotRemove}
                onApply={oMinsData.onApply}
            />
          </div>
          <label className="frequencyLabel">minute(s)</label>
        </div>
    return oDateView;
  };

  getDailyView = (oData) => {
    let oDailyView = null;
    let oHoursData = oData.hours;
    let oMinsData = oData.mins;
    oDailyView =
        <div className="frequency">
          <label className="frequencyLabel">Every Day at </label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oHoursData.isMultiSelect}
                disabled={oHoursData.disabled}
                label={CS.getLabelOrCode(oHoursData)}
                items={oHoursData.items}
                selectedItems={oHoursData.selectedItems}
                context={oHoursData.context}
                cannotRemove={oHoursData.cannotRemove}
                onApply={oHoursData.onApply}
            />
          </div>
          <label className="frequencyLabel">hour(s)</label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oMinsData.isMultiSelect}
                disabled={oMinsData.disabled}
                label={CS.getLabelOrCode(oMinsData)}
                items={oMinsData.items}
                selectedItems={oMinsData.selectedItems}
                context={oMinsData.context}
                cannotRemove={oMinsData.cannotRemove}
                onApply={oMinsData.onApply}
            />
          </div>
          <label className="frequencyLabel">minute(s)</label>
        </div>
    return oDailyView;
  };

  getHourMinView = (oData) => {
    let oHourMinView = null;
    let oHoursData = oData.hours;
    let oMinsData = oData.mins;
    oHourMinView =
        <div className="frequency">
          <label className="frequencyLabel">Every </label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oHoursData.isMultiSelect}
                disabled={oHoursData.disabled}
                label={CS.getLabelOrCode(oHoursData)}
                items={oHoursData.items}
                selectedItems={oHoursData.selectedItems}
                context={oHoursData.context}
                cannotRemove={oHoursData.cannotRemove}
                onApply={oHoursData.onApply}
            />
          </div>
          <label className="frequencyLabel">hour(s)</label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oMinsData.isMultiSelect}
                disabled={oMinsData.disabled}
                label={CS.getLabelOrCode(oMinsData)}
                items={oMinsData.items}
                selectedItems={oMinsData.selectedItems}
                context={oMinsData.context}
                cannotRemove={oMinsData.cannotRemove}
                onApply={oMinsData.onApply}
            />
          </div>
          <label className="frequencyLabel">minute(s)</label>
        </div>
    return oHourMinView;
  };

  getWeeklyView = (oData) => {
    let oWeeklyView = null;
    let oDaysOfWeekData = oData.daysOfWeeks;
    let oHoursData = oData.hours;
    let oMinsData = oData.mins;
    oWeeklyView =
        <div className="frequency">
          <label className="frequencyLabel">Select Day(s)</label>
          <div className="frequencyMSSView day">
            <NewMultiSelectSearchView
                isMultiSelect={oDaysOfWeekData.isMultiSelect}
                disabled={oDaysOfWeekData.disabled}
                label={CS.getLabelOrCode(oDaysOfWeekData)}
                items={oDaysOfWeekData.items}
                selectedItems={oDaysOfWeekData.selectedItems}
                context={oDaysOfWeekData.context}
                cannotRemove={oDaysOfWeekData.cannotRemove}
                onApply={oDaysOfWeekData.onApply}
            />
          </div>
            <label className="frequencyLabel">Starts at</label>
            <div className="frequencyMSSView">
              <NewMultiSelectSearchView
                  isMultiSelect={oHoursData.isMultiSelect}
                  disabled={oHoursData.disabled}
                  label={CS.getLabelOrCode(oHoursData)}
                  items={oHoursData.items}
                  selectedItems={oHoursData.selectedItems}
                  context={oHoursData.context}
                  cannotRemove={oHoursData.cannotRemove}
                  onApply={oHoursData.onApply}
              />
            </div>
            <label className="frequencyLabel">hour(s)</label>
            <div className="frequencyMSSView">
              <NewMultiSelectSearchView
                  isMultiSelect={oMinsData.isMultiSelect}
                  disabled={oMinsData.disabled}
                  label={CS.getLabelOrCode(oMinsData)}
                  items={oMinsData.items}
                  selectedItems={oMinsData.selectedItems}
                  context={oMinsData.context}
                  cannotRemove={oMinsData.cannotRemove}
                  onApply={oMinsData.onApply}
              />
            </div>
            <label className="frequencyLabel">minute(s)</label>
        </div>

    return oWeeklyView;
  };

  getMonthlyView = (oData) => {
    let oMonthlyView = null;
    let oDaysData = oData.days;
    let oMonthsData = oData.months;
    oMonthlyView =
        <div className="frequency">
          <label className="frequencyLabel">Day</label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oDaysData.isMultiSelect}
                disabled={oDaysData.disabled}
                label={CS.getLabelOrCode(oDaysData)}
                items={oDaysData.items}
                selectedItems={oDaysData.selectedItems}
                context={oDaysData.context}
                cannotRemove={oDaysData.cannotRemove}
                onApply={oDaysData.onApply}
            />
          </div>
          <label className="frequencyLabel">of every</label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oMonthsData.isMultiSelect}
                disabled={oMonthsData.disabled}
                label={CS.getLabelOrCode(oMonthsData)}
                items={oMonthsData.items}
                selectedItems={oMonthsData.selectedItems}
                context={oMonthsData.context}
                cannotRemove={oMonthsData.cannotRemove}
                onApply={oMonthsData.onApply}
            />
          </div>
          <label className="frequencyLabel">months</label>
        </div>
    return oMonthlyView;
  };

  getYearlyView = (oData) => {
    let oYearlyView = null;
    let oDaysData = oData.days;
    let oMonthsOfYearData = oData.monthsOfYear;
    oYearlyView =
        <div className="frequency">
          <label className="frequencyLabel">Every </label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oMonthsOfYearData.isMultiSelect}
                disabled={oMonthsOfYearData.disabled}
                label={CS.getLabelOrCode(oMonthsOfYearData)}
                items={oMonthsOfYearData.items}
                selectedItems={oMonthsOfYearData.selectedItems}
                context={oMonthsOfYearData.context}
                cannotRemove={oMonthsOfYearData.cannotRemove}
                onApply={oMonthsOfYearData.onApply}
            />
          </div>
          <label className="frequencyLabel">of </label>
          <div className="frequencyMSSView">
            <NewMultiSelectSearchView
                isMultiSelect={oDaysData.isMultiSelect}
                disabled={oDaysData.disabled}
                label={CS.getLabelOrCode(oDaysData)}
                items={oDaysData.items}
                selectedItems={oDaysData.selectedItems}
                context={oDaysData.context}
                cannotRemove={oDaysData.cannotRemove}
                onApply={oDaysData.onApply}
            />
          </div>
        </div>
    return oYearlyView;
  };


  /** Generate Views Based on the Selected Tab Id **/
  getView = () => {
    let oView = null;
    let sSelectedTabId = this.props.selectedTabId;
    let oData = this.props.data;
    switch (sSelectedTabId) {
      case MockDataForProcessFrequencyDictionary.DURATION :
        oView = this.getDurationView(oData);
        break;
      case MockDataForProcessFrequencyDictionary.DATE :
        oView = this.getDateView(oData);
        break;
      case MockDataForProcessFrequencyDictionary.DAILY :
        oView = this.getDailyView(oData);
        break;
      case MockDataForProcessFrequencyDictionary.HOURMIN :
        oView = this.getHourMinView(oData);
        break;
      case MockDataForProcessFrequencyDictionary.WEEKLY :
        oView = this.getWeeklyView(oData);
        break;
      case MockDataForProcessFrequencyDictionary.MONTHLY :
        oView = this.getMonthlyView(oData);
        break;
      case MockDataForProcessFrequencyDictionary.YEARLY :
        oView = this.getYearlyView(oData);
        break;
    }

    return oView;
  };

  render () {
    let oView = this.getView();
    return (
        <div className="processfrequencySummaryView">
          {oView}
        </div>
    );
  }
}

ProcessFrequencySummaryView.propTypes = oPropTypes;

export const view = ProcessFrequencySummaryView;
export const events = oEvents;
