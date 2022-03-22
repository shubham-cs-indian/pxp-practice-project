import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { PropertyCollectionRequestMapping as oPropertyCollectionRequestMapping } from '../../tack/setting-screen-request-mapping';
import PropertyCollectionProps from './../model/property-collection-config-view-props';
import ClassUtils from './../helper/class-utils';
import SettingUtils from './../helper/setting-utils';
import SettingScreenConstants from '../../store/model/setting-screen-constants';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import assetTypes from '../../tack/coverflow-asset-type-list';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ManageEntityStore from "./config-manage-entity-store";
import SettingScreenProps from "../model/setting-screen-props";
import PropertyCollectionTabLayoutData from "../../tack/property-collection-tab-layout-data";
import ConfigDataEntityTypeDictionary from '../../../../../../commonmodule/tack/config-data-entities-dictionary';
import MockDataForBaseTypeDictionary from '../../../../../../commonmodule/tack/mock-data-for-property-base-type-dictionary';
import PropertyCollectionDraggableListTabsLayoutData from "../../tack/property-collection-draggable-list-tab-layout-data";

var PropertyCollectionStore = (function () {

  var _triggerChange = function () {
    PropertyCollectionStore.trigger('property-collection-changed');
  };

  var _getActivePropertyCollection = function () {
    return PropertyCollectionProps.getActivePropertyCollection();
  };

  var _makeActivePropertyCollectionDirty = function () {
    var oActivePropertyCollection = _getActivePropertyCollection();
    if (!oActivePropertyCollection.clonedObject) {
      oActivePropertyCollection.clonedObject = CS.cloneDeep(oActivePropertyCollection);
    }
    return oActivePropertyCollection.clonedObject;
  };

  var successFetchPropertyCollectionListCallback = function (bLoadMore, bFetchPCDetails, oResponse) {
    var oAppData = SettingUtils.getAppData();
    var aPropertyCollectionListFromServer = oResponse.success.list;
    let iTotalCount = oResponse.success.count; //Count is currently unused

    if(bLoadMore){
      var aPCList = oAppData.getPropertyCollectionList();
      aPropertyCollectionListFromServer = aPCList.concat(aPropertyCollectionListFromServer);
    }

    oAppData.setPropertyCollectionList(aPropertyCollectionListFromServer);
    PropertyCollectionProps.setShowLoadMore(CS.size(aPropertyCollectionListFromServer) !== iTotalCount);
    var oSectionValueList = _getPropertyCollectionValueList();
    SettingUtils.addNewTreeNodesToValueList(oSectionValueList, aPropertyCollectionListFromServer);
    bFetchPCDetails && aPropertyCollectionListFromServer[0] ?
    _fetchPropertyCollectionDetailsAndSwitch(aPropertyCollectionListFromServer[0].id) :
    _triggerChange();
  };

  var failureFetchPropertyCollectionListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchPropertyCollectionListCallback", getTranslation());
  };

  let successRemovePropertyCollectionCallback = (oResponse) => {
    let aPropertyCollectionList = SettingUtils.getAppData().getPropertyCollectionList();
    let oPropertyCollectionValueList = _getPropertyCollectionValueList();
    let oActivePropertyCollection = _getActivePropertyCollection();

    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnListNodeDelete(aPropertyCollectionList, oPropertyCollectionValueList, oActivePropertyCollection.id, oResponse.success);
    oNewActiveNode && _fetchPropertyCollectionDetailsAndSwitch(oNewActiveNode.id);
    PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(false);
    alertify.success(getTranslation().PROPERTY_COLLECTION_SUCCESSFULLY_REMOVED);
    _triggerChange();
  };

  var failureRemovePropertyCollectionCallback = function (oCallback, oResponse) {
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
    SettingUtils.failureCallback(oResponse, "failureRemovePropertyCollectionCallback" , getTranslation());
  };

  var _createClassSectionElementMap = function () {
    var oAppData = SettingUtils.getAppData();
    var oAttributeMap = oAppData.getAttributeList();
    var oTaxonomyMap = oAppData.getTaxonomyListMap();
    var aTagMap = oAppData.getTagMap();
    SettingUtils.rejectStatusAndLifecycleTagsTypes(oAppData.getTagList()[0].children);
    let sActiveTabId = SettingScreenProps.propertyCollectionConfigView.getPropertyCollectionDraggableListActiveTabId();
    PropertyCollectionProps.setClassSectionAttributeMap(oAttributeMap);
    PropertyCollectionProps.setClassSectionTagMap(aTagMap);
    PropertyCollectionProps.setSectionTaxonomyList(oTaxonomyMap);
    let aActiveTabListMap = [];
    let oActiveTabListMap = (sActiveTabId === ConfigDataEntityTypeDictionary.ATTRIBUTES) ? oAttributeMap :
                            (sActiveTabId === ConfigDataEntityTypeDictionary.TAGS) ? aTagMap :
                            (sActiveTabId === ConfigDataEntityTypeDictionary.TAXONOMIES) ? oTaxonomyMap : null;
    CS.forEach(oActiveTabListMap, function (oElements) {
      aActiveTabListMap.push(oElements);
    });
    PropertyCollectionProps.setAvailablePropertyList(aActiveTabListMap);
  };

  var _setElementDataFromReferencedElements = function (oPropertyCollection) {

    CS.forEach(oPropertyCollection.elements, function (oElement) {
      var oElementData = {};
      switch(oElement.type) {
        case 'attribute':
          oElementData = CS.find(oPropertyCollection.referencedAttributes, {id: oElement.id});
          break;
        case 'tag':
          oElementData = CS.find(oPropertyCollection.referencedTags, {id: oElement.id});
          break;
        case 'relationship':
          oElementData = CS.find(oPropertyCollection.referencedRelationships, {id: oElement.id});
          break;
        case 'taxonomy':
          oElementData = CS.find(oPropertyCollection.referencedTaxonomies, {id: oElement.id});
          break;
      }
      oElement.icon = oElementData.icon;
      oElement.iconKey = oElementData.iconKey;
      oElement.label = oElementData.label;
      oElement.data = oElementData;
    });
    delete oPropertyCollection.referencedAttributes;
    delete oPropertyCollection.referencedTags;
    delete oPropertyCollection.referencedRelationships;
    delete oPropertyCollection.referencedTaxonomies;
  };

  var successCreatePropertyCollectionCallback = function (oClassValue, oCallback, oResponse) {

    var ooPropertyCollectionValueListValueList = _getPropertyCollectionValueList();
    var aPropertyCollectionList = SettingUtils.getAppData().getPropertyCollectionList();

    var oPCFromServer = oResponse.success;
    oClassValue.id = oPCFromServer.id;
    oClassValue.code = oPCFromServer.code;

    var oDummyClassNode = oClassValue;
    SettingUtils.addNewTreeNodesToValueList(ooPropertyCollectionValueListValueList, [oDummyClassNode]);
    SettingUtils.applyValueToAllTreeNodes(ooPropertyCollectionValueListValueList, 'isSelected', false);
    SettingUtils.applyValueToAllTreeNodes(ooPropertyCollectionValueListValueList, 'isChecked', 0);

    var oPropertyCollectionValueObj = ooPropertyCollectionValueListValueList[oDummyClassNode.id];
    SettingUtils.setPropertyValue(oPropertyCollectionValueObj, 'isSelected', true);
    SettingUtils.setPropertyValue(oPropertyCollectionValueObj, 'isEditable', true);
    SettingUtils.setPropertyValue(oPropertyCollectionValueObj, 'isChecked', 2);
    SettingUtils.setPropertyValue(oPropertyCollectionValueObj, 'isChildLoaded', true);
    SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);
    alertify.success(getTranslation().PROPERTY_COLLECTION_CREATED_SUCCESSFULLY);
    delete oDummyClassNode.isCreated;
    PropertyCollectionProps.setPropertyCollectionToCreate({});
    aPropertyCollectionList.push(oDummyClassNode);
    successFetchPropertyCollectionElaborateCallback(oClassValue, oCallback, oResponse);
  };

  var successFetchPropertyCollectionElaborateCallback = function (oClassValue, oCallback, oResponse) {
    var oPropertyCollectionValueList = _getPropertyCollectionValueList();
    var aPropertyCollectionList = SettingUtils.getAppData().getPropertyCollectionList();
    var oPropertyCollection = SettingUtils.getNodeFromTreeListById(aPropertyCollectionList, oClassValue.id);
    var oResponseFromServer = oResponse.success;

    if (oClassValue.id != SettingUtils.getTreeRootId()) {
      CS.assign(oPropertyCollection, oResponseFromServer);
      delete oPropertyCollection.clonedObject;
    }
    _setElementDataFromReferencedElements(oPropertyCollection);
    var oTreeItemValue = oPropertyCollectionValueList[oResponseFromServer.id];
    _switchActivePropertyCollection(oTreeItemValue);
    PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(false);
    PropertyCollectionProps.setPropertyCollectionDraggableModifiedPropertiesList([]);

    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var failureFetchPropertyCollectionElaborateCallback = function (sId, oResponse) {

    if(oResponse.failure && CS.find(oResponse.failure.devExceptionDetails, {key: "PropertyCollectionNotFoundException"})) {
      var aPropertyCollectionList = SettingUtils.getAppData().getPropertyCollectionList();
      SettingUtils.removeNodeById(aPropertyCollectionList, sId);
      var oActivePropertyCollection = _getActivePropertyCollection();
      if(oActivePropertyCollection.id == sId) {
        _setActivePropertyCollection({});
      }
      PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(false);
      _triggerChange();
    }

    SettingUtils.failureCallback(oResponse, "failureFetchPropertyCollectionListCallback", getTranslation());
  };

  var _fillClassSectionElementMap = function (oPropertyCollection) {
    let aActiveTabListMap = PropertyCollectionProps.getAvailablePropertyList();
    let aSelectedPropertyList = oPropertyCollection.clonedObject ? oPropertyCollection.clonedObject.elements : oPropertyCollection.elements;

    CS.forEach(aActiveTabListMap, function (oElement) {
      let oProperty = CS.find(aSelectedPropertyList, {id: oElement.id});
      if (CS.isNotEmpty(oProperty)) {
        oElement.isAvailable = false;
      }else {
        oElement.isAvailable = true;
      }
    });
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

  let _resetPaginationData = function () {
    PropertyCollectionProps.resetPaginationData();
  };

  var _fetchPropertyCollectionsList = function (bLoadMore = false, sSelectedTabId, bFetchPCDetails = true) {

    let aTabListIds = PropertyCollectionTabLayoutData().tabListIds;
    sSelectedTabId = sSelectedTabId || PropertyCollectionProps.getPropertyCollectionListActiveTabId();
    let bIsForXRay = sSelectedTabId === aTabListIds.PROPERTY_COLLECTION_X_RAY_LIST_TAB;

    var oParameters = {};
    oParameters.isForXRay = bIsForXRay;

    let sSearchText = PropertyCollectionProps.getSearchText();
    let iFrom = PropertyCollectionProps.getFrom();
    let oPostConstantData = PropertyCollectionProps.getSearchConstantData();
    if(!bLoadMore){
      iFrom = 0;
    }
    let oPostData = {
      searchText: sSearchText,
      from: iFrom,
      size: oPostConstantData.size,
      searchColumn: oPostConstantData.searchColumn,
      sortOrder: oPostConstantData.sortOrder,
      sortBy: oPostConstantData.sortBy,
    };

    SettingUtils.csPostRequest(oPropertyCollectionRequestMapping.GetAllPropertyCollections, oParameters, oPostData,
        successFetchPropertyCollectionListCallback.bind(this, bLoadMore, bFetchPCDetails), failureFetchPropertyCollectionListCallback);
  };

  let _indexFinder = function(aNew, aAdded){
    let iNewIndex = null;
    CS.forEach(aNew, function (oElement, iIndex) {
      if(oElement.id === aAdded.id){
        iNewIndex = iIndex + 1;
         return false;
      }
    });
    return iNewIndex;
  };

  let _getTypeFromBaseType = function (sBaseType) {
    let sType = "";
    CS.forEach(MockDataForBaseTypeDictionary, function (aPropertyBaseTypeList, sKey) {
      if (CS.includes(aPropertyBaseTypeList, sBaseType)) {
        sType = sKey;
        return false;
      }
    });
    return sType;
  };

  var _generateADMForPropertyCollectionSave = function (oOld, oNew) {
    var oADMToSave = CS.cloneDeep(oNew);
    delete oADMToSave.elements;
    oADMToSave.addedElements = CS.map(CS.differenceBy(oNew.elements, oOld.elements, 'id'), function (oElement) {
      return {
        id: oElement.id,
        type:oElement.type,
        index: _indexFinder(oNew.elements, oElement)
      };
    });

    oADMToSave.deletedElements = CS.map(CS.differenceBy(oOld.elements, oNew.elements, 'id'), function (oElement) {
      return {
        id: oElement.id,
        type: oElement.type,
        index: _indexFinder(oOld.elements, oElement)
      };
    });

    let aModifiedElementsList = PropertyCollectionProps.getPropertyCollectionDraggableModifiedPropertiesList();
    let aModifiedElements = [];
    CS.forEach(aModifiedElementsList, function (sElementId) {
      let iNewIndex = CS.findIndex(oNew.elements, {id: sElementId});
      if (iNewIndex != -1 && !CS.find(oADMToSave.addedElements, {id: sElementId})) {
        let oShuffledElement = CS.find(oNew.elements, {id: sElementId});
        aModifiedElements.push(
            {
              id: oShuffledElement.id,
              type: oShuffledElement.type,
              index: iNewIndex + 1
            })
      }
    });

    oADMToSave.modifiedElements = aModifiedElements;
    let oCustomTabsADM = SettingUtils.generateADMForCustomTabs( oOld.tab, oNew.tab);
    oADMToSave.addedTab = oCustomTabsADM.addedTab;
    oADMToSave.deletedTab = oCustomTabsADM.deletedTab;

    CS.forEach(oADMToSave.addedElements, function (oElement) {
      delete oElement.data;
    });
    return oADMToSave;
  };

  var _savePropertyCollection = function (oCallback) {
    var oPropertyCollection = _getActivePropertyCollection();
    if (CS.isEmpty(oPropertyCollection)) {
      alertify.message(getTranslation().NOTHING_IS_SELECTED);
      return;
    }

    if (!oPropertyCollection.clonedObject) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }

    var oPropertyCollectionClone = oPropertyCollection.clonedObject;
    oPropertyCollectionClone.label = oPropertyCollectionClone.label.trim();
    if(CS.isEmpty(oPropertyCollectionClone.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    //This method remove keys from request data.
    SettingUtils.removeKeysFromRequestData(oPropertyCollectionClone, ['iconKey']);

    var oPropertyCollectionToSave = _generateADMForPropertyCollectionSave(oPropertyCollection, oPropertyCollectionClone);

    var _saveSuccessAlert = function (oCallback) {
      if(oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute();
      }
      alertify.success(getTranslation().PROPERTY_COLLECTION_SAVED_SUCCESSFULLY);
    };

    var oCallbackData = {};
    oCallbackData.functionToExecute = _saveSuccessAlert.bind(this, oCallback);
    var fFailure = failureFetchPropertyCollectionElaborateCallback.bind(this, oPropertyCollection.id);
    SettingUtils.csPostRequest(oPropertyCollectionRequestMapping.SavePropertyCollection, {}, oPropertyCollectionToSave,
        successFetchPropertyCollectionElaborateCallback.bind(this, oPropertyCollection, oCallbackData), fFailure);
  };

  var _removePropertyCollections = function (aSavedPropertyCollectionIdsToDelete, aCreatedPropertyCollectionIds, oCallback) {
    var oPropertyCollectionData = {};
    oPropertyCollectionData.ids = aSavedPropertyCollectionIdsToDelete;
    var oPropertyCollectionValueList = _getPropertyCollectionValueList();
    var aPropertyCollectionList = SettingUtils.getAppData().getPropertyCollectionList();

    CS.forEach(aCreatedPropertyCollectionIds, function (sCreatedId) {
      SettingUtils.removeNodeById(aPropertyCollectionList, sCreatedId);
      delete oPropertyCollectionValueList[sCreatedId];
    });

    if (CS.isEmpty(oPropertyCollectionData.ids)) {

      PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(false);
      alertify.success(getTranslation().PROPERTY_COLLECTION_SUCCESSFULLY_REMOVED);
      _triggerChange();
    } else {
      SettingUtils.csDeleteRequest(oPropertyCollectionRequestMapping.DeletePropertyCollections, {}, oPropertyCollectionData, successRemovePropertyCollectionCallback, failureRemovePropertyCollectionCallback.bind(this, oCallback));
    }
  };

  var _switchActivePropertyCollection = function (oPropertyCollectionValue) {
    var aList = SettingUtils.getAppData().getPropertyCollectionList();
    var oValueList = _getPropertyCollectionValueList();
    var oPropertyCollection = SettingUtils.getNodeFromTreeListById(aList, oPropertyCollectionValue.id);

    SettingUtils.applyValueToAllTreeNodes(oValueList, 'isSelected', false);
    SettingUtils.applyValueToAllTreeNodes(oValueList, 'isChecked', 0);
    SettingUtils.setPropertyValue(oValueList, oPropertyCollectionValue.id, oPropertyCollectionValue);
    var oPropertyCollectionValueObj = oValueList[oPropertyCollectionValue.id];
    oPropertyCollectionValueObj.isSelected = true;
    oPropertyCollectionValueObj.isChecked = 2;
    oPropertyCollectionValueObj.isEditable = false;
    oPropertyCollectionValueObj.isLoading = false;
    oPropertyCollectionValueObj.label = oPropertyCollection.label;
    PropertyCollectionProps.setSelectedSectionId('');
    _createClassSectionElementMap();
    _fillClassSectionElementMap(oPropertyCollection);
    _setActivePropertyCollection(oPropertyCollection);
  };

  let _createNewPropertyCollection = function () {
    let oPropertyCollectionToCreate = PropertyCollectionProps.getPropertyCollectionToCreate();

    let oCodeToVerifyUniqueness = {
      id: oPropertyCollectionToCreate.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION

    };
    if (CS.isEmpty(oPropertyCollectionToCreate.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    let oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = _createPropertyCollectionCall.bind(this, oPropertyCollectionToCreate);
    let sURL = oPropertyCollectionRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  let switchTheTab = function(oActivePropertyCollection) {
    let aTabIds = PropertyCollectionTabLayoutData().tabListIds;
    let sSelectedTabId = oActivePropertyCollection.isForXRay
                         ? aTabIds.PROPERTY_COLLECTION_X_RAY_LIST_TAB : aTabIds.PROPERTY_COLLECTION_LIST_TAB;
    if (PropertyCollectionProps.getPropertyCollectionListActiveTabId() !== sSelectedTabId) {
      _fetchPropertyCollectionsList(false, sSelectedTabId, false);
    }
    PropertyCollectionProps.setPropertyCollectionListActiveTabId(sSelectedTabId);

  };

  let _createPropertyCollectionCall = async function (oPropertyCollectionToCreate) {

    await switchTheTab(oPropertyCollectionToCreate);
    let sSearchText = PropertyCollectionProps.getSearchText();
    if(CS.isNotEmpty(sSearchText)){
      PropertyCollectionProps.setSearchText("");
      _fetchPropertyCollectionsList();
    }
    var fFailure = failureFetchPropertyCollectionElaborateCallback.bind(this, oPropertyCollectionToCreate.id);
    SettingUtils.csPutRequest(oPropertyCollectionRequestMapping.CreatePropertyCollection, {}, oPropertyCollectionToCreate,
        successCreatePropertyCollectionCallback.bind(this, oPropertyCollectionToCreate, {}), fFailure);
  };

  var _changeAllValueDataOfClass = function (sId, sKey, defaultValue, value) {
    var oClassValueList = _getPropertyCollectionValueList();

    CS.forEach(oClassValueList, function (oClassValue) {
      oClassValue[sKey] = defaultValue;
    });
    oClassValueList[sId][sKey] = value;

  };

  var _fetchPropertyCollectionDetailsAndSwitch = function (sId) {
    var sUrl;
    var oParameters = {};
    var oRequestData = {};
    var oCallbackData = {};
    var _failureFetchClassElaborateCallback;
    var _successFetchClassElaborateCallback;

    _changeAllValueDataOfClass(sId, 'isSelected', false, true);
    _changeAllValueDataOfClass(sId, 'isChecked', false, true);
    _changeAllValueDataOfClass(sId, 'isLoading', false, true);

    var oValuesList = _getPropertyCollectionValueList();
    var oSelectedValue = oValuesList[sId];
    oParameters.id = sId;
    _failureFetchClassElaborateCallback = failureFetchPropertyCollectionElaborateCallback.bind(this, sId);
    _successFetchClassElaborateCallback = successFetchPropertyCollectionElaborateCallback.bind(this, oSelectedValue, oCallbackData);
    sUrl = oPropertyCollectionRequestMapping.GetPropertyCollection;

    SettingUtils.csGetRequest(sUrl, oParameters, _successFetchClassElaborateCallback, _failureFetchClassElaborateCallback);
  };

  var _saveUnsavedClass = function (oCallback) {
    var oCallbackData = {};
    oCallbackData.functionToExecute = oCallback.functionToExecute;
    _savePropertyCollection(oCallbackData);
  };

  var _discardUnsavedPropertyCollection = function (oCallback) {
    var oPropertyCollection = _getActivePropertyCollection();
    delete oPropertyCollection.clonedObject;
    PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(false);
    PropertyCollectionProps.setPropertyCollectionDraggableModifiedPropertiesList([]);
    _fillClassSectionElementMap(oPropertyCollection);
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var _deletePropertyCollections = function () {
    deleteUnusedPropertyCollections();
  }

  let deleteUnusedPropertyCollections = function () {
  var oPropertyCollectionData = {};
  oPropertyCollectionData.ids = [];
  var oPropertyCollectionValueList = _getPropertyCollectionValueList();
  var aPropertyCollectionList = SettingUtils.getAppData().getPropertyCollectionList();
  var aCreatedPropertyCollectionIds = [];
  let sType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION;
  var aPropertyCollectionsToBeDeletedList;
  aPropertyCollectionsToBeDeletedList = SettingUtils.getListOfNamesToDeleteFromTreeNodeList(aPropertyCollectionList, oPropertyCollectionValueList, oPropertyCollectionData.ids, aCreatedPropertyCollectionIds);

  if (CS.isEmpty(aPropertyCollectionsToBeDeletedList)) {
    alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
    return;
  }

  if (oPropertyCollectionData.ids.length || aCreatedPropertyCollectionIds.length) {

    var bIsStandardKlassSelected = false;
    CS.forEach(oPropertyCollectionData.ids, function (sPropertyCollectionId) {
      var oPropertyCollectionInfo = oPropertyCollectionValueList[sPropertyCollectionId];
      if (oPropertyCollectionInfo.isStandard) {
        bIsStandardKlassSelected = true;
        return true;
      }
    });

    if (bIsStandardKlassSelected) {
      alertify.message(getTranslation().STANDARD_PROPERTY_COLLECTION_DELETION);
    } else {
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aPropertyCollectionsToBeDeletedList,
          function () {
            _removePropertyCollections(oPropertyCollectionData.ids, aCreatedPropertyCollectionIds, {functionToExecute:  ManageEntityStore.handleManageEntityDialogOpenButtonClicked.bind(this, oPropertyCollectionData.ids, sType)});
          }, function (oEvent) {
          }, true);
    }
  } else {
    var oRootClassValue = oPropertyCollectionValueList[SettingUtils.getTreeRootId()];
    if (oRootClassValue.isSelected || oRootClassValue.isChecked) {
      alertify.message(getTranslation().ROOT_NODE_CAN_NOT_BE_DELETED);
    }
  }
};

  var _fetchPropertyCollectionDetails = function (oPropertyCollectionValue) {
    var bIsPropertyCollectionScreenLocked = _getPropertyCollectionScreenLockedStatus();
    if (bIsPropertyCollectionScreenLocked) {
      var oSwitchCallback = {};
      var sId = oPropertyCollectionValue.id || oPropertyCollectionValue.id;
      oSwitchCallback.functionToExecute = _fetchPropertyCollectionDetailsAndSwitch.bind(this, sId);
      CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveUnsavedClass.bind(this, oSwitchCallback),
          _discardUnsavedPropertyCollection.bind(this, oSwitchCallback),
          function () {
          });
    } else {
      _fetchPropertyCollectionDetailsAndSwitch(oPropertyCollectionValue.id);
    }
  };

  var _setActivePropertyCollection = function (oSection) {
    PropertyCollectionProps.setActivePropertyCollection(oSection);
  };

  var _getPropertyCollectionScreenLockedStatus = function () {
    return PropertyCollectionProps.getPropertyCollectionScreenLockedStatus();
  };

  var _setPropertyCollectionScreenLockedStatus = function (bVal) {
    return PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(bVal);
  };

  var _getPropertyCollectionValueList = function () {
    return PropertyCollectionProps.getPropertyCollectionValueList();
  };

  var _removeElementByReferenceId = function (aElements, sId) {
    var bFoundElement = false;
    CS.forEach(aElements, function (oElement, iIndex) {
      if (oElement.referenceId == sId) {
        aElements.splice(iIndex, 1);
        return false;
      }

      if (oElement.elements.length > 0) {
        bFoundElement = _removeElementByReferenceId(oElement.elements, sId);
        if (bFoundElement) {
          return false;
        }
      }
    });
    return bFoundElement;
  };

  var _handlePropertyCollectionIconChanged = function (sIconKey, sIconObjectKey) {
    var oActivePropertyCollection = _makeActivePropertyCollectionDirty();
    oActivePropertyCollection.icon = sIconKey ? sIconKey : "";
    oActivePropertyCollection.iconKey = sIconObjectKey;
    oActivePropertyCollection.showSelectIconDialog = false;
    _setPropertyCollectionScreenLockedStatus(true);
    _triggerChange();
  };

  let _discardPropertyCollection = function () {
    PropertyCollectionProps.setPropertyCollectionToCreate({});
    _setPropertyCollectionScreenLockedStatus(false);
    _triggerChange();
  };

  var _handlePropertyCollectionConfigDialogButtonClicked = function (sButtonId) {
    if (sButtonId == "create") {
      _createNewPropertyCollection();
    } else {
      _discardPropertyCollection({});
    }
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

  var _handlePropertyCollectionImportButtonClicked = function (aFiles,oImportExcel) {
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

  var _handlePropertyCollectionTabChanged = function (sTabId) {
    let bIsPropertyCollectionScreenLocked = _getPropertyCollectionScreenLockedStatus();
    let oCallback = {};
    oCallback.functionToExecute = () => {
      PropertyCollectionProps.setPropertyCollectionListActiveTabId(sTabId);
      _fetchPropertyCollectionsList();
    };
    if (bIsPropertyCollectionScreenLocked) {
      CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveUnsavedClass.bind(this, oCallback),
          _discardUnsavedPropertyCollection.bind(this, oCallback),
          function () {
          });
    }
    else {
      oCallback.functionToExecute();
    }

  };

  var _handlePropertyCollectionExportButtonClicked = function (oSelectiveExport) {
    return SettingUtils.csPostRequest(oSelectiveExport.sUrl, {}, oSelectiveExport.oPostRequest, successExportFile, failureExportFile);
  };

  var successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
    return true;
  };

  var failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
    return false;
  };

  let _handlePropertyCollectionManageEntityButtonClicked = function () {
    let oPropertyCollectionValueList = PropertyCollectionProps.getActivePropertyCollection();

    if (CS.isEmpty(oPropertyCollectionValueList)) {
      alertify.message(getTranslation().NOTHING_SELECTED);
      return;
    }
    else{
      var oPropertyCollectionData = {};
      oPropertyCollectionData.ids = [];
      let aPropertyCollectionValueList = _getPropertyCollectionValueList();
      let aPropertyCollectionList = SettingUtils.getAppData().getPropertyCollectionList();
      let sType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION;
      SettingUtils.getListOfNamesToDeleteFromTreeNodeList(aPropertyCollectionList, aPropertyCollectionValueList, oPropertyCollectionData.ids, []);
      ManageEntityStore.handleManageEntityDialogOpenButtonClicked(oPropertyCollectionData.ids, sType)
    }
  };

  let _handlePropertyCollectionDraggableListColumnsShuffled = function (oSource, oDestination, aDraggableIds) {
    let oActivePropertyCollection = _makeActivePropertyCollectionDirty();
    _setPropertyCollectionScreenLockedStatus(true);
    let aSelectedColumns = oActivePropertyCollection.elements;
    let aAvailableColumnsList = PropertyCollectionProps.getAvailablePropertyList();
    let iDestinationIndex = oDestination.index;
    let aDraggablePropertyListTabData = PropertyCollectionDraggableListTabsLayoutData().tabsList;
    let aShuffledColumns = [];

    CS.forEach(aDraggableIds, function (sId) {
      let oProperty = CS.find(aSelectedColumns, {id: sId}) || CS.find(aAvailableColumnsList, {id: sId});
      oProperty && aShuffledColumns.push(oProperty)
    });

    if(oSource.droppableId === "propertyList" && oDestination.droppableId === "propertySequenceList"){
      aSelectedColumns.splice(iDestinationIndex, 0, ...aShuffledColumns);
      CS.forEach(aShuffledColumns, function (oShuffledProperty) {
        let oAvailableProperty = CS.find(aAvailableColumnsList, {id:oShuffledProperty.id});
        let oActiveTab = CS.find(aDraggablePropertyListTabData, {type:oAvailableProperty.type});
        oAvailableProperty.type = oActiveTab ? oAvailableProperty.type : _getTypeFromBaseType(oAvailableProperty.type);
        oAvailableProperty.isAvailable = false;
      });
    }
    else if (oSource.droppableId === "propertySequenceList") {
      CS.remove(aSelectedColumns, function (oProperty) {
        return CS.includes(aDraggableIds, oProperty.id);
      });
      if (oDestination.droppableId === "propertySequenceList") {
        iDestinationIndex = (oSource.index < iDestinationIndex) ? iDestinationIndex - (aDraggableIds.length - 1) : iDestinationIndex;
        aSelectedColumns.splice(iDestinationIndex, 0, ...aShuffledColumns);
        let aModifiedColumnIds = [];
        aModifiedColumnIds.push(aDraggableIds[0]);
        let aShuffledColumnsIds = CS.concat(aModifiedColumnIds, PropertyCollectionProps.getPropertyCollectionDraggableModifiedPropertiesList());
        PropertyCollectionProps.setPropertyCollectionDraggableModifiedPropertiesList(aShuffledColumnsIds);
      } else {
        aAvailableColumnsList = CS.concat(aAvailableColumnsList, aShuffledColumns);
        CS.forEach(aShuffledColumns, function (oElements) {
          let oProperty = CS.find(aAvailableColumnsList, {id: oElements.id});
          oProperty.isAvailable = true
        });
        aAvailableColumnsList = CS.sortBy(aAvailableColumnsList, "label");
      }
    }
    _triggerChange();
  };

  let _handlePropertyCollectionDraggableListPropertyRemove = function (draggableIds) {
    let oActivePropertyCollection = _makeActivePropertyCollectionDirty();
    _setPropertyCollectionScreenLockedStatus(true);
    let aSelectedColumns = oActivePropertyCollection.elements;
    let aAvailableColumnsList = PropertyCollectionProps.getAvailablePropertyList();
    let aShuffledColumns = [];
    let aSelectedTabId = PropertyCollectionProps.getPropertyCollectionDraggableListActiveTabId();
    let aDraggablePropertyListTabData = PropertyCollectionDraggableListTabsLayoutData().tabsList;

    let oProperty = CS.find(aSelectedColumns, {id: draggableIds});
    let oActiveTabList = CS.find(aDraggablePropertyListTabData, {type:oProperty.type});
    oProperty.type = oActiveTabList ? oProperty.type : _getTypeFromBaseType(oProperty.type);
    oProperty && aShuffledColumns.push(oProperty);

    CS.remove(aSelectedColumns, function (oProperty) {
      return CS.includes(draggableIds, oProperty.id);
    });

    CS.forEach(aShuffledColumns, function (oElements) {
      let oProperty = CS.find(aAvailableColumnsList, {id: oElements.id});
      if(CS.isNotEmpty(oProperty)) {
        let oActiveTabList = CS.find(aDraggablePropertyListTabData, {type: oProperty.type});
        oProperty.type = oActiveTabList ? oProperty.type : _getTypeFromBaseType(oProperty.type);
        let oActiveTab = CS.find(aDraggablePropertyListTabData, {id: aSelectedTabId});
        if (oProperty.type === oActiveTab.type) {
          oProperty.isAvailable = true
        }
      }
    });

    aAvailableColumnsList = CS.sortBy(aAvailableColumnsList, "label");
    _triggerChange();
  };

  return {

    handleCommonConfigPropertyCollectionChanged: function (sKey, sVal) {
      let oPropertyCollectionToCreate = PropertyCollectionProps.getPropertyCollectionToCreate();
      if (CS.isNotEmpty(oPropertyCollectionToCreate)) {
        oPropertyCollectionToCreate[sKey] = sVal;
      }
      else {
        let oActivePropertyCollection = _makeActivePropertyCollectionDirty();
        oActivePropertyCollection[sKey] = sVal;
        PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(true);
      }
      _triggerChange();
    },

    getActiveClass: function () {
      return ClassUtils.getActiveClass();
    },

    getPropertyCollectionScreenLockedStatus: function () {
      return _getPropertyCollectionScreenLockedStatus();
    },

    fetchPropertyCollectionsList: function (bLoadMore) {
      _fetchPropertyCollectionsList(bLoadMore);
    },

    savePropertyCollection: function (oCallback) {
      _savePropertyCollection(oCallback);
    },

    saveUnsavedClass: function (oSwitchCallback, sContext) {
      _saveUnsavedClass(oSwitchCallback, sContext);
    },

    fetchPropertyCollectionDetails: function (oClassValue) {
      _fetchPropertyCollectionDetails(oClassValue);
    },

    createPropertyCollection: function (sContext) {
      let bIsClassScreenLocked = _getPropertyCollectionScreenLockedStatus();
      let oCreatedPropertyCollection = {
        id: UniqueIdentifierGenerator.generateUUID(),
        label: UniqueIdentifierGenerator.generateUntitledName(),
        code: "",
        isCreated: true
      };

      if(sContext == SettingScreenConstants.X_RAY_PROPERTY_COLLECTION) {
        oCreatedPropertyCollection.isForXRay = true;
      }

      if (bIsClassScreenLocked) {

        var oCallbackData = {};
        CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            _saveUnsavedClass.bind(this, oCallbackData),
            _discardUnsavedPropertyCollection.bind(this, oCallbackData),
            function () {
            }
        );
      }
      PropertyCollectionProps.setPropertyCollectionToCreate(oCreatedPropertyCollection);
      _setPropertyCollectionScreenLockedStatus(true);
      _triggerChange();

    },

    discardUnsavedPropertyCollection: function (oCallback, sContext) {
      _discardUnsavedPropertyCollection(oCallback, sContext);
    },

    deletePropertyCollection: function () {
      _deletePropertyCollections();
    },

/*
    copyPropertyCollectionNodeModel: function (oModel, oItem) {
      oItem.id = oModel.id;
      oItem.isChecked = oModel.isChecked;
      oItem.isEditable = oModel.isEditable;
      oItem.isSelected = oModel.isSelected;
      oItem.icon = oModel.icon;
      oItem.properties = oModel.properties;
    },
*/
/*

    handleClassTreeViewNodeValueChanged: function (oModel, sContext) {
      //SettingUtils.setBuildPendingStatus(true);

      var sClassName = oModel.label;
      var oValueList = _getClassValueListByTypeGeneric(sContext);
      //var oActiveClass = ClassUtils.getActiveClass();
      if (sClassName) {
        SettingUtils.setPropertyValue(oValueList[oModel.id], 'isEditable', false);
        var oActiveClass = ClassUtils.makeActiveClassDirty();
        oActiveClass.label = sClassName;
        _savePropertyCollection({});

      } else {
        alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      }
    },

   handleVisualAttributeDrop: function (oVisualAttribute, sContainerId) {
      var sSplitter = SettingUtils.getSplitter();
      var oElementMap;
      var iRowIndex = Number(sContainerId.split(sSplitter)[0]);
      var iColIndex = Number(sContainerId.split(sSplitter)[1]);

      let oActivePC = _getActivePropertyCollection();
      oActivePC = oActivePC.clonedObject || oActivePC;

      if (oVisualAttribute.id === 'taxonomy') {
        if (CS.find(oActivePC.elements, {position: {x: iRowIndex}})) {
          alertify.error(getTranslation().CLASS_CONFIG_ELEMENT_ALREADY_EXIST);
          return;
        }
      }
      let oSection = _makeActivePropertyCollectionDirty();

      switch (oVisualAttribute.id) {
        case "attribute":
          oElementMap = PropertyCollectionProps.getClassSectionAttributeMap();
          break;
        case "tag":
          oElementMap = PropertyCollectionProps.getClassSectionTagMap();
          break;
        case "taxonomy":
          oElementMap = PropertyCollectionProps.getSectionTaxonomyList();
          break;
      }


      if (!CS.find(oSection.elements, {position: {x: iRowIndex, y: iColIndex}})) {
        var oElement = {
          "id": oVisualAttribute.dataId ? oVisualAttribute.dataId : null,
          "type": oVisualAttribute.id,
          "position": {
            x: iRowIndex,
            y: iColIndex
          },
          "data": oVisualAttribute.attributeData
        };
        oElementMap[oElement.data.id].isAvailable = false;
        if (!CS.isEmpty(oSection)) {
          oSection.elements.push(oElement);
        }

        PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(true);
      }
      else {
        alertify.error(getTranslation().CLASS_CONFIG_ELEMENT_ALREADY_EXIST);
      }
    },

    handleSectionElementDroppedFromWithinSection: function (oVisualAttribute, sContainerId) {
      var sSplitter = SettingUtils.getSplitter();
      var sSectionElId = oVisualAttribute.dataId;
      var iRowIndex = Number(sContainerId.split(sSplitter)[0]);
      var iColIndex = Number(sContainerId.split(sSplitter)[1]);

      var oSectionFromDragged = _makeActivePropertyCollectionDirty();

      if (oVisualAttribute.id === 'taxonomy') {
        if (CS.find(oSectionFromDragged.elements, {position: {x: iRowIndex}})) {
          alertify.error(getTranslation().CLASS_CONFIG_ELEMENT_ALREADY_EXIST);
          return;
        }
      }

      if (!CS.find(oSectionFromDragged.elements, {position: {x: iRowIndex, y: iColIndex}})) {
        var oDraggedSectionElement = CS.remove(oSectionFromDragged.elements, {id: sSectionElId})[0];
        oDraggedSectionElement.position = {x: iRowIndex, y: iColIndex};
        oSectionFromDragged.elements.push(oDraggedSectionElement);
      } else {
        alertify.error(getTranslation().CLASS_CONFIG_ELEMENT_ALREADY_EXIST);
      }
      _triggerChange();
    },*/

    handleDeleteVisualElementIconClicked: function (oInfo) {
      if(oInfo.elementId === "assetcoverflowattribute"){
        alertify.error(getTranslation().CANNOT_REMOVE_ATTRIBUTE);
      }
      else {
        PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(true);
      }
    },

    handleVisualElementBlockerClicked: function (oInfo) {
    },

    handleSectionClicked: function (sSectionId) {
      PropertyCollectionProps.setSelectedSectionId(sSectionId);
    },

    handlePropertyCollectionSingleValueChanged: function (sKey, oVal) {
      var oActivePropertyCollection = _makeActivePropertyCollectionDirty();
      oActivePropertyCollection[sKey] = oVal;
      PropertyCollectionProps.setPropertyCollectionScreenLockedStatus(true);
      _triggerChange();
    },

    handlePropertyCollectionIconChanged: function (sIconKey, sIconObjectKey) {
      _handlePropertyCollectionIconChanged(sIconKey, sIconObjectKey);
    },

    updateClassSectionElementMap: function () {
      _createClassSectionElementMap();
      var oActivePropertyCollection = _getActivePropertyCollection();
      oActivePropertyCollection = oActivePropertyCollection.clonedObject || oActivePropertyCollection;
      _fillClassSectionElementMap(oActivePropertyCollection);
    },

    handlePropertyCollectionConfigDialogButtonClicked: function (sButtonId) {
      _handlePropertyCollectionConfigDialogButtonClicked(sButtonId);
    },

    handleListViewSearchOrLoadMoreClicked: function (sSearchText, bLoadMore, oCallBack) {
      var aPropertyCollectionList = SettingUtils.getAppData().getPropertyCollectionList();
      if (bLoadMore) {
        sSearchText = PropertyCollectionProps.getSearchText();
      }
      SettingUtils.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, aPropertyCollectionList, PropertyCollectionProps, oCallBack);
    },

    resetPaginationData: function () {
      _resetPaginationData();
    },

    handlePropertyCollectionImportButtonClicked: function (aFiles,oImportExcel) {
      _handlePropertyCollectionImportButtonClicked(aFiles,oImportExcel);
    },

    handlePropertyCollectionTabChanged: function (sTabId) {
      _handlePropertyCollectionTabChanged(sTabId);
    },

    handlePropertyCollectionExportButtonClicked: function (oSelectiveExport) {
      _handlePropertyCollectionExportButtonClicked(oSelectiveExport);
    },

    handlePropertyCollectionManageEntityButtonClicked: function () {
      _handlePropertyCollectionManageEntityButtonClicked();
    },

    handlePropertyCollectionDraggableListColumnsShuffled: function ( oSource, oDestination, aDraggableIds) {
      _handlePropertyCollectionDraggableListColumnsShuffled( oSource, oDestination, aDraggableIds);
    },

    handlePropertyCollectionDraggableListPropertyRemove: function (sDraggableIds) {
      _handlePropertyCollectionDraggableListPropertyRemove(sDraggableIds);
    },

    createClassSectionElementMap: function () {
      _createClassSectionElementMap();
    },

    fillClassSectionElementMap: function (oActivePropertyCollection) {
      _fillClassSectionElementMap(oActivePropertyCollection);
    }
  };

})();

MicroEvent.mixin(PropertyCollectionStore);

export default PropertyCollectionStore;
