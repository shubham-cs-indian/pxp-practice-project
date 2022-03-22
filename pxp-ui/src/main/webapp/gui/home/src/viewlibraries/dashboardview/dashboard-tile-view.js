import React from 'react';
import ReactPropTypes from 'prop-types';
import Loader from 'halogen/PulseLoader';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../tooltipview/tooltip-view';
import { view as BreadcrumbView } from '../breadcrumbview/breadcrumb-wrapper-view';
import DashboardConstants from '../../screens/homescreen/screens/contentscreen/screens/dashboardscreen/tack/dashboard-constants';
const DashboardTileButtons = DashboardConstants.kpiTileButton;
const KPITileViews = DashboardConstants.kpiTileView;

const oEvents = {
  HANDLE_DASHBOARD_TILE_PAGINATION_BUTTON_CLICKED : "handle_dashboard_tile_button_clicked",
  HANDLE_DASHBOARD_TILE_OPEN_DIALOG_BUTTON_CLICKED : "handle_dashboard_tile_toggle_dialog_visibility",
  HANDLE_DASHBOARD_TILE_SHOW_CONTENTS_BUTTON_CLICKED : "handle_dashboard_tile_show_contents_button_clicked",
  HANDLE_DASHBOARD_TILE_REFRESH_BUTTON_CLICKED: "handle_dashboard_tile_refresh_button_clicked",
  HANDLE_INVERT_CHART_BUTTON_CLICKED: "handle_invert_chart_button_clicked",
};

const oPropTypes = {
  headerLabel: ReactPropTypes.string,
  breadcrumbClickHandler: ReactPropTypes.func,
  breadCrumbData: ReactPropTypes.array,
  tileId : ReactPropTypes.string,
  hideButtons: ReactPropTypes.bool,
  showPreviousButton: ReactPropTypes.bool,
  showNextButton: ReactPropTypes.bool,
  showKpiContentExplorerButton: ReactPropTypes.bool,
  view: ReactPropTypes.string,
  openDialogButtonVisible: ReactPropTypes.bool,
  isKpiLoading: ReactPropTypes.bool,
  isKPIChartInvert: ReactPropTypes.bool,
};
/**
 * @class DashboardTileView - DashboardTileView used for displaying KPIs and Violations,
 * @memberOf Views
 * @property {string} [headerLabel] - Tile Label, Showing on top left of the tile.
 * @property {func} [breadcrumbClickHandler] - Executes when breadcrumb item is clicked.
 * @property {array} [breadCrumbData] - Breadcrumb data used for navigation.
 * @property {string} [tileId] - Contain unique id for tile(for example: KPI tile have KIP ID).
 * @property {bool} [hideButtons] - Used for toggle(hide/show) for Next and Previous buttons.(Next and Previous buttons are used for KPI pagination)
 * @property {bool} [showPreviousButton] - Used for display or hide previous button on tile(Displaying if true).
 * @property {bool} [showNextButton] - Used for displaying next button on tile(Displaying if true).
 * @property {bool} [showKpiContentExplorerButton] - Used for displaying KPI Content Explorer Button(Displaying if true).
 * @property {string} [view] - Contain mode of view.
 * @example view = normalView
 * @property {bool} [openDialogButtonVisible] - Used for displaying 'Open in viewer' button(Displaying if true).
 * @property {bool} [isKpiLoading] - Used for displaying loading screen on KPI tile(Displaying if true).
 * @property {bool} [isKPIChartInvert] - Contains true if KPI chart data is inverted, else false.
 */

/** Using decorator for HOC
 * here we using error boundary as HOC **/
// @React.ErrorBoundary
// @CS.SafeComponent
class DashboardTileView extends React.Component {
  static propTypes = oPropTypes;

  openKPIInDialogButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_DASHBOARD_TILE_OPEN_DIALOG_BUTTON_CLICKED, this.props.tileId);
  };

  handlePaginationButtonClicked = (sContext, sTileId) => {
    EventBus.dispatch(oEvents.HANDLE_DASHBOARD_TILE_PAGINATION_BUTTON_CLICKED, sContext, sTileId);
  };

  getKPIData = () => {
    let __props = this.props;
    return {
      kpiId: __props.tileId,
      kpiLabel: __props.headerLabel,
      breadCrumbData: __props.breadCrumbData
    };
  };

  handleShowContentsButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_DASHBOARD_TILE_SHOW_CONTENTS_BUTTON_CLICKED, this.getKPIData());
  };

  handleInvertChartButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_INVERT_CHART_BUTTON_CLICKED, this.getKPIData());
  };

  handleRefreshKPIButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_DASHBOARD_TILE_REFRESH_BUTTON_CLICKED, this.getKPIData());
  };

  getTileButtonsView = () => {
    let aButtonsView = [];
    let bShowPreviousButton = this.props.showPreviousButton;
    let bShowNextButton = this.props.showNextButton;
    if(!bShowPreviousButton && !bShowNextButton){
     return null;
    }
    let sTileId = this.props.tileId;
    let sPreviousButtonClassName = bShowPreviousButton ? "tileButton previous" : "tileButton previous disabled";
    let sNextButtonClassName = bShowNextButton ? "tileButton next" : "tileButton next disabled";

    aButtonsView.push(
        <TooltipView placement="bottom" label={getTranslation().PREVIOUS}>
          <div className={sPreviousButtonClassName}
               onClick={bShowPreviousButton ? this.handlePaginationButtonClicked.bind(this, DashboardTileButtons.PREVIOUS_BUTTON, sTileId) : null}>
          </div>
        </TooltipView>
    );

    aButtonsView.push(
        <TooltipView label={getTranslation().NEXT}>
          <div className={sNextButtonClassName}
               onClick={bShowNextButton ? this.handlePaginationButtonClicked.bind(this, DashboardTileButtons.NEXT_BUTTON, sTileId) : null}>
          </div>
        </TooltipView>
    );
    return aButtonsView;
  };

  getLoaderView = () => {
    let bIsKpiLoading = this.props.isKpiLoading;
    let bIsDataTileLoading = this.props.isLoading;
    let oLoadingSection = null;

    if(bIsKpiLoading || bIsDataTileLoading) {
      oLoadingSection = (
          <div className="loadingAnimationContainer">
            <Loader color="#26A65B"/>
          </div>
      );
    }

    return oLoadingSection;
  };

  getDashboardTileView = (sView) => {
    let sHeaderLabel = this.props.headerLabel;
    let aBreadcrumbPath = this.props.breadCrumbData;
    let sBreadcrumbContext = "dashboardTile"; //todo: hardcoded alert
    let fBreadcrumbClickHandler = this.props.breadcrumbClickHandler;

    let oOpenKpiContentExplorerButton = null;
    let oRefreshButton = null;
    let oInvertChart = null;
    if (this.props.showKpiContentExplorerButton) {
      oOpenKpiContentExplorerButton = (
          <TooltipView placement="bottom" label={getTranslation().EXPLORE_CONTENTS}>
            <div className="dashboardOpenKpiContentExplorerButton" onClick={this.handleShowContentsButtonClicked}></div>
          </TooltipView>
      );
      oRefreshButton = (
          <TooltipView placement="bottom" label={getTranslation().REFRESH}>
            <div className="dashboardKpiRefreshButton" onClick={this.handleRefreshKPIButtonClicked}></div>
          </TooltipView>
      )

      let sInvertChartIconClassName = this.props.isKPIChartInvert? "invertChartIcon invert" : "invertChartIcon";
      oInvertChart = (
          <TooltipView placement="bottom" label={getTranslation().SHOW_INVERTED_GRAPH}>
          <div className={sInvertChartIconClassName} onClick={this.handleInvertChartButtonClicked.bind(this)}></div>
          </TooltipView>
            )
    }

    let aToolbarView = [];
    aToolbarView.push(oInvertChart);
    !this.props.hideButtons && aToolbarView.push(this.getTileButtonsView());

    let oStyle = this.props.isLoading ? {
      minHeight: "300px"
    } : null;

    return (
        <div className="dashboardTile" style={oStyle}>
          {this.getLoaderView()}
          <div className="dashboardTileBreadcrumbContainer">
            <BreadcrumbView context={sBreadcrumbContext} breadcrumbPath={aBreadcrumbPath}
                            clickHandler={fBreadcrumbClickHandler} toolbarView={aToolbarView}/>
          </div>
          {sView === KPITileViews.NORMAL_VIEW && this.props.openDialogButtonVisibility ? (
              <TooltipView placement="bottom" label={getTranslation().OPEN_IN_VIEWER}>
                <div className="dashboardKPIDialogOpenButton" onClick={this.openKPIInDialogButtonClicked}/>
              </TooltipView>) : null}
          {oOpenKpiContentExplorerButton}
          {oRefreshButton}
          <div className="dashboardChartContainer">
            {this.props.children}
          </div>
        </div>
    )
  };

 /* getDashboardTileDialogView = () => {
    var oBodyStyle = {
      overflow: "hidden",
    };

    var oContentStyle = {//main dialog (container style)
      padding: "0",
      overflow: "hidden",
      width: '90%',
      maxWidth: "90%",
    };

    var aButtonData = [{
      id: "apply",
      label: getTranslation().OK,
      isFlat: false,
      isDisabled: false
    }];

    return (<CustomDialogView
        open={this.state.isDialogOpen}
        bodyStyle={oBodyStyle}
        contentStyle={oContentStyle}
        modal={true}
        buttonData={aButtonData}
        buttonClickHandler={this.openKPIInDialogButtonClicked}
        bodyClassName="KPIDialog"
        contentClassName="KPIDialogContentContainer"
        //onRequestClose={this.openKPIInDialogButtonClicked}
        title={this.props.headerLabel}>
      {this.getDashboardTileView("dialogView")}
    </CustomDialogView>)

  };*/

  render () {
    return (
        <div className="dashboardTileContainer">
          {this.getDashboardTileView(this.props.view)}
        </div>
    );
  }
}

export const view = DashboardTileView;
export const events = oEvents;
