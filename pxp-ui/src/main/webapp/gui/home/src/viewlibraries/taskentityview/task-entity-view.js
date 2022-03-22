import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ImageViewModel from '../contentimageview/model/content-image-view-model';
import { view as ImageView } from '../contentimageview/content-image-view';
import { view as ButtonView } from '../../viewlibraries/buttonview/button-view';
import ViewUtils from '../../screens/homescreen/screens/contentscreen/view/utils/view-utils';
import LinkedEntityDictionary from '../tack/linked-entity-dictionary';

const oEvents = {
  CONTENT_TASK_DETAIL_CONTENT_NAME_CLICKED : "content_task_detail_content_name_clicked"
};

const oPropTypes = {
  taskLinkedInstanceData : ReactPropTypes.object
};

class TaskEntityView extends React.Component {
  constructor (props) {
    super(props);
  }

  handleOpenEntityButtonClicked = () => {
    let oTaskLinkedInstanceData = this.props.taskLinkedInstanceData;
    EventBus.dispatch(oEvents.CONTENT_TASK_DETAIL_CONTENT_NAME_CLICKED, oTaskLinkedInstanceData.contentId, oTaskLinkedInstanceData.baseType);
  };

    getThumbnailImageView = (oEntityType) => {
        let oTaskLinkedInstanceData = this.props.taskLinkedInstanceData;
        let oAssetInstance = oTaskLinkedInstanceData.assetInstance;
        let sThumbnailSrc = "";
        let sThumbnailType = "";
        if(oAssetInstance && oAssetInstance.thumbKey) {
            let oAssetData = ViewUtils.getImageSrcUrlFromImageObject(oAssetInstance);
            sThumbnailSrc =  oAssetData.thumbnailImageSrc;
            sThumbnailType = oAssetData.thumbnailType;
        } else if(oEntityType.previewImage) {
            sThumbnailSrc = ViewUtils.getIconUrl(oEntityType.previewImage);
            sThumbnailType = "";
        }
        let oProperties = {};
        let oImageViewStyle = {};

        let oImageViewModel = new ImageViewModel(
            '',
            sThumbnailSrc,
            sThumbnailType,
            "thumbnailImagePreview",
            oImageViewStyle,
            oProperties
        );

        return (<ImageView model={oImageViewModel} onLoad={this.handleImageOnLoad}/>);
    };

  getOpenEntityButtonView = () => {
    let oTaskLinkedData = this.props.taskLinkedInstanceData;
    let sLabel = getTranslation()[LinkedEntityDictionary[oTaskLinkedData.baseType]];
    return (
        <div className="taskOpenEntityButtonView">
          <ButtonView id={"openEntity"}
                      showLabel={true}
                      placement={"left"}
                      isDisabled={false}
                      type={"simple"}
                      onChange={this.handleOpenEntityButtonClicked}
                      theme={"light"}
                      label={sLabel}/>
        </div>
    );
  };


  render () {
    let oTaskLinkedInstanceData = this.props.taskLinkedInstanceData;
    let oReferencedKlasses = oTaskLinkedInstanceData.referencedKlasses;
    let sEntityTypeId = oTaskLinkedInstanceData.types[0];
    let oEntityType  = oReferencedKlasses[sEntityTypeId];

    let oImageView = this.getThumbnailImageView(oEntityType);
    let oOpenEntityButtonView = this.getOpenEntityButtonView();

    {/*<div className="contentDetails">
          <div className="contentName" onClick={this.handleOpenEntityButtonClicked.bind(this, oContentData.contentId, oContentData.baseType)}>{oContentData.label}</div>
        </div>*/
    }
    return (
          <div className="taskEntityViewWrapper">
            <div className="entityImageContainer">
              {oImageView}
            </div>
              <div className="primaryInformationSection">
                <div className="entityName">{oTaskLinkedInstanceData.label}</div>
                <div className="className">{oEntityType.label}</div>
                {oOpenEntityButtonView}
              </div>
          </div>
    );
  }
}



TaskEntityView.propTypes = oPropTypes;

export const view = React.getSafeComponent(TaskEntityView);
export const events = oEvents;