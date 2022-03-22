import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomTextFieldView } from '../customtextfieldview/custom-text-field-view';
import { view as NumberLocaleView } from '../numberlocaleview/number-locale-view';
import { view as CustomDatePicker } from '../customdatepickerview/customdatepickerview.js';
import { view as FroalaWrapperView } from '../customfroalaview/froala-wrapper-view';
import MasterUserListContext from '../../commonmodule/HOC/master-user-list-context';
import { view as ContentMeasurementMetricsView } from '../measurementmetricview/content-measurement-metrics-view.js';
import ContentMeasurementMetricsViewModel from '../measurementmetricview/model/content-measurement-metrics-view-model';
import { view as ContentConcatenatedAttributeView } from '../concatenatedattributeview/content-concatenated-attribute-view';
import ContentConcatenatedAttributeViewModel from '../concatenatedattributeview/model/content-concatenated-attribute-view-model';
import ContentAttributeElementViewModel from './model/content-attribute-element-view-model';
import MockDataForMeasurementMetricAndImperial from './../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ViewUtils from './../utils/view-library-utils';
import { view as AttributeContextView } from '../attributecontextview/attribute-context-view';
import Constants from '../../commonmodule/tack/constants';

const oEvents = {};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(ContentAttributeElementViewModel).isRequired,
  onChangeHandler: ReactPropTypes.func,
  onBlurHandler: ReactPropTypes.func,
  filterContext: ReactPropTypes.object,
  masterUserList: ReactPropTypes.array,
  customPlaceholder: ReactPropTypes.string
};
/**
 * @class ContentAttributeElementView - use for display attribute view.
 * @memberOf Views
 * @property {custom} model - model name.
 * @property {func} [onChangeHandler] -  function which used onChangeHandler event.
 * @property {func} [onBlurHandler] -  function which used onBlurHandler event.
 */

// @MasterUserListContext
// @CS.SafeComponent
class ContentAttributeElementView extends React.Component {

  constructor(props) {
    super(props);
  }

  handleOnChangeHandler = (valueData, newDate) => {
    var oData = this.getDataForHandler(valueData, newDate);
    this.props.onChangeHandler.call(this, oData);
  }

  handleOnBlurHandler = (valueData, newDate) => {
    var oData = this.getDataForHandler(valueData, newDate);
    this.props.onBlurHandler.call(this, oData);
  }

  getDataForHandler = (valueData, secondParameter) => {
    var oModel = this.props.model;
    var oMasterAttribute = oModel.masterAttribute;
    var sType = oMasterAttribute.type;

    var sValue = valueData;
    var sValueAsHtml = "";
    var sExpressionId = "";

    if (ViewUtils.isAttributeTypeDate(sType)){
      if(secondParameter !== ""){
        sValue = Date.parse(secondParameter);
      }
    }
    else if(ViewUtils.isAttributeTypeHtml(sType)){ // For HTML type attribute
      sValueAsHtml = valueData.htmlValue;
      sValue = valueData.textValue;
    }
    else if(ViewUtils.isAttributeTypeConcatenated(sType)){
      sExpressionId = secondParameter;
      sValueAsHtml = valueData.htmlValue;
      sValue = valueData.textValue;
    }
    else if(valueData.target){
      sValue = valueData.target.value;
    }

    var oData = {
      value: sValue,
      valueAsHtml: sValueAsHtml,
      expressionId: sExpressionId
    };

    var iPrecision = oModel.properties["precision"];
    if (CS.isNumber(iPrecision)) {
      oData.precision = iPrecision;
    }

    return oData;
  }

