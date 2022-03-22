/**
 * Created by DEV on 23-12-2015.
 */

import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import { UserRequestMapping as oUserRequestMapping } from '../../tack/setting-screen-request-mapping';
import UserProps from './../model/user-config-view-props';
import SettingUtils from './../helper/setting-utils';
import UserConfigGridViewSkeleton from '../../tack/user-config-grid-view-skeleton';
import GridViewContexts from '../../../../../../commonmodule/tack/grid-view-contexts';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import assetTypes from '../../tack/coverflow-asset-type-list';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';

var UserStore = (function () {

  var _triggerChange = function () {
    UserStore.trigger('user-changed');
  };

  /** Server Callbacks * */
  var successFetchUserListCallback = function (oResponse) {
    SettingUtils.getAppData().setUserList(oResponse.success);
    // _setUserValueList(oResponse.success, UserProps.getUserValuesList());
    _triggerChange();
  };

  var failureFetchUserListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureFetchUserListCallback', getTranslation());
  };

  var successSavePasswordCallback = function (oResponse) {
    var oUserFromServer = oResponse.success;
    oUserFromServer.password = "";
    UserProps.setChangePasswordEnabled(false);
    alertify.success(getTranslation().PASSWORD_SUCCESSFULLY_SAVED);
    _triggerChange();
  };

  var failureSavePasswordCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureSavePasswordCallback', getTranslation());
  };

  let _handleDeleteUsersFailure = function (aFailureIds) {
    var aUsersAlreadyDeleted = [];
    var aUnhandleUsers = [];
    var aUserGridData = UserProps.getUserGridData();
    CS.forEach(aFailureIds, function (oItem) {
      var oEvent = CS.find(aUserGridData, {id: oItem.itemId});
      if (oItem.key == "UserNotFoundException") {
        aUsersAlreadyDeleted.push(oEvent.label);
      } else {
        aUnhandleUsers.push(oEvent.label);
      }
    });

    if (aUsersAlreadyDeleted.length > 0) {
      let sUsersAlreadyDeleted = aUsersAlreadyDeleted.join(',');
      alertify.error(Exception.getCustomMessage("User_already_deleted", getTranslation(), sUsersAlreadyDeleted), 0);
    }
    if (aUnhandleUsers.length > 0) {
      let sUnhandleUsers = aUnhandleUsers.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_User", getTranslation(), sUnhandleUsers), 0);
    }
  };

  let successDeleteUsersCallback = function (oResponse) {
    let aSuccessIds = oResponse.success;
    let aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.USER);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oMasterUserMap = SettingUtils.getAppData().getUserList();
    let oGridViewSkeleton = oGridViewPropsByContext.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      CS.pull(oGridViewSkeleton.selectedContentIds, sId);
      delete oMasterUserMap[sId];
    });
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - aSuccessIds.length);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().USERS}));

    if (aFailureIds && aFailureIds.length > 0) {
      _handleDeleteUsersFailure(aFailureIds);
    }
  };

  var failureDeleteUsersCallback = function (oCallback, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      let configError = CS.reduce(oResponse.failure.exceptionDetails, (isConfigError, error) => {
        if (error.key === "EntityConfigurationDependencyException") {
          isConfigError = true;
        }
        return isConfigError;
      }, false);
      if (configError) {
        if (oCallback && oCallback.functionToExecute) {
          oCallback.functionToExecute();
          return;
        }
      }
      let aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
      _handleDeleteUsersFailure(aFailureIds);
    } else {
      SettingUtils.failureCallback(oResponse, 'failureDeleteUsersCallback', getTranslation());
    }
    _triggerChange();
  };

  var _emptyUserErrors = function () {
    var oErrors = UserProps.getErrorFields();
    if(!CS.isEmpty(oErrors)) {
      UserProps.emptyErroFields();
    }
  };

  /** Private API's ** */

  var _setSelectedUser = function (oUser) {
    UserProps.setSelectedUser(oUser);
  };

  /** Unused function */
  var _fetchUserList = function () {
    SettingUtils.csGetRequest(oUserRequestMapping.getAll, {}, successFetchUserListCallback, failureFetchUserListCallback);
  };

  var _getDefaultUserObject = function () {
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      firstName: '',
      lastName: '',
      userName: '',
      password: '',
      confirmPassword: '',
      birthDate: new Date(0),
      icon: '',
      type: 'com.cs.config.interactor.entity.User',
      gender: 'male',
      email: '',
      contact: '',
      isCreated: true,
      code: ""
    }
  };

  var _getSelectedUser = function () {
    return UserProps.getSelectedUser();
  };


