let ThemeConfigurationViewProps = (function () {

  let Props = function () {
    return {
      themeConfigurationData: {
        primaryLogoId: null,
        faviconId: null,
        title: "",
        headerBackgroundColor: "",
        headerFontColor: "",
        headerIconColor: "",
        dialogBackgroundColor: "",
        dialogFontColor: "",
        loginScreenBackgroundImageKey: null,
        loginScreenBackgroundThumbKey: null,
        loginScreenBackgroundColor: "",
        loginScreenButtonBackgroundColor: "",
        loginScreenFontColor: "",
        loginScreenTitle: "",
        isLandingPageExpanded: true,
        isProductInfoPageExpanded: false,
        loginScreenButtonFontColor: "",
        welcomeMessage: "",
        generalButtonFontColor: "",
        generalButtonBackgroundColor: "",
        generalFontColor: "",
        generalThemeColor: "",
        generalSelectionColor: "",
        generalHeaderLogo: "",
      },
    }
  };

  let oProperties = new Props();

  return {
    getThemeConfigurationData: function () {
      return oProperties.themeConfigurationData;
    },

    setThemeConfigurationData: function (_oThemeConfigurationData) {
      oProperties.themeConfigurationData = _oThemeConfigurationData;
    },

    reset: function () {
      oProperties = new Props();
    }
  }


})();

export default ThemeConfigurationViewProps;