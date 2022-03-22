let ColumnOrganizerProps = (function () {

  let Props = function () {
    return {
      sColumnOrganizerContext: "",
      aSelectedColumns: [],
      aColumns: [],
      aTotalColumns: [],
      sSearchText: "",
      bIsLoadMore: false,
      bIsDialogOpen: false,
      bIsListDirty: false,
      sSelectedColumnsLimit: "",
      customRequestResponseInfo: {},
      oPaginationData: {
        from: 0,
        pageSize: 20
      },
    }
  };

  let oProperties = new Props();

  return {

    getColumnOrganizerContext: function () {
      return oProperties.sColumnOrganizerContext;
    },

    setColumnOrganizerContext: function (_sColumnOrganizerContext) {
      oProperties.sColumnOrganizerContext = _sColumnOrganizerContext;
    },

    getSelectedOrganizedColumns: function () {
      return oProperties.aSelectedColumns;
    },

    setSelectedOrganizedColumns: function (_aSelectedColumns) {
      oProperties.aSelectedColumns = _aSelectedColumns;
    },

    getHiddenColumns: function () {
      return oProperties.aColumns;
    },

    setHiddenColumns: function (_aColumns) {
      oProperties.aColumns = _aColumns;
    },

    getIsLoadMore: function () {
      return oProperties.bIsLoadMore;
    },

    setIsLoadMore: function (_bIsLoadMore) {
      oProperties.bIsLoadMore = _bIsLoadMore;
    },

    getTotalColumns: function () {
      return oProperties.aTotalColumns;
    },

    setTotalColumns: function (_aTotalColumns) {
      oProperties.aTotalColumns = _aTotalColumns;
    },

    getSearchText: function () {
      return oProperties.sSearchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getIsDialogOpen: function () {
      return oProperties.bIsDialogOpen;
    },

    setIsDialogOpen: function (_bIsDialogOpen) {
      oProperties.bIsDialogOpen = _bIsDialogOpen;
    },

    getIsListDirty: function () {
      return oProperties.bIsListDirty;
    },

    setIsListDirty: function (_bIsListDirty) {
      oProperties.bIsListDirty = _bIsListDirty;
    },

    getSelectedColumnsLimit: function () {
      return oProperties.sSelectedColumnsLimit;
    },

    setSelectedColumnsLimit: function (_sSelectedColumnsLimit) {
      oProperties.sSelectedColumnsLimit = _sSelectedColumnsLimit;
    },

    getCustomRequestResponseInfo: function () {
      return oProperties.customRequestResponseInfo;
    },

    setCustomRequestResponseInfo: function (_oCustomRequestResponseInfo) {
      oProperties.customRequestResponseInfo = _oCustomRequestResponseInfo;
    },

    getPaginationData: () => {
      return oProperties.oPaginationData;
    },

    setPaginationData: (_oPaginationData) => {
      oProperties.oPaginationData = _oPaginationData;
    },

    reset: function () {
      oProperties = new Props();
    },
  }
})();

export default ColumnOrganizerProps;