import ClassesAndTaxonomyStore from "./classes-and-taxonomies-store";
import MicroEvent from "../../../../../../libraries/microevent/MicroEvent";
import ContentScreenProps from "../model/content-screen-props";
import CS from "../../../../../../libraries/cs";
import TreeViewStore from "../../../../../../viewlibraries/treeviewnew/store/tree-view-store";
import TreeViewProps from "../../../../../../viewlibraries/treeviewnew/store/model/tree-view-props";
import MultiClassificationDialogToolbarLayoutData from "../../tack/multiclassification-dialog-toolbar-layout-data";
import ScreenModeUtils from "./screen-mode-utils";
import ContentUtils from "./content-utils";
import TaxonomyTypeDictionary from "../../../../../../commonmodule/tack/taxonomy-type-dictionary";
import alertify from "../../../../../../commonmodule/store/custom-alertify-store";
import {getTranslations as getTranslation} from "../../../../../../commonmodule/store/helper/translation-manager";

const MultiClassificationTypeConstants = new MultiClassificationDialogToolbarLayoutData()["multiClassificationViewTypesIds"];
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

class NewMinorTaxonomyStoreStore extends ClassesAndTaxonomyStore {

  triggerChange = () => {
    obj.trigger('classes-and-taxonomies-change');
  };

  fetchMultiClassificationViewData = (sTaxonomyId, oExtraData) => {
    if (oExtraData.isForEmbedded) {
      let oActiveEmbProperty = ContentScreenProps.screen.getActiveEmbProperty();
      let oMinorTaxonomiesDataModel = oActiveEmbProperty.model.multiClassificationViewData;
      this.setMinorTaxonomiesDataForEmbedded(oActiveEmbProperty);
      this.getMinorTaxonomiesTreeData(sTaxonomyId)
          .then(this.prepareMinorTaxonomiesTreeData.bind(this, oMinorTaxonomiesDataModel))
          .then(() => {
            oMinorTaxonomiesDataModel.showClassificationDialog = true;
            this.triggerChange();
          });
      return;
    }
    let oMinorTaxonomiesData = this.getActiveMinorTaxonomiesData(sTaxonomyId);
    this.prepareMinorTaxonomyDialogDataInSectionElement(sTaxonomyId);
    this.getMinorTaxonomiesTreeData(sTaxonomyId)
        .then(this.prepareMinorTaxonomiesTreeData.bind(this, oMinorTaxonomiesData))
        .then(this.triggerChange);
  };

  prepareMinorTaxonomyDialogDataInSectionElement = (sTaxonomyId) => {
    let MultiClassificationProps = ContentScreenProps.multiClassificationViewProps;
    let sSelectedTabInClassificationDialog = MultiClassificationProps.getSelectedTabInClassificationDialog();

    let oMinorTaxonomiesData = this.getActiveMinorTaxonomiesData(sTaxonomyId);
    oMinorTaxonomiesData.showClassificationDialog = true;
    MultiClassificationProps.setMultiClassificationData(oMinorTaxonomiesData.multiClassificationData);

    let oMultiClassificationData = this.makeMultiClassificationDataDirty();

    oMinorTaxonomiesData.classificationDialogData = {
      selectedTabIdInClassificationDialog: sSelectedTabInClassificationDialog,
      multiClassificationData: oMultiClassificationData,
      isDirty: false,
      multiClassificationTreeData: {}
    };
  };

  prepareMinorTaxonomiesTreeData = (oMinorTaxonomiesData) => {
    let oClassificationDialogData = oMinorTaxonomiesData.classificationDialogData;
    let sSearchText = oClassificationDialogData.multiClassificationTreeData.searchText;
    oClassificationDialogData.multiClassificationTreeData = {
      treeData: TreeViewProps.getTreeData(),
      searchText: sSearchText,
      treeLoadMoreMap: TreeViewProps.getTreeLoadMoreMap(),
      context: "minorTaxonomiesTreeView",
      activeNodeId: oMinorTaxonomiesData.id
    }
  };

