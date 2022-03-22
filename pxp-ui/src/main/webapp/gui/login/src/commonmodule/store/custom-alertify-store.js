import CustomSnackbarView from '../../viewlibraries/customAlertifyView/custom-snackbar-view';
import React from 'react';
import ReactDOM from 'react-dom';


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

  return {

    error: function (sMessage) {
      const variant = "error";
      enqueueSnackbar(sMessage, {
        variant: variant,
        persist: true,
        action: (key) => <div className="messageCloseButton" onClick={() => closeSnackbar(key)}/>
      });
    },

    success: function (sMessage) {
      const variant = "success";
      enqueueSnackbar(sMessage, {
        variant: variant,
        autoHideDuration: 3000,
        action: (key) => <div className="messageCloseButton" onClick={() => closeSnackbar(key)}/>
      });
    },

    message: function (sMessage) {
      const variant = "info";
      enqueueSnackbar(sMessage, {
        variant: variant,
        autoHideDuration: 10000,
        action: (key) => <div className="messageCloseButton" onClick={() => closeSnackbar(key)}/>
      });
    },

    warning: function (sMessage) {
      const variant = "warning";
      enqueueSnackbar(sMessage, {
        variant: variant,
        autoHideDuration: 10000,
        action: (key) => <div className="messageCloseButton" onClick={() => closeSnackbar(key)}/>
      });
    }

  }

})();


export default customAlertify;

