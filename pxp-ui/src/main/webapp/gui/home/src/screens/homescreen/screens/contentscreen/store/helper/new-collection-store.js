
import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import ContentScreenAppData from './../model/content-screen-app-data';
import CollectionViewProps from './../model/viewprops/collection-view-props';
import ScreenModeUtils from './screen-mode-utils';
import CollectionRequestMapping from '../../tack/collection-request-mapping';
import ContentScreenConstants from './../model/content-screen-constants';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import { communicator as HomeScreenCommunicator } from '../../../../store/home-screen-communicator';
import oFilterPropType from '../../../contentscreen/tack/filter-prop-type-constants';
import ContentUtils from './content-utils';
import FilterStoreFactory from './filter-store-factory';
import BreadcrumbStore from '../../../../../../commonmodule/store/helper/breadcrumb-store';
import UniqueIdentifierGenerator from './../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import AvailableEntityStore from "./available-entity-store";
import ThumbnailModeConstants from "../../../../../../commonmodule/tack/thumbnail-mode-constants";
var getTranslation = ScreenModeUtils.getTranslationDictionary;

var NewCollectionStore = (function () {

  var _triggerChange = function () {
    NewCollectionStore.trigger('new-collection-change');
  };

  var _postRequest = function (sUrl, oQueryString, oRequestData, fSuccess, fFailure) {
    return CS.postRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure);
  };

  var _handleCreateNewCollectionButtonClicked = function (oCreationData, oCallback) {
    var bIsDynamic = oCreationData.isDynamic;
    var sCollectionName = oCreationData.collectionName;
    var sId = oCreationData.id;
    var bIsPrivateMode = oCreationData.isPrivateMode;

    var oRequestPayload = {};
    if (bIsDynamic) {

      //DYNAMIC COLLECTION :
      oRequestPayload.getRequestModel = ContentUtils.getAllInstancesRequestData(oCallback.filterContext);
      let oSpecialUsecaseFilterData = AvailableEntityStore.handleSpecialUsecaseFilters(oCallback);
      CS.isNotEmpty(oSpecialUsecaseFilterData) && CS.assign(oRequestPayload.getRequestModel, oSpecialUsecaseFilterData);

      if (CS.isEmpty(oRequestPayload.getRequestModel.moduleId)) {
        return;
      }
      oCallback = oCallback || {};
      oRequestPayload.label = sCollectionName;
      oRequestPayload.parentId = -1;
      oRequestPayload.isPublic = !bIsPrivateMode;
      CS.putRequest(CollectionRequestMapping.CreateDynamicCollection, {}, oRequestPayload, successCreateCollectionCallback.bind(this, bIsDynamic, oCallback), failureCreateCollectionCallback);

    }
    else {

      //STATIC COLLECTION :
      let oActiveCollection = CollectionViewProps.getActiveCollection();
      var aSelectedContentIds = ContentUtils.getSelectedContentIds();
      if (CS.isEmpty(aSelectedContentIds)) {
        let oFilterStore = FilterStoreFactory.getFilterStore(oCallback.filterContext);
        let oFilterProps = oFilterStore.getFilterProps();
        var iTotalContentCount = oFilterProps.getTotalItemCount();
        var sAddAllLabel = getTranslation().ADD + " " + iTotalContentCount + " " + getTranslation().ITEMS;
        let sAllPagesMode = CS.isEmpty(oActiveCollection) ? "all_pages" : "collection_all_pages";
        let aCustomButtonData = [
          {
            id: "cancel",
            label: getTranslation().CANCEL,
            isFlat: true,
            handler: _createStaticCollectionAccordingToMode.bind(this, "cancel", sCollectionName, sId, bIsPrivateMode, oCallback)
          },
          {
            id: "addCustomCollection",
            label: sAddAllLabel,
            isFlat: false,
            handler: _createStaticCollectionAccordingToMode.bind(this, sAllPagesMode, sCollectionName, sId, bIsPrivateMode, oCallback)
          },
          {
            id: "createEmptyCollection",
            label: getTranslation().CREATE_EMPTY_COLLECTION,
            isFlat: false,
            handler: _createStaticCollectionAccordingToMode.bind(this, "no_content", sCollectionName, sId, bIsPrivateMode, oCallback)
          }
        ]
        CustomActionDialogStore.showCustomConfirmDialog(getTranslation().COLLECTION_TRI_STATE_PROMPT,'',aCustomButtonData);

      } else {
        _createStaticCollectionAccordingToMode("selected_content", sCollectionName, sId, bIsPrivateMode, oCallback);
      }
    }
    _handleCollectionBreadcrumbReset();
  };

  var _createStaticCollectionAccordingToMode = function (sMode, sCollectionName, sId, bIsPrivateMode, oCallbackData) {
    if(CS.isEmpty(sCollectionName)){
      sCollectionName = UniqueIdentifierGenerator.generateUntitledName();
    }
    var oRequestPayload = {};
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallbackData.filterContext)
    oRequestPayload.label = sCollectionName;
    oRequestPayload.parentId = sId || -1;
    oRequestPayload.isPublic = !bIsPrivateMode;
    if(oRequestPayload.parentId == 999){
      oRequestPayload.parentId = -1;
    }

    switch (sMode) {
      case "collection_all_pages":
        let aEntityList = ContentUtils.getAppData().getContentList();
        let aContentIdsInCollection = CS.map(aEntityList, "id");
       oRequestPayload.klassInstanceIds = aContentIdsInCollection;
       break;

      case "selected_content":
        var aSelectedContentIds = ContentUtils.getSelectedContentIds();
        oRequestPayload.klassInstanceIds = aSelectedContentIds;
        break;

      case "current_page":
        var aContentList = ContentUtils.getEntityList();
        var aContentIds = [];
        CS.forEach(aContentList, function (oContent) {
          aContentIds.push(oContent.id);
        });
        oRequestPayload.klassInstanceIds = aContentIds;
        break;

      case "all_pages":
        oRequestPayload.filterResultsToSave = oFilterStore.createFilterPostData();
        break;

      case "no_content":
        oRequestPayload.klassInstanceIds = [];
        break;

      case "cancel":
        return;
    }

    oCallbackData = oCallbackData || {};
    var bIsDynamic = false;
    CS.putRequest(CollectionRequestMapping.CreateStaticCollection, {}, oRequestPayload, successCreateCollectionCallback.bind(this, bIsDynamic, oCallbackData), failureCreateCollectionCallback);
  };

  var successCreateCollectionCallback = function (bIsDynamic, oCallbackData, oResponse) {
    if (!CS.isEmpty(oResponse.success)) {
      var oResponseData = oResponse.success;
      var sCollectionName = oResponseData.label;
      if (bIsDynamic) {
        ContentUtils.showSuccess(sCollectionName + " " + getTranslation().BOOKMARK_CREATION_SUCCESS);
      } else {
        ContentUtils.setSelectedContentIds([]); //clear any selections
        ContentUtils.showSuccess(getTranslation().COLLECTION + " " + sCollectionName + " " + getTranslation().SUCCESSFULLY_CREATED);
      }
    }
    _handleCollectionBreadcrumbReset();

    if(oCallbackData && oCallbackData.functionToExecute){
      oCallbackData.functionToExecute(oResponse);
    }
    _triggerChange();
  };

  var failureCreateCollectionCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureCreateCollectionCallback", getTranslation());
  };

  var _handleModifyCollectionClicked = function (sCollectionId, sContext, oFilterContext) {
    var aCollectionList = [];
    var oCollection = {};
    if(sContext == "dynamicCollection"){
      aCollectionList = ContentScreenAppData.getDynamicCollectionList();
      oCollection = CS.find(aCollectionList, {id: sCollectionId});
      let oCallBackData = {isModified: true};
      oCallBackData.filterContext = oFilterContext;
      _modifyDynamicCollection(oCollection, oCallBackData);
    } else {
      var oStaticCollectionMap = ContentScreenAppData.getStaticCollectionMap();
      var iActiveLevel = ContentScreenAppData.getActiveStaticCollectionLevel();
      aCollectionList = (oStaticCollectionMap[iActiveLevel]) ? oStaticCollectionMap[iActiveLevel].collectionList : [];
      oCollection = CS.find(aCollectionList, {id: sCollectionId});
      _modifyStaticCollection(oCollection, [], oFilterContext);
    }
    _handleCollectionBreadcrumbReset();
  };

  var _modifyDynamicCollection = function (oCollection, oCallBackData) {
    var oRequestPayload = {};
    oRequestPayload.id = oCollection.id;
    oRequestPayload.getRequestModel = ContentUtils.createFilterPostData(null, oCallBackData.filterContext);
    if(CS.isEmpty(oRequestPayload.getRequestModel.moduleId)) {
      return;
    }
    let oSpecialUsecaseFilterData = AvailableEntityStore.handleSpecialUsecaseFilters(oCallBackData);
    CS.isNotEmpty(oSpecialUsecaseFilterData) && CS.assign(oRequestPayload.getRequestModel, oSpecialUsecaseFilterData);

    oRequestPayload.label = oCollection.label;
    oRequestPayload.parentId = -1;
    oRequestPayload.type = oCollection.type;
    oRequestPayload.isPublic = oCollection.isPublic;
    CS.postRequest(CollectionRequestMapping.SaveDynamicCollection, {}, oRequestPayload, successSaveDynamicCollectionCallback.bind(this, oCallBackData), failureSaveDynamicCollectionCallback);
  };

  var _modifyStaticCollection = function (oCollection, aToAddEntityIdsFromDrop, oFilterContext) {
    var aSelectedContentIds = ContentUtils.getSelectedContentIds();
    if (CS.isEmpty(aSelectedContentIds) && CS.isEmpty(aToAddEntityIdsFromDrop)) {
      //Not reachable code - Shashank Check again
      CustomActionDialogStore.showTriActionDialog( getTranslation().COLLECTION_TRI_STATE_PROMPT,
          _modifyStaticCollectionAccordingToMode.bind(this, "current_page", oCollection, "", [], null, oFilterContext),
          _modifyStaticCollectionAccordingToMode.bind(this, "all_pages", oCollection, "", [], null, oFilterContext),
          _modifyStaticCollectionAccordingToMode.bind(this, "cancel", oCollection, "", [], null, oFilterContext)
      ).set('labels', {save: getTranslation().CURRENT_PAGE, discard: getTranslation().ALL_PAGES, cancel: getTranslation().CANCEL});
    } else {
      _modifyStaticCollectionAccordingToMode("selected_content", oCollection, "", aToAddEntityIdsFromDrop, null, oFilterContext);
    }
  };

  var _handleSingleContentAddToStaticCollectionClicked = function (sCollectionId, sContentId, oModel, oFilterContext) {
    var aCollectionList = ContentScreenAppData.getStaticCollectionList();
    var oCollection = CS.find(aCollectionList, {id : sCollectionId});
    if (oCollection.type === "staticCollection") {
      _modifyStaticCollectionAccordingToMode("individual_content", oCollection, sContentId, [], oModel, oFilterContext);
    }
  };

  var _modifyStaticCollectionAccordingToMode = function (sMode, oCollection, sContentId, aSelectedContentIds, oModel, oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);

    var oRequestPayload = oFilterStore.createFilterPostData();
    oRequestPayload.id = oCollection.id;
    oRequestPayload.label = oCollection.label;
    oRequestPayload.parentId = -1;
    oRequestPayload.isPublic = oCollection.isPublic;
    oRequestPayload.removedKlassInstanceIds = [];

    var aContentList = ContentUtils.getEntityList();
    switch (sMode) {

      case "selected_content":
        if (CS.isNotEmpty(aSelectedContentIds)) {
          aContentList = ContentUtils.getAppData().getAvailableEntities();
        }else {
          aSelectedContentIds = ContentUtils.getSelectedContentIds();
        }

        var aSelectedContentIdAndType = [];
        CS.forEach(aSelectedContentIds, function (sId) {
          var oFoundContent = CS.find(aContentList, {id: sId});
          aSelectedContentIdAndType.push({id: oFoundContent.id, type: oFoundContent.baseType});
        });
        oRequestPayload.addedKlassInstanceIds = aSelectedContentIdAndType;
        break;

      case "current_page":
        var aContentIds = [];
        CS.forEach(aContentList, function (oContent) {
          aContentIds.push({id:oContent.id, type:oContent.baseType});
        });
        oRequestPayload.addedKlassInstanceIds = aContentIds;
        break;

      case "all_pages":
        oRequestPayload.filterResultsToSave = oFilterStore.createFilterPostData();
        break;

      case "individual_content":
        var oFoundContent;
        if(oModel && oModel.properties) {
          oFoundContent = oModel.properties["entity"];
        }

        if(CS.isEmpty(oFoundContent)) {
          oFoundContent = CS.find(aContentList, {id: sContentId});
        }

        if(!CS.isEmpty(oFoundContent)) {
          oRequestPayload.addedKlassInstanceIds = [{id:oFoundContent.id, type:oFoundContent.baseType}];
        }
        break;

      case "cancel":
        return;
    }

    CS.postRequest(CollectionRequestMapping.SaveStaticCollection, {}, oRequestPayload, successModifyStaticCollectionCallback, failureModifyStaticCollectionCallback);
  };

  var successModifyStaticCollectionCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.success)) {
      var oResponseData = oResponse.success;
      ContentUtils.setSelectedContentIds([]); //clear any selections
      ContentUtils.showSuccess(getTranslation().SUCCESSFULLY_ADDED_TO_COLLECTION);
    }
    _handleCollectionBreadcrumbReset();
    _triggerChange();
  };

  var failureModifyStaticCollectionCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureModifyStaticCollectionCallback", getTranslation());
  };

  var successGetDynamicCollectionsCallBack = function (oResponse) {
    if (oResponse.success) {
      var aDynamicCollectionList = oResponse.success;
      ContentScreenAppData.setDynamicCollectionList(aDynamicCollectionList);
      var oBreadCrumb = CollectionViewProps.getBreadCrumb();
      var oBreadCrumbItem = {};
      oBreadCrumbItem.id = -1;
      oBreadCrumbItem.label = getTranslation().BOOKMARK;
      oBreadCrumb.treePath.push(oBreadCrumbItem);
    }
    _triggerChange();
  };

  var _getDynamicCollectionsList = function () {

    CS.getRequest(CollectionRequestMapping.GetDynamicCollections, {}, successGetDynamicCollectionsCallBack, failureGetDynamicCollectionsCallback);
  };

  var failureGetDynamicCollectionsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureGetDynamicCollectionsCallback", getTranslation());
  };

  var _getStaticCollectionsList = function (oCallBackData, oExtraData) {
    oCallBackData = oCallBackData || {};
    oExtraData = oExtraData || {};
    CS.getRequest(CollectionRequestMapping.GetStaticCollections, {}, successGetStaticCollectionsCallBack.bind(this, oCallBackData), failureGetStaticCollectionsCallback, oExtraData);
  };

  var successGetStaticCollectionsCallBack = function (oCallbackData, oResponse) {
    if(oResponse.success){
      var aStaticCollectionList = oResponse.success;
      ContentScreenAppData.setStaticCollectionList(aStaticCollectionList);

      if(!oCallbackData.doNotSetBreadCrumb) {
        var oBreadCrumb = CollectionViewProps.getBreadCrumb();
        var oFound = CS.find(oBreadCrumb.treePath, {id: -1});
        if (CS.isEmpty(oFound)) {
          var oBreadCrumbItem = {};
          oBreadCrumbItem.id = -1;
          oBreadCrumbItem.label = getTranslation().COLLECTION_LABEL;
          oBreadCrumb.treePath.push(oBreadCrumbItem);
        }
      }

      var oStaticCollectionMap = {};
      var iLevel = 0;
      oStaticCollectionMap[iLevel] = {
        parentId: -1,
        collectionList: aStaticCollectionList
      };
      ContentScreenAppData.setStaticCollectionMap(oStaticCollectionMap);
      ContentScreenAppData.setActiveStaticCollectionLevel(iLevel);
    }

    if(oCallbackData && oCallbackData.functionToExecute){
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var failureGetStaticCollectionsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureGetStaticCollectionsCallback", getTranslation());
  };

  /**
   * @function _handleCollectionSelected
   * @param sId
   * @param sContext
   * @param oCallbackData - we get filter context in oCallback data.
   * @param oExtraData
   * @returns {*}
   * @private
   */
  var _handleCollectionSelected = function (oCollection, sContext, oCallbackData, oExtraData) {
    let oFilterContext = oCallbackData.filterContext;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    if(oExtraData && oExtraData.sPreviouslySelectedModuleId) {
      oFilterStore.setModuleSelectedById(oExtraData.sPreviouslySelectedModuleId);
    }

    ContentUtils.setSelectedContentIds([]); //clear any selections
    ContentUtils.clearSelectedAvailableEntities();
    ContentUtils.setActiveEntity({}); //for cases when entity is already opened and a collection is opened

    ContentUtils.setViewMode(ContentUtils.getDefaultViewMode());

    var oActiveCollection = CollectionViewProps.getActiveCollection();
    sContext = sContext || oActiveCollection.type;
    if(sContext == "staticCollection"){
      _handleCollectionBreadcrumbReset();
      return _handleStaticCollectionSelected(oCollection, oCallbackData, oExtraData);
    } else {
      CollectionViewProps.setBreadCrumbPath([]);
      return _handleDynamicCollectionSelected(oCollection, oCallbackData);
    }
  };

  var failureGetStaticCollectionChildrenCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureGetStaticCollectionChildrenCallback", getTranslation());
  };

  var successGetStaticCollectionChildrenCallback = function (sId, oResponse) {
    var aCollections = oResponse.success;
    var oBreadCrumb = CollectionViewProps.getBreadCrumb();
    var iLevel = ContentScreenAppData.getActiveStaticCollectionLevel();
    var oStaticCollectionMap = ContentScreenAppData.getStaticCollectionMap();
    var aCollectionList = (oStaticCollectionMap[iLevel]) ? oStaticCollectionMap[iLevel].collectionList : [];
    var oCollection = CS.find(aCollectionList, {id: sId});

    var iSize = CS.size(oStaticCollectionMap);
    ContentScreenAppData.setActiveStaticCollectionLevel(iSize);
    oStaticCollectionMap[iSize] = {
      parentId: sId,
      collectionList: aCollections
    };

    var oBreadCrumbItem = {};
    oBreadCrumbItem.id = oCollection.id;
    oBreadCrumbItem.label = oCollection.label;
    oBreadCrumb.treePath.push(oBreadCrumbItem);

    ContentScreenAppData.setStaticCollectionList(aCollections);
    _triggerChange();
  };

  var _handleNextCollectionClicked = function (sId) {
    var oCollectionData = {
      id: sId
    };

    CS.getRequest(CollectionRequestMapping.GetStaticCollectionsTree, oCollectionData,
        successGetStaticCollectionChildrenCallback.bind(this, sId), failureGetStaticCollectionChildrenCallback);
  };

  var _handleCollectionBreadcrumbReset = function () {
    CollectionViewProps.setBreadCrumbPath([]);
    ContentScreenAppData.setActiveStaticCollectionLevel(0);
    ContentScreenAppData.setStaticCollectionMap({});
  };

  var _handleCollectionItemVisibilityModeChanged = function(sId, bIsDynamic, oFilterContext){
    var aCollectionList = [];
    var oCollection = {};
    var sUrl = "";

    var oRequestPayload = {};

    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);

    if(bIsDynamic){
      aCollectionList = ContentScreenAppData.getDynamicCollectionList();
      oCollection = CS.find(aCollectionList, {id: sId});
      sUrl = CollectionRequestMapping.SaveDynamicCollection;
      //TODO: Temporary fix.
      oRequestPayload.getRequestModel = null/*ContentUtils.createFilterPostData(null, oFilterContext)*/;
    }else{
      aCollectionList = ContentScreenAppData.getStaticCollectionList();
      oCollection = CS.find(aCollectionList, {id: sId});
      sUrl = CollectionRequestMapping.SaveStaticCollection;
      oRequestPayload = oFilterStore.createFilterPostData();
      oRequestPayload.addedKlassInstanceIds = [];
      oRequestPayload.removedKlassInstanceIds = [];
    }

    var bIsPublic = oCollection.isPublic;

    oRequestPayload.id = oCollection.id;
    oRequestPayload.label = oCollection.label;
    oRequestPayload.parentId = -1;
    oRequestPayload.isPublic = !bIsPublic;

    CS.postRequest(sUrl, {}, oRequestPayload, successCollectionItemVisibilityModeChanged.bind(this, bIsDynamic), failureCollectionItemVisibilityModeChanged);
  };


  var successCollectionItemVisibilityModeChanged = function(bIsDynamic, oResponse){
    oResponse = oResponse.success;
    var aCollectionList = (bIsDynamic) ? ContentScreenAppData.getDynamicCollectionList() : ContentScreenAppData.getStaticCollectionList();
    var oCollection = CS.find(aCollectionList, {id: oResponse.id});
    oCollection.isPublic = oResponse.isPublic;

    /** Update hierarchy tree after updating collection list if hierarchy mode is selected **/
    var oScreenProps = ContentUtils.getComponentProps().screen;
    if(ContentUtils.getSelectedHierarchyContext()) {
      var oContentHierarchy = oScreenProps.getContentHierarchyTree();
      let oHierarchyToUpdate = CS.find(oContentHierarchy.children, {id: oResponse.id});
      oHierarchyToUpdate.isPublic = oCollection.isPublic;
    }

    let sSuccessMessage = bIsDynamic ? 'BOOKMARK_SUCCESSFULLY_SAVED' : 'COLLECTION_SUCCESSFULLY_SAVED';

    ContentUtils.showSuccess(getTranslation()[sSuccessMessage]);
    _triggerChange();
  };

  var failureCollectionItemVisibilityModeChanged = function(oResponse){
    ContentUtils.failureCallback(oResponse, "failureCollectionItemVisibilityModeChanged", getTranslation());
  };

  var _handleStaticCollectionRootBreadCrumbClicked = function () {
    CollectionViewProps.setBreadCrumbPath([]);
    ContentScreenAppData.setActiveStaticCollectionLevel(0);

    var oStaticCollectionMap = ContentScreenAppData.getStaticCollectionMap();
    CS.forEach(oStaticCollectionMap, function (oValue, iLevel) {
      if(iLevel > 0){
        delete oStaticCollectionMap[iLevel];
      }
    });

    var oBreadCrumb = CollectionViewProps.getBreadCrumb();
    var oBreadCrumbItem = {};
    oBreadCrumbItem.id = -1;
    oBreadCrumbItem.label = getTranslation().COLLECTION_LABEL;
    oBreadCrumb.treePath.push(oBreadCrumbItem);
  };

  var _handleStaticCollectionBreadCrumbItemClicked = function (oItem) {
    var oBreadcrumb = CollectionViewProps.getBreadCrumb();
    var iIndex = CS.findIndex(oBreadcrumb.treePath, {id: oItem.id});
    oBreadcrumb.treePath.splice(iIndex + 1);

    var oStaticCollectionMap = ContentScreenAppData.getStaticCollectionMap();
    CS.forEach(oStaticCollectionMap, function (oValue, iLevel) {
      if(iLevel > iIndex){
        delete oStaticCollectionMap[iLevel];
      }
    });
    ContentScreenAppData.setActiveStaticCollectionLevel(iIndex);
    ContentScreenAppData.setStaticCollectionList(oStaticCollectionMap[iIndex].collectionList);

    _triggerChange();
  };

  /**
   *@function _handleDynamicCollectionSelected
   * @param oBookmark
   * @param oCallback - we get filter context in oCallback data.
   * @private
   */
  var _handleDynamicCollectionSelected = function (oBookmark, oCallback) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallback.filterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    let oComponentProps = ContentUtils.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    var oCallbackData = {};
    CS.assign(oCallbackData, oCallback);
    let oAjaxExtraData = {
      URL: CollectionRequestMapping.GetDynamicCollection,
      requestData: {},
      postData: {
        bookmarkId: oBookmark.id,
        isFilterDataRequired: oFilterProps.getIsFilterInformationRequired(),
        xrayEnabled: oScreenProps.getThumbnailMode() === ThumbnailModeConstants.XRAY,
      },
    };

    let sHelpScreenId;
    let sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION;

    let aPayloadData = [oCallbackData, oAjaxExtraData];
    oCallbackData.bookmark = oBookmark;
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(oBookmark.id, "", sType, sHelpScreenId, oCallback.filterContext, oAjaxExtraData, "", aPayloadData, _dynamicCollectionBreadcrumbFunctionToExecute);

    return _handleDynamicCollectionSelectedCall(oCallbackData, oAjaxExtraData);
  };

  var _dynamicCollectionBreadcrumbFunctionToExecute = function (oCallbackData, oAjaxExtraData, extraInformation) {
    let oFilterProps = ContentUtils.getFilterProps(oCallbackData.filterContext);
    if(oFilterProps.getShowDetails()){
      oAjaxExtraData.postData.isFilterDataRequired = true;
    }
    _handleDynamicCollectionSelectedCall(oCallbackData, oAjaxExtraData, extraInformation);
  }

  var _handleDynamicCollectionSelectedCall = function (oCallbackData, oAjaxExtraData, extraInformation = {}) {
    CS.postRequest(oAjaxExtraData.URL, oAjaxExtraData.requestData, oAjaxExtraData.postData, successDynamicCollectionSelectedCallback.bind(this, CS.merge(oCallbackData, extraInformation)), failureDynamicCollectionSelectedCallback);
  };

  /**
   * @function - successDynamicCollectionSelectedCallback
   * @param oCallbackData -  we get filter context in oCallback data.
   * @param oResponse
   */
  var successDynamicCollectionSelectedCallback = function (oCallbackData, oResponse) {
    let oResponseData = oResponse.success;
    let oComponentProps = ContentUtils.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oActiveCollection = {};
    oActiveCollection.id = oCallbackData.bookmark.id;
    oActiveCollection.label = oCallbackData.bookmark.label;
    oActiveCollection.type = oCallbackData.bookmark.type;
    oActiveCollection.isPublic = oCallbackData.bookmark.isPublic;
    oActiveCollection.createdBy = oCallbackData.bookmark.createdBy;
    oActiveCollection.isDirty = oCallbackData.bookmark.isDirty;
    oScreenProps.setFunctionalPermission(oResponseData.functionPermission);
    /** Don't change the sequence of Breadcrumb functions.(oCallbackData.breadCrumbFunction reset the props of "Collection" when is open from another Collection)**/

    oCallbackData.breadCrumbData.label = "Bookmarks : " + oActiveCollection.label;
    if (oActiveCollection.createdBy === ContentUtils.getCurrentUser().id) {
      oCallbackData.breadCrumbData.iconClassName = oActiveCollection.isPublic ? "visibilityModeIcon" : "visibilityModeIcon privateMode";
      oCallbackData.breadCrumbData.iconTooltipLabel = oActiveCollection.isPublic ? getTranslation().COLLECTION_PUBLIC_MODE : getTranslation().COLLECTION_PRIVATE_MODE;
    }
    BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);
    CollectionViewProps.setActiveCollection(oActiveCollection); //to save the entire success object??

    /** Don't change the sequence of below statement : active collection needs to be set before executing this **/
    let oFilterContext = oCallbackData.filterContext;
    ContentUtils.performCommonSuccessOperations(oResponseData, {filterContext: oFilterContext});
    var oAppliedFilterData = oResponseData.getRequestModel;
    let sClickedTaxonomyId = CS.isNull(oAppliedFilterData.clickedTaxonomyId) ? "" : oAppliedFilterData.clickedTaxonomyId;
    /**
     * Set pagination size
     * remaining pagination data has been set from performCommonSuccessOperations function.
     */
    let oFilterProps = ContentUtils.getFilterProps(oCallbackData.filterContext);
    oFilterProps.setPaginationSize(oAppliedFilterData.size);
    oFilterProps.setSelectedTypes(oAppliedFilterData.selectedTypes);
    oFilterProps.setSelectedTaxonomyIds(oAppliedFilterData.selectedTaxonomyIds);
    oFilterProps.setSearchText(oAppliedFilterData.allSearch);
    ContentUtils.setSelectedContentIds([]);

    _setAppliedFiltersOfDynamicCollection(oResponseData, oFilterProps, oCallbackData);
    if(oFilterProps.getIsFilterInformationRequired()){
      let oFilterStore = FilterStoreFactory.getFilterStore(oCallbackData.filterContext);
      oFilterStore.prepareAndUpdateFilterAndSortModel({filterData : oResponseData.filterData}, oCallbackData);
    }
    /**Set module id for bookmark **/
    ContentUtils.setBookmarkModuleId(oAppliedFilterData.moduleId);
    HomeScreenCommunicator.disablePhysicalCatalog(true);
    _triggerChange();
  };

  /***
   * This method apply the selected filters of saved bookmark.
   * @param oResponseData
   * @param oFilterProps
   * @param oCallbackData
   */
  let _setAppliedFiltersOfDynamicCollection = function (oResponseData, oFilterProps, oCallbackData) {

    var oAppliedFilterData = oResponseData.getRequestModel;
    //attributes
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallbackData.filterContext);
    let aAppliedFilters = [];
    let oReferencedAttributes = oResponseData.referencedAttributes;
    let aAttributes = oAppliedFilterData.attributes;
    CS.forEach(aAttributes, function (oAttribute) {
      let oAppliedAttribute = oFilterStore.getDefaultAppliedFilterData(oAttribute);
      if(oAttribute.advancedSearchFilter){
        oFilterProps.setAdvancedFilterAppliedStatus(true);
        oAppliedAttribute.advancedSearchFilter = true;
      }

      let oReferencedAttribute = oReferencedAttributes[oAttribute.id];
      oAppliedAttribute.label = oReferencedAttribute.label;
      oAppliedAttribute.iconKey = oReferencedAttribute.iconKey;

      CS.forEach(oAttribute.mandatory, function (oChild) {
        oChild.label = oChild.value;
        oAppliedAttribute.children.push(oChild);
      });
      aAppliedFilters.push(oAppliedAttribute);
    });

    //tags
    let oReferencedTags = oResponseData.referencedTags;
    let aTags = oAppliedFilterData.tags;
    CS.forEach(aTags, function (oTag) {
      let oReferencedTag = oReferencedTags[oTag.id];
      let oAppliedTag = oFilterStore.getDefaultAppliedFilterData(oTag);
      oAppliedTag.label = oReferencedTag.label;
      oAppliedTag.iconKey = oReferencedTag.iconKey;
      if(oTag.advancedSearchFilter){
        oFilterProps.setAdvancedFilterAppliedStatus(true);
        oAppliedTag.advancedSearchFilter = true;
      }
      CS.forEach(oTag.mandatory, function (oChild) {
        let oMasterChild = CS.find(oReferencedTag.children, {id: oChild.id}) || {};
        oChild.label = oMasterChild.label;
        oChild.iconKey = oMasterChild.iconKey;
        oAppliedTag.children.push(oChild);

      });

      aAppliedFilters.push(oAppliedTag);
    });

    //special usecase filters
    let aSpecialUsecaseFilters = oAppliedFilterData.specialUsecaseFilters;
    CS.forEach(aSpecialUsecaseFilters, function (oSpecialUsecaseFilter) {
      let aAppliedValues = oSpecialUsecaseFilter.appliedValues;
      let sSpecialUsecaseFilterId = oSpecialUsecaseFilter.id;
      let sLabel = "";
      let sId = "";
      let aChildren = [];
      switch(sSpecialUsecaseFilterId){
        case "assetExpiry":
          sId = ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY;
          sLabel = getTranslation().ASSET_EXPIRY;
          CS.forEach(aAppliedValues, function(childId){
            if(childId === "expired"){
              aChildren.push({
                id: "expiredAsset",
                type: "exact",
                baseType: "com.cs.runtime.interactor.model.klassinstance.filter.FilterValueMatchModel",
                label: getTranslation().EXPIRED_ASSET
              });
            }
            if(childId === "nonExpired"){
              aChildren.push({
                id: "nonExpiredAsset",
                type: "exact",
                baseType: "com.cs.runtime.interactor.model.klassinstance.filter.FilterValueMatchModel",
                label: getTranslation().NON_EXPIRED_ASSET
              });
            }
          });

          break;
        case "colorVoilation":
          sId = sSpecialUsecaseFilterId;
          sLabel = getTranslation().RULE_VIOLATIONS;
          CS.forEach(aAppliedValues, function(childId){
            switch(childId){
              case "red":
                aChildren.push({
                  id: "red",
                  type: "exact",
                  baseType: "com.cs.runtime.interactor.model.klassinstance.filter.FilterValueMatchModel",
                  label: getTranslation().RED
                });
                break;
              case "orange":
                aChildren.push({
                  id: "orange",
                  type: "exact",
                  baseType: "com.cs.runtime.interactor.model.klassinstance.filter.FilterValueMatchModel",
                  label: getTranslation().ORANGE
                });
                break;
              case "yellow":
                aChildren.push({
                  id: "yellow",
                  type: "exact",
                  baseType: "com.cs.runtime.interactor.model.klassinstance.filter.FilterValueMatchModel",
                  label: getTranslation().YELLOW
                });
                break;
              case "green":
                aChildren.push({
                  id: "green",
                  type: "exact",
                  baseType: "com.cs.runtime.interactor.model.klassinstance.filter.FilterValueMatchModel",
                  label: getTranslation().GREEN
                });
                break;
            }
          });
          break;
      }
      let oAppliedSpecialFilter = {
        "id": sId,
        "type": oSpecialUsecaseFilter.type,
        "label": sLabel,
        "children": aChildren
      }
      aAppliedFilters.push(oAppliedSpecialFilter);
    });
    oFilterProps.setAppliedFilters(aAppliedFilters);
  };

  var failureDynamicCollectionSelectedCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureDynamicCollectionSelectedCallback", getTranslation());
  };

  /**
   * @function _handleStaticCollectionSelected
   * @param sId
   * @param oCallbackData - We get filter context in oCallback data.
   * @param oExtraData
   * @returns {*}
   * @private
   */
  var _handleStaticCollectionSelected = function (oCollection, oCallbackData, oExtraData = {}) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    oCallbackData = oCallbackData || {};
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallbackData.filterContext);
    //FilterStore should always pick FilterPropsForCollection irrespective of Active Screen
    let oRequestPayload = oFilterStore.createGetAllInstancesData();
    let oSpecialUsecaseFilterData = AvailableEntityStore.handleSpecialUsecaseFilters(oCallbackData);
    CS.isNotEmpty(oSpecialUsecaseFilterData) && CS.assign(oRequestPayload, oSpecialUsecaseFilterData);
    oRequestPayload.moduleId = "allmodule";
    oRequestPayload.collectionId = oCollection.id;

    oRequestPayload.xrayEnabled = oScreenProps.getThumbnailMode() === ThumbnailModeConstants.XRAY;
    let oXRayData = ContentUtils.getXRayPostData();
    if (!CS.isEmpty(oXRayData)) {
      CS.assign(oRequestPayload, oXRayData);
    }

    /** Required to handle collection pagination(Next/Previous) */
    if (!CS.isEmpty(oExtraData.postData)) {
      CS.assign(oRequestPayload, oExtraData.postData);
      if (oExtraData.postData) {
        let { from, size } = oExtraData.postData;
        oCallbackData.paginationData = { size, from };
      }
    }

    let oAjaxExtraData = {
      URL: CollectionRequestMapping.GetStaticCollection,
      requestData: {},
      postData: oRequestPayload,
    };

    let sHelpScreenId;
    let sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION;
    let aPayloadData = [oCallbackData, oAjaxExtraData];
    oCallbackData.collection = oCollection;
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(oCollection.id, "", sType, sHelpScreenId, oCallbackData.filterContext, oAjaxExtraData, "", aPayloadData, _staticCollectionBreadcrumbFunctionToExecute);

    return _handleStaticCollectionSelectedCall(oCallbackData, oAjaxExtraData);
  };

  var _staticCollectionBreadcrumbFunctionToExecute = function (oCallbackData, oAjaxExtraData, extraInformation) {
    let oFilterProps = ContentUtils.getFilterProps(oCallbackData.filterContext);
    if(oFilterProps.getShowDetails()){
      oAjaxExtraData.postData.isFilterDataRequired = true;
    }
    _handleStaticCollectionSelectedCall(oCallbackData, oAjaxExtraData, extraInformation);
  }

  /**
   * @function _handleStaticCollectionSelectedCall
   * @param oCallbackData - We get filterContext in oCallbackData.
   * @param oAjaxExtraData
   * @returns {*}
   * @private
   */
  var _handleStaticCollectionSelectedCall = function (oCallbackData, oAjaxExtraData) {
    return CS.customPostRequest(
        RequestMapping.getRequestUrl(oAjaxExtraData.URL, oAjaxExtraData.requestData), JSON.stringify(oAjaxExtraData.postData),
        successStaticCollectionSelectedCallback.bind(this, oCallbackData),
        failureStaticCollectionSelectedCallback
    );
  };

  var successStaticCollectionSelectedCallback = function (oCallbackData, oResponse) {
    if (!CS.isEmpty(oResponse.success)) {
      let oFilterContext = oCallbackData.filterContext;
      let oComponentProps = ContentUtils.getComponentProps();
      var oResponseData = oResponse.success;
      let oScreenProps = oComponentProps.screen;

      var oActiveCollection = {};
      oActiveCollection.id = oCallbackData.collection.id;
      oActiveCollection.label = oCallbackData.collection.label;
      oActiveCollection.type = oCallbackData.collection.type;
      oActiveCollection.isPublic = oCallbackData.collection.isPublic;
      oActiveCollection.createdBy = oCallbackData.collection.createdBy;
      oActiveCollection.parentId = oCallbackData.collection.parentId;

      oScreenProps.setFunctionalPermission(oResponseData.functionPermission);

      /**
       * When add/remove entity from Collection quick list,
       * should not add "Collection" breadcrumbNode in case of quick list.
       */
      if (!oCallbackData.doNotAddBreadcrumb) {
        /** Don't change the sequence of Breadcrumb functions.(oCallbackData.breadCrumbFunction reset the props of "Collection" when is open from another Collection)**/
        oCallbackData.breadCrumbData.label = "Collection : " + oActiveCollection.label;
        if (oActiveCollection.createdBy === ContentUtils.getCurrentUser().id) {
          oCallbackData.breadCrumbData.iconClassName = oActiveCollection.isPublic ? "visibilityModeIcon" : "visibilityModeIcon privateMode";
          oCallbackData.breadCrumbData.iconTooltipLabel = oActiveCollection.isPublic ? getTranslation().COLLECTION_PUBLIC_MODE : getTranslation().COLLECTION_PRIVATE_MODE;
        }
        BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);
      }

      CollectionViewProps.setActiveCollection(oActiveCollection);

      let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
      if(oFilterProps.getIsFilterInformationRequired()){
        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.prepareAndUpdateFilterAndSortModel({filterData : oResponseData.filterData}, oCallbackData, true);
      }

      let oExtraData = {
        filterContext: oFilterContext,
        paginationData: oCallbackData.paginationData,
        isMergeOldReferencedAssetList: oCallbackData.isCollectionQuickListOpened,
      };
      ContentUtils.performCommonSuccessOperations(oResponseData, oExtraData);
      ContentUtils.setSelectedContentIds([]);
      let oReferencedTags = oScreenProps.getReferencedTags();
      let oReferencedAttributes = oScreenProps.getReferencedAttributes();
      CS.assign(oReferencedTags, oResponseData.referencedTags);
      CS.assign(oReferencedAttributes, oResponseData.referencedAttributes);
      let oConfigDetails = {
        referencedAttributes: oReferencedAttributes,
        referencedTags: oReferencedTags
      };
      ContentUtils.setLoadedPropertiesFromConfigDetails(oConfigDetails);

      ContentUtils.prepareConcatenatedAttributeExpressionListForProducts(oResponseData.children,oReferencedAttributes,oReferencedTags, oScreenProps.getReferencedElements());
    }

    //xray view's concatinated attribute expressionList is prepared using below function

    if(oCallbackData && oCallbackData.functionToExecute){
      oCallbackData.functionToExecute();
    }
    HomeScreenCommunicator.disablePhysicalCatalog(true);
    _triggerChange();
  };

  var failureStaticCollectionSelectedCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureStaticCollectionSelectedCallback", getTranslation());
  };

  var _saveDynamicCollection = function (oCallBackData, oCollection) {
    var oRequestPayload = {};
    oRequestPayload.id = oCollection.id;

    oRequestPayload.getRequestModel = ContentUtils.getAllInstancesRequestData(oCallBackData.filterContext);
    let oSpecialUsecaseFilterData = AvailableEntityStore.handleSpecialUsecaseFilters(oCallBackData);
    CS.isNotEmpty(oSpecialUsecaseFilterData) && CS.assign(oRequestPayload.getRequestModel, oSpecialUsecaseFilterData);

    if(CS.isEmpty(oRequestPayload.getRequestModel.moduleId)){
      return;
    }
    oRequestPayload.label = oCollection.label;
    oRequestPayload.parentId = -1;
    oRequestPayload.type = oCollection.type;
    oRequestPayload.saveComment = oCollection.saveComment;

    if(oCollection.isPublic != null){
      oRequestPayload.isPublic = oCollection.isPublic;
    }

    let oCallbackDataToUpdateBreadCrumb = {};
    let sLabel = getTranslation().BOOKMARK + " : " + oCollection.label;
    let sHelpScreenId;
    let sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION;
    oCallbackDataToUpdateBreadCrumb.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(oCollection.id, sLabel, sType, sHelpScreenId, oCallBackData.filterContext);

    if (oCollection.createdBy === ContentUtils.getCurrentUser().id) {
      oCallbackDataToUpdateBreadCrumb.breadCrumbData.iconClassName = oCollection.isPublic ? "visibilityModeIcon" : "visibilityModeIcon privateMode";
      oCallbackDataToUpdateBreadCrumb.breadCrumbData.iconTooltipLabel = oCollection.isPublic ? getTranslation().COLLECTION_PUBLIC_MODE : getTranslation().COLLECTION_PRIVATE_MODE;
    }


    oCallBackData.updateBreadCrumb = function(oCallback) {
      BreadcrumbStore.addNewBreadCrumbItem(oCallback.breadCrumbData);
    };
    oCallBackData.callbackDataToUpdateBreadCrumb = oCallbackDataToUpdateBreadCrumb;

    /**
     * Reset pagination data before save bookmark.
     */
    let oFilterContext = oCallBackData.filterContext;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.resetPaginationDataByContext();

    CS.postRequest(CollectionRequestMapping.SaveDynamicCollection, {}, oRequestPayload,
        successSaveDynamicCollectionCallback.bind(this, oCallBackData), failureSaveDynamicCollectionCallback);
  };

  var successSaveDynamicCollectionCallback = function (oCallBackData, oResponse) {
    if (!CS.isEmpty(oResponse.success)) {
      var oResponseData = oResponse.success;
      let oActiveCollection = {};

      if (oCallBackData.isModified) {
        oActiveCollection = CollectionViewProps.getActiveCollection();
        if (!CS.isEmpty(oActiveCollection)) { //to avoid adding isDirty key when there is no active collection
          oActiveCollection.isDirty = false;
          delete oActiveCollection.saveComment;
        }
      } else {
        oActiveCollection.id = oResponseData.id;
        oActiveCollection.label = oResponseData.label;
        oActiveCollection.type = oResponseData.type;
        oActiveCollection.isPublic = oResponseData.isPublic;
        oActiveCollection.createdBy = oResponseData.createdBy;
        CollectionViewProps.setActiveCollection(oActiveCollection);
      }
    }

    /**
     * Set Pagination and Filter data
     */
    let oAppliedFilterData = oResponseData.getRequestModel;
    let oFilterProps = ContentUtils.getFilterProps(oCallBackData.filterContext);
    oFilterProps.setTotalItemCount(oResponseData.totalContents);
    oFilterProps.setFromValue(oAppliedFilterData.from);
    oFilterProps.setCurrentPageItems(oResponseData.klassInstances.length);
    oFilterProps.setPaginationSize(oAppliedFilterData.size);

    CollectionViewProps.setBreadCrumbPath([]);
    if(oCallBackData.functionToExecute){
      oCallBackData.functionToExecute();
    }
    if(oCallBackData.updateBreadCrumb) {
      oCallBackData.updateBreadCrumb(oCallBackData.callbackDataToUpdateBreadCrumb);
    }
    CollectionViewProps.setIsEditCollectionScreen(false);
    ContentUtils.showSuccess(getTranslation().SUCCESSFULLY_SAVED);
    _triggerChange();
  };

  var failureSaveDynamicCollectionCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureSaveDynamicCollectionCallback", getTranslation());
  };

  var _saveStaticCollection = function (oCallBack, oCollection, oADMData) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallBack.filterContext);
    let oRequestPayload = oFilterStore.createGetAllInstancesData();
    oRequestPayload.id = oCollection.id;
    oRequestPayload.addedKlassInstanceIds = [];
    oRequestPayload.removedKlassInstanceIds = [];
    if (!CS.isEmpty(oADMData)) {
      let aRemovedContentIdAndType = [];
      let aSelectedContentIdAndType = [];
      let aContentList = ContentUtils.getAppData().getAvailableEntities();
      if(oADMData.addedKlassInstanceIds){
        CS.forEach(oADMData.addedKlassInstanceIds, function (sId) {
          let oFoundContent = CS.find(aContentList, {id: sId});
          aSelectedContentIdAndType.push({id: oFoundContent.id, type: oFoundContent.baseType});
        });
      }

      if(oADMData.removedKlassInstanceIds){
        aContentList = ContentUtils.getEntityList();
        CS.forEach(oADMData.removedKlassInstanceIds, function (sId) {
          let oFoundContent = CS.find(aContentList, {id: sId});
          aRemovedContentIdAndType.push({id: oFoundContent.id, type: oFoundContent.baseType});
        });
      }

      oRequestPayload.addedKlassInstanceIds = aSelectedContentIdAndType;
      oRequestPayload.removedKlassInstanceIds = aRemovedContentIdAndType;
    }
    oRequestPayload.label = oCollection.label;
    oRequestPayload.parentId = oCollection.parentId;
    oRequestPayload.type = oCollection.type;
    oRequestPayload.saveComment = oCollection.saveComment;
    oRequestPayload.isPublic = oCollection.isPublic;
    oRequestPayload.moduleId = ContentUtils.isCollectionScreen() ? 'allmodule' : oRequestPayload.moduleId;
    let oAjaxExtraData = {
      URL: CollectionRequestMapping.SaveStaticCollection,
      requestData: {},
      postData: oRequestPayload
    };


    let oCallBackData = {};
    if(oCallBack.functionToExecute){
      oCallBackData.functionToExecute = oCallBack.functionToExecute;
    }

    let sHelpScreenId;
    let sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION;
    let sLabel = getTranslation().COLLECTION + " : " + oCollection.label;
    oCallBackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(oCollection.id, sLabel, sType, sHelpScreenId, oCallBack.filterContext);

    if (oCollection.createdBy === ContentUtils.getCurrentUser().id) {
      oCallBackData.breadCrumbData.iconClassName = oCollection.isPublic ? "visibilityModeIcon" : "visibilityModeIcon privateMode";
      oCallBackData.breadCrumbData.iconTooltipLabel = oCollection.isPublic ? getTranslation().COLLECTION_PUBLIC_MODE : getTranslation().COLLECTION_PRIVATE_MODE;
    }
    oCallBackData.updateBreadCrumb = function(oCallback) {
      /**
       * When add/remove entity from Collection quick list,
       * should not add "Collection" breadcrumbNode in case of quick list.
       */
      if (!oCallback.doNotAddBreadcrumb) {
        BreadcrumbStore.addNewBreadCrumbItem(oCallback.breadCrumbData);
      }
    };
    oCallBackData.filterContext = oCallBack.filterContext;
    oCallBackData.doNotAddBreadcrumb = oCallBack.doNotAddBreadcrumb;
    oCallBackData.removedIds = oRequestPayload.removedKlassInstanceIds;
    _saveStaticCollectionCall(oCallBackData, oAjaxExtraData);
  };

  var _saveStaticCollectionCall = function (oCallbackData, oAjaxExtraData) {
    CS.postRequest(CollectionRequestMapping.SaveStaticCollection,
        oAjaxExtraData.requestData, oAjaxExtraData.postData,
        successSaveStaticCollectionCallback.bind(this, oCallbackData),
        failureSaveStaticCollectionCallback);
  };

  var successSaveStaticCollectionCallback = function (oCallBack, oResponse) {
    if (!CS.isEmpty(oResponse.success)) {
      var oResponseData = oResponse.success;
      var aSelectedContentIds = ContentUtils.getSelectedContentIds();
      CS.remove(aSelectedContentIds, function (sContentId) {
        return CS.find(oCallBack.removedIds, {id: sContentId});
      });
      const oExtraData = {};
      oExtraData.filterContext = oCallBack.filterContext;

      let oFilterProps = ContentUtils.getFilterProps(oCallBack.filterContext);
      if (oFilterProps.getIsFilterInformationRequired()) {
        let oFilterStore = FilterStoreFactory.getFilterStore(oCallBack.filterContext);
        oFilterStore.prepareAndUpdateFilterAndSortModel({filterData: oResponseData.filterData}, oCallBack, true);
      }

      ContentUtils.performCommonSuccessOperations(oResponseData, oExtraData);
      ContentUtils.showSuccess(getTranslation().COLLECTION_SUCCESSFULLY_SAVED);
      let oActiveCollection = {};
      oActiveCollection.id = oResponseData.id;
      oActiveCollection.label = oResponseData.label;
      oActiveCollection.type = oResponseData.type;
      oActiveCollection.isPublic = oResponseData.isPublic;
      oActiveCollection.createdBy = oResponseData.createdBy;
      oActiveCollection.parentId = oResponseData.parentId;

      CollectionViewProps.setActiveCollection(oActiveCollection);
    }

    CollectionViewProps.setIsEditCollectionScreen(false);
    if(oCallBack.updateBreadCrumb) {
      oCallBack.updateBreadCrumb(oCallBack);
    }
    if(oCallBack.functionToExecute){
      oCallBack.functionToExecute();
    }
    _triggerChange();
  };

  var failureSaveStaticCollectionCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureSaveStaticCollectionCallback", getTranslation());
  };

  var _saveCollection = function (oCallBackData = {}, oADMData) {
    var oCollection = CollectionViewProps.getActiveCollection();
    oCollection = oCollection.clonedObject || oCollection;
    if(CS.isEmpty(oCollection.label)) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    oCallBackData.filterContext = {
      filterType: oFilterPropType.COLLECTION,
      screenContext: oCollection.id
    };
    if (oCollection.type === "dynamicCollection") {
      _saveDynamicCollection(oCallBackData, oCollection);
    } else {
      _saveStaticCollection(oCallBackData, oCollection, oADMData);
    }
  };

  var _handleDeleteCollectionClicked = function (sId, sContext, oCallback) {
    var sUrl = "";
    var sSuccessMessage = null;

    if (sContext === "dynamicCollection") {
      sUrl = CollectionRequestMapping.DeleteDynamicCollection;
      sSuccessMessage = getTranslation().BOOKMARK_DELETED;
    } else {
      sUrl = CollectionRequestMapping.DeleteStaticCollection;
      sSuccessMessage = getTranslation().COLLECTION_DELETED;
    }
    var oRequestPayload = {};
    oRequestPayload.ids = [sId];
    CS.deleteRequest(sUrl, {}, oRequestPayload, successDeleteCollection.bind(this, oCallback, sSuccessMessage, sContext), failureDeleteCollection);
  };

  var successDeleteCollection = function (oCallback, sSuccessMessage, sContext, oResponse) {
    if (!CS.isEmpty(oResponse.success)) {
      var aResponseData = oResponse.success;
      ContentUtils.showSuccess(sSuccessMessage);
      var oActiveCollection = CollectionViewProps.getActiveCollection();
      if(!CS.isEmpty(oActiveCollection) && CS.includes(aResponseData, oActiveCollection.id)) {
        let oFilterStore = FilterStoreFactory.getFilterStore(oCallback.filterContext)
        CollectionViewProps.setActiveCollection({});
        oFilterStore.resetModuleSelection();
        oFilterStore.resetFilterPropsByContext();
        let ContentStore = ContentUtils.getContentStore();
        ContentStore.fetchContentList(); //fetch all contents when closing currently selected collection due to deletion.
      }

      if(sContext == "dynamicCollection") {
        CollectionViewProps.setBreadCrumbPath([]);
        _getDynamicCollectionsList();
      } else {
        _handleCollectionBreadcrumbReset();
        _getStaticCollectionsList();
      }

      if(oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute();
      }

    }
  };

  var failureDeleteCollection = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureDeleteCollection", getTranslation());
  };

  var _handleStaticCollectionAddEntityButtonClicked = function  () {
    CollectionViewProps.setAddEntityInCollectionViewStatus(true);
  };

  var _handleStaticCollectionAddViewCloseClicked = function () {
    CollectionViewProps.setAddEntityInCollectionViewStatus(false);
  };

  var _addNewEntityInActiveStaticCollection = function (oCallbackData, aSelectedEntities) {
    var aSelectedEntitiesIds = CS.map(aSelectedEntities, "id");
    var oADM = {
      addedKlassInstanceIds: aSelectedEntitiesIds,
      removedKlassInstanceIds:[]
    };
    oCallbackData.doNotAddBreadcrumb = true;
    _saveCollection(oCallbackData, oADM);
  };

  var _removeEntityFromCollection = function (oCallbackData, aRemovedEntityIds) {
    var aContentList = ContentUtils.getEntityList();
    var aContentNames = [];
    CS.forEach(aContentList, function (oContent) {
      if(CS.includes(aRemovedEntityIds, oContent.id)) {
        let sContentName = ContentUtils.getContentName(oContent);
        aContentNames.push(sContentName);
      }
    });

    if(aContentNames.length) {
      //Are you sure you want to remove following Content from collection?
      ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_UNLINK,
          aContentNames,
          function () {
            var oADM = {
              addedKlassInstanceIds: [],
              removedKlassInstanceIds: aRemovedEntityIds
            };
            _saveCollection(oCallbackData, oADM);
          },
          {},
          getTranslation().OK,
          getTranslation().CANCEL
      );
    } else {
      ContentUtils.showMessage(getTranslation().NOTHING_IS_SELECTED_TO_DELETE);
    }
  };

  var _makeActiveCollectionDirty = function () {
    var oCollection = CollectionViewProps.getActiveCollection();
    if (!oCollection.clonedObject) {
      oCollection.isDirty = true;
      oCollection.clonedObject = CS.cloneDeep(oCollection);
    }
    return oCollection.clonedObject;
  };

  var _handleActiveCollectionLabelChanged = function (sLabel) {
    let oActiveCollection = _makeActiveCollectionDirty();
    oActiveCollection.label = sLabel;
    _triggerChange();
  };

  var _handleActiveCollectionVisibilityChanged = function(){
    let oActiveCollection = _makeActiveCollectionDirty();
    var bIsPublic = oActiveCollection.isPublic;
    oActiveCollection.isPublic = !bIsPublic;
    _triggerChange();
  };

  let _discardFilters = function(oCallBack) {
    let oFilterProps = ContentUtils.getFilterProps(oCallBack.filterContext);
    let oAppliedTaxonomyCloneData = CollectionViewProps.getAppliedTaxonomyCloneData();
    if (CS.isNotEmpty(oAppliedTaxonomyCloneData)) {
      oFilterProps.setReferencedClasses(oAppliedTaxonomyCloneData.ReferencedClasses);
      oFilterProps.setReferencedTaxonomies(oAppliedTaxonomyCloneData.ReferencedTaxonomies);

      CollectionViewProps.setAppliedTaxonomyCloneData({});

      oFilterProps.setTaxonomyTree({});//Need to restore taxonomy tree data on discard
    }

    let oAvailableSortData = oFilterProps.getAvailableSortData();
    let aActiveSortDetails = oFilterProps.getActiveSortDetails();
    CS.forEach(oAvailableSortData, (oSortData) => {
      oSortData.sortOrder = "";
    });
    CS.forEach(aActiveSortDetails, (oSortData) => {
      oSortData.sortOrder = "";
    });
  };

  var _discardActiveCollection = function (oCallBack = {}) {
    var oActiveCollection = CollectionViewProps.getActiveCollection();
    if (oActiveCollection.type === "dynamicCollection") {
      oCallBack.isCreateNewProps = false;
      oCallBack.filterContext = {
        screenContext: oActiveCollection.id,
        filterType: oFilterPropType.COLLECTION
      };
      _discardFilters(oCallBack);

      NewCollectionStore.handleCollectionSelected(oActiveCollection, oActiveCollection.type, oCallBack);
    }
    delete oActiveCollection.clonedObject;
    delete oActiveCollection.isDirty;
    CollectionViewProps.setIsEditCollectionScreen(false);
  };

  let _cancelEditDialogClicked = function () {
    var oActiveCollection = CollectionViewProps.getActiveCollection();
    if (oActiveCollection.type === "dynamicCollection") {
      let oClonedObject = oActiveCollection.clonedObject;
      let bIsBookmarkNameModified = oClonedObject && oClonedObject.label !== oActiveCollection.label;
      let bIsBookmarkPrivacyModified = oClonedObject && oClonedObject.isPublic !== oActiveCollection.isPublic;

      bIsBookmarkNameModified && (oClonedObject.label = oActiveCollection.label);
      bIsBookmarkPrivacyModified && (oClonedObject.isPublic = oActiveCollection.isPublic);

      if(!oActiveCollection.isBookmarkFilterDirty) {
        delete oActiveCollection.clonedObject;
        delete oActiveCollection.isDirty;
      }
    }
    else {
      delete oActiveCollection.clonedObject;
      delete oActiveCollection.isDirty;
    }

    CollectionViewProps.setIsEditCollectionScreen(false);
  };


  var _handleStaticCollectionHierarchyCutPaste = function (sAddToCollectionId, sRemoveFromCollectionId, aSelectedEntityIdAndType, oCallbackData) {
    if(sAddToCollectionId == sRemoveFromCollectionId){
      ContentUtils.showError(getTranslation().ENTITIES_ALREADY_EXISTS);
      return;
    }

    var oReqData = {
      "addToCollectionId": sAddToCollectionId,
      "removeFromCollectionId": sRemoveFromCollectionId,
      "addedContents":aSelectedEntityIdAndType
    };

    oCallbackData = oCallbackData || {};
    oCallbackData.fromPasteCall = true;
    var sUrl = CollectionRequestMapping.MoveStaticCollection;
    var fSuccess = successStaticCollectionHierarchyEntityDropCallback.bind(this, oCallbackData);
    var fFailure = failureStaticCollectionHierarchyEntityDropCallback;
    _postRequest(sUrl, {}, oReqData, fSuccess, fFailure);
  };

  var successStaticCollectionHierarchyEntityDropCallback = function (oCallback, oResponse) {
    oResponse = oResponse.success;
    if(oResponse.failure){
      var aFailureResponseData = oResponse.failure.exceptionDetails;
      if(!CS.isEmpty(aFailureResponseData)){
        ContentUtils.failureCallback(oResponse, "successStaticCollectionHierarchyEntityDropCallback", getTranslation());
      }
    }

    if(oResponse.successIds.length /*|| oCallback.fromPasteCall*/){
      ContentUtils.showSuccess(getTranslation().SUCCESSFULLY_ADDED_TO_COLLECTION);
    }

    if(oCallback && oCallback.functionToExecute){
      oCallback.functionToExecute();
    }else {
      _triggerChange();
    }
  };

  var failureStaticCollectionHierarchyEntityDropCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureStaticCollectionHierarchyEntityDropCallback", getTranslation());
  };

  var _pasteSelectedEntityInCollectionHierarchy = function (sClickedNodeId, oCallbackData) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oCopiedEntityData = oScreenProps.getHierarchyEntitiesToCopyOrCutData();
    var sCopiedFromCollectionId = oCopiedEntityData.hierarchyNodeId;

    var sAddToCollectionId = sClickedNodeId;
    if(sAddToCollectionId == sCopiedFromCollectionId){
      ContentUtils.showMessage(getTranslation().ENTITIES_ALREADY_EXISTS);
      return;
    }

    var aCopiedEntityList = oCopiedEntityData.entityList;
    if(CS.isEmpty(aCopiedEntityList)){
      ContentUtils.showMessage(getTranslation().NOTHING_SELECTED_TO_PASTE);
      return;
    }

    var aSelectedEntityIdAndType = [];
    CS.forEach(aCopiedEntityList, function (oEntity) {
      var sType = oEntity.baseType ? oEntity.baseType : oEntity.type;
      aSelectedEntityIdAndType.push({id:oEntity.id, type: sType});
    });

    var oPostData = {
      "addToCollectionId": sAddToCollectionId,
      "addedContents":aSelectedEntityIdAndType
    };

    oCallbackData = oCallbackData || {};
    oCallbackData.fromPasteCall = true;

    var sUrl = CollectionRequestMapping.MoveStaticCollection;
    var fSuccess = successStaticCollectionHierarchyEntityDropCallback.bind(this, oCallbackData);
    var fFailure = failureStaticCollectionHierarchyEntityDropCallback;
    _postRequest(sUrl, {}, oPostData, fSuccess, fFailure);
  };

  var _pasteCutSelectedEntityInCollectionHierarchy = function (sAddToCollectionId, oCallbackData) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oCutOrCopiedData = oScreenProps.getHierarchyEntitiesToCopyOrCutData();

    var aSelectedEntityIdAndType = [];
    CS.forEach(oCutOrCopiedData.entityList, function (oEntity) {
        var sBaseType = oEntity.baseType ? oEntity.baseType : oEntity.type;
        aSelectedEntityIdAndType.push({id:oEntity.id, type: sBaseType});
    });

    var sRemoveFromCollectionId = oCutOrCopiedData.hierarchyNodeId;

    _handleStaticCollectionHierarchyCutPaste(sAddToCollectionId, sRemoveFromCollectionId, aSelectedEntityIdAndType, oCallbackData);
  };

  let _handleCollectionCommentChanged = function (sNewValue) {
    let oActiveCollection = CollectionViewProps.getActiveCollection();
    if(oActiveCollection.isDirty){
      oActiveCollection.saveComment = sNewValue;
      CollectionViewProps.setActiveCollection(oActiveCollection);
    }
  }

  /******************** PUBLIC API's ***********************/

  return {

    handleCreateNewCollectionButtonClicked : function (oCreationData, oCallBack) {
      _handleCreateNewCollectionButtonClicked(oCreationData, oCallBack);
    },

    handleModifyCollectionClicked : function (sCollectionId, sContext, oFilterContext) {
      _handleModifyCollectionClicked(sCollectionId, sContext, oFilterContext);
    },

    handleSingleContentAddToStaticCollectionClicked: function (sCollectionId, sContentId, oModel, oFilterContext) {
      _handleSingleContentAddToStaticCollectionClicked(sCollectionId, sContentId, oModel, oFilterContext);
    },

    handleCreateDynamicCollectionButtonClicked : function () {
      _getDynamicCollectionsList();
    },

    handleCreateStaticCollectionButtonClicked : function (oExtraData) {
      _getStaticCollectionsList({}, oExtraData);
    },

    handleCollectionSelected : function (oCollection, sContext, oCallbackData, oExtraData) {
      return _handleCollectionSelected(oCollection, sContext, oCallbackData, oExtraData);
    },

    handleNextCollectionClicked : function (sId) {
      _handleNextCollectionClicked(sId);
    },

    handleCollectionBreadcrumbReset : function () {
      _handleCollectionBreadcrumbReset();
    },

    handleStaticCollectionRootBreadCrumbClicked : function () {
      _handleStaticCollectionRootBreadCrumbClicked();
    },

    handleCollectionItemVisibilityModeChanged : function(sId, bIsDynamic, oFilterContext){
      _handleCollectionItemVisibilityModeChanged(sId, bIsDynamic, oFilterContext);
    },

    handleStaticCollectionBreadCrumbItemClicked: function (oItem, sContext) {
      _handleStaticCollectionBreadCrumbItemClicked(oItem, sContext);
    },

    handleCollectionSaved : function (oCallBackData, oADMData) {
      //there will be no oADMData for Dynamic Collections
      _saveCollection(oCallBackData, oADMData);
    },

    handleActiveCollectionLabelChanged : function (sLabel) {
      _handleActiveCollectionLabelChanged(sLabel);
    },

    handleActiveCollectionVisibilityChanged: function(){
      _handleActiveCollectionVisibilityChanged();
    },

    handleDeleteCollectionClicked : function (sId, sContext, oCallback) {
      _handleDeleteCollectionClicked(sId, sContext, oCallback);
    },

    handleStaticCollectionAddEntityButtonClicked: function () {
      _handleStaticCollectionAddEntityButtonClicked();
    },

    handleStaticCollectionAddViewCloseClicked: function () {
      _handleStaticCollectionAddViewCloseClicked();
      _triggerChange();
    },

    addNewEntityInActiveStaticCollection: function (oCallbackData, aSelectedEntities) {
      _addNewEntityInActiveStaticCollection(oCallbackData, aSelectedEntities);
    },

    removeEntityFromCollection: function (oCallbackData, aRemovedEntityIds) {
      _removeEntityFromCollection(oCallbackData, aRemovedEntityIds);
    },

    discardActiveCollection: function (oCallbackData) {
      _discardActiveCollection(oCallbackData);
      _triggerChange();
    },

    cancelEditDialogClicked: function (oCallbackData) {
      _cancelEditDialogClicked(oCallbackData);
      _triggerChange();
    },

    handleEditCollectionOKButtonClicked: function () {
      CollectionViewProps.setIsEditCollectionScreen(false);
      _triggerChange();
    },

    getStaticCollectionsList: function (oCallBack) {
      _getStaticCollectionsList(oCallBack);
    },

    pasteCopiedSelectedEntityInCollectionHierarchy: function (sClickedNodeId, oCallbackData) {
      _pasteSelectedEntityInCollectionHierarchy(sClickedNodeId, oCallbackData);
    },

    pasteCutSelectedEntityInCollectionHierarchy: function (sAddToCollectionId, oCallbackData) {
      _pasteCutSelectedEntityInCollectionHierarchy(sAddToCollectionId, oCallbackData);
    },

    createStaticCollectionAccordingToMode: function (sMode, sCollectionName, sId, bIsPrivateMode, oCallbackData) {
      _createStaticCollectionAccordingToMode(sMode, sCollectionName, sId, bIsPrivateMode, oCallbackData);
    },

    handleCollectionCommentChanged: function (sNewValue) {
      _handleCollectionCommentChanged(sNewValue);
    }
  }
})();

MicroEvent.mixin(NewCollectionStore);

export default NewCollectionStore;
