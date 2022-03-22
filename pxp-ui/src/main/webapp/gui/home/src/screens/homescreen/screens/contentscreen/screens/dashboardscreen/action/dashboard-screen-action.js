import CS from '../../../../../../../libraries/cs';
import eventBus from '../../../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ActionInterceptor from '../../../../../../../libraries/actioninterceptor/action-interceptor.js';
import { events as KeydownEvents } from '../../../../../../../libraries/keydownhandler/keydownhandler';
import DashboardScreenStore from '../store/dashboard-screen-store';
import { events as DashboardView } from '../../../../../../../viewlibraries/dashboardview/dashboard-view';
import { events as DashboardEndpointTileViewEvents } from '../view/dashboard-endpoint-tile-view';
import { events as PhysicalCatalogSelectorEvents } from '../../../../../../../viewlibraries/physicalcatalogselectorview/physical-catalog-selector-view';
import { events as DashboardWrapperViewEvents } from '../view/dashboard-wrapper-view';
import { events as DashboardTileViewEvents } from '../../../../../../../viewlibraries/dashboardview/dashboard-tile-view';
import { events as TabLayoutViewEvents } from '../../../../../../../viewlibraries/tablayoutview/tab-layout-view';
import { events as LanguageSelectionView } from '../../../../../../../viewlibraries/languageselectionview/language-selection-view';
import ExceptionLogger from '../../../../../../../libraries/exceptionhandling/exception-logger';
import { events as DataIntegrationLogsView } from "../view/data-integration-logs-view";
import {events as GridViewEvents} from "../../../../../../../viewlibraries/gridview/grid-view";
import {events as BackgroundProcessDetailEvents} from "../view/background-processes-dialog-view";
import {events as GridFilterViewEvents} from "../../../../../../../viewlibraries/gridview/grid-filter-view";

