import React from 'react' ;
import ReactPropTypes from 'prop-types';
import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as CustomDialogView} from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import {view as ContextualGridView} from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';

const oEvents = {
  AUDIT_LOG_DIALOG_BUTTON_CLICKED: "audit_log_dialog_button_clicked",
  AUDIT_LOG_EXPORT_DIALOG_REFRESH_BUTTON_CLICKED: "audit_log_export_dialog_refresh_button_clicked",
  AUDIT_LOG_EXPORT_DIALOG_PAGINATION_CHANGED: "audit_log_export_dialog_pagination_changed",
};

const oPropTypes = {
  skeleton: ReactPropTypes.object,
  data: ReactPropTypes.array,
  visualData: ReactPropTypes.object,
  paginationData: ReactPropTypes.object,
  totalItems: ReactPropTypes.number,
};

// @CS.SafeComponent
class ShowExportStatusDialogView extends React.Component {

  static oPropTypes = oPropTypes;

  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.AUDIT_LOG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  handleRefreshButtonClicked = () => {
    EventBus.dispatch(oEvents.AUDIT_LOG_EXPORT_DIALOG_REFRESH_BUTTON_CLICKED);
  };

  handlePaginationChangeClicked = (oNewPaginationData) => {
    EventBus.dispatch(oEvents.AUDIT_LOG_EXPORT_DIALOG_PAGINATION_CHANGED, oNewPaginationData);
  };

  getGridView = () => {
    let oColumnOrganizerData = this.props.columnOrganizerData;
    return (
        <div className='showExportStatusDialogGridView'>
          <ContextualGridView
              context={this.props.context}
              tableContextId={this.props.tableContextId}
              showCheckboxColumn={false}
              disableDeleteButton={true}
              enableImportExportButton={false}
              disableCreate={true}
              enableManageEntityButton={false}
              hideSearchBar={true}
              refreshButtonClickedHandler={this.handleRefreshButtonClicked}
              gridPaginationChangedHandler={this.handlePaginationChangeClicked}
              selectedColumns={oColumnOrganizerData.selectedColumns}
              isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
          />
        </div>

    );
  };

  getDialogView = () => {
    let oBodyStyle = {
      maxWidth: "1200px",
      minWidth: "800px",
      padding: 0,
      overflow: "hidden",
    };
    let aButtonData = [
      {
        id: "ok",
        label: getTranslations().OK,
        isDisabled: false,
        isFlat: false,
      }
    ];
    let fButtonHandler = this.handleDialogButtonClicked;
    let oGridViewDom = this.getGridView();
    let sTitle = getTranslations().AUDIT_LOG_EXPORT_STATUS;
    return (
        <CustomDialogView modal={true}
                          open={true}
                          bodyStyle={oBodyStyle}
                          contentStyle={oBodyStyle}
                          title={sTitle}
                          className={'showExportStatusDialog'}
                          buttonData={aButtonData}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          buttonClickHandler={fButtonHandler}>
          {oGridViewDom}
        </CustomDialogView>
    )
  };


  render () {
    return (this.getDialogView());
  };
}

export const view = ShowExportStatusDialogView;
export const events = oEvents;
