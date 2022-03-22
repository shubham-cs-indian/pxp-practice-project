import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import {SortableContainer, SortableElement, SortableHandle, arrayMove} from 'react-sortable-hoc';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ContextMenuViewModel from './model/context-menu-custom-view-model';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import KeyboardNavigationForLists, { oCustomEvents } from '../../commonmodule/HOC/keyboard-navigation-for-lists';

//TODO Quick fix: remove only when you discuss this - Shashank
import RequestMapping from '../../libraries/requestmappingparser/request-mapping-parser.js';

import { UploadRequestMapping as oUploadRequestMapping } from '../tack/view-library-request-mapping';
const DragHandle = SortableHandle(() => <span className="handle"></span>);

const oEvents = {};

const oPropTypes = {
  contextMenuViewModel: ReactPropTypes.oneOfType([
    ReactPropTypes.arrayOf(ReactPropTypes.instanceOf(ContextMenuViewModel)),
    ReactPropTypes.arrayOf(ReactPropTypes.object),
  ]).isRequired,
  onClickHandler: ReactPropTypes.func,
  onApplyHandler: ReactPropTypes.func,
  searchHandler: ReactPropTypes.func,
  searchText: ReactPropTypes.string,
  loadMoreHandler: ReactPropTypes.func,
  isMultiselect: ReactPropTypes.bool,
  style: ReactPropTypes.object,
  showSelectedItems: ReactPropTypes.bool,
  selectedItems: ReactPropTypes.array,
  showColor: ReactPropTypes.bool,
  showCustomIcon: ReactPropTypes.bool,
  isMovable: ReactPropTypes.bool,
  showCreateButton: ReactPropTypes.bool,
  showSearch: ReactPropTypes.bool,
  onCreateHandler: ReactPropTypes.func,
  menuListHeight: ReactPropTypes.string,
  onItemsChecked: ReactPropTypes.func
};
/**
 * @class ContextMenuItemView
 * @memberOf Views
 * @property {custom} contextMenuViewModel - Instance of ContextMenuViewModel.
 * @property {func} [onClickHandler] - Executes when item clicked.
 * @property {func} [onApplyHandler] - Executes when apply button is clicked(apply button are used to select multiple items from drop down).
 * @property {func} [searchHandler] - Executes when text is searched.
 * @property {string} [searchText] - Contains text which have to be searched.
 * @property {func} [loadMoreHandler] - Executes after load more option is clicked.
 * @property {bool} [isMultiselect] - To select multiple items from dropdowm list flag is set to true and vice versa.
 * @property {object} [style] - CSS style for drop down view.
 * @property {bool} [showSelectedItems] -  To show selected items in dropdown list.
 * @property {array} [selectedItems] - Contains selected items.
 * @property {bool} [showColor] - To change the style of item, if true(ex.display = 'block'; backgroundColor = sColor).
 * @property {bool} [showCustomIcon] - To show custom icons for items in dropdown list.
 * @property {bool} [isMovable] - Used for making draggable dropdown list.
 * @property {bool} [showCreateButton] - To show create button on dropdown list.
 * @property {bool} [showSearch] - To show search bar on dropdown list.
 * @property {func} [onCreateHandler] - Executes when create button is clicked.
 * @property {string} [menuListHeight] - Height for menu list.
 * @property {func} [onItemsChecked] - Executes when item checkbox is clicked.
 */

const SortableItem = SortableElement(({value}) => {
  return (
      <div className='contextMenuShuffleItemContainer'>
        {value}
        <DragHandle />
      </div>

  );
});

const SortableList = SortableContainer(({items}) => {
  return (
      <div className="contextMenuList" style={{backgroundColor: '#fafafa'}}>
        {items.map((value, index) => (
            <SortableItem key={'item-' + index} index={index} value={value}/>
        ))}
      </div>
  );

});

// @KeyboardNavigationForLists
// @CS.SafeComponent
class ContextMenuItemView extends React.Component {

