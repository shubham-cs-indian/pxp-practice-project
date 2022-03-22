import MultiClassificationProps from "../model/multiclassification-view-props";
import CS from "../../../../../../libraries/cs";
import TreeViewStore from "../../../../../../viewlibraries/treeviewnew/store/tree-view-store";
import TreeViewProps from "../../../../../../viewlibraries/treeviewnew/store/model/tree-view-props";
import ContentUtils from "./content-utils";
import CommonUtils from "../../../../../../commonmodule/util/common-utils";
import {ActionButtonConstants} from "../../../../../../viewlibraries/treeviewnew/store/model/tree-view-action-buttons";
import MultiClassificationDialogToolbarLayoutData from "../../tack/multiclassification-dialog-toolbar-layout-data";
import ScreenModeUtils from "./screen-mode-utils";
import ContentScreenProps from "../model/content-screen-props";
const MultiClassificationTypeConstants = new MultiClassificationDialogToolbarLayoutData()["multiClassificationViewTypesIds"];
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
const getTranslation = ScreenModeUtils.getTranslationDictionary;

class ClassesAndTaxonomiesStore {

  triggerChange = () => {
    ClassesAndTaxonomiesStore.trigger('classes-and-taxonomies-change');
  };

  openMultiClassificationDialog = (sTabId, sTaxonomyId, oExtraData = {}) => {
    MultiClassificationProps.setIsShowClassificationDialog(true);
    MultiClassificationProps.setSelectedTabInClassificationDialog(sTabId);
    this.fetchMultiClassificationViewData(sTaxonomyId, oExtraData);
  };

  resetMultiClassificationProps = (oCallback) => {
    MultiClassificationProps.setMultiClassificationData({});
    MultiClassificationProps.setIsShowClassificationDialog(false);
    MultiClassificationProps.setSelectedTabInClassificationDialog(MultiClassificationTypeConstants.CLASSES);
    MultiClassificationProps.setMultiClassificationTreeSearchText("");
    MultiClassificationProps.resetTaxonomyInheritanceProps();

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
  };

  makeMultiClassificationDataDirty = () => {
    let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
    if (!CS.isEmpty(oMultiClassificationData)) {
      if (!oMultiClassificationData.clonedObject) {
        oMultiClassificationData.clonedObject = CS.cloneDeep(oMultiClassificationData);
      }
      return oMultiClassificationData.clonedObject;
    }
    return null;
  };

  updateChipsDataOnTreeCheckboxClick = (sTaxonomyId) => {
    let oMultiClassificationData = this.makeMultiClassificationDataDirty();
    let oSelectedTaxonomies = oMultiClassificationData.taxonomies;
    let bIsChecked = CS.isEmpty(oSelectedTaxonomies[sTaxonomyId]);

    if (bIsChecked) {
      let oReferencedTaxonomy = MultiClassificationProps.getReferencedTaxonomyMap();
      let oChipsViewModel = ContentUtils.prepareSelectedTaxonomiesDataForChipsView(oReferencedTaxonomy, [sTaxonomyId]);
      CS.assign(oSelectedTaxonomies, oChipsViewModel);
    }
    else {
      delete oSelectedTaxonomies[sTaxonomyId];
    }
  };

  getTaxonomyType = (oTaxonomy) => {
    if (CS.isEmpty(oTaxonomy.taxonomyType)) {
      return this.getTaxonomyType(oTaxonomy.parent);
    } else {
      return oTaxonomy.taxonomyType;
    }
  };

  getActionButtonsForMultiClassificationView = () => {
    return {
      rightSideActionButtonIds: [ActionButtonConstants.EXPANDED],
      leftSideActionButtonIds: [ActionButtonConstants.CHECKBOX],
    }
  };

  taxonomyTreeNodeClicked = (sTaxonomyId, bLoadMore, oExtraData = {}) => {
    let aMultiClassificationTree = TreeViewProps.getTreeData();
    sTaxonomyId = sTaxonomyId || aMultiClassificationTree[0].id;
    /* if searchText is available in Taxonomy then on node click backend call should not be executed */
    if(MultiClassificationProps.getMultiClassificationTreeSearchText()){
      CS.forEach(aMultiClassificationTree, (oTaxonomyNodeList) => {
        oTaxonomyNodeList.isExpanded = oTaxonomyNodeList.id === sTaxonomyId;
      });
      return CommonUtils.resolveEmptyPromise();
    }
    let fSuccess = this.successSelectTaxonomyTreeNode.bind(this, bLoadMore, oExtraData);
    let fFailure = this.failureSelectTaxonomyTreeNode;
    let oTaxonomy = this.getTreeNodeById(aMultiClassificationTree, sTaxonomyId);
    let oRequestObj = {
      id: sTaxonomyId,
      from: bLoadMore ? CS.size(oTaxonomy.children) : 0,
      size: 20
    };
    let sURL = getRequestMapping().GetTaxonomyHierarchyForSelectedTaxonomy;
    return CS.postRequest(sURL, {}, oRequestObj, fSuccess, fFailure);
  };

