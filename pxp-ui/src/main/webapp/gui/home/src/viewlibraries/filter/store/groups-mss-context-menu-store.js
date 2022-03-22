import CS from '../../../libraries/cs';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import GroupMssContextMenuProps from './model/group-mss-context-menu-props';
import { LazyDataRequestMapping } from '../../tack/view-library-request-mapping';
import ExceptionLogger from '../../../libraries/exceptionhandling/exception-logger';
import ContentUtils from '../../../screens/homescreen/screens/contentscreen/store/helper/content-utils';

let GroupMSSContextMenuStore = function (oRequestResponseInfo) {
  let requestType = {},
    entityName = {},
    oGroupMssContextMenuProps = {};

  oRequestResponseInfo = CS.isArray(oRequestResponseInfo) ? oRequestResponseInfo : [oRequestResponseInfo];
  CS.forEach(oRequestResponseInfo, reqObj => {
    if (reqObj && reqObj.requestInfo) {
      requestType[reqObj.id] = reqObj.requestInfo.requestType;
      entityName[reqObj.id] = reqObj.requestInfo.entityName || reqObj.requestInfo.entities;
      entityName[reqObj.id] = CS.isArray(entityName[reqObj.id]) ? entityName[reqObj.id] : [entityName[reqObj.id]];
    }
  })

  //Constructor Implementation
  oGroupMssContextMenuProps = new GroupMssContextMenuProps(oRequestResponseInfo);

  /**
   * Function to trigger event which will be listened by view
   */
  let _triggerChange = () => {
    //TODO: Resolve prototype handling
    GroupMSSContextMenuStore.prototype.trigger('group-mss-view-data-changed');
  };

  /**
   * Function which will return the url to make api calls
   */
  let _getUrl = function (sGroupType) {
    let oReqResInfo = oGroupMssContextMenuProps.getRequestResponseInfo(sGroupType);
    if(oReqResInfo.requestURL) {
      return oReqResInfo.requestURL;
    } else if (requestType[sGroupType] == "configData") {
      return LazyDataRequestMapping.GetConfigData;
    } else {
        throw Error("Invalid Request URL");
    }
  };

  /**
   * Function to create api call request object
   */
  let _getDataFromRequestFormat = (sGroupType) => {
    // let oRequestFormat = oGroupMssContextMenuProps.getRequestFormat();
    let oPaginationData = oGroupMssContextMenuProps.getPaginationData(sGroupType);
    // let oRequestData = CS.cloneDeep(oRequestFormat);
    // _processRequestData(oRequestData);
    let oRequestData = {};
    let sSelectedModuleId = ContentUtils.isCollectionScreen() ? "allmodule" : ContentUtils.getSelectedModuleId();
    let oReqResInfo = oGroupMssContextMenuProps.getRequestResponseInfo(sGroupType);
    if (requestType[sGroupType] === "configData") {
      oRequestData = {
        searchColumn: oPaginationData.searchColumn,
        searchText: oPaginationData.searchText,
        entities: {},
        moduleId: sSelectedModuleId
      };
      if (oReqResInfo.customRequestInfoModel) {
          CS.assign(oRequestData, oReqResInfo.customRequestInfoModel);
      }
      CS.each(entityName[sGroupType], (name) => {
        let oEntityReqData = {
          from: oPaginationData.from,
          size: oPaginationData.size,
          sortBy: oPaginationData.sortBy,
          sortOrder: oPaginationData.sortOrder,
          types: oReqResInfo.types || [],
          isDisabled: oReqResInfo.isDisabled,
          typesToExclude: oReqResInfo.typesToExclude || [],
          idsToExclude: oReqResInfo.entitiesToExclude || []
        };
        if (oReqResInfo.customRequestModel) {
          CS.assign(oEntityReqData, oReqResInfo.customRequestModel);
        }
        oRequestData.entities[name] = oEntityReqData;
      })
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
    }
    return oRequestData;
  };

  /**
   * Function to find the path of required object in response
   * @param {object} oResponse Api call response object
   */
  let _getDataFromResponsePath = (oResponse) => {
    let aGroupData = oGroupMssContextMenuProps.getGroupsData();
    let sGroupType = oGroupMssContextMenuProps.getActiveGroup();
    let response = {};
    response[sGroupType] = CS.cloneDeep(aGroupData[sGroupType]);
    let aResponsePath = oGroupMssContextMenuProps.getResponsePath(sGroupType);
    let oDataFromPath = _getDataFromPath(aResponsePath, oResponse);
    response[sGroupType].list = oDataFromPath.list;
    response[sGroupType].totalCount = oDataFromPath.totalCount;
    return response;
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

  /**
   * Function to make api calls for load more options
   * @param {string} sGroupType  type of group through load more button is clicked.
   */
  let _handleLoadMoreClicked = function (sGroupType) {
    let oPaginationData = oGroupMssContextMenuProps.getPaginationData(sGroupType);
    let oAllData = oGroupMssContextMenuProps.getGroupsData();
    oPaginationData.from = CS.size(oAllData[sGroupType].list);

    let oRequestData = _getDataFromRequestFormat(sGroupType);
    // oRequestData.entities = CS.pick(oRequestData.entities, sGroupType);

    CS.postRequest(_getUrl(sGroupType), oGroupMssContextMenuProps.getQueryString(sGroupType), oRequestData,
      successFetchItemsData.bind(this, false), failureFetchItemsData);
  };

  /**
   * Function to make api call with filtered string
   * @param {string} sSearchString String required to search the api call response
   */
  let _handleSearchInputApi = (sSearchText) => {
    let sGroupType = oGroupMssContextMenuProps.getActiveGroup();
    if (sSearchText) {
      oGroupMssContextMenuProps.resetGroupsData([]);
    }
    oGroupMssContextMenuProps.resetPaginationData(sGroupType);

    let oPaginationData = oGroupMssContextMenuProps.getPaginationData(sGroupType);
    oPaginationData.searchText = sSearchText || "";
    oPaginationData.from = 0;

    let oRequestData = _getDataFromRequestFormat(sGroupType);

    CS.postRequest(_getUrl(sGroupType), oGroupMssContextMenuProps.getQueryString(sGroupType), oRequestData,
      successFetchItemsData.bind(this, true), failureFetchItemsData);
  };

  /**
   * Function to handle success response of api calls
   * @param {boolean} bShouldReplaceList Boolean value which tells to update existing list or create a new list
   * @param {object} oResponse  Response object
   */
  let successFetchItemsData = function (bShouldReplaceList, oResponse) {
    let aItemsDataFromResponse = _getDataFromResponsePath(oResponse);

    //TODO: get excluded entities from response
    let sActiveGroup = oGroupMssContextMenuProps.getActiveGroup();
    let oActiveGroupReqInfo = oGroupMssContextMenuProps.getRequestResponseInfo(oGroupMssContextMenuProps.getActiveGroup());
    if (oActiveGroupReqInfo.entitiesToExclude) {
      CS.forEach(oActiveGroupReqInfo.entitiesToExclude, (sId) => {
        let oAttribute = aItemsDataFromResponse[sActiveGroup];
        CS.remove(oAttribute.list, {id: sId})
      });
    }
    let oSuccess = oResponse.success;
    if (bShouldReplaceList) {
      oGroupMssContextMenuProps.setGroupsData(aItemsDataFromResponse);
    } else {
      let aItemsData = oGroupMssContextMenuProps.getGroupsData() || [];
      let aGroups = CS.keys(aItemsDataFromResponse);
      CS.each(aGroups, group => {
        aItemsData[group].list = aItemsData[group].list.concat(aItemsDataFromResponse[group].list);
        oGroupMssContextMenuProps.setGroupsData(aItemsData);
      })
    }
    let aItemsData = oGroupMssContextMenuProps.getGroupsData() || [];
    let oProperty = aItemsData[sActiveGroup];
    if(oProperty.totalCount) {
      oProperty.showLoadMore = oProperty.list.length !== oProperty.totalCount;
    }
    _triggerChange();
  };

  /**
   * Function to handle any failed request api
   */
  let failureFetchItemsData = function (oResponse) {
    ExceptionLogger.log(oResponse);
    ExceptionLogger.error('failureFetchItemsData');
  };

  let _handleSearchInputManual = (sSearchString) => {
    let sGroupType = oGroupMssContextMenuProps.getActiveGroup();
    let aData = oGroupMssContextMenuProps.getGroupsData();
    let aArray = [];
    let aResponse = CS.cloneDeep(aData);
    if (!sSearchString) {
      aArray = aResponse;
    } else {
      aResponse[sGroupType].list.forEach((obj) => {
        let sObjValue = obj.label || obj.id
        if ((sObjValue.toLowerCase()).indexOf(sSearchString.toLowerCase()) > -1) {
          aArray.push(obj);
        }
      })
      if (aArray.length > 0) {
        aResponse[sGroupType].list = aArray;
      } else {
        aResponse[sGroupType].list = [];
      }
    }
    return aResponse
  };

  return {

    //Do not remove or change name
    handleSearchInput: (sSearchString) => {
      let oReqResObj = oGroupMssContextMenuProps.getRequestResponseInfo(oGroupMssContextMenuProps.getActiveGroup());
      let manualSearch = oReqResObj ? false : true;
      return manualSearch ? _handleSearchInputManual(sSearchString) : _handleSearchInputApi(sSearchString);
    },

    //Do not remove or change name
    triggerChange: () => {
      _triggerChange();
    },

    //Do not remove or change name
    setGroupsData: (aGroupsData) => {
      oGroupMssContextMenuProps.setGroupsData(aGroupsData);
    },

    //Do not remove or change name
    getGroupsData: () => {
      return oGroupMssContextMenuProps.getGroupsData()
    },

    //Do not remove or change name
    handleLoadMoreClicked: (sGroupType) => {
      _handleLoadMoreClicked(sGroupType);
    },

    //Do not remove or change name
    setGroupsDataMap: (oObj) => {
      oGroupMssContextMenuProps.setGroupsDataMap(oObj);
    },

    //Do not remove or change name
    getGroupsDataMap: (sKey) => {
      return oGroupMssContextMenuProps.getGroupsDataMap(sKey);
    },

    setActiveGroup: (sGroupType) => {
      oGroupMssContextMenuProps.setActiveGroup(sGroupType);
    },

    getActiveGroup: () => {
      return oGroupMssContextMenuProps.getActiveGroup();
    },

    resetGroupsData: () => {
      oGroupMssContextMenuProps.resetGroupsData();
    },

    getRequestResponseInfo: (sGroupType) => {
      return oGroupMssContextMenuProps.getRequestResponseInfo(sGroupType);
    }
  };

};

MicroEvent.mixin(GroupMSSContextMenuStore);

export default GroupMSSContextMenuStore;