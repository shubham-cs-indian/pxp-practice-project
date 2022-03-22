import CS from '../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import DropDownListView from '../dropdownlistview/drop-down-list-view';
import DropDownListViewModel from '../dropdownlistview/model/drop-down-list-model';
import TooltipView from './../tooltipview/tooltip-view';
import ViewUtils from '../utils/view-library-utils';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import ViewLibraryConstants from '../../commonmodule/tack/mss-view-constants';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
const sDefaultValue = ViewLibraryConstants.MSS_NOTHING_FOUND_ID;

const oEvents = {
  MULTI_SEARCH_DROP_DOWN_LIST_NODE_CLICKED: 'multi_search_drop_down_list_node_clicked',
  MULTI_SEARCH_DROP_DOWN_LIST_NODE_BLURRED: 'multi_search_drop_down_list_node_blurred',
  MULTI_SEARCH_BAR_CROSS_ICON_CLICKED: 'multi_search_bar_cross_icon_clicked'
};

const oPropTypes = {
  checkbox: ReactPropTypes.bool,
  disabled: ReactPropTypes.bool,
  label: ReactPropTypes.string,
  items: ReactPropTypes.array,
  selectedItems: ReactPropTypes.array,
  singleSelect: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  disableCross: ReactPropTypes.bool,
  hideTooltip: ReactPropTypes.bool,
  onSelect: ReactPropTypes.func,
  onDeselect: ReactPropTypes.func,
  onChange: ReactPropTypes.func
};
/**
 * @class MultiSelectSearchView - Used to generate a drop down list.
 * @memberOf Views
 * @property {bool} [checkbox] - To show checkbox.
 * @property {bool} [disabled] - Used to display or hide MultiSelectSearchView.
 * @property {string} [label] -
 * @property {array} [items] - Contains all items list(it may have selected or deselected).
 * @property {array} [selectedItems] - Contains all selected items.
 * @property {bool} [singleSelect] - Indicate drop down list is single select or multiselect(true means single select).
 * @property {string} [context] -
 * @property {bool} [disableCross] - To show/hide cross icon on the selected items.
 * @property {bool} [hideTooltip] - To hide the tooltip of the item(s).
 * @property {func} [onSelect] - Executes when the item is clicked.
 * @property {func} [onDeselect] - Executes when the cross icon is clicked.
 * @property {func} [onChange] - Executes when Select or deselect item.
 */

