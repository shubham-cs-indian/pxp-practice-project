import CS from '../../libraries/cs';
import ReactDOM from 'react-dom';
import React from 'react';
import ReactPropTypes from 'prop-types';
import TooltipView from "../tooltipview/tooltip-view";
import ViewUtils from "../utils/view-library-utils";
import {view as NothingFoundView} from "../nothingfoundview/nothing-found-view";
import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";
import {view as CustomPopperView} from "../customPopperView/custom-popper-view";
import ClickAwayListener from "@material-ui/core/ClickAwayListener";
import KeyboardNavigationForLists, {oCustomEvents} from "../../commonmodule/HOC/keyboard-navigation-for-lists";
import CustomSearchBarPopperViewStore from "./../../screens/homescreen/screens/settingscreen/store/helper/custom-searchbar-popper-view-store";

const oPropTypes = {
  items: ReactPropTypes.array,
  onKeyPressHandler: ReactPropTypes.func,
  registerCustomEvent: ReactPropTypes.func,
  itemInFocus: ReactPropTypes.number,
  setIndexMap: ReactPropTypes.func,
  bDisableAutoFocus: ReactPropTypes.bool,
  oStyle: ReactPropTypes.object,
  requestApiData: ReactPropTypes.object,
  themeLoaderConfigure: ReactPropTypes.bool,
  itemClickHandler: ReactPropTypes.func
};
const constant = {
  maxItemToShow: 7,
  maxHeightOfItem: 36
};

