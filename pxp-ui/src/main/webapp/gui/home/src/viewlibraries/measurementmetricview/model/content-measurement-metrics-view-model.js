/**
 * @param sId
 * @param sLabel
 * @param sValue
 * @param sDefaultUnit
 * @param sBaseUnit
 * @param sPlaceholder
 * @param sErrorText
 * @param sDescription
 * @param bIsDisabled
 * @param bConverterVisibility
 * @param aConverterList
 * @param oProperties
 * @param iPrecision
 * @constructor
 */
var ContentMeasurementMetricsViewModel = function (sId, sLabel, sValue, sDefaultUnit, sBaseUnit, sPlaceholder, sErrorText, sDescription, bIsDisabled,
                                                   bConverterVisibility, aConverterList, oProperties, iPrecision, sMeasurementType) {

  this.id = sId;
  this.label = sLabel;
  this.value = sValue;
  this.defaultUnit = sDefaultUnit;
  this.placeholder = sPlaceholder;
  this.errorText = sErrorText;
  this.description = sDescription;
  this.isDisabled = bIsDisabled;
  this.converterVisibility = bConverterVisibility;
  this.converterList = aConverterList;
  this.properties = oProperties;
  this.precision = iPrecision;
  this.baseUnit = sBaseUnit;
  this.measurementAttributeType = sMeasurementType;

};

export default ContentMeasurementMetricsViewModel;