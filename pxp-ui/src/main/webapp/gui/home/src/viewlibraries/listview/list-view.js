import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ListNodeView } from './list-node-view';
import ListViewModel from './model/list-view-model';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';

const oEvents = {
  LIST_ITEM_CREATED: "List item created",
  LIST_VIEW_SEARCH_OR_LOAD_MORE_CLICKED: "list_view_search_or_load_more_clicked"
};

const oPropTypes = {
  model: ReactPropTypes.array.isRequired,
  bListItemCreated: ReactPropTypes.bool,
  bEnableLoadMore: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  hideSearchBar: ReactPropTypes.bool
};
/**
 * @class ListView - use for display listView in Application.
 * @memberOf Views
 * @property {array} model -  a model name.
 * @property {bool} [bListItemCreated] -  boolean value for bListItemCreated or not.
 * @property {bool} [bEnableLoadMore] -  boolean value for bEnableLoadMore or not.
 * @property {string} [context] -  string which contain context.
 * @property {bool} [hideSearchBar] -  boolean value for hideSearchBar or not.
 */

// @CS.SafeComponent
class ListView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      searchText: ""
    }
  }

  static getDerivedStateFromProps(oNextProp, oState) {
    if(oNextProp.bListItemCreated) {
      EventBus.dispatch(oEvents.LIST_ITEM_CREATED);
      return {
        searchText: ""
      };
    }
  }

  /*componentWillReceiveProps(oNextProps) {
    if(oNextProps.bListItemCreated) {
      this.setState({
        searchText: ""
      });
      EventBus.dispatch(oEvents.LIST_ITEM_CREATED);
    }
  }*/

  handleListSearchTextChanged =(oEvent)=> {
    var sNewSearchValue = oEvent.target.value;
    this.setState({
      searchText: sNewSearchValue
    });
  }

  handleSearchTextBlurred = (oEvent) => {
    if (this.props.bEnableLoadMore) {
      this.handleListViewSearchOrLoadMoreClicked(false);
    }
  }

  handleOnKeyDown = (oEvent) => {
    if (oEvent.keyCode === 13) {
      this.handleListViewSearchOrLoadMoreClicked(false);
    }
  }

  handleListViewSearchOrLoadMoreClicked = (bLoadMore) => {
    let sSearchText = this.state.searchText;
    EventBus.dispatch(oEvents.LIST_VIEW_SEARCH_OR_LOAD_MORE_CLICKED, this.props.context, sSearchText, bLoadMore);
  }

  getListNodeView=()=> {
    var aModels = this.props.model;
    var aListNodeView = [];
    var sSearchText = this.state.searchText.toLowerCase();
    let bIsLoadMoreEnabled = this.props.bEnableLoadMore;

    CS.forEach(aModels, function (oModel, iIndex) {
      if (React.isValidElement(oModel)) {
        aListNodeView.push(oModel)
      } else {
        var sLabel = CS.getLabelOrCode(oModel);
        if (sLabel.toLowerCase().includes(sSearchText) || bIsLoadMoreEnabled) {
          aListNodeView.push(<ListNodeView key={oModel.label + "_" + iIndex} model={oModel}/>);
        }
      }
    });

    if(CS.isEmpty(aListNodeView) && sSearchText){
      aListNodeView.push(<div className="nothingFoundMessage">{getTranslation().NO_RESULT}</div>);
    }
    return aListNodeView;
  }

  getSearchView=()=> {
    // var aModels = this.props.model;

    // if(!CS.isEmpty(aModels)) {
      return (<div className="listSearchBar">
        <input className="listSearchInput"
               type="text"
               value={this.state.searchText}
               onChange={this.handleListSearchTextChanged}
               onBlur={this.handleSearchTextBlurred}
               onKeyDown={this.handleOnKeyDown}
               placeholder={getTranslation().SEARCH}/>
        <div className="searchBarIcon"></div>
      </div>);
    /*} else {
      return null;
    }*/
  }

  getLoadMoreDom = () => {
    let oLoadMoreDOM = null;
    if (this.props.bEnableLoadMore) {
      oLoadMoreDOM = (
          <div className="listViewLoadMore" onClick={this.handleListViewSearchOrLoadMoreClicked.bind(this, true)}>
            {getTranslation().LOAD_MORE}
          </div>);
    }

    return oLoadMoreDOM;
  }

  render() {
    let oSearchView = this.props.hideSearchBar ? null : this.getSearchView();
    let aListNodeView = this.getListNodeView();
    let oLoadMoreDOM = this.getLoadMoreDom();
    return (
        <div className="listViewContainer">
          {oSearchView}
          <div className="listView">
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
