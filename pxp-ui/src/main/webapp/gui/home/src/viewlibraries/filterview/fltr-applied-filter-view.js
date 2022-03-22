import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

import TooltipView from '../tooltipview/tooltip-view';
import FilterViewUtils from '../../commonmodule/util/fltr-view-utils';
import ViewLibraryUtils from '../utils/view-library-utils';
import TagTypeConstants from '../../commonmodule/tack/tag-type-constants';
import FilterAllTypeDictionary from '../../commonmodule/tack/filter-type-dictionary';
import HorizontalSlider from "../horizontalslider/horizontal-slider";
import AttributeTypeDictionary from "../../commonmodule/tack/attribute-type-dictionary-new";
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";
const FilterAllTypeDictionaryAll = FilterAllTypeDictionary.ALL;

var oEvents = {
  CLEAR_ALL_APPLIED_FILTERS: "clear_all_applied_filters",
  REMOVE_APPLIED_GROUP_FILTER: "remove_applied_group_filter",
  HANDLE_ADVANCED_SEARCH_BUTTON_CLICKED: "handle_advanced_search_button_clicked"
};

const oPropTypes = {
  availableFilterData: ReactPropTypes.array,
  appliedFilterData: ReactPropTypes.array,
  masterAttributeList: ReactPropTypes.array,
  selectedFilterHierarchyFilterGroups: ReactPropTypes.array,
  context: ReactPropTypes.string,
  extraData: ReactPropTypes.object,
  isAdvancedFilterApplied: ReactPropTypes.bool,
  showClearFilterButton: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object.isRequired,
  selectedHierarchyContext: ReactPropTypes.string,
  handleRemoveFilterGroupClicked: ReactPropTypes.func,
  showCount: ReactPropTypes.bool,
  handleExpandFilter: ReactPropTypes.func,
  showEmptyMessage: ReactPropTypes.bool,
};

// @CS.SafeComponent
class AppliedFiltersView extends React.Component {
  static propTypes = oPropTypes;

  constructor(props) {
    super(props);
    this.state = {
      clearFilterWidth: 0,
      appliedFilterWidth: 0,
      expandFilterWidth: 0,
    };
    this.appliedFilterDOM = React.createRef();
    this.clearFilterDOM = React.createRef();
    this.expandFilterDOM = React.createRef();
  }

  componentDidMount() {
    this.updateComponentWidths();
  }

  componentDidUpdate(oPrevProps) {
    if((oPrevProps.showExpandFilterButton !== this.props.showExpandFilterButton) || (this.props.showClearFilterButton && this.clearFilterDOM.current && !this.state.clearFilterWidth)){
      this.updateComponentWidths();
    }
  }

  updateComponentWidths = () => {
    let clearFilterWidth = this.getWidthByComponent(this.clearFilterDOM);
    let appliedFilterWidth = this.getWidthByComponent(this.appliedFilterDOM);
    let expandFilterWidth = this.getWidthByComponent(this.expandFilterDOM);
    this.setState({
      clearFilterWidth,
      appliedFilterWidth,
      expandFilterWidth
    })
  };

  handleSummaryObjRemove = (sFilterId) => {
    var __props = this.props;

    if (this.props.handleRemoveFilterGroupClicked) {
      this.props.handleRemoveFilterGroupClicked(sFilterId, __props.context, __props.extraData, __props.selectedHierarchyContext, __props.filterContext);
    }
    else {
      EventBus.dispatch(oEvents.REMOVE_APPLIED_GROUP_FILTER, sFilterId, __props.context, __props.extraData, __props.selectedHierarchyContext, __props.filterContext);
    }
  };

  handleClearAllClicked = () => {
    var __props = this.props;

    if (__props.handleClearAllClicked) {
      __props.handleClearAllClicked(__props.selectedHierarchyContext, __props.filterContext);
    } else {
      EventBus.dispatch(oEvents.CLEAR_ALL_APPLIED_FILTERS, __props.selectedHierarchyContext, __props.filterContext);
    }
  };

