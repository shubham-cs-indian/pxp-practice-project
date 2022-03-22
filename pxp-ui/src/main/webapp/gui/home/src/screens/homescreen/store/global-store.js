import alertify from '../../../commonmodule/store/custom-alertify-store';
import CS from '../../../libraries/cs';
import MicroEvent from '../../../libraries/microevent/MicroEvent.js';
import LogFactory from '../../../libraries/logger/log-factory';
import MethodTracker from '../../../libraries/methodtracker/method-tracker';
import {getTranslations, getLanguageInfo} from '../../../commonmodule/store/helper/translation-manager';
import { homeScreenRequestMapping as oHomeScreenRequestMapping } from '../tack/home-screen-request-mapping';
import HomeScreenProps from '../store/model/home-screen-props';
import SessionProps from '../../../commonmodule/props/session-props';
import HomeScreenAppData from '../store/model/home-screen-app-data';
import HomeScreenConstant from '../tack/home-screen-constants';
import SharableURLProps from '../../../commonmodule/store/model/sharable-url-props';
import EntityProps from '../../../commonmodule/props/entity-props';
import Exception from '../../../libraries/exceptionhandling/exception.js';
import ExternalAffairs from './../../../commonmodule/util/external-affairs';
import ModuleDictionary from './../../../commonmodule/tack/module-dictionary';
import PhysicalCatalogDictionary from './../../../commonmodule/tack/physical-catalog-dictionary';
import PortalTypeDictionary from './../../../commonmodule/tack/portal-type-dictionary';
import MenuDictionary from './../../../screens/homescreen/tack/menu-dictionary';
import WebsocketConstants from '../../../commonmodule/tack/websocket-constants';
import BaseTypesDictionary from '../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import MockDataForPhysicalCatalogAndPortal from '../../../commonmodule/tack/mock-data-physical-catalog-and-portal-types';
import TranslationStore from './../../../commonmodule/store/translation-store';
import SessionStorageManager from '../../../libraries/sessionstoragemanager/session-storage-manager';
import ExceptionLogger from '../../../libraries/exceptionhandling/exception-logger';
import BreadCrumbProps from '../../../commonmodule/props/breadcrumb-props';
import SessionStorageConstants from '../../../commonmodule/tack/session-storage-constants';
import PXPWebsocket from '../../../commonmodule/util/websocket-util'
import EventBus from "../../../libraries/eventdispatcher/EventDispatcher";
import {events as WebsocketEvents} from '../../../commonmodule/tack/websocket-constants';

import { setAllowedEntitiesList } from '../../../commonmodule/tack/entities-list';
import { setDataModelForClassTypeList }from '../../../commonmodule/tack/mock-data-for-class-types';
import WebsocketUtils from '../../../commonmodule/util/websocket-util'
import SharableURLStore from "../../../commonmodule/store/helper/sharable-url-store";
import EndpointTypeDictionary from "../../../commonmodule/tack/endpoint-type-dictionary";
import CommonUtils from "../../../commonmodule/util/common-utils";
var logger = LogFactory.getLogger('global-store');
var trackMe = MethodTracker.getTracker('GlobalStore');

