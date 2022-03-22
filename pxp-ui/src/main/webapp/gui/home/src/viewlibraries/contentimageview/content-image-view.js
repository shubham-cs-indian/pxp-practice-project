import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import DefaultImage from '../tack/mock/mock-data-for-default-image-dictionary';
import ImageViewModel from './model/content-image-view-model';

const oEvents = {};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(ImageViewModel).isRequired,
  onLoad: ReactPropTypes.func,
  onError: ReactPropTypes.func
};
/**
 * @class ImageView - use to display imageview.
 * @memberOf Views
 * @property {custom} model - model name of imageView.
 * @property {func} [onLoad] -  function which used when image load.
 * @property {func} [onError] -  function which used when image have some errors.
 */

// @CS.SafeComponent
class ImageView extends React.Component {

  constructor(props) {
    super(props);
  }

  getDefaultImageClassName =(sType)=> {
    return DefaultImage[sType] || "noImage";
  }

  getImage =()=> {
    var oModel = this.props.model;
    var oImage = "";
    if (!CS.isEmpty(oModel.imageSrc) || CS.isNotEmpty(oModel.properties.previewImageSrc)) {
      var sTypeClassName = this.getDefaultImageClassName(oModel.imageType);
      let sPreviewImageSrc = oModel.properties.previewImageSrc ? oModel.properties.previewImageSrc : "";
      oImage = (
          <div className="imageWrapper" style={oModel.imageStyle}>
            <ImageSimpleView classLabel={oModel.className}
                             typeClassLabel={sTypeClassName}
                             onLoad={this.props.onLoad}
                             onError={this.props.onError}
                             imageSrc={oModel.imageSrc}
                             previewImageSrc={sPreviewImageSrc}
            />
          </div>
      );
    } else {
      var sClassName = this.getDefaultImageClassName(oModel.imageType);
      oImage = (<div className={"imageWrapper "+sClassName} style={oModel.imageStyle}></div>);
    }
    return oImage;
  }

  render() {
    var oImage = this.getImage();
    return (oImage);
  }
}

ImageView.propTypes = oPropTypes;

export const view = ImageView;
export const events = oEvents;
