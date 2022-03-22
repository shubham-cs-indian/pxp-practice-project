
import CS from '../../../../../../libraries/cs';

import { endpointsWorkspaceList as aEndpointsWorkspaceList } from '../../tack/mock/mock-data-for-endpoints-workspace';

var ProfileConfigViewProps = (function () {

  var Props = function () {
    return {
      oSelectedProfile: {},
      aSelectedMappingRows: [],
      aEndpointsList: [],
      aEndpointsIndexList: aEndpointsWorkspaceList() || [],
      aSystemList: [],
      sSystemsListSearchText: "",
      oSystemsListPaginationData: {
        from: 0,
        size: 20
      },
      aEndpointGridData: [],
      oSelectedSystemsMap: {},
      aReferencedSystems:[],
      oReferencedDataRules:{},
      oReferencedProcesses:{},
      oReferencedJmsProcesses: {},
      oReferencedMappings:{},
      oReferencedDashboardTabs:{},
      oReferencedAuthorizationMappings: {},
      oConfigDetails:{},
      setIsProfileDialogActive: false,
      aAllowedTaxonomies:[],
      taxonomyPaginationData:{
        from: 0,
        size: 20,
        sortBy: "label",
        sortOrder: "asc",
        types: [],
        searchColumn: "label",
        searchText: "",
      },
      parentTaxonomyList:[],
      oReferencedTaxonomies:{},
      profileScreenLockStatus:false,
      isSaveAsDialogActive: false,
      dataForSaveAsDialog: [],
      dataForWorkflowSaveAsDialog: [],
      duplicateCodesForEndpoints: [],
      duplicateCodesForWorkflows: [],
      duplicateLabelsForEndpoints: [],
      duplicateLabelsForWorkflows: [],
      codeUniqueness: false
    }
  };

  var oProperties = new Props();

  return {

    getSelectedProfile: function () {
      return oProperties.oSelectedProfile;
    },

    setSelectedProfile: function (_oProfile) {
      oProperties.oSelectedProfile = _oProfile;
    },

    getSelectedMappingRows: function () {
      return oProperties.aSelectedMappingRows;
    },

    setSelectedMappingRows: function (_aSelectedMappingRows) {
      oProperties.aSelectedMappingRows = _aSelectedMappingRows;
    },

    getEndpointsList: function () {
      return oProperties.aEndpointsList;
    },

    setEndpointsList: function (_aEndpointsList) {
      oProperties.aEndpointsList = _aEndpointsList;
    },

    getEndpointIndexList: function () {
      return CS.cloneDeep(aEndpointsWorkspaceList());
    },

    getSystemsListSearchText: function () {
      return oProperties.sSystemsListSearchText;
    },

    setSystemsListSearchText: function (_sSystemsListSearchText) {
      oProperties.sSystemsListSearchText = _sSystemsListSearchText;
    },

    getSystemsListPaginationData: function () {
      return oProperties.oSystemsListPaginationData;
    },

    setSystemsListPaginationData: function (_oSystemsListPaginationData) {
      oProperties.oSystemsListPaginationData = _oSystemsListPaginationData;
    },

    getEndpointGridData: function () {
      return oProperties.aEndpointGridData;
    },

    setEndpointGridData: function (_aEndpointGridData) {
      oProperties.aEndpointGridData = _aEndpointGridData;
    },

    getSystemsList: function () {
      return oProperties.aSystemList;
    },

    setSystemsList: function (_aSystemsList) {
      oProperties.aSystemList = _aSystemsList;
    },

    getSelectedSystem: function (sEndpointId) {
      return oProperties.oSelectedSystemsMap[sEndpointId];
    },

    setSelectedSystem: function (sEndpointId, _aSelectedSystem) {
      oProperties.oSelectedSystemsMap[sEndpointId] = _aSelectedSystem;
    },

    getReferencedSystems: function () {
      return oProperties.aReferencedSystems;
    },

    setReferencedSystems: function (_List) {
      oProperties.aReferencedSystems = _List
    },

    setReferencedDataRules: function (_List) {
      oProperties.oReferencedDataRules = _List
    },

    getReferencedDataRules: function () {
      return oProperties.oReferencedDataRules;
    },

    setReferencedMappings: function (_List) {
      oProperties.oReferencedMappings = _List
    },

    getReferencedMappings: function () {
      return oProperties.oReferencedMappings;
    },

    setReferencedProcesses: function (_List) {
      oProperties.oReferencedProcesses = _List
    },

    getReferencedProcesses: function () {
      return oProperties.oReferencedProcesses;
    },

    setReferencedJmsProcesses: function (_List) {
      oProperties.oReferencedJmsProcesses = _List
    },

    getReferencedJmsProcesses: function () {
      return oProperties.oReferencedJmsProcesses;
    },

    setReferencedDashboardTabs: function (_oMap) {
      oProperties.oReferencedDashboardTabs = _oMap
    },

    getReferencedDashboardTabs: function () {
      return oProperties.oReferencedDashboardTabs;
    },

    setReferencedAuthorizationMappings: function (_oReferencedAuthorizationMappings) {
      oProperties.oReferencedAuthorizationMappings = _oReferencedAuthorizationMappings;
    },

    getReferencedAuthorizationMappings: function () {
      return oProperties.oReferencedAuthorizationMappings;
    },

    setConfigDetails: function (_oConfigDetails) {
      oProperties.oConfigDetails = _oConfigDetails;
    },

    getConfigDetails: function () {
      return oProperties.oConfigDetails;
    },

    setIsProfileDialogActive: function (_bSetIsProfileDialogActive) {
      oProperties.setIsProfileDialogActive = _bSetIsProfileDialogActive;
    },

    getIsProfileDialogActive: function () {
      return oProperties.setIsProfileDialogActive;
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


    getParentTaxonomyList: function () {
      return oProperties.parentTaxonomyList;
    },

    setParentTaxonomyList: function (_aParentTaxonomyList) {
      oProperties.parentTaxonomyList = _aParentTaxonomyList;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.oReferencedTaxonomies = _oReferencedTaxonomies
    },

    getReferencedTaxonomies: function () {
      return oProperties.oReferencedTaxonomies;
    },

    getProfileScreenLockStatus: function () {
      return oProperties.profileScreenLockStatus;
    },

    setProfileScreenLockStatus: function (bProfileScreenLockStatus) {
      oProperties.profileScreenLockStatus = bProfileScreenLockStatus;
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

    getDataForWorkflowSaveAsDialog: function () {
      return oProperties.dataForWorkflowSaveAsDialog;
    },

    setDataForWorkflowSaveAsDialog: function (_dataForWorkflowSaveAsDialog) {
      oProperties.dataForWorkflowSaveAsDialog = _dataForWorkflowSaveAsDialog;
    },

    getDuplicateCodesForEndpoints: function () {
      return oProperties.duplicateCodesForEndpoints;
    },

    setDuplicateCodesForEndpoints: function (_aDuplicateCodesForEndpoints) {
      oProperties.duplicateCodesForEndpoints = _aDuplicateCodesForEndpoints;
    },

    getDuplicateCodesForWorkflows: function () {
      return oProperties.duplicateCodesForWorkflows;
    },

    setDuplicateCodesForWorkflows: function (_aDuplicateCodesForWorkflows) {
      oProperties.duplicateCodesForWorkflows = _aDuplicateCodesForWorkflows;
    },

    getDuplicateLabelsForEndpoints: function () {
      return oProperties.duplicateLabelsForEndpoints;
    },

    setDuplicateLabelsForEndpoints: function (_aDuplicateLabelsForEndpoints) {
      oProperties.duplicateLabelsForEndpoints = _aDuplicateLabelsForEndpoints;
    },

    getDuplicateLabelsForWorkflows: function () {
      return oProperties.duplicateLabelsForWorkflows;
    },

    setDuplicateLabelsForWorkflows: function (_aDuplicateLabelsForWorkflows) {
      oProperties.duplicateLabelsForWorkflows = _aDuplicateLabelsForWorkflows;
    },

    setDuplicates: function (_aDuplicateCodesForEndpoints = [], _aDuplicateCodesForWorkflows = [], _aDuplicateLabelsForEndpoints = [], _aDuplicateLabelsForWorkflows = []) {
      oProperties.duplicateCodesForEndpoints = _aDuplicateCodesForEndpoints;
      oProperties.duplicateCodesForWorkflows = _aDuplicateCodesForWorkflows;
      oProperties.duplicateLabelsForEndpoints = _aDuplicateLabelsForEndpoints;
      oProperties.duplicateLabelsForWorkflows = _aDuplicateLabelsForWorkflows;
    },

    resetPaginationData: function () {
      oProperties.sSystemsListSearchText = "";
      oProperties.oSystemsListPaginationData = {
        from: 0,
        size: 20
      };
    },

    reset: function () {
      oProperties = new Props();
    }

  };
})();

export default ProfileConfigViewProps;
