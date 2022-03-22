import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import AttributeTypeDictionary from '../../../../../commonmodule/tack/attribute-type-dictionary-new';

import { view as ContentFilterTagGroupView } from '../../../../../viewlibraries/contentfiltertaggroupview/content-filter-tag-group-view';
import { view as MultiSelectSearchView } from '../../../../../viewlibraries/multiselectview/multi-select-search-view';
import { view as NumberLocaleView } from '../../../../../viewlibraries/numberlocaleview/number-locale-view';
import { view as ContentMeasurementMetricsView } from '../../../../../viewlibraries/measurementmetricview/content-measurement-metrics-view.js';
import ContentMeasurementMetricsViewModel from '../../../../../viewlibraries/measurementmetricview/model/content-measurement-metrics-view-model';
import { view as CustomDatePicker } from '../../../../../viewlibraries/customdatepickerview/customdatepickerview.js';
import ViewUtils from './utils/view-utils';
import MockDataForMeasurementMetricAndImperial from './../../../../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import FilterTypeDictionary from '../../../../../commonmodule/tack/filter-type-dictionary';
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  CONTENT_FILTER_ATTRIBUTE_DELETE_CLICKED: "content_filter_attribute_delete_clicked",
  CONTENT_FILTER_ATTRIBUTE_EXPAND_CLICKED: "content_filter_attribute_expand_clicked",
  CONTENT_FILTER_ATTRIBUTE_VALUE_TYPE_CLICKED: "content_filter_attribute_value_type_clicked",
  CONTENT_FILTER_ADD_ATTRIBUTE_CLICKED: "content_filter_add_attribute_clicked",
  CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED: "content_filter_attribute_value_changed",
  CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE: "content_filter_attribute_value_changed_for_range",
  CONTENT_FILTER_ATTRIBUTE_VALUE_DELETE_CLICKED: "content_filter_attribute_value_delete_clicked",
  CONTENT_FILTER_USER_VALUE_CHANGED: "content_filter_user_value_changed"
};

const oPropTypes = {
  appliedFilterData: ReactPropTypes.array,
  appliedFilterCollapseStatusMap: ReactPropTypes.object,
  userList: ReactPropTypes.array,
  masterAttributeList: ReactPropTypes.array,
  loadedAttributes: ReactPropTypes.object,
  loadedTags: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  loadedRoles: ReactPropTypes.object,
};

// @CS.SafeComponent
class ContentFilterElementsView extends React.Component {
  static propTypes = oPropTypes;

  handleContentFilterUserValueChanged = (sRoleId, aUsers) => {
    EventBus.dispatch(oEvents.CONTENT_FILTER_USER_VALUE_CHANGED, sRoleId, aUsers, this.props.filterContext);
  };

  getTypeDropdownView = (sSelectedId, sAttrId, sValId, sAttributeType) => {
    var aOptions = [];
    var sTypeForVisual = ViewUtils.getAttributeTypeForVisual(sAttributeType, sAttrId);
    var filter = new FilterTypeDictionary.FILTER_TYPES();
    if(sTypeForVisual == "number" || sTypeForVisual == "calculated" || sTypeForVisual == "measurementMetrics") {
      filter = new FilterTypeDictionary.FILTER_TYPES_FOR_NUMBER();
    }
    else if(sTypeForVisual == "date") {
      filter = new FilterTypeDictionary.FILTER_TYPES_FOR_DATE();
    }
    else if(ViewUtils.isAttributeTypeUser(sAttributeType)) {
      filter = new FilterTypeDictionary.FILTER_TYPES_FOR_USER_ATTRS();
    }
    CS.forEach(filter, function(sTypeVal, sKey){
      aOptions.push(<option value={sKey}>{sTypeVal}</option>)
    });

    return (<select value={sSelectedId} onChange={this.handleAttributeValTypeDropdownChanged.bind(this, sAttrId, sValId)}>{aOptions}</select>);
  };

