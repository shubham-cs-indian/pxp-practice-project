import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from '../store/helper/screen-mode-utils';

import ThumbnailModel from '../../../../../viewlibraries/thumbnailview2/model/thumbnail-model';
import { view as DropView } from '../../../../../viewlibraries/dragndropview/drop-view.js';
import DropViewModel from '../../../../../viewlibraries/dragndropview/model/drop-view-model';
import { view as PaginationView } from '../../../../../viewlibraries/paginationview/pagination-view';
import { view as ThumbnailTemplateViewNew } from '../../../../../viewlibraries/thumbnailview2/templates/thumbnail-template-view-new';
import { view as ButtonView } from '../../../../../viewlibraries/buttonview/button-view';
import ViewUtils from './utils/view-utils';
import Constants from '../../../../../commonmodule/tack/constants';
import { view as ContentTileListView } from './content-tile-list-view';
let getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  ENTITY_RELATIONSHIP_OK_BUTTON_CLICKED: "entity_relationship_ok_button_clicked",
  ENTITY_RELATIONSHIP_DELETE_BUTTON_CLICKED: "entity_relationship_delete_button_clicked",
  ENTITY_RELATIONSHIP_SELECT_ALL_BUTTON_CLICKED: "entity_relationship_select_all_button_clicked",
  ENTITY_RELATIONSHIP_ADD_PAGINATION_CHANGED: "entity_relationship_add_pagination_changed"
};

const oPropTypes = {
  entityViewData: ReactPropTypes.object,
  selectedSectionId: ReactPropTypes.string,
  collectionData: ReactPropTypes.object,
  presentEntityNavigationData: ReactPropTypes.object,
  variantSectionViewData: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
};

// @CS.SafeComponent
class QuickListView extends React.Component {
  static propTypes = oPropTypes;

  handleOkButtonClicked = () => {
    let sContext = "";
    let oCollectionData = this.props.collectionData;
    let oEntityViewData = this.props.entityViewData;
    let bAvailableEntityViewVisibilityStatus = oEntityViewData.availableEntityViewVisibilityStatus;
    let bAvailableEntityParentViewVisibilityStatus = oEntityViewData.availableEntityParentViewVisibilityStatus;
    if (oCollectionData && oCollectionData.addEntityInCollectionViewStatus) {
      sContext = "staticCollection";
    } else if (bAvailableEntityViewVisibilityStatus) {
      sContext = "contextEntity";
    } else if (bAvailableEntityParentViewVisibilityStatus) {
      sContext = "manageVariantLinkedInstances";
    }
    EventBus.dispatch(oEvents.ENTITY_RELATIONSHIP_OK_BUTTON_CLICKED, sContext);
  };

  handleSelectAllButtonClicked = (sRelationshipId, sViewContext) => {
    EventBus.dispatch(oEvents.ENTITY_RELATIONSHIP_SELECT_ALL_BUTTON_CLICKED, this, sRelationshipId, sViewContext);
  };

  handleDeleteButtonClicked = (sSelectedSectionModelId, sViewContext) => {
    EventBus.dispatch(oEvents.ENTITY_RELATIONSHIP_DELETE_BUTTON_CLICKED, sSelectedSectionModelId, sViewContext, this.props.filterContext);
  };

  handlePaginationChanged = (sRelationshipId, oPaginationData, oFilterContext) => {
    EventBus.dispatch(oEvents.ENTITY_RELATIONSHIP_ADD_PAGINATION_CHANGED, sRelationshipId, oPaginationData, oFilterContext);
  };

