import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import AutoSize from 'autosize';
import ReactPropTypes from 'prop-types';
import {blue} from '@material-ui/core/colors';
import { view as CustomDatePicker } from '../customdatepickerview/customdatepickerview.js';
import { view as NumberLocaleView } from '../numberlocaleview/number-locale-view';
import { view as FroalaWrapperView } from '../customfroalaview/froala-wrapper-view';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import { view as MappingSummaryView } from '../mappingsummaryview/mapping-summary-view';
import { view as MappingSummaryHeaderView } from '../mappingsummaryview/mapping-summary-header-view';
import { view as GridYesNoPropertyView } from '../gridview/grid-yes-no-property-view';
import { view as TabLayoutView } from '../tablayoutview/tab-layout-view';
import { view as CustomTextFieldView } from '../customtextfieldview/custom-text-field-view';
import { view as ContentMeasurementMetricsView } from '../measurementmetricview/content-measurement-metrics-view';
import ContentMeasurementMetricsViewModel from '../measurementmetricview/model/content-measurement-metrics-view-model';
import { view as NewMultiSelectSearchView } from '../multiselectview/multi-select-search-view';
import { view as LazyMSSView } from '../lazy-mss-view/lazy-mss-view';
import { view as SelectionToggleView } from '../selectiontoggleview/selection-toggle-view';
import { view as ColorPickerView } from '../color-picker-view/color-picker-view';
import ViewLibraryUtils from '../utils/view-library-utils';
import { view as AuthorizationMappingSummaryView} from '../authorizationMappingSummaryView/authorization-mapping-summary-view';
import { view as PropertyGroupSummaryView } from '../mappingsummaryview/property-group-summary-view';
import MappingTabHeaderData from '../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-map-summery-header';
import { view as ProcessFrequencySummaryHeaderView } from '../processFrequencySummaryView/process-frequency-summary-view';
import MockDataForMeasurementMetricAndImperial from '../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import MockDataIdsForFroalaView from '../../commonmodule/tack/mock-data-ids-for-froala-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ViewContextConstants from '../../commonmodule/tack/view-context-constants';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as RadioButtonView } from '../radiobuttonview/radio-button-view';
import ThemeConfigurationConstants from '../../screens/homescreen/screens/settingscreen/tack/mock/theme-configuration-constants';
import {view as IconLibrarySelectIconView} from '../../screens/homescreen/screens/settingscreen/view/icon-library-select-icon-view';

const aDisableViaOverlayFieldTypes = ["image", "custom", "yesNoBoth", "formula", "concatenatedFormula", "customView",
  "froalaView", "mappingSummary", "tabSummary", "mappingSummaryHeader", ViewContextConstants.RULE_DETAILS_VIEW_CONFIG, "selectionToggle"];
const oEvents = {
  COMMON_CONFIG_SECTION_MAPPING_SUMMARY_TAB_CHANGED: "common_config_section_mapping_summary_tab_changed",
  COMMON_CONFIG_SECTION_SINGLE_TEXT_CHANGED: "common_config_section_single_text_changed",
  COMMON_CONFIG_SECTION_FROALA_TEXT_CHANGED: "common_config_section_froala_text_changed",
  COMMON_CONFIG_SECTION_MULTI_TEXT_CHANGED: "common_config_section_multi_text_changed",
  COMMON_CONFIG_SECTION_TAG_TYPE_RANGE_VALUE_CHANGED: "common_config_section_tag_type_range_value_changed",
  COMMON_CONFIG_SECTION_ICON_CHANGED: "common_config_section_icon_changed",
  COMMON_CONFIG_SECTION_ICON_REMOVED: "common_config_section_icon_removed",
  COMMON_CONFIG_SECTION_SINGLE_TEXT_NUMBER_CHANGED: "common_config_section_single_text_number_changed",
  COMMON_CONFIG_CHECKBOX_ON_CHANGE_FOR_ENTITY_PERMISSION: "common_config_checkbox_on_change_entity_permission",
  COMMON_CONFIG_SECTION_NATIVE_DROPDOWN_VALUE_CHANGED: "common_config_section_native_dropdown_value_changed",
  COMMON_CONFIG_SECTION_ADD_OPERATOR: "common_config_section_add_operator",
  COMMON_CONFIG_SECTION_OPERATOR_ATTRIBUTE_VALUE_CHANGED: "common_config_section_operator_attribute_value_changed",
  COMMON_CONFIG_SECTION_DELETE_OPERATOR_ATTRIBUTE_VALUE: "common_config_section_delete_operator_attribute_value",
  COMMON_CONFIG_SECTION_ADD_CONCAT_OBJECT: "common_config_section_add_concat_object",
  COMMON_CONFIG_SECTION_CONCAT_INPUT_CHANGED: "common_config_section_concat_input_changed",
  COMMON_CONFIG_SECTION_CONCAT_OBJECT_REMOVED: "common_config_section_concat_object_removed",
  COMMON_CONFIG_SECTION_YES_NO_BUTTON_CHANGED: "common_config_section_yes_no_button_changed",
  COMMON_CONFIG_SECTION_TEMPLATE_ACTION_CLICKED: "common_config_section_template_action_clicked",
  COMMON_CONFIG_UPLOAD_ICON_CLICKED: "common_config_upload_icon_clicked",
  COMMON_CONFIG_SECTION_RADIO_BUTTON_CHANGED: "common_config_section_radio_button_changed",
};
const oPropTypes = {
  context: ReactPropTypes.string,
  sectionLayout: ReactPropTypes.array,
  disabledFields: ReactPropTypes.array,
  data: ReactPropTypes.object,
  oCalculatedAttributeMapping: ReactPropTypes.object,
  errorTextForCodeEntity: ReactPropTypes.string,
  mappingType: ReactPropTypes.string
};

/**
 * @class CommonConfigSectionView - use for create mapping to Application.
 * @memberOf Views
 * @property {string} [context] -  context.
 * @property {array} [sectionLayout] -  an array of section layout including elements.
 * @property {array} [disabledFields] -  an array of disabled fileds.
 * @property {object} [data] -  object which contain code , lable, and errortext.
 * @property {object} [oCalculatedAttributeMapping] -  object which contain oCalculatedAttributeMapping.
 * @property {string} [errorTextForCodeEntity] -  string of errorTextForCodeEntity
 */

// @CS.SafeComponent
class CommonConfigSectionView extends React.Component {

  constructor(props) {
    super(props);

    this.iconUpload = React.createRef();
    this.setRef =( sContext, element) => {
      this["iconUpload" + sContext] = element;
    };
  }

  componentDidUpdate(){
    var _this = this;
    var sContext = _this.props.context;
    var sRef = "iconUpload"+sContext;
    // var $aTextAreaDom = $(ReactDOM.findDOMNode(this)).find('.textAreaEl');
    var aTextAreaDom = ReactDOM.findDOMNode(this).querySelectorAll('.textAreaEl');

    for(let i=0; i<aTextAreaDom.length; i++) {
      _this.autoHeight(aTextAreaDom[i]);
    }
/*
    $.each($aTextAreaDom, function(iIndex, oDom){
      _this.autoHeight(oDom);
    });*/

    if(this[sRef] && this[sRef]) {
      this[sRef].value = '';
    }

  }

