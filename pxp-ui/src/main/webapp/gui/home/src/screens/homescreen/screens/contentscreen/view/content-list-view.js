import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as LazyScrollView } from '../../../../../viewlibraries/lazyscrollview/lazy-scroll-view.js';
import { view as ListItemView } from '../../../../../viewlibraries/detailedlistview/detailed-list-item-view';
import ListItemModel from '../../../../../viewlibraries/detailedlistview/model/detailed-list-item-model';
import { view as DragView } from './../../../../../viewlibraries/dragndropview/drag-view.js';
import DragViewModel from './../../../../../viewlibraries/dragndropview/model/drag-view-model';
import { view as PaginationView } from './../../../../../viewlibraries/paginationview/pagination-view';
import ViewUtils from './utils/view-utils';
import EntityBaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import ModuleDictionary from '../../../../../commonmodule/tack/module-dictionary';
import PhysicalCatalogDictionary from '../../../../../commonmodule/tack/physical-catalog-dictionary';
import LogFactory from '../../../../../libraries/logger/log-factory';
import Constants from '../../../../../commonmodule/tack/constants';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
var logger = LogFactory.getLogger('content-tile-view');

const oEvents = {
  CONTENT_TILE_PAGINATION_CHANGED: "content_tile_pagination_changed"
};

const oPropTypes = {
  contentListViewData: ReactPropTypes.object,
  sortViewData: ReactPropTypes.object,
  toolbarData: ReactPropTypes.object,
  currentZoom: ReactPropTypes.number,
  navigationData: ReactPropTypes.object,
  dragDropContext: ReactPropTypes.string,
  filterContext: ReactPropTypes.object,
  activeCollection: ReactPropTypes.object,
  canClone: ReactPropTypes.bool
};

// @CS.SafeComponent
class ContentListView extends React.Component {
  constructor(props) {
    super(props);

    this.lazyScrollView = React.createRef();
  }
  static propTypes = oPropTypes;

  handlePaginationChanged = (oPaginationData, oFilterContext) => {
    EventBus.dispatch(oEvents.CONTENT_TILE_PAGINATION_CHANGED, oPaginationData, oFilterContext);
  };

  getImageSrcForThumbnail = (oContent, sNatureType) => {
    var aList = oContent.referencedAssets;
    var oImage = CS.find(aList, {isDefault: true});

    if(CS.isEmpty(oImage)){
      if(oContent.baseType == EntityBaseTypeDictionary.assetBaseType) {
        oImage = oContent.assetInformation;
      } else{
        var __props = this.props;
        let aReferencedAsset = __props.contentListViewData.referencedAssets;
        oImage = CS.find(aReferencedAsset, {id: oContent.defaultAssetInstanceId});
      }
    }

    let oImageData = ViewUtils.getImageSrcUrlFromImageObject(oImage);
    oImageData.thumbnailType = oImageData.thumbnailType || sNatureType;;
    return oImageData;
  };

