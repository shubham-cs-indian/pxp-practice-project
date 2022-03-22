import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ViewUtils from './../utils/view-library-utils';
import { view as ImageFitToContainerView } from './../imagefittocontainerview/image-fit-to-container-view';

const oEvents = {
  GRID_IMAGE_PROPERTY_IMAGE_CHANGED: "grid_image_property_image_changed",
  GRID_GET_ASSET_EXTENSIONS: "grid_get_asset_extensions"
};

const oPropTypes = {
  propertyId: ReactPropTypes.string,
  contentId: ReactPropTypes.string,
  icon: ReactPropTypes.string,
  onChange: ReactPropTypes.func,
  limitImageSize: ReactPropTypes.bool,
  pathToRoot: ReactPropTypes.string, //only in case of tags
  isIconImageType: ReactPropTypes.bool, //only in case of showing image asset as icon
  defaultImageClass: ReactPropTypes.string, //only in case of showing image asset as icon,
  isDisabled: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  iconKey: ReactPropTypes.string,
};
/**
 * @class GridImagePropertyView - Used for showing image on grid view.
 * @memberOf Views
 * @property {string} [propertyId] -
 * @property {string} [contentId] - Content Id
 * @property {string} [icon] - Icon Id.
 * @property {func} [onChange] - Execute when setting image to content.
 * @property {bool} [limitImageSize] - To keep the image size limited.
 * @property {string} [pathToRoot] - Contains parent Ids(ex.grant parent id, parent id );
 * @property {bool} [isIconImageType] - If true image type is "image" else "icon".
 * @property {string} [defaultImageClass] - Class name for image.
 * @property {bool} [isDisabled] - Used for disabling image, If true you cannot upload or replace the image.
 */

class GridImagePropertyView extends  React.Component {

  constructor(props) {
    super(props);

    var sRef = "iconUpload" + this.props.contentId;
    this[sRef] = React.createRef();
  }

  shouldComponentUpdate(oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  }

  componentDidUpdate() {
    var sRef = "iconUpload" + this.props.contentId;
    if(this[sRef].current) {
      this[sRef].current.value = '';
    }
  }

  handleImageChange =(sContentId, sPropertyId, oEvent)=> {
    var aFiles = oEvent.target.files;
    EventBus.dispatch(oEvents.GRID_IMAGE_PROPERTY_IMAGE_CHANGED, sContentId, sPropertyId, aFiles, this.props.pathToRoot, this.props.limitImageSize, this.props.context);
  }

  handleClearImageClicked =(sContentId, sPropertyId)=> {
    EventBus.dispatch(oEvents.GRID_IMAGE_PROPERTY_IMAGE_CHANGED, sContentId, sPropertyId, [], this.props.pathToRoot, "", this.props.context);
  }

  handleUploadOrReplaceIconClicked =(sContentId, sPropertyId)=> {
    if(this.props.isDisabled){
      return
    }

    var sRef = "iconUpload" + sContentId;
    EventBus.dispatch(oEvents.GRID_GET_ASSET_EXTENSIONS, this[sRef].current, "allAssets", sContentId, sPropertyId, this.props.context, this.props.pathToRoot);
  }

  render() {
    var sImageSrc;
    var sActionIconClass = "gridImageActionIcon ";
    if (this.props.icon) {
      let sIconKey = this.props.iconKey ? this.props.iconKey : this.props.icon;
      sActionIconClass += " replace ";
      sImageSrc = ViewUtils.getIconUrl(sIconKey, this.props.isIconImageType);
    } else {
      sActionIconClass += " upload ";
      sImageSrc = "";
    }

    var sTypeClassLabel = this.props.isIconImageType && CS.isEmpty(sImageSrc) ? this.props.defaultImageClass : "";

    if(this.props.isDisabled){
      sActionIconClass = "gridImageActionIcon ";
    }

    let bIsUploadIconDOM = this.props.hideIconUpload;

    let oUploadImageDOM = () => {
      return(
          <React.Fragment>
            <div className="gridImageActionIconContainer">
              <div className={sActionIconClass}
                   onClick={this.handleUploadOrReplaceIconClicked.bind(this, this.props.contentId, this.props.propertyId)}></div>
            </div>
            <input style={{"visibility": "hidden", height: "0", width: "0", position: "absolute", top: "0"}}
                   ref={this["iconUpload" + this.props.contentId]}
                   onChange={this.handleImageChange.bind(this, this.props.contentId, this.props.propertyId)}
                   type="file"
            />
          </React.Fragment>
    )
    };
    return (
        <div className="gridImagePropertyView">
          <div className="gridImageContainer">
            <ImageFitToContainerView imageSrc={sImageSrc} typeClassLabel={sTypeClassLabel}/>
            {this.props.icon && !this.props.isDisabled? <div className="gridImageClearButton" onClick={this.handleClearImageClicked.bind(this, this.props.contentId, this.props.propertyId)}></div> : null}
          </div>
          {bIsUploadIconDOM ? null : oUploadImageDOM()}
        </div>
    );
  }

}

GridImagePropertyView.propTypes = oPropTypes;

export const view = GridImagePropertyView;
export const events = oEvents;
