import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import GridViewContexts from '../../../../../../commonmodule/tack/grid-view-contexts';
import AuditLogStore from './auditlogstore/audit-log-store';
import ProcessStore from "./process-store";
import DownloadTrackerConfigStore from "./download-tracker-config-store";

let GridFilterStore = (function () {

    let _handleFilterChildToggled = function (sParentId, sChildId, sContext, oExtraData, oFilterContext) {
      switch (oFilterContext.screenContext) {
        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleAuditLogFilterChildToggled(sParentId, sChildId, sContext, oExtraData);
          break;
        case GridViewContexts.PROCESS:
          ProcessStore.handleFilterChildToggled(sParentId, sChildId, sContext, oExtraData);
          break;
        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleDownloadLogFilterChildToggled(sParentId, sChildId, oExtraData.label);
          break;
      }
    };

    let _handleDateRangePickerUpdateData = function (sContext, oRange, sId) {
      switch (sContext) {
        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleDateRangePickerUpdateData(oRange, sId);
          break;
        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleDateRangePickerUpdateData(oRange, sId);
          break;
      }
    };

    let _handleCollapseFilterClicked = function (bExpanded, sContext) {
      switch (sContext) {
        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleCollapseFilterClicked(bExpanded);
          break;
        case GridViewContexts.PROCESS:
          ProcessStore.handleCollapseFilterClicked(bExpanded);
          break;
        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleCollapseFilterClicked(bExpanded);
          break;
      }
    };

    let _handleApplyFilterButtonClicked = function (sContext) {
      switch (sContext) {
        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleApplyFilterButtonClicked();
          break;
        case GridViewContexts.PROCESS:
          ProcessStore.handleApplyFilterButtonClicked();
          break;
        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleApplyFilterButtonClicked();
          break;
      }
    };

    let _handleFilterItemPopoverClosed = function (sContext) {
      switch (sContext) {
        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleFilterItemPopoverClosed();
          break;
        case GridViewContexts.PROCESS:
          ProcessStore.handleFilterItemPopoverClosed();
          break;
        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleFilterItemPopoverClosed();
          break;
      }
    };

    let _handleFilterItemSearchTextChanged = function (sContext, sId, sSearchedText) {
      switch (sContext) {
        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleFilterItemSearchTextChanged(sId, sSearchedText);
          break;
        case GridViewContexts.PROCESS:
          ProcessStore.handleFilterItemSearchTextChanged(sId, sSearchedText);
          break;
        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleFilterItemSearchTextChanged(sId, sSearchedText);
          break;
      }
    };

    let _handleFilterSummarySearchTextChanged = function (oFilterContext, sFilterId, sSearchedText) {
        switch (oFilterContext.screenContext) {
          case GridViewContexts.AUDIT_LOG:
            AuditLogStore.handleFilterSummarySearchTextChanged(sFilterId, sSearchedText);
            break;
          case GridViewContexts.PROCESS:
            ProcessStore.handleFilterSummarySearchTextChanged(sFilterId, sSearchedText);
            break;
          case GridViewContexts.DOWNLOAD_TRACKER:
            DownloadTrackerConfigStore.handleFilterSummarySearchTextChanged(sFilterId, sSearchedText);
            break;
        }
      };

    return {
      handleFilterChildToggled: function (sParentId, sChildId, sContext, oExtraData, oFilterContext) {
        _handleFilterChildToggled(sParentId, sChildId, sContext, oExtraData, oFilterContext);
      },

      handleDateRangePickerUpdateData: function (sContext, oRange, sId) {
        _handleDateRangePickerUpdateData(sContext, oRange, sId);
      },

      handleCollapseFilterClicked: function (bExpanded, sContext) {
        _handleCollapseFilterClicked(bExpanded, sContext);
      },

      handleApplyFilterButtonClicked: function (sContext) {
        _handleApplyFilterButtonClicked(sContext);
      },

      handleFilterItemPopoverClosed: function (sContext) {
        _handleFilterItemPopoverClosed(sContext);
      },

      handleFilterItemSearchTextChanged: function (sContext, sId, sSearchedText) {
        _handleFilterItemSearchTextChanged(sContext, sId, sSearchedText);
      },

      handleFilterSummarySearchTextChanged: function (oFilterContext, sFilterId, sSearchedText) {
        _handleFilterSummarySearchTextChanged(oFilterContext, sFilterId, sSearchedText);
      }
    }
  }
)();


MicroEvent.mixin(GridFilterStore);
export default GridFilterStore;