  getMeasurementMetricView = () => {
    var sSplitter = ViewUtils.getSplitter();

    var fHandler = this.props.onBlurHandler ? this.handleOnBlurHandler : this.handleOnChangeHandler;
    var oModel = this.props.model;

    var bDisabled = oModel.isDisabled;
    var sValue = oModel.value;

    var oMasterAttribute = oModel.masterAttribute;
    var iPrecision = Number(oMasterAttribute["precision"]);
    var sPlaceholder = oMasterAttribute.placeholder || (this.bIsDefaultPlaceholderVisible ? getTranslation().ENTER_HERE : this.props.customPlaceholder || "");
    var sDescription = oModel.properties["hideDescription"] ? "" : oMasterAttribute.description;
    var sDefaultUnit = oMasterAttribute.defaultUnit;
    var sType = oMasterAttribute.type;
    var oProperties = {};
    let bConverterVisibility = !ViewUtils.isAttributeTypePrice(sType);

    var sLabel = CS.getLabelOrCode(oModel);
    var sRef = oModel.ref || "attr" + sSplitter + oModel.id + sSplitter + oMasterAttribute.id;

    if(ViewUtils.isAttributeTypeCalculated(sType)){
      sType = oMasterAttribute.calculatedAttributeType;
      sDefaultUnit = oMasterAttribute.calculatedAttributeUnit;
    }
    let oMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
    var aConverterList = oMeasurementMetricAndImperial[sType];
    CS.forEach(aConverterList,function (oConverterItem) {
      oConverterItem.unitToDisplay = oConverterItem.unitToDisplay +' (' + CS.getLabelOrCode(oConverterItem) +')'
    });
    var oBaseUnit = CS.find(aConverterList, {isBase: true});
    var sBaseUnit = oBaseUnit ? oBaseUnit.unit : sDefaultUnit;

    oProperties.disableValueChangeByDefaultUnit = ViewUtils.isAttributeTypePrice(sType);

    if(ViewUtils.isMeasurementAttributeTypeCustom(sType)){
      oProperties.defaultUnitAsHTML = oMasterAttribute.defaultUnitAsHTML || oMasterAttribute.calculatedAttributeUnitAsHTML;
    }else{
      oProperties.baseUnit = (CS.find(oMeasurementMetricAndImperial[sType], {isBase: true}))["unit"];
    }

    var oSectionElementDetails = oModel.properties["sectionElementDetails"];
    if (!CS.isEmpty(oSectionElementDetails)) {
      oProperties.sectionElementDetails = oSectionElementDetails;
    }
    var sDefaultUnitProperty = oModel.properties["defaultUnit"];
    if (!CS.isEmpty(sDefaultUnitProperty)) {
      sDefaultUnit = sDefaultUnitProperty;
      oProperties.useDefaultUnitFromModel = true;
    }
    var iPrecisionProperty = oModel.properties["precision"];
    if (CS.isNumber(iPrecisionProperty)) {
      iPrecision = iPrecisionProperty;
    }
    oProperties.disableNumberLocaleFormatting = oMasterAttribute.hideSeparator;

    var oMeasurementMetricModel = new ContentMeasurementMetricsViewModel(
        oModel.id,
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
                                           onBlur={fHandler}
                                           model={oMeasurementMetricModel}/>);
  }

  getConcatenatedAttributeView = () => {
    var sSplitter = ViewUtils.getSplitter();

    var fHandler = this.props.onBlurHandler ? this.handleOnBlurHandler : this.handleOnChangeHandler;
    var oModel = this.props.model;

    var bDisabled = oModel.isDisabled;
    var sValue = oModel.value;
    var sErrorText = oModel.errorText || "";

    var oMasterAttribute = oModel.masterAttribute;
    var sPlaceholder = oMasterAttribute.placeholder || (this.bIsDefaultPlaceholderVisible ? getTranslation().ENTER_HERE : this.props.customPlaceholder || "");
    var sDescription = oModel.properties["hideDescription"] ? "" : oMasterAttribute.description;

    var sLabel = CS.getLabelOrCode(oModel);
    var sRef = oModel.ref || "attr" + sSplitter + oModel.id + sSplitter + oMasterAttribute.id;

    var aExpression = oModel.properties["expressionList"];
    var bShowDisconnected = oModel.properties["showDisconnected"];
    var oProperties = {};

    var oConcatModel = new ContentConcatenatedAttributeViewModel(
        oModel.id,
        sLabel,
        sValue,
        sPlaceholder,
        sErrorText,
        sDescription,
        bDisabled,
        bShowDisconnected,
        aExpression,
        oProperties
    );

    return (<ContentConcatenatedAttributeView ref={sRef}
                                              key={sRef}
                                              onChange={fHandler}
                                              model={oConcatModel}/>);
  }

