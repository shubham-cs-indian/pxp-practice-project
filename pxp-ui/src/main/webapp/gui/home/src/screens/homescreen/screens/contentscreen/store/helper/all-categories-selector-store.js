import ClassesAndTaxonomyStore from "./classes-and-taxonomies-store";
import MicroEvent from "../../../../../../libraries/microevent/MicroEvent";
import CS from "../../../../../../libraries/cs";
import TreeViewStore from "../../../../../../viewlibraries/treeviewnew/store/tree-view-store";
import TreeViewProps from "../../../../../../viewlibraries/treeviewnew/store/model/tree-view-props";
import ScreenModeUtils from "./screen-mode-utils";
import ContentUtils from "./content-utils";
import ContextualAllCategoriesProps from "../model/contextual-all-categories-selector-view-props";
import {ActionButtonConstants} from "../../../../../../viewlibraries/treeviewnew/store/model/tree-view-action-buttons";
import FilterStoreFactory from "./filter-store-factory";
import CategoriesConstantDictionary from "../../../../../../commonmodule/tack/categories-constant-dictionary";

const getTranslation = ScreenModeUtils.getTranslationDictionary;

class AllCategoriesSelectorStore extends ClassesAndTaxonomyStore {

  triggerChange = () => {
    obj.trigger('all-categories-taxonomies-change');
  };