  getRelationshipElementsThumbView = (aRelationshipContentElementsList, sCurrentRelationshipId, aSelectedElementsOfRelationship, oElement, bForceNature) => {
    let _this = this;
    let __props = _this.props;
    let oEntityViewData = __props.entityViewData;
    let aContentThumbnails = [];
    let aUserList = oEntityViewData.userList;
    let sThumbnailType = "article";
    let bDisableDelete = !oElement.canDelete;

    CS.forEach(aRelationshipContentElementsList, function (oRelationshipContentElement) {
      if (CS.isEmpty(oRelationshipContentElement)) {
        return;
      }
      let bIsActive = false;
      let sContentId = oRelationshipContentElement["id"];
      let bIsSelected = CS.find(aSelectedElementsOfRelationship, function (n) {
        return n == sContentId;
      }) ? true : false;
      sThumbnailType = ViewUtils.getThumbnailTypeFromBaseType(oRelationshipContentElement.baseType);

      let sClassName = "";
      let sClassIcon = "";
      let sElementType = ViewUtils.getEntityClassType(oRelationshipContentElement);
      let oClass = ViewUtils.getKlassFromReferencedKlassesById(sElementType);
      if (oClass) {
        sClassName = CS.getLabelOrCode(oClass);
        sClassIcon = oClass.icon;
      }

      let oDataForThumbnail = _this.getImageSrcForThumbnail(oRelationshipContentElement, oClass.natureType);
      let sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc;
      let sImageType = oDataForThumbnail.thumbnailType;
      let sPreviewImageSrc = "";
      if (oClass) {
        sPreviewImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      }
      if (!sThumbnailImageSrc) {
        sThumbnailImageSrc = sPreviewImageSrc;
      }
      //TODO: Remove or condition when backend is ready
      let oUser = CS.find(aUserList, { id: oRelationshipContentElement.lastModifiedBy }) || {};
      let sLastModifiedBy = oUser.firstName + " " + oUser.lastName;

      let aIcons = [];
      let sBranchNumber = "";
      if (oRelationshipContentElement.variantOf != "-1") {
        aIcons.push("variantIcon");
      }
      if (oRelationshipContentElement.branchOf != "-1") {
        aIcons.push("branchIcon");
        sBranchNumber = oRelationshipContentElement.branchNo || "";
      }

      let sVersionText = sBranchNumber ? ("Version " + sBranchNumber) : "";
      let sRelationshipType = "";
      if (!bForceNature) {
        sRelationshipType = oElement.relationship.relationshipType
      }

      let sViewContext = "";
      let oSelectedSectionModel = CS.find(oEntityViewData.sectionViewModel, {
        id: __props.selectedSectionId
      });

      if (!CS.isEmpty(oSelectedSectionModel)) {
        sViewContext = oSelectedSectionModel.properties.viewContext;
      }
      let bIsArchive = ViewUtils.getIsArchive();
      let oThumbnailProperties = {
        entity: oRelationshipContentElement,
        entityType: "article",
        isActive: bIsActive,
        isSelected: bIsSelected,
        isFolder: oRelationshipContentElement.isFolder,
        isDirty: Boolean(oRelationshipContentElement.isDirty) || Boolean(oRelationshipContentElement.isCreated),
        lastModifiedBy: sLastModifiedBy,
        isRelationship: true,
        currentRelationshipId: sCurrentRelationshipId,
        thumbnailType: sThumbnailType,
        versionText: sVersionText,
        icons: aIcons,
        classIcon: sClassIcon,
        isHorizontal: true,
        disableDelete: bDisableDelete,
        currentRelationshipType: sRelationshipType,
        viewContext: sViewContext,
        disableClone: true,
        disableView: false,
        disableCheck: false,
        disableDownload: true,
        disableAddToCollection: true,
        isEntityRemovable: true,
        disableRestore: !bIsArchive,
        previewImageSrc: sPreviewImageSrc,
        hideDeleteButton: true

      };

      let bListMode = false;
      let oThumbnailModel = null;
      if (bListMode) {
      } else {
        oThumbnailModel = new ThumbnailModel(
          sContentId,
          oRelationshipContentElement.name,
          sThumbnailImageSrc,
          oRelationshipContentElement.tags,
          sImageType,
          sClassName,
          oThumbnailProperties);
      }

      let oThumbnailView = bListMode ? null :
        <ThumbnailTemplateViewNew
          id={sContentId}
          name={oRelationshipContentElement.name}
          className={sClassName}
          imageSrc={sThumbnailImageSrc}
          type={sImageType}
          tags={oRelationshipContentElement.tags}
          thumbnailViewModel={oThumbnailModel}
          zoomLevel={1}
          filterContext={__props.filterContext}
        />;

      aContentThumbnails.push(oThumbnailView);
    });

    return aContentThumbnails;
  };

