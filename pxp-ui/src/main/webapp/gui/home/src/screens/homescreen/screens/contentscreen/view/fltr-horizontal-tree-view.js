import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as HorizontalTreeNodeView } from './fltr-horizontal-tree-node-view';
import { view as HorizontalTreeChildNodeView } from './fltr-horizontal-tree-child-node-view';
import ViewUtils from './utils/view-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_TREE_CHECK_CLICKED: "handle_tree_check_clicked",
  HANDLE_TREE_HEADER_CHECK_CLICKED: "handle_tree_header_check_clicked",
  HANDLE_TREE_LAZY_CHILDREN_DATA: "handle_tree_lazy_children_data",
  FLTR_HIERARCHY_DROP_DOWN_LIST_POPOVER_CLOSED: "fltr_hierarchy_drop_down_list_popover_closed",
};

const oPropTypes = {
  taxonomyTree: ReactPropTypes.array,
  taxonomyVisualProps: ReactPropTypes.object,
  matchingTaxonomyIds: ReactPropTypes.array,
  hideEmpty: ReactPropTypes.bool,
  taxonomyTreeSearchText: ReactPropTypes.string,
  parentTaxonomyId: ReactPropTypes.string,
  affectedTreeNodeIds: ReactPropTypes.array,
  handleTreeCheckClicked: ReactPropTypes.func,
};

// @CS.SafeComponent
class FltrHorizontalTreeView extends React.Component {
  constructor (props) {
    super(props);

    this.setRef =( sId, element) => {
      this["plusIcon" + sId] = element;
    };
  }
  static propTypes = oPropTypes;

  state = {

  };

  handleTreeCheckClicked = (iNodeId, iLevel) => {
    if (this.props.handleTreeCheckClicked) {
      this.props.handleTreeCheckClicked(iNodeId, iLevel, this.props.filterContext);
    } else {
      EventBus.dispatch(oEvents.HANDLE_TREE_CHECK_CLICKED, this, iNodeId, iLevel, this.props.filterContext);
    }
  };

  handleLoadLazyTreeChildrenData = (iNodeId, iLevel) => {
    EventBus.dispatch(oEvents.HANDLE_TREE_LAZY_CHILDREN_DATA, iNodeId, iLevel, this.props.filterContext);
  };

  handleTreeHeaderCheckClicked = (iNodeId, iLevel) => {
    EventBus.dispatch(oEvents.HANDLE_TREE_HEADER_CHECK_CLICKED, this, iNodeId, iLevel, this.props.filterContext);
  };

  handleRequestPopoverClose = () => {
    EventBus.dispatch(oEvents.FLTR_HIERARCHY_DROP_DOWN_LIST_POPOVER_CLOSED, this.props.filterContext);
  };

  getFormattedDataForTreeView = (aRes, oNode, oParentNode, iLevel) => {
    var oVisualProps = this.props.taxonomyVisualProps;
    iLevel = iLevel || 1;
    oParentNode = oParentNode || {id: -1, label: ""};

    var _this = this;
    var oLevelObj = CS.find(aRes, {id: iLevel});

    if (!oLevelObj) {
      oLevelObj = {
        id: iLevel,
        nodes: []
      };

      aRes.push(oLevelObj);
    }

    var oGroupNode = CS.find(oLevelObj.nodes, {id: oParentNode.id});

    if (!oGroupNode) {
      var oVisualProp = oVisualProps[oParentNode.id] || {};
      oGroupNode = {
        id: oParentNode.id,
        label: CS.getLabelOrCode(oParentNode),
        isExpanded: oVisualProp.isExpanded,
        count: oParentNode.count,
        isChecked: oVisualProp.isChecked,
        children: []
      };

      oLevelObj.nodes.push(oGroupNode);
    }

    oGroupNode.children.push(oNode);

    var bHideEmpty = this.props.hideEmpty;
    var oChildVisualProp = oVisualProps[oNode.id] || {};
    if (!oChildVisualProp.isHidden) {
      CS.forEach(oNode.children, function (oChild) {
        if (!bHideEmpty || oChild.count != 0) {
        _this.getFormattedDataForTreeView(aRes, oChild, oNode, iLevel + 1)
        }
      });
    }

    return aRes;
  };

