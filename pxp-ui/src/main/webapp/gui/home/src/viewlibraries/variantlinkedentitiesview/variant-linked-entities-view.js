import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ViewLibraryUtils from './../utils/view-library-utils';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import BaseTypeDictionary from '../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import EntitiesList from '../../commonmodule/tack/entities-list';
import oFilterPropType from './../../screens/homescreen/screens/contentscreen/tack/filter-prop-type-constants';

const oEvents = {
  VARIANT_LINKED_ENTITIES_VIEW_ADD_ENTITY: "variant_linked_entities_view_add_entity",
  VARIANT_LINKED_ENTITIES_VIEW_REMOVE_ENTITY: "variant_linked_entities_view_remove_entity"
};

const oPropTypes = {
  variantSectionViewData: ReactPropTypes.object,
  isInstanceRemovable: ReactPropTypes.bool,
  canEdit: ReactPropTypes.bool,
  addedLinkedInstancesHandler: ReactPropTypes.func,
  removeLinkedInstanceHandler: ReactPropTypes.func
};
/**
 * @class VariantLinkedEntitiesView
 * @memberOf Views
 * @property {object} [variantSectionViewData]
 * @property {bool} [isInstanceRemovable]
 * @property {bool} [canEdit]
 * @property {func} [addedLinkedInstancesHandler]
 * @property {func} [removeLinkedInstanceHandler]
 */

// @CS.SafeComponent
class VariantLinkedEntitiesView extends React.Component {
  static propTypes = oPropTypes;

