import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ScreenModeUtils from './screen-mode-utils';
import TagTypeConstants from './../../../../../../commonmodule/tack/tag-type-constants';
import MockDataForEntityBaseTypesDictionary from './../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import MSSViewConstants from './../../../../../../commonmodule/tack/mss-view-constants';
import PhysicalCatalogDictionary from '../../../../../../commonmodule/tack/physical-catalog-dictionary';
import ModuleDictionary from '../../../../../../commonmodule/tack/module-dictionary';
import ContentScreenConstants from './../model/content-screen-constants';
import ContentScreenViewContextConstants from '../../tack/content-screen-view-context-constants';
import TableHeaderIdEntityMap from '../../tack/table-header-id-entity-map';
import HierarchyTypesDictionary from '../../../../../../commonmodule/tack/hierarchy-types-dictionary';
import FilterUtils from './filter-utils';
import ContentUtils from './content-utils';
import CollectionViewProps from '../../store/model/viewprops/collection-view-props';
import EntityBaseTypeDictionary from './../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import FilterStoreFactory from './filter-store-factory';
import TaxonomyBaseTypeDictionary from './../../../../../../commonmodule/tack/mock-data-for-taxonomy-base-types-dictionary';
import AdditionalFilterSkeleton from '../../tack/mock/additional-filter-skeleton';
import {gridViewPropertyTypes as oGridViewPropertyTypes} from "../../../../../../viewlibraries/tack/view-library-constants";
import UUIDGenerator from "../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator";
import ContextTypeDictionary from "../../../../../../commonmodule/tack/context-type-dictionary";
import ContextualAllCategoriesProps from "../model/contextual-all-categories-selector-view-props";
import CategoriesConstantDictionary from "../../../../../../commonmodule/tack/categories-constant-dictionary";
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
const getTranslation = ScreenModeUtils.getTranslationDictionary;

const NewFilterStore = function (oContext) {
  this.screenContext = oContext.screenContext;
  this.filterType = oContext.filterType;
};

/**
 *Please do not add arrow functions here(reference issue).
 */


  let oPrototype = NewFilterStore.prototype;
  oPrototype.triggerChange = function () {
    oPrototype.trigger('filter-change');
  };

  oPrototype.failureGenericFunction = function(sCallingFailureFunctionName, oResponse) {
    ContentUtils.failureCallback(oResponse, sCallingFailureFunctionName, getTranslation());
  };

  oPrototype.failureFetchSortAndFilterData = function(sCallingFailureFunctionName, oResponse) {
    ContentUtils.failureCallback(oResponse, sCallingFailureFunctionName, getTranslation());
  };

  oPrototype.successGetAddClassDropDownListInHierarchyCallback = function (oCallback, oResponse) {
    let aResponse = oResponse.success;
    let oData = {
      nodeId: oCallback.nodeId,
      list: aResponse
    };
    let oFilterProps = this.getFilterProps();
    oFilterProps.setInnerAddClassDropDownData(oData);

    this.triggerChange();
  };

  oPrototype.prepareModelDataForFilters = function (oFilter, oCallback, oExtraData, bIsUpdate) {
    let sScreen = "";
    let sRequestUrl = getRequestMapping(sScreen).GetFilterChildren;

    oFilter.filterViewType = oGridViewPropertyTypes.LAZY_MSS;
    oFilter.requestResponseInfo = {
      requestType: "custom",
      responsePath: ["success", "filterChildren"],
      requestURL: sRequestUrl,
      keysToRemove: ["isCustomPaginationOnLoadMore", "customPaginateOnLoadModel"],
      customRequestModel: {
        id: oExtraData.id || oFilter.id,
        filterType: oExtraData.filterType || oFilter.propertyType,
        isCustomPaginationOnLoadMore: true,
        customPaginateOnLoadModel: {
          from: 0,
          size: 0
        },
        isArchivePortal: ContentUtils.getIsArchive()
      }
    };

    if(ContentUtils.isAttributeTypeNumber(oFilter.type) || ContentUtils.isAttributeTypeMeasurement(oFilter.type)
    || ContentUtils.isAttributeTypeDate(oFilter.type)) {
      oFilter.requestResponseInfo.responsePostProcessingFunc = this.processFilterChildrens.bind(this, oFilter.type);
    }
    oFilter = this.modifyFilterByScreenContext(oFilter, oCallback.filterContext.screenContext);
    let oAdditionalRequestData = {};
    let AvailableEntityStore = ContentUtils.getAvailableEntityStore();
    let aAppliedFilterData = this.getFilterProps().getAppliedFilters();
    let oGetAllInstanceData = this.createGetAllInstancesData();
    /**
     *Below code:- Remove applied filter data from attributes(Post data) for same filter.
     */
    CS.forEach(aAppliedFilterData, function (oAppliedFilterData) {
      if (oAppliedFilterData.id === oFilter.id) {
        CS.remove(oGetAllInstanceData.attributes, function (oAttribute) {
          if (oFilter.id === oAttribute.id) {
            return true;
          }
        });
      }
    });

    delete oGetAllInstanceData.from;
    delete oGetAllInstanceData.size;
    let oSpecialUsecaseFilterData = AvailableEntityStore.handleSpecialUsecaseFilters({filterContext: oCallback.filterContext});
    ContentUtils.getAdditionalDataForRelationshipCalls(oAdditionalRequestData);
    CS.assign(oFilter.requestResponseInfo.customRequestModel, oGetAllInstanceData);
    CS.assign(oFilter.requestResponseInfo.customRequestModel, oSpecialUsecaseFilterData);
    CS.assign(oFilter.requestResponseInfo.customRequestModel, oAdditionalRequestData);
    bIsUpdate && (oFilter.key = UUIDGenerator.generateUUID());
    oFilter.hideButtons = false;
  };

  oPrototype.processFilterChildrens = function (sAttributeType, oResponse) {
    let aFilterChildren = oResponse.filterChildren;
    let oReferencedProperty = oResponse.referencedProperty;
    CS.forEach(aFilterChildren, function (oChild) {
      if (ContentUtils.isAttributeTypeDate(sAttributeType)) {
        FilterUtils.getDateRangeLabel(oChild);
      } else if (ContentUtils.isAttributeTypeNumber(sAttributeType) || ContentUtils.isAttributeTypeMeasurement(sAttributeType)) {
        oChild.hideSeparator = oReferencedProperty.hideSeparator;
        FilterUtils.getNumberAccordingToNumberFormat(oChild);
      }
    });
  };

  oPrototype.fillInstanceIdAndBaseTypeForContext = function (oPostData){
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oComponentProps = ContentUtils.getComponentProps();
    let oActivePopOverContextData = oComponentProps.uomProps.getActivePopOverContextData();
    let sContextId = oComponentProps.uomProps.getOpenedDialogTableContextId() || oActivePopOverContextData.tableContextId;
    let oReferencedVariantContexts = oComponentProps.screen.getReferencedVariantContexts();
    let oEmbeddedVariantContexts = CS.isNotEmpty(oReferencedVariantContexts) && oReferencedVariantContexts.embeddedVariantContexts;
    let oFoundVariantContext = CS.find(oEmbeddedVariantContexts, {id: sContextId});
    let sVariantContextType = CS.isNotEmpty(oFoundVariantContext) && oFoundVariantContext.type;
    let aContextList = [ContextTypeDictionary.CONTEXTUAL_VARIANT, ContextTypeDictionary.IMAGE_VARIANT];
    if (!CS.includes(aContextList, sVariantContextType)){
      oPostData.instanceId = oActiveEntity.id;
      oPostData.baseType = oActiveEntity.baseType;
    } else {
      if (CS.isNotEmpty(oActivePopOverContextData)) {
        oPostData.instanceId = oActivePopOverContextData.contextInstanceId;
        oPostData.baseType = oActiveEntity.baseType;
      }
    }
  };

  oPrototype.modifyFilterByScreenContext = function (oFilter, sScreenContext) {
    let oComponentProps = ContentUtils.getComponentProps();
    let bIsForTableContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
    if (ContentUtils.getIsStaticCollectionScreen()) {
      oFilter.requestResponseInfo.requestURL = getRequestMapping().GetFilterChildrenForCollection;
      oFilter.requestResponseInfo.customRequestModel.collectionId = CollectionViewProps.getActiveCollection().id;
      if (ContentUtils.getAvailableEntityViewStatus()) {
        oFilter.requestResponseInfo.customRequestModel.isQuicklist = true;
      }
    } else if (sScreenContext === "quickList") {
      if (ContentUtils.getIsVariantQuicklist() || bIsForTableContext) {
        let oActivePopOverContextData = oComponentProps.uomProps.getActivePopOverContextData();
        ContentUtils.fillIdsToExcludeForVariantQuicklist(oFilter.requestResponseInfo.customRequestModel);
        oFilter.requestResponseInfo.requestURL = getRequestMapping().GetFilterChildrenForVariantQuicklist;
        oFilter.requestResponseInfo.customRequestModel.moduleEntities = bIsForTableContext ? [TableHeaderIdEntityMap[oActivePopOverContextData.entity]]
                                                                                           : [oComponentProps.variantSectionViewProps.getSelectedEntity()];
        this.fillInstanceIdAndBaseTypeForContext(oFilter.requestResponseInfo.customRequestModel);
      } else {
        oFilter.requestResponseInfo.requestURL = getRequestMapping().GetFilterChildrenForRelationshipQuickList;
      }
    } else if (sScreenContext === "matchAndMerge") {
      oFilter.requestResponseInfo.requestURL = getRequestMapping().GetFilterChildrenForGoldenRecord;
    }
    return oFilter;
  };

  oPrototype.prepareAndUpdateFilterAndSortModel = function (oFilterInfo, oCallback, bIsUpdate) {
    let oFilterData = oFilterInfo.filterData;
    let oRecommendedAssetsFilterData = this.getFilterProps().getFiltersForRecommendedAssets();
    if (CS.isNotEmpty(oRecommendedAssetsFilterData)) {
      let oExtraData = {
        id: "recommendedAssets",
        filterType: "recommendedAssets"
      };
      this.prepareModelDataForFilters(oRecommendedAssetsFilterData, oCallback, oExtraData, bIsUpdate);
    }
    CS.forEach(oFilterData, (oFilter) => {
      this.prepareModelDataForFilters(oFilter, oCallback, {}, bIsUpdate);
    });

    let oDataRuleFilterData = CS.find(oFilterInfo.filterData, {id: "colorVoilation"});
    if(!CS.isEmpty(oDataRuleFilterData)){
      oDataRuleFilterData.label = getTranslation().RULE_VIOLATIONS;
      CS.forEach(oDataRuleFilterData.children, (oDataRule)=> {
        oDataRule.label = getTranslation()[oDataRule.label];
      })
    }

    if (bIsUpdate) {
      let oFilterDataToUpdate = this.getFilterInfo();
      oFilterDataToUpdate.filterData = oFilterInfo.filterData;
      oFilterDataToUpdate.sortData = oFilterInfo.appliedSortData;
      return;
    }
    this.setFilterInfo(oFilterInfo);
  };

  oPrototype.successFetchSortAndFilterData = function (oCallback = {}, oResponse) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setShowDetails(!oFilterProps.getShowDetails());
    let oFilterInfo = oResponse.success;
    this.prepareAndUpdateFilterAndSortModel(oFilterInfo, oCallback);
    /**Required to update applied filters tag values **/
    this.updateTagDataFromAppliedFilters(oFilterInfo.referencedTags);
    ContentUtils.setLoadedPropertiesFromConfigDetails({referencedTags: oFilterInfo.referencedTags, referencedAttributes: oFilterInfo.referencedAttributes});
    oFilterProps.setIsFilterDirty(false);
    ContentUtils.setFilterProps(this.getFilterInfo(), oCallback.preventResetFilterProps, oCallback.filterContext);

    if (CS.isNotEmpty(oFilterInfo.referencedKlasses)) {
      oFilterProps.setReferencedClasses(oFilterInfo.referencedKlasses);
    }
    if (CS.isNotEmpty(oFilterInfo.referencedTaxonomies)) {
      oFilterProps.setReferencedTaxonomies(oFilterInfo.referencedTaxonomies);
    }
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    this.triggerChange();
  };

  oPrototype.getKlassTreeNodeById = function (iId, aNodes) {
    let oFilterProps = this.getFilterProps();
    aNodes = aNodes || oFilterProps.getTaxonomyTree();
    let oFound = null;
    let _this = this;

    CS.forEach(aNodes, function (oItem) {
      if (oItem.id == iId) {
        oFound = oItem;
        return false;
      } else if (!oFound && oItem.children && oItem.children.length) {
        oFound = _this.getKlassTreeNodeById(iId, oItem.children);
      }
    });

    if (oFound) {
      return oFound;
    } else {
      return null;
    }
  };

  oPrototype.assignToAllNodesBelow = function (oObjToAssign, oNode, oVisualProps, aAllChildNodeIds) {
    let oNodeVisualProp = oVisualProps[oNode.id] || {};
    CS.assign(oNodeVisualProp, oObjToAssign);
    oVisualProps[oNode.id] = oNodeVisualProp;
    let _this = this;

    CS.forEach(oNode.children, function (oChild) {
      if(aAllChildNodeIds){
        aAllChildNodeIds.push(oChild.id);
      }
      _this.assignToAllNodesBelow(oObjToAssign, oChild, oVisualProps, aAllChildNodeIds)
    });
  };

  oPrototype.clearAllAppliedFilter = function () {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setAppliedFilters([]);
  };

  oPrototype.getDefaultAppliedChildFilterData = function (oChild, sType, sFilterBaseType, bIsTag) {
    let iFrom = oChild.from;
    let iTo = oChild.to;
    if(bIsTag){
      iFrom = 100;
      iTo = 100;
    }

    return {
      "id": oChild.id,
      "type": sType,
      "baseType": sFilterBaseType,
      "from": iFrom,
      "to": iTo,
      "label": CS.getLabelOrCode(oChild),
      "hideSeparator": oChild.hideSeparator,
      "iconKey": oChild.iconKey
    }
  };

  oPrototype.getDefaultAppliedFilterData = function (oParent) {

    return {
      "id": oParent.id,
      "children": [],
      "type": oParent.type,
      "label": oParent.label,
      "code": oParent.code,
      "isHiddenInAdvancedFilters": oParent.isHiddenInAdvancedFilters,
      "defaultUnit": oParent.defaultUnit,
      "iconKey": oParent.iconKey
    }
  };

  oPrototype.removeFilterByGroupId = function (sFilterGroupId, oExtraData) {
    let oFilterProps = this.getFilterProps(oExtraData);
    let aAppliedFilters = oFilterProps.getAppliedFilters();
    CS.remove(aAppliedFilters, {id: sFilterGroupId});
  };

  oPrototype.getAvailableFiltersByParentId = function (sParentId, oFilterProps) {
    switch (sParentId) {
      case ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY:
      case ContentScreenConstants.FILTER_FOR_DUPLICATE_ASSETS:
        return oFilterProps.getAdditionalFiltersForAsset();
      default:
        return oFilterProps.getAvailableFilters();
    }
  };

  oPrototype.handleChildFilterToggled = function (sParentId, sChildId, bIsSingleSelect, oExtraData) {
    this.ChildFilterToggled(sParentId, sChildId, bIsSingleSelect, oExtraData);

    if(!bIsSingleSelect){
      this.triggerChange();
    }

  };

  oPrototype.handleApplyLazyFilter = function (oFilterData) {
    let oSelectedFilterData = oFilterData.selectedFilterData;
    let oExtraData = oSelectedFilterData.extraData;
    let oFilterProps = this.getFilterProps(oExtraData);
    let sParentId = oFilterData.parentId;
    let aAvailableFilters = this.getAvailableFiltersByParentId(sParentId, oFilterProps);
    let oParentNode = CS.find(aAvailableFilters, {id: sParentId});
    let aAppliedFilters = this.getAppliedFiltersClone(oExtraData);
    let oAppliedFilter = CS.find(aAppliedFilters, {id: sParentId});
    let aRangeTypeAttributes = ContentUtils.getAllNumericTypeAttributes();
    let aSelectedIds = oFilterData.selectedIds;
    let aAppliedChildFilters = [];
    if (CS.isEmpty(oAppliedFilter)) {
      oAppliedFilter = this.getDefaultAppliedFilterData(oParentNode);
      aAppliedFilters.push(oAppliedFilter);
    }

    CS.forEach(aSelectedIds, (sId) => {
      let oChild = CS.find(oAppliedFilter.children, {id: sId});
      if (CS.isEmpty(oChild)) {
        oChild = CS.find(oFilterData.referencedData, {id: sId});
        let sType = "";
        let sFilterBaseType = "";
        let bIsTag = ContentUtils.isTag(oParentNode.type);
        if (CS.includes(aRangeTypeAttributes, oParentNode.type) || bIsTag) {
          sType = "range";
          sFilterBaseType = "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel"
        } else {
          sType = "exact";
          sFilterBaseType = "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel"
        }
        oChild = this.getDefaultAppliedChildFilterData(oChild, sType, sFilterBaseType, bIsTag);
      }
      aAppliedChildFilters.push(oChild);
    });

    if (CS.isEqual(oAppliedFilter.children, aAppliedChildFilters)) {
      oFilterProps.clearAppliedFiltersClone();
      oFilterProps.setIsFilterDirty(false);
    }
    oAppliedFilter.children = aAppliedChildFilters;
  };

  /**If trigger is not required, call this function directly instead of handleChildFilterToggled function */
  oPrototype.ChildFilterToggled = function (sParentId, sChildId, bIsSingleSelect, oExtraData) {
    let aRangeTypeAttributes = ContentUtils.getAllNumericTypeAttributes();

    let oFilterProps = this.getFilterProps(oExtraData);
    let aAvailableFilters = this.getAvailableFiltersByParentId(sParentId, oFilterProps);
    let oParentNode = CS.find(aAvailableFilters, {id: sParentId});
    let aAppliedFilters = this.getAppliedFiltersClone(oExtraData);

    let oAppliedFilter = CS.find(aAppliedFilters, {id: sParentId});
    if (CS.isEmpty(oAppliedFilter)) {
      oAppliedFilter = this.getDefaultAppliedFilterData(oParentNode);
      aAppliedFilters.push(oAppliedFilter);
    }

    let bForceRemove = false;
    if(bIsSingleSelect){
      bForceRemove = !!CS.find(oAppliedFilter.children, {id: sChildId});
      oAppliedFilter.children = [];
    }

    let oAppliedChildFilter = CS.remove(oAppliedFilter.children, {id: sChildId});
    if (CS.isEmpty(oAppliedChildFilter) && !bForceRemove) {
      let oChild = CS.find(oParentNode.children, {id: sChildId});
      let sType = "";
      let sFilterBaseType = "";
      let bIsTag = ContentUtils.isTag(oParentNode.type);
      if (CS.includes(aRangeTypeAttributes, oParentNode.type) || bIsTag) {
        sType = "range";
        sFilterBaseType = "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel"
      } else {
        sType = "exact";
        sFilterBaseType = "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel"
      }

      oAppliedFilter.children.push(this.getDefaultAppliedChildFilterData(oChild, sType, sFilterBaseType, bIsTag));
    } else {
      if (CS.isEmpty(oAppliedFilter.children)) {
        CS.remove(aAppliedFilters, {id: oAppliedFilter.id});
      }
    }
  };

  oPrototype.addExpandAndCollapsePropertyToTaxonomyTree = function (aTaxonomyTree, iLevel, oNewTreeProps, iCheckedStatus, bUseParentStatus) {
    let oFilterProps = this.getFilterProps();
    let oTaxonomyVisualProps = oFilterProps.getTaxonomyVisualProps();
    iLevel = iLevel ? ++iLevel : 1;
    let _this = this;

    CS.forEach(aTaxonomyTree, function (oTaxonomy) {
      if (!oTaxonomy.children) {
        oTaxonomy.children = [];
      }
      if (!CS.isEmpty(oTaxonomy.children)) {
        _this.addExpandAndCollapsePropertyToTaxonomyTree(oTaxonomy.children, iLevel, oNewTreeProps, iCheckedStatus, bUseParentStatus);
      }
      oNewTreeProps[oTaxonomy.id] = oTaxonomyVisualProps[oTaxonomy.id];
      if (!oTaxonomyVisualProps[oTaxonomy.id]) {
        oNewTreeProps[oTaxonomy.id] = {
          isChecked: (bUseParentStatus && iCheckedStatus === 2 && 2) || 0,
          isExpanded: false,
          isHidden: false
        };
      } else {
        if(bUseParentStatus) {
          oNewTreeProps[oTaxonomy.id].isChecked = iCheckedStatus > 0 ? 2 : 0;
        }
      }

      if(iLevel == 1) {
        oNewTreeProps[oTaxonomy.id].isHidden = false;
      }

    });
  };

