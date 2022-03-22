// const CommonUtils = require('../../commonmodule/util/common-utils');
import { isDevMode } from "../crautils/cra-utils";

var ExceptionLogger = (function () {

  let _bIsDeveloperMode = isDevMode();

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