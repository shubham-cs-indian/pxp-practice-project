
let VariantConfigurationViewProps = (function () {
  let Props = function () {
    return {
      variantConfigData: {
        isDirty: false,
        isVariantConfigurationEnable: true,
      }
    }
  };

  let oProperties = new Props();

  return {
    getVariantViewData: function () {
      return oProperties.variantConfigData;
    },

    setVariantViewData: function (bVariantViewData) {
      oProperties.variantConfigData = bVariantViewData;
    },

  }
})();

export default VariantConfigurationViewProps;