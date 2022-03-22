let ContentGridProps = (() => {

  let Props = function () {
    return {
      oGridViewSkeleton: {},
      aGridViewData: [],
      oGridViewVisualData: {},
      oGridViewPaginationData: {
        from: 0,
        pageSize: 20
      },
      sSearchText: "",
      sSortBy: "",
      sSortOrder: "",
      iTotalItems: 0,
      bIsGridDataDirty: false,
      bShowCheckboxColumn: false,
      bDisableCreate: true,
      bDisableDelete: true,
      sActiveContentId: "",
      oInstancesData: {},
      oActiveContentForEditing: {},
      aFilteredRowIds: [],
      aEditableProperties: [],
      aSelectedEditableProperties: [],
      isGridEditViewOpen : false,
      sequenceListLimit: "",
      sResizedColumnId: ''
    }
  };

  let Properties = new Props();

  return {

    getGridViewSkeleton: () => {
      return Properties.oGridViewSkeleton;
    },

    setGridViewSkeleton: (_oGridViewSkeleton) => {
      Properties.oGridViewSkeleton = _oGridViewSkeleton;
    },

    getGridViewData: () => {
      return Properties.aGridViewData;
    },

    setGridViewData: (_aGridViewData) => {
      Properties.aGridViewData = _aGridViewData;
    },

    getGridViewVisualData: () => {
      return Properties.oGridViewVisualData;
    },

    setGridViewVisualData: (_oGridViewVisualData) => {
      Properties.oGridViewVisualData = _oGridViewVisualData;
    },

    getGridViewPaginationData: () => {
      return Properties.oGridViewPaginationData;
    },

    setGridViewPaginationData: (_oGridViewPaginationData) => {
      Properties.oGridViewPaginationData = _oGridViewPaginationData;
    },

    getGridViewSearchText: () => {
      return Properties.sSearchText;
    },

    setGridViewSearchText: (_sSearchText) => {
      Properties.sSearchText = _sSearchText;
    },

    getGridViewSortOrder: () => {
      return Properties.sSortOrder;
    },

    setGridViewSortOrder: (_sSortOrder) => {
      Properties.sSortOrder = _sSortOrder;
    },

    getGridViewSortBy: () => {
      return Properties.sSortBy;
    },

    setGridViewSortBy: (_sSortBy) => {
      Properties.sSortBy = _sSortBy;
    },

    getGridViewTotalItems: () => {
      return Properties.iTotalItems;
    },

    setGridViewTotalItems: (_iTotalItems) => {
      Properties.iTotalItems = _iTotalItems;
    },

    getIsGridDataDirty: () => {
      return Properties.bIsGridDataDirty;
    },

    setIsGridDataDirty: (_bIsGridDataDirty) => {
      Properties.bIsGridDataDirty = _bIsGridDataDirty;
    },

    getShowCheckboxColumn: () => {
      return Properties.bShowCheckboxColumn;
    },

    getDisableCreate: () => {
      return Properties.bDisableCreate;
    },

    getDisableDelete: () => {
      return Properties.bDisableDelete;
    },

    getActiveContentId: () => {
      return Properties.activeContentId;
    },

    setActiveContentId: (_sActiveContentId) => {
      Properties.activeContentId = _sActiveContentId;
    },

    getInstancesData: () => {
      return Properties.oInstancesData;
    },

    setInstancesData: (_oInstancesData) => {
      Properties.oInstancesData = _oInstancesData;
    },

    getActiveContentForEditing: () => {
      return Properties.oActiveContentForEditing;
    },

    setActiveContentForEditing: (_oActiveContentForEditing) => {
      Properties.oActiveContentForEditing = _oActiveContentForEditing;
    },

    getFilteredRowIds: () => {
      return Properties.aFilteredRowIds;
    },

    setFilteredRowIds: (_aFilteredRowIds) => {
      Properties.aFilteredRowIds = _aFilteredRowIds;
    },

    getSelectedEditableProperties: () => {
      return Properties.aSelectedEditableProperties;
    },

    setSelectedEditableProperties: (_aSelectedEditableProperties) => {
      Properties.aSelectedEditableProperties = _aSelectedEditableProperties;
    },

    getIsContentGridEditViewOpen: () => {
      return Properties.isGridEditViewOpen;
    },

    setIsContentGridEditViewOpen: (_isGridEditViewOpen) => {
      Properties.isGridEditViewOpen = _isGridEditViewOpen;
    },

    getSequenceListLimit: function () {
      return Properties.sequenceListLimit;
    },

    setSequenceListLimit: function (sSequenceListLimit) {
      Properties.sequenceListLimit = sSequenceListLimit;
    },

    getResizedColumnId: () => {
      return Properties.sResizedColumnId;
    },

    setResizedColumnId: (sResizableId) => {
      Properties.sResizedColumnId = sResizableId;
    },

    reset: () => {
      Properties = new Props();
    },

    toJSON: () => {
      return Properties;
    }

  };

})();

export default ContentGridProps;