  getContentThumbnailViews = () => {
    var __props = this.props;
    var oActiveCollection = __props.activeCollection;
    var oContentListViewData = __props.contentListViewData;
    let sSelectedModuleId = ViewUtils.getSelectedModuleId();

    var bIsLinkedVariant = false;
    var oActiveContent = oContentListViewData.activeEntity || {};
    var aSelectedContentList = oContentListViewData.selectedEntityList;
    var bIsDragNotAllowed = oContentListViewData.isDragNotAllowed;
    var aContentList = oContentListViewData.entityList;
    var aCutEntityList = oContentListViewData.cutEntityList;
    let bIsKPIContentExplore = oContentListViewData.isKPIContentExplore;

    var aFolderThumbnails = [];
    var aContentThumbnails = [];

    logger.debug(
        'getContentThumbnailViews: creating Content Thumbnails on master list',
        {'contentList': aContentList});
    var aUserList = oContentListViewData.userList;

    CS.forEach(aContentList, function (oContent) {

      var sContentId = oContent.id,
          bIsActive = false,
          bIsSelected = false,
          sIdKey = "id";
      let sContentName = ViewUtils.getContentName(oContent);

      if (oContent.uuid) {
        sIdKey = "uuid";
      }

      sContentId = oContent[sIdKey];
      bIsActive = oContent[sIdKey] == oActiveContent[sIdKey];
      var oCondition = {};
      oCondition[sIdKey] = oContent[sIdKey];
      bIsSelected = !!CS.find(aSelectedContentList, oCondition);

      var sClassName = "";
      var sClassIcon = "";

      var sKlassId = ViewUtils.getEntityClassType(oContent);
      var oClass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
      var sThumbnailImageSrc = "";
      let sPreviewImageSrc = "";

      if (oClass) {
        sClassName = CS.getLabelOrCode(oClass);
        sClassIcon = oClass.icon;
        sPreviewImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      }

      var oDataForThumbnail = this.getImageSrcForThumbnail(oContent,oClass.natureType);
      sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc || sPreviewImageSrc;
      var sThumbnailType = oDataForThumbnail.thumbnailType;

      var sClassNameForIcon = "";
        // EntityBaseTypeDictionary["contentBaseType"] && EntityBaseTypeDictionary["assetBaseType"]
        sClassNameForIcon = oContent.isFolder ? 'setFolderThumb' : 'articleThumb';

      var oUser = CS.find(aUserList, {id: oContent.lastModifiedBy});
      var sLastModifiedBy = CS.isEmpty(oUser) ? "" : oUser.firstName +" "+oUser.lastName;
      var sEntityType = ViewUtils.getEntityType(oContent);

      var bIsEntityRemovable = (!CS.isEmpty(oActiveCollection) && oActiveCollection.type == "staticCollection");

      var bIsCheckDisabled = bIsLinkedVariant;
      var bAllowRightClick = false;
      var bIsDeleteDisabled = false;
      var bIsViewDisabled = false;
      var bIsCloneDisabled = true;
      var bIsAddToCollectionDisabled = true;
      var bIsCollection = !CS.isEmpty(oActiveCollection);
      var oDataIntegrationInfo = ViewUtils.getDataIntegrationInfo();
      let bIsRuleViolationScreen = ViewUtils.getIsRuleViolatedContentsScreen();

      let bIsArchive = ViewUtils.getIsArchive();
      var bIsDownloadDisabled = (oDataIntegrationInfo.physicalCatalogId !== PhysicalCatalogDictionary.DATAINTEGRATION ||
          ViewUtils.getSelectedModuleId() !== ModuleDictionary.FILES);
      if ((bIsCollection || ViewUtils.getIsOnLandingPage()
          || (__props.dragDropContext && (__props.dragDropContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY
          || __props.dragDropContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY))) && !bIsArchive){
        bIsAddToCollectionDisabled = false;
      }
      if(__props.dragDropContext && (__props.dragDropContext == "relationshipEntity" || __props.dragDropContext == "staticCollection"
          || __props.dragDropContext == "manageVariantLinkedInstances" || __props.dragDropContext == "contextEntity")){
        bIsDeleteDisabled = true;
        bIsViewDisabled = true;
      }

      if(bIsArchive){
        // bIsDeleteDisabled = true;
        bIsViewDisabled = true;
      }

      if (ViewUtils.isBaseTypeArticle(oContent.baseType) && ViewUtils.getIsOnLandingPage() && !bIsArchive && !bIsRuleViolationScreen) {
        bIsCloneDisabled = !this.props.canClone;
      }

      if(!CS.isEmpty(oActiveCollection) && sSelectedModuleId === ModuleDictionary.PIM && oActiveCollection.type === "dynamicCollection"){
        bIsCloneDisabled = !this.props.canClone;
      }

      if(ViewUtils.isSupplierBaseType(oContent.baseType) && !bIsEntityRemovable) {
        bIsDeleteDisabled = true;
        bIsCloneDisabled = true;
      }

      if (bIsKPIContentExplore) {
        bIsCloneDisabled = true;
      }

      /**Disable Clone button visibility for Data Integration **/
      if(oDataIntegrationInfo.physicalCatalogId === PhysicalCatalogDictionary.DATAINTEGRATION){
        bIsCloneDisabled = true;
      }

      if(__props.dragDropContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
        if(bIsDragNotAllowed){
          bIsDeleteDisabled = true;
        }else {
          bIsEntityRemovable = true;
          bIsDeleteDisabled = false;
        }
      }else if(__props.dragDropContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
        bIsDeleteDisabled = true;
      }
      if (ViewUtils.getSelectedModuleId() === ModuleDictionary.FILES) {
        bIsAddToCollectionDisabled = true;
      }

      var bToCutEntity = !!CS.find(aCutEntityList, {id:sContentId});

      let bIsGoldenArticle = CS.includes(oContent.types, "golden_article_klass");
      let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();

      var oThumbnailProperties = {
        entity: oContent,
        entityType: sEntityType,
        thumbnailType: ViewUtils.getThumbnailType() || sThumbnailType,
        isActive: bIsActive,
        isSelected: bIsSelected,
        isDirty: Boolean(oContent.isDirty) || Boolean(oContent.isCreated),
        className: sClassNameForIcon,
        lastModifiedBy: sLastModifiedBy,
        classIcon: sClassIcon,
        searchFilterData: oContentListViewData.searchFilterData,
        disableDelete: bIsDeleteDisabled || bIsCurrentUserReadOnly,
        disableClone: bIsCloneDisabled || bIsCurrentUserReadOnly,
        disableView: bIsViewDisabled,
        disableDownload: bIsDownloadDisabled,
        disableAddToCollection: bIsAddToCollectionDisabled || bIsCurrentUserReadOnly,
        disableCheck: bIsCheckDisabled,
        isEntityRemovable: bIsEntityRemovable,
        isToCutEntity: bToCutEntity,
        relevanceValue: oContent.relevance,
        disableRestore: !bIsArchive || bIsCurrentUserReadOnly,
        isGoldenArticle: bIsGoldenArticle,
        previewImageSrc: sPreviewImageSrc
      };
      let sEndpointId = CS.isNotEmpty(__props.contentListViewData.endpointId) ? __props.contentListViewData.endpointId : "";
      let sOrganizationId = CS.isNotEmpty(__props.contentListViewData.organizationId) ? __props.contentListViewData.organizationId : "";
      var oThumbnailModel = new ListItemModel(
          sContentId,
          sContentName,
          sThumbnailImageSrc,
          oContent.tags,
          sThumbnailType,
          sClassName,
          oThumbnailProperties,
          sEndpointId,
          sOrganizationId
      );

      var oDragModelProperties = {
        data: sContentId,
        className: "listItemDrag",
        noOfSelectedContents: aSelectedContentList.length
      };
      var oDragViewModel = new DragViewModel(sContentId, sContentName, true, "article", oDragModelProperties);
      if (__props.dragDropContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY || __props.dragDropContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY) {
        /** TO enable drag, remove SELF 'else if' block*/
        oDragViewModel = new DragViewModel(sContentId, sContentName, false, __props.dragDropContext, oDragModelProperties);
      }
      else if(__props.dragDropContext){
        oDragViewModel = new DragViewModel(sContentId, sContentName, true, __props.dragDropContext, oDragModelProperties);
      }

      var oThumbnailView = (<DragView model={oDragViewModel} key={sContentId}>
        <ListItemView
            model={oThumbnailModel}
            filterContext={__props.filterContext}
        />
      </DragView>);

      aContentThumbnails.push(oThumbnailView);

    }.bind(this));

    return aContentThumbnails;
  };

