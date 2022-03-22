
import {bulkEditTabTypesConstants as BulkEditTabTypesConstants} from "../../tack/bulk-edit-layout-data";

var FilterProps = (function () {

  var Props = function () {
    return {
      sSelectedTabType: BulkEditTabTypesConstants.PROPERTIES,
      bIsBulkEditViewOpen: false,
      aAppliedAttributes: [],
      aAppliedTags: [],
      oReferencedAttributes: {},
      oReferencedTags: {},
      oReferencedTaxonomies: {},
      oReferencedClasses: {},
      sSearchText: "",
      oTaxonomySummary: {
        taxonomiesToAdd: {},
        taxonomiesToDelete: {}
      },
      oClassSummary: {
        classesToAdd: {},
        classesToDelete: {}
      }
    }
  };

  var oProperties = new Props();

  return {

    getSelectedTabForBulkEdit: function () {
      return oProperties.sSelectedTabType;
    },

    setSelectedTabForBulkEdit: function (_sSelectedTabType) {
      oProperties.sSelectedTabType = _sSelectedTabType;
    },

    getIsBulkEditViewOpen: function () {
      return oProperties.bIsBulkEditViewOpen;
    },

    setIsBulkEditViewOpen: function (bVal) {
      oProperties.bIsBulkEditViewOpen = bVal;
    },

    getAppliedAttributes: function () {
      return oProperties.aAppliedAttributes;
    },

    getAppliedTags: function () {
      return oProperties.aAppliedTags;
    },

    getReferencedAttributes: function () {
      return oProperties.oReferencedAttributes;
    },

    setReferencedAttributes: function (_oReferencedAttributes) {
      oProperties.oReferencedAttributes = _oReferencedAttributes;
    },

    getReferencedTags: function () {
      return oProperties.oReferencedTags;
    },

    setReferencedTags: function (_oReferencedTags) {
      oProperties.oReferencedTags = _oReferencedTags;
    },

    getReferencedTaxonomies: function () {
      return oProperties.oReferencedTaxonomies;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.oReferencedTaxonomies = _oReferencedTaxonomies;
    },

    getTreeSearchText: function () {
      return oProperties.sSearchText;
    },

    setTreeSearchText: function (_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getTaxonomySummary: function () {
      return oProperties.oTaxonomySummary;
    },

    getReferencedClasses: function () {
      return oProperties.oReferencedClasses;
    },

    setReferencedClasses: function (_oReferencedClasses) {
      oProperties.oReferencedClasses = _oReferencedClasses;
    },

    getClassesTreeSearchText: function () {
      return oProperties.sSearchText;
    },

    getClassSummary: function () {
      return oProperties.oClassSummary;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {}
    }
  }
})();

export default FilterProps;
