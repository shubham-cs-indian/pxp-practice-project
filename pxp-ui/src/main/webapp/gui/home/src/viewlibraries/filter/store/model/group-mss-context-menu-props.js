import CS from '../../../../libraries/cs';
let groupsDataMap = {};
class GroupMssContextMenuProps {

    static defaultPaginationData = {
      from: 0,
      size: 20,
      sortBy: 'label',
      sortOrder: 'asc',
      searchColumn: 'label',
      searchText: ''
    };
  
    constructor (aRequestResponseInfo) {
      this.requestResponseInfo = {};
      this.requestUrl = {};
      this.requestFormat = {};
      this.responsePath = [];
      this.requestQueryString = {};
      this.groupsData = {};
      this.activeGroup = "";
      this.paginationData = {};
      this.customPaginationData = {};
      CS.forEach(aRequestResponseInfo, oReqResObj=> {
        if (oReqResObj && oReqResObj.requestInfo) {
          this.setPaginationData(oReqResObj.requestInfo.paginationData, oReqResObj.id);
          this.setRequestResponseInfo(oReqResObj.requestInfo, oReqResObj.id);
          this.setRequestUrl(oReqResObj.requestInfo.requestURL, oReqResObj.id);
          this.setRequestFormat(oReqResObj.requestInfo.requestFormat, oReqResObj.id);
          this.setResponsePath(oReqResObj.requestInfo.responsePath, oReqResObj.id);
          this.setQueryString(oReqResObj.requestInfo.requestQueryString, oReqResObj.id);
        }
      })
    }
  
  
    getGroupsData () {
      return this.groupsData;
    };
  
    setGroupsData (aGroupsData) {
      Object.assign(this.groupsData, aGroupsData)
    };

    resetGroupsData() {
      CS.forOwn(this.groupsData, (value, sGroupType) => {
          if (this.requestResponseInfo[sGroupType]) {
            CS.set(this.groupsData[sGroupType], 'list', []);
          }
      })
    }
  
    getPaginationData (sGroupType) {
      return this.paginationData[sGroupType];
    };
  
    getRequestUrl (sGroupType) {
      return this.requestUrl[sGroupType];
    };
  
    setRequestUrl (_sUrl, sGroupType) {
      this.requestUrl[sGroupType] = _sUrl;
    };
  
    getRequestFormat (sGroupType) {
      return this.requestFormat[sGroupType];
    };
  
    setRequestFormat (_oRequestFormat, sGroupType) {
      this.requestFormat[sGroupType] = _oRequestFormat;
    };
  
    getQueryString (sGroupType) {
      return this.requestQueryString[sGroupType];
    };
  
    setQueryString (_oQueryString, sGroupType) {
      this.requestQueryString[sGroupType] = _oQueryString;
    };
  
    getResponsePath (sGroupType) {
      return this.responsePath[sGroupType];
    };
  
    setResponsePath (_aResponsePath, sGroupType) {
      this.responsePath[sGroupType] = _aResponsePath;
    };
  
    getRequestResponseInfo (sGroupType) {
      return this.requestResponseInfo[sGroupType];
    };
  
    setRequestResponseInfo (_oRequestResponseInfo, sGroupType) {
      this.requestResponseInfo[sGroupType] = _oRequestResponseInfo;
    };
  
    resetPaginationData (sGroupType) {
      let oDefaultPaginationData = CS.cloneDeep(GroupMssContextMenuProps.defaultPaginationData);
      let oCustomPaginationData = CS.cloneDeep(this.customPaginationData[sGroupType]);
      this.paginationData[sGroupType] = Object.assign(oDefaultPaginationData, oCustomPaginationData);
    };

    setGroupsDataMap(_oGroupsDataMap) {
      groupsDataMap[_oGroupsDataMap.id] = _oGroupsDataMap;
    };

    getGroupsDataMap(_sKey) {
      return groupsDataMap[_sKey];
    };

    setActiveGroup(_sActiveGroup) {
      this.activeGroup = _sActiveGroup;
    };

    getActiveGroup() {
      return this.activeGroup;
    };

    setPaginationData(oPaginationData, sGroupType) {
      this.customPaginationData[sGroupType] = oPaginationData;
      let oDefaultPaginationData = CS.cloneDeep(GroupMssContextMenuProps.defaultPaginationData);
      let oPagination = Object.assign(oDefaultPaginationData, oPaginationData);
      this.paginationData[sGroupType] = oPagination;
  };
};

export default GroupMssContextMenuProps;