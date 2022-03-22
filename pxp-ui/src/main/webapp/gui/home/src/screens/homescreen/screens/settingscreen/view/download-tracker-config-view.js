import React from 'react';
import ReactPropTypes from 'prop-types';
import {view as GridView} from '../../../../../viewlibraries/gridview/grid-view.js';

const oEvents = {};
const oPropTypes = {
  gridViewProps: ReactPropTypes.object,
};

// @CS.SafeComponent
class DownloadTrackerConfigView extends React.Component {
  static propTypes = oPropTypes;

  getDownloadInfoGridView = () => {
    let oGridViewProps = this.props.gridViewProps;
    let oScrollbarStyle = {
      height: "calc(100% - 10px)"
    };
    let oColumnOrganizerData = this.props.columnOrganizerData;

    return (
        <GridView
            context={"download_tracker"}
            skeleton={oGridViewProps.gridViewSkeleton}
            data={oGridViewProps.gridViewData}
            visualData={{}}
            paginationData={oGridViewProps.gridPaginationData}
            totalItems={oGridViewProps.totalItems}
            isGridDataDirty={false}
            showCheckboxColumn={true}
            disableDeleteButton={true}
            disableCreate={true}
            disableRefresh={false}
            hideUpperSection={false}
            enableDownload={false}
            hideSearchBar={true}
            enableFilterButton={false}
            enableImportExportButton={true}
            disableImportButton={true}
            showFilterView={oGridViewProps.showFilterView}
            scrollbarStyle={oScrollbarStyle}
            filterData={oGridViewProps.filterData}
            searchText={""}
            sortBy={oGridViewProps.sortBy}
            sortOrder={oGridViewProps.sortOrder}
            selectedColumns={oColumnOrganizerData.selectedColumns}
            isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
        />
    );
  };

  render () {
    return (
        <div className={"downloadTrackerConfigViewWrapper"}>
          {this.getDownloadInfoGridView()}
        </div>
    );
  }
}

export const view = DownloadTrackerConfigView;
export const events = oEvents;
