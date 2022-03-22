import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ReactDOM from 'react-dom';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import KeyboardNavigationForLists , { oCustomEvents } from '../../../../../commonmodule/HOC/keyboard-navigation-for-lists';

import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as BreadcrumbView } from '../../../../../viewlibraries/breadcrumbview/breadcrumb-wrapper-view';
import { view as CustomMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import oFilterPropType from './../tack/filter-prop-type-constants';
import HierarchyTypesDictionary from './../../../../../commonmodule/tack/hierarchy-types-dictionary';
import ViewUtils from './utils/view-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_CREATE_NEW_COLLECTION_BUTTON_CLICKED: "handle_create_new_collection_button_clicked",
  HANDLE_MODIFY_COLLECTION_CLICKED: "handle_modify_collection_clicked",
  HANDLE_COLLECTION_SELECTED: "handle_collection_selected",
  HANDLE_DELETE_COLLECTION_CLICKED: "handle_delete_collection_clicked",
  HANDLE_NEXT_COLLECTION_CLICKED: "handle_next_collection_clicked",
  HANDLE_STATIC_COLLECTION_BACK_BUTTON_CLICKED: "handle_static_collection_back_button_clicked",
  HANDLE_STATIC_COLLECTION_ORGANISE_ICON_CLICKED: "handle_static_collection_organise_icon_clicked",
  HANDLE_STATIC_COLLECTION_ROOT_BREADCRUMB_CLICKED: "handle_static_collection_root_breadcrumb_clicked",
  HANDLE_COLLECTION_VISIBILITY_MODE_CHANGED: "handle_collection_visibility_mode_changed",
  HANDLE_COLLECTION_CREATION_VIEW_BREADCRUMB_ITEM_CLICKED: "handle_collection_creation_view_breadcrumb_item_clicked"
};

const oPropTypes = {
  isDynamic: ReactPropTypes.bool,
  onClose: ReactPropTypes.func,
  collectionData: ReactPropTypes.object,
  collectionPopOverWidth: ReactPropTypes.number,
  sectionComponents: ReactPropTypes.array,

  onKeyPressHandler: ReactPropTypes.func,
  registerCustomEvent: ReactPropTypes.func,
  itemInFocus: ReactPropTypes.number,
  setIndexMap: ReactPropTypes.func
};

// @KeyboardNavigationForLists
// @CS.SafeComponent
class CollectionCreationView extends React.Component {

