import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations } from '../../commonmodule/store/helper/translation-manager.js';
import { view as DashboardTileView } from './dashboard-tile-view';
import { view as DashboardEndpointTileView } from '../../screens/homescreen/screens/contentscreen/screens/dashboardscreen/view/dashboard-endpoint-tile-view';
import { view as ChartJsChartView } from '../../screens/homescreen/screens/contentscreen/screens/dashboardscreen/view/chartjs-chart-view';
import { view as CustomDialogView } from '../customdialogview/custom-dialog-view';
import DashboardConstants from '../../screens/homescreen/screens/contentscreen/screens/dashboardscreen/tack/dashboard-constants';
import { view as KpiSummaryTileView } from './../kpisummaryview/kpi-summary-tile-view';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import CommonUtils from "../../commonmodule/util/common-utils";
const KPITileViews = DashboardConstants.kpiTileView;

const oEvents = {
  DASHBOARD_VIEW_BAR_GROUP_CLICKED: 'dashboard_view_bar_group_clicked',
  DASHBOARD_VIEW_BREAD_CRUMB_CLICKED: 'dashboard_view_bread_crumb_clicked',
  HANDLE_LOAD_MORE_CLICKED: "handle_load_more_clicked",
  HANDLE_DASHBOARD_VIEW_CLOSE_DIALOG_CLICKED: "handle_dashboard_view_close_dialog_clicked"
};

const oPropTypes = {
  selectedDashboardTabId: ReactPropTypes.string,
  gridLayout: ReactPropTypes.array,
  dashboardDataGovernanceMap: ReactPropTypes.object,
  dashboardDataIntegrationMap: ReactPropTypes.object,
  leftScrollInfo: ReactPropTypes.object,
  activeDashboardTileData: ReactPropTypes.object,
  isKPISummaryView: ReactPropTypes.bool,
  openDialogButtonVisibility: ReactPropTypes.bool,
  KPIChartInvertData: ReactPropTypes.object,
  autoHeight: ReactPropTypes.bool
};
/**
 * @class DashboardView - use for display PERFORMANCE INDICES in Dashboard.
 * @memberOf Views
 * @property {string} [selectedDashboardTabId] - id pf dashboard tab.
 * @property {array} [gridLayout] -  array of gridlayout.
 * @property {object} [dashboardDataGovernanceMap] -  object of dashboardDataGovernanceMap[breadCrumbs, chartData, chartOptions, chartType, id,label,showNextButton,showPreviousButton]
 * @property {object} [dashboardDataIntegrationMap] - ass an object of dashboardDataIntegrationMap.
 * @property {object} [leftScrollInfo] - left scroll info.
 * @property {object} [activeDashboardTileData] - activeDashboardTileData.
 * @property {bool} [isKPISummaryView] bool type for isKPISummaryView or not.
 * @property {bool} [openDialogButtonVisibility] bool value for openDialogButtonVisibility or not.
 * @property {object} [KPIChartInvertData] -  object of KPIChartInvertData.
 */


// @CS.SafeComponent
class DashboardView extends React.Component {
  constructor(props) {
    super(props);
    this.dashboardDataGovernanceTilesContainer = React.createRef();
    this.dashboardDataIntegrationTilesContainer = React.createRef();
    this.dashboardDataIntegrationSectionContainer = React.createRef();
    this.leftScrollButtonDataIntegration = React.createRef();
    this.leftScrollButtonDataGovernance = React.createRef();
    this.dashboardDataGovernanceSectionContainer = React.createRef();
    this.scrollContainer = React.createRef();
  }

  static propTypes = oPropTypes;
  rowHeight = 60;
  iWidthDifferenceForDataGovernance = 0; /** difference in width of tileContainer and tileList */
  iWidthDifferenceForDataIntegration = 0; /** difference in width of tileContainer and tileList */

componentDidMount = () => {
    this.handleScrollButtonVisibility();
};

    componentDidUpdate = () => {
    this.handleScrollButtonVisibility();
    let oLeftScrollInfo = this.props.leftScrollInfo;
    if (oLeftScrollInfo && oLeftScrollInfo.dataGovernanceLeftScroll) {
        let oTilesListDOM = this.dashboardDataGovernanceTilesContainer.current;
      CommonUtils.scrollBy(oTilesListDOM.parentElement, {left: oLeftScrollInfo.dataGovernanceLeftScroll}, "");
        //$(oTilesListDOM).animate({left: oLeftScrollInfo.dataGovernanceLeftScroll}, 300);
    }

    if (oLeftScrollInfo && oLeftScrollInfo.dataIntegrationLeftScroll) {
        let oTilesListDOM = this.dashboardDataIntegrationTilesContainer.current;
        // $(oTilesListDOM).animate({left: oLeftScrollInfo.dataIntegrationLeftScroll}, 300);
      CommonUtils.scrollBy(oTilesListDOM.parentElement, {left: oLeftScrollInfo.dataIntegrationLeftScroll}, "");
    }
};

/*
  onLayoutChange = (layout) => {
    //nothing yet.
  };
*/

