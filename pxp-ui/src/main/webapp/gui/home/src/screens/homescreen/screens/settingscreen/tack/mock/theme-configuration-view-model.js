import {getTranslations as oTranslations} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ThemeConfigurationConstants from "./theme-configuration-constants";

var getSectionModel = (sKey, sValue, sContext) => {
  return {
    [sKey]: sValue,
    context: sContext,
  }
};

var ThemeConfigurationScreenModel = function (oThemeConfigurationData) {
  this.logoConfiguration = {
    headerLabel: oTranslations().BROWSER_CONFIGURATION,
    logoTitle: oThemeConfigurationData.logoTitle || "",
    faviconId: getSectionModel('icon', oThemeConfigurationData.faviconId, ThemeConfigurationConstants.FAVICON_ID),
  };
  this.loginScreenConfiguration = {
    headerLabel: oTranslations().LOGIN_SCREEN_CONFIGURATION,
    primaryLogoId: getSectionModel('icon', oThemeConfigurationData.primaryLogoId, ThemeConfigurationConstants.PRIMARY_LOGO_ID),
    loginScreenBackgroundThumbKey: getSectionModel('icon', oThemeConfigurationData.loginScreenBackgroundThumbKey, ThemeConfigurationConstants.LOGIN_SCREEN_BACKGROUND_THUMB_KEY),
    loginScreenButtonBackgroundColor: getSectionModel('color', oThemeConfigurationData.loginScreenButtonBackgroundColor, ThemeConfigurationConstants.LOGIN_SCREEN_BUTTON_BACKGROUND_COLOR),
    loginScreenButtonFontColor: getSectionModel('color', oThemeConfigurationData.loginScreenButtonFontColor, ThemeConfigurationConstants.LOGIN_SCREEN_BUTTON_FONT_COLOR),
    welcomeMessage: oThemeConfigurationData.welcomeMessage || "",
  };
  this.generalConfiguration = {
    headerLabel: oTranslations().GENERAL_CONFIGURATION,
    generalThemeColor: getSectionModel('color', oThemeConfigurationData.generalThemeColor, ThemeConfigurationConstants.GENERAL_THEME_COLOR),
    generalFontColor: getSectionModel('color', oThemeConfigurationData.generalFontColor, ThemeConfigurationConstants.GENERAL_FONT_COLOR),
    generalHeaderLogo:getSectionModel('icon', oThemeConfigurationData.generalHeaderLogo, ThemeConfigurationConstants.GENERAL_HEADER_LOGO),
    generalSelectionColor:getSectionModel('color', oThemeConfigurationData.generalSelectionColor, ThemeConfigurationConstants.GENERAL_SELECTION_COLOR),
    generalButtonBackgroundColor:getSectionModel('color', oThemeConfigurationData.generalButtonBackgroundColor, ThemeConfigurationConstants.GENERAL_BUTTON_BACKGROUND_COLOR),
    generalButtonFontColor: getSectionModel('color', oThemeConfigurationData.generalButtonFontColor, ThemeConfigurationConstants.GENERAL_BUTTON_FONT_COLOR),
  };
};

export default ThemeConfigurationScreenModel;