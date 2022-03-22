import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from '../utils/view-library-utils';
import TooltipView from './../tooltipview/tooltip-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import SessionStorageManager from '../../libraries/sessionstoragemanager/session-storage-manager';
import { view as ImageSimpleView } from './../../viewlibraries/imagesimpleview/image-simple-view';
import { view as CustomDialogView } from '../customdialogview/custom-dialog-view';
import { view as UserProfileView } from './user-profile-view';
import { view as LanguageSelectionView } from '../languageselectionview/language-selection-view';
import { view as ContextMenuCustomView } from './../../viewlibraries/contextmenuwithcustomview/context-menu-custom-view';
import ContextMenuCustomViewModel from './../../viewlibraries/contextmenuwithcustomview/model/context-menu-custom-view-model';
import SessionStorageConstants from '../../commonmodule/tack/session-storage-constants';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';


var oEvents = {
  USER_PROFILE_ACTION_VIEW_USER_MENU_ITEM_CLICKED: "USER_PROFILE_ACTION_VIEW_USER_MENU_ITEM_CLICKED",
  USER_PROFILE_ACTION_VIEW_USER_MENU_HIDE: "USER_PROFILE_ACTION_VIEW_USER_MENU_HIDE",
  HANDLE_PHYSICAL_CATALOG_OR_PORTAL_SELECTION_CHANGED: "handle_physical_catalog_or_portal_selection_changed",
};

const oPropTypes = {
  userProfileData: ReactPropTypes.shape({
    isUserInformationViewEnabled: ReactPropTypes.bool,
    isUserLangaugesEnabled: ReactPropTypes.bool,
    isChangePasswordEnabled: ReactPropTypes.bool,
    isUserInformationDirty: ReactPropTypes.bool,
    isSettingScreenActive: ReactPropTypes.bool,
    errorFields: ReactPropTypes.object,
    userProfileActionData: ReactPropTypes.array,
    languageSelectionData: ReactPropTypes.array,
    clonedUser: ReactPropTypes.object,
    currentUser: ReactPropTypes.object,
  }),
};
/**
 * @class UserInformationActionView - Used to display user icon on top bar, There are Multiple user profile actions which displaying on user icon click.
 * @memberOf Views
 * @property {custom} [userProfileData] - Contains all information about user.(for example: currentUser, isChangePasswordEnable, isSettingScreenActive, isUserInformationDirty,
 * languageSelectionData, userProfileActionData)
 */

// @CS.SafeComponent
class UserInformationActionView extends React.Component {
  constructor (props) {
    super(props);
  }

  handleUserMenuItemClick = (oSelectedItem) => {
    if (oSelectedItem.properties.key == "portalAction") {
      EventBus.dispatch(oEvents.HANDLE_PHYSICAL_CATALOG_OR_PORTAL_SELECTION_CHANGED, "portal", [oSelectedItem.id]);
    } else {
      EventBus.dispatch(oEvents.USER_PROFILE_ACTION_VIEW_USER_MENU_ITEM_CLICKED, oSelectedItem.id);
    }
  };

  handleUserMenuHideOperation = () => {
    EventBus.dispatch(oEvents.USER_PROFILE_ACTION_VIEW_USER_MENU_HIDE);
  };

  getUserControlMenuItemList = (aUserControlMenuItemList) => {
    var aUserControlMenuItemModel = [];
    CS.forEach(aUserControlMenuItemList, function (oUserControlMenuItem) {
      let oProperties = {
        context: 'UserMenuItem',
        disabled: oUserControlMenuItem.disabled,
        customIconClassName : oUserControlMenuItem.menuClassName
      };
      CS.assign(oProperties, oUserControlMenuItem.properties);
      aUserControlMenuItemModel.push(new ContextMenuCustomViewModel(
          oUserControlMenuItem.id,
          CS.getLabelOrCode(oUserControlMenuItem),
          oUserControlMenuItem.active,
          oUserControlMenuItem.icon,
          oProperties
      ));
    });
    return aUserControlMenuItemModel;
  };

