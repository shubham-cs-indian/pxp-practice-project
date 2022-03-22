import CS from '../../../../../libraries/cs';
import React, {Fragment} from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as ImageView } from '../../../../../viewlibraries/contentimageview/content-image-view';
import { view as CircularTagListView } from '../../../../../viewlibraries/minithumbnailview/circular-taglist-view';
import ImageViewModel from '../../../../../viewlibraries/contentimageview/model/content-image-view-model';
import { view as ImageCoverflowDetailView } from '../../../../../viewlibraries/imagecoverflowview/image-coverflow-detail-view.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import ViewUtils from './utils/view-utils';
import {view as BulkAssetLinkSharingDialogView} from './bulk-asset-link-sharing-view';
import {view as BulkDownloadDialogView} from './bulk-download-dialog-view';
import  AssetDownloadViewDictionary from "../tack/asset-download-view-dictionary";

const oPropTypes = {
  headerPermission: ReactPropTypes.object,
  contentInfoData: ReactPropTypes.object,
  imageCollapseHandler: ReactPropTypes.func,
  assetDownloadData: ReactPropTypes.object
};

const oEvents = {
  IMAGE_GALLERY_DIALOG_VIEW_VISIBILITY_STATUS_CHANGED: "image_gallery_dialog_view_visibility_status_changed",
  ASSET_IMAGE_UPLOAD_EVENT: "asset_image_upload_event",
  CONTENT_IMAGE_WRAPPER_VIEW_CLOSE_PREVIEW_CLICKED: "content_image_wrapper_view_close_preview_clicked",
  CONTENT_IMAGE_WRAPPER_IMAGE_COVERFLOW_OPEN_VIEWER_CLICKED: "content_image_wrapper_image_coverflow_open_viewer_clicked",
  CONTENT_IMAGE_WRAPPER_ASSET_UPLOAD_REPLACE_CLICKED: "content_image_wrapper_image_upload_replace_clicked",
  GET_ASSET_DOWNLOAD_OR_SHARE_DIALOG: "get_asset_download_or_share_dialog",
};

// @CS.SafeComponent
class ContentImageWrapperView extends React.Component {
  constructor (props) {
    super(props);

    this.state = {
      imageExpanded: true,
    };

    this.getImageView = this.getImageView.bind(this);
    this.getVariantIndicator = this.getVariantIndicator.bind(this);
    this.handleImageGalleryDialogViewVisibilityStatusChanged = this.handleImageGalleryDialogViewVisibilityStatusChanged.bind(this);
    this.handleExpandCollapseIconClicked = this.handleExpandCollapseIconClicked.bind(this);

    this.imageUpload = React.createRef();
  };

  addNewImage = (oEvent) => {
    let oAssetObject = this.props.contentInfoData.assetObject || {};
    let bIsAssetReplaced = CS.isEmpty(oAssetObject.assetObjectKey) ? false : true;
    EventBus.dispatch(oEvents.CONTENT_IMAGE_WRAPPER_ASSET_UPLOAD_REPLACE_CLICKED, this.imageUpload.current, bIsAssetReplaced);
  };

  handleButtonClick = (sId, sButtonId) => {
    if(sButtonId === "close") {
      let oContentInfoData = this.props.contentInfoData;
      let oCoverflowImageModels = oContentInfoData.coverflowimagemodels;
      EventBus.dispatch(oEvents.CONTENT_IMAGE_WRAPPER_VIEW_CLOSE_PREVIEW_CLICKED, this, sId, oCoverflowImageModels.context);
    }
  };

  handleOpenViewerButtonClicked = (oEvent) => {
      let oCoverflowImageModels = this.props.contentInfoData.coverflowimagemodels;
      let aItems = oCoverflowImageModels.coverflowItems;
      let iCurrentIndex = oCoverflowImageModels.activeIndex;
      let oModel = aItems[0];
      EventBus.dispatch(oEvents.CONTENT_IMAGE_WRAPPER_IMAGE_COVERFLOW_OPEN_VIEWER_CLICKED, this, oEvent, oModel,
          iCurrentIndex, oCoverflowImageModels.attributeId, oCoverflowImageModels.context);
  };

