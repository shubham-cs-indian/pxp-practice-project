
var InformationTabProps = (function () {

  var Props = function () {
    return {
      lastModifiedContentsList: [],
      latestCreatedContentsList: [],
      isRuleViolatedContentScreenOpen: false,
      dashboardRuleViolationTileData: [],
      assetClassList: [],
      showBulkUploadDialog: false,
      isLastModifiedListLoading: false,
      isLatestCreatedListLoading: false,
    }
  };

  var oProperties = new Props();

  return {

    setLastModifiedContentsList: function (_aLastModifiedContentsList) {
      oProperties.lastModifiedContentsList = _aLastModifiedContentsList;
    },

    getLastModifiedContentsList: function () {
      return oProperties.lastModifiedContentsList;
    },

    setLatestCreatedContentsList: function (_aLatestCreatedContentsList) {
      oProperties.latestCreatedContentsList = _aLatestCreatedContentsList;
    },

    getLatestCreatedContentsList: function () {
      return oProperties.latestCreatedContentsList;
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

    getLastModifiedListLoading: function () {
      return oProperties.isLastModifiedListLoading;
    },

    setLastModifiedListLoading: function (_bIsLastModifiedListLoading){
      oProperties.isLastModifiedListLoading = _bIsLastModifiedListLoading;
    },

    getLatestCreatedListLoading: function () {
      return oProperties.isLatestCreatedListLoading;
    },

    setLatestCreatedListLoading: function (_bIsLatestCreatedListLoading){
      oProperties.isLatestCreatedListLoading = _bIsLatestCreatedListLoading;
    },

    reset: () => {
      oProperties = new Props();
    },
  };

})();

export default InformationTabProps;