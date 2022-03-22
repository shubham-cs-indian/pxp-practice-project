import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import FilterUtils from '../store/helper/filter-utils';
import FilterViewUtils from '../../../../../commonmodule/util/fltr-view-utils';
import ViewUtils from './utils/view-utils';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import FilterAllTypeDictionary from '../../../../../commonmodule/tack/filter-type-dictionary';
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {};

const oPropTypes = {
  availableSortData: ReactPropTypes.array,
  availableFilterData: ReactPropTypes.array,
  appliedFilterData: ReactPropTypes.array,
  activeSortDetails: ReactPropTypes.object,
  masterAttributeList: ReactPropTypes.array,
  filterContext: ReactPropTypes.object.isRequired,
  isAdvancedFilterApplied: ReactPropTypes.bool,
};

// @CS.SafeComponent
class FilterSummaryView extends React.Component {
  static propTypes = oPropTypes;

  getSummaryView = (sSummaryLabel, sValue, sIconKey, sChildIconKey) => {
    return (<div className="summaryLittleObject" key={sSummaryLabel}>
      <div className="summaryLittleObjectDetails">
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sIconKey}/>
        </div>
        <div className="summaryLittleObjectDetailsLabel">{sSummaryLabel}:</div>
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sChildIconKey}/>
        </div>
        <TooltipView placement="bottom" label={sValue}>
          <div className="summaryLittleObjectDetailsValue">{sValue}</div>
        </TooltipView>
      </div>
    </div>);
  };

  getSummaryForSortOrderView = (oActiveSortField, sValue) => {
    let sSummaryLabel = CS.getLabelOrCode(oActiveSortField);
    let sImageSrc = ViewUtils.getIconUrl(oActiveSortField.iconKey);
    var sSortOrder = sValue == 'asc'? getTranslation().ASCENDING :getTranslation().DESCENDING ;
    var sIconClassName = "sortOrder " + sValue;
    return (<div className="summaryLittleObject" key={sSummaryLabel}>
      <div className="summaryLittleObjectDetails">
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sImageSrc}/>
        </div>
        <div className="summaryLittleObjectDetailsLabel">{sSummaryLabel}:</div>
        <TooltipView placement="bottom" label={sValue}>
          <div className="summaryLittleObjectDetailsValue">{sSortOrder}</div>
        </TooltipView>
        <div className={sIconClassName}></div>
      </div>
    </div>);
  };

  getAllSummaryView = () => {
    var _this = this;
    var __props = this.props;
    var aSummaryObjects = [];
    var bIsAdvancedFilterApplied = __props.isAdvancedFilterApplied;
    var aAvailableFilterData = __props.availableFilterData;
    var that = this;
    let sChildIconKey = null;

    CS.forEach(__props.appliedFilterData, function (oFilter) {
      var oMainFilter = CS.find(aAvailableFilterData, {id: oFilter.id});
      var bIsTag = ViewUtils.isTag(oFilter.type);

      var aSelectedItems = [];
      CS.forEach(oFilter.children, function (oChild) {
        var sFilterLabel = "";
        if(bIsTag){
          if(!(oChild.to == 0 && oChild.from == 0) ||
              (oFilter.type == TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE || oFilter.type == TagTypeConstants.RANGE_TAG_TYPE)){
            sFilterLabel = CS.getLabelOrCode(oChild);
          }else {
            return;
          }
        }else {
          var oMainChild = {};
          if(oMainFilter){
            oMainChild = CS.find(oMainFilter.children, {id: oChild.id});
          }

          if (!CS.isEmpty(oMainChild) && !oChild.advancedSearchFilter) {
            var oChildToPass = oMainChild;
            if(bIsAdvancedFilterApplied){
              oChildToPass = oChild;
            }
            sFilterLabel = FilterViewUtils.getLabelByAttributeType(oFilter.type, oChildToPass, oMainFilter.defaultUnit, oMainFilter.precision, __props.filterContext);
          }else {
            if(ViewUtils.isUserTypeAttribute(oFilter.type)){
              sFilterLabel = ViewUtils.getUserNameById(oChild.value);
              let oFilterAllTypeDictionary = new FilterAllTypeDictionary();
              sFilterLabel = sFilterLabel + "(" + oFilterAllTypeDictionary[oChild.type] +")";

            }else {
              var oAttr = !CS.isEmpty(oMainFilter) ? oMainFilter : CS.find(aMasterAttributeList, {id: oFilter.id}) || {};
              sFilterLabel = FilterViewUtils.getLabelByAttributeType(oFilter.type, oChild, oAttr.defaultUnit, oAttr.precision, __props.filterContext);
            }

          }

        }
        sChildIconKey = ViewUtils.getIconUrl(oChild.iconKey);
        aSelectedItems.push(sFilterLabel);

      });

      //for roles
      if(oFilter.users){
        CS.forEach(oFilter.users, function (sUserId) {
          aSelectedItems.push(ViewUtils.getUserNameById(sUserId));
        });
      }

      if(aSelectedItems.length) {
        var sValue = aSelectedItems.join(", ");

        var sMainLabel = CS.isEmpty(oMainFilter) ? CS.getLabelOrCode(oFilter) : CS.getLabelOrCode(oMainFilter);
        if(CS.isEmpty(sMainLabel) && CS.includes(oFilter.type.toLowerCase(), "attribute")){
          var aMasterAttributeList = _this.props.masterAttributeList;
          var oAttr = CS.find(aMasterAttributeList, {id: oFilter.id});
          sMainLabel = CS.getLabelOrCode(oAttr);
        }
        let sIconKey = ViewUtils.getIconUrl(oFilter.iconKey)
        var oSummaryView = that.getSummaryView(sMainLabel, sValue, sIconKey, sChildIconKey);
        aSummaryObjects.push(oSummaryView);
      }
    });

    var aSortBySummaryView = [];
    var oActiveSortDetails = __props.activeSortDetails;
    var aActiveSortIds = CS.keys(oActiveSortDetails);
    CS.forEach(aActiveSortIds, function (sActiveSortId) {
      var sSortOrder = oActiveSortDetails[sActiveSortId].sortOrder;
      if (sSortOrder) {
        var oActiveSortField = CS.find(__props.availableSortData, {sortField: sActiveSortId});
        if (oActiveSortField) {
          aSortBySummaryView.push(that.getSummaryForSortOrderView(oActiveSortField, sSortOrder));
        }
      }
    });

    var aSelectedTaxonomies = FilterUtils.getCheckedKlassTreeNodes(null, null, __props.filterContext).checkedNodes;
    if (CS.isEmpty(aSummaryObjects) && CS.isEmpty(aSortBySummaryView) && CS.isEmpty(aSelectedTaxonomies)) {
      return null;
    }
    var oStyle = {};
    if(CS.isEmpty(aSummaryObjects)) {
      oStyle['marginLeft'] = "0";
    }

    return (
        <div className="filterSummaryLittleRowContainer">
          <div className="filterSummarySectionWrapper">
            {!CS.isEmpty(aSummaryObjects) ? (
                <div className="filterSummaryLittleRowFilterLabel">{getTranslation().APPLIED_FILTERS + " : "}</div>) : null}
            <div className="filterSummaryViewWrapper">{aSummaryObjects}</div>
            {!CS.isEmpty(aSortBySummaryView) ?
                <div className="filterSortSummaryLittleRowFilterLabel" style={oStyle}>{getTranslation().SORT_BY + " : "}</div> : null}
            <div className="filterSortSummaryViewWrapper">{aSortBySummaryView}</div>
          </div>
        </div>
    );
  };

  render() {

    return (
        <div className="filterSummarySectionContainer">{this.getAllSummaryView()}</div>
    );
  }
}

export const view = FilterSummaryView;
export const events = oEvents;
