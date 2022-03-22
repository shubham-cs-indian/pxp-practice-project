import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";

const oEvents = {
    HANDLE_LIST_SEARCHED: "handle_list_searched",
    HANDLE_SEARCH_LIST_ITEM_NODE_CLICKED: "handle_search_list_node_clicked",
    HANDLE_SEARCH_LIST_LOAD_MORE_CLICKED: "handle_search_list_load_more_clicked"
};

const oPropTypes = {
  data: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  context: ReactPropTypes.string,
  handleSearch: ReactPropTypes.func,
  handleListItemNodeClicked: ReactPropTypes.func,
  handleListLoadMoreClicked: ReactPropTypes.func
};
/**
 * @class ItemListPanelView - Used to show list of items on panel. It may have Tags, Attributes or Roles.
 * @memberOf Views
 * @property {object} [data] - Contains the data which required to generate list(ex: applied attribute list, applied tag list and attribute list etc.)
 * @property {string} [context] - Used to differentiate which operation will be performed.
 */

// @CS.SafeComponent
class ItemListPanelView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      isAttributeCollapsed: false,
      isTagCollapsed: false,
      isRoleCollapsed: false,
    }

    this.searchInput = React.createRef();
  }

    componentDidMount() {
        this.setSearchInputFromProps();
    }

    componentDidUpdate() {
        this.setSearchInputFromProps();
    }

    setSearchInputFromProps =()=> {
        var oSearchInputDOM = this.searchInput.current || {};
        var oSearchPanelData = this.props.data || {};
        oSearchInputDOM.value = oSearchPanelData.searchInput || "";
    }

    handleSearch = (sSearchText) => {
        if (this.props.handleSearch) {
            this.props.handleSearch(sSearchText, this.props.context, this.props.filterContext);
        } else {
            EventBus.dispatch(oEvents.HANDLE_LIST_SEARCHED, sSearchText, this.props.context);
        }
    }

    handleListItemNodeClicked =(sItemId, sType)=> {
        var sContext = this.props.context;

        if (this.props.handleListItemNodeClicked) {
            this.props.handleListItemNodeClicked(sItemId, sType, sContext, this.props.filterContext);
        } else {
            EventBus.dispatch(oEvents.HANDLE_SEARCH_LIST_ITEM_NODE_CLICKED, sItemId, sType, sContext, this.props.filterContext);
        }
    }

    handleListLoadMoreClicked = (sType) => {
        if (this.props.handleListLoadMoreClicked) {
            this.props.handleListLoadMoreClicked(sType, this.props.context, this.props.filterContext);
        } else {
            EventBus.dispatch(oEvents.HANDLE_SEARCH_LIST_LOAD_MORE_CLICKED, sType, this.props.context);
        }
    }

    handleClearSearchText =()=> {
      this.handleSearch("");
    }

    handleSearchKeyPressed =(oEvent)=>{
        if (oEvent.key === 'Enter' || oEvent.keyCode === 13) {
            oEvent.target.blur();
        }
    }

    handleItemCollapseToggled =(sType)=> {
        var bStateValue = false;
        if(sType === "attributes"){
            bStateValue = this.state.isAttributeCollapsed;
            this.setState({
                isAttributeCollapsed: !bStateValue
            });
        }else if(sType === "tags"){
            bStateValue = this.state.isTagCollapsed;
            this.setState({
                isTagCollapsed: !bStateValue
            });
        }else if(sType === "roles"){
            bStateValue = this.state.isRoleCollapsed;
            this.setState({
                isRoleCollapsed: !bStateValue
            });
        }
    }


    handleUserInputChanged =(oEvent)=> {
        var sUserInput = oEvent.target.value;
        this.handleSearch(sUserInput);
    }

    getItemListHeaderLabel =(sType)=> {
        var sLabel = '';
        if(sType === "attributes"){
            sLabel = getTranslation().ATTRIBUTES;
        }else if(sType === "tags"){
            sLabel = getTranslation().TAGS;
        }else if(sType === "roles"){
            sLabel = getTranslation().ROLES;
        }
        return sLabel;
    }

    getBodyCollapsedStatus =(sType)=> {
        var bBodyCollapsed = false;
        if(sType === "attributes") {
            bBodyCollapsed = this.state.isAttributeCollapsed
        } else if(sType === "tags") {
            bBodyCollapsed = this.state.isTagCollapsed
        } else if(sType === "roles") {
            bBodyCollapsed = this.state.isRoleCollapsed
        }
        return bBodyCollapsed;
    }

    getListItemDetailedView =(aItems, sType)=> {
        var _this = this;
        var aItemDom = [];
        CS.forEach(aItems, function (oItem, iIndex) {
          let sImageSrc = ViewUtils.getIconUrl(oItem.iconKey);
            var sLabel = CS.getLabelOrCode(oItem);
            aItemDom.push(
                <div className="listItem" key={iIndex} onClick={_this.handleListItemNodeClicked.bind(_this, oItem.id, sType)}>
                  <div className="customIcon">
                    <ImageFitToContainerView imageSrc={sImageSrc}/>
                  </div>
                  <div className="itemLabel">{sLabel}</div>
                    <div className="itemAddIcon"></div>
                </div>
            )
        });

        var sLabel = _this.getItemListHeaderLabel(sType);
        var bIsBodyCollapsed = _this.getBodyCollapsedStatus(sType);
        var sBodyCollapsed = bIsBodyCollapsed ? " collapsed": "";

        if(CS.isEmpty(aItemDom)){
            aItemDom.push(
                <div className="listItem" key={"listItem"}>
                    <div className="itemLabel nothingToDisplay">{getTranslation().NO_RESULT}</div>
                </div>
            );
        }

        let oShowMoreDom = <div className="listLoadMore" onClick={this.handleListLoadMoreClicked.bind(this, sType)}>{getTranslation().LOAD_MORE}</div>


        return(
            <div className={"itemContainer " + sType}>
                <div className="itemHeader" onClick={_this.handleItemCollapseToggled.bind(_this, sType)}>
                    <div className={"expandCollapseIcon" + sBodyCollapsed} onClick={_this.handleItemCollapseToggled.bind(_this, sType)}></div>
                    <div className="itemHeaderLabel">{sLabel}</div>
                </div>
                <div className={"itemBody" + sBodyCollapsed}>
                    {aItemDom}
                    {oShowMoreDom}
                </div>
            </div>
        );
    }


    getItemListPanelSearchBar =()=> {
        var oSearchPanelData = this.props.data || {};
        var sSearchInput = oSearchPanelData.searchInput;
        var oSearchIconView = sSearchInput ? (<div className="searchClearIcon" onClick={this.handleClearSearchText}></div>) : null;

        return (
            <div className="userInputContainer">
                <input className="userInput"
                       ref={this.searchInput}
                       onKeyPress={this.handleSearchKeyPressed}
                       onBlur={this.handleUserInputChanged}
                       type="text"
                       placeholder={getTranslation().SEARCH}/>
                {oSearchIconView}
            </div>
        );
    }

    getItemListView =()=> {
        var __props = this.props;
        var oSearchPanelData = __props.data;
        var aAttributeListToShow = oSearchPanelData.attributeList;
        var aTagListToShow = oSearchPanelData.tagList;
        var aRoleListToShow = oSearchPanelData.roleList;

        return  (
            <div className="itemListWrapper">
                {this.getListItemDetailedView(aAttributeListToShow, "attributes")}
                {/*
                {__props.context == "advancedSearch"? this.getListItemDetailedView(aRoleListToShow, "roles") : null}
                */}
                {this.getListItemDetailedView(aTagListToShow, "tags")}
            </div>
        );
    }

    render() {

        return (<div className="itemListPanelContainer">
            <div className="itemListPanelHeader">
                {this.getItemListPanelSearchBar()}
            </div>
            {this.getItemListView()}
        </div>);
    }


}

ItemListPanelView.propTypes = oPropTypes;

export const view = ItemListPanelView;
export const events = oEvents;
