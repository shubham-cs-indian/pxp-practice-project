/**
 * Created by DEV on 28-12-2015.
 */
import CS from '../../../../../../libraries/cs';

var RoleConfigViewProps = (function () {

  let Props = function () {
    return {
      oRolesValueList: {},
      oRolesSearchedValueList: {},
      oSelectedRole: {},
      bRoleScreenLockStatus: false,
      oMultiValuedListObject: {},
      oReferencedTaxonomies: {},
      oAllowedTaxonomyHierarchyList: {},
      oReferencedKPIs: {},
      aReferencedKlasses: [],
      oRolePaginationData: {
        taxonomies: {},
        Klass: {},
        users: {}
      },

      oRoleEntities: {
        taxonomies: [],
        Klass: [],
        users: [],
        endpoints: [],
      },
      aAddedProfileList: [],
      sSearchText: ""
    };
  };

  let oProperties = new Props();

  return {

    setMultiValuedListObject: function (_oMultiValuedListObject) {
      oProperties.oMultiValuedListObject = _oMultiValuedListObject;
    },

    getMultiValuedListObject: function () {
      return oProperties.oMultiValuedListObject;
    },

    getRoleValuesList: function () {
      return oProperties.oRolesValueList;
    },

    setRoleValuesList: function (_oRolesValueList) {
      oProperties.oRolesValueList = _oRolesValueList;
    },

    getSearchedRoleValuesList: function () {
      return oProperties.oRolesSearchedValueList;
    },

    setSearchedRoleValuesList: function (_oRolesSearchedValueList) {
      oProperties.oRolesSearchedValueList = _oRolesSearchedValueList;
    },

    getRoleScreenLockStatus: function () {
      return oProperties.bRoleScreenLockStatus;
    },
    getSelectedRole: function () {
      return oProperties.oSelectedRole;
    },
    setRoleScreenLockStatus: function (_status) {
      oProperties.bRoleScreenLockStatus = _status;
    },

    setSelectedRole: function (_oRole) {
      oProperties.oSelectedRole = _oRole;
    },

    getAddedProfileIds: function () {
      return oProperties.aAddedProfileList;
    },

    setAddedProfileIds: function (_aAddedProfileList) {
      oProperties.aAddedProfileList = _aAddedProfileList;
    },

    getReferencedTaxonomies: function () {
      return oProperties.oReferencedTaxonomies;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.oReferencedTaxonomies = _oReferencedTaxonomies;
    },

    getAllowedTaxonomyHierarchyList: function () {
      return oProperties.oAllowedTaxonomyHierarchyList;
    },

    setAllowedTaxonomyHierarchyList: function (_oAllowedTaxonomyHierarchyList) {
      oProperties.oAllowedTaxonomyHierarchyList = _oAllowedTaxonomyHierarchyList;
    },

    getReferencedKlasses: function () {
      return oProperties.aReferencedKlasses;
    },

    setReferencedKlasses: function (_aReferencedKlasses) {
      oProperties.aReferencedKlasses = _aReferencedKlasses;
    },

    getReferencedKPIs: function () {
      return oProperties.oReferencedKPIs;
    },

    setReferencedKPIs: function (_oReferencedKPIs) {
      oProperties.oReferencedKPIs = _oReferencedKPIs;
    },

    getAllowedEntitiesById: function (sId) {
      return oProperties.oRoleEntities[sId];
    },

    setAllowedEntitiesById: function (sId, _aAllowedEntities) {
      oProperties.oRoleEntities[sId] = _aAllowedEntities;
    },

    getRolePaginationDataById: function (sId) {
      if (CS.isEmpty(oProperties.oRolePaginationData[sId])) {
        this.resetRolePaginationDataById(sId);
      }
      return oProperties.oRolePaginationData[sId];
    },

    setRolePaginationSearchTextById: function (sId, sSearchText) {
      oProperties.oRolePaginationData[sId].searchText = sSearchText;
    },

    getRoleSearchText: function () {
      return oProperties.sSearchText;
    },

    setRoleSearchText: function (sSearchText) {
      oProperties.sSearchText = sSearchText
    },

    resetRolePaginationDataById: function (sId) {
      oProperties.oRolePaginationData[sId] = {
        searchText: "",
        from: 0,
        size: 20
      }
    },

    reset: function () {
      oProperties = new Props();
    }
  }
})();

export default RoleConfigViewProps;