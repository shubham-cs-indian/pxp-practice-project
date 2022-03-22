import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import { ClassRequestMapping as oClassRequestMapping, ContextRequestMapping as oContextRequestMapping,
  TasksRequestMapping as oTaskRequestMapping, AssetRequestMapping as oAssetRequestMapping,
  TargetRequestMapping as oTargetRequestMapping,  TextAssetRequestMapping as oTextAssetRequestMapping,
  SupplierRequestMapping as oSupplierRequestMapping, UploadRequestMapping as oUploadRequestMapping}
  from '../../tack/setting-screen-request-mapping';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import ClassProps from './../model/class-config-view-props';
import ClassUtils from './../helper/class-utils';
import SettingUtils from './../helper/setting-utils';
import ContextUtils from './../helper/context-utils';
import MockDataForEntityBaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import NatureTypeDictionary from '../../../../../../commonmodule/tack/nature-type-dictionary.js';
import RelationshipTypeDictionary from '../../../../../../commonmodule/tack/relationship-type-dictionary';
import SecondaryTypeDictionary from '../../../../../../commonmodule/tack/secondary-type-dictionary';
import SystemLevelId from '../../../../../../commonmodule/tack/system-level-id-dictionary';
import {relAndContextCouplingTypes} from '../../../../../../commonmodule/tack/version-variant-coupling-types';
import MockDataForClassNatureTypes from './../../tack/mock/mock-data-for-class-nature-types';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../../commonmodule/tack/config-data-entities-dictionary';
import CouplingConstants from '../../../../../../commonmodule/tack/coupling-constans';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import assetTypes from '../../tack/coverflow-asset-type-list';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ViewUtils from "../../../contentscreen/view/utils/view-utils";
import ManageEntityStore from "./config-manage-entity-store";
import MockDataForTaxonomyInheritance from "../../../../../../commonmodule/tack/mock-data-for-taxonomy-inheritance";
import SettingScreenModuleDictionary from '../../../../../../commonmodule/tack/setting-screen-module-dictionary';
import ExportSide2RelationshipDictionary from '../../../../../../commonmodule/tack/export-side2-relationship-dictionary'
import oDataModelImportExportEntityTypeConstants from "../../tack/settinglayouttack/config-module-import-export-entity-list-dictionary";

