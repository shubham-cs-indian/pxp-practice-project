import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { Suspense } from 'react';
import LogFactory from '../../../libraries/logger/log-factory';
import MenuDictionary from '../tack/menu-dictionary';
import { view as MainScreen } from './../view/main-screen';
import { view as MenuViewContainer } from '../../../viewlibraries/menuviewnew/header-menu-view';
import MockDataForMenu from '../../../commonmodule/tack/mock-data-for-header-menu.js';
import { view as PhysicalCatalogSelectorView } from '../../../viewlibraries/physicalcatalogselectorview/physical-catalog-selector-view';
import { view as UserProfileActionView } from '../../../viewlibraries/userprofileview/user-profile-action-view';
import { getTranslations as oTranslations } from '../../../commonmodule/store/helper/translation-manager.js';
import RoleTypeDictionary from '../../../commonmodule/tack/role-type-dictionary';
import SessionProps from '../../../commonmodule/props/session-props';
import UniqueIdentifierGenerator from './../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { view as UIAndDataLanguageInfoView } from '../../../viewlibraries/uianddatalanguageinfoview/ui-and-data-language-info-view';
import SessionStorageManager from '../../../libraries/sessionstoragemanager/session-storage-manager';
import SessionStorageConstants from '../../../commonmodule/tack/session-storage-constants';
import RequestMapping from '../../../libraries/requestmappingparser/request-mapping-parser.js';
import { UploadRequestMapping as oUploadRequestMapping } from '../../../viewlibraries/tack/view-library-request-mapping';
import PhysicalCatalogDictionary from '../../../commonmodule/tack/physical-catalog-dictionary';
var ContentScreenController = React.lazy(()=>import('../screens/contentscreen/controller/content-screen-controller'));
var SettingScreenController = React.lazy(()=>import('../screens/settingscreen/controller/setting-screen-controller'));
var logger = LogFactory.getLogger('home-screen-controller');

const oPropTypes = {
  store: ReactPropTypes.object.isRequired,
  action: ReactPropTypes.object.isRequired,
  quickViewData: ReactPropTypes.object
};

// @CS.SafeComponent
class HomeScreenController extends React.Component {
  static propTypes = oPropTypes;

  constructor(props, context) {
    super(props, context);

    this.state = {
      appData: this.getStore().getData().appData,
      componentProps: this.getStore().getData().componentProps,
      uniqueKey: UniqueIdentifierGenerator.generateUUID()
    };

    /**
     * Needed to put register event in constructor because task-notification sends call on load of the view
     */
    props.action.registerEvent();
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    let oComponentProps = oState.componentProps;
    let bIsResetRequired = oComponentProps.getIsResetRequired();
    if(bIsResetRequired) {
      return {
        uniqueKey: UniqueIdentifierGenerator.generateUUID()
      }
    }

    return null;
  }
  //@Bind: Store with state
  componentDidMount() {
    this.debouncedHomeScreenStateChange = CS.debounce(this.homeScreenStateChanged, 10);
    this.getStore().bind('change', this.debouncedHomeScreenStateChange);
  }

  componentDidUpdate() {
    this.getComponentProps().setIsResetRequired(false);
    this.getComponentProps().setIsNotificatioinChanged(false);
  }

  //@UnBind: store with state
  componentWillUnmount = () => {
    this.getStore().unbind('change', this.debouncedHomeScreenStateChange);
    this.props.action.deRegisterEvent();
  }

  //@set: state
  homeScreenStateChanged = () => {

    var changedState = {
      appData: this.getStore().getData().appData,
      componentProps: this.getStore().getData().componentProps
    };

    this.setState(changedState);
  };

  getStore = () => {
    return this.props.store;
  };

  getComponentProps = () => {

    return this.state.componentProps;
  };

  getAppData = () => {

    return this.state.appData;
  };

  handleOnMenuContainerClick =(oEvent)=> {
    if (!CS.isEmpty(oEvent)) {
      oEvent.nativeEvent.isMenuContainerClicked = true; //Do not remove
    }
  };

