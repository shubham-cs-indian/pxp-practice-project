
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as ThumbnailScrollerView } from '../../../../../viewlibraries/thumbnail-scroller/thumbnail-scroller';
import ThumbnailModel from '../../../../../viewlibraries/thumbnailview2/model/thumbnail-model';
import ListItemModel from '../../../../../viewlibraries/detailedlistview/model/detailed-list-item-model';
import BaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import RelationshipConstants from '../../../../../commonmodule/tack/relationship-constants';
import ViewUtils from './utils/view-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  ARTICLE_DETAIL_VIEW_SECTION_CLICKED: "article_detail_view_section_clicked",
  ADD_ENTITY_BUTTON_CLICKED: "add_entity_button_clicked",
  MAKE_DEFAULT_ASSET_BUTTON_CLICKED: "make_default_asset_button_clicked"
};

const oPropTypes = {
  element: ReactPropTypes.object,
  relationshipContextData: ReactPropTypes.object
};

// @CS.SafeComponent
class RelationshipSectionElementView extends React.Component {
  constructor(props) {
    super(props);

    this.setDefaultImage = React.createRef();
  }

  static propTypes = oPropTypes;
  activeItemId = 0;

  componentDidMount() {
    this.updateDefaultSelection(this.activeItemId);
  }

  componentDidUpdate() {
    this.updateDefaultSelection(this.activeItemId);
  }

  updateDefaultSelection = (iActiveItemIndex) => {
    var oElement = this.props.element;
    var aRelationshipContentElementsList = oElement.relationshipContentElementList;
    var oActiveElement = aRelationshipContentElementsList[iActiveItemIndex] || {};
    var sActiveItemId = oActiveElement.id;
    if(this.setDefaultImage.current){
      if(oElement.defaultAssetInstanceId == sActiveItemId){
        this.setDefaultImage.current.classList.add("active");
      } else {
        this.setDefaultImage.current.classList.remove("active");
      }
    }
  };

  setActiveItem = (iActiveIndex) => {
    this.updateDefaultSelection(iActiveIndex);
    this.activeItemId = iActiveIndex;
  };

  getImageSrcForThumbnail = (oContent) => {
    var aList = oContent.referencedAssets;
    if(oContent.baseType == BaseTypeDictionary.assetBaseType) {
      aList = oContent.attributes;
    }
    var oImage = CS.find(aList, {isDefault: true});

    return ViewUtils.getImageSrcUrlFromImageObject(oImage);
  };

  getRelationshipElementsThumbView = (
    aRelationshipContentElementsList,
    sCurrentRelationshipId,
    aSelectedElementsOfRelationship,
    oElement,
  ) => {
    var _this = this;
    var aContentThumbnails = [];
    var aUserList = ViewUtils.getUserList();
    var sThumbnailType = "";
    var bIsSectionDisabled = oElement.isDisabled;
    var oRelationshipProps = oElement.relationshipToolbarProps  || {};

    CS.forEach(aRelationshipContentElementsList, function (oRelationshipContentElement, iIndex) {
      var bIsActive = false;
      var sContentId = oRelationshipContentElement["id"];
      if(iIndex == 0 && _this.activeItemId == "") {
        // _this.activeItemId = sContentId;
        _this.activeItemId = iIndex;
      }
      var bIsSelected = !!CS.find(aSelectedElementsOfRelationship, function (n) {
        return n == sContentId;
      });
      sThumbnailType = ViewUtils.getThumbnailTypeFromBaseType(oRelationshipContentElement.baseType);
      var sElementType = ViewUtils.getEntityClassType(oRelationshipContentElement);
      var oClass = ViewUtils.getKlassFromReferencedKlassesById(sElementType);
      var sClassName = CS.getLabelOrCode(oClass);
      var sClassIcon = oClass.icon;
      var oDataForThumbnail = _this.getImageSrcForThumbnail(oRelationshipContentElement);
      var sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc;
      var sImageType = oDataForThumbnail.thumbnailType;
      if (!sThumbnailImageSrc && oClass) {
        sThumbnailImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      }
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

      var oThumbnailProperties = {
        entity: oRelationshipContentElement,
        entityType: ViewUtils.getEntityType(oRelationshipContentElement),
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
        thumbnailMode: 'basic',
        disableDelete: bIsSectionDisabled,
        disableCheck: bIsSectionDisabled,
        disableEditRelationshipVariantContext: CS.isEmpty(_this.props.relationshipContextData)
      };

      var bListMode = false;
      var oThumbnailModel = null;
      if (bListMode) {
        oThumbnailProperties.className = oRelationshipContentElement.isFolder ? 'folderThumb' : 'articleThumb';
        oThumbnailModel = new ListItemModel(
            sContentId,
            oRelationshipContentElement.name,
            sThumbnailImageSrc,
            oRelationshipContentElement.tags,
            sImageType,
            sClassName,
            oThumbnailProperties);
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

      aContentThumbnails.push(oThumbnailModel);
    });

    return (
        <ThumbnailScrollerView
            onChangeActiveItem={this.setActiveItem}
            thumbnailModels={aContentThumbnails}
            relationshipProps={oRelationshipProps}
            activeIndex={this.activeItemId}
            elementId={sCurrentRelationshipId}
        />
    );

  };

  getRelationshipSectionVerticalMenuView = () => {
    var oElement = this.props.element;
    var oRelationship = oElement.relationship;
    var oRelationshipSide = oElement.relationshipSide;
    var aRelationshipContentElementsList = oElement.relationshipContentElementList;
    var oRelationshipToolbarProps = oElement.relationshipToolbarProps;
    var iTotalCount = oRelationshipToolbarProps.totalCount;

    var aVerticalMenus = [];
    var bHideAdd = oRelationshipSide.cardinality == RelationshipConstants.ONE && iTotalCount > 0;

    if (!bHideAdd) {
      aVerticalMenus.push(
          <TooltipView placement="bottom" key="add" label={getTranslation().ADD_ENTITY}>
            <div className="actionButton addEntityButton"
                 onClick={this.handleAddEntityButtonClicked.bind(this, oRelationship.id, oRelationshipSide)}></div>
          </TooltipView>
      );
    }

    if (oRelationshipSide.targetType == BaseTypeDictionary["assetKlassBaseType"] && aRelationshipContentElementsList.length) {
      aVerticalMenus.push(
          <TooltipView placement="bottom" key="default" label={getTranslation().SET_DEFAULT_IMAGE}>
            <div className="actionButton setDefaultImage"
                 ref={this.setDefaultImage}
                 onClick={this.handleApplyDefaultAssetButtonClicked}></div>
          </TooltipView>
      );
    }

    return (<div className="addEntityButtonContainer">
      {aVerticalMenus}
    </div>);
  };

  handleAddEntityButtonClicked = (sRelationshipId, oRelationshipSide, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.ADD_ENTITY_BUTTON_CLICKED, this, sRelationshipId, oRelationshipSide);
    oEvent.stopPropagation();
  };

