import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import { view as MSSView } from '../../../../../viewlibraries/multiselectview/multi-select-search-view';
import { view as TabLayoutView } from '../../../../../viewlibraries/tablayoutview/tab-layout-view';
import { view as LazyMSSView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import { view as AttributeTagSelectorView } from '../../../../../viewlibraries/attributeandtagselectorview/attribute-and-tag-selector-view';
import MockRuntimeMappingTabItemsList from '../tack/mock/mock-data-for-runtime-mapping';
import FilterItemsList from '../tack/mapping-filter-items-list';
import FilterDictionary from '../tack/mapping-filter-items-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';
import { view as ButtonView } from '../../../../../viewlibraries/buttonview/button-view';
const getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  PROFILE_CONFIG_MAPPED_ELEMENT_CHANGED: "profile_config_mapped_element_changed",
  PROFILE_CONFIG_UNMAPPED_ELEMENT_CHANGED: "profile_config_unmapped_element_changed",
  PROFILE_CONFIG_ISIGNORED_TOGGLED: "profile_config_isignored_toggled",
  PROFILE_CONFIG_MAPPED_TAG_VALUE_CHANGED: "profile_config_mapped_tag_value_changed",
  ENDPOINT_MAPPING_VIEW_TAG_VALUE_IGNORE_CASE_TOGGLED: "endpoint_mapping_view_tag_value_ignore_case_toggled",
  HANDLE_ENDPOINT_MAPPING_VIEW_BACK_BUTTON_CLICKED: "handle_endpoint_mapping_view_back_button_clicked",
  HANDLE_ENDPOINT_MAPPING_VIEW_IMPORT_BUTTON_CLICKED: "handle_endpoint_mapping_view_import_button_clicked",
  HANDLE_ENDPOINT_MAPPING_TAB_CLICKED: "handle_endpoint_mapping_tab_clicked",
  HANDLE_ENDPOINT_MAPPING_UNMAPPED_ELEMENT_IS_IGNORED_TOGGLED: "handle_endpoint_mapping_unmapped_element_is_ignored_toggled",
  HANDLE_ENDPOINT_MAPPING_FILTER_OPTION_CHANGED : "handle_endpoint_mapping_filter_option_changed"
};

const oPropTypes = {
  activeEndpoint: ReactPropTypes.object,
  selectedMappingRows: ReactPropTypes.array,
  endPointMappingList: ReactPropTypes.array,
  activeTabId: ReactPropTypes.string,
  selectedMappingFilterId: ReactPropTypes.string,
  mappingFilterProps:ReactPropTypes.object,
  unmappedColumnValuesList: ReactPropTypes.object,
  endPointReferencedData: ReactPropTypes.object,
  endPointReqResInfo: ReactPropTypes.object,
  propertyRowTypeData: ReactPropTypes.object,
};

// @CS.SafeComponent
class EndpointMappingView extends React.Component {
  static propTypes = oPropTypes;

  handleMappedElementValueChanged = (sId, sTabId, sName, aSelectedItems, oReferencedData) => {
    let sMappedElementId = !CS.isEmpty(aSelectedItems) ? aSelectedItems[0] : "";
    if(CS.isEmpty(sMappedElementId)){
      return;
    }

    let oMappedPropertyRowData = this.props.propertyRowTypeData;
    let sSelectedType = oMappedPropertyRowData[sName];
    let sOptionType = sTabId;
    if(sSelectedType === "attribute"){
      sOptionType = ConfigDataEntitiesDictionary.ATTRIBUTES;
    }else if(sSelectedType === "tag"){
      sOptionType = ConfigDataEntitiesDictionary.TAGS;
    }

    if(sOptionType === "properties"){
      sOptionType = ConfigDataEntitiesDictionary.ATTRIBUTES;
    }

    EventBus.dispatch(oEvents.PROFILE_CONFIG_MAPPED_ELEMENT_CHANGED, sId, sMappedElementId, sOptionType, oReferencedData);
  };

