import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import ThumbnailModel from '../../../../../viewlibraries/thumbnailview2/model/thumbnail-model';
import { view as NatureThumbnailView } from './nature-thumbnail-view';
import { view as NatureThumbnailConnectorView } from './nature-thumbnail-connector-view';
import { view as NothingFoundView } from './../../../../../viewlibraries/nothingfoundview/nothing-found-view';
import ViewUtils from './utils/view-utils';
import BaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  NATURE_ADD_ENTITY_BUTTON_CLICKED: "nature_add_entity_button_clicked"
};

const oPropTypes = {
  natureSectionViewData: ReactPropTypes.object
};

// @CS.SafeComponent
class NatureRelationshipView extends React.Component {

  handleAddProductToBundleClicked = () => {
    var oNatureSectionViewData = this.props.natureSectionViewData;
    var oRelationshipElement = oNatureSectionViewData.natureRelationshipElement;
    var oRelationshipSide = oRelationshipElement.relationshipSide;
    EventBus.dispatch(oEvents.NATURE_ADD_ENTITY_BUTTON_CLICKED, oRelationshipElement.id, oRelationshipSide, oNatureSectionViewData.viewContext);
  };

  getAddProductThumbnail = () => {
    let sLabel = getTranslation().ADD_PRODUCTS;
    return (
        <div className="addProductThumbnail" onClick={this.handleAddProductToBundleClicked}>
          <div className="plusIcon"></div>
          <div className="addProductMessage">{sLabel}</div>
        </div>
    );
  };

  getImageSrcForThumbnail = (oEntity, aAssetRelationshipEntities) => {
    var oContent = CS.find(aAssetRelationshipEntities, {id: oEntity.defaultAssetInstanceId}) || oEntity;
    if(oContent) {
      var aList = oContent.referencedAssets || [oContent];
      if(oContent.baseType == BaseTypeDictionary.assetBaseType) {
        aList = oContent.attributes;
      }
      var oImage = CS.find(aList, {isDefault: true});
      return ViewUtils.getImageSrcUrlFromImageObject(oImage);
    }

    return  {
      thumbnailImageSrc: '',
      thumbnailType: ''
    }
  };

  getPreviewImage = (oEntity) => {
    var sEntityType = ViewUtils.getEntityClassType(oEntity);
    var oClass = ViewUtils.getKlassFromReferencedKlassesById(sEntityType);
    var sPreviewImage = "";

    if (oClass && oClass.previewImage) {
      sPreviewImage = ViewUtils.getIconUrl(oClass.previewImage);
    }
    return sPreviewImage;
  };

  getEntityNatureThumbnailModel = (oEntity) => {
    var __props = this.props;
    var oNatureSectionViewData = __props.natureSectionViewData;
    var aSelectedNatureElements = oNatureSectionViewData.selectedNatureElements;
    var aAssetRelationshipEntities = oNatureSectionViewData.assetRelationshipEntities || [];
    var oNatureRelationship = oNatureSectionViewData.natureRelationship;
    var sCurrentRelationshipId = oNatureRelationship.sideId;
    var bIsReadOnly = !!oNatureSectionViewData.isReadOnly;
    let oNatureRelationshipElement = oNatureSectionViewData.natureRelationshipElement;
    let oNatureThumbnailProperties = oNatureSectionViewData.natureThumbnailProperties || {};

    var sIdKey = "id";
    var sEntityId = oEntity[sIdKey];
    var bIsActive = false;
    var oCondition = {};
    oCondition[sIdKey] = oEntity[sIdKey];
    var bIsSelected = CS.includes(aSelectedNatureElements, sEntityId);

    var sEntityType = ViewUtils.getEntityClassType(oEntity);

    var sClassName = "";
    var sClassIcon = "";
    var oClass = ViewUtils.getKlassFromReferencedKlassesById(sEntityType);
    if (oClass) {
      sClassName = CS.getLabelOrCode(oClass);
      sClassIcon = oClass.icon;
    }


    var oImageDataForThumbnail = this.getImageSrcForThumbnail(oEntity, aAssetRelationshipEntities);
    var sThumbnailImageSrc = oImageDataForThumbnail.thumbnailImageSrc;
    if (!sThumbnailImageSrc) {
      sThumbnailImageSrc = this.getPreviewImage(oEntity);
    }

    //temp fix - krish
    var bCanEdit = !bIsReadOnly;
    var oThumbnailProperties = {
      entity: oEntity,
      entityType: ViewUtils.getEntityType(oEntity),
      thumbnailType: "",
      isActive: bIsActive,
      isSelected: bIsSelected,
      className: "",
      isDirty: Boolean(oEntity.isDirty) || Boolean(oEntity.isCreated) || false,
      isRemoveIcon: true,
      versionText: "",
      icons: [],
      classIcon: sClassIcon,
      thumbnailMode: __props.thumbnailMode,
      count: oEntity.count,
      disableCheck: !bCanEdit || oNatureThumbnailProperties.disableCheck,
      disableDelete: !oNatureRelationshipElement.canDelete || oNatureThumbnailProperties.disableDelete,
      canEdit: bCanEdit,
      currentRelationshipId: sCurrentRelationshipId,
      viewContext:oNatureSectionViewData.viewContext,
    };

    let sEntityName = ViewUtils.getContentName(oEntity);
    var oThumbnailModel = new ThumbnailModel(
        sEntityId,
        sEntityName,
        sThumbnailImageSrc,
        oEntity.tags,
        "",
        sClassName,
        oThumbnailProperties
    );

    return (oThumbnailModel);
  };

