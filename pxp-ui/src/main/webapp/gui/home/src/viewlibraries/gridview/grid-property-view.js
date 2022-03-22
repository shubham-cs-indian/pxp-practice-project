import CS from '../../libraries/cs';
import React, {Fragment} from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../tack/view-library-constants';
import MockDataForMeasurementMetricAndImperial from './../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import ContentAttributeElementViewModel from '../../viewlibraries/attributeelementview/model/content-attribute-element-view-model';
import { view as ContentAttributeElementView } from './../attributeelementview/content-attribute-element-view';
import MockDataIdsForFroalaView from './../../commonmodule/tack/mock-data-ids-for-froala-view';
import { view as CustomTextFieldView } from './../customtextfieldview/custom-text-field-view';
import { view as NumberLocaleView } from './../numberlocaleview/number-locale-view';
import { view as FroalaWrapperView } from '../customfroalaview/froala-wrapper-view';
import ContentMeasurementMetricsViewModel from './../measurementmetricview/model/content-measurement-metrics-view-model';
import { view as ContentMeasurementMetricsView } from './../measurementmetricview/content-measurement-metrics-view';
import { view as MultiSelectSearchView } from './../multiselectview/multi-select-search-view';
import { view as LazyMultiSelectSearchView } from './../lazy-mss-view/lazy-mss-view';
import { view as GridImagePropertyView } from './grid-image-property-view';
import { view as GridYesNoPropertyView } from './grid-yes-no-property-view';
import { view as GridTickPropertyView } from './grid-tick-property-view';
import { view as CustomDatePickerView } from './../customdatepickerview/customdatepickerview';
import { view as CustomeDateTimePickerView } from  './../customdatetimepickerview/custom-datetime-picker-view';
import { view as GridContextualAttributePropertyView } from './grid-contextual-attribute-property-view';
import { view as GridConcatenatedAttributePropertyView } from './grid-concatenated-attribute-property-view';
import { view as GridCalculatedAttributePropertyView } from './grid-calculated-attribute-property-view';
import { view as GridLinkedInstanceSummaryView } from './grid-linked-instance-summary-view';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import { view as TagGroupView } from '../taggroupview/tag-group-view.js';
import { view as GridMSSWithAdditionalList } from './grid-mss-with-additional-list';
import { view as SmallTaxonomyView } from '../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import ConfigDataEntitiesDictionary from '../../commonmodule/tack/config-data-entities-dictionary';
import ConcatenatedEntityTypes from '../../commonmodule/tack/concatenated-entity-types';
import AttributeTypeDictionary from '../../commonmodule/tack/attribute-type-dictionary-new';
import { view as CustomMobileNumberView } from '../custommobilenumberView/custom-mobile-number-view';
import {view as ChipsView} from "../chipsView/chips-view";
import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";
import ContentConcatenatedAttributeViewModel
  from "../concatenatedattributeview/model/content-concatenated-attribute-view-model";
import {view as ContentConcatenatedAttributeView} from "../concatenatedattributeview/content-concatenated-attribute-view";
import {view as StatusBar} from '../statusBar/status-bar';
import {view as GroupMSSChipsView} from "../filter/group-mss-chips-view";
import {view as MultiClassificationSectionView} from "../../screens/homescreen/screens/contentscreen/view/multiclassification-section-view";

const oEvents = {
  GRID_PROPERTY_KEY_EVENT: "grid_property_key_event",
  GRID_PROPERTY_PARENT_EXPAND_TOGGLED: "grid_property_parent_expand_toggled",
  GRID_PROPERTY_FILTER_CLICKED: "grid_property_filter_clicked",
  GRID_PROPERTY_CLEAR_SHOULD_FOCUS: "grid_property_clear_should_focus",
  GRID_PROPERTY_VALUE_CHANGED: "handle_grid_property_value_changed",
  GRID_PROPERTY_VALUE_CREATED: "handle_grid_property_value_created",
  GRID_DATE_FIELD_FOCUSED:"handle_grid_date_focused",
  GRID_TAG_PROPERTY_VALUE_CHANGED: "handle_grid_tag_property_value_changed",
  GRID_PROPERTY_VIEW_CHIPS_EDIT_BUTTON_CLICKED: "Grid_property_view_chips_edit_button_clicked",
  GRID_PROPERTY_VIEW_CLASSIFICATION_DIALOG_BUTTON_CLICKED: "GRID_PROPERTY_VIEW_CLASSIFICATION_DIALOG_BUTTON_CLICKED"
};

