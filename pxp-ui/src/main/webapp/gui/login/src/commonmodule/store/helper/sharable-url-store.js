import CS from '../../../libraries/cs';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import SharableURLProps from './../model/sharable-url-props';
import SharableUrlConstants from '../../tack/sharable-url-constants';

var SharableURLStore = (function () {

  var _removeParametersFromQueryString = function (sSearchQuery, aParamsToRemove) {
    let aSearchParams = sSearchQuery.split("&");
    let aRemoveParamsIndices = [];
    CS.forEach(aSearchParams, function (sParameter, iIndex) {
      let aParam = sParameter.split("=");
      if (CS.includes(aParamsToRemove, aParam[0])) {
        aRemoveParamsIndices.push(iIndex);
      }
    });
    CS.reverse(aRemoveParamsIndices);
    CS.forEach(aRemoveParamsIndices, function (iIndex) {
      aSearchParams.splice(iIndex, 1);
    });
    return aSearchParams.join("&");
  };

  let _getQueryVariable = function (sParameterKey) {
    let sURL = window.location.search.substring(1);
    let aQueryParameters = sURL.split("&");
    for (let i = 0; i < aQueryParameters.length; i++) {
      let aParameterKeyValue = aQueryParameters[i].split("=");
      if (aParameterKeyValue[0] === sParameterKey) {
        return aParameterKeyValue[1];
      }
    }
  };

  let _addLanguageParamsInWindowURL = function (sUILanguageCode, sDataLanguageCode) {
    let sURL = window.location.search;
    let aRemoveParameters = [];
    let sUILanguageFromURL = _getQueryVariable(SharableUrlConstants.UI_LANG);
    let sDataLanguageFromURL = _getQueryVariable(SharableUrlConstants.DATA_LANG);

    /** If language is not changed, no need to update the URL **/
    if(sUILanguageFromURL === sUILanguageCode && sDataLanguageFromURL === sDataLanguageCode){
      return;
    }

    /** Remove existing parameters **/
    sUILanguageCode && aRemoveParameters.push(SharableUrlConstants.UI_LANG);
    sDataLanguageCode && aRemoveParameters.push(SharableUrlConstants.DATA_LANG);
    sURL = _removeParametersFromQueryString(sURL, aRemoveParameters);

    if(CS.isEmpty(sURL) || !CS.includes(sURL, "?")){
      sURL = "?login=xqa3vq16193n1acxh6wteh0qn" + sURL;
    }

    if(sUILanguageCode){
      sURL = sURL + "&" + SharableUrlConstants.UI_LANG + "=" + sUILanguageCode;
    }
    if(sDataLanguageCode) {
      sURL = sURL +  "&" + SharableUrlConstants.DATA_LANG + "=" + sDataLanguageCode
    }
    let oHistoryState = CS.getHistoryState();

    let oStateObj = oHistoryState ? oHistoryState : null;
    CS.replaceNavigation(oStateObj, "", sURL);
  };

  let _removeParamAndUpdateUrl = function(sParameterKey){
      let sURL = window.location.search;
      sURL = _removeParametersFromQueryString(sURL, sParameterKey);
      let oHistoryState = CS.getHistoryState();

      let oStateObj = oHistoryState ? oHistoryState : null;
      CS.replaceNavigation(oStateObj, "", sURL);
  };

  return {

    setIsEntityNavigation: function (bIsEntityNavigation) {
      SharableURLProps.setIsEntityNavigation(bIsEntityNavigation);
    },

    getQueryVariable: function (sParameterKey) {
      return _getQueryVariable(sParameterKey);
    },

    addLanguageParamsInWindowURL: function (sUILanguageCode, sDataLanguageCode) {
      _addLanguageParamsInWindowURL(sUILanguageCode, sDataLanguageCode);
    },
    removeParamAndUpdateUrl : function (sParamKey){
        _removeParamAndUpdateUrl(sParamKey)
    },
  }
})();

MicroEvent.mixin(SharableURLStore);

export default SharableURLStore;