  setMinorTaxonomiesDataForEmbedded = (oActiveEmbProperty) => {
    let MultiClassificationProps = ContentScreenProps.multiClassificationViewProps;
    let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
    let aSelectedTaxonomies = [];
    let aSelectedOptions = oActiveEmbProperty.model.selectedOptions;
    if (CS.isNotEmpty(aSelectedOptions)) {
      CS.forEach(aSelectedOptions, (oData) => {
        aSelectedTaxonomies.push(oData.id);
      })
    }
    else {
      aSelectedTaxonomies = oActiveEmbProperty.selectedTaxonomies;
    }
    let oReferencedTaxonomies = MultiClassificationProps.getReferencedTaxonomyMap();

    oMultiClassificationData.taxonomies = {};


    if (CS.isNotEmpty(aSelectedTaxonomies)) {
      oMultiClassificationData.taxonomies = ContentUtils.prepareSelectedTaxonomiesDataForChipsView(oReferencedTaxonomies, aSelectedTaxonomies);
    }

    let sSelectedTabInClassificationDialog = MultiClassificationProps.getSelectedTabInClassificationDialog();

    let oMinorTaxonomiesData = oActiveEmbProperty.model.multiClassificationViewData;
    oMultiClassificationData = this.makeMultiClassificationDataDirty();

    oMinorTaxonomiesData.classificationDialogData = {
      selectedTabIdInClassificationDialog: sSelectedTabInClassificationDialog,
      multiClassificationData: oMultiClassificationData,
      isDirty: false,
      multiClassificationTreeData: {}
    };
  };

  getMinorTaxonomiesTreeData = (sTaxonomyId) => {
    let MultiClassificationProps = ContentScreenProps.multiClassificationViewProps;
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedTaxonomies = oScreenProps.getReferencedTaxonomies();
    let oMinorTaxonomiesData = CS.find(oReferencedTaxonomies, {id: sTaxonomyId});
    /* first time pencil icon click tree node data level 1 */
    let oTree = {
      code: oMinorTaxonomiesData.code,
      icon: oMinorTaxonomiesData.icon,
      id: oMinorTaxonomiesData.id,
      parentId: "-1",
      label: oMinorTaxonomiesData.label
    };
    let aTree = [];
    aTree.push(oTree);

    let oReferencedTaxonomiesMap = MultiClassificationProps.getReferencedTaxonomyMap();
    CS.assign(oReferencedTaxonomiesMap, CS.keyBy(aTree, "id"));

    TreeViewStore.updateLoadMoreMap(1, 20, oTree.parentId);
    this.updateTreeVisualProps(aTree, MultiClassificationTypeConstants.TAXONOMIES, sTaxonomyId);
    return this.taxonomyTreeNodeClicked(sTaxonomyId, false);
  };

  minorTaxonomyTreeNodeClicked = (sTreeNodeId, bLoadMore) => {
    this.taxonomyTreeNodeClicked(sTreeNodeId, bLoadMore)
        .then(this.triggerChange);
  };

  updateTreeVisualProps = (aList, sContext, sTaxonomyId, bIsSearchValue) => {
    let MultiClassificationProps = ContentScreenProps.multiClassificationViewProps;
    let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
    oMultiClassificationData = oMultiClassificationData.clonedObject || oMultiClassificationData;
    let aCheckedItems = CS.keys(oMultiClassificationData[sContext]);
    aCheckedItems = CS.remove(aCheckedItems, function (sId) {
      return sId !== sTaxonomyId;
    });
    let oActionButtons = this.getActionButtonsForMultiClassificationView();
    let oExtraProperties = {
      checkBoxData: {
        canCheck: bIsSearchValue ? true : false,
        canUnCheck: bIsSearchValue ? true : false,
        checkedItems: aCheckedItems,
      },
      referenceElements: MultiClassificationProps.getReferencedTaxonomyMap()
    };
    TreeViewStore.updateVisualPropsInTreeData(aList, oActionButtons, oExtraProperties)
  };

  setMinorTaxonomiesData = (sTaxonomyElementId) => {
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedTaxonomies = oScreenProps.getReferencedTaxonomies();
    let oReferencedRootTaxonomy = oReferencedTaxonomies[sTaxonomyElementId];
    let oMinorTaxonomiesData = {
      "id": sTaxonomyElementId
    };
    let oReferencedElements = oScreenProps.getReferencedElements();
    let oMinorTaxonomyReferencedElement = oReferencedElements[sTaxonomyElementId];
    let bCanEdit = !oMinorTaxonomyReferencedElement.isDisabled;

    let aTaxonomyIds = CS.map(oReferencedRootTaxonomy.children, function (oTaxonomy) {
      return oTaxonomy.id;
    });
    aTaxonomyIds.push(sTaxonomyElementId);
    let aAppliedTaxonomyIds = CS.intersection(aTaxonomyIds, oScreenProps.getActiveTaxonomyIds());

    oMinorTaxonomiesData.showClassificationDialog = false;
    oMinorTaxonomiesData.showEditClassificationIcon = bCanEdit && ContentUtils.isContentAvailableInSelectedDataLanguage();

    oMinorTaxonomiesData.multiClassificationData = {
      "taxonomies": {}
    };
    if (CS.isNotEmpty(aAppliedTaxonomyIds)) {
      oMinorTaxonomiesData.multiClassificationData.taxonomies = this.prepareSelectedTaxonomiesDataForChipsView(oReferencedTaxonomies, aAppliedTaxonomyIds);
    }
    return oMinorTaxonomiesData;
  };