  getAttributeInputViewByAttributeType = (sAttrId, oAttrVal, sAttributeType) => {
    var oLoadedAttributes = this.props.loadedAttributes;
    var oMasterAttribute = oLoadedAttributes[sAttrId] || oAttrVal;

    if(ViewUtils.isAttributeTypeUser(sAttributeType)) {
      var aUsers = this.props.userList;
      var aOptions = [];
      CS.forEach(aUsers, function (oUser) {
        aOptions.push(<option value={oUser.id}>{oUser.userName}</option>);
      });
      aOptions.unshift(<option value={"none"}>{getTranslation().NONE}</option>);
      return (
          <div className="attributeConditionUserInput">
            <select value={oAttrVal.value} onChange={this.handleAttributeValueChanged.bind(this, sAttrId, oAttrVal.id)}>{aOptions}</select>
          </div>
      );
    }
    else {

      var sSplitter = ViewUtils.getSplitter();
      var sTypeForVisual = ViewUtils.getAttributeTypeForVisual(sAttributeType, sAttrId);
      var sValue = oAttrVal.advancedSearchFilter ? oAttrVal.value : oAttrVal.label;

      switch (sTypeForVisual) {

        case "html":
        case "text":
        case "concatenated":
          return (<input className="attributeConditionInput"
                         type={"text"}
                         value={sValue}
                         onChange={this.handleAttributeValueChanged.bind(this, sAttrId, oAttrVal.id)}/>);
        case "number":
        case "calculated":
          var sPrecision = oMasterAttribute.precision;
          if (oAttrVal.type != "range") {
            return (
                <NumberLocaleView
                    value={sValue}
                    precision={sPrecision}
                    onBlur={this.handleAttributeValueNumberTypeChanged.bind(this, sAttrId, oAttrVal.id)}
                    disableNumberLocaleFormatting={oMasterAttribute.hideSeparator}
                />
            );
          }
          else {
            if (oAttrVal.from == null) oAttrVal.from = "";
            if (oAttrVal.to == null) oAttrVal.to = "";
            return (
                <div className="attributeMultipleInput">
                  <div className="attributeMultipleInputCell">
                    <div className="attributeMultipleInputLabel"> {getTranslation().FROM}:</div>
                    <NumberLocaleView value={oAttrVal.from}
                                      precision={sPrecision}
                                      onBlur={this.handleAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "from")}
                                      disableNumberLocaleFormatting={oMasterAttribute.hideSeparator}/>
                  </div>
                  <div className="attributeMultipleInputCell">
                    <div className="attributeMultipleInputLabel"> {getTranslation().TO}:</div>
                    <NumberLocaleView value={oAttrVal.to}
                                      precision={sPrecision}
                                      onBlur={this.handleAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "to")}
                                      disableNumberLocaleFormatting={oMasterAttribute.hideSeparator}/>
                  </div>
                </div>);
          }

        case "date":
          if (oAttrVal.type != "range") {
            sValue = sValue ? +sValue ? new Date(+sValue) : null : null;
            if (CS.isNaN(Date.parse(sValue))) {
              sValue = null;
            }
            return (<CustomDatePicker
                value={sValue}
                className="datePickerCustom"
                onChange={this.handleDateTypeAttributeValueChanged.bind(this, sAttrId, oAttrVal.id)}/>);
          }
          else {
            var sTo = oAttrVal.to;
            var sFrom = oAttrVal.from;
            sTo = sTo ? +sTo ? new Date(+sTo) : null : null;
            sFrom = sFrom ? +sFrom ? new Date(+sFrom) : null : null;
            if (CS.isNaN(Date.parse(sTo))) {
              sTo = null;
            }
            if (CS.isNaN(Date.parse(sFrom))) {
              sFrom = null;
            }
            return (
                <div className="attributeMultipleInputDate">
                  <div className="attributeMultipleInputDateOneSide">
                    <div className="attributeMultipleInputDateLabel">{getTranslation().FROM}:</div>
                    <CustomDatePicker
                        value={sFrom}
                        className="datePickerCustom"
                        onChange={this.handleDateTypeAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "from")}/>
                  </div>
                  <div className="attributeMultipleInputDateOneSide">
                    <div className="attributeMultipleInputDateLabel"> {getTranslation().TO}:</div>
                    <CustomDatePicker
                        value={sTo}
                        className="datePickerCustom"
                        onChange={this.handleDateTypeAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "to")}
                        endOfDay={true}/>
                  </div>
                </div>);
          }

        case 'measurementMetrics':
          if (oAttrVal.type != "range") {
            var oHandler = this.handleAttributeValueNumberTypeChanged.bind(this, sAttrId, oAttrVal.id);
            return (
                <div className="filterWithMeasurementMetrics">
                  {this.getMeasurementMetricView(sAttrId, oAttrVal.id, oAttrVal.value, oHandler)}
                </div>
            );
          }
          else {
            if (oAttrVal.from == null) oAttrVal.from = "";
            if (oAttrVal.to == null) oAttrVal.to = "";
            var oFromHandler = this.handleAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "from");
            var oToHandler = this.handleAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "to");
            return (
                <div className="measurementAttributeMultipleInput">
                  <div className="measurementAttributeMultipleInputCell">
                    <div className="measurementAttributeMultipleInputLabel"> {getTranslation().FROM}:</div>
                    <div className="filterWithMeasurementMetrics">
                      {this.getMeasurementMetricView(sAttrId, oAttrVal.id, oAttrVal.from, oFromHandler, "from")}
                    </div>
                  </div>
                  <div className="measurementAttributeMultipleInputCell">
                    <div className="measurementAttributeMultipleInputLabel"> {getTranslation().TO}:</div>
                    <div className="filterWithMeasurementMetrics">
                      {this.getMeasurementMetricView(sAttrId, oAttrVal.id, oAttrVal.to, oToHandler, "to")}
                    </div>
                  </div>
                </div>);
          }

        default:
          return null;
      }
    }

  };

  getMeasurementMetricView = (sAttrId, sValueId, sValue, oHandler, sRangeType) => {
    var oLoadedAttributes = this.props.loadedAttributes;
    var oMaster = oLoadedAttributes[sAttrId];

    let aAppliedFilterData = this.props.appliedFilterData;
    let oAttribute = CS.find(aAppliedFilterData,{id:sAttrId});
    let oChild = CS.find(oAttribute.children, {id: sValueId});

    var sSplitter = ViewUtils.getSplitter();

    var sType = oMaster.type;
    var sLabel = CS.getLabelOrCode(oMaster);
    var sRef = "attr" + sSplitter + oMaster.id;
    var sPlaceholder = oMaster.placeholder;
    var sDescription = oMaster.description;
    var bDisabled = false;
    let sDefaultUnit = oChild.defaultUnit || oMaster.defaultUnit;
    if(CS.isNotEmpty(sRangeType)){
      sDefaultUnit = sDefaultUnit[sRangeType];
    }
    let oMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
    var aConverterList = oMeasurementMetricAndImperial[sType];
    var oBaseUnit = CS.find(aConverterList, {isBase: true});
    var sBaseUnit = oBaseUnit ? oBaseUnit.unit : sDefaultUnit;
    let bConverterVisibility = ViewUtils.isAttributeTypePrice(sType)|| CS.isEmpty(aConverterList) ? false : true;

    let oProperties = {
      valueId: sValueId
    };
    CS.isNotEmpty(sRangeType) && (oProperties.rangeType = sRangeType);

    (sType !== AttributeTypeDictionary.CUSTOM_UNIT) && (oProperties.baseUnit = (CS.find(oMeasurementMetricAndImperial[sType], {isBase: true}))["unit"]);
    oProperties.defaultUnitAsHTML = oMaster.defaultUnitAsHTML;
    oProperties.disableValueChangeByDefaultUnit = ViewUtils.isAttributeTypePrice(sType);

    var iPrecision = Number(oMaster.precision);
    oProperties.disableNumberLocaleFormatting = oMaster.hideSeparator;

    let oModel = new ContentMeasurementMetricsViewModel(
        sAttrId,
        sLabel,
        sValue,
        sDefaultUnit,
        sBaseUnit,
        sPlaceholder,
        "",
        sDescription,
        bDisabled,
        bConverterVisibility,
        aConverterList,
        oProperties,
        iPrecision,
        sType
    );
    return (<ContentMeasurementMetricsView ref={sRef}
                                           key={sRef}
                                           onBlur={oHandler}
                                           filterContext={this.props.filterContext}
                                           model={oModel}/>);
  };

  getAttrValueViews = (aAttrVals, sAttrId, sAttributeType) => {

    var _this = this;
    var aViews = [];

    CS.forEach(aAttrVals, function (oAttrVal, iIndex) {

      var sType = oAttrVal.type;
      var oSelectView = _this.getTypeDropdownView(sType, sAttrId, oAttrVal.id, sAttributeType);
      var bIsEmptyNonEmpty = (sType == "empty" || sType == "notempty");
      var oAttributeInputView = bIsEmptyNonEmpty ? null : _this.getAttributeInputViewByAttributeType(sAttrId, oAttrVal, sAttributeType);

      var sOr = iIndex > 0 ? getTranslation().OR : "";
      aViews.push(
          <div className="attributeConditionWrapper" key={iIndex}>
            <div className="attributeConditionOrBlock">{sOr}</div>
            <div className="attributeConditionSelect">{oSelectView}</div>
            {oAttributeInputView}
            <div className="attributeConditionDelete"
                 onClick={_this.handleAttributeValueDeleteClicked.bind(_this, sAttrId, oAttrVal.id)}></div>
          </div>)
    });

    return aViews;
  };

  handleAttributeValTypeDropdownChanged = (sAttrId, sValId, oEvent) => {
    var sTypeId = oEvent.target.value;
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_TYPE_CLICKED, sAttrId, sValId, sTypeId, this.props.filterContext);
  };

  handleFilterElementDeleteClicked = (sElId) => {
    if (this.props.handleFilterElementDeleteClicked) {
      this.props.handleFilterElementDeleteClicked(sElId, this.props.filterContext);
    } else {
      EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_DELETE_CLICKED, sElId, this.props.filterContext);
    }
  };

  handleFilterElementExpandClicked = (sElId) => {
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_EXPAND_CLICKED, sElId, this.props.filterContext);
  };

  handleAddAttributeValueClicked = (sAttrId) => {
    EventBus.dispatch(oEvents.CONTENT_FILTER_ADD_ATTRIBUTE_CLICKED, sAttrId, this.props.filterContext);
  };

  handleAttributeValueChanged = (sAttrId, sValId, oEvent) => {
    var sVal = oEvent.target.value;
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED, sAttrId, sValId, sVal, this.props.filterContext);
  };

  handleAttributeValueNumberTypeChanged = (sAttrId, sValId, sVal) => {
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED, sAttrId, sValId, sVal, this.props.filterContext);
  };

  handleAttributeValueChangedForRange = (sAttrId, sValId, sRange, sVal) => {
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE, sAttrId, sValId, sVal, sRange, this.props.filterContext);
  };

  handleDateTypeAttributeValueChanged = (sAttrId, sValId, sNull, sValue) => {
    sValue = new Date(sValue).getTime();
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED, sAttrId, sValId, sValue, this.props.filterContext);
  };

  handleDateTypeAttributeValueChangedForRange = (sAttrId, sValId, sRange, sNull, sValue) => {
    sValue = new Date(sValue).getTime();
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE, sAttrId, sValId, sValue, sRange, this.props.filterContext);
  };

  handleAttributeValueDeleteClicked = (sAttrId, sValId) => {
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_DELETE_CLICKED, sAttrId, sValId, this.props.filterContext);
  };

 getCustomIcon = (sKey) => {
   let sImageSrc = ViewUtils.getIconUrl(sKey);
   return ( <div className="customIcon">
         <ImageFitToContainerView imageSrc={sImageSrc}/>
       </div>
   )
 };

  getAttributeFilterView = (oAttr, iIndex) => {
    var _this = this;
    var oAppliedFilterCollapseStatusMap = this.props.appliedFilterCollapseStatusMap;
    var bIsCollapsed = oAppliedFilterCollapseStatusMap[oAttr.id].isCollapsed;

    var aMandatoryAttrViews = _this.getAttrValueViews(oAttr.children, oAttr.id, oAttr.type);
    var sAttributeExpandCollapseClass = bIsCollapsed ? "collapsed " : "expanded ";
    sAttributeExpandCollapseClass += "attributeExpandCollapse ";
    var sAttributeBodyClass = "attributeBody ";
    sAttributeBodyClass += bIsCollapsed ? "collapsed " : "expanded ";

    var sAnd = iIndex > 0 ? getTranslation().AND : "";

    let oIconDOM = this.getCustomIcon(oAttr.iconKey);
    return (
        <div className="attributeWrapper">
          <div className="attributeHeader">
            <div className="attributeHeaderAndOrContainer">{sAnd}</div>
            <div className={sAttributeExpandCollapseClass}
                 onClick={_this.handleFilterElementExpandClicked.bind(_this, oAttr.id)}></div>
            {oIconDOM}
            <div className="attributeText"
                 onClick={_this.handleFilterElementExpandClicked.bind(_this, oAttr.id)}>{CS.getLabelOrCode(oAttr)}</div>
            <div className="attributeDelete"
                 onClick={_this.handleFilterElementDeleteClicked.bind(_this, oAttr.id)}></div>
            <div className="attributeAddCondition"
                 onClick={_this.handleAddAttributeValueClicked.bind(_this, oAttr.id)}></div>
          </div>
          <div className={sAttributeBodyClass}>
            <div className="attributeValueWrapper">
              <div className="attributeValueWrapperBody">{aMandatoryAttrViews}</div>
            </div>
          </div>
        </div>);
  };

  //Role related Codes
  getUserValueViews = (oRole) => {
    var aUsers = this.props.userList;
    var aViews = [];

    aViews.push(
        (<div className="contentFilterMSSContainer">
          <div className="contentFilterMSSIcon"></div>
          <MultiSelectSearchView
              disabled={false}
              items={aUsers}
              selectedItems={oRole.users}
              isMultiSelect={true}
              onApply={this.handleContentFilterUserValueChanged.bind(this, oRole.id)}/>
        </div>)
    );

    return aViews;
  };

  getRoleFilterView = (oRole, iIndex) => {
    var _this = this;

    var oAppliedFilterCollapseStatusMap = this.props.appliedFilterCollapseStatusMap;
    var bIsCollapsed = oAppliedFilterCollapseStatusMap[oRole.id].isCollapsed;

    var sRoleExpandCollapseClass = bIsCollapsed ? "collapsed " : "expanded ";
    sRoleExpandCollapseClass += "attributeExpandCollapse ";
    var sRoleBodyClass = "attributeBody tagBody ";
    sRoleBodyClass += bIsCollapsed ? "collapsed " : "expanded ";

    var aUserAttrViews = _this.getUserValueViews(oRole);

    var sAnd = (iIndex == 0) ? "" : getTranslation().AND;
    return (
        <div className="attributeWrapper">
          <div className="attributeHeader">
            <div className="attributeHeaderAndOrContainer">{sAnd}</div>
            <div className={sRoleExpandCollapseClass}
                 onClick={_this.handleFilterElementExpandClicked.bind(_this, oRole.id)}></div>
            <div className="attributeText"
                 onClick={_this.handleFilterElementExpandClicked.bind(_this, oRole.id)}>{CS.getLabelOrCode(oRole)}</div>
            <div className="attributeDelete"
                 onClick={_this.handleFilterElementDeleteClicked.bind(_this, oRole.id)}></div>
          </div>
          <div className={sRoleBodyClass}>
            {aUserAttrViews}
          </div>
        </div>);
  };

  //Tag related codes
  getTagGroupView = (aChildrenTags, oMasterTag, loadedTags) => {
    var oExtraData = {
      filterContext: this.props.filterContext,
      outerContext: (this.props.context) ? this.props.context : "contentFilterTagsInner"
    };

    return (<ContentFilterTagGroupView
        masterTag={oMasterTag}
        filterTags={aChildrenTags}
        extraData={oExtraData}
        loadedTags={loadedTags}
        showDefaultIcon={this.props.showDefaultIcon}
    />);
  };

  getTagFilterView = (oTag, iIndex) => {
    var _this = this;
    var oAppliedFilterCollapseStatusMap = this.props.appliedFilterCollapseStatusMap;
    var oLoadedTags = this.props.loadedTags;
    var oMasterTag = oLoadedTags[oTag.id] || oTag;
    var bIsCollapsed = oAppliedFilterCollapseStatusMap[oTag.id].isCollapsed;

    var aTagGroupViews = _this.getTagGroupView(oTag.children, oMasterTag, oLoadedTags);
    var sTagExpandCollapseClass = bIsCollapsed ? "collapsed " : "expanded ";
    sTagExpandCollapseClass += "attributeExpandCollapse ";
    var sTagBodyClass = "attributeBody tagBody ";
    sTagBodyClass += bIsCollapsed ? "collapsed " : "expanded ";

    var sAnd = (iIndex == 0) ? "" : getTranslation().AND;

    let oIconDOM = this.getCustomIcon(oTag.iconKey);
    return (<div className="attributeWrapper">
      <div className="attributeHeader">
        <div className="attributeHeaderAndOrContainer">{sAnd}</div>
        <div className={sTagExpandCollapseClass}
             onClick={_this.handleFilterElementExpandClicked.bind(_this, oTag.id)}></div>
        {oIconDOM}
        <div className="attributeText"
             onClick={_this.handleFilterElementExpandClicked.bind(_this, oTag.id)}>{CS.getLabelOrCode(oTag)}</div>
        <div className="attributeDelete"
             onClick={_this.handleFilterElementDeleteClicked.bind(_this, oTag.id)}></div>
      </div>
      <div className={sTagBodyClass}>
        {aTagGroupViews}
      </div>
    </div>);
  };

  getElementColumnarView = () => {
    var aAppliedFilterData = this.props.appliedFilterData;
    let aTypesToHide = ["dataRules", "colorVoilation"]; // Note: Data Rules need to be hidden in Advanced Filters
    var _this = this;
    var aAllElements = [];
    let index = -1;
    CS.forEach(aAppliedFilterData, function (oAppliedFilter) {
      index++;
      if (CS.includes(aTypesToHide, oAppliedFilter.type) || oAppliedFilter.isHiddenInAdvancedFilters) { // if Filter
        // Type is to be hidden continue;
        index--;
        return true;
      } else if( CS.includes(oAppliedFilter.type, "attribute")){
        aAllElements.push(_this.getAttributeFilterView(oAppliedFilter, index) || []);
      }else if(ViewUtils.isTag(oAppliedFilter.type)){
        aAllElements.push(_this.getTagFilterView(oAppliedFilter, index) || []);
      }else { //for roles
        /**code to be removed for roles*/
        aAllElements.push(_this.getRoleFilterView(oAppliedFilter, index) || []);
      }
    });

    return(
        <div className="contentFilterItem">
          {aAllElements}
        </div>
    );
  };

  render() {
    return this.getElementColumnarView();
  }
}

export const view = ContentFilterElementsView;
export const events = oEvents;
