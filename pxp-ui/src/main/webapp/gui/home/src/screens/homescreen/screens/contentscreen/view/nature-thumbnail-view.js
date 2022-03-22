import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../../../../../screens/homescreen/screens/contentscreen/store/helper/screen-mode-utils';
import { view as ImageFitToContainerView } from '../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
import ThumbnailModel from '../../../../../viewlibraries/thumbnailview2/model/thumbnail-model';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import oFilterPropType from '../tack/filter-prop-type-constants';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  NATURE_THUMBNAIL_DELETE_BUTTON_CLICKED: "nature_thumbnail_delete_clicked",
  NATURE_THUMBNAIL_VIEW_BUTTON_CLICKED: "nature_thumbnail_view_clicked",
  NATURE_THUMBNAIL_SELECT_BUTTON_CLICKED: "nature_thumbnail_select_button_clicked"
};

const oPropTypes = {
  thumbnailModel: ReactPropTypes.instanceOf(ThumbnailModel).isRequired,
  price: ReactPropTypes.string,
  index: ReactPropTypes.number,
  nature: ReactPropTypes.string,
  pricing: ReactPropTypes.array,
  relationshipId: ReactPropTypes.string
};

// @CS.SafeComponent
class NatureThumbnailView extends React.Component {

  static propTypes = oPropTypes;

  constructor (oProps){
    super(oProps);
    let oThumbModel = this.props.thumbnailModel;
    let iCount = oThumbModel.properties['count'];
    this.state = {
      count: iCount == 0 ? "" : (iCount || 1)
    }
  }

  static getDerivedStateFromProps(oNewProps, oState){
    let oThumbModel = oNewProps.thumbnailModel;
    let iCount = oThumbModel.properties['count'];
    return {
      count: iCount == 0 ? "" : (iCount || 1)
    }
  }

  getPricingView = () => {
    var aPricingData = this.props.pricing;
    var aPricingSets = [];

    CS.forEach(aPricingData, function (aPricingSet) {
      var aPricingElements = [];

      CS.forEach(aPricingSet, function (oPriceElement) {
        aPricingElements.push(
            <div className={"priceElement " + oPriceElement.type}>
              {oPriceElement.value}
            </div>
        );
      });

      aPricingSets.push(
          <div className="pricingSet">
            {aPricingElements}
          </div>
      );
    });

    return (
        <div className="productPricing">
          {aPricingSets}
        </div>
    );
  };

  getProductInformationViewAccordingToNature = () => {
    var oModel = this.props.thumbnailModel;
    return (
        <div className="productInformationContainer">
          <div className="productName">{CS.getLabelOrCode(oModel)}</div>
          {/*<div className="productPrice">{this.props.price}</div>*/}
          {this.getPricingView()}
        </div>
    );

  };


  handleNatureThumbnailDeleteClicked = () => {
    var oThumbModel = this.props.thumbnailModel;
    EventBus.dispatch(oEvents.NATURE_THUMBNAIL_DELETE_BUTTON_CLICKED, oThumbModel);
  };

  handleNatureThumbnailViewClicked = () => {
    var oThumbModel = this.props.thumbnailModel;
    EventBus.dispatch(oEvents.NATURE_THUMBNAIL_VIEW_BUTTON_CLICKED, oThumbModel, {
      filterType: oFilterPropType.MODULE,
      screenContext: oFilterPropType.MODULE,
    });
  };

  handleNatureThumbnailSelectClicked = () => {
    var oThumbModel = this.props.thumbnailModel;
    var sRelationshipId = this.props.relationshipId;
    EventBus.dispatch(oEvents.NATURE_THUMBNAIL_SELECT_BUTTON_CLICKED, oThumbModel, sRelationshipId);
  };

  getThumbnailButtonView = () => {
    var oModel = this.props.thumbnailModel;
    var bIsSelected = oModel.properties['isSelected'];
    var sSelectLabel = getTranslation().SELECT;
    if(bIsSelected) {
      sSelectLabel = getTranslation().DESELECT;
    }
    var bIsDisableDelete = oModel.properties['disableDelete'];
    var bIsRemoveIcon = oModel.properties['isRemoveIcon'];

    var oDeleteButton = null;
    if (!bIsDisableDelete) {
      if(bIsRemoveIcon) {
        oDeleteButton = <TooltipView label={getTranslation().REMOVE}>
          <div className="natureThumbnailButton removeButton"
               onClick={this.handleNatureThumbnailDeleteClicked}></div>
        </TooltipView>
      } else {
        oDeleteButton = <TooltipView label={getTranslation().DELETE}>
          <div className="natureThumbnailButton deleteButton"
               onClick={this.handleNatureThumbnailDeleteClicked}></div>
        </TooltipView>;
      }
    }

    var oViewButton = oModel.properties['disableView'] ? null : (
            <TooltipView label={getTranslation().VIEW}>
              <div className="natureThumbnailButton viewButton"
                   onClick={this.handleNatureThumbnailViewClicked}></div>
            </TooltipView>);

    var oSelectButton = oModel.properties['disableCheck'] ? null : (
            <TooltipView label={sSelectLabel}>
              <div className="natureThumbnailButton selectButton"
                   onClick={this.handleNatureThumbnailSelectClicked}></div>
            </TooltipView>);

    return (
        <div className="natureThumbnailButtonContainer">
          {oSelectButton}
          {oViewButton}
          {oDeleteButton}
        </div>
    );
  };

  render() {
    var oModel = this.props.thumbnailModel;
    var oProductInformationView = this.getProductInformationViewAccordingToNature();
    var oThumbnailButtonView = this.getThumbnailButtonView();

    var sSelectedThumbClass = oModel.properties["isSelected"] ? " selected" : "";

    return (
        <div className="natureThumbnailView">
          <div className="mainThumbnailWrapper">
            <div className="productImageCounterWrapper">
              <div className="productImageContainer">
                <div className={"productImage" + sSelectedThumbClass}>
                  <ImageFitToContainerView imageSrc={oModel.imageSrc}/>
                </div>
                <div className="onHoverSection">
                  {oThumbnailButtonView}
                </div>
              </div>
            </div>
            {oProductInformationView}
          </div>
        </div>
    );
  }
}

export const view = NatureThumbnailView;
export const events = oEvents;
