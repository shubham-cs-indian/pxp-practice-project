let GridEditViewProps = (() => {

  let Props = function () {
    return {
      oPaginationData: {
        from: 0,
        pageSize: 20
      },
      sSearchText: "",
      sSortBy: "label",
      sSortOrder: "asc",
      iTotalItems: 0,
      bIsDirty: false,
      aProperties: [],
      bIsLoadMore: false,
      aPropertiesSequenceList: [],
      bIsDialogOpen: false,
      bIsSequenceListModified: false,
    }
  };

  let Properties = new Props();

  return {

    getIsLoadMore: function () {
      return Properties.bIsLoadMore;
    },

    setIsLoadMore: function (_bIsLoadMore) {
      Properties.bIsLoadMore = _bIsLoadMore;
    },

    getPaginationData: () => {
      return Properties.oPaginationData;
    },

    setPaginationData: (_oPaginationData) => {
      Properties.oPaginationData = _oPaginationData;
    },

    getSearchText: () => {
      return Properties.sSearchText;
    },

    setSearchText: (_sSearchText) => {
      Properties.sSearchText = _sSearchText;
    },

    getSortOrder: () => {
      return Properties.sSortOrder;
    },

    setSortOrder: (_sSortOrder) => {
      Properties.sSortOrder = _sSortOrder;
    },

    getSortBy: () => {
      return Properties.sSortBy;
    },

    setSortBy: (_sSortBy) => {
      Properties.sSortBy = _sSortBy;
    },

    getTotalItems: () => {
      return Properties.iTotalItems;
    },

    setTotalItems: (_iTotalItems) => {
      Properties.iTotalItems = _iTotalItems;
    },

    getIsDirty: () => {
      return Properties.bIsDirty;
    },

    setIsDirty: (_bIsDirty) => {
      Properties.bIsDirty = _bIsDirty;
    },

    getProperties: () => {
      return Properties.aProperties;
    },

    setProperties: (_aProperties) => {
      Properties.aProperties = _aProperties;
    },

    getPropertiesSequenceList: () => {
      return Properties.aPropertiesSequenceList;
    },

    setPropertiesSequenceList: (_aPropertiesSequenceList) => {
      Properties.aPropertiesSequenceList = _aPropertiesSequenceList;
    },

    getIsDialogOpen: () => {
      return Properties.bIsDialogOpen;
    },

    setIsDialogOpen: (_bIsDialogOpen) => {
      Properties.bIsDialogOpen = _bIsDialogOpen;
    },

    getIsSequenceListModified: () => {
      return Properties.bIsSequenceListModified;
    },

    setIsSequenceListModified: (_bIsSequenceListModified) => {
      Properties.bIsSequenceListModified = _bIsSequenceListModified;
    },

    reset: () => {
      Properties = new Props();
    },

    toJSON: () => {
      return Properties;
    }

  };

})();

export default GridEditViewProps;
