import {getTranslations as oTranslations} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ThemeConfigurationConstants from "./theme-configuration-constants";

var getSectionModel = (sKey, sValue, sContext) => {
  return {
    [sKey]: sValue,
    context: sContext,
  }
};

var ViewConfigurationScreenModel = function (oThemeConfigurationData) {
  this.sidePanelConfiguration = {
    headerLabel: oTranslations().SIDE_PANEL_CONFIGURATION,
    landingPage: getSectionModel('radio', oThemeConfigurationData.isLandingPageExpanded, ThemeConfigurationConstants.LANDING_PAGE),
    productInformationPage: getSectionModel('radio', oThemeConfigurationData.isProductInfoPageExpanded, ThemeConfigurationConstants.PRODUCT_INFORMATION_PAGE),
  };
};

export default ViewConfigurationScreenModel;