// @KeyboardNavigationForLists
// @CS.SafeComponent
class CustomSearchBarPopperView extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      sSearchText: "",
      sAutoHeight: constant.maxItemToShow * constant.maxHeightOfItem,
      aLists: [],
      store: null
    };

    this.setRef = (sRef, element) => {
      this[sRef] = element;
    };
    this.initializeStore();
  }

  /**
   * Initialize the store and set it's instance in view state
   */
  initializeStore = () => {
    let oProps = this.props;
    let oInitializeData = {
      requestApiData: oProps.requestApiData,
      items: oProps.items
    };
    this.state.store = new CustomSearchBarPopperViewStore(oInitializeData);
  };

  componentDidMount() {
    CustomSearchBarPopperViewStore.prototype.bind('custom-searchBar-changed', this.stateChanged);
    this.props.registerCustomEvent(oCustomEvents.SELECTION_OF_ITEM, this.handleEnterKey);
    this.handleAdjustHeightForPopperView();
  };

  componentDidUpdate() {
    this.handleAdjustHeightForPopperView();
  };

  componentWillUnmount() {
    CustomSearchBarPopperViewStore.prototype.unbind('custom-searchBar-changed', this.stateChanged);
  };

  static getDerivedStateFromProps(oNextProps, oState) {
    if (!oState.sSearchText && oNextProps.items) {
      oState.store.setItems(oNextProps.items);
      return ({aLists: oNextProps.items})
    } else {
      return ({aLists: oState.store.getItems()})
    }
  };

  stateChanged = () => {
    let aLists = this.state.store.getItems();
    if (!CS.isEmpty(aLists)) {
      this.setState({
        aLists: aLists
      });
    }
  };

  /**
   * Call search api in case of Api Integration else handle enterkey pressed on list element
   * @param {Number} iIndex index of item in the list
   */
  handleEnterKey = (iIndex) => {
    let oState = this.state;
    if (iIndex >= 0) {
      let aListOfItems = oState.aLists;
      let oItem = aListOfItems[iIndex];
      this.handleItemAction(oItem.id, oItem.parentId, oItem.path)
    } else {
      oState.store.handleConfigModuleSearchBar(oState.sSearchText);
    }
  };

  /**
   * Auto resize the dialog based on the list of items
   */
  handleAdjustHeightForPopperView = () => {
    let oState = this.state;
    let aItems = oState.sSearchText ? oState.store.getItems() : oState.aLists;
    let sTotalItem = aItems ? aItems.length : 0;
    let sSearchItemHeightToShow = sTotalItem * constant.maxHeightOfItem;
    let iMaxHeight = constant.maxItemToShow * constant.maxHeightOfItem;
    let sAutoHeightShouldBe = (sTotalItem === 0) ? iMaxHeight : (iMaxHeight > sSearchItemHeightToShow && (sTotalItem !== 0)) ? sSearchItemHeightToShow : iMaxHeight;
    if (oState.sAutoHeight !== sAutoHeightShouldBe) {
      this.setState({
        sAutoHeight: sAutoHeightShouldBe,
      });
    }
  };

  /**
   * Search operation handler
   * @param {object} oEvent Click event object
   */
  handleSearchOperation = (oEvent) => {
    let oProps = this.props;
    let oState = this.state;
    let sSearchText = oEvent.target.value;
    this.setState({
      sSearchText: sSearchText,
    });
    if (oProps.requestApiData) return false;
    let sSearchTextForSearch = sSearchText.toLocaleLowerCase();
    oState.store.handleConfigModuleSearchBar(sSearchTextForSearch);
    oProps.setItemInFocus(-1);
  };

  /**
   * handle action on click of list
   * @param {String} sItemId element id on which click is triggered
   * @param {String} sParentId parent id of the element
   */
  handleItemAction = (sItemId, sParentId, aPath) => {
    let oProps = this.props;
    let oState = this.state;
    oProps.itemClickHandler ? oProps.itemClickHandler(sItemId, sParentId, aPath) : oState.store.handleConfigModuleSearchBarItemClicked(sItemId, sParentId, aPath);
    this.setState({
      sSearchText: "",
    });
    oProps.setItemInFocus(-1);
  };

  /**
   * Clear the search text & reset the list
   */
  clearSearchInput = () => {
    this.setState({
      sSearchText: "",
      sAutoHeight: constant.maxItemToShow * constant.maxHeightOfItem
    });
    this.state.store.handleConfigModuleSearchClearInputClicked();
  };

  /**
   * createPathDOM for config searchBar view only...
   * **/
  createPathDOM = (aPath) => {
    let sPath = "";
    let sSlash = " / ";
    CS.forEach(aPath, (path) => {
      sPath = path.label ? sPath + path.label + sSlash : "";
    });
    sPath = sPath ? sPath.slice(0, -2) : "";
    return (sPath) ? (<TooltipView placement={"bottom"} label={sPath}>
      <div className="path">{sPath}</div>
    </TooltipView>) : null;
  };

  /**
   * when focus on input its open the dialog
   */
  handleOnFocusListDisplayOperation = () => {
    let oState = this.state;
    let bIsDialogOpen = oState.store.getIsDialogOpen();
    if (CS.isEmpty(oState.sSearchText) || !bIsDialogOpen) {
      oState.store.handleInputFocusClickedForOpenDialog(oState.sSearchText);
    }
  };

  /**
   * handling of outside click of popperview
   * @param oEvent event for outside of popperview click
   * @returns {boolean}
   */
  handleClickAway = (oEvent) => {
    if (oEvent.target.offsetParent.className === 'searchContainerWrapper' || oEvent.target.offsetParent.className === 'customSearchContainerWrapper') {
      return false;
    }
    this.setState({
      sAutoHeight: constant.maxItemToShow * constant.maxHeightOfItem,
      sSearchText: ""
    });
    this.state.store.handleClickAwayClicked();
    this.props.setItemInFocus(-1);
  };

  /**
   * loadMore click handling for when bind data from api call
   */
  handleSearchLoadMoreClicked = () => {
    this.state.store.handleSearchLoadMoreClicked(this.state.sSearchText);
  };

  /**
   * handle popper view data and open dialog
   * @returns {*}
   */
  handlePopOverOpen = () => {
    let oProps = this.props;
    let oState = this.state;
    let aListOfItems = oState.sSearchText ? oState.store.getItems() : oState.aLists;
    let iItemIndex = 0;
    let oIndexMap = [];
    let aItemsDom = [];
    let oStyle = {
      height: oState.sAutoHeight + 'px',
    };
    let oPopStyle = oProps.oStyle;

    oProps.setIndexMap({});
    CS.forEach(aListOfItems, (oItem, iIndex) => {
      oIndexMap[iItemIndex] = iIndex;
      let oPathDOM = this.createPathDOM(oItem.path);
      let sClassName = CS.replace(oItem.id, ViewUtils.getSplitter(), "");
      let sIconClassName = "itemImage " + sClassName;
      let sContextMenuItemClass = "item ";
      if (oProps.itemInFocus === iItemIndex) {
        sContextMenuItemClass += "inFocus";
      }

      aItemsDom.push(
          <div className={sContextMenuItemClass}
               onClick={this.handleItemAction.bind(this, oItem.id, oItem.parentId, oItem.path)}
               key={oItem.id}
               ref={this.setRef.bind(this, 'contextMenuItem' + iItemIndex++)}>
            <div className={sIconClassName}></div>
            <div className="itemLabel">
              {CS.getLabelOrCode(oItem)}
            </div>
            {oPathDOM}
          </div>
      );
    });

    oProps.setIndexMap(oIndexMap);

    if (CS.isEmpty(aItemsDom)) {
      aItemsDom.push(<NothingFoundView message={getTranslation().CONTROLLER_NO_MATCH_FOUND} key={"no_match_found"}/>)
    }

    let aLoadMoreDOM = [];
    if (oProps.requestApiData && aListOfItems.length !== 0) {
      aLoadMoreDOM = <div className="itemLoadMore" onClick={this.handleSearchLoadMoreClicked}>{getTranslation().LOAD_MORE}</div>
    }

    let oInputDOM = (
        <ClickAwayListener onClickAway={this.handleClickAway}>
          <div className="searchItemsContainer" style={oStyle} ref={this.setRef.bind(this, 'scrollbar')}>
            <div className={"searchBarWrapper"} >
              {aItemsDom}
              {aLoadMoreDOM}
            </div>
          </div>
        </ClickAwayListener>
    );
    let oPopoverStyle = {
      width: '468px',
      borderRadius: '3px',
      position: 'absolute',
      top: '31px',
      background: '#fff',
      left: '0',
      right: '0',
      margin: '0 auto',
      boxShadow: '0px 5px 5px -3px rgba(0, 0, 0, 0.2), 0px 8px 10px 1px rgba(0, 0, 0, 0.14), 0px 3px 14px 2px rgba(0, 0, 0, 0.12)',
      maxHeight: '490px',
      zIndex: 99,
    };

    Object.assign(oPopoverStyle, oPopStyle);
    let bIsDialogOpen = oState.store.getIsDialogOpen();

    return (
        <CustomPopperView
            className="generic-view"
            style={oPopoverStyle}
            placement="bottom"
            open={bIsDialogOpen}
            disablePortal={true}
        >
          {oInputDOM}
        </CustomPopperView>
    );
  };

  handleCrossButtonClicked = () => {
    let oCrossIconDOM = CS.isEmpty(this.state.sSearchText) ? null : (
        <TooltipView label={getTranslation().CLEAR} key="clear">
          <div className="crossIcon" onClick={this.clearSearchInput}></div>
        </TooltipView>);
    return oCrossIconDOM;
  };

  render() {
    let bIsDialogOpen = this.state.store.getIsDialogOpen();
    let sClassNameForWrapper = this.props.themeLoaderConfigure ? "searchContainerWrapper" : "customSearchContainerWrapper";

    return (
        <div className={sClassNameForWrapper} onKeyDown={this.props.onKeyPressHandler} tabIndex={0}
             ref={this.setRef.bind(this, 'contextMenuView')}>
          <div className="searchInputWrapper">
            <div className="searchIcon"></div>
            <input className="searchInput" value={this.state.sSearchText} placeholder={getTranslation().SEARCH}
                   onChange={this.handleSearchOperation} onClick={this.handleOnFocusListDisplayOperation}
                   ref={this.setRef.bind(this, 'searchBox')}></input>
            {this.handleCrossButtonClicked()}
          </div>
          {bIsDialogOpen && this.handlePopOverOpen()}
        </div>
    );
  };
}

export const view = KeyboardNavigationForLists(CustomSearchBarPopperView);
export const propTypes = oPropTypes;
