import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as LazyMSSView } from '../lazy-mss-view/lazy-mss-view';
import TooltipView from '../tooltipview/tooltip-view';
import MappingTypeDictionary from "../../commonmodule/tack/mapping-type-dictionary";
import {view as NothingFoundView} from "../nothingfoundview/nothing-found-view";

const oEvents = {
  MAPPING_SUMMARY_VIEW_ADD_NEW_MAPPING_ROW: "mapping_summary_view_add_new_mapping_row",
  MAPPING_SUMMARY_VIEW_COLUMN_NAME_CHANGED: "mapping_summary_view_column_name_changed",
  MAPPING_SUMMARY_VIEW_MAPPED_ELEMENT_CHANGED: "mapping_summary_view_mapped_element_changed",
  MAPPING_SUMMARY_VIEW_MAPPING_ROW_SELECTED: "mapping_summary_view_mapping_row_selected",
  MAPPING_SUMMARY_VIEW_IS_IGNORED_TOGGLED: "mapping_summary_view_is_ignored_toggled",
  MAPPING_SUMMARY_VIEW_MAPPING_ROW_DELETED: "mapping_summary_view_mapping_row_deleted",
  MAPPING_SUMMARY_VIEW_ADD_TAG_VALUE_CLICKED: "mapping_summary_view_add_tag_value_clicked",
  MAPPING_SUMMARY_VIEW_TAG_VALUE_CHANGED: "mapping_summary_view_tag_value_changed",
  MAPPING_SUMMARY_VIEW_TAG_VALUE_IGNORE_CASE_TOGGLED: "mapping_summary_view_tag_value_ignore_case_toggled",
  MAPPING_SUMMARY_VIEW_MAPPED_TAG_VALUE_CHANGED: "mapping_summary_view_mapped_tag_value_changed",
  MAPPING_SUMMARY_VIEW_MAPPED_TAG_VALUE_ROW_DELETE_CLICKED: "mapping_summary_view_mapped_tag_value_row_delete_clicked",
  PROPERTY_GROUP_SUMMARY_VIEW_CHECKBOX_BUTTON_CLICKED: "property_group_summary_view_checkbox_button_clicked"
};

const oPropTypes = {
  configRuleMappings: ReactPropTypes.array,
  selectedMappingRows: ReactPropTypes.array,
  summaryType: ReactPropTypes.string,
  context: ReactPropTypes.string,
  referencedData: ReactPropTypes.object,
  lazyMSSReqResInfo: ReactPropTypes.object,
  mappingType: ReactPropTypes.string,
  isExportAllCheckboxClicked: ReactPropTypes.boolean
};
/**
 * @class MappingSummaryView
 * @memberOf Views
 * @property {array} [configRuleMappings] - Contains array of mappings data.
 * @example: [{columnNames:Array[1], id:"8dae70bf-10ab-43a2-b49c-dc36cb51a916", isAutomapped:false, isIgnored:true, mappedElementId: null}].
 * @property {array} [selectedMappingRows] - Deprecated
 * @property {string} [summaryType] - Contains summary type(Attribute/Tag/Taxonomy/Class).
 * @property {string} [context] - Used to differentiate which operation will be performed(ex. mapping).
 * @property {object} [referencedData] - Contains Referenced data of that summary type(ex. referenced classes or referenced tags etc).
 * @property {object} [lazyMSSReqResInfo] - Contains the referenced data for lazy MSS view.
 */


// @CS.SafeComponent
class MappingSummaryView extends React.Component {
  constructor(props) {
    super(props);
  }

  handleAddMappingRow =()=> {
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_ADD_NEW_MAPPING_ROW, this.props.context, this.props.summaryType);
  }