// @CS.SafeComponent
class MultiSelectSearchView extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      filteredItems: this.props.items,
      filterText: "",
      checkedItems: [],
      active: false
    }

    this.multiSearchInputBox = React.createRef();
    this.searchBarContainer = React.createRef();
    this.moreCounter = React.createRef();
  }

  resetState =()=>{
    document.removeEventListener('click', this.handleInputBoxBlurred);
    this.setState({
      filteredItems: this.props.items,
      filterText: "",
      checkedItems: [],
      active: false
    })
  }

  changeStateByProperty =(sKey, sValue)=>{
    var oCurrentState = {};
    oCurrentState[sKey] = sValue;

    this.setState(oCurrentState);
  }

  callOnChangeHandler =(value, bDeselect)=> {
    var aValuesToSend;
    var aSelectedItems = this.props.selectedItems;
    if (this.props.singleSelect) {
      aValuesToSend = bDeselect ? [] : [value];
    } else if (this.props.checkbox) {
      aValuesToSend = bDeselect ? (CS.WITHOUT(aSelectedItems, value)) : (CS.concat(aSelectedItems, value));
    } else {
      aValuesToSend = bDeselect ? (CS.WITHOUT(aSelectedItems, value)) : (CS.concat(aSelectedItems, [value]));
    }
    this.props.onChange(aValuesToSend);
  }

  handleHideDropDown =(oEvent)=> {
    var aValidClasses = ['dropDownListView', 'dropDownNodeContainer'];
    var oTarget = oEvent.target || {};
    var oParent = oTarget.parentNode || {};
    var bIsNotListNode = CS.isEmpty(CS.intersection(aValidClasses, oTarget.classList)) &&
        CS.isEmpty(CS.intersection(aValidClasses, oParent.classList));
    if(bIsNotListNode) {
      this.resetState();
    }
  }

  filterItemsByText =(sText)=> {
    var aFilteredList = [];
    var aItemList = this.props.items;
    var aSelectedItems = this.props.selectedItems;

    CS.forEach(aItemList, function(oItem){
      if(aSelectedItems.indexOf(oItem.id) == -1 && oItem.label.toLowerCase().indexOf(sText.toLowerCase()) != -1){
        aFilteredList.push({id: oItem.id, label: CS.getLabelOrCode(oItem)});
      }
    });

    this.changeStateByProperty('filteredItems', aFilteredList);
  }

  getDropDownListModel =()=>{

    var aItems = this.state.filteredItems;
    var aSelectedItems = this.props.selectedItems;
    var aModel = [];

    CS.forEach(aItems, function(oItem){

      if(aSelectedItems.indexOf(oItem.id) == -1){
        aModel.push(new DropDownListViewModel(oItem.id, CS.getLabelOrCode(oItem)))
      }
    });

    if(!aModel.length) {
      aModel.push(new DropDownListViewModel(sDefaultValue, getTranslation().NOTHING_FOUND));
    }

    return aModel;
  }

  getDropDownListView =()=> {
    var _this = this;
    var aListModel = this.getDropDownListModel();
    var aCheckedItems = this.state.checkedItems || [];
    var bUseCheckbox = this.props.checkbox && !this.props.singleSelect;
    return (<DropDownListView
        ref={function (i) {
                _this._dropdown = i
              }}
        checkbox={bUseCheckbox}
        checkedItems={aCheckedItems}
        dropDownListModel={aListModel}
        handleOnClick={this.handleListNodeClicked}
        handleHideDropDown={this.handleHideDropDown}
        maxHeight="200"/>);
  }

  handleInputValueChanged =(oEvent)=> {
    var sInputValue = oEvent.target.value;

    this.filterItemsByText(sInputValue);
  }

  handleKeyDown =(oEvent)=> {

    oEvent = oEvent || window.event;

    if (oEvent.keyCode == '38') {
      // up arrow
      this._dropdown.decrementActiveIndex();
    }
    else if (oEvent.keyCode == '40') {
      // down arrow
      this._dropdown.incrementActiveIndex();
    }
    else if (oEvent.keyCode == '37') {
      // left arrow
    }
    else if (oEvent.keyCode == '39') {
      // right arrow
    }
    else if (oEvent.keyCode == '13') {
      // enter
      var oModel = this._dropdown.getActiveModel();
      if(oModel){
        this.handleListNodeClicked(oModel);
      }
    }

  }

  handleInputBoxClicked =()=> {
    if (this.props.disabled) {
      return;
    }

    var divInput = this.multiSearchInputBox.current;

    if (!divInput.value) {
      this.changeStateByProperty('active', true);
      document.addEventListener('click', this.handleInputBoxBlurred);
    }

    //On click on input hide the shown item in case of singleSelect
    /*if(this.props.singleSelect){
      $(ReactDOM.findDOMNode(this)).find(".singleSelectedItem").hide();
    }*/
  }

  canRaiseBlurEvent =(oEvent)=> {
    var oNode = oEvent.target;
    //Check if event target is searchBarContainer
    //And its not any other multi-search context used anywhere else then don't blur
    while (oNode.parentNode) {
      if (oNode.classList[0] == "searchBarContainer") {
        var oSelfSearchBarContainer = this.searchBarContainer.current;

        if(oSelfSearchBarContainer == oNode){
          return false;
        }

        break;
      }

      if (!(this.props.singleSelect)) {
        if (CS.includes(oNode.classList, 'dropDownNodeContainer')) {
          return false;
        }
      }

      oNode = oNode.parentNode;
    }


    //Check if cross Icon is clicked then  also don't blur
    if (oEvent.target.className == 'crossIcon') {
       return false;
    }

    //Check if its not single select and target is dropDownNodeContainer then also don't blur
    if (!(this.props.singleSelect)) {
      if (oEvent.target.className == 'dropDownNodeContainer') {
        return false;
      }
    }

    //If clicked anywhere else then raise blur event
    return true;
  }

  handleInputBoxBlurred =(oEvent)=> {

    if (this.props.disabled) {
      return;
    }
    var bRaiseBlurEvent = this.canRaiseBlurEvent(oEvent);
    if (bRaiseBlurEvent) {
      document.removeEventListener('click', this.handleInputBoxBlurred);
      var oInputNode = this.multiSearchInputBox.current;
      if (oInputNode) {
        var iInputView = oInputNode;
        iInputView.style.display = "none";
        iInputView.value = null;
      }

      if (this.props.singleSelect) {
        var thisComponent = ReactDOM.findDOMNode(this);
        // var aNodeList = $(thisComponent).find(".singleSelectedItem");
        var aNodeList = thisComponent.getElementsByClassName("singleSelectedItem");
        CS.forEach(aNodeList, function (oNode, iIndex) {
          oNode.style.display = "inline-block";
        });
      }

      this.changeStateByProperty('active', false);
      if (this.props.checkbox && !this.props.singleSelect) {
        var sContextKey = this.props.context;
        var aCheckedItems = this.state.checkedItems;
        if(CS.isFunction(this.props.onChange)){
          this.callOnChangeHandler(aCheckedItems, false);
        } else {
          EventBus.dispatch(oEvents.MULTI_SEARCH_DROP_DOWN_LIST_NODE_BLURRED, sContextKey, aCheckedItems);
        }
      }
    }
  }

  handleListNodeCheckboxClicked =(oModel)=> {
    var aCheckedItems = this.state.checkedItems;
    if (CS.includes(aCheckedItems, oModel.id)) {
      CS.remove(aCheckedItems, function (sId) {return sId == oModel.id})
    } else {
      aCheckedItems.push(oModel.id);
    }
    this.changeStateByProperty("checkedItems", aCheckedItems);
  }

  handleListNodeClicked =(oModel)=> {
    var sContextKey = this.props.context;
    var bIsSingleSelect = this.props.singleSelect;
    if (!bIsSingleSelect && this.props.checkbox) {
      this.handleListNodeCheckboxClicked(oModel);
      return;
    }
    if(oModel.id != sDefaultValue){
      if(CS.isFunction(this.props.onSelect)){
        this.props.onSelect(oModel.id);
      } else if (CS.isFunction(this.props.onChange)) {
        this.callOnChangeHandler(oModel.id, false);
      } else {
        EventBus.dispatch(oEvents.MULTI_SEARCH_DROP_DOWN_LIST_NODE_CLICKED, this, sContextKey, oModel.id, bIsSingleSelect);
      }

      this.changeStateByProperty('active', false);
      var divInput = this.multiSearchInputBox.current;
      divInput.value = null;
      divInput.style.display = "inline-block";
      divInput.focus();
    }
  }

  getSelectedList =()=> {
    var __props = this.props;
    var aSelectedItems = __props.selectedItems || [];
    var aItems = __props.items || [];
    var bIsDisabled = __props.disabled;
    var bIsSingleSelect = __props.singleSelect;
    var bCrossDisabled = __props.disableCross || false;

    return CS.map(aSelectedItems, function (sId) {
      var oItem = CS.find(aItems, {id: sId});
      if (CS.isEmpty(oItem)) {
        return null;
      }
      var sName = CS.getLabelOrCode(oItem);
      var sCrossIconDiv = '';

      if (!bIsDisabled && !bCrossDisabled) {
        sCrossIconDiv = <TooltipView placement="bottom" label={getTranslation().REMOVE}>
          <div className="crossIcon" onClick={this.handleCrossIconClicked.bind(this, sId)}></div>
        </TooltipView>;
      }
      var sClassName = bIsSingleSelect ? "singleSelectedItem" : "selectedItems";
      var sSelectedItemsNameContainer = bIsDisabled ? "disabledMultiSearch" : "selectedItemsNameContainer";
      var sColorHex = oItem.color || "#e8e8e8";

      var oTagDetailView = null;
      var sRandom = Math.random()*1000;
      if (!CS.isEmpty(oItem.codeName)) {
        sName += " (" + oItem.codeName + ")";
      }

      var oStyle = {};
      if(oItem.color) {
        oStyle.backgroundColor = oItem.color;
      }

      if (oItem.iconKey) {
        var sIcon = ViewUtils.getIconUrl(oItem.iconKey);
        var oBackgroundColor = {};
        oTagDetailView = (<TooltipView placement="top" label={sName} key={oItem.id + sRandom + "_overlay"}>
          <div className={"tagIconWrapper " + sSelectedItemsNameContainer}>
            <div className="tagIcon" style={oBackgroundColor}>
              <ImageFitToContainerView imageSrc={sIcon}/>
            </div>
            <div className="tagText">{sName}</div>
          </div></TooltipView>);
      } else if (oItem.color) {
        oTagDetailView = (<div className={"tagIconWrapper " + sSelectedItemsNameContainer} title={sName}
                               key={oItem.id + sRandom + "_overlay"}>
          <div className={"tagColor " + sSelectedItemsNameContainer} style={oStyle}></div>
          <div className="tagText">{sName}</div>
        </div>);
      } else if (oItem.codeName) {
        oTagDetailView = (<div className={"tagValue " + sSelectedItemsNameContainer} title={sName}
                               key={oItem.id + sRandom + "_overlay"}>{sName}</div>);
      } else {
        oTagDetailView = (<div className={"tagValue " + sSelectedItemsNameContainer} title={sName}
                               key={oItem.id + sRandom + "_overlay"}>{sName}</div>);
      }

      return (
          <div className={sClassName} key={sName + "_" + sId}>
            {/*<div className={sSelectedItemsNameContainer}>{sName}</div>*/}
            <div className="tagContainer">{oTagDetailView}{sCrossIconDiv}</div>
            {/*{sCrossIconDiv}*/}
          </div>
      );
    }.bind(this));
  }

  handleCrossIconClicked =(sId)=> {
    var sContextKey = this.props.context;

    if(CS.isFunction(this.props.onDeselect)){
      this.props.onDeselect(sId);
    } else if (CS.isFunction(this.props.onChange)) {
      this.callOnChangeHandler(sId, true);
    } else {
      EventBus.dispatch(oEvents.MULTI_SEARCH_BAR_CROSS_ICON_CLICKED, this, sContextKey, sId);
    }
  }

  searchBarContainerClicked =(sId)=> {

    if(this.props.disabled){
      return ;
    }
    var divInput = this.multiSearchInputBox.current;
    divInput.style.display = "inline-block";
    divInput.focus();
    this.resetState();
    this.handleInputBoxClicked();
  }

  componentDidMount =()=> {
    this.stateWiseRender();
  }

  componentDidUpdate =()=> {
    this.stateWiseRender();
  }

  stateWiseRender =()=> {
    var bActiveStatus = this.state.active;
    var thisComponent = ReactDOM.findDOMNode(this);
    var iWidth = 25; // because x div width is hidden initially on hover its width get increases
    var aParentNode = thisComponent.getElementsByClassName("searchBarContainer");
    var oParentStyle = window.getComputedStyle(aParentNode[0], null);
    var iParentWidth = oParentStyle.getPropertyValue("width").split('px')[0] - 65; //-85 is morecontainer's width
    var bFlag = false;
    var aNodeList = thisComponent.getElementsByClassName("selectedItems");
    CS.forEach(aNodeList, function (oNode, iIndex) {
      oNode.style.display = "inline-block";
    });

    if (bActiveStatus == false) {

      var divInput = this.multiSearchInputBox.current;
      divInput.value = null;
      divInput.style.display = "none";

      if (!(this.props.singleSelect)) {
        //calculate divs inside searchBarContainer

        var oMoreCounterDom = this.moreCounter.current;
        oMoreCounterDom.style.display = "none";

        //Check if total width of items exceeds parent width then hide exceeding items and show their count
        CS.forEach(aNodeList, function (oNode, iIndex) {
          iWidth += parseInt(oNode.offsetWidth);
          if (iWidth > iParentWidth) {
            /*var sMoreLang = getTranslation().MORE;
            var iMoreCount = aNodeList.length - iIndex + sMoreLang;
            if (this.refs.moreCounter && bFlag == false) {
              this.refs.selectedItemCount.innerText = "+" + iMoreCount;
              oMoreCounterDom.style.display = "block";
              bFlag = true;
            }*/
          }

          //hide remaining selected classNames
          if (bFlag == true) {
            oNode.style.display = "none";
          }
        });
      }
    }
  }

