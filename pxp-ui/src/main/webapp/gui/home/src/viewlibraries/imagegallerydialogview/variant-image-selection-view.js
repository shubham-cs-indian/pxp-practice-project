import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import { view as ImageView } from '../contentimageview/content-image-view';
import ImageViewModel from '../contentimageview/model/content-image-view-model';
import ViewLibraryUtils from '../utils/view-library-utils';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
  ASSIGN_ASSET_BUTTON_CLICKED: "assign_asset_button_clicked"
};

const oPropTypes = {
  assetList: ReactPropTypes.array,
  variantId: ReactPropTypes.string,
  selectedEntityId: ReactPropTypes.string,
  context: ReactPropTypes.string
};
/**
 * @class VariantImageSelectionView
 * @memberOf Views
 * @property {array} [assetList] - Contains asset data.
 * @example assetList = [assetInstanceId: "f5b4053d-90ab-463e-ba95-b4c634b13fae", isDefault: false,
 * label: "royal-enfield-classic-350-500x500", properties: {…}, thumbKey: "03b579e6-c936-4dc6-9c71-964c624de572", type: "Image"]
 * @property {string} [variantId] - ex. for article assets containing article id.
 * @property {string} [selectedEntityId] - Contains selected entity id.
 * @property {string} [context] - Used to differentiate screens.
 */

// @CS.SafeComponent
class VariantImageSelectionView extends React.Component {

  constructor(props) {
    super(props);
  }

  getImageSrcForThumbnail =(oContent)=> {
    var aList = oContent.referencedAssets;
    if(ViewLibraryUtils.isAssetBaseType(oContent.baseType)) {
      aList = oContent.attributes;
    }
    var oImage = CS.find(aList, {isDefault: true});

    return ViewLibraryUtils.getImageSrcUrlFromImageObject(oImage);
  }

  handleAssignImageToVariantButtonClicked =(sAssetId)=> {
    var sContext = this.props.context;
    EventBus.dispatch(oEvents.ASSIGN_ASSET_BUTTON_CLICKED, this.props.variantId, sAssetId, sContext);
  }

  getImagesView =()=> {
    var _this = this;
    var __props = _this.props;
    var aAssetList = __props.assetList;
    var aImageList = [];

    CS.forEach(aAssetList, function (oAsset, index) {
      var sAssetId = oAsset.assetInstanceId;
      var bIsSelected = sAssetId == __props.selectedEntityId;

      var oDataForThumbnail = ViewLibraryUtils.getImageSrcUrlFromImageObject(oAsset);//_this.getImageSrcForThumbnail(oEntity);
      var sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc;
      var sImageType = oDataForThumbnail.thumbnailType;

      var oProperties = {};
      var oImageStyle = {};
      var oImageViewModel = new ImageViewModel( '',
          sThumbnailImageSrc,
          sImageType,
          "variantImage",
          oImageStyle, oProperties);

      var sVariantImageContainerClass = "variantImageContainer ";
      sVariantImageContainerClass += bIsSelected ? "selected" : "";
      aImageList.push(
          <div key={index}
               className={sVariantImageContainerClass}
               title={CS.getLabelOrCode(oAsset)}
               onClick={_this.handleAssignImageToVariantButtonClicked.bind(_this, oAsset.assetInstanceId)}>
            <ImageView model={oImageViewModel}/>
            <div className="imageLabel">{CS.getLabelOrCode(oAsset)}</div>
          </div>
      );
    });

    return aImageList;
  }

  render() {
    var aAssetList = this.props.assetList;
    return (
        <div className="variantImageSelectionViewContainer">
          {!CS.isEmpty(aAssetList) ? this.getImagesView() : (<div className="assets-not-found-container">
            <div className="assets-not-found-message">{getTranslation().ASSETS_NOT_FOUND}</div>
          </div>)}
        </div>
    );
  }
}

VariantImageSelectionView.propTypes = oPropTypes;

export const view = VariantImageSelectionView;
export const events = oEvents;
