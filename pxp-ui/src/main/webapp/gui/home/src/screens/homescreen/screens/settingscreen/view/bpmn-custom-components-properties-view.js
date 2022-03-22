import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import ViewUtils from './utils/view-utils';
import UniqueIdentifierGenerator from '../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator.js';
import { view as CustomTextFieldView } from '../../../../../viewlibraries/customtextfieldview/custom-text-field-view';
import { view as MulitSelectView } from '../../../../../viewlibraries/multiselectview/multi-select-search-view';
import { view as LazyMSSView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as SmallTaxonomyView } from './../../../../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import SmallTaxonomyViewModel from './../../../../../viewlibraries/small-taxonomy-view/model/small-taxonomy-view-model';
import MockDataForProcessInputElements from '../tack/mock-data-for-process-input-parameters';
import MockDataForProcessSourceTypeModules from '../tack/mock/mock-data-for-process-source-type-modules';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager';
import AttributeUtils from '../../../../../commonmodule/util/attribute-utils';
import { view as CustomDatePicker } from '../../../../../viewlibraries/customdatepickerview/customdatepickerview.js';
import { view as CustomMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import { view as AppliedFilterView } from '../../../../../viewlibraries/filterview/fltr-applied-filter-view';
import MockDataForProcessFilterModules from '../tack/mock/mock-data-for-process-filter-modules';
import {view as GroupMssWrapperView} from "../../../../../viewlibraries/filter/group-mss-wrapper-view";
import MockDataForTaskRolesMSS from "../../../../../smartviewlibraries/taskview/tack/mock/mock-data-for-task-roles-mss";
import {view as GridYesNoPropertyView} from "../../../../../viewlibraries/gridview/grid-yes-no-property-view";

const oPropTypes = {
  store: ReactPropTypes.object,
  element: ReactPropTypes.object,
  referencedData: ReactPropTypes.object,
};

const oEvents = {
  HANDLE_BPMN_ELEMENTS_TEXT_CHANGED: "handle_bpmn_elements_text_changed",
  HANDLE_BPMN_ELEMENTS_MSS_CHANGED: "handle_bpmn_elements_mss_changed",
  COMPONENT_TAXONOMY_DATA_SOURCE_CLASS_VALUE_CHANGED: "component_taxonomy_data_source_class_value_changed",
  COMPONENT_ADD_ROW_BUTTON_CLICKED: "component_add_row_button_clicked",
  COMPONENT_REMOVE_ROW_BUTTON_CLICKED: "component_remove_row_button_clicked",
  BPMN_PROPERTIES_TAG_MSS_CHANGED: "bpmn_properties_tag_mss_changed",
  COMPONENT_ADD_TAG_GROUP_IN_CLASS_VALUE: "component_add_tag_group_in_class_value",
  COMPONENT_REMOVE_TAG_GROUP_IN_CLASS_VALUE: "component_remove_tag_group_in_class_value",
  BPMN_PROPERTIES_TAG_MSS_CHANGED_CUSTOM: "bpmn_properties_tag_mss_changed_custom",
  COMPONENT_ADD_ATTRIBUTE_GROUP_IN_CLASS_VALUE: "component_add_attribute_group_in_class_value",
  COMPONENT_REMOVE_ATTRIBUTE_GROUP_IN_CLASS_VALUE: "component_remove_attribute_group_in_class_value",
  BPMN_PROPERTIES_ATTRIBUTE_MSS_CHANGED_CUSTOM: "bpmn_properties_attribute_mss_changed_custom",
  HANDLE_BPMN_ELEMENTS_DATE_CHANGED : "handle_bpmn_elements_date_changed",
  COMPONENT_SEARCH_FILTER_EDIT_BUTTON_CLICKED : "component_search_filter_edit_button_clicked",
  HANDLE_BPMN_ELEMENTS_MSS_CLICKED : "handle_bpmn_elements_mss_clicked",
  HANDLE_BPMN_ELEMENTS_DATA_LANGUAGE_MSS_CHANGED: "handle_bpmn_elements_data_language_mss_changed",
  COMPONENT_CONTENT_TASK_DETAIL_TASK_ROLE_MSS_VALUE_CHANGED: "component_content_task_detail_task_role_mss_value_changed",
  COMPONENT_VARIABLE_MAP_VALUE_CHANGED: "component_variable_map_value_changed",
  COMPONENT_ADD_VARIABLE_MAP_VALUE: "component_add_variable_map_value",
  COMPONENT_REMOVE_VARIABLE_MAP_VALUE : "component_remove_variable_map_value",
  COMPONENT_ENTITY_MAP_VALUE_CHANGED : "component_entity_map_value_changed",
  COMPONENT_SEARCH_CRITERIA_EDIT_BUTTON_CLICKED : "component_search_criteria_edit_button_clicked",
};

// @CS.SafeComponent
class BPMNCustomComponentsPropertiesView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);
    this.groupMss = React.createRef();

    this.state = {
      isDirty: false
    };
  };

  componentDidMount () {
    this.props.store.bind('bpmn-custom-properties-change', this.contentStateChanged);
  }

  componentWillReceiveProps(oNextProps) {
      this.setState({
        isDirty: false
      });
  }

  contentStateChanged = () => {
    this.setState({});
  }

  handleTextElementChanged = function (sName, sSelectedAttributeId, sNewVal ) {
    EventBus.dispatch(oEvents.HANDLE_BPMN_ELEMENTS_TEXT_CHANGED, sName, sSelectedAttributeId, sNewVal);
  };

  handleMSSValueChanged = function (sName, aSelectedItems, oReferencedData) {
    EventBus.dispatch(oEvents.HANDLE_BPMN_ELEMENTS_MSS_CHANGED, sName, aSelectedItems, oReferencedData);
  };

  handleDataLanguageMSSValueChanged = function (sName, aSelectedItems, oReferencedData) {
    EventBus.dispatch(oEvents.HANDLE_BPMN_ELEMENTS_DATA_LANGUAGE_MSS_CHANGED, sName, aSelectedItems, oReferencedData);
  };

  handleMSSValueClicked = function(){
    EventBus.dispatch(oEvents.HANDLE_BPMN_ELEMENTS_MSS_CLICKED);
  };

  handleTagMSSChanged = function  (sKey, aSelectedItems, oReferencedData) {
    EventBus.dispatch(oEvents.BPMN_PROPERTIES_TAG_MSS_CHANGED, sKey, aSelectedItems, oReferencedData);
  };

  handleTagMSSChangedCustom = function  (sKey, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
    EventBus.dispatch(oEvents.BPMN_PROPERTIES_TAG_MSS_CHANGED_CUSTOM, sKey, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData);
  };

  handleAttributeMSSChangedCustom = function  (sKey, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
    EventBus.dispatch(oEvents.BPMN_PROPERTIES_ATTRIBUTE_MSS_CHANGED_CUSTOM, sKey, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData);
  };

  handleTaxonomyInfoValueChanged = (sName, iIndex, sValue) => {
    EventBus.dispatch(oEvents.COMPONENT_TAXONOMY_DATA_SOURCE_CLASS_VALUE_CHANGED, sName, iIndex, sValue);
  };

  handleVariableMapValueChanged = (sName, iIndex, sType, sValue) => {
    EventBus.dispatch(oEvents.COMPONENT_VARIABLE_MAP_VALUE_CHANGED, sName, iIndex, sType, sValue);
  };

  handleAddVariableMapClicked = (sName) => {
    EventBus.dispatch(oEvents.COMPONENT_ADD_VARIABLE_MAP_VALUE, sName);
  };

  handleRemoveVariableMapClicked = (sName, iIndex) => {
    EventBus.dispatch(oEvents.COMPONENT_REMOVE_VARIABLE_MAP_VALUE, sName, iIndex);
  };

  handleAddRowButtonClicked = (sName) => {
    EventBus.dispatch(oEvents.COMPONENT_ADD_ROW_BUTTON_CLICKED, sName);
  };

  handleRemoveRowButtonClicked = (sName, iIndex) => {
    EventBus.dispatch(oEvents.COMPONENT_REMOVE_ROW_BUTTON_CLICKED, sName, iIndex);
  };

  handleAddTagGroupClicked = (sName) => {
    EventBus.dispatch(oEvents.COMPONENT_ADD_TAG_GROUP_IN_CLASS_VALUE, sName);
  };

  handleRemoveTagGroupClicked = (sName, iIndex) => {
    EventBus.dispatch(oEvents.COMPONENT_REMOVE_TAG_GROUP_IN_CLASS_VALUE, sName, iIndex);
  };

  handleAddAttributeGroupClicked = (sName) => {
    EventBus.dispatch(oEvents.COMPONENT_ADD_ATTRIBUTE_GROUP_IN_CLASS_VALUE, sName);
  };

  handleRemoveAttributeGroupClicked = (sName, iIndex) => {
    EventBus.dispatch(oEvents.COMPONENT_REMOVE_ATTRIBUTE_GROUP_IN_CLASS_VALUE, sName, iIndex);
  };

  handleDateElementChanged = function (sName, sSelectedAttributeId, sNewVal) {
    EventBus.dispatch(oEvents.HANDLE_BPMN_ELEMENTS_DATE_CHANGED, sName, sSelectedAttributeId, sNewVal);
  };

  handleSearchFilterEditButtonClicked = (sName) => {
    EventBus.dispatch(oEvents.COMPONENT_SEARCH_FILTER_EDIT_BUTTON_CLICKED, sName);
  };

  handleTasksRoleMSSValueChanged = (sContext,sAction ,aNewValue) => {
    EventBus.dispatch(oEvents.COMPONENT_CONTENT_TASK_DETAIL_TASK_ROLE_MSS_VALUE_CHANGED,sContext,sAction, aNewValue);
    this.setState({isDirty: false});
    };

  handleEntityMapValueChanged = (sName, iIndex, sItemid, sNewVal) => {
    EventBus.dispatch(oEvents.COMPONENT_ENTITY_MAP_VALUE_CHANGED, sName, iIndex, sItemid, sNewVal);
  };

  handleSearchCriteriaEditButtonClicked = (sName) => {
    EventBus.dispatch(oEvents.COMPONENT_SEARCH_CRITERIA_EDIT_BUTTON_CLICKED, sName);
  };

  getRowListView = (oInputParameter, oInputSkeleton) => {
    var _this = this;
    var aViews = [];
    let oTooltip = oInputSkeleton.tooltip;
    let oDefinition = oInputParameter.definition;
    let sName = oInputParameter.name;
    let aSelectedItems = [];
    CS.forEach(oDefinition.items, function (oItem) {
      aSelectedItems.push(oItem.value);
    });

    CS.forEach(oDefinition.items, function (oItem, iIndex) {
      var oColumnNameView = (<CustomTextFieldView
          value={oItem.value}
          hintText={oInputSkeleton.label}
          onBlur={_this.handleTaxonomyInfoValueChanged.bind(_this, sName, iIndex)}/>);
      aViews.push(
          <div className="parameterRow">
            <div className="parameterValue">
              {oColumnNameView}
            </div>
            <TooltipView label={oTooltip.remove}>
              <div className="removeRowList"
                   onClick={_this.handleRemoveRowButtonClicked.bind(_this, sName, iIndex)}>
              </div>
            </TooltipView>
          </div>
      );
    });


    return (
        <div className="rowListWrapper">
          <TooltipView label={oTooltip.add}>
            <div className="addRowListButton" onClick={_this.handleAddRowButtonClicked.bind(_this, sName)}>
            </div>
          </TooltipView>
          <div className="rowListBody">{aViews}</div>
        </div>
    );
  };

  getTextFieldView = (oInputParameter,oInputSkeleton) => {
    let sName = oInputParameter.name;
    return (
        <CustomTextFieldView className={"custom" + sName} label={sName} value={oInputParameter.value}
                             onBlur={this.handleTextElementChanged.bind(this, sName,"")} isMultiLine={oInputSkeleton.isMultiLine} multiLineNumberOfRows="4"/>);
  };

  getInlineTextFieldView = (oInputParameter,oInputSkeleton) => {
    let sName = oInputParameter.name;
    let sPostLabel = oInputSkeleton.postLabel;
    return (
        <div className="inlineTextWrapper">
          <CustomTextFieldView className="inlineText" label={sName} value={oInputParameter.value}
                               onBlur={this.handleTextElementChanged.bind(this, sName, "")}/>
          <span className="postLabel">{sPostLabel}</span>
        </div>
    );
  };

  getMSSView = (sDelegateExpression, oInputParameter, oInputParameterSkeleton) => {
    let oStoreProps = this.props.store.props;
    let aSelectedItems = [];
    let oReferencedData = oStoreProps.referencedData || {};
    let oViewProperties = oInputParameterSkeleton.viewProperties || {};
    let bIsMultiSelect = oInputParameterSkeleton.selectionContext === "multiSelectList";
    if (bIsMultiSelect || oInputParameterSkeleton.selectionContext === "singleSelectList") {
      let oDefinition = oInputParameter.definition;

      CS.forEach(oDefinition.items, function (oItem) {
        aSelectedItems.push(oItem.value);
      });
    } else {
      aSelectedItems = CS.isEmpty(oInputParameter.value) ? [] : [oInputParameter.value];
    }

    if (oInputParameterSkeleton.requestResponseInfo) {
      let oRequestResponseInfo = oInputParameterSkeleton.requestResponseInfo;
      let oLazyMSSReferencedData = oReferencedData[oInputParameterSkeleton.referencedDataKey] || {};
      if (oInputParameterSkeleton.requestSelectedElementKey == "TalendJobs" && CS.isNotEmpty(aSelectedItems)) {
        CS.forEach(aSelectedItems, function (sSelectedItems) {
          oLazyMSSReferencedData[sSelectedItems] = {
            code: sSelectedItems,
            id: sSelectedItems,
            label: sSelectedItems
          }
        });
      }
      aSelectedItems = CS.filter(aSelectedItems, (sSelectedItem) => {
        return CS.isNotEmpty(oLazyMSSReferencedData[sSelectedItem]);
      });
      return (<LazyMSSView selectedItems={aSelectedItems}
                           onApply={this.handleMSSValueChanged.bind(this, oInputParameter.name)}
                           requestResponseInfo={oRequestResponseInfo}
                           referencedData={oLazyMSSReferencedData}
                           context={`${sDelegateExpression}_${oInputParameter.name}`}
                           isMultiSelect={bIsMultiSelect}
                           {...oViewProperties}
                           popoverStyle = {{maxWidth: '500px'}}

      />);
    } else {
      let oCustomPropertiesData = oStoreProps.customPropertiesData;
      let aItems = oInputParameterSkeleton.items || oCustomPropertiesData[oInputParameterSkeleton.propertyDataKey] || [];
      return (<MulitSelectView selectedItems={aSelectedItems}
                               items={aItems}
                               onApply={this.handleMSSValueChanged.bind(this, oInputParameter.name)}
                               context={sDelegateExpression}
                               isMultiSelect={bIsMultiSelect}
                               {...oViewProperties}
                               popoverStyle = {{maxWidth: '500px'}}
      />);
    }

  };

  getCheckBoxView = (oInputParameter) => {
    let sName = oInputParameter.name;
    let bIsChecked = oInputParameter.value == "true";
    return (<input type={"checkbox"} className={"propertyCheckbox"} checked={bIsChecked} onChange={this.handleTextElementChanged.bind(this,sName,"",(!bIsChecked).toString())}/>)
  };

  getTagIdView = (oTagId, oTagIdSkeleton, oReferencedData) => {
    let aSelectedItems = CS.isEmpty(oTagId.value) ? [] : [oTagId.value];
    let oRequestResponseInfo = oTagIdSkeleton.requestResponseInfo;
    let oLazyMSSReferencedData = oReferencedData[oTagIdSkeleton.referencedDataKey] || {};

    return (<div className={"bpmnPropertiesTagGroup"}>
      <LazyMSSView
          selectedItems={aSelectedItems}
          onApply={this.handleTagMSSChanged.bind(this, oTagId.key)}
          requestResponseInfo={oRequestResponseInfo}
          referencedData={oLazyMSSReferencedData}
          context={oTagId.key}
          isMultiSelect={false}/>
    </div>);
  };

  getTagValueView = (oTagValue, oTagValueIdSkeleton, sSelectedTagGroup, oReferencedData) => {
    let aSelectedItems = CS.isEmpty(oTagValue.value) ? [] : [oTagValue.value];
    let oRequestResponseInfo = CS.cloneDeep(oTagValueIdSkeleton.requestResponseInfo);
    oRequestResponseInfo.customRequestModel = {
      tagGroupId: sSelectedTagGroup
    };
    let oLazyMSSReferencedData = oReferencedData[oTagValueIdSkeleton.referencedDataKey] || {};
    return (
        <div className={"bpmnPropertiesTagGroup"}>
          <LazyMSSView selectedItems={aSelectedItems}
                       onApply={this.handleTagMSSChanged.bind(this, oTagValue.key)}
                       requestResponseInfo={oRequestResponseInfo}
                       referencedData={oLazyMSSReferencedData}
                       context={sSelectedTagGroup}
                       isMultiSelect={false}
          />
        </div>)
  };

  getTagGroupView = (oInputParameter, oMockDataForProcessInputElement) => {
    let aViewToReturn = [];
    let aEntries = oInputParameter.definition.entries;
    let oTagIDEntry = aEntries[0];
    let oTagValueEntry = aEntries[1];
    let oStoreProps = this.props.store.props;
    let oReferencedData = oStoreProps.referencedData || {};
    let sSelectedTagId = oTagIDEntry.value;
    aViewToReturn.push(this.getTagIdView(oTagIDEntry, oMockDataForProcessInputElement[oTagIDEntry.key], oReferencedData));
    !CS.isEmpty(sSelectedTagId) && aViewToReturn.push(this.getTagValueView(oTagValueEntry, oMockDataForProcessInputElement[oTagValueEntry.key], sSelectedTagId, oReferencedData));
    return aViewToReturn;
  };

  getTagIdCustomView = (sTagId, oTagIdSkeleton, oReferencedData, aExcludedItems) => {
    let aSelectedItems = CS.isEmpty(sTagId) ? [] : [sTagId];
    let oRequestResponseInfo = oTagIdSkeleton.requestResponseInfo;
    let oLazyMSSReferencedData = oReferencedData[oTagIdSkeleton.referencedDataKey] || {};

    return (<div className={"bpmnPropertiesTagGroup"}>
      <LazyMSSView
          selectedItems={aSelectedItems}
          excludedItems={aExcludedItems}
          onApply={this.handleTagMSSChangedCustom.bind(this, sTagId,"tagKey","")}
          requestResponseInfo={oRequestResponseInfo}
          referencedData={oLazyMSSReferencedData}
          context={sTagId}
          isMultiSelect={false}
          cannotRemove={true}/>
    </div>);
  };

  getTagValueCustomView = (sTagValueId, oTagValueIdSkeleton, sSelectedTagGroupId, oReferencedData) => {
    let aSelectedItems = CS.isEmpty(sTagValueId) ? [] : sTagValueId.split(',');
    let oRequestResponseInfo = CS.cloneDeep(oTagValueIdSkeleton.requestResponseInfo);
    oRequestResponseInfo.customRequestModel = {
      tagGroupId: sSelectedTagGroupId
    };
    let oLazyMSSReferencedData = oReferencedData[oTagValueIdSkeleton.referencedDataKey] || {};
    if (oLazyMSSReferencedData && oLazyMSSReferencedData[sSelectedTagGroupId].tagType === TagTypeConstants.TAG_TYPE_BOOLEAN) {
      let bValue = (CS.isNotEmpty(sTagValueId)) ? true : false;
      return (
          <div className="toggleSelection">
            <GridYesNoPropertyView
                isDisabled={false}
                value={bValue}
                onChange={this.handleTagMSSChangedCustom.bind(this, sTagValueId, "tagValue", sSelectedTagGroupId)}
            />
          </div>)
    } else {
      let bIsMultiSelect = oLazyMSSReferencedData[sSelectedTagGroupId].isMultiselect ? oLazyMSSReferencedData[sSelectedTagGroupId].isMultiselect : false;
      let bCannotRemove = bIsMultiSelect ? false : true;
      return (
          <div className={"bpmnPropertiesTagGroup"}>
            <LazyMSSView selectedItems={aSelectedItems}
                         onApply={this.handleTagMSSChangedCustom.bind(this, sTagValueId, "tagValue", sSelectedTagGroupId)}
                         requestResponseInfo={oRequestResponseInfo}
                         referencedData={oLazyMSSReferencedData}
                         context={sSelectedTagGroupId}
                         isMultiSelect={bIsMultiSelect}
                         cannotRemove={bCannotRemove}/>
          </div>)
    }

  };

  getTagGroupCustomView = (oEntry, oMockDataForProcessInputElement, aExcludedItems) => {
    var _this = this;
    let aViewToReturn = [];
    let sTagId = oEntry.key;
    let sTagValueId = oEntry.value || "";
    let oStoreProps = _this.props.store.props;
    let oReferencedData = oStoreProps.referencedData || {};
    let sSelectedTagId = sTagId;
    if(CS.isNotEmpty(sTagId) && oReferencedData.referencedTags[sTagId] && (oReferencedData.referencedTags[sTagId].tagType === TagTypeConstants.TAG_TYPE_BOOLEAN)){
      sTagValueId = oEntry.value;
    }
    let oTagIdSkeleton = oMockDataForProcessInputElement["tagId"];
    let aRefTags = oReferencedData[oTagIdSkeleton.referencedDataKey];
    if( oReferencedData &&  aRefTags && CS.isEmpty(aRefTags[sSelectedTagId])){
      sSelectedTagId = [];
    }
    if (CS.isNotEmpty(sSelectedTagId) || CS.isEmpty(sTagId)) {
      aViewToReturn.push(_this.getTagIdCustomView(sTagId, oTagIdSkeleton, oReferencedData, aExcludedItems));
    }
    !CS.isEmpty(sSelectedTagId) && aViewToReturn.push(_this.getTagValueCustomView(sTagValueId, oMockDataForProcessInputElement["tagValueId"], sSelectedTagId, oReferencedData));
    return aViewToReturn;
  };

  getCustomTagGroupView = (oInputParameter, oMockDataForProcessInputElement) => {
    var _this = this;
    let sName = oInputParameter.name;
    var aTagGroup = [];
    let aEntries = oInputParameter.definition.entries;
    let aExcludedItems = CS.map(aEntries, function (oEntry) {
      return oEntry.key;
    });

    CS.forEach(aEntries, function (oEntry, iIndex) {

      let oTagRow = _this.getTagGroupCustomView(oEntry, oMockDataForProcessInputElement,aExcludedItems);
      aTagGroup.push(<div className="parameterRow">
        <div className="parameterValue">
          {oTagRow}
        </div>
        <TooltipView label={getTranslation().REMOVE_TAGGROUP}>
          <div className="removeTagGroup"
               onClick={_this.handleRemoveTagGroupClicked.bind(_this, sName, iIndex)}>
          </div>
        </TooltipView>
      </div>);
    });

    return (<div className="tagGroupWrapper">
      <TooltipView label="Add TagGroup">
        <div className="addTagGroupButton" onClick={_this.handleAddTagGroupClicked.bind(_this, sName)}>
        </div>
      </TooltipView>
      <div className="tagGroupBody">{aTagGroup}</div>
    </div>);
};

  handleDateChanged = (sKey, sSelectedAttributeId, oNull, oDate) => {
    let sValue = oDate ? oDate.getTime() : "";
    this.handleDateElementChanged(sKey, sSelectedAttributeId, sValue);
  };

  getAttributeIdCustomView = (sAttributeId, oAttributeIdSkeleton, oReferencedData, aExcludedItems) => {
    let aSelectedItems = CS.isEmpty(sAttributeId) ? [] : [sAttributeId];
    let oRequestResponseInfo = oAttributeIdSkeleton.requestResponseInfo;
    let oLazyMSSReferencedData = oReferencedData[oAttributeIdSkeleton.referencedDataKey] || {};

    return (<div className={"bpmnPropertiesTagGroup"}>
      <LazyMSSView
          selectedItems={aSelectedItems}
          excludedItems={aExcludedItems}
          onApply={this.handleAttributeMSSChangedCustom.bind(this,sAttributeId,"attributeKey","")}
          requestResponseInfo={oRequestResponseInfo}
          referencedData={oLazyMSSReferencedData}
          context={sAttributeId}
          isMultiSelect={false}
          cannotRemove={true}/>
    </div>);
  };

  getAttributeValueCustomView = (sAttributeValue, oAttributeValueIdSkeleton, sSelectedAttributeId, oReferencedData,oInputParameter) => {
    let oDefinition = oInputParameter.definition;
    let aEntries = oDefinition.entries;
    let oEntry = CS.find(aEntries,{key:sSelectedAttributeId});
    let sName = oInputParameter.name;
    let oReferencedAttribute = oReferencedData.referencedAttributes[oEntry.key];
    let oData = null;

    if (AttributeUtils.isAttributeTypeDate(oReferencedAttribute.type)) {
      let oValue = oEntry.value ? new Date(+oEntry.value) : null;
      oData = <CustomDatePicker
        value={oValue}
        className=" datePickerCustomImBPMN "
        disabled={false}
        hintText="Select Date"
        onChange={this.handleDateChanged.bind(this, sName, sSelectedAttributeId)}
        endOfDay={false}/>;
    } else {
      oData = <CustomTextFieldView
        value={oEntry.value}
        hintText="Attribute value"
        onBlur={this.handleTextElementChanged.bind(this, sName, sSelectedAttributeId)}/>;
    }
    return (
      <div className={"bpmnPropertiesTagGroup"} key={sSelectedAttributeId + UniqueIdentifierGenerator.generateUUID()}>
        {oData}
      </div>
    )
  };

  getAttributeGroupView = (oEntry, oMockDataForProcessInputElement, aExcludedItems ,oInputParameter) => {
    var _this = this;
    let aViewToReturn = [];
    let sAttributeIDEntry = oEntry.key;
    let sAttributeValueEntry = oEntry.value || "";
    let oStoreProps = _this.props.store.props;
    let oReferencedData = oStoreProps.referencedData || {};
    let sSelectedAttributeId = sAttributeIDEntry;
    let oAttributeIdSkeleton = oMockDataForProcessInputElement["attributeId"];
    let aRefAttr = oReferencedData[oAttributeIdSkeleton.referencedDataKey];
    if( oReferencedData &&  aRefAttr && !aRefAttr[sSelectedAttributeId]){
      sSelectedAttributeId = [];
    }
    if (CS.isNotEmpty(sSelectedAttributeId) || CS.isEmpty(sAttributeIDEntry)) {
      aViewToReturn.push(_this.getAttributeIdCustomView(sAttributeIDEntry, oAttributeIdSkeleton, oReferencedData, aExcludedItems));
    }
    !CS.isEmpty(sSelectedAttributeId) && aViewToReturn.push(_this.getAttributeValueCustomView(sAttributeValueEntry, oMockDataForProcessInputElement["attributeValue"], sSelectedAttributeId, oReferencedData,oInputParameter));
    return aViewToReturn;

  };

  getAttributeValueMapView = (oInputParameter, oMockDataForProcessInputElement) => {
    var _this = this;
    let sName = oInputParameter.name;
    var aAttributeValueGroup = [];
    let aEntries = oInputParameter.definition.entries;
    let aExcludedItems = CS.map(aEntries, function (oEntry) {
      return oEntry.key;
    });

    CS.forEach(aEntries, function (oEntry, iIndex) {

      let oAttributeRow = _this.getAttributeGroupView(oEntry, oMockDataForProcessInputElement, aExcludedItems ,oInputParameter);
      aAttributeValueGroup.push(<div className="parameterRow">
        <div className="parameterValue">
          {oAttributeRow}
        </div>
        <TooltipView label={getTranslation().REMOVE_ATTRIBUTE}>
          <div className="removeTagGroup"
               onClick={_this.handleRemoveAttributeGroupClicked.bind(_this, sName, iIndex)}>
          </div>
        </TooltipView>
      </div>);
    });

    return (<div className="tagGroupWrapper">
      <TooltipView label={getTranslation().ADD_ATTRIBUTES}>
        <div className="addTagGroupButton" onClick={_this.handleAddAttributeGroupClicked.bind(_this, sName)}>
        </div>
      </TooltipView>
      <div className="tagGroupBody">{aAttributeValueGroup}</div>
    </div>);
  }

  getModuleIdDOM = (oInputParameter) => {
    let aItems = MockDataForProcessFilterModules();
    let sValue = oInputParameter.value;
    let bIsMultiSelect = false;
    let sModuleId = getTranslation().MODULE_ID;
    let sDelegateExpression = "searchFilterModuleId"
    let oModule = <MulitSelectView selectedItems={[sValue]}
                               items={aItems}
                               onApply={this.handleMSSValueChanged.bind(this, oInputParameter.name)}
                               context={sDelegateExpression}
                               isMultiSelect={bIsMultiSelect}
    />

    return (
        <div>
          <div>
            {sModuleId} :
          </div>
          {oModule}
        </div>
    )
  };

  getDataLanguageDOM = (oInputParameter) => {
    let oStoreProps = this.props.store.props;
    let oReferencedData = oStoreProps.referencedData;
    let oReferencedLanguages = oReferencedData.referencedLanguages;
    let aItems = oInputParameter.items;
    let sValue = oInputParameter.value;
    let oRefLanguage = {};

    if (CS.isNotEmpty(aItems) &&  CS.isNotEmpty(sValue)) {
      let oLanguage = CS.find(aItems, {code: sValue});
      if (CS.isNotEmpty(oLanguage)) {
        sValue = oLanguage.id;
      }
    } else if(CS.isEmpty(aItems) && CS.isNotEmpty(sValue)) {
      oRefLanguage = oReferencedLanguages[sValue];
      if (CS.isNotEmpty(oRefLanguage)) {
        sValue = oRefLanguage.id;
        aItems = [oRefLanguage];
      }
    }
    let oBusinessObject = oStoreProps.element.businessObject;
    let sCustomElementID = ViewUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject);
    /** Fetching language data from referenced languages when dataLanguage is selected on dialog open(1st time)*/
    let sLanguage = (sCustomElementID === "searchContentController") ? getTranslation().DATA_LANGUAGE : getTranslation().SCHEDULER_INPUT_LANGUAGE;
    let sDelegateExpression = "dataLanguage"
    let oDataLanguage = <MulitSelectView selectedItems={[sValue]}
                                         items={aItems}
                                         onApply={this.handleDataLanguageMSSValueChanged.bind(this, oInputParameter.name)}
                                         onPopOverOpen={this.handleMSSValueClicked}
                                         context={sDelegateExpression}
                                         isMultiSelect={false}
    />
    return (
        <div>
          <div>{sLanguage} :</div>
          {oDataLanguage}
        </div>
    )
  };

  getSearchContentMapView = (oInputParameter) => {
    let oStoreProps = this.props.store.props;
    let oCustomPropertiesData = oStoreProps.customPropertiesData;
    let oFilterData = oCustomPropertiesData.filterData;
    let sOrginalAppliedFilter = oInputParameter.originalValue;
    let oAppliedFilterData = {};
    if(!CS.isEmpty(sOrginalAppliedFilter)) {
      oAppliedFilterData = JSON.parse(sOrginalAppliedFilter);
    }

    return (<div className="searchComponentProperty">
      <div className="advancedFilterContainer">
        <AppliedFilterView appliedFilterData={oAppliedFilterData}
                           appliedFilterDataClone={{}}
                           masterAttributeList={oFilterData.masterAttributeList}
                           filterContext={oFilterData.filterContext}
                           showClearFilterButton={true}/>
      </div>
      <CustomMaterialButtonView
          label={getTranslation().EDIT}
          isRaisedButton={true}
          isDisabled={false}
          onButtonClick={this.handleSearchFilterEditButtonClicked.bind(this)}/>
    </div>)
  };

  getTaxonomiesListView = (oInputParameter) => {
    let oStoreProps = this.props.store.props;
    let oDefinition = oInputParameter.definition;
    let aSelectedTaxonomies = oDefinition.items || [];
    let aSelectedTaxonomyIds = CS.map(aSelectedTaxonomies, 'value');
    let oAllowedTaxonomiesById = oStoreProps.taxonomy;
    let oCustomPropertiesData = oStoreProps.customPropertiesData;
    let oReferencedTaxonomy = oCustomPropertiesData.referencedTaxonomiesForComponent;
    let oMultiTaxonomyDataForProcess = ViewUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomy, oAllowedTaxonomiesById) || {};
    let oTaxonomyDOM = this.getSmallTaxonomyView(oMultiTaxonomyDataForProcess);
    return (<div className="abc">{oTaxonomyDOM}</div>);
  };

  getSmallTaxonomyView = (oMultiTaxonomyDataForProcess) => {
    let oStoreProps = this.props.store.props;

    let oCustomPropertiesData = oStoreProps.customPropertiesData;
    let oSmallTaxonomyPaginationData = oCustomPropertiesData.oSmallTaxonomyPaginationData;
    let oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oMultiTaxonomyDataForProcess);
    return (
        <div className='goldenRecordMatchEntitiesWrapper'>
          <SmallTaxonomyView model={oSmallTaxonomyViewModel}
                             isDisabled={false}
                             context="processComponentTaxonomy"
                             showAllComponents={true}
                             paginationData={oSmallTaxonomyPaginationData}
          />
        </div>)
  };

  getSmallTaxonomyViewModel = (oMultiTaxonomyData) => {
    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    var sRootTaxonomyId = ViewUtils.getTreeRootId();
    var oProperties = {};
    oProperties.headerPermission = {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };

  getSourceTypeView = (oInputParameter) => {
    let aItems = MockDataForProcessSourceTypeModules();
    let sValue = oInputParameter.value;
    let bIsMultiSelect = false;
    let sSourceType = "Source Type";
    let sDelegateExpression = "sourceType"
    let oSourceTypeDOM = (
        <MulitSelectView selectedItems={[sValue]}
                         items={aItems}
                         onApply={this.handleMSSValueChanged.bind(this, oInputParameter.name)}
                         context={sDelegateExpression}
                         isMultiSelect={bIsMultiSelect}
        />)

    return (
        <div>
          <div>
            {sSourceType} :
          </div>
          {oSourceTypeDOM}
        </div>
    )
  };

  handleOptionClicked = () => {
    this.setState({
      isDirty: true
    });
  }

  getSelectedRoles = (aUserIds, aRoleIds) => {
    let aSelectedRoles = [];
    let oStoreProps = this.props.store.props;
    let oActiveProcess = oStoreProps.activeProcess.clonedObject ? oStoreProps.activeProcess.clonedObject : oStoreProps.activeProcess;
    let oConfigDetails = oActiveProcess.configDetails;
    let oReferencedUsers = oConfigDetails.referencedUsers;
    let oReferencedRoles = oConfigDetails.referencedRoles;
    CS.forEach(aUserIds, function (sUserId) {
      let oUser = CS.find(oReferencedUsers, {id: sUserId});
      if (CS.isNotEmpty(oUser)) {
        aSelectedRoles.push({
          id: sUserId,
          label: oUser.label,
          groupType: "users"
        })
      }
    });
    CS.forEach(aRoleIds, function (sRoleId) {
      let oRole = CS.find(oReferencedRoles, {id: sRoleId});
      if (CS.isNotEmpty(oRole)) {
        aSelectedRoles.push({
          id: sRoleId,
          label: oRole.label,
          groupType: "roles"
        })
      }
    });
    return aSelectedRoles;
  };

  getGroupMssView = (oInputParameter,oInputParameterSkeleton) => {
    let oValue = JSON.parse(oInputParameter.value);
    let sContext = oInputParameter.name;
    let bIsMultiSelect = oInputParameterSkeleton.isMultiSelect;
    let aEditableRoles = ["consulted","informed","responsible","verify","signoff"];
    let aSelectedRole = this.getSelectedRoles(oValue.userIds, oValue.roleIds);
    let bIsDisabled = !(CS.includes(aEditableRoles, sContext));
    let sClassName = "taskMSSViewContainer ";
    sClassName += sContext;
    let aTaskRolesData = [];

    let aKeyList = ["requestResponseInfoForUsersList", "requestResponseInfoForRoleList"];
    CS.forEach(aKeyList, function (sKey) {
        let oReqInfo = oInputParameterSkeleton[sKey];
        let oCustomReqModel = oReqInfo.requestInfo.customRequestInfoModel;
        /** To fetch all roles and users of all the organisation **/
        oCustomReqModel.organizationId = null;
        aTaskRolesData.push(oReqInfo);
    });

    let oStoreProps = this.props.store.props;
    let oActiveProcess = oStoreProps.activeProcess;
    let oConfigDetails = oActiveProcess.isDirty ? oActiveProcess.clonedObject.configDetails : oActiveProcess.configDetails;
    let oReferencedUsers = oConfigDetails.referencedUsers;
    let oReferencedRoles = oConfigDetails.referencedRoles;
    let oReferencedData =  {};
    CS.assign(oReferencedData,oReferencedUsers);
    CS.assign(oReferencedData,oReferencedRoles);
    let aGroupOptionsToShow = MockDataForTaskRolesMSS[sContext];

    aTaskRolesData = CS.filter(aTaskRolesData, function (oRoles) {
      return CS.includes(aGroupOptionsToShow, oRoles.id);
    });

    return (
        <div className={sClassName} key={"taskMSSView" + sContext + oActiveProcess.id}>
          <div className="taskMSSDetail">
            <GroupMssWrapperView
                groupsData={aTaskRolesData}
                handleApplyButton={this.handleTasksRoleMSSValueChanged.bind(this, sContext, "add")}
                removeOption={this.handleTasksRoleMSSValueChanged.bind(this, sContext, "remove")}
                activeOptions={aSelectedRole}
                hideChips={false} // Chips is hidden always in xray view
                showPopup={true}
                showApply={true}
                isMultiSelect={bIsMultiSelect}
                ref={this.groupMss}
                disabled={bIsDisabled}
                referencedData={oReferencedData}
                handleOptionClicked={this.handleOptionClicked}
                isDirty={this.state.isDirty}
            />
          </div>
        </div>
    )

  };

  getVariableMapView = (oInputParameter) => {
    let _this = this;
    let aViews = [];

    let oDefinition = oInputParameter.definition;
    let sName = oInputParameter.name;

    CS.forEach(oDefinition.entries, function (oItem, iIndex) {
      let oKeyView = (<CustomTextFieldView
      value={oItem.key}
      hintText={getTranslation().VARIABLE}
      onBlur={_this.handleVariableMapValueChanged.bind(_this, sName, iIndex, "key")}/>);
      let oValueView = (<CustomTextFieldView
          value={oItem.value}
          hintText={getTranslation().OUTPUT_VARIABLE}
          onBlur={_this.handleVariableMapValueChanged.bind(_this, sName, iIndex, "value")}/>);
      aViews.push(
          <div className="variableMapRow">
            <div className="variableKey">
              {oKeyView}
            </div>
            <div className="variableValue">
              {oValueView}
            </div>
            <TooltipView label={getTranslation().REMOVE_VARIABLE_MAP}>
              <div className="removeVariableMap"
                   onClick={_this.handleRemoveVariableMapClicked.bind(_this, sName, iIndex)}>
              </div>
            </TooltipView>
          </div>
      );
    });


    return (
        <div className="variableMapWrapper">
          <TooltipView label={getTranslation().ADD_VARIABLE_MAP}>
            <div className="addVariableMapButton" onClick={_this.handleAddVariableMapClicked.bind(_this, sName)}>
            </div>
          </TooltipView>
          <div className="variableMapBody">{aViews}</div>
        </div>
    );
  };

  /** Transfer Component EntityMap View **/
  getEntityMapView = (sDelegateExpression, oInputParameter, oMockDataForProcessInputElement ,oInputParameterSkeleton) => {
    let _this = this;
    let aViews = [];
    let sName = oInputParameter.name;
    let oDefinition = oInputParameter.definition;
    let aEntity = oInputParameterSkeleton.entityList;
    CS.forEach(oDefinition.entries, function (oItem, iIndex) {
      let oFoundItem = CS.find(aEntity, {id : oItem.key});
      let oKeyView = <div>{oFoundItem.label} : </div>;
      let oValueView = (<CustomTextFieldView
          value={oItem.value}
          onBlur={_this.handleEntityMapValueChanged.bind(_this, sName, iIndex, oItem.key)}/>);
      aViews.push(
          <div className="entityMapRow">
            <div className="entityMapKey">
              {oKeyView}
            </div>
            <div className="entityMapValue">
              {oValueView}
            </div>
          </div>
      );
    });

    return (
        <div className="entityMapWrapper">
          <div className="entityMapBody">{aViews}</div>
        </div>
    );
  };

  /** Function to display edit button and the choose Taxonomy dialog box **/
  getSearchCriteriaMapView = () => {
    let oChooseTaxonomyHandler = this.handleSearchCriteriaEditButtonClicked;
    return (
        <CustomMaterialButtonView
            label={"Edit"}
            isRaisedButton={true}
            isDisabled={false}
            onButtonClick={oChooseTaxonomyHandler}/>)
  };

  /** Function to display Search Criteria TextArea Field **/
  getSearchCriteriaTextAreaView = (oInputParameter, oMockDataForProcessInputElement, oInputParameterSkeleton) => {
    let sName = oInputParameterSkeleton.name;
    let sClassName = "customTextFieldView";
    if (oInputParameter.hasOwnProperty("value")) {
      if(!oInputParameter.value.startsWith("$")) {
        if (CS.isNotEmpty(JSON.parse(oInputParameter.value))) {
          sClassName += " isDisabled";
        }
      }
    }
    return (
        <div className="textAreaWrapper">
          <div className="customTextFieldLabel">{sName}</div>
          <CustomTextFieldView className={sClassName} label={sName} value={oInputParameter.value}
                               onBlur={this.handleTextElementChanged.bind(this, "SEARCH_CRITERIA", "")} isMultiLine={true}
                               multiLineNumberOfRows="4"/>
        </div>
    );
  };

  fillPropertiesViewsByType = (aViews, aInputParameters, sDelegateExpression, oMockDataForProcessInputElement, aInvalidPropertyList = []) => {
    CS.forEach(aInputParameters, (oInputParameter) => {
      let sName = oInputParameter.name;
      let oView = null;
      let oInputParameterSkeleton = oMockDataForProcessInputElement[sName];
      CS.isEmpty(oInputParameterSkeleton) && (oInputParameterSkeleton = {});
      if(!oInputParameterSkeleton.isHidden) {
        let sType = oInputParameterSkeleton.type;

        switch (sType) {
          case "text":
            oView = this.getTextFieldView(oInputParameter,oInputParameterSkeleton);
            break;
          case "list":
            oView = this.getMSSView(sDelegateExpression, oInputParameter, oInputParameterSkeleton);
            break;
          case "rowList":
            oView = this.getRowListView(oInputParameter,oInputParameterSkeleton);
            break;

          case "checkbox":
            oView = this.getCheckBoxView(oInputParameter);
            break;

          case "tagGroup":
            oView = this.getTagGroupView(oInputParameter, oMockDataForProcessInputElement);
            break;

          case "tagsMap":
            oView = this.getCustomTagGroupView(oInputParameter, oMockDataForProcessInputElement ,oInputParameterSkeleton);
            break;

          case "attributesValueMap":
            oView = this.getAttributeValueMapView(oInputParameter, oMockDataForProcessInputElement ,oInputParameterSkeleton);
            break;

          case "searchFilterContent":
            oView = this.getSearchContentMapView(oInputParameter);
            break;

          case "moduleId":
            oView = this.getModuleIdDOM(oInputParameter);
            break;

          case "taxonomiesList":
            oView = this.getTaxonomiesListView(oInputParameter);
            break;

          case "sourceType":
            oView = this.getSourceTypeView(oInputParameter);
            break;

          case "dataLanguage":
          case "setDataLanguage":
            oView = this.getDataLanguageDOM(oInputParameter);
            break;

          case "groupMss":
            oView = this.getGroupMssView(oInputParameter,oInputParameterSkeleton);
            break;

          case "variableMap":
            oView = this.getVariableMapView(oInputParameter);
            break;

          case "entityMap":
            oView = this.getEntityMapView(sDelegateExpression, oInputParameter, oMockDataForProcessInputElement, oInputParameterSkeleton);
            break;

          case "inlineText":
            oView = this.getInlineTextFieldView(oInputParameter,oInputParameterSkeleton);
            break;

          case "searchCriteriaEdit":
            oView = this.getSearchCriteriaMapView(oInputParameter, oMockDataForProcessInputElement, oInputParameterSkeleton);
            break;

          case "searchCriteria":
            oView = this.getSearchCriteriaTextAreaView(oInputParameter, oMockDataForProcessInputElement, oInputParameterSkeleton);
            break;
        }
        let sErrorField = (aInvalidPropertyList.includes(sName)) ? "errorField" : "";

        oView && (aViews.push(<div className={"bpmnCustomProperty " + sType}>
          <div className={"bpmnCustomPropertyLabel bpmnCustomPropertyLabelBold"}>{oInputParameterSkeleton.label}</div>
          <div className={"bpmnCustomPropertyView " + sErrorField}>{oView} </div>
        </div>));
      }
    });
  };

  /**Segregation Of Input and Output Parameter **/
  segregationOfInputOutputParameter = (oValue) => {
    let aOutputParameters = CS.remove(oValue.inputParameters, function (oElement) {
      if (CS.isNotEmpty(oElement.$attrs.parameterType)) {
        if (oElement.$attrs.parameterType == "output") {
          return true;
        }
      }
      return false;
    });
    if (CS.isNotEmpty(aOutputParameters)) {
      if (CS.isEmpty(oValue.outputParameters)) {
        oValue.outputParameters = [];
      }
      oValue.outputParameters = oValue.outputParameters.concat(aOutputParameters);
    }
  };

  getViews = () => {
    let oStoreProps = this.props.store.props;
    let oBusinessObject = oStoreProps.element.businessObject;
    let oExtensionElements = oBusinessObject.extensionElements;
    let oValue = oExtensionElements.values[0];
    this.segregationOfInputOutputParameter(oValue);
    let aInputParameters = CS.cloneDeep(oValue.inputParameters);
    let aOutputParameters = CS.cloneDeep(oValue.outputParameters);
    let sDelegateExpression = oBusinessObject.delegateExpression;
    let sCustomElementID = ViewUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject);
    let oMockDataForProcessInputElement = MockDataForProcessInputElements(sCustomElementID);
    let aViews = [];
    let oCustomPropertiesData = oStoreProps.customPropertiesData;
    let aInputParamForAdvancedOptionToggle = [{
      name: "isAdvancedOptionsEnabled",
      value: oCustomPropertiesData.isAdvancedOptionsEnabled.toString()
    }];
    let sTaskId = oBusinessObject.id;
    let oWorkflowToValidate = oStoreProps.validationInfo;
    let aInvalidPropertyList = (CS.isNotEmpty(oWorkflowToValidate) && !oWorkflowToValidate.isWorkflowValid && CS.isNotEmpty(oWorkflowToValidate.validationDetails[sTaskId])) ? oWorkflowToValidate.validationDetails[sTaskId] : [];
    this.fillPropertiesViewsByType(aViews, aInputParamForAdvancedOptionToggle, sDelegateExpression, oMockDataForProcessInputElement);
    /***For Advanced Tab Toggle Option****/
    aViews.push(<div className="parameters">Input Parameters</div>);
    this.fillPropertiesViewsByType(aViews, aInputParameters, sDelegateExpression, oMockDataForProcessInputElement, aInvalidPropertyList);
    aViews.push(<div className="parameters">Output Parameters</div>);
    this.fillPropertiesViewsByType(aViews, aOutputParameters, sDelegateExpression, oMockDataForProcessInputElement, aInvalidPropertyList);

    return aViews;

  };

  render () {
    return (<div className={"bpmnCustomComponentsPropertiesView"}>{this.getViews()}</div>)

  }
}


export const view = BPMNCustomComponentsPropertiesView;
export const events = oEvents;
