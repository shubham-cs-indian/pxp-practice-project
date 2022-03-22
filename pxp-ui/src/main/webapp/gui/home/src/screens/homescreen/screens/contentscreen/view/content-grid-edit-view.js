import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import { view as GridView } from './../../../../../viewlibraries/gridview/grid-view';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
let getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

const oEvents = {
  CONTENT_GRID_EDIT_HANDLE_ROW_SAVE: "content_grid_edit_handle_row_save",
  CONTENT_GRID_EDIT_BUTTON_CLICKED: "content_grid_edit_button_clicked",
  GRID_EDITABLE_PROPERTIES_CONFIG_DIALOG_BUTTON_CLICKED: "grid_editable_properties_config_dialog_button_clicked"
};

const oPropTypes = {
  contentGridEditViewData: ReactPropTypes.object
};

// @CS.SafeComponent
class ContentGridEditView extends React.Component {
  constructor(props) {
    super(props);
  }

  handleContentGridEditViewContainerClicked =(oEvent)=>{
    EventBus.dispatch(oEvents.CONTENT_GRID_EDIT_HANDLE_ROW_SAVE, oEvent);
  };

  handleGridEditButtonClicked = () => {
    EventBus.dispatch(oEvents.CONTENT_GRID_EDIT_BUTTON_CLICKED);
  };

  handleColumnOrganizerDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.GRID_EDITABLE_PROPERTIES_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  getContextMenuModel (aEditableProperties) {
    let aModelsList = [];

    CS.forEach(aEditableProperties, function (oEditableProperty) {
      aModelsList.push(new ContextMenuViewModel(
          oEditableProperty.id,
          CS.getLabelOrCode(oEditableProperty),
          false,
          "",
          {customIconClassName: oEditableProperty.type}
      ));
    });

    return aModelsList;
  };

  getColumnOrganizerRequestResponseInfo() {
    let oCustomReqResInfo = {};
    oCustomReqResInfo.searchRequestResponseInfo = {
      url: getRequestMapping().GetGridEditableProperties,
      hiddenColumnsResponsePath: "gridEditProperties",
    };
    return oCustomReqResInfo;
  }

  getContentGridEditView() {
    let oContentGridEditViewData = this.props.contentGridEditViewData;
    let oScrollbarStyle = {
      height: "calc(100% - 5px)"
    };

    let oColumnOrganizeData = {
      context: "contentGridEdit",
      availableColumns: oContentGridEditViewData.columnOrganizerData.availableColumns,
      selectedColumns: [],
      isColumnOrganizerDialogOpen: false,
      columnOrganizerButtonHandler: this.handleGridEditButtonClicked,
      customRequestResponseInfo: this.getColumnOrganizerRequestResponseInfo(),
      selectedColumnsLimit: oContentGridEditViewData.columnOrganizerData.selectedColumnsLimit,
      bIsLoadMore :oContentGridEditViewData.columnOrganizerData.bIsLoadMore
    };

    return (
        <div className="contentGridEditViewContainer" onClick={this.handleContentGridEditViewContainerClicked}>
          <GridView
              skeleton={oContentGridEditViewData.gridViewSkeleton}
              data={oContentGridEditViewData.gridViewData}
              visualData={oContentGridEditViewData.gridViewVisualData}
              searchText={oContentGridEditViewData.searchText}
              sortBy={oContentGridEditViewData.sortBy}
              sortOrder={oContentGridEditViewData.sortOrder}
              isGridDataDirty={oContentGridEditViewData.isGridDataDirty}
              showCheckboxColumn={oContentGridEditViewData.showCheckboxColumn}
              disableCreate={oContentGridEditViewData.disableCreate}
              disableDeleteButton={oContentGridEditViewData.disableDelete}
              disableRefresh={oContentGridEditViewData.disableRefresh}
              scrollbarStyle={oScrollbarStyle}
              isColumnOrganizerDialogOpen={oContentGridEditViewData.columnOrganizerData.isColumnOrganizerDialogOpen}
              selectedColumns={oContentGridEditViewData.columnOrganizerData.selectedColumns}
              columnOrganizerData={oColumnOrganizeData}
              resizedColumnId={oContentGridEditViewData.resizedColumnId}
          />
        </div>
    )
  };

  render() {
    return this.getContentGridEditView();
  }
}

ContentGridEditView.propTypes = oPropTypes;

export const view = ContentGridEditView;
export const events = oEvents;