  constructor(props) {
    super(props);

    this.collectionList = React.createRef();
    this.userInput = React.createRef();

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

  state = {
    userInput: "",
    selectedCollection: {},
    privateMode: false
  };

  iMaxWidth = 0;

  componentDidMount() {
    var oCollectionCreationSearchInputDOM = this.searchBox;
    if (!CS.isEmpty(oCollectionCreationSearchInputDOM)) {
      oCollectionCreationSearchInputDOM.focus();
    }
    this.props.registerCustomEvent(oCustomEvents.FOCUS_OF_ITEM, this.showModifyAndOpenButton);
    let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();
    !bIsCurrentUserReadOnly && this.props.registerCustomEvent(oCustomEvents.SELECTION_OF_ITEM, this.handleOpenCollectionClicked);

  }

  componentDidUpdate() {
    var iActiveLevel = this.props.collectionData.activeLevel;
    this.handleCollectionScrolling(iActiveLevel);
  }

  handleCollectionScrolling = (iActiveLevel) => {
    var iScrollLeft = iActiveLevel*this.props.collectionPopOverWidth;
    var oStaticCollectionDOM = this.collectionList.current;
    oStaticCollectionDOM && (oStaticCollectionDOM.style.marginLeft = -iScrollLeft + "px");
  };

  handleCollectionBreadcrumbClicked = (sContext, oItem) => {
    var oCollectionData = this.props.collectionData;
    var aBreadCrumb = oCollectionData.breadCrumbPath;
    var iActiveLevel = CS.findIndex(aBreadCrumb, {id: oItem.id});
    this.handleCollectionScrolling(iActiveLevel);
    EventBus.dispatch(oEvents.HANDLE_COLLECTION_CREATION_VIEW_BREADCRUMB_ITEM_CLICKED, oItem, sContext);
  };

  handleCreateCollectionButtonClicked = (sId) => {
    var bIsDynamic = this.props.isDynamic;
    var oUserInputDOM = this.searchBox;
    var sCollectionName = oUserInputDOM.value;
    var bIsPrivateMode = this.state.privateMode;

    var oCreationData = {
      isDynamic: bIsDynamic,
      collectionName: sCollectionName,
      id: sId,
      isPrivateMode: bIsPrivateMode
    };

    this.props.onClose();

    EventBus.dispatch(oEvents.HANDLE_CREATE_NEW_COLLECTION_BUTTON_CLICKED, oCreationData, this.props.filterContext);
  };

  handleModifyCollectionClicked = (sContext) => {
    this.props.onClose();
    let sId = this.state.selectedCollection.id;

    EventBus.dispatch(oEvents.HANDLE_MODIFY_COLLECTION_CLICKED, sId, sContext, this.props.filterContext);
  };

  handleOpenCollectionClicked = () => {
    this.props.onClose();
    let oSelectedCollection = this.state.selectedCollection;
    let bIsDynamic = this.props.isDynamic;
    let sContext = bIsDynamic ? "dynamicCollection" : "staticCollection";
    let oNewFilterContextForCollection = {
      filterType: oFilterPropType.COLLECTION,
      screenContext: oSelectedCollection.id
    };
    if (sContext === "staticCollection"){ oSelectedCollection.type = "staticCollection"}
    EventBus.dispatch(oEvents.HANDLE_COLLECTION_SELECTED, this, oSelectedCollection, sContext, oNewFilterContextForCollection);
  };

  handleCollectionDeleteClicked = (sCollectionId, sContext) => {
    EventBus.dispatch(oEvents.HANDLE_DELETE_COLLECTION_CLICKED, this, sCollectionId, sContext, this.props.filterContext);
    if (this.state.selectedCollection.id == sCollectionId) { //clear selectedId from state if the same one is deleted
      this.setState({
        selectedCollection : {}
      });
    }
  };

  handleCollectionNextClicked = (sCollectionId) => {
    this.setState({
      selectedCollection : {}
    });
    EventBus.dispatch(oEvents.HANDLE_NEXT_COLLECTION_CLICKED, sCollectionId);
  };

  handleCollectionPreviousClicked = (iActiveLevel) => {
    this.setState({
      selectedCollection : {}
    });
    var iExpectedLevel = iActiveLevel - 1;
    var oCollectionData = this.props.collectionData;
    var aBreadCrumb = oCollectionData.breadCrumbPath;
    var oExpectedBreadCrumbItem = aBreadCrumb[iExpectedLevel];
    EventBus.dispatch(oEvents.HANDLE_STATIC_COLLECTION_BACK_BUTTON_CLICKED, oExpectedBreadCrumbItem);
  };

  handleStaticCollectionRootBreadCrumbClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_STATIC_COLLECTION_ROOT_BREADCRUMB_CLICKED);
  };

  handleCollectionItemModeChanged = (sId) => {
    var bIsDynamic = this.props.isDynamic;
    EventBus.dispatch(oEvents.HANDLE_COLLECTION_VISIBILITY_MODE_CHANGED, sId, bIsDynamic, this.props.filterContext);
  };

  showModifyAndOpenButton = (oCollection) => {
    let sAlreadySelectedId = this.state.selectedCollection.id;
    let oCollectionToSet = (sAlreadySelectedId == oCollection.id) ? {} : oCollection;

    this.props.setItemInFocus(oCollectionToSet.id);
    this.setState({
      selectedCollection: oCollectionToSet
    });
  };

  handleUserInputChanged = (oEvent) => {
    var sUserInput = oEvent.target.value;
    this.setState({
      userInput: sUserInput,
      selectedCollection: {}
    });
  };

  handleSearchBoxKeyDown = (oEvent) => {
    let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();
    if (oEvent.keyCode === 13 && !bIsCurrentUserReadOnly) {
      let oCollectionData = this.props.collectionData;
      let oActiveCollection = oCollectionData.activeCollection;
      if (CS.isNotEmpty(oActiveCollection) && this.props.isDynamic) {
        oEvent.nativeEvent.dontRaise = true;
      }
      else {
        this.handleCreateCollectionButtonClicked();
      }
    }
  };

  handleSearchBoxClicked =()=> {
    this.props.setItemInFocus(-1);
  };

  handleOrganiseCollectionIconClicked = (sClickedNodeId) => {
    this.props.onClose();
    let oNewFilterContextForOrganiseCollection = {
      filterType: oFilterPropType.HIERARCHY,
      screenContext: HierarchyTypesDictionary.COLLECTION_HIERARCHY
    };
    EventBus.dispatch(oEvents.HANDLE_STATIC_COLLECTION_ORGANISE_ICON_CLICKED, sClickedNodeId, oNewFilterContextForOrganiseCollection);
  };

  handleVisibilityModeChanged = () => {
    var bIsPrivateMode = this.state.privateMode;
    this.setState({
      privateMode: !bIsPrivateMode
    });
  };

  getCollectionItemViewList = (aCollections, sContext) => {
    var aCollectionViews = [];
    var _this = this;
    var oCollectionData = this.props.collectionData;
    var oActiveCollection = oCollectionData.activeCollection;
    var iActiveLevel = oCollectionData.activeLevel;
    let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();

    if (CS.isEmpty(aCollections)) {
      var sEmptyListMessage = (this.props.isDynamic) ? getTranslation().NO_BOOKMARKS_SAVED : getTranslation().NO_COLLECTIONS_CREATED;
      aCollectionViews.push(
          <div className="emptyListMessage">{sEmptyListMessage}</div>
      );
    } else {
      let iItemIndex = 0;
      this.props.setIndexMap({});
      let oIndexMap = [];
      CS.forEach(aCollections, function (oCollection, iIndex) {
        var sId = oCollection.id;
        if(oActiveCollection.id === sId) {
          //Do not show active collection or bookmark in the list. - Shashank
          return;
        }
        var sCreatedBy = oCollection.createdBy;
        var sCreatedOn = ViewUtils.getDateAttributeInTimeFormat(oCollection.createdOn);
        var sLabel = CS.getLabelOrCode(oCollection);
        var sCollectionItemClass = "collectionItem ";
        var sUserInput = _this.state.userInput;

        var oPublicButtonDom = null;
        var oNextButtonDom = null;
        var oPreviousButtonDom = null;
        var oOrganiseButtonDom = null; //_this.getOrganiseCollectionIconViewDOM(sId);
        var sCreatorsInfo = ViewUtils.getUserNameById(sCreatedBy);
        if(sContext == "staticCollection"){
          oNextButtonDom = (<TooltipView label={getTranslation().NEXT}>
            <div className="collectionItemNavigatorButton collectionItemNext"
                 onClick={_this.handleCollectionNextClicked.bind(_this, sId)}></div>
          </TooltipView>);

          oPreviousButtonDom = iActiveLevel > 0 ? (<TooltipView label={getTranslation().PREVIOUS}>
            <div className="collectionItemNavigatorButton collectionItemPrevious"
                 onClick={_this.handleCollectionPreviousClicked.bind(_this, iActiveLevel)}></div>
          </TooltipView>) : null;
        }

        if(oCollection.createdBy == ViewUtils.getCurrentUser().id){
          var sTooltipLabel = getTranslation().COLLECTION_PUBLIC_MODE;
          var sVisibilityButtonClassName = "collectionItemPublicButton";

          if(!oCollection.isPublic){
            sTooltipLabel = getTranslation().COLLECTION_PRIVATE_MODE;
            sVisibilityButtonClassName = "collectionItemPublicButton collectionItemPrivateButton";
          }

          if (iActiveLevel == 0 && !bIsCurrentUserReadOnly) {
            oPublicButtonDom = (<TooltipView placement="top" label={sTooltipLabel}>
              <div className={sVisibilityButtonClassName}
                   onClick={_this.handleCollectionItemModeChanged.bind(_this, sId)}></div>
            </TooltipView>)
          }
        }

        if (sLabel.toLowerCase().includes(sUserInput.toLowerCase())) {
          oIndexMap[iItemIndex] = iIndex;
          if (sId === _this.props.itemInFocus) {
            sCollectionItemClass += "isSelected ";
          }
          aCollectionViews.push(
              <div className={sCollectionItemClass} >
                {oPreviousButtonDom}
                <div className="collectionItemInformation"
                     onClick={_this.showModifyAndOpenButton.bind(_this, oCollection)}
                     ref = {_this.setRef.bind(_this, 'contextMenuItem'+ iItemIndex++)}
                >
                  <div className="collectionItemLabel" title={sLabel}>{sLabel}</div>
                  <div className="collectionItemCreatedBy" title={sCreatorsInfo}>{sCreatorsInfo}</div>
                  <div className="collectionItemCreatedOn" title={sCreatedOn}>{sCreatedOn}</div>
                </div>
                {oNextButtonDom}
                {bIsCurrentUserReadOnly ? null :
                    <TooltipView label={getTranslation().DELETE}>
                      <div className="collectionItemDelete"
                           onClick={_this.handleCollectionDeleteClicked.bind(_this, sId, sContext)}>

                      </div>
                    </TooltipView>}
                {oPublicButtonDom}
                {oOrganiseButtonDom}
              </div>
          );
        }
      });

      this.props.setIndexMap(oIndexMap);

    }
    return aCollectionViews;
  };

  getDynamicCollections = () => {
    var oCollectionData = this.props.collectionData;
    var aDynamicCollectionList = oCollectionData.dynamicCollectionList;
    var aDynamicCollectionViews = this.getCollectionItemViewList(aDynamicCollectionList, "dynamicCollection");
    if (CS.isEmpty(aDynamicCollectionViews)) {
      return null;
    }

    var oCollectionsListStyle = {
      width: this.props.collectionPopOverWidth + "px"
    };

    return (
        <div className="collectionListContainer" style={oCollectionsListStyle}>
          {aDynamicCollectionViews}
        </div>
    );
  };

  getStaticCollections = () => {
    var _this = this;
    var oCollectionData = this.props.collectionData;
    var aStaticCollectionList = [];
    var aStaticCollectionViews = [];
    var oScrollContainerStyle = {
      width: this.props.collectionPopOverWidth + "px"
    };
    var oStaticCollectionMap = oCollectionData.staticCollection;
    CS.forEach(oStaticCollectionMap, function (oCollection, iLevel) {
      aStaticCollectionList = oCollection.collectionList;
      aStaticCollectionViews.push(
          <div className="scrollContainer" style={oScrollContainerStyle}>
            {_this.getCollectionItemViewList(aStaticCollectionList, "staticCollection")}
          </div>);
    });
    if (CS.isEmpty(aStaticCollectionViews)) {
      return null;
    }
    var oCollectionsListStyle = {
      width: this.iMaxWidth+ "px",
      transition: 'all .2s',
    };

    return (
        <div className="collectionListContainer">
          <div className="collectionsList" style={oCollectionsListStyle} ref={this.collectionList}>
            {aStaticCollectionViews}
          </div>
        </div>
    );
  };

  getButtonDOM = (sContext) => {
    let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();
    var sUserInput = this.state.userInput;
    var oSelectedCollection = this.state.selectedCollection;
    var oCollectionData = this.props.collectionData;
    var aBreadcrumb = oCollectionData.breadCrumbPath;
    var sSelectedCollectionId = "";
    if(!CS.isEmpty(aBreadcrumb)){
      var iIndex = aBreadcrumb.length;
      var oCollection = aBreadcrumb[iIndex-1];
      sSelectedCollectionId = oCollection.id;
    }
    var sButtonContainerClass = "buttonContainer ";

    if (CS.isEmpty(sUserInput) && CS.isEmpty(oSelectedCollection)) {
      sButtonContainerClass += " collapsed ";
    }

    var oApplyButtonLabelStyles = {
      fontSize: 11,
      lineHeight: "24px",
      textTransform: "uppercase",
      padding: "0px"
    };

    var oOnTouchTapHandler = null;

    if (!CS.isEmpty(oSelectedCollection)) {

      sButtonContainerClass += "modifyAndOpenButtonContainer";
      var sOpenButtonLabel = getTranslation().OPEN;
      var sModifyButtonLabel = "";

      if (this.props.isDynamic) {
        sModifyButtonLabel = getTranslation().MODIFY_BOOKMARK;
      } else {
        sModifyButtonLabel = getTranslation().ADD_TO_COLLECTION;
      }
      var oActiveCollection = oCollectionData.activeCollection;

      let bIsAddToCollectionButtonVisible = (oActiveCollection.id !== oSelectedCollection.id);

      if(bIsAddToCollectionButtonVisible) {
        let oOrganiseButtonDOM = this.props.isDynamic || !CS.isEmpty(oActiveCollection) || bIsCurrentUserReadOnly
            ? null :
            <TooltipView placement="bottom" label={getTranslation().ORGANISE}>
              <div className="modifyAndOpenButtons">
                <CustomMaterialButtonView
                    label={getTranslation().ORGANISE}
                    isRaisedButton={true}
                    isDisabled={false}
                    // style={oButtonStyle}
                    labelStyle={oApplyButtonLabelStyles}
                    onButtonClick={this.handleOrganiseCollectionIconClicked.bind(this, oSelectedCollection.id)}/>
              </div>
            </TooltipView>;

        var bShowModifyCollectionDOM = null;
        if(this.props.isDynamic) {
          //Bookmark is not open, then show modify collection
          bShowModifyCollectionDOM = CS.isEmpty(oActiveCollection);
        } else {
          //No content is selected then hide add to collection
          var sSelectedContentIds = oCollectionData.selectedContentIds;
          bShowModifyCollectionDOM = !CS.isEmpty(sSelectedContentIds);
        }

        var oModifyCollectionDOM = (bShowModifyCollectionDOM && !bIsCurrentUserReadOnly) ?
            <TooltipView placement="bottom" label={sModifyButtonLabel}>
              <div className="modifyAndOpenButtons">
                <CustomMaterialButtonView
                    label={sModifyButtonLabel}
                    isRaisedButton={true}
                    isDisabled={false}
                    labelStyle={oApplyButtonLabelStyles}
                    onButtonClick={this.handleModifyCollectionClicked.bind(this, sContext)}/>
              </div>
            </TooltipView> : null;

        return (
            <div className={sButtonContainerClass}>
              <TooltipView placement="bottom" label={sOpenButtonLabel}>
                <div className="modifyAndOpenButtons">
                  <CustomMaterialButtonView
                      label={sOpenButtonLabel}
                      isRaisedButton={true}
                      isDisabled={false}
                      labelStyle={oApplyButtonLabelStyles}
                      onButtonClick={this.handleOpenCollectionClicked}/>
                </div>
              </TooltipView>
              {oOrganiseButtonDOM}
              {oModifyCollectionDOM}
            </div>
        );
      }
      return null;

    } else {
      if (!bIsCurrentUserReadOnly) {
        var oCreateButtonLabelDOM = (
            <span className="createButtonLabel">
             <span>{getTranslation().CREATE}</span>
             <span> "</span>
             <span className="userInputContainer">
               <div className="userInput">{sUserInput}
               </div>
             </span>
             <span>"</span>
          </span>
        );

        sButtonContainerClass += "createButton";
        oOnTouchTapHandler = this.handleCreateCollectionButtonClicked.bind(this, sSelectedCollectionId);
        return (this.props.isDynamic && !CS.isEmpty(oCollectionData.activeCollection) ? null :
            <div className={sButtonContainerClass}>
              <CustomMaterialButtonView
                  children={oCreateButtonLabelDOM}
                  isRaisedButton={true}
                  isDisabled={false}
                  onButtonClick={oOnTouchTapHandler}/>
            </div>);
      } else {
        return null;
      }
    }
  };

  handleClearSearchText = () => {
    this.setState({
      userInput: ""
    });
    var oCollectionCreationSearchInputDOM = this.searchBox;
    if (!CS.isEmpty(oCollectionCreationSearchInputDOM)) {
      oCollectionCreationSearchInputDOM.focus();
    }
  };

  getOrganiseCollectionIconViewDOM = (sId) => {
    var bIsDynamic = this.props.isDynamic;
    var oCollectionData = this.props.collectionData;
    var oActiveCollection = oCollectionData.activeCollection;
    var aStaticCollectionList = oCollectionData.staticCollectionList;
    let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();

    if(bIsDynamic || !CS.isEmpty(oActiveCollection) || CS.isEmpty(aStaticCollectionList)
    || bIsCurrentUserReadOnly){
      return null;
    }
    else{
      return (
          <TooltipView placement="bottom" label={getTranslation().ORGANISE}>
            <div className="organiseCollectionIcon" onClick={this.handleOrganiseCollectionIconClicked.bind(this, sId)}>
              {getTranslation().ORGANISE}
            </div>
          </TooltipView>);
    }
  };

  getPublicModeButtonDom = () => {
    var sUserInput = this.state.userInput;
    var oSelectedCollection = this.state.selectedCollection;
    var bIsPrivateMode = this.state.privateMode;
    var sLabel = getTranslation().COLLECTION_PUBLIC_MODE;
    var sButtonClassName = "publicModeButton";
    var oCollectionData = this.props.collectionData;
    var iActiveLevel = oCollectionData.activeLevel;
    let bIsCurrentUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();

    if(iActiveLevel > 0){
      return null;
    }

    if(bIsPrivateMode){
      sLabel = getTranslation().COLLECTION_PRIVATE_MODE;
      sButtonClassName = sButtonClassName + " isPrivateButton";
    }

    if(CS.isEmpty(oSelectedCollection) && !CS.isEmpty(sUserInput) && !bIsCurrentUserReadOnly){

      return(<TooltipView placement="top" label={sLabel}>
        <div className={sButtonClassName} onClick={this.handleVisibilityModeChanged}></div>
      </TooltipView>);
    }

    return null;
  };

  render() {

    var bIsDynamic = this.props.isDynamic;
    var oCollectionData = this.props.collectionData;
    var sContext = bIsDynamic ? "dynamicCollection" : "staticCollection";
    var oButtonDOM = this.getButtonDOM(sContext);
    var sHeaderLabel = "";

    var oCollectionListView = null;
    let bHideHeader = false;
    let sLabelClass = "collectionListLabelsContainer ";
    if (this.props.isDynamic) {
      let aDynamicCollectionList = oCollectionData.dynamicCollectionList;
      let oActiveCollection = oCollectionData.activeCollection;
      if (CS.isEmpty(oActiveCollection)) {
        bHideHeader = CS.isEmpty(aDynamicCollectionList);
      } else {
        bHideHeader = (CS.some(aDynamicCollectionList, {id: oActiveCollection.id}) && aDynamicCollectionList.length === 1)
      }
      oCollectionListView = this.getDynamicCollections();
    } else {
      var iActiveLevel = oCollectionData.activeLevel;
      let oStaticCollectionList = oCollectionData.staticCollection;
      var iWidth = (iActiveLevel) ? this.props.collectionPopOverWidth*(iActiveLevel+1) : this.props.collectionPopOverWidth;
      this.iMaxWidth = iWidth > this.iMaxWidth ? iWidth : this.iMaxWidth;
      (iActiveLevel > 0) && (sLabelClass += "notOnRootNode");
      oCollectionListView = this.getStaticCollections();
      bHideHeader = !CS.isEmpty(oStaticCollectionList[iActiveLevel]) && CS.isEmpty(oStaticCollectionList[iActiveLevel].collectionList);
    }

    var sPlaceholder = null;
    if(oCollectionListView) {
      if(this.props.isDynamic) {
        sPlaceholder = CS.isEmpty(oCollectionData.activeCollection) ? getTranslation().SEARCH_OR_CREATE + "..." : getTranslation().SEARCH;
      } else {
        sPlaceholder = getTranslation().SEARCH_OR_CREATE;
      }
    } else {
      sPlaceholder = (getTranslation().CREATE + "...");
    }

    var oSearchIconView = this.state.userInput ? (<div className="searchClearIcon" onClick={this.handleClearSearchText}></div>) : null;
    var aBreadCrumbPath = this.props.collectionData.breadCrumbPath;
    var iActiveLevel = this.props.collectionData.activeLevel;
    var oBreadCrumbView = (iActiveLevel >= 0) ? (<div className="breadCrumbSection">
            <div className="collections" onClick={this.handleStaticCollectionRootBreadCrumbClicked}></div>
            <BreadcrumbView breadcrumbPath={aBreadCrumbPath} context={sContext} clickHandler={this.handleCollectionBreadcrumbClicked.bind(this, sContext)}/>
          </div>) : null;

    var oOrganiseIcon = (iActiveLevel == 0) ? this.getOrganiseCollectionIconViewDOM("noId") : null;
    var oPublicPvtIcon = (bIsDynamic && !CS.isEmpty(oCollectionData.activeCollection)) ? null : this.getPublicModeButtonDom();

    let oHeaderDOM = bHideHeader ? null :
                     (<div className={sLabelClass}>
                       <div className={"collectionName"}>{getTranslation().NAME}</div>
                       <div className={"createdBy"}>{getTranslation().CREATED_BY}</div>
                       <div className={"creationDate"}>{getTranslation().CREATED_ON}</div>
                     </div>);

    return (
        <div className="collectionCreationViewContainer" onKeyDown={this.props.onKeyPressHandler} tabIndex={0} ref={this.setRef.bind(this, 'contextMenuView')}>

          <div className="headerSectionContainer">
            {oBreadCrumbView}
            {oOrganiseIcon}
          </div>

          {/*upper section*/}
          <div className="upperSectionContainer">
            <div className="userInputContainer">
                <input className="userInput"
                       ref={this.setRef.bind(this, 'searchBox')}
                       onKeyDown={this.handleSearchBoxKeyDown}
                       onClick={this.handleSearchBoxClicked}
                       onBlur={null}
                       onChange={this.handleUserInputChanged}
                       value={this.state.userInput}
                       type="text"
                       placeholder={sPlaceholder}/>
                {oSearchIconView}
            </div>
            {oHeaderDOM}
          </div>


          {/*middle section*/}
          <div className="middleSectionContainer" ref = {this.setRefForScrollbar.bind(this, 'scrollbar')}>
              {oCollectionListView}
          </div>

          {/*lower section*/}
          <div className="lowerSectionContainer">
            {oPublicPvtIcon}
            {oButtonDOM}
          </div>

        </div>
    );
  }
}

export const view = KeyboardNavigationForLists(CollectionCreationView);
export const events = oEvents;
