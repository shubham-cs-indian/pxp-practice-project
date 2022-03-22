import TabHeaderData from "../../tack/mock/mock-data-for-frequency-dictionary";

/**
 * Created by cs05 on 03/05/17.
 */

var ProcessConfigViewProps = (function () {


  var Props = function () {
    return {
      processValueList: [],
      processScreenLockStatus: false,
      activeProcess: {},
      availableComponents: [],
      selectedComponent: {},
      taxonomyPaginationData: {
        from: 0,
        size: 20,
        sortBy: "label",
        sortOrder: "asc",
        types: [],
        searchColumn: "label",
        searchText: "",
      },
      BPMNTaxonomyPaginationData: {
        from: 0,
        size: 20,
        sortBy: "label",
        sortOrder: "asc",
        types: [],
        searchColumn: "label",
        searchText: "",
      },
      searchText: "",
      searchConstantData: {
        searchColumn: "label",
        sortOrder: "asc",
        sortBy: "label",
        size: 20
      },
      from: 0,
      //BPMN Props
      activeBPMNElement: {},
      bpmnFactoryInstance: {},
      modelingInstance: {},
      modelerInstance: {},
      pureProcessDefinitionJSON: {},
      isUploadWindowActive: false,
      isAdvancedOptionsEnabled: false,
      shouldImportXML: false,
      isProcessDialogActive: false,
      isSearchFilterComponentDialogActive: false,
      aAllowedTaxonomies: [],
      oAllowedTaxonomyConfigDetails: {},
      oAllowedTaxonomyHierarchyList: {},
      referencedTaxonomies: {},
      selectedTaxonomies: [],
      parentTaxonomyList:[],
      taxonomyIds:[],
      processGridDataList:[],
      configDetails:{},
      isFullScreenMode:false,
      allowedTaxonomiesProcessConfig:[],
      parentTaxonomyListProcessConfig:[],
      referencedTaxonomiesForComponent:{},
      oLoadedTags: {},
      oLoadedAttributes : {},
      isSaveAsDialogActive: false,
      dataForSaveAsDialog: [],
      codeDuplicates:[],
      codeUniqueness:false,
      workflowType: "",
      entityType: "",
      entityTypeDisabled: false,
      validationInfo: {},
      gridWFValidationErrorList: [],
      aAppliedFilterData: [],
      aAppliedFilterClonedData: [],
      aSearchFilterData: [],
      bIsFilterExpanded: false,
      bIsFilterDirty: false,
      oAvailableFilterSearchData: {},
      aHeaderData: [
        {
          id: TabHeaderData.DURATION,
          label: 'Duration'
        },
        {
          id: TabHeaderData.DATE,
          label: 'Date'
        },
        {
          id: TabHeaderData.DAILY,
          label: 'Daily'
        },
        {
          id: TabHeaderData.HOURMIN,
          label: 'HourMin'
        },
        {
          id: TabHeaderData.WEEKLY,
          label: 'Weekly'
        },
        {
          id: TabHeaderData.MONTHLY,
          label: 'Monthly'
        },
        {
          id: TabHeaderData.YEARLY,
          label: 'Yearly'
        }
      ],
      sSelectedTabId: TabHeaderData.DURATION,
      oFrequencyData : {
        duration : {
          hours : 0,
          mins : 0
        },
        date : {
          date :"",
          hours : 0,
          mins : 0
        },
        daily : {
          hours : 0,
          mins : 0
        },
        hourmin : {
          hours : 0,
          mins : 0
        },
        weekly : {
          daysOfWeeks: [],
          hours : 0,
          mins : 0
        },
        monthly : {
          days: 1,
          months : 1,
        },
        yearly : {
          monthsOfYear : 1,
          days : 1
        },
      },
      bIsUploadedWF : false
    }
  };

  var oProperties = new Props();

  return {

    getProcessValueList: function () {
      return oProperties.processValueList;
    },

    setProcessValueList: function (oProcessValueList) {
      oProperties.processValueList = oProcessValueList;
    },

    getProcessScreenLockStatus: function () {
      return oProperties.processScreenLockStatus;
    },

    setProcessScreenLockStatus: function (bProcessScreenLockStatus) {
      oProperties.processScreenLockStatus = bProcessScreenLockStatus;
    },

    getActiveProcess: function () {
      return oProperties.activeProcess;
    },

    setActiveProcess: function (oActiveProcess) {
      oProperties.activeProcess = oActiveProcess;
    },

    getSelectedComponent: function () {
      return oProperties.selectedComponent;
    },

    setSelectedComponent: function (oSelectedComponent) {
      oProperties.selectedComponent = oSelectedComponent;
    },

    reset: function () {
      oProperties = new Props();
    },

    getSearchText: function () {
      return oProperties.searchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.searchText = _sSearchText;
    },

    getSearchConstantData: function () {
      return oProperties.searchConstantData;
    },

    getFrom: function () {
      return oProperties.from
    },

    setFrom: function (_iFrom) {
      oProperties.from = _iFrom
    },

    setActiveBPMNElement: function (_oBPMNElement) {
      oProperties.activeBPMNElement = _oBPMNElement;
    },

    getActiveBPMNElement: function () {
      return oProperties.activeBPMNElement;
    },
    setActiveBPMNFactoryInstance: function (_oBPMMFactoryInstance) {
      oProperties.bpmnFactoryInstance= _oBPMMFactoryInstance;
    },

    getActiveBPMNFactoryInstance: function () {
      return oProperties.bpmnFactoryInstance;
    },

    setActiveBPMNModelingInstance: function (_oModelingInstance) {
      oProperties.modelingInstance= _oModelingInstance;
    },

    getActiveBPMNModelingInstance: function () {
      return oProperties.modelingInstance;
    },

    setActiveBPMNModelerInstance: function (_oModelerInstance) {
      oProperties.modelerInstance = _oModelerInstance;
    },

    getActiveBPMNModelerInstance: function () {
      return oProperties.modelerInstance;
    },

    setPureProcessDefinitionJSON: function (_oDefinition) {
      oProperties.pureProcessDefinitionJSON = _oDefinition;
    },

    getPureProcessDefinitionJSON: function () {
      return oProperties.pureProcessDefinitionJSON;
    },

    getIsUploadWindowActive: function () {
      return oProperties.isUploadWindowActive;
    },

    setIsUploadWindowActive: function (bIsUploadWindowActive) {
      oProperties.isUploadWindowActive = bIsUploadWindowActive;
    },

    getIsAdvancedOptionsEnabled: function () {
      return oProperties.isAdvancedOptionsEnabled;
    },

    setIsAdvancedOptionsEnabled: function (bIsAdvancedOptionsEnabled) {
      oProperties.isAdvancedOptionsEnabled = bIsAdvancedOptionsEnabled;
    },

    getShouldImportXML: function () {
      return oProperties.shouldImportXML;
    },

    setShouldImportXML: function (bShouldImportXML) {
      oProperties.shouldImportXML = bShouldImportXML;
    },

    getIsProcessDialogActive: function () {
      return oProperties.isProcessDialogActive;
    },

    setIsProcessDialogActive: function (bIsProcessDialogActive) {
      oProperties.isProcessDialogActive = bIsProcessDialogActive;
    },

    getIsSearchFilterComponentDialogActive: function () {
      return oProperties.isSearchFilterComponentDialogActive;
    },

    setIsSearchFilterComponentDialogActive: function (bIsSearchFilterComponentDialogActive) {
      oProperties.isSearchFilterComponentDialogActive = bIsSearchFilterComponentDialogActive;
    },

    getLoadedAttributes: function () {
      return oProperties.oLoadedAttributes;
    },

    setLoadedAttributes: function (_oLoadedAttributes) {
      oProperties.oLoadedAttributes = _oLoadedAttributes;
    },

    getLoadedTags: function () {
      return oProperties.oLoadedTags;
    },

    setLoadedTags: function (_oLoadedTags) {
      oProperties.oLoadedTags = _oLoadedTags;
    },

    getAllowedTaxonomies: function () {
      return oProperties.aAllowedTaxonomies;
    },

    setAllowedTaxonomies: function (_aAllowedTaxonomies) {
      oProperties.aAllowedTaxonomies = _aAllowedTaxonomies;
    },

    getTaxonomyPaginationData: function () {
      return oProperties.taxonomyPaginationData;
    },

    setTaxonomyPaginationData: function (_oTaxonomyPaginationData) {
      oProperties.taxonomyPaginationData = _oTaxonomyPaginationData;
    },

    getBPMNTaxonomyPaginationData: function () {
      return oProperties.BPMNTaxonomyPaginationData;
    },


    getReferencedTaxonomies: function () {
      return oProperties.referencedTaxonomies;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.referencedTaxonomies = _oReferencedTaxonomies;
    },

    getSelectedTaxonomies: function () {
      return oProperties.selectedTaxonomies;
    },

    setSelectedTaxonomies: function (_aSelectedTaxonomies) {
      oProperties.selectedTaxonomies = _aSelectedTaxonomies;
    },

    getParentTaxonomyList: function () {
      return oProperties.parentTaxonomyList;
    },

    setParentTaxonomyList: function (_aParentTaxonomyList) {
      oProperties.parentTaxonomyList = _aParentTaxonomyList;
    },

    getTaxonomyIds: function () {
      return oProperties.taxonomyIds;
    },

    setTaxonomyIds: function (_aTaxonomyIds) {
      oProperties.taxonomyIds = _aTaxonomyIds;
    },

    getProcessGridViewData: function () {
      return oProperties.processGridDataList;
    },

    setProcessGridViewData: function (_aProcessGridDataList) {
      oProperties.processGridDataList = _aProcessGridDataList;
    },

    getConfigDetailsData:  function () {
      return oProperties.configDetails;
    },

    setConfigDetailsData: function (_oConfigDetailsData) {
      oProperties.configDetails = _oConfigDetailsData;
    },

    getIsFullScreenMode: function () {
      return oProperties.isFullScreenMode;
    },

    setIsFullScreenMode: function (bIsFullScreenMode) {
      oProperties.isFullScreenMode = bIsFullScreenMode;
    },

    getAllowedTaxonomiesInProcessConfig: function () {
      return oProperties.allowedTaxonomiesProcessConfig;
    },

    setAllowedTaxonomiesInProcessConfig: function (_aAllowedTaxonomiesProcessConfig) {
      oProperties.allowedTaxonomiesProcessConfig = _aAllowedTaxonomiesProcessConfig;
    },

    getParentTaxonomyListProcessConfig: function () {
      return oProperties.parentTaxonomyListProcessConfig;
    },

    setParentTaxonomyListProcessConfig: function (_aParentTaxonomyListProcessConfig) {
      oProperties.parentTaxonomyListProcessConfig = _aParentTaxonomyListProcessConfig;
    },

    getReferencedTaxonomiesForComponent: function () {
      return oProperties.referencedTaxonomiesForComponent;
    },

    setReferencedTaxonomiesForComponent: function (_oReferencedTaxonomiesForComponent) {
      oProperties.referencedTaxonomiesForComponent = _oReferencedTaxonomiesForComponent;
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

    getWorkflowType: function () {
      return oProperties.workflowType;
    },

    setWorkflowType: function (_sWorkflowType) {
      oProperties.workflowType = _sWorkflowType;
    },

    getEntityType: function () {
      return oProperties.entityType;
    },

    setEntityType: function (_sEntityType) {
      oProperties.entityType = _sEntityType;
    },

    getEntityTypeDisabled: function () {
      return oProperties.entityTypeDisabled;
    },

    setEntityTypeDisabled: function (_bIsEntityTypeDisabled) {
      oProperties.entityTypeDisabled = _bIsEntityTypeDisabled;
    },

    getValidationInfo: function () {
      return oProperties.validationInfo;
    },

    setValidationInfo: function (oValidationInfo) {
      oProperties.validationInfo = oValidationInfo;
    },

    getAllowedTaxonomyConfigDetails: function () {
      return oProperties.oAllowedTaxonomyConfigDetails;
    },

    setAllowedTaxonomyConfigDetails: function (_oAllowedTaxonomyConfigDetails) {
      oProperties.oAllowedTaxonomyConfigDetails = _oAllowedTaxonomyConfigDetails;
    },

    getAllowedTaxonomyHierarchyList: function () {
      return oProperties.oAllowedTaxonomyHierarchyList;
    },

    setAllowedTaxonomyHierarchyList: function (_oAllowedTaxonomyHierarchyList) {
      oProperties.oAllowedTaxonomyHierarchyList = _oAllowedTaxonomyHierarchyList;
    },

    getGridWFValidationErrorList: function () {
      return oProperties.gridWFValidationErrorList;
    },

    setGridWFValidationErrorList: function (aGridWFValidationErrorList) {
      oProperties.gridWFValidationErrorList = aGridWFValidationErrorList;
    },

    getAppliedFilterData: function () {
      return oProperties.aAppliedFilterData;
    },

    setAppliedFilterData: function (_aAppliedFilterData) {
      oProperties.aAppliedFilterData = _aAppliedFilterData;
    },

    getAppliedFilterClonedData: function () {
      return oProperties.aAppliedFilterClonedData;
    },

    setAppliedFilterClonedData: function (_aAppliedFilterClonedData) {
      oProperties.aAppliedFilterClonedData = _aAppliedFilterClonedData;
    },

    getSearchFilterData: function () {
      return oProperties.aSearchFilterData;
    },

    setSearchFilterData: function (_aSearchFilterData) {
      oProperties.aSearchFilterData = _aSearchFilterData;
    },

    getIsFilterExpanded: function () {
      return oProperties.bIsFilterExpanded;
    },

    setIsFilterExpanded: function (_bIsFilterExpanded) {
      oProperties.bIsFilterExpanded = _bIsFilterExpanded;
    },

    getIsFilterDirty: function () {
      return oProperties.bIsFilterDirty;
    },

    setIsFilterDirty: function (_bIsFilterDirty) {
      oProperties.bIsFilterDirty = _bIsFilterDirty;
    },

    getAvailableFilterSearchData: function () {
      return oProperties.oAvailableFilterSearchData;
    },

    setAvailableFilterSearchData: function (_oAvailableFilterSearchData) {
      oProperties.oAvailableFilterSearchData = _oAvailableFilterSearchData;
    },

    getSelectedTabId: function () {
      return oProperties.sSelectedTabId;
    },

    setSelectedTabId: function (_sSelectedId) {
      oProperties.sSelectedTabId = _sSelectedId;
    },

    getTabHeaderData: function () {
      return oProperties.aHeaderData;
    },

    setTabHeaderData: function (_aHeaderData) {
      oProperties.aHeaderData = _aHeaderData;
    },

    getFrequencyData: function () {
      return oProperties.oFrequencyData;
    },

    setFrequencyData: function (_oFrequencyData) {
      oProperties.oFrequencyData = _oFrequencyData;
    },
    getIsUploadedWF: function () {
      return oProperties.bIsUploadedWF;
    },

    setIsUploadedWF: function (_bIsUploadedWF) {
      oProperties.bIsUploadedWF = _bIsUploadedWF;
    },


    resetBPMNProperties: function () {
      oProperties.isAdvancedOptionsEnabled = false;
      oProperties.isUploadWindowActive = false;
      oProperties.shouldImportXML = false;
      oProperties.pureProcessDefinitionJSON = {};
      oProperties.modelerInstance = {};
      oProperties.modelingInstance = {};
      oProperties.bpmnFactoryInstance = {};
      oProperties.activeBPMNElement = {};
    },


    toJSON: function () {
      return {};
    }
  }

})();

export default ProcessConfigViewProps;