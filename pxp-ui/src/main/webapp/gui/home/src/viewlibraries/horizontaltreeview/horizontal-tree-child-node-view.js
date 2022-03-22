import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ViewLibraryUtils from './../utils/view-library-utils';
import TooltipView from '../tooltipview/tooltip-view';
import BaseTypeDictionary from '../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import CommonConstants from '../../commonmodule/tack/constants';
import HierarchyTypesDictionary from '../../commonmodule/tack/hierarchy-types-dictionary';
import ClassCategoryConstantsDictionary from '../../screens/homescreen/screens/settingscreen/tack/class-category-constants-dictionary';
import {view as GridYesNoPropertyView} from "../gridview/grid-yes-no-property-view";
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";

var oEvents = {
  /**Warning: Do not dispatch events from this view, rather pass the handlers*/
};

const oPropTypes = {
  node: ReactPropTypes.object,
  parentNode: ReactPropTypes.object,
  selectedHierarchyContext: ReactPropTypes.string,
  grandParentNode: ReactPropTypes.object,
  greatGrandParentNode: ReactPropTypes.object,
  level: ReactPropTypes.number,
  visualProp: ReactPropTypes.object,
  permissionMap: ReactPropTypes.object,
  permissionButtonToggleHandler: ReactPropTypes.func,
  popoverVisibilityStatus: ReactPropTypes.bool,
  arrowClickHandler: ReactPropTypes.func,
  loadChildNodesHandler: ReactPropTypes.func,
  nodeRightClickHandler: ReactPropTypes.func,
  contextMenuPopoverCloseHandler: ReactPropTypes.func,
  contextMenuItemClickHandler: ReactPropTypes.func,
  deleteHierarchyNodeClickHandler: ReactPropTypes.func,
  viewHierarchyNodeClickHandler: ReactPropTypes.func,
  hierarchyNodeContextMenuItemList: ReactPropTypes.array,
  changeVisibilityModeHandler: ReactPropTypes.func,
  isMasterCollectionListOrganiseClicked: ReactPropTypes.bool,
  showPublicPrivateModeButton: ReactPropTypes.bool,
  functionalpermissionMap: ReactPropTypes.object,
  showCode: ReactPropTypes.bool,
};
/**
 * @class HorizontalTreeChildNodeView
 * @memberOf Views
 * @property {object} [node] - Contains all information about tree item(for example: id, label, code, type, task etc).
 * @property {object} [parentNode] - Contains all information about parent node.
 * @property {string} [selectedHierarchyContext] - Hierarchy name(ex: class config hierarchy)
 * @property {object} [grandParentNode] - Grandparent node data(node -> parentNode -> grandParentNode).
 * @property {object} [greatGrandParentNode] - Parent node data of grandparent node(node -> parentNode -> grandParentNode -> greatGrandParentNode).
 * @property {number} [level] - Level of tree.
 * @property {object} [visualProp] - Contains visual props ex. id, isActive, isChildLoaded, isExpanded, isEditable, isSelected, label etc.
 * @property {object} [permissionMap] - Contains permissions data(ex: canCreate, canDelete, canEdit, canRead, canDownload, type etc).
 * @property {func} [permissionButtonToggleHandler] - Executes when permission button is clicked.
 * @property {bool} [popoverVisibilityStatus] - To check popover visibility status(if true popover will be visible).
 * @property {func} [arrowClickHandler]. - Deprecated
 * @property {func} [loadChildNodesHandler] - Executes when node is clicked. loadChildNodesHandler is used to get all child nodes.
 * @property {func} [nodeRightClickHandler] - Deprecated
 * @property {func} [contextMenuPopoverCloseHandler] - The contextMenuPopoverCloseHandler event occurs when a popover loses focus.
 * @property {func} [contextMenuItemClickHandler] - Executes after Context menu item is clicked.
 * @property {func} [deleteHierarchyNodeClickHandler] - Executes when delete icon of node is clicked.
 * @property {func} [viewHierarchyNodeClickHandler] - Used to show the preview of node(used in relationship hierarchy).
 * @property {array} [hierarchyNodeContextMenuItemList] -
 * @property {func} [changeVisibilityModeHandler] - Two modes - public and private.
 * @property {bool} [isMasterCollectionListOrganiseClicked] -
 * @property {bool} [showPublicPrivateModeButton] - Used for display or hide public/private mode button(if true - display).
 */

