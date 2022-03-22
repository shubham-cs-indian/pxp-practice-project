import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import {getTranslations as getTranslation} from '../../commonmodule/store/helper/translation-manager.js';
import {view as LazyContextMenuView} from '../lazycontextmenuview/lazy-context-menu-view';
import {view as SimpleSearchBarView} from "../simplesearchbarview/simple-search-bar-view";
import TooltipView from "../tooltipview/tooltip-view";
import MappingTypeDictionary from "../../commonmodule/tack/mapping-type-dictionary";
import {view as GridYesNoPropertyView} from "../gridview/grid-yes-no-property-view";
import {view as NothingFoundView} from "../nothingfoundview/nothing-found-view";
import MockDataForMapSummaryHeader from "../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-map-summery-header";


const oEvents = {
  PROPERTY_GROUP_SUMMARY_VIEW_APPLY_BUTTON_CLICKED: "property_group_summary_view_apply_button_clicked",
  PROPERTY_GROUP_SUMMARY_VIEW_CHECKBOX_BUTTON_CLICKED: "property_group_summary_view_checkbox_button_clicked",
  PROPERTY_GROUP_SUMMARY_VIEW_DELETE_BUTTON_CLICKED: "property_group_summary_view_delete_button_clicked",
  PROPERTY_GROUP_SUMMARY_VIEW_ATTRIBUTE_AND_TAG_CHECKBOX_BUTTON_CLICKED: "property_group_summary_view_attribute_and_tag_checkbox_button_clicked",
  PROPERTY_GROUP_SUMMARY_VIEW_PROPERTY_COLLECTION_CLICKED: "property_group_summary_view_property_collection_clicked",
  PROPERTY_GROUP_SUMMARY_VIEW_COLUMN_NAME_CHANGED: "property_group_summary_view_column_name_changed",
  PROPERTY_GROUP_SUMMARY_VIEW_SEARCH_VIEW_CHANGED: "property_group_summary_view_search_view_changed",
  PROPERTY_GROUP_SUMMARY_VIEW_PROPERTY_COLLECTION_TOGGLE_BUTTON_CLICKED: "property_group_summary_view_property_collection_toggle_button_clicked",
};

const oPropTypes = {
  elementData: ReactPropTypes.object
};

// @CS.SafeComponent
class PropertyGroupSummaryView extends React.Component {
  constructor (props) {
    super(props);
  }