  handleUnmappedElementValueChanged = (sName, sTabId, aSelectedItems, oReferencedData) => {
    let sMappedElementId = !CS.isEmpty(aSelectedItems) ? aSelectedItems[0] : "";
    if(CS.isEmpty(sMappedElementId)){
      return;
    }

    let oUnmappedPropertyRowData = this.props.propertyRowTypeData;
    let sSelectedType = oUnmappedPropertyRowData[sName];
    let sOptionType = sTabId;
    if(sSelectedType === "attribute"){
      sOptionType = ConfigDataEntitiesDictionary.ATTRIBUTES;
    }else if(sSelectedType === "tag"){
      sOptionType = ConfigDataEntitiesDictionary.TAGS;
    }

    if(sOptionType === "properties"){
      sOptionType = ConfigDataEntitiesDictionary.ATTRIBUTES;
    }

    EventBus.dispatch(oEvents.PROFILE_CONFIG_UNMAPPED_ELEMENT_CHANGED, sName, sMappedElementId, sOptionType, oReferencedData);
  };

  handleMappedPropertyTypeChanged = (sId, aSelectedItems) => {
    let sSelectedType = !CS.isEmpty(aSelectedItems) ? aSelectedItems[0] : "attribute";
    let sOptionType = "";

    if(sSelectedType === "attribute"){
      sOptionType = ConfigDataEntitiesDictionary.ATTRIBUTES;
    }else if(sSelectedType === "tag"){
      sOptionType = ConfigDataEntitiesDictionary.TAGS;
    }

    EventBus.dispatch(oEvents.PROFILE_CONFIG_MAPPED_ELEMENT_CHANGED, sId, "", sOptionType, {});
  };

  handleUnmappedPropertyTypeChanged = (sName, aSelectedItems) => {
    let sSelectedType = !CS.isEmpty(aSelectedItems) ? aSelectedItems[0] : "attribute";
    let sOptionType = "";

    if(sSelectedType === "attribute"){
      sOptionType = ConfigDataEntitiesDictionary.ATTRIBUTES;
    }else if(sSelectedType === "tag"){
      sOptionType = ConfigDataEntitiesDictionary.TAGS;
    }

    EventBus.dispatch(oEvents.PROFILE_CONFIG_UNMAPPED_ELEMENT_CHANGED, sName, "", sOptionType, {});
  };

  handleUnmappedElementIsIgnoredToggled = (sId, sTabId) => {
    EventBus.dispatch(oEvents.HANDLE_ENDPOINT_MAPPING_UNMAPPED_ELEMENT_IS_IGNORED_TOGGLED, sId, sTabId);
  };

  handleIsIgnoredToggled = (sId, sTabId) => {
    EventBus.dispatch(oEvents.PROFILE_CONFIG_ISIGNORED_TOGGLED, sId, sTabId);
  };

  isTypeTag = (sTagId) => {
    let oReferencedData = this.props.endPointReferencedData;
    let oReferencedTags = oReferencedData.referencedTags || {};
    let oReferencedTag = !CS.isEmpty(oReferencedTags) ? oReferencedTags[sTagId] : {};
    return !CS.isEmpty(oReferencedTag);
  };

  handleMappedTagValueChanged = (sId, sMappedTagValueId, sTagGroupId, aSelectedItems, oReferencedTagValues) => {
    let sMappedElementId = !CS.isEmpty(aSelectedItems) ? aSelectedItems[0] : "";
    if(CS.isEmpty(sMappedElementId)){
      return;
    }
    EventBus.dispatch(oEvents.PROFILE_CONFIG_MAPPED_TAG_VALUE_CHANGED, sId, sMappedTagValueId, sMappedElementId, sTagGroupId, oReferencedTagValues);
  };

  handleTagValueIgnoreCaseToggled = (sId, sMappedTagValueId, oEvent) => {
    EventBus.dispatch(oEvents.ENDPOINT_MAPPING_VIEW_TAG_VALUE_IGNORE_CASE_TOGGLED, sId, sMappedTagValueId);
  };

