import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as NothingFoundView} from "./../../../../../viewlibraries/nothingfoundview/nothing-found-view";
import TooltipView from "../../../../../viewlibraries/tooltipview/tooltip-view";
import {getTranslations} from "../../../../../commonmodule/store/helper/translation-manager";
import {view as DragDropContextView} from "../../../../../viewlibraries/draggableDroppableView/drag-drop-context-view";
import alertify from "../../../../../commonmodule/store/custom-alertify-store";
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
import ViewLibraryUtils from "../../../../../viewlibraries/utils/view-library-utils";
import {view as TabLayoutView} from "../../../../../viewlibraries/tablayoutview/tab-layout-view";
import {view as SimpleSearchBarView} from "../../../../../viewlibraries/simplesearchbarview/simple-search-bar-view";

const oEvents = {
  PROPERTY_COLLECTION_DRAGGABLE_LIST_COLUMNS_SHUFFLE: "property_collection_draggable_list_columns_shuffle",
  PROPERTY_COLLECTION_DRAGGABLE_LIST_TAB_CLICKED: "property_collection_draggable_list_tab_clicked",
  PROPERTY_COLLECTION_DRAGGABLE_LIST_VIEW_LOAD_MORE: "property_collection_draggable_list_view_load_more",
  PROPERTY_COLLECTION_DRAGGABLE_LIST_REMOVE_BUTTON_CLICKED: "property_collection_draggable_list_remove_button_clicked",
  PROPERTY_COLLECTION_DRAGGABLE_LIST_SEARCH_TEXT_CHANGED: "property_collection_draggable_list_search_text_changed"
};

const oPropTypes = {
  leftListData: ReactPropTypes.array,
  rightListData: ReactPropTypes.array,
  activeTabId: ReactPropTypes.string,
  searchText: ReactPropTypes.string,
  emptyMessage: ReactPropTypes.string,
  activeClass: ReactPropTypes.string,
  showPropertyLoadMore: ReactPropTypes.bool
};

// @CS.SafeComponent
class PropertyCollectionDraggableListView extends React.Component {
  constructor (oProps) {
    super(oProps);

    this.state = {
      isDropDisabled: false
    };
  }

  static propTypes = oPropTypes;

  handleRemoveSelectedProperties = (sId, oEvent) => {
    oEvent.stopPropagation();
    EventBus.dispatch(oEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_REMOVE_BUTTON_CLICKED, sId);
  };

  handleListLoadMoreClicked = (sTabId) => {
    EventBus.dispatch(oEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_VIEW_LOAD_MORE, sTabId);
  };

  handleTabClicked = (sTabId) => {
    EventBus.dispatch(oEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_TAB_CLICKED, sTabId);
  };

  handleSearchTextChanged = (sSearchText) => {
    EventBus.dispatch(oEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_SEARCH_TEXT_CHANGED, sSearchText);
  };

  getLeftSidePropertiesData = (availableActiveListModel) => {
    return CS.map(availableActiveListModel, (oProperty) => {
      let gridEditLabelClass = "propertyLabelWrapper";
      let sLabelWrapperClassName = "propertyNewListWrapper ";
      let sIconURL = ViewLibraryUtils.getIconUrl(oProperty.properties.attributeData.iconKey);
      let bShouldHighlight = (CS.isNotEmpty(this.props.searchText) && this.shouldHighlightLabel(oProperty));
      let oPropertyIconDOM = (
          <ImageFitToContainerView imageSrc={sIconURL}/>
      );

      let oPropertyDOM = (
          <div className={sLabelWrapperClassName}>
            <div className="iconWrapper">
              {oPropertyIconDOM}
            </div>
            <div className={gridEditLabelClass}>
              <div className="propertyLabel">{CS.getLabelOrCode(oProperty.properties.attributeData)}</div>
              <TooltipView placement={"top"} label={oProperty.properties.code}>
                <div className="propertyCode">{oProperty.properties.code}</div>
              </TooltipView>
            </div>
          </div>
      );
      return {
        id: oProperty.id,
        label: oPropertyDOM,
        shouldHighlight: bShouldHighlight
      }
    });
  };

  getRightSideSelectedPropertyList = (availableListModel) => {
    return CS.map(availableListModel, (oProperty, iIndex) => {
      let gridEditLabelClass = "selectedPropertyLabelWrapper";
      let sLabelWrapperClassName = "selectedPropertyNewListWrapper ";
      let sIconURL = CS.isNotEmpty(oProperty.icon) ? ViewLibraryUtils.getIconUrl(oProperty.iconKey) : null;
      let bShouldHighlight = (CS.isNotEmpty(this.props.searchText) && this.shouldHighlightLabel(oProperty));

      let oPropertyIconDOM = (
          <ImageFitToContainerView imageSrc={sIconURL}/>
      );
      let sDefaultIconClassName = "propertyIcon ";
      sDefaultIconClassName += oProperty.type;

      let oPropertyDOM = (
          <div className={sLabelWrapperClassName}>
            <div className="selectedIconWrapper">
              <div className="propertySequence">{iIndex + 1}</div>
              {oPropertyIconDOM}
            </div>
            <div className={gridEditLabelClass}>
              <div className="labelWrapper">
                <div className="propertyLabel">{CS.getLabelOrCode(oProperty)}</div>
                <TooltipView placement={"top"} label={oProperty.code}>
                  <div className="propertyCode">{oProperty.code}</div>
                </TooltipView>
              </div>
              <div className={sDefaultIconClassName}></div>
              <TooltipView placement={"bottom"} label={getTranslation().DELETE}>
                <div className="removeIcon"
                     onClick={this.handleRemoveSelectedProperties.bind(this, oProperty.id)}>
                </div>
              </TooltipView>
            </div>
          </div>
      );
      return {
        id: oProperty.id,
        label: oPropertyDOM,
        shouldHighlight: bShouldHighlight
      }
    });
  };