  constructor (props) {
    super(props);

    var aCheckedItems = (this.props.showSelectedItems) ? (CS.cloneDeep(this.props.selectedItems) || []) : [];
    //TODO: Review -> Performance Check
    this.state = {
      list: CS.cloneDeep(this.props.contextMenuViewModel),
      searchValue: this.props.searchText || '',
      checkedItems: aCheckedItems,
      showSelectedItems: this.props.showSelectedItems
    }

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };

    this.props.registerCustomEvent(oCustomEvents.APPLY_SELECTION, this.handleApplyButtonClicked);

  }

  static getDerivedStateFromProps (oNextProps, oState) {
    let aCheckedItems = (oState.showSelectedItems) ? (CS.cloneDeep(oNextProps.selectedItems) || []) : [];
    if (oState.isLoadMoreClicked) {
      aCheckedItems = this.state.checkedItems;
    }

    return {
      list: oNextProps.contextMenuViewModel,
      searchValue: oNextProps.searchText || '',
      checkedItems: aCheckedItems,
      isLoadMoreClicked: false,
      showSelectedItems: oNextProps.selectedItems
    }
  }

  /*componentWillReceiveProps = (oNextProps) => {
    var aCheckedItems = (this.props.showSelectedItems) ? (CS.cloneDeep(oNextProps.selectedItems) || []) : [];

    if (this.state.isLoadMoreClicked) {
      aCheckedItems = this.state.checkedItems;
    }

    this.setState({
      list: oNextProps.contextMenuViewModel,
      searchValue: oNextProps.searchText || '',
      checkedItems: aCheckedItems,
      isLoadMoreClicked: false
    });
  };*/


 /* componentWillMount = () => {
    this.setState({
      list: CS.cloneDeep(this.props.contextMenuViewModel),
      searchValue: this.props.searchText || ''
    })
  }*/

  componentDidMount = () => {
    document.body.addEventListener("click", this.documentListenerCallback);
    this.props.registerCustomEvent(oCustomEvents.SELECTION_OF_ITEM, this.handleContextMenuItemClicked);
  }

  unMountMe = () => {
    if (document.getElementById('contextContainer') instanceof Element) {
      ReactDOM.unmountComponentAtNode(document.getElementById('contextContainer'));
    }
    document.body.removeEventListener("click", this.documentListenerCallback);
  }

  documentListenerCallback = (oEvent) => {
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
          var aModelList = this.state.list;
          var aSelectedItems = [];
          CS.forEach(aModelList, function (oModel) {
            if (CS.includes(aCheckedItems, oModel.id)) {
              aSelectedItems.push(oModel.id);
            }
          });
          aCheckedItems = aSelectedItems;
        }

        this.props.onApplyHandler(aCheckedItems);
        // this.setState({checkedItems: []});
      }
    }
  }

  handleCreateButtonClicked = () => {
    var sSearchValue = this.state.searchValue;
    this.props.onCreateHandler(sSearchValue);
  }

  isAllChecked = () => {
    var aCheckedItems = this.state.checkedItems;
    var aFilteredList = this.state.list;
    var bAllChecked = !!aFilteredList.length;
    var rPattern = this.getSearchTextRegExPattern();

    CS.forEach(aFilteredList, function (oModel) {
      var bIsInSearchText = rPattern ? rPattern.test(oModel.label) : true;
      if (!CS.includes(aCheckedItems, oModel.id) && bIsInSearchText) {
        bAllChecked = false;
        return false;
      }
    });

    return bAllChecked;
  }

  handleSearchClearClicked = () => {

    this.setState({
      list: this.state.list,
      searchValue: ""
    });

    if (CS.isFunction(this.props.searchHandler)) {
      this.props.searchHandler("");
    }
  }

  handleCheckAllClicked = (bCurrentlyAllChecked) => {
    var aCheckedItems = this.state.checkedItems;
    var aList = this.state.list;
    var rPattern = this.getSearchTextRegExPattern();

    if (bCurrentlyAllChecked) { //if all items are checked, then un-check them based on search text

      var aItemsToUncheck = [];

      CS.forEach(aList, function (oModel) {
        var bIsInSearchText = rPattern ? rPattern.test(oModel.label) : true;
        if (bIsInSearchText) {
          aItemsToUncheck.push(oModel.id);
        }
      });

      aCheckedItems = CS.difference(aCheckedItems, aItemsToUncheck);

    } else { //Else check those items which are not checked but include the search text

      CS.forEach(aList, function (oModel) {
        var bIsInSearchText = rPattern ? rPattern.test(oModel.label) : true;
        if (!CS.includes(aCheckedItems, oModel.id) && bIsInSearchText) {
          aCheckedItems.push(oModel.id);
        }
      });

    }

    this.setState({
      checkedItems: aCheckedItems
    });
    if (CS.isFunction(this.props.onItemsChecked)) {
      this.props.onItemsChecked(aCheckedItems, true);
    }
  }

  handleContextMenuItemContainerClicked = (iItemIndex, oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      this.handleContextMenuItemClicked(iItemIndex, oEvent);
    }
  }

  handleContextMenuItemClicked = (iItemIndex, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let oModel = this.state.list[iItemIndex];
    oModel.properties = oModel.properties || {};

    if(oModel.properties.disabled) {
      return;
    }
    if (this.props.isMultiselect) {
      var aCheckedItems = this.state.checkedItems;
      var sCheckedId = oModel.id;
      if (CS.includes(aCheckedItems, sCheckedId)) {
        CS.remove(aCheckedItems, function (sId) {
          return sId === sCheckedId;
        });
      } else {
        aCheckedItems.push(sCheckedId);
      }
      this.setState({checkedItems: aCheckedItems});
      if (CS.isFunction(this.props.onItemsChecked)) {
        this.props.onItemsChecked([sCheckedId]);
      }
      return;
    }
    this.props.onClickHandler && this.props.onClickHandler.apply(this, [oModel]);
    this.unMountMe();
  }

  getIconUrl =(sKey)=> {
    if (CS.startsWith(sKey, "data:")) {
      return sKey;
    }
    return RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {type: "Icons", id: sKey}) + "/";
  };

  getImageView = (sKey) => {
    if (sKey) {
      var sThumbnailImageSrc = this.getIconUrl(sKey);
      return (<ImageSimpleView classLabel="contextMenuIcon" imageSrc={sThumbnailImageSrc}/>);
    }
    return null;
  }

  handleLoadMore = () => {
    this.setState({
      isLoadMoreClicked: true
    });
    if (CS.isFunction(this.props.loadMoreHandler)) {
      this.props.loadMoreHandler();
    }
  }

