import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';

const oPropTypes = {
  title: ReactPropTypes.string,
  uploadHandler: ReactPropTypes.func,
  cancelHandler: ReactPropTypes.func,
  acceptedFileTypes: ReactPropTypes.array
};
const oEvents = {};

// @CS.SafeComponent
class BPNMWrapperView extends React.Component {
  constructor (oProps) {
    super(oProps);

    this.uploadHandle = React.createRef();
  }
  static propTypes = oPropTypes;

  handleButtonClicked = (sButtonId) => {
    if (sButtonId == "upload") {
      this.uploadHandle.current && this.uploadHandle.current.click();
    }
    else {
      this.props.cancelHandler();
    }
  };

  handleUploadButtonClicked = (oEvent) => {
    this.props.uploadHandler(oEvent.target.files);
  };

  render () {
    let __props = this.props;
    let aButtonData = [
      {id: "upload", label: getTranslation().UPLOAD},
      {id: "cancel", label: getTranslation().CANCEL}
    ];
    let fButtonHandler = this.handleButtonClicked;
    let aAcceptedFileTypes = __props.acceptedFileTypes || [];
    return (<div className={"configUploadDialogView"}>
      <CustomDialogView buttonData={aButtonData}
                        onRequestClose={fButtonHandler.bind(this, aButtonData[1].id)}
                        buttonClickHandler={fButtonHandler}
                        modal={true}
                        open={true}
                        title={__props.title}/>
      <input style={{"visibility": "hidden"}}
             ref={this.uploadHandle}
             onChange={this.handleUploadButtonClicked}
             type="file"
             accept={aAcceptedFileTypes.join(",")}
      />
    </div>)
  }
}


export const view = BPNMWrapperView;
export const events = oEvents;
