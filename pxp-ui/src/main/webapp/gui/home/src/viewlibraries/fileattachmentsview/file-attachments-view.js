import CS from '../../libraries/cs';
import React from 'react';
import ReactDom from 'react-dom';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ResizeSensor from 'css-element-queries/src/ResizeSensor';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import Dialog from '@material-ui/core/Dialog';
import { view as VideoView } from '../videoview/video-view';
import ViewUtils from '../utils/view-library-utils';

const oEvents = {
  FILE_ATTACHMENT_VIEW_FILE_UPLOAD_CLICKED: 'file_attachment_view_file_upload_clicked',
  FILE_ATTACHMENT_VIEW_REMOVE_FILE: 'file_attachment_view_remove_file',
  FILE_ATTACHMENTS_DETAIL_VIEW_OPEN: 'file_attachments_detail_view_open',
  FILE_ATTACHMENTS_DETAIL_VIEW_CLOSE: 'file_attachments_detail_view_close',
  FILE_ATTACHMENT_VIEW_GET_ALL_ASSET_EXTENSIONS: "file_attachment_view_get_all_asset_extensions",
};

const oPropTypes = {
  context: ReactPropTypes.string,
  attachmentsData: ReactPropTypes.array,
  isUploadDisabled: ReactPropTypes.bool,
  attachmentDetailViewData: ReactPropTypes.string
};
/**
 * @class FileAttachmentsView - Use for attach files in task tab.
 * @memberOf Views
 * @property {string} [context] - Pass task file attachment context.
 * @property {array} [attachmentsData] - Pass attached data in array.
 * @property {bool} [isUploadDisabled] - when true disabled upload button.
 * @property {string} [attachmentDetailViewData] - Not in use.
 */

// @CS.SafeComponent
class FileAttachmentsView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      showMore: false,
      isDetailViewOpen: false,
      openAttachmentId: ""
    }

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };
  }

  componentDidMount() {
    this.organiseAttachmentView();

  }

  componentDidUpdate() {
    this.organiseAttachmentView();
  }

  uploadAttachmentButtonClicked =()=>{
    EventBus.dispatch(oEvents.FILE_ATTACHMENT_VIEW_GET_ALL_ASSET_EXTENSIONS, this["uploadAttachment"]);
    //this["uploadAttachment"].click();
  }

  domMounted =(oView)=> {
    if(oView) {
      var _this = this;
      new ResizeSensor(oView, CS.throttle(_this.organiseAttachmentView, 150));
    }
  }

  handleMoreButtonClicked =()=> {
    var bCurrentState = this.state.showMore;
    this.setState({
      showMore: !bCurrentState,
    });
  }

  handleUploadAttachmentButtonClicked =(sContext, oEvent)=> {
    var aFiles = oEvent.target.files;
    EventBus.dispatch(oEvents.FILE_ATTACHMENT_VIEW_FILE_UPLOAD_CLICKED, sContext, aFiles);
  }

  removeAttachment =(sContext, sId)=>{
    EventBus.dispatch(oEvents.FILE_ATTACHMENT_VIEW_REMOVE_FILE, sContext, sId);
  }

  openAttachmentDetailPopup =(sAttachmentId)=> {
    EventBus.dispatch(oEvents.FILE_ATTACHMENTS_DETAIL_VIEW_OPEN, sAttachmentId);
  }

  closeAttachmentDetailPopup =()=> {
    EventBus.dispatch(oEvents.FILE_ATTACHMENTS_DETAIL_VIEW_CLOSE);
  }