/*
  handleMappingRowSelected =(sId)=> {
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_MAPPING_ROW_SELECTED, sId, this.props.context);
  }
*/

  handleColumnNameChanged =(sId, oEvent)=> {
    var sName = oEvent.target.value;
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_COLUMN_NAME_CHANGED, sId, sName, this.props.context, this.props.summaryType);
  }

  handleMappedElementChanged =(sId, aMappedElementIds, oReferencedData)=> {
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_MAPPED_ELEMENT_CHANGED, sId, aMappedElementIds, this.props.context, this.props.summaryType, oReferencedData);
  }

  handleIsIgnoredToggled =(sId)=> {
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_IS_IGNORED_TOGGLED, sId, this.props.context, this.props.summaryType);
  }

  handleMappingRowDeleted =(sId)=> {
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_MAPPING_ROW_DELETED, sId, this.props.context, this.props.summaryType);
  }

  handleAddTagValueClicked =(sId, sMappingElementId, oEvent)=> {
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_ADD_TAG_VALUE_CLICKED, sId, sMappingElementId, this.props.context);
  }

  handleTagValueInputChanged =(sId, sMappedTagValueId, oEvent)=> {
    var sNewValue = oEvent.target.value;
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_TAG_VALUE_CHANGED, sId, sMappedTagValueId, sNewValue, this.props.context);
  }

  handleMappedTagValueChanged =(sId, sMappedTagValueId, sMappedElementId, oReferencedData)=> {
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_MAPPED_TAG_VALUE_CHANGED, sId, sMappedTagValueId, sMappedElementId, this.props.context, oReferencedData);
  }

  handleTagValueMappingRowDeleted =(sId, sMappedTagValueId, oEvent)=> {
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_MAPPED_TAG_VALUE_ROW_DELETE_CLICKED, sId, sMappedTagValueId, this.props.context);
  }

  handleTagValueIgnoreCaseToggled =(sId, sMappedTagValueId, bIsIgnored, oEvent)=> {
    if(bIsIgnored){
      return;
    }
    EventBus.dispatch(oEvents.MAPPING_SUMMARY_VIEW_TAG_VALUE_IGNORE_CASE_TOGGLED, sId, sMappedTagValueId, this.props.context);
  }

  handleTabCheckboxClicked = (bIsCheckboxClicked, sContext) => {
    EventBus.dispatch(oEvents.PROPERTY_GROUP_SUMMARY_VIEW_CHECKBOX_BUTTON_CLICKED, bIsCheckboxClicked, sContext);
  }

  getTagValueRow =(sId)=> {
    let aConfigRuleMapping = this.props.configRuleMappings;
    let oConfigRuleMapping = CS.find(aConfigRuleMapping, {id: sId});

    if (oConfigRuleMapping) {
      let aColumnsName = oConfigRuleMapping.columnNames;
      let aTagValueMapping = oConfigRuleMapping.tagValueMappings;

      if (!CS.isEmpty(aTagValueMapping)) {
        let sTagId = oConfigRuleMapping.mappedElementId;
        let aTagValueMappedRows = [];
        let oReferencedData = this.props.referencedData || {};
        let oReferencedTags = !CS.isEmpty(oReferencedData.referencedTags) ? oReferencedData.referencedTags : {};
        let oTagValuesReqResInfo = !CS.isEmpty(this.props.lazyMSSReqResInfo) ? this.props.lazyMSSReqResInfo.tagValues : {};
        let oReferencedTag = oReferencedTags[sTagId] || {};
        let oReferencedTagValueData = {};
        CS.forEach(oReferencedTag.children, function(oTagChildren){
          oReferencedTagValueData[oTagChildren.id] = oTagChildren;
        });
        let oClonedTagValuesReqResInfo = CS.cloneDeep(oTagValuesReqResInfo);
        oClonedTagValuesReqResInfo.customRequestModel = {
          tagGroupId: sTagId
        };

        let _this = this;

        CS.forEach(aColumnsName, function (sColumnName) {
          let oTagValueMapping = CS.find(aTagValueMapping, {columnName: sColumnName});

          let aMappedTagValueList = (CS.isNotEmpty(oTagValueMapping)) ? oTagValueMapping.mappings : [];
          if (!CS.isEmpty(aMappedTagValueList)) {

            CS.forEach(aMappedTagValueList, function (oMappedTagValue) {
              //var sIsIgnoredClass = oMappedTagValue.isIgnored ? "isIgnored selected" : "isIgnored ";
              let aSelectedItems = [];
              if(!CS.isEmpty(oMappedTagValue.mappedTagValueId)) {
                aSelectedItems.push(oMappedTagValue.mappedTagValueId);
              }

              aTagValueMappedRows.push(
                  <div className="tagValueRowWrapper">
                    {/*<input className="tagValueMappingCheckbox" type="checkbox" selected={false}/>*/}
                    <div className="tagValuesWrapper">
                      <input value={oMappedTagValue.tagValue} className="tagValueInputField"
                             onChange={_this.handleTagValueInputChanged.bind(_this, sId, oMappedTagValue.id)}/>
                    </div>
                    <div className="mappingElement">
                      <div className="multiSelect">
                        {_this.getLazyMSSView(aSelectedItems, oReferencedTagValueData, oClonedTagValuesReqResInfo,
                            _this.handleMappedTagValueChanged.bind(_this, sId, oMappedTagValue.id), sId, true, oConfigRuleMapping.isIgnored)}
                      </div>
                    </div>
                    <div className="ignoreCaseWrapper"
                         onClick={_this.handleTagValueIgnoreCaseToggled.bind(_this, sId, oMappedTagValue.id, oConfigRuleMapping.isIgnored)}>
                      <input className="checkbox" type="checkbox" checked={oMappedTagValue.isIgnoreCase} disabled={oConfigRuleMapping.isIgnored}/>
                      <div className="checkboxLabel">{getTranslation().IGNORE_CASE}</div>
                    </div>

                    <div className="deleteButton"
                         onClick={_this.handleTagValueMappingRowDeleted.bind(_this, sId, oMappedTagValue.id)}></div>
                  </div>);
            });
          }
        });
        return aTagValueMappedRows;
      }
    }
    return null;
  }

  getLazyMSSView = (aSelectedItems, oReferencedData, oReqResInfo, fOnApply, sContext, bCannotRemove, bIsDisabled, aExcludedItems) => {

    return (
        <LazyMSSView
            selectedItems={aSelectedItems}
            context={sContext}
            referencedData={oReferencedData}
            requestResponseInfo={oReqResInfo}
            onApply={fOnApply}
            cannotRemove={bCannotRemove}
            disabled={bIsDisabled}
            excludedItems={aExcludedItems}
        />
    );
  };

  getDropDownView = (aSelectedItems, bIsDisabled, sId, aExcludedItems = []) => {
    let oProps = this.props;
    let oReferencedData = oProps.referencedData || {};

    switch (oProps.summaryType) {
      case "attributes":
        let oReferencedAttributes = !CS.isEmpty(oReferencedData.referencedAttributes) ? oReferencedData.referencedAttributes : {};
        let oAttributeReqResInfo = !CS.isEmpty(oProps.lazyMSSReqResInfo) ? oProps.lazyMSSReqResInfo.attributes : {};
        return this.getLazyMSSView(aSelectedItems, oReferencedAttributes, oAttributeReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled);

      case "tags":
        let oReferencedTags = !CS.isEmpty(oReferencedData.referencedTags) ? oReferencedData.referencedTags : {};
        let oTagReqResInfo = !CS.isEmpty(oProps.lazyMSSReqResInfo) ? oProps.lazyMSSReqResInfo.tags : {};
        return this.getLazyMSSView(aSelectedItems, oReferencedTags, oTagReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled);

      case "taxonomies":
        let oReferencedTaxonomies = !CS.isEmpty(oReferencedData.referencedTaxonomies) ? oReferencedData.referencedTaxonomies : {};
        let oTaxonomyReqResInfo = !CS.isEmpty(oProps.lazyMSSReqResInfo) ? oProps.lazyMSSReqResInfo.taxonomies : {};
        return this.getLazyMSSView(aSelectedItems, oReferencedTaxonomies, oTaxonomyReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled, aExcludedItems);

      case "classes":
        let oReferencedKlasses = !CS.isEmpty(oReferencedData.referencedKlasses) ? oReferencedData.referencedKlasses : {};
        let oClassesReqResInfo = !CS.isEmpty(oProps.lazyMSSReqResInfo) ? oProps.lazyMSSReqResInfo.classes : {};
        return this.getLazyMSSView(aSelectedItems, oReferencedKlasses, oClassesReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled, aExcludedItems);

      case "relationship":
        let oReferencedRelationships = !CS.isEmpty(oReferencedData.referencedRelationships) ? oReferencedData.referencedRelationships : {};
        let oRelationshipReqResInfo = !CS.isEmpty(oProps.lazyMSSReqResInfo) ? oProps.lazyMSSReqResInfo.relationships : {};
        return this.getLazyMSSView(aSelectedItems, oReferencedRelationships, oRelationshipReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, true, aExcludedItems);
    }

    return null;
  }

  getMappingTable = (aRows, sSummaryType) => {
   if(aRows.length==0){
      if(sSummaryType === "relationship"){
        return (<NothingFoundView message={getTranslation().NOTHING_FOUND}/>);
      }else {
        return null;
      }
    }
    let sRowLabel = "addedRows";
    let sLeftColumnForMapping = getTranslation().COLUMN_NAME_TO_BE_MAPPED;
    let sRightColumnForMapping = getTranslation().MAPPED_ELEMENT;
    let sLeftColumnCss = "leftColumnCssForInbound";
    let sRightColumnCss = "rightColumnCssForInbound";
    let sHeaderForMapping = "headerForInboundMapping";
    if(this.props.mappingType == MappingTypeDictionary.OUTBOUND_MAPPING ){
      sRowLabel += " outBound";
      sLeftColumnForMapping = getTranslation().ELEMENT_TO_BE_MAPPED;
      sRightColumnForMapping = getTranslation().MAPPED_EXCEL_COLUMN_NAME;
      sLeftColumnCss = "leftColumnCssForOutbound";
      sRightColumnCss = "rightColumnCssForOutbound";
      sHeaderForMapping = "headerForOutboundMapping";
    }
    let aHeader = (
        <div className={sHeaderForMapping}>
          <span className={sLeftColumnCss}>{sLeftColumnForMapping}</span>
          <span className={sRightColumnCss}>{sRightColumnForMapping}</span>
        </div>
    );
    return <div className={sRowLabel}>
      {aHeader}
      {aRows}
    </div>;
  };

  getCheckboxAndLabelForOutboundMapping = (sMappingType) => {
    if (sMappingType == MappingTypeDictionary.INBOUND_MAPPING) {
      return null;
    }
    let sSummaryType = this.props.summaryType;
    let bIsExportAllCheckboxClicked = this.props.isExportAllCheckboxClicked;
    let checkboxClassChecked = "listGroupCheckbox checkedItem";
    if(!bIsExportAllCheckboxClicked){
      checkboxClassChecked = "listGroupCheckbox";
    }
    let label = getTranslation().EXPORT_ALL_CLASSES;
    if (sSummaryType == "taxonomies") {
      label = getTranslation().EXPORT_ALL_TAXONOMIES;
    } if (sSummaryType == "relationship") {
      label = getTranslation().EXPORT_ALL_RELATIONSHIPS;
    }
    return (
        <div className="bodySection">
          <div className="withBorder">
            <div className="checkboxWrapperForOutboundMapping">
              <div className={checkboxClassChecked}
                   onClick={this.handleTabCheckboxClicked.bind(this, bIsExportAllCheckboxClicked, sSummaryType)}>
              </div>
              <div className="propertyCheckboxLabel">{label}</div>
            </div>
          </div>
        </div>
    )
  };


  render() {
    var _this = this;
    var aRows = [];
    var aConfigRuleMappings = this.props.configRuleMappings;
    let sMappingType = this.props.mappingType;
    let sSummaryType = this.props.summaryType;
    let oCheckBoxForOutbound = this.getCheckboxAndLabelForOutboundMapping(sMappingType);
    let aExcludedItemsForOutBoundMapping = [];
    CS.forEach(aConfigRuleMappings, function (oRow) {
      if (oRow.mappedElementId) {
        aExcludedItemsForOutBoundMapping.push(oRow.mappedElementId);
      }
    });
    CS.forEach(aConfigRuleMappings, function (oRow) {
      var sId = oRow.id;
      var sIsIgnoredClass = oRow.isIgnored ? "isIgnored selected" : "isIgnored ";
      var sName = CS.join(oRow.columnNames, "");
      var sNameInputClass = sName ? "" : "inputError ";
      var aSelectedItems = oRow.mappedElementId? [oRow.mappedElementId]: [];
      if(sMappingType == MappingTypeDictionary.INBOUND_MAPPING){
        let oDeleteDOM = null;
        if(sSummaryType !== "relationship"){
          oDeleteDOM = <div className="deleteButton" onClick={_this.handleMappingRowDeleted.bind(_this, sId)}></div>
        }
        aRows.push(
            <div className="mappingRow" key={sId}>
              {/*<input className="mappingCheckbox" type="checkbox" selected={bSelected}
                   onClick={_this.handleMappingRowSelected.bind(_this, sId)}/>*/}
              <div className="fieldName">
                <input value={sName} className={sNameInputClass}
                       onChange={_this.handleColumnNameChanged.bind(_this, sId)}/>
              </div>
              <div className="mappingElement">
                {/* <div className="mappedElement" title={getTranslation().MAPPED_ELEMENT}>{getTranslation().MAPPED_ELEMENT}</div>*/}
                <div className="multiSelect">
                  {_this.getDropDownView(aSelectedItems, oRow.isIgnored, sId)}
                </div>
              </div>
              {((_this.props.summaryType === "tags") && !oRow.isIgnored && CS.isNotEmpty(aSelectedItems)) ?
               <div className="addTagValue" title={getTranslation().ADD_TAG_VALUES}
                    onClick={_this.handleAddTagValueClicked.bind(_this, sId, oRow.mappedElementId)}>
               </div> : null}
              <TooltipView label={getTranslation().IGNORE}>
                <div className={sIsIgnoredClass} onClick={_this.handleIsIgnoredToggled.bind(_this, sId)}></div>
              </TooltipView>
              {oDeleteDOM}
              {(_this.props.summaryType === "tags") ? <div className="mappingTagValuesRow">
                {_this.getTagValueRow(sId)}
              </div> : null}
            </div>);
      }
      else{
        let oDeleteDOM = null;
        if(sSummaryType === "classes" || sSummaryType === "taxonomies"){
          oDeleteDOM = <div className="deleteButton" onClick={_this.handleMappingRowDeleted.bind(_this, sId)}></div>
        }
        let oIgnoreDom = (!_this.props.isExportAllCheckboxClicked) ? (<TooltipView label={getTranslation().IGNORE}>
          <div className={sIsIgnoredClass} onClick={_this.handleIsIgnoredToggled.bind(_this, sId)}></div>
        </TooltipView>) : <div></div>;
        aRows.push(
            <div className="mappingRow" key={sId}>
              <div className="mappingElement">
                {/* <div className="mappedElement" title={getTranslation().MAPPED_ELEMENT}>{getTranslation().MAPPED_ELEMENT}</div>*/}
                <div className="multiSelect">
                  {_this.getDropDownView(aSelectedItems, false, sId, aExcludedItemsForOutBoundMapping)}
                </div>
              </div>
              {((_this.props.summaryType === "tags") && !oRow.isIgnored) ? <div className="addTagValue" title={getTranslation().ADD_TAG_VALUES}
                                                                                onClick={_this.handleAddTagValueClicked.bind(_this, sId, oRow.mappedElementId)}>
              </div> : null}
              {oIgnoreDom}
              {/*<div className="deleteButton" onClick={_this.handleMappingRowDeleted.bind(_this, sId)}></div>*/}
              {oDeleteDOM}
              {(_this.props.summaryType === "tags") ? <div className="mappingTagValuesRow">
                {_this.getTagValueRow(sId)}
              </div> : null}

              <div className="fieldName">
                <input value={sName} className={sNameInputClass}
                       onChange={_this.handleColumnNameChanged.bind(_this, sId)}/>
              </div>
            </div>);
      }

    });

    let oAddMappingsDOM = null;
    if (sSummaryType !== "relationship") {
      oAddMappingsDOM = <div className="addMappingRow" onClick={this.handleAddMappingRow}>
        <div className="addMappingIcon"></div>
        <div className="addMappingText">{getTranslation().ADD_MAPPING}</div>
      </div>
    }
    return (
        <div>
          {oCheckBoxForOutbound}
          <div className="mappingSummaryView">
            <div className="mappingRows">
              {oAddMappingsDOM}
              {this.getMappingTable(aRows,sSummaryType)}
            </div>
          </div>
        </div>
          );
  }
}

MappingSummaryView.propTypes = oPropTypes;

export const view = MappingSummaryView;
export const events = oEvents;
