
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ThumbnailView } from '../../../../../viewlibraries/thumbnailview2/thumbnail-view.js';
import ThumbnailModel from '../../../../../viewlibraries/thumbnailview2/model/thumbnail-model';
import { view as PaginationView } from './../../../../../viewlibraries/paginationview/pagination-view';
import { view as DragView } from './../../../../../viewlibraries/dragndropview/drag-view.js';
import DragViewModel from './../../../../../viewlibraries/dragndropview/model/drag-view-model';
import { view as ThumbnailWrapperForDimensionCalculation } from './thumbnail-wrapper-for-dimension-calculation';
import { view as ThumbnailGoldenRecordBucketView } from '../../../../../viewlibraries/thumbnailview2/thumbnail-golden-record-bucket-view';
import BaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import ViewUtils from './utils/view-utils';
import LogFactory from '../../../../../libraries/logger/log-factory';
import ModuleDictionary from '../../../../../commonmodule/tack/module-dictionary';
import PhysicalCatalogDictionary from '../../../../../commonmodule/tack/physical-catalog-dictionary';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
import Constants from '../../../../../commonmodule/tack/constants';
import ContentScreenViewContextConstants from '../tack/content-screen-view-context-constants';
import MarkerClassTypeDictionary from '../../../../../commonmodule/tack/marker-class-type-dictionary';
import ContentScreenConstants from '../store/model/content-screen-constants';

var logger = LogFactory.getLogger('content-tile-view');

const oEvents = {
  CONTENT_TILE_PAGINATION_CHANGED: "content_tile_pagination_changed"
};

const oPropTypes = {
  contentTileViewData: ReactPropTypes.object,
  toolbarData: ReactPropTypes.object,
  navigationData: ReactPropTypes.object,
  thumbnailMode: ReactPropTypes.string,
  dragDropContext: ReactPropTypes.string,
  activeXRayPropertyGroup: ReactPropTypes.object,
  xRayData: ReactPropTypes.object,
  activeCollection: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  currentZoom: ReactPropTypes.number
};

// @CS.SafeComponent
class ContentTileView extends React.Component {
  constructor(props) {
    super(props);
    this.contentContainer = React.createRef();
  }

  static propTypes = oPropTypes;
  componentDidMount() {
  }

  handlePaginationChanged = (oPaginationData, oFilterContext) => {
    EventBus.dispatch(oEvents.CONTENT_TILE_PAGINATION_CHANGED, oPaginationData, oFilterContext);
  };

  getPaginationView = (oCurrentNavigationData, bIsGoldenRecordBucketsMode) => {
    let oNavigationData;
    if (bIsGoldenRecordBucketsMode) {
      var oContentTileViewData = this.props.contentTileViewData;
      oNavigationData = oContentTileViewData.goldenRecordBucketsPaginationData;
    } else {
      oNavigationData = oCurrentNavigationData;
    }

    return (
        <div className="paginationSection">
          <PaginationView
              currentPageItems={oNavigationData.currentPageItems}
              from={oNavigationData.from}
              pageSize={oNavigationData.pageSize}
              totalItems={oNavigationData.totalContents}
              onChange={this.handlePaginationChanged}
              filterContext={this.props.filterContext}
              displayTheme={Constants.DARK_THEME}
          />
        </div>
    )
  };

  getImageSrcForThumbnail = (oContent, sNatureType) => {
    let oContentTileViewData = this.props.contentTileViewData;
    let aList = oContentTileViewData.referencedAssets;
    let oImage = {};
    if (oContent.baseType == BaseTypeDictionary.assetBaseType) {
      oImage = oContent.assetInformation;
    } else {
      oImage = CS.find(aList, {isDefault: true})  || CS.find(aList, {id: oContent.defaultAssetInstanceId});;
    }

    let oImageData = ViewUtils.getImageSrcUrlFromImageObject(oImage);
    oImageData.thumbnailType = oImageData.thumbnailType || sNatureType;
    return oImageData;
  };

  getGoldenRecordBucketsThumbnails = () => {
    var oContentTileViewData = this.props.contentTileViewData;
    let oGoldenRecordBucketsData = oContentTileViewData.goldenRecordBucketsData;
    let aContentThumbnails = [];
    let oConfigDetails = oGoldenRecordBucketsData.configDetails;

    CS.forEach(oGoldenRecordBucketsData.buckets, function (oBucket) {
      aContentThumbnails.push(
          <ThumbnailGoldenRecordBucketView
              id={oBucket.id}
              key={oBucket.id}
              name={CS.getLabelOrCode(oBucket)}
              matchCount={oBucket.klassInstanceCount}
              activeTabId={oBucket.activeTabId}
              attributes={oBucket.attributes}
              referencedAttributes={oConfigDetails.referencedAttributes}
              tags={oBucket.tags}
              referencedTags={oConfigDetails.referencedTags}
              multiTaxonomyData={oBucket.multiTaxonomyData}
              klassesData={oBucket.klassesData}
              userList={oContentTileViewData.userList}
              matches={oBucket.matches}/>
      )
    });

    return aContentThumbnails;
  };