  //Return Component According to Menu Selection
  getSelectedModule = (oSelectedMenu) => {
    var oQuickViewData = this.props.quickViewData;
    var oGlobalModulesData = this.getComponentProps().getGlobalModulesData();
    var aScreens = oGlobalModulesData.screens;
    logger.debug('Selected menu', oSelectedMenu);
    let oComponentProps = this.getComponentProps();
    let oViewConfigData = this.getStore().getViewConfig();

    switch (oSelectedMenu && oSelectedMenu.type) {
      case MenuDictionary['home']:
        var oScreenMasterData = CS.find(aScreens, {id: oSelectedMenu.id}) || {};
        return <Suspense fallback={<div/>}>
          <ContentScreenController
              mode={oSelectedMenu.name}
              quickViewData={oQuickViewData}
              screenMasterData={oScreenMasterData}
              isInsideDataIntegration={oComponentProps.getIsInsideDataIntegration()}
              viewConfigData={oViewConfigData}
          />
        </Suspense>;


      case MenuDictionary['setting']:
        return <Suspense fallback={<div/>}>
          <SettingScreenController/>
        </Suspense>;
    }
  };

  getMenuList = () => {
    var oGlobalModulesData = this.getComponentProps().getGlobalModulesData();
    return oGlobalModulesData.screens;
  };

  getSelectedMenu = (aMenuList) => {
    return CS.find(aMenuList, {isSelected: true});
  };

  getUserProfileData (oSelectedMenu) {
    let oLanguageInfo = SessionProps.getLanguageInfoData();
    let oComponentProps = this.getComponentProps();
    var oCurrentUser = this.props.store.getCurrentUser();
    var oClonedUser = oCurrentUser.clonedObject ? oCurrentUser.clonedObject : oCurrentUser;
    let bIsSettingScreenActive = oSelectedMenu && oSelectedMenu.type === MenuDictionary['setting'];
    let oUserProfileActionData = CS.clone(oComponentProps.getUserProfileActionList());
    let sSelectedPreferredUILanguage = oComponentProps.getSelectedUserPreferredUILanguage();
    let sSelectedPreferredDataLanguage = oComponentProps.getSelectedUserPreferredDataLanguage();
    // if (bShowPortalMenu) {
    //   let iSpliceIndex = 2;
    //   let aAllowedPortals = this.getComponentProps().getAllowedPortals();
    //   CS.forEach(aAllowedPortals, function (oPortalObject, iIndex) {
    //     let oPortal = {
    //       id: oPortalObject.id,
    //       label: oTranslations()[oPortalObject.id.toUpperCase()],
    //       active: false,
    //       icon: "",
    //       disabled: false,
    //       properties : {
    //         key: "portalAction",
    //         isSelected: SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL) === oPortalObject.id
    //       }
    //     };
    //     iSpliceIndex += iIndex;
    //     oUserProfileActionData.splice(iSpliceIndex, 0, oPortal);
    //   });
    // } else {
    //   oUserProfileActionData.splice(1, 1);
    // }

    let sPreferredUILanguage = CS.isNotEmpty(sSelectedPreferredUILanguage)
                               ? sSelectedPreferredUILanguage : CS.isNotEmpty(oComponentProps.getPreferredUILanguage())
                               ? oComponentProps.getPreferredUILanguage() : oLanguageInfo.defaultLanguage;
    let sPreferredDataLanguage = CS.isNotEmpty(sSelectedPreferredDataLanguage)
                                 ? sSelectedPreferredDataLanguage : CS.isNotEmpty(oComponentProps.getPreferredDataLanguage())
                                 ? oComponentProps.getPreferredDataLanguage() : oLanguageInfo.defaultLanguage;

    return {
      isUserInformationViewEnabled: oComponentProps.getShowUserInformationView(),
      isUserLangaugesEnabled: oComponentProps.getShowUserLangaugesView(),
      isChangePasswordEnabled: oComponentProps.getChangePasswordEnabled(),
      isUserInformationDirty: !CS.isEmpty(oCurrentUser.clonedObject),
      isSettingScreenActive: bIsSettingScreenActive,
      errorFields: oComponentProps.getErrorFields(),
      userProfileActionData: oUserProfileActionData,
      languageSelectionData: oComponentProps.getLanguageSelectionData(),
      clonedUser: oClonedUser,
      currentUser: oCurrentUser,
      dataLanguages: oLanguageInfo.dataLanguages,
      uiLanguages: oLanguageInfo.userInterfaceLanguages,
      defaultLanguage: oLanguageInfo.defaultLanguage,
      preferredUILanguage: sPreferredUILanguage,
      preferredDataLanguage: sPreferredDataLanguage,
    }
  };

