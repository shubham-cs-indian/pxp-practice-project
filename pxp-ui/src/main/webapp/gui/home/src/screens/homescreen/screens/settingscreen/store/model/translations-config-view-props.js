var TranslationsConfigViewProps = (function () {

  let Props = function () {
    return {
      bCategoryScreenLockedStatus: false,
      sSelectedModuleId: "tag",
      sSelectedClassModuleId: "article",
      aDisplayLanguages: [],
      oGridData: {},
      sSelectedProperty: "label",
      sSortByField: "label",
      aAvailableLanguages: [],
    };
  };

  let oProperties = new Props();

  return {

    getTranslationsScreenLockedStatus: function () {
      return oProperties.bCategoryScreenLockedStatus;
    },

    setTranslationsScreenLockedStatus: function (_bLock) {
      oProperties.bCategoryScreenLockedStatus = _bLock;
    },

    getSelectedModule: function () {
      return oProperties.sSelectedModuleId;
    },

    setSelectedModule: function (sModule) {
      oProperties.sSelectedModuleId = sModule;
    },

    getDisplayLanguages: function () {
      return oProperties.aDisplayLanguages;
    },

    setDisplayLanguages: function (_aDisplaylanguages) {
      oProperties.aDisplayLanguages = _aDisplaylanguages;
    },

    setTranslationsGridData: function (_oGridData) {
      oProperties.oGridData = _oGridData;
    },

    getTranslationsGridData: function () {
      return oProperties.oGridData;
    },

    getSelectedProperty: function () {
      return oProperties.sSelectedProperty;
    },

    setSelectedProperty: function (_sSelectedProperty) {
      oProperties.sSelectedProperty = _sSelectedProperty;
    },

    getSortByField: function () {
      return oProperties.sSortByField;
    },

    setSortByField: function (_sSortByField) {
      oProperties.sSortByField = _sSortByField;
    },

    setSelectedChildModule: function (_sModuleId) {
      oProperties.sSelectedClassModuleId = _sModuleId
    },

    getSelectedChildModule: function () {
      return oProperties.sSelectedClassModuleId;
    },

    getAvailableLanguages: function () {
      return oProperties.aAvailableLanguages;
    },

    setAvailableLanguages: function (_aAvailableLanguages) {
      oProperties.aAvailableLanguages = _aAvailableLanguages;
    },

    reset: function () {
      oProperties.sSelectedProperty = "label";
      oProperties.sSortByField = "label";
    }
  }
})();

export default TranslationsConfigViewProps;