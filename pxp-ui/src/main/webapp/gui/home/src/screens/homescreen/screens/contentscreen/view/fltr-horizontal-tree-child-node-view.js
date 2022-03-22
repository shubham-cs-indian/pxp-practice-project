import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
const oEvents = {};

const oPropTypes = {
  node: ReactPropTypes.object,
  level: ReactPropTypes.number,
  parentTaxonomyId: ReactPropTypes.string,
  matchingTaxonomyIds: ReactPropTypes.array,
  searchText: ReactPropTypes.string,
  visualProps: ReactPropTypes.object,
  nodeCheckClickHandler: ReactPropTypes.func,
  loadChildNodesHandler: ReactPropTypes.func,
  headerText: ReactPropTypes.object,
  affectedTreeNodeIds: ReactPropTypes.array,
};

// @CS.SafeComponent
class HorizontalTreeChildNodeView extends React.Component {
  static propTypes = oPropTypes;

  shouldComponentUpdate(oNextProps) {
    var sNodeId = oNextProps.node.id;
    var aNewAffectedIds = oNextProps.affectedTreeNodeIds;

    if (CS.includes(aNewAffectedIds, sNodeId) || CS.isEmpty(aNewAffectedIds)) {
      return true;
    }

    return false;
  }

  render() {
    var __props = this.props;
    var oNode = __props.node;
    var iLevel = __props.level;
    var sParentTaxonomyId = __props.parentTaxonomyId;
    var aMatchingTaxonomyIds = __props.matchingTaxonomyIds;
    var sSearchText = __props.searchText;
    var oVisualProps = __props.visualProps;
    var fNodeClickHandler = __props.nodeCheckClickHandler;
    var fLoadChildNodesHandler = __props.loadChildNodesHandler;
    var oHeaderTextFromProps = __props.headerText;

    var oChildVisualProp = oVisualProps[oNode.id] || {};

    var sClassName = "treeNodeCheck ";
    if (oChildVisualProp.isChecked == 1) {
      sClassName += "partialChecked";
    } else if (oChildVisualProp.isChecked == 2) {
      sClassName += "checked";
    }

    var sNodeContainerClassName = "horizontalTreeGroupElContainer ";
    sNodeContainerClassName += iLevel == 0 ? "zeroLevelGroupEl " : "";

    var oCheckNode = (<div className={sClassName} onClick={fNodeClickHandler}></div>);
    if (iLevel == 0) {
      oCheckNode = null;
      if ((sParentTaxonomyId == oNode.id) || (oNode.id == -1 && sParentTaxonomyId == "")) {
        sNodeContainerClassName += "active ";
      }
    }

    var oCountSpan = (oNode.count >= 0) ? (
            <span className="countSpan">{" (" + (oNode.count || 0) + ")"}</span>) : null;
    var sChildId = oNode.id;
    var sExpandedClass = "";
    if(!CS.isEmpty(oNode.children)) {
      sExpandedClass = oChildVisualProp.isHidden ? '' : ' groupExpanded';
    }
    var sLabelClassName = "horizontalTreeGroupElLabel" + sExpandedClass;
    var oHeaderText = (<span className="labelSpan" title={CS.getLabelOrCode(oNode)}>{CS.getLabelOrCode(oNode)}</span>);
    var oCodeName = null;
    if (sSearchText) {
      if (aMatchingTaxonomyIds.length != 0 && aMatchingTaxonomyIds.indexOf(sChildId) != -1) {
        oHeaderText = oHeaderTextFromProps;
      } else {
        sLabelClassName = "horizontalTreeGroupGreyedElLabel";
      }
    }

    var fTempNodeClickHandler = CS.isEmpty(oCheckNode)? fNodeClickHandler : fLoadChildNodesHandler;
    return (
        <div className={sNodeContainerClassName} key={oNode.id}>
          {oCheckNode}
          <div className={sLabelClassName} onClick={fTempNodeClickHandler}>
            {oHeaderText}
            {oCodeName}
            {oCountSpan}
          </div>
        </div>)
  }
}

export const view = HorizontalTreeChildNodeView;
export const events = oEvents;