  getActiveMinorTaxonomiesData = (sTaxonomyId, sContext) => {
    let oScreenProps = ContentScreenProps.screen;
    if (sContext === "minorTaxonomiesInEmbeddedTable" || sContext === "minorTaxonomiesDialogContextInEmbeddedTable") {
      return oScreenProps.getActiveEmbProperty().model.multiClassificationViewData;
    }
    let oActiveSection = oScreenProps.getActiveSections();
    let oData = {};
    CS.forEach(oActiveSection, function (oPropertyCollection) {
      CS.forEach(oPropertyCollection.elements, function (oElement) {
        if (oElement.id === sTaxonomyId) {
          return oData = oElement;
        }
        CS.forEach(oElement.elements, function (oInnerElement) {
          CS.forEach(oInnerElement.elements, function (oElement) {
            if (oElement.id === sTaxonomyId) {
              return oData = oElement
            }
          })
        })
      })
    });
    return oData.minorTaxonomiesData;
  };

  IsMinorTaxonomySelectionAllowed = (oMultiClassificationData, sCheckedNodeId, sTaxonomyId) => {
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedElements = oScreenProps.getReferencedElements();
    let oReferencedElement = oReferencedElements[sTaxonomyId];
    let oClonedSelectedTaxonomy = oMultiClassificationData.clonedObject && oMultiClassificationData.clonedObject.taxonomies ||
        oMultiClassificationData.taxonomies;
    if(!oClonedSelectedTaxonomy[sCheckedNodeId]) {
      let bIsMinorTaxonomySelected = CS.values(oClonedSelectedTaxonomy).length >= 1;
      if (CS.isNotEmpty(oReferencedElement) && !oReferencedElement.isMultiselect && bIsMinorTaxonomySelected) {
        return false;
      }
    }
    return true;
  };

  handleMinorTaxonomyTreeNodeCheckboxClicked = (sCheckedNodeId, sTaxonomyId, sContext) => {
   let oActiveMinorTaxonomiesData = this.getActiveMinorTaxonomiesData(sTaxonomyId, sContext);
    let oMultiClassificationData = oActiveMinorTaxonomiesData.multiClassificationData;
    let bIsAllowed = this.IsMinorTaxonomySelectionAllowed(oMultiClassificationData, sCheckedNodeId, sTaxonomyId);
    if(!bIsAllowed) {
      alertify.error(getTranslation().MULTIPLE_SELECTIONS_NOT_ALLOWED);
      return;
    }
    oActiveMinorTaxonomiesData.classificationDialogData.isDirty = true;
    TreeViewStore.treeNodeCheckboxClicked(sCheckedNodeId);
    this.updateChipsDataOnTreeCheckboxClick(sCheckedNodeId);
    this.triggerChange();
  };

  handleMinorTaxonomyCrossIconClicked = (sLeafNodeId, sIdToRemove, sTaxonomyId, sContext) => {
    let oActiveMinorTaxonomiesData = this.getActiveMinorTaxonomiesData(sTaxonomyId, sContext);
    oActiveMinorTaxonomiesData.classificationDialogData.isDirty = true;
    this.updateMinorTaxonomyChipsDataOnCrossIconClicked(sLeafNodeId, sIdToRemove);
    this.triggerChange();
  };

  updateMinorTaxonomyChipsDataOnCrossIconClicked = (sLeafNodeId, sIdToRemove) => {
    let oMultiClassificationData = this.makeMultiClassificationDataDirty();
    let oSelectedTaxonomies = oMultiClassificationData.taxonomies;
    let aSelectedTaxonomiesPath = oSelectedTaxonomies[sLeafNodeId];

    let aIndexOfTaxonomyToRemove = CS.findIndex(aSelectedTaxonomiesPath, {id: sIdToRemove});
    let oNewSelectedLeafNode = null;
    //To deselect Tree node
    TreeViewStore.treeNodeCheckboxClicked(sLeafNodeId);
    if (aIndexOfTaxonomyToRemove) {
      let aNewSelectedTaxonomyPath = CS.slice(aSelectedTaxonomiesPath, 0, aIndexOfTaxonomyToRemove);
      oNewSelectedLeafNode = CS.last(aNewSelectedTaxonomyPath);
      // Need to select previous tree node if it is not selected
      !oSelectedTaxonomies[oNewSelectedLeafNode.id] && TreeViewStore.treeNodeCheckboxClicked(oNewSelectedLeafNode.id);

      if (oNewSelectedLeafNode.parentId != "-1") {
        oSelectedTaxonomies[oNewSelectedLeafNode.id] = aNewSelectedTaxonomyPath;
      }
    }
    delete oSelectedTaxonomies[sLeafNodeId];
  };

