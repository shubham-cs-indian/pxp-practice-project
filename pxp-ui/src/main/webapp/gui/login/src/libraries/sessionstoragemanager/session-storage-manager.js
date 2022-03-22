import CS from '../cs';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

var SessionStorageManager = (function(){

  return {
    getSessionStorage: function () {
      return window.sessionStorage;
    },

    setSessionStorage: function(oValue){
      ExceptionLogger.log("Warning: SessionStorage got reset! #LOCALSTORAGEMANAGER#");
      CS.forEach(oValue, function (value, sKey) {
        window.sessionStorage.setItem(sKey, value);
      });
    },

    setPropertyInSessionStorage: function(sProperty, oValue){
      window.sessionStorage.setItem(sProperty, oValue);
    },

    getPropertyFromSessionStorage: function(sProperty){
      return window.sessionStorage.getItem(sProperty);
    }
  }

})();

export default SessionStorageManager;