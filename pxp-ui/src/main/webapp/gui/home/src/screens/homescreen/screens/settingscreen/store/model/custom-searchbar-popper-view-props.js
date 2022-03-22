class CustomSearchBarPopperViewProps {

  static defaultPaginationData = {
    from: 0,
    size: 20,
    sortBy: 'label',
    sortOrder: 'asc',
    searchColumn: 'label',
    searchText: ''
  };

  constructor(oInitializeData) {
    this.items= [];
    this.isDialogOpen= false;
    this.clonedItems= [];
    this.paginationData = CustomSearchBarPopperViewProps.defaultPaginationData;
    this.requestUrl= "";
    this.responsePath= [];
    //this.requestMethod= "";
    this.requestBody= "";
    this.requestType= "";
    this.entities= "";
    if (oInitializeData.requestApiData) {
      this.setRequestUrl(oInitializeData.requestApiData.requestUrl);
      this.setEntity(oInitializeData.requestApiData.entities);
      this.setRequestType(oInitializeData.requestApiData.requestType);
      this.setResponsePath(oInitializeData.requestApiData.responsePath);
      //this.setRequestMethod(oInitializeData.requestApiData.requestMethod);
      this.setRequestBody(oInitializeData.requestApiData.requestBody);
    }
    if(oInitializeData.items){
      this.setSearchItemList(oInitializeData.items);
    }
  }

  getRequestType () {
    return this.requestType
  };

  setRequestType (_oRequestType) {
    this.requestType = _oRequestType;
  };

  getEntity () {
    return this.entities
  };

  setEntity (_entities) {
    this.entities = _entities;
  }

  getPaginationData () {
    return this.paginationData;
  };

  getRequestBody () {
    return this.requestBody;
  };

  setRequestBody (_oRequestBody) {
    this.requestBody = _oRequestBody;
  };

  //getRequestMethod () {
    //return this.requestMethod;
  //};

  //setRequestMethod (_sMethod) {
    //this.requestMethod = _sMethod;
  //};

  getRequestResponseInfo () {
    return this.requestResponseInfo;
  };

  getRequestUrl () {
    return this.requestUrl;
  };

  setRequestUrl (_sUrl) {
    this.requestUrl = _sUrl;
  };

  getResponsePath () {
    return this.responsePath;
  };

  setResponsePath (_aResponsePath) {
    this.responsePath = _aResponsePath;
  };

  getClonedList () {
    return this.clonedItems
  };

  setCloneSearchItemList (_aItems) {
    this.clonedItems = _aItems;
  };

  getSearchItemList () {
    return this.items;
  };

  setSearchItemList (_aItems) {
    this.clonedItems && this.clonedItems.length > 0 ? "" : this.setCloneSearchItemList(_aItems); //eslint-disable-line
    this.items = _aItems;
  };

  getDialogOpen () {
    return this.isDialogOpen;
  };

  setDialogOpen(_bIsDialogOpen) {
    this.isDialogOpen = _bIsDialogOpen;
  };

  resetSearchList () {
    this.items = this.clonedItems;
  };

  destroyProps () {
    this.items = [];
    this.clonedItems = []
  };
};

export default CustomSearchBarPopperViewProps