oPrototype.updateNodeStateByChildInfo = function (oTreeNodeVisualProp, oChildInfo ) {
  if (oChildInfo.isAllChildChecked) {
    oTreeNodeVisualProp.isChecked = 2;
  }
  else if (oChildInfo.isAnyChildChecked) {
    oTreeNodeVisualProp.isChecked = 1;
  }
  else {
    oTreeNodeVisualProp.isChecked = 0
  }
};

oPrototype.createVisualPropsData = function (oTreeVisualProps, aTaxonomyTree, aSelectedNodeIds) {
  let _this = this;
  let bAllChildChecked = true;
  let bAnyChildChecked = false;

  CS.forEach(aTaxonomyTree, function (oTaxonomy) {
    let oTreeNodeVisualProp = {};
    if (CS.includes(aSelectedNodeIds, oTaxonomy.id)) {
      bAnyChildChecked = true;
      oTreeNodeVisualProp = {
        isChecked: 2,
        isHidden: false
      };
      _this.assignToAllNodesBelow({isChecked: 2, isHidden: false}, oTaxonomy, oTreeVisualProps);
    }
    else {
      let oChildInfo = _this.createVisualPropsData(oTreeVisualProps, oTaxonomy.children, aSelectedNodeIds);
      _this.updateNodeStateByChildInfo(oTreeNodeVisualProp, oChildInfo);
      bAllChildChecked = false;
      bAnyChildChecked = bAnyChildChecked || oChildInfo.isAnyChildChecked; // To check inner child is checked
    }
    oTreeVisualProps[oTaxonomy.id] = oTreeNodeVisualProp;
  });
  bAllChildChecked = bAllChildChecked && CS.isNotEmpty(aTaxonomyTree);

  return {
    isAnyChildChecked: bAnyChildChecked,
    isAllChildChecked: bAllChildChecked
  };
};

  oPrototype.resetClassTaxonomyProperty = function (oNewTreeProps, aDefaultTaxonomyTree) {
    let sParentTaxonomyId =this.getFilterProps().getSelectedOuterParentId();
    let bIsClassTaxonomy = sParentTaxonomyId == "" || sParentTaxonomyId == "-1";
    if(bIsClassTaxonomy) {
      let oClassTaxonomy = CS.find(aDefaultTaxonomyTree, {id: "-1"});
      if (oClassTaxonomy) {
        let bIsAllChecked = true;
        let bIsAnyChecked = false;

        let iCheckedStatus = 0;
        CS.forEach(oClassTaxonomy.children, function (oTaxonomy) {
          let oVisualProps = oNewTreeProps[oTaxonomy.id];
          if (oVisualProps.isChecked == 0) {
            bIsAnyChecked = bIsAnyChecked || false;
            bIsAllChecked = false;
          } else if (oVisualProps.isChecked == 1) {
            bIsAnyChecked = true;
            bIsAllChecked = false;
            return false;
          } else if (oVisualProps.isChecked == 2) {
            bIsAnyChecked = true;
            bIsAllChecked = bIsAllChecked && true;
          }
        });

        if (bIsAllChecked) {
          iCheckedStatus = 2;
        } else if (bIsAnyChecked) {
          iCheckedStatus = 1;
        }
        let oVisualProps = oNewTreeProps[oClassTaxonomy.id];
        oVisualProps.isChecked = iCheckedStatus;

        if(iCheckedStatus == 0) {
          oVisualProps.isChecked = 2;
          this.checkAndShowAllNodes(oClassTaxonomy.id);
        }
      }
    }
  };

  oPrototype.handleSubmitFilterSearchText = function (sSearchText) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setSearchText(sSearchText);
  };

  oPrototype.getSelectedCategories = function (aAvailableTaxonomy) {
    let oFilterProps = this.getFilterProps();
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();
    let aCheckedTaxonomy = [];
    let _this = this;
    CS.forEach(aAvailableTaxonomy, function (oTaxonomy) {
      let oTaxonomyVisualProp = oVisualProps[oTaxonomy.id] || {};
      if (oTaxonomyVisualProp.isChecked == 2 && oTaxonomy.id != "-1") {
        aCheckedTaxonomy.push(oTaxonomy.id);
      }

      if (!CS.isEmpty(oTaxonomy.children)) {
        aCheckedTaxonomy.push.apply(aCheckedTaxonomy, _this.getSelectedCategories(oTaxonomy.children));
      }
    });

    return aCheckedTaxonomy;
  };

  oPrototype.getDefaultAttributeChildData = function (bIsAdvancedFilterClicked, oAttr) {
    let sValue = oAttr.label;
    let sType = "exact";
    let bIsAdvancedFilter = false;

    if (oAttr.advancedSearchFilter) {
      sValue = oAttr.value;
      sType = oAttr.type;
      bIsAdvancedFilter = true;
    } else if (bIsAdvancedFilterClicked) {
      sType = oAttr.type;
      bIsAdvancedFilter = true;
    }
    return {
      "id": oAttr.id,
      "type": sType,
      "baseType": "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel",
      "value": sValue,
      "defaultUnit": "",
      "advancedSearchFilter": bIsAdvancedFilter
    }
  };

  oPrototype.getDefaultNumberAttributeChildData = function (bIsAdvancedFilterClicked, oAttr) {
    let sType = "range";
    let bIsAdvancedFilter = false;

    if (oAttr.advancedSearchFilter || bIsAdvancedFilterClicked) {
      sType = oAttr.type;
      bIsAdvancedFilter = true;
    }
    return {
      "id": oAttr.id,
      "type": sType,
      "baseType": "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel",
      "to": oAttr.to,
      "from": oAttr.from,
      "defaultUnit": "",
      "advancedSearchFilter": bIsAdvancedFilter
    }
  };

  oPrototype.makeDefaultAttributeFilterData = function (oAppliedFilter, bIsAdvancedFilterClicked) {
    let _this = this;
    let aChildrenModel = [];
    let aRangeTypeAttributes = ContentUtils.getAllNumericTypeAttributes();

    if (oAppliedFilter.advancedSearchFilter || bIsAdvancedFilterClicked) {
      CS.forEach(oAppliedFilter.children, function (oChildFilter) {
        if (oChildFilter.type == "range") {
          aChildrenModel.push(_this.getDefaultNumberAttributeChildData(bIsAdvancedFilterClicked, oChildFilter));
        } else {
          aChildrenModel.push(_this.getDefaultAttributeChildData(bIsAdvancedFilterClicked, oChildFilter));
        }
      });
    } else {
      if (CS.includes(aRangeTypeAttributes, oAppliedFilter.type)) {
        aChildrenModel = CS.map(oAppliedFilter.children, this.getDefaultNumberAttributeChildData.bind(_this, bIsAdvancedFilterClicked))
      } else {
        aChildrenModel = CS.map(oAppliedFilter.children, this.getDefaultAttributeChildData.bind(_this, bIsAdvancedFilterClicked))
      }
    }

    let bIsAdvancedFilter = oAppliedFilter.advancedSearchFilter || bIsAdvancedFilterClicked || false;
    return {
      "id": oAppliedFilter.id,
      "type": oAppliedFilter.type,
      "mandatory": aChildrenModel,
      "should": [],
      "label": oAppliedFilter.label,
      "advancedSearchFilter": bIsAdvancedFilter
    };

  };

  oPrototype.makeFilterDataForAppliedAttributeAndTags = function (aAppliedFilterData, bIsAdvancedFilterClicked) {
    let aFilterDataToProcess = ContentUtils.removeZeroValuesFromFilterTags(aAppliedFilterData);
    let aAppliedTags = [];
    let aAppliedAttributes = [];
    let oContentScreenProps = ContentUtils.getComponentProps();
    let oScreenProps = oContentScreenProps.screen;
    oScreenProps.resetDataRuleFilterProps();
    let _this = this;

    CS.forEach(aFilterDataToProcess, function (oProperty) {
      if (ContentUtils.isTag(oProperty.type)) {
        aAppliedTags.push(oProperty);
        oProperty.mandatory = oProperty.children;
      }
      else if(oProperty.type && CS.includes(oProperty.type.toLowerCase(), "attribute")){
        aAppliedAttributes.push(_this.makeDefaultAttributeFilterData(oProperty, bIsAdvancedFilterClicked));
      }
      delete oProperty.children;
    });

    return {
      tags: aAppliedTags,
      attributes: aAppliedAttributes,
    }
  };

  oPrototype.getTaxonomyTreeData = function () {
    let oContentScreenProps = ContentUtils.getComponentProps();
    let oScreenProps = oContentScreenProps.screen;
    let oFilterProps = this.getFilterProps();
    let sSelectedParentId = oFilterProps.getSelectedOuterParentId();
    let sSelectedOuterParentTaxonomyId = sSelectedParentId == "-1" ? "" : sSelectedParentId;
    let sClickedTaxonomyId = CS.isEmpty(sSelectedOuterParentTaxonomyId) ? null : sSelectedOuterParentTaxonomyId;
    let aSelectedParentTaxonomyIds = oFilterProps.getSelectedParentTaxonomyIds();
    if (sSelectedOuterParentTaxonomyId && CS.isEmpty(aSelectedParentTaxonomyIds)) {
      aSelectedParentTaxonomyIds = [sSelectedOuterParentTaxonomyId];
    }
    let aSelectedCategories = oFilterProps.getSelectedTypes();

    /**Required for Taxonomy Hierarchy */
    let sRootNodeId = "-1";
    let bIsHierarchyMode = CS.isNotEmpty(ContentUtils.getSelectedHierarchyContext());
    let bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();
    if (bIsTaxonomyHierarchySelected && bIsHierarchyMode) {
      let oActiveHierarchyTaxonomy = oFilterProps.getTaxonomySettingIconClickedNode();
      let sOuterHierarchyNodeId = oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId();
      if (sOuterHierarchyNodeId === sRootNodeId) {
        if(oActiveHierarchyTaxonomy.id !== sRootNodeId && oActiveHierarchyTaxonomy.id !== ContentUtils.getHierarchyDummyNodeId()){
          aSelectedCategories = [oActiveHierarchyTaxonomy.id];
        }
      } else if (sOuterHierarchyNodeId !== ContentUtils.getHierarchyDummyNodeId()) {
        aSelectedParentTaxonomyIds = [oActiveHierarchyTaxonomy.id];
      }
    }

    return {
      selectedTypes: aSelectedCategories,
      selectedTaxonomyIds: aSelectedParentTaxonomyIds,
      clickedTaxonomyId: sClickedTaxonomyId
    }
  };

  oPrototype.getCategoriesVsSelectedIds = function (bIsForApplyCategories) {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(
        this.screenContext);
    let aSummaryViewTreeData = contextualAllCategoriesProps.getSummaryTreeData();
    aSummaryViewTreeData = aSummaryViewTreeData.clonedObject || aSummaryViewTreeData;
    let oFilterProps = this.getFilterProps();
    let oReferencedClasses = {};
    let oReferencedTaxonomies = {};
    let oSelectedIds = {
      natureClasses: [],
      attributionClasses: [],
      taxonomies: [],
    };

    CS.forEach(aSummaryViewTreeData, function (aTreeData, key) {
      let aSelectedIds = [];
      CS.forEach(aTreeData, function (oTreeData) {
        aSelectedIds.push(oTreeData.id);
        if (key === CategoriesConstantDictionary.NATURE_CLASSES ||
            key === CategoriesConstantDictionary.ATTRIBUTION_CLASSES) {
          oReferencedClasses[oTreeData.id] = oTreeData;
        }
        else if (key === CategoriesConstantDictionary.TAXONOMIES) {
          oReferencedTaxonomies[oTreeData.id] = oTreeData;
        }
      });
      oSelectedIds[key] = aSelectedIds;
    });
    bIsForApplyCategories && oFilterProps.setReferencedClasses(oReferencedClasses);
    bIsForApplyCategories && oFilterProps.setReferencedTaxonomies(oReferencedTaxonomies);
    return oSelectedIds;
  };

  oPrototype.createGetAllInstancesData = function () {
    let oFilterProps = this.getFilterProps();
    let bIsAdvancedFilterClicked = oFilterProps.getIsAdvanceSearchFilterClickedStatus();
    let oAppliedFilterData = oFilterProps.getIsFilterDirty() && (CS.isNotEmpty(oFilterProps.getAppliedFiltersClone()) || bIsAdvancedFilterClicked)
        ? oFilterProps.getAppliedFiltersClone() : oFilterProps.getAppliedFilters();
    let oAppliesAttrAndTags = this.makeFilterDataForAppliedAttributeAndTags(oAppliedFilterData, bIsAdvancedFilterClicked);
    FilterUtils.setOldSearchText({screenContext: this.screenContext, filterType: this.filterType}); //set the current search text as old search text in props
    this.updateAppliedFilterDataFromClone();

    let aSelectedIdsForClasses = oFilterProps.getSelectedTypes();
    let aSelectedIdsForTaxonomies = oFilterProps.getSelectedTaxonomyIds();

    let oTaxonomyTreeData = this.getTaxonomyTreeData();
    let aSortOptions = [];
    let oSortDetails = oFilterProps.getActiveSortDetails();
    CS.forEach(oFilterProps.getAvailableSortData(), function (oSortData) {
      let sSortField = oSortData.sortField;
      let oSortDetail = oSortDetails[sSortField];
      if (!oSortDetail.sortOrder) {
        return;
      }
      let sSortOrder = oSortDetail.sortOrder;
      let bIsNumeric = false;
      if (sSortField != ContentScreenConstants.relevance) {
        bIsNumeric = ContentUtils.isAttributeTypeNumeric(oSortData.type);
      }
      aSortOptions.push({
        sortField: sSortField,
        sortOrder: sSortOrder,
        isNumeric: bIsNumeric
      });
    });

    let bIsAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
    if (bIsAvailableEntityViewStatus && CS.isEmpty(aSortOptions)) {
      aSortOptions.push({
        sortField: ContentScreenConstants.lastModifiedAttribute,
        sortOrder: "desc",
        isNumeric: false
      });
    }
    let aSelectedModules = ContentUtils.getSelectedModulesForFilter();

    return {
      attributes: oAppliesAttrAndTags.attributes,
      tags: oAppliesAttrAndTags.tags,
      allSearch: oFilterProps.getSearchText(),
      size: oFilterProps.getPaginationSize(),
      from: oFilterProps.getFromValue(),
      sortOptions: aSortOptions,
      selectedTypes: aSelectedIdsForClasses,
      selectedTaxonomyIds: aSelectedIdsForTaxonomies,
      moduleId: aSelectedModules,
      clickedTaxonomyId: oTaxonomyTreeData.clickedTaxonomyId,
      isFilterDataRequired: oFilterProps.getIsFilterInformationRequired()
    }
  };

  oPrototype.resetPaginationOnApplyingFilter = function () {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setFromValue(0);
  };

  oPrototype.createFilterPostData = function () {
    let oContentScreenProps = ContentUtils.getComponentProps();
    let oScreenProps = oContentScreenProps.screen;
    let oFilterProps = this.getFilterProps();
    let aSelectedIds = [];
    let aTaxonomyTree = oFilterProps.getTaxonomyTree();
    let oDataIntegrationInfo = ContentUtils.getDataIntegrationInfo();
    let oCurrentUser = ContentUtils.getCurrentUser();

    oFilterProps.setSelectedParentTaxonomyIds(aSelectedIds);
    this.updateAppliedFilterDataFromClone();
    FilterUtils.setOldSearchText({screenContext: this.screenContext, filterType: this.filterType}); //set the current search text as old search text in props
    let oTaxonomyVisualProps = oFilterProps.getTaxonomyVisualProps();
    let iSize = oFilterProps.getPaginationSize();
    let iStartIndex = oFilterProps.getFromValue();
    let oArticleProps = oContentScreenProps.articleViewProps;
    let oArticleFolderVisibility = oArticleProps.getArticleFolderVisibility();
    let aSelectedParentTaxonomyIds = oFilterProps.getSelectedParentTaxonomyIds();
    let sSelectedParentId = oFilterProps.getSelectedOuterParentId();

    let aSortOptions = [];
    let oSortDetails = oFilterProps.getActiveSortDetails();
    CS.forEach(oFilterProps.getAvailableSortData(), function (oSortData) {
      let sSortField = oSortData.sortField;
      let oSortDetail = oSortDetails[sSortField];
      if (!oSortDetail.sortOrder) {
        return;
      }
      let sSortOrder = oSortDetail.sortOrder;
      let bIsNumeric = false;
      if (sSortField != ContentScreenConstants.relevance) {
        bIsNumeric = ContentUtils.isAttributeTypeNumeric(oSortData.type);
      }
      aSortOptions.push({
        sortField: sSortField,
        sortOrder: sSortOrder,
        isNumeric: bIsNumeric
      });
    });

    let bIsAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
    if(bIsAvailableEntityViewStatus && CS.isEmpty(aSortOptions)){
      aSortOptions.push({
        sortField: ContentScreenConstants.lastModifiedAttribute,
        sortOrder: "desc",
        isNumeric: false
      });
    }

    let aSelectedCategories = [];
    let sRootNodeId = "-1";
    if(sSelectedParentId == sRootNodeId || sSelectedParentId == ''){
      if (oTaxonomyVisualProps[sRootNodeId] && oTaxonomyVisualProps[sRootNodeId].isChecked == 1) {
        if (aTaxonomyTree[0] && aTaxonomyTree[0].children) {
          aSelectedCategories = this.getSelectedCategories(aTaxonomyTree[0].children);
        }
      }
    }

    let sSelectedOuterParentTaxonomyId = sSelectedParentId == "-1" ? "" : sSelectedParentId;
    let bIsAdvancedFilterClicked = oFilterProps.getIsAdvanceSearchFilterClickedStatus();
    let oAppliesAttrAndTags = this.makeFilterDataForAppliedAttributeAndTags(oFilterProps.getAppliedFilters(), bIsAdvancedFilterClicked);
    let aSelectedModules = ContentUtils.getSelectedModulesForFilter();

    if(sSelectedOuterParentTaxonomyId == ContentUtils.getHierarchyDummyNodeId()){
      sSelectedOuterParentTaxonomyId = "";
    }

    if(sSelectedOuterParentTaxonomyId && CS.isEmpty(aSelectedParentTaxonomyIds)) {
      aSelectedParentTaxonomyIds = [sSelectedOuterParentTaxonomyId];
    }

    let sEndPoint =  oDataIntegrationInfo.endPoint == "null" ? null : oDataIntegrationInfo.endPoint;
    let oDateRuleFilterProps = oScreenProps.getDataRuleFilterProps();
    let sPhysicalCatalogId = oDataIntegrationInfo.physicalCatalogId;
    if(sPhysicalCatalogId === PhysicalCatalogDictionary.PIM_ARCHIVAL){
      sPhysicalCatalogId = PhysicalCatalogDictionary.PIM;
    }
    let oAssetExpiryStatus = oAppliesAttrAndTags.assetExpiryStatus;

    let bIsKPIExplorerOpen = oScreenProps.getIsKpiContentExplorerOpen();
    bIsKPIExplorerOpen && (aSelectedCategories = oFilterProps.getSelectedTypes());

    let oRequestData = {
      attributes: oAppliesAttrAndTags.attributes,
      tags: oAppliesAttrAndTags.tags,
      allSearch: oFilterProps.getSearchText(),
      size: iSize,
      from: iStartIndex,
      sortOptions: aSortOptions,
      getFolders: oArticleFolderVisibility.showFolders,
      getLeaves: oArticleFolderVisibility.showArticles,
      selectedRoles: oAppliesAttrAndTags.roles,
      selectedTypes: aSelectedCategories,
      selectedTaxonomyIds: aSelectedParentTaxonomyIds,
      parentTaxonomyId: sSelectedOuterParentTaxonomyId,//To be removed in future.
      clickedTaxonomyId: sSelectedOuterParentTaxonomyId,
      moduleId: aSelectedModules,
      isRed: oDateRuleFilterProps.red,
      isOrange: oDateRuleFilterProps.orange,
      isYellow: oDateRuleFilterProps.yellow,
      isGreen: oDateRuleFilterProps.green,
      logicalCatalogId: "",
      systemId: "",
      physicalCatalogId: sPhysicalCatalogId,
      organizationId: oCurrentUser.organizationId,
      endpointId: sEndPoint
    };

    if (CS.isNotEmpty(oAssetExpiryStatus)) {
      oRequestData.expiryStatus = oAssetExpiryStatus.expiryStatus;
    }
    if (oAppliesAttrAndTags.isScopeFilterSeletcted) {
      oRequestData.isScopeFilterSeletcted = true;
    }
    return oRequestData;
  };

  oPrototype.handleFilterSortOrderToggled = function (sId) {
    let oFilterProps = this.getFilterProps();
    let oActiveSortDetails = oFilterProps.getActiveSortDetails();
    let oActiveSort = oActiveSortDetails[sId];
    if (oActiveSort) {
      /**** We Added Attribute Type check because only in the case of createdOn attribute. we have to follow
       *  Toggle Sequence "Desc + asc". but if click on some other attribute then we have to follow
       *  "Asc + desc + neutral" (In neutral case sort jumped to createdOn attribute.) .******/
      oActiveSort.sortOrder = oActiveSort.sortOrder === "asc" ? "desc" : "asc";
    }
  };

  oPrototype.handleFilterSortDeactivatedItemClicked = function (sId, bMultiSelect) {
    let oFilterProps = this.getFilterProps();
    let oActiveSortDetails = oFilterProps.getActiveSortDetails();
    let oActiveSort = oActiveSortDetails[sId];
    if (!bMultiSelect) {
      CS.forEach(oActiveSortDetails, function (oSort, sKey) {
        if (sKey != sId) {
          oSort.sortOrder = "";
        }
      });
    }
    if (oActiveSort) {
      oActiveSort.sortOrder = oActiveSort.sortOrder === "asc" ? "desc" : "asc";
    }
  };

  oPrototype.handleFilterSortActivatedItemClicked = function (sId) {
    let oFilterProps = this.getFilterProps();
    let oActiveSortDetails = oFilterProps.getActiveSortDetails();
    let oActiveSort = oActiveSortDetails[sId];
    if (oActiveSort) {
      oActiveSort.sortOrder = "";
    }

    this.triggerChange();
  };

  oPrototype.removeInValidAppliedFilterData = function () {
    let oFilterProps = this.getFilterProps();
    let bAdvanceSearchFilterApply = oFilterProps.getIsAdvanceSearchFilterClickedStatus();
    let aAppliedFilterData = oFilterProps.getAppliedFilters();
    oFilterProps.setAdvancedFilterAppliedStatus(false);

    CS.remove(aAppliedFilterData, function (oAppliedFilter) {
      if (oAppliedFilter.id == ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY) {
        return false;
      }
      if (oAppliedFilter.id == ContentScreenConstants.FILTER_FOR_DUPLICATE_ASSETS) {
        return false;
      }
      if(oAppliedFilter.advancedSearchFilter){
        oFilterProps.setAdvancedFilterAppliedStatus(true);
        return false;
      }

    });

    if(bAdvanceSearchFilterApply){
      !CS.isEmpty(aAppliedFilterData) && oFilterProps.setAdvancedFilterAppliedStatus(true);
      oFilterProps.setIsAdvanceSearchFilterClickedStatus(false);
    }
  };

  oPrototype.cleanAvailableFilterProps = function (aFilterData) {
    let _this = this;
    CS.forEach(aFilterData, function (oFilterData) {
      if(oFilterData.children) {
        _this.cleanAvailableFilterProps(oFilterData.children);
      }
      delete oFilterData.isHidden;
    });
  };

