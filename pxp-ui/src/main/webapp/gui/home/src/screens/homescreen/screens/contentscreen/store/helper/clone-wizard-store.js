import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import VariantSectionViewProps from './../model/variant-section-view-props';
import ContentScreenProps from './../model/content-screen-props';
import ContentScreenRequestMapping from '../../tack/content-screen-request-mapping';
import BreadcrumbStore from '../../../../../../commonmodule/store/helper/breadcrumb-store';
import SharableURLStore from '../../../../../../commonmodule/store/helper/sharable-url-store';
import BaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import TaxonomyTypeDictionary from '../../../../../../commonmodule/tack/taxonomy-type-dictionary';
import BreadCrumbModuleAndHelpScreenIdDictionary
  from '../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import ContentUtils from './content-utils';
import ScreenModeUtils from './screen-mode-utils';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary';

const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
const getTranslation = ScreenModeUtils.getTranslationDictionary;

let CloneWizardStore = (function () {

  let _triggerChange = function () {
    CloneWizardStore.trigger('clone-wizard-change');
  };

  let _getRequestDataToCreateCloneOfLinkedVariant = function () {
    let oSelectedIdsForCloningWizard = VariantSectionViewProps.getSelectedEntityIdsForCloning();
    let sCloneWizardContentId = VariantSectionViewProps.getCloningWizardContentId();
    let sActiveEntityId = CS.isNotEmpty(sCloneWizardContentId) ? sCloneWizardContentId : ContentUtils.getActiveEntity().id;
    let aSelectedTaxonomyIds = CS.isNotEmpty(oSelectedIdsForCloningWizard)
        ? CS.combine(oSelectedIdsForCloningWizard.taxonomies, oSelectedIdsForCloningWizard.minorTaxonomies)
        : ContentScreenProps.screen.getActiveTaxonomyIds();

    let aActiveClassIds = CS.cloneDeep(ContentScreenProps.screen.getActiveClassIds());
    let oReferencedClasses = ContentScreenProps.screen.getReferencedClasses();

    let sNatureKlassId = "";
    CS.forEach(aActiveClassIds, function (sId) {
      if(oReferencedClasses[sId].isNature) {
        sNatureKlassId = sId;
        return false;
      }
    });

    return {
      id: sActiveEntityId,
      selectedTypesIds: oSelectedIdsForCloningWizard.classes,
      selectedTaxonomyIds: aSelectedTaxonomyIds,
      isForLinkedVariant: true,
      parentNatureKlassId: sNatureKlassId,
    };
  };

  let _handleSelectTypeToCreateLinkedVariant = function (sIdToRemove, sSelectedId) {
    let oCallback = {};
    oCallback.context = "productVariant";

    if(CS.isNotEmpty(sSelectedId)) {
      let oSelectedIdsForCloningWizard = VariantSectionViewProps.getSelectedEntityIdsForCloning();
      CS.remove(oSelectedIdsForCloningWizard.classes, function (sTypeId) {
        return sTypeId === sIdToRemove;
      });
      oSelectedIdsForCloningWizard.classes.push(sSelectedId);

      oCallback.functionToExecute = function () {
        let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
        CS.forEach(oSelectedIdsForCloningWizard.properties, function (aSelectedProperties, sGroupId) {
          if(CS.isEmpty(oCloningWizardViewData.properties[sGroupId])) {
            delete oSelectedIdsForCloningWizard.properties[sGroupId];
          }
        });

        CS.forEach(oCloningWizardViewData.properties, function (oGroup, sGroupId) {
          if(!oSelectedIdsForCloningWizard.properties[sGroupId]) {
            oSelectedIdsForCloningWizard.properties[sGroupId] = [];
          }
        });
      };

      oCallback.requestData = _getRequestDataToCreateCloneOfLinkedVariant();

      _fetchCloningWizardData(oCallback);
    }
  };

  let _handleCloningWizardCloneCountChanged = function (iCloneCount) {
    VariantSectionViewProps.setCloningWizardCloneCount(iCloneCount);
    _triggerChange();
  };

  let _getCloningDataToSend = function () {
    let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
    let oContextPropertyMap = VariantSectionViewProps.getCloneWizardContextPropertyMap();
    let oSelectedEntitiesForCloningWizard = VariantSectionViewProps.getSelectedEntityIdsForCloning();

    let oCloningDataToSend = {
      attributes: [],
      tags: [],
      relationships: [],
      contexts: [],
      types: oSelectedEntitiesForCloningWizard.classes,
      taxonomies: CS.combine(oSelectedEntitiesForCloningWizard.taxonomies, oSelectedEntitiesForCloningWizard.minorTaxonomies)
    };

    CS.forEach(oSelectedEntitiesForCloningWizard.properties, function (aSelectedEntities, sGroupId) {
      switch(sGroupId) {
        case "relationships":
          oCloningDataToSend.relationships = aSelectedEntities;
          break;

        default:
          let aProperties = oCloningWizardViewData.properties[sGroupId].data;
          CS.forEach(aSelectedEntities, function (sSelectedId) {
              let oProperty = CS.find(aProperties, {id: sSelectedId});
              if(oProperty.type === "attribute") {
                oCloningDataToSend.attributes.push(sSelectedId);
              }
              else if(oProperty.type === "tag") {
                oCloningDataToSend.tags.push(sSelectedId);
              }
          })
      }
    });

    let aContextIds = oCloningDataToSend.contexts;
    let aAttributeIds = oCloningDataToSend.attributes;
    let aTagIds = oCloningDataToSend.tags;

    //for adding attributes & tags of selected context
    CS.forEach(aContextIds, function (sId) {
      let oContextData = oContextPropertyMap[sId];
      let aAttributes = oContextData.attributes;
      let aTags = oContextData.tags;
      let aAttributeIdsToAdd = CS.map(aAttributes, 'id');
      let aTagIdsToAdd = CS.map(aTags, 'id');
      let aUniqueAttributeIds = CS.difference(aAttributeIdsToAdd, aAttributeIds);
      let aUniqueTagIds = CS.difference(aTagIdsToAdd, aTagIds);
      aAttributeIds = aAttributeIds.concat(aUniqueAttributeIds);
      aTagIds = aTagIds.concat(aUniqueTagIds);
    });

    oCloningDataToSend.attributes = aAttributeIds;
    oCloningDataToSend.tags = aTagIds;

    return oCloningDataToSend;
  };

  let _handleCancelCloningButtonClicked = function (doNotTrigger) {
    VariantSectionViewProps.setIsCloningWizardOpen(false);
    VariantSectionViewProps.setCloningWizardViewData({});
    VariantSectionViewProps.setIsSelectAllForCloningWizard(false);
    VariantSectionViewProps.setCloningWizardCloneCount(1);
    VariantSectionViewProps.setCloningWizardContentId("");
    VariantSectionViewProps.setSelectedEntityIdsForCloning({});
    VariantSectionViewProps.setIsExactCloneSelected(false);
    if (!doNotTrigger) {
      _triggerChange();
    }
  };

  let _handleCloneExpandSectionToggled = function (sTypeId) {
    let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
    let oCloningViewData = (oCloningWizardViewData.properties[sTypeId]);

    if (CS.isEmpty(oCloningViewData)) {
      return;
    }

    let bIsExpanded = oCloningViewData.isExpanded;
    oCloningViewData.isExpanded = !bIsExpanded;

    _triggerChange();
  };

  let failureHandleCreateLinkedVariant = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureHandleCreateLinkedVariant", getTranslation());
  };

  let successHandleCreateLinkedVariant = function (oResponse) {
    let oResponseData = oResponse.success;
    let oCreatedEntity = oResponseData.klassInstance;
    ContentUtils.getAppData().getAvailableEntities().push(oCreatedEntity);

    let oCallback = {};
    oCallback.functionToExecute = function () {
      let oContentStore = ContentUtils.getContentStore();
      oContentStore.processGetArticleResponse(oResponseData);
      let aPayloadData = [oCreatedEntity.id, {entityType: oCreatedEntity.baseType}];
      let sHelpScreenId;
      let sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT;
      let oBreadcrumbItem = BreadcrumbStore.createBreadcrumbItem(oCreatedEntity.id, oCreatedEntity.name, sType, sHelpScreenId, {}, {}, "", aPayloadData, oContentStore.fetchArticleById);

      SharableURLStore.setIsPushHistoryState(true);
      BreadcrumbStore.addNewBreadCrumbItem(oBreadcrumbItem);
      ContentScreenProps.screen.setIsRelationshipContextDialogOpen(false);
      _handleCancelCloningButtonClicked();
    };

    /**Flag needed in backend to verify link variant manual creation in saveRelationship call**/
    oCallback.isManuallyCreated = true;

    ContentUtils.getContentStore().handleContentEntityDropInRelationshipSection(oCreatedEntity.id, oCallback);
  };

  let _createLinkedVariant = function () {
    let oActiveEntity = ContentUtils.getActiveEntity();
    let bIsAllPropertiesClone = VariantSectionViewProps.getIsExactCloneSelected();

    let oDataToPost = {};
    oDataToPost.klassInstanceIdToClone = oActiveEntity.id;
    oDataToPost.type = oActiveEntity.baseType;
    oDataToPost.cloneData = bIsAllPropertiesClone ? {} : _getCloningDataToSend();
    oDataToPost.shouldCloneAllProperties = bIsAllPropertiesClone;

    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let oContextValidateData = {
      id: oActiveEntity.id,
      contextId: oRelationshipContextData.context.id,
      timeRange: oRelationshipContextData.timeRange,
      tags: oRelationshipContextData.tags,
      relationshipId: oRelationshipContextData.context.actualRelationshipId
    };
    oDataToPost.contextValidateData = oContextValidateData;

    let sURL = getRequestMapping().CreateCloneForLinkedVariant;
    CS.putRequest(sURL, {}, oDataToPost, successHandleCreateLinkedVariant, failureHandleCreateLinkedVariant);
  };

  let _handleCreateCloneButtonClicked = function (sContextId, oModel, oFilterContext) {
    switch (sContextId) {

      case "cloneSingleContent": /** For Thumbnail **/
       _createBulkClone({filterContext: oFilterContext}, oModel.id);
        break;

      case "cloneContent": /** For Tile View Toolbar **/
      _showContentListToBeCloned(sContextId, oModel, {
            filterContext: oFilterContext,
            functionToExecute: _createBulkClone
          });
      break;

      case "productVariant":
        _createLinkedVariant();
        break;

      default:
        _createSingleClone(); /** For open content **/
        break;
    }
  };

  let _createSingleClone = function (oCallback) {
    let oActiveEntity = ContentUtils.getActiveEntity();
    let bIsAllPropertiesClone = VariantSectionViewProps.getIsExactCloneSelected();

    let oDataToPost = {};
    oDataToPost.klassInstanceIdToClone = oActiveEntity.id;
    oDataToPost.type = oActiveEntity.baseType;
    oDataToPost.cloneData = bIsAllPropertiesClone ? {} : _getCloningDataToSend();
    oDataToPost.shouldCloneAllProperties = bIsAllPropertiesClone;

    let sBaseType = oActiveEntity.baseType;
    let sURL = null;
    switch (sBaseType) {
      case BaseTypesDictionary.marketBaseType :
        sURL = getRequestMapping().CreateSingleCloneForTarget;
        break;
      case BaseTypesDictionary.textAssetBaseType:
        sURL = getRequestMapping().CreateSingleCloneForTextAsset;
        break;
      case BaseTypesDictionary.contentBaseType:
          sURL = getRequestMapping().CreateSingleClone;
        break
    }

    let fSuccess = successHandleCreateSingleClone.bind(this, oCallback);
    let fFailure = failureHandleCreateSingleClone;
    CS.putRequest(sURL, {}, oDataToPost, fSuccess, fFailure);
  };

  let successHandleCreateSingleClone = function (oCallback, oResponse) {
    let oResponseData = oResponse.success;

    if (!CS.isEmpty(oResponseData.configDetails)) {
      let oContentStore = ContentUtils.getContentStore();
      oContentStore.processGetArticleResponse(oResponseData);
      let oCreatedEntity = oResponseData.klassInstance;

      let aPayloadData = [oCreatedEntity.id, {entityType: oCreatedEntity.baseType}];
      let sHelpScreenId;
      let sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT;
      let oBreadcrumbItem = BreadcrumbStore.createBreadcrumbItem(oCreatedEntity.id, oCreatedEntity.name, sType, sHelpScreenId, {}, {}, "", aPayloadData, oContentStore.fetchArticleById);

      SharableURLStore.setIsPushHistoryState(true);
      BreadcrumbStore.addNewBreadCrumbItem(oBreadcrumbItem);
    }
    ContentUtils.showSuccess(getTranslation().SUCCESS_CLONE_CREATE);
    ContentScreenProps.screen.setIsRelationshipContextDialogOpen(false);
    _handleCancelCloningButtonClicked()
  };

  let failureHandleCreateSingleClone = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureHandleCreateSingleClone", getTranslation());
  };

  let _createBulkClone = function (oCallback, sContentIdToBeCloned) {
    let oAppData = ContentUtils.getAppData();
    let aContentList = oAppData.getContentList();
    let aSelectedContentIds = CS.isNotEmpty(sContentIdToBeCloned) && [sContentIdToBeCloned] || ContentUtils.getSelectedContentIds();
    let oDataToPost = {};
    oDataToPost.contentsToClone = [];
    let sURL = "";
    CS.forEach(aSelectedContentIds, function (sId) {
      let oContent = CS.find(aContentList, {id: sId});
      oDataToPost.contentsToClone.push({
        id: oContent.id,
        type: oContent.baseType
      });
    });
    let oContent = CS.find(aContentList, {id: aSelectedContentIds[0]});
    if(oContent.baseType === BaseTypesDictionary.marketBaseType) {
      sURL = getRequestMapping().CreateBulkCloneForTarget
    }else if(oContent.baseType === BaseTypesDictionary.textAssetBaseType) {
      sURL = getRequestMapping().CreateBulkCloneForTextAsset
    }
    else if (oContent.baseType === BaseTypesDictionary.contentBaseType) {
      sURL = getRequestMapping().CreateBulkClone;
    }

    let fSuccess = successHandleCreateBulkClone.bind(this, oCallback);
    let fFailure = failureHandleCreateBulkClone;
    CS.putRequest(sURL, {}, oDataToPost, fSuccess, fFailure);
  };

  let _showContentListToBeCloned = function (sContextId, oModel, oCallback) {
    let aSelectedEntities = ContentUtils.getSelectedEntityList();
    let aSelectedEntityNames = CS.map(aSelectedEntities, "name");

    if(!CS.isEmpty(aSelectedEntityNames)) {
      ContentUtils.listModeConfirmation(getTranslation().ALERT_CONFIRM_CREATE_CLONE, aSelectedEntityNames,
          function () {
            oCallback.functionToExecute(oCallback.filterContext || {});
          }, function (oEvent) {
          });
    }
  };

  let successHandleCreateBulkClone = function (oCallback, oResponse) {
    if (!CS.isEmpty(oResponse.failure.exceptionDetails)) {
      ContentUtils.failureCallback(oResponse, "successHandleCreateBulkClone", getTranslation());
    }
    ContentUtils.showSuccess(getTranslation().CLONE_IS_IN_PROGRESS);
    let oContentStore = ContentUtils.getContentStore();
    oContentStore.handleRefreshContentButtonClicked(oCallback.filterContext);
    _triggerChange();
  };

  let failureHandleCreateBulkClone = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureHandleCreateBulkClone", getTranslation());
  };

  let failureFetchCloningWizardData = function (oCallbackData = {}, oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchCloningWizardData', getTranslation());
  };

  let _fetchCloningDataAfterDeselectionOfClassification = () => {
    let oCallback = {};
    let sCloneWizardContext = VariantSectionViewProps.getCloneWizardDialogContext();
    if(sCloneWizardContext === "productVariant") {
      oCallback.requestData = _getRequestDataToCreateCloneOfLinkedVariant();
    }
    oCallback.functionToExecute = function () {
      let oSelectedPropertyIds = VariantSectionViewProps.getSelectedEntityIdsForCloning();
      let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();

      CS.forEach(oSelectedPropertyIds.properties, function (aSelectedProperties, sGroupId) {
        if(CS.isEmpty(oCloningWizardViewData.properties[sGroupId])) {
          delete oSelectedPropertyIds.properties[sGroupId];
        }
      });
    };

    _fetchCloningWizardData(oCallback);
  };

  let _deselectClassification = (aSelectedProperty, sId, sContext) => {
    CS.remove(aSelectedProperty, function (sPropertyId) {
      if (sId === sPropertyId) {
        return true;
      }
    });

    (sContext === TaxonomyTypeDictionary.MINOR_TAXONOMY) && _deselectMinorTaxonomy(sId);

    _fetchCloningDataAfterDeselectionOfClassification();
  };

  let _deselectMinorTaxonomy = (sId) => {
    let oSelectedPropertyIds = VariantSectionViewProps.getSelectedEntityIdsForCloning();
    let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
    let oMinorTaxonomies = oCloningWizardViewData.minorTaxonomies;

    CS.remove(oSelectedPropertyIds.minorTaxonomies, function (sMinorTaxonomyId) {
      let aMinorTaxonomy = oMinorTaxonomies[sMinorTaxonomyId];
      let sRootNodeId = aMinorTaxonomy[0].id;
      return sRootNodeId === sId;
    });
  };

  let _selectMinorTaxonomy = (sId) => {
    let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
    let oMinorTaxonomiesData = oCloningWizardViewData.minorTaxonomies;
    let oSelectedPropertyIds = VariantSectionViewProps.getSelectedEntityIdsForCloning();
    let aSelectedMinorTaxonomyIds = oSelectedPropertyIds.minorTaxonomies;

    CS.forEach(oMinorTaxonomiesData, function (aMinorTaxonomy, sMinorTaxonomyId) {
      let sRootNodeId = aMinorTaxonomy[0].id;
      if(sRootNodeId === sId) {
        aSelectedMinorTaxonomyIds.push(sMinorTaxonomyId);
      }
    });
  };

  let _fetchCloningDataAfterSelectionOfClassification = () => {
    let oCallback = {};
    let sCloneWizardContext = VariantSectionViewProps.getCloneWizardDialogContext();
    if(sCloneWizardContext === "productVariant") {
      oCallback.requestData = _getRequestDataToCreateCloneOfLinkedVariant();
    }

    oCallback.functionToExecute = function () {
      let oSelectedPropertyIds = VariantSectionViewProps.getSelectedEntityIdsForCloning();
      let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();

      CS.forEach(oCloningWizardViewData.properties, function (oGroup, sGroupId) {
        if(!oSelectedPropertyIds.properties[sGroupId]) {
          oSelectedPropertyIds.properties[sGroupId] = [];
        }
      });
    };

    _fetchCloningWizardData(oCallback);
  };

  let _selectClassification = (aSelectedProperty, sId, sContext) => {
    aSelectedProperty.push(sId);
    (sContext === TaxonomyTypeDictionary.MINOR_TAXONOMY) && _selectMinorTaxonomy(sId);

    _fetchCloningDataAfterSelectionOfClassification();
  };

  let _selectOrDeselectProperties = (aSelectedProperties, sId) => {
    if (CS.includes(aSelectedProperties, sId)) {
      CS.remove(aSelectedProperties, function (sPropertyId) {
        if (sId === sPropertyId) {
          return true;
        }
      });
    }
    else {
      aSelectedProperties.push(sId);
    }
    _triggerChange();
  };

  let _handleCheckboxButtonClicked = (sId, sGroupId, sContext) => {
    let oSelectedPropertyIds = VariantSectionViewProps.getSelectedEntityIdsForCloning();

    switch (sContext) {
      case "classes":
      case "taxonomies":
      case "minorTaxonomies":
        let aSelectedClassificationIds = oSelectedPropertyIds[sContext];
        if (CS.includes(aSelectedClassificationIds, sId)) {
          _deselectClassification(aSelectedClassificationIds, sId);
        }
        else {
          _selectClassification(aSelectedClassificationIds, sId);
        }
        break;

      case "properties":
        let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
        let oSelectedPropertyData = oCloningWizardViewData.properties[sGroupId].data[sId];
        let aSelectedProperties = oSelectedPropertyIds[sContext][sGroupId];

        if(oSelectedPropertyData.type === TaxonomyTypeDictionary.MINOR_TAXONOMY) {
          if (CS.includes(aSelectedProperties, sId)) {
            _deselectClassification(aSelectedProperties, sId, TaxonomyTypeDictionary.MINOR_TAXONOMY);
          }
          else {
            _selectClassification(aSelectedProperties, sId, TaxonomyTypeDictionary.MINOR_TAXONOMY);
          }
        }
        else {
          _selectOrDeselectProperties(aSelectedProperties, sId);
        }
        break;
    }
  };

  let _getMinorTaxonomiesExistsInPropertyCollection = (oPropertyGroupData) => {
    return CS.filter(oPropertyGroupData, function (oGroupElement) {
      return oGroupElement.type === TaxonomyTypeDictionary.MINOR_TAXONOMY;
    });
  };

  let _handleCheckboxHeaderButtonClicked = (sKey) => {
    let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
    let oPropertiesGroup = oCloningWizardViewData.properties[sKey];
    let oSelectedPropertyIds = VariantSectionViewProps.getSelectedEntityIdsForCloning();
    let oGroupElements = oPropertiesGroup.data;
    let bDeselectHeader = oSelectedPropertyIds.properties[sKey].length === CS.size(oGroupElements);

    switch (oPropertiesGroup.id) {
      case "relationships":
        oSelectedPropertyIds.properties[sKey] = bDeselectHeader ? [] : CS.keys(oGroupElements);
        break;

      default: /** For Property collection group selection/deselection **/
        let aMinorTaxonomiesInPC = _getMinorTaxonomiesExistsInPropertyCollection(oPropertiesGroup.data);

        if(CS.isNotEmpty(aMinorTaxonomiesInPC)) {
          if(bDeselectHeader) {
            oSelectedPropertyIds.properties[sKey] = [];
            CS.forEach(aMinorTaxonomiesInPC, function (oMinorTaxonomy) {
              _deselectMinorTaxonomy(oMinorTaxonomy.id);
            });
            _fetchCloningDataAfterDeselectionOfClassification();
          }
          else {
            oSelectedPropertyIds.properties[sKey] = CS.keys(oGroupElements);
            CS.forEach(aMinorTaxonomiesInPC, function (oMinorTaxonomy) {
              _selectMinorTaxonomy(oMinorTaxonomy.id);
            });
            _fetchCloningDataAfterSelectionOfClassification();
          }
        }
        else {
          oSelectedPropertyIds.properties[sKey] = bDeselectHeader ? [] : CS.keys(oGroupElements);
        }
    }

    _triggerChange();
  };

  let _selectExactClone = () => {
    let oCallback = {};
    oCallback.context = VariantSectionViewProps.getCloneWizardDialogContext();
    oCallback.functionToExecute = function () {
      _createSelectedPropertiesForCloningWizard();
      VariantSectionViewProps.setIsExactCloneSelected(true);
    };
    _fetchCloningWizardData(oCallback);
  };

  let _handleExactCloneCheckboxClicked = () => {
    let bIsExactCloneSelected = VariantSectionViewProps.getIsExactCloneSelected();

    if(bIsExactCloneSelected) {
      VariantSectionViewProps.setIsExactCloneSelected(false);
      _triggerChange();
    }
    else {
      _selectExactClone();
    }
  };

  let _createSelectedPropertiesForCloningWizard = () => {
    let oCloningWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
    let sContext = VariantSectionViewProps.getCloneWizardDialogContext();
    let bIsProductVariant = sContext === ContextTypeDictionary.PRODUCT_VARIANT;

    let oSelectedPropertyIds = {
      classes: {},
      taxonomies: [],
      minorTaxonomies: [],
      properties: {}
    };

    oSelectedPropertyIds.classes = CS.keys(oCloningWizardViewData.classes);
    oSelectedPropertyIds.taxonomies = CS.keys(oCloningWizardViewData.taxonomies);
    oSelectedPropertyIds.minorTaxonomies = CS.keys(oCloningWizardViewData.minorTaxonomies);

    oSelectedPropertyIds.properties = {};
    CS.forEach(oCloningWizardViewData.properties, function (oGroup, sGroupId) {
      oSelectedPropertyIds.properties[sGroupId] = CS.keys(oGroup.data);
    });

    VariantSectionViewProps.setSelectedEntityIdsForCloning(oSelectedPropertyIds);
    VariantSectionViewProps.setIsExactCloneSelected(!bIsProductVariant);
  };

  let _getTaxonomyType = (oTaxonomy) => {
    if (CS.isEmpty(oTaxonomy.taxonomyType)) {
      return _getTaxonomyType(oTaxonomy.parent);
    } else {
      return oTaxonomy.taxonomyType;
    }
  };

  let _filterTaxonomiesByType = (oReferencedTaxonomies, aSelectedTaxonomies) => {
    let aMajorTaxonomies = [];
    let aMinorTaxonomies = [];

    CS.forEach(aSelectedTaxonomies, function (sSelectedTaxonomyId) {
      let oTaxonomy = oReferencedTaxonomies[sSelectedTaxonomyId];
      let sTaxonomyType = _getTaxonomyType(oTaxonomy);
      if(sTaxonomyType === "minorTaxonomy") {
        aMinorTaxonomies.push(sSelectedTaxonomyId);
      }
      else {
        aMajorTaxonomies.push(sSelectedTaxonomyId);
      }
    });

    return {
      majorTaxonomies: aMajorTaxonomies,
      minorTaxonomies: aMinorTaxonomies
    }
  };

  let _getMinorTaxonomy = (oReferencedTaxonomies, sMinorTaxonomyId) => {
    let oReferencedTaxonomyMap = {};
    CS.forEach(oReferencedTaxonomies, function (oReferencedTaxonomy) {
      ContentUtils.prepareReferencedTaxonomyMap(oReferencedTaxonomy, oReferencedTaxonomyMap);
    });
    oReferencedTaxonomyMap[sMinorTaxonomyId].type = "minorTaxonomy";

    return oReferencedTaxonomyMap[sMinorTaxonomyId];
  };

  let _getTaxonomyDetails = (oCloneWizardDataFromServer) => {
    let oActiveContent = ContentScreenProps.screen.getActiveContent();
    let aSelectedTaxonomies = oCloneWizardDataFromServer.taxonomies;
    let {majorTaxonomies: aMajorTaxonomies, minorTaxonomies: aMinorTaxonomies}  = _filterTaxonomiesByType(aSelectedTaxonomies, oActiveContent.selectedTaxonomyIds);
    let oMajorTaxonomiesDataForChipsView = ContentUtils.prepareSelectedTaxonomiesDataForChipsView(aSelectedTaxonomies, aMajorTaxonomies);
    let oMinorTaxonomiesDataForChipsView = ContentUtils.prepareSelectedTaxonomiesDataForChipsView(aSelectedTaxonomies, aMinorTaxonomies);

    return {
      oMajorTaxonomies: oMajorTaxonomiesDataForChipsView,
      oMinorTaxonomies: oMinorTaxonomiesDataForChipsView
    }
  };

  let _getDataForCloneWizardView = function (oCloneWizardDataFromServer) {
    let oCloneWizardViewData = VariantSectionViewProps.getCloningWizardViewData();
    let aReferencedPropertyCollections = oCloneWizardDataFromServer.propertyCollections;
    let oAttributes = oCloneWizardDataFromServer.attributes;
    let oTags = oCloneWizardDataFromServer.tags;
    let oProperties = {};

    CS.forEach(aReferencedPropertyCollections, function (oPropertyCollection, sPropertyCollectionId) {
      let oElementsData = {};
      if(CS.isEmpty(oPropertyCollection.elements)) {
        return;
      }
      CS.forEach(oPropertyCollection.elements, function (oElement) {
        let sElementId = oElement.id;
        if(oAttributes[sElementId]) {
          oElementsData[sElementId] = oAttributes[sElementId];
        }
        else if (oTags[sElementId]) {
          oElementsData[sElementId] = oTags[sElementId];
        }
        else {
          oElementsData[sElementId] = _getMinorTaxonomy(oCloneWizardDataFromServer.taxonomies, sElementId);
        }
      });

      let oOldPCData = CS.isNotEmpty(oCloneWizardViewData) && oCloneWizardViewData.properties[sPropertyCollectionId];
      oProperties[sPropertyCollectionId] = {
        id: sPropertyCollectionId,
        code: oPropertyCollection.code,
        label: oPropertyCollection.label,
        isExpanded: oOldPCData ? oOldPCData.isExpanded : true,
        data: oElementsData
      };
    });

    let oOldRelationshipsGroupData = CS.isNotEmpty(oCloneWizardViewData) && oCloneWizardViewData.properties["relationships"] || null;
    oProperties.relationships = {
      label: getTranslation().RELATIONSHIPS,
      isExpanded: oOldRelationshipsGroupData ? oOldRelationshipsGroupData.isExpanded : true,
      data: oCloneWizardDataFromServer.relationships,
      id: "relationships",
    };

    let {oMajorTaxonomies: taxonomies, oMinorTaxonomies: minorTaxonomies} = _getTaxonomyDetails(oCloneWizardDataFromServer);

    return {
      classes: oCloneWizardDataFromServer.klasses,
      taxonomies: taxonomies,
      minorTaxonomies: minorTaxonomies,
      properties: oProperties,
    }
  };

  let _getContextPropertyMap = function (aContextsCloningData) {
    let oContextPropertyMap = {};
    let aContextAttributes = [];
    let aContextTags = [];

    CS.forEach(aContextsCloningData, function (oData) {
      aContextAttributes = aContextAttributes.concat(oData.attributes);
      aContextTags = aContextTags.concat(oData.tags);
      oContextPropertyMap[oData.id] = {
        attributes: oData.attributes,
        tags: oData.tags
      }
    });

    return oContextPropertyMap;
  };

  let successFetchCloningWizardData = function (oCallback = {}, oResponse) {
    let oCloningData = oResponse.success;
    let aCloningWizardViewData = _getDataForCloneWizardView(oCloningData);
    let oContextPropertyMap = _getContextPropertyMap(oCloningData.contexts);

    VariantSectionViewProps.setCloningWizardViewData(aCloningWizardViewData);
    VariantSectionViewProps.setCloneWizardContextPropertyMap(oContextPropertyMap);
    VariantSectionViewProps.setCloneWizardDialogContext(oCallback.context || VariantSectionViewProps.getCloneWizardDialogContext());
    VariantSectionViewProps.setCloningWizardContentId(oCallback.contentId || "");
    let oSectionSelectionStatus = ContentScreenProps.screen.getSetSectionSelectionStatus();
    oSectionSelectionStatus["relationshipContainerSelectionStatus"] = true;

    if(oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  let successGetAllowedTypesToCreateLinkedVariant = function (oResponse) {
    let aAllowedTypesToCreateProductVariant = oResponse.success && oResponse.success.side2NatureKlasses || [];
    VariantSectionViewProps.setAllowedTypesToCreateLinkedVariant(aAllowedTypesToCreateProductVariant);
    _triggerChange();
  };

  let failureGetAllowedTypesToCreateLinkedVariant = function (oResponse) {
    ContentUtils.failureCallback(oResponse,'failureGetAllowedTypesToCreateLinkedVariant',getTranslation());
    _triggerChange();
  };

  let _handleGetAllowedTypesToCreateLinkedVariant = function () {
    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let sNatureRelationshipId = oRelationshipContextData.context.actualRelationshipId;
    let sURL = ContentScreenRequestMapping.GetAllSide2NatureKlassFromNatureRelationship;

    CS.getRequest(sURL, {id: sNatureRelationshipId}, successGetAllowedTypesToCreateLinkedVariant, failureGetAllowedTypesToCreateLinkedVariant);
  };

  let successGetSide2NatureKlass = function (oResponse) {
    let oSide2NatureKlassOfProductVariant = oResponse.success;

    VariantSectionViewProps.setSide2NatureClassOfProductVariant(oSide2NatureKlassOfProductVariant);
    _triggerChange();
  };

  let failureGetSide2NatureKlass = function (oResponse) {
    ContentUtils.failureCallback(oResponse,'failureGetSide2NatureKlass',getTranslation());
    _triggerChange();
  };

  let _getSide2NatureKlassFromNatureRelationship = function (sRelationshipId) {
    let oReferencedElement = ContentScreenProps.screen.getReferencedElements()[sRelationshipId];
    let sNatureRelationshipId = oReferencedElement.propertyId;
    let sURL = ContentScreenRequestMapping.GetSide2NatureKlassFromNatureRelationship;

    return CS.getRequest(sURL, {id: sNatureRelationshipId}, successGetSide2NatureKlass, failureGetSide2NatureKlass);
  };

  let _getSelectedTypeToCreateProductVariant = function () {
      let oSide2NatureClassOfProductVariant = VariantSectionViewProps.getSide2NatureClassOfProductVariant();
      let aActiveClassIds = CS.cloneDeep(ContentScreenProps.screen.getActiveClassIds());
      let oReferencedClasses = ContentScreenProps.screen.getReferencedClasses();
      let sActiveKlassTypeId = null;

      let aSelectedTypeIdsToCreateLinkedVariant = CS.remove(aActiveClassIds, function (sId) {
        if(oReferencedClasses[sId].isNature) {
          sActiveKlassTypeId = sId;
        }
        return !oReferencedClasses[sId].isNature;
      });

      aSelectedTypeIdsToCreateLinkedVariant.push(oSide2NatureClassOfProductVariant.id);

      return {
        aSelectedTypeIdsToCreateLinkedVariant,
        sActiveKlassTypeId
      };
  };

  let _getClassesToCloneForCreateClone = function () {
    let oSelectedIdsForCloningWizard = VariantSectionViewProps.getSelectedEntityIdsForCloning();
    if(CS.isNotEmpty(oSelectedIdsForCloningWizard)) {
      return oSelectedIdsForCloningWizard.classes;
    }
    else {
      return ContentScreenProps.screen.getActiveClassIds();
    }
  };

  let _prepareRequestDataAccordingToContext = function (oCallback) {
    let sCloneWizardContentId = VariantSectionViewProps.getCloningWizardContentId();
    let sActiveEntityId = CS.isNotEmpty(sCloneWizardContentId) ? sCloneWizardContentId : ContentUtils.getActiveEntity().id;
    let oSelectedIdsForCloningWizard = VariantSectionViewProps.getSelectedEntityIdsForCloning();
    let aSelectedTaxonomyIds = CS.isNotEmpty(oSelectedIdsForCloningWizard)
        ? CS.combine(oSelectedIdsForCloningWizard.taxonomies, oSelectedIdsForCloningWizard.minorTaxonomies)
        : ContentScreenProps.screen.getActiveTaxonomyIds();
    let aSelectedTypeIds = [];
    let sParentNatureKlassId = null;
    let sContext = CS.isNotEmpty(oCallback.context) ? oCallback.context : VariantSectionViewProps.getCloneWizardDialogContext();

    switch (sContext) {
      case "createClone":
      case "createTargetClone":
      case "createTextAssetClone":
        aSelectedTypeIds = _getClassesToCloneForCreateClone();
        break;

      case "productVariant":
        let {aSelectedTypeIdsToCreateLinkedVariant, sActiveKlassTypeId} = _getSelectedTypeToCreateProductVariant(oCallback);
        aSelectedTypeIds = aSelectedTypeIdsToCreateLinkedVariant;
        sParentNatureKlassId = sActiveKlassTypeId;
        break;
    }

    return {
      id: sActiveEntityId,
      selectedTypesIds: aSelectedTypeIds,
      selectedTaxonomyIds: aSelectedTaxonomyIds,
      parentNatureKlassId: sParentNatureKlassId,
      isForLinkedVariant: oCallback.context === "productVariant"
    }
  };

  let _fetchCloningWizardData = function (oCallback = {}) {
    let sCloneWizardContentId = VariantSectionViewProps.getCloningWizardContentId();
    oCallback.contentId = CS.isNotEmpty(sCloneWizardContentId) ? sCloneWizardContentId : ContentUtils.getActiveEntity().id;

    let fSuccess = successFetchCloningWizardData.bind(this, oCallback);
    let fFailure = failureFetchCloningWizardData.bind(this, oCallback);
    let oRequestData = CS.isNotEmpty(oCallback.requestData) ? oCallback.requestData : _prepareRequestDataAccordingToContext(oCallback);
    let sContext = oCallback.context ? oCallback.context : null;
    let sUrl = null;
    switch (sContext) {
      case "createTargetClone" :
        sUrl = ContentScreenRequestMapping.GetPropertiesToCloneTarget;
        break;
      case "createTextAssetClone":
        sUrl = ContentScreenRequestMapping.GetPropertiesToCloneTextAsset;
        break;
      default :
        sUrl = ContentScreenRequestMapping.GetPropertiesToClone;
    }
    CS.customPostRequest(sUrl, oRequestData, fSuccess, fFailure);
  };

  let _openCloneWizardDialog = function (sContext, oCallback = {}) {
    let oActiveEntity = ContentUtils.getActiveEntity();
    if (!CS.isEmpty(oActiveEntity.contentClone)) {
      ContentUtils.setShakingStatus(true);
      _triggerChange();
      return;
    }

    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    if (CS.isNotEmpty(oRelationshipContextData) && !ContentUtils.validateVariantContextSelection(oRelationshipContextData, oRelationshipContextData.context)) {
      return;
    }

    oCallback.context = sContext;
    oCallback.functionToExecute = function () {
      _createSelectedPropertiesForCloningWizard();
      VariantSectionViewProps.setIsCloningWizardOpen(true);
    };

    _fetchCloningWizardData(oCallback);
  };

  return {
    openCloneWizardDialog: function (sContext, oCallback) {
      _openCloneWizardDialog(sContext, oCallback);
    },

    handleCheckboxButtonClicked: function (sId, sGroupId, sContext) {
      _handleCheckboxButtonClicked(sId, sGroupId, sContext)
    },

    handleCheckboxHeaderButtonClicked: function (sKey) {
      _handleCheckboxHeaderButtonClicked(sKey)
    },

    handleExactCloneCheckboxClicked: function () {
      _handleExactCloneCheckboxClicked()
    },

    handleCancelCloningButtonClicked: function (bDoNotTrigger) {
      _handleCancelCloningButtonClicked(bDoNotTrigger);
    },

    handleCloningWizardCloneCountChanged: function (iCloneCount) {
      _handleCloningWizardCloneCountChanged(iCloneCount);
    },

    handleCreateCloneButtonClicked: function (sContextId, oModel, oFilterContext) {
      _handleCreateCloneButtonClicked(sContextId, oModel, oFilterContext);
    },

    handleCloneExpandSectionToggled: function (sTypeId) {
      _handleCloneExpandSectionToggled(sTypeId)
    },

    handleGetAllowedTypesToCreateLinkedVariant: function () {
      _handleGetAllowedTypesToCreateLinkedVariant();
    },

    handleSelectTypeToCreateLinkedVariant: function (sIdToRemove, sSelectedId) {
      _handleSelectTypeToCreateLinkedVariant(sIdToRemove, sSelectedId);
    },

    getSide2NatureKlassFromNatureRelationship: function (sRelationshipId) {
      return _getSide2NatureKlassFromNatureRelationship(sRelationshipId);
    },
  }
})();


MicroEvent.mixin(CloneWizardStore);

export default CloneWizardStore;
