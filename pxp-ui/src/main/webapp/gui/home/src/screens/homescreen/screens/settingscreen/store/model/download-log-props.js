let DownloadLogProps = (function () {

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
      aSearchFilterClonedData: [],
      bIsFilterExpanded: false,
      bIsFilterDirty: false,
      bIsSearchDirty: false,
      oAvailableFilterSearchData: {},
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

    getSearchFilterClonedData: function(){
      return oProperties.aSearchFilterClonedData;
    },

    setSearchFilterClonedData: function(_aSearchFilterClonedData){
      oProperties.aSearchFilterClonedData = _aSearchFilterClonedData;
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

    getIsSearchDirty: function(){
        return oProperties.bIsSearchDirty;
    },

    setIsSearchDirty: function(_bIsSearchDirty){
        oProperties.bIsSearchDirty = _bIsSearchDirty;
    },

    getAvailableFilterSearchData: function(){
      return oProperties.oAvailableFilterSearchData;
    },

    setAvailableFilterSearchData: function(_oAvailableFilterSearchData){
      oProperties.oAvailableFilterSearchData = _oAvailableFilterSearchData;
    },

    reset: function () {
      oProperties = new Props();
    },
  }
})();

export default DownloadLogProps;