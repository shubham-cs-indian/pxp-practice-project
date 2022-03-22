
var TableViewContextProps = function () {

  var aHeaderData = [];
  var aData = [];
  var oSkeleton = {};
  var oSettings = {};
  var aSelectedHeaders = [];
  var aColumnOrganiserHeaderData = [];
  var aOriginalRowData = []; //Note - this data is for reading purposes only
  let aOriginalHeaderData = []; //Note - this data is for reading purposes only
  var oTableOrganiserConfig = {
    showCreateButton: false,
    showDateRangeSelector: false
  };
  var bIsSectionLoading = false;
  let sViewContext = "";
  let sViewMode = "";
  let oVisualProps = {};
  let aOriginalCommonRows = [];
  let sVariantInstanceId = "";
  let sResizedColumnId = '';

  return {

    getHeaderData: function () {
      return aHeaderData;
    },

    setHeaderData: function (_aHeaderData) {
      aHeaderData = _aHeaderData;
    },

    getSelectedHeaders: function () {
      return aSelectedHeaders;
    },

    setSelectedHeaders: function (_aSelectedHeaders) {
      aSelectedHeaders = _aSelectedHeaders;
    },

    getColumnOrganiserHeaderData: function () {
      return aColumnOrganiserHeaderData;
    },

    setColumnOrganiserHeaderData: function (_aColumnOrganiserHeaderData) {
      aColumnOrganiserHeaderData = _aColumnOrganiserHeaderData;
    },

    getBodyData: function () {
      return aData;
    },

    setBodyData: function (_aData) {
      aData = _aData;
    },

    setGridSkeleton: function (_oSkeleton) {
      oSkeleton = _oSkeleton;
    },

    getGridSkeleton: function () {
      return oSkeleton;
    },

    //Note - this data is for reading purposes only
    getOriginalRowData: function () {
      return aOriginalRowData;
    },

    //Note - this data is for reading purposes only
    setOriginalRowData: function (_aOriginalRowData) {
      aOriginalRowData = _aOriginalRowData;
    },

    //Note - this data is for reading purposes only
    getOriginalHeaderData: function () {
      return aOriginalHeaderData;
    },

    //Note - this data is for reading purposes only
    setOriginalHeaderData: function (_aOriginalHeaderData) {
      aOriginalHeaderData = _aOriginalHeaderData;
    },

    //Note - this data is for reading purposes only
    getOriginalCommonRows: function () {
      return aOriginalCommonRows;
    },

    //Note - this data is for reading purposes only
    setOriginalCommonRows: function (_aOriginalHeaderData) {
      aOriginalCommonRows = _aOriginalHeaderData;
    },

    getSettings: function () {
      return oSettings;
    },

    setSettings: function (_oSettings) {
      oSettings = _oSettings;
    },

    getTableOrganiserConfig: function () {
      return oTableOrganiserConfig;
    },

    getIsSectionLoading: function (){
      return bIsSectionLoading;
    },

    setIsSectionLoading: function (_bIsSectionLoading) {
      bIsSectionLoading = _bIsSectionLoading;
    },

    getViewContext: function (){
      return sViewContext;
    },

    setViewContext: function (_sViewContext) {
      sViewContext = _sViewContext;
    },

    getViewMode: function (){
      return sViewMode;
    },

    setViewMode: function (_sViewMode) {
      sViewMode = _sViewMode;
    },

    getVisualProps: function (){
      return oVisualProps;
    },

    setVisualProps: function (_oVisualProps) {
      oVisualProps = _oVisualProps;
    },

    getVariantInstanceId: function () {
      return sVariantInstanceId;
    },

    setVariantInstanceId: function (_sVariantInstanceId) {
      sVariantInstanceId = _sVariantInstanceId;
    },

    getResizedColumnId: () => {
      return sResizedColumnId;
    },

    setResizedColumnId: (sResizableId) => {
      sResizedColumnId = sResizableId;
    },
  }
};

export default TableViewContextProps;