  getTagValueRow = (sId, bIsDisabled, oMappingFilterProps) => {
    let aEndPointMappingList = this.props.endPointMappingList;
    let oEndPointReqResInfo = this.props.endPointReqResInfo;
    let oConfigRuleMapping = CS.find(aEndPointMappingList, {id: sId});
    let sSelectedMappingFilterId =  this.props.selectedMappingFilterId;

    if(oConfigRuleMapping) {
      let aColumnsName = oConfigRuleMapping.columnNames;
      let aTagValueMapping = oConfigRuleMapping.tagValueMappings;

      if(!CS.isEmpty(aTagValueMapping)) {
        let sTagGroupId = oConfigRuleMapping.mappedElementId;
        let oTagValueReqResInfo = CS.cloneDeep(oEndPointReqResInfo.tagValues);
        let aTagValueMappedRows = [];
        let _this = this;

        oTagValueReqResInfo.customRequestModel = {
          tagGroupId: sTagGroupId
        };

        let oReferencedTagValueData = {};
        let oReferencedData = this.props.endPointReferencedData;
        let oReferencedTags = oReferencedData.referencedTags || {};
        let oReferencedTag = !CS.isEmpty(oReferencedTags) ? oReferencedTags[sTagGroupId] : {};
        CS.forEach(oReferencedTag.children, function(oTagChildren){
          oReferencedTagValueData[oTagChildren.id] = oTagChildren;
        });

        CS.forEach(aColumnsName, function (sColumnName) {
          let oTagValueMapping = CS.find(aTagValueMapping, {columnName: sColumnName}) || {};
          let aMappedTagValueList = oTagValueMapping.mappings;

          if(!CS.isEmpty(aMappedTagValueList)) {
            CS.forEach(aMappedTagValueList, function (oMappedTagValue) {
              let sTagValueId = oMappedTagValue.mappedTagValueId;
              let aSelectedItems = !CS.isEmpty(sTagValueId) ? [sTagValueId] : [];
              let aMappedTagValueIds = oMappingFilterProps.mappedTagValueIds;
              let aUnmappedTagValueIds = oMappingFilterProps.unMappedTagValueIds;

              if (sSelectedMappingFilterId === FilterDictionary.MAPPED && !CS.includes(aMappedTagValueIds, oMappedTagValue.id)) {
                return;
              } else if(sSelectedMappingFilterId === FilterDictionary.UNMAPPED && !CS.includes(aUnmappedTagValueIds, oMappedTagValue.id)){
                return;
              }

              aTagValueMappedRows.push(
                  <div className="tagValueRowWrapper">
                    <div className="tagValuesWrapper">
                      <div className="fieldNameLabel">{oMappedTagValue.tagValue}</div>
                    </div>
                    <div className="mappingElement">
                      {_this.getLazyMSSView(aSelectedItems, oReferencedTagValueData, oTagValueReqResInfo,
                          _this.handleMappedTagValueChanged.bind(_this, sId, oMappedTagValue.id, sTagGroupId), sTagGroupId, true, bIsDisabled)}
                    </div>
                    <div className="ignoreCaseWrapper" onClick={_this.handleTagValueIgnoreCaseToggled.bind(_this, sId, oMappedTagValue.id)}>
                      <input className="checkbox" type="checkbox" checked={oMappedTagValue.isIgnoreCase} />
                      <div className="checkboxLabel">{getTranslation().IGNORE_CASE}</div>
                    </div>
                  </div>);
            });
          }
        });

        return aTagValueMappedRows;
      }
    }
    return null;
  };

  handleBackButtonOnClick = () => {
    EventBus.dispatch(oEvents.HANDLE_ENDPOINT_MAPPING_VIEW_BACK_BUTTON_CLICKED, this);
  };

  handleImportButtonClick = () => {
    EventBus.dispatch(oEvents.HANDLE_ENDPOINT_MAPPING_VIEW_IMPORT_BUTTON_CLICKED, this);
  };

  handleRuntimeMappingTabClicked = (sTabId) => {
    EventBus.dispatch(oEvents.HANDLE_ENDPOINT_MAPPING_TAB_CLICKED, sTabId);
  };

