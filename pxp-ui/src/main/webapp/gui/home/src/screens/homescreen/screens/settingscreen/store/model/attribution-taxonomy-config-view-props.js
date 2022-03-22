var AttributionTaxonomyConfigViewProps = (function () {

  let Props = function () {
    return {
      oActiveTaxonomy: {},
      oActiveDetailedTaxonomy: {},
      bCategoryScreenLockedStatus: false,
      aSelectedLevels: [-1],
      oLinkedMasterTypeTags: {},
      aAllowedTagValues: [],
      bPopoverVisibilityStatus: false,
      sAddChildPopoverVisibleLevelId: "",
      bIsScrollAutomaticallyEnabled: false,
      oActiveTaxonomyLevel: {},
      oActiveTaxonomyLevelChildren: {},
      oReferencedData: {},
      oAllowedTaxonomyHierarchyList: {},
      oAllowedTaxonomyConfigDetails: {},
      level: 0,
      oSelectedTaxonomyId: {},
      sSelectedTabId :""
    }
  };

  let oProperties = new Props();

  return {

    getPopoverVisibilityStatus: function () {
      return oProperties.bPopoverVisibilityStatus;
    },

    setPopoverVisibilityStatus: function (bStatus) {
      oProperties.bPopoverVisibilityStatus = bStatus;
    },

    getLinkedMasterTypeTags: function () {
      return oProperties.oLinkedMasterTypeTags;
    },

    getActiveTaxonomy: function () {
      return oProperties.oActiveTaxonomy;
    },

    setActiveTaxonomy: function (_oActiveTaxonomy) {
      oProperties.oActiveTaxonomy = _oActiveTaxonomy;
    },

    getActiveTaxonomyLevel: function () {
      return oProperties.oActiveTaxonomyLevel;
    },

    setActiveTaxonomyLevel: function (_oActiveTaxonomyLevel) {
      oProperties.oActiveTaxonomyLevel = _oActiveTaxonomyLevel;
    },

    getActiveTaxonomyLevelChildren: function () {
      return oProperties.oActiveTaxonomyLevelChildren;
    },

    setActiveTaxonomyLevelChildren: function (_oActiveTaxonomyLevelChildren) {
      oProperties.oActiveTaxonomyLevelChildren = _oActiveTaxonomyLevelChildren;
    },

    getAttributionTaxonomyListActiveTabId: function () {
      return oProperties.sSelectedTabId;
    },

    setAttributionTaxonomyListActiveTabId: function (sSelectedTabId) {
      oProperties.sSelectedTabId = sSelectedTabId;
    },


    getActiveDetailedTaxonomy: function () {
      return oProperties.oActiveDetailedTaxonomy;
    },

    setActiveDetailedTaxonomy: function (_oActiveDetailedTaxonomy) {
      oProperties.oActiveDetailedTaxonomy = _oActiveDetailedTaxonomy;
    },

    getAttributionTaxonomyScreenLockedStatus: function () {
      return oProperties.bCategoryScreenLockedStatus;
    },

    setAttributionTaxonomyScreenLockedStatus: function (_bLock) {
      oProperties.bCategoryScreenLockedStatus = _bLock;
    },

    getSelectedTaxonomyLevels: function () {
      return oProperties.aSelectedLevels;
    },

    getAllowedTagValues: function () {
      return oProperties.aAllowedTagValues;
    },

    setAllowedTagValues: function (_aAllowedTagValues) {
      oProperties.aAllowedTagValues = _aAllowedTagValues;
    },

    getAddChildPopoverVisibleLevelId: function () {
      return oProperties.sAddChildPopoverVisibleLevelId;
    },

    setAddChildPopoverVisibleLevelId: function (sLevelId) {
      oProperties.sAddChildPopoverVisibleLevelId = sLevelId;
    },

    resetSelectedTaxonomyLevels: function () {
      oProperties.aSelectedLevels = [-1];
    },

    setIsAttributionTaxonomyHierarchyScrollAutomaticallyEnabled: function (bVal) {
      oProperties.bIsScrollAutomaticallyEnabled = bVal;
    },

    getIsAttributionTaxonomyHierarchyScrollAutomaticallyEnabled: function () {
      return oProperties.bIsScrollAutomaticallyEnabled;
    },

    setReferencedData: function (_oReferencedData) {
      oProperties.oReferencedData = _oReferencedData;
    },

    getReferencedData: function () {
      return oProperties.oReferencedData;
    },

    getAllowedTaxonomyHierarchyList: function () {
      return oProperties.oAllowedTaxonomyHierarchyList;
    },

    setAllowedTaxonomyHierarchyList: function (_oAllowedTaxonomyHierarchyList) {
      oProperties.oAllowedTaxonomyHierarchyList = _oAllowedTaxonomyHierarchyList;
    },

    getAllowedTaxonomyConfigDetails: function () {
      return oProperties.oAllowedTaxonomyConfigDetails;
    },

    setAllowedTaxonomyConfigDetails: function (_oAllowedTaxonomyConfigDetails) {
      oProperties.oAllowedTaxonomyConfigDetails = _oAllowedTaxonomyConfigDetails;
    },

    setLevel: function (_level) {
      oProperties.level = _level;
    },

    getLevel: function () {
      return oProperties.level;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {
        oActiveTaxonomy: oProperties.oActiveTaxonomy,
        oActiveDetailedTaxonomy: oProperties.oActiveDetailedTaxonomy,
        bCategoryScreenLockedStatus: oProperties.bCategoryScreenLockedStatus,
        aSelectedLevels: oProperties.aSelectedLevels,
        oLinkedMasterTypeTags: oProperties.oLinkedMasterTypeTags,
        aAllowedTagValues: oProperties.aAllowedTagValues,
        bPopoverVisibilityStatus: oProperties.bPopoverVisibilityStatus,
        sAddChildPopoverVisibleLevelId: oProperties.sAddChildPopoverVisibleLevelId,
        bIsScrollAutomaticallyEnabled: oProperties.bIsScrollAutomaticallyEnabled,
        oActiveTaxonomyLevel: oProperties.oActiveTaxonomyLevel,
        oActiveTaxonomyLevelChildren: oProperties.oActiveTaxonomyLevelChildren,
        oReferencedData: oProperties.oReferencedData
      };
    }
  }
})();

export default AttributionTaxonomyConfigViewProps;