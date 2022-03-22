let _renderHomeScreen = async function () {
  require('./../libraries/globalloader/global-loader');
  await require('../commonmodule/store/helper/translation-manager.js').init();
  require('./../libraries/windowunloadmanager/window-unload-manager.js').init();

  const CS = require('./../libraries/cs').default;
  const LogFactory = require('./../libraries/logger/log-factory').default;
  const logger = LogFactory.getLogger('home-screen');

  const React = require('react');
  const ReactDOM = require('react-dom');
  const Loader = require('halogen/PulseLoader');

  // const ThemeLoader = require('./../libraries/themeloader/theme-loader.js');
  const LocalStorageManager = require('./../libraries/localstoragemanager/local-storage-manager').default;
  // const Keydownhandler = require('./../libraries/keydownhandler/keydownhandler');
  const LinkManager = require('../libraries/linkmanager/link-manager').default;
  const SessionStorageManager = require('./../libraries/sessionstoragemanager/session-storage-manager').default;
  const SessionStorageConstants = require('../commonmodule/tack/session-storage-constants').default;

  const SharableURLStore = require('./../commonmodule/store/helper/sharable-url-store').default;
  SharableURLStore.updateQueryProps(false, true);
  const HomeScreenStore = require('./../screens/homescreen/store/home-screen-store').default;
  const BreadCrumbProps = require('../commonmodule/props/breadcrumb-props').default;
  const BreadCrumbModuleAndHelpScreenIdDictionary = require('../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary').default;
  const CommonUtils = require('../commonmodule/util/common-utils').default;

  /**
   * @Router Impl
   */
  window.addEventListener('popstate', function (event) {
    let oForwardBreadCrumbData = BreadCrumbProps.getForwardBreadCrumbData();
    let aBreadCrumbData = BreadCrumbProps.getBreadCrumbData();
    let oStateObj = event.state;
    let sId = oStateObj && oStateObj.id;
    let oBreadcrumbData = CS.find(oForwardBreadCrumbData, {id: sId});
    let bIsEntityNavigation = SharableURLStore.getIsEntityNavigation();

    let sSelectedUILanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let sSelectedDataLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    SharableURLStore.addLanguageParamsInWindowURL(sSelectedUILanguage, sSelectedDataLanguage);

    if (bIsEntityNavigation) {

      /**Handle inconsistent state if no breadcrumb and still history state exists**/
      if (CS.isEmpty(oBreadcrumbData) && sId) {
        _makeBreadcrumbAndHistoryStateConsistent();
      }
      else if (!CS.isEmpty(oBreadcrumbData)) {
        /**No need to allow push a new state by pop-state event**/
        SharableURLStore.setIsPushHistoryState(false);

        /**Required to reset history state when coming back on Dashboard**/
        if (oBreadcrumbData.id === BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD && aBreadCrumbData.length > 1) {
          SharableURLStore.setIsEntityNavigation(false);

          let fCallback = () => {
            SharableURLStore.setIsPushHistoryState(true);
            SharableURLStore.addParamsInWindowURL(oBreadcrumbData.id, oBreadcrumbData.type);
            HomeScreenStore.handleBrowserBackOrForwardButtonClicked(oBreadcrumbData);
          };
          CS.navigateBack(fCallback);
        } else {
          HomeScreenStore.handleBrowserBackOrForwardButtonClicked(oBreadcrumbData);
        }
      }
    }
    else {
      SharableURLStore.setIsEntityNavigation(true);
    }
  });

  /**
   * @Router Impl
   * @function _makeBreadcrumbAndHistoryStateConsistent
   * @description Set history state consistent with breadcrumb.
   * @private
   */
  let _makeBreadcrumbAndHistoryStateConsistent = function () {
    let oHistoryState = CS.getHistoryState();

    if (oHistoryState && oHistoryState.id){
      SharableURLStore.setIsEntityNavigation(false);
      CS.navigateBack(_makeBreadcrumbAndHistoryStateConsistent);
    } else {
      let aBreadCrumbData = BreadCrumbProps.getBreadCrumbData();
      SharableURLStore.setIsPushHistoryState(true);

      CS.forEach(aBreadCrumbData, (oBreadCrumbData) => {
        SharableURLStore.addParamsInWindowURL(oBreadCrumbData.id, oBreadCrumbData.type, oBreadCrumbData.baseType);
      });
    }
  };



  require("react-billboardjs/lib/billboard.css");


  let UniqueIdentifierGenerator = require('./../libraries/uniqueidentifiergenerator/unique-identifier-generator.js').default;
  SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SESSION_ID, UniqueIdentifierGenerator.generateUUID());
  SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.USER_ID, "730e576b-9203-4f58-a0d6-443596f656f7");
  LocalStorageManager.setPropertyInLocalStorage('requestId', UniqueIdentifierGenerator.generateUUID());
  // ThemeLoader.loadTheme();

let fCallback = async () => {
    let oQuickViewData = {
      entityId: LinkManager.getParameterByName('entityId'),
      entityType: LinkManager.getParameterByName('entityType')
    };


    let _renderHomeScreenController = function () {
      const HomeScreenAction = require('./../screens/homescreen/action/home-screen-action').default;
      const HomeScreenController = require('./../screens/homescreen/controller/home-screen-controller').default;

      ReactDOM.render(<HomeScreenController
              store={HomeScreenStore}
              action={HomeScreenAction}
              quickViewData={oQuickViewData}/>,
          document.getElementById('mainContainer'));
    };

    await HomeScreenStore.fetchGlobalData(oQuickViewData, false);
    await HomeScreenStore.fetchLogoConfig();
    await HomeScreenStore.fetchViewConfig();
    _renderHomeScreenController();

    ReactDOM.render(<Loader color="#26A65B"/>,
        document.getElementById('loaderContainer'));
  };

  //Reset Window history state
  CommonUtils.setHistoryStateToNull(fCallback);

};

_renderHomeScreen();