  getContextEntityElementsThumbView = (aContextEntityElementsList) => {
    let _this = this;
    let __props = _this.props;
    let oEntityViewData = __props.entityViewData;
    let aContentThumbnails = [];
    let aUserList = oEntityViewData.userList;
    let sThumbnailType = "article";
    let aSelectedEntityList = oEntityViewData.selectedVariantIds;

    CS.forEach(aContextEntityElementsList, (oContextEntityElement) => {
      if (CS.isEmpty(oContextEntityElement)) {
        return;
      }
      let bIsActive = false;
      let sContentId = oContextEntityElement["id"];
      sThumbnailType = ViewUtils.getThumbnailTypeFromBaseType(oContextEntityElement.baseType);

      let sClassName = "";
      let sClassIcon = "";
      let sElementType = ViewUtils.getEntityClassType(oContextEntityElement);
      let oClass = ViewUtils.getKlassFromReferencedKlassesById(sElementType);

      if (oClass) {
        sClassName = CS.getLabelOrCode(oClass);
        sClassIcon = oClass.icon;
      }

      let bIsSelected = !!CS.find(aSelectedEntityList, function (oSelectedEntity) { return oSelectedEntity.id == sContentId; });

      let oDataForThumbnail = _this.getImageSrcForThumbnail(oContextEntityElement);
      let sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc;
      let sImageType = oDataForThumbnail.thumbnailType;
      if (!sThumbnailImageSrc && oClass) {
        sThumbnailImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      }
      //TODO: Remove or condition when backend is ready
      let oUser = CS.find(aUserList, { id: oContextEntityElement.lastModifiedBy }) || {};
      let sLastModifiedBy = oUser.firstName + " " + oUser.lastName;

      let aIcons = [];
      let sBranchNumber = "";
      if (oContextEntityElement.variantOf != "-1") {
        aIcons.push("variantIcon");
      }
      if (oContextEntityElement.branchOf != "-1") {
        aIcons.push("branchIcon");
        sBranchNumber = oContextEntityElement.branchNo || "";
      }

      let sVersionText = sBranchNumber ? ("Version " + sBranchNumber) : "";
      let bIsArchive = ViewUtils.getIsArchive();

      let oThumbnailProperties = {
        entity: oContextEntityElement,
        entityType: "article",
        isActive: bIsActive,
        isSelected: bIsSelected,
        isFolder: oContextEntityElement.isFolder,
        isDirty: Boolean(oContextEntityElement.isDirty) || Boolean(oContextEntityElement.isCreated),
        lastModifiedBy: sLastModifiedBy,
        isRelationship: true,
        thumbnailType: sThumbnailType,
        versionText: sVersionText,
        icons: aIcons,
        classIcon: sClassIcon,
        isHorizontal: true,
        disableDelete: false,
        disableView: true,
        isEntityRemovable: true,
        disableClone: true,
        disableCheck: false,
        disableDownload: true,
        disableAddToCollection: true,
        disableRestore: !bIsArchive,
        hideDeleteButton: true,
        hideVariantOfIcon: true
      };

      let bListMode = false;
      let oThumbnailModel = null;
      if (bListMode) {
      } else {
        oThumbnailModel = new ThumbnailModel(
          sContentId,
          oContextEntityElement.name,
          sThumbnailImageSrc,
          oContextEntityElement.tags,
          sImageType,
          sClassName,
          oThumbnailProperties);
      }

      let oThumbnailView = bListMode ? null :
        <ThumbnailTemplateViewNew
          id={sContentId}
          name={oContextEntityElement.name}
          className={sClassName}
          imageSrc={sThumbnailImageSrc}
          type={sImageType}
          tags={oContextEntityElement.tags}
          thumbnailViewModel={oThumbnailModel}
          zoomLevel={1}
          filterContext={this.props.filterContext}
        />;

      aContentThumbnails.push(oThumbnailView);
    });

    return aContentThumbnails;
  };