  handleManageLinkedInstancesClicked = (sContextEntity) => {
    if (CS.isFunction(this.props.addedLinkedInstancesHandler)) {
      this.props.addedLinkedInstancesHandler(sContextEntity);
    } else {
      let oFilterContext = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      }
      EventBus.dispatch(oEvents.VARIANT_LINKED_ENTITIES_VIEW_ADD_ENTITY, sContextEntity, oFilterContext);
    }
  };

  handleRemoveVariantLinkedInstanceClicked = (sLinkedInstanceId, sContextEntity) => {
    if (CS.isFunction(this.props.removeLinkedInstanceHandler)) {
      this.props.removeLinkedInstanceHandler(sLinkedInstanceId, sContextEntity);
    } else {
      EventBus.dispatch(oEvents.VARIANT_LINKED_ENTITIES_VIEW_REMOVE_ENTITY, sLinkedInstanceId, sContextEntity);
    }
  };

  getModuleName = (sContextEntity) => {
    let aEntitiesList = EntitiesList();
    var oModule = CS.find(aEntitiesList, {id: sContextEntity});
    return oModule && CS.getLabelOrCode(oModule);
  };

  getImageSrcForThumbnail = (oVariant, aAssetRelationshipEntities) => {
    var oContent = CS.find(aAssetRelationshipEntities, {id: oVariant.defaultAssetInstanceId}) || oVariant;
    if (oContent) {
      var aList = oContent.referencedAssets  || [oContent];
      if (oContent.baseType == BaseTypeDictionary.assetBaseType) {
        aList = oContent.attributes;
      }
      var oImage = null;
      if(oVariant.defaultAssetInstanceId){
        aList = aAssetRelationshipEntities;
        oImage = CS.find(aList,{assetInstanceId: oVariant.defaultAssetInstanceId })
      }
      else{
        oImage = CS.find(aList, {isDefault: true});
      }

      return ViewLibraryUtils.getImageSrcUrlFromImageObject(oImage);
    }

    return {
      thumbnailImageSrc: '',
      thumbnailType: ''
    }
  };

  getLinkedInstanceViews = (sContextEntity) => {
    var _this = this;
    var oVariantSectionViewData = this.props.variantSectionViewData;
    var bIsInstanceRemovable = this.props.isInstanceRemovable;
    var bCanEdit = this.props.canEdit;
    var oVariant = !CS.isEmpty(oVariantSectionViewData.dummyVariant) ? oVariantSectionViewData.dummyVariant : oVariantSectionViewData.editableVariant;
    if(CS.isEmpty(oVariant)){
      oVariant = oVariantSectionViewData.activeVariant;
    }
    oVariant = oVariant.contentClone ? oVariant.contentClone : oVariant;
    var aLinkedInstanceViews = [];
    var sModuleName = this.getModuleName(sContextEntity);
    var oLinkedInstances = ViewLibraryUtils.getLinkedInstancesFromVariant(oVariant);
    var aLinkedInstances = oLinkedInstances[sContextEntity];
    // var sClassName = "";
    // var sClassIcon = "";
    var oClass = {};

    CS.forEach(aLinkedInstances, function (oLinkedInstance, iIndex) {
      var oDataForThumbnail = _this.getImageSrcForThumbnail(oLinkedInstance, oVariantSectionViewData.assetRelationshipEntities);
      var sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc;
      var oCircleView = null;

      /**TODO: Think about a logic by not using view-utils of content-screen to get referenced klass **/
      // var sElementType = ViewLibraryUtils.getEntityClassType(oLinkedInstance);
      // oClass = ViewLibraryUtils.getKlassFromReferencedKlassesById(sElementType);

      /*if (oClass) {
        sClassName = CS.getLabelOrCode(oClass);
        sClassIcon = oClass.icon;
      }*/

      /*if (!sThumbnailImageSrc && oClass) {
        sThumbnailImageSrc = oClass.previewImage ? ViewLibraryUtils.getIconUrl(oClass.previewImage,false) : "";
      }*/
      if (sThumbnailImageSrc) {
        oCircleView = <ImageFitToContainerView imageSrc={sThumbnailImageSrc}/>;
      } else {
        oCircleView = (<div className="defaultVariantLinkedImage"></div>);
      }

      var oRemoveButtonView = bIsInstanceRemovable ? (<div className="variantInstanceRemoveImage"
                                                           onClick={_this.handleRemoveVariantLinkedInstanceClicked.bind(_this, oLinkedInstance.id, sContextEntity)}></div>) : null;

      aLinkedInstanceViews.push(
          <div className="circledLinkedInstanceNodeViewContainer" title={oLinkedInstance.name}>
            <div className="nodeVariantImageCircleContainer">
              {oRemoveButtonView}
              <div className="variantLinkedInstanceImageWrapper">
                {oCircleView}
              </div>
            </div>
            <div className="nodeTextContainer" key={iIndex}>{oLinkedInstance.name}</div>
          </div>)
    });

    var sAddInstanceHandle = bCanEdit ? "addInstanceHandle" :"addInstanceHandle disabled";
    var fOnClick = bCanEdit ? _this.handleManageLinkedInstancesClicked.bind(_this, sContextEntity) : null;
    var sAddClassName = bCanEdit ? "addInstance" : "addInstance disabled";

    var oAddInstanceDOM = (
        <div className={sAddInstanceHandle}
             onClick={fOnClick}>
          <div className={sAddClassName}></div>
        </div>);

    var sLinkedInstanceClassName = (!CS.isEmpty(aLinkedInstanceViews)) ? "linkedInstanceControl linkedInstance" : "linkedInstanceControl";

    return (<div className="linkedInstanceContainer">
      <div className="linkedInstanceLabel">{sModuleName}</div>
      <div className="linkedInstanceBody">
        {aLinkedInstanceViews}
        <div className={sLinkedInstanceClassName}>
          {oAddInstanceDOM}
        </div>
      </div>
    </div>);
  };

  getLinkedEntitiesView = () => {
    var oVariantSectionViewData = this.props.variantSectionViewData;
    var oSelectedVisibleContext = oVariantSectionViewData.selectedVisibleContext;
    var aContextEntityList = oSelectedVisibleContext.entities;
    var aLinkedInstanceViews = [];
    var _this = this;

    var oSelectedContext = oVariantSectionViewData.selectedContext;
    if(CS.isEmpty(oSelectedVisibleContext)) {
      aContextEntityList = oSelectedContext.entities;
    }

    if (!oVariantSectionViewData.isProductVariant) {
      CS.forEach(aContextEntityList, function (sContextEntity) {
        aLinkedInstanceViews.push(_this.getLinkedInstanceViews(sContextEntity));
      })
    }

    return aLinkedInstanceViews;
  };

  render() {

    return (
        <div className="variantLinkedEntitiesContainer">
          {this.getLinkedEntitiesView()}
        </div>
    );
  }
}

export const view = VariantLinkedEntitiesView;
export const events = oEvents;