  handleMinorTaxonomiesTreeSearchClicked = (sSearchText, sActiveNodeId) => {
    let oMinorTaxonomiesData = this.getActiveMinorTaxonomiesData(sActiveNodeId);

    oMinorTaxonomiesData.classificationDialogData.multiClassificationTreeData.searchText = sSearchText;
    if (sSearchText) {
      return this.fetchMinorTaxonomies(oMinorTaxonomiesData.id, sSearchText)
          .then(this.prepareMinorTaxonomiesTreeData.bind(this, oMinorTaxonomiesData))
          .then(this.triggerChange);
    }
    else {
      this.getMinorTaxonomiesTreeData(sActiveNodeId)
          .then(this.prepareMinorTaxonomiesTreeData.bind(this, oMinorTaxonomiesData))
          .then(this.triggerChange);
    }
  };

  fetchMinorTaxonomies = (sTaxonomyId, sSearchText, bLoadMore = false) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oPaginationData = oComponentProps.screen.getAllowedTaxonomiesPaginationData();
    let aTree = TreeViewProps.getTreeData();
    oPaginationData.from = bLoadMore ? aTree.length : 0;

    let oData = {
      baseType: oActiveEntity.baseType,
      selectionType: TaxonomyTypeDictionary.MINOR_TAXONOMY,
      idsToExclude: [],
      id: sTaxonomyId
    };
    oPaginationData.searchText = sSearchText || "";
    CS.assign(oData, oPaginationData);

    let oCallbackData = {
      "taxonomyId": sTaxonomyId,
      "searchText": sSearchText
    };
    let fSuccess = this.successFetchAllowedTaxonomies.bind(this, bLoadMore, oCallbackData);

    return CS.postRequest(getRequestMapping().AllowedTypes, {}, oData, fSuccess, this.failureFetchAllowedTaxonomies);
  };

  handleTreeViewLoadMoreClicked = (sParentTaxonomyId, sActiveNodeId) => {
    let bLoadMore = true;

    let oMinorTaxonomiesData = this.getActiveMinorTaxonomiesData(sActiveNodeId);
    let sSearchText = oMinorTaxonomiesData.classificationDialogData.multiClassificationTreeData.searchText;

    if(sParentTaxonomyId === "-1")
    {
      return this.fetchMinorTaxonomies(oMinorTaxonomiesData.id,sSearchText, bLoadMore)
          .then(this.prepareMinorTaxonomiesTreeData.bind(this, oMinorTaxonomiesData))
          .then(this.triggerChange);
    }
    return this.minorTaxonomyTreeNodeClicked(sParentTaxonomyId, bLoadMore);
  };

  handleMinorTaxonomiesApplyButtonClicked = (oCallback) => {
    oCallback.isMinorTaxonomySwitch = true;
    this.applyClassesOrTaxonomies(oCallback)
        .then(this.triggerChange);
  };

  prepareSelectedTaxonomiesDataForChipsView = function (oReferencedTaxonomies, aSelectedTaxonomyIds) {
    let oTaxonomyMap = {};
    CS.forEach(oReferencedTaxonomies, function (oReferencedTaxonomy) {
      ContentUtils.prepareReferencedTaxonomyMap(oReferencedTaxonomy, oTaxonomyMap);
    });

    let oTaxonomies = {};
    CS.forEach(aSelectedTaxonomyIds, (sTaxonomyId) => {
      let aTaxonomy = [];
      this.prepareSelectedTaxonomiesFromMap(sTaxonomyId, aTaxonomy, oTaxonomyMap);
      oTaxonomies[sTaxonomyId] = (CS.reverse(aTaxonomy));
    });

    return oTaxonomies;
  };

  prepareSelectedTaxonomiesFromMap = (sTaxonomyId, aSelectedTaxonomies, oTaxonomyMap) => {
    if(oTaxonomyMap[sTaxonomyId]) {
      let oTaxonomy = CS.cloneDeep(oTaxonomyMap[sTaxonomyId]);
      oTaxonomy.canRemove = true;
      oTaxonomy.toolTip = ContentUtils.getTaxonomyPath(CS.getLabelOrCode(oTaxonomy), oTaxonomy.parentId, oTaxonomyMap);
      aSelectedTaxonomies.push(oTaxonomy);
      let sParentId = oTaxonomy.parentId;

      if(!CS.isEmpty(sParentId) && sParentId != -1) {
        this.prepareSelectedTaxonomiesFromMap(sParentId, aSelectedTaxonomies, oTaxonomyMap);
      }
    }
  };

}

let obj = new NewMinorTaxonomyStoreStore();
MicroEvent.mixin(obj);
export default obj;
