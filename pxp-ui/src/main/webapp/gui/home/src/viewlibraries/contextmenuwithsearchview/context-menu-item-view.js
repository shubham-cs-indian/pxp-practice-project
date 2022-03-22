import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import TooltipView from '../../viewlibraries/tooltipview/tooltip-view';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import { view as CustomTextFieldView } from '../customtextfieldview/custom-text-field-view';
import {view as DragDropContextView} from "../draggableDroppableView/drag-drop-context-view";
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";
const {default: KeyboardNavigationForLists, oCustomEvents } = require('../../commonmodule/HOC/keyboard-navigation-for-lists');

const oEvents = {

};

const oPropTypes = {
  contextMenuViewModel: ReactPropTypes.array.isRequired,
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
  onItemsChecked: ReactPropTypes.func,
  onDeleteHandler: ReactPropTypes.func,
  showDeleteIcon: ReactPropTypes.bool,
  showDefaultIcon: ReactPropTypes.bool,
  onKeyPressHandler: ReactPropTypes.func,
  registerCustomEvent: ReactPropTypes.func,
  itemInFocus: ReactPropTypes.number,
  setIndexMap: ReactPropTypes.func
};

// @KeyboardNavigationForLists
// @CS.SafeComponent
class ContextMenuItemView extends React.Component {


  constructor(props) {
    super(props);

    //TODO: Review -> Performance Check
    var aCheckedItems = (this.props.showSelectedItems) ? (CS.cloneDeep(this.props.selectedItems) || []) : [];
    let iItemInFocus = this.props.showSearch == false  ? 0 : -1;
    this.state = {
      list: CS.cloneDeep(this.props.contextMenuViewModel),
      checkedItems: aCheckedItems,
      isDirty: false,
      searchValue: this.props.searchText || '',
      showSelectedItems: this.props.showSelectedItems,
      isDragged: false,
      itemInFocus: iItemInFocus
    };

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };

