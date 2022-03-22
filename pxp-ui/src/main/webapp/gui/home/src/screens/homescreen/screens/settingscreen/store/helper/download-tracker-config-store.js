import MicroEvent from "../../../../../../libraries/microevent/MicroEvent";
import SettingUtils from "./setting-utils";
import {getTranslations as oTranslations} from "../../../../../../commonmodule/store/helper/translation-manager";
import CS from "../../../../../../libraries/cs";
import SettingScreenProps from "../model/setting-screen-props";
import DownloadTrackerSkeleton from '../../tack/download-tracker-grid-view-skeleton';
import GridViewContexts from "../../../../../../commonmodule/tack/grid-view-contexts";
import {DownloadTrackerRequestMapping as oDownloadTrackerRequestMapping} from '../../tack/setting-screen-request-mapping';
import DownloadTrackerFilterModelDictionary
  from "../../../../../../commonmodule/tack/config-download-tracker-filter-model-dictionary"
import oDownloadTrackerDictionary from "../../tack/download-tracker-dictionary"
import MomentUtils from "../../../../../../commonmodule/util/moment-utils";
import MockDataForDateFilter from "../../../../../../commonmodule/tack/mock-data-for-audit-log-date-filter";
import moment from "moment";


let DownloadTrackerConfigStore = (function () {

  let _triggerChange = function () {
    DownloadTrackerConfigStore.trigger('download-tracker-config-changed');
  };

  let _resetGridData = function () {
    SettingScreenProps.downloadLogProps.reset();
    SettingScreenProps.screen.setGridViewSkeleton(new DownloadTrackerSkeleton());
    SettingScreenProps.screen.setGridViewSortBy("timeStamp");
    SettingScreenProps.screen.setGridViewSortOrder("desc");
  };

  let getStartAndEndTime = function (sId) {
    let oTimeStamp = {};
    switch (sId) {
      case "today":
        oTimeStamp.startTime = moment().startOf('day');
        oTimeStamp.endTime = moment();
        break;
      case "yesterday":
        oTimeStamp.startTime = moment().subtract(1, 'days').startOf('day');
        oTimeStamp.endTime = moment().subtract(1, 'days').endOf('day');
        break;
      case "last7Days":
        oTimeStamp.startTime = moment().subtract(6, 'days');
        oTimeStamp.endTime = moment();
        break;
      case "last30Days":
        oTimeStamp.startTime = moment().subtract(29, 'days');
        oTimeStamp.endTime = moment();
        break;
      case "thisMonth":
        oTimeStamp.startTime = moment().startOf('month');
        oTimeStamp.endTime = moment().endOf('month');
        break;
    }
    return oTimeStamp;
  };

  let prepareGridViewFilterModel = function () {
    let aGridViewFilterModel = [];
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    let aAppliedFilterData = oDownloadLogProps.getAppliedFilterData();
    let aFilterSearchData = oDownloadLogProps.getSearchFilterData();
    let aDownloadTrackerFilterModelDictionary = new DownloadTrackerFilterModelDictionary();
    CS.forEach(CS.concat(aAppliedFilterData, aFilterSearchData), function (oFilter) {
      let oFilterModel = {};
      switch(oFilter.id) {
        case oDownloadTrackerDictionary.DATE:
          oFilterModel.filterField = oDownloadTrackerDictionary.TIMESTAMP;
          if (CS.isNotEmpty(oFilter.children) && CS.isNotEmpty(MockDataForDateFilter[oFilter.children[0]])) {
            let oTimeMoment = getStartAndEndTime(oFilter.children[0]);
            let iStartTimestamp = oTimeMoment.startTime;
            let iEndTimestamp = oTimeMoment.endTime;
            oFilterModel.filterValues = {startTime: iStartTimestamp.valueOf(), endTime: iEndTimestamp.valueOf()};
          } else {
            oFilterModel.filterValues = oFilter.value;
          }
          oFilterModel.filterType = aDownloadTrackerFilterModelDictionary.TIME_RANGE;
          break;
        case oDownloadTrackerDictionary.COMMENT:
        case oDownloadTrackerDictionary.ASSET_FILE_NAME:
        case oDownloadTrackerDictionary.RENDITION_FILENAME:
        case oDownloadTrackerDictionary.ASSET_INSTANCE_NAME:
        case oDownloadTrackerDictionary.RENDITION_INSTANCE_NAME:
          let sValue = oFilter.value.trim();
          if (CS.isNotEmpty(sValue)) {
            oFilterModel.filterField = oFilter.id;
            oFilterModel.filterValues = oFilter.value;
            oFilterModel.filterType = aDownloadTrackerFilterModelDictionary.TEXT;
          } else {
            CS.remove(aFilterSearchData, {id: oFilter.id});
            return;
          }
          break;
        case oDownloadTrackerDictionary.DOWNLOADED_BY:
        case oDownloadTrackerDictionary.ASSET_INSTANCE_CLASS_NAME:
        case oDownloadTrackerDictionary.RENDITION_INSTANCE_CLASS_NAME:
          if (CS.isNotEmpty(oFilter.children)) {
            oFilterModel.filterField = oFilter.id;
            oFilterModel.filterValues = _getValuesFromIdsArray(oFilter.children);
            oFilterModel.filterType = aDownloadTrackerFilterModelDictionary.LIST;
          } else {
            CS.remove(aAppliedFilterData, {id: oFilter.id});
            return;
          }
          break;
      }
      aGridViewFilterModel.push(oFilterModel);
    });

    oDownloadLogProps.setAppliedFilterData(aAppliedFilterData);
    oDownloadLogProps.setSearchFilterData(aFilterSearchData);

    return aGridViewFilterModel;
  };

  let _getValuesFromIdsArray = function (aChildren) {
    let aIdsArray = [];
    CS.forEach(aChildren, function (oChild) {
      aIdsArray.push(oChild.id);
    });

    return aIdsArray;
  };

  let getSortOptions = function () {
    let sSortField = "";
    let sGridViewSortBy = SettingScreenProps.screen.getGridViewSortBy();
    switch (sGridViewSortBy) {
      case oDownloadTrackerDictionary.DOWNLOADED_BY:
        sSortField = oDownloadTrackerDictionary.USER_NAME;
        break;
      case oDownloadTrackerDictionary.DATE:
        sSortField = oDownloadTrackerDictionary.TIMESTAMP;
        break;
      default:
        sSortField = sGridViewSortBy;
    }

    return {
      sortField: sSortField,
      sortOrder: SettingScreenProps.screen.getGridViewSortOrder()
    }
  };

  let _getDownloadLogs = function () {
    let oGridPaginationData = SettingScreenProps.screen.getGridViewPaginationData();
    let oPostData = {
      from: oGridPaginationData.from,
      size: oGridPaginationData.pageSize,
      sortOptions: getSortOptions(),
      gridViewFilterModel: prepareGridViewFilterModel(),
    };

    CS.postRequest(oDownloadTrackerRequestMapping.GetDownloadLogs, {}, oPostData, successGetDownloadLogs, failureGetDownloadLogs);
  };

  let successGetDownloadLogs = function (oResponse) {
    let oResponseData = oResponse.success;
    let oScreenProps = SettingScreenProps.screen;
    let oDownloadTrackerSkeleton = oScreenProps.getGridViewSkeleton();
    oScreenProps.setGridViewSkeleton(oDownloadTrackerSkeleton);
    let aGridViewData = _preProcessDataForGridView(oResponseData.downloadLogList, oDownloadTrackerSkeleton);
    oScreenProps.setGridViewData(aGridViewData);
    oScreenProps.setGridViewTotalItems(oResponseData.totalCount);
    oScreenProps.setGridViewContext(GridViewContexts.DOWNLOAD_TRACKER);
    _triggerChange();
  };

  let failureGetDownloadLogs = function (oResponse) {
    _resetGridData();
    SettingUtils.failureCallback(oResponse, "failureDownloadTrackerConfigTab", oTranslations());
    _triggerChange();
  };

  let _getAssetGridProperties = function (oProcessedAsset, oDownloadedAsset, oColumns) {
    CS.forEach(oColumns, function (oColumn) {
      switch (oColumn.id) {
        case oDownloadTrackerDictionary.DATE:
          oProcessedAsset.properties[oColumn.id] = {
            value: oDownloadedAsset[oDownloadTrackerDictionary.TIMESTAMP],
            isDisabled: true,
            isExpandable: false,
            isHideDisabledMask: true,
            bIsMultiLine: true,
      };
          break;

        case oDownloadTrackerDictionary.ASSET_INSTANCE_CLASS_NAME :
        case oDownloadTrackerDictionary.RENDITION_INSTANCE_CLASS_NAME:
        case oDownloadTrackerDictionary.DOWNLOADED_BY:
          let sValue = oDownloadedAsset[oColumn.id];
          if (CS.isEmpty(sValue)) {
            let sKey = _getColumnKey(oColumn.id);
            let sId = oDownloadedAsset[sKey];
            let bIsDisabled = CS.isEmpty(sId) ? false : true;
            oProcessedAsset.properties[oColumn.id] = {
              value: sId,
              isDisabled: bIsDisabled,
              isExpandable: false,
              bIsMultiLine: true,
            };
          } else {
            oProcessedAsset.properties[oColumn.id] = {
              value: oDownloadedAsset[oColumn.id],
              isDisabled: true,
              isExpandable: false,
              isHideDisabledMask: true,
              bIsMultiLine: true,
            };
          }
          break;

        default:
          oProcessedAsset.properties[oColumn.id] = {
            value: oDownloadedAsset[oColumn.id],
            isDisabled: true,
            isExpandable: false,
            isHideDisabledMask: true,
            bIsMultiLine: true,
          };
      }
    });
    return oProcessedAsset;
  };

  let _getColumnKey = function (sColumnId) {
    if (sColumnId === oDownloadTrackerDictionary.ASSET_INSTANCE_CLASS_NAME) {
      return oDownloadTrackerDictionary.ASSET_INSTANCE_CLASS_ID;
    }
    if (sColumnId === oDownloadTrackerDictionary.RENDITION_INSTANCE_CLASS_NAME) {
      return oDownloadTrackerDictionary.RENDITION_INSTANCE_CLASS_ID;
    }
    if (sColumnId === oDownloadTrackerDictionary.DOWNLOADED_BY) {
      return oDownloadTrackerDictionary.USER_ID;
    }
  };

  let _preProcessDataForGridView = function (aDownloadAssetList, oGridSkeleton) {
    let aGridViewData = [];
    CS.forEach(aDownloadAssetList, function (oDownloadedAsset) {
      let oProcessedAsset = {};
      oProcessedAsset.id = oDownloadedAsset.primaryId;
      oProcessedAsset.isExpanded = false;
      oProcessedAsset.children = [];
      oProcessedAsset.properties = {};
      oProcessedAsset.children = [];
      oProcessedAsset.pathToRoot = oDownloadedAsset.primaryId;
      //setting the properties of grid data
      oProcessedAsset = _getAssetGridProperties(oProcessedAsset, oDownloadedAsset, oGridSkeleton.scrollableColumns);
      oProcessedAsset = _getAssetGridProperties(oProcessedAsset, oDownloadedAsset, oGridSkeleton.fixedColumns);

      aGridViewData.push(oProcessedAsset);
    });

    return aGridViewData;
  };

  let _handleDownloadLogsClicked = function () {
    let oGridViewSkeleton = SettingScreenProps.screen.getGridViewSkeleton();

    let oPostData = {
      primaryKeys: oGridViewSkeleton.selectedContentIds,
      gridViewFilterModel: prepareGridViewFilterModel(),
      sortOptions: getSortOptions(),
    };

    CS.postRequest(oDownloadTrackerRequestMapping.DownloadLogs, {}, oPostData, successDownloadLogs, failureDownloadLogs);
  };

  let successDownloadLogs = function (oResponse) {
    let oResponseData =  oResponse.success;
    _downloadCSVLogsFromByteArray(oResponseData.csvBytes);
  };

  let failureDownloadLogs = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureDownloadLogs", oTranslations());
  };

  let _downloadCSVLogsFromByteArray = function (oCsvBytes) {
    var sByteCharacters = atob(oCsvBytes);
    var oByteNumbers = new Array(sByteCharacters.length);
    for (var i = 0; i < sByteCharacters.length; i++) {
      oByteNumbers[i] = sByteCharacters.charCodeAt(i);
    }
    var aByteArray = new Uint8Array(oByteNumbers);
    let oFile = new Blob([aByteArray], {type: 'data:text/csv;charset=utf-8'});
    let sFileURL = window.URL.createObjectURL(oFile);
    var downloadLink = document.createElement("a");
    downloadLink.href = sFileURL;
    downloadLink.download = "DownloadLogs.csv";
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
  };

  let _handleCollapseFilterClicked = function (bIsExpanded) {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    oDownloadLogProps.setIsFilterExpanded(bIsExpanded);
  };

  let _handleApplyFilterButtonClicked = function () {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    if (oDownloadLogProps.getIsFilterDirty() || oDownloadLogProps.getIsSearchDirty()) {
      CS.isNotEmpty(oDownloadLogProps.getAppliedFilterClonedData()) && oDownloadLogProps.setAppliedFilterData(oDownloadLogProps.getAppliedFilterClonedData());
      CS.isNotEmpty(oDownloadLogProps.getSearchFilterClonedData()) && oDownloadLogProps.setSearchFilterData(oDownloadLogProps.getSearchFilterClonedData());
      oDownloadLogProps.setAppliedFilterClonedData([]);
      oDownloadLogProps.setSearchFilterClonedData([]);
      oDownloadLogProps.setIsFilterDirty(false);
      oDownloadLogProps.setIsSearchDirty(false);
      _getDownloadLogs();
    }
  };

  let _handleFilterItemPopoverClosed = function () {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    oDownloadLogProps.setAvailableFilterSearchData({});
  };

  let _handleFilterItemSearchTextChanged = function (sId, sSearchedText) {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    let oAvailableFilterSearchData = oDownloadLogProps.getAvailableFilterSearchData();
    oAvailableFilterSearchData[sId] = sSearchedText;
  };

  let _handleFilterSummarySearchTextChanged = function (sFilterId, sSearchedText) {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    let aSearchFilterData = oDownloadLogProps.getSearchFilterClonedData();
    aSearchFilterData = CS.isEmpty(aSearchFilterData) ? CS.cloneDeep(oDownloadLogProps.getSearchFilterData()) : aSearchFilterData;

    let oFilter = CS.find(aSearchFilterData, {id: sFilterId});

    if (CS.isEmpty(oFilter) && CS.isNotEmpty(sSearchedText)) {
      oDownloadLogProps.setIsSearchDirty(true);
      oFilter = {
        id: sFilterId,
        value: sSearchedText
      };
      aSearchFilterData.push(oFilter);
      oDownloadLogProps.setSearchFilterClonedData(aSearchFilterData);
    } else if (oFilter.value !== sSearchedText) {
      oDownloadLogProps.setIsSearchDirty(true);
      oFilter.value = sSearchedText;
      oDownloadLogProps.setSearchFilterClonedData(aSearchFilterData);
    }
  };

  let _handleDateRangePickerUpdateData = function (oRange, sId) {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    let aAppliedFilterData = oDownloadLogProps.getAppliedFilterClonedData();
    aAppliedFilterData = CS.isEmpty(aAppliedFilterData) ? CS.cloneDeep(oDownloadLogProps.getAppliedFilterData()) : aAppliedFilterData;

    let oTimestampFilter = CS.find(aAppliedFilterData, {id: oDownloadTrackerDictionary.DATE});

    sId = sId == "custom" ? MomentUtils.getShortDate(+oRange.startDate) + " - " + MomentUtils.getShortDate(+oRange.endDate) : sId;
    if (CS.isEmpty(oTimestampFilter)) {
      let oFilter = {
        id: oDownloadTrackerDictionary.DATE,
        children: [sId],
        value: {
          startTime: +(oRange.startDate),
          endTime: +(oRange.endDate),
        }
      };
      aAppliedFilterData.push(oFilter);
    } else {
      oTimestampFilter.children = CS.isEmpty(sId) ? [] : [sId];
      oTimestampFilter.isDefault = false;
      oTimestampFilter.value.startTime = +(oRange.startDate);
      oTimestampFilter.value.endTime = +(oRange.endDate);
    }

    oDownloadLogProps.setAppliedFilterClonedData(aAppliedFilterData);
    oDownloadLogProps.setIsFilterDirty(true);
  };

  let _handleDownloadLogFilterChildToggled = function (sParentId, sChildId, sLabel) {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    let aAppliedFilterData = oDownloadLogProps.getAppliedFilterClonedData();
    aAppliedFilterData = CS.isEmpty(aAppliedFilterData) ? CS.cloneDeep(oDownloadLogProps.getAppliedFilterData())
                                                        : aAppliedFilterData;
    let oFilter = CS.find(aAppliedFilterData, {id: sParentId});

    if (CS.isEmpty(oFilter)) {
      oFilter = {
        id: sParentId,
        children: [{id: sChildId, label: sLabel}],
      };
      aAppliedFilterData.push(oFilter);
    } else {
      CS.find(oFilter.children, {id: sChildId}) ? CS.remove(oFilter.children, oChild => sChildId == oChild.id)
                                                : oFilter.children.push({id: sChildId, label: sLabel});
    }
    oDownloadLogProps.setAppliedFilterClonedData(aAppliedFilterData);
    oDownloadLogProps.setIsFilterDirty(true);
  };

  let _handleAppliedFilterClearClicked = function () {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    oDownloadLogProps.setAppliedFilterData([]);
    oDownloadLogProps.setAppliedFilterClonedData([]);
    oDownloadLogProps.setSearchFilterClonedData([]);
    oDownloadLogProps.setSearchFilterData([]);
    oDownloadLogProps.setIsFilterDirty(false);
    oDownloadLogProps.setIsSearchDirty(false);
    _getDownloadLogs();
  };

  let _handleRemoveAppliedFilterClicked = function (sParentId) {
    let oDownloadLogProps = SettingScreenProps.downloadLogProps;
    let aAppliedFilterData = oDownloadLogProps.getAppliedFilterData();
    CS.remove(aAppliedFilterData, {id: sParentId});
    _getDownloadLogs();
  };

  return {

    getDownloadLogs: function () {
      _getDownloadLogs();
    },

    handleDownloadLogsClicked: function () {
      _handleDownloadLogsClicked();
    },

    resetGridData: function () {
      _resetGridData();
    },

    handleCollapseFilterClicked (bIsExpanded) {
      _handleCollapseFilterClicked(bIsExpanded);
      _triggerChange();
    },

    handleApplyFilterButtonClicked () {
      _handleApplyFilterButtonClicked();
    },

    handleFilterItemPopoverClosed () {
      _handleFilterItemPopoverClosed();
      _triggerChange();
    },

    handleFilterItemSearchTextChanged (sId, sSearchedText) {
      _handleFilterItemSearchTextChanged(sId, sSearchedText);
      _triggerChange();
    },

    handleFilterSummarySearchTextChanged (sFilterId, sSearchedText) {
      _handleFilterSummarySearchTextChanged(sFilterId, sSearchedText);
      _triggerChange();
    },

    handleDateRangePickerUpdateData (oRange, sId) {
      _handleDateRangePickerUpdateData(oRange, sId);
      _triggerChange();
    },

    handleDownloadLogFilterChildToggled (sParentId, sChildId, sLabel) {
      _handleDownloadLogFilterChildToggled(sParentId, sChildId, sLabel);
      _triggerChange();
    },

    handleAppliedFilterClearClicked () {
      _handleAppliedFilterClearClicked();
    },

    handleRemoveAppliedFilterClicked (sElementId) {
      _handleRemoveAppliedFilterClicked(sElementId);
    },

  };

})();

MicroEvent.mixin(DownloadTrackerConfigStore);
export default DownloadTrackerConfigStore;
