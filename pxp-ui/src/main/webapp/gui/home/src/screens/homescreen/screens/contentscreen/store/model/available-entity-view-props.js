/**
 * Created by CS80 on 6/15/2016.
 */

var ArticleViewProps = (function () {

  var Props = function () {
    return {
      iPaginatedIndex: 0,
      iDefaultPaginationLimit: 20,
      sDefaultSortingField: "createdOn",
      sDefaultSortingOrder: "desc",
      aSelectedEntities: [],
      sSearchText: '',
      advanceSearch: false,
      availableEntityViewContext: "",
      rightPanelSelectedEntities: [],
    }
  };

  var oProperties = new Props();

  return {

    getPaginatedIndex: function () {
      return oProperties.iPaginatedIndex;
    },

    setPaginatedIndex: function (iIndex) {
      oProperties.iPaginatedIndex = iIndex;
    },

    getDefaultPaginationLimit: function () {
      return oProperties.iDefaultPaginationLimit;
    },

    getSelectedEntities: function () {
      return oProperties.aSelectedEntities;
    },

    setSelectedEntities: function (_aSelectedEntities) {
      oProperties.aSelectedEntities = _aSelectedEntities;
    },

    getSelectedRightPanelEntities: function () {
      return oProperties.rightPanelSelectedEntities;
    },

    setSelectedRightPanelEntities: function (_aRightPanelSelectedEntities) {
      oProperties.rightPanelSelectedEntities = _aRightPanelSelectedEntities;
    },

    getSearchText: function () {
      return oProperties.sSearchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getAvailableEntityViewContext: function () {
      return oProperties.availableEntityViewContext;
    },

    setAvailableEntityViewContext: function (_sAvailableEntityViewContext) {
      oProperties.availableEntityViewContext = _sAvailableEntityViewContext;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {

      }
    }
  }
})();

export default ArticleViewProps;
