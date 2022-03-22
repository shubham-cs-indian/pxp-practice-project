/**
 * Created by DEV on 17-06-2015.
 */
import axios from 'axios';
import qs from 'qs';
import CS from '../cs';
import { omit } from '../cs/cs-lodash';
import MomentUtils from "../../commonmodule/util/moment-utils";
import { Logger } from "../logger";

var iCallCounter = 0;
var bDisableLoader = false;

let oStartTime = {}, oEndTime = {};

var AjaxPromiseOnly = {
  ajax: function (type, url, data, contentType, disableLoader) {
    bDisableLoader = disableLoader;

    if (!disableLoader) {
      iCallCounter++;
    }

    let bProcessData = undefined;

    if(!contentType) {
      contentType = "application/json; charset=utf-8";
    } else if (contentType === 'application/x-www-form-urlencoded') {
      data = qs.stringify(data);
    }

    if(contentType === false) {
      bProcessData = false;
    }

    let opts = {
      url: url,
      method: type,
      responseType: 'json',
      data: data,
      headers: {
        'userId': sessionStorage.userId,
        'Content-Type': contentType,
        processData: bProcessData
      }
    };

    // Add a request interceptor
    axios.interceptors.request.use((config) => {
      let loaderContainer = document.getElementById('loaderContainer');

      if (loaderContainer && iCallCounter) {
        loaderContainer.classList.remove('loaderInVisible');
      }

      return config;
    }, (error) => {
      return Promise.reject(error);
    });
    oStartTime[url] = new Date().toISOString();
    return axios(opts);
  },

  log: function (url, method, success) {
    oEndTime[url] = new Date().toISOString();
    global.startTime = oEndTime[url];
    //console.log("response success", oEndTime);
    Logger.log(`${MomentUtils.getTimeDifference(oStartTime[url], oEndTime[url])}s was taken for the response.`);
    Logger.log("Metadata", {
      startTime: oStartTime[url],
      endTime: oEndTime[url],
      url,
      method,
      success,
      timeTaken: `${MomentUtils.getTimeDifference(oStartTime[url], oEndTime[url])}s`
    });
    omit(oStartTime, [url]);
    omit(oEndTime, [url]);
  },

  get: function (url, data, contentType, disableLoader, fSuccess, fFailure) {
    return this.ajax('GET', url, data, contentType, disableLoader)
      .then(response => {
        if (url.split("/")[1] == "taskinstances") {
          global.startTime = "";
          omit(oStartTime, [url]);
        } else {
          this.log(url, "GET", true);
        }
        
        
        return successCallback(fSuccess, disableLoader, response);
      })
      .catch(error => {
        this.log(url, "GET", false);
        return failureCallback(fFailure, disableLoader, error);
      });
  },
  post: function (url, data, contentType, disableLoader, fSuccess, fFailure) {
    return this.ajax('POST', url, data, contentType, disableLoader)
      .then(response => {
        this.log(url, "POST", true);
        return successCallback(fSuccess, disableLoader, response);
      })
      .catch(error => {
        this.log(url, "POST", false);
        return failureCallback(fFailure, disableLoader, error);
      });
  },
  put: function (url, data, contentType, disableLoader, fSuccess, fFailure) {
    return this.ajax('PUT', url, data, contentType, disableLoader)
      .then(response => {
        this.log(url, "PUT", true);
        return successCallback(fSuccess, disableLoader, response);
      })
      .catch(error => {
        this.log(url, "PUT", false);
        return failureCallback(fFailure, disableLoader, error);
      });
  },
  patch: function (url, data, contentType, disableLoader, fSuccess, fFailure) {
    return this.ajax('PATCH', url, data, contentType, disableLoader)
      .then(response => {
        this.log(url, "PATCH", true);
        return successCallback(fSuccess, disableLoader, response);
      })
      .catch(error => {
        this.log(url, "PATCH", false);
        return failureCallback(fFailure, disableLoader, error);
      });
  },
  delete: function (url, data, contentType, disableLoader, fSuccess, fFailure) {
    return this.ajax('DELETE', url, data, contentType, disableLoader)
      .then(response => {
        this.log(url, "DELETE", true);
        return successCallback(fSuccess, disableLoader, response);
      })
      .catch(error => {
        this.log(url, "DELETE", false);
        return failureCallback(fFailure, disableLoader, error);
      });
  },
};

var successCallback = function (oCallback, disableLoader, oResponse) {
  if (!disableLoader && iCallCounter > 0) {
    iCallCounter--;
  }

  let handleLoader =  function () {
    var loaderContainer = document.getElementById('loaderContainer');
    if(loaderContainer && iCallCounter == 0) {
      loaderContainer.classList.add('loaderInVisible');
    }
  }

  setTimeout(handleLoader, 0);

  if(oCallback) {
    // oResponse = getDecodedData(oResponse);
    return oCallback(oResponse.data);
  }

};

var failureCallback = function (oCallback, disableLoader, oError) {
  if (!disableLoader && iCallCounter > 0) {
    iCallCounter--;
  }
  if (oError.response && (oError.response.status === 401 || oError.response.status === 405)) {
    if(CS.isNotEmpty(oError.response.headers.roleexception)) {
      let sUrl = window.location.pathname;
      if(!CS.includes(sUrl, "?")){
          sUrl += "?";
      } else {
          sUrl += "&";
      }
      window.location = sUrl + oError.response.headers.roleexception + "=" + true;
    }
    else{
      window.location = window.location.pathname;
    }

  }

  let handleLoader =  function () {
    var loaderContainer = document.getElementById('loaderContainer');
    if(loaderContainer) {
      loaderContainer.classList.add('loaderInVisible');
    }
  }
  setTimeout(handleLoader, 0);

  if (oCallback) {
    if (oError.response && oError.response.data) {
      return oCallback(oError.response.data);
    } else if (oError) {
      console.error(oError);
    } else {
      var responseObject = {
        errorCode: "999999",
        response: "Server Not Responded",
        status: "FAILURE"
      };
      return oCallback(responseObject);
    }
  }
};

var getDecodedData = function (oData) {
  if (oData && !CS.isEmpty(oData)) {
    var sData = JSON.stringify(oData);
    var txt = document.createElement("textarea");
    txt.innerHTML = sData;
    sData = txt.value;
    oData = JSON.parse(sData);
  }

  return oData;
};

var getEncodedData = function (oData) {
  if (oData && !CS.isEmpty(oData)) {
    var sData = JSON.stringify(oData);
    sData = sData.replace(/[\u00A0-\u9999<>\&]/gim, function(i) {
      return '&#'+i.charCodeAt(0)+';';
    });
    oData = JSON.parse(sData);

  }

  return oData;
};

export default AjaxPromiseOnly;
