import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {view as AppliedFilterVIew} from '../filterview/fltr-applied-filter-view';
import {view as AvailableFilterView} from '../filterview/fltr-available-filters-view';
import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";
import EventBus from "../../libraries/eventdispatcher/EventDispatcher";
import {view as SimpleSearchBarView} from "../simplesearchbarview/simple-search-bar-view";
import {gridViewPropertyTypes as oGridViewPropertyTypes} from "../tack/view-library-constants";

const oEvents = {
  HANDLE_FILTER_CHILD_TOGGLED: "handle_filter_child_toggled",
  HANDLE_FILTER_SUMMARY_SEARCH_TEXT_CHANGED: "handle_filter_summary_search_text_changed",
  HANDLE_FILTER_ITEM_SEARCH_TEXT_CHANGED: "handle_filter_item_search_text_changed",
  HANDLE_COLLAPSE_FILTER_CLICKED: "handle_collapse_filter_clicked",
  HANDLE_APPLY_FILTER_BUTTON_CLICKED: "handle_apply_filter_button_clicked",
  HANDLE_FILTER_ITEM_POPOVER_CLOSED: "handle_popover_closed"
};

const oPropTypes = {
  availableFilterData: ReactPropTypes.array,
  appliedFilterData: ReactPropTypes.array,
  appliedFilterClonedData: ReactPropTypes.array,
  searchFilterData: ReactPropTypes.array,
  filterContext: ReactPropTypes.object,
  isFilterExpanded: ReactPropTypes.bool,
  showExpandFilterButton: ReactPropTypes.bool,
  showApplyButton: ReactPropTypes.bool,
};

// @CS.SafeComponent
class GridFilterSummaryView extends React.Component {
  constructor(props) {
    super(props);
  };

  handleFilterSearchTextChanged = (oFilterContext, sSearchFilterId, sInputType, sSearchedText) => {
    if (sInputType === oGridViewPropertyTypes.NUMBER && (CS.isNaN(CS.toNumber(sSearchedText)) || !CS.isNumber(CS.toNumber(sSearchedText)))) {
      return;
    }

    if(sSearchFilterId === "ipAddress") {
      //To Allow only numbers and dots
      let sRegexForIPAddress = /^[0-9.]*$/;
      if(!sRegexForIPAddress.test(sSearchedText)){
        return;
      }
    }

    if(sSearchFilterId === "elementCode") {
      //To Allow only numbers, letters and '-'
      let sRegexForCode = /^[a-zA-Z0-9_-]*$/;
      if(!sRegexForCode.test(sSearchedText)){
        return;
      }
    }
    EventBus.dispatch(oEvents.HANDLE_FILTER_SUMMARY_SEARCH_TEXT_CHANGED, oFilterContext, sSearchFilterId, sSearchedText);
  };

  handleChildFilterToggled = (sParentId, sChildId, sContext, oExtraData, oFilterContext) => {
    EventBus.dispatch(oEvents.HANDLE_FILTER_CHILD_TOGGLED, sParentId, sChildId, sContext, oExtraData, oFilterContext);
  };

  handleLazyFilterToggled = (oFilterData) => {
    EventBus.dispatch(oEvents.HANDLE_FILTER_CHILD_TOGGLED, oFilterData.context, oFilterData.id, "", {label: oFilterData.label}, this.props.filterContext);
  };

  handleFilterItemSearchTextChanged = (oEvent, oSearchedData, sContext) => {
    EventBus.dispatch(oEvents.HANDLE_FILTER_ITEM_SEARCH_TEXT_CHANGED, this.props.filterContext.screenContext, oSearchedData.attributeId, oSearchedData.attributeSearchText, sContext);
  };

  handleCollapseExpandAvailableFilters = (bExpandClicked) => {
    EventBus.dispatch(oEvents.HANDLE_COLLAPSE_FILTER_CLICKED, bExpandClicked, this.props.filterContext.screenContext);
  };

