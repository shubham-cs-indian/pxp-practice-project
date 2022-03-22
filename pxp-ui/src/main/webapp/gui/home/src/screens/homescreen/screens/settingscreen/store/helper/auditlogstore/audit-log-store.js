import CS from '../../../../../../../libraries/cs';
import MicroEvent from "../../../../../../../libraries/microevent/MicroEvent";
import SettingScreenProps from './../../model/setting-screen-props';
import AuditLogGridSkeleton from '../../../tack/audit-log-grid-view-skeleton';
import ShowExportStatusGridViewSkeleton from '../../../tack/export-status-grid-view-skeleton';
import AuditLogProvider from './audit-log-provider';
import SettingUtils from "../setting-utils";
import {getTranslations as getTranslation} from "../../../../../../../commonmodule/store/helper/translation-manager";
import MomentUtils from '../../../../../../../commonmodule/util/moment-utils';
import GridViewContexts from '../../.././../../../../commonmodule/tack/grid-view-contexts';
import {AuditLogRequestMapping as oAuditLogRequestMapping} from '../../../tack/setting-screen-request-mapping';
import alertify from '../../../../../../../commonmodule/store/custom-alertify-store';
import GlobalStore from '../../../../../store/global-store';
import GridViewStore from "../../../../../../../viewlibraries/contextualgridview/store/grid-view-store";


