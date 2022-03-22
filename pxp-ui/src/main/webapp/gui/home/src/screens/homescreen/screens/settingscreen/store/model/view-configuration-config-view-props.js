let ViewConfigurationViewProps = (function () {

  let Props = function () {
    return {
      viewConfigurationData: {
        isLandingPageExpanded: true,
        isProductInfoPageExpanded: false,
      },
    }
  };

  let oProperties = new Props();

  return {
    getViewConfigurationData: function () {
      return oProperties.viewConfigurationData;
    },

    setViewConfigurationData: function (_oViewConfigurationData) {
      oProperties.viewConfigurationData = _oViewConfigurationData;
    },

    reset: function () {
      oProperties = new Props();
    }
  }


})();

export default ViewConfigurationViewProps;