  getPresentEntityThumbsWithEntityList = () => {
    let _this = this;
    let __props = _this.props;
    let oEntityViewData = __props.entityViewData;

    let aUserList = oEntityViewData.userList;
    let aEntityList = oEntityViewData.entityList;
    let aSelectedEntityList = oEntityViewData.selectedEntityList;

    let aContentThumbnails = [];
    let sThumbnailType = "article";
    CS.forEach(aEntityList, function (oEntity) {
      let bIsActive = false;
      let sEntityId = oEntity["id"];
      let bIsSelected = !!CS.find(aSelectedEntityList, { id: sEntityId });

      sThumbnailType = ViewUtils.getThumbnailTypeFromBaseType(oEntity.baseType);
      let sType = ViewUtils.getEntityClassType(oEntity);
      let sClassName = "";
      let sClassIcon = "";

      let oDataForThumbnail = _this.getImageSrcForThumbnail(oEntity);
      let sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc;
      let sImageType = oDataForThumbnail.thumbnailType;

      /** To set preview image if default image is not present in variants, content relationships & relationship tab **/
      let oReferencedClass = ViewUtils.getKlassFromReferencedKlassesById(sType);

      if (CS.isEmpty(sThumbnailImageSrc) && oReferencedClass) {
        sClassName = oReferencedClass.label;
        sClassIcon = oReferencedClass.icon;
        sThumbnailImageSrc = oReferencedClass.previewImage ? ViewUtils.getIconUrl(oReferencedClass.previewImage) : "";
      }

      //TODO: Remove or condition when backend is ready
      let oUser = CS.find(aUserList, { id: oEntity.lastModifiedBy }) || {};
      let sLastModifiedBy = oUser.firstName + " " + oUser.lastName;

      let aIcons = [];
      let sBranchNumber = "";
      if (oEntity.variantOf != "-1") {
        aIcons.push("variantIcon");
      }
      if (oEntity.branchOf != "-1") {
        aIcons.push("branchIcon");
        sBranchNumber = oEntity.branchNo || "";
      }

      let sVersionText = sBranchNumber ? ("Version " + sBranchNumber) : "";
      let bIsArchive = ViewUtils.getIsArchive();

      let oThumbnailProperties = {
        entity: oEntity,
        entityType: "article",
        isActive: bIsActive,
        isSelected: bIsSelected,
        isFolder: oEntity.isFolder,
        isDirty: Boolean(oEntity.isDirty) || Boolean(oEntity.isCreated),
        lastModifiedBy: sLastModifiedBy,
        isRelationship: true,
        thumbnailType: sThumbnailType,
        versionText: sVersionText,
        icons: aIcons,
        classIcon: sClassIcon,
        isHorizontal: true,
        disableClone: true,
        disableView: true,
        disableCheck: false,
        disableDownload: true,
        disableAddToCollection: true,
        isEntityRemovable: true,
        disableRestore: !bIsArchive,
        hideDeleteButton: true,
        hideVariantOfIcon: true
      };

      let oThumbnailModel = new ThumbnailModel(
        sEntityId,
        oEntity.name,
        sThumbnailImageSrc,
        oEntity.tags,
        sImageType,
        sClassName,
        oThumbnailProperties);

      let oThumbnailView = (
        <ThumbnailTemplateViewNew
          id={sEntityId}
          name={oEntity.name}
          className={sClassName}
          imageSrc={sThumbnailImageSrc}
          type={sImageType}
          tags={oEntity.tags}
          thumbnailViewModel={oThumbnailModel}
          zoomLevel={1}
          filterContext={__props.filterContext}
        />
      );

      aContentThumbnails.push(oThumbnailView);
    });

    return {
      presentThumbs: aContentThumbnails,
      sectionLabel: "",
      selectedEntities: aSelectedEntityList,
      canDelete: true,//bCanDelete,
      canAdd: true//bCanAdd
      //IMP: static collection does not have any permission check on UI. BackEnd is handling this scenario
    };
  };

