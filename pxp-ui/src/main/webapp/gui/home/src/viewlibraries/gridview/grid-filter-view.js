import CS from "../../libraries/cs";
import React, {Fragment} from 'react';
import ReactPropTypes from "prop-types";
import {gridViewFilterTypes as oGridViewFilterTypes} from '../tack/view-library-constants';
import {view as CustomTextFieldView} from '../customtextfieldview/custom-text-field-view'
import {view as DateRangePickerView} from "../customdaterangepickerview/custom-date-range-picker-view";
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import {view as LazyContextMenuView} from "../lazycontextmenuview/lazy-context-menu-view";
import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";

const oEvents = {
  GRID_FILTER_APPLIED:"grid_filter_applied",
};

const oPropTypes = {
  filterColumnId: ReactPropTypes.string,
  gridFilterType: ReactPropTypes.string,
  filterData: ReactPropTypes.object
};


// @CS.SafeComponent
class GridFilterView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleGridTextFilterValueChanged = (oEvent) => {
    if (oEvent.keyCode === 13) {
      let oFilterData = this.props.filterData;
      if(!CS.isEqual(oEvent.target.value, oFilterData.value)) {
        let oAppliedFilterData = {
          filterField: oFilterData.id,
          filterType: this.props.gridFilterType,
          filterValues: oEvent.target.value
        };
        EventBus.dispatch(oEvents.GRID_FILTER_APPLIED, oAppliedFilterData);
      }
    }
  };

  handleClearFilterClicked = () => {
    let oFilterData = this.props.filterData;
    let sFilterType = this.props.gridFilterType;
    if (oFilterData.isFilterApplied) {
      let oAppliedFilterData = {
        filterField: oFilterData.id,
        filterType: sFilterType,
      };
      switch (sFilterType) {
        case oGridViewFilterTypes.TEXT:
          oAppliedFilterData.filterValues = "";
          break;

        case oGridViewFilterTypes.DATE_RANGE:
          oAppliedFilterData.filterValues = {};
          break;

        default:
          return null;
      }
      EventBus.dispatch(oEvents.GRID_FILTER_APPLIED, oAppliedFilterData);
    }
  };

  getTextFilterView = () => {
    let oFilterData = this.props.filterData;
    let sClassName = oFilterData.isFilterApplied ? "gridFilterView filterApplied" : "gridFilterView";
    let oClearFilterView = oFilterData.isFilterApplied ? (
        <div className={"clearTextAppliedFilter"} onClick={this.handleClearFilterClicked}></div>) : null;
    return (<Fragment>
      <CustomTextFieldView
          className={sClassName}
          value={oFilterData.value}
          isDisabled={false}
          isMultiLine={false}
          hintText={oFilterData.hintText}
          onKeyDown={this.handleGridTextFilterValueChanged}
      />
      {oClearFilterView}
    </Fragment>)
  };

  applySelectedFilters = (aSelectedItems, oReferencedData) => {
    let oFilterData = this.props.filterData;
    let aPreviouslySelectedItems = oFilterData.selectedItems;
    let oAppliedFilterData = {};
    if(!CS.isEqual(aSelectedItems, aPreviouslySelectedItems)) {
      oAppliedFilterData = {
        filterField: oFilterData.id,
        filterType: this.props.gridFilterType,
        filterValues: aSelectedItems
      };
      EventBus.dispatch(oEvents.GRID_FILTER_APPLIED, oAppliedFilterData, oReferencedData)
    }
  };


  getDropDownFilterView = () => {
    let oFilterData = this.props.filterData;
    let oSelectMSSDom = this.getSelectMSSDom(oFilterData);

    return (<LazyContextMenuView
        context={"gridFilterView"}
        selectedItems={oFilterData.selectedItems}
        excludedItems={this.props.excludedItems}
        isMultiselect={oFilterData.isMultiSelect}
        showSelectedItems={oFilterData.isMultiSelect}
        showColor={oFilterData.showColor}
        anchorOrigin={oFilterData.anchorOrigin}
        targetOrigin={oFilterData.targetOrigin}
        useAnchorElementWidth={true}
        onPopOverOpenedHandler={this.onPopOverOpen}
        requestResponseInfo={oFilterData.requestResponseInfo}
        onApplyHandler={this.applySelectedFilters}
    >
    {oSelectMSSDom}
    </LazyContextMenuView>);
  };


  getSelectMSSDom = (oFilterData) => {
    let sPlaceholder = (oFilterData.customPlaceHolder) ? oFilterData.customPlaceHolder : getTranslation().NOTHING_IS_SELECTED;
    let sSelectedItemClassName = oFilterData.isFilterApplied ? "selectedItemView filterApplied" : "selectedItemView";
    return (<div className={"selectedItemViewWrapper"}>
      <div className={sSelectedItemClassName}>{sPlaceholder}</div>
      <div className={"expandButton"}></div>
    </div>);
  };

  handleTimeRangeApplyClicked = (oDownloadRange) => {
    let oFilterData = this.props.filterData;
    let oPreviousRange = {
      startTime: oFilterData.startTime,
      endTime: oFilterData.endTime,
    };
    oDownloadRange = {
      startTime: oDownloadRange.startDate.toDate().getTime(),
      endTime: oDownloadRange.endDate.toDate().getTime()
    };

    if(!(CS.isEqual(oDownloadRange, oPreviousRange) && oFilterData.isFilterApplied)) {
      let oAppliedFilterData = {
        filterField: oFilterData.id,
        filterType: this.props.gridFilterType,
        filterValues: oDownloadRange
      };
      EventBus.dispatch(oEvents.GRID_FILTER_APPLIED, oAppliedFilterData)
    }
  };

  getDateRangeFilterView = () => {
    let oFilterData = this.props.filterData;
    let sClassName = oFilterData.isFilterApplied ? "filterDateRangeSection filterApplied" : "filterDateRangeSection";
    let sExpandClassName = "expandButton";
    let oSelectCalendarView = (<div className={"gridCalenderSection"}>
      <div className={sClassName}>{oFilterData.selectCalendarViewLabel}</div>
      <div className={sExpandClassName}></div>
    </div>);
    return (<div className={"dateRangePickerWrapper"}>
      <DateRangePickerView context={"gridViewFilter"}
                           startDate={oFilterData.startTime}
                           endDate={oFilterData.endTime}
                           dateRangeLocale={oFilterData.dateRangeLocale}
                           calendarOpenDirection={oFilterData.calendarOpenDirection}
                           alwaysShowCalendars={oFilterData.alwaysShowCalendar}
                           showDropdowns={oFilterData.showDropDowns}
                           timePicker24Hour={oFilterData.timePicker24Hour}
                           timePicker={oFilterData.timePicker}
                           timePickerSeconds={oFilterData.timePickerSeconds}
                           singleDatePicker={oFilterData.singleDatePicker}
                           onApply={this.handleTimeRangeApplyClicked}
                           onCancel={this.handleClearFilterClicked}>
        {oSelectCalendarView}
      </DateRangePickerView>
    </div>);
  };

  getFilterViewByType = () => {
    switch (this.props.gridFilterType) {
      case oGridViewFilterTypes.TEXT:
        return this.getTextFilterView();

      case oGridViewFilterTypes.DROP_DOWN:
        return this.getDropDownFilterView();

      case oGridViewFilterTypes.DATE_RANGE:
        return this.getDateRangeFilterView();

      default: return null;
    }
  };

  stopEventPropagation = (oEvent) => {
    oEvent.stopPropagation();
  };

  render () {
    let oFilterData = this.props.filterData;
    let sClassName = oFilterData.isFilterApplied ? "gridViewFilterWrapper filterApplied" : "gridViewFilterWrapper";
    let oFilterView = this.getFilterViewByType();

    return (
        <div className={sClassName} onClick={this.stopEventPropagation}>
          {oFilterView}
        </div>
    );
  }
}

GridFilterView.propTypes = oPropTypes;
export const view = GridFilterView;
export const events = oEvents;
