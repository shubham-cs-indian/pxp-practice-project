import CS from '../../../../../../libraries/cs';
import SortFieldDictionary from '../../tack/mock/mock-data-for-sort-field-dictionary';
import ZoomSettings from '../../../../../../commonmodule/tack/zoom-toolbar-settings';

var RelationshipViewProps = (function () {

  var Props = function () {
    return {
      aRelationshipAvailableContents: [],
      sSelectedRelationshipId: '',
      oSelectedContentTileIds: {},
      oContentRelationshipContentDragStatus: {},
      aContentRelationshipOfMasterClasses: [],
      aRelationshipContentSearchList: [],
      sSelectedRelationshipUUID: '',
      bIsFromSearchList: false,
      oContentRelationshipListItemDragStatus: {},
      oKlassesForRelationship : {},
      oSelectedRelationshipElements : {},
      oSelectedNatureRelationshipElements : {},
      oRelationshipToolbarProps: {},
      oModifiedRelationshipElements:{},
      referenceRelationshipInstanceElements: {},
      oReferenceRelationshipInstanceTabElements: {},
      referenceNatureRelationshipInstanceElements: {},
      referenceCommonRelationshipInstanceElements: {},
      referenceElementInstances: {},
      aAssetClassList: [],
      bIsBulkUploadDialogOpen: false,
      aBulkUploadFiles: [],
      sContext: '',
      bisDragging: false
    }
  };

  var oProperties = new Props();

  var oDefaultRelationshipToolbarProps = {
    elements: [],
    currentZoom: ZoomSettings.defaultZoom,
    paginationLimit: 20,
    sortField: SortFieldDictionary.CREATED_ON,
    sortOrder: SortFieldDictionary.DESC,
    startIndex: 0,
    totalElementsCount: 0,
    activeXRayPropertyGroup: {
    label: "",
    properties: [],
    createNew: true,
    unappliedChanges: true
    }
  };

  return {

    setContentRelationshipContentDragDetails: function (_oDragStatus) {
      oProperties.oContentRelationshipContentDragStatus = _oDragStatus;
    },

    setRelationshipListItemDragDetails: function (_oDragStatus) {
      oProperties.oContentRelationshipListItemDragStatus = _oDragStatus;
    },

    getRelationshipToolbarProps: function () {
      return oProperties.oRelationshipToolbarProps;
    },

    setRelationshipToolbarProps: function (_oNewRelationshipProps) {
      oProperties.oRelationshipToolbarProps = _oNewRelationshipProps;
    },

    addNewRelationshipToolbarPropById: function (sId) {
      oProperties.oRelationshipToolbarProps[sId] = CS.cloneDeep(oDefaultRelationshipToolbarProps);
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {}
    },

    getSelectedRelationshipElements : function () {
      return oProperties.oSelectedRelationshipElements;
    },

    emptySelectedRelationshipElements : function () {
      oProperties.oSelectedRelationshipElements = {};
    },

    getModifiedRelationshipElements : function () {
      return oProperties.oModifiedRelationshipElements;
    },

    emptyModifiedRelationshipElements : function () {
      oProperties.oModifiedRelationshipElements = {};
    },

    getReferenceRelationshipInstanceElements: function () {
      return oProperties.referenceRelationshipInstanceElements;
    },

    setReferenceRelationshipInstanceElements: function (_oReferenceRelationshipInstanceElements) {
      oProperties.referenceRelationshipInstanceElements = _oReferenceRelationshipInstanceElements;
    },

    getReferenceNatureRelationshipInstanceElements: function () {
      return oProperties.referenceNatureRelationshipInstanceElements;
    },

    setReferenceNatureRelationshipInstanceElements: function (_oReferenceNatureRelationshipInstanceElements) {
      oProperties.referenceNatureRelationshipInstanceElements = _oReferenceNatureRelationshipInstanceElements;
    },

    getReferencedCommonRelationshipInstanceElements: function () {
      return oProperties.referenceCommonRelationshipInstanceElements;
    },

    setReferencedCommonRelationshipInstanceElements: function (_oReferenceNatureRelationshipInstanceElements) {
      oProperties.referenceCommonRelationshipInstanceElements = _oReferenceNatureRelationshipInstanceElements;
    },

    getReferenceElementInstances: function () {
      return oProperties.referenceElementInstances;
    },

    setReferenceElementInstances: function (_oReferenceElementInstances) {
      oProperties.referenceElementInstances = _oReferenceElementInstances;
    },

    emptySelectedNatureRelationshipElements: function () {
      oProperties.oSelectedNatureRelationshipElements = {};
    },

    setAssetClassList: function (_aAssetClassList) {
      oProperties.aAssetClassList = _aAssetClassList;
    },

    getAssetClassList: function () {
      return oProperties.aAssetClassList;
    },

    setIsBulkUploadDialogOpen: function (_bIsBulkUploadDialogOpen) {
      oProperties.bIsBulkUploadDialogOpen = _bIsBulkUploadDialogOpen;
    },

    getIsBulkUploadDialogOpen: function () {
      return oProperties.bIsBulkUploadDialogOpen;
    },

    getBulkUploadFiles: function() {
      return oProperties.aBulkUploadFiles;
    },

    setBulkUploadFiles: function (_aBulkUploadFiles) {
      oProperties.aBulkUploadFiles = _aBulkUploadFiles;
    },

    getContext: function () {
      return oProperties.sContext;
    },

    setContext: function (_sContext) {
      oProperties.sContext = _sContext;
    },

    getIsDragging: function () {
      return oProperties.bisDragging;
    },

    setIsDragging: function (_bisDragging) {
      oProperties.bisDragging = _bisDragging;
    },

  };

})();

export default RelationshipViewProps;