  getPresentEntityThumbsForVariantNatureRelationshipWithSectionLabel = (oSectionViewData, sContext) => {
    let _this = this;

    let oNatureRelationship = oSectionViewData.natureRelationship;
    let aElementIds = oNatureRelationship.elementIds;
    let aNatureRelationshipInstanceElements = oSectionViewData.natureRelationshipInstanceElements;

    let aRelationshipContentElementsList = [];
    CS.forEach(aElementIds, function (sElementId) {
      let oElement = CS.find(aNatureRelationshipInstanceElements, { id: sElementId });
      aRelationshipContentElementsList.push(oElement);
    });

    let aSelectedElementsOfRelationship = oSectionViewData.selectedNatureElements;
    let oElement = {};
    if (sContext == "natureRelationship") {
      oElement.canDelete = oSectionViewData.natureRelationshipElement ? oSectionViewData.natureRelationshipElement.canDelete : !oSectionViewData.isReadOnly;
      oElement.canCreate = !oSectionViewData.isReadOnly;
    }
    let aElementsThumbView = _this.getRelationshipElementsThumbView(aRelationshipContentElementsList,
      oNatureRelationship.relationshipId || oNatureRelationship.id, aSelectedElementsOfRelationship, oElement, true);

    return {
      presentThumbs: aElementsThumbView,
      sectionLabel: CS.getLabelOrCode(oNatureRelationship),
      selectedEntities: aSelectedElementsOfRelationship,
      canDelete: oElement.canDelete,
      canAdd: oElement.canAdd
    }
  };

  getPresentEntityThumbsWithSectionLabelNatureVariantCommon = (sContext) => {
    let oSectionData = {};
    if (sContext == "natureRelationship") {
      let oEntityViewData = this.props.entityViewData;
      oSectionData = oEntityViewData.natureSectionViewData;
    } else if (sContext == "variantRelationship") {
      oSectionData = this.props.variantSectionViewData;
    }

    return this.getPresentEntityThumbsForVariantNatureRelationshipWithSectionLabel(oSectionData, sContext)
  };

  getPresentEntityThumbsWithSectionLabel = () => {
    let _this = this;
    let __props = _this.props;

    let oEntityViewData = __props.entityViewData;
    let oAddEntityInRelationshipScreenData = oEntityViewData.addEntityInRelationshipScreenData;
    let sActiveEntityId = oEntityViewData.activeEntity.id;

    let oNatureSectionViewData = oEntityViewData.natureSectionViewData;
    let bNatureRelationshipAddViewStatus = oNatureSectionViewData.natureRelationshipAddViewStatus;

    let sRelationshipId = oAddEntityInRelationshipScreenData[sActiveEntityId].relationshipId;
    let sRelationshipType = ViewUtils.getRelationshipTypeById(sRelationshipId);

    if (bNatureRelationshipAddViewStatus && ViewUtils.isNatureRelationship(sRelationshipType)) {
      return this.getPresentEntityThumbsWithSectionLabelNatureVariantCommon("natureRelationship");
    } else {
      let aSectionViewModel = oEntityViewData.sectionViewModel;
      let sSelectedSectionModelId = __props.selectedSectionId;

      let sSectionLabel = "";
      let aElementsThumbView = null;
      let aSelectedElementsOfRelationship = [];
      let bCanDelete = true;
      let bCanAdd = true;
      CS.forEach(aSectionViewModel, function (oSectionModel) {
        CS.forEach(oSectionModel.elements, function (oElement) {

          if (oElement.type == "relationship"  && sSelectedSectionModelId == oElement.id) {
            let oRelationship = oElement.relationship;
            sSectionLabel = CS.getLabelOrCode(oRelationship);
            let aRelationshipContentElementsList = oElement.relationshipContentElementList;
            aSelectedElementsOfRelationship = oEntityViewData.selectedRelationshipElements[oElement.id];
            bCanDelete = oElement.canDelete;
            bCanAdd = oElement.canAdd;
            aElementsThumbView = _this.getRelationshipElementsThumbView(aRelationshipContentElementsList, oElement.id, aSelectedElementsOfRelationship, oElement);
          }
        });

      });

      return {
        presentThumbs: aElementsThumbView,
        sectionLabel: sSectionLabel,
        selectedEntities: aSelectedElementsOfRelationship,
        canDelete: bCanDelete,
        canAdd: bCanAdd
      };
    }
  };

  getPresentContextEntityThumbsWithSectionLabel = () => {
    let _this = this;
    let __props = _this.props;

    let oEntityViewData = __props.entityViewData;
    let aSelectedEntityList = oEntityViewData.selectedVariantIds;
    let aElementsThumbView = _this.getContextEntityElementsThumbView(oEntityViewData.contextSelectedEntities);
    return {
      presentThumbs: aElementsThumbView,//aElementsThumbView,
      sectionLabel: '',//sSectionLabel,
      selectedEntities: aSelectedEntityList,//aSelectedElementsOfRelationship,
      canDelete: true,//bCanDelete,
      canAdd: true//bCanAdd
    };
  };

