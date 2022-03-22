/**
 * Created by CS80 on 10/10/2016.
 */

export default {
  BasicToolbarItems: {
    leftItemList : () => ["refresh", "selectAll", "exportContents", "delete", "comparison", "bulkEdit", "gridEditContents", "transferContents", "smartDocumentPresets"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },

  AvailableEntityTileView: {
    leftItemList: () => [ "refresh", "selectAll", "addAll"],
    rightItemList: () => ["toggleMode"]
  },

  FileModule: {
    leftItemList: () => [ "refresh", "selectAll", "delete", "onboardingFileUpload"],
    rightItemList: () => [ "zoomin", "zoomout", "tile", "list"]
  },

  PimModule: {
    leftItemList: () => ["refresh", "selectAll", "importContents", "exportContents", "delete", "cloneContent", "comparison", "bulkEdit", "gridEditContents", "transferContents", "smartDocumentPresets"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },

  DamModule: {
    leftItemList: () => ["refresh", "selectAll", "exportContents", "assetBulkDownload", "assetBulkUpload", "delete", "comparison", "bulkEdit", "gridEditContents", "transferContents", "assetBulkShare" ],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },

  hierarchyToolbarItemList: {
    leftItemList: () => [ "selectAll"],
    rightItemList: () => [ "paste_hierarchy", "copy_hierarchy", "cut_hierarchy", "zoomin", "zoomout", "toggleMode", "tile", "list"]
  },

  EditToolbarItems: {
    leftItemList: () => [ "refresh" ],
    rightItemList: () => [],
  },

  RelationshipToolbar: {
    leftItemList: () => [ "add_relationship_entities", "selectAll_relationship_entities", "delete_relationship_entities", "upload_relationship_entities"],
    rightItemList: () => ["zoomin_relationship_entities", "zoomout_relationship_entities", "view_relationship_variants", "tile_relationship_entities", "list_relationship_entities"]
  },

  PimArchivalToolbarItems: {
    leftItemList: () => ["refresh", "selectAll", "restoreContents", "delete"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },

  TimeLineToolbarData: {
    leftItemList: () => ["compare_timeline_entities", "archive_timeline_entities", "delete_timeline_entities", "restore_timeline_entities", "selectAll_timeline_entities"],
    rightItemList: () => ["zoomin_timeline_entities", "zoomout_timeline_entities"]
  },

  TimelineComparisonDialogToolbarData: {
    leftItemList: () => [],
    rightItemList: () => ["timeline_comparison_properties", "timeline_comparison_relationship"]
  },

  BundleSectionToolbarData: {
    selectAll: [
      {
        id: "selectAll",
        className: "toolCheckAll",
        label: "SELECT_ALL"
      },
    ],

    remove: [
      {
        id: "delete",
        className: "toolRemove",
        label: "REMOVE"
      },
    ],

    transfer: [
      {
        id: "transferMarkets",
        className: "toolTransferMarkets",
        label: "TRANSFER_MARKETS_LABEL"
      }
    ]
  },

  GoldenRecordToolbarItems: {
    leftItemList: () => ["refresh"],
    rightItemList: () => [],
  },

  KPIExplorerScreen: {
    leftItemList: () => ["refresh", "selectAll", "delete", "comparison", "bulkEdit", "gridEditContents", "transferContents"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]

  },

  InformationTabRuleViolationScreen: {
    leftItemList: () => ["refresh", "selectAll", "delete", "comparison"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },

  StaticCollectionScreen: {
    leftItemList: () => ["refresh", "selectAll", "exportContents", "remove", "delete", "comparison", "cloneContent", "bulkEdit", "gridEditContents", "transferContents"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },

  DynamicCollectionScreen: {
    leftItemList: () => [ "refresh", "selectAll", "exportContents", "delete", "cloneContent", "comparison", "bulkEdit", "gridEditContents", "transferContents"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },

  DuplicateAssetsTabMode: {
    leftItemList: () => ["refresh"],
    rightItemList: () => []
  },

  TargetData: {
    leftItemList : () => ["refresh", "selectAll", "exportContents", "delete", "comparison", "cloneContent", "bulkEdit", "gridEditContents", "transferContents", "smartDocumentPresets"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },
  TextAssetData: {
    leftItemList : () => ["refresh", "selectAll", "exportContents", "delete", "comparison", "cloneContent", "bulkEdit", "gridEditContents", "transferContents", "smartDocumentPresets"],
    rightItemList: () => ["zoomin", "zoomout", "toggleMode", "tile", "list"]
  },
};

