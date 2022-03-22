let assetLinkSharingProps = (() => {

  let Props = function () {
    return {
      oInstancesData: {},
      bShowAssetLinkSharingDialog: false,
      oGridViewSkeleton: {},
      aGridViewData: [],
      oGridViewVisualData: {},
      aMasterAssetTypeIdData: [],
      aTechnicalVariantTypeIdData: [],
      oMasterAsset: {
        id: "",
        canDownload: false
      },
      aMasterAssetID: [],
      iTotalNestedItems: 0,
      sAssetDownloadContext: "",
      sAssetShareContext: "",
    }
  };

  let Properties = new Props();

  return {

    getShowAssetLinkSharingDialog: function () {
      return Properties.bShowAssetLinkSharingDialog;
    },

    setShowAssetLinkSharingDialog: function (_bShowAssetLinkSharingDialog) {
      Properties.bShowAssetLinkSharingDialog = _bShowAssetLinkSharingDialog;
    },

    getGridViewSkeleton: function () {
      return Properties.oGridViewSkeleton;
    },

    setGridViewSkeleton: function (_oGridViewSkeleton) {
      Properties.oGridViewSkeleton = _oGridViewSkeleton;
    },

    getGridViewData: function () {
      return Properties.aGridViewData;
    },

    setGridViewData: function (_aGridViewData) {
      Properties.aGridViewData = _aGridViewData;
    },

    getGridViewVisualData: function () {
      return Properties.oGridViewVisualData;
    },

    setGridViewVisualData: function (_oGridViewVisualData) {
      Properties.oGridViewVisualData = _oGridViewVisualData;
    },

    getGridViewPaginationData: function () {
      return Properties.oGridViewPaginationData;
    },

    setGridViewPaginationData: function (_oGridViewPaginationData) {
      Properties.oGridViewPaginationData = _oGridViewPaginationData;
    },

    setMasterAssetTypeIDData: function (_aMasterAssetTypeIdData) {
      Properties.aMasterAssetTypeIdData = _aMasterAssetTypeIdData;
    },

    getTechnicalVariantTypeIdsList: function () {
      return Properties.aTechnicalVariantTypeIdData;
    },

    setTechnicalVariantTypeIdsList: function (_aTechnicalVariantTypeIdData) {
      Properties.aTechnicalVariantTypeIdData = _aTechnicalVariantTypeIdData;
    },

    getMasterAssetID: function () {
      return Properties.aMasterAssetID;
    },

    setMasterAssetID: function (_aMasterAssetID) {
      Properties.aMasterAssetID = _aMasterAssetID;
    },

    getTotalNestedItems: function (){
      return Properties.iTotalNestedItems;
    },

    setTotalNestedItems: function (_iTotalNestedItems){
      Properties.iTotalNestedItems = _iTotalNestedItems;
    },

    getAssetShareContext: function () {
      return Properties.sAssetShareContext;
    },

    setAssetShareContext: function (_sAssetShareContext) {
      Properties.sAssetShareContext = _sAssetShareContext;
    },

    reset: () => {
      Properties = new Props();
    },

    toJSON: () => {
      return Properties;
    }
  }

})();

export default assetLinkSharingProps;