  setHeight = (el, val) => {
    if (typeof val === "function") val = val();
    if (typeof val === "string") el.style.height = val;
    else el.style.height = val + "px";
  };

  autoHeight = (a) => {
/*    if (!$(a).prop('scrollTop')) {
      do {
        var b = $(a).prop('scrollHeight');
        var h = $(a).height();
        $(a).height(h - 5);
      }
      while (b && (b != $(a).prop('scrollHeight')));
    }

    $(a).height($(a).prop('scrollHeight') + 20);*/

    if (!a.scrollTop) {
      do {
        var b = a.scrollHeight;
        var h = parseFloat(getComputedStyle(a, null).height.replace("px", ""));
        this.setHeight(h - 5);
      }
      while (b && (b != a.scrollHeight));
    }

    this.setHeight(a, a.scrollHeight + 20);
    // $(a).height($(a).prop('scrollHeight') + 20);
  };

  handleTextAreaKeyUp =(oEvent)=>{

    this.autoHeight(oEvent.target);
  }

  getIsDisabledField = (sKey) => {
    var aDisabledKeys = this.props.disabledFields || [];
    return CS.includes(aDisabledKeys, sKey);
  };

  handleSingleTextChanged =(sKey, sValue)=> {
    var sNewValue = sValue;
    var sContext = this.props.context;

    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_SINGLE_TEXT_CHANGED, this, sContext, sKey, sNewValue);
  }

  handleFroalaTextChanged =(sKey, oEvent)=>{
    var oNewValue = {
      valueAsHtml: oEvent.htmlValue,
      value: oEvent.textValue
    };
    var sContext = this.props.context;
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_FROALA_TEXT_CHANGED, this, sContext, sKey, oNewValue);
  }

  handleNumberChanged =(sKey, sNewValue)=>{
    var sOldValue = this.props.data[sKey];
    var sContext = this.props.context;
    if(sNewValue != sOldValue){
      if (sNewValue.includes(".")) {
        var sSplitter = ".";
        var aSplits = sNewValue.split(sSplitter);
        if (aSplits[1].length <= this.props.data.selectedPrecision) {
          EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_MULTI_TEXT_CHANGED, this, sContext, sKey, sNewValue);
          return;
        }
        else {
          return;
        }
      }
      EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_MULTI_TEXT_CHANGED, this, sContext, sKey, sNewValue);
    }
  }

  handleNumberBlurred =(sKey, sBaseUnit, sCurrentUnit, sPrecision, sNewValue)=> {
    var sOldValue = this.props.data[sKey];
    if (sBaseUnit != "" && sCurrentUnit != "" && sOldValue != "") {
        // sOldValue = ViewLibraryUtils.getMeasurementAttributeValueToShow(sOldValue, sBaseUnit, sCurrentUnit, sPrecision).toString();
    }
    var sContext = this.props.context;
    sNewValue = sNewValue.toString();
    if (!CS.isEmpty(sNewValue) && sNewValue.includes(".")) {
      var sSplitter = ".";
      var aSplits = sNewValue.split(sSplitter);
      if(this.props.data.selectedPrecision == 0) {
        sNewValue = aSplits[0];
      }
      if (aSplits[1].length > this.props.data.selectedPrecision) {
        //EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_MULTI_TEXT_CHANGED, this, sContext, sKey, sNewValue);
        return;
      }
    }
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_MULTI_TEXT_CHANGED, this, sContext, sKey, sNewValue);
  }

  handleMultiTextChanged=(sKey, oEvent)=> {
    var sNewValue = oEvent.target.value;
    var sOldValue = this.props.data[sKey];
    var sContext = this.props.context;
    if(sNewValue != sOldValue){
      EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_MULTI_TEXT_CHANGED, this, sContext, sKey, sNewValue);
    }
  }

  getSingleTextView=(sValue, sKey, sHintText)=> {
    var sErrorText ="";
    var fOnBlur = this.handleSingleTextChanged.bind(this, sKey);
    var fOnChange = null;
    let bShowCopyToClipboardButton = false;

    if(sKey === "code") {
      let sContext = this.props.context;
      if(sContext === "languageTree") {
        sErrorText = this.props.errorTextForCodeEntity;
      } else {
        sHintText = CS.isEmpty(this.props.errorTextForCodeEntity) ? "" : getTranslation().AUTO_GENERATED_CODE;
      }
      bShowCopyToClipboardButton = true;
    }

    let bIsDisabled = this.getIsDisabledField(sKey);
    return (<div className="singleTextWrapper elementWrapper">
      <CustomTextFieldView className="singleText"
                           value={sValue}
                           onBlur={fOnBlur}
                           onChange={fOnChange}
                           errorText={sErrorText}
                           shouldShowErrorText={true}
                           showCopyToClipboardButton={bShowCopyToClipboardButton}
                           isDisabled={bIsDisabled}
                           hintText={sHintText}/>
    </div>);
  }

  getCustomMaterialStyles =()=> {

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
  }

  handleDateChanged =(sKey, oNull, sNewDate)=> {
    var sNewValue = sNewDate;
    var sContext = this.props.context;
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_MULTI_TEXT_CHANGED, this, sContext, sKey, sNewValue);
  }

  getMultiTextView =(sValue, sKey)=> {
    var oData = this.props.data;
    var oDataType = oData.type;
    let bIsDisabled = this.getIsDisabledField(sKey);
    let oMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
    if(oDataType && oDataType.context == "attributeType" && sKey == "defaultValue" && ViewLibraryUtils.isAttributeTypeNumeric(oDataType.selectedItems[0])){
      if (CS.isString(sValue)&& !(CS.endsWith(sValue, ".") || CS.endsWith(sValue, ",")) && sValue != "" && !((sValue.includes(".") || sValue.includes(",")) && CS.endsWith(sValue, "0"))) {
        var oPrecision = oData.precision;
        var sNewValue = ViewLibraryUtils.getTruncatedValue(Number(sValue), oPrecision.selectedValue);
        if (sNewValue != sValue) {
          sValue = sNewValue.toString();
          this.handleNumberChanged(sKey, sValue);
        }
      }
      if(ViewLibraryUtils.isAttributeTypeNumber(oDataType.selectedItems[0])) { //NumberAttribute
        return (<div className="multiTextWrapper elementWrapper">
          <NumberLocaleView
              value={sValue}
              isDisabled={bIsDisabled}
              onChange={this.handleNumberChanged.bind(this, sKey)}
              label={""}
          />
        </div>);
      } else if (oData.isUnitAttribute) { //Measurement Attribute
        var sBaseUnit = "";
        var sCurrentUnit = "";
        var aItems = [];
        if(oData.defaultUnit != null){
          aItems =  oData.defaultUnit.items || [];
        }
        if(!ViewLibraryUtils.isMeasurementAttributeTypeCustom(oDataType.selectedItems[0])){
          sBaseUnit = (CS.find(oMeasurementMetricAndImperial[oDataType.selectedItems[0]], {isBase: true}))["unit"];
          sCurrentUnit = (CS.find(oData.defaultUnit.items, {"id": oData.defaultUnit.selectedItems[0]}))["unit"];
        }
        var sPrecisionValue = oData.precision.selectedValue;
        var oProperties = {};
        oProperties.baseUnit = sBaseUnit;
        oProperties.attributeType = oDataType.selectedItems[0];

        if(oData.customDefaultUnit){
          oProperties.defaultUnitAsHTML = oData.customDefaultUnit;
        }

        var oModel = new ContentMeasurementMetricsViewModel(
            oData.id,
            "",
            sValue,
            sCurrentUnit,
            sBaseUnit,
            oData.placeholder,
            "",
            oData.description,
            bIsDisabled,
            false,
            aItems,
            oProperties,
            Number(sPrecisionValue)
        );
        return (<div className="multiTextWrapper elementWrapper">
          <ContentMeasurementMetricsView model={oModel}
                                         onBlur={this.handleNumberBlurred.bind(this, sKey, sBaseUnit, sCurrentUnit, sPrecisionValue)}/>
        </div>);
      } else if (ViewLibraryUtils.isAttributeTypeDate(oDataType.selectedItems[0])) { //date
        sValue = sValue ? +sValue ? new Date(+sValue) : null : null;
        var oStyle = {width: "100%"};
        var oCustomStyle = this.getCustomMaterialStyles();

        if (CS.isNaN(Date.parse(sValue))) {
          sValue = null;
        }

        return (<div className="multiTextWrapper elementWrapper">
            <CustomDatePicker
                value={sValue}
                disabled={bIsDisabled}
                className="datePickerCustom"
                onChange={this.handleDateChanged.bind(this, sKey)}
                style={oStyle}
                underlineStyle={oCustomStyle.underlineStyle}
                underlineFocusStyle={oCustomStyle.underlineFocusStyle}
            />
        </div>);
      }
    }

    return (<div className="multiTextWrapper elementWrapper">
      <textarea className="multiText textAreaEl"
                onKeyUp={this.handleTextAreaKeyUp}
                value={sValue || ""}
                disabled={bIsDisabled}
                readOnly={bIsDisabled}
                onChange={this.handleMultiTextChanged.bind(this, sKey)}/>
    </div>);
  }

  getNewMSSView =(oData, sKey)=> {
    let bIsDisabled = this.getIsDisabledField(sKey);

    var sMSSClassName = (sKey == "color") ? "mssWrapper elementWrapper color": "mssWrapper elementWrapper";
    var aMSSView = [];
    aMSSView.push(<div className={sMSSClassName} key="1">
      <NewMultiSelectSearchView
          isMultiSelect={oData.isMultiSelect}
          disabled={oData.disabled || bIsDisabled}
          label={CS.getLabelOrCode(oData)}
          items={oData.items}
          selectedItems={oData.selectedItems}
          selectedObjects={oData.selectedObjects}
          context={oData.context}
          cannotRemove={oData.cannotRemove}
          showColor= {true}
          onApply= {oData.onApply}
          showCreateButton={oData.showCreateButton}
          onPopOverOpen={oData.onPopOverOpen}
          searchHandler={oData.searchHandler}
          searchText={oData.searchText}
          loadMoreHandler={oData.loadMoreHandler}
          isLoadMoreEnabled={oData.isLoadMoreEnabled}
      />
    </div>);

    if(sKey == "color"){
      var oStyle = {
        "backgroundColor" : oData.selectedItems[0]
      };
      aMSSView.push(<div className="colorDisplayBlock elementWrapper" style={oStyle} key="2"/>);
    }

    return (aMSSView);
  }

  getLazyMSSView = (oData, sKey) => {
    let sMSSClassName = (sKey == "color") ? "mssWrapper elementWrapper color" : "mssWrapper elementWrapper";
    let aMSSView = [];
    let bIsDisabled = this.getIsDisabledField(sKey);
    let oStyle = {
      maxWidth: '90%',
      maxHeight: '350px'
    };

    aMSSView.push(<div className={sMSSClassName} key="1">
      <LazyMSSView
          isMultiSelect={oData.isMultiSelect}
          disabled={oData.disabled || bIsDisabled}
          label={CS.getLabelOrCode(oData)}
          selectedItems={oData.selectedItems}
          context={oData.context}
          cannotRemove={oData.cannotRemove}
          showColor={true}
          onApply={oData.onApply}
          showCreateButton={oData.showCreateButton}
          onCreateHandler={oData.onCreateHandler}
          isLoadMoreEnabled={oData.isLoadMoreEnabled}
          onPopOverOpen={oData.onPopOverOpen}
          searchHandler={oData.searchHandler}
          searchText={oData.searchText}
          loadMoreHandler={oData.loadMoreHandler}
          referencedData={oData.referencedData}
          requestResponseInfo={oData.requestResponseInfo}
          popoverStyle = {oStyle}
          excludedItems={oData.excludedItems}
          bShowIcon={oData.bShowIcon}
      />
    </div>);

    if (sKey == "color") {
      let oStyle = {
        "backgroundColor": oData.selectedItems[0]
      };
      aMSSView.push(<div className="colorDisplayBlock elementWrapper" style={oStyle} key="2"/>);
    }

    return (aMSSView);
  }

  getYesNeutralView =()=> {
    return (
        <div className="ynWrapper elementWrapper">
          <div className="singleTextWrapper">
            <div className="singleText">{getTranslation().NEUTRAL} :</div>
            <input className="singleTextInput" value={0} readOnly/>
          </div>
          <div className="singleTextWrapper" >
            <div className="singleText" >{getTranslation().YES} :</div>
            <input className="singleTextInput" value={100} readOnly/>
          </div>
        </div>);
  }

  getYesNoNeutralView =()=> {
    return (
        <div className="ynnWrapper elementWrapper">
          <div className="singleTextWrapper" >
            <div className="singleText">{getTranslation().NO} :</div>
            <input className="singleTextInput" value={-100} readOnly/>
          </div>
          <div className="singleTextWrapper">
            <div className="singleText" style={{display: "inline-block"}}>{getTranslation().NEUTRAL} :</div>
            <input className="singleTextInput" style={{display: "inline-block"}} value={0} readOnly/>
          </div>
          <div className="singleTextWrapper">
            <div className="singleText">{getTranslation().YES} :</div>
            <input className="singleTextInput"value={100} readOnly/>
          </div>
        </div>);
  }

  getRangeView =()=> {
    return (
        <div className="rangeTypeWrapper elementWrapper">
          <div className="singleTextWrapper">
            <div className="singleText" >{getTranslation().FROM} :</div>
            <input className="singleTextInput" value={-100} readOnly/>
          </div>
          <div className="singleTextWrapper" style={{display: "inline-block"}}>
            <div className="singleText" >{getTranslation().TO} :</div>
            <input className="singleTextInput" style={{display: "inline-block"}} value={100} readOnly/>
          </div>
        </div>);
  }

  handleCustomTagValueChanged=(sTagValueId, oEvent)=> {
    var sNewValue = oEvent.target.value;
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_TAG_TYPE_RANGE_VALUE_CHANGED, this, sTagValueId, sNewValue);
  }

  handleCustomTagValueBlurred=(sTagValueId, oEvent)=> {
    var sNewValue = oEvent.target.value;
    if(CS.isEmpty(sNewValue)){
      sNewValue = 0;
      EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_TAG_TYPE_RANGE_VALUE_CHANGED, this, sTagValueId, sNewValue);
    }
  }

  getCustomTagView=(oElement)=> {
    var iFrom = oElement.fromValue;
    var iTo = oElement.toValue;
    return (
        <div className="customTypeWrapper elementWrapper">
          <div className="singleTextWrapper" >
            <div className="singleText" >{getTranslation().FROM} :</div>
            <input className="singleTextInput"
                   type="number"
                   value={iFrom}
                   onBlur={this.handleCustomTagValueBlurred.bind(this, oElement.fromId)}
                   onChange={this.handleCustomTagValueChanged.bind(this, oElement.fromId)}/>
          </div>
          <div className="singleTextWrapper" >
            <div className="singleText" style={{display: "inline-block"}}>{getTranslation().TO} :</div>
            <input className="singleTextInput"
                   type="number"
                   value={iTo}
                   onBlur={this.handleCustomTagValueBlurred.bind(this, oElement.toId)}
                   onChange={this.handleCustomTagValueChanged.bind(this, oElement.toId)}/>
          </div>
        </div>);

  }

  handleGridYesNoChanged=(sKey, sContext, sConfigContext)=> {
    var bValue = !sContext;
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_YES_NO_BUTTON_CHANGED, this, sKey, bValue, sConfigContext);
  }

  handleRadioButtonChanged=(sContext, sRadioKey)=> {
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_RADIO_BUTTON_CHANGED, this, sContext, sRadioKey);
  }

  handleCheckboxValueChanged =(oData, sContext, oEvent)=>{
    var bIsChecked = oEvent.target.checked;
    var sScreenContext = oData.type.context;
    EventBus.dispatch(oEvents.COMMON_CONFIG_CHECKBOX_ON_CHANGE_FOR_ENTITY_PERMISSION, this, bIsChecked, sContext, sScreenContext);
  }

  getCheckBoxView=(oData, sContext, sKey)=> {
    var bChecked = oData[sContext];
    var oView = (
        <input className="chkScreen" type="checkbox" checked={bChecked}
               onChange={this.handleCheckboxValueChanged.bind(this, oData, sContext)}/>
    );
    return (<div className="checkbox">{oView}</div>);
  }

  getYesNoView=(oElementData, sKey)=> {
    let bIsDisabled = this.getIsDisabledField(sKey);
    var sConfigContext = oElementData.context;
    return (
        <div className="yesNoViewWrapper elementWrapper">
          <div className="yesNoContainer">
            <GridYesNoPropertyView propertyId={oElementData.context} value={oElementData.isSelected}
                                   isDisabled={oElementData.isDisabled || bIsDisabled} onChange={this.handleGridYesNoChanged.bind(this, sKey, oElementData.isSelected, sConfigContext)}/>
          </div>
        </div>
    );
  }

  getYesNoBothView=(oData, sKey)=> {

    var oStyle = {display: "inline-block"};
    return (
        <div className="yesNoBothViewWrapper elementWrapper">
          <div className="pimContainer checkboxGroupContainer">
            <div className="pimLabel checkboxLabel" style={oStyle}>{getTranslation().PIM}</div>
            <div className="pimCheckBoxContainer" style={oStyle}>
              {this.getCheckBoxView(oData, "forPim", sKey)}
            </div>
          </div>
          <div className="mamContainer checkboxGroupContainer">
            <div className="mamLabel checkboxLabel" style={oStyle}>{getTranslation().MAM}</div>
            <div className="mamCheckBoxContainer" style={oStyle}>
              {this.getCheckBoxView(oData, "forMam", sKey)}
            </div>
          </div>
          <div className="targetContainer checkboxGroupContainer">
            <div className="targetLabel checkboxLabel" style={oStyle}>{getTranslation().MARKET}</div>
            <div className="targetCheckBoxContainer" style={oStyle}>
              {this.getCheckBoxView(oData, "forTarget", sKey)}
            </div>
          </div>
        </div>
    );
  }

  getSelectionToggleView=(oData, sKey)=> {
    return (
        <SelectionToggleView
            disabledItems={oData.disabledItems}
            items={oData.items}
            selectedItems={oData.selectedItems}
            context={oData.context}
            singleSelect={oData.singleSelect}
            contextKey={sKey}
        />
    );
  }

  showSelectIconDialog = () => {
    let oSelectIconData = this.props.data.selectIconData;
    let sContext = this.props.context;
    return (
        <IconLibrarySelectIconView
            selectIconData={oSelectIconData}
            context={sContext}>
        </IconLibrarySelectIconView>
    );
  }

  uploadCommonConfigIcon = (sConfigContext) => {
    var sRefContext = "iconUpload" + sConfigContext;
    EventBus.dispatch(oEvents.COMMON_CONFIG_UPLOAD_ICON_CLICKED, this[sRefContext], sConfigContext);
  }

  removeCommonConfigIcon =(sKey, sConfigContext)=> {
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_ICON_REMOVED, this, sKey, sConfigContext, []);
  }

  handleCommonConfigIconChanged =(sKey, sConfigContext, oEvent)=> {
    var aFiles = oEvent.target.files;
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_ICON_CHANGED, this, sKey, sConfigContext, aFiles);
  }

  getImageView=(oElement)=> {

    var sIcon = oElement.icon;
    var sConfigContext = oElement.context;

    var sDefaultCommonConfigIcon = '';
    var sUploadCommonConfigIconString = getTranslation().CHANGE_ICON;
    var sRemoveCommonConfigIconString = getTranslation().REMOVE_ICON;
    var sRemoveCommonConfigIconClass = 'iconRemoveImage';
    var sDownloadCommonConfigIconString = getTranslation().DOWNLOAD_ICON;
    var sDownloadCommonConfigIconClass = 'iconDownloadImage';
    var sIconView = "";

    if( sConfigContext == "hideImage"){
      return null;
    }

    if(CS.isEmpty(sIcon)) {
      sDefaultCommonConfigIcon = 'defaultIcon';
      sUploadCommonConfigIconString = getTranslation().UPLOAD_ICON;
      sRemoveCommonConfigIconString = "";
      sDownloadCommonConfigIconString = "";
      sRemoveCommonConfigIconClass += " dispN";
      sDownloadCommonConfigIconClass += " dispN";
      sIconView = (<span className="icon"></span>);
    } else {
      let sIconKey = oElement.iconKey ? oElement.iconKey : oElement.icon;
      sIcon = ViewLibraryUtils.getIconUrl(sIconKey,false);
      sIconView = (<ImageSimpleView classLabel="icon" imageSrc={sIcon}/>);
    }
    return (
        <div className='iconDetailRow elementWrapper'>
          <div className="iconControlContainer">
            <div className="iconChangeImage" onClick={this.uploadCommonConfigIcon.bind(this, sConfigContext)} title={sUploadCommonConfigIconString}></div>
            <a className={sDownloadCommonConfigIconClass}
               href={sIcon+"?download=icon"} title={sDownloadCommonConfigIconString} target="_blank">
            </a>
            {/*<div className={sRemoveCommonConfigIconClass} onClick={this.removeCommonConfigIcon.bind(this, 'icon', sConfigContext)} title={sRemoveCommonConfigIconString}></div>*/}
          </div>
          <div className="iconContainer">
            <div className={"iconWrapper " + sDefaultCommonConfigIcon}>
              {/*<img className="icon" src={sIcon}/>*/}
              {sIconView}
            </div>
            <div className={sRemoveCommonConfigIconClass} onClick={this.removeCommonConfigIcon.bind(this, 'icon', sConfigContext)} title={sRemoveCommonConfigIconString}></div>
            {/*<span className="iconChange" onClick={this.uploadTagIcon}>
              {sUploadTagIconString}
            </span>*/}
          </div>
          <input style={{"visibility": "hidden", height: "0", width: "0"}}
                 ref={this.setRef.bind(this,sConfigContext)}
                 onChange={this.handleCommonConfigIconChanged.bind(this, 'icon', sConfigContext)}
                 type="file"
                 />
        </div>
    );
  }

  handleSingleTextNumberChanged=(sKey, sNewValue)=> {
    if (+sNewValue >= 0) {
      EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_SINGLE_TEXT_NUMBER_CHANGED, this.props.context, sKey, sNewValue);
    }
  }

  handleNativeDropdownValueChanged=(sKey, oEvent)=> {
    var sNewValue = oEvent.target.value;
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_NATIVE_DROPDOWN_VALUE_CHANGED, this.props.context, sKey, sNewValue);
  }

