import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ReactDOM from 'react-dom';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import CommonUtils from '../../commonmodule/util/common-utils';
import TooltipView from './../tooltipview/tooltip-view';
import ResizeSensor from 'css-element-queries/src/ResizeSensor';
import { view as GridRowView } from './grid-row-view';
import { view as PaginationView } from './../paginationview/pagination-view';
import { view as SimpleSearchBarView } from './../simplesearchbarview/simple-search-bar-view';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import { view as ConfigFileUploadButtonView } from '../../viewlibraries/configfileuploadbuttonview/config-file-upload-button-view';
import { view as NothingFoundView } from '../../viewlibraries/nothingfoundview/nothing-found-view';
import { view as GridFilterView } from './grid-filter-view';
import GridViewImportExportLabels from '../tack/grid-view-import-export-label-dictionary';
import { view as FilterView } from './grid-filter-summary-view';
import {view as ColumnOrganizerDialogView} from "../columnorganizerview/column-organizer-dialog-view";
import {view as ImageFitToContainerView} from "../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
import ViewLibraryUtils from '../utils/view-library-utils';
const IS_FIREFOX = typeof InstallTrigger !== 'undefined';

const oEvents = {
  GRID_VIEW_SELECT_CLICKED: "grid_view_select_clicked",
  GRID_VIEW_ACTION_ITEM_CLICKED: "grid_view_action_item_clicked",
  GRID_VIEW_COLUMN_ACTION_ITEM_CLICKED: "grid_view_column_action_item_clicked",
  GRID_VIEW_DELETE_BUTTON_CLICKED: "grid_view_delete_button_clicked",
  GRID_VIEW_CREATE_BUTTON_CLICKED: "grid_view_create_button_clicked",
  GRID_VIEW_SORT_BUTTON_CLICKED: "grid_view_sort_button_clicked",
  GRID_VIEW_PAGINATION_CHANGED: "grid_view_pagination_changed",
  GRID_VIEW_SEARCH_TEXT_CHANGED: "grid_view_search_text_changed",
  GRID_VIEW_SAVE_BUTTON_CLICKED: "grid_view_save_button_clicked",
  GRID_VIEW_DISCARD_BUTTON_CLICKED: "grid_view_discard_button_clicked",
  GRID_VIEW_COLUMN_HEADER_CLICKED: "grid_view_column_header_clicked",
  GRID_VIEW_EMPTY_CELL_CLICKED: "grid_view_empty_cell_clicked",
  GRID_VIEW_RESET_ACTIVE_CELL_DETAILS: "grid_view_reset_active_cell_details",
  GRID_VIEW_EXPORT_BUTTON_CLICKED: "grid_view_export_button_clicked",
  GRID_VIEW_SHOW_EXPORT_STATUS_BUTTON_CLICKED: "grid_view_show_export_status_button_clicked",
  GRID_VIEW_REFRESH_BUTTON_CLICKED: "grid_view_refresh_button_clicked",
  GRID_VIEW_MANAGE_ENTITY_DIALOG_BUTTON_CLICKED: "grid_view_handle_manage_entity_dialog_button_clicked",
  GRID_VIEW_SAVEAS_BUTTON_CLICKED: "grid_view_saveas_button_clicked",
  GRID_VIEW_DOWNLOAD_BUTTON_CLICKED: "grid_view_download_button_clicked",
  GRID_VIEW_FILTER_BUTTON_CLICKED: "grid_view_filter_button_clicked",
  GRID_VIEW_ORGANIZE_COLUMNS_BUTTON_CLICKED: "grid_view_organize_columns_button_clicked",
  GRID_VIEW_COLUMN_RESIZER_MODE: "grid_view_column_resizer_mode",
};

const oPropTypes = {

  context: ReactPropTypes.string.isRequired, //denotes where the grid view is being used
  skeleton: ReactPropTypes.shape({
    fixedColumns: ReactPropTypes.arrayOf(
        ReactPropTypes.shape({
          id: ReactPropTypes.string,
          label: ReactPropTypes.string,
          type: ReactPropTypes.string,
          width: ReactPropTypes.number
        })
    ),
    scrollableColumns: ReactPropTypes.arrayOf(
        ReactPropTypes.shape({
          id: ReactPropTypes.string,
          label: ReactPropTypes.string,
          type: ReactPropTypes.string,
          isVisible: ReactPropTypes.bool,
          width: ReactPropTypes.number
        })
    ),
    actionItems: ReactPropTypes.arrayOf(
        ReactPropTypes.shape({
          id: ReactPropTypes.string,
          label: ReactPropTypes.string,
          class: ReactPropTypes.string
        })
    ),
    selectedContentIds: ReactPropTypes.array,
    showSubHeader: ReactPropTypes.bool
  }).isRequired,

  data: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        isExpanded: ReactPropTypes.bool,
        actionItemsToShow: ReactPropTypes.arrayOf(ReactPropTypes.string),
        children: ReactPropTypes.array, //todo: Recursive check for child object
        properties: ReactPropTypes.object,
      })
  ), //list of all the contents (pre-processed)

  visualData: ReactPropTypes.object,
  hierarchical: ReactPropTypes.bool,
  hideActiveCellSelection: ReactPropTypes.bool,
  paginationData: ReactPropTypes.shape({
    from: ReactPropTypes.number,
    pageSize: ReactPropTypes.number
  }),
  searchText: ReactPropTypes.string,
  sortBy: ReactPropTypes.string,
  sortOrder: ReactPropTypes.string,
  totalItems: ReactPropTypes.number,
  totalNestedItems: ReactPropTypes.number,
  isGridDataDirty: ReactPropTypes.bool,
  showCheckboxColumn: ReactPropTypes.bool,
  showSortButton: ReactPropTypes.bool,
  showContentCheckBox: ReactPropTypes.bool,
  showIsRowDisabled: ReactPropTypes.bool,
  customMessage: ReactPropTypes.string,
  showAddOnView: ReactPropTypes.bool,
  addOnView: ReactPropTypes.object,
  hideSelectAll: ReactPropTypes.bool,
  hideUpperSection: ReactPropTypes.bool,
  hideLowerSection: ReactPropTypes.bool,
  disableCreate: ReactPropTypes.bool,
  activeContentId: ReactPropTypes.string,
  activeCellDetails: ReactPropTypes.shape({
    dataId: ReactPropTypes.string,
    columnId: ReactPropTypes.string
  }),
  customActionView: ReactPropTypes.object,
  disableDeleteButton: ReactPropTypes.bool,
  gridViewSearchBarPlaceHolder: ReactPropTypes.string,
  enableImportExportButton: ReactPropTypes.bool,
  disableImportButton : ReactPropTypes.bool,
  enableShowExportStatusButton : ReactPropTypes.bool,
  disableRefresh: ReactPropTypes.bool,
  enableOrganizeColumnButton: ReactPropTypes.bool,
  customScrollbarViewData: ReactPropTypes.object,
  isSingleSelect: ReactPropTypes.bool,
  //Handlers
  selectClickedHandler: ReactPropTypes.func,
  selectAllClickedHandler: ReactPropTypes.func,
  deleteButtonClickedHandler: ReactPropTypes.func,
  createButtonClickedHandler: ReactPropTypes.func,
  sortButtonClickedHandler: ReactPropTypes.func,
  gridPaginationChangedHandler: ReactPropTypes.func,
  searchTextChangedHandler: ReactPropTypes.func,
  saveButtonClickedHandler: ReactPropTypes.func,
  discardButtonClickedHandler: ReactPropTypes.func,
  columnHeaderClickedHandler: ReactPropTypes.func,
  exportButtonClickedHandler: ReactPropTypes.func,
  refreshButtonClickedHandler: ReactPropTypes.func,
  columnOrganizerButtonClickHandler: ReactPropTypes.func,
  saveAsButtonClickedHandler: ReactPropTypes.func,
  emptyCellClickedHandler: ReactPropTypes.func,
  showExportStatusButtonClickedHandler: ReactPropTypes.func,
  //Child Handlers
  gridPropertyViewHandlers: ReactPropTypes.object,
  enableManageEntityButton: ReactPropTypes.bool,
  enableCopyButton: ReactPropTypes.bool,
  duplicateCode: ReactPropTypes.array,
  duplicateLabel: ReactPropTypes.array,
  hideSearchBar: ReactPropTypes.bool,
  displayShadedRows: ReactPropTypes.bool,
  filterData: ReactPropTypes.object,
  enableDownload: ReactPropTypes.bool,
  enableFilterButton: ReactPropTypes.bool,
  showFilterView: ReactPropTypes.bool,
  selectedColumns: ReactPropTypes.array,
  isColumnOrganizerDialogOpen: ReactPropTypes.bool,
  columnOrganizerData: ReactPropTypes.object,
  disableColumnOrganizer: ReactPropTypes.bool,
  disableResizable: ReactPropTypes.bool,
};
/**
 * @class GridView - use to Display GridView in Application.
 * @memberOf Views
 * @property {string} context -  context name.
 * @property {custom} skeleton -  string of id , label, type and width of gridView.
 * @property {array} [data] -  array of data of gridview.
 * @property {object} [visualData] -  object of visualData.
 * @property {bool} [hierarchical] -  boolean for grid have hierarchical or not.
 * @property {bool} [hideActiveCellSelection] -  boolean for grid have hideActiveCellSelection or not.
 * @property {custom} [paginationData] - pagination data value in number.
 * @property {string} [searchText] - search text in string.
 * @property {string} [sortBy] - sortBy text in string.
 * @property {string} [sortOrder] - sortOrder in string.
 * @property {number} [totalItems] - totalItems in number.
 * @property {number} [totalNestedItems] - totalNestedItems in number.
 * @property {bool} [isGridDataDirty] -  boolean for grid have isGridDataDirty or not.
 * @property {bool} [showCheckboxColumn] -  boolean for grid have showCheckboxColumn or not.
 * @property {bool} [showAddOnView] -  boolean for grid have showAddOnView or not.
 * @property {object} [addOnView] -  object of addOnView.
 * @property {bool} [hideUpperSection] -  boolean for grid hideUpperSection or not.
 * @property {bool} [hideLowerSection] -  boolean for grid hideLowerSection or not.
 * @property {bool} [disableCreate] -  boolean for grid have disableCreate or not.
 * @property {string} [activeContentId] - string of activeContentId of gridView.
 * @property {custom} [activeCellDetails] - ctiveCellDetails in strings.
 * @property {object} [customActionView] -  object of customActionView.
 * @property {bool} [disableDeleteButton] -  boolean for grid have disableDeleteButton or not.
 * @property {string} [gridViewSearchBarPlaceHolder] -  string of gridViewSearchBarPlaceHolder.
 * @property {bool} [enableImportExportButton] -  boolean for grid have enableImportExportButton or not.
 * @property {bool} [disableRefresh] -  boolean for grid have disableRefresh or not.
 * @property {bool} [isSingleSelect] - boolean for hiding select all on grid header
 * @property {bool} [autoSaveGrid] - To autosave the row
 */

