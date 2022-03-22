import Constants from '../../../../../../commonmodule/tack/constants';

var OrganisationConfigViewProps = (function () {


  var Props = function () {
    return {
      activeOrganisation: {},
      organisationConfigMap: [],
      organisationConfigValueMap:{},
      activeTabId:Constants.ORGANISATION_CONFIG_INFORMATION,
      isPermissionVisible: false,
      allowedTaxonomies: [],
      oAllowedTaxonomyHierarchyList: {},
      screenLockStatus: false,
      referencedTaxonomies: {},
      referencedKlasses: {},
      taxonomyPaginationData: {
        from: 0,
        size: 20,
        sortBy: "label",
        sortOrder: "asc",
        types: [],
        searchColumn: "label",
        searchText: "",
      },
      searchText: "",
      searchConstantData: {
        searchColumn: "label",
        sortOrder: "asc",
        sortBy: "label",
        size: 20
      },
      showLoadMore: true,
      from: 0
    }
  };

  var oProperties = new Props();
  return {

    reset: function () {
      oProperties = new Props();
    },


    getActiveOrganisation: function () {
      return oProperties.activeOrganisation;
    },

    setActiveOrganisation: function (_oActiveOrganisation) {
      oProperties.activeOrganisation = _oActiveOrganisation;
    },

    getOrganisationConfigMap: function () {
      return oProperties.organisationConfigMap;
    },

    setOrganisationConfigMap: function (_aOrganisationConfigMap) {
      oProperties.organisationConfigMap = _aOrganisationConfigMap;
    },

    getOrganisationConfigValueMap: function () {
      return oProperties.organisationConfigValueMap;
    },

    setOrganisationConfigValueMap: function (_oOrganisationConfigValueMap) {
      oProperties.organisationConfigValueMap = _oOrganisationConfigValueMap;
    },

    getOrganisationConfigTabId: function () {
      return oProperties.activeTabId;
    },

    setOrganisationConfigTabId: function (_sTabId) {
      oProperties.activeTabId = _sTabId;
    },

    getAllowedTaxonomies: function () {
      return oProperties.allowedTaxonomies;
    },

    setAllowedTaxonomies: function (_sTabId) {
      oProperties.allowedTaxonomies = _sTabId;
    },

    getAllowedTaxonomyHierarchyList: function () {
      return oProperties.oAllowedTaxonomyHierarchyList;
    },

    setAllowedTaxonomyHierarchyList: function (_oAllowedTaxonomyHierarchyList) {
      oProperties.oAllowedTaxonomyHierarchyList = _oAllowedTaxonomyHierarchyList;
    },

    getReferencedTaxonomies: function () {
      return oProperties.referencedTaxonomies;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.referencedTaxonomies = _oReferencedTaxonomies;
    },

    getReferencedKlasses: function () {
      return oProperties.referencedKlasses;
    },

    setReferencedKlasses: function (_oReferencedKlasses) {
      oProperties.referencedKlasses = _oReferencedKlasses;
    },

    getTaxonomyPaginationData: function () {
      return oProperties.taxonomyPaginationData;
    },

    setTaxonomyPaginationData: function (_oTaxonomyPaginationData) {
      oProperties.taxonomyPaginationData = _oTaxonomyPaginationData;
    },

    getOrganisationConfigScreenLockStatus: function () {
      return oProperties.screenLockStatus;
    },

    setOrganisationConfigScreenLockStatus: function (_bScreenLockStatus) {
      oProperties.screenLockStatus = _bScreenLockStatus;
    },

    getIsPermissionVisible() {
      return oProperties.isPermissionVisible;
    },

    setIsPermissionVisible(_bIsPermissionVisible) {
      oProperties.isPermissionVisible = _bIsPermissionVisible;
    },

    getSearchText: function () {
      return oProperties.searchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.searchText = _sSearchText;
    },

    getSearchConstantData: function () {
      return oProperties.searchConstantData;
    },

    setSearchConstantData: function (_oSearchConstantData) {
      oProperties.searchConstantData = _oSearchConstantData;
    },

    getFrom: function () {
      return oProperties.from
    },

    setFrom: function (_iFrom) {
      oProperties.from = _iFrom
    },

    getShowLoadMore: function () {
      return oProperties.showLoadMore;
    },

    setShowLoadMore: function (_showLoadMore) {
      oProperties.showLoadMore = _showLoadMore;
    },

  }
})();

export default OrganisationConfigViewProps;