  getBundledItemsList = () => {
    var aBundledItemsList = [];
    var oNatureSectionViewData = this.props.natureSectionViewData;
    var oNatureRelationship = oNatureSectionViewData.natureRelationship;
    var sCurrentRelationshipId = oNatureRelationship.relationshipId;
    var aElementIds = oNatureRelationship.elementIds;
    var aNatureRelationshipInstanceElements = oNatureSectionViewData.natureRelationshipInstanceElements;
    let oNatureRelationshipElement = oNatureSectionViewData.natureRelationshipElement;
    var _this = this;
    var aAssetRelationshipEntities = oNatureSectionViewData.assetRelationshipEntities || [];

    aBundledItemsList.push(<NatureThumbnailConnectorView nature={_this.nature} isFirst={true} isNatureThumbConnectorHidden={oNatureSectionViewData.isNatureThumbConnectorHidden}/>);

    CS.forEach(aElementIds, function (sElementId, iIndex) {
      var oElement = CS.find(aNatureRelationshipInstanceElements, {id: sElementId});
      if (oElement.elementClone) {
        oElement = oElement.elementClone; //use clone if exists
      }
      var oImageDataForThumbnail = _this.getImageSrcForThumbnail(oElement, aAssetRelationshipEntities);
      var sThumbnailImageSrc = oImageDataForThumbnail.thumbnailImageSrc;

      var oThumnailViewModel = _this.getEntityNatureThumbnailModel(oElement);

      if (iIndex > 0) {
        aBundledItemsList.push(<NatureThumbnailConnectorView nature={_this.nature} isNatureThumbConnectorHidden={oNatureSectionViewData.isNatureThumbConnectorHidden}/>);
      }

      aBundledItemsList.push(
          <NatureThumbnailView
              key={oElement.id}
              thumbnailModel={oThumnailViewModel}
              id={oElement.id}
              label={oElement.name}
              imageSrc={sThumbnailImageSrc}
              index={iIndex}
              nature={_this.nature}
              relationshipId={sCurrentRelationshipId}
          />
      );
    });

    var oRelationshipToolbarProp = oNatureSectionViewData.relationshipToolbarProps;
    var bIsReadOnly = oNatureSectionViewData.isReadOnly;
    var iTotalAddedElementCount = oRelationshipToolbarProp.totalCount;

    var oReferencedNatureRelationship = oNatureSectionViewData.referencedNatureRelationship;
    if ((iTotalAddedElementCount < this.maxNoOfItems) || (this.maxNoOfItems == 0)) { // maxNoOfItems = 0 means infinite number of items can b added
      if (!bIsReadOnly) {
        if(!CS.isEmpty(aElementIds)){
          aBundledItemsList.push(<NatureThumbnailConnectorView nature={_this.nature} isNatureThumbConnectorHidden={oNatureSectionViewData.isNatureThumbConnectorHidden}/>);
        }
        let bShowAddProductThumbnail = !oReferencedNatureRelationship.autoCreateSettings && oNatureRelationshipElement.canAdd;
        if (bShowAddProductThumbnail) {
          aBundledItemsList.push(this.getAddProductThumbnail())
        }
      }
    }
    if (!oNatureRelationshipElement.canAdd && CS.isEmpty(aElementIds)) {
      aBundledItemsList = <NothingFoundView message={getTranslation().NOTHING_TO_DISPLAY}/>
    } else {
      aBundledItemsList.push(<NatureThumbnailConnectorView nature={_this.nature} isLast={true} isNatureThumbConnectorHidden={oNatureSectionViewData.isNatureThumbConnectorHidden}/>);
    }
    return aBundledItemsList;
  };

  render() {
    var oNatureSectionViewData = this.props.natureSectionViewData;
    var oReferencedNatureRelationship = oNatureSectionViewData.referencedNatureRelationship;
    this.nature = oReferencedNatureRelationship.natureType;
    this.maxNoOfItems = oReferencedNatureRelationship.maxNoOfItems;

    var aBundledItemsList = this.getBundledItemsList();
    return (
        <div className="bundledItemsContainer">
          {aBundledItemsList}
        </div>
    );
  }
}

export const view = NatureRelationshipView;
export const events = oEvents;