  handleSearchViewChanged = (sValue) => {
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_SEARCH_VIEW_CHANGED, sValue);
  };

  handleColumnNameChanged = (sId, sSummaryType, oEvent) => {
    let sName = oEvent.target.value;
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_COLUMN_NAME_CHANGED, sId, sName, sSummaryType);
  };

  handleApplyClicked = (sContext, oSelectedItems, oReferencedData) => {
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_APPLY_BUTTON_CLICKED, sContext, oSelectedItems, oReferencedData);
  };

  handleTabCheckboxClicked = (bIsCheckboxClicked, sContext) => {
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_CHECKBOX_BUTTON_CLICKED, bIsCheckboxClicked, sContext);
  };

  handleDeleteIconClicked = (sSelectedElement, sContext) => {
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_DELETE_BUTTON_CLICKED, sSelectedElement, sContext);
  };

  handleAttributeAndTagCheckboxClicked = (bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag) => {
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_ATTRIBUTE_AND_TAG_CHECKBOX_BUTTON_CLICKED, bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag);
  };

  handlePropertyCollectionClicked = (sSelectedElement, sContext) => {
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_PROPERTY_COLLECTION_CLICKED, sSelectedElement, sContext);
  };

  handlePropertyCollectionToggleButtonClicked = (bIsPropertyCollectionToggleButtonClicked, sContext) => {
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_PROPERTY_COLLECTION_TOGGLE_BUTTON_CLICKED, bIsPropertyCollectionToggleButtonClicked, sContext);
  };

  handleLazyMSSView = (oElement, sLabel) => {
    let aSelectedItems = oElement.selectedPropertyCollectionForMapping || [];
    let oReferencedData = oElement.referencedData || {};
    //Create Lazy View for PropertyCollections
    let oReferencedPropertyGroups = !CS.isEmpty(oReferencedData.referencedPropertyCollections) ? oReferencedData.referencedPropertyCollections : {};
    let oPropertyGroupReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.propertyCollections : {};
    return this.getLazyMSSView(aSelectedItems, oReferencedPropertyGroups, oPropertyGroupReqResInfo, null, oElement.summaryType, false, false, false, sLabel);
  };

  getLazyMSSView = (aSelectedItems, oReferencedData, oReqResInfo, fOnApply, sContext, bCannotRemove, bIsDisabled, bIsMultiSelect, sLabel) => {
    let aSelectedItemsDummy = [];
    CS.forEach(aSelectedItems, function (oSelectedItem) {
      aSelectedItemsDummy.push(oSelectedItem.id);
    });
    aSelectedItems = aSelectedItemsDummy;

    let oAnchorOrigin = {horizontal: 'left', vertical: 'top'};
    let oTargetOrigin = {horizontal: 'left', vertical: 'bottom'};
    return (
        <LazyContextMenuView
            isMultiselect={bIsMultiSelect}
            onClickHandler={this.handleApplyClicked.bind(this, sContext)}
            anchorOrigin={oAnchorOrigin}
            targetOrigin={oTargetOrigin}
            menuListHeight={"250px"}
            requestResponseInfo={oReqResInfo}
            selectedItems={aSelectedItems}
            className={""}
            key={sContext}>
          <div className="headerWrapper">
            <div className="contextMenuViewNewContainer">
              <div className='addTemplateHandler'></div>
            </div>
            <div className="headerLabel">{sLabel}</div>
          </div>
        </LazyContextMenuView>
    );
  };

  getLabel = (sSelectedElement, sContext, oReferencedData) => {
    if (sSelectedElement.id) {
      return CS.getLabelOrCode(sSelectedElement);
    }
    if (!CS.isEmpty(sSelectedElement)) {
      let oRefObject = oReferencedData.referencedPropertyCollections[sSelectedElement];
      return CS.getLabelOrCode(oRefObject);
    }

  };

  getRowsForPropertyCollection = (sSelectedElement, sContext, oReferencedData, sSelectedPropertyCollectionId, sSearchViewText, mappingType) => {
    let sLabel = this.getLabel(sSelectedElement, sContext, oReferencedData) || " ";
    let sPropertyCollectionListCss = "listForPropertyCollection"
    if (sSelectedElement.id == sSelectedPropertyCollectionId) {
      sPropertyCollectionListCss = sPropertyCollectionListCss + " effectForSelectedPropertyCollection"
    }
    if (!CS.includes(sLabel.toLowerCase(), sSearchViewText.toLowerCase()) && !CS.includes("")) {
      return null;
    }
    let oDeleteButtonDOM = null ;
   /* if(mappingType === MappingTypeDictionary.OUTBOUND_MAPPING){
      oDeleteButtonDOM = <div className="deleteRowForPropertyGroup"
                              onClick={this.handleDeleteIconClicked.bind(this, sSelectedElement.id, sContext)}></div>
    }*/
    return (
        <div className={sPropertyCollectionListCss}>
          <span className="propertyGroupListLabel"
                onClick={this.handlePropertyCollectionClicked.bind(this, sSelectedElement.id, sContext)}>{sLabel}</span>
          {oDeleteButtonDOM}
        </div>)
  };

  getRowsForAttributeAndTag = (oSelectedElement, sRenderingType, sParentTagId) => {
    let _this = this;
    let sMappedValue = "";
    let oElementData = _this.props.elementData;
    let oReferencedData = oElementData.referencedData;
    let oReferencedTags  = oReferencedData.referencedTags;
    let oReferencedAttributes  = oReferencedData.referencedAttributes;
    let bIsCheckboxClicked = oSelectedElement.isIgnored;
    if(sRenderingType == "subTag"){
      let oReferencedTag  = oReferencedTags[sParentTagId];
      let aChildrens = oReferencedTag.children;
      if(CS.isNotEmpty(aChildrens)){
        let oFoundChildTag = CS.find(aChildrens,{id: oSelectedElement.mappedTagValueId});
        sMappedValue = CS.getLabelOrCode(oFoundChildTag);
      }
    } else if (sRenderingType == "tag") {
      sMappedValue = (CS.isEmpty(oReferencedTags[oSelectedElement.mappedElementId].label)) ? oReferencedTags[oSelectedElement.mappedElementId].code : oReferencedTags[oSelectedElement.mappedElementId].label;
    } else {
      sMappedValue = (CS.isEmpty(oReferencedAttributes[oSelectedElement.mappedElementId].label)) ? oReferencedAttributes[oSelectedElement.mappedElementId].code : oReferencedAttributes[oSelectedElement.mappedElementId].label;
    }

    let sUserValue = (sRenderingType == "subTag") ? oSelectedElement.tagValue : oSelectedElement.columnNames[0];
    let checkboxClassChecked = bIsCheckboxClicked ? "listGroupCheckbox" : "listGroupCheckbox checkedItem";
    let iconClass = (sRenderingType == "attribute") ? "tileIconAttribute" : "tileIconTag";
    let listClass = (sRenderingType == "subTag") ? "listForAttributesAndTags paddingForSubTag" : "listForAttributesAndTags";
    let sPropertyGroupLabelClass = "propertyGroupLabel";
    let sPropertyGroupTextBoxClass = "propertyGroupTextBox";
    if (oElementData.mappingType === MappingTypeDictionary.OUTBOUND_MAPPING) {
      listClass += " outboundMapping";
      sPropertyGroupLabelClass += " outboundMappingForLabel";
      sPropertyGroupTextBoxClass += " outboundMappingForTextBox";
    } else {
      listClass += " inboundMapping";
      sPropertyGroupLabelClass += " inboundMappingForLabel";
      sPropertyGroupTextBoxClass += " inboundMappingForTextBox";
    }
    let sId = oSelectedElement.id;
    let oCheckBoxDom = ((oElementData.isExportAllCheckboxClicked && oElementData.mappingType === MappingTypeDictionary.OUTBOUND_MAPPING) || sRenderingType === "subTag") ? null :
                       (<div className={checkboxClassChecked}
                             onClick={this.handleAttributeAndTagCheckboxClicked.bind(this, bIsCheckboxClicked, oSelectedElement.id, sRenderingType)}>
                       </div>);
    let oPropertyGroupLabelDom = (<div className={sPropertyGroupLabelClass}>
      <div className={"tileIcon " + iconClass}></div>
      <TooltipView placement="bottom" label={sMappedValue}>
        <span className="mappedValueCss">{sMappedValue}</span>
      </TooltipView>
    </div>);
    let oPropertyGroupTextBoxDom = (<div className={sPropertyGroupTextBoxClass}>
      <div>
        <input className="inputTextPadding" value={sUserValue} type="text" placeholder={sUserValue}
               onChange={_this.handleColumnNameChanged.bind(_this, sId, sRenderingType)}/>
      </div>
    </div>);
    let oPropertyGroupLabelAndTextBoxDom = (oElementData.mappingType === MappingTypeDictionary.OUTBOUND_MAPPING) ?
                                           <React.Fragment>
                                             {oPropertyGroupLabelDom}
                                             {oPropertyGroupTextBoxDom}
                                           </React.Fragment> : <React.Fragment>
                                             {oPropertyGroupTextBoxDom}
                                             {oPropertyGroupLabelDom}
                                           </React.Fragment>;
    return (
        <div className={listClass}>
          <div className="propertyGroupCheckBox">
            {oCheckBoxDom}
          </div>
          {oPropertyGroupLabelAndTextBoxDom}
        </div>)
  };

  _generateModelForAttributeAnsTags = (sContext) => {
    return ({
      code: sContext,
      icon: null,
      iconKey: null,
      id: sContext,
      iid: null,
      label: sContext.charAt(0).toUpperCase() + sContext.slice(1),
      type: null,
    })
  };

  handleSelectedPropertyCollectionList = (oElementData) => {
    let _this = this;
    let oReferencedData = oElementData.referencedData;
    let oSelectedPropertyCollectionForMapping = oElementData.selectedTabId === MockDataForMapSummaryHeader.contextTag ? oElementData.selectedContextForMapping : oElementData.selectedPropertyCollectionForMapping;
    let sSelectedPropertyCollectionId = oElementData.selectedTabId === MockDataForMapSummaryHeader.contextTag ? oElementData.selectedContextId : oElementData.selectedPropertyCollectionId;
    let aSlectedPropertyCollectionForMappingKeys = Object.keys(oSelectedPropertyCollectionForMapping);
    let aListDOM = [];
    let bToggleFlag = oElementData.selectedTabId === MockDataForMapSummaryHeader.contextTag ? true : oElementData.bIsPropertyCollectionToggleFlag;
    if(!bToggleFlag){
      let oSelectedPropertyCollectionForMapping_AT = {};
      oSelectedPropertyCollectionForMapping_AT["attributes"] = this._generateModelForAttributeAnsTags("attributes");
      oSelectedPropertyCollectionForMapping_AT["tags"] = this._generateModelForAttributeAnsTags("tags");
      let aSlectedPropertyCollectionForMappingKeys_AT = ["attributes","tags"];
      CS.forEach(aSlectedPropertyCollectionForMappingKeys_AT, function (mKey) {
        let sSelectedElement = oSelectedPropertyCollectionForMapping_AT[mKey];
        let oRows = _this.getRowsForPropertyCollection(sSelectedElement, oElementData.summaryType, oReferencedData, sSelectedPropertyCollectionId, oElementData.searchViewText, oElementData.mappingType) || null;
        aListDOM.push(oRows);
      });
    }else {
      CS.forEach(aSlectedPropertyCollectionForMappingKeys, function (mKey) {
        let sSelectedElement = oSelectedPropertyCollectionForMapping[mKey];
        let oRows = _this.getRowsForPropertyCollection(sSelectedElement, oElementData.summaryType, oReferencedData, sSelectedPropertyCollectionId, oElementData.searchViewText, oElementData.mappingType) || null;
        aListDOM.push(oRows);
      });
    }
    return aListDOM;
  };

  handleSelectedAttributeAndTagList = (oElementData) => {
    let _this = this;
    let oReferencedData = oElementData.referencedData;
    let aSelectedPropertyCollectionForMapping = oElementData.selectedTabId === MockDataForMapSummaryHeader.contextTag ? oElementData.selectedContextForMapping : oElementData.selectedPropertyCollectionForMapping;
    let aSelectedAttributesForMapping = oElementData.configRuleMappingsForAttributes;
    let aSelectedTagForMapping = oElementData.configRuleMappingsForTags;
    let aListDOM = [];
    let aListAttribute = [];
    let aListTag = [];

    if (aSelectedPropertyCollectionForMapping.length === 0) {
      return null;
    }

    let sColumnNameLabel = getTranslation().EXCEL_COLUMN_NAME;
    let sMappedElementLabel = getTranslation().MAPPED_ELEMENT;
    let sHeadingForAttributeAndTagClass = "headingForAttributeAndTag";
    let sMappedElementLabelClass = "mappedElementLabelCss";
    let sColumnNameLabelClass = "columnNameLabelCss";
    let oColumnHeading;
    if (oElementData.mappingType === MappingTypeDictionary.OUTBOUND_MAPPING) {
      sHeadingForAttributeAndTagClass += " outboundMappingHeading";
      sMappedElementLabelClass += " outboundMappedElement";
      sColumnNameLabelClass += " outboundColumnName";
      oColumnHeading = (<div className={sHeadingForAttributeAndTagClass}>
        <span className={sMappedElementLabelClass}>{sMappedElementLabel}</span>
        <span className={sColumnNameLabelClass}>{sColumnNameLabel}</span>
      </div>);
    } else {
      sHeadingForAttributeAndTagClass += " inboundMappingHeading";
      sMappedElementLabelClass += " inboundMappedElement";
      sColumnNameLabelClass += " inboundColumnName";
      oColumnHeading = (<div className={sHeadingForAttributeAndTagClass}>
        <span className={sColumnNameLabelClass}>{sColumnNameLabel}</span>
        <span className={sMappedElementLabelClass}>{sMappedElementLabel}</span>
      </div>);
    }

    aListDOM.push(oColumnHeading);

    CS.forEach(aSelectedAttributesForMapping, function (oSelectedElement) {
      let oRows = _this.getRowsForAttributeAndTag(oSelectedElement, "attribute");
      aListAttribute.push(oRows);
    });

    CS.forEach(aSelectedTagForMapping, function (oSelectedElement) {
      let oRows = _this.getRowsForAttributeAndTag(oSelectedElement, "tag");
      aListTag.push(oRows);
      if (CS.isNotEmpty(oSelectedElement.tagValueMappings)) {
        CS.forEach(oSelectedElement.tagValueMappings[0].mappings, function (oSelectedElementForSubTag) {
          let oRowsForSubTag = _this.getRowsForAttributeAndTag(oSelectedElementForSubTag, "subTag", oSelectedElement.mappedElementId);
          aListTag.push(oRowsForSubTag);
        });
      }
    });

    if (CS.isEmpty(aListTag) && CS.isEmpty(aListAttribute)) {
      return null;
    }

    aListDOM.push(aListAttribute);
    aListDOM.push(aListTag);

    return aListDOM;
  };

  handleSelectedAttributeAndTagListInPropCollection = (oElementData) => {
    let _this = this;
    let oReferencedData = oElementData.referencedData;
    let aSelectedPropertyCollectionForMapping = oElementData.selectedPropertyCollectionForMapping || [];
    let aSelectedAttributesForMapping = oElementData.configRuleMappingsForAttributes;
    let aSelectedTagForMapping = oElementData.configRuleMappingsForTags;
    let aListDOM = [];
    let aListAttribute = [];
    let aListTag = [];

    if (aSelectedPropertyCollectionForMapping.length === 0) {
      return null;
    }

    let sColumnNameLabel = getTranslation().EXCEL_COLUMN_NAME;
    let sMappedElementLabel = getTranslation().MAPPED_ELEMENT;
    let sHeadingForAttributeAndTagClass = "headingForAttributeAndTag";
    let sMappedElementLabelClass = "mappedElementLabelCss";
    let sColumnNameLabelClass = "columnNameLabelCss";
    let oColumnHeading;
    if (oElementData.mappingType === MappingTypeDictionary.OUTBOUND_MAPPING) {
      sHeadingForAttributeAndTagClass += " outboundMappingHeading";
      sMappedElementLabelClass += " outboundMappedElement";
      sColumnNameLabelClass += " outboundColumnName";
      oColumnHeading = (<div className={sHeadingForAttributeAndTagClass}>
        <span className={sMappedElementLabelClass}>{sMappedElementLabel}</span>
        <span className={sColumnNameLabelClass}>{sColumnNameLabel}</span>
      </div>);
    } else {
      sHeadingForAttributeAndTagClass += " inboundMappingHeading";
      sMappedElementLabelClass += " inboundMappedElement";
      sColumnNameLabelClass += " inboundColumnName";
      oColumnHeading = (<div className={sHeadingForAttributeAndTagClass}>
        <span className={sColumnNameLabelClass}>{sColumnNameLabel}</span>
        <span className={sMappedElementLabelClass}>{sMappedElementLabel}</span>
      </div>);
    }

    aListDOM.push(oColumnHeading);

    CS.forEach(aSelectedAttributesForMapping, function (oSelectedElement) {
      let oRows = _this.getRowsForAttributeAndTag(oSelectedElement, "attribute");
      aListAttribute.push(oRows);
    });

    CS.forEach(aSelectedTagForMapping, function (oSelectedElement) {
      let oRows = _this.getRowsForAttributeAndTag(oSelectedElement, "tag");
      aListTag.push(oRows);
      if (CS.isNotEmpty(oSelectedElement.tagValueMappings)) {
        CS.forEach(oSelectedElement.tagValueMappings[0].mappings, function (oSelectedElementForSubTag) {
          let oRowsForSubTag = _this.getRowsForAttributeAndTag(oSelectedElementForSubTag, "subTag", oSelectedElement.mappedElementId);
          aListTag.push(oRowsForSubTag);
        });
      }
    });

    if (CS.isEmpty(aListTag) && CS.isEmpty(aListAttribute)) {
      return null;
    }

    if(oElementData.selectedPropertyCollectionId === "attributes"){
      if(CS.isEmpty(aListAttribute)){
        aListDOM.push(<NothingFoundView message={getTranslation().NOTHING_FOUND}/>);
      }else{
        aListDOM.push(aListAttribute);
      }
    }else if(oElementData.selectedPropertyCollectionId === "tags"){
      if(CS.isEmpty(aListTag)){
        aListDOM.push(<NothingFoundView message={getTranslation().NOTHING_FOUND}/>);
      }else{
        aListDOM.push(aListTag);
      }
    }else{
      aListDOM.push(aListAttribute);
      aListDOM.push(aListTag);
    }
    return aListDOM;
  };

  getPropertyGroupMappingSection = (oElementData) => {
    let oAddEntityView = null ;
    // if(oElementData.mappingType !== MappingTypeDictionary.INBOUND_MAPPING){
    //   oAddEntityView = this.handleLazyMSSView(oElementData, getTranslation().ADD_PROPERTY_GROUP);
    // }
    let oSelectedPropertyCollectionListView = this.handleSelectedPropertyCollectionList(oElementData);
    let bToggleFlag = oElementData.selectedTabId === MockDataForMapSummaryHeader.contextTag ? true : oElementData.bIsPropertyCollectionToggleFlag;
    if (!bToggleFlag) {
      let oSelectedAttributeAndTagList = this.handleSelectedAttributeAndTagListInPropCollection(oElementData);
      return (
          <div className="containerListView row">
            <div className="column leftSideForPropertyGroupView">
              <div className="listGroupForPropertyCollection">
                {oSelectedPropertyCollectionListView}
              </div>
            </div>

            <div className="column rightSideForPropertyGroupView">
              <div className="listGroupForAttributesAndTags">
                {oSelectedAttributeAndTagList}
              </div>
            </div>
          </div>
      );
    }
    else {
      let oSelectedAttributeAndTagList = this.handleSelectedAttributeAndTagList(oElementData);
      return (
          <div className="containerListView row">
            <div className="column leftSideForPropertyGroupView">
              {oAddEntityView}
              <div className="searchBarContainer">
                <SimpleSearchBarView
                    onBlur={this.handleSearchTextChanged}
                    searchText={oElementData.searchViewText}
                    placeHolder={this.props.gridViewSearchBarPlaceHolder}
                    isDisabled={this.props.disableSearch}
                    onChange={this.handleSearchViewChanged.bind(this)}
                />
              </div>
              <div className="listGroupForPropertyCollection">
                {oSelectedPropertyCollectionListView}
              </div>
            </div>

            <div className="column rightSideForPropertyGroupView">
              <div className="listGroupForAttributesAndTags">
                {oSelectedAttributeAndTagList}
              </div>
            </div>
          </div>
      );
    }
  };

  getCheckboxAndLabelForOutboundMapping = () => {
    let oElementData = this.props.elementData;
    let sMappingType = oElementData.mappingType;
    let sLabel = getTranslation().PROPERTY_COLLECTION;
    let fHandlerToggle = this.handlePropertyCollectionToggleButtonClicked;
    let oLabelDOM = null;
    let oCheckboxDOM = null;
    let sLabelCheckbox = "";
    if (sMappingType == MappingTypeDictionary.OUTBOUND_MAPPING ) {
      if(oElementData.summaryType === "propertyCollections"){
        sLabelCheckbox = getTranslation().EXPORT_ALL_PROPERTIES;
      }else {
        sLabelCheckbox = getTranslation().EXPORT_ALL_CONTEXTS;
      }
      let bIsExportAllCheckboxClicked = oElementData.isExportAllCheckboxClicked;
      let checkboxClassChecked = "listGroupCheckbox checkedItem";
      if (!bIsExportAllCheckboxClicked) {
        checkboxClassChecked = "listGroupCheckbox";
      }
      oCheckboxDOM = <div className={checkboxClassChecked}
                          onClick={this.handleTabCheckboxClicked.bind(this, bIsExportAllCheckboxClicked, oElementData.summaryType)}>
      </div>;
      oLabelDOM = <div className={"propertyCheckboxLabel"}>{sLabelCheckbox}</div>;
    }
    let oToggleButton = null;
    if(oElementData.summaryType === "propertyCollections"){
      oToggleButton = <div className="propCollectionToggle">
        <GridYesNoPropertyView
            isDisabled={false}
            value={oElementData.bIsPropertyCollectionToggleFlag}
            onChange={fHandlerToggle.bind(this, "propertyCollectionMappingToggle")}
        />
        <div className={"propCollectionToggleLabel"}>{sLabel}</div>
      </div>
    }
    return (
        <div  className="outboundPropertyTabWrapper">
          {oCheckboxDOM}
          {oLabelDOM}
          {oToggleButton}
        </div>)
  };

  render () {
    let oElementData = this.props.elementData;
    let oPropertyGroupMappingDOM = this.getPropertyGroupMappingSection(oElementData);
    let oCheckBoxForOutbound = this.getCheckboxAndLabelForOutboundMapping();
    return (
        <div className="checkboxWrapperForOutboundMapping checkboxWrapperSupportForPropertyGroup">
          {oCheckBoxForOutbound}
          {oPropertyGroupMappingDOM}
        </div>)
  }
}

PropertyGroupSummaryView.propTypes = oPropTypes;

export const view = PropertyGroupSummaryView;
export const events = oEvents;