/*
  addOperator=(sOperatorType)=> {
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_ADD_OPERATOR, this, sOperatorType);
  }
*/

  /*addConcatObject=(sObjectType)=> {
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_ADD_CONCAT_OBJECT, sObjectType);
  }

  handleConcatInputChanged=(oConcat, oEvent)=> {
    var sValue = "";
    if(oConcat.type == "html"){
      sValue = {
        valueAsHtml: oEvent.htmlValue,
        value: oEvent.textValue
      }
    }else {
      sValue = oEvent.target.value;
    }

    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_CONCAT_INPUT_CHANGED, sValue, oConcat);
  }

  handleConcatObjectRemoveClicked=(sId)=> {
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_CONCAT_OBJECT_REMOVED, sId);
  }*/

  handleOperatorAttributeValueChanged =(oAttributeOperator, bForConcatenation, oEvent)=> {
    var sValue = oEvent.target.value;
    if(bForConcatenation){
      EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_CONCAT_INPUT_CHANGED, sValue, oAttributeOperator);
    }else{
      var sType = oAttributeOperator.type;
      var sId = oAttributeOperator.id;
      EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_OPERATOR_ATTRIBUTE_VALUE_CHANGED, this, sId, sType, sValue);
    }
  }

  deleteOperatorAttributeValue =(sId)=>{
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_DELETE_OPERATOR_ATTRIBUTE_VALUE, this, sId);
  }

  getSingleTextNumberView=(oElementData, oElement)=> {
    var sKey = oElement.key;
    var sLabel = "";
    var bIsDisabled = oElement.isDisabled || this.getIsDisabledField(sKey);
    if (sKey == 'characterLimit') {
      sLabel = getTranslation().CHARACTER_LIMIT + " :";
    } else if (sKey == 'numberOfItemsAllowed') {
      sLabel = getTranslation().NUMBER_OF_ITEMS + " :";
    }
    var iMin = oElement.min ? oElement.min : null;
    var iMax = oElement.max ? oElement.max : null;

    var oInputDOM = null;
    if (bIsDisabled) {
      oInputDOM = <input disabled className="singleTextInput disabled" value={oElementData}/>
    } else {
      oInputDOM = <NumberLocaleView
          value={oElementData}
          onBlur={this.handleSingleTextNumberChanged.bind(this, sKey)}
          minValue={iMin}
          maxValue={iMax}/>;
    }

    return (
        <div className="singleTextNumberWrapper elementWrapper">
          <div className="singleText" >{sLabel}</div>
          {oInputDOM}
        </div>);
  }

  getNativeDropdownView=(sKey, oData)=> {
    var aData = oData.values;
    var sSelectedValue = oData.selectedValue;
    var aOptions = [];
    var bIsDisabled = !!oData.isDisabled || this.getIsDisabledField(sKey);
    if (oData.idAsLabel) {
      aOptions = CS.map(aData, function (sValue) {
        return (<option value={sValue}>{sValue}</option>);
      });
    } else {
      if (CS.isEmpty(oData.optionGroups)) {
        aOptions = CS.map(aData, function (oValue) {
          return (<option value={oValue.id}>{CS.getLabelOrCode(oValue)}</option>);
        });
      } else {
        CS.forEach(aData, function (aGroup, sIndex) {
          var aOptionViews = CS.map(aGroup, function (oValue) {
            return (<option value={oValue.id} key={oValue.id}>{CS.getLabelOrCode(oValue)}</option>);
          });
          if (!CS.isEmpty(aOptionViews)) {
            aOptions.push(
                <optgroup key={oData.optionGroups[sIndex]} label={oData.optionGroups[sIndex]}>
                  {aOptionViews}
                </optgroup>
            );
          }
        })
      }
    }

    if (oData.addEmptyOption) {
      aOptions.unshift(<option value={""} key="1">{"---"}</option>);
    }

    return (
        <div className="nativeDropdownWrapper elementWrapper">
          <select value={sSelectedValue} disabled={bIsDisabled} className="nativeDropdown" onChange={this.handleNativeDropdownValueChanged.bind(this, sKey)}>{aOptions}</select>
        </div>
    );
  }