  getSectionElementLoadMoreView = () => {
    let __props = this.props;
    let oNavigationData = __props.presentEntityNavigationData;
    if (CS.isEmpty(oNavigationData) || oNavigationData.to == 0 || oNavigationData.totalContents == 0) {
      return null;
    }

    let sRelationshipId = "";
    if (CS.isEmpty(__props.collectionData)) { //if not collection means it is for relationship.
      sRelationshipId = __props.selectedSectionId;//oElement.relationship.id;
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
            isMiniPaginationView={true}
            displayTheme={Constants.DARK_THEME}
            filterContext={this.props.filterContext}
          />
        </div>
      </div>
    )
  };

  presentEntitiesView = () => {
    let oCollectionData = this.props.collectionData;
    let oActiveCollection = !CS.isEmpty(oCollectionData) ? oCollectionData.activeCollection : {};
    let oEntityViewData = this.props.entityViewData;
    let bAvailableEntityViewVisibilityStatus = oEntityViewData.availableEntityViewVisibilityStatus;
    let bAvailableEntityParentViewVisibilityStatus = oEntityViewData.availableEntityParentViewVisibilityStatus;
    let oPresentEntityData = {};
    let sDropContext = "";
    let bShowRemoveButton = false;      // default - show delete button

    if (!CS.isEmpty(oActiveCollection) && oActiveCollection.type == "staticCollection") {
      sDropContext = "staticCollection";
      bShowRemoveButton = true;
      oPresentEntityData = this.getPresentEntityThumbsWithEntityList();
    } else if (!CS.isEmpty(oEntityViewData.availableEntityViewContext)) {
      sDropContext = oEntityViewData.availableEntityViewContext;
      bShowRemoveButton = true;
      if (sDropContext === "productVariantQuickList") {
        oPresentEntityData = this.getPresentEntityThumbsWithSectionLabel();
      }
      else {
        oPresentEntityData = this.getPresentContextEntityThumbsWithSectionLabel();
      }
    } else if (bAvailableEntityViewVisibilityStatus) {
      sDropContext = "contextEntity";
      bShowRemoveButton = true;
      oPresentEntityData = this.getPresentContextEntityThumbsWithSectionLabel();
    } else if (bAvailableEntityParentViewVisibilityStatus) {
      sDropContext = "manageVariantLinkedInstances";
      bShowRemoveButton = true;
      oPresentEntityData = this.getPresentContextEntityThumbsWithSectionLabel();
    }
    else {
      sDropContext = "relationshipEntity";
      bShowRemoveButton = true;
      oPresentEntityData = this.getPresentEntityThumbsWithSectionLabel();
    }

    let sViewContext = "";
    let oSelectedSectionModel = CS.find(oEntityViewData.sectionViewModel, {
      id: this.props.selectedSectionId
    });
    if (!CS.isEmpty(oSelectedSectionModel)) {
      sViewContext = oSelectedSectionModel.properties.viewContext;
    } else {
      sViewContext = sDropContext;
    }

    let sSectionLabel = oPresentEntityData.sectionLabel;
    let aPresentEntityHorizontalThumbs = oPresentEntityData.presentThumbs;
    let bCanDelete = oPresentEntityData.canDelete;
    let aSelectedEntities = oPresentEntityData.selectedEntities || [];  /** Sometimes "selectedEntities" is undefined. **/

    let sSelectedSectionModelId = this.props.selectedSectionId;
    let oActiveEntity = oEntityViewData.activeEntity || {};
    let sEntityType = oActiveEntity.type;
    let oDropViewModel = new DropViewModel(sEntityType, true, [sDropContext], {});
    let sActiveEntityLabel = !CS.isEmpty(oActiveCollection) ? CS.getLabelOrCode(oActiveCollection) : CS.getLabelOrCode(oEntityViewData.activeEntity);
    let sDeleteButtonClassName = "button ";
    sDeleteButtonClassName += bShowRemoveButton ? "remove" : "delete";
    let sTooltip = bShowRemoveButton ? getTranslation().REMOVE : getTranslation().DELETE;
    let oDeleteButtonDom = (CS.isEmpty(aSelectedEntities) || !bCanDelete) ? null :
      (<div className="deleteButtonContainer">
        <ButtonView id={'delete'}
          showLabel={false}
          tooltip={sTooltip}
          isDisabled={false}
          className={sDeleteButtonClassName}
          type={"simple"}
          onChange={this.handleDeleteButtonClicked.bind(this, sSelectedSectionModelId, sViewContext)}
          theme={"light"} />
      </div>);

    let oBackButtonContainer = oEntityViewData.isBackButtonHidden ? null : (
      <div className="okButtonContainer">
        <ButtonView id={'ok'}
          showLabel={false}
          tooltip={getTranslation().BACK}
          isDisabled={false}
          className={"okButton"}
          type={"simple"}
          onChange={this.handleOkButtonClicked}
          theme={"light"} />
      </div>);

    let sTranslations = getTranslation().SELECT_ALL;
    let sSelectAllClassName = "toolCheckAll";
    if (aSelectedEntities.length) {
      sTranslations = getTranslation().DESELECT_ALL;
      sSelectAllClassName = aPresentEntityHorizontalThumbs.length === aSelectedEntities.length ? "toolUnCheckAll " : 'toolHalfChecked ';
    }


    /**IMP: bCanDelete condition is added because, in case of bundles,
     * there is no any permission handling on quick list right panel,
     * so delete icons is disabled and if delete icon is disabled
     * then there no any need of select all icon*/
    let oSelectAllButtonDOM = (!CS.isEmpty(aPresentEntityHorizontalThumbs) && bCanDelete) ?
      (<div className="toolbarItemWrapper" key={sSelectAllClassName}>
        <ButtonView id={'selectAll'}
          showLabel={false}
          tooltip={sTranslations}
          isDisabled={false}
          className={sSelectAllClassName}
          type={"simple"}
          onChange={this.handleSelectAllButtonClicked.bind(this, sSelectedSectionModelId, sViewContext)}
          theme={"light"}
          selectedItemsCount={aSelectedEntities.length} />
      </div>) : null;

    let aSectionLabel = [sActiveEntityLabel];
    if (sSectionLabel) {
      aSectionLabel.push(<span className="smallArrow"></span>);
      aSectionLabel.push(sSectionLabel);
    }

    return (
      <div className="presentEntityContainer">
        <div className="upperBlockContainer">
          {oBackButtonContainer}
          {oSelectAllButtonDOM}
          {oDeleteButtonDom}
        </div>
          <div className="lowerEntityListContainer">
            <DropView model={oDropViewModel} filterContext={this.props.filterContext}>
              {aPresentEntityHorizontalThumbs}
            </DropView>
          </div>
        {this.getSectionElementLoadMoreView()}
      </div>
    );
  };

