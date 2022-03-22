/**
 * Created by DEV on 07-01-2016.
 */
import CS from '../../../libraries/cs';

import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import LoginStore from './../store/login-screen-store';
import { events as LoginScreenFormEvents } from './../view/login-screen-form-view.jsx';

var LoginScreenAction = (function () {

  var oEventHandler = {};

  var handleUserNameChanged = function (oContext, sUserName) {
    LoginStore.handleUserNameChanged(sUserName);
  };

  var handlePasswordChanged = function (oContext, sPassword) {
    LoginStore.handlePasswordChanged(sPassword);
  };

  var handleLoginButtonClicked = function () {
    LoginStore.handleLoginButtonClicked();
  };

  let handleNextButtonClicked = function () {
    LoginStore.handleNextButtonClicked();
  };

  let handleEnableInputField = function () {
    LoginStore.handleEnableInputField();
  };

  (function () {
    oEventHandler[LoginScreenFormEvents.USERNAME_CHANGED] = handleUserNameChanged;
    oEventHandler[LoginScreenFormEvents.PASSWORD_CHANGED] = handlePasswordChanged;
    oEventHandler[LoginScreenFormEvents.LOGIN_BUTTON_CLICKED] = handleLoginButtonClicked;
    oEventHandler[LoginScreenFormEvents.NEXT_BUTTON_CLICKED] = handleNextButtonClicked;
    oEventHandler[LoginScreenFormEvents.EDIT_BUTTON_CLICKED] = handleEnableInputField;
  })();

  return  {
    //Register Event Listener
    registerEvent: function () {

      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        EventBus.addEventListener(sEventName, oHandler);
      });
    },

    //De-Register Event Listener
    deRegisterEvent: function () {

      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        EventBus.removeEventListener(sEventName, oHandler);
      });
    }
  }
})();

export default LoginScreenAction;