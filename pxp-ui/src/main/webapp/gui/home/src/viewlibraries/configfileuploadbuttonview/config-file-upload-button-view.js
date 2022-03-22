import CS from '../../libraries/cs/index';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import TooltipView from '../tooltipview/tooltip-view';
import assetTypes from '../tack/coverflow-asset-type-list';
var FileImportAllowedTypes = assetTypes.fileImportTypes.join(', ');

var oEvents = {
  CONFIG_FILE_UPLOAD_EVENT: "file_upload_event"
};

const oPropTypes = {
  label: ReactPropTypes.string,
  filesUploadHandler: ReactPropTypes.func,
  context: ReactPropTypes.string,
  className: ReactPropTypes.string,
  aAllowedFiles: ReactPropTypes.array,
  bMultiple:ReactPropTypes.bool,
};

// @CS.SafeComponent
class ConfigFileUploadButtonView extends React.Component{

  static propTypes = oPropTypes;

  constructor(props) {
    super(props);
    this.onboardingFileUpload = React.createRef();
  }

  componentDidUpdate = () => {
    this.onboardingFileUpload && (this.onboardingFileUpload.current.value = null);
  };

  handleFilesSelectedToUpload = (oEvent) => {
    var aFiles = oEvent.target.files;
    if (this.props.context == "iconLibrary") {
      CS.forEach(oEvent.target.files, function (oFile) {
        oFile.src = URL.createObjectURL(oFile);
      });
    }
    if(!CS.isFunction(this.props.filesUploadHandler)){
      EventBus.dispatch(oEvents.CONFIG_FILE_UPLOAD_EVENT, aFiles, this.props.context);
    }
    else {
      this.props.filesUploadHandler(aFiles)
    }
  };

  handleAssetBulkUploadClicked = () => {
    !CS.isEmpty(this.onboardingFileUpload.current) && this.onboardingFileUpload.current.click();
  };

  render() {

    var bMultiple = this.props.bMultiple? this.props.bMultiple: false;
    let {lightToolbarIcons: bLightToolbarIcons} = this.props;
    FileImportAllowedTypes = CS.isNotEmpty(this.props.aAllowedFiles) ? this.props.aAllowedFiles.join(', ') : FileImportAllowedTypes;
    let className = CS.isNotEmpty(this.props.className) ? this.props.className : "import";

    return (
        <TooltipView label={this.props.label}>
          <div className= "headerButton import"  onClick={this.handleAssetBulkUploadClicked}>
            <div className={`headerButtonIcon ${className} ${!!bLightToolbarIcons && "light"}`}>
              <input style={{"visibility": "hidden"}}
                     ref={this.onboardingFileUpload}
                     onChange={this.handleFilesSelectedToUpload}
                     type="file"
                     accept={FileImportAllowedTypes}
                     multiple={bMultiple}/>
            </div>
          </div>
        </TooltipView>
    );
  }

}

export const view = ConfigFileUploadButtonView;
export const events = oEvents;