// @CS.SafeComponent
class HorizontalTreeChildNodeView extends React.Component {
  constructor (oProps) {
    super(oProps);

    this.contentHorizontalTreeNode = React.createRef();
  }
  static propTypes = oPropTypes;

  getImageSrcForThumbnail = (oNode) => {
    var aList = oNode.referencedAssets;
    if(oNode.baseType == BaseTypeDictionary.assetBaseType) {
      aList = oNode.attributes;
    }
    var oImage = CS.find(aList, {isDefault: true});

    return ViewLibraryUtils.getImageSrcUrlFromImageObject(oImage);
  };

/*
  getThumbnailImageView = (sImageSrc, sType) => {
    var oProperties = {};
    var oImageViewStyle = {};

    var oImageViewModel = new ImageViewModel(
        '',
        sImageSrc,
        sType,
        "thumbnailImagePreview",
        oImageViewStyle,
        oProperties
    );

    return (<ImageView model={oImageViewModel} onLoad={CS.noop}/>);
  };
*/


  getDeleteNodeDom = () => {
    var __props = this.props;
    var oVisualProp = __props.visualProp;
    var bCanDelete = CS.isEmpty(oVisualProp) ? false : oVisualProp.canDelete === undefined ? false : oVisualProp.canDelete;
    if (bCanDelete) {
      return (
          <TooltipView label={getTranslation().DELETE}>
            <div className="deleteHierarchyNode" onClick={__props.deleteHierarchyNodeClickHandler}></div>
          </TooltipView>
      );
    } else {
      return null;
    }
  };

  getCountDOM = () => {
    var __props = this.props;
    var sSelectedHierarchyContext = __props.selectedHierarchyContext;
    var oNode = __props.node;
    if (sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY) {
      var sSpecialNode = oNode.id == ViewLibraryUtils.getHierarchyDummyNodeId() ?  " specialNode" : "";
      return (
          <div className={"showCount" + sSpecialNode}>{"(" + oNode.count + ")"}</div>
      );
    } else {
      return null;
    }
  };

/*
  getArrowDom = () => {
    var oVisualProp = this.props.visualProp;
    if(!oVisualProp){
      return null;
    }
    var sClassName = "collapseExpandArrow ";
    if(oVisualProp.noChildren){
      sClassName += "dispN"
    }else {
      if(oVisualProp.isCollapsed){
        sClassName += "collapsed"
      }else {
        sClassName += "expanded"
      }
    }
    // var fArrowClickHandler = this.props.arrowClickHandler;
    var fArrowClickHandler = CS.noop;
    return (
        <div className={sClassName} onClick={fArrowClickHandler}></div>
    );
  };
*/

  handleContextMenuItemClick = (sItemId) => {
    this.props.contextMenuItemClickHandler(sItemId);
  };

  getVisibilityButtonDom = (iLevel, oCollection) => {
    var sVisibilityIcon = oCollection.isPublic ? "visibilityModeButton" : "visibilityModeButton isPrivate";
    var sVisibilityTooltipLabel = oCollection.isPublic ? getTranslation().COLLECTION_PUBLIC_MODE : getTranslation().COLLECTION_PRIVATE_MODE;
    let bShowPublicPrivateModeButton = iLevel == 0 && this.props.showPublicPrivateModeButton;

    if ((oCollection.createdBy == ViewLibraryUtils.getCurrentUser().id) && bShowPublicPrivateModeButton) {
      return (<TooltipView placement="bottom" label={sVisibilityTooltipLabel}>
        <div className={sVisibilityIcon} onClick={this.props.changeVisibilityModeHandler}></div>
      </TooltipView>);
    }

    return null;
  };

  getPopoverChildView = () => {
    var __props = this.props;
    let bIsPopoverVisible = __props.popoverVisibilityStatus;
    if(!bIsPopoverVisible){
      return null;
    }

    var _this = this;
    var aHierarchyNodeContextMenuItemList = __props.hierarchyNodeContextMenuItemList;

    var aContextItemDOMNodes = [];
    CS.forEach(aHierarchyNodeContextMenuItemList, function (oContextItem) {
      aContextItemDOMNodes.push(
          <div className="contextMenuNode"
               onClick={_this.handleContextMenuItemClick.bind(_this, oContextItem.id)}>
            {CS.getLabelOrCode(oContextItem)}
          </div>
      )
    });

    return <div className="hierarchyNodeContextMenuContainer">{aContextItemDOMNodes}</div>
  };