oPrototype.setFilterSortData = function (aSortData) {
  let oNewActiveSortData = {};
  let oFilterProps = this.getFilterProps();
  let aExistingSortList = oFilterProps.getAvailableSortData();
  let oActiveSortDetails = oFilterProps.getActiveSortDetails();

  //Fill new sort data in existing list and replace sort order of selected sort detail in existing sort list.
  CS.forEach(aSortData, function (oSortData) {
    let oExistingItem = CS.find(aExistingSortList, {sortField: oSortData.sortField});
    if (CS.isEmpty(oExistingItem)) {
      aExistingSortList.push(oSortData);
    } else {
      let oActiveSortDetail = oActiveSortDetails[oSortData.sortField];
      if(CS.isNotEmpty(oActiveSortDetail.sortOrder) && oActiveSortDetail.sortField === oExistingItem.sortField){
        oExistingItem.sortOrder = oActiveSortDetail.sortOrder;
      } else {
        oExistingItem.sortOrder = oSortData.sortOrder;
      }
    }
  });

  //Fill sort data list by sort id and fill selected sort details.
  CS.forEach(aExistingSortList, function (oExistingSortListItem) {
    oNewActiveSortData[oExistingSortListItem.sortField] = oExistingSortListItem;
  });

  //Set available sort data(sorted).
  oFilterProps.setAvailableSortData(aExistingSortList);
  oFilterProps.setActiveSortDetails(oNewActiveSortData);
};

  oPrototype.setFilterProps = function (oFilterInfo, bIsPreventResetFilterProps, aSelectedFilters) {
    let oFilterProps = this.getFilterProps();
    let oComponentProps = ContentUtils.getComponentProps();
    let aFilterData = oFilterInfo.filterData || [];
    let aSortData = oFilterInfo.sortData ? oFilterInfo.sortData : [];
    let bIsForTableContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
    let bRelationshipView = ContentUtils.getRelationshipViewStatus() || ContentUtils.getIsVariantQuicklist()
        || bIsForTableContext;
    let aEntityList = bRelationshipView ? ContentUtils.getAppData().getAvailableEntities() : ContentUtils.getEntityList();
    this.cleanAvailableFilterProps(aFilterData);
    let oAdditionalFiltersForAsset = AdditionalFilterSkeleton.assetAdditionalFilters();
    let bIsAssetPresent = false;
    /**
     * Required to show asset expiry filter when selected module id is 'dammodule'.
     */
    let aAdditionalFiltersForAsset = oFilterProps.getAdditionalFiltersForAsset();
    if (ContentUtils.getIsStaticCollectionScreen()) {
      CS.forEach(aEntityList, function (oEntity) {
        if (oEntity.baseType === EntityBaseTypeDictionary.assetBaseType) {
          bIsAssetPresent = true;
          return false;
        }
      });
    } else {
      bIsAssetPresent = !CS.isEmpty(aEntityList) && aEntityList[0].baseType === EntityBaseTypeDictionary.assetBaseType;
    }
    if (ContentUtils.getSelectedModuleId() === ModuleDictionary.MAM) {
      if (bIsAssetPresent) {
        oFilterProps.setAdditionalFiltersForAsset && oFilterProps.setAdditionalFiltersForAsset(oAdditionalFiltersForAsset);
      } else if (CS.isNotEmpty(aEntityList) && !CS.includes(aAdditionalFiltersForAsset, {id: ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY})) {
        let oExpiredAssetData = CS.find(oAdditionalFiltersForAsset, {id: ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY});
        CS.isEmpty(aAdditionalFiltersForAsset) ? oFilterProps.getAdditionalFiltersForAsset().push(oExpiredAssetData)
                                               : oFilterProps.setAdditionalFiltersForAsset([oExpiredAssetData]);
      }
    } else {
      oFilterProps.setAdditionalFiltersForAsset && oFilterProps.setAdditionalFiltersForAsset([]);
    }

    oFilterProps.setAvailableFilters(aFilterData);

    if(CS.isNotEmpty(aSortData)){
      this.setFilterSortData(aSortData);
    }
    this.removeInValidAppliedFilterData(aFilterData);

    oFilterProps.setIsFilterDirty(false);
  };

  oPrototype.putParentNodeIdInAllChildNodesAndCreateFlatNodeMap = function (aChildren, sParentNodeId, oFlatNodeMap) {
    let _this = this;

    CS.forEach(aChildren, function (oChild) {
      let aInnerChildren = [];
      if(oChild && oChild.children && oChild.children.length){
        aInnerChildren = oChild.children;
        _this.putParentNodeIdInAllChildNodesAndCreateFlatNodeMap(aInnerChildren, oChild.id, oFlatNodeMap);
      }
      oFlatNodeMap[oChild.id] = {
        id: oChild.id,
        label: oChild.label,
        children: aInnerChildren,
        parentId: sParentNodeId
      };
    });
  };

  oPrototype.setTaxonomyTreeData = function (aClassTaxonomyTreeData, aTaxonomyTreeData) {
    let oFilterProps = this.getFilterProps();
    if(CS.isEmpty(oFilterProps.getTaxonomyTree())) { /**** Go inside only if the tree loaded at first time. *****/
      let aDefaultTaxonomyTree = CS.cloneDeep(oFilterProps.getDefaultTaxonomyTree());
      aDefaultTaxonomyTree = aDefaultTaxonomyTree.concat(aTaxonomyTreeData);

      let sTreeRootNode = "-1";
      let sParentTaxonomyId = oFilterProps.getSelectedOuterParentId();
      let bIsClassTaxonomy = sParentTaxonomyId == "" || sParentTaxonomyId == sTreeRootNode;

      if(bIsClassTaxonomy) {
        let oRootTaxonomy = CS.find(aDefaultTaxonomyTree, {id: sTreeRootNode});
        oRootTaxonomy && (oRootTaxonomy.children = aClassTaxonomyTreeData);
      }
      else {
        CS.forEach(aClassTaxonomyTreeData, function (oClassTaxonomy) {
          let oTaxonomy = CS.find(aDefaultTaxonomyTree, {id: oClassTaxonomy.id});
          oTaxonomy && (CS.assign(oTaxonomy, oClassTaxonomy));
        });
      }

      let oFlatNodeMap = {};
      this.putParentNodeIdInAllChildNodesAndCreateFlatNodeMap(aDefaultTaxonomyTree, null, oFlatNodeMap);
      oFilterProps.setTaxonomyTreeFlatMap(oFlatNodeMap);

      let oNewTreeProps = {};
      this.addExpandAndCollapsePropertyToTaxonomyTree(aDefaultTaxonomyTree, null, oNewTreeProps);
      oFilterProps.setTaxonomyVisualProps(oNewTreeProps);
      oFilterProps.setTaxonomyTree(aDefaultTaxonomyTree);
    }
  };

  oPrototype.setTaxonomyTreeOnRefresh = function (aTaxonomyTreeData) {
    let oFilterProps = this.getFilterProps();
    let aTreeRootNodeId = "-1";
    let aTaxonomyTree = oFilterProps.getTaxonomyTree();

    /** If taxonomy tree is empty then prepare its data **/
    if(CS.isEmpty(aTaxonomyTree)) {
      this.setTaxonomyTreeData([], []);
      return;
    }

    let oFlatNodeMap = oFilterProps.getTaxonomyTreeFlatMap();
    let oNewTreeProps = oFilterProps.getTaxonomyVisualProps();
    let sSelectedParentId = oFilterProps.getSelectedOuterParentId();

    /** To remove & update taxonomies & its related props **/
    CS.remove(aTaxonomyTree, function (oTree) {
      let sTreeId = oTree.id;
      if (sTreeId !== aTreeRootNodeId) {
        let oTaxonomy = CS.find(aTaxonomyTreeData, {id: sTreeId});
        let oNodeFromFlatMap = oFlatNodeMap[sTreeId];
        if (CS.isEmpty(oTaxonomy)) {
          /** To remove deleted taxonomies data **/
          oFlatNodeMap[sTreeId] && delete oFlatNodeMap[sTreeId];
          oNewTreeProps[sTreeId] && delete oNewTreeProps[sTreeId];

          /** To Reset applied taxonomies if already applied & deleted **/
          sTreeId === sSelectedParentId && oFilterProps.setSelectedOuterParentId("");
          return true;
        }
        else {
          oTree.label = oNodeFromFlatMap.label = oTaxonomy.label; /** To update taxonomies data for updated taxonomy**/
        }
      }
    });

    /** To update taxonomy data for newly added taxonomies **/
    let aAddedTaxonomies = CS.differenceBy(aTaxonomyTreeData, aTaxonomyTree, "id");
    if (!CS.isEmpty(aAddedTaxonomies)) {
      aTaxonomyTree = CS.concat(aTaxonomyTree, aAddedTaxonomies);
      this.putParentNodeIdInAllChildNodesAndCreateFlatNodeMap(aAddedTaxonomies, aTreeRootNodeId, oFlatNodeMap);
      this.addExpandAndCollapsePropertyToTaxonomyTree(aAddedTaxonomies, null, oNewTreeProps);
    }

    oFilterProps.setTaxonomyTree(aTaxonomyTree);
    oFilterProps.setTaxonomyTreeFlatMap(oFlatNodeMap);
    oFilterProps.setTaxonomyVisualProps(oNewTreeProps);
  };

  oPrototype.getToggledNodeState = function (iState) {
    if (iState >= 1 ) {
      return 0;
    } else {
      return 2;
    }
  };

  oPrototype.checkAndEnableAllNodes = function (aTaxonomyTreeData) {
    let oFilterProps = this.getFilterProps();
    let sParentTaxonomyId = oFilterProps.getSelectedOuterParentId();
    let bIsClassTaxonomy = sParentTaxonomyId == "" || sParentTaxonomyId == "-1";
    let _this = this;

    if(bIsClassTaxonomy) {
      this.checkAndShowAllNodes("-1");
    } else {
      CS.forEach(aTaxonomyTreeData, function (oTaxonomy) {
        _this.checkAndShowAllNodes(oTaxonomy.id);
      });
    }
  };

  oPrototype.checkAndShowAllNodes = function (sId) {
    let oFilterProps = this.getFilterProps();
    let oTaxonomyTree = CS.find(oFilterProps.getTaxonomyTree(), {id: sId});
    if (!oTaxonomyTree) {
      return;
    }
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();
    this.assignToAllNodesBelow({isChecked: 2, isHidden: false}, oTaxonomyTree, oVisualProps); //Passed visualProps to
    // speed up the process
    oFilterProps.setTaxonomyVisualProps(oFilterProps.getTaxonomyVisualProps());
  };

  oPrototype.toggleHeaderNodeStateRecursively = function (aNodes, sNodeId, oVisualProps, aAllAffectedNodeIds, bCrossClicked) {

    let bNodeFound = false;
    let bAllChildChecked = true;
    let bAnyChildChecked = false;
    let oFilterProps = this.getFilterProps();
    oVisualProps = oVisualProps || oFilterProps.getTaxonomyVisualProps();
    let sSelectedParentId = oFilterProps.getSelectedOuterParentId();
    let _this = this;

    CS.forEach(aNodes, function (oNode) {
      let oNodeVisualProp = oVisualProps[oNode.id] || {};
      if (oNode.id == sNodeId) {
        bNodeFound = true;
        /**** Fixes for cross icon click not working while click on parent node cross icon****/
        let iCheckedState = oNodeVisualProp.isChecked;
        if(oNode.id == sSelectedParentId && bCrossClicked) {
          oFilterProps.setSelectedOuterParentId("-1");
          let oTaxonomyTreeFlatMap = oFilterProps.getTaxonomyTreeFlatMap();
          let aDefaultTaxonomyTree = oFilterProps.getDefaultTaxonomyTree();
          oTaxonomyTreeFlatMap["-1"] = oTaxonomyTreeFlatMap["-1"] || aDefaultTaxonomyTree[0];
          iCheckedState = 2;
        }
        /********************************************* ***************************************/
        let iNewState = _this.getToggledNodeState(iCheckedState);
        oNodeVisualProp.isChecked = iNewState;
        let bNodeHiddenState = oNodeVisualProp.isHidden;
        _this.assignToAllNodesBelow({isChecked: iNewState, isHidden: !iNewState}, oNode, oVisualProps, aAllAffectedNodeIds);
        oNodeVisualProp.isHidden = bNodeHiddenState;
        aAllAffectedNodeIds.push(oNode.id);
      }
      else if (!bNodeFound && !CS.isEmpty(oNode.children)) {
        let oChildInfo = _this.toggleHeaderNodeStateRecursively(oNode.children, sNodeId, oVisualProps, aAllAffectedNodeIds);
        _this.updateNodeStateByChildInfo(oNodeVisualProp, oChildInfo);
        if(oChildInfo.isNodeFound) {
          bNodeFound = true;
          aAllAffectedNodeIds.push(oNode.id);
        }
      } else if (bNodeFound) {
        aAllAffectedNodeIds.push(oNode.id);
      }

      bAllChildChecked = bAllChildChecked && oNodeVisualProp.isChecked == 2;
      bAnyChildChecked = bAnyChildChecked || !!oNodeVisualProp.isChecked;
    });

    return {
      isNodeFound: bNodeFound,
      isAnyChildChecked: bAnyChildChecked,
      isAllChildChecked: bAllChildChecked
    }

  };

  oPrototype.getAppliedFiltersClone = function (oExtraData) {
    let oFilterProps = this.getFilterProps(oExtraData);
    if (oFilterProps.getAppliedFiltersClone() == null) {
      oFilterProps.createAppliedFiltersClone();
      oFilterProps.setIsFilterDirty(true);
    }
    return oFilterProps.getAppliedFiltersClone();
  };

  oPrototype.updateAppliedFilterDataFromClone = function (oExtraData) {
    let oFilterProps = this.getFilterProps(oExtraData);
    let oAppliedFiltersClone = oFilterProps.getAppliedFiltersClone();
    if (oAppliedFiltersClone != null) {
      oFilterProps.setAppliedFilters(oAppliedFiltersClone);
      oFilterProps.clearAppliedFiltersClone();
    }
  };

  oPrototype.isNewSearchTextDifferent = function () {
    let oFilterProps = this.getFilterProps();
    let sNewSearchText = oFilterProps.getSearchText();
    let sOldSearchText = oFilterProps.getOldSearchText();
    return (sOldSearchText != sNewSearchText);
  };

  oPrototype.setSelectedParentTaxonomyIds = function (aTreeList, oVisualProps, aSelectedIds) {
    let _this = this;

    CS.forEach(aTreeList, function (oTreeNode) {
      if(oTreeNode.id != "-1"){
        if(oVisualProps[oTreeNode.id].isChecked == 2) {
          aSelectedIds.push(oTreeNode.id);
        } else if(!CS.isEmpty(oTreeNode.children)) {
          _this.setSelectedParentTaxonomyIds(oTreeNode.children, oVisualProps, aSelectedIds);
        }
      }
    });
  };

  oPrototype.setSelectedTaxonomyIdsRecursively = function (aTreeList, oVisualProps, aSelectedIds) {
    let _this = this;

    CS.forEach(aTreeList, function (oTreeNode) {
      if(oTreeNode.id != "-1"){
        if(CS.isEmpty(oVisualProps[oTreeNode.id])){
          oVisualProps[oTreeNode.id] = {
            isChecked: 2
          }
        }
        if(oVisualProps[oTreeNode.id].isChecked == 2) {
          aSelectedIds.push(oTreeNode.id);
        } else if(!CS.isEmpty(oTreeNode.children)) {
          _this.setSelectedTaxonomyIdsRecursively(oTreeNode.children, oVisualProps, aSelectedIds);
        }
      }
    });
  };

  oPrototype.setSelectedOuterParentId = function  (aTreeList, oVisualProps) {
    let oFilterProps = this.getFilterProps();
    let sId = "";
    CS.forEach(aTreeList, function (oNode) {
      if(oVisualProps[oNode.id].isChecked == 1 || oVisualProps[oNode.id].isChecked == 2 ){
        sId = oNode.id;
      }
    });

    oFilterProps.setSelectedOuterParentId(sId);
  };

  oPrototype.getFilteredSearchDataFilledWithVisualState = function (aOldAvailableData, aNewAvailableData) {
    let aFilteredSearchData = [];
    CS.forEach(aNewAvailableData, function (oAvailableObject) {
      let oSearchedData = CS.find(aOldAvailableData, {id: oAvailableObject.id});
      if(!CS.isEmpty(oSearchedData)) {
        aFilteredSearchData.push(oSearchedData);
      } else {
        aFilteredSearchData.push(oAvailableObject);
      }
    });
    return aFilteredSearchData;
  };

  oPrototype.successFilterSearchTextChanged = function (oSearchedData, oResponse) {
    let oFilterProps = this.getFilterProps();
    let aAvailableFilters = oFilterProps.getAvailableFilters();
    let oParentNode = CS.find(aAvailableFilters, {id: oSearchedData.attributeId});
    let oResponseData = oResponse.success;
    if(!CS.isEmpty(oResponseData)) {
      oParentNode.children = this.getFilteredSearchDataFilledWithVisualState(oParentNode.children, oResponseData.children);
    }
    this.triggerChange();
  };

  oPrototype.handleFilterTagSearch = function (oSearchedData, oExtraData) {
    let oFilterProps = this.getFilterProps(oExtraData);
    let aAvailableFilters = oFilterProps.getAvailableFilters();
    let oParentNode = CS.find(aAvailableFilters, {id: oSearchedData.attributeId});
    let sSearchText = oSearchedData.attributeSearchText ? (oSearchedData.attributeSearchText+"").toLowerCase(): "";
    CS.forEach(oParentNode.children, function (oTagChild) {
      let sSearchTextIn = oTagChild.label ? (oTagChild.label+"").toLowerCase(): "";
      oTagChild.isHidden = !CS.includes(sSearchTextIn, sSearchText);
    });
    this.triggerChange();
  };

  oPrototype.handleFilterSearchTextChanged = function (oSearchedData, oExtraData) {
    let oFilterProps = this.getFilterProps(oExtraData);
    oFilterProps.setIsFilterDirty(true);
    if(ContentUtils.isTag(oSearchedData.type)) {
      this.handleFilterTagSearch(oSearchedData, oExtraData);
    } else {
      let oCallExtraData = {};
      let oAttributePropertySearch = {
        attributePropertySearch: {
          attributeId: oSearchedData.attributeId,
          attributeSearchText: oSearchedData.attributeSearchText
        }
      };
      let oPostDataForFilter = ContentUtils.createFilterPostData(oAttributePropertySearch, {screenContext: this.screenContext, filterType: this.filterType});
      if (CS.isEmpty(oPostDataForFilter.moduleId)) {
        return;
      }
      let sUrl = getRequestMapping().ArticlePropertySearch;
      let fSuccess = this.successFilterSearchTextChanged.bind(this, oSearchedData);
      let fFailure = this.failureGenericFunction.bind(this, "failureFilterSearchTextChanged");
      CS.postRequest(sUrl, {}, oPostDataForFilter, fSuccess, fFailure, false, oCallExtraData);
    }
  };

  oPrototype.clearSelectedTaxonomisFromProps = function () {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setSelectedOuterParentId("");
    oFilterProps.setSelectedParentTaxonomyIds([]);
    oFilterProps.setSelectedParentTypeIds([]);
    oFilterProps.setSelectedTypes([]);
  };

  oPrototype.getFilterProps = function(oExtraData){
    let sContextId = oExtraData ? oExtraData.contextId : "";
    let sKlassInstanceId = oExtraData && oExtraData.klassInstanceId ? oExtraData.klassInstanceId : "";
    return FilterUtils.getFilterProps({screenContext: this.screenContext, filterType: this.filterType, contextId: sContextId, klassInstanceId: sKlassInstanceId});
  };

  oPrototype.updateVisualPropsForNodesBelow = function (oNode, bSkipIsExpanded, bSkipIsChecked, bSkipIsHidden) {
    let oFilterProps = this.getFilterProps();
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();
    let oNodeVisualProp = oVisualProps[oNode.id] || {};
    let areAllChildValues2 = true;
    let areAllChildValues0 = true;
    let _this = this;

    if (oNode.children.length) {
      CS.forEach(oNode.children, function (oChild) {
        let iChildIsCheckedValue = _this.updateVisualPropsForNodesBelow(oChild, bSkipIsExpanded, bSkipIsChecked, bSkipIsHidden);
        if (iChildIsCheckedValue === 2) {
          areAllChildValues0 = false;
        } else if (iChildIsCheckedValue === 0) {
          areAllChildValues2 = false;
        } else {
          areAllChildValues0 = false;
          areAllChildValues2 = false;
        }
      });
    }
    else {

      return oNodeVisualProp.isChecked;
    }

    let bIsChecked, bIsExpanded, bIsHidden;
    //if all elements in children are 2, set 2
    if (areAllChildValues2) {
      bIsChecked = 2;
      bIsExpanded = true;
      bIsHidden = false;
    }
    //if all elements in children are 0, set 0
    else if (areAllChildValues0) {
      bIsChecked = 0;
      bIsExpanded = false;
      bIsHidden = true;
    }
    //else set 1
    else {
      bIsChecked = 1;
      bIsExpanded = true;
      bIsHidden = false;
    }

    bSkipIsExpanded || (oNodeVisualProp.isExpanded = bIsExpanded);
    bSkipIsChecked || (oNodeVisualProp.isChecked = bIsChecked);
    bSkipIsHidden || (oNodeVisualProp.isHidden = bIsHidden);

    return bIsChecked;
  };

  oPrototype.updateTaxonomyTreeVisualProps = function (sParentTaxonomyId, aSelectedTaxonomyIds) {
    let oFilterProps = this.getFilterProps();
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();
    let aTreeData = oFilterProps.getTaxonomyTree();

    sParentTaxonomyId = sParentTaxonomyId || "-1";

    //set visual props of first level (i.e. taxonomies) to default. This will prevent multiple taxonomies to be selected.
    CS.forEach(aTreeData, function (oTaxonomy) {
      let sId = oTaxonomy.id;
      oVisualProps[sId].isChecked = 0;
      oVisualProps[sId].isExpanded = false;
      oVisualProps[sId].isHidden = true;
    });

    let oParentNode = CS.find(aTreeData, {id: sParentTaxonomyId});

    CS.forEach(aSelectedTaxonomyIds, function (sId) {
      if(!CS.isEmpty(oVisualProps[sId])) {
        oVisualProps[sId].isChecked = 2;
        oVisualProps[sId].isExpanded = true;
        oVisualProps[sId].isHidden = false;
      }
    });

    //update visual props by parsing tree...
    oVisualProps[sParentTaxonomyId].isChecked = this.updateVisualPropsForNodesBelow(oParentNode, false, false, true);
    oVisualProps[sParentTaxonomyId].isExpanded = true;
    oVisualProps[sParentTaxonomyId].isHidden = false;

  };

  oPrototype.setTagsAsAppliedFilters = function (aTags, oMasterTags) {
    let oFilterProps = this.getFilterProps();
    let aAppliedFilters = [];
    let _this = this;

    CS.forEach(aTags, function (oTag) {
      let oMasterTag = oMasterTags[oTag.id];
      let oAppliedTag = _this.getDefaultAppliedFilterData(oTag);
      oAppliedTag.label = oMasterTag.label;

      CS.forEach(oTag.mandatory, function (oChild) {
        let oMasterChild = CS.find(oMasterTag.children, {id: oChild.id}) || {};
        oChild.label = oMasterChild.label;
        oAppliedTag.children.push(oChild);

      });

      aAppliedFilters.push(oAppliedTag);
    });

    oFilterProps.setAppliedFilters(aAppliedFilters);
  };


