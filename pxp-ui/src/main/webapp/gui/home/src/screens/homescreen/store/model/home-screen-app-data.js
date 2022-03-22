import oMenus from '../../tack/menu-list';

var HomeScreenAppData = (function () {
  var aModuleList = [];
  var aExcludedMenuList =[];
  let oExchangeRates = {};
  let aUserList = [];

  return {

    getAllModules: function () {
      return oMenus.moduleList;
    },

    getAllMenus: function () {
      return aModuleList;
    },

    setAllMenus: function (_aModuleList) {
      aModuleList = _aModuleList;
    },

    getExcludedMenus: function () {
      return aExcludedMenuList;
    },

    setExcludedMenus: function (_aExcludedMenuList) {
      aExcludedMenuList = _aExcludedMenuList;
    },

    getExchangeRates: function () {
      return oExchangeRates;
    },

    setExchangeRates: function (_oExchangeRates) {
      oExchangeRates = _oExchangeRates;
    },

    setUserList: function (_aUserList) {
      aUserList = _aUserList;
    },

    getUserList: function () {
      return aUserList;
    },

    toJSON: function () {
      return {
        moduleList: oMenus
      };
    }

  }

})();

export default HomeScreenAppData;