/*
  getInputBox =()=> {
    if (this.state.active) {
      return (<input ref={this.multiSearchInputBox}
                     className="multiSearchInputBox"
                     onChange={this.handleInputValueChanged}
                     onClick={this.handleInputBoxClicked}
          />);
    }
    else {
      return '';
    }
  }
*/

  getHiddenElementsCountView =()=> {
    var sSelectedItemsNameContainer = (this.props.disabled) ? "disabledMultiSearch" : "selectedItemsNameContainer" ;
    if (!this.state.active) {
      return (
          <div className="moreCounter" ref={this.moreCounter}>
            <div className={sSelectedItemsNameContainer}></div>
          </div>
      );
    }

    return "";
  }

  getNothingSelectedDom =()=> {

    var __props = this.props;
    var __state = this.state;

    var bIsActive = __state.active;
    var aSelectedItems = __props.selectedItems || [];

    if (!bIsActive && !aSelectedItems.length) {
      return (<div className="multiSelectNothingSelected">{getTranslation().NOTHING_IS_SELECTED}</div> );
    }

    return null;
  }

  render() {
    var aDropDownList = this.state.active ? this.getDropDownListView() : null;
    var aSelectedItems = this.getSelectedList();

    var aMoreCounter = this.getHiddenElementsCountView();
    var className= "searchBarContainer ";
    var oNothingSelected = this.getNothingSelectedDom();

    var bHideTooltip = this.props.hideTooltip;
    var oTooltip = !bHideTooltip ?
        <TooltipView placement="bottom" label={this.props.label}>
          <div className="multiSelectSearchLabel">{this.props.label}</div>
        </TooltipView> :
        <div className="multiSelectSearchLabel">{this.props.label}</div>;


    return (
        <div className="searchBarView">
          {oTooltip}
          <div className={className} onClick={this.searchBarContainerClicked} ref={this.searchBarContainer}>
            {aSelectedItems}
            <input ref={this.multiSearchInputBox}
                   className= "multiSearchInputBox"
                   onChange={this.handleInputValueChanged}
                   onKeyDown={this.handleKeyDown}
                   onClick={this.handleInputBoxClicked}
                />
            {aMoreCounter}
            {oNothingSelected}
          </div>

          {aDropDownList}
        </div>
    );
  }
}

MultiSelectSearchView.propTypes = oPropTypes;

export const view = MultiSelectSearchView;
export const events = oEvents;
