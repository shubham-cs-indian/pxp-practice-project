import CS from './../../../libraries/cs';
import SessionProps from './../../../commonmodule/props/session-props';
import TranslationStore from '../translation-store';
import CommonModuleRequestMapping from '../../tack/common-module-request-mapping';
import SessionStorageManager from '../../../libraries/sessionstoragemanager/session-storage-manager';
import ShareableUrl from './../../../commonmodule/store/helper/sharable-url-store';
import SessionStorageConstants from '../../tack/session-storage-constants';
import SharableUrlConstants from '../../tack/sharable-url-constants';
import alertify from '../../../commonmodule/store/custom-alertify-store';

var TranslationManager = (function () {

  var _GetTranslations = function (sContext) {
    return TranslationStore.getTranslations();
  };


  return {
    getLanguageFromServer: function (oCallBack) {
      let sSelectedUILanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
      let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
      let oData = {
        isGetDataLanguages: true,
        isGetUILanguages: true,
        dataLanguage: sSelectedDataLanguageCode || ShareableUrl.getQueryVariable(SharableUrlConstants.DATA_LANG) || "",
        uiLanguage: sSelectedUILanguageCode || ShareableUrl.getQueryVariable(SharableUrlConstants.UI_LANG) || ""
      };
      return CS.postRequest(CommonModuleRequestMapping.GetLanguageInfo, {}, oData,TranslationManager.successFetchLanguageInfoCallback.bind(TranslationManager, oCallBack), TranslationManager.failureFetchLanguageInfoCallback);
    },

    successFetchLanguageInfoCallback: function (oCallBack, oResponse) {
      let oTranslations = _GetTranslations();
      let oLanguageData = CS.processGetLanguageInfoResponse(oResponse);
      let sUILanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
      let sDataLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
      ShareableUrl.addLanguageParamsInWindowURL(sUILanguage, sDataLanguage);

      /** If currently selected UI/data language not found in Language Info data then we switch to default language */
      if (oLanguageData.setDefaultLanguageAsUILanguage || oLanguageData.setDefaultLanguageAsDataLanguage) {
        alertify.message(oTranslations.AUTO_SET_DEFAULT_LANG);
        /** To refresh application with newly set UI language **/
        if (oLanguageData.setDefaultLanguageAsUILanguage) {
          oCallBack.functionToExecuteAfterSwitchedToDefaultLanguage();
          return ;
        }
      }

      if(oCallBack && oCallBack.functionToExecute){
        oCallBack.functionToExecute();
      }
    },


    failureFetchLanguageInfoCallback: function (oResponse) {
      if (!CS.isEmpty(oResponse.failure)) {
        console.error("Failed in failureFetchLanguageInfoCallback");
        console.log(oResponse);
      } else {
        console.error("Something went wrong in failureFetchLanguageInfoCallback");
        console.error(oResponse);
      }
    },

    init: function (oCallBack) {
      return TranslationManager.getLanguageFromServer(oCallBack);
    },

    getLanguageInfo: function () {
      return SessionProps.getLanguageInfoData();
    },

    getTranslations: function () {
      return _GetTranslations();
    }
  }
})();

export const  {getLanguageFromServer, init, getLanguageInfo, getTranslations} = TranslationManager;
