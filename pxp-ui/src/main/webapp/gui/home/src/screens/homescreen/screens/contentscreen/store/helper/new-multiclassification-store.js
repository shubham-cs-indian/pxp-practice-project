import ClassesAndTaxonomyStore from './classes-and-taxonomies-store';
import ContentUtils from "./content-utils";
import CS from "../../../../../../libraries/cs";
import ContentScreenProps from "../model/content-screen-props";
import MultiClassificationProps from "../model/multiclassification-view-props";
import MarkerClassTypeDictionary from "../../../../../../commonmodule/tack/marker-class-type-dictionary";
import ClassNameFromBaseTypeDictionary from "../../../../../../commonmodule/tack/class-name-base-types-dictionary";
import MultiClassificationDialogToolbarLayoutData from "../../tack/multiclassification-dialog-toolbar-layout-data";
import TreeViewProps from "../../../../../../viewlibraries/treeviewnew/store/model/tree-view-props";
import TreeViewStore from "../../../../../../viewlibraries/treeviewnew/store/tree-view-store";
import CommonUtils from "../../../../../../commonmodule/util/common-utils";
import TaxonomyTypeDictionary from "../../../../../../commonmodule/tack/taxonomy-type-dictionary";
import ScreenModeUtils from "./screen-mode-utils";
import MicroEvent from "../../../../../../libraries/microevent/MicroEvent";
const MultiClassificationTypeConstants = new MultiClassificationDialogToolbarLayoutData()["multiClassificationViewTypesIds"];
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
const getTranslation = ScreenModeUtils.getTranslationDictionary;

class NewMultiClassificationStore extends ClassesAndTaxonomyStore {

  triggerChange = () => {
    obj.trigger('classes-and-taxonomies-change');
  };

  fetchMultiClassificationViewData = () => {
    this.getTypesAndTaxonomiesOfContent()
        .then(this.getMultiClassificationTreeData)
        .then(this.triggerChange)
  };

  getMultiClassificationTreeData = () => {
    let sButtonId = MultiClassificationProps.getSelectedTabInClassificationDialog();

    switch (sButtonId) {
      case MultiClassificationTypeConstants.CLASSES:
        return this.fetchNonNatureTypes();

      case MultiClassificationTypeConstants.TAXONOMIES:
        return this.fetchMultiClassificationTaxonomies(false).then(() => {
          let aTree = TreeViewProps.getTreeData();
          return CS.isNotEmpty(aTree) ? this.handleMultiClassificationTreeNodeClicked() : true;
        });
    }
  };

  handleMultiClassificationTreeNodeClicked = (sTreeNodeId = "", bLoadMore = false) => {
    let sButtonId = MultiClassificationProps.getSelectedTabInClassificationDialog();

    switch (sButtonId) {
      case MultiClassificationTypeConstants.CLASSES:
        return this.classTreeNodeClicked(sTreeNodeId);

      case MultiClassificationTypeConstants.TAXONOMIES:
        let oReferencedPermissions = ContentScreenProps.screen.getReferencedPermissions();
        let oHeaderPermission = oReferencedPermissions.headerPermission;
        let oExtraData = {};
        oExtraData.checkBoxData = {
          canCheck: oHeaderPermission.canAddTaxonomy,
          canUnCheck: oHeaderPermission.canDeleteTaxonomy,
        };
        return this.taxonomyTreeNodeClicked(sTreeNodeId, bLoadMore, oExtraData);
    }
  };

  classTreeNodeClicked = (sClassId) => {
    let aMultiClassificationTree = TreeViewProps.getTreeData();
    CS.forEach(aMultiClassificationTree, (oClassNodeList) => {
      oClassNodeList.isExpanded = oClassNodeList.id === sClassId;
    });

    return CommonUtils.resolveEmptyPromise();
  };

  fetchNonNatureTypes = (bLoadMore = false) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oArticleProps = oComponentProps.articleViewProps;
    let oPaginationData = oArticleProps.getAllowedNonNatureTypesPaginationData();
    let aTree = TreeViewProps.getTreeData();
    oPaginationData.from = bLoadMore ? CS.size(aTree) : 0;

