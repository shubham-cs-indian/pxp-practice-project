import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import KeyboardNavigationForLists, { oCustomEvents } from '../../commonmodule/HOC/keyboard-navigation-for-lists';
import ClassNameFromBaseTypeDictionary from '../../commonmodule/tack/class-name-base-types-dictionary';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";


const oEvents = {};

const oPropTypes = {
  contextMenuItems: ReactPropTypes.array,
  searchText: ReactPropTypes.string,
  isMultiselect: ReactPropTypes.bool,
  showSelectedItems: ReactPropTypes.bool,
  selectedItems: ReactPropTypes.array,
  showColor: ReactPropTypes.bool,
  showCustomIcon: ReactPropTypes.bool,
  isMovable: ReactPropTypes.bool,
  showCreateButton: ReactPropTypes.bool,
  showClearButton: ReactPropTypes.bool,
  hideApplyButton: ReactPropTypes.bool,

  onClickHandler: ReactPropTypes.func,
  onApplyHandler: ReactPropTypes.func,
  searchHandler: ReactPropTypes.func,
  loadMoreHandler: ReactPropTypes.func,
  onCreateHandler: ReactPropTypes.func,
  onClearHandler: ReactPropTypes.func,
  getItemLabel: ReactPropTypes.func,
  addReferencedData: ReactPropTypes.func,
  showSearch: ReactPropTypes.bool,
  style: ReactPropTypes.object,
  menuListHeight: ReactPropTypes.string,

  onKeyPressHandler: ReactPropTypes.func,
  registerCustomEvent: ReactPropTypes.func,
  itemInFocus: ReactPropTypes.number,
  setIndexMap: ReactPropTypes.func,
  updatePosition: ReactPropTypes.func,
  showDefaultIcon: ReactPropTypes.bool
};
/**
 * @class LazyContextMenuItemView
 * @memberOf Views
 * @property {array} [contextMenuItems] - Contains item list.
 * @property {string} [searchText] - Contains text which you have searched.
 * @property {bool} [isMultiselect] - To select multiple items from dropdowm list flag is set to true and vice versa.
 * @property {bool} [showSelectedItems] - To show selected items in dropdown list.
 * @property {array} [selectedItems] - Contains selected items.
 * @property {bool} [showColor] - To change the style of item, if true(ex.display = 'block'; backgroundColor = sColor).
 * @property {bool} [showCustomIcon] - To show custom icons for items in dropdown list.
 * showing custom icon for nodes).
 * @property {bool} [isMovable] - Used for making draggable dropdown list.
 * @property {bool} [showCreateButton] - To show create button on dropdown list.
 * @property {func} [onClickHandler] - Execute when item is clicked.
 * @property {func} [onApplyHandler] - Execute when apply button is clicked(apply button are used to select multiple items from drop down).
 * @property {func} [searchHandler] - Executes when text is searched.
 * @property {func} [loadMoreHandler] - Execute after load more option is clicked.
 * @property {func} [onCreateHandler] - Executes when create button is clicked.
 * @property {func} [getItemLabel] - Used for getting label of the item.
 * @property {func} [addReferencedData] - Used for adding selected items into referenced data.
 * @property {boolean} [showSearch] - To show search bar on dropdown list.
 * @property {object} [style] CSS style for LazyContextMenuItemView.
 * @property {string} [menuListHeight] - Height for menu list.
 */

// @KeyboardNavigationForLists
// @CS.SafeComponent
class LazyContextMenuItemView extends React.Component {