  //New view without preprocessing data
  getNewView = (oViews, oNode, iLevel) => {
    var _this = this;
    var aChildrenView = [];
    oViews[iLevel] = oViews[iLevel] || [];

    var aAffectedTreeNodeIds = this.props.affectedTreeNodeIds;
    var oVisualProps = this.props.taxonomyVisualProps;
    var aMatchingTaxonomyIds = this.props.matchingTaxonomyIds;
    var sSearchText = this.props.taxonomyTreeSearchText;
    var oNodeProp = oVisualProps[oNode.id];
    var bHideEmpty = this.props.hideEmpty;
    var sParentTaxonomyId = this.props.parentTaxonomyId;
    var aChildren = oNode.children;
    var oHeaderView = null;
    var sHeaderContClassName = "horizontalTreeGroupHeader ";
    sHeaderContClassName += iLevel == 0 ? "zeroLevelGroupHeader " : "";

    if (oNode.label && iLevel != 0) {
      var sHeaderClassName = "treeNodeCheck ";
      if (oNodeProp && oNodeProp.isChecked == 1) {
        sHeaderClassName += "partialChecked";
      } else if (oNodeProp && oNodeProp.isChecked == 2) {
        sHeaderClassName += "checked";
      }
      let oCodeName = "";
      if(oNode.id !== "-1"){       // skipping root taxonomy as there is no code associated.
        oCodeName = oNode.label ? " - " + oNode.code: null;
      }
      oHeaderView = (
          <div className={sHeaderContClassName}
               key={oNode.id}
               onClick={_this.handleTreeHeaderCheckClicked.bind(_this, oNode.id, iLevel+1)}>
            <div className={sHeaderClassName}></div>
            {CS.getLabelOrCode(oNode)}
            {oCodeName}
          </div>
      );
    } else {
      var oStyle = {};
      oStyle.marginLeft = 14;
      oHeaderView = (
          <div className={sHeaderContClassName} style={oStyle}>
            {CS.getLabelOrCode(oNode)}
          </div>
      );
    }

    CS.forEach(aChildren, function (oChild) {
      var oChildVisualProp = oVisualProps[oChild.id] || {};

      if (!bHideEmpty || (oChild.count && oChild.count != 0)) {
        var oHeaderText = null;
        if (aMatchingTaxonomyIds.length != 0 && aMatchingTaxonomyIds.indexOf(oChild.id) != -1) {
          oHeaderText = ViewUtils.getHighlightedHeaderText(CS.getLabelOrCode(oChild), sSearchText, "taxonomyTreeHighlightedText");
        }
        var fTreeCheckHandler = _this.handleTreeCheckClicked.bind(_this, oChild.id, iLevel+1);
        var fLoadChildNodesHandler = _this.handleLoadLazyTreeChildrenData.bind(_this, oChild.id, iLevel+1);
        aChildrenView.push(
            <HorizontalTreeChildNodeView  key={oChild.id}
                                          node={oChild}
                                          level={iLevel}
                                          parentTaxonomyId={sParentTaxonomyId}
                                          matchingTaxonomyIds={aMatchingTaxonomyIds}
                                          searchText={sSearchText}
                                          visualProps={oVisualProps}
                                          nodeCheckClickHandler={fTreeCheckHandler}
                                          loadChildNodesHandler={fLoadChildNodesHandler}
                                          headerText={oHeaderText}
                                          affectedTreeNodeIds={aAffectedTreeNodeIds}

            />
        );

        if (!oChildVisualProp.isHidden && oChild.children && oChild.children.length) {
          _this.getNewView(oViews, oChild, iLevel + 1);
        }
      }
    });

    var oNodeView = <HorizontalTreeNodeView key={oNode.id}
                                            nodeId={oNode.id}
                                            headerView={oHeaderView}
                                            childrenViewArray={aChildrenView}
                                            affectedTreeNodeIds={aAffectedTreeNodeIds}
    />;

    oViews[iLevel].push(oNodeView);

    return oViews;
  };

  //New view without preprocessing data
  getNewViewWrap = () => {
    var _this = this;
    var __props = _this.props;
    var oAllTrees = {
      isChecked: 2,
      id: 999,
      label: getTranslation().TAXONOMIES,
      children: __props.taxonomyTree
    };


    var oViews = this.getNewView({}, oAllTrees, 0);

    var aLevelView = [];
    CS.forEach(oViews, function (oLevelView, iKey) {
      aLevelView.push(
          <div className="horizontalTreeLevelContainer" key={iKey}>
              {oLevelView}
          </div>
      )
    });

    return (<div className="horizontalTreeViewContainer">
      {aLevelView}
    </div>);
  };

  render() {

    let aViews = this.getNewViewWrap();
    return (
        <div className="klassTreeViewContainer">
          {aViews}
        </div>
    );
  }
}

export const view = FltrHorizontalTreeView;
export const events = oEvents;
