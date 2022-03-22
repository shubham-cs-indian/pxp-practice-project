let ColumnOrganizerProps = function () {

  let Props = function () {
    return {
      sColumnOrganizerContext: "",
      aSelectedColumns: [],
      aColumns: [],
      aTotalColumns: [],
      sSearchText: "",
      bIsDialogOpen: false,
      bIsLoadMore: false,
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

    setSelectedOrganizedColumns: function (_aPropertiesSequenceList) {
      oProperties.aSelectedColumns = _aPropertiesSequenceList;
    },

    getTotalColumns: function () {
      return oProperties.aTotalColumns;
    },

    setTotalColumns: function (_aTotalColumns) {
      oProperties.aTotalColumns = _aTotalColumns;
    },

    getHiddenColumns: function () {
      return oProperties.aColumns;
    },

    setHiddenColumns: function (_aPropertiesList) {
      oProperties.aColumns = _aPropertiesList;
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

    getSelectedColumnsLimit: function () {
      return oProperties.sSelectedColumnsLimit;
    },

    setSelectedColumnsLimit: function (_sSelectedColumnsLimit) {
      oProperties.sSelectedColumnsLimit = _sSelectedColumnsLimit;
    },

    getPaginationData: () => {
      return oProperties.oPaginationData;
    },

    getIsLoadMore: function () {
      return oProperties.bIsLoadMore;
    },

    setIsLoadMore: function (_bIsLoadMore) {
      oProperties.bIsLoadMore = _bIsLoadMore;
    },

    setPaginationData: (_oPaginationData) => {
      oProperties.oPaginationData = _oPaginationData;
    },

    getIsListDirty: function () {
      return oProperties.bIsListDirty;
    },

    setIsListDirty: function (_bIsListDirty) {
      oProperties.bIsListDirty = _bIsListDirty;
    },

    getCustomRequestResponseInfo: function () {
      return oProperties.customRequestResponseInfo;
    },

    setCustomRequestResponseInfo: function (_oCustomRequestResponseInfo) {
      oProperties.customRequestResponseInfo = _oCustomRequestResponseInfo;
    },

    reset: function () {
      oProperties = new Props();
    },
  }
};

export default ColumnOrganizerProps;