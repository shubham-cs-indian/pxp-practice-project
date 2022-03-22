import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const ContentToolbarItemLayoutData = function () {
  return {
    toolbarItems: {
      refresh: {
        id: "refresh",
        showSeparate: true,
        className: "toolRefresh",
        label: "REFRESH"
      },
      selectAll: {
        id: "selectAll",
        showSeparate: true,
        className: "toolCheckAll",
        label: "SELECT_ALL"
      },
      exportContents: {
        id: "exportContents",
        className: "toolExportContents",
        label: "EXPORT_CONTENTS"
      },
      importContents: {
        id: "importContents",
        className: "toolImportContents",
        label: "IMPORT_CONTENTS",
      },
      delete: {
        id: "delete",
        showSeparate: true,
        className: "toolDelete",
        label: "DELETE"
      },
      remove: {
        id: "remove",
        showSeparate: true,
        className: "toolRemove",
        label: "REMOVE"
      },
      comparison: {
        id: "comparison",
        showSeparate: true,
        className: "toolComparison",
        label: "COMPARISON_VIEW"
      },
      bulkEdit: {
        id: "bulkEdit",
        showSeparate: true,
        className: "toolBulkEdit",
        label: "BULK_EDIT"
      },
      gridEditContents: {
        id: "gridEditContents",
        showSeparate: true,
        className: "toolGridEditContents",
        label: "GRID_EDIT"
      },
      transferContents: {
        id: "transferContents",
        showSeparate: true,
        className: "tooltransferContents",
        label: "TRANSFER_CONTENTS"
      },
      addAll: {
        id: "addAll",
        showSeparate: true,
        className: "toolAddAll",
        label: "ADD"
      },
      onboardingFileUpload: {
        id: "onboardingFileUpload",
        showSeparate: true,
        className: "assetBulkUpload",
        label: "ONBOARDING_IMPORT_FILE"
      },
      cloneContent: {
        id: "cloneContent",
        showSeparate: true,
        className: "toolCloneContent",
        label: "CREATE_CLONE"
      },
      assetBulkDownload: {
        id: "assetBulkDownload",
        showSeparate: true,
        className: "assetBulkDownload",
        label: "BULK_DOWNLOAD_CONTENT_MENU_ITEM_TITLE"
      },
      assetBulkUpload: {
        id: "assetBulkUpload",
        showSeparate: true,
        className: "assetBulkUpload",
        label: "BULK_UPLOAD_CONTENT_MENU_ITEM_TITLE"
      },

      paste_hierarchy: {
        id: "paste_hierarchy",
        className: "toolPaste",
        label: "PASTE"
      },
      copy_hierarchy: {
        id: "copy_hierarchy",
        className: "toolCopy",
        label: "COPY"
      },
      cut_hierarchy: {
        id: "cut_hierarchy",
        className: "toolCut",
        label: "CUT"
      },
      save: {
        id: "save",
        showSeparate: true,
        className: "toolSave",
        label: "SAVE"
      },
      add_relationship_entities: {
        id: "add_relationship_entities",
        showSeparate: true,
        className: "toolAdd",
        label: "ADD_ENTITY"
      },
      selectAll_relationship_entities: {
        id: "selectAll_relationship_entities",
        showSeparate: true,
        className: "toolCheckAll",
        label: "SELECT_ALL"
      },
      delete_relationship_entities: {
        id: "delete_relationship_entities",
        showSeparate: true,
        className: "toolRemove",
        label: "REMOVE"
      },
      upload_relationship_entities: {
        id: "upload_relationship_entities",
        showSeparate: true,
        className: "assetBulkUpload",
        label: "BULK_UPLOAD_CONTENT_MENU_ITEM_TITLE"
      },
      restoreContents: {
        id: "restoreContents",
        showSeparate: true,
        className: "toolRestoreContents",
        label: "RESTORE"
      },

      compare_timeline_entities: {
        id: "compare_timeline_entities",
        className: "toolCompare",
        label: "COMPARE"
      },
      archive_timeline_entities: {
        id: "archive_timeline_entities",
        className: "toolArchive",
        label: "ARCHIVE"
      },
      delete_timeline_entities: {
        id: "delete_timeline_entities",
        className: "toolArchive",
        label: "DELETE"
      },
      restore_timeline_entities: {
        id: "restore_timeline_entities",
        className: "toolRestore",
        label: "RESTORE"
      },
      selectAll_timeline_entities: {
        id: "selectAll_timeline_entities",
        className: "toolCheckAll",
        label: "SELECT_ALL"
      },
      deselectAll_timeline_entities: {
        id: "deselectAll_timeline_entities",
        className: "toolUnCheckAll",
        label: "DESELECT_ALL"
      },
      zoomin_timeline_entities: {
        id: "zoomin_timeline_entities",
        className: "toolZoomIn",
        label: "ZOOM_IN"
      },
      zoomout_timeline_entities: {
        id: "zoomout_timeline_entities",
        className: "toolZoomOut",
        label: "ZOOM_OUT"
      },
      smartDocumentPresets: {
        id: "smartDocumentPresets",
        showSeparate: true,
        className: "smartDocumentPresets",
        label: oTranslations().GENERATE_SMART_DOCUMENT,
      },

      zoomin: {
        id: "zoomin",
        className: "toolZoomIn",
        label: "ZOOM_IN"
      },
      zoomout: {
        id: "zoomout",
        className: "toolZoomOut",
        label: "ZOOM_OUT"
      },
      toggleMode: {
        id: "toggleMode",
        className: "toolToggleMode",
        label: "X_RAY_VISION"
      },
      tile: {
        id: "tile",
        className: "toolView1",
        label: "TILE_VIEW"
      },
      list: {
        id: "list",
        className: "toolView2",
        label: "LIST_VIEW"
      },
      /*calendar: {
        id: "calendar",
        className: "toolCalendarView",
        label: "CALENDAR_VIEW"
      },*/
      zoomin_relationship_entities: {
        id: "zoomin_relationship_entities",
        className: "toolZoomIn",
        label: "ZOOM_IN"
      },
      zoomout_relationship_entities: {
        id: "zoomout_relationship_entities",
        className: "toolZoomOut",
        label: "ZOOM_OUT"
      },
      view_relationship_variants: {
        id: "view_relationship_variants",
        className: "toolToggleMode",
        label: "X-RAY",
      },
      tile_relationship_entities: {
        id: "tile_relationship_entities",
        className: "toolView1",
        label: "TILE_VIEW"
      },
      list_relationship_entities: {
        id: "list_relationship_entities",
        className: "toolView2",
        label: "LIST_VIEW"
      },
      tile_variant_entities: {
        id: "tile_variant_entities",
        className: "toolView1",
        label: "TILE_VIEW"
      },
      grid_variant_entities: {
        id: "grid_variant_entities",
        className: "toolView3",
        label: "GRID_VIEW"
      },
      timeline_comparison_properties: {
        id: "timeline_comparison_properties",
        //className: "toolTimelineComparisonProperties",
        label: oTranslations().PROPERTIES,
        showLabel: true
      },
      timeline_comparison_relationship: {
        id: "timeline_comparison_relationship",
        //className: "toolTimelineComparisonRelationship",
        label: oTranslations().RELATIONSHIPS,
        showLabel: true
      },
      assetBulkShare: {
        id: "assetBulkShare",
        showSeparate: true,
        className: "assetBulkShare",
        label: "BULK_SHARE_CONTENT_MENU_ITEM_TITLE"
      },

    }
  };
}

export default ContentToolbarItemLayoutData;