import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import MethodTracker from '../../../../../../libraries/methodtracker/method-tracker';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager';
import ScreenModeUtils from './screen-mode-utils';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import { communicator as HomeScreenCommunicator } from '../../../../store/home-screen-communicator';
import ContentScreenProps from './../model/content-screen-props';
import FilterStoreFactory from './filter-store-factory';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import RuleViolationIdHashcodeMapping from '../../tack/rule-violation-id-hashcode-mapping';
import DashboardUtils from '../../screens/dashboardscreen/store/helper/dashboard-utils';
import ContentUtils from './content-utils';
import BreadcrumbStore from '../../../../../../commonmodule/store/helper/breadcrumb-store';
import DashboardTabDictionary from '../../screens/dashboardscreen/tack/dashboard-tab-dictionary';
let getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

let InformationTabStore = (function () {

  let oComponentProps = ContentScreenProps;

  let _triggerChange = function () {
    MethodTracker.emptyCallTrace();
    InformationTabStore.trigger('Information-tab-changed');
  };

  let _getFilterParameters = function (sContext, sSortField, iFrom, iSize, sSortOrder = "desc") {
    let aSortOptions = [];
    if (!CS.isEmpty(sSortField)) {
      aSortOptions.push({
        sortField: sSortField,
        sortOrder: sSortOrder,
        isNumeric: true
      });
    }

    let sModule = "";

    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        sModule = "allmodule"
        break;

      case DashboardTabDictionary.DAM_TAB:
        sModule = "mammodule"
        break;
    }

    let sModuleId = ContentUtils.getIsDynamicCollectionScreen() ? ContentUtils.getSelectedModuleId() : sModule;

    return {
      sortOptions: aSortOptions,
      from: iFrom || 0,
      size: iSize || 20,
      moduleId: sModuleId
    }
  };

  let createEmptyDataForRuleViolationTile = function () {
    let aSequenceToDisplay = RuleViolationIdHashcodeMapping.ruleViolationList;
    let aDashboardRuleViolationTileData = [];
    CS.forEach(aSequenceToDisplay, function (oSequence) {
      let oChartData = {
        chartData: {
          data: {
            columns: [["data", ""]],
            type: "gauge",
          },
          color: {
            pattern: [
              oSequence.color
            ],
            threshold: {
              values: [
                100
              ]
            }
          },
          size: {
            height: 180
          }
        },
        label: null,
        id: oSequence.id,
        contentCount: 0,
        totalContentCount: 0,
        isLoading: true
      };
      aDashboardRuleViolationTileData.push(oChartData);
    });
    oComponentProps.informationTabProps.setDashboardRuleViolationTileData(aDashboardRuleViolationTileData);
  };

  let _fetchFilteredContentList = function (oCallback) {

    let { context: sContext } = oCallback;

    let aSortOptions = ["lastmodifiedattribute", "createdonattribute"];

    if (sContext == DashboardTabDictionary.DAM_TAB) {
      aSortOptions = CS.concat(aSortOptions, ["expiredassets", "endTime", "duplicateAssets"]);
    }

    let sModule = "";

    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        sModule = "allmodule"
        break;

      case DashboardTabDictionary.DAM_TAB:
        sModule = "mammodule"
        break;
    }

    let aFunctionToBeExecuted = [];
    let oPostDataForFilter = DashboardUtils.getFilterDataForDashboardTab();
    let oExtraData = oPostDataForFilter;
    oPostDataForFilter.moduleId = sModule;

    createEmptyDataForRuleViolationTile();
    BreadcrumbStore.addNewBreadCrumbItem(oCallback.breadCrumbData);

    let sUrl = getRequestMapping().GetRuleViolationInformation;
    aFunctionToBeExecuted.push(CS.postRequest.bind(this, sUrl, {}, oPostDataForFilter, successFetchRuleViolationInformationCallBack.bind(this, oCallback), failureFetchRuleViolationInformationCallBack, true, oExtraData));

    CS.forEach(aSortOptions, function (sSortField) {
      let sSortOrder = "desc";
      if (sContext == DashboardTabDictionary.DAM_TAB) {
        sSortOrder = (sSortField === "endTime") ? "asc" : "desc";
      }
      let oFilterParameters = _getFilterParameters(sContext, sSortField, 0, 10, sSortOrder);
      let oPostDataForFilter = DashboardUtils.getFilterDataForDashboardTab();
      let oExtraData = oPostDataForFilter;
      oPostDataForFilter.sortOptions = oFilterParameters.sortOptions;
      oPostDataForFilter.moduleId = oFilterParameters.moduleId;
      oPostDataForFilter.from = oFilterParameters.from;
      oPostDataForFilter.size = oFilterParameters.size;

      let oCallbackData = { sortField: sSortField };
      CS.assign(oCallbackData, oCallback);
      let sUrl = "";

      switch (sContext) {
        case DashboardTabDictionary.INFORMATION_TAB:
          sUrl = getRequestMapping().GetAllDashboardEntities;
          oComponentProps.informationTabProps.setLatestCreatedListLoading(true);
          oComponentProps.informationTabProps.setLastModifiedListLoading(true);
          break;

        case DashboardTabDictionary.DAM_TAB:
          switch (sSortField) {
            case "expiredassets":
              oPostDataForFilter.expiryStatus = -1;
              sUrl = getRequestMapping().GetAllDashboardEntities;
              break;
            case "endTime":
              sUrl = getRequestMapping().GetAssetsAboutToExpire;
              break;
            case "duplicateAssets":
              oCallbackData.sortField = "duplicateAssets";
              sUrl = getRequestMapping().GetDashboardDuplicateAssetsInfo;
              break;
            default:
              sUrl = getRequestMapping().GetAllDashboardEntities;
          }
          break;
      }

      aFunctionToBeExecuted.push(CS.postRequest.bind(this, sUrl, {}, oPostDataForFilter, successFetchContentListCallBack.bind(this, oCallbackData), failureFetchContentListCallBack.bind(this, oCallbackData), true, oExtraData));
    });
    CS.forEach(aFunctionToBeExecuted, function (oFunction) {
      oFunction();
    });

    return true;
  };

  let _handleDashboardTabClicked = function (oCallbackData) {
    let oScreenProps = oComponentProps.screen;
    oScreenProps.resetDataRuleFilterProps();
    oScreenProps.setReferencedClasses({});
    return _fetchFilteredContentList(oCallbackData);
  };

  let successFetchContentListCallBack = function (oCallbackData, oResponse) {
    let { context: sContext } = oCallbackData;

    try {
      let oResponseData = oResponse.success;
      let oInformationTabProps = {};
      switch (sContext) {
        case DashboardTabDictionary.INFORMATION_TAB:
          oInformationTabProps = oComponentProps.informationTabProps;
          break;

        case DashboardTabDictionary.DAM_TAB:
          oInformationTabProps = oComponentProps.damInformationTabProps;
          break;
      }
      if (oCallbackData.sortField === "lastmodifiedattribute") {
        switch (sContext) {
          case DashboardTabDictionary.INFORMATION_TAB:
            oInformationTabProps.setLastModifiedContentsList(oResponseData.children);
            oInformationTabProps.setLastModifiedListLoading(false);
            break;

          case DashboardTabDictionary.DAM_TAB:
            oInformationTabProps.setLastModifiedAssetList(oResponseData.children);
            break;
        }
      }
      else if (oCallbackData.sortField === "createdonattribute") {
        switch (sContext) {
          case DashboardTabDictionary.INFORMATION_TAB:
            oInformationTabProps.setLatestCreatedContentsList(oResponseData.children);
            oInformationTabProps.setLatestCreatedListLoading(false);
            break;

          case DashboardTabDictionary.DAM_TAB:
            oInformationTabProps.setLatestCreatedAssetList(oResponseData.children);
            break;
        }
      }
      else if (oCallbackData.sortField === "expiredassets") {
        oInformationTabProps.setExpiredAssetList(oResponseData.children);
      }
      else if (oCallbackData.sortField === "endTime") {
        oInformationTabProps.setAssetAboutToExpireList(oResponseData.children);
      }
      else if (oCallbackData.sortField === "duplicateAssets") {
        oInformationTabProps.setDuplicateAssetsList(oResponseData.children);
      }
      //oComponentProps.screen.setReferencedClasses(oResponseData.referencedKlasses);
      let oExistingReferencedClasses = oComponentProps.screen.getReferencedClasses();
      CS.assign(oExistingReferencedClasses, oResponseData.referencedKlasses);
      HomeScreenCommunicator.disablePhysicalCatalog(false);

    } catch (oException) {
      ExceptionLogger.error(oException);
    } finally {
      _triggerChange();  //currently getting null in the oResponse

    }
  };

  let successFetchRuleViolationInformationCallBack = function (oCallbackData, oResponse) {
    let { context: sContext } = oCallbackData;
    let oResponseData = oResponse.success;
    let oFilterInfo = oResponseData.filterInfo;
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallbackData.filterContext);
    oFilterStore.setFilterInfo(oFilterInfo);
    ContentUtils.setFilterProps(oFilterStore.getFilterInfo(), false, oCallbackData.filterContext);
    prepareDataForRuleViolationTiles(oResponseData.totalContents, oCallbackData.filterContext, sContext);
    HomeScreenCommunicator.disablePhysicalCatalog(false);
    _triggerChange();

  };

  let failureFetchRuleViolationInformationCallBack = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureRuleViolationInformationCallBack', getTranslation());
    return false;
  };

  let failureFetchContentListCallBack = function (oCallbackData, oResponse) {
    let { context: sContext } = oCallbackData;
    let sFunctionName = ""
    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        sFunctionName = 'failureFetchContentListCallBack';
        break;

      case DashboardTabDictionary.DAM_TAB:
        sFunctionName = 'failureFetchAssetListCallBack';
        break;
    }
    ContentUtils.failureCallback(oResponse, sFunctionName, getTranslation());
    return false;
  };

  let prepareDataForRuleViolationTiles = function (iTotalContents, filterContext, sContext) {
    let oInformationTabProps = {};
    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        oInformationTabProps = oComponentProps.informationTabProps;
        break;

      case DashboardTabDictionary.DAM_TAB:
        oInformationTabProps = oComponentProps.damInformationTabProps;
        break;
    }
    let oFilterProps = ContentUtils.getFilterProps(filterContext);
    let aAvailableFilterData = oFilterProps.getAvailableFilters();
    let oDataRuleFilterData = CS.find(aAvailableFilterData, { id: "colorVoilation" });
    let aSequenceToDisplay = RuleViolationIdHashcodeMapping.ruleViolationList;
    let aDataRuleChildren = oDataRuleFilterData.children;
    let aDashboardRuleViolationTileData = [];
    CS.forEach(aSequenceToDisplay, function (oSequence) {
      let oChildren = CS.find(aDataRuleChildren, { id: oSequence.id });
      let iPercentage = CS.round((oChildren.count * 100) / iTotalContents, 1);
      let oChartData = {
        chartData: {
          data: {
            columns: [["data", iPercentage]],
            type: "gauge",
          },
          color: {
            pattern: [
              oSequence.color
            ],
            threshold: {
              values: [
                100
              ]
            }
          },
          size: {
            height: 180
          }
        },
        label: oChildren.label,
        id: oChildren.id,
        contentCount: oChildren.count,
        totalContentCount: iTotalContents,
        isLoading: false,
      };
      aDashboardRuleViolationTileData.push(oChartData);
    });
    oInformationTabProps.setDashboardRuleViolationTileData(aDashboardRuleViolationTileData);
  };

  let _handleInformationTabRuleViolationItemClicked = function (oCallbackData, oExtraData) {
    let { context: sContext } = oCallbackData;
    let oInformationTabProps = {};
    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        oInformationTabProps = oComponentProps.informationTabProps;
        break;

      case DashboardTabDictionary.DAM_TAB:
        oInformationTabProps = oComponentProps.damInformationTabProps;
        break;
    }
    oInformationTabProps.setIsRuleViolatedContentsScreen(true);
    let oContentStore = ContentUtils.getContentStore();

    oCallbackData = oCallbackData || {};
    oExtraData = oExtraData || {};

    let sType, sHelpScreenId, sId;
    sId = sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION;

    if (sContext == DashboardTabDictionary.DAM_TAB) {
      sId = BreadCrumbModuleAndHelpScreenIdDictionary.ASSET_RULE_VIOLATION;
    }

    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(sId, getTranslation().RULE_VIOLATIONS, sType, sHelpScreenId, oCallbackData.filterContext);

    oCallbackData.preventResetFilterProps = true;
    oContentStore.fetchContentList(oCallbackData, oExtraData);
  };

  let _fetchAssetClassList = function (oFilterContext, sContext) {
    let oFilterParameters = {
      sortOptions: [],
      from: 0,
      size: 0,
      moduleId: "mammodule"
    };
    let sContentId = ContentUtils.getTreeRootNodeId();
    let oData = {
      id: sContentId,
    };

    let oPostDataForFilter = ContentUtils.createFilterPostData(oFilterParameters, oFilterContext);
    let sUrl = getRequestMapping().GetAllEntities;
    let oCallback = { context: sContext }
    CS.postRequest(sUrl, oData, oPostDataForFilter, successFetchClassListCallBack.bind(this, oCallback), failureFetchClassListCallBack.bind(this, oCallback));
  };

  let successFetchClassListCallBack = function (oCallback, oResponse) {
    let { context: sContext } = oCallback;
    let oInformationTabProps = {};
    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        oInformationTabProps = oComponentProps.informationTabProps;
        break;

      case DashboardTabDictionary.DAM_TAB:
        oInformationTabProps = oComponentProps.damInformationTabProps;
        break;
    }
    let aAssetClassList = oResponse.success.defaultTypes.children;
    oInformationTabProps.setAssetClassList(aAssetClassList);
    _triggerChange();
  };

  let failureFetchClassListCallBack = function (oCallback, oResponse) {
    let { context: sContext } = oCallbackData; // eslint-disable-line
    let sFunctionName = ""
    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        sFunctionName = 'failureFetchContentListCallBack';
        break;

      case DashboardTabDictionary.DAM_TAB:
        sFunctionName = 'failureFetchAssetListCallBack';
        break;
    }
    ContentUtils.failureCallback(oResponse, sFunctionName, getTranslation());
    return false;
  };

  let _resetInformationTabData = function () {
    oComponentProps.informationTabProps.reset();
  };


  return {

    getData: function () {
      return {
        componentProps: oComponentProps
      }
    },

    handleDashboardTabClicked: function (oCallbackData) {
      return _handleDashboardTabClicked(oCallbackData);
    },

    handleInformationTabRuleViolationItemClicked: function (oCallbackData, oExtraData) {
      _handleInformationTabRuleViolationItemClicked(oCallbackData, oExtraData);
    },

    getFilterParameters: function (sContext, sSortField, iFrom, iSize) {
      return _getFilterParameters(sContext, sSortField, iFrom, iSize);
    },

    fetchAssetClassList: function (oFilterContext, sContext) {
      _fetchAssetClassList(oFilterContext, sContext);
    },

    resetInformationTabData: function () {
      _resetInformationTabData();
    }

  };

})();

MicroEvent.mixin(InformationTabStore);

export default InformationTabStore;
