/**
 * Created by CS56 on 8/7/2016.
 */

import CS from '../../../../../../libraries/cs';

import DefaultPaginationData from '../../tack/mock/default-pagination-data';

var RuleConfigViewProps = (function () {

  let Props = function () {
    return {
      oRulesValueList: {},
      oActiveRule: {},
      bRuleScreenLockStatus: false,
      bIsRuleDialogActive: false,
      aRuleGridData:[],
      oReferencedData:{},
      oActiveBlock:{},
      sActiveTabId:"",
      bRightPanelActive: false,
      rulesList:[],
      oRightBarIconsMap: {
        ruleList: false
      },
      sKlassTypeId: "",
      aTaxonomyTree: [],
      aAllowedTaxonomies: [],
      oAllowedTaxonomyConfigDetails: {},
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
      dataLanguages: [],
    };
  };

  let oProperties = new Props();

  return {
    getRuleValuesList: function () {
      return oProperties.oRulesValueList;
    },

    setRuleValuesList: function (_oRulesValueList) {
      oProperties.oRulesValueList = _oRulesValueList;
    },

    getRuleScreenLockStatus: function () {
      return oProperties.bRuleScreenLockStatus;
    },

    setRuleScreenLockStatus: function (_status) {
      oProperties.bRuleScreenLockStatus = _status;
    },

    getActiveRule: function () {
      return oProperties.oActiveRule;
    },

    setActiveRule: function (_sActiveRuleId) {
      oProperties.oActiveRule = _sActiveRuleId;
    },

    getRightPanelVisibility: function(){
      return oProperties.bRightPanelActive;
    },

    setRightPanelVisibility: function(_bVal){
      oProperties.bRightPanelActive = _bVal;
    },

    getRightBarIconClickMap: function () {
      return oProperties.oRightBarIconsMap;
    },

    setRightBarIconClickMap: function (sKey, bVal) {
      return oProperties.oRightBarIconsMap;
    },

    getRuleViolationsAndNormalizationProps: function () {
      return oProperties.aRuleViolationAndNormalizationProps;
    },

    setRuleViolationsAndNormalizationProps: function (_aRuleViolationAndNormalizationProps) {
      oProperties.aRuleViolationAndNormalizationProps = _aRuleViolationAndNormalizationProps;
    },

    getKlassTypeId: function () {
      return oProperties.sKlassTypeId;
    },

    setKlassTypeId: function (_sKlassTypeId) {
      oProperties.sKlassTypeId = _sKlassTypeId;
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

    getAllowedTaxonomyConfigDetails: function () {
      return oProperties.oAllowedTaxonomyConfigDetails;
    },

    setAllowedTaxonomyConfigDetails: function (_oAllowedTaxonomyConfigDetails) {
      oProperties.oAllowedTaxonomyConfigDetails = _oAllowedTaxonomyConfigDetails;
    },

    getAllowedTaxonomyHierarchyList: function () {
      return oProperties.oAllowedTaxonomyHierarchyList;
    },

    setAllowedTaxonomyHierarchyList: function (_oAllowedTaxonomyHierarchyList) {
      oProperties.oAllowedTaxonomyHierarchyList = _oAllowedTaxonomyHierarchyList;
    },

    getSearchText: function () {
      return oProperties.sSearchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getSearchConstantData: function () {
      return oProperties.oSearchConstantData;
    },

    setSearchConstantData: function (_oSearchConstantData) {
      oProperties.oSearchConstantData = _oSearchConstantData;
    },

    getTaxonomyPaginationData: function () {
      return oProperties.oTaxonomyPaginationData;
    },

    setTaxonomyPaginationData: function (_oTaxonomyPaginationData) {
      oProperties.oTaxonomyPaginationData = _oTaxonomyPaginationData;
    },

    getFrom: function () {
      return oProperties.iFrom
    },

    setFrom: function (_iFrom) {
      oProperties.iFrom = _iFrom
    },

    resetPaginationData: function () {
      oProperties.sSearchText = "";
      oProperties.oSearchConstantData = CS.cloneDeep(DefaultPaginationData);
      oProperties.iFrom = 0;
    },

    getRuleGridData: function () {
      return oProperties.aRuleGridData;
    },

    setRuleGridData: function (_aGridData) {
      oProperties.aRuleGridData = _aGridData;
    },

    setActiveTabId: function (_sActiveTabId) {
      oProperties.sActiveTabId = _sActiveTabId;
    },

    setActiveBlock: function (_oActiveBlock) {
      oProperties.oActiveBlock = _oActiveBlock;
    },

    getActiveBlock: function () {
      return oProperties.oActiveBlock;
    },

    setIsRuleDialogActive: function (_bIsKPIDialogActive) {
      oProperties.bIsRuleDialogActive = _bIsKPIDialogActive;
    },

    getIsRuleDialogActive: function () {
      return oProperties.bIsRuleDialogActive;
    },

    setRuleList: function (ruleList) {
      oProperties.rulesList = ruleList;
    },

    getRuleList: function () {
      return oProperties.rulesList;
    },

    setDataLanguages: function (aLanguageList) {
      oProperties.dataLanguages = aLanguageList;
    },

    getDataLanguages: function () {
      return oProperties.dataLanguages;
    },

    getReferencedData: function () {
      return oProperties.oReferencedData;
    },

    reset: function () {
      oProperties = new Props();
    },

  };
})();

export default RuleConfigViewProps;