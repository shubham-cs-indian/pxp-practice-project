import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import Loader from 'halogen/PulseLoader';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as TableView } from './../../../../../viewlibraries/tableview/table-view';
import {view as GridView} from './../../../../../viewlibraries/gridview/grid-view';
import {view as NothingFoundView} from './../../../../../viewlibraries/nothingfoundview/nothing-found-view'
import { view as TableOperationsView } from './../../../../../viewlibraries/tableview/table-operations-view';
import { view as AttributeFilterView } from './uom-table-filter-wrapper-view';
import { view as VariantDialogView } from './variant-dialog-view';
import { view as PaperView } from '../../../../../viewlibraries/paperview/paper-view';
import PaperViewModel from './../../../../../viewlibraries/paperview/model/paper-model';
import ViewUtils from './utils/view-utils';
import ContentScreenConstants from "../store/model/content-screen-constants";
import ScreenModeUtils from "../store/helper/screen-mode-utils";
import FullScreenViewObj from './../../../../../viewlibraries/fullscreenview/fullscreen-view';
import oFilterPropType from "../tack/filter-prop-type-constants";
import ContentUtils from "../store/helper/content-utils";
import ToolTip from "../../../../../viewlibraries/tooltipview/tooltip-view";
let getTranslation = ScreenModeUtils.getTranslationDictionary;
const FullScreenView = FullScreenViewObj.view;

const oEvents = {
  UOM_VIEW_TABLE_CREATE_ROW_BUTTON_CLICKED: "uom_view_table_create_row_button_clicked",
  UOM_VIEW_DATE_RANGE_APPLIED: "uom_view_date_range_applied",
  UOM_VIEW_TABLE_SORT_DATA_CHANGED: "uom_view_table_sort_data_changed",
  UOM_VIEW_TABLE_PAGINATION_CHANGED: "uom_view_table_pagination_changed",

  //Grid Specific
  UOM_VIEW_GRID_VALUE_CHANGED: "uom_view_grid_value_changed",
  UOM_VIEW_GRID_TAG_VALUE_CHANGED: "uom_view_grid_tag_value_changed",
  UOM_VIEW_GRID_ACTION_ITEM_CLICKED: "uom_view_grid_action_item_clicked",
  UOM_VIEW_FULL_SCREEN_ICON_CLICKED: "uom_view_full_screen_icon_clicked",
  UOM_GRID_VIEW_FULL_SCREEN_BUTTON_HANDLER: "uom_grid_view_full_screen_button_handler",
  UOM_VIEW_GRID_ADD_LINKED_INSTANCES_CLICKED: "uom_view_grid_add_linked_instances",
  UOM_VIEW_GRID_REMOVE_LINKED_INSTANCES_CLICKED: "uom_view_grid_remove_linked_instances_clicked"
};

const oPropTypes = {
  contextId: ReactPropTypes.string,
  contextLabel: ReactPropTypes.string,
  isSectionExpanded: ReactPropTypes.bool,
  tableData: ReactPropTypes.object,
  filterData: ReactPropTypes.object,
  UOMVariantViewData: ReactPropTypes.object,
  sectionIsExpandedMap: ReactPropTypes.object,
  showCreateButton: ReactPropTypes.bool,
  isTableContentDirty: ReactPropTypes.bool,
  showDateRangeSelector: ReactPropTypes.bool,
  hidePaper: ReactPropTypes.bool,
  isRowSelectionMode: ReactPropTypes.bool,
  rowSelectionData: ReactPropTypes.object,
  context: ReactPropTypes.string,
  eventHandlers: ReactPropTypes.object,
  isFullScreenMode: ReactPropTypes.bool,
};

// @CS.SafeComponent
class UOMView extends React.Component {
  static propTypes = oPropTypes;

  handleTableCreateRowButtonClicked = (sContextId) => {
    EventBus.dispatch(oEvents.UOM_VIEW_TABLE_CREATE_ROW_BUTTON_CLICKED, sContextId);
  };

