import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ImageSimpleView } from './../imagesimpleview/image-simple-view';
import { view as VideoView } from '../videoview/video-view';
import Dialog from '@material-ui/core/Dialog';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ViewUtils from '../utils/view-library-utils';

const oEvents = {};

const oPropTypes = {
  mediaData: ReactPropTypes.shape({
    assetObjectKey: ReactPropTypes.string,
    extension: ReactPropTypes.string,
    fileName: ReactPropTypes.string,
    imageSrc: ReactPropTypes.string,
    previewSrc: ReactPropTypes.string,
    thumbKeySrc: ReactPropTypes.string,
  }),
  showPreview: ReactPropTypes.bool,
  title: ReactPropTypes.string,
  onClose: ReactPropTypes.func
};
/**
 * @class MediaPreviewDialogView
 * @memberOf Views
 * @property {custom} [mediaData] - pass a stings of fileName, imageSrc, previewSrc, thumbKeySrc, assetObjectKey, extension.
 * @property {bool} [showPreview] -  boolean which is used for show or hide showPreview.
 * @property {string} [title] -  string of title of media.
 * @property {func} [onClose] -  a function which used for onClose event for close the media.
 */

// @CS.SafeComponent
class MediaPreviewDialogView extends React.Component {

  constructor(props) {
    super(props);
  }

/*
  onVideoError =(oEvent)=> {
    ExceptionLogger.error('Error in video source');
    ExceptionLogger.log(oEvent);
  }
*/

