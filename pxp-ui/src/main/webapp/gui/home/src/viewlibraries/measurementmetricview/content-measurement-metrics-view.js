import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import MetricCheckConstants from '../../commonmodule/tack/measurement-metrics-constants.js';
import AttributeTypeDictionary from '../../commonmodule/tack/attribute-type-dictionary-new.js';
import ContentMeasurementMetricsViewModel from './model/content-measurement-metrics-view-model';
import MeasurementMetrics from '../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import ViewLibraryUtils from '../utils/view-library-utils';
import ToolTip from '../tooltipview/tooltip-view';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import { view as NumberLocaleView } from '../numberlocaleview/number-locale-view';

const oEvents = {
  METRIC_LENGTH_EXCEEDED : "Entered value exceeds the maximum value",
  METRIC_UNIT_CHANGED: "metric_unit_changed"
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(ContentMeasurementMetricsViewModel).isRequired,
  onBlur: ReactPropTypes.func
};
/**
 * @class ContentMeasurementMetricsView -
 * @memberOf Views
 * @property {custom} model - Model contains attributeId, label, value, default unit, base unit, placeholder, errorText,
 * description, isDisabled, converterVisibility, converterList, properties, precision.
 * @property {func} [onBlur] - onblur event executes when an object loses focus.
 */

let shouldSetState;
// @CS.SafeComponent
class ContentMeasurementMetricsView extends React.Component {

  constructor(props) {
    super(props);

    shouldSetState = false;

    var oModel = this.props.model;
    let oProperties = oModel.properties;
    var sDefaultUnitAsHTML = oProperties["defaultUnitAsHTML"];
    var sDefaultUnit = oModel.defaultUnit;
    var sBaseUnit = oModel.baseUnit;
    var sValue = oModel.value;
    var sPrecision = oModel.precision;
    var sValueToShow = sValue;
    if(sDefaultUnitAsHTML == null){
      sValueToShow = this.getMeasurementAttributeValueToShow(oModel, sValue, sBaseUnit, sDefaultUnit, sPrecision);
    }
    this.state = {
      value: sValueToShow,
      defaultUnit: sDefaultUnit,
      model: this.props.model
    }
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    let oNewModel = oNextProps.model;
    let oOldModel = oState.model;

    if (oNewModel.value !== (oOldModel && oOldModel.value)
      || oOldModel.defaultUnit !== oNewModel.defaultUnit
      || oOldModel.precision !== oNewModel.precision
    ) {

      let oProperties = oNewModel.properties;
      let sDefaultUnitAsHTML = oProperties["defaultUnitAsHTML"];
      let sSelectedUnitFromState = oState.defaultUnit;
      let sSelectedUnit = oNewModel.defaultUnit;
      if (oNewModel.converterVisibility && !oProperties['useDefaultUnitFromModel']) {
        sSelectedUnit = sSelectedUnitFromState;
      }
      let sBaseUnit = oNewModel.baseUnit;
      let sValue = oNewModel.value;
      let sPrecision = oNewModel.precision;
      let sValueToShow = sValue;
      let bDisableValueConversionOnUnitChange = oProperties.hasOwnProperty("disableValueChangeByDefaultUnit") ?
                                                oProperties.disableValueChangeByDefaultUnit : false;

      if (sDefaultUnitAsHTML == null && !bDisableValueConversionOnUnitChange) {
        sValueToShow = ViewLibraryUtils.getMeasurementAttributeValueToShow(sValue, sBaseUnit, sSelectedUnit, sPrecision);
      }
      return {
        value: sValueToShow,
        defaultUnit: sSelectedUnit,
        model: oNewModel
      };
    }
    return null;
  }

