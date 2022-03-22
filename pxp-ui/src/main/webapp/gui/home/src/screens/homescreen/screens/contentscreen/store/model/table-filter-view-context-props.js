import CS from '../../../../../../libraries/cs';

var TableFilterViewContextProps = function () {

  var aAvailableFilterData = [];
  var aAppliedFilterData = [];
  var aAppliedFilterDataClone = null;
  var bIsFilterDirty = false;
  var sAllSearchText = "";
  var filterInfo = {};
  var oTimeRangeData = {
    from: null,
    to: null
  };
  var oSortData = {
    sortField: null,
    sortOrder: "asc"
  };
  var oPaginationData = {
    from: 0,
    pageSize: 20,
    totalItems: 0
  };

  return {

    getAvailableFilters: function () {
      return aAvailableFilterData;
    },

    setAvailableFilters: function (_aAvailableFilterData) {
      aAvailableFilterData = _aAvailableFilterData;
    },

    getAppliedFilters: function () {
      return aAppliedFilterData;
    },

    setAppliedFilters: function (aAppliedFilters) {
      aAppliedFilterData = aAppliedFilters;
    },

    createAppliedFiltersClone: function () {
      aAppliedFilterDataClone = CS.cloneDeep(aAppliedFilterData);
    },

    getAppliedFiltersClone: function () {
      return aAppliedFilterDataClone;
    },

    setAppliedFiltersClone: function (aAppliedFiltersClone) {
      aAppliedFilterDataClone = aAppliedFiltersClone;
    },

    clearAppliedFiltersClone: function () {
      aAppliedFilterDataClone = null;
    },

    getIsFilterDirty: function () {
      return bIsFilterDirty;
    },

    setIsFilterDirty: function (_bIsFilterDirty) {
      bIsFilterDirty = _bIsFilterDirty;
    },

    getTimeRangeData: function () {
      return oTimeRangeData;
    },

    setTimeRangeData: function (_oTimeRangeData) {
      oTimeRangeData = _oTimeRangeData;
    },

    getAllSearchText: function () {
      return sAllSearchText;
    },

    getSortData: function () {
      return oSortData;
    },

    setSortData: function (_oSortData) {
      oSortData = _oSortData;
    },

    getPaginationData: function () {
      return oPaginationData;
    },

    setPaginationData: function (_oPaginationData) {
      oPaginationData = _oPaginationData;
    },

    getFilterInfo: function() {
      return filterInfo;
    },

    setFilterInfo: function(_oFilterInfo) {
      filterInfo = _oFilterInfo;
    },

  }
};

export default TableFilterViewContextProps;
