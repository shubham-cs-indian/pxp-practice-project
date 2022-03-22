const NumberProps = (function () {

  const Props = function () {
    return {
      currentNumberFormat: null
    }
  };

  let oProperties = new Props();

  return {

    getCurrentNumberFormat: function () {
      return oProperties.currentNumberFormat;
    },

    setCurrentNumberFormat: function (currentNumberFormat) {
      oProperties.currentNumberFormat = currentNumberFormat;
    },

    reset: function () {
      oProperties = new Props();
    }
  }
})();

export default NumberProps;
