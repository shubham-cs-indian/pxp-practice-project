import CS from '../../../../../../libraries/cs';
import ImageCoverflowProps from './../model/image-coverflow-view-props';

var ContentLogUtils = (function () {

  var _getDefaultCoverflowProps = function () {

    return {
      imageCoverflowActiveIndex: -1,
      selectedImagesList: {},
      isAttributeImageDialogOpen: false,
      isHeaderImageDialogOpen: false,
      isEditImageDialogOpen: false
    }
  };

  var _getImageCoverflowMapProps = function(){

    return ImageCoverflowProps.getImageCoverflowMapProps();
  };
  /******************************** PUBLIC *****************************/

  return {

    getImageCoverflowActiveIndex: function () {

      return _getImageCoverflowMapProps().imageCoverflowActiveIndex;
    },

    getSelectedImages: function () {
      return _getImageCoverflowMapProps().selectedImagesList || [];
    },

    emptySelectedImages: function () {
      var oImageCoverflowMapProps = _getImageCoverflowMapProps();

      oImageCoverflowMapProps.selectedImagesList = {};
    },

    setImageCoverflowActiveIndex: function (index) {
      _getImageCoverflowMapProps().imageCoverflowActiveIndex = index;
    },

    setSelectedImage: function (sImageKey, oImage) {
      _getImageCoverflowMapProps().selectedImagesList[sImageKey] = oImage;
    },

    setIsAttributeImageDialogOpen: function(bStatus) {
      _getImageCoverflowMapProps().isAttributeImageDialogOpen = bStatus;
    },

    getIsAttributeImageDialogOpen: function() {
      return _getImageCoverflowMapProps().isAttributeImageDialogOpen;
    },

    setIsHeaderImageDialogOpen: function(bStatus) {
      _getImageCoverflowMapProps().isHeaderImageDialogOpen = bStatus;
    },

    getIsHeaderImageDialogOpen: function() {
      return _getImageCoverflowMapProps().isHeaderImageDialogOpen;
    },

    setDefaultPropsForContext: function () {
      var oImageCoverflowMapProps = _getImageCoverflowMapProps();

      var iMaxCoverflowItemAllowed = 0;
      if(oImageCoverflowMapProps){
        iMaxCoverflowItemAllowed = oImageCoverflowMapProps.maxCoverflowItemAllowed;
      }
      oImageCoverflowMapProps = _getDefaultCoverflowProps();
      oImageCoverflowMapProps.maxCoverflowItemAllowed = iMaxCoverflowItemAllowed;
      ImageCoverflowProps.setImageCoverflowMapProps(oImageCoverflowMapProps);
    },

    getPropsForContext: function () {
      return _getImageCoverflowMapProps();
    },

    setMaxCoverflowItemAllowed: function(iMaxCoverflowItemAllowed) {
      var oImageCoverflowMapProps = _getImageCoverflowMapProps();

      if(!oImageCoverflowMapProps) {
        oImageCoverflowMapProps = {};
      }
      oImageCoverflowMapProps.maxCoverflowItemAllowed = iMaxCoverflowItemAllowed;
    },

    setOriginalAssetObjectList: function(aAssetObjectList) {
      ImageCoverflowProps.setOriginalAssetObjectList(aAssetObjectList);
    },

    getOriginalAssetObjectList: function() {
      return ImageCoverflowProps.getOriginalAssetObjectList();
    },

    removeFromActiveVideoStatusRequests: function(sKey) {
      var aActiveVideoStatusRequests = ImageCoverflowProps.getActiveVideoStatusRequests();
      ImageCoverflowProps.setActiveVideoStatusRequests(CS.WITHOUT(aActiveVideoStatusRequests, sKey));
    },

    getActiveVideoStatusRequests: function() {
      return ImageCoverflowProps.getActiveVideoStatusRequests();
    },

    getIsEditImageDialogOpen: function () {
      return _getImageCoverflowMapProps().isEditImageDialogOpen;
    },

    setIsEditImageDialogOpen: function (bIsEditImageDialogOpen) {
      _getImageCoverflowMapProps().isEditImageDialogOpen = bIsEditImageDialogOpen;
    },

    resetActiveVideoStatusRequests: function() {
      ImageCoverflowProps.setActiveVideoStatusRequests([]);
    }
  }

})();

export default ContentLogUtils;
