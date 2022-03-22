import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as AvailableFiltersView } from '../../../../../viewlibraries/filterview/fltr-available-filters-view';
import { view as AppliedFilterView } from '../../../../../viewlibraries/filterview/fltr-applied-filter-view';
import { view as FilterSummaryView } from './fltr-summary-view';
import { view as SortByView } from './fltr-sort-by-view';
import { view as AdvancedSearchPanelView } from './advanced-search-panel-view';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
import FilterUtils from '../store/helper/filter-utils';
import {view as GroupMSSChipsView} from "../../../../../viewlibraries/filter/group-mss-chips-view";

var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_FILTER_SHOW_DETAILS_CLICKED: "handle_filter_show_details_clicked",
  HANDLE_TREE_CHECK_CLICKED: "handle_tree_check_clicked",
  HANDLE_SELECTED_TAXONOMIES_CLEAR_ALL_CLICKED: "handle_selected_taxonomies_clear_all_clicked",
  HANDLE_APPLIED_TAXONOMY_REMOVED_CLICKED: "handle_applied_taxonomy_removed_clicked"
};

const oPropTypes = {
  filterData: ReactPropTypes.object,
  onViewUpdate: ReactPropTypes.func,
  viewMasterData: ReactPropTypes.object
};

// @CS.SafeComponent
class FilterView extends React.Component {
  static propTypes = oPropTypes;

  state = {
    showDetails: true
  };

  handleLessMoreToggleButtonClicked = (oFilterContext) => {
    EventBus.dispatch(oEvents.HANDLE_FILTER_SHOW_DETAILS_CLICKED, oFilterContext);
  };

  handleTreeCheckClicked = (iNodeId) => {
    let oFilterContext = this.props.filterData.filterContext;
    EventBus.dispatch(oEvents.HANDLE_TREE_CHECK_CLICKED, this, iNodeId, "", oFilterContext);
  };

  handleTaxonomyRemoveClicked = (sCategoryType, oData) => {
    let oFilterContext = this.props.filterData.filterContext;
    EventBus.dispatch(oEvents.HANDLE_APPLIED_TAXONOMY_REMOVED_CLICKED, this, oData.id, oFilterContext, sCategoryType);
  };

  handleSelectedTaxonomiesClearAll = (sCategoryType) => {
    let oFilterContext = this.props.filterData.filterContext;
    EventBus.dispatch(oEvents.HANDLE_SELECTED_TAXONOMIES_CLEAR_ALL_CLICKED, oFilterContext, sCategoryType);
  };

  getSelectedCategoriesRow = (selectedOptions, aSelectedCategories, sContext) => {

    let _this = this;

    if (CS.isEmpty(selectedOptions)) {
      return null;
    }

    let sCategoryLabel = getTranslation().APPLIED_TAXONOMIES;
    let sClearCategoryLabel = getTranslation().CLEAR_TAXONOMIES;
    if (sContext === "classes"){
      sCategoryLabel = getTranslation().APPLIED_CLASSES;
      sClearCategoryLabel = getTranslation().CLEAR_CLASSES;
    }
    return (
        <div className="selectedCategoriesContainer">
          <div className="filterSummaryRowContainer">
            <TooltipView placement="bottom" label={sCategoryLabel}>
              <div className="filterSummaryRowFilterLabel">{sCategoryLabel + " : "}</div>
            </TooltipView>
            <TooltipView placement="bottom" label={sClearCategoryLabel}>
              <div className="filterSummaryRowClearLabel"
                   onClick={this.handleSelectedTaxonomiesClearAll.bind(this, sContext)}>{sClearCategoryLabel}</div>
            </TooltipView>
            <div className="appliedFilterWrapper">
              <GroupMSSChipsView
                  selectedOptions={selectedOptions}
                  referencedData={aSelectedCategories}
                  handleRemove={_this.handleTaxonomyRemoveClicked.bind(_this, sContext)}
                  showMoreLabel={true}
              />
            </div>

          </div>
        </div>);
  };

