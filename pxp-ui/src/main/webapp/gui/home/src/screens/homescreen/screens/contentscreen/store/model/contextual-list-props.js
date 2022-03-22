class ContextualListProps {

  static defaultPaginationData = {
    from: 0,
    size: 20,
    totalCount: 0,
    currentPageItems: 0,
    sortBy: 'label',
    sortOrder: 'asc',
    searchColumn: 'label',
    searchText: ''

  };

  constructor (sContext, oRequestResponseInfo) {
    this.requestResponseInfo = oRequestResponseInfo;
    this.referencedData = {};
    this.paginationData = ContextualListProps.defaultPaginationData;
    this.requestUrl = "";
    this.requestFormat = {};
    this.responsePath = [];
    this.requestQueryString = {};
    this.itemsData = [];
    this.extraDataMap = {};
    this.contextId = sContext;
  }

  getReferencedData () {
    return this.referencedData;
  };

  getItemsData () {
    return this.itemsData;
  };

  setItemsData (aItemsData) {
    this.itemsData = aItemsData;
  };

  getPaginationData () {
    return this.paginationData;
  };

  setPaginationData (oPaginationData) {
    this.paginationData = oPaginationData;
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

  getExtraDataMap () {
    return this.extraDataMap;
  };

  setExtraDataMap (_oExtraDataMap) {
    this.extraDataMap = _oExtraDataMap;
  };

  resetPaginationData () {
    this.paginationData = ContextualListProps.defaultPaginationData;
  };
}
export default ContextualListProps;
