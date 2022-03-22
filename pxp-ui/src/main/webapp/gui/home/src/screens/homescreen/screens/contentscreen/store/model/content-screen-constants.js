/**
 * Created by DEV on 14-09-2015.
 */
import CommonModuleConstants from '../../../../../../commonmodule/tack/constants';

var ContentScreenConstants = (function () {
  return {

    entityModes:{
      ARTICLE_MODE: 'article_mode',

      ASSET_MODE: 'asset_mode',

      MARKET_MODE: 'market_mode',

      EDITORIAL_MODE: "editorial_mode",
      STATIC_COLLECTION_MODE: "static_collection_mode",
      DYNAMIC_COLLECTION_MODE: "dynamic_collection_mode",

      SUPPLIER_MODE: "supplier_mode",
      TEXTASSET_MODE: "textasset_mode",
    },

    viewModes: {
      /** Don't add any mode here */
      TILE_MODE: 'tile_mode',
      LIST_MODE: 'list_mode',
      JOB_SCREEN_MODE: 'job_screen_mode',
    },

    TREE_ROOT_ID: CommonModuleConstants.TREE_ROOT_ID,
    TAXONOMY_HIERARCHY_ALL_DUMMY_NODE: CommonModuleConstants.TAXONOMY_HIERARCHY_ALL_DUMMY_NODE,

    quicklistMode: {
      CHILDREN: "children",
      LINKED: "linked",
      MATCH: "match",
      COVERFLOW: "coverflow"
    },
    thumbnailModes : {
      BASIC : "basic",
      XRAY : "xray"
    },
    tabItems : {
      TAB_TIMELINE : CommonModuleConstants.TAB_TIMELINE,
      TAB_TASKS : CommonModuleConstants.TAB_TASKS,
      TAB_DASHBOARD: CommonModuleConstants.TAB_DASHBOARD,
      TAB_CUSTOM: CommonModuleConstants.TAB_CUSTOM,
      TAB_OVERVIEW: CommonModuleConstants.TAB_OVERVIEW,
      TAB_RENDITION: CommonModuleConstants.TAB_RENDITION,
      TAB_MDM: CommonModuleConstants.TAB_MDM,
      TAB_DUPLICATE_ASSETS: CommonModuleConstants.TAB_DUPLICATE_ASSETS,
      TAB_CONTEXT: CommonModuleConstants.TAB_CONTEXT,
      TAB_USAGES: "usage_tab",
      EMPTY : null
    },
    sectionTypes: {
      SECTION_TYPE_PROPERTY_COLLECTION: "propertyCollection",
      SECTION_TYPE_RELATIONSHIP: "relationship",
      SECTION_TYPE_NATURE_RELATIONSHIP: "natureRelationship",
      SECTION_TYPE_CONTEXT: "context",
      SECTION_TYPE_CONTEXT_SELECTION: "contextSelection",
      SECTION_TYPE_SCHEDULE_SELECTION: "scheduleSelection",
      SECTION_TYPE_PERFORMANCE_INDICES: "performanceIndices",
      SECTION_TYPE_CLASSIFICATION: "classification",
      SECTION_TYPE_LIFE_CYCLE_STATUS: "lifeCycleStatus",
      SECTION_TYPE_CLASS_TAG_INFO: "sectionClassTagInfo",
      SECTION_TYPE_LINKED_ITEM_BUTTON: "linkedItemButton",
      SECTION_TYPE_DOWNLOAD_INFO: "downloadInfo",
      SECTION_TYPE_INSTANCE_PROPERTIES: "instanceProperties"
    },

    relevance: "relevance",
    lastModifiedAttribute: "lastmodifiedattribute",
    TAXONOMY_HIERARCHY_ROOT: "taxonomyHierarchyRoot",
    unitTableContext: "UOM",
    FILTER_FOR_ASSET_EXPIRY: "filter_for_asset_expiry",
    MODULE_ID_CAN_NOT_BE_EMPTY: "Module id can not be empty",
    FILTER_FOR_DUPLICATE_ASSETS: "filter_for_duplicate_assets",

    UOM_BODY_GRID_MODE: "uom_body_grid_mode",
  };
})();

export default ContentScreenConstants;