  handleDateRangeApplied = (oRangeData) => {
    if (this.props.eventHandlers && CS.isFunction(this.props.eventHandlers["dateRangeAppliedHandler"])) {
      this.props.eventHandlers["dateRangeAppliedHandler"](oRangeData, this.props.contextId);
    } else {
      EventBus.dispatch(oEvents.UOM_VIEW_DATE_RANGE_APPLIED, oRangeData, this.props.contextId);
    }
  };

  handleSortDataChanged = (oSortData) => {
    if (this.props.eventHandlers && CS.isFunction(this.props.eventHandlers["sortDataChangedHandler"])) {
      this.props.eventHandlers["sortDataChangedHandler"](this.props.contextId, oSortData);
    } else {
      let sTableContext = this.props.tableData.tableSettings.context;
      EventBus.dispatch(oEvents.UOM_VIEW_TABLE_SORT_DATA_CHANGED, this.props.contextId, oSortData, sTableContext);
    }
  };

  handlePaginationChanged = (oPaginationData) => {
    if (this.props.eventHandlers && CS.isFunction(this.props.eventHandlers["paginationChangedHandler"])) {
      this.props.eventHandlers["paginationChangedHandler"](this.props.contextId, oPaginationData, this.props.filterContext);
    } else {
      let sTableContext = this.props.tableData.tableSettings.context;
      EventBus.dispatch(oEvents.UOM_VIEW_TABLE_PAGINATION_CHANGED, this.props.contextId, oPaginationData, this.props.filterContext, sTableContext);
    }
  };

  //Grid Specific

  handleGridPropertyValueChanged = (oViewData, sContentId, sPropertyId, sValue, sPathToRoot, sExpressionId) => {
    oViewData.expressionId = sExpressionId || "";
    EventBus.dispatch(oEvents.UOM_VIEW_GRID_VALUE_CHANGED, sContentId, sPropertyId, sValue, oViewData);
  };

  handleGridTagPropertyValueChanged = (oViewData, sContentId, sTagId, aTagValueRelevanceData) => {
    let oExtraData = this.getTagValueChangeExtraData(sContentId, sTagId);
    EventBus.dispatch(oEvents.UOM_VIEW_GRID_TAG_VALUE_CHANGED, sContentId, sTagId, aTagValueRelevanceData, oExtraData, oViewData);
  };

  handleGridActionItemClicked = (sViewContext, sActionItemId, sContentId) => {
  let oFilterData = this.props.filterData;
  var oExtraData = {
    hideFilterSearch: true,
    filterContext: this.props.filterContext
  };
    if (this.props.eventHandlers && CS.isFunction(this.props.eventHandlers["gridActionItemClickedHandler"])) {
      this.props.eventHandlers["gridActionItemClickedHandler"](sActionItemId, sContentId);
    } else {
      EventBus.dispatch(oEvents.UOM_VIEW_GRID_ACTION_ITEM_CLICKED, sActionItemId, sContentId, this.props.contextId, sViewContext, oFilterData, oExtraData);
    }
  };

  handleGridSortDataChanged = (sHeaderId) => {
    this.handleSortDataChanged(ViewUtils.getUpdatedSortData(this.props.tableData.tableSettings.sortData, sHeaderId));
  };

  handleGridCheckBoxClicked = (aSelectedItems) => {
    if (this.props.eventHandlers && CS.isFunction(this.props.eventHandlers["gridCheckBoxClickedHandler"])) {
      this.props.eventHandlers["gridCheckBoxClickedHandler"](aSelectedItems);
    }
  };

  handleGridSelectAllCheckBoxClicked = (aSelectedItems, bSelectAllClicked) => {
    if (this.props.eventHandlers && CS.isFunction(this.props.eventHandlers["selectAllClickedHandler"])) {
      this.props.eventHandlers["selectAllClickedHandler"](aSelectedItems, bSelectAllClicked);
    }
  };

