import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import ImageViewModel from './../../contentimageview/model/content-image-view-model';
import { view as ImageView } from './../../contentimageview/content-image-view';
import ThumbnailItemModel from './../model/thumbnail-model';
import ViewUtils from '../../utils/view-library-utils';
import { view as ImageSimpleView } from '../../imagesimpleview/image-simple-view';
import { view as ThumbnailActionItemView } from './thumbnail-action-item-view';
import { view as ThumbnailInformationIndicatorIconsView } from '../../thumbnailview2/templates/thumbnail-information-indicator-icons-view';
import { view as ImageFitToContainerView } from '../../imagefittocontainerview/image-fit-to-container-view';
import { view as RadioButtonView } from '../../radiobuttonview/radio-button-view';
import DefaultImage from '../../tack/mock/mock-data-for-default-image-dictionary';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../tooltipview/tooltip-view';
import MockDataForDefaultImage from '../../tack/mock/mock-data-for-default-image-dictionary';

const oEvents = {
  THUMB_SINGLE_CLICKED: "thumb_single_clicked",
  THUMB_CHECKBOX_CLICKED: "thumb_checkbox_clicked",
};

const oPropTypes = {
  id: ReactPropTypes.string,
  name: ReactPropTypes.string,
  className: ReactPropTypes.string,
  imageSrc: ReactPropTypes.string,
  type: ReactPropTypes.string,
  tags: ReactPropTypes.array,
  theme: ReactPropTypes.string,
  onSingleClickHandler: ReactPropTypes.func,
  thumbnailViewModel: ReactPropTypes.instanceOf(ThumbnailItemModel).isRequired,
  filterContext: ReactPropTypes.object.isRequired,
  zoomLevel: ReactPropTypes.number
};
/**
 * @class ThumbnailTemplateViewNew - ThumbnailTemplateViewNew is used to display Contents.
 * @memberOf Views
 * @property {string} [id] - Contains id of Content.
 * @property {string} [name] - Contains name of content.
 * @example name = New Single Article
 * @property {string} [className] - Contains the label of primary type.(for example: Single Article)
 * @example className = Single Article
 * @property {string} [imageSrc] - Contains the image path of content.
 * @property {string} [type] -
 * @property {array} [tags] -
 * @property {string} [theme] - Contains content type(PIM and MAM). MAM is used for assets and PIM is used for remaining all.
 * @property {func} [onSingleClickHandler] -  Execute when thumbnail item is clicked.
 * @property {custom} thumbnailViewModel - Contains a view.
 * @property {number} [zoomLevel] - Contains zoom level.(We have two levels- 1 and 2)
 */

// @CS.SafeComponent
class ThumbnailTemplateViewNew extends React.Component {

  constructor (props) {
    super(props);

    this.state = {
      showPreview: false,
    }
  }

