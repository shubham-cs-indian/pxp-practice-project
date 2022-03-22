
var ImageCoverflowItemModel = function (sKey, sId, sSrc, bIsActive, bIsSelected, bIsDefault, sOrigSrc, sPreviewSrc, sType, sFileName, sDescription, oProperties) {

  this.id = sId;
  this.coverflowImageKey = sKey;
  this.coverflowSrc = sSrc;
  this.coverflowAssetObjectSrc = sOrigSrc;
  this.coverflowType = sType;
  this.properties = oProperties;
  this.isActive = bIsActive;
  this.isSelected = bIsSelected;
  this.isDefault = bIsDefault;
  this.previewImageSrc = sPreviewSrc;
  this.fileName = sFileName;
  this.description = sDescription;

};

export default ImageCoverflowItemModel;
