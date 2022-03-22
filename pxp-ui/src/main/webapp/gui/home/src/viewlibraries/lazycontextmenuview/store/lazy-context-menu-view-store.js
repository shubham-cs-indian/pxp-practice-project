import CS from '../../../libraries/cs';
import PaginationRequestKeysDictionary from '../../../commonmodule/tack/pagination-request-keys-dictionary';
import LazyContextMenuViewProps from './model/lazy-context-menu-view-props';
import { LazyDataRequestMapping } from '../../tack/view-library-request-mapping';
import ExceptionLogger from '../../../libraries/exceptionhandling/exception-logger';

let LazyContextMenuViewStore = function (oRequestResponseInfo, fTriggerChangeHandler, fPostMethodToCall) {

  let oLazyContextMenuViewProps = {};
  let requestType = "";
  let entityName = "";
  let keyResponseMapForExtraData = {};
  let keysToRemove = [];

  let _resetStoreProps = function (oRequestResponseInfo) {
    requestType = oRequestResponseInfo.requestType;
    entityName = oRequestResponseInfo.entityName;
    keysToRemove = oRequestResponseInfo.keysToRemove;
    keyResponseMapForExtraData = oRequestResponseInfo.keyResponseMapForExtraData;

    oLazyContextMenuViewProps = new LazyContextMenuViewProps(oRequestResponseInfo);
    oLazyContextMenuViewProps.setRequestUrl(oRequestResponseInfo.requestURL);
    oLazyContextMenuViewProps.setRequestFormat(oRequestResponseInfo.requestFormat);
    oLazyContextMenuViewProps.setResponsePath(oRequestResponseInfo.responsePath);
    oLazyContextMenuViewProps.setQueryString(oRequestResponseInfo.requestQueryString);
    oLazyContextMenuViewProps.setReqResExtraData(oRequestResponseInfo.reqResExtraData);
    oLazyContextMenuViewProps.setResponsePostProcessingFunc(oRequestResponseInfo.responsePostProcessingFunc);
  };

  let _getStoreProps = function(){
    return oLazyContextMenuViewProps;
  };

  //Constructor Implementation
  _resetStoreProps(oRequestResponseInfo);


  let _triggerChange = () => {
    fTriggerChangeHandler && fTriggerChangeHandler();
  };

  let _addToReferencedData = (aSelectedIds, aData) => {
    let oReferencedData = oLazyContextMenuViewProps.getReferencedData();
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
      let aResponsePath = oLazyContextMenuViewProps.getResponsePath();
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
    let oPaginationData = oLazyContextMenuViewProps.getPaginationData();
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

  let _removeKeysFromRequestData = (oRequestData, aKeysToRemove) => {
    CS.forEach(aKeysToRemove, function (sKeyToRemove) {
      delete oRequestData[sKeyToRemove];
    });
  };

  let _getDataFromRequestFormat = () => {
    let oRequestFormat = oLazyContextMenuViewProps.getRequestFormat();
    let oPaginationData = oLazyContextMenuViewProps.getPaginationData();
    // let oRequestData = CS.cloneDeep(oRequestFormat);
    // _processRequestData(oRequestData);


    let oRequestData = {};
    let oReqResInfo = oLazyContextMenuViewProps.getRequestResponseInfo();

    if (requestType === "configData") {
      oRequestData = _getRequestData(oRequestData , oReqResInfo, oPaginationData);

    } else if (requestType === "custom") {
      oRequestData.from = oPaginationData.from;
      oRequestData.size = oPaginationData.size;
      oRequestData.searchText = oPaginationData.searchText;
      CS.assign(oRequestData, oReqResInfo.customRequestModel);
      _removeKeysFromRequestData(oRequestData, keysToRemove);
    } else if (requestType === "customImpExp") {
      oRequestData = _getRequestData(oRequestData, oReqResInfo, oPaginationData);
    } else {
      oRequestData = {
        searchColumn: oPaginationData.searchColumn,
        searchText: oPaginationData.searchText,
        from: oPaginationData.from,
        size: oPaginationData.size,
        sortBy: oPaginationData.sortBy,
        sortOrder: oPaginationData.sortOrder,
      };
      CS.assign(oRequestData, oReqResInfo.customRequestModel);
      _removeKeysFromRequestData(oRequestData, keysToRemove);
      //HANDLE CUSTOM REQUEST MODEL
    }

    return oRequestData;
  };

  let _getRequestData = function (oRequestData, oReqResInfo, oPaginationData) {
    oRequestData = {
      searchColumn: oPaginationData.searchColumn,
      searchText: oPaginationData.searchText,
      entities: {}
    };

    let oEntityReqData = {
      from: oPaginationData.from,
      size: oPaginationData.size,
      sortBy: oPaginationData.sortBy,
      sortOrder: oPaginationData.sortOrder,
      types: oReqResInfo.types || [],
      typesToExclude: oReqResInfo.typesToExclude || []
    };

    if (oReqResInfo.customRequestModel) {
      CS.assign(oEntityReqData, oReqResInfo.customRequestModel);
    }
    oRequestData.entities[entityName] = oEntityReqData;
    return oRequestData;
  };

  let _getUrl = function () {
    if (requestType == "configData") {
      return LazyDataRequestMapping.GetConfigData;
    }

    let oReqResInfo = oLazyContextMenuViewProps.getRequestResponseInfo();
    return oReqResInfo.requestURL;
  };

  let _handleSearchClicked = (sSearchText, oCallbackData = {}) => {
    let oPaginationData = oLazyContextMenuViewProps.getPaginationData();
    oPaginationData.searchText = sSearchText || "";
    oPaginationData.from = 0;

    let oRequestData = _getDataFromRequestFormat();

    let fAjaxMethodToCall = _getAjaxMethodToCall();
    let oReqResExtraData = oLazyContextMenuViewProps.getReqResExtraData();
    fAjaxMethodToCall.call(this, _getUrl(), oLazyContextMenuViewProps.getQueryString(), oRequestData,
      successFetchItemsData.bind(this, true, oCallbackData), failureFetchItemsData, "", oReqResExtraData);
  };

  let _handleLoadMoreClicked = function () {
    let oPaginationData = oLazyContextMenuViewProps.getPaginationData();
    let oGetCustomReqResInfo = oLazyContextMenuViewProps.getRequestResponseInfo().customRequestModel;
    if (oGetCustomReqResInfo && oGetCustomReqResInfo.isCustomPaginationOnLoadMore) {
      let oCustomPaginationModel = oGetCustomReqResInfo.customPaginateOnLoadModel;
      oPaginationData.from = oCustomPaginationModel.from;
      oPaginationData.size = oCustomPaginationModel.size;
      oLazyContextMenuViewProps.setItemsData([]);
    } else {
      oPaginationData.from = CS.size(oLazyContextMenuViewProps.getItemsData());
    }

    let oRequestData = _getDataFromRequestFormat();

    let fAjaxMethodToCall = _getAjaxMethodToCall();
    let oReqResExtraData = oLazyContextMenuViewProps.getReqResExtraData();
    fAjaxMethodToCall.call(this, _getUrl(), oLazyContextMenuViewProps.getQueryString(), oRequestData,
        successFetchItemsData.bind(this, false, {}), failureFetchItemsData, "", oReqResExtraData);
  };

  let _getAjaxMethodToCall = function () {
    if (CS.isFunction(oRequestResponseInfo.postMethodToCall)) {
      return oRequestResponseInfo.postMethodToCall;
    } else {
      if (CS.isFunction(fPostMethodToCall)) {
        return fPostMethodToCall;
      } else {
        return CS.postRequest;
      }
    }
  }

  let _updateExtraDataMap = function (oResponse) {
    let oExtraDataMap = oLazyContextMenuViewProps.getExtraDataMap();
    if (requestType == "configData") {
      oResponse = oResponse.success[entityName];
    }
    CS.forEach(keyResponseMapForExtraData, function (aPath, sKey) {
      let oData = _getDataFromPath(aPath, oResponse);
      oExtraDataMap[sKey] = oData;
    });
  };

  let _removeExcludedEntities = function (aItemsDataFromResponse = []) {
    let oReqResInfo = oLazyContextMenuViewProps.getRequestResponseInfo();
    let aEntitiesToExclude = oReqResInfo.entitiesToExclude;
    if (CS.isNotEmpty(aEntitiesToExclude)) {
      CS.forEach(aEntitiesToExclude, (sEntity) => {
        CS.remove(aItemsDataFromResponse, { id : sEntity})
      });
    }
    return aItemsDataFromResponse;
  };

  let _postProcessItems = function (oResponse) {
    if (CS.isFunction(oLazyContextMenuViewProps.getResponsePostProcessingFunc())) {
      let oFunc = oLazyContextMenuViewProps.getResponsePostProcessingFunc();
      oFunc(oResponse.success);
    }
  };

  let _addDisablePropertyToItems = function (aItems) {
    CS.forEach(aItems, function (oItem) {
      if (CS.isNotEmpty(oItem) && oItem.hasOwnProperty('count')) {
        let iCountValue = oItem.count;
        oItem.isDisabled = (iCountValue == 0);
      }
    });
  };

  let successFetchItemsData = function (bShouldReplaceList, oCallbackData, oResponse) {
    _postProcessItems(oResponse);
    let aItemsDataFromResponse = _getDataFromResponsePath(oResponse);
    aItemsDataFromResponse = _removeExcludedEntities(aItemsDataFromResponse);
    _updateExtraDataMap(oResponse);
    _addDisablePropertyToItems(aItemsDataFromResponse);
    if (bShouldReplaceList) {
      oLazyContextMenuViewProps.setItemsData(aItemsDataFromResponse);
    } else {
      let aItemsData = oLazyContextMenuViewProps.getItemsData() || [];
      oLazyContextMenuViewProps.setItemsData(aItemsData.concat(aItemsDataFromResponse));
    }

    if (oCallbackData && oCallbackData.funcToExecute) {
      oCallbackData.funcToExecute();
    }

    _triggerChange();
  };

  let failureFetchItemsData = function (oResponse) {
    ExceptionLogger.log(oResponse);
    ExceptionLogger.error('failureFetchItemsData');
  };

  let _resetPropsOnDropdownClose = function () {
    //Reset items list and pagination.
    oLazyContextMenuViewProps.setItemsData([]);
    oLazyContextMenuViewProps.resetPaginationData();
  };

  return {

    //Do not remove or change name
    addToReferencedData: (aSelectedIds, aData) => {
      _addToReferencedData(aSelectedIds, aData);
    },

    //Do not remove or change name
    getReferencedData: () => {
      return oLazyContextMenuViewProps.getReferencedData();
    },

    //Do not remove or change name
    updateReferencedData: (_oReferencedData) => {
      let oReferencedData = oLazyContextMenuViewProps.getReferencedData();
      CS.assign(oReferencedData, _oReferencedData);
    },

    //Do not remove or change name
    getUpdatedReferencedData: () => {
      return oLazyContextMenuViewProps.getReferencedData();
    },

    //Do not remove or change name
    handleSearchClicked: (sSearchText, oCallbackData) => {
      _handleSearchClicked(sSearchText, oCallbackData);
    },

    //Do not remove or change name
    handleLoadMoreClicked: () => {
      _handleLoadMoreClicked();
    },

    //Do not remove or change name
    setItemsData: (aItemsData) => {
      oLazyContextMenuViewProps.setItemsData(aItemsData);
    },

    //Do not remove or change name
    getItemsData: () => {
      return oLazyContextMenuViewProps.getItemsData();
    },

    getExtraDataMap: () => {
      return oLazyContextMenuViewProps.getExtraDataMap();
    },

    setExtraDataMap: (oExtraDataMap) => {
      oLazyContextMenuViewProps.setExtraDataMap(oExtraDataMap);
    },

    //Do not remove or change name
    getSearchText: () => {
      let oPaginationData = oLazyContextMenuViewProps.getPaginationData();
      return oPaginationData.searchText;
    },

    resetPropsOnDropdownClose: function(){
      _resetPropsOnDropdownClose();
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


export default LazyContextMenuViewStore;