import CS from '../../../../../../../libraries/cs';
import alertify from '../../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../../libraries/microevent/MicroEvent.js';
import MethodTracker from '../../../../../../../libraries/methodtracker/method-tracker';
import RequestMapping from '../../../../../../../libraries/requestmappingparser/request-mapping-parser';
import { getTranslations as getTranslation } from '../../../../../../../commonmodule/store/helper/translation-manager';
import DashboardScreenAppData from './model/dashboard-screen-app-data';
import DashboardScreenProps from './model/dashboard-screen-props';
import DashboardUtils from './helper/dashboard-utils';
import ContentUtils from './../../../../contentscreen/store/helper/content-utils';
import DashboardScreenRequestMapping from './../tack/dashboard-screen-request-mapping';
import DashboardChartTypeDictionary from './../tack/dashboard-chart-type-dictionary';
import DashboardChartConfiguration from '../tack/dashboard-chart-configuration';
import TranslationStore from '../../../../../../../commonmodule/store/translation-store';
import SessionStorageManager from '../../../../../../../libraries/sessionstoragemanager/session-storage-manager';
import DashboardTabDictionary from './../tack/dashboard-tab-dictionary';
import DashboardConstants from './../tack/dashboard-constants';
import KPIDictionary from '../../../../../../../commonmodule/tack/kpi-config-dictionary';
import { communicator as HomeScreenCommunicator } from '../../../../../store/home-screen-communicator';
import ExceptionLogger from '../../../../../../../libraries/exceptionhandling/exception-logger';
import InformationTabStore from '../../../store/helper/information-tab-store';
import ModulesDictionary from '../../../../../../../commonmodule/tack/module-dictionary';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import NotificationProps from '../../../../../../../commonmodule/props/notification-props';
import SessionStorageConstants from '../../../../../../../commonmodule/tack/session-storage-constants';
import SessionProps from '../../../../../../../commonmodule/props/session-props';
import oFilterPropType from '../../../tack/filter-prop-type-constants';
import BreadcrumbStore from '../../../../../../../commonmodule/store/helper/breadcrumb-store';
import Moment from "moment";
import BackgroundProcessGridSkeleton from "../tack/background-process-grid-view-skeleton";
import JobProps from "../../../store/model/job-screen-view-props";
const BackgroundColors = DashboardChartConfiguration.backgroundColors;