  getClassNameFromBaseType = (sType) => {
    let sIconClassName = '';
    switch (sType) {
      case BaseTypeDictionary.articleKlassBaseType :
        sIconClassName = 'articleIcon';
        break;
      case BaseTypeDictionary.assetKlassBaseType :
        sIconClassName = 'assetIcon';
        break;
      case BaseTypeDictionary.supplierKlassBaseType :
        sIconClassName = 'supplierIcon';
        break;
      case BaseTypeDictionary.marketKlassBaseType :
        sIconClassName = 'targetIcon';
        break;
      case BaseTypeDictionary.textAssetKlassBaseType :
        sIconClassName = 'textAssetIcon';
        break;
      case BaseTypeDictionary.smartDocumentTemplateBaseType :
        sIconClassName = 'smartDocumentTemplateIcon';
        break;
      case BaseTypeDictionary.smartDocumentPresetBaseType :
        sIconClassName = 'smartDocumentPresetIcon';
        break;
    }
    return sIconClassName;
  };

  getIconDom = () => {
    let __props = this.props;
    let oNode = __props.node;
    let sNodeIconClassName = "nodeIconClass ";
    let oNodeIconView = null;
    if(CS.has(oNode, "className")){
      sNodeIconClassName += oNode.className;
    }
    if (CS.has(oNode, "isNature") || __props.showIcon) {
      if (oNode.isNature) {
        sNodeIconClassName += oNode.natureType;
      } else {
        let sClassName = this.getClassNameFromBaseType(oNode.type);
        sNodeIconClassName += sClassName;
      }
    }
    oNodeIconView = <div className={sNodeIconClassName}></div>;
    return oNodeIconView;
  };

  handlePermissionButtonToggled = (sId, sProperty, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;

  };

  getPermissionButtonView = (sId, sProperty, sPropertyClass, sDisplayText, sTooltip) => {
    let sIconClassName = "buttonIcon " + sProperty;
    return (
        <div className={sPropertyClass}
             key={sProperty + "_" + sId}
             onClick={this.props.permissionButtonToggleHandler.bind(this, sId, sProperty, "")}>
          <TooltipView label={sTooltip} placement={"top"}>
            <div className={sIconClassName}></div>
          </TooltipView>
        </div>);
  };

  getPermissionButtonsView = () => {
    let oPermissionMap = this.props.permissionMap;
    let oIsNatureClass = this.props.node.isNature
    let oParentNode = this.props.parentNode;
    if (CS.isEmpty(oPermissionMap)) {
      return null;
    }

    let bIsNonRootTaxonomy = oPermissionMap.type === CommonConstants.ENTITY_TYPE_TAXONOMY && this.props.level > 0
    if ((CS.isNotEmpty(oParentNode.tabsData) && oParentNode.tabsData.selectedTabId === "minorTaxonomy") || bIsNonRootTaxonomy) {
      return (
          <div className="permissionButtonsView">
            {this.getCopyToClipboardDom()}
          </div>);
    }

    let bIsTask = oPermissionMap.type === CommonConstants.ENTITY_TYPE_TASK;
    let sId = this.props.node.id;
    let bIsAssetClass = (oParentNode.id === ClassCategoryConstantsDictionary.ASSET_CLASS);
    let sDisableClass = oPermissionMap.isDisabled ? " disabledNode" : "";
    let sCreateClass = "permissionButton" + (oPermissionMap.canCreate ? " selected" : "") + sDisableClass;
    let sUpdateClass = "permissionButton" + (oPermissionMap.canEdit ? " selected" : "") + sDisableClass;
    let sDeleteClass = "permissionButton" + (oPermissionMap.canDelete ? " selected" : "") + sDisableClass;
    let sReadClass = "permissionButton" + (oPermissionMap.canRead ? " selected" : "") + sDisableClass;
    let sDownloadClass = "permissionButton" + (oPermissionMap.canDownload ? " selected" : "");
    return (
        <div className="permissionButtonsView">
          {this.getCopyToClipboardDom()}
          {bIsTask && this.getPermissionButtonView(sId, "canRead", sReadClass, "R", getTranslation().READ)}
          {this.getPermissionButtonView(sId, "canCreate", sCreateClass, "C", getTranslation().CREATE)}
          {bIsTask && this.getPermissionButtonView(sId, "canEdit", sUpdateClass, "U", getTranslation().UPDATE)}
          {this.getPermissionButtonView(sId, "canDelete", sDeleteClass, "D", getTranslation().DELETE)}
          {bIsAssetClass && oIsNatureClass && this.getPermissionButtonView(sId, "canDownload", sDownloadClass, "DW", getTranslation().DOWNLOAD)}
        </div>
    );

  };

