import CS from '../../../libraries/cs';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import LinkManager from '../../../libraries/linkmanager/link-manager';
import BaseTypesDictionary from '../../tack/mock-data-for-entity-base-types-dictionary';
import SharableURLProps from './../model/sharable-url-props';
import SessionProps from './../../props/session-props';
import SharableUrlConstants from '../../tack/sharable-url-constants';
import SessionStorageConstants from '../../tack/session-storage-constants';
import SessionStorageManager from '../../../libraries/sessionstoragemanager/session-storage-manager';

var SharableURLStore = (function () {

  var _triggerChange = function () {
    SharableURLStore.trigger('sharable-URL-change');
  };

  var _reverseString = function (str) {
    let splitString = str.split("");
    let reverseArray = splitString.reverse();
    return reverseArray.join("");
  };

  var _encodeBaseType = function (sBaseType) {
    let oReverseBaseTypeDictionary = CS.invert(BaseTypesDictionary);
    return _reverseString(oReverseBaseTypeDictionary[sBaseType]);
  };

  var _decodeBaseType = function (sEncodedBaseType = "") {
    let sReverseString = _reverseString(sEncodedBaseType);
    return BaseTypesDictionary[sReverseString];
  };

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

  /*var _addParamsInWindowURL = function (sEntityId, sBaseType) {
    let sAlreadyExistingEntityId = LinkManager.getParameterByName(SharableUrlConstants.C_ID);
    if (sAlreadyExistingEntityId && sAlreadyExistingEntityId === sEntityId) {
      return;
    }

    let sSearchQuery = window.location.search;
    sSearchQuery = _removeParametersFromQueryString(sSearchQuery,
        [SharableUrlConstants.C_ID, SharableUrlConstants.BT, SharableUrlConstants.PH_ID, SharableUrlConstants.PPH_ID, SharableUrlConstants.EP_ID, SharableUrlConstants.EP_TYPE, SharableUrlConstants.ORG_ID]);

    let sPhysicalCatalogId = SessionProps.getSessionPhysicalCatalogId();
    let sPrevPhysicalCatalogId = SessionProps.getSessionPreviousPhysicalCatalogId();
    let sEndpiontId = SessionProps.getSessionEndpointId();
    let sEndpiontType = SessionProps.getSessionEndpointType();
    let sOrganizationId = SessionProps.getSessionOrganizationId();
    let sPortalId = SessionProps.getSessionPortalId();

    if(CS.isEmpty(sSearchQuery) || !CS.includes(sSearchQuery, "?")){
      /!**
       * Info: We can not set empty searchQuery in browser history,
       * for that purpose when we don't have anything in search query,
       * we are appending tid (temporary id) with absolutely random value - codingDoodler
       * *!/
      sSearchQuery = "?login=xqa3vq16193n1acxh6wteh0qn" + sSearchQuery;
    }

    let iBaseTypeCode = _encodeBaseType(sBaseType);
    let sURL = sSearchQuery +
         "&" + SharableUrlConstants.C_ID + "=" + sEntityId +
         "&" + SharableUrlConstants.BT + "=" + iBaseTypeCode +
         "&" + SharableUrlConstants.PH_ID + "=" + sPhysicalCatalogId +
         "&" + SharableUrlConstants.PPH_ID + "=" + sPrevPhysicalCatalogId +
         "&" + SharableUrlConstants.PH_ID_IU + "=" + sPhysicalCatalogIdInUse +
         "&" + SharableUrlConstants.EP_ID + "=" + sEndpiontId +
         "&" + SharableUrlConstants.EP_TYPE + "=" + sEndpiontType +
         "&" + SharableUrlConstants.ORG_ID + "=" + sOrganizationId +
         "&porid=" + sPortalId;

    let stateObj = {id: sEntityId};
    history.pushState(stateObj, "", sURL);
  }; */

  let _getWindowURL = function (sEntityId, sEntityBaseType) {
    let sSearchQuery = window.location.search;
    sSearchQuery = _removeParametersFromQueryString(sSearchQuery,
        ["cid", "bt", "phid", "pphid", "epid", "eptype", "orgid"]);

    if (sEntityId === 'login') {
      sSearchQuery = _removeParametersFromQueryString(sSearchQuery,
          ["cid", "bt", "phid", "pphid", "epid", "eptype", "orgid", "uilang", "datalang"]);
    }
    return _getCurrentURL(sSearchQuery, sEntityId, sEntityBaseType)
  };

  let _getSharableURL = function(sEntityId, sEntityBaseType, sPortalId) {
    let sSearchQuery = window.location.search;
    return window.location.pathname + _getCurrentURL(sSearchQuery, sEntityId, sEntityBaseType, sPortalId)
  };

  let _getCurrentURL = function(sSearchQuery, sEntityId, sEntityBaseType, _sPortalId) {

    let sPhysicalCatalogId = SessionProps.getSessionPhysicalCatalogId();
    let sPrevPhysicalCatalogId = SessionProps.getSessionPreviousPhysicalCatalogId();
    let sEndpiontId = SessionProps.getSessionEndpointId();
    let sEndpiontType = SessionProps.getSessionEndpointType();
    let sOrganizationId = SessionProps.getSessionOrganizationId();
    let sPortalId = _sPortalId || SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL);
    if(CS.isEmpty(sSearchQuery) || !CS.includes(sSearchQuery, "?")){
      /**
       * Info: We can not set empty searchQuery in browser history,
       * for that purpose when we don't have anything in search query,
       * we are appending tid (temporary id) with absolutely random value - codingDoodler
       * */
      sSearchQuery = "?login=xqa3vq16193n1acxh6wteh0qn" + sSearchQuery;
    }

    let iBaseTypeCode = sEntityBaseType && _encodeBaseType(sEntityBaseType);
    let sURL = sSearchQuery;
    if (sEntityBaseType) {
      sURL = sURL +
          "&cid=" + sEntityId +
          "&bt=" + iBaseTypeCode +
          "&phid=" + sPhysicalCatalogId +
          "&pphid=" + sPrevPhysicalCatalogId +
          "&epid=" + sEndpiontId +
          "&eptype=" + sEndpiontType +
          "&orgid=" + sOrganizationId +
          "&porid=" + sPortalId;
    }
    return sURL;
  };

  /**
   * @Router Impl
   *
   * @param sEntityId: Breadcrumb Item ID [Content, Dashboard, Module, Hierarchy etc]
   * @param sBreadCrumbItemType: Breadcrumb Type from Breadcrumb Dictionary
   * @param sEntityBaseType: Content Base Type If Any
   * @description: Replaces current history state
   * @private
   */
  let _replaceParamsInWindowURL = function (sEntityId, sBreadCrumbItemType, sEntityBaseType) {
    let sURL = _getWindowURL(sEntityId, sEntityBaseType);
    let stateObj = sEntityId ?
        {
          id: sEntityId,
          type: sBreadCrumbItemType
        } :
        null;
    let bIsPushHistoryState = SharableURLProps.getIsPushHistoryState();
    if (bIsPushHistoryState) {
      CS.replaceNavigation(stateObj, "", sURL);
    }
  };

  /**
   * @Router Impl
   *
   * @param sEntityId
   * @param sBreadCrumbItemType
   * @param sEntityBaseType
   * @description: Add new item in history state
   * @private
   */
  let _addParamsInWindowURL = function (sEntityId, sBreadCrumbItemType, sEntityBaseType) {
    /*let sAlreadyExistingEntityId = LinkManager.getParameterByName('cid');
    if (sAlreadyExistingEntityId && sAlreadyExistingEntityId === sEntityId) {
      return;
    }*/

    let sURL = _getWindowURL(sEntityId, sEntityBaseType);

    let stateObj = sEntityId ?
        {
          id: sEntityId,
          type: sBreadCrumbItemType
        } :
        null;

    let bIsPushHistoryState = SharableURLProps.getIsPushHistoryState();
    if (bIsPushHistoryState) {
      CS.addNavigation(stateObj, "", sURL)
    }
  };

  /**
   * @deprecated Use new API's with history navigation
   * @param bUpdateSessionProps
   * @private
   */
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

  let _addPortalIdInWindowURL = function (sPortalId) {
    let sURL = window.location.search;
    let aRemoveParameters = [];
    let sPortalIdFromURL = _getQueryVariable("porid");

    if(sPortalIdFromURL === sPortalId) {
      return;
    }

    sPortalId && aRemoveParameters.push("porid");
    sURL = _removeParametersFromQueryString(sURL, aRemoveParameters);

    if(CS.isEmpty(sURL) || !CS.includes(sURL, "?")){
      sURL = "?login=xqa3vq16193n1acxh6wteh0qn" + sURL;
    }
    if(sPortalId){
      sURL = sURL + "&" + "porid" + "=" + sPortalId;
    }

    let oHistoryState = CS.getHistoryState();

    let oStateObj = oHistoryState ? oHistoryState : null;
    CS.replaceNavigation(oStateObj, "", sURL);
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



  let _addPortalParamsInWindowURL = function (sPortalId) {
    let sURL = window.location.search;
    let aRemoveParameters = [];
    let sPortalIdFromURL = _getQueryVariable(SharableUrlConstants.PRT_ID);

    /** If language is not changed, no need to update the URL **/
    if(sPortalIdFromURL === sPortalId){
      return;
    }

    /** Remove existing parameters **/
    sPortalId && aRemoveParameters.push(SharableUrlConstants.PRT_ID);
    sURL = _removeParametersFromQueryString(sURL, aRemoveParameters);

    if(CS.isEmpty(sURL) || !CS.includes(sURL, "?")){
      sURL = "?login=xqa3vq16193n1acxh6wteh0qn" + sURL;
    }

    if(sPortalId){
      sURL = sURL + "&" + SharableUrlConstants.PRT_ID + "=" + sPortalId;
    }

    let oHistoryState = CS.getHistoryState();

    let oStateObj = oHistoryState ? oHistoryState : null;
    CS.replaceNavigation(oStateObj, "", sURL);
  };



  var _removeWindowURL = function (bUpdateSessionProps) {
    _updateQueryProps(true, bUpdateSessionProps);
    /*let sSearchQuery = window.location.search;
    sSearchQuery = _removeParametersFromQueryString(sSearchQuery,
        [SharableUrlConstants.C_ID, SharableUrlConstants.BT, SharableUrlConstants.PH_ID, SharableUrlConstants.PH_ID_IU ,SharableUrlConstants.PPH_ID, SharableUrlConstants.EP_ID, SharableUrlConstants.EP_TYPE, SharableUrlConstants.ORG_ID, "porid"]);
    history.pushState({}, "", sSearchQuery);*/
  };

  var _updateQueryProps = function (bRemove, bUpdateSessionProps) {
    let sEntityId = "",
        sEntityBaseType = "",
        sPhysicalCatalogId = "",
        sPrevPhysicalCatalogId = "",
        sPhysicalCatalogIdInUse = "",
        sEndpiontId = "",
        sEndpiontType = "",
        sPortalId = "",
        sOrganizationId = "";

    if (!bRemove) {
      sEntityId = LinkManager.getParameterByName(SharableUrlConstants.C_ID) || "";
      sEntityBaseType = SharableURLStore.decodeBaseType(LinkManager.getParameterByName(SharableUrlConstants.BT)) || "";
    }

    SharableURLProps.setEntityId(sEntityId);
    SharableURLProps.setEntityBaseType(sEntityBaseType);

    if (bUpdateSessionProps) {
      sPhysicalCatalogId = LinkManager.getParameterByName(SharableUrlConstants.PH_ID) || SessionProps.getSessionPhysicalCatalogId();
      sPhysicalCatalogIdInUse = LinkManager.getParameterByName(SharableUrlConstants.PH_ID_IU) || SessionProps.getSessionPhysicalCatalogId();
      sPrevPhysicalCatalogId = LinkManager.getParameterByName(SharableUrlConstants.PPH_ID) || SessionProps.getSessionPreviousPhysicalCatalogId();
      sEndpiontId = LinkManager.getParameterByName(SharableUrlConstants.EP_ID) || SessionProps.getSessionEndpointId();
      sEndpiontType = LinkManager.getParameterByName(SharableUrlConstants.EP_TYPE) || SessionProps.getSessionEndpointType();
      sOrganizationId = LinkManager.getParameterByName(SharableUrlConstants.ORG_ID) || SessionProps.getSessionOrganizationId();
      sPortalId = LinkManager.getParameterByName(SharableUrlConstants.POR_ID) || SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL);

      SessionProps.setSessionPhysicalCatalogId(sPhysicalCatalogId);
      SessionProps.setSessionPreviousPhysicalCatalogId(sPrevPhysicalCatalogId);
      SessionProps.setSessionEndpointId(sEndpiontId);
      SessionProps.setSessionEndpointType(sEndpiontType);
      SessionProps.setSessionOrganizationId(sOrganizationId);
      SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.PORTAL, sPortalId);

      SessionProps.setDontResetSessionPropsURLDependencyFlag(true);
    }
  };

  /********************************* PUBLIC API's *********************************/
  return {

    addParamsInWindowURL: function (sEntityId, sBreadCrumbItemType, sEntityBaseType) {
      _addParamsInWindowURL(sEntityId, sBreadCrumbItemType, sEntityBaseType);
    },

    replaceParamsInWindowURL: function (sEntityId, sBreadCrumbItemType, sEntityBaseType) {
      _replaceParamsInWindowURL(sEntityId, sBreadCrumbItemType, sEntityBaseType);
    },

    setIsPushHistoryState: function (bIsPushHistoryState) {
      SharableURLProps.setIsPushHistoryState(bIsPushHistoryState);
    },

    getIsPushHistoryState: function () {
      return SharableURLProps.getIsPushHistoryState();
    },

    setIsEntityNavigation: function (bIsEntityNavigation) {
      SharableURLProps.setIsEntityNavigation(bIsEntityNavigation);
    },

    getIsEntityNavigation: function () {
      return SharableURLProps.getIsEntityNavigation();
    },

    /**
     * @deprecated Use new API's with history navigation
     * @param bUpdateSessionProps
     */
    removeWindowURL: function (bUpdateSessionProps) {
      _removeWindowURL(bUpdateSessionProps);
    },

    updateQueryProps: function (bRemove, bUpdateSessionProps) {
      _updateQueryProps(bRemove, bUpdateSessionProps);
    },

    decodeBaseType: function (sStr) {
      return _decodeBaseType(sStr);
    },

    encodeBaseType: function (sBaseType) {
      return _encodeBaseType(sBaseType);
    },
    getQueryVariable: function (sParameterKey) {
      return _getQueryVariable(sParameterKey);
    },

    addLanguageParamsInWindowURL: function (sUILanguageCode, sDataLanguageCode) {
      _addLanguageParamsInWindowURL(sUILanguageCode, sDataLanguageCode);
    },

    addPortalIdInWindowURL: function (sPortalId) {
      _addPortalIdInWindowURL(sPortalId);
    },

    getSharableURL: function (sEntityId, sEntityBaseType, sPortalId) {
      return _getSharableURL(sEntityId, sEntityBaseType, sPortalId)
    }
  }
})();

MicroEvent.mixin(SharableURLStore);

export default SharableURLStore;