import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ImageGalleryDialogViewModel from './model/image-gallery-dialog-view-model';
import { view as VariantImageSelectionView } from './variant-image-selection-view';
import { view as CustomDialogView } from '../customdialogview/custom-dialog-view';

const oEvents = {
  CLOSE_VARIANT_IMAGES: "close_variant_images"
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(ImageGalleryDialogViewModel).isRequired,
  onClose: ReactPropTypes.func
};
/**
 * @class ImageGalleryDialogView -
 * @memberOf Views
 * @property {custom} model - Contain activeEntityDetails, assetList, dialogVisibilityStatus, id, properties etc.
 * @property {func} [onClose] - Executes when the dialog view is closed.
 */

// @CS.SafeComponent
class ImageGalleryDialogView extends React.Component {


  constructor(props) {
    super(props);
  }

  handleClose =(sContext)=> {
    EventBus.dispatch(oEvents.CLOSE_VARIANT_IMAGES, sContext);
    this.props.onClose.call(this);
  }

  handleButtonClick =(sButtonId)=> {
    var oModel = this.props.model;
    var sContext = oModel.properties['context'];
      this.handleClose(sContext);
  }

  getViewer =()=> {
    var oModel = this.props.model;
    var aAssetList = oModel.assetList || [];
    var bDialogOpenStatus = oModel.dialogVisibilityStatus;
    var oActiveEntityDetails = oModel.activeEntityDetails;

    var oBodyStyle = {
      padding: 0,
      maxHeight: 'none'
    };
    var oContentStyle = {
      width: '95%',
      height: '80%',
      maxWidth: 'none'
    };
    var sEntityId = '';
    var sDefaultAssetInstanceId = '';
    if(!CS.isEmpty(oActiveEntityDetails)) {
      sEntityId = oActiveEntityDetails.id;
      sDefaultAssetInstanceId = oActiveEntityDetails.defaultAssetInstanceId;
    }
    var oButtonStyle = {
      "margin": "0 5px",
      "height": "30px",
      "lineHeight": "30px"
    };

    var oButtonLabelStyle = {
      fontSize: 12,
      lineHeight: "30px"
    };

    var sContext = oModel.properties['context'];
    var bHasCloseIcon = oModel.hasCloseIcon;

    var aButtonData = [];
    if(bHasCloseIcon || CS.isEmpty(aAssetList)){
      aButtonData = [{
        id: "ok",
        label: getTranslation().OK,
        isFlat: false
      }];
    }
    else {
      aButtonData=[
        {
          id: "cancel",
          label: getTranslation().CANCEL,
          isFlat: true
        },
        {
          id: "save",
          label: getTranslation().SAVE,
          isFlat: false
        }
      ];
    }

    let sBodyClassName = oModel.properties['bodyClassName'] ? oModel.properties['bodyClassName'] : "coverflowAssetDetailsViewModel";
    let sContentClassName = oModel.properties['contentClassName'] ? oModel.properties['contentClassName'] : "coverflowAssetDetailsViewModelDialog";
    let sTitle = oModel.properties['title'] ? oModel.properties['title'] : "";
    let fButtonHandler = this.handleButtonClick;

    /*{bHasCloseIcon || CS.isEmpty(aAssetList)? (<div className="variantSectionHandler">
      <RaisedButton
          label={getTranslation().OK}
          style={oButtonStyle}
          buttonStyle={oButtonStyle}
          labelStyle={oButtonLabelStyle}
          secondary={true}
          backgroundColor="#555"
          onTouchTap={this.handleClose.bind(this, sContext)}
      />
    </div>) : (<div className="variantSectionHandler">
      <RaisedButton
          label={getTranslation().CANCEL}
          style={oButtonStyle}
          buttonStyle={oButtonStyle}
          labelStyle={oButtonLabelStyle}
          secondary={true}
          backgroundColor="#555"
          onTouchTap={this.handleClose.bind(this, sContext)}
      />
      <RaisedButton
          label={getTranslation().SAVE}
          secondary={true}
          style={oButtonStyle}
          buttonStyle={oButtonStyle}
          labelStyle={oButtonLabelStyle}
          backgroundColor="#555"
          onTouchTap={this.handleSaveVariantImage.bind(this, sContext)}
      />
    </div>)}*/

    return (
        <CustomDialogView modal={false}
                          open={bDialogOpenStatus}
                          bodyClassName={sBodyClassName}
                          bodyStyle={oBodyStyle}
                          contentStyle={oContentStyle}
                          contentClassName={sContentClassName}
                          buttonData={aButtonData}
                          title={sTitle}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          buttonClickHandler={fButtonHandler}>
          <div className="variantSectionImageContainer">
            <div className="variantImageWrapper">
              <VariantImageSelectionView
                  assetList={aAssetList}
                  selectedEntityId={sDefaultAssetInstanceId}
                  variantId={sEntityId}
                  context={sContext}
              />
            </div>

          </div>

        </CustomDialogView>
    );
  }

  render() {
    return (
        <div className="imageGalleryDialogViewContainer">

          {this.getViewer()}
        </div>
    );
  }
}

ImageGalleryDialogView.propTypes = oPropTypes;

export const view = ImageGalleryDialogView;
export const events = oEvents;