  getMappingActionItemsView = () => {

    let sSelectedMappingFilterId =  this.props.selectedMappingFilterId;

    if(CS.isEmpty(sSelectedMappingFilterId)){
      sSelectedMappingFilterId = FilterDictionary.ALL;
    }

    let oBackButtonView = (
        <div className="mappingViewActionItemWrapperLeft">
          <ButtonView
              id={"onboardingBackButton"}
              showLabel={false}
              isDisabled={false}
              tooltip={getTranslation().BACK}
              className={"onboardingBackButton"}
              onChange={this.handleBackButtonOnClick}
              theme={"light"}
          />
        </div>
    );

    let oFilterView = (
        <div className="filterViewWrapper">
          <MSSView
              disabled={false}
              isMultiSelect={false}
              cannotRemove={true}
              items={new FilterItemsList()}
              selectedItems={[sSelectedMappingFilterId]}
              onApply={this.handleFilterOptionChanged}
              showSearch={false}
              showCustomIcon={true}
          />
        </div>
    );

    let oImportMappingButton = (
        <ButtonView
            id={"onboardingImportFile"}
            showLabel={false}
            isDisabled={false}
            tooltip={getTranslation().ONBOARDING_IMPORT_FILE}
            className={"onboardingImportFile"}
            onChange={this.handleImportButtonClick}
        />
    );

    return (
        <div className="mappingViewActionItemWrapper">
          {oBackButtonView}
          <div className="mappingViewActionItemWrapperRight">
            <div className={"dropdownSection"}>
              {oFilterView}
            </div>
            {oImportMappingButton}
          </div>
        </div>
    );
  };

  getAttributeTagSelectorView = (sPropertyId, sItemId, oReferencedData, bIsDisabled, fOnListChanged, fOnTypeChanged, sType) => {
    let oReferencedAttributes = !CS.isEmpty(oReferencedData) ? oReferencedData.referencedAttributes : {};
    let oReferencedTags = !CS.isEmpty(oReferencedData) ? oReferencedData.referencedTags : {};
    let oReferencedProperties = !CS.isEmpty(oReferencedTags[sItemId]) ? oReferencedTags : oReferencedAttributes;
    let aSelectedItems = !CS.isEmpty(sItemId) ? [sItemId] : [];

    return (
        <div className="attributeTagSelectorContainer">
          <AttributeTagSelectorView
              context={sPropertyId}
              isTypeListDisabled={bIsDisabled}
              selectedType={sType}
              onTypeListChanged={fOnTypeChanged}
              isPropertyListDisabled={bIsDisabled}
              isPropertyListMultiSelect={false}
              referencedProperties={oReferencedProperties}
              selectedProperties={aSelectedItems}
              onPropertyListChanged={fOnListChanged}
          />
        </div>
    );
  };

  getUnmappedColumnsPropertiesView = (aUnmappedColumns, sTabId, sUnmappedColumn, oReferencedData) => {
    let _this = this;
    let aUnmappedRows = [];
    let oUnmappedColumnValuesList = this.props.unmappedColumnValuesList;
    let oRowPropertyData = this.props.propertyRowTypeData;
    let oUnmappedColumnValue = oUnmappedColumnValuesList[sUnmappedColumn];
    let sSelectedMappingFilterId = _this.props.selectedMappingFilterId;

    CS.forEach(aUnmappedColumns, function (sColumn) {
      let oUnmappedColumn = oUnmappedColumnValue[sColumn];
      let sIsIgnoredClass = oUnmappedColumn.isIgnored ? "isIgnored selected" : "isIgnored ";
      let sType = oRowPropertyData[sColumn];

      let bIsValid =  _this.checkRowValidity(sColumn);
      if(!bIsValid){
        return;
      }

      let sRowHighlightClassName = (sSelectedMappingFilterId === "All" ? " highlightUnmappedRow" : "");

      aUnmappedRows.push(
          <div className={"mappingRow unmapped" + sRowHighlightClassName}>
            <div className="fieldName">
              <div className="fieldNameLabel">{sColumn}</div>
            </div>
            <div className="mappingElement">
              {_this.getAttributeTagSelectorView(sColumn, "", oReferencedData, oUnmappedColumn.isIgnored,
                  _this.handleUnmappedElementValueChanged.bind(_this, sColumn, sTabId), _this.handleUnmappedPropertyTypeChanged.bind(_this, sColumn), sType)}
            </div>
            <div className={sIsIgnoredClass} onClick={_this.handleUnmappedElementIsIgnoredToggled.bind(_this, sColumn,sTabId)}></div>
          </div>
      );
    });

    return aUnmappedRows;
  };

