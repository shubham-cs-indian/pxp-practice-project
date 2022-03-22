
var UOMProps = (function () {

  var Props = function () {
    return {
      sOpenedDialogTableContextId: "",
      oOpenedDialogAttributeData: {
        attributeId: "",
        contextId: ""
      },
      popOverContextData:{},
      sFullScreenViewContextId: "",
    }
  };

  var oProperties = new Props();

  return {

    getOpenedDialogTableContextId: function () {
      return oProperties.sOpenedDialogTableContextId;
    },

    setOpenedDialogTableContextId: function (sContextId) {
      oProperties.sOpenedDialogTableContextId = sContextId;
    },

    getOpenedDialogAttributeData: function () {
      return oProperties.oOpenedDialogAttributeData;
    },

    setOpenedDialogAttributeData: function (_oOpenedDialogAttributeData) {
      oProperties.oOpenedDialogAttributeData = _oOpenedDialogAttributeData;
    },

    setActivePopOverContextData: function (_oPopOverContextData) {
      oProperties.popOverContextData = _oPopOverContextData;
    },

    getActivePopOverContextData: function () {
      return oProperties.popOverContextData;
    },

    getFullScreenTableContextId: function () {
      return oProperties.sFullScreenViewContextId;
    },

    setFullScreenTableContextId: function (sContextId) {
      oProperties.sFullScreenViewContextId = sContextId;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {
      };
    }

  };

})();

export default UOMProps;
