
let DamInformationTabProps = (function () {

  let Props = function () {
    return {
      lastModifiedAssetList: [],
      latestCreatedAssetList: [],
      expiredAssetList:[],
      assetsAboutToExpireList: [],
      isRuleViolatedContentScreenOpen: false,
      dashboardRuleViolationTileData: [],
      assetClassList: [],
      showBulkUploadDialog: false,
      duplicateAssetsList: [],
    }
  };

  let oProperties = new Props();

  return {

    setLastModifiedAssetList: function (_aLastModifiedContentsList) {
      oProperties.lastModifiedAssetList = _aLastModifiedContentsList;
    },

    getLastModifiedAssetList: function () {
      return oProperties.lastModifiedAssetList;
    },

    setLatestCreatedAssetList: function (_aLatestCreatedContentsList) {
      oProperties.latestCreatedAssetList = _aLatestCreatedContentsList;
    },

    getLatestCreatedAssetList: function () {
      return oProperties.latestCreatedAssetList;
    },

    setExpiredAssetList: function (_aExpiredAssetList) {
      oProperties.expiredAssetList = _aExpiredAssetList;
    },

    getExpiredAssetList: function () {
      return oProperties.expiredAssetList;
    },

    setIsRuleViolatedContentsScreen: function (_bIsRuleViolatedContentScreenOpen) {
      oProperties.isRuleViolatedContentScreenOpen = _bIsRuleViolatedContentScreenOpen;
    },

    getIsRuleViolatedContentsScreen: function () {
      return oProperties.isRuleViolatedContentScreenOpen;
    },

    setDashboardRuleViolationTileData: function (_aDashboardRuleViolationTileData) {
      oProperties.dashboardRuleViolationTileData = _aDashboardRuleViolationTileData;
    },

    getDashboardRuleViolationTileData: function () {
      return oProperties.dashboardRuleViolationTileData;
    },

    setShowBulkUploadDialog: function (_showBulkUploadDialog) {
      oProperties.showBulkUploadDialog = _showBulkUploadDialog;
    },

    getShowBulkUploadDialog: function () {
      return oProperties.showBulkUploadDialog;
    },

    setAssetClassList: function (_assetClassList) {
      oProperties.assetClassList = _assetClassList;
    },

    getAssetClassList: function () {
      return oProperties.assetClassList;
    },

    setAssetAboutToExpireList: function (_assetsAboutToExpireList) {
      oProperties.assetsAboutToExpireList = _assetsAboutToExpireList;
    },

    getAssetAboutToExpireList: function () {
      return oProperties.assetsAboutToExpireList;
    },

    setDuplicateAssetsList: function (_duplicateAssetsList) {
      oProperties.duplicateAssetsList = _duplicateAssetsList;
    },

    getDuplicateAssetsList: function () {
      return oProperties.duplicateAssetsList;
    },

    reset: () => {
      oProperties = new Props();
    },
  };

})();

export default DamInformationTabProps;
