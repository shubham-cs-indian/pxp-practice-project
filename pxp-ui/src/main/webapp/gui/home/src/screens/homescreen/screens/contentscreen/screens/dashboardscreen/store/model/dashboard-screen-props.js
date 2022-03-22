import CS from '../../../../../../../../libraries/cs';
import DashboardTabDictionary from './../../tack/dashboard-tab-dictionary';
import { getTranslations as getTranslation } from '../../../../../../../../commonmodule/store/helper/translation-manager';
import SessionProps from '../../../../../../../../commonmodule/props/session-props';

let DashboardScreenProps = (function () {

  let Props = function () {

    return {
      dashboardGridLayout: [],
      processedDashboardGovernanceDataMap: {},
      processedDashboardIntegrationDataMap: {},
      dashboardTabList: [],
      previousPhysicalCatalog: '',
      isDIDialogVisible: false,
      selectedDashboardTabId: '',
      dataGovernancePaginationData: {
        from: 0,
        size: 10
      },
      dataIntegrationPaginationData: {
        from: 0,
        size: 10
      },
      leftScrollData: {
        dataGovernanceLeftScroll: 0,
        dataIntegrationLeftScroll: 0
      },
      dashboardTileMasterData: [],
      activeDashboardTileObject: {},
      KPIChartInvertData: {},

      dataIntegrationLogsViewData:{
        isEndpointSelected:true,
        isWorkflowSelected:false,
        isPhysicalCatalogSelected:false,
        selectedEndpoints:[],
        selectedWorkflows:[],
        selectedPhysicalCatalogs:[],
        selectedUsers:[],
        selectedUserIids:[],
        selectedMessageTypes:[],
        timeStampData:{
          isUserDateRangeApplied:false,
          userDateRangeStartDate: "",
          userDateRangeEndDate: ""
        },
        jobList: [],
        isMessageDialogOpen: false,
        isProcessInstanceDialogOpen: false,
        processInstanceId: 0,
        instanceIID: 0
      },
      gridViewData: {},
      isBackgroundProcessDetailDialogOpen: false,
      backgroundProcessDialogData: {},
      gridViewPaginationData: {
        from: 0,
        pageSize: 20
      },
      gridViewTotalItems: 0,
      gridViewSortBy: "",
      gridViewSortOrder: "",
      isGridFilterView: false,
      selectedUserIds: [],
      gridViewSkeleton: {},
      userList: {},
      serviceList: [],
      BGPService: "",
      isUploadEnableForCurrentUser: false,
    }
  };

  let oProperties = new Props();

  return {

    getLeftScrollData: function () {
      return oProperties.leftScrollData
    },

    getDataGorvernanceTilesPaginationData: function () {
      return oProperties.dataGovernancePaginationData;
    },

    getDataIntegrationTilesPaginationData: function () {
      return oProperties.dataIntegrationPaginationData;
    },

    getDashboardGridLayout: function () {
      return oProperties.dashboardGridLayout;
    },

    setDashboardGridLayout: function (_aDashboardGridLayout) {
      oProperties.dashboardGridLayout = _aDashboardGridLayout;
    },

    getProcessedDashboardDataGovernanceMap: function () {
      return oProperties.processedDashboardGovernanceDataMap;
    },

    setProcessedDashboardDataGovernanceMap: function (_oProcessedDashboardDataMap) {
      oProperties.processedDashboardGovernanceDataMap = _oProcessedDashboardDataMap;
    },

    getProcessedDashboardDataIntegrationMap: function () {
      return oProperties.processedDashboardIntegrationDataMap;
    },

    setProcessedDashboardDataIntegrationMap: function (_oProcessedDashboardDataMap) {
      oProperties.processedDashboardIntegrationDataMap = _oProcessedDashboardDataMap;
    },

    getDashboardTabsList: function () {
      return oProperties.dashboardTabList;
    },

    setDashboardTabsList: function (aDashboardTabs, bIsUserReadOnly) {
      if (bIsUserReadOnly) {
        CS.remove(aDashboardTabs, {id: DashboardTabDictionary.DATA_INTEGRATION_TAB});
      }

      oProperties.dashboardTabList = aDashboardTabs;
      if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.INFORMATION_TAB})) {
        let oDashboardTab = {
          id: DashboardTabDictionary.INFORMATION_TAB,
          label: getTranslation().INFORMATION_TAB,
        };
        oProperties.dashboardTabList.unshift(oDashboardTab);
      }
      if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.DAM_TAB})) {
        let oDashboardTab = {
          id: DashboardTabDictionary.DAM_TAB,
          label: getTranslation().DAM_TAB
        };
        oProperties.dashboardTabList.push(oDashboardTab);
      }
      /*if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.TASK_DASHBOARD})) {
        let oTaskDashboardTab = {
          id: DashboardTabDictionary.TASK_DASHBOARD,
          label: getTranslation().TASKS,
        };
        oProperties.dashboardTabList.push(oTaskDashboardTab);
      }*/
      //TODO: Remove after backend implementation
      else if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB}) &&
          !bIsUserReadOnly) {
        let oTaskDashboardTab = {
          id: DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB,
          label: getTranslation().WORKFLOW_WORK_BENCH,
        };
        oProperties.dashboardTabList.push(oTaskDashboardTab);
      }
      if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.BUCKETS_TAB}) && !bIsUserReadOnly) {
        let oBucketsTab = {
          id: DashboardTabDictionary.BUCKETS_TAB,
          label: getTranslation().MATCH_AND_MERGE,
        };
        oProperties.dashboardTabList.push(oBucketsTab);
      }
      //TODO: Remove after backend implementation
      // else if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB})) {
      if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB}) && !bIsUserReadOnly) {
        let oTaskDashboardTab = {
          id: DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB,
          label: getTranslation().WORKFLOW_WORK_BENCH,
        };
        oProperties.dashboardTabList.push(oTaskDashboardTab);
      }

      if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.DATA_INTEGRATION_LOGS}) && !bIsUserReadOnly) {
        let oDashboardDataIntegrationLogsTab = {
          id: DashboardTabDictionary.DATA_INTEGRATION_LOGS,
          label: getTranslation().DATA_INTEGRATION_LOGS,
        };
        oProperties.dashboardTabList.push(oDashboardDataIntegrationLogsTab);
      }

      if (!CS.find(aDashboardTabs, {id: DashboardTabDictionary.BACKGROUND_PROCESSES_TAB})) {
        let oBackgroundProcessDashboardTab = {
          id: DashboardTabDictionary.BACKGROUND_PROCESSES_TAB,
          label: getTranslation().BACKGROUND_PROCESSES,
        };
        oProperties.dashboardTabList.push(oBackgroundProcessDashboardTab);
      }
    },

    getGridViewData: function () {
      return oProperties.gridViewData;
    },

    setGridViewData: function (_gridViewData) {
      oProperties.gridViewData = _gridViewData;
    },

    getIsBackgroundProcessDetailDialogOpen: function () {
      return oProperties.isBackgroundProcessDetailDialogOpen;
    },
    setIsBackgroundProcessDetailDialogOpen: function (_isBackgroundProcessDetailDialogOpen) {
      oProperties.isBackgroundProcessDetailDialogOpen = _isBackgroundProcessDetailDialogOpen;
    },

    getBackgroundProcessDialogData: function () {
      return oProperties.backgroundProcessDialogData;
    },

    setBackgroundProcessDialogData: function (_backgroundProcessDialogData) {
      oProperties.backgroundProcessDialogData = _backgroundProcessDialogData
    },

    setGridViewTotalItems: function (_gridViewTotalItems) {
      oProperties.gridViewTotalItems = _gridViewTotalItems;
    },

    getGridViewTotalItems: function () {
      return oProperties.gridViewTotalItems;
    },

    setGridViewPaginationData: function (_gridViewPaginationData) {
      oProperties.gridViewPaginationData = _gridViewPaginationData;
    },

    getGridViewPaginationData: function () {
      return oProperties.gridViewPaginationData;
    },

    setGridViewSortBy: function (_gridViewSortBy) {
      oProperties.gridViewSortBy = _gridViewSortBy;
    },

    getGridViewSortBy: function () {
      return oProperties.gridViewSortBy;
    },

    setGridViewSortOrder: function (_gridViewSortOrder) {
      oProperties.gridViewSortOrder = _gridViewSortOrder;
    },

    getGridViewSortOrder: function () {
      return oProperties.gridViewSortOrder;
    },

    setIsGridFilterView: function (_bIsGridFilterView) {
      oProperties.isGridFilterView = _bIsGridFilterView;
    },

    getIsGridFilterView: function () {
      return oProperties.isGridFilterView;
    },

    setSelectedUserIds: function (_aSelectedUserIds) {
      oProperties.selectedUserIds = _aSelectedUserIds;
    },

    getSelectedUserIds: function () {
      return oProperties.selectedUserIds;
    },

    setGridViewSkeleton: function (_oGridViewSkeleton) {
      oProperties.gridViewSkeleton = _oGridViewSkeleton;
    },

    getGridViewSkeleton: function () {
      return oProperties.gridViewSkeleton;
    },

    setUserList: function (_oUserList) {
      oProperties.userList = _oUserList
    },

    getUSerList: function(){
      return oProperties.userList
    },

    setServiceList: function (_serviceList) {
      oProperties.serviceList = _serviceList
    },

    getServiceList: function(){
      return oProperties.serviceList
    },

    setSelectedBGPService: function (_BGPService) {
      oProperties.BGPService = _BGPService
    },

    getSelectedBGPService: function(){
      return oProperties.BGPService
    },

    getPreviousPhysicalCatalog: function () {
      return oProperties.previousPhysicalCatalog;
    },

    setPreviousPhysicalCatalog: function (_previousPhysicalCatalog) {
      SessionProps.setSessionPreviousPhysicalCatalogId(_previousPhysicalCatalog);
      return oProperties.previousPhysicalCatalog = _previousPhysicalCatalog;
    },

    getIsDIDialogVisible: function () {
      return oProperties.isDIDialogVisible;
    },

    setIsDIDialogVisible: function (_isDIDialogVisible) {
      return oProperties.isDIDialogVisible = _isDIDialogVisible;
    },

    setSelectedDashboardTabId: function (_sId) {
      oProperties.selectedDashboardTabId = _sId;
    },

    getSelectedDashboardTabId: function () {
      return oProperties.selectedDashboardTabId;
    },

    setDashboardTileMasterData: function (_dashboardTileMasterData) {
      oProperties.dashboardTileMasterData = _dashboardTileMasterData;
    },

    getDashboardTileMasterData: function () {
      return oProperties.dashboardTileMasterData;
    },

    setActiveDashboardTileObject: function (_activeDashboardTileObjec) {
      oProperties.activeDashboardTileObject = _activeDashboardTileObjec;
    },

    getActiveDashboardTileObject: function () {
      return oProperties.activeDashboardTileObject;
    },

    setKPIChartInvertData: function (oKPIInvert) {
      oProperties.KPIChartInvertData = oKPIInvert;
    },

    getDataIntegrationLogsViewData: function () {
      return oProperties.dataIntegrationLogsViewData;
    },

    setDataIntegrationLogsViewData: function (_dataIntegrationLogsViewData) {
      oProperties.dataIntegrationLogsViewData = _dataIntegrationLogsViewData;
    },

    getKPIChartInvertData: function () {
      return oProperties.KPIChartInvertData;
    },

    resetDataIntegrationLogsViewData: function () {
      oProperties.dataIntegrationLogsViewData = {
        isEndpointSelected: true,
        isWorkflowSelected: false,
        selectedEndpoints: [],
        selectedWorkflows: [],
        selectedUsers: [],
        selectedUserIids: [],
        selectedMessageTypes: [],
        timeStampData: {
          isUserDateRangeApplied: false,
          userDateRangeStartDate: "",
          userDateRangeEndDate: ""
        },
        jobList: [],
        isProcessInstanceDialogOpen: false,
        processInstanceId: 0,
        instanceIID: 0
      }
    },

    getIsUploadEnableForCurrentUser: function () {
      return oProperties.isUploadEnableForCurrentUser;
    },

    setIsUploadEnableForCurrentUser: function (_isUploadEnableForCurrentUser) {
      oProperties.isUploadEnableForCurrentUser = _isUploadEnableForCurrentUser;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {

      };
    }

  };

})();

export default DashboardScreenProps;
