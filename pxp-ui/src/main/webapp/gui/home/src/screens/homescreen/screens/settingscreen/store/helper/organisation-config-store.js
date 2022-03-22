import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import { OrganisationConfigRequestMapping as oOrganisationConfigRequestMapping, SettingsRequestMapping as oSettingScreenRequestMapping }
  from '../../tack/setting-screen-request-mapping';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import OrganisationTypeDictionary from '../../tack/organisation-type-id-dictionary';
import CommonUtils from './../../../../../../commonmodule/util/common-utils';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import SettingUtils from './../helper/setting-utils';
import OrganisationConfigViewProps from './../model/organisation-config-view-props';
import MockDataForOrganisationConfigPhysicalCatalogAndPortal from '../../../../../../commonmodule/tack/mock-data-physical-catalog-and-portal-types';
import Constants from '../../../../../../commonmodule/tack/constants';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import assetTypes from '../../tack/coverflow-asset-type-list';
import ManageEntityStore from "./config-manage-entity-store";
import GridViewStore from "../../../../../../viewlibraries/contextualgridview/store/grid-view-store";
import GridViewContext from '../../../../../../commonmodule/tack/grid-view-contexts';
import SSOSettingStore from "./sso-setting-store";
import RoleProps from "../model/role-config-view-props";