  fetchCategories = (sId, oFilterContext, oTaxonomyTreeRequestData, oExtraData = {}) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps
        .getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let sSearchText = contextualAllCategoriesProps.getTreeSearchText();
    let sParentId = oExtraData.parentId;
    let iFrom = 0;
    let iSize = 20;
    let aTreeData = TreeViewProps.getTreeData();

    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);

    let oPostDataForFilter = oFilterStore.createGetAllInstancesData();
    oPostDataForFilter.searchText = sSearchText;
    oPostDataForFilter.selectedCategory = sId;
    delete oPostDataForFilter.isFilterDataRequired;
    oPostDataForFilter.clickedTaxonomyId = sParentId || contextualAllCategoriesProps.getClickedTaxonomyId()
        || null;
    if (CS.isEmpty(sSearchText) && oExtraData.isLoadMore) {
      iFrom = aTreeData.length;
      if (sParentId) {
        let oTreeNode = this.getTreeNodeById(aTreeData, sParentId);
        iFrom = oTreeNode.children.length;
      }
    }
    else if (sSearchText) {
      iSize = 10;
      if (oExtraData.isLoadMore) {
        aTreeData = contextualAllCategoriesProps.getSearchViewTreeData()[oExtraData.loadMoreCategoryId];
        iFrom = aTreeData.length;
        oPostDataForFilter.selectedCategory = oExtraData.loadMoreCategoryId
      }
    }
    oPostDataForFilter.from = iFrom;
    oPostDataForFilter.size = iSize;

    let {aSelectedTypes, aSelectedTaxonomyIds} = this.getSelectedTaxonomyIdsAndClassIdsAccordingToDirtyState(oFilterContext);
    oPostDataForFilter.selectedTypes = aSelectedTypes;
    oPostDataForFilter.selectedTaxonomyIds = aSelectedTaxonomyIds;
    oPostDataForFilter.isArchivePortal = ContentUtils.getIsArchive();

    let {url:sURL, ...oExtraPayLoad} = oTaxonomyTreeRequestData;
    CS.assign(oPostDataForFilter, oExtraPayLoad);

    let fSuccess = this.successFetchAllCategoriesTreeData.bind(this, oFilterContext, oExtraData);
    let fFailure = this.failureFetchAllCategoriesTreeData;
    return CS.postRequest(sURL, {}, oPostDataForFilter, fSuccess, fFailure, null, {});
  };

  handleCategoriesButtonClicked = (sId, oFilterContext, oTaxonomyTreeRequestData) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps
        .getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.setSelectedCategoryID(sId);
    contextualAllCategoriesProps.setClickedTaxonomyId(null);

    this.fetchCategories(sId, oFilterContext, oTaxonomyTreeRequestData);
  };

  successFetchAllCategoriesTreeData = (oFilterContext, oExtraData, oResponse) => {

    let oSuccess = oResponse.success;
    let sScreenContext = oFilterContext.screenContext;
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(sScreenContext);
    let sSearchText = contextualAllCategoriesProps.getTreeSearchText();
    let oConfigDetails = oSuccess.configDetails;
    this.prepareSelectedCategoriesSummaryData(oFilterContext, oConfigDetails);

    if (CS.isEmpty(sSearchText)) {
      let sSelectedCategoryId = contextualAllCategoriesProps.getSelectedCategoryID();
      let sClickedTaxonomyId = contextualAllCategoriesProps.getClickedTaxonomyId();
      let oCategoryData = oSuccess[sSelectedCategoryId][0] || {};
      let aTreeData = oCategoryData.children || [];
      let sParentId = oExtraData.parentId;

      if ((oExtraData.isLoadMore && sParentId) || sClickedTaxonomyId) {
          let aSelectedCheckboxIds = this.getSelectedIds(oFilterContext);
          let oExtraProperties = {
            loadMore: oExtraData.isLoadMore,
            checkBoxData: {
              canCheck: true,
              canUnCheck: true,
              checkedItems: aSelectedCheckboxIds,
            },
            referenceElements: {},
            nodeId: sParentId || sClickedTaxonomyId,
            totalCount: oCategoryData.totalChildrenCount,
          };
          let oActionButtons = this.getActionButtonsForTreeView();

          TreeViewStore.updateDataOnTreeNodeClick(aTreeData, oExtraProperties
              , oActionButtons);
      }
      else {
        if (oExtraData.isLoadMore) {
          aTreeData = TreeViewProps.getTreeData();
          aTreeData.push.apply(aTreeData, oCategoryData.children || [])
        }
        this.updateTreeVisualProps(aTreeData, oFilterContext);
        let sNodeId = oExtraData.parentId || sClickedTaxonomyId || sSelectedCategoryId;
        if (sSelectedCategoryId === CategoriesConstantDictionary.TAXONOMIES &&
            sNodeId === CategoriesConstantDictionary.TAXONOMIES) {
          sNodeId = "-1";
        }
        TreeViewStore.updateLoadMoreMap(oCategoryData.totalChildrenCount,
            aTreeData.length, sNodeId);
      }
    }
    else {
      this.prepareSearchViewTreesResponse(oExtraData, oSuccess, oFilterContext);
    }
    contextualAllCategoriesProps.setIsAllCategoriesTaxonomiesPopOverVisible(true);
    this.triggerChange();
  };

  prepareSelectedCategoriesSummaryData = (oFilterContext, oConfigDetails = {}) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let oReferencedClasses = oConfigDetails.referencedKlasses;
    let oReferencedTaxonomies = oConfigDetails.referencedTaxonomies;
    let aNatureClassesData = [];
    let aAttributionClassesData = [];
    let aTaxonomiesData = [];
    let {aSelectedTypes, aSelectedTaxonomyIds} = this.getSelectedTaxonomyIdsAndClassIdsAccordingToDirtyState(oFilterContext);
    CS.forEach(aSelectedTypes, (sKlassId) => {
      let oReferencedClass = oReferencedClasses[sKlassId];
      let oSummaryNode = {
        actionButtons: TreeViewStore.getActionButtons(this.getActionButtonsForSummaryTreeView(), oReferencedClass),
        icon: oReferencedClass.icon,
        iconKey: oReferencedClass.iconKey,
        id: oReferencedClass.id,
        label: CS.getLabelOrCode(oReferencedClass),
        parentId: oReferencedClass.parentId,
        toolTip: CS.getLabelOrCode(oReferencedClass),
      };
      if (CS.isEmpty(oReferencedClass.natureType)) {
        aAttributionClassesData.push(oSummaryNode);
      }
      else {
        aNatureClassesData.push(oSummaryNode);
      }
    });

    CS.forEach(aSelectedTaxonomyIds, (sTaxonomyId) => {
      let oReferencedTaxonomy = oReferencedTaxonomies[sTaxonomyId];
      let oSummaryNode = {
        actionButtons: TreeViewStore.getActionButtons(this.getActionButtonsForSummaryTreeView(), oReferencedTaxonomy),
        icon: oReferencedTaxonomy.icon,
        iconKey: oReferencedTaxonomy.iconKey,
        id: oReferencedTaxonomy.id,
        label: oReferencedTaxonomy.label,
        parentId: oReferencedTaxonomy.parentId,
        toolTip: ContentUtils.getTaxonomyPath(CS.getLabelOrCode(oReferencedTaxonomy), oReferencedTaxonomy.parentId,
            oReferencedTaxonomies),
      };
      aTaxonomiesData.push(oSummaryNode);
    });
    let oSummaryData = {};
    CS.isNotEmpty(aNatureClassesData) && (oSummaryData[CategoriesConstantDictionary.NATURE_CLASSES] = aNatureClassesData);
    CS.isNotEmpty(aAttributionClassesData) && (oSummaryData[CategoriesConstantDictionary.ATTRIBUTION_CLASSES] = aAttributionClassesData);
    CS.isNotEmpty(aTaxonomiesData) && (oSummaryData[CategoriesConstantDictionary.TAXONOMIES] = aTaxonomiesData);
    contextualAllCategoriesProps.setSummaryTreeData(oSummaryData);
  };

  getSelectedTaxonomyIdsAndClassIdsAccordingToDirtyState = (oFilterContext) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    let aSelectedClassIds = [];
    let aSelectedTaxonomyIds = [];
    if (!contextualAllCategoriesProps.getIsAllCategoriesTaxonomySelectorPopDirty()) {
      aSelectedClassIds = oFilterProps.getSelectedTypes();
      aSelectedTaxonomyIds = oFilterProps.getSelectedTaxonomyIds();
    }
    else {
      let oCategoriesVsSelectedIds = oFilterStore.getCategoriesVsSelectedIds();
      aSelectedClassIds.push.apply(aSelectedClassIds, oCategoriesVsSelectedIds[CategoriesConstantDictionary.NATURE_CLASSES]);
      aSelectedClassIds.push.apply(aSelectedClassIds, oCategoriesVsSelectedIds[CategoriesConstantDictionary.ATTRIBUTION_CLASSES]);
      aSelectedTaxonomyIds = oCategoriesVsSelectedIds[CategoriesConstantDictionary.TAXONOMIES];
    }
    return {aSelectedTypes: aSelectedClassIds, aSelectedTaxonomyIds};
  };

  prepareSearchViewTreesResponse (oExtraData, oSuccess, oFilterContext) {
    let sScreenContext = oFilterContext.screenContext;
    let oConfigDetails = oSuccess.configDetails || {};
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(sScreenContext);
    let aTreeData = [];
    let aCategories = [CategoriesConstantDictionary.NATURE_CLASSES, CategoriesConstantDictionary.ATTRIBUTION_CLASSES,
                       CategoriesConstantDictionary.TAXONOMIES];
    let oReferencedTaxonomies = contextualAllCategoriesProps.getReferencedTaxonomies();
    CS.assign(oReferencedTaxonomies, oConfigDetails.referencedTaxonomies);
    contextualAllCategoriesProps.setReferencedTaxonomies(oReferencedTaxonomies);

    if (oExtraData.isLoadMore) {
      let oSearchViewTreeData = contextualAllCategoriesProps.getSearchViewTreeData();
      let oLoadMoreMap = oSearchViewTreeData.loadMoreMap;
      aTreeData = oSearchViewTreeData[oExtraData.loadMoreCategoryId];
      let oLoadedCategory = oSuccess[oExtraData.loadMoreCategoryId][0] || {};
      aTreeData.push.apply(aTreeData, oLoadedCategory.children || []);
      let sLoadMoreMapKey = oExtraData.loadMoreCategoryId;
      if (oExtraData.loadMoreCategoryId === CategoriesConstantDictionary.TAXONOMIES) {

        this.updateParentId(oLoadedCategory, oReferencedTaxonomies);
        sLoadMoreMapKey = "-1";
      }
      this.updateTreeVisualProps(aTreeData, oFilterContext, oReferencedTaxonomies);
      oLoadMoreMap[sLoadMoreMapKey] = oLoadedCategory.totalChildrenCount > aTreeData.length;
    }
    else {
      let oLoadMoreMap = {};
      let oSearchViewTreeData = {
        loadMoreMap: oLoadMoreMap
      };
      CS.forEach(aCategories, (sCategory) => {
        let oCategoryData = oSuccess[sCategory][0];
        if (!oCategoryData) {
          return;
        }
        aTreeData = oCategoryData.children;
        let sLoadMoreMapKey = sCategory;
        if (sCategory === CategoriesConstantDictionary.TAXONOMIES) {
          this.updateParentId(oCategoryData, oReferencedTaxonomies);
          sLoadMoreMapKey = "-1"
        }
        this.updateTreeVisualProps(aTreeData, oFilterContext, oReferencedTaxonomies);
        oLoadMoreMap[sLoadMoreMapKey] = oCategoryData.totalChildrenCount > aTreeData.length;
        oSearchViewTreeData[sCategory] = aTreeData;
      });
      contextualAllCategoriesProps.setSearchViewTreeData(oSearchViewTreeData);
    }
  }

  updateParentId (oLoadedCategory, oReferencedElements) {
    CS.forEach(oLoadedCategory.children, (oChild) => {
      let oReferencedTaxonomy = CS.find(oReferencedElements, {id: oChild.id})
      oChild.parentId = oReferencedTaxonomy.parentId;
    })
  }

  failureFetchAllCategoriesTreeData = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureFetchCreateButtonList', getTranslation());
  };

  updateTreeVisualProps = (aList, oFilterContext, oReferenceElements) => {
    let oPermissions = {
      canAdd: true,
      canRemove: true
    };

    let aSelectedCheckboxIds = this.getSelectedIds(oFilterContext);

    let oActionButtons = this.getActionButtonsForTreeView();
    let oExtraProperties = {
      checkBoxData: {
        canCheck: oPermissions.canAdd,
        canUnCheck: oPermissions.canRemove,
        checkedItems: aSelectedCheckboxIds,
      },
      referenceElements: oReferenceElements,
      nodeId: "-1",
    };
    TreeViewStore.updateVisualPropsInTreeData(aList, oActionButtons, oExtraProperties)
  };

  getSelectedIds (oFilterContext) {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let aSelectedCheckboxIds = [];
    let oSummaryTreeViewData = contextualAllCategoriesProps.getSummaryTreeData();
    oSummaryTreeViewData = oSummaryTreeViewData.clonedObject || oSummaryTreeViewData;
    let sSearchText = contextualAllCategoriesProps.getTreeSearchText();

    if (CS.isEmpty(sSearchText)) {
      let sSelectedCategoryId = contextualAllCategoriesProps.getSelectedCategoryID();
        aSelectedCheckboxIds = this._getSelectedNodeIdsFromTreeData(oSummaryTreeViewData[sSelectedCategoryId]);
    }
    else {
      CS.forEach(oSummaryTreeViewData, (oSummaryData) => {
        aSelectedCheckboxIds.push.apply(aSelectedCheckboxIds, this._getSelectedNodeIdsFromTreeData(oSummaryData));
      });
    }
    return aSelectedCheckboxIds;
  }

  _getSelectedNodeIdsFromTreeData (oSummaryData) {
    let aSelectedCheckboxIds = [];
    CS.forEach(oSummaryData, function (oData) {
      aSelectedCheckboxIds.push(oData.id);
    });
    return aSelectedCheckboxIds;
  }

  getActionButtonsForSummaryTreeView = () => {
    return {
      rightSideActionButtonIds: [ActionButtonConstants.REMOVE],
      leftSideActionButtonIds: [],
    }
  };

  getActionButtonsForTreeView = () => {
    return {
      rightSideActionButtonIds: [ActionButtonConstants.EXPANDED],
      leftSideActionButtonIds: [ActionButtonConstants.CHECKBOX],
    }
  };

  handleSummaryClearSelectionButtonClicked = (oFilterContext) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    oFilterProps.setIsFilterDirty(true);
    let aSummaryData = contextualAllCategoriesProps.getSummaryTreeData();
    aSummaryData =  aSummaryData.clonedObject || aSummaryData;
    let sSearchText = contextualAllCategoriesProps.getTreeSearchText();
    let bIsPopOverDirty = contextualAllCategoriesProps.getIsAllCategoriesTaxonomySelectorPopDirty();

    CS.forEach(aSummaryData, function (aData, key) {
      CS.forEach(aData, function (oData) {
        if (CS.isNotEmpty(sSearchText)) {
          let aSearchViewTreeData = contextualAllCategoriesProps.getSearchViewTreeData()[key];
          TreeViewStore.treeNodeCheckboxClicked(oData.id, aSearchViewTreeData);
        } else {
          TreeViewStore.treeNodeCheckboxClicked(oData.id);
        }
      });
    });
    aSummaryData.clonedObject ={};
    contextualAllCategoriesProps.setSummaryTreeData(aSummaryData);
    !bIsPopOverDirty && contextualAllCategoriesProps.setIsAllCategoriesTaxonomySelectorPopDirty(true);
    this.triggerChange();
  };

  handleSummaryCrossButtonClicked = (sContext, sButtonId, sSelectedId, oFilterContext) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    oFilterProps.setIsFilterDirty(true);
    let sSearchText = contextualAllCategoriesProps.getTreeSearchText();
    let aTreeData = CS.isNotEmpty(sSearchText) ? contextualAllCategoriesProps.getSearchViewTreeData()[sContext] : TreeViewProps.getTreeData();

    let oSummaryDataCloned = this.makeAllCategoriesSelectorDataDirty(oFilterContext);
    let sSelectedCategoriesId = contextualAllCategoriesProps.getSelectedCategoryID();

    if (sSelectedCategoriesId === sContext || CS.isNotEmpty(sSearchText)) {
      this.updateTreeNodeCheckboxRecursively(aTreeData, sSelectedId)
    }
    let aSelectedCategorySummaryData = oSummaryDataCloned[sContext];

    if (CS.find(aSelectedCategorySummaryData, {id: sSelectedId})){
      contextualAllCategoriesProps.setIsAllCategoriesTaxonomySelectorPopDirty(true);
      delete CS.remove(aSelectedCategorySummaryData, {id: sSelectedId});
    }

    if (CS.isEmpty(aSelectedCategorySummaryData)) {
      delete oSummaryDataCloned[sContext];
    }
    this.triggerChange();
  };

  updateTreeNodeCheckboxRecursively = (aTree, sSelectedId) => (
    CS.forEach(aTree, (oTreeData) => {
      if (oTreeData.id === sSelectedId) {
        TreeViewStore.treeNodeCheckboxClicked(sSelectedId, aTree);
        return false;
      }
      if (CS.isNotEmpty(oTreeData.children)) {
        this.updateTreeNodeCheckboxRecursively(oTreeData.children, sSelectedId);
      }
    })
  );

  makeAllCategoriesSelectorDataDirty = (oFilterContext) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let oSummaryViewData = contextualAllCategoriesProps.getSummaryTreeData();
    if (!oSummaryViewData.clonedObject) {
      oSummaryViewData.clonedObject = CS.cloneDeep(oSummaryViewData);
    }
    return oSummaryViewData.clonedObject;
  };

  handleAllCategoriesSelectorTreeNodeCheckboxClicked = (sContext, sCheckedNodeId, oFilterContext) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps
        .getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    oFilterProps.setIsFilterDirty(true);
    let sSearchText = contextualAllCategoriesProps.getTreeSearchText();
    let aTreeData = CS.isNotEmpty(sSearchText) ? contextualAllCategoriesProps.getSearchViewTreeData()[sContext] : TreeViewProps.getTreeData();
    let oSummaryDataCloned = this.makeAllCategoriesSelectorDataDirty(oFilterContext);
    let aSelectedCategorySummaryData = oSummaryDataCloned[sContext] || [];

    if (!CS.find(aSelectedCategorySummaryData, {id: sCheckedNodeId})) {
      let oSelectedSummaryViewNode = TreeViewStore.getTreeNodeById(aTreeData, sCheckedNodeId);
      let oSummaryNode = {
        actionButtons: TreeViewStore.getActionButtons(this.getActionButtonsForSummaryTreeView(), oSelectedSummaryViewNode),
        icon: oSelectedSummaryViewNode.icon,
        iconKey: oSelectedSummaryViewNode.iconKey,
        id: oSelectedSummaryViewNode.id,
        label: oSelectedSummaryViewNode.label,
        parentId: oSelectedSummaryViewNode.parentId,
        toolTip: oSelectedSummaryViewNode.toolTip,
        customIconClassName: oSelectedSummaryViewNode.customIconClassName,
      };
      aSelectedCategorySummaryData.push(oSummaryNode);
      oSummaryDataCloned[sContext] = aSelectedCategorySummaryData
    } else {
      CS.remove(aSelectedCategorySummaryData, {id: sCheckedNodeId});
      if (CS.isEmpty(aSelectedCategorySummaryData)) {
        delete oSummaryDataCloned[sContext];
      }
    }

    TreeViewStore.treeNodeCheckboxClicked(sCheckedNodeId, aTreeData);

    contextualAllCategoriesProps.setIsAllCategoriesTaxonomySelectorPopDirty(true);
    this.triggerChange();
  };

  handleAllCategoriesSelectorTreeNodeClicked = (sClickedNodeId, oFilterContext, oTaxonomyTreeRequestData) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps
        .getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.setClickedTaxonomyId(sClickedNodeId);
    let sId = contextualAllCategoriesProps.getSelectedCategoryID();
    this.fetchCategories(sId, oFilterContext, oTaxonomyTreeRequestData);
  };

  handleAllCategoriesSelectorCancelButtonClicked = (oFilterContext) => {
    this.resetDirtyProps(oFilterContext);
    this.triggerChange();
  };

  handleAllCategoriesTreeViewSearched = (oFilterContext, sSearchText, oTaxonomyTreeRequestData) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps
        .getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.setTreeSearchText(sSearchText);
    contextualAllCategoriesProps.setClickedTaxonomyId(null);
    let sSelectedCategory = contextualAllCategoriesProps.getSelectedCategoryID();
    let sCategoryToSearch = sSearchText && "all" || sSelectedCategory;
    this.fetchCategories(sCategoryToSearch, oFilterContext, oTaxonomyTreeRequestData);
  };

  handleLoadMoreCategoriesClicked = (sCategoryId, sParentId, oTaxonomyTreeRequestData,
                                     oFilterContext) => {
    let oExtraData = {
      isLoadMore: true,
    };
    let contextualAllCategoriesProps = ContextualAllCategoriesProps
        .getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.setClickedTaxonomyId(null);

    let aCategories = [CategoriesConstantDictionary.NATURE_CLASSES, CategoriesConstantDictionary.ATTRIBUTION_CLASSES,
                       "-1"];
    if (!CS.includes(aCategories, sParentId)) {
      oExtraData.parentId = sParentId;
    }
    else {
      oExtraData.loadMoreCategoryId = sParentId === "-1" ? CategoriesConstantDictionary.TAXONOMIES : sParentId;
    }

    this.fetchCategories(sCategoryId, oFilterContext, oTaxonomyTreeRequestData, oExtraData);
  };

  resetDirtyProps = (oFilterContext) => {
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    let aSummaryViewTreeData = contextualAllCategoriesProps.getSummaryTreeData();
    delete aSummaryViewTreeData.clonedObject;
    contextualAllCategoriesProps.setTreeSearchText("");
    contextualAllCategoriesProps.setIsAllCategoriesTaxonomiesPopOverVisible(false);
    contextualAllCategoriesProps.setIsAllCategoriesTaxonomySelectorPopDirty(false);
  };

}

let obj = new AllCategoriesSelectorStore();
MicroEvent.mixin(obj);
export default obj;
