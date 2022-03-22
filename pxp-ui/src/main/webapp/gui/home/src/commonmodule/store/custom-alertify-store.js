import CustomSnackbarView from '../../viewlibraries/customAlertifyView/custom-snackbar-view';
import React from 'react';
import ReactDOM from 'react-dom';
import CS from './../../libraries/cs'


var customAlertify = (function () {

  let enqueueSnackbar = null;
  let closeSnackbar = null;

  const showMessage = function (fFunction) {
    enqueueSnackbar = fFunction;
  };

  const hideMessage = function (fFunction) {
    closeSnackbar = fFunction;
  };

  const oAlertifyDOM = document.getElementById('alertifyContainer');
  ReactDOM.render(
    <CustomSnackbarView showMessage={showMessage} hideMessage={hideMessage}/>,
    oAlertifyDOM
  );

  const addLineBreakInAlertify = function(aMessages) {
    let aMessagesDom = [];
    CS.forEach(aMessages, function (oMessage) {
      if (CS.isNotEmpty(oMessage)) {
        aMessagesDom.push(<div>{oMessage}</div>)
      }
    });
    return(typeof aMessages == "string" ? aMessages :  aMessagesDom);
  };

  return {

    error: function (sMessage) {
      const variant = "error";
      let aMessagesDom = addLineBreakInAlertify(sMessage);
      enqueueSnackbar(<div className="alertifyError">{aMessagesDom}</div>, {
        variant: variant,
        persist: true,
        action: (key) => <div className="messageCloseButton" onClick={() => closeSnackbar(key)}/>
      });
    },

    success: function (sMessage) {
      const variant = "success";
      let aMessagesDom = addLineBreakInAlertify(sMessage);
      enqueueSnackbar(<div className="alertifySuccess">{aMessagesDom}</div>, {
        variant: variant,
        autoHideDuration: 3000,
        action: (key) => <div className="messageCloseButton" onClick={() => closeSnackbar(key)}/>
      });
    },

    message: function (sMessage) {
      const variant = "info";
      let aMessagesDom = addLineBreakInAlertify(sMessage);
      enqueueSnackbar(<div className="alertifyMessage">{aMessagesDom}</div>, {
        variant: variant,
        autoHideDuration: 10000,
        action: (key) => <div className="messageCloseButton" onClick={() => closeSnackbar(key)}/>
      });
    },

    warning: function (sMessage) {
      const variant = "warning";
      let aMessagesDom = addLineBreakInAlertify(sMessage);
      enqueueSnackbar(<div className="alertifyWarning">{aMessagesDom}</div>, {
        variant: variant,
        autoHideDuration: 10000,
        action: (key) => <div className="messageCloseButton" onClick={() => closeSnackbar(key)}/>
      });
    }

  }

})();


export default customAlertify;

