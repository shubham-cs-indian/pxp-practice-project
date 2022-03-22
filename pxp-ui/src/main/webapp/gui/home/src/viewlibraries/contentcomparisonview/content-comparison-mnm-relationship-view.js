import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import ImageViewModel from '../contentimageview/model/content-image-view-model';
import ViewLibraryUtils from '../utils/view-library-utils';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import TooltipView from '../../viewlibraries/tooltipview/tooltip-view';
import { view as PaginationView } from '../../viewlibraries/paginationview/pagination-view';
import GRConstants from '../../screens/homescreen/screens/contentscreen/tack/golden-record-view-constants';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {
  CONTENT_COMPARISON_MNM_RELATIONSHIP_PAGINATION_CHANGED: "content_comparison_mnm_relationship_pagination_changed",
  CONTENT_COMPARISON_MNM_REMOVE_RELATIONSHIP_BUTTON_CLICKED: "content_comparison_mnm_remove_relationship_button_clicked"
};

const oPropTypes = {
  property: ReactPropTypes.object,
  viewContext: ReactPropTypes.string,
  tableId: ReactPropTypes.string
};

const oDefaultProps = {
  sliderImageWrapperDimension: "70px",
  scrollButtonWidth: "20px",
  countOfShiftingImagesInSlider: 3,
};

// @CS.SafeComponent
class ContentComparisonMnmRelationshipView extends React.Component {

  constructor (props) {
    super(props);
  }

  handlePaginationChanged = (oPaginationData, oFilterContext, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let oProperty = this.props.property;
    let sPropertyId = oProperty.id;
    let sViewContext = this.props.viewContext;
    let sTableId = this.props.tableId;
    EventBus.dispatch(oEvents.CONTENT_COMPARISON_MNM_RELATIONSHIP_PAGINATION_CHANGED, sViewContext, sPropertyId, sTableId, oPaginationData, oProperty.sourceKlassInstanceId);
  };

  handleRemoveRelationshipButtonClicked = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let oProperty = this.props.property;
    let sPropertyId = oProperty.id;
    let sTableId = this.props.tableId;
    let oPropertyToSave = {};

    oPropertyToSave.sourceKlassInstanceId = "";
    oPropertyToSave.referencedAssetsData = [];
    oPropertyToSave.paginationData = {};


    EventBus.dispatch(oEvents.CONTENT_COMPARISON_MNM_REMOVE_RELATIONSHIP_BUTTON_CLICKED, sPropertyId, sTableId, oPropertyToSave);
  };

  getRelationshipLoadMoreView = (oPaginationData) => {
    if (CS.isEmpty(oPaginationData) || oPaginationData.currentPageItems === 0) {
      return null;
    }
    oPaginationData.onChange = this.handlePaginationChanged;
    return (
        <PaginationView {...oPaginationData}/>
    )
  };

  getRelationshipContextView = (sInstanceName, aContext) => {
    let aContextView = [];
    aContextView.push(
        <div className="relationshipElementTooltipLabel">
          {sInstanceName}
        </div>
    );
    CS.forEach(aContext, function (sContextLabel) {
      aContextView.push(
          <div className="relationshipElementTooltipLabel">
            {sContextLabel}
          </div>);
    });
    return (
        <div className="relationshipElementTooltipContainer">
          {aContextView}
        </div>
    )
  };

  getImagesView = (aReferencedAssetsData) => {
    let _this = this;
    let aImageList = [];
    CS.forEach(aReferencedAssetsData, function (oAsset) {
      let oAssetInstance = oAsset.assetData;
      let sThumbnailImageSrc = oAssetInstance.thumbKeySrc;
      let sImageType = oAssetInstance.extension;
      let oTooltipLabelDom = _this.getRelationshipContextView(oAsset.instanceName, oAsset.context);

      let oProperties = {};
      let oImageStyle = {};
      let oImageViewModel = new ImageViewModel(
          '',
          sThumbnailImageSrc,
          sImageType,
          "images",
          oImageStyle,
          oProperties
      );

      let imageWrapperClassName = "sliderImageWrapper";

      let oImageView = null;
      if (ViewLibraryUtils.isXlsOrXlsxFile(sImageType)) {
        imageWrapperClassName += " xlsIcon";
      } else if (ViewLibraryUtils.isObjStpFbxFile(sImageType)) {
        imageWrapperClassName += sImageType === "obj" && " objIcon";
        imageWrapperClassName += sImageType === "stp" && " stpIcon";
        imageWrapperClassName += sImageType === "fbx" && " fbxIcon";
      }
      else {
        oImageView = <ImageSimpleView imageSrc={oImageViewModel.imageSrc} classLabel={"sliderImage"}/>;
      }

      aImageList.push(
          <TooltipView placement="bottom" label={oTooltipLabelDom}>
            <div className={imageWrapperClassName}>
              {oImageView}
            </div>
          </TooltipView>
      );
    });

    return (
        <div className='imagesContainer'>
          {aImageList}
        </div>
    );
  };

  render () {
    let _this = this;
    let __props = _this.props;
    let oProperty = __props.property;
    let aReferencedAssetsData = oProperty.referencedAssetsData;
    let oPaginationData = oProperty.paginationData;
    let sViewContext = this.props.viewContext;
    let oRemoveRelationshipButtonView = (CS.isEmpty(aReferencedAssetsData) || sViewContext === GRConstants.PROPERTY_DETAIL_VIEW) ? null :
                                        <TooltipView placement="bottom" label={getTranslation().REMOVE_RELATIONSHIP}>
                                        <div className={"removeRelationshipButtonView"} onClick={this.handleRemoveRelationshipButtonClicked}/>
                                        </TooltipView>

    return (
        <div className="mnmRelationshipViewContainer">
          <div className="imageSliderView">
            {this.getImagesView(aReferencedAssetsData)}
            {oRemoveRelationshipButtonView}
          </div>
          {this.getRelationshipLoadMoreView(oPaginationData)}
        </div>
    );
  }
}

ContentComparisonMnmRelationshipView.propTypes = oPropTypes;
ContentComparisonMnmRelationshipView.defaultProps = oDefaultProps;

export const view = ContentComparisonMnmRelationshipView;
export const events = oEvents;
