import ReactPropTypes from "prop-types";
import React from "react";
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";
import {view as CusromMaterialButtonView} from "../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view";
import {getTranslations as getTranslation} from "../../../../../commonmodule/store/helper/translation-manager";

const oEvents = {
  DAM_CONFIG_USE_ORIGINAL_FILE_NAME_VALUE_CHANGED: "dam_config_use_original_file_name_value_changed",
  DAM_CONFIGURATION_SAVE_DISCARD_BUTTON_CLICKED: "dam_configuration_save_discard_button_clicked",
};
let oPropTypes = {
  damConfigurationData: ReactPropTypes.object,
};

// @CS.SafeComponent
class DAMConfigurationView extends React.Component {
  constructor (props) {
    super(props);
  }

  static propTypes = oPropTypes;
  handleToggleButtonValueChanged = () => {
    EventBus.dispatch(oEvents.DAM_CONFIG_USE_ORIGINAL_FILE_NAME_VALUE_CHANGED);
  };
  handleClassSaveDialogClose = (sButtonId) => {
    EventBus.dispatch(oEvents.DAM_CONFIGURATION_SAVE_DISCARD_BUTTON_CLICKED, sButtonId);
  };

  getSaveBarButtons = () => {
    let oButtonLabelStyle = {
      fontSize: 12,
    };
    return (
        <div className={"damConfigurationSaveBarButtonsWrapper"}>
          <CusromMaterialButtonView
              isRaisedButton={true}
              label={getTranslation().DISCARD}
              onButtonClick={this.handleClassSaveDialogClose.bind(this, "discard")}
          />
          <CusromMaterialButtonView
              isRaisedButton={true}
              label={getTranslation().SAVE}
              labelStyle={oButtonLabelStyle}
              keyboardFocused={true}
              onButtonClick={this.handleClassSaveDialogClose.bind(this, "save")}
          />
        </div>
    );
  };
  getSaveCancelView = (oDamConfigurationData) => {
    let sSaveBarClass = "damConfigurationSaveBar";
    let bIsDirty = oDamConfigurationData.isDirty;
    if (bIsDirty) {
      sSaveBarClass += " dirty";
    }
    return (
        <div className={sSaveBarClass}>{this.getSaveBarButtons()}</div>
    );
  };

  render () {
    let fHandleValueChanged = this.handleToggleButtonValueChanged;
    let sStringToDisplay = getTranslation().USE_ORIGINAL_FILE_NAME;
    let oDamConfigurationData = this.props.damConfigurationData;
    let bIsOriginalFileNameUsedForDownload = oDamConfigurationData.isOriginalFileNameUsedForDownload;
    let sButtonClassForUseOriginalFileName = "damConfigurationYesNoSwitchButton ";
    if (bIsOriginalFileNameUsedForDownload) {
      sButtonClassForUseOriginalFileName += "yes";
    } else {
      sButtonClassForUseOriginalFileName += "no";
    }

    return (
        <div className="damConfigurationContainer">
          <div className="damConfigurationHeader"></div>
          <div className="damConfigurationBodyView">
            <div className="damConfigurationViewOuterWrapper">
              <div className="damConfigurationWrapper">
                <div className="damConfigYesNoLabel">{sStringToDisplay}</div>
                <div className="damConfigurationYesNoView">
                  <div className="YesNoSwitch" onClick={fHandleValueChanged}>
                    <div className={sButtonClassForUseOriginalFileName}>
                      <div className="textSection yes">I</div>
                      <div className="circleSection"></div>
                      <div className="textSection no ">O</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          {this.getSaveCancelView(oDamConfigurationData)}
        </div>
    );
  };
};
DAMConfigurationView.propTypes = oPropTypes;
export const view = DAMConfigurationView;
export const events = oEvents;