let DashboardScreenStore = (function () {

  let oAppData = DashboardScreenAppData;
  let oComponentProps = DashboardScreenProps;

  let _triggerChange = function () {
    MethodTracker.emptyCallTrace();
    DashboardScreenStore.trigger('dashboard-changed');
  };

  let _getDataGovernanceDataAccordingToSelectedTab = function (sSelectedDashboardTabId, oCallbackData = {}) {
    let sURL = DashboardScreenRequestMapping.GetAll;
    let oPaginationData = DashboardScreenProps.getDataGorvernanceTilesPaginationData();
    let oRequestData = {
      dashboardTabId: sSelectedDashboardTabId,
      from: oPaginationData.from,
      size: oPaginationData.size
    };
    let oBreadCrumbData = {};
    oBreadCrumbData.extraData = {
      URL: sURL,
      postData: oRequestData
    };
    if (oCallbackData && oCallbackData.breadCrumbData) {
      CS.assign(oCallbackData.breadCrumbData.extraData, oBreadCrumbData.extraData);
    } else {
      oCallbackData.breadCrumbData = oBreadCrumbData;
    }

    _getDataGovernanceDataAccordingToSelectedTabCall(oCallbackData, oBreadCrumbData.extraData);
  };

  let _getDataGovernanceDataAccordingToSelectedTabCall = function (oCallbackData, oAjaxExtraData) {
    let fSuccess = successFetchDashboardGovernanceData.bind(this, oCallbackData);
    let fFailure = failureFetchDashboardGovernanceData;

    return CS.postRequest(oAjaxExtraData.URL, {}, oAjaxExtraData.postData, fSuccess, fFailure);
  };

  let successFetchDashboardGovernanceData = function (oCallbackData, oResponse) {
    oResponse = oResponse.success;
    if (!oResponse.dataGovernance.length) {
      let oLeftScrollData = DashboardScreenProps.getLeftScrollData();
      oLeftScrollData.dataGovernanceLeftScroll = 0;
    }

    BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData, true);

    let aOldDashboardGovernanceData = oAppData.getDashboardGovernanceData();
    let aNewDashboardGovernanceData = oResponse.dataGovernance;
    let aDashboardGovernanceData = oCallbackData.isLoadMoreClicked ? CS.assign(aNewDashboardGovernanceData, aOldDashboardGovernanceData) : aNewDashboardGovernanceData;
    oAppData.setDashboardGovernanceData(aDashboardGovernanceData);

    let oReferencedKPI = oAppData.getReferencedKPI();
    CS.assign(oReferencedKPI, oResponse.referencedKpi);
    oAppData.setReferencedKPI(oReferencedKPI);
    let oReferencedTaxonomies = oAppData.getReferencedTaxonomies();
    CS.assign(oReferencedTaxonomies, oResponse.referencedTaxonomies);
    oAppData.setReferencedTaxonomies(oReferencedTaxonomies);

    let oReferencedClasses = oAppData.getReferencedClasses();
    oAppData.setReferencedClasses(CS.combine(oReferencedClasses, oResponse.referencedKlasses));

    let aFunctionToCallForKPIDrillDrown = [];
    _processDashboardGovernanceData(aDashboardGovernanceData, aFunctionToCallForKPIDrillDrown);
    CS.forEach(aFunctionToCallForKPIDrillDrown, function (fKPIDrillDown) {
      fKPIDrillDown();
    });

    _triggerChange();

    return true;
  };

  let failureFetchDashboardGovernanceData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureFetchDashboardGovernanceData", getTranslation());

    return false;
  };

  let _getDataIntegrationDataAccordingToSelectedTab = function (sSelectedDashboardTabId, oCallbackData = {}) {
    let sURL = DashboardScreenRequestMapping.GetDataIntegrationInfo;
    let oPaginationData = DashboardScreenProps.getDataIntegrationTilesPaginationData();
    let oRequestData = {
      mode: DashboardConstants.endpointTileModes.UPLOAD_SUMMARY,
      dashboardTabId: sSelectedDashboardTabId,
      from: oPaginationData.from,
      size: oPaginationData.size
    };
    let oBreadCrumbData = {};
    oBreadCrumbData.extraData = {
      URL: sURL,
      postData: oRequestData
    };
    if (oCallbackData && oCallbackData.breadCrumbData) {
      CS.assign(oCallbackData.breadCrumbData.extraData, oBreadCrumbData.extraData);
    } else {
      oCallbackData.breadCrumbData = {};
      oCallbackData.breadCrumbData = oBreadCrumbData;
    }

    _getDataIntegrationDataAccordingToSelectedTabCall(oCallbackData, oBreadCrumbData.extraData);
  };

  let _getDataIntegrationDataAccordingToSelectedTabCall = function (oCallbackData, oAjaxExtraData) {
    let fSuccess = successFetchDataIntegrationDashboardData.bind(this, oCallbackData);
    let fFailure = failureFetchDataIntegrationDashboardData;

    return CS.postRequest(oAjaxExtraData.URL, {}, oAjaxExtraData.postData, fSuccess, fFailure);
  }

  let successFetchDataIntegrationDashboardData = function (oCallbackData, oResponse) {
    let success = oResponse.success;
    oResponse = success.dataIntegrationInfo;
    let bIsUploadEnableForCurrentUser = success.functionPermission.canImport;
    if (!oResponse.length) {
      let oLeftScrollData = DashboardScreenProps.getLeftScrollData();
      oLeftScrollData.dataIntegrationLeftScroll = 0;
    }

    BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);

    let aDashboardIntegrationData = oAppData.getDashboardIntegrationData();
    oAppData.setDashboardIntegrationData(aDashboardIntegrationData.concat(oResponse));
    DashboardScreenProps.setIsUploadEnableForCurrentUser(bIsUploadEnableForCurrentUser);

    _processDataIntegrationDashboardData(oResponse);
    HomeScreenCommunicator.disablePhysicalCatalog(false);
    SessionProps.setActiveEndpointData({});
    _triggerChange();
  };

  let failureFetchDataIntegrationDashboardData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureFetchDataIntegrationDashboardData", getTranslation());
  };

  /**--------------------------------------- PROCESSING DATA FOR DASHBOARD -------------------------------------------*/
  let _processDashboardGovernanceData = function (aNewDashboardGovernanceData, aFunctionToCallForKPIDrillDrown) {
    let aDashboardGridLayout = [];
    let oProcessedDashboardDataMap = oComponentProps.getProcessedDashboardDataGovernanceMap();
    CS.forEach(aNewDashboardGovernanceData, function (oKPI, iIndex) {
      let sKPIId = oKPI.kpiId;
      aDashboardGridLayout.push({
        i: sKPIId,
        x: ((iIndex * 5) % 25), //we need x values as 0, 5, 10, 15, 20 and so on...(since total columns = 25 and one
        // tile width = 5 columns)
        y: 0,
        w: 5,
        h: 5,
        minH: 3, // TODO: Change MaxHeight & MaxWidth
        // maxH: 6,
        minW: 3,
        // maxW:6
      });

      oProcessedDashboardDataMap[sKPIId] = _prepareDashboardTileDataFromKPI(oKPI, oProcessedDashboardDataMap[sKPIId], aFunctionToCallForKPIDrillDrown);
    });
    oComponentProps.setDashboardGridLayout(aDashboardGridLayout);
  };

  let _getCategoryEntityByType = function (sType, sTypeId, sParentId) {
    if (sType === "taxonomy") {
      let oReferencedTaxonomies = oAppData.getReferencedTaxonomies();
      let oTaxonomy = oReferencedTaxonomies[sTypeId];
      return oTaxonomy;
    } else if (sType === "klass") {
      let oReferencedClasses = oAppData.getReferencedClasses();
      return oReferencedClasses[sTypeId];
    } else { //if type is tag
      let oReferencedTags = oAppData.getReferencedTags();
      let oTagGroup = oReferencedTags[sParentId];
      let oTagValue = CS.find(oTagGroup.children, {id: sTypeId});
      oTagValue.parentLabel = oTagGroup.label;
      return oTagValue;
    }
  };

  let _handleDashboardButtonVisibility = function (iFrom, iSize, iTaxonomyCount, oDashboardTileData) {
    oDashboardTileData.showPreviousButton = true;
    oDashboardTileData.showNextButton = true;

    if (iTaxonomyCount <= iSize && iFrom === 0) {
      oDashboardTileData.showPreviousButton = false;
      oDashboardTileData.showNextButton = false;
    }
    else if (iFrom + iSize > iTaxonomyCount) {
      oDashboardTileData.showNextButton = false;
    }
    else if (iFrom === 0) {
      oDashboardTileData.showPreviousButton = false;
    }
  };

  let _getTileDataAccordingToPagination = function (oMasterTileData, sKPIId) {
    let oDashboardTileData = {};
    let oPaginationData = oMasterTileData.paginationData;
    let iSize = oPaginationData.closedDialogSize;
    if (!CS.isEmpty(oComponentProps.getActiveDashboardTileObject())) {
      iSize = oPaginationData.openedDialogSize;
    }

    let aLabels = oMasterTileData.labels;
    let aDataSets = oMasterTileData.datasets;
    let aLabelsToShow = CS.slice(aLabels, oPaginationData.from, oPaginationData.from + iSize);
    let aDataSetsToShow = [];

    CS.forEach(aDataSets, function (oDataSet) {
      aDataSetsToShow.push({
        label: oDataSet.label,
        data: CS.slice(oDataSet.data, oPaginationData.from, oPaginationData.from + iSize),
        backgroundColor: oDataSet.backgroundColor
      });
    });

    let oKPIChartInvert = DashboardScreenProps.getKPIChartInvertData();
    CS.forEach(aDataSetsToShow, function (oDataSet) {
      if (oKPIChartInvert[sKPIId]) {
        let aData = oDataSet.data;
        for (let i = 0; i < aData.length; i++) {
          aData[i] = 100 - aData[i];
        }
        oDataSet.borderColor = '#ff0000';
        oDataSet.borderWidth = 0.5;

      } else {
        oDataSet.borderColor = 'none';
        oDataSet.borderWidth = 0;
      }
    });

    oDashboardTileData.chartData = {
      labels: aLabelsToShow,
      datasets: aDataSetsToShow
    };

    let iTotalTaxonomyCount = (aLabels).length;
    _handleDashboardButtonVisibility(oPaginationData.from, iSize, iTotalTaxonomyCount, oDashboardTileData);

    return oDashboardTileData;
  };

  let _getDashboardTilePaginationData = function () {
    return {
      from: 0,
      closedDialogSize: 5,
      openedDialogSize: 10
    }
  };

  let _checkKPIRuleValidationCriteria = (sDimensionId, oKPIRuleValidationCriteria) => {
    switch (sDimensionId) {
      case KPIDictionary.ACCURACY_RULES:
        return oKPIRuleValidationCriteria.isAccuracyValid;
      case KPIDictionary.UNIQUENESS_RULES:
        return oKPIRuleValidationCriteria.isUniquenessValid;
      case KPIDictionary.CONFORMITY_RULES:
        return oKPIRuleValidationCriteria.isConformityValid;
      case KPIDictionary.COMPLETENESS_RULES:
        return oKPIRuleValidationCriteria.isCompletenessValid;
      default:
        return false;
    }
  };

  let _getAndProcessBreadCrumbData = function (oKPI, oProcessedDashboardKPIData, oReferencedKPI, aCategories, aFunctionToCallForKPIDrillDrown) {
    let aBreadCrumbData = oProcessedDashboardKPIData.breadCrumbs ||
        [{
          id: -1,
          label: oReferencedKPI.label,
          levelId: 0,
          code: oReferencedKPI.code,
          path: []
        }];

    if (aBreadCrumbData.length > 1) {
      let oLastNode = CS.last(aBreadCrumbData);
      aFunctionToCallForKPIDrillDrown.push(_handleBreadCrumbClicked.bind(this, oKPI.kpiId, oLastNode.id));
    }
    return aBreadCrumbData;
  };

  let _prepareDashboardTileDataFromKPI = function (oKPI, oProcessedDashboardKPIData = {}, aFunctionToCallForKPIDrillDrown = []) {
    let oReferencedKPIs = oAppData.getReferencedKPI();
    let oReferencedKPI = oReferencedKPIs[oKPI.kpiId] || {};
    let oDashboardTileData = {};
    oDashboardTileData.id = oKPI.kpiId;
    oDashboardTileData.label = oReferencedKPI.label;
    oDashboardTileData.code = oReferencedKPI.code;
    let aLabels = [];
    let aDataSets = [];

    //oDataSetMap : keys will be the dimension id, and value will be an array of values.
    let oDataSetMap = {};
    let sParentLabel = '';
    let aCategories = oKPI.categories;
    CS.forEach(aCategories, function (oCategory) {
      let oEntity = _getCategoryEntityByType(oCategory.type, oCategory.typeId, oCategory.parentId);
      let sCategoryLabel = CS.getLabelOrCode(oEntity);
      sParentLabel = oEntity.parentLabel || "";
      aLabels.push(sCategoryLabel);

      CS.forEach(oCategory.dimensions, function (oDimension) {
        let sDimensionId = oDimension.id;
        if (!_checkKPIRuleValidationCriteria(sDimensionId, oReferencedKPI.kpiRuleValidationCriteria)) {
          return;
        }
        let iValue = oDimension.value ? oDimension.value.toFixed(2) : oDimension.value;

        if (!oDataSetMap.hasOwnProperty(sDimensionId)) {
          oDataSetMap[sDimensionId] = [];
        }
        oDataSetMap[sDimensionId].push(iValue);
      })

    });

    //iterate over oDataSetMap and prepare data in the format of Chart.js
    CS.forEach(oDataSetMap, function (aData, sDimensionId) {
      aDataSets.push({
        label: getTranslation()[sDimensionId.toUpperCase()],
        data: aData,
        backgroundColor: BackgroundColors[sDimensionId], //todo: choose color according to the color field if available else use the default ones
      });
    });

    /**Storing Master chart data with pagination for each KPI */
    let oPaginationData = _getDashboardTilePaginationData();
    let aDashboardTileMasterData = oComponentProps.getDashboardTileMasterData();
    aDashboardTileMasterData[oKPI.kpiId] = {
      labels: aLabels,
      datasets: aDataSets,
      paginationData: oPaginationData
    };
    CS.assign(oDashboardTileData, _getTileDataAccordingToPagination(aDashboardTileMasterData[oKPI.kpiId], oKPI.kpiId));

    oDashboardTileData.chartType = DashboardChartTypeDictionary.GROUPED_BAR;
    oDashboardTileData.chartOptions = new DashboardChartConfiguration[DashboardChartTypeDictionary.GROUPED_BAR]();
    if (sParentLabel) {
      oDashboardTileData.chartOptions.scales.xAxes[0].scaleLabel = {
        display: true,
        labelString: sParentLabel
      };
    } else {
      oDashboardTileData.chartOptions.scales.xAxes[0].scaleLabel = {
        display: false
      }
    }
    oDashboardTileData.breadCrumbs = _getAndProcessBreadCrumbData(oKPI, oProcessedDashboardKPIData, oReferencedKPI, aCategories, aFunctionToCallForKPIDrillDrown);

    return oDashboardTileData;
  };

  let _handleDashboardTilePaginationButtonClick = function (sContext, sTileId) {
    let oDashboardTileMasterDataMap = oComponentProps.getDashboardTileMasterData();
    let oMasterTileData = oDashboardTileMasterDataMap[sTileId];
    let oProcessedDashboardDataMap = oComponentProps.getProcessedDashboardDataGovernanceMap();
    let oProcessedTileData = oProcessedDashboardDataMap[sTileId];
    let oPaginationData = oMasterTileData.paginationData;
    let iSize = oPaginationData.closedDialogSize;
    //if tile is expanded -
    if (!CS.isEmpty(oComponentProps.getActiveDashboardTileObject())) {
      iSize = oPaginationData.openedDialogSize;
      oProcessedTileData = oComponentProps.getActiveDashboardTileObject();
    }
    if (sContext === DashboardConstants.kpiTileButton.NEXT_BUTTON) {
      oPaginationData.from = oPaginationData.from + iSize;
    }
    else if (sContext === DashboardConstants.kpiTileButton.PREVIOUS_BUTTON) {
      oPaginationData.from = oPaginationData.from > iSize ? oPaginationData.from - iSize : 0;
    }
    CS.assign(oProcessedTileData, _getTileDataAccordingToPagination(oMasterTileData, sTileId));
    _triggerChange();
  };

  let _handleDashboardTileOpenDialogButtonClicked = function (sTileId) {
    let oDashboardTileMasterDataMap = oComponentProps.getDashboardTileMasterData();
    let oMasterTileData = oDashboardTileMasterDataMap[sTileId];
    let oProcessedDashboardDataMap = oComponentProps.getProcessedDashboardDataGovernanceMap();
    let oProcessedTileData = CS.cloneDeep(oProcessedDashboardDataMap[sTileId]);
    oComponentProps.setActiveDashboardTileObject(oProcessedTileData);
    CS.assign(oComponentProps.getActiveDashboardTileObject(), _getTileDataAccordingToPagination(oMasterTileData, sTileId));
    _triggerChange();
  };

  let _handledDashboardViewCloseDialogClicked = function (sTileId) {
    let oDashboardTileMasterDataMap = oComponentProps.getDashboardTileMasterData();
    let oMasterTileData = oDashboardTileMasterDataMap[sTileId];
    let oProcessedDashboardDataMap = oComponentProps.getProcessedDashboardDataGovernanceMap();
    let oProcessedTileData = oProcessedDashboardDataMap[sTileId];
    oProcessedTileData.breadCrumbs = oComponentProps.getActiveDashboardTileObject().breadCrumbs;
    oComponentProps.setActiveDashboardTileObject({});
    CS.assign(oProcessedTileData, _getTileDataAccordingToPagination(oMasterTileData, sTileId));
    _triggerChange();
  };

  /**------------------------------ PROCESSING DATA FOR DATA INTEGRATION DASHBOARD -----------------------------------*/
  let _processDataIntegrationDashboardData = function (aNewDashboardIntegrationData) {
    let aDashboardGridLayout = [];
    let oProcessedDashboardDataMap = oComponentProps.getProcessedDashboardDataIntegrationMap();
    CS.forEach(aNewDashboardIntegrationData, function (oEndpointData) {
      try {
        let sEndpointId = oEndpointData.id;
        oProcessedDashboardDataMap[sEndpointId] = _prepareDashboardTileDataFromEndpoint(oEndpointData);
      } catch (e) {
        ExceptionLogger.error(e);
      }
    });
    oComponentProps.setDashboardGridLayout(aDashboardGridLayout);
  };

  let _prepareDashboardTileDataFromEndpoint = function (oEndpoint) {
    let oDashboardTileData = {};
    oDashboardTileData.id = oEndpoint.id;
    oDashboardTileData.code = oEndpoint.code;
    oDashboardTileData.label = oEndpoint.label;
    oDashboardTileData.type = oEndpoint.type;
    oDashboardTileData.mode = oEndpoint.mode;
    oDashboardTileData.icon = oEndpoint.icon;
    oDashboardTileData.iconKey = oEndpoint.iconKey;
    oDashboardTileData.physicalCatalogId = oEndpoint.physicalCatalogId;
    oDashboardTileData.isUploadEnableForCurrentUser = DashboardScreenProps.getIsUploadEnableForCurrentUser();

    let oTileInfo = oEndpoint.data;
    let oTileData = {};

    if (oEndpoint.type === DashboardConstants.endpointTypes.INBOUND) {
      switch (oEndpoint.mode) {

        case DashboardConstants.endpointTileModes.LAST_UPLOAD:
          oTileInfo = oTileInfo[0];
          if (CS.isNotEmpty(oTileInfo)) {
            let sDownloadUrl = oTileInfo.assetInstanceId ? RequestMapping.getRequestUrl(DashboardScreenRequestMapping.DownloadFile, {id: oTileInfo.assetInstanceId}) : "";
            oTileData.fileName = oTileInfo.label;
            oTileData.successCount = oTileInfo.success;
            oTileData.failureCount = oTileInfo.failure;
            oTileData.redCount = oTileInfo.totalRedViolations;
            oTileData.orangeCount = oTileInfo.totalOrangeViolations;
            oTileData.yellowCount = oTileInfo.totalYellowViolations;
            oTileData.uploadTimestamp = oTileInfo.timeStamp;
            oTileData.downloadUrl = sDownloadUrl;
          }
          break;

        case DashboardConstants.endpointTileModes.ALL_UPLOADS:
          CS.assign(oTileData, oTileInfo);
          break;

        case DashboardConstants.endpointTileModes.UPLOAD_SUMMARY:
          if(CS.isNotEmpty(oTileInfo)){
            oTileData = getInboundEndpointTileSummaryData(oTileInfo[0]);
          }
          break;
      }
    } else {
      oTileData.totalArticlesCount = oEndpoint.totalArticlesCount || 0;
      oTileData.filesToDownload = [];
      CS.forEach(oTileInfo, function (oFileData) {
        let sDownloadUrl = oFileData.assetInstanceId ? RequestMapping.getRequestUrl(DashboardScreenRequestMapping.DownloadFile, {id: oFileData.assetInstanceId}) : "";
        oTileData.filesToDownload.push({
          fileName: oFileData.label,
          downloadUrl: sDownloadUrl
        });
      })
    }

    oDashboardTileData.tileData = oTileData;

    return oDashboardTileData;
  };

  let getInboundEndpointTileSummaryData = (oTileInfo) => {
    let iTotalFileUploads = oTileInfo.totalFileUploads;
    let iSuccessfulUploads = oTileInfo.successfulUploads;
    let iFailedImports = oTileInfo.failedImports;
    let iSuccessfulImports = oTileInfo.successfulImports;
    let iTotalImports = iSuccessfulImports + iFailedImports;
    let iTotalRedViolations = oTileInfo.totalRedViolations;
    let iTotalOrangeViolations = oTileInfo.totalOrangeViolations;
    let iTotalYellowViolations = oTileInfo.totalYellowViolations;

    return [
      {
        "label": getTranslation().FILE_UPLOADS,
        "value": iTotalFileUploads,
        "type": "none"
      },
      {
        "label": getTranslation().SUCCESSFUL_FILE_UPLOADS,
        "value": iSuccessfulUploads,
        "type": "success",
        "graphData": {
          "total": iTotalFileUploads,
          "success": iSuccessfulUploads,
          "failure": iTotalFileUploads - iSuccessfulUploads,
          "inProgress": 0
        }
      },
      {
        "label": getTranslation().SUCCESSFUL_ARTICLE_IMPORTS,
        "value": iSuccessfulImports,
        "type": "success",
        "graphData": {
          "total": iTotalImports,
          "success": iSuccessfulImports,
          "failure": iFailedImports,
          "inProgress": 0
        }
      },
      {
        "label": getTranslation().REDVIOLATIONS,
        "iconClass": "red",
        "value": iTotalRedViolations,
        "type": "other",
        "graphData": {
          "total": iTotalImports,
          "other": iTotalRedViolations,
          "otherClass": "redBlock"
        }
      },
      {
        "label": getTranslation().ORANGEVIOLATIONS,
        "iconClass": "orange",
        "value": iTotalOrangeViolations,
        "type": "other",
        "graphData": {
          "total": iTotalImports,
          "other": iTotalOrangeViolations,
          "otherClass": "orangeBlock"
        }
      },
      {
        "label": getTranslation().YELLOWVIOLATIONS,
        "iconClass": "yellow",
        "value": iTotalYellowViolations,
        "type": "other",
        "graphData": {
          "total": iTotalImports,
          "other": iTotalYellowViolations,
          "otherClass": "yellowBlock"
        }
      }
    ];
  };
  /**-----------------------------------------------------------------------------------------------------------------*/

  let _updateDashboardAppData = function (oResponse) {
    let oOriginalReferencedTags = oAppData.getReferencedTags();
    let oReferencedTags = oResponse.referencedTags;
    CS.forEach(oReferencedTags, function (oTag, sTagId) {
      oOriginalReferencedTags[sTagId] = oTag;
    });
    let oOriginalReferencedTaxonomies = oAppData.getReferencedTaxonomies();
    let oReferencedTaxonomies = oResponse.referencedTaxonomies;
    CS.forEach(oReferencedTaxonomies, function (oTax, sTaxId) {
      oOriginalReferencedTaxonomies[sTaxId] = oTax;
    });

    let oOriginalReferencedClasses = oAppData.getReferencedClasses();
    let oReferencedClasses = oResponse.referencedKlasses;
    CS.forEach(oReferencedClasses, function (oClass, sClassId) {
      oOriginalReferencedClasses[sClassId] = oClass;
    });
  };

  let _getPathFromSelectedCategory = function (sKpiId, sCategoryLabel, iCategoryIndex) {
    let aDataGovernance = oAppData.getDashboardGovernanceData();
    let oDashboardTileMasterDataMap = oComponentProps.getDashboardTileMasterData();
    let oMasterTileData = oDashboardTileMasterDataMap[sKpiId];
    let oPaginationData = oMasterTileData.paginationData;
    let iSize = oPaginationData.from;

    let oKpi = CS.find(aDataGovernance, {kpiId: sKpiId});
    let aCategories = oKpi.categories;
    let oSelectedCategory = aCategories[iSize + iCategoryIndex];
    return {
      levelId: oKpi.levelId,
      path: [{
        type: oSelectedCategory.type,
        typeId: oSelectedCategory.typeId,
        parentId: oSelectedCategory.parentId
      }]//,
      // breadCrumbId: oSelectedCategory.typeId
    };
  };

  let _handleDashboardViewBarGroupClicked = function (sKpiId, sCategoryLabel, iCategoryIndex) {
    let oProcessedDashboardDataMap = DashboardScreenProps.getProcessedDashboardDataGovernanceMap();
    let oTileDataToConsider = {};
    let oActiveDashboardTileData = DashboardScreenProps.getActiveDashboardTileObject();
    let bIsDialogOpen = !CS.isEmpty(oActiveDashboardTileData);

    if (bIsDialogOpen) {
      oTileDataToConsider = oActiveDashboardTileData;
    } else {
      oTileDataToConsider = oProcessedDashboardDataMap[sKpiId];
    }
    oTileDataToConsider.isKpiLoading = true;
    let aBreadCrumbs = oTileDataToConsider.breadCrumbs;
    let oPathData = _getPathFromSelectedCategory(sKpiId, sCategoryLabel, iCategoryIndex);
    let aPath = CS.concat(aBreadCrumbs[aBreadCrumbs.length - 1].path, oPathData.path);
    let oReferencedTags = oAppData.getReferencedTags();
    let sBreadCrumbLabel = sCategoryLabel;
    if (oPathData.path[0].parentId) {
      let oTagGroup = oReferencedTags[oPathData.path[0].parentId];
      sBreadCrumbLabel = oTagGroup.label + " : " + sCategoryLabel;
    }

    let oRequestData = {
      kpiId: sKpiId,
      levelId: oPathData.levelId,
      path: aPath,
      from: 0,
      size: 999
    };
    let oCallBack = {};
    let oLastBreadCrumbPath = CS.last(oPathData.path);
    oCallBack.breadCrumb = {
      id: oLastBreadCrumbPath.typeId,
      label: sBreadCrumbLabel,
      levelId: oPathData.levelId,
      path: aPath
    };

    let fSuccess = successDrillDownCallback.bind(this, oCallBack);
    let fFailure = failureDrillDownCallback;

    CS.postRequest(DashboardScreenRequestMapping.DrillDown, {}, oRequestData, fSuccess, fFailure, true);
  };

  let successDrillDownCallback = function (oCallBack, oResponse) {
    oResponse = oResponse.success;
    let oKpi = oResponse.kpiStatistics;
    let sKpiId = oKpi.kpiId;
    let aDataGovernance = oAppData.getDashboardGovernanceData();
    let oProcessedDashboardDataMap = DashboardScreenProps.getProcessedDashboardDataGovernanceMap();
    let oActiveDashboardTileData = DashboardScreenProps.getActiveDashboardTileObject();
    let bIsDialogOpen = !CS.isEmpty(oActiveDashboardTileData);
    let oTileDataToConsider = bIsDialogOpen ? oActiveDashboardTileData : oProcessedDashboardDataMap[sKpiId];
    if (!CS.isEmpty(oKpi.categories)) {
      _updateDashboardAppData(oResponse);
      let oTileDataToConsider = {};
      let oActiveDashboardTileData = DashboardScreenProps.getActiveDashboardTileObject();
      let bIsDialogOpen = !CS.isEmpty(oActiveDashboardTileData);

      if (bIsDialogOpen) {
        oTileDataToConsider = oActiveDashboardTileData;
      } else {
        oTileDataToConsider = oProcessedDashboardDataMap[sKpiId];
      }
      oTileDataToConsider.isKpiLoading = false;
      let aOldBreadCrumbs = oTileDataToConsider.breadCrumbs;
      let oUpdatedTileData = _prepareDashboardTileDataFromKPI(oKpi);
      oUpdatedTileData.breadCrumbs = aOldBreadCrumbs.concat(oCallBack.breadCrumb);

      if (bIsDialogOpen) {
        DashboardScreenProps.setActiveDashboardTileObject(oUpdatedTileData);
      } else {
        oProcessedDashboardDataMap[sKpiId] = oUpdatedTileData;
      }

      // Update AppData of changed KPI
      // TODO: donot edit app data
      let iChangedKpiIndex = CS.findIndex(aDataGovernance, {kpiId: sKpiId});
      aDataGovernance.splice(iChangedKpiIndex, 0, oKpi);
      aDataGovernance.splice(iChangedKpiIndex + 1, 1);

      oCallBack && oCallBack.functionToExecute && oCallBack.functionToExecute();
    } else {
      oTileDataToConsider.isKpiLoading = false;
      alertify.message(getTranslation().NO_DRILL_DOWN_POSSIBLE);
    }
    _triggerChange();
  };

  let failureDrillDownCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureDrillDownCallback", getTranslation());
  };

  let _handleDashboardRefreshTile = function (oKpiData, oCallback) {
    let sKpiId = oKpiData.kpiId;
    let aBreadCrumbData = oKpiData.breadCrumbData;
    let oActiveDrillDownLevel = aBreadCrumbData[aBreadCrumbData.length - 1];

    _handleBreadCrumbClicked(sKpiId, oActiveDrillDownLevel.id, oCallback);
    _triggerChange();
  };

  let _handleRefreshEndpoint = function (sEndpointId, sTileMode, oCallback) {
    let oProcessedDashboardDataMap = oComponentProps.getProcessedDashboardDataIntegrationMap();
    if (!CS.isEmpty(oProcessedDashboardDataMap)) {
      oProcessedDashboardDataMap[sEndpointId].isEndpointLoading = true;
      //TODO: Temporary fix
      if (oProcessedDashboardDataMap[sEndpointId].type === DashboardConstants.endpointTypes.OUTBOUND) {
        sTileMode = "outbound";
      }
    }
    let bHideLoader = true;
    _handleDashboardEndpointTileModeChanged(sEndpointId, sTileMode, oCallback, bHideLoader);
  };

  let _handleBreadCrumbClicked = function (sKpiId, sBreadCrumbId, oCallback) {
    let oProcessedDashboardDataMap = DashboardScreenProps.getProcessedDashboardDataGovernanceMap();
    let oProcessedKpi = {};
    let oActiveDashboardTileData = DashboardScreenProps.getActiveDashboardTileObject();
    let bIsDialogOpen = !CS.isEmpty(oActiveDashboardTileData);

    if (bIsDialogOpen) {
      oProcessedKpi = oActiveDashboardTileData;
    } else {
      oProcessedKpi = oProcessedDashboardDataMap[sKpiId];
    }

    let oBreadCrumb = CS.find(oProcessedKpi.breadCrumbs, {id: sBreadCrumbId});
    let oRequestData = {
      kpiId: sKpiId,
      levelId: oBreadCrumb.levelId,
      path: oBreadCrumb.path,
      from: 0,
      size: 999
    };

    oProcessedKpi.isKpiLoading = true;
    let fSuccess = successBreadCrumbClickedCallback.bind(this, sKpiId, sBreadCrumbId, oCallback);
    let fFailure = failureFetchDashboardData;

    CS.postRequest(DashboardScreenRequestMapping.DrillDown, {}, oRequestData, fSuccess, fFailure, true);
  };

  let _handleDashboardTilesLoadMoreClicked = function (sContext, iNewLeft) {
    let oPaginationData = {};
    let oLeftScrollData = {};
    let sSelectedDashboardTabId = DashboardScreenProps.getSelectedDashboardTabId();
    let oCallbackData = {};
    let aDashboardTabsList = oComponentProps.getDashboardTabsList();
    let oSelectedTab = CS.find(aDashboardTabsList, {id: sSelectedDashboardTabId});
    let oSelectedModule = ContentUtils.getSelectedModule();
    let sBreadcrumbId = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD;
    let sBreadcrumbLabel = getTranslation().DASHBOARD_TITLE + " : " + (oSelectedTab ? oSelectedTab.label : "");
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(sBreadcrumbId, sBreadcrumbLabel, "module", oSelectedModule.id);
    switch (sContext) {
      case "dataGovernance":
        oPaginationData = DashboardScreenProps.getDataGorvernanceTilesPaginationData();
        oPaginationData.from = oAppData.getDashboardGovernanceData().length;
        oLeftScrollData = DashboardScreenProps.getLeftScrollData();
        oLeftScrollData.dataGovernanceLeftScroll = iNewLeft;
        oCallbackData.isLoadMoreClicked = true;
        _getDataGovernanceDataAccordingToSelectedTab(sSelectedDashboardTabId, oCallbackData);
        return;

      case "dataIntegration":
        oPaginationData = DashboardScreenProps.getDataIntegrationTilesPaginationData();
        oPaginationData.from = oAppData.getDashboardIntegrationData().length;
        oLeftScrollData = DashboardScreenProps.getLeftScrollData();
        oLeftScrollData.dataIntegrationLeftScroll = iNewLeft;
        _getDataIntegrationDataAccordingToSelectedTab(sSelectedDashboardTabId, oCallbackData);
        break;
    }
  };

  let successBreadCrumbClickedCallback = function (sKpiId, sBreadCrumbId, oCallback, oResponse) {
    let oSuccess = oResponse.success;
    let oProcessedDashboardDataMap = DashboardScreenProps.getProcessedDashboardDataGovernanceMap();
    let oTileDataToConsider = {};
    let oActiveDashboardTileData = DashboardScreenProps.getActiveDashboardTileObject();
    let bIsDialogOpen = !CS.isEmpty(oActiveDashboardTileData);

    if (bIsDialogOpen) {
      oTileDataToConsider = oActiveDashboardTileData;
    } else {
      oTileDataToConsider = oProcessedDashboardDataMap[sKpiId];
    }

    let aOldBreadCrumbs = oTileDataToConsider.breadCrumbs;
    let iIndexToSplice = CS.findIndex(aOldBreadCrumbs, {id: sBreadCrumbId});
    aOldBreadCrumbs.splice(iIndexToSplice + 1);

    let oKpi = oSuccess.kpiStatistics;
    _updateDashboardAppData(oResponse);
    let oUpdatedTileData = _prepareDashboardTileDataFromKPI(oKpi);
    oUpdatedTileData.breadCrumbs = aOldBreadCrumbs;

    if (bIsDialogOpen) {
      oUpdatedTileData.isKpiLoading = false;
      DashboardScreenProps.setActiveDashboardTileObject(oUpdatedTileData);
    } else {
      oProcessedDashboardDataMap[sKpiId].isKpiLoading = false;
      oProcessedDashboardDataMap[sKpiId] = oUpdatedTileData;
    }

    // Update AppData for CurrentKpi
    let aDashboardData = oAppData.getDashboardGovernanceData();
    let iChangedKpiIndex = CS.findIndex(aDashboardData, {kpiId: sKpiId});
    aDashboardData.splice(iChangedKpiIndex, 0, oKpi);
    aDashboardData.splice(iChangedKpiIndex + 1, 1);

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    _triggerChange();
  };

  let failureFetchDashboardData = (oResponse) => {
    ContentUtils.failureCallback(oResponse, "failureFetchDashboardData", getTranslation());
  };

  let _handleModuleItemClicked = function (sModuleId) {
    oComponentProps.setSelectedDashboardTabId(sModuleId);
    HomeScreenCommunicator.setSelectedDashboardTabId(sModuleId);
    DashboardScreenProps.setSelectedUserIds([]);
    DashboardScreenProps.setIsGridFilterView(false);
    _fetchDashboardTabData();
    /*
    switch (sModuleId) {
      case DashboardTabDictionary.DATA_GOVERNANCE_TAB:
        _fetchDashboardData();
        break;
      case DashboardTabDictionary.DATA_INTEGRATION_TAB:
        _fetchDataIntegrationDashboardData();
        break;
      case DashboardTabDictionary.TASK_DASHBOARD:
        // relevant data fetched Internally
        _triggerChange();
        break;
    }
    if (sModuleId == DashboardTabDictionary.DATA_INTEGRATION_TAB) {
      HomeScreenCommunicator.handlePhysicalCatalogMenuButtonVisibility(false);
    }else {
      HomeScreenCommunicator.handlePhysicalCatalogMenuButtonVisibility(true);
    }
    */
  };

  let _handleDashboardEndpointTileModeChanged = (sEndpointId, sMode, oCallback, bHideLoader) => {
    let sUrl = DashboardScreenRequestMapping.GetDataIntegrationInfoForEndpoint;
    let oRequestData = {endpointId: sEndpointId, mode: sMode};
    let fSuccess = successDashboardEndpointTileModeChanged.bind(this, sEndpointId, oCallback);
    let fFailure = failureDashboardEndpointTileModeChanged;
    CS.postRequest(sUrl, {}, oRequestData, fSuccess, fFailure, bHideLoader);
  };

  let successDashboardEndpointTileModeChanged = (sEndpointId, oCallback, oResponse) => {
    let oProcessedDashboardDataMap = oComponentProps.getProcessedDashboardDataIntegrationMap();
    oProcessedDashboardDataMap[sEndpointId] = _prepareDashboardTileDataFromEndpoint(oResponse.success);

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    oProcessedDashboardDataMap[sEndpointId].isEndpointLoading = false;
    _triggerChange();
  };

  let failureDashboardEndpointTileModeChanged = (oResponse) => {
    ContentUtils.failureCallback(oResponse, "failureDashboardEndpointTileModeChanged", getTranslation());
  };

  let _getCustomDashboardTabs = () => {
    let sUrl = DashboardScreenRequestMapping.GetDashboardTabs;
    let fSuccess = _successFetchDashboardTabsCallback;
    let fFailure = _failureFetchDashboardTabsCallback;

    return CS.customGetRequest(sUrl, {}, fSuccess, fFailure, null, true);
  };

  let _successFetchDashboardTabsCallback = function (oResponse) {
    let bIsUserReadOnly = DashboardUtils.isCurrentUserReadOnly();
    DashboardScreenProps.setDashboardTabsList(oResponse.success, bIsUserReadOnly);
    if (oResponse.success.length) {
      if (CS.isEmpty(ContentUtils.getActiveEndpointData())) {
        oComponentProps.setSelectedDashboardTabId(oResponse.success[0].id);
        HomeScreenCommunicator.setSelectedDashboardTabId(oResponse.success[0].id);
      }
      let oSelectedModule = ContentUtils.getSelectedModule();
      if (NotificationProps.getIsNotificationButtonSelected()) {
        oComponentProps.setSelectedDashboardTabId(DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB);
        HomeScreenCommunicator.setSelectedDashboardTabId(DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB);
      }
      if (oSelectedModule.id === ModulesDictionary.DASHBOARD) {
        return _fetchDashboardTabData();
      }
      return true;
    }

    return true;
  };

  let _failureFetchDashboardTabsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureFetchDashboardTabsCallback", getTranslation());
    return false;
  };

  let _resetRequiredDashboardData = function () {
    let oDataGovernanceTilesPaginationData = oComponentProps.getDataGorvernanceTilesPaginationData();
    oDataGovernanceTilesPaginationData.from = 0;
    oDataGovernanceTilesPaginationData.size = 10;
    let oDataIntegrationTilesPaginationData = oComponentProps.getDataIntegrationTilesPaginationData();
    oDataIntegrationTilesPaginationData.from = 0;
    oDataIntegrationTilesPaginationData.size = 10;
    let oLeftScrollData = oComponentProps.getLeftScrollData();
    oLeftScrollData.dataGovernanceLeftScroll = 0;
    oLeftScrollData.dataIntegrationLeftScroll = 0;
    oComponentProps.setProcessedDashboardDataGovernanceMap({});
    oComponentProps.setProcessedDashboardDataIntegrationMap({});
    InformationTabStore.resetInformationTabData();
  };

    let successFetchBackgroundProcessData = (oCallback, oResponse) => {
    oResponse = oResponse.success;
    let aBGPJobs = oResponse.bgpJobs;
    let oUserMap = oResponse.usersMap;
    let aGridData = [];
    let oTabData = {};
    CS.forEach(aBGPJobs, function (oBJPJob) {
      let sCreatedBy = oUserMap[oBJPJob.createdBy];
      let oJobData = {
        id: oBJPJob.jobId,
        properties: {
          "jobId": {value: oBJPJob.jobId},
          "service": {value: getTranslation()[oBJPJob.service]},
          "progress": {value: oBJPJob.progress},
          "status": {value: getTranslation()[oBJPJob.status]},
          "createdBy": {value: sCreatedBy},
          "created": {value: oBJPJob.created}
        },
        actionItemsToShow: ["view"]
      };
      aGridData.push(oJobData)
    });
    let oPaginationData = {
      from: oResponse.from,
      pageSize: oResponse.size
    };

    oTabData.gridData = aGridData;
    oTabData.paginationData = oPaginationData;
    oPaginationData.totalItemsCount = oResponse.totalCount;
    DashboardScreenProps.setGridViewData(aGridData);
    DashboardScreenProps.setGridViewTotalItems(oResponse.totalCount);
    DashboardScreenProps.setUserList(oUserMap);
      if (oCallback.breadCrumbData) {
        BreadcrumbStore.addNewBreadCrumbItem(oCallback.breadCrumbData, true);
      } else {
        _triggerChange();
      }
  };

  let failureFetchBackgroundProcessData = (oResponse) => {
    ContentUtils.failureCallback(oResponse, "failureFetchBackgroundProcessData", getTranslation());
    return false;
  };

  let successGetAllServices = (oResponse) => {
    oResponse = oResponse.success;
    let aList = [{id: "ALL", label: getTranslation()["ALL"]}];
    CS.forEach(oResponse.ids, function (sId) {
      aList.push({id: sId, label: getTranslation()[sId]});
    });
    DashboardScreenProps.setServiceList(aList);
  };

  let failureGetAllServices = (oResponse) => {
    ContentUtils.failureCallback(oResponse, "failureGetAllServices", getTranslation());
    return false;
  };

  let _GetAllServices = function () {
    CS.getRequest(DashboardScreenRequestMapping.getAllServices, {}, successGetAllServices, failureGetAllServices);
  }

  let _FetchBackgroundProcessData = function (oCallback = {}) {
    let oGridPaginationData = DashboardScreenProps.getGridViewPaginationData();
    let sGridViewSortBy = DashboardScreenProps.getGridViewSortBy();
    let sGridViewSortOrder = DashboardScreenProps.getGridViewSortOrder();
    let aSelectedUserIds = DashboardScreenProps.getSelectedUserIds();
    let sBDPService = DashboardScreenProps.getSelectedBGPService() === "ALL" ? "" : DashboardScreenProps.getSelectedBGPService();
	
	let oGridViewSkeleton = DashboardScreenProps.getGridViewSkeleton();
	let oFilterData = CS.find(oGridViewSkeleton.scrollableColumns, {id: "createdBy"}).filterData;
	let oReferencedData = oFilterData.referencedData;
	let aUserNames = [];
	CS.forEach(aSelectedUserIds, (sId) => {
 	 	aUserNames.push(oReferencedData[sId].userName);
	});
    let oRequestBody = {
      "bgpService": sBDPService,
      "from": oGridPaginationData.from,
      "moduleId": "allmodule",
      "size": oGridPaginationData.pageSize,
      "userNames": aUserNames,
      "jobStatus": "",
      "sortOptions":
          {
            "sortField": sGridViewSortBy,
            "sortOrder": sGridViewSortOrder
          }

    }
    CS.postRequest(DashboardScreenRequestMapping.backgroundProcessTab, "", oRequestBody, successFetchBackgroundProcessData.bind(this, oCallback), failureFetchBackgroundProcessData);

  }

  let successFetchBackgroundProcessDetails = function (oResponse) {
    oResponse = oResponse.success;
    let oBackgroundProcessDialogData = {
      jobId: oResponse.jobId,
      status: oResponse.status,
      logData: oResponse.logData,
      service: oResponse.service,
      processDefination: oResponse.processDefination
    };
    DashboardScreenProps.setBackgroundProcessDialogData(oBackgroundProcessDialogData);
    DashboardScreenProps.setIsBackgroundProcessDetailDialogOpen(true);
    _triggerChange();
  };

  let failureFetchBackgroundProcessDetails = (oResponse) => {
    ContentUtils.failureCallback(oResponse, "failureFetchBackgroundProcessDetails", getTranslation());
    return false;
  };

  let _handleGridViewActionItemClicked = function (sActionItemId, sContentId) {
    switch (sActionItemId) {
      case 'view':
        let oQueryString = {
          id: sContentId
        };
        CS.getRequest(DashboardScreenRequestMapping.getBackgroundProcessDetails, oQueryString, successFetchBackgroundProcessDetails, failureFetchBackgroundProcessDetails);
        break;
    }

  };

  let _handleBackgroundProcessDialogViewOkButtonClicked = function () {
    DashboardScreenProps.setBackgroundProcessDialogData({});
    DashboardScreenProps.setIsBackgroundProcessDetailDialogOpen(false);
    _triggerChange();
  };

  let _handleBackgroundProcessDialogViewButtonClicked = function (sButtonId) {
    let oData = DashboardScreenProps.getBackgroundProcessDialogData();
    let sProcessId = oData.jobId;
    let oQueryString = {
      id: sProcessId
    };
  switch (sButtonId) {

    case 'refresh':
      CS.getRequest(DashboardScreenRequestMapping.getBackgroundProcessDetails, oQueryString, successFetchBackgroundProcessDetails, failureFetchBackgroundProcessDetails);
      break;

    case "ok":
      _handleBackgroundProcessDialogViewOkButtonClicked();
      break;
  }
};

  let _handleGridViewFilterButtonClicked = function (bShowFilterView) {
    DashboardScreenProps.setIsGridFilterView(bShowFilterView);
    if (!bShowFilterView) {
      DashboardScreenProps.setSelectedUserIds([])
    }
    _triggerChange();

  };

  let _handleGridViewColumnHeaderClicked = function (sColumnId) {
    let sGridViewSortBy = DashboardScreenProps.getGridViewSortBy();
    sGridViewSortBy !== sColumnId && DashboardScreenProps.setGridViewSortOrder(sColumnId) && DashboardScreenProps.setGridViewSortOrder("asc");
    sGridViewSortBy === sColumnId && DashboardScreenProps.getGridViewSortOrder() === "desc" ? DashboardScreenProps.setGridViewSortOrder("asc") : DashboardScreenProps.setGridViewSortOrder("desc");
    _FetchBackgroundProcessData();
  }

  let _handleGridPaginationChanged = function (oNewPaginationData) {
    DashboardScreenProps.setGridViewPaginationData(oNewPaginationData);
    _FetchBackgroundProcessData();
  };

  let _handleGridFilterApplyClicked = function (oAppliedFilterData, oReferencedData) {
   let aFilterValues = oAppliedFilterData.filterValues;
    DashboardScreenProps.setSelectedUserIds(aFilterValues);
    let oGridViewSkeleton = DashboardScreenProps.getGridViewSkeleton();
    let oFilterData = CS.find(oGridViewSkeleton.scrollableColumns, {id: "createdBy"}).filterData;
    oFilterData.selectedItems = aFilterValues;
    oFilterData.referencedData = oReferencedData;
    oFilterData.isFilterApplied = true;
    let iItems = aFilterValues.length;
    let sCustomPlaceHolder = aFilterValues.length > 1 ? ContentUtils.getDecodedTranslation(getTranslation().ITEMS_SELECTED,
        {items: iItems}) : (CS.isNotEmpty(aFilterValues) ? oReferencedData[aFilterValues[0]].label : getTranslation().SELECT);
    oFilterData.customPlaceHolder = sCustomPlaceHolder;

    _FetchBackgroundProcessData();
  };

  let _handleBGPServiceChange = function (sId) {
    DashboardScreenProps.setSelectedBGPService(sId);
    _FetchBackgroundProcessData();
  }

  let _fetchDashboardTabData = function () {
    let aDashboardTabsList = oComponentProps.getDashboardTabsList();
    oAppData.reset();
    _resetRequiredDashboardData();
    //oComponentProps.setDashboardTabsList(aDashboardTabsList);
    let sSelectedDashboardTabId = oComponentProps.getSelectedDashboardTabId();
    let aPromises = [];

    let oSelectedTab = CS.isEmpty(sSelectedDashboardTabId) ? aDashboardTabsList[0] : CS.find(aDashboardTabsList, {id: sSelectedDashboardTabId});

    let oSelectedModule = ContentUtils.getSelectedModule();
    let oCallbackData = {};
    /**
     * Breadcrumb Data
     */
    let sBreadcrumbId = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD;
    let sBreadcrumbLabel = getTranslation().DASHBOARD_TITLE  + " : " + oSelectedTab.label;
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(sBreadcrumbId, sBreadcrumbLabel, "module", oSelectedModule.id);
    let oBreadCrumbData = oCallbackData.breadCrumbData;

    switch (sSelectedDashboardTabId) {
      case DashboardTabDictionary.TASK_DASHBOARD:
      case DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB:
        //TaskStore.clearAllTaskProps();
        _triggerChange();
        return;

      case DashboardTabDictionary.BUCKETS_TAB:
        return;

      case DashboardTabDictionary.INFORMATION_TAB:
        oCallbackData.filterContext = {
            screenContext: oFilterPropType.MODULE, filterType: oFilterPropType.MODULE
          };
        oCallbackData.context = DashboardTabDictionary.INFORMATION_TAB;
        oBreadCrumbData.filterContext = oCallbackData.filterContext;
        oBreadCrumbData.payloadData = [oCallbackData];
        oBreadCrumbData.functionToSet = InformationTabStore.handleDashboardTabClicked;
        return InformationTabStore.handleDashboardTabClicked(oCallbackData);

      case  DashboardTabDictionary.DAM_TAB:
        oCallbackData.filterContext = {
          screenContext: oFilterPropType.MODULE, filterType: oFilterPropType.MODULE
        };
        oCallbackData.context = DashboardTabDictionary.DAM_TAB;
        oBreadCrumbData.filterContext = oCallbackData.filterContext;
        oBreadCrumbData.payloadData = [oCallbackData];
        oBreadCrumbData.functionToSet = InformationTabStore.handleDashboardTabClicked;
        return InformationTabStore.handleDashboardTabClicked(oCallbackData);

      case DashboardTabDictionary.BACKGROUND_PROCESSES_TAB:
        DashboardScreenProps.setGridViewSortBy("created");
        DashboardScreenProps.setGridViewSortOrder("desc");
        DashboardScreenProps.setGridViewSkeleton(new BackgroundProcessGridSkeleton());
        DashboardScreenProps.setSelectedBGPService("");
        DashboardScreenProps.setSelectedBGPService("ALL");
        _GetAllServices();
        return _FetchBackgroundProcessData(oCallbackData);

      case DashboardTabDictionary.DATA_INTEGRATION_LOGS:
        DashboardScreenProps.resetDataIntegrationLogsViewData();
        BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);
        return;

    }

    oBreadCrumbData.functionToSet = () => {
      if (!DashboardUtils.isCurrentUserSystemAdmin()) {
        aPromises.push(_getDataGovernanceDataAccordingToSelectedTab(sSelectedDashboardTabId, oCallbackData));
      }

      if (DashboardUtils.isDataIntegrationAllowedForCurrentUser()) {
        aPromises.push(_getDataIntegrationDataAccordingToSelectedTab(sSelectedDashboardTabId, oCallbackData));
      }
      return Promise.all(aPromises).then(() => true)
    };
    return oBreadCrumbData.functionToSet();
  };

  let _handleInvertKPIChartButtonClicked = function (oKpiData) {
    let oKPIChartInvert = DashboardScreenProps.getKPIChartInvertData();
    let sKPIId = oKpiData.kpiId;
    oKPIChartInvert[sKPIId] = !oKPIChartInvert[sKPIId];
    //DashboardScreenProps.setKPIChartInvertData(oKPIChartInvert);
    let oProcessedDashboardDataGovernanceMap = DashboardScreenProps.getProcessedDashboardDataGovernanceMap();
    let oActiveDashboardTileData = DashboardScreenProps.getActiveDashboardTileObject();
    let bIsDialogOpen = !CS.isEmpty(oActiveDashboardTileData);
    let oKPI = bIsDialogOpen ? oActiveDashboardTileData : oProcessedDashboardDataGovernanceMap[oKpiData.kpiId];
    let aKPIChartDataSets = oKPI.chartData.datasets;
    CS.forEach(aKPIChartDataSets, function (oDataSet) {

      if (oKPIChartInvert[sKPIId]) {
        oDataSet.borderColor = '#ff0000';
        oDataSet.borderWidth = 0.5;
      }
      else {
        oDataSet.borderColor = 'none';
        oDataSet.borderWidth = 0;
      }
      let aData = oDataSet.data;
      for (let i = 0; i < aData.length; i++) {
        aData[i] = 100 - aData[i];
      }
    });

    _triggerChange();
  };

  /**To handle Radio Button */
  let _handleDataIntegrationLogsViewRadioButtonClicked = function (sContext) {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    sContext === "endpoint" ? oDataIntegrationLogsViewData.isEndpointSelected = true : oDataIntegrationLogsViewData.isEndpointSelected = false, oDataIntegrationLogsViewData.selectedEndpoints = []; // eslint-disable-line
    sContext === "workflow" ? oDataIntegrationLogsViewData.isWorkflowSelected = true : oDataIntegrationLogsViewData.isWorkflowSelected = false, oDataIntegrationLogsViewData.selectedWorkflows = []; // eslint-disable-line
    sContext === "physicalCatalog" ? oDataIntegrationLogsViewData.isPhysicalCatalogSelected = true : oDataIntegrationLogsViewData.isPhysicalCatalogSelected = false, oDataIntegrationLogsViewData.selectedPhysicalCatalogs = []; // eslint-disable-line

    DashboardScreenProps.setDataIntegrationLogsViewData(oDataIntegrationLogsViewData);
  };

  /**To handle Lazy Mss Apply Clicked */
  let _handleDataIntegrationLogsViewLazyMssApplyClicked = function (sContext, aSelectedItems, oReferencedData) {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    switch (sContext) {
      case "endpoint":
        oDataIntegrationLogsViewData.selectedEndpoints = aSelectedItems;
        oDataIntegrationLogsViewData.selectedWorkflows = [];
        break;
      case "workflow":
        oDataIntegrationLogsViewData.selectedWorkflows = aSelectedItems;
        oDataIntegrationLogsViewData.selectedEndpoints = [];
        break;
      case "user":
        oDataIntegrationLogsViewData.selectedUsers = aSelectedItems;
        oDataIntegrationLogsViewData.selectedUserIids = [];
        CS.forEach(aSelectedItems, (sSelectedItem) => {
          oDataIntegrationLogsViewData.selectedUserIids.push(oReferencedData[sSelectedItem].iid);
        });
        break;
      case "messageType":
        oDataIntegrationLogsViewData.selectedMessageTypes = aSelectedItems;
        break;
      case "physicalCatalog":
        oDataIntegrationLogsViewData.selectedPhysicalCatalogs = aSelectedItems;
        break;
    }
  };

  /**To handle Date Selector */
  let _handleDateRangeSelected = function (oSelectedTimeRange) {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();

    oDataIntegrationLogsViewData.timeStampData.isUserDateRangeApplied = true;
    oDataIntegrationLogsViewData.timeStampData.userDateRangeStartDate = (Moment(oSelectedTimeRange.startDate).valueOf());
    oDataIntegrationLogsViewData.timeStampData.userDateRangeEndDate = (Moment(oSelectedTimeRange.endDate).valueOf());

  };

  /**To Clear Date Range Selector */
  let _handleDateRangeClearButtonClicked = function(){
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();

    oDataIntegrationLogsViewData.timeStampData.isUserDateRangeApplied = false;
    oDataIntegrationLogsViewData.timeStampData.userDateRangeStartDate = "";
    oDataIntegrationLogsViewData.timeStampData.userDateRangeEndDate = "";
  };

  /**To Update the Filters */
  let _updateFilterValuesForDataIntegrationLogs = function (sContextKey, sId) {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    let iIndex = -1;
    switch (sContextKey) {
      case "endpoint":
        iIndex = oDataIntegrationLogsViewData.selectedEndpoints.indexOf(sId);
        oDataIntegrationLogsViewData.selectedEndpoints.splice(iIndex, 1);
        break;
      case "workflow":
        iIndex = oDataIntegrationLogsViewData.selectedWorkflows.indexOf(sId);
        oDataIntegrationLogsViewData.selectedWorkflows.splice(iIndex, 1);
        break;
      case "user":
        iIndex = oDataIntegrationLogsViewData.selectedUsers.indexOf(sId);
        oDataIntegrationLogsViewData.selectedUsers.splice(iIndex, 1);
        break;
      case "messageType":
        iIndex = oDataIntegrationLogsViewData.selectedMessageTypes.indexOf(sId);
        oDataIntegrationLogsViewData.selectedMessageTypes.splice(iIndex, 1);
        break;
    }
  };

  /**Handling Of Clear Button for Mss*/
  let _handleDataIntegrationLogsViewMSSClearButtonClicked = function (sContext) {
    let sSplitter = ContentUtils.getSplitter();
    let aSplitContext = CS.split(sContext, sSplitter);
    sContext = aSplitContext[1];
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    switch (sContext) {
      case "endpoint":
        oDataIntegrationLogsViewData.selectedEndpoints = [];
        break;
      case "workflow":
        oDataIntegrationLogsViewData.selectedWorkflows = [];
        break;
      case "user":
        oDataIntegrationLogsViewData.selectedUsers = [];
        oDataIntegrationLogsViewData.selectedUserIids = [];
        break;
      case "messageType":
        oDataIntegrationLogsViewData.selectedMessageTypes = [];
        break;
    }
  };

  /**To Download Logs */
  let _handleDataIntegrationLogsViewDownloadButtonClicked = function () {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    let oReqBody = {
      endpointIds: oDataIntegrationLogsViewData.selectedEndpoints,
      processEventIds: oDataIntegrationLogsViewData.selectedWorkflows,
      physicalCatalogs: oDataIntegrationLogsViewData.selectedPhysicalCatalogs,
      userIds: oDataIntegrationLogsViewData.selectedUserIids,
      messageTypes: oDataIntegrationLogsViewData.selectedMessageTypes,
      from: oDataIntegrationLogsViewData.timeStampData.userDateRangeStartDate,
      to: oDataIntegrationLogsViewData.timeStampData.userDateRangeEndDate
    };
    if (CS.isEmpty(oReqBody.endpointIds) && CS.isEmpty(oReqBody.processEventIds) && CS.isEmpty(oReqBody.physicalCatalogs) && CS.isEmpty(oReqBody.userIds) && CS.isEmpty(oReqBody.messageTypes) && CS.isEmpty(oReqBody.from + "") && CS.isEmpty(oReqBody.to + "")) {
      alertify.message(getTranslation().SELECT_ATLEAST_ONE_FILTER);
    } else {
      oDataIntegrationLogsViewData.isMessageDialogOpen = true;
    }
  };

  /**To Fetch Process Instance Data on the basis of Search */
  let _handleDataIntegrationLogsViewSearchButtonClicked = function () {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    let oReqBody = {
      endpointIds: oDataIntegrationLogsViewData.selectedEndpoints,
      processEventIds: oDataIntegrationLogsViewData.selectedWorkflows,
      userIds: oDataIntegrationLogsViewData.selectedUserIids,
      physicalCatalogs: oDataIntegrationLogsViewData.selectedPhysicalCatalogs,
      messageTypes: oDataIntegrationLogsViewData.selectedMessageTypes,
      from: oDataIntegrationLogsViewData.timeStampData.userDateRangeStartDate,
      to: oDataIntegrationLogsViewData.timeStampData.userDateRangeEndDate
    };

    if (CS.isEmpty(oReqBody.endpointIds) && CS.isEmpty(oReqBody.processEventIds) && CS.isEmpty(oReqBody.physicalCatalogs) && CS.isEmpty(oReqBody.userIds) && CS.isEmpty(oReqBody.messageTypes) && CS.isEmpty(oReqBody.from + "") && CS.isEmpty(oReqBody.to + "")) {
      alertify.message(getTranslation().SELECT_ATLEAST_ONE_FILTER);
    } else {
      JobProps.setActiveJob({});
      JobProps.setActiveJobGraphData({});
      CS.postRequest(DashboardScreenRequestMapping.GetProcessInstanceSearchData, "", oReqBody, successFetchProcessInstanceSearchData.bind(this), failureFetchProcessInstanceSearchData);
    }

  };

  let successFetchProcessInstanceSearchData = function (oResponse) {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    oDataIntegrationLogsViewData.jobList = oResponse.success;
    _triggerChange();
  };

  let failureFetchProcessInstanceSearchData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchProcessInstanceSearchData', getTranslation());
  };

  let _handleMessageTypeDialogButtonClicked = function (sButtonId) {
    if (sButtonId == "download") {
      _downloadExecutionStatus({});
    }
    else if (sButtonId == "cancel") {
      _closeMessageDialog();
    }
  };

  let _downloadExecutionStatus = function () {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    let oReqBody = {
      endpointIds: oDataIntegrationLogsViewData.selectedEndpoints,
      processEventIds: oDataIntegrationLogsViewData.selectedWorkflows,
      userIds: oDataIntegrationLogsViewData.selectedUserIids,
      physicalCatalogs: oDataIntegrationLogsViewData.selectedPhysicalCatalogs,
      messageTypes: oDataIntegrationLogsViewData.selectedMessageTypes,
      from: oDataIntegrationLogsViewData.timeStampData.userDateRangeStartDate,
      to: oDataIntegrationLogsViewData.timeStampData.userDateRangeEndDate
    };
    CS.postRequest(DashboardScreenRequestMapping.GetExecutionStatus, "", oReqBody, successFetchExecutionStatusData.bind(this), failureFetchExecutionStatusData);
    _closeMessageDialog();
  };

  let successFetchExecutionStatusData = function (oResponse) {
    if (CS.isEmpty(oResponse.success.fileStream)) {
      alertify.message(getTranslation().NO_LOGS_FOUND);
    } else {
      ContentUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
    }

  };

  let failureFetchExecutionStatusData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchExecutionStatusData', getTranslation());
  };

  let _closeMessageDialog = function () {
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    oDataIntegrationLogsViewData.isMessageDialogOpen = false;
    oDataIntegrationLogsViewData.selectedMessageTypes = [];
    _triggerChange();
  };

  return {

    getData: function () {
      return {
        appData: oAppData,
        componentProps: oComponentProps
      }
    },

    refreshAll: function () {
      _getCustomDashboardTabs();
      _triggerChange();
    },

    resetAll: function () {
      DashboardUtils.resetAll();
    },

    handleDashboardViewBarGroupClicked: function (sKpiId, sCategoryLabel, iCategoryIndex) {
      _handleDashboardViewBarGroupClicked(sKpiId, sCategoryLabel, iCategoryIndex);
      _triggerChange();
    },

    handleBreadCrumbClicked: function (sKpiId, sBreadCrumbId) {
      _handleBreadCrumbClicked(sKpiId, sBreadCrumbId);
      _triggerChange();
    },

    handleDashboardRefreshTile: function (oKpiData, oCallback) {
      _handleDashboardRefreshTile(oKpiData, oCallback);
    },

    handleRefreshEndpoint: function (sEndpointId, sTileMode, oCallback) {
      _handleRefreshEndpoint(sEndpointId, sTileMode, oCallback);
      _triggerChange();
    },

    handleDashboardTilesLoadMoreClicked: function (sContext, iNewLeft) {
      _handleDashboardTilesLoadMoreClicked(sContext, iNewLeft);
    },

    handleModuleItemClicked: function (sModuleId, sContext) {
      _handleModuleItemClicked(sModuleId, sContext);
    },

    handleDashboardEndpointTileModeChanged: function (sEndpointId, sMode, oCallback) {
      _handleDashboardEndpointTileModeChanged(sEndpointId, sMode, oCallback);
    },

    triggerChange: function () {
      _triggerChange();
    },

    handleDashboardTilePaginationButtonClick: function (sContext, sTileId) {
      _handleDashboardTilePaginationButtonClick(sContext, sTileId);
    },

    handleDashboardTileOpenDialogButtonClicked: function (sTileId) {
      _handleDashboardTileOpenDialogButtonClicked(sTileId);
    },

    handledDashboardViewCloseDialogClicked: function (sTileId) {
      _handledDashboardViewCloseDialogClicked(sTileId);
    },

    handleLogoClicked: function () {
      this.resetAll();
      this.refreshAll();
    },

    handleUILanguageChanged: function (sLanguage) {
      SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE, sLanguage);

      this.resetAll();
      this.refreshAll();
      //HomeScreenCommunicator.triggerHomeScreen();
      /*}*/
    },

    handleInvertKPIChartButtonClicked: function (oKpiData) {
      _handleInvertKPIChartButtonClicked(oKpiData);
    },

    handleDataIntegrationLogsViewRadioButtonClicked: function (sContext) {
      _handleDataIntegrationLogsViewRadioButtonClicked(sContext);
      _triggerChange();
    },

    handleDataIntegrationLogsViewLazyMssApplyClicked: function (sContext, aSelectedItems, oReferencedData) {
      _handleDataIntegrationLogsViewLazyMssApplyClicked(sContext, aSelectedItems, oReferencedData);
      _triggerChange();
    },

    handleDateRangeSelected: function (oSelectedTimeRange) {
      _handleDateRangeSelected(oSelectedTimeRange);
      _triggerChange();
    },

    handleDateRangeClearButtonClicked: function () {
      _handleDateRangeClearButtonClicked();
      _triggerChange();
    },

    updateFilterValuesForDataIntegrationLogs: function (sContextKey, sId) {
      _updateFilterValuesForDataIntegrationLogs(sContextKey, sId);
      _triggerChange();
    },

    handleDataIntegrationLogsViewDownloadButtonClicked: function () {
      _handleDataIntegrationLogsViewDownloadButtonClicked();
      _triggerChange();
    },

    handleDataIntegrationLogsViewSearchButtonClicked: function () {
      _handleDataIntegrationLogsViewSearchButtonClicked();
      _triggerChange();
    },

    handleMessageTypeDialogButtonClicked: function (sButtonId) {
      _handleMessageTypeDialogButtonClicked(sButtonId);
      _triggerChange();
    },

    handleDataIntegrationLogsViewMSSClearButtonClicked: function (sContext) {
      _handleDataIntegrationLogsViewMSSClearButtonClicked(sContext);
      _triggerChange();
    },

    handleGridViewRefreshButtonClicked: function () {
      _FetchBackgroundProcessData();
    },

     handleGridViewActionItemClicked : function (sActionItemId, sContentId) {
      _handleGridViewActionItemClicked(sActionItemId, sContentId)
     },

    handleBackgroundProcessDialogViewButtonClicked: function(sButtonId) {
      _handleBackgroundProcessDialogViewButtonClicked(sButtonId)
    },

    handleGridViewFilterButtonClicked: function (bShowFilterView) {
      _handleGridViewFilterButtonClicked(bShowFilterView);
    },

    handleGridViewColumnHeaderClicked: function (sColumnId) {
      _handleGridViewColumnHeaderClicked(sColumnId);
    },

    handleGridPaginationChanged : function (oNewPaginationData) {
      _handleGridPaginationChanged(oNewPaginationData);
    },

    handleGridFilterApplyClicked: function (oAppliedFilter, oReferencedData) {
      _handleGridFilterApplyClicked(oAppliedFilter, oReferencedData);
    },

    handleBGPServiceChanged: function (sId) {
      _handleBGPServiceChange(sId)
    }
  };

})();

MicroEvent.mixin(DashboardScreenStore);

TranslationStore.bind('translation-changed', DashboardScreenStore.triggerChange);


export default DashboardScreenStore;