    this.setRefForSearchBox =( sRef, element) => {
      if(element) {
        this[sRef] = element.textField;
      }
    };

  }

  static getDerivedStateFromProps (oNextProps, oState) {
    let aCheckedItems = (oState.showSelectedItems) ? (CS.cloneDeep(oNextProps.selectedItems) || []) : [];
    let aList = oNextProps.contextMenuViewModel;
    if (oState.isDirty) {
      aCheckedItems = oState.checkedItems;
      aList = !CS.isEqual(oState.list, oNextProps.contextMenuViewModel) ? oNextProps.contextMenuViewModel : oState.list;
    }
    if(oState.isDragged){
      return {
        list: oState.list,
        checkedItems: aCheckedItems,
        isDragged: false
      }
    }
    return {
      list: aList,
      checkedItems: aCheckedItems,
      isDirty: true
    }
  }

  /*componentWillReceiveProps = (oNextProps) => {
    var aCheckedItems = (this.props.showSelectedItems) ? (CS.cloneDeep(oNextProps.selectedItems) || []) : [];

    if(this.state.isLoadMoreClicked) {
      aCheckedItems = this.state.checkedItems;
    }

    this.setState({
      list: oNextProps.contextMenuViewModel,
      //searchValue: oNextProps.searchText || '',
      checkedItems: aCheckedItems,
      isLoadMoreClicked: false
    });
  }*/


 /* componentWillMount = () => {
    this.setState({
      list: CS.cloneDeep(this.props.contextMenuViewModel),
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

  documentListenerCallback= (oEvent) => {//TODO (Snehal): check if its still needed
    var aClassesToIgnore = ['contextMenuCheckbox', 'contextMenuCheckboxAll', 'contextMenuSearchClear', 'contextMenuView', 'contextMenuItem', 'contextMenuData', "contextMenuSearchInput", "contextMenuSearchIcon", "contextMenuSearch"];
    if(!CS.isEmpty(CS.intersection(aClassesToIgnore, oEvent.target.classList))) {
      return;
    }
    this.unMountMe();
  }

  handleApplyButtonClicked = () => {
    if (this.props.isMultiselect) {
      if (this.props.onApplyHandler) {
        var aCheckedItems = this.state.checkedItems;
        var bIsMovable = this.props.isMovable;
        if(bIsMovable) {
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

  isAllChecked = () => {
    var aCheckedItems = this.state.checkedItems;
    var aFilteredList = this.state.list;
    var bAllChecked = !!aFilteredList.length;
    var rPattern = this.getSearchTextRegExPattern();

    let aSearchResultItems = [];
    CS.forEach(aFilteredList, function (oModel) {
      var bIsInSearchText = rPattern ? rPattern.test(oModel.label) : true;
      bIsInSearchText && (aSearchResultItems.push(oModel));
      if(!CS.includes(aCheckedItems, oModel.id) && bIsInSearchText){
        bAllChecked = false;
        return false;
      }
    });

    return bAllChecked && CS.isNotEmpty(aSearchResultItems);
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

    } else {
      if (CS.isEmpty(this.state.checkedItems)) {
        CS.forEach(aList, function (oModel) {
          var bIsInSearchText = rPattern ? rPattern.test(oModel.label) : true;
          if (!CS.includes(aCheckedItems, oModel.id) && bIsInSearchText) {
            aCheckedItems.push(oModel.id);
          }
        });
      } else {
        aCheckedItems = [];
      }

    }

    this.setState({
      checkedItems: aCheckedItems
    });
    if(CS.isFunction(this.props.onItemsChecked)){
      this.props.onItemsChecked([...aCheckedItems], true);
    }
  }

  handleContextMenuItemContainerClicked = (iItemInFocus, oEvent) => {
    if(!oEvent.nativeEvent.dontRaise) {
      this.handleContextMenuItemClicked(iItemInFocus, oEvent);
    }
  }

  handleContextMenuItemClicked = (iItemIndex, oEvent) => {
    if(oEvent.ctrlKey) {
      return;
    }
    oEvent.nativeEvent.dontRaise = true;
    let oModel = this.state.list[iItemIndex];
    if (this.props.isMultiselect) {
      var aCheckedItems = this.state.checkedItems;
      var sCheckedId = oModel.id;
      if (CS.includes(aCheckedItems, sCheckedId)) {
        CS.remove(aCheckedItems, function (sId) {return sId === sCheckedId;});
      } else {
        aCheckedItems.push(sCheckedId);
      }
      this.setState({checkedItems: aCheckedItems});
      if(CS.isFunction(this.props.onItemsChecked)){
        this.props.onItemsChecked([sCheckedId]);
      }
      this.props.setItemInFocus(iItemIndex);
      return;
    }
    this.props.onClickHandler && this.props.onClickHandler.apply(this, [oModel]);
    this.props.setItemInFocus(iItemIndex);
    this.unMountMe();
  }

  getImageView = (sKey) => {
    if (sKey) {
      var sThumbnailImageSrc = ViewUtils.getIconUrl(sKey);
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

  moveRow = (oListItem, iDirection , oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let aModelList = this.state.list;
    let iIndex = CS.findIndex(aModelList, function (oModel) {
      return oModel.id === oListItem.id;
    });
    let oItem = aModelList[iIndex];
    aModelList[iIndex] = aModelList[iIndex + iDirection];
    aModelList[iIndex + iDirection] = oItem;

    this.setState({
      list :aModelList
    });
  }

  onDragEnd = (oSource, oDestination, aDraggableIds) => {
    let aModelList = this.state.list;
    let iDestinationIndex = oDestination.index;
    if(oSource.index < iDestinationIndex) {
      iDestinationIndex = oDestination.index - (aDraggableIds.length - 1);
    }
    let aShuffledProperties = CS.remove(aModelList, function (oModel) {
      return CS.includes(aDraggableIds, oModel.id);
    });
    aModelList.splice(iDestinationIndex, 0, ...aShuffledProperties);
    this.setState({
      list: aModelList,
      isDragged: true
    });
  };

  getSearchTextRegExPattern  = () =>{
    var sSearchText = this.state.searchValue;
    sSearchText = (this.props.searchHandler) ? "" : ViewUtils.escapeRegexCharacters(sSearchText);
    var rPattern = sSearchText ? new RegExp(sSearchText, "i") : null;
    return rPattern;
  }

  getCustomImageView = (sKey) => {
    var sThumbnailImageSrc = ViewUtils.getIconUrl(sKey);
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sThumbnailImageSrc}/>
        </div>
    );
  };

  getHtmlContextMenuItemList = (aModelList) => {
    var that = this;
    var bShowCheckbox = this.props.isMultiselect;
    var aCheckedItems = this.state.checkedItems;
    var bIsMovable = this.props.isMovable;
    let aExcludedItems = this.props.excludedItems;
    var rPattern = this.getSearchTextRegExPattern();
    var aMenuItemList = [];
    let sAutoHeightMax = this.props.menuListHeight || '250px';
    let iItemIndex = 0;
    this.props.setIndexMap({});
    let oIndexMap = [];

    let aMovableList = [];
    CS.forEach(aModelList, function (oModel, iIndex) {
      if (!CS.isFunction(that.props.searchHandler) && rPattern) {
        if (!rPattern.test(oModel.label)) {
          return;
        }
      }
      if (CS.includes(aExcludedItems, oModel.id)) {
        return;
      }
      oModel.properties = oModel.properties || {};
      var sContextMenuItemClass = oModel.isActive ? 'contextMenuItem contextMenuItemIsActive ' : 'contextMenuItem contextMenuItemInActive ';
      var sContextMenuIcon = "contextMenuIcon ";
      var sColor = oModel.properties["color"];
      var oIconStyle = {
        display: 'none'
      };

      var oIconColorDom = null;
      if(that.props.showDefaultIcon){
        oIconColorDom = that.getCustomImageView(oModel.iconKey)
      }
      else if (oModel.iconKey) {
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

      let oDeleteView = that.props.showDeleteIcon ? (
          <div className='deleteIcon' onClick={that.props.onDeleteHandler.bind(that, oModel)}></div>) : null;
      let sTitle = oModel.properties.code ? (oModel.label || ' [' + oModel.properties.code + ']') : CS.getLabelOrCode(oModel);
      let fContextMenuItemContainerClicked = oModel.properties.disabled ? null : that.handleContextMenuItemContainerClicked.bind(that, iIndex);

      oIndexMap[iItemIndex] = iIndex;
      let sTooltipLabel = oModel.properties.taxonomyHierarchy ? oModel.properties.taxonomyHierarchy : sTitle;
      sTooltipLabel = oModel.properties.code ? sTooltipLabel + ' (' + oModel.properties.code + ')' : sTooltipLabel;
      let sLabelClass = oModelCode ? "contextMenuData contextMenuLabel" : "contextMenuData";
      let oMenuItem = (
          <TooltipView placement="bottom" label={sTooltipLabel}>
            <div className={sContextMenuItemClass}
                 ref={that.setRef.bind(that, 'contextMenuItem' + iItemIndex++)}
                 key={oModel.id}>
              <div className="contextMenuContent"
                   onClick={fContextMenuItemContainerClicked}>
                <div className={sLabelClass}>
                  {oCheckbox}
                  {oIconColorDom}
                  {sTitle}
                </div>
                {oModelCode}
              </div>
              {oDeleteView}
            </div>
          </TooltipView>
      );
      aMenuItemList.push(oMenuItem);
      bIsMovable && aMovableList.push({
        id: oModel.id,
        label: oMenuItem,
        className: "contextMenuItemWrapper"
      });
    });

    this.props.setIndexMap(oIndexMap);
    if (!CS.isEmpty(aModelList) && CS.isFunction(this.props.loadMoreHandler)) {
      aMenuItemList.push(
          <div onClick={this.handleLoadMore} className="menuItemLoadMore" key="loadMore">{getTranslation().LOAD_MORE}</div>
      );
    }

    if (CS.isEmpty(aMenuItemList)) {
      return <div className="nothingFoundMessage">{getTranslation().NOTHING_FOUND}</div>;
    }
    else if (bIsMovable && !rPattern) {
      let oListData = {
        droppableId: "contextMenuList",
        items: aMovableList
      };
      return <DragDropContextView listData={[oListData]}
                                  context={"contextMenuList"}
                                  onDragEnd={this.onDragEnd}
                                  showDraggableIcon={true}
                                  enableMultiDrag={true}/>;
    }
    else {
      let oStyle = {
        maxHeight: sAutoHeightMax
      };
      return (
          <div className="contextMenuListWrapper" ref={this.setRef.bind(this, 'scrollbar')} style={oStyle}>
              {aMenuItemList}
          </div>
      )
    }
  };

  handleSearchTextChanged = (sValue) => {
    this.setState({
      searchValue: sValue
    });
  }

  searchTextOnKeyDown = (oEvent) => {
    if (oEvent.keyCode == 13) { //13 -> Enter key
      oEvent.nativeEvent.dontRaise = true;
      CS.isFunction(this.props.searchHandler) && this.props.searchHandler(oEvent.target.value);
    }
  }

  getApplyButtonView = () => {
    let oProps = this.props;
    let aSelectedItems = oProps.selectedItems ? oProps.selectedItems : [];
    let bIsMultiSelect = oProps.isMultiselect;
    let bApplyButtonVisible = bIsMultiSelect && !CS.isEqual(this.state.checkedItems, aSelectedItems);

    if(bApplyButtonVisible){

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
        color: '#3248A7'
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

    if(bCreateButtonVisible){

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

  handleSearchBoxClicked =()=> {
    this.props.setItemInFocus(-1)
  };

  getSearchView= () => {
    var sSearchedText = this.state.searchValue;
    var sContextMenuSearchClearClassName = "contextMenuSearchClear ";
    sContextMenuSearchClearClassName += sSearchedText ? "" : "noShow ";

    /* <input className="contextMenuSearchInput"
    value={this.state.searchValue}
    onKeyDown={this.searchTextOnKeyDown}
    onChange={this.handleSearchTextChanged}
    placeholder={getTranslation().SEARCH}
    autoFocus={true}/>*/
    return (
        <div className="contextMenuSearch">
          <div className="contextMenuSearchIcon"></div>
          <CustomTextFieldView
              value={this.state.searchValue}
              onChange={this.handleSearchTextChanged}
              hintText={getTranslation().SEARCH}
              hideTooltip={true}
              showLabel={false}
              showDescription={false}
              onKeyDown={this.searchTextOnKeyDown}
              ref = {this.setRefForSearchBox.bind(this, 'searchBox')}
              onFocus={this.handleSearchBoxClicked}
          />
          <div className={sContextMenuSearchClearClassName} onClick={this.handleSearchClearClicked}></div>
        </div>
    )
  }

  render() {

    var bIsMultiselect = this.props.isMultiselect;
    var aModelList = this.state.list;

    var bAllChecked = this.isAllChecked();
    var sContainerClassName = "contextMenuView ";
    sContainerClassName += bIsMultiselect ? "" : "contextMenuSingleSelect ";
    var sCheckboxAllClassName = "contextMenuCheckboxAll ";
    if(bAllChecked) {
      sCheckboxAllClassName += "checked ";
    } else if(this.state.checkedItems && this.state.checkedItems.length) {
      sCheckboxAllClassName += "halfChecked ";
    }

    var aContextMenuItemList = this.getHtmlContextMenuItemList(aModelList);

    let oContextMenuListStyle = {};
    if (this.props.menuListHeight) {
      oContextMenuListStyle = {height: this.props.menuListHeight}
    }

    var oApplyButtonView = this.getApplyButtonView();
    var oCreateButtonView = this.getCreateButtonView();
    let oClearButtonView = this.getClearButtonView();
    /*if(!CS.isEmpty(oCreateButtonView)){
      oApplyButtonView = null;
    }*/

    return (
        <div className={sContainerClassName} style={this.props.style} onKeyDown={this.props.onKeyPressHandler} tabIndex={0} ref={this.setRef.bind(this, 'contextMenuView')}>
          {this.props.showSearch == false ? null :
           (<div className="contextMenuHeader">
             <div className={sCheckboxAllClassName} onClick={this.handleCheckAllClicked.bind(this, bAllChecked)}></div>
             {this.getSearchView()}
           </div>)}
          <div className="contextMenuList" style={oContextMenuListStyle}>
              {aContextMenuItemList}
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

ContextMenuItemView.propTypes = oPropTypes;

export const view = KeyboardNavigationForLists(ContextMenuItemView);
export const events = oEvents;