// @CS.SafeComponent
class GridView extends React.Component {

  constructor(props) {
    super(props);

    this.iActionItemWidth = 36;
    this.minWidthForResizableDOM = 70;
    this.iResizableWidth = "";
    this.oSelectedGridCellDOM = null;
    this.bAdjustPingPong = false;
    this.state = {
      iExcessWhitespace: 0,
      isResizable: false,
    };

    this.rowCounter = 0;
    this.iActionItemsCellWidth = 0;

    this.leftFixedSection = React.createRef();
    this.centralScrollableSection = React.createRef();
    this.scrollableSheet = React.createRef();
    this.rightFixedSection = React.createRef();
    this.gridViewUpperSection = React.createRef();
    this.gridViewTableContainer = React.createRef();
    this.gridView = null;
    this.addOnViewContainer = React.createRef();
    this.centralScrollableSectionSibling = React.createRef();
    this.scrollableHeader = React.createRef();
    this.isScrolling = false;
    this.rowsHeightToProcess = [];
    this.showBorderToResizableDOM = false;
  }


  /*componentWillMount() {
    this.rowCounter = 0;
    this.iActionItemsCellWidth = 0;
  }*/

  /*componentWillReceiveProps(oNextProps) {
    this.handleNoContentOnLastPageScenario(oNextProps.data, oNextProps.paginationData);
    if (this.props.paginationData && this.props.paginationData.from !== oNextProps.paginationData.from) {
      var oGridViewTableContainer = this.gridViewTableContainer.current;
      $(oGridViewTableContainer).animate({
        scrollTop: 0
      });
    }
  }*/

  /*static getDerivedStateFromProps(oNextProps) {
    this.handleNoContentOnLastPageScenario(oNextProps.data, oNextProps.paginationData);
    if (this.props.paginationData && this.props.paginationData.from !== oNextProps.paginationData.from) {
      var oGridViewTableContainer = this.refs["gridViewTableContainer"];
      $(oGridViewTableContainer).animate({
        scrollTop: 0
      });
    }
  }*/

  /*componentWillUpdate() {
    this.rowCounter = 0;
    this.iActionItemsCellWidth = 0;
  }*/

  componentDidMount() {
    this.adjustExtraWhitespace();
    this.adjustAddOnViewWidth();
    // this.calculateScrollbarVisibility();
    this.attachResizeEventListener();
    this.setScrollPositions();
  }

  componentDidUpdate(oPreProps, oPreState) {
    this.rowCounter = 0;
    this.iActionItemsCellWidth = 0;
    this.handleNoContentOnLastPageScenario(this.props.data, this.props.paginationData);
    /*
      // Commenting following code as vertical scrolling is now handled by custom-scrollbar-view
      if (oPreProps.paginationData && oPreProps.paginationData.from !== this.props.paginationData.from) {
      let oGridViewTableContainer = this.gridViewTableContainer.current;
      $(oGridViewTableContainer).animate({
        scrollTop: 0
      });
    }*/

    this.adjustExtraWhitespace();
    this.adjustAddOnViewWidth();
    // this.calculateScrollbarVisibility();

  }

  stateChanged = () => {
    this.setState({});
  };

  setScrollPositions = () => {
    let oCentralScrollableSectionSibling = this.centralScrollableSectionSibling.current;
    let oCentralScrollableSection = this.centralScrollableSection.current;
    if(oCentralScrollableSectionSibling && oCentralScrollableSection) {
      oCentralScrollableSectionSibling.scrollLeft = 0;

      oCentralScrollableSection.scrollLeft = 0;
    }
  };

  adjustExtraWhitespace =()=> {
    let aScrollableColumns = this.getSelectedColumnsFromColumnOrganizer();
    this.bAdjustPingPong = !this.bAdjustPingPong; //TODO: Temporary for June release
    if (!CS.isEmpty(aScrollableColumns)) {
      let iScrollableSectionWidth = Math.round(parseFloat(getComputedStyle(this.centralScrollableSection.current).width));
      let iScrollableSheetWidth = iScrollableSectionWidth;
      if(this.scrollableSheet && this.scrollableSheet.current){
        iScrollableSheetWidth = Math.round(parseFloat(getComputedStyle(this.scrollableSheet.current).width));
      }
      if (iScrollableSheetWidth < iScrollableSectionWidth) {
        let iExtraWhitespace = this.bAdjustPingPong ? iScrollableSectionWidth - iScrollableSheetWidth : 0;
        this.setState({
          iExcessWhitespace: iExtraWhitespace
        });
      } else if (this.state.iExcessWhitespace && (iScrollableSheetWidth > iScrollableSectionWidth)) {
        this.setState({
          iExcessWhitespace: 0
        });
      }
      /** iScrollableSheetWidth === iScrollableSectionWidth is the stable state */
    }
  }

/*
  calculateScrollbarVisibility =()=> {
    var oCentralScrollableSection = this.centralScrollableSection.current;
    var oLeftFixedSection = this.leftFixedSection.current;
    var oRightFixedSection = this.rightFixedSection.current;
    var oGridViewUpperSection = this.gridViewUpperSection.current;
    var oGridViewTableContainer = this.gridViewTableContainer.current;

    if (!oCentralScrollableSection || oCentralScrollableSection.scrollWidth <= oCentralScrollableSection.clientWidth) {
      oLeftFixedSection && (oLeftFixedSection.style.overflowX = "hidden");
      oCentralScrollableSection && (oCentralScrollableSection.style.overflowX = "hidden");
      oRightFixedSection && (oRightFixedSection.style.overflowX = "hidden");
    } else {
      oLeftFixedSection && (oLeftFixedSection.style.overflowX = "scroll");
      oCentralScrollableSection && (oCentralScrollableSection.style.overflowX = "scroll");
      oRightFixedSection && (oRightFixedSection.style.overflowX = "scroll");
    }

    if (!oGridViewTableContainer || oGridViewTableContainer.scrollHeight <= oGridViewTableContainer.clientHeight) {
      oGridViewUpperSection && (oGridViewUpperSection.style.overflowY = "hidden");
      // oGridViewTableContainer && (oGridViewTableContainer.style.overflowY = "hidden");
    } else {
      oGridViewUpperSection && (oGridViewUpperSection.style.overflowY = "hidden");
      // oGridViewTableContainer && (oGridViewTableContainer.style.overflowY = "scroll");
    }
  }
*/

  handleGridCellClicked =(oEvent)=> {
    this.makeGridCellActive(oEvent.currentTarget);
  }

