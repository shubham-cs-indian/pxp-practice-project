import React from 'react';
import alertify from '../../commonmodule/store/custom-alertify-store';

var CommonUtils = (function () {


  let _showMessage = function (sMessage) {
    alertify.message(sMessage);
  };

  return {

    showMessage: function (sMessage) {
      _showMessage(sMessage);
    },

    isFirefox: function () {
      return typeof InstallTrigger !== 'undefined';
    },

  }
})();

export default CommonUtils;
