import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import { PermissionRequestMapping, PropertyCollectionRequestMapping as oPropertyCollectionRequestMapping }
  from '../../tack/setting-screen-request-mapping';
import EntityTreeNavigationTreeData from '../../tack/permission-config-entity-tree-data';
import RolePermissionFunctionLayoutData from '../../tack/role-permission-function-data';
import PermissionProps from './../model/permission-config-view-props';
import RoleProps from './../model/role-config-view-props';
import TemplateProps from './../model/template-config-view-props';
import oPermissionEntityDetailsMap from '../../tack/permission-entity-details-map';
import ClassCategoryConstants from '../../tack/class-category-constants-dictionary';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import PermissionEntityTypeDictionary from '../../../../../../commonmodule/tack/permission-entity-type-dictionary';
import SettingUtils from './../helper/setting-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import oClassCategoryConstants from '../../tack/class-category-constants-for-role-dictionary';
import TableViewStore from './table-view-store'
import TableViewPropertyCollectionSkeleton from '../../tack/table-view-property-collection-skeleton'
import TableViewRelationshipSkeleton from '../../tack/table-view-relationship-data-skeleton'
import TableViewGeneralInformationSkeleton from '../../tack/table-view-general-information-skeleton'
import ContextualTableViewProps from "../model/contextual-table-view-props";
import {oTableRenderTypeConstant} from "../../tack/table-render-type";
import CommonUtils from "../../../../../../commonmodule/util/common-utils";
import TaxonomyTypeDictionary from "../../../../../../commonmodule/tack/taxonomy-type-dictionary";
import UniqueIdentifierGenerator
  from "../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator";
import assetTypes from "../../tack/coverflow-asset-type-list";
import ClassCategoryConstantsForPermissions from '../../tack/class-category-constants-dictionary-for-permissions';
import RolePermissionFunctionBulkEditLayoutData from "../../tack/role-permission-function-bulk-edit-data";