const OrganisationConfigStore = (function () {

  let _triggerChange = function () {
    OrganisationConfigStore.trigger('organisation-config-changed');
  };

  let _makeActiveOrganisationDirty = function () {
    let oActiveOrganisation = OrganisationConfigViewProps.getActiveOrganisation();
    SettingUtils.makeObjectDirty(oActiveOrganisation);
    OrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(true);
    return oActiveOrganisation.clonedObject;
  };

  let _fetchOrganisationConfigMap = function (bLoadMore) {
    bLoadMore = bLoadMore || false;
    let oPostData = SettingUtils.getEntityListViewLoadMorePostData(OrganisationConfigViewProps, bLoadMore);
    SettingUtils.csPostRequest(oOrganisationConfigRequestMapping.GetAll, {}, oPostData, successFetchOrganisationConfigMapCallBack.bind(this, bLoadMore), failureFetchOrganisationConfigMapCallBack);
  };

  let successFetchOrganisationConfigMapCallBack = function (bLoadMore, oResponse) {
    oResponse = oResponse.success;
    let aOrganisationConfigList = oResponse.list;
    let iCount = oResponse.count;

    if (bLoadMore) {
      let oOrganisationConfigMap = OrganisationConfigViewProps.getOrganisationConfigMap();
      aOrganisationConfigList = oOrganisationConfigMap.concat(aOrganisationConfigList);
    }

    OrganisationConfigViewProps.setOrganisationConfigMap(aOrganisationConfigList);
    OrganisationConfigViewProps.setShowLoadMore(CS.size(aOrganisationConfigList) !== iCount);
    _resetOrganisationConfigListValueMap();

    if(CS.isEmpty(aOrganisationConfigList)){
      OrganisationConfigViewProps.setActiveOrganisation({});
      _triggerChange()
    } else {
      _fetchOrganisation(aOrganisationConfigList[0].id);
    }
  };

  let failureFetchOrganisationConfigMapCallBack = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchOrganisationConfigMapCallBack", getTranslation);
  };

  let _resetOrganisationConfigListValueMap = function () {
    let oOrganisationConfigList = OrganisationConfigViewProps.getOrganisationConfigMap(); //todo : rename
    let oOrganisationConfigValueMap = {};
    CS.forEach(oOrganisationConfigList, function (oOrganisationConfigListItem) {
      oOrganisationConfigValueMap[oOrganisationConfigListItem.id] = SettingUtils.getDefaultListProps();
    });
    OrganisationConfigViewProps.setOrganisationConfigValueMap(oOrganisationConfigValueMap);
  };

  let _getActiveOrganisation = function () {
    return OrganisationConfigViewProps.getActiveOrganisation();
  } ;

  let _handleRolePermissionToggleButtonClicked = () => {
    let bIsPermissionVisible = !OrganisationConfigViewProps.getIsPermissionVisible();
    let oPermissionStore = SettingUtils.getPermissionStore();
    let oRoleStore = SettingUtils.getRoleStore();
    let sRoleId = oRoleStore.getActiveRoleIdFromList();
    if (bIsPermissionVisible) {
      oPermissionStore.handlePermissionViewOpened();
    }
    else {
      oRoleStore.handleRoleListNodeClicked(sRoleId);
    }

    _setIsPermissionVisible(bIsPermissionVisible);
  };

  let _isPermissionVisible = () => {
    return OrganisationConfigViewProps.getIsPermissionVisible();
  };

  let _setIsPermissionVisible = (bValue) => {
    OrganisationConfigViewProps.setIsPermissionVisible(bValue);
  };

  let _createOrganisation = function () {
    let oRequestData = {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      type: OrganisationTypeDictionary.SUPPLIERS,
      code: "",
      isCreated: true
    };
    OrganisationConfigViewProps.setActiveOrganisation(oRequestData);
    OrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(true);
    _triggerChange();
   /* AjaxPromise.put(RequestMapping.getRequestUrl(oOrganisationConfigRequestMapping.Create), JSON.stringify(oRequestData))
        .then(successCreateOrganisationConfigMapCallBack, failureCreateOrganisationConfigMapCallBack)
        .catch(failureCreateOrganisationConfigMapCallBack);*/
  };

  let successCreateOrganisationConfigMapCallBack = function (oResponse) {
    let oSuccess = oResponse.success;
    let oOrganisationFromServer = oSuccess.organization;
    // oOrganisationFromServer.isNewlyCreated = true;

    if(CS.isEmpty(oOrganisationFromServer.physicalCatalogs)){
      let aAllPhysicalCatalogs = MockDataForOrganisationConfigPhysicalCatalogAndPortal.physicalCatalogs();
      oOrganisationFromServer.physicalCatalogs = CS.map(aAllPhysicalCatalogs,'id');
    }

    if(CS.isEmpty(oOrganisationFromServer.portals)){
      let aAllPortals = MockDataForOrganisationConfigPhysicalCatalogAndPortal.portals();
      oOrganisationFromServer.portals = CS.map(aAllPortals,'id');
    }

    let oReferencedTaxonomies = oSuccess.referencedTaxonomies;
    _addOrganisationToProps(oOrganisationFromServer,oReferencedTaxonomies);
    let oRoleStore = SettingUtils.getRoleStore();
    oRoleStore.setSelectedRole({});
    oRoleStore.setRoleList(oSuccess.referencedRoles);
    OrganisationConfigViewProps.setOrganisationConfigTabId(Constants.ORGANISATION_CONFIG_INFORMATION);
    _setIsPermissionVisible(false);
    OrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(false);
    alertify.success(getTranslation().ORGANISATION_CREATED_SUCCESSFULLY);
    _triggerChange();
  };

  let failureCreateOrganisationConfigMapCallBack = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCreateOrganisationConfigMapCallBack", getTranslation);
  };

  let _addOrganisationToProps = function (oOrganisation, oReferencedTaxonomies) {
    let aOrganisationConfigMap = OrganisationConfigViewProps.getOrganisationConfigMap();
    aOrganisationConfigMap.push(oOrganisation);
    OrganisationConfigViewProps.setActiveOrganisation(oOrganisation);
    _resetOrganisationConfigListValueMap();
    let oOrganisationConfigValueMap = OrganisationConfigViewProps.getOrganisationConfigValueMap();
    let oDefaultListProps = SettingUtils.getDefaultListProps();
    oDefaultListProps.isSelected = true;
    oOrganisationConfigValueMap[oOrganisation.id] = oDefaultListProps;
    OrganisationConfigViewProps.setOrganisationConfigMap(aOrganisationConfigMap);
    OrganisationConfigViewProps.setOrganisationConfigValueMap(oOrganisationConfigValueMap);
    OrganisationConfigViewProps.setReferencedTaxonomies(oReferencedTaxonomies);
  };

  let _handleOrganisationSingleValueChanged = function (sKey, sValue) {
    let oActiveOrgConfig = _makeActiveOrganisationDirty();
    oActiveOrgConfig[sKey] = sValue;
  };

  let _handleSelectionToggleMultipleValueChanged = function (sKey, sValue) {
    let oActiveOrgConfig = _getActiveOrganisation();
    oActiveOrgConfig = oActiveOrgConfig.clonedObject || oActiveOrgConfig;
    let bSelectionExists = CS.includes(oActiveOrgConfig[sKey], sValue);
    if (sKey == "portals" && bSelectionExists && oActiveOrgConfig.portals.length === 1) {
      alertify.message(getTranslation().PORTAL_SELECTION_EMPTY_WARNING);
      return;
    }
    oActiveOrgConfig = _makeActiveOrganisationDirty();
    if (bSelectionExists) {
      CS.pull(oActiveOrgConfig[sKey], sValue);
    } else {
      oActiveOrgConfig[sKey].push(sValue);
    }
  };

  let _handleOrganisationMSSValueChanged = function (sKey, aValues) {
    let oActiveOrgConfig = _makeActiveOrganisationDirty();
    oActiveOrgConfig[sKey] = aValues;
    if (sKey === "klassIds") {
      _saveOrganisation({});
    }
  };

  let _handleOrganisationMSSValueRemoved = function (sKey, sId) {
    let oActiveOrgConfig = _makeActiveOrganisationDirty();
    CS.pull(oActiveOrgConfig[sKey], sId);
    if (sKey === "klassIds") {
      _saveOrganisation({});
    }
  };

  let _handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oTaxonomy, sActiveTaxonomyId) {
    let oActiveOrgConfig = _makeActiveOrganisationDirty();
    let aTaxonomyIds = oActiveOrgConfig.taxonomyIds;
    let iActiveTaxonomyIndex = aTaxonomyIds.indexOf(sActiveTaxonomyId);

    if (oTaxonomy.parent.id == SettingUtils.getTreeRootId()) {
      // aTaxonomyIds.splice(iActiveTaxonomyIndex, 1);
      CS.remove(aTaxonomyIds, function (sTaxonomyId) {
        return sTaxonomyId == sActiveTaxonomyId;
      })
    } else {
      aTaxonomyIds[iActiveTaxonomyIndex] = oTaxonomy.parent.id;
    }

    _saveOrganisation({});
  };

  let _handleTaxonomyAdded = function (sTaxonomyId, sParentTaxonomyId) {
    let oActiveOrgConfig = _makeActiveOrganisationDirty();
    CS.pull(oActiveOrgConfig.taxonomyIds, sParentTaxonomyId);
    oActiveOrgConfig.taxonomyIds.push(sTaxonomyId);
    let oCallback = {};
    _saveOrganisation(oCallback);
  };

  let _fetchOrganisation = function (sId, sTab) {
    let oData = {
      id: sId
    };

    SettingUtils.csGetRequest(oOrganisationConfigRequestMapping.Get, oData, successFetchOrganisationCallBack.bind(this, sTab), failureFetchOrganisationCallBack);
  };

  let successFetchOrganisationCallBack = function (sTab, oResponse) {
    let oSuccess = oResponse.success;
    let oOrganisationFromServer = oSuccess.organization;

    if(CS.isEmpty(oOrganisationFromServer.physicalCatalogs)){
      let aAllPhysicalCatalogs = MockDataForOrganisationConfigPhysicalCatalogAndPortal.physicalCatalogs();
      oOrganisationFromServer.physicalCatalogs = CS.map(aAllPhysicalCatalogs,'id');
    }

    if(CS.isEmpty(oOrganisationFromServer.portals)){
      let aAllPortals = MockDataForOrganisationConfigPhysicalCatalogAndPortal.portals();
      oOrganisationFromServer.portals = CS.map(aAllPortals,'id');
    }

    let oReferencedTaxonomies = oSuccess.referencedTaxonomies;
    let aOrganisationConfigMasterMap = OrganisationConfigViewProps.getOrganisationConfigMap();
    OrganisationConfigViewProps.setActiveOrganisation(oOrganisationFromServer);
    if(sTab == Constants.ORGANISATION_CONFIG_ROLES ){
      OrganisationConfigViewProps.setOrganisationConfigTabId(Constants.ORGANISATION_CONFIG_ROLES);
    }
    else {
      OrganisationConfigViewProps.setOrganisationConfigTabId(Constants.ORGANISATION_CONFIG_INFORMATION);
    }

    let oFoundOrganisation = CS.find(aOrganisationConfigMasterMap, {id: oOrganisationFromServer.id});
    oFoundOrganisation.label = oOrganisationFromServer.label;

    _resetOrganisationConfigListValueMap();
    let oOrganisationConfigValueMap = OrganisationConfigViewProps.getOrganisationConfigValueMap();
    oOrganisationConfigValueMap[oOrganisationFromServer.id].isSelected = true;
    OrganisationConfigViewProps.setReferencedTaxonomies(oReferencedTaxonomies);
    OrganisationConfigViewProps.setReferencedKlasses(oSuccess.referencedKlasses);

    let oRoleStore = SettingUtils.getRoleStore();
    let oReferencedRoles = oSuccess.referencedRoles;
    oRoleStore.setRoleList(oReferencedRoles);

    if (!CS.isEmpty(oReferencedRoles)) {
      let aSortedRoleList = CS.sortBy(oReferencedRoles, (oRole) => {
        return oRole.label || oRole.code;
      });
      if (!CS.isEmpty(aSortedRoleList)) {
        oRoleStore.getRoleDetails(aSortedRoleList[0].id);
      }
    }
    oRoleStore.setSelectedRole({});
    _setIsPermissionVisible(false);
    _triggerChange();
  };

  let failureFetchOrganisationCallBack = function (oCallbackData, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "OrganizationNotFoundException"}))) {
        let oOrganisationConfigMap = OrganisationConfigViewProps.getOrganisationConfigMap();
        alertify.error("[" + oOrganisationConfigMap[oCallbackData.id].label + "] " + getTranslation().ERROR_ALREADY_DELETED);
        SettingUtils.removeOrganisationsFromProps([oCallbackData.id]);
        _triggerChange();
      } else {
        SettingUtils.failureCallback(oResponse, "failureFetchOrganisationCallBack", getTranslation);
      }
    } else {
      ExceptionLogger.error("failureFetchOrganisationCallBack: Something went wrong" + oResponse);
    }
  };

  let _fetchTaxonomyById = function (sContext, sTaxonomyId) {
    let oParameters = {};
    if (sContext == "majorTaxonomy") {
      if (sTaxonomyId == "addItemHandlerforMultiTaxonomy") {
        sTaxonomyId = SettingUtils.getTreeRootId();
      } else {
        sTaxonomyId = sTaxonomyId;
      }
    }
    oParameters.id = sTaxonomyId;

    let oTaxonomyPaginationData = OrganisationConfigViewProps.getTaxonomyPaginationData();
    let oPostData = CS.assign({taxonomyId: sTaxonomyId}, oTaxonomyPaginationData);

    SettingUtils.csPostRequest(oSettingScreenRequestMapping.GetAllTaxonomy, {}, oPostData, successFetchFetchTaxonomy, failureFetchClassListCallback);
  };

  let successFetchFetchTaxonomy = function (oResponse) {
    let oSuccess = oResponse.success;
    let aTaxonomyListFromServer = oSuccess.list;
    let oConfigDetails = oSuccess.configDetails;

    let aTaxonomyList = OrganisationConfigViewProps.getAllowedTaxonomies();
    OrganisationConfigViewProps.setAllowedTaxonomies(CS.concat(aTaxonomyList, aTaxonomyListFromServer));
    let oAllowedTaxonomyHierarchyListData = CommonUtils.prepareTaxonomyHierarchyData(OrganisationConfigViewProps.getAllowedTaxonomies(), oConfigDetails);
    OrganisationConfigViewProps.setAllowedTaxonomyHierarchyList(oAllowedTaxonomyHierarchyListData);
    _triggerChange();
  };

  let failureFetchClassListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchOrganisationConfigCallBack", getTranslation);
  };

  let _generateADMToSave = function (oOldOrgConfig, oNewOrgConfig) {
    let oSystemsMap = SettingUtils.getAppData().getSystemsMap();
    let oADM = {};
    oADM.addedTaxonomyIds = CS.difference(oNewOrgConfig.taxonomyIds, oOldOrgConfig.taxonomyIds);
    oADM.deletedTaxonomyIds = CS.difference(oOldOrgConfig.taxonomyIds, oNewOrgConfig.taxonomyIds);

    oADM.addedKlassIds = CS.difference(oNewOrgConfig.klassIds, oOldOrgConfig.klassIds);
    oADM.deletedKlassIds = CS.difference(oOldOrgConfig.klassIds, oNewOrgConfig.klassIds);

    oADM.addedEndpointIds = CS.difference(oNewOrgConfig.endpointIds, oOldOrgConfig.endpointIds);
    oADM.deletedEndpointIds = CS.difference(oOldOrgConfig.endpointIds, oNewOrgConfig.endpointIds);

    let aOldSystems = oOldOrgConfig.systems || [];
    let aNewSystems = oNewOrgConfig.systems || [];
    oADM.addedSystemIds = CS.difference(aNewSystems, aOldSystems);
    oADM.deletedSystemIds = CS.difference(aOldSystems, aNewSystems);

    CS.forEach(oNewOrgConfig.systems, function (sSystemId) {
      let aSystemEndpointIds = CS.map(oSystemsMap[sSystemId].list, "id");
      let aDifference = CS.difference(aSystemEndpointIds, oNewOrgConfig.endpointIds);
      if (aDifference.length === aSystemEndpointIds.length) {
        if (!CS.includes(oADM.addedSystemIds, sSystemId)) {
          oADM.deletedSystemIds.push(sSystemId);
        } else {
          CS.pull(oADM.addedSystemIds, sSystemId);
        }
      }
    });

    return oADM;
  };

  let _saveOrganisation = function (oCallbackData) {
    let oActiveOrganisation = OrganisationConfigViewProps.getActiveOrganisation();
    let oNewOrganisation = oActiveOrganisation.clonedObject;
    if(CS.isEmpty(oNewOrganisation)){
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }
    let oADM = _generateADMToSave(oActiveOrganisation, oNewOrganisation);
    oADM.id = oNewOrganisation.id;
    oADM.label = oNewOrganisation.label;
    oADM.type = oNewOrganisation.type;
    oADM.icon = oNewOrganisation.icon;
    let aAllPhysicalCatalogs = MockDataForOrganisationConfigPhysicalCatalogAndPortal.physicalCatalogs();
    if(oNewOrganisation.physicalCatalogs.length == aAllPhysicalCatalogs.length){
        oADM.physicalCatalogs = [];
    }else{
        oADM.physicalCatalogs = oNewOrganisation.physicalCatalogs;
    }

    let aAllPortals = MockDataForOrganisationConfigPhysicalCatalogAndPortal.portals();
    if(oNewOrganisation.portals.length == aAllPortals.length){
      oADM.portals = [];
    }else{
      oADM.portals = oNewOrganisation.portals;
    }

    let oCallback = {};
    CS.assign(oCallback, oCallbackData);

    SettingUtils.csPostRequest(oOrganisationConfigRequestMapping.Save, {}, oADM, successSaveOrganisationCallBack.bind(this, oCallback), failureSaveOrganisationCallBack);
  };

  let successSaveOrganisationCallBack = function (oCallbackData, oResponse) {
    OrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(false);
    let oSuccess = oResponse.success;
    let oOrganisationFromServer = oSuccess.organization;

    if(CS.isEmpty(oOrganisationFromServer.physicalCatalogs)){
        let aAllPhysicalCatalogs = MockDataForOrganisationConfigPhysicalCatalogAndPortal.physicalCatalogs();
        oOrganisationFromServer.physicalCatalogs = CS.map(aAllPhysicalCatalogs,'id');
    }

    if(CS.isEmpty(oOrganisationFromServer.portals)){
        let aAllPortals = MockDataForOrganisationConfigPhysicalCatalogAndPortal.portals();
        oOrganisationFromServer.portals = CS.map(aAllPortals,'id');
    }
    let aOrganisationConfigMap = OrganisationConfigViewProps.getOrganisationConfigMap();
    let oFoundOrganisation = CS.find(aOrganisationConfigMap, {id: oOrganisationFromServer.id});
    oFoundOrganisation.label = oOrganisationFromServer.label;

   /* let oOrganisationConfigMap = OrganisationConfigViewProps.getOrganisationConfigMap();
    oOrganisationConfigMap[oOrganisationFromServer.id] = oOrganisationFromServer;*/

    OrganisationConfigViewProps.setActiveOrganisation(oOrganisationFromServer);
    OrganisationConfigViewProps.setReferencedTaxonomies(oSuccess.referencedTaxonomies);
    OrganisationConfigViewProps.setReferencedKlasses(oSuccess.referencedKlasses);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY, { entity : getTranslation().ORGANISATION } ));
    oCallbackData.functionToExecute && oCallbackData.functionToExecute();
    _triggerChange();
  };

  let failureSaveOrganisationCallBack = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "OrganizationNotFoundException"}))) {
        let oActiveOrganisation = OrganisationConfigViewProps.getActiveOrganisation();
        alertify.error("[" + oActiveOrganisation.label + "] " + getTranslation().ERROR_ALREADY_DELETED);
        SettingUtils.removeOrganisationsFromProps([oActiveOrganisation.id]);
        _triggerChange();
      } else {
        SettingUtils.failureCallback(oResponse, "failureSaveOrganisationCallBack", getTranslation);
      }
    } else {
      ExceptionLogger.error("failureSaveOrganisationCallBack: Something went wrong" + oResponse);
    }
  };

  let successLoadSystemListCallback = function (oResponse) {
    let aSystems = oResponse.success;

    let aEndPointList = SettingUtils.getAppData().getProfileList();
    let oSystemEndPoints = CS.groupBy(aEndPointList, 'systemId');

    CS.forEach(aSystems, function (oSystem) {
      oSystem.list = oSystemEndPoints[oSystem.id] || [];
    });

    SettingUtils.getAppData().setSystemsList(aSystems);
    SettingUtils.getAppData().setSystemsMap(aSystems);

    _triggerChange();
  };

  let failureLoadSystemListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureLoadSystemListCallback", getTranslation);
  };

  let _discardUnsavedOrganisation = function (oCallbackData) {
    let oActiveOrganisationConfig = OrganisationConfigViewProps.getActiveOrganisation();
    delete oActiveOrganisationConfig.clonedObject;
    delete oActiveOrganisationConfig.isDirty;
    OrganisationConfigViewProps.setActiveOrganisation(oActiveOrganisationConfig);
    OrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(false);
    OrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(false);
    oCallbackData && oCallbackData.functionToExecute && oCallbackData.functionToExecute();
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    _triggerChange();
  };

  let _deleteOrganisation = function (aSelectedIds, oCallback) {
    let oCallbackData = {
      functionToExecute: SettingUtils.removeOrganisationsFromProps.bind(this, aSelectedIds)
    };
    SettingUtils.csDeleteRequest(oOrganisationConfigRequestMapping.Save, {}, {ids: aSelectedIds}, successDeleteOrganisationCallBack.bind(this, oCallbackData), failureDeleteOrganisationCallBack.bind(this, oCallback));
  };

  let successDeleteOrganisationCallBack = function (oCallbackData, oResponse) {
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().ORGANISATION}));
    if (CS.isFunction(oCallbackData.functionToExecute)) {
      oCallbackData.functionToExecute(oResponse);
    }
    /*Removing lock screen status after deleting organisation in which there exists unsaved changes in role */
    let RoleStore = SettingUtils.getRoleStore();
    RoleStore.setSelectedRole({});
    RoleStore.setRoleScreenLockStatus(false);

    _triggerChange();
  };

  let failureDeleteOrganisationCallBack = function (oCallback, oResponse) {
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
    SettingUtils.failureCallback(oResponse, "failureDeleteOrganisationCallBack", getTranslation);
  };

  let _loadSystemList = function () {

    let oPostData = {
      searchText: '',
      from: 0,
      size: 200
    };

    SettingUtils.csPostRequest(oOrganisationConfigRequestMapping.GetSystems, {}, oPostData, successLoadSystemListCallback, failureLoadSystemListCallback);
  };

  let _saveLockedOrganisationConfigScreen = function (oCallBack) {
    /** organisation screen consist of Roles and permission screen hence need to check lock status for each and
     *  save/discard accordingly as we cannot blindly send callback to all three of them**/
    let RoleStore = SettingUtils.getRoleStore();
    let PermissionStore = SettingUtils.getPermissionStore();
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContext.SSO_SETTING);
    let bIsSSOSettingConfigDirty = oGridViewProps && oGridViewProps.getIsGridDataDirty() || false;
    if (RoleStore.getRoleScreenLockStatus()) {
      RoleStore.saveRole(oCallBack);
    } else if (OrganisationConfigViewProps.getOrganisationConfigScreenLockStatus()) {
      OrganisationConfigStore.saveOrganisationDetailConfig(oCallBack);
    } else if (PermissionStore.getPermissionScreenLockStatus()) {
      PermissionStore.savePermission(oCallBack);
    }
    else if (bIsSSOSettingConfigDirty) {
      SSOSettingStore.postProcessSSOListAndSave(oCallBack);
    }
    else {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
    }
  };

  let _discardLockedOrganisationConfigScreen = function (oCallBack) {
    /** organisation screen consist of Roles and permission screen hence need to check lock status for each and
     *  save/discard accordingly as we cannot blindly send callback to all three of them**/
    let RoleStore = SettingUtils.getRoleStore();
    let PermissionStore = SettingUtils.getPermissionStore();
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContext.SSO_SETTING);
    let bIsSSOSettingConfigDirty = oGridViewProps && oGridViewProps.getIsGridDataDirty() || false;
    if (RoleStore.getRoleScreenLockStatus()) {
      RoleStore.discardUnsavedRole(oCallBack);
    } else if (OrganisationConfigViewProps.getOrganisationConfigScreenLockStatus()) {
      OrganisationConfigStore.discardOrganisationDetailConfig(oCallBack);
    } else if (PermissionStore.getPermissionScreenLockStatus()) {
      PermissionStore.discardUnsavedPermission(oCallBack);
    }
    else if (bIsSSOSettingConfigDirty) {
      SSOSettingStore.discardSSOGridViewChanges(oCallBack);
    }
  };

  let _showLockedOrganisationConfigScreenAlertify = function (oCallbackData) {
    CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
        _saveLockedOrganisationConfigScreen.bind(this, oCallbackData),
        _discardLockedOrganisationConfigScreen.bind(this, oCallbackData),
        function () {
        });
  };

  let _isOrganisationConfigScreenLocked = function () {
    let RoleStore = SettingUtils.getRoleStore();
    let PermissionStore = SettingUtils.getPermissionStore();
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContext.SSO_SETTING);
    let bIsSSOSettingConfigDirty = oGridViewProps && oGridViewProps.getIsGridDataDirty() || false;
    return OrganisationConfigViewProps.getOrganisationConfigScreenLockStatus() || RoleStore.getRoleScreenLockStatus() || PermissionStore.getPermissionScreenLockStatus() || bIsSSOSettingConfigDirty;
  };