  checkRowValidity = (sName, bIsTag) => {
    let sSelectedMappingFilterId = this.props.selectedMappingFilterId;
    let oMappingFilterProps = this.props.mappingFilterProps;

    let bIsValid = false;
    if (oMappingFilterProps.hasOwnProperty(sName)) {
      if (oMappingFilterProps[sName].isIgnored && sSelectedMappingFilterId === FilterDictionary.IGNORED) {
        bIsValid = true;
      }

      if(oMappingFilterProps[sName].isMapped && !oMappingFilterProps[sName].isIgnored && sSelectedMappingFilterId === FilterDictionary.UNMAPPED && bIsTag) {
        bIsValid = !oMappingFilterProps[sName].isTagValuesMapped;
      }

      if(oMappingFilterProps[sName].isMapped && !oMappingFilterProps[sName].isIgnored && sSelectedMappingFilterId === FilterDictionary.MAPPED && bIsTag) {
        bIsValid = true;
      }
      else if (oMappingFilterProps[sName].isMapped && !oMappingFilterProps[sName].isIgnored && sSelectedMappingFilterId === FilterDictionary.MAPPED ) {
        bIsValid = true;
      }

      if ((!oMappingFilterProps[sName].isMapped && !oMappingFilterProps[sName].isIgnored) && sSelectedMappingFilterId === FilterDictionary.UNMAPPED) {
        bIsValid = true;
      }

      if (sSelectedMappingFilterId === FilterDictionary.ALL) {
        bIsValid = true;
      }
    }

    return bIsValid;
  };

  getMappedColumnsPropertiesView = (aMappedColumns, sTabId, oReferencedData) => {
    let _this = this;
    let aMappedRows = [];
    let oMappedRowPropertyData = this.props.propertyRowTypeData;
    let sSelectedMappingFilterId = _this.props.selectedMappingFilterId;
    let oMappingFilterProps = this.props.mappingFilterProps;

    CS.forEach(aMappedColumns, function (oRow) {
      let sId = oRow.id;
      let sIsIgnoredClass = oRow.isIgnored ? "isIgnored selected" : "isIgnored ";
      let sName = CS.join(oRow.columnNames, "");
      let sValue = oRow.mappedElementId || getTranslation().PLEASE_SELECT;
      let sType = oMappedRowPropertyData[sName];

      let bIsValid = _this.checkRowValidity(sName, oRow.hasOwnProperty("tagValueMappings"));
      if (!bIsValid) {
        return;
      }
      let sHighlightRowClassName = (sSelectedMappingFilterId === "All" && !oRow.mappedElementId && !oRow.isIgnored ? " highlightUnmappedRow" : "");

      aMappedRows.push(
          <div className={"mappingRow" + sHighlightRowClassName}>
            <div className="fieldName">
              <div className="fieldNameLabel">{sName}</div>
            </div>
            <div className="mappingElement">
              {_this.getAttributeTagSelectorView(sId, sValue, oReferencedData, oRow.isIgnored,
                  _this.handleMappedElementValueChanged.bind(_this, sId, sTabId, sName), _this.handleMappedPropertyTypeChanged.bind(_this, sId), sType)}
            </div>
            <div className={sIsIgnoredClass} onClick={_this.handleIsIgnoredToggled.bind(_this, sId, sTabId)}></div>
            {_this.isTypeTag(oRow.mappedElementId) ? <div className="mappingTagValuesRow">
              {_this.getTagValueRow(sId, oRow.isIgnored, oMappingFilterProps[sName])}
            </div> : null}
          </div>);
    });

    return aMappedRows;
  };