  getCloneButtonVisibility = (oContent, bIsArchive, bIsRuleViolationScreen, bIsEntityRemovable, bIsKPIContentExplore) => {
    let oActiveCollection = this.props.activeCollection;
    let sPhysicalCatalogId = ViewUtils.getDataIntegrationInfo().physicalCatalogId;
    let bIsCloneDisabled = true;

    if ((CS.isNotEmpty(oActiveCollection)
        && ViewUtils.getIsDynamicCollectionScreen()
        && ViewUtils.isBaseTypeArticle(oContent.baseType))
        || (ViewUtils.isBaseTypeArticle(oContent.baseType))
        || (ViewUtils.isTextAssetBaseType(oContent.baseType)) || (ViewUtils.isTargetBaseType(oContent.baseType))
        && ViewUtils.getIsOnLandingPage()
        && !bIsArchive
        && !bIsRuleViolationScreen) {
      bIsCloneDisabled = !this.props.canClone;
    }

    /**Clone button visibility for supplier & KPI**/
    if ((ViewUtils.isSupplierBaseType(oContent.baseType) && !bIsEntityRemovable) || bIsKPIContentExplore) {
      bIsCloneDisabled = true;
    }

    /**Disable Clone button visibility for Data Integration **/
    if(sPhysicalCatalogId === PhysicalCatalogDictionary.DATAINTEGRATION){
      bIsCloneDisabled = true;
    }
    /**Disable Clone button visibility for PIM Archival **/
    if (bIsArchive) {
      bIsCloneDisabled = true;
    }
    return bIsCloneDisabled;
  };