  getHTMLFieldView = () =>{
    const sSplitter = ViewUtils.getSplitter();

    let fHandler = null;
    if(this.props.onBlurHandler){
      fHandler = this.handleOnBlurHandler;
    }else if(this.props.onChangeHandler){
      fHandler = this.handleOnChangeHandler;
    }

    let oModel = this.props.model;

    let bDisabled = oModel.isDisabled;
    let sValue = oModel.value;

    if(CS.isObject(sValue)) {
      sValue = sValue && sValue.htmlValue ? sValue.htmlValue : sValue;
    }

    let oMasterAttribute = oModel.masterAttribute;
    let sPlaceholder = oMasterAttribute.placeholder  || (this.bIsDefaultPlaceholderVisible ? getTranslation().ENTER_HERE : this.props.customPlaceholder || "");
    let iMaxCharacterLimit = oMasterAttribute.characterLimit || Constants.HTML_DEFAULT_MAX_CHARACTER_LIMIT;

    let aAllowedRTEIcons = [];
    if(!CS.isEmpty(oMasterAttribute.validator)) {
      aAllowedRTEIcons = oMasterAttribute.validator.allowedRTEIcons;
    }

    let sSectionId = oModel.properties["sectionId"] || "noSectionId";
    let sContext = oModel.properties["context"] || "noContext";
    let sElementId = oModel.properties["elementId"] || "noElementId";
    let sAttributeId = oModel.id || oMasterAttribute.id;
    let sDataId = "attribute" + sSplitter + sContext + sSplitter + sElementId + sSplitter + sAttributeId + sSplitter + sSectionId;
    let sActiveFroalaId = bDisabled ? "" : sDataId;

    return (
      <div className="attributeElementFroalaWrapper">
        <FroalaWrapperView dataId={sDataId}
                           activeFroalaId={sActiveFroalaId}
                           content={sValue}
                           maxCharacterLimit={iMaxCharacterLimit}
                           toolbarIcons={aAllowedRTEIcons}
                           placeholder={sPlaceholder}
                           showPlaceHolder={!!sPlaceholder}
                           handler={fHandler}
                           filterContext={this.props.filterContext}/>
      </div>);
  };

  getValueWithPrecision = (sValue, iPrecision) => {

    if(CS.includes(sValue, 'E')) {
      sValue = ViewUtils.getValueWithoutExponent(sValue);
    }

    if (sValue.includes(".")) {
      let sSplitter = ".";
      let aSplits = sValue.split(sSplitter);
      if(iPrecision == 0) {
        sValue = aSplits[0];
      }
      if (aSplits[1].length <= iPrecision) {
        return sValue;
      }
      else {
        let sValueAfterDecimal = aSplits[1].substring(0, iPrecision);
        sValue = aSplits[0] + sSplitter + sValueAfterDecimal;
      }
    }
    return sValue;
  };

  getTextFieldView = () => {
    let aUserList = this.props.masterUserList;

    let fOnChangeHandler = this.props.onChangeHandler ? this.handleOnChangeHandler : null;
    let fOnBlurHandler = this.props.onBlurHandler ? this.handleOnBlurHandler : null;
    let oModel = this.props.model;

    let bDisabled = oModel.isDisabled;
    let sValue = oModel.value;
    if (sValue === "null" || sValue == null || sValue == "NaN") {
      sValue = "";
    }

    let oMasterAttribute = oModel.masterAttribute;
    let sPlaceholder = oMasterAttribute.placeholder || (this.bIsDefaultPlaceholderVisible ? getTranslation().ENTER_HERE : this.props.customPlaceholder || "");
    let sDescription = oModel.properties["hideDescription"] ? "" : oMasterAttribute.description;
    let sType = oMasterAttribute.type;

    let sLabel = CS.getLabelOrCode(oModel);
    let bMultiLine = oModel.properties["isMultiLine"];

    if (ViewUtils.isAttributeTypeUser(sType)) {
      sValue = ViewUtils.getUserNameById(sValue, aUserList);
    }

    let sMsgToDisplay = sDescription;

    if (ViewUtils.isAttributeTypeCalculated(sType)) {
      let iPrecision = Number(oMasterAttribute.precision);
      sValue = this.getValueWithPrecision(sValue, iPrecision);
      bDisabled = true;
    }

    return (
          <CustomTextFieldView
              value={sValue}
              onChange={fOnChangeHandler}
              onBlur={fOnBlurHandler}
              label={sLabel}
              hintText={sPlaceholder}
              errorText={sMsgToDisplay}
              isDisabled={bDisabled}
              hideTooltip={true}
              isMultiLine={bMultiLine}
              showLabel={this.props.showLabel}
              showDescription={this.props.showDescription}
          />)
  };

