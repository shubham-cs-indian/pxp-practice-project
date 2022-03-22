import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as TreeNodeView } from './tree-node-view';
import {view as SimpleSearchBarView} from "../simplesearchbarview/simple-search-bar-view";
import {view as NothingFoundView} from "../nothingfoundview/nothing-found-view";

const oEvents = {
  TREE_VIEW_DATA_RESET: "tree_view_data_reset",
  TREE_VIEW_LOAD_MORE_CLICKED: "tree_view_load_more_clicked",
  TREE_VIEW_TREE_SEARCHED: "tree_view_tree_searched"
};

const oPropTypes = {
  treeData: ReactPropTypes.object,
  showLevelInHeader: ReactPropTypes.bool,
  nodeCheckClickHandler: ReactPropTypes.func,
  loadChildNodesHandler: ReactPropTypes.func,
  context: ReactPropTypes.string,
  searchText: ReactPropTypes.string,
  rightSideActionButtonsHandler: ReactPropTypes,
  suppressUnmount: ReactPropTypes.bool,
  levelNames: ReactPropTypes.string,
  hideSearchbar: ReactPropTypes.bool,
  loadMoreHandler: ReactPropTypes.func,
  showCount: ReactPropTypes.bool
};

// @CS.SafeComponent
class TreeView extends React.Component {
  constructor (props) {
    super(props);
  }

  static defaultProps = {
    suppressUnmount: false
  }

  static propTypes = oPropTypes;

  state = {

  };

  componentWillUnmount = () => {
    let { suppressUnmount } = this.props;
    !suppressUnmount && EventBus.dispatch(oEvents.TREE_VIEW_DATA_RESET);
  };

  handleLoadMoreClicked = (sParentId) => {
    if (CS.isFunction(this.props.loadMoreHandler)) {
      this.props.loadMoreHandler(this.props.context, sParentId);
    }
    else {
      EventBus.dispatch(oEvents.TREE_VIEW_LOAD_MORE_CLICKED, this.props.context, sParentId, this.props.activeNodeId);
    }
  };

  handleTreeSearched = (sSearchText) => {
    EventBus.dispatch(oEvents.TREE_VIEW_TREE_SEARCHED, this.props.context, sSearchText, this.props.activeNodeId);
  };

  getDefaultLevelInfo = () => {
    return {
      view: [],
      parentId: "",
    };
  };

  setParentIdInLevelData = (oLevel, oListItem) => {
    let _this = this;
    let __props = _this.props;
    let sParentId = oListItem.parentId || (oListItem.parent && oListItem.parent.id) || ""
    if(__props.context === "classes") {
      oLevel.parentId = __props.context;
    } else if(sParentId === "-1" && (__props.context === "natureClasses" || __props.context === "attributionClasses")){
      oLevel.parentId = __props.context;
    } else {
      /* if searchText is present then parent id set to -1 because all taxonomy will be display on flat level. */
      oLevel.parentId = __props.searchText && "-1" || sParentId;
    }
  };

  getLevelView = (oTreeView, aTreeLevelList, iLevel) => {
    let _this = this;
    let __props = _this.props;

    oTreeView[iLevel] = oTreeView[iLevel] || this.getDefaultLevelInfo();
    _this.setParentIdInLevelData(oTreeView[iLevel], aTreeLevelList[0]);
    CS.forEach(aTreeLevelList, function (oListItem) {
      let fTreeCheckHandler = __props.nodeCheckClickHandler.bind(_this,oListItem.id);
      let fLoadChildNodeHandler = CS.isFunction(__props.loadChildNodesHandler) ? __props.loadChildNodesHandler.bind(_this, oListItem.id) : __props.loadChildNodesHandler;
      let oChildrenView =
          (<TreeNodeView key={oListItem.id}
                         node={oListItem}
                         searchText={__props.searchText}
                         nodeCheckClickHandler={fTreeCheckHandler}
                         loadChildNodesHandler={fLoadChildNodeHandler}
                         showCheckNodeAtAllLevels={true}
                         rightSideActionButtonsHandler={__props.rightSideActionButtonsHandler}
                         context={__props.context}
                         showCount={__props.showCount}
                         showEndNodeIndicator={__props.showEndNodeIndicator}

          />);

      oTreeView[iLevel].view.push(oChildrenView);
      if (oListItem.children && oListItem.children.length) {
        _this.getLevelView(oTreeView, oListItem.children, iLevel + 1);
      }
    });
    return oTreeView;
  };

  getLoadMoreView = (oLevel) => {
    let _this = this;
    let oTreeLoadMoreMap = _this.props.treeLoadMoreMap || {};
    let bShowLoadMore = oTreeLoadMoreMap[oLevel.parentId || "-1"];

    if(bShowLoadMore) {
      return (
          <div className="loadMore"
               onClick={_this.handleLoadMoreClicked.bind(this, oLevel.parentId)}>
            {getTranslation().LOAD_MORE}
          </div>
      )
    }
    else {
      return null;
    }
  };

  getTreeLevelView = () => {
    let _this = this;
    let __props = _this.props;
    let oTreeData = __props.treeData;
    let iLevel = 0;
    let oTreeView = {};
    oTreeView = this.getLevelView(oTreeView, oTreeData, iLevel);

    let aLevelView = [];
    let iLevelCount =  1;
    CS.forEach(oTreeView, function (oLevelView, iKey) {
      let sLevelTranslation = CS.isNotEmpty(__props.levelNames) ? (__props.levelNames[iLevelCount - 1] && __props.levelNames[iLevelCount - 1] || "")  : getTranslation().LEVEL;
      let oLevelHeaderDom = __props.showLevelInHeader ? <div className="levelHeader">{sLevelTranslation + " " +
      (CS.isNotEmpty(__props.levelNames) ? "" : iLevelCount)}</div> : null;
      let sClassName = __props.showLevelInHeader ? "levelListItems withHeader" : "levelListItems";

      aLevelView.push(
          <div className="treeLevel" key={iKey}>
            {oLevelHeaderDom}
            <div className={sClassName}>
                <div className={"treeLevelView"}>
                  {oLevelView.view}
                  {_this.getLoadMoreView(oLevelView)}
                </div>
            </div>
          </div>
      );
      iLevelCount += 1;
    });

    return aLevelView;
  };



  getSearchView = () => {
    if(!this.props.hideSearchbar) {
      return (
          <div className="treeSearchBarContainer">
            <SimpleSearchBarView onChange={this.handleTreeSearched} searchText={this.props.searchText}/>
          </div>);
    }
  };

  getTreeView = () => {
    if (CS.isEmpty(this.props.treeData)) {
      return (
          <div className="treeLevelContainer">
            <NothingFoundView message={getTranslation().NOTHING_TO_DISPLAY}/>
          </div>
      )
    }
    let aTreeLevelViews = this.getTreeLevelView();
    return (
        <div className="treeLevelContainer">
          {/*required for horizontal scrollbar for taxonomy levels.*/}
          <div className="treeWrapper">
            {aTreeLevelViews}
          </div>
        </div>
    );
  };

  render() {
    return (
        <div className="treeViewWrapper">
          {this.getSearchView()}
          {this.getTreeView()}
        </div>
    );
  }
};

export const view = TreeView;
export const events = oEvents;
