import React from "react";
import ReactPropTypes from "prop-types";
import CS from "../../../../../libraries/cs";
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";
import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as CustomDialogView} from "../../../../../viewlibraries/customdialogview/custom-dialog-view";
import ViewLibraryUtils from "../../../../../viewlibraries/utils/view-library-utils";
import TooltipView from "../../../../../viewlibraries/tooltipview/tooltip-view";
import assetTypes from '../../../../../viewlibraries/tack/coverflow-asset-type-list';

const oPropTypes = {
  oActiveIcon: ReactPropTypes.object,
  oOriginalIcon: ReactPropTypes.object
};

const oEvents = {
  HANDLE_ICON_ELEMENT_SAVE_BUTTON_CLICKED: "handle_icon_element_save_button_clicked",
  HANDLE_ICON_ELEMENT_CANCEL_BUTTON_CLICKED: "handle_icon_element_cancel_button_clicked",
  HANDLE_ICON_ELEMENT_REPLACE_BUTTON_CLICKED: "handle_icon_element_replace_button_clicked",
  HANDLE_ICON_ELEMENT_ICON_NAME_CHANGED: "handle_icon_element_icon_name_changed",
};

class IconEditView extends React.Component {

  constructor (props) {
    super(props);

    this.editIconFileUpload = React.createRef();
    this.state = {
      iconName: "",
      codeName: ""
    }
  }

  componentDidMount () {
    this.setState({
      iconName: this.props.oActiveIcon.label,
      codeName: this.props.oActiveIcon.code
    });
  };

  handleIconReplaceClicked = () => {
    !CS.isEmpty(this.editIconFileUpload.current) && this.editIconFileUpload.current.click();
  };

  handleFileToReplace = (event) => {
    let aFiles = event.target.files;
    EventBus.dispatch(oEvents.HANDLE_ICON_ELEMENT_REPLACE_BUTTON_CLICKED, aFiles[0], this.state.iconName);
  };

  _handleButtonClicked = (sButtonId) => {
    if (sButtonId == "cancel" || sButtonId == "ok") {
      this._handleCancelAction(sButtonId);
    }
    else if (sButtonId == "save") {
      this._handleSaveAction();
    }
  };

  _handleCancelAction = (sButtonId) => {
    this.setState({
      iconName: this.props.oOriginalIcon.label
    });
    EventBus.dispatch(oEvents.HANDLE_ICON_ELEMENT_CANCEL_BUTTON_CLICKED, sButtonId);
  };

  _handleSaveAction = function () {
    let iconName = this.state.iconName;
    let codeName = this.state.codeName;
    let oActiveIcon = this.props.oActiveIcon;
    let bNameEmpty = !/\S/.test(iconName);
    EventBus.dispatch(oEvents.HANDLE_ICON_ELEMENT_SAVE_BUTTON_CLICKED, oActiveIcon, codeName, iconName, bNameEmpty)

  };

  updateIconName = (event) => {

    this.setState({
      iconName: event.target.value
    });

    EventBus.dispatch(oEvents.HANDLE_ICON_ELEMENT_ICON_NAME_CHANGED, event.target.value);
  };

  onIconNameChange (value) {
    this.setState({
      iconName: value
    });
  };

  render () {
    let oActiveIcon = this.props.oActiveIcon;
    let sIconName = this.state.iconName;
    let oOriginalIcon = this.props.oOriginalIcon;
    let aButtonData = [];

    if (sIconName !== oOriginalIcon.label || CS.isNotEmpty(oActiveIcon.src)) {
      aButtonData = [
        {
          id: "cancel",
          label: getTranslations().DISCARD,
          isFlat: true,
        },
        {
          id: "save",
          label: getTranslations().SAVE,
          isFlat: false,
        }];

    } else {
      aButtonData = [
        {
          id: "ok",
          label: getTranslations().OK,
          isFlat: false,
        }];
    }

    let oStyle = {
      width: '606px',
      height: '295px',
    };


    let sFileImportAllowedTypes = assetTypes.imageTypes.join(', ');
    let sIconUrl = CS.isNotEmpty(oActiveIcon.src) ? oActiveIcon.src : ViewLibraryUtils.getIconUrl(oActiveIcon.iconKey, false);

    let sHeader = getTranslations().EDIT_ICON;
    return (
        <CustomDialogView modal={true} open={true}
                          title={sHeader}
                          bodyClassName=""
                          contentStyle={oStyle}
                          buttonData={aButtonData}
                          buttonClickHandler={this._handleButtonClicked}
                          onRequestClose={this._handleCancelAction.bind(this, "ok")}>
          <div className="iconImageInformation">
            <img src={sIconUrl}/>
            <div className="editIconDialogContainer">
              <TooltipView placement={"top"} label={getTranslations().REPLACE_ICON}>
                <div className="editIconDialogActionButton replace" onClick={this.handleIconReplaceClicked}>
                  <input style={{"visibility": "hidden"}}
                         ref={this.editIconFileUpload}
                         onChange={this.handleFileToReplace}
                         type="file"
                         accept={sFileImportAllowedTypes}
                         multiple={false}/>
                </div>
              </TooltipView>
            </div>
          </div>
          <div className="iconInformation">
            <h4>{getTranslations().ICON_NAME}</h4>
            <input type="text" className="iconName" value={this.state.iconName} placeholder={oActiveIcon.code}
                   onChange={e => this.onIconNameChange(e.target.value)}
                   onBlur={this.updateIconName}/>
            <h4>{getTranslations().CODE_NAME}</h4>
            <input type="text" className="codeName" value={oActiveIcon.code} disabled={true}/>
          </div>
        </CustomDialogView>
    );
  }
}


export const view = IconEditView;
export const events = oEvents;