/*
  getOperatorView =(oAttributeOperator)=>{
    var sOperatorValue = oAttributeOperator.operator;
    var sId = oAttributeOperator.id;
    var aDom = [];
    if(sOperatorValue == "OPENING_BRACKET" || sOperatorValue == "CLOSING_BRACKET"){
      if(sOperatorValue == "OPENING_BRACKET"){
        aDom.push("(");
      }else{
        aDom.push(")");
      }
    }else{
      aDom.push(<select value={sOperatorValue} onChange={this.handleOperatorAttributeValueChanged.bind(this, oAttributeOperator, false)}>
        <option value="ADD">+</option>
        <option value="SUBTRACT">-</option>
        <option value="MULTIPLY">*</option>
        <option value="DIVIDE">/</option>
      </select>);
    }
    return(<div className="operatorWrapper elementWrapper">
      <div className="operatorButtonWrapper">
        <input type="button" value="X" onClick={this.deleteOperatorAttributeValue.bind(this, sId)}/>
      </div>
      <div>
        {aDom}
      </div>
    </div>);
  }
*/

/*
  isFromAttributesToExcludeForConcatenated=(sBaseType)=> {
    var sVisualType = ViewLibraryUtils.getAttributeTypeForVisual(sBaseType);
    var aTypesToExclude = ["concatenated", "image", "coverflow"];

    return (
        CS.includes(aTypesToExclude, sVisualType) ||
        ViewLibraryUtils.isAttributeTypeFile(sBaseType) ||
        ViewLibraryUtils.isAttributeTypeType(sBaseType) ||
        ViewLibraryUtils.isAttributeTypeTaxonomy(sBaseType)
    );
  }
*/

  /**
   * @deprecated
   * @param oAttributeOperator
   * @param sContext
   * @param bShowId
   * @returns {*}
   */
