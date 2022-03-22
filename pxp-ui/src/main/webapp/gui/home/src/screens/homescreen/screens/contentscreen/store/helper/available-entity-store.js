/**
 * Created by CS80 on 6/15/2016.
 */
import CS from '../../../../../../libraries/cs';

import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import ScreenModeUtils from './screen-mode-utils';
import ContentUtils from './content-utils';
import FilterStoreFactory from './filter-store-factory';
import AvailableEntityProps from './../model/available-entity-view-props';
import ContentScreenConstants from './../model/content-screen-constants';
import ContentScreenViewContexts from '../../tack/content-screen-view-context-constants';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary';
import { communicator as HomeScreenCommunicator } from '../../../../store/home-screen-communicator';
import GlobalStore from '../../../../store/global-store';
import ContentScreenProps from './../model/content-screen-props';
import TableHeaderIdEntityMap from '../../tack/table-header-id-entity-map';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import BreadcrumbStore from '../../../../../../commonmodule/store/helper/breadcrumb-store';
import TableViewProps from "../model/table-view-props";
import ThumbnailModeConstants from "../../../../../../commonmodule/tack/thumbnail-mode-constants";
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

var getTranslation = ScreenModeUtils.getTranslationDictionary;

var AvailableEntityStore = (function () {

  var _triggerChange = function () {
    AvailableEntityStore.trigger('available-entity-change');
  };

  var _fetchAvailableEntities = function (oCallbackData, oExtraData) {
    let sSelectedContext = (oCallbackData && oCallbackData.selectedContext) || "";
    return _handleSearchApplyClicked(oCallbackData, oExtraData, {}, sSelectedContext);
  };

  var _getUrlAndDataForVariantLinkedInstanceQuicklist = function (oCommonPostDataForFilter) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oActiveEntity = ContentUtils.getActiveEntity();
    var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    var sSelectedEntity = oVariantSectionViewProps.getSelectedEntity();
    var bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
    if (bAvailableEntityChildVariantViewVisibilityStatus) {
      ContentUtils.fillIdsToExcludeForVariantQuicklist(oCommonPostDataForFilter);
    }

    var bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
    if (bAvailableEntityParentVariantViewVisibilityStatus) {
      var oSelectedContext = oVariantSectionViewProps.getSelectedContext();
      if (oSelectedContext) {
        oCommonPostDataForFilter.contextId = oSelectedContext.id;
      }
      oCommonPostDataForFilter.variantInstanceId = oActiveEntity.variantInstanceId;
      if (oActiveEntity.linkedInstances && oActiveEntity.linkedInstances[sSelectedEntity]) {
        oCommonPostDataForFilter.linkedInstances = CS.map(oActiveEntity.linkedInstances[sSelectedEntity], 'id');
      }
    }

    var sOpenedDialogTableContextId = ContentScreenProps.uomProps.getOpenedDialogTableContextId();
    var oOpenedDialogAttributeData = ContentScreenProps.uomProps.getOpenedDialogAttributeData();
    if (oOpenedDialogAttributeData.contextId || sOpenedDialogTableContextId || !CS.isEmpty(oActiveEntity.context)) {
      oCommonPostDataForFilter.contextId = oOpenedDialogAttributeData.contextId || sOpenedDialogTableContextId || oActiveEntity.context.contextId;
    }

    var oVariantQuicklistDataToReturn = {
      isForVariantQuicklist: bAvailableEntityChildVariantViewVisibilityStatus || bAvailableEntityParentVariantViewVisibilityStatus,
      url: RequestMapping.getRequestUrl(getRequestMapping().GetAllVariantEntitiesQuicklist),
      selectedEntity: sSelectedEntity
    };

    return oVariantQuicklistDataToReturn
  };

  let _fetchAvailableEntitiesForTableViewContext = function (oPaginationData, oFilterContext, oExtraData = {}) {
    let UOMProps = ContentScreenProps.uomProps;
    if(CS.isEmpty(oExtraData)) {
      let oActivePopOverContextData = UOMProps.getActivePopOverContextData();
      oExtraData.rowId = oActivePopOverContextData.contextInstanceId;
      oExtraData.sTableContextId = oActivePopOverContextData.contextId;
      oExtraData.columnId = oActivePopOverContextData.entity;
      oExtraData.idForProps = oActivePopOverContextData.idForProps;
    }

    let oActiveContent = ContentUtils.getActiveContent();
    let sTableContextId = oExtraData.sTableContextId;
    let sIdForProps = CS.isNotEmpty(oExtraData.idForProps) ? oExtraData.idForProps : sTableContextId;
    let oUOMTableProps = TableViewProps.getTableViewPropsByContext(sIdForProps, oActiveContent.id);
    let aBodyData = oUOMTableProps.getBodyData();
    let oRow = CS.find(aBodyData, {id: oExtraData.rowId});
    let sColumnId = oExtraData.columnId;
    let oCell = oRow.properties[sColumnId];
    let sSelectedEntity = TableHeaderIdEntityMap[oExtraData.columnId];
    ContentScreenProps.variantSectionViewProps.setSelectedEntity(sSelectedEntity);

    let oCommonPostDataForFilter = ContentUtils.getAllInstancesRequestData(oFilterContext);
    let oPostDataForFilter = CS.assign({}, oCommonPostDataForFilter);
    delete oPostDataForFilter.isFilterDataRequired;
    oPostDataForFilter.contextId = sTableContextId;
    oPostDataForFilter.idsToExclude = oCell.values;
    oPostDataForFilter = CS.omit(oPostDataForFilter, 'moduleId');
    oPostDataForFilter.instanceId = oExtraData.rowId;
    oPostDataForFilter.baseType = oActiveContent.baseType;
    oPostDataForFilter.moduleEntities = [sSelectedEntity];
    let oSpecialUsecaseFilterData = _handleSpecialUsecaseFilters({filterContext: oFilterContext});
    CS.isNotEmpty(oSpecialUsecaseFilterData) && CS.assign(oPostDataForFilter, oSpecialUsecaseFilterData);

    var oXRayData = ContentUtils.getXRayPostData();
    if (!CS.isEmpty(oXRayData)) {
      CS.assign(oPostDataForFilter, oXRayData);
    }

    let oCallbackData = {};
    let oAjaxExtraData = {
      URL: getRequestMapping().GetAllVariantEntitiesQuicklist,
      postData: oPostDataForFilter,
      requestData: {}
    };

    let sBreadcrumbLabel = sSelectedEntity;
    let sBreadcrumbId,sBreadcrumbType;
    sBreadcrumbType = sBreadcrumbId = BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST;
    let sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST;
    let aPayloadData = [oCallbackData, oAjaxExtraData];
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(sBreadcrumbId, sBreadcrumbLabel, sBreadcrumbType, sHelpScreenId, oFilterContext, oAjaxExtraData, "", aPayloadData, _fetchAvailableEntitiesForTableViewContextCall);
    oCallbackData.filterContext = oFilterContext;
    oCallbackData.paginationData = {
      from: oPostDataForFilter.from,
      size: oPostDataForFilter.size
    };

    UOMProps.setActivePopOverContextData({
      contextInstanceId: oExtraData.rowId,
      contextId: sTableContextId,
      entity: oExtraData.columnId,
      tableContextId: oExtraData.sTableContextId,
      idForProps: oExtraData.idForProps,
    });

    ContentUtils.fillIdsToExcludeForVariantQuicklist(oPostDataForFilter);
    return _fetchAvailableEntitiesForTableViewContextCall(oCallbackData, oAjaxExtraData);
  };

  var _fetchAvailableEntitiesForTableViewContextCall = function (oCallbackData, oAjaxExtraData) {
    return CS.customPostRequest(oAjaxExtraData.URL, JSON.stringify(oAjaxExtraData.postData),
        successSearchedContentListCallBack.bind(this, oCallbackData), failureSearchedContentListCallBack.bind(this, oCallbackData));
  };

  let _handleSpecialUsecaseFilters = function (oCallbackData) {
    let oFilterProps = ContentUtils.getFilterProps(oCallbackData.filterContext);
    let aAppliedFilterData = (oFilterProps.getIsFilterDirty() && CS.isNotEmpty(oFilterProps.getAppliedFiltersClone()))
                             ? oFilterProps.getAppliedFiltersClone() : oFilterProps.getAppliedFilters();
    let aFilterDataToProcess = ContentUtils.removeZeroValuesFromFilterTags(aAppliedFilterData);
    let aSpecialUseCaseFilters = [];
    let oContentScreenProps = ContentUtils.getComponentProps();
    let oScreenProps = oContentScreenProps.screen;
    oScreenProps.resetDataRuleFilterProps();

    CS.forEach(aFilterDataToProcess, function (oProperty) {
      if (oProperty.type == ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY) {
        let bExpiredAsset = false;
        let bNonExpiredAsset = false;
        if (!CS.isEmpty(oProperty.children)) {
          CS.forEach(oProperty.children, function (oChild) {
            switch (oChild.id) {
              case "expiredAsset":
                bExpiredAsset = true;
                break;
              case "nonExpiredAsset":
                bNonExpiredAsset = true;
                break;
            }
          });
          let aAssetExpiryStatus = bNonExpiredAsset && bExpiredAsset ? ["expired", "nonExpired"] : bExpiredAsset ? ["expired"] : ["nonExpired"];
          aSpecialUseCaseFilters.push({
            id: "assetExpiry",
            type: "assetExpiry",
            appliedValues: aAssetExpiryStatus
          })
        }
      } else if (oProperty.type == ContentScreenConstants.FILTER_FOR_DUPLICATE_ASSETS) {
        let bIsDuplicateAsset = false;
        CS.forEach(oProperty.children, function (oChild) {
          switch (oChild.id) {
            case "duplicateAssets":
              bIsDuplicateAsset = true;
              break;
          }
        });
        bIsDuplicateAsset && aSpecialUseCaseFilters.push({
          id: "duplicateAssets",
          type: "duplicateAssets",
          appliedValues: ["duplicate"]
        })
      }
      //----------------------------------- Handling of color voilation filter ----------------------------------------
      else if(oProperty.type && CS.includes(oProperty.type, "colorVoilation")) {
        let oDataRulesProps = oScreenProps.getDataRuleFilterProps();
        oDataRulesProps.all = false;
        oDataRulesProps.red = false;
        oDataRulesProps.orange = false;
        oDataRulesProps.yellow = false;
        oDataRulesProps.green = false;
        let oDataRuleFilter = {
          id: "colorVoilation",
          type: "colorVoilation",
          appliedValues: []
        };
        CS.forEach(oProperty.children, function (oChild) {
          switch (oChild.id) {
            case "red":
              oDataRulesProps.red = true;
              oDataRuleFilter.appliedValues.push("red");
              break;
            case "orange":
              oDataRulesProps.orange = true;
              oDataRuleFilter.appliedValues.push("orange");
              break;
            case "yellow":
              oDataRulesProps.yellow = true;
              oDataRuleFilter.appliedValues.push("yellow");
              break;
            case "green":
              oDataRulesProps.green = true;
              oDataRuleFilter.appliedValues.push("green");
              break;
          }
        });
        aSpecialUseCaseFilters.push(oDataRuleFilter);
      }
      delete oProperty.children;
    });

    oCallbackData.isSearchApplyClicked = true;

    return {
      specialUsecaseFilters: aSpecialUseCaseFilters
    }
  };

  let _getRelationshipQuickListURLByRelationship = function () {
    let sURL = getRequestMapping().RelationshipQuickList;
    return RequestMapping.getRequestUrl(sURL, {});
  };

  var _handleSearchApplyClicked = function (oCallbackData, oExtraData, oPaginationData, sSelectedContext) {
    let oFilterContext = oCallbackData.filterContext;
    let oComponentProps = ContentUtils.getComponentProps();
    let sAvailableEntityViewContext = AvailableEntityProps.getAvailableEntityViewContext();
    let oActiveEntity = ContentUtils.getActiveEntity();
    let bForTotalEntity = (ContentUtils.getIsStaticCollectionScreen() && !ContentUtils.getRelationshipViewStatus());
    let oAppData = ContentUtils.getAppData();
    let sContentScreenMode = ContentUtils.getScreenModeBasedOnActiveEntity();
    let sUrl = '';
    let sRelationshipSectionElementId = '';
    let sEntityId = oActiveEntity.id;
    let oURLData = {};
    let oScreenProps = ContentScreenProps.screen;

    if(bForTotalEntity) {
        sEntityId = ContentUtils.getTreeRootNodeId();
    }

    let sMode = "";
    let dummyPromise = Promise.resolve();
    let aContextList = [ContextTypeDictionary.CONTEXTUAL_VARIANT, ContextTypeDictionary.IMAGE_VARIANT];

    if (sAvailableEntityViewContext == ContentScreenViewContexts.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW) {
      return _fetchAvailableEntitiesForTableViewContext(oPaginationData, oFilterContext, oExtraData);
    } else if (!bForTotalEntity && oActiveEntity.globalPermission && !oActiveEntity.globalPermission.canEdit) {
      return dummyPromise;
    }

    var oPostDataForFilter = ContentUtils.getAllInstancesRequestData(oFilterContext);
    oPostDataForFilter.moduleId = ContentUtils.isCollectionScreen() ? "allmodule" : oPostDataForFilter.moduleId;
    let oSpecialUsecaseFilterData = _handleSpecialUsecaseFilters(oCallbackData);
    CS.isNotEmpty(oSpecialUsecaseFilterData) && CS.assign(oPostDataForFilter, oSpecialUsecaseFilterData);

    if(CS.isNotEmpty(oPaginationData)) {
      oPostDataForFilter.from = oPaginationData.from;
      oPostDataForFilter.size = oPaginationData.pageSize;
    }

    if (!CS.isEmpty(oExtraData)) {
      CS.assign(oPostDataForFilter, oExtraData);
    }

    var sBreadCrumbNodeLabel = "";
    var sBreadCrumbNodeId = BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST;
    var sBreadCrumbNodeType = BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST;
    var sBreadCrumbHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST;
    var sBreadCrumbNodeContext = "";

    let oBreadCrumbProps = ContentScreenProps.breadCrumbProps;
    let aBreadCrumbData = oBreadCrumbProps.getBreadCrumbData();
    let oActiveBreadcrumbNode = aBreadCrumbData[aBreadCrumbData.length - 1];
    if (oActiveBreadcrumbNode.type === BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST) {
      sSelectedContext = "contextEntity";
    }
    let sSectionInnerThumbnailMode = oScreenProps.getSectionInnerThumbnailMode();
    oPostDataForFilter.xrayEnabled = sSectionInnerThumbnailMode === ThumbnailModeConstants.XRAY;

    switch (sSelectedContext) {
      case "taxonomyHierarchy":
        sBreadCrumbNodeType = BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY;
        sBreadCrumbNodeContext = "taxonomyHierarchy";
        sBreadCrumbHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY;
        sBreadCrumbNodeId = BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY;
        if (oExtraData) {
          if (!CS.isEmpty(oExtraData.breadCrumbNodeLabel)) {
            sBreadCrumbNodeLabel = oExtraData.breadCrumbNodeLabel;
            delete oExtraData.breadCrumbNodeLabel;
          }
        }
        oCallbackData.selectedContext = sSelectedContext;
        sUrl = RequestMapping.getRequestUrl(getRequestMapping().GetAllInstances);
        break;

      case "staticCollection":  //open static collection
        let oActiveCollection = oComponentProps.collectionViewProps.getActiveCollection();
        sBreadCrumbNodeType = BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST;
        sBreadCrumbHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST;
        sBreadCrumbNodeLabel = getTranslation().ADD_TO_COLLECTION;
        oCallbackData.selectedContext = sSelectedContext;
        var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveCollection.type);
        sUrl = getRequestMapping(sScreenMode).GetStaticCollection;
        oPostDataForFilter.collectionId = oActiveCollection.id;
        oPostDataForFilter.isQuicklist = true;
        break;

      case "collectionHierarchy":
        var sToFetchCollectionId = oCallbackData && oCallbackData.toFetchCollectionId ? oCallbackData.toFetchCollectionId : oScreenProps.getActiveHierarchyNodeId();
        oPostDataForFilter.collectionId = sToFetchCollectionId;
        sScreenMode = ContentScreenConstants.entityModes.STATIC_COLLECTION_MODE;
        sUrl = getRequestMapping(sScreenMode).GetStaticCollection;
        sBreadCrumbNodeType = BreadCrumbModuleAndHelpScreenIdDictionary.COLLECTION_HIERARCHY;
        sBreadCrumbNodeId = "collectionHierarchy";
        sBreadCrumbNodeContext = "collectionHierarchy";
        sBreadCrumbHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.COLLECTION_HIERARCHY;
        if (oExtraData) {
          if (!CS.isEmpty(oExtraData.breadCrumbNodeLabel)) {
            sBreadCrumbNodeLabel = oExtraData.breadCrumbNodeLabel;
            delete oExtraData.breadCrumbNodeLabel;
          }
        }
        break;

      case "contextEntity":
        let oVariantInstanceQuicklistData = _getUrlAndDataForVariantLinkedInstanceQuicklist(oPostDataForFilter);
        sUrl = oVariantInstanceQuicklistData.url;
        let oReferencedVariantContexts = oComponentProps.screen.getReferencedVariantContexts();
        let oEmbeddedVariantContexts = CS.isNotEmpty(oReferencedVariantContexts) && oReferencedVariantContexts.embeddedVariantContexts;
        let oFoundVariantContext = CS.find(oEmbeddedVariantContexts, {id: oPostDataForFilter.contextId});
        let sVariantContextType = CS.isNotEmpty(oFoundVariantContext) && oFoundVariantContext.type;
        if (!CS.includes(aContextList, sVariantContextType)){
          oPostDataForFilter.baseType = oActiveEntity.baseType;
        }
        oPostDataForFilter.moduleEntities = [oVariantInstanceQuicklistData.selectedEntity];
        sBreadCrumbNodeLabel = oVariantInstanceQuicklistData.selectedEntity;
        sBreadCrumbHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST;
        sBreadCrumbNodeType = BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST;
        break;


      default:
        var oSectionSelectionStatus = oScreenProps.getSetSectionSelectionStatus();
        if (!oSectionSelectionStatus['relationshipContainerSelectionStatus']) {
          var bIsChildrenSectionSelected = oSectionSelectionStatus['childrenContainerSelectionStatus'];
          oComponentProps.screen.emptySetSectionSelectionStatus();
          oComponentProps.screen.resetSelectedContext();
          if (bIsChildrenSectionSelected) {
            oSectionSelectionStatus['childrenContainerSelectionStatus'] = true; // To Highlight Children in Article/Asset
            sUrl = RequestMapping.getRequestUrl(getRequestMapping(sContentScreenMode).ChildrenQuicklist, {});
            sMode = ContentScreenConstants.quicklistMode.CHILDREN;
          } else {
            oSectionSelectionStatus['linkedElementSelectionStatus'] = true;
            sUrl = RequestMapping.getRequestUrl(getRequestMapping(sContentScreenMode).LinkedQuickList, {});
          }
        } else {
          sRelationshipSectionElementId = oSectionSelectionStatus['selectedRelationship'].id;
          oCallbackData.relationshipId = oSectionSelectionStatus['selectedRelationship'].relationshipId;;
          sBreadCrumbNodeId = sRelationshipSectionElementId;
          var oRelationshipToolbarProps = oComponentProps.relationshipView.getRelationshipToolbarProps();
          oComponentProps.relationshipView.emptySelectedRelationshipElements();
          if (oRelationshipToolbarProps[sRelationshipSectionElementId]) {
            sUrl = _getRelationshipQuickListURLByRelationship();
          } else {
            oSectionSelectionStatus.relationshipContainerSelectionStatus = false;
            oSectionSelectionStatus.selectedRelationship = {};
            oAppData.setAvailableEntities([]);
            return dummyPromise;
          }
          var oReferencedRelationshipsProperties = oComponentProps.screen.getReferencedRelationshipProperties();
          let sFoundRelationshipId = ContentUtils.getRelationshipIdFromSideId(sRelationshipSectionElementId);
          var oReferencedRelationship = oReferencedRelationshipsProperties[sFoundRelationshipId];
          sBreadCrumbNodeLabel = oReferencedRelationship ? CS.getLabelOrCode(oReferencedRelationship) : "";
        }
        oCallbackData.getDefaultTypesCallback = ContentUtils.getContentStore().handleModuleCreateButtonClicked.bind(this, "", "relationshipEntity", oFilterContext, false);
    }

    if (sUrl) {
      let sOpenedDialogTableContextId = oComponentProps.uomProps.getOpenedDialogTableContextId();
      let oReferencedVariantContexts = oComponentProps.screen.getReferencedVariantContexts();
      let oEmbeddedVariantContexts = CS.isNotEmpty(oReferencedVariantContexts) && oReferencedVariantContexts.embeddedVariantContexts;
      let oFoundVariantContext = CS.find(oEmbeddedVariantContexts, {id: sOpenedDialogTableContextId});
      let sVariantContextType = CS.isNotEmpty(oFoundVariantContext) && oFoundVariantContext.type;
      if (!CS.includes(aContextList, sVariantContextType)){
        oPostDataForFilter.instanceId = sEntityId;
      }

      ContentUtils.getAdditionalDataForRelationshipCalls(oPostDataForFilter, oCallbackData);

      var oXRayData = ContentUtils.getXRayPostData();
      let sCurrentThumbnailMode = oScreenProps.getSectionInnerThumbnailMode();
      if (!CS.isEmpty(oXRayData) && sCurrentThumbnailMode === ThumbnailModeConstants.XRAY) {
        CS.assign(oPostDataForFilter, oXRayData);
      }
      let bIsHierarchyMode = CS.isNotEmpty(ContentUtils.getSelectedHierarchyContext());
      let bIsTaxonomyHierarchySelected = oComponentProps.screen.getIsTaxonomyHierarchySelected();
      if (bIsTaxonomyHierarchySelected && bIsHierarchyMode) {
        let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
        var oActiveHierarchyTaxonomy = oFilterProps.getTaxonomySettingIconClickedNode();
        var sOuterHierarchyNodeId = oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId();
        oPostDataForFilter.isSearchOnSelectedTaxonomy = true;
        if (sOuterHierarchyNodeId === "-1") {
          oPostDataForFilter.selectedTypes = [oActiveHierarchyTaxonomy.id];
          oPostDataForFilter.selectedTaxonomyIds = [];
          oPostDataForFilter.clickedTaxonomyId = "";
          oPostDataForFilter.parentTaxonomyId = "";
        } else if (sOuterHierarchyNodeId !== ContentUtils.getHierarchyDummyNodeId()) {
          oPostDataForFilter.selectedTaxonomyIds = [oActiveHierarchyTaxonomy.id];
          oPostDataForFilter.clickedTaxonomyId = sOuterHierarchyNodeId;
          oPostDataForFilter.parentTaxonomyId = sOuterHierarchyNodeId;
          oPostDataForFilter.selectedTypes = []
        }
      }

      let oAjaxExtraData = {
        URL: sUrl,
        postData: oPostDataForFilter,
        requestData: oURLData,
      };

     let aPayloadData = [oCallbackData, oAjaxExtraData];

     /**BreadCrumb node data creation nad preparation */
      oCallbackData = oCallbackData || {};
      oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(sBreadCrumbNodeId, "", sBreadCrumbNodeType, sBreadCrumbHelpScreenId, oFilterContext, oAjaxExtraData, "", aPayloadData, _handleSearchApplyClickedCall);
      oCallbackData.paginationData = {
        from: oPostDataForFilter.from,
        size: oPostDataForFilter.size
      };

      if (!CS.isEmpty(sBreadCrumbNodeLabel)) {
        oCallbackData.breadCrumbData.label = sBreadCrumbNodeLabel;
      }
      if (!CS.isEmpty(sBreadCrumbNodeContext)) {
        oCallbackData.breadCrumbData.context = sBreadCrumbNodeContext;
      }

      return _handleSearchApplyClickedCall(oCallbackData, oAjaxExtraData);
    } else {
      _triggerChange();
      return dummyPromise;
    }

  };

  var _handleSearchApplyClickedCall = function (oCallbackData, oAjaxExtraData) {
    return CS.customPostRequest(oAjaxExtraData.URL, JSON.stringify(oAjaxExtraData.postData),
        successSearchedContentListCallBack.bind(this, oCallbackData), failureSearchedContentListCallBack.bind(this, oCallbackData), null, null, oAjaxExtraData.requestData);
  };

  var successSearchedContentListCallBack = function (oCallbackData, oResponse) {
    var oAppData = ContentUtils.getAppData();
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    let oFilterContext = oCallbackData.filterContext;
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);

    var bRelationshipView = ContentUtils.getRelationshipViewStatus();
    var bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();
    var oResponseData = oResponse.success;
    var aEntityListFromServer = oResponseData.children;
    var iTotalEntityCount = oResponseData.totalContents;
    var aClassTaxonomyTreeData = [];//oResponseData.categoryInfo;
    oComponentProps.screen.setQuickListDragNDropState(false);
    var iFrom = oResponseData.from;
    oComponentProps.availableEntityViewProps.setSelectedEntities([]);
    if (bRelationshipView || bIsTaxonomyHierarchySelected) {
      aEntityListFromServer = oResponseData.children;
    }

    if (bIsTaxonomyHierarchySelected) {
      var oActiveHierarchyTaxonomy = oFilterProps.getTaxonomySettingIconClickedNode();
      if (oActiveHierarchyTaxonomy.id == "-1") {
        aEntityListFromServer = [];
      } else if (oActiveHierarchyTaxonomy.id == ContentUtils.getHierarchyDummyNodeId()) {
        var oHierarchyTree = oScreenProps.getContentHierarchyTree();
        var oDummyNode = CS.find(oHierarchyTree.children, {id: oActiveHierarchyTaxonomy.id});
        oDummyNode.count = oResponseData.totalContents;
      }
    }

    /**
     * Set pagination data
     */
    let oPaginationData = oCallbackData && oCallbackData.paginationData || {};
    oFilterProps.setPaginationSize(oPaginationData.size);
    oFilterProps.setTotalItemCount(iTotalEntityCount);
    oFilterProps.setFromValue(iFrom);
    oFilterProps.setCurrentPageItems(aEntityListFromServer.length);

    ContentUtils.addEntityInformationData(aEntityListFromServer);
    oAppData.setAvailableEntities(aEntityListFromServer);

    let bIsFilterInfoRequired = oFilterProps.getIsFilterInformationRequired();
    if (bIsFilterInfoRequired) {
      oFilterStore.prepareAndUpdateFilterAndSortModel(oResponseData, oCallbackData, true);
    } else {
      CS.forEach(oResponseData.appliedSortData, function (oSortData) {
        let oProperty = CS.find(oResponseData.referencedAttributes, {code:oSortData.sortField});
        oSortData.iconKey = oProperty.iconKey;
      });
      let oFilterInfo = {
        sortData: oResponseData.appliedSortData,
        filterData: oFilterProps.getAvailableFilters()
      };
      oFilterStore.setFilterInfo(oFilterInfo);
    }
    ContentUtils.setFilterProps(oFilterStore.getFilterInfo(), false, oFilterContext);
    ContentUtils.addDataToReferencedKlasses(oResponseData.referencedKlasses);
    let oReferencedTags = oScreenProps.getReferencedTags();
    let oReferencedAttributes = oScreenProps.getReferencedAttributes();
    CS.assign(oReferencedTags, oResponseData.referencedTags);
    CS.assign(oReferencedAttributes, oResponseData.referencedAttributes);
    let oConfigDetails = {
      referencedAttributes: oReferencedAttributes,
      referencedTags: oReferencedTags
    };
    ContentUtils.setLoadedPropertiesFromConfigDetails(oConfigDetails);


    if (oCallbackData && oCallbackData.checkAndShowAllNodes) {
    }

    if (oCallbackData) {
      if (oCallbackData.checkDefaultKlassTaxonomy) {
        oCallbackData.checkDefaultKlassTaxonomy(aClassTaxonomyTreeData);
      }
      if (oCallbackData.functionToExecuteForCollectionHierarchy) {
        oCallbackData.functionToExecuteForCollectionHierarchy(oResponse);
      }
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    }

    BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);

    /**Set view mode as a tile mode in quicklist view**/
    let sSelectedContext = oCallbackData.selectedContext;
    let aContextListToShowTileMode = ["contextEntity", "staticCollection",
                                      ContentScreenConstants.sectionTypes.SECTION_TYPE_REFERENCE,
                                      ContentScreenConstants.sectionTypes.SECTION_TYPE_RELATIONSHIP,
                                      ContentScreenConstants.sectionTypes.SECTION_TYPE_NATURE_RELATIONSHIP];
    if(CS.isEmpty(sSelectedContext) || CS.includes(aContextListToShowTileMode, sSelectedContext)) {
      oScreenProps.setViewMode(ContentScreenConstants.viewModes.TILE_MODE);
    }

    if (sSelectedContext == "taxonomyHierarchy" || sSelectedContext == "collectionHierarchy") {
      HomeScreenCommunicator.disablePhysicalCatalog(true);
    }
    if (oResponseData.referencedAssets) {
      ContentUtils.getContentStore().preProcessAndSetReferencedAssets(oResponseData.referencedAssets);
    }

    oCallbackData.getDefaultTypesCallback && oCallbackData.getDefaultTypesCallback();
    _triggerChange();

    return true;
  };

  var failureSearchedContentListCallBack = function (oCallbackData, oResponse) {
    if (oResponse.failure && oResponse.failure.exceptionDetails[0].key === "IllegalArgumentException") {
      //TODO: remove this if block after backend handling is complete -shashasnk
      return;
    }
    else if (oResponse.failure && oResponse.failure.exceptionDetails[0].key === "StaticCollectionNotFoundException" && oCallbackData.functionHandleAfterEffectsOfStaticCollectionNotFound) {
      oCallbackData.functionHandleAfterEffectsOfStaticCollectionNotFound();
    }

    ContentUtils.failureCallback(oResponse, 'failureSearchedContentListCallBack', getTranslation());

    return false;
  };

  var _handleSearchedEntityLoadMoreClicked = function (oPaginationData, oCallbackData) {
    let sSelectedContext = (oCallbackData && oCallbackData.selectedContext) || "";
    _handleSearchApplyClicked(oCallbackData, null, oPaginationData, sSelectedContext);
  };

  var _handleSectionClicked = function (sActiveSetId, sContext, oSelectionStatus, sRelationshipSectionId) {
    var oComponentProps = ContentUtils.getComponentProps();
    var iSize = oComponentProps.screen.getDefaultPaginationLimit();
    var iStartIndex = 0;
    var sSearchedText = AvailableEntityProps.getSearchText();
    var oFilterProps = oComponentProps.screen.getContentAvailableEntityFilterProps();
    var oCurrentUser = GlobalStore.getCurrentUser();
    var oEntity = ContentUtils.getActiveEntity();
    if (CS.isEmpty(oEntity)) {
      return;
    }
    if (oEntity.globalPermission) {
      return;
    }

    var sContentScreenMode = ContentUtils.getScreenModeBasedOnActiveEntity();
    var sUrl = '';
    var oSectionSelectionStatus = oComponentProps.screen.getSetSectionSelectionStatus();
    oComponentProps.screen.resetSelectedContext();
    var sMode = "";
    var aTagsWithNoZeroValues = ContentUtils.removeZeroValuesFromFilterTags(oFilterProps.selectedTags);

    //TODO: check for errors after merge!! 02/08/2016
    if (CS.isEmpty(sRelationshipSectionId)) {
      if (oSectionSelectionStatus['childrenContainerSelectionStatus']) {
        oSectionSelectionStatus['childrenContainerSelectionStatus'] = true; // To Highlight Children in Article/Asset
        sUrl = RequestMapping.getRequestUrl(getRequestMapping(sContentScreenMode).ChildrenQuicklist, {});
        sMode = ContentScreenConstants.quicklistMode.CHILDREN;
      } else {
        oSectionSelectionStatus['linkedElementSelectionStatus'] = true;
        sUrl = RequestMapping.getRequestUrl(getRequestMapping(sContentScreenMode).LinkedQuickList, {});
      }
    } else {
      sUrl = RequestMapping.getRequestUrl(getRequestMapping(sContentScreenMode).RelationshipQuickList, {});
    }
    var oActiveEntity = ContentUtils.getActiveEntity();
    var oAttributeAndNumeric = ContentUtils.getIsAttributeAndIsNumeric(oFilterProps.sortField);

    var oPostDataForFilter = {
      size: iSize,
      from: iStartIndex,
      searchString: sSearchedText,
      sortField: oFilterProps.sortField,
      sortOrder: oFilterProps.sortOrder,
      currentUserId: oCurrentUser.id,
      id: sActiveSetId,
      isAttribute: oAttributeAndNumeric.isAttribute,
      isNumeric: oAttributeAndNumeric.isNumeric,
      attributes: oFilterProps.selectedAttributes,
      tags: aTagsWithNoZeroValues,
      allSearch: sSearchedText,
      selectedRoles: oFilterProps.selectedRoles,
      selectedTypes: oFilterProps.selectedTypes,
      baseType: oActiveEntity.baseType,
      mode: sMode
    };
    if (!CS.isEmpty(sRelationshipSectionId)) {
      let sRelationshipId = ContentUtils.getRelationshipIdFromSideId(sRelationshipSectionId);

      oPostDataForFilter.sideId = sRelationshipSectionId;
      oPostDataForFilter.relationshipId = sRelationshipId;
      var oReferencedRelationshipsMapping = oComponentProps.screen.getReferencedRelationshipsMapping();
      if (CS.isEmpty(oReferencedRelationshipsMapping[sRelationshipId])) {
        return
      }
      oPostDataForFilter.typeKlassId = oReferencedRelationshipsMapping[sRelationshipId];
    }
    var sShowMoreContext = "";

    CS.postRequest(sUrl, {}, oPostDataForFilter, successSectionClickedCallBack.bind(this, sShowMoreContext, sContext, oSelectionStatus), failureSectionClickedCallBack);
  };

  let _toggleRightPanelSelectAllEntities = function (aEntitiesToSelect) {
    let aCurrentSelectedEntities = AvailableEntityProps.getSelectedRightPanelEntities();
    if (aCurrentSelectedEntities.length !== aEntitiesToSelect.length) {
      let aNewSelectedEntities = [];
      CS.forEach(aEntitiesToSelect, function (oEntity) {
        let sEntityName = ContentUtils.getContentName(oEntity);
        aNewSelectedEntities.push({
          id: oEntity.id,
          name: sEntityName || oEntity.label,
          type: oEntity.baseType
        })
      });
      AvailableEntityProps.setSelectedRightPanelEntities(aNewSelectedEntities);
    } else {
      AvailableEntityProps.setSelectedRightPanelEntities([]);
    }
  };

  let _removeFromRightPanelSelectedEntities = function (aEntitiesIdsToRemove) {
    let aSelectedEntities = AvailableEntityProps.getSelectedRightPanelEntities();
    CS.forEach(aEntitiesIdsToRemove, function (sEntityId) {
      CS.remove(aSelectedEntities, {id: sEntityId});
    })
  };

  var successSectionClickedCallBack = function (sShowMoreContext, sContext, oSelectionStatus, oResponse) {
    var oAppData = ContentUtils.getAppData();
    oSelectionStatus[sContext] = true;
    if (sShowMoreContext == "show_more") {
      var aAvailableEntities = oAppData.getAvailableEntities();
      var aNewAvailableEntities = CS.concat(aAvailableEntities, oResponse.success);
      oAppData.setAvailableEntities(aNewAvailableEntities);
    } else {
      oAppData.setAvailableEntities(oResponse.success);
    }
    _triggerChange();
  };

  var failureSectionClickedCallBack = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureSectionClickedCallBack', getTranslation());
  };

  return {
    fetchAvailableEntities: function (oCallbackData, oExtraData) {
      return _fetchAvailableEntities(oCallbackData, oExtraData);
    },

    handleThumbnailClicked: function (oModel, bClickWithControl, bIsRightPanelThumb) {
      var sId = oModel.id;
      var oContent = oModel.properties['content'] || oModel.properties['entity'];
      var sName = ContentUtils.getContentName(oContent);
      var sBaseType = oContent.baseType;//oModel.properties['entityType'];
      let fGetSelectedEntities = bIsRightPanelThumb ? AvailableEntityProps.getSelectedRightPanelEntities : AvailableEntityProps.getSelectedEntities;
      let fSetSelectedEntities = bIsRightPanelThumb ? AvailableEntityProps.setSelectedRightPanelEntities : AvailableEntityProps.setSelectedEntities;
      var oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();

      if (!bClickWithControl) {
        var oSelectedEntity = CS.find(fGetSelectedEntities(), {id: sId});
        var aResetEntity = (oSelectedEntity) ? [oSelectedEntity] : [];
        fSetSelectedEntities(aResetEntity);
      }
      var aSelectedEntities = fGetSelectedEntities();
      var iIndex = CS.findIndex(aSelectedEntities, {id: sId});
      let oRelationshipContext = oRelationshipContextData.context;
      /** in case of linked variant if the allowed duplicate is off then user can select one content at a time */
      if (!CS.isEmpty(oRelationshipContext) && oRelationshipContext.type == ContextTypeDictionary.PRODUCT_VARIANT && !oRelationshipContext.isDuplicateVariantAllowed) {
        if (iIndex == -1) {
          aSelectedEntities = [{
            id: sId, type: sBaseType, name: sName
          }]
        } else {
          aSelectedEntities = [];
        }
        AvailableEntityProps.setSelectedEntities(aSelectedEntities)
      } else {
        if (iIndex == -1) {
            aSelectedEntities.push({id: sId, type: sBaseType, name: sName});
        } else {
          aSelectedEntities.splice(iIndex, 1);
        }
      }
    },

    handleSearchApplyClicked: function (oCallBackData, oExtraData, sSelectedHierarchyContext) {
      return _handleSearchApplyClicked(oCallBackData, oExtraData, {}, sSelectedHierarchyContext);
    },

    handleSearchedEntityLoadMoreClicked: function (oPaginationData, oCallbackData) {
      _handleSearchedEntityLoadMoreClicked(oPaginationData, oCallbackData);
    },

    handleSectionClicked: function (sActiveSetId, sContext, oSelectionStatus, sRelationshipSectionId, oRelationshipSide) {
      _handleSectionClicked(sActiveSetId, sContext, oSelectionStatus, sRelationshipSectionId, oRelationshipSide);
    },

    toggleRightPanelSelectAllEntities: function (aEntitiesToSelect) {
      _toggleRightPanelSelectAllEntities(aEntitiesToSelect);
      _triggerChange();
    },

    removeFromRightPanelSelectedEntities: function (aEntities) {
      _removeFromRightPanelSelectedEntities(aEntities);
    },

    handleSpecialUsecaseFilters: function (oCallBackData) {
      return _handleSpecialUsecaseFilters(oCallBackData);
    },

  }
})();

MicroEvent.mixin(AvailableEntityStore);
export default AvailableEntityStore;
