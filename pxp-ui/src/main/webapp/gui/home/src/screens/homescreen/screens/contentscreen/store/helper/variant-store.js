import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ScreenModeUtils from './screen-mode-utils';
import ContentUtils from './content-utils';

import TagTypeConstants from '../../../../../../commonmodule/tack/tag-type-constants';
import EntitiesList from '../../../../../../commonmodule/tack/entities-list';
import CommonUtils from '../../../../../../commonmodule/util/common-utils';
import AttributeUtils from '../../../../../../commonmodule/util/attribute-utils';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary';
import BaseTypeDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import TaxonomyBaseTypeDictionary from '../../../../../../commonmodule/tack/mock-data-for-taxonomy-base-types-dictionary';
import SystemLevelIdDictionary from '../../../../../../commonmodule/tack/system-level-id-dictionary';
import ContentScreenConstants from './../model/content-screen-constants';
import ContentScreenViewContextConstants from './../../tack/content-screen-view-context-constants';
import ContentScreenProps from './../model/content-screen-props';
import UOMProps from './../model/uom-props';
import TableViewProps from './../model/table-view-props';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import EntitiesIdMap from '../../../../../../commonmodule/tack/entities-id-map';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import SessionStorageConstants from '../../../../../../commonmodule/tack/session-storage-constants';
import oFilterPropType from './../../tack/filter-prop-type-constants';
import BreadCrumbStore from '../../../../../../commonmodule/store/helper/breadcrumb-store';
import ContentGridStore from './content-grid-store';
import SessionStorageManager from './../../../../../../libraries/sessionstoragemanager/session-storage-manager';
import {
  gridViewPropertyTypes as GridViewPropertyTypes,
  gridViewPropertyTypes as oGridViewPropertyTypes
} from './../../../../../../viewlibraries/tack/view-library-constants';
import UniqueIdentifierGenerator
  from "../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator";
import {getTranslations} from "../../../../../../commonmodule/store/helper/translation-manager";
import CouplingConstants from "../../../../../../commonmodule/tack/coupling-constans";
import AttributeTypeDictionary from "../../../../../../commonmodule/tack/attribute-type-dictionary-new";
let getTranslation = ScreenModeUtils.getTranslationDictionary;
let getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

