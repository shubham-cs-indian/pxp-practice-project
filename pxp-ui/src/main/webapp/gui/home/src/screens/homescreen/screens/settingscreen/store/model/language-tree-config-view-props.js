let LanguageTreeProps = (function () {

  let Props = function () {
    return {
      ActiveLanguage: {},
      languageTree: {},
      oLanguageValueList: {
        "-1": {
          id: -1,
          isChecked: 0,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'Language Trees',
          canCreate: true,
        }
      }
    }
  };

  let oProperties = new Props();

  return {
    setActiveLanguage: function (_ActiveLanguage) {
      oProperties.ActiveLanguage = _ActiveLanguage;
    },

    getActiveLanguage: function () {
      return oProperties.ActiveLanguage;
    },

    setLanguageTree: function (_languageTree) {
      oProperties.languageTree = _languageTree;
    },

    getLanguageTree: function () {
      return oProperties.languageTree;
    },

    getLanguageValueListByTypeGeneric: function () {
      oProperties.oLanguageValueList[-1].label = "Language Tree";
      return oProperties.oLanguageValueList;
    },

    reset: function () {
      oProperties = new Props();
    },


    toJSON: function () {
      return {

      };
    }
  }
})();

export default LanguageTreeProps;