  downloadImageButtonClicked = (sButtonContext)=>{
    EventBus.dispatch(oEvents.GET_ASSET_DOWNLOAD_OR_SHARE_DIALOG, AssetDownloadViewDictionary.CONTENT_OPEN_SIDEBAR_VIEW, sButtonContext);
  };

  componentDidUpdate() {
    if(this.imageUpload.current) {
      this.imageUpload.current.value = "";
    }
  }

  handleAddItemControlClicked =(oEvent)=>{
    //let sId = this.props.id;
    let aFiles = oEvent.target.files;
    let oProperties = {files : aFiles};
    let oContentInfoData = this.props.contentInfoData;
    let oCoverflowImageModels = oContentInfoData.coverflowimagemodels;
    EventBus.dispatch(oEvents.ASSET_IMAGE_UPLOAD_EVENT, this, oEvent, "upload_image", oProperties, oCoverflowImageModels.context);
  }

  getVariantIndicator () {
    var oView = null;
    let __props = this.props;
    let oEntityDetailData = __props.contentInfoData;
    if (oEntityDetailData.variantInstanceId) {
      var aContextTagDisplayData = oEntityDetailData.activeVariantTagDisplayData;
      if (aContextTagDisplayData.length != 0) {
        oView = (
            <CircularTagListView
                tags={aContextTagDisplayData}
            />
        );
      }
      else {
        oView = (
            <div className="variantIndicator">
              <div className="variantIcon"></div>
            </div>
        );
      }
    }
    return oView;
  };

  handleExpandCollapseIconClicked () {
    var bIsImageExpanded = this.state.imageExpanded;
    this.setState({
      imageExpanded: !bIsImageExpanded
    });
    this.props.imageCollapseHandler(bIsImageExpanded);
  };

  getImageView () {
    var oImageObject = this.props.contentInfoData.imageObject;
    var oProperties = {
      previewImageSrc: this.props.contentInfoData.previewImageSrc
    };
    var oImageViewStyle = {};
    var oImageViewModel = new ImageViewModel(
        '',
        oImageObject.thumbnailImageSrc,
        oImageObject.thumbnailType,
        "thumbnailImagePreview",
        oImageViewStyle,
        oProperties
    );

    return (<ImageView model={oImageViewModel}
                       onLoad={this.handleImageOnLoad}/>
    );
  };

  getImageDialogView () {
    let oContentInfoData = this.props.contentInfoData;
    let oCoverflowImageModels = oContentInfoData.coverflowimagemodels;
    if (CS.isEmpty(oCoverflowImageModels)) {
      return null;
    }
    var oBodyStyle = {
      padding: 0,
      maxHeight: 'none'
    };
    var oContentStyle = {
     height: '80%',
      width: '95%',
      maxWidth: 'none'
    };

    return this.getImagePreviewDialogView(oCoverflowImageModels, oBodyStyle, oContentStyle, oContentInfoData.entityId);
  };

  handleImageGalleryDialogViewVisibilityStatusChanged (oEvent) {
    if (!oEvent.nativeEvent.dontRaise) {
      EventBus.dispatch(oEvents.IMAGE_GALLERY_DIALOG_VIEW_VISIBILITY_STATUS_CHANGED);
    }
  };

