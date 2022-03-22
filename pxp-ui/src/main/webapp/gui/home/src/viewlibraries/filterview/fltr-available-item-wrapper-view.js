import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {view as CustomPopoverView} from '../customPopoverView/custom-popover-view';
import {view as AvailableFilterItemView} from './fltr-available-filter-item-view';

import ViewLibraryUtils from '../utils/view-library-utils';

import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import {gridViewPropertyTypes as oGridViewPropertyTypes} from "../tack/view-library-constants";
import {view as DateRangePickerView} from "../customdaterangepickerview/custom-date-range-picker-view";
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import {view as LazyContextMenuView} from "../lazycontextmenuview/lazy-context-menu-view";
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";
var oEvents = {
  APPLY_FILTER_ON_TAG_GROUP_CLOSE: "apply_filter_on_tag_group_close",
  APPLY_LAZY_FILTER: "apply_lazy_filter",
  DISCARD_FILTER_ON_TAG_GROUP_CLOSE: 'discard filter on tag group close'
};

const oPropTypes = {
  filterObject: ReactPropTypes.object,
  appliedFilters: ReactPropTypes.array,
  context: ReactPropTypes.string,
  extraData: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  selectedHierarchyContext: ReactPropTypes.string,
  handleFilterButtonClicked: ReactPropTypes.func,
  handleFilterSearchTextChanged: ReactPropTypes.func,
  handleRequestPopoverClose: ReactPropTypes.func,
  handleChildFilterToggled: ReactPropTypes.func,
  hideButtons: ReactPropTypes.bool,
};

// @CS.SafeComponent
class AvailableFilterItemWrapperView extends React.Component {
  static propTypes = oPropTypes;

  constructor(props) {
    super(props);
  }

  state = {
    showAllItems: false,
    showDetails: false
  };

  componentDidUpdate() {
  }

  handleShowDetailsButtonClicked = (oEvent) => {
    this.setState({
      showDetails: true,
      anchorElement: oEvent.currentTarget,
    });
  };

  handleFilterButtonClicked = () => {
    var __props = this.props;

    this.setState({
      showAllItems: false,
      showDetails: false
    });

    if (this.props.handleFilterButtonClicked) {
      this.props.handleFilterButtonClicked(__props.context, __props.extraData, __props.selectedHierarchyContext, __props.filterContext);
    } else {
      EventBus.dispatch(oEvents.APPLY_FILTER_ON_TAG_GROUP_CLOSE, __props.context, __props.extraData, __props.selectedHierarchyContext, __props.filterContext);
    }
  };

  handleLazyFilterApplyClicked = (aSelectedIds, oReferencedData, sContext) => {
    let oFilterData = {
      selectedIds: aSelectedIds,
      referencedData: oReferencedData,
      parentId: sContext,
      selectedFilterData: {
        context: this.props.context,
        filterContext: this.props.filterContext,
        extraData: this.props.extraData,
        selectedHierarchyContext: this.props.selectedHierarchyContext
      },
    };
    EventBus.dispatch(oEvents.APPLY_LAZY_FILTER, oFilterData);
  };

  handleRequestPopoverClose = () => {
    var __props = this.props;
    this.setState({
      showAllItems: false,
      showDetails: false
    });

    if (this.props.handleRequestPopoverClose) {
      this.props.handleRequestPopoverClose(__props.context, __props.extraData, "", __props.filterContext);
    } else {
      EventBus.dispatch(oEvents.DISCARD_FILTER_ON_TAG_GROUP_CLOSE, __props.filterContext, __props.context, __props.extraData);
    }
  };

  getDateRangePickerView = (oFilterObject) => {
    let oDateTimeFormat = ViewLibraryUtils.getStandardDateTimeFormat();
    let oDateRangeLocale = {
      customRangeLabel: getTranslation().DEFINE_PERIOD,
      format: oDateTimeFormat.dateFormat + " " + oDateTimeFormat.timeFormat
    };

    let oSelectionRange = oFilterObject.ranges;
    let oSelectCalendarView = <div className="availableFilterItem">
      <div className="availableFilterItemLabel">
        {CS.getLabelOrCode(oFilterObject)}
      </div>
      <div className="availableFilterItemExpand"></div>
    </div>;
    return (
      <div className="availableFilterItemWrapper">
        <div className={"dateRangePickerWrapper"}>
          <DateRangePickerView context={this.props.filterContext.screenContext}
                               labelVsIdMap={oFilterObject.labelIdMap}
                               ranges={oSelectionRange}
                               dateRangeLocale={oDateRangeLocale}
                               showDropdowns={true}>
            {oSelectCalendarView}
          </DateRangePickerView>
        </div>
      </div>);
  };

