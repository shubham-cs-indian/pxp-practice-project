var DAMConfigurationViewProps = (function () {
  let Props = function () {
    return {
      oConfigurationData: {},
    }
  };
  let oProperties = new Props();
  return {
    getDamConfigurationData: function () {
      return oProperties.oConfigurationData;
    },
    setDamConfigurationData: function (oConfigurationData) {
      oProperties.oConfigurationData = oConfigurationData;
    },
    reset: function () {
      oProperties = new Props();
    }
  }
})();
export default DAMConfigurationViewProps;