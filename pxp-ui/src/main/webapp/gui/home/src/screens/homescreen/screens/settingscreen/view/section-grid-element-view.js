
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import {blue} from '@material-ui/core/colors';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as DragView } from './../../../../../viewlibraries/dragndropview/drag-view.js';
import DragViewModel from './../../../../../viewlibraries/dragndropview/model/drag-view-model';
import { view as CustomDatePicker } from '../../../../../viewlibraries/customdatepickerview/customdatepickerview.js';
import { view as ContentMeasurementMetricsView } from '../../../../../viewlibraries/measurementmetricview/content-measurement-metrics-view.js';
import { view as RelationshipSectionView } from './relationship-section-view';
import ContentMeasurementMetricsViewModel from '../../../../../viewlibraries/measurementmetricview/model/content-measurement-metrics-view-model';
import MockDataForMeasurementMetricAndImperial from './../../../../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import ViewUtils from '../view/utils/view-utils';
import {versionCouplingTypes, variantCouplingTypes} from '../../../../../commonmodule/tack/version-variant-coupling-types';
import AttributeTypeDictionary from './../../../../../commonmodule/tack/attribute-type-dictionary-new';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import MockDataForTagTypes from '../../../../../commonmodule/tack/mock-data-for-tag-types';
import SettingScreenConstant from './../store/model/setting-screen-constants';
const  ElementPropertyConstants = SettingScreenConstant.elementProperty;

const oEvents = {
  VISUAL_ELEMENT_BLOCKER_CLICKED: "visual_element_blocker_clicked",
  VISUAL_ELEMENT_ATTRIBUTE_TILE_VALUE_CHANGED: "visual_element_attribute_tile_value_changed",
  VISUAL_ELEMENT_TAG_CHECK_ALL_CHANGED: "visual_element_tag_check_all_changed",
  VISUAL_ELEMENT_FILTERABLE_OR_SORTABLE_CHECK_ALL_CHANGED: "visual_element_filterable_or_sortable_check_all_changed",
};

const oPropTypes = {
  element: ReactPropTypes.object,
  style: ReactPropTypes.object,
  activeClass: ReactPropTypes.object,
  visualAttributeDragStatus: ReactPropTypes.object,
  sectionId: ReactPropTypes.string,
  masterEntitiesForSection: ReactPropTypes.object,
  tagList: ReactPropTypes.array,
  klassesForRelationship: ReactPropTypes.object
};

// @CS.SafeComponent
class SectionGridElementView extends React.Component {
  constructor(props) {
    super(props);

    this.typeAttributeDefaultValue = React.createRef();
    this.attributeDefaultValue = React.createRef();
    this.numberOfItemsAllowed = React.createRef();
    this.numberOfVersionsAllowed = React.createRef();
  }

  componentDidMount() {
    this.updateInputValues();
  }

  componentDidUpdate() {
    this.updateInputValues();
  }

  updateInputValues = () => {
    var oVisualElement = this.props.element;
    if(oVisualElement.type == "attribute"){
      if(ViewUtils.isAttributeTypeType(oVisualElement.attribute.type)){ // type attribute
        this.typeAttributeDefaultValue.current && (this.typeAttributeDefaultValue.current.value = oVisualElement.defaultValue);
      } else if (ViewUtils.isAttributeTypeDate(oVisualElement.attribute.type)) { // date attribute
      } else {
        this.attributeDefaultValue.current && (this.attributeDefaultValue.current.value = oVisualElement.defaultValue);
      }

      if(ViewUtils.isAttributeTypeCoverflow(oVisualElement.attribute.type)){
        this.numberOfItemsAllowed.current.value = oVisualElement.numberOfItemsAllowed;
      }

      if(this.numberOfVersionsAllowed.current){
        this.numberOfVersionsAllowed.current.value = oVisualElement.numberOfVersionsAllowed;
      }
    } else if(oVisualElement.type == "tag" || oVisualElement.type == "role"){
      if(this.numberOfVersionsAllowed.current){
        this.numberOfVersionsAllowed.current.value = oVisualElement.numberOfVersionsAllowed;
      }
    }
  };

