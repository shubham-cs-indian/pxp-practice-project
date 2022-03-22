import CS from '../../../../../../libraries/cs';
import ContentScreenConstants from './content-screen-constants';
import SortFieldDictionary from '../../tack/mock/mock-data-for-sort-field-dictionary';
import ZoomSettings from '../../../../../../commonmodule/tack/zoom-toolbar-settings';

var ScreenProps = (function () {

  var Props = function () {

    return {
      sPreviouslySelectedModuleId: "",
      aToolbarDummyButtons: [],
      aSelectedContents: [],
      aSelectedContentIds: [],
      aSelectedSetList: [],
      aSelectedContentContentList: [],
      oActiveContent: {},
      oActiveContentClass: {},
      aActiveClassIds: [],
      aActiveClonedClassIds: [],
      aActiveTaxonomyIds: [],
      aActiveClonedTaxonomyIds: [],
      oActiveTaxonomyPaginationData: {},
      oDefaultActiveTaxonomyPaginationData: {from: 0, size: ZoomSettings.defaultPaginationLimit, searchColumn: "label", searchText: ""},
      aActiveSections: [],
      oConflictingValues: {},
      oReferencedClasses: {},
      oReferencedTemplate: {},
      oReferencedTemplates: {},
      oReferencedPropertyCollections: {},
      oReferencedTaxonomies: {},
      oReferencedElements: {},
      oReferencedAttributes: {},
      oReferencedTags: {},
      oReferencedRoles: {},
      aReferencedVariantContexts: {},
      ReferencedRelationships: {},
      oReferencedNatureRelationships: {},
      bVariantTagSummaryEditViewOpen: false,
      oVariantsCount: {},
      sSelectedTemplateId: "",
      aImageVariantData:[],

      sPreviousScreenMode: ContentScreenConstants.entityModes.ARTICLE_MODE,
      sContentScreenMode: ContentScreenConstants.entityModes.ARTICLE_MODE, // possible values: "grid" and "edit"
      oRelationshipViewMode: {}, //stores relationship ids and their corresponding view mode
      sCurrentTreeContext: ContentScreenConstants.entityModes.ARTICLE_MODE,

      iPaginatedIndex: 0,
      sItemSelectionMode: '',
      iDefaultPaginationLimit: ZoomSettings.defaultPaginationLimit,
      iPreviousDefaultPaginationLimit: ZoomSettings.defaultPaginationLimit,
      iDefaultPaginationInnerLimit: ZoomSettings.defaultPaginationLimit,
      oSelectedContext: {
        'context': '',
        'elementId': '',
        'structureId': '',
        'masterEntityId': '',
        'attributeType': null,
        'sectionId': ''
      },
      oTagGroupValueProps: {},
      oContentFilterProps: {
        attributeList: [],
        attributes: {},
        typeList: [],
        selectedAttributes: [],
        tagList: [],
        selectedTags: [],
        allSearch: '',
        isActive: false,
        selectedRoles: [],
        selectedTypes: []
      },
      oTempFilterProps: {
        attributeList: [],
        attributes: {},
        typeList: [],
        selectedAttributes: [],
        tagList: [],
        selectedTags: [],
        allSearch: '',
        isActive: false,
        selectedRoles: [],
        selectedTypes: [],
        isDirty: false
      },
      oTempAvailableEntityFilterProps: {
        attributeList: [],
        attributes: {},
        typeList: [],
        selectedAttributes: [],
        tagList: [],
        selectedTags: [],
        allSearch: '',
        isActive: false,
        sortField: "createdOn",
        sortOrder: "asc",
        selectedRoles: [],
        selectedTypes: [],
        isDirty: false
      },
      oContentAvailableEntityFilterProps: {
        attributeList: [],
        typeList: [],
        attributes: {},
        selectedAttributes: [],
        tagList: [],
        selectedTags: [],
        allSearch: '',
        isActive: false,
        sortField: "relevance",
        sortOrder: "asc",
        selectedRoles: [],
        selectedTypes: []
      },
      sActiveSortingField: SortFieldDictionary.CREATED_ON,
      sActiveSortingOrder: SortFieldDictionary.DESC,
      bIsUpload: false,
      currentZoom: ZoomSettings.defaultZoom,
      setSectionSelectionStatus: {
        linkedElementSelectionStatus: false,
        childrenContainerSelectionStatus: false,
        relationshipContainerSelectionStatus : false,
        selectedRelationship :{}
      },
      oRuleViolation : {},
      dataRuleFilter: {
        all: true,
        red: true,
        orange: true,
        yellow: true,
        green: true
      },
      sViewMode: ContentScreenConstants.viewModes.TILE_MODE,
      sDefaultViewMode: ContentScreenConstants.viewModes.TILE_MODE,
      oContextData: {},
      iTotalContents: 0,
      sThumbnailMode: ContentScreenConstants.thumbnailModes.BASIC,
      oAddEntityInRelationshipScreenData: {},
      iSectionInnerZoom: ZoomSettings.defaultZoom,
      sSectionInnerThumbnailMode: ContentScreenConstants.thumbnailModes.BASIC,
      iTotalAvailableEntitiesCount: 0,
      iAvailableEntitiesPaginationIndex: 0,
      isShakingEnable: false,

      sEntitySearchText: "",
      oDefaultEntitiesPaginationData: {
        attributes: {from: 0, size: 20, sortBy: "label", sortOrder: "asc", types: [], typesToExclude: []},
        tags: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        roles: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        relationships: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        propertyCollections: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        taxonomies: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"}
      },
      oEntitiesPaginationData: {},
      oEntitiesTotalCounts: {},


      oLoadedAttributes: {},
      oLoadedTags: {},
      oLoadedRoles: {},

      aXRayAttributeList: [],
      aXRayTagList: [],
      aXRayPropertyGroupList: [],
      oActiveXRayPropertyGroup: {},
      oActiveInnerXRayPropertyGroup: {},

      oBulkUploadProps: {},
      bBulkUploadCollectionView: false,
      bIsOnboardingFileMappingView: false,
      aReferencedAssetList: [],

      bVariantImageUploadDialogStatus: false,
      bIsFilterAndSearchViewDisabled: false,

      oActiveEntitySelectedTabIdMap: {},
      aActiveNatureSections: [],
      aActiveVariantSections: [],
      aSelectedContextVariantSections: [],

      aNatureRelationshipIds: [],
      sVariantRelationshipId: "",
      aArticleNatureKlassIds: [],
      aAllowedTaxonomies: [],
      oAllowedTaxonomyConfigDetails: {},
      oAllowedTaxonomyHierarchyList: {},
      aReferencedLifeCycleTags:[],
      aReferencedStatusTags:[],
      oReferencedRelationshipMapping:{},
      oReferencedContents:{},
      oReferencedRelationshipProperties:{},
      oRelationshipContextData: {},
      oReferencedVariantContexts: {},
      bEntityHeaderLabelInEditMode: false,
      oContextualPriceInstanceInfo: {},
      sBranchOfLabel: "",
      aAssetList: [],
      aAllModuleList: [],
      oReferencedPermissions: {},

      modifiedRelationshipsContextTempData: {},
      oContentHierarchyTree:{},
      oContentHierarchyVisualProps: {},
      sSelectedOuterParentContentHierarchyTaxonomyId: "",
      bIsTaxonomyHierarchySelected: false,
      bIsCollectionHierarchySelected: false,
      oActiveHierarchyCollection: {},
      sHierarchyDetailViewMode: "thumbnailViewMode",
      iHierarchyNodeLevel: 0,
      oDefaultPaginationData: {
        from: 0,
        size: 20,
        searchText: ""
      },
      oHierarchyPaginationDataByLevels: {},
      sActiveHierarchyNodeId: "",
      oHierarchyEntitiesToCopyOrCutData: {},
      oClickedThumbnailModel: {},
      oHierarchyTreeRightClickedNodeData:{},
      bIsFilterHierarchySelected: false,
      aFilterHierarchySelectedFilters: [],
      bIsScrollAutomaticallyEnabled: false,
      bIsVersionMatchMergeDialogOpen: false,
      sVariantTagSummaryEditClickedVariantId: "",
      aReferencedCollections: [],
      oVersionInstance: {},
      bIsMasterCollectionListOrganiseClicked: false,
      sSelectedUnitName: "",
      bIsContentHeaderCollapsed: false,
      bIsBulkEditViewOpen: false,
      oLayoutData: {},
      aMasterKlassInstanceListForComparison: [],
      bIsChooseTaxonomyVisible: true,
      aPropertyCollectionsList: [],
      aEndpoints : [],
      bExportContentsPopoverVisibility: false,
      bImportContentsPopoverVisibility:false,
      bImportEndpointSelected:false,
      sSelectedImportEndpointCode:"",
      oSelectedTemplate: {},
      iMultipleTaxonomyHierarchyTypeChangeCallCounter: 0,
      aKpiListForSummary: [],
      bIsDashboardModuleSelected: true,
      processedContentKPIDataMap: {},
      activeKPIObject : {},
      selectedKPIChartType : "doughnut",
      bIsKpiContentExplorerOpen: false,
      oKpiContentExplorerData: {},
      bPreventDashboardDataReset: false,
      oConflictingSources: {},
      oAllPossibleConflictingSources: {},
      oContentElementsHavingConflictingValues: {},
      sSelectedImageId: "",
      bIsActiveClassGoldenRecord: false,
      sSelectedDashboardTabId: "",
      aDirtyTableContextIds: [],
      attributeVariantsStats: {},
      isAcrolinxSidebarVisible: false,
      oGridViewSelectedContentBySectionIds: {},
      quicklistDragNDropState: false,
      oReferencedLanguages: {},
      bIsRelationshipContextDialogOpen: false,
      sSelectedLanguageForComparison: "",
      oEntitiesSelectorData: {
        entitiesList : [],
        selectedItemsList: [],
        isMultiSelect: false
      },
      showHelperLanguagePopover : false,
      helperLanguageInstances: [],
      bIsDataLanguagePopoverVisible: false,
      bIsCreateTranslationPopoverVisible: false,
      aImageVariantContext: [],
      oSelectedImageVariantContext: {},
      bIsHelperLanguageDisabled: false,
      bSmartDocumentPresetsPopoverVisibility: false,
      bIsGenerateSmartDocumentButtonVisible: false,
      isVersionableAttributeValueChanged: false,
      bIsSwitchDataLanguageDisabled: false,
      commonReferencedRelationships:{},
      oAssetExtensions: {
        imageAsset:[],
        videoAsset:[],
        documentAsset:[],
        allAssets:[]
      },
      bIsExportDialogOpened:false,
      aOrganizationList:[],
      aAuthorizationMappingList:[],
      bIsAuthMappingDisabled:true,
      selectedOrganizationId:"",
      selectedPhysicalCatlogId:"",
      selectedEndpointId: null,
      aInboundEndpointList: [],
      aOutboundEndpointList: [],
      bIsTransferBetweenStagesEnabled: false,
      selectedAuthorizationMappingId:"",
      bShowClassificationDialog: false,
      bIsContentComparisonMode: false,
      bIsHierarchyMode: false,
      bIsEditMode: false,
      sAssetSharedUrl: "",
      bIsKlassInstanceFlatPropertyUpdate: false,
      sDefaultTypesSearchText: "",
      oFunctionPermission: {},
      isRevisionableTransfer: false,
      activeEmbProperty: {}
    }
  };

  var Properties = new Props();

  return {
    getSelectedLanguageForComparison: function () {
      return Properties.sSelectedLanguageForComparison;
    },

    setSelectedLanguageForComparison: function (_sSelectedLanguageForComparison) {
      Properties.sSelectedLanguageForComparison = _sSelectedLanguageForComparison;
    },

    setPreviouslySelectedModuleId: function (_sPreviouslySelectedModuleId) {
      Properties.sPreviouslySelectedModuleId = _sPreviouslySelectedModuleId;
    },

    getMasterKlassInstanceListForComparison: function () {
      return Properties.aMasterKlassInstanceListForComparison;
    },

    setMasterKlassInstanceListForComparison: function (_aMasterKlassInstanceListForComparison) {
      Properties.aMasterKlassInstanceListForComparison = _aMasterKlassInstanceListForComparison;
    },

    getContentComparisonLayoutData: function () {
      return Properties.oLayoutData;
    },

    setContentComparisonLayoutData: function (_oLayoutData) {
      Properties.oLayoutData = _oLayoutData;
    },

    setEntityHeaderLabelInEditMode: function (bFlag) {
      Properties.bEntityHeaderLabelInEditMode = bFlag;
    },

    getEntityHeaderLabelInEditMode: function () {
      return Properties.bEntityHeaderLabelInEditMode;
    },

    getBranchOfLabel: function () {
      return Properties.sBranchOfLabel;
    },

    setBranchOfLabel: function (sBranchOfLabel) {
      return Properties.sBranchOfLabel = sBranchOfLabel;
    },

    getCurrentZoom: function(){
      return Properties.currentZoom;
    },

    setCurrentZoom: function(sZoom){
      Properties.currentZoom = sZoom;
    },

    getTempAvailableEntityFilterProps: function(){
      return Properties.oTempAvailableEntityFilterProps;
    },

    getContentFilterProps: function(){
      return Properties.oContentFilterProps;
    },

    getContentAvailableEntityFilterProps: function(){
      return Properties.oContentAvailableEntityFilterProps;
    },

    setAttributeListForFiltering: function (oAttributes) {
      Properties.oTempFilterProps.attributeList = [];
      Properties.oTempFilterProps.attributes = oAttributes;
      Properties.oTempAvailableEntityFilterProps.attributeList = [];
      Properties.oTempAvailableEntityFilterProps.attributes = oAttributes;
    },

    getIsSortActive: function () {
      return Properties.bIsSortActive;
    },

    getActiveSortingField : function (){
      return Properties.sActiveSortingField;
    },

    getActiveSortingOrder : function (){
      return Properties.sActiveSortingOrder;
    },

    getSelectedContentList: function () {
      return Properties.aSelectedContents;
    },

    setSelectedContentList: function (_aSelectedContentList) {
      Properties.aSelectedContents = _aSelectedContentList;
    },

    getSelectedContentIds: function () {
      return Properties.aSelectedContentIds;
    },

    setSelectedContentIds: function (_aSelectedContentIds) {
      Properties.aSelectedContentIds = _aSelectedContentIds;
    },

    setSelectedSetList: function (_aSelectedSetList) {
      Properties.aSelectedSetList = _aSelectedSetList;
    },

    setSelectedCollectionContentList: function (_aSelectedContentContentList) {
      Properties.aSelectedContentContentList = _aSelectedContentContentList;
    },

    getActiveContent: function () {
      return Properties.oActiveContent;
    },

    getActiveContentClass: function () {
      return Properties.oActiveContentClass;
    },

    getActiveClassIds: function () {
      return Properties.aActiveClassIds;
    },

    getActiveClonedClassIds: function () {
      return Properties.aActiveClonedClassIds;
    },

    getActiveTaxonomyIds: function () {
      return Properties.aActiveTaxonomyIds;
    },

    getActiveClonedTaxonomyIds: function () {
      return Properties.aActiveClonedTaxonomyIds;
    },

    getActiveSections: function () {
      return Properties.aActiveSections;
    },

    getConflictingValues: function () {
      return Properties.oConflictingValues;
    },

    getReferencedClasses: function () {
      return Properties.oReferencedClasses;
    },

    getReferencedTemplate: function () {
      return Properties.oReferencedTemplate;
    },

    getReferencedTemplates: function () {
      return Properties.oReferencedTemplates;
    },

    getReferencedPropertyCollections: function () {
      return Properties.oReferencedPropertyCollections;
    },

    getReferencedTaxonomies: function () {
      return Properties.oReferencedTaxonomies;
    },

    getReferencedElements: function () {
      return Properties.oReferencedElements;
    },

    getReferencedAttributes: function () {
      return Properties.oReferencedAttributes;
    },

    getReferencedTags: function () {
      return Properties.oReferencedTags;
    },

    getReferencedRoles: function () {
      return Properties.oReferencedRoles;
    },

    getReferencedRelationships: function () {
      return Properties.oReferencedRelationships;
    },

    setCommonReferencedRelationships: function (oCommonReferencedRelationships) {
      Properties.commonReferencedRelationships = oCommonReferencedRelationships;
    },

    getCommonReferencedRelationships: function () {
      return Properties.commonReferencedRelationships;
    },

    getReferencedVariantContexts: function () {
      return Properties.aReferencedVariantContexts;
    },

    setEntitySearchText: function (_sEntitySearchText) {
      Properties.sEntitySearchText = _sEntitySearchText;
    },

    getEntitySearchText : function () {
      return Properties.sEntitySearchText;
    },

    getEntitiesPaginationData : function () {
      if (CS.isEmpty(Properties.oEntitiesPaginationData)) {
        this.resetEntitiesPaginationData();
      }
      return Properties.oEntitiesPaginationData;
    },

    resetEntitiesPaginationData : function () {
      Properties.sEntitySearchText = "";
      Properties.oEntitiesPaginationData = CS.cloneDeep(Properties.oDefaultEntitiesPaginationData);
    },

    getEntitiesTotalCounts: function () {
      return Properties.oEntitiesTotalCounts;
    },

    getLoadedAttributes: function () {
      return Properties.oLoadedAttributes;
    },

    getLoadedTags: function () {
      return Properties.oLoadedTags;
    },

    getLoadedRoles: function () {
      return Properties.oLoadedRoles;
    },

    getContentScreenMode: function () {
      return Properties.sContentScreenMode;
    },

    getRelationshipViewMode: function () {
      return Properties.oRelationshipViewMode;
    },

    getPaginatedIndex: function () {
      return Properties.iPaginatedIndex;
    },

    getItemSelectionMode: function () {
      return Properties.sItemSelectionMode;
    },

    getDefaultPaginationLimit: function () {
      return Properties.iDefaultPaginationLimit;
    },

    setPaginationLimit: function (iPaginationLimit) {
      Properties.iPreviousDefaultPaginationLimit = Properties.iDefaultPaginationLimit;
      Properties.iDefaultPaginationLimit = iPaginationLimit;
    },

    getDefaultPaginationInnerLimit: function () {
      return Properties.iDefaultPaginationInnerLimit;
    },

    setPaginationInnerLimit: function (iPaginationInnerLimit) {
      Properties.iDefaultPaginationInnerLimit = iPaginationInnerLimit;
    },

    emptySelectedContentList: function () {
      Properties.aSelectedContents = [];
    },

    setActiveContent: function (_oActiveContent) {
      Properties.oActiveContent = _oActiveContent;
    },

    setActiveContentClass: function (_oActiveContentClass) {
      Properties.oActiveContentClass = _oActiveContentClass;
    },

    setActiveClassIds: function (_aActiveClassIds) {
      Properties.aActiveClassIds = _aActiveClassIds;
    },

    setActiveTaxonomyIds: function (_aActiveTaxonomyIds) {
      Properties.aActiveTaxonomyIds = _aActiveTaxonomyIds;
    },

    setActiveSections: function (_aActiveSections) {
      Properties.aActiveSections = _aActiveSections;
    },

    setConflictingValues: function (_oConflictingValues) {
      Properties.oConflictingValues = _oConflictingValues;
    },

    setReferencedClasses: function (_oReferencedClasses) {
      Properties.oReferencedClasses = _oReferencedClasses;
    },

    setReferencedTemplate: function (_oReferencedTemplate) {
      Properties.oReferencedTemplate = _oReferencedTemplate;
    },

    setReferencedTemplates: function (_oReferencedTemplates) {
      Properties.oReferencedTemplates = _oReferencedTemplates;
    },

    setReferencedPropertyCollections: function (_oReferencedPropertyCollections) {
      Properties.oReferencedPropertyCollections = _oReferencedPropertyCollections;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      Properties.oReferencedTaxonomies = _oReferencedTaxonomies;
    },

    setReferencedElements: function (_oReferencedElements) {
      Properties.oReferencedElements = _oReferencedElements;
    },

    setReferencedAttributes: function (_oReferencedAttributes) {
      Properties.oReferencedAttributes = _oReferencedAttributes;
    },

    setReferencedTags: function (_oReferencedTags) {
      Properties.oReferencedTags = _oReferencedTags;
    },

    setReferencedRoles: function (_oReferencedRoles) {
      Properties.oReferencedRoles = _oReferencedRoles;
    },

    setReferencedRelationships: function (_oReferencedRelationships) {
      Properties.oReferencedRelationships = _oReferencedRelationships;
    },

    setReferencedVariantContexts: function (_aReferencedVariantContexts) {
      Properties.aReferencedVariantContexts = _aReferencedVariantContexts;
    },

    setContentScreenMode: function (sScreenMode) {
      Properties.sPreviousScreenMode = Properties.sContentScreenMode;
      Properties.sContentScreenMode = sScreenMode;
    },

    setRelationshipViewMode: function (_sId, _sRelationshipViewMode) {
      Properties.oRelationshipViewMode[_sId] = _sRelationshipViewMode;
    },

    setPaginatedIndex: function (_index) {
      Properties.iPaginatedIndex = _index;
    },

    setItemSelectionMode: function (sSelectionMode) {
      Properties.sItemSelectionMode = sSelectionMode;
    },

    getSelectedContext: function () {
      return Properties.oSelectedContext;
    },

    resetSelectedContext: function () {
      Properties.oSelectedContext.context = '';
      Properties.oSelectedContext.elementId = '';
      Properties.oSelectedContext.structureId = '';
      Properties.oSelectedContext.masterEntityId = '';
      Properties.oSelectedContext.attributeType = null;
      Properties.oSelectedContext.sectionId = '';
    },

    isContentFilterPropsActive: function () {
      return !(Properties.oContentFilterProps.allSearch == "" && CS.isEmpty(Properties.oContentFilterProps.selectedAttributes)
          && CS.isEmpty(Properties.oContentFilterProps.selectedTags) && CS.isEmpty(Properties.oContentFilterProps.selectedRoles)
          && CS.isEmpty(Properties.oContentFilterProps.selectedTypes));
    },

    applyFilterProps: function () {
      Properties.oContentFilterProps = CS.cloneDeep(Properties.oTempFilterProps);
    },

    applyAdvanceFilterProps: function () {
      Properties.oContentAvailableEntityFilterProps = CS.cloneDeep(Properties.oTempAvailableEntityFilterProps);
    },

    setIsUpload: function (_bIsUpload) {
      Properties.bIsUpload = _bIsUpload;
    },

    getSetSectionSelectionStatus: function () {
      return Properties.setSectionSelectionStatus;
    },

    emptySetSectionSelectionStatus: function () {
      var oSetSectionSelectionStatus = Properties.setSectionSelectionStatus;
      oSetSectionSelectionStatus.linkedElementSelectionStatus = false;
      oSetSectionSelectionStatus.childrenContainerSelectionStatus = false;
      oSetSectionSelectionStatus.relationshipContainerSelectionStatus = false;
      oSetSectionSelectionStatus.selectedRelationship = {};
    },

    getRuleViolationObject : function () {
      return Properties.oRuleViolation;
    },

    emptyRuleViolationObject : function () {
      Properties.oRuleViolation = {};
    },

    getDataRuleFilterProps: function () {
      return Properties.dataRuleFilter;
    },

    resetDataRuleFilterProps: function () {
      Properties.dataRuleFilter = {
        all: true,
        red: true,
        orange: true,
        yellow: true,
        green: true
      };
    },

    setViewMode: function (sViewMode) {
      if(sViewMode != Properties.sViewMode) {
        Properties.sViewMode = sViewMode;
      }
    },

    getViewMode: function () {
      return Properties.sViewMode;
    },

    setDefaultViewMode: function (sDefaultViewMode) {
      Properties.sDefaultViewMode = sDefaultViewMode;
    },

    getDefaultViewMode: function () {
      return Properties.sDefaultViewMode;
    },

    getContextData: function () {
      return Properties.oContextData;
    },

    getThumbnailMode: function () {
      return Properties.sThumbnailMode;
    },

    setThumbnailMode: function (sThumbnailMode) {
      Properties.sThumbnailMode = sThumbnailMode;
    },

    getAddEntityInRelationshipScreenData: function () {
      return Properties.oAddEntityInRelationshipScreenData;
    },

    getSectionInnerZoom: function () {
      return Properties.iSectionInnerZoom;
    },

    setSectionInnerZoom: function (iZoom) {
      Properties.iSectionInnerZoom = iZoom;
    },

    getSectionInnerThumbnailMode: function () {
      return Properties.sSectionInnerThumbnailMode;
    },

    setSectionInnerThumbnailMode: function (sMode) {
      Properties.sSectionInnerThumbnailMode = sMode;
    },

    getShakingStatus: function () {
      return Properties.isShakingEnable;
    },

    setShakingStatus: function (_isShakingEnable) {
      Properties.isShakingEnable = _isShakingEnable;
    },

    getXRayPropertyGroupList: function () {
      return Properties.aXRayPropertyGroupList;
    },

    setXRayPropertyGroupList: function (_aXRayPropertyGroupList) {
      Properties.aXRayPropertyGroupList = _aXRayPropertyGroupList;
    },

    getActiveXRayPropertyGroup: function () {
      return Properties.oActiveXRayPropertyGroup;
    },

    setActiveXRayPropertyGroup: function (_oActiveXRayPropertyGroup) {
      Properties.oActiveXRayPropertyGroup = _oActiveXRayPropertyGroup;
    },

    getInnerActiveXRayPropertyGroup: function () {
      return Properties.oActiveInnerXRayPropertyGroup;
    },

    setInnerActiveXRayPropertyGroup: function (_oActiveXRayPropertyGroup) {
      Properties.oActiveInnerXRayPropertyGroup = _oActiveXRayPropertyGroup;
    },

    getBulkUploadProps: function () {
      return Properties.oBulkUploadProps;
    },

    setBulkUploadProps: function (_oBulkUploadProps) {
      Properties.oBulkUploadProps = _oBulkUploadProps;
    },

    getBulkUploadCollectionView: function () {
      return Properties.bBulkUploadCollectionView;
    },

    setBulkUploadCollectionView: function (_bBulkUploadCollectionView) {
      Properties.bBulkUploadCollectionView = _bBulkUploadCollectionView;
    },

    getOnboardingFileMappingViewVisibilityStatus: function () {
      return Properties.bIsOnboardingFileMappingView;
    },

    setOnboardingFileMappingViewVisibilityStatus: function (_bIsOnboardingFileMappingView) {
      Properties.bIsOnboardingFileMappingView = _bIsOnboardingFileMappingView;
    },

    //TODO: Refactor referenced asset list from array to object
    getReferencedAssetList: function () {
      return Properties.aReferencedAssetList;
    },

    setReferencedAssetList: function (_aReferencedAssetList) {
      Properties.aReferencedAssetList = _aReferencedAssetList;
    },

    getVariantUploadImageDialogStatus: function () {
      return Properties.bVariantImageUploadDialogStatus;
    },

    setVariantUploadImageDialogStatus: function (bVal) {
      Properties.bVariantImageUploadDialogStatus = bVal;
    },

    getFilterAndSearchViewDisabledStatus: function () {
      return Properties.bIsFilterAndSearchViewDisabled;
    },

    getActiveEntitySelectedTabIdMap: function () {
      return Properties.oActiveEntitySelectedTabIdMap;
    },

    getReferencedNatureRelationships: function () {
      return Properties.oReferencedNatureRelationships;
    },

    setReferencedNatureRelationships: function (_oReferencedNatureRelationships) {
      Properties.oReferencedNatureRelationships = _oReferencedNatureRelationships;
    },

    getVariantsCount: function () {
      return Properties.oVariantsCount;
    },

    setVariantsCount: function (_oVariantsCount) {
      Properties.oVariantsCount = _oVariantsCount;
    },

    getSelectedTemplateId: function () {
      // return Properties.sSelectedTemplateId;
      return Properties.oSelectedTemplate.id;
    },

    getSelectedTemplate: function () {
      return Properties.oSelectedTemplate;
    },

    setSelectedTemplate: function (_oSelectedTemplate) {
      Properties.oSelectedTemplate = _oSelectedTemplate;
    },

    getActiveNatureSections: function (){
      return Properties.aActiveNatureSections;
    },

    setActiveNatureSections: function (_aSections){
      Properties.aActiveNatureSections = _aSections;
    },

    getNatureRelationshipIds: function (){
      return Properties.aNatureRelationshipIds;
    },

    setNatureRelationshipIds: function (_aNatureRelationshipIds){
      Properties.aNatureRelationshipIds = _aNatureRelationshipIds;
    },

    getVariantRelationshipId: function (){
      return Properties.sVariantRelationshipId;
    },

    getActiveVariantSections: function (){
      return Properties.aActiveVariantSections;
    },

    getSelectedContextVariantSections: function (){
      return Properties.aSelectedContextVariantSections;
    },

    setSelectedContextVariantSections: function (_aSections){
      Properties.aSelectedContextVariantSections = _aSections;
    },

    getArticleNatureKlassIds: function () {
      return Properties.aArticleNatureKlassIds;
    },

    setArticleNatureKlassIds: function (_aIds) {
      Properties.aArticleNatureKlassIds = _aIds;
    },

    getAllowedTaxonomies: function () {
      return Properties.aAllowedTaxonomies;
    },

    setAllowedTaxonomies: function (_aAllowedTaxonomies) {
      Properties.aAllowedTaxonomies = _aAllowedTaxonomies;
    },

    getAllowedTaxonomyConfigDetails: function () {
      return Properties.oAllowedTaxonomyConfigDetails;
    },

    getAllowedTaxonomyHierarchyList: function () {
      return Properties.oAllowedTaxonomyHierarchyList;
    },

    setAllowedTaxonomyHierarchyList: function (_oAllowedTaxonomyHierarchyList) {
      Properties.oAllowedTaxonomyHierarchyList = _oAllowedTaxonomyHierarchyList;
    },

    getAllowedTaxonomiesPaginationData: function () {
      if (CS.isEmpty(Properties.oActiveTaxonomyPaginationData)) {
        Properties.oActiveTaxonomyPaginationData = CS.cloneDeep(Properties.oDefaultActiveTaxonomyPaginationData);
      }
      return Properties.oActiveTaxonomyPaginationData;
    },

    resetActiveTaxonomyPaginationData: function () {
      Properties.oActiveTaxonomyPaginationData = CS.cloneDeep(Properties.oDefaultActiveTaxonomyPaginationData);
    },

    getReferencedLifeCycleTags: function (){
      return Properties.aReferencedLifeCycleTags;
    },

    setReferencedLifeCycleTags: function (aTagIds){
      Properties.aReferencedLifeCycleTags = aTagIds;
    },

    getReferencedStatusTags: function (){
      return Properties.aReferencedStatusTags;
    },

    setReferencedStatusTags: function (aTagIds){
      Properties.aReferencedStatusTags = aTagIds;
    },

    getReferencedRelationshipsMapping: function () {
      return Properties.oReferencedRelationshipMapping;
    },

    setReferencedRelationshipsMapping: function (oMap) {
      Properties.oReferencedRelationshipMapping = oMap;
    },

    getReferencedContents: function () {
      return Properties.oReferencedContents;
    },

    setReferencedContents: function (oMap) {
      Properties.oReferencedContents = oMap;
    },

    getReferencedRelationshipProperties: function () {
      return Properties.oReferencedRelationshipProperties;
    },

    setReferencedRelationshipProperties: function (oMap) {
      Properties.oReferencedRelationshipProperties = oMap;
    },

    setContextualPriceInfo: function (_oContextualPriceInstanceInfo) {
      Properties.oContextualPriceInstanceInfo = _oContextualPriceInstanceInfo;
    },

    setReferencedProductVariantContexts: function (_oReferencedVariantContexts) {
      Properties.oReferencedVariantContexts = _oReferencedVariantContexts;
    },

    getRelationshipContextData: function () {
      return  Properties.oRelationshipContextData;
    },

    setRelationshipContextData: function (_oRelationshipContextData) {
      Properties.oRelationshipContextData = _oRelationshipContextData;
    },

    getAssetList: function () {
      return  Properties.aAssetList;
    },

    setAssetList: function (_oAssetList) {
      Properties.aAssetList = _oAssetList;
    },

    getAllModuleList: function () {
      return  Properties.aAllModuleList;
    },

    setAllModuleList: function (_aAllModuleList) {
      Properties.aAllModuleList = _aAllModuleList;
    },

    getReferencedPermissions: function () {
      return Properties.oReferencedPermissions;
    },

    setReferencedPermissions: function (_oReferencedPermissions) {
      Properties.oReferencedPermissions = _oReferencedPermissions;
    },

    getVariantTagSummaryEditViewStatus: function () {
      return Properties.bVariantTagSummaryEditViewOpen;
    },

    setVariantTagSummaryEditViewStatus: function (bVariantTagSummaryEditViewOpen) {
      Properties.bVariantTagSummaryEditViewOpen = bVariantTagSummaryEditViewOpen;
    },

    getModifiedRelationshipsContextTempData: function () {
      return Properties.modifiedRelationshipsContextTempData;
    },

    setModifiedRelationshipsContextTempData: function (oModifiedRelationshipsContextTempData) {
      Properties.modifiedRelationshipsContextTempData = oModifiedRelationshipsContextTempData;
    },

    getContentHierarchyTree: function () {
      return Properties.oContentHierarchyTree;
    },

    setContentHierarchyTree: function (oTree) {
      Properties.oContentHierarchyTree = oTree;
    },

    getContentHierarchyVisualProps: function () {
      return Properties.oContentHierarchyVisualProps;
    },

    setContentHierarchyVisualProps: function (oVisualProps) {
      Properties.oContentHierarchyVisualProps = oVisualProps
    },

    getSelectedOuterParentContentHierarchyTaxonomyId: function () {
      return Properties.sSelectedOuterParentContentHierarchyTaxonomyId;
    },

    setSelectedOuterParentContentHierarchyTaxonomyId: function (sId) {
      Properties.sSelectedOuterParentContentHierarchyTaxonomyId = sId;
    },

    getIsTaxonomyHierarchySelected: function () {
      return Properties.bIsTaxonomyHierarchySelected;
    },

    setIsTaxonomyHierarchySelected: function (bVal) {
      Properties.bIsTaxonomyHierarchySelected = bVal;
    },

    getIsCollectionHierarchySelected: function () {
      return Properties.bIsCollectionHierarchySelected;
    },

    setIsCollectionHierarchySelected: function (bVal) {
      Properties.bIsCollectionHierarchySelected = bVal;
    },

    getActiveHierarchyCollection: function () {
      return Properties.oActiveHierarchyCollection;
    },

    setActiveHierarchyCollection: function (oCollection) {
      Properties.oActiveHierarchyCollection = oCollection;
    },

    getHierarchyDetailViewMode: function () {
      return Properties.sHierarchyDetailViewMode;
    },

    setHierarchyDetailViewMode: function (sViewMode) {
      Properties.sHierarchyDetailViewMode = sViewMode;
    },

    getActiveHierarchyNodeLevel: function () {
      return Properties.iHierarchyNodeLevel;
    },

    setActiveHierarchyNodeLevel: function (iLevel) {
      Properties.iHierarchyNodeLevel = iLevel;
    },

    getActiveHierarchyNodeId: function () {
      return Properties.sActiveHierarchyNodeId;
    },

    setActiveHierarchyNodeId: function (sId) {
      Properties.sActiveHierarchyNodeId = sId;
    },

    getHierarchyEntitiesToCopyOrCutData: function () {
      return Properties.oHierarchyEntitiesToCopyOrCutData;
    },

    setHierarchyEntitiesToCopyOrCutData: function (oData) {
      Properties.oHierarchyEntitiesToCopyOrCutData = oData;
    },

    setRightClickedThumbnailModel: function (oModel) {
      Properties.oClickedThumbnailModel = oModel;
    },

    getRightClickedThumbnailModel: function () {
      return Properties.oClickedThumbnailModel;
    },

    setRightClickedHierarchyTreeNodeData: function (oReqData) {
      Properties.oHierarchyTreeRightClickedNodeData = oReqData;
    },

    getRightClickedHierarchyTreeNodeData: function () {
      return Properties.oHierarchyTreeRightClickedNodeData;
    },

    setIsFilterHierarchySelected: function (bVal) {
      Properties.bIsFilterHierarchySelected = bVal;
    },

    getIsFilterHierarchySelected: function () {
      return Properties.bIsFilterHierarchySelected;
    },

    setFilterHierarchySelectedFilters: function (aList) {
      Properties.aFilterHierarchySelectedFilters = aList;
    },

    getFilterHierarchySelectedFilters: function () {
      return Properties.aFilterHierarchySelectedFilters;
    },

    setIsHierarchyTreeScrollAutomaticallyEnabled: function (bVal) {
      Properties.bIsScrollAutomaticallyEnabled = bVal;
    },

    getIsHierarchyTreeScrollAutomaticallyEnabled: function () {
      return Properties.bIsScrollAutomaticallyEnabled;
    },

    getIsVersionMatchMergeDialogOpen: function () {
      return Properties.bIsVersionMatchMergeDialogOpen;
    },

    setVariantTagSummaryEditClickedVariantId: function (sId) {
      Properties.sVariantTagSummaryEditClickedVariantId = sId;
    },

    getVariantTagSummaryEditClickedVariantId: function () {
      return Properties.sVariantTagSummaryEditClickedVariantId;
    },

    setReferencedCollections: function (aList) {
      Properties.aReferencedCollections = aList;
    },

    getReferencedCollections: function () {
      return Properties.aReferencedCollections;
    },

    getIsMasterCollectionListOrganiseClicked: function () {
      return Properties.bIsMasterCollectionListOrganiseClicked;
    },

    setIsMasterCollectionListOrganiseClicked: function (bVal) {
      Properties.bIsMasterCollectionListOrganiseClicked = bVal;
    },

    getIsBulkEditViewOpen: function () {
      return Properties.bIsBulkEditViewOpen;
    },

    setIsContentHeaderCollapsed: function (bVal) {
      Properties.bIsContentHeaderCollapsed = bVal;
    },

    getIsChooseTaxonomyVisible: function () {
      return Properties.bIsChooseTaxonomyVisible;
    },

    setIsChooseTaxonomyVisible: function (bVal) {
      Properties.bIsChooseTaxonomyVisible = bVal;
    },

    getPropertyCollectionsList: function () {
      return Properties.aPropertyCollectionsList;
    },

    setPropertyCollectionsList: function (_aPropertyCollectionsList) {
      Properties.aPropertyCollectionsList = _aPropertyCollectionsList;
    },

    getEndpointsListAccordingToUser: function () {
      return Properties.aEndpoints;
    },

    getExportContentsPopoverVisibility: function () {
      return Properties.bExportContentsPopoverVisibility;
    },

    setExportContentsPopoverVisibility: function (_bExportContentsPopoverVisibility) {
      Properties.bExportContentsPopoverVisibility = _bExportContentsPopoverVisibility;
    },

    getImportContentsPopoverVisibility: function () {
      return Properties.bImportContentsPopoverVisibility;
    },

    setImportContentsPopoverVisibility: function (_bImportContentsPopoverVisibility) {
      Properties.bImportContentsPopoverVisibility = _bImportContentsPopoverVisibility;
    },

    getImportEndpointSelected: function () {
      return Properties.bImportEndpointSelected;
    },

    setImportEndpointSelected: function (_bImportEndpointSelected) {
      Properties.bImportEndpointSelected = _bImportEndpointSelected;
    },

    getSelectedImportEndpointCode: function () {
      return Properties.sSelectedImportEndpointCode;
    },

    setSelectedImportEndpointCode: function (_sSelectedImportEndpointCode) {
      Properties.sSelectedImportEndpointCode = _sSelectedImportEndpointCode;
    },

    getExportDialogViewOpened: function () {
      return Properties.bIsExportDialogOpened;
    },

    setExportDialogViewOpened: function (_bIsExportDialogOpened) {
      Properties.bIsExportDialogOpened = _bIsExportDialogOpened;
    },

    getOrganizationListForExportDialog: function () {
      return Properties.aOrganizationList;
    },

    setOrganizationListForExportDialog: function (_aEndpointList) {
      Properties.aOrganizationList = _aEndpointList;
    },

    getAuthorizationMappingListForExportDialog: function () {
      return Properties.aAuthorizationMappingList;
    },

    setAuthorizationMappingListForExportDialog: function (_aAuthorizationMappingList) {
      Properties.aAuthorizationMappingList = _aAuthorizationMappingList;
    },

    getIsAuthMappingDisabled: function () {
      return Properties.bIsAuthMappingDisabled;
    },

    setIsAuthMappingDisabled: function (_bIsAuthMappingDisabled) {
      Properties.bIsAuthMappingDisabled = _bIsAuthMappingDisabled;
    },

    getSelectedOrganizationId: function () {
      return Properties.selectedOrganizationId;
    },

    setSelectedOrganizationId: function (_sSelectedOrganizationId) {
      Properties.selectedOrganizationId = _sSelectedOrganizationId;
    },

    getSelectedPhysicalCatlogId: function () {
      return Properties.selectedPhysicalCatlogId;
    },

    setSelectedPhysicalCatlogId: function (_sSelectedPhysicalCatlogId) {
      Properties.selectedPhysicalCatlogId = _sSelectedPhysicalCatlogId;
    },

    getselectedEndpointId: function () {
      return Properties.selectedEndpointId;
    },

    setselectedEndpointId: function (_sSelectedEndpointId) {
      Properties.selectedEndpointId = _sSelectedEndpointId;
    },

    getInboundEndpointListForExportDialog: function () {
      return Properties.aInboundEndpointList;
    },

    setInboundEndpointListForExportDialog: function (_aInboundEndpointList) {
      Properties.aInboundEndpointList = _aInboundEndpointList;
    },

    getOutboundEndpointListForExportDialog: function () {
      return Properties.aOutboundEndpointList;
    },

    setOutboundEndpointListForExportDialog: function (_aOutboundEndpointList) {
      Properties.aOutboundEndpointList = _aOutboundEndpointList;
    },

    getIsTransferBetweenStagesEnabled: function () {
      return Properties.bIsTransferBetweenStagesEnabled;
    },

    setIsTransferBetweenStagesEnabled: function (_bIsTransferBetweenStagesEnabled) {
      Properties.bIsTransferBetweenStagesEnabled = _bIsTransferBetweenStagesEnabled;
    },

    getSelectedAuthorizationMappingId: function () {
      return Properties.selectedAuthorizationMappingId;
    },

    setSelectedAuthorizationMappingId: function (_sSelectedAuthorizationMappingId) {
      Properties.selectedAuthorizationMappingId = _sSelectedAuthorizationMappingId;
    },

    getSmartDocumentPresetsPopoverVisibility: function () {
      return Properties.bSmartDocumentPresetsPopoverVisibility;
    },

    setSmartDocumentPresetsPopoverVisibility: function (_bSmartDocumentPresetsPopoverVisibility) {
      Properties.bSmartDocumentPresetsPopoverVisibility = _bSmartDocumentPresetsPopoverVisibility;
    },

    setOrganizationsPopoverVisibility: function (_bOrganizationsPopoverVisibility) {
      Properties.bOrganizationsPopoverVisibility = _bOrganizationsPopoverVisibility;
    },

    getKpiListForSummary: function () {
      return Properties.aKpiListForSummary;
    },

    setKpiListForSummary: function (_aKpiListForSummary) {
      Properties.aKpiListForSummary = _aKpiListForSummary;
    },

    getProcessedContentKPIDataMap: function () {
      return Properties.processedContentKPIDataMap;
    },

    setProcessedContentKPIDataMap: function (_processedContentKPIDataMap) {
      Properties.processedContentKPIDataMap = _processedContentKPIDataMap;
    },

    getSelectedKPIChartType: function () {
      return Properties.selectedKPIChartType;
    },

    setSelectedKPIChartType: function (_selectedKPIChartType) {
      Properties.selectedKPIChartType = _selectedKPIChartType;
    },

    getActiveKPIObject: function () {
      return Properties.activeKPIObject;
    },

    setActiveKPIObject: function (_activeKPIObject) {
      Properties.activeKPIObject = _activeKPIObject;
    },

    getIsKpiContentExplorerOpen: function () {
      return Properties.bIsKpiContentExplorerOpen;
    },

    setIsKpiContentExplorerOpen: function (_bIsKpiContentExplorerOpen) {
      Properties.bIsKpiContentExplorerOpen = _bIsKpiContentExplorerOpen;
    },

    getKpiContentExplorerData: function () {
      return Properties.oKpiContentExplorerData;
    },

    setKpiContentExplorerData: function (_oKpiContentExplorerData) {
      Properties.oKpiContentExplorerData = _oKpiContentExplorerData;
    },

    getPreventDashboardDataReset: function () {
      return Properties.bPreventDashboardDataReset;
    },

    setPreventDashboardDataReset: function (_bPreventDashboardDataReset) {
      Properties.bPreventDashboardDataReset = _bPreventDashboardDataReset;
    },

    getMultipleTaxonomyHierarchyTypeChangeCallCounter: function () {
      return Properties.iMultipleTaxonomyHierarchyTypeChangeCallCounter;
    },

    setMultipleTaxonomyHierarchyTypeChangeCallCounter: function (iCount) {
      Properties.iMultipleTaxonomyHierarchyTypeChangeCallCounter = iCount;
    },

    decrementMultipleTaxonomyHierarchyTypeChangeCallCounter: function () {
      Properties.iMultipleTaxonomyHierarchyTypeChangeCallCounter--;
    },

    getConflictingSources: function () {
      return Properties.oConflictingSources;
    },

    setConflictingSources: function (_oConflictingSources) {
      Properties.oConflictingSources = _oConflictingSources;
    },

    getAllPossibleConflictingSources: function () {
      return Properties.oAllPossibleConflictingSources;
    },

    setAllPossibleConflictingSources: function (_oAllPossibleConflictingSources) {
      Properties.oAllPossibleConflictingSources = _oAllPossibleConflictingSources;
    },

    getElementsHavingConflictingValues: function () {
      return Properties.oContentElementsHavingConflictingValues;
    },

    setElementsHavingConflictingValues: function (_oContentElementsHavingConflictingValues) {
      Properties.oContentElementsHavingConflictingValues = _oContentElementsHavingConflictingValues;
    },

    setIsActiveClassGoldenRecord: function (_bIsActiveClassGoldenRecord) {
      Properties.bIsActiveClassGoldenRecord = _bIsActiveClassGoldenRecord;
    },

    getIsActiveClassGoldenRecord: function () {
      return Properties.bIsActiveClassGoldenRecord;
    },

    resetContentKPIData: function () {
      Properties.aKpiListForSummary = [];
      Properties.processedContentKPIDataMap ={};
      Properties.selectedKPIChartType = "doughnut";
    },

    setSelectedImageId: function (sSelectedImageId) {
      Properties.sSelectedImageId = sSelectedImageId;
    },

    getSelectedImageId: function () {
      return Properties.sSelectedImageId;
    },

    getSelectedDashboardTabId: function () {
      return Properties.sSelectedDashboardTabId;
    },

    setSelectedDashboardTabId: function (_sSelectedDashboardTabId) {
      Properties.sSelectedDashboardTabId = _sSelectedDashboardTabId;
    },

    getDirtyTableContextIds: function () {
      return Properties.aDirtyTableContextIds;
    },

    setAttributeVariantsStats: function (_attributeVariantsStats) {
      Properties.attributeVariantsStats = _attributeVariantsStats;
    },

    getAttributeVariantsStats: function () {
      return Properties.attributeVariantsStats;
    },

    getToolbarButtons: function () {
      return Properties.aToolbarDummyButtons;
    },

    updateButtonsState: function (_sButtonId) {
      let aListButtons = this.getToolbarButtons();
      let oButtonToUpdate = aListButtons.find(button => button.id === _sButtonId);
      oButtonToUpdate.isActive = !oButtonToUpdate.isActive;
    },

    getIsAcrolinxSidebarVisible: function () {
      return Properties.isAcrolinxSidebarVisible;
    },

    setIsAcrolinxSidebarVisible: function (_bIsAcrolinxSidebarVisible) {
      Properties.isAcrolinxSidebarVisible = _bIsAcrolinxSidebarVisible;
    },

    getQuickListDragNDropState: function () {
      return Properties.quicklistDragNDropState;
    },

    setQuickListDragNDropState: function (_bQuickListDragNDropState) {
      Properties.quicklistDragNDropState = _bQuickListDragNDropState;
    },

    getReferencedLanguages: function () {
      return Properties.oReferencedLanguages;
    },

    setReferencedLanguages: function (oReferencedLanguages) {
      Properties.oReferencedLanguages = oReferencedLanguages;
    },

    getIsRelationshipContextDialogOpen: function () {
      return Properties.bIsRelationshipContextDialogOpen;
    },

    setIsRelationshipContextDialogOpen: function (_bIsRelationshipContextDialogOpen) {
      Properties.bIsRelationshipContextDialogOpen = _bIsRelationshipContextDialogOpen;
    },

    getIsDataLanguagePopoverVisible: function () {
      return Properties.bIsDataLanguagePopoverVisible;
    },

    setIsDataLanguagePopoverVisible: function (_bIsDataLanguagePopoverVisible) {
      Properties.bIsDataLanguagePopoverVisible = _bIsDataLanguagePopoverVisible;
    },

    getIsCreateTranslationPopoverVisible: function () {
      return Properties.bIsCreateTranslationPopoverVisible;
    },

    setIsCreateTranslationPopoverVisible: function (_bIsDataLanguagePopoverVisible) {
      Properties.bIsCreateTranslationPopoverVisible = _bIsDataLanguagePopoverVisible;
    },

    getIsHelperLanguagePopoverVisible: function () {
      return Properties.showHelperLanguagePopover;
    },

    setIsHelperLanguagePopoverVisible: function (_showHelperLanguagePopover) {
      Properties.showHelperLanguagePopover = _showHelperLanguagePopover;
    },

    getHelperLanguageInstances: function () {
      return Properties.helperLanguageInstances;
    },

    setHelperLanguageInstances: function (_helperLanguageInstances) {
      Properties.helperLanguageInstances = _helperLanguageInstances;
    },

    /**Currently active image variant context on rendition tab is store in selected image variant context*/
    getSelectedImageVariantContext: function () {
      return Properties.oSelectedImageVariantContext;
    },

    getIsHelperLanguageDisabled: function () {
      return Properties.bIsHelperLanguageDisabled;
    },

    setIsHelperLanguageDisabled: function (_bIsHelperLanguageDisabled) {
      Properties.bIsHelperLanguageDisabled = _bIsHelperLanguageDisabled;
    },

    getIsGenerateSmartDocuemntButtonVisible: function () {
      return Properties.bIsGenerateSmartDocumentButtonVisible;
    },

    setIsGenerateSmartDocuemntButtonVisible: function (_bIsGenerateSmartDocumentButtonVisible) {
      Properties.bIsGenerateSmartDocumentButtonVisible = _bIsGenerateSmartDocumentButtonVisible;
    },

    getImageVariantData: function () {
      return Properties.aImageVariantData;
    },

    getIsVersionableAttributeValueChanged: function () {
      return Properties.isVersionableAttributeValueChanged;
    },

    setIsVersionableAttributeValueChanged: function (_bIsVersionableAttributeValueChanged) {
      Properties.isVersionableAttributeValueChanged = _bIsVersionableAttributeValueChanged;
	},

    getAssetExtensions: function () {
      return Properties.oAssetExtensions;
    },

    setAssetExtensions: function (_oAssetExtensions) {
      Properties.oAssetExtensions = _oAssetExtensions;
    },

    getIsSwitchDataLanguageDisabled: function () {
      return Properties.bIsSwitchDataLanguageDisabled;
    },

    setIsSwitchDataLanguageDisabled: function (_bIsSwitchDataLanguageDisabled) {
      Properties.bIsSwitchDataLanguageDisabled = _bIsSwitchDataLanguageDisabled;
    },

    getVariantOfLabel: function () {
      return Properties.sVariantOfLabel;
    },

    setVariantOfLabel: function (sVariantOfLabel) {
      return Properties.sVariantOfLabel = sVariantOfLabel;
    },

    getIsShowClassificationDialog: function () {
      return Properties.bShowClassificationDialog;
    },

    getIsContentComparisonMode: function () {
      return Properties.bIsContentComparisonMode;
    },

    setIsContentComparisonMode: function (_bIsContentComparisonMode) {
      Properties.bIsContentComparisonMode = _bIsContentComparisonMode
    },

    getIsEditMode: function () {
      return Properties.bIsEditMode;
    },

    setIsEditMode: function (_bIsEditMode) {
      Properties.bIsEditMode = _bIsEditMode
    },

    getAssetSharedUrl: function () {
      return Properties.sAssetSharedUrl;
    },

    setAssetSharedUrl: function (_sAssetSharedUrl) {
      Properties.sAssetSharedUrl = _sAssetSharedUrl;
    },

    getIsKlassInstanceFlatPropertyStatus: function () {
      return Properties.bIsKlassInstanceFlatPropertyUpdate;
    },

    setIsKlassInstanceFlatPropertyStatus: function (_bIsKlassInstanceFlatPropertyUpdate) {
      Properties.bIsKlassInstanceFlatPropertyUpdate = _bIsKlassInstanceFlatPropertyUpdate;
    },

    getDefaultPaginationData: function () {
      return new Props().oDefaultPaginationData;
    },

    getDefaultTypesSearchText: function () {
      return  Properties.sDefaultTypesSearchText;
    },

    setDefaultTypesSearchText: function (_sDefaultTypesSearchText) {
      Properties.sDefaultTypesSearchText = _sDefaultTypesSearchText;
    },

    getFunctionalPermission: function () {
      return  Properties.oFunctionPermission;
    },

    setFunctionalPermission: function (_oFunctionPermission) {
      Properties.oFunctionPermission = _oFunctionPermission;
    },

    getIsRevisionableTransfer: function () {
      return  Properties.isRevisionableTransfer;
    },

    setIsRevisionableTransfer: function (_bIsRevisionableTransfer) {
      Properties.isRevisionableTransfer = _bIsRevisionableTransfer;
    },

    getActiveEmbProperty: function () {
      return Properties.activeProperty;
    },

    setActiveEmbProperty: function (oActiveProperty) {
      Properties.activeProperty = oActiveProperty;
    },

    //Add functions above reset function
    reset: function () {
      Properties = new Props();
    }

  };


})();

export default ScreenProps;
