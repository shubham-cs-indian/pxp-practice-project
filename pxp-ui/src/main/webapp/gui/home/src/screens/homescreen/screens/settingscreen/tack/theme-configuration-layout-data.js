import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import ThemeConfigurationConstants from "../tack/mock/theme-configuration-constants";

const themeConfigurationLayoutData = function () {
  return {
    logoConfiguration: [{
      id: "1",
      label: getTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "1",
          label: getTranslations().LOGO_TITLE,
          key: ThemeConfigurationConstants.LOGO_TITLE,
          type: "singleText",
          width: 50
        },
        {
          id: "2",
          label: getTranslations().FAVICON,
          key: ThemeConfigurationConstants.FAVICON_ID,
          type: "image",
          width: 50
        }

      ]
    }],
    loginScreenConfiguration: [{
      id: "2",
      label: getTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "1",
          label: getTranslations().WELCOME_MESSAGE,
          key: ThemeConfigurationConstants.WELCOME_MESSAGE,
          type: "singleText",
          width: 50
        },
               {
          id: "2",
          label: getTranslations().LOGIN_SCREEN_BACKGROUND_IMAGE,
          key: ThemeConfigurationConstants.LOGIN_SCREEN_BACKGROUND_THUMB_KEY,
          type: "image",
          width: 50
        },
        {
          id: "3",
          label: getTranslations().LOGIN_SCREEN_LOGO,
          key: ThemeConfigurationConstants.PRIMARY_LOGO_ID,
          type: "image",
          width: 33
        },
        {
          id: "4",
          label: getTranslations().LOGIN_SCREEN_BUTTON_BACKGROUND_COLOR,
          key: ThemeConfigurationConstants.LOGIN_SCREEN_BUTTON_BACKGROUND_COLOR,
          type: "colorPicker",
          width: 33
        },
        {
          id: "5",
          label: getTranslations().LOGIN_SCREEN_BUTTON_FONT_COLOR,
          key: ThemeConfigurationConstants.LOGIN_SCREEN_BUTTON_FONT_COLOR,
          type: "colorPicker",
          width: 33
        },
      ]
    }],
    generalConfiguration: [{
      id: "3",
      label: getTranslations().BASIC_INFORMATION,
      elements: [
        {
          id: "1",
          label:getTranslations().GENERAL_THEME_COLOR,
          key: ThemeConfigurationConstants.GENERAL_THEME_COLOR,
          type: "colorPicker",
          width: 50
        },
        {
          id: "2",
          label: getTranslations().GENERAL_FONT_COLOR,
          key: ThemeConfigurationConstants.GENERAL_FONT_COLOR,
          type: "colorPicker",
          width: 50
        },
        {
          id: "3",
          label:getTranslations().GENERAL_HEADER_LOGO,
          key: ThemeConfigurationConstants.GENERAL_HEADER_LOGO,
          type: "image",
          width: 50
        },
        {
          id: "4",
          label:getTranslations().GENERAL_SELECTION_COLOR,
          key: ThemeConfigurationConstants.GENERAL_SELECTION_COLOR,
          type: "colorPicker",
          width: 50
        },
        {
          id: "5",
          label:  getTranslations().GENERAL_BUTTON_BACKGROUND_COLOR,
          key: ThemeConfigurationConstants.GENERAL_BUTTON_BACKGROUND_COLOR,
          type: "colorPicker",
          width: 50
        },
        {
          id: "6",
          label:  getTranslations().GENERAL_BUTTON_FONT_COLOR,
          key: ThemeConfigurationConstants.GENERAL_BUTTON_FONT_COLOR,
          type: "colorPicker",
          width: 50
        },
      ]
    }],
    sidePanelConfiguration: [{
      id: "5",
      label: "Side Panel Configuration",
      elements: [
        {
          id: "1",
          label: getTranslations().LANDING_PAGE,
          key: ThemeConfigurationConstants.LANDING_PAGE,
          type: "radioButton",
          width: 50,
          elements: [
            {
              key: ThemeConfigurationConstants.EXPAND,
              label: getTranslations().EXPAND
            },
            {
              key: ThemeConfigurationConstants.COLLAPSE,
              label: getTranslations().COLLAPSE
            }
          ]
        },
        {
          id: "2",
          label: getTranslations().PRODUCT_INFORMATION_PAGE,
          key: ThemeConfigurationConstants.PRODUCT_INFORMATION_PAGE,
          type: "radioButton",
          width: 50,
          elements: [
            {
              key: ThemeConfigurationConstants.EXPAND,
              label: getTranslations().EXPAND
            },
            {
              key: ThemeConfigurationConstants.COLLAPSE,
              label: getTranslations().COLLAPSE
            }
          ]
        }
      ]
    }],
  };
};

export default themeConfigurationLayoutData;