
import {isEmpty, cloneDeep} from './../../../../../../libraries/cs/cs-lodash';
import ScreenModeUtils from '../helper/screen-mode-utils';
import PaginationProps from './pagination-props';
const { getTranslationDictionary: getTranslation } = ScreenModeUtils;

class AbstractFilterProps extends PaginationProps {
  constructor() {
    super();
    this.oProperties = {
      availableSortData: [], //AvailableSortData,
      taxonomyTree: [],
      taxonomyVisualProps: {},
      taxonomyTreeSearchText: '',
      matchingTaxonomyIds: [],
      defaultTaxonomyTree: [
        {
          id: "-1",
          label: getTranslation().CLASS_TAXONOMY,
          code: "-1",
          children: []
        }
      ],
      defaultTaxonomyVisualProps: {},
      selectedTaxonomies: ["article"],
      isFilterRequired: false,
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
      appliedFilterCollapsedStatus: {},
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
      oInnerAddClassDropDownData: {},
      bIsSelectTaxonomyPopOverVisible: false,
      aSelectedTypes: [],
      filterInfo: {},
      filtersForRecommendedAssets: [],
      aViolatingMandatoryElements: [],
      additionalFiltersForAsset: [],
      filtersForNewProducts: [],
      selectedSortDetails: {},
      aSelectedParentTypeIds: [],
      hideEmpty: true,
      referencedTaxonomies: {},
      referencedClasses: {},
      selectedTaxonomyIds: [],
    }
  }

  getActiveSortDetails = () => this.oProperties.activeSortDetails

  setActiveSortDetails = aSortDetails => {
    this.oProperties.activeSortDetails = aSortDetails;
  }

  getAvailableSortData = () => this.oProperties.availableSortData

  setAvailableSortData = oData => {
    this.oProperties.availableSortData = oData;
  }

  getSelectedSortDetails = () => this.oProperties.selectedSortDetails

  setSelectedSortDetails = oSortDetails => {
    this.oProperties.selectedSortDetails = oSortDetails;
  }

  getTaxonomyTree = () => this.oProperties.taxonomyTree

  setTaxonomyTree = oTaxonomyTree => {
    this.oProperties.taxonomyTree = oTaxonomyTree;
  }

  getDefaultTaxonomyTree = () => {
    this.oProperties.defaultTaxonomyTree[0].label = getTranslation().CLASS_TAXONOMY;
    return this.oProperties.defaultTaxonomyTree;
  }

  getTaxonomyVisualProps = () => this.oProperties.taxonomyVisualProps

  setTaxonomyVisualProps = oTaxonomyVisualProps => {
    this.oProperties.taxonomyVisualProps = oTaxonomyVisualProps;
  }

  getDefaultTaxonomyVisualProps = () => {
    const oDefaultTaxonomyVisualProps = this.oProperties.defaultTaxonomyVisualProps;
    if (isEmpty(oDefaultTaxonomyVisualProps)) {
      oDefaultTaxonomyVisualProps[-1] = {
        isChecked: 0,
        isExpanded: false,
        isHidden: true
      };
    }
    return oDefaultTaxonomyVisualProps;
  }

  getAvailableFilters = () => this.oProperties.availableFilters

  setAvailableFilters = oAvailableFilters => {
    this.oProperties.availableFilters = oAvailableFilters;
  }

  getAppliedFilters = () => this.oProperties.appliedFilters

  setAppliedFilters = aAppliedFilters => {
    this.oProperties.appliedFilters = aAppliedFilters;
  }

  createAppliedFiltersClone = () => {
    this.oProperties.appliedFiltersClone = cloneDeep(this.oProperties.appliedFilters);
  }

  getAppliedFiltersClone = () => this.oProperties.appliedFiltersClone

  setAppliedFiltersClone = aAppliedFiltersClone => {
    this.oProperties.appliedFiltersClone = aAppliedFiltersClone;
  }

  clearAppliedFiltersClone = () => {
    this.oProperties.appliedFiltersClone = null;
  }

  getSearchText = () => this.oProperties.searchText

  setSearchText = sSearchText => {
    this.oProperties.searchText = sSearchText;
  }

  getIsFilterInformationRequired = () => this.oProperties.isFilterRequired;

  setIsFilterInformationRequired = bIsFilterRequired => {
    this.oProperties.isFilterRequired = bIsFilterRequired;
  }

  getOldSearchText = () => this.oProperties.oldSearchText