  handleGridPropertyInstanceAddButtonClicked = (oViewData, sContentId, sPropertyId) => {
    if (this.props.eventHandlers && CS.isFunction(this.props.eventHandlers["gridPropertyInstanceAddButtonClickedHandler"])) {
      this.props.eventHandlers["gridPropertyInstanceAddButtonClickedHandler"](sContentId, sPropertyId);
    } else {
      let sContext = this.props.context;
      oViewData.columnId = sPropertyId;
      oViewData.rowId = sContentId;
      EventBus.dispatch(oEvents.UOM_VIEW_GRID_ADD_LINKED_INSTANCES_CLICKED, sContext, {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      }, oViewData);
    }
  };

  handleGridPropertyInstanceRemoveButtonClicked = (oViewData, sContentId, sPropertyId, aSelectedItems) => {
    oViewData.columnId = sPropertyId;
    oViewData.rowId = sContentId;
    EventBus.dispatch(oEvents.UOM_VIEW_GRID_REMOVE_LINKED_INSTANCES_CLICKED, oViewData, aSelectedItems);
  };

  handleFullScreenButtonClicked = () => {
    let sContextId = this.props.contextId;
    EventBus.dispatch(oEvents.UOM_VIEW_FULL_SCREEN_ICON_CLICKED, sContextId)
  };

  handleFullScreenGridViewButtonHandler = () => {
    let sContextId = this.props.contextId;
    EventBus.dispatch(oEvents.UOM_GRID_VIEW_FULL_SCREEN_BUTTON_HANDLER, sContextId);
  };

  getTagValueChangeExtraData = (sContentId, sTagId) => {
    let _oProps = this.props;
    let oHeaderData = CS.find(_oProps.tableData.tableHeaderData, {id: sTagId});
    let sContext = _oProps.tableData.tableSettings.context;

    return ({
      outerContext: sContext,
      rowId: sContentId,
      type: oHeaderData.type,
      columnId: sTagId,
      tableContextId: _oProps.contextId
    });
  };

  getIsDisableTableView = () => {
    let oTableData = this.props.tableData;
    let oSettings = oTableData.tableSettings;
    let aHeaderData = oTableData.tableHeaderData;
    return (oSettings && oSettings.isTranspose) ? (aHeaderData).length <= 1 : false;
  };

  getAttributeFilterView = () => {
    var __props = this.props;
    let oFilterData = this.props.filterData;
    var oExtraData = {
      contextId: __props.contextId,
      hideFilterSearch: true,
    };
    if (this.props.showFilterView === false) {
      return null;
    }


    if (!CS.isEmpty(oFilterData.availableFilterData) || __props.isRowSelectionMode) {
      let sContext = this.props.tableData.tableSettings.context || "UOM_Table_Filter";
      return (
          <AttributeFilterView
              filterData={oFilterData}
              onViewUpdate={null}
              context={sContext}
              extraData={oExtraData}
              filterContext={this.props.filterContext}
          />
      );
    }
    else {
      return null;
    }
  };

  getTableOperationsView = () => {
    let _props = this.props;
    let oTableData = _props.tableData;
    let sContextId = _props.contextId;

    let oCreateButtonData = null;
    if (_props.showCreateButton && !_props.isTableContentDirty) {
      oCreateButtonData = {
        onClickHandler: this.handleTableCreateRowButtonClicked.bind(this, sContextId)
      };
    }

    if (_props.isTableContentDirty) {
      oCreateButtonData = {
        isDisabled : true
      };
    }

    let oDateRangeSelectorData = null;
    let oTableSettings = oTableData.tableSettings;
    let oTimeRangeData = !CS.isEmpty(oTableSettings) && oTableSettings.timeRangeData || {from: null, to: null};
    if (_props.showDateRangeSelector) {
      oDateRangeSelectorData = {
        startDate: oTimeRangeData.from,
        endDate: oTimeRangeData.to,
        showCurrentDateButton: true,
        onApply: this.handleDateRangeApplied
      };
    }

    return (
        <TableOperationsView
            createButtonData={oCreateButtonData}
            dateRangeSelectorData={oDateRangeSelectorData}
            isDisableTableView={this.getIsDisableTableView()}
        />
    );
  };

  getUOMBodyByMode = () => {
    let sMode = this.props.viewMode;
    if (sMode == ContentScreenConstants.UOM_BODY_GRID_MODE) {
      return this.getGridView();
    } else {
      return this.getTableView();
    }
  };