  handleAttributeTileValueChanged = (sKey, oEvent) => {
    var __props = this.props;
    var oDomElement = oEvent.target;
    var sValue = "";
    var oVisualElement = this.props.element;

    if(oDomElement.type == 'checkbox'){
      sValue = oEvent.target.checked;
    } else if(oDomElement.type == 'select-one') {
      sValue = oEvent.target.selectedOptions[0].value;
    } else {
      sValue = oEvent.target.value;
      if(sValue < 0) {
        sValue = 0;
      }
    }

    if(sValue != oVisualElement[sKey]) {

      var oInfo = {
        sectionId: __props.sectionId,
        elementId: __props.element.id,
        propertyName: sKey,
        value: sValue
      };

      EventBus.dispatch(oEvents.VISUAL_ELEMENT_ATTRIBUTE_TILE_VALUE_CHANGED, this, oInfo)
    }
  };

  handleDateChanged = (sKey, oNull, sValue) => {
    var __props = this.props;
    var oVisualElement = __props.element;
    sValue = sValue ? new Date(sValue).getTime() : '';
    if(sValue != oVisualElement[sKey]) {
      var oInfo = {
        sectionId: __props.sectionId,
        elementId: __props.element.id,
        propertyName: sKey,
        value: sValue
      };

      EventBus.dispatch(oEvents.VISUAL_ELEMENT_ATTRIBUTE_TILE_VALUE_CHANGED, this, oInfo)
    }
  };

  handleMeasurementValueChanged = (sKey, sValue) => {
    var __props = this.props;
    var oVisualElement = __props.element;
    if(sValue != oVisualElement[sKey]) {
      var oInfo = {
        sectionId: __props.sectionId,
        elementId: __props.element.id,
        propertyName: sKey,
        value: sValue
      };
      EventBus.dispatch(oEvents.VISUAL_ELEMENT_ATTRIBUTE_TILE_VALUE_CHANGED, this, oInfo)
    }
  };

  getHeaderView = (oDragViewModel, sTileIconClass, sLabel) => {
    var oElement = this.props.element;
    var bIsInherited = oElement.isInheritedUI;
    var bIsCutOff = oElement.isCutoffUI;

    var sLockViewClassName = "visualElementLock ";
    sLockViewClassName += bIsInherited ? "locked " : "";
    sLockViewClassName += bIsCutOff ? "unlocked " : "";
    var oLockView = (<div className={sLockViewClassName}></div>);

    return (
        <DragView model={oDragViewModel}>
          <div className="tileHeader">
            <div className={sTileIconClass}></div>
            <div className="tileValue" title={sLabel}>{sLabel}</div>
            {oLockView}
          </div>
        </DragView>
    );
  };

  handleBlockerClicked = (oEvent) => {
    var __props = this.props;

    var oInfo = {
      sectionId: __props.sectionId,
      elementId: __props.element.id
    };

    EventBus.dispatch(oEvents.VISUAL_ELEMENT_BLOCKER_CLICKED, this, oEvent, oInfo);
  };

  getElementBlockerView = () => {
    var oElement = this.props.element;
    var sElementBlocker = "elementBlocker ";

    sElementBlocker += oElement.isInheritedUI ? "elementBlockerActive " : "";

    return (<div className={sElementBlocker} onClick={this.handleBlockerClicked}></div>);
  };

  getPlaceHolderViewForRelationship = (oDragViewModel) => {
    var oVisualElement = this.props.element;
    var sLabel = oVisualElement.relationship.label;
    var sTileIconClass = "tileIcon tileIconRelationship";
    var oTileHeaderDOM = this.getHeaderView(oDragViewModel, sTileIconClass, sLabel);
    var oKlassesForRelationship = this.props.klassesForRelationship;
    var oSectionElement = oVisualElement.relationshipSide;

    var oBothSides = oVisualElement.relationship;
    oSectionElement.sideNo = (oBothSides.side2.id == oSectionElement.id) ? "2" : "1";

    return (
        <div className="sectionTileContainer">
          {oTileHeaderDOM}
          <RelationshipSectionView relationshipSideElement={oSectionElement}
                                   klassesForRelationship={oKlassesForRelationship}/>
        </div>
    );

  };

