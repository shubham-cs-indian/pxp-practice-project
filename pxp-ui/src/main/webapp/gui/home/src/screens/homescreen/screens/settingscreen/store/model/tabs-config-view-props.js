let TabsConfigViewProps = (function () {
  var Props = function () {
    return {
      tabList: [],
      activeTab: {},
      isTabDetailsDialogActive: false,
      referencedProperties: {},
      isSortTabsDialogActive: false
    }
  };
  var oProperties = new Props();
  return {

    reset: function () {
      oProperties = new Props();
    },

    getTabList: function () {
      return oProperties.tabList;
    },

    setTabList: function (_aTabList) {
      oProperties.tabList = _aTabList;
    },

    getActiveTab: function () {
      return oProperties.activeTab;
    },

    setActiveTab: function (_oActiveTab) {
      oProperties.activeTab = _oActiveTab;
    },

    setReferencedProperties: function (_oReferencedProperties) {
      oProperties.referencedProperties = _oReferencedProperties;
    },

    getReferencedProperties: function () {
      return oProperties.referencedProperties;
    },

    getSortTabsDialogActive: function (){
      return oProperties.isSortTabsDialogActive;
    },

    setSortTabsDialogActive: function (_oIsSortTabsDialogActive) {
      oProperties.isSortTabsDialogActive = _oIsSortTabsDialogActive;
    }

  }

})();

export default TabsConfigViewProps;