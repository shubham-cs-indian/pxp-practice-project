import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ImageViewModel from '../contentimageview/model/content-image-view-model';
import ViewLibraryUtils from '../utils/view-library-utils';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

const oEvents = {};

const oPropTypes = {
  assetList: ReactPropTypes.array,
  selectedImageObject: ReactPropTypes.object,
  handler: ReactPropTypes.func,
  sliderImageWrapperDimension: ReactPropTypes.string,
  scrollButtonWidth: ReactPropTypes.string,
  countOfShiftingImagesInSlider: ReactPropTypes.number,
};
/**
 * @class ImageSliderView - Use to Display Image slider in product description view.
 * @memberOf Views
 * @property {array} [assetList] - Pass list of images data like assetInstanceId, label, properties, isDefault, type in array.
 * @property {object} [selectedImageObject] - selected image data like image path, image type.
 * @property {func} [handler] - Pass function which use on image click.
 * @property {string} [sliderImageWrapperDimension] - Pass default 50px value for image dimension.
 * @property {string} [scrollButtonWidth] - Pass default 20px width for button.
 * @property {number} [countOfShiftingImagesInSlider] - Pass number of count of image.
 */

const oDefaultProps = {
  sliderImageWrapperDimension: "50px",
  scrollButtonWidth: "20px",
  countOfShiftingImagesInSlider: 3,
};

// @CS.SafeComponent
class ImageSliderView extends React.Component {

  constructor (props) {
    super(props);
    this.bIsRightScrollButtonVisible = false;
    this.iWidthDifference = 0;

    this.imageSliderView = React.createRef();
    this.imagesContainer = React.createRef();
    this.leftScrollButton = React.createRef();
    this.rightScrollButton = React.createRef();
    this.scrollContainer = React.createRef();
  }

  componentDidMount = () => {
    this.handleScrollButtonVisibility();
  };

  componentDidUpdate = () => {
    this.handleScrollButtonVisibility();
  };

