import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ViewLibraryUtils from './../utils/view-library-utils';
import { view as HorizontalTreeNodeView } from './horizontal-tree-node-view';
import { view as HorizontalTreeChildNodeView } from './horizontal-tree-child-node-view';
import TooltipView from '../tooltipview/tooltip-view';
import EntityBaseTypeDictionary from '../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import HierarchyTypesDictionary from '../../commonmodule/tack/hierarchy-types-dictionary';

var oEvents = {
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CLICKED: "handle_content_horizontal_tree_node_clicked",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_ARROW_CLICKED: "handle_content_horizontal_tree_node_arrow_clicked",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_RIGHT_CLICKED: "handle_content_horizontal_tree_node_right_clicked",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CONTEXT_MENU_POPOVER_CLOSE: "handle_content_horizontal_tree_node_context_menu_popover_close",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CONTEXT_MENU_ITEM_CLICKED: "handle_content_horizontal_tree_node_context_menu_item_clicked",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_ADD_NEW_NODE: "handle_content_horizontal_tree_node_add_new_node",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_TOGGLE_AUTOMATIC_SCROLL_PROP: "handle_content_horizontal_tree_node_toggle_automatic_scroll_prop",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_DELETE_ICON_CLICKED: "handle_content_horizontal_tree_node_delete_icon_clicked",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_VIEW_ICON_CLICKED: "handle_content_horizontal_tree_node_view_icon_clicked",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_VIEW_PERMISSION_TOGGLED: "handle_content_horizontal_tree_node_view_permission_toggled",
  HANDLE_CONTENT_HORIZONTAL_TREE_NODE_VISIBILITY_CHANGED: "handle_content_horizontal_tree_node_visibility_changed"
};

const oPropTypes = {
  contentHierarchyData: ReactPropTypes.object,
  showIcon: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object,
  showCode: ReactPropTypes.bool,
};
/**
 * @class HorizontalTreeView - Use for Display choose Taxonomy searchBar tree view.
 * @memberOf Views
 * @property {object} [contentHierarchyData] -  object of contentHierarchyData.
 */

// @CS.SafeComponent
class HorizontalTreeView extends React.Component {
constructor (oProps) {
  super(oProps);

  this.contentHorizontalTreeViewContainer = React.createRef();
}

  static propTypes = oPropTypes;



  state = {
    showEditNamePopover: false,
    editNamePopoverParentNodeId: "",
    newNodeInputValue: ""
  };

  componentDidUpdate() {
    this.updateHorizontalScroll()
  }

  updateHorizontalScroll = () => {
    var oHierarchyData = this.props.contentHierarchyData;
    if(oHierarchyData.scrollAutomatically){
      var oContainerDivDOM = this.contentHorizontalTreeViewContainer.current;
      oContainerDivDOM.scrollLeft = oContainerDivDOM.scrollWidth;
      EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_TOGGLE_AUTOMATIC_SCROLL_PROP);
    }
  };

  handleNodeRightArrowClicked = (oChild, oNode, iLevel, oVisualProp, oEvent) => {
    oEvent.stopPropagation();
    if(oVisualProp.isCollapsed){
      this.handleLoadLazyTreeChildrenData(oChild, oNode, iLevel);
    }else {
      var oContentHierarchyData = this.props.contentHierarchyData;
      var sSelectedContext = oContentHierarchyData.selectedContext;
      var oReqData = {
        level: iLevel,
        clickedNodeId: oChild.id,
        selectedContext: sSelectedContext
      };
      EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_ARROW_CLICKED, oReqData);
    }
  };

  handleLoadLazyTreeChildrenData = (oChild, oNode, iLevel, oEvent) => {
    if (oEvent.nativeEvent.dontRaise) {
      return;
    }
    var oContentHierarchyData = this.props.contentHierarchyData;
    var sSelectedContext = oContentHierarchyData.selectedContext;

    var oReqData = {
      level: iLevel,
      clickedNodeId: oChild.id,
      selectedContext: sSelectedContext,
      baseType: oChild.baseType
    };

    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CLICKED, oReqData, this.props.filterContext);
  };

  handleNodeRightClicked = (oChild, oNode, iLevel, oEvent) => {
    var oContentHierarchyData = this.props.contentHierarchyData;
    var sSelectedContext = oContentHierarchyData.selectedContext;

    if(sSelectedContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY || sSelectedContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
      oEvent.preventDefault();
      let oReqData = {
        clickedNodeId:oChild.id,
        level: iLevel,
        selectedContext: sSelectedContext
      };
      EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_RIGHT_CLICKED, oReqData);
    }
  };

  handleContextMenuPopoverClose = () => {
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CONTEXT_MENU_POPOVER_CLOSE);
  };

  handleContextMenuItemClicked = (sItemId) => {
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CONTEXT_MENU_ITEM_CLICKED, sItemId);
  };

  handleDeleteHierarchyNodeClicked = (oClickedNode, iLevel, oEvent) => {
    oEvent.stopPropagation();
    var sClickedNodeId = oClickedNode.id;
    var oContentHierarchyData = this.props.contentHierarchyData;
    var sSelectedContext = oContentHierarchyData.selectedContext;
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_DELETE_ICON_CLICKED, sClickedNodeId, iLevel, sSelectedContext, this.props.filterContext);
  };

