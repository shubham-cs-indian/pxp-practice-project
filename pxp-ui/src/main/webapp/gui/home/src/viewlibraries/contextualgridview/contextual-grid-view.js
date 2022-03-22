import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as NothingFoundView } from './../nothingfoundview/nothing-found-view';
import ContextualGridViewProps from "./store/model/contextual-grid-view-props";
import { view as GridView } from '../gridview/grid-view';

const oEvents = {
};

const oPropTypes = {

  context: ReactPropTypes.string.isRequired, //denotes where the grid view is being used
  hierarchical: ReactPropTypes.bool,
  hideActiveCellSelection: ReactPropTypes.bool,
  isGridDataDirty: ReactPropTypes.bool,
  showCheckboxColumn: ReactPropTypes.bool,
  showSortButton: ReactPropTypes.bool,
  showIsRowDisabled: ReactPropTypes.bool,
  customMessage: ReactPropTypes.string,
  showAddOnView: ReactPropTypes.bool,
  addOnView: ReactPropTypes.object,
  hideSelectAll: ReactPropTypes.bool,
  hideUpperSection: ReactPropTypes.bool,
  hideLowerSection: ReactPropTypes.bool,
  enableAutoLoadMore: ReactPropTypes.bool,
  disableCreate: ReactPropTypes.bool,
  enableManageEntityButton: ReactPropTypes.bool,
  columnOrganizerData: ReactPropTypes.object,
  tableContextId: ReactPropTypes.string,
  disableDeleteButton: ReactPropTypes.bool,
  enableImportExportButton: ReactPropTypes.bool,
  hideSearchBar: ReactPropTypes.bool,
  duplicateCode: ReactPropTypes.array,
  duplicateCodesForWorkflows: ReactPropTypes.array,
  enableCopyButton: ReactPropTypes.bool,
  disableSearch: ReactPropTypes.bool,
  activeEntity: ReactPropTypes.object,
  showGridColumnOrganiserButton: ReactPropTypes.bool,
  disableImportButton: ReactPropTypes.bool,
  enableShowExportStatusButton: ReactPropTypes.bool,
  filterData: ReactPropTypes.object,

  //Handlers
  refreshButtonClickedHandler: ReactPropTypes.func,
  gridPaginationChangedHandler: ReactPropTypes.func,

  //Child Handlers
  gridPropertyViewHandlers: ReactPropTypes.object,
};

/**
 * @class GridView - use to Display GridView in Application.
 * @memberOf Views
 * @property {string} context -  context name.
 * @property {bool} [hierarchical] -  boolean for grid have hierarchical or not.
 * @property {bool} [hideActiveCellSelection] -  boolean for grid have hideActiveCellSelection or not.
 * @property {bool} [isGridDataDirty] -  boolean for grid have isGridDataDirty or not.
 * @property {bool} [showCheckboxColumn] -  boolean for grid have showCheckboxColumn or not.
 * @property {bool} [showAddOnView] -  boolean for grid have showAddOnView or not.
 * @property {object} [addOnView] -  object of addOnView.
 * @property {bool} [hideUpperSection] -  boolean for grid hideUpperSection or not.
 * @property {bool} [hideLowerSection] -  boolean for grid hideLowerSection or not.
 * @property {bool} [enableAutoLoadMore] -  boolean for grid have enableAutoLoadMore or not.
 * @property {bool} [disableCreate] -  boolean for grid have disableCreate or not.
 */

// @CS.SafeComponent
class ContextualGridView extends React.Component {

  constructor (props) {
    super(props);

  }

  componentWillUnmount () {
    ContextualGridViewProps.resetGridViewPropsByContext(this.props.context)
  };

  getGridViewProps = () => {
    let sContext = this.props.context;
    if (!sContext) {
      throw "Inside contextual-grid-view context should not be empty"
    }
    return ContextualGridViewProps.getGridViewPropsByContext(sContext)?.gridViewProps;
  };

  getColumnOrganizerProps = () => {
    let sContext = this.props.context;
    if (!sContext) {
      throw "Inside contextual-grid-view context should not be empty"
    }
    return ContextualGridViewProps.getGridViewPropsByContext(sContext)?.columnOrganizerProps;
  };


  render () {
    let oGridViewProps = this.getGridViewProps();
    if(oGridViewProps) {
      let oSortData = oGridViewProps.getGridViewSortData();
      let oColumnOrganizerProps = this.getColumnOrganizerProps();
      let oProps = this.props;

      return (
        <GridView
          context={oProps.context}
          resizedColumnId={oGridViewProps.getResizedColumnId()}
          skeleton={oGridViewProps.getGridViewSkeleton()}
          data={oGridViewProps.getGridViewData()}
          visualData={oGridViewProps.getGridViewVisualData()}
          hierarchical={oProps.hierarchical}
          hideActiveCellSelection={this.props.hideActiveCellSelection}
          paginationData={oGridViewProps.getGridViewPaginationData()}
          searchText={oGridViewProps.getGridViewSearchText()}
          sortBy={oSortData.sortBy}
          sortOrder={oSortData.sortOrder}
          totalItems={oGridViewProps.getGridViewTotalItems()}
          totalNestedItems={oGridViewProps.getGridViewTotalNestedItems()}
          isGridDataDirty={oGridViewProps.getIsGridDataDirty()}
          showCheckboxColumn={oProps.showCheckboxColumn}
          showSortButton={oProps.showSortButton}
          showIsRowDisabled={oProps.showIsRowDisabled}
          customMessage={oProps.customMessage}
          showAddOnView={oProps.showAddOnView}
          addOnView={oProps.addOnView}
          hideSelectAll={oProps.hideSelectAll}
          hideUpperSection={oProps.hideUpperSection}
          hideLowerSection={oProps.hideLowerSection}
          enableAutoLoadMore={oProps.enableAutoLoadMore}
          disableCreate={oProps.disableCreate}
          activeContentId={oGridViewProps.getGridViewParentId()}
          activeCellDetails={oGridViewProps.getGridViewActiveCellDetails()}
          selectedColumns={oColumnOrganizerProps.getSelectedOrganizedColumns()}
          isColumnOrganizerDialogOpen={oColumnOrganizerProps.getIsDialogOpen()}
          columnOrganizerData={oProps.columnOrganizerData}
          disableDeleteButton={oProps.disableDeleteButton}
          enableImportExportButton={oProps.enableImportExportButton}
          enableManageEntityButton={oProps.enableManageEntityButton}
          tableContextId={oProps.tableContextId}
          hideSearchBar={oProps.hideSearchBar}
          refreshButtonClickedHandler={oProps.refreshButtonClickedHandler}
          gridPaginationChangedHandler={oProps.gridPaginationChangedHandler}
          gridPropertyViewHandlers={oProps.gridPropertyViewHandlers}
          duplicateCode={oProps.duplicateCode}
          duplicateCodesForWorkflows={oProps.duplicateCodesForWorkflows}
          enableCopyButton={oProps.enableCopyButton}
          disableSearch={oProps.disableSearch}
          activeEntity={oProps.activeEntity}
          showGridColumnOrganiserButton={oProps.showGridColumnOrganiserButton}
          disableImportButton={oProps.disableImportButton}
          enableShowExportStatusButton={oProps.enableShowExportStatusButton}
          filterData={oProps.filterData}
          gridWFValidationErrorList={oProps.gridWFValidationErrorList}
        />
      )
    } else {
      return <NothingFoundView/>;
    }
  }
}
ContextualGridView.propTypes = oPropTypes;

export const view = ContextualGridView;
export const events = oEvents;