var PermissionStore = (function () {

  var _triggerChange = function () {
    PermissionStore.trigger('permission-changed');
  };

  var _updatePermissionResponse = function (oPermission) {
    var aClassesList = ["articles", "assets", "markets", "suppliers", /*"targets", */"textAssets"];
    var aDummyClassList = [];
    var oClassVisibilityStatus = PermissionProps.getClassVisibilityStatus();
    CS.forEach(aClassesList, function (sClass) {
      var aChildren = oPermission[sClass];
      let PermissionEntityDetailsMap = new oPermissionEntityDetailsMap();
      var oDummyClass = PermissionEntityDetailsMap[sClass];
      oDummyClass.children = aChildren;
      oDummyClass.globalPermission = {};
      aDummyClassList.push(oDummyClass);
      if(!oClassVisibilityStatus[sClass]) {
        oClassVisibilityStatus[sClass] = false;
      }
      delete oPermission[sClass];
    });
    oPermission["classes"] = aDummyClassList;
  };

  let _setActivePermission = (oPermission) => {
    PermissionProps.setActivePermission(oPermission);
  };

  let _showScreenLockedAlertify = function (oCallBack) {
    CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
        _savePermission.bind(this, oCallBack),
        _discardUnsavedPermission.bind(this, oCallBack),
        function () {
        });
  };

  var successFetchPermissionByIdCallback = function (oCallbackData, sRoleId, oResponse) {
    var oPermission = oResponse.success;
    // var sRoleId = oPermission.id;
    _updatePermissionResponse(oPermission);
    _selectPermissionById(sRoleId);
    PermissionProps.reset();
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    let oUIProperties = {hideUpdateButton: true, hideReadButton: true};

    CS.forEach(oPermission.classes, function (oClassGroup) {
      CS.forEach(oClassGroup.children, function (oChild) {
        oChild.uiProperties = oUIProperties;
      });
    });
    CS.forEach(oPermission.taxonomies, function (oTaxonomy) {
      oTaxonomy.uiProperties = oUIProperties;
    });

    PermissionProps.setActivePermission(oPermission);
    PermissionProps.setActiveRoleId(sRoleId);
    _triggerChange();
  };

  var successSaveTemplatePermissionCallback = function (oCallbackData, oResponse) {
    let oTemplate = oResponse.success;
    /*var oActivePermission = PermissionProps.getActivePermission();
    oActivePermission.templateInPermission = oTemplate;*/
    // PermissionProps.reset(true);
    // delete oActivePermission.clonedObject;
    /*var oActivePermissionClone = oActivePermission.clonedObject;
    if(!!oActivePermissionClone){
      var oTempActivePermissionClone = CS.cloneDeep(oActivePermission);
      delete oTempActivePermissionClone.clonedObject;
      if(CS.isEqual(oTempActivePermissionClone, oActivePermissionClone)){
        delete oActivePermission.clonedObject;
      }
    }*/

    //TODO: (Temporary fix)refactor - assign whole new headerpermission object into active permission.
    let oActivePermission = PermissionProps.getActivePermission();
    let oTemplateInPermission = oActivePermission.templateInPermission;
    let oTemplatePermissions = oTemplateInPermission.templatePermission;
    let oHeaderPermission = oTemplatePermissions.headerPermission;
    let oHeaderPermissionFromServer = oTemplate.permission.headerPermission;
    if(CS.isNotEmpty(oHeaderPermissionFromServer)) {
      oHeaderPermission.id = oHeaderPermissionFromServer.id;
      oHeaderPermission.code = oHeaderPermissionFromServer.code;
    }
    PermissionProps.setPermissionScreenLockStatus(false);
    PermissionProps.setIsPermissionDirty(false);
    PermissionProps.setIsPermissionModeClass(false);
    delete PermissionProps.getActivePermission().clonedObject;

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  var failureFetchPermissionByIdCallback = function (oResponse) {
    PermissionProps.setPermissionScreenLockStatus(false);
    SettingUtils.failureCallback(oResponse, "failureFetchPermissionByIdCallback", getTranslation());
    /*alertify.error(getTranslation().ERROR_CONTACT_ADMINISTRATOR, 0);*/
    /*console.log(oResponse);*/
    _triggerChange();
  };

  var successFetchTemplateById = function (oResponse) {
    var oTemplateFromServer = oResponse.success;
    var oCurrentTemplate = oTemplateFromServer.templateDetails;

    var oPermission = PermissionProps.getActivePermission();
    oPermission.templateInPermission = oTemplateFromServer;
    TemplateProps.setCurrentTabType("");/*TemplateProps.setCurrentTabType(TemplateTabsDictionary.HOME_TAB);*/
    TemplateProps.setActiveTemplate(oPermission.templateInPermission);
    PermissionProps.setSelectedTemplate(oCurrentTemplate.id);

    var aSectionMap = [];
    TemplateProps.setTemplateSectionMap(aSectionMap);
    _triggerChange();
  };

  var failureFetchTemplateById = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGetTemplate", getTranslation());
  };

  var _handlePermissionTemplateAdded = function (aSelectedIds) {
    var oActivePermissionObject = _makeActivePermissionDirty();
    let oTemplateInPermission = oActivePermissionObject.templateInPermission;
    let aSelectedItems = oTemplateInPermission.allowedTemplates || [];
    let aAllowedTemplates = SettingUtils.getAppData().getTemplates();
    CS.forEach(aSelectedIds, function (sId) {
      aSelectedItems.push(CS.find(aAllowedTemplates, {id: sId}));
    });
    oTemplateInPermission.allowedTemplates = aSelectedItems;


    /*var aClasses = oActivePermissionObject["classes"];
    CS.forEach(aClasses, function (oClass) {
      var oChild = CS.find(oClass.children, {id: oSelectedNatureClassDetails.id});
      if(oChild) {
        if(CS.isEmpty(oChild.allowedTemplates)) {
          oChild.allowedTemplates = [];
        }
        CS.forEach(aSelectedIds, function (sId) {
          if(!CS.includes(oChild.allowedTemplates, sId)) {
            oChild.allowedTemplates.push(sId);
            oChild.defaultTemplate = oSelectedNatureClassDetails.defaultTemplate;
          }
        });

        CS.remove(oChild.allowedTemplates, function (sId) {
          return sId == oSelectedNatureClassDetails.defaultTemplate;
        });

        PermissionProps.setActivePermissionEntity(oChild);
        return false;
      }
    });*/
    _triggerChange();
  };

  var _makeActivePermissionDirty = function () {
    var oActiveRoleList = PermissionProps.getActivePermission();
    if (!oActiveRoleList.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveRoleList);
    }
    PermissionProps.setPermissionScreenLockStatus(true);
    return oActiveRoleList.clonedObject;
  };

  var _isActivePermissionDirty = function () {
    var oActivePermission = PermissionProps.getActivePermission();
    if (!CS.isEmpty(oActivePermission)) {
      return !!oActivePermission.clonedObject;
    }
    return false;
  };

  var _selectPermissionById = function (sPermissionId) {
    var oRoleValueList = SettingUtils.getComponentProps().roleConfigView.getRoleValuesList();
    CS.forEach(oRoleValueList, function (oValue, sId) {
      oValue.isChecked = false;
      oValue.isSelected = (sId == sPermissionId);
    })
  };

  var _applyPermissionById = function (oGlobalPermission, sId, sProperty, sType, bUseNewValue, bNewValue) {
    if (!bUseNewValue) {
      bNewValue = !oGlobalPermission[sProperty];
    }

    oGlobalPermission[sProperty] = bNewValue;
    if (sType == "klass" || sType == "taxonomy" || sType == "tasks" || sType == "context") {
      if (sProperty != "canRead" && bNewValue) {
        oGlobalPermission.canRead = true;
      } else if (sProperty == "canRead" && !bNewValue) {
        oGlobalPermission.canCreate = false;
        oGlobalPermission.canEdit = false;
        oGlobalPermission.canDelete = false;

      }
    }
    return bNewValue;
  };

  var _applyPermissionToAllChildren = function (oActivePermission, aChildren, sProperty, sType, bNewValue) {
    CS.forEach(aChildren, function (oChild) {
      _applyPermissionById(oActivePermission, oChild.id, sProperty, sType, true, bNewValue);
      _applyPermissionToAllChildren(oActivePermission, oChild.children, sProperty, sType, bNewValue);
    });
  };

  var _getClassItemType = function (sType) {
    switch (sType) {
      case 'klass':
        return 'classes';

      case 'context':
        return 'contexts';

      case 'taxonomy':
        return 'taxonomies';

      case 'tasks':
        return 'tasks';

      default:
        return "";
    }
  };

  var _handlePermissionButtonToggled = function (sId, sProperty, sType, bForAllChildren) {
    if (CS.includes(["all", "class_taxonomy"], sId)) {
      return;
    }
    var oAppData = SettingUtils.getAppData();
    var oActivePermissionObject = _makeActivePermissionDirty();
    var oActivePermission;
    var oCurrentNode = null;
    var oChild = null;
    var aChildrenList = [];
    switch (sType) {
      case "klass":
      case "tasks":
      case "context":
      case "taxonomy":
        var sClassType = _getClassItemType(sType) || sType;
        var aChildren = oActivePermissionObject[sClassType];
        if(sType == "klass") {
          oChild =  SettingUtils.getNodeFromTreeListById(aChildren, sId);
          oActivePermission = oChild && oChild.globalPermission || {};
        } else {
          oChild = CS.find(aChildren, {id: sId});
          oActivePermission = oChild && oChild.globalPermission || {};
        }
        if (bForAllChildren) {
          if (sType == "klass") {
            aChildrenList = oAppData.getClassTree()[0].children;
          } else {
            aChildrenList = oAppData.getTaxonomyTree();
          }
          oCurrentNode = SettingUtils.getNodeFromTreeListById(aChildrenList, sId);
        }
        break;
      case "propertycollection":
        /*oActivePermission = oActivePermissionObject.propertyCollectionPermissions;
        if (sProperty == "readOnly") {
          sProperty = "canEdit";
        }*/
        break;
      case "attribute":
      case "tag":
      case "role":
      case "relationship":
       /* oActivePermission = oActivePermissionObject.propertyPermissions;
        if (sProperty == "readOnly") {
          sProperty = "isDisabled";
        }*/
        break
    }
    var bNewValue = _applyPermissionById(oActivePermission, sId, sProperty, sType);
    if (bForAllChildren && oCurrentNode) {
      _applyPermissionToAllChildren(oActivePermission, oCurrentNode.children, sProperty, sType, bNewValue);
    }
    _triggerChange();
  };

  var _handlePermissionSelectionToggled = function (sClickedId) {
    var aSelectedIds = PermissionProps.getSelectedIds();
    if (CS.includes(aSelectedIds, sClickedId)) {
      CS.remove(aSelectedIds, function (sId) {
        return sId == sClickedId;
      });
    } else {
      aSelectedIds.push(sClickedId);
    }
    _triggerChange();
  };

  var _resetClassVisibilityStatus = function () {
    var oClassVisibilityStatus = PermissionProps.getClassVisibilityStatus();
    CS.forEach(oClassVisibilityStatus, function (bStatus, sKey) {
      oClassVisibilityStatus[sKey] = false;
    });
  };

  var _handlePermissionFirstLevelItemClicked = function (sClickedId, sType) {

    if (sType == "propertycollection") {
      PermissionProps.setSelectedPropertyCollection(sClickedId);
      // _fetchPermissionProperties();
      _triggerChange();
    } else if (sType === "taxonomy") {
      _handlePermissionKlassItemClicked(sClickedId, "", sType);
    } else {
      var oClassVisibilityStatus = PermissionProps.getClassVisibilityStatus();
      _resetClassVisibilityStatus();
      PermissionProps.setActivePermissionEntity({});
      oClassVisibilityStatus[sClickedId] = true;
      PermissionProps.setSelectedFirstLevelClass(sClickedId);
      PermissionProps.setSelectedIds([]);

      if (sType == "template") {
        if (_isActivePermissionDirty()) {
          CustomActionDialogStore.showConfirmDialog(getTranslation().UNSAVED_DATA_LOST_CONFIRM, "",
              function () {
                _discardUnsavedPermission({});
                _fetchTemplateById(sClickedId);
              }, function (oEvent) {
              });
        }else {
          _fetchTemplateById(sClickedId);
        }
      }else {
        _triggerChange();
      }

    }
  };

  var _handlePermissionKlassItemClicked = function (sId, sPermissionNodeId, sType) { //todo: rename
    /*var oClassVisibilityStatus = PermissionProps.getClassVisibilityStatus();*/
    _fetchDetailedPermissionsForEntity(sId, sType);
  };

  let _fetchDetailedPermissionsForEntity = function (sId, sType) {
    PermissionProps.setSelectedTypeAndEntity({
      entityType: sType,
      id: sId
    });
    let sRoleId = _getSelectedRole().id || "";
    var oData = {
      id: sId,
      roleId: sRoleId,
      entityType: sType,
    };

    let oNode = SettingUtils.getNodeFromTreeListById([PermissionProps.getHierarchyTree()], sId);

    let oActivePermissionEntityData = {
      id: sId,
      label: oNode.label,
      type: sType,
      isNature: oNode.isNature,
      code: oNode.code
    };

    SettingUtils.csPostRequest(PermissionRequestMapping.GetPermissionTemplate, {}, oData, successFetchDetailedPermissionsForEntity.bind(this, oActivePermissionEntityData), failureFetchDetailedPermissionsForEntity);
  };

  let _getGeneralInformationDataAccordingToTableSkeleton = (oDataFromServer) => {
    let oGeneralData = {
      image: {
        label: getTranslation().IMAGE,
        isVisible: oDataFromServer.viewIcon,
        canEdit: oDataFromServer.canChangeIcon,
      },
      name: {
        label: getTranslation().NAME,
        isVisible: oDataFromServer.viewName,
        canEdit: oDataFromServer.canEditName,
      },
      natureType: {
        label: getTranslation().NATURE_TYPE,
        isVisible: oDataFromServer.viewPrimaryType,
      },
      additionalClasses: {
        label: getTranslation().ADDITIONAL_CLASSES,
        isVisible: oDataFromServer.viewAdditionalClasses,
        canAdd: oDataFromServer.canAddClasses,
        canRemove: oDataFromServer.canDeleteClasses,
      },
      taxonomies: {
        label: getTranslation().TAXONOMIES,
        isVisible: oDataFromServer.viewTaxonomies,
        canAdd: oDataFromServer.canAddTaxonomy,
        canRemove: oDataFromServer.canDeleteTaxonomy,
      },
      lifeStatusTag: {
        label: getTranslation().LIFECYCLE_STATUS,
        isVisible: oDataFromServer.viewStatusTags,
        canEdit: oDataFromServer.canEditStatusTag,
      },
      createAndModified: {
        label: getTranslation().CREATED_LAST_MODIFIED_INFO,
        isVisible: oDataFromServer.viewCreatedOn && oDataFromServer.viewLastModifiedBy,
      },
    };
    return oGeneralData
  };

  let _prepareGeneralInformationTableData = (oDataFromServer, oSkeleton, sContext, bIsReadOnly) => {

    let oGeneralData = _getGeneralInformationDataAccordingToTableSkeleton(oDataFromServer);

    let bIsAllVisibleSelected = true;
    let bIsAllVisibleDeSelected = true;
    let bIsAllCanAddSelected = true;
    let bIsAllCanAddDeSelected = true;
    let bIsAllCanEditSelected = true;
    let bIsAllCanEditDeSelected = true;
    let bIsAllCanRemoveSelected = true;
    let bIsAllCanRemoveDeSelected = true;
    let oHasDisabledField = {};

    let aRow = [];
    let oProcessedProperties = {};

    CS.forEach(oGeneralData, function (oData) {
      let bIsVisible = oData.isVisible;
      let bIsDisabled = bIsReadOnly ? bIsReadOnly : !bIsVisible
      let aDataForGeneralInfo = [];
      CS.forEach(oSkeleton, function (oSkeletonData) {
        let bHasDisabledField = oHasDisabledField[oSkeletonData.id] ? oHasDisabledField[oSkeletonData.id] : bIsDisabled
        switch (oSkeletonData.id) {
          case "label":
            oProcessedProperties = {
              renderType: oTableRenderTypeConstant.LABEL,
              value: oData.label,
              width: oSkeletonData.width,
              id: "label"
            };
            break;

          case "visible":
            oProcessedProperties = {
              renderType: oTableRenderTypeConstant.CHECKBOX,
              value: bIsVisible,
              width: oSkeletonData.width,
              id: "visible"
            };
            if (bIsVisible) {
              bIsAllVisibleDeSelected = false;
            }
            else {
              bIsAllVisibleSelected = false;
            }
            break;

          case "canEdit":
            if (oData.hasOwnProperty("canEdit")) {
              oProcessedProperties = {
                renderType: oTableRenderTypeConstant.CHECKBOX,
                value: (bIsReadOnly || !bIsVisible) ? false : oData.canEdit,
                width: oSkeletonData.width,
                id: "canEdit",
                isDisabled: bIsDisabled
              };
              if (oData.canEdit && !bIsReadOnly && bIsVisible) {
                bIsAllCanEditDeSelected = false;
              } else {
                bIsAllCanEditSelected = false
              }
              oHasDisabledField[oSkeletonData.id] = bHasDisabledField;
            }
            else {
              oProcessedProperties = {
                renderType: null,
                width: oSkeletonData.width,
                id: "canEdit"
              };
            }
            break;

          case "canAdd":
            if (oData.hasOwnProperty("canAdd")) {
              oProcessedProperties = {
                renderType: oTableRenderTypeConstant.CHECKBOX,
                value: (bIsReadOnly || !bIsVisible) ? false : oData.canAdd,
                width: oSkeletonData.width,
                id: "canAdd",
                isDisabled: bIsDisabled
              };

              if (oData.canAdd && !bIsReadOnly && bIsVisible) {
                bIsAllCanAddDeSelected = false;
              } else {
                bIsAllCanAddSelected = false
              }
              oHasDisabledField[oSkeletonData.id] = bHasDisabledField;
            }
            else {
              oProcessedProperties = {
                renderType: null,
                width: oSkeletonData.width,
                id: "canAdd"
              };
            }
            break;

          case "canRemove":
            if (oData.hasOwnProperty("canRemove")) {
              oProcessedProperties = {
                renderType: oTableRenderTypeConstant.CHECKBOX,
                value: (bIsReadOnly || !bIsVisible) ? false : oData.canRemove,
                width: oSkeletonData.width,
                id: "canRemove",
                isDisabled: bIsDisabled
              };
              if (oData.canRemove && !bIsReadOnly && bIsVisible) {
                bIsAllCanRemoveDeSelected = false;
              } else {
                bIsAllCanRemoveSelected = false
              }
              oHasDisabledField[oSkeletonData.id] = bHasDisabledField;
            }
            else {
              oProcessedProperties = {
                renderType: null,
                width: oSkeletonData.width,
                id: "canRemove"
              };
            }
            break;
        }
        aDataForGeneralInfo.push(oProcessedProperties);
        //TODO: Refactor - Row id should be unique, don't use label
        aDataForGeneralInfo.rowId = oData.label
      });
      aRow.push(aDataForGeneralInfo);
    });

    let oTableViewPropsForPropertyCollection = ContextualTableViewProps.getTableViewPropsByContext(sContext);
    oTableViewPropsForPropertyCollection.setTableViewRowData(aRow);

    return {
      isAllVisibleSelected: bIsAllVisibleSelected,
      isAllVisibleDeSelected: bIsAllVisibleDeSelected,
      isAllCanAddSelected: bIsAllCanAddSelected,
      isAllCanAddDeSelected: bIsAllCanAddDeSelected,
      isAllCanEditSelected: bIsAllCanEditSelected,
      isAllCanEditDeSelected: bIsAllCanEditDeSelected,
      isAllCanRemoveSelected: bIsAllCanRemoveSelected,
      isAllCanRemoveDeSelected: bIsAllCanRemoveDeSelected,
      hasDisabledFieldMap: oHasDisabledField
    }
  };

  let _updateHeaderField = (oHeaderField, bIsAllSelected, bIsAllDeselected, bIsReadOnly, bHasDisabledField) => {
    if (bIsAllSelected && !bIsReadOnly) {
      oHeaderField.value = true;
      oHeaderField.isDisabled = bHasDisabledField || bIsReadOnly
    } else if (bIsAllDeselected || bIsReadOnly) {
      oHeaderField.value = false;
      oHeaderField.isDisabled = bHasDisabledField || bIsReadOnly
    } else {
      oHeaderField.value = true;
      oHeaderField.isPartiallySelected = true;
      oHeaderField.isDisabled = bHasDisabledField
    }
  };

  let _prepareGeneralInformationHeaderData = (aGeneralInformationSkeletonData, oTableViewPropsForGeneralInformation, oGeneralInfoSelectionData, bIsReadOnly) => {
    CS.forEach(aGeneralInformationSkeletonData, function (oSkeletonData) {
      let bHasDisabledField = oGeneralInfoSelectionData.hasDisabledFieldMap[oSkeletonData.id];
      switch (oSkeletonData.id) {
        case "visible":
          _updateHeaderField(oSkeletonData, oGeneralInfoSelectionData.isAllVisibleSelected,
              oGeneralInfoSelectionData.isAllVisibleDeSelected);
          break;

        case "canEdit":
          _updateHeaderField(oSkeletonData, oGeneralInfoSelectionData.isAllCanEditSelected,
              oGeneralInfoSelectionData.isAllCanEditDeSelected, bIsReadOnly, bHasDisabledField);
          break;

        case "canAdd":
          _updateHeaderField(oSkeletonData, oGeneralInfoSelectionData.isAllCanAddSelected,
              oGeneralInfoSelectionData.isAllCanAddDeSelected, bIsReadOnly, bHasDisabledField);
          break;

        case "canRemove":
          _updateHeaderField(oSkeletonData, oGeneralInfoSelectionData.isAllCanRemoveSelected,
              oGeneralInfoSelectionData.isAllCanRemoveDeSelected, bIsReadOnly, bHasDisabledField);
          break;
      }
    });

    oTableViewPropsForGeneralInformation.setTableViewHeaderData(aGeneralInformationSkeletonData);
  };

  let successFetchDetailedPermissionsForEntity = function (oData, oResponse) {
    let oResponseData = oResponse.success;

    /** Active entity part -------------------------------------------------------------------------------------------*/
    let aTableViewPropertyCollectionSkeleton = new TableViewPropertyCollectionSkeleton();
    let aRelationshipHeaderData = new TableViewRelationshipSkeleton();
    let aGeneralInformationSkeletonData = new TableViewGeneralInformationSkeleton();
    let oSelectedRole = RoleProps.getSelectedRole();
    let bIsReadOnly = oSelectedRole.isReadOnly;

    if (PermissionProps.getSelectedEntityType() === ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS) {
      let oTableViewPropsForGeneralInformation = ContextualTableViewProps.createTableViewPropsByContext("generalInformation");

      let oGeneralInfoSelectionData = _prepareGeneralInformationTableData(oResponseData.permission.headerPermission, aGeneralInformationSkeletonData, "generalInformation", bIsReadOnly);

      _prepareGeneralInformationHeaderData(aGeneralInformationSkeletonData, oTableViewPropsForGeneralInformation, oGeneralInfoSelectionData, bIsReadOnly);

      if (CS.isNotEmpty(oResponseData.referencedRelationships)) {
        let oTableViewPropsForRelationship = ContextualTableViewProps.createTableViewPropsByContext("relationship");
        oTableViewPropsForRelationship.setTableViewHeaderData(aRelationshipHeaderData);

        CS.forEach(oResponseData.permission.relationshipPermission, function (oRelationship) {
          let oRelationshipData = CS.find(oResponseData.referencedRelationships, {id: oRelationship.relationshipId});
          oRelationship["label"] = oRelationshipData.label;
          oRelationship["code"] = oRelationshipData.code;
        });
        let oExtraData = {};
        oExtraData["isReadOnly"] = bIsReadOnly;
        TableViewStore.prepareTableViewRowData(aRelationshipHeaderData, oResponseData.permission.relationshipPermission, "relationship", oExtraData);
      }
    }
    CS.forEach(oResponseData.permission.propertyCollectionPermission, function (oProperty) {
      let oPropertyData = CS.find(oResponseData.referencedPropertyCollections, {id: oProperty.propertyCollectionId});
      oProperty["label"] = oPropertyData.label;
    });

    let oPropertyPermission = oResponseData.permission.propertyPermission;

    let oExtraData = {
      "propertyPermission": oPropertyPermission,
      "isReadOnly":bIsReadOnly
    };
    CS.forEach(oResponseData.permission.propertyCollectionPermission, function (oPropertyCollection, key) {
      /**To update property permission, need to prepare property data**/
      let oReferencedPropertyCollections = oResponseData.referencedPropertyCollections[key];
      let aCurrentPCProperties = [];
      let bHasDisabledField = false;
      CS.forEach(oReferencedPropertyCollections.elementIds, (sId) => {
        let oProperty = CS.assign({}, oPropertyPermission[sId]);
        oProperty.id = sId;
        aCurrentPCProperties.push(oProperty);
        bHasDisabledField = bHasDisabledField ? bHasDisabledField : !oProperty.isVisible;
      });

      TableViewStore.prepareTableViewHeaderData(aTableViewPropertyCollectionSkeleton, oPropertyCollection , key, bIsReadOnly, bHasDisabledField);
      _updatePropertyCollectionHeaderPartiallySelection(key, oResponseData.referencedPropertyCollections, oResponseData.permission)

      TableViewStore.prepareTableViewRowData(aTableViewPropertyCollectionSkeleton, aCurrentPCProperties, key, oExtraData);
    });

    PermissionProps.setIsPermissionModeClass(false);
    PermissionProps.setActivePermissionEntity(oData);

    /** Template part ------------------------------------------------------------------------------------------------*/

    let oPermission = PermissionProps.getActivePermission();
    delete oPermission.clonedObject;
    // let oProcessedPermissions = _getProcessedPermissionForReadOnlyUser(oResponseData.permission);
    let oTemplate = {
      templateDetails: {},
      templatePermission: oResponseData.permission,
      allowedTemplates: oResponseData.allowedTemplates
    };
    /** need to have it this way in order to make it work with existing code */

    oPermission.templateInPermission = oTemplate;
    TemplateProps.setActiveTemplate(oPermission.templateInPermission);

    let oPropertyCollectionPermissionMap = oTemplate.templatePermission.propertyCollectionPermission;
    // let oRelationshipPermissionMap = oTemplate.templatePermission.relationshipPermission;

    var oMasterSectionDetailsMap = [];
      CS.forEach(oPropertyCollectionPermissionMap, function (oPC, sId) {
        oMasterSectionDetailsMap[oPC.propertyCollectionId] = {
          isCollapsed: true
        };
      });

    TemplateProps.setTemplateSectionMap(oMasterSectionDetailsMap);

    PermissionProps.setActiveEntityInformation(oData);
    PermissionProps.setReferencedPropertyCollections(oResponseData.referencedPropertyCollections);
    PermissionProps.setReferencedRelationships(oResponseData.referencedRelationships);
    let oHierarchyTreeVisualData = PermissionProps.getHierarchyTreeVisualData();
    CS.forEach(oHierarchyTreeVisualData, function (oVisualData, sKey) {
      // set isSelected true only for those ids that are in the path
      oVisualData.isSelected = CS.includes([oData.id], sKey);
    });
    _triggerChange();
  };

  let _getIsPartiallySelected = (sKey, sContext, oReferencedPropertyCollections, oPermission) => {
    let oPropertyCollection = oReferencedPropertyCollections[sContext];
    let oPropertyPermissions = oPermission.propertyPermission;
    let aElementIds = oPropertyCollection.elementIds;
    let bIsPartiallySelected = false;
    CS.forEach(aElementIds, (sElementId) => {
      let oPropertyPermission = oPropertyPermissions[sElementId];
      if (!oPropertyPermission[sKey] || !oPropertyPermission.isVisible) {
        bIsPartiallySelected = true;
        return false;
      }
    });
    return bIsPartiallySelected;
  };

  let _updatePropertyCollectionHeaderPartiallySelection = (sContext, oReferencedPropertyCollections,
                                                           oPermission) => {
    let oTableViewPropsForPropertyCollection = ContextualTableViewProps.getTableViewPropsByContext(sContext);
    let aTableViewHeaderData = oTableViewPropsForPropertyCollection.getTableViewHeaderData();

    CS.forEach(aTableViewHeaderData, (oData) => {
      switch (oData.id) {
        case "visible":
          if (oData.value) {
            oData.isPartiallySelected = _getIsPartiallySelected("isVisible", sContext, oReferencedPropertyCollections, oPermission);

          };
          break;

        case "canEdit":
          if (oData.value) {
            oData.isPartiallySelected = _getIsPartiallySelected("canEdit", sContext, oReferencedPropertyCollections, oPermission);
          };
          break;
      }
    });
  };

  let _handlePermissionRestoreMenuClicked = function () {
    let sActiveEntity = PermissionProps.getSelectedEntityType();
    let oPermissions = PermissionProps.getActivePermission();
    let oTemplateInPermissions = oPermissions.templateInPermission;
    let oTemplatePermissions = oTemplateInPermissions.templatePermission;
    let oSelectedRole = RoleProps.getSelectedRole();
    let bIsReadOnly = oSelectedRole.isReadOnly;
    PermissionProps.setIsPermissionDirty(true);
    _makeActivePermissionDirty();

    let oActiveEntityInformation = PermissionProps.getActiveEntityInformation();
    let oModifiedPermissionMap = PermissionProps.getModifiedPermissionMap()[oActiveEntityInformation.id];
    if(!bIsReadOnly) {
      oModifiedPermissionMap.canCreate = true;
      oModifiedPermissionMap.canDelete = true;
      oModifiedPermissionMap.canDownload = true;
      oModifiedPermissionMap.canEdit = true;
      oModifiedPermissionMap.canRead = true;
    } else {
      oModifiedPermissionMap.canDownload = true;
    }

    if (sActiveEntity === ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS) {
      handleRestoreButtonClicked("generalInformation", bIsReadOnly);
      handleRestoreButtonClicked("relationship", bIsReadOnly);
    }

    let oPropertyCollectionPermissions = oTemplatePermissions.propertyCollectionPermission;
    CS.forEach(oPropertyCollectionPermissions, function (oPropertyCollection, key) {
      handleRestoreButtonClicked(key, bIsReadOnly);
    });
    _triggerChange();
  };

  /** Function to handle export of role permissions **/
  let _handleRolesPermissionsExportButtonClicked = function (oSelectiveExport) {
    return SettingUtils.csPostRequest(oSelectiveExport.sUrl, {}, oSelectiveExport.oPostRequest, successExportPermissions, failureExportPermissions);
  };

  /** Function to handle success for export of role permissions **/
  let successExportPermissions = function(oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
    return true;
  };

  /** Function to handle failure for export of role permissions **/
  let failureExportPermissions = function(oResponse){
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
    return false;
  };

  /** Funtion to get valid Files **/
  let _getIsValidFileTypes = function (oFile) {
    // var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    let sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  /** Function to handle success for import of role permissions **/
  let successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  /** Function to handle failure for export of role permissions **/
  let failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
  };

  /** Function to handle upload excel sheet of role permissions **/
  let uploadFileImport = function (oImportExcel, oCallback) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  /** Function to handle fileupload import role permissions **/
  let _handlePermissionFileUploaded = function(aFiles,oImportExcel){
    let bIsAnyValidImage = false;
    let bIsAnyInvalidImage = false;
    let aSelectedRoles = [];
    let aSelectedEntityType = [];
    let sSelectedTreeItemId = PermissionProps.getSelectedTreeItemId();
    let sSelectedRole = RoleProps.getSelectedRole().id;
    let sSelectedEntityType = ClassCategoryConstantsForPermissions[sSelectedTreeItemId];
    aSelectedRoles.push(sSelectedRole);
    aSelectedEntityType.push(sSelectedEntityType);
    if (aFiles.length) {

      let iFilesInProcessCount = 0;
      let count = 0;

      let data = new FormData();
      CS.forEach(aFiles, function (file, index) {
        if (_getIsValidFileTypes(file)) {
          bIsAnyValidImage = true;
          let reader = new FileReader();
          iFilesInProcessCount++;
          // Closure to capture the file information.
          reader.onload = (function (theFile) {
            return function (event) {
              count += 1;
              let filekey = UniqueIdentifierGenerator.generateUUID();
              data.append(filekey, theFile);
            };
          })(file);

          reader.onloadend = function () {
            iFilesInProcessCount--;
            if (iFilesInProcessCount == 0) {
              data.append("entityType", oImportExcel.entityType);
              data.append("permissionTypes", aSelectedEntityType);
              data.append("roleIds",aSelectedRoles);
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

  let handleRestoreButtonClicked = (sContext, bIsReadOnly) => {
    let oTableViewProps = ContextualTableViewProps.getTableViewPropsByContext(sContext);
    let aTableHeaderData = oTableViewProps.getTableViewHeaderData();
    let aTableRowData = oTableViewProps.getTableViewRowData();

    if(sContext !== "relationship") {
      _makeRowDirty(aTableHeaderData);
      CS.forEach(aTableHeaderData, function (oHeaderCellData) {
        switch (oHeaderCellData.id) {
          case "visible":
            oHeaderCellData.value = true;
            oHeaderCellData.isPartiallySelected = false;
            break;

          case "canEdit":
            oHeaderCellData.value = bIsReadOnly ? oHeaderCellData.value : true;
            oHeaderCellData.isPartiallySelected = false;
            oHeaderCellData.isDisabled = bIsReadOnly ? bIsReadOnly : false;
            break;

          case "canAdd":
            oHeaderCellData.value = bIsReadOnly ? oHeaderCellData.value : true;
            oHeaderCellData.isPartiallySelected = false;
            oHeaderCellData.isDisabled = bIsReadOnly ? bIsReadOnly : false;
            break;

          case "canRemove":
            oHeaderCellData.value = bIsReadOnly ? oHeaderCellData.value : true;
            oHeaderCellData.isPartiallySelected = false;
            oHeaderCellData.isDisabled = bIsReadOnly ? bIsReadOnly : false;
            break;
        }
      });
    }

    CS.forEach(aTableRowData, function (aRowData) {
      _makeRowDirty(aRowData);
      CS.forEach(aRowData, function (oCellData) {
        if (CS.isNotEmpty(oCellData.renderType)) {
          switch (oCellData.id) {
            case "visible":
              oCellData.value = true;
              break;

            case "canEdit":
              oCellData.value = bIsReadOnly ? oCellData.value : true;
              oCellData.isDisabled = bIsReadOnly ? oCellData.isDisabled : false;
              break;

            case "canAdd":
              oCellData.value = bIsReadOnly ? oCellData.value : true;
              oCellData.isDisabled = bIsReadOnly ? oCellData.isDisabled : false;
              break;

            case "canRemove":
              oCellData.value = bIsReadOnly ? oCellData.value : true;
              oCellData.isDisabled = bIsReadOnly ? oCellData.isDisabled : false;
              break;
          }
        }
      });
    });

    oTableViewProps.setIsTableViewDataDirty(true);
    oTableViewProps.setTableViewHeaderData(aTableHeaderData);
    oTableViewProps.setTableViewRowData(aTableRowData);
  };

  let _resetTableViewProps = (context) => {
    ContextualTableViewProps.resetTableViewPropsByContext(context);
  };

  var failureFetchDetailedPermissionsForEntity = function (oResponse) {
    PermissionProps.setPermissionScreenLockStatus(false);
    SettingUtils.failureCallback(oResponse, "failureFetchDetailedPermissionsForEntity", getTranslation());
    _triggerChange();
  };

  var _handleSetDefaultTemplateClicked = function (oDefaultTemplate) {
    var oActivePermissionObject = _makeActivePermissionDirty();
    var aClasses = oActivePermissionObject["classes"];
    var oActiveNatureClassDetails = PermissionProps.getActivePermissionEntity();
    CS.forEach(aClasses, function (oClass) {
      var oChild = CS.find(oClass.children, {id: oActiveNatureClassDetails.id});
      if(oChild) {
        oChild.defaultTemplate = oDefaultTemplate.id;
        CS.remove(oChild.allowedTemplates, function (sId) {return (sId == oDefaultTemplate.id);});
        PermissionProps.setActivePermissionEntity(oChild);
        return false;
      }
    });
    _triggerChange();
  };

  var _handlePermissionRemoveTemplateClicked = function (sId) {
    var oActivePermissionObject = _makeActivePermissionDirty();
    let oTemplateInPermission = oActivePermissionObject.templateInPermission;
    CS.remove(oTemplateInPermission.allowedTemplates, {id: sId});
    // var aClasses = oActivePermissionObject["classes"];
    // var oActiveNatureClassDetails = PermissionProps.getActivePermissionEntity();
    // CS.forEach(aClasses, function (oClass) {
    //   var oChild = CS.find(oClass.children, {id: oActiveNatureClassDetails.id});
    //   if(oChild) {
    //     CS.remove(oChild.allowedTemplates, function (sId) {
    //       return (sId == sId.id);
    //     });
    //
    //     if(!CS.isEmpty(oChild.allowedTemplates) && oChild.defaultTemplate == sId.id) {
    //       oChild.defaultTemplate = oChild.allowedTemplates[0];
    //     } else {
    //       oChild.defaultTemplate = "";
    //     }
    //     PermissionProps.setActivePermissionEntity(oChild);
    //     return false;
    //   }
    // });
    _triggerChange();
  };

  var _fetchPropertyCollectionDetailsAndSwitch = function (sId) {
    var sUrl;
    var oParameters = {};

    oParameters.id = sId;

    sUrl = oPropertyCollectionRequestMapping.GetPropertyCollection;

    return SettingUtils.csGetRequest(sUrl, oParameters, successFetchPropertyCollectionElaborate, failureFetchPropertyCollectionElaborate);
  };

  let failureFetchPropertyCollectionElaborate = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchPropertyCollectionElaborate", getTranslation());
  };

  let successFetchPropertyCollectionElaborate = function (oResponse) {
    var oResponseFromServer = oResponse.success;
    let oReferencedPropertyCollections = PermissionProps.getReferencedPropertyCollections();

    let oTableViewPropertyCollectionSkeleton = new TableViewPropertyCollectionSkeleton();
    let oActivePermission = PermissionProps.getActivePermission();
    let oPropertyPermission = oActivePermission.templateInPermission.templatePermission.propertyPermission;
    let oExtraData = {};
    let oSelectedRole = RoleProps.getSelectedRole();
    let bIsReadOnly = oSelectedRole.isReadOnly;

    oExtraData["propertyPermission"] = oPropertyPermission;
    oExtraData["isReadOnly"] = bIsReadOnly;
    oExtraData.updatePropertyInAnotherTable = _updatePropertyInAnotherTable;
    let aPropertyPermission = CS.concat(oResponseFromServer.referencedAttributes, oResponseFromServer.referencedTags,oResponseFromServer.referencedTaxonomies);
    TableViewStore.prepareTableViewRowData(oTableViewPropertyCollectionSkeleton, aPropertyPermission, oResponseFromServer.id, oExtraData);

    // _setElementDataFromReferencedElements(oResponseFromServer);
    oReferencedPropertyCollections[oResponseFromServer.id] = oResponseFromServer;
    // _changeVisibalityofElementsAccordingToThePC(oResponseFromServer);
  };

  let _handleTableExpandCollapsedClicked = function (sId) {
    let oTableViewProps = ContextualTableViewProps.getTableViewPropsByContext(sId);
    let aHeaderData = oTableViewProps.getTableViewHeaderData();
    let oFirstField = aHeaderData[0];
    oFirstField.isCollapsed = !oFirstField.isCollapsed;
    if (!oFirstField.isCollapsed) {
      return _fetchPropertyCollectionDetailsAndSwitch(sId);
    } else{
      return CommonUtils.resolveEmptyPromise();
    }
  };

  let _updateRowDataOnHeaderChange = (aSectionBodyData, aSectionHeaderData, sCellId, bValue, sTableId, sScreenContext) => {
    CS.forEach(aSectionBodyData, function (aRowData) {
      (sCellId == "visible") && _toggleTableRowButtons(aRowData, bValue, sScreenContext);
      let oProperty = CS.find(aRowData, {id: sCellId});
      if (oProperty.hasOwnProperty("value")) {
        _makeRowDirty(aRowData);
        oProperty.value = bValue;
        _updatePropertyInAnotherTable(bValue, sTableId, aRowData.rowId, sCellId);
      }
    })
    if (sCellId == "visible") {
      const __result = _checkColumnValuesAreSelectedOrDeselected(aSectionBodyData);
      _updateHeaderValuesOnRowChange(__result, sTableId, aSectionHeaderData);
    }
  };

  /**
   * This is specific to property collection
   * @param bValue
   * @param sTableId
   * @param sRowId
   * @param sCellId
   * @private
   */
  let _updatePropertyInAnotherTable = (bValue, sTableId, sRowId, sCellId) => {
    let oPermissions = PermissionProps.getActivePermission();
    let oTemplateInPermissions = oPermissions.templateInPermission;
    let oTemplatePermissions = oTemplateInPermissions.templatePermission;
    let oPropertyCollectionPermissions = oTemplatePermissions.propertyCollectionPermission;
    let aPropertyCollectionIds = CS.keys(oPropertyCollectionPermissions);
    if (CS.includes(aPropertyCollectionIds, sTableId)) {
      CS.forEach(aPropertyCollectionIds, (sPropertyCollId) => {
        if (sPropertyCollId !== sTableId) {
          let oPropertyCollProps = ContextualTableViewProps.getTableViewPropsByContext(sPropertyCollId);
          let aTableViewRowData = oPropertyCollProps.getTableViewRowData();
          let aTableViewHeaderData = oPropertyCollProps.getTableViewHeaderData();
          CS.forEach(aTableViewRowData, (aRowData) => {
            if (aRowData.rowId === sRowId) {
              _makeRowDirty(aRowData);
              (sCellId == "visible") && _toggleTableRowButtons(aRowData, bValue, "propertyCollection");
              oPropertyCollProps.setIsTableViewDataDirty(true);
              let oProperty = CS.find(aRowData, {id: sCellId});
              oProperty.value = bValue;
              const __result = _checkColumnValuesAreSelectedOrDeselected(aTableViewRowData);
              _updateHeaderValuesOnRowChange(__result, sTableId, aTableViewHeaderData);
            }
          });
        }
      });
    }
  };

  let _checkColumnValuesAreSelectedOrDeselected = (aSectionBodyData, sCellId) => {
    let oCellSelectedDeselected = {};
    CS.forEach(aSectionBodyData, function (aRow) {
      let aBodyRowData = sCellId ? [CS.find(aRow, {id: sCellId})] : aRow;
      CS.forEach(aBodyRowData, function (oData) {
        if (oData.hasOwnProperty("value") && typeof oData.value == "boolean") {
          if (!oCellSelectedDeselected.hasOwnProperty(oData.id))
            oCellSelectedDeselected[oData.id] = {"isAllDeSelected": true, "isAllSelected": true, hasDisabledField: false}
          if (oData.value == true) oCellSelectedDeselected[oData.id].isAllDeSelected = false;
          else oCellSelectedDeselected[oData.id].isAllSelected = false;
          if (oData.isDisabled) oCellSelectedDeselected[oData.id].hasDisabledField = true
        }
      })
    })
    return oCellSelectedDeselected;
  }

  let _updateHeaderValuesOnRowChange = (__result, sTableId, aSectionHeaderData, sCellId) => {
    let aHeaderData = sCellId ? [CS.find(aSectionHeaderData, {id: sCellId})] : aSectionHeaderData;
    if (sTableId !== "relationship") {
      CS.forEach(aHeaderData, function(oHeaderCell) {
        let oCellSelectionData = __result[oHeaderCell.id];
        if (CS.isEmpty(oCellSelectionData)) return;
        _makeRowDirty(aSectionHeaderData);
        if (oCellSelectionData.isAllSelected) {
          oHeaderCell.value = true;
          oHeaderCell.isPartiallySelected = false;
        } else if (oCellSelectionData.isAllDeSelected) {
          oHeaderCell.value = false;
          oHeaderCell.isPartiallySelected = false;
        } else {
          oHeaderCell.value = true;
          oHeaderCell.isPartiallySelected = true;
        }
        oHeaderCell.isDisabled = oCellSelectionData.hasDisabledField;
      })
    }
  };

  let _toggleTableRowButtons = (aRowData, bToggle, sScreenContext) => {
    _makeRowDirty(aRowData);
    aRowData = CS.forEach(aRowData, oCell => {
      if (typeof oCell.value == "boolean" && oCell.id != "visible") {
        if (bToggle) {
          _updateValueOnVisibleChange(oCell, sScreenContext, aRowData.rowId);
        }
        else {
          oCell.value = false;
        }
        oCell.isDisabled = !bToggle;
      }
    })
  };

  let _updateValueOnVisibleChange = (oCell, sScreenContext, sRowId) => {
    let oPermissions = PermissionProps.getActivePermission();
    let oTemplateInPermissions = oPermissions.templateInPermission;
    let oTemplatePermissions = oTemplateInPermissions.templatePermission;
    let oPermission = {};
    switch (sScreenContext) {
      case "propertyCollection":
        oPermission = oTemplatePermissions.propertyPermission[sRowId];
        break;
      case "relationship":
        oPermission = oTemplatePermissions.relationshipPermission[sRowId];
        break;
      case "generalInformation":
        let oGeneralInformationData = _getGeneralInformationDataAccordingToTableSkeleton(oTemplatePermissions.headerPermission);
        oPermission = CS.find(oGeneralInformationData, {label: sRowId});
        break;
    }
    oCell.value = oPermission[oCell.id];
  };

  let _handleTableButtonClicked = (sTableId, sRowId, sCellId, sScreenContext) => {
    PermissionProps.setIsPermissionDirty(true);
    _makeActivePermissionDirty();
    let oTableViewProps = ContextualTableViewProps.getTableViewPropsByContext(sTableId);
    let aSectionHeaderData = oTableViewProps.getTableViewHeaderData();
    let aSectionBodyData = oTableViewProps.getTableViewRowData();

    oTableViewProps.setIsTableViewDataDirty(true);
    /* !sRowId consider as a header */
    if (!sRowId) {
      let oHeaderCell = CS.find(aSectionHeaderData, {id: sCellId});
      _makeRowDirty(aSectionHeaderData);
      if (oHeaderCell.isPartiallySelected) {
        oHeaderCell.value = false;
        oHeaderCell.isPartiallySelected = false;
      } else {
        oHeaderCell.value = !oHeaderCell.value
      }
      let bValue = oHeaderCell.value;
      _updateRowDataOnHeaderChange(aSectionBodyData, aSectionHeaderData, sCellId, bValue, sTableId, sScreenContext);
    } else {
      let aClickedRow = CS.find(aSectionBodyData, {"rowId":sRowId});
      _makeRowDirty(aClickedRow);
      let oClickedCell = CS.find(aClickedRow, {id: sCellId});
      oClickedCell.value = !oClickedCell.value;
      let sCellIdToUpdateHeaderValues = sCellId;
      if (sCellId == "visible") {
        sCellIdToUpdateHeaderValues = "";
        _toggleTableRowButtons(aClickedRow, oClickedCell.value, sScreenContext);
      }
      _updatePropertyInAnotherTable(oClickedCell.value, sTableId, aClickedRow.rowId, sCellId);

      const __result = _checkColumnValuesAreSelectedOrDeselected(aSectionBodyData, sCellIdToUpdateHeaderValues);
      _updateHeaderValuesOnRowChange(__result, sTableId, aSectionHeaderData, sCellIdToUpdateHeaderValues);
    }
    _triggerChange();
  };

  var _handlePermissionSectionStatusChanged = function (sStatus, sSectionId, aElements) {
    var oActivePermission = _makeActivePermissionDirty();
    var oTemplateInPermission = oActivePermission.templateInPermission;
    var oTemplatePermissions = oTemplateInPermission.templatePermission;

    var oPropertyCollectionPermission = oTemplatePermissions.propertyCollectionPermission;
    var oRelationshipPermission = oTemplatePermissions.relationshipPermission;
    var oReferencePermission = oTemplatePermissions.referencePermissions;
    var oTabPermission = oTemplatePermissions.tabPermission;
    var oPropertyPermission = oTemplatePermissions.propertyPermission;

    if (oPropertyCollectionPermission[sSectionId]) {
      oPropertyCollectionPermission[sSectionId][sStatus] = !oPropertyCollectionPermission[sSectionId][sStatus];

      CS.forEach(aElements, function (oElement) {
        oPropertyPermission[oElement.id][sStatus] = oPropertyCollectionPermission[sSectionId][sStatus];
      });
    } else if (oRelationshipPermission[sSectionId]) {
      oRelationshipPermission[sSectionId][sStatus] = !oRelationshipPermission[sSectionId][sStatus];
    } else if (oReferencePermission[sSectionId]) {
      oReferencePermission[sSectionId][sStatus] = !oReferencePermission[sSectionId][sStatus];
    } else if (oTabPermission[sSectionId]) {
      oTabPermission[sSectionId][sStatus] = !oTabPermission[sSectionId][sStatus];
    }
    _triggerChange();
  };

  var _handlePermissionElementStatusChanged = function (sStatus, sSectionId, sElementType, sParentSectionId) {
    var oActivePermission = _makeActivePermissionDirty();
    var oTemplateInPermission = oActivePermission.templateInPermission || {};
    var oTemplatePermissions = oTemplateInPermission.templatePermission|| {};
    var oPropertyCollectionPermission = oTemplatePermissions.propertyCollectionPermission || {};
    var oPropertyPermission = oTemplatePermissions.propertyPermission || {};
    let oReferencedPropertyCollections = PermissionProps.getReferencedPropertyCollections();
    let oReferencedPropertyCollection = oReferencedPropertyCollections[sParentSectionId];
    let oElement = CS.find(oReferencedPropertyCollection.elements, {id:sSectionId});

    oPropertyPermission[sSectionId][sStatus] = !oPropertyPermission[sSectionId][sStatus];

    if(oElement && !oPropertyCollectionPermission[sParentSectionId][sStatus]){
      oPropertyCollectionPermission[sParentSectionId][sStatus] = !oPropertyCollectionPermission[sParentSectionId][sStatus]
    }

    let bSelectedPermission = oPropertyPermission[sSectionId][sStatus];
    let bChangeParentPermission = true;
    CS.forEach(oReferencedPropertyCollection.elements, function (oElement) {
      if(oPropertyPermission[oElement.id][sStatus] !== bSelectedPermission) {
        bChangeParentPermission = false;
        return false;
      }
    });

    if(bChangeParentPermission) {
      oPropertyCollectionPermission[sParentSectionId][sStatus] = bSelectedPermission;
    }

    oPropertyPermission[sSectionId]["type"] = sElementType;
    _triggerChange();
  };

  var _handlePermissionOfTemplateHeaderToggled = function (sType, sContext, sId) {
    var oActivePermission = _makeActivePermissionDirty();
    var oTemplateInPermission = oActivePermission.templateInPermission;
    var oTemplatePermissions = oTemplateInPermission.templatePermission;

    switch (sType) {
      case "headerPermission":
        var oHeaderPermission = oTemplatePermissions.headerPermission;
        oHeaderPermission[sContext] = !oHeaderPermission[sContext];
        if (sContext == "viewCreatedOn") {
          oHeaderPermission["viewLastModifiedBy"] = oHeaderPermission[sContext];
        }
        break;
      case "tabPermission":
        var oTabPermission = oTemplatePermissions.tabPermission;
        var oTab = oTabPermission[sId];
        oTab[sContext] = !oTab[sContext];
        break;
    }
    _triggerChange();
  };

  var _fetchPermissionById = function (sRoleId, oCallbackData) {
    var oCallback = {};
    if (oCallbackData) {
      oCallback.functionToExecute = oCallbackData.functionToExecute
    }
    SettingUtils.csGetRequest(PermissionRequestMapping.GetPermission, {id: sRoleId}, successFetchPermissionByIdCallback.bind(this, oCallback, sRoleId), failureFetchPermissionByIdCallback);
  };

  var _getPermissionScreenLockStatus = function () {
    return PermissionProps.getPermissionScreenLockStatus();
  };

  var _getSelectedRole = function () {
    return RoleProps.getSelectedRole();
  };

  var _handlePermissionRefreshMenuClicked = function () {
    var oActivePermission = PermissionProps.getActivePermission();
    var oRoleValueList = SettingUtils.getComponentProps().roleConfigView.getRoleValuesList();
    var sActivePermissionId = "";
    CS.forEach(oRoleValueList, function (oValue, sId) {
      if(oValue.isSelected){
        sActivePermissionId = sId;
        return false;
      }
    });

    if (_isActivePermissionDirty()) {
      var oCallbackData = {};
      oCallbackData.functionToExecute = _fetchPermissionById.bind(this, sActivePermissionId);

      CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _savePermission.bind(this, oCallbackData),
          _discardUnsavedPermission,
          function () {
          }
      );
    } else {
      let oSelectedType = PermissionProps.getSelectedTypeAndEntity();
      if (CS.isEmpty(oActivePermission)) {
        // TODO: Fetch role list again
      } else {
        if (!CS.isEmpty(oSelectedType)) {
          _fetchDetailedPermissionsForEntity(oSelectedType.id, oSelectedType.entityType);
        } else {
        _fetchPermissionById(sActivePermissionId);
        }
      }
    }
  };

  let _getTemplateIds = function (aAllowedTemplates) {
    let aIds = [];
    CS.forEach(aAllowedTemplates,function (oAllowedTemplate) {
      aIds.push(oAllowedTemplate.id);
    });
    return aIds;
  }

  let _getModifiedRelationship = (aRelationshipBodyData, oRelationshipPermissions, oADM, bIsReadOnly) => {
    CS.forEach(aRelationshipBodyData, function (aRowData) {
      if (aRowData.isDirty) {
        let oUpdatedRelationship = oRelationshipPermissions[aRowData.rowId];
        let bIsVisible = CS.find(aRowData, {id: "visible"}).value;
        CS.forEach(aRowData, (oCellData) => {
          if (oCellData.id === "visible") {
            oUpdatedRelationship.isVisible = oCellData.value
          } else if (oCellData.id === "canAdd" && !bIsReadOnly && bIsVisible) {
            oUpdatedRelationship.canAdd = oCellData.value;
          } else if (oCellData.id === "canRemove" && !bIsReadOnly && bIsVisible) {
            oUpdatedRelationship.canDelete = oCellData.value;
          } else if (oCellData.id === "canEdit" && !bIsReadOnly && bIsVisible) {
            oUpdatedRelationship.canEdit = oCellData.value;
          }
        });
        oADM.modifiedRelationshipPermissions.push(oUpdatedRelationship);
        delete aRowData.isDirty;
        delete aRowData.clonedData;
      }
    });
  };

  let _getModifiedGeneralInformationData = (oGeneralInformationBodyData, oHeaderPermission, bIsReadOnly) => {
    CS.forEach(oGeneralInformationBodyData, function (oData) {
      let bIsVisible = CS.find(oData, {id: "visible"}).value;
      CS.forEach(oData, (oProperty) => {
        switch (oData.rowId) {
          case getTranslation().IMAGE:
            if (oProperty.id === "visible") {
              oHeaderPermission.viewIcon = oProperty.value;
            } else if (oProperty.id === "canEdit" && !bIsReadOnly && bIsVisible) {
              oHeaderPermission.canChangeIcon = oProperty.value;
            }
            break;
          case getTranslation().NAME:
            if (oProperty.id === "visible") {
              oHeaderPermission.viewName = oProperty.value;
            } else if (oProperty.id === "canEdit" && !bIsReadOnly && bIsVisible) {
              oHeaderPermission.canEditName = oProperty.value;
            }
            break;
          case getTranslation().NATURE_TYPE:
            if (oProperty.id === "visible") {
              oHeaderPermission.viewPrimaryType = oProperty.value;
            }
            break;
          case getTranslation().ADDITIONAL_CLASSES:
            if (oProperty.id === "visible") {
              oHeaderPermission.viewAdditionalClasses = oProperty.value;
            } else if (oProperty.id === "canAdd" && !bIsReadOnly && bIsVisible) {
              oHeaderPermission.canAddClasses = oProperty.value;
            } else if (oProperty.id === "canRemove" && !bIsReadOnly && bIsVisible) {
              oHeaderPermission.canDeleteClasses = oProperty.value;
            }
            break;
          case getTranslation().TAXONOMIES:
            if (oProperty.id === "visible") {
              oHeaderPermission.viewTaxonomies = oProperty.value;
            } else if (oProperty.id === "canAdd" && !bIsReadOnly && bIsVisible) {
              oHeaderPermission.canAddTaxonomy = oProperty.value;
            } else if (oProperty.id === "canRemove" && !bIsReadOnly && bIsVisible) {
              oHeaderPermission.canDeleteTaxonomy = oProperty.value;
            }
            break;
          case getTranslation().LIFECYCLE_STATUS:
            if (oProperty.id === "visible") {
              oHeaderPermission.viewStatusTags = oProperty.value;
            } else if (oProperty.id === "canEdit" && !bIsReadOnly && bIsVisible) {
              oHeaderPermission.canEditStatusTag = oProperty.value;
            }
            break;
          case getTranslation().CREATED_LAST_MODIFIED_INFO:
            if (oProperty.id === "visible") {
              oHeaderPermission.viewCreatedOn = oProperty.value;
              oHeaderPermission.viewLastModifiedBy = oProperty.value;
            }
        }
        delete oData.isDirty;
        delete oData.clonedData;
      });
    });
  };

  let _getModifiedPropertyCollectionData = (oPropertyCollectionPermissions, aPropertyCollectionHeaderData, oADM, bIsReadOnly) => {
    let bIsVisible = CS.find(aPropertyCollectionHeaderData, {id: "visible"}).value;
    CS.forEach(aPropertyCollectionHeaderData, function (oProperty) {
      if (oProperty.id === "visible") {
        oPropertyCollectionPermissions.isVisible = oProperty.value
      } else if (oProperty.id === "canEdit" && !bIsReadOnly && bIsVisible) {
        oPropertyCollectionPermissions.canEdit = oProperty.value
      }
    });
    oADM.modifiedPropertyCollectionPermissions.push(oPropertyCollectionPermissions)
  }

  let _getModifiedPropertyData = (aPropertyCollectionBodyData, oPropertiesPermission, oADM, bIsReadOnly) => {
    CS.forEach(aPropertyCollectionBodyData, function (oRowData) {
      if (oRowData.isDirty) {
        let bIsVisible = CS.find(oRowData, {id: "visible"}).value;
        let oPropertyPermission = oPropertiesPermission[oRowData.rowId];
        CS.forEach(oRowData, function (oCellData) {
          if (oCellData.id === "visible") {
            oPropertyPermission.isVisible = oCellData.value
          } else if (oCellData.id === "canEdit" && !bIsReadOnly && bIsVisible) {
            oPropertyPermission.canEdit = oCellData.value
          }
        });
        let oProperty = CS.find(oADM.modifiedPropertyPermissions, {propertyId: oPropertyPermission.propertyId});
        if (CS.isEmpty(oProperty)) {
          oADM.modifiedPropertyPermissions.push(oPropertyPermission);
        }
        delete oRowData.isDirty;
        delete oRowData.clonedData;
      }
    });
  };

  let _createTablePermissionADM = () => {
    let oPermissions = PermissionProps.getActivePermission();
    if(CS.isEmpty(oPermissions) || !oPermissions.clonedObject){
      return
    }
    let oTemplateInPermissions = oPermissions.templateInPermission;
    let oTemplatePermissions = oTemplateInPermissions.templatePermission;
    let oPropertyCollectionPermissions = oTemplatePermissions.propertyCollectionPermission;
    let oRelationshipPermissions = oTemplatePermissions.relationshipPermission;
    let oHeaderPermission = oTemplatePermissions.headerPermission;
    let oPropertyPermission = oTemplatePermissions.propertyPermission;
    let sTemplateId = oTemplateInPermissions.templateDetails.id;
    let oActiveEntityInformation = PermissionProps.getActiveEntityInformation();

    let oNewPermissions = PermissionProps.getActivePermission().clonedObject;
    let oNewTemplateInPermissions = oNewPermissions.templateInPermission;
    let oOldTemplateInPermissions = oPermissions.templateInPermission;
    let aOldAllowedTemplates = oOldTemplateInPermissions.allowedTemplates || [];
    let aNewAllowedTemplates = oNewTemplateInPermissions.allowedTemplates || [];
    let oSelectedRole = RoleProps.getSelectedRole();
    let bIsReadOnly = oSelectedRole.isReadOnly;

    let oADM = {
      roleId: _getSelectedRole().id,
      templateId: sTemplateId,
      headerPermission: oHeaderPermission,
      modifiedRelationshipPermissions: [],
      modifiedReferencePermissions: [],
      modifiedPropertyCollectionPermissions: [],
      modifiedPropertyPermissions: [],
      modifiedTabPermissions: [],
      addedAllowedTemplates: [],
      deletedAllowedTemplates: [],
      entityId: oActiveEntityInformation.id,
      entityType: oActiveEntityInformation.type
    };

    let aNewAllowedTemplateIds = _getTemplateIds(aNewAllowedTemplates);
    let aOldAllowedTemplateIds = _getTemplateIds(aOldAllowedTemplates);
    oADM.addedAllowedTemplates = CS.difference(aNewAllowedTemplateIds, aOldAllowedTemplateIds);
    oADM.deletedAllowedTemplates = CS.difference(aOldAllowedTemplateIds, aNewAllowedTemplateIds);

    /* GeneralInformation Data Prepare */
    if(PermissionProps.getSelectedEntityType() === ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS) {
      let oTablePropsForGeneralInformation = ContextualTableViewProps.getTableViewPropsByContext("generalInformation");
      let oGeneralInformationBodyData = oTablePropsForGeneralInformation.getTableViewRowData();
      if (oTablePropsForGeneralInformation.getIsTableViewDataDirty()) {
        _getModifiedGeneralInformationData(oGeneralInformationBodyData, oHeaderPermission, bIsReadOnly);
        oTablePropsForGeneralInformation.setIsTableViewDataDirty(false);
        let oGeneralInformationHeaderData = oTablePropsForGeneralInformation.getTableViewHeaderData();
        delete oGeneralInformationHeaderData.isDirty;
        delete oGeneralInformationHeaderData.clonedData;
      }

      /* Relationship Data Prepare */
      let oTablePropsForRelationship = ContextualTableViewProps.getTableViewPropsByContext("relationship");
      let oRelationshipBodyData = oTablePropsForRelationship.getTableViewRowData();
      if (oTablePropsForRelationship.getIsTableViewDataDirty()) {
        _getModifiedRelationship(oRelationshipBodyData, oRelationshipPermissions, oADM, bIsReadOnly);
        oTablePropsForRelationship.setIsTableViewDataDirty(false);
      }
    }
    /* PropertyCollection Data Prepare */
    CS.forEach(oPropertyCollectionPermissions, function (oPropertyCollectionPermission, key) {
      let oTablePropsForPropertyCollection = ContextualTableViewProps.getTableViewPropsByContext(key);
      let aPropertyCollectionHeaderData = oTablePropsForPropertyCollection.getTableViewHeaderData();
      let aPropertyCollectionBodyData = oTablePropsForPropertyCollection.getTableViewRowData();
      if(oTablePropsForPropertyCollection.getIsTableViewDataDirty()){
        if (aPropertyCollectionHeaderData.isDirty) {
          _getModifiedPropertyCollectionData(oPropertyCollectionPermission, aPropertyCollectionHeaderData, oADM, bIsReadOnly);
          delete aPropertyCollectionHeaderData.isDirty
          delete aPropertyCollectionHeaderData.clonedData;
        }
        /* Property Data Prepare */
        _getModifiedPropertyData(aPropertyCollectionBodyData, oPropertyPermission, oADM, bIsReadOnly);
        oTablePropsForPropertyCollection.setIsTableViewDataDirty(false)
      }
    });
    return oADM;
  };

  var _savePermission = function (oCallback) {
     _saveTemplatePermission(oCallback).then(_saveGlobalPermissions.bind(this, oCallback))
         .then(alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().PERMISSION})))
         .then(_triggerChange);
  };

  let _discardUnsavedPermission = function (oCallbackData) {
    let oActivePermission = PermissionProps.getActivePermission();
    if (oActivePermission.clonedObject) {
      let oTemplateInPermissions = oActivePermission.templateInPermission;
      let oPropertyCollectionPermissions = oTemplateInPermissions.templatePermission.propertyCollectionPermission;
      delete oActivePermission.clonedObject;
      delete oActivePermission.isDirty;

      if (PermissionProps.getSelectedEntityType() === ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS) {
        /* GeneralInformation Data Prepare */
        _discardTableData("generalInformation");
        /* Relationship Data Prepare */
        _discardTableData("relationship");
      }
      /* PropertyCollection Data Prepare */
      CS.forEach(oPropertyCollectionPermissions, function (oPropertyCollectionPermission, key) {
        _discardTableData(key);
      });
    }
    PermissionProps.setIsPermissionDirty(false);
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    PermissionProps.setPermissionScreenLockStatus(false);
    let oSelectedRole = RoleProps.getSelectedRole();

    let oModifiedPermissionMap = PermissionProps.getModifiedPermissionMap();
    let oPermissionMap = PermissionProps.getPermissionMap();

    CS.forEach(oModifiedPermissionMap, function (modifiedProperty, key) {
      let oPermission = oPermissionMap[key];
      if(oSelectedRole.isReadOnly){
        modifiedProperty.canDownload = oPermission.canDownload;
      } else {
        modifiedProperty.canCreate = oPermission.canCreate;
        modifiedProperty.canEdit = oPermission.canEdit;
        modifiedProperty.canDelete = oPermission.canDelete;
        modifiedProperty.canRead = oPermission.canRead;
        modifiedProperty.canDownload = oPermission.canDownload;
      }
    });

    PermissionProps.setModifiedFunctionalPermissionMap(CS.cloneDeep(PermissionProps.getFunctionalPermissionMap()));

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var _saveTemplatePermission = function (oCallback) {
    var oADM = _createTablePermissionADM();

    oCallback = oCallback || {};
    if (CS.isNotEmpty(oADM)) {
      return SettingUtils.csPostRequest(PermissionRequestMapping.SaveTemplatePermission, {}, oADM, successSaveTemplatePermissionCallback.bind(this, oCallback), failureFetchPermissionByIdCallback);
    } else {
      return CommonUtils.resolveEmptyPromise();
    }
  };

  var _handleEditBlackListItem = function (iIndex, sValue) {
    var oActivePermission = _makeActivePermissionDirty();
    var aBlackList = oActivePermission.list;
    aBlackList[iIndex] = sValue;
  };

  var _handleAddNewBlackListItem = function (sValue) {
    var oActivePermission = _makeActivePermissionDirty();
    var aBlackList = oActivePermission.list;
    aBlackList.push(sValue);
    oActivePermission.list = aBlackList;
  };

  var _handleRemoveBlackListItem = function (iIndex) {
    var oActivePermission = _makeActivePermissionDirty();
    var aBlackList = oActivePermission.list;
    aBlackList.splice(iIndex, 1);
  };

  var _fetchTemplateById = function (sId) {
    var oData = {
      templateId: sId,
      roleId: PermissionProps.getActiveRoleId()
    };
    SettingUtils.csPostRequest(PermissionRequestMapping.GetTemplatePermission, {}, oData, successFetchTemplateById, failureFetchTemplateById);
  };

  let _setSelectedTypeAndEntity = (oType) => {
    PermissionProps.setSelectedTypeAndEntity(oType);
  };

  let _handlePermissionViewOpened = () => {
    let aEntityTreeNavigationTreeData = new EntityTreeNavigationTreeData();
    let oEntityNavigationTreeVisualData = {};
    let sRoleId = RoleProps.getSelectedRole().id;
    let oRoleMaster = SettingUtils.getAppData().getRoleList()[sRoleId];
    oRoleMaster = oRoleMaster.clonedObject ? oRoleMaster.clonedObject : oRoleMaster;
    let aEntities = oRoleMaster.entities ? oRoleMaster.entities : [];
    CS.forEach(aEntityTreeNavigationTreeData, function (oData, iIndex) {
      oEntityNavigationTreeVisualData[oData.id] = {
        isExpanded: !iIndex
      }
      if(oData.id === ConfigEntityTypeDictionary.ENTITY_TYPE_TASK && oRoleMaster.isReadOnly) {
        oData.isDisabled = true;
      }
    });

    let oClassEntityTreeData = CS.find(aEntityTreeNavigationTreeData, {id: 'class'});
    if (!CS.isEmpty(aEntities)) {
      CS.remove(oClassEntityTreeData.children, function (oData) {
        let oClassConstant = oClassCategoryConstants[oData.id];
        if (oClassConstant && oClassConstant.moduleId === ClassCategoryConstants.TARGET_CLASS) {
          if (!CS.includes(aEntities, oClassConstant.domainId)) {
            return oData;
          }
        } else if (!CS.includes(aEntities, oClassConstant.domainId)) {
          return oData;
        }
      });
    }

    PermissionProps.setEntityNavigationTreeData(aEntityTreeNavigationTreeData);
    PermissionProps.setEntityNavigationTreeVisualData(oEntityNavigationTreeVisualData);

    let oClasses = aEntityTreeNavigationTreeData[0];
    let oEntity = oClasses.children[0];
    _fetchEntityChildrenWithGlobalPermissions(oEntity.id, ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS);

  };

  let _getEntityType = (sId) => {
    switch (sId) {
      case ClassCategoryConstants.ARTICLE_CLASS:
        return 'article';
      case ClassCategoryConstants.ASSET_CLASS:
        return 'asset';
      case ClassCategoryConstants.TARGET_CLASS:
        return 'target';
      case ClassCategoryConstants.SUPPLIER_CLASS:
        return 'supplier';
      case ClassCategoryConstants.TEXTASSET_CLASS:
        return 'textAsset';
      case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
        return 'masterTaxonomy';
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TASK:
        return 'task';
      case PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION:
        return  'function';
      default:
        return null;
    }
  };

  let _getEntitiesArray = () => {
    let aEntities = CS.values(ClassCategoryConstants);
    aEntities.push(ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY);
    aEntities.push(ConfigEntityTypeDictionary.ENTITY_TYPE_TAXONOMYMASTERLIST);
    aEntities.push(ConfigEntityTypeDictionary.ENTITY_TYPE_TASK);
    aEntities.push(PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION);

    return aEntities;
  };

  let _getIdAndEntityType = (sItemId) => {
    let aEntities = _getEntitiesArray();

    let sId = sItemId;
    let sEntityType = _getEntityType(PermissionProps.getSelectedTreeItemId());

    if (CS.includes(aEntities, sItemId)) {
      sId = "-1";
    }

    return {id: sId, entityType: sEntityType};

  };

  let _handleMenuListExpandToggled = (sItemId) => {
    let oEntityNavigationTreeVisualData = PermissionProps.getEntityNavigationTreeVisualData();
    let oSelectedTreeValues = oEntityNavigationTreeVisualData[sItemId];
    oSelectedTreeValues.isExpanded = !oSelectedTreeValues.isExpanded;
    _triggerChange();
  };

  let _clearHierarchyTreeWithPaginationAndVisualData = (aIdsToExclude) => {
    let oHierarchyTree = PermissionProps.getHierarchyTree();
    let aIds = SettingUtils.getAllIdsInTree([oHierarchyTree]);
    aIds = CS.difference(aIds, aIdsToExclude);
    PermissionProps.setAllEntityChildrenPaginationData(CS.omit(PermissionProps.getAllEntityChildrenPaginationData(), aIds));
    PermissionProps.setHierarchyTreeVisualData(CS.omit(PermissionProps.getHierarchyTreeVisualData(), aIds));
    PermissionProps.setHierarchyTree({});
  };

  let _fetchEntityChildrenWithGlobalPermissions = (sItemId, sNewSelectedEntityType, bIsLoadMore) => {
    let bPermissionScreenLockStatus = _getPermissionScreenLockStatus();
    if (!bIsLoadMore) {
      if (bPermissionScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = () => {
          _fetchEntityChildrenWithGlobalPermissions(sItemId, sNewSelectedEntityType, bIsLoadMore);
        };
        _showScreenLockedAlertify(oCallbackData);
        return
      }
    }

    let sOldSelectedEntityType = PermissionProps.getSelectedEntityType();

    // since sNewSelectedEntityType is undefined in case of 'tasks' & 'events'
    if (sItemId === ConfigEntityTypeDictionary.ENTITY_TYPE_TASK || sItemId === PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION
        || sItemId === ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
      sNewSelectedEntityType = sItemId;
      /**Enabled search for tasks. Before removing this please check, search is working for tasks**/
      /*if (sOldSelectedEntityType === sNewSelectedEntityType && !bIsLoadMore) {
        return;
      }*/
    }

    if (sNewSelectedEntityType && (sNewSelectedEntityType !== sOldSelectedEntityType)) {
      PermissionProps.setSelectedEntityType(sNewSelectedEntityType);
      _clearHierarchyTreeWithPaginationAndVisualData([]);
      PermissionProps.setAllEntityChildrenPaginationData({});
    }

    let sEntityType = PermissionProps.getSelectedEntityType();

    if (sEntityType === ConfigEntityTypeDictionary.ENTITY_TYPE_TASK && sItemId !== ConfigEntityTypeDictionary.ENTITY_TYPE_TASK) {
      return;
    }

    if (sEntityType === PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION && sItemId !== PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION) {
      return;
    }

    let aEntities = _getEntitiesArray();

    if(!(sEntityType === "class" && !CS.includes(ClassCategoryConstants, sItemId))) {

      let oPaginationData = PermissionProps.getEntityChildrenPaginationDataByEntityId(sItemId);

      if (CS.includes(aEntities, sItemId)) {
        if (!bIsLoadMore) {
          _clearHierarchyTreeWithPaginationAndVisualData([sItemId]);
        }
        PermissionProps.setSelectedTreeItemId(sItemId);
      }

      let oRequestData = _getIdAndEntityType(sItemId);
      CS.assign(oRequestData, oPaginationData);
      oRequestData.roleId = _getSelectedRole().id;

      if(PermissionProps.getSelectedTreeItemId() === ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY){
        if(CS.isEmpty(PermissionProps.getActiveTabId())){
          PermissionProps.setActiveTabId(TaxonomyTypeDictionary.MAJOR_TAXONOMY);
        }
        oRequestData.taxonomyType = PermissionProps.getActiveTabId();
      }

      let fSuccess = successFetchEntityChildrenWithGlobalPermissions.bind(this, sItemId, oRequestData);
      let fFailure = failureFetchEntityChildrenWithGlobalPermissions;

      if (sEntityType === PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION) {
        SettingUtils.csGetRequest(PermissionRequestMapping.GetPermissionFunction, {roleId: oRequestData.roleId},
            fSuccess, fFailure);
      } else {
        SettingUtils.csPostRequest(PermissionRequestMapping.GetHierarchyPermission, {}, oRequestData,
            fSuccess, fFailure);
      }
    }

    if (!bIsLoadMore) {
      if (!CS.includes(aEntities, sItemId)) {
        let sType = "";
        if (sEntityType === 'class') {
          sType = "klass";
        } else if (sEntityType === 'attributionTaxonomy') {
          sType = "attributionTaxonomy";
        }
        if (!CS.isEmpty(sType)) {
          _fetchDetailedPermissionsForEntity(sItemId, sType);
        }
      } else {
        PermissionProps.setActivePermission({});
        PermissionProps.setActivePermissionEntity({});
      }
    }

  };

  let _getDefaultVisualData = (iLevel) => {
    return {
      isActive: false,
      isExpanded: false,
      isSelected: false,
      isCollapsed: true,
      level: iLevel,
    };
  };

  let _clearChildrenOfSiblings = (sSelectedItemId) => {
    let oVisualData = PermissionProps.getHierarchyTreeVisualData();
    let oSelectedItemVisualData = oVisualData[sSelectedItemId];
    if (!CS.isEmpty(oSelectedItemVisualData)) {
      let iLevel = oSelectedItemVisualData.level;
      let aSiblingNodeIds = CS.map(oVisualData, function (oData, sKey) {
        if (oData.level == iLevel)
          return sKey;
        else
          return sSelectedItemId;
      });
      aSiblingNodeIds = CS.without(aSiblingNodeIds, sSelectedItemId);
      CS.forEach(aSiblingNodeIds, function (sId) {
        let oNode = SettingUtils.getNodeFromTreeListById([PermissionProps.getHierarchyTree()], sId);
        oNode.children = [];
      });
    }
  };

  let _getLevelForOwnChildren = (sSelectedItemId) => {
    let oVisualData = PermissionProps.getHierarchyTreeVisualData();
    let oSelectedItemVisualData = oVisualData[sSelectedItemId];
    return CS.isEmpty(oSelectedItemVisualData) ? 0 : oSelectedItemVisualData.level + 1;
  };

  let _getPermissionForEntity = (oPermission, sPermissionId, bIsFunctionPermission) => {
    let oSelectedRole = RoleProps.getSelectedRole();
    if(oSelectedRole.isReadOnly) {
      if (bIsFunctionPermission) {
        oPermission[sPermissionId] = false;
      } else {
        oPermission.canCreate = false;
        oPermission.canDelete = false;
        oPermission.isDisabled = true;
      }
    }
    return oPermission;
  };

  /**
   * Fill roll permissions functions details.
   * @param aChildren
   * @param oSelectedRole
   * @param oFunctionalPermissionMap
   * @param oModifiedFunctionalPermissionMap
   * @param oHierarchyTreeVisualData
   * @private
   */
  let _fillRolePermissionFunctionDetails = (aChildren, oSelectedRole, oFunctionalPermissionMap, oModifiedFunctionalPermissionMap, oHierarchyTreeVisualData) => {
    CS.forEach(aChildren, function (oChild) {
      oHierarchyTreeVisualData[oChild.id] = _getDefaultVisualData();
      let aEnabledFunctionPermissionIds = ["canExport", "canShare"];
      let oChildPermission = oChild.permission;
      if(!CS.includes(aEnabledFunctionPermissionIds, oChild.id)) {
        oChild.isDisabled = oSelectedRole.isReadOnly;
        oChildPermission = _getPermissionForEntity(oChild.permission, oChild.id, true);
      }
      !oModifiedFunctionalPermissionMap[oChild.id] && (oModifiedFunctionalPermissionMap[oChild.id] = CS.cloneDeep(oChildPermission));
      oFunctionalPermissionMap[oChild.id] = CS.cloneDeep(oChildPermission);
      if (oChild.hasOwnProperty('children') && CS.isNotEmpty(oChild.children)) {
        _fillRolePermissionFunctionDetails(oChild.children, oSelectedRole, oFunctionalPermissionMap, oModifiedFunctionalPermissionMap, oHierarchyTreeVisualData);
      }
    });
  };

  /**
   * Prepare roll permission function details for children.
   * @param oLayoutData
   * @param aResponse
   * @returns {Array}
   * @private
   */
  let _prepareRolePermissionFunctionChildrenData = (oLayoutData, aResponse) => {
    let aChildrenData = [];
    let aLayoutData = CS.map(oLayoutData, function (oChild, sKey) {
      let bValue = aResponse[sKey];
      oChild.permission[sKey] = bValue;
      if (oChild.hasOwnProperty('children') && CS.isNotEmpty(oChild.children)) {
        aChildrenData = _prepareRolePermissionFunctionChildrenData(oChild.children, aResponse);
      }

      return {
        id: oChild.id,
        label: oChild.label,
        permission: oChild.permission,
        className: oChild.className,
        children: aChildrenData,
      }
    });

    return aLayoutData;
  };

  let successFetchEntityChildrenWithGlobalPermissions = (sSelectedItemId, oRequestData, oResponse) => {
    let oContentHierarchyData = PermissionProps.getHierarchyTree();
    let sParentId = PermissionProps.getSelectedTreeItemId();
    let aResponse = oResponse.success;
    let sEntity = PermissionProps.getSelectedEntityType();
    let oSelectedRole = RoleProps.getSelectedRole();
    let sRoleId = RoleProps.getSelectedRole().id;
    let oRoleMaster = SettingUtils.getAppData().getRoleList()[sRoleId];
    oRoleMaster = oRoleMaster.clonedObject ? oRoleMaster.clonedObject : oRoleMaster;
    let aEntities = oRoleMaster.entities ? oRoleMaster.entities : [];

    if(!CS.isEmpty(aEntities)) {
      CS.remove(aResponse, function (oData) {
        let oClassConstant = oClassCategoryConstants[oData.id];
        if(CS.isEmpty(oClassConstant) && oData.id === "market"){
          /** *Warning: this is special handling for target group i.e. for Market, Persona and Situation*/
          oClassConstant = oClassCategoryConstants[ClassCategoryConstants.TARGET_CLASS];
        }
        if (oClassConstant && !CS.includes(aEntities, oClassConstant.domainId)) {
          return oData;
        }
      });
    }

    let aChildren = [];
    if (sSelectedItemId === PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION) {
      aChildren = _prepareRolePermissionFunctionChildrenData(new RolePermissionFunctionLayoutData(), aResponse);
    } else {
      aChildren = CS.map(aResponse, function (oChild) {
        return {
          id: oChild.id,
          label: oChild.label,
          isNature: oChild.isNature,
          code: oChild.code,
          type: oChild.type,
          natureType: oChild.natureType,
          children: []
        };
      });
    }

    let oPermissionMap = PermissionProps.getPermissionMap();
    let oModifiedPermissionMap = PermissionProps.getModifiedPermissionMap();
    let oFunctionalPermissionMap = PermissionProps.getFunctionalPermissionMap();
    let oModifiedFunctionalPermissionMap = PermissionProps.getModifiedFunctionalPermissionMap();
    let oHierarchyTreeVisualData = PermissionProps.getHierarchyTreeVisualData();

    if (oRequestData.from === 0) {
      _clearChildrenOfSiblings(oRequestData.id);

      // Get path of ids from root node to selected node
      let aSelectedIds = SettingUtils.getNodePathIdsFromTreeById([oContentHierarchyData], sSelectedItemId);

      CS.forEach(oHierarchyTreeVisualData, function (oData, sKey) {
        // set isSelected true only for those ids that are in the path
        oData.isSelected = CS.includes(aSelectedIds, sKey);
      });
    }

    let iLevelForChildren = _getLevelForOwnChildren(oRequestData.id);

    if (sSelectedItemId === PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION) {
      _fillRolePermissionFunctionDetails(aChildren, oSelectedRole, oFunctionalPermissionMap, oModifiedFunctionalPermissionMap, oHierarchyTreeVisualData);
    } else {
      CS.forEach(aResponse, function (oChild) {
        oChild.permission.type = sEntity === "class" ? "klass" : sEntity;
        // do not update existing entity permissions in modified map
        oPermissionMap[oChild.id] = CS.cloneDeep(oChild.permission)
        let oChildPermission = _getPermissionForEntity(oChild.permission);
        !oModifiedPermissionMap[oChild.id] && (oModifiedPermissionMap[oChild.id] = CS.cloneDeep(oChildPermission));
        oHierarchyTreeVisualData[oChild.id] = _getDefaultVisualData(iLevelForChildren);
      });
    }

    if (CS.isEmpty(oContentHierarchyData)) {
      let aEntityArray = new EntityTreeNavigationTreeData();

      let oMainEntity = CS.find(aEntityArray, {id: PermissionProps.getSelectedEntityType()});

      let oEntity = CS.find(oMainEntity.children, {id: sParentId}) || oMainEntity;

      oContentHierarchyData = {
        id: oEntity.id,
        label: oEntity.label,
        code: oEntity.code,
        children: aChildren
      };

      oHierarchyTreeVisualData[oEntity.id] = _getDefaultVisualData();

      PermissionProps.setHierarchyTree(oContentHierarchyData);
    } else {
      let oNode = SettingUtils.getNodeFromTreeListById([oContentHierarchyData], sSelectedItemId);
      if (oRequestData.from === 0) {
        oNode.children = aChildren;
      } else {
        oNode.children = oNode.children.concat(aChildren);
      }
    }

    if (oRequestData.id !== "-1") {
      oHierarchyTreeVisualData[sSelectedItemId].isSelected = true;
      oHierarchyTreeVisualData[sSelectedItemId].isExpanded = true;
    } else {
      oHierarchyTreeVisualData[sParentId].isSelected = true;
      oHierarchyTreeVisualData[sParentId].isExpanded = true;
    }

    _triggerChange();
  };

  let failureFetchEntityChildrenWithGlobalPermissions = (oResponse) => {
    SettingUtils.failureCallback(oResponse, "failureFetchEntityChildrenWithGlobalPermissions", getTranslation());
    _triggerChange();
  };

  /**
   * Handling on toggling of function permissions.
   * @param sId
   * @param sValue
   * @param oFunctionalPermissionMap
   * @private
   */
  let _updateVisualPropsForPermissionFunctionOnToggleChange = (sId, sValue, oFunctionalPermissionMap) => {
    let oHierarchyTreeVisualData = PermissionProps.getHierarchyTreeVisualData();
    let aKeys = Object.keys(new RolePermissionFunctionBulkEditLayoutData());
    switch (sId) {
      case "canBulkEdit":
        let oBulkEditVisualData = oHierarchyTreeVisualData[sId];
        if (!oBulkEditVisualData.isSelected) {
          oBulkEditVisualData.isSelected = true;
          oBulkEditVisualData.isActive = true;
          oBulkEditVisualData.isCollapsed = false;
        }
        CS.forEach(aKeys, function (sKey) {
          let oFoundPermissionMapData = oFunctionalPermissionMap[sKey];
          oFoundPermissionMapData[sKey] = sValue;
        });
        PermissionProps.setActiveTabId(sId);
        break;
      case "canBulkEditClasses":
      case "canBulkEditProperties":
      case "canBulkEditTaxonomies":
        let bIsAnyBulkEditChildHasPermission = false;
        CS.forEach(aKeys, function (sKey) {
          let oFoundPermissionMapData = oFunctionalPermissionMap[sKey];
          if (oFoundPermissionMapData[sKey]) {
            bIsAnyBulkEditChildHasPermission = true;
            return false;
          }
        });
        if (!bIsAnyBulkEditChildHasPermission) {
          let oFoundPermissionMapData = oFunctionalPermissionMap["canBulkEdit"];
          oFoundPermissionMapData["canBulkEdit"] = bIsAnyBulkEditChildHasPermission;
        }
        break;
      default:
        let oBulkEditData = oHierarchyTreeVisualData["canBulkEdit"];
        oBulkEditData.isActive = false;
        oBulkEditData.isSelected = false;
        oBulkEditData.isCollapsed = true;
    }
  };

  let _handlePermissionToggled = (sId, sProperty, sValue) => {
    let oPermissionMap = PermissionProps.getModifiedPermissionMap();
    let oFunctionalPermissionMap = PermissionProps.getModifiedFunctionalPermissionMap();
    PermissionProps.setIsPermissionDirty(true);
    let oEntityPermission = oPermissionMap[sId];
    let oFunctionalEntityPermission = oFunctionalPermissionMap[sId];
    if (!CS.isEmpty(oEntityPermission)) {
      oEntityPermission[sProperty] = !oEntityPermission[sProperty];
      if (sProperty === 'canRead' && !oEntityPermission[sProperty]) {
        oEntityPermission.canCreate = false;
        oEntityPermission.canEdit = false;
        oEntityPermission.canDelete = false;
      } else if (oEntityPermission[sProperty]) {
        oEntityPermission.canRead = true;
      }
      _triggerChange();
    }
    if (CS.isNotEmpty(oFunctionalEntityPermission)) {
      oFunctionalEntityPermission[sId] = sValue;
    }
    _updateVisualPropsForPermissionFunctionOnToggleChange(sId, sValue, oFunctionalPermissionMap);
    PermissionProps.setPermissionScreenLockStatus(true);
    _triggerChange();
  };

  let _handleHierarchyTreeTabChanged = (sTabId) => {
    PermissionProps.setActiveTabId(sTabId);
    _fetchEntityChildrenWithGlobalPermissions(PermissionProps.getSelectedTreeItemId(), PermissionProps.getSelectedEntityType())
  };

  let _getADMForGlobalPermission = (oOldPermission, oNewPermission) => {
    oNewPermission.canCreate = oOldPermission.canCreate;
    oNewPermission.canDelete = oOldPermission.canDelete;
    return oNewPermission;
  };

  let _saveGlobalPermissions = (oCallBack) => {
    let aGlobalPermissionsToSave = [];
    let oFunctionalPermissionsToSave = {};
    let oOriginalPermissionMap = PermissionProps.getPermissionMap();
    let oModifiedPermissionMap = PermissionProps.getModifiedPermissionMap();
    let oModifiedFunctionalPermissionMap = PermissionProps.getModifiedFunctionalPermissionMap();
    let oOriginalFunctionalPermissionMap = PermissionProps.getFunctionalPermissionMap();

    CS.forEach(oModifiedPermissionMap, function (oModifiedPermission, sEntityId) {
      let oGlobalPermission = {};
      let oSelectedRole = RoleProps.getSelectedRole();
      if (oSelectedRole.isReadOnly) {
        if(oOriginalPermissionMap[sEntityId].canDownload !== oModifiedPermission.canDownload) {
          let oNewPermission = CS.assign({}, oModifiedPermission);
          oGlobalPermission = _getADMForGlobalPermission(oOriginalPermissionMap[sEntityId], oNewPermission);
        }
      }
      else if (!CS.isEqual(oModifiedPermission, oOriginalPermissionMap[sEntityId])) {
        oGlobalPermission = CS.cloneDeep(oModifiedPermission);
      }
      if(CS.isNotEmpty(oGlobalPermission)) {
        oGlobalPermission.type === ConfigEntityTypeDictionary.ENTITY_TYPE_TASK && (oGlobalPermission.type = "tasks");
        let oPermission = {
          entityId: sEntityId,
          globalPermission: oGlobalPermission,
          roleId: _getSelectedRole().id
        };
        aGlobalPermissionsToSave.push(oPermission);
      }
    });

    CS.forEach(oModifiedFunctionalPermissionMap, function (oModifiedPermission, sEntityId) {
      if (!CS.isEqual(oModifiedPermission, oOriginalFunctionalPermissionMap[sEntityId])) {
        let oFunctionalPermissions = CS.cloneDeep(oModifiedPermission);
        delete oFunctionalPermissions.type;
        CS.assign(oFunctionalPermissionsToSave, oFunctionalPermissions);
      }
    });
    if (CS.isNotEmpty(aGlobalPermissionsToSave) || CS.isNotEmpty(oFunctionalPermissionsToSave)) {

      let oPostData = {
        "roleId": _getSelectedRole().id,
        "functionPermission": oFunctionalPermissionsToSave,
        "list": aGlobalPermissionsToSave
      };
      return SettingUtils.csPostRequest(PermissionRequestMapping.SavePermission, {}, oPostData, successSaveGlobalPermissionsCallback.bind(this, oCallBack), failureSaveGlobalPermissionsCallback);
    } else {
      PermissionProps.setIsPermissionDirty(false);
      delete PermissionProps.getActivePermission().isDirty;
      PermissionProps.setPermissionScreenLockStatus(false);
      delete PermissionProps.getActivePermission().clonedObject;
      return CommonUtils.resolveEmptyPromise();
    }
  };

  let successSaveGlobalPermissionsCallback = (oCallBack, oResponse) => {
    PermissionProps.setIsPermissionDirty(false);
    PermissionProps.setPermissionScreenLockStatus(false);
    delete PermissionProps.getActivePermission().isDirty;
    delete PermissionProps.getActivePermission().clonedObject;
    let oSuccess = oResponse.success;
    let aGlobalPermissionFromServer = oSuccess.globalPermissionWithAllowedTemplates;
    let oPermissionMap = PermissionProps.getPermissionMap();
    CS.forEach(aGlobalPermissionFromServer, (oDataFromServer) => {
      let oOldPermissionMap = oPermissionMap[oDataFromServer.id];
      let oGlobalPermissions = oDataFromServer.globalPermission;
      let {canCreate, canDelete, canDownload, canEdit, canRead} = oGlobalPermissions;
      let obj = {canCreate, canDelete, canDownload, canEdit, canRead};
      CS.assign(oOldPermissionMap, obj);
    });

    let oFunctionPermission = oSuccess.functionPermission;
    let oOriginalFunctionalPermissionMap = PermissionProps.getFunctionalPermissionMap();
    CS.forEach(oFunctionPermission, (bValue, sKey) => {
      if (bValue !== null) {
        oOriginalFunctionalPermissionMap[sKey][sKey] = bValue;
      }
    });
    if(oCallBack && oCallBack.functionToExecute) {
      oCallBack.functionToExecute();
    }
  };

  let failureSaveGlobalPermissionsCallback = (oResponse) => {
    SettingUtils.failureCallback(oResponse, "failureSaveGlobalPermissionsCallback", getTranslation());
    _triggerChange();
  };

  let _closePermissionDialogAndClearProps = () => {
    PermissionProps.reset();
    SettingUtils.getOrganisationStore().setIsPermissionVisible(false);
    _triggerChange();
  };

  let _handlePermissionDialogButtonClicked = (sButtonId) => {
    switch (sButtonId) {
      case 'save':
        _savePermission();
        break;
      case 'ok':
        _closePermissionDialogAndClearProps();
        break;
      case 'discard':
        _discardUnsavedPermission({});
    }
  };

  let _updatePaginationDataOfEntity = (oRequestData) => {
    let oPaginationData = PermissionProps.getEntityChildrenPaginationDataByEntityId(oRequestData.clickedNodeId);

    if (oRequestData.loadMore) {
      let oNode = SettingUtils.getNodeFromTreeListById([PermissionProps.getHierarchyTree()], oRequestData.id);
      oPaginationData.from = oNode.children ? oNode.children.length : 0;

    } else {
      oPaginationData.searchText = oRequestData.searchText;
      oPaginationData.from = 0;
    }
  };

  let _makeRowDirty = (aRowData) => {
    if(!aRowData.isDirty) {
      aRowData.clonedData = CS.cloneDeep(aRowData);
      aRowData.isDirty = true;
    }
  };

  let _discardTableData = (sTableContext) => {
    let oTableProps = ContextualTableViewProps.getTableViewPropsByContext(sTableContext);
    let aTableBodyData = oTableProps.getTableViewRowData();
    let aTableHeaderData = oTableProps.getTableViewHeaderData()
    if (oTableProps.getIsTableViewDataDirty()) {
      if(aTableHeaderData.isDirty) {
        oTableProps.setTableViewHeaderData(aTableHeaderData.clonedData);
      }
      for(let index = 0; index < aTableBodyData.length; index++) {
        if(aTableBodyData[index].isDirty) {
          let sRowId = aTableBodyData[index].rowId;
          aTableBodyData[index] = aTableBodyData[index].clonedData;
          aTableBodyData[index].rowId = sRowId;
        }
      }
      oTableProps.setTableViewRowData(aTableBodyData);
      oTableProps.setIsTableViewDataDirty(false);
    }
  };

  /**
   * Handling for role permissions functions child click.
   * @param oReqData
   * @private
   */
  let _handleRolePermissionChildClicked = (oReqData) => {
    let sClickedNodeId = oReqData.clickedNodeId;
    let sEntity = PermissionProps.getSelectedEntityType();
    if (sEntity === PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION) {
      let oHierarchyTreeVisualData = PermissionProps.getHierarchyTreeVisualData();
      switch (sClickedNodeId) {
        case "canBulkEdit":
          let oFoundObject = oHierarchyTreeVisualData[sClickedNodeId];
          oFoundObject.isActive = true;
          oFoundObject.isSelected = true;
          oFoundObject.isCollapsed = false;
          PermissionProps.setActiveTabId(sClickedNodeId);
          break;
        case "canBulkEditClasses":
        case "canBulkEditProperties":
        case "canBulkEditTaxonomies":
          break;
        default:
          let oBulkEditData = oHierarchyTreeVisualData["canBulkEdit"];
          oBulkEditData.isActive = false;
          oBulkEditData.isSelected = false;
          oBulkEditData.isCollapsed = true;
      }
      _triggerChange();
    } else {
      _updatePaginationDataOfEntity(oReqData);
      _fetchEntityChildrenWithGlobalPermissions(oReqData.clickedNodeId, null, oReqData.loadMore);
    }
  };

  return {
    getPermissionScreenLockStatus: function () {
      return _getPermissionScreenLockStatus();
    },

    getActivePermission: function () {
      return PermissionProps.getActivePermission();
    },

    handlePermissionRoleListNodeClicked: function (sRoleId) {
      var bPermissionScreenLockStatus = _getPermissionScreenLockStatus();
      // var sSelectedRole = _getSelectedRole();
      _resetClassVisibilityStatus();
      PermissionProps.setActivePermissionEntity({});
      // if (!(sSelectedRole.id === sRoleId)) {
      if (bPermissionScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = () => {
          let oSelectedType = PermissionProps.getSelectedTypeAndEntity();
          if (!CS.isEmpty(oSelectedType)) {
            _fetchDetailedPermissionsForEntity(oSelectedType.id, oSelectedType.entityType);
          } else {
            _fetchPermissionById(sRoleId, {});
          }
        };
        _showScreenLockedAlertify(oCallbackData);
      } else {
        _fetchPermissionById(sRoleId, {});
        }
    },

    handlePermissionButtonToggled: function (sId, sProperty, sType, bForAllChildren) {
      _handlePermissionButtonToggled(sId, sProperty, sType, bForAllChildren);
    },

    handlePermissionSelectionToggled: function (sId, sType) {
      _handlePermissionSelectionToggled(sId, sType);
    },

    handlePermissionFirstLevelItemClicked: function (sId, sType) {
      _handlePermissionFirstLevelItemClicked(sId, sType);
    },

    handlePermissionKlassItemClicked: function (sId, sPermissionNodeId, sType) {
      _handlePermissionKlassItemClicked(sId, sPermissionNodeId, sType);
    },

    handleSetDefaultTemplateClicked: function (oDefaultTemplate) {
      _handleSetDefaultTemplateClicked(oDefaultTemplate);
    },

    handlePermissionRemoveTemplateClicked: function (sId) {
      _handlePermissionRemoveTemplateClicked(sId);
    },

    handleTableExpandCollapsedClicked: function (sId) {
      _handleTableExpandCollapsedClicked(sId)
          .then(_triggerChange);
    },

    handleTableButtonClicked: function (sTableId, sRowId, sId, sScreenContext) {
      _handleTableButtonClicked(sTableId, sRowId, sId, sScreenContext);
    },

    handlePermissionSectionStatusChanged: function (sStatus, sSectionId, aElements) {
      _handlePermissionSectionStatusChanged(sStatus, sSectionId, aElements);
    },

    handlePermissionElementStatusChanged: function (sStatus, sSectionId, sElementType, sParentSectionId) {
      _handlePermissionElementStatusChanged(sStatus, sSectionId, sElementType, sParentSectionId);
    },

    handlePermissionOfTemplateHeaderToggled: function (sType, sContext, sId) {
      _handlePermissionOfTemplateHeaderToggled(sType, sContext, sId);
    },

    handlePermissionRefreshMenuClicked: function () {
      _handlePermissionRefreshMenuClicked();
    },

    savePermission: function (oCallbackData) {
      _savePermission(oCallbackData);
    },

    discardUnsavedPermission: function (oCallbackData) {
      _discardUnsavedPermission(oCallbackData);
    },

    handleEditBlackListItem: function (iIndex, sValue) {
      _handleEditBlackListItem(iIndex, sValue);
      _triggerChange();
    },

    handleAddNewBlackListItem: function (sValue) {
      _handleAddNewBlackListItem(sValue);
      _triggerChange();
    },

    handleRemoveBlackListItem: function (sIndex) {
      _handleRemoveBlackListItem(sIndex);
      _triggerChange();
    },

    handlePermissionTemplateAdded: function (aSectionIds) {
      _handlePermissionTemplateAdded(aSectionIds);
    },

    setActivePermission: (oPermission) => {
      _setActivePermission(oPermission);
    },

    showScreenLockedAlertify: function (oCallBack) {
      _showScreenLockedAlertify(oCallBack);
    },

    setSelectedTypeAndEntity: function (oType) {
      _setSelectedTypeAndEntity(oType);
    },

    handlePermissionViewOpened: function () {
      _handlePermissionViewOpened();
    },

    handleMenuListExpandToggled: function (sItemId) {
      _handleMenuListExpandToggled(sItemId);
    },

    fetchEntityChildrenWithGlobalPermissions: function (sItemId, sParentId, bIsLoadMore) {
      _fetchEntityChildrenWithGlobalPermissions(sItemId, sParentId, bIsLoadMore);
    },

    handlePermissionToggled: function (sId, sProperty, sValue) {
      _handlePermissionToggled(sId, sProperty, sValue);
    },

    handleHierarchyTreeTabChanged: function (sTabId) {
      _handleHierarchyTreeTabChanged(sTabId);
    },

    handlePermissionDialogButtonClicked: function (sButtonId) {
      _handlePermissionDialogButtonClicked(sButtonId);
    },

    updatePaginationDataOfEntity: function (oRequestData) {
      _updatePaginationDataOfEntity(oRequestData);
    },

    handlePermissionRestoreMenuClicked: function () {
      _handlePermissionRestoreMenuClicked();
    },

    handleRolesPermissionsExportButtonClicked: function (oSelectiveExport) {
      _handleRolesPermissionsExportButtonClicked(oSelectiveExport);
    },

    handlePermissionFileUploaded: function(aFiles,oImportExcel){
      _handlePermissionFileUploaded(aFiles,oImportExcel);
    },

    handleRolePermissionChildClicked: function (oReqData) {
      _handleRolePermissionChildClicked(oReqData);
    },

    resetTableViewProps: function (context) {
      _resetTableViewProps(context);
    }
  }
})();

MicroEvent.mixin(PermissionStore);

export default PermissionStore;
