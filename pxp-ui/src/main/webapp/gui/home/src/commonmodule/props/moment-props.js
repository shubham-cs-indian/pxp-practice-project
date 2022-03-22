const MomentProps = (function () {

  const Props = function () {
    return {
      currentDateFormat: null
    }
  };

  let oProperties = new Props();

  return {

    getCurrentDateFormat: function () {
      return oProperties.currentDateFormat;
    },

    setCurrentDateFormat: function (currentDateFormat) {
      oProperties.currentDateFormat = currentDateFormat;
    },

    reset: function () {
      oProperties = new Props();
    }
  }
})();

export default MomentProps;
