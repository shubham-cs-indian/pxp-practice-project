/*var LocalStorageManager = require('../localstoragemanager/local-storage-manager');
var $ = require('jquery');*/
import oBrandDetails from '../branddetails/brand-details';
import CS from "../cs";
import ThemeLoaderConfigurationModel from "./theme-loader-configuration-model";

var ThemeLoader = (function(){

  return {

    changeFavicon: function(sFavicon, sFromConfig){
      //var sPortal = sessionStorage.portal;
      // TODO: #Refact20
  /*    var sLocation = window.location.origin + window.location.pathname;
      var oDom = document.querySelector('[data-type="application-favicon"]');
      if(!sFavicon){
        sFavicon = "contentserv-favicon.ico";
      }
      if(sFromConfig){
        oDom.href = sLocation + sFavicon;
      }else{
        oDom.href = sLocation + "app/themes/basic/login/images/" + sFavicon;
      }*/

      // if(theme == "theme4" && oDom){
      //   oDom.href = sLocation + "app/themes/basic/homescreen/images/Edeka_Favicon.ico";
      //   if(sPortal == "onBoarding") {
      //      oDom.href = sLocation + "app/themes/basic/homescreen/images/Edeka_Favicon.ico";
      //     oDom.href = sLocation + "app/themes/basic/homescreen/images/fav2.PNG";
      //   }
      //    oDom.href = sLocation + "app/themes/basic/homescreen/images/fav1.PNG";
      // } else if(theme == "but"  && oDom) {
      //   oDom.href = sLocation + "app/themes/basic/homescreen/images/but_bookmark.gif";
      // } else if(theme == "frauenthal"  && oDom) {
      //   oDom.href = sLocation + "app/themes/basic/homescreen/images/frauenthal.gif";//Gif Not Available
      // } else if(theme == "xxxlutz" && oDom){
      //     oDom.href = sLocation + "app/themes/xxxlutz/homescreen/images/xxxlutz_favicon.ico";
      // } else if(theme == "metro" && oDom){
      //     oDom.href = sLocation + "app/themes/metro/homescreen/images/metro-favicon-32x32.png";
      // } else if(theme == "zegna" && oDom){ 
      //   oDom.href = sLocation + "app/themes/zegna/homescreen/images/zegna_favicon.ico"; 
      // } else if(theme == "auchan" && oDom){
      //     oDom.href = sLocation + "app/themes/auchan/homescreen/images/favicon.ico";
      // } else if(theme == "cs" && oDom){
      //     oDom.href = sLocation + "app/themes/cs/homescreen/images/favicon.ico";
      // } else if(theme == "valkyrie" && oDom){
      //     oDom.href = sLocation + "app/themes/valkyrie/homescreen/images/Valkyrie_favicon.png";
      // } else {
      //   oDom.href = sLocation + "app/themes/basic/homescreen/images/allianz_icon.ico";
      // }
    },

    changeTitle: function(sTitle){
      //var sPortal = sessionStorage.portal;

       var oDom = document.querySelector('[data-type="application-title"]');
           if(!sTitle){
             sTitle = "Contentserv";
           }
           oDom.innerHTML = sTitle;

      // if(theme == "theme4" && oDom){
      //   oDom.innerHTML = "EDEKA";
      //   /*if(sPortal == "onBoarding") {
      //     oDom.innerHTML = "OnBoarding";
      //   } else {
      //     oDom.innerHTML = "MDM";
      //   }*/
      // } else if(theme == "but" && oDom) {
      //   oDom.innerHTML = "BUT";
      // } else if(theme == "frauenthal" && oDom) {
      //   oDom.innerHTML = "FRAUENTHAL";
      // } else if(theme == "xxxlutz" && oDom){
      //   oDom.innerHTML = "XXXLutz";
      // } else if(theme == "metro" && oDom){
      //   oDom.innerHTML = "Metro";
      // } else if(theme == "zegna" && oDom){ 
      //   oDom.innerHTML = "ZEGNA"; 
      // } else if(theme == "auchan" && oDom){
      //   oDom.innerHTML = "AUCHAN";
      // } else if(theme == "cs" && oDom){
      //   oDom.innerHTML = "ContentSphere";
      // } else if(theme == "valkyrie" && oDom){
      //   oDom.innerHTML = "Valkyrie";
      // }/* else{
      //   oDom.innerHTML = "Allianz";
      // }*/
    },

    changeTheme: function (oLogoConfig) {
      var BrandDetails = oBrandDetails;
      //document.querySelector('body').className = theme;
      /*var sPortal = sessionStorage.portal;
      if(sPortal == "onBoarding") {
        document.querySelector('body').classList.add(sPortal);
      }*/
      //LocalStorageManager.setPropertyInLocalStorage('currentTheme', theme);
      if(!oLogoConfig || !oLogoConfig.faviconId){
        this.changeFavicon(BrandDetails.favicon);
      }
      this.changeTitle(oLogoConfig.logoTitle || BrandDetails.logoTitle);
    },

    setNewCSSStyleSheetRule: function (sSelector, oRule) {
      let oStyleSheets = document.styleSheets;
      let oStylesheet = oStyleSheets[(oStyleSheets.length - 1)];

     /** addRule is not supported by webkit browsers soo keep both**/
      if (oStylesheet.addRule) {
        oStylesheet.addRule(sSelector, oRule);
      } else if (oStylesheet.insertRule) {
        oStylesheet.insertRule(sSelector + ' { ' + oRule + ' }', oStylesheet.cssRules.length);
      }
    },

    changeThemeConfiguration: function (oThemeConfigurations) {
      let aThemeLoaderConfiguration = ThemeLoaderConfigurationModel(oThemeConfigurations);
      CS.forEach(aThemeLoaderConfiguration, (oThemeLoaderConfiguration) => {
        let sRules = oThemeLoaderConfiguration.rules;
        CS.forEach(oThemeLoaderConfiguration.klassesSelectors, (sKlasses) => {
          this.setNewCSSStyleSheetRule(sKlasses, sRules);
        })
      });
    },

    loadTheme: function (oLogoConfig) {
      this.changeTheme(oLogoConfig);
      /*var sTheme = this.getQueryVariable("theme");
      var sThemeInLocalStorage = LocalStorageManager.getPropertyFromLocalStorage('currentTheme');

      if(sTheme){
        sTheme = sTheme.toLowerCase();
        this.changeTheme(sTheme);
      } else if(sThemeInLocalStorage){
        this.changeTheme(sThemeInLocalStorage);
      } else {
        this.changeTheme("cs");
      }*/
    }
  }

})();

export default ThemeLoader;