  getLazyMSSView = (aSelectedItems, oReferencedData, oReqResInfo, fOnApply, sContext, bCannotRemove, bIsDisabled) => {

    return (
        <div className="lazyMSSContainer">
          <LazyMSSView
              selectedItems={aSelectedItems}
              context={sContext}
              referencedData={oReferencedData}
              requestResponseInfo={oReqResInfo}
              onApply={fOnApply}
              cannotRemove={bCannotRemove}
              disabled={bIsDisabled}
          />
        </div>
    );
  };

  getMappedColumnsView = (aMappedColumns, sTabId, oReferencedData, oReqResInfo) => {
    let _this = this;
    let aMappedRows = [];
    let sSelectedMappingFilterId = _this.props.selectedMappingFilterId;

    CS.forEach(aMappedColumns, function (oRow) {
      let sId = oRow.id;
      let sIsIgnoredClass = oRow.isIgnored ? "isIgnored selected" : "isIgnored ";
      let sName = CS.join(oRow.columnNames, "");
      let sValue = oRow.mappedElementId || getTranslation().PLEASE_SELECT;
      let aSelectedItems = !CS.isEmpty(sValue) ? [sValue] : [];

      let bIsValid =  _this.checkRowValidity(sName);
      if(!bIsValid){
        return;
      }

      let sMappingRowHighlightClassName = (sSelectedMappingFilterId === "All" && !oRow.mappedElementId && !oRow.isIgnored ? " highlightUnmappedRow" : "");

      aMappedRows.push(
          <div className={"mappingRow" + sMappingRowHighlightClassName}>
            <div className="fieldName">
              <div className="fieldNameLabel">{sName}</div>
            </div>
            <div className="mappingElement">
              {_this.getLazyMSSView(aSelectedItems, oReferencedData, oReqResInfo,
                  _this.handleMappedElementValueChanged.bind(_this, sId, sTabId, sName), sId, true, oRow.isIgnored)}
            </div>
            <div className={sIsIgnoredClass} onClick={_this.handleIsIgnoredToggled.bind(_this, sId, sTabId)}></div>
          </div>);
    });

    return aMappedRows;
  };

  getUnmappedColumnsView = (aUnmappedColumns, sTabId, sUnmappedColumn, oReferencedData, oReqResInfo) => {
    let _this = this;
    let aUnmappedRows = [];
    let oUnmappedColumnValuesList = this.props.unmappedColumnValuesList;
    let oUnmappedColumnValue = oUnmappedColumnValuesList[sUnmappedColumn];
    let sSelectedMappingFilterId = _this.props.selectedMappingFilterId;

    CS.forEach(aUnmappedColumns, function (sColumn) {
      let oUnmappedColumn = oUnmappedColumnValue[sColumn];
      let sIsIgnoredClass = oUnmappedColumn.isIgnored ? "isIgnored selected" : "isIgnored ";

      let bIsValid =  _this.checkRowValidity(sColumn);
      if(!bIsValid){
        return;
      }

      let sRowHighlightClassName = (sSelectedMappingFilterId === "All" ? " highlightUnmappedRow" : "");

      aUnmappedRows.push(
          <div className={"mappingRow unmapped" + sRowHighlightClassName}>
            <div className="fieldName">
              <div className="fieldNameLabel">{sColumn}</div>
            </div>
            <div className="mappingElement">
              {_this.getLazyMSSView([], oReferencedData, oReqResInfo,
                  _this.handleUnmappedElementValueChanged.bind(_this, sColumn, sTabId), sColumn, true, oUnmappedColumn.isIgnored)}
            </div>
            <div className={sIsIgnoredClass}
                 onClick={_this.handleUnmappedElementIsIgnoredToggled.bind(_this, sColumn, sTabId)}></div>
          </div>
      );
    });

    return aUnmappedRows;
  };

