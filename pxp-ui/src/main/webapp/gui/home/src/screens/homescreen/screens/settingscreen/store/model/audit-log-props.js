let AuditLogProps = (function () {

  let Props = function () {
    return {
      aAppliedFilterData : [
        {
          id:"date",
          children:["last7Days"],
          isDefault: true,
          value:{},
        }
      ],
      aAppliedFilterClonedData : [],
      aSearchFilterData : [],
      bIsFilterExpanded: false,
      bIsFilterDirty: false,
      oAvailableFilterSearchData: {},
      bShowExportStatusDialog: false
    }
  };

  let oProperties = new Props();

  return {

    getAppliedFilterData: function(){
      return oProperties.aAppliedFilterData;
    },

    setAppliedFilterData: function(_aAppliedFilterData){
      oProperties.aAppliedFilterData = _aAppliedFilterData;
    },

    getAppliedFilterClonedData: function(){
      return oProperties.aAppliedFilterClonedData;
    },

    setAppliedFilterClonedData: function(_aAppliedFilterClonedData){
      oProperties.aAppliedFilterClonedData = _aAppliedFilterClonedData;
    },

    getSearchFilterData: function(){
      return oProperties.aSearchFilterData;
    },

    setSearchFilterData: function(_aSearchFilterData){
      oProperties.aSearchFilterData = _aSearchFilterData;
    },

    getIsFilterExpanded: function(){
      return oProperties.bIsFilterExpanded;
    },

    setIsFilterExpanded: function(_bIsFilterExpanded){
      oProperties.bIsFilterExpanded = _bIsFilterExpanded;
    },

    getIsFilterDirty: function(){
      return oProperties.bIsFilterDirty;
    },

    setIsFilterDirty: function(_bIsFilterDirty){
      oProperties.bIsFilterDirty = _bIsFilterDirty;
    },

    getAvailableFilterSearchData: function(){
      return oProperties.oAvailableFilterSearchData;
    },

    setAvailableFilterSearchData: function(_oAvailableFilterSearchData){
      oProperties.oAvailableFilterSearchData = _oAvailableFilterSearchData;
    },

    setIsShowExportStatusDialogActive: function(_bShowExportStatusDialog) {
      oProperties.bShowExportStatusDialog = _bShowExportStatusDialog ;
    },
    
    getIsShowExportStatusDialogActive: function() {
      return oProperties.bShowExportStatusDialog;
    },
    
    reset: function () {
      oProperties = new Props();
    },
  }
})();

export default AuditLogProps;