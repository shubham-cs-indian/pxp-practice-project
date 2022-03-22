import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as CustomDatePicker } from '../customdatepickerview/customdatepickerview.js';
import TooltipView from '../tooltipview/tooltip-view';
import Constants from '../../commonmodule/tack/constants';
import ViewLibraryUtils from '../utils/view-library-utils';

const oEvents = {
};

const oPropTypes = {
  startDate: ReactPropTypes.number,
  endDate: ReactPropTypes.number,
  showCurrentDateButton: ReactPropTypes.bool,
  onApply: ReactPropTypes.func.isRequired,
};
/**
 * @class NewDateRangeSelector - NewDateRangeSelector View used to select date range from to till.(for example: 01/01/2018 to 31/12/2018.)
 * @memberOf Views
 * @property {number} [startDate] - Start date for DateRangeSelector view.
 * @property {number} [endDate] - End date for DateRangeSelector view
 * @property {bool} [showCurrentDateButton] - Used to displaying CurrentDateButton, CurrentDateButton is used to display or hide currently selected date range.
 * @property {func} onApply - Contain function which execute on applying date range.
 */

// @CS.SafeComponent
class NewDateRangeSelector extends React.Component {

  constructor(props) {
    super(props);
    let bIsCurrentDate = NewDateRangeSelector.currentDateDecider(props.startDate, props.endDate);

    this.state = {
      startDate: props.startDate || null,
      endDate: props.endDate || null,
      isCurrentDate: bIsCurrentDate,
      isInfinityDate: false
    }
  }

  /*componentWillMount() {
    var bIsCurrentDate = this.currentDateDecider(this.props.startDate, this.props.endDate);
    this.setState({
      isCurrentDate: bIsCurrentDate
    });
  }*/

  static getDerivedStateFromProps (oNextProps, oState) {
    let bIsCurrentDate = NewDateRangeSelector.currentDateDecider(oNextProps.startDate, oNextProps.endDate);
    return {
      startDate: oNextProps.startDate,
      endDate: oNextProps.endDate,
      isCurrentDate: bIsCurrentDate
    };
  }

  /*componentWillReceiveProps(oNextProps) {
    var bIsCurrentDate = this.currentDateDecider(oNextProps.startDate, oNextProps.endDate);
    this.setState({
      startDate: oNextProps.startDate,
      endDate: oNextProps.endDate,
      isCurrentDate: bIsCurrentDate
    });
  }*/

  static currentDateDecider =(iStartDate , iEndDate )=> {
    if(!iStartDate || !iEndDate) {
      return false;
    }

    var iCurrentStartDate = ViewLibraryUtils.getMomentOfDate().startOf('day').valueOf();

    var oStartDate = ViewLibraryUtils.getMomentOfDate(iStartDate);
    var iStartOfStartDate = oStartDate.startOf("day").valueOf();
    var oEndDate = ViewLibraryUtils.getMomentOfDate(iEndDate);
    var iStartOfEndDate = oEndDate.startOf("day").valueOf();

    return (iStartOfEndDate === iCurrentStartDate && iStartOfStartDate === iCurrentStartDate);
  }

  handleCalendarRangeClear =()=> {

    if (CS.isFunction(this.props.onApply)) {
      this.props.onApply({
        startDate: null,
        endDate: null
      })
    }

    this.setState({
      startDate: null,
      endDate: null
    });

  }

  callApplyFunctionIfRequired =(_iStartDate, _iEndDate)=> {
    let iStartDate = _iStartDate || this.state.startDate;
    let iEndDate = _iEndDate || this.state.endDate;

    if (iStartDate && iEndDate && CS.isFunction(this.props.onApply)) {
      this.props.onApply({
        startDate: iStartDate,
        endDate: iEndDate
      });
    }
  }

  handleStartDateChanged =(oNull, oDate)=> {
    let iStartDate = oDate.getTime();
    let iEndDate = this.state.endDate;
    if(!iEndDate) {
      iEndDate = this.getEndOfCurrentDate(iStartDate);
    }
    this.callApplyFunctionIfRequired(iStartDate, iEndDate);
    this.setState({
      endDate: iEndDate,
      startDate: iStartDate,
      isCurrentDate: false,
      isInfinityDate: this.state.isInfinityDate
    });
  }

  handleEndDateChanged =(oNull, oDate)=> {
    let iEndDate = oDate.getTime();
    let iStartDate = this.state.startDate;
    if(!iStartDate) {
      iStartDate = this.getStartOfCurrentDate(iEndDate);
    }
    this.callApplyFunctionIfRequired(iStartDate, iEndDate);
    this.setState({
      endDate: iEndDate,
      startDate: iStartDate,
      isCurrentDate: false,
      isInfinityDate: this.state.isInfinityDate
    });
  }

  getStartOfCurrentDate = (iEndDate) => {
    return ViewLibraryUtils.getMomentOfDate(iEndDate).startOf('day').valueOf();
  };

  getEndOfCurrentDate = (iStartDate) => {
    return ViewLibraryUtils.getMomentOfDate(iStartDate).endOf('day').valueOf();
  };

