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
    CS.assign(oTranslations, oResponse.success.staticLabelTranslations);
    /*let sLanguageInUse = sessionStorage.languageInUse;

    let oLanguageInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = oLanguageInfo.dataLanguages;
    let sPreviousDataLanguageId = SessionProps.getPreviousDataLanguageId();
    if (!CS.isEmpty(sPreviousDataLanguageId)) {
      SessionProps.setDataLanguageId(sPreviousDataLanguageId);
      SessionProps.setPreviousDataLanguageId("");
    } else if (CS.find(aDataLanguages, {code: sLanguageInUse})) {
      SessionProps.setDataLanguageId(sLanguageInUse);
    } else {
      SessionProps.setDataLanguageId(oLanguageInfo.defaultLanguage);
    }*/

    if(oCallback) {
      /*
      if (oCallback.functionToExecute) {
        oCallback.functionToExecute();
      }
      */
      if (oCallback.triggerChange) { /** accepting triggerChange from callback as we are avoiding triggerChange in this function */
        oCallback.triggerChange();
      }
    }
    //_triggerChange();
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
