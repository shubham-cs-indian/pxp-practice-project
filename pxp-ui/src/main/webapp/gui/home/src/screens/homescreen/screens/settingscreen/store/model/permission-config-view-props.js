/**
 * Created by CS80 on 2/1/2017.
 */
import CS from'../../../../../../libraries/cs';

var PermissionConfigViewProps = (function () {

  let Props = function () {
    return {
      oActivePermission: {},
      bPermissionScreenLockStatus: false,
      bIsPermissionModeClass: true,
      aSelectedIds: [],
      sSelectedFirstLevelClass: "",
      sSelectedPropertyCollection: "",
      sSelectedTemplate: "",
      oClassesVisibilityStatus: {},
      sActiveRoleId: "",
      oActiveNatureClassDetails: {},
      oActiveEntityInformation: {},
      oReferencedPropertyCollections: {},
      oReferencedRelationships: {},
      oSelectedType: {},
      aEntityNavigationTreeData: [],
      oEntityNavigationTreeVisualData: {},
      sSelectedTreeItemId: "",
      sSelectedEntityType: "",
      oEntityChildrenPaginationData: {},
      oHierarchyTree: {},
      oHierarchyTreeVisualData: {},
      oPermissionMap: {},
      oFunctionalPermissionMap: {},
      oModifiedPermissionMap: {},
      oModifiedFunctionalPermissionMap: {},
      sHierarchyTreeActiveTabId: "",
      bIsPermissionDirty: false,

    };
  };

  let oProperties = new Props();
  let oDefaultPaginationData = {
    from: 0,
    size: 20,
    searchText: ""
  };

  return {

    getPermissionScreenLockStatus: function () {
      return oProperties.bPermissionScreenLockStatus;
    },

    setPermissionScreenLockStatus: function (_status) {
      oProperties.bPermissionScreenLockStatus = _status;
    },

    getIsPermissionModeClass: function () {
      return oProperties.bIsPermissionModeClass;
    },

    setIsPermissionModeClass: function (_bPermissionModeClass) {
      oProperties.bIsPermissionModeClass = _bPermissionModeClass;
    },

    getSelectedIds: function () {
      return oProperties.aSelectedIds;
    },

    setSelectedIds: function (_aSelectedIds) {
      oProperties.aSelectedIds = _aSelectedIds;
    },

    getActivePermission: function () {
      return oProperties.oActivePermission;
    },

    getCurrentPermission: function () {
      return oProperties.oActivePermission.clonedObject ? oProperties.oActivePermission.clonedObject : oProperties.oActivePermission;
    },

    setActivePermission: function (_sActiveRuleId) {
      oProperties.oActivePermission = _sActiveRuleId;
    },

    getSelectedFirstLevelClass: function () {
      return oProperties.sSelectedFirstLevelClass;
    },

    setSelectedFirstLevelClass: function (_sSelectedFirstLevelClass) {
      oProperties.sSelectedFirstLevelClass = _sSelectedFirstLevelClass;
    },

    getSelectedPropertyCollection: function () {
      return oProperties.sSelectedPropertyCollection;
    },

    setSelectedPropertyCollection: function (_sSelectedPropertyCollection) {
      oProperties.sSelectedPropertyCollection = _sSelectedPropertyCollection;
    },

    getSelectedTemplate: function () {
      return oProperties.sSelectedTemplate;
    },

    setSelectedTemplate: function (sId) {
      oProperties.sSelectedTemplate = sId;
    },

    getClassVisibilityStatus: function () {
      return oProperties.oClassesVisibilityStatus;
    },

    setClassVisibilityStatus: function (_oClassesVisibilityStatus) {
      oProperties.oClassesVisibilityStatus = _oClassesVisibilityStatus;
    },

    getActiveRoleId: function () {
      return oProperties.sActiveRoleId;
    },

    setActiveRoleId: function (_sActiveRoleId) {
      oProperties.sActiveRoleId = _sActiveRoleId;
    },

    getActivePermissionEntity: function () {
      return oProperties.oActiveNatureClassDetails;
    },

    setActivePermissionEntity: function (_oActiveNatureClassDetails) {
      oProperties.oActiveNatureClassDetails = _oActiveNatureClassDetails;
    },

    getActiveEntityInformation: function () {
      return oProperties.oActiveEntityInformation;
    },

    setActiveEntityInformation: function (_oActiveEntityInformation) {
      oProperties.oActiveEntityInformation = _oActiveEntityInformation;
    },

    getReferencedPropertyCollections: function () {
      return oProperties.oReferencedPropertyCollections;
    },

    setReferencedPropertyCollections: function (_oReferencedPropertyCollections) {
      oProperties.oReferencedPropertyCollections = _oReferencedPropertyCollections;
    },

    getReferencedRelationships: function () {
      return oProperties.oReferencedRelationships;
    },

    setReferencedRelationships: function (_oReferencedRelationships) {
      oProperties.oReferencedRelationships = _oReferencedRelationships;
    },

    getSelectedTypeAndEntity: function () {
      return oProperties.oSelectedType;
    },

    setSelectedTypeAndEntity: function (_oSelectedType) {
      oProperties.oSelectedType = _oSelectedType;
    },

    getEntityNavigationTreeData: function () {
      return oProperties.aEntityNavigationTreeData;
    },

    setEntityNavigationTreeData: function (_aEntityNavigationTreeData) {
      oProperties.aEntityNavigationTreeData = _aEntityNavigationTreeData;
    },

    getEntityNavigationTreeVisualData: function () {
      return oProperties.oEntityNavigationTreeVisualData;
    },

    setEntityNavigationTreeVisualData: function (_oEntityNavigationTreeVisualData) {
      oProperties.oEntityNavigationTreeVisualData = _oEntityNavigationTreeVisualData;
    },

    getSelectedTreeItemId: function() {
      return oProperties.sSelectedTreeItemId;
    },

    setSelectedTreeItemId: function(_sSelectedTreeItemId) {
      oProperties.sSelectedTreeItemId = _sSelectedTreeItemId;
    },

    getSelectedEntityType: function() {
      return oProperties.sSelectedEntityType;
    },

    setSelectedEntityType: function(_sSelectedEntityType) {
      oProperties.sSelectedEntityType = _sSelectedEntityType;
    },

    getEntityChildrenPaginationDataByEntityId: function (sId) {
      if (CS.isEmpty(oProperties.oEntityChildrenPaginationData[sId])) {
        oProperties.oEntityChildrenPaginationData[sId] = CS.cloneDeep(oDefaultPaginationData);
      }
      return oProperties.oEntityChildrenPaginationData[sId];
    },

    getAllEntityChildrenPaginationData: function () {
      return oProperties.oEntityChildrenPaginationData;
    },

    setAllEntityChildrenPaginationData: function (oData) {
      oProperties.oEntityChildrenPaginationData = oData;
    },

    getHierarchyTree: function () {
      return oProperties.oHierarchyTree;
    },

    setHierarchyTree: function (_oHierarchyTree) {
      oProperties.oHierarchyTree = _oHierarchyTree;
    },

    getHierarchyTreeVisualData: function () {
      return oProperties.oHierarchyTreeVisualData;
    },

    setHierarchyTreeVisualData: function (oData) {
      oProperties.oHierarchyTreeVisualData = oData;
    },

    getPermissionMap: function () {
      return oProperties.oPermissionMap;
    },

    getModifiedPermissionMap: function () {
      return oProperties.oModifiedPermissionMap;
    },

    getFunctionalPermissionMap: function () {
      return oProperties.oFunctionalPermissionMap;
    },

    getIsPermissionDirty: function () {
      return oProperties.bIsPermissionDirty;
    },

    setIsPermissionDirty: function (_bIsPermissionDirty) {
      oProperties.bIsPermissionDirty = _bIsPermissionDirty;
    },

    setFunctionalPermissionMap: function (oFunctionalPermissionMap) {
      oProperties.oFunctionalPermissionMap = oFunctionalPermissionMap;
    },

    getModifiedFunctionalPermissionMap: function () {
      return oProperties.oModifiedFunctionalPermissionMap;
    },

    setModifiedFunctionalPermissionMap: function (oModifiedFunctionalPermissionMap) {
      oProperties.oModifiedFunctionalPermissionMap = oModifiedFunctionalPermissionMap;
    },

    getActiveTabId: function () {
      return oProperties.sHierarchyTreeActiveTabId;
    },

    setActiveTabId: function (_sHierarchyTreeActiveTabId) {
      oProperties.sHierarchyTreeActiveTabId = _sHierarchyTreeActiveTabId;
    },


    reset: function (bIsSave) {
      if (!bIsSave) {
        oProperties.oActivePermission = {};
        oProperties.aSelectedIds = [];
        oProperties.bIsPermissionModeClass = true;
        oProperties.sSelectedFirstLevelClass = "";
        oProperties.sSelectedPropertyCollection = "";
        oProperties.sSelectedTemplate = "";
        oProperties.aEntityNavigationTreeData = [];
        oProperties.oEntityNavigationTreeVisualData = {};
        oProperties.sSelectedTreeItemId = "";
        oProperties.sSelectedEntityType = "";
        oProperties.oEntityChildrenPaginationData = {};
        oProperties.oHierarchyTree = {};
        oProperties.oHierarchyTreeVisualData = {};
        oProperties.oPermissionMap = {};
        oProperties.oModifiedPermissionMap = {};
        oProperties.oModifiedFunctionalPermissionMap = {};
        oProperties.bIsPermissionDirty = false;
        oProperties.sHierarchyTreeActiveTabId = "";
      }
      oProperties.bPermissionScreenLockStatus = false;
      delete oProperties.oActivePermission.clonedObject;
      delete oProperties.oActivePermission.isDirty;
    }
  };
})();

export default PermissionConfigViewProps;