  getLoadMoreDom = () => {
    return (
        <div className="PropertyCollectionViewLoadMore"
             onClick={this.handleListLoadMoreClicked.bind(this, this.props.activeTabId)}>
          {getTranslation().LOAD_MORE}
        </div>)
  };

  shouldHighlightLabel = (oProperty) => {
    let sSearchText = this.props.searchText;
    sSearchText = sSearchText && (sSearchText).toLowerCase();
    let sPropertyLabel = CS.getLabelOrCode(oProperty).toLowerCase();
    return !CS.isEmpty(sSearchText) && sPropertyLabel.includes(sSearchText);
  };

  getLeftElements = () => {
    var __props = this.props;
    let aPropertiesList = this.getLeftSidePropertiesData(__props.leftListData);
    let oNothingFoundMessageDOM = CS.isEmpty(aPropertiesList) ?
                                  <NothingFoundView message={getTranslation().NOTHING_FOUND}/> : null;
    let oLoadMoreDOM = __props.showPropertyLoadMore ? this.getLoadMoreDom() : null;

    let oHeaderTabs = (
        <TabLayoutView
            key={__props.activeTabId}
            tabList={__props.tabList}
            activeTabId={__props.activeTabId}
            addBorderToBody={false}
            onChange={this.handleTabClicked}>
        </TabLayoutView>
    )
    return {
      droppableId: "propertyList",
      items: aPropertiesList,
      nothingFoundMessageDOM: oNothingFoundMessageDOM,
      loadMoreDOM: oLoadMoreDOM,
      headerLabel: oHeaderTabs,
    }
  };

  getRightElements = () => {
    var __props = this.props;
    let aUpdatedSelectedColumns = this.getRightSideSelectedPropertyList(__props.rightListData, true);
    let oNothingFoundMessageDOM = CS.isEmpty(aUpdatedSelectedColumns) ?
                                  <NothingFoundView message={this.props.emptyMessage}/> : null;

    return {
      droppableId: "propertySequenceList",
      items: aUpdatedSelectedColumns,
      nothingFoundMessageDOM: oNothingFoundMessageDOM,
      headerLabel: getTranslations().SELECTED_PROPERTIES,
    }
  };

  onDragStart = result => {
    const {source: oSource} = result;
    if (oSource.droppableId === "propertySequenceList") {
      this.setState({
        isDropDisabled: false
      });
    } else {
      this.setState({
        isDropDisabled: true
      });
    }
  };

  onDragEnd = (oSource, oDestination, aDraggableIds) => {
    let aSelectedColumns = this.props.activeClass;
    if (!oDestination)
      return;
    if (oDestination.droppableId === "propertyList" && oSource.droppableId === "propertyList")
      return;
    if (oSource.droppableId === "propertySequenceList" && oDestination.droppableId === "propertyList")
      return;
    if (oDestination.droppableId === oSource.droppableId && oSource.index === oDestination.index)
      return;

    if (oSource.droppableId === "propertySequenceList" && oDestination.droppableId === "propertyList" && CS.size(aSelectedColumns) === CS.size(aDraggableIds)) {
      alertify.message(getTranslation().SELECTED_COLUMNS_SHOULD_NOT_BE_EMPTY);
      return;
    }
    EventBus.dispatch(oEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_COLUMNS_SHUFFLE, oSource, oDestination, aDraggableIds);

  };

  getDragDropContextView = () => {
    let aListData = [];
    aListData.push(this.getLeftElements(true));
    aListData.push(this.getRightElements(true));
    return (
        <DragDropContextView
            listData={aListData}
            onDragStart={this.onDragStart}
            onDragEnd={this.onDragEnd}
            enableMultiDrag={true}
        />
    )
  };

  getSearchBarView = () => {
    return (
        <div className="searchBarContainer">
          <SimpleSearchBarView
              onBlur={this.handleSearchTextChanged}
              searchText={this.props.searchText}
          />
        </div>
    );
  };

  getView = () => {
    return (
        <div className="propertiesCollectionDragabbleListContainer">
          <div className="propertyCollectionSearchBar">
            {this.getSearchBarView()}
          </div>
          <div className="propertyCollectionBody">
            {this.getDragDropContextView()}
          </div>
        </div>
    );
  };

  render () {
    return this.getView();
  }
}

export const view = PropertyCollectionDraggableListView;
export const events = oEvents;
