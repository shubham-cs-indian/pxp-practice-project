import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
let SmartDocumentProps = (function () {

  let Props = function () {
    return {
      oSmartDocumentValueList: {
        "smartdocument": {
          id: "smartdocument",
          isChecked: 0,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'Smart Document',
          canCreate: true,
        }
      },
      bSmartDocumentDirty: false,
      smartDocumentEntity: {},
      activeSection: {},
      languageCodes: [],
      aAllowedTaxonomies: [],
      parentTaxonomyList:[],
      referencedTaxonomies: {},
      referencedKlasses: {},
      oTaxonomyPaginationData: {
        from: 0,
        size: 20,
        sortBy: "label",
        sortOrder: "asc",
        types: [],
        searchColumn: "label",
        searchText: "",
      }
    }
  };

  let oProperties = new Props();

  return {

    getIsSmartDocumentConfigDirty: function () {
      return oProperties.bSmartDocumentDirty;
    },

    setIsSmartDocumentConfigDirty: function (_bSmartDocumentDirty) {
      oProperties.bSmartDocumentDirty = _bSmartDocumentDirty;
    },

    getSmartDocumentValueListByTypeGeneric: function () {
      oProperties.oSmartDocumentValueList["smartdocument"].label = oTranslations().TEMPLATES;
      return oProperties.oSmartDocumentValueList;
    },

    getSmartDocumentEntity: function () {
      return oProperties.smartDocumentEntity;
    },

    setSmartDocumentEntity: function (_smartDocumentEntity) {
      oProperties.smartDocumentEntity = _smartDocumentEntity;
    },

    getActiveSection: function () {
      return oProperties.activeSection;
    },

    setActiveSection: function (_activeSection) {
      oProperties.activeSection = _activeSection;
    },

    getLanguageCodes: function () {
      return oProperties.languageCodes;
    },

    setLanguageCodes: function (_languageCodes) {
      oProperties.languageCodes = _languageCodes;
    },

    getReferencedTaxonomies: function () {
      return oProperties.referencedTaxonomies;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.referencedTaxonomies = _oReferencedTaxonomies;
    },

    getReferencedKlasses: function () {
      return oProperties.referencedKlasses;
    },

    setReferencedKlasses: function (_oReferencedKlasses) {
      oProperties.referencedKlasses = _oReferencedKlasses;
    },

    getAllowedTaxonomies: function () {
      return oProperties.aAllowedTaxonomies;
    },

    setAllowedTaxonomies: function (_aAllowedTaxonomies) {
      oProperties.aAllowedTaxonomies = _aAllowedTaxonomies;
    },

    getTaxonomyPaginationData: function () {
      return oProperties.oTaxonomyPaginationData;
    },

    setTaxonomyPaginationData: function (_oTaxonomyPaginationData) {
      oProperties.oTaxonomyPaginationData = _oTaxonomyPaginationData;
    },

    getParentTaxonomyList: function () {
      return oProperties.parentTaxonomyList;
    },

    setParentTaxonomyList: function (_aParentTaxonomyList) {
      oProperties.parentTaxonomyList = _aParentTaxonomyList;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {};
    }
  }
})();

export default SmartDocumentProps;