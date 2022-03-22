import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ScreenModeUtils from './screen-mode-utils';

import BulkEditProps from '../model/bulk-edit-props';
import {bulkEditTabTypesConstants as BulkEditTabTypesConstants} from "../../tack/bulk-edit-layout-data";
import ContentUtils from './content-utils';
import ZoomSettings from '../../../../../../commonmodule/tack/zoom-toolbar-settings';
import UniqueIdentifierGenerator from './../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import TagTypeConstants from '../../../../../../commonmodule/tack/tag-type-constants';
import TreeViewStore from "../../../../../../viewlibraries/treeviewnew/store/tree-view-store";
import TreeViewProps from "../../../../../../viewlibraries/treeviewnew/store/model/tree-view-props";
import {ActionButtonConstants} from "../../../../../../viewlibraries/treeviewnew/store/model/tree-view-action-buttons";
import BreadCrumbStore from "../../../../../../commonmodule/store/helper/breadcrumb-store";
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
const getTranslation = ScreenModeUtils.getTranslationDictionary;

let BulkEditStore = (function () {

  let _triggerChange = () => {
    BulkEditStore.trigger('bulk-edit-change');
  };

  let _handleTaxonomyCrossIconClicked = (sActionButtonId, sTaxonomyId) => {
    let oTaxonomySummary = BulkEditProps.getTaxonomySummary();
    let oTaxonomiesToAdd = oTaxonomySummary.taxonomiesToAdd;
    let oTaxonomiesToDelete = oTaxonomySummary.taxonomiesToDelete;
    let bIsToggled = false;

    if(oTaxonomiesToAdd[sTaxonomyId]) {
      delete oTaxonomiesToAdd[sTaxonomyId];
      if(sActionButtonId === ActionButtonConstants.ADD) {
        bIsToggled = true;
      }
    }
    else if (oTaxonomiesToDelete[sTaxonomyId]) {
      delete oTaxonomiesToDelete[sTaxonomyId];
      if(sActionButtonId === ActionButtonConstants.REMOVE) {
        bIsToggled = true;
      }
    }
    else {
      return;
    }

    let aTree = TreeViewProps.getTreeData();
    let oTaxonomy = TreeViewStore.getTreeNodeById(aTree, sTaxonomyId);
    if(oTaxonomy) {
      let oActionButtons = TreeViewStore.getActionButtons(_getActionButtonsForBulkEditTreeView(), oTaxonomy);
      oTaxonomy.actionButtons.rightSideButtons = oActionButtons.rightSideButtons;
    }
    return bIsToggled;
  };

  let _handleClassCrossIconClicked = (sActionButtonId, sClassId) => {
    let oClassSummary = BulkEditProps.getClassSummary();
    let oClassesToAdd = oClassSummary.classesToAdd;
    let oClassesToDelete = oClassSummary.classesToDelete;
    let bIsToggled = false;

    if(oClassesToAdd[sClassId]) {
      delete oClassesToAdd[sClassId];
      if(sActionButtonId === ActionButtonConstants.ADD) {
        bIsToggled = true;
      }
    }
    else if (oClassesToDelete[sClassId]) {
      delete oClassesToDelete[sClassId];
      if(sActionButtonId === ActionButtonConstants.REMOVE) {
        bIsToggled = true;
      }
    } else {
      return;
    }

    let aTree = TreeViewProps.getTreeData();
    let oClass = TreeViewStore.getTreeNodeById(aTree, sClassId);
    if(oClass) {
      let oActionButtons = TreeViewStore.getActionButtons(_getActionButtonsForBulkEditTreeView(), oClass);
      oClass.actionButtons.rightSideButtons = oActionButtons.rightSideButtons;
    }
    return bIsToggled;
  }

  let _handleCrossIconClicked = (sActionButtonId, sId) => {
    let sBulkEditType = BulkEditProps.getSelectedTabForBulkEdit();
    switch (sBulkEditType) {
      case BulkEditTabTypesConstants.TAXONOMIES:
        return _handleTaxonomyCrossIconClicked(sActionButtonId, sId);
      case BulkEditTabTypesConstants.CLASSES:
        return _handleClassCrossIconClicked(sActionButtonId, sId);
    }
  }

  let _handleTreeViewNodeRightSideButtonClicked = (sActionButtonId, sId) => {
    let bIsToggled = _handleCrossIconClicked(sActionButtonId, sId);
    if(bIsToggled) {
      return;
    }
    let sBulkEditType = BulkEditProps.getSelectedTabForBulkEdit();
    let oReferencedData, oChipsViewModel;
    let oEntity = {};
    switch (sBulkEditType) {
      case BulkEditTabTypesConstants.TAXONOMIES:
        let oTaxonomySummary = BulkEditProps.getTaxonomySummary();

        switch (sActionButtonId) {
          case ActionButtonConstants.ADD:
            oEntity = oTaxonomySummary.taxonomiesToAdd;
            break;

          case ActionButtonConstants.REMOVE:
            oEntity = oTaxonomySummary.taxonomiesToDelete;
            break;
        }

        oReferencedData = BulkEditProps.getReferencedTaxonomies();
        oChipsViewModel = ContentUtils.prepareSelectedTaxonomiesDataForChipsView(oReferencedData, [sId]);
        break;
      case BulkEditTabTypesConstants.CLASSES:
        let oClassSummary = BulkEditProps.getClassSummary();

        switch (sActionButtonId) {
          case ActionButtonConstants.ADD:
            oEntity = oClassSummary.classesToAdd;
            break;

          case ActionButtonConstants.REMOVE:
            oEntity = oClassSummary.classesToDelete;
            break;
        }

        oReferencedData = BulkEditProps.getReferencedClasses();
        oChipsViewModel = ContentUtils.prepareSelectedClassesDataForChipsView(oReferencedData, [sId]);
        break;
    }
    CS.assign(oEntity, oChipsViewModel);
    TreeViewStore.treeNodeRightSideActionButtonsClicked(sId, sActionButtonId);
  };

  let _showSummaryOfBulkApply = function (oCallback) {
    let aCustomButtonData = [
      {
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        handler: _handleBulkEditApplyButtonClicked.bind(this, oCallback)
      },
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: false,
        handler: CS.noop
      }
    ];
    CustomActionDialogStore.showCustomConfirmDialog("Bulk Edit Confirmation", oCallback.summaryView, aCustomButtonData);
  };

  let _handleBulkEditApplyButtonClicked = function (oCallback) {
    let aKlassInstances = [];
    let aSelectedContentList = ContentUtils.getSelectedEntityList();
    let oTaxonomySummary = BulkEditProps.getTaxonomySummary();
    let aTaxonomiesToAdd = CS.keys(oTaxonomySummary.taxonomiesToAdd);
    let aTaxonomiesToDelete = CS.keys(oTaxonomySummary.taxonomiesToDelete);
    let oClassSummary = BulkEditProps.getClassSummary();
    let aClassesToAdd = CS.keys(oClassSummary.classesToAdd);
    let aClassesToDelete = CS.keys(oClassSummary.classesToDelete);

    CS.forEach(aSelectedContentList, function (oContent) {
      let oInstance = {
        id: oContent.id,
        baseType: oContent.baseType
      };

      aKlassInstances.push(oInstance);
    });

    let oRequestData = {
      attributes: BulkEditProps.getAppliedAttributes(),
      tags: BulkEditProps.getAppliedTags(),
      klassInstances: aKlassInstances,
      addedTaxonomyIds: aTaxonomiesToAdd,
      deletedTaxonomyIds: aTaxonomiesToDelete,
      addedKlassIds: aClassesToAdd,
      deletedKlassIds: aClassesToDelete
    };

    let fSuccess = successBulkEditCallback.bind(this, oCallback);
    return CS.postRequest(getRequestMapping().BulkEdit, {}, oRequestData, fSuccess, failureBulkEditCallback);
  };

  let successBulkEditCallback = function (oCallback) {
    BulkEditProps.reset();

    if(oCallback && CS.isFunction(oCallback.functionToExecute)) {
      oCallback.functionToExecute();
    }
  };

  let failureBulkEditCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureBulkEditCallback', getTranslation());
  };

  let _handleBulkEditCancelClicked = () => {
    BulkEditProps.reset();
    BreadCrumbStore.refreshCurrentBreadcrumbEntity();
  };

  let successFetchAttributeDetailsCallback = function (oResponse) {
    let oAttribute = oResponse.success;
    let oReferencedAttributes = BulkEditProps.getReferencedAttributes();
    let aAppliedAttributes = BulkEditProps.getAppliedAttributes();

    oReferencedAttributes[oAttribute.id] = oAttribute;
    aAppliedAttributes.push({
      attributeId: oAttribute.id,
      value: "",
      valueAsHtml: "",
      label: CS.getLabelOrCode(oAttribute)
    });
  };

  let failureFetchAttributeDetailsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureFetchAttributeDetailsCallback", getTranslation());
  };

  let _fetchAttribute = function (sAttributeId) {
    let oParameters = {
      id: sAttributeId
    };

    return CS.getRequest(getRequestMapping().GetAttribute, oParameters, successFetchAttributeDetailsCallback, failureFetchAttributeDetailsCallback);
  };

  var _updateBulkEditTagValuesRelevance = function (aTagValueRelevanceData, sTagGroupId) {
    let aAppliedTags = BulkEditProps.getAppliedTags();
    let oTagGroupFromDialog = CS.find(aAppliedTags, {tagId: sTagGroupId});
    let aTagValuesFromDialog = oTagGroupFromDialog.tagValues;

    CS.forEach(aTagValuesFromDialog, function (oTagValueFromDialog) {
      let oData = CS.find(aTagValueRelevanceData, {tagId: oTagValueFromDialog.tagId});
      oTagValueFromDialog.relevance = !CS.isEmpty(oData) ? oData.relevance : 0;
    });
  };

  var _updateBulkEditTagRelevance = function (aCheckedItems, sTagGroupId, iNewRelevance) {
    var oReferencedTags = BulkEditProps.getReferencedTags();
    var sTagType = oReferencedTags[sTagGroupId].tagType;
    var aAppliedTags = BulkEditProps.getAppliedTags();
    var oTagGroupFromDialog = CS.find(aAppliedTags, {tagId: sTagGroupId});
    var aTagValuesFromDialog = oTagGroupFromDialog.tagValues;

    switch (sTagType) {
      case TagTypeConstants.RULER_TAG_TYPE:
      case TagTypeConstants.YES_NEUTRAL_TAG_TYPE:
      case TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE:
      case TagTypeConstants.STATUS_TAG_TYPE:
      case TagTypeConstants.TAG_TYPE_MASTER:
        CS.forEach(aTagValuesFromDialog, function (oTagValueFromDialog) {
          var sTagValueId = oTagValueFromDialog.tagId;
          oTagValueFromDialog.relevance = CS.includes(aCheckedItems, sTagValueId) ? 100 : 0;
        });
        break;
      default:
        var oFoundTagValueFromDialog = CS.find(aTagValuesFromDialog, {tagId: aCheckedItems[0]});
        oFoundTagValueFromDialog.relevance = iNewRelevance;
    }
  };

  let _getInstanceOfTagValuesInTag = function (oMasterTag) {
    let aListToIterate = CS.map(oMasterTag.children, "id");
    let aTagValues = [];

    CS.forEach(aListToIterate, function (sId) {
      aTagValues.push({
          id: UniqueIdentifierGenerator.generateUUID(),
          tagId: sId,
          relevance: 0
        });
    });

    return aTagValues;
  };

  let successFetchTagDetailsCallback = function (oResponse) {
    let oTag = oResponse.success;
    let oReferencedTags = BulkEditProps.getReferencedTags();
    let oAppliedTags = BulkEditProps.getAppliedTags();

    oReferencedTags[oTag.id] = oTag;
    oAppliedTags.push({
      tagId: oTag.id,
      tagValues: _getInstanceOfTagValuesInTag(oTag),
      label: CS.getLabelOrCode(oTag)
    });
  };

  let failureFetchTagDetailsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureFetchTagDetailsCallback", getTranslation());
  };

  let _fetchTag = function (sTagId) {
    let oParameters = {
      id: sTagId,
      mode: "all"
    };

    return CS.getRequest(getRequestMapping().GetAllTags, oParameters, successFetchTagDetailsCallback, failureFetchTagDetailsCallback);
  };

  let _attributeCheckboxClicked = (oProperty) => {
    let aAppliedAttributes = BulkEditProps.getAppliedAttributes();
    let oReferencedAttributes = BulkEditProps.getReferencedAttributes();
    let oRemovedProperty = CS.remove(aAppliedAttributes, {attributeId: oProperty.id});

    if(CS.isNotEmpty(oRemovedProperty)) {
      delete oReferencedAttributes[oRemovedProperty.id];
      return _resolveEmptyPromise();
    }
    else {
      return _fetchAttribute(oProperty.id);
    }
  };

  let _tagCheckboxClicked = (oProperty) => {
    let aAppliedTags = BulkEditProps.getAppliedTags();
    let oReferencedTags = BulkEditProps.getReferencedTags();
    let oRemovedProperty = CS.remove(aAppliedTags, {tagId: oProperty.id});

    if(CS.isNotEmpty(oRemovedProperty)) {
      delete oReferencedTags[oRemovedProperty.id];
      return _resolveEmptyPromise();
    }
    else {
      return _fetchTag(oProperty.id);
    }
  };

  let _handleBulkEditPropertyCheckboxClicked = (oProperty) => {
    switch (oProperty.groupType) {
      case "attributes":
        return _attributeCheckboxClicked(oProperty);

      case "tags":
        return _tagCheckboxClicked(oProperty);
    }
  };

  let _handleBulkEditPropertyValueChanged = function (sAttributeId, valueData) {
    let aAppliedAttributes = BulkEditProps.getAppliedAttributes();
    let oAttribute = CS.find(aAppliedAttributes, {attributeId: sAttributeId});
    if (oAttribute) {
      oAttribute.value = valueData.value;
      oAttribute.valueAsHtml = valueData.valueAsHtml;
    }
  };

  let _handleBulkEditRemoveAppliedProperty = (oProperty) => {
    switch (oProperty.type) {
      case "attribute":
        let aAppliedAttributes = BulkEditProps.getAppliedAttributes();
        CS.remove(aAppliedAttributes, {attributeId: oProperty.id});
        let oReferencedAttributes = BulkEditProps.getReferencedAttributes();
        delete oReferencedAttributes[oProperty.id];
        break;

      case "tag":
        let aAppliedTags = BulkEditProps.getAppliedTags();
        CS.remove(aAppliedTags, {tagId: oProperty.id});
        let oReferencedTags = BulkEditProps.getReferencedTags();
        delete oReferencedTags[oProperty.id];
        break;
    }
  };

  let _getActionButtonsForBulkEditTreeView = () => {
    return {
      rightSideActionButtonIds: [ActionButtonConstants.ADD, ActionButtonConstants.REMOVE, ActionButtonConstants.EXPANDED],
      leftSideActionButtonIds: [],
    }
  };

  let _successFetch = (bLoadMore, oResponse) => {
    let oSuccess = oResponse.success;
    let aList = oSuccess.list;
    let iTotalCount = oSuccess.count;
    let aTree = TreeViewProps.getTreeData();
    aTree = bLoadMore ? CS.combine(aTree, aList) : aList;

    let oReferencedData = null;
    let sBulkEditType = BulkEditProps.getSelectedTabForBulkEdit();

    switch (sBulkEditType) {
      case BulkEditTabTypesConstants.TAXONOMIES:
        oReferencedData = BulkEditProps.getReferencedTaxonomies();
        break

      case BulkEditTabTypesConstants.CLASSES:
        oReferencedData = BulkEditProps.getReferencedClasses();
        break
    }

    CS.assign(oReferencedData, CS.keyBy(aTree, "id"));

    let oActionButtons = _getActionButtonsForBulkEditTreeView();
    let oExtraProperties = null;

    switch (sBulkEditType) {
      case BulkEditTabTypesConstants.TAXONOMIES:
        let oTaxonomySummary = BulkEditProps.getTaxonomySummary();
        oExtraProperties = {
          bAddTaxonomyType: true,
          taxonomiesToAdd: oTaxonomySummary.taxonomiesToAdd,
          taxonomiesToDelete: oTaxonomySummary.taxonomiesToDelete,
        };
        break

      case BulkEditTabTypesConstants.CLASSES:
        let oClassSummary = BulkEditProps.getClassSummary();
        oExtraProperties = {
          bAddClassType: true,
          classesToAdd: oClassSummary.classesToAdd,
          classesToDelete: oClassSummary.classesToDelete,
        };
        break
    }

    TreeViewStore.updateVisualPropsInTreeData(aTree, oActionButtons, oExtraProperties, sBulkEditType);
    TreeViewStore.updateLoadMoreMap(iTotalCount, aTree.length);
  }

  let successFetchTaxonomies = (bLoadMore, oResponse) => {
    return _successFetch(bLoadMore, oResponse);
  };

  let failureFetchTaxonomies = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureFetchTaxonomies', getTranslation());
  };

  let successFetchClasses = (bLoadMore, oResponse) => {
    return _successFetch(bLoadMore, oResponse);
  };

  let failureFetchClasses = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureFetchClasses', getTranslation());
  };

  let _getTree = (bLoadMore, sBulkEditType, fSuccess, fFailure) => {
    let aTree = TreeViewProps.getTreeData();
    let iFrom = bLoadMore ? aTree.length : 0;
    let sSearchText = BulkEditProps.getTreeSearchText();
    let sSelectionType = "";
    let oModuleId = ContentUtils.getSelectedModulesForFilter();
    switch (sBulkEditType) {
      case BulkEditTabTypesConstants.TAXONOMIES:
        sSelectionType = "taxonomy";
        break

      case BulkEditTabTypesConstants.CLASSES:
        sSelectionType = "secondaryTypes";
        break
    }

    let oData = {
      baseType: "",
      selectionType: sSelectionType,
      idsToExclude: [],
      id: CS.isEmpty(sSearchText) ? "-1" : null,
      searchText: sSearchText || "",
      from: iFrom,
      size: ZoomSettings.defaultPaginationLimit,
      searchColumn: "label",
      moduleId: oModuleId
    };

    return CS.postRequest(getRequestMapping().AllowedTypes, {}, oData,
      fSuccess.bind(this, bLoadMore),
      fFailure);
  }

  let _getTaxonomyTree = (bLoadMore = false) => {
    return _getTree(bLoadMore, BulkEditTabTypesConstants.TAXONOMIES, successFetchTaxonomies, failureFetchTaxonomies);
  };

  let _getClassTree = (bLoadMore = false) => {
    return _getTree(bLoadMore, BulkEditTabTypesConstants.CLASSES, successFetchClasses, failureFetchClasses);
  };

  let successGetTaxonomyChildren = (bLoadMore, oResponse) => {
    let oSuccess = oResponse.success;
    let aChildren = oSuccess.children;
    let oReferencedTaxonomies = BulkEditProps.getReferencedTaxonomies();
    CS.assign(oReferencedTaxonomies, CS.keyBy(aChildren, "id"));
    let oActionButtonIds = _getActionButtonsForBulkEditTreeView();
    let oTaxonomySummary = BulkEditProps.getTaxonomySummary();
    let oExtraProperties = {
      loadMore: bLoadMore,
      nodeId: oSuccess.id,
      totalCount: oSuccess.count,
      taxonomiesToAdd: oTaxonomySummary.taxonomiesToAdd,
      taxonomiesToDelete: oTaxonomySummary.taxonomiesToDelete,
    };
    TreeViewStore.updateDataOnTreeNodeClick(aChildren, oExtraProperties, oActionButtonIds);
  };

  let failureGetTaxonomyChildren = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureGetTaxonomyChildren', getTranslation());
  };

  let _getTreeNodeById = (aTree, sTreeNodeId) => (
    aTree.reduce((oData, oTreeData) => {
      if (oData)
        return oData;
      if (oTreeData.id === sTreeNodeId)
        return oTreeData;
      if (oTreeData.children)
        return _getTreeNodeById(oTreeData.children, sTreeNodeId)
    }, null)
  );

  let _getTaxonomyChildrenList = (sTaxonomyId = "", bLoadMore = false) => {
    let aTaxonomyTree = TreeViewProps.getTreeData();
    sTaxonomyId = sTaxonomyId || aTaxonomyTree[0].id;
    let fSuccess = successGetTaxonomyChildren.bind(this, bLoadMore);
    let fFailure = failureGetTaxonomyChildren;
    let oTaxonomy = _getTreeNodeById(aTaxonomyTree, sTaxonomyId);
    let oRequestObj = {
      id: sTaxonomyId,
      from: bLoadMore ? CS.size(oTaxonomy.children) : 0,
      size: ZoomSettings.defaultPaginationLimit
    };
    let sURL = getRequestMapping().GetTaxonomyHierarchyForSelectedTaxonomy;
    return CS.postRequest(sURL, {}, oRequestObj, fSuccess, fFailure);
  };

  let _fetchTaxonomiesForBulkEdit = () => {
    return _getTaxonomyTree()
        .then(() => {
          let aTree = TreeViewProps.getTreeData();
          if (CS.isNotEmpty(aTree)) {
            return _getTaxonomyChildrenList();
          }
          else {
            return new Promise((resolve) => {
              resolve(null);
            });
          }
        });
  };

  let _fetchClassesForBulkEdit = () => {
    return _getClassTree();
  };

  let _handleTreeSearchClicked = (sSearchText) => {
    let sBulkEditType = BulkEditProps.getSelectedTabForBulkEdit();

    BulkEditProps.setTreeSearchText(sSearchText);

    switch (sBulkEditType) {
      case BulkEditTabTypesConstants.CLASSES:
        return _getClassTree();

      case BulkEditTabTypesConstants.TAXONOMIES:
        if (sSearchText) {
          return _getTaxonomyTree();
        }
        else {
          return _getTaxonomyTree().then(_getTaxonomyChildrenList);
        }
    }
  };

  let _handleTreeViewLoadMoreClicked = (sParentId) => {
    let sBulkEditType = BulkEditProps.getSelectedTabForBulkEdit();
    let bLoadMore = true;

    switch (sBulkEditType) {
      case BulkEditTabTypesConstants.CLASSES:
        return _getClassTree(bLoadMore);

      case BulkEditTabTypesConstants.TAXONOMIES:
        if (sParentId === "-1") {
          return _getTaxonomyTree(bLoadMore);
        }
        else {
          return _getTaxonomyChildrenList(sParentId, bLoadMore);
        }
    }
  };

  let _resolveEmptyPromise = () => {
    return new Promise((resolve) => {
      resolve(null);
    });
  };

  let _fetchBulkEditData = (sButtonId) => {
    switch (sButtonId) {
      case BulkEditTabTypesConstants.PROPERTIES:
        return _resolveEmptyPromise();

      case BulkEditTabTypesConstants.TAXONOMIES:
        return _fetchTaxonomiesForBulkEdit();

      case BulkEditTabTypesConstants.CLASSES:
        return _fetchClassesForBulkEdit();
    }
  };

  return {
    openBulkEditView: function () {
      BulkEditProps.setIsBulkEditViewOpen(true);
      _triggerChange();
    },

    handleBulkEditHeaderToolbarButtonClicked: function (sButtonId) {
      BulkEditProps.setSelectedTabForBulkEdit(sButtonId);
      BulkEditProps.setTreeSearchText("");
      _fetchBulkEditData(sButtonId)
          .then(_triggerChange);
    },

    getTaxonomyChildrenList: function (sTreeNodeId) {
      _getTaxonomyChildrenList(sTreeNodeId)
          .then(_triggerChange);
    },

    handleBulkEditCancelClicked: function () {
      _handleBulkEditCancelClicked();
    },

    handleBulkEditPropertyCheckboxClicked: function (oProperty) {
      _handleBulkEditPropertyCheckboxClicked(oProperty)
          .then(_triggerChange);
    },

    handleBulkEditRemoveAppliedProperty: function (oProperty) {
      _handleBulkEditRemoveAppliedProperty(oProperty);
      _triggerChange();
    },

    handleBulkEditPropertyValueChanged: function (sAttributeId, valueData) {
      _handleBulkEditPropertyValueChanged(sAttributeId, valueData);
      _triggerChange();
    },

    handleBulkEditApplyButtonClicked: function (oCallback) {
      _showSummaryOfBulkApply(oCallback);
    },

    updateBulkEditTagRelevance: function (aCheckedItems, sTagId, iNewRelevance) {
      _updateBulkEditTagRelevance(aCheckedItems, sTagId, iNewRelevance);
      _triggerChange();
    },

    updateBulkEditTagValuesRelevance: function (aTagValueRelevanceData, sTagId) {
      _updateBulkEditTagValuesRelevance(aTagValueRelevanceData, sTagId);
      _triggerChange();
    },

    handleTreeSearchClicked: function (sSearchText) {
      _handleTreeSearchClicked(sSearchText)
        .then(_triggerChange);
    },

    handleTreeViewNodeRightSideButtonClicked: function (sAction, sNodeId) {
      _handleTreeViewNodeRightSideButtonClicked(sAction, sNodeId);
      _triggerChange();
    },

    handleTreeViewLoadMoreClicked: function (sParentId) {
      _handleTreeViewLoadMoreClicked(sParentId)
          .then(_triggerChange);
    },

    handleCrossIconClicked: function (sId) {
      _handleCrossIconClicked(null, sId);
      _triggerChange();
    }
  }
})();

MicroEvent.mixin(BulkEditStore);

export default BulkEditStore;
