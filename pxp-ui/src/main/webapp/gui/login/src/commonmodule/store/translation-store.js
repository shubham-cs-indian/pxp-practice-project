import MicroEvent from '../../libraries/microevent/MicroEvent.js';
import CS from '../../libraries/cs';

var TranslationStore = (function () {

  var oTranslations = {};

  var _triggerChange = function () {
    TranslationStore.trigger('translation-changed');
  };

  var _fetchTranslations = function (aKeys, sLanguage, oCallback) {

    var oReqData = {
      "language": sLanguage,
      "screens": aKeys
    };
    var sUrl = 'runtime/statictranslations/get';

    let fCallback = CS.noop;
    if(oCallback && oCallback.functionToExecute) {
      fCallback = oCallback.functionToExecute;
    }
    return CS.postRequest(sUrl, {}, oReqData, successFetchTranslationsCallback.bind(this, sLanguage, oCallback), failureFetchTranslationsCallback).then(fCallback)
        .catch(failureFetchTranslationsCallback);

  };

  var successFetchTranslationsCallback = function (sLanguage, oCallback, oResponse) {
    oTranslations = { ...oTranslations, ...oResponse.success.staticLabelTranslations };

    if(oCallback) {
      if (oCallback.triggerChange) { /** accepting triggerChange from callback as we are avoiding triggerChange in this function */
        oCallback.triggerChange();
      }
    }
    return true;
  };

  let failureFetchTranslationsCallback = function (oResponse) {
    return false;
  };

  return {
    fetchTranslations: function (aKeys, sLanguage, oCallback) {
      return _fetchTranslations(aKeys, sLanguage, oCallback);
    },

    getTranslations: function () {
      return oTranslations;
    }
  }

})();

MicroEvent.mixin(TranslationStore);

export default TranslationStore;
