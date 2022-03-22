/**
 * Created by CS80 on 4-4-2017.
 */
import TabHeaderData from '../../tack/mock/mock-data-for-map-summery-header';

var MappingConfigViewProps = (function () {

  var Props = function () {
    return {
      oSelectedMapping: {},
      aMappingGridData: [],
      aMappingList: [],
      bIsMappingDialogActive:false,
      aSelectedMappingRows: [],
      aHeaderData: [
        {
          id: TabHeaderData.relationship,
          label: 'Relationship'
        },
        {
          id: TabHeaderData.taxonomy,
          label: 'Taxonomy'
        },
        {
          id: TabHeaderData.class,
          label: 'Class'
        },
        {
          id: TabHeaderData.propertyCollection,
          label: 'Properties'
        },
        {
          id: TabHeaderData.contextTag,
          label: 'Context Tag'
        },
      ],
      sSelectedTabId: TabHeaderData.class,
      sSearchText: "",
      oSearchConstantData: {
        searchColumn: "label",
        sortOrder: "asc",
        sortBy: "label",
        size: 20
      },
      iFrom: 0,
      configData: {
        referencedTags: {},
        referencedAttributes: {},
        referencedKlasses: {},
        referencedTaxonomies: {},
        referencedPropertyCollections: {},
        referencedRelationships: {},
        referencedContexts: {}
      },
      isSaveAsDialogActive: false,
      dataForSaveAsDialog: [],
      codeDuplicates:[],
      nameDuplicates:[],
      codeUniqueness:false,
      aSelectedPropertyCollectionForMapping:[],
      sSearchViewText:"",
      aAttributeMappings: [],
      aTagMappings: [],
      bIsPropertyCollectionToggleFlag: true,
    }
  };

  var oProperties = new Props();

  return {

    getSelectedMapping: function () {
      return oProperties.oSelectedMapping;
    },

    setSelectedMapping: function (_oProfile) {
      oProperties.oSelectedMapping = _oProfile;
    },

    getConfigData: function(){
      return oProperties.configData;
    },

    getSelectedMappingRows: function () {
      return oProperties.aSelectedMappingRows;
    },

    setSelectedMappingRows: function (_aSelectedMappingRows) {
      oProperties.aSelectedMappingRows = _aSelectedMappingRows;
    },

    getSelectedId: function () {
      return oProperties.sSelectedTabId;
    },

    setSelectedId: function (_sSelectedId) {
      oProperties.sSelectedTabId = _sSelectedId;
    },

    getTabHeaderData: function () {
      return oProperties.aHeaderData;
    },

    setTabHeaderData: function (_aHeaderData) {
      oProperties.aHeaderData = _aHeaderData;
    },

    getSearchText: function () {
      return oProperties.sSearchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getSearchConstantData: function () {
      return oProperties.oSearchConstantData;
    },

    setSearchConstantData: function (_oSearchConstantData) {
      oProperties.oSearchConstantData = _oSearchConstantData;
    },

    getFrom: function () {
      return oProperties.iFrom
    },

    setFrom: function (_iFrom) {
      oProperties.iFrom = _iFrom
    },

    getIsMappingDialogActive: function () {
      return oProperties.bIsMappingDialogActive;
    },

    setIsMappingDialogActive: function (_bIsMappingDialogActive) {
      oProperties.bIsMappingDialogActive = _bIsMappingDialogActive;
    },

    getMappingGridData: function () {
      return oProperties.aMappingGridData;
    },

    setMappingGridData: function (_aMappingGridData) {
      oProperties.aMappingGridData = _aMappingGridData;
    },

    getMappingList: function () {
      return oProperties.aMappingList;
    },

    setMappingList: function (_aMappingList) {
      oProperties.aMappingList = _aMappingList;
    },

    getIsSaveAsDialogActive: function () {
      return oProperties.isSaveAsDialogActive;
    },

    setIsSaveAsDialogActive: function (_bIsSaveAsDialogActive) {
      oProperties.isSaveAsDialogActive = _bIsSaveAsDialogActive;
    },

    getDataForSaveAsDialog: function () {
      return oProperties.dataForSaveAsDialog;
    },

    setDataForSaveAsDialog: function (_dataForSaveAsDialog) {
      oProperties.dataForSaveAsDialog = _dataForSaveAsDialog;
    },

    getCodeDuplicates: function () {
      return oProperties.codeDuplicates;
    },

    setCodeDuplicates: function (_aCodeDuplicates) {
      oProperties.codeDuplicates = _aCodeDuplicates;
    },
    getNameDuplicates: function () {
      return oProperties.nameDuplicates;
    },

    setNameDuplicates: function (_aNameDuplicates) {
      oProperties.nameDuplicates = _aNameDuplicates;
    },

    getSearchViewText: function () {
      return oProperties.sSearchViewText;
    },

    setSearchViewText: function (_sSearchViewText) {
      oProperties.sSearchViewText = _sSearchViewText;
    },

    getAttributeMappings: function () {
      return oProperties.aAttributeMappings;
    },

    setAttributeMappings: function (_aAttributeMappings) {
      oProperties.aAttributeMappings = _aAttributeMappings;
    },

    getTagMappings: function () {
      return oProperties.aTagMappings;
    },

    setTagMappings: function (_aTagMappings) {
      oProperties.aTagMappings = _aTagMappings;
    },

    getPropertyCollectionToggleFlag: function () {
      return oProperties.bIsPropertyCollectionToggleFlag;
    },

    setPropertyCollectionToggleFlag: function (_bIsPropertyCollectionToggleFlag) {
      oProperties.bIsPropertyCollectionToggleFlag = _bIsPropertyCollectionToggleFlag;
    },

    reset: function () {
      oProperties = new Props();
    }

  };
})();

export default MappingConfigViewProps;