var GlobalStore = (function () {

  var oTagValuesInList = {};
  var aTags = [];
  var oTagGroupList = {};
  var oTranslations = {};
  var oCurrentUser = {};
  var aMasterAttributes = [];
  var oAssetServerInformation = {};

  var triggerChange = function () {
    logger.info('triggerChange: HomeScreenStore',
        {
          'callTrace': MethodTracker.getTrace().join(', '),
          'data': {
            aTags: aTags
          }
        }
    );
    MethodTracker.emptyCallTrace();
    GlobalStore.trigger('global-change');
  };

  var setDefaultValuesOfTags = function (aTagGroup) {
    trackMe('setDefaultValuesOfTags');
    logger.debug(
        'setDefaultValuesOfTags: Setting default values, it takes "TagGroup" as input', {
          'TagGroup': aTagGroup,
          'TagValuesInList': oTagValuesInList
        });

    for (var iTagGroupCount = 0; iTagGroupCount < aTagGroup.length; iTagGroupCount++) {
      var oTagGroup = aTagGroup[iTagGroupCount];
      if (oTagGroup.children && oTagGroup.children.length > 0) {
        setDefaultValuesOfTags(oTagGroup.children);
      }

      var oTag = {};
      oTag.id = oTagGroup.id;
      oTag.name = oTagGroup.name;
      oTag.relevance = 0; // oTagGroup.defaultValue;
      oTagValuesInList[oTagGroup.id] = oTag;
    }
  };

  let _filterAndSetAllowedPhysicalCatalogs = function (aAllowedPhysicalCatalogIds) {
    let aFilteredAllowedPhysicalCatalogs = [];
    CS.forEach(MockDataForPhysicalCatalogAndPortal.physicalCatalogs(), function (oPhysicalCatalog) {
      if (CS.includes(aAllowedPhysicalCatalogIds, oPhysicalCatalog.id)) {
        aFilteredAllowedPhysicalCatalogs.push(oPhysicalCatalog);
      }
    });
    HomeScreenProps.setAllowedPhysicalCatalogs(aFilteredAllowedPhysicalCatalogs);
  };

  let _filterAndSetAllowedPortals = function (aAllowedPortalIds) {
    let aFilteredAllowedPortals = [];
    CS.forEach(MockDataForPhysicalCatalogAndPortal.portals(), function (oPortal) {
      if (CS.includes(aAllowedPortalIds, oPortal.id)) {
        aFilteredAllowedPortals.push(oPortal);
      }
    });
    HomeScreenProps.setAllowedPortals(aFilteredAllowedPortals);
  };

  let _checkIfPhysicalCatalogIsAllowed = function (sPhysicalCatalogId) {
    let aAllowedPhysicalCatalogs = HomeScreenProps.getAllowedPhysicalCatalogs();
    return !CS.isEmpty(CS.find(aAllowedPhysicalCatalogs, {id: sPhysicalCatalogId}));
  };

  let _setPreviousOrDefaultPhysicalCatalogAndPortal = function (sPortalIdToSet, sPhysicalCatalogId) {
    let aAllowedPhysicalCatalogs = HomeScreenProps.getAllowedPhysicalCatalogs();
    let sDefaultPhysicalCatalogToSet = sPhysicalCatalogId || PhysicalCatalogDictionary.PIM;
    let sPreviousPhysicalCatalogIdToSet = SessionProps.getSessionPhysicalCatalogId();
    if (!CS.isEmpty(sPreviousPhysicalCatalogIdToSet) && _checkIfPhysicalCatalogIsAllowed(sPreviousPhysicalCatalogIdToSet)) {
      sDefaultPhysicalCatalogToSet = sPreviousPhysicalCatalogIdToSet;
    } else if (CS.isEmpty(CS.find(aAllowedPhysicalCatalogs, {id: sDefaultPhysicalCatalogToSet}))) {
      sDefaultPhysicalCatalogToSet = aAllowedPhysicalCatalogs[0].id;
    }
    SessionProps.setSessionPhysicalCatalogId(sDefaultPhysicalCatalogToSet);

    let aAllowedPortals = HomeScreenProps.getAllowedPortals();
    let sDefaultPortalToSet = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL) || PortalTypeDictionary.PIM;
    let bIsPortalIdExistInAllowedPortals = !CS.isEmpty(CS.find(aAllowedPortals, {id: sDefaultPortalToSet}));
    if (!bIsPortalIdExistInAllowedPortals) {
      sDefaultPortalToSet = aAllowedPortals[0].id;
    }

    SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.PORTAL, sDefaultPortalToSet);
    SharableURLStore.addPortalIdInWindowURL(sDefaultPortalToSet);
  };

  var successCurrentUserCallback = function (oQuickViewData, oResponse) {
    oCurrentUser = oResponse.success;
    if (oQuickViewData && oQuickViewData.entityId && oQuickViewData.entityType) {
      _SelectMenuAccordingToQuickViewData(oQuickViewData);
    }

    let sActiveScreenId = oQuickViewData.activeScreenId || "";

    if(oCurrentUser.shouldMaintainArchives !== 0){
      oCurrentUser.screenModuleMapping.screens.push({
        id: MenuDictionary.Archival,
        className: "archivalButton",
        title: 'ARCHIVAL',
        isVisible: true,
      });
    }

    HomeScreenProps.setPreferredUILanguage(oCurrentUser.preferredUILanguage);
    HomeScreenProps.setPreferredDataLanguage(oCurrentUser.preferredDataLanguage);
    _fetchGlobalModuleData(oCurrentUser.screenModuleMapping, oCurrentUser.isSettingAllowed, sActiveScreenId);
    if (CS.isEmpty(oQuickViewData) || !oQuickViewData.isForDataIntegration) {
      // _fetchGlobalModuleData(oCurrentUser.screenModuleMapping, oCurrentUser.isSettingAllowed, sActiveScreenId);
      SessionProps.setSessionOrganizationId(oCurrentUser.organizationId);
      let aAllowedPhysicalCatalogIds = oCurrentUser.allowedPhysicalCatalogIds || [];
      _filterAndSetAllowedPhysicalCatalogs(aAllowedPhysicalCatalogIds);
      let aAllowedPPortalIds = oCurrentUser.allowedPortalIds || [];
      _filterAndSetAllowedPortals(aAllowedPPortalIds);
      _setPreviousOrDefaultPhysicalCatalogAndPortal(oQuickViewData.portalIdToSet, oQuickViewData.physicalCatalogId);
    }

    if (oQuickViewData && oQuickViewData.functionToExecute) {
      oQuickViewData.functionToExecute();
    }

    if(SessionProps.getIsArchive()){
      let aAllMenus = HomeScreenAppData.getAllMenus();
      CS.remove(aAllMenus, {id: ModuleDictionary.DASHBOARD});
    }

    PXPWebsocket.openSession();
    triggerChange();
    return true;
  };

  var failureCurrentUserCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      ExceptionLogger.error("Failed in failureCurrentUserCallback");
      ExceptionLogger.log(oResponse);
    } else {
      ExceptionLogger.error("Something went wrong in failureCurrentUserCallback");
      ExceptionLogger.error(oResponse);
    }
    return false;
  };

  var _fetchTranslations = function (oCallback) {
    let sUILanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    return TranslationStore.fetchTranslations(['home', 'view', 'dashboard', 'content'], sUILanguage, oCallback);
  };

  var _fetchPhysicalCatalog = function () {
    return CS.getRequest(oHomeScreenRequestMapping.GetPhysicalCatalogIds, {}, successFetchPhysicalCatalogCallBack, failureFetchPhysicalCatalogCallBack);
  };

  let successFetchPhysicalCatalogCallBack = (oResponse) => {
    let oSuccess = oResponse.success;
    let aAvailablePhysicalCatalogIds = oSuccess.availablePhysicalCatalogIds;
    MockDataForPhysicalCatalogAndPortal.setPhysicalCatalog(aAvailablePhysicalCatalogIds);

  };

  let failureFetchPhysicalCatalogCallBack = (oResponse) => {
    CommonUtils.failureCallback(oResponse, 'failureFetchPhysicalCatalogCallBack', getTranslations()); // eslint-disable-line
  }

  let _fetchPortals = function(){
    return CS.getRequest(oHomeScreenRequestMapping.GetPortals, {}, successFetchPortals, failureFetchPortals);
  };

  let successFetchPortals = (oResponse) => {
    let oSuccess = oResponse.success;
    let aPortals = oSuccess.portals;
    let aAllEntities = [];
    let aPortalIds = [];
    CS.forEach(aPortals,(oPortal)=> {
      if(oPortal.id === PortalTypeDictionary.PIM) {
        setAllowedEntitiesList(oPortal.allowedEntities);
      }
      aPortalIds.push(oPortal.id);
      aAllEntities = CS.combine(aAllEntities, oPortal.allowedEntities);
    });
    EntityProps.setAllEntityIds(aAllEntities);
    setDataModelForClassTypeList(aAllEntities);
    MockDataForPhysicalCatalogAndPortal.setPortals(aPortalIds);
  };

  let failureFetchPortals = (oResponse) => {
    CommonUtils.failureCallback(oResponse,'failureFetchPhysicalCatalogCallBack',getTranslations()); // eslint-disable-line
  };

  var _fetchCurrentUserInformation = function (oQuickViewData) {
    return CS.getRequest(oHomeScreenRequestMapping.GetCurrentUser, {}, successCurrentUserCallback.bind(this, oQuickViewData), failureCurrentUserCallback, {portalId: oQuickViewData.portalIdToSet});
  };

  let _updateSessionProps = function (bDontResetPhysicalCatalog) {
    HomeScreenProps.setIsInsideDataIntegration(false);

    var bDontResetSessionProps = SessionProps.getDontResetSessionPropsURLDependencyFlag();
    if(!bDontResetSessionProps){
      if (!bDontResetPhysicalCatalog) {
        SessionProps.setSessionPhysicalCatalogData(null);
      }
      SessionProps.setSessionEndpointId("");
      SessionProps.setSessionEndpointType("");
    }else {
      SessionProps.setDontResetSessionPropsURLDependencyFlag(false);
    }
  };

  var _checkModulesVisibilityStatus = function (aScreens) {
    var oModuleDictionary = ModuleDictionary;
    var aRuntimeModules = [];
    var aItemsToRemove = [];

    !ExternalAffairs.getPimModuleVisibilityStatus() && aItemsToRemove.push(oModuleDictionary.PIM);
    !ExternalAffairs.getMamModuleVisibilityStatus() && aItemsToRemove.push(oModuleDictionary.MAM);
    !ExternalAffairs.getTargetModuleVisibilityStatus() && aItemsToRemove.push(oModuleDictionary.TARGET);
    !ExternalAffairs.getTextassetModuleVisibilityStatus() && aItemsToRemove.push(oModuleDictionary.TEXT_ASSET);
    !ExternalAffairs.getFilesModuleVisibilityStatus() && aItemsToRemove.push(oModuleDictionary.FILES);
    !ExternalAffairs.getAllModuleVisibilityStatus() && aItemsToRemove.push(oModuleDictionary.ALL);
    !ExternalAffairs.getSupplierModuleVisibilityStatus() && aItemsToRemove.push(oModuleDictionary.SUPPLIER);
    !ExternalAffairs.getJosStatusModuleVisibilityStatus() && aItemsToRemove.push(oModuleDictionary.JOSSTATUS);

    var aModuleList = HomeScreenAppData.getAllModules();
    !ExternalAffairs.getDashboardVisibilityStatus() && CS.remove(aModuleList, {id: MenuDictionary.dashboard});
    !ExternalAffairs.getTaskDashboardVisibilityStatus() && CS.remove(aModuleList, {id: MenuDictionary.taskDashboard});

    var oRuntime = CS.find(aScreens, {id: MenuDictionary.runtime});
    aRuntimeModules = oRuntime.modules;
    CS.forEach(aItemsToRemove, function (sItemToRemove) {
      CS.remove(aRuntimeModules, {id: sItemToRemove});
    });
  };

  var _fetchGlobalModuleData = function (oGlobalModulesData, bIsSettingAllowed, sActiveScreenId) {

    _checkModulesVisibilityStatus(oGlobalModulesData.screens);

    HomeScreenProps.setGlobalModulesData(oGlobalModulesData);
    var aScreens = oGlobalModulesData.screens;
    var aModuleList = HomeScreenAppData.getAllModules();

    let sEntityId = SharableURLProps.getEntityId();
    if(!CS.isEmpty(sEntityId)) {
      sActiveScreenId = "runtime";
    }

    if (!ExternalAffairs.getDashboardVisibilityStatus()) {
      CS.remove(aModuleList, {id: MenuDictionary.dashboard});
    }

    try {
      var oSettingModule = CS.find(aScreens, {id: MenuDictionary.setting});
      oSettingModule.isVisible = bIsSettingAllowed;
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }

    CS.forEach(aScreens, function (oScreen) {
      if (oScreen.id === MenuDictionary.runtime) {
        _addPropertiesToScreenModuleList(oScreen);
      }
      var oModule = CS.find(aModuleList, {type: oScreen.type});
      if (oModule) {
        CS.assign(oScreen, oModule);
        if (sActiveScreenId) {
          //if we want some screen selected instead of default screen
          if (oScreen.id === sActiveScreenId) {
            _setActiveScreen(oScreen);
          }
        } else if (oScreen.isDefault) {
          _setActiveScreen(oScreen);
        }
      }
    });
  };

  let _setActiveScreen = function (oScreen) {
    oScreen.isSelected = true;
    HomeScreenProps.setSelectedMenu(oScreen);
  };

  var _getIsSelectedModuleBasedOnBaseType = function (oModule, sURLBaseType) {
    let sExpectedSelectedModuleId = "";
    switch (sURLBaseType){
      case BaseTypesDictionary["contentBaseType"]:
      case BaseTypesDictionary["setBaseType"]:
      case BaseTypesDictionary["collectionKlassInstanceEntityBaseType"]:
        sExpectedSelectedModuleId = ModuleDictionary.PIM;
        break;

      case BaseTypesDictionary["assetBaseType"]:
      case BaseTypesDictionary["assetInstanceCollection"]:
      case BaseTypesDictionary["imageAttributeInstanceBaseType"]:
        sExpectedSelectedModuleId = ModuleDictionary.MAM;
        break;

      case BaseTypesDictionary["marketBaseType"]:
      case BaseTypesDictionary["targetInstanceCollection"]:
        sExpectedSelectedModuleId = ModuleDictionary.TARGET;
        break;

      case BaseTypesDictionary["supplierBaseType"]:
        sExpectedSelectedModuleId = ModuleDictionary.SUPPLIER;
        break;

      case BaseTypesDictionary["textAssetBaseType"]:
        sExpectedSelectedModuleId = ModuleDictionary.TEXT_ASSET;
        break;

      default:
        sExpectedSelectedModuleId = ModuleDictionary.DASHBOARD;
        break;
    }

    return oModule.id === sExpectedSelectedModuleId
  };

  var _addPropertiesToScreenModuleList = function (oScreen) {
    var aModuleList = oScreen.modules;
    let aModulesToExclude = [];
    let sEndpointType = SessionProps.getSessionEndpointType();
    if (!CS.isEmpty(aModuleList)) {
      let sURLBaseType = SharableURLProps.getEntityBaseType();
      CS.forEach(aModuleList, function (oModule, iIndex) {
        let sModuleLabel = oModule.label;
        oModule.label = getTranslations()[sModuleLabel] || sModuleLabel;
        oModule.isSelected = false;
        if (CS.isEmpty(sURLBaseType) && sEndpointType === EndpointTypeDictionary.INBOUND_ENDPOINT && iIndex == 0) {
          oModule.isSelected = true;
        } else if (CS.isEmpty(sURLBaseType) && sEndpointType === EndpointTypeDictionary.OUTBOUND_ENDPOINT && iIndex == 1) {
          oModule.isSelected = true;
        } else {
          oModule.isSelected = _getIsSelectedModuleBasedOnBaseType(oModule, sURLBaseType);
        }
      });
      }

    if(SessionProps.getIsArchive()) {
      CS.remove(aModuleList, {id: ModuleDictionary.DASHBOARD});
      aModuleList[0].isSelected = true;
    }
    HomeScreenAppData.setAllMenus(aModuleList);
  };

  var _setMenuSelection = function (sMenu) {
    var aMenus = HomeScreenAppData.getAllModules();
    CS.forEach(aMenus, function (oMenu) {
      oMenu.isSelected = false;
      if (sMenu == oMenu.id) {
        oMenu.isSelected = true;
        HomeScreenProps.setSelectedMenu(oMenu);
      }
    })
  };

  var _SelectMenuAccordingToQuickViewData = function (oQuickViewData) {
    var sEntityType = oQuickViewData.entityType;
    switch (sEntityType) {
      case HomeScreenConstant.ENTITY_ARTICLE:
      case HomeScreenConstant.ENTITY_SET:
      case HomeScreenConstant.ENTITY_COLLECTION:
        _setMenuSelection(HomeScreenConstant.PIM);
        break;

      case HomeScreenConstant.ENTITY_ASSET:
      case HomeScreenConstant.ENTITY_ASSET_COLLECTION:
        _setMenuSelection(HomeScreenConstant.MAM);
        break;

      case HomeScreenConstant.ENTITY_MARKET:
      case HomeScreenConstant.ENTITY_TARGET:
        _setMenuSelection(HomeScreenConstant.TARGET);
        break;
    }
  };

  var _fetTagList = function (oCallback) {
    trackMe('_fetTagList');
    var oParameters = {};
    oParameters.id = "-1";
    oParameters.mode = MenuDictionary.all;
    CS.getRequest(oHomeScreenRequestMapping.GetAllTags, oParameters, successFetchTagListCallBack.bind(this, oCallback), failureFetchTagListCallBack);
  };

  var successFetchTagListCallBack = function (oCallback, oResponse) {
    aTags = oResponse.success.children;

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute(aTags);
    }
    triggerChange();
  };

  var failureFetchTagListCallBack = function (oResponse) {
    trackMe('failureFetchTagListCallBack');

    if (!CS.isEmpty(oResponse.failure)) {
      alertify.error(Exception.getMessage(oResponse, ""), 0);
      logger.errorAjaxRequest('Failure in fetch tag list',
          {'response': oResponse.failure}, 'tag/get/failure');
    } else {
      ExceptionLogger.error("failureFetchTagListCallBack: Something went wrong: ");
      ExceptionLogger.error(oResponse);
      logger.error("failureFetchTagListCallBack: Something went wrong", oResponse);
    }
  };

  let _showAlertOnLanguageChange = () => {
    let bIsAfterLogin = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.IS_AFTER_LOGIN);
    if (bIsAfterLogin) {
      let bIsLanguageChanged = SessionProps.getIsLanguageChanged();
      if (bIsLanguageChanged) {
        alertify.error(getTranslations()['AUTO_LANGUAGE_CHANGED']);
      }
      SessionStorageManager.removePropertyFromSessionStorage(SessionStorageConstants.IS_AFTER_LOGIN);
    }
  };

  let _fetchCurrencyExchangeRates = () => {
    CS.getRequest(oHomeScreenRequestMapping.GetCurrencyExchangeRates, {}, successFetchCurrencyExchangeRatesCallBack, failureFetchCurrencyExchangeRatesCallBack);
  };

  let successFetchCurrencyExchangeRatesCallBack = (oResponse) => {
    HomeScreenAppData.setExchangeRates(oResponse.success.exchangeRates);
  };

  let failureFetchCurrencyExchangeRatesCallBack = (oResponse) => {
    if (!CS.isEmpty(oResponse.failure)) {
      alertify.error(Exception.getMessage(oResponse, ""), 0);
      logger.errorAjaxRequest('Failure in fetch currency exchange rates',
          {'response': oResponse.failure}, 'config/exchangerates');
    } else {
      ExceptionLogger.error("failureFetchCurrencyExchangeRatesCallBack: Something went wrong: ");
      ExceptionLogger.error(oResponse);
      logger.error("failureFetchCurrencyExchangeRatesCallBack: Something went wrong", oResponse);
    }
  };

  let _closeWebsocketConnection = () => {
    PXPWebsocket.closeSession();
  }

  let _websocketOnMessage = (oMessage) => {
    switch(oMessage.message) {
      case WebsocketConstants.DTP_DOCUMENT_UPDATED_FROM_IDSN:
        EventBus.dispatch(WebsocketEvents.DTP_DOCUMENT_UPDATED_FROM_IDSN, oMessage);
        break;
      case WebsocketConstants.DTP_DOCUMENT_UPDATED_FROM_WBD:
        EventBus.dispatch(WebsocketEvents.DTP_DOCUMENT_UPDATED_FROM_WBD, oMessage);
        break;
      case WebsocketConstants.SLOT_LOCKED_FROM_WHITEBOARD:
        EventBus.dispatch(WebsocketEvents.SLOT_LOCKED_FROM_WHITEBOARD, oMessage);

    }
  };

  let _websocketOnOpen = (event) => {
  };

  let _websocketOnClose = (event) => {
    if (event.code === 1001 && event.reason === "Idle Timeout") {
      WebsocketUtils.openSession();
    }
  };

  let _websocketOnError = (event) => {

  };

  return {
    getTags: function () {
      return aTags;
    },

    getCurrentUser: function () {
      return oCurrentUser;
    },

    getLanguageInfo: function () {
      return getLanguageInfo();
    },

    getAssetServerInformation: function () {
      return oAssetServerInformation;
    },

    setTags: function (_aTags) {
      oTagValuesInList = {};
      aTags = _aTags;
    },

    getTagValuesList: function () {
      return oTagValuesInList;
    },

    getTagGroupList: function () {
      return oTagGroupList;
    },

    setTagValue: function (iTagId, oTagValues) {
      trackMe('setTagValue');
      logger.info('setTagValue: set Tag value', {iTagId: oTagValues});
      oTagValuesInList[iTagId] = oTagValues;
    },

    getTagsWithDefaultRelevances: function () {
      trackMe('getTagsWithDefaultRelevances');
      var oTagValuesList = this.getTagValuesList();
      var aTagsWithDefaultRelevances = [];
      CS.forEach(oTagValuesList, function (oTag) {
        //oTag contains only value (i.e an object)  e.g. {id: 28, name: SE, relevance: 0}
        var oSelectedTag = CS.cloneDeep(oTag);
        oSelectedTag.relevance = 0;
        aTagsWithDefaultRelevances.push(oSelectedTag);
      });
      logger.debug(
          'getTagsWithDefaultRelevances: return "aTagsWithDefaultRelevances"',
          {'TagsWithDefaultRelevance': aTagsWithDefaultRelevances});
      return aTagsWithDefaultRelevances;
    },

    getTranslations: function (sContext) {
      return getTranslations(sContext);
    },

    setTranslations: function (_oTranslations) {
      oTranslations = _oTranslations;
    },

    setDefaultTagValueInTagValueList: function () {
      trackMe('setDefaultTagValueInTagValueList');
      var oTagValuesList = GlobalStore.getTagValuesList();
      CS.forEach(oTagValuesList, function (oTag) {
        oTag.relevance = 0;
      });
    },

    getMasterAttributes: function () {
      return aMasterAttributes;
    },

    updateSessionProps: function (oCallback, bDontResetPhysicalCatalog) {
      _updateSessionProps(oCallback, bDontResetPhysicalCatalog);
    },

    fetchCurrentUserInformation: function (oCallback) {
      _fetchCurrentUserInformation(oCallback);
    },

    fetchMasterData: async function (oQuickViewData, bDontResetPhysicalCatalog, oCallbackData) {
      trackMe('fetchMasterData');
      logger.debug('fetchMasterData: fetch all default tags, content class, content status and content type !');
      //_fetchAssetServerInformation();
      _fetchCurrencyExchangeRates();
      await _fetchTranslations({});
      await _showAlertOnLanguageChange();
      await _fetchPhysicalCatalog();
      await _fetchPortals();

      if (oQuickViewData && oQuickViewData.isForDataIntegration) {
        HomeScreenProps.setIsInsideDataIntegration(true);
      } else {
        _updateSessionProps(bDontResetPhysicalCatalog);
      }
      return _fetchCurrentUserInformation(oQuickViewData);
    },

    getSelectedMenu: function () {
      var aScreens = HomeScreenProps.getGlobalModulesData().screens;
      var oSelectedMenu = CS.find(aScreens, "isSelected");

      return oSelectedMenu;
    },

    setSelectedMenu: function (sSelectedMenuId) {
      var aScreens = HomeScreenProps.getGlobalModulesData().screens;
      let oPreviousScreen = {};
      CS.forEach(aScreens, function (oScreen) {
        if(oScreen.isSelected){
          oPreviousScreen = oScreen;
        }
        if (oScreen.id == sSelectedMenuId) {
          if (oScreen.isVisible) {
            oScreen.isSelected = true;
          } else {
            alertify.warning(oTranslations['HomeScreenTranslations'].PERMISSION_DENIED);
          }
        } else {
          oScreen.isSelected = false;
        }
      });

      if(HomeScreenProps.getIsInsideDataIntegration() && oPreviousScreen.id === MenuDictionary.runtime) {
        let aBreadCrumbData = BreadCrumbProps.getBreadCrumbData();
        BreadCrumbProps.setPreviousBreadCrumbData(aBreadCrumbData);
      }
      BreadCrumbProps.setBreadCrumbData([]);
      triggerChange();
    },

    fetchTagList: function (oCallback) {
      _fetTagList(oCallback);
    },

    getAllMenus: function () {
      return HomeScreenAppData.getAllMenus();
    },

    getAllModules: function () {
      return HomeScreenAppData.getAllModules();
    },

    getGlobalModulesData: function () {
      return HomeScreenProps.getGlobalModulesData();
    },

    getSelectedModule: function () {
      var aModuleList = HomeScreenAppData.getAllMenus();
      return CS.find(aModuleList, {isSelected: true}) || {};
    },

    getAllowedPhysicalCatalogs: function () {
      return HomeScreenProps.getAllowedPhysicalCatalogs();
    },

    closeWebsocketConnection() {
      _closeWebsocketConnection();
    },

    websocketOnMessage: function(oMessage) {
      _websocketOnMessage(oMessage);
    },

    websocketOnOpen: function(event) {
      _websocketOnOpen(event);
    },

    websocketOnClose: function(event) {
      _websocketOnClose(event);
    },

    websocketOnError: function(event) {
      _websocketOnError(event);
    },

  }
})();

MicroEvent.mixin(GlobalStore);

export default GlobalStore;
