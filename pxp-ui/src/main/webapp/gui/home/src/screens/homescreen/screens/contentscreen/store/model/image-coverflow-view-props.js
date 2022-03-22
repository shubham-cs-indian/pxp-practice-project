var ImageCoverflowViewProps = (function () {

  var Props = function () {
    return {
      aOriginalAssetObjectList: [],
      oImageCoverflowMapProps: {},
      aActiveVideoStatusRequests: [],
      bulkUploadFiles: []
    }
  };

  var oProperties = new Props();

  return {

    getImageCoverflowMapProps: function(){
      return oProperties.oImageCoverflowMapProps;
    },

    setImageCoverflowMapProps: function(oImageCoverflowMapProps){
      oProperties.oImageCoverflowMapProps = oImageCoverflowMapProps;
    },

    getOriginalAssetObjectList: function() {
      return oProperties.aOriginalAssetObjectList;
    },

    setOriginalAssetObjectList: function(aAssetObjectList) {
      oProperties.aOriginalAssetObjectList =  aAssetObjectList;
    },

    getActiveVideoStatusRequests: function() {
      return oProperties.aActiveVideoStatusRequests;
    },

    getBulkUploadFiles: function() {
      return oProperties.bulkUploadFiles;
    },

    setBulkUploadFiles: function (aFiles) {
      oProperties.bulkUploadFiles = aFiles;
    },

    setActiveVideoStatusRequests: function(aRequests) {
      oProperties.aActiveVideoStatusRequests = aRequests;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {
      };
    }
  };

})();

export default ImageCoverflowViewProps;
