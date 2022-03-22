import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ListNodeView } from './list-node-view-new';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import {view as SimpleSearchBarView} from "../simplesearchbarview/simple-search-bar-view";

const oEvents = {
  LIST_VIEW_SEARCH_OR_LOAD_MORE_CLICKED: "list_view_search_or_load_more_clicked",
  LIST_VIEW_NEW_ACTION_ITEM_CLICKED: "LIST_VIEW_NEW_ACTION_ITEM_CLICKED",
};

const oPropTypes = {
  model: ReactPropTypes.array.isRequired,
  bListItemCreated: ReactPropTypes.bool,
  bEnableLoadMore: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  hideSearchBar: ReactPropTypes.bool,
  customHeaderView: ReactPropTypes.object,
  searchText: ReactPropTypes.string
};
/**
 * @class ListView - use for display listView in Application.
 * @memberOf Views
 * @property {array} model -  a model name.
 * @property {bool} [bListItemCreated] -  boolean value for bListItemCreated or not.
 * @property {bool} [bEnableLoadMore] -  boolean value for bEnableLoadMore or not.
 * @property {string} [context] -  string which contain context.
 */

// @CS.SafeComponent
class ListView extends React.Component {

  constructor(props) {
    super(props);
  }

  handleSearchTextBlurred = (sSearchText) => {
    this.handleListViewSearchOrLoadMoreClicked(false, sSearchText);
  }

  handleListViewSearchOrLoadMoreClicked = (bLoadMore, sSearchText) => {
    EventBus.dispatch(oEvents.LIST_VIEW_SEARCH_OR_LOAD_MORE_CLICKED, this.props.context, sSearchText, bLoadMore);
  };

  handleActionButtonClicked = (sId, sContext) => {
    EventBus.dispatch(oEvents.LIST_VIEW_NEW_ACTION_ITEM_CLICKED, sId, sContext);
  };

  getListNodeView=()=> {
    var aModels = this.props.model;
    var aListNodeView = [];
    var sSearchText = this.props.searchText && this.props.searchText.toLowerCase();
    let bIsLoadMoreEnabled = this.props.bEnableLoadMore;
    let sContext = this.props.context;

    CS.forEach(aModels, function (oModel, iIndex) {
      if (React.isValidElement(oModel)) {
        aListNodeView.push(oModel)
      } else {
        var sLabel = CS.getLabelOrCode(oModel);
        if (sLabel.toLowerCase().includes(sSearchText) || bIsLoadMoreEnabled) {
          aListNodeView.push(<ListNodeView key={oModel.label + "_" + iIndex} model={oModel}
                                           context={sContext} />);
        }
      }
    });

    if(CS.isEmpty(aListNodeView)){
      aListNodeView.push(<div key="noRes" className="nothingFoundMessage">{getTranslation().NO_RESULT}</div>);
    }
    return aListNodeView;
  };

  getSearchView = () => {
    return (<div className="searchBarContainer">
      <SimpleSearchBarView searchText={this.props.searchText}
                           //onChange={this.handleListSearchTextChanged}
                           onBlur={this.handleSearchTextBlurred}
                           placeholder={getTranslation().SEARCH}/>
    </div>);
  };

  getActionItemButtonDOM = () => {
    let oActionItemButton = this.props.listViewActionItem;
    let aButtonDOM = [];
    let _this = this;

    CS.forEach(oActionItemButton, function (oButton) {
      aButtonDOM.push(
        <div className={oButton.className} onClick={_this.handleActionButtonClicked.bind(this, oButton.id, _this.props.context)}>{oButton.label}</div>
      )
    });
    return (
        <div className="actionItemButton">
          {aButtonDOM}
        </div>
    )
  };

  getLoadMoreDom = () => {
    let oLoadMoreDOM = null;
    if (this.props.bEnableLoadMore) {
      oLoadMoreDOM = (
          <div className="listViewLoadMore" onClick={this.handleListViewSearchOrLoadMoreClicked.bind(this, true, this.props.searchText)}>
            {getTranslation().LOAD_MORE}
          </div>);
    }

    return oLoadMoreDOM;
  }

  render() {
    let oSearchView = this.props.hideSearchBar ? null : this.getSearchView();
    let oActionItemButtonDOM = this.props.listViewActionItem ? this.getActionItemButtonDOM() : null;
    let oCustomHeaderView = this.props.customHeaderView ? <div className="customHeaderView"> {this.props.customHeaderView} </div> : null;
    let aListNodeView = this.getListNodeView();
    let oLoadMoreDOM = this.getLoadMoreDom();
    let sListClassName = this.props.bEnableLoadMore ?  "listView LoadMoreAdded" : "listView";
    return (
        <div className="listViewNewContainer">
          <div className="wrapper">
            {oSearchView}
            {oActionItemButtonDOM}
          </div>
          {oCustomHeaderView}
            <div className={sListClassName}>
              {aListNodeView}
            </div>
            {oLoadMoreDOM}
        </div>
    );
  }
}

ListView.propTypes = oPropTypes;

export const view = ListView;
export const events = oEvents;
