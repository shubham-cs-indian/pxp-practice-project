import LocalStorageManager from './../localstoragemanager/local-storage-manager';

var WindowUnloadManager = (function () {

  var _confirmOnUnload = function(){
    window.onbeforeunload = function() {

      var bIsDisabled = LocalStorageManager.getPropertyFromLocalStorage('disableConfirmOnUnload');
      if(bIsDisabled == "true"){
        LocalStorageManager.setPropertyInLocalStorage('disableConfirmOnUnload', "false");
        return undefined;
      } else {
        return '';
      }
    }
  };

  return {

    init: function () {
      _confirmOnUnload();
    }
  }
})();

export const {init} = WindowUnloadManager;