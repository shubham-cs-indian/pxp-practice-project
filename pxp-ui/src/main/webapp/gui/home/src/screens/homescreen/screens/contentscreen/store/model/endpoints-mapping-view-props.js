import CS from '../../../../../../libraries/cs';
import FilterDictionary from '../../tack/mapping-filter-items-dictionary';

var ScreenProps = (function () {

  var Props = function () {

    return {
      aSelectedMappingRows: [],
      aEndPointsMappingList: [],
      oActiveEndpoint: {},
      oActiveEndpointClone: {},
      aUploadedFileIds: [],
      activeTabId: "properties",
      selectedMappingFilterId : FilterDictionary.ALL,
      mappingFilterProps: {},
      oUnmappedElementValuesList: {
        unmappedColumns: {},
        unmappedKlassColumns: {},
        unmappedTaxonomyColumns: {}
      },
      configData: {
        referencedTags: {},
        referencedAttributes: {},
        referencedKlasses: {},
        referencedTaxonomies: {}
      },
      propertyRowTypeData: {},
      selectedPhysicalCatalogId: ""
    }
  };

  var Properties = new Props();

  return {

    getUnmappedElementValuesList: function () {
      return Properties.oUnmappedElementValuesList;
    },

    setUnmappedElementValuesList: function (_oUnmappedValuesList) {
      Properties.oUnmappedElementValuesList = _oUnmappedValuesList;
    },

    setCurrentlyUploadedFileIds: function (_aUploadedFileIds) {
      Properties.aUploadedFileIds = _aUploadedFileIds;
    },

    getActiveEndpoint: function () {
      return Properties.oActiveEndpoint;
    },

    getActiveEndpointClone: function () {
      return Properties.oActiveEndpointClone;
    },

    setActiveEndpoint: function (_oActiveEndpoint) {
      Properties.oActiveEndpoint = _oActiveEndpoint;
      Properties.oActiveEndpointClone = CS.cloneDeep(_oActiveEndpoint);
    },

    getActiveTabId: function () {
      return Properties.activeTabId;
    },

    setActiveTabId: function (_sActiveTabId) {
      Properties.activeTabId = _sActiveTabId;
    },

    getConfigData: function(){
      return Properties.configData;
    },

    getPropertyRowTypeData: function () {
      return Properties.propertyRowTypeData;
    },

    setPropertyRowTypeData: function (_oPropertyRowTypeData) {
      Properties.propertyRowTypeData = _oPropertyRowTypeData;
    },

    getSelectedMappingFilterId: function () {
      return Properties.selectedMappingFilterId;
    },

    setSelectedMappingFilterId: function (_sSelectedMappingFilterId) {
      Properties.selectedMappingFilterId = _sSelectedMappingFilterId;
    },

    resetSelectedMappingFilterId: function () {
      Properties.selectedMappingFilterId = FilterDictionary.ALL;
    },

    getMappingFilterProps: function () {
      return Properties.mappingFilterProps;
    },

    setMappingFilterProps: function (_oMappingFilterProps) {
      Properties.mappingFilterProps = _oMappingFilterProps;
    },

    getSelectedPhysicalCatalogId: function () {
      return Properties.selectedPhysicalCatalogId;
    },

    setSelectedPhysicalCatalogId: function (_sSelectedPhysicalCatalogId) {
      Properties.selectedPhysicalCatalogId = _sSelectedPhysicalCatalogId;
    },

    reset: function () {
      Properties = new Props();
    },

    toJSON: function () {
      return {
        endPointsMappingList: Properties.aEndPointsMappingList
      };
    }
  };


})();

export default ScreenProps;
