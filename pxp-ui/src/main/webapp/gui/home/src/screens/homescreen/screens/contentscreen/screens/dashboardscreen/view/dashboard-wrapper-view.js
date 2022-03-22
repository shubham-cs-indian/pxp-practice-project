import CS from '../../../../../../../libraries/cs';
import React, { Suspense } from 'react';
import ReactPropTypes from 'prop-types';
import DashboardTabDictionary from './../tack/dashboard-tab-dictionary';
import { view as DashboardView } from '../../../../../../../viewlibraries/dashboardview/dashboard-view';
import { view as TabLayoutView } from '../../../../../../../viewlibraries/tablayoutview/tab-layout-view';
import EventBus from '../../../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations } from '../../../../../../../commonmodule/store/helper/translation-manager';
import {view as DataIntegrationLogsView} from "./data-integration-logs-view";
import {view as GridView} from '../../../../../../../viewlibraries/gridview/grid-view';
import {view as BackgroundProcessDialogView} from './background-processes-dialog-view';
import {view as ContextMenuView} from "../../../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new";
import ContextMenuViewModel
  from "../../../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model";

const TaskController = React.lazy(()=>import('../../../../../../../smartviewlibraries/taskview/controller/task-controller'));

let Events = {
  BGP_SERVICE_CHANGED: "BGP_service_changed"
};

const oPropTypes = {
  isDIDDialogVisible: ReactPropTypes.bool,
  dashboardTabList: ReactPropTypes.array,
  selectedModule: ReactPropTypes.string,
  processedDashboardDataGovernanceMap: ReactPropTypes.object,
  processedDashboardDataIntegrationMap: ReactPropTypes.object,
  leftScrollInfo: ReactPropTypes.object,
  selectedDashboardTabId: ReactPropTypes.string,
  selectedEndpoint: ReactPropTypes.string,
  taskDashboardView: ReactPropTypes.object,
  workFlowWorkBenchView: ReactPropTypes.object,
  runtimeMappingDialogView: ReactPropTypes.object,
  activeDashboardTileData: ReactPropTypes.object,
  runtimeView: ReactPropTypes.object,
  informationTabView: ReactPropTypes.object,
  damInformationTabView: ReactPropTypes.object,
  showModuleSidebar: ReactPropTypes.bool,
  KPIChartInvertData: ReactPropTypes.object,
  dataForDataIntegrationLogsView: ReactPropTypes.object,
  backgroundProcessTabData: ReactPropTypes.object,
  isBackgroundProcessDetailDialogOpen: ReactPropTypes.bool,
  backgroundProcessDialogData: ReactPropTypes.object,
};

// @CS.SafeComponent
class DashboardWrapperView extends React.Component {
  static propTypes = oPropTypes;

  componentDidMount = () => {
  };

  componentDidUpdate = () => {
  };

  getTaskDashboardView = () => {
    let oProps = this.props;
    let oTaskData = oProps.taskData;
    oTaskData.selectedDashboardTabId = oProps.selectedDashboardTabId;
    let oSelectedDashboardTab = CS.find(oProps.dashboardTabList, {id: oProps.selectedDashboardTabId});
    oTaskData.callbackData.breadCrumbData.label = getTranslations().DASHBOARD_TITLE + ":" + oSelectedDashboardTab.label;

    return (<div className="taskDashboardView">
      <Suspense fallback={<div></div>}>
        <TaskController
          data={oTaskData}
          context={"dashboardWorkflowWorkbenchTask"}
        />
      </Suspense>
    </div>);
  };

  handleBGPServiceChanged = (oItem) => {
    EventBus.dispatch(Events.BGP_SERVICE_CHANGED, oItem.id);
  }

  getBGPServiceSelectView = (aList, sSelectedService) => {
    let aListModels = [];

    CS.forEach(aList, function (oListItem) {
      if(sSelectedService !== oListItem.id) {
        aListModels.push(new ContextMenuViewModel(
            oListItem.id,
            CS.getLabelOrCode(oListItem),
            false,
            "",
            {context: ""}
        ));
      }
    });

    let sSelectedServiceLabel = !CS.isEmpty(aList) && CS.find(aList, {id: sSelectedService}).label;
    return (
       <div className={"BGPServiceSelectorWrapper"}>
         <div className={"BGPServiceLabel"}>
           {getTranslations().BGP_SERVICE}
         </div>
         <ContextMenuView
             onClickHandler={this.handleBGPServiceChanged}
             contextMenuViewModel={aListModels}
         >
           <div className='BGPServiceSelector'>
             <div className="buttonLabel">{sSelectedServiceLabel}</div>
             <div className="buttonIcon"></div>
           </div>
         </ContextMenuView>
       </div>
    );
  }

