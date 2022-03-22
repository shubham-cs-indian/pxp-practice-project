let IconLibraryProps = (function () {

  let Props = function () {
    return {
      icons: [],
      selectedIconIds: [],
      totalCount: 0,
      searchText: "",
      from: 0,
      searchConstantData: {
        searchColumn: "label",
        sortOrder: "asc",
        sortBy: "lastmodified",
        size: 50
      },
      paginationData: {
        size: 50,
        from: 0,
      },
      selectDialogPaginationData: {
        size: 20,
        from: 0,
      },
      bIconLibraryUploadClicked: false,
      aAllActiveIcons: [],
      aAllowedExtensions:[],
      bisDragging: false,
      bIsUsageDeleteDialog: false,
      sActiveIconId: "",
      aDuplicateIconNames: [],
      bIconElementEditClicked: false,
      oActiveIconElement: new Map(),
    }
  };

  let oProperties = new Props();

  return {
    getActiveIconElement: function () {
      return oProperties.oActiveIconElement;
    },

    setActiveIconElement: function (_oActiveIconElement) {
      oProperties.oActiveIconElement = _oActiveIconElement;
    },

    getIconElementEditClicked: function () {
      return oProperties.bIconElementEditClicked;
    },

    setIconElementEditClicked: function (_bIconElementEditClicked) {
      oProperties.bIconElementEditClicked = _bIconElementEditClicked;
    },

    getAllActiveIcons: function () {
      return oProperties.aAllActiveIcons;
    },

    setAllActiveIcons: function (_aAllActiveIcons) {
      oProperties.aAllActiveIcons = _aAllActiveIcons;
    },

    getIcons: function () {
      return oProperties.icons;
    },

    setIcons: function (_aIcons) {
      oProperties.icons = _aIcons;
    },

    reset: function () {
      oProperties = new Props();
    },

    getSelectedIconIds: function () {
      return oProperties.selectedIconIds;
    },

    setSelectedIconIds: function (_aSelectedIconIds) {
      oProperties.selectedIconIds = _aSelectedIconIds;
    },

    getSearchText: function () {
      return oProperties.searchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.searchText = _sSearchText;
    },

    getFrom: function () {
      return oProperties.from
    },

    setFrom: function (_iFrom) {
      oProperties.from = _iFrom
    },

    getSearchConstantData: function () {
      return oProperties.searchConstantData;
    },

    setSearchConstantData: function (_oSearchConstantData) {
      oProperties.searchConstantData = _oSearchConstantData;
    },

    getTotalCount: function () {
      return oProperties.totalCount;
    },

    setTotalCount: function (_totalCount) {
      oProperties.totalCount = _totalCount;
    },

    getPaginationData: function () {
      return oProperties.paginationData;
    },

    setPaginationData: function (_paginationData) {
      oProperties.paginationData = _paginationData;
    },

    getSelectDialogPaginationData: function () {
      return oProperties.selectDialogPaginationData;
    },

    setSelectDialogPaginationData: function (_selectDialogPaginationData) {
      oProperties.selectDialogPaginationData = _selectDialogPaginationData;
    },

    getAllowedExtensions: function () {
      return oProperties.aAllowedExtensions;
    },

    setAllowedExtensions: function (_aAllowedExtensions) {
      oProperties.aAllowedExtensions = _aAllowedExtensions;
    },

    setIconLibraryUploadClicked: function (_bIconLibraryUploadClicked) {
      oProperties.bIconLibraryUploadClicked = _bIconLibraryUploadClicked;
    },

    getIconLibraryUploadClicked: function () {
      return oProperties.bIconLibraryUploadClicked;
    },

    getIsDragging: function () {
      return oProperties.bisDragging;
    },

    setIsDragging: function (_bisDragging) {
      oProperties.bisDragging = _bisDragging;
    },

    getIsUsageDeleteDialog: function () {
      return oProperties.bIsUsageDeleteDialog;
    },

    setIsUsageDeleteDialog: function (_bIsUsageDeleteDialog) {
      oProperties.bIsUsageDeleteDialog = _bIsUsageDeleteDialog;
    },

    getActiveIconId: function () {
      return oProperties.sActiveIconId;
    },

    setActiveIconId: function (_sActiveIconId) {
      oProperties.sActiveIconId = _sActiveIconId;
    },
  }
})();

export default IconLibraryProps;