  getNumberFieldView = () => {
    var fOnChangeHandler = this.props.onChangeHandler ? this.handleOnChangeHandler : null;
    var fOnBlurHandler = this.props.onBlurHandler ? this.handleOnBlurHandler : null;
    var oModel = this.props.model;

    var bDisabled = oModel.isDisabled;
    var sValue = oModel.value;
    if (sValue === "null" || sValue == null || sValue == "NaN") {
      sValue = "";
    }

    var oMasterAttribute = oModel.masterAttribute;
    var sType = oMasterAttribute.type;
    var iPrecision = oMasterAttribute.precision;
    var iPrecisionProperty = oModel.properties["precision"];
    if (CS.isNumber(iPrecisionProperty)) {
      iPrecision = iPrecisionProperty;
    }
    var sPlaceholder = oMasterAttribute.placeholder || (this.bIsDefaultPlaceholderVisible ? getTranslation().ENTER_HERE : this.props.customPlaceholder || "");
    var sDescription = oModel.properties["hideDescription"] ? "" : oMasterAttribute.description;
    var sLabel = CS.getLabelOrCode(oModel);
    var sMsgToDisplay = sDescription;
    var oErrorStyle = {color: '#555', borderWidth: '1px', fontStyle: 'italic'};
    var bIsTelephone = false;
    var bNegativeAllowed = true;
    let bDisableNumberLocaleFormatting = oMasterAttribute.hideSeparator;

    if (ViewUtils.isAttributeTypeTelephone(sType)) {
      bIsTelephone = true;
      bNegativeAllowed = false;
      iPrecision = 0;
    }

    return (
      <NumberLocaleView
          value={sValue}
          onChange={fOnChangeHandler}
          onBlur={fOnBlurHandler}
          label={sLabel}
          hintText={sPlaceholder}
          errorText={sMsgToDisplay}
          errorStyle={oErrorStyle}
          isDisabled={bDisabled}
          negativeAllowed={bNegativeAllowed}
          hideTooltip={true}
          precision={iPrecision}
          isTelePhone={bIsTelephone}
          disableNumberLocaleFormatting={bDisableNumberLocaleFormatting}
      />);

        //Keep this commented code
        /*  <div className="textFieldWrapperForDescription">
            <CustomTextFieldView
                value={sValue}
                onChange={fOnChangeHandler}
                onBlur={fOnBlurHandler}
                label={sLabel}
                hintText={sPlaceholder}
                errorText={sMsgToDisplay}
                isDisabled={bDisabled}
                hideTooltip={true}
                isMultiLine={bMultiLine}
            />
            <TextField
                hintText={sPlaceholder}
                floatingLabelText={sLabel}
                disabled={bDisabled}
                ref={sRef}
                key={sRef}
                className={sClassName}
                fullWidth={true}
                errorStyle={oErrorStyle}
                inputStyle={oCustomStyles.inputStyles}
                multiLine={bMultiLine}
                type={sInputType}
                floatingLabelStyle={oCustomStyles.floatingLabelStyles}
                onChange={fOnChangeHandler}
                onBlur={fOnBlurHandler}
                defaultValue={sValue}
                underlineStyle={oUnderlineStyle}
                underlineFocusStyle={oUnderlineFocusStyle}
            />
            <div className="textFieldDescription">
              {sMsgToDisplay}
            </div>
          </div>)
    }*/
  }