  constructor (props) {
    super(props);

    //TODO: Review -> Performance Check
    var aCheckedItems = (this.props.showSelectedItems) ? (CS.cloneDeep(this.props.selectedItems) || []) : [];
    let iItemInFocus = this.props.showSearch == false ? 0 : -1;

    this.state = {
      list: CS.cloneDeep(this.props.contextMenuItems),
      searchValue: this.props.searchText || '',
      checkedItems: aCheckedItems,
      isDirty: false,
      showSelectedItems: this.props.showSelectedItems,
      itemInFocus: iItemInFocus,
    };
    this.isLoadMoreClicked = false;

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };

  }

  static getDerivedStateFromProps (oNextProps, oState) {
    let aCheckedItems = (oState.showSelectedItems) ? (CS.cloneDeep(oNextProps.selectedItems) || []) : [];

    if (oState.isDirty) {
      aCheckedItems = oState.checkedItems;
    }

    return {
      list: oNextProps.contextMenuItems,
      checkedItems: aCheckedItems,
      isDirty: true,
      showSelectedItems: oNextProps.showSelectedItems
    }
  }

 /* componentWillReceiveProps = (oNextProps) => {
    var aCheckedItems = (this.props.showSelectedItems) ? (CS.cloneDeep(oNextProps.selectedItems) || []) : [];

    if (this.state.isLoadMoreClicked) {
      aCheckedItems = this.state.checkedItems;
    }

    this.setState({
      list: oNextProps.contextMenuItems,
      searchValue: oNextProps.searchText || '',
      checkedItems: aCheckedItems,
      isLoadMoreClicked: false
    });
  }*/


  /*componentWillMount = () => {
    this.setState({
      list: CS.cloneDeep(this.props.contextMenuItems),
      searchValue: this.props.searchText || ''
    })
  }*/

  componentDidMount = () => {
    document.body.addEventListener("click", this.documentListenerCallback);
    this.props.registerCustomEvent(oCustomEvents.END_OF_LIST, this.handleLoadMore);
    this.props.registerCustomEvent(oCustomEvents.SELECTION_OF_ITEM, this.handleContextMenuItemClicked);
    this.props.registerCustomEvent(oCustomEvents.APPLY_SELECTION, this.handleApplyButtonClicked);
  };

  unMountMe = () => {
    ReactDOM.unmountComponentAtNode(document.getElementById('contextContainer'));
    document.body.removeEventListener("click", this.documentListenerCallback);
  }

  documentListenerCallback = (oEvent) => {//TODO (Snehal): check if its still needed
    var aClassesToIgnore = ['contextMenuCheckbox', 'contextMenuCheckboxAll', 'contextMenuSearchClear', 'contextMenuView', 'contextMenuItem', 'contextMenuData', "contextMenuSearchInput", "contextMenuSearchIcon", "contextMenuSearch"];
    if (!CS.isEmpty(CS.intersection(aClassesToIgnore, oEvent.target.classList))) {
      return;
    }
    this.unMountMe();
  }

  handleApplyButtonClicked = () => {
    if (this.props.isMultiselect) {
      if (this.props.onApplyHandler) {
        var aCheckedItems = this.state.checkedItems;
        var bIsMovable = this.props.isMovable;
        if (bIsMovable) {
          var aItemList = this.state.list;
          var aSelectedItems = [];
          CS.forEach(aItemList, function (oItem) {
            if (CS.includes(aCheckedItems, oItem.id)) {
              aSelectedItems.push(oItem.id);
            }
          });
          aCheckedItems = aSelectedItems;
        }

        this.props.onApplyHandler(aCheckedItems);
        this.setState({isDirty: false});
      }
    }
  }

  handleCreateButtonClicked = () => {
    var sSearchValue = this.state.searchValue;
    this.props.onCreateHandler(sSearchValue);
  }

  handleClearButtonClicked = () => {
    if (CS.isFunction(this.props.onClearHandler)) {
      this.props.onClearHandler();
    }
  };

  /*isAllChecked = () => {
    var aCheckedItems = this.state.checkedItems;
    var aFilteredList = this.state.list;
    var bAllChecked = !!aFilteredList.length;
    var rPattern = this.getSearchTextRegExPattern();

    CS.forEach(aFilteredList, (oItem) => {
      var bIsInSearchText = rPattern ? rPattern.test(this.getItemLabel(oItem)) : true;
      if (!CS.includes(aCheckedItems, oItem.id) && bIsInSearchText) {
        bAllChecked = false;
        return false;
      }
    });

    return bAllChecked;
  }*/

  handleSearchClearClicked = () => {

    this.setState({
      list: this.state.list,
      searchValue: ""
    });

    if (CS.isFunction(this.props.searchHandler)) {
      this.props.searchHandler("");
    }
  }

  handleCheckAllClicked = (bIsAnyItemSelected) => {
    var aCheckedItems = this.state.checkedItems;
    var aList = this.state.list;
    var rPattern = this.getSearchTextRegExPattern();

    if (bIsAnyItemSelected) { //if all items are checked, then un-check them based on search text

      var aItemsToUncheck = [];

      CS.forEach(aList, (oItem) => {
        var bIsInSearchText = rPattern ? rPattern.test(this.getItemLabel(oItem)) : true;
        if (bIsInSearchText) {
          aItemsToUncheck.push(oItem.id);
        }
      });

      aCheckedItems = CS.difference(aCheckedItems, aItemsToUncheck);

    } else { //Else check those items which are not checked but include the search text

      CS.forEach(aList, (oItem) => {
        var bIsInSearchText = rPattern ? rPattern.test(this.getItemLabel(oItem)) : true;
        if (!CS.includes(aCheckedItems, oItem.id) && bIsInSearchText) {
          aCheckedItems.push(oItem.id);
          // onClickHandler is called on item selection, when apply button is hidden.
          this.props.hideApplyButton && this.props.onClickHandler && this.props.onClickHandler(oItem);
        }
      });

    }
    this.props.addReferencedData(aCheckedItems);
    this.setState({
      checkedItems: aCheckedItems
    });
    this.props.updatePosition(); // To update dropdown position on item checked
  }

  handleContextMenuItemContainerClicked = (iItemIndex, oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      this.handleContextMenuItemClicked(iItemIndex, oEvent);
    }
  }

  handleContextMenuItemClicked = (iItemIndex, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let oItem = this.state.list[iItemIndex];
    let bIsItemDisabled = oItem.isDisabled || false;
    if (bIsItemDisabled) {
      return;
    }
    if (this.props.isMultiselect) {
      var aCheckedItems = this.state.checkedItems;
      var sCheckedId = oItem.id;
      if (CS.includes(aCheckedItems, sCheckedId)) {
        CS.remove(aCheckedItems, function (sId) {
          return sId === sCheckedId;
        });
      } else {
        aCheckedItems.push(sCheckedId);
      }
      this.props.addReferencedData(aCheckedItems);
      this.setState({checkedItems: aCheckedItems});
      this.props.setItemInFocus(iItemIndex);
      //onClickHandler is called on item selection when apply button is hidden.
      this.props.hideApplyButton && this.props.onClickHandler && this.props.onClickHandler(oItem);
      this.props.updatePosition(); // To update dropdown position on item checked
      return;
    }
    this.props.onClickHandler && this.props.onClickHandler.apply(this, [oItem]);
    this.props.setItemInFocus(iItemIndex);
    this.unMountMe();
  };

  getImageView = (sKey) => {
    if (sKey) {
      var sThumbnailImageSrc = ViewUtils.getIconUrl(sKey);
      return (<ImageSimpleView classLabel="contextMenuIcon" imageSrc={sThumbnailImageSrc}/>);
    }
    return null;
  }

  getCustomImageView = (sKey) => {
    var sThumbnailImageSrc = ViewUtils.getIconUrl(sKey);
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sThumbnailImageSrc}/>
        </div>
    );
  };


  handleLoadMore = () => {
    /*this.setState({
      isLoadMoreClicked: true
    });*/
    if (CS.isFunction(this.props.loadMoreHandler)) {
      this.props.loadMoreHandler();
    }
  }

  moveRow = (oListItem, iDirection, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let aItemList = this.state.list;
    let iIndex = CS.findIndex(aItemList, function (oItem) {
      return oItem.id === oListItem.id;
    });
    let oItem = aItemList[iIndex];
    aItemList[iIndex] = aItemList[iIndex + iDirection];
    aItemList[iIndex + iDirection] = oItem;

    this.setState({
      list: aItemList
    });
  }

  getSearchTextRegExPattern = () => {
    var sSearchText = this.state.searchValue;
    sSearchText = (this.props.searchHandler) ? "" : ViewUtils.escapeRegexCharacters(sSearchText);
    var rPattern = sSearchText ? new RegExp(sSearchText, "i") : null;
    return rPattern;
  }

  getItemLabel = (oItem) => {
    if(CS.isFunction(this.props.getItemLabel)) {
      return this.props.getItemLabel(oItem);
    }
    return CS.getLabelOrCode(oItem);
  };

  getIconColorView = (oItem) => {
    let sContextMenuIcon = "contextMenuIcon ";
    let sColor = oItem.color;
    let oIconStyle = {
      display: 'none'
    };
    let oIconColorDom = null;
    if(this.props.showDefaultIcon){
      oIconColorDom = this.getCustomImageView(oItem.iconKey);
    } else if (oItem.iconKey) {
      oIconColorDom = this.getImageView(oItem.iconKey)
    } else if (this.props.showColor && !CS.isEmpty(sColor)) {
      oIconStyle.display = 'block';
      oIconStyle.backgroundColor = sColor;
      oIconColorDom = (<div className={sContextMenuIcon} style={oIconStyle}></div>);
    } else if (this.props.showCustomIcon) {
      let sCustomIconClassName = oItem.customIconClassName || ClassNameFromBaseTypeDictionary[oItem.type];
      oIconColorDom = (<div className={"contextMenuCustomIcon " + sCustomIconClassName}></div>);
    }
    return oIconColorDom;
  }

  getHtmlContextMenuItemList = (aItemList) => {
    var that = this;
    var bShowCheckbox = this.props.isMultiselect;
    var aCheckedItems = this.state.checkedItems;
    var bIsMovable = this.props.isMovable;
    var rPattern = this.getSearchTextRegExPattern();
    var aMenuItemList = [];
    let bAnyItemSelected = false;
    let bIsAllSelected = !!aItemList.length;

    let iItemIndex = 0;
    this.props.setIndexMap({});
    let oIndexMap = [];
    CS.forEach(aItemList, function (oItem, iIndex) {

      var sItemLabel = that.getItemLabel(oItem);
      if (!CS.isFunction(that.props.searchHandler) && rPattern) {
        if (!rPattern.test(sItemLabel)) {
          return;
        }
      }

      var sContextMenuItemClass = oItem.isActive ? 'contextMenuItem ' : 'contextMenuItem contextMenuItemInActive ';
      oItem.isDisabled && (sContextMenuItemClass += "disabled ");
      if(that.props.itemInFocus == iItemIndex) {
        sContextMenuItemClass += "inFocus";

      }

      let oIconColorDom = that.getIconColorView(oItem);

      if (oItem.id == "context_menu_separator") {
        return (<div className="contextMenuSeparator"></div>)
      }

      var oCheckbox = null;
      if (bShowCheckbox) {
        var sCheckboxClass = "contextMenuCheckbox ";
        let bIsItemChecked = CS.includes(aCheckedItems, oItem.id);
        bIsAllSelected = bIsAllSelected && bIsItemChecked;
        bAnyItemSelected = bAnyItemSelected || bIsItemChecked;
        sCheckboxClass += bIsItemChecked ? " checked " : "";
        oCheckbox = <div className={sCheckboxClass}
                         onClick={!oItem.isDisabled ? that.handleContextMenuItemClicked.bind(that, iIndex) : null}></div>
      }

      var iPosition = CS.findIndex(aItemList, function (oListItem) {
        return oListItem.id === oItem.id;
      });

      var aShufflingIcons = [];
      if (bIsMovable && (!CS.isFunction(that.props.searchHandler) && !rPattern)) {
        let oMoveUpIcon = <div className="contextMenuItemShuffleKeys Up" title={getTranslation().MOVE_UP} key="up"
                               onClick={that.moveRow.bind(that, oItem, -1)}></div>;
        let oMoveDownIcon = <div className="contextMenuItemShuffleKeys Down" title={getTranslation().MOVE_DOWN} key="down"
                                 onClick={that.moveRow.bind(that, oItem, 1)}></div>;

        if (aItemList.length > 1) {
          if (iPosition === aItemList.length - 1) {
            aShufflingIcons.push(oMoveUpIcon);
          } else if (iPosition === 0) {
            aShufflingIcons.push(oMoveDownIcon);
          } else {
            aShufflingIcons.push(oMoveUpIcon);
            aShufflingIcons.push(oMoveDownIcon);
          }
        }
      }
      let oItemCode;
      if (oItem.code) {
        oItemCode = <div className='contextMenuItemCode'>{oItem.code}</div>
      }
      let oItemCount = oItem.count !== null && (typeof(oItem.count) !== 'undefined') ? (
          <div className='contextMenuItemCount'>{"(" + oItem.count + ")"}</div>) : null;
      let sTitle = oItem.code ? sItemLabel + ' (' + oItem.code + ')' : sItemLabel;

      oIndexMap[iItemIndex] = iIndex;
      let sLabelClass = oItemCode ? "contextMenuData contextMenuLabel" : "contextMenuData";
      aMenuItemList.push(<div className={sContextMenuItemClass}
                              ref = {that.setRef.bind(that, 'contextMenuItem'+ iItemIndex++)}
                              key={oItem.id} title={sTitle}
                              onClick={!oItem.isDisabled ? that.handleContextMenuItemContainerClicked.bind(that, iIndex) : null}>
        <div className={sLabelClass}>
          {oCheckbox}
          {oIconColorDom}
          {sItemLabel}
        </div>
        {oItemCode}
        {oItemCount}
        {aShufflingIcons}
      </div>);
    });

    this.props.setIndexMap(oIndexMap);

    if (!CS.isEmpty(aItemList) && CS.isFunction(this.props.loadMoreHandler)) {
      aMenuItemList.push(
          <div onClick={this.handleLoadMore} className="menuItemLoadMore" key="loadMore">{getTranslation().LOAD_MORE}</div>
      );
    }

    let iCheckAllIndicator = bIsAllSelected ? 2 : (bAnyItemSelected ? 1 : 0);

    return {
      menuItemLis: aMenuItemList,
      checkAllIndicator: iCheckAllIndicator
    };
  }

  handleSearchTextChanged = (oEvent) => {
    var sSearchedValue = oEvent.target.value;

    this.setState({
      searchValue: sSearchedValue
    });
  };

  handleSearchBoxClicked =()=> {
    this.props.setItemInFocus(-1);
  };

  searchTextOnKeyDown = (oEvent) => {
    if ((oEvent.keyCode == 13) && (CS.isFunction(this.props.searchHandler))) { //13 -> Enter key
      oEvent.nativeEvent.dontRaise = true;
      this.props.searchHandler(oEvent.target.value);
    }
  }

  getApplyButtonView = () => {
    //TODO: Need to have clean logic to show apply button when something got changed in multiselect (In Dirty State
    // Only)
    let oProps = this.props;
    let aSelectedItems = oProps.selectedItems ? oProps.selectedItems : [];
    let bApplyButtonVisible = oProps.showCreateButton ? oProps.isMultiselect && !CS.isEmpty(this.state.checkedItems) : oProps.isMultiselect;

    if (bApplyButtonVisible && !oProps.hideApplyButton && !CS.isEqual(this.state.checkedItems, aSelectedItems)) {

      // var oButtonStyle = {
      //   height: "17px",
      //   lineHeight: "15px",
      //   margin: "6px"
      // };

      var oApplyButtonLabelStyles = {
        fontSize: 11,
        fontWeight: 300,
        lineHeight: "17px",
        padding: "0px"
      };

      return <CustomMaterialButtonView label={getTranslation().APPLY}
                                       isRaisedButton={true}
                                       isDisabled={false}
                                       // style={oButtonStyle}
                                       labelStyle={oApplyButtonLabelStyles}
                                       onButtonClick={this.handleApplyButtonClicked}/>
    }

    return null;
  }

  /** Added clear button for clearing the selected items **/
  getClearButtonView = () => {
    let bClearButtonVisible = (this.props.showClearButton && this.props.isMultiselect);

    if (bClearButtonVisible) {

      let oButtonStyle = {
        height: "28px",
        lineHeight: "28px",
        margin: "0 5px",
        padding: '0 10px',
        minWidth: '64px',
        minHeight: '28px',
        boxShadow: 'none',
        border: "#3248A7 1px solid",
        color: "#3248A7"
      };

      return <CustomMaterialButtonView label={getTranslation().CLEAR}
                                       isRaisedButton={false}
                                       isDisabled={false}
                                       style={oButtonStyle}
                                       onButtonClick={this.handleClearButtonClicked}/>
    }
    return null;
  }

  getCreateButtonView = () => {
    var sSearchText = this.state.searchValue;
    var bCreateButtonVisible = this.props.showCreateButton && !CS.isEmpty(sSearchText);

    if (bCreateButtonVisible) {

      // var oButtonStyle = {
      //   height: "17px",
      //   lineHeight: "15px",
      //   margin: "6px"
      // };

      var oApplyButtonLabelStyles = {
        fontSize: 11,
        fontWeight: 300,
        lineHeight: "17px",
        padding: "0px",
        marginLeft: "5px",
        marginRight: "5px"
      };

      return (
          <CustomMaterialButtonView
              label={getTranslation().CREATE + " \'" + sSearchText + "\'"}
              isRaisedButton={true}
              isDisabled={false}
              // style={oButtonStyle}
              labelStyle={oApplyButtonLabelStyles}
              onButtonClick={this.handleCreateButtonClicked}/>
      );
    }

    return null;
  }

  getSearchView = () => {
    var sSearchedText = this.state.searchValue;
    var sContextMenuSearchClearClassName = "contextMenuSearchClear ";
    sContextMenuSearchClearClassName += sSearchedText ? "" : "noShow ";

    return (<div className="contextMenuSearch">
      <div className="contextMenuSearchIcon"></div>
      <input className="contextMenuSearchInput"
             value={this.state.searchValue}
             onKeyDown={this.searchTextOnKeyDown}
             onChange={this.handleSearchTextChanged}
             placeholder={getTranslation().SEARCH}
             ref = {this.setRef.bind(this, 'searchBox')}
             onClick={this.handleSearchBoxClicked}
             />
      <div className={sContextMenuSearchClearClassName} onClick={this.handleSearchClearClicked}></div>
    </div>)
  }

  render () {

    var bIsMultiselect = this.props.isMultiselect;
    var aItemList = this.state.list;

    var sContainerClassName = "contextMenuView lazyContextMenuItemView ";
    sContainerClassName += bIsMultiselect ? "" : "contextMenuSingleSelect ";
    let sCheckboxAllClassName = "contextMenuCheckboxAll ";

    let oApplyButtonView = this.getApplyButtonView();
    let oClearButtonView = this.getClearButtonView();

    let {menuItemLis:aContextMenuItemList, checkAllIndicator: iCheckAllIndicator} = this.getHtmlContextMenuItemList(aItemList);
    if (!aContextMenuItemList.length) {
      aContextMenuItemList = <div className="nothingFoundMessage">{getTranslation().NOTHING_FOUND}</div>;
    }

    if (iCheckAllIndicator === 2) {
      sCheckboxAllClassName += "checked ";
    } else if (iCheckAllIndicator === 1) {
      sCheckboxAllClassName += "halfChecked ";
    }

    let bAutoHeight = true;
    let sAutoHeightMax = this.props.menuListHeight || '250px';
    let oContextMenuListStyle = {};
    if (this.props.menuListHeight) {
      bAutoHeight = false;
      oContextMenuListStyle = {height: this.props.menuListHeight}
    }

    var oCreateButtonView = this.getCreateButtonView();
    /*if(!CS.isEmpty(oCreateButtonView)){
     oApplyButtonView = null;
     }*/

    let bIsAnyItemSelected = iCheckAllIndicator > 0;
    let oCheckAllButton = CS.isEmpty(this.state.list) ? null : <div className={sCheckboxAllClassName} onClick={this.handleCheckAllClicked.bind(this, bIsAnyItemSelected)}></div>;


    return (
        <div className={sContainerClassName} style={this.props.style} onKeyDown={this.props.onKeyPressHandler} tabIndex={0} ref={this.setRef.bind(this, 'contextMenuView')}>
          {this.props.showSearch == false ? null :
            (<div className="contextMenuHeader">
              {oCheckAllButton}
              {this.getSearchView()}
            </div>)}
          <div className="contextMenuList" style={oContextMenuListStyle}>
            <div className={"contextMenuListWrapper"} style={{maxHeight: sAutoHeightMax}} ref = {this.setRef.bind(this, 'scrollbar')}>
            {aContextMenuItemList}
            </div>
          </div>
          {CS.isEmpty(oApplyButtonView) && CS.isEmpty(oCreateButtonView) ? null :
            <div className="contextMenuItemButtonContainer">
              {oClearButtonView}
              {oApplyButtonView}
              {oCreateButtonView}
            </div>}
        </div>
    );
  }
}

LazyContextMenuItemView.propTypes = oPropTypes;

export const view = KeyboardNavigationForLists(LazyContextMenuItemView);
export const events = oEvents;