  getPlaceHolderViewForTag = (sType, sTypeKey, oDragViewModel) => {
    var oVisualElement = this.props.element;
    var sLabel = sTypeKey == "tag"  ? oVisualElement.tag.label : oVisualElement.role.label;
    var sTileIconClass = "tileIcon ";
    sTileIconClass += sTypeKey == "tag" ? "tileIconTag " : "tileIconRole ";

    var oTagListDom = null;
    var oTagTypeDom = sTypeKey == "tag" ? this.getTagTypeDom(oVisualElement) : null;

    var oTileHeaderDOM = this.getHeaderView(oDragViewModel, sTileIconClass, sLabel);

    var oVersionCouplingViewDOM = this.getVersionCouplingDom();
    var oVariantCouplingViewDOM = this.getVariantCouplingDom();
    var oBlockerDom = this.getElementBlockerView();
    var oCheckBoxDom = this.getFilterableSortableView();


    return (
        <div className="sectionTileContainer">
          {oTileHeaderDOM}
          <div className="sectionTileForm">
            <div className="sectionTileFieldWrapper">
              <div className="sectionTileFieldKey">{getTranslation().VERSION_ALLOWED}</div>
              <input type="number" ref={this.numberOfVersionsAllowed}
                     min="0"
                     className="sectionTileFieldValue"
                     onChange={this.handleAttributeTileValueChanged.bind(this, "numberOfVersionsAllowed")}
              />
            </div>
            {oVersionCouplingViewDOM}
            {oVariantCouplingViewDOM}
            {oTagTypeDom}
          </div>
          {oCheckBoxDom}
          {oTagListDom}
          {oBlockerDom}
        </div>
    );
  };

  getCustomMaterialStyles = () => {

    return {
      inputStyles: {
        "fontSize": "14px"
      },

      floatingLabelStyles: {
        "fontSize": "14px",
        "color": "#555"
      },

      underlineStyle: {
        color: '#ccc',
        borderWidth: '0px'
      },

      underlineFocusStyle: {
        color: blue["300"],
        borderWidth: '0px'
      }
    }
  };

  getDefaultValueView = (bIsDisabled) => {
    var oVisualElement = this.props.element;
    var sLabel = oVisualElement.attribute.label;
    var sAttributeType = oVisualElement.attribute.type;
    var sValue = oVisualElement.defaultValue;

    if(ViewUtils.isAttributeTypeDate(sAttributeType)) {

      sValue = sValue ? +sValue ? new Date(+sValue) : null : null;
      var oStyle = {width: "100%"};
      var oCustomStyle = this.getCustomMaterialStyles();
      if (CS.isNaN(Date.parse(sValue))) {
        sValue = null;
      }

      return (
          <CustomDatePicker
              value={sValue}
              key={"attributeDefaultValue"}
              className="datePickerCustom"
              onChange={this.handleDateChanged.bind(this, "defaultValue")}
              style={oStyle}
              disabled={!!bIsDisabled}
              underlineStyle={oCustomStyle.underlineStyle}
              underlineFocusStyle={oCustomStyle.underlineFocusStyle}
          />
      )
    }

    if(ViewUtils.isAttributeTypeMeasurement(sAttributeType)) {

      var _this=this;
      var sSplitter = ViewUtils.getSplitter();
      var sType = sAttributeType;
      var sAttrValue =sValue;
      var oMasterAttribute = oVisualElement.attribute;
      var sRef = "attr" + sSplitter + oMasterAttribute.id + sSplitter + oMasterAttribute.sContext;
      var sPlaceholder = "";
      var sDescription = "";
      var bDisabled = false;
      var sDefaultUnit = oMasterAttribute.defaultUnit;
      let oMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
      var aConverterList = oMeasurementMetricAndImperial[sType];
      var oBaseUnit = CS.find(aConverterList, {isBase: true});
      var sBaseUnit = oBaseUnit ? oBaseUnit.unit : sDefaultUnit;
      var oProperties = {};
      oProperties.baseUnit = (CS.find(oMeasurementMetricAndImperial[sType], {isBase: true}))["unit"];
      oProperties.attributeType = sType;
      var iPrecision = Number(oMasterAttribute.precision);
      var oModel = new ContentMeasurementMetricsViewModel(
          oMasterAttribute.id,
          sLabel,
          sAttrValue,
          sDefaultUnit,
          sBaseUnit,
          sPlaceholder,
          "",
          sDescription,
          bDisabled,
          false,
          aConverterList,
          oProperties,
          iPrecision
      );
      return (<ContentMeasurementMetricsView ref={sRef}
                                             key={sRef}
                                             onBlur={_this.handleMeasurementValueChanged.bind(this,"defaultValue")}
                                             model={oModel}/>);
    }


    var defaultInputField = 'text';
    if (ViewUtils.isAttributeTypeNumber(sAttributeType)) {
      defaultInputField = 'number';
    }

    return (
        <input type={defaultInputField} ref={this.attributeDefaultValue} className="sectionTileFieldValue"
               onBlur={this.handleAttributeTileValueChanged.bind(this, "defaultValue")} disabled={bIsDisabled}/>
    );

  };

