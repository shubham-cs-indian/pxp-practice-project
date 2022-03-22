/**
 * Created by CS107 on 13-07-2017.
 */

var CalculatedAttributeConfigViewProps = (function () {

  var Props = function () {
    return {
      from: 0,
      size: 20,
      sortBy: "label",
      sortOrder: "asc",
      searchColumn: "label",
      searchText: "",
      calculatedAttributeType: "",
      calculatedAttributeUnit: "",
      shouldAllowSelf: false,
      attributeId: "",
      allowedAttributes: [],
      totalCount: 0,
      isFirstCall: true
    };
  };

  var Properties = new Props();

  return {

    getFrom: function () {
      return Properties.from;
    },

    setFrom: function (_from) {
      Properties.from = _from;
    },

    getSize: function () {
      return Properties.size;
    },

    getSortBy: function () {
      return Properties.sortBy;
    },

    getSortOrder: function () {
      return Properties.sortOrder;
    },

    getSearchColumn: function () {
      return Properties.searchColumn;
    },

    getSearchText: function () {
      return Properties.searchText;
    },

    setSearchText: function (_searchText) {
      Properties.searchText = _searchText;
    },

    getCalculatedAttributeType: function () {
      return Properties.calculatedAttributeType;
    },

    setCalculatedAttributeType: function (_calculatedAttributeType) {
      Properties.calculatedAttributeType = _calculatedAttributeType;
    },

    getCalculatedAttributeUnit: function () {
      return Properties.calculatedAttributeUnit;
    },

    setCalculatedAttributeUnit: function (_calculatedAttributeUnit) {
      Properties.calculatedAttributeUnit = _calculatedAttributeUnit;
    },

    getShouldAllowSelf: function () {
      return Properties.shouldAllowSelf;
    },

    setShouldAllowSelf: function (_shouldAllowSelf) {
      Properties.shouldAllowSelf = _shouldAllowSelf;
    },

    getAttributeId: function () {
      return Properties.attributeId;
    },

    setAttributeId: function (_attributeId) {
      Properties.attributeId = _attributeId;
    },

    getAllowedAttributes: function () {
      return Properties.allowedAttributes;
    },

    setAllowedAttributes: function (_allowedAttributes) {
      Properties.allowedAttributes = _allowedAttributes;
    },

    getTotalCount: function () {
      return Properties.totalCount;
    },

    setTotalCount: function (_totalCount) {
      Properties.totalCount = _totalCount;
    },

    getIsFirstCall: function () {
      return Properties.isFirstCall;
    },

    setIsFirstCall: function (_isFirstCall) {
      Properties.isFirstCall = _isFirstCall;
    },

    reset: function () {
      Properties = new Props();
    }
  }

})();

export default CalculatedAttributeConfigViewProps;