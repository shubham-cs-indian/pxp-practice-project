/**
 * Created by CS62 on 13-12-2016.
 */

let ContextConfigViewProps = (function () {

  let Props = function () {
    return {
      aContextGridData: [],
      oActiveContext: {},
      bIsContextDialogActive: false,
      aContextTagOrder: [],
      sActiveTagUniqueSelectionId: "",
    };
  };

  let oProperties = new Props();

  return {
    getActiveContext: function () {
      return oProperties.oActiveContext;
    },

    setActiveContext: function (_oActiveContext) {
      oProperties.oActiveContext = _oActiveContext;
    },

    getContextTagOrder: function () {
      return oProperties.aContextTagOrder;
    },

    setContextTagOrder: function (_aContextTagOrder) {
      oProperties.aContextTagOrder = _aContextTagOrder;
    },

    getActiveTagUniqueSelectionId: function () {
      return oProperties.sActiveTagUniqueSelectionId;
    },

    setActiveTagUniqueSelectionId: function (_sActiveTagUniqueSelectionId) {
      oProperties.sActiveTagUniqueSelectionId= _sActiveTagUniqueSelectionId;
    },

    getIsContextDialogActive: function () {
      return oProperties.bIsContextDialogActive;
    },

    setIsContextDialogActive: function (_bIsContextDialogActive) {
      oProperties.bIsContextDialogActive = _bIsContextDialogActive;
    },

    getContextGridData: function () {
      return oProperties.aContextGridData;
    },

    setContextGridData: function (_aContextGridData) {
      oProperties.aContextGridData = _aContextGridData;
    },

    reset: function () {
      oProperties = new Props();
    },
  }
})();

export default ContextConfigViewProps;