  getImageSrcForThumbnail = (oContent, sNatureType) => {
    let oVariantSectionViewData = this.props.variantSectionViewData;
    let oEntityViewData = this.props.entityViewData;
    let oImage = null;
    let aList = [];
    if (oContent.defaultAssetInstanceId) {
      aList = oVariantSectionViewData && oVariantSectionViewData.assetRelationshipEntities;
      if (oContent.referencedAssets) {
        aList = CS.concat(aList, oContent.referencedAssets);
      } else if (oEntityViewData.hasOwnProperty('referencedAssets')) {
        aList = CS.concat(aList, oEntityViewData.referencedAssets);
      }
      oImage = CS.find(aList, { assetInstanceId: oContent.defaultAssetInstanceId });
    } else {
      oImage = oContent.assetInformation;
    }

    let oImageData = ViewUtils.getImageSrcUrlFromImageObject(oImage);
    if (CS.isEmpty(oImageData.thumbnailType)) {
      oImageData.thumbnailType = sNatureType;
    }
    return oImageData;
  };

  getAvailableEntityView(oAvailableEntityData) {
    return (<ContentTileListView
      {...oAvailableEntityData}
    />);
  }

  render() {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    let oAvailableEntityView = this.getAvailableEntityView(oEntityViewData.availableEntityData);

    return (
      <div className="addEntityInRelationshipWrapper">
        <div className="entityRelationshipAddContainer">
          {oAvailableEntityView}
          {this.presentEntitiesView()}
        </div>
      </div>
    );
  }
}

export const view = QuickListView;
export const events = oEvents;