  closeMediaPreviewDialogView = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    if (this.props.onClose) {
      this.props.onClose(oEvent);
    }
  };

  getMediaPreviewDialogView =()=> {
    let bShowPreview = this.props.showPreview;
    let oMediaData = this.props.mediaData;
    let sExtension = oMediaData.extension || '';
    let sSourceSRC = "";
    let aAssetView = [];

    if (CS.isEmpty(oMediaData.assetObjectKey)) {

      sSourceSRC = oMediaData.thumbKeySrc;
      if(CS.isNotEmpty(sSourceSRC)){
        var oImageView = sSourceSRC ? <ImageSimpleView classLabel="" imageSrc={sSourceSRC}/> : <div className="imageNotFound">{getTranslation().NO_PREVIEW_IMAGE}</div>;
        aAssetView.push(<div className="mediaPreviewDialogAssetView" key={"mediaPreviewDialogAssetView" + aAssetView.length}>
          <div className="mediaPreviewDialogAssetViewImage">
            {oImageView}
            <span className="mediaPreviewDialogAssetViewImageCenterSpan"></span>
          </div>
        </div>);
      } else{
        aAssetView.push(<p key={"previewNotAvailable" + aAssetView.length}>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
      }


    } else if (CS.includes(["jpg", "jpeg", "png", "ico", "gif"], sExtension.toLowerCase())) {

      sSourceSRC = oMediaData.imageSrc;
      if(CS.isNotEmpty(sSourceSRC)) {
        aAssetView.push(<div className="mediaPreviewDialogAssetView" key={"mediaPreviewDialogAssetView" + aAssetView.length}>
          <div className="mediaPreviewDialogAssetViewImage">
            <span className="mediaPreviewDialogAssetViewImageCenterSpan"></span>
            <ImageSimpleView classLabel="" imageSrc={sSourceSRC}/>
          </div>
        </div>);
      } else{
        aAssetView.push(<p key={"previewNotAvailable" + aAssetView.length}>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
      }

    } else if (CS.includes(["eps", "ai", "psd", "tif", "tiff"], sExtension.toLowerCase())) {

      sSourceSRC = oMediaData.thumbKeySrc;
      if(CS.isNotEmpty(sSourceSRC)){
        aAssetView.push(<div className="mediaPreviewDialogAssetView" key={"mediaPreviewDialogAssetView" + aAssetView.length}>
          <div className="mediaPreviewDialogAssetViewImage">
            <span className="mediaPreviewDialogAssetViewImageCenterSpan"></span>
            <ImageSimpleView classLabel="" imageSrc={sSourceSRC}/>
          </div>
        </div>);
      } else{
        aAssetView.push(<p key={"previewNotAvailable" + aAssetView.length}>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
      }


    } else if (CS.includes(["wmv", "avi", "mov", "flv", "mpeg", "mpg", "mp4"], sExtension.toLowerCase())) {

      if (oMediaData.mp4Src) {
        aAssetView.push(
            <div className="mediaPreviewDialogAssetView" key={"mediaPreviewDialogAssetView" + aAssetView.length}>
              <span className="mediaPreviewDialogAssetViewImageCenterSpan"></span>
              <VideoView
                  elementKey={oMediaData.assetObjectKey}
                  poster={oMediaData.thumbKeySrc}
                  videoSrc={oMediaData.mp4Src}
              />
            </div>);
      } else{
        aAssetView.push(<p key={"previewNotAvailable" + aAssetView.length}>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
      }

    } else if (CS.includes(["pdf"], sExtension.toLowerCase())) {

      let sSourceSRC = oMediaData.imageSrc;
      if(CS.isNotEmpty(sSourceSRC)){
        aAssetView.push(<embed src={sSourceSRC} alt="pdf" type="application/pdf" key={oMediaData.assetObjectKey}
                               pluginspage="http://www.adobe.com/products/acrobat/readstep2.html"/>);
      }else{
        aAssetView.push(<p key={"previewNotAvailable" + aAssetView.length}>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
      }


    } else if (CS.includes(["ppt", "pptx", "indd", "doc", "docx", "indt", "idml", "idms", "idlk"], sExtension.toLowerCase())) {

      let sSourceSRC = oMediaData.previewSrc;
      if(CS.isNotEmpty(sSourceSRC)){
        aAssetView.push(<embed src={sSourceSRC} alt="pdf" type="application/pdf" key={oMediaData.assetObjectKey}
                               pluginspage="http://www.adobe.com/products/acrobat/readstep2.html"/>);
      }else{
        aAssetView.push(<p key={"previewNotAvailable" + aAssetView.length}>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
      }


    } else if (ViewUtils.isXlsOrXlsxFile(sExtension) || ViewUtils.isObjStpFbxFile(sExtension)) {
      aAssetView.push(<p key={"previewNotAvailable" + aAssetView.length}>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
    }

    let sTitle = this.props.title;
    let oTitleView = null;
    if (sTitle) {
      oTitleView = (
          <div className="matchAndMergeViewTitle">
            {sTitle + " :"}
          </div>
      );
    }

    let oStyle = {
      'height': '80%',
      'width': '75%',
      'max-width': '75%'
    };
    return (<Dialog
        open={bShowPreview}
        className="mediaPreviewDialog customDialog"
        /*contentClassName="mediaPreviewDialogContent"*/
        onClose={this.closeMediaPreviewDialogView}
        /*bodyClassName="mediaPreviewDialogBody"*/
        PaperProps={{style :oStyle}}
    >
      <div className="mediaPreviewPopupEditSectionContainer">
        <div className="mediaPreviewPopupHeader">
          {oTitleView}
          <div className="mediaPreviewCloseButton" onClick={this.closeMediaPreviewDialogView}></div>
        </div>
        <div className="mediaPreviewEditSection">
          <div className="mediaPreviewDialogAssetView">
            <div className="mediaPreviewDialogAssetViewImage">
              {aAssetView}
            </div>
          </div>
        </div>
      </div>
    </Dialog>);

  }

  render() {
    return (
        <div className="mediaPreviewDialogView">
          {this.getMediaPreviewDialogView()}
        </div>
    );
  }

}

MediaPreviewDialogView.propTypes = oPropTypes;

export const view = MediaPreviewDialogView;
export const events = oEvents;
