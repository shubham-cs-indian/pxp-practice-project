import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import TextField from '@material-ui/core/TextField';
import SelectField from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';
import MenuItem from '@material-ui/core/MenuItem';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as oTranslations } from '../../commonmodule/store/helper/translation-manager.js';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import { view as CustomMobileNumberView } from '../custommobilenumberView/custom-mobile-number-view';
import ClickAwayListener from "@material-ui/core/ClickAwayListener/ClickAwayListener";
import ContentUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import {view as CustomPopoverView} from "../customPopoverView/custom-popover-view";

var oEvents = {
  USER_PROFILE_VIEW_HANDLE_CHANGE_PASSWORD_CLICKED: "USER_PROFILE_VIEW_HANDLE_CHANGE_PASSWORD_CLICKED",
  USER_PROFILE_VIEW_HANDLE_UPLOAD_USER_IMAGE_CHANGED: "USER_PROFILE_VIEW_HANDLE_UPLOAD_USER_IMAGE_CHANGED",
  USER_PROFILE_VIEW_HANDLE_USER_DATA_CHANGED: "USER_PROFILE_VIEW_HANDLE_USER_DATA_CHANGED",
  USER_PROFILE_VIEW_HANDLE_USER_DETAILS_SAVE_CLICKED: "USER_PROFILE_VIEW_HANDLE_USER_DETAILS_SAVE_CLICKED",
  USER_PROFILE_VIEW_HANDLE_USER_DETAILS_DISCARD_CLICKED: "USER_PROFILE_VIEW_HANDLE_USER_DETAILS_DISCARD_CLICKED",
  USER_PROFILE_VIEW_HANDLE_USER_CREATE_CLICKED: "user_profile_view_handle_user_create_clicked",
  USER_PROFILE_VIEW_HANDLE_USER_CREATE_CANCEL_CLICKED: "user_profile_view_handle_user_create_cancel_clicked",
  CREATE_USER_PROFILE_VIEW_HANDLE_USER_DATA_CHANGED: "create_user_profile_view_handle_user_data_changed",
  USER_PROFILE_PREFERRED_LANGUAGE_CHANGED: "user_profile_preferred_language_changed",
  USER_PROFILE_VIEW_HANDLE_REMOVE_USER_IMAGE_CLICKED: "user_profile_view_handle_remove_user_image_clicked",
  CREATE_USER_PROFILE_VIEW_HANDLE_USER_IMAGE_UPLOAD_CLICKED: "create_user_profile_view_handle_user_image_upload_clicked",
  CREATE_USER_PROFILE_VIEW_HANDLE_REMOVE_USER_IMAGE_CLICKED: "create_user_profile_view_handle_remove_user_image_clicked",
};

const oPropTypes = {
  model: ReactPropTypes.shape({
    id: ReactPropTypes.string,
    firstName: ReactPropTypes.string,
    lastName: ReactPropTypes.string,
    userName: ReactPropTypes.string,
    password: ReactPropTypes.string,
    confirmPassword: ReactPropTypes.string,
    emailId: ReactPropTypes.string,
    mobileNumber: ReactPropTypes.string,
    gender: ReactPropTypes.string,
    userImage: ReactPropTypes.string,
    properties: ReactPropTypes.object,
  }).isRequired,
  languageInUse: ReactPropTypes.string,
  savedDetails: ReactPropTypes.object,
  createUser: ReactPropTypes.bool,
  isUserInformationDirty: ReactPropTypes.bool,
  defaultLanguageData: ReactPropTypes.object,
  bShowDefaultLanguageSection: ReactPropTypes.bool,
};
/**
 * @class UserProfileView - Used to create or edit user profile.
 * @memberOf Views
 * @property {custom} model - Contains all information about user(for example: firstName, lastName, email, mobileNumber, gender etc)
 * @property {string} [languageInUse] - Contains currently selected UI language.
 * @property {object} [savedDetails] - Contains saved information of user.
 * @property {bool} [createUser] - It shows user is newly created or not.(In profile editing scenario user is not newly created)
 * @property {bool} [isUserInformationDirty] - Showing user information is dirty or not.
 */

// @CS.SafeComponent
class UserProfileView extends React.Component {
  constructor (props) {
    super(props);

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };

    this.userImageUpload = React.createRef();
    this.state = {
      isUILanguageExpanded: false,
      isDataLanguageExpanded: false,
      isChangePictureExpanded: false,
    }
  }

  updateTextInputValues = () => {
    if (this.userImageUpload.current) {
      this.userImageUpload.current.value = null;
    }
  };

  componentDidMount () {
    this.updateTextInputValues();
  }

  componentDidUpdate () {
    this.updateTextInputValues();
  }

  handleUploadUserImage = (oEvent) => {
    let aFiles = oEvent.target.files;
    this.setState({isChangePictureExpanded: false});
    if (this.props.createUser) {
      EventBus.dispatch(oEvents.CREATE_USER_PROFILE_VIEW_HANDLE_USER_IMAGE_UPLOAD_CLICKED, this, oEvent, aFiles);
    } else {
      EventBus.dispatch(oEvents.USER_PROFILE_VIEW_HANDLE_UPLOAD_USER_IMAGE_CHANGED, this, oEvent, aFiles);
    }
  };

  uploadImage = () => {
    this.userImageUpload.current.click();
  };

  handleUserDataChanged = (sContext, oEvent, sValue) => {
    let oModel = this.props.model;
    let sNewValue = !CS.isEmpty(oEvent) ? oEvent.target.value : sValue;
    if (sContext === "gender") {
      sNewValue = sNewValue;
    }
    let bIsCreated = this.props.createUser;

    if (oModel[sContext] !== sNewValue && !bIsCreated) {
      EventBus.dispatch(oEvents.USER_PROFILE_VIEW_HANDLE_USER_DATA_CHANGED, this, oModel, sContext, sNewValue);
    }
    else {
      EventBus.dispatch(oEvents.CREATE_USER_PROFILE_VIEW_HANDLE_USER_DATA_CHANGED, this, oModel, sContext, sNewValue);
    }
  };

  handleSaveUserDetails = (oModel, oEvent) => {
    oEvent.context = "MyView";
      EventBus.dispatch(oEvents.USER_PROFILE_VIEW_HANDLE_USER_DETAILS_SAVE_CLICKED, this, oModel);
  };

  handleDiscardUserDetails() {
    EventBus.dispatch(oEvents.USER_PROFILE_VIEW_HANDLE_USER_DETAILS_DISCARD_CLICKED);
  }

  handleUserCreateClicked() {
    EventBus.dispatch(oEvents.USER_PROFILE_VIEW_HANDLE_USER_CREATE_CLICKED, this.props.model);
  }

  handleUserCreateCancelClicked() {
    EventBus.dispatch(oEvents.USER_PROFILE_VIEW_HANDLE_USER_CREATE_CANCEL_CLICKED, this.props.model);
  }

  handleChangePictureExpandButtonClicked = () => {
    let bIsExpanded = this.state.isChangePictureExpanded;
    this.setState({isChangePictureExpanded: !bIsExpanded});
  };

  handleUILanguageExpandButtonClicked = (oEvent) => {
    let bIsExpanded = this.state.isUILanguageExpanded;
    this.setState({isUILanguageExpanded: !bIsExpanded, moreView: oEvent.currentTarget});
  };

  handleDataLanguageExpandButtonClicked = (oEvent) => {
    let bIsExpanded = this.state.isDataLanguageExpanded;
    this.setState({isDataLanguageExpanded: !bIsExpanded, moreView: oEvent.currentTarget});
  };

  handleRemoveProfilePictureClicked = () => {
    this.setState({isChangePictureExpanded: false});
    if (this.props.createUser) {
      EventBus.dispatch(oEvents.CREATE_USER_PROFILE_VIEW_HANDLE_REMOVE_USER_IMAGE_CLICKED);
    } else {
      EventBus.dispatch(oEvents.USER_PROFILE_VIEW_HANDLE_REMOVE_USER_IMAGE_CLICKED);
    }
  };

  handleSelectDefaultLanguageClicked = (sId, sContext) => {
    EventBus.dispatch(oEvents.USER_PROFILE_PREFERRED_LANGUAGE_CHANGED, sId, sContext);
  };

  handleChangePasswordButtonClicked = (oEvent) => {
    EventBus.dispatch(oEvents.USER_PROFILE_VIEW_HANDLE_CHANGE_PASSWORD_CLICKED, this, oEvent);
  };

  getUserImageView = () => {
    let sDefaultUserImage = "";
    let oModel = this.props.model;
    let sImageSrc = oModel.userImage;
    let oUserImageView = null;
    if (!sImageSrc && (CS.isNotEmpty(oModel.firstName) || CS.isNotEmpty(oModel.lastName))) {
      let sInitials = oModel.firstName.substring(0, 1).toUpperCase() + oModel.lastName.substring(0, 1).toUpperCase();
      oUserImageView = (<div data-initials={sInitials}></div>);
    } else {
      if (!sImageSrc) {
        sDefaultUserImage = 'defaultUserIcon';
      }
      oUserImageView = (<div className="userImageContainer">
        <div className="imageContainer">
          <div className={"imageWrapper " + sDefaultUserImage}>
            {sImageSrc ? (<ImageFitToContainerView imageSrc={sImageSrc}/>) : null}
          </div>
        </div>
      </div>);
    }

    return (
        <div className="userInformationContainerLeftSection">
          {oUserImageView}
          {this.getUserProfileChangePictureView()}
          {this.state.isChangePictureExpanded ? this.getChangePictureExpandedListView() : null}
        </div>);
  };

  getUserProfileChangePictureView = () => {
    return (
        <div className="userInformationChangePictureWrapper" onClick={this.handleChangePictureExpandButtonClicked}>
          <div className="userInformationChangePictureIcon"></div>
          <span className="userInformationChangePictureLabel">{oTranslations().CHANGE_PICTURE}</span>
          {this.getExpandButtonView("leftSectionExpandButton", "changePic")}
        </div>
    )
  };

  getExpandButtonView = (sClassName, sContext = "", bIsExpandButtonDisabled = false) => {
    if ((this.state.isChangePictureExpanded && sContext == "changePic") || (this.state.isDataLanguageExpanded && sContext == "dataLang")
        || (this.state.isUILanguageExpanded && sContext == "uiLang")) {
      sClassName += " expanded";
    }
    sClassName += bIsExpandButtonDisabled ? " disabled" : "";

    return (<div className={sClassName}></div>);
  };

  getUserInfoChangePicListItems = () => {
    return [{
      id: "uploadPicture",
      isDisabled: false,
      label: oTranslations().UPLOAD_PICTURE,
    }, {
      id: "removePicture",
      isDisabled: false,
      label: oTranslations().REMOVE_PICTURE,
    }];
  };

  getChangePictureExpandedListView = () => {
    let _this = this;
    let aListItemView = [];
    let aListItems = this.getUserInfoChangePicListItems();
    CS.forEach(aListItems, function (oListItem) {
      aListItemView.push(_this.getChangePictureExpandedListItemView(oListItem))
    });

    return (
        <ClickAwayListener onClickAway={this.handleChangePictureExpandButtonClicked}>
          <div className="userInfoChangePicListItemContainer">{aListItemView}</div>
        </ClickAwayListener>
    );
  };

  getDefaultLanguageListView = (aListItems, sContext, iWidth) => {
    let _this = this;
    let aListItemsView = [];
    CS.forEach(aListItems, function (oListItem) {
      aListItemsView.push(_this.getDefaultLanguageListItemView(oListItem, sContext, iWidth));
    });

    return (
        <CustomPopoverView
            className="popover-root"
            style={{marginTop: "2px"}}
            open={sContext == "uiLanguage" ? _this.state.isUILanguageExpanded : _this.state.isDataLanguageExpanded}
            anchorEl={_this.state.moreView}
            anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
            transformOrigin={{horizontal: 'left', vertical: 'top'}}
            onClose={sContext == "uiLanguage" ? _this.handleUILanguageExpandButtonClicked : _this.handleDataLanguageExpandButtonClicked}>
          <div className={"userInfoDefaultLanguageListItemContainer"}>
            {aListItemsView}
          </div>
        </CustomPopoverView>
    )
  };

  getChangePictureExpandedListItemView = (oListItem) => {
    let fOnClickHandler;
    let oUploadPicView = null;

    switch (oListItem.id) {
      case "uploadPicture":
        fOnClickHandler = this.uploadImage;
        oUploadPicView = (
            <input style={{"visibility": "hidden", "width": "0", "height": "0"}}
                   ref={this.userImageUpload}
                   onChange={this.handleUploadUserImage}
                   type="file"
                   accept="image/*"
                   multiple={false}/>
        );
        break;
      case "removePicture":
        fOnClickHandler = this.handleRemoveProfilePictureClicked;
        break;
    }

    return (
        <div className="userInfoChangePicListItem" onClick={fOnClickHandler}>
          <div className={"userInfoChangePicListItemIcon " + oListItem.id}></div>
          <div className="userInfoChangePicListItemLabel">{oListItem.label}</div>
          {oUploadPicView}
        </div>
    )
  };

  getDefaultLanguageListItemView = function (oListItem, sContext, iWidth) {
    let sImageSrc = "";
    let oDefaultLanguageData = this.props.defaultLanguageData;
    let sPreferredUILanguage = oDefaultLanguageData.preferredUILanguage;
    let sPreferredDataLanguage = oDefaultLanguageData.preferredDataLanguage;
    let sSelectedItemClassName = "userInfoDefaultLanguageListItemSelection";
    let sListItemIconClassName = "userInfoDefaultLanguageListItemIcon";
    if (CS.isNotEmpty(oListItem.iconKey)) {
      sListItemIconClassName += " selectedIcon";
      sImageSrc = ContentUtils.getIconUrl(oListItem.iconKey);
    } else {
      sListItemIconClassName += " default";
    }
    if ((sContext == "uiLanguage" && oListItem.id == sPreferredUILanguage) ||
        (sContext == "dataLanguage" && oListItem.id == sPreferredDataLanguage)) {
      sSelectedItemClassName += " selected";
    }

    return (
        <div className={"userInfoDefaultLanguageListItem"} style={{"width" : iWidth + "px"}} onClick={this.handleSelectDefaultLanguageClicked.bind(this, oListItem.id, sContext)}>
          <div className={sListItemIconClassName}>
            {sImageSrc ? (<ImageFitToContainerView imageSrc={sImageSrc}/>) : null}
          </div>
          <div className="userInfoDefaultLanguageListItemLabel">{oListItem.label}</div>
          <div className={sSelectedItemClassName}></div>
        </div>
    )
  };

  getIconView = (sIconKey) => {
    let sIconSrc = ContentUtils.getIconUrl(sIconKey);
    return (
        <ImageFitToContainerView imageSrc={sIconSrc}/>
    )
  };

  getDefaultLanguagesView = () => {
    let iUIListContainerWidth = 0;
    let iDataListContainerWidth = 0;
    let oDefaultLanguageData = this.props.defaultLanguageData;
    let sSelectedUILanguageIconClassName = "selectedUILanguageIcon";
    let sSelectedDataLanguageIconClassName = "selectedDataLanguageIcon";
    let oSelectedUILanguageData = CS.find(oDefaultLanguageData.uiLanguages, {id: oDefaultLanguageData.preferredUILanguage});
    let oSelectedDataLanguageData = CS.find(oDefaultLanguageData.dataLanguages, {id: oDefaultLanguageData.preferredDataLanguage});
    if (CS.isEmpty(oSelectedUILanguageData)) {
      oSelectedUILanguageData = CS.find(oDefaultLanguageData.uiLanguages, {id: oDefaultLanguageData.defaultLanguage});
    }
    if (CS.isEmpty(oSelectedDataLanguageData)) {
      oSelectedDataLanguageData = CS.find(oDefaultLanguageData.dataLanguages, {id: oDefaultLanguageData.defaultLanguage});
    }

    sSelectedUILanguageIconClassName += CS.isEmpty(oSelectedUILanguageData.iconKey) ? " default" : "";
    sSelectedDataLanguageIconClassName += CS.isEmpty(oSelectedDataLanguageData.iconKey) ? " default" : "";

    if (CS.isNotEmpty(this.selectedUILanguageWrapper)) {
      iUIListContainerWidth = this.selectedUILanguageWrapper.offsetWidth;
    }
    if (CS.isNotEmpty(this.selectedDataLanguageWrapper)) {
      iDataListContainerWidth = this.selectedDataLanguageWrapper.offsetWidth;
    }

    return (
        <div className="userInfoDefaultLanguagesContainer">
          <div className="defaultLanguagesHeader">{oTranslations().DEFAULT_LANGUAGES}</div>
          <div className="userInfoDefaultLanguagesWrapper">
            <div className="selectedUILanguageContainer">
              <div className="uiLanguageLabel">{oTranslations().UI_LANGUAGE}</div>
              <div ref={this.setRef.bind(this,"selectedUILanguageWrapper")} className="selectedUILanguageWrapper" onClick={this.handleUILanguageExpandButtonClicked}>
                <div className={sSelectedUILanguageIconClassName}>
                  {CS.isNotEmpty(oSelectedUILanguageData.iconKey) ? this.getIconView(oSelectedUILanguageData.iconKey) : null}
                </div>
                <div className="selectedUILanguageLabel">{oSelectedUILanguageData.label}</div>
                {this.getExpandButtonView("rightSectionExpandButton", "uiLang")}
                {this.state.isUILanguageExpanded ? this.getDefaultLanguageListView(oDefaultLanguageData.uiLanguages, "uiLanguage", iUIListContainerWidth) : null}
              </div>
            </div>
            <div className="selectedDataLanguageContainer">
              <div className="dataLanguageLabel">{oTranslations().DATA_LANGUAGE}</div>
              <div ref={this.setRef.bind(this,"selectedDataLanguageWrapper")} className="selectedDataLanguageWrapper" onClick={this.handleDataLanguageExpandButtonClicked}>
                <div className={sSelectedDataLanguageIconClassName}>
                  {CS.isNotEmpty(oSelectedDataLanguageData.iconKey) ? this.getIconView(oSelectedDataLanguageData.iconKey) : null}
                </div>
                <div className="selectedDataLanguageLabel">{oSelectedDataLanguageData.label}</div>
                {this.getExpandButtonView("rightSectionExpandButton", "dataLang")}
                {this.state.isDataLanguageExpanded ? this.getDefaultLanguageListView(oDefaultLanguageData.dataLanguages, "dataLanguage", iDataListContainerWidth) : null}
              </div>
            </div>
          </div>
          <div className="defaultLanguageNotificationContainer">
            <div className="defaultLanguageNotificationIcon"></div>
            <div className="defaultLanguageNotificationLabel" title={oTranslations().USER_PROFILE_NOTIFICATION}>
              {oTranslations().USER_PROFILE_NOTIFICATION}
            </div>
          </div>
        </div>
    )
  };

  getTextFieldView = (sKey, sTranslatedLabel, sDefaultValue, sType, bIsDisabled, fOnKeyDown) => {

    sType = sType ? sType : "text";
    bIsDisabled = !!bIsDisabled;

    let sMandatoryField = (this.props.createUser && sKey !== "mobileNumber" && sKey !== "password" && sKey !== "confirmPassword") ? " mandatoryFieldAsterisk " : null;

    let oFloatingLabelStyle = {
      color: "#A1A1A1",
      fontWeight: 500
    };

    let oUnderlineStyle = {
      borderBottom: "1px solid rgb(224, 224, 224)"
    };

    let oInputDOM = <TextField id={sKey}
                               ref={sKey}
                               key={sKey}
                               disabled={bIsDisabled}
                               className={sMandatoryField}
                               type={sType}
                               defaultValue={sDefaultValue}
                               floatingLabelStyle={oFloatingLabelStyle}
                               label={sTranslatedLabel}
                               onKeyDown={fOnKeyDown}
                               onBlur={this.handleUserDataChanged.bind(this, sKey)}
                               /*errorText={sKey === "email" && this.props.createUser ? oTranslations().EMAIL_VALIDATE : null}*/
                               underlineStyle={sKey === "email" && this.props.createUser ? oUnderlineStyle : null}
                               autoComplete={"off"}
                               fullWidth={this.props.createUser}/>;

    let oView = <div className="userInputFieldContainer" key={sKey}>
      {oInputDOM}
    </div>;
    return (oView)
  };

  getGenderFieldView = () => {

    var sMale = oTranslations().MALE;
    var sFemale = oTranslations().FEMALE;
    var sOther = oTranslations().OTHER;

    let oFloatingLabelStyle = {
      color: "#A1A1A1",
      fontWeight: 500
    };

    return (
        <div className="userInputFieldContainer">
          <FormControl>
            <InputLabel htmlFor="name-disabled">{oTranslations().GENDER}</InputLabel>
            <SelectField
                /*floatingLabelStyle={oFloatingLabelStyle}
                floatingLabelText={sLabel}
                fullWidth={this.props.createUser}*/
                value={this.props.model.gender || "other"}
                onChange={this.handleUserDataChanged.bind(this, 'gender')}>
              <MenuItem value="male" primaryText={sMale}>{sMale}</MenuItem>
              <MenuItem value="female" primaryText={sFemale}>{sFemale}</MenuItem>
              <MenuItem value="other" primaryText={sOther}>{sOther}</MenuItem>
            </SelectField>
          </FormControl>
        </div>
    )
  };

  getButtonView () {
    let oButtonDom = [];
    if (this.props.createUser) {
      oButtonDom.push(
          <CustomMaterialButtonView
              key="cancel"
              label={oTranslations().CANCEL}
              isRaisedButton={false}
              isDisabled={false}
              onButtonClick={this.handleUserCreateCancelClicked.bind(this, this.props.model)}/>
      );
      oButtonDom.push(
          <CustomMaterialButtonView
              key="create"
              label={oTranslations().CREATE}
              isRaisedButton={true}
              isDisabled={false}
              onButtonClick={this.handleUserCreateClicked.bind(this, this.props.model)}/>
      );
    } else {
      oButtonDom.push(
          <CustomMaterialButtonView
              key="discard"
              label={oTranslations().DISCARD}
              isRaisedButton={false}
              isDisabled={false}
              // style={oStyle}
              onButtonClick={this.handleDiscardUserDetails}
          />
      );
      oButtonDom.push(
          <CustomMaterialButtonView
              key="save"
              label={oTranslations().SAVE}
              isRaisedButton={true}
              isDisabled={false}
              onButtonClick={this.handleSaveUserDetails.bind(this, this.props.model)}/>
      );
    }

    return oButtonDom;
  }

  getPasswordInputView = function (oModel) {
    return (
        <div className={"userDataInnerContainerRow "}>
          {this.getTextFieldView('password', oTranslations().PASSWORD, oModel.password, "password")}
          {this.getTextFieldView('confirmPassword', oTranslations().CONFIRM_PASSWORD, oModel.confirmPassword, "password")}
        </div>);
  };

  getChangePasswordView = function () {
    return (
    <div className="changePasswordContainer">
      <div key="btnChangePassword" className="btnChangePassword" onClick={this.handleChangePasswordButtonClicked}>{oTranslations().CHANGE_PASSWORD}</div>
      <div className="changePWDButtonIcon" onClick={this.handleChangePasswordButtonClicked} ></div>
    </div>);
  };

  render () {
    let oModel = this.props.model;
    let oErrorStyle = {};
    oErrorStyle.height = '0px';
    oErrorStyle.width = '0px';
    let sFirstName = oTranslations().FIRST_NAME;
    let sLastName = oTranslations().LAST_NAME;
    let sUsername = oTranslations().USER_NAME;
    let sEmail = oTranslations().EMAIL;
    let sMobile = oTranslations().MOBILE_NUMBER;
    let bChangePasswordEnabled = oModel.properties['changePasswordEnabled'];

    let oFirstName = this.getTextFieldView('firstName', sFirstName, oModel.firstName, "text", false);
    let oLastName = this.getTextFieldView('lastName', sLastName, oModel.lastName, "text", false);
    let oUserName = this.getTextFieldView('userName', sUsername, oModel.userName, "text", !this.props.createUser);
    let oEmail = this.getTextFieldView('email', sEmail, oModel.emailId, "text", false);
    let oGender = this.getGenderFieldView();
    let oRightSectionStyle = bChangePasswordEnabled ? {height: "calc(100% - 43px)"} : {height: "calc(100% - 93px)"};

    let oMobile = <CustomMobileNumberView defaultValue={oModel.mobileNumber}
                                          label={oTranslations().MOBILE_NUMBER}
                                          onBlurHandler={this.handleUserDataChanged.bind(this, 'mobileNumber', {})}
                                          doNotValidateTheNumber={true}
    />;

    return (
        <div className="userInfoContainer">
          {this.getUserImageView()}
          <div className="userInformationContainerRightSection">
            <div className="userInformationContainerRightSectionBody" style={oRightSectionStyle}>
              {this.props.bShowDefaultLanguageSection ? this.getDefaultLanguagesView() : null}
              <div className="userInfoPersonalDetailsHeader">{oTranslations().PERSONAL_DETAILS}</div>
              <div className="userDataInnerContainer">
                <div className="userDataInnerContainerRow">{oFirstName}{oLastName}</div>
                <div className="userDataInnerContainerRow">{oUserName}{oEmail}</div>
                <div className="userDataInnerContainerRow">{oMobile}{oGender}</div>
                {bChangePasswordEnabled ? this.getPasswordInputView(oModel) : null}
              </div>
            </div>
            {!bChangePasswordEnabled ? this.getChangePasswordView() : null}
            <div className="userInformationContainerRightSectionFooter">
              <div className="userInformationRightSectionFooterButtons">
                {this.getButtonView()}
              </div>
            </div>
          </div>
        </div>
    );
  }
}

UserProfileView.propTypes = oPropTypes;

export const view = UserProfileView;
export const events = oEvents;