  failureSelectTaxonomyTreeNode = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureSelectTaxonomyTreeNode', getTranslation());
  };

  getTreeNodeById = (aTree, sTreeNodeId) => (
      aTree.reduce((oData, oTreeData) => {
        if (oData)
          return oData;
        if (oTreeData.id === sTreeNodeId)
          return oTreeData;
        if (oTreeData.children)
          return this.getTreeNodeById(oTreeData.children, sTreeNodeId)
      }, null)
  );

  successSelectTaxonomyTreeNode = (bLoadMore, oExtraData, oResponse) => {
    let oSuccess = oResponse.success;
    let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
    oMultiClassificationData = oMultiClassificationData.clonedObject || oMultiClassificationData;
    let aCheckedItems = CS.keys(oMultiClassificationData.taxonomies);

    let aChildren = oSuccess.children;
    let bCanCheck = true;
    let bCanUnCheck = true;
    if (oExtraData.checkBoxData) {
      bCanCheck = oExtraData.checkBoxData.canCheck;
      bCanUnCheck = oExtraData.checkBoxData.canUnCheck;
    }
    let oExtraProperties = {
      loadMore: bLoadMore,
      nodeId: oSuccess.id,
      totalCount: oSuccess.count,
      checkBoxData: {
        canCheck: bCanCheck,
        canUnCheck: bCanUnCheck,
        checkedItems: aCheckedItems,
      }
    };
    //TODO: This should come from backend (Need Refactoring)
    this.addParentIdInChildren(aChildren, oSuccess.id);

    let oActionButtonIds = this.getActionButtonsForMultiClassificationView();
    TreeViewStore.updateDataOnTreeNodeClick(aChildren, oExtraProperties, oActionButtonIds);
  };

  addParentIdInChildren = (aChildren, sParentId) => {
    let oReferencedTaxonomies = MultiClassificationProps.getReferencedTaxonomyMap();
    CS.forEach(aChildren, (oChild) => {
      oChild.parentId = sParentId;
      oReferencedTaxonomies[oChild.id] = oChild;
    });
  };

  generateADMForMultiClassification = (oOldMultiClassificationData, oNewMultiClassificationData) => {
    let oADMForClassification = {
      addedSecondaryTypes: [],
      deletedSecondaryTypes: [],
      addedTaxonomyIds: [],
      deletedTaxonomyIds: []
    };

    /***** ADM For Classes ***/
    CS.forEach(oNewMultiClassificationData.classes, (oTaxonomy, sTaxonomyId) => {
      if (!oOldMultiClassificationData.classes[sTaxonomyId]) {
        oADMForClassification.addedSecondaryTypes.push(sTaxonomyId);
      }
    });

    CS.forEach(oOldMultiClassificationData.classes, (oTaxonomy, sTaxonomyId) => {
      if (!oNewMultiClassificationData.classes[sTaxonomyId]) {
        oADMForClassification.deletedSecondaryTypes.push(sTaxonomyId);
      }
    });

    /***** ADM For Taxonomies ***/
    CS.forEach(oNewMultiClassificationData.taxonomies, (oTaxonomy, sTaxonomyId) => {
      if (!oOldMultiClassificationData.taxonomies[sTaxonomyId]) {
        oADMForClassification.addedTaxonomyIds.push(sTaxonomyId);
      }
    });

    CS.forEach(oOldMultiClassificationData.taxonomies, (oTaxonomy, sTaxonomyId) => {
      if (!oNewMultiClassificationData.taxonomies[sTaxonomyId]) {
        oADMForClassification.deletedTaxonomyIds.push(sTaxonomyId);
      }
    });

    return oADMForClassification;
  };

  applyClassesOrTaxonomies = (oCallbackData) => {
    let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
    let oRequestData = this.getRequestDataForTypeSwitch(oMultiClassificationData, oCallbackData);
    let oActiveContent = ContentUtils.getActiveContent();
    let oCallback = !CS.isEmpty(oCallbackData) ? oCallbackData : {};
    let sBaseType = oActiveContent.baseType ? oActiveContent.baseType : oActiveContent.type;
    let sScreenContext = ContentUtils.getScreenModeBasedOnEntityBaseType(sBaseType);
    let sUrl = getRequestMapping(sScreenContext).GetEntityWithTypeChanged;
    let fSuccess = this.successApplyClassificationCallback.bind(this, oCallback);
    let fFailure = this.failureApplyClassificationCallback.bind(this, oCallback);

    return CS.postRequest(sUrl, {}, oRequestData, fSuccess, fFailure);
  };

  getRequestDataForTypeSwitch = (oMultiClassificationData, oCallbackData) => {
    let oNewMultiClassificationData = CS.cloneDeep(oMultiClassificationData.clonedObject);
    delete oMultiClassificationData.clonedObject;

    let oADMForClassification = this.generateADMForMultiClassification(oMultiClassificationData, oNewMultiClassificationData);
    let sSelectedTab = ContentUtils.getSelectedTabId();
    let oSelectedTemplate = ContentScreenProps.screen.getSelectedTemplate();
    let oTemplateDataForServer = ContentUtils.getTemplateAndTypeIdForServer(oSelectedTemplate.id);
    let oActiveContent = ContentUtils.getActiveContent();

    return {
      klassInstanceId: oActiveContent.id,
      isLinked: true,
      natureClassId: oActiveContent.natureKlassId,
      isNatureKlassSwitched: false,
      isMinorTaxonomySwitch: !!oCallbackData.isMinorTaxonomySwitch,
      deletedSecondaryTypes: oADMForClassification.deletedSecondaryTypes || [],
      addedSecondaryTypes: oADMForClassification.addedSecondaryTypes || [],
      deletedTaxonomyIds: oADMForClassification.deletedTaxonomyIds || [],
      addedTaxonomyIds: oADMForClassification.addedTaxonomyIds || [],
      tabType: ContentUtils.getTabTypeFromTabId(sSelectedTab),
      tabId: sSelectedTab,
      typeId: oTemplateDataForServer.typeId,
      templateId: oTemplateDataForServer.templateId,
      isResolved: oCallbackData.isResolved || false
    };
  };

  successApplyClassificationCallback = (oCallback, oResponse) => {
    oResponse = oResponse.success;
    this.resetMultiClassificationProps();
    ContentUtils.showSuccess(ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY, { entity : getTranslation().CONTENT }));

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute(oResponse);
    }
  };

  failureApplyClassificationCallback = (oCallback, oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureApplyClassificationCallback', getTranslation());
  };

  sortSelectedTypesByNatureType = (aTypes) => {
    let aNatureTypes = [];
    let aNonNatureTypes = [];
    CS.forEach(aTypes, (sTypeId) => {
      if(ContentUtils.isNatureClass(sTypeId)) {
        aNatureTypes.push(sTypeId);
      }
      else {
        aNonNatureTypes.push(sTypeId);
      }
    });

    return aNatureTypes.concat(aNonNatureTypes);
  };

  failureFetchAllowedTaxonomies = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureFetchAllowedTaxonomies', getTranslation());
  };

  successFetchAllowedTaxonomies = (bLoadMore, oCallbackData, oResponse) => {
    let oSuccess = oResponse.success;
    let aList = oSuccess.list;
    let iTotalCount = oSuccess.count;
    let aTree = TreeViewProps.getTreeData();
    aTree = bLoadMore ? CS.combine(aTree, aList) : aList;

    let oReferencedTaxonomies = MultiClassificationProps.getReferencedTaxonomyMap();
    CS.assign(oReferencedTaxonomies, CS.keyBy(aTree, "id"));
    if (CS.isNotEmpty(oSuccess.configDetails)) {
      CS.assign(oReferencedTaxonomies, oSuccess.configDetails);
    }

    this.updateTreeVisualProps(aTree, MultiClassificationTypeConstants.TAXONOMIES, oCallbackData.taxonomyId, oCallbackData.searchText);
    TreeViewStore.updateLoadMoreMap(iTotalCount, aTree.length);
  };
}

export default ClassesAndTaxonomiesStore;