  getDateFieldView = () =>{
    let oModel = this.props.model;
    let fHandler = this.props.onBlurHandler ? this.handleOnBlurHandler : this.handleOnChangeHandler;
    let sValue = oModel.value;
    // var sLabel = oModel.label;
    let sRef = oModel.ref;
    let bDisabled = oModel.isDisabled;

    let oMasterAttribute = oModel.masterAttribute;
    let sPlaceholder = oMasterAttribute.placeholder || ((this.bIsDefaultPlaceholderVisible && CS.isEmpty(sValue.toString())) ? getTranslation().ENTER_HERE : this.props.customPlaceholder || "");
    let sTooltip = oMasterAttribute.tooltip;

    sValue = sValue ? +sValue ? new Date(+sValue) : null : null;
    if (CS.isNaN(Date.parse(sValue))) {
      sValue = null;
    }

    let oStyle = {width: "100%"};
    let oTextFieldStyle = {width: "100%"};
    let oUnderlineStyle = {border: 'none'};
    return (
        <div className="attributeElementDateWrapper">
          <CustomDatePicker
              value={sValue}
              ref={sRef}
              key={sRef}
              className="datePickerCustom"
              defaultDate={sValue}
              disabled={bDisabled}
              onChange={fHandler}
              style={oStyle}
              hintText={sPlaceholder}
              tooltip={sTooltip}
              hideTooltip={true}
              textFieldStyle={oTextFieldStyle}
              underlineStyle={oUnderlineStyle}
          />
        </div>
    )
  };

  getAttributeContextView = (oMaster, sAttributeVariantContext, oAttributeVariantsStats, oModel) => {
    let sAttributeId = oMaster.id;
    let oAttributeContextViewData = oModel && oModel.properties && oModel.properties.attributeContextViewData || {};
    let sVariantInstanceId = oModel && oModel.properties && oModel.properties.variantInstanceId || "";
    let sParentContextId = oModel && oModel.properties && oModel.properties.parentContextId || "";
    oAttributeContextViewData.variantInstanceId = sVariantInstanceId;
    oAttributeContextViewData.parentContextId = sParentContextId;

    return (
        <AttributeContextView
            attributeId={sAttributeId}
            attributeVariantContextId={sAttributeVariantContext}
            attributeVariantsStats={oAttributeVariantsStats}
            extraViewData={oAttributeContextViewData}
            filterContext={this.props.filterContext}
            isDisable={oModel.isDisabled}
        />
    );
  }

  getAttributeView = () => {
    let oModel = this.props.model;
    let oMaster = oModel.masterAttribute;

    let sType = oMaster.type;
    let sViewAttributeType = oModel.properties.viewAttributeType;
    this.bIsDefaultPlaceholderVisible = !(oModel.isDisabled || ViewUtils.isAttributeTypeCalculated(sType));
    let sAttributeVariantContext = oModel.properties["attributeVariantContext"];
    let oAttributeVariantsStats = oModel.properties.attributeVariantsStats || {};

    if(sAttributeVariantContext){
      return this.getAttributeContextView(oMaster, sAttributeVariantContext, oAttributeVariantsStats, oModel);
    }
    else if (ViewUtils.isAttributeTypeNumber(sViewAttributeType)
        || ViewUtils.isAttributeTypeNumber(sType)
        || ViewUtils.isAttributeTypeTelephone(sType)) {
      return this.getNumberFieldView();
    }
    else if (ViewUtils.isAttributeTypeDate(sType)) {
      return this.getDateFieldView();
    }
    else if (ViewUtils.isAttributeTypeHtml(sType)) {
      return this.getHTMLFieldView();
    }
    else if (ViewUtils.isAttributeTypeMeasurement(sType)) {
      return this.getMeasurementMetricView();
    }
    else if (ViewUtils.isAttributeTypeCoverflow(sType)) {
      console.log("Changes done by Vidit in the refactoring. Please contact if you have problem" +
        " viewing Image Gallery.")
    }
    else if (ViewUtils.isAttributeTypeConcatenated(sType)) {
      return this.getConcatenatedAttributeView();
    }
    else if (ViewUtils.isAttributeTypeCalculated(sType)) {
      if(oMaster.calculatedAttributeType){
        return  this.getMeasurementMetricView();
      }else{
        return this.getNumberFieldView();
      }
    }
    else {
      return this.getTextFieldView();
    }

  };

  handleOnKeyDown = (oEvent) => {
    if (oEvent.keyCode == 9 || oEvent.keyCode == 27) {
      oEvent.target.blur();
    }
  }

  render() {
    return (
        <div className="contentAttributeElementViewWrapper" onKeyDown={this.handleOnKeyDown}>{this.getAttributeView()}</div>
    );
  }
}

ContentAttributeElementView.propTypes = oPropTypes;
export const events = oEvents;
export const view = MasterUserListContext(ContentAttributeElementView);
