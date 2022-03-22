import React, {Component} from 'react';

import ReactDOM from 'react-dom';

import CS from '../../libraries/cs';
import ViewUtils from "../../viewlibraries/utils/view-library-utils";

const KEY_ARROW_DOWN = 40;
const KEY_ARROW_UP = 38;
const KEY_SPACE = 32; //Selection/Deselect
const KEY_ENTER = 13; //Apply/Search
const SCROLL_CAL_HEIGHT = 10; //Apply for custom scrollbar view/scroll down & top
const SCROLL_CAL_HEIGHT_MOZ = 17; //Apply for custom scrollbar view/scroll down & top


export const oCustomEvents = {
  END_OF_LIST: "END_OF_LIST",
  SELECTION_OF_ITEM: "SELECTION_OF_ITEM",
  APPLY_SELECTION: "APPLY_SELECTION",
  FOCUS_OF_ITEM: "FOCUS_OF_ITEM"
};

export default function (View) {

  if (!View) {
    throw new Error("UNDEFINED_VIEW_PASSED");
  }

  return class KeyboardNavigationForLists extends Component {

    constructor (props) {
      super(props);
      let iItemInFocus = this.props.showSearch == false ? 0 : -1;

      this.state = {
        itemInFocus: iItemInFocus,
      };

      this.indexMap = {};
      this.eventBus = {};

      this.setRef = (sRef, element) => {
        this[sRef] = element;
      };
    }

    componentDidMount = () => {
      let {searchBox, contextMenuView} = this.getChildRefItems();
      let oItemToBeFocused = searchBox || contextMenuView;
      if(oItemToBeFocused) {
        if(this.props.bDisableAutoFocus){
          return;
        }
        oItemToBeFocused.focus();
      }
    };

    componentDidUpdate = () => {
      if (this.isLoadMoreClicked) {
        if (this.lengthOfList !== CS.keys(this.indexMap).length) {
          this.setItemInFocus(this.state.itemInFocus + 1, true);
        }
        this.isLoadMoreClicked = false;
      }
    };

    checkInView = (oElement) => {
      let oContainer = this.getChildRefItems().scrollbar;
      let contHeight = oContainer.clientHeight;
      let oElementBoundingClient = oElement.getBoundingClientRect();
      let oContainerBoundingClient = oContainer.getBoundingClientRect();
      let elemTop = oElementBoundingClient.top - oContainerBoundingClient.top;
      let elemBottom = elemTop + oElementBoundingClient.height;

      return (elemTop >= 0 && elemBottom <= contHeight);
    };

    scrollIntoView = (iItemIndex, isDownArrowPressed) => {
      let oElement = this.getChildRefItems(iItemIndex).contextMenuItem;
      if (oElement) {
        let oElementBoundingClient = oElement.getBoundingClientRect();
        let bIsVisible = this.checkInView(oElement);
        if (!bIsVisible) {
          let oScrollbar = this.getChildRefItems().scrollbar;
          let oScrollbarBoundingClient = oScrollbar.getBoundingClientRect();
          let iScrollBy = (oElementBoundingClient.bottom - oScrollbarBoundingClient.bottom);

          /**ScrollBy respective height to scroll DOM with margin**/
          iScrollBy += ViewUtils.isFirefox() ? SCROLL_CAL_HEIGHT_MOZ : SCROLL_CAL_HEIGHT;

          if (!isDownArrowPressed) {
            iScrollBy = oElementBoundingClient.top - oScrollbarBoundingClient.top;
          }

          oScrollbar.scrollBy({
            top: iScrollBy,
            left: 0,
            behavior: 'smooth'
          })
        }
      }

    };

    getChildRefItems = (iItemIndex) => {
      return {
        searchBox: this.viewWithArrowFunctionality.searchBox,
        contextMenuView: this.viewWithArrowFunctionality.contextMenuView,
        scrollbar: this.viewWithArrowFunctionality.scrollbar,
        contextMenuItem: this.viewWithArrowFunctionality['contextMenuItem' + iItemIndex]
      }
    };

    blurSearchBar = () => {
      let {searchBox} = this.getChildRefItems();
      if (searchBox) {
        searchBox.blur();
        return true;
      }
    };

    focusSearchBar = () => {
      let {searchBox} = this.getChildRefItems();
      if (searchBox) {
        searchBox.focus();
        return true;
      }
    };

    focusContextMenuView = () => {
      let {contextMenuView} = this.getChildRefItems();
      contextMenuView.focus();
      return true;
    };

    checkIfFocusIsAtLastItem = (iItemInFocus) => {
      let iLengthOfList = CS.keys(this.indexMap).length;
      return iItemInFocus === iLengthOfList - 1;
    };

    checkIfFocusIsAtFirstItem = (iItemInFocus) => {
      let {searchBox} = this.getChildRefItems();
      return iItemInFocus === -1 || (!searchBox && iItemInFocus === 0);
    };

    setItemInFocus = (iItemIndex, isDownArrowPressed) => {
      this.scrollIntoView(iItemIndex, isDownArrowPressed);
      this.setState({itemInFocus: iItemIndex})
    };

    handleKeyPressed = (oEvent) => {
      let iItemInFocus = this.state.itemInFocus;
      switch (oEvent.keyCode) {
        case KEY_ARROW_DOWN:
          if (this.checkIfFocusIsAtFirstItem(iItemInFocus)) {
            if(!this.lengthOfList) {
              return;
            }
            if (iItemInFocus === -1) {
              this.blurSearchBar();
              this.focusContextMenuView();
            }
          }
          else if (this.checkIfFocusIsAtLastItem(iItemInFocus)) {
            this.isLoadMoreClicked = true;
            this.dispatchEvent(oCustomEvents.END_OF_LIST);
            return;
          }
          this.dispatchEvent(oCustomEvents.FOCUS_OF_ITEM, iItemInFocus + 1);

          this.setItemInFocus(iItemInFocus + 1, true);

          break;

        case KEY_ARROW_UP:
          this.isLoadMoreClicked = false;
          if (this.checkIfFocusIsAtFirstItem(iItemInFocus)) {
            return;
          }
          else if (iItemInFocus === 0) {
            this.focusSearchBar(oEvent);
          }
          this.dispatchEvent(oCustomEvents.FOCUS_OF_ITEM, iItemInFocus - 1);

          this.setItemInFocus(iItemInFocus - 1);
          break;

        case KEY_SPACE:
          if (!oEvent.nativeEvent.dontRaise) {
            if (this.props.isMultiselect) {
              this.dispatchEvent(oCustomEvents.SELECTION_OF_ITEM, this.indexMap[iItemInFocus], oEvent);
            }
          }
          break;

        case KEY_ENTER:
          if (oEvent.nativeEvent.dontRaise) {
            return;
          }
          else if (!this.props.isMultiselect) {
            this.dispatchEvent(oCustomEvents.SELECTION_OF_ITEM, this.indexMap[iItemInFocus], oEvent);
          }
          else {
            if (oEvent.target.type === "button") {
              oEvent.target.click();
            } else {
              this.dispatchEvent(oCustomEvents.APPLY_SELECTION);
            }
          }
          break;
      }
    };

    setIndexMap = (oIndexMap) => {
      this.indexMap = oIndexMap;
      this.lengthOfList = CS.keys(this.indexMap).length;
    };

    registerCustomEvents = (sEvent, fCallback) => {
      if (oCustomEvents[sEvent]) {
        this.eventBus[sEvent] = fCallback;
      }
      else {
        console.error("UNKNOWN EVENT")
      }
    };

    dispatchEvent = (sEvent, ...restParameters) => {
      const fFunToCall = this.eventBus[sEvent];
      if (CS.isFunction(fFunToCall)) {
        fFunToCall.apply(this.viewWithArrowFunctionality, restParameters);
      }
    };

    render () {

      this.lengthOfList = CS.keys(this.indexMap).length;
      //lengthOfList is required for identifying first and last item of the list
      // indexMap - map for the index of item in the list (required for up down in case of search results)

      return (<View {...this.props}
                    onKeyPressHandler={this.handleKeyPressed}
                    registerCustomEvent={this.registerCustomEvents}
                    itemInFocus={this.state.itemInFocus}
                    setIndexMap={this.setIndexMap}
                    setItemInFocus={this.setItemInFocus}
                    ref={this.setRef.bind(this, 'viewWithArrowFunctionality')}/>);
    }
  }


};
