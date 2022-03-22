import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as DraggableListView } from '../draggablelistview/draggable-list-view.js';

var oEvents = {
  CLASS_AVAILABLE_LIST_VIEW_LOAD_MORE: "available_list_view_load_more",
  CLASS_AVAILABLE_LIST_VIEW_SEARCH: "available_list_view_search",
  CLASS_AVAILABLE_LIST_VIEW_HANDLE_ITEM_COLLAPSED_TOGGLED: "handle_item-collapse_toggled"
};

const oPropTypes = {
  entitySearchText: ReactPropTypes.string,
  entitiesTotalCounts: ReactPropTypes.object,
  availableAttributeListModel: ReactPropTypes.array,
  availableRelationshipListModel: ReactPropTypes.array,
  availableRoleListModel: ReactPropTypes.array,
  availableTagListModel: ReactPropTypes.array,
  availableTaxonomyListModel: ReactPropTypes.array,
  propertyCollapsedStatusMap: ReactPropTypes.object
};

class CoreClassAvailableListSearchView extends React.Component {
  constructor (props) {
    super(props);

    this.sectionAvailableSearchBarInput = React.createRef();
  }

  static propTypes = oPropTypes;

  componentDidUpdate() {
    var oSearchInput = this.sectionAvailableSearchBarInput.current;
    if (oSearchInput) {
      oSearchInput.value = this.props.entitySearchText || "";;
    }
  }

  handleListLoadMoreClicked = (sContext) => {
    EventBus.dispatch(oEvents.CLASS_AVAILABLE_LIST_VIEW_LOAD_MORE, sContext);
  };

  getNothingToDisplayView = () => {

    return (<div className="nothingToDisplay">{getTranslation().NO_RESULT}</div>);
  };

  handleSearch = (sSearchText) => {
    EventBus.dispatch(oEvents.CLASS_AVAILABLE_LIST_VIEW_SEARCH, sSearchText);
  };

  handleSearchInputChanged = (oEvent) => {
    var sSearchText = oEvent.target.value || "";
    this.handleSearch(sSearchText)
  };

  handleSearchKeyPressed = (oEvent) => {
    if (oEvent.key === 'Enter' || oEvent.keyCode === 13) {
      oEvent.target.blur();
    }
  };

  getAvailableList = () => {
    var __props = this.props;

    var aAvailableAttributeList = __props.availableAttributeListModel;
    var aAvailableRelationshipListModel = __props.availableRelationshipListModel;
    var aAvailableRoleList = __props.availableRoleListModel;
    var aAvailableTagsList = __props.availableTagListModel;
    var aAvailableTaxonomiesList = __props.availableTaxonomyListModel;

    return {
      attributeModels: aAvailableAttributeList,
      relationshipModels: aAvailableRelationshipListModel,
      roleModels: aAvailableRoleList,
      tagModels: aAvailableTagsList,
      taxonomyModels: aAvailableTaxonomiesList
    }
  };

  getLoadMoreView = (sContext, aModels) => {
    return aModels.length ?
        <div className="listLoadMore"
             onClick={this.handleListLoadMoreClicked.bind(this, sContext)}>{getTranslation().LOAD_MORE}</div>
        : null
  };

  handlePropertyCollapsedToggled = (sType) => {
    EventBus.dispatch(oEvents.CLASS_AVAILABLE_LIST_VIEW_HANDLE_ITEM_COLLAPSED_TOGGLED, sType);
  };

  getSectionAvailablePropertyList = (sKey, aModels, sPropertyCollapsed) => {
    var oAvailablePropertyListView = {};
    oAvailablePropertyListView = aModels.length ?
                                 <DraggableListView
                                     model={aModels}/> : this.getNothingToDisplayView();

    return (
        <div className={"itemBody" + sPropertyCollapsed}>
          {oAvailablePropertyListView}
          {this.getLoadMoreView(sKey, aModels)}
        </div>
    )
  };

  getPropertyCollapsedStatusMap = ()=>{
    return this.props.propertyCollapsedStatusMap
  };

  getTotalCountByKey = (sKey) => {
    let oEntitiesTotalCounts = this.props.entitiesTotalCounts;
    return oEntitiesTotalCounts[sKey] || 0;
  };

  getSectionAvailableLists = (sKey, sLabel, aModels) => {
    let iTotalCount = this.getTotalCountByKey(sKey);
    let sHeader = sLabel + " (" + aModels.length + "/" + iTotalCount + ")";
    var bIsPropertyCollapsed = this.getPropertyCollapsedStatusMap()[sKey];
    var sPropertyCollapsedClass = bIsPropertyCollapsed ? " collapsed" : "";
    var oAvailableListView = null;
    if (!bIsPropertyCollapsed) {
      oAvailableListView = this.getSectionAvailablePropertyList(sKey, aModels, sPropertyCollapsedClass);
    }

    return (
        <div className="sectionAvailableListWrapper" key={sKey}>
          <div className={"expandCollapseIcon" + sPropertyCollapsedClass}
               onClick={this.handlePropertyCollapsedToggled.bind(this, sKey)}></div>
          <div className="availableDragElHeader">{sHeader}</div>
          {oAvailableListView}
        </div>
    )
  };

  //#Overriden Do no Remove Rohan
  getAttributeList = () =>{
    let oAvailableLists = this.getAvailableList();
    return oAvailableLists.attributeModels;
  };

  getTagsList = () => {
    let oAvailableLists = this.getAvailableList();
    return oAvailableLists.tagModels;
  };

  getTaxonomiesList  = () =>{
    let oAvailableLists = this.getAvailableList();
    return oAvailableLists.taxonomyModels;
  };

  getEntitySearchText = () =>{
    return this.props.entitySearchText;
  };

  getRelationshipModels = () =>{
    var oAvailableLists = this.getAvailableList();
    return oAvailableLists.relationshipModels;
  };

  getEntitiesToShowMap = () => {
    return {
      attributes: true,
      tags: true,
      taxonomies: true
    }
  };

  getSearchBarDOM = () => {
    return (
        <div className="sectionAvailableSearchBar">
          <input className="sectionAvailableSearchBarInput"
                 ref={this.sectionAvailableSearchBarInput}
                 onKeyPress={this.handleSearchKeyPressed}
                 onBlur={this.handleSearchInputChanged}/>
          <div className="sectionAvailableSearchBarIcon"></div>
          {this.getEntitySearchText() ?
           <div className="sectionAvailableClearSearch" onClick={this.handleSearch.bind(this, "")}></div> : null}
        </div>)
  };

  getDraggableElementsNew = () => {
    var aSectionAvailableLists = [];
    let oEntitiesToShowMap = this.getEntitiesToShowMap();

    oEntitiesToShowMap["attributes"] && aSectionAvailableLists.push(this.getSectionAvailableLists("attributes", getTranslation().ATTRIBUTES, this.getAttributeList()));
    oEntitiesToShowMap["tags"] && aSectionAvailableLists.push(this.getSectionAvailableLists("tags", getTranslation().TAGS, this.getTagsList()));
    oEntitiesToShowMap["taxonomies"] && aSectionAvailableLists.push(this.getSectionAvailableLists("taxonomies", getTranslation().CLASSIFICATIONS, this.getTaxonomiesList()));


    return aSectionAvailableLists;
  };

  render() {
    return (
        <div className="classAvailableListSearchViewContainer">
          {this.getSearchBarDOM()}
          <div className="sectionAvailableLists">
            {this.getDraggableElementsNew()}
          </div>
        </div>
    );
  }
}

export const view = CoreClassAvailableListSearchView;
export const events = oEvents;
