/**
 * @param sId
 * @param sLabel
 * @param iSize
 * @param sExtension
 * @param bIsDisabled
 * @param bIsInvalid
 * @param sInvalidMessage
 * @constructor
 */
var DownloadDialogListViewModel = function (sId, sLabel, iSize, sExtension, bIsChecked, bIsDisabled, bIsInvalid, sInvalidMessage) {
  this.id = sId;
  this.size = iSize;
  this.label = sLabel;
  this.isInvalid = bIsInvalid;
  this.extension = sExtension;
  this.isChecked = bIsChecked;
  this.isDisabled = bIsDisabled;
  this.invalidMessage = sInvalidMessage;
};

export default DownloadDialogListViewModel;