  //Not in use
  handleAdvancedSearchButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_ADVANCED_SEARCH_BUTTON_CLICKED);
  };

  getWidthByComponent = component => {
    return component.current && component.current.offsetWidth;
  }

  getIconDOM = (oFilter) => {
    let sThumbnailImageSrc = ViewUtils.getIconUrl(oFilter.iconKey);
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sThumbnailImageSrc}/>
        </div>
    );
  };

  getSearchFilterSummaryRow = () => {
    let aSummaryObjects = [];
    let _this = this;
    let __props = this.props;
    let bIsAdvancedFilterApplied = __props.isAdvancedFilterApplied;
    let aAvailableFilterData = __props.availableFilterData;
    let aMasterAttributeList = __props.masterAttributeList;
    let aSelectedFilterHierarchyFilterGroups = __props.selectedFilterHierarchyFilterGroups;
    let bShowEmptyMessage = __props.showEmptyMessage;
    let oSummaryObject;
    let sChildIconKey = null;

    CS.forEach(__props.appliedFilterData, function (oFilter) {
      if(CS.find(aSelectedFilterHierarchyFilterGroups, {id: oFilter.id})){
        return;
      }

      let bIsTag = ViewLibraryUtils.isTag(oFilter.type);
      var oMainFilter = CS.find(aAvailableFilterData, {id: oFilter.id});

      var aSelectedItems = [];
      // for attribute and tags
      CS.forEach(oFilter.children, function (oChild) {
        var sFilterLabel = "";
        if(bIsTag){
          if(!(oChild.to == 0 && oChild.from == 0) ||
              (oFilter.type == TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE)){
            sFilterLabel = CS.getLabelOrCode(oChild);
          } else {
            return;
          }
        }
        else {
          var oMainChild = {};
          if(oMainFilter){
            oMainChild = CS.find(oMainFilter.children, {id: oChild.id});
          }

          if (!CS.isEmpty(oMainChild) && !oChild.advancedSearchFilter) {
            var oChildToPass = oMainChild;
            if(bIsAdvancedFilterApplied){
              oChildToPass = oChild;
            }
            sFilterLabel = FilterViewUtils.getLabelByAttributeType(oFilter.type, oChildToPass, oMainFilter.defaultUnit, oMainFilter.precision, _this.props.filterContext);
          }else {
            if(ViewLibraryUtils.isUserTypeAttribute(oFilter.type)){
              sFilterLabel = ViewLibraryUtils.getUserNameById(oChild.value);
              let oFilterAllTypeDictionary = new FilterAllTypeDictionaryAll();
              sFilterLabel = sFilterLabel + "(" + oFilterAllTypeDictionary[oChild.type] +")";

            }else{
              let sDefaultUnit = "";
              var oAttr = !CS.isEmpty(oMainFilter) ? oMainFilter : CS.find(aMasterAttributeList, {id: oFilter.id}) || {};
              if(!oAttr.defaultUnit && (oFilter.type === AttributeTypeDictionary.CUSTOM_UNIT || oFilter.type === AttributeTypeDictionary.TEMPERATURE)) {
                oAttr.defaultUnit = oChild.defaultUnit;
                sDefaultUnit = oChild.defaultUnit;
              }
              else if (oFilter.type === AttributeTypeDictionary.PRICE) {
                sDefaultUnit = oFilter.defaultUnit;
              }
              //In case of price attribute we are showing value in default unit otherwise it will be in base unit.
              sFilterLabel = FilterViewUtils.getLabelByAttributeType(oFilter.type, oChild,
                  sDefaultUnit, oAttr.precision, _this.props.filterContext);
            }
          }
        }
        sChildIconKey = ViewUtils.getIconUrl(oChild.iconKey);
        aSelectedItems.push(sFilterLabel);
      });

      //for roles
      if(oFilter.users){
        CS.forEach(oFilter.users, function (sUserId) {
          aSelectedItems.push(ViewLibraryUtils.getUserNameById(sUserId));
        });
      }

      if(aSelectedItems.length) {
        let sValue = aSelectedItems.join(", ");
        let sValueWithCount = "";
        if (__props.showCount) {
          sValueWithCount = aSelectedItems.length > 1 ? aSelectedItems[0] + ",+" + (aSelectedItems.length - 1) : aSelectedItems[0];
        }

        var sMainLabel = CS.isEmpty(oMainFilter) ? CS.getLabelOrCode(oFilter) : CS.getLabelOrCode(oMainFilter);
        if(CS.isEmpty(sMainLabel) && CS.includes(oFilter.type.toLowerCase(), "attribute")){
          var oAttr = CS.find(aMasterAttributeList, {id: oFilter.id});
          sMainLabel = CS.getLabelOrCode(oAttr);
        }


        aSummaryObjects.push(<div className="summaryObject" key={oFilter.id}>
          <div className="summaryObjectDetails">
            {_this.props.showDefaultIcon || CS.isNotEmpty(oFilter.iconKey) ? _this.getIconDOM(oFilter) : null}
            <div className="summaryObjectDetailsLabel">{sMainLabel}:</div>
            <TooltipView placement="bottom" label={sValue}>
              <div className="summaryObjectDetailsValue">{__props.showCount ? sValueWithCount : sValue}</div>
            </TooltipView>
          </div>
          <TooltipView placement="bottom" label={getTranslation().CLEAR}>
            <div className="summaryObjectRemove"
               onClick={_this.handleSummaryObjRemove.bind(_this, oFilter.id)}></div>
          </TooltipView>
        </div>)
      }
    });

    if (CS.isEmpty(aSummaryObjects)) {
      if (bShowEmptyMessage) {
        let oEmptyMessage =
          <div className="appliedFiltersEmptyMessage">
            {getTranslation().NO_FILTERS_APPLIED}
          </div>;
        aSummaryObjects.push(oEmptyMessage)
      } else {
        return null;
      }
    }

    oSummaryObject = aSummaryObjects;

    let {
      clearFilterWidth,
      appliedFilterWidth,
      expandFilterWidth,
    } = this.state;

    let iWidthToSubtract = clearFilterWidth + expandFilterWidth;

    let rowContainerStyle = {
      width: `calc(100% - ${iWidthToSubtract + 10}px)`
    }

    let prevButtonStyle = {
      left: appliedFilterWidth + 10
    }

    if(this.props.horizontalSliderForAppliedFilter) {
      oSummaryObject = <HorizontalSlider elements={aSummaryObjects} prevButtonStyle={prevButtonStyle} />
    }

    return (
        <div className="filterSummaryRowContainer" style={rowContainerStyle}>
          <div className="filterSummaryRowFilterLabel" ref={this.appliedFilterDOM}>{getTranslation().APPLIED_FILTERS + " : "}</div>
          {oSummaryObject}
          {/*<div className="filterSummaryRowClearLabel" onClick={this.handleClearAllClicked}>{getTranslation().CLEAR_ALL}</div>*/}
        </div>
    );
  };

  render() {
    var oViewToRender = this.getSearchFilterSummaryRow();
    var bShowClearFilterButton = this.props.showClearFilterButton && !CS.isEmpty(oViewToRender);
    let bShowExpandFilterButton = this.props.showExpandFilterButton && !this.props.showClearFilterButton;
    return (
        <div className="appliedFilterContainer">
          {bShowClearFilterButton ? <div className="filterSummaryRowClearLabel" ref={this.clearFilterDOM}
                                            onClick={this.handleClearAllClicked}><span className="clearFilterLabel">{getTranslation().CLEAR_FILTERS}</span></div> : null}
          {oViewToRender}
          {bShowExpandFilterButton && <div className="expandFilterButton" ref={this.expandFilterDOM}
                                          onClick={this.props.handleExpandFilter}><span className="filtersLabel">{getTranslation().FILTERS}</span><div className="expandButton"/> </div>}
        </div>
    );
  }
}

export const view = AppliedFiltersView;
export const events = oEvents;