  handleApplyButtonClick = () => {
    EventBus.dispatch(oEvents.HANDLE_APPLY_FILTER_BUTTON_CLICKED, this.props.filterContext.screenContext);
  };

  handleRequestPopoverClose = () => {
    EventBus.dispatch(oEvents.HANDLE_FILTER_ITEM_POPOVER_CLOSED, this.props.filterContext.screenContext);
  };


  getSearchFiltersView = () => {
    let aSearchFilterData = this.props.searchFilterData;
    let aSearchFilterView = [];
    let _this = this;
    CS.forEach(aSearchFilterData, function (oSearchFilter) {
      aSearchFilterView.push(
        <div className="filterSearchBox" key={oSearchFilter.id}>
          <div className="searchLabel">{oSearchFilter.label}</div>
          <SimpleSearchBarView
            onChange={_this.handleFilterSearchTextChanged.bind(this, _this.props.filterContext, oSearchFilter.id, oSearchFilter.type)}
            searchText={oSearchFilter.value}
            placeHolder={getTranslation().SEARCH}
            inputType={oSearchFilter.type}
          />
          <div className="searchIcon"></div>
        </div>
      )
    });
    return (
      <div className="searchFilterContainer">
        {aSearchFilterView}
        <div className="hideButtonWrapper" onClick={this.handleCollapseExpandAvailableFilters.bind(this, false)}>
          <div className="hideButtonLabel">{getTranslation().HIDE}</div>
          <div className="collapseIcon"/>
        </div>
      </div>
    )
  };

  getAvailableFiltersView = () => {
    let oSearchFilterView = this.getSearchFiltersView();
    let oAvailableFilterItemViewDetails = {
      showDefaultIcon: this.props.showDefaultIconForAvailableAndAppliedFilter
    };

    let aAppliedFilterData = CS.isEmpty(this.props.appliedFilterClonedData) ? this.props.appliedFilterData : this.props.appliedFilterClonedData;
    let oAvailableFiltersView = <AvailableFilterView availableFilterData={this.props.availableFilterData}
                                                     appliedFilterData={aAppliedFilterData}
                                                     handleFilterButtonClicked={this.handleRequestPopoverClose}
                                                     handleChildFilterToggled={this.handleChildFilterToggled}
                                                     handleLazyFilterToggled={this.handleLazyFilterToggled}
                                                     handleFilterSearchTextChanged={this.handleFilterItemSearchTextChanged}
                                                     handleRequestPopoverClose={this.handleRequestPopoverClose}
                                                     hideButtons={true}
                                                     filterContext={this.props.filterContext}
                                                     availableFilterItemViewDetails={oAvailableFilterItemViewDetails}
    />;

    let oApplyButton =
      <div className="applyFilterButton" onClick={this.handleApplyButtonClick}>
        <span className="applyFilterLabel">{getTranslation().APPLY_FILTERS}</span>
      </div>;

    return (
      <div className="searchFilterWrapper">
        {oSearchFilterView}
        <div className= "availableFiltersWrapper">
          {oAvailableFiltersView}
          {this.props.showApplyButton && oApplyButton}
        </div>
      </div>
    )
  };

  render() {
    return (
      <div className="filterDetailsSectionContainer">
        {this.props.isFilterExpanded && this.getAvailableFiltersView()}
        <AppliedFilterVIew availableFilterData={this.props.availableFilterData}
                           appliedFilterData={this.props.appliedFilterData}
                           filterContext={this.props.filterContext}
                           showCount={true}
                           showEmptyMessage={true}
                           showExpandFilterButton={this.props.showExpandFilterButton}
                           handleExpandFilter={this.handleCollapseExpandAvailableFilters.bind(this, true)}
                           showClearFilterButton={this.props.isFilterExpanded}
                           showDefaultIcon={this.props.showDefaultIconForAvailableAndAppliedFilter}
                           horizontalSliderForAppliedFilter={this.props.horizontalSliderForAppliedFilter} />
      </div>
    );
  }
}

GridFilterSummaryView.propTypes = oPropTypes;

export const view = GridFilterSummaryView;
export const events = oEvents;
