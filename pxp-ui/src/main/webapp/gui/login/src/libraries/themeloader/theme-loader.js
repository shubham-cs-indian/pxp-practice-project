import oBrandDetails from '../branddetails/brand-details';

var ThemeLoader = (function(){

  return {

    changeFavicon: function(sFavicon, sFromConfig){
      var sLocation = window.location.origin + window.location.pathname;
      var oDom = document.querySelector('[data-type="application-favicon"]');
      if(!sFavicon){
        sFavicon = "contentserv-favicon.ico";
      }
      if(sFromConfig){
        oDom.href = sLocation + sFavicon;
      }else{
        oDom.href = sLocation + "home/src/themes/basic/login/images/" + sFavicon;
      }
    },

    changeTitle: function(sTitle){
       var oDom = document.querySelector('[data-type="application-title"]');
           if(!sTitle){
             sTitle = "Contentserv";
           }
           oDom.innerHTML = sTitle;

    },

    changeTheme: function (oLogoConfig) {
      var BrandDetails = oBrandDetails;
      if(!oLogoConfig || !oLogoConfig.faviconId){
        this.changeFavicon(BrandDetails.favicon);
      }
      this.changeTitle(oLogoConfig.logoTitle || BrandDetails.logoTitle);
    },

    setNewCSSStyleSheetRule: function (sSelector, oRule) {
      let oStyleSheets = document.styleSheets;
      let oStylesheet = oStyleSheets[(oStyleSheets.length - 1)];

      if (oStylesheet.addRule) {
        oStylesheet.addRule(sSelector, oRule);
      } else if (oStylesheet.insertRule) {
        oStylesheet.insertRule(sSelector + ' { ' + oRule + ' }', oStylesheet.cssRules.length);
      }
    },

    loadTheme: function (oLogoConfig) {
      this.changeTheme(oLogoConfig);
    }
  }

})();

export default ThemeLoader;
