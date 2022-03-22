import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";
import {view as SimpleSearchBarView} from "../simplesearchbarview/simple-search-bar-view";
import CS from "../../libraries/cs";
import React from "react";
import ReactPropTypes from "prop-types";
import EventBus from "../../libraries/eventdispatcher/EventDispatcher";
import TooltipView from "./../tooltipview/tooltip-view";
import {view as DragDropContextView} from "../draggableDroppableView/drag-drop-context-view";
import {getTranslations} from '../../commonmodule/store/helper/translation-manager';
import copy from 'copy-to-clipboard';
import alertify from '../../commonmodule/store/custom-alertify-store';
import {view as NothingFoundView} from './../nothingfoundview/nothing-found-view';
import ColumnOrganizerStore from "./column-organizer-store";
import {view as CustomMaterialButtonView} from "../custommaterialbuttonview/custom-material-button-view";
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {
  HANDLE_COLUMN_ORGANIZER_VIEW_SAVE_BUTTON_CLICKED: "handle_column_organizer_view_save_button_clicked",
};

const oPropTypes = {
  columns: ReactPropTypes.array,
  searchText: ReactPropTypes.string,
  searchBarPlaceHolder: ReactPropTypes.string,
  disableSearch: ReactPropTypes.bool,
  selectedColumns: ReactPropTypes.array,
  emptyMessage: ReactPropTypes.object,
  sequenceListLimit: ReactPropTypes.string,
  onShuffleCallback: ReactPropTypes.func,
  shuffleHandler: ReactPropTypes.func,
  preventEmptySelectedColumns: ReactPropTypes.bool,
  customHeaderLabel: ReactPropTypes.string,
  hideHeader: ReactPropTypes.bool
};