let ContentScreenAction = (function () {

  let oEventHandler = {};

  let handleKeyDown = function () {
    ExceptionLogger.log("Key Down!");
  };

  let handleDashboardViewBarGroupClicked = function (sKpiId, sCategoryLabel, sCategoryIndex) {
    DashboardScreenStore.handleDashboardViewBarGroupClicked(sKpiId, sCategoryLabel, sCategoryIndex);
  };

  let handleDashboardBreadCrumbClicked = function (sKpiId, sBreadCrumbId) {
    DashboardScreenStore.handleBreadCrumbClicked(sKpiId, sBreadCrumbId);
  };

  let handleDashboardTilesLoadMoreClicked = function (sContext, iNewLeft) {
    DashboardScreenStore.handleDashboardTilesLoadMoreClicked(sContext, iNewLeft);
  };

  let handledDashboardViewCloseDialogClicked = function (sTileId) {
    DashboardScreenStore.handledDashboardViewCloseDialogClicked(sTileId);
  };

  let handleModuleItemClicked = function (sModuleId, sContext) {
    DashboardScreenStore.handleModuleItemClicked(sModuleId, sContext);
  };

  let handleDashboardEndpointTileModeChanged = (sEndpointId, sMode) => {
    DashboardScreenStore.handleDashboardEndpointTileModeChanged(sEndpointId, sMode);
  };

  let handlePhysicalCatalogChanged = function () {
    DashboardScreenStore.refreshAll();
  };

  let handleDashboardTilePaginationButtonClick = function (sContext, sTileId) {
    DashboardScreenStore.handleDashboardTilePaginationButtonClick(sContext, sTileId);
  };

  let handleDashboardTileOpenDialogButtonClicked = function (sTileId) {
    DashboardScreenStore.handleDashboardTileOpenDialogButtonClicked(sTileId);
  };

  let handleUILanguageChanged = function (sButtonId) {
    DashboardScreenStore.handleUILanguageChanged(sButtonId);
  };

  let handleInvertKPIChartButtonClicked = function (oKpiData) {
    DashboardScreenStore.handleInvertKPIChartButtonClicked(oKpiData);
  };

  let handleDataIntegrationLogsViewRadioButtonClicked = function (sContext) {
    DashboardScreenStore.handleDataIntegrationLogsViewRadioButtonClicked(sContext);
  };

  let handleDataIntegrationLogsViewLazyMssApplyClicked = function (sContext, aSelectedItems, oReferencedData) {
    DashboardScreenStore.handleDataIntegrationLogsViewLazyMssApplyClicked(sContext, aSelectedItems, oReferencedData);
  };

  let handleDataIntegrationLogsViewDownloadButtonClicked = function () {
    DashboardScreenStore.handleDataIntegrationLogsViewDownloadButtonClicked();
  };

  let handleDataIntegrationLogsViewSearchButtonClicked = function () {
    DashboardScreenStore.handleDataIntegrationLogsViewSearchButtonClicked();
  };

  let handleMessageTypeDialogButtonClicked = function (sButtonId) {
    DashboardScreenStore.handleMessageTypeDialogButtonClicked(sButtonId);
  };

  let handleDataIntegrationLogsViewMSSClearButtonClicked = function (sContext) {
    DashboardScreenStore.handleDataIntegrationLogsViewMSSClearButtonClicked(sContext);
  };

  let handleGridViewRefreshButtonClicked = function () {
    DashboardScreenStore.handleGridViewRefreshButtonClicked();
  };

  let handleGridViewActionItemClicked = function (sActionItemId, sContentId) {
    DashboardScreenStore.handleGridViewActionItemClicked(sActionItemId, sContentId);
  };

  let handleBackgroundProcessDialogViewButtonClicked = function (sButtonId) {
    DashboardScreenStore.handleBackgroundProcessDialogViewButtonClicked(sButtonId)
  };

  let handleGridViewFilterButtonClicked = function (bShowFilterView) {
    DashboardScreenStore.handleGridViewFilterButtonClicked(bShowFilterView);
  };

  let handleGridViewColumnHeaderClicked = function (sColumnId) {
    DashboardScreenStore.handleGridViewColumnHeaderClicked(sColumnId);
  };

  let handleGridPaginationChanged = function (oNewPaginationData) {
    DashboardScreenStore.handleGridPaginationChanged(oNewPaginationData);
  };

  let handleGridFilterApplyClicked = function (oAppliedFilterData, oReferencedData) {
    DashboardScreenStore.handleGridFilterApplyClicked(oAppliedFilterData, oReferencedData);
  };

  let handleBGPServiceChanged = function (sId) {
    DashboardScreenStore.handleBGPServiceChanged(sId)
  };

  /**
   * Binding Events into EventHandler
   */
  (() => {
    /** @deprecated*/
    let _setEvent = CS.set.bind(this, oEventHandler);

    oEventHandler[KeydownEvents.KEY_DOWN] = ActionInterceptor('Key Down', handleKeyDown);

    oEventHandler[DashboardView.DASHBOARD_VIEW_BAR_GROUP_CLICKED] = ActionInterceptor('Dashboard View Bar Group Clicked', handleDashboardViewBarGroupClicked);
    oEventHandler[DashboardView.DASHBOARD_VIEW_BREAD_CRUMB_CLICKED] = ActionInterceptor('Dashboard View Bread Crumb Clicked', handleDashboardBreadCrumbClicked);
    oEventHandler[DashboardView.HANDLE_LOAD_MORE_CLICKED] = ActionInterceptor('load more Clicked', handleDashboardTilesLoadMoreClicked);
    oEventHandler[DashboardView.HANDLE_DASHBOARD_VIEW_CLOSE_DIALOG_CLICKED] = ActionInterceptor('Dashboard View close dialog clicked', handledDashboardViewCloseDialogClicked);
    oEventHandler[DashboardEndpointTileViewEvents.DASHBOARD_ENDPOINT_TILE_MODE_CHANGED] = ActionInterceptor('Dashboard Endpoint Tile Mode Changed', handleDashboardEndpointTileModeChanged);

    oEventHandler[PhysicalCatalogSelectorEvents.HANDLE_PHYSICAL_CATALOG_OR_PORTAL_SELECTION_CHANGED] = ActionInterceptor('Physical catalog changed', handlePhysicalCatalogChanged);
    oEventHandler[DashboardTileViewEvents.HANDLE_DASHBOARD_TILE_PAGINATION_BUTTON_CLICKED] = ActionInterceptor('Dashboard tile view button click', handleDashboardTilePaginationButtonClick);
    oEventHandler[DashboardTileViewEvents.HANDLE_DASHBOARD_TILE_OPEN_DIALOG_BUTTON_CLICKED] = ActionInterceptor('Dashboard tile view open kpi in dialog button click', handleDashboardTileOpenDialogButtonClicked);
    oEventHandler[DashboardTileViewEvents.HANDLE_INVERT_CHART_BUTTON_CLICKED] = ActionInterceptor('Invert KPI Chart button clicked' , handleInvertKPIChartButtonClicked);

    oEventHandler[LanguageSelectionView.LANGUAGE_SELECTION_VIEW_HANDLE_LANGUAGE_CHANGED] = ActionInterceptor('User Language Changed: handleUILanguageChanged', handleUILanguageChanged);

    oEventHandler[TabLayoutViewEvents.HANDLE_TAB_LAYOUT_VIEW_TAB_CLICKED] = ActionInterceptor('Dashboard Tab' +
        ' Changed:handleModuleItemClicked', handleModuleItemClicked);

    //Data Integration Log View
    oEventHandler[DataIntegrationLogsView.DATA_INTEGRATION_LOGS_VIEW_RADIO_BUTTON_CLICKED] = ActionInterceptor('Data Integration logs view radio button clicked', handleDataIntegrationLogsViewRadioButtonClicked);
    oEventHandler[DataIntegrationLogsView.DATA_INTEGRATION_LOGS_VIEW_LAZY_MSS_CLICKED] = ActionInterceptor('Data' +
        ' Integration logs view  Lazy Mss apply clicked', handleDataIntegrationLogsViewLazyMssApplyClicked);
    oEventHandler[DataIntegrationLogsView.DATA_INTEGRATION_LOGS_VIEW_DOWNLOAD_BUTTON_CLICKED] = ActionInterceptor('Data Integration logs view download button clicked', handleDataIntegrationLogsViewDownloadButtonClicked);
    oEventHandler[DataIntegrationLogsView.DATA_INTEGRATION_LOGS_VIEW_SEARCH_BUTTON_CLICKED] = ActionInterceptor('Data Integration logs view search button clicked', handleDataIntegrationLogsViewSearchButtonClicked);
    oEventHandler[DataIntegrationLogsView.MESSAGE_TYPE_DIALOG_BUTTON_CLICKED] = ActionInterceptor('Message type download button clicked' , handleMessageTypeDialogButtonClicked);
    oEventHandler[DataIntegrationLogsView.DATA_INTEGRATION_LOGS_VIEW_MSS_CLEAR_BUTTON_CLICKED] = ActionInterceptor('Data Integration logs view Mss Clear button clicked', handleDataIntegrationLogsViewMSSClearButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_REFRESH_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewRefreshButtonClicked', handleGridViewRefreshButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_ACTION_ITEM_CLICKED] = ActionInterceptor('Grid View Action Item Clicked', handleGridViewActionItemClicked);
    oEventHandler[BackgroundProcessDetailEvents.BACKGROUND_PROCESS_DIALOG_VIEW_BUTTON_CLICKED] = ActionInterceptor('Grid View Action Item Clicked', handleBackgroundProcessDialogViewButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_FILTER_BUTTON_CLICKED] = ActionInterceptor('handleGridViewFilterButtonClicked', handleGridViewFilterButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_COLUMN_HEADER_CLICKED] = ActionInterceptor('handleGridViewColumnHeaderClicked', handleGridViewColumnHeaderClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_PAGINATION_CHANGED] = ActionInterceptor('Grid View Pagination Changed', handleGridPaginationChanged);
    oEventHandler[GridFilterViewEvents.GRID_FILTER_APPLIED] = ActionInterceptor('handleGridFilterApplyClicked', handleGridFilterApplyClicked);

    oEventHandler[DashboardWrapperViewEvents.BGP_SERVICE_CHANGED] = ActionInterceptor('handleBGPServiceChanged', handleBGPServiceChanged);
  })();

  return {

    registerEvent: function () {
      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        eventBus.addEventListener(sEventName, oHandler);
      });
    },

    deRegisterEvent: function () {
      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        eventBus.removeEventListener(sEventName, oHandler);
      });
    }
  }
})();

export default ContentScreenAction;