  getTabDetailView = (oTabDetail) => {
    let oProps = this.props;
    let sId = oTabDetail.id;
    let aMappedColumns = [];
    let aUnmappedColumns = [];
    let oActiveEndpoint = oProps.activeEndpoint;
    let sUnmappedColumn = "";
    let aMappedRowsView = null;
    let aUnmappedRowsView = null;
    let oReqResInfo = oProps.endPointReqResInfo;
    let oReferencedData = oProps.endPointReferencedData;

    switch (sId) {
      case "properties":
        aMappedColumns = oActiveEndpoint.attributeMappings.concat(oActiveEndpoint.tagMappings);
        aUnmappedColumns = oActiveEndpoint.unmappedColumns || [];
        sUnmappedColumn = "unmappedColumns";
        aMappedRowsView = this.getMappedColumnsPropertiesView(aMappedColumns, sId, oReferencedData);
        aUnmappedRowsView = this.getUnmappedColumnsPropertiesView(aUnmappedColumns, sId, sUnmappedColumn, oReferencedData);
        break;

      case "classes":
        aMappedColumns = oActiveEndpoint.classMappings;
        aUnmappedColumns = oActiveEndpoint.unmappedKlassColumns;
        sUnmappedColumn = "unmappedKlassColumns";
        aMappedRowsView = this.getMappedColumnsView(aMappedColumns, sId, oReferencedData.referencedKlasses, oReqResInfo.classes);
        aUnmappedRowsView = this.getUnmappedColumnsView(aUnmappedColumns, sId, sUnmappedColumn, oReferencedData.referencedKlasses, oReqResInfo.classes);
        break;

      case "taxonomies":
        aMappedColumns = oActiveEndpoint.taxonomyMappings;
        aUnmappedColumns = oActiveEndpoint.unmappedTaxonomyColumns;
        sUnmappedColumn = "unmappedTaxonomyColumns";
        aMappedRowsView = this.getMappedColumnsView(aMappedColumns, sId, oReferencedData.referencedTaxonomies, oReqResInfo.taxonomies);
        aUnmappedRowsView = this.getUnmappedColumnsView(aUnmappedColumns, sId, sUnmappedColumn, oReferencedData.referencedTaxonomies, oReqResInfo.taxonomies);
        break;
    }

    if (CS.isEmpty(aMappedRowsView) && CS.isEmpty(aUnmappedRowsView)) {
      return (
          <div className="nothingToDisplayContainer">
            <div className="nothingToDisplay">{getTranslation().NOTHING_TO_DISPLAY}</div>
          </div>
      );
    } else {
      return (
          <div className="mappingViewTabDetailView">
            <div className="mappingViewHeader">
              <div className="mappingViewHeaderElement columnName">{getTranslation().FIELD}</div>
              <div className="mappingViewHeaderElement mappedElement">{getTranslation().MAPPED_ELEMENT}</div>
            </div>
            <div className="mappingRows">
              {aMappedRowsView}
              {aUnmappedRowsView}
            </div>
          </div>
      )
    }

  };

  handleFilterOptionChanged (aIds) {
    EventBus.dispatch(oEvents.HANDLE_ENDPOINT_MAPPING_FILTER_OPTION_CHANGED, aIds[0]);
  };

  getRuntimeMappingView = () => {
    var _this = this;
    var aTabList = new MockRuntimeMappingTabItemsList();
    var sActiveTabId = this.props.activeTabId;
    var oTabBodyView = null;
    let sSelectedMappingFilterId =  this.props.selectedMappingFilterId;

    if(CS.isEmpty(sSelectedMappingFilterId)){
      sSelectedMappingFilterId = FilterDictionary.ALL;
    }

    CS.forEach(aTabList, function (oTabItem) {
      if(sActiveTabId === oTabItem.id) {
        oTabBodyView = _this.getTabDetailView(oTabItem);
      }
    });

    return (
          <TabLayoutView
              tabList={aTabList}
              activeTabId={sActiveTabId}
              onChange={this.handleRuntimeMappingTabClicked}
              addBorderToBody={true}
              selectedFilter = {sSelectedMappingFilterId}>
            {oTabBodyView}
          </TabLayoutView>
    )
  };

  render() {
    return (
        <div className="mappingView">
          {this.getMappingActionItemsView()}
          {this.getRuntimeMappingView()}
        </div>
    );
  }
}

export const view = EndpointMappingView;
export const events = oEvents;