  getPhysicalCatalogSelectorView = () => {
    let oCurrentUser =this.props.store.getCurrentUser();
    let aAllowedPhysicalCatalogs = this.getComponentProps().getAllowedPhysicalCatalogs();
    let bIsCurrentRoleSystemAdmin = oCurrentUser.roleType !== RoleTypeDictionary.SYSTEM_ADMIN;
    var bShowPhysicalCatalogMenu = this.getComponentProps().getShowPhysicalCatalogMenu() && bIsCurrentRoleSystemAdmin;
    let bIsDisabledPhysicalCatalog = this.getComponentProps().getIsPhysicalCatalogDisabled();
    return (<PhysicalCatalogSelectorView showPhysicalCatalog={bShowPhysicalCatalogMenu}
                                         allowedPhysicalCatalogs={aAllowedPhysicalCatalogs}
                                         disabled={bIsDisabledPhysicalCatalog}
                                         selectedPortal={SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL)}/>);
  };

  getLanguageInfoView = (oSelectedMenu) => {
    let oComponentProps = this.getComponentProps();
    let oLanguageInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = oLanguageInfo.dataLanguages;
    let sSelectedDataLanguageId = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    let aUILanguages = oLanguageInfo.userInterfaceLanguages;
    let sSelectedUILanguageId = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let bIsSettingScreenActive = oSelectedMenu && oSelectedMenu.type !== MenuDictionary['setting'];
    let bIsShowDataLanguageOptionInHeader = bIsSettingScreenActive && !oComponentProps.getIsHideDataLanguageOptions();
    let bIsUIAndDataLanguageSelectionDialogOpen = oComponentProps.getIsUIAndDataLanguageSelectionDialogOpen();
    let isShowUILanguage = !oComponentProps.getIsHideUILanguageOptions();

    return(
        <UIAndDataLanguageInfoView
        dataLanguages={aDataLanguages}
        selectedDataLanguageId={sSelectedDataLanguageId}
        uiLanguages={aUILanguages}
        selectedUILanguageId={sSelectedUILanguageId}
        isShowUILanguage={isShowUILanguage}
        isShowDataLanguage={bIsShowDataLanguageOptionInHeader}
        isDialogOpen={bIsUIAndDataLanguageSelectionDialogOpen}
        />
    );
  };

  getIconUrl =(sKey)=> {
    return RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {type: "Icons", id: sKey}) + "/";
  };

  render() {
    var aMenuList = this.getMenuList();
    let oSelectedMenu = CS.find(aMenuList, function (oMenu) {
      return oMenu.isSelected;
    });
    var module = this.getSelectedModule(oSelectedMenu);
    let sMode = oSelectedMenu ? oSelectedMenu.name : "";
    let oLogoConfig =  this.props.store.getLogoConfig();
    let sApplicationIcon = undefined;
    let sApplicationBrandLogo = undefined;

    if(oLogoConfig){
      if(oLogoConfig.generalHeaderLogo){
        sApplicationIcon = this.getIconUrl(oLogoConfig.generalHeaderLogo);
      }
      if(oLogoConfig.primaryLogoId){
        sApplicationBrandLogo = this.getIconUrl(oLogoConfig.primaryLogoId);
      }
    }

    var oCurrentUser = this.props.store.getCurrentUser();
    var oClonedUser = oCurrentUser.clonedObject ? oCurrentUser.clonedObject : oCurrentUser;
    oCurrentUser = oClonedUser;
    var bAboutDialogVisibility = this.getComponentProps().getAboutDialogVisibility();
    var bShowPhysicalCatalogMenu = this.getComponentProps().getShowPhysicalCatalogMenu() && oCurrentUser.roleType != 'SystemAdmin';
    let oUserProfileData = this.getUserProfileData(oSelectedMenu);

    let aMenuData = [];

    let oLanguageInfoView = this.getLanguageInfoView(oSelectedMenu);
    let aLanguageInfoDOM = [{
      view: (oLanguageInfoView)
    }];
    aMenuData.push(aLanguageInfoDOM);

    /*Group 1 */
    if (bShowPhysicalCatalogMenu) {
      let oPhysicalCatalogView = this.getPhysicalCatalogSelectorView();
      let aPhysicalCatalogDOM = [{
        view: (oPhysicalCatalogView),
      }];

      aMenuData.push(aPhysicalCatalogDOM);
    }

    /*Group 2 */
    let aMainMenuList = [];
    let bIsArchive = SessionProps.getIsArchive();
    if(SessionProps.getSessionPhysicalCatalogId() === PhysicalCatalogDictionary.DATAINTEGRATION) {
      CS.remove(aMenuList, {id: MenuDictionary.Archival})
    }
    CS.forEach(aMenuList, function (oMenu) {
      if (oMenu.isVisible) {
        let oMenuItem ={
          id:oMenu.id,
          menuClassName: oMenu.className,
          label : oTranslations()[oMenu.title],
          containerClassName: "newMenuViewContainer",
        };

        if(bIsArchive && oMenu.id === MenuDictionary.Archival) {
          oMenuItem.menuClassName += " selected ";
        }
        else if (oSelectedMenu.id === oMenu.id && !bIsArchive) {
          oMenuItem.menuClassName += " selected ";
        }
        aMainMenuList.push(oMenuItem);
      }
    });
    if(!CS.isEmpty(aMainMenuList)){
      aMenuData.push(aMainMenuList);
    }

    /*Group 3 */
    let aMenuSection = new MockDataForMenu();
    const bIsNotificationChanged = this.getComponentProps().getIsNotificatioinChanged();
    /** Hidden planned task icon for setting screen  **/
    if (oSelectedMenu.id != MenuDictionary.runtime || oCurrentUser.isReadOnly) {
      let index = CS.findIndex(aMenuSection, {id: MenuDictionary.TASKPLANNED});
      aMenuSection.splice(index, 1);
    } else {
      /** Hidden planned task icon for Data Integration (Inbound and Outbound) and archival screen  **/
      if (SessionProps.getSessionPhysicalCatalogId() === PhysicalCatalogDictionary.DATAINTEGRATION || SessionProps.getIsArchive()) {
        let index = CS.findIndex(aMenuSection, {id: MenuDictionary.TASKPLANNED});
        aMenuSection.splice(index, 1);
      } else {
        const iNotificationCount = this.getComponentProps().getUnreadNotificationCount();
        let oNotificationDomData = CS.find(aMenuSection, {id: MenuDictionary.TASKPLANNED});
        oNotificationDomData.count = iNotificationCount;
      }
    }

    let oUserDOM = {view : <UserProfileActionView userProfileData={oUserProfileData}/>}
    aMenuSection.push(oUserDOM);
    aMenuData.push(aMenuSection);

    return (
      <div className="appContainer" id="appContainer">
        <MenuViewContainer
          key={`${SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL)}:${SessionProps.getSessionPhysicalCatalogId()}`}
          applicationIcon={sApplicationIcon}
          applicationBrandLogo={sApplicationBrandLogo}
          mode={sMode}
          aboutDialogVisible={bAboutDialogVisibility}
          onClickFun={this.handleOnMenuContainerClick}
          MenuData={aMenuData}
        />
        <div className="menuModuleSeparator"></div>
        <div className="moduleContainer">
          <MainScreen notificationCountChanged={bIsNotificationChanged}>{module}</MainScreen>
        </div>
      </div>
    );
  }
}

export default HomeScreenController;
