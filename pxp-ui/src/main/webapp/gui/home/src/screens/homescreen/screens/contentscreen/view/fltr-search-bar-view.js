import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_SUBMIT_SEARCH_TEXT: "handle_submit_search_text",
};

const oPropTypes = {
  searchText: ReactPropTypes.string,
  searchInputSize: ReactPropTypes.number,
  isFilterAndSearchViewDisabled: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object.isRequired,
  selectedHierarchyContext: ReactPropTypes.string
};

// @CS.SafeComponent
class SearchBarView extends React.Component {
  constructor(props) {
    super(props);
    this.searchInput = React.createRef();
  }

  static propTypes = oPropTypes;

  state = {
    searchText: this.props.searchText,
    typing: false
  };

  static getDerivedStateFromProps (oNextProps, oState) {
    let sSearchText = oState.typing? oState.searchText : oNextProps.searchText;
    return {
      searchText: sSearchText,
      typing: false
    }
  }

  handleSubmitSearchText = (sSearchText) => {
    let sSelectedHierarchyContext = this.props.selectedHierarchyContext;
    EventBus.dispatch(oEvents.HANDLE_SUBMIT_SEARCH_TEXT, this, sSearchText, sSelectedHierarchyContext, this.props.filterContext);
  };

  handleClearSearchText = () => {
    let sSelectedHierarchyContext = this.props.selectedHierarchyContext;
    this.setState({
      searchText: ""
    });
    EventBus.dispatch(oEvents.HANDLE_SUBMIT_SEARCH_TEXT, this, "", sSelectedHierarchyContext,this.props.filterContext);
  };

  handleSearchTextChanged = (oEvent) => {
    var sSearchText = oEvent.target.value;
    this.setState({
      searchText: sSearchText,
      typing: true
    });
  };

  watchText = (oEvent) => {
    if (oEvent.key == 'Enter') {
      var oSearchInputDom = this.searchInput.current;
      if(oSearchInputDom){
        var sSearchText = oSearchInputDom.value;
        this.applySearch(sSearchText);
      }
    }
  };

  applySearch = (sSearchText) => {
    this.handleSubmitSearchText(sSearchText);
  };

  getSearchInputDom = () => {
    var sSearchInputClass = "searchInput size_" + this.props.searchInputSize;
    var sPlaceHolder = getTranslation().SEARCH;
    if(!this.props.isFilterAndSearchViewDisabled) {
      return (<input className={sSearchInputClass}
                     onKeyPress={this.watchText}
                     onChange={this.handleSearchTextChanged}
                     value={this.state.searchText}
                     type="text"
                     ref={this.searchInput}
                     placeholder={sPlaceHolder}/>);
    } else {
      sPlaceHolder = ""
      return (<input className={sSearchInputClass + " disabled"}
                     value={this.state.searchText}
                     type="text"
                     ref={this.searchInput}
                     disabled="disabled"
                     placeholder={sPlaceHolder}/>);
    }
  };

  render() {

    var sSearchBarContainerClass = !this.props.isFilterAndSearchViewDisabled ?
                                    "searchBarContainer " :
                                    "searchBarContainer disabled ";

    var oSearchIconView = this.state.searchText.length ? (<TooltipView placement="bottom" label={getTranslation().CLEAR}>
      <div className="searchClearIcon" onClick={this.handleClearSearchText}></div></TooltipView>) : null;

    return (
        <div className="searchBarWrapper">
          <div className={sSearchBarContainerClass}>
            <div className="searchInputIcon"></div>
            {this.getSearchInputDom()}
            {oSearchIconView}
          </div>
        </div>
    );
  }
}

export const view = SearchBarView;
export const events = oEvents;