  createSelectOptionList = (aList) => {
    var aOptions = [];
    CS.forEach(aList, function(oOption){
      aOptions.push(<option value={oOption.id}>{oOption.label}</option>)
    });
    return aOptions;
  };

  getVersionCouplingDom = () => {
    var oVisualElement = this.props.element;
    let oVersionCouplingTypes = new versionCouplingTypes();
    var aVersionOption = this.createSelectOptionList(oVersionCouplingTypes);
    return (
        <div className="sectionTileFieldWrapper">
          <div className="sectionTileFieldKey">{getTranslation().VERSION_COUPLING}</div>
          <select className="sectionTileFieldValue"
                  value={oVisualElement.versionCouplingType}
                  onChange={this.handleAttributeTileValueChanged.bind(this, "versionCouplingType")}>
            {aVersionOption}
          </select>
        </div>
    );
  };

  getVariantCouplingDom = () => {
    var oVisualElement = this.props.element;
    let oVariantCouplingTypes = new variantCouplingTypes();
    var aVariantOption = this.createSelectOptionList(oVariantCouplingTypes);
    return(
        <div className="sectionTileFieldWrapper">
          <div className="sectionTileFieldKey">{getTranslation().VARIANT_COUPLING}</div>
          <select className="sectionTileFieldValue"
                  value={oVisualElement.variantCouplingType}
                  onChange={this.handleAttributeTileValueChanged.bind(this, "variantCouplingType")}>
            {aVariantOption}
          </select>
        </div>
    );
  };

  createSelectOptionListForTagTypes = (aList) => {
    var aOptions = [];
    CS.forEach(aList, function(oOption){
      if(oOption.id != TagTypeConstants.CUSTOM_TAG_TYPE){
        aOptions.push(<option value={oOption.id}>{CS.getLabelOrCode(oOption)}</option>)
      }
    });
    return aOptions;
  };

  getTagTypeDom = () => {
    var oVisualElement = this.props.element;
    let oMockDataForTagTypes = new MockDataForTagTypes();
    var aTagTypeOption = this.createSelectOptionListForTagTypes(oMockDataForTagTypes);
    return(
        <div className="sectionTileFieldWrapper">
          <div className="sectionTileFieldKey">{getTranslation().TYPE}</div>
          <select className="sectionTileFieldValue"
                  value={oVisualElement.tagType}
                  onChange={this.handleAttributeTileValueChanged.bind(this, "tagType")}>
            {aTagTypeOption}
          </select>
        </div>
    );
  };

  getPlaceHolderViewForAttribute = (sType, oDragViewModel) => {
    var oVisualElement = this.props.element;
    var sLabel = oVisualElement.attribute.label;
    var sAttributeType = oVisualElement.attribute.type;
    var sAttributeId =  oVisualElement.attribute.id;
    var bIsImageCoverflowAttribute = ViewUtils.isAttributeTypeCoverflow(sAttributeType);
    var bIsTypeAttribute =  ViewUtils.isAttributeTypeType(sAttributeType);
    var oTagListDom = null;
    var oActiveClass = this.props.activeClass;
    var bIsDisabled = false;
    if (!oActiveClass.isStandard) {
      if (ViewUtils.isAttributeTypeName(oVisualElement.attribute.type)) {
        bIsDisabled = true;
      }
    }

    var sTileIconClass = "tileIcon tileIconAttribute ";
    var oCheckBoxDom = this.getFilterableSortableView();

    var oVersionsAllowedDom = (bIsTypeAttribute || bIsImageCoverflowAttribute) ? null :
        (<div className="sectionTileFieldWrapper">
          <div className="sectionTileFieldKey">{getTranslation().VERSION_ALLOWED}</div>
          <input type="number" ref={this.numberOfVersionsAllowed}
                 min="0"
                 className="sectionTileFieldValue"
                 onChange={this.handleAttributeTileValueChanged.bind(this, "numberOfVersionsAllowed")}
          />
        </div>);

    var bIsAssetType = sAttributeId == "assetcoverflowattribute";
    var oNumberOfItemsAllowedDom = (bIsImageCoverflowAttribute) ?
        (<div className="sectionTileFieldWrapper">
          <div className="sectionTileFieldKey">{getTranslation().MAXIMUM_ALLOWED_ITEMS}</div>
          <input type="number" ref={this.numberOfItemsAllowed}
                 min="0"
                 disabled={bIsAssetType}
                 className="sectionTileFieldValue"
                 value={oVisualElement.numberOfItemsAllowed}
                 onChange={this.handleAttributeTileValueChanged.bind(this, "numberOfItemsAllowed")}
          />
        </div>) : null;

    var oTileHeaderDOM = this.getHeaderView(oDragViewModel, sTileIconClass, sLabel);

    var oVersionCouplingViewDOM = this.getVersionCouplingDom();
    var oVariantCouplingViewDOM = this.getVariantCouplingDom();
    var oBlockerDom = this.getElementBlockerView();

    return (
        <div className="sectionTileContainer">
          {oTileHeaderDOM}
          <div className="sectionTileForm">
            <div className="sectionTileFieldWrapper">
              <div className="sectionTileFieldKey">{getTranslation().DEFAULT_VALUE}</div>
              {this.getDefaultValueView(bIsDisabled)}
            </div>
            {oVersionsAllowedDom}
            {oNumberOfItemsAllowedDom}
            {oVersionCouplingViewDOM}
            {oVariantCouplingViewDOM}
          </div>
          {oCheckBoxDom}
          {oTagListDom}
          {oBlockerDom}
        </div>
    );

  };

