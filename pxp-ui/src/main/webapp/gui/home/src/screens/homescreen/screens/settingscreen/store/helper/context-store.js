import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import ContextProps from './../model/context-config-view-props';
import SettingUtils from './../helper/setting-utils';
import ContextUtils from './../helper/context-utils';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import { ContextRequestMapping as oContextRequestMapping } from '../../tack/setting-screen-request-mapping';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import SettingScreenProps from './../model/setting-screen-props';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import ContextConfigGridViewSkeleton from './../../tack/context-config-grid-view-skeleton';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import assetTypes from '../../tack/coverflow-asset-type-list';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import SectionLayout from '../../tack/context-config-layout-data';
import ViewUtils from '../../view/utils/view-utils';

var ContextStore = (function () {

  var _triggerChange = function () {
    ContextStore.trigger('context-changed');
  };

  var _makeActiveContextDirty = function () {
    var sActiveContextId = _getActiveContext().id;
    var oAttributeMap = SettingUtils.getAppData().getContextList();
    var oActiveContext = oAttributeMap[sActiveContextId];
    SettingUtils.makeObjectDirty(oActiveContext);
    return oActiveContext.clonedObject;
  };

  var _getCurrentContext = function () {
    var oActiveContext = _getActiveContext();
    if (!CS.isEmpty(oActiveContext.clonedObject)) {
      return oActiveContext.clonedObject;
    } else {
      return oActiveContext;
    }
  };

  var _getActiveContext = function(){
    return ContextProps.getActiveContext();
  };

  var _setActiveContext = function (oContext) {
    ContextProps.setActiveContext(oContext);
  };

  var _createDefaultContextMasterObject = function () {
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      code: "",
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      icon: "",
      contextTags: [],
      editableProperties: [],
      type: ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT //create attribute variant context by default
    }
  };

  var _generateActiveContextADM = function (oActiveContext, oCurrentContext, oADM) {
    var aOldContextTags = oActiveContext.contextTags;
    var aNewContextTags = oCurrentContext.contextTags;
    let oAppData = SettingUtils.getAppData();

    CS.forEach(aNewContextTags, function (oNewContextTag) {

      var oActiveContextADM = {
        tagId: '',
        addedTagValueIds: [],
        deletedTagValueIds: []
      };
      var oOldContextTag = CS.find(aOldContextTags, {tagId: oNewContextTag.tagId});
      if(CS.isEmpty(oOldContextTag)) {
        var oAddedTags = {};
        oAddedTags.tagId = oNewContextTag.tagId;
        var oSelectedTagValues = [];
        CS.forEach(oNewContextTag.tagValues, function (oTagValue) {
          oTagValue.isSelected && oSelectedTagValues.push(oTagValue.tagValueId);
        });
        oAddedTags.tagValueIds = oSelectedTagValues;

        oADM.addedTags.push(oAddedTags);
      } else {
        var bIsModifiedFlag = false;
        CS.forEach(oOldContextTag.tagValues, function (oOldTagValue) {
          var oNewTagValue = CS.find(oNewContextTag.tagValues, {tagValueId: oOldTagValue.tagValueId});
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
      var oNewContextTags = CS.find(aNewContextTags, {tagId: oOldContextTags.tagId});
      if (CS.isEmpty(oNewContextTags)) {
        oADM.deletedTags.push(oOldContextTags.tagId);
      }
    });

    let oReferencedTabs = oCurrentContext.configDetails.referencedTabs || {};
    let oActiveContextTab = oReferencedTabs[oActiveContext.tabId];
    let oCurrentContextTab = oReferencedTabs[oCurrentContext.tabId] || {};
    let oCustomTabsADM = SettingUtils.generateADMForCustomTabs(oActiveContextTab, oCurrentContextTab);
    oADM.addedTab = oCustomTabsADM.addedTab;
    oADM.deletedTab = oCustomTabsADM.deletedTab;

    var sSplitter = SettingUtils.getSplitter();
    var oClassClone = CS.cloneDeep(oActiveContext);

    var aOldSections = oClassClone.sections;
    var aNewSections = oClassClone.clonedObject.sections;
    oADM.modifiedElements = [];
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

        var bIsSectionModified = !CS.isEqual(oNewSection.sequence, oOldSection.sequence);
        oNewSection.isModified = bIsSectionModified;

        //iterating on new section elements
        CS.forEach(aNewSectionElements, function(oNewSectionElement){
          var oOldSectionElement = CS.remove(aOldSectionElements, {id: oNewSectionElement.id});

          //if element found in old version
          if(oOldSectionElement.length > 0){

            //if element is not modified
            var bIsElementModified = !CS.isEqual(oOldSectionElement[0], oNewSectionElement);
            if (bIsElementModified) {
              oElementADMObject.modified.push(oNewSectionElement);
              oNewSectionElement.isModified = bIsElementModified;
            }
          } else {
            //not found in old so add to added list
            oNewSectionElement.id = oNewSectionElement.id.split(sSplitter)[0];
            oElementADMObject.added.push(oNewSectionElement);
          }
        });

        var aDeletedElements = CS.map(aOldSectionElements, 'id');
        CS.merge(oElementADMObject.deleted, aDeletedElements);

        oNewSection.addedElements = oElementADMObject.added;
        oNewSection.deletedElements = oElementADMObject.deleted;
        oNewSection.modifiedElements = oElementADMObject.modified;
        oADM.modifiedElements = oADM.modifiedElements.concat(oElementADMObject.modified);
        delete oNewSection.elements;

        if (bIsSectionModified) {
          oSectionADMObject.modified.push(oNewSection);
        }

      } else {
        oSectionADMObject.added.push(oNewSection);
      }
    });

    var aDeletedSections = CS.map(aOldSections, 'id');
    CS.merge(oSectionADMObject.deleted, aDeletedSections);

    oADM.addedSections = oSectionADMObject.added;
    oADM.deletedSections = oSectionADMObject.deleted;
    oADM.modifiedSections = [];
    CS.forEach(oSectionADMObject.modified, function (oModifiedSection) {
      oADM.modifiedSections.push({
        id: oModifiedSection.id,
        sequence: oModifiedSection.sequence,
        isModified: oModifiedSection.isModified
      });
      oADM.modifiedElements = oADM.modifiedElements.concat(oModifiedSection.modifiedElements);
    });

    var aOldSubContexts= oActiveContext.subContexts;
    var aNewSubContexts = oCurrentContext.subContexts;

    oADM.addedSubContexts = CS.difference(aNewSubContexts, aOldSubContexts);
    oADM.deletedSubContexts = CS.difference(aOldSubContexts, aNewSubContexts);

    var aOldEntitiesList = oActiveContext.entities || [];
    var aNewEntitiesList = oCurrentContext.entities || [];

    oADM.addedEntities = CS.difference(aNewEntitiesList, aOldEntitiesList);
    oADM.deletedEntities = CS.difference(aOldEntitiesList, aNewEntitiesList);

    var aOldUniqueTagSelection = oActiveContext.uniqueSelectors;
    var aNewUniqueTagSelection = oCurrentContext.uniqueSelectors;
    oADM.addedUniqueSelections = [];
    oADM.deletedUniqueSelections = [];

    CS.forEach(aNewUniqueTagSelection, function (oUniqueTagSelection) {
      var oFound = CS.find(aOldUniqueTagSelection, {id: oUniqueTagSelection.id});
      if(!oFound) {
        oADM.addedUniqueSelections.push(oUniqueTagSelection);
      }else if(SettingUtils.isUniqueSelectorModifiedForCombination(oFound,oUniqueTagSelection)){
        oUniqueTagSelection.id = UniqueIdentifierGenerator.generateUUID();
        oADM.addedUniqueSelections.push(oUniqueTagSelection);
      }
    });

    CS.forEach(aOldUniqueTagSelection, function (oOldUniqueTagSelection) {
      var oFound = CS.find(aNewUniqueTagSelection, {id: oOldUniqueTagSelection.id});
      if(!oFound) {
        oADM.deletedUniqueSelections.push(oOldUniqueTagSelection.id);
      }
    });


  };

  var _saveContext = function (oCallbackData) {
    var sActiveContextId = _getActiveContext().id;
    var oContextMap = SettingUtils.getAppData().getContextList();
    var oActiveContext = oContextMap[sActiveContextId];
    var flag = false;

    if (!(oActiveContext.isDirty || oActiveContext.isCreated)) {
      if (oCallbackData.functionToExecute) {
        //unreachable code
        oCallbackData.functionToExecute();
      }
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }

    let oSectionLayout = new SectionLayout();
    var aDisabledFields = ViewUtils.getAllKeysFromSectionLayout(oSectionLayout);
    if(oActiveContext.isContextUsed) {
      CS.remove(aDisabledFields ,(x)=> {return x =="label" || x == "icon"});
    }


    var oCurrentContext = {};
    if(!CS.isEmpty(oActiveContext)) {
      oCurrentContext = oActiveContext.isDirty ? oActiveContext.clonedObject : oActiveContext;
    }
    oCurrentContext.label = oCurrentContext.label.trim();
    if(SettingUtils.checkUniqueSelectorsDuplications(oCurrentContext)){
      alertify.error(getTranslation().DUPLICATE_COMBINATIONS_SELECTED);
      return;
    }
    if(CS.isEmpty(oCurrentContext.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    if (oCurrentContext.type === ContextTypeDictionary.LANGUAGE_VARIANT) {
      if (CS.isEmpty(oCurrentContext.contextTags)) {
        alertify.message(getTranslation().SELECT_AT_LEAST_ONE_TAG);
        return;
      } else if (CS.isEmpty(CS.filter(oCurrentContext.contextTags[0].tagValues, {isSelected: true}))) {
        alertify.message(getTranslation().SELECT_AT_LEAST_ONE_TAG_VALUE);
        return;
      }
    }

    var oServerCallback = {};
    oServerCallback.functionToExecute = oCallbackData.functionToExecute;

    if(SettingUtils.isLinkedTypeContext(oActiveContext.type)){
      oActiveContext.statusTags = [];
    }

    if(oContextMap[sActiveContextId].isCreated){
      var oCodeToVerifyUniqueness = {
          id: oCurrentContext.code,
          entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT
      };
      oServerCallback.isCreated = true;

      var oCallbackDataForUniqueness = {};
      oCallbackDataForUniqueness.functionToExecute = _createContextCall.bind(this, oCurrentContext, oServerCallback);

      var sURL = oContextRequestMapping.CheckEntityCode;
      SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);

    } else {
      var oADM = {
        id: oCurrentContext.id,
        code: oCurrentContext.code,
        label: oCurrentContext.label,
        icon: oCurrentContext.icon,
        type: oCurrentContext.type,
        defaultView: oCurrentContext.defaultView,
        isLimitedObject: oCurrentContext.isLimitedObject,
        isTimeEnabled: oCurrentContext.isTimeEnabled,
        isDuplicateVariantAllowed: oCurrentContext.isDuplicateVariantAllowed,
        defaultTimeRange: oCurrentContext.defaultTimeRange,
        //isDisabled : aDisabledFields,
        addedTags: [],
        modifiedTags: [],
        deletedTags: [],
        editableProperties: oCurrentContext.editableProperties,
        isAutoCreate: oCurrentContext.isAutoCreate
      };
      SettingUtils.removeEmptyTagGroupFromCurrentContext(oCurrentContext);
      _generateActiveContextADM(oActiveContext, oCurrentContext, oADM);

      SettingUtils.csPostRequest(oContextRequestMapping.save, {}, oADM, successSaveContextCallback.bind(this, oServerCallback), failureSaveContextCallback.bind(this, oServerCallback));
    }
  };

  var _getContextDetails = function (sSelectedContextId) {

    SettingUtils.csGetRequest(oContextRequestMapping.get, {id: sSelectedContextId}, successGetContextDetailsCallback.bind(this, sSelectedContextId), failureGetContextDetailsCallback.bind(this, sSelectedContextId));

  };

  var _setContextTagOrder = function (oActiveContext) {
    var aAllowedContextToSetTagOrder = [ContextTypeDictionary.CONTEXTUAL_VARIANT,
                                        ContextTypeDictionary.IMAGE_VARIANT];
    if(CS.includes(aAllowedContextToSetTagOrder, oActiveContext.type)) {
      var aContextTagOrder = [];
      CS.forEach(oActiveContext.contextTags, function (oTag) {
        var aSelectedTagValues = CS.filter(oTag.tagValues, {isSelected: true});
        !CS.isEmpty(aSelectedTagValues) && aContextTagOrder.push(oTag.tagId);
      });
      ContextProps.setContextTagOrder(aContextTagOrder);
    }
  };

  var successGetContextDetailsCallback = function (sContextId, oResponse) {
    let oContextFromServer = oResponse.success;

    SettingUtils.preProcessContextData(oContextFromServer);

    let oContextList = SettingUtils.getAppData().getContextList();
    let oContextFromList = oContextList[sContextId];

    if (!oContextFromServer.editableProperties) {
      oContextFromServer.editableProperties = [];
    }
    CS.assign(oContextFromList, oContextFromServer);

    delete oContextFromList.clonedObject;
    delete oContextFromList.isDirty;

    _setActiveContext(oContextFromList);
    _setContextTagOrder(oContextFromList);

    ContextProps.setIsContextDialogActive(true);
    _triggerChange();
  };

  var failureGetContextDetailsCallback = function (sContextId, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureGetContextDetailsCallback", getTranslation());
  };

  var successSaveContextCallback = function (oCallbackData, oResponse) {
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.CONTEXT_VARIANT);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();

    var oMasterContextList = SettingUtils.getAppData().getContextList();
    var sSelectedContextId = _getActiveContext().id;
    var oSavedContext = oResponse.success;

    if(oMasterContextList.hasOwnProperty(oSavedContext.id)){
      oMasterContextList[oSavedContext.id] = oSavedContext;
    }

    SettingUtils.preProcessContextData(oSavedContext);
    ContextProps.setActiveTagUniqueSelectionId("");
    if (!oSavedContext.contextTags) {
      oSavedContext.contextTags = [];
    }

    var oOldMasterContext = oMasterContextList[sSelectedContextId];
    if (oOldMasterContext.isCreated) {
      delete oMasterContextList[sSelectedContextId];
    }

    oMasterContextList[oSavedContext.id] = oSavedContext;

    _setActiveContext(oSavedContext);

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    let oScreenProps = SettingScreenProps.screen;
    if (oCallbackData.isCreated) {
      let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.CONTEXT_VARIANT, [oSavedContext])[0];
      aGridViewData.unshift(aProcessedGridViewData);
      let aContextData = ContextProps.getContextGridData();
      aContextData.push(oSavedContext);
      oGridViewPropsByContext.setGridViewTotalItems(oScreenProps.getGridViewTotalItems() + 1 );
      alertify.success(getTranslation().CONTEXT_CREATED_SUCCESSFULLY);
      SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);

      /** To open edit view immediately after create */
      successGetContextDetailsCallback(oSavedContext.id, oResponse);

    } else {
      GridViewStore.preProcessDataForGridView(GridViewContexts.CONTEXT_VARIANT, oMasterContextList);
      let oContextFromList = CS.find(aGridViewData, {id: oSavedContext.id});
      delete oContextFromList.clonedObject;
      delete oContextFromList.isDirty;
      _setActiveContext(oContextFromList);
      _setContextTagOrder(oContextFromList);
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().CONTEXT } ));
    }

    _triggerChange();
  };

  var failureSaveContextCallback = function (oCallback, oResponse){
    if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {key: "ContextConfigurationDependencyException"}))) {
       _discardUnsavedContext({},false);
      };
    SettingUtils.failureCallback(oResponse, "failureSaveContextCallback", getTranslation());
  };

  var _createContext = function (oCallbackData) {
    var oCreatedContextMaster = _createDefaultContextMasterObject();
    var sCreatedContextId = oCreatedContextMaster.id;

    var oContextList = SettingUtils.getAppData().getContextList();
    oContextList[sCreatedContextId] = oCreatedContextMaster;

    _setActiveContext(oCreatedContextMaster);
  };

  var _createContextCall = function (oCreatedContextMaster, oCallbackData) {
    SettingUtils.csPutRequest(oContextRequestMapping.create, {}, oCreatedContextMaster, successSaveContextCallback.bind(this, oCallbackData), failureSaveContextCallback.bind(this, oCallbackData));
  };

  var _getPropertyType =  function (sId) {
    var oAttributeMap = SettingUtils.getAppData().getAttributeList();
    var oTagMap = SettingUtils.getAppData().getTagMap();
    var oRoleMap = SettingUtils.getAppData().getRoleList();
    if (oAttributeMap[sId]) {
      return "attribute";
    }
    if (oTagMap[sId]) {
      return "tag";
    }
    if (oRoleMap[sId]) {
      return "role";
    }
    return null;
  };

  let _handleContextSingleValueChanged = function (sKey, oVal, oReferencedTabs) {
    var oActiveContext = _makeActiveContextDirty();
     // add new selected tab data in referencedTab
     oActiveContext.configDetails.referencedTabs = oReferencedTabs;
     oActiveContext[sKey] = oVal;
  };

  let _setUpContextConfigGridView = function () {
    //set skeleton
    var oContextConfigGridViewSkeleton = new ContextConfigGridViewSkeleton();
    let sContext = GridViewContexts.CONTEXT_VARIANT;
    GridViewStore.createGridViewPropsByContext(GridViewContexts.CONTEXT_VARIANT, {skeleton: oContextConfigGridViewSkeleton});
    _fetchContextListForGridView(sContext);
  }

  var _handleBeforeDeleteContext = function (aContextIds) {
    var sSelectedContextId = _getActiveContext().id;
    if (CS.indexOf(aContextIds, sSelectedContextId) >= 0) {
      _setActiveContext({});
    }
  };

  let _handleResetActiveContext = function (oItem, oMasterContextList, oSelectedContext={}){
    if(oSelectedContext.id && oSelectedContext.id == oItem.itemId){
      ContextProps.setActiveContext({});
    }
    delete oMasterContextList[oItem.itemId];
  };

  let handleDeleteContextFailure = function (List, oMasterContextList, oSelectedContext) {
    let oContextAlreadyDeleted = {exceptionType: "Context_already_deleted", items: []};
    let oContextInstancePresent = {exceptionType: "PriceContextExistsInAttributeException", items: []};
    let oUnhandledContext = {exceptionType: "Unhandled_Context", items: []};

    CS.forEach(List, function (oItem) {
      let sItemLabel = oMasterContextList[oItem.itemId].label;
      if (oItem.key == oContextAlreadyDeleted.exceptionType) {
        oContextAlreadyDeleted.items.push(sItemLabel);
        _handleResetActiveContext(oItem, oMasterContextList, oSelectedContext);
      } else if (oItem.key === oContextInstancePresent.exceptionType) {
        oContextInstancePresent.items.push(sItemLabel);
        _handleResetActiveContext(oItem, oMasterContextList, oSelectedContext);
      } else {
        oUnhandledContext.items.push(sItemLabel);
      }
    });

    CS.forEach([oContextAlreadyDeleted, oUnhandledContext, oContextInstancePresent], (oExceptions) => {
      if(CS.isNotEmpty(oExceptions.items)){
        alertify.error(Exception.getCustomMessage(oExceptions.exceptionType, getTranslation(), oExceptions.items.join(',')), 0)
      }
    });
  };

  var failureDeleteContextCallback = function (oCallback, oResponse) {
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
      var oMasterContextList = SettingUtils.getAppData().getContextList();
      var oSelectedContext = ContextProps.getActiveContext();
      handleDeleteContextFailure(oResponse.failure.exceptionDetails, oMasterContextList, oSelectedContext);

    } else {
      SettingUtils.failureCallback(oResponse, "failureDeleteContextCallback", getTranslation());
    }
    _triggerChange();
  };

  var successDeleteContextCallback = function (oResponse) {

    var oMasterContextList = SettingUtils.getAppData().getContextList();

    var aSuccessIds = oResponse.success;
    var aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    let GridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.CONTEXT_VARIANT);
    let oSkeleton = GridViewProps.getGridViewSkeleton();

    _handleBeforeDeleteContext(aSuccessIds);
    CS.forEach(aSuccessIds, function (id) {
      delete oMasterContextList[id];
      CS.remove(oSkeleton.selectedContentIds, function (oSelectedId) {
        return oSelectedId == id;
      });
    });

    if(aSuccessIds && aSuccessIds.length > 0) {
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.CONTEXT_VARIANT);
      let iTotalCount = oGridViewPropsByContext.getGridViewTotalItems() - aSuccessIds.length;
      GridViewStore.preProcessDataForGridView(GridViewContexts.CONTEXT_VARIANT, oMasterContextList,
          iTotalCount);
      alertify.success(getTranslation().CONTEXT_DELETE_SUCCESSFUL);
    }

    if(aFailureIds && aFailureIds.length > 0 ) {
      var oSelectedContext = ContextProps.getActiveContext();
      handleDeleteContextFailure(aFailureIds, oMasterContextList, oSelectedContext);
    }
  };

  var _deleteUnSavedContext = function (aList) {
    var oMasterList = SettingUtils.getAppData().getContextList();
    var aSavedContextIds = [];
    var aDeletedContextIds = [];

    CS.forEach(aList, function (uid) {
      if (oMasterList[uid].isCreated) {
        aDeletedContextIds.push(uid);
        delete oMasterList[uid];
      } else {
        aSavedContextIds.push(uid);
      }
    });

    var sSelectedContextId = _getActiveContext().id;
    if (CS.indexOf(aDeletedContextIds, sSelectedContextId) >= 0) {
      _setActiveContext({});
    }

    return aSavedContextIds;
  };

  var _deleteContexts = function (aBulkDeleteList, oCallBack) {
    var aFilteredContextIds = _deleteUnSavedContext(aBulkDeleteList);
    if (!CS.isEmpty(aFilteredContextIds)) {
      return SettingUtils.csDeleteRequest(oContextRequestMapping.delete, {}, {ids: aFilteredContextIds}, successDeleteContextCallback, failureDeleteContextCallback.bind(this, oCallBack));
    } else {
      alertify.success(getTranslation().Context_DELETE_SUCCESSFUL);
      _triggerChange();
    }
  };

  var _setContextTagCheckAllStatus = function (aTagValues, bStatus) {
    CS.forEach(aTagValues, function (oTag) {
      if(!CS.isEmpty(oTag)) {
        oTag.isSelected = bStatus;
      }
    });
  };

  var _handleContextTagCheckAllChanged =  function (sTagId) {
    var oActiveContext = _makeActiveContextDirty();
    var oContextTag = CS.find(oActiveContext.contextTags, {tagId: sTagId});
    if (!CS.isEmpty(oContextTag)) {
      var aTagValues = oContextTag.tagValues;
      var aSelectedTags = CS.filter(aTagValues, {isSelected: true});
      var iSelectedCout = aSelectedTags.length;
      if (iSelectedCout == 0) {
        _setContextTagCheckAllStatus(aTagValues, true);
      } else if (iSelectedCout != aTagValues.length) {
        _setContextTagCheckAllStatus(aTagValues, true);
      } else {
        _setContextTagCheckAllStatus(aTagValues, false);
      }
      _setContextTagOrder(oActiveContext);
    }
    _triggerChange();
  };

  var _handleDefaultFromDateValueChanged = function (sValue, sContext) {
    var oActiveContext = _makeActiveContextDirty();
    var oDefaultTimeRange = oActiveContext.defaultTimeRange || {};

    if(sContext == "fromDefaultCurrentDate") {
      oDefaultTimeRange.from = oDefaultTimeRange.to = null;
      oDefaultTimeRange.isCurrentTime = !oDefaultTimeRange.isCurrentTime;
    } else if(sContext == "fromDefaultDate") {
      oDefaultTimeRange.from = sValue;
      if(!oDefaultTimeRange.to) {
        oDefaultTimeRange.to = sValue;
      }
    } else if(sContext == "toDefaultDate") {
      oDefaultTimeRange.to = sValue;
    }
    _triggerChange();
  };

  var _handleContextualTagCombinationUniqueSelectionChanged = function (sUniqueSelectionId) {
    var oActiveContext = _getActiveContext();
    oActiveContext = oActiveContext.clonedObject || oActiveContext;
    ContextProps.setActiveTagUniqueSelectionId(sUniqueSelectionId);
    SettingUtils.updateUniqueSelectionOrder(oActiveContext, sUniqueSelectionId);
    _triggerChange();
  };

  var _handleContextualTagCombinationUniqueSelectionDelete = function (sUniqueSelectionId) {
    var oActiveContext = _makeActiveContextDirty();
    SettingUtils.deleteTagCombination(sUniqueSelectionId, oActiveContext);
    _triggerChange();
  };

  var _handleContextualTagCombinationUniqueSelectionOk = function () {
    var oActiveContext = _makeActiveContextDirty();
    SettingUtils.removeEmptyTagSelections(oActiveContext);
    ContextProps.setActiveTagUniqueSelectionId("");
    _triggerChange();
  };

  var _deleteContext = function (aSelectedIds, oCallBack) {
    var aContextIdsListToDelete = aSelectedIds;
    var oMasterContexts = SettingUtils.getAppData().getContextList();

    if (!CS.isEmpty(aContextIdsListToDelete)) {
      var aBulkDeleteContexts = [];
      let bCannotDelete = false;
      var sMessage = "";
      CS.forEach(aContextIdsListToDelete, function (iId) {
        var oMasterContext = oMasterContexts[iId];
        var sMasterContextLabel = CS.getLabelOrCode(oMasterContext);
        aBulkDeleteContexts.push(sMasterContextLabel);
        if (oMasterContext.isStandard) {
          bCannotDelete = true;
          sMessage = getTranslation().CANNOT_DELETE_SELECTED_VARIANT;
          return true;
        }
      });
      if (bCannotDelete) {
        alertify.message(sMessage);
      } else {
        CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteContexts,
          function () {
            _deleteContexts(aContextIdsListToDelete, oCallBack)
            .then(_fetchContextListForGridView);
          }, function (oEvent) {
          }, true);
      }

    } else {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
    }
  };

  let _fetchContextListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.CONTEXT_VARIANT);
   SettingUtils.csPostRequest(oContextRequestMapping.getAll, {}, oPostData, successFetchContextListForGridView, failureFetchContextListForGridView);
  }

  let successFetchContextListForGridView = function (oResponse) {
    let oResponseData = oResponse.success;
    let aContextList = oResponseData.list;
    SettingUtils.getAppData().setContextList(aContextList);
    ContextProps.setContextGridData(aContextList);
    GridViewStore.preProcessDataForGridView(GridViewContexts.CONTEXT_VARIANT, aContextList, oResponseData.count);
   _triggerChange();
  };

  var failureFetchContextListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchContextListForGridView", getTranslation());
  };

  var _handleContextUploadIconChangeEvent = function (sIconKey, sIconObjectKey) {
    var oActiveContext = _makeActiveContextDirty();
    oActiveContext.icon = sIconKey;
    oActiveContext.iconKey = sIconObjectKey;
    oActiveContext.showSelectIconDialog = false;
  };

  var _handleSectionAdded = function (aSectionIds){
    var oActiveContext = _makeActiveContextDirty();
    SettingUtils.handleSectionAdded(oActiveContext, aSectionIds);
    _saveContext({});
  };

  var _handleSectionDeleted = function (sSectionId){
    var oActiveContext = _makeActiveContextDirty();
    SettingUtils.handleSectionDeleted(oActiveContext, sSectionId);
    _saveContext({});
  };

  var _handleSectionMoveUpClicked = function (sSectionId){
    var oActiveContext = _getActiveContext();
    if (SettingUtils.handleSectionMoveUp(oActiveContext, sSectionId)) {
      _saveContext({});
    }
  };

  var _handleSectionMoveDownClicked = function (sSectionId){
    var oActiveContext = _getActiveContext();
    if (SettingUtils.handleSectionMoveDown(oActiveContext, sSectionId)) {
      _saveContext({});
    }
  };

  var _handleSelectionToggleButtonClicked = function (sKey, sId) {
    var oActiveContext = _makeActiveContextDirty();

    var aSelectedItems = oActiveContext[sKey] || [];
    if (CS.includes(aSelectedItems, sId)) {
      CS.remove(aSelectedItems, function (sDataId) {
        return sDataId == sId;
      });
    } else {
      aSelectedItems.push(sId);
    }
    _triggerChange();
  };

  var _handleContextCombinationMSSPopOver = function (sSelectorId, sTagId, aSelectedTags) {
    var oActiveContext = _makeActiveContextDirty();
    SettingUtils.setTagValueDataInTagCombination(oActiveContext, sSelectorId, sTagId, aSelectedTags)
    _triggerChange();
  };

  var _handleContextCombinationDeleteTagValue = function (sSelectorId, sTagId, sId) {
    var oActiveContext = _makeActiveContextDirty();
    SettingUtils.deleteTagValueFromCombination(oActiveContext, sSelectorId, sTagId, sId);
    _triggerChange();
  };

  var _getContextTagValues = function (oTag) {
    var aTagValues = [];
    CS.forEach(oTag.children, function (oChildTag) {
      var oMasterTagValue = {};
      oMasterTagValue.tagValueId = oChildTag.id;
      oMasterTagValue.label = oChildTag.label;
      oMasterTagValue.isSelected = true;
      oMasterTagValue.color = oChildTag.color;
      aTagValues.push(oMasterTagValue);
    });
    return aTagValues;
  };

  let _discardUnsavedContext = function (oCallbackData, showDiscardMessage = true) {
    var oActiveContext = _getActiveContext();
    var oMasterContextList = SettingUtils.getAppData().getContextList();
    var oSelectedContextInMaster = oMasterContextList[oActiveContext.id];

    if (!CS.isEmpty(oSelectedContextInMaster)) {
      if (oSelectedContextInMaster.isDirty) {

        delete oSelectedContextInMaster.clonedObject;
        delete oSelectedContextInMaster.isDirty;
        if(showDiscardMessage) {
          alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        }

      }
      if (oSelectedContextInMaster.isCreated) {

        delete oMasterContextList[oActiveContext.id];
        _getActiveContext({});
        if(showDiscardMessage) {
          alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        }
      }
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      _triggerChange();

    } else {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
    }
  };

  let _handleContextAddTagGroup = function (sTagId) {
    let oCallBackData = {functionToExecute: _triggerChange};
    let oActiveContext = _makeActiveContextDirty();
    ContextUtils.handleContextAddTagClicked(sTagId, oActiveContext, oCallBackData);
  };

  let _closeContextDialog = function () {
    ContextProps.setIsContextDialogActive(false);
    ContextProps.setActiveContext({});
    _triggerChange();
  };

  let _processProcessContextListAndSave = function (oCallBackData) {
    let aContextToSave = [];
    let bSafeToSave = GridViewStore.processGridDataToSave(aContextToSave, GridViewContexts.CONTEXT_VARIANT, ContextProps.getContextGridData());

    if (bSafeToSave) {
      return SettingUtils.csPostRequest(oContextRequestMapping.BulkSave, {}, aContextToSave, successBulkSaveContextList.bind(this, oCallBackData), failureBulkSaveContextList);
    }
  };

  let successBulkSaveContextList = function (oCallBackData, oResponse) {
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.CONTEXT_VARIANT);
    let aContextGridData = ContextProps.getContextGridData();
    let aGridData = oGridViewProps.getGridViewData();
    let oContextList = SettingUtils.getAppData().getContextList();
    let aResponseContextList = oResponse.success.list;
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.CONTEXT_VARIANT, aResponseContextList);
    /*------------------Updating Context Prop from Response---------------------*/
    CS.forEach(aResponseContextList, function (oData) {
      let contextId = oData.id;
      let iIndex = CS.findIndex(aContextGridData, {id: contextId});
      aContextGridData[iIndex] = oData;
      oContextList[contextId] = oData;
    });
    oGridViewProps.setIsGridDataDirty(false);

    /*------------------Updating Grid Prop---------------------*/
    CS.forEach(aProcessedGridViewData, function (oProcessedContext) {
      let iIndex = CS.findIndex(aGridData, {id: oProcessedContext.id});
      aGridData[iIndex] = oProcessedContext;
    });

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().CONTEXT } ));
    oCallBackData && oCallBackData.functionToExecute && oCallBackData.functionToExecute();
  };

  let failureBulkSaveContextList = function (oResponse) {
    SettingUtils.failureCallback(oResponse,"failureBulkSaveContextList",getTranslation());
  };

  let _discardContextListChanges = function (oCallbackData) {

    var aContextGridData = ContextProps.getContextGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.CONTEXT_VARIANT);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    var bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedAttribute, iIndex) {
      if (oOldProcessedAttribute.isDirty) {
        var oContext = CS.find(aContextGridData, {id: oOldProcessedAttribute.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.CONTEXT_VARIANT, [oContext])[0];
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

  let _handleAddTagCombinationsClicked = function () {
    let oClassContextProps = _makeActiveContextDirty();
    let sActiveTagUniqueSelectionId = SettingUtils.addNewTagCombination(oClassContextProps);
    if(!CS.isEmpty(sActiveTagUniqueSelectionId)){
      ContextProps.setActiveTagUniqueSelectionId(sActiveTagUniqueSelectionId);
    }
    _triggerChange();
  };

  let _handleContextConfigCombinationMSSPopOver = function(sSelectorId,sTagId,aSelectedItems) {
    let oActiveContext = _makeActiveContextDirty();
    SettingUtils.setTagValueDataInTagCombination(oActiveContext, sSelectorId, sTagId, aSelectedItems);
    _triggerChange();
  };

  var _handleExportContext = function (oSelectiveExportDetails) {
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

  var _handleContextFileUploaded = function (aFiles,oImportExcel) {
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

  /************************** PUBLIC ***********************************/
  return {

    getActiveContext: function() {
      return _getActiveContext();
    },

    setActiveContext: function(oContext) {
      return _setActiveContext(oContext);
    },

    saveContext: function (oCallbackData) {
      _saveContext(oCallbackData);
    },

    createContext: function(){
      _createContext();
      _triggerChange();
    },

    handleContextConfigDialogButtonClicked: function (sButtonId) {
      if (sButtonId == "save") {
        _saveContext({});
      } else if (sButtonId == "cancel") {
        _discardUnsavedContext({});
      } else {
        _closeContextDialog();
      }
    },

    handleAddTagCombinationsClicked: function () {
      _handleAddTagCombinationsClicked();
    },

    handleContextConfigCombinationMSSPopOver: function(sSelectorId,sTagId,aSelectedItems) {
      _handleContextConfigCombinationMSSPopOver(sSelectorId,sTagId,aSelectedItems)
    },

    handleContextTagGroupItemAddButtonClicked: function (sTagId) {
      var oTagMap = SettingUtils.getAppData().getTagMap();
      var oTag = oTagMap[sTagId];
      try {
        var oActiveContext = _makeActiveContextDirty();
        var oContextTag = {
          tagId: oTag.id,
          label: oTag.label,
          isMultiselect: oTag.isMultiselect,
          tagValues: _getContextTagValues(oTag)
        };
        oActiveContext.contextTags.push(oContextTag);
        CS.forEach(oActiveContext.uniqueSelectors, function (oUniqueSelector) {
          var oSelectionContextTag = CS.cloneDeep(oContextTag);
          oSelectionContextTag.tagValues = [];
          oUniqueSelector.selectionValues.push(oSelectionContextTag);
        });
        _setContextTagOrder(oActiveContext);
      }catch (oException) {
        ExceptionLogger.log(oException); //in case if tag is not found in the list
      }
      _triggerChange();
    },

    handlePropertyDrop: function (sContext, sDroppedId, sDraggedId) {
      if (sDraggedId == sDroppedId) {
        return;
      }
      var oActiveContext = _makeActiveContextDirty();
      var aEditableProperties = oActiveContext.editableProperties;
      var iDroppedIndex = 0;
      if (!CS.isEmpty(aEditableProperties)) {
        iDroppedIndex = CS.findIndex(aEditableProperties, {id: sDroppedId});
      }
      var oPropertyToAdd;
      if (sContext == "dragFromWithinEditableProperties") {
        oPropertyToAdd = CS.remove(aEditableProperties, {id: sDraggedId})[0];
      } else {
        var sType = _getPropertyType(sDraggedId);
        if (!sType) {
          return;
        }
        oPropertyToAdd = {
          id: sDraggedId,
          type: sType
        };
      }
      if (oPropertyToAdd) {
        aEditableProperties.splice(iDroppedIndex, 0, oPropertyToAdd);
      }

      _triggerChange();
    },

    handleContextTagCheckAllChanged: function (sContextTagId) {
      _handleContextTagCheckAllChanged(sContextTagId);
    },

    handleDefaultFromDateValueChanged: function (sValue, sContext) {
      _handleDefaultFromDateValueChanged(sValue, sContext);
    },

    handleContextConfigDialogButton: function (sContext) {
      var oActiveContext = _getActiveContext();
      switch (sContext) {
        case "create":
          _saveContext({});
          break;
        case "cancel":
          var oMasterContextList = SettingUtils.getAppData().getContextList();
          var sSelectedContextId = oActiveContext.id;
          let oOldMasterContext = oMasterContextList[sSelectedContextId];
          if (oOldMasterContext.isCreated) {
            delete oMasterContextList[sSelectedContextId];
          }
          _setActiveContext({});
          _triggerChange();
          break;
      }
    },

    deleteContext: function (aSelectedIds, oCallBack) {
      _deleteContext(aSelectedIds, oCallBack);
    },

    fetchContextListForGridView: function () {
      _fetchContextListForGridView();
    },

    handleContextUploadIconChangeEvent: function (sIconKey, sIconObjectKey) {
      _handleContextUploadIconChangeEvent(sIconKey, sIconObjectKey);
      _triggerChange();
    },

    handleCommonConfigAttributeChanged: function (sKey, sVal) {
      var oActiveContext = _makeActiveContextDirty();
      oActiveContext[sKey] = sVal;
      if(sKey == "isTimeEnabled" && !sVal){
        oActiveContext.defaultTimeRange = {
          from: null,
          isCurrentTime: null,
          to: null
        }
      }
      _triggerChange();
    },

    handleContextTypeChanged: function (sTypeId) {
      var oActiveContext = _makeActiveContextDirty();
      oActiveContext.type = sTypeId;
      if (sTypeId === ContextTypeDictionary.IMAGE_VARIANT) {
        oActiveContext.contextTags = []; //clear existing tags
        this.handleContextTagGroupItemAddButtonClicked("resolutiontag"); //add resolution tag //todo: hardcode alert!
      }
      _triggerChange();
    },

    handleSectionAdded: function (aSectionIds){
      _handleSectionAdded(aSectionIds);
    },

    handleSectionDeleted: function (sSectionId){
      _handleSectionDeleted(sSectionId);
    },

    handleSectionMoveUpClicked: function (sSectionId){
      _handleSectionMoveUpClicked(sSectionId);
    },

    handleSectionMoveDownClicked: function (sSectionId){
      _handleSectionMoveDownClicked(sSectionId);
    },

    handleSectionElementCheckboxToggled: function (sSectionId, sElementId, sProperty){
      var oActiveContext = _makeActiveContextDirty();
      SettingUtils.handleSectionElementCheckboxToggled(oActiveContext, sSectionId, sElementId, sProperty);
      _triggerChange();
    },

    handleSectionElementInputChanged: function (sSectionId, sElementId, sProperty, sNewValue){
      var oActiveContext = _makeActiveContextDirty();
      SettingUtils.handleSectionElementInputChanged(oActiveContext, sSectionId, sElementId, sProperty, sNewValue);
      _triggerChange();
    },

    handleVisualElementBlockerClicked: function (oInfo){
      var oActiveContext = _getCurrentContext();
      SettingUtils.handleVisualElementBlockerClicked(oActiveContext, oInfo, _triggerChange);
    },

    handleSelectionToggleButtonClicked: function (sKey, sId, bSingleSelect){
      _handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
    },

    handleContextCombinationMSSPopOver: function (sSelectorId,sTagId,aSelectedItems) {
      _handleContextCombinationMSSPopOver(sSelectorId,sTagId,aSelectedItems);
    },

    handleContextCombinationDeleteTagValue: function (sSelectorId,sTagId,sId) {
      _handleContextCombinationDeleteTagValue(sSelectorId,sTagId,sId);
    },

    handleContextualTagCombinationUniqueSelectionChanged: function (sUniqueSelectionId) {
      _handleContextualTagCombinationUniqueSelectionChanged(sUniqueSelectionId);
    },

    handleContextualTagCombinationUniqueSelectionDelete: function (sUniqueSelectionId) {
      _handleContextualTagCombinationUniqueSelectionDelete(sUniqueSelectionId);
    },

    handleContextualTagCombinationUniqueSelectionOk: function (sUniqueSelectionId) {
      _handleContextualTagCombinationUniqueSelectionOk(sUniqueSelectionId);
    },

    handleContextSingleValueChanged: function (sKey, oVal, oReferencedTabs) {
      _handleContextSingleValueChanged(sKey, oVal, oReferencedTabs);
      _triggerChange();
    },

    handleContextAddTagGroup: function (sTagId) {
      /**New functions for tag handling remove old function when everything works fine**/
      _handleContextAddTagGroup(sTagId)
    },

    handleContextAddOrRemoveTagValue: function (sTagGroupId, aCheckedItems) {
      ContextUtils.handleContextAddOrRemoveTagValue(sTagGroupId, aCheckedItems, _makeActiveContextDirty());
      _triggerChange();
    },

    handleRemoveSelectedTagGroupClicked: function (sTagGroupId) {
      ContextUtils.handleRemoveSelectedTagGroupClicked(sTagGroupId, _makeActiveContextDirty());
      _triggerChange();
    },

    setUpContextConfigGridView: function () {
      _setUpContextConfigGridView();
    },

    editButtonClicked: function (sEntityId) {
      _getContextDetails(sEntityId);
    },

    processProcessContextListAndSave: function (oCallBackData) {
      _processProcessContextListAndSave(oCallBackData)
          .then(_triggerChange);
    },

    discardContextListChanges: function (oCallbackData) {
      _discardContextListChanges(oCallbackData);

    },

    handleExportContext: function (oSelectiveExportDetails) {
      _handleExportContext(oSelectiveExportDetails);
    },

    handleContextFileUploaded: function (aFiles,oImportExcel) {
      _handleContextFileUploaded(aFiles,oImportExcel);
    },

  }
})();

MicroEvent.mixin(ContextStore);

export default ContextStore;