/*
  var _checkForBlankUserPassword = function (oUser) {
    return !(CS.isEmpty(oUser.password) || CS.isEmpty(oUser.confirmPassword));
  };
*/

  let _showErrors = function (oErrors) {
    let aErrors = [];
    let iCount = 1;
    CS.forEach(oErrors, function (sError) {
      aErrors.push(iCount+ ". " +sError);
      iCount++;
    });
    !CS.isEmpty(aErrors) && alertify.error(aErrors, 0);
  };

  var _deleteUsers = function (aBulkDeleteList, oCallBack) {
    if (!CS.isEmpty(aBulkDeleteList)) {
      return SettingUtils.csDeleteRequest(oUserRequestMapping.bulkDelete, {}, {ids: aBulkDeleteList}, successDeleteUsersCallback, failureDeleteUsersCallback.bind(this, oCallBack));
    } else {
      _emptyUserErrors();
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().USERS}));
      _triggerChange();
    }
  };

  var _checkUsernameAvailability = function (oActiveUser, sOldUserName, bIsGrid) {
    oActiveUser = CS.omit(oActiveUser, ['confirmPassword', 'password']);
    SettingUtils.csPostRequest(oUserRequestMapping.checkUserAvailability, {}, oActiveUser,
        successCheckUserAvailabilityCallback.bind(this, oActiveUser.id, bIsGrid), failureCheckUserAvailabilityCallback.bind(this, sOldUserName, oActiveUser.id));
  };

  var successCheckUserAvailabilityCallback = function (sUserId, bIsGrid, oResponse) {
    var oErrors = UserProps.getErrorFields();
    delete oErrors['userName'];
    if (bIsGrid) {
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.USER);
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
      let oUserFromGrid = CS.find(aGridViewData, {id: sUserId});
      oUserFromGrid.isDirty = true;
      oGridViewPropsByContext.setIsGridDataDirty(true);
    }
    _triggerChange();
  };

  var failureCheckUserAvailabilityCallback = function (sOldUserName, sUserId, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var oErrors = UserProps.getErrorFields();
      oErrors['userName'] = getTranslation().USER_ERROR_MESSAGE;
      _showErrors(oErrors);
      let oSelectedUser = _getSelectedUser();
      oSelectedUser.userName = sOldUserName;
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TEMPLATE);
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
      let oUserFromGrid = CS.find(aGridViewData, {id: sUserId});
      if (!CS.isEmpty(oUserFromGrid)) {
        oUserFromGrid.properties['userName'].value = sOldUserName;
      }
    } else {
      SettingUtils.failureCallback(oResponse, 'failureCheckUserAvailabilityCallback', getTranslation());
    }
  };

  /**
	 * **************************** GRID VIEW FUNCTIONS
	 * *****************************
	 */

  /*let _getUserMSSModel = function (aSelectedItems) {
   return {
     label: "",
     disabled: false,
     items: [
       {
         id: "male",
         label: getTranslation().MALE
       },
       {
         id: "female",
         label: getTranslation().FEMALE
       },
       {
         id: "other",
         label: getTranslation().OTHER
       }
     ],
     selectedItems: aSelectedItems,
     singleSelect: true,
     context: "users"
   };
  };


  let _preProcessUserDataForGridView = function (aUserList) {
    let oGridSkeleton = {}//SettingScreenProps.screen.getGridViewSkeleton();
    let aGridViewData = [];

    CS.forEach(aUserList, function (oUser) {
      let oProcessedUser = {};
      oProcessedUser.id = oUser.id;
      oProcessedUser.isExpanded = false;
      oProcessedUser.children = [];
      oProcessedUser.actionItemsToShow = ["manageEntity","edit","delete"];
      oProcessedUser.properties = {};
      oProcessedUser.isBackgroundUser = oUser.isBackgroundUser;
      CS.forEach(oGridSkeleton.fixedColumns, function (oColumn) {
        if (oUser.hasOwnProperty(oColumn.id)) {
          let oProperties = {
            value: oUser[oColumn.id]
          };
          switch (oColumn.id) {
            case "icon":
              oProperties.limitImageSize = false;
              break;

            case "userName":
              oProperties.bIsMultiLine = false;
              oProperties.showTooltip = true;
              oProperties.isDisabled = true;
              break;
          }
          oProcessedUser.properties[oColumn.id] = oProperties
        }
      });
      CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
        switch (oColumn.id) {
          case "firstName":
          case "lastName":
            if (oUser.hasOwnProperty(oColumn.id)) {
              oProcessedUser.properties[oColumn.id] = {
                value: oUser[oColumn.id]
              };
              if (oProcessedUser.isBackgroundUser) {
                oProcessedUser.properties[oColumn.id].isDisabled = true;
              }
            }
            break;
          case "email":
            if (oUser.hasOwnProperty(oColumn.id)) {
              oProcessedUser.properties[oColumn.id] = {
                value: oUser[oColumn.id]
              };
            }
            break;
          case "contact":
            if (oUser.hasOwnProperty(oColumn.id)) {
              oProcessedUser.properties[oColumn.id] = {
                value: oUser[oColumn.id]
              };
              oProcessedUser.properties[oColumn.id].rendererType = oGridViewPropertyTypes.NUMBER;
            }
            break;
          case "gender":
            let aSelectedUserGender = oUser.gender && [oUser.gender];
            oProcessedUser.properties[oColumn.id] = _getUserMSSModel(aSelectedUserGender);
            oProcessedUser.properties[oColumn.id].rendererType = oGridViewPropertyTypes.DROP_DOWN;
            oProcessedUser.properties[oColumn.id].value = oProcessedUser.properties[oColumn.id].selectedItems;
            if (oProcessedUser.isBackgroundUser) {
              oProcessedUser.properties[oColumn.id].isDisabled = true;
            }
            break;

          case "roleId":
            let oReferencedRoles = UserProps.getReferencedRoles();
            let sRoleId = oUser[oColumn.id];
            let oReferencedRole = oReferencedRoles && oReferencedRoles[sRoleId] || null;
            if (oUser.hasOwnProperty(oColumn.id)) {
              oProcessedUser.properties[oColumn.id] = {
                value: CS.isNotEmpty(oReferencedRole) && CS.getLabelOrCode(oReferencedRole) || "",
                isDisabled: true
              };
            }
            break;

          case "isEmailLog":
            if (oUser.hasOwnProperty(oColumn.id)) {
              oProcessedUser.properties[oColumn.id] = {
                value: oUser.isEmailLog || false
              };
            }
            break;

        }
      });
      aGridViewData.push(oProcessedUser);
    });


    return aGridViewData;
  };
*/

  let _fetchUserListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.USER);
    SettingUtils.csPostRequest(oUserRequestMapping.Grid, {}, oPostData, successGetAllUsersGrid, failureGetAllUsersGrid);
  };

  let successGetAllUsersGrid = function (oResponse) {
    let oSuccess = oResponse.success;
    let aUserList = oSuccess.usersList;
    let oReferencedRoles = oSuccess.referencedRoles;
    UserProps.setReferencedRoles(oReferencedRoles);
    //let aProcessedGridViewData = _preProcessUserDataForGridView(aUserList);
    GridViewStore.preProcessDataForGridView(GridViewContexts.USER, aUserList, oResponse.success.count);
    SettingUtils.getAppData().setUserList(aUserList);
    UserProps.setUserGridData(aUserList);
    //SettingScreenProps.screen.setGridViewContext(GridViewContexts.USER);
    _triggerChange();
  };

  let failureGetAllUsersGrid = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGetAllEventsGrid", getTranslation());
  };

  let _getAllUsers = function (bIsTreeItemClicked) {
    let oUserConfigGridViewSkeleton = new UserConfigGridViewSkeleton();
    /*SettingScreenProps.screen.setGridViewSkeleton(oUserConfigGridViewSkeleton);

    if (bIsTreeItemClicked) {
      SettingScreenProps.screen.setGridViewPaginationData({
        from: 0,
        pageSize: 20
      });
      SettingScreenProps.screen.setGridViewSortBy('userName');
      SettingScreenProps.screen.setGridViewSearchBy('userName');
      SettingScreenProps.screen.setGridViewSortOrder('asc');
      SettingScreenProps.screen.setGridViewSearchText('');
    }*/
    let oData = {
      skeleton: oUserConfigGridViewSkeleton,
      sortBy: 'userName',
      searchBy: 'userName',
    };
    GridViewStore.createGridViewPropsByContext(GridViewContexts.USER, oData);
    _fetchUserListForGridView();
  };

  let _safeToSave = function (aUserListToSave) {
    var bSafeToSave = true;

    if (CS.isEmpty(aUserListToSave)) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return false;
    }

    CS.forEach(aUserListToSave, function (oUser) {
      CS.forOwn(oUser, function (oProperty, sPropertyId) {
        switch (sPropertyId) {
          case "userName":
            if (CS.trim(oUser.userName) === "") {
              bSafeToSave = false;
              alertify.error(getTranslation().ERROR_USERNAME_NOT_EMPTY, 0);
              return false;
            }
            break;
          case "lastName":
          case "firstName":
            if (CS.trim(oUser.lastName) === "" || CS.trim(oUser.firstName) === "") {
              bSafeToSave = false;
              alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
              return false;
            }
            break;
          case "emailId":
            if (!CS.isEmailValid(oUser.emailId)) {
              bSafeToSave = false;
              alertify.error(getTranslation().ERROR_PLEASE_ENTER_EMAIL, 0);
              return false;
            }
            break;
        }
      });
      /* Breaking out of loop in case of validation on bulk save,
      If any one of validation is false no need to continue the loop */
      if (!bSafeToSave) {
        return false;
      }
    });

    return bSafeToSave;
  };

  let _saveUsersInBulk = function (aUserListToSave, oCallbackData) {
    if(_safeToSave(aUserListToSave)) {
      SettingUtils.csPostRequest(oUserRequestMapping.save, {}, aUserListToSave, successSaveUsersInBulk.bind(this, oCallbackData), failureSaveUsersInBulk);
    }
  };

  let successSaveUsersInBulk = function (oCallbackData, oResponse) {
    let oSuccess = oResponse.success;
    let aUsersList = oSuccess.usersList;
    UserProps.setReferencedRoles(oSuccess.referencedRoles);
    //let aProcessedGridViewData = _preProcessUserDataForGridView(aUsersList);
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.USER, aUsersList);
    let aUserGridData = UserProps.getUserGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.USER);

    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oMasterUsersMap = SettingUtils.getAppData().getUserList();

    CS.forEach(aUsersList, function (oUser) {
      var sUserId = oUser.id;
      var iIndex = CS.findIndex(aUserGridData, {id: sUserId});
      aUserGridData[iIndex] = oUser;
      oMasterUsersMap[sUserId] = oUser;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedUser) {
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedUser.id});
      aGridViewData[iIndex] = oProcessedUser;
    });

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().USER}));
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let failureSaveUsersInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveUsersInBulk", getTranslation());
  };

  let _postProcessUserListAndSave = function (oCallbackData) {
    var aUsersGridData = UserProps.getUserGridData();
    var aUserListToSave = [];
    GridViewStore.processGridDataToSave(aUserListToSave, GridViewContexts.USER, aUsersGridData);

    CS.forEach(aUserListToSave, function (oUserToSave) {
      oUserToSave.label = oUserToSave.firstName + " " + oUserToSave.lastName;
    });

    _saveUsersInBulk(aUserListToSave, oCallbackData);
  };

  let _discardUserGridViewChanges = function (oCallbackData) {
    let aUsersGridData = UserProps.getUserGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.USER);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedUser, iIndex) {
      if (oOldProcessedUser.isDirty) {
        var oUser = CS.find(aUsersGridData, {id: oOldProcessedUser.id});
        //aGridViewData[iIndex] = _preProcessUserDataForGridView([oUser])[0];
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.USER, [oUser])[0];
        bShowDiscardMessage = true;
      }
    });

    bShowDiscardMessage && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let _setShowChangePasswordDialog = function (bStatus, sUserId) {
    let oUserMap = SettingUtils.getAppData().getUserList();
    _setSelectedUser(oUserMap[sUserId]);
    UserProps.setChangePasswordEnabled(bStatus);
  };

  let _createCall = function (oUserToSave, errors) {
    let oUserMap = SettingUtils.getAppData().getUserList();
    let oMasterUser = oUserMap[oUserToSave.id];
    let sPassword = oUserToSave.password;
    let sUsername = oUserToSave.userName;
    let aErrors = [];

    if (CS.isEmpty(oUserToSave.firstName) || CS.trim(oUserToSave.firstName) === "") {
      aErrors.push(getTranslation().FIRST_NAME_NOT_EMPTY);
    }

    if (CS.isEmpty(oUserToSave.lastName) || CS.trim(oUserToSave.lastName) === "") {
      aErrors.push(getTranslation().LAST_NAME_NOT_EMPTY);
    }

    if (!CS.isEmpty(errors.userName)) aErrors.push(errors.userName);
    else if (CS.isEmpty(oUserToSave.userName)) {
      aErrors.push(getTranslation().ERROR_USERNAME_NOT_EMPTY);
    }
   /*
	 * if (!_checkForBlankUserPassword(oUserToSave)) {
	 * aErrors.push(getTranslation().ERROR_PASSWORD_NOT_EMPTY); }
	 */

    if (oUserToSave.password !== oUserToSave.confirmPassword) {
      aErrors.push(getTranslation().ERROR_PASSWORD_AND_CONFIRM_PASSWORD);
    }

    if (CS.isEmpty(oUserToSave.emailId)) {
      aErrors.push(getTranslation().ERROR_PLEASE_ENTER_EMAIL);
    } else if (!CS.isEmailValid(oUserToSave.emailId)) {
      aErrors.push(getTranslation().EMAIL_VALIDATE);
    }

    if (!CS.isMobileNumberValid(oUserToSave.mobileNumber)) {
      aErrors.push(getTranslation().PLEASE_ENTER_VALID_MOBILE_NUMBER);
    }

    if (aErrors.length > 0) {
      _showErrors(aErrors);
      return;
    }

    sPassword = CS.isNotEmpty(sPassword) ? btoa(sUsername + "::" + sPassword) : "";
    oUserToSave.password = sPassword;

    oUserToSave.email = oUserToSave.emailId;
    oUserToSave.type = oMasterUser.type;
    oUserToSave.icon = oMasterUser.icon;
    oUserToSave.contact = oUserToSave.mobileNumber;
    oUserToSave.label = oUserToSave.firstName + " " + oUserToSave.lastName;
    delete oUserToSave.emailId;
    delete oUserToSave.mobileNumber;
    delete oUserToSave.confirmPassword;
    delete oUserToSave.properties;

    var oServerCallback = {};

    SettingUtils.csPutRequest(oUserRequestMapping.create, {}, oUserToSave, successCreateUserGridCallback.bind(this, oServerCallback),
        failureCreateUserGridCallback.bind(this, oServerCallback));
  };

  let successCreateUserGridCallback = function (oCallbackData, oResponse) {
    UserProps.setSelectedUser({});
    let oSavedUser = oResponse.success;
    let aUsersGridData = UserProps.getUserGridData();
    aUsersGridData.push(oSavedUser);
    UserProps.setReferencedRoles(oSavedUser.referencedRoles);
    let oProcessedUser = GridViewStore.getProcessedGridViewData(GridViewContexts.USER, [oSavedUser])[0];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.USER);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedUser);
    let aUserList = SettingUtils.getAppData().getUserList();
    aUserList[oSavedUser.id] = oSavedUser;
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);

    alertify.success(getTranslation().USER_CREATED_SUCCESSFULLY);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let failureCreateUserGridCallback = function (oCallbackData, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCreateUserGridCallback", getTranslation());
  };

  let _handleUserCreateClicked = function (oModel) {
    let oErrors = UserProps.getErrorFields();
    // if (CS.isEmpty(oErrors)) {
    _createCall(oModel, oErrors);
    // } else{
    // _showErrors(oErrors);
    // }
  };

  let _cancelCreateUser = function (oModel) {
    let oUserMap = SettingUtils.getAppData().getUserList();
    delete oUserMap[oModel.id];
    UserProps.setErrorFields({});
    _setSelectedUser({});
  };