  handleScrollButtonVisibility = () => {
    try {
      let oImageSliderViewDOM = this.imageSliderView.current;
      /** The Container */
      let iImageSliderViewWidth = Math.ceil(oImageSliderViewDOM.offsetWidth) || 0;
      let oImagesContainerDOM = this.imagesContainer.current;
      /** The inner section */
      let iImagesContainerWidth = Math.ceil(oImagesContainerDOM.offsetWidth) || 0;

      let oLeftScrollButton = this.leftScrollButton.current;
      let oRightScrollButton = this.rightScrollButton.current;
      oLeftScrollButton.style.visibility = "hidden";
      oRightScrollButton.style.visibility = "hidden";
      this.bIsRightScrollButtonVisible = false;

      if (iImagesContainerWidth > iImageSliderViewWidth) {
        this.iWidthDifference = iImagesContainerWidth - iImageSliderViewWidth;
        /** all possible 'left' values of the oImagesContainerViewDOM will lie between 0px and -(iWidthDifference)px */
        let iImagesContainerLeft = oImagesContainerDOM.offsetLeft;
        let iTabListLeft = Math.ceil(oImagesContainerDOM.parentElement.scrollLeft) || 0;
        if (iTabListLeft) {
          //left button visible
          oLeftScrollButton.style.visibility = "visible";
        }
        if (iTabListLeft + iImageSliderViewWidth < iImagesContainerWidth) {
          //right button visible
          oRightScrollButton.style.visibility = "visible";
        }
      }
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  handleScrollButtonClicked = (bScrollLeft) => {
    try {
      let iImageWrapperWidth = parseInt(this.props.sliderImageWrapperDimension);
      let iCountOfShiftingImagesInSlider = this.props.countOfShiftingImagesInSlider;
      let iScrollButtonWidth = parseInt(this.props.scrollButtonWidth);
      let iScrollByWidth = 0;

      let oImagesContainerDOM = this.imagesContainer.current;
      let iCurrentLeft = oImagesContainerDOM.offsetLeft;

      /** Right most position: Execute when Slider is Gone to the End and hidden right button*/
      if (!this.bIsRightScrollButtonVisible) {
        let oImagesSliderViewDOM = this.imageSliderView.current;
        let iImageSliderViewWidth = oImagesSliderViewDOM.offsetWidth;
        let iContainerWidth = iImageSliderViewWidth - iScrollButtonWidth;
        let iExtraContainerSpace = iContainerWidth % iImageWrapperWidth;
        iScrollByWidth = (iImageWrapperWidth * iCountOfShiftingImagesInSlider) - iExtraContainerSpace;
      }
      /** Move middle section when both scroll button present*/
      else if (iCurrentLeft !== 0) {
        iScrollByWidth = iImageWrapperWidth * iCountOfShiftingImagesInSlider;
      }
      /** Left most position: remove the left scroll button width to fit the image front of left scroll button */
      else {
        iScrollByWidth = (iImageWrapperWidth * iCountOfShiftingImagesInSlider) - iScrollButtonWidth;
      }

      let iNewLeft = 0;
      let oScrollContainer = this.scrollContainer;
      if (bScrollLeft) {
        iNewLeft = (iCurrentLeft + Math.ceil(oScrollContainer.current.offsetWidth)) - Math.ceil(oImagesContainerDOM.offsetWidth);
        if ((Math.abs(iNewLeft) > iScrollByWidth) || Math.abs(iNewLeft) == 0) {
          iNewLeft = -iScrollByWidth;
        }
        ;
      } else {
        iNewLeft = Math.ceil(oImagesContainerDOM.offsetWidth) - (iCurrentLeft + Math.ceil(oScrollContainer.current.offsetWidth));
        iNewLeft = (iNewLeft > iScrollByWidth) ? iScrollByWidth : iNewLeft;
      }
      ViewLibraryUtils.scrollBy(oImagesContainerDOM.parentElement, {left: iNewLeft}, "", this.handleScrollButtonVisibility);

    }
    //   if (bScrollLeft) {
    //     iNewLeft = iCurrentLeft + iScrollByWidth;
    //     if (iNewLeft > 0) {
    //       /** left cannot go into +ve values */
    //       iNewLeft = 0;
    //     }
    //   }
    //   else {
    //     iNewLeft = iCurrentLeft - iScrollByWidth;
    //     if (Math.abs(iNewLeft) > this.iWidthDifference) {
    //       /** left cannot be greater than iWidthDifference */
    //       iNewLeft = -this.iWidthDifference;
    //     }
    //   }
    //   $(oImagesContainerDOM).animate({left: iNewLeft}, 300, this.handleScrollButtonVisibility);
    // }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  handleImageClicked = (sImageId) => {
    this.props.handler(sImageId);
  };

  getImagesView = () => {
    let _this = this;
    let __props = _this.props;
    let aAssetList = __props.assetList;
    let aImageList = [];

    CS.forEach(aAssetList, function (oAsset, iIndex) {
      let oDataForThumbnail = ViewLibraryUtils.getImageSrcUrlFromImageObject(oAsset);
      let sThumbnailImageSrc = oDataForThumbnail.thumbnailImageSrc;
      let sImageType = oDataForThumbnail.thumbnailType;

      let oProperties = {};
      let oImageStyle = {};
      let oImageViewModel = new ImageViewModel(
          '',
          sThumbnailImageSrc,
          sImageType,
          "variantImage",
          oImageStyle,
          oProperties
      );

      let imageWrapperClassName = "sliderImageWrapper";
      if (__props.selectedImageObject.thumbnailImageSrc == oImageViewModel.imageSrc) {
        imageWrapperClassName = imageWrapperClassName + " selected";
      }

      let oImageView = null;
      if (ViewLibraryUtils.isXlsOrXlsxFile(sImageType)) {
        imageWrapperClassName += " xlsIcon";
      } else if (ViewLibraryUtils.isObjStpFbxFile(sImageType)) {
        imageWrapperClassName += sImageType=== "obj" && " objIcon";
        imageWrapperClassName += sImageType=== "stp" && " stpIcon";
        imageWrapperClassName += sImageType=== "fbx" && " fbxIcon";
      }
      else {
        oImageView = <ImageSimpleView imageSrc={oImageViewModel.imageSrc} classLabel={"sliderImage"}/>;
      }

      aImageList.push(
          <div className={imageWrapperClassName} key={oImageViewModel.imageSrc + iIndex}
               onClick={_this.handleImageClicked.bind(_this, oAsset.assetInstanceId)}
               style={{width: _this.props.sliderImageWrapperDimension, height: _this.props.sliderImageWrapperDimension}}>
            {oImageView}
          </div>
      );
    });

    return (
        <div className='imagesContainer' ref={this.imagesContainer}>
          {aImageList}
        </div>
    );
  };

  render () {

    return (
        <div className="imageSliderView" ref={this.imageSliderView}>

          <div className="scrollButton left" ref={this.leftScrollButton}
               style={{width: this.props.scrollButtonWidth, height: this.props.sliderImageWrapperDimension}}
               onClick={this.handleScrollButtonClicked.bind(this, true)}>
          </div>

          <div className="parentScrollContainer" ref={this.scrollContainer}>
            {this.getImagesView()}
          </div>

          <div className="scrollButton right" ref={this.rightScrollButton}
               style={{width: this.props.scrollButtonWidth, height: this.props.sliderImageWrapperDimension}}
               onClick={this.handleScrollButtonClicked.bind(this, false)}>
          </div>

        </div>
    );
  }
}

ImageSliderView.propTypes = oPropTypes;
ImageSliderView.defaultProps = oDefaultProps;

export const view = ImageSliderView;
export const events = oEvents;