  handleCategoryClickedToDrillDown = (sId, oEvent, aChartElements) => {
    let oChartElement = aChartElements[0];
    if (oChartElement && oChartElement._model.label) {
      EventBus.dispatch(oEvents.DASHBOARD_VIEW_BAR_GROUP_CLICKED, sId, oChartElement._model.label, oChartElement._index);
    }
  };

  handleBreadcrumbItemClicked = (sKpiId, oItem) => {
    EventBus.dispatch(oEvents.DASHBOARD_VIEW_BREAD_CRUMB_CLICKED, sKpiId, oItem.id);
    //todo: probably can use the same event for drill down as well as breadcrumb navigation. think and decide. :P
  };

  closeDashboardTileDialogClicked = (sTileId) => {
    EventBus.dispatch(oEvents.HANDLE_DASHBOARD_VIEW_CLOSE_DIALOG_CLICKED, sTileId);
  };

  handleScrollButtonClicked = (bScrollLeft, sContext) => {
    let iScrollByWidth = 420; /** HARDCODED ALERT */
    let oTilesListDOM = sContext == "dataGovernance" ? this.dashboardDataGovernanceTilesContainer.current:
            this.dashboardDataIntegrationTilesContainer.current;
    let iCurrentLeft = oTilesListDOM.parentElement.scrollLeft;
    let iNewLeft = 0;
    if (bScrollLeft) {
      if(iCurrentLeft) {
        iNewLeft = -(iScrollByWidth);
      }
    } else {
      let iNewLeftWidth = oTilesListDOM.clientWidth - (iCurrentLeft + oTilesListDOM.parentElement.clientWidth);
      let iWidthDifference = sContext == "dataGovernance" ? this.iWidthDifferenceForDataGovernance: this.iWidthDifferenceForDataIntegration;
      if (Math.ceil(iCurrentLeft) - iWidthDifference == 0) {
        iNewLeft += iScrollByWidth;
        EventBus.dispatch(oEvents.HANDLE_LOAD_MORE_CLICKED, sContext, iNewLeft);
      } else if(iNewLeftWidth > 1){ /** left cannot be greater than iWidthDifference */
       iNewLeft += iScrollByWidth;
      }
    }
    let fun = this.handleScrollButtonVisibility.bind(this, sContext);
    // $(oTilesListDOM).animate({left: iNewLeft}, 900, fun);
    CommonUtils.scrollBy(oTilesListDOM.parentElement, {left: iNewLeft}, "", fun);
  };