/*
  handleViewHierarchyNodeClicked = (oClickedNode, oEvent) => {
    oEvent.stopPropagation();
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_VIEW_ICON_CLICKED, oClickedNode,this.props.filterContext);
  };
*/

  handleHierarchyLoadMoreClicked = (iLevel, oNode, oParent, oEvent) => {
    var oContentHierarchyData = this.props.contentHierarchyData;
    var sSelectedContext = oContentHierarchyData.selectedContext;
    var oReqData = {
      level: iLevel,
      id: oNode.id,
      clickedNodeId: oNode.id,
      childrenLength: oNode.children.length,
      selectedContext: sSelectedContext,
      loadMore: true
    };

    if (iLevel == 0) {
      oReqData.klassId = oNode.klassId;
    } else {
      oReqData.isRelationshipSpecific = true;
      oReqData.instanceId = oParent.instanceId;
    }
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CLICKED, oReqData, this.props.filterContext);
  };

  handleHierarchySearched = (iLevel, oNode, oParent, sSearchText) => {
    var oContentHierarchyData = this.props.contentHierarchyData;
    var sSelectedContext = oContentHierarchyData.selectedContext;
    var oReqData = {
      level: iLevel,
      id: oNode.id,
      searchText: sSearchText,
      clickedNodeId: oNode.id,
      selectedContext: sSelectedContext
    };

    if (iLevel === 0) {
      oReqData.klassId = oNode.klassId;
    } else {
      oReqData.isRelationshipSpecific = true;
      oReqData.instanceId = oParent.instanceId;
    }
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CLICKED, oReqData, this.props.filterContext);
  };

  handleVisibilityModeChanged = (oClickedNode, oEvent) => {
    oEvent.stopPropagation();
    var sClickedNodeId = oClickedNode.id;
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_VISIBILITY_CHANGED, sClickedNodeId, this.props.filterContext);
  };

  handleCreateNewNodeClicked = (sNodeId) => {
    this.setState({
      showEditNamePopover: true,
      editNamePopoverParentNodeId: sNodeId
    });
  };

  handleAddNewNodePopoverClosed = () => {
    var sNewLabel = this.state.newNodeInputValue;
    if(!CS.isEmpty(sNewLabel)){
      this.handleNewNodeOkClicked()
    }else {
      this.handleNewNodeCancelClicked();
    }
  };

  handleNewNodeNameValueChanged = (oEvent) => {
    this.setState({
      newNodeInputValue: oEvent.target.value
    });
  };

  handleNewNodeInputKeyDown = (oEvent) => {
    if(oEvent.keyCode == 13){ //Enter Clicked
      this.handleNewNodeOkClicked();
    }
  };

  handleNewNodeOkClicked = () => {
    var __state = this.state;
    var sNewLabel = __state.newNodeInputValue;
    var sParentNode = __state.editNamePopoverParentNodeId;
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_ADD_NEW_NODE, sParentNode, sNewLabel, this.props.filterContext);
    this.handleNewNodeCancelClicked();
  };

  handleNewNodeCancelClicked = () => {
    this.setState({
      showEditNamePopover: false,
      editNamePopoverParentNodeId: "",
      newNodeInputValue: ""
    });
  };

  getEditNewNodeNamePopoverChild = () => {
    return (
        <div className="editHierarchyNodeNameContainer">
          <div className="editNameInputContainer">
            <input className="editNodeNameInput"
                   value={this.state.newNodeInputValue}
                   placeholder={getTranslation().PLEASE_ENTER_NAME}
                   onChange={this.handleNewNodeNameValueChanged}
                   onKeyDown={this.handleNewNodeInputKeyDown}
                   onBlur={this.handleNewNodeOkClicked}
                   autoFocus={true}/>
          </div>
        </div>
    );
  };

  getCreateNewNodeDOM = (sContext, sNodeId) => {
    var sSplitter = ViewLibraryUtils.getSplitter();
    var sLabel = getTranslation().ADD_NEW_NODE;
    if(sContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
      sLabel = getTranslation().ADD_NEW_COLLECTION;
    }
    var sRef = "createNewNodeContainer" + sSplitter + sNodeId;
    var oEditableInputNode = this.state.showEditNamePopover && sNodeId == this.state.editNamePopoverParentNodeId ?
        this.getEditNewNodeNamePopoverChild() : null;

    return (
        <div className="createNewNodeMainWrapper" key={sNodeId}>
          {oEditableInputNode}
          <div className="createNewNodeContainer" ref={sRef}
               onClick={this.handleCreateNewNodeClicked.bind(this, sNodeId)}>
            <div className="createIcon"></div>
            <TooltipView placement="bottom" label={sLabel}>
              <div className="createNewIconLabel">{sLabel}</div>
            </TooltipView>
          </div>
        </div>);
  };

  handlePermissionToggled = (sId, sProperty, sValue, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_VIEW_PERMISSION_TOGGLED, sId, sProperty, sValue);
  };

  getNewView = (oViews, oNode, iLevel) => {
    var _this = this;
    var oContentHierarchyData = this.props.contentHierarchyData;
    var oVisualProps = oContentHierarchyData.oHierarchyTreeVisualProps;
    var sSelectedContext = oContentHierarchyData.selectedContext;
    var sRightClickedHierarchyNodeId = oContentHierarchyData.rightClickedHierarchyNodeId;
    var aHierarchyNodeContextMenuItemList = oContentHierarchyData.hierarchyNodeContextMenuItemList;
    var oLevelsPaginationData = oContentHierarchyData.paginationData;
    var bIsMasterCollectionListOrganiseClicked = oContentHierarchyData.collectionSectionsData ?
        oContentHierarchyData.collectionSectionsData.isMasterCollectionListOrganiseClicked: false;
    let oPermissionMap = oContentHierarchyData.permissionMap || {};
    let oFunctionalPermissionMap = oContentHierarchyData.permissionFunctionalMap || {};

    let bIsPermissionConfigSelected = sSelectedContext === "permissionConfig";
    if ((oVisualProps[oNode.id].isCollapsed == false) || (oVisualProps[oNode.id].isExpanded == true)) {
      oViews[iLevel] = oViews[iLevel] || [];

      var oParentNode = {};
      var oGrandParent = {};

      var aChildrenView = [];
      var aChildren = oNode.children;
      CS.forEach(aChildren, function (oChild) {
        var bIsRightClickedPopoverVisible = sRightClickedHierarchyNodeId == oChild.id;
        var oVisualProp = oVisualProps[oChild.id];
        var fLoadChildNodesHandler = _this.handleLoadLazyTreeChildrenData.bind(_this, oChild, oNode, iLevel);
        var fArrowClickHandler = _this.handleNodeRightArrowClicked.bind(_this, oChild, oNode, iLevel, oVisualProp);
        var fNodeRightClickHandler = _this.handleNodeRightClicked.bind(_this, oChild, oNode, iLevel);
        var fContextMenuPopoverCloseHandler = _this.handleContextMenuPopoverClose;
        var fContextMenuItemClickHandler = _this.handleContextMenuItemClicked;
        var fDeleteHierarchyNodeClickHandler = _this.handleDeleteHierarchyNodeClicked.bind(_this, oChild, iLevel);
        var fChangeVisibilityModeHandler = _this.handleVisibilityModeChanged.bind(_this, oChild);
        let oChildPermissionMap = oPermissionMap[oChild.id];
        let oChildFunctionalPermissionMap = oFunctionalPermissionMap[oChild.id];
        let bShowPublicPrivateModeButton = oContentHierarchyData.collectionSectionsData && oContentHierarchyData.collectionSectionsData.showPublicPrivateModeButton;
        aChildrenView.push(
            <HorizontalTreeChildNodeView key={oChild.id}
                                                node={oChild}
                                                selectedHierarchyContext={sSelectedContext}
                                                parentNode={oNode}
                                                grandParentNode={oParentNode}
                                                greatGrandParentNode={oGrandParent}
                                                level={iLevel}
                                                visualProp={oVisualProp}
                                                permissionMap={oChildPermissionMap}
                                                functionalpermissionMap={oChildFunctionalPermissionMap}
                                                permissionButtonToggleHandler={_this.handlePermissionToggled}
                                                popoverVisibilityStatus={bIsRightClickedPopoverVisible}
                                                arrowClickHandler={fArrowClickHandler}
                                                loadChildNodesHandler={fLoadChildNodesHandler}
                                                nodeRightClickHandler={fNodeRightClickHandler}
                                                contextMenuPopoverCloseHandler={fContextMenuPopoverCloseHandler}
                                                contextMenuItemClickHandler={fContextMenuItemClickHandler}
                                                deleteHierarchyNodeClickHandler={fDeleteHierarchyNodeClickHandler}
                                                hierarchyNodeContextMenuItemList={aHierarchyNodeContextMenuItemList}
                                                changeVisibilityModeHandler={fChangeVisibilityModeHandler}
                                                isMasterCollectionListOrganiseClicked={bIsMasterCollectionListOrganiseClicked}
                                                showPublicPrivateModeButton={bShowPublicPrivateModeButton}
                                                showIcon={_this.props.showIcon}
                                                showCode={_this.props.showCode}
            />
        );

        if(CS.isEmpty(oChild.children) &&  oChild.type === EntityBaseTypeDictionary.supplierKlassBaseType){
          return;
        }
        else if (oChild.children) {
          if (!oChild.instanceId) {
            //For Other Hierarchies.
            /*  // Commented because all entities are displayed in flat level in Permission config dialog
            if  (bIsPermissionConfigSelected && oVisualProp.isSelected) {
               _this.getNewView(oViews, oChild, iLevel + 1);
             } else */
            //TODO: check below IF..ELSE ..need to refactor.
            if (sSelectedContext != HierarchyTypesDictionary.COLLECTION_HIERARCHY && oChild.children.length) {
              _this.getNewView(oViews, oChild, iLevel + 1);
            } else if(sSelectedContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
              _this.getNewView(oViews, oChild, iLevel + 1);
            } else if(sSelectedContext == "classConfig") {
              _this.getNewView(oViews, oChild, iLevel + 1);
            } else if(sSelectedContext == "smartDocument") {
              _this.getNewView(oViews, oChild, iLevel + 1);
            } else if(sSelectedContext == "languageTree") {
              _this.getNewView(oViews, oChild, iLevel + 1);
            }

          }
        } else if (oVisualProps[oChild.id] && oVisualProps[oChild.id].emptySearchResults) {
          _this.getNewView(oViews, oChild, iLevel + 1);
        }
      });

      if(sSelectedContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
        if(!(iLevel === 0 && !bIsMasterCollectionListOrganiseClicked)){
          aChildrenView.push(_this.getCreateNewNodeDOM(sSelectedContext, oNode.id));
        }
      }

      var sSearchText = "";
    if (bIsPermissionConfigSelected) {
        sSearchText = CS.isNotEmpty(oLevelsPaginationData[oNode.id]) ? oLevelsPaginationData[oNode.id].searchText : "";
      }

      let bShowSearchAndLoadMore = (bIsPermissionConfigSelected && oContentHierarchyData.showSearch);
      if (!oVisualProps[oNode.id] || !oVisualProps[oNode.id].hideChildrenIfEmpty) {

        oViews[iLevel].push(
            <HorizontalTreeNodeView
                key={oNode.id}
                selectedContext={sSelectedContext}
                level={iLevel}
                node={oNode}
                parentNode={oParentNode}
                showSearch={bShowSearchAndLoadMore}
                showLoadMore={bShowSearchAndLoadMore}
                searchText={sSearchText}
                onLoadMore={this.handleHierarchyLoadMoreClicked}
                onSearch={this.handleHierarchySearched}
                visualProp={oVisualProps[oNode.id]}>
              {aChildrenView}
            </HorizontalTreeNodeView>
        );

      }
    }
    return oViews;
  };

  getNewViewWrap = () => {
    var _this = this;
    var __props = _this.props;
    var oContentHierarchyData = __props.contentHierarchyData;
    var oHierarchyTree = oContentHierarchyData.oHierarchyTree;

    let oViews = CS.isNotEmpty(oHierarchyTree)? this.getNewView({}, oHierarchyTree, 0) : [];

    var aLevelView = [];
    CS.forEach(oViews, function (oLevelView, iKey) {
      if (CS.isEmpty(oLevelView)) {
        return;
      }
      aLevelView.push(
          <div className="contentHorizontalTreeLevelContainer" key={iKey}>
            {oLevelView}
          </div>);
    });

    return (<div className="contentHorizontalTreeViewContainer" ref={this.contentHorizontalTreeViewContainer}>
      {aLevelView}
    </div>);
  };

  render() {
    var aViews = this.getNewViewWrap();
    var oContentHierarchyData = this.props.contentHierarchyData;
    var sSelectedContext = oContentHierarchyData.selectedContext;

    return (
        <div className={"contentHierarchyTreeViewContainer " + sSelectedContext}>
          {aViews}
        </div>
    );
  }
}

export const view = HorizontalTreeView;
export const events = oEvents;
