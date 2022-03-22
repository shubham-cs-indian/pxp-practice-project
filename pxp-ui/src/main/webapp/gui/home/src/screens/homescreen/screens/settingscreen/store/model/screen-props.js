/**
 * Created by DEV on 27-07-2015.
 */
import CS from '../../../../../../libraries/cs';

var ScreenProps = (function () {


  let Props = function () {
    return {
      sConfigScreenViewName: 'tags', //['tags', 'classes', 'attributes', relationships]
      oDummyAllAtrribueObject: {
        id : "all",
        label : "All",
        type : "all"
      },
      bListItemCreated: false,
      sEntitySearchText: "",
      oDefaultEntitiesPaginationData: {
        attributes: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        tags: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        roles: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        relationships: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        propertyCollections: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        taxonomies: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        attributeVariantContexts: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        tabs: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        templates: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        contexts: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
        natureRelationships: {from: 0, size: 20, sortBy: "label", sortOrder: "asc"},
      },
      oEntitiesPaginationData: {},
      oEntitiesTotalCounts: {},
      sGridViewContext: "",
      oGridViewSkeleton: {},
      aGridViewData: [],
      oGridViewVisualData: {},
      oGridViewPaginationData: {
        from: 0,
        pageSize: 20
      },
      iGridViewTotalItems: 0,
      iGridViewTotalNestedItems: 0,
      sGridViewSearchText: "",
      sGridViewSortBy: "",
      sGridViewSearchBy: "",
      sGridViewSortLanguage: "",
      sGridViewSortOrder: "",
      bIsGridDataDirty: false,
      aGridAppliedFilterData: [],
      bIsGridFilterView: false,
      oReferencedAttributes: {},
      oReferencedTags: {},
      oReferencedRoles: {},
      oReferencedContexts: {},
      oReferencedRelationships: {},
      oAttributesLoadedData: {},
      oTagsLoadedData: {},
      oRolesLoadedData: {},
      oPropertyCollapsedStatusMap: {
        attributes: false,
        tags: false,
        roles: false
      },
      oEntityVsSearchTextMapping: {
        "attributes": '',
        "tags": '',
        "roles": '',
        "propertyCollections": '',
        "taxonomies": "",
      },
      sSelectedTabId: "",
      aLeftNavigationTreeData: [],
      sSelectedNavigationTreeNode: "",
      oLeftNavigationTreeValuesMap: {},
      sSelectedNavigationParentNodeId: "",
      bIsScrollAutomaticallyEnabled: false,
      aConfigScreenTabLayoutData: [],
      oAssetExtensions: {
        imageAsset: [],
        videoAsset: [],
        documentAsset: [],
        allAssets: []
      },
      universalSearchList: [],
      sResizedColumnId: ''
    };
  };

  let oProperties = new Props();

  return {
    getListItemCreationStatus: function () {
      return oProperties.bListItemCreated;
    },

    setListItemCreationStatus: function (_bListItemCreated) {
      oProperties.bListItemCreated = _bListItemCreated;
    },

    getConfigScreenViewName: function () {
      return oProperties.sConfigScreenViewName;
    },

    setConfigScreenViewName: function (_sConfigScreenViewName) {
      oProperties.sConfigScreenViewName = _sConfigScreenViewName;
    },

    getDummyAllAttributeObject : function () {
      return oProperties.oDummyAllAtrribueObject;
    },

    getReferencedAttributes: function () {
      return oProperties.oReferencedAttributes;
    },

    getReferencedTags: function () {
      return oProperties.oReferencedTags;
    },

    getReferencedContexts: function () {
      return oProperties.oReferencedContexts;
    },

    getReferencedRoles: function () {
      return oProperties.oReferencedRoles;
    },

    getReferencedRelationships: function () {
      return oProperties.oReferencedRelationships;
    },

    setReferencedAttributes: function (_oReferencedAttributes) {
      oProperties.oReferencedAttributes = _oReferencedAttributes;
    },

    setReferencedTags: function (_oReferencedTags) {
      oProperties.oReferencedTags = _oReferencedTags;
    },

    setReferencedContexts: function (_oReferencedContexts) {
      oProperties.oReferencedContexts = _oReferencedContexts;
    },

    setReferencedRoles: function (_oReferencedRoles) {
      oProperties.oReferencedRoles = _oReferencedRoles;
    },

    setReferencedRelationships: function (_oReferencedRelationships) {
      oProperties.oReferencedRelationships = _oReferencedRelationships;
    },

    setEntitySearchText: function (_sEntitySearchText) {
      oProperties.sEntitySearchText = _sEntitySearchText;
    },

    getEntitySearchText : function () {
      return oProperties.sEntitySearchText;
    },

    getPropertyCollapsedStatusMap: function () {
      return oProperties.oPropertyCollapsedStatusMap;
    },

    setPropertyCollapsedStatusMap: function (_oPropertyCollapsedStatusMap) {
      oProperties.oPropertyCollapsedStatusMap = _oPropertyCollapsedStatusMap;
    },

    getEntitiesPaginationData : function () {
      if (CS.isEmpty(oProperties.oEntitiesPaginationData)) {
        this.resetEntitiesPaginationData();
      }
      return oProperties.oEntitiesPaginationData;
    },

    resetEntitiesPaginationData : function () {
      oProperties.sEntitySearchText = "";
      oProperties.oEntitiesPaginationData = CS.cloneDeep(oProperties.oDefaultEntitiesPaginationData);
    },

    getEntitiesTotalCounts: function () {
      return oProperties.oEntitiesTotalCounts;
    },

    setGridViewContext: function (_sGridViewContext) {
      oProperties.sGridViewContext = _sGridViewContext;
    },

    getGridViewContext : function () {
      return oProperties.sGridViewContext;
    },

    setGridViewSkeleton: function (_oGridViewSkeleton) {
      oProperties.oGridViewSkeleton = _oGridViewSkeleton;
    },

    getGridViewSkeleton : function () {
      return oProperties.oGridViewSkeleton;
    },

    setGridViewData: function (_aGridViewData) {
      oProperties.aGridViewData = _aGridViewData;
    },

    getGridViewData : function () {
      return oProperties.aGridViewData;
    },

    setGridViewVisualData: function (_oGridViewVisualData) {
      oProperties.oGridViewVisualData = _oGridViewVisualData;
    },

    getGridViewVisualData : function () {
      return oProperties.oGridViewVisualData;
    },

    setGridViewPaginationData: function (_oGridViewPaginationData) {
      oProperties.oGridViewPaginationData = _oGridViewPaginationData;
    },

    getGridViewPaginationData : function () {
      return oProperties.oGridViewPaginationData;
    },

    setIsGridFilterView: function (_bIsGridFilterView) {
      oProperties.bIsGridFilterView = _bIsGridFilterView;
    },

    getIsGridFilterView: function () {
      return oProperties.bIsGridFilterView;
    },

    setGridViewTotalItems: function (_iGridViewTotalItems) {
      oProperties.iGridViewTotalItems = _iGridViewTotalItems;
    },

    getGridViewTotalItems : function () {
      return oProperties.iGridViewTotalItems;
    },

    setGridViewTotalNestedItems: function (_iGridViewTotalNestedItems) {
      oProperties.iGridViewTotalNestedItems = _iGridViewTotalNestedItems;
    },

    getGridViewTotalNestedItems : function () {
      return oProperties.iGridViewTotalNestedItems;
    },

    setGridViewSearchText: function (_sGridViewSearchText) {
      oProperties.sGridViewSearchText = _sGridViewSearchText;
    },

    getGridViewSearchText : function () {
      return oProperties.sGridViewSearchText;
    },

    setGridViewSortBy: function (_sGridViewSortBy) {
      oProperties.sGridViewSortBy = _sGridViewSortBy;
    },

    getGridViewSortBy : function () {
      return oProperties.sGridViewSortBy;
    },

    setGridViewSearchBy: function (_sGridViewSearchBy) {
      oProperties.sGridViewSeachBy = _sGridViewSearchBy;
    },

    getGridViewSearchBy : function () {
      return oProperties.sGridViewSeachBy;
    },

    setGridViewSortLanguage: function (_sGridViewSortBy) {
      oProperties.sGridViewSortLanguage = _sGridViewSortBy;
    },

    getGridViewSortLanguage : function () {
      return oProperties.sGridViewSortLanguage;
    },

    setGridViewSortOrder: function (_sGridViewSortOrder) {
      oProperties.sGridViewSortOrder = _sGridViewSortOrder;
    },

    getGridViewSortOrder : function () {
      return oProperties.sGridViewSortOrder;
    },

    setIsGridDataDirty: function (_bIsGridDataDirty) {
      oProperties.bIsGridDataDirty = _bIsGridDataDirty;
    },

    getIsGridDataDirty: function () {
      return oProperties.bIsGridDataDirty;
    },

    setGridAppliedFilterData: function (_aGridAppliedFilterData) {
      oProperties.aGridAppliedFilterData = _aGridAppliedFilterData;
    },

    getGridAppliedFilterData: function () {
      return oProperties.aGridAppliedFilterData;
    },

    getLoadedAttributesData: function () {
      return oProperties.oAttributesLoadedData;
    },

    getLoadedTagsData: function () {
      return oProperties.oTagsLoadedData;
    },

    getLoadedRolesData: function () {
      return oProperties.oRolesLoadedData;
    },

    setLoadedAttributesData: function (_oAttributesLoadedData) {
      oProperties.oAttributesLoadedData = _oAttributesLoadedData;
    },

    setLoadedTagsData: function (_oTagsLoadedData) {
      oProperties.oTagsLoadedData = _oTagsLoadedData;
    },

    setLoadedRolesData: function (_oRolesLoadedData) {
      oProperties.oRolesLoadedData = _oRolesLoadedData;
    },

    setEntityVsSearchTextMapping: function (_oEntityVsSearchTextMapping) {
      oProperties.oEntityVsSearchTextMapping = _oEntityVsSearchTextMapping;
    },

    getEntityVsSearchTextMapping: function () {
      return oProperties.oEntityVsSearchTextMapping;
    },

    getSelectedTabId: function () {
      return oProperties.sSelectedTabId;
    },

    setSelectedTabId: function (sTabId) {
      oProperties.sSelectedTabId = sTabId;
    },

    getLeftNavigationTreeData: function () {
      return oProperties.aLeftNavigationTreeData;
    },

    setLeftNavigationTreeData: function (_aLeftNavigationTreeData) {
      oProperties.aLeftNavigationTreeData = _aLeftNavigationTreeData;
    },

    getLeftNavigationTreeValuesMap: function () {
      return oProperties.oLeftNavigationTreeValuesMap;
    },

    setLeftNavigationTreeValuesMap: function (_aLeftNavigationTreeValuesMap) {
      oProperties.oLeftNavigationTreeValuesMap = _aLeftNavigationTreeValuesMap;
    },

    getSelectedLeftNavigationTreeItem: function () {
      return oProperties.sSelectedNavigationTreeNode
    },

    setSelectedLeftNavigationTreeItem: function (_sItemId) {
      oProperties.sSelectedNavigationTreeNode = _sItemId;
    },

    getSelectedLeftNavigationTreeParentId: function () {
      return oProperties.sSelectedNavigationParentNodeId
    },

    setSelectedLeftNavigationTreeParentId: function (_sSelectedNavigationParentNodeId) {
      oProperties.sSelectedNavigationParentNodeId = _sSelectedNavigationParentNodeId;
    },

    setIsHierarchyTreeScrollAutomaticallyEnabled: function (bVal) {
      oProperties.bIsScrollAutomaticallyEnabled = bVal;
    },

    getIsHierarchyTreeScrollAutomaticallyEnabled: function () {
      return oProperties.bIsScrollAutomaticallyEnabled;
    },

    setConfigScreenTabLayoutData: function (_aConfigScreenTabLayoutData) {
      oProperties.aConfigScreenTabLayoutData = _aConfigScreenTabLayoutData;
    },

    getConfigScreenTabLayoutData: function () {
      return oProperties.aConfigScreenTabLayoutData;
    },

    getAssetExtensions: function () {
      return oProperties.oAssetExtensions;
    },

    setAssetExtensions: function (_oAssetExtensions) {
      oProperties.oAssetExtensions = _oAssetExtensions;
    },

    getUniversalSearchList: function () {
      return oProperties.universalSearchList;
    },

    setUniversalSearchList: function (_oUniversalSearchList) {
      oProperties.universalSearchList = _oUniversalSearchList;
    },

    getResizedColumnId: () => {
      return oProperties.sResizedColumnId;
    },

    setResizedColumnId: (sResizableId) => {
      oProperties.sResizedColumnId = sResizableId;
    },

    resetTranslationGridProps: function () {
      oProperties.oGridViewPaginationData = {
        from: 0,
        pageSize: 20
      };
      oProperties.sGridViewSearchText = '';
      oProperties.sGridViewSortBy = 'label';
      oProperties.sGridViewSeachBy = 'label';
    }
  };
})();

export default ScreenProps;