  handleElementTagCheckAllChanged = (oEvent) => {
    var __props = this.props;

    var oInfo = {
      sectionId: __props.sectionId,
      elementId: __props.element.id,
      checked: oEvent.currentTarget.checked
    };

    EventBus.dispatch(oEvents.VISUAL_ELEMENT_TAG_CHECK_ALL_CHANGED, this, oInfo)
  };

  handleElementFilterableOrSortableCheckboxChanged = (sProperty, oEvent) => {
    if (!sProperty) {
      return;
    }
    var __props = this.props;

    var oInfo = {
      sectionId: __props.sectionId,
      elementId: __props.element.id,
      checked: oEvent.currentTarget.checked,
      property: sProperty
    };

    EventBus.dispatch(oEvents.VISUAL_ELEMENT_FILTERABLE_OR_SORTABLE_CHECK_ALL_CHANGED, this, oInfo)
  };

  getPlaceholderViewForTypeAndTaxonomyAttribute = (sType, oDragViewModel) => {
    var oVisualElement = this.props.element;
    var sLabel = oVisualElement.attribute.label;
    var sAttributeType = oVisualElement.attribute.type;
    var bIsMandatoryFieldDisabled = ViewUtils.isMandatoryAttribute(sAttributeType);
    var sIsInheritedClassName = "sectionTileFieldValue isInheritedCheck " + oVisualElement.isInheritedUI ? "checked" : "";

    var oTagListDom = null;
    var sTileIconClass = "tileIcon tileIconAttribute ";
    var oCheckBoxDom = this.getFilterableSortableView();

    var oVersionsAllowedDom = bIsMandatoryFieldDisabled ? null :
        (<div className="sectionTileFieldWrapper">
          <div className="sectionTileFieldKey">{getTranslation().VERSION_ALLOWED}</div>
          <input type="number" ref={this.numberOfVersionsAllowed}
                 min="0"
                 className="sectionTileFieldValue"
                 onChange={this.handleAttributeTileValueChanged.bind(this, "numberOfVersionsAllowed")}
          />
        </div>);

    var oTileHeaderDOM = this.getHeaderView(oDragViewModel, sTileIconClass, sLabel);
    var oBlockerDom = this.getElementBlockerView();

    return (
      <div className="sectionTileContainer">
        {oTileHeaderDOM}
        <div className="sectionTileForm">
          <div className="sectionTileFieldWrapper">
            <div className="sectionTileFieldKey">{getTranslation().IS_INHERITED}</div>
            <div className={sIsInheritedClassName}></div>
          </div>
          {oVersionsAllowedDom}
        </div>
        {oCheckBoxDom}
        {oTagListDom}
        {oBlockerDom}
      </div>
    );
  };

