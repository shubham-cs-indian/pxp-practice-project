import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";
import {ActionButtonConstants} from "./store/model/tree-view-action-buttons.js";
import TooltipView from './../tooltipview/tooltip-view';
import {getTranslations} from "../../commonmodule/store/helper/translation-manager";

var oEvents = {};

const oPropTypes = {
  node: ReactPropTypes.object,
  searchText: ReactPropTypes.string,
  nodeCheckClickHandler: ReactPropTypes.func,
  loadChildNodesHandler: ReactPropTypes.func,
  showCheckNodeAtAllLevels: ReactPropTypes.bool,
  rightSideActionButtonsHandler: ReactPropTypes.func,
  context: ReactPropTypes.string,
  showCount: ReactPropTypes.bool,
  showEndNodeIndicator: ReactPropTypes.bool
};

// @CS.SafeComponent
class TreeNodeView extends React.Component {
  static propTypes = oPropTypes;

  static defaultProps = {
    loadChildNodesHandler: (()=>{})
  }

  getHeaderText () {
    let __props = this.props;
    let oNode = __props.node;
    let aRightSideButtons = oNode.actionButtons.rightSideButtons || [];
    let sSearchText = __props.searchText;
    let sTooltipLabel = oNode.toolTip || CS.getLabelOrCode(oNode);
    let sLabelClassName = "treeNodeLabel ";
    let oLabelDOM = CS.getLabelOrCode(oNode);

    if (CS.isNotEmpty(sSearchText)) {
      sLabelClassName += "highLightedText";
      oLabelDOM = ViewUtils.getHighlightedHeaderText(CS.getLabelOrCode(oNode), sSearchText, "highLightedTextValue", sTooltipLabel);
    }
    CS.forEach(aRightSideButtons, (oRightSideButton) => {
      if (oRightSideButton.id === ActionButtonConstants.ADD && oRightSideButton.isAdded === true) {
        sLabelClassName += " selected ";
      }
      else if (oRightSideButton.id === ActionButtonConstants.REMOVE && oRightSideButton.isRemoved === true) {
        sLabelClassName += " selected ";
      }
    });

    return (
        <TooltipView placement="bottom" label={sTooltipLabel}>
          <div className={sLabelClassName}>{oLabelDOM}</div>
        </TooltipView>
    );
  }

  getIconView = (oNode) => {
    let oIconDom = null;

    if (CS.isEmpty(oNode.iconKey) && oNode.customIconClassName) {
      oIconDom = <div className={"treeNodeIcon customIcon " + oNode.customIconClassName} key="classIcon"></div>;
    }
    else {
      let sSrcURL = ViewUtils.getIconUrl(oNode.iconKey);
      oIconDom = <ImageFitToContainerView imageSrc={sSrcURL}/>;
    }

    return oIconDom;
  };

  getRightSideActionButtonsDom = () => {
    let oNodeData = this.props.node;
    let oActionButtons = oNodeData.actionButtons || {};
    let aRightSideButtons = oActionButtons.rightSideButtons;
    let aDom = [];
    let sTreeNodeIconClassName = "treeNodeIcon ";
    let fNodeClickHandler = this.props.rightSideActionButtonsHandler || CS.noop;
    CS.forEach(aRightSideButtons, (oButton) => {
      oButton.isDisabled && (fNodeClickHandler = CS.noop);
      aDom.push(
          <div className={sTreeNodeIconClassName + oButton.className}
               onClick={fNodeClickHandler.bind(this, this.props.context, oButton.id, oNodeData.id)}></div>
      )
    });
    if (oNodeData.isLastNode && this.props.showEndNodeIndicator) {
      aDom.push(
          <TooltipView placement="bottom" label={getTranslations().END_NODE}>
            <div className={sTreeNodeIconClassName + "rightSideButton treeLastNodeIcon"}></div>
          </TooltipView>
      )
    }

    return aDom;
  };

  getLeftSideActionButtonsDom = () => {
    let oNodeData = this.props.node;
    let oActionButtons = oNodeData.actionButtons || {};
    let aLeftSideButtons = oActionButtons.leftSideButtons;
    if (!aLeftSideButtons.length) {
      return null;
    }
    let aDom = [];
    let sTreeNodeIconClassName = "treeNodeIcon ";
    let fNodeClickHandler = this.props.nodeCheckClickHandler || CS.noop;

    CS.forEach(aLeftSideButtons, (oButton) => {
      oButton.isDisabled && (fNodeClickHandler = CS.noop);
      aDom.push(
        <div className={sTreeNodeIconClassName + oButton.className} onClick={fNodeClickHandler}></div>
      )
    });
    return aDom;
  };

  getTypeIcon = (oNode) => {
    let sTypeIconClassName = oNode.typeIconClassName;
    let oTypeIconDOM = null;

    if(sTypeIconClassName) {
      oTypeIconDOM = (
          <div className={"treeNodeIcon " + sTypeIconClassName}></div>
      );
    }

    return oTypeIconDOM;
  };

  getCountDOM = (oNode) =>{
    if (this.props.showCount && CS.isNumber(oNode.count)) {
      return (
          <div className={"showCount"}>{"(" + oNode.count + ")"}</div>
      );
    } else {
      return null;
    }
  };

  render () {
    let __props = this.props;
    let oNode = __props.node;
    let fLoadChildNodesHandler = __props.loadChildNodesHandler;
    let sClassName = "treeNode";


    if(oNode.isExpanded) {
      sClassName += " selected ";
    }

    return (
      <div className={sClassName} key={oNode.id}>
        {this.getLeftSideActionButtonsDom()}
        {this.getTypeIcon(oNode)}
        <div className="treeNodeIconLabelDOM" onClick={fLoadChildNodesHandler}>
          {this.getIconView(oNode)}
          {this.getHeaderText()}
          {this.getCountDOM(oNode)}
        </div>
        <div className="rightSideActionButton">
          {this.getRightSideActionButtonsDom()}
        </div>
      </div>)
  }
}

export const view = TreeNodeView;
export const events = oEvents;