 /* componentWillReceiveProps(nextProps) {

    var oNewModel = nextProps.model;
    var oOldModel = this.props.model;

    if (oNewModel.value !== (oOldModel && oOldModel.value)) {

      var sDefaultUnitAsHTML = oNewModel.properties["defaultUnitAsHTML"];
      var sSelectedUnitFromState = this.state.defaultUnit;
      var sSelectedUnit = oNewModel.defaultUnit;
      if (oNewModel.converterVisibility && !oNewModel.properties['useDefaultUnitFromModel']) {
        sSelectedUnit = sSelectedUnitFromState;
      }
      var sBaseUnit = oNewModel.baseUnit;
      var sValue = oNewModel.value;
      var sPrecision = oNewModel.precision;
      var sValueToShow = sValue;
      if (sDefaultUnitAsHTML == null) {
        sValueToShow = ViewLibraryUtils.getMeasurementAttributeValueToShow(sValue, sBaseUnit, sSelectedUnit, sPrecision);
      }
      this.setState({value: sValueToShow, defaultUnit: sSelectedUnit});
    }
  }*/

  testNumber =(sNumber)=> {
    var oRegexPatt = /^-?[0-9]\d*(\.\d*)?$/;

    return oRegexPatt.test(sNumber);
  };

  /*getTruncatedValue =(sOriginalValue, sPrecision)=> {
    if (sOriginalValue != "-") {
      // var sLastChar = CS.last(sOriginalValue);
      sOriginalValue = ViewLibraryUtils.getTruncatedValue(sOriginalValue, sPrecision);
      // if (sLastChar == ".") {
      //   sOriginalValue += sLastChar;
      // }
    }
    return sOriginalValue;
  };*/

  /*getValue =(sOriginalValue, sPrecision)=> {
    var sCurrentLocale = SessionStorageManager.getPropertyFromSessionStorage('language');
    sOriginalValue = sOriginalValue + "";
    if (!this.testNumber(sOriginalValue) && sOriginalValue != "-") {
      return ""
    }

    sOriginalValue = this.getTruncatedValue(sOriginalValue, sPrecision);

    if (sCurrentLocale == Constants.GERMAN_CODE || sCurrentLocale == Constants.FRENCH_CODE) {
      return CS.replace(sOriginalValue, /\./g, ',');
    } else {
      return sOriginalValue;
    }
  };*/
  getMeasurementAttributeValueToShow = (oModel, sNewValue, sCurrentUnit, sSelectedUnit, sPrecision) => {
    let oProperties = oModel.properties;
    let bDisableValueConversionOnUnitChange = oProperties.hasOwnProperty("disableValueChangeByDefaultUnit") ?
                                              oProperties.disableValueChangeByDefaultUnit : false;
    let sNewValueToStore = sNewValue;
    if(!bDisableValueConversionOnUnitChange) {
      sNewValueToStore = ViewLibraryUtils.getMeasurementAttributeValueToShow(sNewValue, sCurrentUnit, sSelectedUnit, sPrecision);
    }
    return sNewValueToStore;
  };


  getUnitConversionView =()=> {
    var oModel = this.props.model;
    var bIsVisible = oModel.converterVisibility;
    var aConversionList = oModel.converterList;
    let oMeasurementMetricAndImperial = new MeasurementMetrics();
    var sUnit = oModel.defaultUnit;
    if(this.state){
      var sCalculationUnit = this.state.defaultUnit;
      var oOption = CS.find(aConversionList, {unit: sCalculationUnit});
      if(oOption != null){
        sUnit = oOption.unitToDisplay;
      }
    }

    var aOptions = CS.map(aConversionList, function (oConversion) {
      var sValue = oConversion.unitToDisplay;
      return (<option key={oConversion.id} value={oConversion.unitToDisplay}>{sValue}</option>);
    });

    if (bIsVisible) {
      return (
          <ToolTip placement="bottom" label={sUnit}>
            <select className="conversionList" value={sUnit}
                    onChange={this.handleUnitConversionChange.bind(this, oModel.id, oModel.properties.valueId, aConversionList, oModel.properties.rangeType)}>
              {aOptions}
            </select>
          </ToolTip>
      );
    }
    else{
      var oView = null;
      var sAttributeType = oModel.properties["attributeType"];
      var sTooltipValue = "";

      try {
        var oUnit = CS.find(oMeasurementMetricAndImperial[sAttributeType], {unitToDisplay: sUnit});
        if (oUnit) {
          sTooltipValue = CS.getLabelOrCode(oUnit);
        }
        oView = (<ToolTip placement="bottom" label={sTooltipValue}>
          <div className="conversionList">{sUnit}</div>
        </ToolTip>);
      }
      catch(oException) {
        ExceptionLogger.error(oException);
        oView = (<ToolTip placement="bottom" label={sUnit}><div className="conversionList">{sUnit}</div></ToolTip>);
      }

      return oView;
    }
  };

