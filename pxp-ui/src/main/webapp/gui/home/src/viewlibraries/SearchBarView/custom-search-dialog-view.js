import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import TooltipView from './../../viewlibraries/tooltipview/tooltip-view';
import {view as CustomPopOverView} from '../customPopoverView/custom-popover-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import ViewUtils from '../utils/view-library-utils';
import { view as NothingFoundView } from './../../viewlibraries/nothingfoundview/nothing-found-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import KeyboardNavigationForLists, {oCustomEvents} from "../../commonmodule/HOC/keyboard-navigation-for-lists";

const oEvents = {
  HANDLE_SEARCH_ITEM_CLICKED : "handle_search_item_clicked",
  HANDLE_SEARCH_DIALOG_CLOSED_CLICKED: "handle_search_dialog_closed_clicked"
};

const oPropTypes = {
  items:ReactPropTypes.array,
  isDialogOpen:ReactPropTypes.bool,
  onKeyPressHandler: ReactPropTypes.func,
  registerCustomEvent: ReactPropTypes.func,
  itemInFocus: ReactPropTypes.number,
  setIndexMap: ReactPropTypes.func,
};
/**
 * @class CustomSearchDialogView
 * @memberOf Views
 * @property {array} [items]
 * @property {bool} [isDialogOpen]
 */

// @KeyboardNavigationForLists
// @CS.SafeComponent
class CustomSearchDialogView extends React.Component {
  constructor (props){
    super(props);

    this.state = {
      listOfItems : this.props.items,
      searchText: "",
    }

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };

    this.setRefForScrollbar =( sRef, element) => {
      let oElement = ReactDOM.findDOMNode(element);
      if(element) {
        this[sRef] = oElement.firstChild;
      }
    };
  }

  componentDidMount() {
    this.props.registerCustomEvent(oCustomEvents.SELECTION_OF_ITEM, this.handleEnterKeyClicked);

  }

  handleEnterKeyClicked = (iIndex) => {
    let aListOfItems = this.state.listOfItems;
    let oItem = aListOfItems[iIndex];
    this.handleSearchClick(oItem.id,oItem.parentId,oItem.path)
  };

  handleSearchOperation = (e) => {
    // console.log(e.target.value);
    let aSearchList = [];
    let sSearchText = e.target.value.toLocaleLowerCase();
    CS.forEach(this.props.items, function (item) {
      if(item.label.toLocaleLowerCase().indexOf(sSearchText) != -1){
        aSearchList.push(item);
      }
    })
    // console.log(aSearchList);
    if (!CS.isEmpty(sSearchText)) {
      this.setState({
        searchText: sSearchText,
        listOfItems: aSearchList,
      })
    } else {
      this.setState({
        searchText : "",
        listOfItems: this.props.items,
      })
    }
    this.props.setItemInFocus(-1);
  };

  handleSearchClick = (sItemId,sParentId,aPath) => {
    EventBus.dispatch(oEvents.HANDLE_SEARCH_ITEM_CLICKED,sItemId,sParentId,aPath);
  };

  hideSearchDialog = () => {
    EventBus.dispatch(oEvents.HANDLE_SEARCH_DIALOG_CLOSED_CLICKED);
  };

  clearSearchInput = () => {
    this.setState({
      searchText : "",
      listOfItems: this.props.items,
    })
  }

  createPathDOM = (aPath) => {
    let sPath = "";
    let sSlash = "/"
    CS.forEach(aPath, function (path) {
      sPath = sPath + path.label + sSlash;
    })
    sPath = sPath.slice(0,-1);
    return (<TooltipView placement={"bottom"} label={sPath}>
        <div className="path">{sPath}</div>
      </TooltipView>);
  };


  render () {

    let oContentStyle = {
      minWidth: '900px',
      height: "50%",
    };

    let oBodyStyle = {
      padding: '0px',
      overflow: "hidden",
      minHeight:"400px"
    };

    let aListOfItems = this.state.listOfItems;
    let aItemsDom = [];
    let _this = this;
    let iItemIndex = 0;
    this.props.setIndexMap({});
    let oIndexMap = [];
    CS.forEach(aListOfItems,function (oItem, iIndex) {
      oIndexMap[iItemIndex] = iIndex;
      let oPathDOM = _this.createPathDOM(oItem.path);
      let sClassName = CS.replace(oItem.id, ViewUtils.getSplitter(), "");
      let sIconClassName = "itemImage " + sClassName;
      let sContextMenuItemClass = "item ";
      if(_this.props.itemInFocus == iItemIndex) {
        sContextMenuItemClass += "inFocus";
      }
      aItemsDom.push(
          <div className={sContextMenuItemClass} onClick={_this.handleSearchClick.bind(this,oItem.id,oItem.parentId,oItem.path)}  key={oItem.id} ref = {_this.setRef.bind(_this, 'contextMenuItem'+ iItemIndex++)}>
            <div className={sIconClassName}></div>
            <div className="itemLabel">
              {CS.getLabelOrCode(oItem)}
            </div>
            {oPathDOM}
          </div>

      );
    });
    this.props.setIndexMap(oIndexMap);

    if (CS.isEmpty(aItemsDom)) {
      aItemsDom.push(<NothingFoundView message={getTranslation().CONTROLLER_NO_MATCH_FOUND} key={"no_match_found"}/>)
    }

    let oCrossIconDOM =  CS.isEmpty(this.state.searchText)? null : (<TooltipView label={getTranslation().CLEAR} key="clear"><div className="crossIcon" onClick={this.clearSearchInput}></div></TooltipView>);

    let oInputDOM = (
        <div className="searchContainerSection  View" onKeyDown={this.props.onKeyPressHandler} tabIndex={0} ref={this.setRef.bind(this, 'contextMenuView')}>
          <div className="searchInputField">
            <div className="searchInputFieldBar">
              <div className="searchIcon"></div>
              <input className="searchInput" value={this.state.searchText} placeholder={getTranslation().SEARCH}
                     onChange={this.handleSearchOperation} autoFocus ref={this.setRef.bind(this, 'searchBox')}></input>
              {oCrossIconDOM}
            </div>
          </div>
          <div className="searchItemsContainer" >
            <div className={"searchBarWrapper"} ref = {this.setRefForScrollbar.bind(this, 'scrollbar')}>
            {aItemsDom}
            </div>
          </div>
        </div>);
    let oPopoverStyle = {width: '450px',
      maxWidth: '1800px',
      maxHeight: '490px',
      marginTop: '-5px',
      borderRadius: '3px'
    };
    return (
        <CustomPopOverView
            className="popover-root"
            // anchorEl={this.state.moreView}
            style={oPopoverStyle}
            anchorOrigin={{horizontal: 'center', vertical: 'top'}}
            transformOrigin={{horizontal: 'center', vertical: 'top'}}
            open={this.props.isDialogOpen}
            bodyStyle={oBodyStyle}
            contentStyle={oContentStyle}
            onClose={this.hideSearchDialog}
            bodyClassName="searchBodySection"
            contentClassName="searchContainerSection">
          {oInputDOM}
        </CustomPopOverView>

    );
  }

}

CustomSearchDialogView.propTypes = oPropTypes;

export const view = KeyboardNavigationForLists(CustomSearchDialogView);
export const events = oEvents;
export const propTypes = oPropTypes;