  handleThumbnailCheckboxClicked = (oModel, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.THUMB_CHECKBOX_CLICKED, this, oModel);
  };

  handleSingleClicked = (oModel, oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      if (this.props.onSingleClickHandler) {
        this.props.onSingleClickHandler(oModel, oEvent);
        return;
      }
      oEvent.nativeEvent.dontRaise = true;
      EventBus.dispatch(oEvents.THUMB_SINGLE_CLICKED, oEvent, oModel, this.props.filterContext);
    }
  };

  getDefaultImageClassName = (sType)=> {
    return MockDataForDefaultImage[sType] || "";
  };

  getSelectionView = () => {
    let oModel = this.props.thumbnailViewModel;
    let bIsDisabled = oModel.properties['disableCheck'];
    let bIsCheckToggleDisabled = oModel.properties['disableCheckToggle'];
    let bIsSelected = oModel.properties['isSelected'];
    let bShowRadioButton = oModel.properties['showRadioButton'];
    let oSelectDom = {};
    if (!bIsDisabled) {
      let fOnCheckHandler = bIsCheckToggleDisabled ? null : this.handleThumbnailCheckboxClicked.bind(this, oModel);
      if (bShowRadioButton) {
        oSelectDom = (
            <div className={bIsSelected ? "thumbnailRadioButtonIcon isSelected" : "thumbnailRadioButtonIcon"}>
              <RadioButtonView context="thumbnailRadioButton"
                               selected={bIsSelected}
                               clickHanlder={fOnCheckHandler}/>
            </div>);
      } else {
        oSelectDom = (
            <div className={bIsSelected ? "thumbnailCheckButtonIcon isSelected" : "thumbnailCheckButtonIcon"}
                 onClick={fOnCheckHandler}/>);
      }
      return (
          <div className="thumbnailTemplateHeaderView">
            {oSelectDom}
          </div>
      );
    }
  };

  getDefaultImageClassName =(sType)=> {
    return DefaultImage[sType] || "";
  };

  getThumbnailImageView = () => {
    var sType = this.props.type;
    var oProperties = {};
    var oImageViewStyle = {};
    let oThumbnailViewModel = this.props.thumbnailViewModel;
    let oThumbnailViewModelProperties = oThumbnailViewModel.properties;

    var oImageViewModel = new ImageViewModel(
        '',
        this.props.imageSrc,
        sType,
        "thumbnailImagePreview",
        oImageViewStyle,
        oProperties
    );
    let sTypeClassName = this.getDefaultImageClassName(sType);

    let sImageClassName = this.getDefaultImageClassName(sType);

    return (<div className='imageContainer'>
      <ImageSimpleView
          imageSrc={this.props.imageSrc}
          typeClassLabel={sTypeClassName}
          previewImageSrc={oThumbnailViewModelProperties.previewImageSrc}
      />
    </div>)
  }

  getIconView = (sIcon) => {
    let oClassIconView = null;
    sIcon = ViewUtils.getIconUrl(sIcon);
    oClassIconView = <ImageFitToContainerView imageSrc={sIcon}/>;
    return oClassIconView;
  };

  getThumbnailFooterView = () => {
    let sClassName = this.props.className;
    let oModel = this.props.thumbnailViewModel;
    let oProperties = oModel.properties;
    let sContainerClassName = oProperties.containerClass || "";
    let sClassNameForIcon = "entityClassIcon " + sContainerClassName;
    let sIcon = oProperties.classIcon;
    let oIconView = null;
    /*if (sIcon) {
      sClassNameForIcon = "userDefinedIcon";
      oIconView = this.getIconView(sIcon);
    }*/

    let oActionItemsView = <ThumbnailActionItemView
        thumbnailViewModel={oModel}
        filterContext={this.props.filterContext}
        theme={this.props.theme}
    />;

    return (
        <div className="thumbnailFooterViewWrapper">
          <div className="thumbnailTemplateClassAndContentName">
            <div className="entityClassName">
              {/*<div className= {sClassNameForIcon}>{oIconView}</div>*/}
              <div className="entityClassNameText" title={sClassName}> {sClassName} </div>
            </div>
            <div className="thumbnailTemplateName" title={this.props.name}>
              {this.props.name}
            </div>
          </div>
          {oActionItemsView}
        </div>
    )

  };

  getNewAndUpdatedIndicator = () => {
    let oModel = this.props.thumbnailViewModel;
    let oContent = oModel.properties.entity;
    return <ThumbnailInformationIndicatorIconsView isRecentlyUpdated={oContent.isRecentlyUpdated}
                                                   isNewlyCreated = {oContent.isNewlyCreated}
                                                   creationLanguageData = {oContent.creationLanguageData}/>
  };

  getThumbnailView = () => {
    let oSelectionDom = this.getSelectionView();
    let oThumbnailImageView = this.getThumbnailImageView();
    let oThumbnailFooterView = this.getThumbnailFooterView();
    let oNewAndUpdatedIndicator = this.getNewAndUpdatedIndicator();
    let sClassName = "thumbnailTemplateViewContainer ";

    let oModel = this.props.thumbnailViewModel;
    let bDisableView = oModel.properties['disableView'];
    let fSingleClickHandler = null;
    if (!bDisableView) {
      fSingleClickHandler = this.handleSingleClicked.bind(this, oModel);
    }
    if (this.props.zoomLevel == 1) {
      sClassName += "basicTemplateContainerZoom1 "
    }
    return (
        <div className={sClassName} onClick={fSingleClickHandler}>
          {oSelectionDom}
          {oNewAndUpdatedIndicator}
          {oThumbnailImageView}
          {oThumbnailFooterView}
        </div>
    )
  };

  render () {
    let oModel = this.props.thumbnailViewModel;
    let bIsSelected = oModel.properties['isSelected'];
    let sClassName = bIsSelected ? "thumbnailTemplateViewNewWrapper selected" : "thumbnailTemplateViewNewWrapper";
    let oThumbnailTemplateView = this.getThumbnailView();
    return (
        <div className={sClassName}>
          {oThumbnailTemplateView}
        </div>);

  };
}

export const view = ThumbnailTemplateViewNew;
export const events = oEvents;