// @CS.SafeComponent
class ColumnOrganizerView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);

    this.state = {
      isDropDisabled: false
    };

    this.initializeStore();
  }

  initializeStore = () => {
    let oStore = this.props.store || new ColumnOrganizerStore(this.props, this.stateChanged);
    this.state.store = oStore;
  };

  static getDerivedStateFromProps (oNextProps, oState) {
    if(oState.store){
      oState.store.initializeStore(oNextProps);
    }
  }

  stateChanged = () => {
    this.setState({});
  };

  handleCopyPropertyClicked = (sPropertyCode) => {
    copy(sPropertyCode) ? alertify.success(getTranslations().CODE_COPIED) : alertify.error(getTranslations().COPY_TO_CLIPBOARD_FAILED);
  };

  onDragEnd = (oSource, oDestination, aDraggableIds) => {
    let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);
    let aSelectedColumns = oColumnOrganizerProps.getSelectedOrganizedColumns().clonedObject || oColumnOrganizerProps.getSelectedOrganizedColumns();
    if (!oDestination)
      return;
    if (oDestination.droppableId === oSource.droppableId === "propertyList")
      return;
    if (oDestination.droppableId === oSource.droppableId && oSource.index === oDestination.index)
      return;

    if(this.props.preventEmptySelectedColumns && oSource.droppableId === "propertySequenceList" && oDestination.droppableId === "propertyList" && CS.size(aSelectedColumns) === CS.size(aDraggableIds)){
      alertify.message(getTranslation().SELECTED_COLUMNS_SHOULD_NOT_BE_EMPTY);
      return;
    }
    let oCallbackData = {};
    if (CS.isFunction(this.props.onShuffleCallback)) {
      oCallbackData.functionToExecute = this.props.onShuffleCallback;
    }
    if(CS.isFunction(this.props.shuffleHandler)){
      this.props.shuffleHandler(oSource, oDestination, aDraggableIds, oCallbackData, this.props.tableContextId);
    }
    else{
      this.state.store.handlePropertySequenceShuffled(oSource, oDestination, aDraggableIds, oCallbackData, this.props.tableContextId);
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

  handleSearchTextChanged = (sSearchText) => {
    if(CS.isFunction(this.props.searchHandler)){
      this.props.searchHandler(sSearchText);
    }
    else{
      this.state.store.handleSearchTextChanged(sSearchText, this.props.tableContextId);
    }
  };

  handleLoadMoreClicked = () => {
    this.state.store.handleLoadMoreClicked(this.props.tableContextId);
  };

  handleSaveGridEditButton = () => {
    this.state.store.saveActiveColumnOrganizerConfig();
    let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);
    let aSelectedOrganizerColumns = oColumnOrganizerProps.getSelectedOrganizedColumns();
    EventBus.dispatch(oEvents.HANDLE_COLUMN_ORGANIZER_VIEW_SAVE_BUTTON_CLICKED, this.props.context, aSelectedOrganizerColumns);
  };

  handleDiscardGridEditButton = () => {
    this.state.store.discardActiveColumnOrganizerConfig();
  };

  getLoadMoreDom = () => {
    let oLoadMoreDOM = (
          <div className="gridEditablePropertiesViewLoadMore" onClick={this.handleLoadMoreClicked}>
            {getTranslation().LOAD_MORE}
          </div>);
    return oLoadMoreDOM;
  };

  getSearchBarView = () => {
    let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);
    if(!this.props.disableSearch){
      return (
        <div className="searchBarContainer">
          <SimpleSearchBarView
            onBlur={this.handleSearchTextChanged}
            searchText={oColumnOrganizerProps.getSearchText()}
            placeHolder={this.props.searchBarPlaceHolder}
            isDisabled={this.props.disableSearch}
          />
        </div>
      );
    }
    return null;
  };

  getHeaderView = () => {
    if (!this.props.hideHeader) {
      let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);
      let oButtonView = null;
      if (CS.isNotEmpty(oColumnOrganizerProps.getSelectedOrganizedColumns().clonedObject)) {
        let oSaveButton = (<CustomMaterialButtonView
            label={getTranslation().SAVE}
            isRaisedButton={true}
            isDisabled={false}
            onButtonClick={this.handleSaveGridEditButton.bind(this, "save")}/>
        );
        let oDiscardButton = (<CustomMaterialButtonView
            label={getTranslation().CANCEL}
            isRaisedButton={false}
            isDisabled={false}
            onButtonClick={this.handleDiscardGridEditButton.bind(this, "discard")}/>
        );
        oButtonView = (<div className="gridEditViewButton">
          {oDiscardButton}
          {oSaveButton}
        </div>);
      }

      return (<div className="gridEditViewWrapperHeaderContainer">
        <div className="gridEditViewWrapperTitle">{this.props.customHeaderLabel}</div>
        {oButtonView}
      </div>);
    }
    return null;
  };

  shouldHighlightLabel = (oProperty) => {
    let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);
    let sSearchText = oColumnOrganizerProps.getSearchText();
    sSearchText = sSearchText && (sSearchText).toLowerCase();
    let sPropertyLabel = oProperty.label.toLowerCase();
    return !CS.isEmpty(sSearchText) && sPropertyLabel.includes(sSearchText);
  };

  getUpdatedPropertiesData = (aPropertiesList, bShowSequence, bHighlightSearchOccurrences, sClass) => {
    return CS.map(aPropertiesList, (oProperty, iIndex) => {
      let gridEditLabelClass = "gridEditLabel";
      let sLabelWrapperClassName = "propertySequenceLabelWrapper ";
      let bShouldHighlight = (bHighlightSearchOccurrences && this.shouldHighlightLabel(oProperty)) ;
      // adding class to remove extra space at bottom of last element for drop area when list is full i.e 25 elements
      let oCopyToClipboardDOM = CS.isNotEmpty(oProperty.code) ?
                                <TooltipView placement={"top"} label={getTranslations().COPY_PROPERTY_CODE}>
                                  <div className={"copyToClipboardButton"}
                                       onClick={this.handleCopyPropertyClicked.bind(this, oProperty.code)}/>
                                </TooltipView> : null;

      let oPropertyIconDOM = CS.isNotEmpty(oProperty.iconClassName) ?
                             <div className={"propertyIcon " + oProperty.iconClassName}/> : null;

      let sImageSrc = ViewUtils.getIconUrl(oProperty.iconKey);
      let bIsEnableIconDOM = oProperty.showDefaultIcon;
      let oIconDOM = bIsEnableIconDOM ? (<div className="customIcon"><ImageFitToContainerView imageSrc={sImageSrc}/></div>) : null;

      let oPropertyDOM = (
          <div className={sLabelWrapperClassName}>
            <div className="gridEditIcon">
              {bShowSequence ? <div className="propertySequence">{iIndex + 1}</div> : null}
              {oIconDOM}
            </div>
            <div className={gridEditLabelClass}>
              <div className="propertyGridLabelWrapper">
                <div className="propertyLabel">{CS.getLabelOrCode(oProperty)}</div>
                <TooltipView placement={"top"} label={oProperty.code}>
                  <div className="propertyCode">{oProperty.code}</div>
                </TooltipView>
              </div>
              {oCopyToClipboardDOM}
              {oPropertyIconDOM}
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


  getLeftSectionData = () => {
    let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);
    let aPropertiesList = this.getUpdatedPropertiesData(oColumnOrganizerProps.getHiddenColumns(), false, true);
    let oNothingFoundMessageDOM = CS.isEmpty(aPropertiesList) ? <NothingFoundView message={getTranslation().NOTHING_FOUND}/> : null;
    let  bIsLoadMore = oColumnOrganizerProps.getIsLoadMore();
    let oLoadMoreDOM = bIsLoadMore  ? this.getLoadMoreDom() : null;


    return {
      droppableId: "propertyList",
      items: aPropertiesList,
      nothingFoundMessageDOM: oNothingFoundMessageDOM,
      headerLabel: getTranslation().PROPERTIES,
      loadMoreDOM: oLoadMoreDOM,
      isDropDisabled: this.state.isDropDisabled,
    }
  };

  getRightSectionData = () => {
    let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);
    let aSelectedColumns = oColumnOrganizerProps.getSelectedOrganizedColumns().clonedObject || oColumnOrganizerProps.getSelectedOrganizedColumns();
    let aUpdatedSelectedColumns = this.getUpdatedPropertiesData(aSelectedColumns, true, true, "lastDropabbleElement");
    let oNothingFoundMessageDOM = CS.isEmpty(aUpdatedSelectedColumns) ?
                                  <NothingFoundView message={this.props.emptyMessage}/> : null;

    return {
      droppableId: "propertySequenceList",
      items: aUpdatedSelectedColumns,
      nothingFoundMessageDOM: oNothingFoundMessageDOM,
      headerLabel: getTranslations().SELECTED_PROPERTIES,
    }
  };


  getDragDropContextView = () => {
    let aListData = [];
    aListData.push(this.getLeftSectionData());
    aListData.push(this.getRightSectionData());

    return (
        <DragDropContextView
            listData={aListData}
            context={"gridEditConfiguration"}
            onDragStart={this.onDragStart}
            onDragEnd={this.onDragEnd}
            enableMultiDrag={true}
        />
    )
  };

  getView = () => {
    return (
        <div className="gridEditablePropertiesConfigContainer">
          {this.getHeaderView()}
          <div className="gridEditableView">
            <div className="gridEditableHeader">
              {this.getSearchBarView()}
            </div>
            <div className="gridEditableBody">
              {this.getDragDropContextView()}
            </div>
          </div>
        </div>
    );
  };

  render () {
    return this.getView();
  }
}

export const view = ColumnOrganizerView;
export const events = oEvents;
