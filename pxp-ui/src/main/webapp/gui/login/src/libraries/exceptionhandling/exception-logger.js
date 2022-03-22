import { isDevMode } from "../crautils/cra-utils";

var ExceptionLogger = (function () {

  var _bIsDeveloperMode = false;

  (() => {
    var bIsDeveloperMode = isDevMode();
    _bIsDeveloperMode = bIsDeveloperMode === "true" || bIsDeveloperMode === "TRUE";
  })();

  return {
    log: function (oException) {
      if (_bIsDeveloperMode) {
        console.log(oException);
      }
    },

    info: function (oException) {
      if (_bIsDeveloperMode) {
        console.info(oException);
      }
    },

    warn: function (oException) {
      if (_bIsDeveloperMode) {
        console.warn(oException);
      }
    },

    error: function (oException) {
      if (_bIsDeveloperMode) {
        console.error(oException);
      }
    }
  }
})();

export default ExceptionLogger;
