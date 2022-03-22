import React from 'react';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher';

var Events = {
  REFRESH_CURRENT_USER_DETAILS: "refresh_current_user_details",
  HANDLE_PHYSICAL_CATALOG_MENU_BUTTON_VISIBILITY: "handle_physical_catalog_menu_button_visibility",
  HANDLE_PHYSICAL_CATALOG_DISABILITY: "handle_physical_catalog_disability",
  CONTENT_DATA_LANGUAGE_CHANGED: "content_data_language_changed",
  HANDLE_LANGUAGE_INFO_CHANGED: "handle_language_info_changed",
  HANDLE_DATA_LANGUAGE_CHANGED: "handle_data_language_changed",
  HANDLE_BROWSER_BUTTON_CLICKED: "handle_browser_button_clicked",
  TRIGGER_HOME_SCREEN: "trigger_home_screen",
  HANDLE_RESET_BREADCRUMB_NODE_PROPS_ACCORDING_TO_CONTEXT: "handle_reset_breadcrumb_node_props_according_to_context",
  HANDLE_UI_LANGUAGE_CHANGED_FROM_CONFIG: "handle_ui_language_changed_from_config",
  HANDLE_NOTIFICATION_BUTTON_CLICKED: "handle_notification_button_clicked",
  HIDE_DATA_LANGUAGE_OPTIONS_ON_HEADER: "hide_data_language_options_on_header",
  SHOW_DATA_LANGUAGE_OPTIONS_ON_HEADER: "show_data_language_options_on_header",
  HIDE_UI_LANGUAGE_OPTIONS_ON_HEADER: "hide_UI_language_options_on_header",
  SHOW_UI_LANGUAGE_OPTIONS_ON_HEADER: "show_UI_language_options_on_header",
  THEME_CONFIGURATION_UPDATED: "theme_configuration_updated",
  HANDLE_DASHBOARD_TAB_SELECTED: "handle_dashboard_tab_selected",
  ARCHIVE_BUTTON_CLICKED: "archive_button_clicked",
  HEADER_MENU_VIEW_REFRESH_NOTIFICATION : "header_menu_view_refresh_notification",
  HANDLE_SIDE_PANEL_TOGGLE: "handle_side_panel_toggle"
};

/**
 * @class HomeScreenCommunicator - Used to communicate with other controllers.
 * @memberOf Communicators
 */

