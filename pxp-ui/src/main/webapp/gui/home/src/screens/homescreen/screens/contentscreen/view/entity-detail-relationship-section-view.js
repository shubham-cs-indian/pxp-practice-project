
import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from '../store/helper/screen-mode-utils';

import ThumbnailModel from '../../../../../viewlibraries/thumbnailview2/model/thumbnail-model';
import { view as ThumbnailView } from '../../../../../viewlibraries/thumbnailview2/thumbnail-view.js';
import ListItemModel from '../../../../../viewlibraries/detailedlistview/model/detailed-list-item-model';
import { view as ListItemView } from '../../../../../viewlibraries/detailedlistview/detailed-list-item-view';
import { view as PaginationView } from './../../../../../viewlibraries/paginationview/pagination-view';
import { view as ToolbarView } from '../../../../../viewlibraries/toolbarview/toolbar-view';
import { view as RuleViolationView } from './rule-violation-view';
import { view as FileDragAndDropView } from '../../../../../viewlibraries/filedraganddropview/file-drag-and-drop-view';
import { view as UploadAssetDialogView } from './upload-asset-dialog-view';
import AssetUploadContextDictionary from '../../../../../commonmodule/tack/asset-upload-context-dictionary';
import BaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import ThumbnailModeConstants from '../../../../../commonmodule/tack/thumbnail-mode-constants';
import RelationshipTypeDictionary from '../../../../../commonmodule/tack/relationship-type-dictionary';
import ContentScreenConstants from '../store/model/content-screen-constants';
import Constants from '../../../../../commonmodule/tack/constants';
import ViewUtils from '../view/utils/view-utils';
import VariantTagGroupDialogView from '../../../../../viewlibraries/varianttagsummaryview/variant-tag-group-dialog-view';
import { view as NothingFoundView } from './../../../../../viewlibraries/nothingfoundview/nothing-found-view';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary';
import oFilterPropType from '../tack/filter-prop-type-constants';
import CoverflowAssetTypeList from '../tack/coverflow-asset-type-list';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  ADD_ENTITY_BUTTON_CLICKED: "add_entity_button_clicked",
  ARTICLE_DETAIL_VIEW_SECTION_CLICKED: "article_detail_view_section_clicked",
  ENTITY_DETAIL_RELATIONSHIP_CONTEXT_NEXT_CLICKED: "entity_detail_relationship_context_next_clicked",
  ENTITY_DETAIL_RELATIONSHIP_CONTEXT_CANCEL_CLICKED: "entity_detail_relationship_context_cancel_clicked",
  ENTITY_DETAIL_RELATIONSHIP_SECTION_PAGINATION_CHANGED: "entity_detail_relationship_section_pagination_changed",
  ENTITY_DETAIL_RELATIONSHIP_CONTEXT_SAVE_CLICKED: "entity_detail_relationship_context_save_clicked",
  ENTITY_DETAIL_RELATIONSHIP_CONTEXT_CREATE_VARIANT_CLICKED: "entity_detail_relationship_context_select_variant_clicked",
};

const oPropTypes = {
  entityViewData: ReactPropTypes.object,
  sectionModel: ReactPropTypes.object,
  element: ReactPropTypes.object,
  relationshipViewMode: ReactPropTypes.object,
  ruleViolation: ReactPropTypes.object,
  relationshipContextData: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  xRayData: ReactPropTypes.object
};

// @CS.SafeComponent
class EntityDetailRelationshipSectionView extends React.Component {
  static propTypes = oPropTypes;

