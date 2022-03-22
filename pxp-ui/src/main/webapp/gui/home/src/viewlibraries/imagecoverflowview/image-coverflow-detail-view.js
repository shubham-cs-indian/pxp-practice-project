import CS from '../../libraries/cs';
import React from 'react';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ImageSimpleView } from './../imagesimpleview/image-simple-view';
import { view as VideoView } from '../videoview/video-view';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
  IMAGE_COVERFLOW_DETAIL_VIEW_ASSET_LIST_ITEM_CLICKED: "image_coverflow_detail_view_asset_list_item_clicked",
};

const oPropTypes = {
};

// @CS.SafeComponent
class ImageCoverflowDetailView extends React.Component {

  constructor(props) {
    super(props);

    this.activeAssetListItem = React.createRef();
    this.downloadButton = React.createRef();
    this.fileName = React.createRef();
    this.description = React.createRef();
  }

  onVideoError =(oEvent)=> {
    ExceptionLogger.error('Error in video source');
    ExceptionLogger.log(oEvent);
  }

  getAssetView =()=> {

    var assetObjectKey = this.props.assetObjectKey;

    var oModel =  CS.find(this.props.aModel, function (oModel){
      return oModel.coverflowImageKey == assetObjectKey;
    });

    var sAssetSrc = oModel.coverflowAssetObjectSrc;
    var sThumbnailSrc = oModel.coverflowSrc;
    var sPreviewSrc = oModel.previewImageSrc;
    var sAssetType = oModel.coverflowType;
    var sExtension = oModel.properties['extension'] || '';
    var sImageKey = oModel.coverflowImageKey;
    var sMp4Src = oModel.properties['mp4'];
    if (CS.isEmpty(sImageKey)) {
      sAssetType = "Not Found";
    }

    var oContent = null;
    var iAverageProgress = oModel.properties['status'] || 1;
    var sAverageStyle = {width: iAverageProgress + "%"};

    if ((sMp4Src && sMp4Src.indexOf('#') != -1) || sImageKey.indexOf('#') != -1) {
      oContent = (<div>
        <div className="statusProgressBarContainer">
          <div className="statusProgressBar" style={sAverageStyle}></div>
          <span>{iAverageProgress + ' %'}</span>
        </div>
      </div>);
    } else if (sAssetType === "Document" && CS.isEmpty(sPreviewSrc)) {
      oContent = (<p>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
    }
    else {

      switch (sAssetType) {
        case 'Image':
          var oImageView;

          if (['eps', 'ai', 'psd', 'tif', 'tiff'].indexOf(sExtension.toLowerCase()) != -1) {
            sAssetSrc = sThumbnailSrc;
          }

          oImageView = (<ImageSimpleView imageSrc={sAssetSrc} />);

          oContent = (
              <div className="assetViewImage image">
                  {oImageView}
              </div>
          );
          break;
        case 'Document':
          var sAssetKey = oModel.coverflowImageKey;
          if (sExtension.toLowerCase() != 'pdf') {
              sAssetSrc = sPreviewSrc;
          }
          oContent = (<embed src={sAssetSrc} alt="pdf" type="application/pdf" key={sAssetKey}
                               pluginspage="http://www.adobe.com/products/acrobat/readstep2.html"/>);
          break;
        case 'Video':
          // /*var */sImageKey = oModel.coverflowImageKey;
          //var sMp4Status = +oModel.properties['mp4status') || 1;
          // var sStatus = +oModel.properties['status') || 1;
          /*var iAverageProgress = sStatus;
          var sAverageStyle = {width: iAverageProgress + "%"};*/
          if (sMp4Src) {
            oContent = (
                <div className="assetViewImage">
                  <span className="assetViewImageCenterSpan"></span>
                  <VideoView
                      elementKey={sImageKey}
                      poster={sThumbnailSrc}
                      videoSrc={sMp4Src}
                  />
                </div>);
          }
          /*else {
            oContent = (<div>
              <div className="statusProgressBarContainer">
                <div className="statusProgressBar" style={sAverageStyle}></div>
                <span>{iAverageProgress + ' %'}</span>
              </div>
            </div>);
          }*/
          break;
        case 'Not Found':
          oContent = (<p>{getTranslation().ASSET_NOT_FOUND}</p>);
          break;
        default:
          oContent = (<p>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
      }
    }

    var oContent = (
        <div className="coverflowAssetView">
          {oContent}
        </div>);
    return oContent;
  }

  handleCoverflowDetailViewAssetItemClicked =(oModel, assetObjectKey, sId, oEvent)=> {
    EventBus.dispatch(oEvents.IMAGE_COVERFLOW_DETAIL_VIEW_ASSET_LIST_ITEM_CLICKED, this, oEvent, oModel, assetObjectKey, sId);
  }

  updateTextInputValues =()=> {
    var assetObjectKey = this.props.assetObjectKey;
    var oModel =  CS.find(this.props.aModel, function (oModel){
      return oModel.coverflowImageKey == assetObjectKey;
    });

    var oTitleDOM = this.fileName.current;
    if(oTitleDOM && oTitleDOM.getValue() != oModel.fileName) {
      oTitleDOM.setValue(oModel.fileName);
    }

    var oDescriptionDOM = this.description.current;
    if(oDescriptionDOM && oDescriptionDOM.getValue() != oModel.description) {
      oDescriptionDOM.setValue(oModel.description);
    }

    var sAssetSrc = oModel.coverflowAssetObjectSrc;
    var sDownloadHref = sAssetSrc + "?download=" + encodeURIComponent(oModel.fileName);
    if(this.downloadButton.current) {
      this.downloadButton.current.href = sDownloadHref;
    }
  }

  scrollToActiveItem =()=> {
    if(this.activeAssetListItem.current) {
      this.activeAssetListItem.current.scrollIntoView();
    }
  }

  componentDidUpdate =()=> {
    this.updateTextInputValues();
  }

  componentDidMount =()=> {
    this.updateTextInputValues();
    this.scrollToActiveItem();
  }

  render() {
    var oAssetView = this.getAssetView();

    return (
        <div className="coverflowAssetDetailsViewWrapper">
          <div className="coverflowAssetDetailsViewContainer">
            <div className="coverflowAssetDetailsColumn assetViewContainer">
              {oAssetView}
            </div>
          </div>
        </div>
    )
  }
}


ImageCoverflowDetailView.propTypes = oPropTypes;

export const view = ImageCoverflowDetailView;
export const events = oEvents;
