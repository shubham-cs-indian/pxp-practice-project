let contentBulkDownloadAssetProps = (() => {

  let Props = function () {
    return {
      aDownloadInfo: [],
      sDownloadComments: "",
      sDownloadFileName: "PXP Asset",
      oActiveDownloadDialogData: {},
      bIsFolderByAsset: false,
      bShowDownloadDialog: false,
      bIsDownloadDialogToggleButtonOn: false,
      bShouldDownloadAssetWithOriginalFilename: false,
      iTotalSelectedAssetsCount: 0,
      iTotalSelectedAssetsSize: 0,
      iExpandedParentIndex: -1,
      oDownloadAsExtraData: {},
      bIsToggleButtonDisabled: false,
      oIdVsNameModel: {},
      aInvalidRowIds: [],
      sSelectedContentId: "",
      sSelectedContextId: "",
    }
  };

  let Properties = new Props();

  return {

    getDownloadInfo: function () {
      return Properties.aDownloadInfo;
    },

    setDownloadInfo: function (_aDownloadInfo) {
      Properties.aDownloadInfo = _aDownloadInfo;
    },

    getIsFolderByAsset: function () {
      return Properties.bIsFolderByAsset;
    },

    setIsFolderByAsset: function (_bIsFolderByAsset) {
      Properties.bIsFolderByAsset = _bIsFolderByAsset;
    },

    getShowDownloadDialog: function() {
      return Properties.bShowDownloadDialog;
    },

    setShowDownloadDialog: function (_bShowDownloadDialog) {
      Properties.bShowDownloadDialog = _bShowDownloadDialog;
    },

    getDownloadComments: function () {
      return Properties.sDownloadComments;
    },

    setDownloadComments: function (_sDownloadComments) {
      Properties.sDownloadComments = _sDownloadComments;
    },

    getDownloadFileName: function () {
      return Properties.sDownloadFileName;
    },

    setDownloadFileName: function (_sDownloadFileName) {
      Properties.sDownloadFileName = _sDownloadFileName;
    },

    getActiveDownloadDialogData: function () {
      return Properties.oActiveDownloadDialogData;
    },

    setActiveDownloadDialogData: function (_oActiveDownloadDialogData) {
      Properties.oActiveDownloadDialogData = _oActiveDownloadDialogData;
    },

    getShouldDownloadAssetWithOriginalFilename: function() {
      return Properties.bShouldDownloadAssetWithOriginalFilename;
    },

    setShouldDownloadAssetWithOriginalFilename: function (_bShouldDownloadAssetWithOriginalFilename) {
      Properties.bShouldDownloadAssetWithOriginalFilename = _bShouldDownloadAssetWithOriginalFilename;
    },

    getIsDownloadDialogToggleButtonOn: function() {
      return Properties.bIsDownloadDialogToggleButtonOn;
    },

    setIsDownloadDialogToggleButtonOn: function (_bIsDownloadDialogToggleButtonOn) {
      Properties.bIsDownloadDialogToggleButtonOn = _bIsDownloadDialogToggleButtonOn;
    },

    getTotalSelectedAssetsCount: function() {
      return Properties.iTotalSelectedAssetsCount;
    },

    setTotalSelectedAssetsCount: function (_iTotalSelectedAssetsCount) {
      Properties.iTotalSelectedAssetsCount = _iTotalSelectedAssetsCount;
    },

    getTotalSelectedAssetsSize: function() {
      return Properties.iTotalSelectedAssetsSize;
    },

    setTotalSelectedAssetsSize: function (_iTotalSelectedAssetsSize) {
      Properties.iTotalSelectedAssetsSize = _iTotalSelectedAssetsSize;
    },

    getExpandedParentIndex: function() {
      return Properties.iExpandedParentIndex;
    },

    setExpandedParentIndex: function (_iExpandedParentIndex) {
      Properties.iExpandedParentIndex = _iExpandedParentIndex;
    },

    getDownloadAsExtraData: function () {
      return Properties.oDownloadAsExtraData;
    },

    setDownloadAsExtraData: function (oDownloadAsExtraData) {
      Properties.oDownloadAsExtraData = oDownloadAsExtraData;
    },

    getIsToggleButtonDisabled: function () {
      return Properties.bIsToggleButtonDisabled;
    },

    setIsToggleButtonDisabled: function (_bIsToggleButtonDisabled) {
      Properties.bIsToggleButtonDisabled = _bIsToggleButtonDisabled;
    },

    getIdVsNameModel: function () {
      return Properties.oIdVsNameModel;
    },

    setIdVsNameModel: function (_oIdVsNameModel) {
      Properties.oIdVsNameModel = _oIdVsNameModel;
    },

    getInvalidRowIds: function () {
      return Properties.aInvalidRowIds;
    },

    setInvalidRowIds: function (_aInvalidRowIds) {
      Properties.aInvalidRowIds = _aInvalidRowIds;
    },

    getSelectedContentId: function () {
      return Properties.sSelectedContentId;
    },

    setSelectedContentId: function (_sSelectedContentId) {
      Properties.sSelectedContentId = _sSelectedContentId;
    },

    getSelectedContextId: function () {
      return Properties.sSelectedContextId;
    },

    setSelectedContextId: function (_sSelectedContextId) {
      Properties.sSelectedContextId = _sSelectedContextId;
    },

    reset: () => {
      Properties = new Props();
    },

    toJSON: () => {
      return Properties;
    }
  }

})();

export default contentBulkDownloadAssetProps;
