import CS from '../../../../../../libraries/cs';
import PaginationRequestKeysDictionary from '../../../../../../commonmodule/tack/pagination-request-keys-dictionary';
import ContextualListPropsFactory from '../model/contextual-list-props-factory';
import ContentScreenRequestMapping from '../../tack/content-screen-request-mapping';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';

let ContextualListStore = function (sContext, oRequestResponseInfo, fTriggerChangeHandler) {

  let oContextualListProps = {};
  let requestType = "";
  let entityName = "";
  let keyResponseMapForExtraData = {};

  let _setContextualLazyContextMenuPropsByContext = function (sContext, oRequestResponseInfo) {
    return ContextualListPropsFactory.setPropsByContext(sContext, oRequestResponseInfo)
  };

  let _resetStoreProps = function (sContext, oRequestResponseInfo) {
    requestType = oRequestResponseInfo.requestType;
    entityName = oRequestResponseInfo.entityName;
    keyResponseMapForExtraData = oRequestResponseInfo.keyResponseMapForExtraData;

    oContextualListProps = _setContextualLazyContextMenuPropsByContext(sContext, oRequestResponseInfo);
    oContextualListProps.setRequestUrl(oRequestResponseInfo.requestURL);
    oContextualListProps.setRequestFormat(oRequestResponseInfo.requestFormat);
    oContextualListProps.setResponsePath(oRequestResponseInfo.responsePath);
    oContextualListProps.setQueryString(oRequestResponseInfo.requestQueryString);
  };

  let _getStoreProps = function(){
    return oContextualListProps;
  };

  //Constructor Implementation
  _resetStoreProps(sContext, oRequestResponseInfo);


  let _triggerChange = () => {
    fTriggerChangeHandler && fTriggerChangeHandler();
  };

  let _addToReferencedData = (aSelectedIds, aData) => {
    let oReferencedData = oContextualListProps.getReferencedData();
    let oData = CS.keyBy(aData, 'id');
    CS.forEach(aSelectedIds, function (sId) {
      if (oData[sId]) {
        oReferencedData[sId] = oData[sId];
      }
    });
  };

  let _getDataFromPath = (aResponsePath, oResponse) => {
    try {
      CS.forEach(aResponsePath, function (sPathId) {
        oResponse = oResponse[sPathId];
      });
    } catch (e) {
      ExceptionLogger.error(e);
    }

    return oResponse;
  };

  let _getDataFromResponsePath = (oResponse) => {
    if (requestType == "configData") {
      return oResponse.success[entityName].list;
    }
    else {
      let aResponsePath = oContextualListProps.getResponsePath();
      oResponse = _getDataFromPath(aResponsePath, oResponse);
    }
    return oResponse;
  };

  let _isPaginationKey = (sKey) => {
    let bIsPaginationKey = false;

    CS.forEach(PaginationRequestKeysDictionary, function (sPaginationKey) {
      if (sPaginationKey === sKey) {
        bIsPaginationKey = true;
        return false; //exit forEach
      }
    });

    return bIsPaginationKey
  };

  let _processRequestData = (oRequestData) => {
    let oProcessedData = {};
    let oPaginationData = oContextualListProps.getPaginationData();
    CS.forEach(oRequestData, function (oData, sKey) {
      if (CS.isObject(oData) || CS.isArray(oData)) {
        _processRequestData(oData);
      } else if (CS.isString(oData)) {
        if (_isPaginationKey[oData]) {
          oProcessedData[sKey] = oPaginationData[oData];
        }
      }
    });
    CS.assign(oRequestData, oProcessedData);
  }

  let _getDataFromRequestFormat = (sSearchKey = "searchText") => {
    let oPaginationData = oContextualListProps.getPaginationData();
    let oRequestData = {};
    let oReqResInfo = oContextualListProps.getRequestResponseInfo();

    if (requestType === "configData") {
      oRequestData = {
        searchColumn: oPaginationData.searchColumn,
        searchText: oPaginationData.searchText,
        entities: {}
      };

      let oEntityReqData = {
        from: oPaginationData.from,
        size: oPaginationData.size,
        totalCount: oPaginationData.totalCount,
        currentPageItems: oPaginationData.currentPageItems,
        sortBy: oPaginationData.sortBy,
        sortOrder: oPaginationData.sortOrder,
        types: oReqResInfo.types || [],
        typesToExclude: oReqResInfo.typesToExclude || []
      };

      if (oReqResInfo.customRequestModel) {
        CS.assign(oEntityReqData, oReqResInfo.customRequestModel);
      }
      oRequestData.entities[entityName] = oEntityReqData;

    } else {
      oRequestData = {
        searchColumn: oPaginationData.searchColumn,
        [sSearchKey]: oPaginationData[sSearchKey],
        from: oPaginationData.from,
        size: oPaginationData.size,
        totalCount: oPaginationData.totalCount,
        currentPageItems: oPaginationData.currentPageItems,
        sortBy: oPaginationData.sortBy,
        sortOrder: oPaginationData.sortOrder,
      };
      CS.assign(oRequestData, oReqResInfo.customRequestModel);
      //HANDLE CUSTOM REQUEST MODEL
    }

    return oRequestData;
  };

  let _getUrl = function () {
    if (requestType == "configData") {
      return ContentScreenRequestMapping.GetConfigData;
    }

    let oReqResInfo = oContextualListProps.getRequestResponseInfo();
    return oReqResInfo.requestURL;
  };

  let _handleSearchClicked = (sSearchText, sSearchKey = "searchText") => {
    oContextualListProps.setItemsData([]);
    oContextualListProps.resetPaginationData();

    let oPaginationData = oContextualListProps.getPaginationData();
    oPaginationData[sSearchKey] = sSearchText || "";
    oPaginationData.from = 0;

    let oRequestData = _getDataFromRequestFormat(sSearchKey);

    CS.postRequest(_getUrl(), oContextualListProps.getQueryString(), oRequestData,
        successFetchItemsData.bind(this, true), failureFetchItemsData);
  };

  let _handleLoadMoreClicked = function (bShouldReplaceList= false) {
    let oRequestData = _getDataFromRequestFormat();

    CS.postRequest(_getUrl(), oContextualListProps.getQueryString(), oRequestData,
        successFetchItemsData.bind(this, bShouldReplaceList), failureFetchItemsData);
  };

  let _handlePaginationChanged = function (oCurrentPaginationData = {}) {
    let oPaginationData = oContextualListProps.getPaginationData();
    oContextualListProps.setPaginationData(CS.assign(oPaginationData, oCurrentPaginationData));
    _handleLoadMoreClicked(true);
  };

  let _updateExtraDataMap = function (oResponse) {
    let oExtraDataMap = oContextualListProps.getExtraDataMap();
    if (requestType == "configData") {
      oResponse = oResponse.success[entityName];
    }
    CS.forEach(keyResponseMapForExtraData, function (aPath, sKey) {
      let oData = _getDataFromPath(aPath, oResponse);
      oExtraDataMap[sKey] = oData;
    });
  };

  let _updatePaginationData = function (oResponse = {}) {
    let oSuccess = oResponse.success;
    let oPaginationData = oContextualListProps.getPaginationData();
    oPaginationData.from = oSuccess.from;
    oPaginationData.totalCount = oSuccess.totalContents;
    oPaginationData.currentPageItems = CS.size(_getDataFromResponsePath(oResponse));
  };

  let successFetchItemsData = function (bShouldReplaceList, oResponse) {
    let aItemsDataFromResponse = _getDataFromResponsePath(oResponse);
    _updateExtraDataMap(oResponse);
    _updatePaginationData(oResponse);
    if (bShouldReplaceList) {
      oContextualListProps.setItemsData(aItemsDataFromResponse);
    } else {
      let aItemsData = oContextualListProps.getItemsData() || [];
      oContextualListProps.setItemsData(aItemsData.concat(aItemsDataFromResponse));
    }
    _triggerChange();
  };

  let failureFetchItemsData = function (oResponse) {
    ExceptionLogger.log(oResponse);
    ExceptionLogger.error('failureFetchItemsData');
  };

  return {

    //Do not remove or change name
    addToReferencedData: (aSelectedIds, aData) => {
      _addToReferencedData(aSelectedIds, aData);
    },

    //Do not remove or change name
    getReferencedData: () => {
      return oContextualListProps.getReferencedData();
    },

    //Do not remove or change name
    updateReferencedData: (_oReferencedData) => {
      let oReferencedData = oContextualListProps.getReferencedData();
      CS.assign(oReferencedData, _oReferencedData);
    },

    //Do not remove or change name
    getUpdatedReferencedData: () => {
      return oContextualListProps.getReferencedData();
    },

    //Do not remove or change name
    handleSearchClicked: (sSearchText, sSearchKey) => {
      _handleSearchClicked(sSearchText, sSearchKey);
    },

    //Do not remove or change name
    handleLoadMoreClicked: () => {
      _handleLoadMoreClicked();
    },

    //Do not remove or change name
    setItemsData: (aItemsData) => {
      oContextualListProps.setItemsData(aItemsData);
    },

    //Do not remove or change name
    getItemsData: () => {
      return oContextualListProps.getItemsData();
    },

    getExtraDataMap: () => {
      return oContextualListProps.getExtraDataMap();
    },

    setExtraDataMap: (oExtraDataMap) => {
      oContextualListProps.setExtraDataMap(oExtraDataMap);
    },

    //Do not remove or change name
    getSearchText: (sSearchKey = "searchText") => {
      let oPaginationData = oContextualListProps.getPaginationData();
      return oPaginationData[sSearchKey];
    },

    handlePaginationChanged: (oPaginationData) => {
      _handlePaginationChanged(oPaginationData);
    },

    resetStoreProps: function(oRequestResponseData){
      _resetStoreProps(oRequestResponseData);
    },

    /**
     * @deprecated
     * USE THIS API IN USE CASES TO GET FIRED!
     * **/
    getStoreProps: function(){
      return _getStoreProps();
    },

    triggerChange: () => {
      _triggerChange();
    }

  };

};

MicroEvent.mixin(ContextualListStore);

export default ContextualListStore;
