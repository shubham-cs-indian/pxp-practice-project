let AllCategoriesTaxonomiesSelectorProps = function () {

  let fProps = function () {
    return {
      sCategoriesSelectedID: "",
      bIsAllCategoriesTaxonomiesPopOverVisible: false,
      bIsAllCategoriesTaxonomySelectorPopDirty: false,
      oSummaryTreeData: {},
      oSearchTreeData: {},
      clickedTaxonomyId: "",
      searchText: "",
      referencedTaxonomies: {}
    }
  };

  var oProperties = new fProps();

  return {

    getIsAllCategoriesTaxonomiesPopOverVisible : function () {
      return oProperties.bIsAllCategoriesTaxonomiesPopOverVisible;
    },

    setIsAllCategoriesTaxonomiesPopOverVisible: function (bVal) {
      oProperties.bIsAllCategoriesTaxonomiesPopOverVisible = bVal;
    },

    getIsAllCategoriesTaxonomySelectorPopDirty : function () {
      return oProperties.bIsAllCategoriesTaxonomySelectorPopDirty;
    },

    setIsAllCategoriesTaxonomySelectorPopDirty: function (bVal) {
      oProperties.bIsAllCategoriesTaxonomySelectorPopDirty = bVal;
    },

    getSelectedCategoryID: function () {
      return oProperties.sCategoriesSelectedID;
    },

    setSelectedCategoryID: function (_sId) {
      oProperties.sCategoriesSelectedID = _sId;
    },

    getTreeSearchText: function () {
      return oProperties.searchText;
    },

    setTreeSearchText: function (_sSearchText) {
      oProperties.searchText = _sSearchText;
    },

    getSummaryTreeData: function () {
      return oProperties.oSummaryTreeData;
    },

    setSummaryTreeData: function (_oSummaryTreeData) {
      oProperties.oSummaryTreeData = _oSummaryTreeData;
    },

    getSearchViewTreeData: function () {
      return oProperties.oSearchTreeData;
    },

    setSearchViewTreeData: function (_oSearchTreeData) {
      oProperties.oSearchTreeData = _oSearchTreeData;
    },

    getClickedTaxonomyId: function () {
      return oProperties.clickedTaxonomyId;
    },

    setClickedTaxonomyId: function (sClickedTaxonomyId) {
      oProperties.clickedTaxonomyId = sClickedTaxonomyId;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.referencedTaxonomies = _oReferencedTaxonomies;
    },

    getReferencedTaxonomies: function () {
      return oProperties.referencedTaxonomies;
    },

    //Add functions above reset function
    reset: function () {
      oProperties = new fProps();
    }
  };

};

export default AllCategoriesTaxonomiesSelectorProps;