/**
 * @function resetFilterPropsByContext
 * @description reset filter props by context
 */
oPrototype.resetFilterPropsByContext = function () {
  FilterStoreFactory.resetStoreByContext(this.screenContext);
};

oPrototype.resetPaginationDataByContext = function () {
  let oFilterProps = this.getFilterProps();
  oFilterProps.resetPaginationData();
};

  oPrototype.matchAndAddTaxonomyIds = function(aTaxonomyTree, aMatchingTaxonomyIds, sSearchText){
    let _this = this;

    CS.forEach(aTaxonomyTree, function (oTaxonomyObject) {
      let sLabel = oTaxonomyObject.label;
      let sLabelToLowerCase = sLabel.toLowerCase();
      let sSearchTextToLowerCase = sSearchText.toLowerCase();
      if(sLabelToLowerCase.includes(sSearchTextToLowerCase)){
        if(oTaxonomyObject.id != -1) {
          aMatchingTaxonomyIds.push(oTaxonomyObject.id);
        }
      }
      _this.matchAndAddTaxonomyIds(oTaxonomyObject.children, aMatchingTaxonomyIds, sSearchText);
    })
  };

  oPrototype.addMatchingTaxonomyIds = function(sSearchText){
    let oFilterProps = this.getFilterProps();
    let aTaxonomyTree = oFilterProps.getTaxonomyTree();
    let aMatchingTaxonomyIds = [];
    this.matchAndAddTaxonomyIds(aTaxonomyTree, aMatchingTaxonomyIds, sSearchText);
    oFilterProps.setMatchingTaxonomyIds(aMatchingTaxonomyIds);
  };

  oPrototype.handleFilterSortReOrdered = function (aSortData) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setAvailableSortData(aSortData);

    this.triggerChange();
  };

  oPrototype.handleAdvancedSearchButtonClicked = function () {
    let oFilterProps = this.getFilterProps();
    let bAdvancedSearchStatus = oFilterProps.getAdvancedSearchPanelShowStatus();
    oFilterProps.setAdvancedSearchPanelShowStatus(!bAdvancedSearchStatus);

    if(!bAdvancedSearchStatus){
      this.createAppliedFilterCollapseStatusMap();
    }
  };

  oPrototype.handleCollectionFilterButtonClicked = function (sCollectionId) {
    CollectionViewProps.setActiveCollectionFilter(sCollectionId);
    let oFilterProps = this.getFilterProps();
    oFilterProps.setAdvancedSearchPanelShowStatus(true);
    this.createAppliedFilterCollapseStatusMap();
  };

  oPrototype.createAppliedFilterCollapseStatusMap = function () {
    let oFilterProps = this.getFilterProps();
    let aAppliedFilters = oFilterProps.getAppliedFilters();
    let oAppliedFilterCollapseStatusMap = oFilterProps.getAppliedFilterCollapseStatusMap();
    CS.forEach(aAppliedFilters, function (oAppliedFilter) {
      if(!CS.isEmpty(oAppliedFilterCollapseStatusMap[oAppliedFilter.id])){
        oAppliedFilterCollapseStatusMap[oAppliedFilter.id].isCollapsed = false;
      }else {
        oAppliedFilterCollapseStatusMap[oAppliedFilter.id] = {isCollapsed: false};
      }
    });
  };

  oPrototype.getMasterTagChildrenInAdvancedFilterStructure = function (oFilter) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oLoadedTags = oComponentProps.screen.getLoadedTags();
    let aRes = [];

    let bIsRangeTagType = oFilter.type === TagTypeConstants.RANGE_TAG_TYPE;
    let bIsYNNTagType = oFilter.type === TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE;

    let oOriginalMasterTag = oLoadedTags[oFilter.id] || ContentUtils.getMasterTagById(oFilter.id);
    let oMasterTag = CS.cloneDeep(oOriginalMasterTag);
    oFilter.label = oMasterTag.label;

    let aMasterChildren = oMasterTag.children;
    let aFilterChildren = oFilter.children;

    let iMin = 0;
    let iMax = 100;
    if(bIsRangeTagType){
      iMin = ContentUtils.getMinValueFromListByPropertyName(oMasterTag.tagValues, 'relevance');
      iMax = ContentUtils.getMaxValueFromListByPropertyName(oMasterTag.tagValues, 'relevance');
    }
    CS.forEach(aMasterChildren, function (oMasterChild) {
      let iFrom = 0;
      let iTo = 0;
      let oFound = CS.find(aFilterChildren, {id: oMasterChild.id});
      if(bIsRangeTagType){
        iTo = iMax;
        if(!CS.isEmpty(oFound)){
          iFrom = oFound.from;
          iTo = oFound.to;
        }

        aRes.push({
          id: oMasterChild.id,
          label: oMasterChild.label,
          type: "range",
          min: iMin,
          max: iMax,
          from: iFrom,
          to: iTo,
          baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel",
          advancedSearchFilter: true
        })
      } else {

        if(!CS.isEmpty(oFound)){
          if(bIsYNNTagType){
            iFrom = oFound.from;
            iTo = oFound.to;
          }else{
            iFrom = 100;
            iTo = 100;
          }

        }

        aRes.push({
          id: oMasterChild.id,
          label: oMasterChild.label,
          type: "range",
          from: iFrom,
          to: iTo,
          baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel",
          advancedSearchFilter: true
        })
      }

    })

    return aRes;
  };

  oPrototype.canModifyMasterTagChildrenInAdvancedFilterStructure = function(sFilterType){
    let aTagList = [
      TagTypeConstants.YES_NEUTRAL_TAG_TYPE,
      TagTypeConstants.TAG_TYPE_MASTER,
      TagTypeConstants.RULER_TAG_TYPE,
      TagTypeConstants.TAG_TYPE_BOOLEAN,
      TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE,
      TagTypeConstants.LISTING_STATUS_TAG_TYPE,
      TagTypeConstants.STATUS_TAG_TYPE
    ];

    return CS.includes(aTagList, sFilterType);
  };

  oPrototype.modifyAlreadyPresentDataIntoAdvancedFilterData = function () {
    let oFilterProps = this.getFilterProps();
    let aAppliedFilters = oFilterProps.getAppliedFilters();
    oFilterProps.setAppliedFiltersCloneBeforeModifications(CS.cloneDeep(aAppliedFilters));

    let aFiltersToRemove = [];
    let _this = this;

    CS.forEach(aAppliedFilters, function (oFilter, iIndex) {
      oFilter.advancedSearchFilter = true;
      if(ContentUtils.isTag(oFilter.type)){
        if(!_this.canModifyMasterTagChildrenInAdvancedFilterStructure(oFilter.type)){
          oFilter.children = _this.getMasterTagChildrenInAdvancedFilterStructure(oFilter);
        }

      }
      else if(oFilter.type && CS.includes(oFilter.type.toLowerCase(), "attribute")){
        if(CS.isEmpty(oFilter.children)){
          aFiltersToRemove.push({index:iIndex});
        }else {
          CS.forEach(oFilter.children, function (oChild) {
            if(!oChild.advancedSearchFilter){
              oChild.advancedSearchFilter = true;
              oChild.value = oChild.label;
            }
          });
        }

      }

      CS.forEach(aFiltersToRemove, function (oIndex) {
        aAppliedFilters.splice(oIndex.index, 1);
      })

    })
  };

  oPrototype.uncheckAndShowAllCurrentTaxonomyNodes = function () {
    let oFilterProps = this.getFilterProps();
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();

    CS.forEach(oVisualProps, function (oProp) {
      oProp.isChecked = 0;
      oProp.isHidden = false;
    });

    this.getFilterProps().setAllAffectedTreeNodeIds([]);
  };

  oPrototype.resetParentCheckedStateBasedOnChildrenState = function () {
    let oFilterProps = this.getFilterProps();
    let aTreeList = oFilterProps.getTaxonomyTree();
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();
    let sParentTaxonomyId = oFilterProps.getSelectedOuterParentId() || "-1";

    CS.forEach(aTreeList, function (oNode) {
      if(oNode.id == sParentTaxonomyId) {
        let iParentState = 0;
        CS.forEach(oNode.children, function (oChild, iIndex) {
          let oChildVisualProps = oVisualProps[oChild.id];
          if(oChildVisualProps) {
            if(oChildVisualProps.isChecked == 2) {
              iParentState = iIndex == 0 ? 2 : (iParentState == 2 && 2)|| 1;
            } else if (oChildVisualProps.isChecked == 1) {
              iParentState = 1;
            } else {
              iParentState = iParentState && 1 || 0;
            }
          }
        });

        let oParentVisualProp = oVisualProps[sParentTaxonomyId];
        oParentVisualProp.isChecked = iParentState;
        return false;
      }
    });

  };

  oPrototype.resetNodeStateAccordingToChildren = function(oNode, oVisualProps){
    oVisualProps[oNode.id] = oVisualProps[oNode.id] || {};
    let oNodeVisualProp = oVisualProps[oNode.id];
    let aChildrenIds = CS.map(oNode.children, "id");

    let bAllChildChecked = true;
    let bAnyChildChecked = false;

    for(let i=0; i<aChildrenIds.length; i++){
      let sChildId = aChildrenIds[i];
      oVisualProps[sChildId] = oVisualProps[sChildId] || {};
      let oChildProps = oVisualProps[sChildId];

      bAllChildChecked = bAllChildChecked && oChildProps.isChecked == 2;
      bAnyChildChecked = bAnyChildChecked || !!oChildProps.isChecked;
    }

    if (bAllChildChecked) {
      oNodeVisualProp.isChecked = 2;
    }
    else if (bAnyChildChecked) {
      oNodeVisualProp.isChecked = 1;
    }
    else {
      oNodeVisualProp.isChecked = 0
    }
  };

  oPrototype.handleTaxonomyChildrenLazyData = function (oCallBackData, oExtraData) {
    //TODO: Change implementation (Move filter related stuff from content-store to filter-store)
    let oContentStore = ContentUtils.getContentStore();
    let oFilterProps = this.getFilterProps();
    if (!CS.isEmpty(oFilterProps.getSelectedParentTaxonomyIds())) {
      if (this.getOldSelectedOuterParentId() == "") {
        let sOldSelectedOuterParentId = this.getSelectedOuterParentId();
        sOldSelectedOuterParentId = sOldSelectedOuterParentId == "" ? -1 : sOldSelectedOuterParentId;
        oFilterProps.setOldSelectedOuterParentId(sOldSelectedOuterParentId);
      }
    }
    this.createTaxonomyTreeBackup();

    oCallBackData.filterContext = {
      screenContext: this.screenContext,
      filterType: this.filterType
    };
    oContentStore.handleTaxonomyChildrenLazyData(oCallBackData, oExtraData);
  };

  oPrototype.getTaxonomyNodeById =  function (sId) {
    let oFilterProps = this.getFilterProps();
    let aTaxonomyTree = oFilterProps.getTaxonomyTree();
    return ContentUtils.getNodeFromTreeListById(aTaxonomyTree, sId);
  };

  oPrototype.resetAllTaxonomyRootNodeOtherThanSelectedTaxonomy = function () {
    let oFilterProps = this.getFilterProps();
    let aTaxonomyTree = oFilterProps.getTaxonomyTree();
    CS.forEach(aTaxonomyTree, function (oTaxonomy) {
      if(oTaxonomy.id != oFilterProps.getSelectedOuterParentId()) {
        oTaxonomy.children = [];
      }
    });
  };

  oPrototype.updateTaxonomyCountAndLabel = function (aUpdatedTaxonomy, aOldTaxonomy) {
    let _this = this;

    CS.forEach(aUpdatedTaxonomy, function (oTaxonomy) {
      let oOldTaxonomyChild = ContentUtils.getNodeFromTreeListById(aOldTaxonomy, oTaxonomy.id);
      if(!CS.isEmpty(oTaxonomy.children)) {
        _this.updateTaxonomyCountAndLabel(oTaxonomy.children, oOldTaxonomyChild.children);
      }
      oOldTaxonomyChild.id = oTaxonomy.id;
      oOldTaxonomyChild.label = oTaxonomy.label;
      oOldTaxonomyChild.count = oTaxonomy.count;
    });
  };

  oPrototype.successFetchTaxonomyTreeDetailsCallBack = function (bIsCreateNewProps, oCallbackData, oResponse) {
    oResponse = oResponse.success;
    let aTaxonomyTreeData = oResponse.klassTaxonomyInfo;
    let aDynamicHierarchyInfo = oResponse.dynamicHierarchyInfo;
    let oFilterProps = this.getFilterProps();
    let oFlatNodeMap = {};
    // To reduce iterations on tree
    this.putParentNodeIdInAllChildNodesAndCreateFlatNodeMap(aTaxonomyTreeData, null, oFlatNodeMap);
    oFilterProps.setTaxonomyTreeFlatMap(oFlatNodeMap);
    let oNewTreeVisualProps = {};

    let aSelectedNodeIds = CS.isNotEmpty(oFilterProps.getSelectedParentTaxonomyIds()) ? oFilterProps.getSelectedParentTaxonomyIds() : oFilterProps.getSelectedTypes();
    this.createVisualPropsData(oNewTreeVisualProps, aTaxonomyTreeData, aSelectedNodeIds);
    oFilterProps.setTaxonomyVisualProps(oNewTreeVisualProps);
    oFilterProps.setTaxonomyTree(aTaxonomyTreeData);

    CS.forEach(aDynamicHierarchyInfo, function (oNode) {
      delete oNode.children;
    });
    if(oCallbackData.functionToExecute){
      oCallbackData.functionToExecute();
    }
    this.createTaxonomyTreeBackup();
    this.triggerChange();
  };

  oPrototype.fetchTaxonomyTreeData = function (bIsCreateNewProps, oCallback = {}, oExtraData) {
    let oPostDataForFilter = this.createGetAllInstancesData();
    delete oPostDataForFilter.isFilterDataRequired;
    if(!oCallback.isRetainSelectedTaxonomyIds){
      delete oPostDataForFilter.selectedTaxonomyIds;
    }
    let sContentId = ContentUtils.getTreeRootNodeId();
    let oData = {
      id: sContentId,
    };
    let oURLData = {};
    oPostDataForFilter.isArchivePortal = ContentUtils.getIsArchive();

    /************** Do not add any check here.(oPostDataForFilter is common request data)************************
     * If anything needs to be added in request data please pass it from controller as a request model to view *****/

    let oTaxonomyTreeRequestData = oExtraData.taxonomyTreeRequestData || {};
    CS.assign(oPostDataForFilter, oTaxonomyTreeRequestData);
    let sUrl = oTaxonomyTreeRequestData.url;
    delete oPostDataForFilter.url;

    let fSuccess = this.successFetchTaxonomyTreeDetailsCallBack.bind(this, bIsCreateNewProps, oCallback);
    let fFailure = this.failureGenericFunction.bind(this, "failureFetchTaxonomyTreeDetailsCallBack");
    return CS.postRequest(sUrl, oData, oPostDataForFilter, fSuccess, fFailure, null, oURLData);
  };

  oPrototype.createTaxonomyTreeBackup = function () {
    let oFilterProps = this.getFilterProps();
    if(CS.isEmpty(oFilterProps.getTaxonomyTreeBackup())) {
      let aTreeList = oFilterProps.getTaxonomyTree();
      let oBackup = {};
      oBackup.taxonomyTree = CS.cloneDeep(aTreeList);
      oBackup.selectedOuterParentId = oFilterProps.getSelectedOuterParentId();
      oBackup.taxonomyVisualProps = CS.cloneDeep(oFilterProps.getTaxonomyVisualProps());
      oBackup.taxonomyTreeFlatMap = CS.cloneDeep(oFilterProps.getTaxonomyTreeFlatMap());
      oBackup.matchingTaxonomyIds = CS.cloneDeep(oFilterProps.getMatchingTaxonomyIds());
      oBackup.selectedParentTaxonomyIds = CS.cloneDeep(oFilterProps.getSelectedParentTaxonomyIds());
      oBackup.selectedTypes = CS.cloneDeep(oFilterProps.getSelectedTypes());
      oBackup.selectedParentTypeIds = CS.cloneDeep(oFilterProps.getSelectedParentTypeIds());
      oFilterProps.setOldTaxonomySearchText(oFilterProps.getTaxonomySearchText());
      oFilterProps.setTaxonomyTreeBackup(oBackup);
    }
  };

  oPrototype.restoreTaxonomyTreeBackup = function (oCallback) {
    let oFilterProps = this.getFilterProps();
    let oTaxonomyTreeBackup = CS.cloneDeep(oFilterProps.getTaxonomyTreeBackup());
    if(!CS.isEmpty(oTaxonomyTreeBackup)) {
      oFilterProps.setTaxonomyTree(oTaxonomyTreeBackup.taxonomyTree);
      oFilterProps.setSelectedOuterParentId(oTaxonomyTreeBackup.selectedOuterParentId);
      oFilterProps.setTaxonomyVisualProps(oTaxonomyTreeBackup.taxonomyVisualProps);
      oFilterProps.setTaxonomyTreeFlatMap(oTaxonomyTreeBackup.taxonomyTreeFlatMap);
      oFilterProps.setMatchingTaxonomyIds(oTaxonomyTreeBackup.matchingTaxonomyIds);
      oFilterProps.setSelectedParentTaxonomyIds(oTaxonomyTreeBackup.selectedParentTaxonomyIds);
      oFilterProps.setSelectedTypes(oTaxonomyTreeBackup.selectedTypes);
      oFilterProps.setSelectedParentTypeIds(oTaxonomyTreeBackup.selectedParentTypeIds);
      oFilterProps.setTaxonomySearchText(oFilterProps.getOldTaxonomySearchText());
      if(oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute(oFilterProps.getOldTaxonomySearchText());
      }
    }
  };

  oPrototype.setTaxonomySearchText = function (sSearchText) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setTaxonomySearchText(sSearchText);
  };

  oPrototype.getSelectedOuterParentId = function () {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.getSelectedOuterParentId();
  };

  oPrototype.getOldSelectedOuterParentId = function () {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.getOldSelectedOuterParentId();
  };

  oPrototype.clearTaxonomyTreeBackup = function () {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.clearTaxonomyTreeBackup();
  };

  oPrototype.getTaxonomyTreeFlatMap = function () {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.getTaxonomyTreeFlatMap();
  };

  oPrototype.getTaxonomyVisualProps = function () {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.getTaxonomyVisualProps();
  };

  oPrototype.setTaxonomyVisualProps = function (oVisualProps) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setTaxonomyVisualProps(oVisualProps);
  };

  oPrototype.setTaxonomyTreeFlatMap = function (oTaxonomyFlatMap) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setTaxonomyTreeFlatMap(oTaxonomyFlatMap);
  };

  oPrototype.getTaxonomyTree = function () {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.getTaxonomyTree();
  };

  oPrototype.setMatchingTaxonomyIds = function (aMatchingTaxonomyIds) {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.setMatchingTaxonomyIds(aMatchingTaxonomyIds);
  };

  oPrototype.handleTaxonomySectionSnackbarButtonClicked = function (sButtonContext) {
    if (sButtonContext == "save") {
      this.handleSaveFilterTaxonomySection();
    } else if (sButtonContext == "discard") {
      this.handleDiscardFilterTaxonomySection()
    }
  };

  oPrototype.clearTaxonomySectionData = function () {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setTaxonomyNodeSections([]);
    oFilterProps.setTaxonomySettingIconClickedNode({});
  };

  oPrototype.generateADMForTaxonomy = function () {
    let oFilterProps = this.getFilterProps();
    let oTaxonomySection = oFilterProps.getTaxonomyNodeSections();

    let oTaxonomySectionClone = CS.cloneDeep(oTaxonomySection);
    if(!oTaxonomySectionClone.clonedObject){
      return null;
    }

    let oADM = {};

    let sSplitter = ContentUtils.getSplitter();

    let aOldSections = oTaxonomySectionClone.sections;
    let aNewSections = oTaxonomySectionClone.clonedObject.sections;

    oADM.modifiedElements = [];
    let oSectionADMObject = {
      added: [],
      deleted: [],
      modified: []
    };

    CS.forEach(aNewSections, function(oNewSection){
      let oOldSection = CS.remove(aOldSections, function(oSection){
        return oSection.id == oNewSection.id
      });
      oOldSection = oOldSection[0];

      //if section found in old version
      if(oOldSection){
        let oElementADMObject = {
          added: [],
          deleted: [],
          modified: []
        };
        let aOldSectionElements = oOldSection.elements;
        let aNewSectionElements = oNewSection.elements;

        let bIsSectionModified = (!CS.isEqual(oNewSection.sequence, oOldSection.sequence) || !CS.isEqual(oNewSection.isSkipped, oOldSection.isSkipped));
        oNewSection.isModified = bIsSectionModified;

        //iterating on new section elements
        CS.forEach(aNewSectionElements, function(oNewSectionElement){
          let oOldSectionElement = CS.remove(aOldSectionElements, {id: oNewSectionElement.id});

          //if element found in old version
          if(oOldSectionElement.length > 0){

            //if element is not modified
            let bIsElementModified = !CS.isEqual(oOldSectionElement[0], oNewSectionElement);
            if (bIsElementModified) {
              if (oNewSectionElement.type == "tag") {
                oNewSectionElement.addedDefaultValues = CS.differenceBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId");
                oNewSectionElement.deletedDefaultValues = CS.map(CS.differenceBy(oOldSectionElement[0].defaultValue, oNewSectionElement.defaultValue, "tagId"), "tagId");
                oNewSectionElement.modifiedDefaultValues = CS.filter(
                    CS.intersectionBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId"),
                    function (oDefaultValue) {
                      let oOldDefaultValue = CS.find(oOldSectionElement[0].defaultValue, {tagId: oDefaultValue.tagId});
                      return oOldDefaultValue && oOldDefaultValue.relevance != oDefaultValue.relevance;
                    }
                );
                delete oNewSectionElement.defaultValue;
              }
              else if(oNewSectionElement.type == "attribute"){
                let sAttributeType = oNewSectionElement.attribute.type;
                if(ContentUtils.isAttributeTypeHtml(sAttributeType)){
                  oNewSectionElement.defaultValue = oNewSectionElement.defaultValue.hasOwnProperty("htmlValue") ?
                      oNewSectionElement.defaultValue.htmlValue : oNewSectionElement.defaultValue;
                }
              }

              oElementADMObject.modified.push(oNewSectionElement);
              oADM.modifiedElements.push(oNewSectionElement);
              oNewSectionElement.isModified = bIsElementModified;
            }
          } else {
            //not found in old so add to added list
            oNewSectionElement.id = oNewSectionElement.id.split(sSplitter)[0];
            oElementADMObject.added.push(oNewSectionElement);
          }
        });

        let aDeletedElements = CS.map(aOldSectionElements, 'id');
        CS.merge(oElementADMObject.deleted, aDeletedElements);

        oNewSection.addedElements = oElementADMObject.added;
        oNewSection.deletedElements = oElementADMObject.deleted;
        oNewSection.modifiedElements = oElementADMObject.modified;
        delete oNewSection.elements;

        if (bIsSectionModified) {
          oSectionADMObject.modified.push(oNewSection);
        }

      } else {
        oSectionADMObject.added.push(oNewSection);
      }
    });

    let aDeletedSections = CS.map(aOldSections, 'id');
    CS.merge(oSectionADMObject.deleted, aDeletedSections);

    oADM.addedSections = oSectionADMObject.added;
    oADM.deletedSections = oSectionADMObject.deleted;
    oADM.modifiedSections = [];
    CS.forEach(oSectionADMObject.modified, function (oModifiedSection) {
      oADM.modifiedSections.push({
        id: oModifiedSection.id,
        sequence: oModifiedSection.sequence,
        isSkipped: oModifiedSection.isSkipped,
        isModified: oModifiedSection.isModified
      });
      oADM.modifiedElements = oADM.modifiedElements.concat(oModifiedSection.modifiedElements);
    });

    oADM.addedAppliedKlasses = [];
    oADM.deletedAppliedKlasses = [];

    let oFilterData = {};
    oFilterData.addedTags = [];
    oFilterData.deletedTags = [];
    oFilterData.addedAttributes = [];
    oFilterData.deletedAttributes = [];
    oADM.appliedFilterData = oFilterData;

    let oSortData = {};
    oSortData.addedAttributes = [];
    oSortData.deletedAttributes = [];
    oADM.appliedSortData = oSortData;

    oADM.addedAppliedDefaultFilters = [];
    oADM.deletedAppliedDefaultFilters = [];
    oADM.modifiedAppliedDefaultFilters = [];

    let oActiveTaxonomy = oFilterProps.getTaxonomyActiveClass();
    oADM.id = oActiveTaxonomy.id;
    oADM.label = oActiveTaxonomy.label;
    oADM.icon = oActiveTaxonomy.icon;
    oADM.parent = oActiveTaxonomy.parent;
    oADM.code = oActiveTaxonomy.code;

    return oADM;
  };

  oPrototype.generateADMForSectionsInClass = function(){
    let oFilterProps = this.getFilterProps();
    let oClassToReturn = oFilterProps.getTaxonomyActiveClass();
    let oTaxonomySection = oFilterProps.getTaxonomyNodeSections();

    let sSplitter = ContentUtils.getSplitter();
    let oTaxonomySectionClone = CS.cloneDeep(oTaxonomySection);
    if(!oTaxonomySectionClone.clonedObject){
      return null;
    }

    let aOldSections = oTaxonomySectionClone.sections;
    let aNewSections = oTaxonomySectionClone.clonedObject.sections;

    oClassToReturn.modifiedElements = [];
    let oSectionADMObject = {
      added: [],
      deleted: [],
      modified: []
    };

    CS.forEach(aNewSections, function(oNewSection){
      let oOldSection = CS.remove(aOldSections, function(oSection){
        return oSection.id == oNewSection.id
      });
      oOldSection = oOldSection[0];

      //if section found in old version
      if(oOldSection){
        let oElementADMObject = {
          added: [],
          deleted: [],
          modified: []
        };
        let aOldSectionElements = oOldSection.elements;
        let aNewSectionElements = oNewSection.elements;

        let bIsSectionModified = !CS.isEqual(oNewSection.sequence, oOldSection.sequence) || !CS.isEqual(oNewSection.isSkipped, oOldSection.isSkipped);
        oNewSection.isModified = bIsSectionModified;

        //iterating on new section elements
        CS.forEach(aNewSectionElements, function(oNewSectionElement){

          //TO handle HTML value change
          if(oNewSectionElement.defaultValue){
            if(oNewSectionElement.defaultValue.hasOwnProperty("htmlValue")){
              oNewSectionElement.defaultValue = oNewSectionElement.defaultValue.htmlValue;
            }else if(oNewSectionElement.defaultValue.hasOwnProperty("valueAsHtml")){
              oNewSectionElement.defaultValue = oNewSectionElement.defaultValue.valueAsHtml;
            }
          }

          let oOldSectionElement = CS.remove(aOldSectionElements, {id: oNewSectionElement.id});

          //if element found in old version
          if(oOldSectionElement.length > 0){

            //if element is not modified
            let bIsElementModified = !CS.isEqual(oOldSectionElement[0], oNewSectionElement);
            if (bIsElementModified) {
              if (oNewSectionElement.type == "tag") {
                oNewSectionElement.addedDefaultValues = CS.differenceBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId");
                oNewSectionElement.deletedDefaultValues = CS.map(CS.differenceBy(oOldSectionElement[0].defaultValue, oNewSectionElement.defaultValue, "tagId"), "tagId");
                oNewSectionElement.modifiedDefaultValues = CS.filter(
                    CS.intersectionBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId"),
                    function (oDefaultValue) {
                      let oOldDefaultValue = CS.find(oOldSectionElement[0].defaultValue, {tagId: oDefaultValue.tagId});
                      return oOldDefaultValue && oOldDefaultValue.relevance != oDefaultValue.relevance;
                    }
                );
                delete oNewSectionElement.defaultValue;
              }

              oElementADMObject.modified.push(oNewSectionElement);
              oNewSectionElement.isModified = bIsElementModified;
            }
          } else {
            //not found in old so add to added list
            oNewSectionElement.id = oNewSectionElement.id.split(sSplitter)[0];
            oElementADMObject.added.push(oNewSectionElement);
          }
        });

        let aDeletedElements = CS.map(aOldSectionElements, 'id');
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
        oSectionADMObject.added.push(oNewSection);
      }
    });

    let aDeletedSections = CS.map(aOldSections, 'id');
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

    oClassToReturn.deletedAllowedTypes = [];
    oClassToReturn.addedAllowedTypes = [];

    oClassToReturn.addedDataRules = [];
    oClassToReturn.deletedDataRules = [];
    delete oClassToReturn.dataRules;

    oClassToReturn.addedEvents = [];
    oClassToReturn.deletedEvents = [];
    delete oClassToReturn.events;

    oClassToReturn.addedLifecycleStatusTags = [];
    oClassToReturn.deletedLifecycleStatusTags = [];
    delete oClassToReturn.lifeCycleStatusTags;

    oClassToReturn.addedContexts = {
      "embeddedVariantContexts": [],
      "productVariantContexts": [],
      /**TODO: Need to remove**/
      "promotionalVersionContexts": [],
    };
    oClassToReturn.deletedContexts = {
      "embeddedVariantContexts": [],
      "productVariantContexts": [],
      /**TODO: Need to remove**/
      "promotionalVersionContexts": [],
    };
    delete oClassToReturn.contexts;

    oClassToReturn.deletedRelationships = [];
    oClassToReturn.modifiedRelationships = [];
    oClassToReturn.addedRelationships = [];
    delete oClassToReturn.relationships;

    delete oClassToReturn.permission;
    delete oClassToReturn.notificationSettings;

    return oClassToReturn;
  };

  oPrototype.translateClassForServer = function (oClassForServer) {
    if (!oClassForServer) {
      return 0;
    }

    delete oClassForServer.attributes;
    delete oClassForServer.relationships;
    delete oClassForServer.children;
    delete oClassForServer.tags;
    delete oClassForServer.isDirty;

    delete oClassForServer.sections;

    return oClassForServer;
  };

  oPrototype.getUrlBasedOnClassType = function (sClassType) {
    var sUrl = "config/klasses";
    if(sClassType == MockDataForEntityBaseTypesDictionary.taskKlassBaseType) {
      sUrl = "config/task";
    } else if(sClassType == MockDataForEntityBaseTypesDictionary.assetKlassBaseType) {
      sUrl = 'config/assets';
    } else if(sClassType == MockDataForEntityBaseTypesDictionary.marketKlassBaseType) {
      sUrl = 'config/targets';
    } else if(sClassType == MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType) {
      sUrl = 'config/textassets';
    } else if(sClassType == MockDataForEntityBaseTypesDictionary.supplierKlassBaseType) {
      sUrl = 'config/suppliers';
    }

    return sUrl;
  };

  oPrototype.handleSaveFilterTaxonomySection = function () {
    let oFilterProps = this.getFilterProps();
    let oClickedNode = oFilterProps.getTaxonomySettingIconClickedNode();
    let sUrl = "";
    let oClassToSave = {};
    if(CS.isEmpty(oClickedNode.type)){
      sUrl = "config/articletaxonomy";
      oClassToSave = this.generateADMForTaxonomy();
    } else if (oClickedNode.type === TaxonomyBaseTypeDictionary.masterTaxonomy) {
      sUrl = "config/attributiontaxonomy";
      oClassToSave = this.generateADMForTaxonomy();
    } else {
      oClassToSave = this.generateADMForSectionsInClass();
      oClassToSave = this.translateClassForServer(oClassToSave);
      sUrl = this.getUrlBasedOnClassType(oClassToSave.type);
    }

    if(!oClassToSave){
      alertify.message(getTranslation().SCREEN_STORE_SAVE_NOTHING_CHANGED);
      return;
    }

    let fSuccess = this.successSaveFilterTaxonomySection.bind(this);
    let fFailure = this.failureGenericFunction.bind(this, "failureSaveFilterTaxonomySection") ;
    CS.postRequest(sUrl, {}, oClassToSave, fSuccess, fFailure)
  };

  oPrototype.successSaveFilterTaxonomySection = function (oResponse) {
    let oSuccess = oResponse.success;
    let oEntity = oSuccess.entity;

    let oFilterProps = this.getFilterProps();
    let aSections = oEntity.sections;

    ContentUtils.initializeSectionExpansionState(aSections);

    oFilterProps.setTaxonomyNodeSections({sections: aSections});
    oFilterProps.setTaxonomyActiveClass(oEntity);

    alertify.success(getTranslation().SUCCESSFULLY_SAVED);
    this.triggerChange();
  }

  oPrototype.handleDiscardFilterTaxonomySection = function () {
    let oFilterProps = this.getFilterProps();
    let oTaxonomySectionData = oFilterProps.getTaxonomyNodeSections();
    if(!oTaxonomySectionData.clonedObject){
      alertify.message(getTranslation().NOTHING_CHANGED_TO_DISCARD);
    } else {
      delete oTaxonomySectionData.clonedObject;
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    }

    this.triggerChange();
  };

  oPrototype.makeTaxonomySectionDirty = function () {
    let oFilterProps = this.getFilterProps();
    let oTaxonomySectionData = oFilterProps.getTaxonomyNodeSections();

    if(!oTaxonomySectionData.clonedObject){
      oTaxonomySectionData.clonedObject = CS.cloneDeep(oTaxonomySectionData);
    }
    return oTaxonomySectionData.clonedObject.sections
  };

  oPrototype.handleTaxonomySectionInputChanged = function (sSectionId, sElementId, sProperty, sNewValue) {
    let aSections = this.makeTaxonomySectionDirty();
    let oSection = CS.find(aSections, {id: sSectionId});
    let oElement = CS.find(oSection.elements, {id: sElementId});
    if (sProperty == "propertyType" && oElement && oElement.type == "tag") {
      sProperty = "tagType";
    }
    oElement[sProperty] = sNewValue;
    if (sNewValue && CS.has(sNewValue, 'htmlValue')) {
      oElement.valueAsHtml = sNewValue.htmlValue;
      oElement.defaultValue = sNewValue.textValue;
    }

    let aViolatingMandatoryFields = ContentUtils.getViolatingMandatoryFields(aSections);
    let oFilterProps = ContentUtils.getComponentProps().filterProps;
    oFilterProps.setViolatingMandatoryElements(aViolatingMandatoryFields);

    this.triggerChange();
  };

  oPrototype.updateTaxonomySectionTagRelevance = function (sSectionId, sElementId, oModel) {
    let aSections = this.makeTaxonomySectionDirty();
    let oSection = CS.find(aSections, {id: sSectionId});
    let oElement = CS.find(oSection.elements, {id: sElementId});
    let sTagId = oModel.id;
    let iNewRelevance = oModel.properties['iNewValue'];
    let oTagValue = CS.find(oElement.defaultValue, {tagId: sTagId});
    if (!oTagValue) {
      oTagValue = {
        tagId: sTagId,
        relevance: 0
      };
      oElement.defaultValue.push(oTagValue);
    }
    oTagValue.relevance = iNewRelevance;
    this.triggerChange();
  };

  oPrototype.addTaxonomySectionDefaultTagValue = function (sSectionId, sElementId, sTagGroupId, aTagValueIds) {
    if(CS.includes(aTagValueIds, MSSViewConstants.MSS_NOTHING_FOUND_ID)){
      return;
    }

    let aSections = this.makeTaxonomySectionDirty();
    let oSection = CS.find(aSections, {id: sSectionId});
    let oElement = CS.find(oSection.elements, {id: sElementId});
    oElement.defaultValue = [];
    let aDefaultValue = oElement.defaultValue;
    CS.forEach(aTagValueIds, function (sTagValueId) {
      aDefaultValue.push({
        relevance: 100,
        tagId: sTagValueId
      });
    });
    this.triggerChange();
  };

  oPrototype.removeTaxonomySectionDefaultTagValue = function (sSectionId, sElementId, sTagGroupId, sTagValueId) {
    let aSections = this.makeTaxonomySectionDirty();
    let oSection = CS.find(aSections, {id: sSectionId});
    let oElement = CS.find(oSection.elements, {id: sElementId});
    let aDefaultValue = oElement.defaultValue;
    CS.remove(aDefaultValue, {tagId: sTagValueId});
    this.triggerChange();
  };

  oPrototype.addTagInContentFromUniqueSearchForTaxonomySections = function (sSectionId, sElementId, sTagValueId) {
    let aSections = this.makeTaxonomySectionDirty();
    let oSection = CS.find(aSections, {id: sSectionId});
    let oElement = CS.find(oSection.elements, {id: sElementId});
    oElement.defaultValue = [{
      tagId: sTagValueId,
      relevance: 100
    }];
  };

  oPrototype.removeChildPropsFromVisualProps = function (aChildList) {
    let oFilterProps = this.getFilterProps();
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();
    CS.forEach(aChildList, function (oChild) {
      delete oVisualProps[oChild.id];
    });
  };

  oPrototype.updateRootLevelTaxonomyCount = function (aUpdatedTaxonomyTree) {
    let oFilterProps = this.getFilterProps();
    let aTaxonomyTree = oFilterProps.getTaxonomyTree();
    CS.forEach(aTaxonomyTree, function (oTaxonomy) {
      let oNewTaxonomy = CS.find(aUpdatedTaxonomyTree, {id: oTaxonomy.id});
      if(oNewTaxonomy) {
        oTaxonomy.count = oNewTaxonomy.count;
      }
    });
  };

  oPrototype.setAllAffectedTreeNodeIds = function (aIds) {
    this.getFilterProps().setAllAffectedTreeNodeIds(aIds);
  };

  oPrototype.handleTreeCheckClicked = function (iId, iLevel, bCrossClicked) {
    let oFilterProps = this.getFilterProps();
    let aTreeList = oFilterProps.getTaxonomyTree();
    oFilterProps.setIsFilterDirty(true);
    if (iLevel === 1) {
      //Todo: check and remove the condition (-1) if not required
      let sParentId = iId == "-1" ? "" : iId;
      oFilterProps.setSelectedOuterParentId(sParentId);
      oFilterProps.setAllAffectedTreeNodeIds([]);
      return;
    } else {
      // added 999 in array as first level dom in taxonomy hierarchy has id 999 in FltrHorizontalTreeView
      let aAllAffectedNodeIds = [999];
      this.toggleHeaderNodeStateRecursively(aTreeList, iId, null, aAllAffectedNodeIds, bCrossClicked);
      oFilterProps.setAllAffectedTreeNodeIds(aAllAffectedNodeIds);
    }
    this.triggerChange();
  };

  oPrototype.setSelectedTaxonomyIds = function () {
    let oFilterProps = this.getFilterProps();
    let aTreeList = oFilterProps.getTaxonomyTree();
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();
    this.clearTaxonomyTreeBackup();
    let aSelectedIds = [];
    this.setSelectedTaxonomyIdsRecursively(aTreeList, oVisualProps, aSelectedIds);
    oFilterProps.setSelectedParentTaxonomyIds(aSelectedIds);
  };

