import MicroEvent from "../../../libraries/microevent/MicroEvent";
import CS from "../../../libraries/cs";
import ClassNameFromBaseTypeDictionary from "../../../commonmodule/tack/class-name-base-types-dictionary";
import TreeViewActionButtons from "./model/tree-view-action-buttons"
import {ActionButtonConstants} from "./model/tree-view-action-buttons"
import TreeViewProps from "./model/tree-view-props";
import { bulkEditTabTypesConstants } from "../../../screens/homescreen/screens/contentscreen/tack/bulk-edit-layout-data";
import TaxonomyTypeDictionary from "./../../../commonmodule/tack/taxonomy-type-dictionary";
import CommonUtils from "./../../../commonmodule/util/common-utils";

let TreeViewStore = (function () {

  let _filterRightSideActionButtons = (aRightSideButtonIds, oExtraProperties, oTreeNode, sBulkEditType) => {

    if (oTreeNode.isLastNode) {
      let iIndex = CS.indexOf(aRightSideButtonIds, ActionButtonConstants.EXPANDED);
      if(iIndex !== -1){
        aRightSideButtonIds.splice(iIndex, 1);
      }
    }

    switch (sBulkEditType) {
      case bulkEditTabTypesConstants.TAXONOMIES:
        if(CS.isNotEmpty(oExtraProperties)) {
          let aValidRightSideButtonIds = CS.cloneDeep(aRightSideButtonIds);
          CS.remove(aValidRightSideButtonIds, (sButtonId) => {
            return (sButtonId === ActionButtonConstants.ADD &&
                CS.isNotEmpty(oExtraProperties.taxonomiesToDelete) && oExtraProperties.taxonomiesToDelete[oTreeNode.id])
                || (sButtonId === ActionButtonConstants.REMOVE &&
                CS.isNotEmpty(oExtraProperties.taxonomiesToAdd) && oExtraProperties.taxonomiesToAdd[oTreeNode.id]);
          });

          return aValidRightSideButtonIds;
        }
        break;

      case bulkEditTabTypesConstants.CLASSES:
        if(CS.isNotEmpty(oExtraProperties)) {
          let aValidRightSideButtonIds = CS.cloneDeep(aRightSideButtonIds);
          CS.remove(aValidRightSideButtonIds, (sButtonId) => {
            return (sButtonId === ActionButtonConstants.ADD &&
                CS.isNotEmpty(oExtraProperties.classesToDelete) && oExtraProperties.classesToDelete[oTreeNode.id])
                || (sButtonId === ActionButtonConstants.REMOVE &&
                CS.isNotEmpty(oExtraProperties.classesToAdd) && oExtraProperties.classesToAdd[oTreeNode.id]);
          });

          return aValidRightSideButtonIds;
        }
        break;
    }

    return aRightSideButtonIds;
  };

  let _getRightSideActionButtonsData = (aRightSideButtonIds, oRightSideButtonData, oExtraProperties, oTreeNode, sBulkEditType) => {
    let aRightSideButtons = [];
    let aValidRightSideButtonIds = _filterRightSideActionButtons(aRightSideButtonIds, oExtraProperties, oTreeNode, sBulkEditType);

    CS.forEach(aRightSideButtonIds, (sButtonId) => {
      let oButton = {};
      if (aValidRightSideButtonIds.includes(sButtonId)) {
        switch (sButtonId) {
          case ActionButtonConstants.ADD:
            oButton = oRightSideButtonData[ActionButtonConstants.ADD];
            let bIsAdded = CS.isNotEmpty(oExtraProperties) && (CS.isNotEmpty(oExtraProperties.taxonomiesToAdd) || CS.isNotEmpty(oExtraProperties.classesToAdd));
            if (bIsAdded && (oExtraProperties.taxonomiesToAdd && oExtraProperties.taxonomiesToAdd[oTreeNode.id] || oExtraProperties.classesToAdd && oExtraProperties.classesToAdd[oTreeNode.id])) {
              oButton.className += " added";
              oButton.isAdded = true;
            }
            break;

          case ActionButtonConstants.REMOVE:
            oButton = oRightSideButtonData[ActionButtonConstants.REMOVE];
            let bRemoved = CS.isNotEmpty(oExtraProperties) && (CS.isNotEmpty(oExtraProperties.taxonomiesToDelete) || CS.isNotEmpty(oExtraProperties.classesToDelete));
            if (bRemoved && (oExtraProperties.taxonomiesToAdd && oExtraProperties.taxonomiesToDelete[oTreeNode.id] || oExtraProperties.classesToAdd && oExtraProperties.classesToDelete[oTreeNode.id])) {
              oButton.className += " removed";
              oButton.isRemoved = true;
            }
            break;

          case ActionButtonConstants.EXPANDED:
            oButton = oRightSideButtonData[ActionButtonConstants.EXPANDED];
            if (oTreeNode.isExpanded && CS.isNotEmpty(oTreeNode.children)) {
              oButton.className += " expanded";
            }
            break
        }
      }
      else {
        switch (sButtonId) {
          case ActionButtonConstants.ADD:
              oButton = oRightSideButtonData[ActionButtonConstants.ADD];
            oButton.className += " collapse-added";
            break;

          case ActionButtonConstants.REMOVE:
              oButton = oRightSideButtonData[ActionButtonConstants.REMOVE];
            oButton.className += " collapse-removed";
            break;

          case ActionButtonConstants.EXPANDED:
              oButton = oRightSideButtonData[ActionButtonConstants.EXPANDED];
            oButton = oRightSideButtonData[ActionButtonConstants.EXPANDED];
            if (oTreeNode.isExpanded && CS.isNotEmpty(oTreeNode.children)) {
              oButton.className += " expanded";
            }
            break
        }
      }

      aRightSideButtons.push(oButton);
    });

    return aRightSideButtons;
  };

  let _getLeftSideActionButtonsData = (aLeftSideButtonIds, oLeftSideButtonData, oExtraProperties, oTreeNode) => {
    let aLeftSideButtons = [];
    CS.forEach(aLeftSideButtonIds, (sButtonId) => {
      let oButton = {};
      switch (sButtonId) {
        case ActionButtonConstants.CHECKBOX :
          oButton = _updateCheckBoxButtonData(oTreeNode, oExtraProperties);
          break;
      }
      aLeftSideButtons.push(oButton);
    });

    return aLeftSideButtons;
  };

  let _getActionButtons = (oActionButtonsData, oTreeNode, oExtraProperties, sBulkEditType) => {
    let {rightSideActionButtonIds: aRightSideButtonIds, leftSideActionButtonIds: aLeftSideButtonIds} = oActionButtonsData;
    let {rightSideActionButtons: oRightSideButtonData, leftSideActionButtons: oLeftSideButtonData} = new TreeViewActionButtons();
    let aRightSideButtons = _getRightSideActionButtonsData(aRightSideButtonIds, oRightSideButtonData, oExtraProperties, oTreeNode, sBulkEditType);
    let aLeftSideButtons = _getLeftSideActionButtonsData(aLeftSideButtonIds, oLeftSideButtonData, oExtraProperties, oTreeNode);

    return {
      rightSideButtons: aRightSideButtons,
      leftSideButtons: aLeftSideButtons
    }
  };

  let _updateCheckBoxButtonData = (oTreeNode, oExtraProperties) => {
    let {leftSideActionButtons: oLeftSideButtonData} = new TreeViewActionButtons();
    let oCheckBox = oLeftSideButtonData[ActionButtonConstants.CHECKBOX];
    let oCheckBoxData = oExtraProperties.checkBoxData;
    oCheckBox.isChecked = CS.includes(oCheckBoxData.checkedItems, oTreeNode.id);
    oCheckBox.canCheck = oCheckBoxData.canCheck;
    oCheckBox.canUnCheck = oCheckBoxData.canUnCheck;
    oCheckBox.className += oCheckBox.isChecked ? " checked " : " unChecked ";
    if((oCheckBox.isChecked && !oCheckBox.canUnCheck) || (!oCheckBox.isChecked && !oCheckBox.canCheck)) {
      oCheckBox.className += "isDisabled ";
      oCheckBox.isDisabled = true;
    }
    return oCheckBox;
  };

  let _updateVisualPropsInTreeData = (aList, oActionButtonsData = {}, oExtraProperties = {}, sBulkEditType) => {
    _recursivelyUpdateVisualPropsInTreeData(aList, oActionButtonsData, oExtraProperties, "", sBulkEditType);
    TreeViewProps.setTreeData(aList);
  };

  //TODO: Refactor - Create a model for tree node
  let _recursivelyUpdateVisualPropsInTreeData = (aList, oActionButtonsData = {}, oExtraProperties = {}, sParentPath = "", sBulkEditType) => {
    CS.forEach(aList, function (oData) {
      let sLabel = CS.getLabelOrCode(oData) || "";
      oData.isLastNode = oData.isLastNode;
      oData.isExpanded = oData.isExpanded || false;
      oData.parentId = oData.parentId || oExtraProperties.nodeId;
      oData.toolTip =  sParentPath && sParentPath.concat(" > ", sLabel) || CommonUtils.getElementPath(oData.parentId, sLabel, oExtraProperties.referenceElements);
      oData.customIconClassName = CS.isEmpty(oData.icon) ? ClassNameFromBaseTypeDictionary[oData.type] : null;
      let oActionButtonsDataClone = CS.cloneDeep(oActionButtonsData);
      if (oData.taxonomyType == TaxonomyTypeDictionary.MINOR_TAXONOMY && oData.parentId == -1) {
        oActionButtonsDataClone.rightSideActionButtonIds = CS.filter(oActionButtonsDataClone.rightSideActionButtonIds, (sButtonIds) => {
          return !CS.includes([ActionButtonConstants.REMOVE, ActionButtonConstants.ADD], sButtonIds)
        });
      };
      oData.actionButtons = _getActionButtons(oActionButtonsDataClone, oData, oExtraProperties, sBulkEditType);
      _recursivelyUpdateVisualPropsInTreeData(oData.children, oActionButtonsData, oExtraProperties, oData.toolTip);

      switch(sBulkEditType){
        case bulkEditTabTypesConstants.TAXONOMIES:
            oData.typeIconClassName = oExtraProperties && oExtraProperties.bAddTaxonomyType ? oData.taxonomyType : "";
            break;
        case bulkEditTabTypesConstants.CLASSES:
            oData.typeIconClassName = oExtraProperties && oExtraProperties.bAddClassType ? oData.classType : "";
            break;
        default:
            oData.typeIconClassName = oExtraProperties && oExtraProperties.bAddTaxonomyType ? oData.taxonomyType : "";
            break;
      }

    });
  };


  let _updateDataOnTreeNodeClick = (aChildren, oExtraProperties, oActionButtonsData) => {
    let aTreeData = TreeViewProps.getTreeData();
    /** add children list into existing tree & change expanded state on selection of tree node**/
    let oSelectedTreeNode = _getTreeNodeById(aTreeData, oExtraProperties.nodeId);

    /** add parentId and visual props in children list **/
    _recursivelyUpdateVisualPropsInTreeData(aChildren, oActionButtonsData, oExtraProperties, oSelectedTreeNode.toolTip);

    if (oExtraProperties.loadMore) {
      oSelectedTreeNode.children = CS.combine(oSelectedTreeNode.children, aChildren);
    }
    else {
      let aSiblings = _getSiblingsOfNode(oSelectedTreeNode, aTreeData);
      CS.forEach(aSiblings, (oNode) => {
        let oExpandedButton = CS.find(oNode.actionButtons.rightSideButtons, {id: ActionButtonConstants.EXPANDED});
        if(oNode.id === oSelectedTreeNode.id) {
          oNode.children = aChildren;
          if(!oNode.isExpanded) {
            oNode.isExpanded = true;
            CS.isNotEmpty(aChildren) && (oExpandedButton.className += " expanded");
          }
        }
        else {
          oNode.isExpanded = false;
          oNode.children = [];
          oExpandedButton && (oExpandedButton.className = "rightSideButton");
        }
      });
    }

    _updateLoadMoreMap(oExtraProperties.totalCount, oSelectedTreeNode.children.length, oExtraProperties.nodeId);
  };

  let _updateLoadMoreMap = (iTotalCount, aLoadedListLength, sParentId = "-1") => {
    let bShowLoadMore = iTotalCount > aLoadedListLength;
    let oTreeLoadMoreMap = TreeViewProps.getTreeLoadMoreMap();
    oTreeLoadMoreMap[sParentId] = bShowLoadMore;
    TreeViewProps.setTreeLoadMoreMap(oTreeLoadMoreMap);
  };

  let _getSiblingsOfNode = (oNode, aMultiClassificationTree) => {
    if (oNode.parentId === "-1") {
      return aMultiClassificationTree;
    }
    else {
      let oParentOfSelectedNode = _getTreeNodeById(aMultiClassificationTree, oNode.parentId);
      return oParentOfSelectedNode.children;
    }
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

  let _treeNodeRightSideActionButtonsClicked = (sNodeId, sActionButtonId) => {
    let aTreeData = TreeViewProps.getTreeData();
    let oTreeNode = _getTreeNodeById(aTreeData, sNodeId);
    let aRightSideActionButtons = oTreeNode.actionButtons.rightSideButtons;
    let oButton = CS.find(aRightSideActionButtons, {id: sActionButtonId});

    switch (sActionButtonId) {
      case ActionButtonConstants.ADD:
        oButton.className += " added";
        oButton.isAdded = true;
        let oRemovedButton = CS.find(aRightSideActionButtons, {id: ActionButtonConstants.REMOVE}); 
        oRemovedButton.className += " collapse-removed";
        break;

      case ActionButtonConstants.REMOVE:
        oButton.className += " removed";
        oButton.isRemoved = true;
        let oAddedButton = CS.find(aRightSideActionButtons, {id: ActionButtonConstants.ADD});
        oAddedButton.className += " collapse-added";
        break;
    }
  };

  let _treeNodeCheckboxClicked = (sCheckedNodeId, aTreeData) => {
    aTreeData = aTreeData || TreeViewProps.getTreeData();
    let oCheckedTreeNode = _getTreeNodeById(aTreeData, sCheckedNodeId);
    if(CS.isEmpty(oCheckedTreeNode)) {
      return;
    }
    let aLeftSideActionButtons = oCheckedTreeNode.actionButtons.leftSideButtons;
    let oCheckBoxButton = CS.find(aLeftSideActionButtons, {id: ActionButtonConstants.CHECKBOX});
    let oExtraProperties = {
      checkBoxData: {
        canCheck: oCheckBoxButton.canCheck,
        canUnCheck: oCheckBoxButton.canUnCheck,
        checkedItems: oCheckBoxButton.isChecked ? [] : [oCheckedTreeNode.id],
      }
    };
    CS.assign(oCheckBoxButton, _updateCheckBoxButtonData(oCheckedTreeNode, oExtraProperties))
  };

  /**    public functions   */
  return {
    updateVisualPropsInTreeData: (aList, oActionButtonsData, oExtraProperties, sBulkEditType) => {
      _updateVisualPropsInTreeData(aList, oActionButtonsData, oExtraProperties, sBulkEditType);
    },

    updateDataOnTreeNodeClick: (aChildren, oProperties, oActionButtonsData) => {
      _updateDataOnTreeNodeClick(aChildren, oProperties, oActionButtonsData);
    },

    treeNodeCheckboxClicked: (sCheckedNodeId, aTreeData) => {
      _treeNodeCheckboxClicked(sCheckedNodeId, aTreeData);
    },

    treeNodeRightSideActionButtonsClicked: (sNodeId, sActionButtonId) => {
      _treeNodeRightSideActionButtonsClicked(sNodeId, sActionButtonId);
    },

    updateLoadMoreMap: (iTotalCount, aLoadedListLength, sParentId) => {
      _updateLoadMoreMap(iTotalCount, aLoadedListLength, sParentId);
    },

    getActionButtons: (oActionButtonsData, oTreeNode, oExtraProperties = {}) => {
      return _getActionButtons(oActionButtonsData, oTreeNode, oExtraProperties);
    },

    getTreeNodeById: (aTree, sTreeNodeId) => {
      return _getTreeNodeById(aTree, sTreeNodeId);
    },

    resetTreeViewData: () => {
      TreeViewProps.reset();
    },
  }
})();

MicroEvent.mixin(TreeViewStore);

export default TreeViewStore;