  handleCurrentDateClicked =()=> {
    if (this.state.isCurrentDate) {
      if (CS.isFunction(this.props.onApply)) {
        this.props.onApply({
          startDate: null,
          endDate: null
        })
      }
      this.setState({
        startDate: null,
        endDate: null,
        isCurrentDate: false,
        isInfinityDate: false
      });
    } else {
      let iStartDate = this.getStartOfCurrentDate();
      let iEndDate = this.getEndOfCurrentDate();
      this.callApplyFunctionIfRequired(iStartDate, iEndDate);

      this.setState({
        startDate: iStartDate,
        endDate: iEndDate,
        isCurrentDate: true,
        isInfinityDate: false
      });
    }
  }

  handleInfinityButtonClicked = () => {
    if (this.state.isInfinityDate) {
      if (CS.isFunction(this.props.onApply)) {
        this.props.onApply({
          startDate: null,
          endDate: null
        })
      }

      this.setState({
        startDate: null,
        endDate: null,
        isInfinityDate: false,
        isCurrentDate: false
      });
    }
    else {
      let iStartDate = this.state.startDate ||  this.getStartOfCurrentDate();
      let iInfiniteDate = Constants.INFINITE_DATE;
      this.callApplyFunctionIfRequired(iStartDate, iInfiniteDate);

      this.setState({
        startDate: iStartDate,
        endDate: iInfiniteDate,
        isCurrentDate: false,
        isInfinityDate: true
      });
    }
  };

  getCurrentDateView =()=> {
    let sClassName = "currentDateSelectorIcon ";
    let isCurrentDateSelected = this.state.isCurrentDate;
    if (isCurrentDateSelected) {
      sClassName += "isSelected";
    }

    return (
        <TooltipView label={getTranslation().CURRENT_DATE}>
          <div className={sClassName} onClick={this.handleCurrentDateClicked}>
            {/*<GridYesNoPropertyView onChange={this.handleCurrentDateClicked} value={isCurrentDateSelected}/>*/}
            {/*<div className="currentDateSelectorLabel">{getTranslation().CURRENT_DATE}</div>*/}
          </div>
        </TooltipView>
    );
  }

  getInfinityDateView = () => {
    let sClassName = this.state.isInfinityDate ? "infinityButton isSelected" : "infinityButton";
    return (
        <TooltipView placement="bottom" label={getTranslation().INFINITE}>
          <div className={sClassName} onClick={this.handleInfinityButtonClicked}>
          </div>
        </TooltipView>
    );
  };

  getViewToShow =()=> {
    let bIsDateRangeApplied = this.state.startDate && this.state.endDate;
    let bIsCurrentDateSelected = this.state.isCurrentDate;
    let bIsInfiniteDateSelected = this.state.isInfinityDate;
    var sViewClass = bIsDateRangeApplied ? "dateRangeSelector isSelected" : "dateRangeSelector ";
    var oDateTextFieldStyle = {
      "lineHeight": "30px",
      "height": "30px",
      "fontSize": "11px",
    };
    var oInputStyle = {
      "fontSize": "11px",
      "lineHeight": "30px",
      "height": "30px"
    };
    var oUnderlineStyle = {
      "borderBottom": "solid 1px #b7b7b7"
    };

    var oClearButtonView = null;

    if (bIsDateRangeApplied) {
      oInputStyle.color = "#fff";
      oClearButtonView = (bIsCurrentDateSelected || bIsInfiniteDateSelected) ? null : <div className="selectedRangeClearButton" onClick={this.handleCalendarRangeClear}></div>;
      sViewClass += (CS.isNotEmpty(oClearButtonView)) ? " addClearIcon" : " ";
    }
    var oStartDate = this.state.startDate ? new Date(this.state.startDate) : null;
    var oEndDate = this.state.endDate ? new Date(this.state.endDate) : null;
    var bDisabled = false; //todo: take it later from props

    var oCurrentDateDOM = this.props.showCurrentDateButton ? this.getCurrentDateView() : null;
    var oInfinityDateDOM = this.props.showCurrentDateButton ? this.getInfinityDateView() : null;

    return (
        <div className={sViewClass}>
          <div className="dateSelector">
            <div className="dateSelectorLabel">{getTranslation().FROM + " :"}</div>
            <div className="datePicker">
              <CustomDatePicker
                  value={oStartDate}
                  maxDate={oEndDate ? oEndDate : undefined}
                  className="datePickerCustom"
                  onChange={this.handleStartDateChanged}
                  inputStyle={oInputStyle}
                  textFieldStyle={oDateTextFieldStyle}
                  underlineStyle={oUnderlineStyle}
                  disabled={bDisabled}/>
            </div>
          </div>
          <div className="dateSelector">
            <div className="dateSelectorLabel">{getTranslation().TO + " :"}</div>
            <div className="datePicker">
              <CustomDatePicker
                  value={oEndDate}
                  minDate={oStartDate ? oStartDate : undefined}
                  className="datePickerCustom"
                  onChange={this.handleEndDateChanged}
                  inputStyle={oInputStyle}
                  textFieldStyle={oDateTextFieldStyle}
                  underlineStyle={oUnderlineStyle}
                  endOfDay={true}
                  disabled={bDisabled}/>
            </div>
            {oInfinityDateDOM}
          </div>
          {oClearButtonView}
          {oCurrentDateDOM}
        </div>
    );
  }

  render() {
    var oViewToShow = this.getViewToShow();

    return (
        <div className="dateRangeSelectorView new">
          {oViewToShow}
        </div>
    );
  }

}

NewDateRangeSelector.propTypes = oPropTypes;

export const view = NewDateRangeSelector;
export const events = oEvents;
export const propTypes = NewDateRangeSelector.propTypes;
