/**
 * Created by CS80 on 27/10/2016.
 */

import alertify from '../../commonmodule/store/custom-alertify-store';

import CS from '../../libraries/cs';
import NumberTypeMetadataAttributes from "../../commonmodule/tack/number-type-metadata-attribute-list";
import HomeScreenAppData from '../../screens/homescreen/store/model/home-screen-app-data';
import MeasurementUnitBaseType from '../tack/measurement-metric-base-type-dictionary';
import AttributeTypeDictionary from '../tack/attribute-type-dictionary-new';
import MetricCheckConstants from '../tack/measurement-metrics-constants';
import MockDataForMeasurementMetricAndImperial from '../tack/mock-data-for-measurement-metrics-and-imperial';
import Qty from '../../libraries/jsquantities/quantities.js';
import MomentUtils from './moment-utils';
import NumberUtils from './number-util';

var AttributeUtils = (function () {

  var _getAttributeTypeForVisual = function(sType, sAttrId){

    switch (sType) {
      case AttributeTypeDictionary.HTML:
      case AttributeTypeDictionary.DESCRIPTION:
        return "html";

      case AttributeTypeDictionary.TEXT:
      case AttributeTypeDictionary.NAME:
      case AttributeTypeDictionary.STANDARD_EXIF:
      case AttributeTypeDictionary.STANDARD_IPTC:
      case AttributeTypeDictionary.STANDARD_XMP:
      case AttributeTypeDictionary.STANDARD_OTHER:
      case AttributeTypeDictionary.ADDRESS:
      case AttributeTypeDictionary.PIN_CODE:
      case AttributeTypeDictionary.TEL:
      case AttributeTypeDictionary.FIRST_NAME:
      case AttributeTypeDictionary.LAST_NAME:
      case AttributeTypeDictionary.LAST_MODIFIED_BY:
      case AttributeTypeDictionary.PID:
      case AttributeTypeDictionary.GTIN:
      case AttributeTypeDictionary.SKU:
      case AttributeTypeDictionary.CREATED_BY:
      case AttributeTypeDictionary.LONG_DESCRIPTION:
      case AttributeTypeDictionary.SHORT_DESCRIPTION:

      case AttributeTypeDictionary.APPLICATION_RECORD_VERSION:
      case AttributeTypeDictionary.CAPTION_ABSTRACT:
      case AttributeTypeDictionary.COMPOSITE:
      case AttributeTypeDictionary.COMPRESSION:
      case AttributeTypeDictionary.CREATOR_TOOL:
      case AttributeTypeDictionary.DOCUMENT_ID:
      case AttributeTypeDictionary.EXIF_TOOL:
      case AttributeTypeDictionary.FILE:
      case AttributeTypeDictionary.KEYWORDS:
      case AttributeTypeDictionary.META_DATA_DATE:
      case AttributeTypeDictionary.OBJECT_NAME:
      case AttributeTypeDictionary.THUMBNAIL_LENGTH:
      case AttributeTypeDictionary.THUMBNAIL_OFFSET:
      case AttributeTypeDictionary.XMP_TOOLKIT:
      case AttributeTypeDictionary.X_RESOLUTION:
      case AttributeTypeDictionary.Y_RESOLUTION:
      case AttributeTypeDictionary.SOURCE_FILE:
      case AttributeTypeDictionary.MODIFY_DATE:
      case AttributeTypeDictionary.CREATE_DATE:
      case AttributeTypeDictionary.CREATOR:
      case AttributeTypeDictionary.FILE_NAME:

        return "text";

      case AttributeTypeDictionary.ASSET_META_DATA:
        return NumberTypeMetadataAttributes.includes(sAttrId) ? "number" : "text";

      case AttributeTypeDictionary.NUMBER:
      case AttributeTypeDictionary.SELLING_PRICE:
      case AttributeTypeDictionary.LIST_PRICE:
      case AttributeTypeDictionary.MAXIMUM_PRICE:
      case AttributeTypeDictionary.MINIMUM_PRICE:
        return "number";

      case AttributeTypeDictionary.DATE:
      case AttributeTypeDictionary.LAST_MODIFIED:
      case AttributeTypeDictionary.DUE_DATE:
      case AttributeTypeDictionary.CREATED_ON:
        return "date";

      case AttributeTypeDictionary.TYPE:
      case AttributeTypeDictionary.OWNER:
      case AttributeTypeDictionary.ASSIGNEE:
        return "multiselect";

      case AttributeTypeDictionary.IMAGE_COVERFLOW:
      case AttributeTypeDictionary.COVERFLOW:
        return "coverflow";

      case AttributeTypeDictionary.IMAGE:
      case AttributeTypeDictionary.IMAGE_ATTRIBUTE:
        return "image";

      case AttributeTypeDictionary.EXIF:
      case AttributeTypeDictionary.IPTC:
      case AttributeTypeDictionary.XMP:
      case AttributeTypeDictionary.OTHER:
        return "text";  //change type from text after discussion

      case AttributeTypeDictionary.LENGTH:
      case AttributeTypeDictionary.CURRENT:
      case AttributeTypeDictionary.POTENTIAL:
      case AttributeTypeDictionary.FREQUENCY:
      case AttributeTypeDictionary.TIME:
      case AttributeTypeDictionary.TEMPERATURE:
      case AttributeTypeDictionary.VOLUME:
      case AttributeTypeDictionary.AREA:
      case AttributeTypeDictionary.MASS:
      case AttributeTypeDictionary.DIGITAL_STORAGE:
      case AttributeTypeDictionary.ENERGY:
      case AttributeTypeDictionary.PLANE_ANGLE:
      case AttributeTypeDictionary.PRESSURE:
      case AttributeTypeDictionary.SPEED:
      case AttributeTypeDictionary.POWER:
      case AttributeTypeDictionary.LUMINOSITY:
      case AttributeTypeDictionary.RADIATION:
      case AttributeTypeDictionary.ILLUMINANCE:
      case AttributeTypeDictionary.FORCE:
      case AttributeTypeDictionary.ACCELERATION:
      case AttributeTypeDictionary.CAPACITANCE:
      case AttributeTypeDictionary.VISCOCITY:
      case AttributeTypeDictionary.INDUCTANCE:
      case AttributeTypeDictionary.RESISTANCE:
      case AttributeTypeDictionary.MAGNETISM:
      case AttributeTypeDictionary.CHARGE:
      case AttributeTypeDictionary.CONDUCTANCE:
      case AttributeTypeDictionary.SUBSTANCE:
      case AttributeTypeDictionary.WEIGHT_PER_AREA:
      case AttributeTypeDictionary.PROPORTION:
      case AttributeTypeDictionary.THERMAL_INSULATION:
      case AttributeTypeDictionary.CUSTOM_UNIT:
      case AttributeTypeDictionary.HEATING_RATE:
      case AttributeTypeDictionary.DENSITY:
      case AttributeTypeDictionary.WEIGHT_PER_TIME:
      case AttributeTypeDictionary.VOLUME_FLOW_RATE:
      case AttributeTypeDictionary.AREA_PER_VOLUME:
      case AttributeTypeDictionary.ROTATION_FREQUENCY:
      case AttributeTypeDictionary.PRICE:
        return "measurementMetrics";

      case AttributeTypeDictionary.CALCULATED:
        return "calculated";

      case AttributeTypeDictionary.CONCATENATED:
        return "concatenated";

      default:
        return "text";
    }

  };

  var _getDateValue = function (sValue) {
    var sData = sValue;
    if (sValue) {
      sData = new Date(+sValue);
      if (CS.isNaN(Date.parse(sData))) {
        sData = sValue
      } else {
        sData = MomentUtils.getDateAttributeInTimeFormat(sData)
      }
    }
    return sData;
  };

  var _getTruncatedValue = function (iVal, sPrecision) {
    return NumberUtils.getNumberAccordingToPrecision(CS.toString(iVal), Number(sPrecision));
    /*var sVal = iVal + "";
    iVal = Number(iVal);
    var sSplitter = null;
    if (sVal.includes(".")) {
      sSplitter = ".";
    } else if (sVal.includes(",")) {
      sSplitter = ",";
    }
    var iPrecision = !sPrecision && sPrecision != 0 ? MetricCheckConstants.DEFAULT_PRECISION : Number(sPrecision);
    if (sSplitter != null) {
      var aSplits = sVal.split(sSplitter);
      if(aSplits[1] && (aSplits[1].length > iPrecision)){
        var iFormattedVal = iPrecision == 0 ? CS.round(iVal) : iVal.toFixed(iPrecision);
        var sWithoutTrails = iFormattedVal.toString();
        return +sWithoutTrails;
      } else {
        return sVal;
      }
    }
    return sVal;
    *//*iVal = Number(iVal);
    var sVal = iVal + "";
    var sSplitter = ".";
    var aSplits = sVal.split(sSplitter);
    var iPrecision = !sPrecision && sPrecision != 0 ? MetricCheckConstants.DEFAULT_PRECISION : Number(sPrecision);

    if(aSplits[1] && (aSplits[1].length > iPrecision)){
      var iFormattedVal = iVal.toFixed(iPrecision);
      var sWithoutTrails = iFormattedVal.toString();
      return +sWithoutTrails;
    } else {
      return sVal;
    }*/
  };

  var _convertCurrency = function (value, from, to) {
    let oMeasurementMetricsAndImperial = new MockDataForMeasurementMetricAndImperial();
    var aCurrencyUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.PRICE];
    var oBaseCurrency = CS.find(aCurrencyUnits, {"isBase": true});
    var aRates = [];
    let oExchangeRates = HomeScreenAppData.getExchangeRates();
    if (oBaseCurrency["unit"] == "EUR") {
      aRates = oExchangeRates.CURRENCY_EXCHANGE_RATES_BASE_EUR;
    } else if (oBaseCurrency["unit"] == "USD") {
      aRates = oExchangeRates.CURRENCY_EXCHANGE_RATES_BASE_USD;
    }
    if (from == to) {
      return value.toString();
    }
    return ((value / aRates[from]) * aRates[to]).toString();
  };

  var _convertWeightPerArea = function (value, from, to) {
    var oWeightPerArea = MetricCheckConstants.WEIGHT_PER_AREA_EXCHANGE_RATES_BASE_KG_PER_M2;

    if (from == to) {
      return value.toString();
    }
    return ((value / oWeightPerArea[from]) * oWeightPerArea[to]).toString();
  };

  var _convertLuminosity = function (value, from, to) {
    var oLuminosity = MetricCheckConstants.LUMINOSITY_EXCHANGE_RATES_BASE_CANDELA;

    if (from == to) {
      return value.toString();
    }
    return ((value / oLuminosity[from]) * oLuminosity[to]).toString();
  };

  var _convertThermalInsulation = function (value, from, to) {
    var oThermalInsulation = MetricCheckConstants.THERMAL_INSULATION_EXCHANGE_RATES_BASE_M2K_PER_W;

    if (from == to) {
      return value.toString();
    }
    return ((value / oThermalInsulation[from]) * oThermalInsulation[to]).toString();
  };

  var _convertUnitAttribute = function (sValue, sFrom, sTo, oConversionObject) {
    if (sFrom == sTo) {
      return sValue.toString();
    }
    return ((sValue / oConversionObject[sFrom]) * oConversionObject[sTo]).toString();
  };

  var _convertProportion = function (value, from, to) {
    var oProportion = MetricCheckConstants.PROPORTION_EXCHANGE_RATES_BASE_PERCENTAGE;

    if (from == to) {
      return value.toString();
    }
    return ((value / oProportion[from]) * oProportion[to]).toString();
  };

  var _convertPowerBaseOnWatt = function (sValue, sFrom, sTo) {
    var oPowerBaseOnWatt = MetricCheckConstants.POWER_EXCHANGE_BASE_ON_WATT;
    if (sFrom == sTo) {
      return sValue.toString();
    }
    return ((sValue / oPowerBaseOnWatt[sFrom]) * oPowerBaseOnWatt[sTo]).toString();
  };

  //TODO: Incomplete implementation (Vidit)
  var _getAttributeTypeFromUnitWIP = function (sUnit) {
    let oMeasurementMetricsAndImperial = new MockDataForMeasurementMetricAndImperial();

    var aWeightPerAreaUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.WEIGHT_PER_AREA];
    if(CS.find(aWeightPerAreaUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.WEIGHT_PER_AREA;
    }

    var aLuminosityUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.LUMINOSITY];
    if(CS.find(aLuminosityUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.LUMINOSITY;
    }

    var aProportionUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.PROPORTION];
    if(CS.find(aProportionUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.PROPORTION;
    }

    var aThermalInsulationUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.THERMAL_INSULATION];
    if(CS.find(aThermalInsulationUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.THERMAL_INSULATION;
    }

    var aHeatingRateUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.HEATING_RATE];
    if(CS.find(aHeatingRateUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.HEATING_RATE;
    }

    var aDensityUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.DENSITY];
    if(CS.find(aDensityUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.DENSITY;
    }

    var aWeightPerTimeUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.WEIGHT_PER_TIME];
    if(CS.find(aWeightPerTimeUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.WEIGHT_PER_TIME;
    }

    var aVolumeFlowRateUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.VOLUME_FLOW_RATE];
    if(CS.find(aVolumeFlowRateUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.VOLUME_FLOW_RATE;
    }

    var aAreaPerVolumeUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.AREA_PER_VOLUME];
    if(CS.find(aAreaPerVolumeUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.AREA_PER_VOLUME;
    }

    var aRotationFrequencyUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.ROTATION_FREQUENCY];
    if(CS.find(aRotationFrequencyUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.ROTATION_FREQUENCY;
    }

    var aLengthUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.LENGTH];
    if(CS.find(aLengthUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.LENGTH;
    }

    var aCurrentUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.CURRENT];
    if(CS.find(aCurrentUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.CURRENT;
    }

    var aPotentialUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.POTENTIAL];
    if(CS.find(aPotentialUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.POTENTIAL;
    }

    var aFrequencyUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.FREQUENCY];
    if(CS.find(aFrequencyUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.FREQUENCY;
    }

    var aTimeUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.TIME];
    if(CS.find(aTimeUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.TIME;
    }

    var aTemperatureUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.TEMPERATURE];
    if(CS.find(aTemperatureUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.TEMPERATURE;
    }

    var aVolumeUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.VOLUME];
    if(CS.find(aVolumeUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.VOLUME;
    }

    var aAreaUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.AREA];
    if(CS.find(aAreaUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.AREA;
    }

    var aMassUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.MASS];
    if(CS.find(aMassUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.MASS;
    }

    var aDigitalStorageAttributeUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.DIGITAL_STORAGE];
    if(CS.find(aDigitalStorageAttributeUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.DIGITAL_STORAGE;
    }

    var aEnergyUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.ENERGY];
    if(CS.find(aEnergyUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.ENERGY;
    }

    var aPlaneAngleUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.PLANE_ANGLE];
    if(CS.find(aPlaneAngleUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.PLANE_ANGLE;
    }

    var aPressureUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.PRESSURE];
    if(CS.find(aPressureUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.PRESSURE;
    }

    var aSpeedUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.SPEED];
    if(CS.find(aSpeedUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.SPEED;
    }

    var aPriceUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.PRICE];
    if(CS.find(aPriceUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.PRICE;
    }

    var aPowerUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.POWER];
    if(CS.find(aPowerUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.POWER;
    }

    var aRadiationUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.RADIATION];
    if(CS.find(aRadiationUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.RADIATION;
    }

    var aIlluminanceUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.ILLUMINANCE];
    if(CS.find(aIlluminanceUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.ILLUMINANCE;
    }

    var aForceUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.FORCE];
    if(CS.find(aForceUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.FORCE;
    }

    var aAccelerationUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.ACCELERATION];
    if(CS.find(aAccelerationUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.ACCELERATION;
    }

    var aCapacitanceUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.CAPACITANCE];
    if(CS.find(aCapacitanceUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.CAPACITANCE;
    }

    var aViscocityUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.VISCOCITY];
    if(CS.find(aViscocityUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.VISCOCITY;
    }

    var aInductanceUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.INDUCTANCE];
    if(CS.find(aInductanceUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.INDUCTANCE;
    }

    var aResistanceUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.RESISTANCE];
    if(CS.find(aResistanceUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.RESISTANCE;
    }

    var aMagnetismUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.MAGNETISM];
    if(CS.find(aMagnetismUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.MAGNETISM;
    }

    var aChargeUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.CHARGE];
    if(CS.find(aChargeUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.CHARGE;
    }

    var aConductanceUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.CONDUCTANCE];
    if(CS.find(aConductanceUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.CONDUCTANCE;
    }

    var aSubstanceUnits = oMeasurementMetricsAndImperial[AttributeTypeDictionary.SUBSTANCE];
    if(CS.find(aSubstanceUnits, {"unit": sUnit})){
      return AttributeTypeDictionary.SUBSTANCE;
    }

    return AttributeTypeDictionary.CUSTOM_UNIT;
  };

  //TODO: Refactor (Vidit)
  var _getMeasurementAttributeValueToShow = function (sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision) {
    if (!isNaN(sOriginalValue) && sOriginalValue !== "") {
      var sValue = CS.isNumber(sOriginalValue) ? sOriginalValue : Number(sOriginalValue);

      var sAttributeType = _getAttributeTypeFromUnitWIP(sCurrentUnit);

      switch (sAttributeType) {
        case AttributeTypeDictionary.PRICE:
          return _getTruncatedValue(sOriginalValue, sPrecision);

        case AttributeTypeDictionary.WEIGHT_PER_AREA:
          return _getTruncatedValue(_convertWeightPerArea(sOriginalValue, sCurrentUnit, sExpectedUnit), sPrecision);

        case AttributeTypeDictionary.LUMINOSITY:
          return _getTruncatedValue(_convertLuminosity(sOriginalValue, sCurrentUnit, sExpectedUnit), sPrecision);

        case AttributeTypeDictionary.PROPORTION:
          return _getTruncatedValue(_convertProportion(sOriginalValue, sCurrentUnit, sExpectedUnit), sPrecision);

        case AttributeTypeDictionary.THERMAL_INSULATION:
          return _getTruncatedValue(_convertThermalInsulation(sOriginalValue, sCurrentUnit, sExpectedUnit), sPrecision);

        case AttributeTypeDictionary.HEATING_RATE:
          return _getTruncatedValue(_convertUnitAttribute(sOriginalValue, sCurrentUnit, sExpectedUnit, MetricCheckConstants.HEATING_RATE_EXCHANGE_RATES_BASE_CELSIUS_PER_SECOND),
              sPrecision);

        case AttributeTypeDictionary.DENSITY:
          return _getTruncatedValue(_convertUnitAttribute(sOriginalValue, sCurrentUnit, sExpectedUnit, MetricCheckConstants.DENSITY_EXCHANGE_RATES_BASE_KG_PER_M3),
              sPrecision);

        case AttributeTypeDictionary.WEIGHT_PER_TIME:
          return _getTruncatedValue(_convertUnitAttribute(sOriginalValue, sCurrentUnit, sExpectedUnit, MetricCheckConstants.WEIGHT_PER_TIME_EXCHANGE_RATES_BASE_GM_PER_SEC),
              sPrecision);

        case AttributeTypeDictionary.VOLUME_FLOW_RATE:
          return _getTruncatedValue(_convertUnitAttribute(sOriginalValue, sCurrentUnit, sExpectedUnit, MetricCheckConstants.VOLUME_FLOW_RATE_EXCHANGE_RATES_BASE_M3_PER_SECOND),
              sPrecision);

        case AttributeTypeDictionary.AREA_PER_VOLUME:
          return _getTruncatedValue(_convertUnitAttribute(sOriginalValue, sCurrentUnit, sExpectedUnit, MetricCheckConstants.AREA_PER_VOLUME_EXCHANGE_RATES_BASE_CM2_PER_ML),
              sPrecision);

        case AttributeTypeDictionary.ROTATION_FREQUENCY:
          return _getTruncatedValue(_convertUnitAttribute(sOriginalValue, sCurrentUnit, sExpectedUnit, MetricCheckConstants.ROTATION_FREQUENCY_EXCHANGE_RATES_BASE_REV_PER_SECOND),
              sPrecision);

        case AttributeTypeDictionary.CUSTOM_UNIT:
          return _getTruncatedValue(sOriginalValue, sPrecision);

        case AttributeTypeDictionary.POWER:
          return _getTruncatedValue(_convertPowerBaseOnWatt(sOriginalValue ,sCurrentUnit, sExpectedUnit));

        default:
          var qty = null;
          try {
            if (sCurrentUnit == "tempC") {
              if (sValue < -273.15) {
                sValue = -273.15;
              }
            } else if (sCurrentUnit == "tempF") {
              if (sValue < -459.67) {
                sValue =  -459.67;
              }
            } else if (sCurrentUnit == "tempK") {
              if (sValue < 0) {
                sValue = 0;
              }
            }
            sValue = CS.isNumber(sValue) ? sValue : Number(sValue);
            qty = new Qty(sValue, sCurrentUnit);
          } catch (err) {
            // TODO: Change message in alertify
            alertify.error("Something went wrong while converting the entered value: " + sValue);
          }
          if (qty == null) {
            return "";
          }
          return _getTruncatedValue(qty.to(sExpectedUnit).scalar, sPrecision);
      }

    } else {
      return "";
    }
  };

  var _getUnitLabel = function (sType, sUnit) {
    let oMockDataForMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
    var aUnits = oMockDataForMeasurementMetricAndImperial[sType];
    var oUnit = CS.find(aUnits, {unit: sUnit});
    return oUnit ? oUnit.unitToDisplay : sUnit;
  };

  var _getLabelByAttributeType = function (sAttributeType, sFilterLabel, sDefaultUnit, iPrecision, bDisableFormatByLocale) {
    var sType = _getAttributeTypeForVisual(sAttributeType);
    if (sType == "date") {
      if(CS.isEmpty(sFilterLabel)){
        return "";
      }
      var sDateLabel = new Date(parseInt(sFilterLabel));
      return _getDateValue(sDateLabel);
    }
    else if (sType == "measurementMetrics") {
      let oMockDataForMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
      var aConverterList = oMockDataForMeasurementMetricAndImperial[sAttributeType];
      var oBaseUnit = CS.find(aConverterList, {isBase: true});
      var sUnit = oBaseUnit ? oBaseUnit.unit : sDefaultUnit;
      //var sUnit = MeasurementUnitBaseType[sAttributeType];
      if (sDefaultUnit) {
        sFilterLabel = _getMeasurementAttributeValueToShow(sFilterLabel, sUnit, sDefaultUnit, iPrecision);
        sUnit = sDefaultUnit;
      }
      sUnit = _getUnitLabel(sAttributeType, sUnit);
      if (CS.isEmpty(sFilterLabel) && !+sFilterLabel) {
        sFilterLabel = NumberUtils.getValueToShowAccordingToNumberFormat(sFilterLabel, iPrecision, {}, bDisableFormatByLocale);
        return sFilterLabel;
      } else {
        sFilterLabel = NumberUtils.getValueToShowAccordingToNumberFormat(sFilterLabel, iPrecision, {}, bDisableFormatByLocale);
        return sFilterLabel + " " + sUnit;
      }
    }
    else if (sType == "number" || sType == "calculated") {
      sFilterLabel = NumberUtils.getValueToShowAccordingToNumberFormat(sFilterLabel, iPrecision, {}, bDisableFormatByLocale);
    }
    else if (sType == "html") {
      var oHTMLDiv = document.createElement("div");
      oHTMLDiv.innerHTML = sFilterLabel;
      return oHTMLDiv.textContent;
    }
    return sFilterLabel;
  };

  let _getAttributeValueToShow = function (oAttribute, oMasterAttribute) {
    let sValue = oAttribute.value;
    let sType = oMasterAttribute.type;

    if(oMasterAttribute.type == AttributeTypeDictionary.HTML) {
      sValue = oAttribute.valueAsHtml || "";
    }
    if(oMasterAttribute.type == AttributeTypeDictionary.DATE || _isAttributeTypeMeasurement(oMasterAttribute.type)) {
      sValue = oAttribute.originalValue || oAttribute.value || "";
    }

    let sExpectedUnit = oMasterAttribute && oMasterAttribute.defaultUnit || '';
    let iPrecision = oMasterAttribute && CS.isNumber(oMasterAttribute.precision) ? oMasterAttribute.precision : 0;

    if(_isAttributeTypeCalculated(sType)){
      sType = oMasterAttribute.calculatedAttributeType;
      sExpectedUnit =  oMasterAttribute.calculatedAttributeUnit;
    }

    return _getLabelByAttributeType(sType, sValue, sExpectedUnit, iPrecision, oMasterAttribute.hideSeparator);
  };

  var _isUserTypeAttribute = function (sAttributeType) {
    return (
        sAttributeType == AttributeTypeDictionary.CREATED_BY || //createdByAttr
        sAttributeType == AttributeTypeDictionary.LAST_MODIFIED_BY // LastModifiedByAttribute
    );
  };

  var _getBaseUnitFromType = function (sAttributeType) {
    return MeasurementUnitBaseType[sAttributeType];
  };

  var _canAttributeBeContextual = function (sAttributeType) {
    switch (sAttributeType) {

      case AttributeTypeDictionary.TYPE: //TypeAttribute
      case AttributeTypeDictionary.LAST_MODIFIED: //LastModifiedAttribute
      case AttributeTypeDictionary.LAST_MODIFIED_BY: //LastModifiedByAttribute
      case AttributeTypeDictionary.CREATED_BY: //CreatedByAttribute
      case AttributeTypeDictionary.TAXONOMY: //TaxonomyAttribute
      case AttributeTypeDictionary.MULTI_CLASSIFICATION: //MultiClassificationAttribute
      case AttributeTypeDictionary.ASSET_META_DATA: //AssetMetadataAttribute
      case AttributeTypeDictionary.CALCULATED: //CalculatedAttribute
      case AttributeTypeDictionary.CONCATENATED: //ConcatenatedAttribute
        return false;

      default:
        return true;
    }
  };

  var _getAllNumericTypeAttributes = function () {
    /**
     * This array includes all those attribute types which are numeric in type
     * i.e, Date types, Measurement Types, Number Types, Calculated.
     *
     * Also used as Range types for filter purpose.
     */
    return [
      AttributeTypeDictionary.NUMBER,
      AttributeTypeDictionary.DATE,
      AttributeTypeDictionary.CREATED_ON,
      AttributeTypeDictionary.DUE_DATE,
      AttributeTypeDictionary.LAST_MODIFIED,
      AttributeTypeDictionary.AREA,
      AttributeTypeDictionary.CURRENT,
      AttributeTypeDictionary.ENERGY,
      AttributeTypeDictionary.FREQUENCY,
      AttributeTypeDictionary.LENGTH,
      AttributeTypeDictionary.MASS,
      AttributeTypeDictionary.PLANE_ANGLE,
      AttributeTypeDictionary.POTENTIAL,
      AttributeTypeDictionary.PRESSURE,
      AttributeTypeDictionary.SPEED,
      AttributeTypeDictionary.TEMPERATURE,
      AttributeTypeDictionary.TIME,
      AttributeTypeDictionary.VOLUME,
      AttributeTypeDictionary.DIGITAL_STORAGE,
      AttributeTypeDictionary.SELLING_PRICE,
      AttributeTypeDictionary.LIST_PRICE,
      AttributeTypeDictionary.MAXIMUM_PRICE,
      AttributeTypeDictionary.MINIMUM_PRICE,

      AttributeTypeDictionary.POWER,
      AttributeTypeDictionary.LUMINOSITY,
      AttributeTypeDictionary.RADIATION,
      AttributeTypeDictionary.ILLUMINANCE,
      AttributeTypeDictionary.FORCE,
      AttributeTypeDictionary.ACCELERATION,
      AttributeTypeDictionary.CAPACITANCE,
      AttributeTypeDictionary.VISCOCITY,
      AttributeTypeDictionary.INDUCTANCE,
      AttributeTypeDictionary.RESISTANCE,
      AttributeTypeDictionary.MAGNETISM,
      AttributeTypeDictionary.CHARGE,
      AttributeTypeDictionary.CONDUCTANCE,
      AttributeTypeDictionary.SUBSTANCE,
      AttributeTypeDictionary.CALCULATED,
      AttributeTypeDictionary.WEIGHT_PER_AREA,
      AttributeTypeDictionary.PROPORTION,
      AttributeTypeDictionary.THERMAL_INSULATION,
      AttributeTypeDictionary.HEATING_RATE,
      AttributeTypeDictionary.DENSITY,
      AttributeTypeDictionary.WEIGHT_PER_TIME,
      AttributeTypeDictionary.VOLUME_FLOW_RATE,
      AttributeTypeDictionary.AREA_PER_VOLUME,
      AttributeTypeDictionary.ROTATION_FREQUENCY,
      AttributeTypeDictionary.PRICE,
      AttributeTypeDictionary.CUSTOM_UNIT
    ];
  };

  var _getAllExcludedAttributeTypeForBulkEdit = function () {

    return [
      AttributeTypeDictionary.CALCULATED,
      AttributeTypeDictionary.CONCATENATED,

      AttributeTypeDictionary.EXIF,
      AttributeTypeDictionary.IPTC,
      AttributeTypeDictionary.XMP,
      AttributeTypeDictionary.OTHER,

      AttributeTypeDictionary.STANDARD_EXIF,
      AttributeTypeDictionary.STANDARD_IPTC,
      AttributeTypeDictionary.STANDARD_XMP,
      AttributeTypeDictionary.STANDARD_OTHER,
      AttributeTypeDictionary.ASSET_META_DATA,

      AttributeTypeDictionary.IMAGE_COVERFLOW,
      AttributeTypeDictionary.COVERFLOW,

      AttributeTypeDictionary.CREATED_ON,

      AttributeTypeDictionary.LAST_MODIFIED,

      AttributeTypeDictionary.LAST_MODIFIED_BY,
      AttributeTypeDictionary.CREATED_BY

    ];
  };

  var _isAttributeTypeCalculated = function (sType) {
    return sType == AttributeTypeDictionary.CALCULATED;
  };

  let _isAttributeTypeMeasurement= (sType) => {
    switch (sType) {
      case AttributeTypeDictionary.LENGTH:
      case AttributeTypeDictionary.CURRENT:
      case AttributeTypeDictionary.POTENTIAL:
      case AttributeTypeDictionary.FREQUENCY:
      case AttributeTypeDictionary.TIME:
      case AttributeTypeDictionary.TEMPERATURE:
      case AttributeTypeDictionary.VOLUME:
      case AttributeTypeDictionary.AREA:
      case AttributeTypeDictionary.MASS:
      case AttributeTypeDictionary.DIGITAL_STORAGE:
      case AttributeTypeDictionary.ENERGY:
      case AttributeTypeDictionary.PLANE_ANGLE:
      case AttributeTypeDictionary.PRESSURE:
      case AttributeTypeDictionary.SPEED:
      case AttributeTypeDictionary.POWER:
      case AttributeTypeDictionary.LUMINOSITY:
      case AttributeTypeDictionary.RADIATION:
      case AttributeTypeDictionary.ILLUMINANCE:
      case AttributeTypeDictionary.FORCE:
      case AttributeTypeDictionary.ACCELERATION:
      case AttributeTypeDictionary.CAPACITANCE:
      case AttributeTypeDictionary.VISCOCITY:
      case AttributeTypeDictionary.INDUCTANCE:
      case AttributeTypeDictionary.RESISTANCE:
      case AttributeTypeDictionary.MAGNETISM:
      case AttributeTypeDictionary.CHARGE:
      case AttributeTypeDictionary.CONDUCTANCE:
      case AttributeTypeDictionary.SUBSTANCE:
      case AttributeTypeDictionary.WEIGHT_PER_AREA:
      case AttributeTypeDictionary.PROPORTION:
      case AttributeTypeDictionary.THERMAL_INSULATION:
      case AttributeTypeDictionary.CUSTOM_UNIT:
      case AttributeTypeDictionary.HEATING_RATE:
      case AttributeTypeDictionary.DENSITY:
      case AttributeTypeDictionary.WEIGHT_PER_TIME:
      case AttributeTypeDictionary.VOLUME_FLOW_RATE:
      case AttributeTypeDictionary.AREA_PER_VOLUME:
      case AttributeTypeDictionary.ROTATION_FREQUENCY:
      case AttributeTypeDictionary.PRICE:

        return true;
      default:
        return false;
    }
  };

  /************************************* Public API's **********************************************/
  return {

    isImageCoverflowAttribute: function (sAttributeType) {
      var aImageAttributeIds = [AttributeTypeDictionary.IMAGE_COVERFLOW, AttributeTypeDictionary.COVERFLOW];
      var bIsImageAttribute = false;
      CS.forEach(aImageAttributeIds, function (sId) {
        if(sAttributeType == AttributeTypeDictionary[sId]) {
          bIsImageAttribute = true;
          return false;
        }
      });

      return bIsImageAttribute;
    },

    getAttributeTypeForVisual: function(sType, sAttrId){
      return _getAttributeTypeForVisual(sType, sAttrId);
    },

    isAttributeTypeHtml: function (sType) {
      return sType == AttributeTypeDictionary.HTML || sType == AttributeTypeDictionary.LONG_DESCRIPTION
          || sType == AttributeTypeDictionary.SHORT_DESCRIPTION || this.isAttributeTypeDescription(sType) ;
    },

    isAttributeTypeDescription: function (sType) {
      return sType == AttributeTypeDictionary.DESCRIPTION;
    },

    /** @deprecated **/
    isAttributeTypeType: function (sType) {
      return sType == AttributeTypeDictionary.TYPE;
    },

    isAttributeTypeName: function (sType) {
      return sType == AttributeTypeDictionary.NAME;
    },

    isAttributeTypeText: function (sType) {
      if (this.isAttributeTypeName(sType)) {
        return true;
      }
      if (this.isAttributeTypeMetadata(sType)) {
        return true;
      }
      switch (sType) {
        case AttributeTypeDictionary.TEXT:
        case AttributeTypeDictionary.ADDRESS:
        case AttributeTypeDictionary.PIN_CODE:
        case AttributeTypeDictionary.FIRST_NAME:
        case AttributeTypeDictionary.LAST_NAME:
        case AttributeTypeDictionary.SIZE:
        case AttributeTypeDictionary.FILE_TYPE:
        case AttributeTypeDictionary.STATUS:
        case AttributeTypeDictionary.TAT:
        case AttributeTypeDictionary.PID:
        case AttributeTypeDictionary.GTIN:
        case AttributeTypeDictionary.SKU:
        case AttributeTypeDictionary.LONG_DESCRIPTION:
        case AttributeTypeDictionary.SHORT_DESCRIPTION:

        case AttributeTypeDictionary.APPLICATION_RECORD_VERSION:
        case AttributeTypeDictionary.CAPTION_ABSTRACT:
        case AttributeTypeDictionary.COMPOSITE:
        case AttributeTypeDictionary.COMPRESSION:
        case AttributeTypeDictionary.CREATOR_TOOL:
        case AttributeTypeDictionary.DOCUMENT_ID:
        case AttributeTypeDictionary.EXIF_TOOL:
        case AttributeTypeDictionary.FILE:
        case AttributeTypeDictionary.KEYWORDS:
        case AttributeTypeDictionary.META_DATA_DATE:
        case AttributeTypeDictionary.OBJECT_NAME:
        case AttributeTypeDictionary.THUMBNAIL_LENGTH:
        case AttributeTypeDictionary.THUMBNAIL_OFFSET:
        case AttributeTypeDictionary.XMP_TOOLKIT:
        case AttributeTypeDictionary.X_RESOLUTION:
        case AttributeTypeDictionary.Y_RESOLUTION:
        case AttributeTypeDictionary.SOURCE_FILE:
        case AttributeTypeDictionary.MODIFY_DATE:
        case AttributeTypeDictionary.CREATE_DATE:
        case AttributeTypeDictionary.CREATOR:
        case AttributeTypeDictionary.ASSET_META_DATA:
        case AttributeTypeDictionary.FILE_NAME:

		
          return true;
        default:
          return false;
      }
    },

    isAttributeTypeCreatedOn: function (sType) {
      return sType == AttributeTypeDictionary.CREATED_ON;
    },

    isAttributeTypeLastModified: function (sType) {
      return sType == AttributeTypeDictionary.LAST_MODIFIED;
    },

    isAttributeTypeDueDate: function (sType) {
      return sType == AttributeTypeDictionary.DUE_DATE;
    },

    isAttributeTypeDate: function (sType) {
      return this.isAttributeTypeCreatedOn(sType) || sType == AttributeTypeDictionary.DATE ||
          this.isAttributeTypeDueDate(sType) || sType == AttributeTypeDictionary.LAST_MODIFIED;
    },

    isAttributeTypeNumber: function (sType) {
      switch (sType) {
        case AttributeTypeDictionary.NUMBER:
        case AttributeTypeDictionary.SELLING_PRICE:
        case AttributeTypeDictionary.LIST_PRICE:
        case AttributeTypeDictionary.MAXIMUM_PRICE:
        case AttributeTypeDictionary.MINIMUM_PRICE:
          return true;
        default:
          return false;
      }
    },

    isAttributeTypeMeasurement: function (sType) {
      return _isAttributeTypeMeasurement(sType);
    },

    isAttributeTypeNumeric: function (sType) {
      return this.isAttributeTypeNumber(sType) || _isAttributeTypeMeasurement(sType) || this.isAttributeTypeDate(sType);
    },

    isAttributeTypeTelephone: function (sType) {
      return sType == AttributeTypeDictionary.TEL;
    },

    isAttributeTypeCoverflow: function (sType) {
      switch (sType) {
        case AttributeTypeDictionary.IMAGE_COVERFLOW:
        case AttributeTypeDictionary.COVERFLOW:
          return true;
        default:
          return false;
      }
    },

    isAttributeTypeMetadata: function (sType) {
      switch (sType) {
        case AttributeTypeDictionary.EXIF:
        case AttributeTypeDictionary.IPTC:
        case AttributeTypeDictionary.XMP:
        case AttributeTypeDictionary.OTHER:

        case AttributeTypeDictionary.STANDARD_EXIF:
        case AttributeTypeDictionary.STANDARD_IPTC:
        case AttributeTypeDictionary.STANDARD_XMP:
        case AttributeTypeDictionary.STANDARD_OTHER:
          return true;
        default:
          return false;
      }
    },

    isAttributeTypeUser: function (sType) {
      return sType == AttributeTypeDictionary.LAST_MODIFIED_BY || sType == AttributeTypeDictionary.CREATED_BY;
    },

    isRoleTypeOwner: function (sType) {
      return sType == AttributeTypeDictionary.OWNER;
    },

    isRoleTypeAssignee: function (sType) {
      return sType == AttributeTypeDictionary.ASSIGNEE;
    },

    isAttributeTypeRole: function (sType) {
      return this.isRoleTypeOwner(sType) || this.isRoleTypeAssignee(sType);
    },

    isMandatoryAttribute: function (sType) {
      return (this.isAttributeTypeType(sType)
          || this.isAttributeTypeCoverflow(sType)
          || this.isAttributeTypeRole(sType)
          || this.isAttributeTypeName(sType)
          || this.isAttributeTypeDueDate(sType)
          || this.isAttributeTypeCreatedOn(sType)
          || (sType == AttributeTypeDictionary.LAST_MODIFIED)
          || this.isAttributeTypeUser(sType)
          || this.isAttributeTypeTaxonomy(sType)
      );
    },

    isMeasurementAttributeTypeCustom: function (sType) {
      return sType == AttributeTypeDictionary.CUSTOM_UNIT;
    },

    getLabelByAttributeType: function (sType, sLabel, sDefaultUnit, sPrecision, bDisableFormatByLocale) {
      return _getLabelByAttributeType(sType, sLabel, sDefaultUnit, sPrecision, bDisableFormatByLocale);
    },

    getAttributeValueToShow: function (oAttribute, oMasterAttribute) {
      return _getAttributeValueToShow(oAttribute, oMasterAttribute);
    },

    getMeasurementAttributeValueToShow: function (sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision) {
      return _getMeasurementAttributeValueToShow(sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision);
    },

    getTruncatedValue: function (iVal, sPrecision) {
      return _getTruncatedValue(iVal, sPrecision);
    },

    isAttributeTypeTaxonomy: function (sType) {
      return sType == AttributeTypeDictionary.TAXONOMY;
    },

    isAttributeTypeSecondaryClasses: function (sType) {
      return sType == AttributeTypeDictionary.MULTI_CLASSIFICATION;
    },

    isAttributeTypeCalculated: function (sType) {
      return _isAttributeTypeCalculated(sType);
    },


    isAttributeTypeConcatenated: function (sType) {
      return sType == AttributeTypeDictionary.CONCATENATED;
    },

    isAttributeTypeFile: function (sType) {
      return (
          sType == AttributeTypeDictionary.FILE_TYPE ||
          sType == AttributeTypeDictionary.FILE ||
          sType == AttributeTypeDictionary.SOURCE_FILE
      );
    },

    isAttributeTypePrice: function(sType){
      return sType == AttributeTypeDictionary.PRICE;
    },

    isUserTypeAttribute: function (sType) {
      return _isUserTypeAttribute(sType);
    },

    getBaseUnitFromType: function (sType) {
      return _getBaseUnitFromType(sType);
    },

    canAttributeBeContextual: function (sType) {
      return _canAttributeBeContextual(sType);
    },

    getDateWithTime: function (iTimeStamp) {
      var date = new Date(iTimeStamp);
      if(!CS.isDate(date)) {
        return '';
      }
      var sDate = date.toLocaleDateString() + "";
      var sTime = this.getTimeInHHMM(iTimeStamp);
      return sDate + " " + sTime;
    },

    getTimeInHHMM: function (iTimeStamp) {
      var sDate = new Date(iTimeStamp);
      var HH = sDate.getHours();
      var MM = sDate.getMinutes();
      if (MM < 10) {
        MM = "0" + MM;
      }
      var suffix = "AM";
      if (HH >= 12) {
        suffix = "PM";
        HH = HH - 12;
      }
      if (HH == 0) {
        HH = 12;
      }
      return HH + ":" + MM + " " + suffix;
    },

    getDateValue: function (sDateValue) {
      return _getDateValue(sDateValue);
    },

    isHTMLAttribute: function (sType) {
      return sType == AttributeTypeDictionary.HTML || sType == AttributeTypeDictionary.DESCRIPTION;
    },

    getAllNumericTypeAttributes: function () {
      return _getAllNumericTypeAttributes();
    },

    getAllExcludedAttributeTypeForBulkEdit: function () {
      return _getAllExcludedAttributeTypeForBulkEdit();
    }

  };

})();

export default AttributeUtils;