/*
  var _handleOrganisationConfigListNodeChecked = function (oModel) {
    var oOrganisationValueMap = OrganisationConfigViewProps.getOrganisationConfigValueMap();
    var oProcessItemValue = oOrganisationValueMap[oModel.id];
    oProcessItemValue.isChecked = SettingUtils.invertCheckedValue(oProcessItemValue.isChecked);
  };
*/

  var _getCheckedAndSelectedIds = function () {
    var oOrganisationValueMap = OrganisationConfigViewProps.getOrganisationConfigValueMap();
    let aCheckedListIds = [];
    CS.forEach(oOrganisationValueMap, function (oData, sId) {
      if (oData.isChecked || oData.isSelected) {
        aCheckedListIds.push(sId);
      }
    });
    return aCheckedListIds;
  };

  var _createOrganisationDialogClick = function () {
    var oActiveOrganisation = OrganisationConfigViewProps.getActiveOrganisation();
    oActiveOrganisation = oActiveOrganisation.clonedObject || oActiveOrganisation;
    var oCodeToVerifyUniqueness = {
      id: oActiveOrganisation.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_ORGANISATION
    };

    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = function(){
      delete oActiveOrganisation.isCreated;
      _createOrganisationCall(oActiveOrganisation);
    };
    var sURL = oOrganisationConfigRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  var _createOrganisationCall = function (oActiveOrganisation) {
    SettingUtils.csPutRequest(oOrganisationConfigRequestMapping.Create, {}, oActiveOrganisation, successCreateOrganisationConfigMapCallBack, failureCreateOrganisationConfigMapCallBack);
  };

  var _cancelOrganisationDialogClicked = function () {
    let oOrganisationConfigValueMap = OrganisationConfigViewProps.getOrganisationConfigValueMap();
    let sSelectedOrganizationId = CS.findKey(oOrganisationConfigValueMap, {isSelected: true});
    sSelectedOrganizationId && _fetchOrganisation(sSelectedOrganizationId);
    OrganisationConfigViewProps.setActiveOrganisation({});
    OrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(false);
    _triggerChange();
  };

  var _handleOrganisationConfigDialogButtonClick = function (sButtonId) {
    if (sButtonId == "create") {
      _createOrganisationDialogClick();
    } else {
      _cancelOrganisationDialogClicked();
    }
  };

  let _handleListViewSearchOrLoadMoreClicked = function (sSearchText, bLoadMore, sContext) {
    if (sContext === "role") {
      let oRoleValueList = RoleProps.getRoleValuesList();
      let oList = {};
      CS.forEach(oRoleValueList, function (obj) {
        if (CS.includes(CS.toLower(obj.label), CS.toLower(sSearchText))) {
          oList[obj.id] = obj;
        }
      });
      RoleProps.setSearchedRoleValuesList(oList);
      RoleProps.setRoleSearchText(sSearchText);
      _triggerChange();
    } else {
      let oOrganisationConfigMap = OrganisationConfigViewProps.getOrganisationConfigMap();
      SettingUtils.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, oOrganisationConfigMap, OrganisationConfigViewProps, _fetchOrganisationConfigMap);
    }
  };

  let _fetchSystemsForOrganization = function (sOrganizationId, oCallbackData) {
    let oRequestData = {
      id: sOrganizationId,
    };

    SettingUtils.csGetRequest(oOrganisationConfigRequestMapping.GetSystemsForOrganization, oRequestData, successSystemsForOrganization.bind(this, oCallbackData), failureSystemsForOrganization);
  };

  let successSystemsForOrganization = function (oCallbackData, oResponse) {
    let oAppData = SettingUtils.getAppData();
    let oSuccess = oResponse.success;
    oAppData.setSystemsForRoles(oSuccess);
    oAppData.setSystemsMapForRoles(oSuccess);
    if (!CS.isEmpty(oCallbackData)) {
      oCallbackData.functionToExecute && oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let failureSystemsForOrganization =function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSystemsForOrganization", getTranslation);
  };

  let _handleSystemSelectApplyClicked = function (aSelectedSystemIds) {
    let oSystemsMap = SettingUtils.getAppData().getSystemsMap();
    let oActiveOrganization = OrganisationConfigViewProps.getActiveOrganisation();
    oActiveOrganization = _makeActiveOrganisationDirty();

    let aSystemsToAdd = CS.difference(aSelectedSystemIds, oActiveOrganization.systems);
    if (CS.isEmpty(aSystemsToAdd)) {
      let aSystemsToRemove = CS.difference(oActiveOrganization.systems, aSelectedSystemIds);
      CS.pullAll(oActiveOrganization.systems, aSystemsToRemove);
    } else {
      oActiveOrganization.systems.push(...aSystemsToAdd);
    }

    CS.forEach(aSystemsToAdd, function (sSystemId) {
      let aEndpointIds = CS.map(oSystemsMap[sSystemId].list, "id");
      oActiveOrganization.endpointIds.push(...aEndpointIds);
    });
  };

  let _handleEndpointSelectionViewSelectAllChecked = function (sSystemId, iCheckboxStatus) {
    let oSystemsMap = SettingUtils.getAppData().getSystemsMap();
    let aSystemEndpointIds = CS.map(oSystemsMap[sSystemId].list, "id");
    let oActiveOrganization = OrganisationConfigViewProps.getActiveOrganisation();
    if (!oActiveOrganization.clonedObject) {
      _makeActiveOrganisationDirty();
    }
    oActiveOrganization = oActiveOrganization.clonedObject || oActiveOrganization;
    let aOrganizationEndpointIds = oActiveOrganization.endpointIds;

    iCheckboxStatus = (iCheckboxStatus + 1) % 3;

    if (iCheckboxStatus === 0) {
      oActiveOrganization.endpointIds =CS.difference(aOrganizationEndpointIds, aSystemEndpointIds);
    } else {
      let aDifference = CS.difference(aSystemEndpointIds, aOrganizationEndpointIds);
      oActiveOrganization.endpointIds.push(...aDifference);
    }
  };

  let _handleEndpointSelectionViewDeleteClicked = function (sSystemId) {
    let oSystemsMap = SettingUtils.getAppData().getSystemsMap();
    let aEndpoints = CS.map(oSystemsMap[sSystemId].list, "id");
    let oActiveOrganization = _makeActiveOrganisationDirty();
    CS.pull(oActiveOrganization.systems, sSystemId);
    oActiveOrganization.endpointIds = CS.difference(oActiveOrganization.endpointIds, aEndpoints);
  };

  let _handleOrganisationUploadIconChangeEvent = function (sIconKey, sIconObjectKey) {
    OrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(true);
    let oActiveOrganisation = _makeActiveOrganisationDirty();
    oActiveOrganisation.icon = sIconKey;
    oActiveOrganisation.iconKey = sIconObjectKey;
    oActiveOrganisation.showSelectIconDialog = false;
  };

  let _handleOrganisationTypeSelected = function (aSelected) {
    let oActiveOrganisation = _makeActiveOrganisationDirty();
    oActiveOrganisation.type = aSelected[0];
  };

  var _getIsValidFileTypes = function (oFile) {
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var uploadFileImport = function (oImportExcel, oCallback) {
    return SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  var successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
    return true;
  };


  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
    return false;
  };

  var _handleOrganisationImportButtonClicked = function (aFiles,oImportExcel) {
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

  var  _handleOrganisationExportButtonClicked = function (oSelectiveExport) {
    return SettingUtils.csPostRequest(oSelectiveExport.sUrl, {}, oSelectiveExport.oPostRequest, successExportFile, failureExportFile);
  };

  var successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName)
    return true;
  };

  var failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
    return false;
  };

  let  _handlePartnersManageEntityButtonClicked = function () {
    let aSelectedIds = _getCheckedAndSelectedIds();
    let sType = OrganisationConfigViewProps.getOrganisationConfigTabId();

    if (CS.isEmpty(aSelectedIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED);
      return
    }
    else {
      ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedIds, sType);
    }
  };

  let deleteUnusedOrganisation = function (aSelectedIds, oCallback) {
    //let bCanDeleteEntity = ManageEntityConfigProps.getDataForDeleteEntity();
    if (CS.isEmpty(aSelectedIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return
    }

    let aOrganisationConfigList = OrganisationConfigViewProps.getOrganisationConfigMap(); //todo : rename
    let bIsAnyStandardOrgSelected = false;
    CS.forEach(aSelectedIds, function (sId) {
      let oFoundOrganization = CS.find(aOrganisationConfigList, {id: sId});
      bIsAnyStandardOrgSelected = oFoundOrganization.isStandard;
      return !bIsAnyStandardOrgSelected;
    });
    if (bIsAnyStandardOrgSelected) {
      alertify.message(getTranslation().STANDARD_ORGANISATION_DELETION);
      return;
    }

    let aSelectedOrganisationLabel = [];
    let aOrganisationConfigMap = OrganisationConfigViewProps.getOrganisationConfigMap();
    CS.forEach(aSelectedIds, function (sSelectedId) {
      let sSelectedOrganisationLabel = CS.getLabelOrCode(CS.find(aOrganisationConfigMap, {id: sSelectedId}));
      aSelectedOrganisationLabel.push(sSelectedOrganisationLabel);
    });
    //sSelectedOrganisationLabel = CS.trimEnd(sSelectedOrganisationLabel, ', ');
    //if(!bCanDeleteEntity) {
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aSelectedOrganisationLabel,
          function () {
            _deleteOrganisation(aSelectedIds, oCallback);
          }, function (oEvent) {
          }, true);
    //}
  };

  /***************** PUBLIC API's **************/

  return {
    reset: function () {
      OrganisationConfigViewProps.reset();
    },

    createOrganisation: function () {
      if (_isOrganisationConfigScreenLocked()) {
        _showLockedOrganisationConfigScreenAlertify({functionToExecute: this.createOrganisation});
      } else {
        _createOrganisation();
        _triggerChange();
      }
    },

    deleteOrganisation: function (sId) {
      let aSelectedIds = [sId] || _getCheckedAndSelectedIds();
      let sType = OrganisationConfigViewProps.getOrganisationConfigTabId();
      //let oCallback = {functionToExecute: deleteUnusedOrganisation.bind(this, aSelectedIds)}
      let oOrganisationConfigList = OrganisationConfigViewProps.getOrganisationConfigMap(); //todo : rename
      let bPresentStandardAttribute = false;
      // CS.forEach(oOrganisationConfigList, function (sId) {
      //   if (aSelectedIds.indexOf(sId.id) != -1 && sId.isStandard) {
      //     bPresentStandardAttribute = true;
      //   }
      // });
      // if(bPresentStandardAttribute){
      //   deleteUnusedOrganisation(aSelectedIds);
      // }
      // else {
      //   ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedIds, sType, oCallback);
      // }
      deleteUnusedOrganisation(aSelectedIds, {functionToExecute:  ManageEntityStore.handleManageEntityDialogOpenButtonClicked.bind(this, aSelectedIds, sType)});
    },

    refreshOrganisationConfig: function (sTab) {
      if (_isOrganisationConfigScreenLocked()) {
        _showLockedOrganisationConfigScreenAlertify({functionToExecute: this.refreshOrganisationConfig});
      } else {
        let oActiveOrganisation = _getActiveOrganisation();
        if (CS.isEmpty(oActiveOrganisation)) {
          _fetchOrganisationConfigMap();
        } else {
          let sId = oActiveOrganisation.id;
          _fetchOrganisation(sId, sTab)
        }
      }
    },

    fetchOrganisationConfigMap: function () {
      _fetchOrganisationConfigMap();
    },

    fetchOrganisation: function (sId) {
      _fetchOrganisation(sId);
    },

    handleOrganisationTabChanged: function (sTabId) {
      let fFunctionToExecute = function () {
        OrganisationConfigViewProps.setOrganisationConfigTabId(sTabId);
        _setIsPermissionVisible(false);
      };

      let RoleStore = SettingUtils.getRoleStore();
      let sActiveRoleId = RoleStore.getActiveRoleId();
      if (sTabId === Constants.ORGANISATION_CONFIG_ROLES) {
        if (!CS.isEmpty(sActiveRoleId)) {
          let oCallBack = {
            functionToExecute: fFunctionToExecute
          };
        RoleStore.getRoleDetails(sActiveRoleId, oCallBack);
        }
        let oCallbackData = {
          functionToExecute: fFunctionToExecute,
        };
        let oActiveOrganizaton = OrganisationConfigViewProps.getActiveOrganisation();
        _fetchSystemsForOrganization(oActiveOrganizaton.id, oCallbackData);
      }
      else if (sTabId === Constants.ORGANISATION_CONFIG_SSO_SETTING) {
        let oCallbackData = {
          functionToExecute: fFunctionToExecute,
        };
        let SSOSettingStore = SettingUtils.getSSOSettingStore();
        SSOSettingStore.setUpSSOSettingConfigGridView(oCallbackData);
      }
      else {
        fFunctionToExecute();
        _triggerChange();
      }
    },

    getActiveOrganisation: function () {
      return _getActiveOrganisation();
    },

    handleRolePermissionToggleButtonClicked: function () {
      _handleRolePermissionToggleButtonClicked();
    },

    isPermissionVisible: () => {
      return _isPermissionVisible();
    },

    setIsPermissionVisible: (bValue) => {
      _setIsPermissionVisible(bValue);
    },

    handleOrganisationSingleValueChanged: function (sKey, sValue) {
      _handleOrganisationSingleValueChanged(sKey, sValue);
      _triggerChange();
    },

    handleOrganisationMSSValueChanged: function (sKey, aValues) {
      _handleOrganisationMSSValueChanged(sKey, aValues);
      _triggerChange();
    },

    handleOrganisationMSSValueRemoved: function (sKey, sId) {
      _handleOrganisationMSSValueRemoved(sKey, sId);
      _triggerChange();
    },

    handleSelectionToggleButtonClicked: function (sKey, sValue, bIsSingleSelect) {
      bIsSingleSelect ? _handleOrganisationSingleValueChanged(sKey, sValue) : _handleSelectionToggleMultipleValueChanged(sKey, sValue);
      _triggerChange();
    },

    saveOrganisationDetailConfig: function (oCallBack) {
      _saveOrganisation(oCallBack)
    },

    discardOrganisationDetailConfig: function (oCallBack) {
      _discardUnsavedOrganisation(oCallBack);
    },

    handleOrganisationConfigListNodeClicked: function (sId) {
      let bLockScreenStatus = _isOrganisationConfigScreenLocked();
      let fFunctionToExecute = _fetchOrganisation.bind(this, sId);
      let oCallBack = {
        functionToExecute: fFunctionToExecute
      };
      if (bLockScreenStatus) {
        _showLockedOrganisationConfigScreenAlertify(oCallBack);
      } else {
        fFunctionToExecute();
      }
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId) {
      let sTabId = OrganisationConfigViewProps.getOrganisationConfigTabId();
      if (sTabId == Constants.ORGANISATION_CONFIG_ROLES) {
        let oRoleStore = SettingUtils.getRoleStore();
        oRoleStore.handleAllowedTaxonomiesDropdownOpened(sTaxonomyId);
      } else {
        OrganisationConfigViewProps.setAllowedTaxonomies([]);
        let oTaxonomyPaginationData = OrganisationConfigViewProps.getTaxonomyPaginationData();
        oTaxonomyPaginationData.searchText = "";
        oTaxonomyPaginationData.from = 0;
        _fetchTaxonomyById(sContext, sTaxonomyId);
      }
    },

    handleTaxonomySearchClicked: function (sContext, sTaxonomyId, sSearchText) {
      OrganisationConfigViewProps.setAllowedTaxonomies([]);
      let oTaxonomyPaginationData = OrganisationConfigViewProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.from = 0;
      oTaxonomyPaginationData.searchText = sSearchText;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleTaxonomyLoadMoreClicked: function (sContext, sTaxonomyId) {
      let oTaxonomyPaginationData = OrganisationConfigViewProps.getTaxonomyPaginationData();
      let aTaxonomyList = OrganisationConfigViewProps.getAllowedTaxonomies();
      oTaxonomyPaginationData.from = aTaxonomyList.length;
      _fetchTaxonomyById(sContext, sTaxonomyId);
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sParentTaxonomyId) {
      _handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId);
    },

    handleTaxonomyAdded: function (sTaxonomyId,sParentTaxonomyId) {
      _handleTaxonomyAdded(sTaxonomyId,sParentTaxonomyId);
    },

    loadSystemList: function () {
      _loadSystemList();
    },

    showLockedOrganisationConfigScreenAlertify: function (oCallBack) {
      // For Organisation Basic, Role Details , Permissions and templates
      _showLockedOrganisationConfigScreenAlertify(oCallBack);
    },

    saveLockedOrganisationConfigScreen: function (oCallBack) {
      _saveLockedOrganisationConfigScreen(oCallBack);
    },

    discardLockedOrganisationConfigScreen: function (oCallBack) {
      _discardLockedOrganisationConfigScreen(oCallBack);
    },

    getOrganisationConfigScreenLockStatus: function () {
      return _isOrganisationConfigScreenLocked();
    },

/*
    handleOrganisationConfigListNodeChecked: function (oModel) {
      _handleOrganisationConfigListNodeChecked(oModel);
      _triggerChange();
    },
*/

    handleOrganisationConfigDialogButtonClick: function (sButtonId) {
      _handleOrganisationConfigDialogButtonClick(sButtonId);
    },

    handleListViewSearchOrLoadMoreClicked: function (sSearchText, bLoadMore, sContext) {
      _handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, sContext);
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

    handleOrganisationUploadIconChangeEvent: function (sIconKey, sIconObjectKey) {
      _handleOrganisationUploadIconChangeEvent(sIconKey, sIconObjectKey);
      _triggerChange();
    },

    handleOrganisationTypeSelected: function (aSelectedItems) {
      _handleOrganisationTypeSelected(aSelectedItems);
      _triggerChange();
    },

    handleOrganisationImportButtonClicked: function (aFiles,oImportExcel) {
      _handleOrganisationImportButtonClicked(aFiles,oImportExcel);
    },

    handlePartnersManageEntityButtonClicked: function () {
      _handlePartnersManageEntityButtonClicked();
    },

    handleOrganisationExportButtonClicked: function (oSelectiveExport) {
      _handleOrganisationExportButtonClicked(oSelectiveExport);
    }
  }
})();

MicroEvent.mixin(OrganisationConfigStore);

export default OrganisationConfigStore;