  getBackgroundProcessesDialogView = () => {
    return <BackgroundProcessDialogView
        backgroundProcessDialogData={this.props.backgroundProcessDialogData}
    />
  }

  getBackgroundProcessesView = () => {
    let oDialogView = this.props.isBackgroundProcessDetailDialogOpen ? this.getBackgroundProcessesDialogView() : null;
    let oData = this.props.backgroundProcessTabData;
    let oBGPServiceDropDown = this.getBGPServiceSelectView(oData.serviceList, oData.selectedService);
    let aGridData = oData.gridData;
    let oPaginationData = oData.paginationData;
    let iTotalCount = oData.totalItemsCount;
    let sSortBy = oData.sortBy;
    let sSortOrder = oData.sortOrder;
    let oSkeleton = oData.skeleton;

    return (
        <React.Fragment>
          <GridView
              skeleton={oSkeleton}
              data={aGridData}
              paginationData={oPaginationData}
              totalItems = {iTotalCount}
              sortBy = {sSortBy}
              sortOrder = {sSortOrder}
              showFilterView = {oData.showGridFilterView}
              customActionView={oBGPServiceDropDown}
              disableDeleteButton
              disableCreate
              enableFilterButton
              hideSearchBar
              disableColumnOrganizer
              disableResizable
          />
          {oDialogView}
        </React.Fragment>)
  };

  getViewToRenderForSelectedTab = () => {
    let oProps = this.props;
    let oProcessedDashboardDataGovernanceMap = oProps.processedDashboardDataGovernanceMap;
    let oProcessedDashboardDataIntegrationMap = oProps.processedDashboardDataIntegrationMap;
    let oLeftScrollInfo = oProps.leftScrollInfo;
    let sSelectedDashboardTabId = oProps.selectedDashboardTabId;
    let oActiveDashboardTileData = oProps.activeDashboardTileData;

    switch (sSelectedDashboardTabId) {
      case DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB:
        return this.getTaskDashboardView();

      case DashboardTabDictionary.BUCKETS_TAB:
      case DashboardTabDictionary.INFORMATION_TAB:
      case DashboardTabDictionary.DAM_TAB:
        return this.props.runtimeView;

      case DashboardTabDictionary.DATA_INTEGRATION_LOGS:
        return <DataIntegrationLogsView dataForDataIntegrationLogsView={this.props.dataForDataIntegrationLogsView}
                                        context={"dashboardScreen"}
        />;

      case DashboardTabDictionary.BACKGROUND_PROCESSES_TAB:
        return this.getBackgroundProcessesView();

      default:
        return (
            <DashboardView selectedDashboardTabId={sSelectedDashboardTabId}
                           dashboardDataGovernanceMap={oProcessedDashboardDataGovernanceMap}
                           dashboardDataIntegrationMap={oProcessedDashboardDataIntegrationMap}
                           leftScrollInfo={oLeftScrollInfo}
                           activeDashboardTileData={oActiveDashboardTileData}
                           openDialogButtonVisibility={true}
                           KPIChartInvertData={this.props.KPIChartInvertData}
            />
        );
    }
  };

  getViewToRender = (bRenderViewWithoutTabs) => {
    let oProps = this.props;
    let oViewToRender;
    let aDashboardTabList = oProps.dashboardTabList;
    if (bRenderViewWithoutTabs) {
      oViewToRender = this.getViewToRenderForSelectedTab();
    } else {
      let sSelectedModule = oProps.selectedDashboardTabId;
      oViewToRender = (
          <TabLayoutView activeTabId={sSelectedModule}
                         tabList={aDashboardTabList}
                         context={"dashboardScreen"}
                         addBorderToBody={false}>
            <div className="dashboardScreenContainer">
              {this.getViewToRenderForSelectedTab()}
            </div>
          </TabLayoutView>
      );
    }
    return oViewToRender;
  };

  render() {
    let bRenderViewWithoutTabs = (!this.props.showModuleSidebar && (this.props.selectedDashboardTabId === DashboardTabDictionary.BUCKETS_TAB));
    let oViewToRender = this.getViewToRender(bRenderViewWithoutTabs);
    let sDashboardContainerWrapper = "dashboardScreenController" ;
    sDashboardContainerWrapper += (this.props.isDIDDialogVisible || bRenderViewWithoutTabs) ? " withEndpointContainer" : " withoutEndpointContainer";
    return (
        <div className="dashboardScreenControllerWrapper">
          <div className={sDashboardContainerWrapper}>
            {oViewToRender}
            {this.props.runtimeMappingDialogView}
          </div>
        </div>);
  }
}

export const view = DashboardWrapperView;
export const events = Events;
