/**
 * Created by CS87 on 07-10-2016.
 */

var CategoryConfigViewProps = (function () {

  let Props = function () {
    return {
      oActiveTaxonomy: {},
      bCategoryScreenLockedStatus: false,
      aSelectedLevels: [-1],
      oActiveTaxonomyLevel: {},
      oActiveTaxonomyLevelChildren: {},
      bPopoverVisibilityStatus: false,
      oLinkedMasterTypeTags: {},
      sAddChildPopoverVisibleLevelId: "",
      aAllowedTagValues: [],
      level: 0
    };
  };

  let oProperties = new Props();

  return {

    getActiveTaxonomy: function () {
      return oProperties.oActiveTaxonomy;
    },

    setActiveTaxonomy: function (_oActiveTaxonomy) {
      oProperties.oActiveTaxonomy = _oActiveTaxonomy;
    },

    getTaxonomyScreenLockedStatus: function () {
      return oProperties.bCategoryScreenLockedStatus;
    },

    setAttributionTaxonomyScreenLockedStatus: function (_bLock) {
      oProperties.bCategoryScreenLockedStatus = _bLock;
    },

    getSelectedTaxonomyLevels: function () {
      return oProperties.aSelectedLevels;
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

    getPopoverVisibilityStatus: function () {
      return oProperties.bPopoverVisibilityStatus;
    },

    setPopoverVisibilityStatus: function (bStatus) {
      oProperties.bPopoverVisibilityStatus = bStatus;
    },

    getLinkedMasterTypeTags: function () {
      return oProperties.oLinkedMasterTypeTags;
    },

    getAddChildPopoverVisibleLevelId: function () {
      return oProperties.sAddChildPopoverVisibleLevelId;
    },

    setAddChildPopoverVisibleLevelId: function (sLevelId) {
      oProperties.sAddChildPopoverVisibleLevelId = sLevelId;
    },

    getAllowedTagValues: function () {
      return oProperties.aAllowedTagValues;
    },

    setAllowedTagValues: function (_aAllowedTagValues) {
      oProperties.aAllowedTagValues = _aAllowedTagValues;
    },

    resetSelectedTaxonomyLevels: function () {
      oProperties.aSelectedLevels = [-1];
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

  }
})();

export default CategoryConfigViewProps;