const oPropTypes = {
  propertyType: ReactPropTypes.string,
  propertyId: ReactPropTypes.string,
  property: ReactPropTypes.object,
  contentId: ReactPropTypes.string,
  pathToRoot: ReactPropTypes.string,
  isParent: ReactPropTypes.bool,
  isExpanded: ReactPropTypes.bool,
  hierarchical: ReactPropTypes.bool,
  extraData: ReactPropTypes.object,
  isGridDataDirty: ReactPropTypes.bool,
  gridPropertyValueChangedHandler: ReactPropTypes.func,
  gridPropertyValueCreatedHandler: ReactPropTypes.func,
  gridTagPropertyValueChangedHandler: ReactPropTypes.func,
  gridInstanceAddButtonClickedHandler: ReactPropTypes.func,
  gridPropertyInstanceCrossIconClickedHandler: ReactPropTypes.func,
  clearShouldFocusHandler: ReactPropTypes.func,
  gridPropertyLoseFocusHandler: ReactPropTypes.func,
  gridParentExpandToggledHandler: ReactPropTypes.func,
  gridPropertyFilterClickedHandler: ReactPropTypes.func,
  gridPropertyKeyEventHandler: ReactPropTypes.func,
  dateFieldFocusedHandler: ReactPropTypes.func,
  duplicateCode: ReactPropTypes.array,
  duplicateLabel: ReactPropTypes.array,
  resizableWidth: ReactPropTypes.string,
  tableContextId: ReactPropTypes.string
};
/**
 * @class GridPropertyView
 * @memberOf Views
 * @property {string} [propertyType] - To render compatible view(ex. for propertyType "native_drop_down" we render multiselect search view).
 * @property {string} [propertyId] - Id.
 * @property {object} [property] - Contains data for cell(ex. context: "gridTagColor", disableCross: false, disabled: false).
 * @property {string} [contentId] - Id of Content.
 * @property {string} [pathToRoot] - Contains parent Ids(ex.grandparent id, parent id );
 * @property {bool} [isParent] - It is set to true if content has a parent.
 * @property {bool} [isExpanded] - Contains true if content is expanded.
 * @property {bool} [hierarchical] - Contains true if it has children.
 * @property {object} [extraData] - Extra data
 * @property {bool} [isGridDataDirty] - Indicating grid data is dirty or not.
 */

// @CS.SafeComponent
class GridPropertyView extends React.Component {

  constructor(props) {
    super(props);
  }

  handleGridPropertyValueChanged = (sValue, oReferencedData) => {
    var sContentId = this.props.contentId;
    var sPropertyId = this.props.propertyId;
    var sPathToRoot = this.props.pathToRoot;
    if (CS.isFunction(this.props.gridPropertyValueChangedHandler)) {
      this.props.gridPropertyValueChangedHandler(sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData);
    } else {
      EventBus.dispatch(oEvents.GRID_PROPERTY_VALUE_CHANGED, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData, this.props.context);
    }
  };

  handleGridPropertyValueCreated = (sSearchText) => {
    var sContentId = this.props.contentId;
    var sPropertyId = this.props.propertyId;
    var sPathToRoot = this.props.pathToRoot;
    if (CS.isFunction(this.props.gridPropertyValueCreatedHandler)) {
      this.props.gridPropertyValueCreatedHandler(sPropertyId, sSearchText, sContentId, sPathToRoot);
    } else {
      EventBus.dispatch(oEvents.GRID_PROPERTY_VALUE_CREATED, sPropertyId, sSearchText, sContentId, sPathToRoot, this.props.context);
    }
  };

  handleGridTagPropertyValueChanged = (sTagId, aTagValueRelevanceData, oExtraData) => {
    let sContentId = this.props.contentId;
    if (CS.isFunction(this.props.gridTagPropertyValueChangedHandler)) {
      this.props.gridTagPropertyValueChangedHandler(sContentId, sTagId, aTagValueRelevanceData);
    } else {
      EventBus.dispatch(oEvents.GRID_TAG_PROPERTY_VALUE_CHANGED, sContentId, sTagId, aTagValueRelevanceData, this.props.context);
    }
  };

  handleGridInstanceAddButtonClicked = () => {
    let __props = this.props;
    if (CS.isFunction(__props.gridPropertyInstanceAddButtonClickedHandler)) {
      __props.gridPropertyInstanceAddButtonClickedHandler(__props.contentId, __props.propertyId);
    }
  };

  handleGridInstanceRemoveButtonClicked = (aSelectedItems) => {
    let __props = this.props;
    if (CS.isFunction(__props.gridPropertyInstanceCrossIconClickedHandler)) {
      __props.gridPropertyInstanceCrossIconClickedHandler(__props.contentId, __props.propertyId, aSelectedItems);
    }
  };

  // handleHTMLValueChanged: function (oData) {
  //   var sValue = oData.htmlValue;
  //   this.handleGridPropertyValueChanged(sValue);
  // },

  handleDateChanged =(oNull, oDate)=> {
    var sDate = oDate ? oDate.getTime() : "";
    this.handleGridPropertyValueChanged(sDate);
  }

