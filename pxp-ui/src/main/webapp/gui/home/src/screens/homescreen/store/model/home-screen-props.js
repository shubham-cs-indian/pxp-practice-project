import CS from '../../../../libraries/cs';
import HomeScreenAppData from './home-screen-app-data';
import DashboardModuleList from './../../tack/dashboard-module-list';
import aUserProfileActionList from '../../../../commonmodule/tack/mock-data-for-user-profile-action';
import {getLanguageInfo as oLanguageInfo} from '../../../../commonmodule/store/helper/translation-manager';

var HomeScreenProps = (function () {

  var oSelectedMenu = {};//HomeScreenAppData.getAllModules()[0];
  var oPopoverDetails = {
    isVisible: false,
    popoverstyle: {}
  };
  var bIsValidUser = true;
  var bChangePasswordEnabled = false;
  var oErrorFields = {};
  var bAboutDialogVisibility = false;
  var oGlobalModulesData = {};
  var aModuleList = DashboardModuleList || [];
  let aAllowedPhysicalCatalogs = [];
  let aAllowedPortals = [];
  var bShowPhysicalCatalogMenu = true;
  let bIsPhysicalCatalogDisabled = false;
  let bIsInsideDataIntegration = false;
  let bShowUserInformationView = false;
  let bShowUserLanguagesView = false;
  let oLanguageSelectionData = oLanguageInfo().userInterfaceLanguages;
  let bIsResetRequired = false;
  let bIsUIAndDataLanguageSelectionDialogOpen = false;
  let iUnreadNotificationCount = 0;
  let bIsNotificatioinChanged = false;
  let bIsHideDataLanguageOptions = false;
  let bIsHideUILanguageOptions = false;
  let sSelectedUserPreferredUILanguage = "";
  let sSelectedUserPreferredDataLanguage = "";
  let sPreferredUILanguage = "";
  let sPreferredDataLanguage = "";

  let oLogoConfig ={
    primaryLogoId: null,
    faviconId: null,
    logoTitle: "",
  };

  let oViewConfig ={
    isLandingPageExpanded: true,
    isProductInfoPageExpanded: false,
  };

  return {
    getAboutDialogVisibility: function () {
      return bAboutDialogVisibility;
    },

    setAboutDialogVisibility: function (bFlag) {
      bAboutDialogVisibility = bFlag;
    },

    getSelectedMenu: function () {
      var aModuleList = HomeScreenAppData.getAllModules();
      return CS.find(aModuleList, {isSelected: true}) || aModuleList[0];
    },

    setSelectedMenu: function (oMenu) {
      oSelectedMenu = oMenu;
    },

    getPopoverDetails: function () {
      return oPopoverDetails;
    },

    emptyPopoverDetails: function () {
      oPopoverDetails = {
        isVisible: false,
        popoverstyle: {}
      };
    },

    setUsernameValidity: function (_bIsValidUser) {
      bIsValidUser = _bIsValidUser;
    },

    getUsernameValidity: function () {
      return bIsValidUser;
    },

    getErrorFields: function () {
      return oErrorFields;
    },

    emptyErroFields: function () {
      oErrorFields = {};
    },

    getChangePasswordEnabled: function () {
      return bChangePasswordEnabled;
    },

    setChangePasswordEnabled: function (_bChangePasswordEnabled) {
      bChangePasswordEnabled = _bChangePasswordEnabled;
    },

    setDashboardModuleList: function (_aModuleList) {
      aModuleList = _aModuleList;
    },

    getDashboardModuleList: function () {
      return aModuleList;
    },

    getGlobalModulesData: function () {
      return oGlobalModulesData;
    },

    setGlobalModulesData: function (_oGlobalModulesData) {
      oGlobalModulesData = _oGlobalModulesData;
    },

    getAllowedPhysicalCatalogs: function () {
      return aAllowedPhysicalCatalogs;
    },

    setAllowedPhysicalCatalogs: function (_aAllowedPhysicalCatalogs) {
      aAllowedPhysicalCatalogs = _aAllowedPhysicalCatalogs;
    },

    getAllowedPortals: function () {
      return aAllowedPortals;
    },

    setAllowedPortals: function (_aAllowedPortals) {
      aAllowedPortals = _aAllowedPortals;
    },

    getShowPhysicalCatalogMenu: function () {
      return bShowPhysicalCatalogMenu;
    },

    setShowPhysicalCatalogMenu: function (_bShowPhysicalCatalogMenu) {
      bShowPhysicalCatalogMenu = _bShowPhysicalCatalogMenu;
    },

    getIsPhysicalCatalogDisabled: function () {
      return bIsPhysicalCatalogDisabled;
    },

    setIsPhysicalCatalogDisabled: function (_bIsPhysicalCatalogDisabled) {
      bIsPhysicalCatalogDisabled = _bIsPhysicalCatalogDisabled;
    },

    getIsInsideDataIntegration: function () {
      return bIsInsideDataIntegration;
    },

    setIsInsideDataIntegration: function (_bIsInsideDataIntegration) {
      bIsInsideDataIntegration = _bIsInsideDataIntegration;
    },

    getShowUserInformationView: function () {
      return bShowUserInformationView;
    },

    setShowUserInformationView: function (bStatus) {
      bShowUserInformationView = bStatus;
    },

    getShowUserLangaugesView: function () {
      return bShowUserLanguagesView;
    },

    setShowUserLangaugesView: function (bStatus) {
      bShowUserLanguagesView = bStatus;
    },

    getLanguageSelectionData: function ()
    {
      return oLanguageSelectionData;
    },

    getUserProfileActionList: function (){
      return aUserProfileActionList();
    },

    getIsResetRequired: function () {
      return bIsResetRequired;
    },

    setIsResetRequired: function (_bIsResetRequired) {
      bIsResetRequired = _bIsResetRequired;
    },

    getUnreadNotificationCount : function (){
      return iUnreadNotificationCount;
    },

    setUnreadNotificationCount : function (_iUnreadNotificationCount){
      iUnreadNotificationCount = _iUnreadNotificationCount;
    },

    getIsNotificatioinChanged : function (){
      return bIsNotificatioinChanged;
    },

    setIsNotificatioinChanged : function (_bIsNotificatioinChanged){
      bIsNotificatioinChanged = _bIsNotificatioinChanged;
    },

    getIsUIAndDataLanguageSelectionDialogOpen: function () {
      return bIsUIAndDataLanguageSelectionDialogOpen;
    },

    setIsUIAndDataLanguageSelectionDialogOpen: function (_bIsUIAndDataLanguageSelectionDialogOpen) {
      bIsUIAndDataLanguageSelectionDialogOpen = _bIsUIAndDataLanguageSelectionDialogOpen;
    },

    getIsHideDataLanguageOptions: function () {
      return bIsHideDataLanguageOptions;
    },

    setIsHideDataLanguageOptions: function (_bIsHideDataLanguageOptions) {
        bIsHideDataLanguageOptions = _bIsHideDataLanguageOptions;
    },

    getIsHideUILanguageOptions: function () {
      return bIsHideUILanguageOptions;
    },

    setIsHideUILanguageOptions: function (_bIsHideUILanguageOptions) {
      bIsHideUILanguageOptions = _bIsHideUILanguageOptions;
    },

    getLogoConfig(){
      return oLogoConfig;
    },

    setLogoConfig(oNewLogoObj){
      CS.assign(oLogoConfig, oNewLogoObj);
    },

    getViewConfig(){
      return oViewConfig;
    },

    setViewConfig(oNewViewObj){
      CS.assign(oViewConfig, oNewViewObj);
    },

    getSelectedUserPreferredUILanguage() {
      return sSelectedUserPreferredUILanguage;
    },

    setSelectedUserPreferredUILanguage(_selectedUserPreferredUILanguage) {
      sSelectedUserPreferredUILanguage = _selectedUserPreferredUILanguage;
    },

    getSelectedUserPreferredDataLanguage() {
      return sSelectedUserPreferredDataLanguage;
    },

    setSelectedUserPreferredDataLanguage(_selectedUserPreferredDataLanguage) {
      sSelectedUserPreferredDataLanguage = _selectedUserPreferredDataLanguage;
    },

    getPreferredUILanguage() {
      return sPreferredUILanguage;
    },

    setPreferredUILanguage(_sPreferredUILanguage) {
      sPreferredUILanguage = _sPreferredUILanguage;
    },

    getPreferredDataLanguage() {
      return sPreferredDataLanguage;
    },

    setPreferredDataLanguage(_sPreferredDataLanguage) {
      sPreferredDataLanguage = _sPreferredDataLanguage;
    },

    toJSON: function () {
      return {
        oSelectedMenu: oSelectedMenu
      };
    }
  }
})();

export default HomeScreenProps;