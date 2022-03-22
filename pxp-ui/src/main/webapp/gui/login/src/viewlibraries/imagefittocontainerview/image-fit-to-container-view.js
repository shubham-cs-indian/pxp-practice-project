import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';

const Events = {};

const oPropTypes = {
  imageSrc: ReactPropTypes.string,
  typeClassLabel: ReactPropTypes.string,
  previewImageSrc: ReactPropTypes.string
};
/**
 * @class ImageFitToContainerView - use for image to fit in container depend on classes.
 * @memberOf Views
 * @property {string} [imageSrc] -  string of imageSrc.
 * @property {string} [typeClassLabel] -  string of image type label.
 */

class ImageFitToContainerView extends React.Component {

  constructor(props) {
    super(props);

    this.imageFitToContainer = React.createRef();
  }

  shouldComponentUpdate(oNextProps){
    return !CS.isEqual(oNextProps, this.props);
  }

  handleImageOnLoad =(oEvent)=> {
    var iImgWidth = oEvent.target.width;
    var iImgHeight = oEvent.target.height;
    let oDOMContainer = this.imageFitToContainer.current;
    let iContainerHeight = oDOMContainer.clientHeight;
    let iContainerWidth = oDOMContainer.clientWidth;

    if ((iContainerWidth / iContainerHeight) > (iImgWidth / iImgHeight)) {
      oDOMContainer.classList.remove('landscape');
      oDOMContainer.classList.add('portrait');
    } else {
      oDOMContainer.classList.add('landscape');
      oDOMContainer.classList.remove('portrait');
    }
  }

  render() {
    let sPreviewImageSrc = this.props.previewImageSrc ? this.props.previewImageSrc : "";
    return (
        <div className="imageFitToContainer" ref={this.imageFitToContainer}>
          <ImageSimpleView typeClassLabel={this.props.typeClassLabel} classLabel="imageFitToContainerImage" onLoad={this.handleImageOnLoad} imageSrc={this.props.imageSrc} previewImageSrc={sPreviewImageSrc} />
        </div>
    )
  }
}

ImageFitToContainerView.propTypes = oPropTypes;

export const view = ImageFitToContainerView;
export const events = Events;