  handleRelationshipWrapperOnClick = (oElements, sSectionId, oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      oEvent.nativeEvent.dontRaise = true;
      var oEntityViewData = this.props.entityViewData;
      var oActiveEntity = oEntityViewData.activeEntity;
      var oRelationshipSide = oElements.relationshipSide;
      var sActiveEntityId = oActiveEntity.id;
      var sContext = 'relationshipContainerSelectionStatus';
      EventBus.dispatch(oEvents.ARTICLE_DETAIL_VIEW_SECTION_CLICKED, this, sActiveEntityId, sContext, sSectionId, oRelationshipSide);
    }
  };

  handleAddEntityButtonClicked = (sRelationshipId, oElement, oEvent) => {
    var oRelationshipSide = oElement.relationshipSide;
    EventBus.dispatch(oEvents.ADD_ENTITY_BUTTON_CLICKED, this, oElement.id, oRelationshipSide, oElement.type);
  };

  handlePaginationChanged = (sRelationshipId, oPaginationData) => {
    EventBus.dispatch(oEvents.ENTITY_DETAIL_RELATIONSHIP_SECTION_PAGINATION_CHANGED, sRelationshipId, oPaginationData);
  };

  getNavigationDataForRelationshipSectionLoadMore = (oRelationshipToolbarProps) => {
    var aContentList = [];
    try{
      aContentList = oRelationshipToolbarProps.elements;
    }catch (oException){
      ExceptionLogger.error("Error :" + oException);
      return {};
    }

    var oNavigationData = {};
    oNavigationData.from = oRelationshipToolbarProps.startIndex;
    oNavigationData.totalContents = oRelationshipToolbarProps.totalCount;
    oNavigationData.currentPageItems = aContentList.length;
    oNavigationData.pageSize = oRelationshipToolbarProps.paginationLimit;
    return oNavigationData;
  };

  getSectionElementLoadMoreView = (oElement) => {
    var sRelationshipId = oElement.id;
    var oNavigationData = this.getNavigationDataForRelationshipSectionLoadMore(oElement.relationshipToolbarProps);

    if(!oNavigationData.totalContents) {
      return;
    }

    return (
        <div className="relationshipLoadMoreContainer">
          <div className="paginationSection">
          <PaginationView
              currentPageItems={oNavigationData.currentPageItems}
              from={oNavigationData.from}
              pageSize={oNavigationData.pageSize}
              totalItems={oNavigationData.totalContents}
              onChange={this.handlePaginationChanged.bind(this, sRelationshipId)}
              displayTheme={Constants.DARK_THEME}
              filterContext={this.props.filterContext}
          />
          </div>
        </div>
    )
  };

  handleRelationshipContextNextButtonClicked = () => {
    let oNewFilterContextForQuickList = {
      filterType: oFilterPropType.QUICKLIST,
      screenContext: oFilterPropType.QUICKLIST
    };
    EventBus.dispatch(oEvents.ENTITY_DETAIL_RELATIONSHIP_CONTEXT_NEXT_CLICKED, oNewFilterContextForQuickList);
  };

  handleRelationshipContextSaveClicked = () => {
    let oElement = this.props.element;
    let oRelationship = oElement.relationship;
    EventBus.dispatch(oEvents.ENTITY_DETAIL_RELATIONSHIP_CONTEXT_SAVE_CLICKED, oRelationship.id);
  };

  handleRelationshipContextCreateVariantClicked = () => {
    let oElement = this.props.element;
    let oRelationship = oElement.relationship;
    EventBus.dispatch(oEvents.ENTITY_DETAIL_RELATIONSHIP_CONTEXT_CREATE_VARIANT_CLICKED, oRelationship.id);
  }

  onRelationshipDialogButtonClicked = (sButtonId) => {
    switch (sButtonId) {
      case "cancel":
      case "close":
        this.handleRelationshipContextCancelButtonClicked(sButtonId);
        break;

      case "select_variant":
      case "ok":
        this.handleRelationshipContextNextButtonClicked();
        break;

      case "create_variant":
        this.handleRelationshipContextCreateVariantClicked();
        break;

      case "save":
        this.handleRelationshipContextSaveClicked();
        break;


    }
  };

  handleRelationshipContextCancelButtonClicked = (sCancelButtonId) => {
    EventBus.dispatch(oEvents.ENTITY_DETAIL_RELATIONSHIP_CONTEXT_CANCEL_CLICKED, sCancelButtonId);
  };

  getImageSrcForThumbnail = (oContent) => {
    var aList = oContent.referencedAssets;
    let oImage = {};
    if(oContent.baseType == BaseTypeDictionary.assetBaseType) {
      oImage = oContent.assetInformation;
    } else {
      oImage = CS.find(aList, {isDefault: true});
    }
    if(CS.isEmpty(oImage)) {
      var sKlassId = ViewUtils.getEntityClassType(oContent);
      var oClass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
      var sThumbnailImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      var oImageObject = {};
      oImageObject.thumbnailImageSrc = sThumbnailImageSrc;
      oImageObject.thumbnailType = oClass.natureType;
      return oImageObject;
    }
    return ViewUtils.getImageSrcUrlFromImageObject(oImage);
  };

  getRelationshipElementsThumbView = (
    aRelationshipContentElementsList,
    sCurrentRelationshipId,
    aSelectedElementsOfRelationship,
    oElement,
  ) => {
    var _this = this;
    var __props = _this.props;
    var oEntityViewData = __props.entityViewData;
    var bDisableDelete = !oElement.canDelete || oElement.isDisabled;
    var sContextId = oElement.relationshipSide.contextId;
    let oRelationship = oElement.relationship;
    let aReferencedContexts = oEntityViewData.referencedContexts;
    let aReferencedVersionContexts = [];
    let sCurrentRelationshipType = "";
    let bIsUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();
    let bCanEditContextRelationship = oElement.canEditRelationshipContext;

    if(oRelationship){
      sCurrentRelationshipType  = oRelationship.relationshipType;

      if(oRelationship.relationshipType === RelationshipTypeDictionary.PRODUCT_VARIANT) {
        aReferencedVersionContexts = aReferencedContexts.productVariantContexts;
      } else {
        aReferencedVersionContexts = aReferencedContexts.relationshipVariantContexts;
      }
    }

    let oContextVersion = CS.find(aReferencedVersionContexts, {id: sContextId});
    let bContextPresent = oContextVersion && (!CS.isEmpty(oContextVersion.tags) || oContextVersion.isTimeEnabled);
    let bEnableEditRelationshipVariantContext = false;
    let bShowContextPreview = false;
    if (bContextPresent && !bIsUserReadOnly && bCanEditContextRelationship) {
      bEnableEditRelationshipVariantContext = true;
    } else if (bContextPresent && !bIsUserReadOnly && !bCanEditContextRelationship) {
      bShowContextPreview = true;
    }
    var aContentThumbnails = [];
    var aUserList = oEntityViewData.userList;
    var sThumbnailType = "";
    var oRelationshipToolbarProps = oElement.relationshipToolbarProps;

    CS.forEach(aRelationshipContentElementsList, function (oRelationshipContentElement) {
      var bIsActive = false;
      var sContentId = oRelationshipContentElement["id"];
      var bIsSelected = !!CS.find(aSelectedElementsOfRelationship, function (n) {
        return n == sContentId;
      });
      sThumbnailType = ViewUtils.getThumbnailTypeFromBaseType(oRelationshipContentElement.baseType);

      var sClassName = "";
      var sClassIcon = "";

      var sKlassId = ViewUtils.getEntityClassType(oRelationshipContentElement);
      var oClass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
      let sPreviewImageSrc = "";

      if (oClass) {
        sClassName = CS.getLabelOrCode(oClass);
        sClassIcon = oClass.icon;
        sPreviewImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      }

      var aThumbnailTags = oRelationshipContentElement.tags;
      var sThumbnailMode = ThumbnailModeConstants.BASIC;
       var oActiveXRayPropertyGroup = oElement.activeXRayPropertyGroup || {};

      if (oRelationshipToolbarProps.isXRayEnabled) {
        sThumbnailMode = ThumbnailModeConstants.XRAY;
       // aThumbnailTags = aRelationshipTagElementMap[sContentId];//TODO: check again
       }

      var oDataForThumbnail = _this.getImageSrcForThumbnail(oRelationshipContentElement);
      var sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc;
      var sImageType = oDataForThumbnail.thumbnailType;
      //TODO: Remove or condition when backend is ready
      var oUser = CS.find(aUserList, {id: oRelationshipContentElement.lastModifiedBy}) || {};
      var sLastModifiedBy = oUser.firstName + " " + oUser.lastName;

      var aIcons = [];
      var sBranchNumber = "";
      if (oRelationshipContentElement.variantOf != "-1") {
        aIcons.push("variantIcon");
      }
      if (oRelationshipContentElement.branchOf != "-1") {
        aIcons.push("branchIcon");
        sBranchNumber = oRelationshipContentElement.branchNo || "";
      }

      var sVersionText = sBranchNumber ? ("Version " + sBranchNumber) : "";

      let bIsArchive = ViewUtils.getIsArchive();

      var oThumbnailProperties = {
        entity: oRelationshipContentElement,
        entityType: ViewUtils.getEntityType(oRelationshipContentElement),
        isActive: bIsActive,
        isSelected: bIsSelected,
        isFolder: oRelationshipContentElement.isFolder,
        isDirty: Boolean(oRelationshipContentElement.isDirty) || Boolean(oRelationshipContentElement.isCreated),
        lastModifiedBy: sLastModifiedBy,
        isRelationship: true,
        isEntityRemovable: true,
        currentRelationshipId: sCurrentRelationshipId,
        thumbnailType: sThumbnailType,
        thumbnailMode: sThumbnailMode,
        versionText: sVersionText,
        icons: aIcons,
        classIcon: sClassIcon,
        xRayData: __props.xRayData,
        disableDelete: bDisableDelete,
        disableCheck: bDisableDelete,
        disableClone: true,
        containerClass: oClass ? oClass.natureType : "",
        enableEditRelationshipVariantContext: bEnableEditRelationshipVariantContext,
        activeXRayPropertyGroup: oActiveXRayPropertyGroup,
        relevanceValue: oRelationshipContentElement.relevance,
        disableDownload: true,
        disableAddToCollection: true,
        currentRelationshipType: sCurrentRelationshipType,
        viewContext: __props.sectionModel.properties.viewContext,
        disableRestore: !bIsArchive,
        previewImageSrc: sPreviewImageSrc,
        hideDeleteButton: true,
        showContextPreview: bShowContextPreview
      };

      var bListMode = (__props.relationshipViewMode[sCurrentRelationshipId] == ContentScreenConstants.viewModes.LIST_MODE);
      var oThumbnailModel = null;
      if (bListMode) {
        oThumbnailProperties.className = oRelationshipContentElement.isFolder ? 'folderThumb' : 'articleThumb';
        oThumbnailModel = new ListItemModel(
            sContentId,
            oRelationshipContentElement.name,
            sThumbnailImageSrc,
            aThumbnailTags,
            sImageType,
            sClassName,
            oThumbnailProperties);
      } else {
        oThumbnailModel = new ThumbnailModel(
            sContentId,
            oRelationshipContentElement.name,
            sThumbnailImageSrc,
            aThumbnailTags,
            sImageType,
            sClassName,
            oThumbnailProperties);
      }

      var bIsAsset = oRelationshipContentElement.baseType == BaseTypeDictionary.assetBaseType;
      var sViewMode = bIsAsset ? "b" : "a";
      var oRelationshipProps = oElement.relationshipToolbarProps || {};
      var oThumbnailView = bListMode ?
          (<ListItemView key={sContentId}
                         model={oThumbnailModel}
                         currentZoom={oRelationshipProps.currentZoom}/>) :
          (<ThumbnailView key={sContentId}
                          model={oThumbnailModel}
                          viewMode={sViewMode}
                          filterContext={__props.filterContext}
                          currentZoom={oRelationshipProps.currentZoom}/>);
      aContentThumbnails.push(oThumbnailView);
    });

    return aContentThumbnails;
  };

  getRelationshipToolbarView = (oElement) => {
    var __props = this.props;
    var sSideId = oElement.id;
    var oRelationshipToolbarProp = oElement.relationshipToolbarProps;
    var oEntityViewData = __props.entityViewData;
    var oDefaultSortViewData = oEntityViewData.sortByViewData;
    var oSortViewData = {};
    oSortViewData.activeSortingField = oRelationshipToolbarProp ? oRelationshipToolbarProp.sortField : oDefaultSortViewData.activeSortingField;
    oSortViewData.activeSortingOrder = oRelationshipToolbarProp ? oRelationshipToolbarProp.sortOrder : oDefaultSortViewData.activeSortingOrder;
    oSortViewData.sortMenuList = oDefaultSortViewData.sortMenuList;
    oSortViewData.attributeList = oDefaultSortViewData.attributeList;
    oSortViewData.isActive = oDefaultSortViewData.isActive;
    oSortViewData.context = "relationshipSort_" + sSideId;
    var oItemList = oElement.relationshipToolbar || {};
    var sSelectedViewMode = __props.relationshipViewMode[sSideId] == ContentScreenConstants.viewModes.LIST_MODE ? "toolView2" : "toolView1";
    let oAddEntityItem = oItemList.add_relationship_entities;
    if (oAddEntityItem) {
      oAddEntityItem[0].onChange = this.handleAddEntityButtonClicked.bind(this, oElement.relationship.id, oElement)
    }

    var oViewContext = {
      viewContext: __props.sectionModel.properties.viewContext,
      elementWorkingId: sSideId,
      selectedViewMode: sSelectedViewMode
    };

    return (<ToolbarView viewContext={oViewContext} toolbarData={oItemList} filterContext={this.props.filterContext}/>);
  };

  getRelationshipContextDetailView = (oRelationshipContextData) => {
    let oActiveContext = oRelationshipContextData.context;
    let bIsUnDeterminedContext = (CS.isEmpty(oActiveContext.tags) && !oActiveContext.isTimeEnabled);
    let oSide2NatureClassOfProductVariant = this.props.entityViewData.side2NatureClassOfProductVariant;
      if(bIsUnDeterminedContext){
      return null;
    }

    let sCancelButtonLabel = getTranslation().CLOSE;
    let sCancelButtonId = 'close';
    let aButtonData = [];

    let sSaveButtonLabel = getTranslation().SAVE;
    if(!oRelationshipContextData.isForSingleContent){
      let sNextButtonLabel = getTranslation().SELECT_VARIANT;
      if(oSide2NatureClassOfProductVariant.selectVariant && oActiveContext.type === ContextTypeDictionary.PRODUCT_VARIANT) {
        aButtonData.push({
          id: "select_variant",
          label: sNextButtonLabel,
          isDisabled: false,
          isFlat: false,
        });
      }
      if(oActiveContext.type === ContextTypeDictionary.RELATIONSHIP_VARIANT){
        aButtonData.push({
          id: "ok",
          label: getTranslation().OK,
          isDisabled: false,
          isFlat: false,
        });
      }

      let sSelectVariantButtonLabel = getTranslation().CREATE_VARIANT;
      let bShowCreateVariant = CS.isNotEmpty(oSide2NatureClassOfProductVariant) ? oSide2NatureClassOfProductVariant.id : null;
      if(CS.isNotEmpty(oActiveContext) && ViewUtils.isContextTypeProductVariant(oActiveContext.type) && bShowCreateVariant && oSide2NatureClassOfProductVariant.type != 'imageAsset') {
        aButtonData.push({
          id: "create_variant",
          label: sSelectVariantButtonLabel,
          isDisabled: false,
          isFlat: false,
        })
      }

      sCancelButtonLabel = getTranslation().CANCEL;
      sCancelButtonId = 'cancel'
    } else if (oRelationshipContextData.isDirty) {
      aButtonData.unshift({
        id: "save",
        label: sSaveButtonLabel,
        isDisabled: false,
        isFlat: false,
      });
    }

    aButtonData.unshift(
        {
          id: sCancelButtonId,
          label: sCancelButtonLabel,
          isDisabled: false,
          isFlat: true,
        });

    var oVariantSectionData = {};
    oVariantSectionData.dummyVariant = {
      tags : oRelationshipContextData.tags,
      timeRange : oRelationshipContextData.timeRange
    };
    oVariantSectionData.selectedVisibleContext = oRelationshipContextData.context;
    oVariantSectionData.viewContext = "entityDetailRelationSection";

    let oEntityViewData = this.props.entityViewData;
    let bIsDialogOpen = oEntityViewData.isRelationshipContextDialogOpen;
    let sContextType = ViewUtils.getContextTypeBasedOnContextId(oActiveContext.id);
    let sHeader =  getTranslation().RELATIONSHIP_CONTEXT_DETAILS;
    if(sContextType === ContextTypeDictionary.PRODUCT_VARIANT) {
      sHeader = getTranslation().PRODUCT_VARIANT_DETAILS;
    }

    return (
        <VariantTagGroupDialogView
             variantSectionViewData = {oVariantSectionData}
                            canEdit = {oRelationshipContextData.isVariantEditable}
                             header = {sHeader}
                onDialogButtonClick = {this.onRelationshipDialogButtonClicked}
                       isDialogOpen = {bIsDialogOpen}
             onClose={this.onRelationshipDialogButtonClicked.bind(this, "cancel")}
                         buttonData = {aButtonData}/>);
  };

  getArticleAssetRelationShipView = (oRelationshipView) => {
    let oSectionModel = this.props.sectionModel;
    let aAllowedFileTypes = CoverflowAssetTypeList.allTypes;
    let oElement = this.props.element;
    let sRelationshipSideId = oElement.relationshipElement.id;
    let sRequestURL =this.props.entityViewData.requestURLToGetDefaultTypesForRelationhsip;
    let oCollectionsDialog = (<UploadAssetDialogView assetClassList={oSectionModel.properties.assetClassList}
                                                     relationshipSideId={sRelationshipSideId}
                                                     context={AssetUploadContextDictionary.RELATIONSHIP_SECTION_DROP}
                                                     showBulkUploadDialog={oSectionModel.properties.showBulkUploadDialog}
                                                     requestURL={sRequestURL}/>);

    let oExtraData = {filterContext: this.props.filterContext};
    let bIsDragging = oElement.isDragging;
    let sRelationshipClassName = "relationshipDragView ";
    bIsDragging ? sRelationshipClassName += "isDragging" : null; // eslint-disable-line
    return (
        <div className="relationshipFileDragAndDrop">
          <FileDragAndDropView
              id="relationshipBulkAssetUpload"
              context={AssetUploadContextDictionary.RELATIONSHIP_SECTION_DROP}
              allowedFileTypes={aAllowedFileTypes}
              extraData={oExtraData}
              customMessage={getTranslation().DROP_HERE}
              isDragging={bIsDragging}
          >
          </FileDragAndDropView>
          <div className={sRelationshipClassName}>
            {oRelationshipView}
          </div>
          {oCollectionsDialog}
        </div>
    );
  };

  render() {
    var __props = this.props;
    var oSectionModel = __props.sectionModel;
    var oElement = __props.element;

    var aRelationshipContentElementsList = oElement.relationshipContentElementList;
    var aSelectedElementsOfRelationship = oElement.selectedElementsOfRelationship;
    var oRelationshipToolbarProps = oElement.relationshipToolbarProps;
    var aElementsThumbView = this.getRelationshipElementsThumbView(aRelationshipContentElementsList, oElement.relationshipElement.id, aSelectedElementsOfRelationship, oElement);

    var oProperties = {};
    oProperties.context = "contentSection";
    oProperties.isSelected = oElement.isSelected;
    oProperties.innerEntityCount = oRelationshipToolbarProps ? oRelationshipToolbarProps.totalCount : 0;

    var sSectionId = oSectionModel.id;
    var oToolbarView = this.getRelationshipToolbarView(oElement);
    var oRelationshipContextData = this.props.relationshipContextData;
    let bShowVariantTagGroupDialog = (oRelationshipContextData.relationshipId == sSectionId && oRelationshipContextData.isEditVariantSectionOpen);
    var oRelationshipVariantTagGroupView = null;
    if(bShowVariantTagGroupDialog) {
      oRelationshipVariantTagGroupView = this.getRelationshipContextDetailView(oRelationshipContextData);
    }
    var oRuleViolationBody = "";
    var aRuleViolation = __props.ruleViolation;
    var oRelationship = oElement.relationship;
    var sRelationshipId = oRelationship.id;
    if (!CS.isEmpty(aRuleViolation)) {
      var aCurrentRuleViolation = aRuleViolation[sRelationshipId];
      if (!CS.isEmpty(aCurrentRuleViolation)) {
        oRuleViolationBody = (<RuleViolationView ruleViolation={aRuleViolation} elements={oElement}/>);
      }
    }
    /****** Extra padding added for list view and tile view. To avoid the overlapping of navigation bar. *********/
    var sRelationshipPaperWrapperClass = "relationshipPaperWrapper extraPadding ";

    if (CS.isEmpty(aElementsThumbView)) {
      aElementsThumbView = <NothingFoundView message={getTranslation().NOTHING_TO_DISPLAY}/>
    }

    let oRelationshipView =
        (<div>
          {oToolbarView}
          <div className={sRelationshipPaperWrapperClass} data-id={sRelationshipId}
               onClick={this.handleRelationshipWrapperOnClick.bind(this, oElement, sRelationshipId)}>
            {oRuleViolationBody}
            <div className="articleEditViewThumbsContainer">
              <div className="articleThumbnailContainer" key="article" data-container-type="thumbnailContainer">
                {aElementsThumbView}
              </div>
            </div>
            {this.getSectionElementLoadMoreView(oElement)}
            {oRelationshipVariantTagGroupView}
          </div>
        </div>);


    if (oElement.showBulkAssetUploadInRelationship) {
      oRelationshipView = this.getArticleAssetRelationShipView(oRelationshipView);
    }

    return (
        <div className="articleRelationshipPaper">
          {oRelationshipView}
        </div>);
  }

}



export const view = EntityDetailRelationshipSectionView;
export const events = oEvents;
