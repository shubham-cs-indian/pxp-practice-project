let GridViewProps = function () {

  let fProps = function () {
    return {
      skeleton: {},
      data: [],
      paginationData: {
        from: "",
        pageSize: ""
      },
      searchText: "",
      searchBy: "",
      isDirty: false,
      sortData: {
        sortBy: "",
        sortOrder: "",
      },
      totalItems: "",

      //Check usage
      activeCellDetails: {
        dataId: "",
        columnId: "",
      },
      totalNestedItems: 0, //Check usage
      parentId: "",
      visualData: {},
      sResizedColumnId: ''
    }
  };

  let oProperties = new fProps();

  return {
    setGridViewSkeleton: (aSkeleton) => {
      oProperties.skeleton = aSkeleton;
    },

    getGridViewSkeleton: () => {
      return oProperties.skeleton;
    },

    setGridViewData: (aData) => {
      oProperties.data = aData;
    },

    getGridViewData: () => {
      return oProperties.data;
    },

    setGridViewTotalItems: (iTotalItems) => {
      oProperties.totalItems = iTotalItems;
    },

    getGridViewTotalItems: () => {
      return oProperties.totalItems;
    },

    setGridViewActiveCellDetails: (oActiveCellDetails) => {
      oProperties.activeCellDetails = oActiveCellDetails;
    },

    getGridViewActiveCellDetails: () => {
      return oProperties.activeCellDetails;
    },

    setGridViewTotalNestedItems: (iTotalNestedItems) => {
      oProperties.totalNestedItems = iTotalNestedItems;
    },

    getGridViewTotalNestedItems: () => {
      return oProperties.totalNestedItems;
    },

    setGridViewParentId: (sParentId) => {
      oProperties.parentId = sParentId;
    },

    getGridViewParentId: () => {
      return oProperties.parentId;
    },

    setGridViewPaginationData: (_oGridViewPaginationData) => {
      oProperties.paginationData = _oGridViewPaginationData;
    },

    getGridViewPaginationData : () => {
      return oProperties.paginationData;
    },

    setGridViewSortData: (oSortData) => {
      oProperties.sortData = oSortData;
    },

    getGridViewSortData : () => {
      return oProperties.sortData;
    },

    setGridViewSearchText: (sSearchText) => {
      oProperties.searchText = sSearchText;
    },

    getGridViewSearchText : () => {
      return oProperties.searchText;
    },

    setGridViewVisualData: (_oGridViewVisualData) => {
      this.visualData = _oGridViewVisualData;
    },

    getGridViewVisualData : () => {
      return oProperties.visualData;
    },

    getGridViewSearchBy: () => {
      return oProperties.searchBy;
    },

    setGridViewSearchBy: (sSearchBy) => {
      oProperties.searchBy = sSearchBy;
    },

    getIsGridDataDirty: () => {
     return oProperties.isDirty;
    },

    setIsGridDataDirty: (bIsDirty) => {
      oProperties.isDirty = bIsDirty;
    },

    getResizedColumnId: () => {
      return oProperties.sResizedColumnId;
    },

    setResizedColumnId: (sResizableId) => {
      oProperties.sResizedColumnId = sResizableId;
    },

    //Add functions above reset function
    reset: () => {
      oProperties = new fProps();
    }
  };
};

export default GridViewProps;