  getGridView = () => {
    let {data: aGridData, skeleton: oSkeleton, tableSettings: oSettings, key:sKey, resizedColumnId: sResizedColumnId} = this.props.tableData;
    let oUOMVariantViewData = this.props.UOMVariantViewData;
    let sKlassInstanceId = ContentUtils.getKlassInstanceId();
    if (CS.isEmpty(aGridData)) {
      return (<NothingFoundView/>)
    } else {
      let oViewData = {viewMode: this.props.viewMode, sContext: oSettings.context, sTableContextId: this.props.contextId};
      let oGridPropertyEventHandlers = {
        gridPropertyValueChangedHandler: this.handleGridPropertyValueChanged.bind(this, oViewData),
        gridTagPropertyValueChangedHandler: this.handleGridTagPropertyValueChanged.bind(this, oViewData),
        gridPropertyInstanceAddButtonClickedHandler: this.handleGridPropertyInstanceAddButtonClicked.bind(this, oViewData),
        gridPropertyInstanceCrossIconClickedHandler: this.handleGridPropertyInstanceRemoveButtonClicked.bind(this, oViewData)
      };
      let oColumnOrganizerData = {
        klassInstanceId: sKlassInstanceId
      };
      return (
          <div className={"uomGridViewWrapper"}>
            <GridView
                data={aGridData}
                resizedColumnId={sResizedColumnId}
                referencedAttributes={oUOMVariantViewData.referencedAttributes}
                skeleton={oSkeleton}
                context={"uomView"}
                key={sKey}
                paginationData={oSettings.paginationData}
                totalItems={oSettings.paginationData.totalItems}
                sortBy={oSettings.sortData.sortBy}
                sortOrder={oSettings.sortData.sortOrder}
                gridPaginationChangedHandler={this.handlePaginationChanged}
                actionItemClickedHandler={this.handleGridActionItemClicked.bind(this, oSettings.context)}
                columnHeaderClickedHandler={this.handleGridSortDataChanged}
                selectClickedHandler={this.handleGridCheckBoxClicked}
                selectAllClickedHandler={this.handleGridSelectAllCheckBoxClicked}
                gridPropertyViewHandlers={oGridPropertyEventHandlers}
                showContentCheckBox={oSettings.showContentCheckBox}
                showContentRadioButton={oSettings.showContentRadioButton}
                showCheckboxColumn={oSettings.showContentCheckBox}
                hideSelectAll={oSettings.hideSelectAll}
                isSingleSelect={oSettings.isSingleSelect}
                hideUpperSection={false}
                disableDeleteButton={true}
                enableImportExportButton={false}
                disableCreate={true}
                enableManageEntityButton={false}
                hideSearchBar={true}
                disableRefresh={true}
                showGridColumnOrganiserButton={true}
                lightToolbarIcons={true}
                tableContextId={oViewData.sTableContextId}
                columnOrganizerData={oColumnOrganizerData}
                isColumnOrganizerDialogOpen={this.props.isColumnOrganizerDialogOpen}
                selectedColumns={this.props.selectedColumns}
            />
          </div>
      )
    }
  };

  getTableView = () => {

    let oTableData = this.props.tableData;
    let oUOMVariantViewData = this.props.UOMVariantViewData;

    if (!CS.isEmpty(oTableData)) {
      return (
          <TableView
              tableContextId={this.props.contextId}
              headerData={oTableData.tableHeaderData}
              data={oTableData.tableBodyData}
              settings={oTableData.tableSettings}
              onSortChange={this.handleSortDataChanged}
              onPaginationChange={this.handlePaginationChanged}
              referencedAttributes={oUOMVariantViewData.referencedAttributes}
              referencedTags={oUOMVariantViewData.referencedTags}
              isDisableTableView={this.getIsDisableTableView()}
              isRowSelectionMode={this.props.isRowSelectionMode}
              rowSelectionData={this.props.rowSelectionData}
              context={this.props.context}
              filterContext={this.props.filterContext}
          />
      );
    }
    else {
      return null;
    }
  };