/*
  let _checkEmailAndUserName = function (oUserGridRow, sPropertyId, sValue) {
    if (sPropertyId === 'email') {
      let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUTHORIZATION_MAPPING);
      let sEmail = oUserGridRow.properties[sPropertyId].value;
      if (sEmail !== sValue && CS.isEmailValid(sValue)) {
        oUserGridRow.properties[sPropertyId].value = sValue;
        oUserGridRow.isDirty = true;
        oGridViewProps.setIsGridDataDirty(true);
      } else {
        alertify.error(getTranslation().EMAIL_VALIDATE, 0);
      }
    } else if (sPropertyId === 'userName') {
      let sUserName = oUserGridRow.properties[sPropertyId].value;
      let aUserGridData = UserProps.getUserGridData();
      let oMasterUser = CS.find(aUserGridData, {id: oUserGridRow.id});
      let oUser = CS.cloneDeep(oMasterUser);
      oUserGridRow.properties[sPropertyId].value = sValue;
      oUser.userName = sValue;
      oUser.password = "";
      if (oUser.userName !== oMasterUser.userName) {
        _checkUsernameAvailability(oUser, sUserName, true);
      }
    }
  };
*/

  let _handleUserDataChangeEvent = function (oModel, sContext, sNewValue) {
    var oMasterUserList = SettingUtils.getAppData().getUserList();
    var oActiveUser = oMasterUserList[oModel.id];
    var oErrors = UserProps.getErrorFields();
    if (sContext == 'lastName') {
      oActiveUser[sContext] = sNewValue;
      if (!CS.isEmpty(sNewValue)) {
        delete oErrors['lastName'];
      } else {
        oErrors['lastName'] = getTranslation().LAST_NAME_NOT_EMPTY;
      }
    } else if (sContext == 'userName') {
      if (!CS.isEmpty(sNewValue)) {
        if (oActiveUser.userName !== sNewValue) {
          let sOldUserName = oActiveUser.userName;
          oActiveUser[sContext] = sNewValue;
          _checkUsernameAvailability(oActiveUser, sOldUserName);
        } else {
          delete oErrors['userName'];
        }
      } else {
        oErrors['userName'] = getTranslation().ERROR_USERNAME_NOT_EMPTY;
      }
    } else if (sContext == 'password') {
      sNewValue = sNewValue.trim();
      oActiveUser[sContext] = sNewValue;
      /*
		 * if (CS.isEmpty(sNewValue)) { oErrors['password'] =
		 * getTranslation().ERROR_PASSWORD_NOT_EMPTY; } else
		 */ if (oActiveUser['confirmPassword']) {
        if (oActiveUser['confirmPassword'] == sNewValue) {
          delete oErrors['confirmPassword'];
          delete oErrors['password'];
        } else {
          oErrors['confirmPassword'] = getTranslation().ERROR_PASSWORD_AND_CONFIRM_PASSWORD;
        }
      } else {
        if (oActiveUser.password != oActiveUser['confirmPassword']) {
          oErrors['confirmPassword'] = getTranslation().ERROR_PASSWORD_AND_CONFIRM_PASSWORD;
        } else {
          delete oErrors['confirmPassword'];
        }
      }
    } else if (sContext == 'confirmPassword') {
      sNewValue = sNewValue.trim();
      oActiveUser[sContext] = sNewValue;
      if (CS.isEmpty(sNewValue)) {
        oErrors['confirmPassword'] = getTranslation().ERROR_PASSWORD_AND_CONFIRM_PASSWORD;
      } else if (oActiveUser.password) {
        if (oActiveUser.password == sNewValue) {
          delete oErrors['confirmPassword'];
          delete oErrors['password'];
        }
      } else {
        oErrors['confirmPassword'] = getTranslation().ERROR_PASSWORD_AND_CONFIRM_PASSWORD;
      }
    } else if (sContext == 'birthDate') {
      oActiveUser[sContext] = new Date(sNewValue);
    } else if (sContext == 'email') {
      sNewValue = sNewValue.trim();
      oActiveUser[sContext] = sNewValue;
      if (!CS.isEmailValid(sNewValue)) {
        oErrors['email'] = getTranslation().EMAIL_VALIDATE;
      } else {
        delete oErrors['email'];
      }
    } else {
      oActiveUser[sContext] = sNewValue;
    }
    _setSelectedUser(oActiveUser);
    UserProps.setErrorFields(oErrors);
  };

  let _handleUserPasswordSubmit = function (sPassword) {
    var oCurrentUser = _getSelectedUser();
    var oDataToSave = {};
    oDataToSave.id = oCurrentUser.id;
    if (UserProps.getChangePasswordEnabled()) {
      var sUsername = oCurrentUser.userName;
     /*
		 * if (sPassword == "") {
		 * alertify.error(getTranslation().ERROR_PASSWORD_NOT_EMPTY); return; }
		 */
      sPassword = CS.isNotEmpty(sPassword) ? btoa(sUsername + "::" + sPassword) : "";
      oDataToSave.password = sPassword;
      SettingUtils.csPostRequest(oUserRequestMapping.ResetPassword, {}, oDataToSave, successSavePasswordCallback, failureSavePasswordCallback);
    }
  };

  var _handleExportUser = function (oSelectiveExportDetails) {
    SettingUtils.csPostRequest(oSelectiveExportDetails.sUrl, {}, oSelectiveExportDetails.oPostRequest, successExportFile, failureExportFile);
  };

  var successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
  };

  var failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
  };

  var _getIsValidFileTypes = function (oFile) {
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var uploadFileImport = function (oImportExcel, oCallback) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  var successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
  };

  var _handleUserFileUploaded = function (aFiles,oImportExcel) {
    var bIsAnyValidImage = false;
    var bIsAnyInvalidImage = false;
    if (aFiles.length) {

      var iFilesInProcessCount = 0;
      var count = 0;

      var data = new FormData();
      CS.forEach(aFiles, function (file, index) {
        if (_getIsValidFileTypes(file)) {
          bIsAnyValidImage = true;
          var reader = new FileReader();
          iFilesInProcessCount++;
          // Closure to capture the file information.
          reader.onload = (function (theFile) {
            return function (event) {
              count += 1;
              var filekey = UniqueIdentifierGenerator.generateUUID();
              data.append(filekey, theFile);
            };
          })(file);

          reader.onloadend = function () {
            iFilesInProcessCount--;
            if (iFilesInProcessCount == 0) {
              data.append("entityType", oImportExcel.entityType);
              oImportExcel.data = data;
              uploadFileImport(oImportExcel, {});
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsAnyInvalidImage = true;
        }
      });
    }
  };

  /**
	 * **************************** GRID VIEW FUNCTIONS
	 * *****************************
	 */

  return {

    getAllUsers: function (bIsTreeItemClicked) {
      _getAllUsers(bIsTreeItemClicked);
    },

    fetchUserListForGridView: function () {
      _fetchUserListForGridView();
    },

    postProcessUserListAndSave: function (oCallbackData) {
      _postProcessUserListAndSave(oCallbackData);
    },

    discardUserGridViewChanges: function (oCallBackData) {
      _discardUserGridViewChanges(oCallBackData);
    },

    setShowChangePasswordDialog: function (bStatus, sUserId) {
      _setShowChangePasswordDialog(bStatus, sUserId);
      _triggerChange();
    },

    handleUserCreateClicked: function (oModel) {
      _handleUserCreateClicked(oModel);
    },

/*
    checkEmailAndUserName: function (oUserGridRow, sPropertyId, sValue) {
      _checkEmailAndUserName(oUserGridRow, sPropertyId, sValue);
      _triggerChange();
    },
*/

    setSelectedUser: function (oUser) {
      _setSelectedUser(oUser);
    },

    createUser: function () {
      var oNewUserMasterObj = _getDefaultUserObject();
      let oUserMap = SettingUtils.getAppData().getUserList();
      oUserMap[oNewUserMasterObj.id] = oNewUserMasterObj;
      _setSelectedUser(oNewUserMasterObj);
      _triggerChange();
    },

    fetchUserList: function () {
      _fetchUserList();
    },

    handleUserPasswordCancel: function () {
      UserProps.setChangePasswordEnabled(false);
      _triggerChange();
    },

    deleteUsers: function (aUserIdsToDelete, oCallBack) {
      let oMasterUserList = SettingUtils.getAppData().getUserList();
      let bIsAdminSelected = false;
      let bIsBackgroundUserSelected = false;
      let bIsBackgroundUser = false;

      CS.remove(aUserIdsToDelete,function (sUser){
        if(sUser === "admin"){
          bIsAdminSelected = true;
          return true;
        }
      });

      CS.remove(aUserIdsToDelete, function (sUser) {
        let oFoundUser = CS.find(oMasterUserList, {id: sUser});
        if (CS.isNotEmpty(oFoundUser) && oFoundUser.isBackgroundUser) {
          bIsBackgroundUser = true;
          return true;
        } else if (sUser === "backgrounduserstandardorganization") {
          bIsBackgroundUserSelected = true;
          return true;
        }
      });

      if (!CS.isEmpty(aUserIdsToDelete) && !bIsAdminSelected && !bIsBackgroundUserSelected) {
        var aBulkDeleteUsers = [];
        CS.forEach(aUserIdsToDelete, function (sUserId) {
          aBulkDeleteUsers.push(oMasterUserList[sUserId].userName);
        });

        // var sBulkDeleteUserLastNames = aBulkDeleteUsers.join(', ');
        CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteUsers,
            function () {
              _deleteUsers(aUserIdsToDelete, oCallBack)
              .then(_fetchUserListForGridView);
            }, function (oEvent) {
            }, true);
      } else if (bIsAdminSelected) {
        alertify.message(getTranslation().ADMIN_USER_CANNOT_BE_DELETED);
      } else if (bIsBackgroundUserSelected || bIsBackgroundUser) {
          alertify.message(getTranslation().BACKGROUND_USER_CANNOT_BE_DELETED);
      }
      else {
        alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      }
    },

    handleUploadImageChangeEvent: function (sIconKey) {
      var oMasterUserList = SettingUtils.getAppData().getUserList();
      var oSelectedUser = _getSelectedUser();
      var oMasterActiveUser = oMasterUserList[oSelectedUser.id];
      oMasterActiveUser.icon = sIconKey;
      _triggerChange();
    },

    handleUserDataChangeEvent: function (oModel, sContext, sNewValue) {
      _handleUserDataChangeEvent(oModel, sContext, sNewValue);
      _triggerChange();
    },

    handleUserPasswordSubmit: function (sPassword) {
      _handleUserPasswordSubmit(sPassword);
    },

/*
    handleUserDateOfBirthChangeEvent: function (oModel, sNewValue) {
      var oMasterUserList = SettingUtils.getAppData().getUserList();
      var oSelectedUser = _getSelectedUser();
      var oMasterActiveUser = oMasterUserList[oSelectedUser.id];
      if(!oMasterActiveUser.isCreated) {
        SettingUtils.makeObjectDirty(oMasterActiveUser);
        oMasterActiveUser = oMasterActiveUser.clonedObject;
      }
      oMasterActiveUser['birthDate'] = sNewValue;
    },
*/

    cancelCreateUser: function (oModel) {
      _cancelCreateUser(oModel);
      _triggerChange();
    },

    handleExportUser: function (oSelectiveExportDetails) {
      _handleExportUser(oSelectiveExportDetails);
    },

    handleUserFileUploaded: function (aFiles,oImportExcel) {
      _handleUserFileUploaded(aFiles,oImportExcel);
    },

  }
})();

MicroEvent.mixin(UserStore);

export default UserStore;