oPrototype.setSelectedNodeIdsRecursively = function (aSelectedNodeIds, aSelectedParentNodeIds, aTreeData, oViewModel, bIsParentSelected) {
  let _this = this;

  CS.forEach(aTreeData, function (oTreeNode) {
    if (oViewModel[oTreeNode.id].isChecked == 2) {
      !bIsParentSelected && aSelectedParentNodeIds.push(oTreeNode.id);
      aSelectedNodeIds.push(oTreeNode.id);
    }
    _this.setSelectedNodeIdsRecursively(aSelectedNodeIds, aSelectedParentNodeIds, oTreeNode.children, oViewModel, oViewModel[oTreeNode.id].isChecked === 2);
  });
};

oPrototype.setSelectedTypeIds = function () {
  let oFilterProps = this.getFilterProps();
  let aTreeList = oFilterProps.getTaxonomyTree();
  let oVisualProps = oFilterProps.getTaxonomyVisualProps();
  let aSelectedNodeIds = [];
  let aSelectedParentNodeIds = [];
  let oRootClassNode = CS.find(aTreeList, {id: "-1"});
  this.setSelectedNodeIdsRecursively(aSelectedNodeIds, aSelectedParentNodeIds, oRootClassNode.children, oVisualProps, false);
  oFilterProps.setSelectedTypes(aSelectedNodeIds);
  oFilterProps.setSelectedParentTypeIds(aSelectedParentNodeIds);
};

  oPrototype.handleSortByItemClicked = function (sId, bMultiSelect) {

    this.handleFilterSortDeactivatedItemClicked(sId, bMultiSelect);
    let oFilterProps = this.getFilterProps();
    let aSortData = oFilterProps.getAvailableSortData();

    let aSortItems = CS.remove(aSortData, {sortField: sId});
    if(aSortItems.length) {
      aSortData.unshift(aSortItems[0]);
    }

    this.triggerChange();
  };

  oPrototype.handleSelectedTaxonomiesClearAllClicked = function (bIsNoTrigger) {
    let oFilterProps = this.getFilterProps();
    let _this = this;

    CS.forEach(oFilterProps.getTaxonomyTree(), function (oKlass) {
      let oAssignObj = {isChecked: false, isHidden: true};
      if(oKlass.id == "-1"){
        oAssignObj.isExpanded = true;
        oAssignObj.isHidden = false;
      }
      _this.assignToAllNodesBelow(oAssignObj, oKlass, _this.getTaxonomyVisualProps());
    });
    this.clearSelectedTaxonomisFromProps();
    //Required for bookmark, when taxonomy tree data is empty
    let oTaxonomyTreeFlatMap = oFilterProps.getTaxonomyTreeFlatMap();
    let aDefaultTaxonomyTree = oFilterProps.getDefaultTaxonomyTree();
    oTaxonomyTreeFlatMap["-1"] = oTaxonomyTreeFlatMap["-1"] || aDefaultTaxonomyTree[0];

    if(!bIsNoTrigger){
      this.triggerChange();
    }
  };

  oPrototype.handleFilterShowDetailsClicked = function (oCallbackData, oExtraData = {}) {
    let oFilterProps = this.getFilterProps();
    let oComponentProps = ContentUtils.getComponentProps();
    if (!oFilterProps.getShowDetails() || oExtraData.isLoadMore) {
      let bIsForTableContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
      let bIsAdvancedFilterApplied = oFilterProps.getIsAdvanceSearchFilterClickedStatus();
      let oAppliedAttrAndTags = this.makeFilterDataForAppliedAttributeAndTags(oFilterProps.getAppliedFilters(), bIsAdvancedFilterApplied);
      let sSelectedEntity = oComponentProps.variantSectionViewProps.getSelectedEntity();

      let oReqExtraData = {};
      let oReqData = {
        tags: oAppliedAttrAndTags.tags,
        attributes: oAppliedAttrAndTags.attributes,
        moduleId: ContentUtils.getSelectedModulesForFilter(),
        allSearch: oFilterProps.getSearchText(),
        isBookmark: false
      };
      if(ContentUtils.getIsArchive()){
        oReqData["isArchivePortal"] = true;
      }
      CS.assign(oReqData, oExtraData);
      let sUrl = getRequestMapping().GetSortAndFilterData;

      if (ContentUtils.getIsStaticCollectionScreen()) {
        oReqData.collectionId = CollectionViewProps.getActiveCollection().id;
        sUrl = getRequestMapping().GetFilterAndSortForStaticCollection;
        if (ContentUtils.getAvailableEntityViewStatus()) {
          oReqData.isQuicklist = true;
        }
      } else if(bIsForTableContext && oCallbackData.filterContext.screenContext === "quickList"){
        oReqData = CS.omit(oReqData, 'moduleId');
        oReqData.moduleEntities = [sSelectedEntity];
        this.fillInstanceIdAndBaseTypeForContext(oReqData);
        sUrl = getRequestMapping().GetSortAndFilterDataForVariantQuicklist;
      } else if (oCallbackData.filterContext.screenContext === "quickList") {
        if (ContentUtils.getIsVariantQuicklist()) {
          sUrl = getRequestMapping().GetSortAndFilterDataForVariantQuicklist;
          this.fillInstanceIdAndBaseTypeForContext(oReqData);
          oReqData = CS.omit(oReqData, 'moduleId');
          oReqData.moduleEntities = [sSelectedEntity];
          ContentUtils.fillIdsToExcludeForVariantQuicklist(oReqData);
        } else {
          sUrl = getRequestMapping().GetSortAndFilterDataForRelationshipQuicklist;
          ContentUtils.getAdditionalDataForRelationshipCalls(oReqData);
        }
      } else if (oCallbackData.filterContext.screenContext === "matchAndMerge") {
        sUrl = getRequestMapping().GetSortAndFilterDataForGoldenRecord;
      }
      else if (ContentUtils.getIsDynamicCollectionScreen()) {
        oReqData.isBookmark = true;
        oCallbackData.isBookmark = oReqData.isBookmark;
      }
      let fSuccess = this.successFetchSortAndFilterData.bind(this, oCallbackData);
      let fFailure = this.failureFetchSortAndFilterData.bind(this, "failureFetchSortAndFilterData");
      CS.postRequest(sUrl, {}, oReqData, fSuccess, fFailure, null, oReqExtraData);
    } else {
      oFilterProps.setShowDetails(!oFilterProps.getShowDetails());
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      this.triggerChange();
    }
  };

  oPrototype.handleClearAllAppliedFilterClicked = function (bIsNoTrigger) {
    this.clearAllAppliedFilter();

    if(!bIsNoTrigger){
      this.triggerChange();
    }
  };

  oPrototype.checkDefaultKlassTaxonomy = function (aClassTaxonomyTreeData) {
    let oFilterProps = this.getFilterProps();
    let oVisualProps = oFilterProps.getTaxonomyVisualProps();
    let sId = "-1";
    oVisualProps[sId].isChecked =  2;
    oVisualProps[sId].isExpanded =  true;
    oVisualProps[sId].isHidden =  false;
    this.checkAndEnableAllNodes(aClassTaxonomyTreeData);
  };

  oPrototype.handleTreeNodeToggleClicked = function (sId) {
    let oTreeNodeToToggle = this.getKlassTreeNodeById(sId);
    if (!CS.isEmpty(oTreeNodeToToggle.children)) {
      oTreeNodeToToggle.isExpanded = !oTreeNodeToToggle.isExpanded;
      this.triggerChange();
    }
  };

  oPrototype.handleTaxonomySearchTextChanged = function (sSearchText) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setTaxonomySearchText(sSearchText);
    if(sSearchText == '') {
      oFilterProps.setMatchingTaxonomyIds([]);
    }else{
      this.addMatchingTaxonomyIds(sSearchText);
    }
    oFilterProps.setAllAffectedTreeNodeIds([]);
    this.triggerChange();
  };

  oPrototype.handleTaxonomyCancelButtonClicked = function () {
    let oFilterProps = this.getFilterProps();
    if(!CS.isEmpty(oFilterProps.getTaxonomyTreeBackup())) {
      this.restoreTaxonomyTreeBackup();
    } else {
      oFilterProps.setSelectedOuterParentId('');
      this.resetAllTaxonomyRootNodeOtherThanSelectedTaxonomy();
    }
    this.clearTaxonomyTreeBackup();
    oFilterProps.setOldSelectedOuterParentId("");
    this.triggerChange();
  };

  oPrototype.handleTaxonomyClearAllClicked = function () {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setIsFilterDirty(true);
    this.uncheckAndShowAllCurrentTaxonomyNodes();
    this.triggerChange();
  };

  oPrototype.resetModuleSelection = function () {
    ContentUtils.resetModuleSelection();
  };

  oPrototype.setModuleSelectedById = function (sId) {
    ContentUtils.setSelectedModuleById(sId);
  };

  oPrototype.getFilterInfo = function () {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.getFilterInfo();
  };

  oPrototype.setFilterInfo = function (oFilterInfo) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setFilterInfo(oFilterInfo);
  };

  oPrototype.getTaxonomySearchText = function () {
    let oFilterProps = this.getFilterProps();
    return oFilterProps.getTaxonomySearchText();
  };

  oPrototype.setSelectedOuterParentId = function (_selectedOuterParentId) {
    this.getFilterProps().setSelectedOuterParentId(_selectedOuterParentId);
  };

  oPrototype.getTaxonomyTreeBackup = function () {
    return this.getFilterProps().getTaxonomyTreeBackup();
  };

  oPrototype.handleTaxonomyShowPopOverStateChanged = function (bShowTaxonomy) {
    let oFilterProps = this.getFilterProps();
    oFilterProps.setIsSelectTaxonomyPopOverVisible(bShowTaxonomy);
    this.triggerChange();
  };

