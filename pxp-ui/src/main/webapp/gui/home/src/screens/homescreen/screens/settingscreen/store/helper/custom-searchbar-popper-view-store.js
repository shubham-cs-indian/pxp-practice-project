import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';
import CustomSearchBarPopperViewProps from "../model/custom-searchbar-popper-view-props";
import ExceptionLogger from "../../../../../../libraries/exceptionhandling/exception-logger";
import SettingScreenStore from "./../setting-screen-store";

let CustomSearchBarPopperViewStore = function (oInitializeData) {

  let oCustomSearchBarPopperViewProps = new CustomSearchBarPopperViewProps(oInitializeData);

  let _triggerChange = function () {
    CustomSearchBarPopperViewStore.prototype.trigger('custom-searchBar-changed');
  };

  /**
   * Search operation handler
   * @param {string} sSearchText search value
   * @private
   */
  let _handleConfigModuleSearchBar = (sSearchText) => {
    let oReqUrl = oCustomSearchBarPopperViewProps.getRequestUrl();
    if (oReqUrl) {
      if(!sSearchText){
        oCustomSearchBarPopperViewProps.resetSearchList();
      }else {
        let oReqBody = _getDataFromRequestFormat(sSearchText);
        CS.postRequest(oReqUrl, "", oReqBody, successFetchItemsData.bind(this, false), failureFetchItemsData);
        return
      }
    }
    let aItemData = oCustomSearchBarPopperViewProps.getClonedList();
    let aSearchList = [];
    CS.forEach(aItemData, function (item) {
      if (item.label && item.label.toLocaleLowerCase().indexOf(sSearchText) !== -1) {
        aSearchList.push(item);
      }
    });
    oCustomSearchBarPopperViewProps.setSearchItemList(aSearchList);
    _triggerChange();
  };

  /**
   * Clear Search input handling
   * @private
   */
  let _handleConfigModuleSearchClearInputClicked = () => {
    let oReqUrl = oCustomSearchBarPopperViewProps.getRequestUrl();
    if (oReqUrl) {
      let aSearchList = oCustomSearchBarPopperViewProps.getSearchItemList();
      oCustomSearchBarPopperViewProps.setSearchItemList(aSearchList);
    }
    oCustomSearchBarPopperViewProps.resetSearchList();
    _triggerChange();
  };

  /**
   * createPathFor searchBar view
   * @param {array} aPath
   * @param {array} aListOfItems
   * @param {array} aPathForApiData
   * @param {string} sParentId
   * @param {array} aSearchList
   */
  let createPathForCustomSearchBar = (aListOfItems, aPath, sParentId) => {
    CS.forEach(aListOfItems, function (item) {
      item.path = [];
      let oPathObject = {};
      item.path.push(oPathObject);
      item.parentId =(item.path[0].id === sParentId)? null : sParentId;

      if(CS.isEmpty(item.children)){
        aListOfItems.push(item);
      }
      if(item.children){
        createPathForCustomSearchBar(item.children,item.path,item.id);
      }
    })
  };

  /**
   * Success call
   * @param {boolean} bLoadMore flag for array data merge
   * @param {object} oResponse api call response
   */
  let successFetchItemsData = (bLoadMore, oResponse) => {
    let aNewList = _getDataFromResponsePath(oResponse);
    let aExistingLists = oCustomSearchBarPopperViewProps.getSearchItemList();

    if (bLoadMore) {
      aNewList = aExistingLists.concat(aNewList);
    }
    createPathForCustomSearchBar(aNewList, [], "");
    oCustomSearchBarPopperViewProps.setSearchItemList(aNewList);
    oCustomSearchBarPopperViewProps.setDialogOpen(true);
    _triggerChange();
  };

  /**
   * Failure Data call
   * @param {object} oResponse api call response
   */
  let failureFetchItemsData = (oResponse) => {
    ExceptionLogger.log(oResponse);
    ExceptionLogger.error('failureFetchItemsData');
  };

  /**
   * handle action on click of list for config searchBar view
   * @param {String} sSearchItem element id on which click is triggered
   * @param {String} sParentId parent id of the element
   */
  let _handleConfigModuleSearchBarItemClicked = (sSearchItem,sParentId,aPath) => {
    SettingScreenStore.handleConfigScreenTabClicked(aPath[0].id);
    if(aPath.length > 2){
      SettingScreenStore.handleMenuListExpandToggledNew(aPath[1].id);
    }
    SettingScreenStore.handleSettingScreenLeftNavigationTreeItemClicked(sSearchItem, sParentId);
    oCustomSearchBarPopperViewProps.setDialogOpen(false);
    oCustomSearchBarPopperViewProps.resetSearchList();
    _triggerChange();
  };

  /**
   * handle input focus click for get data
   * @param {string} sSearchText Search value string
   * @private
   */
  let _handleInputFocusClickedForOpenDialog = (sSearchText) => {
    let oReqUrl = oCustomSearchBarPopperViewProps.getRequestUrl();
    let bIsDialogOpen = oCustomSearchBarPopperViewProps.getDialogOpen();
    if(CS.isEmpty(sSearchText) && oReqUrl === undefined){
      oCustomSearchBarPopperViewProps.resetSearchList();
    }
    else if (oReqUrl && CS.isEmpty(sSearchText) && !bIsDialogOpen) {
      let oReqBody = _getDataFromRequestFormat(sSearchText);
      CS.postRequest(oReqUrl, "", oReqBody, successFetchItemsData.bind(this, false), failureFetchItemsData);
      return
    }
    else if (CS.isNotEmpty(sSearchText)) {
      let aItemData = oCustomSearchBarPopperViewProps.getClonedList();
      let aSearchList = [];
      CS.forEach(aItemData, function (item) {
        if (item.label && item.label.toLocaleLowerCase().indexOf(sSearchText) !== -1) {
          aSearchList.push(item);
        }
      });
      oCustomSearchBarPopperViewProps.setSearchItemList(aSearchList);
    }
    oCustomSearchBarPopperViewProps.setDialogOpen(true);
    _triggerChange();
  };

  /**
   * handling of outside click of popperview
   * @private
   */
  let _handleClickAwayClicked = () => {
    oCustomSearchBarPopperViewProps.setDialogOpen(false);
    oCustomSearchBarPopperViewProps.resetSearchList();
    _triggerChange();
  };

  /**
   * loadMore click handling for when bind data from api call
   * @param {string} searchText Search value string
   * @private
   */
  let _handleSearchLoadMoreClicked = (searchText) => {
    let oReqUrl = oCustomSearchBarPopperViewProps.getRequestUrl();
    if (oReqUrl) {
      let oReqBody = _getDataFromRequestFormat(searchText);
      CS.postRequest(oReqUrl, "", oReqBody, successFetchItemsData.bind(this, true), failureFetchItemsData);
    }
    _triggerChange();
  };

  /**
   * create request data for api
   * @param {string} searchText Search value string
   * @returns {{searchColumn: *, searchText: *, from: *, size: *, sortBy: *, sortOrder: *}|{searchColumn: *, searchText: *, entities: {}}}
   * @private
   */
  let _getDataFromRequestFormat = (searchText) => {
    let aLists = oCustomSearchBarPopperViewProps.getSearchItemList();
    let oPaginationData = oCustomSearchBarPopperViewProps.getPaginationData();
    let oRequestData = {};
    let oReqBody = oCustomSearchBarPopperViewProps.getRequestBody();
    let sRequestType = oCustomSearchBarPopperViewProps.getRequestType();
    let sEntityName = oCustomSearchBarPopperViewProps.getEntity();
    let bIsDialogOpen = oCustomSearchBarPopperViewProps.getDialogOpen();

    if (sRequestType === "configData") {
      oRequestData = {
        searchColumn: oPaginationData.searchColumn,
        searchText: searchText,
        entities: {}
      };

      let oFrom = (bIsDialogOpen && !searchText) ? aLists.length : oPaginationData.from ;

      let oEntityReqData = {
        from: oFrom,
        size: oPaginationData.size,
        sortBy: oPaginationData.sortBy,
        sortOrder: oPaginationData.sortOrder
      };
      oRequestData.entities[sEntityName] = oEntityReqData;

    } else {
      oRequestData = {
        searchColumn: oPaginationData.searchColumn,
        searchText: searchText,
        from: oPaginationData.from,
        size: oPaginationData.size,
        sortBy: oPaginationData.sortBy,
        sortOrder: oPaginationData.sortOrder,
      };
    }
    CS.assign(oRequestData, oReqBody);
    return oRequestData;
  };

  /**
   * create response data for api
   * @param {object} oResponse api response
   * @returns {*}
   * @private
   */
  let _getDataFromResponsePath = (oResponse) => {
    let aResponsePath = oCustomSearchBarPopperViewProps.getResponsePath();
    let oResponseFromPath = _getDataFromPath(aResponsePath, oResponse);
    return oResponseFromPath;
  };

  /**
   * get response data from provided response path
   * @param {array} aResponsePath response path
   * @param {object} oResponse response
   * @returns {*}
   * @private
   */
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

  return {

    handleConfigModuleSearchBar: function (sSearchText) {
      _handleConfigModuleSearchBar(sSearchText);
    },

    handleConfigModuleSearchClearInputClicked: function () {
      _handleConfigModuleSearchClearInputClicked();
    },

    handleInputFocusClickedForOpenDialog: function (sSearchText) {
      _handleInputFocusClickedForOpenDialog(sSearchText);
    },

    handleClickAwayClicked: function () {
      _handleClickAwayClicked();
    },

    handleConfigModuleSearchBarItemClicked: function(sSearchItem,sParentId,aPath) {
      _handleConfigModuleSearchBarItemClicked(sSearchItem,sParentId,aPath);
    },

    getIsDialogOpen: function() {
      return oCustomSearchBarPopperViewProps.getDialogOpen();
    },

    getItems: function() {
      return oCustomSearchBarPopperViewProps.getSearchItemList();
    },

    setItems: function(aList) {
      oCustomSearchBarPopperViewProps.setSearchItemList(aList);
    },

    handleSearchLoadMoreClicked: function () {
      _handleSearchLoadMoreClicked();
    },
  }
};

MicroEvent.mixin(CustomSearchBarPopperViewStore);

export default CustomSearchBarPopperViewStore;