  makeGridCellActive =(oDOM)=> {
    if(!this.props.hideActiveCellSelection) {
      if (this.oSelectedGridCellDOM) {
        //reset previous cell first
        this.oSelectedGridCellDOM.classList.remove("activeCell");
      }
      //add class to cell
      if (oDOM) {
        this.oSelectedGridCellDOM = oDOM;
        this.oSelectedGridCellDOM.classList.add("activeCell");
      }
    }
  }

  makeGridCellActiveFromActiveCellDetails =(oDOM)=> {
    if (oDOM) {
      this.makeGridCellActive(oDOM);
      EventBus.dispatch(oEvents.GRID_VIEW_RESET_ACTIVE_CELL_DETAILS);
    }
  }

  handleSelectClicked = (sContentId) => {
    if (CS.isFunction(this.props.selectClickedHandler)) {
      this.props.selectClickedHandler([sContentId]);
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_SELECT_CLICKED, [sContentId], false, this.props.context);
    }
  };

  handleSelectAllClicked =(bSelectAll)=> {
    let aData = this.props.data;
    let bSelectAllClicked = true;
    let aSelectedContentIds = [];
    if (bSelectAll) {
      CS.forEach(aData, function (oContent) {
        aSelectedContentIds.push(oContent.id);
        if (oContent.children) {
          aSelectedContentIds = aSelectedContentIds.concat(CS.map(oContent.children, "id"));
        }
      });
    } else {
      /** handled select all option disabled in grid view*/
      CS.forEach(aData, function (oContent) {
        if (oContent.hasOwnProperty("isDisabled") && !oContent.isDisabled) {
          aSelectedContentIds.push(oContent.id);
          if (oContent.children) {
            aSelectedContentIds = aSelectedContentIds.concat(CS.map(oContent.children, "id"));
          }
        }
        !CS.isEmpty(aSelectedContentIds) && (bSelectAllClicked = false);
      });
    };
    if (CS.isFunction(this.props.selectAllClickedHandler)) {
      this.props.selectAllClickedHandler(aSelectedContentIds, bSelectAllClicked);
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_SELECT_CLICKED, aSelectedContentIds, bSelectAllClicked, this.props.context);
    }
  }

  handleDeleteButtonClicked = () => {
    if (CS.isFunction(this.props.deleteButtonClickedHandler)) {
      this.props.deleteButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_DELETE_BUTTON_CLICKED, this.props.context);
    }
  };

  handleFilterButtonClicked = () => {
    EventBus.dispatch(oEvents.GRID_VIEW_FILTER_BUTTON_CLICKED,  !this.props.showFilterView, this.props.context);
  };

  handleManageEntityClicked =(sSelectedId)=> {
    EventBus.dispatch(oEvents.GRID_VIEW_MANAGE_ENTITY_DIALOG_BUTTON_CLICKED, this.props.context);
  }

  handleCreateButtonClicked = () => {
    if (CS.isFunction(this.props.createButtonClickedHandler)) {
      this.props.createButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_CREATE_BUTTON_CLICKED, this.props.context);
    }
  }

  handleSortButtonClicked = () => {
    if (CS.isFunction(this.props.sortButtonClickedHandler)) {
      this.props.sortButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_SORT_BUTTON_CLICKED, this.props.context);
    }
  }

  handleDownloadButtonClicked = () => {
    EventBus.dispatch(oEvents.GRID_VIEW_DOWNLOAD_BUTTON_CLICKED, this.props.context);
  };

  handleGridPaginationChanged = (oNewPaginationData) => {
    if (CS.isFunction(this.props.gridPaginationChangedHandler)) {
      this.props.gridPaginationChangedHandler(oNewPaginationData);
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_PAGINATION_CHANGED, oNewPaginationData, this.props.context);
    }
  }

  handleSearchTextChanged = (sSearchText) => {
    let sContext = this.props.context;
    if (CS.isFunction(this.props.searchTextChangedHandler)) {
      this.props.searchTextChangedHandler(sSearchText, sContext);
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_SEARCH_TEXT_CHANGED, sSearchText, sContext);
    }
  }

  handleSaveButtonClicked = () => {
    if (CS.isFunction(this.props.saveButtonClickedHandler)) {
      this.props.saveButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_SAVE_BUTTON_CLICKED, this.props.context);
    }
  }

  handleDiscardButtonClicked = () => {
    if (CS.isFunction(this.props.discardButtonClickedHandler)) {
      this.props.discardButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_DISCARD_BUTTON_CLICKED, this.props.context);
    }
  }

  handleColumnHeaderClicked = (sColumnId) => {
    if (CS.isFunction(this.props.columnHeaderClickedHandler)) {
      this.props.columnHeaderClickedHandler(sColumnId);
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_COLUMN_HEADER_CLICKED, sColumnId, this.props.context);
    }
  }

  handleExportButtonClicked = () => {
    if (CS.isFunction(this.props.exportButtonClickedHandler)) {
      this.props.exportButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_EXPORT_BUTTON_CLICKED, this.props.context);
    }
  }

  handleShowExportStatusButtonClicked = () => {
    if (CS.isFunction(this.props.showExportStatusButtonClickedHandler)) {
      this.props.showExportStatusButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_SHOW_EXPORT_STATUS_BUTTON_CLICKED, this.props.context);
    }
  };

  handleRefreshButtonClicked = () => {
    if (CS.isFunction(this.props.refreshButtonClickedHandler)) {
      this.props.refreshButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_REFRESH_BUTTON_CLICKED, this.props.context)
    }
  }

  handleOrganizeColumnsButtonClicked = () => {
    if (this.props.columnOrganizerData && CS.isFunction(this.props.columnOrganizerData.columnOrganizerButtonHandler)) {
      this.props.columnOrganizerData.columnOrganizerButtonHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_ORGANIZE_COLUMNS_BUTTON_CLICKED, this.props.tableContextId, this.props.context)
    }
  };

  handleSaveAsButtonClicked = () => {
    if (CS.isFunction(this.props.saveAsButtonClickedHandler)) {
      this.props.saveAsButtonClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_SAVEAS_BUTTON_CLICKED, this.props.context)
    }
  }

  handleEmptyCellClicked = (oCellData, oEvent) => {
    if (CS.isFunction(this.props.emptyCellClickedHandler)) {
      this.props.emptyCellClickedHandler();
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_EMPTY_CELL_CLICKED, oCellData);
    }
    this.handleGridCellClicked(oEvent);
  }

  handleNoContentOnLastPageScenario =(aData, oPaginationData)=> {
    if (oPaginationData && (aData.length === 0) && (oPaginationData.from)) { //todo: replace oPaginationData check with showPaginationSection prop after it is available
      var iNewFrom = oPaginationData.from - oPaginationData.pageSize;
      if (iNewFrom < 0) {
        iNewFrom = 0;
      }
      var oNewPaginationData = {
        from: iNewFrom,
        pageSize: oPaginationData.pageSize
      };
      this.handleGridPaginationChanged(oNewPaginationData);
    }
  }

  handleRowClicked =(iCurrentRowNumber, oEvent)=> {
    var oTargetElement = oEvent.currentTarget;
    //the latest resizeSensor will be in resizeSensor1, the previous one will be put into resizeSensor2 :
    if (this.resizeSensor1) {
      if (this.resizeSensor1.rowNumber == iCurrentRowNumber) { //if resizeSensor of the same row already exists, then don't do anything
        return;
      }
      if (this.resizeSensor2) {
        this.resizeSensor2.detach(); //if there exists a resizeSensor2 then detach it before pushing 1st sensor into it
        this.resizeSensor2 = null;
      }
      this.resizeSensor2 = this.resizeSensor1;
    }
    this.resizeSensor1 = new ResizeSensor(oTargetElement, CS.throttle(this.adjustRowHeight.bind(this, iCurrentRowNumber), 300));
    this.resizeSensor1.rowNumber = iCurrentRowNumber;
    this.rowNumber = iCurrentRowNumber;
  }

  adjustRowHeight =(iRowNumber)=> {
    // console.log("adjust height of row " + iRowNumber);
    var sClassName = "rowNumber" + iRowNumber;
    CommonUtils.applyStyleToDOMByClassNameOnGridViewMount(this.gridView, sClassName, "height", "");
  };

  handleRowOnLoad =(iCurrentRowNumber, oDOM)=> {
    if (oDOM) {
      oDOM = ReactDOM.findDOMNode(oDOM);
      var oBoundingClientRect = oDOM.getBoundingClientRect(); //needed to get accurate height including decimal values (misalignment issue in firefox)
      var iNewHeight = oBoundingClientRect.height;
      var sClassName = "rowNumber" + iCurrentRowNumber;

      if (this.gridView) {
        CommonUtils.applyStyleToDOMByClassNameOnGridViewMount(this.gridView, sClassName, "height", "");
      }
      else {
        this.rowsHeightToProcess.push([sClassName, "height", iNewHeight + "px"]);
      }
    }
  }

  handleActionItemClicked = (sActionItemId, sContentId) => {
    if (CS.isFunction(this.props.actionItemClickedHandler)) {
      this.props.actionItemClickedHandler(sActionItemId, sContentId);
    } else {
      EventBus.dispatch(oEvents.GRID_VIEW_ACTION_ITEM_CLICKED, sActionItemId, sContentId, this.props.context);
    }
  };

