import DefaultPaginationData from '../../tack/mock/default-pagination-data';
import CS from '../../../../../../libraries/cs';

var GoldenRecordsConfigViewProps = (function () {

  let Props = function () {
    return {
      oRulesValueList: {},
      bIsRuleDialogActive: false,
      aRuleGridData:null,
      oReferencedData:{},
      oActiveBlock:{},
      sActiveTabId:null,
      oGoldenRecordRules: {},
      oActiveGoldenRecordRule: {},
      sKlassTypeId: null,
      bRuleScreenLockStatus: false,
      bRightPanelActive: false,
      rulesList:[],
      oRightBarIconsMap: {
        ruleList: false
      },
      aTaxonomyTree: [],
      aAllowedTaxonomies: [],
      oAllowedTaxonomyHierarchyList: {},
      aRuleViolationAndNormalizationProps: [],
      oNormalizedFieldVisibilityStatus: {},
      sSearchText: "",
      oSearchConstantData: CS.clone(DefaultPaginationData),
      iFrom: 0,
      oTaxonomyPaginationData: {
        from: 0,
        size: 20,
        sortBy: "label",
        sortOrder: "asc",
        types: [],
        searchColumn: "label",
        searchText: "",
      },
    };
  };

  let oProperties = new Props();

  return {

    getActiveGoldenRecordRule: function () {
      return oProperties.oActiveGoldenRecordRule;
    },

    setActiveGoldenRecordRule: function (_oActiveGoldenRecordRule) {
      oProperties.oActiveGoldenRecordRule = _oActiveGoldenRecordRule;
    },

    getSearchText: function () {
      return oProperties.sSearchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getFrom: function () {
      return oProperties.iFrom
    },

    setFrom: function (_iFrom) {
      oProperties.iFrom = _iFrom
    },

    getSearchConstantData: function () {
      return oProperties.oSearchConstantData;
    },

   /* setSearchConstantData: function (_oSearchConstantData) {
      oProperties.oSearchConstantData = _oSearchConstantData;
    },*/

    getGoldenRecordRuleScreenLockStatus: function () {
      return oProperties.bRuleScreenLockStatus;
    },

    setGoldenRecordRuleScreenLockStatus: function (_status) {
      oProperties.bRuleScreenLockStatus = _status;
    },

    getGoldenRecordRuleValuesList: function () {
      return oProperties.oRulesValueList;
    },

   /* setGoldenRecordRuleValuesList: function (_oRulesValueList) {
      oProperties.oRulesValueList = _oRulesValueList;
    },

    getKlassTypeId: function () {
      return oProperties.sKlassTypeId;
    },*/

    setKlassTypeId: function (_sKlassTypeId) {
      oProperties.sKlassTypeId = _sKlassTypeId;
    },

    getRightPanelVisibility: function () {
      return oProperties.bRightPanelActive;
    },

    setRightPanelVisibility: function (_bVal) {
      oProperties.bRightPanelActive = _bVal;
    },
    getRightBarIconClickMap: function () {
      return oProperties.oRightBarIconsMap;
    },

  /*  setRightBarIconClickMap: function (sKey, bVal) {
      return oProperties.oRightBarIconsMap;
    },*/

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

    getTaxonomyPaginationData: function () {
      return oProperties.oTaxonomyPaginationData;
    },

   /* setTaxonomyPaginationData: function (_oTaxonomyPaginationData) {
      oProperties.oTaxonomyPaginationData = _oTaxonomyPaginationData;
    },*/

    getRuleGridData: function () {
      return oProperties.aRuleGridData;
    },
  /*  setActiveRule: function (_sActiveRule) {
      oProperties.oActiveGoldenRecordRule = _sActiveRule;
    },

    getActiveRule: function () {
      // oProperties.oActiveGoldenRecordRule;
    },
    setActiveBlock: function (_oActiveBlock) {
      oProperties.oActiveBlock = _oActiveBlock;
    },
    getActiveBlock: function () {
      return oProperties.oActiveBlock;
    },*/
    setIsRuleDialogActive: function (_bIsGOldenRuleDialogActive) {
      oProperties.bIsRuleDialogActive = _bIsGOldenRuleDialogActive;
    },
    getReferencedData: function () {
      return oProperties.oReferencedData;
    },
    getIsRuleDialogActive: function () {
      return oProperties.bIsRuleDialogActive;
    },
    setGoldenRecordRuleGridData: function (_aGridData) {
      oProperties.aRuleGridData = _aGridData;
    },

    getRuleList: function () {
      return oProperties.rulesList;
    },

    setRuleList: function (aRuleList) {
       oProperties.rulesList = aRuleList;
    },

    reset: function () {
      oProperties = new Props();
    },

  };
})();

export default GoldenRecordsConfigViewProps;