  /** Function to handle Date & Time changed from DateTimePicker **/
  handleDateTimeChanged =(oDate)=> {
    let sDate = oDate ? oDate.getTime() : "";
    this.handleGridPropertyValueChanged(sDate);
  };

  /*shouldComponentUpdate: function (oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  },*/

  clearShouldFocus = () => {
    var sContentId = this.props.contentId;
    var sPathToRoot = this.props.pathToRoot;
    if (CS.isFunction(this.props.clearShouldFocusHandler)) {
      this.props.clearShouldFocusHandler(sContentId, sPathToRoot);
    } else {

      EventBus.dispatch(oEvents.GRID_PROPERTY_CLEAR_SHOULD_FOCUS, sContentId, sPathToRoot, this.props.context);

    }
  };

  handleGridPropertyLoseFocus = () => {
    var sContentId = this.props.contentId;
    var sPathToRoot = this.props.pathToRoot;
    var oMetadata = this.props.metadata;
    if (oMetadata && oMetadata.isCreated) {
      if (CS.isFunction(this.props.gridPropertyLoseFocusHandler)) {
        this.props.gridPropertyLoseFocusHandler("enter", sContentId, sPathToRoot);
      } else {

        EventBus.dispatch(oEvents.GRID_PROPERTY_KEY_EVENT, "enter", sContentId, sPathToRoot, this.props.context);

      }
    }
  };

  handleGridParentExpandToggled = () => {
    if (CS.isFunction(this.props.gridParentExpandToggledHandler)) {
      this.props.gridParentExpandToggledHandler(this.props.contentId);
    } else {
      EventBus.dispatch(oEvents.GRID_PROPERTY_PARENT_EXPAND_TOGGLED, this.props.contentId, this.props.context);
    }
  };

  handleGridPropertyFilterClicked = () => {
    if (CS.isFunction(this.props.gridPropertyFilterClickedHandler)) {
      this.props.gridPropertyFilterClickedHandler(this.props.contentId);
    } else {
      EventBus.dispatch(oEvents.GRID_PROPERTY_FILTER_CLICKED, this.props.contentId);
    }
  };

  handleGridPropertyKeyEvent = (sKey) => {
    var sContentId = this.props.contentId;
    var sPathToRoot = this.props.pathToRoot;
    if (CS.isFunction(this.props.gridPropertyKeyEventHandler)) {
      this.props.gridPropertyKeyEventHandler(sKey, sContentId, sPathToRoot);
    } else {
      EventBus.dispatch(oEvents.GRID_PROPERTY_KEY_EVENT, sKey, sContentId, sPathToRoot, this.props.context)
    }
  };

  handleChipsEditButtonClicked = (oProperty) => {
    EventBus.dispatch(oEvents.GRID_PROPERTY_VIEW_CHIPS_EDIT_BUTTON_CLICKED, oProperty, oProperty.id);
  };

  handleChipsRemoveItemButtonClicked = (oProperty, oRemovedItem) => {
    let aSelectedOptions = oProperty.model.selectedOptions;
    let aValues = [];
    CS.forEach(aSelectedOptions, (oData) => {
      if (oData.id !== oRemovedItem.id) {
        aValues.push(oData)
      }
    });
    this.handleGridPropertyValueChanged(aValues, oProperty.model.referencedData);
  };

  handleClassificationDialogButtonClicked = (sButtonId, sContext) => {
    let sEmbeddedContentId = this.props.contentId;
    let sPropertyId = this.props.propertyId;
    EventBus.dispatch(oEvents.GRID_PROPERTY_VIEW_CLASSIFICATION_DIALOG_BUTTON_CLICKED, sButtonId, sContext, sEmbeddedContentId,
        sPropertyId, this.props.tableContextId)

  };


  handleOnKeyDown =(oEvent)=> {

    var oMetadata = this.props.metadata;
    var sKeyCode = oEvent.keyCode;
    if (sKeyCode == 13) {
      this.handleGridPropertyKeyEvent("enter");
      oEvent.preventDefault();
    } else if (oMetadata && oMetadata.isCreated && sKeyCode == 9) {
       if (oEvent.shiftKey || oEvent.metaKey) {
         this.handleGridPropertyKeyEvent("shiftTab");
         oEvent.preventDefault();
       } else {
         this.handleGridPropertyKeyEvent("tab");
         oEvent.preventDefault();
       }
    }
  }

/*
  handleNativeDropdownChanged =(sValue)=> {
    this.handleGridPropertyValueChanged([sValue]);
  }
*/

/*
  getNativeDropdownView =(aItems, oSelectedItem, bIsDisabled)=> {

    var aOptions = CS.map(aItems, function (oItem) {
      return (<option>{CS.getLabelOrCode(oItem)}</option>);
    });

    return (<select className="gridPropertyNativeDropdown" disabled={bIsDisabled} value={oSelectedItem} onChange={this.handleNativeDropdownChanged}>{aOptions}</select>);
  }
*/