  getContentThumbnailViews = () => {
    let _this = this;
    var __props = this.props;
    var oContentTileViewData = __props.contentTileViewData;

    var bIsDragNotAllowed = oContentTileViewData.isDragNotAllowed;
    var oActiveContent = oContentTileViewData.activeEntity || {};
    var aSelectedContentList = oContentTileViewData.selectedEntityList || [];
    var aCheckToggleDisabledEntityList = oContentTileViewData.checkToggleDisabledEntityList || [];
    var aContentList = oContentTileViewData.entityList;
    var sRightClickedThumbnailId = oContentTileViewData.rightClickedThumbnailId;
    var aContextMenuList = oContentTileViewData.contextMenuList;
    var aCutEntityList = oContentTileViewData.cutEntityList;
    let bIsKPIContentExplore = oContentTileViewData.isKPIContentExplore;
    var aContentThumbnails = [];

    logger.debug(
        'getContentThumbnailViews: creating Content Thumbnails on master list',
        {'contentList': aContentList});

      CS.forEach(aContentList, function (oContent) {

        var sContentId = oContent.id,
            bIsActive = false,
            bIsSelected = false,
            sIdKey = "id";


        if (oContent.uuid) {
          sIdKey = "uuid";
        }

        sContentId = oContent[sIdKey];
        bIsActive = oContent[sIdKey] == oActiveContent[sIdKey];
        var oCondition = {};
        oCondition[sIdKey] = oContent[sIdKey];
        bIsSelected = !!CS.find(aSelectedContentList, oCondition);
        let bDisableCheckToggle = CS.includes(aCheckToggleDisabledEntityList, sContentId);

        var sClassName = "";
        var sClassIcon = "";

        var sKlassId = ViewUtils.getEntityClassType(oContent);
        var oClass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
        var sThumbnailImageSrc = "";
        var sContentName = ViewUtils.getContentName(oContent);
        let sPreviewImageSrc =  "";

        if (oClass) {
          sClassName = CS.getLabelOrCode(oClass);
          sClassIcon = oClass.icon;
          sPreviewImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
        }

        var aIcons = [];
        var sBranchNumber = "";
        if (oContent.variantOf != "-1") {
          aIcons.push("variantIcon");
        }
        if (oContent.branchOf != "-1") {
          aIcons.push("branchIcon");
          sBranchNumber = oContent.branchNo || "";
        }

        var oDataForThumbnail = _this.getImageSrcForThumbnail(oContent, oClass.natureType);
        sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc || sPreviewImageSrc;
        var sThumbnailType = oDataForThumbnail.thumbnailType;
        var oImageAttribute = CS.find(oContent.attributes, {baseType: BaseTypeDictionary.imageAttributeInstanceBaseType});
        var sThumbnailClass = "articleThumb";
        var sClassNameForIcon = oContent.isFolder ? sThumbnailClass + ' folderThumb' : sThumbnailClass;
        var sVersionText = sBranchNumber ? ("Version " + sBranchNumber) : "";
        var bIsAddToCollectionDisabled = true;
        var oActiveCollection = __props.activeCollection;
        var oDataIntegrationInfo = ViewUtils.getDataIntegrationInfo();

        let bIsRuleViolationScreen = ViewUtils.getIsRuleViolatedContentsScreen();
        let bIsArchive = ViewUtils.getIsArchive();
        var bIsEntityRemovable = (!CS.isEmpty(oActiveCollection) && oActiveCollection.type == "staticCollection");
        var bIsCollection = !CS.isEmpty(oActiveCollection);
        if ((bIsCollection || ViewUtils.getIsOnLandingPage()
            || (__props.dragDropContext && (__props.dragDropContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY
            || __props.dragDropContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY))) && !bIsArchive){
          bIsAddToCollectionDisabled = false;
        }


        var bAllowRightClick = false;
        var bIsDeleteDisabled = false;
        var bIsShowVariantCreation = false;
        var bIsCloneDisabled = true;
        var bIsViewDisabled = false;
        var bShowRadioButton = false;
        let bHideDeleteButton = false;
        var bIsDownloadDisabled = (oDataIntegrationInfo.physicalCatalogId !== PhysicalCatalogDictionary.DATAINTEGRATION ||
            ViewUtils.getSelectedModuleId() !== ModuleDictionary.FILES);
        var bIsCheckDisabled = false;
        if(__props.dragDropContext && (__props.dragDropContext == "relationshipEntity" || __props.dragDropContext == "staticCollection"
                || __props.dragDropContext == "manageVariantLinkedInstances" || __props.dragDropContext == "contextEntity" || __props.dragDropContext == ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW)) {
          bIsDeleteDisabled = true;
          bIsViewDisabled = true;
        }
        if (__props.dragDropContext == "productVariantQuickList") {
          bIsDeleteDisabled = true;
        }
        if (__props.dragDropContext == ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS) {
          bIsDeleteDisabled = true;
          bIsCheckDisabled = true;
        }
        if(bIsArchive){
          // bIsDeleteDisabled = true;
          bIsViewDisabled = true;
        }

        if(ViewUtils.isSupplierBaseType(oContent.baseType) && !bIsEntityRemovable) {
          bIsDeleteDisabled = true;
          bIsCloneDisabled = true;
        }

        if(__props.dragDropContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
          if(bIsDragNotAllowed){
            bIsDeleteDisabled = true;
          }else {
            bIsEntityRemovable = true;
            bIsDeleteDisabled = false;
            bHideDeleteButton = true;
          }
        }else if(__props.dragDropContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY) {
          bIsDeleteDisabled = true;
        }

        var aKlassIds = oContent.types;
        var aKlassLabel = [];
        let bIsGoldenArticle = false;
        CS.forEach(aKlassIds, function (sKlassId) {
          if (sKlassId === MarkerClassTypeDictionary.GOLDEN_RECORD) {
            bIsGoldenArticle = true;
          }
          if(sKlassId != MarkerClassTypeDictionary.MARKER && sKlassId != MarkerClassTypeDictionary.GOLDEN_RECORD) {
            var oKlass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
            aKlassLabel.push(CS.getLabelOrCode(oKlass));
          }
        });

        var bToCutEntity = !!CS.find(aCutEntityList, {id:sContentId});

        if (ViewUtils.getSelectedModuleId() === ModuleDictionary.FILES) {
          bIsAddToCollectionDisabled = true;
        }

        /** Clone button visibility*/
        bIsCloneDisabled = _this.getCloneButtonVisibility(oContent, bIsArchive, bIsRuleViolationScreen, bIsEntityRemovable, bIsKPIContentExplore);
        let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();

        var bThumbContextPopoverOpenStatus = sRightClickedThumbnailId == sContentId;
        var oThumbnailProperties = {
          entity: oContent,
          entityType: ViewUtils.getEntityType(oContent),
          isActive: bIsActive,
          isSelected: bIsSelected,
          thumbnailType: ViewUtils.getThumbnailType() || sThumbnailType,
          lastModifiedBy: "",
          isDirty: Boolean(oContent.isDirty) || Boolean(oContent.isCreated) || false,
          className: sClassNameForIcon,
          classIcon: sClassIcon,
          imageAttribute: oImageAttribute,
          versionText: sVersionText,
          icons: aIcons,
          searchFilterData: oContentTileViewData.searchFilterData,
          thumbnailMode: __props.thumbnailMode,
          disableDelete: bIsDeleteDisabled || bIsCurrentUserReadOnly,
          isShowVariantCreation : bIsShowVariantCreation,
          disableClone: bIsCloneDisabled || bIsCurrentUserReadOnly,
          disableView: bIsViewDisabled,
          disableCheck: bIsCheckDisabled,
          disableDownload: bIsDownloadDisabled,
          disableAddToCollection: bIsAddToCollectionDisabled || bIsCurrentUserReadOnly,
          activeXRayPropertyGroup: __props.activeXRayPropertyGroup,
          xRayData: __props.xRayData,
          classNames: aKlassLabel,
          containerClass: oClass.natureType || "",
          isEntityRemovable: bIsEntityRemovable,
          allowRightClick: bAllowRightClick,
          thumbContextPopoverOpenStatus: bThumbContextPopoverOpenStatus,
          contextMenuList: aContextMenuList,
          isToCutEntity: bToCutEntity,
          relevanceValue: oContent.relevance,
          showPreview: true,
          isGoldenArticle: bIsGoldenArticle,
          disableRestore: !bIsArchive || bIsCurrentUserReadOnly,
          showRadioButton: bShowRadioButton,
          disableCheckToggle: bDisableCheckToggle,
          previewImageSrc: sPreviewImageSrc,
          hideDeleteButton: bHideDeleteButton
        };

        let sEndpointId = CS.isNotEmpty(__props.contentTileViewData.endpointId) ? __props.contentTileViewData.endpointId : "";
        let sOrganizationId = CS.isNotEmpty(__props.contentTileViewData.organizationId) ? __props.contentTileViewData.organizationId : "";
        var oThumbnailModel = new ThumbnailModel(
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

        var bIsAsset = ViewUtils.isAssetBaseType(oContent.baseType);
        var sViewMode = bIsAsset ? "b" : "a";

        if(bIsDragNotAllowed){
          var oDragViewModel = new DragViewModel(sContentId, sContentName, false, "noDropAllowed", {});
        }else {
          var oDragModelProperties = {
            data: sContentId,
            entity: oContent,
            noOfSelectedContents: aSelectedContentList.length
          };
          oDragViewModel = new DragViewModel(sContentId, sContentName, true, "article", oDragModelProperties);
          if (__props.dragDropContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY || __props.dragDropContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY) {
            /** TO enable drag, remove SELF 'else if' block*/
            oDragViewModel = new DragViewModel(sContentId, sContentName, false, __props.dragDropContext, oDragModelProperties);
          } else if (__props.dragDropContext) {
            oDragViewModel = new DragViewModel(sContentId, sContentName, true, __props.dragDropContext, oDragModelProperties);
          }

        }

        var oThumbnailView = (<DragView model={oDragViewModel} key={sContentId}>
          <ThumbnailView
              model={oThumbnailModel}
              viewMode={sViewMode}
              currentZoom={__props.currentZoom}
              filterContext={__props.filterContext}
          />
        </DragView>);

        aContentThumbnails.push(oThumbnailView);

      });
    return aContentThumbnails;
  };

  getTilePapers = (oContentTileViewData) => {
    let aThumbnailViews = [];
    if (oContentTileViewData.showGoldenRecordBuckets) {
      aThumbnailViews = this.getGoldenRecordBucketsThumbnails();
    }
    else {
      aThumbnailViews = this.getContentThumbnailViews();
    }

    var aSections = [];

    aSections.push(
        <ThumbnailWrapperForDimensionCalculation key="article" ref={this.contentContainer}>
          <div className="articleThumbnailContainer" data-container-type="thumbnailContainer">
            {aThumbnailViews}
          </div>
        </ThumbnailWrapperForDimensionCalculation>
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

  render() {
    var __props = this.props;
    var oContentTileViewData = __props.contentTileViewData;
    let bIsGoldenRecordBucketsMode = oContentTileViewData.showGoldenRecordBuckets;
    var oNavigationData = this.props.navigationData;
    var oLoadMoreView = oNavigationData.isHidden ? null : this.getPaginationView(oNavigationData, bIsGoldenRecordBucketsMode);
    var oSortViewData = __props.contentTileViewData.sortByViewData;

    var sThumbLazyViewClassName = "contentThumbsLazyScrollContainer ";
    sThumbLazyViewClassName += oSortViewData.isActive ? "reducedHeight " : "";
    var oView = this.getTilePapers(oContentTileViewData);

    return (
        <div className="contentTileViewContainer">
          <div className={sThumbLazyViewClassName} onDragEnter={this.handleDragEnter}>
            {oView}
            {oLoadMoreView}
          </div>
        </div>
    );
  }
}

export const view = ContentTileView;
export const events = oEvents;
