// TODO: #Refact20

/*
import CS from '../cs';
import LocalStorageManager from '../localstoragemanager/local-storage-manager';
import { isDevMode } from "../crautils/cra-utils";

var Logger = function (className, worker) {

  var getTimeStamp = function () {
    var currentDate = new Date();
    return currentDate.getFullYear() + "-"
        + padLeft(2, '0', (currentDate.getMonth() + 1).toString()) + "-"
        + padLeft(2, '0',(currentDate.getDate()).toString()) + "T"
        + padLeft(2, '0',(currentDate.getHours()).toString()) + ":"
        + padLeft(2, '0',(currentDate.getMinutes()).toString()) + ":"
        + padLeft(2, '0',(currentDate.getSeconds()).toString()) + "."
        + padLeft(3, '0',(currentDate.getMilliseconds()).toString());
  };

  var getLogRecord = function (description, data, logType, userScenario, ajaxRequest) {
    var sRequestId = LocalStorageManager.getPropertyFromLocalStorage('requestId');
    var sUserScenario = LocalStorageManager.getPropertyFromLocalStorage('userScenario');

    return {
      captureTimeStamp: getTimeStamp(),                      //Time of logging
      description: description,                              //Description of log
      appData: (CS.isEmpty(data)) ? {} : {value: data},       //Actual data for log
      userScenario: sUserScenario + (CS.isEmpty(userScenario) ? '' : '/' + userScenario),             //useCase of log
      className: className,                                 //Class OR File name from where logging is done
      logType: logType,                                     //Log type [log, error, debug, warn, info]
      sessionId: sessionStorage.sessionId,                  //sessionId
      requestId: sRequestId,                    //requestId
      mode: 'CLIENT',
      ajaxRequest: Boolean(ajaxRequest)                     //Ajax request or normal
    }
  };

  var padLeft = function (length, newStr, str) {
    newStr = newStr || ' ';
    return str.length >= length
        ? str
        : (new Array(Math.ceil((length - str.length) / newStr.length) + 1).join(newStr)).substr(0, (length - str.length)) + str;
  };

  var devMode = {
    info: function (description, data) {
      // console.log(getLogRecord(description, data, 'INFO'));
    },

    infoAjaxRequest: function (description, data, userScenario) {
      // console.log(getLogRecord(description, data, 'INFO', userScenario, true));
    },

    error: function (description, data) {
      // console.log(getLogRecord(description, data, 'ERROR'));
    },

    errorAjaxRequest: function (description, data, userScenario) {
      // console.log(getLogRecord(description, data, 'ERROR', userScenario, true));
    },

    debug: function (description, data) {
      // if(Config.DEBUG_MODE) {
      //   console.log(getLogRecord(description, data, 'DEBUG'));
      // }
    },

    debugAjaxRequest: function (description, data, userScenario) {
      // if(Config.DEBUG_MODE) {
      //   console.log(getLogRecord(description, data, 'DEBUG', userScenario, true));
      //
      // }
    },

    warn: function (description, data) {
      // console.log(getLogRecord(description, data, 'WARN'));
    },

    warnAjaxRequest: function (description, data, userScenario) {
      // console.log(getLogRecord(description, data, 'WARN', userScenario, true));
    }
  };

  var productionMode = {
    info: function (description, data) {
      // try {
      //   worker.postMessage(getLogRecord(description, data, 'INFO'));
      // } catch (e) {
      //   console.error("Logging Failed");
      //   console.log(e);
      // }
    },

    infoAjaxRequest: function (description, data, userScenario) {
      // try {
      //   worker.postMessage(getLogRecord(description, data, 'INFO', userScenario, true));
      // } catch (e) {
      //   console.error("Logging Failed");
      //   console.log(e);
      // }
    },

    error: function (description, data) {
      // try {
      //   worker.postMessage(getLogRecord(description, data, 'ERROR'));
      // } catch (e) {
      //   console.error("Logging Failed");
      //   console.log(e);
      // }
    },

    errorAjaxRequest: function (description, data, userScenario) {
      // try {
      //   worker.postMessage(getLogRecord(description, data, 'ERROR', userScenario, true));
      // } catch (e) {
      //   console.error("Logging Failed");
      //   console.log(e);
      // }
    },

    debug: function (useCase, description, data) {
      // try {
      //   if(Config.DEBUG_MODE) {
      //     worker.postMessage(getLogRecord(useCase, description, data, 'DEBUG'));
      //   }
      // } catch (e) {
      //   console.error("Logging Failed");
      //   console.log(e);
      // }
    },

    debugAjaxRequest: function (description, data, userScenario) {
      // try {
      //   if(Config.DEBUG_MODE) {
      //     worker.postMessage(getLogRecord(description, data, 'DEBUG', userScenario, true));
      //   }
      // } catch (e) {
      //   console.error("Logging Failed");
      //   console.log(e);
      // }
    },

    warn: function (useCase, description, data) {
      // try {
      //   worker.postMessage(getLogRecord(useCase, description, data, 'WARN'));
      // } catch (e) {
      //   console.error("Logging Failed");
      //   console.log(e);
      // }
    },

    warnAjaxRequest: function (description, data, userScenario) {
      // try {
      //   worker.postMessage(getLogRecord(description, data, 'WARN', userScenario, true));
      // } catch (e) {
      //   console.error("Logging Failed");
      //   console.log(e);
      // }
    }
  };

  return isDevMode() ? devMode : productionMode;
};

export default Logger;
*/