  getImagePreviewDialogView (oCoverflowImageModels, oBodyStyle, oContentStyle, sEntityId) {
    let bIsCoverflowDetailViewOpened = oCoverflowImageModels.isDialogOpen;
    let oModel = oCoverflowImageModels.coverflowItems[0];
    let oImageAttribute = {
      id: oCoverflowImageModels.id,
      instanceId: sEntityId,
      type: "attribute"
    };
    let aButtonData = [
      {
        id: "close",
        label: getTranslation().CLOSE,
        isDisabled: false,
        isFlat: false,
      }
    ];
    return (<CustomDialogView modal={true} open={bIsCoverflowDetailViewOpened}
                              bodyClassName="coverflowAssetDetailsViewModel"
                              bodyStyle={oBodyStyle}
                              contentStyle={oContentStyle}
                              contentClassName="coverflowAssetDetailsViewModelDialog"
                              buttonData={aButtonData}
                              onRequestClose={this.handleButtonClick.bind(this, oCoverflowImageModels.attributeId, aButtonData[0].id)}
                              buttonClickHandler={this.handleButtonClick.bind(this, oCoverflowImageModels.attributeId)}>
      {bIsCoverflowDetailViewOpened ? <ImageCoverflowDetailView
          id={oCoverflowImageModels.attributeId}
          aModel={oCoverflowImageModels.coverflowItems}
          imageCoverflowActiveIndex={oCoverflowImageModels.activeIndex}
          assetObjectKey={oModel.coverflowImageKey}
          isUpload={oCoverflowImageModels.isUpload}
          isDisabled={false}
          imageAnnotationData={oCoverflowImageModels.imageAnnotationData}
          imageAttribute={oImageAttribute}
      /> : null}
    </CustomDialogView>)
  }

  getDownloadOrLinkSharingDialogView (oEntityDetailData) {
    if (CS.isNotEmpty(oEntityDetailData.assetDownloadData)) {
      let oAssetDownloadData = oEntityDetailData.assetDownloadData;
      return (
          <BulkDownloadDialogView isFolderByAsset={oAssetDownloadData.isFolderByAsset}
                                  toggleButtonData={oAssetDownloadData.toggleButtonData}
                                  downloadData={oAssetDownloadData.downloadData}
                                  fixedSectionData={oAssetDownloadData.fixedSectionData}
                                  downloadAsExtraData={oAssetDownloadData.downloadAsExtraData}
                                  buttonExtraData={oAssetDownloadData.buttonExtraData}
                                  invalidRowIds={oAssetDownloadData.invalidRowIds}/>
      );

    } else if (CS.isNotEmpty(oEntityDetailData.assetLinkSharingDialogData)) {
      let oAssetLinkSharingData = oEntityDetailData.assetLinkSharingDialogData;
      return (
          <BulkAssetLinkSharingDialogView
              assetLinkSharingGridViewData={oAssetLinkSharingData.assetLinkSharingGridViewData}/>
      );
    }
  }

