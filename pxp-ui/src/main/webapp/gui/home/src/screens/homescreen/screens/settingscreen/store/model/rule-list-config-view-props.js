/**
 * Created by CS49 on 8/9/2016.
 */

import CS from '../../../../../../libraries/cs';

import DefaultPaginationData from '../../tack/mock/default-pagination-data';

var RuleListConfigViewProps = (function () {

  let Props = function () {
    return {
      oRulesListValueList: {},
      oActiveRuleList: {},
      bRuleListScreenLockStatus: false,
      sSearchText: "",
      oSearchConstantData: CS.cloneDeep(DefaultPaginationData),
      iFrom: 0,
      bShowLoadMore: true,
    };
  };

  let oProperties = new Props();

  return {
    getRuleListValuesList: function () {
      return oProperties.oRulesListValueList;
    },

    setRuleListValuesList: function (_oRulesValueList) {
      oProperties.oRulesListValueList = _oRulesValueList;
    },

    getRuleListScreenLockStatus: function () {
      return oProperties.bRuleListScreenLockStatus;
    },

    setRuleListScreenLockStatus: function (_status) {
      oProperties.bRuleListScreenLockStatus = _status;
    },

    getActiveRuleList: function () {
      return oProperties.oActiveRuleList;
    },

    getCurrentRuleList: function () {
      return oProperties.oActiveRuleList.clonedObject ? oProperties.oActiveRuleList.clonedObject : oProperties.oActiveRuleList;
    },

    setActiveRuleList: function (_sActiveRuleId) {
      oProperties.oActiveRuleList = _sActiveRuleId;
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

    getFrom: function () {
      return oProperties.iFrom
    },

    setFrom: function (_iFrom) {
      oProperties.iFrom = _iFrom
    },

    getShowLoadMore: function () {
      return oProperties.bShowLoadMore
    },

    setShowLoadMore: function (_bShowLoadMore) {
      oProperties.bShowLoadMore = _bShowLoadMore
    },

    resetPaginationData: function () {
      oProperties.sSearchText = "";
      oProperties.oSearchConstantData = CS.cloneDeep(DefaultPaginationData);
      oProperties.iFrom = 0;
      oProperties.bShowLoadMore = true;
    },

    reset: function () {
      oProperties = new Props();
    },

  };
})();

export default RuleListConfigViewProps;