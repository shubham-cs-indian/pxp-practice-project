import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import assetTypes from '../tack/coverflow-asset-type-list';
var OnBoardingImportAllowedTypes = assetTypes.onBoardingImportTypes.join(', ');

const oEvents = {
  ONBOARDING_FILE_UPLOAD_EVENT: "onboarding_file_upload_event",
  PRODUCTS_RUNTIME_IMPORT_BUTTON_CLICKED: "products_runtime_import_button_clicked",
  ONBOARDING_FILE_UPLOAD_CANCEL: "onboarding_file_upload_cancel"

};

const oPropTypes = {
  extraDataForUpload: ReactPropTypes.object,
  className : ReactPropTypes.string,
  context : ReactPropTypes.string,
  label : ReactPropTypes.string,
  dropdown: ReactPropTypes.bool,
  endpointSelected: ReactPropTypes.bool
};

// @CS.SafeComponent
class OnboardingFileUploadButtonView extends React.Component {
  constructor (oProps) {
    super(oProps);

    this.onboardingFileUpload = React.createRef();
  }
  static propTypes = oPropTypes;

  componentDidUpdate() {
    this.onboardingFileUpload.current && (this.onboardingFileUpload.current.value = null);
  }

  handleFilesSelectedToUpload = (oEvent) => {
    var aFiles = oEvent.target.files;
    if(this.props.context == "runtimeProductImport"){
      EventBus.dispatch(oEvents.PRODUCTS_RUNTIME_IMPORT_BUTTON_CLICKED, aFiles);
    }
    else{
      EventBus.dispatch(oEvents.ONBOARDING_FILE_UPLOAD_EVENT, aFiles, this.props.extraDataForUpload);
    }
  };

  handleFocusBack = () => {
    EventBus.dispatch(oEvents.ONBOARDING_FILE_UPLOAD_CANCEL);
    window.removeEventListener('focus', this.handleFocusBack);
  };

  clickedFileInput = () =>{
    window.addEventListener('focus', this.handleFocusBack);
  };

  handleAssetBulkUploadClicked = () => {
    !CS.isEmpty(this.onboardingFileUpload.current) && this.onboardingFileUpload.current.click();
  };

  render() {

    var bMultiple = false;
    let sClassName = "bulkUploadContainer";
    let sClass = CS.isNotEmpty(this.props.className)? this.props.className :"assetBulkUpload";
    let sToolTip = this.props.label? this.props.label : "";
    let oBulkUploadView = null;
    if (this.props.dropdown) {
      this.props.endpointSelected && this.handleAssetBulkUploadClicked();
    } else {
      oBulkUploadView = (
          <div className={"toolbarItemNew " + sClass} onClick={this.handleAssetBulkUploadClicked}></div>
      );
    }

    return (
        <TooltipView label={sToolTip} placement={"top"}>
        <div className ={sClassName}>
          {oBulkUploadView}
          <input style={{"visibility": "hidden"}}
                 ref={this.onboardingFileUpload}
                 onChange={this.handleFilesSelectedToUpload}
                 onClick={this.clickedFileInput}
                 type="file"
                 accept={OnBoardingImportAllowedTypes}
                 multiple={bMultiple}/>
        </div>
        </TooltipView>
    );
  }
}

export const view = OnboardingFileUploadButtonView;
export const events = oEvents;