  getToggleButtonView = (oNode) => {
    let oFunctionalPermissionMap = this.props.functionalpermissionMap;
    if (CS.isNotEmpty(oFunctionalPermissionMap)) {
      let sValue = oFunctionalPermissionMap[oNode.id];
      return <GridYesNoPropertyView value={sValue}
                                    onChange={this.props.permissionButtonToggleHandler.bind(this, oNode.id, "")}
                                    isDisabled={oNode.isDisabled}/>
    }
  };

  getCopyToClipboardDom = () => {
    return (
        <TooltipView placement={"top"} label={getTranslation().COPY_TO_CLIPBOARD_TOOLTIP}>
          <div className={"copyToClipboard"}
               onClick={ViewUtils.copyToClipboard.bind(this, this.props.node.code)}> </div>
        </TooltipView>
    );
  };

  render() {
    var __props = this.props;
    var oNode = __props.node;
    var iLevel = __props.level;
    var oVisualProp = __props.visualProp;
    var fLoadChildNodesHandler = __props.loadChildNodesHandler;
    var fContextMenuPopoverCloseHandler = __props.contextMenuPopoverCloseHandler;
    var bIsPopoverVisible = __props.popoverVisibilityStatus;

    var sNodeContainerClassName = "contentHorizontalTreeGroupElContainer ";
    if(oVisualProp.isSelected){
      sNodeContainerClassName += "selected ";
    }
    if(oVisualProp.isActive){
      sNodeContainerClassName += "active ";
    }
    if (oVisualProp.isDisabled) {
      sNodeContainerClassName += "disabled ";
    }


    // let oReadPermissionButtonDom = this.getPermissionButtonsView(true);
    let oPermissionButtonsDom = this.getPermissionButtonsView();

    var sNodeLabel = CS.getLabelOrCode(oNode);
    let sNodeCode = oNode.code || "";
    var sSpecialNode = oNode.id == ViewLibraryUtils.getHierarchyDummyNodeId() ?  " specialNode" : "";
    let aLabels = [sNodeLabel];
    let oCodeDOM = null;

    if (this.props.showCode) {
      aLabels.push(<span className="tooltipNodeId" key={sNodeCode}>{sNodeCode}</span>);
      oCodeDOM = (<span className={"treeNodeCodeSpan" + sSpecialNode}>{sNodeCode}</span>);
    }

    var oLabelDom = (
        <TooltipView label={aLabels}>
          <div className="tableLabelCodeWrapper">
            <span className={"treeNodeLabelSpan" + sSpecialNode}>{sNodeLabel}</span>
            {oCodeDOM}
          </div>
        </TooltipView>);
    var oCountDom = this.getCountDOM();
    var oDeleteIconDom = this.getDeleteNodeDom();
    var oPopoverChildView = this.getPopoverChildView();
    var oVisibilityButtonDom = this.getVisibilityButtonDom(iLevel, oNode);
    let oIconDom = this.getIconDom();
    let oToggleButtonDom = this.getToggleButtonView(oNode);

    //onContextMenu={fNodeRightClickHandler}
    let oInnerReturnDomView = (
        <div className={sNodeContainerClassName} key={oNode.id} >
          <div className="contentHorizontalTreeGroupElLabel"
               ref={this.contentHorizontalTreeNode} onClick={fLoadChildNodesHandler}>
            {oIconDom}
            {oLabelDom}
            {oToggleButtonDom}
            {oCountDom}
            {/*{oArrowDom}*/}
            {oVisibilityButtonDom}
            {oDeleteIconDom}
            {oPermissionButtonsDom}
          </div>
          <CustomPopoverView
              className="popover-root"
              open={bIsPopoverVisible}
              anchorEl={this.contentHorizontalTreeNode.current}
              anchorOrigin={{horizontal: 'center', vertical: 'center'}}
              transformOrigin={{horizontal: 'left', vertical: 'top'}}
              onClose={fContextMenuPopoverCloseHandler}>
            {oPopoverChildView}
          </CustomPopoverView>
        </div>);

    return oInnerReturnDomView;
  }
}

export const view = HorizontalTreeChildNodeView;
export const events = oEvents;
