var GridEditViewProps = (function () {

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
      propertyList: [],
      propertySequenceList: [],
      gridEditScreenLockStatus: false,
      sequenceListLimit: ""
    }
  };

  let oProperties = new Props();

  return {

    setPropertyList: function (_aPropertyListObject) {
      oProperties.propertyList = _aPropertyListObject;
    },

    getPropertyList: function () {
      return oProperties.propertyList;
    },

    setPropertySequenceList: function (_aPropertySequenceList) {
      oProperties.propertySequenceList = _aPropertySequenceList;
    },

    getPropertySequenceList: function () {
      return oProperties.propertySequenceList;
    },

    setGridEditData: function(_oGridEditData) {
      GridEditViewProps.setPropertyList(_oGridEditData.propertyList);
      GridEditViewProps.setPropertySequenceList(_oGridEditData.propertySequenceList);
    },

    getGridEditData: function() {
      return {
        propertyList: GridEditViewProps.getPropertyList(),
        propertySequenceList: GridEditViewProps.getPropertySequenceList(),
      }
    },

    getPaginationData: function() {
      return oProperties.oPaginationData;
    },

    setPaginationData: function(_oPaginationData) {
      oProperties.oPaginationData = _oPaginationData;
    },

    getSearchText: function (){
      return oProperties.sSearchText;
    },

    setSearchText : function(_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getSortBy : function(){
      return oProperties.sSortBy;
    },

    setSortBy: function(_sSortBy){
      oProperties.sSortBy = _sSortBy;
    },

    getSortOrder : function(){
      return oProperties.sSortOrder;
    },

    setSortOrder: function(_sSortOrder){
      oProperties.sSortOrder = _sSortOrder;
    },

    getTotalItems : function(){
      return oProperties.iTotalItems;
    },

    setTotalItems: function(_iTotalItems){
      oProperties.iTotalItems = _iTotalItems;
    },

    getIsDirty : function(){
      return oProperties.bIsDirty;
    },

    setIsDirty: function(_bIsDirty){
      oProperties.bIsDirty = _bIsDirty;
    },

    getGridEditScreenLockStatus: function () {
      return oProperties.gridEditScreenLockStatus;
    },

    setGridEditScreenLockStatus: function (bGridEditScreenLockStatus) {
      oProperties.gridEditScreenLockStatus = bGridEditScreenLockStatus;
    },

    getSequenceListLimit: function () {
      return oProperties.sequenceListLimit;
    },

    setSequenceListLimit: function (sSequenceListLimit) {
      oProperties.sequenceListLimit = sSequenceListLimit;
    },


    reset: function () {
      oProperties = new Props();
    }
  }
})();

export default GridEditViewProps;