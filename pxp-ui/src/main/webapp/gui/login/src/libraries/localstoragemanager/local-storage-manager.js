
var LocalStorageManager = (function(){

  return {
    setPropertyInLocalStorage: function(sProperty, oValue){
      window.localStorage[sProperty] = oValue;
    },

    getPropertyFromLocalStorage: function(sProperty){
      return window.localStorage[sProperty];
    }
  }

})();

export default LocalStorageManager;
