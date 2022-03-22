/**
 * Created by CS49 on 1/9/2017.
 */
/**
 *
 * @param sId
 * @param sLabel
 * @param sValue (mandatory)
 * @param bIsDisabled (mandatory)
 * @param sErrorText
 * @param sRef
 * @param oMasterAttribute (mandatory)
 * @param oProperties
 * @constructor
 */
var ContentAttributeElementViewModel = function (sId, sLabel, sValue, bIsDisabled, sErrorText, sRef, oMasterAttribute, oProperties) {
  this.id = sId;
  this.label = sLabel;
  this.value = sValue;
  this.errorText = sErrorText;
  this.isDisabled = bIsDisabled;
  this.ref = sRef;
  this.masterAttribute = oMasterAttribute;
  this.properties = oProperties
};

export default ContentAttributeElementViewModel;