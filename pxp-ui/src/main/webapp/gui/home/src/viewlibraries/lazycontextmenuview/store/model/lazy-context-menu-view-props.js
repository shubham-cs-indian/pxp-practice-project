let defaultPaginationData = function () {
  return {
    from: 0,
    size: 20,
    sortBy: 'label',
    sortOrder: 'asc',
    searchColumn: 'label',
    searchText: ''
  };
};

class LazyContextMenuViewProps {

  constructor (oRequestResponseInfo) {
    this.requestResponseInfo = oRequestResponseInfo;
    this.referencedData = {};
    this.paginationData = defaultPaginationData();
    this.requestUrl = "";
    this.requestFormat = {};
    this.responsePath = [];
    this.requestQueryString = {};
    this.itemsData = [];
    this.extraDataMap = {};
    this.reqResExtraData = {};
    this.resPostProcessingFunc = {};
  }


  getReferencedData () {
    return this.referencedData;
  };

  setReferencedData (oReferencedData) {
    this.referencedData = oReferencedData;
  }

  getItemsData () {
    return this.itemsData;
  };

  setItemsData (aItemsData) {
    this.itemsData = aItemsData;
  };

  getPaginationData () {
    return this.paginationData;
  };

  getRequestUrl () {
    return this.requestUrl;
  };

  setRequestUrl (_sUrl) {
    this.requestUrl = _sUrl;
  };

  getRequestFormat () {
    return this.requestFormat;
  };

  setRequestFormat (_oRequestFormat) {
    this.requestFormat = _oRequestFormat;
  };

  getQueryString () {
    return this.requestQueryString;
  };

  setQueryString (_oQueryString) {
    this.requestQueryString = _oQueryString;
  };

  getResponsePath () {
    return this.responsePath;
  };

  setResponsePath (_aResponsePath) {
    this.responsePath = _aResponsePath;
  };

  getRequestResponseInfo () {
    return this.requestResponseInfo;
  };

  setRequestResponseInfo (_oRequestResponseInfo) {
    this.requestResponseInfo = _oRequestResponseInfo;
  };

  getReqResExtraData () {
    return this.reqResExtraData;
  };

  setReqResExtraData (_oReqResExtraData) {
    this.reqResExtraData = _oReqResExtraData;
  };

  getResponsePostProcessingFunc () {
    return this.resPostProcessingFunc;
  };

  setResponsePostProcessingFunc (_fResPostProcessingFunc) {
    this.resPostProcessingFunc = _fResPostProcessingFunc;
  };

  getExtraDataMap () {
    return this.extraDataMap;
  };

  setExtraDataMap (_oExtraDataMap) {
    this.extraDataMap = _oExtraDataMap;
  };

  resetPaginationData () {
    this.paginationData = defaultPaginationData();
  };
}
export default LazyContextMenuViewProps;