/*
  getAttributeDropdownView =(oAttributeOperator, sContext, bShowId)=>{
    var _this = this;
    var bForConcatenation = sContext == "concatenated";
    var sAttributeId = oAttributeOperator.attributeId;
    var oAttrDropDownMap = ViewUtils.getAttributeList();
    var aOptionDom = [];
    var sMainAttrId = ViewUtils.getSelectedAttribute().id;
    var oMainAttr = ViewUtils.getAttributeById(sMainAttrId);
    aOptionDom.push(<option disabled selected></option>);
    CS.forEach(oAttrDropDownMap, function (oAttribute) {
      var sAttributeId = oAttribute.id;
      var sLabel = bShowId ? CS.getLabelOrCode(oAttribute) + "  (" + sAttributeId + ") ": CS.getLabelOrCode(oAttribute);
      if (!bForConcatenation && sAttributeId != sMainAttrId &&
          _this.checkAttributeForCalculatedAttribute(oMainAttr, oAttribute.type, oAttribute.calculatedAttributeType)) {
        aOptionDom.push(<option value={sAttributeId} title={sLabel}>{sLabel}</option>);
      }else if(bForConcatenation){
        if(!_this.isFromAttributesToExcludeForConcatenated(oAttribute.type)){
          aOptionDom.push(<option value={sAttributeId} title={sLabel}>{sLabel}</option>);
        }
      }
    });
    return(<select value={sAttributeId} onChange={this.handleOperatorAttributeValueChanged.bind(this, oAttributeOperator, bForConcatenation)}>
      {aOptionDom}
    </select>)
  }
*/

