/**
 * Created by DEV on 23-12-2015.
 */

var UserConfigViewProps = (function () {

  let Props = function () {
    return {
      oSelectedUser: {},
      bIsUsernameValid: true,
      bChangePasswordEnabled: false,
      oErrorFields: {},
      aUserGridData: [],
      referencedRoles: {}
    };
  };

  let oProperties = new Props();

  return {
    getReferencedRoles: function () {
      return oProperties.referencedRoles;
    },

    setReferencedRoles: function (_oReferencedRoles) {
      oProperties.referencedRoles = _oReferencedRoles;
    },

    getSelectedUser: function () {
      return oProperties.oSelectedUser;
    },

    setSelectedUser: function (_oUser) {
      oProperties.oSelectedUser = _oUser;
    },

    setUsernameValidity: function (_status) {
      oProperties.bIsUsernameValid = _status;
    },

    isUsernameValid: function () {
      return oProperties.bIsUsernameValid;
    },

    getChangePasswordEnabled: function () {
      return oProperties.bChangePasswordEnabled;
    },

    setChangePasswordEnabled: function (_bChangePasswordEnabled) {
      oProperties.bChangePasswordEnabled = _bChangePasswordEnabled;
    },

    getErrorFields: function () {
      return oProperties.oErrorFields;
    },

    setErrorFields: function (_oErrorFields) {
      oProperties.oErrorFields = _oErrorFields
    },

    emptyErroFields: function () {
      oProperties.oErrorFields = {};
    },

    getUserGridData: function () {
      return oProperties.aUserGridData;
    },

    setUserGridData: function (_aUserGridData) {
      oProperties.aUserGridData = _aUserGridData;
    },

    reset: function () {
      oProperties = new Props();
    },

  };
})();

export default UserConfigViewProps;