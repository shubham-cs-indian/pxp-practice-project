import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ScreenModeUtils from './screen-mode-utils';
import ContentUtils from './content-utils';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import { gridViewPropertyTypes as GridViewPropertyTypes } from '../../../../../../viewlibraries/tack/view-library-constants';
import TemplateTabsDictionary from '../../../../../../commonmodule/tack/template-tabs-dictionary';
import SystemLevelIdDictionary from '../../../../../../commonmodule/tack/system-level-id-dictionary';
import BaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import CouplingConstants from '../../../../../../commonmodule/tack/coupling-constans';
import ContentScreenProps from './../model/content-screen-props';
import ContentGridProps from './../model/content-grid-props';
import GridEditViewProps from './../model/grid-edit-view-props';
import {getTranslations as oTranslations} from "../../../../../../commonmodule/store/helper/translation-manager";
import BreadCrumbStore from "../../../../../../commonmodule/store/helper/breadcrumb-store";

let getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
let getTranslation = ScreenModeUtils.getTranslationDictionary;

let ContentGridStore = (() => {

  let _triggerChange = () => {
    ContentGridStore.trigger('content-grid-change');
  };

  /******************* PRIVATE API  *******************/

  let _fetchContentGridData = () => {
    let aSelectedContentIds = ContentUtils.getSelectedContentIds();
    let aContentList = ContentUtils.getAppData().getContentList();
    let aKlassInstances = [];
    CS.forEach(aSelectedContentIds, function (sContentId) {
      let oContent = CS.find(aContentList, {id: sContentId});
      aKlassInstances.push({
            id: sContentId,
            baseType: oContent.baseType
          });
    });

    let oRequestData = {
      klassInstances: aKlassInstances,
      moduleId: ContentUtils.getSelectedModuleId(),
      selectedPropertyIds: ContentGridProps.getSelectedEditableProperties() || [],
    };

    CS.postRequest(getRequestMapping().GetContentGridData, {}, oRequestData,
        successFetchContentGridDataCallback, failureFetchContentGridDataCallback);
  };

  let successFetchContentGridDataCallback = (oResponse) => {
    let oInstancesData = oResponse.success;
    _preProcessDataForContentGrid(oInstancesData);
    ContentGridProps.setInstancesData(oInstancesData);
    _triggerChange();
  };

  let failureFetchContentGridDataCallback = (oResponse) => {
    ContentUtils.failureCallback(oResponse, "failureFetchContentGridDataCallback", getTranslation);
  };

  let _getGridPropertyForAttribute = (oAttributeInstance, oReferencedAttribute, oReferencedElement, oKlassInstance) => {
    let oGridProperty = {
      value: oAttributeInstance.value ? oAttributeInstance.value : ''
    };

    let sType = oReferencedAttribute.type;

    if (ContentUtils.isAttributeTypeMeasurement(sType)) {
      oGridProperty.defaultUnit = oReferencedAttribute.defaultUnit;
      oGridProperty.defaultUnitAsHTML = oReferencedAttribute.defaultUnitAsHTML;
      oGridProperty.precision = oReferencedAttribute.precision;
      oGridProperty.type = oReferencedAttribute.type;
      oGridProperty.converterVisibility = !ContentUtils.isAttributeTypePrice(sType);
      oGridProperty.disableValueConversionByDefaultUnit = ContentUtils.isAttributeTypePrice(sType);
      oGridProperty.rendererType = GridViewPropertyTypes.MEASUREMENT;
      oGridProperty.disableNumberLocaleFormatting = oReferencedAttribute.hideSeparator;

    } else if (ContentUtils.isAttributeTypeNumber(sType)) {
      oGridProperty.precision = oReferencedElement.precision == null ? oReferencedAttribute.precision : oReferencedElement.precision;
      oGridProperty.rendererType = GridViewPropertyTypes.NUMBER;
      oGridProperty.disableNumberLocaleFormatting = oReferencedAttribute.hideSeparator;
    } else if (ContentUtils.isAttributeTypeCalculated(sType)) {
      oGridProperty.precision = oReferencedAttribute.precision;
      oGridProperty.type = oReferencedAttribute.type;
      oGridProperty.converterVisibility = false;
      oGridProperty.rendererType = GridViewPropertyTypes.MEASUREMENT;
      oGridProperty.isDisabled = true;
      oGridProperty.disableNumberLocaleFormatting = oReferencedAttribute.hideSeparator;
      let sCalculatedAttributeType = oReferencedAttribute.calculatedAttributeType;
      if (!CS.isEmpty(sCalculatedAttributeType)) {
        oGridProperty.type = sCalculatedAttributeType;
        oGridProperty.defaultUnit = oReferencedAttribute.calculatedAttributeUnit;
        oGridProperty.converterVisibility = true;
      }

    } else if (ContentUtils.isAttributeTypeConcatenated(sType)) {
      oGridProperty.rendererType = GridViewPropertyTypes.TEXT;
      oGridProperty.isDisabled = true;

    } else if (ContentUtils.isAttributeTypeDate(sType)) {
      if (ContentUtils.isAttributeTypeCreatedOn(sType) || ContentUtils.isAttributeTypeLastModified(sType)) {
        oGridProperty.isDisabled = true;
      }
      oGridProperty.rendererType = GridViewPropertyTypes.DATE;

    } else if (ContentUtils.isAttributeTypeHtml(sType)) {
      oGridProperty.value = oAttributeInstance && oAttributeInstance.valueAsHtml || "";
      oGridProperty.valueAsHtml = oAttributeInstance && oAttributeInstance.value || "";
      oGridProperty.toolbarIcons = (oReferencedAttribute.validator && oReferencedAttribute.validator.allowedRTEIcons) || [];
      oGridProperty.rendererType = GridViewPropertyTypes.HTML;
      oGridProperty.fixedToolbar = false;
      oGridProperty.characterLimit = oReferencedAttribute.characterLimit;

    } else if (ContentUtils.isAttributeTypeUser(sType)) {
      oGridProperty.isDisabled = true;
      oGridProperty.rendererType = GridViewPropertyTypes.TEXT;

    } else {
      oGridProperty.rendererType = GridViewPropertyTypes.TEXT;
    }

    let bIsContentAvailableInSelectedLanguage = !ContentUtils.getIsContentDisabled(oKlassInstance);
    if (!bIsContentAvailableInSelectedLanguage ||
        (!CS.isEmpty(oReferencedElement) &&
            (
                oReferencedElement.couplingType === CouplingConstants.DYNAMIC_COUPLED ||
                oReferencedElement.couplingType === CouplingConstants.READ_ONLY_COUPLED ||
                oReferencedElement.isDisabled || oReferencedAttribute.isDisabled
            )
        )
    ) {
      oGridProperty.isDisabled = true;
    }

    if (oAttributeInstance.attributeId == SystemLevelIdDictionary.NameAttributeId) {
      oGridProperty.bIsMultiLine = false;
      oGridProperty.value = ContentUtils.getContentName(oKlassInstance);
    }

    return oGridProperty;
  };

  let _getGridPropertyForTag = (oTagInstance, oReferencedTag, oReferencedElement, oKlassInstance) => {
    let oGridProperty = {
      tag: oTagInstance
    };

    let bIsContentAvailableInSelectedLanguage = !ContentUtils.getIsContentDisabled(oKlassInstance);
    if (!bIsContentAvailableInSelectedLanguage ||
        (!CS.isEmpty(oReferencedElement) &&
            (
            oReferencedElement.couplingType === CouplingConstants.DYNAMIC_COUPLED ||
            oReferencedElement.isDisabled || oReferencedTag.isDisabled ||
            oReferencedElement.couplingType === CouplingConstants.READ_ONLY_COUPLED)
        )
    ) {
      oGridProperty.isDisabled = true;
    }

    return oGridProperty;
  };

  let _addDummyNameAttributeInstanceInGridData = function (oGridPropertyData, oKlassInstanceData) {
    let oKlassInstance = oKlassInstanceData.klassInstance;

    oGridPropertyData[SystemLevelIdDictionary.NameAttributeId] = {
      isDisabled: true,
      showTooltip: true,
      value: oKlassInstance.name, // add creation language name in value
      rendererType: GridViewPropertyTypes.TEXT
    };
  };

  let _addToGridPropertyDataFromKlassInstance = (oKlassInstanceData, oGridPropertyData, oReferencedAttributes, oReferencedTags) => {
    let oKlassInstance = oKlassInstanceData.klassInstance;
    let oReferencedElements = oKlassInstanceData.referencedElements;
    let oAlreadyAddedElementsMap = {}; //to check weather element is already added to grid property or not
    let aAttributes = oKlassInstance.attributes;
    let bIsNameAttributeInstancePresent = false;

    CS.forEach(aAttributes, function (oAttributeInstance) {
      try {
        let oReferencedAttribute = oReferencedAttributes[oAttributeInstance.attributeId];
        let oReferencedElement = oReferencedElements[oAttributeInstance.attributeId];

        if ((oAttributeInstance.attributeId != SystemLevelIdDictionary.NameAttributeId) &&
            (CS.isEmpty(oReferencedElement) || CS.isEmpty(oReferencedAttribute))) {
          return; //continue with next iteration
        }

        oGridPropertyData[oAttributeInstance.attributeId] = _getGridPropertyForAttribute(oAttributeInstance, oReferencedAttribute, oReferencedElement, oKlassInstance);
        if (!CS.isEmpty(oGridPropertyData[SystemLevelIdDictionary.NameAttributeId])) {
          oGridPropertyData[SystemLevelIdDictionary.NameAttributeId].isDisabled = !oKlassInstanceData.isNameEditable;
          oGridPropertyData[SystemLevelIdDictionary.NameAttributeId].showTooltip= true;
          bIsNameAttributeInstancePresent = true;
        }
        oAlreadyAddedElementsMap[oAttributeInstance.attributeId] = oReferencedElement;
      } catch(exception) {
        ExceptionLogger.error(exception);
      }
    });

    /** Hack Work : added dummy name attribute for grid editing of its instance not present **/
    let bIsContentAvailableInSelectedLanguage = !ContentUtils.getIsContentDisabled(oKlassInstance);
    if(!(bIsNameAttributeInstancePresent || bIsContentAvailableInSelectedLanguage)) {
      _addDummyNameAttributeInstanceInGridData(oGridPropertyData, oKlassInstanceData);
    }

    CS.forEach(oKlassInstance.tags, function (oTagInstance) {
      try {
        let oReferencedElement = oReferencedElements[oTagInstance.tagId];

        if (CS.isEmpty(oReferencedElement)) {
          return; //continue with next iteration
        }

        /**Add tag values into entity tag from tag references */
        let oReferencedTag = CS.find(oReferencedTags, {id: oTagInstance.tagId});
        ContentUtils.addTagsInContentBasedOnTagType(oReferencedTag, oTagInstance);

        oGridPropertyData[oTagInstance.tagId] = _getGridPropertyForTag(oTagInstance, oReferencedTag, oReferencedElement, oKlassInstance);
        oGridPropertyData[oTagInstance.tagId].customRequestObject = {
          klassInstanceId: oKlassInstance.id,
          baseType: oKlassInstance.baseType
        };
        oGridPropertyData[oTagInstance.tagId].showDefaultIcon = true;
        oAlreadyAddedElementsMap[oTagInstance.tagId] = oReferencedElement;
      }
      catch(exception) {
        ExceptionLogger.error(exception);
      }
    });

    CS.forEach(oReferencedElements, function (oReferencedElement) {
      try {
        if (CS.isEmpty(oAlreadyAddedElementsMap[oReferencedElement.id])) {
          switch (oReferencedElement.type) {
            case "attribute":
              let oMasterAttribute = oReferencedAttributes[oReferencedElement.id];
              let oAttributeInstance = ContentUtils.createAttributeInstanceObject("", oMasterAttribute.id);
              oGridPropertyData[oMasterAttribute.id] = _getGridPropertyForAttribute(oAttributeInstance, oMasterAttribute, oReferencedElement, oKlassInstance);
              break;

            case "tag":
              let oMasterTag = oReferencedTags[oReferencedElement.id];
              let oTagInstance = ContentUtils.createTagInstanceObject(oMasterTag, false);
              oGridPropertyData[oMasterTag.id] = _getGridPropertyForTag(oTagInstance, oMasterTag, oReferencedElement, oKlassInstance);
              oGridPropertyData[oMasterTag.id].showDefaultIcon = true;
              break;
          }
        }
      }
      catch(exception) {
        ExceptionLogger.error(exception);
      }
    });
  };

  let _preProcessDataForContentGrid = (oInstancesData) => {
    let oReferencedAttributes = oInstancesData.referencedAttributes;
    let oReferencedTags = oInstancesData.referencedTags;
    let aGridViewData = [];
    let oGridVisualData = {};
    let aPropertySequenceLists = oInstancesData.gridPropertySequenceList;
    CS.forEach(oInstancesData.klassInstances, (oKlassInstanceData, iIndex) => {
      let oKlassInstance = oKlassInstanceData.klassInstance;
      let oGridPropertyData = {};


      _addToGridPropertyDataFromKlassInstance(oKlassInstanceData, oGridPropertyData, oReferencedAttributes, oReferencedTags);

      aGridViewData.push({
        id: oKlassInstance.id,
        isExpanded: false,
        actionItemsToShow: [],
        children: [],
        properties: oGridPropertyData
      });

      oGridVisualData[oKlassInstance.id] = {
        isExpanded: false
      };

    });

    let aGridViewFixedColumns = [];
    let aGridViewScrollableColumns = [];
    let aGridViewScrollableColumnsSequence = [];
    let aSelectedEditablePropertiesSequence = [];


    CS.forEach(oReferencedAttributes, function (oReferencedAttribute) {
      if (oReferencedAttribute.id === SystemLevelIdDictionary.NameAttributeId) {
        aGridViewFixedColumns.push({
          id: oReferencedAttribute.id,
          code: oReferencedAttribute.code,
          label: oReferencedAttribute.label,
          type: GridViewPropertyTypes.FLEXIBLE,
          isVisible: true,
          width: 200,
          iconKey: oReferencedAttribute.iconKey,
          showDefaultIcon: true
        });
        return; //continue next iteration
      }

      aGridViewScrollableColumns.push({
        id: oReferencedAttribute.id,
        code: oReferencedAttribute.code,
        label: oReferencedAttribute.label,
        type: GridViewPropertyTypes.FLEXIBLE,
        isVisible: true,
        width: 200,
        iconClassName: "Attribute",
        iconKey: oReferencedAttribute.iconKey,
        showDefaultIcon: true
      });
    });

    CS.forEach(oReferencedTags, function (oReferencedTag) {
      aGridViewScrollableColumns.push({
        id: oReferencedTag.id,
        label: CS.getLabelOrCode(oReferencedTag),
        type: GridViewPropertyTypes.TAG,
        isVisible: true,
        width: 200,
        extraData: {
          referencedTag: oReferencedTag
        },
        iconClassName: "Tag",
        iconKey: oReferencedTag.iconKey,
        showDefaultIcon: true
      });
    });


    CS.forEach(aPropertySequenceLists, (sPropertyId) => {
      let oColumnData = CS.find(aGridViewScrollableColumns, {id: sPropertyId});
      if (oColumnData) {
        aSelectedEditablePropertiesSequence.push(oColumnData.id);
        aGridViewScrollableColumnsSequence.push(oColumnData)
      }
    });

    ContentGridProps.setGridViewSkeleton({
      actionItems: [],
      fixedColumns: aGridViewFixedColumns,
      scrollableColumns: aGridViewScrollableColumnsSequence,
      selectedContentIds: [],
    });
    ContentGridProps.setGridViewData(aGridViewData);
    ContentGridProps.setGridViewVisualData(oGridVisualData);
    ContentGridProps.setSelectedEditableProperties(aSelectedEditablePropertiesSequence);
    let oScreenProps = ContentScreenProps.screen;
    let oOldReferencedAttributes = oScreenProps.getReferencedAttributes();
    let oOldReferencedTags = oScreenProps.getReferencedTags();
    CS.assign(oOldReferencedAttributes, oReferencedAttributes || {});
    CS.assign(oOldReferencedTags, oReferencedTags || {});
  };

  let _getActiveContentForEditing = (sContentId) => {
    let oActiveContentForEditing = ContentGridProps.getActiveContentForEditing();

    if (CS.isEmpty(oActiveContentForEditing) || oActiveContentForEditing.id != sContentId) {
      let oInstanceData = _getInstanceDataByContentId(sContentId);
      oActiveContentForEditing = CS.cloneDeep(oInstanceData.klassInstance);
      ContentGridProps.setActiveContentForEditing(oActiveContentForEditing);
      ContentGridProps.setActiveContentId(sContentId);
    }

    return oActiveContentForEditing;
  };

  let _handleGridTagPropertyEdit = (sContentId, sTagId, aTagValueRelevanceData) => {
    let oActiveContent = _getActiveContentForEditing(sContentId);

    let oTagGroup = CS.find(oActiveContent.tags, {tagId: sTagId});
    if (CS.isEmpty(oTagGroup)) {
      let oInstancesData = ContentGridProps.getInstancesData();
      let oReferencedTags = oInstancesData.referencedTags;
      let oReferencedTag = oReferencedTags[sTagId];
      oTagGroup = ContentUtils.createTagInstanceObject(oReferencedTag, false);
      oActiveContent.tags.push(oTagGroup);
    }

    ContentUtils.getUpdatedEntityTag(oActiveContent, aTagValueRelevanceData, sTagId);

    let aGridViewData = ContentGridProps.getGridViewData();
    let oGridRow = CS.find(aGridViewData, {id: sContentId});
    let oProperty = oGridRow.properties[sTagId];
    oProperty.tag = CS.find(oActiveContent.tags, {tagId: sTagId});
    oGridRow.isDirty = true;
  };

  let _handleGridPropertyChanged = (sContentId, sPropertyId, sValue) =>{
    let aGridViewData = ContentGridProps.getGridViewData();
    let oGridRow = CS.find(aGridViewData, {id: sContentId});
    let oProperty = oGridRow.properties[sPropertyId];

    if (!CS.isEmpty(oProperty) && !CS.isEqual(oProperty.value, sValue)) {
      oProperty.value = sValue;
      oGridRow.isDirty = true;
      ContentGridProps.setIsGridDataDirty(true);
    }
  };

  let _handleGridPropertyAttributeValueChanged = (sContentId, sPropertyId, sValue) => {
    if (sPropertyId == 'nameattribute' && !sValue) {
        return;
    }

    let aGridViewData = ContentGridProps.getGridViewData();
    let oGridRow = CS.find(aGridViewData, {id: sContentId});
    let oProperty = oGridRow.properties[sPropertyId];
    let oInstancesData = ContentGridProps.getInstancesData();
    let oReferencedAttributes = oInstancesData.referencedAttributes;
    let oReferencedAttribute = oReferencedAttributes[sPropertyId];
    let oKlassInstance = _getActiveContentForEditing(sContentId);
    let oAttribute = CS.find(oKlassInstance.attributes, {attributeId: sPropertyId});

    if (CS.isEmpty(oAttribute)) {
      oAttribute = ContentUtils.createAttributeInstanceObject("", oReferencedAttribute.id);
      oKlassInstance.attributes.push(oAttribute);
    }

    if (ContentUtils.isAttributeTypeHtml(oReferencedAttribute.type)) {
      oAttribute.valueAsHtml = sValue.textValue;
      oAttribute.value = sValue.htmlValue;
      if (!CS.isEmpty(oProperty) && !CS.isEqual(oProperty.value, sValue.htmlValue)) {
        oProperty.value = sValue.htmlValue;
        oProperty.valueAsHtml = sValue.textValue
      }
    } else {
      oAttribute.value = sValue;
      if (!CS.isEmpty(oProperty) && !CS.isEqual(oProperty.value, sValue)) {
        oProperty.value = sValue;
      }
    }

    if (oAttribute.attributeId === SystemLevelIdDictionary.NameAttributeId) {
      oKlassInstance.name = sValue;
    }

  };

  let _resetGridActiveContentData = () => {
    ContentGridProps.setActiveContentId("");
    ContentGridProps.setActiveContentForEditing({});
  };

  let _handleGridViewSelectButtonClicked = (aSelectedContentIds, bSelectAllClicked) => {
    let oSkeleton = ContentGridProps.getGridViewSkeleton();
    if (bSelectAllClicked) {
      oSkeleton.selectedContentIds = CS.isEmpty(oSkeleton.selectedContentIds) ? aSelectedContentIds : [];
    }
    else {
      oSkeleton.selectedContentIds = CS.xor(oSkeleton.selectedContentIds, aSelectedContentIds);
    }
    _triggerChange();
  };

  let _processGridDataAfterDelete = (aIdsOfDeletedContents) => {
    let oGridViewSkeleton = ContentGridProps.getGridViewSkeleton();
    let oGridViewData = ContentGridProps.getGridViewData();
    let aInstancesData = ContentGridProps.getInstancesData();
    let aKlassInstances = aInstancesData.klassInstances;
    let aSelectedContentIds = oGridViewSkeleton.selectedContentIds;

    CS.forEach(aIdsOfDeletedContents, function (sDeletedContentId) {

      //remove deleted ids from grid skeleton
      CS.remove(aSelectedContentIds, function (sId) {
        return sId === sDeletedContentId;
      });

      //remove deleted ids from grid data
      CS.remove(oGridViewData, function (oRow) {
        return oRow.id === sDeletedContentId;
      });

      //remove deleted ids from klass instances data
      CS.remove(aKlassInstances, function (oKlassInstancesData) {
        let oKlassInstance = oKlassInstancesData.klassInstance;
        return oKlassInstance.id === sDeletedContentId;
      });
    });
  };

  let successDeleteSelectedArticlesCallback = (oCallbackData, oResponse) => {
    let oDeletedContent = {};
    let aIdsOfDeletedContents = oResponse.success;

    if (!CS.isEmpty(aIdsOfDeletedContents) && aIdsOfDeletedContents.length === 1) {
      let oInstanceData = _getInstanceDataByContentId(aIdsOfDeletedContents[0]);
      oDeletedContent = oInstanceData.klassInstance;
    }

    _resetGridActiveContentData();
    _processGridDataAfterDelete(aIdsOfDeletedContents);
    let sSuccessMessage;
    if (oDeletedContent) {
      if (oDeletedContent.baseType === BaseTypesDictionary['textAssetBaseType']) {
        sSuccessMessage = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().TEXTASSET_TAB } );
      } else if (oDeletedContent.baseType === BaseTypesDictionary['supplierBaseType']) {
        sSuccessMessage = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().SUPPLIER } );
      } else if (oDeletedContent.baseType === BaseTypesDictionary['marketBaseType']) {
        sSuccessMessage = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().MARKET } );
      } else {
        sSuccessMessage = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().ARTICLES_LABEL } );
      }
    } else {
      sSuccessMessage = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().ARTICLES_LABEL } );
    }

    if (!CS.isEmpty(aIdsOfDeletedContents)) {
      ContentUtils.showSuccess(sSuccessMessage);
    }

    _triggerChange();
  };

  let failureDeleteSelectedContentsCallback = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureDeleteSelectedContentsCallback', getTranslation());
  };

  let _makeDataToDeleteInstances = (aSelectedIds) => {
    let oSelectedContents = {};

    CS.forEach(aSelectedIds, function (sId) {
      let oInstanceData = _getInstanceDataByContentId(sId);
      let oContent = oInstanceData.klassInstance;
      if (oContent) {
        if (!oSelectedContents[oContent.baseType]) {
          oSelectedContents[oContent.baseType] = [];
        }
        oSelectedContents[oContent.baseType].push(sId);
      }
    });

    let aContentsToDelete = CS.map(oSelectedContents, function (aIds, sBaseType) {
      return {
        baseType: sBaseType,
        ids: aIds
      }
    });

    return {
      deleteRequest: aContentsToDelete
    }
  };

  let _deleteArticles = (aIds, oCallbackData) => {
    let oPostDataForBulkDelete = _makeDataToDeleteInstances(aIds);
    CS.deleteRequest(getRequestMapping().DeleteInstances, {}, oPostDataForBulkDelete,
        successDeleteSelectedArticlesCallback.bind(this, oCallbackData), failureDeleteSelectedContentsCallback);
  };

  let _handleContentDeleteClicked = (oContent) => {
    let sContentNamesString = ContentUtils.getContentName(oContent);
    let oCallbackData = {};

    //Are you sure you want to delete following Content?
    ContentUtils.confirmation(sContentNamesString,
        getTranslation().STORE_ALERT_CONFIRM_DELETE,
        _deleteArticles.bind(this, [oContent.id], oCallbackData),
        {},
        getTranslation().OK,
        getTranslation().CANCEL
    );
  };

  let _handleGridViewActionItemClicked = (sActionItemId, sContentId) => {
    switch (sActionItemId) {
      case "delete":
        let oInstanceData = _getInstanceDataByContentId(sContentId);
        let oContentToDelete = oInstanceData.klassInstance;
        _handleContentDeleteClicked(oContentToDelete);
        break;
    }
  };

  let _handleGridViewDeleteButtonClicked = (oCallbackData) => {
    let aGridViewSkeleton = ContentGridProps.getGridViewSkeleton();
    let aSelectedContentIds = aGridViewSkeleton.selectedContentIds;
    let aArticleIds = [];
    let aArticleNames = [];

    if (CS.isEmpty(aSelectedContentIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return;
    }
    CS.forEach(aSelectedContentIds, function (sId) {
      let oInstanceData = _getInstanceDataByContentId(sId);
      let oArticle = oInstanceData.klassInstance;
      aArticleIds.push(oArticle.id);
      aArticleNames.push(oArticle.name);
    });

    if (!CS.isEmpty(aArticleIds)) {
      ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_DELETE,
          aArticleNames,
          _deleteArticles.bind(this, aArticleIds, oCallbackData),
          {},
          getTranslation().OK,
          getTranslation().CANCEL
      );
    }
  };

  let _generateADMForContentToSave = (oContent, oContentClone) => {
    let oScreenProps = ContentScreenProps.screen;
    let aAttributeToRemove = ['isDirty', 'masterKlasses', 'isCreated'];
    let oContentToSave = CS.omit(oContentClone, function (value, key) {
      return CS.includes(aAttributeToRemove, key);
    });

    let oADMForAttributes = ContentUtils.generateADMForAttribute(oContent.attributes, oContentToSave.attributes);
    oContentToSave.addedAttributes = oADMForAttributes.added;
    oContentToSave.deletedAttributes = oADMForAttributes.deleted;
    oContentToSave.modifiedAttributes = oADMForAttributes.modified;
    delete oContentToSave.attributes;

    let oADMForTags = ContentUtils.generateADMForTags(oContent.tags, oContentToSave.tags);
    oContentToSave.addedTags = oADMForTags.added;
    oContentToSave.deletedTags = oADMForTags.deleted;
    oContentToSave.modifiedTags = oADMForTags.modified;
    delete oContentToSave.tags;

    let iSize = oScreenProps.getDefaultPaginationLimit();
    let iStartIndex = oScreenProps.getPaginatedIndex();
    oContentToSave.getKlassInstanceTreeInfo = ContentUtils.getEmptyFilterObject(iSize, iStartIndex);
    oContentToSave.tabType = TemplateTabsDictionary.CUSTOM_TAB;

    return oContentToSave;
  };

  let _getInstanceDataByContentId = (sKlassInstanceId) => {
    let oInstancesData = ContentGridProps.getInstancesData();
    let oInstanceDataToReturn = {};

    CS.forEach(oInstancesData.klassInstances, function (oInstanceData) {
      let oKlassInstance = oInstanceData.klassInstance;
      if (oKlassInstance.id === sKlassInstanceId) {
        oInstanceDataToReturn = oInstanceData;
        return false; //exit forEach
      }
    });

    return oInstanceDataToReturn;
  };

  let _handleContentGridSave = (oCallbackData) => {
    let oActiveContentForEditing = ContentGridProps.getActiveContentForEditing();
    let sActiveContentId = ContentGridProps.getActiveContentId();
    let oInstanceData = _getInstanceDataByContentId(sActiveContentId);
    let oContentToSave = _generateADMForContentToSave(oInstanceData.klassInstance, oActiveContentForEditing);
    oContentToSave.isGridEdit = true;
    oContentToSave.gridPropertySequenceList = ContentGridProps.getSelectedEditableProperties();
    let fSuccess = successSaveGridContentCallback.bind(this, oCallbackData);
    let sUrl = getRequestMapping(ContentUtils.getScreenModeBasedOnEntityBaseType(oContentToSave.baseType)).SaveEntity;
    CS.postRequest(sUrl, {isRollback: false}, oContentToSave, fSuccess, failureSaveGridContentCallback);
  };

  let successSaveGridContentCallback = (oCallbackData, oResponse) => {
    oResponse = oResponse.success;

    let oSavedKlassInstance = oResponse.klassInstance;

    if (ContentGridProps.getActiveContentId() === oSavedKlassInstance.id) {
      _resetGridActiveContentData();
    }

    let oNewReferencedElements = oResponse.configDetails.referencedElements;
    let oInstanceData = _getInstanceDataByContentId(oSavedKlassInstance.id);

    oInstanceData.klassInstance = oSavedKlassInstance; //assign new klass instance in data
    CS.forEach(oInstanceData.referencedElements, function (oReferencedElement) {
      let oNewReferencedElement = oNewReferencedElements[oReferencedElement.id];
      CS.assign(oReferencedElement, oNewReferencedElement); //assign new referenced element data
    });

    let aGridViewData = ContentGridProps.getGridViewData();
    let oGridRow = CS.find(aGridViewData, {id: oSavedKlassInstance.id});
    let oGridPropertyData = oGridRow.properties;
    oGridRow.isDirty = false;

    let oInstancesData = ContentGridProps.getInstancesData();
    let oReferencedAttributes = oInstancesData.referencedAttributes;
    let oReferencedTags = oInstancesData.referencedTags;

    _addToGridPropertyDataFromKlassInstance(oInstanceData, oGridPropertyData, oReferencedAttributes, oReferencedTags);

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    _triggerChange();
  };

  let failureSaveGridContentCallback = (oResponse) => {
    ContentUtils.failureCallback(oResponse, "failureSaveContentsCallback", getTranslation);
  };

  let _handleGridViewSearchTextChanged = (sSearchText) => {
    let aFilteredRowIds = [];
    let aGridViewData = ContentGridProps.getGridViewData();

    ContentGridProps.setGridViewSearchText(sSearchText);
    if (CS.isEmpty(sSearchText)) {
      return;
    }

    CS.forEach(aGridViewData, function (oRow) {
      let aProperties = oRow.properties;
      let oNameAttribute = aProperties[SystemLevelIdDictionary.NameAttributeId];
      let sLabel = oNameAttribute.value;

      if ((sLabel.toLowerCase().indexOf(sSearchText.toLowerCase())) === -1) {
        aFilteredRowIds.push(oRow.id);
      }
    });

    ContentGridProps.setFilteredRowIds(aFilteredRowIds);
  };

  let successFetchGridEditablePropertySequences = (oCallback, oResponse) => {
    oResponse = oResponse.success;
    let aPropertyList = oResponse.propertyList;
    let aPropertySequenceList = oResponse.propertySequenceList;
    ContentGridProps.setSequenceListLimit(25);

    let aPropertySequenceListIds = CS.map(aPropertySequenceList, function (oProperty) {
      return oProperty.id;
    });

    CS.map(aPropertyList, (oProperty) => {
      oProperty.iconClassName = oProperty.type;
    });

    CS.remove(aPropertyList, function (oProperty) {
      if (oProperty.id === "nameattribute")
        return true;
    });

    CS.remove(aPropertySequenceList, function (oProperty) {
      if (oProperty.id === "nameattribute")
        return true;
    });
    let bIsLoadMore = oResponse.propertyListTotalCount !== aPropertyList.length ;

    GridEditViewProps.setProperties(aPropertyList);
    GridEditViewProps.setIsLoadMore(bIsLoadMore);
    ContentGridProps.setSelectedEditableProperties(aPropertySequenceListIds);
    if (!GridEditViewProps.getPropertiesSequenceList().isDirty) {
      GridEditViewProps.setPropertiesSequenceList(aPropertySequenceList);
    }

    if(oCallback && oCallback.functionToExecute){
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  let failureFetchGridEditableProperties = (oResponse) => {
    ContentUtils.failureCallback(oResponse, "failureFetchGridEditableProperties", getTranslation);
  };

  let _handleApplySelectedEditablePropertiesClicked = (aSelectedEditableProperties) => {
    aSelectedEditableProperties.push(SystemLevelIdDictionary.NameAttributeId);
    ContentGridProps.setSelectedEditableProperties(aSelectedEditableProperties);
    _fetchContentGridData();
  };

  let _fetchGridEditPropertyList = (oCallback) => {
    let oPaginationData = GridEditViewProps.getPaginationData();
    oPaginationData.from = 0;

    let oRequestData = {
      from: oPaginationData.from,
      searchColumn: "label",
      searchText: GridEditViewProps.getSearchText(),
      size: oPaginationData.pageSize,
      sortBy: GridEditViewProps.getSortBy(),
      sortOrder: GridEditViewProps.getSortOrder(),
      isRuntimeRequest: true,
    };
    CS.postRequest(getRequestMapping().GetAllGridEditablePropertyList, {}, oRequestData,
      successFetchGridEditablePropertySequences.bind(this, oCallback), failureFetchGridEditableProperties);
  };

  let _fetchGridEditProperties = (bIsLoadMore, bDisableLoader) => {
    let oPaginationData = GridEditViewProps.getPaginationData();
    let iSize = !bIsLoadMore ? oPaginationData.from + oPaginationData.pageSize : oPaginationData.pageSize;

    let aPropertySequenceList = GridEditViewProps.getPropertiesSequenceList();
    aPropertySequenceList = aPropertySequenceList.clonedObject && aPropertySequenceList.clonedObject || aPropertySequenceList;
    let aPropertySequenceIdsList = CS.map(aPropertySequenceList, "id");

    let oRequestData = {
      from: bIsLoadMore ? oPaginationData.from : 0,
      searchColumn: "label",
      searchText: GridEditViewProps.getSearchText(),
      size: iSize,
      sortBy: GridEditViewProps.getSortBy(),
      sortOrder: GridEditViewProps.getSortOrder(),
      idsToExclude: aPropertySequenceIdsList
    };
    CS.postRequest(getRequestMapping().GetGridEditableProperties, {}, oRequestData,
    successFetchGridEditProperties.bind(this,bIsLoadMore), failureFetchGridEditableProperties, bDisableLoader);
  };

  let successFetchGridEditProperties = (bIsLoadMore, oResponse) => {
    let aPropertyList = oResponse.success.gridEditProperties;

    if (!!bIsLoadMore) {
      let aCurrentPropertyList = GridEditViewProps.getProperties();
      aPropertyList = CS.concat(aCurrentPropertyList, aPropertyList);
    }
    CS.remove(aPropertyList, function (oProperty) {
      if (oProperty.id === "nameattribute")
        return true;
    });

    GridEditViewProps.setProperties(aPropertyList);
    _triggerChange();
  };

  let _handleGridEditButtonClicked = () => {
    let oCallbackData = {
      functionToExecute: () => {
        GridEditViewProps.setIsDialogOpen(!GridEditViewProps.getIsDialogOpen());
      }
    };
    if (GridEditViewProps.getIsSequenceListModified()) {
      _fetchGridEditProperties(false, false);
      GridEditViewProps.setIsDialogOpen(!GridEditViewProps.getIsDialogOpen());
    } else {
      _fetchGridEditPropertyList(oCallbackData);
    }
  };

  let _handleConfigDialogButtonClicked = (sButtonId) => {
    if (sButtonId == "save") {
      _saveActiveGridEditablePropertiesConfig();
    } else if (sButtonId == "cancel") {
      _discardActiveGridEditablePropertiesConfig();
    } else {
      _closeGridEditablePropertiesConfigDialog();
    }
  };

  let _handleSearchTextChanged = (sSearchText) => {
    GridEditViewProps.setSearchText(sSearchText);
    _fetchGridEditProperties(false, false);
  };

  let _handleLoadMoreClicked = () => {
    let oPaginationData = GridEditViewProps.getPaginationData();
    let aPropertyList = GridEditViewProps.getProperties();
    oPaginationData.from = CS.size(aPropertyList);
    _fetchGridEditProperties(true, false);
  };

  let _handlePropertySequenceShuffled = function (oSource, oDestination, aDraggableIds) {
    _makeSequenceListDirty();
    let aPropertiesSequenceList = GridEditViewProps.getPropertiesSequenceList().clonedObject;
    let aPropertiesList = GridEditViewProps.getProperties();
    let iDestinationIndex = oDestination.index;
    let iSourceIndex = oSource.index;
    let aShuffledProperties = [];
    CS.forEach(aDraggableIds, function (sId) {
      let oProperty = CS.find(aPropertiesList, {id: sId}) || CS.find(aPropertiesSequenceList, {id: sId});
      oProperty && aShuffledProperties.push(oProperty)
    });

    if (oSource.droppableId === "propertyList" && oDestination.droppableId === "propertySequenceList") {
      if (CS.size(aPropertiesSequenceList) === ContentGridProps.getSequenceListLimit()) {
        alertify.error(oTranslations().LIMIT_OF_TWENTY_FIVE_PROPERTIES_EXCEEDED);
        return;
      }
      aPropertiesSequenceList.splice(iDestinationIndex, 0, ...aShuffledProperties);
      _fetchGridEditProperties(false, true);
    }
    else if (oSource.droppableId === "propertySequenceList") {
      CS.remove(aPropertiesSequenceList, function (oProperty) {
        return CS.includes(aDraggableIds, oProperty.id);
      });
      oDestination.droppableId === "propertyList" && _fetchGridEditProperties(false);

      if (oDestination.droppableId === "propertySequenceList") {
        if(iSourceIndex < iDestinationIndex) {
          iDestinationIndex = iDestinationIndex - (aDraggableIds.length - 1);
        }
        aPropertiesSequenceList.splice(iDestinationIndex, 0, ...aShuffledProperties);
        _triggerChange();
      }
    }
  };

  let _makeSequenceListDirty = () => {
    let oPropertiesSequenceList = GridEditViewProps.getPropertiesSequenceList();
    if(!oPropertiesSequenceList.clonedObject){
      oPropertiesSequenceList.clonedObject = CS.cloneDeep(oPropertiesSequenceList);
      oPropertiesSequenceList.isDirty = true;
    }
  };

  let _saveActiveGridEditablePropertiesConfig = () => {
    GridEditViewProps.setPropertiesSequenceList(GridEditViewProps.getPropertiesSequenceList().clonedObject);
    delete GridEditViewProps.getPropertiesSequenceList().isDirty;

    let aPropertySequenceList = GridEditViewProps.getPropertiesSequenceList();
    aPropertySequenceList = aPropertySequenceList.clonedObject && aPropertySequenceList.clonedObject || aPropertySequenceList;
    let aSelectedPropertyIds = [];
    CS.forEach(aPropertySequenceList, function (oProperty) {
      aSelectedPropertyIds.push(oProperty.id);
    });
    ContentGridProps.setSelectedEditableProperties(aSelectedPropertyIds);
    GridEditViewProps.setIsDialogOpen(false);
    _resetGridEditViewPaginationData();
    GridEditViewProps.setIsSequenceListModified(true);
    ContentGridStore.handleApplySelectedEditablePropertiesClicked();
    alertify.success(getTranslation().PROPERTY_SAVED_SUCCESSFULLY);
  };

  let _discardActiveGridEditablePropertiesConfig = () => {
    delete GridEditViewProps.getPropertiesSequenceList().clonedObject;
    delete GridEditViewProps.getPropertiesSequenceList().isDirty;

    _fetchGridEditProperties(false,false);
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    _triggerChange();
  };

  let _resetGridEditViewPaginationData = () => {
    GridEditViewProps.setSearchText("");
    GridEditViewProps.setPaginationData({
      from: 0,
      pageSize: 20
    });
  };

  let _closeGridEditablePropertiesConfigDialog = () => {
    GridEditViewProps.setIsDialogOpen(false);
    _resetGridEditViewPaginationData();
    _triggerChange();
  };

  let _resetGridEditablePropertiesData = () => {
    GridEditViewProps.reset();
  };

  let _handleGridEditFullScreenButtonClicked = () => {
    let bIsGridEditViewOpen = ContentGridProps.getIsContentGridEditViewOpen();
    ContentGridProps.setIsContentGridEditViewOpen(!bIsGridEditViewOpen);
    ContentGridProps.setGridViewSearchText("");
    ContentGridProps.setGridViewPaginationData({
      from: 0,
      pageSize: 20
    });
    GridEditViewProps.setIsSequenceListModified(false);
  };

  let _handleColumnOrganizerDialogButtonClicked = (sButtonId, aColumns) => {
    let bIsDialogOpen = GridEditViewProps.getIsDialogOpen();
    ContentGridProps.setResizedColumnId("");
    switch (sButtonId) {
      case "ok":
        GridEditViewProps.setIsDialogOpen(!bIsDialogOpen);
        _triggerChange();
        break;

      case "save":
        GridEditViewProps.setPropertiesSequenceList(aColumns);
        let aSelectedPropertiesIds = CS.map(aColumns,'id');
        GridEditViewProps.setIsSequenceListModified(true);
        GridEditViewProps.setIsDialogOpen(!bIsDialogOpen);
        _handleApplySelectedEditablePropertiesClicked(aSelectedPropertiesIds);
        break;
    }
  };

  /*******************  PUBLIC API  *******************/

  return {

    handleGridTagPropertyEdit: (sContentId, sTagId, aTagValueRelevanceData) => {
      _handleGridTagPropertyEdit(sContentId, sTagId, aTagValueRelevanceData);
    },

    fetchContentGridData: () => {
      _handleGridEditFullScreenButtonClicked();
      _fetchContentGridData();
    },

    handleGridPropertyAttributeValueChanged: (sContentId, sPropertyId, sValue) => {
      _handleGridPropertyAttributeValueChanged(sContentId, sPropertyId, sValue);
    },

    handleGridPropertyChanged: (sContentId, sPropertyId, sValue) => {
      _handleGridPropertyChanged(sContentId, sPropertyId, sValue);
    },

    handleContentGridSave: (oCallbackData) => {
      let sActiveContentId = ContentGridProps.getActiveContentId();
      if (!CS.isEmpty(sActiveContentId)) {
        _handleContentGridSave(oCallbackData);
      }
    },

    handleGridViewSelectButtonClicked: (aSelectedContentIds, bSelectAllClicked) => {
      _handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
    },

    handleGridViewActionItemClicked: (sActionItemId, sContentId) => {
      _handleGridViewActionItemClicked(sActionItemId, sContentId);
    },

    handleGridViewDeleteButtonClicked: () => {
      _handleGridViewDeleteButtonClicked();
    },

    handleGridViewSearchTextChanged: (sSearchText) => {
      _handleGridViewSearchTextChanged(sSearchText);
    },

    handleApplySelectedEditablePropertiesClicked: () => {
      let aSelectedEditableProperties = ContentGridProps.getSelectedEditableProperties();
      _handleApplySelectedEditablePropertiesClicked(aSelectedEditableProperties);
    },

    handleGridEditButtonClicked: () => {
      _handleGridEditButtonClicked();
    },

    getActiveContentId: () => {
      return ContentGridProps.getActiveContentId();
    },

    getGridPropertyForAttribute: (oAttributeInstance, oMasterAttribute = {}, oReferencedElement = {}, oKlassInstance = ContentUtils.getActiveContent()) => {
      return _getGridPropertyForAttribute(oAttributeInstance, oMasterAttribute, oReferencedElement, oKlassInstance);
    },

    handleConfigDialogButtonClicked: (sButtonId) => {
      _handleConfigDialogButtonClicked(sButtonId);
    },

    handleSearchTextChanged: (sSearchText) => {
      _handleSearchTextChanged(sSearchText);
    },

    handleLoadMoreClicked: () => {
      _handleLoadMoreClicked();
    },

    handlePropertySequenceShuffled: (oSource, oDestination, aDraggableIds) => {
      _handlePropertySequenceShuffled(oSource, oDestination, aDraggableIds);
    },

    handleContentGridEditCloseButtonClicked: () => {
      ContentGridProps.setSelectedEditableProperties([]);
      _handleGridEditFullScreenButtonClicked();
      BreadCrumbStore.refreshCurrentBreadcrumbEntity();
    },

    handleColumnOrganizerDialogButtonClicked: (sButtonId, aColumns) => {
      _handleColumnOrganizerDialogButtonClicked(sButtonId, aColumns);
    },

    resetGridEditablePropertiesData: () => {
      _resetGridEditablePropertiesData();
    },

  }
})();

MicroEvent.mixin(ContentGridStore);

export default ContentGridStore;