/*
  checkAttributeForCalculatedAttribute=(oCalculatedAttr, sAttrType, sCalcAttrType)=> {
    var sVisualType = ViewLibraryUtils.getAttributeTypeForVisual(sAttrType);

    if (sVisualType == "number") {
      return true;
    }

    oCalculatedAttr = oCalculatedAttr.clonedObject ? oCalculatedAttr.clonedObject : oCalculatedAttr;

    var sCalculatedAttributeType = oCalculatedAttr.calculatedAttributeType;
    var sCalculatedAttributeUnit = oCalculatedAttr.calculatedAttributeUnit;

    var bIsCalcAttrOfCustomType = ViewLibraryUtils.isMeasurementAttributeTypeCustom(sCalculatedAttributeType);

    if (!sCalculatedAttributeType || bIsCalcAttrOfCustomType || !sCalculatedAttributeUnit) {
      if (sVisualType == "measurementMetrics" || sVisualType == "calculated") {
        return true;
      }
      return false;
    }

    var sTypeToCheck = sVisualType != 'calculated' ? sAttrType : sCalcAttrType;
    var aAllowedAttributeTypes = CS.keys(this.props.oCalculatedAttributeMapping[sCalculatedAttributeType][sCalculatedAttributeUnit]);

    return CS.includes(aAllowedAttributeTypes, sTypeToCheck);
  }
*/

  /**
   * @deprecated
   * @param oAttributeOperator
   * @returns {*}
   */
/*
  getAttributeOrValueView =(oAttributeOperator)=>{
    var sType = oAttributeOperator.type;
    var sId = oAttributeOperator.id;
    var aDom = [];

    if(sType == "ATTRIBUTE"){
      var oDom = this.getAttributeDropdownView(oAttributeOperator);
      aDom.push(oDom);
    }else if(sType == "VALUE"){
      aDom.push(<input value={oAttributeOperator.value} type="number" onChange={this.handleOperatorAttributeValueChanged.bind(this, oAttributeOperator, false)}/>);
    }

    return((<div className="operatorWrapper elementWrapper">
      <div className="operatorButtonWrapper">
        <input type="button" value="X" onClick={this.deleteOperatorAttributeValue.bind(this, sId)}/>
      </div>
      <div>
        {aDom}
      </div>
    </div>));
  }
*/

  /**
   * @deprecated
   * @param aAttributeOperatorList
   * @returns {Array}
   */
/*
  getExpressionView=(aAttributeOperatorList)=> {
    var aOperatorView = [];
    var _this = this;
    CS.forEach(aAttributeOperatorList, function(oAttributeOperator){
      var sType = oAttributeOperator.type;
      var oDom = null;
      if(sType != "ATTRIBUTE" && sType != "VALUE"){
        oDom = _this.getOperatorView(oAttributeOperator);
      }else{
        oDom = _this.getAttributeOrValueView(oAttributeOperator);
      }
      aOperatorView.push(oDom);
    });
    return aOperatorView;
  }
*/

  /**
   * @deprecated
   * @param oElementData
   * @returns {*}
   */
/*
  getFormulaView =(oElementData)=>{

    var oOperatorView = this.getExpressionView(oElementData);

    return(
        <div>
          <div className="inputButtonWrapper elementWrapper">
            <input className="inputButton" type="button" value="+" onClick={this.addOperator.bind(this, "ADD")}/>
            <input className="inputButton" type="button" value="-" onClick={this.addOperator.bind(this, "SUBTRACT")}/>
            <input className="inputButton" type="button" value="*" onClick={this.addOperator.bind(this, "MULTIPLY")}/>
            <input className="inputButton" type="button" value="/" onClick={this.addOperator.bind(this, "DIVIDE")}/>
            <input className="inputButton" type="button" value="(" onClick={this.addOperator.bind(this, "OPENING_BRACKET")}/>
            <input className="inputButton" type="button" value=")" onClick={this.addOperator.bind(this, "CLOSING_BRACKET")}/>
            <input className="inputButton" type="button" value="A" onClick={this.addOperator.bind(this, "ATTRIBUTE")}/>
            <input className="inputButton" type="button" value="V" onClick={this.addOperator.bind(this, "VALUE")}/>
          </div>
          <div className="expressionWrapper elementWrapper">
            {oOperatorView}
          </div>
        </div>
    );
  }
*/

  getMappingSummaryView=(oElementData)=> {
    return (<MappingSummaryView
        configRuleMappings={oElementData.configRuleMappings}
        selectedMappingRows={oElementData.selectedMappingRows}
        summaryType={oElementData.summaryType}
        referencedData={oElementData.referencedData}
        lazyMSSReqResInfo={oElementData.lazyMSSReqResInfo}
        context={this.props.context}
        mappingType={oElementData.mappingType}
        isExportAllCheckboxClicked={oElementData.isExportAllCheckboxClicked}/>);
  }

  getPropertyGroupSummaryView = (oElementData, ToggleFlag) => {
    return (
        <PropertyGroupSummaryView
            elementData={oElementData}
            flag={ToggleFlag}
        />);
  }

  getContextSummaryView = (oElementData) => {
    return (
        <PropertyGroupSummaryView
            elementData={oElementData}
            flag={true}
        />
    );
  }

  getAuthorizationMappingSummaryView = (oElementData) => {
    return (
        <AuthorizationMappingSummaryView
            elementData={oElementData}
        />
    );
  }

  getMappingSummaryHeaderView=(oElementData)=> {
    return (
        <MappingSummaryHeaderView
            configRuleMappings={oElementData.configRuleMappings}
        />
    );
  }

  getProcessFrequencySummaryHeaderView=(oElementData)=> {
    return (
        <ProcessFrequencySummaryHeaderView
            selectedTabId={oElementData.selectedTabId}
            data={oElementData.data}
        />
    );
  }

  handleTabLayoutTabClicked = (sTabId) => {
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_MAPPING_SUMMARY_TAB_CHANGED, sTabId, this.props.context);
  };

  getFreqencyTabLayoutView=(oElementData)=> {
    let oTabView = null;
    oTabView = this.getProcessFrequencySummaryHeaderView(oElementData);

    return(
        <div className="tabLayoutViewBasicWithDarkBackgound">
          <TabLayoutView
              activeTabId={oElementData.selectedTabId}
              tabList={oElementData.tabHeaderData}
              addBorderToBody={true}
              onChange={this.handleTabLayoutTabClicked}
          >
            {oTabView}
          </TabLayoutView>
        </div>);
  };

  getTabLayoutView=(oElementData)=> {
    let oTabView = null;
    if(this.props.context === "authorization_mapping"){
        oTabView = this.getAuthorizationMappingSummaryView(oElementData);
    } else {
      if (oElementData.selectedTabId === MappingTabHeaderData.propertyCollection) {
        oTabView = this.getPropertyGroupSummaryView(oElementData, oElementData.bIsPropertyCollectionToggleFlag);
      } else if (oElementData.selectedTabId === MappingTabHeaderData.contextTag) {
        oTabView = this.getContextSummaryView(oElementData);
      } else {
        oTabView = this.getMappingSummaryView(oElementData);
      }
    }
    return(
      <div className="tabLayoutViewBasicWithDarkBackgound">
        <TabLayoutView
            activeTabId={oElementData.selectedTabId}
            tabList={oElementData.tabHeaderData}
            addBorderToBody={true}
            onChange={this.handleTabLayoutTabClicked}
        >
          {oTabView}
        </TabLayoutView>
      </div>);
  };

  getFroalaView=(sId, sValue, aStyle, fHandler)=>{

    var aAllowedRTEIcons = MockDataIdsForFroalaView;
    return (
    AutoSize(<FroalaWrapperView dataId={sId}
                           activeFroalaId={sId}
                           content={sValue}
                           // className={sFroalaWrapperClassName}
                           toolbarIcons={aAllowedRTEIcons}
                           showPlaceHolder={false}
                           handler={fHandler}/>)
    )
  }