/*
  moveRow = (oListItem, iDirection, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let aModelList = this.state.list;
    let iIndex = CS.findIndex(aModelList, function (oModel) {
      return oModel.id === oListItem.id;
    });
    let oItem = aModelList[iIndex];
    aModelList[iIndex] = aModelList[iIndex + iDirection];
    aModelList[iIndex + iDirection] = oItem;

    this.setState({
      list: aModelList
    });
  }
*/

  onSortEnd = ({oldIndex, newIndex}) => {
    let aModelList = this.state.list;

    this.setState({
      list: arrayMove(aModelList, oldIndex, newIndex),
    });
  };

  getSearchTextRegExPattern = () => {
    var sSearchText = this.state.searchValue;
    sSearchText = (this.props.searchHandler) ? "" : CS.escapeRegexCharacters(sSearchText);
    var rPattern = sSearchText ? new RegExp(sSearchText, "i") : null;
    return rPattern;
  }

  getHtmlContextMenuItemList = (aModelList) => {
    var that = this;
    var bShowCheckbox = this.props.isMultiselect;
    var aCheckedItems = this.state.checkedItems;
    var bIsMovable = this.props.isMovable;
    var rPattern = this.getSearchTextRegExPattern();
    var aMenuItemList = [];

    let iItemIndex = 0;
    this.props.setIndexMap({});
    let oIndexMap = [];
    CS.forEach(aModelList, function (oModel, iIndex) {

      if (!CS.isFunction(that.props.searchHandler) && rPattern) {
        if (!rPattern.test(oModel.label)) {
          return;
        }
      }
      if (React.isValidElement(oModel.properties.view)) {
        /** if oModel.properties.view is dom element to be render as menu item */
        let oRenderView = oModel.properties.view;     /** object which contains the logic of rendering */
        let bIsDisabled = oModel.properties.disabled; /**to disable the menu item form selection */
        let sClassName = "contextMenuItem contextMenuItemIsActive";
        sClassName = !oModel.properties.isSubHeader ? sClassName : sClassName + " isSubHeader";
        oIndexMap[iItemIndex] = iIndex;

        aMenuItemList.push(<div key={oModel.id}
                                className={sClassName}
                                ref = {that.setRef.bind(that, 'contextMenuItem'+ iItemIndex++)}
                                style={bIsDisabled ? { pointerEvents : "none" } : null}
                                onClick={that.handleContextMenuItemContainerClicked.bind(that, iIndex)}>{oRenderView}</div>);

      } else {
        /** if model is object with normal properties, to be render as menu item */

        var sContextMenuItemClass = oModel.isActive ? 'contextMenuItem contextMenuItemIsActive' : 'contextMenuItem contextMenuItemInActive';
        sContextMenuItemClass = !oModel.properties.isSubHeader ? sContextMenuItemClass : sContextMenuItemClass + " isSubHeader";
        var sContextMenuIcon = "contextMenuIcon ";
        var sColor = oModel.properties["color"];
        var oIconStyle = {
          display: 'none'
        };

        var oIconColorDom = null;
        if (oModel.iconKey) {
          oIconColorDom = that.getImageView(oModel.iconKey)
        }
        else if (that.props.showColor && !CS.isEmpty(sColor)) {
          oIconStyle.display = 'block';
          oIconStyle.backgroundColor = sColor;
          oIconColorDom = (<div className={sContextMenuIcon} style={oIconStyle}></div>);
        }
        else if (that.props.showCustomIcon) {
          var sCustomIconClassName = oModel.properties["customIconClassName"];
          oIconColorDom = (<div className={"contextMenuCustomIcon " + sCustomIconClassName}></div>);
        }

        if (oModel.id == "context_menu_separator") {
          return (<div className="contextMenuSeparator"></div>)
        }

        var oCheckbox = null;
        if (bShowCheckbox) {
          var sCheckboxClass = "contextMenuCheckbox ";
          sCheckboxClass += CS.includes(aCheckedItems, oModel.id) ? " checked " : "";
          oCheckbox =
              <div className={sCheckboxClass} onClick={that.handleContextMenuItemClicked.bind(that, iIndex)}></div>
        }
        let oModelCode;
        if (oModel.properties.code) {
          oModelCode = <div className='contextMenuItemCode'>{oModel.properties.code}</div>
        }
        let sTitle = oModel.properties.code ? CS.getLabelOrCode(oModel) + ' (' + oModel.properties.code + ')' : CS.getLabelOrCode(oModel);
        let fContextMenuItemContainerClicked = oModel.properties.disabled ? null : that.handleContextMenuItemContainerClicked.bind(that, iIndex);
        let oSelectedIconDOM = !oModel.properties.isSelected ? null : <div className="selectedItem"></div>
        if(!oModel.properties.isSubHeader) {
          oIndexMap[iItemIndex] = iIndex;
          iItemIndex++;
        }
        let sLabelClass = oModelCode ? "contextMenuData contextMenuLabel" : "contextMenuData";
        aMenuItemList.push(
            <div className={sContextMenuItemClass}
                 ref = {that.setRef.bind(that, 'contextMenuItem'+ iItemIndex)}
                 key={oModel.id}
                 title={sTitle}
                 onClick={fContextMenuItemContainerClicked}>
              <div className={sLabelClass}>
                {oCheckbox}
                {oIconColorDom}
                {CS.getLabelOrCode(oModel)}
              </div>
              {oModelCode}
              {oSelectedIconDOM}
            </div>
        );
      }
    });

    this.props.setIndexMap(oIndexMap);


    if (!CS.isEmpty(aModelList) && CS.isFunction(this.props.loadMoreHandler)) {
      aMenuItemList.push(
          <div onClick={this.handleLoadMore} className="menuItemLoadMore">{getTranslation().LOAD_MORE}</div>
      );
    }

    if (bIsMovable && (!CS.isFunction(that.props.searchHandler) && !rPattern) && aMenuItemList.length) {
      return [<SortableList items={aMenuItemList}
                            onSortEnd={this.onSortEnd}
                            useDragHandle={true}
                            lockToContainerEdges={true}
                            axis={"y"}
                            lockAxis={"y"}/>]
    }
    else {
      return aMenuItemList;
    }
  }

  handleSearchTextChanged = (oEvent) => {
    var sSearchedValue = oEvent.target.value;

    this.setState({
      searchValue: sSearchedValue
    });
  }

  searchTextOnKeyDown = (oEvent) => {
    if ((oEvent.keyCode == 13) && (CS.isFunction(this.props.searchHandler))) { //13 -> Enter key
      this.props.searchHandler(oEvent.target.value);
    }
  }

  getApplyButtonView = () => {
    var bApplyButtonVisible = this.props.showSelectedItems || !!(this.state.checkedItems.length);

    if (bApplyButtonVisible) {

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
             autoFocus={true}/>
      <div className={sContextMenuSearchClearClassName} onClick={this.handleSearchClearClicked}></div>
    </div>)
  }

  render () {

    var bIsMultiselect = this.props.isMultiselect;
    var aModelList = this.state.list;

    var bAllChecked = this.isAllChecked();
    var sContainerClassName = "contextMenuView ";
    sContainerClassName += bIsMultiselect ? "" : "contextMenuSingleSelect ";
    var sCheckboxAllClassName = "contextMenuCheckboxAll ";
    sCheckboxAllClassName += bAllChecked ? "checked " : "";


    var aContextMenuItemList = this.getHtmlContextMenuItemList(aModelList);
    if (!aContextMenuItemList.length) {
      aContextMenuItemList = <div className="nothingFoundMessage">{getTranslation().NOTHING_FOUND}</div>;
    }
    let oContextMenuListStyle = {};
    if (this.props.menuListHeight) {
      oContextMenuListStyle = {height: this.props.menuListHeight}
    }

    var oApplyButtonView = this.getApplyButtonView();
    var oCreateButtonView = this.getCreateButtonView();
    /*if(!CS.isEmpty(oCreateButtonView)){
     oApplyButtonView = null;
     }*/

    return (
        <div className={sContainerClassName} style={this.props.style} onKeyDown={this.props.onKeyPressHandler} tabIndex={0} ref={this.setRef.bind(this, 'contextMenuView')}>
          {this.props.showSearch == false ? null :
              (<div className="contextMenuHeader">
                <div className={sCheckboxAllClassName}
                     onClick={this.handleCheckAllClicked.bind(this, bAllChecked)}></div>
                {this.getSearchView()}
              </div>)}
          <div className="contextMenuList" style={oContextMenuListStyle} ref={this.setRef.bind(this, 'scrollbar')}>
            {aContextMenuItemList}
          </div>
          {CS.isEmpty(oApplyButtonView) && CS.isEmpty(oCreateButtonView) ? null :
            <div className="contextMenuItemButtonContainer">
              {oApplyButtonView}
              {oCreateButtonView}
            </div>}
        </div>
    );
  }
}

ContextMenuItemView.propTypes = oPropTypes;

export const view = KeyboardNavigationForLists(ContextMenuItemView);
export const events = oEvents;
