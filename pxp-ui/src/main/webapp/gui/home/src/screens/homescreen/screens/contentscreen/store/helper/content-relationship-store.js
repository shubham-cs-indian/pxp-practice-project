import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import ContentUtils from './content-utils';
import ScreenModeUtils from './screen-mode-utils';
import ContentScreenProps from './../model/content-screen-props';
import AvailableEntityStore from './available-entity-store';
import Constants from '../../../../../../commonmodule/tack/constants';
import ContentScreenViewContextConstants from '../../tack/content-screen-view-context-constants';
import oFilterPropType from '../../tack/filter-prop-type-constants';
import RelationshipTypeDictionary from '../../../../../../commonmodule/tack/relationship-type-dictionary';
const getTranslation = ScreenModeUtils.getTranslationDictionary;
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

let ContentRelationshipStore = (function () {

  let _triggerChange = function () {
    ContentRelationshipStore.trigger('relationship-change');
  };

  let _getKeyToFindRelationshipByContextType = (sInnerViewContext) => {
    let sKeyToFindRelationship = "";
    switch (sInnerViewContext) {
      case ContentScreenViewContextConstants.NATURE_TYPE_RELATIONSHIP:
        sKeyToFindRelationship = "natureRelationships";
        break;
      case ContentScreenViewContextConstants.CONTENT_RELATIONSHIP:
        sKeyToFindRelationship = "contentRelationships";
        break;
    }
    return sKeyToFindRelationship;

  };

  let _removeElementFromSelectedRelationshipProps = function (sId, sCurrentRelationshipId) {
    let oSelectedRelationshipElements = ContentScreenProps.relationshipView.getSelectedRelationshipElements();
    let aSelectedIds = oSelectedRelationshipElements[sCurrentRelationshipId];
    let iIndex = CS.indexOf(aSelectedIds, sId);
    if (iIndex >= 0) {
      aSelectedIds.splice(iIndex, 1);
    }
  };

  let _toggleRelationshipThumbnailSelection = (oModel) => {
    let oRelationshipSelectedElements = ContentScreenProps.relationshipView.getSelectedRelationshipElements();
    let oCurrentRelationshipId = oModel.properties['currentRelationshipId'];
    let aSelectedRelationshipElements = oRelationshipSelectedElements[oCurrentRelationshipId];

    if (CS.isEmpty(aSelectedRelationshipElements)) {
      aSelectedRelationshipElements = [];
      aSelectedRelationshipElements.push(oModel.id);
      oRelationshipSelectedElements[oCurrentRelationshipId] = aSelectedRelationshipElements;
    } else {
      let oSelectedElement = CS.remove(aSelectedRelationshipElements, function (n) {
        return n == oModel.id;
      });
      if (CS.isEmpty(oSelectedElement)) {
        aSelectedRelationshipElements.push(oModel.id);
      }
    }
    _triggerChange();
  };

  let _deleteRelationshipElement = (oModel, oCallback) => {
    let oActiveEntity = ContentUtils.getActiveEntity();
    let sCurrentRelationshipId = oModel.properties['currentRelationshipId'];
    let sViewContext = oModel.properties['viewContext'] || "";
    let aSplitContext = CS.split(sViewContext, ContentUtils.getSplitter());
    let sInnerViewContext = aSplitContext[1];

    let sModelId = "";
    let sKeyToFindRelationship = _getKeyToFindRelationshipByContextType(sInnerViewContext);
    sModelId = oModel.id;

    let aActiveRelationships = oActiveEntity[sKeyToFindRelationship];
    let oActiveRelationship = CS.find(aActiveRelationships, {relationshipId: sCurrentRelationshipId});
    if(CS.isEmpty(oActiveRelationship)){
      oActiveRelationship = CS.find(aActiveRelationships, {sideId: sCurrentRelationshipId});
      sCurrentRelationshipId = ContentUtils.getRelationshipIdFromSideId(sCurrentRelationshipId);
    }
    if(CS.isEmpty(oActiveRelationship)){
      oActiveRelationship = CS.find(aActiveRelationships, {id: sCurrentRelationshipId});
    }

    let oNewRelationship = CS.cloneDeep(oActiveRelationship);
    CS.remove(oNewRelationship.elementIds, function (sId) {
      if (sId == sModelId) {
        return true;
      }
    });


    let oSaveRelationshipCallback = {};
    let oNewFilterContextForFetchAvailableEntities = {
      filterType: oFilterPropType.QUICKLIST,
      screenContext: oFilterPropType.QUICKLIST
    };
    let bIsAvailableEntityView = ContentUtils.getAvailableEntityViewStatus();
    if (bIsAvailableEntityView) {
      oSaveRelationshipCallback.functionToExecute = AvailableEntityStore.fetchAvailableEntities.bind(this, {filterContext: oNewFilterContextForFetchAvailableEntities});
    }

    oSaveRelationshipCallback.funcRemoveRelationshipElementFromSelectedProp = _removeElementFromSelectedRelationshipProps.bind(this, sModelId, oActiveRelationship.sideId);
    let sRelationshipTypeRelationship = ContentUtils.getRelationshipTypeById(sCurrentRelationshipId);
    let bIsNature = ContentUtils.isNatureRelationship(sRelationshipTypeRelationship) ||
        ContentUtils.isVariantRelationship(sRelationshipTypeRelationship);
    _saveRelationship([oActiveRelationship], [oNewRelationship], bIsNature, oSaveRelationshipCallback);

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
  };

  let _getReferencedRelationshipContexts = function (sRelationshipId) {
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedVariantContexts = oScreenProps.getReferencedVariantContexts();
    let oReferencedRelationshipContexts = oReferencedVariantContexts.relationshipVariantContexts;
    let oReferencedProductVariants = oReferencedVariantContexts.productVariantContexts;
    let oReferencedElements = oScreenProps.getReferencedElements();
    let oReferencedRelationship = oReferencedElements[sRelationshipId];
    let sRelationshipContextId = oReferencedRelationship.relationshipSide.contextId;

    let oReferencedRelationshipContext = null;
    if (sRelationshipContextId) {
      if (oReferencedRelationshipContexts && oReferencedRelationshipContexts[sRelationshipContextId]) {
        oReferencedRelationshipContext = oReferencedRelationshipContexts[sRelationshipContextId];
      } else if (oReferencedProductVariants && oReferencedProductVariants[sRelationshipContextId]) {
        oReferencedRelationshipContext = oReferencedProductVariants[sRelationshipContextId];
      }

      if(!CS.isEmpty(oReferencedRelationshipContext)){
        oReferencedRelationshipContext.actualRelationshipId = oReferencedRelationship.propertyId;
      }
    }

    return oReferencedRelationshipContext;
  };

  let _handleThumbnailRelationshipEditVariantIconClicked = function (oContext, oModel) {
    let oContent = ContentUtils.getActiveContent();
    if (oContent.contentClone) {
      alertify.error(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return true;
    }

    let sSideId = oModel.properties['currentRelationshipId'];
    let sRelationshipType = oModel.properties['currentRelationshipType'];
    let oOldRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let oRelationshipContext = _getReferencedRelationshipContexts(sSideId);

    let oReferencedElements = ContentScreenProps.screen.getReferencedElements();
    let oReferencedRelationshipElement = oReferencedElements[sSideId];
    let sRelationshipId = oReferencedRelationshipElement.propertyId;
    let bIsContentEditable = !ContentUtils.getIsContentDisabled();

    if (!CS.isEmpty(oOldRelationshipContextData) && CS.isEmpty(oOldRelationshipContextData.relationshipContentInstanceId)) {
      if (ContentUtils.isContextTypeProductVariant(oRelationshipContext.type)) {
        ContentUtils.showError(getTranslation().VARIANT_LINKING_IS_IN_PROGRESS);
      } else {
        ContentUtils.showError(getTranslation().VARIANT_CREATION_IN_PROGRESS);
      }
      return;
    }

    let oReferencedPermissions = ContentScreenProps.screen.getReferencedPermissions();
    let aCanEditContextRelationshipIds = oReferencedPermissions["canEditContextRelationshipIds"];
    let oRelationshipContextData = {};
    oRelationshipContextData.context = oRelationshipContext;
    oRelationshipContextData.relationshipId = sSideId;
    oRelationshipContextData.actualRelationshipId = sRelationshipId;
    oRelationshipContextData.tags = _getVariantTagsForLinkedRelationshipContent(sSideId, oModel.id, sRelationshipType);
    oRelationshipContextData.timeRange = _getVariantTimeRangeForLinkedRelationshipContent(sSideId, oModel.id, sRelationshipType);
    oRelationshipContextData.isVariantEditable = bIsContentEditable && CS.includes(aCanEditContextRelationshipIds, sRelationshipId); //to keep variant tag drawer view editable /
    // un-editable
    oRelationshipContextData.isForSingleContent = true;
    oRelationshipContextData.isEditVariantSectionOpen = true;
    oRelationshipContextData.relationshipContentInstanceId = oModel.id;
    oRelationshipContextData.type = sRelationshipType;

    ContentScreenProps.screen.setRelationshipContextData(oRelationshipContextData);
    ContentScreenProps.screen.setIsRelationshipContextDialogOpen(true);
    _triggerChange();
  };

  let _handleRelationshipContextCancelButtonClicked = function (sCancelButtonId, oCallbackData) {
    if(sCancelButtonId == 'cancel'){
      ContentScreenProps.screen.setRelationshipContextData({});
    }else{
      let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
      oRelationshipContextData.isEditVariantSectionOpen = false;

      if(oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    }

    ContentScreenProps.screen.setIsRelationshipContextDialogOpen(false);
    _triggerChange();
  };

  let _handleRelationshipContextSaveButtonClicked = function (sRelationshipId) {
    let oActiveContent = ContentUtils.getActiveContent();
    let oContentToSave = oActiveContent.contentClone || oActiveContent;
    let sRelationshipTypeRelationship = ContentUtils.getRelationshipTypeById(sRelationshipId);
    let bIsNatureRelationship = ContentUtils.isNatureRelationship(sRelationshipTypeRelationship)  ||
        ContentUtils.isVariantRelationship(sRelationshipTypeRelationship);
    let aOldRelationships = oActiveContent.contentRelationships;
    let aNewRelationships = oContentToSave.contentRelationships;

    if(bIsNatureRelationship) {
      aOldRelationships = oActiveContent.natureRelationships;
      aNewRelationships = oContentToSave.natureRelationships;
    }

    let fSaveRelationshipFunction = _saveRelationship.bind(this, aOldRelationships, aNewRelationships, bIsNatureRelationship);
    if (sRelationshipTypeRelationship === RelationshipTypeDictionary.PRODUCT_VARIANT) {
      ContentUtils.validateContextDuplication({functionToExecute : fSaveRelationshipFunction})
    } else {
      fSaveRelationshipFunction();
    }
  };

  let _getRelationshipKeyInEntityByType = function (sRelationshipType) {
    let sKeyToFindRelationShip = "contentRelationships";
    if (ContentUtils.isNatureRelationship(sRelationshipType) || ContentUtils.isProductVariantRelationship(sRelationshipType) || ContentUtils.isProductVariantRelationship(sRelationshipType)) {
      sKeyToFindRelationShip = "natureRelationships";
    }
    return sKeyToFindRelationShip;
  };

  let _getVariantTagsForLinkedRelationshipContent = function (sRelationshipSideId, sRelationshipContentId, sRelationshipType) {
    let oActiveEntity = ContentUtils.getActiveEntity();
    oActiveEntity = oActiveEntity.contentClone || oActiveEntity;
    let aTagsToReturn = [];

    let sKeyToFindRelationShip = _getRelationshipKeyInEntityByType(sRelationshipType);
    let oContentRelationship = CS.find(oActiveEntity[sKeyToFindRelationShip], {sideId: sRelationshipSideId});
    if (!CS.isEmpty(oContentRelationship) && !CS.isEmpty(oContentRelationship.elementTagMapping)) {
      let oElementTagMapping = oContentRelationship.elementTagMapping;
      aTagsToReturn = oElementTagMapping[sRelationshipContentId];
    }
    return aTagsToReturn;
  };

  let _getVariantTimeRangeForLinkedRelationshipContent = function (sRelationshipSideId, sRelationshipContentId, sRelationshipType) {
    let oActiveEntity = ContentUtils.getActiveEntity();
    oActiveEntity = oActiveEntity.contentClone || oActiveEntity;
    let oTimeRangeToReturn = {};
    let sKeyToFindRelationShip = _getRelationshipKeyInEntityByType(sRelationshipType);

    let oContentRelationship = CS.find(oActiveEntity[sKeyToFindRelationShip], {sideId: sRelationshipSideId});
    if (!CS.isEmpty(oContentRelationship)) {
      let oElementTimeRangeMapping = oContentRelationship.elementTimeRangeMapping;
      oTimeRangeToReturn = oElementTimeRangeMapping[sRelationshipContentId];
    }
    return oTimeRangeToReturn;
  };

  let _setRelationshipContextDataFromClone = function (oRelationshipContextData) {
    let sRelationshipId = oRelationshipContextData.relationshipId;
    let sRelationshipContentInstanceId = oRelationshipContextData.relationshipContentInstanceId;
    let sRelationshipType = oRelationshipContextData.type;
    oRelationshipContextData.tags = _getVariantTagsForLinkedRelationshipContent(sRelationshipId, sRelationshipContentInstanceId, sRelationshipType);
    oRelationshipContextData.timeRange = _getVariantTimeRangeForLinkedRelationshipContent(sRelationshipId, sRelationshipContentInstanceId, sRelationshipType);
  };

  let _handleVariantTagSummaryDateValueChanged = function (sKey, sValue) {

    let sOtherKey = (sKey == "to") ? "from" : "to";
    let sKeyOldValue, sOtherKeyOldValue;
    let oVariantObject = {};
    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let bIsRelationshipContext = !CS.isEmpty(oRelationshipContextData);
    if (bIsRelationshipContext) {
      //for contextual variant
      if (!oRelationshipContextData.isDummy) {
        ContentUtils.makeActiveContentDirty();
        oRelationshipContextData.isDirty = true;
        _setRelationshipContextDataFromClone(oRelationshipContextData);
      }
      oVariantObject.timeRange = oRelationshipContextData.timeRange;
    } else {
      oVariantObject = ContentUtils.makeActiveContentOrVariantDirty();
    }

    if (!oVariantObject.timeRange) {
      oVariantObject.timeRange = {
        to: null,
        from: null
      }
    }

    let oTimeRange = oVariantObject.timeRange;
    sKeyOldValue = oTimeRange[sKey];
    sOtherKeyOldValue = oTimeRange[sOtherKey];
    oTimeRange[sKey] = sValue;
    if(oTimeRange.to == Constants.INFINITE_DATE && !oTimeRange.from){
      ContentUtils.showError(getTranslation().FROM_SHOULD_BE_SELECTED);
      return;
    }

    if (sOtherKey === "to" && sValue) {
      sValue = ContentUtils.convertTimeStampToEOD(sValue);
      oTimeRange[sOtherKey] = oTimeRange[sOtherKey] || sValue;
    }

    if (sOtherKey === "from" && sValue) {
      sValue = ContentUtils.convertTimeStampToSOD(sValue);
      oTimeRange[sOtherKey] = oTimeRange[sOtherKey] || sValue;
    }

    if ((oTimeRange.from && oTimeRange.to) && (oTimeRange.from > oTimeRange.to)) {
      oTimeRange[sKey] = sKeyOldValue;
      oTimeRange[sOtherKey] = sOtherKeyOldValue;
      ContentUtils.showError(getTranslation().FROM_SHOULD_NOT_BE_GREATER_THAN_TO);
    }
    if (bIsRelationshipContext) {
      oRelationshipContextData.timeRange = oTimeRange;
      if (oRelationshipContextData.isForSingleContent) {
        let oTempModifiedRelationshipContextData = ContentScreenProps.screen.getModifiedRelationshipsContextTempData();
        let oTempObject = oTempModifiedRelationshipContextData[oRelationshipContextData.relationshipContentInstanceId];
        oTempObject = CS.isEmpty(oTempObject) ? {} : oTempObject;
        oTempObject.timeRange = oTimeRange;
        oTempModifiedRelationshipContextData[oRelationshipContextData.relationshipContentInstanceId] = oTempObject;
      }
    }

  };

  let _handleSelectAllRelationshipEntityClicked = function (sRelationshipId, sViewContext) {
    let oActiveEntity = ContentUtils.getActiveEntity();

    let oRelationshipSelectedElements = {};
    let aSplitContext = CS.split(sViewContext, ContentUtils.getSplitter());
    let sInnerViewContext = aSplitContext[1];
    let sKeyToFindRelationship = _getKeyToFindRelationshipByContextType(sInnerViewContext);

    oRelationshipSelectedElements = ContentScreenProps.relationshipView.getSelectedRelationshipElements();

    let aContentRelationships = oActiveEntity[sKeyToFindRelationship];
    let oRelationship = CS.find(aContentRelationships, function (oRelationship) {
      return oRelationship.sideId == sRelationshipId;
    });
    if (!oRelationship) {
      oRelationship = CS.find(aContentRelationships, function (oRelationship) {
        return oRelationship.id == sRelationshipId;
      });
    }

    if (!CS.isEmpty(oRelationship)) {
      let aElementIds = oRelationship.elementIds;
      oRelationshipSelectedElements[sRelationshipId] || (oRelationshipSelectedElements[sRelationshipId] = []);
      let aSelectedRelationshipElements = oRelationshipSelectedElements[sRelationshipId];
      /*** De-select all only if all entities are selected. ***/
      if (aSelectedRelationshipElements.length) {
        oRelationshipSelectedElements[sRelationshipId] = [];
      } else {
        oRelationshipSelectedElements[sRelationshipId] = [];
        CS.forEach(aElementIds, function (sId) {
          oRelationshipSelectedElements[sRelationshipId].push(sId);
        });
      }
    }
    _triggerChange();
  };

  let _deleteRelationshipElements = function(aElementList, aSelectedRelationshipElements, sSideId, sViewContext, oCallback, fSuccessHandler) {
    let aNames = [];
    let aElementIds = [];
    CS.forEach(aElementList, function (oElement) {
      if (CS.includes(aSelectedRelationshipElements, oElement.id)) {
        aNames.push(oElement.name);
        aElementIds.push(oElement.id);
      }
    });

    if (aNames.length) {
      ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_UNLINK, aNames,
          function () {
            _handleDeleteAllRelationshipElements(aElementIds,sSideId, sViewContext, oCallback, fSuccessHandler);
          }, function (oEvent) {
          });
    } else {
      alertify.message(getTranslation().STORE_ALERT_NOTHING_SELECTED);
    }
  }

  let _handleDeleteAllRelationshipEntityClicked = function (sRelationshipId, sViewContext, oCallback) {
    let oContent = ContentUtils.getActiveContent();
    if (oContent.contentClone) {
      alertify.error(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return true;
    }

    let oRelatedElements = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
    let aElementList = oRelatedElements[sRelationshipId] ? oRelatedElements[sRelationshipId].elements : [];
    let oRelationshipSelectedElements = ContentScreenProps.relationshipView.getSelectedRelationshipElements();
    let aSelectedRelationshipElements = oRelationshipSelectedElements[sRelationshipId];
    _deleteRelationshipElements(aElementList, aSelectedRelationshipElements, sRelationshipId, sViewContext, oCallback);
  };

  let _handleDeleteAllRelationshipElements = function (aSelectedRelationshipElements,sCurrentRelationshipId, sViewContext, oCallback,fSuccesshandler) {
    let aSplitContext = CS.split(sViewContext, ContentUtils.getSplitter());
    let sInnerViewContext = aSplitContext[1];
    let sKeyToFindRelationship = "";

    let oActiveEntity = ContentUtils.getActiveContent();
    let aDeletedElementsInRelationship = [];
    let bIsNatureRelationship = false;
    switch (sInnerViewContext) {
      case ContentScreenViewContextConstants.NATURE_TYPE_RELATIONSHIP:
        sKeyToFindRelationship = "natureRelationships";
        bIsNatureRelationship = true;
        break;

      case ContentScreenViewContextConstants.CONTENT_RELATIONSHIP:
        sKeyToFindRelationship = "contentRelationships";
        break;
    }
    let oRelationshipSelectedElements = ContentScreenProps.relationshipView.getSelectedRelationshipElements();


    let aActiveEntityRelationships = oActiveEntity[sKeyToFindRelationship];
    let aNewClonedRelationship = CS.cloneDeep(aActiveEntityRelationships);

    let oClonedRelationship = CS.find(aNewClonedRelationship, {sideId: sCurrentRelationshipId});
    if (!oClonedRelationship) {
      oClonedRelationship = CS.find(aNewClonedRelationship, {id: sCurrentRelationshipId});
    }
    let aRelationshipElements = oClonedRelationship.elementIds;
    CS.forEach(oClonedRelationship.elementIds, function (sId) {
      if (CS.includes(aSelectedRelationshipElements, sId)) {
        aDeletedElementsInRelationship.push(sId);
      }
    });
    CS.forEach(aDeletedElementsInRelationship, function (sId) {
      CS.pull(aRelationshipElements, sId);
    });

    oClonedRelationship.elementIds = aRelationshipElements;

    let oSaveContentCallback = {};
    let bAvailableEntityView = ContentUtils.getAvailableEntityViewStatus();
    let oNewFilterContextForFetchAvailableEntities = {
      filterType: oFilterPropType.QUICKLIST,
      screenContext: oFilterPropType.QUICKLIST
    };
    if (bAvailableEntityView) {
      oSaveContentCallback.functionToExecute = AvailableEntityStore.fetchAvailableEntities.bind(this, {filterContext: oNewFilterContextForFetchAvailableEntities});
    }
    oRelationshipSelectedElements[sCurrentRelationshipId] = [];
    _saveRelationship(aActiveEntityRelationships, aNewClonedRelationship, bIsNatureRelationship, oSaveContentCallback, fSuccesshandler);
  };

  let failureSaveRelationshipCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureSaveRelationshipCallback',  getTranslation());
  };

  let _generateADMForRelationships = function (aCurrentRelationship, aNewRelationship, bIsNature, oRelationshipADMData = {}) {
    let oADMForRelationships =  ContentUtils.generateADMForRelationships(aCurrentRelationship, aNewRelationship, bIsNature );
      oRelationshipADMData.addedNatureRelationships = oRelationshipADMData.addedNatureRelationships || [];
      oRelationshipADMData.modifiedNatureRelationships = oRelationshipADMData.modifiedNatureRelationships || [];
      oRelationshipADMData.deletedNatureRelationships= [];
      oRelationshipADMData.addedRelationships = oRelationshipADMData.addedRelationships || [];
      oRelationshipADMData.modifiedRelationships = oRelationshipADMData.modifiedRelationships || [];
      oRelationshipADMData.deletedRelationships= [];

      if(bIsNature) {
        oRelationshipADMData.addedNatureRelationships = oADMForRelationships.added;
        oRelationshipADMData.modifiedNatureRelationships = oADMForRelationships.modified;
      } else {
        oRelationshipADMData.addedRelationships = oADMForRelationships.added;
        oRelationshipADMData.modifiedRelationships = oADMForRelationships.modified;
      }
  };

  let _saveRelationshipCall = function (fSuccessCallBack, oSaveContentCallback, oContentDataToSave) {
    let sSelectedTabId = ContentUtils.getSelectedTabId();
    let oContent = ContentUtils.getActiveContent();
    oContentDataToSave.id = oContent.id;
    oContentDataToSave.baseType = oContent.baseType;
    oContentDataToSave.tabId = sSelectedTabId;
    oContentDataToSave.tabType = ContentUtils.getTabTypeFromTabId(sSelectedTabId);

    if(oSaveContentCallback.isManuallyCreated) {
      oContentDataToSave.isManuallyCreated = oSaveContentCallback.isManuallyCreated;
    }

    let fSuccessHandler = !CS.isFunction(fSuccessCallBack) ? ContentUtils.getContentStore().successSaveContentsCallback : fSuccessCallBack;

    let sRelationshipId = "";
    if (!CS.isEmpty(oContentDataToSave.modifiedRelationships)) {
      sRelationshipId = oContentDataToSave.modifiedRelationships[0].sideId;
    } else if (!CS.isEmpty(oContentDataToSave.modifiedNatureRelationships)) {
      sRelationshipId = oContentDataToSave.modifiedNatureRelationships[0].sideId;
    }

    let oCallbackData = {};
    oCallbackData.functionToExecute = () => {
      CS.isFunction(oSaveContentCallback.functionToExecute) && oSaveContentCallback.functionToExecute();
      CS.isFunction(oSaveContentCallback.funcRemoveRelationshipElementFromSelectedProp) && oSaveContentCallback.funcRemoveRelationshipElementFromSelectedProp();
      _resetRelationshipPaginationData(sRelationshipId); // eslint-disable-line
    };

    oContentDataToSave.from = 0;
    oContentDataToSave.size = 20;

    let sScreenContext = ContentUtils.getScreenModeBasedOnEntityBaseType(oContent.baseType);
    let sUrl = getRequestMapping(sScreenContext).saveRelationship;
    let fSuccess = fSuccessHandler.bind(this, oSaveContentCallback);
    return CS.postRequest(sUrl, {}, oContentDataToSave, fSuccess, failureSaveRelationshipCallback);
  };

  let _saveRelationship = function (aCurrentRelationship, aNewRelationship, bIsNatureRelationship, oSaveContentCallback = {}, fSuccessCallBack) {
    let oScreenProps = ContentScreenProps.screen;
    let oRelationshipContextData = oScreenProps.getRelationshipContextData();
    if (!CS.isEmpty(oRelationshipContextData) && !ContentUtils.validateVariantContextSelection(oRelationshipContextData, oRelationshipContextData.context)) {
      return;
    }

    let oRelationshipADMData = {};
    _generateADMForRelationships(aCurrentRelationship, aNewRelationship, bIsNatureRelationship, oRelationshipADMData);
    return _saveRelationshipCall(fSuccessCallBack, oSaveContentCallback, oRelationshipADMData);
  };

  var _fetchRelationship = function (sId, sRelationshipIdToFetchData, bIsLoadMore, sPaginationContext, oPaginationData, oCallbackData) {

    var oAppData = ContentUtils.getAppData();
    var oActiveEntity = ContentUtils.getActiveEntity() || {};
    let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);
    var oRelationshipToolbarProps = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
    var oRelationshipSortProp = oRelationshipToolbarProps[sId];
    var iSize = ContentScreenProps.screen.getDefaultPaginationInnerLimit();
    var iStartIndex = 0;
    var iCurrentElementsCount = 0;
    if (oRelationshipSortProp) {
      if (bIsLoadMore) {
        iStartIndex = oRelationshipSortProp.startIndex;
      }
      iSize = oRelationshipSortProp.paginationLimit;
      iCurrentElementsCount = oRelationshipSortProp.elements.length;
    }
    var oFilterProps = ContentScreenProps.screen.getContentFilterProps();
    var aTagsWithNoZeroValues = ContentUtils.removeZeroValuesFromFilterTags(oFilterProps.selectedTags);

    var sSortField = oRelationshipSortProp.sortField;
    var sSortOrder = oRelationshipSortProp.sortOrder;
    var isAttribute = false;
    var isNumeric = false;
    var aCustomAttribute = oAppData.getAttributeList();
    var oAttribute = CS.find(aCustomAttribute, {id: sSortField});
    if(oAttribute) {
      isAttribute = true;
      isNumeric = ContentUtils.isAttributeTypeNumeric(oAttribute.type);
    }
    var oArticleProps = ContentScreenProps.articleViewProps;
    var oArticleFolderVisibility = oArticleProps.getArticleFolderVisibility();

    if(CS.isEmpty(oPaginationData)) {
      var oPagination = _getPaginationData(sPaginationContext, oRelationshipSortProp);
      iSize = oPagination.size;
      iStartIndex = oPagination.from;
    } else {
      oRelationshipToolbarProps[sId].paginationLimit = oPaginationData.pageSize;
      iSize = oPaginationData.pageSize;
      iStartIndex = oPaginationData.from;
    }

    let oFilterData = {};
    let sUrl = "";
      sUrl = getRequestMapping(sScreenMode).GetRelationshipElements;
      let sContextId = "";
      let oSelectedContext = ContentScreenProps.variantSectionViewProps.getSelectedContext();
      if (!CS.isEmpty(oSelectedContext)) {
        sContextId = oSelectedContext.id;
      }
      let sIdToFetchType = sRelationshipIdToFetchData || sId;
      let sRelationshipType = ContentUtils.getRelationshipTypeById(sIdToFetchType);

      let bIsNatureRelationship = ContentUtils.isNatureRelationship(sRelationshipType);
      let sRelationshipId = ContentUtils.getRelationshipIdFromSideId(sId);
      oFilterData = {
        attributes: oFilterProps.selectedAttributes,
        tags: aTagsWithNoZeroValues,
        allSearch: oFilterProps.allSearch,
        size: iSize,
        from: iStartIndex,
        sortField: sSortField,
        sortOrder: sSortOrder,
        getFolders: oArticleFolderVisibility.showFolders,
        getLeaves: oArticleFolderVisibility.showArticles,
        isAttribute: isAttribute,
        isNumeric: isNumeric,
        selectedRoles: oFilterProps.selectedRoles,
        relationshipId: sRelationshipId,
        sideId: sId,
        baseType: oActiveEntity.baseType,
        contextId: sContextId,
        isLinked: true,
        isNatureRelationship: bIsNatureRelationship
      };

    let oXRayData = ContentUtils.getXRayPostData(sId);
    if (!CS.isEmpty(oXRayData)) {
      CS.assign(oFilterData, oXRayData);
    }

    var oCallback = {};
    CS.assign(oCallback, oCallbackData);

    return CS.postRequest(sUrl, {id: oActiveEntity.id}, oFilterData,
        successFetchRelationshipCallback.bind(this, sId, bIsLoadMore, iStartIndex, oCallback), failureFetchRelationshipCallback);
  };

  var _getPaginationData = function (sPaginationContext, oRelationshipSortProp) {
    let iSize = oRelationshipSortProp.paginationLimit;
    let iStartIndex = oRelationshipSortProp.startIndex;
    var iCurrentElementsCount = oRelationshipSortProp.elements.length;
    if(!CS.isEmpty(sPaginationContext)){

      var iTotalContents = oRelationshipSortProp.totalCount;
      switch (sPaginationContext) {
        case "PREVIOUS":
          iStartIndex -= iSize;
          if (iStartIndex < 0) {
            iStartIndex = 0;
          }
          break;

        case "NEXT":
          iStartIndex += iCurrentElementsCount;
          break;

        case "TOP":
          iStartIndex = 0;
          break;

        case "BOTTOM":
          var iRemainder = (iTotalContents%iSize);
          iStartIndex = (iRemainder === 0) ? (iTotalContents - iSize) : (iTotalContents - iRemainder);
          if (iStartIndex < 0) {
            iStartIndex = 0;
          }
          break;
      }
    }

    return {
      from: iStartIndex,
      size: iSize
    }
  };

  var successFetchRelationshipCallback = function (sId, bIsLoadMore, iStartIndex, oCallbackData, oResponse) {
    var oActiveEntity = ContentUtils.getActiveEntity();
    if (CS.isEmpty(oActiveEntity)) {
      return;
    }
    oResponse = oResponse.success;
    var oReferenceRelationshipInstanceElements = oResponse.referenceRelationshipInstanceElements;
    var aServerContentRelationship = oResponse.contentRelationships;
    var oReferenceNatureRelationshipInstanceElements = oResponse.referenceNatureRelationshipInstanceElements;
    var aServerNatureRelationship = oResponse.natureRelationships;

    var oXRayConfigDetails = oResponse.xrayConfigDetails || {};
    var oReferencedAttributes = ContentScreenProps.screen.getReferencedAttributes();
    var oReferencedTags = ContentScreenProps.screen.getReferencedTags();
    CS.assign(oReferencedAttributes, oXRayConfigDetails.referencedAttributes || {});
    CS.assign(oReferencedTags, oXRayConfigDetails.referencedTags || {});

    _updateRelationshipDataInContent(aServerContentRelationship, aServerNatureRelationship);
    /**For ContentRelationship**/
    _setPropsForFetchedRelationship(aServerContentRelationship, sId, oReferenceRelationshipInstanceElements, bIsLoadMore, oResponse);
    /**For NatureRelationship**/
    _setPropsForFetchedRelationship(aServerNatureRelationship, sId, oReferenceNatureRelationshipInstanceElements, bIsLoadMore, oResponse);

    let oRelationshipProps = ContentScreenProps.relationshipView;
    oRelationshipProps.setReferenceNatureRelationshipInstanceElements(oReferenceRelationshipInstanceElements);

    var oRelationshipToolbarProps = oRelationshipProps.getRelationshipToolbarProps();
    var sRelationshipType = ContentUtils.getRelationshipTypeById(sId);

    let sKey = (ContentUtils.isNatureRelationship(sRelationshipType) || ContentUtils.isVariantRelationship(sRelationshipType)) ?
                 "natureRelationships" : "contentRelationships";
    let oEntityRelationship = CS.find(oActiveEntity[sKey], {sideId: sId});
    let sKeyToProcess = "sideId";

    oEntityRelationship.elements = oRelationshipToolbarProps[sId].elements;
    oEntityRelationship.elementIds = CS.map(oRelationshipToolbarProps[sId].elements, "id");
    ContentUtils.addEntityInformationData(oEntityRelationship.elements);

    var oSelectedRelationshipElements = oRelationshipProps.getSelectedRelationshipElements();
    oSelectedRelationshipElements[oEntityRelationship[sKeyToProcess]] = [];

    if (ContentUtils.isNatureRelationship(sRelationshipType)) {
      var oReferenceNatureRelationshipInstanceElementsProps = oRelationshipProps.getReferenceNatureRelationshipInstanceElements();
      oReferenceNatureRelationshipInstanceElementsProps[sId] = oReferenceNatureRelationshipInstanceElements[sId];
    }

    if(!CS.isEmpty(oReferenceRelationshipInstanceElements)) {
      ContentUtils.prepareConcatenatedAttributeExpressionListForProducts(oReferenceRelationshipInstanceElements[sId], oReferencedAttributes, oReferencedTags, ContentScreenProps.screen.getReferencedElements());
    } else if(!CS.isEmpty(oReferenceNatureRelationshipInstanceElements)){
      ContentUtils.prepareConcatenatedAttributeExpressionListForProducts(oReferenceNatureRelationshipInstanceElements[sId], oReferencedAttributes, oReferencedTags, ContentScreenProps.screen.getReferencedElements());
    }

    if (oCallbackData) {
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    }
    _triggerChange();

    return true;
  };

  var _updateRelationshipDataInContent = function (aServerContentRelationship, aServerNatureRelationships) {
    var oActiveEntity = ContentUtils.getCurrentEntity();
    let oRelationshipProps = ContentScreenProps.relationshipView;
    var oRelationshipToolbarProps = oRelationshipProps.getRelationshipToolbarProps();
    CS.forEach(aServerNatureRelationships, function (oNatureRelationship) {
      var sRelationshipId = oNatureRelationship.relationshipId;
      var oExistRelationship = CS.find(oActiveEntity.natureRelationships, {relationshipId: sRelationshipId});
      if(oExistRelationship) {
        CS.remove(oActiveEntity.natureRelationships, {relationshipId: sRelationshipId});
      }
      if (CS.isEmpty(oRelationshipToolbarProps[sRelationshipId])) {
        oRelationshipProps.addNewRelationshipToolbarPropById(sRelationshipId);
      }
      oActiveEntity.natureRelationships.push(oNatureRelationship);
    });


    CS.forEach(aServerContentRelationship, function (oContentRelationship) {
      var sRelationshipId = oContentRelationship.relationshipId;
      var oExistRelationship = CS.find(oActiveEntity.contentRelationships, {relationshipId: sRelationshipId});
      if(oExistRelationship) {
        CS.remove(oActiveEntity.contentRelationships, {relationshipId: sRelationshipId});
      }
      if (CS.isEmpty(oRelationshipToolbarProps[sRelationshipId])) {
        oRelationshipProps.addNewRelationshipToolbarPropById(sRelationshipId);
      }
      oActiveEntity.contentRelationships.push(oContentRelationship);
    });

    /**** "ContentUtils.setRelationshipElements(oActiveEntity);" is commented because it reset the RelationshipToolbarProps,
     * because of this ZoomIn and ZoomOut not working in Relationship Section***/
  };

  var _setPropsForFetchedRelationship = function(aServerRelationship, sId, oReferenceRelationshipInstanceElements, bIsLoadMore, oResponseData) {
    var oServerRelationship = CS.find(aServerRelationship, {sideId: sId});
    if (!CS.isEmpty(oServerRelationship)) {
      var aElements = oReferenceRelationshipInstanceElements[oServerRelationship.sideId] || [];
      ContentScreenProps.relationshipView.getReferencedCommonRelationshipInstanceElements()[sId] = aElements;
      var oRelationshipToolbarProps = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
      var oRelationshipProp = oRelationshipToolbarProps[sId];
      if (bIsLoadMore && !CS.isEmpty(aElements)) {
        oRelationshipProp.startIndex = oResponseData.from;
      }
      oRelationshipProp.elements = aElements;
      oRelationshipProp.totalCount = oServerRelationship.totalCount;
    }
  };

  var failureFetchRelationshipCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse,'failureFetchRelationshipCallback',getTranslation());
    return false;
  };

  return {

    saveRelationship: function (aCurrentRelationship, aNewRelationship, bIsNatureRelationship, oSaveContentCallback,fSuccessCallBack) {
      return _saveRelationship(aCurrentRelationship, aNewRelationship, bIsNatureRelationship, oSaveContentCallback,fSuccessCallBack);
    },

    generateADMForRelationships: function (aCurrentRelationship, aNewRelationship, bIsNatureRelationship, oRelationshipADMData) {
      _generateADMForRelationships(aCurrentRelationship, aNewRelationship, bIsNatureRelationship,  oRelationshipADMData)
    },

    saveRelationshipCall: function (fSuccessCallBack, oSaveContentCallback, oRelationshipADMData) {
      return _saveRelationshipCall(fSuccessCallBack, oSaveContentCallback, oRelationshipADMData)
    },

    toggleRelationshipThumbnailSelection: (oModel) => {
      _toggleRelationshipThumbnailSelection(oModel);
    },

    deleteRelationshipElement: function (oModel, oCallback) {
      ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_UNLINK, [oModel.label],
          function () {
            _deleteRelationshipElement(oModel, oCallback);
          }, function (oEvent) {
          });
    },

    handleThumbnailRelationshipEditVariantIconClicked: function (oContext, oModel) {
      _handleThumbnailRelationshipEditVariantIconClicked(oContext, oModel);
    },

    handleRelationshipContextCancelButtonClicked: function (sCancelButtonId, oCallbackData) {
      _handleRelationshipContextCancelButtonClicked(sCancelButtonId, oCallbackData);
    },

    handleRelationshipContextSaveButtonClicked: function (sRelationshipId) {
      _handleRelationshipContextSaveButtonClicked(sRelationshipId);
    },

    handleVariantTagGroupDateValueChanged: function (sKey, sValue) {
      _handleVariantTagSummaryDateValueChanged(sKey, sValue);
    },

    setRelationshipContextDataFromClone: function (oRelationshipContextData) {
      _setRelationshipContextDataFromClone(oRelationshipContextData);
    },

    handleSelectAllRelationshipEntityClicked: function (sRelationshipId, sViewContext) {
      _handleSelectAllRelationshipEntityClicked(sRelationshipId, sViewContext);
    },

    handleDeleteAllRelationshipEntityClicked: function (sRelationshipId, sViewContext, oCallback) {
      _handleDeleteAllRelationshipEntityClicked(sRelationshipId, sViewContext, oCallback);
    },

    fetchRelationship: function (sId, sRelationshipIdToFetchData, oCallbackData) {
      return _fetchRelationship(sId, sRelationshipIdToFetchData, false, "", {}, oCallbackData);
    },

    loadMoreRelationship: function (sId, sPaginationContext, oPaginationData, oCallBack) {
      _fetchRelationship(sId, "", true, sPaginationContext, oPaginationData, oCallBack);
    },
  }

})();


MicroEvent.mixin(ContentRelationshipStore);

export default ContentRelationshipStore;
