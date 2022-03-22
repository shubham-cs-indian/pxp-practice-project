import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import ResizeSensor from 'css-element-queries/src/ResizeSensor';
import { view as CustomPopoverView } from '../customPopoverView/custom-popover-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

import AvailableFilterItemWrapperView from './fltr-available-item-wrapper-view';
import ViewLibraryUtils from '../utils/view-library-utils';

var oEvents = {
  HANDLE_ADVANCED_SEARCH_BUTTON_CLICKED: "handle_advanced_search_button_clicked",
  HANDLE_LOAD_MORE_FILTER_BUTTON_CLICKED: "handle_load_more_filter_button_clicked"
};

const oPropTypes = {
  availableFilterData: ReactPropTypes.array,
  appliedFilterData: ReactPropTypes.array,
  onViewUpdate: ReactPropTypes.func,
  appliedFilterDataClone: ReactPropTypes.array,
  selectedHierarchyContext: ReactPropTypes.string,
  selectedFilterHierarchyFilterGroups: ReactPropTypes.array,
  isAdvancedFilterApplied: ReactPropTypes.bool,
  availableEntityViewStatus: ReactPropTypes.bool,
  showGroupBy: ReactPropTypes.bool,
  showAdvancedSearchOptions: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  filterContext: ReactPropTypes.object.isRequired,
  extraData: ReactPropTypes.object,
  handleFilterButtonClicked: ReactPropTypes.func,
  handleFilterSearchTextChanged: ReactPropTypes.func,
  handleRequestPopoverClose: ReactPropTypes.func,
  handleChildFilterToggled: ReactPropTypes.func,
  handleLazyFilterToggled: ReactPropTypes.func,
  hideButtons: ReactPropTypes.bool,
  availableFilterItemViewDetails: ReactPropTypes.object
};

// @CS.SafeComponent
class AvailableFiltersView extends React.Component {

