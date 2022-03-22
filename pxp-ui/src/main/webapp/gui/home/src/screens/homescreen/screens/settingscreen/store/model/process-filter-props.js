
import CS from '../../../../../../libraries/cs';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';

var ProcessFilterProps = (function () {

  var Props = function () {
    return {
      availableSortData: [], //AvailableSortData,
      taxonomyTree: [],
      taxonomyVisualProps: {},
      taxonomyTreeSearchText: '',
      matchingTaxonomyIds: [],
      defaultTaxonomyTree: [
        {
          id: "-1",
          label: 'CLASS_TAXONOMY',
          children: []
        }
      ],
      defaultTaxonomyVisualProps: {},
      taxonomyVisualPropsClone: {},
      selectedTaxonomies: ["article"],
      searchText: '',
      oldSearchText: '',
      availableFilters: [], //AvailableFilterData,
      appliedFilters: [], //AppliedFilterData,
      appliedFiltersClone: null,
      selectedAttributes: [],
      selectedRoles: [],
      showDetails: false,
      activeSortDetails: {},
      activeSortId: '',
      activeSortOrder: 'asc',
      bIsFilterDirty: false,
      selectedParentTaxonomyIds: [],
      selectedOuterParentId: "",
      bAdvancedSearchPanelShowStatus: false,
      appliedFilterCollapsedStatus:{},
      bIsAdvanceSearchFilterClickedStatus: false,
      bIsAdvancedFilterApplied: false,
      aAppliedFiltersCloneBeforeModifications: [],
      oTaxonomyTreeFlatMap: {},
      aAllAffectedTreeNodeIds: [],
      aTaxonomyTreeBackup: {},
      selectedOldOuterParentId: '',
      taxonomyTreeOldSearchText: '',
      oTaxonomySettingIconClickedNode: {},
      oTaxonomyNodeSections: {},
      oTaxonomyActiveClass: {},
      sEditableHierarchyId: '',
      bIsAllClassDropDownListOpen: false,
      oInnerAddClassDropDownData:{},
      bIsSelectTaxonomyPopOverVisible: false,
      sTaxonomyDeselectClickedId: "",
      aSelectedTypes: [],
      aViolatingMandatoryElements: [],
      bSearchCriteriaDialogShowStatus: false,
      bSearchCriteriaData: {},
      taxonomyTreeData: [],
      bIsTaxonomySelected: false,
      oSearchCriteria: {
        selectedBaseTypes:[],
        exportSubType:"",
        selectedTypes:[],
        selectedTaxonomyIds:[],
        baseEntityIIds:[],
        collectionId: "",
        bookmarkId: ""
      },
    }
  };

  var oProperties = new Props();

  return {

    getActiveSortDetails: function () {
      return oProperties.activeSortDetails;
    },

    setActiveSortDetails: function (aSortDetails) {
      oProperties.activeSortDetails = aSortDetails;
    },

    /*getActiveSortId: function () {
      return oProperties.activeSortId;
    },

    setActiveSortId: function (sId) {
      oProperties.activeSortId = sId;
    },

    getActiveSortOrder: function () {
      return oProperties.activeSortOrder ;
    },

    setActiveSortOrder: function (sOrder) {
      oProperties.activeSortOrder = sOrder;
    },*/

    getAvailableSortData: function () {
      return oProperties.availableSortData;
    },

    setAvailableSortData: function (oData) {
      oProperties.availableSortData = oData;
    },

    getTaxonomyTree: function () {
      return oProperties.taxonomyTree;
    },

    setTaxonomyTree: function (oTaxonomyTree) {
      oProperties.taxonomyTree = oTaxonomyTree;
    },

    getDefaultTaxonomyTree: function () {
      oProperties.defaultTaxonomyTree[0].label = getTranslation().CLASS_TAXONOMY;
      return oProperties.defaultTaxonomyTree;
    },

    getTaxonomyVisualProps: function () {
      return oProperties.taxonomyVisualProps;
    },

    setTaxonomyVisualProps: function (oTaxonomyVisualProps) {
      oProperties.taxonomyVisualProps = oTaxonomyVisualProps;
    },

   /* getDefaultTaxonomyVisualProps: function () {
      var oDefaultTaxonomyVisualProps = oProperties.defaultTaxonomyVisualProps;
      if (CS.isEmpty(oDefaultTaxonomyVisualProps)) {
        oDefaultTaxonomyVisualProps[-1] = {
          isChecked: 0,
          isExpanded: false,
          isHidden: true
        };
      }
      return oDefaultTaxonomyVisualProps;
    },

    getDesiredTaxonomyVisualProps: function () {
      if (!CS.isEmpty(oProperties.taxonomyVisualPropsClone)) {
        return oProperties.taxonomyVisualPropsClone;
      } else {
        return oProperties.taxonomyVisualProps;
      }
    },

    createTaxonomyVisualPropsClone: function () {
      oProperties.taxonomyVisualPropsClone = CS.cloneDeep(oProperties.taxonomyVisualProps);
    },

    getTaxonomyVisualPropsClone: function () {
      return oProperties.taxonomyVisualPropsClone;
    },

    setTaxonomyVisualPropsClone: function (oTaxonomyVisualProps) {
      oProperties.taxonomyVisualPropsClone = oTaxonomyVisualProps;
    },

    clearTaxonomyVisualPropsClone: function () {
      oProperties.taxonomyVisualPropsClone = {};
    },

    getSelectedTaxonomies: function () {
      return oProperties.selectedTaxonomies;
    },

    setSelectedTaxonomies: function (oSelectedTaxonomies) {
      oProperties.selectedTaxonomies = oSelectedTaxonomies;
    },*/

    getAvailableFilters: function () {
      return oProperties.availableFilters;
    },

    setAvailableFilters: function (oAvailableFilters) {
      oProperties.availableFilters = oAvailableFilters;
    },

    getAppliedFilters: function () {
      return oProperties.appliedFilters;
    },

    setAppliedFilters: function (aAppliedFilters) {
      oProperties.appliedFilters = aAppliedFilters;
    },

    createAppliedFiltersClone: function () {
      oProperties.appliedFiltersClone = CS.cloneDeep(oProperties.appliedFilters);
    },

    getAppliedFiltersClone: function () {
      return oProperties.appliedFiltersClone;
    },

    setAppliedFiltersClone: function (aAppliedFiltersClone) {
      oProperties.appliedFiltersClone = aAppliedFiltersClone;
    },

    clearAppliedFiltersClone: function () {
      oProperties.appliedFiltersClone = null;
    },

    getSearchText: function () {
      return oProperties.searchText;
    },

    setSearchText: function (sSearchText) {
      oProperties.searchText = sSearchText;
    },

    getOldSearchText: function () {
      return oProperties.oldSearchText;
    },

    setOldSearchText: function (sOldSearchText) {
      oProperties.oldSearchText = sOldSearchText;
    },

    /*getSelectedAttributes: function () {
      return oProperties.selectedAttributes;
    },

    setSelectedAttributes: function (aSelectedAttributes) {
      oProperties.selectedAttributes = aSelectedAttributes;
    },

    getSelectedRoles: function () {
      return oProperties.selectedRoles;
    },

    setSelectedRoles: function (aSelectedRoles) {
      oProperties.selectedRoles = aSelectedRoles;
    },*/

    getShowDetails: function () {
      return oProperties.showDetails;
    },

    setShowDetails: function (_bShowDetails) {
      oProperties.showDetails = _bShowDetails;
    },

    getIsFilterDirty: function () {
      return oProperties.bIsFilterDirty;
    },

    setIsFilterDirty: function (bIsFilterDirty) {
      oProperties.bIsFilterDirty = bIsFilterDirty;
    },

    setSelectedParentTaxonomyIds: function (aIds) {
      oProperties.selectedParentTaxonomyIds = aIds;
    },

    getSelectedParentTaxonomyIds: function () {
      return oProperties.selectedParentTaxonomyIds;
    },

    setSelectedOuterParentId: function (sId) {
      oProperties.selectedOuterParentId = sId;
    },

    getSelectedOuterParentId: function () {
      return oProperties.selectedOuterParentId;
    },

    getTaxonomySearchText: function () {
      return oProperties.taxonomyTreeSearchText;
    },

    setTaxonomySearchText: function(sTaxonomyTreeSearchText){
      oProperties.taxonomyTreeSearchText = sTaxonomyTreeSearchText;
    },

    getMatchingTaxonomyIds: function () {
      return oProperties.matchingTaxonomyIds;
    },

    setMatchingTaxonomyIds: function(aMatchingTaxonomyIds){
      oProperties.matchingTaxonomyIds = aMatchingTaxonomyIds;
    },

    setAdvancedSearchPanelShowStatus: function (bVal) {
      oProperties.bAdvancedSearchPanelShowStatus = bVal
    },

    getAdvancedSearchPanelShowStatus: function () {
      return oProperties.bAdvancedSearchPanelShowStatus;
    },

    getAppliedFilterCollapseStatusMap: function () {
      return oProperties.appliedFilterCollapsedStatus;
    },

    getIsAdvanceSearchFilterClickedStatus: function () {
      return oProperties.bIsAdvanceSearchFilterClickedStatus;
    },

    setIsAdvanceSearchFilterClickedStatus: function (bVal) {
      oProperties.bIsAdvanceSearchFilterClickedStatus = bVal;
    },

    setAdvancedFilterAppliedStatus: function (bVal) {
      oProperties.bIsAdvancedFilterApplied = bVal;
    },

    getAdvancedFilterAppliedStatus: function () {
      return oProperties.bIsAdvancedFilterApplied;
    },

    setAppliedFiltersCloneBeforeModifications: function (aData) {
      oProperties.aAppliedFiltersCloneBeforeModifications = aData;
    },

    getAppliedFiltersCloneBeforeModifications: function () {
      return oProperties.aAppliedFiltersCloneBeforeModifications;
    },

    setTaxonomyTreeFlatMap: function (oMap) {
      oProperties.oTaxonomyTreeFlatMap = oMap;
    },

    getTaxonomyTreeFlatMap: function () {
      return oProperties.oTaxonomyTreeFlatMap;
    },

    setAllAffectedTreeNodeIds: function (aIds) {
      oProperties.aAllAffectedTreeNodeIds = aIds;
    },

    getAllAffectedTreeNodeIds: function () {
      return oProperties.aAllAffectedTreeNodeIds;
    },

    getTaxonomyTreeBackup: function () {
      return oProperties.aTaxonomyTreeBackup;
    },

    setTaxonomyTreeBackup: function (_aTaxonomyTreeBackup) {
      oProperties.aTaxonomyTreeBackup = _aTaxonomyTreeBackup;
    },

    clearTaxonomyTreeBackup: function () {
      oProperties.aTaxonomyTreeBackup = {};
      oProperties.selectedOldOuterParentId = "";
    },

    getOldSelectedOuterParentId: function () {
      return oProperties.selectedOldOuterParentId;
    },

    setOldSelectedOuterParentId: function (_OldSelectedOuterParentId) {
      oProperties.selectedOldOuterParentId = _OldSelectedOuterParentId;
    },

    getOldTaxonomySearchText: function () {
      return oProperties.taxonomyTreeOldSearchText;
    },

    setOldTaxonomySearchText: function(sTaxonomyTreeOldSearchText){
      oProperties.taxonomyTreeOldSearchText = sTaxonomyTreeOldSearchText;
    },

    getTaxonomyNodeSections: function () {
      return oProperties.oTaxonomyNodeSections;
    },

    setTaxonomyNodeSections: function (aSections) {
      oProperties.oTaxonomyNodeSections = aSections;
    },

    getTaxonomySettingIconClickedNode: function () {
      return oProperties.oTaxonomySettingIconClickedNode;
    },

    setTaxonomySettingIconClickedNode: function (sId) {
      oProperties.oTaxonomySettingIconClickedNode = sId;
    },

    getTaxonomyActiveClass: function () {
      return oProperties.oTaxonomyActiveClass;
    },

    setTaxonomyActiveClass: function (_oTaxonomyActiveClass) {
      oProperties.oTaxonomyActiveClass = _oTaxonomyActiveClass;
    },

    /*getEditableHierarchyNodeId: function () {
      return oProperties.sEditableHierarchyId;
    },

    setEditableHierarchyNodeId: function (sId) {
      oProperties.sEditableHierarchyId = sId;
    },

    getIsAllClassDropDownListOpen: function () {
      return oProperties.bIsAllClassDropDownListOpen;
    },

    setIsAllClassDropDownListOpen: function (bVal) {
      oProperties.bIsAllClassDropDownListOpen = bVal
    },

    getInnerAddClassDropDownData: function () {
      return oProperties.oInnerAddClassDropDownData;
    },*/

    setInnerAddClassDropDownData: function (oData) {
      oProperties.oInnerAddClassDropDownData = oData
    },

    getIsSelectTaxonomyPopOverVisible: function () {
      return oProperties.bIsSelectTaxonomyPopOverVisible;
    },

    setIsSelectTaxonomyPopOverVisible: function (bVal) {
      oProperties.bIsSelectTaxonomyPopOverVisible = bVal;
    },

   /* getTaxonomyDeselectClickedId: function () {
      return oProperties.sTaxonomyDeselectClickedId;
    },

    setTaxonomyDeselectClickedId: function (sID) {
      oProperties.sTaxonomyDeselectClickedId = sID;
    },*/

    getSelectedTypes: function () {
      return oProperties.aSelectedTypes;
    },

    setSelectedTypes: function (aList) {
      oProperties.aSelectedTypes = aList
    },

    getViolatingMandatoryElements: function () {
      return oProperties.aViolatingMandatoryElements;
    },

    setViolatingMandatoryElements: function (_aViolatingMandatoryElements) {
      oProperties.aViolatingMandatoryElements = _aViolatingMandatoryElements;
    },

    getSearchCriteriaDialogShowStatus: function () {
      return oProperties.bSearchCriteriaDialogShowStatus;
    },

    setSearchCriteriaDialogShowStatus: function (_bSearchCriteriaDialogShowStatus) {
      oProperties.bSearchCriteriaDialogShowStatus = _bSearchCriteriaDialogShowStatus;
    },

    getSearchCriteriaData: function () {
      return oProperties.bSearchCriteriaData;
    },

    setSearchCriteriaData: function (_bSearchCriteriaData) {
      oProperties.bSearchCriteriaData = _bSearchCriteriaData;
    },

    getTaxonomyTreeData: function () {
      return oProperties.taxonomyTreeData;
    },

    setTaxonomyTreeData: function (_aTaxonomyTreeData) {
      oProperties.taxonomyTreeData = _aTaxonomyTreeData;
    },

    getIsTaxonomySelected: function () {
      return oProperties.bIsTaxonomySelected;
    },

    setIsTaxonomySelected: function (_bIsTaxonomySelected) {
      oProperties.bIsTaxonomySelected = _bIsTaxonomySelected;
    },

    getSearchCriteria: function () {
      return oProperties.oSearchCriteria;
    },

    setSearchCriteria: function (_oSearchCriteria) {
      oProperties.oSearchCriteria = _oSearchCriteria;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {

      }
    }


  }
})();

export default ProcessFilterProps;
