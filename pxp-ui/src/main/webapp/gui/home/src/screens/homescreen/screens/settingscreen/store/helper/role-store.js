import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import { RoleRequestMapping as oRoleRequestMapping } from '../../tack/setting-screen-request-mapping';
import RoleProps from './../model/role-config-view-props';
import SettingUtils from './../helper/setting-utils';
import CommonUtils from './../../../../../../commonmodule/util/common-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import EntitiesList from '../../../../../../commonmodule/tack/entities-list';
import PortalTypeDictionary from '../../../../../../commonmodule/tack/portal-type-dictionary';
import OrganisationConfigViewProps from './../model/organisation-config-view-props';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import RoleCloneDialogViewProps from './../model/role-clone-dialog-props'
import PhysicalCatalogDictionary from "../../../../../../commonmodule/tack/physical-catalog-dictionary";

var RoleStore = (function () {

  var _triggerChange = function () {
    RoleStore.trigger('role-changed');
  };

  let _getActiveOrganisation = function () {
    return SettingUtils.getOrganisationStore().getActiveOrganisation();
  };

  let _getActiveOrganisationId = function () {
    let oActiveOrganisation = _getActiveOrganisation();
    return oActiveOrganisation && oActiveOrganisation.id || "";
  };

  let _setRoleList = (oRoleList) => {
    SettingUtils.getAppData().setRoleList(oRoleList);
    _setRoleValueList(oRoleList);
  };

  var successFetchRoleListCallback = function (oResponse) {
    _setRoleList(oResponse.success);
    _triggerChange();
  };

  var failureFetchRoleListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchRoleListCallback", getTranslation());
  };

  var successDeleteRoleCallback = function (oResponse) {

    let aDeletedId = oResponse.success;
    var oMasterRoleList = SettingUtils.getAppData().getRoleList();
    var oRoleValueList = RoleProps.getRoleValuesList();
    let aSortedRoleList = CS.sortBy(oMasterRoleList, (oRole) => {
      return oRole.label || oRole.code;
    });
    let oActiveRole = RoleProps.getSelectedRole();
    _setRoleScreenLockStatus(false);
    _handleBeforeDeleteRoles(oResponse.success);
    CS.forEach(aDeletedId, function (id) {
      delete oMasterRoleList[id];
    });
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnListNodeDelete(aSortedRoleList, oRoleValueList,oActiveRole.id, aDeletedId);
    CS.isNotEmpty(oNewActiveNode) && _getRoleDetails(oNewActiveNode.id);
    alertify.success(getTranslation().ROLES_DELETED_SUCCESSFULLY);

    if(oResponse.failure) {
      var oSelectedRole = RoleProps.getSelectedRole();
      handleDeleteRoleFailure(oResponse.failure.exceptionDetails, oMasterRoleList, oRoleValueList, oSelectedRole);
    }
    _setRoleScreenLockStatus(false);

    _triggerChange();

  };

  var handleDeleteRoleFailure = function(List, oMasterRoleList, oRoleValueList, oSelectedRole){
    var aRoleAlreadyDeleted = [];
    var aUnhandledRole = [];
    CS.forEach(List, function (oItem){
      if(oItem.key == "RoleNotFoundException"){
        aRoleAlreadyDeleted.push(oMasterRoleList[oItem.itemId].label);


        if(oSelectedRole.id && oSelectedRole.id == oItem.itemId){
          var oDummyNull = {};
          RoleProps.setSelectedRole(oDummyNull);
        }

        delete oMasterRoleList[oItem.itemId];
        delete oRoleValueList[oItem.itemId];
      }else {
        aUnhandledRole.push(oMasterRoleList[oItem.itemId].label);
      }
    });

    if(aRoleAlreadyDeleted.length>0){
      var sRoleAlreadyDeleted = aRoleAlreadyDeleted.join(',');
      alertify.error(Exception.getCustomMessage('Role_Already_Deleted',getTranslation(),sRoleAlreadyDeleted));
    }
    if(aUnhandledRole.length>0){
      var sUnhandledRole = aUnhandledRole.join(',');
      alertify.error(Exception.getCustomMessage('Unhandled_Role',getTranslation(),sUnhandledRole));
    }
  };

  var failureDeleteRoleCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var oMasterRoleList = SettingUtils.getAppData().getRoleList();
      var oRoleValueList = RoleProps.getRoleValuesList();
      var oSelectedRole = RoleProps.getSelectedRole();
      handleDeleteRoleFailure(oResponse.failure.exceptionDetails,oMasterRoleList, oRoleValueList, oSelectedRole );
      _setRoleScreenLockStatus(false);

    } else {
      SettingUtils.failureCallback(oResponse, "faliureDeleteRoleCallback", getTranslation());
    }
    _triggerChange();
  };

  var successSaveRoleCallback = function (oCallbackData, oResponse) {
    var oListItemValue = {};
    var sSelectedRoleId = _getSelectedRole().id;
    let oSuccess = oResponse.success;
    var oRoleFromServer = oResponse.success.role;

    _updatePhysicalCatalogListAfterServerCall(oRoleFromServer);
    _updatePortalListAfterServerCall(oRoleFromServer);
    _updateEntitiesListAfterServerCall(oRoleFromServer);

    var oAppData = SettingUtils.getAppData();
    oAppData.setAllClassesFlatList(oResponse.success.articles);
    oAppData.setAllCollectionClassesFlatList(oResponse.success.collections);
    oAppData.setAllSetClassesFlatList(oResponse.success.sets);
    oAppData.setAssetLazyList(oResponse.success.assets);
    oAppData.setAllAssetCollectionClassList(oResponse.success.collectionAssets);
    oAppData.setAllMarketTargetClassesFlatList(oResponse.success.markets);
    // oAppData.setUserList(oResponse.response.users);

    var oRoleList = oAppData.getRoleList();
    var oRoleValueList = RoleProps.getRoleValuesList();
    oListItemValue = oRoleValueList[sSelectedRoleId];
    var oRoleFromList = oRoleList[oRoleFromServer.id];
    CS.assign(oRoleFromList, oRoleFromServer);

    delete oRoleFromList.clonedObject;
    delete oRoleFromList.isDirty;

    _applyRoleValueToAllListNodes('isEditable', false);
    _applyRoleValueToAllListNodes('isSelected', false);
    _applyRoleValueToAllListNodes('isChecked', false);
    oListItemValue.isSelected = true;
    oListItemValue.isChecked = true;
    _setRoleListValue(sSelectedRoleId, oListItemValue);

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().ROLE}));
    _setSelectedRole(oRoleFromList);
    _setRoleScreenLockStatus(false);
    RoleProps.setReferencedTaxonomies(_processReferencedTaxonomies(oSuccess.referencedTaxonomies));
    RoleProps.setReferencedKlasses(oSuccess.referencedKlasses);

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    _triggerChange();

  };

  var failureSaveRoleCallback = function (oRole, oCallbackData, oResponse) {
    var oFailure = oResponse.failure;
    if (!CS.isEmpty(oFailure)) {
      checkAndDeleteRoleFromList(oResponse, oRole.id);
      SettingUtils.failureCallback(oResponse, "failureSaveRoleCallback", getTranslation());
      _setRoleScreenLockStatus(false);
      if(CS.find(oFailure.devExceptionDetails, {key:"UserAlreadyExistInRoleException"})) {
        let sSelectedRoleId = _getSelectedRole().id;
        let oRoleMap = SettingUtils.getAppData().getRoleList();
        delete oRoleMap[sSelectedRoleId].clonedObject;
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveRoleCallback", getTranslation());
    }
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var successCreateRoleCallback = function (oRoleToSave, oCallbackData,oResponse) {
    var oMasterRoles = SettingUtils.getAppData().getRoleList();
    var oRoleValues = RoleProps.getRoleValuesList();
    var oSavedRole = oResponse.success.role;

    _updatePhysicalCatalogListAfterServerCall(oSavedRole);
    _updatePortalListAfterServerCall(oSavedRole);
    _updateEntitiesListAfterServerCall(oSavedRole);

    var oAppData = SettingUtils.getAppData();
    oAppData.setAllClassesFlatList(oResponse.success.articles  || []);
    oAppData.setAllCollectionClassesFlatList(oResponse.success.collections  || []);
    oAppData.setAllSetClassesFlatList(oResponse.success.sets  || []);
    oAppData.setAssetLazyList(oResponse.success.assets  || []);
    oAppData.setAllAssetCollectionClassList(oResponse.success.collectionAssets  || []);
    oAppData.setAllMarketTargetClassesFlatList(oResponse.success.markets  || []);
    OrganisationConfigViewProps.setIsPermissionVisible(false);

    delete oMasterRoles[oRoleToSave.id];
    delete oRoleValues[oRoleToSave.id];
    _applyRoleValueToAllListNodes('isEditable', false);
    _applyRoleValueToAllListNodes('isSelected', false);
    _applyRoleValueToAllListNodes('isChecked', false);
    var sId = oSavedRole.id;
    oMasterRoles[sId] = oSavedRole;
    oRoleValues[sId] = {
      id: sId,
      isChecked: true,
      isEditable: false,
      isSelected: true,
      label: oSavedRole.label
    };
    _setSelectedRole(oSavedRole);
    _setRoleScreenLockStatus(false);

    let oReferencedTaxonomies = _processReferencedTaxonomies(oResponse.success.referencedTaxonomies);
    RoleProps.setReferencedTaxonomies(oReferencedTaxonomies);
    let aEndPoints = oSavedRole.endpoints || [];
    RoleProps.setAddedProfileIds(aEndPoints);
    RoleProps.setReferencedKlasses(oResponse.success.referencedKlasses);
    RoleProps.setReferencedKPIs(oResponse.success.referencedKPIs);

    alertify.success(getTranslation().ROLE_CREATED_SUCCESSFULLY);
    SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);
    _getAllowedEndpoints();

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var failureCreateRoleCallback = function (oCallbackData, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {

      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "OrganizationNotFoundException"}))) {
        let oActiveOrganisation = OrganisationConfigViewProps.getActiveOrganisation();
        alertify.error("[" + oActiveOrganisation.label + "] " + getTranslation().ERROR_ALREADY_DELETED);
        SettingUtils.removeOrganisationsFromProps([oActiveOrganisation.id]);
        _setRoleScreenLockStatus(false);
        _triggerChange();
      } else {
        var sRole = checkAndDeleteRoleFromList(oResponse);
        alertify.error(Exception.getMessage(oResponse, getTranslation(), sRole), 0);
        _setRoleScreenLockStatus(false);
      }

    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveRoleCallback", getTranslation());
    }

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  let _processReferencedTaxonomies = function (aReferencedTaxonomies) {
    let oReferencedTaxonomies = {};
    CS.forEach(aReferencedTaxonomies, function (oReferencedTaxonomy) {
      oReferencedTaxonomies[oReferencedTaxonomy.id] = oReferencedTaxonomy;
    });
    return oReferencedTaxonomies;
  };

  var successGetRoleDetailsCallback = function (sSelectedRoleId, oCallBack, oResponse) {
    var oListItemValue = {};
    var oRoleFromServer = oResponse.success.role;
    var oAppData = SettingUtils.getAppData();

    _updatePhysicalCatalogListAfterServerCall(oRoleFromServer);
    _updatePortalListAfterServerCall(oRoleFromServer);
    _updateEntitiesListAfterServerCall(oRoleFromServer);

    oAppData.setAllClassesFlatList(oResponse.success.articles);
    oAppData.setAllCollectionClassesFlatList(oResponse.success.collections);
    oAppData.setAllSetClassesFlatList(oResponse.success.sets);
    oAppData.setAssetLazyList(oResponse.success.assets);
    oAppData.setAllAssetCollectionClassList(oResponse.success.collectionAssets);
    oAppData.setAllTaskClassesFlatList(oResponse.success.tasks);
    oAppData.setAllEditorialClassesFlatList(oResponse.success.editorials);
    oAppData.setAllEditorialCollectionClassesFlatList(oResponse.success.collectionEditorials);
    oAppData.setAllMarketTargetClassesFlatList(oResponse.success.markets);
    oAppData.setAllTargetCollectionClassesFlatList(oResponse.success.collectionTargets);
   // oAppData.setUserList(oResponse.response.users);

    var oRoleList = oAppData.getRoleList();
    var oRoleValueList = RoleProps.getRoleValuesList();
    oListItemValue = oRoleValueList[sSelectedRoleId];
    var oRoleFromList = oRoleList[oRoleFromServer.id];
    CS.assign(oRoleFromList, oRoleFromServer);

    delete oRoleFromList.clonedObject;
    delete oRoleFromList.isDirty;

    _applyRoleValueToAllListNodes('isEditable', false);
    _applyRoleValueToAllListNodes('isSelected', false);
    _applyRoleValueToAllListNodes('isChecked', false);
    oListItemValue.isSelected = true;
    oListItemValue.isChecked = true;
    _setRoleListValue(sSelectedRoleId, oListItemValue);
    _setSelectedRole(oRoleFromList);
    let oReferencedTaxonomies = _processReferencedTaxonomies(oResponse.success.referencedTaxonomies);
    RoleProps.setReferencedTaxonomies(oReferencedTaxonomies);
    var aEndPoints = oRoleFromServer.endpoints || [];
    RoleProps.setAddedProfileIds(aEndPoints);
    RoleProps.setReferencedKlasses(oResponse.success.referencedKlasses);
    RoleProps.setReferencedKPIs(oResponse.success.referencedKPIs);

    /**Un-necessary call now due to lazy loading*/
    // _getAllowedEndpoints();

    _setRoleScreenLockStatus(false);
    oCallBack && oCallBack.functionToExecute && oCallBack.functionToExecute();

    _triggerChange();
  };

  var failureGetRoleDetailsCallback = function (sSelectedRoleId,oResponse) {
    if (!CS.isEmpty(oResponse.failure) && CS.find(oResponse.failure.exceptionDetails, {key: "RoleNotFoundException"})) {
      var sRoleName = checkAndDeleteRoleFromList(oResponse,sSelectedRoleId);
      alertify.error("[" + sRoleName +"] "+getTranslation().ERROR_ALREADY_DELETED, 0);
      _setRoleScreenLockStatus(false);

    } else {
      SettingUtils.failureCallback(oResponse, "failureGetRoleDetailsCallback", getTranslation());
    }
  };

  //todo remove after proper backend impl
  let _getAllowedEndpoints = function () {
    let oParameters = {
      id: _getActiveOrganisationId()
    };
    SettingUtils.csGetRequest(oRoleRequestMapping.GetAllowedEndpoints, oParameters, successGetAllowedEndpointsCallback, failureGetAllowedEndpointsCallback)
  };

  let successGetAllowedEndpointsCallback = function (oResponse) {
    RoleProps.setAllowedEntitiesById("endpoints", oResponse.success);
    _triggerChange();
  };

  let failureGetAllowedEndpointsCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGetAllowedEndpointsCallback", getTranslation())
  };

  var successGetRoleDetailsByIdCallback = function (sSelectedRoleId, oCallback, oResponse) {
    var oRoleFromServer = oResponse.success.role;
    var oScreenProps = SettingUtils.getComponentProps().screen;
    var oLoadedRoleData = oScreenProps.getLoadedRolesData();
    oLoadedRoleData[oRoleFromServer.id] = oRoleFromServer;
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var failureGetRoleDetailsByIdCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGetRoleDetailsByIdCallback", getTranslation())
  };

  var checkAndDeleteRoleFromList = function (oResponse, sRoleId) {
    // if(oResponse.errorCode = "com.cs.config.interactor.incoming.InteractorException-com.cs.config.interactor.outgoing.database.RoleNotFoundException"){
    var oMasterRoles = SettingUtils.getAppData().getRoleList();
    var oRoleValues = RoleProps.getRoleValuesList();

    var oSelectedRole = RoleProps.getSelectedRole();
    var aExceptionDetails = oResponse.failure.exceptionDetails;
    var oNotFoundObj = CS.find(aExceptionDetails, {key: "RoleNotFoundException"});
    if (oNotFoundObj && (sRoleId || oSelectedRole.id)) {
      var sRoleName = oMasterRoles[sRoleId || oSelectedRole.id].label;
      delete oRoleValues[sRoleId || oSelectedRole.id];
      delete oMasterRoles[sRoleId || oSelectedRole.id];
      var oDummyNull = {};
      RoleProps.setSelectedRole(oDummyNull);
      return sRoleName;
    }
    return null;
  };

  var _saveRole = function (oCallbackData) {
    var sSelectedRoleId = _getSelectedRole().id;
    var oRoleMap = SettingUtils.getAppData().getRoleList();
    var oSelectedRole = oRoleMap[sSelectedRoleId];

    //if (!(oSelectedRole || oSelectedRole))
    if(!(_getRoleScreenLockStatus())){
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }

    var oRoleValueObject = RoleProps.getRoleValuesList()[sSelectedRoleId];
    oRoleValueObject.isEditable = false;

    var oRole = oSelectedRole.isDirty? oSelectedRole.clonedObject : oSelectedRole;
    oRole.label = oRole.label.trim();
    if (CS.isEmpty(oRole.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      _triggerChange();
      return;
    }
    if (oRole.physicalCatalogs.length === 1
        && oRole.physicalCatalogs[0] === PhysicalCatalogDictionary.DATAINTEGRATION
        && !oRole.isReadOnly)
    {
      alertify.message(getTranslation().PHYSICAL_CATALOG_ONE_MORE_SELECTION_WARNING);
      _triggerChange();
      return;
    }

    _makeDataForServer(oRole, oSelectedRole);

    var oServerCallback = {};
    oServerCallback.functionToExecute = oCallbackData && oCallbackData.functionToExecute;

    SettingUtils.csPostRequest(oRoleRequestMapping.save, {}, oRole, successSaveRoleCallback.bind(this, oServerCallback), failureSaveRoleCallback.bind(this, oRole, oServerCallback));

  };

  var _handleBeforeDeleteRoles = function (aRoleIds) {
    let oPermissionStore = SettingUtils.getPermissionStore();
    var sSelectedRoleId = _getSelectedRole().id;
    if (CS.indexOf(aRoleIds, sSelectedRoleId) >= 0) {
      _setSelectedRole({});
      oPermissionStore.setActivePermission({});
    }
  };

  var _setRoleValueList = function (aRoleList) {
    let oRoleValueMap = {};
    CS.forEach(aRoleList, function (oListNode) {
      var oListItem = {};
      oListItem.id = oListNode.id;
      oListItem.label = oListNode.label;
      oListItem.isChecked = false;
      oListItem.isEditable = false;
      oListItem.isSelected = false;

      oRoleValueMap[oListNode.id] = oListItem;
    });
    RoleProps.setRoleValuesList(oRoleValueMap);
  };

  var _fetchRoleList = function () {
    //TODO: Organisation (Provide organisation Id to get Roles of an organisation)
    SettingUtils.csGetRequest(oRoleRequestMapping.getAll, {}, successFetchRoleListCallback, failureFetchRoleListCallback);

  };

  var _deleteUnSavedRole = function (aList) {
    var oValueList = RoleProps.getRoleValuesList();
    var oMasterList = SettingUtils.getAppData().getRoleList();
    var aSavedRoleIds = [];
    var aDeletedRoleIds = [];

    CS.forEach(aList, function (uid) {
      if (oMasterList[uid].isCreated) {
        aDeletedRoleIds.push(uid);
        delete oValueList[uid];
        delete oMasterList[uid];
      } else {
        aSavedRoleIds.push(uid);
      }
    });

    var sSelectedRoleId = _getSelectedRole().id;
    if (CS.indexOf(aDeletedRoleIds, sSelectedRoleId) >= 0) {
      _setSelectedRole({});
    }

    return aSavedRoleIds;
  };

  var _deleteRoles = function (aBulkDeletelist) {
    var aFilteredRoleIds = _deleteUnSavedRole(aBulkDeletelist);
    if (!CS.isEmpty(aFilteredRoleIds)) {
      SettingUtils.csDeleteRequest(oRoleRequestMapping.bulkDelete, {}, {ids: aFilteredRoleIds}, successDeleteRoleCallback, failureDeleteRoleCallback);
    } else {
      _setRoleScreenLockStatus(false);
      alertify.success(getTranslation().ROLES_DELETED_SUCCESSFULLY);
      _triggerChange();
    }
  };

  var _getSelectedRole = function () {
    return RoleProps.getSelectedRole();
  };

  var _makeActiveRoleDirty = function () {
    _setRoleScreenLockStatus(true);
    var oActiveRole = RoleProps.getSelectedRole();
    if (!oActiveRole.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveRole);
    }
    _setRoleScreenLockStatus(true);
    return oActiveRole.clonedObject;
  };

  var _applyRoleValueToAllListNodes = function (sRoleName, oValue) {
    var oListValuesInList = RoleProps.getRoleValuesList();
    CS.map(oListValuesInList, function (oListValue) {
      oListValue[sRoleName] = oValue;
    });
  };

  var _getRoleDetails = function (sSelectedRoleId, oCallBack) {

    SettingUtils.csGetRequest(oRoleRequestMapping.get, {id: sSelectedRoleId}, successGetRoleDetailsCallback.bind(this, sSelectedRoleId, oCallBack), failureGetRoleDetailsCallback.bind(this, sSelectedRoleId));

  };

  var _getRoleDetailsById = function (sSelectedRoleId, oCallbackData) {

    SettingUtils.csGetRequest(oRoleRequestMapping.get, {id: sSelectedRoleId}, successGetRoleDetailsByIdCallback.bind(this, sSelectedRoleId, oCallbackData), failureGetRoleDetailsByIdCallback.bind(this,sSelectedRoleId));

  };

  var _setRoleListValue = function (iId, oListItemValue) {
    var oListValuesInList = RoleProps.getRoleValuesList();
    oListValuesInList[iId] = oListItemValue;
  };

  var _createUntitledRoleValueObject = function () {
    return {
      id: "",
      isChecked: false,
      isEditable: false,
      isSelected: true
    };
  };

  var _createUntitledRoleMasterObject = function () {
    let sOrganisationId = _getActiveOrganisationId();

    return {
      id: "",
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      isStandard: false,
      defaultValue: "",
      description: "",
      tooltip: "",
      placeholder: "",
      isMultiselect: true,
      organizationId: sOrganisationId,
      entities: [],
      code: ""
    }
  };

  var _setRoleScreenLockStatus = function (bStatus) {
    RoleProps.setRoleScreenLockStatus(bStatus);
  };

  var _getRoleScreenLockStatus = function () {
    return RoleProps.getRoleScreenLockStatus();
  };

  var _setSelectedRole = function (oRole) {
    RoleProps.setSelectedRole(oRole);
  };

  var _roleDataChanged = function (oModel, sContext, sNewValue) {
    var oList = SettingUtils.getAppData().getRoleList();
    var oRoleMaster = oList[oModel.id];
    if(!oRoleMaster.isCreated && !oRoleMaster.isDirty) {
      SettingUtils.makeObjectDirty(oRoleMaster);
    }

    oRoleMaster = oRoleMaster.clonedObject? oRoleMaster.clonedObject: oRoleMaster;
    oRoleMaster[sContext] = sNewValue;
    var oRoleValueObject = RoleProps.getRoleValuesList()[oModel.id];
    oRoleValueObject.isEditable = false;
    RoleStore.setRoleScreenLockStatus(true);
  };

  let _handleRoleUsersChanged = function (aSelectedUserIds) {
    var oMasterRoleList = SettingUtils.getAppData().getRoleList();
    var oSelectedRole = _getSelectedRole();
    var oMasterActiveRole = oMasterRoleList[oSelectedRole.id];
    SettingUtils.makeObjectDirty(oMasterActiveRole);
    oMasterActiveRole = oMasterActiveRole.clonedObject;

    var aSelectedUserObjectList = [];
    CS.forEach(aSelectedUserIds, function (sUserId) {
      aSelectedUserObjectList.push({id: sUserId});
    });
    oMasterActiveRole.users = aSelectedUserObjectList;
    _setRoleScreenLockStatus(true);
    _saveRole({});
  }

  var _makeDataForServer = function (oNewRoleForServer, oOldRoleData) {
    let oSystemsForRoleMap = SettingUtils.getAppData().getSystemsMapForRoles();

    var aOldRoleUsers = oOldRoleData.users;
    var aNewRoleUsers = oNewRoleForServer.users;


    var aAddedUsers = [];
    var aRemovedUsers = [];

    //For newly added members
    CS.forEach(aNewRoleUsers, function (oUser) {
      var oMemberFromOldRole = CS.find(aOldRoleUsers, {'id': oUser.id});
      if(CS.isEmpty(oMemberFromOldRole)) {
        aAddedUsers.push(oUser.id);
      }
    });

    //For removed members
    CS.forEach(aOldRoleUsers, function (oUser) {
      var oUserFromNewRole = CS.find(aNewRoleUsers, {'id': oUser.id});
      if(CS.isEmpty(oUserFromNewRole)) {
        aRemovedUsers.push(oUser.id);
      }
    });

    oNewRoleForServer.addedUsers = aAddedUsers;
    oNewRoleForServer.deletedUsers = aRemovedUsers;
    delete oNewRoleForServer.users;

    var aOldRoleProfile = oOldRoleData.endpoints;
    var aNewRoleProfile = oNewRoleForServer.endpoints;
    oNewRoleForServer.addedEndpoints = CS.difference(aNewRoleProfile, aOldRoleProfile);
    oNewRoleForServer.deletedEndpoints = CS.difference(aOldRoleProfile, aNewRoleProfile);

   let aOldRoleSystems = oOldRoleData.systems;
   let aNewRoleSystems = oNewRoleForServer.systems;
    oNewRoleForServer.addedSystemIds = CS.difference(aNewRoleSystems, aOldRoleSystems);
    oNewRoleForServer.deletedSystemIds = CS.difference(aOldRoleSystems, aNewRoleSystems);

    CS.forEach(oNewRoleForServer.systems, function (sSystemId) {
      let aSystemEndpointIds = oSystemsForRoleMap[sSystemId].list;
      let aDifference = CS.difference(aSystemEndpointIds, oNewRoleForServer.endpoints);
      if (aDifference.length === aSystemEndpointIds.length) {
        if (!CS.includes(oNewRoleForServer.addedSystemIds, sSystemId)) {
          oNewRoleForServer.deletedSystemIds.push(sSystemId);
        } else {
          CS.pull(oNewRoleForServer.addedSystemIds, sSystemId);
        }
      }
    });

    oNewRoleForServer.addedTargetTaxonomies = CS.difference(oNewRoleForServer.targetTaxonomies, oOldRoleData.targetTaxonomies);
    oNewRoleForServer.deletedTargetTaxonomies = CS.difference(oOldRoleData.targetTaxonomies, oNewRoleForServer.targetTaxonomies);
    oNewRoleForServer.addedTargetKlasses = CS.difference(oNewRoleForServer.targetKlasses, oOldRoleData.targetKlasses);
    oNewRoleForServer.deletedTargetKlasses = CS.difference(oOldRoleData.targetKlasses, oNewRoleForServer.targetKlasses);
    delete oNewRoleForServer.endpoints;
    delete oNewRoleForServer.systems;

    oNewRoleForServer.deletedKPIs = CS.difference(oOldRoleData.kpis, oNewRoleForServer.kpis);
    oNewRoleForServer.addedKPIs = CS.difference(oNewRoleForServer.kpis, oOldRoleData.kpis);
    delete oNewRoleForServer.kpis;

    let aOrganisationPhysicalCatalogs = _getActiveOrganisation().physicalCatalogs;
    oNewRoleForServer.physicalCatalogs = oNewRoleForServer.physicalCatalogs.length === aOrganisationPhysicalCatalogs.length ? [] : oNewRoleForServer.physicalCatalogs;

    _fillSelectedEntitiesToBeSent(oNewRoleForServer);

    let aOrganisationPortals = _getActiveOrganisation().portals;
    oNewRoleForServer.portals = oNewRoleForServer.portals.length === aOrganisationPortals.length ? [] : oNewRoleForServer.portals;
  };

  let _fillSelectedEntitiesToBeSent = function (oNewRoleForServer) {
    let iMasterEntitiesLength = 0;
    //Validating entities Portal Wise
    let aSelectedPortals = CS.isEmpty(oNewRoleForServer.portals) ? CS.values(PortalTypeDictionary) : oNewRoleForServer.portals;
    CS.forEach(aSelectedPortals, function (sPortalId) {
      let aMasterEntities = [];
      switch (sPortalId) {
        case PortalTypeDictionary.PIM:
          aMasterEntities = CS.map(EntitiesList(), 'id');
          break;
      }
      iMasterEntitiesLength += aMasterEntities.length;
      !CS.isEmpty(aMasterEntities) && CS.isEmpty(CS.intersection(oNewRoleForServer.entities, aMasterEntities)) && (oNewRoleForServer.entities.push(...aMasterEntities));
    });
    if (oNewRoleForServer.entities.length === iMasterEntitiesLength) {
      oNewRoleForServer.entities = [];
    }
  };

  let _handlePortalChanged = function (sKey, sId) {
    let oActiveRole = _getSelectedRole();
    oActiveRole = oActiveRole.clonedObject || oActiveRole;
    let aEntities = EntitiesList();
    let bSelectionExists = CS.includes(oActiveRole.portals, sId);
    if (oActiveRole.portals.length == 1 && bSelectionExists) {
      alertify.message(getTranslation().PORTAL_SELECTION_EMPTY_WARNING);
      return;
    }
    oActiveRole = _makeActiveRoleDirty();
    if (bSelectionExists) {
      //Portal De-selection
      CS.forEach(aEntities, function (oEntity) {
        CS.pull(oActiveRole.entities, oEntity.id);
      });
    } else {
      oActiveRole.entities.push(...CS.map(aEntities, 'id'));
    }

    _handleSelectionToggleButtonClicked(sKey, sId);
  };

  let _handleSelectionToggleButtonClicked = function (sKey, sId, bSingleSelect) {
    let oActiveRole = _makeActiveRoleDirty();
    if (CS.isEmpty(oActiveRole[sKey])) {
      oActiveRole[sKey] = [];
    }
    if (CS.includes(oActiveRole[sKey], sId)) {
      CS.pull(oActiveRole[sKey], sId);
    } else {
      oActiveRole[sKey].push(sId);
    }

    if (sKey === "physicalCatalogs" && oActiveRole[sKey].length === 1
        && oActiveRole[sKey][0] === PhysicalCatalogDictionary.DATAINTEGRATION
        && !oActiveRole.isReadOnly) {
      alertify.message(getTranslation().PHYSICAL_CATALOG_ONE_MORE_SELECTION_WARNING);
    }
  };

  let _getActiveRoleId = () => {
    let oActiveRole = _getSelectedRole();
    return oActiveRole && oActiveRole.id || "";
  };

  let _getActiveRoleIdFromList = () => {
    let oRoleListValues = RoleProps.getRoleValuesList();
    let oSelectedRoleListValue = CS.find(oRoleListValues, {isSelected: true});
    return oSelectedRoleListValue && oSelectedRoleListValue.id || "";
  };

  let _handleTaxonomyAdded = function (sTaxonomyId, sParentTaxonomyId) {
    let oActiveRole = _makeActiveRoleDirty();

    CS.pull(oActiveRole.targetTaxonomies, sParentTaxonomyId);
    oActiveRole.targetTaxonomies.push(sTaxonomyId);
    let oCallback = {};
    _saveRole(oCallback);
  };

  let _handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oTaxonomy, sActiveTaxonomyId) {
    let oActiveRole = _makeActiveRoleDirty();
    let aTaxonomyIds = oActiveRole.targetTaxonomies;
    let iActiveTaxonomyIndex = aTaxonomyIds.indexOf(sActiveTaxonomyId);

    if (oTaxonomy.parent.id == SettingUtils.getTreeRootId()) {
      CS.remove(aTaxonomyIds, function (sTaxonomyId) {
        return sTaxonomyId == sActiveTaxonomyId;
      });
    } else {
      aTaxonomyIds[iActiveTaxonomyIndex] = oTaxonomy.parent.id;
    }

    _saveRole({});
  };

  let _handleMultiSelectSearchCrossIconClicked = function (sKey, sId) {
    let oActiveRole = _makeActiveRoleDirty();
    if(oActiveRole.id === "adminrole" && sId === "admin") {
      alertify.message(getTranslation().ADMIN_USER_CANNOT_BE_DELETED);
      return;
    }
    if (sKey == "users") {
      CS.remove(oActiveRole[sKey], {id: sId});
    } else {
      CS.pull(oActiveRole[sKey], sId);
    }

    _saveRole({});
  };

  let _makePostDataToFetch = function (sKey, oPagination, sId) {
    let oPaginationData = CS.isEmpty(oPagination) ? RoleProps.getRolePaginationDataById(sKey) : oPagination;
    let sActiveOrganisationId = _getActiveOrganisation().id;
    let oPostData = {
      searchText: oPaginationData.searchText,
      from: oPaginationData.from,
      size: oPaginationData.size,
    };
    if (sKey == "taxonomies" || sKey == "Klass") {
      oPostData.organizationId = sActiveOrganisationId;
      oPostData.selectionType = sKey;
      oPostData.id = sId == "addItemHandlerforMultiTaxonomy" ? SettingUtils.getTreeRootId().toString() : sId;
    }
    return oPostData;
  };

  let _fetchAllowedRoleEntities = function (sKey, bAppend, oPagination, sId) {
    let oPostData = _makePostDataToFetch(sKey, oPagination, sId);
    let sUrl = oRoleRequestMapping.GetAllowedRoleEntities;

    if (sKey == "users") {
      sUrl = oRoleRequestMapping.GetAllowedUsers;
    }

    SettingUtils.csPostRequest(sUrl, {}, oPostData, successFetchRoleEntitiesCallback.bind(this, sKey, bAppend), failureFetchRoleEntitiesCallback);
  };

  let _handleMssSearchClicked = function (sContext, sSearchText, sTaxonomyId) {
    RoleProps.resetRolePaginationDataById(sContext);
    RoleProps.setRolePaginationSearchTextById(sContext, sSearchText);
    _fetchAllowedRoleEntities(sContext, false, {}, sTaxonomyId);
  };

  let _handleDashboardVisibilityToggled = function () {
    var oActiveRole = _makeActiveRoleDirty();
    let bIsDashboardEnable = oActiveRole.isDashboardEnable;
    if(bIsDashboardEnable){
      oActiveRole.landingScreen = "explore"
    }
    oActiveRole.isDashboardEnable = !bIsDashboardEnable;
  };

  let _handleReadOnlyPermissionToggled = function () {
    CustomActionDialogStore.showConfirmDialog("Do you want to continue ?", "",
        _setIsReadOnlyPermissionInRole,
        function () {
        }
    );
  };

  let _setIsReadOnlyPermissionInRole = function () {
    let oActiveRole = _makeActiveRoleDirty();
    oActiveRole.isReadOnly = !oActiveRole.isReadOnly;
    _triggerChange();
  };

  let successFetchRoleEntitiesCallback = function (sKey, bAppend, oResponse) {
    let oSuccess = oResponse.success;
    let aEntitiesFromServer = oSuccess.list;
    let oTaxonomyConfigDetails = oSuccess.configDetails;
    let aEntities = RoleProps.getAllowedEntitiesById(sKey);
    if (bAppend) {
      if (CS.isEmpty(aEntitiesFromServer)) {
        //todo translations
        alertify.message(getTranslation().REACHED_END_OF_THE_LIST);
      } else {
        aEntities = CS.concat(aEntities, aEntitiesFromServer);
        let oSystemsListPaginationData = RoleProps.getRolePaginationDataById(sKey);
        oSystemsListPaginationData.from = oSystemsListPaginationData.from + aEntities.length;
      }
    } else {
      aEntities = aEntitiesFromServer;
    }
    RoleProps.setAllowedEntitiesById(sKey, aEntities);
    let oAllowedTaxonomyHierarchyListData = CommonUtils.prepareTaxonomyHierarchyData(RoleProps.getAllowedEntitiesById(sKey), oTaxonomyConfigDetails);
    RoleProps.setAllowedTaxonomyHierarchyList(oAllowedTaxonomyHierarchyListData);
    _triggerChange();
  };

  let failureFetchRoleEntitiesCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchRoleEntitiesCallback", getTranslation());
  };

  var _createRoleDialogButtonClicked = function () {
    var oSelectedRole = _getSelectedRole();
    var oMasterRoles = SettingUtils.getAppData().getRoleList();
    var oRoleToSave = oMasterRoles[oSelectedRole.id];
    oRoleToSave = oRoleToSave.clonedObject || oRoleToSave;
    var oCallbackData = {};
    oCallbackData.functionToExecute = _triggerChange;

    if(CS.isEmpty(oRoleToSave.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    var oCodeToVerifyUniqueness = {
      id: oRoleToSave.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_ROLE
    };

    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = _createRoleCall.bind(this, oRoleToSave, oCallbackData);

    var sURL = oRoleRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  var _createRoleCall = function (oRoleToSave, oCallbackData) {
    SettingUtils.csPutRequest(oRoleRequestMapping.create, {}, oRoleToSave, successCreateRoleCallback.bind(this, oRoleToSave, oCallbackData), failureCreateRoleCallback.bind(this, oCallbackData));
  };

  var _cancelRoleDialogClick = function () {
    var oRoleValueList = RoleProps.getRoleValuesList();
    var oSelectedRole = _getSelectedRole();
    var oMasterRoles = SettingUtils.getAppData().getRoleList();
    delete oMasterRoles[oSelectedRole.id];
    delete oRoleValueList[oSelectedRole.id];
    _setSelectedRole({});
    _setRoleScreenLockStatus(false);
    OrganisationConfigViewProps.setIsPermissionVisible(false);
    _triggerChange();

  };

  var _handleRoleConfigDialogButtonClicked = function (sButtonId) {
    if (sButtonId == "create") {
      _createRoleDialogButtonClicked();
    } else {
      _cancelRoleDialogClick();
    }
  };

  var _handleRoleSingleValueChanged = function (sKey, sVal) {
    var oActiveRole = _makeActiveRoleDirty();
    oActiveRole[sKey] = sVal;
    _triggerChange();
  };

  let _handleSystemSelectApplyClicked = function (aSelectedSystemIds) {
    let oSelectedRole = RoleProps.getSelectedRole();
    let oSystemsMap = SettingUtils.getAppData().getSystemsMapForRoles();
    if (!oSelectedRole.clonedObject) {
      _makeActiveRoleDirty();
    }

    oSelectedRole = oSelectedRole.clonedObject || oSelectedRole;
    let aSystemsToSelect = CS.difference(aSelectedSystemIds, oSelectedRole.systems);
    if (CS.isEmpty(aSystemsToSelect)) {
        let aSystemsToRemove = CS.difference(oSelectedRole.systems, aSelectedSystemIds);
        CS.pullAll(oSelectedRole.systems, aSystemsToRemove);
    } else {
      oSelectedRole.systems.push(...aSystemsToSelect);
    }

    CS.forEach(aSystemsToSelect, function (sSystemId) {
      oSelectedRole.endpoints.push(...oSystemsMap[sSystemId].list);
    });
  };

  let _handleEndpointSelectionViewSelectAllChecked = function (sSystemId, iCheckboxStatus) {
    let oSystemsMap = SettingUtils.getAppData().getSystemsMap();
    let aSystemEndpointIds = CS.map(oSystemsMap[sSystemId].list, "id");
    let oSelectedRole = RoleProps.getSelectedRole();
    if (!oSelectedRole.clonedObject) {
      _makeActiveRoleDirty();
    }
    oSelectedRole = oSelectedRole.clonedObject || oSelectedRole;
    let aRoleEndpointIds = oSelectedRole.endpoints;

    iCheckboxStatus = (iCheckboxStatus + 1) % 3;

    if (iCheckboxStatus === 0) {
      oSelectedRole.endpoints =CS.difference(aRoleEndpointIds, aSystemEndpointIds);
    } else {
      let aDifference = CS.difference(aSystemEndpointIds, aRoleEndpointIds);
      oSelectedRole.endpoints.push(...aDifference);
    }
  };

  let _handleEndpointSelectionViewDeleteClicked = function (sSystemId) {
    let oSystemsMap = SettingUtils.getAppData().getSystemsMap();
    let aEndpoints = CS.map(oSystemsMap[sSystemId].list, "id");
    let oSelectedRole = RoleProps.getSelectedRole();
    if (!oSelectedRole.clonedObject) {
      _makeActiveRoleDirty();
    }
    oSelectedRole = oSelectedRole.clonedObject || oSelectedRole;
    CS.pull(oSelectedRole.systems, sSystemId);
    oSelectedRole.endpoints = CS.difference(oSelectedRole.endpoints, aEndpoints);
  };

  let _updateEntitiesListAfterServerCall = function (oRoleFromServer) {
    let aEntitiesList = CS.map(SettingUtils.getEntitiesListByPortalSelections(), 'id');
    if(CS.isEmpty(oRoleFromServer.entities)){
      oRoleFromServer.entities = aEntitiesList;
    }
  };

  let _updatePhysicalCatalogListAfterServerCall = function (oRoleFromServer) {
    let aOrganisationPhysicalCatalogs = _getActiveOrganisation().physicalCatalogs;
    if(CS.isEmpty(oRoleFromServer.physicalCatalogs)){
      oRoleFromServer.physicalCatalogs = aOrganisationPhysicalCatalogs;
    }
  };

  let _updatePortalListAfterServerCall = function (oRoleFromServer) {
    let aOrganisationPortals = _getActiveOrganisation().portals;
    if(CS.isEmpty(oRoleFromServer.portals)){
      oRoleFromServer.portals = aOrganisationPortals;
    }
  };

  let _openCloneRoleDialog = (sId) => {
    let oSelectedRole = _getSelectedRole();
    if (sId === oSelectedRole.id) {
      RoleCloneDialogViewProps.setSelectedRole(oSelectedRole);
      RoleCloneDialogViewProps.setLabel(getTranslation().CLONE_OF + " " + oSelectedRole.label);
      RoleCloneDialogViewProps.setReferencedTaxonomies(RoleProps.getReferencedTaxonomies());
      RoleCloneDialogViewProps.setReferencedKlasses(RoleProps.getReferencedKlasses());
      RoleCloneDialogViewProps.setReferencedKPIs(RoleProps.getReferencedKPIs());
      RoleCloneDialogViewProps.setIsRoleCloneDialogActive(true);
    }
    else {
      SettingUtils.csGetRequest(oRoleRequestMapping.get, {id: sId}, successGetRoleDetailsForCloneDialogCallback.bind(this, sId, {}), failureGetRoleDetailsByIdCallback);
    }
  };

  let successGetRoleDetailsForCloneDialogCallback = (sSelectedRoleId, oCallBack, oResponse) => {
    let oRoleFromServer = oResponse.success.role;

    _updatePhysicalCatalogListAfterServerCall(oRoleFromServer);
    _updatePortalListAfterServerCall(oRoleFromServer);
    _updateEntitiesListAfterServerCall(oRoleFromServer);

    RoleCloneDialogViewProps.setLabel(getTranslation().CLONE_OF + " " + oRoleFromServer.label);
    RoleCloneDialogViewProps.setSelectedRole(oRoleFromServer);
    let oReferencedTaxonomies = _processReferencedTaxonomies(oResponse.success.referencedTaxonomies);
    RoleCloneDialogViewProps.setReferencedTaxonomies(oReferencedTaxonomies);
    RoleCloneDialogViewProps.setReferencedKlasses(oResponse.success.referencedKlasses);
    RoleCloneDialogViewProps.setReferencedKPIs(oResponse.success.referencedKPIs);
    RoleCloneDialogViewProps.setIsRoleCloneDialogActive(true);
    _triggerChange();
  };

  let _handleCreateRoleCloneDialogActionButtonClicked = (sId) => {
    let oCallbackData = {};
    oCallbackData.functionToExecute = function () {
      RoleCloneDialogViewProps.setIsRoleCloneDialogActive(false);
      RoleCloneDialogViewProps.reset();
    };
    if (sId == "save") {
      let bIsExactClone = RoleCloneDialogViewProps.getIsExactCloneSelected();
      let oDialogData = RoleCloneDialogViewProps.getRoleCloneDialogData();
      let oRequestModel = {
        label: oDialogData.sLabel,
        code: oDialogData.sCode,
        roleId: RoleCloneDialogViewProps.getSelectedRole().id,
        isExactClone: bIsExactClone
      };
      if (!bIsExactClone) {
        oRequestModel.clonePhysicalCatalogs = oDialogData.bIsClonePhysicalCatalog;
        oRequestModel.cloneTaxonomies = oDialogData.bIsCloneTaxonomies;
        oRequestModel.cloneTargetClasses = oDialogData.bIsCloneTargetClasses;
        oRequestModel.cloneEnableDashboard = oDialogData.bIsCloneEnableDashboard;
        oRequestModel.cloneKPI = oDialogData.bIsCloneKPI;
        oRequestModel.cloneEntities = oDialogData.bIsCloneEntities;
      }
      SettingUtils.csPutRequest(oRoleRequestMapping.cloneRole, {}, oRequestModel, successCreateRoleCallback.bind(this, {}, oCallbackData), failureCreateRoleCallback.bind(this, {}));
    } else {
      oCallbackData.functionToExecute();
    }
  };

  let _handleCreateRoleCloneDialogCheckboxButtonClicked = (sContext) => {
    let oRoleCloneDialogData = RoleCloneDialogViewProps.getRoleCloneDialogData();
    switch (sContext) {
      case "physicalCatalog":
        RoleCloneDialogViewProps.setIsClonePhysicalCatalogSelected(!oRoleCloneDialogData.bIsClonePhysicalCatalog);
        break;
      case "taxonomy":
        RoleCloneDialogViewProps.setIsCloneTaxonomiesSelected(!oRoleCloneDialogData.bIsCloneTaxonomies);
        break;
      case "targetClasses":
        RoleCloneDialogViewProps.setIsCloneTargetClassesSelected(!oRoleCloneDialogData.bIsCloneTargetClasses);
        break;
      case "enableDashboard":
        RoleCloneDialogViewProps.setIsCloneEnableDashboardSelected(!oRoleCloneDialogData.bIsCloneEnableDashboard);
        break;
      case "kpi":
        RoleCloneDialogViewProps.setIsCloneKPISelected(!oRoleCloneDialogData.bIsCloneKPI);
        break;
      case "entities":
        RoleCloneDialogViewProps.setIsCloneEntitiesSelected(!oRoleCloneDialogData.bIsCloneEntities);
        break;
    }
    ;
  };

  let _handleCreateRoleCloneDialogExactCloneButtonClicked = () => {
    let bIsExactClone = RoleCloneDialogViewProps.getIsExactCloneSelected();
    if (bIsExactClone) {
      RoleCloneDialogViewProps.setIsExactCloneSelected(false);
    }
    else {
      RoleCloneDialogViewProps.setIsExactCloneSelected(true);
      RoleCloneDialogViewProps.setIsClonePhysicalCatalogSelected(true);
      RoleCloneDialogViewProps.setIsCloneTaxonomiesSelected(true);
      RoleCloneDialogViewProps.setIsCloneTargetClassesSelected(true);
      RoleCloneDialogViewProps.setIsCloneEnableDashboardSelected(true);
      RoleCloneDialogViewProps.setIsCloneKPISelected(true);
      RoleCloneDialogViewProps.setIsCloneEntitiesSelected(true);
    }
  };

  let _handleCreateRoleCloneDialogEditValueChanged = (sContext, sValue) => {
    if (sContext == "label") {
      RoleCloneDialogViewProps.setLabel(sValue);
    } else {
      RoleCloneDialogViewProps.setCode(sValue);
    }
  }


  /******************************** PUBLIC API's ************************************/

  return {

    setSelectedRole: function (oRole) {
      _setSelectedRole(oRole);
    },

    getRoleScreenLockStatus: function () {
      return _getRoleScreenLockStatus();
    },

    setRoleScreenLockStatus: function (bStatus) {
      _setRoleScreenLockStatus(bStatus);
    },

    fetchRoleList: function () {
      _fetchRoleList();
    },

    saveRole: function (oCallbackData) {
      _saveRole(oCallbackData);
    },

    deleteRole: function (sId) {
      var aRoleIdsListToDelete = [sId];
      var oMasterRoles = SettingUtils.getAppData().getRoleList();

      if (!CS.isEmpty(aRoleIdsListToDelete)) {
        var aBulkDeleteRoles = [];
        var isStandardRoleSelected = false;
        CS.forEach(aRoleIdsListToDelete, function (sRoleId) {
          var oMasterRole = oMasterRoles[sRoleId];
          aBulkDeleteRoles.push(oMasterRoles[sRoleId].label);
          if(oMasterRole.isStandard){
            isStandardRoleSelected = true;
            return true;
          }
        });
        if(isStandardRoleSelected){
          alertify.message(getTranslation().STANDARD_ROLE_DELETEION);
        } else {
          //sBulkDeleteRoles = CS.trimEnd(sBulkDeleteRoles, ', ');
          CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION
              , aBulkDeleteRoles,
              function () {
                _deleteRoles(aRoleIdsListToDelete);
              }, function (oEvent) {
              });
        }

      } else {
        alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      }
    },

    discardUnsavedRole: function (oCallbackData) {
      var oSelectedRole = _getSelectedRole();
      var oMasterRoles = SettingUtils.getAppData().getRoleList();
      var oSelectedInMaster = oMasterRoles[oSelectedRole.id];
      var bScreenLockStatus = _getRoleScreenLockStatus();
      if(!bScreenLockStatus) {
        alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
        return;
      }

      if (!CS.isEmpty(oSelectedInMaster)) {

        if (oSelectedInMaster.isDirty) {
          delete oSelectedInMaster.clonedObject;
          delete oSelectedInMaster.isDirty;
          alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        }
        if (oSelectedInMaster.isCreated) {

          delete oMasterRoles[oSelectedRole.id];
          delete RoleProps.getRoleValuesList()[oSelectedRole.id];
          _setSelectedRole({});
          alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        }
        _setRoleScreenLockStatus(false);
        if (!oSelectedRole.isReadOnly && oCallbackData.functionToExecute) {
          oCallbackData.functionToExecute();
        }
        _triggerChange();

      } else {
        alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      }


    },

    handleRoleListNodeClicked: function (sRoleId) {
      var bRoleScreenLockStatus = _getRoleScreenLockStatus();
      var selectedRole = _getSelectedRole();

      // if (!(selectedRole.id === sRoleId)) {

        if (bRoleScreenLockStatus) {
          var oCallbackData = {};
          oCallbackData.functionToExecute = _getRoleDetails.bind(this, sRoleId);

          CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
              RoleStore.saveRole.bind(this, oCallbackData),
              RoleStore.discardUnsavedRole.bind(this, oCallbackData),
              function () {
              }
          );
        } else {
          _getRoleDetails(sRoleId);
        }

    },

    createRole: function () {
      var uuid = UniqueIdentifierGenerator.generateUUID();
      var oUntitledRoleValueObj = _createUntitledRoleValueObject();
      var oUntitledRoleMasterObj = _createUntitledRoleMasterObject();

      oUntitledRoleValueObj.id = uuid;
      oUntitledRoleMasterObj.id = uuid;


      _applyRoleValueToAllListNodes('isSelected', false);
      _applyRoleValueToAllListNodes('isChecked', false);
      _applyRoleValueToAllListNodes('isEditable', false);

      var oRoleValues = RoleProps.getRoleValuesList();
      var oMasterRoles = SettingUtils.getAppData().getRoleList();

       oRoleValues[uuid] = oUntitledRoleValueObj;
       oMasterRoles[uuid] = oUntitledRoleMasterObj;

      _setSelectedRole(oUntitledRoleMasterObj);
      _setRoleScreenLockStatus(true);
      _triggerChange();
    },

    handleSettingScreenRoleDataChanged: function (oModel, sContext, sNewValue) {
      _roleDataChanged(oModel, sContext, sNewValue);
    },

    handleRoleModulePermissionChanged: function (sModule) {
      var roleId = _getSelectedRole().id;
      var oRoleMaster = SettingUtils.getAppData().getRoleList()[roleId];
      if(!oRoleMaster.isCreated && !oRoleMaster.isDirty) {
        SettingUtils.makeObjectDirty(oRoleMaster);
      }

      oRoleMaster = oRoleMaster.clonedObject? oRoleMaster.clonedObject: oRoleMaster;
      //TODO: Remove after server integration
      oRoleMaster.entities = oRoleMaster.entities ? oRoleMaster.entities : [];
      var sRemovedItem = CS.remove(oRoleMaster.entities, function (sItem) {
        return sItem == sModule;
      });

      if(CS.isEmpty(sRemovedItem)) {
        oRoleMaster.entities.push(sModule);
      }
      RoleStore.setRoleScreenLockStatus(true);
    },

    handleRoleRefreshMenuClicked: function (){
      var sSelectedRoleId = _getSelectedRole().id;
      if(sSelectedRoleId) {
        _getRoleDetails(sSelectedRoleId);
      } else {
        _fetchRoleList();
      }
    },

    getRoleDetails: function (_sSelectedRoleId, oCallBack) {
      var sSelectedRoleId = _sSelectedRoleId || _getSelectedRole().id;
      _getRoleDetails(sSelectedRoleId, oCallBack);
    },

    handleSelectionToggleButtonClicked: function (sKey, sId, bSingleSelect) {
      if (sKey == "portals") {
        _handlePortalChanged(sKey, sId);
      } else {
        _handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
      }
      _triggerChange();
    },

    getRoleDetailsById: function (_sRoleId, oCallbackData) {
      var sSelectedRoleId = _sRoleId || _getSelectedRole().id;
      _getRoleDetailsById(sSelectedRoleId, oCallbackData);
    },

    handleRoleConfigDialogButtonClicked: function (sButtonId) {
      _handleRoleConfigDialogButtonClicked(sButtonId);
    },

    handleRoleSingleValueChanged: function (sKey, sValue) {
      _handleRoleSingleValueChanged(sKey,sValue);
    },

    handleRoleMSSValueChanged: function (sKey, aValue) {
      var oActiveRole = _makeActiveRoleDirty();
      switch (sKey) {
        case "roleType":
          oActiveRole[sKey] = aValue[0];
          break;
        case "endpoints":
          let aEndpoints = oActiveRole[sKey];
          aEndpoints.push(...aValue);
          _saveRole({});
          break;
        case "users":
          _handleRoleUsersChanged(aValue);
          break;
        case "systems":
          oActiveRole[sKey] = aValue;
          break;
        case "landingScreen":
          oActiveRole[sKey] = aValue[0];
          break;
        default:
          oActiveRole[sKey] = aValue;
          _saveRole({});
      }
      _triggerChange();
    },

    handleTaxonomyAdded: function (sTaxonomyId, sParentTaxonomyId) {
      _handleTaxonomyAdded(sTaxonomyId, sParentTaxonomyId);
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sParentTaxonomyId) {
      _handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId);
    },

    getActiveRoleId: () => {
      return _getActiveRoleId();
    },

    getActiveRoleIdFromList: () => {
      return _getActiveRoleIdFromList();
    },

    setRoleList: (oRoleList) => {
      _setRoleList(oRoleList);
    },

    handleMultiSelectSearchCrossIconClicked: function (sKey, sId) {
      _handleMultiSelectSearchCrossIconClicked(sKey, sId);
    },

    fetchRoleEntities: function (sKey, bLoadMore, sTaxonomyId) {
      let oPagination = {};
      if (bLoadMore) {
        let oPagniationFromProps = RoleProps.getRolePaginationDataById(sKey);
        oPagination.from = oPagniationFromProps.from + oPagniationFromProps.size;
        oPagination.searchText = oPagniationFromProps.searchText;
        oPagination.size = oPagniationFromProps.size;
      }
      _fetchAllowedRoleEntities(sKey, bLoadMore, oPagination, sTaxonomyId);
    },

    showScreenLockedAlertify: function (oCallBack) {
      CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveRole.bind(this, oCallBack),
          this.discardUnsavedRole.bind(this, oCallBack),
          function () {
          });
    },

    handleAllowedTaxonomiesDropdownOpened: function (sTaxonomyId) {
      _fetchAllowedRoleEntities("taxonomies", false, {}, sTaxonomyId);
    },

    handleMssSearchClicked: function (sKey, sSearchText, sTaxonomyId) {
      _handleMssSearchClicked(sKey, sSearchText, sTaxonomyId);
    },

    handleDashboardVisibilityToggled: function () {
      _handleDashboardVisibilityToggled();
      _triggerChange()
    },

    handleReadOnlyPermissionToggled: function () {
      _handleReadOnlyPermissionToggled();
    },

    handleSystemSelectApplyClicked: function (aSelectedSystemIds) {
      _handleSystemSelectApplyClicked(aSelectedSystemIds);
      _triggerChange();
    },

    handleEndpointSelectionViewSelectAllChecked: function (sSystemId, iCheckboxStatus) {
      _handleEndpointSelectionViewSelectAllChecked(sSystemId, iCheckboxStatus);
      _triggerChange();
    },

    handleEndpointSelectionViewDeleteClicked: function (sSystemId) {
      _handleEndpointSelectionViewDeleteClicked(sSystemId);
      _triggerChange();
    },

    openCloneRoleDialog: function (sId) {
      if (_getRoleScreenLockStatus()) {
        let oCallbackData = {};
        oCallbackData.functionToExecute = _openCloneRoleDialog.bind(this,sId);

        RoleStore.showScreenLockedAlertify(oCallbackData);
      }
      else {
        _openCloneRoleDialog(sId);
        _triggerChange();
      }
    },

    handleCreateRoleCloneDialogActionButtonClicked: function (sId) {
      _handleCreateRoleCloneDialogActionButtonClicked(sId);
      _triggerChange();
    },

    handleCreateRoleCloneDialogCheckboxButtonClicked: function (sContext) {
      _handleCreateRoleCloneDialogCheckboxButtonClicked(sContext);
      _triggerChange();
    },

    handleCreateRoleCloneDialogExactCloneButtonClicked: function () {
      _handleCreateRoleCloneDialogExactCloneButtonClicked();
      _triggerChange();
    },

    handleCreateRoleCloneDialogEditValueChanged: function (sContext, sValue) {
      _handleCreateRoleCloneDialogEditValueChanged(sContext, sValue);
      _triggerChange()
    }
  }

})();

MicroEvent.mixin(RoleStore);

export default RoleStore;
