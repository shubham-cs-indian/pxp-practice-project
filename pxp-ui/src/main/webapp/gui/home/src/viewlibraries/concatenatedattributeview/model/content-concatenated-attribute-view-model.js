/**
 * Created by CS49 on 1/2/2017.
 */

/***
 *
 * @param sId
 * @param sLabel
 * @param sValue
 * @param sPlaceholder
 * @param sErrorText
 * @param sDescription
 * @param bIsDisabled
 * @param aExpressionList
 * @param oProperties
 * @constructor
 */
var ContentConcatenatedAttributeViewModel = function (sId, sLabel, sValue, sPlaceholder, sErrorText, sDescription,
                                                      bIsDisabled, bShowDisconnected, aExpressionList, oProperties) {
  this.id = sId;
  this.label = sLabel;
  this.value = sValue;
  this.placeholder = sPlaceholder;
  this.errorText = sErrorText;
  this.description = sDescription;
  this.isDisabled = bIsDisabled;
  this.showDisconnected = bShowDisconnected;
  this.expressionList = aExpressionList;
  this.properties = oProperties;
};

export default ContentConcatenatedAttributeViewModel;