  handleScrollButtonVisibility = (sContext) => {
    //todo: may have to be called on window resize!
    try{
      if (sContext) {
        let oTabsSectionDOM = sContext == "dataGovernance" ? this.dashboardDataGovernanceSectionContainer.current:
          this.dashboardDataIntegrationSectionContainer.current;
        let iTabsSectionWidth = oTabsSectionDOM.offsetWidth;
        let oTabsListDOM = sContext == "dataGovernance" ? this.dashboardDataGovernanceTilesContainer.current:
          this.dashboardDataIntegrationTilesContainer.current;
        let iTabsListWidth = oTabsListDOM.offsetWidth;

        let oLeftScrollButton = sContext == "dataGovernance" ? this.leftScrollButtonDataGovernance.current:
          this.leftScrollButtonDataIntegration.current;
        oLeftScrollButton.style.visibility = "hidden";

        if (iTabsListWidth > iTabsSectionWidth) {
          this.iWidthDifference = iTabsListWidth - iTabsSectionWidth;
          /** all possible 'left' values of the oTabsListDOM will lie between 0px and -(iWidthDifference)px */
          let iTabListLeft = oTabsListDOM.parentElement.scrollLeft;
          if (Math.abs(iTabListLeft)) {
            //left button visible
            oLeftScrollButton.style.visibility = "visible";
          }
        }
      } else {
        let oDataGovernanceTilesSectionDOM = this.dashboardDataGovernanceSectionContainer.current;
        let oDataIntegrationTilesSectionDOM = this.dashboardDataIntegrationSectionContainer.current;

        if (oDataGovernanceTilesSectionDOM) {
          let iDataGovernanceTilesSectionWidth = oDataGovernanceTilesSectionDOM.offsetWidth;
          let oDataGovernanceTilesListDOM = this.dashboardDataGovernanceTilesContainer.current;
          let iDataGovernanceTilesListWidth = oDataGovernanceTilesListDOM.offsetWidth;
          let oLeftScrollButtonDataGovernance = this.leftScrollButtonDataGovernance.current;
          oLeftScrollButtonDataGovernance.style.visibility = "hidden";
          if (iDataGovernanceTilesListWidth > iDataGovernanceTilesSectionWidth) {
            this.iWidthDifferenceForDataGovernance = iDataGovernanceTilesListWidth - iDataGovernanceTilesSectionWidth;
            /** all possible 'left' values of the oDataGovernanceTilesListDOM will lie between 0px and -(iWidthDifferenceForDataGovernance)px */
            let iTilesListLeft = oDataGovernanceTilesListDOM.parentElement.scrollLeft;
            if (Math.abs(iTilesListLeft)) {
              //left button visible
              oLeftScrollButtonDataGovernance.style.visibility = "visible";
            }
          }
        }

        if (oDataIntegrationTilesSectionDOM) {
          let iDataIntegrationTilesSectionWidth = oDataIntegrationTilesSectionDOM.offsetWidth;
          let oDataIntegrationTilesListDOM = this.dashboardDataIntegrationTilesContainer.current;
          let iDataIntegrationTilesListWidth = oDataIntegrationTilesListDOM.offsetWidth;
          let oLeftScrollButtonDataIntegration = this.leftScrollButtonDataIntegration.current;
          oLeftScrollButtonDataIntegration.style.visibility = "hidden";
          if (iDataIntegrationTilesListWidth > iDataIntegrationTilesSectionWidth) {
            this.iWidthDifferenceForDataIntegration = iDataIntegrationTilesListWidth - iDataIntegrationTilesSectionWidth;
            /** all possible 'left' values of the oDataIntegrationTilesListDOM will lie between 0px and -(iWidthDifferenceForDataIntegration)px */
            let iTilesListLeft = oDataIntegrationTilesListDOM.offsetLeft;
            if (Math.abs(iTilesListLeft)) {
              //left button visible
              oLeftScrollButtonDataIntegration.style.visibility = "visible";
            }
          }
        }
      }
    }catch (oException) {
      ExceptionLogger.error("Something went wrong in 'handleScrollButtonVisibility' (file - dashboard-view): ");
      ExceptionLogger.error(oException);
    }
  };