/*
  getConcatenatedBodyDOM=(aConcatData)=> {
    var aDom = [];
    var _this = this;

    CS.forEach(aConcatData, function (oConcat) {
      var oCrossIconDom = (
          <div className="crossIconWrapper">
            <TooltipView placement="bottom" label={getTranslation().REMOVE}>
              <div className="crossIcon" onClick={_this.handleConcatObjectRemoveClicked.bind(_this, oConcat.id)}></div>
            </TooltipView>
          </div>);

      var sConcatType = oConcat.type;
      var fHandler = _this.handleConcatInputChanged.bind(_this, oConcat);
      if (sConcatType == "string") {
        aDom.push(
            <div className="inputDomWrapper">
              <AutosizeInput value={oConcat.value}
                             name="form-field-name"
                             className={"autoSizeContainer"}
                             inputClassName={"concatInput"}
                             onChange={fHandler}/>
              {oCrossIconDom}
            </div>);
      } else if (sConcatType == "html"){
        aDom.push(
            <div className="froalaDomWrapper">
              {_this.getFroalaView(oConcat.id, oConcat.valueAsHtml, [], fHandler)}
              {oCrossIconDom}
            </div>);
      } else if (sConcatType == "attribute"){
        aDom.push(
            <div className="selectDomWrapper">
              {_this.getAttributeDropdownView(oConcat, "concatenated", true)}
              {oCrossIconDom}
            </div>);
      }
    });


    return aDom
  }
*/