  render () {
    let __props = this.props;
    let _this = this;
    let bCanDownload = __props.isDownloadIconVisible;
    let oHeaderPermission = __props.headerPermission;
    let oEntityDetailData = __props.contentInfoData;
    let oVariantIndicatorView = !oEntityDetailData.isDetailedUnitView ? this.getVariantIndicator() : null;
    let sImageContainerClassName = "contentImageWrapper";
    sImageContainerClassName = this.state.imageExpanded ? sImageContainerClassName : sImageContainerClassName + " collapsed";
    let sExpandCollapseIcon = "expandCollapseIcon";
    sExpandCollapseIcon = this.state.imageExpanded ? sExpandCollapseIcon : sExpandCollapseIcon + " collapsed";

    let oImageView = this.getImageView();
    let bIsAssetBaseType = ViewUtils.isAssetBaseType(oEntityDetailData.imageGalleryDialogViewData.activeEntityBaseType);
    let oDefaultImageViewDOM = null;
    let oDownloadOrLinkSharingDialogDom = null;

    if((CS.isEmpty(oHeaderPermission) || oHeaderPermission.canChangeIcon)) {
      if(bIsAssetBaseType) {
        let oAssetObject = this.props.contentInfoData.assetObject || {};
        let oImageObject = this.props.contentInfoData.imageObject;
        let sLabel = CS.isEmpty(oImageObject.thumbnailImageSrc) ? getTranslation().ADD_ASSET : getTranslation().REPLACE_ASSET;
        let sClassName =  CS.isEmpty(oAssetObject.assetObjectKey) ? "setAssetImage" : "setDefaultImage";
        let oPreviewImageDom = null;
        let oDownloadDom = null;
        let oShareDom = null;
        let bShowShareDom = __props.isShareIconVisible;
        let oEditDom = null;
        let oShareAssetData = oEntityDetailData.shareAssetData;

        if(oAssetObject.fileName && !oEntityDetailData.isFirstimeUploded && bCanDownload){
          oDownloadDom = (<TooltipView label={getTranslation().DOWNLOAD_ASSET} placement="bottom">
            <div className="downloadImage"
                 onClick={_this.downloadImageButtonClicked.bind(this, AssetDownloadViewDictionary.BULK_ASSET_DOWNLOAD_VIEW)}>
            </div>
          </TooltipView>)
        }
        if (oAssetObject.fileName && !oEntityDetailData.isFirstimeUploded && !oShareAssetData.bIsEmbeddedAsset
            && bShowShareDom) {
          oShareDom = (<TooltipView label={getTranslation().SHARE_ASSET} placement="bottom">
            <div className="shareImage"
                 onClick={_this.downloadImageButtonClicked.bind(this, AssetDownloadViewDictionary.BULK_LINK_SHARING_VIEW)}>
            </div>
          </TooltipView>);
        }

        oDownloadOrLinkSharingDialogDom = this.getDownloadOrLinkSharingDialogView(oEntityDetailData);

        if(!CS.isEmpty(oAssetObject.fileName)){
          oPreviewImageDom = (
            <TooltipView label={getTranslation().OPEN_IN_VIEWER} placement="bottom">
              <div className="previewImage"
                   onClick={_this.handleOpenViewerButtonClicked}>
              </div>
            </TooltipView>)
        }
        oDefaultImageViewDOM = !oEntityDetailData.isDisableHeader ?
            (<Fragment>
              {oShareDom}
              {oDownloadDom}
              {oPreviewImageDom}
              {oDownloadOrLinkSharingDialogDom}
              {oEditDom}
              <TooltipView placement="bottom" label={sLabel}>
                <div className={sClassName} onClick={this.addNewImage}>
                </div>
              </TooltipView>
            </Fragment>)
            : <Fragment>
              {oShareDom}
              {oDownloadDom}
              {oPreviewImageDom}
              {oDownloadOrLinkSharingDialogDom}
              {oEditDom}
            </Fragment>;
      } else {
        oDefaultImageViewDOM = !oEntityDetailData.isDisableHeader ?
            (<Fragment>
              <TooltipView label={getTranslation().REPLACE} placement="bottom">
                <div className={"setDefaultImage"}
                     onClick={this.handleImageGalleryDialogViewVisibilityStatusChanged}>
                </div>
              </TooltipView>
            </Fragment>)
        : null;
      }
    }


    var sTooltipLabel = this.state.imageExpanded ? "COLLAPSE" : "EXPAND";
    return (<div className="contentImageContainer">
      {this.getImageDialogView()}
      <div className={sImageContainerClassName}>
        {oImageView}
        <div className="contentImageWrapperIconSection">
          <div className="contentImageWrapperIconRightSection">
            {oDefaultImageViewDOM}
            <input style={{"visibility": "hidden", "height": "0", "width": "0"}}
                   className={"uploadImage"}
                   ref={this.imageUpload}
                   onChange={_this.handleAddItemControlClicked}
                   type="file"
            />
            <TooltipView label={getTranslation()[sTooltipLabel]} placement="bottom">
              <div className={sExpandCollapseIcon} onClick={this.handleExpandCollapseIconClicked}></div>
            </TooltipView>
          </div>
          {oVariantIndicatorView}
        </div>
      </div>
    </div>)
  }
};

ContentImageWrapperView.propTypes = oPropTypes;

export const view = ContentImageWrapperView;
export const events = oEvents;