  handleDateFieldFocused = () => {
    if (CS.isFunction(this.props.dateFieldFocusedHandler)) {
      this.props.dateFieldFocusedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_DATE_FIELD_FOCUSED);
    }
  };

  getConcatenatedAttributeView = (oModel) => {
    let sSplitter = ViewUtils.getSplitter();
    let oMasterAttribute = oModel.masterAttribute;
    let sPlaceholder = oMasterAttribute.placeholder || (this.bIsDefaultPlaceholderVisible ? getTranslation().ENTER_HERE : this.props.customPlaceholder || "");
    let sRef = oModel.ref || "attr" + sSplitter + oModel.id + sSplitter + oMasterAttribute.id;
    let aExpression = oModel && !CS.isEmpty(oModel.expressionList) ? oModel.expressionList : oMasterAttribute.attributeConcatenatedList;
    let oConcatModel = new ContentConcatenatedAttributeViewModel(
        oModel.id,
        CS.getLabelOrCode(oModel),
        oModel.value,
        sPlaceholder,
        oModel.errorText || "",
        "",
        oModel.isDisabled,
        false,
        aExpression,
        {}
    );

    return (<ContentConcatenatedAttributeView ref={sRef}
                                              key={sRef}
                                              onChange={this.handleGridPropertyValueChanged}
                                              model={oConcatModel}/>);
  };

  getViewAccordingToType =()=> {
    var sPropertyId = this.props.propertyId;
    var oProperty = this.props.property;
    var sContentId = this.props.contentId;
    var sPathToRoot = this.props.pathToRoot;
    var oMetadata = this.props.metadata || {};
    var bIsDisabled;
    var iPrecision = 0;
    var sRendererType = this.props.propertyType;
    let bShowCopyToClipboardButton = false;
    let oReferencedAttribute = this.props.referencedAttribute;

    if (oProperty.attributeVariantContext && oProperty.variantInstanceId) {
      let oContentAttributeElementViewModel = new ContentAttributeElementViewModel(oReferencedAttribute.id, CS.getLabelOrCode(oReferencedAttribute), oProperty.value, oProperty.isDisabled, "", "", oReferencedAttribute, oProperty);
      return (
          <ContentAttributeElementView model={oContentAttributeElementViewModel}/>
      );
    }

    if (sRendererType == oGridViewPropertyTypes.FLEXIBLE) {
      sRendererType = oProperty.rendererType; //if the specified type in the skeleton is FLEXIBLE, we will take the type from the data
    }

    switch (sRendererType) {

      default:
      case oGridViewPropertyTypes.TEXT:
        var sClassName = "";
        var fKeyDownHandler = null;
        var bShouldFocus = false;
        let bShowTooltip = false;
        if (this.props.hierarchical && (sPropertyId == "label" || sPropertyId == "defaultLanguage")) {
          fKeyDownHandler = this.handleOnKeyDown;
          bShouldFocus = !!oMetadata.shouldFocus;
          if (sPathToRoot && sContentId != sPathToRoot) {
            sClassName = "childField ";
          }
        }
        var bIsMultiLine = (oProperty.bIsMultiLine != false);
        bIsDisabled = !!oProperty.isDisabled;
        bShowTooltip = oProperty.showTooltip || bIsDisabled;
        bShowCopyToClipboardButton = (this.props.propertyId === 'code');

        return (
            <CustomTextFieldView className={sClassName}
                                 value={oProperty.value}
                                 shouldFocus={bShouldFocus}
                                 isDisabled={bIsDisabled}
                                 isMultiLine={bIsMultiLine}
                                 onBlur={this.handleGridPropertyValueChanged}
                                 onKeyDown={fKeyDownHandler}
                                 onLoseFocus={this.handleGridPropertyLoseFocus}
                                 onForceFocus={this.clearShouldFocus}
                                 showTooltip={bShowTooltip}
                                 showCopyToClipboardButton={bShowCopyToClipboardButton}
                                 hintText={oProperty.placeholder}/>
        );

      case oGridViewPropertyTypes.COLLECTION_PLANNER_HEADER:
        var sClassName = "";
        var sFilterClassName = oProperty.isFilterActive ? "filterButton active " : "filterButton ";

        return (
            <div className="collectionPlannerHeaderView">
              <div className="collectionPlannerTextFieldView">
                <CustomTextFieldView className={sClassName}
                    value={oProperty.value}
                    isDisabled={true}
                    isMultiLine={false}
                    showTooltip={false}
                />
              </div>
              <div className="collectionPlannerToolBar">
                <div className="gridCountView">{oProperty.totalLinkedEntitiesCount}</div>
                <div className={sFilterClassName} onClick={this.handleGridPropertyFilterClicked}></div>
              </div>
            </div>
        );

      case oGridViewPropertyTypes.NUMBER:
        iPrecision = oProperty.precision || 0;
        bIsDisabled = !!oProperty.isDisabled;
        let sPropertyValue = oProperty.value;
        let bDisableNumberLocaleFormatting = oProperty.disableNumberLocaleFormatting;

        let sMinValue = null;
        let sMaxValue = null;
        if (sPropertyId === "precision") {
          sMinValue = 0;
          sMaxValue = 9;
          sPropertyValue = sPropertyValue || 0;
        }

        return (
            <NumberLocaleView value={sPropertyValue}
                              isDisabled={bIsDisabled}
                              onBlur={this.handleGridPropertyValueChanged}
                              precision={iPrecision}
                              minValue={sMinValue}
                              maxValue={sMaxValue}
                              disableNumberLocaleFormatting={bDisableNumberLocaleFormatting}/>
        );

      case oGridViewPropertyTypes.HTML:
        /* this FroalaWrapperView view will return an object :
         {
           htmlValue: "",
           textValue: ""
         }
        */
        let sHTMLValue = oProperty.value;
        var aToolbarIcons = oProperty.toolbarIcons || MockDataIdsForFroalaView;
        bIsDisabled = !!oProperty.isDisabled;
        var sActiveFroalaId = bIsDisabled ? "" : this.props.propertyId;
        let bFixedToolbar = !!oProperty.fixedToolbar;
        return (
            <div className={"froalaWrapper"} style={{maxHeight: '200px',overflow: 'auto',scrollbarWidth: 'thin'}}>
              <FroalaWrapperView content={sHTMLValue}
                                 dataId={this.props.propertyId}
                                 activeFroalaId={sActiveFroalaId}
                                 handler={this.handleGridPropertyValueChanged}
                                 toolbarIcons={aToolbarIcons}
                                 fixedToolbar={bFixedToolbar}/>
              </div>
        );

      case oGridViewPropertyTypes.MEASUREMENT:
        var sDefaultUnit = oProperty.defaultUnit;
        var sValue = oProperty.value;
        iPrecision = oProperty.precision;
        var sAttributeType = oProperty.type;
        let oMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
        var aConverterList = oMeasurementMetricAndImperial[sAttributeType];
        CS.forEach(aConverterList,function (oConverterItem) {
          oConverterItem.unitToDisplay = oConverterItem.unitToDisplay +' (' + CS.getLabelOrCode(oConverterItem) +')'
        });
        var oBaseUnit = CS.find(aConverterList, {isBase: true});
        var sBaseUnit = oBaseUnit ? oBaseUnit.unit : sDefaultUnit;
        bIsDisabled = oProperty.isDisabled || false;
        var bConverterVisibility = (oProperty.converterVisibility != false); //default value should be true, take false only when converterVisibility = false
        var oProperties = {};
        if (ViewUtils.isMeasurementAttributeTypeCustom(oProperty.type)) {
          oProperties.defaultUnitAsHTML = oProperty.defaultUnitAsHTML;
        }
        // isValueDisabled: only value disabled not unit dropdown.
        oProperty.isValueDisabled && (oProperties.isValueDisabled = oProperty.isValueDisabled);

        if(oProperty.hasOwnProperty("disableValueConversionByDefaultUnit")) {
          oProperties.disableValueChangeByDefaultUnit = oProperty.disableValueConversionByDefaultUnit;
        }
        if(oProperty.hasOwnProperty("disableDefaultUnit")) {
          oProperties.disableDefaultUnit = oProperty.disableDefaultUnit;
        }
        oProperties.disableNumberLocaleFormatting = oProperty.disableNumberLocaleFormatting;
        var oModel = new ContentMeasurementMetricsViewModel(sPropertyId, "", sValue, sDefaultUnit, sBaseUnit, "", "", "",
            bIsDisabled, bConverterVisibility, aConverterList, oProperties, iPrecision, sAttributeType);
        return (
            <ContentMeasurementMetricsView model={oModel} onBlur={this.handleGridPropertyValueChanged}/>
        );

      /*case oGridViewPropertyTypes.NATIVE_DROP_DOWN:
        var aItems = oProperty.items || [];
        var oSelectedItem = oProperty.value ? oProperty.value[0] : "";
        var bMandatory = oProperty.disableCross;
        bIsDisabled = !!oProperty.isDisabled || oProperty.disabled || false;

        return <NativeDropdownView items={aItems}
                                   value={oSelectedItem}
                                   disabled={bIsDisabled}
                                   mandatory={bMandatory}
                                   onValueChange={this.handleNativeDropdownChanged}/>*/

      case oGridViewPropertyTypes.NATIVE_DROP_DOWN:
      case oGridViewPropertyTypes.DROP_DOWN:
        var aItems = oProperty.items;
        var aSelectedItems = oProperty.value;
        var bSingleSelect = oProperty.singleSelect;
        var bWithCheckbox = oProperty.checkBox;
        var bDisableCross = oProperty.disableCross;
        let sContext = oProperty.context;
        let bIsLoadMoreEnabled = oProperty.isLoadMoreEnabled;
        let sSearchText = oProperty.searchText;
        let bShowCreateButton = oProperty.showCreateButton;
        var bShowColor = oProperty.showColor || false;
        bIsDisabled = !!oProperty.isDisabled || oProperty.disabled || false;
        return (
            <MultiSelectSearchView
                key={this.props.resizableWidth}
                model={null}
                onApply={this.handleGridPropertyValueChanged}
                items={aItems}
                selectedItems={aSelectedItems}
                isMultiSelect={!bSingleSelect}
                checkbox={bWithCheckbox}
                disabled={bIsDisabled}
                cannotRemove={bDisableCross}
                showColor={bShowColor}
                context={sContext}
                isLoadMoreEnabled={bIsLoadMoreEnabled}
                searchText={sSearchText}
                showCreateButton={bShowCreateButton}
                clearSearchOnPopoverClose={true}
                adjustRowHeight={this.props.adjustRowHeight}
            />
        );

      case oGridViewPropertyTypes.LAZY_MSS:
        return (
            <LazyMultiSelectSearchView
                isMultiSelect={!oProperty.singleSelect}
                context={oProperty.context}
                disabled={!!oProperty.isDisabled || oProperty.disabled || false}
                selectedItems={oProperty.value}
                onApply={this.handleGridPropertyValueChanged}
                showColor={oProperty.showColor || false}
                cannotRemove={oProperty.disableCross}
                showSelectedInDropdown={true}
                onPopOverOpen={oProperty.onPopOverOpen}
                referencedData={oProperty.referencedData}
                requestResponseInfo={oProperty.requestResponseInfo}
                showCreateButton={oProperty.showCreateButton ? true: false}
                onCreateHandler={this.handleGridPropertyValueCreated}
                adjustRowHeight={this.props.adjustRowHeight}
            />
        );

      case oGridViewPropertyTypes.IMAGE:
        /**Do not use isEmpty check here**/
        let bLimitImageSize = CS.isBoolean(oProperty.limitImageSize) ? oProperty.limitImageSize : true;
        return (
            <GridImagePropertyView icon={oProperty.value}
                                   propertyId={this.props.propertyId}
                                   contentId={this.props.contentId}
                                   limitImageSize={bLimitImageSize}
                                   pathToRoot={this.props.pathToRoot}
                                   isDisabled={this.props.property.isDisabled}
                                   isIconImageType={this.props.property.isIconImageType}
                                   defaultImageClass={this.props.property.defaultImageClass}
                                   hideIconUpload={this.props.hideIconUpload}
                                   context={this.props.context}
                                   iconKey={oProperty.iconKey}
            />
        );

      case oGridViewPropertyTypes.YES_NO:
        bIsDisabled = !!oProperty.isDisabled;
        return (
            <GridYesNoPropertyView value={oProperty.value} onChange={this.handleGridPropertyValueChanged} isDisabled={bIsDisabled}/>
        );

      case oGridViewPropertyTypes.TICK:
        bIsDisabled = !!oProperty.isDisabled;
        return (
            <GridTickPropertyView value={oProperty.value} onChange={this.handleGridPropertyValueChanged} isDisabled={bIsDisabled}/>
        );

      case oGridViewPropertyTypes.DATE:
        var oDate = +oProperty.value ? new Date(+oProperty.value) : null;
        bIsDisabled = !!oProperty.isDisabled;
        return (
            <CustomDatePickerView value={oDate}
                                  className="datePickerCustom"
                                  disabled={bIsDisabled}
                                  onFocus={this.handleDateFieldFocused}
                                  onChange={this.handleDateChanged}/>
        );

      case oGridViewPropertyTypes.DATETIME:
        let oDateTime = +oProperty.value ? new Date(+oProperty.value) : null;
        bIsDisabled = !!oProperty.isDisabled;
        let bHideRemoveButton = oProperty.hideRemoveButton ? true : false;
        return (
          <CustomeDateTimePickerView value={oDateTime}
                                     disabled={bIsDisabled}
                                     hideRemoveButton={bHideRemoveButton}
                                     onChange={this.handleDateTimeChanged}/>
        );

      case oGridViewPropertyTypes.CONTEXT_TAGS:
        return (
            <GridContextualAttributePropertyView attributeTags={oProperty.value} onChange={this.handleGridPropertyValueChanged}/>
        );

/*
      case oGridViewPropertyTypes.VARIANT_TAGS:
        var oVariantSectionViewData = oProperty.variantSectionViewData;
        var oProperties = {variantId: oVariantSectionViewData.activeVariant.id};

        return (
            <VariantTagSummaryView tagInstances={oProperty.tagsData}
                                   isEditViewOpen={oProperty.isEditViewOpen}
                                   masterTagGroups={oVariantSectionViewData.selectedContext.tags}
                                   renderInGrid={true}
                                   canEdit={oVariantSectionViewData.selectedContext.canEdit}
                                   properties={oProperties}
            />
        );
*/

      case oGridViewPropertyTypes.CONCATENATED_FORMULA:
        let oRequestResponseInfoForAttributes = {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.ATTRIBUTES,
          types: [],
          typesToExclude: [AttributeTypeDictionary.CONCATENATED]
        };

        return (
            <GridConcatenatedAttributePropertyView
                attributeConcatenatedList={oProperty.value}
                referencedData={oProperty.referencedData}
                allowedEntities={[ConcatenatedEntityTypes.html, ConcatenatedEntityTypes.attribute, ConcatenatedEntityTypes.tag]}
                requestResponseInfoForAttributes={oRequestResponseInfoForAttributes}
                onChange={this.handleGridPropertyValueChanged}/>
        );

      case oGridViewPropertyTypes.CALCULATED_FORMULA:
        let oPaginationData = oProperty.paginationData;
        if (CS.isEmpty(oPaginationData)) {
          oPaginationData = {
            from: 0,
            size: 20,
            sortBy: "label",
            sortOrder: "asc",
            searchColumn: "label",
            searchText: "",
            shouldAllowSelf: false,
            isFirstCall: true
          };
        }
        return (
            <GridCalculatedAttributePropertyView calculatedAttributeId={oProperty.value.calculatedAttributeId}
                                                 attributeOperatorList={oProperty.value.attributeOperatorList}
                                                 calculatedAttributeType={oProperty.value.calculatedAttributeType}
                                                 calculatedAttributeUnit={oProperty.value.calculatedAttributeUnit}
                                                 calculatedAttributeUnitAsHTML={oProperty.value.calculatedAttributeUnitAsHTML}
                                                 unitAttributesList={oProperty.unitAttributesList}
                                                 measurementUnitsData={oProperty.measurementUnitsData}
                                                 paginationData={oPaginationData}
                                                 extraData={this.props.extraData}
                                                 isGridDataDirty={this.props.isGridDataDirty}
                                                 allowedAttributes={oProperty.allowedAttributes}
                                                 onChange={this.handleGridPropertyValueChanged}/>
        );

      case oGridViewPropertyTypes.TAG:
        let oExtraData = this.props.extraData;
        let oMasterTag = !CS.isEmpty(oExtraData) ? oExtraData.referencedTag : {};
        let oTag =oProperty.tag;
        let oTags = {
          [oTag.id]: CS.cloneDeep(oTag)
        };
        let oTagGroupModel = ViewUtils.getTagGroupModels(oMasterTag, {tags: oTags}, {}, "gridViewTag");
        bIsDisabled = !!oProperty.isDisabled;
        oExtraData.contentId = this.props.contentId;

        let sGridTagViewContainerClassName = bIsDisabled ? "gridTagView disabled" : "gridTagView";
        let oTagGroupModelProperties = oTagGroupModel.tagGroupModel.properties;
        return (
            <div className={sGridTagViewContainerClassName}>
              <TagGroupView
                  key={this.props.resizableWidth}
                  tagGroupModel={oTagGroupModel.tagGroupModel}
                  tagRanges={oTagGroupModelProperties.tagRanges}
                  tagValues={oTagGroupModel.tagValues}
                  disabled={bIsDisabled}
                  singleSelect={oTagGroupModelProperties.singleSelect}
                  showLabel={false}
                  masterTagList={[oMasterTag]}
                  displayInGrid={true}
                  onApply={this.handleGridTagPropertyValueChanged}
                  extraData={oExtraData}
                  customRequestObject={oProperty.customRequestObject}
                  adjustRowHeight={this.props.adjustRowHeight}
                  showDefaultIcon ={oProperty.showDefaultIcon}
              />
            </div>);

      case oGridViewPropertyTypes.DROP_DOWN_WITH_ADDITIONAL_LIST:
        return (
            <GridMSSWithAdditionalList
                property={oProperty}
                onApply={this.handleGridPropertyValueChanged}
                context={this.props.context}
            />
        );

      case oGridViewPropertyTypes.DISABLED_WITH_EDIT_BUTTON:
        return (<div className="disabledWithEditButton">
          <div className="editButton" onClick={this.handleGridPropertyValueChanged.bind(this, "")}>
            <div className="edit"></div>
          </div>
        </div>);

      case oGridViewPropertyTypes.TAXONOMY:

          let oSmallTaxonomyViewModel = oProperty.model;
          return (
              <div className='gridPropertyTaxonomyView'>
                <SmallTaxonomyView model={oSmallTaxonomyViewModel}
                                   isDisabled={oProperty.isTaxonomyDisabled}
                                   context="process"
                                   showAllComponents={oProperty.showAllComponents}
                                   localSearch={true}/>
              </div>);

      case oGridViewPropertyTypes.MOBILE_NUMBER:
        let sDefaultValue = oProperty.value;
        return (
            <CustomMobileNumberView defaultValue={sDefaultValue}
                                    onBlurHandler={this.handleGridPropertyValueChanged}
                                    disableUnderline={true}/>
        );

      case oGridViewPropertyTypes.LINKED_INSTANCE:
        bIsDisabled = oProperty.isDisabled || false;
        return (
            <GridLinkedInstanceSummaryView model={null}
                                           items={oProperty.items}
                                           cannotAdd={oProperty.cannotAdd}
                                           cannotRemove={oProperty.cannotRemove}
                                           isDisabled={bIsDisabled}
                                           selectedItems={oProperty.selectedItems || oProperty.values}
                                           addButtonHandler={this.handleGridInstanceAddButtonClicked}
                                           onApply={this.handleGridInstanceRemoveButtonClicked}
            />
        );

      case oGridViewPropertyTypes.CHIPS:
        return (
            <ChipsView items={[oProperty]}/>
        );

      case oGridViewPropertyTypes.CONCATENATED:
        return this.getConcatenatedAttributeView(oProperty);


      // default:
      //   return (
      //       <CustomTextFieldView value={oProperty.value} isMultiLine={true} onBlur={this.handleGridPropertyValueChanged}/>
      //   );

      case oGridViewPropertyTypes.BAR:
        return(<StatusBar completeness = {oProperty.value}/>)

    case oGridViewPropertyTypes.CHIPS_WITH_MORE_OPTION:
      let oMultiClassificationDialogData = oProperty.model.multiClassificationViewData;

      return (
          <Fragment>
            <div className="chipsViewWrapper">
              <GroupMSSChipsView
                  selectedOptions={oProperty.model.selectedOptions}
                  referencedData={oProperty.model.referencedData}
                  handleRemove={this.handleChipsRemoveItemButtonClicked.bind(this, oProperty)}
              />
            </div>
            <MultiClassificationSectionView
                multiClassificationViewData={oMultiClassificationDialogData}
                context={oProperty.model.context}
                dialogContext={oProperty.model.dialogContext}
                sTaxonomyId={oProperty.id}
                doNotShowClassification={oProperty.model.doNotShowClassification}
                dialogButtonHandler={this.handleClassificationDialogButtonClicked}
            />
            <div className = "editMultiClassificationIcon"
                 onClick={this.handleChipsEditButtonClicked.bind(this, oProperty)}/>
          </Fragment>
      )


    }
  };

  render() {

    var oPropertyView = this.getViewAccordingToType();

    var sGridPropertyClass = "gridPropertyView ";
    var oExpandButtonView = null;
    if (this.props.isParent) {
      var sExpandClass = "expandButton ";
      sExpandClass += this.props.isExpanded ? "expanded " : "collapsed ";
      oExpandButtonView = (<div className={sExpandClass} onClick={this.handleGridParentExpandToggled}></div>)
    }

    if (this.props.propertyId === "newCode") {
      let duplicateCodeClass = this.props.duplicateCode;
      if (duplicateCodeClass.includes(this.props.property.value)) {
        sGridPropertyClass = sGridPropertyClass + " duplicateCode ";
      }
    } else if (this.props.propertyId === "newLabel") {
      let duplicateLabelClass = this.props.duplicateLabel || [];
      if (duplicateLabelClass.includes(this.props.property.value)) {
        sGridPropertyClass = sGridPropertyClass + " duplicateCode ";
      }
    } else if (this.props.propertyId === "activation") {
      let aGridWFValidationErrorList = this.props.gridWFValidationErrorList;
      if (CS.isNotEmpty(aGridWFValidationErrorList)) {
        if (aGridWFValidationErrorList.includes(this.props.contentId)) {
          sGridPropertyClass = sGridPropertyClass + " duplicateCode ";
        }
      }
    }
    if (this.props.hierarchical && (this.props.propertyId == "label" || this.props.isExpandable)) { //todo: hardcoded, need to change.
      sGridPropertyClass += "rowLabel ";
    }

    var oProperty = this.props.property;
    if (oProperty && (oProperty.isDisabled || oProperty.isValueDisabled) && !oProperty.isHideDisabledMask) {
      sGridPropertyClass += "gridElementDisabledMask ";
    }

    return (
        <div className={sGridPropertyClass}>
          {oExpandButtonView}
          {oPropertyView}
        </div>
    );
  }

}

GridPropertyView.propTypes = oPropTypes;

export const view = GridPropertyView;
export const events = oEvents;