  handleApplyDefaultAssetButtonClicked = () => {
    var sActiveAssetIndex = this.activeItemId;
    var oElement = this.props.element;
    var aRelationshipContentElementsList = oElement.relationshipContentElementList;
    var oActiveElement = aRelationshipContentElementsList[sActiveAssetIndex] || {};
    var sActiveAssetId = oActiveElement.id;
    EventBus.dispatch(oEvents.MAKE_DEFAULT_ASSET_BUTTON_CLICKED, sActiveAssetId);
  };

  handleRelationshipWrapperOnClick = (oElements, oEvent) => {
    var oRelationship = oElements.relationship;
    var oRelationshipSide = oElements.relationshipSide;
    var sActiveEntityId = oElements.activeEntityId;
    var sContext = 'relationshipContainerSelectionStatus';
    EventBus.dispatch(oEvents.ARTICLE_DETAIL_VIEW_SECTION_CLICKED, this, sActiveEntityId, sContext, oRelationship.id, oRelationshipSide);
  };

  render() {
    var oElement = this.props.element;
    var oRelationship =  oElement.relationship;
    var aRelationshipContentElementsList = oElement.relationshipContentElementList;
    var aSelectedElementsOfRelationship = oElement.selectedElementsOfRelationship;
    var oRelationshipToolbarProps = oElement.relationshipToolbarProps;


    var aElementsThumbView = this.getRelationshipElementsThumbView(aRelationshipContentElementsList, oRelationship.id, aSelectedElementsOfRelationship, oElement);

    var oProperties = {};
    oProperties.context = "contentSection";
    oProperties.isSelected = oElement.isSelected;
    oProperties.innerEntityCount = oRelationshipToolbarProps.totalCount;

    var oVerticalMenuView = !oElement.isDisabled ? this.getRelationshipSectionVerticalMenuView() : null;

    return (
        <div className="relationshipSectionElementViewContainer sectionElementContainer" data-id={oRelationship.id}
             onClick={this.handleRelationshipWrapperOnClick.bind(this, oElement)}>
          <div className="relationshipLabel">{CS.getLabelOrCode(oRelationship)}</div>
          <div className="verticalMenuViewInArticle">
            {oVerticalMenuView}
          </div>
          <div className="articleEditViewThumbsContainer">
            <div className="articleThumbnailContainer" key="article">
              {aElementsThumbView}
            </div>
          </div>
        </div>
    );
  }
}

export const view = RelationshipSectionElementView;
export const events = oEvents;