let AuditLogStore = (function () {
  let _successFetchAuditLogListForGridView = function (oResponse) {
    let aAuditLogList = oResponse.auditLogList;
    GridViewStore.preProcessDataForGridView(GridViewContexts.AUDIT_LOG, aAuditLogList, oResponse.count);
    this.triggerChange();
  };

  let _failureFetchAuditLogListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAuditLogListForGridView", getTranslation());
  };

/*
  let _processDataForGrid = function (oResponse) {
    let aGridData = [];
    CS.forEach(oResponse, function (oLog) {
      let oData = {id: oLog.activityId};
      let oProperties = {};
      CS.forEach(oLog, function (sValue, sKey) {
        oProperties[sKey] = {
          isDisabled: true,
          isHideDisabledMask: true,
        };
        if (sKey === "activity" || sKey === "entityType" || sKey === "element" || sKey === "elementType") {
          oProperties[sKey].value = getTranslation()[sValue];
        } else {
          oProperties[sKey].value = sValue;
        }
      });
      oData.properties = oProperties;
      aGridData.push(oData);
    });
    return aGridData
  };
*/

  class AuditLogStore {
    triggerChange () {
      this.trigger('audit-log-changed');
    };

    setUpAuditLogGridView () {
      let oData = {
        skeleton: AuditLogGridSkeleton(),
        sortBy: "activityId",
        sortOrder: "desc",
        from: 0,
        pageSize: 60,
      };
      GridViewStore.createGridViewPropsByContext(GridViewContexts.AUDIT_LOG, oData)
      this.fetchAuditLogListForGridView();
    }

    fetchAuditLogListForGridView () {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      if (oAuditLogProps.getIsShowExportStatusDialogActive()) {

      } else {
        AuditLogProvider.fetchAuditLogListForGridView(_successFetchAuditLogListForGridView.bind(this), _failureFetchAuditLogListForGridView);
      }
    }

    handleAuditLogFilterChildToggled (sParentId, sChildId) {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      let aAppliedFilterData = oAuditLogProps.getAppliedFilterClonedData();
      aAppliedFilterData = CS.isEmpty(aAppliedFilterData) ? CS.cloneDeep(oAuditLogProps.getAppliedFilterData()) : aAppliedFilterData;
      let oFilter = CS.find(aAppliedFilterData, {id: sParentId});

      if (CS.isEmpty(oFilter)) {
        oFilter = {
          id: sParentId,
          children: [{id: sChildId}],
        };
        aAppliedFilterData.push(oFilter);
      } else {
        CS.find(oFilter.children, {id: sChildId}) ? CS.remove(oFilter.children, oChild => sChildId == oChild.id) : oFilter.children.push({id: sChildId});
      }
      oAuditLogProps.setAppliedFilterClonedData(aAppliedFilterData);
      oAuditLogProps.setIsFilterDirty(true);
      this.triggerChange();
    };

    handleRemoveAppliedFilterClicked (sParentId) {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      let aAppliedFilterData = oAuditLogProps.getAppliedFilterData();
      CS.remove(aAppliedFilterData, {id: sParentId});
      this.fetchAuditLogListForGridView();
    }

    handleAppliedFilterClearClicked () {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      oAuditLogProps.setAppliedFilterData([]);
      oAuditLogProps.setAppliedFilterClonedData([]);
      oAuditLogProps.setSearchFilterData([]);
      oAuditLogProps.setIsFilterDirty(false);
      this.fetchAuditLogListForGridView();
    }

    handleFilterSummarySearchTextChanged (sFilterId, sSearchedText) {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      let aSearchFilterData = oAuditLogProps.getSearchFilterData();
      if (!aSearchFilterData.clonedObject) {
        aSearchFilterData.clonedObject = CS.cloneDeep(aSearchFilterData);
      }
      let oFilter = CS.find(aSearchFilterData, {id: sFilterId});

      if (CS.isEmpty(oFilter) && CS.isNotEmpty(sSearchedText)) {
        oAuditLogProps.setIsFilterDirty(true);
        oFilter = {
          id: sFilterId,
          value: sSearchedText
        };
        aSearchFilterData.push(oFilter);
      } else if (oFilter.value !== sSearchedText) {
        oAuditLogProps.setIsFilterDirty(true);
        oFilter.value = sSearchedText;
      }

      this.triggerChange();
    }

    handleDateRangePickerUpdateData (oRange, sId) {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      let aAppliedFilterData = oAuditLogProps.getAppliedFilterClonedData();
      aAppliedFilterData = CS.isEmpty(aAppliedFilterData) ? CS.cloneDeep(oAuditLogProps.getAppliedFilterData()) : aAppliedFilterData;

      let oTimestampFilter = CS.find(aAppliedFilterData, {id: "date"});

      sId = sId === "custom" ? MomentUtils.getShortDate(+oRange.startDate) + " - " + MomentUtils.getShortDate(+oRange.endDate) : sId;
      if (CS.isEmpty(oTimestampFilter)) {
        let oFilter = {
          id: "date",
          children: [sId],
          value: {
            startTime: +(oRange.startDate),
            endTime: +(oRange.endDate),
          }
        };
        aAppliedFilterData.push(oFilter);
      } else {
        oTimestampFilter.children = [sId];
        oTimestampFilter.isDefault = false;
        oTimestampFilter.value.startTime = +(oRange.startDate);
        oTimestampFilter.value.endTime = +(oRange.endDate);
      }

      oAuditLogProps.setAppliedFilterClonedData(aAppliedFilterData);
      oAuditLogProps.setIsFilterDirty(true);
      this.triggerChange();
    }

    handleCollapseFilterClicked (bExpanded) {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      oAuditLogProps.setIsFilterExpanded(bExpanded);
      this.triggerChange();
    }

    handleApplyFilterButtonClicked () {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      if (oAuditLogProps.getIsFilterDirty()) {
        CS.isNotEmpty(oAuditLogProps.getAppliedFilterClonedData()) && oAuditLogProps.setAppliedFilterData(oAuditLogProps.getAppliedFilterClonedData());
        oAuditLogProps.setAppliedFilterClonedData([]);
        oAuditLogProps.setIsFilterDirty(false);
        let aSearchFilterData = oAuditLogProps.getSearchFilterData();
        delete aSearchFilterData.clonedObject;
        this.fetchAuditLogListForGridView();
      }
    }

    handleFilterItemPopoverClosed () {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      oAuditLogProps.setAvailableFilterSearchData({});
      this.triggerChange();
    };

    handleFilterItemSearchTextChanged (sId, sSearchedText) {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      let oAvailableFilterSearchData = oAuditLogProps.getAvailableFilterSearchData();
      oAvailableFilterSearchData[sId] = sSearchedText;
      this.triggerChange();
    }

    resetAuditLogFilterProps () {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      oAuditLogProps.reset();
    }

    _setUpExportStatusGridView () {
      let oSkeleton = new ShowExportStatusGridViewSkeleton();
      let oData = {
        skeleton: oSkeleton,
        sortBy: "assetId",
        sortOrder: "desc"
      };
      GridViewStore.createGridViewPropsByContext(GridViewContexts.AUDIT_LOG_EXPORT_STATUS, oData);
      this._fetchAuditLogExportStatusList();
    }

    _fetchAuditLogExportStatusList () {
      let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.AUDIT_LOG_EXPORT_STATUS);
      delete oPostData.searchText;
      delete oPostData.searchBy;
      oPostData.userName = GlobalStore.getCurrentUser().userName;
      SettingUtils.csPostRequest(oAuditLogRequestMapping.getAuditLogExportStatusListForGridView, {}, oPostData, this.successFetchAuditLogExportStatusList.bind(this), this.failureFetchAuditLogExportStatusList);
    }

    failureFetchAuditLogExportStatusList (oResponse) {
      SettingUtils.failureCallback(oResponse, "failureFetchAuditLogExportStatusListCallback", getTranslation());
    }

    successFetchAuditLogExportStatusList (oResponse) {
      let oResponseData = oResponse.success;
      GridViewStore.preProcessDataForGridView(GridViewContexts.AUDIT_LOG_EXPORT_STATUS,
          oResponseData.auditLogExportList, oResponseData.count);
      this.triggerChange();
    }

    handleExportAuditLog () {
      let oPostData = AuditLogProvider.preparePostData();
      oPostData.size = 0;
      SettingUtils.csPutRequest(oAuditLogRequestMapping.exportAuditLogList, {}, oPostData, this.successFetchAuditLogExportButtonClicked.bind(this),  this.failureFetchAuditLogExportButtonClicked);
    }

    successFetchAuditLogExportButtonClicked (oResponse) {
      alertify.message(getTranslation().EXPORT_GENERATE_INITIATED_AND_CHECK_AUDIT_LOG_EXPORT_STATUS);
    }

    failureFetchAuditLogExportButtonClicked () {
      SettingUtils.failureCallback(oResponse, "failureFetchAuditLogExportButtonClicked", getTranslation()); // eslint-disable-line
    }

    handleShowAuditLogExportStatus () {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      oAuditLogProps.setIsShowExportStatusDialogActive(true);
      this._setUpExportStatusGridView();
    }

    handleAuditLogExportDialogPaginationChanged (oNewPaginationData) {
      let GridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUDIT_LOG_EXPORT_STATUS);
      GridViewProps.setGridViewPaginationData(oNewPaginationData);
      this._fetchAuditLogExportStatusList();

    }

    handleAuditLogExportStatusOkButtonClicked () {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      let oAuditLogExportProps = SettingScreenProps.auditLogExportProps;
      oAuditLogExportProps.setGridViewContext("");
      oAuditLogExportProps.setGridViewData([]);
      oAuditLogExportProps.setGridViewSkeleton([]);
      oAuditLogExportProps.setGridViewTotalItems(0);
      oAuditLogProps.setIsShowExportStatusDialogActive(false);
      this.triggerChange();
    }

    handleAuditLogExportDialogRefreshButtonClicked () {
      this._fetchAuditLogExportStatusList();
    }

    deleteAuditLogExportStatusDialogClicked(aSelectedIds) {
      let oAuditLogProps = SettingScreenProps.auditLogProps;
      if (oAuditLogProps.getIsShowExportStatusDialogActive()) {
        let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUDIT_LOG_EXPORT_STATUS);
        let aGridViewData = oGridViewPropsByContext.getGridViewData();
        let [sSelectedId] = aSelectedIds;
        let sAssetId = CS.find(aGridViewData, { id: sSelectedId }).assetId;
        let oPostData = {
          ids: [sAssetId]
        };
        SettingUtils.csDeleteRequest(oAuditLogRequestMapping.deleteAuditLogExportStatus, {}, oPostData, this.successFetchAuditLogExportStatusDeleteCallback.bind(this, aSelectedIds[0]), this.failureFetchAuditLogExportStatusDeleteCallback);
      }
    }

    successFetchAuditLogExportStatusDeleteCallback (sContentId) {
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUDIT_LOG_EXPORT_STATUS);
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
      CS.remove(aGridViewData, {id: sContentId});
      oGridViewPropsByContext.setGridViewData(aGridViewData);
      oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - 1);
      this.triggerChange();
    }

    failureFetchAuditLogExportStatusDeleteCallback (oResponse) {
      SettingUtils.failureCallback(oResponse, "failureFetchAuditLogExportStatusDeleteCallback", getTranslation());
    }


  }

  return AuditLogStore;

})();

let oAuditLogStore = new AuditLogStore();
MicroEvent.mixin(AuditLogStore);

export default oAuditLogStore;