  constructor (props) {
    super(props);

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };
  }

  state = {
    showMore: false,
    selectedFilters: []
  };

  componentDidMount() {
    /** setTimeout() is used in componentDidMount() of Popover.js,
      * which causes delay in rendering Popover view,
      * hence it is used here for delay.
      */
    setTimeout(this.calculateFilterItemCount);
  }

  componentDidUpdate() {
    /** setTimeout() is used in componentDidUpdate() of Popover.js,
     * which causes delay in updating Popover view,
     * hence it is used here for delay.
     */
    setTimeout(this.calculateFilterItemCount);
  }

  handleAdvancedSearchButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_ADVANCED_SEARCH_BUTTON_CLICKED, this.props.filterContext);
  };

  handleMoreButtonClicked = (oEvent) => {
    var bCurrentState = this.state.showMore;
    this.setState({
      showMore: !bCurrentState,
      moreView: oEvent.currentTarget
    });
  };

  handleClosePopoverButtonClicked = () => {
    var bCurrentState = this.state.showMore;
    this.setState({
      showMore: !bCurrentState
    });
  };

  calculateFilterItemCount = () => {
    var _this = this;
    var __props = _this.props;
    var bIsAdvancedFilterApplied = this.props.isAdvancedFilterApplied;
    if (!CS.isEmpty(_this.availableFiltersContainer) && !bIsAdvancedFilterApplied) {
      var aAppliedData = __props.appliedFilterDataClone ? __props.appliedFilterDataClone : __props.appliedFilterData;
      var aSelectedData = _this.state.selectedFilters;
      let sSplitter = ViewLibraryUtils.getSplitter();

      if (_this["moreContainer"]) {
        _this["moreContainer"].classList.remove('hideMe');
      }
      var iAdvancedSearchButtonWidth = _this["advancedSearchButton"] ? _this["advancedSearchButton"].offsetWidth : 0;
      var iWidthToSubtract = (
          _this["availableFiltersLabel"].offsetWidth +
          _this["moreContainer"].offsetWidth +
          iAdvancedSearchButtonWidth
          + 1); //1 as a buffer to avoid case where sum is very close to width, in decimals (filter goes
      var iTotalWidth = _this.availableFiltersContainer.offsetWidth - iWidthToSubtract; //100px saved for 'more' section
      var iSum = 0;
      var iMoreCounter =  0;
      var aAdjustableRefIds = [];
      var aSelectedFilterHierarchyFilterGroups = __props.selectedFilterHierarchyFilterGroups;

      CS.forEach(__props.availableFilterData, function (oFilterData) {
        if(!!CS.find(aSelectedFilterHierarchyFilterGroups, {id: oFilterData.id})){
          return;
        }

        var sRefId = oFilterData.id + sSplitter;
        var oAvailDOM = ReactDOM.findDOMNode(_this[sRefId + "avail"]);
        var oMoreDOM = ReactDOM.findDOMNode(_this[sRefId + "more"]);

        oAvailDOM.classList.remove('hideMe');
        iSum = iSum + oAvailDOM.offsetWidth;

        if (iSum >= iTotalWidth) {
          iMoreCounter++;
          if(!(CS.includes(aSelectedData, oFilterData.id) || CS.find(aAppliedData, {id: oFilterData.id}))) {
            oAvailDOM.classList.add('hideMe');
          }
          oMoreDOM && oMoreDOM.classList.remove('hideMe');
          if (iSum < (iTotalWidth + _this.moreContainer.offsetWidth)) {
            aAdjustableRefIds.push(sRefId);
          } else {
            aAdjustableRefIds = [];
          }
        }
        else {
          oMoreDOM && oMoreDOM.classList.add('hideMe');
        }
      });

      if(_this["moreContainer"] && _this["moreCounter"]) {
        if(!iMoreCounter) {
          _this["moreContainer"].classList.add('hideMe');
        } else if (!CS.isEmpty(aAdjustableRefIds)) {
          CS.forEach(aAdjustableRefIds, function (sRefId) {
            var oAvailDOM = ReactDOM.findDOMNode(_this[sRefId + "avail"]);
            var oMoreDOM = ReactDOM.findDOMNode(_this[sRefId + "more"]);
            oAvailDOM.classList.remove('hideMe');
            oMoreDOM && oMoreDOM.classList.add('hideMe');
          });
          _this["moreContainer"].classList.add('hideMe');
        } else {
          _this["moreContainer"].classList.remove('hideMe');
          _this["moreCounter"].innerHTML =  iMoreCounter + " " + getTranslation().MORE;
        }
      }

      if(typeof __props.onViewUpdate === 'function'){
        __props.onViewUpdate();
      }
    }
  };

  handleLoadMore = () => {
    EventBus.dispatch(oEvents.HANDLE_LOAD_MORE_FILTER_BUTTON_CLICKED, this.props.filterContext);
  };

  getMoreSectionView = (aMoreFilterItems) => {
    var sMoreFiltersButtonLabel = aMoreFilterItems.length + " " + getTranslation().MORE;
    var sMoreFiltersSectionClassName = this.state.showMore ? "moreFiltersSection" : "moreFiltersSection invisible";
    aMoreFilterItems.push(<div onClick={this.handleLoadMore} className="moreFilterLoadMore"
                               key="loadMore">{getTranslation().LOAD_MORE}</div>);
    return (
        <div className="moreSectionContainer" ref= {this.setRef.bind(this,"moreContainer")} >
          <div className="moreFiltersButton" onClick={this.handleMoreButtonClicked}>
            <div className="moreFiltersButtonLabel" ref={this.setRef.bind(this,"moreCounter")}>{sMoreFiltersButtonLabel}</div>
            <div className="moreFiltersButtonExpand"></div>
          </div>
          <CustomPopoverView
              className="popover-root"
              open={this.state.showMore}
              anchorEl={this.state.moreView}
              anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
              transformOrigin={{horizontal: 'right', vertical: 'top'}}
              onClose={this.handleClosePopoverButtonClicked}
          >
            <div className={sMoreFiltersSectionClassName}>{aMoreFilterItems}</div>
          </CustomPopoverView>
        </div>
    );
  };

  handleShowMoreFilterItemClicked = (sId) => {
    var aAddOrRemoveFilter = [sId];
    var aMustShowFilters = CS.xor(aAddOrRemoveFilter, this.state.selectedFilters);
    this.setState({
      selectedFilters : aMustShowFilters
    });
  };

  getAvailableFilterItemsViews = (aAvailableFilterData, aAvailableFilterItemsView, aMoreFilterItems) => {
    var _this = this;
    var __props = this.props;
    var aAppliedFilters = __props.appliedFilterDataClone ? __props.appliedFilterDataClone : __props.appliedFilterData;
    var aSelectedFilterHierarchyFilterGroups = __props.selectedFilterHierarchyFilterGroups;
    var aSelectedFilters = this.state.selectedFilters;
    let sSplitter = ViewLibraryUtils.getSplitter();

    CS.forEach(aAvailableFilterData, function (oFilterData){

      /** Do not show filterItem if it is selected in Filter Hierarchy*/
      if(!!CS.find(aSelectedFilterHierarchyFilterGroups, {id: oFilterData.id})){
        return;
      }

      var aAppliedChildren = [];
      var oAppliedFilter = CS.find(aAppliedFilters, {id: oFilterData.id});
      if (oAppliedFilter) {
        aAppliedChildren = oAppliedFilter.children;
      }

      aAvailableFilterItemsView.push(
          <AvailableFilterItemWrapperView key={oFilterData.id}
                               ref={_this.setRef.bind(_this,oFilterData.id + sSplitter + "avail")}
                               filterObject={oFilterData}
                               appliedFilters={aAppliedChildren}
                               context={__props.context}
                               extraData={__props.extraData}
                               filterContext={__props.filterContext}
                               selectedHierarchyContext={__props.selectedHierarchyContext}
                               handleFilterButtonClicked={__props.handleFilterButtonClicked}
                               handleFilterSearchTextChanged={__props.handleFilterSearchTextChanged}
                               handleRequestPopoverClose={__props.handleRequestPopoverClose}
                               handleLazyFilterToggled={__props.handleLazyFilterToggled}
                               handleChildFilterToggled={__props.handleChildFilterToggled}
                               hideButtons={__props.hideButtons}
                               showDefaultIcon={__props.availableFilterItemViewDetails.showDefaultIcon}
          />
      );

      var sCheckboxClass = "moreFiltersItemCheckbox ";
      if(CS.includes(aSelectedFilters, oFilterData.id) || !!CS.find(aAppliedFilters, {id: oFilterData.id})) {
        sCheckboxClass += "checked";
      }

      aMoreFilterItems.push(
          <div className="moreFiltersItemContainer"
               key={oFilterData.id}
               ref={_this.setRef.bind(_this,oFilterData.id + sSplitter + "more")}
               onClick={_this.handleShowMoreFilterItemClicked.bind(_this, oFilterData.id)}>
            <div className={sCheckboxClass}></div>
            <div className="moreFiltersItem">
              <div className="moreFiltersItemLabel">{CS.getLabelOrCode(oFilterData)}</div>
              <div className="moreFiltersItemBuffer"></div>
            </div>
          </div>
      );
    });
  }

  getAvailableFilterItems = () => {
    var __props = this.props;
    var aAvailableFilterData = __props.availableFilterData;
    var aAvailableFilterItemsView = [];
    var aMoreFilterItems = [];
    var bIsAdvancedFilterApplied = this.props.isAdvancedFilterApplied;

    if (bIsAdvancedFilterApplied) {
      aAvailableFilterItemsView.push(this.getSingleEmptyNodeWithMessage(getTranslation().ADVANCED_FILTER_APPLIED, 2));
    }

    if (CS.isNotEmpty(aAvailableFilterData)) {
      this.getAvailableFilterItemsViews(aAvailableFilterData, aAvailableFilterItemsView, aMoreFilterItems);
    }

    if (CS.isEmpty(aAvailableFilterItemsView)) {
      aAvailableFilterItemsView.push(this.getSingleEmptyNodeWithMessage(getTranslation().NO_AVAILABLE_FILTER, 1));
    }

    var oMoreSectionView = aMoreFilterItems.length ? this.getMoreSectionView(aMoreFilterItems): null;

    return {
      availableFilterItemsView : aAvailableFilterItemsView,
      moreSectionView : oMoreSectionView
    };
  };

  domMounted = (oView) => {
    if(oView) {
      var _this = this;
      new ResizeSensor(oView, CS.debounce(_this.calculateFilterItemCount, 150));
    }
  };

  getSingleEmptyNodeWithMessage = (sMessage, sKey) => {
    var oStyle = {paddingRight: "5px"};
    return(
        <div className="availableFilterItemWrapper" key={sKey}>
          <div className="availableFilterItem">
            <div className="availableFilterItemLabel" style={oStyle}>
              {sMessage}
            </div>
          </div>
        </div>
    );
  };

  render() {
    var {availableFilterItemsView : aAvailableFilterItemsView, moreSectionView : oMoreSectionView} = this.getAvailableFilterItems();
    var oAdvancedFilterButton = this.props.showAdvancedSearchOptions ?
        <div className="advancedSearchButton"
             ref={this.setRef.bind(this,"advancedSearchButton")}
             onClick={this.handleAdvancedSearchButtonClicked}>{getTranslation().ADVANCED_FILTERS}</div>
        : null;

    return (
        <div ref={this.domMounted}>
          <div ref={this.setRef.bind(this,"availableFiltersContainer")} className="availableFiltersContainer">
            <div ref={this.setRef.bind(this,"availableFiltersLabel")}
                 className="availableFiltersLabel">{getTranslation().APPLICABLE_FILTERS + " : "}</div>
            {oAdvancedFilterButton}
            {oMoreSectionView} {/* more section above available filters as floating it right made it go down too */}
            <div className="availableFilterItemsContainer">
              {aAvailableFilterItemsView}
            </div>
          </div>
        </div>
    );
  }
}

export const view = AvailableFiltersView;
export const events = oEvents;