let VariantStore = (function () {

  let _triggerChange = function () {
    VariantStore.trigger('variant-change');
  };

  //TODO:   W E    M U S T    R E S E T    T H E    P R O P S    S O M E W H E R E!

  /******************* PRIVATE API  *******************/

  let _createFilterContext = function (sScreenContext) {
    return {
      filterType: oFilterPropType.MODULE,
      screenContext: sScreenContext
    };
  }

  let _getVariantContextPropsByContext = function (sContextId, sKlassInstanceId) {
    sKlassInstanceId = sKlassInstanceId || _getKlassInstanceId();
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId, sKlassInstanceId);
    return TableViewProps.getVariantContextPropsByContext(sIdForProps, sKlassInstanceId);
  };

  let _getTableViewPropsByContext = function (sContextId, sKlassInstanceId) {
    sKlassInstanceId = sKlassInstanceId || _getKlassInstanceId();
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId, sKlassInstanceId);
    return TableViewProps.getTableViewPropsByContext(sIdForProps, sKlassInstanceId);
  };

  let _handleUOMFilterButtonClicked = function (oExtraData) {
    oExtraData.klassInstanceId = _getKlassInstanceId();
    let oFilterStore = ContentUtils.getFilterStore(oExtraData.filterContext);
    oFilterStore.updateAppliedFilterDataFromClone(oExtraData);
    _fetchUOMData(oExtraData.contextId, false, "", "", {filterContext: oExtraData.filterContext}, oExtraData.klassInstanceId);
  };

  let _handleUOMChildFilterToggled = function (sParentId, sChildId, oExtraData) {
    oExtraData.klassInstanceId = _getKlassInstanceId();
    var oFilterStore = ContentUtils.getFilterStore(oExtraData.filterContext);
    oFilterStore.handleChildFilterToggled(sParentId, sChildId, false, oExtraData);
  };

  let _handleUOMRemoveFilterGroupClicked = function (sFilterGroupId, oExtraData) {
    oExtraData.klassInstanceId = _getKlassInstanceId();
    var oFilterStore = ContentUtils.getFilterStore(oExtraData.filterContext);
    oFilterStore.removeFilterByGroupId(sFilterGroupId, oExtraData);
    _fetchUOMData(oExtraData.contextId, false, "", "", {filterContext: oExtraData.filterContext}, oExtraData.klassInstanceId);
  };

  let _setCurrentDateForFilter = function (oContext) {
    if (oContext.isTimeEnabled) {
      let sIdForProps = ContentUtils.getIdForTableViewProps(oContext.id);
      let sActiveContentId = _getKlassInstanceId();
      let oUnitTableFilterProps = TableViewProps.getTableFilterViewPropsByContext(sIdForProps, sActiveContentId);
      let oCurrentDate = ContentUtils.getMomentOfDate();
      let iStartDate = oCurrentDate.startOf("day").valueOf();
      let iEndDate = oCurrentDate.endOf("day").valueOf();
      let oTimeRangeData = {
        from: iStartDate,
        to: iEndDate
      };
      oUnitTableFilterProps.setTimeRangeData(oTimeRangeData);
    }

  };

  let _handleUOMSectionExpandToggle = function (sSectionId) {
    let oSectionVisualProps = ContentScreenProps.contentSectionViewProps.getSectionVisualProps();
    let sIdForProps = ContentUtils.getIdForTableViewProps(sSectionId);
    let oActiveContent = ContentUtils.getActiveContent();
    let oCurrentSectionVisualProps = oSectionVisualProps[sSectionId];
    if (oCurrentSectionVisualProps) {
      if (oCurrentSectionVisualProps.isExpanded) {
        oCurrentSectionVisualProps.isExpanded = false;
      } else {
        let oCallBackData = {
          filterContext: _createFilterContext(sSectionId)
        };
        let oTableViewProps = ContentScreenProps.tableViewProps.getTableViewPropsByContext(sIdForProps, oActiveContent.id);
        oCurrentSectionVisualProps.isExpanded = true;
        if (!oTableViewProps.getTableOrganiserConfig().isTableContentDirty) {
          _fetchUOMData(sSectionId, null, null, true, oCallBackData);
          oTableViewProps.setIsSectionLoading(true);
        }
      }
    }
    _triggerChange();
  };

  // /**------------------------------------- Attribute Context STARTS here ---------------------------------------------*/

  let _getContextDialogLabel = function (oReferencedContext) {
    switch (oReferencedContext.type) {
      case  ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT:
        return getTranslation().ATTRIBUTE_VARIANTS;
      default:
        return CS.getLabelOrCode(oReferencedContext);
    }
  };

  let _handleAttributeContextViewShowVariantsClicked = function (sAttributeVariantContextId, sAttributeId, sVariantInstanceId, sParentContextId) {
    let oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    let oReferencedAttributes = ContentScreenProps.screen.getReferencedAttributes();
    let oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    let oReferencedContext = oEmbeddedVariantContexts[sAttributeVariantContextId];
    let sLabelForContextDialog = _getContextDialogLabel(oReferencedContext);
    sLabelForContextDialog += " - " + CS.getLabelOrCode(oReferencedAttributes[sAttributeId]);
    ContentUtils.setOpenedDialogAttributeData(sAttributeVariantContextId, sAttributeId, sLabelForContextDialog, oReferencedContext.type, sVariantInstanceId, sParentContextId);
    let sIdForProps = ContentUtils.getIdForTableViewProps(sAttributeVariantContextId);
    let oActiveContent = ContentUtils.getActiveContent();

    if (CS.isNotEmpty(sVariantInstanceId)) {
      let oContextProps = _getVariantContextPropsByContext(sParentContextId, oActiveContent.id);
      let oUOMTableProps = TableViewProps.getTableViewPropsByContext(sParentContextId, oActiveContent.id);
      let aTableBodyData = oUOMTableProps.getBodyData();
      oContextProps.setActiveVariantForEditing(CS.find(aTableBodyData, {id: sVariantInstanceId}));
    } else {
      sVariantInstanceId = ContentUtils.getActiveContent().id;
    }
    TableViewProps.createNewTableViewPropsByContext(sIdForProps, sVariantInstanceId); //should happen only once for the first time
    let oContextProps = TableViewProps.getTableViewPropsByContext(sIdForProps, sVariantInstanceId);
    oContextProps && oContextProps.setVariantInstanceId(sVariantInstanceId);
    _setCurrentDateForFilter(oReferencedContext);
    _fetchUOMData(sAttributeVariantContextId, sAttributeId, false, false, {}, sVariantInstanceId);
  };

  let _handleUOMViewVersionActionItemClicked = function (sContentId, sContextId) {
    //Single Price Context
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    oOpenedDialogAttributeData.contentIdForVersion = sContentId;
    _handleUOMViewModeMenuApply(sContextId, "version");
  };

  let _handleAttributeContextViewCloseDialog = function (sAttributeContextId) {
    let sIdForProps = ContentUtils.getIdForTableViewProps(sAttributeContextId);
    let oActiveContent = ContentUtils.getActiveContent();
    TableViewProps.resetPropsByContext(sIdForProps, oActiveContent.id); //clear the props of the context when it is collapsed
    UOMProps.setOpenedDialogAttributeData({
      contextId: "",
      attributeId: "",
      label: "",
      gridComponentKey: "",
      contextType: ""
    });
    let sSelectedTabId = ContentUtils.getSelectedTabId();
    let oContentStore = ContentUtils.getContentStore();
    oContentStore.handleEntityTabClicked(sSelectedTabId);
  };

  let _setEditableAttributeVariant = function (sVariantId, sTableContextId) {
    let sIdForProps = ContentUtils.getIdForTableViewProps(sTableContextId);
    let oActiveContent = ContentUtils.getActiveContent();
    let oUnitTableProps = TableViewProps.getTableViewPropsByContext(sIdForProps, oActiveContent.id);
    let aRowData = oUnitTableProps.getOriginalRowData();
    let oRow = CS.find(aRowData, {id: sVariantId});
    let oRowProperties = oRow.properties;
    let oVariantContextProps = _getVariantContextPropsByContext(sTableContextId);
    let oReferencedContexts = oVariantContextProps.getReferencedVariantContexts();
    let oReferencedInstances = oVariantContextProps.getReferencedInstances();
    let oReferencedContext = oReferencedContexts[sTableContextId];
    let aReferencedTags = oReferencedContext.tags;
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oVariant = {
      id: sVariantId,
      contextId: sTableContextId,
      name: "",
      value: "",
      baseType: oActiveEntity.baseType,
      tags: [],
      linkedInstances: {},
      timeRange: {
        to: null,
        from: null
      }
    };

    //for tags
    CS.forEach(aReferencedTags, function (oReferencedTag) {
      if (oRowProperties[oReferencedTag.id]) {
        let oTagInstance = oRowProperties[oReferencedTag.id];
        oVariant.tags.push(oTagInstance);
      }
    });

    //for value
    let oValueProperty = oRowProperties.value;
    if (oValueProperty) {
      oVariant.value = oValueProperty.valueAsHtml || oValueProperty.value;
    }

    //for 'From' in time range
    let oFromProperty = oRowProperties.from;
    if (oFromProperty) {
      oVariant.timeRange.from = +oFromProperty.value;
    }

    //for 'To' in time range
    let oToProperty = oRowProperties.to;
    if (oToProperty) {
      oVariant.timeRange.to = +oToProperty.value;
    }

    //for linked instances
    let oLinkedInstances = oVariant.linkedInstances;
    let aEntitiesList = EntitiesList();
    CS.forEach(oRowProperties, function (oRowProperty, sKey) {
      if (oRowProperty.id == "linkedInstances") {
        try {

          let oEntity = CS.find(aEntitiesList, {id: EntitiesIdMap[sKey]});
          oLinkedInstances[oEntity.id] = [];
          CS.forEach(oRowProperty.values, function (sId) {
            let oLinkedInstance = oReferencedInstances[sId];
            if (oLinkedInstance) {
              oLinkedInstances[oEntity.id].push(oLinkedInstance);
            }
          });
        }

        catch (oException) {
          ExceptionLogger.error(oException);
        }
      }
    });

    oVariantContextProps.setActiveVariantForEditing(oVariant);

    let sOpenDialogContext = "edit" + ContentUtils.getSplitter() + ContentScreenConstants.unitTableContext;
    oVariantContextProps.setVariantDialogOpenContext(sOpenDialogContext);
    oVariantContextProps.setIsVariantDialogOpen(true);
    UOMProps.setOpenedDialogTableContextId(sTableContextId);

    _triggerChange();
  };

  let _getADMForLinkedInstanced = function (oOldCell, oNewCell) {
    let aDeleted = CS.difference(oOldCell.values, oNewCell.values);
    let aAddedLinkedInstancedIds = CS.difference(oNewCell.values, oOldCell.values);
    let aAdded = [];
    let aReferencedLinkedEntities = oNewCell.referencedLinkedEntites;
    CS.forEach(aAddedLinkedInstancedIds, function (sId) {
      let oEntity = CS.find(aReferencedLinkedEntities, {id: sId});
      aAdded.push({
        id: sId,
        baseType: oEntity.baseType
      })
    });
    return {aAdded, aDeleted}
  };

  let _fillADMForTaxonomies = function (aAddedTaxonomyIds, aDeletedTaxonomyIds, oOldCell = {}, oNewCell) {
    let aNewTaxonomyIds = [];
    CS.forEach(oNewCell.model.selectedOptions, (oData) => {
      aNewTaxonomyIds.push(oData.id)
    });
    aDeletedTaxonomyIds.push(...CS.difference(oOldCell.selectedTaxonomies, aNewTaxonomyIds));
    aAddedTaxonomyIds.push(...CS.difference(aNewTaxonomyIds, oOldCell.selectedTaxonomies));
  };

  let _getInstancesToSaveFromTableData = function (sContextId, oReferencedContext, bIsSaveAndPublish) {
    let aInstancesToSave = [];
    let oActiveContent = ContentUtils.getActiveContent();
    let oUOMContextProps = TableViewProps.getVariantContextPropsByContext(sContextId, oActiveContent.id);
    let oSelectedVisibleContext = oUOMContextProps.getSelectedVisibleContext();
    let oUOMTableProps = TableViewProps.getTableViewPropsByContext(sContextId, oActiveContent.id);
    let aTableBodyData = oUOMTableProps.getBodyData();
    let aOriginalBodyData = CS.cloneDeep(oUOMTableProps.getOriginalRowData());
    let oActiveEntity = ContentUtils.getActiveContent();
    let bIsLinkedInstances = false;

    CS.forEach(aTableBodyData, function (oModifiedRow) {
      if (oModifiedRow.isDirty) {
        let oNameAttribute = oModifiedRow.properties[SystemLevelIdDictionary.NameAttributeId];
        let oInstanceToSave = {
          id: oModifiedRow.id,
          name:  oNameAttribute ? oNameAttribute.value : null,
          versionId: oModifiedRow.versionId,
          types: [oSelectedVisibleContext.contextKlassId],
          baseType: oActiveEntity.baseType
        };
        let aOldTags = [];
        let aNewTags = [];
        let aOldAttributes = [];
        let aNewAttributes = [];
        let aAddedLinkedInstances = [];
        let aDeletedLinkedInstances = [];
        let aAddedTaxonomyIds = [];
        let aDeletedTaxonomyIds = [];
        let oOriginalRow = CS.find(aOriginalBodyData, {id: oModifiedRow.id});
        let aLinkedInstance = [];

        CS.forEach(oModifiedRow.properties, function (oCell, sId) {
          if (oCell.isDirty) {
            delete oCell.isDirty;
            /** When instance already exist then we get baseTyp o.w we take type from header data **/
            let sCellType = oCell.baseType || oCell.type;
            switch (sCellType) {
              case BaseTypeDictionary.attributeInstanceBaseType:
              case "attribute":
                delete oCell.originalValue;
                oCell.valueAsExpression = oCell.expressionList;
                delete oCell.expressionList;
                aNewAttributes.push(oCell);
                let oOldAttribute = CS.find(oOriginalRow.properties, {"id": oCell.id});
                oOldAttribute && aOldAttributes.push(oOldAttribute);
                break;
              case BaseTypeDictionary.tagInstanceBaseType:
              case "tag":
                let oClonedCell = CS.cloneDeep(oCell);
                delete oClonedCell.tag;
                if(CS.isNotEmpty(oCell.customRequestObject) && CS.isNotEmpty(oCell.customRequestObject.contextId)){
                  oCell.contextInstanceId = oOriginalRow.id;
                  oCell.variantInstanceId = oOriginalRow.id;
                  oCell.klassInstanceId = oActiveEntity.id;
                }
                aNewTags.push(oClonedCell);
                let oOldTag = CS.find(oOriginalRow.properties, {"id": oCell.id});
                oOldTag && aOldTags.push(oOldTag);
                break;
              case BaseTypeDictionary.linkedInstancesPropertyBaseType:
              case "linkedInstances":
                let oOldCell = oOriginalRow.properties[sId];
                let {aAdded, aDeleted} = _getADMForLinkedInstanced(oOldCell, oCell);
                aDeletedLinkedInstances.push(...aDeleted);
                aAddedLinkedInstances.push(...aAdded);
                break;
              case TaxonomyBaseTypeDictionary.masterTaxonomy:
                _fillADMForTaxonomies(aAddedTaxonomyIds, aDeletedTaxonomyIds, oOriginalRow.properties[sId], oCell);
                break;
            }
          }
          if (oCell.baseType == BaseTypeDictionary.linkedInstancesPropertyBaseType) {
            aLinkedInstance = aLinkedInstance.concat(oCell.values);
          }
        });

        if (CS.isEmpty(aLinkedInstance) && !CS.isEmpty(oReferencedContext.entities)) {
          bIsLinkedInstances = true;
          return;
        }

        let oADMForAttributes = ContentUtils.generateADMForAttribute(aOldAttributes, aNewAttributes);
        let oADMForTags = ContentUtils.generateADMForTags(aOldTags, aNewTags);

        oInstanceToSave.addedAttributes = oADMForAttributes.added;
        oInstanceToSave.deletedAttributes = oADMForAttributes.deleted;
        oInstanceToSave.modifiedAttributes = oADMForAttributes.modified;
        oInstanceToSave.addedTags = oADMForTags.added;
        oInstanceToSave.deletedTags = oADMForTags.deleted;
        oInstanceToSave.modifiedTags = oADMForTags.modified;
        oInstanceToSave.addedLinkedInstances = aAddedLinkedInstances;
        oInstanceToSave.deletedLinkedInstances = aDeletedLinkedInstances;
        oInstanceToSave.addedTaxonomyIds = aAddedTaxonomyIds;
        oInstanceToSave.deletedTaxonomyIds = aDeletedTaxonomyIds;

        CS.forEach(oInstanceToSave.addedAttributes, function (oAddedAttribute) {
          delete oAddedAttribute.valueAsNumber;
        });

        CS.forEach(oInstanceToSave.modifiedAttributes, function (oModifiedAttribute) {
          delete oModifiedAttribute.valueAsNumber;
        });

        if (oOriginalRow.properties.to && oOriginalRow.properties.from) {
          if (oOriginalRow.properties.to.value !== oModifiedRow.properties.to.value || oOriginalRow.properties.from.value !== oModifiedRow.properties.from.value) {
            oInstanceToSave.timeRange = {};
            oInstanceToSave.timeRange.to = oModifiedRow.properties.to.value * 1;
            oInstanceToSave.timeRange.from = oModifiedRow.properties.from.value * 1;
          }
        }

        /**Sending a saveAndPublish key only in case of "saveAndPublish" is true*/
        if (bIsSaveAndPublish) {
          oInstanceToSave.isSaveAndPublish = true;
        }
        aInstancesToSave.push(oInstanceToSave);
      }
    });

    return bIsLinkedInstances ? null : aInstancesToSave;
  };

  let _fetchUOMData = function (sContextId, sAttributeId, bDoNotPreserveColumnSequence, bIsDisableLoader, oCallBackData = {}, sParentId = ContentUtils.getActiveContent().id) {
    //sAttributeId is optional. Will be req. only in case of attribute context.
    let oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    let oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    let oReferencedContext = oEmbeddedVariantContexts[sContextId] || {};
    let oActiveEntity = ContentUtils.getActiveContent();
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);

    sParentId = CS.isEmpty(sParentId) ? ContentUtils.getActiveContent().id : sParentId;
    let oUnitTableProps = TableViewProps.getTableViewPropsByContext(sIdForProps, sParentId);
    let aColumnIds = [];

    /**
     * below handling is for attribute context creation dialog.
     **/
    let {aAttributes, aTags, sAllSearchText, aSortOptions, oPaginationData, oTimeRangeData} = _getFetchUOMFilterInformation(sIdForProps, sParentId);

    if (!CS.isEmpty(oUnitTableProps)) {
      aColumnIds = oUnitTableProps.getSelectedHeaders();
    }

    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    sAttributeId = sAttributeId || (oOpenedDialogAttributeData.contextId == sContextId ? oOpenedDialogAttributeData.attributeId : sAttributeId);
    let oExtraData = {
      attributeId: sAttributeId,
      klassInstanceId: sParentId
    };

    let sKlassInstanceId = oActiveEntity.id;
    if (!CS.isEmpty(oActiveEntity.context)) {
      sKlassInstanceId = oActiveEntity.klassInstanceId;
    }

    let oData = {
      contextId: sContextId,
      klassInstanceId: sKlassInstanceId,
      parentId: sParentId,
      attributes: aAttributes,
      tags: aTags,
      allSearch: sAllSearchText,
      sortOptions: aSortOptions,
      from: oPaginationData.from || 0,
      size: oPaginationData.pageSize || 20,
      columnIds: [],
      timeRange: oTimeRangeData
    };

    oExtraData.columnIds = aColumnIds;
    oExtraData.doNotPreserveColumnSequence = bDoNotPreserveColumnSequence;
    let sTableContext = UOMProps.getOpenedDialogTableContextId();
    let sURL = "";
    let oUOMVariantContextProps = TableViewProps.getVariantContextPropsByContext(sIdForProps, sParentId);
    var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);

    //Onclick of save button close variant quicklist view
    if (!CS.isEmpty(sTableContext) || (_getIsEditFromTableEnable(oReferencedContext)) &&
        (!CS.isEmpty(oUOMVariantContextProps) && (oUOMVariantContextProps.getIsVariantDialogOpen() ||
            !CS.isEmpty(oUOMVariantContextProps.getDummyVariant()))) ||
        (!CS.isEmpty(oUnitTableProps) && oUnitTableProps.getTableOrganiserConfig().isTableContentDirty)) {
      oExtraData.shouldCloseVariantDialog = true;
    }

    let fSuccess = successFetchUOMData.bind(this, sContextId, oExtraData, oCallBackData);
    let fFailure = failureFetchUOMData.bind(this, sContextId);

    if (oReferencedContext.type === ContextTypeDictionary.IMAGE_VARIANT) {
      oExtraData.viewContext = ContentScreenViewContextConstants.IMAGE_VARIANT_CONTEXT;
    }
    if (oReferencedContext.type === ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
      sURL = getRequestMapping(sScreenMode).GetPropertyTableViewData;
      oData.attributeId = sAttributeId;
      oData.templateId = ContentUtils.getTemplateIdForServer(ContentScreenProps.screen.getSelectedTemplateId());
    } else if (_getIsEditFromTableEnable(oReferencedContext) &&
        (!CS.isEmpty(oUOMVariantContextProps) && (oUOMVariantContextProps.getIsVariantDialogOpen() || !CS.isEmpty(oUOMVariantContextProps.getDummyVariant())))) {
      sURL = getRequestMapping(sScreenMode).CreateLimitedObject;
      let sLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
      let oRequestData = {
        createVariantRequest: _getCreateVariantRequestData(sContextId),
        tableViewRequest: oData
      };
      oRequestData.createVariantRequest.name = ContentUtils.getActiveContent().name;
      CS.putRequest(sURL, {lang: sLanguage}, oRequestData, fSuccess, fFailure);
      return;
    } else if (!CS.isEmpty(oUnitTableProps) && oUnitTableProps.getTableOrganiserConfig().isTableContentDirty) {
      oExtraData.isBulkSaveTableDataSuccess = true;
      oExtraData.doNotPreserveColumnSequence = true;
      /**sURL = getRequestMapping(sScreenMode).BulkSaveVariantsTableData;
       let oInstancesToSave = _getInstancesToSaveFromTableData(sContextId, oReferencedContext, oCallBackData.isSaveAndPublish);
       if (!oInstancesToSave) {
        let sEntityName = ContentUtils.getContentName(oActiveEntity)
        alertify.error(getTranslation().EmptyMandatoryFieldsException + " [" + sEntityName + "]");
        return;
      }
       let oRequestData = {
        instancesToSave: oInstancesToSave,
        tableViewRequest: oData
      };
       CS.postRequest(sURL, {}, oRequestData, fSuccess, fFailure, bIsDisableLoader);*/
      let oInstancesToSave = _getInstancesToSaveFromTableData(sContextId, oReferencedContext, oCallBackData.isSaveAndPublish);
      _bulkSaveVariantTableData(sContextId, oReferencedContext, bIsDisableLoader, oActiveEntity, oData, oCallBackData, oExtraData, oInstancesToSave);
      return;

    } else {
      let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);
      sURL = getRequestMapping(sScreenMode).GetVariantTableViewData;
    }

    CS.postRequest(sURL, {}, oData, fSuccess, fFailure, true);
  };

  let _bulkSaveVariantTableData = function (sContextId, oReferencedContext, bIsDisableLoader, oActiveEntity, oData, oCallBackData, oExtraData, oInstancesToSave) {
    var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);
    let sURL = getRequestMapping(sScreenMode).BulkSaveVariantsTableData;

    if (!oInstancesToSave) {
      let sEntityName = ContentUtils.getContentName(oActiveEntity)
      alertify.error(getTranslation().EmptyMandatoryFieldsException + " [" + sEntityName + "]");
      return;
    }

    oExtraData.totalInstancesToSave = CS.size(oInstancesToSave);

    oExtraData.isBulkSave = true;
    let fSuccess = successFetchUOMData.bind(this, sContextId, oExtraData, oCallBackData);
    let fFailure = failureFetchUOMData.bind(this, sContextId);
    CS.postRequest(sURL, {}, {
      instancesToSave: oInstancesToSave,
      tableViewRequest: oData
    }, fSuccess, fFailure, bIsDisableLoader);
  };

  let _getFetchUOMFilterInformation = function (sIdForProps, sContentId) {
    let oUnitTableFilterProps = TableViewProps.getTableFilterViewPropsByContext(sIdForProps, sContentId);
    let aAttributes = [];
    let aTags = [];
    let sAllSearchText = "";
    let aSortOptions = [];
    let oPaginationData = {};
    let oTimeRangeData = {
      from: null,
      to: null
    };
    if (CS.isNotEmpty(oUnitTableFilterProps)) {
      let aAppliedFilters = oUnitTableFilterProps.getAppliedFilters();
      let oFilterContext = _createFilterContext(sIdForProps);
      let oFilterStore = ContentUtils.getFilterStore(oFilterContext);
      let oFilterData = oFilterStore.makeFilterDataForAppliedAttributeAndTags(aAppliedFilters, false);
      let bIsRenditionTab = ContentUtils.getSelectedTabId() === ContentScreenConstants.tabItems.TAB_RENDITION;
      aAttributes = oFilterData.attributes;
      aTags = oFilterData.tags;
      sAllSearchText = oUnitTableFilterProps.getAllSearchText();
      let oSortData = oUnitTableFilterProps.getSortData();
      oPaginationData = oUnitTableFilterProps.getPaginationData();
      oTimeRangeData = bIsRenditionTab ? oTimeRangeData : oUnitTableFilterProps.getTimeRangeData();
      if (oSortData.sortBy) {
        aSortOptions.push({
          sortField: oSortData.sortBy,
          sortOrder: oSortData.sortOrder
        });
      }
    }

    return {aAttributes, aTags, sAllSearchText, aSortOptions, oPaginationData, oTimeRangeData};
  };

  let _getShouldEnableOpen = function (sContextId, oUnitVariantContextProps) {
    if (CS.isEmpty(oUnitVariantContextProps)) {
      oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);
    }
    let oSelectedVisibleContext = oUnitVariantContextProps.getSelectedVisibleContext() || {};
    return _getIsEditFromTableEnable(oSelectedVisibleContext);
  };

  let _setSelectedAndVisibleContextContexts = function (sContextId, oUnitVariantContextProps) {
    if (CS.isEmpty(oUnitVariantContextProps)) {
      oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);
    }
    var oReferencedContexts = oUnitVariantContextProps.getReferencedVariantContexts();
    if (oReferencedContexts) {
      var oSelectedContext = sContextId ? oReferencedContexts[sContextId] : {};
      oSelectedContext = oSelectedContext || {};
      oUnitVariantContextProps.setSelectedContext(oSelectedContext);
      oUnitVariantContextProps.setSelectedVisibleContext(oSelectedContext);
    }
    else {
      oUnitVariantContextProps.setSelectedVisibleContext({});
      oUnitVariantContextProps.setSelectedContext({});
    }
  };

  let _getGridColumnModel = function (oHeader, iWidth, sType, bIsDisabled, bIsSortable, sCode, oExtraData, sClassName) {
    return {
      id: oHeader.id,
      label: oHeader.value,
      width: iWidth,
      isVisible: true,
      isMultiLine: false,
      isDisabled: bIsDisabled,
      isSortable: bIsSortable,
      code: sCode,
      extraData: oExtraData,
      type: sType || oGridViewPropertyTypes.TEXT,
      iconClassName: sClassName,
      iconKey:oHeader.iconKey,
      showDefaultIcon: oHeader.showDefaultIcon
    };
  };

  let _getGridSkeletonByColumnType = function (aHeaderData, oVisualProps, oConfigDetails, bShouldSplit, sTableContextId) {
    let {actionItems: aActionItems, sortableIds: aSortableIds, fixedColumnIds: aFixedColumnIds} = oVisualProps;
    let {referencedTags: oReferencedTags, referencedAttributes: oReferencedAttributes,
      referencedTaxonomies: oReferencedTaxonomies} = oConfigDetails;
    let aScrollableColumns = [];
    let aFixedColumns = [];

    CS.forEach(aHeaderData, function (oHeader) {
      let bIsDisabled = oVisualProps.allColumnsDisabled || false;

      let bIsSortable = CS.includes(aSortableIds, oHeader.id);
      let sType = oGridViewPropertyTypes.TEXT;
      let sIdForReferencedData = oHeader.id;
      let oExtraData = {};
      if (bIsDisabled && CS.includes(oVisualProps.priorityEditableColumns, oHeader.id)) {
        bIsDisabled = false;
      } else if (!bIsDisabled && CS.includes(oVisualProps.priorityDisabledColumns, oHeader.id)) {
        bIsDisabled = true;
      }
      if (bShouldSplit) {
        let aSplit = CS.split(oHeader.id, "__");
        if (aSplit && aSplit.length > 1) {
          CS.isNotEmpty(aSplit[1]) && (sIdForReferencedData = aSplit[1])
        }
      }
      let sCode = "";
      let sClassName = "";

      switch (oHeader.type) {
        case "custom":
          sType = oGridViewPropertyTypes.FLEXIBLE;
          break;
        case "tag":
          sType = oGridViewPropertyTypes.TAG;
          let oReferencedTag = oReferencedTags[sIdForReferencedData] || {};
          oExtraData = {
            referencedTag: oReferencedTag
          };
          sCode = oReferencedTag.code;
          sClassName = "Tag";
          break;

        case "taxonomy":
          sType = oGridViewPropertyTypes.CHIPS_WITH_MORE_OPTION;
          let oReferencedTaxonomy = oReferencedTaxonomies[sIdForReferencedData] || {};
          sCode = oReferencedTaxonomy.code;
          sClassName = "Taxonomy";
          break;

        case "linkedInstances":
          sType = oGridViewPropertyTypes.LINKED_INSTANCE;
          break;

        case "attribute":
          sType = oGridViewPropertyTypes.FLEXIBLE;
          sCode = oReferencedAttributes[sIdForReferencedData].code;
          sClassName = "Attribute";
          break;

        case "date":
          sType = oGridViewPropertyTypes.DATE;
          sClassName = "Context";
          break;

        case "image":
          sType = oGridViewPropertyTypes.IMAGE;
          break;
      }

      /** If any column width is resized, then preserve it **/
      let sIdForProps = ContentUtils.getIdForTableViewProps(sTableContextId);
      let sActiveContentId = _getKlassInstanceId();
      let oGridProps = TableViewProps.getTableViewPropsByContext(sIdForProps, sActiveContentId);
      let oSkeleton = oGridProps.getGridSkeleton();
      let obj = oSkeleton && CS.find(oSkeleton.scrollableColumns, {id: oHeader.id});
      let ofixedObj = oSkeleton && CS.find(oSkeleton.fixedColumns, {id: oHeader.id});
      let iWidth = CS.isNotEmpty(obj) ? obj.width : 200;
      if (oHeader.id === "icon") {
        iWidth = CS.isNotEmpty(ofixedObj) ? ofixedObj.width : 119;
      }

      if (CS.includes(aFixedColumnIds, oHeader.id)) {
        aFixedColumns.push(_getGridColumnModel(oHeader, iWidth, sType, bIsDisabled, bIsSortable, sCode, oExtraData, sClassName));
      } else {
        aScrollableColumns.push(_getGridColumnModel(oHeader, iWidth, sType, bIsDisabled, bIsSortable, sCode, oExtraData, sClassName));
      }
    });
    return {
      fixedColumns: aFixedColumns,
      scrollableColumns: aScrollableColumns,
      actionItems: aActionItems,
      selectedContentIds: []
    }
  };

  let _getGridPropertyForAttribute = (oRow, oAttributeInstance, oReferencedAttribute = {}, oReferencedElement = {}, oKlassInstance = ContentUtils.getActiveContent(), oConfigDetails) => {
    let oGridProperty = {
      value: oAttributeInstance.value
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
      oGridProperty.rendererType = oReferencedAttribute.calculatedAttributeType ? GridViewPropertyTypes.MEASUREMENT : GridViewPropertyTypes.NUMBER;
      oGridProperty.isDisabled = true;
      oGridProperty.isHideDisabledMask = false;
      oGridProperty.defaultUnitAsHTML = oReferencedAttribute.calculatedAttributeUnitAsHTML;
      let sCalculatedAttributeType = oReferencedAttribute.calculatedAttributeType;
      oGridProperty.disableNumberLocaleFormatting = oReferencedAttribute.hideSeparator;
      if (!CS.isEmpty(sCalculatedAttributeType)) {
        oGridProperty.type = sCalculatedAttributeType;
        oGridProperty.defaultUnit = oReferencedAttribute.calculatedAttributeUnit;
        oGridProperty.converterVisibility = true;
      }
    } else if (ContentUtils.isAttributeTypeConcatenated(sType)) {
      oGridProperty.rendererType = GridViewPropertyTypes.CONCATENATED;
      oGridProperty.masterAttribute = oReferencedAttribute || oReferencedElement;
      let oReferencedAttributes = oConfigDetails.referencedAttributes;
      let oReferencedTags = oConfigDetails.referencedTags;
      let oReferencedElements = oConfigDetails.referencedElements;
      oGridProperty.expressionList = ContentUtils.getConcatenatedAttributeExpressionList(oAttributeInstance, oRow.properties, oRow.properties, oReferencedAttributes, oReferencedTags, oReferencedElements);
      //oGridProperty.isDisabled = true;
    } else if (ContentUtils.isAttributeTypeDate(sType)) {
      if (ContentUtils.isAttributeTypeCreatedOn(sType) || ContentUtils.isAttributeTypeLastModified(sType)) {
        oGridProperty.isDisabled = true;
      }
      oGridProperty.rendererType = GridViewPropertyTypes.DATE;

    } else if (ContentUtils.isAttributeTypeHtml(sType)) {
      oGridProperty.value = oAttributeInstance && oAttributeInstance.valueAsHtml || "";
      oGridProperty.valueAsHtml =  oAttributeInstance && oAttributeInstance.value || "";
      oGridProperty.toolbarIcons = (oReferencedAttribute.validator && oReferencedAttribute.validator.allowedRTEIcons) || [];
      oGridProperty.rendererType = GridViewPropertyTypes.HTML;
      oGridProperty.fixedToolbar = false;

    } else if (ContentUtils.isAttributeTypeUser(sType)) {
      oGridProperty.isDisabled = true;
      oGridProperty.rendererType = GridViewPropertyTypes.TEXT;

    } else {
      oGridProperty.rendererType = GridViewPropertyTypes.TEXT;
      oGridProperty.showTooltip = true;
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
    }

    return oGridProperty;
  };

  let _fillGridPropertyModel = function (oRow, sId, oProperty, oVisualProps, oConfigDetails, oReferencedInstances, oDateFieldInfo = {}, bRowDisabled = false, sViewContext = "", sContextId) {
    let {referencedAttributes: oReferencedAttributes, referencedElements: oReferencedElements, referencedTags: oReferencedTags,
      referencedTaxonomies: oReferencedTaxonomies} = oConfigDetails;
    /** When instance already exist then we get baseTyp o.w we take type from header data **/
    let sPropertyType = oProperty.baseType || oProperty.type;
    let oModel = {};
    let bIsDisabled = oVisualProps.allColumnsDisabled || false;
    if (bIsDisabled && CS.includes(oVisualProps.priorityEditableColumns, sId)) {
      bIsDisabled = false;
    } else if (!bIsDisabled && CS.includes(oVisualProps.priorityDisabledColumns, sId)) {
      bIsDisabled = true;
    }
    let bHideDisabledMask = !bRowDisabled;

    let oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);
    let oReferencedVariantContexts = oUnitVariantContextProps && oUnitVariantContextProps.getReferencedVariantContexts();
    let oReferencedContext = oReferencedVariantContexts[sContextId] || {};
    let aReferencedContextTags = oReferencedContext.tags || [];
    let oContextTag = CS.find(aReferencedContextTags, {id: sId});
    let bTagDisabled = false;
    if (oContextTag) {
      oProperty.customRequestObject = {
        contextId: oReferencedContext.id,
        entityName: "contextTagValues",
      }
    }

    switch (sPropertyType) {    // not present in referencedElement // check if required for attributeVariantContext
      case BaseTypeDictionary.attributeInstanceBaseType:
      case "attribute":
        let sAttributeId = oProperty.attributeId;
        if (CS.isEmpty(sAttributeId)) {
          if (sId == "versionTimestamp") {
            let oDateTime = ContentUtils.getDateAttributeInDateTimeFormat(oProperty.value);
            oProperty.value = `${oDateTime.date} ${oDateTime.time}`;
          } else if (sId == "lastModifiedBy") {
            let aMasterUserList = ContentUtils.getUserList();
            let oLastModifiedBy = ContentUtils.getUserById(oProperty.value, aMasterUserList);
            let sLastModifiedBy = CS.trim(CS.isNotEmpty(oLastModifiedBy) ? (oLastModifiedBy.firstName + " " + oLastModifiedBy.lastName) : "");
            oProperty.value = sLastModifiedBy;
          } else {
            return;
          }
        }

        let oAttributeReferencedElements = CS.isNotEmpty(oReferencedElements) &&  oReferencedElements[sAttributeId] || {};
        oModel = _getGridPropertyForAttribute(oRow, oProperty, oReferencedAttributes[sAttributeId], oAttributeReferencedElements, ContentUtils.getActiveContent(), oConfigDetails);
        break;

      case BaseTypeDictionary.tagInstanceBaseType:
      case "tag":
        let sTagId = oProperty.tagId;
        let oMasterTag = CS.find(oReferencedTags, {id: sTagId});
        if (CS.isEmpty(sTagId)) {
          return;
        }
        bTagDisabled = oMasterTag.isDisabled;
        let oReferencedElement = CS.isNotEmpty(oReferencedElements) &&  oReferencedElements[sTagId] || oReferencedTags[sTagId];

        /**For YN Tag, when multiSelect is off then remove previously selected tags**/
        if (oMasterTag.tagType === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
          if (!oReferencedElement.isMultiselect) {
            let oContent = {tags: [oProperty]};
            ContentUtils.removeOldTagsByTimeStampFromContentUsingTagGroup(oContent, oMasterTag);
          }
        }

        ContentUtils.addTagsInContentBasedOnTagType(oMasterTag, oProperty);
        oModel = {
          tag: CS.cloneDeep(oProperty)
        };

        if (oContextTag) {
          oProperty.customRequestObject = {
            contextId: oReferencedContext.id,
            entityName: "contextTagValues",
          }
        }
        else {
          oProperty.customRequestObject = {
            klassInstanceId: oRow.id,
            baseType: ContentUtils.getActiveContent().baseType,
          };
        }
        break;

      case TaxonomyBaseTypeDictionary.masterTaxonomy:
      case "minorTaxonomy":
        // let sTaxonomyId = oProperty.taxonomyId;
          let aSelectedOptions = [];
          CS.forEach(oProperty.selectedTaxonomies, (sId) => {
            aSelectedOptions.push({
              id: sId
            });
          });

        let oMultiClassificationViewData = {
          id: oProperty.id,
          showClassificationDialog: false,
          showEditClassificationIcon: true,
          multiClassificationData: {
            taxonomies: {}
          },
        };
        oProperty.model = {
          context: "minorTaxonomiesInEmbeddedTable",
          dialogContext: "minorTaxonomiesDialogContextInEmbeddedTable",
          selectedOptions: aSelectedOptions,
          referencedData: oReferencedTaxonomies,
          multiClassificationViewData: oMultiClassificationViewData,
          doNotShowClassification: true,
        };
        break;


      case BaseTypeDictionary.linkedInstancesPropertyBaseType:
      case "linkedInstances":
        let aLinkedInstances = [];
        let aAssetRelationshipEntities = [];
        CS.forEach(oProperty.values, function (sId) {
          let oReferencedInstance = oReferencedInstances[sId];
          if (oReferencedInstance) {
            oReferencedInstance.label = CS.getLabelOrCode(oReferencedInstance);
            aLinkedInstances.push(oReferencedInstance);
          }
        });
        oProperty.referencedLinkedEntites = aLinkedInstances;
        oProperty.items = aLinkedInstances;
        oProperty.assetRelationshipEntities = aAssetRelationshipEntities;
        oModel.cannotAdd = oVisualProps.cannotAddLinkedInstances;
        oModel.cannotRemove = oVisualProps.cannotRemoveLinkedInstances;
        break;

      case BaseTypeDictionary.dateInstanceBaseType:
        let sHeaderId = oProperty.id;
        if (sHeaderId === "from") {
          oProperty.maxValue = oDateFieldInfo.to;
          oProperty.isMandatory = true;
        } else if (sHeaderId === "to") {
          oProperty.minValue = oDateFieldInfo.from;
          oProperty.isMandatory = true;
        }
        oProperty.originalValue = oProperty.value;
        break;

    }

    oProperty.isDisabled = bIsDisabled;
    oProperty.isHideDisabledMask = false;
    let oSelectedVisibleContextType = oUnitVariantContextProps.getSelectedVisibleContext().type;
    if (oSelectedVisibleContextType !== ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT){
      oProperty.isDisabled = bIsDisabled || ContentUtils.getIsContentDisabled(oRow) || bTagDisabled;
      oProperty.isHideDisabledMask = oProperty.isHideDisabledMask || !ContentUtils.getIsContentDisabled(oRow);
    }

    if (oSelectedVisibleContextType === ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
      oProperty.isDisabled = bIsDisabled || ContentUtils.getIsCurrentUserReadOnly();
      oProperty.isHideDisabledMask = bHideDisabledMask || !ContentUtils.getIsCurrentUserReadOnly();
    }

    if (oRow.mdmKlassInstanceId && (oContextTag || oProperty.id === "linkedInstances")) {
      oProperty.isDisabled = true;
    }
    CS.assign(oProperty, oModel);
  };

  let _getDummyInstanceOfProperty = (oProperty, sPropertyType, sPropertyId) =>{
    oProperty = {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: sPropertyType,
      value: ""
    };

    switch (sPropertyType) {
      case "tag":
        oProperty.tagId = sPropertyId;
        oProperty.baseType = BaseTypeDictionary.tagInstanceBaseType;
        oProperty.tagValues = [];
        break;

      case "attribute":
        oProperty.attributeId = sPropertyId;
        oProperty.baseType = BaseTypeDictionary.attributeInstanceBaseType;
        break;

      case "taxonomy":
        // oProperty.taxonomyId = sPropertyId;
        oProperty.id = sPropertyId;
        oProperty.baseType = TaxonomyBaseTypeDictionary.masterTaxonomy;
        break;
    }

    return oProperty;
  };

  let _fillGridDataInRows = function (aRows, oVisualProps, oConfigDetails, oReferencedInstances, aCommonRows, sViewContext, sContextId, aHeaderData = []) {
    let {referencedAttributes: oReferencedAttributes, referencedElements: oReferencedElements,
      referencedTags: oReferencedTags, referencedTaxonomies: oReferencedTaxonomies,
      instanceIdVsReferencedElements: oInstanceIdVsReferencedElements} = oConfigDetails;
    let oReferencedEmbeddedVariantContexts = oConfigDetails.referencedVariantContexts
        .embeddedVariantContexts;

    let oContentStore = ContentUtils.getContentStore();

    CS.forEach(aRows, function (oRow) {
      let oProperties = oRow.properties;
      let oDateFieldInfo = {};
      var oFrom = oProperties["from"];
      var oTo = oProperties["to"];
      if (oFrom && oTo) {
        oDateFieldInfo.from = +oFrom.value || +oFrom.originalValue;
        oDateFieldInfo.to = +oTo.value || +oTo.originalValue;
      }

      oReferencedElements = oInstanceIdVsReferencedElements && oInstanceIdVsReferencedElements[oRow.id]
          || oReferencedElements;

      CS.isNotEmpty(oRow.attributeVariantsStats) && oContentStore.processAttributeVariantsStats(oRow.attributeVariantsStats, oReferencedAttributes, oReferencedElements);

      CS.forEach(aHeaderData, function (oHeaderData) {
        let sPropertyId = oHeaderData.id;
        let oProperty = oProperties[sPropertyId];
        if (CS.isEmpty(oProperty)){
          oProperties[sPropertyId] = _getDummyInstanceOfProperty(oProperty, oHeaderData.type, sPropertyId)
        }

        let aContextTags = oReferencedEmbeddedVariantContexts[sContextId].tags;
        let bIsPropertyAvailable = !CS.isEmpty(oReferencedElements[sPropertyId])
            || CS.find(aContextTags, {id: sPropertyId});
        if((oHeaderData.type === "attribute" || oHeaderData.type === "tag" || oHeaderData.type === "taxonomy")
            && !bIsPropertyAvailable){
          oProperties[sPropertyId] = {};
        } else{
          _fillGridPropertyModel(oRow, sPropertyId, oProperties[sPropertyId], oVisualProps, oConfigDetails, oReferencedInstances, oDateFieldInfo, oRow.isDisabled, sViewContext, sContextId);
        }
      });

      /** For attribute and price context when attribute instance is not present then add it from referencedElements */
      CS.forEach(oReferencedElements, function (oReferencedElement, sPropertyId) {
        if (oReferencedElement.attributeVariantContext) {
          // context
          let oElement = CS.cloneDeep(oReferencedElement);
          oProperties[sPropertyId] = oElement;
          oProperties[sPropertyId].attributeVariantsStats = oRow.attributeVariantsStats[sPropertyId] || {};
          let oModel = ContentGridStore.getGridPropertyForAttribute(oElement, oReferencedAttributes[sPropertyId], oReferencedElement, ContentUtils.getActiveContent());
          CS.assign(oElement, oModel);
        }
        if (oReferencedElement.attributeVariantContext || (oProperties[sPropertyId] && oProperties[sPropertyId].attributeVariantContext)) {
          let oProperty = oProperties[sPropertyId];
          let bIsTargetPriceId = oProperty.attributeId === oConfigDetails.targetPriceId;
          let bIsContentEditable = !ContentUtils.getIsContentDisabled(oRow);
          let bIsDisabled = oProperty.isDisabled || (!bIsContentEditable && !bIsTargetPriceId);
          oProperty.variantInstanceId = oRow.id;
          oProperty.parentContextId = sContextId;

          /**For read only user with attribute context**/
          let bIsCurrentUserReadOnly = ContentUtils.getIsCurrentUserReadOnly();
          let bIsContentAvailableInSelectedDataLanguage = ContentUtils.isContentAvailableInSelectedDataLanguage(oRow);
          if (bIsCurrentUserReadOnly && bIsContentAvailableInSelectedDataLanguage) {
            oProperty.isDisabled = !bIsCurrentUserReadOnly;
            oProperty.isHideDisabledMask = bIsCurrentUserReadOnly
          } else {
            oProperty.isDisabled = bIsDisabled;
            oProperty.isHideDisabledMask = bIsContentEditable;
          }
        }
      });
      oRow.actionItemsToShow = oVisualProps.actionItemsToShow;
      /** For preview of technical image variant */
      if (ContentUtils.getSelectedTabId() === ContentScreenConstants.tabItems.TAB_RENDITION && CS.isNotEmpty(oRow.assetInformation)) {
        let oProperty = {};
        oProperty.propertyId = "icon";
        oProperty.contentId = "preview";
        oProperty.variantInstanceId = oRow.id;
        oProperty.parentContextId = sContextId;
        oProperty.isDisabled = true;
        oProperty.rendererType = GridViewPropertyTypes.IMAGE;
        oProperty.value = oRow.assetInformation.thumbKey;
        oProperty.isIconImageType = true;
        oProperty.isHideDisabledMask = !ContentUtils.getIsContentDisabled(oRow);
        oProperties[oProperty.propertyId] = oProperty;
      }
      if (CS.isNotEmpty(oRow.instanceId) && CS.isNotEmpty(aCommonRows)) {
        let oFoundRow = CS.find(aCommonRows, {id: oRow.instanceId});
        CS.isNotEmpty(oFoundRow) && CS.assign(oRow.properties, oFoundRow.properties);
      }
     CS.forEach(oProperties, function (oProperty) {
       let bIsTag = (oProperty.baseType === BaseTypeDictionary.tagInstanceBaseType);
       if(bIsTag){
         oProperty.showDefaultIcon = true
       }
     });
    });

  };

  let _generateUOMVisualPropsByViewContext = function (sViewContext, oConfigDetails, oTableOrganiserConfig, oGridVisualProps, oSettings, aSortableHeaders) {
    // Grid Visual Props
    let aSortableIds = [];
    let aActionItems = [];
    let aActionItemsToShow = [];
    let bCannotAddLinkedInstances = true;
    let bCannotRemoveLinkedInstances = true;
    let bAllColumnsDisabled = false;
    let aPriorityEditableColumns = [];
    let aPriorityDisabledColumns = [];
    let aFixedColumnIds = [];
    let bIsUserReadOnly = ContentUtils.getIsCurrentUserReadOnly();

    switch (sViewContext) {
      case ContentScreenViewContextConstants.PRICE_VERSION_ALL:
        aSortableIds = ["from", "to"];
        oTableOrganiserConfig.showCreateButton = false;
        oTableOrganiserConfig.showColumnOrganiser = false;
        bAllColumnsDisabled = true;
        break;

      case ContentScreenViewContextConstants.PRICE_VERSION_SINGLE:
        aSortableIds = ["versionTimestamp"];
        oTableOrganiserConfig.showDateRangeSelector = false;
        oTableOrganiserConfig.showColumnOrganiser = false;
        bAllColumnsDisabled = true;
        break;

      case ContentScreenViewContextConstants.VARIANT_CONTEXT:
        oTableOrganiserConfig.showColumnOrganiser = false;
        let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
        aSortableIds = CS.map(aSortableHeaders,'id');
        if (!bIsUserReadOnly) {
          aActionItemsToShow = ["delete"];
          aActionItems = [
            {
              id: "delete",
              label: getTranslations().DELETE,
              class: "deleteIcon",
            },
          ];
        }
        if (CS.isEmpty(oOpenedDialogAttributeData.contextId)) {
          aActionItemsToShow.unshift("view");
          aActionItems.unshift({
            id: "view",
            label: getTranslations().VIEW,
            class: "viewIcon",
          })
        }

        bCannotAddLinkedInstances = false;
        bCannotRemoveLinkedInstances = false;
        break;

      case ContentScreenViewContextConstants.IMAGE_VARIANT_CONTEXT:
        oTableOrganiserConfig.showColumnOrganiser = false;
        aPriorityDisabledColumns = CS.difference(oConfigDetails.referencedPermissions.visiblePropertyIds, oConfigDetails.referencedPermissions.editablePropertyIds);
        aFixedColumnIds = ["icon", "nameattribute"];
        let oOpenedDialogData = UOMProps.getOpenedDialogAttributeData();
        if (!bIsUserReadOnly) {
          aActionItemsToShow = ["delete"];
          aActionItems = [
            {
              id: "delete",
              label: getTranslations().DELETE,
              class: "deleteIcon",
            },
          ];
        }
        if (CS.isEmpty(oOpenedDialogData.contextId)) {
          aActionItemsToShow.unshift("view");
          aActionItems.unshift({
            id: "view",
            label: getTranslations().VIEW,
            class: "viewIcon",
          })
        }
        aActionItemsToShow.unshift("downloadImage");
        aActionItems.unshift({
          id: "downloadImage",
          label: getTranslations().DOWNLOAD,
          class: "downloadIcon",
        });

        bCannotAddLinkedInstances = false;
        bCannotRemoveLinkedInstances = false;
        break;
    }

    oGridVisualProps.sortableIds = aSortableIds;
    oGridVisualProps.actionItems = aActionItems;
    oGridVisualProps.actionItemsToShow = aActionItemsToShow;
    oGridVisualProps.allColumnsDisabled = bAllColumnsDisabled;
    oGridVisualProps.priorityEditableColumns = aPriorityEditableColumns;
    oGridVisualProps.priorityDisabledColumns = aPriorityDisabledColumns;
    oGridVisualProps.cannotAddLinkedInstances = bCannotAddLinkedInstances;
    oGridVisualProps.cannotRemoveLinkedInstances = bCannotRemoveLinkedInstances;
    oGridVisualProps.fixedColumnIds = aFixedColumnIds;
  };

  let _processAndSetGridData = function (oUnitTableProps, aHeaderData, aRows, oConfigDetails, oReferencedInstances, aCommonRows, sContextId) {
    let oVisualProps = oUnitTableProps.getVisualProps();
    let oContentGridViewProps = ContentScreenProps.contentGridProps;
    let bShouldSplit = CS.isArray(aCommonRows);
    let sViewContext = oUnitTableProps.getViewContext()
    if (ContentUtils.getSelectedTabId() !== ContentScreenConstants.tabItems.TAB_RENDITION) {
      aHeaderData = CS.sortBy(aHeaderData, (oColumn) => {
        return oColumn.label.toLocaleLowerCase();
      });
    };
    let oGridSkeleton = _getGridSkeletonByColumnType(aHeaderData, oVisualProps, oConfigDetails, bShouldSplit, sContextId);
    _fillGridDataInRows(aRows, oVisualProps, oConfigDetails, oReferencedInstances, aCommonRows, sViewContext, sContextId, aHeaderData);
    //For Representation
    oUnitTableProps.setGridSkeleton(oGridSkeleton);
    //For handling
    oContentGridViewProps.setGridViewSkeleton(oGridSkeleton);
    oUnitTableProps.setBodyData(aRows);
    oContentGridViewProps.setGridViewData(aRows);
  };

  /** Same as _processProperty() but for headers */
  let _processHeader = (oHeader, aSortableHeaders, sContextId, bShouldSplit, oUnitVariantContextProps) => {
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);
    let oReferencedTags = oUnitVariantContextProps.getReferencedTags();
    let oReferencedAttributes = oUnitVariantContextProps.getReferencedAttributes();
    let oReferencedTaxonomies = oUnitVariantContextProps.getReferencedTaxonomies();
    let sIdForReferencedData = oHeader.id;
    let sPreHeaderLabel = "";
    if (bShouldSplit) {
      let aSplit = CS.split(oHeader.id, "__");
      if (aSplit && aSplit.length > 1) {
        sPreHeaderLabel = CS.isNotEmpty(aSplit[0]) ? (`${CS.upperFirst(aSplit[0])} : `) : "";
        CS.isNotEmpty(aSplit[1]) && (sIdForReferencedData = aSplit[1])
      }
    }

    switch (oHeader.type) {

      case "attribute":
        let oReferencedAttribute = oReferencedAttributes[sIdForReferencedData];
        oHeader.value = sPreHeaderLabel + CS.getLabelOrCode(oReferencedAttribute);
        oHeader.baseType = oReferencedAttribute.type;
        oHeader.iconKey = oReferencedAttribute.iconKey;
        oHeader.showDefaultIcon = true;
        break;

      case "tag":
        let oReferencedTag = oReferencedTags[sIdForReferencedData];
        oHeader.value = sPreHeaderLabel + CS.getLabelOrCode(oReferencedTag);
        oHeader.icon = oReferencedTag.icon;
        oHeader.color = oReferencedTag.color;
        oHeader.tagType = oReferencedTag.tagType;
        oHeader.iconKey = oReferencedTag.iconKey;
        oHeader.showDefaultIcon = true;
        oHeader.isColumnDisabled = oReferencedTag.isDisabled;
        break;

      case "taxonomy":
        let oReferencedTaxonomy = oReferencedTaxonomies[sIdForReferencedData];
        oHeader.value = sPreHeaderLabel + CS.getLabelOrCode(oReferencedTaxonomy);
        oHeader.iconKey = oReferencedTaxonomy.iconKey;
        oHeader.showDefaultIcon = true;
        break;

      case "date":
        if (oHeader.id === "from") {
          oHeader.value = getTranslation().FROM;
          aSortableHeaders.push({ //make 'from' sortable (does not come from backend)
            id: "from"
          });
        } else if (oHeader.id === "to") {
          oHeader.value = getTranslation().TO;
          aSortableHeaders.push({ //make 'to' sortable (does not come from backend)
            id: "to"
          });
        }
        break;

      case "name":
        oHeader.value = getTranslation().ATTRIBUTE;
        break;

      case "linkedInstances":
      case "timestamp":
      case "text":
      case "user":
        oHeader.value = sPreHeaderLabel + sIdForReferencedData;
        break;

      case "custom":
        if (oHeader.id === "value") {
          oHeader.value = getTranslation().VALUE;
        } else {
          oHeader.value = oHeader.id;
        }

        // Adding field entityId to get data from reference data
        let sSplitter = CommonUtils.getSplitter();
        oHeader.entityId = (sIdForProps.includes(sSplitter)) ? sIdForProps.split(sSplitter)[1] : null;
        break;

        return true;
    }

    if (CS.find(aSortableHeaders, {id: oHeader.id})) {
      oHeader.isSortable = true;
    }

    oHeader.label = oHeader.value;

    /*let oReferencedElement = oReferencedElements[oHeader.id];
    if (!CS.isEmpty(oReferencedElement)) {
      oHeader.isColumnDisabled = oReferencedElement.couplingType === 'dynamicCoupled' || oReferencedElement.couplingType === 'readOnlyCoupled' || oReferencedElement.isDisabled;
    }*/
  };

  let successFetchUOMData = function (sContextId, oExtraData, oCallBackData, oResponse) {
    _setUpUOMTable(sContextId, oExtraData, oCallBackData, oResponse);
    //We need to reset dirty IDs as dirty execution is done.
    !oCallBackData.doNotTrigger && _triggerChange();
  };

  let _updateSelectedGridColumnsAfterSave = (sIdForProps, sKlassInstanceId) => {
    let oUnitTableProps = TableViewProps.getTableViewPropsByContext(sIdForProps, sKlassInstanceId);
    let oColumnOrganizerPropsByContext = TableViewProps.getColumnOrganizerPropsByContext(sIdForProps,
        sKlassInstanceId);
    let aSelectedOrganizedColumns = oColumnOrganizerPropsByContext.getSelectedOrganizedColumns();
    if (CS.isNotEmpty(aSelectedOrganizedColumns)) {
      let oGridSkeleton = oUnitTableProps.getGridSkeleton();
      let aScrollableColumns = oGridSkeleton.scrollableColumns;
      CS.forEach(aScrollableColumns, (oColumn) => {
        if (!CS.find(aSelectedOrganizedColumns, {code: oColumn.code})) {
          aSelectedOrganizedColumns.push(oColumn);
        }
      });
      CS.remove(aSelectedOrganizedColumns, (oSelectedColumn) => {
        return !CS.find(aScrollableColumns, {code: oSelectedColumn.code});
      });
    }
  };

  let _setUpUOMTable = function (sContextId, oExtraData, oCallBackData, oResponse) {
    let oFailureResponse = oResponse.failure || {};
    oResponse = oResponse.success;
    let bDoNotPreserveColumnSequence = oExtraData.doNotPreserveColumnSequence || false;

    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);
    let sKlassInstanceId = oExtraData.klassInstanceId || _getKlassInstanceId();
    let oUnitTableProps = TableViewProps.getTableViewPropsByContext(sIdForProps, sKlassInstanceId);
    let oUnitTableFilterProps = TableViewProps.getTableFilterViewPropsByContext(sIdForProps, sKlassInstanceId);

    let oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId, sKlassInstanceId);
    _setContextRelatedReferencedData(oResponse, sContextId, oUnitVariantContextProps);
    _setSelectedAndVisibleContextContexts(sContextId, oUnitVariantContextProps);
    _makeVariantContextData(sContextId, oUnitVariantContextProps);

    if (oExtraData.shouldCloseVariantDialog) {
      _closeVariantDialog();
      CommonUtils.removeQuickListBreadCrumbFromPath();
    }

    let oConfigDetails = oResponse.configDetails;
    let oReferencedEmbeddedVariantContexts = oConfigDetails.referencedVariantContexts.embeddedVariantContexts;
    let aHeaderData = oResponse.columns;
    let oReferencedAssets = oResponse.referencedAssets;
    !CS.isEmpty(oReferencedAssets) && ContentScreenProps.screen.setReferencedAssetList(oReferencedAssets);
    let aBodyData = oResponse.rows;
    let aCommonRows = oResponse.commonRows;
    let oFilterInfo = oResponse.filterInfo || {};
    let oSortData = oFilterInfo.sortData || {};
    let aSortableHeaders = oSortData.attributes || [];
    let bShouldEnableOpen = _getShouldEnableOpen(sContextId, oUnitVariantContextProps);
    oUnitTableFilterProps.setFilterInfo(oFilterInfo);

    oUnitTableProps.setOriginalRowData(CS.cloneDeep(oResponse.rows));
    oUnitTableProps.setOriginalHeaderData(CS.cloneDeep(aHeaderData));
    oUnitTableProps.setOriginalCommonRows(CS.cloneDeep(aCommonRows));
    let oReferencedVariantContexts = oUnitVariantContextProps.getReferencedVariantContexts();
    let oReferencedContext = oReferencedVariantContexts[sContextId] || {};

    let sViewContextToSet = CS.isNotEmpty(oExtraData) && CS.isNotEmpty(oExtraData.viewContext) ? oExtraData.viewContext : ContentScreenViewContextConstants.VARIANT_CONTEXT;
    oUnitTableProps.setViewContext(sViewContextToSet);
    oUnitTableProps.setViewMode(ContentScreenConstants.UOM_BODY_GRID_MODE);

    let bShouldSplit = CS.isArray(aCommonRows);
    //------------------------------------- Header Data Processing : ---------------------------------------------------
    CS.remove(aHeaderData, function (oHeader) {
      return _processHeader(oHeader, aSortableHeaders, sContextId, bShouldSplit, oUnitVariantContextProps);
    });

    //set header data for column organiser initially, once on the first get call.
    if (CS.isEmpty(oExtraData.columnIds)) {
      let aOldColumnHeaderData = oUnitTableProps.getColumnOrganiserHeaderData();
      let aNewColumnHeaderData = !bDoNotPreserveColumnSequence ? CommonUtils.preserveOrderedDataInArray(aOldColumnHeaderData, aHeaderData, 'id') : aHeaderData;
      oUnitTableProps.setColumnOrganiserHeaderData(aNewColumnHeaderData);
    }

    let aColumnOrganiserHeaderData = oUnitTableProps.getColumnOrganiserHeaderData();
    let aSelectedHeaders = oUnitTableProps.getSelectedHeaders();

    if (CS.isEmpty(aSelectedHeaders)) {
      oUnitTableProps.setSelectedHeaders(CS.map(aColumnOrganiserHeaderData, 'id'));
    }
    /**
     * Multi tab scenario Handled
     * selected column ids (aSelectedHeaders) length should not be greater than updated header data (aHeaderData).
     * MultiTab scenario: aSelectedHeaders length is might be greater than aHeaderData in case tag/entities is removed
     * from context config in another tab.
     * Need to remove selected column ids from updated header data.
     */
    else if (aSelectedHeaders.length > aHeaderData.length) {
      let aColumnHeaderData = oUnitTableProps.getColumnOrganiserHeaderData();
      CS.remove(aSelectedHeaders, function (sColumnId) {
        if (!CS.find(aHeaderData, {id: sColumnId})) {
          CS.remove(aColumnHeaderData, {id: sColumnId});
          return true;
        }
      });
    }

    let aOldHeaderData = oUnitTableProps.getHeaderData();
    let aNewHeaderData = !bDoNotPreserveColumnSequence ? CommonUtils.preserveOrderedDataInArray(aOldHeaderData, aHeaderData, 'id') : aHeaderData;
    if (ContentUtils.getSelectedTabId() === ContentScreenConstants.tabItems.TAB_RENDITION && CS.isNotEmpty(aBodyData)) {
      aNewHeaderData.unshift({
        id: "icon",
        value: getTranslation().PREVIEW,
        isSortable: true,
        label: getTranslation().PREVIEW,
        isColumnDisabled: false,
        type: oGridViewPropertyTypes.IMAGE,
        width: 30
      });
    }

    oUnitTableProps.setHeaderData(aNewHeaderData);

    let oReferencedAttributes = oConfigDetails.referencedAttributes;
    /** Modify filter info based on referenced Attribute **/
    CS.forEach(oFilterInfo.filterData, function (oFilterData) {
      let oReferencedAttribute = oReferencedAttributes[oFilterData.id];
      if (CS.isNotEmpty(oReferencedAttribute)) {
        CS.forEach(oFilterData.children, function (oChild) {
          oChild.hideSeparator = oReferencedAttribute.hideSeparator;
        })
      }
    });


    CS.forEach(oFilterInfo.filterData, function (oProperty) {
      let oFilterProperty = CS.find(oConfigDetails.referencedAttributes, {id: oProperty.id}) ||
                            CS.find(oConfigDetails.referencedTags, {id: oProperty.id});
      oProperty.iconKey = oFilterProperty.iconKey;
      oFilterProperty.children && CS.forEach(oProperty.children, function (oChild) {
        let oChildren = CS.find(oFilterProperty.children, {id: oChild.id});
        oChild.iconKey = oChildren.iconKey;
      });
    });

    oUnitTableFilterProps.setAvailableFilters(oFilterInfo.filterData);

    let bShowEditButton = false, bShowDeleteButton = true;
    //let oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);
    let oTableOrganiserConfig = oUnitTableProps.getTableOrganiserConfig();
    if (oReferencedContext.type === ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
      //don't show create, edit, delete button in normal attribute context section
      oTableOrganiserConfig.showCreateButton = bShowEditButton = bShowDeleteButton = !!oExtraData.attributeId;
    }
    oTableOrganiserConfig.showDateRangeSelector = oReferencedContext.isTimeEnabled;  //show range selector if context is time enabled
    oTableOrganiserConfig.showColumnOrganiser = false;


    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    let sAttributeId = oOpenedDialogAttributeData.attributeId;
    let bIsContentEditable = !ContentUtils.getIsContentDisabled();

    if (!CS.isEmpty(sAttributeId)) {  // if attribute variant dialog is open
      let oReferencedElements = oUnitVariantContextProps.getReferencedElements();
      let oReferencedElement = oReferencedElements[sAttributeId];
      if (!CS.isEmpty(oReferencedElement)) {
        oTableOrganiserConfig.showCreateButton = bShowEditButton = bShowDeleteButton = !oReferencedElement.isDisabled;
      }

    } else if (oReferencedContext.type !== ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
      let oReferencedPermissions = oUnitVariantContextProps.getReferencedPermissions() || {};
      let oGlobalPermission = oReferencedPermissions.globalPermission || {};
      oTableOrganiserConfig.showCreateButton = oGlobalPermission.canCreate;
      bShowDeleteButton = bIsContentEditable && oGlobalPermission.canDelete;
    }

    if (oReferencedContext.type === ContextTypeDictionary.IMAGE_VARIANT) {
      let oGlobalPermissions = oUnitVariantContextProps.getReferencedPermissions().globalPermission;
      bShowDeleteButton = oGlobalPermissions.canDelete;
      oTableOrganiserConfig.showCreateButton = oGlobalPermissions.canCreate && !oReferencedContext.isAutoCreate;
      _updateImageVariantEditableProperties(sContextId, sKlassInstanceId, oReferencedContext.isAutoCreate);
    }

    let oSettings = {
      isCellEditable: oReferencedContext.isLimitedObject,
      showDeleteButton: bShowDeleteButton && bIsContentEditable,
      showEditButton: bShowEditButton && bIsContentEditable,
      showOpenButton: bShouldEnableOpen,
      showPagination: true,
      context: oExtraData.viewContext || ContentScreenViewContextConstants.VARIANT_CONTEXT
    };

    // Don't show create button when...
    oTableOrganiserConfig.showCreateButton =
        // no property collection is added in context & no tags are available in contextual tags - Neha
        (CS.isNotEmpty(oConfigDetails.referencedPropertyCollections) || CS.isNotEmpty(aNewHeaderData))
        // user actually don't have permission to create && Content is not Available In Selected DataLanguage
        && oTableOrganiserConfig.showCreateButton
        && bIsContentEditable;


    oUnitTableProps.setSettings(oSettings);
    let oPaginationData = oUnitTableFilterProps.getPaginationData();
    oPaginationData.from = oResponse.from;
    oPaginationData.totalItems = oResponse.totalContents;

    oTableOrganiserConfig.isTableContentDirty && (oTableOrganiserConfig.isTableContentDirty = false);
    if (oExtraData.isBulkSaveTableDataSuccess) {
      if (!CS.isEmpty(oFailureResponse.exceptionDetails)) {
        failureFetchUOMData(sContextId, {failure: oFailureResponse});
      }
      if (oExtraData.totalInstancesToSave > CS.size(CS.uniqBy(oFailureResponse.exceptionDetails, "itemId"))) {
        alertify.success(getTranslation().REFRESH_AFTER_SAVE);
      }
      let aDirtyTableIds = ContentScreenProps.screen.getDirtyTableContextIds();
      CS.remove(aDirtyTableIds, function (sId) {
        return sId === sContextId
      });
    }
    oUnitTableProps.setIsSectionLoading(false);
    oUnitVariantContextProps.setDummyVariant({});
    //--------------------------------------- Post Processing -----------------------------------------------------
    //TODO #Refactoring : Make all oTableOrganiserConfig properties modifications wrt to Context Refer
    let oGridVisualProps = {};
    let sViewContext = oUnitTableProps.getViewContext();
    let sViewMode = oUnitTableProps.getViewMode();
    _generateUOMVisualPropsByViewContext(sViewContext, oConfigDetails, oTableOrganiserConfig, oGridVisualProps, oSettings, aSortableHeaders, aNewHeaderData);
    oUnitTableProps.setVisualProps(oGridVisualProps);
    if (sViewMode === ContentScreenConstants.UOM_BODY_GRID_MODE) {
      //pre Processing Common Properties
      _processAndSetGridData(oUnitTableProps, aNewHeaderData, aBodyData, oConfigDetails, oUnitVariantContextProps.getReferencedInstances(), aCommonRows, sContextId);

      if (oExtraData.isBulkSave) {
        _updateSelectedGridColumnsAfterSave(sIdForProps, sKlassInstanceId);
      }
    }
    //---------------------------------------   -----------------------------------------------------
    if (oCallBackData && oCallBackData.functionToExecute) {
      oCallBackData.functionToExecute();
    }

  };

  let _updateImageVariantEditableProperties = function (sContextId, sKlassInstanceId, bIsAutoCreate) {
    let oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId, sKlassInstanceId);
    let aEditablePropertyIds = oUnitVariantContextProps.getReferencedPermissions().editablePropertyIds;
    let aMetaDataAttributeIds = [];
    CS.map(oUnitVariantContextProps.getReferencedAttributes(), function (oAttribute) {
      oAttribute.type === AttributeTypeDictionary.ASSET_META_DATA && aMetaDataAttributeIds.push(oAttribute.id);
    });
    aEditablePropertyIds = CS.difference(aEditablePropertyIds, aMetaDataAttributeIds);
    if (bIsAutoCreate) {
      aEditablePropertyIds = CS.difference(aEditablePropertyIds, ["imageextensiontag", "resolutiontag"]);
    }
    oUnitVariantContextProps.getReferencedPermissions().editablePropertyIds = aEditablePropertyIds;
  };

  let failureFetchUOMData = function (sContextId, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      let aExceptionDetails = oResponse.failure.exceptionDetails;
      let oDuplicateVariantExists = CS.find(aExceptionDetails, {key: "DuplicateVariantExistsException"});
      if (!CS.isEmpty(oDuplicateVariantExists)) {
        _removeAttributeVariantInstanceFromContent();
      }
    }
    ContentUtils.failureCallback(oResponse, "failureFetchUOMData", getTranslation())
    return false;
  };

  let _setContextRelatedReferencedData = function (oResponse, sContextId, oUnitVariantContextProps) {
    if (CS.isEmpty(oUnitVariantContextProps)) {
      oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);
    }
    let oConfigDetails = oResponse.configDetails;

    /** Set other data from config details in here IF NECESSARY*/

    let oReferencedContextsToSet = oConfigDetails.referencedVariantContexts.embeddedVariantContexts ? oConfigDetails.referencedVariantContexts.embeddedVariantContexts : oConfigDetails.referencedVariantContexts;
    oUnitVariantContextProps.setReferencedVariantContexts(oReferencedContextsToSet);
    oUnitVariantContextProps.setReferencedTags(oConfigDetails.referencedTags);
    oUnitVariantContextProps.setReferencedAttributes(oConfigDetails.referencedAttributes);
    oUnitVariantContextProps.setReferencedPropertyCollections(oConfigDetails.referencedPropertyCollections);
    oUnitVariantContextProps.setReferencedPermissions(oConfigDetails.referencedPermissions);
    oUnitVariantContextProps.setReferencedInstances(oResponse.referencedInstances);

    oUnitVariantContextProps.setReferencedTemplate(oConfigDetails.referencedTemplate);
    oUnitVariantContextProps.setReferencedTaxonomies(oConfigDetails.referencedTaxonomies);
    let oReferencedTaxonomy = ContentScreenProps.screen.getReferencedTaxonomies();
    CS.forEach(oConfigDetails.referencedTaxonomies, function (oTaxonomy) {
      oTaxonomy.rootNodeInfo = _addRootNodeIdToReferencedTaxonomies(oTaxonomy);
    });
    if (CS.isNotEmpty(oReferencedTaxonomy)) {
        oReferencedTaxonomy = {...oReferencedTaxonomy, ...oConfigDetails.referencedTaxonomies};
    } else {
        oReferencedTaxonomy = oConfigDetails.referencedTaxonomies;
    }
    ContentScreenProps.screen.setReferencedTaxonomies(oReferencedTaxonomy);

    let oReferencedVariantContext = ContentScreenProps.screen.getReferencedVariantContexts();
    CS.assign(oReferencedVariantContext.embeddedVariantContexts, oUnitVariantContextProps.getReferencedVariantContexts());

    var oReferencedTags = ContentScreenProps.screen.getReferencedTags();
    CS.assign(oReferencedTags, oConfigDetails.referencedTags);

    var oReferencedAttributes = ContentScreenProps.screen.getReferencedAttributes();
    CS.assign(oReferencedAttributes, oConfigDetails.referencedAttributes);

     if (oConfigDetails.hasOwnProperty("instanceIdVsReferencedElements") &&
         CS.isNotEmpty(oConfigDetails.instanceIdVsReferencedElements)) {
      oUnitVariantContextProps.setInstanceIdVsReferencedElements(oConfigDetails.instanceIdVsReferencedElements);
    }
    else if (oConfigDetails.hasOwnProperty("referencedElements") &&
         CS.isNotEmpty(oConfigDetails.referencedElements)) {
      //Need to set referencedElements if context type is attribute context
      oUnitVariantContextProps.setReferencedElements(oConfigDetails.referencedElements);
    }
  };

  let _makeVariantContextData = function (sContextId, oUnitVariantContextProps) {
    if (CS.isEmpty(oUnitVariantContextProps)) {
      oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);
    }

    var oReferencedTags = oUnitVariantContextProps.getReferencedTags();
    var oReferencedContexts = oUnitVariantContextProps.getReferencedVariantContexts();
    CS.forEach(oReferencedContexts, function (oVariantContext) {
      var aContextTags = [];
      CS.forEach(oVariantContext.tags, function (oContextTag) {
        var oMasterTag = oReferencedTags[oContextTag.tagId] || oReferencedTags[oContextTag.id]; //color, icon, label, id, tagType, children
        if (oMasterTag) {
          var aChildren = [];
          var oTagGroup = {};
          oTagGroup.id = oMasterTag.id;
          oTagGroup.icon = oMasterTag.icon;
          oTagGroup.label = oMasterTag.label;
          oTagGroup.color = oMasterTag.color;
          oTagGroup.tagType = oMasterTag.tagType;
          oTagGroup.code = oMasterTag.code;
          oTagGroup.children = aChildren;

          CS.forEach(oContextTag.tagValueIds, function (sTagId) {
            var oTagChildren = CS.find(oMasterTag.children, {id: sTagId});
            if (oTagChildren) {
              aChildren.push(oTagChildren);
            }
          });
        }
        aContextTags.push(oTagGroup);
      });

      oVariantContext.tags = aContextTags;
    });

  };

  /**
   * @deprecated
   * @param sVariantId
   * @param oCallback
   * @private
   */
  let _fetchUOMVariantById = function (sVariantId, oCallback) {
    let oActiveEntity = ContentUtils.getActiveEntity();
    let sURL = ContentUtils.getEntityByIdUrl(oActiveEntity.baseType);
    let fSuccess = successHandleVariantEditButton.bind(this, oCallback);
    let fFailure = failureHandleVariantEditButton;

    let oData = {
      id: sVariantId,
      tab: ""/*ContentUtils.getTabUrlFromTabId(ContentScreenConstants.tabItems.TAB_GENERAL)*/,
      isLoadMore: false,
      getAll: true
    };

    let sSortField = ContentScreenProps.screen.getActiveSortingField();
    let sSortOrder = ContentScreenProps.screen.getActiveSortingOrder();

    let oPostData = {
      attributes: [],
      tags: [],
      allSearch: "",
      size: 20,
      from: 0,
      sortField: sSortField,
      sortOrder: sSortOrder,
      getFolders: true,
      getLeaves: true,
      isAttribute: false,
      isNumeric: false,
      selectedRoles: [],
      selectedTypes: [],
      isRed: true,
      isOrange: true,
      isYellow: true,
      isGreen: true,
      childContextId: null,
    };

    CS.postRequest(sURL, oData, oPostData, fSuccess, fFailure);
  };

  let successHandleVariantEditButton = function (oCallback, oResponse) {
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute(oCallback, oResponse);
    }
  };

  let failureHandleVariantEditButton = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureHandleVariantEditButton", getTranslation())
  };

  let _addPropertyCollectionsInContext = function (sContextId) {
    let oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);
    let oReferencedContexts = oUnitVariantContextProps.getReferencedVariantContexts();
    if (oReferencedContexts) {
      let oContext = oReferencedContexts[sContextId];
      let oReferencedPropertyCollections = oUnitVariantContextProps.getReferencedPropertyCollections();
      CS.forEach(oReferencedPropertyCollections, function (oPC) {
        oContext.propertyCollections.push({id: oPC.id});
      });
    }
  };

  let _handleUOMVariantPropsForDialog = function (oExtraData, oResponse) {
    let sTableContextId = oExtraData.tableContextId;
    let sIdForProps = ContentUtils.getIdForTableViewProps(sTableContextId);

    UOMProps.setOpenedDialogTableContextId(sTableContextId);
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sIdForProps);

    if (!oExtraData.isCreate) {
      let oResponseData = oResponse.success;

      _setContextRelatedReferencedData(oResponseData, sTableContextId);
      _addPropertyCollectionsInContext(sTableContextId);
      _setSelectedAndVisibleContextContexts(sTableContextId);
      _makeVariantContextData(sTableContextId);

      var oVariant = oResponseData.klassInstance;
      if (oVariant.context) {
        oVariant.timeRange = oVariant.context.timeRange;

        let oReferencedInstances = oVariantContextByContextProps.getReferencedInstances();
        ContentUtils.processLinkedInstancesForContentContext(oVariant, oReferencedInstances);

      }
      oVariantContextByContextProps.setActiveVariantForEditing(oVariant);
    }

    let sOpenDialogContext = (oExtraData.isCreate ? "create" : "edit") + ContentUtils.getSplitter() + ContentScreenViewContextConstants.VARIANT_CONTEXT;
    oVariantContextByContextProps.setVariantDialogOpenContext(sOpenDialogContext);
    oVariantContextByContextProps.setIsVariantDialogOpen(true);
    _triggerChange();
  };

  let _closeVariantDialog = function () {
    let sTableContext = UOMProps.getOpenedDialogTableContextId();
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sTableContext);

    if (CS.isEmpty(oVariantContextByContextProps)) {
      ContentScreenProps.availableEntityViewProps.setAvailableEntityViewContext("");
      return;
    }
    let oVariantSectionViewProps = ContentScreenProps.variantSectionViewProps;
    oVariantSectionViewProps.setAvailableEntityChildVariantViewVisibilityStatus(false);
    oVariantSectionViewProps.setAvailableEntityParentVariantViewVisibilityStatus(false);
    oVariantContextByContextProps.setIsVariantDialogOpen(false);
    oVariantContextByContextProps.setVariantDialogOpenContext("");
    oVariantContextByContextProps.setActiveVariantForEditing({});
    oVariantContextByContextProps.setDummyVariant({});
    oVariantContextByContextProps.setEditVariantTags(false);

    UOMProps.setOpenedDialogTableContextId("");
  };

  let _handleUOMViewSortDataChanged = function (sContextId, oSortData) {
    _handleCustomUOMViewSortDataChanged(sContextId, oSortData);
    let oCallBackData = {
      filterContext: _createFilterContext(sContextId)
    }
    _fetchUOMData(sContextId, "", false, false, oCallBackData);
  };

  let _handleUOMViewPaginationDataChanged = function (sContextId, oNewPaginationData, oFilterContext) {
    _handleCustomUOMViewPaginationDataChanged(sContextId, oNewPaginationData, oFilterContext);
    _fetchUOMData(sContextId, "", false, false, {filterContext: oFilterContext}, _getKlassInstanceId());
  };

  let _resetAndFetchUOMData = function (sContextId) {
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);
    //Resetting Props
    let sActiveContentId = _getKlassInstanceId();
    let oUnitTableProps = _getTableViewPropsByContext(sContextId);
    let sVariantInstanceId = oUnitTableProps && oUnitTableProps.getVariantInstanceId();

    TableViewProps.resetPropsByContext(sIdForProps, sActiveContentId);
    TableViewProps.createNewTableViewPropsByContext(sIdForProps, sActiveContentId);

    if (sVariantInstanceId) {
      oUnitTableProps = TableViewProps.getTableViewPropsByContext(sIdForProps, sActiveContentId);
      oUnitTableProps.setVariantInstanceId(sVariantInstanceId);
    }
    _fetchUOMData(sContextId, "", false, false, {}, sActiveContentId);
  };

  let _handleUOMViewModeMenuApply = function (sContextId, sSelectedItem) {
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    let oViewModeData = oOpenedDialogAttributeData.viewModeData;
    if (oViewModeData) {
      let oSelectedItem = CS.find(oViewModeData.items, {id: sSelectedItem});
      oViewModeData.value = sSelectedItem;
      oViewModeData.valueLabel = oSelectedItem.label;
      oOpenedDialogAttributeData.gridComponentKey = ContentUtils.generateUUID();
      _resetAndFetchUOMData(sContextId);
    }
  };

  var _handleVariantDialogChangeAttributeValue = function (oData) {
    let oActiveEntity = ContentUtils.makeActiveContentOrVariantDirty();
    if (!CS.isEmpty(oData.valueAsHtml)) {
      oActiveEntity.valueAsHtml = oData.value;
      oActiveEntity.value = oData.valueAsHtml;
    } else {
      oActiveEntity.value = oData.value;
    }
  };

  let _getCreateVariantData = function (sContextId) {
    var oActiveEntity = ContentUtils.getActiveEntity();
    var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);

    var oVariantSectionProps = _getVariantContextPropsByContext(sContextId);
    var oReferencedVariantContexts = oVariantSectionProps.getReferencedVariantContexts();
    let oReferencedContext = oReferencedVariantContexts[sContextId];

    let sUrl = getRequestMapping(sScreenMode).GetVariantConfigData;
    let fSuccessCallback = successGetCreateVariantData.bind(this, sContextId);
    let fFailureCallback = failureGetCreateVariantData;
    let oData = {
      id: oReferencedContext.contextKlassId
    };

    CS.getRequest(sUrl, oData, fSuccessCallback, fFailureCallback);
  };

  let successGetCreateVariantData = function (sContextId, _oResponse) {

    let oResponse = {
      configDetails: _oResponse.success
    };
    _setContextRelatedReferencedData(oResponse, sContextId);
    _setSelectedAndVisibleContextContexts(sContextId);
    _makeVariantContextData(sContextId);
    _prepareCreateVariantData(sContextId);
  };

  let failureGetCreateVariantData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureGetCreateVariantData", getTranslation());
  };

  let _createUOMVariant = function (sContextId) {
    var oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    var oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    var oReferencedContext = oEmbeddedVariantContexts[sContextId] || {};
    let oActiveContent = ContentUtils.getActiveContent();
    let sKlassInstanceId = oActiveContent.id;
    if (oReferencedContext.type == ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
      let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
      sKlassInstanceId = oOpenedDialogAttributeData.klassInstanceId || ContentUtils.getActiveContent().id;
    }
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId, sKlassInstanceId);

    let oUnitTableFilterProps = TableViewProps.getTableFilterViewPropsByContext(sIdForProps, sKlassInstanceId);
    var aAppliedFilters = oUnitTableFilterProps.getAppliedFilters();
    var oTimeRangeData = oUnitTableFilterProps.getTimeRangeData();
    var oSortData = oUnitTableFilterProps.getSortData();
    var oPaginationData = oUnitTableFilterProps.getPaginationData();
    var bIsDateRangeFilterApplied = oTimeRangeData && oTimeRangeData.from && oTimeRangeData.to;
    var bIsPaginationApplied = (oPaginationData.from || oPaginationData.pageSize !== 20);
    var bIsSortingApplied = oSortData.hasOwnProperty("sortBy");
    var bIsAnyFilterApplied = bIsDateRangeFilterApplied || !CS.isEmpty(aAppliedFilters) || bIsSortingApplied || bIsPaginationApplied;

    if (_getIsEditFromTableEnable(oReferencedContext) && bIsAnyFilterApplied) {
      CustomActionDialogStore.showConfirmDialog(getTranslation().RESET_FILTER_CONFIRMATION, "",
          function () {
            _handleCreateUOMVariantClicked(sContextId);
          }, function (oEvent) {
          });
    } else {
      _handleCreateUOMVariantClicked(sContextId);
    }
  };

  //TODO: Need to refactor below code. Resolve if-else in a better way
  let _handleCreateUOMVariantClicked = function (sContextId) {
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    var oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    var oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    var oReferencedContext = oEmbeddedVariantContexts[sContextId] || {};

    if (oReferencedContext.type === ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
      if (oOpenedDialogAttributeData.contextId === sContextId) {
        _prepareCreateVariantData(sContextId);
      } else {
        _getCreateVariantData(sContextId);
      }
    } else {
      if (_getIsEditFromTableEnable(oReferencedContext)) {
        _prepareCreateVariantData(sContextId);
      } else {
        _handleVariantDialogSaveClicked(sContextId, "create");
      }
    }
  };

  let _handleUOMTableDiscardButtonClicked = function (sContextId) {
    let oUnitTableProps = _getTableViewPropsByContext(sContextId);
    let aOriginalBodyData = CS.cloneDeep(oUnitTableProps.getOriginalRowData());
    let oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);

    if (oUnitTableProps.getViewMode() === ContentScreenConstants.UOM_BODY_GRID_MODE) {
      let oConfigDetails = {
        referencedAttributes: oUnitVariantContextProps.getReferencedAttributes(),
        referencedTaxonomies: oUnitVariantContextProps.getReferencedTaxonomies(),
        referencedTags: oUnitVariantContextProps.getReferencedTags(),
        referencedVariantContexts: {
          embeddedVariantContexts: oUnitVariantContextProps.getReferencedVariantContexts()
        }
      };
      let oReferencedVariantContexts = oUnitVariantContextProps.getReferencedVariantContexts();
      let oReferencedContext = oReferencedVariantContexts[sContextId] || {};
      if (oReferencedContext.type === ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
        oConfigDetails.referencedElements = oUnitVariantContextProps.getReferencedElements();
      }
      else {
        oConfigDetails.instanceIdVsReferencedElements = oUnitVariantContextProps.getInstanceIdVsReferencedElements();
      }
      let aCommonRows = CS.cloneDeep(oUnitTableProps.getOriginalCommonRows());
      _fillGridDataInRows(aCommonRows, oUnitTableProps.getVisualProps(), oConfigDetails, oUnitVariantContextProps.getReferencedInstances(), [], "", sContextId);
      _processAndSetGridData(oUnitTableProps, oUnitTableProps.getHeaderData(), aOriginalBodyData, oConfigDetails, oUnitVariantContextProps.getReferencedInstances(), aCommonRows, sContextId);
    }
    let oTableOrganiserConfig = oUnitTableProps.getTableOrganiserConfig();
    oTableOrganiserConfig.isTableContentDirty = false;
    _triggerChange();
  };

  let _getClassContextById = function (sContextId) {
    let oCallbackData = {
      context: sContextId
    };
    return CS.getRequest(getRequestMapping().GetContext, {id: sContextId}, _successGetClassContextById.bind(this, oCallbackData), _failureGetClassContextById);
  };

  let _successGetClassContextById = function (oCallbackData, oResponse) {
    let oContextFromServer = oResponse.success;
    let sContextId = oCallbackData.context;
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sContextId);
    let oConfigDetails = oContextFromServer.configDetails;
    let oVariantContextReferencedTags = oVariantContextByContextProps.getReferencedTags();
    CS.assign(oVariantContextReferencedTags, oConfigDetails.referencedTags);

    let oReferencedTags = ContentScreenProps.screen.getReferencedTags();
    CS.assign(oReferencedTags, oConfigDetails.referencedTags);

    let oSelectedVisibleContext = oVariantContextByContextProps.getSelectedVisibleContext();
    oSelectedVisibleContext.referencedTags = oConfigDetails.referencedTags;
    oSelectedVisibleContext.tags = oContextFromServer.contextTags;
    oSelectedVisibleContext.entities = oContextFromServer.entities;
    _makeVariantContextData(sContextId);
  };

  let _failureGetClassContextById = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureGetClassContextById", getTranslation());
  };

  let _getIsEditFromTableEnable = function (oSelectedVisibleContext) {
    return oSelectedVisibleContext.type === ContextTypeDictionary.GTIN_CONTEXT ||
        oSelectedVisibleContext.type === ContextTypeDictionary.CONTEXTUAL_VARIANT  ||
        oSelectedVisibleContext.type === ContextTypeDictionary.IMAGE_VARIANT;;
  };

  let _getKlassInstanceId = function () {
    let oActiveContent = ContentUtils.getActiveContent();
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    return oOpenedDialogAttributeData.klassInstanceId || oActiveContent.id;
  };

  let _prepareCreateVariantData = async function (sContextId) {
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sContextId);

    var bDoTrigger = true;
    if (CS.isEmpty(oVariantContextByContextProps.getDummyVariant())) {
      var oDummyVariant = _createUOMDummyVariant(oVariantContextByContextProps, sContextId);
      await _getClassContextById(sContextId);
      var oSelectedVisibleContext = oVariantContextByContextProps.getSelectedVisibleContext();
      if (oSelectedVisibleContext.defaultTimeRange) {
        var oDefaultTimeRange = oSelectedVisibleContext.defaultTimeRange;
        var oDummyVariantTimeRange = oDummyVariant.timeRange;
        if (oDefaultTimeRange.isCurrentTime) {
          var oDate = new Date();
          oDate.setHours(0, 0, 0, 0); //Ignore time consider only date.
          oDummyVariantTimeRange.from = oDate.getTime();
          oDummyVariantTimeRange.to = oDefaultTimeRange.to;
        } else {
          oDummyVariantTimeRange.from = oDefaultTimeRange.from;
          oDummyVariantTimeRange.to = oDefaultTimeRange.to;
        }
      }

      let bIsLimitedObject = _getIsEditFromTableEnable(oSelectedVisibleContext);
      if (oSelectedVisibleContext.isTimeEnabled || !CS.isEmpty(oSelectedVisibleContext.tags)) {
        var bWithoutSuffix = true;
        CS.forEach(oSelectedVisibleContext.tags, function (oMasterTag) {
          ContentUtils.addAllMasterTagInEntity(oDummyVariant, oMasterTag, bWithoutSuffix);
        });

        var aSelectedContextSections = !bIsLimitedObject ? oVariantContextByContextProps.getSelectedContextVariantSections() : [];
        CS.forEach(aSelectedContextSections, function (oPropertyCollection) {
          var aElements = oPropertyCollection.elements;
          CS.forEach(aElements, function (oElement) {
            if (oElement.type === "attribute") {
              ContentUtils.addAllMasterAttributeInEntity(oDummyVariant, oElement.attribute, bWithoutSuffix);
            } else if (oElement.type === "tag") {
              ContentUtils.addAllMasterTagInEntity(oDummyVariant, oElement.tag, bWithoutSuffix);
            }
          });
        });
      }

      /** User should not be able to create a variant which does not have any property collections, variant tags & time range
       * EXCEPT attributeContext
       */
      if (oSelectedVisibleContext.type !== ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT &&
          !oSelectedVisibleContext.isTimeEnabled &&
          CS.isEmpty(oSelectedVisibleContext.tags) &&
          (CS.isEmpty(aSelectedContextSections) && !bIsLimitedObject)) {
        alertify.message(getTranslation().CANNOT_CREATE_VARIANTS_WITHOUT_PROPERTIES);
        return;
      }

      oVariantContextByContextProps.setDummyVariant(oDummyVariant);

      //TODO: find a better solution to the below if(s)
      if (bIsLimitedObject && CS.isEmpty(oSelectedVisibleContext.tags) && CS.isEmpty(oSelectedVisibleContext.entities) && !oSelectedVisibleContext.isTimeEnabled) {
        let oFilterContext = _createFilterContext(sContextId);
        let oCallback = {
          filterContext: oFilterContext
        };
        _createNewUOMVariant(sContextId, oCallback);
        return;
      }

      var sOpenedDialogVariatingAttributeId = UOMProps.getOpenedDialogAttributeData().attributeId;

      var oExtraData = {
        tableContextId: sContextId,
        isCreate: true,
        tableAttributeId: sOpenedDialogVariatingAttributeId
      };
      _handleUOMVariantPropsForDialog(oExtraData);

      /** If there is nothing to show on dialog and if its not Attribute related then directly call create function*/
      if (CS.isEmpty(oSelectedVisibleContext.tags) &&
          !oSelectedVisibleContext.isTimeEnabled &&
          (!bIsLimitedObject) &&
          (CS.isEmpty(sOpenedDialogVariatingAttributeId) && !bIsLimitedObject) &&
          (!oDummyVariant.isDirty || !oDummyVariant.contentClone)) {
        let oFilterContext = _createFilterContext(sContextId);
        let oCallback = {
          filterContext: oFilterContext
        }
        _createNewUOMVariant(sContextId, oCallback);
        bDoTrigger = false;
      }
    }
    else {
      ContentUtils.showError(getTranslation().VARIANT_CREATION_IN_PROGRESS);
    }

    if (bDoTrigger) {
      _triggerChange();
    }
  };

  let _createUOMDummyVariant = function (oVariantContextByContextProps, sContextId) {
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    let sSelectedContextId = sContextId || oOpenedDialogAttributeData.contextId;
    let oUnitVariantContextProps = _getVariantContextPropsByContext(sSelectedContextId);

    let sHtmlValue = null;
    let sValue = null;
    let sAttributeId = oOpenedDialogAttributeData.attributeId;
    if (!CS.isEmpty(sAttributeId)) { //if Dialog is for Attribute variant
      let oReferenceElements = oUnitVariantContextProps.getReferencedElements();
      let oElement = oReferenceElements[sAttributeId];
      let oMasterAttribute = ContentUtils.getMasterAttributeById(oElement.id);
      sValue = oElement.defaultValue;
      if (oMasterAttribute && ContentUtils.isAttributeTypeHtml(oMasterAttribute.type)) {
        sHtmlValue = oElement.defaultValue;
        sValue = ContentUtils.getDecodedHtmlContent(sHtmlValue);
      }
    }

    let oVariantsCount = ContentScreenProps.screen.getVariantsCount();
    let iNumberOfVariantsOrClones = 1;
    let oSelectedVisibleContext = oVariantContextByContextProps.getSelectedVisibleContext();
    if (oVariantsCount[oSelectedVisibleContext.id]) {
      iNumberOfVariantsOrClones = (+oVariantsCount[oSelectedVisibleContext.id] + 1);
    }

    let oActiveEntity = ContentUtils.getActiveEntity();
    let sEntityName = ContentUtils.getContentName(oActiveEntity);
    let sDummyVariantName = sEntityName + ('-' + getTranslation().VARIANT + '-' + iNumberOfVariantsOrClones);
    let sIdToSet = !CS.isEmpty(oActiveEntity.klassInstanceId) ? oActiveEntity.klassInstanceId : oActiveEntity.id;

    return {
      id: sIdToSet,
      contextId: oSelectedVisibleContext.id,
      name: sDummyVariantName,
      tags: [],
      contextTags: [],
      attributes: [],
      value: sValue,
      valueAsHtml: sHtmlValue,
      baseType: oActiveEntity.baseType,
      timeRange: {
        from: null,
        to: null
      }
    }
  };

  let _closeDialogAndFetchData = function () {
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    _closeVariantDialog();
    _fetchUOMData(oOpenedDialogAttributeData.contextId, oOpenedDialogAttributeData.attributeId, {}, {}, {},
        oOpenedDialogAttributeData.klassInstanceId);
  };

  let _getCreateVariantRequestData = function (sContextId) {
    let oDataToPost = {};

    oDataToPost.templateId = ContentUtils.getTemplateIdForServer(null);

    let oVariantContextByContextProps = _getVariantContextPropsByContext(sContextId);
    let oSelectedVisibleContext = oVariantContextByContextProps.getSelectedVisibleContext();
    oDataToPost.types = [oSelectedVisibleContext.contextKlassId];
    oDataToPost.contextId = oSelectedVisibleContext.id;

    let oActiveEntity = ContentUtils.getActiveEntity();
    oDataToPost.id = !CS.isEmpty(oActiveEntity.klassInstanceId) ? oActiveEntity.klassInstanceId : oActiveEntity.id;
    oDataToPost.baseType = oActiveEntity.baseType;
    oDataToPost.parentId = oActiveEntity.id;  //parentId check

    let oDummyVariant = oVariantContextByContextProps.getDummyVariant();
    if (!CS.isEmpty(oDummyVariant)) {
      oDummyVariant = oDummyVariant.contentClone || oDummyVariant;
      oDataToPost.timeRange = oDummyVariant.timeRange;
      oDataToPost.contextTags = oDummyVariant.tags;
      oDataToPost.linkedInstances = _getLinkedInstances(oDummyVariant.linkedInstances);
    }

    return oDataToPost
  };

  let _createEntityVariant = function (sContextId) {
    let oActiveEntity = ContentUtils.getActiveEntity();
    let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);

    let oDataToPost = _getCreateVariantRequestData(sContextId);

    let oCallbackData = {};
    oCallbackData.contextId = sContextId;

    let sLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let sURL = getRequestMapping(sScreenMode).CreateEntityVariant;

    CS.putRequest(sURL, {lang: sLanguage}, oDataToPost, successHandleCreateContentVariant.bind(this, oCallbackData), failureHandleCreateContentVariant);
  };

  let _createNewUOMVariant = function (sContextId, oCallBackData) {
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sContextId);
    let oSelectedVisibleContext = oVariantContextByContextProps.getSelectedVisibleContext();

    if (_getIsEditFromTableEnable(oSelectedVisibleContext)) {
      _validateAndCreateLimitedObject(sContextId, oCallBackData);
    } else {
      _createEntityVariant(sContextId);
    }
  };

  let _validateAndCreateLimitedObject = function (sContextId, oCallBackData) {
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sContextId);
    let oSelectedVisibleContext = oVariantContextByContextProps.getSelectedVisibleContext();
    let oDummyVariant = oVariantContextByContextProps.getDummyVariant();
    oDummyVariant = oDummyVariant.contentClone || oDummyVariant;
    if (!ContentUtils.validateVariantContextSelection(oDummyVariant, oSelectedVisibleContext)) {
      return;
    }
    let sActiveContentId = _getKlassInstanceId();
    TableViewProps.resetTableFilterViewProps(sContextId, sActiveContentId);
    _fetchUOMData(sContextId, "", "", "", oCallBackData);
  };

  let successHandleCreateContentVariant = function (oCallbackData, oResponse) {
    var oContentStore = ContentUtils.getContentStore();
    let oKlassInstance = oResponse.success.klassInstance;

    let aPayloadData = [oKlassInstance.id, {entityType: oKlassInstance.baseType}];
    oCallbackData.breadCrumbData = BreadCrumbStore.createBreadcrumbItem("", "", "", "", {}, {}, "", aPayloadData, oContentStore.fetchArticleById);

    oContentStore.successFetchArticleById(oCallbackData, oResponse);

    _triggerChange();
  };

  let failureHandleCreateContentVariant = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureHandleCreateContentVariant", getTranslation());
  };

  let _handleVariantDialogSaveClicked = function (sOpenedContextId, sEditContext) {
    sOpenedContextId = CS.isEmpty(sOpenedContextId) ? UOMProps.getOpenedDialogTableContextId() : sOpenedContextId;
    let oFilterContext = _createFilterContext(sOpenedContextId);
    var oVariantContextProps = _getVariantContextPropsByContext(sOpenedContextId);
    var sOpenedDialogVariantContext = oVariantContextProps.getVariantDialogOpenContext();
    let oReferencedVariantContexts = oVariantContextProps.getReferencedVariantContexts();
    let oReferencedContext = oReferencedVariantContexts[sOpenedContextId] || {};

    if (CS.includes(sOpenedDialogVariantContext, "create") || sEditContext === "create") {

      if (oReferencedContext.type === ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
        _createAttributeVariant(oFilterContext);
      } else {
        let oCallback = {
          functionToExecute: function () {
            alertify.success(getTranslation().REFRESH_AFTER_CREATE);
          },
          filterContext: oFilterContext
        };
        _createNewUOMVariant(sOpenedContextId, oCallback);
      }

    } else {
      if (oReferencedContext.type === ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
        _saveAttributeVariant(); // eslint-disable-line
      } else {
        _saveUOMVariant(sOpenedContextId);
      }
    }
  };

  let _saveUOMVariant = function (sOpenedContextId) {
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sOpenedContextId);
    let oVariant = oVariantContextByContextProps.getActiveVariantForEditing();

    if (CS.isEmpty(oVariant.contentClone)) {
      ContentUtils.showMessage(getTranslation().SCREEN_STORE_SAVE_NOTHING_CHANGED);
      return;
    }

    let ContentStore = ContentUtils.getContentStore();
    let oVariantToSave = ContentStore.mapContentsForServer(oVariant);
    let sScreenContext = ContentUtils.getScreenModeBasedOnEntityBaseType(oVariant.baseType);
    let sUrl = getRequestMapping(sScreenContext).SaveEntity;
    let fSuccess = successSaveUOMVariant.bind(this, sOpenedContextId);
    let fFailure = failureSaveUOMVariant;

    let oData = {
      isRollback: false
    };

    CS.postRequest(sUrl, oData, oVariantToSave, fSuccess, fFailure);
  };

  let successSaveUOMVariant = function () {
    /** sContextId not being used now but might be required later **/
    _closeVariantDialog();
    let ContentStore = ContentUtils.getContentStore();
    ContentStore.handleRefreshContentButtonClicked();
  };

  let failureSaveUOMVariant = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureSaveUOMVariant", getTranslation())
  };

  let _handleVariantDialogCancelClicked = function () {
    let sTableContext = UOMProps.getOpenedDialogTableContextId();
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sTableContext);

    var oEditableVariant = oVariantContextByContextProps.getActiveVariantForEditing();
    var oDummyVariant = oVariantContextByContextProps.getDummyVariant();

    var sOpenedDialogContext = oVariantContextByContextProps.getVariantDialogOpenContext();
    var oActiveVariant = CS.includes(sOpenedDialogContext, "create") ? oDummyVariant : oEditableVariant;

    if (oActiveVariant && oActiveVariant.contentClone) {
      delete oActiveVariant.contentClone;
      delete oActiveVariant.isDirty;
    }

    _closeVariantDialog();
    CommonUtils.removeQuickListBreadCrumbFromPath();
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
  };

  let _handleUOMVariantEditButtonClicked = function (sVariantId, sContext, sTableContextId) {
    let oCallbackData = {
      context: sContext,
      isCreate: false,
      tableContextId: sTableContextId,
      functionToExecute: _handleUOMVariantPropsForDialog
    };

    let oVariantContextProps = _getVariantContextPropsByContext(sTableContextId);
    let oReferencedContexts = oVariantContextProps.getReferencedVariantContexts();
    let oReferencedContext = oReferencedContexts[sTableContextId];

    if (oReferencedContext.type == ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
      _setEditableAttributeVariant(sVariantId, sTableContextId);
    } else {
      _fetchUOMVariantById(sVariantId, oCallbackData);
    }

  };

  let _getLinkedInstances = function (oLinkedInstances) {
    let aLinkedInstanceList = [];
    CS.forEach(oLinkedInstances, function (aLinkedInstances) {
      CS.forEach(aLinkedInstances, function (oLinkedInstance) {
        aLinkedInstanceList.push({
          id: oLinkedInstance.id,
          baseType: oLinkedInstance.baseType
        });
      });
    });
    return aLinkedInstanceList;
  };

  let _handleDeleteUOMVariantClicked = function (sVariantId, sContextId, oFilterContext, oFilterData, oExtraData) {
    let oUnitVariantContextProps = _getVariantContextPropsByContext(sContextId);
    let oReferencedContext = oUnitVariantContextProps.getSelectedVisibleContext() || oUnitVariantContextProps.getSelectedContext();
    let oCallbackData = {filterContext: oFilterContext};
    if (oReferencedContext.type == ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
      _deleteUOMAttributeVariant(sVariantId, sContextId, oCallbackData);
    } else {
      _deleteUOMVariant(sVariantId, sContextId, oCallbackData);
    }
    let oUnitTableProps = _getTableViewPropsByContext(sContextId);
    let aOriginalBodyData = CS.cloneDeep(oUnitTableProps.getOriginalRowData());
    if (oFilterData.appliedFilterData.length > 0 && aOriginalBodyData.length <= 1) {
      oExtraData.contextId = sContextId;
      var oFilterStore = ContentUtils.getFilterStore(oExtraData.filterContext);
      CS.forEach(oFilterData.appliedFilterData, function (oFilter) {
        let sId = oFilter.id;
        oFilterStore.removeFilterByGroupId(sId, oExtraData);
      });
    }
  };

  let _deleteUOMVariant = function (sVariantId, sContextId, oCallbackData) {
    let oActiveEntity = ContentUtils.getActiveEntity();
    let sScreenContext = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);
    let sUrl = getRequestMapping(sScreenContext).DeleteEntityVariant;

    var oPostDataForBulkDelete = {
      "contentId": oActiveEntity.id,
      "ids": [sVariantId]
    };

    CS.deleteRequest(sUrl, {}, oPostDataForBulkDelete, successDeleteUOMVariant.bind(this, sContextId, oCallbackData), failureDeleteUOMVariant);
  };

  let successDeleteUOMVariant = function (sContextId, oCallbackData, oResponse) {
    /** sContextId not being used now but might be required later
     let ContentStore = ContentUtils.getContentStore();
     ContentStore.handleRefreshContentButtonClicked();*/

        //TODO : Temporary solution : after delete of UOM variant reset contents dirty flag
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);
    let oActiveContent = ContentUtils.getActiveContent();
    let oUnitTableProps = TableViewProps.getTableViewPropsByContext(sIdForProps, oActiveContent.id);
    if (CS.isNotEmpty(oUnitTableProps)) {
      oUnitTableProps.getTableOrganiserConfig().isTableContentDirty = false;
    }

    if (oCallbackData && oCallbackData.functionToExecute) {
      if (oCallbackData.selectedTab === ContentScreenConstants.tabItems.TAB_RENDITION) {
        oCallbackData.functionToExecute();
      }
      alertify.success(getTranslation().REFRESH_AFTER_DELETE);
    } else {
      _fetchUOMData(sContextId, "", false, false, oCallbackData);
      alertify.success(getTranslation().REFRESH_AFTER_DELETE);
      _triggerChange();
    }
  };

  let failureDeleteUOMVariant = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureSaveUOMVariant", getTranslation())
  };

  let _getDataForHandler = (valueData, secondParameter, oElement, sType) => {
    var sValue = valueData;
    var sValueAsHtml = "";
    var sExpressionId = "";

    if (ContentUtils.isAttributeTypeDate(sType)) {
      if (secondParameter !== "") {
        sValue = Date.parse(secondParameter);
      }
    }
    else if (ContentUtils.isAttributeTypeHtml(sType)) { // For HTML type attribute
      sValueAsHtml = valueData.htmlValue;
      sValue = valueData.textValue;
    }
    else if (ContentUtils.isAttributeTypeConcatenated(sType)) {
      sExpressionId = secondParameter;
      sValueAsHtml = valueData.htmlValue;
      sValue = valueData.textValue;
    }
    else if (valueData.target) {
      sValue = valueData.target.value;
    }

    var oData = {
      value: sValue,
      valueAsHtml: sValueAsHtml,
      expressionId: sExpressionId
    };

    var iPrecision = oElement.properties["precision"];
    if (CS.isNumber(iPrecision)) {
      oData.precision = iPrecision;
    }

    return oData;
  };

  var _handleVariantTagSummaryDateValueChanged = function (sKey, sValue) {
    var sOtherKey = (sKey === "to") ? "from" : "to";
    var sKeyOldValue, sOtherKeyOldValue;

    var sOpenDialogContextId = UOMProps.getOpenedDialogTableContextId();
    var oVariantContextPropsByContext = _getVariantContextPropsByContext(sOpenDialogContextId);
    var sDialogOpenContext = oVariantContextPropsByContext.getVariantDialogOpenContext();

    var oVariantObject = CS.includes(sDialogOpenContext, "create") ? oVariantContextPropsByContext.getDummyVariant() :
                         oVariantContextPropsByContext.getActiveVariantForEditing();
    var oCurrentVariantObject = ContentUtils.makeContentDirty(oVariantObject);

    if (!oCurrentVariantObject.timeRange) {
      oCurrentVariantObject.timeRange = {
        to: null,
        from: null
      }
    }

    var oTimeRange = oCurrentVariantObject.timeRange;
    sKeyOldValue = oTimeRange[sKey];
    sOtherKeyOldValue = oTimeRange[sOtherKey];
    oTimeRange[sKey] = sValue;

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
  };

  var _handleUOMViewDateRangeApplied = function (oRangeData, sContextId) {
    _handleCustomUOMViewDateRangeApplied(oRangeData, sContextId);
    let oCallBackData = {
      filterContext: _createFilterContext(sContextId)
    };
    _fetchUOMData(sContextId, "", false, false, oCallBackData, _getKlassInstanceId());
  };

  let _handleVariantCellValueChanged = function (oCellData, sTableContextId, sViewContext) {
    let oComponentProps = ContentUtils.getComponentProps();
    let bIsForTableContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
    if (bIsForTableContext) {
      let oActivePopOverContextData = oComponentProps.uomProps.getActivePopOverContextData();
      sTableContextId = CS.isNotEmpty(oActivePopOverContextData.idForProps) ? oActivePopOverContextData.idForProps: sTableContextId;
    }
    let oUOMContextProps = _getVariantContextPropsByContext(sTableContextId);
    let oUOMTableProps = _getTableViewPropsByContext(sTableContextId);
    let aTableBodyData = oUOMTableProps.getBodyData();
    let oSettings = oUOMTableProps.getSettings();
    let bIsTranspose = oSettings && oSettings.isTranspose;
    let aHeaderData = bIsTranspose ? CS.cloneDeep(oUOMTableProps.getHeaderData()) : oUOMTableProps.getHeaderData();
    let oHtmlData;
    let oValue = CS.clone(oCellData.value);
    let oRow = CS.find(aTableBodyData, {id: oCellData.rowId});
    let sEntityId = bIsTranspose ? oCellData.rowId : oCellData.columnId;
    let sColumnId = oCellData.columnId;
    let oColumn = CS.find(aHeaderData, {id: sColumnId});
    let oCell = oRow.properties[sColumnId];
    let sType = bIsTranspose ? oRow.type : oColumn.type

    if (oValue && oValue.textValue) {
      oHtmlData = _getDataForHandler(oValue, oCellData.expressionId || "", oRow, oColumn.baseType);
      oCellData.textValue = oValue.textValue;
      oCellData.valueAsHtml = oHtmlData.valueAsHtml;
      oCellData.expressionId = oHtmlData.expressionId;
    }

    let oReferencedAttribute = {};
    let sAttributeType = "";

    switch (sType) {
      case "attribute":
        oReferencedAttribute = oUOMContextProps.getReferencedAttributes()[sEntityId];
        sAttributeType = oReferencedAttribute.type;

        if (CS.isEmpty(oCell.attributeId)) {
          oCell = ContentUtils.createAttributeInstanceObject("", oReferencedAttribute.id);
          oRow.properties[sColumnId] = oCell;
        }

        oCell.originalValue = oCell.value = CS.isObject(oCellData.value) ? oCellData.value.textValue + "" : oCellData.value + "" ; //to always keep value in string

        if (ContentUtils.isAttributeTypeHtml(sAttributeType)) {
          oCell.value = oCellData.valueAsHtml;
          oCell.valueAsHtml = oCellData.textValue;
        }
        else if (ContentUtils.isAttributeTypeConcatenated(sAttributeType)) {
          let aExpressionList = oCell.expressionList;
          let sExpressionId = oCellData.expressionId;
          var oFoundExpression = CS.find(aExpressionList, {id: sExpressionId}) || {};
          if (oFoundExpression.type === "html") {
            oFoundExpression.value = oCellData.textValue;
            oFoundExpression.valueAsHtml = oCellData.valueAsHtml;
          } else {
            oFoundExpression.value = oCellData.value;
          }
        } else if (ContentUtils.isAttributeTypeNumber(sAttributeType) ||
            ContentUtils.isAttributeTypeDate(sAttributeType) ||
            ContentUtils.isAttributeTypeMeasurement(sAttributeType)) {
          oCell.valueAsNumber = oCellData.value;
          oCell.value = oCellData.value;
          // oCell.value = AttributeUtils.getAttributeValueToShow(oCell, oReferencedAttribute);
        } else {
          oCell.value = AttributeUtils.getAttributeValueToShow(oCell, oReferencedAttribute);
        }

        break;

      case "date":
        if (sColumnId === "from") {
          let oToCell = oRow.properties["to"];
          oToCell.minValue = oCellData.value;

          if (!CS.toNumber(oToCell.value)) {
            let iCurrentEndDate = ContentUtils.convertTimeStampToEOD(oCellData.value);
            oToCell.value = iCurrentEndDate;
            oToCell.originalValue = iCurrentEndDate;
          }
        } else if (sColumnId === "to") {
          let oFromCell = oRow.properties["from"];
          oFromCell.maxValue = oCellData.value;
          if (!CS.toNumber(oFromCell.value)) {
            let iCurrentStartDate = ContentUtils.convertTimeStampToSOD(oCellData.value);
            oFromCell.value = iCurrentStartDate;
            oFromCell.originalValue = iCurrentStartDate;
          }
        }

        oCell.originalValue = oCell.value = oCellData.value;
        break;

      case "tag":
        let oReferencedTag = oUOMContextProps.getReferencedTags()[sEntityId];

        if (CS.isEmpty(oCell.tagId)) {
          oCell = ContentUtils.createTagInstanceObject(oReferencedTag, false);
          oRow.properties[sColumnId] = oCell;
        }

        let aTagValues = oCell.tagValues;

        //set relevance 0 for single select
        if ((!oReferencedTag.isMultiSelect && oReferencedTag.tagType === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) ||
            oReferencedTag.tagType === TagTypeConstants.RULER_TAG_TYPE ||
            oReferencedTag.tagType === TagTypeConstants.STATUS_TAG_TYPE ||
            oReferencedTag.tagType === TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE ||
            oReferencedTag.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN
        ) {
          CS.forEach(aTagValues, function (oTagValue) {
            oTagValue.relevance = 0;
          });
        }

        let oReferencedTagLabelMap = {};

        CS.forEach(oReferencedTag.children, function (oReferencedTagValue) {
          let sTagValueId = oReferencedTagValue.id;

          //store tag label against tag value id
          oReferencedTagLabelMap[sTagValueId] = oReferencedTagValue.label;

          let oTagValue = CS.find(aTagValues, {tagId: sTagValueId});
          if (CS.includes(oCellData.checkedItems, sTagValueId)) {

            if (CS.isEmpty(oTagValue)) {
              oTagValue = ContentUtils.getNewTagValue(sTagValueId, 0, false);
              aTagValues.push(oTagValue);
            }

            if (oCellData.relevanceMap && oCellData.relevanceMap[sTagValueId] !== null && oCellData.relevanceMap[sTagValueId] !== undefined) {
              oTagValue.relevance = oCellData.relevanceMap[sTagValueId];
            } else {
              oTagValue.relevance = 100;
            }

            if (oReferencedTag.tagType == TagTypeConstants.TAG_TYPE_BOOLEAN) {
              oCell.value = (oTagValue.relevance == 100);
            }


          } else {
            if (!CS.isEmpty(oTagValue)) {
              oTagValue.relevance = 0;
            }
          }
        });

        if (oReferencedTag.tagType != TagTypeConstants.TAG_TYPE_BOOLEAN) {
          let aTagLabels = [];
          CS.forEach(aTagValues, function (oTagValue) {
            if (oTagValue.relevance != 0) {
              let sTagLabel = oReferencedTagLabelMap[oTagValue.tagId];
              sTagLabel && aTagLabels.push(sTagLabel);
            }
          });
          oCell.value = aTagLabels.join(', ');
        }

        oCell.tag.tagValues = aTagValues;

        break;

      case "linkedInstances":
        let oAppData = ContentUtils.getAppData();
        let aAvailableEntities = oAppData.getAvailableEntities();
        let aSelectedLinkedInstanceIds = oCellData.selectedLinkedInstanceIds;
        if (oCellData.hasOwnProperty("selectedLinkedInstanceIds")) {
          if (sViewContext === "variantQuickList") {
            CS.remove(oCell.values, function (sId) {
              return CS.includes(aSelectedLinkedInstanceIds, sId);
            });
            CS.remove(oCell.referencedLinkedEntites, function (oReferencedEntities) {
              return CS.includes(aSelectedLinkedInstanceIds, oReferencedEntities.id);
            });
          } else {
            oCell.values = aSelectedLinkedInstanceIds;
            CS.remove(oCell.referencedLinkedEntites, function (oReferencedEntities) {
              return !CS.includes(aSelectedLinkedInstanceIds, oReferencedEntities.id);
            });
          }
        } else if (!CS.isEmpty(oCellData.addedLinkedInstances)) {
          let aIds = [];
          let aLinkedInstances = [];
          CS.forEach(oCellData.addedLinkedInstances, function (oLinkedInstance) {
            aIds.push(oLinkedInstance.id);
            let oEntity = CS.find(aAvailableEntities, {id: oLinkedInstance.id});
            aLinkedInstances.push(oEntity);
          });
          oCell.values.push(...aIds);
          oCell.referencedLinkedEntites.push(...aLinkedInstances);
        }

        let aNewLabels = CS.map(oCell.referencedLinkedEntites, 'name');
        oCell.value = aNewLabels.join(', ');
        break;

      case "custom":
        oReferencedAttribute = oUOMContextProps.getReferencedAttributes()[oRow.attributeId];
        if (ContentUtils.isAttributeTypeHtml(oReferencedAttribute.type)) {
          oCell.value = oHtmlData.value.htmlValue;
          oCell.valueAsHtml = oHtmlData.value.textValue;
        } else {
          oCell.value = oValue;
        }
        break;

      case "taxonomy":
        oCell.model.selectedOptions = oValue;
        break;

    }

    let oTableOrganiserConfig = oUOMTableProps.getTableOrganiserConfig();
    oTableOrganiserConfig.isTableContentDirty = true;

    oCell.isDirty = true;
    oRow.isDirty = true;
    oColumn.isDirty = true;
    let aDirtyTableContextIds = ContentScreenProps.screen.getDirtyTableContextIds();
    if (!CS.includes(aDirtyTableContextIds, sTableContextId)) {
      aDirtyTableContextIds.push(sTableContextId);
    }

    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    if (CS.isNotEmpty(oOpenedDialogAttributeData.contextId)) {
      ContentScreenProps.contentGridProps.setIsGridDataDirty(true);
      let sParentContextId = oOpenedDialogAttributeData.parentContextId || sTableContextId;
      let oUOMTableProps = _getTableViewPropsByContext(sParentContextId, ContentUtils.getActiveContent().id);
      let aTableBodyData = oUOMTableProps.getBodyData();
      let sKlassInstanceId = oOpenedDialogAttributeData.klassInstanceId || oRow.id;
      let oRowData = CS.find(aTableBodyData, {id: sKlassInstanceId});
      oRowData.isDirty = true;
    }

    bIsTranspose && oUOMTableProps.setHeaderData(aHeaderData);

    _triggerChange();
  };

  let _handleUOMViewFullScreenButtonClicked = (sContextId) => {
    let sFullScreenContextId = UOMProps.getFullScreenTableContextId();
    sContextId = sFullScreenContextId === sContextId ? "" : sContextId;
    UOMProps.setFullScreenTableContextId(sContextId);
  };

  let _handleUOMViewFullScreenCancelButtonClicked = (sContextId) => {
    _handleUOMTableDiscardButtonClicked(sContextId);
    _handleUOMViewFullScreenButtonClicked(sContextId);
  };

  let _handleVariantAddLinkedInstances = function (aEntities) {
    let oAvailableEntityProps = ContentScreenProps.availableEntityViewProps;
    let aSelectedEntities = aEntities || oAvailableEntityProps.getSelectedEntities();
    let oActiveTablePopOverContextData = UOMProps.getActivePopOverContextData();
    let sContextId = oActiveTablePopOverContextData.contextId;
    let sRowId;
    let sColumnId;
    if (oActiveTablePopOverContextData.isTranspose) {
      sRowId = oActiveTablePopOverContextData.entity;
      sColumnId = oActiveTablePopOverContextData.contextInstanceId;
    } else {
      sRowId = oActiveTablePopOverContextData.contextInstanceId;
      sColumnId = oActiveTablePopOverContextData.entity;
    }
    let oCellData = {
      rowId: sRowId,
      columnId: sColumnId,
      type: "linkedInstances",
      addedLinkedInstances: aSelectedEntities,
    };
    _handleVariantCellValueChanged(oCellData, sContextId);
  };

  let _handleGridViewRemoveLinkedInstanceClicked = (oViewData, aSelectedItems, sViewContext) => {
    if (CS.isEmpty(oViewData)) {
      let oActiveTablePopOverContextData = UOMProps.getActivePopOverContextData();
      oViewData = {
        rowId: oActiveTablePopOverContextData.contextInstanceId,
        columnId: oActiveTablePopOverContextData.entity,
        type: "linkedInstances",
        sTableContextId: oActiveTablePopOverContextData.tableContextId
      };
    }
    oViewData.selectedLinkedInstanceIds = aSelectedItems;
    _handleVariantCellValueChanged(oViewData, oViewData.sTableContextId, sViewContext);
  };

  let _handleCustomUOMViewPaginationDataChanged = function (sContextId, oNewPaginationData, oFilterContext) {
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);
    let sActiveContentId = _getKlassInstanceId();
    let oUnitTableFilterProps = TableViewProps.getTableFilterViewPropsByContext(sIdForProps, sActiveContentId);
    let oPaginationData = oUnitTableFilterProps.getPaginationData();
    oPaginationData.from = oNewPaginationData.from;
    oPaginationData.pageSize = oNewPaginationData.pageSize;
  };

  let _handleCustomUOMViewDateRangeApplied = function (oRangeData, sContextId) {
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);
    let sActiveContentId = _getKlassInstanceId();
    let oUnitTableFilterProps = TableViewProps.getTableFilterViewPropsByContext(sIdForProps, sActiveContentId);
    oUnitTableFilterProps.setTimeRangeData({
      from: oRangeData.startDate,
      to: oRangeData.endDate
    });
  };

  let _handleCustomUOMViewSortDataChanged = function (sContextId, oSortData) {
    let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);
    let sActiveContentId = _getKlassInstanceId();
    let oUnitTableFilterProps = TableViewProps.getTableFilterViewPropsByContext(sIdForProps, sActiveContentId);
    oUnitTableFilterProps.setSortData(oSortData);
  };

  let _validateAttributeVariant = function (oActiveVariant, oSelectedVisibleContext, oNewAttributeInstanceObject) {
    if (!ContentUtils.validateVariantContextSelection(oActiveVariant, oSelectedVisibleContext)) {
      return false;
    } else if (!CS.isNumber(oNewAttributeInstanceObject.value) && CS.isEmpty(oNewAttributeInstanceObject.value)) {
      alertify.message(getTranslation().PLEASE_ENTER_VALUE_TO_PROCEED);
      return false;
    } else {
      return true;
    }
  };

  let _processAttributeVariant = function (sTableContext, sActiveEntityId, sParentContextId, oNewAttributeInstanceObject, oCallbackData) {
    let oContentStore = ContentUtils.getContentStore();
    let sIdForProps = ContentUtils.getIdForTableViewProps(sTableContext);
    let oUnitTableProps = TableViewProps.getTableViewPropsByContext(sIdForProps, sActiveEntityId);
    let oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    let oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    let oReferencedContext = oEmbeddedVariantContexts[sTableContext] || {};

    let sVariantInstanceId = oUnitTableProps && oUnitTableProps.getVariantInstanceId();
    if (sVariantInstanceId && sVariantInstanceId !== ContentUtils.getActiveContent().id) {
      let {aAttributes, aTags, sAllSearchText, aSortOptions, oPaginationData, oTimeRangeData} = _getFetchUOMFilterInformation(sIdForProps, sVariantInstanceId);
      oCallbackData.variantInstanceId = sVariantInstanceId;
      let oData = {
        contextId: sParentContextId,
        klassInstanceId: ContentUtils.getActiveEntity().id,
        parentId: sVariantInstanceId,
        attributes: aAttributes,
        tags: aTags,
        allSearch: sAllSearchText,
        sortOptions: aSortOptions,
        from: oPaginationData.from || 0,
        size: oPaginationData.pageSize || 20,
        columnIds: [],
        timeRange: oTimeRangeData
      };
      let oExtraData = {
        isBulkSaveTableDataSuccess: true
      };
      let oInstancesToSave = _getInstancesToSaveFromTableDataForAttributeVariant(sTableContext, oReferencedContext, sActiveEntityId, sParentContextId, oNewAttributeInstanceObject);
      oCallbackData.doNotTrigger = true;
      _bulkSaveVariantTableData(sTableContext, oReferencedContext, false, ContentUtils.getActiveContent(), oData, oCallbackData, oExtraData, oInstancesToSave);
    }
    else {
      oContentStore.saveActiveContent(oCallbackData);
    }
  };

  let _getAttributeVariantInstance = function (oVariant, sAttributeId) {
    let oAttributeInstance = ContentUtils.createAttributeInstanceObject(oVariant.value, sAttributeId, true);
    oAttributeInstance.tags = oVariant.tags;
    oAttributeInstance.context = CS.cloneDeep(oVariant);
    oAttributeInstance.context.linkedInstances = _getLinkedInstances(oVariant.linkedInstances);
    oAttributeInstance.valueAsNumber = oVariant.valueAsNumber;
    oAttributeInstance.timeRange = oVariant.timeRange;

    if (!CS.isEmpty(oVariant.valueAsHtml)) {
      oAttributeInstance.valueAsHtml = oVariant.valueAsHtml;
      oAttributeInstance.value = oVariant.value;
    }

    //delete un-wanted properties
    delete oAttributeInstance.context.contentClone;
    delete oAttributeInstance.context.tags;
    delete oAttributeInstance.context.contextTags;
    delete oAttributeInstance.context.value;
    delete oAttributeInstance.context.isDirty;
    delete oAttributeInstance.context.attributes;

    return oAttributeInstance;
  };

  let _getCreateAttributeContextDialogData = function () {
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    let oVariantContextByContextProps = _getVariantContextPropsByContext(oOpenedDialogAttributeData.contextId);
    let oEditableVariant = oVariantContextByContextProps.getActiveVariantForEditing();
    let oDummyVariant = oVariantContextByContextProps.getDummyVariant();
    let sOpenedDialogContext = oVariantContextByContextProps.getVariantDialogOpenContext();
    let oActiveVariant = CS.includes(sOpenedDialogContext, "create") ? oDummyVariant : oEditableVariant;

    oActiveVariant = oActiveVariant.contentClone ? oActiveVariant.contentClone : oActiveVariant;

    let oSelectedVisibleContext = oVariantContextByContextProps.getSelectedVisibleContext();
    let oNewAttributeInstanceObject = _getAttributeVariantInstance(oActiveVariant, oOpenedDialogAttributeData.attributeId);
    return {oActiveVariant, oSelectedVisibleContext, oNewAttributeInstanceObject};
  };

  let _handleAttributeContextViewDialogButtonClick = function (sAttributeContextId, sButtonId) {
    let oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    let oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    let oReferencedContext = oEmbeddedVariantContexts[sAttributeContextId];
    switch (sButtonId) {
      case "ok":
        _handleAttributeContextViewCloseDialog(sAttributeContextId);
        break;
      case "discard":
        _handleUOMTableDiscardButtonClicked(sAttributeContextId);
        ContentScreenProps.contentGridProps.setIsGridDataDirty(false);
        break;
      case "save":
        if (oReferencedContext.type === ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT) {
          _handleAttributeVariantSave(sAttributeContextId);
        }
        break;
    }
  };

  let _removeAttributeVariantInstanceFromContent = function () {
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    let sAttributeId = oOpenedDialogAttributeData.attributeId;
    if (!sAttributeId) {
      return;
    }

    let sNewSuffix = ContentUtils.getNewSuffix();
    let oActiveContent = ContentUtils.getActiveContent();
    let oActiveContentClone = oActiveContent.contentClone || {};
    let sTableContext = UOMProps.getOpenedDialogTableContextId();
    let oVariantContextByContextProps = _getVariantContextPropsByContext(sTableContext);
    if (CS.isNotEmpty(oVariantContextByContextProps )) {
      let oEditableVariant = oVariantContextByContextProps.getActiveVariantForEditing();

      CS.remove(oActiveContentClone.attributes, function (oAttribute) {
        if ((oAttribute.attributeId == sAttributeId && CS.includes(oAttribute.id, sNewSuffix) && !CS.isEmpty(oAttribute.context)) ||
            (oAttribute.id == oEditableVariant.id)) {
          return true;
        }
      });

      CS.remove(oActiveContent.attributes, {id: oEditableVariant.id});
    }

    delete oActiveContent.contentClone;
    delete oActiveContent.isDirty;
  };

  let _getAttributeContextTagsADM = function (oClonedData, oOriginalData) {
    let oOldTags = CS.filter(oOriginalData, {baseType: BaseTypeDictionary.tagInstanceBaseType});
    let oClonedTags = CS.filter(oClonedData, {baseType: BaseTypeDictionary.tagInstanceBaseType});
    return ContentUtils.generateADMForTags(oOldTags, oClonedTags);
  };

  let _prepareModifiedAttributeContextModel = function (sContextId) {
    let oAttributeContextUOMTableProps = _getTableViewPropsByContext(sContextId);
    let aAttributeContextClonedData = oAttributeContextUOMTableProps.getBodyData();
    let aAttributeContextOriginalData = oAttributeContextUOMTableProps.getOriginalRowData();
    let oActiveEntity = ContentUtils.getActiveContent();
    let aModifiedAttributeContexts = [];

    CS.forEach(aAttributeContextClonedData, (oAttributeContextClonedData) => {
      if (oAttributeContextClonedData.isDirty) {
        let oAttributeContextOriginalData = CS.find(aAttributeContextOriginalData, {id: oAttributeContextClonedData.id});
        let oAttributeContextClonedProperties = oAttributeContextClonedData.properties;
        let oAttributeContextOriginalProperties = oAttributeContextOriginalData.properties;
        let oTagsADM = _getAttributeContextTagsADM(oAttributeContextClonedProperties, oAttributeContextOriginalProperties);

        let oAttributeContextValue = oAttributeContextClonedProperties.value;

        let oFrom = oAttributeContextClonedProperties.from || {};
        let oTo = oAttributeContextClonedProperties.to || {};
        let oValue = {
          value: oAttributeContextValue.value
        };
        if(oAttributeContextValue.rendererType === GridViewPropertyTypes.HTML) {
          oValue.valueAsHtml = oAttributeContextValue.value;
          oValue.value = oAttributeContextValue.valueAsHtml;
        }

        let oModifiedAttributeContext = {
          id: oAttributeContextClonedData.id,
          attributeId: oAttributeContextValue.attributeId,
          baseType: oAttributeContextValue.baseType,
          timeRange: {
            from: oFrom.value || null,
            to: oTo.value || null
          },
          addedTags: oTagsADM.added,
          modifiedTags: oTagsADM.modified,
          deletedTags: oTagsADM.deleted,
          modifiedContext: {
            id: oAttributeContextClonedData.id,
            contextId: sContextId,
            name: "",
            baseType: oActiveEntity.baseType,
            linkedInstances: [],
            timeRange: {
              from: oFrom.value || null,
              to: oTo.value || null
            }
          }
        };

        CS.assign(oModifiedAttributeContext, oValue);
        aModifiedAttributeContexts.push(oModifiedAttributeContext);
      }
    });
    return aModifiedAttributeContexts;
  };

  let _getInstancesToSaveFromTableDataForAttributeVariant = function (sContextId, oReferencedContext, sActiveEntityId, sParentContextId, oNewAttributeInstanceObject) {
    let aInstancesToSave = [];
    let oUOMContextProps = TableViewProps.getVariantContextPropsByContext(sParentContextId, ContentUtils.getActiveContent().id);
    let oSelectedVisibleContext = oUOMContextProps.getSelectedVisibleContext();
    let oUOMTableProps = TableViewProps.getTableViewPropsByContext(sParentContextId, ContentUtils.getActiveContent().id);
    let aTableBodyData = oUOMTableProps.getBodyData();
    let oActiveEntity = ContentUtils.getActiveContent();
    let oADMForAttributes = {
      added: [],
      deleted: [],
      modified: []
    };

    let oAttributeContextUOMTableProps = _getTableViewPropsByContext(sContextId);
    let aAttributeContextClonedData = oAttributeContextUOMTableProps.getBodyData();

    CS.forEach(aTableBodyData, function (oModifiedRow) {
      if (oModifiedRow.isDirty) {
        let oInstanceToSave = {
          id: oModifiedRow.id,
          name: oModifiedRow.properties[SystemLevelIdDictionary.NameAttributeId].value,
          versionId: oModifiedRow.versionId,
          types: [oSelectedVisibleContext.contextKlassId],
          baseType: oActiveEntity.baseType
        };

        if (CS.isNotEmpty(oNewAttributeInstanceObject)) {
          let oFoundAttribute = CS.find(aAttributeContextClonedData, {id: oNewAttributeInstanceObject.id});
          if (CS.isEmpty(oFoundAttribute)) {
            oNewAttributeInstanceObject = ContentUtils.preProcessAttributeBeforeSave(oNewAttributeInstanceObject);
            oADMForAttributes.added = [oNewAttributeInstanceObject];
          } else {
            oADMForAttributes.deleted = [oNewAttributeInstanceObject.id];
          }
        }
        else {
          oADMForAttributes.modified = _prepareModifiedAttributeContextModel(sContextId);
        }

        oInstanceToSave.addedAttributes = oADMForAttributes.added;
        oInstanceToSave.deletedAttributes = oADMForAttributes.deleted;
        oInstanceToSave.modifiedAttributes = oADMForAttributes.modified;
        oInstanceToSave.addedTags = [];
        oInstanceToSave.deletedTags = [];
        oInstanceToSave.modifiedTags = [];
        oInstanceToSave.addedLinkedInstances = [];
        oInstanceToSave.deletedLinkedInstances = [];


        aInstancesToSave.push(oInstanceToSave);
      }
    });

    return aInstancesToSave;
  };

  let _createAttributeVariant = function () {
    let {oActiveVariant, oSelectedVisibleContext, oNewAttributeInstanceObject} = _getCreateAttributeContextDialogData();

    if (!_validateAttributeVariant(oActiveVariant, oSelectedVisibleContext, oNewAttributeInstanceObject)) {
      return;
    }

    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    let oActiveEntity = ContentUtils.getActiveContent();
    if (oOpenedDialogAttributeData.parentContextId) {
      let oUOMTableProps = TableViewProps.getTableViewPropsByContext(oOpenedDialogAttributeData.parentContextId, oActiveEntity.id);
      let aTableBodyData = oUOMTableProps.getBodyData();
      let oRowData = CS.find(aTableBodyData, {id: oOpenedDialogAttributeData.klassInstanceId});
      oRowData.isDirty = true;
    } else {
      oActiveEntity = ContentUtils.makeActiveContentDirty();
      oActiveEntity.attributes.push(oNewAttributeInstanceObject);
    }

    let oCallbackData = {};
    oCallbackData.functionToExecute = _closeDialogAndFetchData;
    let sTableContext = UOMProps.getOpenedDialogTableContextId();
    let sActiveContentId = _getKlassInstanceId();
    _processAttributeVariant(sTableContext, sActiveContentId, oOpenedDialogAttributeData.parentContextId, oNewAttributeInstanceObject, oCallbackData);
  };

  let _deleteUOMAttributeVariant = function (sVariantId, sContextId, oCallbackData) {
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    let sAttributeId = oOpenedDialogAttributeData.attributeId;
    let oAttributeInstance = ContentUtils.createAttributeInstanceObject("", sAttributeId);
    oAttributeInstance.id = sVariantId;

    let oActiveEntity = ContentUtils.getActiveContent();
    if (oOpenedDialogAttributeData.parentContextId) {
      let oUOMTableProps = TableViewProps.getTableViewPropsByContext(oOpenedDialogAttributeData.parentContextId, oActiveEntity.id);
      let aTableBodyData = oUOMTableProps.getBodyData();
      let oRowData = CS.find(aTableBodyData, {id: oOpenedDialogAttributeData.klassInstanceId});
      oRowData.isDirty = true;
    } else {
      let oActiveEntity = ContentUtils.getActiveEntity();
      ContentUtils.makeActiveContentDirty();
      oActiveEntity.attributes.push(oAttributeInstance);
    }
    let oCallbackDataToSave = {
      filterContext: oCallbackData.filterContext,
      functionToExecute: _fetchUOMData.bind(this, oOpenedDialogAttributeData.contextId, oOpenedDialogAttributeData.attributeId, false, false, oCallbackData, oOpenedDialogAttributeData.klassInstanceId)
    };
    let sActiveContentId = oOpenedDialogAttributeData.klassInstanceId || oActiveEntity.id;

    _processAttributeVariant(sContextId, sActiveContentId, oOpenedDialogAttributeData.parentContextId, oAttributeInstance, oCallbackDataToSave);
  };

  let _handleAttributeVariantSave = function (sAttributeContextId, oCallbackData = {}) {
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    let sActiveContentId = _getKlassInstanceId();
    let oCallbackDataToSave = {
      filterContext: oCallbackData.filterContext || {},
      functionToExecute: function () {
        ContentScreenProps.contentGridProps.setIsGridDataDirty(false);
        _fetchUOMData(oOpenedDialogAttributeData.contextId, oOpenedDialogAttributeData.attributeId, false, false, oCallbackData, oOpenedDialogAttributeData.klassInstanceId)
      }
    };

    if (!oOpenedDialogAttributeData.parentContextId) {
      let oActiveContent = ContentUtils.getActiveContent();
      let oActiveContentClone = ContentUtils.makeActiveContentDirty();
      let oUnitTableProps = _getTableViewPropsByContext(sAttributeContextId, oActiveContent.id);
      let aClonedBodyData = oUnitTableProps.getBodyData();
      let aOriginalBodyData = oUnitTableProps.getOriginalRowData();
      let oAttributes = _getAttributesVariantToSave(aClonedBodyData, aOriginalBodyData, sAttributeContextId, oOpenedDialogAttributeData.attributeId);
      oActiveContent.attributes = CS.assign(oActiveContent.attributes, oAttributes.originalAttributes);
      oActiveContentClone.attributes = CS.assign(oActiveContentClone.attributes, oAttributes.clonedAttributes);
    }

    _processAttributeVariant(sAttributeContextId, sActiveContentId, oOpenedDialogAttributeData.parentContextId, {}, oCallbackDataToSave);
  };

  let _getUpdatedAttributeVariant = function (oRow, sTableContextId) {
    let oActiveContent = ContentUtils.getActiveContent();
    let oRowProperties = oRow.properties;
    let oVariantContextProps = _getVariantContextPropsByContext(sTableContextId, oActiveContent.id);
    let oReferencedContexts = oVariantContextProps.getReferencedVariantContexts();
    let oReferencedInstances = oVariantContextProps.getReferencedInstances();
    let oReferencedContext = oReferencedContexts[sTableContextId];
    let aReferencedTags = oReferencedContext.tags;
    let oReferencedAttributes = oVariantContextProps.getReferencedAttributes();
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oVariant = {
      id: oRow.id,
      contextId: sTableContextId,
      name: "",
      value: "",
      baseType: oActiveEntity.baseType,
      tags: [],
      linkedInstances: {},
      timeRange: {
        to: null,
        from: null
      }
    };

    //for tags
    CS.forEach(aReferencedTags, function (oReferencedTag) {
      if (oRowProperties[oReferencedTag.id]) {
        let oTagInstance = oRowProperties[oReferencedTag.id];
        oVariant.tags.push(oTagInstance);
      }
    });

    //for value
    let oValueProperty = oRowProperties.value;
    let sAttributeType = oReferencedAttributes[oValueProperty.attributeId].type;
    if (CS.isNotEmpty(oValueProperty)) {
      if (sAttributeType === AttributeTypeDictionary.HTML) {
        oVariant.value = oValueProperty.value;
        oVariant.valueAsHtml = oValueProperty.valueAsHtml;
      } else {
        oVariant.value = oValueProperty.value;
      }
    }

    //for 'From' in time range
    let oFromProperty = oRowProperties.from;
    if (oFromProperty) {
      oVariant.timeRange.from = +oFromProperty.value;
    }

    //for 'To' in time range
    let oToProperty = oRowProperties.to;
    if (oToProperty) {
      oVariant.timeRange.to = +oToProperty.value;
    }

    //for linked instances
    let oLinkedInstances = oVariant.linkedInstances;
    let aEntitiesList = EntitiesList();
    CS.forEach(oRowProperties, function (oRowProperty, sKey) {
      if (oRowProperty.id == "linkedInstances") {
        try {

          let oEntity = CS.find(aEntitiesList, {id: EntitiesIdMap[sKey]});
          if (oEntity) {
            oLinkedInstances[oEntity.id] = [];
            CS.forEach(oRowProperty.values, function (sId) {
              let oLinkedInstance = oReferencedInstances[sId];
              if (oLinkedInstance) {
                oLinkedInstances[oEntity.id].push(oLinkedInstance);
              }
            });
          }
        }

        catch (oException) {
          ExceptionLogger.error(oException);
        }
      }
    });
    return oVariant;
  }

  let _getAttributesVariantToSave = function (aClonedBodyData, aOriginalBodyData, sTableContextId, sAttributeId) {
    let aClonedAttributes = [];
    let aOriginalAttributes = [];

    CS.forEach(aClonedBodyData, function (oRow) {
      if (oRow.isDirty) {
        let oOriginalRow = CS.find(aOriginalBodyData, {id: oRow.id});
        let oOriginalVariant = _getUpdatedAttributeVariant(oOriginalRow, sTableContextId);
        let oClonedVariant = _getUpdatedAttributeVariant(oRow, sTableContextId);
        let oOriginalInstance = _getAttributeVariantInstance(oOriginalVariant, sAttributeId);
        let oClonedInstance = _getAttributeVariantInstance(oClonedVariant, sAttributeId);
        oOriginalInstance.id = oOriginalVariant.id;
        oClonedInstance.id = oClonedVariant.id;
        aOriginalAttributes.push(oOriginalInstance);
        aClonedAttributes.push(oClonedInstance);
      }
    });

    return {
      originalAttributes: aOriginalAttributes,
      clonedAttributes: aClonedAttributes
    };
  };

  let _handleUOMFilterPopoverClosed = function (oExtraData) {
    oExtraData.klassInstanceId = _getKlassInstanceId();
    let oFilterStore = ContentUtils.getFilterStore(oExtraData.filterContext);
    oFilterStore.discardSelectedFilters(oExtraData);
  };

  let _addRootNodeIdToReferencedTaxonomies = function (oTaxonomy) {
    if (oTaxonomy.parent.id != -1) {
      return _addRootNodeIdToReferencedTaxonomies(oTaxonomy.parent)
    } else return {id: oTaxonomy.id, code: oTaxonomy.code, label: oTaxonomy.label};
  };

  /*******************  PUBLIC API  *******************/
  return {
    getVariantContextPropsByContext: function (sContextId) {
      return _getVariantContextPropsByContext(sContextId);
    },

    getTableViewPropsByContext: function (sContextId) {
      return _getTableViewPropsByContext(sContextId);
    },

    /** Variant view get, delete and edit handlers*/
    fetchVariantData: function (sContextId, sAttributeId, bDoNotPreserveColumnSequence, bIsDisableLoader, oCallbackData) {
      oCallbackData.filterContext = oCallbackData.filterContext ? oCallbackData.filterContext : _createFilterContext(sContextId);
      _fetchUOMData(sContextId, sAttributeId, bDoNotPreserveColumnSequence, bIsDisableLoader, oCallbackData);
    },

    handleDeleteUOMVariantClicked: function (sVariantId, sContextId, oFilterData, oExtraData, bDeleteWithoutConfirmation, oFilterContext) {
      if (bDeleteWithoutConfirmation) {
        _handleDeleteUOMVariantClicked(sVariantId, sContextId, oFilterContext);
      } else {
        CustomActionDialogStore.showConfirmDialog(getTranslation().STORE_ALERT_CONFIRM_DELETE, "",
            function () {
              _handleDeleteUOMVariantClicked(sVariantId, sContextId, oFilterContext, oFilterData, oExtraData);
            }, function (oEvent) {
            });
      }
    },

    handleUOMVariantEditButtonClicked: function (sVariantId, sContext, sTableContextId) {
      _handleUOMVariantEditButtonClicked(sVariantId, sContext, sTableContextId);
    },

    handleUOMViewVersionActionItemClicked: function (sVariantId, sContextId) {
      _handleUOMViewVersionActionItemClicked(sVariantId, sContextId);
    },

    /** Variant View Filter Button Handlers**/

    handleUOMFilterButtonClicked: function (oExtraData, oFilterContext) {
      _handleUOMFilterButtonClicked(oExtraData, oFilterContext);
    },

    handleUOMChildFilterToggled: function (sParentId, sChildId, oExtraData) {
      _handleUOMChildFilterToggled(sParentId, sChildId, oExtraData);
    },

    handleUOMRemoveFilterGroupClicked: function (sFilterGroupId, oExtraData) {
      _handleUOMRemoveFilterGroupClicked(sFilterGroupId, oExtraData);
    },

    setCurrentDateForFilter: function (oContext) {
      _setCurrentDateForFilter(oContext);
    },

    handleUOMViewSortDataChanged: function (sContextId, oSortData) {
      _handleUOMViewSortDataChanged(sContextId, oSortData);
    },

    handleUOMViewPaginationDataChanged: function (sContextId, oPaginationData, oFilterContext) {
      _handleUOMViewPaginationDataChanged(sContextId, oPaginationData, oFilterContext);
    },

    /** Variant Dialog Handler's**/

    handleVariantDialogSaveClicked: function (sContextId, sEditContext) {
      _handleVariantDialogSaveClicked(sContextId, sEditContext);
    },

    handleVariantDialogCancelClicked: function () {
      _handleVariantDialogCancelClicked();
      _triggerChange();
    },

    handleVariantDialogChangeAttributeValue: function (oData) {
      _handleVariantDialogChangeAttributeValue(oData);
      _triggerChange();
    },

    /**Variant View Header Button Handler's**/

    handleVariantDiscardButtonClicked: function (sContextId) {
      _handleUOMTableDiscardButtonClicked(sContextId);
    },

    handleClearActivePopOverVariant: function () {
      UOMProps.setActivePopOverContextData({});
    },

    handleUOMSectionExpandToggle: function (sSectionId) {
      _handleUOMSectionExpandToggle(sSectionId);
    },

    handleUOMTableCreateRowButtonClicked: function (sContextId) {
      _createUOMVariant(sContextId);
    },

    /**Variant Value changed Handler's**/

    handleVariantCellValueChanged: function (oCellData, sTableContextId) {
      _handleVariantCellValueChanged(oCellData, sTableContextId);
    },

    handleVariantTagGroupDateValueChanged: function (sKey, sValue) {
      _handleVariantTagSummaryDateValueChanged(sKey, sValue);
    },

    handleVariantAddLinkedInstances: function (aEntities) {
      _handleVariantAddLinkedInstances(aEntities);
    },

    handleRemoveLinkedInstanceClicked: function (oViewData, aSelectedItems, sViewContext) {
      _handleGridViewRemoveLinkedInstanceClicked(oViewData, aSelectedItems, sViewContext);
    },

    handleUOMViewDateRangeApplied: function (oRangeData, sContextId) {
      _handleUOMViewDateRangeApplied(oRangeData, sContextId);
    },

    /** FullScreen Handler's **/
    handleUOMViewFullScreenButtonClicked: function (sContextId) {
      _handleUOMViewFullScreenButtonClicked(sContextId);
      _triggerChange();
    },

    handleUOMViewFullScreenCancelButtonClicked: function (sContextId) {
      _handleUOMViewFullScreenCancelButtonClicked(sContextId);
      _triggerChange();
    },

    /** Attribute Context Handler's**/
    handleAttributeContextViewShowVariantsClicked: function (sAttributeVariantContextId, sAttributeId, sVariantInstanceId, sParentContextId) {
      _handleAttributeContextViewShowVariantsClicked(sAttributeVariantContextId, sAttributeId, sVariantInstanceId, sParentContextId)
    },

    handleAttributeContextViewDialogButtonClick: function (sAttributeContextId, sButtonId) {
      _handleAttributeContextViewDialogButtonClick(sAttributeContextId, sButtonId);
    },

    removeAttributeVariantInstanceFromContent: function () {
      _removeAttributeVariantInstanceFromContent();
    },

    handleUOMFilterPopoverClosed: function (oExtraData) {
      _handleUOMFilterPopoverClosed(oExtraData);
    },

  }
})();

MicroEvent.mixin(VariantStore);

export default VariantStore;
