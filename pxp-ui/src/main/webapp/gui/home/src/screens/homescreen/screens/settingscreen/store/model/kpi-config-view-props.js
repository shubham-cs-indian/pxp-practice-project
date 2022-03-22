import CS from '../../../../../../libraries/cs';

let KpiConfigViewProps = (function () {

  let Props = function () {
    return {
      oKpiValueList: {},
      oActiveKPI: {},
      bKpiScreenLockStatus: false,
      bIsKPIDialogActive: false,
      aKpiGridData: [],
      oSelectedKpi: {},
      sActiveTabId: "kpiInformation",
      aTaxonomyTree: [],
      aAllowedTaxonomies: [],
      oAllowedTaxonomyHierarchyList: {},
      oReferencedData: {},
      oReferencedRule: {},
      oReferencedAttributes: {},
      oReferencedTags: {},
      oActiveBlock: {},
      oSelectedTagMap: {},
      aSelectedDrillDownTags: [],
      oReferencedDashboardTabs: {},
      KpiList:[],
      oTaxonomyPaginationData: {
        from: 0,
        size: 20,
        sortBy: "label",
        sortOrder: "asc",
        types: [],
        searchColumn: "label",
        searchText: "",
      }
    };
  };

  let oProperties = new Props();

  return {
    getKpiValueList: function () {
      return oProperties.oKpiValueList;
    },

    setKpiGridData: function (_aGridData) {
      oProperties.aKpiGridData = _aGridData;
    },

    getKpiGridData: function () {
      return oProperties.aKpiGridData;
    },

    getActiveKPI: function () {
      return oProperties.oActiveKPI;
    },

    setActiveKPI: function (_oActiveKPI) {
      oProperties.oActiveKPI = _oActiveKPI;
    },

    getIsKPIDialogActive: function () {
      return oProperties.bIsKPIDialogActive;
    },

    setIsKPIDialogActive: function (_bIsKPIDialogActive) {
      oProperties.bIsKPIDialogActive = _bIsKPIDialogActive;
    },

    getSelectedKpi: function () {
      return oProperties.oSelectedKpi;
    },

    setSelectedKpi: function (_oSelectedKpi) {
      oProperties.oSelectedKpi = _oSelectedKpi;
    },

    getActiveTabId: function () {
      return oProperties.sActiveTabId;
    },

    setActiveTabId: function (_sActiveTabId) {
      oProperties.sActiveTabId = _sActiveTabId;
    },

    getTaxonomyTree: function () {
      return oProperties.aTaxonomyTree;
    },

    setTaxonomyTree: function (_aTaxonomyTree) {
      oProperties.aTaxonomyTree = _aTaxonomyTree;
    },

    getAllowedTaxonomies: function () {
      return oProperties.aAllowedTaxonomies;
    },

    setAllowedTaxonomies: function (_aAllowedTaxonomies) {
      oProperties.aAllowedTaxonomies = _aAllowedTaxonomies;
    },

    getAllowedTaxonomyHierarchyList: function () {
      return oProperties.oAllowedTaxonomyHierarchyList;
    },

    setAllowedTaxonomyHierarchyList: function (_oAllowedTaxonomyHierarchyList) {
      oProperties.oAllowedTaxonomyHierarchyList = _oAllowedTaxonomyHierarchyList;
    },

    getReferencedData: function () {
      return oProperties.oReferencedData;
    },

    setReferencedData: function (_oLazyData) {
      oProperties.oReferencedData = _oLazyData;
    },

   /* getReferencedRules: function () {
      return oProperties.oReferencedRule;
    },*/

    setReferencedRules: function (_oReferencedRule) {
      oProperties.oReferencedRule = CS.cloneDeep(_oReferencedRule);
    },

    getActiveBlock: function () {
      return oProperties.oActiveBlock;
    },

    setActiveBlock: function (_oActiveBlock) {
      oProperties.oActiveBlock = _oActiveBlock;
    },

    getSelectedTagMap: function () {
      return oProperties.oSelectedTagMap;
    },

    setSelectedTagMap: function (_oTagMap) {
     oProperties.oSelectedTagMap = _oTagMap;
    },

    getSelectedDrillDownTags: function () {
      return oProperties.aSelectedDrillDownTags;
    },

    setSelectedDrillDownTags: function (_aSelectedDrillDownTags) {
      oProperties.aSelectedDrillDownTags = _aSelectedDrillDownTags;
    },

    setReferencedDashboardTabs: function (_oMap) {
      oProperties.oReferencedDashboardTabs = _oMap;
    },

    getReferencedDashboardTabs: function () {
      return oProperties.oReferencedDashboardTabs;
    },

    getTaxonomyPaginationData: function () {
      return oProperties.oTaxonomyPaginationData;
    },

   /* setTaxonomyPaginationData: function (_oTaxonomyPaginationData) {
      oProperties.oTaxonomyPaginationData = _oTaxonomyPaginationData
    },*/

    getReferencedAttributes: function () {
      return oProperties.oReferencedAttributes;
    },

    setReferencedAttributes: function (_oReferencedAttributes) {
      oProperties.oReferencedAttributes = _oReferencedAttributes;
    },

    getReferencedTags: function () {
      return oProperties.oReferencedTags;
    },

    setReferencedTags: function (_oReferencedTags) {
      oProperties.oReferencedTags = _oReferencedTags;
    },

   /* setKpiList: function (kpiList) {
      oProperties.KpiList = ruleList;
    },

    getKpiList: function () {
      return oProperties.KpiList;
    },
*/
    reset: function () {
      oProperties = new Props();
    }

  };
})();

export default KpiConfigViewProps;
