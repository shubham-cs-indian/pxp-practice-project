/**
 * Created by Vinay Gaikwad on 10-4-2019.
 */
import TabHeaderData from '../../tack/mock/mock-data-for-map-summery-header';

var AuthorizationMappingConfigViewProps = (function () {

  var Props = function () {
    return {
      oSelectedMapping: {},
      aAuthorizationMappingGridData: [],
      bIsAuthorizationMappingDialogActive:false,
      aSelectedMappingRows: [],
      aHeaderData: [
        {
          id: TabHeaderData.class,
          label: 'Class'
        },
        {
          id: TabHeaderData.context,
          label: 'Variant'
        },
        {
          id: TabHeaderData.relationship,
          label: 'Relationship'
        },
        {
          id: TabHeaderData.attribute,
          label: 'Attribute'
        },
        {
          id: TabHeaderData.tag,
          label: 'Tag'
        },
        {
          id: TabHeaderData.taxonomy,
          label: 'Taxonomy'
        }
      ],
      sSelectedTabId: TabHeaderData.attribute,
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
        referencedContexts: {},
        referencedRelationships: {},
      },
      checkboxClickedDetails : {
       attributes : false,
       tags : false,
       classes : false,
       taxonomies : false,
       contexts : false,
       relationships : false,
      },
      toggleSelectionClickedDetails : {
        attributes : false,
        tags : false,
        classes : false,
        taxonomies : false,
        contexts : false,
        relationships : false,
      },
    }
  };

  var oProperties = new Props();

  return {

    getSelectedMapping: function () {
      return oProperties.oSelectedMapping;
    },

    setSelectedMapping: function (_oSelectedMapping) {
      oProperties.oSelectedMapping = _oSelectedMapping;
    },

    getCheckboxClickedDetails: function () {
      return oProperties.checkboxClickedDetails;
    },

    setCheckboxClickedDetails: function (_oCheckboxClickedDetails) {
      oProperties.checkboxClickedDetails = _oCheckboxClickedDetails;
    },

    getToggleSelectionClickedDetails: function () {
      return oProperties.toggleSelectionClickedDetails;
    },

    setToggleSelectionClickedDetails: function (_oToggleSelectionClickedDetails) {
      oProperties.toggleSelectionClickedDetails = _oToggleSelectionClickedDetails;
    },

    getConfigData: function(){
      return oProperties.configData;
    },

    setConfigData: function (_oConfigData) {
      oProperties.configData = _oConfigData;
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

    getIsAuthorizationMappingDialogActive: function () {
      return oProperties.bIsAuthorizationMappingDialogActive;
    },

    setIsAuthorizationMappingDialogActive: function (_bIsAuthorizationMappingDialogActive) {
      oProperties.bIsAuthorizationMappingDialogActive = _bIsAuthorizationMappingDialogActive;
    },

    getAuthorizationMappingGridData: function () {
      return oProperties.aAuthorizationMappingGridData;
    },

    setAuthorizationMappingGridData: function (_aAuthorizationMappingGridData) {
      oProperties.aAuthorizationMappingGridData = _aAuthorizationMappingGridData;
    },


    reset: function () {
      oProperties = new Props();
    }

  };
})();

export default AuthorizationMappingConfigViewProps;