var HomeScreenCommunicator = (function () {

  let bContentScreenLoaded = false;

  let bSettingScreenLoaded = false;

  return {

    /**
     * @function disablePhysicalCatalog
     * @description Disable or Enable the PhysicalCatalog which is present on header bar.
     * @memberOf Communicators.HomeScreenCommunicator
     * @param {boolean} bIsPhysicalCatalogDisabled - If true physical catalog gets disabled and vice versa.
     * @example
     * disablePhysicalCatalog(true)
     */
    disablePhysicalCatalog: function (bIsPhysicalCatalogDisabled) {
      EventBus.dispatch(Events.HANDLE_PHYSICAL_CATALOG_DISABILITY, bIsPhysicalCatalogDisabled);
    },

    setSelectedDashboardTabId: function (sTabId) {
      EventBus.dispatch(Events.HANDLE_DASHBOARD_TAB_SELECTED, sTabId);
    },

    /**
     * @function handleBrowserBackOrForwardButtonClicked
     * @description Dispatch Event on clicking Browser back or forward button.
     * @memberOf Communicators.HomeScreenCommunicator
     * @param {Object} oItem - BreadCrumb item.
     * @example
     * handleDataLanguageChanged({{"isGetContent":true,"entityType":"com.cs.runtime.interactor.entity.ArticleInstance","breadCrumbData":{"extraData":{"URL":"runtime/klassinstances/<%=tab%>/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>","requestData":{"id":"726d80f2-a286-47e6-9ad7-80335235374e","tab":"overviewtab","isLoadMore":"false","getAll":"true"},"postData":{"attributes":[],"tags":[],"allSearch":"","size":20,"from":0,"sortField":"createdOn","sortOrder":"desc","getFolders":true,"getLeaves":true,"isAttribute":false,"isNumeric":false,"selectedRoles":[],"selectedTypes":[],"isRed":false,"isOrange":false,"isYellow":false,"isGreen":true,"templateId":null,"typeId":null,"tabId":null,"tabType":"com.cs.config.interactor.entity.template.CustomTemplateTab","childContextId":null,"selectedTimeRange":{"endTime":null,"startTime":null}}},"id":"726d80f2-a286-47e6-9ad7-80335235374e","label":"A_IND 10","type":"com.cs.runtime.interactor.entity.ArticleInstance","helpScreenId":"content"}}})
     */
    handleBrowserBackOrForwardButtonClicked: function (oItem) {
      EventBus.dispatch(Events.HANDLE_BROWSER_BUTTON_CLICKED, oItem);
    },

    handleResetBreadcrumbNodePropsAccordingToContext: function (sContext, sContentId, oPostData) {
      EventBus.dispatch(Events.HANDLE_RESET_BREADCRUMB_NODE_PROPS_ACCORDING_TO_CONTEXT, sContext, sContentId, oPostData);
    },

    /**
     * @function handleLanguageInfoChanged
     * @description Dispatch Event on changing Language Info.
     * @memberOf Communicators.HomeScreenCommunicator
     * @param {string} sDataLanguageCode - Data language code
     * @param {Object} oCallbackData - Callback data.
     * @example
     * handleDataLanguageChanged('English', {})
     */
    handleLanguageInfoChanged: function (oLanguage) {
      EventBus.dispatch(Events.HANDLE_LANGUAGE_INFO_CHANGED, oLanguage);
    },

    /**
     * @function handleContentDataLanguageChanged
     * @description Dispatch Event on changing Content Data Language.
     * @memberOf Communicators.HomeScreenCommunicator
     * @param {Object} oCallbackData - Callback data.
     * @example
     * handleContentDataLanguageChanged({})
     */
    handleContentDataLanguageChanged: function (oCallbackData) {
      EventBus.dispatch(Events.CONTENT_DATA_LANGUAGE_CHANGED, oCallbackData);
    },

    /**
     * @function handleDataLanguageChanged
     * @description Dispatch Event on changing Data Language.
     * @memberOf Communicators.HomeScreenCommunicator
     * @param {string} sDataLanguageCode - Data language code.
     * @param {Object} oCallbackData - Callback data.
     * @example
     * handleDataLanguageChanged('English', {})
     */
    handleDataLanguageChanged: function (sDataLanguageCode, oCallbackData) {
      EventBus.dispatch(Events.HANDLE_DATA_LANGUAGE_CHANGED, sDataLanguageCode, oCallbackData);
    },

    /**
     * @function handleUILanguageChangedFromConfig
     * @description Dispatch event on changing UI language from Config Screen.
     * @memberOf Communicators.HomeScreenCommunicator
     * @param {object} oCallbackData - Callback data.
     */
    handleUILanguageChangedFromConfig: function (oCallbackData) {
      EventBus.dispatch(Events.HANDLE_UI_LANGUAGE_CHANGED_FROM_CONFIG, oCallbackData);
    },

    /**
     * @function triggerHomeScreen
     * @description Dispatch event for changing trigger of Home Screen.
     * @memberOf Communicators.HomeScreenCommunicator
     */
    triggerHomeScreen: function () {
      EventBus.dispatch(Events.TRIGGER_HOME_SCREEN);
    },

    /**
     * @function handleNotificationButtonClicked
     * @description Dispatch event after clicking on bell icon on Header Menu Bar.
     * @memberOf Communicators.HomeScreenCommunicator
     */
    handleNotificationButtonClicked: function () {
      EventBus.dispatch(Events.HANDLE_NOTIFICATION_BUTTON_CLICKED);
    },

    /**
     * @function handleLogoConfigChange
     * @description Dispatch event to update the changes made in logo configuration.
     * @memberOf Communicators.HomeScreenCommunicator
    */
    handleThemeConfigurationChange: function (oNewThemeConfiguration) {
        EventBus.dispatch(Events.THEME_CONFIGURATION_UPDATED, oNewThemeConfiguration);
    },

    /**
     * @function handleArchivalButtonClicked
     * @description dispatch event to open archive view.
     * @memberOf Communicators.HomeScreenCommunicator
     */
    handleArchivalButtonClicked: function () {
      EventBus.dispatch(Events.ARCHIVE_BUTTON_CLICKED);
    },

    handleNotificationRefresh:function () {
      EventBus.dispatch(Events.HEADER_MENU_VIEW_REFRESH_NOTIFICATION);
    },

    handleSidePanelToggleClicked:function (bIsLandingPage) {
      EventBus.dispatch(Events.HANDLE_SIDE_PANEL_TOGGLE, bIsLandingPage);
    },

    setContentScreenLoaded: function (bFlag) {
      bContentScreenLoaded = bFlag;
    },

    getContentScreenLoaded: function () {
      return bContentScreenLoaded;
    },

    setSettingScreenLoaded: function (bFlag) {
      bSettingScreenLoaded = bFlag;
    },

    getSettingScreenLoaded: function () {
      return bSettingScreenLoaded;
    }
  }

})();


export const communicator = HomeScreenCommunicator;
export const events = Events;