  getVariantDialogView = () => {
    let oUomVariantViewData = this.props.UOMVariantViewData;

    if (!CS.isEmpty(oUomVariantViewData)) {
      let bIsDialogOpen = oUomVariantViewData.isVariantDialogOpen;

      return (
          <VariantDialogView isDialogOpen={bIsDialogOpen}
                             variantSectionViewData={oUomVariantViewData}
                             canEdit={true}
                             canDelete={true}
                             filterContext={this.props.filterContext}/>
      );
    }
    else {
      return null;
    }
  };

  getFilterRowView = () => {
    let oAttributeFilterView = this.getAttributeFilterView();
    let sWrapperClass = "filterRowWrapper ";
    if (CS.isEmpty(oAttributeFilterView)) {
      sWrapperClass = null;
    }
    return (<div className={sWrapperClass}>{oAttributeFilterView}</div>)
  };

  getFullScreenView = (oView) => {
    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true,
        isDisabled: false,
        variant: "outlined",
        color: "primary",
        actionHandler: this.handleFullScreenGridViewButtonHandler,
      },
    ];
    if (this.props.isTableContentDirty) {
      aButtonData.push(
        {
          id: "apply",
          label: getTranslation().APPLY,
          isFlat: false,
          isDisabled: false,
          actionHandler: this.handleFullScreenButtonClicked,
        }
      );
    }


    return (
        <FullScreenView
            showHeader={false}
            header={this.props.contextLabel}
            isFullScreenMode={true}
            bodyView={oView}
            actionButtonsData={aButtonData}
        />
    )

  };

  getFullScreenButtonView = () => {
    let {data: aGridData} = this.props.tableData;
    return (
        CS.isNotEmpty(aGridData) ?
        <ToolTip placement="bottom" label={getTranslation().EXPAND}>
            <div className="fullScreenButtonForUOM" onClick={this.handleFullScreenButtonClicked}>
              <div className="fullScreenButton"></div>
            </div>
        </ToolTip>
            : null
    )
  };

  render() {

    let sContextId = this.props.contextId;
    let sContextLabel = this.props.contextLabel;
    let bIsSectionExpanded = this.props.isSectionExpanded;
    let bIsSectionLoading = this.props.isSectionLoading;
    let sPaperContext = "UOM";
    if(CS.isNotEmpty(this.props.tableData) && CS.isNotEmpty(this.props.tableData.tableSettings)) {
      sPaperContext = this.props.tableData.tableSettings.context == "variant_context" && "Variant";
    };
    let oPaperViewModel = new PaperViewModel(sContextId, null, null, null, sContextLabel, "", {context: sPaperContext});

    let oTableOperationsView = this.getTableOperationsView();
    let oTableView = this.getUOMBodyByMode();
    let oVariantDialogView = this.getVariantDialogView();
    let bHidePaper = this.props.hidePaper;
    let oLoadingSection = null;
    let bIsFullScreenMode = this.props.isFullScreenMode;

    let oView = <div className="uomView">
      {this.getFilterRowView()}
      <div className="tableOperationWrapper">
        {this.props.isRowSelectionMode ? null : oTableOperationsView}
        {bIsFullScreenMode ? null : this.getFullScreenButtonView()}
      </div>
      {oTableView}
      {this.props.isRowSelectionMode ? null : oVariantDialogView}
    </div>;

    if (bIsSectionLoading) {
      oLoadingSection = (
            <div className="loadingAnimationContainer">
              <Loader color="#26A65B"/>
            </div>
      );
    }

    if (bHidePaper) {
      return oView;
    } else {
      if (bIsFullScreenMode) {
        return (
            <React.Fragment>
              {oLoadingSection}
              {this.getFullScreenView(oView)}
            </React.Fragment>
        )
      }
      else {
        return <PaperView model={oPaperViewModel} isExpanded={bIsSectionExpanded}>
          {oLoadingSection}
          {oView}
        </PaperView>
      }
    }
  }
}

export const view = UOMView;
export const events = oEvents;