/**
 * @function resetTaxonomyRelatedData
 * @description - Reset taxonomies inner level(children) data and visual props.
 */
oPrototype.resetTaxonomyRelatedData =  function () {
  let _this = this;
  let aTreeList = _this.getTaxonomyTree();
  let oOldProps = {};
  CS.forEach(aTreeList, function (oTree) {
    /** To reset taxonomies children data**/
    oTree.children = [];

    /** To reset taxonomies children visual props data**/
    oOldProps[oTree.id] = {
      isChecked: 0, // values can be  0, 1 or 2 (0: if no children node is selected, 1 : some children nodes are selected, 2: all children nodes are selected)
      isExpanded: false, // To show children nodes of tree
      isHidden: true // To show taxonomy node if count is > 0 & hide if count is 0)
    };
  });
  _this.setTaxonomyVisualProps(oOldProps);
};

/**
 * @function updateTagDataFromAppliedFilters
 * @description - Required to update applied filter tag values(On data language change tag value label should get updated with latest data language)
 * @param aReferencedTags - referenced tag data
 */
oPrototype.updateTagDataFromAppliedFilters = function (aReferencedTags) {
  let oFilterProps = this.getFilterProps();
  let aAppliedFilters = oFilterProps.getAppliedFilters();
  CS.forEach(aReferencedTags, (oTag) => {
    let oAppliedTag = CS.find(aAppliedFilters, {id: oTag.id});
    if (oAppliedTag) {
      let aAppliedTagChildren = oAppliedTag.children;
      CS.forEach(aAppliedTagChildren, (oChild) => {
        let oReferencedTagChild = CS.find(oTag.children, {id: oChild.id});
        oChild.label = CS.getLabelOrCode(oReferencedTagChild);
      });
    }
  });
};

oPrototype.discardSelectedFilters = function (oExtraData) {
  let oFilterProps = this.getFilterProps(oExtraData);
  oFilterProps.setIsFilterDirty(false);
  oFilterProps.setAppliedFiltersClone(null);
  this.triggerChange();
};

oPrototype.deleteChildIdRecursively = function (aSelectedTypeIds, oTaxonomy) {
  CS.forEach(oTaxonomy.children, (oChild) => {
    if (CS.includes(aSelectedTypeIds, oChild.id)) {
      let index = CS.indexOf(aSelectedTypeIds, oChild.id);
      aSelectedTypeIds.splice(index, 1);
    }
    this.deleteChildIdRecursively(aSelectedTypeIds, oChild.children);
  })
};

MicroEvent.mixin(NewFilterStore);

export default NewFilterStore;