  setOldSearchText = sOldSearchText => {
    this.oProperties.oldSearchText = sOldSearchText;
  }

  getSelectedAttributes = () => this.oProperties.selectedAttributes

  setSelectedAttributes = aSelectedAttributes => {
    this.oProperties.selectedAttributes = aSelectedAttributes;
  }

  getSelectedRoles = () => this.oProperties.selectedRoles

  setSelectedRoles = aSelectedRoles => {
    this.oProperties.selectedRoles = aSelectedRoles;
  }

  getShowDetails = () => this.oProperties.showDetails

  setShowDetails = _bShowDetails => {
    this.oProperties.showDetails = _bShowDetails;
  }

  getIsFilterDirty = () => this.oProperties.bIsFilterDirty

  setIsFilterDirty = bIsFilterDirty => {
    this.oProperties.bIsFilterDirty = bIsFilterDirty;
  }

  setSelectedParentTaxonomyIds = aIds => {
    this.oProperties.selectedParentTaxonomyIds = aIds;
  }

  getSelectedParentTaxonomyIds = () => this.oProperties.selectedParentTaxonomyIds

  setSelectedOuterParentId = sId => {
    this.oProperties.selectedOuterParentId = sId;
  }

  getSelectedOuterParentId = () => this.oProperties.selectedOuterParentId

  getTaxonomySearchText = () => this.oProperties.taxonomyTreeSearchText

  setTaxonomySearchText = sTaxonomyTreeSearchText => {
    this.oProperties.taxonomyTreeSearchText = sTaxonomyTreeSearchText;
  }

  getMatchingTaxonomyIds = () => this.oProperties.matchingTaxonomyIds

  setMatchingTaxonomyIds = aMatchingTaxonomyIds => {
    this.oProperties.matchingTaxonomyIds = aMatchingTaxonomyIds;
  }

  setAdvancedSearchPanelShowStatus = bVal => {
    this.oProperties.bAdvancedSearchPanelShowStatus = bVal
  }

  getAdvancedSearchPanelShowStatus = () => this.oProperties.bAdvancedSearchPanelShowStatus

  getAppliedFilterCollapseStatusMap = () => this.oProperties.appliedFilterCollapsedStatus

  getIsAdvanceSearchFilterClickedStatus = () => this.oProperties.bIsAdvanceSearchFilterClickedStatus

  setIsAdvanceSearchFilterClickedStatus = bVal => {
    this.oProperties.bIsAdvanceSearchFilterClickedStatus = bVal;
  }

  setAdvancedFilterAppliedStatus = bVal => {
    this.oProperties.bIsAdvancedFilterApplied = bVal;
  }

  getAdvancedFilterAppliedStatus = () => this.oProperties.bIsAdvancedFilterApplied

  setAppliedFiltersCloneBeforeModifications = aData => {
    this.oProperties.aAppliedFiltersCloneBeforeModifications = aData;
  }

  getAppliedFiltersCloneBeforeModifications = () => this.oProperties.aAppliedFiltersCloneBeforeModifications

  setTaxonomyTreeFlatMap = oMap => {
    this.oProperties.oTaxonomyTreeFlatMap = oMap;
  }

  getTaxonomyTreeFlatMap = () => this.oProperties.oTaxonomyTreeFlatMap

  setAllAffectedTreeNodeIds = aIds => {
    this.oProperties.aAllAffectedTreeNodeIds = aIds;
  }

  getAllAffectedTreeNodeIds = () => this.oProperties.aAllAffectedTreeNodeIds

  getTaxonomyTreeBackup = () => this.oProperties.aTaxonomyTreeBackup

  setTaxonomyTreeBackup = _aTaxonomyTreeBackup => {
    this.oProperties.aTaxonomyTreeBackup = _aTaxonomyTreeBackup;
  }

  clearTaxonomyTreeBackup = () => {
    this.oProperties.aTaxonomyTreeBackup = {};
    this.oProperties.selectedOldOuterParentId = "";
  }

  getOldSelectedOuterParentId = () => this.oProperties.selectedOldOuterParentId

  setOldSelectedOuterParentId = _OldSelectedOuterParentId => {
    this.oProperties.selectedOldOuterParentId = _OldSelectedOuterParentId;
  }

  getOldTaxonomySearchText = () => this.oProperties.taxonomyTreeOldSearchText

  setOldTaxonomySearchText = sTaxonomyTreeOldSearchText => {
    this.oProperties.taxonomyTreeOldSearchText = sTaxonomyTreeOldSearchText;
  }