  getSelectedCategoriesSummary = () => {
    /** Hide Applied Taxonomies Row in case of taxonomy Hierarchy*/
    let oFilterData = this.props.filterData;
    let sSelectedHierarchyContext = oFilterData.selectedHierarchyContext;
    if (sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY ||
        sSelectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY) {
      return;
    }

    let oFilterViewData = this.props.filterData;
    let aSelectedKlasses = oFilterViewData.appliedClasses;
    let aSelectedTaxonomies = oFilterViewData.appliedTaxonomies;
    let aSelectedClassOptions = oFilterViewData.selectedClassOptions;
    let aSelectedTaxonomyOptions = oFilterViewData.selectedTaxonomyOptions;
    let oSelectedKlassesDOM = this.getSelectedCategoriesRow(aSelectedClassOptions, aSelectedKlasses, "classes");
    let oSelectedTaxonomiesDOM = this.getSelectedCategoriesRow(aSelectedTaxonomyOptions, aSelectedTaxonomies, "taxonomies");

    return(
        <React.Fragment>
          {oSelectedKlassesDOM}
          {oSelectedTaxonomiesDOM}
        </React.Fragment>
    );

  };

  getFilterDetailsSectionView = () => {
    var __props = this.props;
    var oFilterData = __props.filterData;


    let oSelectedCategoriesDOM = this.getSelectedCategoriesSummary();

    var oLessMoreDom = null;
    var sLessMoreToggleButtonLabel = oFilterData.showDetails ? (
        <span className="lessMore">{getTranslation().HIDE}<span className="lessMoreIcon">▲</span></span>) : (
        <span className="lessMore">{getTranslation().SORT_AND_FILTER}<span className="lessMoreIcon">▼</span></span>);
    var oStyle = {
      "position": "absolute",
      "bottom": "0px",
      "lineHeight": "22px",
      "right": "5px"
    };
    if(!(CS.isEmpty(oFilterData.availableFilterData) && CS.isEmpty(oFilterData.appliedFilterData) && CS.isEmpty(oFilterData.availableSortData))) {
      var sLessMoreButtonLabel = oFilterData.showDetails ? getTranslation().HIDE : getTranslation().SORT_AND_FILTER;
      oLessMoreDom = (<TooltipView placement="bottom" label={sLessMoreButtonLabel}>
        <div className="lessMoreToggleButton " style={oStyle}
             onClick={this.handleLessMoreToggleButtonClicked.bind(this, oFilterData.filterContext)}>{sLessMoreToggleButtonLabel}</div>
      </TooltipView>);
    }
    var oViewMasterData = this.props.viewMasterData;
    let oAvailableFilterItemViewDetails = {
      showDefaultIcon: true
    };
    // TODO: Change to taxonomy
    return (
        <div className="filterDetailsSectionContainer">
          {/*<SelectedTaxonomyView/>*/}
          <div className="selectedCategoriesContainer">
            {oViewMasterData.canFilterTaxonomy? oSelectedCategoriesDOM: null}
          </div>
          {oViewMasterData.canFilter ?
           (<AvailableFiltersView availableFilterData={oFilterData.availableFilterData}
                                  onViewUpdate={__props.onViewUpdate}
                                  appliedFilterData={oFilterData.appliedFilterData}
                                  appliedFilterDataClone={oFilterData.appliedFilterDataClone}
                                  selectedHierarchyContext={oFilterData.selectedHierarchyContext}
                                  selectedFilterHierarchyFilterGroups={oFilterData.selectedFilterHierarchyFilterGroups}
                                  availableEntityViewStatus={oFilterData.availableEntityViewStatus}
                                  isAdvancedFilterApplied={oFilterData.isAdvancedFilterApplied}
                                  showGroupBy={oFilterData.showGroupBy}
                                  filterContext={oFilterData.filterContext}
                                  availableFilterItemViewDetails = {oAvailableFilterItemViewDetails}
                                  showAdvancedSearchOptions={true}/>) : null}

          <AppliedFilterView availableFilterData={oFilterData.availableFilterData}
                             appliedFilterData={oFilterData.appliedFilterData}
                             appliedFilterDataClone={oFilterData.appliedFilterDataClone}
                             masterAttributeList={oFilterData.masterAttributeList}
                             selectedFilterHierarchyFilterGroups={oFilterData.selectedFilterHierarchyFilterGroups}
                             isAdvancedFilterApplied={oFilterData.isAdvancedFilterApplied}
                             showClearFilterButton={true}
                             filterContext={oFilterData.filterContext}
                             showDefaultIcon={true}
                             selectedHierarchyContext={oFilterData.selectedHierarchyContext}/>

          {oViewMasterData.canSort ? (<SortByView availableSortData={oFilterData.availableSortData}
                                                  activeSortDetails={oFilterData.activeSortDetails}
                                                  selectedHierarchyContext={oFilterData.selectedHierarchyContext}
                                                  filterContext = {oFilterData.filterContext}
                                                  onViewUpdate={__props.onViewUpdate}/>) : null}
          {(oViewMasterData.canSort || oViewMasterData.canFilter) ? oLessMoreDom : null}
        </div>
    );
  };