/*
  onVideoError =(oEvent)=> {
    ExceptionLogger.error('Error in video source');
    ExceptionLogger.log(oEvent);
  }
*/

  hideSecondRow =(iCount)=>{
    while(iCount != 0){
      var sRef = "iconWrappersecond"+iCount;
      this[sRef].classList.add('hideAttachmentIcon');
      iCount--;
    }
  }

  organiseAttachmentView =()=>{
    var _this = this;
    var aAttachmentsData = this.props.attachmentsData;
    _this["moreLessButton"] && _this["moreLessButton"].classList.remove('showMoreLessButton');
    if(!CS.isEmpty(aAttachmentsData)){
      let oParentDOM = ReactDom.findDOMNode(this);
    /*  var iIconWidth = $(_this["iconWrapperfirst1"]).outerWidth()
          + parseInt($(_this["iconWrapperfirst1"]).css('margin-right')) +
          parseInt($(_this["iconWrapperfirst1"]).css('margin-left'));*/
    var oChildDOM = oParentDOM.getElementsByClassName("iconWrapper");
    var iIconWidth = oChildDOM[0].offsetWidth + (2 * oChildDOM[0].offsetLeft);
      var iContainerWidth = Math.floor(_this["fileAttachmentsTile"].offsetWidth);
      var iNoOfIcons = aAttachmentsData.length;
      var iNoOfAllowedIcons = Math.floor(iContainerWidth/iIconWidth);
      var iCount;
      if(iNoOfIcons > iNoOfAllowedIcons){
        _this["moreLessButton"] && _this["moreLessButton"].classList.add('showMoreLessButton');
        iNoOfAllowedIcons--;
        if(_this.state.showMore){
          var sRef = "";
          iCount = iNoOfIcons - iNoOfAllowedIcons;
          _this["moreLessCount"].innerHTML = getTranslation().LESS;
          _this["moreLessCount"].title = getTranslation().LESS;
          iCount = 1;
          while(iCount <= iNoOfIcons){
            if(iCount <= iNoOfAllowedIcons){
              sRef = "iconWrappersecond"+iCount;
              this[sRef].classList.add('hideAttachmentIcon');
            }else{
              sRef = "iconWrapperfirst"+iCount;
              this[sRef].classList.add('hideAttachmentIcon');
              sRef = "iconWrappersecond"+iCount;
              this[sRef].classList.remove('hideAttachmentIcon');
            }
            iCount++;
          }
        }else{
          iCount = iNoOfIcons - iNoOfAllowedIcons;
          _this["moreLessCount"].innerHTML =  getTranslation().MORE;
          _this["moreLessCount"].title = getTranslation().MORE;
          _this.hideSecondRow(iNoOfIcons);
          iCount = iNoOfAllowedIcons;
          iCount++;
          while(iCount <= iNoOfIcons){
            sRef = "iconWrapperfirst"+iCount;
            this[sRef].classList.add('hideAttachmentIcon');
            iCount++;
          }
        }
      }else{
        var sRef = "iconWrapperfirst"+iNoOfIcons;
        this[sRef].classList.remove('hideAttachmentIcon');
        //this[sRef].classList.add('removeRightMargin');
        _this["moreLessButton"] && _this["moreLessButton"].classList.remove('showMoreLessButton');
        _this.hideSecondRow(iNoOfIcons);
      }
    }
  }

  getAttachmentIconView =(sContext, sCallName)=> {
    var _this = this;
    var bIsUploadDisabled = this.props.isUploadDisabled;
    var aAttachments = this.props.attachmentsData;
    var sClassName = "iconRemoveImage";
    var aIconView = [];
    var sRef = "";
    var iCount = 1;

    if(bIsUploadDisabled){
      sClassName += " hideAttachmentRemoveIcon";
    }

    CS.forEach(aAttachments, function (oAttachmentObject) {
      sRef = "iconWrapper" + sCallName + iCount;
      let sIconWrapperClassName = "attachmentWrapper";
      let oIconView = null;

      let sExtension = oAttachmentObject.extension;
      if (ViewUtils.isXlsOrXlsxFile(sExtension)) {
        sIconWrapperClassName += " xlsIcon";
      }
      else if (ViewUtils.isObjStpFbxFile(sExtension)) {
        sIconWrapperClassName += sExtension=== "obj" && " objIcon";
        sIconWrapperClassName += sExtension=== "stp" && " stpIcon";
        sIconWrapperClassName += sExtension=== "fbx" && " fbxIcon";
      } else {
        oIconView = <ImageSimpleView classLabel="icon" imageSrc={oAttachmentObject.thumbKeySrc}/>;
      }

      aIconView.push(<div className="iconWrapper" ref={_this.setRef.bind(_this, sRef)} key={sRef}>
        <div className={sClassName} title={getTranslation().REMOVE_ATTACHMENT} onClick={_this.removeAttachment.bind(_this, sContext, oAttachmentObject.id)}></div>
        <div className={sIconWrapperClassName} onClick={_this.openAttachmentDetailPopup.bind(_this, oAttachmentObject.id)}>
          {oIconView}
        </div>
      </div>);
      iCount++;
    });

    return(aIconView);
  }

  getAssetView =()=> {
    var sSelectedAttachmentId = this.props.openAttachmentId;
    var oSelectedAttachment = {};
    var aAttachments = this.props.attachmentsData;
    CS.forEach(aAttachments, function (oAttachment) {
      if(sSelectedAttachmentId == oAttachment.id){
        oSelectedAttachment = oAttachment;
        return true;
      }
    });

    if (CS.isEmpty(oSelectedAttachment)) {
      return(<p>{getTranslation().ASSET_NOT_FOUND}</p>);
    }

    var sExtension = oSelectedAttachment.extension || '';

    if (CS.includes(["jpg", "jpeg", "png", "ico"], sExtension.toLowerCase())) {
      var aImageView = [];
      var sSourceSRC = oSelectedAttachment.imageSrc;
      aImageView.push(<ImageSimpleView classLabel="" imageSrc={sSourceSRC}/>);
      return(
          <div className="previewAssetViewImage">
            <span className="previewAssetViewImageCenterSpan"></span>
            {aImageView}
          </div>
      );
    } else if(CS.includes(["eps", "ai", "psd", "tif", "tiff"], sExtension.toLowerCase())) {
      var aImageView = [];
      var sSourceSRC = oSelectedAttachment.thumbKeySrc;
      aImageView.push(<ImageSimpleView classLabel="" imageSrc={sSourceSRC}/>);
      return(
          <div className="previewAssetViewImage">
            <span className="previewAssetViewImageCenterSpan"></span>
            {aImageView}
          </div>
      );
    } else if(CS.includes(["wmv", "avi", "mov", "flv", "mpeg", "mpg", "mp4"], sExtension.toLowerCase())) {
      if (oSelectedAttachment.mp4Src) {
        return(
            <div className="previewAssetViewImage">
              <span className="previewAssetViewImageCenterSpan"></span>
              <VideoView
                  elementKey={oSelectedAttachment.assetObjectKey}
                  poster={oSelectedAttachment.thumbKeySrc}
                  videoSrc={oSelectedAttachment.mp4Src}
              />
            </div>);
      }
      return;
    } else if(CS.includes(["pdf"], sExtension.toLowerCase())) {
      var sSourceSRC = oSelectedAttachment.imageSrc;
      return (<embed src={sSourceSRC} alt="pdf" type="application/pdf" key={oSelectedAttachment.assetObjectKey}
                     pluginspage="http://www.adobe.com/products/acrobat/readstep2.html"/>);
    } else if(CS.includes(["ppt", "pptx", "indd", "doc", "docx"], sExtension.toLowerCase())) {
      var sSourceSRC = oSelectedAttachment.previewSrc;
      return (<embed src={sSourceSRC} alt="pdf" type="application/pdf" key={oSelectedAttachment.assetObjectKey}
                     pluginspage="http://www.adobe.com/products/acrobat/readstep2.html"/>);
    } else {
      return(<p>{getTranslation().PREVIEW_NOT_AVAILABLE}</p>);
    }

  }

  getAttachmentDetailPopup =()=> {
    return (<Dialog
        open={true}
        className="propertyTaskDialog customDialog"
        contentClassName="propertyTaskDialogContent"
        onClose={this.closeAttachmentDetailPopup}
        bodyClassName="propertyTaskDialogBody"
    >
      <div className="propertyTaskEditSectionContainer">
        <div className="propertyTaskHeader">
          <div className="closeButton" onClick={this.closeAttachmentDetailPopup}></div>
        </div>
        <div className="propertyTaskEditSection">
          <div className="previewAssetView">
            {this.getAssetView()}
          </div>
        </div>
      </div>
    </Dialog>);
  }

  render() {
    var sContext = this.props.context;
    var bIsUploadDisabled = this.props.isUploadDisabled;
    var aAttachments = this.props.attachmentsData;
    var aAttachmentDetailPopup = [];
    var sOpenAttachmentId = this.props.openAttachmentId;

    if(CS.find(aAttachments, {id: sOpenAttachmentId})){
      aAttachmentDetailPopup.push(this.getAttachmentDetailPopup());
    }

    var sUploadIconClassName = "fileAttachmentsUploadWrapper";
    var sAttachmentTileClassName = "fileAttachmentsTile";
    if(bIsUploadDisabled){
      sUploadIconClassName += " hideAttachmentUploadButton";
      sAttachmentTileClassName += " maximumAttachmentTileWidth";
    }

    if(aAttachments && aAttachments.length){
      sUploadIconClassName += " moreHeight";
    }

    return (
        <div ref={this.domMounted}>
          <div className="fileAttachmentViewWrapper">
            {aAttachmentDetailPopup}
            <div className={sAttachmentTileClassName} ref= {this.setRef.bind(this, "fileAttachmentsTile")}>
              {this.getAttachmentIconView(sContext, "first")}
              <div className="moreLessButton" onClick={this.handleMoreButtonClicked} ref={this.setRef.bind(this, "moreLessButton")}>
                <div className="moreLessCount" ref={this.setRef.bind(this, "moreLessCount")}></div>
              </div>
              {this.getAttachmentIconView(sContext, "second")}
            </div>
            <div className={sUploadIconClassName} onClick={this.uploadAttachmentButtonClicked}>
              <div className="fileAttachmentUploadLabel">{getTranslation().ATTACH_FILES}</div>
              <div className="uploadAttachment"></div>
            </div>
          </div>
          <input style={{"display": "none", height: "0", width: "0"}}
                 ref={this.setRef.bind(this, "uploadAttachment")}
                 onChange={this.handleUploadAttachmentButtonClicked.bind(this, sContext)}
                 type="file"
                 multiple
          />
        </div>
    );
  }
}

FileAttachmentsView.propTypes = oPropTypes;


export const view = FileAttachmentsView;
export const events = oEvents;
