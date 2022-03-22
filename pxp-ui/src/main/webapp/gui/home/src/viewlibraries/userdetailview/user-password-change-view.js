import CS from '../../libraries/cs';
import React from 'react';
import alertify from '../../commonmodule/store/custom-alertify-store';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as CustomDialogView } from '../customdialogview/custom-dialog-view';

const oEvents = {
  CONTENT_USER_NEW_PASSWORD_SUBMIT: "content_user_new_password_submit",
  CONTENT_USER_NEW_PASSWORD_CANCEL: "content_user_new_password_cancel",
};

const oPropTypes = {

};
/**
 * @class UserPasswordChangeView - Use for change the password view.
 * @memberOf Views
 */

// @CS.SafeComponent
class UserPasswordChangeView extends React.Component{

  constructor (props) {
    super(props);

    this.passwordDialog =React.createRef();
    this.confirmPasswordDialog =  React.createRef();
  }

  handleChangePasswordCancelClicked =(oEvent)=> {
    this.passwordDialog.current.value = "";
    this.confirmPasswordDialog.current.value = "";
    EventBus.dispatch(oEvents.CONTENT_USER_NEW_PASSWORD_CANCEL, this);
  }

  handleChangePasswordSubmitClicked =(oEvent)=> {
    var sPassword = this.passwordDialog.current.value;
    var sConfirmPassword = this.confirmPasswordDialog.current.value;
    if (sPassword == sConfirmPassword) {
      this.passwordDialog.current.value = "";
      this.confirmPasswordDialog.current.value = "";
      EventBus.dispatch(oEvents.CONTENT_USER_NEW_PASSWORD_SUBMIT, this, sPassword);
    } else {
      alertify.error(getTranslation().ERROR_PASSWORD_AND_CONFIRM_PASSWORD);
    }
  }

  handleButtonClick =(sButtonId)=> {
    if(sButtonId === "save") {
      this.handleChangePasswordSubmitClicked();
    }
    else{
      this.handleChangePasswordCancelClicked();
    }
  }

  handlePasswordDialogOnClick =(oEvent)=> {
    oEvent.nativeEvent.context = "MyView";
  }

  render() {
    let fButtonHandler = this.handleButtonClick;
    let oContentStyle = {
      width: '500px'
    };

    let oBodyStyle = {
      padding: '12px'
    };

    let aButtonData=[
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: false,
      },
      {
        id: "save",
        label: getTranslation().SAVE,
        isFlat: false,
      }
    ];

    return (
        <CustomDialogView modal={true} open={true}
                          className="changePasswordDialog"
                          bodyStyle={oBodyStyle}
                          contentStyle={oContentStyle}
                          contentClassName=""
                          buttonData={aButtonData}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          buttonClickHandler={fButtonHandler}>
          <div className="changePasswordContainer" onClick={this.handlePasswordDialogOnClick}>
            <div className="changePasswordInputContainer">
              <div className="changePasswordInputText">{getTranslation().NEW_PASSWORD}</div>
              <input type="password" ref={this.passwordDialog} className="changePasswordInputField" autoComplete="new-password"/>
            </div>
            <div className="changePasswordInputContainer">
              <div className="changePasswordInputText">{getTranslation().CONFIRM_PASSWORD}</div>
              <input type="password" ref={this.confirmPasswordDialog} className="changePasswordInputField" autoComplete="off"/>
            </div>
          </div>
        </CustomDialogView>
    );
  }
}



UserPasswordChangeView.propTypes = oPropTypes;

export const view = UserPasswordChangeView;
export const events = oEvents;