    let oData = {
      baseType: oActiveEntity.baseType,
      selectionType: "secondaryTypes",
      idsToExclude: []
    };
    oPaginationData.searchText = MultiClassificationProps.getMultiClassificationTreeSearchText() || "";
    CS.assign(oData, oPaginationData);

    let sAllowedTypesMapping = getRequestMapping().AllowedTypes;

    return CS.postRequest(sAllowedTypesMapping, {}, oData,
        this.successFetchAllowedNonNatureTypes.bind(this, bLoadMore),
        this.failureFetchAllowedNonNatureTypes);
  };

  failureFetchAllowedNonNatureTypes = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureFetchAllowedNonNatureTypes', getTranslation());
  };

  successFetchAllowedNonNatureTypes = (bLoadMore, oResponse) => {
    let oSuccess = oResponse.success;
    let aList = oSuccess.list;
    let iTotalCount = oSuccess.count;
    let aTree = TreeViewProps.getTreeData();
    aTree = bLoadMore ? CS.combine(aTree, aList) : aList;

    let oReferencedClasses = ContentScreenProps.screen.getReferencedClasses();
    CS.assign(oReferencedClasses, CS.keyBy(aList, "id"));

    this.updateTreeVisualProps(aTree, MultiClassificationTypeConstants.CLASSES);
    TreeViewStore.updateLoadMoreMap(iTotalCount, aTree.length, MultiClassificationTypeConstants.CLASSES)
  };

  getTypesAndTaxonomiesOfContent = () => {
    let sSelectedMultiClassificationType = ContentScreenProps.multiClassificationViewProps.getSelectedTabInClassificationDialog();
    let oActiveContent = ContentUtils.getActiveContent();
    let oRequestObj = {
      id: oActiveContent.id,
      baseType: oActiveContent.baseType,
      entityType: sSelectedMultiClassificationType === MultiClassificationTypeConstants.CLASSES ? "class" : "taxonomy"
    };
    let sURL = getRequestMapping().GetTypesAndTaxonomiesOfContent;
    let fSuccess = this.successGetTypesAndTaxonomiesOfContent;
    return CS.postRequest(sURL, {}, oRequestObj, fSuccess, this.failureGetTypesAndTaxonomiesOfContent);
  };

  updateTreeVisualProps = (aList, sContext) => {
    let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
    oMultiClassificationData = oMultiClassificationData.clonedObject || oMultiClassificationData;
    let oReferencedData = null;
    let oReferencedPermissions = ContentScreenProps.screen.getReferencedPermissions();
    let oHeaderPermission = oReferencedPermissions.headerPermission;
    let aCheckedItems = CS.keys(oMultiClassificationData[sContext]);
    let oPermissions = {
      canAdd: true,
      canRemove: true
    };

    switch (sContext) {
      case MultiClassificationTypeConstants.CLASSES:
        oPermissions.canAdd = oHeaderPermission.canAddClasses;
        oPermissions.canRemove = oHeaderPermission.canDeleteClasses;
        oReferencedData = ContentScreenProps.screen.getReferencedClasses();
        break;

      case MultiClassificationTypeConstants.TAXONOMIES:
        oPermissions.canAdd = oHeaderPermission.canAddTaxonomy;
        oPermissions.canRemove = oHeaderPermission.canDeleteTaxonomy;
        oReferencedData = MultiClassificationProps.getReferencedTaxonomyMap();
        break;
    }

    let oActionButtons = this.getActionButtonsForMultiClassificationView();
    let oExtraProperties = {
      checkBoxData: {
        canCheck: oPermissions.canAdd,
        canUnCheck: oPermissions.canRemove,
        checkedItems: aCheckedItems,
      },
      referenceElements: MultiClassificationProps.getReferencedTaxonomyMap()
    };
    TreeViewStore.updateVisualPropsInTreeData(aList, oActionButtons, oExtraProperties)
  };

  successGetTypesAndTaxonomiesOfContent = (oResponse) => {
    let oSuccess = oResponse.success;
    let sButtonId = MultiClassificationProps.getSelectedTabInClassificationDialog();

    switch (sButtonId) {
      case MultiClassificationTypeConstants.CLASSES:
        this.setMultiClassificationClassesData(oSuccess.referencedKlasses);
        break;

      case MultiClassificationTypeConstants.TAXONOMIES:
        let oReferencedTaxonomyMap = MultiClassificationProps.getReferencedTaxonomyMap();
        MultiClassificationProps.setReferencedTaxonomyMap(CS.combine(oReferencedTaxonomyMap, oSuccess.referencedTaxonomies));
        this.setMultiClassificationTaxonomiesData();
        break;
    }
  };

  setMultiClassificationTaxonomiesData = (oReferencedTaxonomies = {}) => {
    let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
    let bIsMultiClassificationDialogOpen = MultiClassificationProps.getIsShowClassificationDialog();
    let aSelectedTaxonomies = [];

    if (bIsMultiClassificationDialogOpen) {
      oReferencedTaxonomies = MultiClassificationProps.getReferencedTaxonomyMap();
      oMultiClassificationData = oMultiClassificationData.clonedObject && oMultiClassificationData.clonedObject || oMultiClassificationData;
      aSelectedTaxonomies = CS.keys(oMultiClassificationData.taxonomies);
    }
    else {
      aSelectedTaxonomies = ContentScreenProps.screen.getActiveTaxonomyIds();
      aSelectedTaxonomies = this.excludeMinorTaxonomies(CS.cloneDeep(aSelectedTaxonomies));
    }

    oMultiClassificationData.taxonomies = {};
    if (CS.isNotEmpty(aSelectedTaxonomies)) {
      oMultiClassificationData.taxonomies = ContentUtils.prepareSelectedTaxonomiesDataForChipsView(oReferencedTaxonomies, aSelectedTaxonomies);
    }
  };

  excludeMinorTaxonomies = (aSelectedTaxonomies) => {
    let oReferencedTaxonomies = ContentScreenProps.screen.getReferencedTaxonomies();
    CS.remove(aSelectedTaxonomies, (sTaxonomyId) => {
      let sTaxonomyType = this.getTaxonomyType(oReferencedTaxonomies[sTaxonomyId]);
      return sTaxonomyType === TaxonomyTypeDictionary.MINOR_TAXONOMY;
    });

    return aSelectedTaxonomies;
  };

  setMultiClassificationClassesData = (oReferencedClasses) => {
    let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
    let bIsMultiClassificationDialogOpen = MultiClassificationProps.getIsShowClassificationDialog();
    let aSelectedClassIds = [];

    if (bIsMultiClassificationDialogOpen) {
      oMultiClassificationData = oMultiClassificationData.clonedObject && oMultiClassificationData.clonedObject || oMultiClassificationData;
      aSelectedClassIds = CS.keys(oMultiClassificationData.classes);
    }
    else {
      aSelectedClassIds = ContentScreenProps.screen.getActiveClassIds();
    }

    oMultiClassificationData.classes = {};
    if(CS.isNotEmpty(aSelectedClassIds)) {
      let oReferencedPermissions = ContentScreenProps.screen.getReferencedPermissions();
      let oHeaderPermission = oReferencedPermissions.headerPermission;
      let bCanDeleteClasses = oHeaderPermission.canDeleteClasses;
      oReferencedClasses = CS.combine(ContentScreenProps.screen.getReferencedClasses(), oReferencedClasses);
      let InvertedMarkerClassTypeDictionary = CS.invert(MarkerClassTypeDictionary);
      CS.forEach(aSelectedClassIds, function (sId) {
        if (!InvertedMarkerClassTypeDictionary[sId]) {
          let oClass = oReferencedClasses[sId];
          oClass.className = oClass.isNature ? "isNature" : "";
          oClass.canRemove = !oClass.isNature && bCanDeleteClasses;
          oClass.customIconClassName = CS.isNotEmpty(oClass.icon) ? oClass.icon : ClassNameFromBaseTypeDictionary[oClass.type];

          oMultiClassificationData.classes[sId] = oClass;
        }
      });
    }
  };

  failureGetTypesAndTaxonomiesOfContent = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureGetTypesAndTaxonomiesOfContent', getTranslation());
  };

  handleMultiClassificationTreeNodeCheckboxClicked = (sCheckedNodeId) => {
    let sButtonId = MultiClassificationProps.getSelectedTabInClassificationDialog();
    TreeViewStore.treeNodeCheckboxClicked(sCheckedNodeId);

    switch (sButtonId) {
      case MultiClassificationTypeConstants.CLASSES:
        this.classTreeNodeCheckboxClicked(sCheckedNodeId);
        break;

      case MultiClassificationTypeConstants.TAXONOMIES:
        this.updateChipsDataOnTreeCheckboxClick(sCheckedNodeId);
        break;
    }
    this.triggerChange();
  };

  classTreeNodeCheckboxClicked = (sClassId) => {
    let oMultiClassificationData = this.makeMultiClassificationDataDirty();
    let oSelectedClasses = oMultiClassificationData.classes;
    let oReferencedClasses = ContentScreenProps.screen.getReferencedClasses();
    let oCheckedTreeNode = oReferencedClasses[sClassId];
    let bIsChecked = CS.isEmpty(oSelectedClasses[sClassId]);

    if (bIsChecked) {
      let oReferencedPermissions = ContentScreenProps.screen.getReferencedPermissions();
      let oHeaderPermission = oReferencedPermissions.headerPermission;
      oSelectedClasses[sClassId] = oCheckedTreeNode;
      oSelectedClasses[sClassId].canRemove = oHeaderPermission.canDeleteClasses;
    }
    else {
      delete oSelectedClasses[sClassId];
    }
  };

  handleMultiClassificationCrossIconClicked = (sLeafNodeId, sIdToRemove) => {
    let sButtonId = MultiClassificationProps.getSelectedTabInClassificationDialog();

    switch (sButtonId) {
      case MultiClassificationTypeConstants.CLASSES:
        this.updateClassesChipsDataOnCrossIconClicked(sIdToRemove);
        break;

      case MultiClassificationTypeConstants.TAXONOMIES:
        this.updateTaxonomyChipsDataOnCrossIconClicked(sLeafNodeId, sIdToRemove);
        break;
    }
    this.triggerChange();
  };

  updateClassesChipsDataOnCrossIconClicked = (sIdToRemove) => {
    let oMultiClassificationData = this.makeMultiClassificationDataDirty();
    let oSelectedTaxonomies = oMultiClassificationData.classes;

    delete oSelectedTaxonomies[sIdToRemove];
    TreeViewStore.treeNodeCheckboxClicked(sIdToRemove);
  };

  handleMultiClassificationDialogViewHeaderButtonClicked = (sButtonId) => {
    MultiClassificationProps.setSelectedTabInClassificationDialog(sButtonId);
    MultiClassificationProps.setMultiClassificationTreeSearchText("");
    this.fetchMultiClassificationViewData();
  };

  handleTreeViewLoadMoreClicked = (sParentTaxonomyId) => {
    this.handleMultiClassificationTreeViewLoadMoreClicked(sParentTaxonomyId)
        .then(this.triggerChange)
  };

  handleMultiClassificationTreeViewLoadMoreClicked = (sParentTaxonomyId) => {
    let sButtonId = MultiClassificationProps.getSelectedTabInClassificationDialog();
    let bLoadMore = true;

    switch (sButtonId) {
      case MultiClassificationTypeConstants.CLASSES:
        return this.fetchNonNatureTypes(bLoadMore);

      case MultiClassificationTypeConstants.TAXONOMIES:
        if (sParentTaxonomyId === "-1") {
          return this.fetchMultiClassificationTaxonomies(bLoadMore);
        }
        else {
          return this.handleMultiClassificationTreeNodeClicked(sParentTaxonomyId, bLoadMore);
        }
    }
  };

  openTaxonomyInheritanceDialog = (_oRequestData) => {
    this.fetchTaxonomyInheritanceConflict(_oRequestData).then(this.triggerChange);
  };

  fetchTaxonomyInheritanceConflict = (oRequestObj) => {
    return CS.postRequest(getRequestMapping().GetTaxonomyConflicts, {}, oRequestObj, this.successFetchTaxonomyInheritance.bind(this, oRequestObj.contentId, oRequestObj.sourceContentId), this.failureFetchTaxonomyInheritance.bind(this));
  };

  failureFetchTaxonomyInheritance = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureFetchTaxonomyInheritance', getTranslation());
  };

  successFetchTaxonomyInheritance = (sContentId, sParentContentId, oResponse) => {
    oResponse = oResponse.success;

    let oReferencedTaxonomies = oResponse.referencedTaxonomies;
    let oReferencedTaxonomyMap = ContentUtils.prepareSelectedTaxonomiesDataForChipsView(oReferencedTaxonomies, CS.keys(oReferencedTaxonomies));

    let oContentIdVsTypesTaxonomies = oResponse.contentIdVsTypesTaxonomies;
    oContentIdVsTypesTaxonomies[sContentId].taxonomyIds = CS.map(oContentIdVsTypesTaxonomies[sContentId].taxonomyIds, sTaxonomyId => {
      return {id: sTaxonomyId, isAvailable: true, isDisabled: false}
    });

    oContentIdVsTypesTaxonomies[sParentContentId].taxonomyIds = CS.map(oContentIdVsTypesTaxonomies[sParentContentId].taxonomyIds, sTaxonomyId => {
      return {id: sTaxonomyId, hideAdaptIcon: false}
    });

    MultiClassificationProps.setReferencedTaxonomyMap(oReferencedTaxonomyMap);
    MultiClassificationProps.setContentIdVsTypesTaxonomies(oContentIdVsTypesTaxonomies);
    MultiClassificationProps.setEntityParentContentId(oResponse.parentContentId);
    MultiClassificationProps.setTaxonomyInheritanceDialog(true);
  };

  makeTaxonomyInheritanceDirty = (sContentId) => {
    let aContentIdVsTypesTaxonomies = MultiClassificationProps.getContentIdVsTypesTaxonomies();
    let oArticleTaxonomies = aContentIdVsTypesTaxonomies[sContentId];
    if (!oArticleTaxonomies.clonedData) {
      oArticleTaxonomies.clonedData = CS.cloneDeep(oArticleTaxonomies);
      oArticleTaxonomies.isDirty = true;
    }
    return oArticleTaxonomies;
  };

  handleAdaptTaxonomyForTaxonomyInheritance = (oBaseArticleTaxonomy, sArticleTaxonomyId, iIndex, sContentId, sParentContentId) => {
    let oContentTypesAndTaxonomies = this.makeTaxonomyInheritanceDirty(sContentId);
    let aContentIdVsTypesTaxonomies = MultiClassificationProps.getContentIdVsTypesTaxonomies();
    let aContentTaxonomies = oContentTypesAndTaxonomies.taxonomyIds;
    if (oBaseArticleTaxonomy.id) {
      let aBaseContentTaxonomies = aContentIdVsTypesTaxonomies[sParentContentId].taxonomyIds;
      aContentTaxonomies.splice(iIndex, 0, oBaseArticleTaxonomy);
      CS.find(aBaseContentTaxonomies, {id: oBaseArticleTaxonomy.id}).hideAdaptIcon = true;
      this.updateChipsDataOnTreeCheckboxClick(oBaseArticleTaxonomy.id);
    } else {
      this.updateChipsDataOnTreeCheckboxClick(sArticleTaxonomyId);
      let oSelectedTaxonomy = CS.find(aContentTaxonomies, {id: sArticleTaxonomyId})
      oSelectedTaxonomy.isDisabled = true;
    }
    this.triggerChange();
  };

  handleAdaptAllTaxonomyForTaxonomyInheritance = (sContentId, sParentContentId) => {
    let oContentTypesAndTaxonomies = this.makeTaxonomyInheritanceDirty(sContentId);
    let aContentIdVsTypesTaxonomies = MultiClassificationProps.getContentIdVsTypesTaxonomies();
    let aParentTaxonomies = aContentIdVsTypesTaxonomies[sParentContentId].taxonomyIds;
    let aContentTaxonomies = oContentTypesAndTaxonomies.taxonomyIds;
    let aUniqTaxonomiesOfBaseArticle = aParentTaxonomies.filter(function(obj) { return !CS.find(aContentTaxonomies, {id: obj.id})});
    let aUniqTaxonomiesOfArticle = aContentTaxonomies.filter(function(obj) { return !CS.find(aParentTaxonomies, {id: obj.id})});
    CS.forEach(aUniqTaxonomiesOfBaseArticle, oTaxonomy => {
      let iIndex = aParentTaxonomies.indexOf(CS.find(aParentTaxonomies, {id: oTaxonomy.id}));
      aContentTaxonomies.splice(iIndex, 0, oTaxonomy);
      let sRemovedTaxonomyId = aContentTaxonomies.splice(iIndex+1, 1);
      (sRemovedTaxonomyId[0] && aContentTaxonomies.push(sRemovedTaxonomyId[0]));
      this.updateChipsDataOnTreeCheckboxClick(oTaxonomy.id);
    });
    CS.forEach(aUniqTaxonomiesOfArticle, oTaxonomy => {
      let iIndex = aContentTaxonomies.indexOf(CS.find(aContentTaxonomies, {id: oTaxonomy.id}));
      aContentTaxonomies[iIndex].isDisabled = true;
      this.updateChipsDataOnTreeCheckboxClick(aContentTaxonomies[iIndex].id);
    });
    aParentTaxonomies.map(oParentTaxonomy => {
      oParentTaxonomy.hideAdaptIcon = true;
      return oParentTaxonomy;
    });
    this.triggerChange();
  };

  handleRevertTaxonomyForTaxonomyInheritance = (oBaseArticleTaxonomy, sArticleTaxonomyId, sContentId, sParentContentId) => {
    let aContentIdVsTypesTaxonomies = MultiClassificationProps.getContentIdVsTypesTaxonomies();
    let oContentTypesAndTaxonomies = aContentIdVsTypesTaxonomies[sContentId];
    let aContentTaxonomies = oContentTypesAndTaxonomies.taxonomyIds;
    if (oBaseArticleTaxonomy.id) {
      CS.remove(aContentTaxonomies, {id: oBaseArticleTaxonomy.id});
      this.updateChipsDataOnTreeCheckboxClick(oBaseArticleTaxonomy.id);
      let aParentTaxonomies = aContentIdVsTypesTaxonomies[sParentContentId].taxonomyIds;
      CS.find(aParentTaxonomies, {id: oBaseArticleTaxonomy.id}).hideAdaptIcon = false;
    } else {
      let oSelectedTaxonomy = CS.find(aContentTaxonomies, {id: sArticleTaxonomyId})
      oSelectedTaxonomy.isDisabled = false;
    }

    let aDifferenceInTaxonomiesBasedOnIds = CS.differenceBy(aContentTaxonomies, oContentTypesAndTaxonomies.clonedData.taxonomyIds, 'id');
    let aDifferenceInTaxonomiesBasedOnIsDisabled = CS.filter(aContentTaxonomies, {isDisabled:true });
    let aDifferenceInTaxonomies = aDifferenceInTaxonomiesBasedOnIds.concat(aDifferenceInTaxonomiesBasedOnIsDisabled);
    if (aDifferenceInTaxonomies.length == 0) {
      this.handleRevertAllTaxonomyForTaxonomyInheritance(sContentId, sParentContentId);
    }
    this.triggerChange();
  };

  handleRevertAllTaxonomyForTaxonomyInheritance = (sContentId, sParentContentId) => {
    let aContentIdVsTypesTaxonomies = MultiClassificationProps.getContentIdVsTypesTaxonomies();
    let oContentTaxonomies = aContentIdVsTypesTaxonomies[sContentId];
    let aUniqTaxonomies = CS.differenceBy(oContentTaxonomies.taxonomyIds, oContentTaxonomies.clonedData.taxonomyIds, "id");
    CS.forEach(aUniqTaxonomies, aTaxonomy => {
      this.updateChipsDataOnTreeCheckboxClick(aTaxonomy.id);
    });
    aContentIdVsTypesTaxonomies[sContentId] = aContentIdVsTypesTaxonomies[sContentId].clonedData;
    let aParentTaxonomies = aContentIdVsTypesTaxonomies[sParentContentId].taxonomyIds;
    aParentTaxonomies.map(oParentTaxonomy => {
      oParentTaxonomy.hideAdaptIcon = false;
      return oParentTaxonomy;
    });
  };

  handleClassificationTreeSearchClicked = (sSearchText) => {
    this.handleClassificationTreeSearched(sSearchText)
    .then(this.triggerChange);
  };

  handleClassificationTreeSearched = (sSearchText) => {
    let sButtonId = MultiClassificationProps.getSelectedTabInClassificationDialog();
    MultiClassificationProps.setMultiClassificationTreeSearchText(sSearchText);

    switch (sButtonId) {
      case MultiClassificationTypeConstants.CLASSES:
        return this.fetchNonNatureTypes();

      case MultiClassificationTypeConstants.TAXONOMIES:
        if(sSearchText) {
          return this.fetchMultiClassificationTaxonomies(false);
        }
        else {
          return this.fetchMultiClassificationTaxonomies(false).then(this.handleMultiClassificationTreeNodeClicked);
        }
    }
  };

  applyMultiClassification = (oCallback) => {
    this.applyClassesOrTaxonomies(oCallback)
        .then(this.triggerChange);
  };

  setActiveClassAndTaxonomyIds = (oActiveContent) => {
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedClasses = oScreenProps.getReferencedClasses();
    let oReferencedTaxonomies = oScreenProps.getReferencedTaxonomies();

    oScreenProps.setActiveClassIds(this.sortSelectedTypesByNatureType(oActiveContent.types));
    oScreenProps.setActiveTaxonomyIds(oActiveContent.selectedTaxonomyIds);
    this.setMultiClassificationClassesData(oReferencedClasses);
    this.setMultiClassificationTaxonomiesData(oReferencedTaxonomies);
  };

  updateTaxonomyChipsDataOnCrossIconClicked = (sLeafNodeId, sIdToRemove) => {
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

      oSelectedTaxonomies[oNewSelectedLeafNode.id] = aNewSelectedTaxonomyPath;
    }

    delete oSelectedTaxonomies[sLeafNodeId];
  };

  fetchMultiClassificationTaxonomies = (bLoadMore = false) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oPaginationData = oComponentProps.screen.getAllowedTaxonomiesPaginationData();
    let aTree = TreeViewProps.getTreeData();
    oPaginationData.from = bLoadMore ? aTree.length : 0;
    let sSearchText = MultiClassificationProps.getMultiClassificationTreeSearchText();

    let oData = {
      baseType: oActiveEntity.baseType,
      selectionType: TaxonomyTypeDictionary.MAJOR_TAXONOMY,
      idsToExclude: [],
      id: CS.isEmpty(sSearchText) ? "-1" : null
    };
    oPaginationData.searchText = sSearchText || "";
    CS.assign(oData, oPaginationData);

    return CS.postRequest(getRequestMapping().AllowedTypes, {}, oData,
        this.successFetchAllowedTaxonomies.bind(this, bLoadMore, {}),
        this.failureFetchAllowedTaxonomies);
  };

}

let obj = new NewMultiClassificationStore();
MicroEvent.mixin(obj);
export default obj;
