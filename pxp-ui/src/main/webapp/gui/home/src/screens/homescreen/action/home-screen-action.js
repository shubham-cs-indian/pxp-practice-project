import CS from '../../../libraries/cs';
import eventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import MethodTracker from '../../../libraries/methodtracker/method-tracker';
import { events as LogoEvents } from '../../../viewlibraries/logoview/logo-view';
import { events as MenuViewEvents } from '../../../viewlibraries/menuview/menu-view.js';
import HomeScreenStore from '../store/home-screen-store';
import ActionInterceptor from '../../../libraries/actioninterceptor/action-interceptor.js';
import { events as ContentPopoverViewEvents } from '../../../viewlibraries/contentpopoverview/content-popover-view.js';
import { events as UserProfileActionEvents } from '../../../viewlibraries/userprofileview/user-profile-action-view';
import { events as UserProfileView } from '../../../viewlibraries/userprofileview/user-profile-view';
import { events as HomeScreenCommunicatorEvents } from '../store/home-screen-communicator';
import { events as PhysicalCatalogSelectorEvents } from '../../../viewlibraries/physicalcatalogselectorview/physical-catalog-selector-view';
import { events as MenuView } from '../../../viewlibraries/menuviewnew/header-menu-item-view';
import { events as MenuViewContainer } from '../../../viewlibraries/menuviewnew/header-menu-view';
import MenuDictionary from '../../../screens/homescreen/tack/menu-dictionary';
import { events as UIAndDataLanguageInfoView } from '../../../viewlibraries/uianddatalanguageinfoview/ui-and-data-language-info-view';
import RefactoringStore from "../../../commonmodule/store/refactoring-store";

var trackMe = MethodTracker.getTracker('HomeScreenAction');

