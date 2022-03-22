import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../tooltipview/tooltip-view';
import {view as TabLayoutView} from "../tablayoutview/tab-layout-view";

var oEvents = {
  HANDLE_ACTION_ITEM_CLICKED: "handle_action_item_clicked",
  HANDLE_HIERARCHY_TREE_TAB_CHANGED: "handle_hierarchy_tree_tab_changed"
};

const oPropTypes = {
  selectedContext: ReactPropTypes.string,
  level: ReactPropTypes.number,
  node: ReactPropTypes.object,
  visualProp: ReactPropTypes.object,
  parentNode: ReactPropTypes.object,
  showSearch: ReactPropTypes.bool,
  showLoadMore: ReactPropTypes.bool,
  searchText: ReactPropTypes.string,
  onLoadMore: ReactPropTypes.func,
  onSearch: ReactPropTypes.func,
  style: ReactPropTypes.object
};
/**
 * @class HorizontalTreeNodeView
 * @memberOf Views
 * @property {string} [selectedContext] - Selected context.
 * @property {number} [level] - Level of tree.
 * @property {object} [node] - Contains all information about tree item(ex: id, label, code, type, task etc).
 * @property {object} [parentNode] - Parent node information.
 * @property {bool}   [showSearch] - To show search bar on dropdown list.
 * @property {bool}   [showLoadMore] - To show loadmore option on dropdown list.
 * @property {string} [searchText] - Contains text which you have searched.
 * @property {func}   [onLoadMore] - Executes when load more option is clicked.
 * @property {func}   [onSearch] - Executes when text is searched.
 * @property {object} [style] - CSS style for HorizontalTreeNodeView.
 */

// @CS.SafeComponent
class HorizontalTreeNodeView extends React.Component {
  constructor(props) {
    super(props);

    this.hierarchySearchInput = React.createRef();
  }

  static propTypes = oPropTypes;

  componentDidMount() {
    this.updateSearchInput();
  }

  componentDidUpdate() {
    this.updateSearchInput();
  }

  updateSearchInput = () => {
    var oSearchInputDOM = this.hierarchySearchInput.current;
    if (oSearchInputDOM) {
      oSearchInputDOM.value = this.props.searchText || "";
    }
  };

  onSearch = (sSearchText) => {
    if (this.props.onSearch) {
      this.props.onSearch(this.props.level - 1, this.props.node, this.props.parentNode, sSearchText);
    }
  };

  handleLevelActionItemClicked =(sAction, sNodeId) => {
    let sContext = this.props.selectedContext;
    EventBus.dispatch(oEvents.HANDLE_ACTION_ITEM_CLICKED, sAction, sNodeId, sContext);
  }

  handleHierarchySearched = (oEvent) => {
    var sSearchText = oEvent.target.value;
    this.onSearch(sSearchText);
  };

  handleHierarchySearchCleared = () => {
    this.onSearch("");
  };

  handleSearchKeyPressed = (oEvent) => {
    if (oEvent.key === 'Enter' || oEvent.keyCode === 13) {
      oEvent.target.blur();
    }
  };

  handleLoadMoreClicked = (oEvent) => {
    if (this.props.onLoadMore) {
      this.props.onLoadMore(this.props.level - 1, this.props.node, this.props.parentNode, oEvent);
    }
  };

  getHeaderNodeView = () => {
    var oNode = this.props.node;
    var iLevel = this.props.level;
    var oHeaderView = null;
    var sHeaderContClassName = "contentHorizontalTreeGroupHeader";
    let sLabel = CS.getLabelOrCode(oNode);
    var oVisualProp = this.props.visualProp;
    var bCanCreate = CS.isEmpty(oVisualProp) ? false : oVisualProp.canCreate === undefined ? false : oVisualProp.canCreate;
    if (bCanCreate) {
      oHeaderView = (
          <div className={sHeaderContClassName} key={oNode.id}>
            <div className="levelLabel">{sLabel}</div>
            <TooltipView label={getTranslation().CREATE} placement={"bottom"}>
              <div className={"levelActionItem " + "create"}
                   onClick={this.handleLevelActionItemClicked.bind(this, "create", oNode.id)}></div>
            </TooltipView>
          </div>
      );
    } else if (oNode.label && iLevel === 1) {
      oHeaderView = (
          <TooltipView label={sLabel}>
            <div className={sHeaderContClassName} key={oNode.id}>
              {sLabel}
            </div>
          </TooltipView>
      );
    }
    else if (iLevel > 1) {
      oHeaderView = (
          <TooltipView label={sLabel + " (" + oNode.relationshipLabel + ")"}>
            <div className={sHeaderContClassName} key={oNode.id}>
              <div className="classLabel">{sLabel}</div>
              <div className="relationshipLabel">{oNode.relationshipLabel}</div>
            </div>
          </TooltipView>
      );
    }
    else {
      oHeaderView = (
          <div className={sHeaderContClassName} key={oNode.id}>
            {sLabel}
          </div>
      );
    }
    return oHeaderView;
  };

  handleTabClicked = (sTabId) => {
    if (sTabId === this.props.selectedTabId) {
      return;
    }
    EventBus.dispatch(oEvents.HANDLE_HIERARCHY_TREE_TAB_CHANGED, sTabId, this.props.selectedContext);
  };

  getTabLayoutView = () => {
    let oNode = this.props.node;
    if(CS.isEmpty(oNode.tabsData)){
      return null;
    }
    let oTabData = oNode.tabsData;
    return (
        <TabLayoutView
            tabList={oTabData.tabList}
            activeTabId={oTabData.selectedTabId}
            addBorderToBody={false}
            onChange={this.handleTabClicked}>
        </TabLayoutView>);
  };

  getSearchView = () => {
    var oSearchView = null;

    if (this.props.showSearch) {
      oSearchView = (
          <div className="hierarchySearchContainer">
            <input className="hierarchySearchInput" type="text" ref={this.hierarchySearchInput}
                   onBlur={this.handleHierarchySearched}
                   onKeyPress={this.handleSearchKeyPressed}
            />
            {this.props.searchText ? <div className="searchClearIcon" onClick={this.handleHierarchySearchCleared}/> : null}
          </div>
      );
    }
    return oSearchView;

  };

  render() {

    var oNode = this.props.node;

    var oHeaderView = this.getHeaderNodeView();
    let oTabLayoutView = this.getTabLayoutView();
    var oSearchView = this.getSearchView();

    var aChildren = this.props.children;

    let sClassName = "contentHorizontalTreeGroupContainer expanded ";
    if(oHeaderView){
      sClassName+="withHeader "
    }
    if(oSearchView){
      sClassName+="withSearch "
    }

    return (
        <div className={sClassName} style={this.props.style} key={oNode.id}>
          {oHeaderView}
          {oTabLayoutView}
          {oSearchView}
          <div className="contentHorizontalTreeGroupNodesWrapper">
              <div className="contentHorizontalTreeGroupNodes">
                {aChildren}
                {CS.isEmpty(aChildren) ?
                  <div className="horizontalTreeNoContentFound">{getTranslation().STORE_ALERT_NO_CONTENT_FOUND}</div> :
                  this.props.showLoadMore ?
                    <div className="hierarchyLoadMore" onClick={this.handleLoadMoreClicked}>
                      {getTranslation().LOAD_MORE}
                    </div> :
                    null
                } </div>
          </div>
          </div>
    );
  }
}

export const view = HorizontalTreeNodeView;
export const events = oEvents;