  getTaxonomyNodeSections = () => this.oProperties.oTaxonomyNodeSections

  setTaxonomyNodeSections = aSections => {
    this.oProperties.oTaxonomyNodeSections = aSections;
  }

  getTaxonomySettingIconClickedNode = () => this.oProperties.oTaxonomySettingIconClickedNode

  setTaxonomySettingIconClickedNode = sId => {
    this.oProperties.oTaxonomySettingIconClickedNode = sId;
  }

  getTaxonomyActiveClass = () => this.oProperties.oTaxonomyActiveClass

  setTaxonomyActiveClass = _oTaxonomyActiveClass => {
    this.oProperties.oTaxonomyActiveClass = _oTaxonomyActiveClass;
  }

  getEditableHierarchyNodeId = () => this.oProperties.sEditableHierarchyId

  setEditableHierarchyNodeId = sId => {
    this.oProperties.sEditableHierarchyId = sId;
  }

  getIsAllClassDropDownListOpen = () => this.oProperties.bIsAllClassDropDownListOpen

  setIsAllClassDropDownListOpen = bVal => {
    this.oProperties.bIsAllClassDropDownListOpen = bVal
  }

  getInnerAddClassDropDownData = () => this.oProperties.oInnerAddClassDropDownData

  setInnerAddClassDropDownData = oData => {
    this.oProperties.oInnerAddClassDropDownData = oData
  }

  getIsSelectTaxonomyPopOverVisible = () => this.oProperties.bIsSelectTaxonomyPopOverVisible

  setIsSelectTaxonomyPopOverVisible = bVal => {
    this.oProperties.bIsSelectTaxonomyPopOverVisible = bVal;
  }

  getSelectedTypes = () => this.oProperties.aSelectedTypes

  setSelectedTypes = aList => {
    this.oProperties.aSelectedTypes = aList
  }

  getSelectedParentTypeIds = () => this.oProperties.aSelectedParentTypeIds;

  setSelectedParentTypeIds = aList => {
    this.oProperties.aSelectedParentTypeIds = aList
  };

  getViolatingMandatoryElements = () => this.oProperties.aViolatingMandatoryElements

  setViolatingMandatoryElements = _aViolatingMandatoryElements => {
    this.oProperties.aViolatingMandatoryElements = _aViolatingMandatoryElements;
  }

  getAdditionalFiltersForAsset = () => this.oProperties.additionalFiltersForAsset;

  setAdditionalFiltersForAsset = _aAdditionalFiltersForAsset => {
    this.oProperties.additionalFiltersForAsset = _aAdditionalFiltersForAsset;
  }

  setFilterInfo = oFilterInfo => {
    this.oProperties.filterInfo = oFilterInfo;
  }

  getFilterInfo = () => this.oProperties.filterInfo

  getFiltersForRecommendedAssets = () => this.oProperties.filtersForRecommendedAssets

  setFiltersForRecommendedAssets = _aFiltersForRecommendedAssets => {
    this.oProperties.filtersForRecommendedAssets = _aFiltersForRecommendedAssets;
  }

  getFiltersForNewProducts = () => this.oProperties.filtersForNewProducts

  setFiltersForNewProducts = _aFiltersForNewProducts => {
    this.oProperties.filtersForNewProducts = _aFiltersForNewProducts;
  }

  getHideEmpty = () => this.oProperties.hideEmpty;

  setHideEmpty = bVal => {
    this.oProperties.hideEmpty = bVal
  };

  getReferencedTaxonomies = () => {
    return this.oProperties.referencedTaxonomies;
  };

  setReferencedTaxonomies = (oReferencedTaxonomies) => {
    this.oProperties.referencedTaxonomies = oReferencedTaxonomies;
  };

  getReferencedClasses = () => {
    return this.oProperties.referencedClasses;
  };

  setReferencedClasses = (oReferencedClasses) => {
    this.oProperties.referencedClasses = oReferencedClasses;
  };

  getSelectedTaxonomyIds = () => {
    return this.oProperties.selectedTaxonomyIds;
  };

  setSelectedTaxonomyIds = (aTaxonomyIds) => {
    this.oProperties.selectedTaxonomyIds = aTaxonomyIds;
  };

  reset = function () {
    Object.resetDataProperties(this);
  }

  setProps = (key, value) => {
    this.oProperties[key] = value;
  }

  toJSON = () => ({})
}

export default AbstractFilterProps;