  getDashboardTileDialogView = () => {
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
      label: getTranslations().OK,
      isFlat: false,
      isDisabled: false
    }];
    let oActiveTileData = this.props.activeDashboardTileData;

    return (<CustomDialogView
        open={true}
        bodyStyle={oBodyStyle}
        contentStyle={oContentStyle}
        modal={true}
        buttonData={aButtonData}
        onRequestClose={this.closeDashboardTileDialogClicked.bind(this, oActiveTileData.id)}
        buttonClickHandler={this.closeDashboardTileDialogClicked.bind(this, oActiveTileData.id)}
        bodyClassName="KPIDialog"
        contentClassName="KPIDialogContentContainer"
        //onRequestClose={this.openKPIInDialogButtonClicked}
        title={CS.getLabelOrCode(oActiveTileData)}>
      {this.getDashboardTileView(oActiveTileData, KPITileViews.DIALOG_VIEW)}
    </CustomDialogView>)

  };

  getDashboardTileView = (oTileData, sView) => {
    let sKpiId = oTileData.id;
    let sLabel = CS.getLabelOrCode(oTileData);
    let oChartData = oTileData.chartData;
    let sChartType = oTileData.chartType;
    let oChartOptions = oTileData.chartOptions;
    let aBreadCrumbsData = oTileData.breadCrumbs;
    oChartOptions.onClick = this.handleCategoryClickedToDrillDown.bind(this, sKpiId);
    let oStyle = {};
    if (sView === KPITileViews.DIALOG_VIEW) {
      oStyle = {
        "height": "100%"
      };
    }

    let bIsKPIChartInvert = this.props.KPIChartInvertData && this.props.KPIChartInvertData[sKpiId];

    return  (
        <div key={sKpiId} style={oStyle}>
          <DashboardTileView headerLabel={sLabel}
                             showKpiContentExplorerButton={!oTileData.hideButtons}
                             breadCrumbData={aBreadCrumbsData}
                             breadcrumbClickHandler={this.handleBreadcrumbItemClicked.bind(this, sKpiId)}
                             tileId={sKpiId}
                             hideButtons={oTileData.hideButtons || false}
                             showPreviousButton={oTileData.showPreviousButton}
                             showNextButton={oTileData.showNextButton}
                             view={sView}
                             isKpiLoading={oTileData.isKpiLoading}
                             openDialogButtonVisibility={this.props.openDialogButtonVisibility}
                             isKPIChartInvert={bIsKPIChartInvert}>
            <ChartJsChartView chartData={oChartData}
                              chartType={sChartType}
                              chartOptions={oChartOptions}/>
          </DashboardTileView>
        </div>
    );
  };

  getDataGovernanceTiles = () => {
    let _this = this;
    let oDataMap = this.props.dashboardDataGovernanceMap;

    let aTileViews = [];

    if (this.props.isKPISummaryView) {
      CS.forEach(oDataMap.kpiList, function (oKpiData) {
        oDataMap.selectedKPIId = oKpiData.id;
        oDataMap.view = KPITileViews.NORMAL_VIEW;
        aTileViews.push(
            <div className="kpiSummaryTile">
              <KpiSummaryTileView {...oDataMap}/>
            </div>);
      });
    }
    else {
      CS.forEach(oDataMap, function (oTileData) {
        aTileViews.push(_this.getDashboardTileView(oTileData, KPITileViews.NORMAL_VIEW));
      });
    }

    let oView = (
        <div className="dashboardViewSection">
        <div className="dashboardSectionContainer" >
          <div className="dashboardSectionHeader">
            {getTranslations().PERFORMANCE_INDICES}
          </div>
          <div className="dashboardTilesContainerWrapper" ref={this.dashboardDataGovernanceSectionContainer}>
            <div className="scrollButton left" ref={this.leftScrollButtonDataGovernance}
                 onClick={this.handleScrollButtonClicked.bind(this, true, "dataGovernance")}></div>
            <div className="parentScrollContainer" ref={this.scrollContainer}>
              <div className="dashboardTilesContainer" ref={this.dashboardDataGovernanceTilesContainer}>
                {aTileViews}
              </div>
            </div>
            <div className="scrollButton right"
                 onClick={this.handleScrollButtonClicked.bind(this, false, "dataGovernance")}></div>
          </div>
        </div>
        </div>
    );
    return oView;
  };

  getDataIntegrationTiles = () => {
    let oDataMap = this.props.dashboardDataIntegrationMap;

    let aTileViews = [];

    CS.forEach(oDataMap, function (oTile) {
      let sId = oTile.id;
      let sLabel = CS.getLabelOrCode(oTile);
      let sType = oTile.type;
      let sMode = oTile.mode;
      let oTileData = oTile.tileData;
      let sIcon = oTile.iconKey;
      let sPhysicalCatalogId = oTile.physicalCatalogId;
      let bIsUploadEnableForCurrentUser = oTile.isUploadEnableForCurrentUser;

      aTileViews.push(
          <div key={sId}>
            <DashboardEndpointTileView
                endpointId={sId}
                endpointLabel={sLabel}
                endpointType={sType}
                tileData={oTileData}
                tileMode={sMode}
                tileIcon={sIcon}
                isEndpointLoading={oTile.isEndpointLoading}
                physicalCatalogId = {sPhysicalCatalogId}
                isUploadEnableForCurrentUser={bIsUploadEnableForCurrentUser}
            />
          </div>
      );
    });
    let oView = (
        <div className="dashboardViewSection">
          <div className="dashboardSectionContainer">
            <div className="dashboardSectionHeader">
              {getTranslations().ENDPOINTS}
            </div>
            <div className="dashboardTilesContainerWrapper" ref={this.dashboardDataIntegrationSectionContainer}>
              <div className="scrollButton left" ref={this.leftScrollButtonDataIntegration}
                   onClick={this.handleScrollButtonClicked.bind(this, true, "dataIntegration")}></div>
              <div className="parentScrollContainer" ref={this.scrollContainer}>
                <div className="dashboardTilesContainer" ref={this.dashboardDataIntegrationTilesContainer}>
                  {aTileViews}
                </div>
              </div>
              <div className="scrollButton right"
                   onClick={this.handleScrollButtonClicked.bind(this, false, "dataIntegration")}></div>
            </div>
          </div>
        </div>
    );
    return oView;
  };

  render() {

    return (
        <div className="dashboardView">
          {!CS.isEmpty(this.props.dashboardDataGovernanceMap) ? this.getDataGovernanceTiles() : null}
          {!CS.isEmpty(this.props.dashboardDataIntegrationMap) ? this.getDataIntegrationTiles() : null}
          {!CS.isEmpty(this.props.activeDashboardTileData) ? this.getDashboardTileDialogView() : null}
        </div>
    );
  }
}

export const view = DashboardView;
export const events = oEvents;