var HomeScreenAction = (function () {

  const oEventHandler = {};

  //Set Selected Menu
  var setSelectedMenu = function (event, target, oModel) {
    trackMe('setSelectedMenu');
    HomeScreenStore.setSelectedMenu(oModel.id);
  };

  var handleHomeModuleClicked = function () {
    HomeScreenStore.handleHomeModuleClicked();
  };

  let handlePhysicalCatalogSelectionChanged = function (sContext, aSelectedItems) {
    HomeScreenStore.handlePhysicalCatalogSelectionChanged(sContext, aSelectedItems);
  };

  var handlePoveroverClicked = function (oModel) {
    HomeScreenStore.handlePoveroverClicked(oModel);
  };

  var handleUserMenuItemClicked = function (sSelectedActionId) {
    HomeScreenStore.handleUserMenuItemClicked(sSelectedActionId);
  };

  var handleUserMenuClosed = function () {
    HomeScreenStore.handleUserMenuClosed();
  };

  var handlePoveroverOnHide = function () {
    HomeScreenStore.handlePoveroverOnHide();
  };

  var handleUserDataChanged = function (oContext, oModel,sContext, sNewValue) {
    HomeScreenStore.handleUserDataChanged(oModel, sContext, sNewValue);
  };

  var handleLanguageChanged = function (sUILanguageCode, sDataLanguageCode) {
    HomeScreenStore.handleLanguageChanged(sUILanguageCode, sDataLanguageCode);
  };

  var handleUserChangePasswordClicked = function () {
    HomeScreenStore.handleUserChangePasswordClicked();
  };

  var handleUserDetailsSaveClicked = function (oContext, oModel) {
    HomeScreenStore.handleUserDetailsSaveClicked(oModel);
  };

  var handleUserDetailsDiscardClicked = function () {
    handleUserMenuClosed();
  };

  let handleUserPreferredLanguageChanged = function (sId, sContext) {
    HomeScreenStore.handleUserPreferredLanguageChanged(sId, sContext);
  };

  let handleRemoveUserImageClicked = function () {
    HomeScreenStore.handleRemoveUserImageClicked();
  };

  var handleUploadImageChangeEvent = function (oContext, oEvent, aFiles) {
    var oCallback = {};
    var bLimitImageSize = false;
    oCallback.functionToExecute = HomeScreenStore.handleUploadImageChangeEvent;
    RefactoringStore.uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
  };

  var handleRefreshCurrentUserDetails = function () {
    HomeScreenStore.handleRefreshCurrentUserDetails();
  };

  var handlePhysicalCatalogMenuButtonVisibility = function (bShowPhysicalCatalogMenu) {
    HomeScreenStore.handlePhysicalCatalogMenuButtonVisibility(bShowPhysicalCatalogMenu);
  };

  var disablePhysicalCatalog = function (bIsPhysicalCatalogDisabled) {
    HomeScreenStore.disablePhysicalCatalog(bIsPhysicalCatalogDisabled);
  };

  let handleSelectedUIOrDataLanguageInfoChangedFromConfig = function (oLanguage) {
    HomeScreenStore.handleSelectedUIOrDataLanguageInfoChangedFromConfig(oLanguage);
  };

  let triggerHomeScreen = function () {
    HomeScreenStore.triggerChange();
  };

  var handleAboutDialogClosed = function () {
    HomeScreenStore.handleAboutDialogClosed();
  };

  var handleNotificationCall = function () {
    HomeScreenStore.handleNotificationCall();
  };

  var handleHeaderMenuClicked = function (sContext, sId) {
    switch (sId) {
      case MenuDictionary.TASKPLANNED:
        HomeScreenStore.handleNotificationButtonClicked();
        break;
      case MenuDictionary.setting:
        trackMe('setSelectedMenu');
        HomeScreenStore.setSelectedMenu(sId);
        break;
      case MenuDictionary.runtime:
        trackMe('handleHomeModuleClicked');
        HomeScreenStore.handleHomeModuleClicked();
        break;
      case MenuDictionary.Archival:
        trackMe('handleArchivalButtonClicked');
        HomeScreenStore.handleArchivalButtonClicked(sId);
        break;
    }
  };

  let handleDataLanguageChanged = function (oCallbackData) {
    HomeScreenStore.handleDataLanguageChanged(oCallbackData);
  };

  let handleGetLanguageInfoFromServer = function () {
    HomeScreenStore.getLanguageInfoFromServer();
  };

  let hideDataLanguageOptionsOnHeader = function (bDonNotTrigger) {
    HomeScreenStore.hideDataLanguageOptionsOnHeader(bDonNotTrigger);
  };

  let showDataLanguageOptionsOnHeader = function (bDonNotTrigger) {
    HomeScreenStore.showDataLanguageOptionsOnHeader(bDonNotTrigger);
  };

  let hideUILanguageOptionsOnHeader = function (bDonNotTrigger) {
    HomeScreenStore.hideUILanguageOptionsOnHeader(bDonNotTrigger);
  };

  let showUILanguageOptionsOnHeader = function (bDonNotTrigger) {
    HomeScreenStore.showUILanguageOptionsOnHeader(bDonNotTrigger);
  };

  let handleLogoConfigUpdated = function (oNewLogoConfig) {
    HomeScreenStore.setLogoConfig(oNewLogoConfig);
  };

  let handleSidePanelToggle = function (bIsLandingPage) {
    HomeScreenStore.handleSidePanelToggle(bIsLandingPage);
  };



  /**
   * Binding Events into EventHandler
   */

  (() => {
    /** @deprecated*/
    let _setEvent = CS.set.bind(this, oEventHandler);

    oEventHandler[LogoEvents.LOGO_CLICKED] = ActionInterceptor('Logo clicked', handleHomeModuleClicked);

    oEventHandler[MenuViewEvents.MENU_SELECTION_CHANGED] = ActionInterceptor('Screen Switch', setSelectedMenu);
    oEventHandler[PhysicalCatalogSelectorEvents.HANDLE_PHYSICAL_CATALOG_OR_PORTAL_SELECTION_CHANGED] = ActionInterceptor('physical' +
        ' Catalog Changed', handlePhysicalCatalogSelectionChanged);

    oEventHandler[ContentPopoverViewEvents.HANDLE_POPOVER_CLICKED] = ActionInterceptor('Popover clicked', handlePoveroverClicked);
    oEventHandler[ContentPopoverViewEvents.HANDLE_POPOVER_ON_HIDE] = ActionInterceptor('Popover On Hide', handlePoveroverOnHide);

    oEventHandler[UserProfileActionEvents.USER_PROFILE_ACTION_VIEW_USER_MENU_ITEM_CLICKED] = ActionInterceptor('Popover action clicked', handleUserMenuItemClicked);
    oEventHandler[UserProfileActionEvents.USER_PROFILE_ACTION_VIEW_USER_MENU_HIDE] = ActionInterceptor('Screen Switch', handleUserMenuClosed);

    oEventHandler[UserProfileView.USER_PROFILE_VIEW_HANDLE_CHANGE_PASSWORD_CLICKED] = ActionInterceptor('User Change Password Clicked', handleUserChangePasswordClicked);
    oEventHandler[UserProfileView.USER_PROFILE_VIEW_HANDLE_UPLOAD_USER_IMAGE_CHANGED] = ActionInterceptor('User Image Upload', handleUploadImageChangeEvent);
    oEventHandler[UserProfileView.USER_PROFILE_VIEW_HANDLE_USER_DATA_CHANGED] = ActionInterceptor('User Data Changed', handleUserDataChanged);
    oEventHandler[UserProfileView.USER_PROFILE_VIEW_HANDLE_USER_DETAILS_SAVE_CLICKED] = ActionInterceptor('User Saved Data Changed', handleUserDetailsSaveClicked);
    oEventHandler[UserProfileView.USER_PROFILE_VIEW_HANDLE_USER_DETAILS_DISCARD_CLICKED] = ActionInterceptor('User Discard Data Changed', handleUserDetailsDiscardClicked);
    oEventHandler[UserProfileView.USER_PROFILE_PREFERRED_LANGUAGE_CHANGED] = ActionInterceptor('User Preferred Language Changed', handleUserPreferredLanguageChanged);
    oEventHandler[UserProfileView.USER_PROFILE_VIEW_HANDLE_REMOVE_USER_IMAGE_CLICKED] = ActionInterceptor('Remove User Image Clicked', handleRemoveUserImageClicked);

    oEventHandler[HomeScreenCommunicatorEvents.REFRESH_CURRENT_USER_DETAILS] = ActionInterceptor('Refresh User Details', handleRefreshCurrentUserDetails);
    oEventHandler[HomeScreenCommunicatorEvents.HANDLE_PHYSICAL_CATALOG_MENU_BUTTON_VISIBILITY] = ActionInterceptor('handle physical Catalog Menu Button Visibility', handlePhysicalCatalogMenuButtonVisibility);
    oEventHandler[HomeScreenCommunicatorEvents.HANDLE_PHYSICAL_CATALOG_DISABILITY] = ActionInterceptor('handle_physical_catalog_disability', disablePhysicalCatalog);
    oEventHandler[HomeScreenCommunicatorEvents.TRIGGER_HOME_SCREEN] = ActionInterceptor('trigger_home_screen', triggerHomeScreen);
    oEventHandler[HomeScreenCommunicatorEvents.HANDLE_LANGUAGE_INFO_CHANGED] = ActionInterceptor('handle_language_info_changed', handleSelectedUIOrDataLanguageInfoChangedFromConfig);
    oEventHandler[HomeScreenCommunicatorEvents.CONTENT_DATA_LANGUAGE_CHANGED] = ActionInterceptor('content_data_language_changed', handleDataLanguageChanged);
    oEventHandler[HomeScreenCommunicatorEvents.HIDE_DATA_LANGUAGE_OPTIONS_ON_HEADER] = ActionInterceptor(
          'hide_data_language_options_on_header', hideDataLanguageOptionsOnHeader);
    oEventHandler[HomeScreenCommunicatorEvents.SHOW_DATA_LANGUAGE_OPTIONS_ON_HEADER] = ActionInterceptor(
          'show_data_language_options_on_header', showDataLanguageOptionsOnHeader);
    oEventHandler[HomeScreenCommunicatorEvents.HIDE_UI_LANGUAGE_OPTIONS_ON_HEADER] = ActionInterceptor(
        'hide_data_language_options_on_header', hideUILanguageOptionsOnHeader);
    oEventHandler[HomeScreenCommunicatorEvents.SHOW_UI_LANGUAGE_OPTIONS_ON_HEADER] = ActionInterceptor(
        'show_data_language_options_on_header', showUILanguageOptionsOnHeader);
    oEventHandler[HomeScreenCommunicatorEvents.THEME_CONFIGURATION_UPDATED] = ActionInterceptor('Logo config updated', handleLogoConfigUpdated);
    oEventHandler[HomeScreenCommunicatorEvents.HANDLE_SIDE_PANEL_TOGGLE] = ActionInterceptor(
        'handle side panel toggle', handleSidePanelToggle);

    oEventHandler[MenuView.HANDLE_HEADER_MENU_CLICKED] = ActionInterceptor("handle header menu clicked", handleHeaderMenuClicked);
    oEventHandler[MenuViewContainer.ABOUT_BUTTON_DIALOG_ABOUT_DIALOG_CLOSED] = ActionInterceptor('ContentDashboard on load', handleAboutDialogClosed);

    /**
     * Action interceptor is not used for notification call for debugging purposes because continues calls are going
     * to fetch notifications
     */
    oEventHandler[MenuViewContainer.HEADER_MENU_VIEW_REFRESH_NOTIFICATION] = handleNotificationCall;

    // UIAndDataLanguageInfoView
    oEventHandler[UIAndDataLanguageInfoView.UI_AND_DATA_LANGUAGE_INFO_VIEW_HANDLE_LANGUAGE_CHANGED] = ActionInterceptor(
        'ui and data language info handle language changed', handleLanguageChanged);
    oEventHandler[UIAndDataLanguageInfoView.UI_AND_DATA_LANGUAGE_INFO_VIEW_GET_LANGUAGE_INFO] = ActionInterceptor(
        'ui and data language info get language info', handleGetLanguageInfoFromServer);

  })();

  return {
    //Register Event Listener
    registerEvent: function () {

      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        eventBus.addEventListener(sEventName, oHandler);
      });
    },

    //De-Register Event Listener
    deRegisterEvent: function () {

      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        eventBus.removeEventListener(sEventName, oHandler);
      });
    }
  }
})();

export default HomeScreenAction;