  getFilterViewToRender = () => {
    var __props = this.props;
    var oFilterData = __props.filterData;
    if(oFilterData.showDetails) {
      return this.getFilterDetailsSectionView();
    }
    var sLessMoreToggleButtonLabel = oFilterData.showDetails ? (
        <span className="lessMore">{getTranslation().HIDE}<span className="lessMoreIcon">▲</span></span>) : (
        <span className="lessMore">{getTranslation().SORT_AND_FILTER}<span className="lessMoreIcon">▼</span></span>);
    var oStyle = {};
    //If not provided then at the time of no selection in filter more link overlapped the search box
    if(CS.isEmpty(oFilterData.activeSortDetails) && CS.isEmpty(oFilterData.appliedFilterData)) {
      oStyle.position = "inherit";
    }
    var oLessMoreDom = null;
    if (!(CS.isEmpty(FilterUtils.getCheckedKlassTreeNodes(null, null, oFilterData.filterContext).checkedNodes)
        && CS.isEmpty(oFilterData.availableFilterData)
        && CS.isEmpty(oFilterData.availableSortData))) {
      var sLessMoreButtonLabel = oFilterData.showDetails ? getTranslation().HIDE : getTranslation().SORT_AND_FILTER;
      oLessMoreDom = (<TooltipView placement="bottom" label={sLessMoreButtonLabel}>
        <div className="lessMoreToggleButton " style={oStyle}
             onClick={this.handleLessMoreToggleButtonClicked.bind(this, oFilterData.filterContext)}>{sLessMoreToggleButtonLabel}</div>
      </TooltipView>);
    }
    return (
        <div className="summaryViewContainer">
        <FilterSummaryView availableSortData={oFilterData.availableSortData}
                           activeSortDetails={oFilterData.activeSortDetails}
                           availableFilterData={oFilterData.availableFilterData}
                           appliedFilterData={oFilterData.appliedFilterData}
                           masterAttributeList={oFilterData.masterAttributeList}
                           isAdvancedFilterApplied={oFilterData.isAdvancedFilterApplied}
                           filterContext={oFilterData.filterContext}

          />
          {oLessMoreDom}
        </div>
    );
  };

  render() {
    var oFilterData = this.props.filterData;

    if(CS.isEmpty(oFilterData)){
      return null;
    }

    var oAdvanceSearchDialog = !oFilterData.advancedSearchPanelData.showAdvancedSearchPanel ? null
        : <AdvancedSearchPanelView advancedSearchPanelData={oFilterData.advancedSearchPanelData}
                                   appliedFilterDataClone={oFilterData.appliedFilterDataClone}
                                   appliedFilterData={oFilterData.appliedFilterData}
                                   masterAttributeList={oFilterData.masterAttributeList}
                                   filterContext={oFilterData.filterContext}
                                   selectedHierarchyContext={oFilterData.selectedHierarchyContext}/>;

    var oFilterView = oFilterData.showFilter ? this.getFilterViewToRender() : null;
    return (
        <div className="filterSectionContainer">
          <div className="filterBody">
            {oFilterView}
          </div>
          {oAdvanceSearchDialog}
        </div>
    );
  }
}

export const view = FilterView;
export const events = oEvents;
