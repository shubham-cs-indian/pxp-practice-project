let SSOSettingConfigViewProps = (function () {


  var Props = function () {
    return {
      activeSSOSetting: {},
      ssoSettingsList: {},
      ssoSettingConfigurationList: {}
    }
  };

  var oProperties = new Props();
  return {

    reset: function () {
      oProperties = new Props();
    },

    getActiveSSOSetting: function () {
      return oProperties.activeSSOSetting;
    },

    setActiveSSOSetting: function (_oActiveSSOSetting) {
      oProperties.activeSSOSetting = _oActiveSSOSetting;
    },

    getSSOSettingsList: function () {
      return oProperties.ssoSettingsList;
    },

    setSSOSettingsList: function (_aSSOSettingConfigMap) {
      oProperties.ssoSettingsList = _aSSOSettingConfigMap;
    },

    getSSOSettingsConfigurationList: function () {
      return oProperties.ssoSettingConfigurationList;
    },

    setSSOSettingsConfigurationList: function (_aSSOSettingConfigMap) {
      oProperties.ssoSettingConfigurationList = _aSSOSettingConfigMap;
    },

  }
})();

export default SSOSettingConfigViewProps;