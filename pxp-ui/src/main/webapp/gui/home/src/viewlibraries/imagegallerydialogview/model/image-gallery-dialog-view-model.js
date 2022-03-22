/***
 *
 * @param sId
 * @param oActiveEntityDetails
 * @param aAssetList
 * @param bDialogVisibilityStatus
 * @param bHasCloseIcon
 * @param oProperties
 * @constructor
 */
var ImageGalleryViewDialogViewModel = function (sId, oActiveEntityDetails, aAssetList, bDialogVisibilityStatus, bHasCloseIcon, oProperties) {


  this.id = sId;
  this.activeEntityDetails = oActiveEntityDetails;
  this.assetList = aAssetList;
  this.dialogVisibilityStatus = bDialogVisibilityStatus;
  this.hasCloseIcon = bHasCloseIcon;
  this.properties = oProperties;

};

export default ImageGalleryViewDialogViewModel;