import AjaxPromiseOnly from './ajax-promise-only';
import LocalStorageManager from './../localstoragemanager/local-storage-manager';
import SessionProps from '../../commonmodule/props/session-props';
import SessionStorageManager from '../../libraries/sessionstoragemanager/session-storage-manager';
import SessionStorageConstants from '../../commonmodule/tack/session-storage-constants';
import { isDevMode } from "../crautils/cra-utils";

var AjaxPromise = {

  getUrl: function (url, oExtraData) {
    var iUrl = url.indexOf('?');
    var sUrl = '';
    var sRequestId = LocalStorageManager.getPropertyFromLocalStorage('requestId');
    let sPhysicalCatalogId = oExtraData.physicalCatalogId || SessionProps.getSessionPhysicalCatalogId();
    /*if(sPhysicalCatalogId === PhysicalCatalogDictionary.PIM_ARCHIVAL){
      sPhysicalCatalogId = PhysicalCatalogDictionary.PIM;
    }*/
    let sPortalId = oExtraData.portalId || SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL);
    let sLanguageInUse = oExtraData.languageInUse || SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let sOrganizationId = oExtraData.organizationId || SessionProps.getSessionOrganizationId();
    let sEndpointId = oExtraData.endpointId || SessionProps.getSessionEndpointId();
    let sEndpointType = oExtraData.endpointType || SessionProps.getSessionEndpointType();

    if (isDevMode()) {
      url = "pxp-ui/" + url;
    }

    //sessionStorage.language

    // sessionId is Removed from url for Security Reason
    //let sSessionId = SessionStorageManager.getPropertyFromSessionStorage("sessionId");
    if(iUrl != -1) {
      sUrl = url +
          /*'&sessionId=' + sSessionId +*/
          '&requestId='+ sRequestId +
          '&lang='+ sLanguageInUse +
          '&organizationId=' + sOrganizationId +
          '&physicalCatalogId=' + sPhysicalCatalogId +
          '&portalId=' + sPortalId +
          '&endpointId=' + sEndpointId +
          '&endpointType=' + sEndpointType;
    } else {
      sUrl = url +
         /* '?sessionId=' + sSessionId +*/
          '?requestId='+ sRequestId +
          '&lang='+ sLanguageInUse +
          '&organizationId=' + sOrganizationId +
          '&physicalCatalogId=' + sPhysicalCatalogId +
          '&portalId=' + sPortalId +
          '&endpointId=' + sEndpointId +
          '&endpointType=' + sEndpointType;
    }

    let sDataLanguageId = oExtraData && oExtraData.dataLanguage ? oExtraData.dataLanguage : SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    //let sURLDataLanguageId = CS.isEmpty(sDataLanguageId) ? SessionProps.getDefaultDataLanguageId() : sDataLanguageId;
    sUrl = sUrl + '&dataLanguage=' + sDataLanguageId;

    return sUrl;
  },

  get: function (url, data, contentType, oExtraData, disableLoader, fSuccess, fFailure) {
    oExtraData = oExtraData || {};
    url = this.getUrl(url, oExtraData);
    return AjaxPromiseOnly.get(url, data, contentType, disableLoader, fSuccess, fFailure);
  },

  post: function (url, data, contentType, disableLoader, oExtraData, fSuccess, fFailure) {
    oExtraData = oExtraData || {};
    url = this.getUrl(url, oExtraData);
    return AjaxPromiseOnly.post(url, data, contentType, disableLoader, fSuccess, fFailure);
  },

  put: function (url, data, contentType, oExtraData, fSuccess, fFailure) {
    oExtraData = oExtraData || {};
    url = this.getUrl(url, oExtraData);
    return AjaxPromiseOnly.put(url, data, contentType, null, fSuccess, fFailure);
  },

  patch: function (url, data, contentType, oExtraData, fSuccess, fFailure) {
    oExtraData = oExtraData || {};
    url = this.getUrl(url, oExtraData);
    return AjaxPromiseOnly.patch(url, data, contentType, null, fSuccess, fFailure);
  },

  delete: function (url, data, contentType, oExtraData, fSuccess, fFailure) {
    oExtraData = oExtraData || {};
    url = this.getUrl(url, oExtraData);
    return AjaxPromiseOnly.delete(url, data, contentType, null, fSuccess, fFailure);
  },

  getCORS: function (url, data, contentType, disableLoader, oExtraData) {
    oExtraData = oExtraData || {};
    url = this.getUrl(url, oExtraData);
    return AjaxPromiseOnly.getCORS(url, data, contentType, disableLoader);
  },

  postCORS: function (url, data, contentType, oExtraData) {
    oExtraData = oExtraData || {};
    url = this.getUrl(url, oExtraData);
    return AjaxPromiseOnly.postCORS(url, data, contentType);
  },

  deleteCORS: function (url, data, oExtraData) {
    oExtraData = oExtraData || {};
    url = this.getUrl(url, oExtraData);
    return AjaxPromiseOnly.deleteCORS(url, data);
  }

};

export default AjaxPromise;