  getUserControlMenuItems = () => {
    let oCurrentUser = this.props.userProfileData.currentUser;
    let sUserFullName = oCurrentUser.firstName + ' ' + oCurrentUser.lastName || '';
    let sInitials = oCurrentUser.firstName.substring(0,1).toUpperCase() + oCurrentUser.lastName.substring(0,1).toUpperCase();
    let oImageDom = oCurrentUser.icon ?
        <ImageSimpleView classLabel="userImage" imageSrc={ViewUtils.getIconUrl(oCurrentUser.icon)}/> : <div data-initials ={sInitials}></div>;
    let sUserImageWrapperClassName = oImageDom ? "userImageWrapper " : "userImageWrapper noImage ";
    let oPopoverStyle = {
      boxShadow: "none",
      backgroundColor: "transparent",
      height: "100%"
    };

    let aUserControlMenuItemList = this.getUserControlMenuItemList(this.props.userProfileData.userProfileActionData);
    return (
          <div className="userControl" title={sUserFullName || ''}>
            <ContextMenuCustomView
                anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
                targetOrigin={{horizontal: 'left', vertical: 'top'}}
                className="userViewPopover"
                contextMenuViewModel={aUserControlMenuItemList}
                onClickHandler={this.handleUserMenuItemClick}
                style={oPopoverStyle}
                selectedItems={null}
                showSearch={false}
                isMultiselect={false}
                showSelectedItems={false}
                showCustomIcon={true}
                isMovable={false}
                disabled={false}
                context="userProfilePopover">
              <div className={sUserImageWrapperClassName} title={sUserFullName}>{oImageDom}</div>
            </ContextMenuCustomView>
          </div>
    );
  };

  getUserInformationView = () => {
    let oUserProfileData = this.props.userProfileData;
    let oUserCloned = oUserProfileData.clonedUser;
    let oErrors = oUserProfileData.errorFields;
    let oUserProperties = {};
    oUserProperties.errors = oErrors;
    oUserProperties.changePasswordEnabled = oUserProfileData.isChangePasswordEnabled;
    let oUserModel = {
      id: oUserCloned.id,
      firstName: oUserCloned.firstName,
      lastName: oUserCloned.lastName,
      userName: oUserCloned.userName,
      password: oUserCloned.password,
      confirmPassword: oUserCloned.confirmPassword,
      emailId: oUserCloned.email,
      mobileNumber: oUserCloned.contact,
      gender: oUserCloned.gender,
      userImage: oUserCloned.icon ? ViewUtils.getIconUrl(oUserCloned.icon) : "",
      properties: oUserProperties,
    };
    let sLanguageInUse = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let oSavedDetails = {
      firstName: oUserCloned.firstName,
      lastName: oUserCloned.lastName,
      email: oUserCloned.email,
    };

    let oContentStyle = {
      width: "74%",
      height: "74%",
      maxWidth: "none",
      maxHeight: "none",
      margin: 0,
    };

    let oBodyStyle = {
      padding: '0px',
      overflow: "hidden",
    };

    let oDefaultLanguageData = {
      dataLanguages: oUserProfileData.dataLanguages,
      uiLanguages: oUserProfileData.uiLanguages,
      defaultLanguage: oUserProfileData.defaultLanguage,
      preferredUILanguage: oUserProfileData.preferredUILanguage,
      preferredDataLanguage: oUserProfileData.preferredDataLanguage,
    };

    return (
        <CustomDialogView open={true}
                             bodyStyle={oBodyStyle}
                             contentStyle={oContentStyle}
                             onRequestClose={this.handleUserMenuHideOperation}
                             bodyClassName="userInformationViewModel"
                             contentClassName="userInformationView">
            <UserProfileView model={oUserModel}
                       savedDetails={oSavedDetails}
                       languageInUse={sLanguageInUse}
                       createUser={false}
                       isUserInformationDirty={oUserProfileData.isUserInformationDirty}
                       defaultLanguageData={oDefaultLanguageData}
                       bShowDefaultLanguageSection={true}/>
    </CustomDialogView>);
  };

  getUserLangaugesView = (oLanguageInfo) => {

    let oBodyStyle = {
      padding: '0px',
      overflow: "hidden",
    };

    return <CustomDialogView open={true}
                             bodyStyle={oBodyStyle}
                             onRequestClose={this.handleUserMenuHideOperation}
                             bodyClassName="languageInfoViewModel"
                             contentClassName="languageInfoView"
                             title={getTranslation().CHANGE_OF_THE_LANGUAGE_WARNING_MESSAGE}>
      <LanguageSelectionView languageInfo={oLanguageInfo}/>
    </CustomDialogView>;
  };

  render () {
    let oUserProfileData = this.props.userProfileData;
    let oUserInformationView = oUserProfileData.isUserInformationViewEnabled ?
        this.getUserInformationView() : null;
    let oUserLangaugesView = oUserProfileData.isUserLangaugesEnabled ?
        this.getUserLangaugesView(oUserProfileData.languageSelectionData) : null;
    let oUserControlMenuItems = this.getUserControlMenuItems();

    return (
        <div className="userProfileViewContainer">
          {oUserControlMenuItems}
          {oUserInformationView}
          {oUserLangaugesView}
        </div>
    );
  }
}

UserInformationActionView.propTypes = oPropTypes;

export const view = UserInformationActionView;
export const events = oEvents;
