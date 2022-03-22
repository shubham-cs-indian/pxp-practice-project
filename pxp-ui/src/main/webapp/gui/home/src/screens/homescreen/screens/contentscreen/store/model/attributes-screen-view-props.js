var AttributesScreenProps = (function () {

  var Props = function () {
    return {
      aSelectedAttributeVariantsList: []
    }
  };

  var oProperties = new Props();

  return {

    getSelectedAttributeVariants: function () {
      return oProperties.aSelectedAttributeVariantsList;
    },

    setSelectedAttributeVariants: function (_aSelectedAttributeVariantsList) {
      return oProperties.aSelectedAttributeVariantsList = _aSelectedAttributeVariantsList;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {
        selectedAttributeVariantsList: oProperties.aSelectedAttributeVariantsList
      };
    }

  };

})();

export default AttributesScreenProps;