var ClassStore = (function () {

  var _triggerChange = function () {
    ClassStore.trigger('class-changed');
  };

  var successFetchClassListCallback = function (oResponse) {
    var aClassList = SettingUtils.getAppData().getClassList();
    var oRootNode = CS.find(aClassList, {'id': SettingUtils.getTreeRootId()});
    let oKlass = oResponse.success.entity;
    oRootNode.children = oKlass.children;

    var oClassValueList = _getClassValueList();
    SettingUtils.addNewTreeNodesToValueList(oClassValueList, oRootNode.children, {'canCreate': true});

    //to set root node as active as well as selected node.
    _setActiveClass(oKlass.children[0]);
    oClassValueList[oKlass.children[0].id].isChecked = true;
    oClassValueList[oKlass.children[0].id].isSelected = true;

    var sContext = ClassProps.getSelectedClassCategory();
    return _fetchClassDetails(oKlass.children[0], sContext);
  };

  var failureFetchClassListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchClassListCallback", getTranslation());
  };

  var successFetchTextAssetListCallback = function (oResponse) {
    var aClassList = SettingUtils.getAppData().getTextAssetList();
    var oRootNode = CS.find(aClassList, {'id': SettingUtils.getTreeRootId()});
    let oClassFromServer = oResponse.success.entity;
    oRootNode.children = oClassFromServer.children;

    var oTextassetValueList = _getTextAssetValueList();
    SettingUtils.addNewTreeNodesToValueList(oTextassetValueList, oRootNode.children, {'canCreate': true});

    _setActiveClass(oClassFromServer.children[0]);
    oTextassetValueList[oClassFromServer.children[0].id].isChecked = true;
    oTextassetValueList[oClassFromServer.children[0].id].isSelected = true;

    var sContext = ClassProps.getSelectedClassCategory();
    return _fetchClassDetails(oClassFromServer.children[0], sContext);

    _triggerChange();
  };

  var failureFetchTextAssetListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTextAssetListCallback", getTranslation());
  };

  var successFetchSupplierListCallback = function (oResponse) {
    var aClassList = SettingUtils.getAppData().getSupplierList();
    var oRootNode = CS.find(aClassList, {'id': SettingUtils.getTreeRootId()});
    let oClassFromServer = oResponse.success.entity;
    oRootNode.children = oClassFromServer.children;

    var oSupplierValueList = _getSupplierValueList();
    SettingUtils.addNewTreeNodesToValueList(oSupplierValueList, oRootNode.children);

    _setActiveClass(oClassFromServer.children[0]);
    oSupplierValueList[oClassFromServer.children[0].id].isChecked = true;
    oSupplierValueList[oClassFromServer.children[0].id].isSelected = true;

    var sContext = ClassProps.getSelectedClassCategory();
    return _fetchClassDetails(oClassFromServer.children[0], sContext);

    _triggerChange();
  };

  var failureFetchSupplierListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchSupplierListCallback", getTranslation());
  };

  var successFetchTaskListCallback = function (oResponse) {
    var aTaskList = SettingUtils.getAppData().getTaskList();
    var oRootNode = CS.find(aTaskList, {'id': SettingUtils.getTreeRootId()});
    let oClassFromServer = oResponse.success.entity;
    oRootNode.children = oClassFromServer.children;
    var oTaskValueList = _getTaskValueList();
    SettingUtils.addNewTreeNodesToValueList(oTaskValueList, oRootNode.children, {'canCreate': true});
    _triggerChange();
  };

  var failureFetchTaskListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTaskListCallback", getTranslation());
  };

  var successFetchAssetListCallback = function (oResponse) {
    var aAssetList = SettingUtils.getAppData().getAssetList();
    var oRootNode = CS.find(aAssetList, {'id': SettingUtils.getTreeRootId()});
    let oClassFromServer = oResponse.success.entity;
    oRootNode.children = oClassFromServer.children;
    var oAssetValueList = _getAssetValueList();
    SettingUtils.addNewTreeNodesToValueList(oAssetValueList, oRootNode.children, {'canCreate': true});

    _setActiveClass(oClassFromServer.children[0]);
    oAssetValueList[oClassFromServer.children[0].id].isChecked = true;
    oAssetValueList[oClassFromServer.children[0].id].isSelected = true;

    var sContext = ClassProps.getSelectedClassCategory();
    return _fetchClassDetails(oClassFromServer.children[0], sContext);

    _triggerChange();
  };

  var failureFetchAssetListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAssetListCallback", getTranslation());
  };

  var successFetchTargetListCallback = function (oResponse) {
    var aTargetList = SettingUtils.getAppData().getTargetList();
    var oRootNode = CS.find(aTargetList, {'id': SettingUtils.getTreeRootId()});
    let oClassFromServer = oResponse.success.entity;
    oRootNode.children = oClassFromServer.children;

    var oTargetValueList = _getTargetValueList();
    SettingUtils.addNewTreeNodesToValueList(oTargetValueList, oRootNode.children, {'canCreate': true});

    _setActiveClass(oClassFromServer.children[0]);
    oTargetValueList[oClassFromServer.children[0].id].isChecked = true;
    oTargetValueList[oClassFromServer.children[0].id].isSelected = true;

    var sContext = ClassProps.getSelectedClassCategory();
    return _fetchClassDetails(oClassFromServer.children[0], sContext);
    _triggerChange();
  };

  var failureFetchTargetListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTargetListCallback", getTranslation());
  };

  var successFetchAllClassesFlatList = function (oCallbackData, oResponse) {
      SettingUtils.getAppData().setAllClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();

    } else {
      _triggerChange();
    }
  };

  var successFetchAllCollectionClassesFlatList = function (oCallbackData, oResponse) {
      SettingUtils.getAppData().setAllCollectionClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();

    } else {
      _triggerChange();
    }
  };

  var successFetchAllSetClassesFlatList = function (oCallbackData, oResponse) {
      SettingUtils.getAppData().setAllSetClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();

    } else {
      _triggerChange();
    }
  };

  var successFetchAllTaskClassesFlatList = function (oCallbackData, oResponse) {
      SettingUtils.getAppData().setAllTaskClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();

    } else {
      _triggerChange();
    }
  };

  var successFetchAllAssetClassesFlatList = function (oCallbackData, oResponse) {
      SettingUtils.getAppData().setAllAssetClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();

    } else {
      _triggerChange();
    }
  };

  var successFetchAllAssetCollectionClassesFlatList = function (oCallbackData, oResponse) {
      SettingUtils.getAppData().setAllAssetCollectionClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();

    } else {
      _triggerChange();
    }
  };

  var successFetchAllTextAssetClassesFlatList = function (oCallbackData, oResponse) {
    SettingUtils.getAppData().setAllTextAssetClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();

    } else {
      _triggerChange();
    }
  };

  var successFetchAllSupplierClassesFlatList = function (oCallbackData, oResponse) {
    SettingUtils.getAppData().setAllSupplierClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    } else {
      _triggerChange();
    }
  };

  var successFetchAllMarketTargetClassesFlatList = function (oCallbackData, oResponse) {
      SettingUtils.getAppData().setAllMarketTargetClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    } else {
      _triggerChange();
    }
  };

  var successFetchAllTargetCollectionClassesFlatList = function (oCallbackData, oResponse) {
      SettingUtils.getAppData().setAllTargetCollectionClassesFlatList(oResponse.success);
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    } else {
      _triggerChange();
    }
  };

  var failureFetchAllClassesFlatList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAllClassesFlatList", getTranslation());
  };

  var failureFetchAllClasesFlatList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAllClasesFlatList", getTranslation());
  };

  var failureFetchAllCollectionClasesFlatList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAllCollectionClasesFlatList", getTranslation());
  };

  var failureFetchAllSetClasesFlatList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAllSetClasesFlatList", getTranslation());
  };

  var successSaveClassCallback = function (oCallback, iClassId, bSilentMode, oServerResponse) {

    let oConfigDetails = oServerResponse.success.configDetails;
    var oClassFromServer = oServerResponse.success.entity;
    _updateReferencedItems(oServerResponse.success);
    var sNewClassId = oClassFromServer.id;
    var oClassValueList = _getClassValueList();
    var oClassMasterObj = _getClassFromMasterListById(iClassId);
    var oClassValueObj = oClassValueList[iClassId];
    oClassValueObj.label = oClassFromServer.label;
    oClassValueObj.isEditable = false;

    oClassMasterObj.id = oClassFromServer.id;
    oClassValueObj.id = oClassFromServer.id;
    delete oClassValueList[iClassId];
    oClassValueList[sNewClassId] = oClassValueObj;
    var oSectionMap = ClassProps.getClassSectionMap();
    CS.forEach(oClassFromServer.sections, function (oSection) {
      if (oSectionMap[oSection.propertyCollectionId]) {
        oSectionMap[oSection.propertyCollectionId].isAvailable = false;
      }
      CS.forEach(oSection.elements, function (oElement) {
        if (oElement.type == 'attribute') {
          oElement.isVariating = false;
        }
      });
    });
    delete oClassMasterObj.isCreated;
    delete oClassMasterObj.clonedObject;
    delete oClassMasterObj.isDirty;
    CS.assign(oClassMasterObj, oClassFromServer);
    _setActiveClass(oClassMasterObj);
    ClassUtils.setClassScreenLockedStatus(false);

    ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
    _setClassContextId(oClassFromServer);

    SettingUtils.addNewTreeNodesToValueList(oClassValueList, oClassMasterObj.children, {'canCreate': true, 'canDelete': true});

    let oHierarchyTree = SettingUtils.getAppData().getClassListByTypeGeneric("class");
    let oParentNode = SettingUtils.getParentNodeByChildId(oHierarchyTree, oClassValueObj.id);
    let aChildrenKeyValueToReset = [
      {key: "isExpanded", value: false}
    ];
    SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oClassValueList, true);
    oClassValueObj.isExpanded = true;
    if (oCallback.isCreated) {
      alertify.success(getTranslation().CLASS_CREATED_SUCCESSFULLY);
    } else {
     // oCallback.isDeletedAnyContext && SettingUtils.showMessage(getTranslation().MODIFY_CLASS_WILL_TAKE_SOME_TIME);
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().S_CLASS}));
    }

    _storeAllSectionRoleList(oClassMasterObj);
    SettingUtils.setInheritanceAndCutOffUIProperties(oClassMasterObj.sections);
    _updateReferencedTabs(oConfigDetails.referencedTabs);

    _processAfterGet(oClassMasterObj);

    ClassProps.setSelectedSectionId('');

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _fetchAllClassesFlatList({});
    _triggerChange();
  };

  var failureSaveClassCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        var aClassList = SettingUtils.getAppData().getClassList();
        var oActiveClass = ClassUtils.getActiveClass();
        SettingUtils.getNodeFromTreeListById(aClassList, oActiveClass.id);
        SettingUtils.removeNodeById(aClassList, oActiveClass.id);
        _setActiveClass({});
        ClassUtils.setClassScreenLockedStatus(false);
        alertify.error("[" + oActiveClass.label +"] "+getTranslation().ERROR_ALREADY_DELETED);
        _triggerChange();
      } else if(!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "ParentChildRelationshipException"}))){
        SettingUtils.failureCallback(oResponse, "failureSaveClassCallback", getTranslation());
        _triggerChange(); //This trigger is necessary
      } else {
        SettingUtils.failureCallback(oResponse, "failureSaveClassCallback", getTranslation());
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveClassCallback", getTranslation());
    }
  };

  var successSaveTaskClassCallback = function (oCallback, iClassId, bSilentMode, oServerResponse) {

    var oTaskFromServer = oServerResponse.success.entity;
    let oConfigDetails = oServerResponse.success.configDetails;
    var aTaskList = SettingUtils.getAppData().getTaskList();
    var oTaskValueList = _getTaskValueList();
    var oTaskMasterObj = _getClassFromMasterListById(iClassId, aTaskList);
    var oTaskValueObj = oTaskValueList[iClassId];
    oTaskValueObj.label = oTaskFromServer.label;
    oTaskValueObj.isEditable = false;
    oTaskValueObj.isLoading = false;
    if (oTaskMasterObj.isCreated) {
      oTaskMasterObj.id = oTaskFromServer.id;
      oTaskValueObj.id = oTaskFromServer.id;
      oTaskValueList[oTaskFromServer.id] = oTaskValueObj;
      delete oTaskValueList[iClassId];
    }
    delete oTaskMasterObj.isCreated;
    delete oTaskMasterObj.clonedObject;
    delete oTaskMasterObj.isDirty;
    CS.assign(oTaskMasterObj, oTaskFromServer);
    _switchActiveClass(oTaskValueObj, 'task');
    ClassUtils.setClassScreenLockedStatus(false);
    SettingUtils.setInheritanceAndCutOffUIProperties(oTaskMasterObj.sections);
    CS.forEach(oTaskMasterObj.children, function (oActiveChild) {
      var oActiveChildValue = oTaskValueList[oActiveChild.id];
      oActiveChildValue.isExpanded = false;
      oActiveChildValue.isChildLoaded = false;
    });
    _alertifySuccess(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().TASK}), bSilentMode);
    _storeAllSectionRoleList(oTaskMasterObj);
    ClassProps.setSelectedSectionId('');
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _fetchAllClassesFlatList({});
    _triggerChange();
  };

  var failureSaveTaskClassCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        var aTaskList = SettingUtils.getAppData().getTaskList();
        var oActiveClass = ClassUtils.getActiveClass();
        SettingUtils.getNodeFromTreeListById(aTaskList, oActiveClass.id);
        SettingUtils.removeNodeById(aTaskList, oActiveClass.id);
        _setActiveClass({});
        ClassUtils.setClassScreenLockedStatus(false);
        alertify.error(getTranslation().TASK_NOT_FOUND, 0);
        _triggerChange();
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveTaskClassCallback", getTranslation());
    }
  };

  var successSaveAssetClassCallback = function (oCallback, iClassId, bSilentMode, oServerResponse) {
    var oAssetFromServer = oServerResponse.success.entity;
    let oConfigDetails = oServerResponse.success.configDetails;
    _updateReferencedItems(oServerResponse.success);
    var aAssetList = SettingUtils.getAppData().getAssetList();
    var oAssetValueList = _getAssetValueList();
    var oAssetMasterObj = _getClassFromMasterListById(iClassId, aAssetList);
    var oAssetValueObj = oAssetValueList[iClassId];
    oAssetValueObj.label = oAssetFromServer.label;
    oAssetValueObj.isEditable = false;
    oAssetValueObj.isLoading = false;
    _sortEmbeddedIdsAndTechnicalImageIds(oAssetFromServer, oConfigDetails.referencedKlasses);
    if (oAssetMasterObj.isCreated) {
      oAssetMasterObj.id = oAssetFromServer.id;
      oAssetValueObj.id = oAssetFromServer.id;
      oAssetValueList[oAssetFromServer.id] = oAssetValueObj;
      delete oAssetValueList[iClassId];
    }
    delete oAssetMasterObj.isCreated;
    delete oAssetMasterObj.clonedObject;
    delete oAssetMasterObj.isDirty;
    CS.assign(oAssetMasterObj, oAssetFromServer);
    _switchActiveClass(oAssetValueObj, 'asset');
    ClassUtils.setClassScreenLockedStatus(false);
    SettingUtils.setInheritanceAndCutOffUIProperties(oAssetMasterObj.sections);
    CS.forEach(oAssetMasterObj.children, function (oActiveChild) {
      var oActiveChildValue = oAssetValueList[oActiveChild.id];
      oActiveChildValue.isExpanded = false;
      oActiveChildValue.isChildLoaded = false;
    });
    ClassProps.setSelectedSectionId('');

    ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
    _setClassContextId(oAssetFromServer);

    let oHierarchyTree = SettingUtils.getAppData().getClassListByTypeGeneric("asset");
    let oParentNode = SettingUtils.getParentNodeByChildId(oHierarchyTree, oAssetValueObj.id);
    let aChildrenKeyValueToReset = [
      {key: "isExpanded", value: false}
    ];
    SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oAssetValueList, true);
    oAssetValueObj.isExpanded = true;
    if (oCallback.deletedExtensionConfiguration && !CS.find(oAssetFromServer.extensionConfiguration, {id: oCallback.deletedExtensionConfiguration})) {
      /*
      * verifying if the deleted id exists in the extensionConfiguration of response.
      * */
      alertify.success(getTranslation().EXTENSION_DELETED_SUCCESSFULLY);
    } else if (oCallback.isCreated) {
      alertify.success(getTranslation().CLASS_CREATED_SUCCESSFULLY);
    } else {
      _alertifySuccess(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().S_CLASS}), bSilentMode);
    }
    _storeAllSectionRoleList(oAssetMasterObj);
    _fetchAllClassesFlatList({});
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var failureSaveAssetClassCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        var aAssetList = SettingUtils.getAppData().getAssetList();
        var oActiveClass = ClassUtils.getActiveClass();
        SettingUtils.getNodeFromTreeListById(aAssetList, oActiveClass.id);
        SettingUtils.removeNodeById(aAssetList, oActiveClass.id);
        _setActiveClass({});
        ClassUtils.setClassScreenLockedStatus(false);
        alertify.error(getTranslation().ASSET_NOT_FOUND, 0);
        _triggerChange();
      } else if(!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "ParentChildRelationshipException"}))){
        SettingUtils.failureCallback(oResponse, "failureSaveAssetClassCallback", getTranslation());
        _triggerChange();
      } else if(!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "ExtensionAlreadyExistsException"}))){
        _handleFailureOnAssetUpload(oResponse);
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveAssetClassCallback", getTranslation());
    }
  };

  var successSaveTargetClassCallback = function (oCallback, iClassId, bSilentMode, oServerResponse) {

    var oTargetFromServer = oServerResponse.success.entity;
    let oConfigDetails = oServerResponse.success.configDetails;
    _updateReferencedItems(oServerResponse.success);
    var aTargetList = SettingUtils.getAppData().getTargetList();
    var oTargetValueList = _getTargetValueList();
    var oTargetMasterObj = _getClassFromMasterListById(iClassId, aTargetList);
    var oTargetValueObj = oTargetValueList[iClassId];
      oTargetValueObj.label = oTargetFromServer.label;
      oTargetValueObj.isEditable = false;
      oTargetValueObj.isLoading = false;
      if (oTargetMasterObj.isCreated) {
        oTargetMasterObj.id = oTargetFromServer.id;
        oTargetValueObj.id = oTargetFromServer.id;
        oTargetValueList[oTargetFromServer.id] = oTargetValueObj;
        delete oTargetValueList[iClassId];
      }
      delete oTargetMasterObj.isCreated;
      delete oTargetMasterObj.clonedObject;
      delete oTargetMasterObj.isDirty;
      CS.assign(oTargetMasterObj, oTargetFromServer);
      _switchActiveClass(oTargetValueObj, 'target');
      ClassUtils.setClassScreenLockedStatus(false);
      SettingUtils.setInheritanceAndCutOffUIProperties(oTargetMasterObj.sections);
      CS.forEach(oTargetMasterObj.children, function (oActiveChild) {
        var oActiveChildValue = oTargetValueList[oActiveChild.id];
        oActiveChildValue.isExpanded = false;
        oActiveChildValue.isChildLoaded = false;
      });
      ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
      _setClassContextId(oTargetFromServer);

      let oHierarchyTree = SettingUtils.getAppData().getClassListByTypeGeneric("target");
      let oParentNode = SettingUtils.getParentNodeByChildId(oHierarchyTree, oTargetValueObj.id);
      let aChildrenKeyValueToReset = [
        {key:"isExpanded", value:false}
      ];
      SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oTargetValueList, true);
      oTargetValueObj.isExpanded = true;
      if (oCallback.isCreated) {
        _alertifySuccess(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_CREATED_SUCCESSFULLY, {entity: _getTranslationLabelByType(oTargetFromServer.type)}), bSilentMode);
      } else {
        _alertifySuccess(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY, {entity: _getTranslationLabelByType(oTargetFromServer.type)}), bSilentMode);
      }
    _storeAllSectionRoleList(oTargetMasterObj);
    ClassProps.setSelectedSectionId('');
    _fetchAllClassesFlatList({});
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var failureSaveTargetClassCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        var aTargetList = SettingUtils.getAppData().getTargetList();
        var oActiveClass = ClassUtils.getActiveClass();
        SettingUtils.getNodeFromTreeListById(aTargetList, oActiveClass.id);
        SettingUtils.removeNodeById(aTargetList, oActiveClass.id);
        _setActiveClass({});
        ClassUtils.setClassScreenLockedStatus(false);
        alertify.error(getTranslation().TARGET_NOT_FOUND, 0);
        _triggerChange();
      } else if(!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "ParentChildRelationshipException"}))){
        SettingUtils.failureCallback(oResponse, "failureSaveTargetClassCallback", getTranslation());
      }else{
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveTargetClassCallback", getTranslation());
    }
  };

  var successSaveTextAssetClassCallback = function (oCallback, iClassId, bSilentMode, oServerResponse) {
    var oTextAssetFromServer = oServerResponse.success.entity;
    let oConfigDetails = oServerResponse.success.configDetails;
    _updateReferencedItems(oServerResponse.success);
    var aTextAssetList = SettingUtils.getAppData().getTextAssetList();
    var oTextAssetValueList = _getTextAssetValueList();
    var oTextAssetMasterObj = _getClassFromMasterListById(iClassId, aTextAssetList);
    var oTextAssetValueObj = oTextAssetValueList[iClassId];
    oTextAssetValueObj.label = oTextAssetFromServer.label;
    oTextAssetValueObj.isEditable = false;
    oTextAssetValueObj.isLoading = false;
    if (oTextAssetMasterObj.isCreated) {
      oTextAssetMasterObj.id = oTextAssetFromServer.id;
      oTextAssetValueObj.id = oTextAssetFromServer.id;
      oTextAssetValueList[oTextAssetFromServer.id] = oTextAssetValueObj;
      delete oTextAssetValueList[iClassId];
    }
    delete oTextAssetMasterObj.isCreated;
    delete oTextAssetMasterObj.clonedObject;
    delete oTextAssetMasterObj.isDirty;
    CS.assign(oTextAssetMasterObj, oTextAssetFromServer);
    _switchActiveClass(oTextAssetValueObj, 'textasset');
    ClassUtils.setClassScreenLockedStatus(false);
    SettingUtils.setInheritanceAndCutOffUIProperties(oTextAssetMasterObj.sections);
    CS.forEach(oTextAssetMasterObj.children, function (oActiveChild) {
      var oActiveChildValue = oTextAssetValueList[oActiveChild.id];
      oActiveChildValue.isExpanded = false;
      oActiveChildValue.isChildLoaded = false;
    });
    ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
    _setClassContextId(oTextAssetFromServer);
    let oHierarchyTree = SettingUtils.getAppData().getClassListByTypeGeneric("textasset");
    let oParentNode = SettingUtils.getParentNodeByChildId(oHierarchyTree, oTextAssetValueObj.id);
    let aChildrenKeyValueToReset = [
      {key: "isExpanded", value: false}
    ];
    SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oTextAssetValueList, true);
    oTextAssetValueObj.isExpanded = true;
    if (oCallback.isCreated) {
      alertify.success(getTranslation().TEXTASSET_CREATED_SUCCESSFULLY);
    } else {
      _alertifySuccess(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TEXT_ASSET}), bSilentMode);
    }
    _storeAllSectionRoleList(oTextAssetMasterObj);
    ClassProps.setSelectedSectionId('');
    _fetchAllClassesFlatList({});
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var successSaveSupplierClassCallback = function (oCallback, iClassId, bSilentMode, oServerResponse) {
    let oResponse = oServerResponse.success;
    var oSupplierFromServer = oResponse.entity;
    let oConfigDetails = oResponse.configDetails;
    _updateReferencedItems(oResponse);
    var aSupplierList = SettingUtils.getAppData().getSupplierList();
    var oSupplierValueList = _getSupplierValueList();
    var oSupplierMasterObj = _getClassFromMasterListById(iClassId, aSupplierList);
    var oSupplierValueObj = oSupplierValueList[iClassId];
    oSupplierValueObj.label = oSupplierFromServer.label;
    oSupplierValueObj.isEditable = false;
    oSupplierValueObj.isLoading = false;
    if (oSupplierMasterObj.isCreated) {
      oSupplierMasterObj.id = oSupplierFromServer.id;
      oSupplierValueObj.id = oSupplierFromServer.id;
      oSupplierValueList[oSupplierFromServer.id] = oSupplierValueObj;
      delete oSupplierValueList[iClassId];
    }
    delete oSupplierMasterObj.isCreated;
    delete oSupplierMasterObj.clonedObject;
    delete oSupplierMasterObj.isDirty;
    CS.assign(oSupplierMasterObj, oSupplierFromServer);
    _switchActiveClass(oSupplierValueObj, 'supplier');
    ClassUtils.setClassScreenLockedStatus(false);
    SettingUtils.setInheritanceAndCutOffUIProperties(oSupplierMasterObj.sections);
    CS.forEach(oSupplierMasterObj.children, function (oActiveChild) {
      var oActiveChildValue = oSupplierValueList[oActiveChild.id];
      oActiveChildValue.isExpanded = false;
      oActiveChildValue.isChildLoaded = false;
    });
    ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
    _setClassContextId(oSupplierFromServer);
    let oHierarchyTree = SettingUtils.getAppData().getClassListByTypeGeneric("supplier");
    let oParentNode = SettingUtils.getParentNodeByChildId(oHierarchyTree, oSupplierValueObj.id);
    let aChildrenKeyValueToReset = [
      {key: "isExpanded", value: false}
    ];
    SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oSupplierValueList, true);
    oSupplierValueObj.isExpanded = true;
    if (oCallback.isCreated) {
      alertify.success(getTranslation().SUPPLIER_CREATED_SUCCESSFULLY);
    } else {
      _alertifySuccess(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().SUPPLIER}), bSilentMode);
    }
    _storeAllSectionRoleList(oSupplierMasterObj);
    ClassProps.setSelectedSectionId('');
    _fetchAllClassesFlatList({});
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  //------------------------------Common Save Callbacks--------------------------------------
  //--------------Add Translations Respective to the klass type here ------------------------
  let _getTranslationLabelByType = function (sType) {
    let sTranslationLabel = "";
    switch (sType) {
      case MockDataForEntityBaseTypesDictionary.marketKlassBaseType:
        sTranslationLabel = "MARKET";
        break;
      default:
        sTranslationLabel = "S_CLASS";
    }
    return getTranslation()[sTranslationLabel];
  };
  //---------------------------------------------------------------------------------------

  var failureSaveTextAssetClassCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        var aTextAssetList = SettingUtils.getAppData().getTextAssetList();
        var oActiveClass = ClassUtils.getActiveClass();
        SettingUtils.getNodeFromTreeListById(aTextAssetList, oActiveClass.id);
        SettingUtils.removeNodeById(aTextAssetList, oActiveClass.id);
        _setActiveClass({});
        ClassUtils.setClassScreenLockedStatus(false);
        alertify.error(getTranslation().TEXTASSET_NOT_FOUND, 0);
        _triggerChange();
      } else if(!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "ParentChildRelationshipException"}))){
        SettingUtils.failureCallback(oResponse, "failureSaveTextAssetClassCallback", getTranslation());
      } else{
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveTextAssetClassCallback", getTranslation());
    }
  };

  var failureSaveSupplierClassCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        var aSupplierList = SettingUtils.getAppData().getSupplierList();
        var oActiveClass = ClassUtils.getActiveClass();
        SettingUtils.getNodeFromTreeListById(aSupplierList, oActiveClass.id);
        SettingUtils.removeNodeById(aSupplierList, oActiveClass.id);
        _setActiveClass({});
        ClassUtils.setClassScreenLockedStatus(false);
        alertify.error(getTranslation().SUPPLIER_NOT_FOUND, 0);
        _triggerChange();
      } else if(!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "ParentChildRelationshipException"}))){
        SettingUtils.failureCallback(oResponse, "failureSaveSupplierClassCallback", getTranslation());
      } else{
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveSupplierClassCallback", getTranslation());
    }
  };

  var successRemoveClassCallback = function (oResponse) {
    var aClassList = SettingUtils.getAppData().getClassList();
    var oClassValueList = _getClassValueList();
    var oActiveClass = ClassUtils.getActiveClass();
    var oSelectedClassValue = CS.find(oClassValueList, 'isSelected');
    let sContext = ClassProps.getSelectedClassCategory();
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnTreeNodeDelete(aClassList, oActiveClass.id, oResponse.success[0]);

    CS.forEach(oResponse.success, function (sClassId) {
      var oClass = SettingUtils.getNodeFromTreeListById(aClassList, sClassId);
      if (!CS.isEmpty(oClass)) {
        SettingUtils.removeChildNodesFromValueList(oClass.children, oClassValueList);
        delete oClassValueList[sClassId];
        SettingUtils.removeNodeById(aClassList, sClassId);
        CS.remove(SettingUtils.getAppData().getAllClassesFlatList(), function (oObj) {
          return oObj.id == sClassId
        });
      }

      if (!CS.isEmpty(oSelectedClassValue) && (oSelectedClassValue.id == sClassId)) {
        delete oClassValueList[oSelectedClassValue.id];
      }

      if (oActiveClass && oActiveClass.id === sClassId) {
        _setActiveClass({});
      }
    });
    CS.isNotEmpty(oNewActiveNode) && _fetchClassDetailsAndSwitch(oNewActiveNode, sContext);

    if (CS.isEmpty(oActiveClass) || !(oActiveClass.isDirty || oActiveClass.isCreated)) {
      ClassUtils.setClassScreenLockedStatus(false);
    }
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_REMOVED_SUCCESSFULLY,{entity : getTranslation().S_CLASS}));
    _triggerChange();
  };

  var failureRemoveClassCallback = function (oCallback, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureRemoveClassCallback", getTranslation(), oCallback);
  };

  var successRemoveTaskCallback = function (oResponse) {
    var aTaskList = SettingUtils.getAppData().getTaskList();
    var oTaskValueList = _getTaskValueList();
    var oActiveClass = ClassUtils.getActiveClass();

    CS.forEach(oResponse.success, function (sClassId) {
      var oClass = SettingUtils.getNodeFromTreeListById(aTaskList, sClassId);
      if (!CS.isEmpty(oClass)) {
        SettingUtils.removeChildNodesFromValueList(oClass.children, oTaskValueList);
        delete oTaskValueList[sClassId];
        SettingUtils.removeNodeById(aTaskList, sClassId);
      }
    });

    var oSelectedTaskValue = CS.find(oTaskValueList, 'isSelected');
    if (CS.isEmpty(oSelectedTaskValue)) {
      _setActiveClass({});
    } else {
      if (oSelectedTaskValue.id != SettingUtils.getTreeRootId()) {
        delete oTaskValueList[oSelectedTaskValue.id];
      }
    }

    if (CS.isEmpty(oActiveClass) || !(oActiveClass.isDirty || oActiveClass.isCreated)) {
      ClassUtils.setClassScreenLockedStatus(false);
    }
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_REMOVED_SUCCESSFULLY,{entity : getTranslation().TASK}));
    _triggerChange();
  };

  var failureRemoveTaskCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureRemoveTaskCallback", getTranslation());
  };

  var successRemoveAssetCallback = function (oResponse) {
    var aAssetList = SettingUtils.getAppData().getAssetList();
    var oAssetValueList = _getAssetValueList();
    var oActiveClass = ClassUtils.getActiveClass();
    var oSelectedTaskValue = CS.find(oAssetValueList, 'isSelected');
    let sContext = ClassProps.getSelectedClassCategory();
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnTreeNodeDelete(aAssetList, oActiveClass.id, oResponse.success[0]);
    CS.forEach(oResponse.success, function (sClassId) {
      var oClass = SettingUtils.getNodeFromTreeListById(aAssetList, sClassId);
      if (!CS.isEmpty(oClass)) {
        SettingUtils.removeChildNodesFromValueList(oClass.children, oAssetValueList);
        delete oAssetValueList[sClassId];
        SettingUtils.removeNodeById(aAssetList, sClassId);
        CS.remove(SettingUtils.getAppData().getAllClassesFlatList(), function (oObj) {
         return oObj.id == sClassId
         });
      }
      if (!CS.isEmpty(oSelectedTaskValue) && (oSelectedTaskValue.id == sClassId)) {
        delete oAssetValueList[oSelectedTaskValue.id];
      }
      if (oActiveClass && oActiveClass.id === sClassId) {
        _setActiveClass({});
      }
    });
    CS.isNotEmpty(oNewActiveNode) && _fetchClassDetailsAndSwitch(oNewActiveNode, sContext);
    if (CS.isEmpty(oActiveClass) || !(oActiveClass.isDirty || oActiveClass.isCreated)) {
      ClassUtils.setClassScreenLockedStatus(false);
    }
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_REMOVED_SUCCESSFULLY,{entity : getTranslation().S_CLASS}));
    _triggerChange();
  };

  var failureRemoveAssetCallback = function (oCallback, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureRemoveAssetCallback", getTranslation(), oCallback);
  };

  var successRemoveTargetCallback = function (oResponse) {
    var aTargetList = SettingUtils.getAppData().getTargetList();
    var oTargetValueList = _getTargetValueList();
    var oActiveClass = ClassUtils.getActiveClass();
    var oSelectedTaskValue = CS.find(oTargetValueList, 'isSelected');
    let sContext = ClassProps.getSelectedClassCategory();
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnTreeNodeDelete(aTargetList, oActiveClass.id, oResponse.success[0]);
    CS.forEach(oResponse.success, function (sClassId) {
      var oClass = SettingUtils.getNodeFromTreeListById(aTargetList, sClassId);
      if (!CS.isEmpty(oClass)) {
        SettingUtils.removeChildNodesFromValueList(oClass.children, oTargetValueList);
        delete oTargetValueList[sClassId];
        SettingUtils.removeNodeById(aTargetList, sClassId);
        CS.remove(SettingUtils.getAppData().getAllClassesFlatList(), function (oObj) {
         return oObj.id == sClassId
         });
      }
      if (!CS.isEmpty(oSelectedTaskValue) && (oSelectedTaskValue.id == sClassId)) {
        delete oTargetValueList[oSelectedTaskValue.id];
      }
      if (oActiveClass && oActiveClass.id === sClassId) {
        _setActiveClass({});
      }
    });
    CS.isNotEmpty(oNewActiveNode) && _fetchClassDetailsAndSwitch(oNewActiveNode, sContext);
    if (CS.isEmpty(oActiveClass) || !(oActiveClass.isDirty || oActiveClass.isCreated)) {
      ClassUtils.setClassScreenLockedStatus(false);
    }

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_REMOVED_SUCCESSFULLY, {entity: _getTranslationLabelByType(oActiveClass.type)}));
    _triggerChange();
  };

  var failureRemoveTargetCallback = function (oCallback, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureRemoveTargetCallback" , getTranslation(), oCallback);
  };

  var successRemoveTextAssetCallback = function (oResponse) {
    var aTextAssetList = SettingUtils.getAppData().getTextAssetList();
    var oTextAssetValueList = _getTextAssetValueList();
    var oActiveClass = ClassUtils.getActiveClass();
    var oSelectedTaskValue = CS.find(oTextAssetValueList, 'isSelected');
    let sContext = ClassProps.getSelectedClassCategory();
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnTreeNodeDelete(aTextAssetList, oActiveClass.id, oResponse.success[0]);

    CS.forEach(oResponse.success, function (sClassId) {
      var oClass = SettingUtils.getNodeFromTreeListById(aTextAssetList, sClassId);
      if (!CS.isEmpty(oClass)) {
        SettingUtils.removeChildNodesFromValueList(oClass.children, oTextAssetValueList);
        delete oTextAssetValueList[sClassId];
        SettingUtils.removeNodeById(aTextAssetList, sClassId);
        CS.remove(SettingUtils.getAppData().getAllClassesFlatList(), function (oObj) {
          return oObj.id == sClassId
        });
      }
      if (!CS.isEmpty(oSelectedTaskValue) && (oSelectedTaskValue.id == sClassId)) {
        delete oTextAssetValueList[oSelectedTaskValue.id];
      }
      if (oActiveClass && oActiveClass.id === sClassId) {
        _setActiveClass({});
      }
    });

    CS.isNotEmpty(oNewActiveNode) && _fetchClassDetailsAndSwitch(oNewActiveNode, sContext);

    if (CS.isEmpty(oActiveClass) || !(oActiveClass.isDirty || oActiveClass.isCreated)) {
      ClassUtils.setClassScreenLockedStatus(false);
    }

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_REMOVED_SUCCESSFULLY,{entity : getTranslation().TEXT_ASSET}));
    _triggerChange();
  };

  var failureRemoveTextAssetCallback = function (oCallback, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureRemoveTextAssetCallback" , getTranslation(), oCallback);
  };

  var successRemoveSupplierCallback = function (oResponse) {
    var aSupplierList = SettingUtils.getAppData().getSupplierList();
    var oSupplierValueList = _getSupplierValueList();
    var oActiveClass = ClassUtils.getActiveClass();
    var oSelectedTaskValue = CS.find(oSupplierValueList, 'isSelected');
    CS.forEach(oResponse.success, function (sClassId) {
      var oClass = SettingUtils.getNodeFromTreeListById(aSupplierList, sClassId);
      if (!CS.isEmpty(oClass)) {
        SettingUtils.removeChildNodesFromValueList(oClass.children, oSupplierValueList);
        delete oSupplierValueList[sClassId];
        SettingUtils.removeNodeById(aSupplierList, sClassId);
        CS.remove(SettingUtils.getAppData().getAllClassesFlatList(), function (oObj) {
          return oObj.id == sClassId
        });
      }
      if (!CS.isEmpty(oSelectedTaskValue) && (oSelectedTaskValue.id == sClassId)) {
        delete oSupplierValueList[oSelectedTaskValue.id];
      }
      if (oActiveClass && oActiveClass.id === sClassId) {
        _setActiveClass({});
      }
    });

    if (CS.isEmpty(oActiveClass) || !(oActiveClass.isDirty || oActiveClass.isCreated)) {
      ClassUtils.setClassScreenLockedStatus(false);
    }

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_REMOVED_SUCCESSFULLY,{entity : getTranslation().SUPPLIER}));
    _triggerChange();
  };

  var failureRemoveSupplierCallback = function (oCallback, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureRemoveSupplierCallback" , getTranslation(), oCallback);
  };

  var _createElementMapForAvailability = function (oMasterObject) {
    var oMapToReturn = {};
    CS.forEach(oMasterObject, function (oObj) {
      oMapToReturn[oObj.id] = {
        element: oObj,
        isAvailable: true
      };
    });

    return oMapToReturn;
  };

  var _createClassSectionElementMap = function () {
    var oAppData = SettingUtils.getAppData();
    var oAttributeMap = oAppData.getAttributeList();
    var oSectionMap = oAppData.getPropertyCollectionList();
    var oRoleMap = oAppData.getRoleList();
    var oRelationshipMap = oAppData.getRelationshipMasterList();
    var aTagList = oAppData.getTagList();
    var oMandatoryAttributeList = oAppData.getMandatoryAttributeList();
    var oTaskMandatoryAttributeList = oAppData.getTaskMandatoryAttributeList();
    var oAssetMandatoryAttributeList = oAppData.getAssetMandatoryAttributeList();
    var oTargetMandatoryAttributeList = oAppData.getTargetMandatoryAttributeList();
    var oEditorialMandatoryAttributeList = oAppData.getEditorialMandatoryAttributeList();

    ClassProps.setClassSectionAttributeMap(_createElementMapForAvailability(oAttributeMap));
    ClassProps.setClassSectionMap(_createElementMapForAvailability(oSectionMap));
    ClassProps.setClassSectionTagMap(_createElementMapForAvailability(aTagList[0].children));
    ClassProps.setClassSectionRelationshipMap(_createElementMapForAvailability(oRelationshipMap));
    ClassProps.setClassSectionRoleMap(_createElementMapForAvailability(oRoleMap));
    ClassProps.setMandatoryAttributeProps(CS.cloneDeep(oMandatoryAttributeList));
    ClassProps.setTaskMandatoryAttributeProps(CS.cloneDeep(oTaskMandatoryAttributeList));
    ClassProps.setAssetMandatoryAttributeProps(CS.cloneDeep(oAssetMandatoryAttributeList));
    ClassProps.setTargetMandatoryAttributeProps(CS.cloneDeep(oTargetMandatoryAttributeList));
    ClassProps.setEditorialMandatoryAttributeProps(CS.cloneDeep(oEditorialMandatoryAttributeList));

  };

  let _handleFailureOnAssetUpload = function (oResponse) {
    let oCurrentClass = ClassUtils.getCurrentClass();
    let oCurrentAssetUploadConfigurationModel = oCurrentClass.currentAssetUploadConfigurationModel;
    let oDevException = oResponse.failure.devExceptionDetails;
    let sClassName = oDevException[0].detailMessage;
    let sErrorMessage = ViewUtils.getDecodedTranslation(
        getTranslation().ExtensionAlreadyExistsException,
        {extensionName: oCurrentAssetUploadConfigurationModel.extension, className: sClassName});
    alertify.error(sErrorMessage);
    if (!CS.isEmpty(oCurrentClass.addedExtensionConfiguration)) {
      CS.pull(oCurrentClass.extensionConfiguration, oCurrentClass.currentAssetUploadConfigurationModel);
    }
    _triggerChange();
  };

  var _storeAllSectionRoleList = function (oKlass) {
    var aSections = oKlass.sections;
    ClassProps.emptySectionRoleList();
    ClassProps.emptySectionRoleValuesList();

    var oRoleMap = SettingUtils.getAppData().getRoleList();
    CS.forEach(oRoleMap, function (oRole, sRoleId) {
      ClassProps.setSectionRoleList(oRole);
      ClassProps.setSectionRoleValuesList(_getSectionRoleValueObject(oRole));
    });
  };

  var successFetchClassElaborateCallback = function (oClassValue, oCallback, oResponse) {
    var oClassValueList = _getClassValueList();
    var aClassList = SettingUtils.getAppData().getClassList();
    var oClass = SettingUtils.getNodeFromTreeListById(aClassList, oClassValue.id);
    var oResponseFromServer = oResponse.success;
    var oKlassFromServer = oResponseFromServer.entity;
    let oConfigDetails = oResponseFromServer.configDetails || {};
    _updateReferencedItems(oResponseFromServer);
    var oSelectedValueAndContext = _getSelectedValueAndContext();
    if (oClassValue.id != SettingUtils.getTreeRootId()) {
      _mapServerClassForUI(oClass, oKlassFromServer);
      var oComponentProps = SettingUtils.getComponentProps();
      var oScreenProps = oComponentProps.screen;
      oScreenProps.setReferencedAttributes(oConfigDetails.referencedAttributes);
      oScreenProps.setReferencedTags(oConfigDetails.referencedTags);
      oScreenProps.setReferencedRelationships(oConfigDetails.referencedRelationships);
    }

    ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
    _setClassContextId(oKlassFromServer);
    SettingUtils.setInheritanceAndCutOffUIProperties(oClass.sections);

    oClass.children = oKlassFromServer.children;
    var oTreeItemValue = oClassValueList[oClassValue.id];
    SettingUtils.addNewTreeNodesToValueList(oClassValueList, oClass.children, {'canDelete': true, 'canCreate': true});
    SettingUtils.checkAllChildrenIfParentIsChecked(oClassValueList, oClassValue.isChecked, oClass.children);
    /*SettingUtils.applyValueToAllTreeNodes(oClassValueList, 'isSelected', false);

    SettingUtils.applyValueToAllTreeNodes(oClassValueList, 'isChecked', 0);
    oTreeItemValue.isExpanded = !oTreeItemValue.isExpanded;
    oTreeItemValue.isSelected = true;
    oTreeItemValue.isChecked = 2;*/
    oTreeItemValue.isChildLoaded = true;
    oTreeItemValue.isLoading = false;
    _switchActiveClass(oTreeItemValue, oSelectedValueAndContext.context);
    _processAfterGet(oClass);
    _fetchAllClassesFlatList({});
    //_fetchLinkedKlassesDataBasedOnNatureType(oKlassFromServer.natureType);

    if(oClass.id != SettingUtils.getTreeRootId()) {
      _storeAllSectionRoleList(oClass);
    }

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    ClassProps.setClassContextDialogProps({});

    SettingUtils.getComponentProps().screen.resetEntitiesPaginationData();

    /* Export Side 2 Relationship Collapsed button Disabled on Tab switch */
    let oSide2RelationshipCollapseEnabled = ClassProps.getSide2RelationshipCollapseEnabled();
    oSide2RelationshipCollapseEnabled.relationshipList = true;
    oSide2RelationshipCollapseEnabled.propertiesList = true;

    _triggerChange();
    return true;
  };

  var failureFetchClassElaborateCallback = function (oSelectedClassValue, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        alertify.error("[" + oSelectedClassValue.label +"] "+getTranslation().ERROR_ALREADY_DELETED);
        if(oSelectedClassValue){
          var aClassList = SettingUtils.getAppData().getClassList();
          // var oActiveClass = ClassUtils.getActiveClass();
          SettingUtils.getNodeFromTreeListById(aClassList, oSelectedClassValue.id);
          SettingUtils.removeNodeById(aClassList, oSelectedClassValue.id);
          _setActiveClass({});
          ClassUtils.setClassScreenLockedStatus(false);
          _triggerChange();
        }
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
        if (oSelectedClassValue) {
          oSelectedClassValue.isLoading = false;
          oSelectedClassValue.isSelected = false;
          _triggerChange();
        }
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchClassesElaborateCallback" , getTranslation());
    }
    return false;
  };

  var successFetchTaskClassElaborateCallback = function (oClassValue, oCallback, oResponse) {
    var oTaskValueList = _getTaskValueList();
    var aTaskList = SettingUtils.getAppData().getTaskList();
    var oTask = SettingUtils.getNodeFromTreeListById(aTaskList, oClassValue.id);
    let oResponseFromServer = oResponse.success;
    var oTaskFromServer = oResponseFromServer.entity;
    let oConfigDetails = oResponseFromServer.configDetails || {};
    _updateReferencedItems(oResponseFromServer);
    if (oClassValue.id != SettingUtils.getTreeRootId()) {
      _mapServerTaskClassForUI(oTask, oTaskFromServer);
    }

    oTask.children = oTaskFromServer.children;
    var oTreeItemValue = oTaskValueList[oClassValue.id];
    SettingUtils.addNewTreeNodesToValueList(oTaskValueList, oTask.children, {'canDelete': true, 'canCreate': true});
    SettingUtils.checkAllChildrenIfParentIsChecked(oTaskValueList, oClassValue.isChecked, oTask.children);
    oTreeItemValue.isChildLoaded = true;
    oTreeItemValue.isLoading = false;
    _switchActiveClass(oTreeItemValue, 'task');
    _fetchAllClassesFlatList({});

    if(oTask.id != SettingUtils.getTreeRootId()) {
      _storeAllSectionRoleList(oTask);
      var sSelectedRoleId = _getSelectedRoleId();
    }
    SettingUtils.setInheritanceAndCutOffUIProperties(oTask.sections);

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var failureFetchTaskClassElaborateCallback = function (oSelectedClassValue, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        alertify.error(getTranslation().TASK_NOT_FOUND, 0);
        if(oSelectedClassValue){
          var aTaskList = SettingUtils.getAppData().getTaskList();
          // var oActiveClass = ClassUtils.getActiveClass();
          SettingUtils.getNodeFromTreeListById(aTaskList, oSelectedClassValue.id);
          SettingUtils.removeNodeById(aTaskList, oSelectedClassValue.id);
          _setActiveClass({});
          ClassUtils.setClassScreenLockedStatus(false);
          _triggerChange();
        }
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
        if (oSelectedClassValue) {
          oSelectedClassValue.isLoading = false;
          oSelectedClassValue.isSelected = false;
          _triggerChange();
        }
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchTaskClassElaborateCallback" , getTranslation());
    }
  };

  var successFetchAssetClassElaborateCallback = function (oClassValue, oCallback, oResponse) {
    var oAssetValueList = _getAssetValueList();
    var aAssetList = SettingUtils.getAppData().getAssetList();
    var oAsset = SettingUtils.getNodeFromTreeListById(aAssetList, oClassValue.id);
    let oResponseFromServer = oResponse.success;
    var oAssetFromServer = oResponseFromServer.entity;
    let oConfiDetils = oResponseFromServer.configDetails;
    _updateReferencedItems(oResponseFromServer);

    ClassProps.setReferencedContexts(oConfiDetils.referencedContexts);
    _sortEmbeddedIdsAndTechnicalImageIds(oAssetFromServer, oConfiDetils.referencedKlasses);
    _setClassContextId(oAssetFromServer);

    if (oClassValue.id != SettingUtils.getTreeRootId()) {
      CS.assign(oAsset, oAssetFromServer);
    }

    oAsset.children = oAssetFromServer.children;
    var oTreeItemValue = oAssetValueList[oClassValue.id];
    SettingUtils.addNewTreeNodesToValueList(oAssetValueList, oAsset.children, {'canDelete': true, 'canCreate': true});
    SettingUtils.checkAllChildrenIfParentIsChecked(oAssetValueList, oClassValue.isChecked, oAsset.children);
    oTreeItemValue.isChildLoaded = true;
    oTreeItemValue.isLoading = false;
    _switchActiveClass(oTreeItemValue, 'asset');
    _processAfterGet(oAsset);
    _fetchAllClassesFlatList({});
    //_fetchLinkedKlassesDataBasedOnNatureType(oAssetFromServer.natureType);

    if(oAsset.id != SettingUtils.getTreeRootId()) {
      _storeAllSectionRoleList(oAsset);
    }
    SettingUtils.setInheritanceAndCutOffUIProperties(oAsset.sections);

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var _sortEmbeddedIdsAndTechnicalImageIds = function (oAssetFromServer, oReferencedKlasses){
    let aEmbeddedClassIds = oAssetFromServer.embeddedKlassIds;
    let aTechnicalImageKlassIdsList = [];
    let aEmbeddedKlassIdsList = [];
    CS.forEach(oReferencedKlasses, function (oKlass, sKey) {
      if (CS.includes(aEmbeddedClassIds, sKey)) {
        if (oKlass.natureType === "technicalImage") {
          aTechnicalImageKlassIdsList.push(sKey);
        } else if (oKlass.natureType === "embedded") {
          aEmbeddedKlassIdsList.push(sKey);
        }
      }
    });
    oAssetFromServer.embeddedKlassIds = aEmbeddedKlassIdsList;
    oAssetFromServer.technicalImageKlassIds = aTechnicalImageKlassIdsList;
  };

  var failureFetchAssetClassElaborateCallback = function (oSelectedClassValue, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        alertify.error(getTranslation().ASSET_NOT_FOUND, 0);
        if(oSelectedClassValue){
          var aAssetList = SettingUtils.getAppData().getAssetList();
          // var oActiveClass = ClassUtils.getActiveClass();
          SettingUtils.getNodeFromTreeListById(aAssetList, oSelectedClassValue.id);
          SettingUtils.removeNodeById(aAssetList, oSelectedClassValue.id);
          _setActiveClass({});
          ClassUtils.setClassScreenLockedStatus(false);
          _triggerChange();
        }
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
        if (oSelectedClassValue) {
          oSelectedClassValue.isLoading = false;
          oSelectedClassValue.isSelected = false;
          _triggerChange();
        }
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchAssetClassElaborateCallback" , getTranslation());
    }
    return false;
  };

  var successFetchTargetClassElaborateCallback = function (oClassValue, oCallback, oResponse) {
    var oTargetValueList = _getTargetValueList();
    var aTargetList = SettingUtils.getAppData().getTargetList();
    var oTarget = SettingUtils.getNodeFromTreeListById(aTargetList, oClassValue.id);
    var oResponseData = oResponse.success;
    var oTargetFromServer = oResponseData.entity;
    let oConfigDetails = oResponseData.configDetails;
    _updateReferencedItems(oResponseData);

    if (oClassValue.id != SettingUtils.getTreeRootId()) {
      CS.assign(oTarget, oTargetFromServer);
    }

    oTarget.children = oTargetFromServer.children;
    var oTreeItemValue = oTargetValueList[oClassValue.id];
    SettingUtils.addNewTreeNodesToValueList(oTargetValueList, oTarget.children, {'canDelete': true, 'canCreate': true});
    SettingUtils.checkAllChildrenIfParentIsChecked(oTargetValueList, oClassValue.isChecked, oTarget.children);
    oTreeItemValue.isChildLoaded = true;
    oTreeItemValue.isLoading = false;
    _switchActiveClass(oTreeItemValue, 'target');
    _fetchAllClassesFlatList({});
    _processAfterGet(oTarget);

    if(oTarget.id != SettingUtils.getTreeRootId()) {
      _storeAllSectionRoleList(oTarget);
      var sSelectedRoleId = _getSelectedRoleId();
    }
    ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
    _setClassContextId(oTargetFromServer);
    SettingUtils.setInheritanceAndCutOffUIProperties(oTarget.sections);

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
    return true;
  };

  var successFetchTextAssetClassElaborateCallback = function (oClassValue, oCallback, oResponse) {
    var oTextAssetValueList = _getTextAssetValueList();
    var aTextAssetList = SettingUtils.getAppData().getTextAssetList();
    var oTextAsset = SettingUtils.getNodeFromTreeListById(aTextAssetList, oClassValue.id);
    var oResponseData = oResponse.success;
    var oTextAssetFromServer = oResponseData.entity;
    let oConfigDetails = oResponseData.configDetails;
    _updateReferencedItems(oResponseData);
    if (oClassValue.id != SettingUtils.getTreeRootId()) {
      CS.assign(oTextAsset, oTextAssetFromServer);
    }

    oTextAsset.children = oTextAssetFromServer.children;
    var oTreeItemValue = oTextAssetValueList[oClassValue.id];
    SettingUtils.addNewTreeNodesToValueList(oTextAssetValueList, oTextAsset.children, {'canDelete': true, 'canCreate': true});
    SettingUtils.checkAllChildrenIfParentIsChecked(oTextAssetValueList, oClassValue.isChecked, oTextAsset.children);
    oTreeItemValue.isChildLoaded = true;
    oTreeItemValue.isLoading = false;
    _switchActiveClass(oTreeItemValue, 'textasset');
    _fetchAllClassesFlatList({});
    _processAfterGet(oTextAsset);

    if(oTextAsset.id != SettingUtils.getTreeRootId()) {
      _storeAllSectionRoleList(oTextAsset);
      var sSelectedRoleId = _getSelectedRoleId();
    }
    ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
    _setClassContextId(oTextAssetFromServer);
    SettingUtils.setInheritanceAndCutOffUIProperties(oTextAsset.sections);

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    _triggerChange();

    return true;
  };

  var successFetchSupplierClassElaborateCallback = function (oClassValue, oCallback, oResponse) {
    var oSupplierValueList = _getSupplierValueList();
    var aSupplierList = SettingUtils.getAppData().getSupplierList();
    var oSupplier = SettingUtils.getNodeFromTreeListById(aSupplierList, oClassValue.id);
    var oResponseData = oResponse.success;
    let oConfigDetails = oResponseData.configDetails;
    var oSupplierFromServer = oResponseData.entity;
    _updateReferencedItems(oResponseData);
    if (oClassValue.id != SettingUtils.getTreeRootId()) {
      CS.assign(oSupplier, oSupplierFromServer);
    }

    oSupplier.children = oSupplierFromServer.children;
    var oTreeItemValue = oSupplierValueList[oClassValue.id];
    SettingUtils.addNewTreeNodesToValueList(oSupplierValueList, oSupplier.children);
    SettingUtils.checkAllChildrenIfParentIsChecked(oSupplierValueList, oClassValue.isChecked, oSupplier.children);
    oTreeItemValue.isChildLoaded = true;
    oTreeItemValue.isLoading = false;
    _switchActiveClass(oTreeItemValue, 'supplier');
    _fetchAllClassesFlatList({});
    _processAfterGet(oSupplier);

    if(oSupplier.id != SettingUtils.getTreeRootId()) {
      _storeAllSectionRoleList(oSupplier);
      var sSelectedRoleId = _getSelectedRoleId();
    }
    ClassProps.setReferencedContexts(oConfigDetails.referencedContexts);
    _setClassContextId(oSupplierFromServer);
    SettingUtils.setInheritanceAndCutOffUIProperties(oSupplier.sections);

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    _triggerChange();

    return true;
  };

  var failureFetchTargetClassElaborateCallback = function (oSelectedClassValue, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        alertify.error(getTranslation().TARGET_NOT_FOUND, 0);
        if(oSelectedClassValue){
          var aTargetList = SettingUtils.getAppData().getTargetList();
          // var oActiveClass = ClassUtils.getActiveClass();
          SettingUtils.getNodeFromTreeListById(aTargetList, oSelectedClassValue.id);
          SettingUtils.removeNodeById(aTargetList, oSelectedClassValue.id);
          _setActiveClass({});
          ClassUtils.setClassScreenLockedStatus(false);
          _triggerChange();
        }
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
        if (oSelectedClassValue) {
          oSelectedClassValue.isLoading = false;
          oSelectedClassValue.isSelected = false;
          _triggerChange();
        }
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchTargetClassElaborateCallback" , getTranslation());
    }
    return false;
  };

  var failureFetchSupplierClassElaborateCallback = function (oSelectedClassValue, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        alertify.error(getTranslation().SUPPLIER_NOT_FOUND, 0);
        if(oSelectedClassValue){
          var aSupplierList = SettingUtils.getAppData().getSupplierList();
          SettingUtils.getNodeFromTreeListById(aSupplierList, oSelectedClassValue.id);
          SettingUtils.removeNodeById(aSupplierList, oSelectedClassValue.id);
          _setActiveClass({});
          ClassUtils.setClassScreenLockedStatus(false);
          _triggerChange();
        }
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
        if (oSelectedClassValue) {
          oSelectedClassValue.isLoading = false;
          oSelectedClassValue.isSelected = false;
          _triggerChange();
        }
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchSupplierClassElaborateCallback" , getTranslation());
    }
    return false;
  };

  var failureFetchTextAssetClassElaborateCallback = function (oSelectedClassValue, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "KlassNotFoundException"}))) {
        alertify.error(getTranslation().TEXTASSET_NOT_FOUND, 0);
        if(oSelectedClassValue){
          var aTextAssetList = SettingUtils.getAppData().getTextAssetList();
          SettingUtils.getNodeFromTreeListById(aTextAssetList, oSelectedClassValue.id);
          SettingUtils.removeNodeById(aTextAssetList, oSelectedClassValue.id);
          _setActiveClass({});
          ClassUtils.setClassScreenLockedStatus(false);
          _triggerChange();
        }
      } else {
        alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
        if (oSelectedClassValue) {
          oSelectedClassValue.isLoading = false;
          oSelectedClassValue.isSelected = false;
          _triggerChange();
        }
      }
    } else {
      SettingUtils.failureCallback(oResponse, "failureFetchTextAssetClassElaborateCallback" , getTranslation());
    }
    return false;
  };

  var _resetClassSectionElementMap = function (oMap) {
    CS.forEach(oMap, function (oObj) {
      oObj.isAvailable = true;
    });
  };

  var _fillClassSectionElementMap = function (oClass) {
    var oAttributeMap = ClassProps.getClassSectionAttributeMap();
    var oSectionMap = ClassProps.getClassSectionMap();
    var oTagMap = ClassProps.getClassSectionTagMap();
    var oRelationshipMap = ClassProps.getClassSectionRelationshipMap();
    var oRoleMap = ClassProps.getClassSectionRoleMap();
    var oComponentProps = SettingUtils.getComponentProps();

    _resetClassSectionElementMap(oRoleMap);
    _resetClassSectionElementMap(oRelationshipMap);
    _resetClassSectionElementMap(oTagMap);
    _resetClassSectionElementMap(oAttributeMap);
    _resetClassSectionElementMap(oSectionMap);

    CS.forEach(oClass.sections, function (oSection) {
      // oSection.isInherited = oSection.propertyCollectionId == "articlegeneralInformationPropertyCollection";
      // oSection.isSkipped = false;

      if (oSectionMap[oSection.propertyCollectionId]) {
        oSectionMap[oSection.propertyCollectionId].isAvailable = false;
      }
      CS.forEach(oSection.elements, function (oElement) {
        if(oElement.type == 'attribute') {
          if (oAttributeMap[oElement.attribute.id]) {
            oAttributeMap[oElement.attribute.id].isAvailable = false;
          }
        } else if (oElement.type == 'relationship') {
          // oRelationshipMap[oElement.relationshipInstance.relationship.id].isAvailable = false;
        } else if (oElement.type == 'tag' && oTagMap[oElement.tag.id]) {
          oTagMap[oElement.tag.id].isAvailable = false;
        } else if (oElement.type == 'role' && oRoleMap[oElement.role.id]) {
          oRoleMap[oElement.role.id].isAvailable = false;
        }
      });
    });
  };

  var _mapServerClassForUI = function(oClass, oClassFromServer){
    CS.assign(oClass, oClassFromServer);
  };

  var _updateReferencedItems = function (oResponse) {
    let oConfigDetails = oResponse.configDetails || {};
    let oReferencedObject = {
      referencedTasks: oConfigDetails.referencedTasks,
      referencedDataRules: SettingUtils.convertReferencedInObjectFormat(oConfigDetails.referencedDataRules),
      referencedContexts: oConfigDetails.referencedContexts,
      referencedKlasses: oConfigDetails.referencedKlasses,
      referencedTags: oConfigDetails.referencedTags,
      referencedTabs: oConfigDetails.referencedTabs,
      referencedAttributes: oConfigDetails.referencedAttributes,
      referencedTaxonomies: oConfigDetails.referencedTaxonomies,
      referencedRelationships: oConfigDetails.referencedRelationships,
    };
    ClassProps.setReferencedClassObjects(oReferencedObject);

    _updateActiveClassReferencedClasses(oResponse);
  };

  var _updateActiveClassReferencedClasses = function (oResponseFromServer) {
    //to update the referenced classes for contextual data transfer

    CS.forEach(oResponseFromServer.configDetails.referencedKlasses, function (oReferencedKlass) {
      oReferencedKlass.propagableAttributes = CS.values(oReferencedKlass.propagableAttributes);
      oReferencedKlass.propagableTags = CS.values(oReferencedKlass.propagableTags);
    });

    var oKlassFromServer = oResponseFromServer.entity;
    oKlassFromServer.referencedKlasses = {};
    CS.assign(oKlassFromServer.referencedKlasses, oResponseFromServer.configDetails.referencedKlasses);
  };

  let _fillRelationshipsWithReferencedTabs = function (oActiveEntity, sKey = "relationships") {
    let oReferencedObject = ClassProps.getReferencedClassObjects();

    let oReferencedTabs = oReferencedObject.referencedTabs;
    /**Selected Tab info for relationship is not present in the relationship object, Comes as referencedTabs.
     *  Check and change implementation*  **/

    CS.forEach(oActiveEntity[sKey], function (oRelationship) {
      let sTabId = oRelationship.tabId;
      let oRelationshipTab = oReferencedTabs[sTabId];
      oRelationship.tab = CS.isEmpty(oRelationshipTab) ? {} : oRelationshipTab;
    });
  };

  let _updateReferencedTabs = function (oReferencedTabs) {
    let oReferencedObject = ClassProps.getReferencedClassObjects();
    oReferencedObject.referencedTabs = oReferencedTabs;
    ClassProps.setReferencedClassObjects(oReferencedObject);
  };

  var _mapServerTaskClassForUI = function(oTask, oTaskFromServer){
    CS.assign(oTask, oTaskFromServer);
  };

  //TODO : genereric code for class, task & asset - first line should be taken care by using context.
  // changes of failure if aClassList is not sent or is null/undefined.
  var _getClassFromMasterListById = function (iId, aClassList) {
    aClassList = aClassList || SettingUtils.getAppData().getClassList();
    var oFound = null;
    CS.forEach(aClassList, function (oClass, iIndex) {
      if (oClass.id == iId) {
        oFound = oClass;
        return false;
      } else {

        if (oClass.children && oClass.children.length > 0) {
          oFound = _getClassFromMasterListById(iId, oClass.children);
          if (oFound) {
            return false;
          }
        }
      }
    });
    return oFound;
  };

  let _fetchAssetParentKlassNatureType = function (sParentId) {
    let oPostData = {
      id: sParentId
    };
    CS.postRequest(RequestMapping.getRequestUrl(RequestMapping.getRequestUrl(oClassRequestMapping.GetAssetParentId)), {}, oPostData, successFetchParentKlassNatureType, failureFetchParentNatureType);
  };

  let successFetchParentKlassNatureType = function (oResponse) {
    let oActiveClass = ClassProps.getActiveClass();
    let oResponseData = oResponse.success;
    oActiveClass.secondaryType = oResponseData.id;
    _setActiveClass(oActiveClass);
    _triggerChange();
  };

  let failureFetchParentNatureType = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchParentKlassId", getTranslation());
  };

  let _getAllAssetExtensions = function () {
    let sURL = oUploadRequestMapping.GetAllAssetExtensions;
    return CS.postRequest(sURL, {}, {}, _successFetchAssetExtensions, _failureFetchAssetExtensions);
  };

  let _successFetchAssetExtensions = function (oResponse) {
    let oResponseData = oResponse.success;
    let oExtensions = oResponseData.assetExtensions;
    let oScreenProps = SettingUtils.getComponentProps().screen;
    let oAssetExtensions = {};
    oAssetExtensions.imageAsset = oExtensions.image_asset;
    oAssetExtensions.videoAsset = oExtensions.video_asset;
    oAssetExtensions.documentAsset = oExtensions.document_asset;
    oAssetExtensions.allAssets = oExtensions.allExtensions;
    oScreenProps.setAssetExtensions(oAssetExtensions);
  };

  let _failureFetchAssetExtensions = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureContextMenuListVisibilityToggledCallback', getTranslation());
  };

  var _fetchClassesList = function () {
    var oParameters = {};
    oParameters.id = SettingUtils.getTreeRootId();

    var oRequestData = {};
    oRequestData.getReferencedClasses = false;
    oRequestData.getChildren = true;

    return SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(oClassRequestMapping.GetClassWithoutPC, oParameters),
        oRequestData, successFetchClassListCallback, failureFetchClassListCallback);
  };

  var _fetchTextAssetList = function () {
    var oParameters = {};
    oParameters.id = SettingUtils.getTreeRootId();

    var oRequestData = {};
    oRequestData.getReferencedClasses = false;
    oRequestData.getChildren = true;

    SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(oTextAssetRequestMapping.GetClassWithoutPC, oParameters), oRequestData,
        successFetchTextAssetListCallback, failureFetchTextAssetListCallback);
  };

  var _fetchSupplierList = function () {
    var oParameters = {};
    oParameters.id = SettingUtils.getTreeRootId();

    var oRequestData = {};
    oRequestData.getReferencedClasses = false;
    oRequestData.getChildren = true;

    SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(oSupplierRequestMapping.GetClassWithoutPC, oParameters), oRequestData,
        successFetchSupplierListCallback, failureFetchSupplierListCallback);
  };

  var _fetchTasksList = function () {
    var oParameters = {};
    oParameters.id = SettingUtils.getTreeRootId();

    var oRequestData = {};
    oRequestData.getChildren = true;

    SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(oTaskRequestMapping.GetClassWithoutPC, oParameters), oRequestData,
        successFetchTaskListCallback, failureFetchTaskListCallback);
  };

  var _fetchAssetsList = function () {
    var oParameters = {};
    oParameters.id = SettingUtils.getTreeRootId();

    var oRequestData = {};
    oRequestData.getChildren = true;

    SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(oAssetRequestMapping.GetClassWithoutPC, oParameters), oRequestData, successFetchAssetListCallback, failureFetchAssetListCallback);
  };

  var _fetchTargetList = function () {
    var oParameters = {};
    oParameters.id = SettingUtils.getTreeRootId();

    var oRequestData = {};
    oRequestData.getChildren = true;

    SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(oTargetRequestMapping.GetClassWithoutPC, oParameters), oRequestData, successFetchTargetListCallback, failureFetchTargetListCallback);
  };

  var _fetchClassTree = function () {
    SettingUtils.csGetRequest(oClassRequestMapping.GetClassTree, {}, successFetchTreeCallback, failureFetchTreeCallback);
  };

  var successFetchTreeCallback = function (oResponse) {
    var aTree = oResponse.success;
    var aClassTree = SettingUtils.getAppData().getClassTree();
    aClassTree[0].children = aTree;
    _triggerChange();
  };

  var failureFetchTreeCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTreeCallback", getTranslation());
  };

  var _fetchAllClassesFlatList = function (oCallback) {
    var oGetCallback = {};
    oGetCallback.functionToExecute = oCallback.functionToExecute;
    if(oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    return;

    var fSuccessFetchAllClassesFlatList = '';
    var fFailureFetchAllClassesFlatList = '';
    var oActiveClass = ClassProps.getActiveClass();

    if (!CS.isEmpty(oActiveClass)) {

      var sId = '';
      if(oActiveClass.id == SystemLevelId.ArticleKlassId || oActiveClass.type == MockDataForEntityBaseTypesDictionary.articleKlassBaseType){
        sId = SystemLevelId.ArticleKlassId;
        fSuccessFetchAllClassesFlatList = successFetchAllClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllClasesFlatList.bind(this);
      } else if (oActiveClass.id == SystemLevelId.CollectionKlassId) {
        sId = SystemLevelId.CollectionKlassId;
        fSuccessFetchAllClassesFlatList = successFetchAllCollectionClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllCollectionClasesFlatList.bind(this);
      }else if(oActiveClass.id == SystemLevelId.SetKlassId){
        sId = SystemLevelId.SetKlassId;
        fSuccessFetchAllClassesFlatList = successFetchAllSetClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllSetClasesFlatList.bind(this);
      } else if (oActiveClass.id == SystemLevelId.TaskId || oActiveClass.type == MockDataForEntityBaseTypesDictionary.taskKlassBaseType) {
        sId = SystemLevelId.TaskId;
        fSuccessFetchAllClassesFlatList = successFetchAllTaskClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllClassesFlatList.bind(this);
      } else if (oActiveClass.id == SystemLevelId.AssetId || oActiveClass.type == MockDataForEntityBaseTypesDictionary.assetKlassBaseType) {
        sId = SystemLevelId.AssetId;
        fSuccessFetchAllClassesFlatList = successFetchAllAssetClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllClassesFlatList.bind(this);
      } else if (oActiveClass.id == SystemLevelId.AssetCollectionId) {
        sId = SystemLevelId.AssetCollectionId;
        fSuccessFetchAllClassesFlatList = successFetchAllAssetCollectionClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllClassesFlatList.bind(this);
      } else if (oActiveClass.id == SystemLevelId.MarketTargetId || oActiveClass.type == MockDataForEntityBaseTypesDictionary.marketKlassBaseType) {
        sId = SystemLevelId.MarketTargetId;
        fSuccessFetchAllClassesFlatList = successFetchAllMarketTargetClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllClassesFlatList.bind(this);
      } else if (oActiveClass.id == SystemLevelId.TargetCollectionId /*|| oActiveClass.type == oClassTypeDictionary["9"]*/) {
        sId = SystemLevelId.TargetCollectionId;
        fSuccessFetchAllClassesFlatList = successFetchAllTargetCollectionClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllClassesFlatList.bind(this);
      } else if (oActiveClass.id == SystemLevelId.TextAssetId || oActiveClass.type == MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType) {
        sId = SystemLevelId.TextAssetId;
        fSuccessFetchAllClassesFlatList = successFetchAllTextAssetClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllClasesFlatList.bind(this);
      } else if (oActiveClass.id == SystemLevelId.SupplierId || oActiveClass.type == MockDataForEntityBaseTypesDictionary.supplierKlassBaseType) {
        sId = SystemLevelId.SupplierId;
        fSuccessFetchAllClassesFlatList = successFetchAllSupplierClassesFlatList.bind(this, oGetCallback);
        fFailureFetchAllClassesFlatList = failureFetchAllClasesFlatList.bind(this);
      }

      SettingUtils.csGetRequest(oClassRequestMapping.GetAllKlassessByCategory,{ id : sId }, fSuccessFetchAllClassesFlatList, fFailureFetchAllClassesFlatList);

    }
  };

  var _translateClassForServer = function (oClass) {
    var oClassForServer = null;
    if (oClass.clonedObject) {
      oClassForServer = oClass.clonedObject;
    } else {
      return 0;
    }

    var bProblemInRelationship = false;
    let oADMClass = oClass.clonedObject;
    let aAllRelationships = oADMClass.addedRelationships.concat(oADMClass.modifiedRelationships);
    CS.forEach(aAllRelationships, function (oRelationship) {
      bProblemInRelationship = CS.isEmpty(oRelationship.side2.klassId);
      return !bProblemInRelationship
    });

    if(bProblemInRelationship) {
      return -1;
    }

    delete oClassForServer.attributes;
    delete oClassForServer.relationships;
    delete oClassForServer.children;
    delete oClassForServer.tags;
    delete oClassForServer.isDirty;

    if (!oClass.isCreated) {
      delete oClassForServer.sections;
    }

    return oClassForServer;
  };

  var _getClassConfigRulesADM = function (oClass) {
    var oClonedClass = oClass.clonedObject;
    var aOriginalRules = oClass.dataRules;
    var aClonedRules = CS.cloneDeep(oClonedClass.dataRules);
    var aDeletedDataRules = [];
    CS.forEach(aOriginalRules, function (sOriginalRuleId) {
      var iIndex = CS.indexOf(aClonedRules, sOriginalRuleId);
      if(iIndex < 0){
        aDeletedDataRules.push(sOriginalRuleId);
      }else {
        aClonedRules.splice(iIndex, 1);
      }
    });

    return {
      addedDataRules: aClonedRules,
      deletedDataRules: aDeletedDataRules
    }
  };

  var _getClassConfigTasksADM = function (oClass) {
    var aOriginalTasks = oClass.tasks;
    var oClonedClass = oClass.clonedObject;
    var aClonedTasks = oClonedClass.tasks;
    var aAddedTasks = CS.difference(aClonedTasks, aOriginalTasks);
    var aDeletedTasks = CS.difference(aOriginalTasks, aClonedTasks);

    return {
      addedTasks: aAddedTasks,
      deletedTasks: aDeletedTasks
    }
  };

  var _getClassConfigGtinKlassADM = function (oClass) {
    var sOriginalGtinKlass = oClass.gtinKlassId;
    var sClonedGtinKlass = oClass.clonedObject.gtinKlassId;
    let bIsModified = false;
    let oModifiedKlass = {contextKlassId: sClonedGtinKlass};
    var oAddedGtinKlass = null;
    var sDeletedGtinKlass = null;
    if (sOriginalGtinKlass != sClonedGtinKlass) {
      sClonedGtinKlass = CS.toString(sClonedGtinKlass);
      sOriginalGtinKlass = CS.toString(sOriginalGtinKlass);
      if (!CS.isEmpty(sClonedGtinKlass)) {
        oAddedGtinKlass = {
          contextKlassId: sClonedGtinKlass,
          attributes: oClass.clonedObject.referencedKlasses[sClonedGtinKlass].propagableAttributes,
          tags: oClass.clonedObject.referencedKlasses[sClonedGtinKlass].propagableTags,
        };
      }
      sDeletedGtinKlass =  CS.isEmpty(sOriginalGtinKlass) ? null : sOriginalGtinKlass;
    } else if (sOriginalGtinKlass && sClonedGtinKlass) {
      let oNewGtinClass = oClass.clonedObject.referencedKlasses[sClonedGtinKlass];
      let oReferencedKlasses = ClassProps.getReferencedClassObjects().referencedKlasses;
      let oOldGtinClass = oReferencedKlasses[sOriginalGtinKlass];
      if (!CS.isEqual(oNewGtinClass.propagableAttributes, oOldGtinClass.propagableAttributes)) {
        oModifiedKlass.addedAttributes = CS.differenceBy(oNewGtinClass.propagableAttributes, oOldGtinClass.propagableAttributes, "id");
        oModifiedKlass.deletedAttributes = CS.map(CS.differenceBy(oOldGtinClass.propagableAttributes, oNewGtinClass.propagableAttributes, "id"), "id");
        oModifiedKlass.modifiedAttributes = CS.filter(oNewGtinClass.propagableAttributes, function (oAttribute) {
              var oOldValue = CS.find(oOldGtinClass.propagableAttributes, {id: oAttribute.id});
              return oOldValue && oOldValue.couplingType != oAttribute.couplingType;
            }
        );
        bIsModified = true;
      }
      if (!CS.isEqual(oNewGtinClass.propagableTags, oOldGtinClass.propagableTags)) {
        oModifiedKlass.addedTags = CS.differenceBy(oNewGtinClass.propagableTags, oOldGtinClass.propagableTags, "id");
        oModifiedKlass.deletedTags = CS.map(CS.differenceBy(oOldGtinClass.propagableTags, oNewGtinClass.propagableTags, "id"), "id");
        oModifiedKlass.modifiedTags = CS.filter(oNewGtinClass.propagableTags, function (oTag) {
              var oOldValue = CS.find(oOldGtinClass.propagableTags, {id: oTag.id});
              return oOldValue && oOldValue.couplingType != oTag.couplingType;
            }
        );
        bIsModified = true;
      }
      if (bIsModified) {
        oModifiedKlass; // eslint-disable-line
      }
    }
    return {
      addedGtinKlass: oAddedGtinKlass,
      deletedGtinKlass: sDeletedGtinKlass,
      modifiedGtinKlass: bIsModified ? oModifiedKlass : {}
    }
  };

  let _getClassConfigLanguageKlassADM = (oClass) => {
    var sOriginalLanguageKlass = oClass.languageKlassId;
    var sClonedLanguageKlass = oClass.clonedObject.languageKlassId;
    let bIsModified = false;
    let oModifiedLanguageKlass = {contextKlassId: sClonedLanguageKlass};
    var oAddedLanguageKlass = null;
    var sDeletedLanguageKlass = null;
    if (sOriginalLanguageKlass !== sClonedLanguageKlass) {
      sClonedLanguageKlass = CS.toString(sClonedLanguageKlass);
      sOriginalLanguageKlass = CS.toString(sOriginalLanguageKlass);
      if (!CS.isEmpty(sClonedLanguageKlass)) {
        oAddedLanguageKlass = {
          contextKlassId: sClonedLanguageKlass,
          attributes: oClass.clonedObject.referencedKlasses[sClonedLanguageKlass].propagableAttributes,
          tags: oClass.clonedObject.referencedKlasses[sClonedLanguageKlass].propagableTags,
        };
      }
      sDeletedLanguageKlass =  CS.isEmpty(sOriginalLanguageKlass) ? null : sOriginalLanguageKlass;
    } else if (sOriginalLanguageKlass && sClonedLanguageKlass) {
      let oNewLanguageClass = oClass.clonedObject.referencedKlasses[sClonedLanguageKlass];
      let oReferencedKlasses = ClassProps.getReferencedClassObjects().referencedKlasses;
      let oOldLanguageClass = oReferencedKlasses[sOriginalLanguageKlass];
      if (!CS.isEqual(oNewLanguageClass.propagableAttributes, oOldLanguageClass.propagableAttributes)) {
        oModifiedLanguageKlass.addedAttributes = CS.differenceBy(oNewLanguageClass.propagableAttributes, oOldLanguageClass.propagableAttributes, "id");
        oModifiedLanguageKlass.deletedAttributes = CS.map(CS.differenceBy(oOldLanguageClass.propagableAttributes, oNewLanguageClass.propagableAttributes, "id"), "id");
        oModifiedLanguageKlass.modifiedAttributes = CS.filter(oNewLanguageClass.propagableAttributes, function (oAttribute) {
              var oOldValue = CS.find(oOldLanguageClass.propagableAttributes, {id: oAttribute.id});
              return oOldValue && oOldValue.couplingType !== oAttribute.couplingType;
            }
        );
        bIsModified = true;
      }
      if (!CS.isEqual(oNewLanguageClass.propagableTags, oOldLanguageClass.propagableTags)) {
        oModifiedLanguageKlass.addedTags = CS.differenceBy(oNewLanguageClass.propagableTags, oOldLanguageClass.propagableTags, "id");
        oModifiedLanguageKlass.deletedTags = CS.map(CS.differenceBy(oOldLanguageClass.propagableTags, oNewLanguageClass.propagableTags, "id"), "id");
        oModifiedLanguageKlass.modifiedTags = CS.filter(oNewLanguageClass.propagableTags, function (oTag) {
              var oOldValue = CS.find(oOldLanguageClass.propagableTags, {id: oTag.id});
              return oOldValue && oOldValue.couplingType !== oTag.couplingType;
            }
        );
        bIsModified = true;
      }
      if (bIsModified) {
        oModifiedLanguageKlass; // eslint-disable-line
      }
    }
    return {
      addedLanguageKlass: oAddedLanguageKlass,
      deletedLanguageKlass: sDeletedLanguageKlass,
      modifiedLanguageKlass: bIsModified ? oModifiedLanguageKlass : {}
    }
  };

  var _getClassConfigEmbeddedKlassesADM = function (oClass) {
    var aOldEmbeddedKlasses = oClass.embeddedKlassIds;
    var aNewEmbeddedKlasses = oClass.clonedObject.embeddedKlassIds;

    if (oClass.type === MockDataForEntityBaseTypesDictionary.assetKlassBaseType) {
      let aTechnicalImageKlassIds = oClass.clonedObject.technicalImageKlassIds || [];
      aNewEmbeddedKlasses = aTechnicalImageKlassIds ? aNewEmbeddedKlasses.concat(aTechnicalImageKlassIds) : aNewEmbeddedKlasses;
      aOldEmbeddedKlasses = oClass.technicalImageKlassIds ? aOldEmbeddedKlasses.concat(oClass.technicalImageKlassIds) : aOldEmbeddedKlasses;
      delete oClass.technicalImageKlassIds;
      delete oClass.clonedObject.technicalImageKlassIds;
    }

    let aAddedEmbeddedKlasses = CS.difference(aNewEmbeddedKlasses, aOldEmbeddedKlasses);
    let aAddedKlasses = [];
    CS.forEach(aAddedEmbeddedKlasses, function (sId) {
      let aAttributes = [];
      let aTags = [];
      if(oClass.clonedObject.referencedKlasses[sId]){
        aAttributes = oClass.clonedObject.referencedKlasses[sId].propagableAttributes;
        aTags = oClass.clonedObject.referencedKlasses[sId].propagableTags;
      }
      let oAddedKlass = {
        contextKlassId: sId,
        attributes: aAttributes,
        tags: aTags,
      };
      aAddedKlasses.push(oAddedKlass);
    });

    let aModifiedEmbeddedKlasses = CS.intersection(aOldEmbeddedKlasses, aNewEmbeddedKlasses);
    let aModifiedKlasses = [];
    CS.forEach(aModifiedEmbeddedKlasses, function (sId) {
      let bIsModified = false;
      let oModifiedKlass = {
        contextKlassId: sId,
        addedAttributes: [],
        deletedAttributes: [],
        modifiedAttributes: [],
        addedTags: [],
        deletedTags: [],
        modifiedTags: []
      };
      let oNewEmbeddedKlass = oClass.clonedObject.referencedKlasses[sId];
      let oOldEmbeddedKlass = oClass.referencedKlasses[sId];
      if (!CS.isEqual(oNewEmbeddedKlass.propagableAttributes, oOldEmbeddedKlass.propagableAttributes)) {
        oModifiedKlass.addedAttributes = CS.differenceBy(oNewEmbeddedKlass.propagableAttributes, oOldEmbeddedKlass.propagableAttributes, "id");
        oModifiedKlass.deletedAttributes = CS.map(CS.differenceBy(oOldEmbeddedKlass.propagableAttributes, oNewEmbeddedKlass.propagableAttributes, "id"), "id");
        oModifiedKlass.modifiedAttributes = CS.filter(oNewEmbeddedKlass.propagableAttributes, function (oAttribute) {
              var oOldValue = CS.find(oOldEmbeddedKlass.propagableAttributes, {id: oAttribute.id});
              return oOldValue && oOldValue.couplingType != oAttribute.couplingType;
            }
        );
        bIsModified = true;
      }
      if (!CS.isEqual(oNewEmbeddedKlass.propagableTags, oOldEmbeddedKlass.propagableTags)) {
        oModifiedKlass.addedTags = CS.differenceBy(oNewEmbeddedKlass.propagableTags, oOldEmbeddedKlass.propagableTags, "id");
        oModifiedKlass.deletedTags = CS.map(CS.differenceBy(oOldEmbeddedKlass.propagableTags, oNewEmbeddedKlass.propagableTags, "id"), "id");
        oModifiedKlass.modifiedTags = CS.filter(oNewEmbeddedKlass.propagableTags, function (oTag) {
              var oOldValue = CS.find(oOldEmbeddedKlass.propagableTags, {id: oTag.id});
              return oOldValue && oOldValue.couplingType != oTag.couplingType;
            }
        );
        bIsModified = true;
      }
      if (bIsModified) {
        aModifiedKlasses.push(oModifiedKlass);
      }
    });

    delete oClass.clonedObject.referencedKlasses;
    return {
      addedContextKlasses: aAddedKlasses,
      modifiedContextKlasses: aModifiedKlasses,
      deletedContextKlasses: CS.map(CS.difference(aOldEmbeddedKlasses, aNewEmbeddedKlasses))
    }
  };

  var _getClassConfigLifecycleStatusADM = function (oClass) {
    var aOriginalEvents = oClass.lifeCycleStatusTags;
    var oClonedClass = oClass.clonedObject;
    var aClonedEvents = oClonedClass.lifeCycleStatusTags;
    var aAddedLifeCycleStatusTags = CS.difference(aClonedEvents, aOriginalEvents);
    var aDeletedLifeCycleStatusTags = CS.difference(aOriginalEvents, aClonedEvents);

    return {
      addedLifecycleStatusTags: aAddedLifeCycleStatusTags,
      deletedLifecycleStatusTags: aDeletedLifeCycleStatusTags
    }
  };

  var _getClassConfigContextsADM = function (oClass) {
    var oClonedClass = oClass.clonedObject;
    var oOriginalContexts = oClass.contexts || {};
    var oClonedContexts = oClonedClass.contexts || {};

    var oAddedContexts = {};
    oAddedContexts.embeddedVariantContexts = CS.difference(oClonedContexts.embeddedVariantContexts, oOriginalContexts.embeddedVariantContexts);
    oAddedContexts.productVariantContexts = CS.difference(oClonedContexts.productVariantContexts, oOriginalContexts.productVariantContexts);
    oAddedContexts.promotionalVersionContexts = CS.difference(oClonedContexts.promotionalVersionContexts, oOriginalContexts.promotionalVersionContexts);
    oAddedContexts.languageVariantContexts = CS.difference(oClonedContexts.languageVariantContexts, oOriginalContexts.languageVariantContexts);


    var oDeletedContexts = {};
    oDeletedContexts.embeddedVariantContexts = CS.difference(oOriginalContexts.embeddedVariantContexts, oClonedContexts.embeddedVariantContexts);
    oDeletedContexts.productVariantContexts = CS.difference(oOriginalContexts.productVariantContexts, oClonedContexts.productVariantContexts);
    oDeletedContexts.promotionalVersionContexts = CS.difference(oOriginalContexts.promotionalVersionContexts, oClonedContexts.promotionalVersionContexts);
    oDeletedContexts.languageVariantContexts = CS.difference(oOriginalContexts.languageVariantContexts, oClonedContexts.languageVariantContexts);

    return {
      addedContexts: oAddedContexts,
      deletedContexts: oDeletedContexts
    }
  };

  var _generateADMForKlassRelationships = function (aNewRelationships, aOldRelationships, oClassToReturn) {
    CS.forEach(aNewRelationships, function (oRelationship) {
      var oOldRelationship = CS.find(aOldRelationships, {id: oRelationship.id});
      if (oOldRelationship) {
        if (!CS.isEqual(oOldRelationship, oRelationship)) {
          var oModifiedRelationship = {};
          CS.assign(oModifiedRelationship, oRelationship);
          if (!CS.isEqual(oOldRelationship.propertyCollection, oRelationship.propertyCollection)) {
            oModifiedRelationship.deletedPropertyCollection = oOldRelationship.propertyCollection ? oOldRelationship.propertyCollection.id : null;
            oModifiedRelationship.addedPropertyCollection = oRelationship.propertyCollection ? oRelationship.propertyCollection : null;
          }
          delete oModifiedRelationship.propertyCollection;
          if (!CS.isEqual(oOldRelationship.contextTags, oRelationship.contextTags)) {
            oModifiedRelationship.deletedContextTags = CS.difference(oOldRelationship.contextTags, oRelationship.contextTags);
            oModifiedRelationship.addedContextTags = CS.difference(oRelationship.contextTags, oOldRelationship.contextTags);
          }

          _generateNatureRelationshipPropertyADM(oModifiedRelationship, oOldRelationship);

          delete oModifiedRelationship.contextTags;
          oClassToReturn.modifiedRelationships.push(oModifiedRelationship);
        }
      } else {
        var oAddedRelationship = {};
        CS.assign(oAddedRelationship, oRelationship);
        if (oRelationship.propertyCollection) {
          oAddedRelationship.addedPropertyCollection = oRelationship.propertyCollection;
        }
        delete oAddedRelationship.propertyCollection;
        oClassToReturn.addedRelationships.push(oAddedRelationship);
      }
    });
  };

  let _fillCurrentAssetUploadConfig = function (aExtensionConfiguration, oCurrentAssetUploadConfigurationModel) {
    if (oCurrentAssetUploadConfigurationModel.isCreated) {
      aExtensionConfiguration.push(oCurrentAssetUploadConfigurationModel);
    } else {
      let iIndex = CS.findIndex(aExtensionConfiguration, {id: oCurrentAssetUploadConfigurationModel.id});
      aExtensionConfiguration[iIndex] = oCurrentAssetUploadConfigurationModel;
    }
  };

  let _generateADMForAssetUploadConfiguration = function(oCallBackData, oClass) {

    if(!oClass.clonedObject && oClass.type !== MockDataForEntityBaseTypesDictionary.assetKlassBaseType){
      return oClass;
    }
    let oClonedObject = oClass.clonedObject;
    let aClonedExtensionConfiguration = oClonedObject.extensionConfiguration;
    let aExtensionConfiguration = oClass.extensionConfiguration;
    if (CS.isNotEmpty(oClonedObject.currentAssetUploadConfigurationModel) && CS.isNotEmpty(oClonedObject.currentAssetUploadConfigurationModel.extension)) {
      _fillCurrentAssetUploadConfig(aClonedExtensionConfiguration, oClonedObject.currentAssetUploadConfigurationModel);
    }
    let aAddedExtensionConfiguration = [];
    let aDeletedExtensionConfiguration = [];
    let aModifiedExtensionConfiguration = [];
    aAddedExtensionConfiguration =  CS.differenceBy(aClonedExtensionConfiguration, aExtensionConfiguration, 'id');
    aDeletedExtensionConfiguration = CS.differenceBy(aExtensionConfiguration, aClonedExtensionConfiguration, 'id');

    let aDeletedIds = CS.map(aDeletedExtensionConfiguration, "id");

    CS.forEach(CS.intersectionBy(aClonedExtensionConfiguration, aExtensionConfiguration, 'id'), function (oClonedObject) {
      let oOldObject = CS.find(aExtensionConfiguration, {'id': oClonedObject.id});
      !CS.isEqual(oOldObject, oClonedObject) && aModifiedExtensionConfiguration.push(oClonedObject)
    });


    oClonedObject.addedExtensionConfiguration = aAddedExtensionConfiguration;
    oClonedObject.deletedExtensionConfiguration = aDeletedIds;
    oClonedObject.modifiedExtensionConfiguration = aModifiedExtensionConfiguration;
    if (CS.isNotEmpty(aDeletedIds)) {
      oCallBackData.deletedExtensionConfiguration = aDeletedIds[0];
    }
    return {
      classToSave: oClass,
      callBackData: oCallBackData,
    };
  };

  var _generateADMForSectionsInClass = function(oClass){

    var sSplitter = ClassUtils.getSplitter();
    var oClassClone = CS.cloneDeep(oClass);
    if(!oClassClone.clonedObject){
      return oClassClone;
    }

    var aOldAllowedClass = CS.cloneDeep(oClassClone.allowedTypes);
    var aNewAllowedClass = CS.cloneDeep(oClassClone.clonedObject.allowedTypes);
    var aAddedAllowedTypes = [];
    var aDeletedAllowedTypes = [];
    CS.forEach(aOldAllowedClass, function (sOldId) {
      var iIndex = CS.indexOf(aNewAllowedClass, sOldId);
      if (iIndex < 0) {
        aDeletedAllowedTypes.push(sOldId);
      } else {
        aNewAllowedClass.splice(iIndex, 1);
      }
    });
    aAddedAllowedTypes = aNewAllowedClass;


    var aOldSections = oClassClone.sections;
    var aNewSections = oClassClone.clonedObject.sections;
    var oClassToReturn = oClassClone.clonedObject;
    oClassToReturn.modifiedElements = [];
    var oSectionADMObject = {
      added: [],
      deleted: [],
      modified: []
    };

    CS.forEach(aNewSections, function(oNewSection){
      var oOldSection = CS.remove(aOldSections, function(oSection){
        return oSection.id == oNewSection.id
      });
      oOldSection = oOldSection[0];

      //if section found in old version
      if(oOldSection){
        var oElementADMObject = {
          added: [],
          deleted: [],
          modified: []
        };
        var aOldSectionElements = oOldSection.elements;
        var aNewSectionElements = oNewSection.elements;

        var bIsSectionModified = !CS.isEqual(oNewSection.sequence, oOldSection.sequence) || !CS.isEqual(oNewSection.isSkipped, oOldSection.isSkipped);
        oNewSection.isModified = bIsSectionModified;

        //iterating on new section elements
        CS.forEach(aNewSectionElements, function(oNewSectionElement){
          var oOldSectionElement = CS.remove(aOldSectionElements, {id: oNewSectionElement.id});

          //if element found in old version
          if(oOldSectionElement.length > 0){

            //if element is not modified
            var bIsElementModified = !CS.isEqual(oOldSectionElement[0], oNewSectionElement);
            if (bIsElementModified) {
              if (oNewSectionElement.type == "tag") {
                oNewSectionElement.addedDefaultValues = CS.differenceBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId");
                oNewSectionElement.deletedDefaultValues = CS.map(CS.differenceBy(oOldSectionElement[0].defaultValue, oNewSectionElement.defaultValue, "tagId"), "tagId");
                oNewSectionElement.modifiedDefaultValues = CS.filter(
                    CS.intersectionBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId"),
                    function (oDefaultValue) {
                      var oOldDefaultValue = CS.find(oOldSectionElement[0].defaultValue, {tagId: oDefaultValue.tagId});
                      return oOldDefaultValue && oOldDefaultValue.relevance != oDefaultValue.relevance;
                    }
                );
                delete oNewSectionElement.defaultValue;

                oNewSectionElement.addedSelectedTagValues = [];
                oNewSectionElement.deletedSelectedTagValues = [];
                let aAddedSelectedTagValues = CS.differenceBy(oNewSectionElement.selectedTagValues, oOldSectionElement[0].selectedTagValues, "tagId");
                CS.forEach(aAddedSelectedTagValues, function (oValue) {
                  oNewSectionElement.addedSelectedTagValues.push(oValue.tagId);
                });
                oNewSectionElement.deletedSelectedTagValues = CS.map(CS.differenceBy(oOldSectionElement[0].selectedTagValues, oNewSectionElement.selectedTagValues, "tagId"), "tagId");


                delete oNewSectionElement.selectedTagValues;
              }
              else if (oNewSectionElement.type == "attribute") {
                oNewSectionElement.defaultValue = CS.trim(oNewSectionElement.defaultValue);
              }

              oElementADMObject.modified.push(oNewSectionElement);
              oNewSectionElement.isModified = bIsElementModified;
              // bIsSectionModified = true;
            }
          } else {
            //not found in old so add to added list
            oNewSectionElement.id = oNewSectionElement.id.split(sSplitter)[0];
            oElementADMObject.added.push(oNewSectionElement);
            // bIsSectionModified = true;
          }
        });

        var aDeletedElements = CS.map(aOldSectionElements, 'id');
        // bIsSectionModified = bIsSectionModified || aDeletedElements.length > 0;
        CS.merge(oElementADMObject.deleted, aDeletedElements);

        oNewSection.addedElements = oElementADMObject.added;
        oNewSection.deletedElements = oElementADMObject.deleted;
        oNewSection.modifiedElements = [];
        oClassToReturn.modifiedElements = oClassToReturn.modifiedElements.concat(oElementADMObject.modified);
        delete oNewSection.elements;

        if (bIsSectionModified) {
          oSectionADMObject.modified.push(oNewSection);
        }

      } else {
        // oNewSection.propertyCollectionId = oNewSection.id.split(sSplitter)[0];
        // oNewSection.id = null;
        oSectionADMObject.added.push(oNewSection);
      }
    });

    var aDeletedSections = CS.map(aOldSections, 'id');
    CS.merge(oSectionADMObject.deleted, aDeletedSections);

    oClassToReturn.addedSections = oSectionADMObject.added;
    oClassToReturn.deletedSections = oSectionADMObject.deleted;
    oClassToReturn.modifiedSections = [];
    CS.forEach(oSectionADMObject.modified, function (oModifiedSection) {
      oClassToReturn.modifiedSections.push({
        id: oModifiedSection.id,
        sequence: oModifiedSection.sequence,
        isSkipped: oModifiedSection.isSkipped,
        isModified: oModifiedSection.isModified
      });
      oClassToReturn.modifiedElements = oClassToReturn.modifiedElements.concat(oModifiedSection.modifiedElements);
    });

    oClassToReturn.deletedAllowedTypes = aDeletedAllowedTypes;
    oClassToReturn.addedAllowedTypes = aAddedAllowedTypes;

    var oRuleADM = _getClassConfigRulesADM(oClassClone);
    oClassToReturn.addedDataRules = oRuleADM.addedDataRules;
    oClassToReturn.deletedDataRules = oRuleADM.deletedDataRules;
    delete oClassToReturn.dataRules;

    var oTasksADM = _getClassConfigTasksADM(oClassClone);
    oClassToReturn.addedTasks = oTasksADM.addedTasks;
    oClassToReturn.deletedTasks = oTasksADM.deletedTasks;
    delete oClassToReturn.tasks;

    var oLifecycleStatusADM = _getClassConfigLifecycleStatusADM(oClassClone);
    oClassToReturn.addedLifecycleStatusTags = oLifecycleStatusADM.addedLifecycleStatusTags;
    oClassToReturn.deletedLifecycleStatusTags = oLifecycleStatusADM.deletedLifecycleStatusTags;
    delete oClassToReturn.lifeCycleStatusTags;

    var oContextADM = _getClassConfigContextsADM(oClassClone);
    oClassToReturn.addedContexts = oContextADM.addedContexts;
    oClassToReturn.deletedContexts = oContextADM.deletedContexts;
    let oClassToReturnContexts = oClassToReturn.contexts;
    delete oClassToReturn.contexts;

    var oGtinKlassADM = _getClassConfigGtinKlassADM(oClassClone);
    oClassToReturn.addedGtinKlass = oGtinKlassADM.addedGtinKlass;
    oClassToReturn.deletedGtinKlass = oGtinKlassADM.deletedGtinKlass;
    oClassToReturn.modifiedGtinKlass = oGtinKlassADM.modifiedGtinKlass;
    delete oClassToReturn.gtinKlassId;

    var oLanguageKlassADM = _getClassConfigLanguageKlassADM(oClassClone);
    oClassToReturn.addedLanguageKlass = oLanguageKlassADM.addedLanguageKlass;
    oClassToReturn.deletedLanguageKlass = oLanguageKlassADM.deletedLanguageKlass;
    oClassToReturn.modifiedLanguageKlass = oLanguageKlassADM.modifiedLanguageKlass;
    delete oClassToReturn.languageKlassId;

    var oEmbeddedKlassesADM = _getClassConfigEmbeddedKlassesADM(oClassClone);
    oClassToReturn.addedContextKlasses = oEmbeddedKlassesADM.addedContextKlasses;
    oClassToReturn.deletedContextKlasses = oEmbeddedKlassesADM.deletedContextKlasses;
    oClassToReturn.modifiedContextKlasses = oEmbeddedKlassesADM.modifiedContextKlasses;

    var aOldRelationships = oClassClone.relationships;
    var aNewRelationships = oClassClone.clonedObject.relationships;

    if (oClassToReturn.natureType == oClassClone.natureType) {
      oClassToReturn.deletedRelationships = CS.map(CS.differenceBy(aOldRelationships, aNewRelationships, "id"), "id");

      let aOldDeletedRelationships = CS.filter(aOldRelationships, function (oRelationship) {
        return CS.includes(oClassToReturn.deletedRelationships, oRelationship.id);
      });

      let bIsLinkedVariantRelationshipDeleted = !CS.isEmpty(CS.find(aOldDeletedRelationships, {"relationshipType": RelationshipTypeDictionary.PRODUCT_VARIANT}));
      let bIsLinkedVariantRelationshipCreated = !CS.isEmpty(CS.find(aNewRelationships, {"relationshipType": RelationshipTypeDictionary.PRODUCT_VARIANT}));

      if (bIsLinkedVariantRelationshipDeleted && bIsLinkedVariantRelationshipCreated) {
        oClassToReturn.addedContexts.productVariantContexts = oClassToReturnContexts.productVariantContexts;
      }

    } else {
      oClassToReturn.isTypeChanged = true;
      oClassToReturn.deletedRelationships = [];
    }
    oClassToReturn.modifiedRelationships = [];
    oClassToReturn.addedRelationships = [];

    _generateADMForKlassRelationships(aNewRelationships, aOldRelationships, oClassToReturn);


    delete oClassToReturn.permission;
    delete oClassToReturn.notificationSettings;

    return oClassClone;

  };

  /* Function to generate ADM for Export Side 2 Relationship Data */
  let _generateADMForExportRelationshipsInClass = function (oClass) {

    let oClassClone = CS.cloneDeep(oClass);
    if(!oClassClone.clonedObject){
      return oClassClone;
    }

    let oNewClassClonedObject = oClassClone.clonedObject;
    let oRelationshipExport = oNewClassClonedObject.relationshipExport;
    let oRelationshipExportOld = oClass.relationshipExport;
    if (oRelationshipExport) {
      oRelationshipExport.addedRelationshipsToExport = CS.difference(oRelationshipExport.relationships, oRelationshipExportOld.relationships);
      oRelationshipExport.deletedRelationshipsToExport = CS.difference(oRelationshipExportOld.relationships, oRelationshipExport.relationships);
      oRelationshipExport.addedAttibutesToExport = CS.difference(oRelationshipExport.attributes, oRelationshipExportOld.attributes);
      oRelationshipExport.deletdAttibutesToExport = CS.difference(oRelationshipExportOld.attributes, oRelationshipExport.attributes);
      oRelationshipExport.addedTagsToExport = CS.difference(oRelationshipExport.tags, oRelationshipExportOld.tags);
      oRelationshipExport.deletedTagsToExport = CS.difference(oRelationshipExportOld.tags, oRelationshipExport.tags);

      delete oClass.relationshipExport;
      delete oRelationshipExport.relationships;
      delete oRelationshipExport.attributes;
      delete oRelationshipExport.tags;
      delete oRelationshipExport.referencedData;
    }
    return oClassClone;
  };

  var _getADMOfPropertyList = function (aPreviousList, aNewList, sSide) {
    var oADM = {
      added: [],
      deleted: [],
      modified: []
    };

    CS.forEach(aPreviousList, function (oPrevAttr) {
      var oNewAttr = CS.find(aNewList, {id: oPrevAttr.id});
      if(oNewAttr) {
        //Modified
        if(oNewAttr.couplingType != oPrevAttr.couplingType) {
          oNewAttr.side = sSide;
          oADM.modified.push(oNewAttr);
        }
      } else {
        //Deleted
        oPrevAttr.side = sSide;
        oADM.deleted.push(oPrevAttr);
      }
    });


    CS.forEach(aNewList, function (oNewAttr) {
      if(!CS.find(aPreviousList, {id: oNewAttr.id})) {
        //Added
        oNewAttr.side = sSide;
        oADM.added.push(oNewAttr);
      }

    });

    return oADM;

  };

  var _generateNatureRelationshipPropertyADM = function (oClonedRelationship, oPreviousRelationship) {
    var oADM = {
      addedAttributes: [],
      modifiedAttributes: [],
      deletedAttributes: [],
      addedPrices: [],
      deletedPrices: [],
      addedTags: [],
      modifiedTags: [],
      deletedTags: [],
      addedRelationshipInheritance: [],
      modifiedRelationshipInheritance: [],
      deletedRelationshipInheritance: []
    };

    var oClonedSide1 = oClonedRelationship.side1;
    var oClonedSide2 = oClonedRelationship.side2;

    var oPreviousSide1 = oPreviousRelationship.side1;
    var oPreviousSide2 = oPreviousRelationship.side2;

    //Side1 Attributes
    var oAttrADM = _getADMOfPropertyList(oPreviousSide1.attributes, oClonedSide1.attributes, "side1");
    oADM.addedAttributes = oADM.addedAttributes.concat(oAttrADM.added);
    oADM.modifiedAttributes = oADM.modifiedAttributes.concat(oAttrADM.modified);
    oADM.deletedAttributes = oADM.deletedAttributes.concat(oAttrADM.deleted);
    let oCustomTabsADM = SettingUtils.generateADMForCustomTabs(oPreviousRelationship.tab, oClonedRelationship.tab);
    oADM.addedTab = oCustomTabsADM.addedTab;
    oADM.deletedTab = oCustomTabsADM.deletedTab;

    //Side2 Attributes
    oAttrADM = _getADMOfPropertyList(oPreviousSide2.attributes, oClonedSide2.attributes, "side2");
    oADM.addedAttributes = oADM.addedAttributes.concat(oAttrADM.added);
    oADM.modifiedAttributes = oADM.modifiedAttributes.concat(oAttrADM.modified);
    oADM.deletedAttributes = oADM.deletedAttributes.concat(oAttrADM.deleted);

    //Side2 Prices (Currently Prices Is Only Required In Side 2)
    var oPricesADM = _getADMOfPropertyList(oPreviousSide2.prices, oClonedSide2.prices, "side2");
    oADM.addedPrices = oADM.addedPrices.concat(oPricesADM.added);
    oADM.deletedPrices = oADM.deletedPrices.concat(oPricesADM.deleted);

    //Side1 Tags
    var oTagADM = _getADMOfPropertyList(oPreviousSide1.tags, oClonedSide1.tags, "side1");
    oADM.addedTags = oADM.addedTags.concat(oTagADM.added);
    oADM.modifiedTags = oADM.modifiedTags.concat(oTagADM.modified);
    oADM.deletedTags = oADM.deletedTags.concat(oTagADM.deleted);

    //Side2 Tags
    oTagADM = _getADMOfPropertyList(oPreviousSide2.tags, oClonedSide2.tags, "side2");
    oADM.addedTags = oADM.addedTags.concat(oTagADM.added);
    oADM.modifiedTags = oADM.modifiedTags.concat(oTagADM.modified);
    oADM.deletedTags = oADM.deletedTags.concat(oTagADM.deleted);

    //Side1 Relationship Inheritance
    var oRelationshipInheritanceADM = _getADMOfPropertyList(oPreviousSide1.relationships, oClonedSide1.relationships, "side1");
    oADM.addedRelationshipInheritance = oADM.addedRelationshipInheritance.concat(oRelationshipInheritanceADM.added);
    oADM.modifiedRelationshipInheritance = oADM.modifiedRelationshipInheritance.concat(oRelationshipInheritanceADM.modified);
    let aDeletedRelationships = oADM.deletedRelationshipInheritance.concat(oRelationshipInheritanceADM.deleted);
    oADM.deletedRelationshipInheritance = CS.map(aDeletedRelationships , 'id');


    delete oClonedSide1.attributes;
    delete oClonedSide1.tags;
    delete oClonedSide2.attributes;
    delete oClonedSide2.tags;
    delete oClonedSide1.relationships;

    CS.assign(oClonedRelationship, oADM);

  };

  var _generateClassRelationships = function (oClassToSave) {
    //Article Nature Relationship
    if (oClassToSave.type === MockDataForEntityBaseTypesDictionary.articleKlassBaseType) {
      _generateNatureRelationships(oClassToSave);
    }
  };

  var _getRequestDataForClass = function (oClassToSave) {
    let oSuccessSaveCallBack = "";
    let oFailureSaveCallBack = "";
    let sSaveURL = "";
    let sCreateURL = "";
    switch(oClassToSave.type){
      case MockDataForEntityBaseTypesDictionary.articleKlassBaseType :
        sCreateURL = oClassRequestMapping.CreateClass;
        oSuccessSaveCallBack = successSaveClassCallback;
        oFailureSaveCallBack = failureSaveClassCallback;
        sSaveURL = oClassRequestMapping.SaveClass;
        break;
      case MockDataForEntityBaseTypesDictionary.assetKlassBaseType:
        sCreateURL = oAssetRequestMapping.CreateAsset;
        oSuccessSaveCallBack = successSaveAssetClassCallback;
        oFailureSaveCallBack = failureSaveAssetClassCallback;
        sSaveURL = oAssetRequestMapping.SaveAsset;
        oClassToSave.isDetectDuplicateModified = SettingUtils.checkKeyValueModified(ClassUtils.getActiveClass(), "detectDuplicate");
        break;
      case MockDataForEntityBaseTypesDictionary.marketKlassBaseType:
        sCreateURL = oTargetRequestMapping.CreateTarget;
        oSuccessSaveCallBack = successSaveTargetClassCallback;
        oFailureSaveCallBack = failureSaveTargetClassCallback;
        sSaveURL = oTargetRequestMapping.SaveTarget;
        break;
      case MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType:
        sCreateURL = oTextAssetRequestMapping.CreateTextAssets;
        oSuccessSaveCallBack = successSaveTextAssetClassCallback;
        oFailureSaveCallBack = failureSaveTextAssetClassCallback;
        sSaveURL = oTextAssetRequestMapping.SaveTextAssets;
        break;
      case MockDataForEntityBaseTypesDictionary.supplierKlassBaseType:
        sCreateURL = oSupplierRequestMapping.CreateSuppliers;
        oSuccessSaveCallBack = successSaveSupplierClassCallback;
        oFailureSaveCallBack = failureSaveSupplierClassCallback;
        sSaveURL = oSupplierRequestMapping.SaveSuppliers;
        break;
    }
    return {
      createURL : sCreateURL,
      saveURL: sSaveURL,
      successSaveCallBack: oSuccessSaveCallBack,
      failureSaveCallBack: oFailureSaveCallBack
    }
  };

  let _getNumberOfMaxVersions = function (oActiveClass) {
    let oClass = oActiveClass.clonedObject || oActiveClass;
    let iNumberOfVersionsToMaintain = oClass.numberOfVersionsToMaintain;

    return !CS.isInteger(parseFloat(iNumberOfVersionsToMaintain));
  };

  let isAnyContextDeleted = function(oClassToSave) {
    let bAnyAttributeContextDeleted =  CS.some(oClassToSave.modifiedElements , ["attributeVariantContext" , undefined]);
    let bAnyEmbeddedContextDeleted = CS.isNotEmpty(oClassToSave.deletedContextKlasses);
    let bAnyGtinDeleted = CS.isNotEmpty(oClassToSave.deletedGtinKlass);

    if(bAnyAttributeContextDeleted || bAnyEmbeddedContextDeleted || bAnyGtinDeleted){
      return true;
    }
  };

  var _saveClass = function (oCallback, bSilentMode) {
    let oActiveClass = ClassUtils.getActiveClass();
    let oCurrentClass = oActiveClass.clonedObject || oActiveClass;
    //TODO: Change implementation
    if (oActiveClass.type === MockDataForEntityBaseTypesDictionary.taskKlassBaseType) {
      _saveTaskClass(oCallback);
      return;
    }
    let sClassId = oActiveClass.id;
    let bIsCreated = oActiveClass.isCreated;
    let oClassWithSectionADM = null;
    let oClassWithAssetConfiguationADM = null;
    let oClassToSave = null;
    let bIsNature = oActiveClass.isNature;
    if (bIsCreated) {
      oClassToSave = CS.cloneDeep(oActiveClass);
      if (!oClassToSave.isNature) {
        oClassToSave.natureType = ""; //remove nature type if non nature selected
      }
      if (oClassToSave.type !== MockDataForEntityBaseTypesDictionary.articleKlassBaseType) {
        oClassToSave.id = null;
      }
      _generateClassRelationships(oClassToSave);
    } else if(bIsNature && _getNumberOfMaxVersions(oActiveClass)) {
      alertify.message(getTranslation().DECIMAL_VALUE_IN_MAX_VERSIONS);
      return;
    } else {
      oClassWithAssetConfiguationADM = _generateADMForAssetUploadConfiguration(oCallback, oActiveClass);
      oCallback = oClassWithAssetConfiguationADM.callBackData;
      oClassWithSectionADM = _generateADMForSectionsInClass(oClassWithAssetConfiguationADM.classToSave);
      let oClassWithExportRelationshipADM = _generateADMForExportRelationshipsInClass(oClassWithSectionADM);
      oClassToSave = _translateClassForServer(oClassWithExportRelationshipADM);

      /* Export Side 2 Relationship Collapsed button Disabled */
      let oCollapsedEnableObject = ClassProps.getSide2RelationshipCollapseEnabled();
      oCollapsedEnableObject.propertiesList = true;
      oCollapsedEnableObject.relationshipList = true;
    }
    if (oClassToSave === 0) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    } else if (oClassToSave === -1) {
      alertify.message(getTranslation().CLASS_SELECT_CLASS_IN_RELATIONSHIP);
      return;
    }
    let sLabel = oClassToSave.label;
    if (CS.isEmpty(sLabel.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    let oRequestData = _getRequestDataForClass(oClassToSave);
    let sCreateURL = oRequestData.createURL;
    let oSuccessSaveCallBack = oRequestData.successSaveCallBack;
    let oFailureSaveCallBack = oRequestData.failureSaveCallBack;
    let sSaveURL = oRequestData.saveURL;

    delete oClassToSave.currentAssetUploadConfigurationModel;
    delete oActiveClass.currentAssetUploadConfigurationModel;

    //This method remove keys from request data.
    SettingUtils.removeKeysFromRequestData(oClassToSave, ['iconKey']);

    if (oActiveClass.isCreated) {
      let oCodeToVerifyUniqueness = {
        id: oClassToSave.code,
        entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS
      };
      let oCallbackDataForUniqueness = {};
      oCallbackDataForUniqueness.functionToExecute = _createClassCall.bind(this, oClassToSave, sClassId, bSilentMode, oCallback, sCreateURL, oSuccessSaveCallBack, oFailureSaveCallBack);

      let sURL = oClassRequestMapping.CheckEntityCode;
      SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
    } else {
      oCallback.isDeletedAnyContext = isAnyContextDeleted(oClassToSave);
      SettingUtils.csPostRequest(sSaveURL, {}, oClassToSave, oSuccessSaveCallBack.bind(this, oCallback, sClassId, bSilentMode), oFailureSaveCallBack);
    }
  };

  var _createClassCall = function (oClassToSave, sClassId, bSilentMode, oCallback, sCreateURL, oSuccessSaveCallBack, oFailureSaveCallBack) {
    SettingUtils.csPutRequest(sCreateURL, {}, oClassToSave, oSuccessSaveCallBack.bind(this, oCallback, sClassId, bSilentMode), oFailureSaveCallBack);
  };

  var _alertifyMessage = function (sMessage, bSilentMode) {
    if(bSilentMode) {
      ExceptionLogger.info(sMessage);
    } else {
      alertify.message(sMessage);
    }
  };

  var _alertifyError = function (sMessage, bSilentMode) {
    if(bSilentMode) {
      ExceptionLogger.error(sMessage);
    } else {
      alertify.error(sMessage);
    }
  };

  var _alertifySuccess = function (sMessage, bSilentMode) {
    if(bSilentMode) {
      ExceptionLogger.log(sMessage);
    } else {
      alertify.success(sMessage);
    }
  };

  var _saveTaskClass = function (oCallback, bSilentMode) {
    var oActiveClass = ClassUtils.getActiveClass();
    var aMasterTaskList = SettingUtils.getAppData().getTaskList();

    var oTaskWithSectionADM = _generateADMForSectionsInClass(oActiveClass);
    var oClassToSave = _translateClassForServer(oTaskWithSectionADM);

    if (oClassToSave == 0) {
      _alertifyMessage(getTranslation().NOTHING_CHANGED_TO_SAVE, bSilentMode);
      return;

    } else if (oClassToSave == -1) {
      _alertifyMessage(getTranslation().CLASS_SELECT_CLASS_IN_RELATIONSHIP, bSilentMode);
      return;
    }

    if(CS.isEmpty(oClassToSave.label)) {
      _alertifyError(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME, bSilentMode);
      return;
    }

    var oCallbackData = {};
    if (oCallback.functionToExecute) {
      oCallbackData.functionToExecute = oCallback.functionToExecute;
    }

    if(oActiveClass.isCreated){
      SettingUtils.csPutRequest(oTaskRequestMapping.CreateTask, {}, oClassToSave, successSaveTaskClassCallback.bind(this, oCallbackData, oActiveClass.id, bSilentMode), failureSaveTaskClassCallback);
    } else {
      SettingUtils.csPutRequest(oTaskRequestMapping.SaveTask, {}, oClassToSave, successSaveTaskClassCallback.bind(this, oCallbackData, oActiveClass.id, bSilentMode), failureSaveTaskClassCallback);
    }

  };

  let _getDeleteActiveMXMKlassDataBySelection = function (sClassType, oCallback) {
    let sURL = "";
    let fSuccessCallback;
    let fFailureCallback;
    let oClassValueList = ClassProps.getClassValueListByTypeGeneric(sClassType);
    let oMarketListItem = oClassValueList["market"];
    if (oMarketListItem && oMarketListItem.isSelected) {
      sURL = oTargetRequestMapping.DeleteTarget;
      fSuccessCallback = successRemoveTargetCallback;
      fFailureCallback = failureRemoveTargetCallback.bind(this, oCallback);
    }
    return [sURL, fSuccessCallback, fFailureCallback]
  };

  let _getDeleteClassRequestData = function (sContext, oCallback) {
    let sURL = "";
    let oSuccessCallback = {};
    let oFailureCallback = {};
    switch (sContext){
      case 'class':
        sURL = oClassRequestMapping.DeleteClasses;
        oSuccessCallback = successRemoveClassCallback;
        oFailureCallback = failureRemoveClassCallback.bind(this, oCallback);
        break;

      case 'task':
        sURL = oTaskRequestMapping.DeleteTasks;
        oSuccessCallback = successRemoveTaskCallback;
        oFailureCallback = failureRemoveTaskCallback;
        break;

      case 'asset':
        sURL = oAssetRequestMapping.DeleteAssets;
        oSuccessCallback = successRemoveAssetCallback;
        oFailureCallback = failureRemoveAssetCallback.bind(this, oCallback);
        break;

      case 'target':
        [sURL, oSuccessCallback, oFailureCallback] = _getDeleteActiveMXMKlassDataBySelection(sContext, oCallback);
        break;

      case 'textasset':
        sURL = oTextAssetRequestMapping.DeleteTextAssets;
        oSuccessCallback = successRemoveTextAssetCallback;
        oFailureCallback = failureRemoveTextAssetCallback.bind(this, oCallback);
        break;

      case 'supplier':
        sURL = oSupplierRequestMapping.DeleteSuppliers;
        oSuccessCallback = successRemoveSupplierCallback;
        oFailureCallback = failureRemoveSupplierCallback.bind(this, oCallback);
        break;
    }
    return {
      URL : sURL,
      successCallback : oSuccessCallback,
      failureCallback : oFailureCallback
    };
  };

  var _removeClasses = function (sContext, aSavedClassIdsToDelete, aCreatedClassIds, oCallback) {
    var oClassData = {};
    oClassData.ids = aSavedClassIdsToDelete;
    var oClassValueList = _getClassValueListByTypeGeneric(sContext);;
    var aClassList = SettingUtils.getAppData().getClassListByTypeGeneric(sContext);
    CS.forEach(aCreatedClassIds, function (sCreatedId) {
      SettingUtils.removeNodeById(aClassList, sCreatedId);
      delete oClassValueList[sCreatedId];
    });

    if (CS.isEmpty(oClassData.ids)) {
      ClassUtils.setClassScreenLockedStatus(false);
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_REMOVED_SUCCESSFULLY,{entity : getTranslation().S_CLASS}));
      _triggerChange();
    } else {
      let oRequestData = _getDeleteClassRequestData(sContext, oCallback);
      SettingUtils.csDeleteRequest(oRequestData.URL, {}, oClassData, oRequestData.successCallback, oRequestData.failureCallback);
    }
  };

  var _createDummyClassNode = function (oParentClass) {
    let oDefaultClassType = oParentClass.type;
    let oReturnObj = {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      code: "",
      isCreated: true,
      parent: {id: oParentClass.id, type: oDefaultClassType},
      type: oDefaultClassType,
      secondaryType: oParentClass.secondaryType,
      isNature: oParentClass.isNature,
      natureType: oParentClass.natureType,
      startDate: "",
      endDate: "",
      shouldVersion: true,
      numberOfVersionsToMaintain: 10,
      children: [],
      relationships: [],
      attributes: [],
      tags:[],
      sections: [],
      icon: "",
      isVisualAttribute: false,
      referencedClassIds: {},
      referencedAttributeIds: {}
    };

    return oReturnObj;
  };

  var _createDummyTaskNode = function (oParentClass) {
    let oDefaultClassType = oParentClass.type;
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      parent: {id: oParentClass.id, type: oDefaultClassType},
      type: oDefaultClassType,
      children: [],
      sections: [],
      icon: ""
    };
  };

  var _createDummyAssetNode = function (oParentClass) {
    let oDefaultClassType = oParentClass.type;
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      code: "",
      parent: {id: oParentClass.id, type: oDefaultClassType},
      type: oDefaultClassType,
      children: [],
      sections: [],
      icon: "",
      isNature: oParentClass.isNature || false,
      natureType: oParentClass.natureType || "",
      numberOfVersionsToMaintain: 10,
    };
  };

  var _createDummyTargetNode = function (oParentClass) {
    let oDefaultClassType = oParentClass.type;
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      code: "",
      parent: {id: oParentClass.id, type: oDefaultClassType},
      type: oDefaultClassType,
      numberOfVersionsToMaintain: 10,
      children: [],
      sections: [],
      icon: "",
      isNature: oParentClass.isNature || false,
      natureType: oParentClass.natureType || "",
    };
  };

  var _createDummyTextAssetNode = function (oParentClass) {
    let oDefaultClassType = oParentClass.type;
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      code: "",
      parent: {id: oParentClass.id, type: oDefaultClassType},
      type: oDefaultClassType,
      children: [],
      sections: [],
      icon: "",
      isNature: oParentClass.isNature || false,
      natureType: oParentClass.natureType || "",
      numberOfVersionsToMaintain: 10,
    };
  };

  var _createDummySupplierNode = function (oParentClass) {
    let oDefaultClassType = oParentClass.type;
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      code: "",
      parent: {id: oParentClass.id, type: oDefaultClassType},
      type: oDefaultClassType,
      children: [],
      sections: [],
      icon: "",
      isNature: oParentClass.isNature || false,
      natureType: oParentClass.natureType || "",
    };
  };

  var _switchActiveClass = function (oClassValue, sContext) {
    var aList = SettingUtils.getAppData().getClassListByTypeGeneric(sContext);
    var oValueList = _getClassValueListByTypeGeneric(sContext);
    var oClass = SettingUtils.getNodeFromTreeListById(aList, oClassValue.id);

    SettingUtils.applyValueToAllTreeNodes(oValueList, 'isActive', false);
    SettingUtils.applyValueToAllTreeNodes(oValueList, 'isChecked', 0);
    SettingUtils.setPropertyValue(oValueList, oClassValue.id, oClassValue);
    oValueList[oClassValue.id].isSelected = true;
    oValueList[oClassValue.id].isActive = true;
    oValueList[oClassValue.id].isChecked = 2;

    if(!oValueList[oClassValue.id].isExpanded){
      oValueList[oClassValue.id].isExpanded = !oValueList[oClassValue.id].isExpanded;
    }
    ClassProps.setSelectedSectionId('');
    ClassProps.setIsGtinKlassEnabled(!CS.isEmpty(oClass.gtinKlassId));

    if(oClassValue.id == -1){
      _toggleLinkScrollActive(1);
    }
    _storeAllSectionRoleList(oClass);
    _createClassSectionElementMap();
    _fillClassSectionElementMap(oClass);
    _setActiveClass(oClass);
  };

  var _toggleLinkScrollActive = function(sId){
    var oScrollLinkList = ClassProps.getScrollLinkList();
    SettingUtils.toggleLinkScrollActive(oScrollLinkList.linkView1, sId);
    //SettingUtils.toggleLinkScrollActive(oScrollLinkList.linkView2, sId);
  };

  var _getDummyClassNode = function (sContext, oClass) {
    switch (sContext){
      case 'class':
        return _createDummyClassNode(oClass);

      case 'task':
        return _createDummyTaskNode(oClass);

      case 'asset':
        return _createDummyAssetNode(oClass);

      case 'target':
        return _createDummyTargetNode(oClass);

      case 'textasset':
        return _createDummyTextAssetNode(oClass);

      case 'supplier':
        return _createDummySupplierNode(oClass);
    }
    return null;
  };

  var _createNewClass = function (sContext, oClassValue, parentId) {
    let aClassList = SettingUtils.getAppData().getClassListByTypeGeneric(sContext);
    let oClassValueList = _getClassValueListByTypeGeneric(sContext);
    let oClass = {};
    if (CS.isEmpty(oClassValue)) {
      alertify.message(getTranslation().CLASS_PLEASE_SELECT);
    } else {
      oClass = SettingUtils.getNodeFromTreeListById(aClassList, oClassValue.id);
      let oDummyClassNode = _getDummyClassNode(sContext, oClass);
      SettingUtils.addNewTreeNodesToValueList(oClassValueList, [oDummyClassNode]);
      SettingUtils.applyValueToAllTreeNodes(oClassValueList, 'isActive', false);
      SettingUtils.applyValueToAllTreeNodes(oClassValueList, 'isChecked', 0);
      let aChildrenKeyValueToReset = [
        {key: "isExpanded", value: false},
        {key: "isSelected", value: false}
      ];
      SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oClass, oClassValueList, true);

      SettingUtils.setPropertyValue(oClassValueList[oDummyClassNode.id], 'isSelected', true);
      SettingUtils.setPropertyValue(oClassValueList[oDummyClassNode.id], 'isActive', true);
      SettingUtils.setPropertyValue(oClassValueList[oDummyClassNode.id], 'isEditable', false);
      SettingUtils.setPropertyValue(oClassValueList[oDummyClassNode.id], 'isChecked', 2);
      SettingUtils.setPropertyValue(oClassValueList[oDummyClassNode.id], 'isChildLoaded', true);
      SettingUtils.setPropertyValue(oClassValueList[oDummyClassNode.id], 'canDelete', true);
      SettingUtils.setPropertyValue(oClassValueList[oDummyClassNode.id], 'canCreate', true);

      if (CS.isEmpty(oClassValue)) {
        aClassList.push(oDummyClassNode);
      } else {
        oClass = SettingUtils.getNodeFromTreeListById(aClassList, oClassValue.id);

        if (parentId) {
          if (CS.isEmpty(oClass)) {
            oClass = _getClassFromMasterListById(parentId);
          }
        }

        if (!CS.isEmpty(oClass)) {
          SettingUtils.setPropertyValue(oClassValueList[oClass.id], 'isExpanded', true);
          oClass.children.push(oDummyClassNode);
          if (oClass.type === MockDataForEntityBaseTypesDictionary.assetKlassBaseType) {
            _fetchAssetParentKlassNatureType(oClassValue.id);
          }

        } else {
          ClassUtils.setClassScreenLockedStatus(false);
          alertify.message(getTranslation().CLASS_CANNOT_CREATE_CHILD_UNDEFINED_CLASS);
          return;
        }
      }
      _setActiveClass(oDummyClassNode);
      ClassProps.emptySectionRoleList();
      ClassProps.emptySectionRoleValuesList();
      ClassUtils.setClassScreenLockedStatus(true);
    }
  };

  var _createNewTask = function (oClassValue, parentId) {
    var aTaskList = SettingUtils.getAppData().getTaskList();
    var oTaskValueList = _getTaskValueList();
    if (CS.isEmpty(oClassValue)) {
      alertify.message(getTranslation().TASK_PLEASE_SELECT);
    } else {
      oClass = SettingUtils.getNodeFromTreeListById(aTaskList, oClassValue.id);
      var oDummyTaskNode = _createDummyTaskNode(oClass);
      SettingUtils.addNewTreeNodesToValueList(oTaskValueList, [oDummyTaskNode]);
      SettingUtils.applyValueToAllTreeNodes(oTaskValueList, 'isSelected', false);
      SettingUtils.applyValueToAllTreeNodes(oTaskValueList, 'isChecked', 0);
      SettingUtils.setPropertyValue(oTaskValueList[oDummyTaskNode.id], 'isSelected', true);
      SettingUtils.setPropertyValue(oTaskValueList[oDummyTaskNode.id], 'isEditable', false);
      SettingUtils.setPropertyValue(oTaskValueList[oDummyTaskNode.id], 'isChecked', 2);
      SettingUtils.setPropertyValue(oTaskValueList[oDummyTaskNode.id], 'isChildLoaded', true);
      if (CS.isEmpty(oClassValue)) {
        aTaskList.push(oDummyTaskNode);
      } else {
        var oClass = SettingUtils.getNodeFromTreeListById(aTaskList, oClassValue.id);
        if (parentId) {
          if (CS.isEmpty(oClass)) {
            oClass = _getClassFromMasterListById(parentId, aTaskList);
          }
        }
        if (!CS.isEmpty(oClass)) {
          SettingUtils.setPropertyValue(oTaskValueList[oClass.id], 'isExpanded', true);
          oClass.children.push(oDummyTaskNode);
        } else {
          ClassUtils.setClassScreenLockedStatus(false);
          alertify.message(getTranslation().TASK_CANNOT_CREATE_CHILD_UNDEFINED_CLASS);
          return;
        }
      }
      _setActiveClass(oDummyTaskNode);
      ClassProps.emptySectionRoleList();
      ClassProps.emptySectionRoleValuesList();
      ClassUtils.setClassScreenLockedStatus(true);
      SettingUtils.csPutRequest(oTaskRequestMapping.CreateTask, {}, oDummyTaskNode, successSaveTaskClassCallback.bind(this, {}, oDummyTaskNode.id, false), failureSaveTaskClassCallback);
    }
  };

  var _changeAllValueDataOfClass = function (sId, sKey, defaultValue, value, sContext) {
    let oClassValueList = _getClassValueList();
    let oTaskValueList = _getTaskValueList();
    let oAssetValueList = _getAssetValueList();
    let oTargetValueList = _getTargetValueList();
    let oEditorialValueList = _getEditorialValueList();
    let oTextAssetValueList = _getTextAssetValueList();
    let oSupplierValueList = _getSupplierValueList();
    CS.forEach(oClassValueList, function (oClassValue) {
      oClassValue[sKey] = defaultValue;
    });

    CS.forEach(oTaskValueList, function (oTaskValue) {
      oTaskValue[sKey] = defaultValue;
    });

    CS.forEach(oAssetValueList, function (oAssetValue) {
      oAssetValue[sKey] = defaultValue;
    });

    CS.forEach(oTargetValueList, function (oTargetValue) {
      oTargetValue[sKey] = defaultValue;
    });

    CS.forEach(oEditorialValueList, function (oEditorialValue) {
      oEditorialValue[sKey] = defaultValue;
    });

    CS.forEach(oTextAssetValueList, function (oTextAssetValue) {
      oTextAssetValue[sKey] = defaultValue;
    });

    CS.forEach(oSupplierValueList, function (oSupplierValue) {
      oSupplierValue[sKey] = defaultValue;
    });

    if(sContext == "task") {
      oTaskValueList[sId][sKey]= value;
    } else if(sContext == "asset") {
      oAssetValueList[sId][sKey]= value;
    } else if(sContext == "target") {
      oTargetValueList[sId][sKey]= value;
    }  else if(sContext == "textasset") {
      oTextAssetValueList[sId][sKey]= value;
    } else if(sContext == "supplier") {
      oSupplierValueList[sId][sKey]= value;
    } else {
      oClassValueList[sId][sKey] = value;
    }

  };

  var _fetchClassDetailsAndSwitch = function (oClassValue, sContext) {
    let sUrl = "";
    let oParameters = {};
    let oRequestData = {};
    let oCallbackData = {};
    let _failureFetchClassElaborateCallback;
    let _successFetchClassElaborateCallback;

    _changeAllValueDataOfClass(oClassValue.id, 'isActive', false, true, sContext);
    _changeAllValueDataOfClass(oClassValue.id, 'isChecked', false, true, sContext);
    _changeAllValueDataOfClass(oClassValue.id, 'isLoading', false, true, sContext);

    let oHierarchyTree = SettingUtils.getAppData().getClassListByTypeGeneric(sContext);
    let oVisualProps = _getClassValueListByTypeGeneric(sContext);
    let oParentNode = SettingUtils.getParentNodeByChildId(oHierarchyTree, oClassValue.id);
    let aChildrenKeyValueToReset = [
      {key: "isExpanded", value: false},
      {key: "isSelected", value: false}
    ];
    SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oVisualProps, true);

    let oValuesList = _getClassValueListByTypeGeneric(sContext);
    let oSelectedValue = oValuesList[oClassValue.id];
    oParameters.id = oClassValue.id;
    oRequestData.getChildren = true;

    if(sContext === "task") {
      _failureFetchClassElaborateCallback = failureFetchTaskClassElaborateCallback.bind(this, oSelectedValue);
      _successFetchClassElaborateCallback = successFetchTaskClassElaborateCallback.bind(this, oSelectedValue, oCallbackData);
      sUrl = oTaskRequestMapping.GetClassWithoutPC;
    } else if(sContext === "asset") {
      _failureFetchClassElaborateCallback = failureFetchAssetClassElaborateCallback.bind(this, oSelectedValue);
      _successFetchClassElaborateCallback = successFetchAssetClassElaborateCallback.bind(this, oSelectedValue, oCallbackData);
      sUrl = oAssetRequestMapping.GetClassWithoutPC;
    } else if(sContext === "target") {
      _failureFetchClassElaborateCallback = failureFetchTargetClassElaborateCallback.bind(this, oSelectedValue);
      _successFetchClassElaborateCallback = successFetchTargetClassElaborateCallback.bind(this, oSelectedValue, oCallbackData);
      sUrl = oTargetRequestMapping.GetClassWithoutPC;
    }  else if(sContext === "textasset") {
      _failureFetchClassElaborateCallback = failureFetchTextAssetClassElaborateCallback.bind(this, oSelectedValue);
      _successFetchClassElaborateCallback = successFetchTextAssetClassElaborateCallback.bind(this, oSelectedValue, oCallbackData);
      sUrl = oTextAssetRequestMapping.GetClassWithoutPC;
    } else if(sContext === "supplier") {
      _failureFetchClassElaborateCallback = failureFetchSupplierClassElaborateCallback.bind(this, oSelectedValue);
      _successFetchClassElaborateCallback = successFetchSupplierClassElaborateCallback.bind(this, oSelectedValue, oCallbackData);
      sUrl = oSupplierRequestMapping.GetClassWithoutPC;
    } else {
      _failureFetchClassElaborateCallback = failureFetchClassElaborateCallback.bind(this, oSelectedValue);
      _successFetchClassElaborateCallback = successFetchClassElaborateCallback.bind(this, oSelectedValue, oCallbackData);
      sUrl = oClassRequestMapping.GetClassWithoutPC;
    }

    return SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(sUrl, oParameters), oRequestData,
        _successFetchClassElaborateCallback, _failureFetchClassElaborateCallback);
  };

  var _saveUnsavedClass = function (oCallback) {
    let oCallbackData = {};
    oCallbackData.functionToExecute = oCallback.functionToExecute;
    let oSelectedAndDirtyContext = _getSelectedValueAndContext();
    let sContext = oSelectedAndDirtyContext.context;
    _saveClass(oCallbackData, sContext);

  };

  var _discardUnsavedClass = function (oCallback, sContext) {
    let oUnsavedClass = ClassUtils.getActiveClass();
    let aMasterList = SettingUtils.getAppData().getClassList();
    let oTaskValueList = _getClassValueList();

    if(sContext === 'task') {
      aMasterList = SettingUtils.getAppData().getTaskList();
      oTaskValueList = _getTaskValueList();
    }
    if (oUnsavedClass.isCreated) {
      delete oTaskValueList[oUnsavedClass.id];
      SettingUtils.removeNodeById(aMasterList, oUnsavedClass.id);
      _setActiveClass({});
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    } else if (oUnsavedClass.isDirty) {
      delete oUnsavedClass.isDirty;
      delete oUnsavedClass.clonedObject;
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    } else {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
    }


    _fillClassSectionElementMap(oUnsavedClass);
    _storeAllSectionRoleList(oUnsavedClass);

    ClassUtils.setClassScreenLockedStatus(false);
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var _deleteClasses = function (sClickedNodeId, oCallback) {
    let sContext = ClassProps.getSelectedClassCategory();
    let oClassValueList = _getClassValueListByTypeGeneric(sContext);
    let bIsStandardKlassSelected = false;
    let oClassInfo = oClassValueList[sClickedNodeId];
    if (!oClassInfo) {
      return;
    }
    if (oClassInfo.isStandard) {
      bIsStandardKlassSelected = true;
    }
    let sClassInfoLabel = CS.getLabelOrCode(oClassInfo);
    if (bIsStandardKlassSelected) {
      alertify.message(getTranslation().STANDARD_CLASS_DELETION);
    } else {
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, [sClassInfoLabel],
          function () {
            _removeClasses(sContext, [sClickedNodeId], [], oCallback);
          }, function (oEvent) {
          }, true);
    }
  };

  var _fetchClassDetails = function (oClassValue, sContext, bRefreshCommand) {
    let oActiveClass = ClassUtils.getActiveClass();
    let oValueList = _getClassValueListByTypeGeneric(sContext);
    let bIsClassScreenLocked = _getClassScreenLockedStatus();

    if (bIsClassScreenLocked) {
      if (oActiveClass.id === oClassValue.id) {
        oValueList[oClassValue.id].isExpanded = !oValueList[oClassValue.id].isExpanded;

        if(bRefreshCommand) {
          var oSwitchCallback = {};
          oSwitchCallback.functionToExecute = _fetchClassDetailsAndSwitch.bind(this, oClassValue, sContext);
          CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
              _saveUnsavedClass.bind(this, oSwitchCallback, sContext),
              _discardUnsavedClass.bind(this, oSwitchCallback, sContext),
              function () {});
        }
      } else {
        let oSwitchCallback = {};
        oSwitchCallback.functionToExecute = _fetchClassDetailsAndSwitch.bind(this, oClassValue, sContext);
        CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            _saveUnsavedClass.bind(this, oSwitchCallback, sContext),
            _discardUnsavedClass.bind(this, oSwitchCallback, sContext),
            function () {
            });
      }
    } else {
      return _fetchClassDetailsAndSwitch(oClassValue, sContext);
    }
  };

  var _processAfterGet = function (oActiveClass) {
    var oContextsEnabledMap = ClassProps.getContextsEnabled();
    CS.forEach(oActiveClass.contexts, function (aContexts, sKey) {
      if (sKey != "productVariantContexts") {
        oContextsEnabledMap[sKey] = !CS.isEmpty(aContexts);
      }
    });
    _fillRelationshipsWithReferencedTabs(oActiveClass);
  };

  var _setActiveClass = function (oClass) {
    if (oClass.isCreated) {
      oClass.natureType = _getNatureTypeBasedOnEntityType(oClass);
    }
    ClassProps.setActiveClass(oClass);
  };

  var _getClassScreenLockedStatus = function () {
    return ClassProps.getClassScreenLockedStatus();
  };

  var _getClassValueList = function () {
    return ClassProps.getClassValueList();
  };

  var _getTaskValueList = function () {
    return ClassProps.getTaskValueList();
  };

  var _getAssetValueList = function () {
    return ClassProps.getAssetValueList();
  };

  var _getTargetValueList = function () {
    return ClassProps.getTargetValueList();
  };

  var _getEditorialValueList = function () {
    return ClassProps.getEditorialValueList();
  };

  var _getTextAssetValueList = function () {
    return ClassProps.getTextAssetValueList();
  };

  var _getSupplierValueList = function () {
    return ClassProps.getSupplierValueList();
  };

  var _getClassValueListByTypeGeneric = function(sType){
    return ClassProps.getClassValueListByTypeGeneric(sType);
  };

  var _fetchClassesByTypeGeneric = function () {
    let sSelectedClassCategory = ClassProps.getSelectedClassCategory();
    if(sSelectedClassCategory === "class") {
      _fetchClassesList();
    }
    else if(sSelectedClassCategory === "task") {
      _fetchTasksList();
    }
    else if(sSelectedClassCategory === "target") {
      _fetchTasksList();
    }
    else if(sSelectedClassCategory === "asset") {
      _fetchAssetsList();
    }
    else if(sSelectedClassCategory === "textasset") {
      _fetchTextAssetList();
    }
    else if(sSelectedClassCategory === "supplier") {
      _fetchSupplierList();
    }
  };

  var _getSelectedValueAndContext = function (sNodeId) {
    let sSelectedClassCategory = ClassProps.getSelectedClassCategory();
    let oSelectedValue = null;
    let oClassValueList = _getClassValueListByTypeGeneric(sSelectedClassCategory);
    let oValueList = oClassValueList;
    CS.forEach(oClassValueList, function (oClassValue) {
      if (oClassValue.isSelected && !sNodeId) {
        oSelectedValue = oClassValue;
        return false;
      } else if (sNodeId == oClassValue.id) {
        oSelectedValue = oClassValue;
        return false;
      }
    });
    return {
      selectedValue: oSelectedValue,
      valueList: oValueList,
      context: sSelectedClassCategory
    };
  };

  var _removeElementByReferenceId = function (aElements, sId) {
    let bFoundElement = false;
    CS.forEach(aElements, function (oElement, iIndex) {
      if(oElement.referenceId === sId) {
        aElements.splice(iIndex, 1);
        return false;
      }

      if(oElement.elements.length > 0) {
        bFoundElement = _removeElementByReferenceId(oElement.elements, sId);
        if(bFoundElement) {
          return false;
        }
      }
    });
    return bFoundElement;
  };

  let _handleMSSValueChanged = (sKey, aSelectedItems, oReferencedData) => {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    oActiveClass[sKey] = aSelectedItems;
    switch (sKey) {
      case "embeddedKlassIds":
        CS.forEach(oReferencedData, function (oData, sId) {
          if (!oActiveClass.referencedKlasses[sId]) {
            oActiveClass.referencedKlasses[sId] = oData;
            oActiveClass.referencedKlasses[sId].propagableAttributes = [];
            oActiveClass.referencedKlasses[sId].propagableTags = [];
          }
        });
        break;
      case "marketKlassIds":
        CS.forEach(oReferencedData, function (oData, sId) {
          if (!oActiveClass.referencedKlasses[sId]) {
            oActiveClass.referencedKlasses[sId] = oData;
            oActiveClass.referencedKlasses[sId].selectedTags = [];
          }
        });
        break;
    }
    _triggerChange();
  };

  let _handleMarketTagsChanged = (sKey, aSelectedItems) => {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let aMarketTags =  oActiveClass.referencedKlasses[sKey].selectedTags;
    if (aSelectedItems && aMarketTags) {
      aMarketTags.splice(0, aMarketTags.length);
      CS.forEach(aSelectedItems, function (tag) {
        if (!CS.includes(aMarketTags, tag)) {
          aMarketTags.push(tag);
        }
      });
    }

    _triggerChange();
  };

  let _handleGroupedRelationshipRemoveItemByOtherSide = function(sId){
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    CS.remove(oActiveClass.relationships, function(oRelationship){
        return oRelationship.side2.klassId==sId;
    });
    _saveClass({});
  };

  let _handleRemoveKlassClicked = function (sKey, sId) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    if (sKey === 'embeddedKlassIds' || sKey === 'marketKlassIds') {
      let aIds = oActiveClass[sKey];
      let iIndex = CS.indexOf(aIds, sId);
      aIds.splice(iIndex, 1);
    } else {
      oActiveClass[sKey] = "";
    }
    delete oActiveClass.referencedKlasses[sId];
    _triggerChange();
  };

  let _handleClassDataTransferPropertiesAdded = function (sEntity, aSelectedIds, oReferencedData, sContext) {
    let oScreenProps = SettingUtils.getComponentProps().screen;
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let sSplitter = SettingUtils.getSplitter();
    let sId = sContext.split(sSplitter)[1];
    let oReferencedClass = oActiveClass.referencedKlasses[sId];
    if (sEntity == ConfigDataEntitiesDictionary.ATTRIBUTES) {
      /** add selected items **/
      CS.forEach(aSelectedIds, function (sId) {
        let iIndex = CS.findIndex(oReferencedClass.propagableAttributes, {id: sId});
        if (iIndex == -1) {
          let oSelectedProperty = {
            id: sId,
            couplingType: CouplingConstants.TIGHTLY_COUPLED
          };
          oReferencedClass.propagableAttributes.push(oSelectedProperty);
        }
      });

     /** removed already selected items if exists **/
      CS.remove(oReferencedClass.propagableAttributes, function (oAttribute) {
        if (!CS.includes(aSelectedIds, oAttribute.id)) {
          return oAttribute;
        }
      });

      CS.assign(oScreenProps.getReferencedAttributes(), oReferencedData);
    } else {
      CS.forEach(aSelectedIds, function (sId) {
        let iIndex = CS.findIndex(oReferencedClass.propagableTags, {id: sId});
        if (iIndex == -1) {
          let oSelectedProperty = {
            id: sId,
            couplingType: CouplingConstants.TIGHTLY_COUPLED
          };
          oReferencedClass.propagableTags.push(oSelectedProperty);
        }
      });
      CS.remove(oReferencedClass.propagableTags, function (oTag) {
        if (!CS.includes(aSelectedIds, oTag.id)) {
          return oTag;
        }
      });
      CS.assign(oScreenProps.getReferencedTags(), oReferencedData);
    }
    _triggerChange();
  };

  let _handleClassDataTransferPropertiesRemoved = function (sClassId, sPropertyId, sContext) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oReferencedClass = oActiveClass.referencedKlasses[sClassId];
    if (sContext == ConfigDataEntitiesDictionary.ATTRIBUTES) {
      CS.remove(oReferencedClass.propagableAttributes, {id: sPropertyId});
    } else {
      CS.remove(oReferencedClass.propagableTags, {id: sPropertyId});
    };
    _triggerChange();
  };

  let _handleClassDataTransferPropertiesCouplingChanged = function (sClassId, sPropertyId, sNewValue, sContext) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oReferencedClass = oActiveClass.referencedKlasses[sClassId];
    let oProperty = 0;
    if (sContext == ConfigDataEntitiesDictionary.ATTRIBUTES) {
      oProperty = CS.find(oReferencedClass.propagableAttributes, {id: sPropertyId});
    } else {
      oProperty = CS.find(oReferencedClass.propagableTags, {id: sPropertyId});
    }
    oProperty.couplingType = sNewValue;
    _triggerChange();
  };

  let _handleMSSValueRemoved = (sKey, sId, sContextKey) => {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let aIds = oActiveClass[sKey];
    let iIndex = CS.indexOf(aIds, sId);
    aIds.splice(iIndex, 1);
    if (sContextKey == "classConfigNatureRelationship") {
      delete oActiveClass[sKey];
    }
    _triggerChange();
  };

  var _getSectionRoleValueObject = function (oRole) {
    return {
      id: oRole.id,
      label: oRole.label,
      icon: oRole.icon,
      isChecked: false,
      isEditable: false,
      isSelected: false
    };
  };

  var _setClassScreenLockedStatus = function (bLockStatus) {
    ClassUtils.setClassScreenLockedStatus(bLockStatus);
  };

  var _getSelectedRoleId = function(){
    let oSectionRoleMap = ClassProps.getSectionRoleValuesList();
    let sSelectedRoleId = null;
    CS.forEach(oSectionRoleMap, function(oRoleValue, sRoleId){
      if(oRoleValue.isSelected){
        sSelectedRoleId = sRoleId;
        return false;
      }
    });
    return sSelectedRoleId;
  };

  var _handleDeleteSectionClicked = function(sSectionId){

    if (sSectionId) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let oSection = CS.find(oActiveClass.sections, {id: sSectionId});

      if (oSection) {
        SettingUtils.handleSectionDeleted(oActiveClass, sSectionId);
      }
      _makeDeletedSectionElementsAvailable(oSection.elements);
      _makeDeletedSectionsAvailable(oSection);
      ClassUtils.setClassScreenLockedStatus(true);
      //_refreshSequenceIds(oActiveClass.sections);
      //Added for autosave
      _saveClass({});
    }
  };

  var _handleSectionSkippedToggled = function(sSectionId){
    if (sSectionId) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let oSection = CS.find(oActiveClass.sections, {id: sSectionId});
      if (oSection) {
        oSection.isSkipped = !oSection.isSkipped;
      }
      _triggerChange();
    }
  };

  var _makeDeletedSectionElementsAvailable = function(aElements){
    let oAttributeMap = ClassProps.getClassSectionAttributeMap();
    let oTagMap = ClassProps.getClassSectionTagMap();
    let oRelationshipMap = ClassProps.getClassSectionRelationshipMap();
    let oRoleMap = ClassProps.getClassSectionRoleMap();
    CS.forEach(aElements, function (oDeletedElement) {
      if(oDeletedElement.type == "attribute" && oAttributeMap[oDeletedElement.attribute.id]) {
        oAttributeMap[oDeletedElement.attribute.id].isAvailable = true;
      } else if(oDeletedElement.type == "tag" && oTagMap[oDeletedElement.tag.id]) {
        oTagMap[oDeletedElement.tag.id].isAvailable = true;
      } else if(oDeletedElement.type == "relationship") {
        oRelationshipMap[oDeletedElement.relationship.id].isAvailable = true;
      } else if(oDeletedElement.type == "role" && oRoleMap[oDeletedElement.role.id]) {
        oRoleMap[oDeletedElement.role.id].isAvailable = true;
      }
    });
  };

  var _makeDeletedSectionsAvailable = function(oSection){
    let oSectionMap = ClassProps.getClassSectionMap();
    if (oSectionMap[oSection.propertyCollectionId]) {
      oSectionMap[oSection.propertyCollectionId].isAvailable = true;
    }
  };

  var _handleSectionMoveUpClicked = function(sSectionId){
    let oCurrentClass = ClassUtils.getCurrentClass();
    if (SettingUtils.handleSectionMoveUp(oCurrentClass, sSectionId)) {
      _saveClass({});
    }
  };

  var _handleSectionMoveDownClicked = function(sSectionId){
    let oCurrentClass = ClassUtils.getCurrentClass();
    if (SettingUtils.handleSectionMoveDown(oCurrentClass, sSectionId)) {
      _saveClass({});
    }
  };

  var _handleSectionIconChanged = function (sIconKey) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let aSectionsInClass = oActiveClass.sections;
    let sSelectedSectionId = ClassProps.getSelectedSectionId();

    var oSelectedSection = CS.find(aSectionsInClass, {'id': sSelectedSectionId});
    oSelectedSection.icon = sIconKey ? sIconKey : "";
    _triggerChange();
  };

  var _handleClassContextDialogDateValueChanged = function(sKey, sDateValue){
    let oContextData = _makeContextDirty();
    let oDefaultTimeRange = oContextData.defaultTimeRange || {};
    let sOtherKey = sKey == 'from' ? 'to' : 'from';

    if(sKey !== "fromCurrentDate"){
      oDefaultTimeRange[sKey] = sDateValue;
      if (sKey==="from" && !oDefaultTimeRange[sOtherKey] && !oDefaultTimeRange.isCurrentTime) {
        oDefaultTimeRange[sOtherKey] = sDateValue;
      }
    }
    else {
      oDefaultTimeRange.from = oDefaultTimeRange.to = null;
      oDefaultTimeRange.isCurrentTime = !oDefaultTimeRange.isCurrentTime;
    }

    oContextData.defaultTimeRange = oDefaultTimeRange;
  };

  var _handleElementAutoIsVariantAllowedChange = function(oElement){
    if(oElement.tagGroups){
      if(oElement.tagGroups.length){
        oElement.isVariantAllowed = true;
      } else {
        oElement.isVariantAllowed = false;
      }
    }
  };

  var _handleClassDefaultOptionClicked = function (sContext) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    if(sContext === "default") {
      if (oActiveClass.isDefaultChild) {
        oActiveClass.isDefaultChild = false;
      } else {
        oActiveClass.isDefaultChild = true;
        oActiveClass.isAbstract = false;
      }
    }
    else if(sContext === "abstract") {
      if (oActiveClass.isAbstract) {
        oActiveClass.isAbstract = false;
      } else {
        oActiveClass.isAbstract = true;
        oActiveClass.isDefaultChild = false;
      }
    }
    _triggerChange();
  };

  var _handleClassRelationshipSingleValueChanged = function (sRelationshipId, sKey, sNewValue,oReferencedKlasses,sType = 'relationships') {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oRelationship = CS.find(oActiveClass[sType], {id: sRelationshipId});
    let oReferencedData = ClassProps.getReferencedClassObjects();
    if (oRelationship) {
      oRelationship[sKey] = sNewValue;
      oRelationship.tabId = CS.isEmpty(sNewValue) ? "" : sNewValue.id;
      CS.assign(oReferencedData.referencedTabs, oReferencedKlasses); }
      if(CS.isObject(sNewValue) && sNewValue.isNewlyCreated){
      //auto save when Custom Tab is created
      _saveClass({});
    }else {
      _triggerChange();
    }
  };

  let _handleClassRelationshipTaxonomyInheritanceChanged = function (sRelationshipId, sKey, sNewValue, sType = 'relationships') {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oRelationship = CS.find(oActiveClass[sType], {id: sRelationshipId});
    if (oRelationship) {
      oRelationship[sKey] = sNewValue;
    }
    _triggerChange();
  };

  var _handleClassRelationshipSide2Changed = function (sRelationshipId, sNewKlassId, oReferencedKlasses) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oOldRelationship = CS.find(oActiveClass.relationships, {id: sRelationshipId});
    let oReferencedClassObjects = ClassProps.getReferencedClassObjects();
    CS.assign(oReferencedClassObjects.referencedKlasses, oReferencedKlasses);

    if (oOldRelationship) {
      oOldRelationship.id = UniqueIdentifierGenerator.generateUUID();
      oOldRelationship.propertyCollection = null;
      oOldRelationship.side2.klassId = sNewKlassId;

      let oNewKlass = !CS.isEmpty(oReferencedKlasses) ? oReferencedKlasses[sNewKlassId] : {};
      if (!CS.isEmpty(oNewKlass)) {
        oOldRelationship.side2.label = oNewKlass.label;
        oOldRelationship.label = _generateRelationshipName(oActiveClass);
        oOldRelationship.side1.relationships = [];
        oOldRelationship.side2.relationships = [];
      }
    }

    if(!CS.isEmpty(sNewKlassId)){
      _saveClass({});
    }else {
      _triggerChange();
    }
  };

  var _generateRelationshipName = function (oActiveClass, oOtherSideKlass) {
    let sNatureTypeId = oActiveClass.natureType;
    let oMockDataForClassNatureTypes = {};
    CS.forEach(MockDataForClassNatureTypes, function (oMockData, sLabel) {
      oMockDataForClassNatureTypes[sLabel] = new oMockData();
    });
    let oNatureTypeMock = CS.find(CS.flatMap(oMockDataForClassNatureTypes), {id: sNatureTypeId});
    let sRelationshipLabel = "";
    if (oNatureTypeMock) {
      if (oOtherSideKlass) {
        sRelationshipLabel = `${oNatureTypeMock.label} - ${oOtherSideKlass.label}`;
      }
      else {
        sRelationshipLabel = `${oNatureTypeMock.label}: ${CS.getLabelOrCode(oActiveClass)}`;
      }

    } else {
      sRelationshipLabel = UniqueIdentifierGenerator.generateUntitledName();
    }
    return sRelationshipLabel;
  };

  var _generateRelationship = function (oActiveClass, sRelationshipType, iMaxNoOfItems, sOtherSideKlass, sSide1Cardinality, sSide2Cardinality) {
    let bIsNatureRelationship = SettingUtils.isNatureTypeRelationship(sRelationshipType);

    /**Info: in case of product variant relationship, we dont want to set self as other side*/
    let sOtherSideClassId = !CS.isEmpty(sOtherSideKlass) ? sOtherSideKlass : oActiveClass.id;
    if(sOtherSideClassId === SettingUtils.getNoIdConstant()){
      sOtherSideClassId = "";
    }

    let oOtherSideKlass = {label: oActiveClass.label};
    let oRelationship = {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: "com.cs.core.config.interactor.entity.relationship.Relationship",
      maxNoOfItems: iMaxNoOfItems || 0,
      relationshipType: sRelationshipType,
      sections: [],
      label: _generateRelationshipName(oActiveClass, oOtherSideKlass)
    };
    oRelationship.side1 = {
      cardinality: sSide1Cardinality || "Many",
      id: UniqueIdentifierGenerator.generateUUID(),
      isVisible: bIsNatureRelationship,
      klassId: oActiveClass.id,
      label: oActiveClass.label
    };
    oRelationship.side2 = {
      cardinality: sSide2Cardinality || "Many",
      id: UniqueIdentifierGenerator.generateUUID(),
      isVisible: false,
      klassId: sOtherSideClassId,
      label: oActiveClass.label
    }

    if (sRelationshipType == RelationshipTypeDictionary.PRODUCT_VARIANT) {
      let aMockDataForTaxonomyInheritance = new MockDataForTaxonomyInheritance();
      oRelationship.taxonomyInheritanceSetting = aMockDataForTaxonomyInheritance[0].id;
    }
    return oRelationship;
  };

  var _addOrRemoveRelationshipsFromClassContext = function (oActiveClass, aSelectedContexts, sRelationshipType, sOtherSideClass, sSide1Cardinality, sSide2Cardinality) {
    let aRelationships = oActiveClass.relationships;
    /**Remove nature relationship context if already exists**/
    CS.remove(aRelationships, {relationshipType :sRelationshipType});

    /**Add nature relationship context**/
    if (!CS.isEmpty(aSelectedContexts) && oActiveClass.isNature) {
      let oProductRelationship = CS.find(aRelationships, {relationshipType: sRelationshipType});
      if (!oProductRelationship) {
        aRelationships.push(_generateRelationship(oActiveClass, sRelationshipType, null, sOtherSideClass, sSide1Cardinality, sSide2Cardinality));
      }
    }
  };

  var _refreshRelationshipsForContexts = function (oActiveClass, sClickedContext) {
    let oContexts = oActiveClass.contexts;
    sClickedContext == "productVariantContexts" && _addOrRemoveRelationshipsFromClassContext(oActiveClass, oContexts.productVariantContexts, RelationshipTypeDictionary.PRODUCT_VARIANT, SettingUtils.getNoIdConstant(), "One", "Many");
  };

  var _handleSelectionToggleButtonClicked = function (sKey, sId, bSingleSelect) {
    if (sKey === "defaultOptions") {
      _handleClassDefaultOptionClicked(sId);
      return;
    }
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let bIsContextToggle = CS.includes(["embeddedVariantContexts", "productVariantContexts", "promotionalVersionContexts"], sKey);
    let sNewKey = sKey;
    if (bIsContextToggle) {
      sNewKey = "contexts." + sKey;
    }
    let aSelectedItems = CS.get(oActiveClass, sNewKey) || [];
    if (CS.includes(aSelectedItems, sId)) {
      CS.remove(aSelectedItems, function (sDataId) {
        return sDataId === sId;
      });
    } else if (bSingleSelect) {
      aSelectedItems = [sId];
    } else {
      aSelectedItems.push(sId);
    }
    CS.set(oActiveClass, sNewKey, aSelectedItems);
    if (bIsContextToggle) {
      _refreshRelationshipsForContexts(oActiveClass, sKey);
    }
    _triggerChange();
  };

  var _handleSectionBlockerClicked = function(sSectionId){
    let oCurrentClass = ClassUtils.getCurrentClass();
    let aSections = oCurrentClass.sections;
    let oSection = CS.find(aSections, {id: sSectionId});

    CustomActionDialogStore.showConfirmDialog(getTranslation().BREAK_INHERITANCE_CONFIRMATION, '',
        function () {
          oSection.isInheritedUI = false;
          oSection.isCutoffUI = true;

          _triggerChange();
        }, function (oEvent) {
        });
  };

  var _refreshSequenceIds = function(aSections){
    CS.each(aSections, function(oSection, iIndex){
      oSection.sequence = iIndex;
    });
  };

  var _handleMetricUnitChanged = function(sSelectedUnit, oSectionElementDetails) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oSection = CS.find(oActiveClass.sections, {id: oSectionElementDetails.sectionId});
    let oElement = CS.find(oSection.elements, {id: oSectionElementDetails.elementId});
    oElement.defaultUnit = sSelectedUnit;
  };

  var _getNatureTypeBasedOnEntityType = function (oActiveClass) {
    if(!CS.isEmpty(oActiveClass.natureType)){
      return oActiveClass.natureType;
    } else {
      switch (oActiveClass.type){
        case MockDataForEntityBaseTypesDictionary.articleKlassBaseType:
          return NatureTypeDictionary.SINGLE_ARTICLE;

        case MockDataForEntityBaseTypesDictionary.assetKlassBaseType:
          return _getNatureTypeBasedOnEntitySecondaryType(oActiveClass.secondaryType);

        case MockDataForEntityBaseTypesDictionary.marketKlassBaseType:
          return NatureTypeDictionary.MARKET;

        case MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType:
          return NatureTypeDictionary.TEXT_ASSET;

        case MockDataForEntityBaseTypesDictionary.supplierKlassBaseType:
          return NatureTypeDictionary.SUPPLIER;

        default:
          return "";

      }
    }
  };

  let _getNatureTypeBasedOnEntitySecondaryType = function (sType) {
    switch (sType) {
      case SecondaryTypeDictionary.ASSET_CLASS:
      case SecondaryTypeDictionary.IMAGE_ASSET:
        return NatureTypeDictionary.IMAGE_ASSET;
      case SecondaryTypeDictionary.VIDEO_ASSET:
        return NatureTypeDictionary.VIDEO_ASSET;
      case SecondaryTypeDictionary.DOCUMENT_ASSET:
        return NatureTypeDictionary.DOCUMENT_ASSET;

    }
  };

  var _getClassTypeContextBasedOnClassType = function (sClassType) {
    let sClassTypeContext = "";
    switch (sClassType) {
      case MockDataForEntityBaseTypesDictionary.articleKlassBaseType:
        sClassTypeContext = "class";
        break;

      case MockDataForEntityBaseTypesDictionary.assetKlassBaseType:
        sClassTypeContext = "asset";
        break;

      case MockDataForEntityBaseTypesDictionary.marketKlassBaseType:
        sClassTypeContext = "target";
        break;

      case MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType:
        sClassTypeContext = "textasset";
        break;

      case MockDataForEntityBaseTypesDictionary.supplierKlassBaseType:
        sClassTypeContext = "supplier";
        break;

      default:
        sClassTypeContext = "";
    }

    return sClassTypeContext;
  };

  var _generateNatureRelationships = function (oActiveClass, sOtherSideKlassId) {
    let sNatureType = oActiveClass.natureType;
    let aTypesToExcludeForRelationship = [NatureTypeDictionary.PID_SKU, NatureTypeDictionary.SINGLE_ARTICLE,
      NatureTypeDictionary.IMAGE_ASSET, NatureTypeDictionary.GTIN,
      NatureTypeDictionary.EMBEDDED, NatureTypeDictionary.LANGUAGE];
    let aClassNatureTypes = SettingUtils.getNatureTypeListBasedOnClassType(oActiveClass.type, oActiveClass.secondaryType);
    let oNatureType = CS.find(aClassNatureTypes, {id: sNatureType}) || {};
    let sNatureTypeKey = CS.findKey(NatureTypeDictionary, function (sValue) {
      return sValue == sNatureType;
    });
    let oRelationship = null;
    if (!CS.includes(aTypesToExcludeForRelationship, sNatureType)) {
      oRelationship = _generateRelationship(oActiveClass, RelationshipTypeDictionary[sNatureTypeKey], oNatureType.maxNoOfItems, sOtherSideKlassId);
    }

    oActiveClass.relationships = oRelationship ? [oRelationship] : [];
  };

  var _handleArticleNatureTypeChanged = function (oActiveClass, sClickedContext) {
    _generateNatureRelationships(oActiveClass);
    oActiveClass.contexts.productVariantContexts = [];
    _refreshRelationshipsForContexts(oActiveClass, sClickedContext);
    _saveClass({});
  };

  let _getDefaulltCouplingToBeAppliedKlassType = function () {
    let oRelAndContextCouplingTypes = new relAndContextCouplingTypes();
    return oRelAndContextCouplingTypes[0].id;
  };

  let _addPropertyInNatureRelationshipSide = (sEntity, sRelationshipId, sSide, aSelectedIds, oReferencedData) => {
    let oScreenProps = SettingUtils.getComponentProps().screen;
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oActiveRelationship = CS.find(oActiveClass.relationships, {"id": sRelationshipId});
    let oSide = oActiveRelationship[sSide];

    let aList = [];
    let sDefaultCouplingTypeToBeApplied = _getDefaulltCouplingToBeAppliedKlassType();
    if (sEntity === ConfigDataEntitiesDictionary.ATTRIBUTES) {
      aList = oSide.attributes;
      CS.assign(oScreenProps.getReferencedAttributes(), oReferencedData);
    } else if (sEntity === ConfigDataEntitiesDictionary.TAGS) {
      aList = oSide.tags;
      CS.assign(oScreenProps.getReferencedTags(), oReferencedData);
    } else if (sEntity === "prices") {
      sDefaultCouplingTypeToBeApplied = "";
      aList = oSide.prices;
      CS.assign(oScreenProps.getReferencedAttributes(), oReferencedData);
    } else if (sEntity === ConfigDataEntitiesDictionary.RELATIONSHIPS) {
      // TODO: Should be handled by _getDefaulltCouplingToBeAppliedKlassType  function
      sDefaultCouplingTypeToBeApplied = CouplingConstants.DYNAMIC_COUPLED;
      aList = oSide.relationships;
      CS.assign(oScreenProps.getReferencedRelationships(), oReferencedData);
    }

    let aNewItems = [];
    CS.forEach(aSelectedIds, function (iItemID) {
      var oProperty = {
        id: iItemID,
        couplingType: sDefaultCouplingTypeToBeApplied
      };
      let oItem = CS.find(aList, {id: iItemID}) || oProperty;
      aNewItems.push(oItem);
    });

    if (sEntity === ConfigDataEntitiesDictionary.ATTRIBUTES) {
      oSide.attributes = aNewItems;
    } else if (sEntity === ConfigDataEntitiesDictionary.TAGS) {
      oSide.tags = aNewItems;
    } else if (sEntity === "prices") {
      oSide.prices = aNewItems;
    } else if (sEntity === ConfigDataEntitiesDictionary.RELATIONSHIPS) {
      oSide[sEntity] = aNewItems;
    }

    _setClassScreenLockedStatus(true)
  };

  var _removePropertyInNatureRelationshipSide = function (sRelationshipId, sSide, iItemID, sContext) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oActiveRelationship = CS.find(oActiveClass.relationships, {"id": sRelationshipId});
    let oSide = oActiveRelationship[sSide];
    CS.remove(oSide[sContext], {id: iItemID});
  };

  var _changeNatureRelationshipPropertyCoupling = function (sRelationshipId, sSide, sPropertyId, sNewValue, sContext) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oActiveRelationship = CS.find(oActiveClass.relationships, {"id": sRelationshipId});
    let oSide = oActiveRelationship[sSide];
    let oItem = CS.find(oSide[sContext], {id: sPropertyId});
    oItem && (oItem.couplingType = sNewValue);
  };

  var _setClassContextId = function(oKlass){
    let oContexts = oKlass.contexts;
    switch (oKlass.natureType) {
      case NatureTypeDictionary.EMBEDDED:
      case NatureTypeDictionary.TECHNICAL_IMAGE: //since, behaviour for embedded & technical image is same
        let aEmbeddedVariantContexts = oContexts.embeddedVariantContexts;
        ClassProps.setClassContextId(aEmbeddedVariantContexts[0]); //it is assumed that there is only one embedded klass context
        break;
    }

  };

  var _genrateADMForClassContextTagCombinations = function(oADM, oOldContext, oClassContextDialogProps){
    let aOldUniqueTagSelection = oOldContext.uniqueSelectors;
    let aNewUniqueTagSelection = oClassContextDialogProps.uniqueSelectors;
    oADM.addedUniqueSelections = [];
    oADM.deletedUniqueSelections = [];

    CS.forEach(aNewUniqueTagSelection, function (oUniqueTagSelection) {
      let oFound = CS.find(aOldUniqueTagSelection, {id: oUniqueTagSelection.id});
      if(!oFound) {
        oADM.addedUniqueSelections.push(oUniqueTagSelection);
      }else if(SettingUtils.isUniqueSelectorModifiedForCombination(oFound,oUniqueTagSelection)){
        oUniqueTagSelection.id = UniqueIdentifierGenerator.generateUUID();
        oADM.addedUniqueSelections.push(oUniqueTagSelection);
      }
    });

    CS.forEach(aOldUniqueTagSelection, function (oOldUniqueTagSelection) {
      let oFound = CS.find(aNewUniqueTagSelection, {id: oOldUniqueTagSelection.id});
      if(!oFound) {
        oADM.deletedUniqueSelections.push(oOldUniqueTagSelection.id);
      }
    });
  };

  var _generateADMForClassContextTags = function(oADM, oOldContext, oClassContextDialogProps){
    let aOldContextTags = oOldContext.contextTags;
    let aNewContextTags = oClassContextDialogProps.contextTags;

    CS.forEach(aNewContextTags, function (oNewContextTag) {

      let oActiveContextADM = {
        tagId: '',
        addedTagValueIds: [],
        deletedTagValueIds: []
      };
      let oOldContextTag = CS.find(aOldContextTags, {tagId: oNewContextTag.tagId});
      if(CS.isEmpty(oOldContextTag)) {
        let oAddedTags = {};
        oAddedTags.tagId = oNewContextTag.tagId;
        let oSelectedTagValues = [];
        CS.forEach(oNewContextTag.tagValues, function (oTagValue) {
          oTagValue.isSelected && oSelectedTagValues.push(oTagValue.tagValueId);
        });
        oAddedTags.tagValueIds = oSelectedTagValues;

        oADM.addedTags.push(oAddedTags);
      } else {
        let bIsModifiedFlag = false;
        CS.forEach(oOldContextTag.tagValues, function (oOldTagValue) {
          let oNewTagValue = CS.find(oNewContextTag.tagValues, {tagValueId: oOldTagValue.tagValueId});
          if(oOldTagValue.isSelected) {
            if(!oNewTagValue.isSelected) {
              oActiveContextADM.deletedTagValueIds.push(oNewTagValue.tagValueId);
              bIsModifiedFlag = true;
            }
          } else {
            if(oNewTagValue.isSelected) {
              oActiveContextADM.addedTagValueIds.push(oNewTagValue.tagValueId);
              bIsModifiedFlag = true;
            }
          }
        });
        if(bIsModifiedFlag) {
          oActiveContextADM.tagId = oNewContextTag.tagId;
          oADM.modifiedTags.push(oActiveContextADM)
        }
      }
    });

    CS.forEach(aOldContextTags, function (oOldContextTags) {
      let oNewContextTags = CS.find(aNewContextTags, {tagId: oOldContextTags.tagId});
      if (CS.isEmpty(oNewContextTags)) {
        oADM.deletedTags.push(oOldContextTags.tagId);
      }
    });
  };

  let _checkSelectedTagCombinations = function() {
    let oClassContextDialogProps = ClassProps.getClassContextDialogProps();
    let aUniqueSelectors = oClassContextDialogProps.uniqueSelectors;
    let bIsInValid = false;
    CS.forEach(aUniqueSelectors, function(oUniqueSelector){
        let aSelectionValues = oUniqueSelector.selectionValues;
        CS.forEach(aSelectionValues, function(oSelectionValue){
          if(CS.isEmpty(oSelectionValue.tagValues)){
            bIsInValid = true;
          }
        });
    });
    return bIsInValid;
   };

  var _saveClassContext = function(){
    let oClassContextDialogProps = ClassProps.getClassContextDialogProps();

    if(oClassContextDialogProps.type === ContextTypeDictionary.IMAGE_VARIANT && _checkSelectedTagCombinations()){
      alertify.error(getTranslation().SELECT_BOTH_IMAGE_EXTENSION_AND_RESOLUTION);
      return;
    }
    if(SettingUtils.checkUniqueSelectorsDuplications(oClassContextDialogProps)){
      alertify.error(getTranslation().DUPLICATE_COMBINATIONS_SELECTED);
      return;
    }

    if (oClassContextDialogProps.type === ContextTypeDictionary.LANGUAGE_VARIANT) {
      if (CS.isEmpty(oClassContextDialogProps.contextTags)) {
        alertify.message(getTranslation().SELECT_AT_LEAST_ONE_TAG);
        return;
      } else if (CS.isEmpty(CS.filter(oClassContextDialogProps.contextTags[0].tagValues, {isSelected: true}))) {
        alertify.message(getTranslation().SELECT_AT_LEAST_ONE_TAG_VALUE);
        return;
      }
    }

    let oOldContext = ClassProps.getClassContext();
    if(!oClassContextDialogProps.id){
      return;
    }
    let bisContextEqual = CS.isEqual(oClassContextDialogProps, oOldContext);
    if (bisContextEqual) {
      //_alertifyMessage(getTranslation().NOTHING_CHANGED_TO_SAVE);
      ClassProps.setClassContextDialogProps({});
      _triggerChange();
      return;
    }

    if (oClassContextDialogProps.type === "imageVariant") {
      var aTagGroups = CS.filter(oClassContextDialogProps.contextTags, function (oContextTag) {
        return !CS.isEmpty(CS.filter(oContextTag.tagValues, {isSelected: true}));
      });

      if (CS.isEmpty(aTagGroups)) {
        CustomActionDialogStore.showConfirmDialog(getTranslation().SAVE_CONTEXT_WITHOUT_TAG_VALUES, '',
          function () {
            _continueSavingClassContext();
          }, function (oEvent) {

          });
      } else {
        _continueSavingClassContext();
      }
    } else {
      _continueSavingClassContext();
    }
  };

  var _handleClassContextUploadIconChangeEvent = function (sIconKey, sIconObjectKey) {
    let oContextData = _makeContextDirty();
    oContextData.icon = sIconKey;
    oContextData.iconKey = sIconObjectKey;
    oContextData.showSelectIconDialog = false;
  };

  var removeEmptyUniqueSelectors = function () {
    let oClassContextDialogProps = ClassProps.getClassContextDialogProps();
    let aUniqueSelectors = oClassContextDialogProps.uniqueSelectors;
    CS.remove(aUniqueSelectors, function (oSelector) {
      let aSelectionValues = oSelector.selectionValues;
      let aValidTagGroups = CS.filter(aSelectionValues, function (oSelectedTag) {
           return !CS.isEmpty(oSelectedTag.tagValues);
      });
      return CS.isEmpty(aValidTagGroups);
    });
  };

  var _continueSavingClassContext = function () {
      let oClassContextDialogProps = ClassProps.getClassContextDialogProps();
      let oOldContext = ClassProps.getClassContext();
      let aNewEntities = oClassContextDialogProps.entities || [];
      let aOldEntities = oOldContext.entities || [];
      removeEmptyUniqueSelectors();
      let oADM = {
          id: oClassContextDialogProps.id,
          label: oClassContextDialogProps.label,
          type: oClassContextDialogProps.type,
          isLimitedObject: oClassContextDialogProps.isLimitedObject,
          isTimeEnabled: oClassContextDialogProps.isTimeEnabled,
          isAutoCreate: oClassContextDialogProps.isAutoCreate,
          isDuplicateVariantAllowed: oClassContextDialogProps.isDuplicateVariantAllowed,
          defaultTimeRange: oClassContextDialogProps.defaultTimeRange,
          uniqueSelectors: oClassContextDialogProps.uniqueSelectors,
          addedTags: [],
          modifiedTags: [],
          deletedTags: [],
          addedEntities: CS.difference(aNewEntities, aOldEntities),
          deletedEntities: CS.difference(aOldEntities, aNewEntities),
          icon: oClassContextDialogProps.icon
      };
      // SettingUtils.removeEmptyTagGroupFromCurrentContext(oClassContextDialogProps);
      _generateADMForClassContextTags(oADM, oOldContext, oClassContextDialogProps);
      _genrateADMForClassContextTagCombinations(oADM, oOldContext, oClassContextDialogProps);
    let oCustomTabsADM = SettingUtils.generateADMForCustomTabs(oOldContext.tab, oClassContextDialogProps.tab);
    oADM.addedTab = oCustomTabsADM.addedTab;
    oADM.deletedTab = oCustomTabsADM.deletedTab;

    SettingUtils.csPostRequest(oContextRequestMapping.save, {}, oADM, successSaveClassContextCallback, failureGetCreateSaveClassContextCallback);
  };

  var successSaveClassContextCallback = function (oResponse) {
    let oContextFromServer = oResponse.success;
    SettingUtils.preProcessContextData(oContextFromServer);
    let oClassContextDialogProps = ClassProps.getClassContextDialogProps();
    delete oClassContextDialogProps.isDirty;
    CS.assign(oClassContextDialogProps, oContextFromServer);
    ClassProps.setClassContext(CS.cloneDeep(oClassContextDialogProps));
    let oReferencedContexts = ClassProps.getReferencedContexts();
    CS.assign(oReferencedContexts[oContextFromServer.id], oContextFromServer);
    _alertifySuccess(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().CONTEXT } ));
    _triggerChange();
  };

  var _handleClassContextDialogClosed = function(){
    ClassProps.setClassContextDialogProps({});
  };

  var _handleClassContextDialogOkClicked = function(){
    _saveClassContext();
  };

  let _handleClassContextDialogDiscardClicked = function (bShowDiscardMessage =true) {
    let oClassContext = CS.cloneDeep(ClassProps.getClassContext());
    _processViewPropertiesForClassContextDialog(oClassContext);
    ClassProps.setClassContextDialogProps(oClassContext);
    if(bShowDiscardMessage) {
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    }
    _triggerChange();
  };

  var failureGetCreateSaveClassContextCallback = function(oResponse){
    if (CS.isNotEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "ContextConfigurationDependencyException"}))) {
      _handleClassContextDialogDiscardClicked(false);
    };
    SettingUtils.failureCallback(oResponse, "failureGetCreateSaveClassContextCallback", getTranslation());
  };

  var successCreateClassContextCallback = function (oResponse) {
    let oContextFromServer = oResponse.success;
    let oClassContextDialogProps = ClassProps.getClassContextDialogProps();
    let oReferencedContexts = ClassProps.getReferencedContexts();
    ClassProps.setClassContext(CS.cloneDeep(oContextFromServer));
    ClassProps.setClassContextId(oContextFromServer.id);
    oReferencedContexts[oContextFromServer.id] = oContextFromServer.label;
    CS.assign(oClassContextDialogProps, oContextFromServer);
    oClassContextDialogProps.isDialogOpen = true;
    _triggerChange();
  };

  var successGetClassContextCallback = function (oResponse) {
    let oContextFromServer = oResponse.success;
    SettingUtils.preProcessContextData(oContextFromServer);
    ClassProps.setReferencedTags(oContextFromServer.configDetails.referencedTags);
    let oClassContextDialogProps = ClassProps.getClassContextDialogProps();
    CS.assign(oClassContextDialogProps, oContextFromServer);
    oClassContextDialogProps.isDialogOpen = true;
    ClassProps.setClassContext(CS.cloneDeep(oClassContextDialogProps));
    _processViewPropertiesForClassContextDialog(oClassContextDialogProps);
    _triggerChange();
  };

  let _processViewPropertiesForClassContextDialog = function (oClassContextDialogProps) {
    let oActiveClass = ClassUtils.getCurrentClass();
    let oNatureTypeRelationshipToCheckMap = {};
    if(oActiveClass.natureType == NatureTypeDictionary.TECHNICAL_IMAGE){
      oClassContextDialogProps.disabledSections = ["tab","isTimeEnabled"];
    }
    let sKeyForRelationshipTypeToCheck = oNatureTypeRelationshipToCheckMap[oClassContextDialogProps.type];
    if (!CS.isEmpty(sKeyForRelationshipTypeToCheck) && CS.some(ClassProps.getActiveClass().relationships, {relationshipType: sKeyForRelationshipTypeToCheck})) {
      oClassContextDialogProps.allSectionsDisabled = true;
    }
  };

  var _getClassContext = function (sSelectedContextId) {

    SettingUtils.csGetRequest(oContextRequestMapping.get, {id: sSelectedContextId}, successGetClassContextCallback, failureGetCreateSaveClassContextCallback);
  };

  var _setContextToCreateType = function (oContextToCreate,sNatureType) {
    switch(sNatureType){
      case NatureTypeDictionary.EMBEDDED:
        oContextToCreate.type = ContextTypeDictionary.CONTEXTUAL_VARIANT;
        break;
    }
  };

  var _createClassContext = function (oClassContextDialogProps) {
    let oActiveClass = ClassUtils.getCurrentClass();
    let oContextToCreate = CS.cloneDeep(oClassContextDialogProps);
    delete oContextToCreate.isDialogOpen;
    _setContextToCreateType(oContextToCreate);
    oContextToCreate.id = UniqueIdentifierGenerator.generateUUID();
    oContextToCreate.label = oActiveClass.label + " " + getTranslation().CLASS_CONTEXT;
    oContextToCreate.klassId = oActiveClass.id;

    SettingUtils.csPutRequest(oContextRequestMapping.create, {}, oContextToCreate, successCreateClassContextCallback, failureGetCreateSaveClassContextCallback);
  };

  var _handleClassContextDialogOpenClicked = function(){
    let oClassContextDialogProps = ClassProps.getClassContextDialogProps();
    let sClassContextId = ClassProps.getClassContextId();

    if(CS.isEmpty(sClassContextId)){
      _createClassContext(oClassContextDialogProps);
    }else{
      _getClassContext(sClassContextId);
    }

  };

  var _handleRemoveSelectedTagGroupClicked = function (sTagGroupId) {
    let oContextData = _makeContextDirty();
    ContextUtils.handleRemoveSelectedTagGroupClicked(sTagGroupId, oContextData)
  };

  var _handleClassContextDialogAddTagGroup = function(sTagGroupId,sContext){
    let oContextData = _makeContextDirty();
    ContextUtils.handleContextAddTagClicked(sTagGroupId, oContextData, {functionToExecute: _triggerChange});
  };

  var _handleClassContextDialogAddOrRemoveTagValue = function(sTagGroupId, aCheckedItems){
    let oContextData = _makeContextDirty();
    ContextUtils.handleContextAddOrRemoveTagValue(sTagGroupId, aCheckedItems, oContextData);
  };

  var _handleClassContextDialogSelectionToggleClicked = function (sKey, sValue) {
    let oContextData = _makeContextDirty();
    let aValues = oContextData[sKey];
    let sRemovedValue = CS.remove(aValues, function (sVal) {
      return sValue == sVal;
    });

    if(CS.isEmpty(sRemovedValue)){
      aValues.push(sValue);
    }
  };

  let _makeContextDirty = function () {
    let oContextData = ClassProps.getClassContextDialogProps();
    oContextData.isDirty = true;
    return oContextData;
  };

  var _handleClassContextDialogPropertyChanged = function(sKey, sVal){
    let oContextData = _makeContextDirty();
    oContextData[sKey] = sVal;
  };

  var _addToEmbeddedKlassIds = function(aCheckedItems){
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    oActiveClass.embeddedKlassIds = aCheckedItems;
  };

  var _addToTechnicalImageKlassIds= function (aCheckedItems) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    oActiveClass.technicalImageKlassIds = aCheckedItems;
  };

  var _removeEmbeddedKlassId = function(sId){
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let aEmbeddedKlassIds = oActiveClass.embeddedKlassIds;
   CS.remove(aEmbeddedKlassIds, function(sEmbeddedKlassId){
     return sId === sEmbeddedKlassId;
   });
  };

  var _removeTechnicalImageKlassId = function(sId){
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let aTechnicalImageKlassIds = oActiveClass.technicalImageKlassIds;
    CS.remove(aTechnicalImageKlassIds, function(sTechnicalImageKlassIds){
      return sId === sTechnicalImageKlassIds;
    });
  };

  var _handleContextCombinationDeleteTagValue = function (sSelectorId, sTagId, sId) {
    let oClassContextProps = _makeContextDirty();
    SettingUtils.deleteTagValueFromCombination(oClassContextProps, sSelectorId, sTagId, sId);
    _triggerChange();
  };

  var _handleClassContextCombinationEditClicked = function (sUniqueSelectionId) {
    let oClassContextProps = _makeContextDirty();
    ClassProps.setActiveTagUniqueSelectionId(sUniqueSelectionId);
    SettingUtils.updateUniqueSelectionOrder(oClassContextProps, sUniqueSelectionId);
    _triggerChange();
  };

  var _handleClassContextCombinationMSSPopOver = function(sSelectorId, sTagId, aSelectedItems){
    let oClassContextProps = _makeContextDirty();
    SettingUtils.setTagValueDataInTagCombination(oClassContextProps, sSelectorId, sTagId, aSelectedItems);
    _triggerChange();
  };

  var _handleAddNewTagCombination = function(){
    let oClassContextProps = _makeContextDirty();
    let sActiveTagUniqueSelectionId = SettingUtils.addNewTagCombination(oClassContextProps);
    if(!CS.isEmpty(sActiveTagUniqueSelectionId)){
      ClassProps.setActiveTagUniqueSelectionId(sActiveTagUniqueSelectionId);
    }
    _triggerChange();
  };

  var _handleTagCombinationSelected = function(){
    let oClassContextProps = _makeContextDirty();
    SettingUtils.removeEmptyTagSelections(oClassContextProps);
    ClassProps.setActiveTagUniqueSelectionId("");
    _triggerChange();
  };

  var _handleDeleteNewTagCombination = function (sUniqueSelectionId) {
    let oClassContextProps = _makeContextDirty();
    SettingUtils.deleteTagCombination(sUniqueSelectionId, oClassContextProps);
    _triggerChange();
  };

  let _assignReferencedItems = function (oConfigDetails) {
    let oReferencedObject = ClassProps.getReferencedClassObjects();
    CS.assign(oReferencedObject, oConfigDetails);
    ClassProps.setReferencedClassObjects(oReferencedObject);
  };

  let _handleSectionToggleButtonClicked = function (sSectionId) {
    let oActiveClass = ClassUtils.getActiveClass();
    let oCallBackData = {
      functionToExecute: _triggerChange,
      preFunctionToExecute: _assignReferencedItems
    };

    let sURL = oClassRequestMapping.GetClassPropertyCollection;
    SettingUtils.handleSectionToggleButtonClicked(sSectionId, oActiveClass, sURL, oCallBackData);
  };

  /* Handle Collapsed button clicked in Export Relationship */
  let _handleSide2RelationshipSectionCollapseButtonClicked = function(sContext){
    let oActiveClass = ClassUtils.getActiveClass();
    let sUrl = oClassRequestMapping.GetSectionInfoForRelationshipExport;
    let oPostData = {};
    oPostData.entityType = (sContext === ExportSide2RelationshipDictionary.RELATIONSHIP_LIST) ? ConfigDataEntitiesDictionary.RELATIONSHIPS : "properties";
    oPostData.klassId = oActiveClass.id;
    /* To set Collapse */
    let oSide2CollapseEnabled = ClassProps.getSide2RelationshipCollapseEnabled();
    oSide2CollapseEnabled[sContext] = !oSide2CollapseEnabled[sContext];
    ClassProps.setSide2RelationshipCollapseEnabled(oSide2CollapseEnabled);
    if(!oSide2CollapseEnabled[sContext]){
      SettingUtils.csPostRequest(sUrl,{}, oPostData, successExportRelationshipCallback.bind(this, sContext) , failureExportRelationshipCallback);
    }else{
      _triggerChange();
    }
  };

  let successExportRelationshipCallback = function(sContext, oResponse) {
    let aRelationshipExportListFromServer = oResponse.success;
    let oActiveClass = ClassUtils.getActiveClass();
    _generateRelationshipExportData(sContext, aRelationshipExportListFromServer, oActiveClass);
    _triggerChange();
  };

  let failureExportRelationshipCallback = function(oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportRelationshipCallback", getTranslation());
  };

  /* Funtion to set the selected data of Export Side 2 Relationships in ActiveClass */
  let _generateRelationshipExportData = function(sContext, aRelationshipExportListFromServer, oActiveClass){
    let oRelationshipExportData = oActiveClass.relationshipExport || {
      attributes: [],
      tags:[],
      relationships:[],
      referencedData : {
        referencedAttributes: {},
        referencedTags: {},
        referencedRelationships: {},
      }
    };


    switch (sContext) {
      case ExportSide2RelationshipDictionary.RELATIONSHIP_LIST:
        let aListRelationships = CS.map(aRelationshipExportListFromServer[ConfigDataEntitiesDictionary.RELATIONSHIPS], 'id');
        let oReferencedDataForRelationships = CS.keyBy(aRelationshipExportListFromServer[ConfigDataEntitiesDictionary.RELATIONSHIPS], 'id');
        oRelationshipExportData[ConfigDataEntitiesDictionary.RELATIONSHIPS] = aListRelationships;
        oRelationshipExportData.referencedData.referencedRelationships = oReferencedDataForRelationships;
        break;

      case ExportSide2RelationshipDictionary.PROPERTIES_LIST:
        let aListAttributes = CS.map(aRelationshipExportListFromServer[ConfigDataEntitiesDictionary.ATTRIBUTES], 'id');
        let aListTags = CS.map(aRelationshipExportListFromServer[ConfigDataEntitiesDictionary.TAGS], 'id');
        let oReferencedDataForAttributes = CS.keyBy(aRelationshipExportListFromServer[ConfigDataEntitiesDictionary.ATTRIBUTES], 'id');
        let oReferencedDataForTags = CS.keyBy(aRelationshipExportListFromServer[ConfigDataEntitiesDictionary.TAGS], 'id');
        oRelationshipExportData[ConfigDataEntitiesDictionary.ATTRIBUTES] = aListAttributes;
        oRelationshipExportData[ConfigDataEntitiesDictionary.TAGS] = aListTags;
        oRelationshipExportData.referencedData.referencedAttributes = oReferencedDataForAttributes;
        oRelationshipExportData.referencedData.referencedTags = oReferencedDataForTags;
        break;
    }
    oActiveClass.relationshipExport = oRelationshipExportData;
  };

  let _getIsValidFileTypes = function (oFile) {
    // var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  let _getImportExportRequestDataForClass = function (sBaseType,oImportExportData, data) {
    let sExportURL = "";
    let sImportURL = "";
    switch(sBaseType){
      case MockDataForEntityBaseTypesDictionary.articleKlassBaseType :
        sImportURL = oImportExportData.sUrl;
        sExportURL = oImportExportData.sUrl;
        if(CS.isNotEmpty(oImportExportData.oPostRequest)) {
          oImportExportData.oPostRequest.configType = oDataModelImportExportEntityTypeConstants.ARTICLE;
        }
        else{
          data.append("importType","entity");
          data.append("entityType", oDataModelImportExportEntityTypeConstants.ARTICLE);
        }
        break;
      case MockDataForEntityBaseTypesDictionary.assetKlassBaseType:
        sImportURL = oImportExportData.sUrl;
        sExportURL = oImportExportData.sUrl;
        if(CS.isNotEmpty(oImportExportData.oPostRequest)) {
          oImportExportData.oPostRequest.configType = oDataModelImportExportEntityTypeConstants.ASSET;
        }
        else{
          data.append("importType","entity");
          data.append("entityType", oDataModelImportExportEntityTypeConstants.ASSET);
        }
        break;
      case MockDataForEntityBaseTypesDictionary.marketKlassBaseType:
        sImportURL = oImportExportData.sUrl;
        sExportURL = oTargetRequestMapping.ExportTarget;
        break;
      case MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType:
        sImportURL = oImportExportData.sUrl;
        sExportURL = oImportExportData.sUrl;
        if(CS.isNotEmpty(oImportExportData.oPostRequest)) {
          oImportExportData.oPostRequest.configType = oDataModelImportExportEntityTypeConstants.TEXTASSET;
        }
        else{
          data.append("importType","entity");
          data.append("entityType", oDataModelImportExportEntityTypeConstants.TEXTASSET);
        }
        break;
      case MockDataForEntityBaseTypesDictionary.supplierKlassBaseType:
        sImportURL = oImportExportData.sUrl;
        sExportURL = oImportExportData.sUrl;
        if(CS.isNotEmpty(oImportExportData.oPostRequest)) {
          oImportExportData.oPostRequest.configType = oDataModelImportExportEntityTypeConstants.SUPPLIER;
        }
        else{
          data.append("importType","entity");
          data.append("entityType", oDataModelImportExportEntityTypeConstants.SUPPLIER);
        }
        break;
    }
    return {
      importURL : sImportURL,
      exportURL: sExportURL,
      exportData : oImportExportData,
      importData: data,
    }
  };

  let uploadFileImport = function (data, oCallback, oImportExcel) {
    let sContext = ClassProps.getSelectedClassCategory();
    let oHierarchyTree = SettingUtils.getAppData().getClassListByTypeGeneric(sContext);
    let oRequestData = _getImportExportRequestDataForClass(oHierarchyTree[0].type, oImportExcel, data);
    return SettingUtils.csCustomPostRequest( oRequestData.importURL, oRequestData.importData,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  let successUploadFileImport = function () {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
    return true;
  };

  let failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
    return false;
  };

  let _handleClassConfigImportButtonClicked = function (aFiles, oImportExcel) {
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
              uploadFileImport(data, {}, oImportExcel);
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsAnyInvalidImage = true;
        }
      });

    }
  };

  let _handleClassConfigExportButtonClicked = function (oSelectiveExport) {
    let sContext = ClassProps.getSelectedClassCategory();
    let oHierarchyTree = SettingUtils.getAppData().getClassListByTypeGeneric(sContext);
    let oRequestData = _getImportExportRequestDataForClass(oHierarchyTree[0].type,oSelectiveExport);
    return SettingUtils.csPostRequest(oRequestData.exportURL, {}, oRequestData.exportData.oPostRequest, successExportFile, failureExportFile);
  };

  let successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
    return true;
  };

  let failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
    return false;
  };

  let _validateAndSaveAssetConfigurationChanges = function(){
    let oADMClass = ClassUtils.getCurrentClass();
    if (CS.find(oADMClass.extensionConfiguration, {"extension": ""}) || (CS.isNotEmpty(oADMClass.currentAssetUploadConfigurationModel) && CS.isEmpty(oADMClass.currentAssetUploadConfigurationModel.extension))) {
      alertify.message(getTranslation().ENTER_EXTENSION);
      return;
    }

    oADMClass.currentAssetUploadConfigurationModel.extension = CS.trim(oADMClass.currentAssetUploadConfigurationModel.extension);
    _saveClass({});

  };

  let _handleAssetConfigurationDialogButtonClicked = function (sId) {
    switch (sId) {
      case "create":
        _validateAndSaveAssetConfigurationChanges();
        break;

      case "cancel":
        let oActiveClass = ClassUtils.getActiveClass();
        delete oActiveClass.currentAssetUploadConfigurationModel;
        if (oActiveClass.clonedObject) {
          _discardUnsavedClass({});
        } else {
          _triggerChange();
        }
        break;
    }
  };

  let _handleClassManageEntityButtonClicked = function () {
    let sType = SettingScreenModuleDictionary.CLASS;
    let oSelectedIds = ClassProps.getActiveClass();
    let aSelectedIds = oSelectedIds.id;

    if (CS.isEmpty(aSelectedIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return
    }
    else {
      ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedIds, sType);
    }
  };

  let  deleteClassConfirm= function (sClickedNodeId, oCallback) {
    //let bCanDeleteEntity = ManageEntityConfigProps.getDataForDeleteEntity();
    let bIsClassScreenLocked = ClassProps.getClassScreenLockedStatus();
    if(bIsClassScreenLocked) {
      CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveClass.bind(this, {}, false),
          _discardUnsavedClass.bind(this, {}),
          function () {
          });
    } else {
      _deleteClasses(sClickedNodeId, oCallback);
    }
  }

  /** Function to handle Class Relationship Toggle button Value Changed **/
  let _handleClassRelationshipToggleButtonValueChanged = function (sRelationshipId, sKey, sNewValue) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let sType = 'relationships';
    let oRelationship = CS.find(oActiveClass[sType], {id: sRelationshipId});
    if(!CS.isEmpty(oRelationship)){
      oRelationship[sKey] = sNewValue;
    }
    _triggerChange();
  };

  /* Function to handle Export Side 2 Relationships Apply button clicked for respective context */
  let _handleSectionListApplyButtonClicked = function (sContext, aSelectedItems, oReferencedDataOfSelectedItems) {
    if (CS.isNotEmpty(aSelectedItems)) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let oRelationshipExport = oActiveClass.relationshipExport;
      let oReferencedData = oRelationshipExport.referencedData || {};
      switch (sContext) {
        case ConfigDataEntitiesDictionary.RELATIONSHIPS :
          oRelationshipExport.relationships = aSelectedItems;
          CS.assign(oReferencedData.referencedRelationships, oReferencedDataOfSelectedItems);
          break;
        case ConfigDataEntitiesDictionary.ATTRIBUTES :
          if (aSelectedItems.length <= 10) {
            oRelationshipExport.attributes = aSelectedItems;
            CS.assign(oReferencedData.referencedAttributes, oReferencedDataOfSelectedItems);
          } else {
            alertify.error(getTranslation().MAX_10_PROPERTIES_CAN_BE_ADDED);
          }
          break;
        case ConfigDataEntitiesDictionary.TAGS :
          if (aSelectedItems.length <= 10) {
            oRelationshipExport.tags = aSelectedItems;
            CS.assign(oReferencedData.referencedTags, oReferencedDataOfSelectedItems);
          } else {
            alertify.error(getTranslation().MAX_10_PROPERTIES_CAN_BE_ADDED);
          }
          break;
      }
      _triggerChange();
    }
  };

  /* Function to handle Export Side 2 Relationships Delete button clicked for respective context */
  let _handleSectionListDeleteButtonClicked = function (sSelectedElement, sContext) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let oRelationshipExport = oActiveClass.relationshipExport;
    let oReferenceData = oRelationshipExport.referencedData || {};
    let iIndex = 0;
    switch(sContext){
      case ConfigDataEntitiesDictionary.RELATIONSHIPS :
        let aRelationshipList = oRelationshipExport.relationships;
        iIndex = aRelationshipList.indexOf(sSelectedElement);
        aRelationshipList.splice(iIndex, 1);
        delete oReferenceData.referencedRelationships[sSelectedElement];
        break;

      case ConfigDataEntitiesDictionary.ATTRIBUTES :
        let aAttributeList = oRelationshipExport.attributes;
        iIndex = aAttributeList.indexOf(sSelectedElement);
        aAttributeList.splice(iIndex, 1);
        delete oReferenceData.referencedAttributes[sSelectedElement];
        break;

      case ConfigDataEntitiesDictionary.TAGS :
        let aTagList =  oRelationshipExport.tags;
        iIndex = aTagList.indexOf(sSelectedElement);
        aTagList.splice(iIndex, 1);
        delete oReferenceData.referencedTags[sSelectedElement];
        break;
    }

    _triggerChange();
  };

  let _deleteExtensionAfterConfirmation = function (oRow) {
    let oActiveClass = ClassUtils.makeActiveClassDirty();
    let aExtensionConfiguration = oActiveClass.extensionConfiguration;
    CS.remove(aExtensionConfiguration, {'id': oRow.id});
    _saveClass({});
  };

  return {

    getActiveClass: function () {
      return ClassUtils.getActiveClass();
    },

    getClassScreenLockedStatus: function () {
      return _getClassScreenLockedStatus();
    },

    fetchClassesList: function () {
      _fetchClassesList();
    },

    fetchTextAssetClasses: function () {
      _fetchTextAssetList();
    },

    fetchSupplierClasses: function () {
      _fetchSupplierList();
    },

    fetchContentClasses: function () {
      return _fetchClassesList();
    },

    fetchTaskClasses: function () {
      _fetchTasksList();
    },

    fetchAssetClasses: function () {
      _fetchAssetsList();
    },

    fetchTargetClasses: function () {
      _fetchTargetList();
    },

    fetchClassTree: function () {
      _fetchClassTree();
    },

    saveClass: function (oCallback) {
      let oActiveClass = ClassUtils.getActiveClass();
      if(!oActiveClass.isCreated) {
        _saveClass(oCallback);
        }
    },

    saveUnsavedClass: function (oSwitchCallback, sContext) {
      _saveUnsavedClass(oSwitchCallback, sContext);
    },

    fetchClassDetails: function (oClassValue) {
      let sContext = ClassProps.getSelectedClassCategory();
      return _fetchClassDetails(oClassValue, sContext);
    },

    createClass: function (sNodeId) {
      let oSelectedValueWithContext = _getSelectedValueAndContext(sNodeId);
      let oSelectedValue = oSelectedValueWithContext.selectedValue; //CS.find(oClassListView, 'isSelected');
      let bIsClassScreenLocked = _getClassScreenLockedStatus();
      let sContext = oSelectedValueWithContext.context;
      if (bIsClassScreenLocked) {
        let oCallbackData = {};
        if(sContext === "task"){
          oCallbackData.functionToExecute = _createNewTask.bind(this, sContext, oSelectedValue);
        }
        oCallbackData.functionToExecute = _createNewClass.bind(this, sContext, oSelectedValue);
        CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            _saveUnsavedClass.bind(this, oCallbackData, sContext),
            _discardUnsavedClass.bind(this, oCallbackData, sContext),
            function () {
            }
        );
      } else {
        _createNewClass(sContext, oSelectedValue);
        _triggerChange();
      }
    },

    discardUnsavedClass: function (oCallback, sContext) {
      _discardUnsavedClass(oCallback, sContext);
    },

    deleteClass: function (sClickedNodeId) {
      let sType = SettingScreenModuleDictionary.CLASS;
      // let oCallback = {functionToExecute: deleteClassConfirm.bind(this, sClickedNodeId)}
      // ManageEntityStore.handleManageEntityDialogOpenButtonClicked(sClickedNodeId, sType, oCallback);
      deleteClassConfirm(sClickedNodeId, { functionToExecute: ManageEntityStore.handleManageEntityDialogOpenButtonClicked.bind(this, sClickedNodeId, sType) });
    },

    handleClassNodeRefreshMenuClicked: function () {
      let sSelectedClassCategory = ClassProps.getSelectedClassCategory();
      let oActiveClass = ClassUtils.getActiveClass();
      let oValuesList = _getClassValueListByTypeGeneric(sSelectedClassCategory);

      if (!CS.isEmpty(oActiveClass)) {
        oValuesList[oActiveClass.id].isChildLoaded = false;
        oActiveClass.children = [];
        return _fetchClassDetails(oActiveClass, sSelectedClassCategory, true);

      } else {
        _fetchClassesByTypeGeneric();
      }
    },

    handleSectionAdded: function (aSectionIds) {
      let oContentClass = ClassUtils.getActiveClass();
      if (CS.isEmpty(oContentClass.label.trim())) {
        alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
        return;
      }
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      SettingUtils.handleSectionAdded(oActiveClass, aSectionIds);
      _saveClass({});
    },

    handleSectionAddedToRelationship: function (sRelationshipId, aSectionIds) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      CS.forEach(aSectionIds, function (sSectionId) {
        let oRelationship = CS.find(oActiveClass.relationships, {id: sRelationshipId});
        if (oRelationship) {
          oRelationship.propertyCollection = sSectionId;
          _saveClass({});
        }
      });
    },

    handleSectionDeletedFromRelationship: function (sRelationshipId, sSectionId) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let oRelationship = CS.find(oActiveClass.relationships, {id: sRelationshipId});
      if (oRelationship) {
        oRelationship.propertyCollection = null;
        _saveClass({});
      }
    },

    handleDeleteVisualElementIconClicked: function (oInfo) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      if(oActiveClass.type == MockDataForEntityBaseTypesDictionary.taskKlassBaseType) {
        _handleDeleteVisualElementFromTask(oActiveClass, oInfo); // eslint-disable-line
      } else if(oActiveClass.type == MockDataForEntityBaseTypesDictionary.assetKlassBaseType) {
        _handleDeleteVisualElementFromAsset(oActiveClass, oInfo); // eslint-disable-line
      } else {
        _handleDeleteVisualElementFromClass(oActiveClass, oInfo); // eslint-disable-line
      }
      ClassUtils.setClassScreenLockedStatus(true);
      //Added for autosave
      //_saveClass({});
    },

    handleVisualElementBlockerClicked: function (oInfo) {
      var oActiveClass = ClassUtils.getCurrentClass();
      SettingUtils.handleVisualElementBlockerClicked(oActiveClass, oInfo, _triggerChange);
    },

    handleSectionClicked: function (sSectionId) {
      ClassProps.setSelectedSectionId(sSectionId);
    },

    addNewAssetConfigurationSectionClicked: function () {
      let oActiveClass =  ClassUtils.getCurrentClass();
      oActiveClass.currentAssetUploadConfigurationModel = {
        id: UniqueIdentifierGenerator.generateUUID(),
        extension: "",
        extractMetadata: false,
        extractRendition: false,
        isCreated: true
      };
      _triggerChange();
    },

    editAssetConfigurationSectionClicked: function (sId) {
      let oActiveClass =  ClassUtils.getCurrentClass() ;
      let aExtensionConfiguration = oActiveClass.extensionConfiguration;
      let oFoundConfig = CS.find(aExtensionConfiguration,{id: sId});
      oFoundConfig && (oActiveClass.currentAssetUploadConfigurationModel = CS.cloneDeep(oFoundConfig));
      _triggerChange();
    },

    handleAssetConfigurationDialogButtonClicked :function (sId) {
      _handleAssetConfigurationDialogButtonClicked(sId);
    },

    handleDeleteAssetConfigurationRowClicked: function (oRow) {
      CustomActionDialogStore.showConfirmDialog(getTranslation().SELECTED_EXTENSION + " - ." + oRow.extension,
        getTranslation().DELETE_CONFIRMATION,
        _deleteExtensionAfterConfirmation.bind(this, oRow),
        function () {},
        function () {});
    },

    handleAssetConfigRowDataChange: function (oField, sColumnId, oData) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let oExtensionConfiguration = oActiveClass.extensionConfiguration;
      let oSelectedSection = CS.find(oExtensionConfiguration, {'id': oField.id});
      /*let bIsExtensionExists = CS.find(oExtensionConfiguration, { 'extension': oData }) ? true : false;

      //check for duplicate extension
      if(bIsExtensionExists){
        _alertifyError("Extension exists");
      }*/

      oSelectedSection[sColumnId] = oData;

      _triggerChange();

    },

    handleSectionNameChanged: function (sNewValue) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let aSectionsInClass = oActiveClass.sections;
      let sSelectedSectionId = ClassProps.getSelectedSectionId();
      let oSelectedSection = CS.find(aSectionsInClass, {'id': sSelectedSectionId});
      oSelectedSection.label = sNewValue;
      ClassUtils.setClassScreenLockedStatus(true);
    },

    handleSectionRowCountChanged: function (sSectionId, iRowCount) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let aSectionsInClass = oActiveClass.sections;
      let oSelectedSection = CS.find(aSectionsInClass, {'id': sSectionId});
      oSelectedSection.rows = iRowCount;
      ClassUtils.setClassScreenLockedStatus(true);
      _triggerChange();
    },

    handleSectionColCountChanged: function (sSectionId, iColCount) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let aSectionsInClass = oActiveClass.sections;
      let oSelectedSection = CS.find(aSectionsInClass, {'id': sSectionId});
      oSelectedSection.columns = iColCount;
      ClassUtils.setClassScreenLockedStatus(true);
      _triggerChange();
    },

    handleSectionSkippedToggled: function(sSectionId){
      _handleSectionSkippedToggled(sSectionId);
    },

    handleSectionDeleteClicked: function(sSectionId){
      _handleDeleteSectionClicked(sSectionId);
    },

    handleSectionMoveUpClicked: function(sSectionId){
      _handleSectionMoveUpClicked(sSectionId);
    },

    handleSectionMoveDownClicked: function(sSectionId){
      _handleSectionMoveDownClicked(sSectionId);
    },

    handleSectionBlockerClicked: function(sSectionId){
      _handleSectionBlockerClicked(sSectionId);
    },

    handleSectionIconChanged: function (sIconKey) {
      _handleSectionIconChanged(sIconKey);
    },

    handleClassCreateDialogButtonClicked: function (sContext, sLabel, bIsNature, sNatureType) {
      let oActiveClass = ClassUtils.getActiveClass();
      switch (sContext) {
        case "create":
          let oCallBackData={};
          oCallBackData.isCreated= true;

          let aTypesToExclude = [
            NatureTypeDictionary.GTIN,
            NatureTypeDictionary.EMBEDDED,
            NatureTypeDictionary.LANGUAGE,
            NatureTypeDictionary.TECHNICAL_IMAGE,
          ];

          let sNatureType = oActiveClass.natureType;
          if (oActiveClass.isNature && !CS.isEmpty(sNatureType) && !CS.includes(aTypesToExclude, sNatureType)) {
            oActiveClass.isDefaultChild = true;
            oActiveClass.isAbstract = false;
          }
          if (!oActiveClass.isNature){
            delete oActiveClass.numberOfVersionsToMaintain;
          }

          _saveClass(oCallBackData);
          break;
        case "cancel":
          let sClassTypeContext = _getClassTypeContextBasedOnClassType(oActiveClass.type);
          let aClassList = SettingUtils.getAppData().getClassListByTypeGeneric(sClassTypeContext);
          let oNode = SettingUtils.getNodeFromTreeListById(aClassList, oActiveClass.id);
          let oParentClass = oNode.parent || {id: "article"};
          SettingUtils.removeNodeById(aClassList, oActiveClass.id);
          _setActiveClass({});
          ClassUtils.setClassScreenLockedStatus(false);
          _fetchClassDetailsAndSwitch(oParentClass, sClassTypeContext);
          break;
      }

    },

    toggleLinkScrollActive : function(sId){
      _toggleLinkScrollActive(sId);
    },

    handleVisualElementAttributeValueChanged: function(oInfo){
      let oActiveClass = ClassUtils.makeActiveClassDirty();

      let oSection = CS.find(oActiveClass.sections, {id: oInfo.sectionId});
      let oElement = CS.find(oSection.elements, {id: oInfo.elementId});
      oElement[oInfo.propertyName] = oInfo.value;
      ClassUtils.setClassScreenLockedStatus(true);
      _triggerChange();
    },

    handleSectionDroppedFromWithinClassSection: function (sDroppedId, sDraggedId) {
      if (sDraggedId === sDroppedId) {
        return;
      }
      let oActiveClass = ClassUtils.getCurrentClass();
      let aSections = oActiveClass.sections;
      let oDropSection = CS.find(aSections, {id: sDroppedId});

      if (!oDropSection.isInherited) {
        let iDroppedIndex = 0;
        let iDraggedIndex = 0;
        if (!CS.isEmpty(aSections)) {
          iDroppedIndex = CS.findIndex(aSections, {id: sDroppedId});
          iDraggedIndex = CS.findIndex(aSections, {id: sDraggedId});
        }

        if (iDraggedIndex !== iDroppedIndex - 1) {
          oActiveClass = ClassUtils.makeActiveClassDirty();
          aSections = oActiveClass.sections;
          let oSectionToAdd;
          oSectionToAdd = CS.remove(aSections, {id: sDraggedId})[0];
          if (iDroppedIndex < iDraggedIndex) {
            aSections.splice(iDroppedIndex, 0, oSectionToAdd);
          } else if (iDroppedIndex > iDraggedIndex) {
            aSections.splice(iDroppedIndex - 1, 0, oSectionToAdd);
          }
          _refreshSequenceIds(aSections);
          _triggerChange();
        }
      }
      _triggerChange();
    },

    handleClassContextDialogDateValueChanged: function(sKey, sDateValue){
      _handleClassContextDialogDateValueChanged(sKey, sDateValue);
      _triggerChange();
    },

    handleSectionElementCheckboxToggled: function (sSectionId, sElementId, sProperty) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      SettingUtils.handleSectionElementCheckboxToggled(oActiveClass, sSectionId, sElementId, sProperty);
      _triggerChange();
    },

    handleSectionElementInputChanged: function (sSectionId, sElementId, sProperty, sNewValue) {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      SettingUtils.handleSectionElementInputChanged(oActiveClass, sSectionId, sElementId, sProperty, sNewValue);
      _triggerChange();
    },

    handleSectionMSSValueChanged: function (sSectionId, sElementId, sProperty, aNewValue, sScreenName){
      let oActiveClass =  ClassUtils.makeActiveClassDirty();
      SettingUtils.handleSectionMSSValueChanged (oActiveClass,sSectionId, sElementId, sProperty, aNewValue, sScreenName);
      _triggerChange();
    },

    handleElementTagCheckAllChanged: function(oInfo){
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let oAppData = SettingUtils.getAppData();

      let oSection = CS.find(oActiveClass.sections, {id: oInfo.sectionId});
      let oElement = CS.find(oSection.elements, {id: oInfo.elementId});
      let aTagList = oAppData.getTagList();

      if (oInfo.checked) {
        oElement.tagGroups = [];
        CS.forEach(aTagList[0].children, function(oTag){
          if(oTag.isDimensional ){
            if(oElement.type === "tag" && oElement.tag.id !== oTag.id){
              oElement.tagGroups.push(oTag.id);
            }else if(oElement.type === "attribute"){
              oElement.tagGroups.push(oTag.id);
            }

          }
        });
      } else {
        oElement.tagGroups = [];
      }

      _handleElementAutoIsVariantAllowedChange(oElement);
      _triggerChange();
    },

    addDefaultTagValue: function (sSectionId, sElementId, sTagGroupId, aTagValueIds, bIsSingleSelect) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      var oSection = CS.find(oActiveClass.sections, {id: sSectionId});
      var oElement = CS.find(oSection.elements, {id: sElementId});
      if(bIsSingleSelect) {
        oElement.defaultValue = [];
        aTagValueIds = [aTagValueIds];
      }
      var aDefaultValue = oElement.defaultValue;
      CS.forEach(aTagValueIds, function (sTagValueId) {
        aDefaultValue.push({
          relevance: 100,
          tagId: sTagValueId
        });
      });
      _triggerChange();
    },

    handleYesNoViewToggled: function(sSectionId, sElementId, bValue){
      try {
        var oActiveClass = ClassUtils.makeActiveClassDirty();
        var oSection = CS.find(oActiveClass.sections, {id: sSectionId});
        var oDefaultValue = {};
        var oElement = CS.find(oSection.elements, {id: sElementId});
        oDefaultValue.tagId = oElement.tag.children[0].id;
        oDefaultValue.relevance = bValue ? 100: -100;
        oElement.defaultValue = [oDefaultValue];
      }
      catch (oException) {
        ExceptionLogger.error(oException);
      }

      _triggerChange();
    },

    addDefaultTagValueForConfigTabularView: function (sSectionId, sElementId, sTagGroupId, aTagValueIds) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      var oSection = CS.find(oActiveClass.sections, {id: sSectionId});
      var oElement = CS.find(oSection.elements, {id: sElementId});
      var aDefaultValue = oElement.defaultValue;
      CS.forEach(aDefaultValue, function (oTagValue) {
        if(!CS.includes(aTagValueIds, oTagValue.tagId)) {
          oTagValue.relevance = 0;
        }
      });

      CS.forEach(aTagValueIds, function (sTagValueId) {
        var oTagValue = CS.find(aDefaultValue, {tagId: sTagValueId});
        if(oTagValue) {
          oTagValue.relevance = 100;
        } else {
          aDefaultValue.push({
            relevance: 100,
            tagId: sTagValueId
          });
        }
      });
      _triggerChange();
    },

    removeDefaultTagValue: function (sSectionId, sElementId, sTagGroupId, sTagValueId) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      var oSection = CS.find(oActiveClass.sections, {id: sSectionId});
      var oElement = CS.find(oSection.elements, {id: sElementId});
      var aDefaultValue = oElement.defaultValue;
      CS.remove(aDefaultValue, {tagId: sTagValueId});
      _triggerChange();
    },

    handleElementFilterableOrSortableCheckAllChanged: function(oInfo){
      var oActiveClass = ClassUtils.makeActiveClassDirty();

      var oSection = CS.find(oActiveClass.sections, {id: oInfo.sectionId});
      var oElement = CS.find(oSection.elements, {id: oInfo.elementId});

      var sProperty = oInfo.property;

      oElement[sProperty] = !oElement[sProperty];
      _triggerChange();
    },

    handleClassRelationshipModified: function (sRelationshipId, sKey, sNewValue, oReferencedData) {
      switch (sKey) {
        case "relationshipLabel":
          _handleClassRelationshipSingleValueChanged(sRelationshipId, "label", sNewValue);
          break;
        case "maxNoOfItems":
          _handleClassRelationshipSingleValueChanged(sRelationshipId, "maxNoOfItems", sNewValue);
          break;
        case "side2klass":
          _handleClassRelationshipSide2Changed(sRelationshipId, sNewValue, oReferencedData);
          break;
        case "tab":
          _handleClassRelationshipSingleValueChanged(sRelationshipId, sKey, sNewValue, oReferencedData);
          break;
        case "taxonomyInheritanceSetting":
          _handleClassRelationshipTaxonomyInheritanceChanged(sRelationshipId, sKey, sNewValue);
          break;
        case "enableAfterSave":
          _handleClassRelationshipToggleButtonValueChanged(sRelationshipId, sKey, sNewValue);
          break;
      }
    },

    handleNatureTypeChanged: function (sNatureType, sClickedContext) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      oActiveClass.natureType = sNatureType;
      switch (oActiveClass.type){
        case MockDataForEntityBaseTypesDictionary.articleKlassBaseType:
          _handleArticleNatureTypeChanged(oActiveClass, sClickedContext);
          break;

        case MockDataForEntityBaseTypesDictionary.assetKlassBaseType: //Assets
          _saveClass({});
          break;
      }
    },

    handleRemoveGtinClass: function (sid) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      delete oActiveClass.gtinKlassId;
      _triggerChange();
    },

    handleRemoveLanguageClass: (sId) => {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      delete oActiveClass.languageKlassId;
      _triggerChange();
    },

    handleClassContextDialogSelectionToggleClicked: function(sKey, sId){
      _handleClassContextDialogSelectionToggleClicked(sKey, sId);
      _triggerChange();
    },

    handleSelectionToggleButtonClicked: function (sKey, sId, bSingleSelect) {
      _handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
    },

    handleClassConfigRuleItemClicked: function(sRuleId){
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      var aAppliedDataRules = oActiveClass.dataRules;
      var iRuleIndex = CS.indexOf(aAppliedDataRules, sRuleId);
      if(iRuleIndex < 0){
        aAppliedDataRules.push(sRuleId);
      }else{
        aAppliedDataRules.splice(iRuleIndex, 1);
      }
    },

    handleClassContextUploadIconChangeEvent: function (sIconKey, sIconObjectKey) {
      _handleClassContextUploadIconChangeEvent(sIconKey, sIconObjectKey);
      _triggerChange();
    },

    handleClassIconChanged: function (sIconKey, sIconObjectKey) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      oActiveClass.icon = sIconKey;
      oActiveClass.iconKey = sIconObjectKey;
      oActiveClass.showSelectIconDialog = false;
    },

    handleClassPreviewImageChanged: function (sIconKey) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      oActiveClass.previewImage = sIconKey;
      _triggerChange();
    },

    handleCommonConfigAttributeChanged: function (sKey, sNewValue) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      if (sKey == 'label') {
        sNewValue = CS.trim(sNewValue);
      }
      oActiveClass[sKey] = sNewValue;
      _triggerChange();
    },

    handleClassAssetUploadConfigChanged :  function(sKey, sNewValue){
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      oActiveClass['currentAssetUploadConfigurationModel'][sKey] = sNewValue;
      _triggerChange();
    },

    handleMetricUnitChanged: function (sSelectedUnit, oSectionElementDetails) {
      _handleMetricUnitChanged(sSelectedUnit, oSectionElementDetails);
    },

    handleClassContextDialogPropertyChanged: function(sKey, sVal){
      _handleClassContextDialogPropertyChanged(sKey, sVal);
      _triggerChange();
    },

    handleGtinClassAddIds: function (aCheckedItems, oReferencedData) {
      var oActiveClass = ClassUtils.makeActiveClassDirty();
      oActiveClass.gtinKlassId = aCheckedItems[0];
      if (!oActiveClass.referencedKlasses) {
        oActiveClass.referencedKlasses = {};
      }
      if (!oActiveClass.referencedKlasses[aCheckedItems[0]]) {
        oActiveClass.referencedKlasses[aCheckedItems[0]] = oReferencedData[aCheckedItems[0]];
        oActiveClass.referencedKlasses[aCheckedItems[0]].propagableAttributes = [];
        oActiveClass.referencedKlasses[aCheckedItems[0]].propagableTags = [];
      }
      _triggerChange();
    },

    handleLanguageClassAddIds: (aCheckedItems, oReferencedData) => {
      let oActiveClass = ClassUtils.makeActiveClassDirty();
      let sLanguageKlassId = aCheckedItems[0];
      oActiveClass.languageKlassId = sLanguageKlassId;
      if (!oActiveClass.referencedKlasses) {
        oActiveClass.referencedKlasses = {};
      }
      if (!oActiveClass.referencedKlasses[sLanguageKlassId]) {
        oActiveClass.referencedKlasses[sLanguageKlassId] = oReferencedData[sLanguageKlassId];
        oActiveClass.referencedKlasses[sLanguageKlassId].propagableAttributes = [];
        oActiveClass.referencedKlasses[sLanguageKlassId].propagableTags = [];
      }
      _triggerChange();
    },

    handleAddEmbeddedKlassIds: function(aCheckedItems){
      _addToEmbeddedKlassIds(aCheckedItems);
      _triggerChange();
    },

    handleAddTechnicalImageKlassIds: function (aCheckedItems) {
      _addToTechnicalImageKlassIds(aCheckedItems);
      _triggerChange();
    },

    handleRemoveEmbeddedKlassId: function(sId){
      _removeEmbeddedKlassId(sId);
      _triggerChange();
    },

    handleRemoveTechnicalImageKlassId: function (sId) {
      _removeTechnicalImageKlassId(sId);
      _triggerChange();
    },

    addPropertyInNatureRelationshipSide: function (sEntity, sRelationshipId, sSide, aSelectedIds, oReferencedData) {
      _addPropertyInNatureRelationshipSide(sEntity, sRelationshipId, sSide, aSelectedIds, oReferencedData);
      _triggerChange();
    },

    handleNatureRelationshipPropertyCouplingChanged: function (sRelationshipId, sSide, sPropertyId, sNewValue, sContext) {
      _changeNatureRelationshipPropertyCoupling(sRelationshipId, sSide, sPropertyId, sNewValue, sContext);
      _triggerChange();
    },

    removePropertyInNatureRelationshipSide: function (sRelationshipId, sSide, iItemID, sContext) {
      _removePropertyInNatureRelationshipSide(sRelationshipId, sSide, iItemID, sContext);
      _triggerChange();
    },

    setClassSectionMap: function(aPropertyCollectionList){
      ClassProps.setClassSectionMap(_createElementMapForAvailability(aPropertyCollectionList));
    },

    handleClassContextDialogClosed: function(){
      _handleClassContextDialogClosed();
      _triggerChange();
    },

    handleClassContextDialogOkClicked: function(){
      _handleClassContextDialogOkClicked();
    },

    handleClassContextDialogDiscardClicked: function(){
      _handleClassContextDialogDiscardClicked();
    },

    handleClassContextDialogOpenClicked: function(){
      _handleClassContextDialogOpenClicked();
    },

    handleRemoveSelectedTagGroupClicked: function(sTagGroupId){
      _handleRemoveSelectedTagGroupClicked(sTagGroupId);
      _triggerChange();
    },

    handleClassContextDialogAddTagGroup: function(sTagGroupId,sContext){
      _handleClassContextDialogAddTagGroup(sTagGroupId,sContext);
    },

    handleClassContextDialogAddOrRemoveTagValue: function(sTagGroupId, aCheckedItems){
      _handleClassContextDialogAddOrRemoveTagValue(sTagGroupId, aCheckedItems);
      _triggerChange();
    },

    handleAddNewTagCombination: function(){
      _handleAddNewTagCombination();
    },

    handleClassContextCombinationMSSPopOver: function (sSelectorId,sTagId,aSelectedItems) {
      _handleClassContextCombinationMSSPopOver(sSelectorId,sTagId,aSelectedItems);
    },

    handleTagCombinationSelected: function(){
      _handleTagCombinationSelected();
    },

    handleDeleteNewTagCombination: function(sUniqueSelectionId){
      _handleDeleteNewTagCombination(sUniqueSelectionId);
    },

    handleClassContextCombinationEditClicked: function(sUniqueSelectionId){
      _handleClassContextCombinationEditClicked(sUniqueSelectionId);
    },

    handleContextCombinationDeleteTagValue: function(sSelectorId, sTagId, sId){
      _handleContextCombinationDeleteTagValue(sSelectorId, sTagId, sId);
    },

    handleMSSValueChanged: function(sKey, aSelectedItems, oReferencedData){
      _handleMSSValueChanged(sKey, aSelectedItems, oReferencedData);
    },

    handleMarketTagsChanged: function(sKey, aSelectedItems){
      _handleMarketTagsChanged(sKey, aSelectedItems);
    },

    handleGroupedRelationshipItemsChanged: function(aSelectedItems){

    },

    handleGroupedRelationshipRemoveItemByOtherSide: function(sId){
      _handleGroupedRelationshipRemoveItemByOtherSide(sId);
    },

    handleMSSValueRemoved: function(sKey, sId, sContextKey){
      _handleMSSValueRemoved(sKey, sId, sContextKey);
    },

    handleRemoveKlassClicked: function(sKey, sId){
      _handleRemoveKlassClicked(sKey, sId);
    },

    handleClassDataTransferPropertiesAdded: function(sEntity, aSelectedIds, oReferencedData, sContext){
      _handleClassDataTransferPropertiesAdded(sEntity, aSelectedIds, oReferencedData, sContext);
    },

    handleClassDataTransferPropertiesRemoved: function (sClassId, sPropertyId, sContext) {
      _handleClassDataTransferPropertiesRemoved(sClassId, sPropertyId, sContext);
    },

    handleClassDataTransferPropertiesCouplingChanged: function (sClassId, sPropertyId, sNewValue, sContext) {
      _handleClassDataTransferPropertiesCouplingChanged(sClassId, sPropertyId, sNewValue, sContext);
    },

    handleSectionToggleButtonClicked: function (sSectionId) {
      _handleSectionToggleButtonClicked(sSectionId);
    },

    handleSide2RelationshipSectionCollapseButtonClicked: function (sContext, sSectionId) {
      _handleSide2RelationshipSectionCollapseButtonClicked(sContext, sSectionId);
    },

    handleClassConfigImportButtonClicked: function (aFiles,oImportExcel) {
      _handleClassConfigImportButtonClicked(aFiles,oImportExcel);
    },

    handleClassConfigExportButtonClicked: function (oSelectiveExport) {
      _handleClassConfigExportButtonClicked(oSelectiveExport);
    },

    getAllAssetExtensions: function () {
      _getAllAssetExtensions();
    },

    handleClassManageEntityButtonClicked: function () {
      _handleClassManageEntityButtonClicked();
    },

    handleSectionListApplyButtonClicked: function (sContext, aSelectedItems, oReferencedData) {
      _handleSectionListApplyButtonClicked(sContext, aSelectedItems, oReferencedData);
    },

    handleSectionListDeleteButtonClicked: function (sSelectedElement, sContext) {
      _handleSectionListDeleteButtonClicked(sSelectedElement, sContext);
    },
  };

})();

MicroEvent.mixin(ClassStore);

export default ClassStore;