  getIconDOM = (sImageSrc) => {
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sImageSrc}/>
        </div>
    );
  };

  getLazyMSSView = (oFilterObject, bIsDisableSearchBox) => {
    let aAppliedFilters = this.props.appliedFilters;
    let aSelectedItems = CS.map(aAppliedFilters, "id");
    let sImageSrc = ViewUtils.getIconUrl(oFilterObject.iconKey);
    let bIsEnableIconDOM = this.props.showDefaultIcon || CS.isNotEmpty(oFilterObject.iconKey);
    let bShowDefaultIcon = (oFilterObject.propertyType === "attribute" ? false : bIsEnableIconDOM);
    return (
      <div className="availableFilterItemWrapper">
        <div className={"lazyFilterItemWrapper"}>
          <LazyContextMenuView
            context={oFilterObject.id}
            key={oFilterObject.key}
            isMultiselect={true}
            hideApplyButton={this.props.hideButtons}
            onClickHandler={this.props.handleLazyFilterToggled}
            selectedItems={aSelectedItems}
            showSelectedItems={true}
            anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
            targetOrigin={{horizontal: 'left', vertical: 'top'}}
            requestResponseInfo={oFilterObject.requestResponseInfo}
            showSearch={!bIsDisableSearchBox}
            onApplyHandler={this.handleLazyFilterApplyClicked}
            style={{"max-width": "220px"}}
            showDefaultIcon={bShowDefaultIcon}
          >
            <div className="availableFilterItem">
              {bIsEnableIconDOM ? this.getIconDOM(sImageSrc) : null}
              <div className="availableFilterItemLabel">
                {CS.getLabelOrCode(oFilterObject)}
              </div>
              <div className="availableFilterItemExpand"></div>
            </div>
          </LazyContextMenuView>
        </div>
      </div>);
  };

  getFilterItemView = (oFilterObject, bIsDisableSearchBox) => {
    let oPopoverStyle = {
      maxWidth: '230px',
      maxHeight: '375px'
    };
    let sImageSrc = ViewUtils.getIconUrl(oFilterObject.iconKey);
    let bIsEnableIconDOM = this.props.showDefaultIcon || CS.isNotEmpty(oFilterObject.iconKey);
    return(
      <div className="availableFilterItemWrapper">
        <div className="availableFilterItem" onClick={this.handleShowDetailsButtonClicked}>
          {bIsEnableIconDOM ? this.getIconDOM(sImageSrc) : null}
          <div className="availableFilterItemLabel">
            {CS.getLabelOrCode(oFilterObject)}
          </div>
          <div className="availableFilterItemExpand"></div>
        </div>
        <CustomPopoverView
          className="popover-root"
          open={this.state.showDetails}
          anchorEl={this.state.anchorElement}
          anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
          transformOrigin={{horizontal: 'left', vertical: 'top'}}
          onClose={this.handleRequestPopoverClose}
          style={oPopoverStyle}
        >

          <AvailableFilterItemView filterObject={oFilterObject}
                                   extraData={this.props.extraData}
                                   filterContext={this.props.filterContext}
                                   context={this.props.context}
                                   showSearch={bIsDisableSearchBox}
                                   isMultiselect={true}
                                   onApplyHandler={this.handleFilterButtonClicked}
                                   appliedFilters={this.props.appliedFilters}
                                   handleFilterSearchTextChanged={this.props.handleFilterSearchTextChanged}
                                   handleChildFilterClicked={this.props.handleChildFilterToggled}
                                   hideButtons={this.props.hideButtons}
                                   showDefaultIcon={this.props.showDefaultIcon}
          >
          </AvailableFilterItemView>
        </CustomPopoverView>
      </div>);
  };

  getViewToRender = () => {
    let oFilterObject = this.props.filterObject;
    let oExtraData = this.props.extraData;
    let bHideFilter = oExtraData ? oExtraData.hideFilterSearch : false;
    let aExcludedIn = ['number', 'date', 'measurementMetrics'];
    let sAttributeVisualType = ViewLibraryUtils.getAttributeTypeForVisual(oFilterObject.type);
    let bIsDisableSearchBox = (oFilterObject.type === "colorVoilation") || CS.includes(aExcludedIn, sAttributeVisualType) || bHideFilter || oFilterObject.isFilterItemSearchHidden;

    switch (oFilterObject.filterViewType) {
      case oGridViewPropertyTypes.DATETIME:
        return this.getDateRangePickerView(oFilterObject);

      case oGridViewPropertyTypes.LAZY_MSS:
        return this.getLazyMSSView(oFilterObject, bIsDisableSearchBox);

      default:
        return this.getFilterItemView(oFilterObject, bIsDisableSearchBox);
    }
  };

  render() {
    return (
      this.getViewToRender()
    );
  }
}

export default AvailableFilterItemWrapperView;
export const events = oEvents;