  handleUnitConversionChange =(sAttrId, sValueId, aConversionList, sRangeType, oEvent)=> {
    var sSelectedDisplayUnit = oEvent.target.value;
    var oOption = CS.find(aConversionList, {unitToDisplay: sSelectedDisplayUnit});

    var sSelectedUnit = oOption.unit;
    var oModel = this.props.model;
    var sNewValue = oModel.value;
    var sCurrentUnit = oModel.baseUnit;
    var sPrecision = oModel.precision;
    var sNewValueToStore = this.getMeasurementAttributeValueToShow(oModel, sNewValue, sCurrentUnit, sSelectedUnit, sPrecision);
    shouldSetState = true;
    if(this.checkValueLength(sNewValueToStore) == false) {
      this.setState({value: sNewValueToStore, defaultUnit: sSelectedUnit});
    }
    EventBus.dispatch(oEvents.METRIC_UNIT_CHANGED, sSelectedUnit, oModel.properties["sectionElementDetails"], sAttrId, sValueId, sRangeType, this.props.filterContext);
  };

  handleOnChange =(sValue)=> {

    var sNewVal = sValue;

    if (sNewVal == "" || this.testNumber(sNewVal) || sNewVal == "-") {
      var sCurrentUnit = this.state.defaultUnit;
      shouldSetState = true;
      if(this.checkValueLength(sNewVal) == false) {
        /*if (sNewVal.includes(".")) {
          var sSplitter = ".";
          var aSplits = sNewVal.split(sSplitter);
          if(this.props.model.precision == 0) {
            sNewVal = aSplits[0];
          }
          if (aSplits[1].length <= this.props.model.precision) {
              this.setState({value: sNewVal, defaultUnit: sCurrentUnit});
              return;
          }
          else {
            return;
          }
        }*/
        this.setState({value: sNewVal, defaultUnit: sCurrentUnit});
      }
    }
  };

  checkValueLength =(sValue)=>{
    sValue = sValue + "";
    // var sCurrentLocale = SessionStorageManager.getPropertyFromSessionStorage('language');
    var iLengthToCheck = MetricCheckConstants.LENGTH_VALUE;

    var bIsGreater = false;

    if (sValue.indexOf(".") > -1) {
      var aSplitStrings = sValue.split(".");
      var sIntegerPart = aSplitStrings[0];
      var sFractionalPart = aSplitStrings[1];
      if (sIntegerPart.length > iLengthToCheck || sFractionalPart.length > iLengthToCheck) {
        bIsGreater = true;
      }
    } else {
      if (sValue.length > iLengthToCheck) {
        bIsGreater = true;
      }
    }
    if (bIsGreater == true) {
      EventBus.dispatch(oEvents.METRIC_LENGTH_EXCEEDED, this);
    }
    return bIsGreater;
  };

  removeUnnecessaryDecimalPoint =(sValue)=> {
    if (sValue.slice(-1) === ".") {
      sValue = sValue.slice(0, -1);
    }
    return sValue;
  };

  handleOnBlur =()=> {
    var oModel = this.props.model;
    var sDefaultUnitAsHTML = oModel.properties["defaultUnitAsHTML"];
    var sExpectedUnit = oModel.baseUnit;
    var sNewValue = this.removeUnnecessaryDecimalPoint(this.state.value);
    CS.isNotEmpty(sNewValue) && (sNewValue = CS.toNumber(sNewValue));

    var sCurrentUnit = this.state.defaultUnit;
    var sPrecision = oModel.precision;
    var sNewValueToStore = sNewValue;
    var sOldValue = oModel.value;
    var sOldValueToCompare = sOldValue;

    /** required for unit conversion **/
    /** no conversion is available for custom measurement attribute unit **/
    if(sDefaultUnitAsHTML == null){
      sNewValueToStore = this.getMeasurementAttributeValueToShow(oModel, sNewValue, sCurrentUnit, sExpectedUnit, sPrecision);
      sOldValueToCompare = this.getMeasurementAttributeValueToShow(oModel, sOldValue, sExpectedUnit, sCurrentUnit, sPrecision);
    }

    if (sOldValueToCompare != sNewValue || (sOldValueToCompare === "" && sNewValueToStore !== "")/* || sNewValueToStore === ""*/) { //TODO: Commented because everything is
      // TODO: running fine, need to find out why the code was added
      this.props.onBlur.call(this, sNewValueToStore);
    } /*else if (this.state.value !== sNewValue) {
      //eg. case : sOldValueToCompare is "1", this.state.value is "1." and sNewValue is "1";
      //Blur is not called since value is not changed, but we need to change state by removing the "."
      this.setState({
        value: sNewValue
      });
    }*/
  };