  getVisualElement = () => {
    var oVisualElement = this.props.element;
    var oVisualAttributeNode = null;

    var oProperties = {};
    var oVisualAttributeData = {
      id: oVisualElement.type,
      dataId: oVisualElement.id,
      sectionId: this.props.sectionId
    };

    oProperties.data = {visualAttributeData: oVisualAttributeData};
    var oDragViewModel = new DragViewModel(oVisualElement.id, oVisualElement.label, true, "dragFromWithinSection", oProperties);

    if (oVisualElement.type === 'attribute') {
      if (ViewUtils.isAttributeTypeType(oVisualElement.attribute.type) || ViewUtils.isAttributeTypeTaxonomy(oVisualElement.attribute.type)) {
        oVisualAttributeNode = this.getPlaceholderViewForTypeAndTaxonomyAttribute('Attribute', oDragViewModel);
      } else {
        oVisualAttributeNode = this.getPlaceHolderViewForAttribute('Attribute', oDragViewModel);
      }

    } else if (oVisualElement.type === 'tag') {
      oVisualAttributeNode = this.getPlaceHolderViewForTag('Tag', 'tag', oDragViewModel);
    } else if (oVisualElement.type === 'role') {
      oVisualAttributeNode = this.getPlaceHolderViewForTag('Role', 'role', oDragViewModel);
    } else if (oVisualElement.type === 'relationship') {
      oVisualAttributeNode = this.getPlaceHolderViewForRelationship(oDragViewModel);
    }

    return oVisualAttributeNode;
  };

  isAttributeTypeNotSortable = (sBaseType) => {
    var aNonSortableAttributes =  [
        AttributeTypeDictionary.IMAGE, AttributeTypeDictionary.COVERFLOW, AttributeTypeDictionary.IMAGE_COVERFLOW,
        AttributeTypeDictionary.OWNER, AttributeTypeDictionary.ASSIGNEE, AttributeTypeDictionary.IMAGE_ATTRIBUTE,
        AttributeTypeDictionary.CREATED_BY, AttributeTypeDictionary.TYPE, AttributeTypeDictionary.LAST_MODIFIED_BY,
        AttributeTypeDictionary.TAXONOMY
    ];

    return CS.includes(aNonSortableAttributes, sBaseType);
  };

  getFilterableSortableView = () => {
    var oVisualElement = this.props.element;
    var aViews = [];

    if(oVisualElement.type === 'attribute') {
      if (!this.isAttributeTypeNotSortable(oVisualElement.attribute.type)) {
        aViews.push(this.getElementPropertyCheckboxView(ElementPropertyConstants.IS_SORTABLE));
        aViews.push(this.getElementPropertyCheckboxView(ElementPropertyConstants.IS_FILTERABLE));
      }
    }
    else if (oVisualElement.type === 'tag') {
      aViews.push(this.getElementPropertyCheckboxView(ElementPropertyConstants.IS_FILTERABLE));

      if (oVisualElement.tagType === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
        aViews.push(this.getElementPropertyCheckboxView(ElementPropertyConstants.IS_MULTISELECT));
      }
    }

    return aViews;
  };

  getElementPropertyCheckboxView = (sProperty) => {
    var oVisualElement = this.props.element;

    var sCheckBoxLabel = null;
    var bIsChecked = null;

    switch (sProperty) {
      case ElementPropertyConstants.IS_SORTABLE:
        sCheckBoxLabel = getTranslation().IS_SORTABLE;
        bIsChecked = !!oVisualElement.isSortable;
        break;
      case ElementPropertyConstants.IS_FILTERABLE:
        sCheckBoxLabel = getTranslation().IS_FILTERABLE;
        bIsChecked = !!oVisualElement.isFilterable;
        break;
      case ElementPropertyConstants.IS_MULTISELECT:
        sCheckBoxLabel = getTranslation().MULTISELECT;
        bIsChecked = !!oVisualElement.isMultiselect;
        break;
      default:
        sProperty = null;
    }

    return (
        <div className="section-element-filter-search-list-container">
          <div className="filterOrSearchHeader">
            <div className="filterOrSearchHeaderText"> {sCheckBoxLabel} </div>
            <div className="filterOrSearchCheckBox">
              <input className="filterOrSearchCheckAll" checked={bIsChecked} type="checkbox"
                     onChange={this.handleElementFilterableOrSortableCheckboxChanged.bind(this, sProperty)}/>
            </div>
          </div>
        </div>
    );

  };

  render() {

    var oStyle = this.props.style;

    return (
        <div className="visualSectionElementContainer" style={oStyle}>
          {this.getVisualElement()}
        </div>
    );
  }
}

export const view = SectionGridElementView;
export const events = oEvents;
