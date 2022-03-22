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

  var _GetTranslations = function () {
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
        if (CS.isNotEmpty(oTranslations)) {
          alertify.message(oTranslations.AUTO_SET_DEFAULT_LANG);
        }
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

    fetchLanguageForUser: function () {
      return CS.getRequest(CommonModuleRequestMapping.GetLanguageForUser, {}, TranslationManager.successFetchLanguageForUserCallBack, TranslationManager.failureFetchLanguageForUserCallBack);
    },

    successFetchLanguageForUserCallBack: function (oResponse) {
      oResponse = oResponse.success;
      let sPreferredUILanguage = oResponse.preferredUILanguage || SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
      let sPreferredDataLanguage = oResponse.preferredDataLanguage || SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);

      SessionProps.setIsLanguageChanged(oResponse.isLanguageChanged);

      ShareableUrl.addLanguageParamsInWindowURL(sPreferredUILanguage, sPreferredDataLanguage);
      SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE, sPreferredUILanguage);
      SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE, sPreferredDataLanguage);
    },

    failureFetchLanguageForUserCallBack: function (oResponse) {
      if (!CS.isEmpty(oResponse.failure)) {
        alertify.error(_GetTranslations()["ERROR_CONTACT_ADMINISTRATOR"]);
        console.error("Failed in failureFetchLanguageForUserCallBack");
      }
    },

    init: async function (oCallBack) {
      let bIsAfterLogin = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.IS_AFTER_LOGIN);
      if (bIsAfterLogin) {
        await TranslationManager.fetchLanguageForUser();
      }
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

export const  {getLanguageFromServer, fetchLanguageForUser, init, getLanguageInfo, getTranslations} = TranslationManager;
