import CS from '../../../libraries/cs';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import CommonModuleRequestMapping from '../../../commonmodule/tack/common-module-request-mapping';
import LazyTagGroupViewProps from './model/lazy-tag-group-view-props';
import ExceptionLogger from '../../../libraries/exceptionhandling/exception-logger';

let LazyTagGroupViewStore = function (oReferencedData, oCustomRequestObject, fTriggerChangeHandler) {

  //Constructor Implementation
  let oLazyTagGroupViewProps = new LazyTagGroupViewProps(oReferencedData, oCustomRequestObject);

  let _triggerChange = () => {
    fTriggerChangeHandler();
  };

  /*let _updateReferencedData = function (sTagId) {
    let oReferencedData = oLazyTagGroupViewProps.getReferencedData();
    oLazyTagGroupViewProps.setItemsDataMap(CS.keyBy(oReferencedData[sTagId].children, 'id'));
  };*/

  let _updateChildrenInReferencedTag = function (sTagGroupId, aTagValueRelevanceData) {
    let oReferencedData = oLazyTagGroupViewProps.getReferencedData();
    let oReferencedTag = oReferencedData[sTagGroupId];
    let aReferencedTagValues = oReferencedTag.children;
    let aTagValuesInProps = oLazyTagGroupViewProps.getItemsData();

    CS.forEach(aTagValueRelevanceData, function (oTagValueData) {
      let oNewTagValue = CS.find(aTagValuesInProps, {id: oTagValueData.tagId});
      if (!CS.isEmpty(oNewTagValue)) {
        aReferencedTagValues.push(oNewTagValue);
      }
    });

    oReferencedTag.children = CS.uniqBy(aReferencedTagValues, "id");
  };

  let _handleSearchClicked = (sTagGroupId, sSearchText, oExtraData) => {
    oLazyTagGroupViewProps.setItemsData([]);
    oLazyTagGroupViewProps.setItemsDataMap({});
    oLazyTagGroupViewProps.resetPaginationData();

    let oPaginationData = oLazyTagGroupViewProps.getPaginationData();
    oPaginationData.searchText = sSearchText || "";
    oPaginationData.from = 0;

    let oTagValuesRequest = {
      from: oPaginationData.from,
      size: oPaginationData.size,
      sortBy: oPaginationData.sortBy,
      sortOrder: oPaginationData.sortOrder,
      tagGroupId: sTagGroupId
    };
    CS.assign(oTagValuesRequest, oLazyTagGroupViewProps.getCustomRequestObject());
    let sEntityKey = oTagValuesRequest.entityName || "tagValues";

    let oRequestData = {
      searchColumn: oPaginationData.searchColumn,
      searchText: oPaginationData.searchText,
      entities: {[sEntityKey]: oTagValuesRequest}
    };

    let oCallbackData = {
      shouldReplaceList: true,
      isSearchApplied: !oExtraData.isInitialSearch
    }

    CS.postRequest(CommonModuleRequestMapping.GetConfigData, {}, oRequestData,
        successFetchItemsData.bind(this, oCallbackData), failureFetchItemsData, false, oExtraData);
  };

  let _handleLoadMoreClicked = function (sTagGroupId, oExtraData) {
    let oPaginationData = oLazyTagGroupViewProps.getPaginationData();
    oPaginationData.from = CS.size(oLazyTagGroupViewProps.getItemsData());

    let oTagValuesRequest = {
      from: oPaginationData.from,
      size: oPaginationData.size,
      sortBy: oPaginationData.sortBy,
      sortOrder: oPaginationData.sortOrder,
      tagGroupId: sTagGroupId
    };
    CS.assign(oTagValuesRequest, oLazyTagGroupViewProps.getCustomRequestObject());

    let sEntityKey = oTagValuesRequest.entityName || "tagValues";
    let oRequestData = {
      searchColumn: oPaginationData.searchColumn,
      searchText: oPaginationData.searchText,
      entities: {[sEntityKey]: oTagValuesRequest}
    };

    let oCallbackData = {
      shouldReplaceList: false,
      isSearchApplied: false
    };

    CS.postRequest(CommonModuleRequestMapping.GetConfigData, {}, oRequestData,
        successFetchItemsData.bind(this, oCallbackData), failureFetchItemsData, false, oExtraData);
  };

  let successFetchItemsData = function (oCallbackData, oResponse) {
    oResponse = oResponse.success;
    let aItemsDataFromResponse = !CS.isEmpty(oResponse.tagValues) ? oResponse.tagValues.list : [];

    if (oCallbackData) {
      if (oCallbackData.shouldReplaceList) {
        oLazyTagGroupViewProps.setItemsData(aItemsDataFromResponse);
      } else {
        let aItemsData = oLazyTagGroupViewProps.getItemsData() || [];
        oLazyTagGroupViewProps.setItemsData(aItemsData.concat(aItemsDataFromResponse));
      }
      oLazyTagGroupViewProps.setIsSearchApplied(oCallbackData.isSearchApplied);
    }

    oLazyTagGroupViewProps.setItemsDataMap(CS.keyBy(oLazyTagGroupViewProps.getItemsData(), 'id'));
    _triggerChange();
  };

  let failureFetchItemsData = function (oResponse) {
    ExceptionLogger.log(oResponse);
    ExceptionLogger.error('failureFetchItemsData');
  };

  let _updateChildrenInReferencedTagFromMasterReferencedTag = function (oMasterReferencedTags) {
    let oReferencedData = oLazyTagGroupViewProps.getReferencedData();
    let aTagValuesInProps = oLazyTagGroupViewProps.getItemsData();

    CS.forEach(oMasterReferencedTags, function (oMasterTag, sKey) {
      if (CS.isEmpty(oReferencedData[sKey])) {
        oReferencedData[sKey] = oMasterTag;
      }
      else {
        let oReferencedTag = oReferencedData[sKey];
        let aReferencedTagChildren = oReferencedTag.children;
        let aMasterTagChildren = oMasterTag.children;
        CS.forEach(aMasterTagChildren, function (oMasterTagChild) {
          let iFoundTagValueIndex = CS.findIndex(aReferencedTagChildren, {id: oMasterTagChild.id});
          if (iFoundTagValueIndex >= 0) {
            aReferencedTagChildren[iFoundTagValueIndex] = oMasterTagChild;
          } else {
            aReferencedTagChildren.push(oMasterTagChild);
            aTagValuesInProps.push(oMasterTagChild);
          }
        });
        oReferencedTag.children = CS.uniqBy(aReferencedTagChildren, "id");
      }
    });
  };

  return {

    //Do not remove or change name
    getReferencedData: () => {
      return oLazyTagGroupViewProps.getReferencedData();
    },

    //Do not remove or change name
    getItemsData: () => {
      return oLazyTagGroupViewProps.getItemsData();
    },

    /*setItemsData: (aItemData) => {
      oLazyTagGroupViewProps.setItemsData(aItemData);
    },*/

    //Do not remove or change name
    getItemsDataMap: () => {
      return oLazyTagGroupViewProps.getItemsDataMap();
    },

    //Do not remove or change name
    getSearchText: () => {
      let oPaginationData = oLazyTagGroupViewProps.getPaginationData();
      return oPaginationData.searchText;
    },

    //Do not remove or change name
    handleSearchClicked: (sTagGroupId, sSearchText, oExtraData) => {
      _handleSearchClicked(sTagGroupId, sSearchText, oExtraData);
    },

    //Do not remove or change name
    handleLoadMoreClicked: (sTagGroupId, oExtraData) => {
      _handleLoadMoreClicked(sTagGroupId, oExtraData);
    },

    //Do not remove or change name
    updateChildrenInReferencedTag: (sTagGroupId, aTagValueRelevanceData) => {
      _updateChildrenInReferencedTag(sTagGroupId, aTagValueRelevanceData);
    },

    getIsSearchApplied: () => {
      return oLazyTagGroupViewProps.getIsSearchApplied();
    },

    updateChildrenInReferencedTagFromMasterReferencedTag: function (oMasterReferencedTags) {
      _updateChildrenInReferencedTagFromMasterReferencedTag(oMasterReferencedTags);
    },

    /*updateReferencedData: function (sTagId) {
      _updateReferencedData(sTagId);
    },*/

  };

};

MicroEvent.mixin(LazyTagGroupViewStore);

export default LazyTagGroupViewStore;