  getListPapers = () => {
    var oThumbnailViews = this.getContentThumbnailViews();
    var aSections = [];

    aSections.push(
        <div className="articleThumbnailContainer articleListViewContainer" key="article">
          {oThumbnailViews}
        </div>
    );

    return aSections;
  };

  handleDragEnter = (oEvent) => {
    var oUploadWrapperDOM = document.querySelector('.upload-wrapper');
    if(oUploadWrapperDOM) {
      if(oEvent.dataTransfer) {
        var aTypes = oEvent.dataTransfer.types;
        if(CS.includes(aTypes, "Files")) {
          oUploadWrapperDOM.classList.add('over');
        }
      }
    }
  };

  getPaginationView = (oNavigationData) => {
    return (
        <div className="paginationSection">
          <PaginationView
              currentPageItems={oNavigationData.currentPageItems}
              from={oNavigationData.from}
              pageSize={oNavigationData.pageSize}
              totalItems={oNavigationData.totalContents}
              onChange={this.handlePaginationChanged}
              displayTheme={Constants.DARK_THEME}
              filterContext={this.props.filterContext}
          />
        </div>
    )
  };

  render() {
    var __props = this.props;
    var oNavigationData = this.props.navigationData;
    var oLoadMoreView = oNavigationData.isHidden ? null : this.getPaginationView(oNavigationData);
    var oSortViewData = __props.contentListViewData.sortByViewData;

    var sThumbLazyViewClassName = "contentThumbsLazyScrollContainer ";
    sThumbLazyViewClassName += oSortViewData.isActive ? "reducedHeight " : "";
    var oView = this.getListPapers();
    return (
        <div className="contentTileViewContainer">

          <div className={sThumbLazyViewClassName} onDragEnter={this.handleDragEnter}>
            <LazyScrollView scrollBottomOffset={100}
                            onInfiniteLoad={this.handleInfiniteLoad}
                            ref={this.lazyScrollView}>
              {oView}
            </LazyScrollView>
            {oLoadMoreView}
          </div>
        </div>
    );
  }
}

export const view = ContentListView;
export const events = oEvents;