/*
  handleColumnActionItemClicked =(sActionItemId, sColumnId)=> {
    EventBus.dispatch(oEvents.GRID_VIEW_COLUMN_ACTION_ITEM_CLICKED, sActionItemId, sColumnId);
  }
*/

  adjustAddOnViewWidth =()=> {
    if (this.addOnViewContainer.current) {
      if (CS.isEmpty(this.props.skeleton)) {
        this.addOnViewContainer.current.style.width = "100%";
      } else {
        this.addOnViewContainer.current.style.width = (this.gridView.offsetWidth - this.gridViewTableContainer.current.offsetWidth) + "px";
      }
    }
  }

  getSelectOptionCell =(bIsHeader, sContentId, bIsDirty, bIsDisabled)=> {
    var oSkeleton = this.props.skeleton;
    var aSelectedContentIds = oSkeleton.selectedContentIds;
    var aData = this.props.data || []; //todo: okay?
    var fOnClickHandler = this.handleSelectClicked.bind(this, sContentId);
    let bShowSquareCheckBox = this.props.showContentCheckBox;
    var sContainerClass = "gridViewCell selectOption ";
    var sIconClass = "selectIcon ";
    if (bShowSquareCheckBox) {
      sIconClass = "contentCheckButtonIcon ";
    }

    var iTotalLength = this.props.totalNestedItems ? this.props.totalNestedItems : aData.length;

    if (bIsHeader) {
      sContainerClass += "header";
      var bSelectAll = true;
      if(aSelectedContentIds.length && aSelectedContentIds.length < iTotalLength) {
        sIconClass += "partiallySelected";
      }
      if (iTotalLength && (aSelectedContentIds.length == iTotalLength)) {
        sIconClass += "isSelected";
        bSelectAll = false;
      }

      /**
       * If row is Disabled and disableCount is same as totalCount then
       * selectaAll option is disabled.
       * @type {number}
       */
      let iDisabledCount = 0;
      CS.forEach(aData, function (oData) {
        oData.isDisabled && (iDisabledCount = iDisabledCount + 1);
      });
      fOnClickHandler = (iDisabledCount === iTotalLength) ? CS.noop :
          this.handleSelectAllClicked.bind(this, bSelectAll);
    } else if (aSelectedContentIds.includes(sContentId)) {
      sIconClass += "isSelected";
    }

    var oDirtyIndicator = bIsDirty ? <div className="dirtyIndicator"></div> : null;
    // if (bIsSelected) {
    //   sIconClass += "isSelected";
    // }

    if (!bIsDisabled) {
      return (
          <div className={sContainerClass} key={sContentId + "_select"} onClick={fOnClickHandler}>
            {oDirtyIndicator}
            <div className={sIconClass}></div>
          </div>
      );
    } else {
      return (
          <div className={sContainerClass}>
          </div>
      );
    }
  }

  getActionItemsCell =(bIsHeader, scrollbarWidth)=> {
    var aActionItems = this.props.skeleton.actionItems;
    if (CS.isEmpty(aActionItems)) {
      return null;
    }
    var iNumberOfActionItems = this.props.skeleton.maxActionItemsToShow || aActionItems.length;
    var oActionItemCellStyle = {};

    if (bIsHeader) {
      this.iActionItemsCellWidth = iNumberOfActionItems * this.iActionItemWidth + 10 + 1; //10px for padding + 1px for zoom adjustment buffer
      oActionItemCellStyle.width = this.iActionItemsCellWidth + scrollbarWidth + "px";
      return (
          <div key="actionItemsHeader" className="gridViewCell header actionItemsContainer" style={oActionItemCellStyle}></div>
      );
    }

    return null;
  }

  adjustRowHeight = () => {
    this.setState({});
  };

  getGridRowView =(iRowNumber, sGridRowClass, oContent, onRowClick, sRowType, aColumnData, onRowMount)=> {
    var _this = this;
    var __props = _this.props;
    var oSkeleton = _this.props.skeleton;
    var aSelectedContentIds = oSkeleton.selectedContentIds;
    var aActionItems = oSkeleton.actionItems;
    var aData = __props.data || [];
    var iTotalDataCount = aData.length;
    var oGridViewVisualData = __props.visualData || {};
    var oRowVisualData = oGridViewVisualData[oContent.id];

    if(CS.includes(aSelectedContentIds,oContent.id)) {
      sGridRowClass = sGridRowClass + " " + "selected";
    }
    let oReferencedAttributes = __props.referencedAttributes;


    return (
        <GridRowView key={oContent.id + "_" + sRowType}
                     context={this.props.context}
                     gridPropertyViewHandlers={this.props.gridPropertyViewHandlers}

                     referencedAttributes = {oReferencedAttributes}
                     className={sGridRowClass}
                     onRowClick={onRowClick}
                     ref={onRowMount || CS.noop}
                     rowNumber={iRowNumber}
                     rowType={sRowType}
                     rowData={oContent}
                     columnData={aColumnData}
                     activeContentId={__props.activeContentId}
                     showCheckboxColumn={__props.showCheckboxColumn}
                     showContentCheckBox={__props.showContentCheckBox}
                     showContentRadioButton={__props.showContentRadioButton}
                     showIsRowDisabled={__props.showIsRowDisabled}

                     selectedContentIds={aSelectedContentIds}
                     totalDataCount={iTotalDataCount}
                     totalNestedItems={__props.totalNestedItems}

                     rowVisualData={oRowVisualData}
                     activeCellDetails={__props.activeCellDetails}
                     hierarchical={__props.hierarchical}
                     isGridDataDirty={__props.isGridDataDirty}
                     actionItems={aActionItems}

                     handleEmptyCellClicked={_this.handleEmptyCellClicked}
                     makeGridCellActiveFromActiveCellDetails={_this.makeGridCellActiveFromActiveCellDetails}
                     handleSelectClicked={_this.handleSelectClicked}
                     handleSelectAllClicked={_this.handleSelectAllClicked}
                     handleGridCellClicked={_this.handleGridCellClicked}
                     getDividedWidth={_this.getDividedWidth}
                     getActionItemCellWidth={_this.getActionItemCellWidth}
                     setActionItemCellWidth={_this.setActionItemCellWidth}
                     getActionItemWidth={_this.getActionItemWidth}
                     handleActionItemClicked={_this.handleActionItemClicked}
                     duplicateCode={this.props.duplicateCode}
                     duplicateLabel={this.props.duplicateLabel}
                     gridWFValidationErrorList= {this.props.gridWFValidationErrorList}
                     resizedColumnId={this.props.resizedColumnId}
                     resizableWidth={this.iResizableWidth}
                     hideIconUpload={this.props.hideIconUpload}
                     adjustRowHeight={this.adjustRowHeight}
                     tableContextId={this.props.tableContextId}
        />);

  }

  recursiveFunctionToGenerateContentRow =(oContent,  aLeft, aCentral, aRight)=> {
    var _this = this;
    var oSkeleton = this.props.skeleton;
    var oGridViewVisualData = this.props.visualData || {};
    var aFixedColumns = oSkeleton.fixedColumns;
    let aScrollableColumns = this.getSelectedColumnsFromColumnOrganizer();

    var iCurrentRowNumber = this.rowCounter;
    var sGridRowClass = "gridViewRow " + "rowNumber" + iCurrentRowNumber;

    aLeft.push(
        this.getGridRowView(iCurrentRowNumber, sGridRowClass, oContent, CS.noop, "leftFixed", aFixedColumns));

    var fHandleRowOnLoad = this.handleRowOnLoad.bind(this, iCurrentRowNumber);

    aCentral.push(
        this.getGridRowView(iCurrentRowNumber, sGridRowClass, oContent, this.handleRowClicked, "scrollable",
            aScrollableColumns, fHandleRowOnLoad)
    );

    aRight.push(
        this.getGridRowView(iCurrentRowNumber, sGridRowClass, oContent, CS.noop, "action", []));

    this.rowCounter++;

    var oVisualData = oGridViewVisualData[oContent.id];
    if (oVisualData && oVisualData.isExpanded && !CS.isEmpty(oContent.children)) {
      CS.forEach(oContent.children, function (oChildContent) {
        _this.recursiveFunctionToGenerateContentRow(oChildContent,  aLeft, aCentral, aRight);
      });
    }
  }

  calculateDistributedWidthToAdd =()=> {
    this.dividedWidth = 0;
    this.lastDividedWidth = 0;

    var oSkeleton = this.props.skeleton;
    var iExcessWhitespace = this.state.iExcessWhitespace || 0;
    let aScrollableColumns = this.getSelectedColumnsFromColumnOrganizer();
    aScrollableColumns = CS.reject(aScrollableColumns, {isVisible: false});
    if (CS.isEmpty(aScrollableColumns) || !iExcessWhitespace) {
      return 0; // avoid divide by 0
    }

    if(this.props.resizedColumnId) {
        let iDividedWidth = Math.trunc(iExcessWhitespace / (aScrollableColumns.length - 1));
        this.dividedWidth = iDividedWidth;
        this.lastDividedWidth = iExcessWhitespace - (iDividedWidth * (aScrollableColumns.length - 2));
      }
    else {
      //scrollable columns related code:
      let iDividedWidth = Math.trunc(iExcessWhitespace / aScrollableColumns.length);

      this.dividedWidth = iDividedWidth;
      this.lastDividedWidth = iExcessWhitespace - (iDividedWidth * (aScrollableColumns.length - 1));
    }
  }

  forceUpdateComponent = () => {
    this.forceUpdate();
  }

  attachResizeEventListener = () => {
    if(this.gridView){
      let _this = this;
      new ResizeSensor(this.gridView, CS.debounce(_this.forceUpdateComponent, 50));
    }
  };

  getDividedWidth =(bIsLastColumn)=> {
    return bIsLastColumn ? this.lastDividedWidth : this.dividedWidth;
  };

  setBorderAndHeightToResizer = (sCellClass, oCellStyle, sContextId) => {
    if (this.showBorderToResizableDOM) {
      let oPanelDOM = document.getElementsByClassName(sCellClass);
      let aArrNew = Array.prototype.slice.call(oPanelDOM);
      let oGridMiddleSection = document.getElementsByClassName("gridViewMiddleSection");
      let oGridMiddleSectionDOM = CS.find(oGridMiddleSection, function (obj) {
        if(CS.isNotEmpty(obj.getElementsByClassName(sContextId))){
          return obj;
        }
      });
      let oStyle =  {
        borderRight: "1px dashed #9D9D9D",
        width: oCellStyle.width,
        height: oGridMiddleSectionDOM.offsetHeight + "px",
        position: 'absolute',
        zIndex: '1',
      };
      CS.assign(aArrNew[0].style, oStyle);
    }
  };

  onMouseDown = (sId, sCellClass, iCellWidth, oCellStyle) => {
    this.showBorderToResizableDOM = true;
    setTimeout(this.setBorderAndHeightToResizer.bind(this, sCellClass, oCellStyle, this.props.tableContextId), 250);
    window.onmouseup = this.onResizeButtonClick.bind(this, sId, sCellClass, iCellWidth);
  };

  onResizeButtonClick = (sId, sCellClass, iCellWidth) => {
    if(this.showBorderToResizableDOM) {
      let sTableContextId = this.props.tableContextId;
      let sOldWidth = iCellWidth + "";
      let oPanelDOM = document.getElementsByClassName(sCellClass);
      let aArrNew = Array.prototype.slice.call(oPanelDOM);
      let sWidthWithoutPX = aArrNew[0].style.width.split("p")[0] || aArrNew[0].offsetWidth;
      this.showBorderToResizableDOM = false;
      let oStyleReset = {
        borderRight: "none",
        height: "100%",
        width: iCellWidth,
        position: "absolute",
        zIndex: "inherit"
      };
      CS.assign(aArrNew[0].style, oStyleReset);
      window.onmouseup = null;
      if (sOldWidth !== sWidthWithoutPX) {
        if (sWidthWithoutPX < this.minWidthForResizableDOM) {
          sWidthWithoutPX = this.minWidthForResizableDOM;
        }
        this.iResizableWidth = sWidthWithoutPX;

        EventBus.dispatch(oEvents.GRID_VIEW_COLUMN_RESIZER_MODE, sWidthWithoutPX, sId, sTableContextId, this.props.context);
      }
    }
  };

  getResizableDOM (oColumn, sCellClass, iIndex, oCellStyle, sResizeClassName, iCellWidth) {
    return (
        <div key={oColumn.id + iIndex}
             className={sResizeClassName}
             style={{width:oCellStyle.width, minWidth: 70 + "px", cursor:oCellStyle.cursor || null}}
             onMouseDown={this.onMouseDown.bind(this, oColumn.id, sResizeClassName, iCellWidth, oCellStyle)}
        >
        </div>
    )
  }

  destroyExistingResizableDOM = () => {
    let oGridViewDOM = this.gridView;
    let aResizableDOM = oGridViewDOM && oGridViewDOM.getElementsByClassName('resize');
    if(CS.isEmpty(aResizableDOM) || aResizableDOM.length > 1) {
      return null;
    } else {
      aResizableDOM[0].remove();
    }
  };

  getHeaderCell =(oColumn, iIndex, oTableData,  bIsFixedColumn, bIsLastColumn, sClassName, bIsResizableEnable)=> {
    let fOnClickHandler = null;
    var sCellClass = "gridViewCell header " + sClassName + iIndex;
    let sResizeClassName =  bIsResizableEnable ? "gridViewCell resize header " + iIndex + " " +this.props.tableContextId || "" : sCellClass;
    sResizeClassName += bIsResizableEnable ? " resizableHeader" : "";
    var iCellWidth = oColumn.width;
    var oSortIndicator = null;
    var sSortIndicatorLabel = "";
    let oSkeleton = this.props.skeleton;
    let oCellStyle = {};
    if (bIsFixedColumn) {
      //fixed columns related code:
      oTableData.leftFixedSectionWidth += oColumn.width;
    }
    else {
      iCellWidth += this.getDividedWidth(bIsLastColumn);
      oTableData.scrollableSheetWidth += oColumn.width;

      /** if resizable width is present then set it else take it from skeleton**/
      oCellStyle.minWidth = 70 + "px";
    }
    oCellStyle = this.props.resizedColumnId === oColumn.id ? {width: oColumn.width} : {width: iCellWidth + "px"};

    if (oColumn.isSortable) {
      let sSortIndicatorClass = "sortIndicator ";
      if (oColumn.id === this.props.sortBy) {
        oCellStyle.backgroundColor = "#d8efff";
        oCellStyle.boxShadow = "inset 0 0 0 2px #5ca4e2";
        if (this.props.sortOrder === "asc") {
          sSortIndicatorLabel = getTranslation().ASCENDING;
          sSortIndicatorClass += "asc";
        } else if (this.props.sortOrder === "desc") {
          sSortIndicatorLabel = getTranslation().DESCENDING;
          sSortIndicatorClass += "desc";
        }
      } else {
        sSortIndicatorClass += "isSortable";
        sSortIndicatorLabel = getTranslation().SORT_BY;
      }
      oCellStyle.cursor = "pointer";
      oSortIndicator = (
          <TooltipView label={sSortIndicatorLabel}>
            <div className={sSortIndicatorClass}></div>
          </TooltipView>
      );
      fOnClickHandler = this.handleColumnHeaderClicked.bind(this, oColumn.id);
    } else {
      fOnClickHandler = null;
    }


    let sColumnSubLabel = oColumn.subLabel ? oColumn.subLabel : "";
    let oSubLabelView = oSkeleton.showSubHeader ? (<div className="headerSubLabel" title={sColumnSubLabel}>{sColumnSubLabel}</div>) : null;
    let oFilterView = null;
    if(this.props.showFilterView) {

      oFilterView = oColumn.isFilterable ? (<GridFilterView
          filterColumnId={oColumn.id}
          gridFilterType={oColumn.filterType}
          filterData={oColumn.filterData}
      />) : (<div className={"gridFilterContainer"}/>)
    }
    let sIconSrc =  ViewLibraryUtils.getIconUrl(oColumn.iconKey);
    let bIsEnableIconDOM = oColumn.showDefaultIcon;
    let oIconDOM = bIsEnableIconDOM ? (<div className="customIcon"><ImageFitToContainerView imageSrc={sIconSrc}/></div>) : null;

    return (
        <div key={oColumn.id + iIndex}
             className={"headerWrapper"}>
          {bIsResizableEnable ? this.getResizableDOM(oColumn, sCellClass, iIndex, oCellStyle, sResizeClassName, iCellWidth) : (bIsFixedColumn ? null : this.destroyExistingResizableDOM())}
          <div key={"inner" + oColumn.id + iIndex}
               className={sCellClass}
               style={oCellStyle}
               onClick={fOnClickHandler}
          >
            {oIconDOM}
            <div className="headerLabel" title={CS.getLabelOrCode(oColumn)}>{CS.getLabelOrCode(oColumn)}</div>
            {oSubLabelView}
            {oSortIndicator}
            {oFilterView}
          </div>
        </div>
    );
  }

  getActionItemWidth =()=> {
    return this.iActionItemWidth;
  }

  getActionItemCellWidth =()=> {
    return this.iActionItemsCellWidth;
  }

  setActionItemCellWidth =(iCellWidth)=> {
    this.iActionItemsCellWidth = iCellWidth;
  }

  generateTableHeaders =(oTableData, scrollbarWidth)=> {
    var _this = this;
    let oProps = this.props;
    var oSkeleton = this.props.skeleton;
    var aFixedColumns = oSkeleton.fixedColumns;
    var aScrollableColumns = this.getSelectedColumnsFromColumnOrganizer();
    let bShowSubHeader = oSkeleton.showSubHeader;
    let bIsResizableEnable = aScrollableColumns && aScrollableColumns.length > 1;
    let enableResizableDOM = !this.props.disableResizable &&  bIsResizableEnable;

    var aLeftFixedSectionCells = [];
    var aScrollableSectionCells = [];
    var aRightFixedSectionCells = [];

    if (_this.props.showCheckboxColumn) {
      aLeftFixedSectionCells.push(this.getSelectOptionCell(true, "", "", this.props.isSingleSelect || this.props.hideSelectAll));
      oTableData.leftFixedSectionWidth += 40;
    };

    //left fixed
    CS.forEach(aFixedColumns, function (oColumn, iIndex) {
      if(!oColumn.hideColumn){
        aLeftFixedSectionCells.push(_this.getHeaderCell(oColumn, iIndex, oTableData, true, '', "headerLeft ", false));
      }
    });


    //scrollable
    CS.forEach(aScrollableColumns, function (oColumn, iIndex) {
      var iIsLastColumn = (iIndex === (aScrollableColumns.length - 1));
      aScrollableSectionCells.push(_this.getHeaderCell(oColumn, iIndex, oTableData, false, iIsLastColumn, "headerMiddleContainer ", enableResizableDOM));
    });

    oTableData.scrollableSheetWidth += _this.state.iExcessWhitespace;

    aRightFixedSectionCells.push(this.getActionItemsCell(true, scrollbarWidth));

    let sHeaderWrapper = "headerRowWrapper";
    if (this.props.showFilterView) {
      sHeaderWrapper += " withFilterView";
    }


    if(oSkeleton.showSubHeader) {
      sHeaderWrapper += " withSubHeader";
    }

    let iLeftWidth = oTableData.leftFixedSectionWidth + 2 +'px';
    let iAdjustWidth = 2;//2px border
    let oCentralScrollableSectionStyle = {
      maxWidth: "calc(100% - " + (oTableData.leftFixedSectionWidth + this.iActionItemsCellWidth + 4 + scrollbarWidth) + "px)",
    };
    let oRightFixedSectionStyle = {};
    if (this.props.skeleton.actionItems) {
      oRightFixedSectionStyle.width = (this.iActionItemsCellWidth + 2) + "px";
    }

    return(
        <div className={sHeaderWrapper} style={{minWidth: (oTableData.leftFixedSectionWidth + this.iActionItemsCellWidth + 200) + "px"}}>
          <div key="headerRowLeft" className="gridViewRow gridViewHeaderRow headerRowLeft" style={{width: iLeftWidth}}>
            {aLeftFixedSectionCells}
          </div>
          <div key="headerRowScrollable" className="gridViewRow gridViewHeaderRow headerRowScrollable" style={oCentralScrollableSectionStyle} ref={this.scrollableHeader}>
            <div className="headerMiddle" style={{width: oTableData.scrollableSheetWidth + "px"}} ref={this.scrollableSheet} >
              {aScrollableSectionCells}
            </div>
          </div>
          {CS.isNotEmpty(this.props.skeleton.actionItems) ?
              <div key="headerRowRight" className="gridViewRow gridViewHeaderRow headerRowRight"
                   style={oRightFixedSectionStyle}>
                {aRightFixedSectionCells}
              </div> : <div style={{width:scrollbarWidth + iAdjustWidth}} className={"DummyScrollbarDOM"}></div>}

        </div>
    )
  };

  handleScrollableSectionScrolled = () => {
    if(!this.isScrolling) {
      this.isScrolling = true;
      let oCentralScrollableSection = this.centralScrollableSection.current;
      oCentralScrollableSection.scrollLeft = this.centralScrollableSectionSibling.current.scrollLeft;
    }else {
      this.isScrolling = false;
    }
  };

  handleCentralScrollableSectionScrolled = (oEvent) => {
    let oCentralScrollableSection = this.centralScrollableSection.current;

    if(!this.isScrolling) {
      this.isScrolling = true;

      if (!CS.isEmpty(this.centralScrollableSectionSibling.current)) {
        let oCentralScrollableSectionSibling = this.centralScrollableSectionSibling.current;
        oCentralScrollableSectionSibling.scrollLeft = oCentralScrollableSection.scrollLeft;
      }
    } else {
      this.isScrolling = false;
    }

    let oContainerDOM = oEvent.currentTarget;
    let iScrollLeft = oContainerDOM.scrollLeft;
    let oScrollableHeader = this.scrollableHeader.current;

    CommonUtils.applyStyleToDOMByClassNameOnGridViewMount(oScrollableHeader, "headerMiddle", "left", -(iScrollLeft) + "px");
  };

  getTableView =()=> {

    if (CS.isEmpty(this.props.skeleton)) {
      return null;
    }
    this.calculateDistributedWidthToAdd();

    var _this = this;
    var oTableData = {
      leftFixedSectionRows : [],
      scrollableSectionRows : [],
      rightFixedSectionRows : [],
      leftFixedSectionWidth : 0,
      scrollableSheetWidth : 0
    };

    let oScrollableDOM = document.querySelector('.gridScrollWrapper');
    let iVerticalScrollbarWidth = oScrollableDOM && oScrollableDOM.offsetWidth - oScrollableDOM.clientWidth;

    let oHeaderView =  this.generateTableHeaders(oTableData, iVerticalScrollbarWidth);

    var aData = this.props.data;
    let aLeft = [];
    let aCentral = [];
    let aRight = [];

    CS.forEach(aData, function (oContent) {
      _this.recursiveFunctionToGenerateContentRow(oContent, aLeft, aCentral, aRight);
    });

    oTableData.leftFixedSectionRows.push(
        <div key="leftBody" className="leftBody" >{aLeft}</div>
    );
    oTableData.scrollableSectionRows.push(
        <div key="scrollBody" className="scrollBody" >{aCentral}</div>
    );
    oTableData.rightFixedSectionRows.push(
        <div key="rightBody" className="rightBody" >{aRight}</div>
    );

    let iLeftFixedSectionBorderWidth = 2;
    let iRightFixedSectionBorderWidth = 2;

    let iLeftFixedSectionTotalWidth = iLeftFixedSectionBorderWidth + oTableData.leftFixedSectionWidth;
    let iRightFixedSectionTotalWidth = iRightFixedSectionBorderWidth + this.iActionItemsCellWidth;
    let iCentralScrollableSectionWidth = "calc(100% - " + (iLeftFixedSectionTotalWidth + iRightFixedSectionTotalWidth) + "px)";
    let iDecimalError = 1;

    var oGridViewTableContainerStyle = {};

    var oLeftFixedSectionStyle = {
      width: (iLeftFixedSectionTotalWidth) + "px",
    };

    var oRightFixedSectionStyle = {};//2px border
    if (this.props.skeleton.actionItems) {
      oRightFixedSectionStyle.width = iRightFixedSectionTotalWidth + "px";
    }

    var oCentralScrollableSectionStyle = {
      width: iCentralScrollableSectionWidth
    };

    var oScrollableSheetStyle = {
      width: oTableData.scrollableSheetWidth + "px"
    };

    /**Remove below code in case of any bug */
    var aScrollableColumns = this.getSelectedColumnsFromColumnOrganizer();
    if(CS.isEmpty(aScrollableColumns)){
      oScrollableSheetStyle.width = "100%";
    }
    /**Remove above code in case of any bug */

    let oData = this.props.customScrollbarViewData;
    let oScrollbarStyle = {
      minHeight:  oData ? oData.minHeight : "auto",
      MaxHeight: oData ? oData.maxHeight : "100%",
      overflowY: 'scroll',
      overflowX: 'hidden'
    };

    let oScrollableSheetStyleForSibling = {
      width: oTableData.scrollableSheetWidth - iDecimalError + "px",
      height: "inherit"
    };

    let oCentralScrollableSectionStyleForSibling = {
      width: iCentralScrollableSectionWidth ,
      height: "inherit",
    };

    let oLeftFixedSectionStyleForSibling = {
      width: iLeftFixedSectionTotalWidth + "px",
      height: "inherit",
    };

    let oRightFixedSectionStyleForSibling = {
      height: "inherit",
    };
    if (this.props.skeleton.actionItems) {
      oRightFixedSectionStyleForSibling.width = iRightFixedSectionTotalWidth + "px";
    }
    // issue fixed: dummy sibling scrollbar DOM is not visible properly.
    const iHeightForSibling = 6;
    let oGridViewTableSiblingStyle = {
      height: iVerticalScrollbarWidth + iHeightForSibling,
      width: "calc(100% - " + iVerticalScrollbarWidth + "px)"
    };

    let oScrollableSiblingSection = <div className="gridViewTableContainerSibling" style={oGridViewTableSiblingStyle}>
      <div className="siblingSection leftFixedSectionSibling" style={oLeftFixedSectionStyleForSibling}>
        <div className="leftFixedSectionScroller"></div>
      </div>
      <div className="siblingSection centralScrollableSectionSibling" ref={this.centralScrollableSectionSibling}
           style={oCentralScrollableSectionStyleForSibling}
           onScroll={this.handleScrollableSectionScrolled}>
        <div className="centralScrollableSectionScroller" style={oScrollableSheetStyleForSibling}></div>
      </div>
      <div className="siblingSection rightFixedSectionSibling" style={oRightFixedSectionStyleForSibling}>
        <div className="rightFixedSectionScroller"></div>
      </div>
    </div>

    var oGridViewTableStyle = {
      minWidth: (oTableData.leftFixedSectionWidth + this.iActionItemsCellWidth + 150) + "px",
    };

    if (this.props.showAddOnView) {
      oGridViewTableContainerStyle.width = "auto";
      oGridViewTableContainerStyle.float = "left";
      oGridViewTableStyle.minWidth = "0";
    }

    let oScrollabelSection = <div className="scrollableSheet" ref={this.scrollableSheet}
                                  style={oScrollableSheetStyle}>{oTableData.scrollableSectionRows}
    </div>;

    let sClassNameForeGridViewContainer = "gridViewTableContainer ";

    if (aLeft.length < 1){
      oCentralScrollableSectionStyle.height = '100%';
      if(this.props.searchText !== "") {
        oScrollabelSection = <NothingFoundView message={this.props.customMessage || getTranslation().NO_MATCHES_FOUND} style={oScrollableSheetStyle}/>
      }
      if (this.props.searchText === "") {
        oScrollabelSection = <NothingFoundView message={this.props.customMessage || getTranslation().NOTHING_FOUND} style={oScrollableSheetStyle}/>
      }
      sClassNameForeGridViewContainer +=" withNothingFountContainer";
      oScrollableSiblingSection = null;
      oGridViewTableStyle.marginBottom = "0";
    }


    let sLeftFixedSectionClassName = "leftFixedSection";
    let sCentralScrollableSectionClassName = "centralScrollableSection";
    let sRightFixedSectionClassName = "rightFixedSection";


    let oStyle = {};

    if(IS_FIREFOX) {
      oStyle.marginBottom = "-22px"
    }

    return (
        <div className={"gridViewMiddleSectionWrapper"} style = {oStyle}>
          {oHeaderView}
          <div className={"gridScrollWrapper"} style = {oScrollbarStyle}>
            <div className= {sClassNameForeGridViewContainer} ref={this.gridViewTableContainer}
                 style={oGridViewTableContainerStyle}>
              <div className="gridViewTable" style={oGridViewTableStyle}>
                <div className={sLeftFixedSectionClassName} ref={this.leftFixedSection}
                     style={oLeftFixedSectionStyle}>{oTableData.leftFixedSectionRows}</div>
                <div className={sCentralScrollableSectionClassName} ref={this.centralScrollableSection}
                     style={oCentralScrollableSectionStyle}
                     onScroll={this.handleCentralScrollableSectionScrolled}
                >
                  {oScrollabelSection}
                </div>
                <div className={sRightFixedSectionClassName} ref={this.rightFixedSection}
                     style={oRightFixedSectionStyle}>{oTableData.rightFixedSectionRows}</div>
              </div>
            </div>
          </div>
          {oScrollableSiblingSection}
        </div>
    );
  }

  getPaginationViewDOM =()=> {
    var oPaginationData = this.props.paginationData;
    var aData = this.props.data;

    if (oPaginationData) {
      return (
          <PaginationView onChange={this.handleGridPaginationChanged}
                          from={oPaginationData.from}
                          pageSize={oPaginationData.pageSize}
                          currentPageItems={aData.length}
                          totalItems={this.props.totalItems}/>
      );
    }
    else {
      return null;
    }
  }

  getAddOnViewDOM =()=> {
    if (this.props.showAddOnView && this.props.addOnView) {
      return (
          <div className="addOnViewContainer" ref={this.addOnViewContainer}>
            {this.props.addOnView}
          </div>
      );
    }
  }

  getGridUpperSection =()=> {
    var oSaveButtonDOM = null;
    var oDiscardButtonDOM = null;
    var oCreateButtonDOM = null;
    var oSortButtonDOM = null;
    var oOnClickSave = this.handleSaveButtonClicked;
    var oOnClickDiscard = this.handleDiscardButtonClicked;
    let {lightToolbarIcons: bLightToolbarIcons} = this.props;
    if (this.props.isGridDataDirty){
      // let oStyle = {
      //   height: "30px",
      //   lineHeight: "30px",
      //   margin: "0 5px",
      //   backgroundColor: "#ededed"
      // };

      oSaveButtonDOM = (
          <div className="saveOrDiscardButton save">
            <CustomMaterialButtonView
                label={getTranslation().SAVE}
                isRaisedButton={true}
                isDisabled={false}
                onButtonClick={oOnClickSave}/>
          </div>
      );
      oDiscardButtonDOM = (
          <div className="saveOrDiscardButton">
            <CustomMaterialButtonView
                label={getTranslation().DISCARD}
                isRaisedButton={false}
                isDisabled={false}
                onButtonClick={oOnClickDiscard}
                // style={oStyle}
            />
          </div>
      );
    }

    if (this.props.showSortButton) {
      oSortButtonDOM = (
          <TooltipView label={getTranslation().REARRANGE_SEQUENCE}>
            <div className="headerButton reorder" onClick={this.handleSortButtonClicked}>
              <div className={`headerButtonIcon reorder ${!!bLightToolbarIcons && "light"}`}></div>
            </div>
          </TooltipView>
      );
    }

    if (!this.props.disableCreate) {
      oCreateButtonDOM = (
          <TooltipView label={getTranslation().CREATE}>
            <div className="headerButton create" onClick={this.handleCreateButtonClicked}>
              <div className={`headerButtonIcon create ${!!bLightToolbarIcons && "light"}`}></div>
            </div>
          </TooltipView>
      );
    }

    let oDownloadButtonDom = null;
    if(this.props.enableDownload && this.props.totalItems > 0){
      oDownloadButtonDom = (
          <TooltipView label={getTranslation().DOWNLOAD}>
            <div className="headerButton download" onClick={this.handleDownloadButtonClicked}>
              <div className="headerButtonIcon download"></div>
            </div>
          </TooltipView>
      );
    }

    let oDeleteButtonDom = null;
    if (!this.props.disableDeleteButton) {
      oDeleteButtonDom = (<TooltipView label={getTranslation().DELETE}>
        <div className="headerButton delete" onClick={this.handleDeleteButtonClicked}>
          <div className={`headerButtonIcon delete ${!!bLightToolbarIcons && "light"}`}></div>
        </div>
      </TooltipView>)
    }

    let oFilterButtonDom = null;
    if (this.props.enableFilterButton) {
      let sClassName = this.props.showFilterView ? "headerButtonIcon filter selected" : "headerButtonIcon filter";

      oFilterButtonDom = (<TooltipView label={getTranslation().FILTER}>
        <div className="headerButton filter" onClick={this.handleFilterButtonClicked}>
          <div className={sClassName}></div>
        </div>
      </TooltipView>)
    }

    let oImportFileButtonDom = null;
    let oExportButtonDom = null;

    let oImportExportLabels = !CS.isEmpty(GridViewImportExportLabels[this.props.context]) ? GridViewImportExportLabels[this.props.context] : {};
    let sExportLabel = !CS.isEmpty(oImportExportLabels) ? oImportExportLabels.EXPORT : getTranslation().EXPORT;
    let sImportLabel = !CS.isEmpty(oImportExportLabels) ? oImportExportLabels.IMPORT : getTranslation().IMPORT;

    if (this.props.enableImportExportButton) {
      oImportFileButtonDom = this.props.disableImportButton ? null :
                             (<ConfigFileUploadButtonView label={sImportLabel}
                                                          context={this.props.context}
                                                          lightToolbarIcon={!!bLightToolbarIcons}/>);
      oExportButtonDom = (<TooltipView label={sExportLabel}>
        <div className="headerButton export" onClick={this.handleExportButtonClicked}>
          <div className={`headerButtonIcon export ${!!bLightToolbarIcons && "light"}`}>
          </div>
        </div>
      </TooltipView>);
    }

    let oShowExportStatusButtonDom = null;
    if (this.props.enableShowExportStatusButton) {
      oShowExportStatusButtonDom = (
          <TooltipView label={getTranslation().AUDIT_LOGS_EXPORT_STATUS}>
            <div className="headerButton exportStatus" onClick={this.handleShowExportStatusButtonClicked}>
              <div className={`headerButtonIcon exportStatus ${!!bLightToolbarIcons && "light"}`}></div>
            </div>
          </TooltipView>
      )
    }

    let oRefreshButtonDOM = null;
    if(!this.props.disableRefresh) {
      oRefreshButtonDOM = (
          <TooltipView label={getTranslation().REFRESH}>
            <div className="headerButton refresh" onClick={this.handleRefreshButtonClicked}>
              <div className={`headerButtonIcon refresh ${!!bLightToolbarIcons && "light"}`}></div>
            </div>
          </TooltipView>
      );
    };

    let oColumnOrganizerButtonDOM = null;
    if(!this.props.disableColumnOrganizer) {
      oColumnOrganizerButtonDOM = (
          <TooltipView label={getTranslation().REARRANGE_COLUMN_SEQUENCE}>
            <div className="headerButton organizeGridEditProperties" onClick={this.handleOrganizeColumnsButtonClicked}>
              <div className={`headerButtonIcon organizeGridEditProperties ${!!bLightToolbarIcons && "light"}`}></div>
            </div>
          </TooltipView>
      );
    }

    let oManageEntityDOM = null;
    if (this.props.enableManageEntityButton && this.props.showCheckboxColumn) {
      oManageEntityDOM = (
          <TooltipView label={getTranslation().MANAGE_ENTITY_USAGE}>
            <div className="headerButton manageEntity" onClick={this.handleManageEntityClicked.bind(this,)}>
              <div className={`headerButtonIcon manageEntity ${!!bLightToolbarIcons && "light"}`}></div>
            </div>
          </TooltipView>
      );
    };

    let oCopyButtonDOM = null;
    if(this.props.enableCopyButton) {
      oCopyButtonDOM = (
          <TooltipView label={getTranslation().COPY}>
            <div className="headerButton copy" onClick={this.handleSaveAsButtonClicked}>
              <div className={`headerButtonIcon copy ${!!bLightToolbarIcons && "light"}`}></div>
            </div>
          </TooltipView>
      );
    };

    let oSearchBarView = null;
    if(!this.props.hideSearchBar){
      oSearchBarView = (
          <div className="searchBarContainer">
            <SimpleSearchBarView
                onBlur={this.handleSearchTextChanged}
                searchText={this.props.searchText}
                placeHolder={this.props.gridViewSearchBarPlaceHolder}
                isDisabled={this.props.disableSearch}
            />
          </div>
      );
    }

    var oCustomActionView = this.props.customActionView;

    return (
        <div className="gridViewUpperSection" ref={this.gridViewUpperSection}>
          {oSearchBarView}
          <div className="upperSectionButtonsContainer">
            {oDiscardButtonDOM}
            {oSaveButtonDOM}
            {oCopyButtonDOM}
            {oCustomActionView}
            {oSortButtonDOM}
            {oRefreshButtonDOM}
            {oColumnOrganizerButtonDOM}
            {oShowExportStatusButtonDom}
            {/*<button className="saveButton" onClick={this.handleSaveButtonClicked}>SAVE</button>
              <button className="saveButton" onClick={this.handleDiscardButtonClicked}>DISCARD</button>*/}
            {oImportFileButtonDom}
            {oExportButtonDom}
            {oDeleteButtonDom}
            {oManageEntityDOM}
            {oCreateButtonDOM}
            {oFilterButtonDom}
            {oDownloadButtonDom}
          </div>

        </div>
    );
  }

  getGridLowerSection =()=> {
    var oPaginationViewDOM = this.getPaginationViewDOM();
    var oPaginationData = this.props.paginationData;

    if(oPaginationData) {
      return (
          <div className="gridViewLowerSection">
            {oPaginationViewDOM}
          </div>
      );
    }
  }

  onGridViewMount = (oView) => {
    this.gridView = oView;
    CS.remove(this.rowsHeightToProcess, (aData) => {
      CommonUtils.applyStyleToDOMByClassNameOnGridViewMount(oView, ...aData);
      return true;
    });
  };

  getFilterView = () => {
    let {appliedFilterData, appliedFilterClonedData, availableFilterData, searchFilterData, isFilterExpanded, filterContext, horizontalSliderForAppliedFilter, isDirty} = this.props.filterData;
    return(
        <FilterView appliedFilterData={appliedFilterData}
                    availableFilterData={availableFilterData}
                    appliedFilterClonedData={appliedFilterClonedData}
                    isFilterExpanded={isFilterExpanded}
                    searchFilterData={searchFilterData}
                    showExpandFilterButton={!isFilterExpanded}
                    filterContext={filterContext}
                    horizontalSliderForAppliedFilter={horizontalSliderForAppliedFilter}
                    showApplyButton={isDirty}
                    showDefaultIconForAvailableAndAppliedFilter={this.props.filterData.showDefaultIconForAvailableAndAppliedFilter}
        />
    );
  };

  getSelectedColumnsFromColumnOrganizer = () => {
    let aScrollableColumns = this.props.skeleton.scrollableColumns;
    let aSelectedColumns = this.props.selectedColumns || [];
    aSelectedColumns = aSelectedColumns.clonedObject || aSelectedColumns;
    let aColumns = [];
    CS.forEach(aSelectedColumns, (oColumn) => {
      let oSelectedColumn = CS.find(aScrollableColumns, {id: oColumn.id});
      aColumns.push(oSelectedColumn);
    });

    return CS.isEmpty(aSelectedColumns) ? aScrollableColumns : aColumns;
  };

  getColumnOrganizerDialogView = () => {
    if(this.props.isColumnOrganizerDialogOpen){
      let bIsListDirty = CS.isNotEmpty(this.props.selectedColumns.clonedObject);
      let aSelectedColumns = this.getSelectedColumnsFromColumnOrganizer();
      let oColumnOrganizerData = this.props.columnOrganizerData || {};

      let aAvailableColumns = oColumnOrganizerData && oColumnOrganizerData.availableColumns ? oColumnOrganizerData.availableColumns : this.props.skeleton.scrollableColumns;

      let oEmptyMessageView = (
        <div className="messageWrapper">
          <div className="emptyPropertiesMessage">{getTranslation().NO_PROPERTIES_SELECTED}</div>
        </div>);
      return (
        <ColumnOrganizerDialogView totalColumns={aAvailableColumns}
                                   selectedColumns={aSelectedColumns}
                                   context={oColumnOrganizerData.context}
                                   isListDirty={bIsListDirty}
                                   disableSearch={!!oColumnOrganizerData.disableSearch}
                                   emptyMessage={oEmptyMessageView}
                                   preventEmptySelectedColumns={true}
                                   customRequestResponseInfo={oColumnOrganizerData.customRequestResponseInfo}
                                   tableContextId={this.props.tableContextId}
                                   klassInstanceId={oColumnOrganizerData.klassInstanceId}
                                   selectedColumnsLimit={oColumnOrganizerData.selectedColumnsLimit}
                                   bIsLoadMore={oColumnOrganizerData.bIsLoadMore}
        />);
    }

    return null;
  };


  render() {

    var oGridTableView = this.getTableView();
    let sClassNameForGridView = "gridView gridViewContainer ";
    if(this.props.hideUpperSection && this.props.hideLowerSection) {
      sClassNameForGridView += "withoutUpperAndLowerSection ";
      sClassNameForGridView += this.props.tableContextId && this.props.tableContextId || "";
    }
    let oColumnOrganizerDialogView = this.getColumnOrganizerDialogView();

    return (
        <div className= {sClassNameForGridView} ref={this.onGridViewMount}>

          {/*upper section - above grid table*/}
          {this.props.hideUpperSection ? null : this.getGridUpperSection()}
          {CS.isNotEmpty(this.props.filterData) && this.getFilterView()}
          <div className="gridViewMiddleSection">
            {/*main grid section*/}
            {oGridTableView}
            {/*add on view section*/}
            {this.getAddOnViewDOM()}
          </div>

          {/*lower section - below grid table*/}
          {this.props.hideLowerSection ? null : this.getGridLowerSection()}
          {oColumnOrganizerDialogView}
        </div>
    );
  }

}

GridView.propTypes = oPropTypes;

export const view = GridView;
export const events = oEvents;
