/**
 * Created by cs05 on 16/05/17.
 */

var TimelineProps = (function () {

  var Props = function () {
    return {
      zoomLevel: 1,
      versions: [],
      selectedVersionIds: [],
      isComparisonMode: false,
      comparisonLayoutData: {},
      maxSelectedVersionCount: 8,
      showCreatedOnInfo: false,
      isArchiveVisible : false,
      numberOfAllowedVersions : 0,
      sSelectedLanguageForComparison: "",
      versionComparisionLanguages: [],
      sSelectedHeaderButtonId: ""
    }
  };

  var oProperties = new Props();

  return {

    getSelectedLanguageForComparison: function () {
      return oProperties.sSelectedLanguageForComparison;
    },

    setSelectedLanguageForComparison: function (_sSelectedLanguageForComparison) {
      oProperties.sSelectedLanguageForComparison = _sSelectedLanguageForComparison;
    },

    getZoomLevel: function () {
      return oProperties.zoomLevel;
    },

    setZoomLevel: function (iZoomLevel) {
      oProperties.zoomLevel = iZoomLevel;
    },

    getVersions: function () {
      return oProperties.versions;
    },

    setVersions: function (aVersions) {
      oProperties.versions = aVersions;
    },

    getSelectedVersionIds: function () {
      return oProperties.selectedVersionIds;
    },

    setSelectedVersionIds: function (aSelectedVersionIds) {
      oProperties.selectedVersionIds = aSelectedVersionIds;
    },

    getIsComparisonMode: function () {
      return oProperties.isComparisonMode;
    },

    setIsComparisonMode: function (bIsComparisonMode) {
      oProperties.isComparisonMode = bIsComparisonMode;
    },

    getComparisonLayoutData: function () {
      return oProperties.comparisonLayoutData;
    },

    setComparisonLayoutData: function (oComparisonLayoutData) {
      oProperties.comparisonLayoutData = oComparisonLayoutData;
    },

    getMaxSelectedVersionCount: function () {
      return oProperties.maxSelectedVersionCount;
    },

    getIsCreatedOnVisible: function () {
      return oProperties.showCreatedOnInfo;
    },

    setIsCreatedOnVisible: function (bShowCreatedOnInfo) {
      oProperties.showCreatedOnInfo = bShowCreatedOnInfo;
    },

    getIsArchiveVisible : function () {
      return oProperties.isArchiveVisible;
    },

    setIsArchiveVisible : function (bArchiveFlag) {
      oProperties.isArchiveVisible = bArchiveFlag;
    },

    getNumberOfAllowedVersions : function () {
      return oProperties.numberOfAllowedVersions;
    },

    setNumberOfAllowedVersions : function (iNumberOfAllowedVersions) {
      oProperties.numberOfAllowedVersions = iNumberOfAllowedVersions;
    },

    getVersionComparisionLanguages : function () {
      return oProperties.versionComparisionLanguages;
    },

    setVersionComparisionLanguages : function (_versionComparisionLanguages) {
      oProperties.versionComparisionLanguages = _versionComparisionLanguages;
    },

    getSelectedHeaderButtonId: function() {
      return oProperties.sSelectedHeaderButtonId;
    },

    setSelectedHeaderButtonId: function(_sSelectedHeaderButtonId) {
      oProperties.sSelectedHeaderButtonId = _sSelectedHeaderButtonId;
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

export default TimelineProps;
