let AuditLogExportProps = (function () {

  let Props = function () {
    return {
      sGridViewContext: "",
      oGridViewSkeleton: {},
      aGridViewData: [],
      oGridViewVisualData: {},
      oGridViewPaginationData: {
        from: 0,
        pageSize: 20
      },
      iGridViewTotalItems: 0
    }
  };

  let oProperties = new Props();

  return {
    getGridViewContext: function () {
      return oProperties.sGridViewContext;
    },

    setGridViewContext: function (_sGridViewContext) {
      oProperties.sGridViewContext = _sGridViewContext;
    },

    getGridViewSkeleton: function () {
      return oProperties.oGridViewSkeleton;
    },

    setGridViewSkeleton: function (_oGridViewSkeleton) {
      oProperties.oGridViewSkeleton = _oGridViewSkeleton;
    },

    getGridViewData: function () {
      return oProperties.aGridViewData;
    },

    setGridViewData: function (_aGridViewData) {
      oProperties.aGridViewData = _aGridViewData;
    },

    getGridViewVisualData: function () {
      return oProperties.oGridViewVisualData;
    },

    setGridViewVisualData: function (_oGridViewVisualData) {
      oProperties.oGridViewVisualData = _oGridViewVisualData;
    },

    setGridViewPaginationData: function (_oGridViewPaginationData) {
      this.oGridViewPaginationData = _oGridViewPaginationData;
    },

    getGridViewPaginationData: function () {
      return this.oGridViewPaginationData;
    },

    setGridViewTotalItems: function (_iGridViewTotalItems) {
      this.iGridViewTotalItems = _iGridViewTotalItems;
    },

    getGridViewTotalItems: function () {
      return this.iGridViewTotalItems;
    }
  }
})();

export default AuditLogExportProps;