  handleKeyDown =(oEvent)=> {
    if (oEvent.key == "Tab") {
      this.handleOnBlur();
    }
  };

  getMeasurementMetricView =()=> {
    var oModel = this.props.model;
    let oProperties = oModel.properties;
    var sDefaultUnitAsHTML = oProperties["defaultUnitAsHTML"];
    // var sLabel = oModel.label;
    var bDisabled = oModel.isDisabled;
    var sPlaceholder = oModel.placeholder;
    var sValue = this.state ? this.state.value : oModel.value;
    let bValueDisabled = oProperties.isValueDisabled; //isValueDisabled: only value disabled not unit dropdown.
    /**
     * Negative Measurement Value Implementations -
     * enter the measurement attribute type which will accept negative values in aNegativeValueAllowedAttributes array.
     */

    let aNegativeValueAllowedAttributes = [AttributeTypeDictionary.TEMPERATURE];
    let bAllowNegativeValues = CS.includes(aNegativeValueAllowedAttributes, oModel.measurementAttributeType);

    // var sDescription = oModel.description;
    var sPrecision = oModel.precision;
    // var sValueToShow = this.getValue(sValue, sPrecision);
    var oUnitView = null;
    /*if (bDisabled) {
      oInputView = (
          <input disabled={bDisabled} className="measurementMetricInput isDisabled" placeholder={sPlaceholder}
                 value={sValueToShow}/>);
    } else {
      oInputView = (<input className="measurementMetricInput" placeholder={sPlaceholder} value={sValueToShow}
                           onKeyDown={this.handleKeyDown}
                           onChange={this.handleOnChange}
                           onBlur={this .handleOnBlur}/>);
    }*/
    var oInputView = (
      <NumberLocaleView
        onChange={this.handleOnChange}
        onForceBlur={this.handleOnBlur}
        onKeyDown={this.handleKeyDown}
        precision={oModel.precision}
        negativeAllowed={bAllowNegativeValues}
        isOnlyInteger={false}
        isDisabled={bDisabled || bValueDisabled}
        value={sValue}
        hintText={sPlaceholder}
        disableNumberLocaleFormatting={oProperties.disableNumberLocaleFormatting}
      />
    );

    let sWrapperClassName = "measurementMetricWrapper ";

    if (oProperties.hasOwnProperty("disableDefaultUnit") && oProperties.disableDefaultUnit) {
      sWrapperClassName += "disableDefaultUnit";
    } else if (sDefaultUnitAsHTML == null) {
      oUnitView = this.getUnitConversionView();
    } else {
      oUnitView = (<div className="dangerouslySetHTMLWrapper" contentEditable='false'
                        dangerouslySetInnerHTML={{__html: sDefaultUnitAsHTML}}></div>);
    }
    return (
        <div className="textField">
          {/*<div className="measurementMetricInputLabel textFieldLabel">{sLabel}</div>*/}
          <div className={sWrapperClassName}>
            <div className="measurementMetricElementWrapper">{oInputView}</div>
            <div className="measurementMetricElementListWrapper">
              {oUnitView}
            </div>
          </div>
          {/*<div className="measurementMetricInputDescription textFieldDescription">{sDescription}</div>*/}
        </div>
    );
  };

  render() {
    return (
        <div className="contentMeasurementMetricsContainer">
          {this.getMeasurementMetricView()}
        </div>
    );
  }
}

ContentMeasurementMetricsView.propTypes = oPropTypes;

export const view = ContentMeasurementMetricsView;
export const events = oEvents;
