import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ReactDom from 'react-dom';
import ListItemModel from './../model/detailed-list-item-model';
import ImageViewModel from './../../contentimageview/model/content-image-view-model';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ImageView } from './../../contentimageview/content-image-view';
import { view as ListButtonsView } from './list-buttons-view';
import { view as ListAdditionalInformationView } from './list-additional-information-view';
import { view as ThumbnailActionItemView } from '../../thumbnailview2/templates/thumbnail-action-item-view';
import TooltipView from './../../tooltipview/tooltip-view';
import { view as ThumbnailInformationIndicatorIconsView } from '../../thumbnailview2/templates/thumbnail-information-indicator-icons-view';
import MockDataForBaseType from '../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';

const oEvents = {
  THUMB_SINGLE_CLICKED: "thumb_single_clicked",
};

const oPropTypes = {
  id: ReactPropTypes.string,
  name: ReactPropTypes.string,
  className: ReactPropTypes.string,
  imageSrc: ReactPropTypes.string,
  type: ReactPropTypes.string,
  tags: ReactPropTypes.array,
  theme: ReactPropTypes.string,
  listItemModel: ReactPropTypes.instanceOf(ListItemModel).isRequired
};
/**
 * @class ListBasicTemplateViewZoom4
 * @memberOf Views
 * @property {string} [id] - Id of content.
 * @property {string} [name] - Name of the content.
 * @property {string} [className] - Contains label of primary type.
 * @property {string} [imageSrc] - Contains image path.
 * @example imagesrc = "asset/Image/74e64c5b-d6a1-4379-9717-07c2ae8778ac".
 * @property {string} [type]
 * @property {array} [tags]
 * @property {string} [theme] - (PIM or MAM).
 * @property {custom} listItemModel - Contains information about content(for example: className, id, imageSrc, label, properties etc).
 */

// @CS.SafeComponent
class ListBasicTemplateViewZoom4 extends React.Component {

  constructor(props) {
    super(props);
  }

  _handleSingleClicked =(oModel, oEvent)=> {
    if(!oEvent.nativeEvent.dontRaise) {
      oEvent.nativeEvent.dontRaise = true;
      EventBus.dispatch(oEvents.THUMB_SINGLE_CLICKED, oEvent, oModel);
    }
  }

  getThumbnailImageView =()=>{
    var sType = this.props.type;
    let oModel = this.props.listItemModel;
    let oImageProperties = oModel.properties;
    let sPreviewImageSrc = oImageProperties.previewImageSrc ? oImageProperties.previewImageSrc : "";
    let oProperties = {
      previewImageSrc: sPreviewImageSrc
    };
    var oImageViewStyle = {};

    var oImageViewModel = new ImageViewModel(
        '',
        this.props.imageSrc,
        sType,
        "thumbnailImagePreview",
        oImageViewStyle,
        oProperties
    );

    return (<ImageView model={oImageViewModel} onLoad={this.handleImageOnLoad}/>);
  };

  getNewAndUpdatedIndicator = () => {
    let oModel = this.props.listItemModel;
    let oEntity = oModel.properties.entity;
    return <ThumbnailInformationIndicatorIconsView isRecentlyUpdated={oEntity.isRecentlyUpdated}
                                                   isNewlyCreated = {oEntity.isNewlyCreated}
                                                   creationLanguageData = {oEntity.creationLanguageData}/>
  };

  render() {

    var sClassName = this.props.className;
    var sEntityName = this.props.name;
    var sTheme = this.props.theme;
    var oModel = this.props.listItemModel;

    var isSelected = oModel.properties['isSelected'];
    // var oSelectOverlayView = isSelected ? <div className="thumbSelectOverlay"></div> : null;
    var fSingleClickHandler = null;
    var bDisableView = oModel.properties['disableView'];
    var oCursorStyle = {};
    var sIsOpenable = "";

    if (!bDisableView) {
      sIsOpenable = " isOpenable ";
      oCursorStyle.cursor = "pointer";
      fSingleClickHandler = this._handleSingleClicked.bind(this, oModel);
    }

    var oImageView = this.getThumbnailImageView();
    let oEntity = oModel.properties.entity;
    let oNewAndUpdatedIndicator = oEntity.baseType !== MockDataForBaseType.OnboardingFileInstanceBaseType ? this.getNewAndUpdatedIndicator() : null;

    return (
        <div className={"basicTemplateContainerZoom4 " + sTheme + sIsOpenable} onClick={fSingleClickHandler} style={oCursorStyle}>

          <ListButtonsView getSelectButton={true} isSelected={isSelected} listItemModel={oModel}/>

          <div className="entityImageContainer">
            {oImageView}
          </div>

          <div className="flexBox">
            {/*primary information section*/}
            <div className="primaryInformationSection">
              <div className="entityName">{sEntityName}</div>
              <div className="className">{sClassName}</div>

              {/*tags section*/}
              <div className="tagInformationContainer">
                <ListAdditionalInformationView
                    getTags={true}
                    listItemModel={oModel}
                />
              </div>

            </div>

            {/*buttons section*/}
            <div className="listViewIndicatorsWrapper">
              <div className="listViewActionItemsAndInformationViewWrapper">
                <ThumbnailActionItemView thumbnailViewModel={oModel}/>
              </div>
              <div className="listViewNewAndUpdatedFlagWrapper">
                {oNewAndUpdatedIndicator}
              </div>
            </div>
          </div>

          {/*additional information section*/}
          <ListAdditionalInformationView
              getShortDescription={true}
              getLongDescription={true}
              listItemModel={oModel}
          />

        </div>
    );
  }
}

ListBasicTemplateViewZoom4.propTypes = oPropTypes;

export const view = ListBasicTemplateViewZoom4;
export const events = oEvents;
