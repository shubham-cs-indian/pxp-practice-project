import CS from '../../../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as DashboardWrapperView } from '../view/dashboard-wrapper-view';
import SessionProps from '../../../../../../../commonmodule/props/session-props';
import DashboardTabDictionary from '../tack/dashboard-tab-dictionary';
import EntityProps from '../../../../../../../commonmodule/props/entity-props';
import DashboardScreenProps from "../store/model/dashboard-screen-props";
import JobProps from "../../../store/model/job-screen-view-props";

const oPropTypes = {
  store: ReactPropTypes.object.isRequired,
  action: ReactPropTypes.object.isRequired,
  taskDashboardView: ReactPropTypes.object,
  workFlowWorkBenchView: ReactPropTypes.object,
  runtimeMappingDialogView: ReactPropTypes.object,
  runtimeView: ReactPropTypes.object,
  preventDataReset: ReactPropTypes.bool,
  showModuleSidebar: ReactPropTypes.bool,
  userHasPimPermission: ReactPropTypes.bool,
  informationTabView: ReactPropTypes.object,
  damInformationTabView: ReactPropTypes.object,
};

// @CS.SafeComponent
class DashboardScreenController extends React.Component {
  //@required: Props
  static propTypes = oPropTypes;
  constructor(props) {
    super(props);
    if (!this.props.preventDataReset) {
      this.refreshAll();
    }
  }

  /*componentWillMount() {
    if (!this.props.preventDataReset) {
      this.refreshAll();
    }
  }*/

  //@Bind: Store with state
  componentDidMount() {
    this.debouncedStateChanged = CS.debounce(this.contentStateChanged, 10);
    this.props.action.registerEvent();
    this.getStore().bind('dashboard-changed', this.debouncedStateChanged);
  }

  //@UnBind: store with state
  componentWillUnmount() {
    this.getStore().unbind('dashboard-changed', this.debouncedStateChanged);
    this.props.action.deRegisterEvent();
  }

  //@set: state
  contentStateChanged = () => {
    let changedState = {
      appData: this.getStore().getData().appData,
      componentProps: this.getStore().getData().componentProps,
    };

    this.setState(changedState);
  };

  refreshAll = () => {
    let oStore = this.getStore();
    oStore.resetAll();
    oStore.refreshAll();
  };

  getStore = () => {
    return this.props.store;
  };

  getComponentProps = () => {
    return this.state.componentProps;
  };

  getAppData = () => {
    return this.state.appData;
  };

  state = {
    appData: this.getStore().getData().appData,
    componentProps: this.getStore().getData().componentProps,
  };

  render() {
    let oComponentProps = this.getComponentProps();
    let bIsDIDialogVisible = oComponentProps.getIsDIDialogVisible();
    let aDashboardTabList = oComponentProps.getDashboardTabsList();
    if (!this.props.userHasPimPermission) {
      aDashboardTabList = CS.reject(aDashboardTabList, {id: DashboardTabDictionary.BUCKETS_TAB});
      aDashboardTabList = CS.reject(aDashboardTabList, {id: DashboardTabDictionary.DAM_TAB});
      aDashboardTabList = CS.reject(aDashboardTabList, {id: DashboardTabDictionary.DATA_INTEGRATION_LOGS});
    }
    if(!CS.includes(EntityProps.getAllEntityIds(), "AssetInstance")){
      aDashboardTabList = CS.reject(aDashboardTabList, {id: DashboardTabDictionary.DAM_TAB});
    }
    let sSelectedModule = oComponentProps.getSelectedDashboardTabId();
    let oProcessedDashboardDataGovernanceMap = oComponentProps.getProcessedDashboardDataGovernanceMap();
    let oProcessedDashboardDataIntegrationMap = oComponentProps.getProcessedDashboardDataIntegrationMap();
    let oLeftScrollInfo = oComponentProps.getLeftScrollData();
    let sSelectedDashboardTabId = oComponentProps.getSelectedDashboardTabId();
    let sEndpointId = SessionProps.getSessionEndpointId();
    let oActiveDashboardTileData = oComponentProps.getActiveDashboardTileObject();
    let oSelectedEndpoint = {};
    let sLabel = "";
    let oKPIChartInvert = oComponentProps.getKPIChartInvertData();
    if (sEndpointId && sEndpointId != "null") {
      oSelectedEndpoint = CS.find(oProcessedDashboardDataIntegrationMap, {id: sEndpointId});
      sLabel = oSelectedEndpoint.label;
    }
    let oDataForDataIntegrationLogsView = DashboardScreenProps.getDataIntegrationLogsViewData();
    oDataForDataIntegrationLogsView.activeJob = JobProps.getActiveJob();
    oDataForDataIntegrationLogsView.activeJobGraphData = JobProps.getActiveJobGraphData();

    let oDataForBackgroundProcessTab = {
      gridData: DashboardScreenProps.getGridViewData(),
      totalItemsCount: DashboardScreenProps.getGridViewTotalItems(),
      paginationData: DashboardScreenProps.getGridViewPaginationData(),
      sortBy: DashboardScreenProps.getGridViewSortBy(),
      sortOrder: DashboardScreenProps.getGridViewSortOrder(),
      showGridFilterView: DashboardScreenProps.getIsGridFilterView(),
      skeleton: DashboardScreenProps.getGridViewSkeleton(),
      serviceList: DashboardScreenProps.getServiceList(),
      selectedService: DashboardScreenProps.getSelectedBGPService()
    };
    let bIsBackgroundProcessDetailDialogOpen = DashboardScreenProps.getIsBackgroundProcessDetailDialogOpen();
    let oBackgroundProcessDialogData = DashboardScreenProps.getBackgroundProcessDialogData();

    return (<DashboardWrapperView isDIDDialogVisible={bIsDIDialogVisible}
                                  dashboardTabList={aDashboardTabList}
                                  selectedModule={sSelectedModule}
                                  processedDashboardDataGovernanceMap={oProcessedDashboardDataGovernanceMap}
                                  processedDashboardDataIntegrationMap={oProcessedDashboardDataIntegrationMap}
                                  leftScrollInfo={oLeftScrollInfo}
                                  selectedDashboardTabId={sSelectedDashboardTabId}
                                  runtimeView={this.props.runtimeView}
                                  showModuleSidebar={this.props.showModuleSidebar}
                                  selectedEndpoint={sLabel}
                                  runtimeMappingDialogView={this.props.runtimeMappingDialogView}
                                  activeDashboardTileData={oActiveDashboardTileData}
                                  informationTabView={this.props.informationTabView}
                                  damInformationTabView={this.props.damInformationTabView}
                                  KPIChartInvertData={oKPIChartInvert}
                                  taskData={this.props.taskData}
                                  dataForDataIntegrationLogsView={oDataForDataIntegrationLogsView}
                                  backgroundProcessTabData={oDataForBackgroundProcessTab}
                                  isBackgroundProcessDetailDialogOpen={bIsBackgroundProcessDetailDialogOpen}
                                  backgroundProcessDialogData = {oBackgroundProcessDialogData}
    />);
  }
}

export default DashboardScreenController;
