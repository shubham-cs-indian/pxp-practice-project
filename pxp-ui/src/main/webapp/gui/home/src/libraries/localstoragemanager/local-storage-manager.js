
var LocalStorageManager = (function(){

  return {
    getLocalStorage: function () {
      return window.localStorage;
    },

    setLocalStorage: function(oValue){
      console.log("Warning: LocalStorage got reset! #LOCALSTORAGEMANAGER#");
      window.localStorage = oValue;
    },

    setPropertyInLocalStorage: function(sProperty, oValue){
      window.localStorage[sProperty] = oValue;
    },

    getPropertyFromLocalStorage: function(sProperty){
      return window.localStorage[sProperty];
    }
  }

})();

export default LocalStorageManager;