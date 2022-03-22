import React from "react";
import ReactPropTypes from "prop-types";
import CS from "../../../../../libraries/cs";
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as CustomDialogView} from "../../../../../viewlibraries/customdialogview/custom-dialog-view";
import {view as CustomTextFieldView} from "../../../../../viewlibraries/customtextfieldview/custom-text-field-view";

const oPropTypes = {
  activeIcons: ReactPropTypes.object
};

const oEvents = {
  HANDLE_DIALOG_SAVE_BUTTON_CLICKED: "handle_dialog_save_button_clicked",
  HANDLE_DIALOG_CANCEL_BUTTON_CLICKED: "handle_dialog_cancel_button_clicked",
  HANDLE_DIALOG_INPUT_CHANGED: "handle_dialog_input_changed",
  HANDLE_DIALOG_LIST_REMOVE_BUTTON_CLICKED: "handle_dialog_list_remove_button_clicked",
};

class IconLibraryEditCodeDialogView extends React.Component {

  _handleButtonClicked = (sButtonId) => {
    if (sButtonId == "cancel") {
      this._handleCancelAction();
    }
    else if (sButtonId == "ok") {
      this._handleSaveAction();
    }

  };

  _handleCancelAction = () => {
    EventBus.dispatch(oEvents.HANDLE_DIALOG_CANCEL_BUTTON_CLICKED);
  };

  _checkIsAnyInvalidIcon = () => {
    let aActiveIcons = this.props.activeIcons;
    let oFoundInvalidIcon = CS.find(aActiveIcons, {status: "invalidImage"});

    return CS.isNotEmpty(oFoundInvalidIcon);
  };

  _handleSaveAction = function () {
      EventBus.dispatch(oEvents.HANDLE_DIALOG_SAVE_BUTTON_CLICKED);
  };

  updateInputValue = (iId, sChangedField, sValue) => {
    let oFileData = {
      id: iId,
      value: sValue,
      changedField: sChangedField,
    };

    EventBus.dispatch(oEvents.HANDLE_DIALOG_INPUT_CHANGED, oFileData);
  };

  deleteActiveIcon = (iIconId) => {
    EventBus.dispatch(oEvents.HANDLE_DIALOG_LIST_REMOVE_BUTTON_CLICKED, iIconId);
  };

  getTextInputView = (sClassName, sValue, fBlurHandler, sHintText, bIsDisabled) => {

    return (<CustomTextFieldView
        className={sClassName}
        value={sValue}
        onBlur={fBlurHandler}
        hintText={sHintText}
        isDisabled={bIsDisabled}
        hideTooltip={true}
        isMultiLine={false}/>)
  };


  render () {
    let _this = this;
    let aIconListDom = [];
    let aActiveIcons = _this.props.activeIcons;

    CS.forEach(aActiveIcons, function (oIconElement) {
      let sIconCodeName = oIconElement.codeName;
      let sIconFileName = oIconElement.fileName;

      if (oIconElement.status === "validImage") {
        aIconListDom.push(<tr className="validImage">
          <td className={"iconImageContainer"}>
            <img className={"iconImage"} src={oIconElement.src}/>
          </td>
          <td className={"iconName"}>{_this.getTextInputView("sectionInput", sIconFileName,
              _this.updateInputValue.bind(_this, oIconElement.id, "fileName"), "", false)}</td>

          <td className={"sectionInputDataTag"}>{_this.getTextInputView("sectionInput", sIconCodeName,
              _this.updateInputValue.bind(_this, oIconElement.id, "codeName"),
              "Auto generated code", false)}</td>
          <td></td>

          <td>
            <div className="crossIconForIconLibrary" onClick={_this.deleteActiveIcon.bind(_this, oIconElement.id)}></div>
          </td>
        </tr>);
      } else {
        aIconListDom.push(<tr className="invalidImage">
          <td className={"iconImageContainer"}>
            <img className={"iconImage"} src={oIconElement.src}/>
          </td>
          <td className={"iconName"}>{_this.getTextInputView("sectionInput", sIconFileName,
              _this.updateInputValue.bind(_this, oIconElement.id, "fileName"),
              "", oIconElement.bIsDisabled)}</td>

          <td className={"sectionInputDataTag"}>{_this.getTextInputView("sectionInput", sIconCodeName,
              _this.updateInputValue.bind(_this, oIconElement.id, "codeName"),
              "", oIconElement.bIsDisabled)}</td>

          <td style={{color: 'Red'}}>{oIconElement.message}</td>
          <td>
            <div className="crossIconForIconLibrary" onClick={_this.deleteActiveIcon.bind(_this, oIconElement.id)}></div>
          </td>
        </tr>);
      }
    });

    let bIsDisabledSaveButton = this._checkIsAnyInvalidIcon();

    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true,
      },
      {
        id: "ok",
        label: getTranslation().SAVE,
        isFlat: false,
        isNotActive: bIsDisabledSaveButton
      }];

    let sHeader = 'Upload Icons';
    let fButtonHandler = _this._handleButtonClicked;
    return (
        <CustomDialogView modal={true} open={true}
                          title={sHeader}
                          bodyClassName=""
                          contentClassName="tabDetailsDialog"
                          buttonData={aButtonData}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          buttonClickHandler={fButtonHandler}>
          <table className={"iconTable"}>
            <tr className={"iconTableHeader"}>
              <th className={"iconImageContainer"}></th>
              <th className={"tableHeaderName1"}><h5>Icon Name</h5></th>
              <th className={"tableHeaderName2"}><h5>Code Name</h5></th>
              <th className={"tableHeaderName1"}></th>
            </tr>
            <tbody>
            {aIconListDom}
            </tbody>
          </table>
        </CustomDialogView>

    );

  }
}

export const view = IconLibraryEditCodeDialogView;
export const events = oEvents;