/*
  getConcatenatedFormulaView=(aElementData)=> {
    var oConcatenatedBodyDOM = this.getConcatenatedBodyDOM(aElementData);

    return(
        <div className="concatenatedFormulaWrapper elementWrapper">
          <div className="concatenatedFormulaButtons">

            {/!*<TooltipView placement="bottom" label={getTranslation().ADD_TEXT}>
              <div className="concatButton addString" onClick={this.addConcatObject.bind(this,"string")}>{getTranslation().TEXT}</div>
            </TooltipView>*!/}

            <TooltipView placement="bottom" label={getTranslation().ADD_HTML_TEXT}>
              <div className="concatButton addHTML" onClick={this.addConcatObject.bind(this,"html")}>{getTranslation().HTML}</div>
            </TooltipView>

            <TooltipView placement="bottom" label={getTranslation().ADD_ATTRIBUTE}>
              <div className="concatButton addAttribute" onClick={this.addConcatObject.bind(this,"attribute")}>{getTranslation().ATTRIBUTE}</div>
            </TooltipView>

          </div>
          <div className="concatenatedFormulaBody">
            {oConcatenatedBodyDOM}
          </div>
        </div>
    );
  }
*/

  getCustomView=(oElementData)=> {
    return(
        oElementData
    );
  }

  handleUploadOrReplaceTemplateClicked = (oElementData, oEvent) => {
    var aFiles = oEvent.target.files;
    EventBus.dispatch(oEvents.COMMON_CONFIG_SECTION_TEMPLATE_ACTION_CLICKED, this, oElementData, aFiles);
    oEvent.target.value = "";
  };

  handleUploadOrReplaceTemplateActionClicked = () => {
    this.iconUpload.current.click();
  };

  getUploadButtonDOM = (oElementData, sActionClassName, sUploadCommonConfigIconString) => {
    return (
        <div className={"uploadTemplateView templateIcon"}>
            <div className={sActionClassName} title={sUploadCommonConfigIconString}
                 onClick={this.handleUploadOrReplaceTemplateActionClicked.bind(this)}>
            </div>
            <input style={{"visibility": "hidden", height: "0", width: "0", position: "absolute", top: "0"}}
                   ref={this.iconUpload}
                   onChange={this.handleUploadOrReplaceTemplateClicked.bind(this, oElementData)}
                   type="file"
                   accept="application/zip"/>
        </div>
    );
  };

  getTemplateUploadView = (oElementData) => {
      let sActionClassName = "uploadTemplateActionIcon ";
      let sDownloadCommonConfigIconString = getTranslation().DOWNLOAD_TEMPLATE;
      let sUploadCommonConfigIconString = getTranslation().UPLOAD_TEMPLATE;
      let oDownloadButtonDOM = null;

      if (CS.isEmpty(oElementData)) {
          sActionClassName += "upload";
      }
      else {
          sActionClassName += " replace";
          sUploadCommonConfigIconString = getTranslation().REPLACE_TEMPLATE;
          oDownloadButtonDOM = (
              <div className={"downloadTemplateView templateIcon"}>
                  <a className={"iconDownloadImage "}
                     href={"asset/SDTemplates/" + oElementData + "?download=icon"} title={sDownloadCommonConfigIconString}
                     target="_blank">
                  </a>
              </div>
          );
      }

      return (
          <div className={"uploadAndDownloadTemplateViewWrapper"}>
              {this.getUploadButtonDOM(oElementData, sActionClassName, sUploadCommonConfigIconString)}
              {oDownloadButtonDOM}
          </div>
      );
  };

  getColorPickerView = (oElementData) => {
    return (
        <ColorPickerView color={oElementData.color} context={oElementData.context}/>
    );
  };

  getElementView=(oElement)=> {
    var oData = this.props.data;
    var sKey = oElement.key;
    var sType = oElement.type;
    var oElementData = oData[sKey];
    let sHintText = (sKey === "label") ? oData["code"] : (oElement.hintText) ? oElement.hintText : "";
    // if (oElement.key == "type" && sType === "mss" && oElementData != null){
    //   oElementData.disabled = true;
    // }
    var bDataExists = CS.isString(oElementData) ? true : oElementData;
    var oElView = null;

    switch (sType){

      case "singleText":
        oElView = bDataExists && this.getSingleTextView(oElementData, sKey, sHintText);
        break;

      case "multiText":
        oElView = bDataExists && this.getMultiTextView(oElementData, sKey);
        break;

      case "mss":
        oElView = bDataExists && this.getNewMSSView(oElementData, sKey);
        break;

      case "mssNew":
        oElView = bDataExists && this.getNewMSSView(oElementData, sKey);
        break;

      case "lazyMSS":
        oElView = bDataExists && this.getLazyMSSView(oElementData, sKey);
        break;

      case "image":
        oElView = bDataExists && this.getImageView(oElementData);
        break;

      case "yesNo":
        oElView = bDataExists && this.getYesNoView(oElementData, sKey);
        break;

      case "yesNeutral":
        oElView = bDataExists && this.getYesNeutralView();
        break;

      case "yesNeutralNo":
        oElView = bDataExists && this.getYesNoNeutralView();
        break;

      case "range" :
        oElView = bDataExists && this.getRangeView();
        break;

      case "custom":
        oElView = bDataExists && this.getCustomTagView(oElementData);
        break;

      case "singleTextNumber":
        oElView = (bDataExists || CS.isNumber(bDataExists)) && this.getSingleTextNumberView(oElementData, oElement);
        break;

      case "yesNoBoth":
          var bAttributeCheckFlag = false;
        if(!CS.isEmpty(oData.type)){
          bAttributeCheckFlag = oData.type.context == "attributeType";
        }
        oElView = (bAttributeCheckFlag || oData.isTagGroup) && this.getYesNoBothView(oData, sKey);
        break;

      case "selectionToggle":
        oElView = bDataExists && this.getSelectionToggleView(oElementData, sKey);
        break;

      case "nativeDropdown":
        oElView = bDataExists && this.getNativeDropdownView(sKey, oElementData);
        break;

      /*case "formula":
        oElView = bDataExists && this.getFormulaView(oElementData);
        break;*/

     /* case "concatenatedFormula":
        oElView = bDataExists && this.getConcatenatedFormulaView(oElementData);
        break;*/

      case "customView":
        oElView = bDataExists && this.getCustomView(oElementData);
        break;

      case "froalaView":
        oElView = bDataExists && this.getFroalaView(sKey,oElementData,[],this.handleFroalaTextChanged.bind(this,sKey));
        break;

      case "mappingSummary":
        oElView = bDataExists && this.getMappingSummaryView(oElementData);
        break;

      case "tabSummary":
        oElView = bDataExists && this.getTabLayoutView(oElementData);
        break;

      case "mappingSummaryHeader":
        oElView = bDataExists && this.getMappingSummaryHeaderView(oElementData);
        break;

     /* case ViewContextConstants.RULE_DETAILS_VIEW_CONFIG:
        oElView = bDataExists && this.getRuleDetailView(oElementData);
        break;*/

      case "zipTemplateId" :
        oElView = bDataExists && this.getTemplateUploadView(oElementData);
        break;

      case "colorPicker" :
        oElView = bDataExists && this.getColorPickerView(oElementData);
        break;

        case "radioButton":
            oElView = bDataExists && this.getRadioButtonView(oElementData, oElement);
            break;

      case "frequencyTabSummary":
        oElView = bDataExists && this.getFreqencyTabLayoutView(oElementData);
        break;
    }

    var oStyle = {width: "calc(" + oElement.width + "% - 10px)"};

    if(oElView){
      return  (<div className="commonConfigSectionElement">
            <div className="commonConfigSectionElementHeader">{oElement.label}</div>
            <div className="commonConfigSectionElementBody">{oElView}</div>
          </div>);

    }else {
      return null;
    }

  }

  getRadioButtonView = (oElementData, oElement) => {

        let aRadioButtonsContainer = [];

        oElement.elements.forEach(element => {

            let isSelected = element.key === ThemeConfigurationConstants.EXPAND ? oElementData.radio : !oElementData.radio;

            aRadioButtonsContainer.push(
                <div className="component">
                    <div className="element rightMargin">{element.label}</div>
                    <div className="element">
                        <RadioButtonView selected={isSelected} context={oElementData.context}
                                         clickHanlder={this.handleRadioButtonChanged.bind(this, oElementData.context, element.key)}/>
                    </div>
                </div>
            )
        });

        return (
            <div className="sidePanelContainer">
                <div className="centerContent">
                    {aRadioButtonsContainer}
                </div>
            </div>
        );
    };

  getView =()=>{

    var _this = this;
    var aSections = this.props.sectionLayout;
    var aDisabledKeys = this.props.disabledFields || [];
    var aSectionView = [];

    let iElementKeyCounter = 1;
    let iRowKeyCounter = 1;
    let iSectionKeyCounter = 1;

    CS.forEach(aSections, function(oSection){
      var aElView = [];
      var iPercentSum = 0;
      var aRowView = [];

      CS.forEach(oSection.elements, function(oElement){
        var oElView = _this.getElementView(oElement);

        if(oElView) {
          if (iPercentSum + oElement.width > 100) {
            aElView.length && aRowView.push(<div className="commonConfigSectionRow" key={iRowKeyCounter++}>{aElView}</div>);
            aElView = [];
            iPercentSum = oElement.width;
          } else {
            iPercentSum += oElement.width;
          }
          var oOverlayView = null;
          if (CS.includes(aDisabledKeys, oElement.key) && CS.includes(aDisableViaOverlayFieldTypes, oElement.type)) {
            oOverlayView = (<div className="commonConfigOverlay"></div>);
          }
          aElView.push(<div className="commonConfigSectionElementWrapper" key={iElementKeyCounter++}>
            {oOverlayView}
            {oElView}
          </div>);
        }
      });

      aElView.length && aRowView.push(<div className="commonConfigSectionRow" key={iRowKeyCounter++}>{aElView}</div>);

      aRowView.length && aSectionView.push(<div className="commonConfigSection" key={iSectionKeyCounter++}>
        <div className="commonConfigSectionHeader"></div>
        <div className="commonConfigSectionBody">{aRowView}</div>
      </div>);
    });

    return aSectionView;
  }

  render() {

    return (
        <div className="commonConfigSectionViewContainer">
          {this.props.data && this.getView()}
          {this.props.data && this.props.data.showSelectIconDialog && this.showSelectIconDialog()}
        </div>
    );
  }
}

CommonConfigSectionView.propTypes = oPropTypes;


export const view = CommonConfigSectionView;
export const events = oEvents;
