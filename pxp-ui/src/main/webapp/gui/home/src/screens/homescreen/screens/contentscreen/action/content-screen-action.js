import CS from '../../../../../libraries/cs';
import MethodTracker from '../../../../../libraries/methodtracker/method-tracker';
import LogFactory from '../../../../../libraries/logger/log-factory';

import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ActionInterceptor from '../../../../../libraries/actioninterceptor/action-interceptor.js';
import {events as KeydownEvents} from '../../../../../libraries/keydownhandler/keydownhandler';
import {events as ThumbnailEvents} from '../../../../../viewlibraries/thumbnailview2/thumbnail-view.js';
import {events as ThumbnailTemplateViewEvents} from '../../../../../viewlibraries/thumbnailview2/templates/thumbnail-template-view-new';
import {events as ThumbnailActionItemEvents} from '../../../../../viewlibraries/thumbnailview2/templates/thumbnail-action-item-view';
import {events as BreadcrumbViewEvent} from '../../../../../viewlibraries/breadcrumbview/breadcrumb-wrapper-view.js';
import {events as TagTreeParentNodeViewEvents} from '../../../../../viewlibraries/taggroupview/tag-node-view.js';
import {events as TagTreeGroupViewEvents} from '../../../../../viewlibraries/taggroupview/tag-group-view.js';
import {events as DragViewEvents} from '../../../../../viewlibraries/dragndropview/drag-view.js';
import {events as DropViewEvents} from '../../../../../viewlibraries/dragndropview/drop-view.js';
import {events as DragNDropViewEvents} from '../../../../../viewlibraries/dragndropview/drag-n-drop-view';
import {events as MultiSelectViewNewEvents} from '../../../../../viewlibraries/multiselectview/core-multi-select-search-view.js';
import { events as PaperViewEvents } from '../../../../../viewlibraries/paperview/paper-view.js';
import { events as ListNodeViewEvents } from '../../../../../viewlibraries/listview/list-node-view.js';
import { events as SingleSelectSearchViewEvents } from '../../../../../viewlibraries/uniqueSelectionView/unique-selection-view.js';
import { events as FileDragAndDropViewEvents } from '../../../../../viewlibraries/filedraganddropview/file-drag-and-drop-view';
import { events as BulkUploadButtonViewEvents } from '../view/bulk-upload-button-view';
import { events as BulkDownloadAndLinkSharingButtonViewEvents } from '../view/bulk-asset-link-sharing-view';
import { events as OnboardingFileUploadButtonViewEvents } from '../view/onboarding-file-upload-button-view';
import { events as ContentTileListViewEvents } from '../view/content-tile-list-view';
import { events as ContentTileEvents } from '../view/content-tile-view';
import { events as EntityDetailViewEvents } from '../view/entity-detail-view';
import { events as ContentTabSpecificViewEvents} from '../view/content-tab-specific-view';
import { events as EntityDetailRelationshipSectionViewEvents } from '../view/entity-detail-relationship-section-view';
import { events as AddTaxonomyPopoverViewEvents } from '../../../../../viewlibraries/addtaxonomypopoverview/add-taxonomy-popover-view';
import { events as SmallTaxonomyViewEvents } from '../../../../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import { events as QuickListView } from '../view/quicklist-view';
import { events as CustomFroalaViewEvents } from '../../../../../viewlibraries/customfroalaview/custom-froala-view';
import { events as ButtonWithContextMenuViewEvents } from '../../../../../viewlibraries/buttonwithcontextmenuview/button-with-context-menu-view';
import { events as ContentFilterElementsViewEvents } from '../view/content-filter-elements-view';
import { events as CreateButtonViewEvents } from '../../../../../viewlibraries/createbuttonview/create-button-view';
import { events as FlatCreateButtonViewEvents } from '../../../../../viewlibraries/createbuttonview/flat-create-button-view';
import { events as CloneWizardViewNewEvents } from '../view/clone-wizard-view-new';
import { events as UOMViewEvents } from '../view/uom-view';
import { events as ContentSectionViewNewEvents } from '../view/content-section-view-new';
import { events as RelationshipSectionElementViewEvents } from '../view/relationship-section-element-view';
import { events as ContentSectionAttributeViewEvents } from '../view/content-section-attribute-view';
import { events as ContentSectionElementViewNewEvents } from '../view/content-section-element-view-new';
import ContentScreenStore from '../store/content-screen-store';
import {events as ContentThumbnailContainerEvents} from '../view/content-thumbs-container-view';
import {events as ContentMeasurementMetricsEvents} from '../../../../../viewlibraries/measurementmetricview/content-measurement-metrics-view.js';
import {events as AppliedFilterViewEvents} from '../../../../../viewlibraries/filterview/fltr-applied-filter-view';
import {events as AdvancedSearchPanelViewEvents} from '../view/advanced-search-panel-view';
import {events as ContentHorizontalTreeViewEvents} from '../../../../../viewlibraries/horizontaltreeview/horizontal-tree-view';
import {events as FilterViewEvents} from '../view/filter-view';
import {events as FltrTaxonomySelectorViewEvents} from '../view/fltr-taxonomy-selector-view';
import {events as FltrSortByViewEvents} from './../view/fltr-sort-by-view';
import {events as AvailableFilterItemViewEvents} from '../../../../../viewlibraries/filterview/fltr-available-filter-item-view';
import {events as AvailableFilterItemWrapperViewEvents} from '../../../../../viewlibraries/filterview/fltr-available-item-wrapper-view';
import {events as FltrSearchBarViewEvents} from '../view/fltr-search-bar-view';
import {events as FltrHorizontalTreeViewEvents} from '../view/fltr-horizontal-tree-view';
import {events as FltrAvailableFiltersViewEvents} from '../../../../../viewlibraries/filterview/fltr-available-filters-view';
import {events as ContentHierarchyTaxonomySectionsViewEvents} from '../view/content-hierarchy-taxonomy-sections-view';
import {events as CollectionOptionsViewEvents} from '../view/fltr-collection-options-view';
import {events as CollectionCreationViewEvents} from '../view/collection-creation-view';
import {events as HeaderViewEvents} from '../../../../../viewlibraries/headerView/header-view';
import {events as EntitySnackBarViewEvents} from './../../../../../viewlibraries/snackbarview/entity-snack-bar-view';
import {events as SelectionToggleViewEvents} from '../../../../../viewlibraries/selectiontoggleview/selection-toggle-view';
import {events as XRayButtonViewEvents} from '../view/x-ray-button-view';
import {events as XRaySettingsViewEvents} from '../view/x-ray-settings-view';
import {events as ContentInformationSidebarViewEvents} from '../view/content-information-sidebar-view';
import {events as ContentImageWrapperViewEvents} from '../view/content-image-wrapper-view';
import {events as ContentEditViewEvents} from '../view/content-edit-view';
import {events as ContentEditToolbarViewEvents} from '../view/content-edit-toolbar-view';
import {events as VariantDialogViewEvents} from '../view/variant-dialog-view';
import {events as VariantLinkedEntitiesView} from '../../../../../viewlibraries/variantlinkedentitiesview/variant-linked-entities-view';
import {events as TableBodyViewEvents} from './../../../../../viewlibraries/tableview/table-body-view';
import {events as TableCellViewEvents} from './../../../../../viewlibraries/tableview/table-cell-view';
import {events as ImageGalleryDialogViewEvents} from './../../../../../viewlibraries/imagegallerydialogview/image-gallery-dialog-view';
import {events as CircledTagGroupViewEvents} from './../../../../../viewlibraries/circledtaggroupview/circled-tag-group-view';
import {events as ThumbnailScrollerViewEvents} from './../../../../../viewlibraries/thumbnail-scroller/thumbnail-scroller';
import {events as ThumbnailInformationViewEvents} from './../../../../../viewlibraries/thumbnailview2/templates/thumbnail-information-view';
import {events as ThumbnailAddToCollectionViewEvents} from './../../../../../viewlibraries/thumbnailview2/templates/thumbnail-add-to-collection-view';
import {events as PropertyTasksEditButtonViewEvents} from '../view/property-tasks-edit-button-view';
import {events as VariantImageSelectionViewEvents} from './../../../../../viewlibraries/imagegallerydialogview/variant-image-selection-view';
import {events as ActionableChipsViewEvents} from './../../../../../viewlibraries/actionablechipsview/actionable-chips-view';
import ContentUtils from './../store/helper/content-utils';
import ContentScreenConstants from './../store/model/content-screen-constants';
import {events as NatureThumbnailViewEvents} from './../view/nature-thumbnail-view';
import {events as NatureCommonSectionViewEvents} from './../view/nature-common-section-view';
import {events as NatureRelationshipViewEvents} from './../view/nature-relationship-view';
import { events as EndpointMappingViewEvents} from './../view/endpoint-mapping-view';
import {events as JobItemViewEvents} from './../view/job-item-view';
import {events as TimelineEvents} from './../view/content-timeline-view';
import {events as TimelineComparisonEvents} from './../view/content-timeline-comparison-view';
import {events as ContentGridEditViewEvents} from './../view/content-grid-edit-view';
import {events as ProcessRuntimeGraphView} from './../view/process-runtime-graph-view';
import {events as VariantTagGroupViewEvents} from '../../../../../viewlibraries/varianttagsummaryview/variant-tag-group-view';
import {events as GridPropertyViewEvents} from '../../../../../viewlibraries/gridview/grid-property-view';
import {events as GridImagePropertyViewEvents} from '../../../../../viewlibraries/gridview/grid-image-property-view';
import {events as GridViewEvents} from './../../../../../viewlibraries/gridview/grid-view';
import {events as TableViewEvents} from './../../../../../viewlibraries/tableview/table-view';
import {events as MatchMergeAttributeCellViewEvents} from '../../../../../viewlibraries/matchandmergeviewnew/match-and-merge-attribute-cell-view';
import {events as MatchMergeCommonCellViewEvents} from '../../../../../viewlibraries/matchandmergeviewnew/match-and-merge-common-cell-view';
import {events as MatchMergeTagViewEvents} from '../../../../../viewlibraries/matchandmergeview/match-and-merge-tag-view';
import {events as MatchMergeRelationshipViewEvents} from '../../../../../viewlibraries/matchandmergeview/match-and-merge-relationship-view';
import {events as MatchMergeViewEvents} from '../../../../../viewlibraries/matchandmergeviewnew/match-and-merge-view';
import {events as MatchMergeHeaderViewEvents} from '../../../../../viewlibraries/matchandmergeviewnew/match-and-merge-header-view';
import {events as AttributeContextViewEvents} from '../../../../../viewlibraries/attributecontextview/attribute-context-view';
import {events as AttributeContextDialogViewEvents} from '../../../../../viewlibraries/attributecontextview/attribute-context-dialog-view';
import {events as ItemListPanelViewEvents} from '../../../../../viewlibraries/itemListPanelView/item-list-panel-view';
import {events as BulkEditViewEvents} from '../view/bulk-edit-view';
import {events as PhysicalCatalogSelectorEvents} from '../../../../../viewlibraries/physicalcatalogselectorview/physical-catalog-selector-view';
import {events as ContentLinkedSectionsViewEvents} from '../view/content-linked-sections-view';
import {events as KpiSummaryTileViewEvents} from '../../../../../viewlibraries/kpisummaryview/kpi-summary-tile-view';
import {events as ContentScreenWrapperViewEvents} from '../view/content-screen-wrapper-view';
import {events as DashboardEndpointTileViewEvents} from '../screens/dashboardscreen/view/dashboard-endpoint-tile-view';
import {events as DashboardViewEvents} from '../../../../../viewlibraries/dashboardview/dashboard-view';
import {events as DashboardTileViewEvents} from '../../../../../viewlibraries/dashboardview/dashboard-tile-view';
import {events as ThumbnailGoldenRecordBucketViewEvents} from '../../../../../viewlibraries/thumbnailview2/thumbnail-golden-record-bucket-view';
import {events as TabLayoutViewEvents} from './../../../../../viewlibraries/tablayoutview/tab-layout-view';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import {events as InformationTabViewEvents} from '../view/information-tab-view';
import {events as UploadAssetDialogViewEvents} from '../view/upload-asset-dialog-view';
import {events as CommonConfigSectionViewEvents} from '../view/common-config-section-replica-for-collection-edit';
import {events as HomeScreenCommunicator} from '../../../store/home-screen-communicator';
import {events as TaskEvents} from './../../../../../smartviewlibraries/taskview/store/helper/task-store';
import {events as ContentComparisonDialogViewEvents} from '../../../../../viewlibraries/contentcomparisonview/content-comparison-dialog-view';
import {events as ContentComparisonFullScreenViewEvents} from '../../../../../viewlibraries/contentcomparisonview/content-comparison-fullscreen-view';
import {events as ContentComparisonViewEvents} from '../../../../../viewlibraries/contentcomparisonview/content-comparison-view';
import {events as ContentComparisonMatchAndMergeViewEvents} from '../../../../../viewlibraries/contentcomparisonview/content-comparison-mnm-view';
import {events as ContentComparisonMatchAndMergeRelationshipEvents} from '../../../../../viewlibraries/contentcomparisonview/content-comparison-mnm-relationship-view';
import {events as StepperViewEvents} from '../../../../../viewlibraries/stepperview/stepper-view';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
import {events as ButtonViewEvent} from '../../../../../viewlibraries/buttonview/button-view';
import CONSTANTS from '../../../../../commonmodule/tack/constants';
import { events as RelationshipCouplingConflictEvent } from '../view/relationship-conflict-coupling-view';
import { events as TransferDialogViewEvents } from '../../../../../viewlibraries/transferDialogView/transfer-dialog-view';
import {events as TaxonomyInheritanceViewEvents} from '../view/taxonomy-inheritance-view';
import { events as CustomDateRangePickerViewEvents } from '../../../../../viewlibraries/customdaterangepickerview/custom-date-range-picker-view';
import { events as MultiClassificationSectionViewEvents } from '../view/multiclassification-section-view';
import { events as MultiClassificationViewEvents } from '../view/multiclassification-view';
import { events as TreeViewEvents } from '../../../../../viewlibraries/treeviewnew/tree-view';
import { events as BulkEditAppliedElementsViewEvents } from '../view/bulk-edit-applied-properties-view';
import { events as DragDropContextView } from '../../../../../viewlibraries/draggableDroppableView/drag-drop-context-view';
import {bulkEditTabTypesConstants as BulkEditTabTypesConstants} from  "../tack/bulk-edit-layout-data";
import {events as ColumnOrganizerDialogEvents} from "../../../../../viewlibraries/columnorganizerview/column-organizer-dialog-view";
import {events as BulkDownloadDialogEvents} from '../view/bulk-download-dialog-view';
import {events as StartAndEndDatePicketViewEvents} from '../view/start-and-end-date-picker-view';
import {events as ExpandableNestedMenuListViewEvents} from "../../../../../viewlibraries/expandablenestedmenulistview/expandable-nested-menu-list-view";
import {events as AllCategoriesSelectorViewEvents} from '../view/all-categories-selector-view';
import {events as AllCategoriesSelectorSummaryViewEvents} from '../view/all-categories-taxonomies-selector-summary-view';

var trackMe = MethodTracker.getTracker('ContentScreenAction');
var logger = LogFactory.getLogger('content-screen-action');

var ContentScreenAction = (function () {

  const oEventHandler = {};

  var handleThumbnailSingleClicked = function (oEvent, oModel, oFilterContext) {

    if (oEvent.extraData && oEvent.extraData.isViolationClicked) {
      /****************TODO: Refactor getIsMarketingContentCreate Check********************/
      oModel.properties["isViolationClicked"] = true;
      delete oEvent.extraData;
    }

    if (oModel.properties["isHorizontal"]) {
      ContentScreenStore.handleHorizontalThumbnailViewClicked(oModel, oFilterContext);
    } else {
      ContentScreenStore.handleThumbnailOpenClicked(oModel, oFilterContext);
    }
  };

  var toggleThumbnailSelection = function (oModel) {
    ContentScreenStore.toggleThumbnailSelection(oModel);
  };

  var handleThumbnailCheckboxClicked = function (oContext, oModel) {
    trackMe('handleThumbnailCheckboxClicked');

    var bIsHorizontal = oModel.properties['isHorizontal'];
    if (bIsHorizontal) {
      var bClickWithControl = true;
      ContentScreenStore.handleHorizontalThumbnailCheckboxClicked(oModel, bClickWithControl)
    } else {
      toggleThumbnailSelection(oModel);
    }
  };

  var handleThumbnailDeleteIconClicked = function (oContext, oModel, oFilterContext) {
    trackMe('handleThumbnailDeleteIconClicked');
    logger.info('handleThumbnailDeleteIconClicked: Thumbnail Delete clicked', {});
    if (oModel.properties["isHorizontal"]) {
      ContentScreenStore.handleHorizontalThumbnailDeleteClicked(oModel, oFilterContext);
    } else {
      ContentScreenStore.handleThumbnailDeleteIconClicked(oModel, oFilterContext);
    }
  };

  var handleThumbnailCreateVariantIconClicked = function (oContext, oModel) {
    let sVariantType = '';
    let oRequestModel = {
      "id": oModel.id,
      "klassInstanceId": oModel.id,
      "type": sVariantType
    };
    ContentScreenStore.handleThumbnailCreateVariantIconClicked(oRequestModel);
  };

  let handleThumbnailRestoreClicked = function (sId) {
    ContentScreenStore.handleThumbnailRestoreClicked(sId);
  };

  var handleThumbnailCloneIconClicked = function (oContext, oModel, oFilterContext) {
    ContentScreenStore.handleThumbnailCloneIconClicked(oModel, oFilterContext);
  };

  var handleThumbnailRelationshipEditVariantIconClicked = function (oContext, oModel) {
    ContentScreenStore.handleThumbnailRelationshipEditVariantIconClicked(oContext, oModel);
  };

  var handleThumbnailRightClick = function (oModel) {
    ContentScreenStore.handleThumbnailRightClick(oModel);
  };

  var handleThumbnailContextMenuPopOverClosed = function () {
    ContentScreenStore.handleThumbnailContextMenuPopOverClosed();
  };

  var handleThumbnailContextMenuItemClicked = function (sMenuItemId) {
    ContentScreenStore.handleThumbnailContextMenuItemClicked(sMenuItemId);
  };

  let handleEditViewFilterOptionChanged = (sId) => {
    ContentScreenStore.handleEditViewFilterOptionChanged(sId);
  };

  let handleAcrolinxResultChecked = (sScoreValue, sScoreCardUrl) => {
    ContentScreenStore.handleAcrolinxResultChecked(sScoreValue, sScoreCardUrl);
  };

  var handleAddEntityButtonClicked = function (oContext, sSectionId, oRelationshipSide, sSelectedContext) {
    trackMe('handleAddEntityButtonClicked');
    ContentScreenStore.handleAddEntityButtonClicked(sSectionId, oRelationshipSide, sSelectedContext);
  };

  var handleFileAttachmentUploadClicked = function (sContext, aFiles, oCallback) {
    return ContentScreenStore.handleFileAttachmentUploadClicked(sContext, aFiles, oCallback);
  };

  var handleOpenProductFromDashboard = function (sId, sBaseType, oFilterContext) {
    ContentScreenStore.handleOpenProductFromDashboard(sId, sBaseType, oFilterContext);
  };

  var handleRelationshipPresentEntityOkButtonClicked = function (sContext) {
    trackMe('handleRelationshipPresentEntityOkButtonClicked');
    ContentScreenStore.handleRelationshipPresentEntityOkButtonClicked(sContext);
  };

  var handlePresentEntityDeleteButtonClicked = function (sSelectedSectionModelId, sViewContext, oFilterContext) {
    trackMe('handlePresentEntityDeleteButtonClicked');
    ContentScreenStore.handlePresentEntityDeleteButtonClicked(sSelectedSectionModelId, sViewContext, oFilterContext);
  };

  var handlePresentEntitySelectAllButtonClicked = function (oContext, sRelationshipId, sViewContext) {
    trackMe('handlePresentEntitySelectAllButtonClicked');
    ContentScreenStore.handlePresentEntitySelectAllButtonClicked(sRelationshipId, sViewContext);
  };

  var handlePresentEntityPaginationChanged = function (sRelationshipId, oPaginationData, oFilterContext) {
    if (!CS.isEmpty(sRelationshipId)) {
      handleRelationshipLoadMore(null, sRelationshipId, "", oPaginationData);
    } else {
      var bIsForPresentEntity = true;
      ContentScreenStore.handleContentTileScrolled("", bIsForPresentEntity, oPaginationData, "", oFilterContext);
    }
  };

  var handleSaveContentsButtonClicked = function () {
    trackMe('handleSaveContentsButtonClicked');
    ContentScreenStore.handleSaveContentsButtonClicked();
  };

  var handleDiscardUnSavedContentsButtonClicked = function () {
    trackMe('handleDiscardUnSavedContentsButtonClicked');
    ContentScreenStore.handleDiscardUnSavedContentsButtonClicked();
  };

  var handleAdvanceSearchClearButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleClearFilterButtonClicked(oFilterContext);
  };

  var handleSelectAllContentClicked = function () {
    trackMe('handleSelectAllContentClicked');
    ContentScreenStore.handleSelectAllContentClicked();
  };

  var handleSelectAllRelationshipEntityClicked = function (sRelationshipId, sViewContext) {
    trackMe('handleSelectAllRelationshipEntityClicked');
    ContentScreenStore.handleSelectAllRelationshipEntityClicked(sRelationshipId, sViewContext);
  };

  var handleViewRelationshipVariantXRayClicked = function (sRelationshipId) {
    trackMe('handleViewRelationshipVariantXRayClicked');
    ContentScreenStore.handleViewRelationshipVariantXRayClicked(sRelationshipId);
  };

  var handleRefreshContentButtonClicked = function (oFilterContext) {
    trackMe('handleRefreshContentsButtonClicked');
    ContentScreenStore.handleRefreshContentButtonClicked(oFilterContext);
  };

  var handleAssetBulkDownloadButtonClicked = function () {
    trackMe('handleAssetBulkDownloadButtonClicked');
    ContentScreenStore.handleAssetBulkDownloadButtonClicked();
  };

  let handleAssetBulkShareButtonClicked = function () {
    trackMe('handleAssetBulkShareButtonClicked');
    ContentScreenStore.handleAssetBulkShareButtonClicked();
  };

  var handleAddAllButtonClicked = function (oFilterContext) {
    trackMe('handleAddAllButtonClicked');
    ContentScreenStore.handleAddAllButtonClicked(oFilterContext);
  };

  var handleComparisonButtonClicked = function () {
    trackMe('handleComparisonButtonClicked');
    ContentScreenStore.handleComparisonButtonClicked();
  };

  var handleDeleteContentsButtonClicked = function () {
    ContentScreenStore.handleDeleteContentsButtonClicked();
  };

  var handleRemoveContentsButtonClicked = function (oContext, oModel, oFilterContext) {
    if (!CS.isEmpty(oModel) && oModel.properties["isHorizontal"]) {
      ContentScreenStore.handleHorizontalThumbnailDeleteClicked(oModel, oFilterContext);
    } else {
      ContentScreenStore.handleRemoveContentsButtonClicked(oModel, oFilterContext);
    }
  };

  var handleDeleteAllRelationshipEntityClicked = function (sRelationshipId, sViewContext) {
    trackMe('handleDeleteAllRelationshipEntityClicked');
    ContentScreenStore.handleDeleteAllRelationshipEntityClicked(sRelationshipId, sViewContext);
  };

  var handleContentAssetBulkUploadButtonClicked = function (sId, oFiles, aCollectionIds, sKlassId, oFilterContext) {
    trackMe('handleContentAssetBulkUploadButtonClicked');
    ContentScreenStore.handleContentAssetBulkUploadButtonClicked(sId, oFiles, aCollectionIds, sKlassId, false, oFilterContext);
  };

  var handleBulkUploadToCollectionClicked = function () {
    trackMe('handleBulkUploadToCollectionClicked');
    ContentScreenStore.handleBulkUploadToCollectionClicked();
  };

  let handleBulkAssetUploadFromRelationshipClicked = function (sContext, oFilterContext) {
    ContentScreenStore.handleBulkAssetUploadFromRelationshipClicked(sContext, oFilterContext);
  };

  var handleBulkUploadToCollectionClosed = function () {
    trackMe('handleBulkUploadToCollectionClosed');
    ContentScreenStore.handleBulkUploadToCollectionClosed();
  };

  let handleBulkAssetLinkSharingActionItemClicked = function (sButtonId, oAssetDownloadData) {
    ContentScreenStore.handleBulkAssetLinkSharingActionItemClicked(sButtonId, oAssetDownloadData);
  };

  var handleOnboardingFileUploaded = function (aFiles, oExtraDataForUpload) {
    ContentScreenStore.handleOnboardingFileUploaded(aFiles, oExtraDataForUpload);
  };

  let handleOnboardingFileUploadCancel = function () {
    ContentScreenStore.handleOnboardingFileUploadCancel();
  };

  var handleContentToolbarItemClicked = function (sId, oViewContext, oFilterContext) {
    var sElementWorkingId = "";
    if (oViewContext && oViewContext.elementWorkingId) {
      sElementWorkingId = oViewContext.elementWorkingId;
    }

    var sViewMode;
    switch (sId) {
      case 'refresh':
        ActionInterceptor('Refresh contents', handleRefreshContentButtonClicked.bind(this, oFilterContext))();
        break;
      case 'assetBulkDownload':
        ActionInterceptor('Asset Bulk download', handleAssetBulkDownloadButtonClicked)();
        break;
      case 'addAll':
        ActionInterceptor('Add All', handleAddAllButtonClicked.bind(this, oFilterContext))();
        break;
      case 'delete':
        ActionInterceptor('Delete Selected Contents', handleDeleteContentsButtonClicked)();
        break;
      case 'remove':
        ActionInterceptor('Remove Selected Contents', handleRemoveContentsButtonClicked.bind(this, this, {}, oFilterContext))();
        break;
      case 'selectAll':
        ActionInterceptor('Select All Contents', handleSelectAllContentClicked)();
        break;
      case 'save':
        ActionInterceptor('Save Contents', handleSaveContentsButtonClicked)();
        break;
      case 'comparison':
        ActionInterceptor('Branch Off', handleComparisonButtonClicked)();
        break;

      case 'zoomin':
      case 'zoomout':
        ActionInterceptor('Zoom In/Out', ContentScreenStore.handleZoomToolClicked.bind(this, sId))();
        break;

      case 'tile':
        sViewMode = ContentScreenConstants.viewModes.TILE_MODE;
        ActionInterceptor('Switch Content View', handleSwitchContentViewTypeClicked.bind(this, sViewMode, oFilterContext))();
        break;

      case 'list':
        sViewMode = ContentScreenConstants.viewModes.LIST_MODE;
        ActionInterceptor('Switch Content View', handleSwitchContentViewTypeClicked.bind(this, sViewMode, oFilterContext))();
        break;

      case 'toggleMode':
        ActionInterceptor('Toggle Thumbnail Mode', ContentScreenStore.handleToggleThumbnailModeClicked.bind(this, "", oFilterContext))();
        break;

      case 'discard':
        ActionInterceptor('Discard UnSaved Contents', handleDiscardUnSavedContentsButtonClicked)();
        break;

        //  TODO: Special handling for relationship

      case 'delete_relationship_entities':
        ActionInterceptor('Delete Selected Contents', handleDeleteAllRelationshipEntityClicked.bind(this, sElementWorkingId, oViewContext.viewContext))();
        break;
      case 'selectAll_relationship_entities':
        ActionInterceptor('Select All Contents', handleSelectAllRelationshipEntityClicked.bind(this, sElementWorkingId, oViewContext.viewContext))();
        break;

      case 'view_relationship_variants':
        ActionInterceptor('View Relationship Variant Tags', handleViewRelationshipVariantXRayClicked.bind(this, sElementWorkingId))();
        break;

      case 'zoomin_relationship_entities':
      case 'zoomout_relationship_entities':
        var sKey = sId.split("_")[0]; //To send zoomin and zoomout as key
        ActionInterceptor('Zoom In/Out', ContentScreenStore.handleZoomToolClicked.bind(this, sKey, oViewContext.viewContext, sElementWorkingId))();
        break;

      case 'tile_relationship_entities':
        sViewMode = ContentScreenConstants.viewModes.TILE_MODE;
        ActionInterceptor('Switch Content View', ContentScreenStore.handleSwitchRelationshipViewMode.bind(this, sElementWorkingId, sViewMode))();
        break;

      case 'list_relationship_entities':
        sViewMode = ContentScreenConstants.viewModes.LIST_MODE;
        ActionInterceptor('Switch Content View', ContentScreenStore.handleSwitchRelationshipViewMode.bind(this, sElementWorkingId, sViewMode))();
        break;

      case 'toggleMode_relationship_entities': //Remaining
        ActionInterceptor('Toggle Thumbnail Mode', ContentScreenStore.handleToggleThumbnailModeClicked.bind(this, "", oFilterContext))();
        break;

      case 'exportToMDM':
        ActionInterceptor('Export To MDM', ContentScreenStore.handleExportToMDMButtonClicked)();
        break;

      case 'exportContents':
        ActionInterceptor('Export Base Entity', ContentScreenStore.handleContextMenuListItemClicked.bind(this, {code: null}, sId, oFilterContext))();
        break;

      case "transferToSupplierStaging" :
        ActionInterceptor('Transfer To Supplier Staging', ContentScreenStore.handleTransferToSupplierStagingButtonClicked)();
        break;

      case 'exportToExcel':
        ActionInterceptor('Export To Excel', ContentScreenStore.handleExportToExcelButtonClicked)();
        break;

      case "copy_hierarchy":
        ActionInterceptor('Copy Hierarchy Contents', ContentScreenStore.handleThumbnailContextMenuItemClicked.bind(this, "copy"))();
        break;

      case "cut_hierarchy":
        ActionInterceptor('Cut Hierarchy Contents', ContentScreenStore.handleThumbnailContextMenuItemClicked.bind(this, "cut"))();
        break;

      case "paste_hierarchy":
        ActionInterceptor('Paste Hierarchy Contents', ContentScreenStore.handleContentHorizontalTreeNodeContextMenuItemClicked.bind(this, "paste", oFilterContext))();
        break;

      case "bulkEdit":
        ActionInterceptor('bulk apply ', ContentScreenStore.handleBulkEditIconClicked)();
        break;


      case "cloneContent":
        ActionInterceptor('Clone Contents', ContentScreenStore.handleCreateCloneButtonClicked.bind(this, sId))();
        break;

      case "gridEditContents":
        ActionInterceptor('Grid Edit Contents', ContentScreenStore.handleContentGridEditButtonClicked)();
        break;

      case "restoreContents":
        ActionInterceptor('Restore Contents', ContentScreenStore.handleRestoreContentsButtonClicked)();
        break;

      case "selectAll_timeline_entities" :
        ActionInterceptor('select all', ContentScreenStore.handleTimelineVersionSelectAllButtonClicked)();
        break;

      case "deselectAll_timeline_entities" :
        ActionInterceptor('deselect all', ContentScreenStore.handleTimelineVersionDeselectAllButtonClicked)();
        break;

      case "zoomin_timeline_entities" :
        ActionInterceptor('zoom in', ContentScreenStore.handleTimelineZoomButtonClicked.bind(this, true))();
        break;

      case "zoomout_timeline_entities" :
        ActionInterceptor('zoom out', ContentScreenStore.handleTimelineZoomButtonClicked.bind(this, false))();
        break;

      case "archive_timeline_entities" :
      case "delete_timeline_entities" :
        ActionInterceptor('archive timeline entities', ContentScreenStore.handleTimelineVersionDeleteSelectedButtonClicked)();
        break;

      case "compare_timeline_entities" :
        ActionInterceptor('compare timeline entities', ContentScreenStore.handleTimelineCompareVersionButtonClicked.bind(this, true))();
        break;

      case "staticCollectionAddEntity":
        ActionInterceptor('Content Tile List View Events', ContentScreenStore.handleStaticCollectionQuickListOpened.bind(this, oFilterContext))();
        break;

      case "restore_timeline_entities":
        ActionInterceptor('restore timeline entities', ContentScreenStore.handleTimelineRestoreArchiveVersionsButtonClicked)();
        break;

      case 'timeline_comparison_properties':
      case 'timeline_comparison_relationship':
        ActionInterceptor('timelineComparisonView', ContentScreenStore.handleTimelineComparisonDialogViewHeaderButtonClicked.bind(this, sId))();
        break;

      case 'taxonomies':
      case 'classes':
        ActionInterceptor('multiClassificationView', ContentScreenStore.handleMultiClassificationDialogViewHeaderButtonClicked.bind(this, sId))();
        break;

      case BulkEditTabTypesConstants.PROPERTIES:
      case BulkEditTabTypesConstants.TAXONOMIES:
      case BulkEditTabTypesConstants.CLASSES:
        ActionInterceptor('bulkEditView', ContentScreenStore.handleBulkEditHeaderToolbarButtonClicked.bind(this, sId))();
        break;
      case 'assetBulkShare':
        ActionInterceptor('Asset Bulk Share', handleAssetBulkShareButtonClicked)();
        break;
    }
  };

  let handleContentLeftSideShareOrDownloadButtonClicked = function(sContext, sButtonContext) {
    ContentScreenStore.handleContentLeftSideShareOrDownloadButtonClicked(sContext, sButtonContext);
  };

  var handleCommentChanged = function (oContext, sNewValue) {
    ContentScreenStore.handleCommentChanged(sNewValue);
  };

  var handleCollectionSaveCommentChanged = function (oContext, sNewValue) {
    ContentScreenStore.handleCollectionSaveCommentChanged(sNewValue);
  };

  var handleEntityHeaderEntityNameEditClicked = function () {
    ContentScreenStore.handleEntityHeaderEntityNameEditClicked();
  };

  var handleEntityHeaderEntityNameBlur = function (oContext, sLabel) {
    ContentScreenStore.handleEntityHeaderEntityNameBlur(sLabel);
  };

  var handleSelectionToggleButtonClicked = function (sContext, sKey, sId, bSingleSelect) {
    ContentScreenStore.handleSelectionToggleButtonClicked(sContext, sKey, sId, bSingleSelect);
  };

  let handleTaskDataChanged = function (oData) {
    ContentScreenStore.handleTaskDataChanged(oData);
  };

  var handleXRayButtonClicked = function (sRelationshipId, sRelationshipIdToFetchData, oFilterContext) {
    ContentScreenStore.handleToggleThumbnailModeClicked(sRelationshipId, sRelationshipIdToFetchData, oFilterContext);
  };

  var handleXRayPropertyClicked = function (aProperties, sRelationshipId, sRelationshipIdToFetchData, oFilterContext) {
    ContentScreenStore.handleXRayPropertyClicked(aProperties, sRelationshipId, sRelationshipIdToFetchData, oFilterContext);
  };

  var handleShowXRayPropertyGroupsClicked = function (bMakeDefaultSelected, bIsNotForXRay, oFilterContext) {
    ContentScreenStore.handleShowXRayPropertyGroupsClicked(bMakeDefaultSelected, bIsNotForXRay, oFilterContext);
  };

  var handleXRayPropertyGroupClicked = function (sId, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
    ContentScreenStore.handleXRayPropertyGroupClicked(sId, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext);
  };

  var handleCloseActiveXRayPropertyGroupClicked = function (sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
    ContentScreenStore.handleCloseActiveXRayPropertyGroupClicked(sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext);
  };

  var handleSwitchContentViewTypeClicked = function (sViewType, oFilterContext) {
    trackMe('handleSwitchContentViewTypeClicked');
    ContentScreenStore.handleSwitchContentViewTypeClicked(sViewType, "article", oFilterContext);
  };

  var handleContentTilePaginationChanged = function (oPaginationData, oFilterContext) {
    ContentScreenStore.handleContentTilePaginationChanged(oPaginationData, oFilterContext);
  };

  var handleRelationshipPaginationChanged = function (sRelationshipId, oPaginationData, oFilterContext) {
    ContentScreenStore.handleRelationshipLoadMore(sRelationshipId, "", oPaginationData, oFilterContext);
  };

  let handleBundleSectionPaginationChanged = function (sSideId, oPaginationData, sRelationshipId) {
    ContentScreenStore.handleBundleSectionPaginationChanged(sSideId, sRelationshipId, oPaginationData);
  };

  var handleRelationshipLoadMore = function (oContext, sId, sNavigationContext, oPaginationData) {
    ContentScreenStore.handleRelationshipLoadMore(sId, sNavigationContext, oPaginationData);
  };

  var handleThumbnailScrollerLoadMore = function (sId, sDirection) {
    ContentScreenStore.handleThumbnailScrollerLoadMore(sId, sDirection);
  };

  var handleTagNodeCLicked = function (oEvent, oModel) {
    trackMe('handleTagNodeCLicked');

    logger.info('handleTagNodeClickedEvent: Dispatching Tag node clicked',
        {'oModel': oModel});
    ContentScreenStore.handleTagNodeClicked(oModel);
  };

  var handleTagGroupTagValueChanged = function (sTagId, aTagValueRelevanceData, oExtraData) {
    ContentScreenStore.handleTagGroupTagValueChanged(sTagId, aTagValueRelevanceData, oExtraData);
  };

  var handleOpenCoverflowViewerButtonClicked = function (oContext, oEvent, oModel, iImageCoverflowItemIndex, sId, sContext) {
    trackMe('handleOpenCoverflowViewerButtonClicked');
    ContentScreenStore.handleOpenCoverflowViewerButtonClicked(oModel, iImageCoverflowItemIndex, sId, sContext);
  };

  let handleAssetUploadReplaceClicked = function (oRefDom, bIsAssetReplaced) {
    ContentScreenStore.handleAssetUploadOrReplaceClicked(oRefDom, bIsAssetReplaced);
  };

  let handleGetAllAssetExtensions = function (oRefDom, sContext) {
    ContentScreenStore.handleGetAllAssetExtensions(oRefDom, sContext);
  };

  let handleChipsEditButtonClicked = function (oProperty, sId) {
    ContentScreenStore.handleChipsEditButtonClicked(oProperty, sId);
  };

  let handleGridPropertyViewClassificationDialogButtonClicked = function (sButtonId, sContext, sEmbeddedContentId,
                                                                          sPropertyId, sTableContextId) {
    ContentScreenStore.handleGridPropertyViewClassificationDialogButtonClicked(sButtonId, sContext, sEmbeddedContentId, sPropertyId, sTableContextId);
  };

  var handleCoverflowDetailViewClosedButtonClicked = function (oEvent, sId, sContext) {
    trackMe('handleCoverflowDetailViewClosedButtonClicked');
    ContentScreenStore.handleCoverflowDetailViewClosedButtonClicked(sId, sContext);
  };

  var handleLinkedSectionButtonClicked = function (oItem, oFilterContext) {
    ContentScreenStore.handleLinkedSectionButtonClicked(oItem, oFilterContext);
  };

  var handleFlatButtonViewBtnClicked = function (sContext, sContextId, oFilterContext) {
    //todo move to contentscreenstore
    switch (sContext) {
      case "renditionTab":
        ContentScreenStore.handleUOMTableCreateRowButtonClicked(sContextId);
        break;
      case "makeupOpen":
        ContentScreenStore.handleMakeUpInstanceOpenClicked(sContextId, oFilterContext);
        break;
    }
  };

  let handleAttributeVariantInputTextChanged = function (oEvent, sContext, sSectionElementId, sAttributeId, sValue, sSectionId, oFilterContext) {
    if (sContext == "content") {
      ContentScreenStore.handleAttributeVariantInputTextChanged(sAttributeId, sValue, sSectionId, oFilterContext);
    }
    else if (sContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY || sContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY) {
      const sProperty = "defaultValue";
      ContentScreenStore.handleRuntimePCElementInputChanged(sSectionId, sSectionElementId, sProperty, sValue, sContext, oFilterContext);
    }
  };

  var handleSectionAttributeClicked = function (oContext, sSectionContext, sContextKey, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, bIsReadOnlyCoupled, sFillerID) {
    trackMe('handleSectionAttributeClicked');
    logger.info('handleSectionAttributeClicked',
        {'fieldType': sContextKey, 'elementId': sElementId, 'structureId': sStructureId});
    ContentScreenStore.handleSectionAttributeClicked(sSectionContext, sContextKey, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, bIsReadOnlyCoupled, sFillerID);
  };

  var handleSectionMaskClicked = function (sSectionId, sSectionElementId, sSectionContext, oFilterContext) {
    trackMe('handleSectionMaskClicked');
    ContentScreenStore.handleSectionMaskClicked(sSectionId, sSectionElementId, sSectionContext, oFilterContext);
  };

  var handleSectionElementNotificationButtonClicked = function (oContext, sButtonKey, oElement) {
    ContentScreenStore.handleSectionElementNotificationButtonClicked(sButtonKey, oElement);
  };

  var handleSectionAttributeDefaultValueChanged = function (sAttributeId, sValue, sSectionId, sSourceId) {
    ContentScreenStore.handleAttributeVariantInputTextChanged(sAttributeId, sValue, sSectionId, sSourceId);
  };

  var handleSectionTagDefaultValueChanged = function (sTagId, aConflictingTagValues, sSectionId, sSourceId) {
    ContentScreenStore.handleSectionTagDefaultValueChanged(sTagId, aConflictingTagValues, sSectionId, sSourceId);
  };

  var handleContentSectionRoleMSSValueChanged = function (sRoleId, aCheckedItems) {
    ContentScreenStore.handleContentSectionRoleMSSValueChanged(sRoleId, aCheckedItems);
  };

  var handleConcatenatedAttributeExpressionChanged = function (sAttributeId, sValue, sSectionId, sExpressionId, oFilterContext) {
    ContentScreenStore.handleConcatenatedAttributeExpressionChanged(sAttributeId, sValue, sSectionId, sExpressionId, oFilterContext);
  };

  var handleContentEntityDragStart = function (oEvent, oModel) {
    ContentScreenStore.handleContentEntityDragStart(oModel);
  };

  var handleContentEntityDragEnd = function (oEvent, oModel) {
    ContentScreenStore.handleContentEntityDragEnd(oModel);
  };

  var handleContentEntityDrop = function (oEvent, oModel, oFilterContext) {
    ContentScreenStore.handleContentEntityDrop(oModel, oFilterContext);
  };

  var handleContentEntityDragEnter = function (oDropModel, oDragModel) {
    ContentScreenStore.handleContentEntityDragEnter(oDropModel, oDragModel);
  };

  var handleMultiSelectPopoverClosed = function (aSelectedItems, sContext) {
    ContentScreenStore.handleDropdownListNodeBlurred(sContext, aSelectedItems);
  };

  var handleTemplateDropDownListNodeClicked = function (oSelectedTemplate, oFilterContext) {
    ContentScreenStore.handleTemplateDropDownListNodeClicked(oSelectedTemplate, oFilterContext);
  };

  var handleAddTaxonomyPopoverItemClicked = function (oContext, oModel, sTaxonomyId, sContext, sTaxonomyType) {
    ContentScreenStore.handleAddTaxonomyPopoverItemClicked(oModel, sTaxonomyId, sContext, sTaxonomyType);
  };

  var handleUniqueSelectionItemClicked = function (sGroupId, sDataId, sContext, oExtraData) {
    ContentScreenStore.handleUniqueSelectionItemClicked(sGroupId, sDataId, sContext, oExtraData);
  };

  var handleMultiSelectSearchCrossIconClicked = function (oEvent, sContextKey, sId) {
    ContentScreenStore.handleMultiSelectSearchCrossIconClicked(sContextKey, sId);
  };

  var handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oContext, oTaxonomy, sParentTaxonomyId, sContext, sTaxonomyType) {
    ContentScreenStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sTaxonomyType);
  };

  var handleAllowedTypesDropdownOpened = function (sContext, sTaxonomyId) {
    ContentScreenStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
  };

  let handleAllowedTypesSearchHandler = function (sContext, sSearchText, sTaxonomyId) {
    ContentScreenStore.handleAllowedTypesSearchHandler(sContext, sSearchText, sTaxonomyId);
  };

  let handleAllowedTypesLoadMoreHandler = function (sContext, sTaxonomyId) {
    ContentScreenStore.handleAllowedTypesLoadMoreHandler(sContext, sTaxonomyId);
  };

  let handleKpiChartTypeSelected = function (sKpiTypeId) {
    ContentScreenStore.handleKpiChartTypeSelected(sKpiTypeId);
  };

  var handleStructureFroalaValueChanged = function (oContext, sStructureId, sNewValue, sContext) {
    //hack for proto
    const sSplitter = ContentUtils.getSplitter();
    var aData = sStructureId.split(sSplitter);
    if (aData.length && aData[0] == "attribute") {
      //TODO: Temp fix
      if (aData[4] == "new") {
        aData[3] += (sSplitter + "new");
      }
      handleAttributeVariantInputTextChanged(null, aData[1], aData[2], aData[3], sNewValue, aData[4], sContext);
    }
  };

  var handleStructureFroalaImageUploaded = function (oEvent, sImageKey) {
    ContentScreenStore.handleStructureFroalaImageUploaded(sImageKey);
  };

  var handleStructureFroalaImagesRemoved = function (oEvent, aImageKeys) {
    ContentScreenStore.handleContentImagesRemoved(aImageKeys);
  };

  var handleSectionHeaderClicked = function (oContext, oModel) {
    ContentScreenStore.handleSectionHeaderClicked(oModel);
  };

  var handleSectionRemoveClicked = function (sSectionId, sSectionLabel) {
    ContentScreenStore.handleSectionRemoveClicked(sSectionId, sSectionLabel);
  };

  var handleSectionSkipToggled = function (oModel) {
    ContentScreenStore.handleSectionSkipToggled(oModel);
  };

  var handleListNodeClicked = function (oContext, oEvent, oModel) {
    var sContext = oModel.properties['context'];
    switch (sContext) {
    }
  };

  var handleListNodeChecked = function (oContext, oEvent, oModel) {
    var sContext = oModel.properties['context'];
    switch (sContext) {
    }
  };

  var handleFilterAttributeDeleteClicked = function (sElementId, oFilterContext) {
    ContentScreenStore.handleFilterElementDeleteClicked(sElementId, oFilterContext);
  };

  var handleFilterAttributeExpandClicked = function (sElementId, oFilterContext) {
    ContentScreenStore.handleFilterElementExpandClicked(sElementId, oFilterContext);
  };

  var handleAddAttributeValueClicked = function (sAttributeId, oFilterContext) {
    ContentScreenStore.handleAddAttributeValueClicked(sAttributeId, oFilterContext);
  };

  var handleFilterAttributeValueTypeChanged = function (sAttributeId, sValId, sTypeId, oFilterContext) {
    ContentScreenStore.handleFilterAttributeValueTypeChanged(sAttributeId, sValId, sTypeId, oFilterContext);
  };

  var handleFilterAttributeValueChanged = function (sAttributeId, sValId, sVal, oFilterContext) {
    ContentScreenStore.handleFilterAttributeValueChanged(sAttributeId, sValId, sVal, oFilterContext);
  };

  var handleFilterAttributeValueChangedForRange = function (sAttributeId, sValId, sVal, sRange, oFilterContext) {
    ContentScreenStore.handleFilterAttributeValueChangedForRange(sAttributeId, sValId, sVal, sRange, oFilterContext);
  };

  var handleFilterAttributeValueDeleteClicked = function (sAttributeId, sValId, oFilterContext) {
    ContentScreenStore.handleFilterAttributeValueDeleteClicked(sAttributeId, sValId, oFilterContext);
  };

  var handleContentFilterUserValueChanged = function (sRoleId, aUsers, oFilterContext) {
    ContentScreenStore.handleContentFilterUserValueChanged(sRoleId, aUsers, oFilterContext);
  };

  var handleLanguageForComparisonChanged = function (sLanguageForComparison) {
    ContentScreenStore.handleLanguageForComparisonChanged(sLanguageForComparison);
  };

  let handleContentTimelineComparisonButtonClicked = function (sButtonId) {
    ContentScreenStore.handleEntityDetailComparisionDialogButtonClick(sButtonId);
  };

  var handleFilterButtonClicked = function (sContext, oExtraData, sSelectedHierarchyContext, oFilterContext) {
    ContentScreenStore.handleFilterButtonClicked(sContext, oExtraData, sSelectedHierarchyContext, oFilterContext);
  };

  let handleChooseTaxonomyApplyButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleChooseTaxonomyApplyButtonClicked(oFilterContext)
  };

  let handleFilterPopoverClosed = function (oFilterContext, sContext, oExtraData) {
    ContentScreenStore.handleFilterPopoverClosed(oFilterContext, sContext, oExtraData);
  };

  var handleFilterSearchTextChanged = function (oContext, oSearchedData, sContext, oExtraData = {}) {
       ContentScreenStore.handleFilterSearchTextChanged(oSearchedData, oExtraData);
  };

  var handleSetSectionClicked = function (oContext, sActiveSetId, sContext, sRelationshipSectionId, oRelationshipSide) {
    ContentScreenStore.handleSetSectionClicked(sActiveSetId, sContext, sRelationshipSectionId, oRelationshipSide);
  };

  var handleBreadcrumbItemClicked = function (oContext, oItem, sContext) {
    if (sContext == "staticCollection") {
      ContentScreenStore.handleStaticCollectionBreadCrumbItemClicked(oItem, sContext);
    } else {
      ContentScreenStore.handleBreadCrumbClicked(oItem);
      //ContentScreenStore.handleBreadcrumbItemClicked.apply(ContentScreenStore, [oItem]);
    }
  };

  var handleVerticalMaterialMenuItemClicked = function (oContext, sContext, oItem) {
    switch (sContext) {
      case "contentTile":
        ContentScreenStore.handleContentTileVerticalMenuItemClicked(oItem);
        break;
    }
  };

  let handleModuleCreateButtonClicked = function (sSearchedText, sContext, oFilterContext, bIsLoadMoreClicked) {
    ContentScreenStore.handleModuleCreateButtonClicked(sSearchedText, sContext, oFilterContext, bIsLoadMoreClicked);
  };

  var handleKeyDown = function () {
    ExceptionLogger.log("Hii");
  };

  var handleControlS = function (oContext, oEvent) {
    ContentScreenStore.handleControlS(oEvent);
  };

  var handleControlC = function (oContext, oEvent) {
  };

  var handleControlV = function (oContext, oEvent) {
  };

  var handleControlD = function (oContext, oEvent) {
    ContentScreenStore.handleControlD(oEvent);
  };

  var handleEsc = function (oContext, oEvent) {
    ContentScreenStore.handleEsc(oEvent);
  };

  var handleTab = function (oContext, oEvent) {
    ContentScreenStore.handleTab(oEvent);
  };

  var handleShiftTab = function (oContext, oEvent) {
    ContentScreenStore.handleTab(oEvent, true);
  };

  var handleEnter = function (oContext, oEvent) {
    ContentScreenStore.handleEnter(oEvent);
  };

  var handleMetricValueExceeded = function () {
    ContentScreenStore.handleMetricValueExceeded();
  };

  let handleMetricUnitChanged = function (sSelectedUnit, oSectionElementDetails, sAttrId, sValueId, sRangeType, oFilterContext) {
    ContentScreenStore.handleMetricUnitChanged(sSelectedUnit, oSectionElementDetails, sAttrId, sValueId, sRangeType, oFilterContext);
  };

  var handleTreeCheckClicked = function (oEvent, iId, iLevel, oFilterContext) {
    ContentScreenStore.handleTreeCheckClicked(iId, iLevel, oFilterContext);
  };

  var handleTaxonomyChildrenLazyData = function (sNodeId, iLevel, oFilterContext) {
    ContentScreenStore.handleTaxonomyChildrenLazyData(sNodeId, iLevel, oFilterContext);
  };

  var handleTreeHeaderCheckClicked = function (oEvent, iId, iLevel, oFilterContext) {
    ContentScreenStore.handleTreeHeaderCheckClicked(iId, iLevel, oFilterContext);
  };

  var handleTreeNodeToggleClicked = function (oEvent, sId, oFilterContext) {
    ContentScreenStore.handleTreeNodeToggleClicked(sId, oFilterContext);
  };

  var handleTaxonomySearchTextChanged = function (oEvent, sSearchText, oFilterContext) {
    ContentScreenStore.handleTaxonomySearchTextChanged(sSearchText, oFilterContext);
  };

  var handleSelectTaxonomiesButtonClicked = function (oFilterContext, oTaxonomyTreeRequestData) {
    ContentScreenStore.handleSelectTaxonomiesButtonClicked(oFilterContext, oTaxonomyTreeRequestData);
  };

  let handleAppliedTaxonomyRemovedClicked = function (oEvent, iId, oFilterContext, sCategoryType) {
    ContentScreenStore.handleAppliedTaxonomyRemovedClicked(iId, oFilterContext, sCategoryType);
  };

  var handleSortByItemClicked = function (sId, bMultiSelect, oFilterContext) {
    ContentScreenStore.handleSortByItemClicked(sId, bMultiSelect, oFilterContext);
  };

  var handleFilterSortOrderToggled = function (sId, oFilterContext) {
    ContentScreenStore.handleFilterSortOrderToggled(sId, false, oFilterContext);
  };

  var handleFilterSortReOrdered = function (aSortData, oFilterContext) {
    ContentScreenStore.handleFilterSortReOrdered(aSortData, oFilterContext);
  };

  var handleSelectedTaxonomiesClearAllClicked = function (oFilterContext, sCategoryType) {
    ContentScreenStore.handleSelectedTaxonomiesClearAllClicked(oFilterContext, sCategoryType);
  };

  var handleAdvancedSearchButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleAdvancedSearchButtonClicked(oFilterContext);
  };

  let handleLoadMoreFilterButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleLoadMoreFilterData(oFilterContext);
  };

  var handleAdvancedSearchPanelCancelClicked = function (oFilterContext) {
    ContentScreenStore.handleAdvancedSearchPanelCancelClicked(oFilterContext);
  };

  var handleBulkEditCancelClicked = function () {
    ContentScreenStore.handleBulkEditCancelClicked();
  };

  var handleBulkEditApplyButtonClicked = function (oSummaryView) {
    ContentScreenStore.handleBulkEditApplyButtonClicked(oSummaryView);
  };

  let handleBulkEditGetTreeNodeChildren = function (sTreeNodeId) {
    ContentScreenStore.handleBulkEditGetTreeNodeChildren(sTreeNodeId);
  };

  let handleBulkEditPropertyCheckboxClicked = function (oProperty) {
    ContentScreenStore.handleBulkEditPropertyCheckboxClicked(oProperty);
  };

  let handleBulkEditPropertyValueChanged = function (sAttributeId, valueData) {
    ContentScreenStore.handleBulkEditPropertyValueChanged(sAttributeId, valueData);
  };

  var handleBulkEditRemoveProperty = function (oProperty) {
    ContentScreenStore.handleBulkEditRemoveProperty(oProperty);
  };

  let handleActionableChipsViewCrossIconClicked = function (sId, sContext) {
    ContentScreenStore.handleActionableChipsViewCrossIconClicked(sId, sContext);
  };

  let handleSectionElementConflictIconClicked = function (sElementId, bShowConflictSourcesOnly, bIsConflictResolved) {
    ContentScreenStore.handleSectionElementConflictIconClicked(sElementId, bShowConflictSourcesOnly, bIsConflictResolved);
  };

  var handleFilterShowDetailsClicked = function (oFilterContext) {
    ContentScreenStore.handleFilterShowDetailsClicked(oFilterContext);
  };

  var handleClearAllAppliedFilterClicked = function (sSelectedHierarchyContext, oFilterContext) {
    ContentScreenStore.handleClearAllAppliedFilterClicked(sSelectedHierarchyContext, oFilterContext);
  };

  var handleRemoveFilterGroupClicked = function (sId, sContext, oExtraData, sSelectedHierarchyContext, oFilterContext) {
    ContentScreenStore.handleRemoveFilterGroupClicked(sId, sContext, oExtraData, sSelectedHierarchyContext, oFilterContext);
  };

  var handleChildFilterToggled = function (sParentId, sChildId, sContext, oExtraData, oFilterContext) {
    ContentScreenStore.handleChildFilterToggled(sParentId, sChildId, sContext, oExtraData, oFilterContext);
  };

  let handleApplyLazyFilter = function (oFilterData) {
    ContentScreenStore.handleApplyLazyFilter(oFilterData);
  };

  var handleSubmitFilterSearchText = function (oContext, sSearchText, sSelectedHierarchyContext, oFilterContext) {
    ContentScreenStore.handleSubmitFilterSearchText(sSearchText, sSelectedHierarchyContext, oFilterContext);
  };

  var handleTaxonomySectionInputChanged = function (sSectionId, sElementId, sProperty, sNewValue) {
    ContentScreenStore.handleTaxonomySectionInputChanged(sSectionId, sElementId, sProperty, sNewValue);
  };

  var handleTaxonomyCancelButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleTaxonomyCancelButtonClicked(oFilterContext);
  };

  var handleOrganiseTaxonomyButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleOrganiseTaxonomyButtonClicked(oFilterContext);
  };

  var handleTaxonomyShowPopOverStateChanged = function (bShow, oFilterContext) {
    ContentScreenStore.handleTaxonomyShowPopOverStateChanged(bShow, oFilterContext);
  };

  var handleTaxonomyClearAllClicked = function (oFilterContext) {
    ContentScreenStore.handleTaxonomyClearAllClicked(oFilterContext);
  };

  var handleModuleItemClicked = function (sModuleId, oFilterContext) {
    ContentScreenStore.handleModuleItemClicked(sModuleId, oFilterContext);
  };

  var handleEditCollectionButtonClicked = function () {
    ContentScreenStore.handleEditCollectionButtonClicked();
  };

  var handleAcrolinxSidebarToggled = function () {
    ContentScreenStore.handleAcrolinxSidebarToggled();
  };

  var handleCollectionEditDialogButtonClicked = function (sContext, oFilterContext) {
    ContentScreenStore.handleCollectionEditDialogButtonClicked(sContext, oFilterContext);
  };

  var handleContentWrapperViewBackButtonClicked = function () {
    ContentScreenStore.handleContentWrapperViewBackButtonClicked();
  };

  var handleDashboardDataResetPrevented = function (bDashboardRecentPrevented) {
    ContentScreenStore.handleDashboardDataResetPrevented(bDashboardRecentPrevented);
  };

  var handleActiveCollectionSaveClicked = function (sContext) {
    ContentScreenStore.handleActiveCollectionSaveClicked(sContext);
  };

  var handleActiveCollectionLabelChanged = function (sLabel) {
    ContentScreenStore.handleActiveCollectionLabelChanged(sLabel);
  };

  var handleActiveCollectionVisibilityChanged = function () {
    ContentScreenStore.handleActiveCollectionVisibilityChanged();
  };

  var handleCreateNewCollectionButtonClicked = function (oCreationData, oFilterContext) {
    ContentScreenStore.handleCreateNewCollectionButtonClicked(oCreationData, oFilterContext);
  };

  var handleModifyCollectionClicked = function (sCollectionId, sContext, oFilterContext) {
    ContentScreenStore.handleModifyCollectionClicked(sCollectionId, sContext, oFilterContext);
  };

  var handleCreateDynamicCollectionButtonClicked = function () {
    ContentScreenStore.handleCreateDynamicCollectionButtonClicked();
  };

  var handleCreateStaticCollectionButtonClicked = function () {
    ContentScreenStore.handleCreateStaticCollectionButtonClicked();
  };

  var handleSingleContentAddToStaticCollectionClicked = function (sCollectionId, sContentId, oModel, oFilterContext) {
    ContentScreenStore.handleSingleContentAddToStaticCollectionClicked(sCollectionId, sContentId, oModel, oFilterContext);
  };

  let handleThumbnailInformationViolationIconClicked = function (oEvent) {
    ContentScreenStore.handleThumbnailInformationViolationIconClicked(oEvent);
  };

  let handleThumbnailInformationEntityInfoIconClicked = function (sContentId, sVariantId) {
    ContentScreenStore.handleThumbnailInformationEntityInfoIconClicked(sContentId, sVariantId);
  };

  var handleCollectionSelected = function (oContext, oCollection, sContext, oFilterContext) {
    ContentScreenStore.handleCollectionSelected(oCollection, sContext, oFilterContext);
  };

  var handleCollectionBreadcrumbReset = function () {
    ContentScreenStore.handleCollectionBreadcrumbReset();
  };

  var handleNextCollectionClicked = function (sId) {
    ContentScreenStore.handleNextCollectionClicked(sId);
  };

  var handleStaticCollectionBackButtonClicked = function (oItem) {
    ContentScreenStore.handleStaticCollectionBreadCrumbItemClicked(oItem);
  };

  var handleStaticCollectionOrganiseButtonClicked = function (sClickedNodeId, oFilterContext) {
    ContentScreenStore.handleStaticCollectionOrganiseButtonClicked(sClickedNodeId, oFilterContext);
  };

  var handleStaticCollectionRootBreadCrumbClicked = function () {
    ContentScreenStore.handleStaticCollectionRootBreadCrumbClicked();
  };

  var handleCollectionItemVisibilityModeChanged = function (sId, bIsDynamic, oFilterContext) {
    ContentScreenStore.handleCollectionItemVisibilityModeChanged(sId, bIsDynamic, oFilterContext);
  };

  var handleDeleteCollectionClicked = function (oContext, sId, sContext, oFilterContext) {
    ContentScreenStore.handleDeleteCollectionClicked(sId, sContext, {filterContext: oFilterContext});
  };

  var handleMakeDefaultImageButtonClicked = function (sActiveAssetId) {
    ContentScreenStore.handleMakeDefaultImageButtonClicked(sActiveAssetId);
  };

  var handleEntitySnackBarStopShaking = function () {
    ContentScreenStore.handleEntitySnackBarStopShaking()
  };

  var handleAdvancedSearchListItemNodeClicked = function (sItemId, sType, sContext, oFilterContext) {
    ContentScreenStore.handleAdvancedSearchListItemNodeClicked(sItemId, sType, sContext, oFilterContext);
  };

  var handleAdvancedSearchListLoadMoreClicked = function (sType, sContext) {
    ContentScreenStore.handleAdvancedSearchListLoadMoreClicked(sType, sContext);
  };

  var handleAdvancedSearchListSearched = function (sSearchText, sContext) {
    ContentScreenStore.handleAdvancedSearchListSearched(sSearchText, sContext);
  };

  var handleAdvancedSearchFilterButtonCLicked = function (sSelectedHierarchyContext, oFilterContext) {
    ContentScreenStore.handleAdvancedSearchFilterButtonCLicked(sSelectedHierarchyContext, oFilterContext);
  };

  var handleNatureThumbDeleteClicked = function (oModel) {
    ContentScreenStore.handleNatureThumbDeleteClicked(oModel);
  };

  var handleNatureThumbViewClicked = function (oModel, oFilterContext) {
    ContentScreenStore.handleNatureThumbViewClicked(oModel, oFilterContext);
  };

  var handleNatureThumbSelectClicked = function (oModel, sRelationshipId) {
    ContentScreenStore.handleNatureThumbSelectClicked(oModel, sRelationshipId);
  };

  var handleBundleToolbarItemClicked = function (sButtonContext, sSideId, sViewContext, sRelationshipId) {
    ContentScreenStore.handleBundleToolbarItemClicked(sButtonContext, sSideId, sViewContext, sRelationshipId);
  };

  var handleNatureAddEntityButtonClicked = function (sNatureRelationshipId, oRelationshipSide, sViewContext) {
    ContentScreenStore.handleNatureAddEntityButtonClicked(sNatureRelationshipId, oRelationshipSide, sViewContext);
  };

  var handleRelationshipContextNextButtonClicked = function (filterContext) {
    ContentScreenStore.handleRelationshipContextNextButtonClicked(filterContext);
  };

  let handleRelationshipContextCreateVariantClicked = function (sRelationshipId) {
    ContentScreenStore.handleRelationshipContextCreateVariantClicked(sRelationshipId);
  };

  let handleCreateLinkedVariantClicked = function (sContext) {
    ContentScreenStore.handleCreateLinkedVariantClicked(sContext);
  };

  var handleRelationshipContextCancelButtonClicked = function (sCancelButtonId) {
    ContentScreenStore.handleRelationshipContextCancelButtonClicked(sCancelButtonId);
  };

  var handleRelationshipContextSaveButtonClicked = function (sRelationshipId) {
    ContentScreenStore.handleRelationshipContextSaveButtonClicked(sRelationshipId);
  };

  let handleEntityDetailComparisionDialogButtonClick = (sButtonId) => {
    ContentScreenStore.handleEntityDetailComparisionDialogButtonClick(sButtonId);
  };

  var handleVariantLinkedEntitiesAddEntityClicked = function (sSelectedEntity, oFilterContext) {
    ContentScreenStore.handleVariantLinkedEntitiesAddEntityClicked(sSelectedEntity, oFilterContext);
  };

  var handleVariantLinkedEntitiesRemoveEntityClicked = function (sLinkedInstanceId, sContextEntity) {
    ContentScreenStore.handleVariantLinkedEntitiesRemoveEntityClicked(sLinkedInstanceId, sContextEntity);
  };

  var handleVariantDialogSaveClicked = function (sContext, oFilterContext) {
    ContentScreenStore.handleVariantDialogSaveClicked(sContext, oFilterContext);
  };

  var handleVariantDialogDiscardClicked = function (sContext) {
    ContentScreenStore.handleVariantDialogCancelClicked(sContext);
  };

  var handleVariantDialogChangeAttributeValue = function (sContext, oData) {
    ContentScreenStore.handleVariantDialogChangeAttributeValue(sContext, oData);
  };

  var handleTableEditButtonClicked = function (sId, sContext, sTableContextId) {
    ContentScreenStore.handleTableEditButtonClicked(sId, sContext, sTableContextId);
  };

  var handleTableDeleteButtonClicked = function (sId, sContext, sTableContextId, oFilterContext) {
    ContentScreenStore.handleTableDeleteButtonClicked(sId, sContext, sTableContextId, oFilterContext);
  };

  var handleTableOpenButtonClicked = function (sId, sContext) {
    ContentScreenStore.handleTableOpenButtonClicked(sId, sContext);
  };

  let handleTableRowSelectionChanged = function (sAttributeId, sId, sContext, sValue) {
    ContentScreenStore.handleTableRowSelectionChanged(sAttributeId, sId, sContext, sValue);
  };

  var handleTableCellValueChanged = function (oCellData, sContext, sTableContextId) {
    ContentScreenStore.handleTableCellValueChanged(oCellData, sContext, sTableContextId);
  };

  let handleTableAddLinkedInstance = function (sContext, oFilterContext) {
    ContentScreenStore.handleTableAddLinkedInstance(sContext, oFilterContext);
  };

  let handleTableSetActivePopOverVariant = function (sContext, sContextInstanceId, sContextId, sEntity, bIsTransponse) {
    ContentScreenStore.handleTableSetActivePopOverVariant(sContext, sContextInstanceId, sContextId, sEntity, bIsTransponse);
  };

  let handleTableClearActivePopOverVariant = function () {
    ContentScreenStore.handleTableClearActivePopOverVariant();
  };

  let handleTableRemoveLinkedIntance = function (sLinkedInstanceId) {
    ContentScreenStore.handleTableRemoveLinkedInstance(sLinkedInstanceId);
  };

  var handleVariantTagGroupDateValueChanged = function (sKey, sValue) {
    ContentScreenStore.handleVariantTagGroupDateValueChanged(sKey, sValue);
  };

  var handleEntityTabClicked = function (sTabId) {
    ContentScreenStore.handleEntityTabClicked(sTabId);
  };

  var handleContentEditToolbarButtonClicked = function (sId) {
    ContentScreenStore.handleContentEditToolbarButtonClicked(sId);
  };

  var handleCircledTagNodeClicked = function (oViewContext, sTagGroupId, sTagId, sContext, oExtraData) {
    ContentScreenStore.handleCircledTagNodeClicked(sTagGroupId, sTagId, sContext, oExtraData);
  };

  var handleAssignImageToVariantButtonClicked = function (sVariantId, sAssetId, sContext) {
    ContentScreenStore.handleAssignImageToVariantButtonClicked(sVariantId, sAssetId, sContext);
  };

  let handleDeleteGeneratedAssetLinkClicked = function () {
    ContentScreenStore.handleDeleteGeneratedAssetLinkClicked();
  };

  var handleVariantCloseImageButtonClicked = function (sContext) {
    ContentScreenStore.handleVariantCloseImageButtonClicked(sContext);
  };

  var handleImageGalleryDialogViewVisibilityStatusChanged = function () {
    ContentScreenStore.handleImageGalleryDialogViewVisibilityStatusChanged();
  };

  var handleTaskDialogOpenClicked = function (oProperty) {
    ContentScreenStore.handleTaskDialogOpenClicked(oProperty);
  };

  var resetTaskData = function () {
    ContentScreenStore.resetTaskData();
  };

  let handleDateRangePickerDateChange = function (sContext, oSelectedTimeRange){
    ContentScreenStore.handleDateRangePickerDateChange(sContext, oSelectedTimeRange);
  };

  let handleDateRangePickerCancelClicked = function (sContext) {
    ContentScreenStore.handleDateRangePickerCancelClicked(sContext);
  };

  var handleFilterSortActivatedItemClicked = function (sId, oFilterContext) {
    ContentScreenStore.handleFilterSortActivatedItemClicked(sId, oFilterContext);
  };

  var handleFilterSortDeactivatedItemClicked = function (sId, bMultiSelect, sSelectedHierarchyContext, oFilterContext) {
    ContentScreenStore.handleFilterSortDeactivatedItemClicked(sId, bMultiSelect, sSelectedHierarchyContext, oFilterContext);
  };

  var handleMatchMergeCellClicked = function (sRowId, sValue, sTableId, sTableGroupName, bIsGoldenRecordComparison, sColumnId) {
    ContentScreenStore.handleMatchMergeCellClicked(sRowId, sValue, sTableId, sTableGroupName, bIsGoldenRecordComparison, sColumnId);
  };

  var handleMatchMergeTagCellClicked = function (sRowId, iValue, sTableId, bIsGoldenRecordComparison, sColumnId) {
    ContentScreenStore.handleMatchMergeTagCellClicked(sRowId, iValue, sTableId, bIsGoldenRecordComparison, sColumnId);
  };

  var handleMatchMergeColumnHeaderClicked = function (sColumnId) {
    ContentScreenStore.handleMatchMergeColumnHeaderClicked(sColumnId);
  };

  var handleMatchMergeRelationshipCellClicked = function (sRowId, oProperty, sTableId, sContext, sColumnId) {
    ContentScreenStore.handleMatchMergeRelationshipCellClicked(sRowId, oProperty, sTableId, sContext, sColumnId);
  };

  var handleMatchMergeRelationshipCellRemoveClicked = function (sRowId, sTableId, sContext) {
    ContentScreenStore.handleMatchMergeRelationshipCellRemoveClicked(sRowId, sTableId, sContext);
  };

  var handleProfileConfigMappedTagValueChanged = function (sId, sMappedTagValueId, sMappedElementId, sTagGroupId, oReferencedTagValues) {
    ContentScreenStore.handleProfileConfigMappedTagValueChanged(sId, sMappedTagValueId, sMappedElementId, sTagGroupId, oReferencedTagValues);
  };

  var handleEndpointTagValueIgnoreCaseToggled = function (sId, sMappedTagValueId) {
    ContentScreenStore.handleEndpointTagValueIgnoreCaseToggled(sId, sMappedTagValueId);
  };

  var handleProfileMappedElementChanged = function (sId, sMappedElementId, sOptionType, oReferencedData) {
    ContentScreenStore.handleEndpointMappedElementChanged(sId, sMappedElementId, sOptionType, oReferencedData);
  };

  var handleProfileUnmappedElementChanged = function (sName, sMappedElementId, sOptionType, oReferencedData) {
    ContentScreenStore.handleProfileUnmappedElementChanged(sName, sMappedElementId, sOptionType, oReferencedData);
  };

  var handleEndpointIsIgnoredToggled = function (sId, sTabId) {
    ContentScreenStore.handleEndpointIsIgnoredToggled(sId, sTabId);
  };

  var handleEndpointUnmappedElementIsIgnoredToggled = function (sId, sTabId) {
    ContentScreenStore.handleEndpointUnmappedElementIsIgnoredToggled(sId, sTabId);
  };

  var handleEndPointMappingViewBackButtonClicked = function () {
    ContentScreenStore.handleEndPointMappingViewBackButtonClicked();
  };

  var handleEndPointMappingViewImportButtonClicked = function () {
    ContentScreenStore.handleEndPointMappingViewImportButtonClicked();
  };

  var handleEndPointMappingTabClicked = function (sTabId) {
    ContentScreenStore.handleEndPointMappingTabClicked(sTabId);
  };

  var handleEndPointMappingFilterOptionChanged = function (sFilterId) {
    ContentScreenStore.handleEndPointMappingFilterOptionChanged(sFilterId);
  };

  var handleJobItemClicked = function (sJobId) {
    ContentScreenStore.handleJobItemClicked(sJobId);
  };

  let handleProcessDataDownloadButtonClicked = function (sButtonId) {
    ContentScreenStore.handleProcessDataDownloadButtonClicked(sButtonId);
  };

  let handleProcessInstanceDownloadButtonClicked = function (iProcessInstanceId, iInstanceIID) {
    ContentScreenStore.handleProcessInstanceDownloadButtonClicked(iProcessInstanceId, iInstanceIID);
  };

  let handleProcessInstanceDialogButtonClicked = function (sButtonId) {
    ContentScreenStore.handleProcessInstanceDialogButtonClicked(sButtonId);
  };

  var handleProcessGraphRefreshButtonClicked = function (bShowLoading) {
    ContentScreenStore.handleProcessGraphRefreshButtonClicked(bShowLoading);
  };

  var handleTimelineDeleteVersionZoomButtonClicked = function (sVersionId) {
    ContentScreenStore.handleTimelineDeleteVersionZoomButtonClicked(sVersionId);
  };

  var handleTimelineCompareVersionButtonClicked = function (bIsComparisonMode, aSelectedVersions) {
    ContentScreenStore.handleTimelineCompareVersionButtonClicked(bIsComparisonMode, aSelectedVersions);
  };

  var handleTimelineVersionSelectAllButtonClicked = function () {
    ContentScreenStore.handleTimelineVersionSelectAllButtonClicked();
  };

  var handleTimelineVersionLoadMoreButtonClicked = function () {
    ContentScreenStore.handleTimelineVersionLoadMoreButtonClicked();
  };

  var handleTimelineCompareVersionCloseButtonClicked = function (sContext) {
    switch (sContext) {
      case "contentComparison":
        ContentScreenStore.handleContentComparisonBackButtonClicked();
        break;
      case "goldenRecordComparison":
        ContentScreenStore.handleGoldenRecordComparisonCloseButtonClicked();
        break;

      default:
        ContentScreenStore.handleTimelineCompareVersionCloseButtonClicked();
    }
  };

  var handleGoldenRecordDialogActionButtonClicked = function (sButtonId, oFilterContext) {
    ContentScreenStore.handleGoldenRecordDialogActionButtonClicked(sButtonId, oFilterContext);
  };

  let handleGridEditButtonClicked = function () {
    ContentScreenStore.handleGridEditButtonClicked();
  };

  var handleTimelineSelectVersionButtonClicked = function (bZoomIn) {
    ContentScreenStore.handleTimelineSelectVersionButtonClicked(bZoomIn);
  };

  var handleTimelineVersionShowArchiveButtonClicked = function (bArchiveFlag, oFilterContext) {
    ContentScreenStore.handleTimelineVersionShowArchiveButtonClicked(bArchiveFlag, oFilterContext);
  };

  var handleTimelineRestoreVersionButtonClicked = function (sVersionId) {
    ContentScreenStore.handleTimelineRestoreVersionButtonClicked(sVersionId);
  };

  var handleTimelineRollbackVersionButtonClicked = function (sVersionId) {
    ContentScreenStore.handleTimelineRollbackVersionButtonClicked(sVersionId);
  };

  var handleGridViewSelectButtonClicked = function (aSelectedContentIds, bSelectAllClicked) {
    ContentScreenStore.handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
  };

  var handleGridViewActionItemClicked = function (sActionItemId, sContentId) {
    ContentScreenStore.handleGridViewActionItemClicked(sActionItemId, sContentId);
  };

  var handleGridViewDeleteButtonClicked = function () {
    ContentScreenStore.handleGridViewDeleteButtonClicked();
  };

  var handleGridPaginationChanged = function (oNewPaginationData) {
    ContentScreenStore.handleGridPaginationChanged(oNewPaginationData);
  };

  var handleAttributeContextViewShowVariantsClicked = function (sAttributeId, sAttributeVariantContextId, oFilterContext, sVariantInstanceId, sParentContextId) {
    ContentScreenStore.handleAttributeContextViewShowVariantsClicked(sAttributeId, sAttributeVariantContextId, oFilterContext, sVariantInstanceId, sParentContextId);
  };

  var handleAttributeContextViewDialogButtonClick = function (sAttributeContextId, sButtonId) {
    ContentScreenStore.handleAttributeContextViewDialogButtonClick(sAttributeContextId, sButtonId);
  };

  var handleGridViewSearchTextChanged = function (sSearchText) {
    ContentScreenStore.handleGridViewSearchTextChanged(sSearchText);
  };

  var handleGridViewSaveButtonClicked = function () {
    ContentScreenStore.handleGridViewSaveButtonClicked();
  };

  var handleGridViewDiscardButtonClicked = function () {
    ContentScreenStore.handleGridViewDiscardButtonClicked();
  };

  let handleGridViewResizerButtonClicked = function (iWidth, sId, sTableContextId) {
    ContentScreenStore.handleGridViewResizerButtonClicked(iWidth, sId, sTableContextId);
  };

  let handleGridOrganizeColumnButtonClicked = function (sContextId) {
    ContentScreenStore.handleGridOrganizeColumnButtonClicked(sContextId);
  };

  let handleColumnOrganizerDialogButtonClicked = function (sButtonId, sContext, aColumns) {
    ContentScreenStore.handleColumnOrganizerDialogButtonClicked(sButtonId, sContext, aColumns);
  };

  var handleGridPropertyValueChanged = function (sContentId, sPropertyId, sValue, sPathToRoot) {
    ContentScreenStore.handleGridPropertyValueChanged(sContentId, sPropertyId, sValue, sPathToRoot);
  };

  var handleGridTagPropertyValueChanged = function (sContentId, sTagId, aTagValueRelevanceData) {
    ContentScreenStore.handleGridTagPropertyValueChanged(sContentId, sTagId, aTagValueRelevanceData);
  };

  var handleGridPropertyParentExpandToggled = function (sContentId) {
    ContentScreenStore.handleGridPropertyParentExpandToggled(sContentId);
  };

  var handleGridPropertyFilterClicked = function (sContentId) {
    ContentScreenStore.handleGridPropertyFilterClicked(sContentId);
  };

  var handleGridImagePropertyImageChanged = function (sContentId, sPropertyId, aFiles) {
  };

  var handleContentHorizontalTreeNodeClicked = function (oReqData, oFilterContext) {
    ContentScreenStore.handleContentHorizontalTreeNodeClicked(oReqData, oFilterContext);
  };

  var handleContentHorizontalTreeNodeCollapseClicked = function (oReqData) {
    ContentScreenStore.handleContentHorizontalTreeNodeCollapseClicked(oReqData);
  };

  var handleContentHorizontalTreeNodeRightClicked = function (oReqData) {
    ContentScreenStore.handleContentHorizontalTreeNodeRightClicked(oReqData);
  };

  var handleContentHorizontalTreeNodePopoverClosed = function () {
    ContentScreenStore.handleContentHorizontalTreeNodePopoverClosed();
  };

  var handleContentHorizontalTreeNodeContextMenuItemClicked = function (sClickedItemId) {
    ContentScreenStore.handleContentHorizontalTreeNodeContextMenuItemClicked(sClickedItemId);
  };

  var handleContentHorizontalTreeNodeAddNewNode = function (sParentId, sNewLabel, filterContext) {
    ContentScreenStore.handleContentHorizontalTreeNodeAddNewNode(sParentId, sNewLabel, filterContext);
  };

  var handleContentHorizontalTreeNodeToggleAutomaticScrollProp = function () {
    ContentScreenStore.toggleHierarchyScrollEnableDisableProp();
  };

  var handleContentHorizontalTreeNodeDeleteIconClicked = function (sIdToDelete, iLevel, sSelectedContext, oFilterContext) {
    ContentScreenStore.handleContentHorizontalTreeNodeDeleteIconClicked(sIdToDelete, oFilterContext)
  };

  var handleContentHorizontalTreeNodeViewVisibilityModeChanged = function (sCollectionId, oFilterContext) {
    ContentScreenStore.handleContentHorizontalTreeNodeViewVisibilityModeChanged(sCollectionId, oFilterContext);
  };

  var handleTaxonomyHierarchyViewModeToggled = function (sViewMode, oFilterContext) {
    ContentScreenStore.handleTaxonomyHierarchyViewModeToggled(sViewMode, oFilterContext);
  };

  let handleTaxonomyHierarchyExpansionToggled = function (sSectionId, oFilterContext) {
    ContentScreenStore.handleTaxonomyHierarchyExpansionToggled(sSectionId, oFilterContext);
  };

  let handleCreateCloneButtonClicked = (sContext, oFilterContext) => {
    ContentScreenStore.handleCreateCloneButtonClicked(sContext, oFilterContext);
  };

  let handleCheckboxButtonClicked = (sId, sGroupId, sContext) => {
    ContentScreenStore.handleCheckboxButtonClicked(sId, sGroupId, sContext);
  };

  let handleCheckboxHeaderButtonClicked = (sKey) => {
    ContentScreenStore.handleCheckboxHeaderButtonClicked(sKey);
  };

  let handleExactCloneCheckboxClicked = () => {
    ContentScreenStore.handleExactCloneCheckboxClicked();
  };

  var handleCancelCloningButtonClicked = function (sContextId) {
    ContentScreenStore.handleCancelCloningButtonClicked(sContextId);
  };

  var handleCloneExpandSectionToggled = function (sTypeId) {
    ContentScreenStore.handleCloneExpandSectionToggled(sTypeId);
  };

  let handleSelectTypeToCreateLinkedVariant = function (sIdToRemove, sSelectedId) {
    ContentScreenStore.handleSelectTypeToCreateLinkedVariant(sIdToRemove, sSelectedId);
  };

  let handleGetAllowedTypesToCreateLinkedVariant = function () {
    ContentScreenStore.handleGetAllowedTypesToCreateLinkedVariant();
  };

  var handleCloningWizardCloneCountChanged = function (iCloneCount) {
    ContentScreenStore.handleCloningWizardCloneCountChanged(iCloneCount);
  };

  var handleUOMTableCreateRowButtonClicked = function (sContextId) {
    ContentScreenStore.handleUOMTableCreateRowButtonClicked(sContextId);
  };

  var handleUOMViewDateRangeApplied = function (oRangeData, sContextId) {
    ContentScreenStore.handleUOMViewDateRangeApplied(oRangeData, sContextId);
  };

  var handleUOMViewSortDataChanged = function (sContextId, oSortData, sTableContext) {
    ContentScreenStore.handleUOMViewSortDataChanged(sContextId, oSortData, sTableContext);
  };

  var handleUOMViewPaginationDataChanged = function (sContextId, oPaginationData, oFilterContext, sTableContext) {
    ContentScreenStore.handleUOMViewPaginationDataChanged(sContextId, oPaginationData, oFilterContext, sTableContext);
  };

  let handleUOMViewGridValueChanged = function (sContentId, sPropertyId, sValue, oViewData) {
    ContentScreenStore.handleUOMViewGridValueChanged(sContentId, sPropertyId, sValue, oViewData);
  };

  let handleUOMViewGridTagValueChanged = function (sContentId, sTagId, aTagValueRelevanceData, oExtraData, oViewData) {
    ContentScreenStore.handleUOMViewGridTagValueChanged(sContentId, sTagId, aTagValueRelevanceData, oExtraData, oViewData);
  };

  let handleUOMViewGridActionItemClicked = function (sId, sContentId, sContextId, sViewContext, oFilterData, oExtraData) {
    ContentScreenStore.handleUOMViewGridActionItemClicked(sId, sContentId, sContextId, sViewContext, oFilterData, oExtraData);
  };

  let handleUOMViewFullScreenButtonClicked = function (sContextId) {
    ContentScreenStore.handleUOMViewFullScreenButtonClicked(sContextId);
  };

  let handleUOMViewFullScreenCancelButtonClicked = function (sContextId) {
    ContentScreenStore.handleUOMViewFullScreenCancelButtonClicked(sContextId);
  };

  let handleUOMViewAddLinkedInstanceClicked = function (sContextId, oFilterContext, oViewData) {
    ContentScreenStore.handleTableAddLinkedInstance(sContextId, oFilterContext, oViewData);
  };

  let handleUOMViewRemoveLinkedInstanceClicked = function (oViewData, aSelectedItems) {
    ContentScreenStore.handleGridViewRemoveLinkedInstance(oViewData, aSelectedItems);
  };

  var handlePhysicalCatalogChanged = function () {
    ContentScreenStore.logoClicked();
  };

  var handleContextMenuButtonClicked = function (sContext) {
    ContentScreenStore.handleContextMenuButtonClicked(sContext);
  };

  var handleContextMenuListItemClicked = function (sSelectedId, sContext, oFilterContext) {
    ContentScreenStore.handleContextMenuListItemClicked(sSelectedId, sContext, oFilterContext);
  };

  var handleContextMenuListVisibilityToggled = function (bIsVisibility, sContext) {
    ContentScreenStore.handleContextMenuListVisibilityToggled(bIsVisibility, sContext);
  };

  var handleHandleDirectClickClicked = function (sSelectedId, sContext, oFilterContext) {
    ContentScreenStore.handleContextMenuListItemClicked(sSelectedId, sContext, oFilterContext);
  };

  let handleHandleTransferDialogClicked = function (sContext, sButtonId) {
    ContentScreenStore.handleHandleTransferDialogClicked(sContext, sButtonId);
  };

  let handleHandleTransferDialogItemClicked = function (sContext, sSelectedId) {
    ContentScreenStore.handleHandleTransferDialogItemClicked(sContext, sSelectedId);
  };

  let handleTransferDialogCheckBoxToggled = function (sContext) {
    ContentScreenStore.handleTransferDialogCheckBoxToggled(sContext);
  };

  var handleKpiSummaryViewKpiSelected = function (sKpiId) {
    ContentScreenStore.handleKpiSummaryViewKpiSelected(sKpiId);
  };

  var handleDashboardEndpointTileClicked = function (oEndpointData) {
    ContentScreenStore.handleDIOpenClicked(oEndpointData);
  };

  var handleShowKpiContentExplorerClicked = function (oKpiData) {
    ContentScreenStore.handleShowKpiContentExplorerClicked(oKpiData);
  };

  let handleRefreshKPIButtonClicked = function (oKpiData) {
    ContentScreenStore.handleRefreshKPIButtonClicked(oKpiData);
  };

  let handleDashboardEndpointTileRefreshClicked = function (sEndpointId, sTileMode) {
    ContentScreenStore.handleRefreshEndpoint(sEndpointId, sTileMode);
  };

  var handleMatchMergeVersionRollbackButtonClicked = function (sActionItem, sVersionId) {
    if (sActionItem === "rollback") {
      ContentScreenStore.handleTimelineRollbackVersionButtonClicked(sVersionId);
    }
  };

  var handleContentLinkedSectionsLinkItemClicked = function (sSectionId, bDoNotTrigger) {
    ContentScreenStore.handleContentLinkedSectionsLinkItemClicked(sSectionId, bDoNotTrigger);
  };

  let handleContentGridEditRowAutoSave = function (oEvent, oFilterContext) {
    ContentScreenStore.handleContentGridEditRowAutoSave(oEvent, oFilterContext);
  };

  let handleContentGridEditClose = function (oEvent) {
    ContentScreenStore.handleContentGridEditCloseButtonClicked(oEvent);
  };

  let handleAssetUploadClick = function (oContext, oEvent, sActionName, oProperties) {
    ContentScreenStore.handleAssetUploadClick(sActionName, oProperties);
  };

  let handleContentKPITileOpenDialogButtonClicked = function (sTileId) {
    ContentScreenStore.handleContentKPITileOpenDialogButtonClicked(sTileId);
  };

  let handleGoldenRecordBucketTabChanged = function (sBucketId, sTabId) {
    ContentScreenStore.handleGoldenRecordBucketTabChanged(sBucketId, sTabId);
  };

  let handleContentKPITileCloseDialogButtonClicked = function (sTileId) {
    ContentScreenStore.handleContentKPITileCloseDialogButtonClicked(sTileId);
  };

  let handleContentSliderImageClicked = function (sSelectedImageID) {
    ContentScreenStore.handleContentSliderImageClicked(sSelectedImageID);
  };

  let handleGoldenRecordBucketMergeButtonClicked = function (sBucketId) {
    ContentScreenStore.handleGoldenRecordBucketMergeButtonClicked(sBucketId);
  };

  let handleFileDrop = (sContext, aFiles, oExtraData) => {
    ContentScreenStore.handleFileDrop(sContext, aFiles, oExtraData);
  };

  let handleFileDragDropViewDraggingState = () => {
    ContentScreenStore.handleFileDragDropViewDraggingState();
  };

  let handleTabLayoutViewTabChanged = function (sTabId, sContext, oFilterContext) {
    switch (sTabId) {
      case BulkEditTabTypesConstants.PROPERTIES:
      case BulkEditTabTypesConstants.TAXONOMIES:
      case BulkEditTabTypesConstants.CLASSES:
        handleContentToolbarItemClicked(sTabId);
        break;
      default:
        ContentScreenStore.handleTabLayoutViewTabChanged(sTabId, sContext, oFilterContext);
        break;
    }
  };

  let handleGridEditablePropertiesDialogButtonClicked = function (sButtonId) {
    ContentScreenStore.handleGridEditablePropertiesConfigDialogButtonClicked(sButtonId);
  };

  let handleMultiClassificationEditIconClicked = function (sTabId, sTaxonomyId, sContext) {
    ContentScreenStore.handleMultiClassificationEditIconClicked(sTabId, sTaxonomyId, sContext);
  };

  let handleMultiClassificationDialogButtonClicked = function (sDialogButtonId, sContext) {
    ContentScreenStore.handleMultiClassificationDialogButtonClicked(sDialogButtonId,sContext);
  };

  let handleMultiClassificationTreeNodeCheckboxClicked = function (sTreeNodeId,sContext, sTaxonomyId) {
    ContentScreenStore.handleMultiClassificationTreeNodeCheckboxClicked(sTreeNodeId, sContext, sTaxonomyId);
  };

  let handleMultiClassificationCrossIconClicked = function (sLeafNodeId, sIdToRemove, sContext, sTaxonomyId) {
    ContentScreenStore.handleMultiClassificationCrossIconClicked(sLeafNodeId, sIdToRemove, sContext, sTaxonomyId);
  };

  let handleMultiClassificationTreeNodeClicked = function (sTreeNodeId, sContext) {
    ContentScreenStore.handleMultiClassificationTreeNodeClicked(sTreeNodeId, sContext);
  };

  let handleTreeViewLoadMoreClicked = function (sContext, sParentTaxonomyId, sActiveNodeId) {
    ContentScreenStore.handleTreeViewLoadMoreClicked(sContext, sParentTaxonomyId, sActiveNodeId);
  };

  let handleTaxonomyInheritanceConflictIconClicked = function () {
    ContentScreenStore.handleTaxonomyInheritanceConflictIconClicked();
  };

  let handleCloseTaxonomyInheritanceDialog = function (sDialogButtonId, oCallback) {
    ContentScreenStore.handleCloseTaxonomyInheritanceDialog(sDialogButtonId, oCallback);
  };

  let handleTaxonomyInheritanceAdaptTaxonomy = function (oBaseArticleTaxonomy, sArticleTaxonomyId, iIndex, sContentId, sParentContentId) {
    ContentScreenStore.handleTaxonomyInheritanceAdaptTaxonomy(oBaseArticleTaxonomy, sArticleTaxonomyId, iIndex, sContentId, sParentContentId);
  };

  let handleTaxonomyInheritanceAdaptAllTaxonomy = function (sContentId, sParentContentId) {
    ContentScreenStore.handleTaxonomyInheritanceAdaptAllTaxonomy(sContentId, sParentContentId);
  };

  let handleTaxonomyInheritanceRevertTaxonomy = function (oBaseArticleTaxonomy, sArticleTaxonomyId, sContentId, sParentContentId, ) {
    ContentScreenStore.handleTaxonomyInheritanceRevertTaxonomy(oBaseArticleTaxonomy, sArticleTaxonomyId, sContentId, sParentContentId);
  };

  let handleTaxonomyInheritanceRevertAllTaxonomy = function (sContentId, sParentContentId) {
    ContentScreenStore.handleTaxonomyInheritanceRevertAllTaxonomy(sContentId, sParentContentId);
  };

  let handleInformationTabRuleViolationItemClicked = function (sParentId, sSelectedRuleViolationId, sContext) {
    ContentScreenStore.handleInformationTabRuleViolationItemClicked(sParentId, sSelectedRuleViolationId, sContext);
  };

  let handleUploadAssetDialogButtonClicked = function (sButtonId, sContext, sSelectedKlassId, sRelationshipSideId, oFiles) {
    ContentScreenStore.handleUploadAssetDialogButtonClicked(sButtonId, sContext, sSelectedKlassId, sRelationshipSideId, oFiles);
  };

  let handleToolbarButtonClicked = function (sButtonId, sButtonType, bIsActive, oViewContext, oFilterContext) {
    // ContentScreenStore.handleToolbarButtonClicked(sButtonId)
    switch (sButtonType) {
      default:
      case CONSTANTS.BUTTON_TYPE_SIMPLE:
        handleContentToolbarItemClicked(sButtonId, oViewContext, oFilterContext);
        /* if(bIsActive !== undefined){
           handleToggleActiveButton(sButtonId);
         }*/
        break;

      case CONSTANTS.BUTTON_TYPE_MENU:
        handleContextMenuButtonClicked(oViewContext);
        break;
    }
  };

  var handleCommonConfigSectionSingleTextChanged = function (oContext, sContext, sKey, sVal) {
    ContentScreenStore.handleCommonConfigSectionSingleTextChanged(sContext, sKey, sVal);
  };

  var handleCommonConfigSectionViewYesNoButtonClicked = function (oContext, sKey, bValue, sConfigContext) {
    ContentScreenStore.handleCommonConfigSectionViewYesNoButtonClicked(sConfigContext, sKey, bValue);
  };

  var handleCollectionBreadCrumbItemClicked = function (oItem, sContext) {
    ContentScreenStore.handleStaticCollectionBreadCrumbItemClicked(oItem, sContext);
  };

  let handleBrowserButtonClicked = function (oItem) {
    ContentScreenStore.handleEntityNavigation(oItem);
  };

  let handleResetBreadcrumbNodePropsAccordingToContext = function (sContext, sContentId, oPostData) {
    ContentScreenStore.handleResetBreadcrumbNodePropsAccordingToContext(sContext, sContentId, oPostData);
  };

  let handleContentDataLanguageChanged = function (sDataLanguageId) {
    ContentScreenStore.handleContentDataLanguageChanged(sDataLanguageId);
  };

  let handleDataLanguageChanged = function (sDataLanguageCode, oCallback) {
    ContentScreenStore.handleDataLanguageChanged(sDataLanguageCode, oCallback);
  };

  let handleDashboardTabSelected = function (sTabId) {
    ContentScreenStore.handleDashboardTabSelected(sTabId);
  };

  let handleDeleteTranslationClicked = function (sLanguageCode) {
    ContentScreenStore.handleDeleteTranslationClicked(sLanguageCode);
  };

  let dataLanguagePopoverVisibilityToggled = function () {
    ContentScreenStore.dataLanguagePopoverVisibilityToggled();
  };

  let handleImportEntityButtonClicked = function(aFiles){
    ContentScreenStore.handleImportEntityButtonClicked(aFiles);
  };

  let handleHeaderMenuNotificationClicked = function () {
    ContentScreenStore.handleHeaderMenuNotificationClicked();
  };

  let handleContentComparisonMatchAndMergePropertyValueChanged = function (sContext, sPropertyId, sTableId, oValue) {
    ContentScreenStore.handleContentComparisonMatchAndMergePropertyValueChanged(sContext, sPropertyId, sTableId, oValue);
  };

  let handleContentComparisonMatchAndMergeViewTableRowClicked = function (sContext, sPropertyId, sTableId) {
    ContentScreenStore.handleContentComparisonMatchAndMergeViewTableRowClicked(sContext, sPropertyId, sTableId);
  };

  let handleStepperViewActiveStepChanged = function (sContext, sButtonId) {
    ContentScreenStore.handleStepperViewActiveStepChanged(sContext, sButtonId);
  };

  let handleContentComparisonMnmRelationshipPaginationChanged = function (sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId) {
    ContentScreenStore.handleContentComparisonMnmRelationshipPaginationChanged(sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId);
  };

  let handleContentComparisonMnmRemoveRelationshipButtonClicked = function (sPropertyId, sTableId, oPropertyToSave) {
    ContentScreenStore.handleContentComparisonMnmRemoveRelationshipButtonClicked(sPropertyId, sTableId, oPropertyToSave);
  };

  let handleContentComparisonMNMLanguageChanged = function (sLanguageId) {
    ContentScreenStore.handleContentComparisonMNMLanguageChanged(sLanguageId);
  };

  let handleContentComparisonFullScreenButtonClicked = function () {
    ContentScreenStore.handleContentComparisonFullScreenButtonClicked();
  };

  let handleArchiveButtonClicked = function () {
    ContentScreenStore.handleArchiveButtonClicked();
  };

  let handleRelationshipConflictPropertyClicked = function (oConflictingValue, oRelationship) {
    ContentScreenStore.handleRelationshipConflictPropertyClicked(oConflictingValue, oRelationship);
  };

  let handleRelationshipConflictPropertyResolved = function (oSelectedConflictValue, oRelationship) {
    ContentScreenStore.handleRelationshipConflictPropertyResolved(oSelectedConflictValue, oRelationship);
  };

  let handleContentEventFieldChanged = function (sContext, sKey, sValue) {
    switch (sContext) {
      case "asset":
        ContentScreenStore.handleAssetEventScheduleFieldChanged(sKey, sValue, true);
        break;
    }
  };

  let resetTreeViewData = () => {
    ContentScreenStore.resetTreeViewData();
  };

  let handleTreeViewNodeRightSideButtonClicked = (sContext, sButtonId, sNodeId) => {
    ContentScreenStore.handleTreeViewNodeRightSideButtonClicked(sContext, sButtonId, sNodeId);
  };

  let handleDragDropContextViewPropertyShuffled = (sContext, sSource, sDestination, sDraggableId) => {
  };

  let handleTreeSearched = (sContext, sSearchedText, sActiveNodeId) => {
    ContentScreenStore.handleTreeSearched(sContext, sSearchedText, sActiveNodeId);
  };

  let handleBulkDownloadDialogCheckboxClicked = (sId, iIndex, sClassId) => {
    ContentScreenStore.handleBulkDownloadDialogCheckboxClicked(sId, iIndex, sClassId);
  };

  let handleBulkDownloadDialogExpandButtonClicked = (sId, iIndex) => {
    ContentScreenStore.handleBulkDownloadDialogExpandButtonClicked(sId, iIndex);
  };

  let handleBulkDownloadDialogActionButtonClicked = (sButtonId) => {
    ContentScreenStore.handleBulkDownloadDialogActionButtonClicked(sButtonId);
  };

  let handleBulkDownloadDialogFixedSectionValueChanged = (sButtonId, oData) => {
    ContentScreenStore.handleBulkDownloadDialogFixedSectionValueChanged(sButtonId, oData);
  };

  let handleBulkDownloadDialogChildElementValueChanged = (sId, sValue, sClassId, iParentIndex) => {
    ContentScreenStore.handleBulkDownloadDialogChildElementValueChanged(sId, sValue, sClassId, iParentIndex);
  };

  let handleTaxonomyHideEmptyToggleClicked = (bHideEmpty, oFilterContext) => {
    ContentScreenStore.handleTaxonomyHideEmptyToggleClicked(bHideEmpty, oFilterContext);
  };

  let handleMenuListToggleButtonClicked = (bHideEmpty, oFilterContext) => {
    ContentScreenStore.handleMenuListToggleButtonClicked(bHideEmpty, oFilterContext);
  };

  let handleCategoriesButtonClicked = (sId, oFilterContext, oTaxonomyTreeRequestData) => {
    ContentScreenStore.handleCategoriesButtonClicked(sId, oFilterContext, oTaxonomyTreeRequestData);
  };

  let handleAllCategoriesSelectorTreeNodeCheckboxClicked = function (sContext, sTreeNodeId, oFilterContext) {
    ContentScreenStore.handleAllCategoriesSelectorTreeNodeCheckboxClicked(sContext, sTreeNodeId, oFilterContext);
  };

  let handleAllCategoriesSelectorTreeNodeClicked = function (sTreeNodeId,oFilterContext, oTaxonomyTreeRequestData) {
    ContentScreenStore.handleAllCategoriesSelectorTreeNodeClicked(sTreeNodeId, oFilterContext, oTaxonomyTreeRequestData);
  };

  let handleAllCategoriesSelectorLoadMoreClicked = function (sSelectedCategory, sParentId, oTaxonomyTreeRequestData, oFilterContext) {
    ContentScreenStore.handleAllCategoriesSelectorLoadMoreClicked(sSelectedCategory, sParentId, oTaxonomyTreeRequestData, oFilterContext);
  };

  let handleAllCategoriesSelectorCancelButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleAllCategoriesSelectorCancelButtonClicked(oFilterContext);
  };

  let handleAllCategoriesSelectorApplyButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleAllCategoriesSelectorApplyButtonClicked(oFilterContext);
  };

  let handleSummaryClearSelectionButtonClicked = function (oFilterContext) {
    ContentScreenStore.handleSummaryClearSelectionButtonClicked(oFilterContext);
  };

  let handleSummaryCrossButtonClicked = function (context, sButtonId, sSelectedId, oFilterContext) {
    ContentScreenStore.handleSummaryCrossButtonClicked(context, sButtonId, sSelectedId, oFilterContext);
  };

  let handleAllCategoriesTreeViewSearched = function (oFilterContext, sSearchText, oTaxonomyTreeRequestData) {
    ContentScreenStore.handleAllCategoriesTreeViewSearched(oFilterContext, sSearchText, oTaxonomyTreeRequestData);
  };


  /**
   * Binding Events into EventHandler
   */
  (() => {

    /** @deprecated */
    const _setEvent = CS.set.bind(this, oEventHandler);

    oEventHandler[ThumbnailEvents.THUMB_SINGLE_CLICKED] = ActionInterceptor('Content Thumbnail Single Clicked', handleThumbnailSingleClicked);
    oEventHandler[ThumbnailTemplateViewEvents.THUMB_CHECKBOX_CLICKED] = ActionInterceptor('Content Thumbnail Single Clicked', handleThumbnailCheckboxClicked);

    oEventHandler[ThumbnailActionItemEvents.ACTION_ITEM_DELETE_CLICKED] = ActionInterceptor('Content Thumbnail Delete Clicked', handleThumbnailDeleteIconClicked);
    oEventHandler[ThumbnailActionItemEvents.ACTION_ITEM_REMOVE_CLICKED] = ActionInterceptor('Content Thumbnail Remove Clicked', handleRemoveContentsButtonClicked);
    oEventHandler[ThumbnailActionItemEvents.ACTION_ITEM_CLONE_CLICKED] = ActionInterceptor('Content Thumbnail clone Clicked', handleThumbnailCloneIconClicked);
    oEventHandler[ThumbnailActionItemEvents.ACTION_ITEM_EDIT_RELATIONSHIP_VARIANT_CLICKED] = ActionInterceptor('Content Thumbnail Edit Relationship Variant Clicked', handleThumbnailRelationshipEditVariantIconClicked);

    oEventHandler[ThumbnailEvents.THUMB_CONTEXT_MENU_CLICKED] = ActionInterceptor('Content Thumbnail Context Menu (Right Click) Clicked', handleThumbnailRightClick);
    oEventHandler[ThumbnailEvents.THUMB_CONTEXT_MENU_POPOVER_CLOSED] = ActionInterceptor('Content Thumbnail Context Menu PopOver Closed', handleThumbnailContextMenuPopOverClosed);
    oEventHandler[ThumbnailEvents.THUMB_CONTEXT_MENU_ITEM_CLICKED] = ActionInterceptor('Content Thumbnail Context Menu Item Clicked', handleThumbnailContextMenuItemClicked);

    oEventHandler[ThumbnailActionItemEvents.ACTION_ITEM_CREATE_VARIANT_CLICKED] = ActionInterceptor('Content Thumbnail Create Variant Clicked', handleThumbnailCreateVariantIconClicked);
    oEventHandler[ThumbnailActionItemEvents.ACTION_ITEM_RESTORE_CLICKED] = ActionInterceptor('Content Thumbnail Restore Clicked', handleThumbnailRestoreClicked);

    oEventHandler[ContentTileEvents.CONTENT_TILE_PAGINATION_CHANGED] = ActionInterceptor('Content Tile Pagination Changed', handleContentTilePaginationChanged);

    oEventHandler[EntityDetailViewEvents.TEMPLATE_DROP_DOWN_LIST_NODE_CLICKED] = ActionInterceptor('Template Drop Down List Node Clicked', handleTemplateDropDownListNodeClicked);
    oEventHandler[EntityDetailViewEvents.HANDLE_KPI_CHART_TYPE_SELECTED] = ActionInterceptor('add nature or non nature class load more handler', handleKpiChartTypeSelected);
    oEventHandler[EntityDetailViewEvents.LINKED_ITEM_BUTTON_CLICKED] = ActionInterceptor('Entity detail view, linked section button clicked', handleLinkedSectionButtonClicked);

    /** Content tab specific view **/
    oEventHandler[ContentTabSpecificViewEvents.CONTENT_TAB_SPECIFIC_COMPARISION_BUTTON_CLICKED] = ActionInterceptor('Entity Detail Comparision Dialog Button Clicked', handleEntityDetailComparisionDialogButtonClick);

    oEventHandler[EntityDetailRelationshipSectionViewEvents.ENTITY_DETAIL_RELATIONSHIP_SECTION_PAGINATION_CHANGED] = ActionInterceptor('Entity detail relationship section pagination chnaged', handleRelationshipPaginationChanged);
    oEventHandler[EntityDetailRelationshipSectionViewEvents.ENTITY_DETAIL_RELATIONSHIP_CONTEXT_NEXT_CLICKED] = ActionInterceptor('Entity detail relationship context next clicked', handleRelationshipContextNextButtonClicked);
    oEventHandler[EntityDetailRelationshipSectionViewEvents.ENTITY_DETAIL_RELATIONSHIP_CONTEXT_CREATE_VARIANT_CLICKED] = ActionInterceptor('Entity detail relationship context next clicked', handleRelationshipContextCreateVariantClicked);
    oEventHandler[EntityDetailRelationshipSectionViewEvents.ENTITY_DETAIL_RELATIONSHIP_CONTEXT_CANCEL_CLICKED] = ActionInterceptor('Entity detail relationship context cancel clicked', handleRelationshipContextCancelButtonClicked);
    oEventHandler[EntityDetailRelationshipSectionViewEvents.ENTITY_DETAIL_RELATIONSHIP_CONTEXT_SAVE_CLICKED] = ActionInterceptor('Entity detail relationship context save clicked', handleRelationshipContextSaveButtonClicked);

    oEventHandler[RelationshipSectionElementViewEvents.ADD_ENTITY_BUTTON_CLICKED] = ActionInterceptor('Entity detail view events', handleAddEntityButtonClicked);
    oEventHandler[RelationshipSectionElementViewEvents.ARTICLE_DETAIL_VIEW_SECTION_CLICKED] = ActionInterceptor('Article Children Section CLicked', handleSetSectionClicked);
    oEventHandler[RelationshipSectionElementViewEvents.MAKE_DEFAULT_ASSET_BUTTON_CLICKED] = ActionInterceptor('Make default asset button clicked', handleMakeDefaultImageButtonClicked);


    oEventHandler[QuickListView.ENTITY_RELATIONSHIP_OK_BUTTON_CLICKED] = ActionInterceptor('Entity detail view events', handleRelationshipPresentEntityOkButtonClicked);
    oEventHandler[QuickListView.ENTITY_RELATIONSHIP_DELETE_BUTTON_CLICKED] = ActionInterceptor('Entity detail view events', handlePresentEntityDeleteButtonClicked);
    oEventHandler[QuickListView.ENTITY_RELATIONSHIP_SELECT_ALL_BUTTON_CLICKED] = ActionInterceptor('Entity detail view events', handlePresentEntitySelectAllButtonClicked);
    oEventHandler[QuickListView.ENTITY_RELATIONSHIP_ADD_PAGINATION_CHANGED] = ActionInterceptor('Entity relationship pagination changed', handlePresentEntityPaginationChanged);

    oEventHandler[TagTreeParentNodeViewEvents.TAG_NODE_CLICKED] = ActionInterceptor('Tag Node Clicked', handleTagNodeCLicked);
    oEventHandler[TagTreeGroupViewEvents.TAG_GROUP_TAG_VALUE_CHANGED] = ActionInterceptor('Tag Group Tag value changed', handleTagGroupTagValueChanged);

    oEventHandler[DragViewEvents.DRAG_VIEW_ON_DRAG_START_EVENT] = ActionInterceptor('Drag View', handleContentEntityDragStart);
    oEventHandler[DragViewEvents.DRAG_VIEW_ON_DRAG_END_EVENT] = ActionInterceptor('Drag View', handleContentEntityDragEnd);
    oEventHandler[DropViewEvents.DROP_VIEW_ON_DROP_EVENT] = ActionInterceptor('Drag View', handleContentEntityDrop);

    oEventHandler[DragNDropViewEvents.DRAG_N_DROP_VIEW_ON_DRAG_START_EVENT] = ActionInterceptor('Drag Start Event Triggered', handleContentEntityDragStart);
    oEventHandler[DragNDropViewEvents.DRAG_N_DROP_VIEW_ON_DRAG_END_EVENT] = ActionInterceptor('Drag End Event Raised', handleContentEntityDragEnd);
    oEventHandler[DragNDropViewEvents.DRAG_N_DROP_VIEW_ON_DROP_EVENT] = ActionInterceptor('Drop Event Raised', handleContentEntityDrop);
    oEventHandler[DragNDropViewEvents.DRAG_N_DROP_VIEW_ON_DRAG_ENTER_EVENT] = ActionInterceptor('Drag Enter EventRaised', handleContentEntityDragEnter);

    oEventHandler[MultiSelectViewNewEvents.MULTI_SELECT_POPOVER_CLOSED] = ActionInterceptor('MultiSelectSearch View', handleMultiSelectPopoverClosed);
    oEventHandler[MultiSelectViewNewEvents.MULTI_SEARCH_BAR_CROSS_ICON_CLICKED] = ActionInterceptor('MultiSelectSearch View', handleMultiSelectSearchCrossIconClicked);

    oEventHandler[SingleSelectSearchViewEvents.UNIQUE_SELECTION_ITEM_CLICKED] = ActionInterceptor('UniqueSelectSearch View', handleUniqueSelectionItemClicked);

    /* add taxonomy view events */
    oEventHandler[AddTaxonomyPopoverViewEvents.TAXONOMY_DROPDOWN_OPENED] = ActionInterceptor('Add Taxonomy Popover Item View Dropdown', handleAllowedTypesDropdownOpened);
    oEventHandler[AddTaxonomyPopoverViewEvents.ADD_TAXONOMY_POPOVER_ITEM_CLICKED] = ActionInterceptor('add taxonomy popover item View', handleAddTaxonomyPopoverItemClicked);
    oEventHandler[AddTaxonomyPopoverViewEvents.TAXONOMY_DROPDOWN_SEARCH_HANDLER] = ActionInterceptor('taxonomy dropdown search handler', handleAllowedTypesSearchHandler);
    oEventHandler[AddTaxonomyPopoverViewEvents.TAXONOMY_DROPDOWN_LOAD_MORE_HANDLER] = ActionInterceptor('taxonomy load more', handleAllowedTypesLoadMoreHandler);

    /* small taxonomy view events */
    oEventHandler[SmallTaxonomyViewEvents.MULTI_TAXONOMY_CROSS_ICON_CLICKED] = ActionInterceptor('EntityDetail View', handleMultiSelectSmallTaxonomyViewCrossIconClicked);

    oEventHandler[ContentSectionElementViewNewEvents.NEW_CONTENT_SECTION_ATTRIBUTE_CLICKED] = ActionInterceptor('New Content Attribute Item Clicked', handleSectionAttributeClicked);
    oEventHandler[ContentSectionElementViewNewEvents.NEW_CONTENT_SECTION_MASK_CLICKED] = ActionInterceptor('New Content Attribute Item Clicked', handleSectionMaskClicked);
    oEventHandler[ContentSectionElementViewNewEvents.NEW_CONTENT_SECTION_VIEW_ATTRIBUTE_DEFAULT_VALUE_CHANGED] = ActionInterceptor('New Content Section Attribute Default Value Changed', handleSectionAttributeDefaultValueChanged);
    oEventHandler[ContentSectionElementViewNewEvents.NEW_CONTENT_SECTION_VIEW_TAG_DEFAULT_VALUE_CHANGED] = ActionInterceptor('New Content Section Tag Default Value Changed', handleSectionTagDefaultValueChanged);
    oEventHandler[ContentSectionElementViewNewEvents.NEW_CONTENT_SECTION_ELEMENT_NOTIFICATION_BUTTON_CLICKED] = ActionInterceptor('New Content Section Element Toolbar Button Clicked', handleSectionElementNotificationButtonClicked);
    oEventHandler[ContentSectionElementViewNewEvents.NEW_CONTENT_SECTION_ELEMENT_CROSS_ICON_CLICKED] = ActionInterceptor('handleBulkEditRemoveProperty', handleBulkEditRemoveProperty);
    oEventHandler[ContentSectionElementViewNewEvents.NEW_CONTENT_SECTION_ELEMENT_CONFLICT_ICON_CLICKED] = ActionInterceptor('Content Section Elements Conflict Icon Clicked', handleSectionElementConflictIconClicked);

    oEventHandler[ContentSectionViewNewEvents.NEW_CONTENT_SECTION_ROLE_MSS_VALUE_CHANGED] = ActionInterceptor('New Content Section Role MSS Value Changed', handleContentSectionRoleMSSValueChanged);

    oEventHandler[ContentSectionAttributeViewEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_TEXT_VAL_CHANGED] = ActionInterceptor('Content Edit Material UI View', handleAttributeVariantInputTextChanged);
    oEventHandler[ContentSectionAttributeViewEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_MEASUREMENT_METRIC_VAL_CHANGED] = ActionInterceptor('Content Measurement Metrics View', handleAttributeVariantInputTextChanged);
    oEventHandler[ContentSectionAttributeViewEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_CONCATENATED_EXPRESSION_CHANGED] = ActionInterceptor('Content Concatenated Attribute View', handleConcatenatedAttributeExpressionChanged);

    oEventHandler[CustomFroalaViewEvents.CONTENT_FROALA_DATA_CHANGED] = ActionInterceptor('Content Structure View', handleStructureFroalaValueChanged);
    oEventHandler[CustomFroalaViewEvents.CONTENT_STRUCTURE_FROALA_IMAGE_UPLOADED] = ActionInterceptor('Content Structure View', handleStructureFroalaImageUploaded);
    oEventHandler[CustomFroalaViewEvents.CONTENT_STRUCTURE_FROALA_IMAGES_REMOVED] = ActionInterceptor('Content Structure View', handleStructureFroalaImagesRemoved);

    oEventHandler[PaperViewEvents.PAPER_VIEW_HEADER_CLICKED] = ActionInterceptor('Toggle paper collapse', handleSectionHeaderClicked);
    oEventHandler[PaperViewEvents.PAPER_VIEW_REMOVE_ICON_CLICKED] = ActionInterceptor('Remove paper', handleSectionRemoveClicked);
    oEventHandler[PaperViewEvents.PAPER_VIEW_SKIP_ICON_CLICKED] = ActionInterceptor('Skip paper', handleSectionSkipToggled);

    oEventHandler[ThumbnailScrollerViewEvents.THUMBNAIL_SCROLLER_LOAD_MORE] = ActionInterceptor('Fetch Next Relationship Element List', handleThumbnailScrollerLoadMore);

    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_DELETE_CLICKED] = ActionInterceptor('Attribute Delete', handleFilterAttributeDeleteClicked);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_EXPAND_CLICKED] = ActionInterceptor('Attribute Expand', handleFilterAttributeExpandClicked);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_TYPE_CLICKED] = ActionInterceptor('ValueType Changed', handleFilterAttributeValueTypeChanged);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ADD_ATTRIBUTE_CLICKED] = ActionInterceptor('ValueType Changed', handleAddAttributeValueClicked);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED] = ActionInterceptor('Value Changed', handleFilterAttributeValueChanged);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE] = ActionInterceptor('Value Changed', handleFilterAttributeValueChangedForRange);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_DELETE_CLICKED] = ActionInterceptor('Delete', handleFilterAttributeValueDeleteClicked);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_USER_VALUE_CHANGED] = ActionInterceptor('User Value Changed', handleContentFilterUserValueChanged);

    //TimelineComparisonEvents
    oEventHandler[TimelineComparisonEvents.COMPARISON_VIEW_LANGUAGE_FOR_COMPARISON_CHANGED] = ActionInterceptor('comparison_view_language_for_comparison_changed', handleLanguageForComparisonChanged);
    oEventHandler[TimelineComparisonEvents.CONTENT_TIMELINE_COMPARISON_BUTTON_CLICKED] = ActionInterceptor('content_timeline_comparison_button_clicked', handleContentTimelineComparisonButtonClicked);

    //LIST
    oEventHandler[ListNodeViewEvents.LIST_NODE_CLICKED] = ActionInterceptor('Set Node Clicked', handleListNodeClicked);
    oEventHandler[ListNodeViewEvents.LIST_NODE_CHECKBOX_CLICKED] = ActionInterceptor('Set Node Checked', handleListNodeChecked);

    //CONTENT TOOLBAR
    oEventHandler[BulkUploadButtonViewEvents.CONTENT_ASSET_BULK_UPLOAD_EVENT] = ActionInterceptor('Content Asset Bulk Upload Event', handleContentAssetBulkUploadButtonClicked);
    oEventHandler[BulkUploadButtonViewEvents.BULK_UPLOAD_TO_COLLECTION_CLICKED] = ActionInterceptor('Bulk Upload To Collection Clicked', handleBulkUploadToCollectionClicked);
    oEventHandler[BulkUploadButtonViewEvents.BULK_UPLOAD_TO_COLLECTION_CLOSED] = ActionInterceptor('Bulk Upload To Collection Closed', handleBulkUploadToCollectionClosed);

    //BULK ASSET LINK SHARING
    oEventHandler[BulkDownloadAndLinkSharingButtonViewEvents.BULK_ASSET_LINK_SHARING_ACTION_ITEM_CLICKED] = ActionInterceptor('bulk_asset_link_sharing_action_item_clicked', handleBulkAssetLinkSharingActionItemClicked);

    oEventHandler[OnboardingFileUploadButtonViewEvents.ONBOARDING_FILE_UPLOAD_EVENT] = ActionInterceptor('Bulk Upload To Collection Closed', handleOnboardingFileUploaded);
    oEventHandler[OnboardingFileUploadButtonViewEvents.PRODUCTS_RUNTIME_IMPORT_BUTTON_CLICKED] = ActionInterceptor('content screen controller import button clicked',handleImportEntityButtonClicked);
    oEventHandler[OnboardingFileUploadButtonViewEvents.ONBOARDING_FILE_UPLOAD_CANCEL] = ActionInterceptor('File Upload Cancel', handleOnboardingFileUploadCancel);

    oEventHandler[CreateButtonViewEvents.CONTENT_CREATE_BUTTON_VIEW_ITEM_CLICKED] = ActionInterceptor('Content Create Button View Item Clicked', handleVerticalMaterialMenuItemClicked);
    oEventHandler[CreateButtonViewEvents.CREATE_BUTTON_CLICKED] = ActionInterceptor('Create Button Clicked', handleModuleCreateButtonClicked);

    oEventHandler[FlatCreateButtonViewEvents.FLAT_CREATE_BUTTON_VIEW_BTN_CLICKED] = ActionInterceptor('Flat Create Button View Item Clicked', handleFlatButtonViewBtnClicked);

    //BREADCRUMB
    oEventHandler[BreadcrumbViewEvent.BREADCRUMB_ITEM_CLICKED] = ActionInterceptor('Breadcrumb Item Clicked', handleBreadcrumbItemClicked);

    //METRIC LENGTH EXCEEDED
    oEventHandler[ContentMeasurementMetricsEvents.METRIC_LENGTH_EXCEEDED] = ActionInterceptor('Metric Length Exceeded', handleMetricValueExceeded);
    oEventHandler[ContentMeasurementMetricsEvents.METRIC_UNIT_CHANGED] = ActionInterceptor('Metric Unit Changed', handleMetricUnitChanged);

    //FILTER VIEW EVENTS
    oEventHandler[FltrHorizontalTreeViewEvents.HANDLE_TREE_CHECK_CLICKED] = ActionInterceptor('Tree Check Clicked', handleTreeCheckClicked);
    oEventHandler[FltrHorizontalTreeViewEvents.HANDLE_TREE_HEADER_CHECK_CLICKED] = ActionInterceptor('Tree Header Check Clicked', handleTreeHeaderCheckClicked);
    oEventHandler[FltrHorizontalTreeViewEvents.HANDLE_TREE_LAZY_CHILDREN_DATA] = ActionInterceptor('Tree Lazy Children Data Load', handleTaxonomyChildrenLazyData);

    oEventHandler[FltrTaxonomySelectorViewEvents.HANDLE_TREE_NODE_TOGGLE_CLICKED] = ActionInterceptor('Tree Node Toggle Clicked', handleTreeNodeToggleClicked);
    oEventHandler[FltrTaxonomySelectorViewEvents.HANDLE_TAXONOMY_SEARCH_TEXT_CHANGED] = ActionInterceptor('Taxonomy Search Text Changed', handleTaxonomySearchTextChanged);
    oEventHandler[FltrTaxonomySelectorViewEvents.HANDLE_SELECTED_TAXONOMIES_BUTTON_CLICKED] = ActionInterceptor('Selected Taxonomies Button Clicked', handleSelectTaxonomiesButtonClicked);
    oEventHandler[FltrTaxonomySelectorViewEvents.FLTR_TAXONOMY_SELECTOR_APPLY_BUTTON_CLICKED] = ActionInterceptor('ChooseTaxonomy Apply Button Clicked', handleChooseTaxonomyApplyButtonClicked);
    oEventHandler[FltrTaxonomySelectorViewEvents.HANDLE_CANCEL_BUTTON_CLICKED] = ActionInterceptor('Handle Cancel Button Clicked', handleTaxonomyCancelButtonClicked);
    oEventHandler[FltrTaxonomySelectorViewEvents.FLTR_TAXONOMY_SELECTOR_CLEAR_ALL_CLICKED] = ActionInterceptor('Handle Clear All Button Clicked', handleTaxonomyClearAllClicked);
    oEventHandler[FltrTaxonomySelectorViewEvents.HANDLE_ORGANISE_TAXONOMY_BUTTON_CLICKED] = ActionInterceptor('Handle Organise Taxonomy Clicked', handleOrganiseTaxonomyButtonClicked);
    oEventHandler[FltrTaxonomySelectorViewEvents.HANDLE_SHOW_TAXONOMIES_POP_OVER_STATE] = ActionInterceptor('Handle Show Taxonomy Pop Over State Changed', handleTaxonomyShowPopOverStateChanged);
    oEventHandler[FltrTaxonomySelectorViewEvents.FLTR_TAXONOMY_SELECTOR_HIDE_EMPTY_TOGGLE_CLICKED] = ActionInterceptor('fltr taxonomy selector hide empty toggle clicked', handleTaxonomyHideEmptyToggleClicked);

    oEventHandler[FltrSortByViewEvents.HANDLE_SORT_BY_ITEM_CLICKED] = ActionInterceptor('Sort By Item Clicked', handleSortByItemClicked);
    oEventHandler[FltrSortByViewEvents.HANDLE_SORT_ORDER_TOGGLED] = ActionInterceptor('Sort Order Toggled', handleFilterSortOrderToggled);
    oEventHandler[FltrSortByViewEvents.HANDLE_SORT_REORDERED] = ActionInterceptor('Sort Order Changed', handleFilterSortReOrdered);
    oEventHandler[FltrSortByViewEvents.FILTER_SORT_BY_ACTIVATED_ITEM_CLICKED] = ActionInterceptor('Sort Order Changed', handleFilterSortActivatedItemClicked);
    oEventHandler[FltrSortByViewEvents.FILTER_SORT_BY_DEACTIVATED_ITEM_CLICKED] = ActionInterceptor('Sort Active Clicked', handleFilterSortDeactivatedItemClicked);

    oEventHandler[FilterViewEvents.HANDLE_TREE_CHECK_CLICKED] = ActionInterceptor('Tree Check Clicked', handleTreeCheckClicked);
    oEventHandler[FilterViewEvents.HANDLE_APPLIED_TAXONOMY_REMOVED_CLICKED] =
        ActionInterceptor('applied taxonomy removed clicked', handleAppliedTaxonomyRemovedClicked);
    oEventHandler[FilterViewEvents.HANDLE_FILTER_SHOW_DETAILS_CLICKED] = ActionInterceptor('Show/Hide filter details', handleFilterShowDetailsClicked);
    oEventHandler[FilterViewEvents.HANDLE_SELECTED_TAXONOMIES_CLEAR_ALL_CLICKED] = ActionInterceptor('Selected Taxonomies Clear All Clicked', handleSelectedTaxonomiesClearAllClicked);
    oEventHandler[FltrAvailableFiltersViewEvents.HANDLE_ADVANCED_SEARCH_BUTTON_CLICKED] = ActionInterceptor('Handle Advanced Search button CLicked', handleAdvancedSearchButtonClicked);
    oEventHandler[FltrAvailableFiltersViewEvents.HANDLE_LOAD_MORE_FILTER_BUTTON_CLICKED] = ActionInterceptor('Handle Load More Filter Button Clicked', handleLoadMoreFilterButtonClicked);
    oEventHandler[AppliedFilterViewEvents.CLEAR_ALL_APPLIED_FILTERS] = ActionInterceptor('Clear all applied filters', handleClearAllAppliedFilterClicked);
    oEventHandler[AppliedFilterViewEvents.REMOVE_APPLIED_GROUP_FILTER] = ActionInterceptor('Remove group filter', handleRemoveFilterGroupClicked);
    oEventHandler[AvailableFilterItemViewEvents.CHILD_FILTER_TOGGLE_CLICKED] = ActionInterceptor('Child filter toggled', handleChildFilterToggled);
    oEventHandler[AvailableFilterItemViewEvents.HANDLE_FILTER_SEARCH_TEXT_CHANGED] = ActionInterceptor('Search text changed', handleFilterSearchTextChanged);
    oEventHandler[FltrSearchBarViewEvents.HANDLE_SUBMIT_SEARCH_TEXT] = ActionInterceptor('Search text submit', handleSubmitFilterSearchText);
    oEventHandler[AvailableFilterItemWrapperViewEvents.APPLY_FILTER_ON_TAG_GROUP_CLOSE] = ActionInterceptor('Tag change for filter', handleFilterButtonClicked);
    oEventHandler[AvailableFilterItemWrapperViewEvents.APPLY_LAZY_FILTER] = ActionInterceptor('apply lazy filter', handleApplyLazyFilter);
    oEventHandler[AvailableFilterItemWrapperViewEvents.DISCARD_FILTER_ON_TAG_GROUP_CLOSE] = ActionInterceptor('discard filter on tag group close', handleFilterPopoverClosed);

    oEventHandler[ContentHierarchyTaxonomySectionsViewEvents.HANDLE_TAXONOMY_SECTION_ELEMENT_INPUT_CHANGED] = ActionInterceptor('Taxonomy Section Input Changed', handleTaxonomySectionInputChanged);
    oEventHandler[ContentHierarchyTaxonomySectionsViewEvents.HANDLE_TAXONOMY_HIERARCHY_VIEW_MODE_TOGGLED] = ActionInterceptor('Taxonomy Section Tag Dialog Closed', handleTaxonomyHierarchyViewModeToggled);
    oEventHandler[ContentHierarchyTaxonomySectionsViewEvents.HANDLE_TAXONOMY_HIERARCHY_SECTION_EXPAND_BUTTON_TOGGLED] = ActionInterceptor('Taxonomy Section Expand Button Toggled', handleTaxonomyHierarchyExpansionToggled);

    oEventHandler[AdvancedSearchPanelViewEvents.CONTENT_FILTER_BUTTON_CLICKED] = ActionInterceptor('List Item Node Clicked', handleAdvancedSearchFilterButtonCLicked);
    oEventHandler[AdvancedSearchPanelViewEvents.CONTENT_FILTER_CLEAR_BUTTON_CLICKED] = ActionInterceptor('List Item Node Clicked', handleAdvanceSearchClearButtonClicked);
    oEventHandler[AdvancedSearchPanelViewEvents.HANDLE_ADVANCED_SEARCH_CANCEL_CLICKED] = ActionInterceptor('List Item Node Clicked', handleAdvancedSearchPanelCancelClicked);

    oEventHandler[BulkEditViewEvents.BULK_EDIT_CANCEL_BUTTON_CLICKED] = ActionInterceptor('Bulk Edit cancel clicked', handleBulkEditCancelClicked);
    oEventHandler[BulkEditViewEvents.BULK_EDIT_APPLY_BUTTON_CLICKED] = ActionInterceptor('Bulk edit apply clicked', handleBulkEditApplyButtonClicked);
    oEventHandler[BulkEditViewEvents.BULK_EDIT_GET_TREE_NODE_CHILDREN] = ActionInterceptor('Bulk edit get children', handleBulkEditGetTreeNodeChildren);
    oEventHandler[BulkEditViewEvents.BULK_EDIT_PROPERTY_CHECKBOX_CLICKED] = ActionInterceptor('Bulk edit property selected', handleBulkEditPropertyCheckboxClicked);
    oEventHandler[BulkEditViewEvents.BULK_EDIT_TREE_NODE_RIGHT_SIDE_BUTTON_CLICKED] = ActionInterceptor('Bulk edit tree node right side button clicked', handleTreeViewNodeRightSideButtonClicked);

    oEventHandler[BulkEditAppliedElementsViewEvents.BULK_EDIT_APPLIED_PROPERTIES_VALUE_CHANGED] = ActionInterceptor('value changed', handleBulkEditPropertyValueChanged);
    oEventHandler[ActionableChipsViewEvents.ACTIONABLE_CHIPS_VIEW_CROSS_ICON_CLICKED] = ActionInterceptor('cross icon clicked', handleActionableChipsViewCrossIconClicked);

    oEventHandler[ItemListPanelViewEvents.HANDLE_LIST_SEARCHED] = ActionInterceptor('Item List Panel View Events', handleAdvancedSearchListSearched);
    oEventHandler[ItemListPanelViewEvents.HANDLE_SEARCH_LIST_ITEM_NODE_CLICKED] = ActionInterceptor('Item List Panel View Events', handleAdvancedSearchListItemNodeClicked);
    oEventHandler[ItemListPanelViewEvents.HANDLE_SEARCH_LIST_LOAD_MORE_CLICKED] = ActionInterceptor('Item List Panel View Events', handleAdvancedSearchListLoadMoreClicked);

    //COLLECTION EVENTS
    oEventHandler[CollectionCreationViewEvents.HANDLE_CREATE_NEW_COLLECTION_BUTTON_CLICKED] = ActionInterceptor('Create Collection', handleCreateNewCollectionButtonClicked);
    oEventHandler[CollectionCreationViewEvents.HANDLE_MODIFY_COLLECTION_CLICKED] = ActionInterceptor('Modify Collection', handleModifyCollectionClicked);
    oEventHandler[CollectionOptionsViewEvents.HANDLE_CREATE_DYNAMIC_COLLECTION_BUTTON_CLICKED] = ActionInterceptor('Create Dynamic Collections', handleCreateDynamicCollectionButtonClicked);
    oEventHandler[CollectionOptionsViewEvents.HANDLE_CREATE_STATIC_COLLECTION_BUTTON_CLICKED] = ActionInterceptor('Create Static Collections', handleCreateStaticCollectionButtonClicked);
    oEventHandler[CollectionOptionsViewEvents.HANDLE_COLLECTION_BREADCRUMB_RESET] = ActionInterceptor('Static Collection BreadCrumb Reset ', handleCollectionBreadcrumbReset);
    oEventHandler[CollectionCreationViewEvents.HANDLE_COLLECTION_SELECTED] = ActionInterceptor('Handle collection selected', handleCollectionSelected);
    oEventHandler[CollectionCreationViewEvents.HANDLE_DELETE_COLLECTION_CLICKED] = ActionInterceptor('Handle Delete Collection Clicked', handleDeleteCollectionClicked);
    oEventHandler[CollectionCreationViewEvents.HANDLE_NEXT_COLLECTION_CLICKED] = ActionInterceptor('Handle Collection Next Clicked', handleNextCollectionClicked);
    oEventHandler[CollectionCreationViewEvents.HANDLE_STATIC_COLLECTION_BACK_BUTTON_CLICKED] = ActionInterceptor('Handle static collection back button clicked', handleStaticCollectionBackButtonClicked);
    oEventHandler[CollectionCreationViewEvents.HANDLE_STATIC_COLLECTION_ORGANISE_ICON_CLICKED] = ActionInterceptor('Handle static collection organise button clicked', handleStaticCollectionOrganiseButtonClicked);
    oEventHandler[CollectionCreationViewEvents.HANDLE_STATIC_COLLECTION_ROOT_BREADCRUMB_CLICKED] = ActionInterceptor('Handle static collection root bread crumb clicked', handleStaticCollectionRootBreadCrumbClicked);
    oEventHandler[CollectionCreationViewEvents.HANDLE_COLLECTION_VISIBILITY_MODE_CHANGED] = ActionInterceptor('Handle collection visibility mode changed', handleCollectionItemVisibilityModeChanged);
    oEventHandler[CollectionCreationViewEvents.HANDLE_COLLECTION_CREATION_VIEW_BREADCRUMB_ITEM_CLICKED] = ActionInterceptor('Handle collection visibility mode changed', handleCollectionBreadCrumbItemClicked);

    oEventHandler[HeaderViewEvents.HEADER_VIEW_SAVE_CLICKED] = ActionInterceptor('Active Collection Save Clicked', handleActiveCollectionSaveClicked);
    oEventHandler[HeaderViewEvents.HEADER_VIEW_COLLECTION_LABEL_CHANGED] = ActionInterceptor('Active Collection Label Changed', handleActiveCollectionLabelChanged);
    oEventHandler[HeaderViewEvents.HEADER_VIEW_COLLECTION_VISIBILITY_CHANGED] = ActionInterceptor('Active Collection Visibility Changed', handleActiveCollectionVisibilityChanged);
    oEventHandler[HeaderViewEvents.HEADER_VIEW_COLLECTION_COMMENT_CHANGE] = ActionInterceptor('Active Collection Save Comment events', handleCollectionSaveCommentChanged);

    //ThumbnailInformationViewEvents
    oEventHandler[ThumbnailInformationViewEvents.THUMBNAIL_INFORMATION_STATIC_COLLECTION_BUTTON_CLICKED] = ActionInterceptor('Add to Collection clicked', handleCreateStaticCollectionButtonClicked);
    oEventHandler[ThumbnailInformationViewEvents.THUMBNAIL_INFORMATION_MODIFY_STATIC_COLLECTION_CLICKED] = ActionInterceptor('Single Content add to collection clicked', handleSingleContentAddToStaticCollectionClicked);
    oEventHandler[ThumbnailInformationViewEvents.THUMBNAIL_INFORMATION_SET_VIOLATION_EVENT_DATA] = ActionInterceptor('Single Content add to collection clicked', handleThumbnailInformationViolationIconClicked);
    oEventHandler[ThumbnailInformationViewEvents.THUMBNAIL_INFORMATION_VIEW_ENTITY_INFO_ICON_CLICKED] = ActionInterceptor('thumbnail entity info icon clicked', handleThumbnailInformationEntityInfoIconClicked);

    //ThumbnailAddToCollectionViewEvents
    oEventHandler[ThumbnailAddToCollectionViewEvents.THUMBNAIL_ADD_TO_COLLECTION_BUTTON_CLICKED] = ActionInterceptor('Add to Collection clicked', handleCreateStaticCollectionButtonClicked);
    oEventHandler[ThumbnailAddToCollectionViewEvents.THUMBNAIL_MODIFY_STATIC_COLLECTION_CLICKED] = ActionInterceptor('Single Content add to collection clicked', handleSingleContentAddToStaticCollectionClicked);

    oEventHandler[StartAndEndDatePicketViewEvents.CONTENT_EVENTS_FIELD_CHANGED] = ActionInterceptor('Event Field Changed', handleContentEventFieldChanged);
    // oEventHandler[ContentEventDetailViewEvents.CONTENT_EVENTS_IMAGE_CHANGED] = ActionInterceptor('Event Image Changed', handleContentEventImageChanged);
    // oEventHandler[ContentEventDetailViewEvents.CONTENT_EVENTS_ON_AFTER_RADIO_BUTTON_CLICKED] = ActionInterceptor('Event Field Changed', handleContentEventOnAfterRadioButtonClicked);
    oEventHandler[SelectionToggleViewEvents.SELECTION_TOGGLE_BUTTON_CLICKED] = ActionInterceptor('Selection Toggle Button Clicked', handleSelectionToggleButtonClicked);

    //TaskEvents
    oEventHandler[TaskEvents.TASK_DATA_CHANGED] = ActionInterceptor('task_data_changed', handleTaskDataChanged);
    oEventHandler[TaskEvents.TASK_FILE_ATTACHMENT_VIEW_FILE_UPLOAD_CLICKED] = ActionInterceptor('task_file_attachment_view_file_upload_clicked', handleFileAttachmentUploadClicked);
    oEventHandler[TaskEvents.RESET_TASK_DATA_FROM_PARENT] = ActionInterceptor('reset_task_data_from_parent', resetTaskData);
    oEventHandler[TaskEvents.OPEN_PRODUCT_FROM_DASHBOARD] = ActionInterceptor('open_product_from_dashboard', handleOpenProductFromDashboard);
    oEventHandler[TaskEvents.TASK_FILE_ATTACHMENT_VIEW_GET_ALL_ASSET_EXTENSIONS] = ActionInterceptor('task_file_attachment_view_get_all_asset_extensions', handleGetAllAssetExtensions);

    oEventHandler[XRayButtonViewEvents.HANDLE_X_RAY_BUTTON_CLICKED] = ActionInterceptor('Handle X-Ray Button Clicked', handleXRayButtonClicked);
    oEventHandler[XRayButtonViewEvents.HANDLE_X_RAY_SETTINGS_BUTTON_CLICKED] = ActionInterceptor('Handle X-Ray Property Searched', handleShowXRayPropertyGroupsClicked);

    oEventHandler[XRaySettingsViewEvents.HANDLE_X_RAY_PROPERTY_CLICKED] = ActionInterceptor('Handle X-Ray Property Clicked', handleXRayPropertyClicked);
    oEventHandler[XRaySettingsViewEvents.HANDLE_SHOW_X_RAY_PROPERTY_GROUPS_CLICKED] = ActionInterceptor('Handle Show X-Ray Property Groups Clicked', handleShowXRayPropertyGroupsClicked);
    oEventHandler[XRaySettingsViewEvents.HANDLE_X_RAY_PROPERTY_GROUP_CLICKED] = ActionInterceptor('Handle X-Ray Property Group Clicked', handleXRayPropertyGroupClicked);
    oEventHandler[XRaySettingsViewEvents.HANDLE_CLOSE_ACTIVE_X_RAY_PROPERTY_GROUP_CLICKED] = ActionInterceptor('Handle Close Active X-Ray Property Group Clicked', handleCloseActiveXRayPropertyGroupClicked);

    oEventHandler[ContentTileListViewEvents.HANDLE_COLLECTION_EDIT_DIALOG_BUTTON_CLICKED] = ActionInterceptor('Edit Collection icon clicked', handleCollectionEditDialogButtonClicked);
    oEventHandler[ContentTileListViewEvents.CONTENT_TILE_LIST_VIEW_CREATE_VARIANT_BUTTON_CLICKED] = ActionInterceptor('Create Linked Variant clicked', handleCreateLinkedVariantClicked);

    oEventHandler[ContentEditViewEvents.CONTENT_EDIT_VIEW_ACROLINX_CHECK_RESULT] = ActionInterceptor('Filter Option Changed (Content Edit View)', handleAcrolinxResultChecked);
    oEventHandler[ContentEditViewEvents.HANDLE_CONTENT_TAB_CLICKED] = ActionInterceptor('Handle entity tab clicked', handleEntityTabClicked);

    oEventHandler[ContentEditToolbarViewEvents.CONTENT_EDIT_TOOLBAR_HANDLE_BUTTON_CLICKED] = ActionInterceptor('handle content edit toolbar button clicked', handleContentEditToolbarButtonClicked);
    oEventHandler[ContentEditToolbarViewEvents.HANDLE_COMMENT_CHANGE] = ActionInterceptor('Entity detail view events', handleCommentChanged);

    oEventHandler[ContentInformationSidebarViewEvents.CONTENT_HEADER_CONTENT_NAME_BLURRED] = ActionInterceptor('Handle events button clicked', handleEntityHeaderEntityNameBlur);
    oEventHandler[ContentInformationSidebarViewEvents.CONTENT_HEADER_CONTENT_NAME_EDIT_CLICKED] = ActionInterceptor('Handle events button clicked', handleEntityHeaderEntityNameEditClicked);
    oEventHandler[ContentInformationSidebarViewEvents.IMAGE_GALLERY_DIALOG_VIEW_VISIBILITY_STATUS_CHANGED] = ActionInterceptor('Open Variant Images', handleImageGalleryDialogViewVisibilityStatusChanged);
    oEventHandler[ContentImageWrapperViewEvents.IMAGE_GALLERY_DIALOG_VIEW_VISIBILITY_STATUS_CHANGED] = ActionInterceptor('Open Variant Images', handleImageGalleryDialogViewVisibilityStatusChanged);
    oEventHandler[ContentImageWrapperViewEvents.ASSET_IMAGE_UPLOAD_EVENT] = ActionInterceptor('Open Variant Images', handleAssetUploadClick);
    oEventHandler[ContentImageWrapperViewEvents.CONTENT_IMAGE_WRAPPER_VIEW_CLOSE_PREVIEW_CLICKED] = ActionInterceptor('image coverflow detail view close', handleCoverflowDetailViewClosedButtonClicked);
    oEventHandler[ContentImageWrapperViewEvents.CONTENT_IMAGE_WRAPPER_IMAGE_COVERFLOW_OPEN_VIEWER_CLICKED] = ActionInterceptor('Coverflow open viewver clicked', handleOpenCoverflowViewerButtonClicked);
    oEventHandler[ContentImageWrapperViewEvents.CONTENT_IMAGE_WRAPPER_ASSET_UPLOAD_REPLACE_CLICKED] = ActionInterceptor('Coverflow asset upload or replace clicked', handleAssetUploadReplaceClicked);
    oEventHandler[ContentImageWrapperViewEvents.GET_ASSET_DOWNLOAD_OR_SHARE_DIALOG] = ActionInterceptor('get_asset_download_or_share_dialog', handleContentLeftSideShareOrDownloadButtonClicked);

    oEventHandler[ImageGalleryDialogViewEvents.CLOSE_VARIANT_IMAGES] = ActionInterceptor('Close Variant Images', handleVariantCloseImageButtonClicked);

    //VariantDetailDialogView events
    oEventHandler[VariantDialogViewEvents.VARIANT_DIALOG_SAVE_BUTTON_CLICKED] = ActionInterceptor('Variant dialog close Clicked', handleVariantDialogSaveClicked);
    oEventHandler[VariantDialogViewEvents.VARIANT_DIALOG_DISCARD_BUTTON_CLICKED] = ActionInterceptor('Variant dialog close Clicked', handleVariantDialogDiscardClicked);
    oEventHandler[VariantDialogViewEvents.VARIANT_DIALOG_ON_CHANGE_ATTRIBUTE_VALUE] = ActionInterceptor('Variant dialog change attribute value', handleVariantDialogChangeAttributeValue);

    //VariantLinkedEntitiesView events
    oEventHandler[VariantLinkedEntitiesView.VARIANT_LINKED_ENTITIES_VIEW_ADD_ENTITY] = ActionInterceptor('Variant linked entities view add entity', handleVariantLinkedEntitiesAddEntityClicked);
    oEventHandler[VariantLinkedEntitiesView.VARIANT_LINKED_ENTITIES_VIEW_REMOVE_ENTITY] = ActionInterceptor('Variant linked entities view remove entity', handleVariantLinkedEntitiesRemoveEntityClicked);

    //TableBodyView events
    oEventHandler[TableBodyViewEvents.TABLE_EDIT_BUTTON_CLICKED] = ActionInterceptor('Table edit button Clicked', handleTableEditButtonClicked);
    oEventHandler[TableBodyViewEvents.TABLE_DELETE_BUTTON_CLICKED] = ActionInterceptor('Table edit button Clicked', handleTableDeleteButtonClicked);
    oEventHandler[TableBodyViewEvents.TABLE_OPEN_BUTTON_CLICKED] = ActionInterceptor('Table open button Clicked', handleTableOpenButtonClicked);
    oEventHandler[TableBodyViewEvents.TABLE_ROW_SELECTION_CHANGED] = ActionInterceptor('Table Row Selected', handleTableRowSelectionChanged);

    //TableCellView events
    oEventHandler[TableCellViewEvents.TABLE_CELL_VIEW_CELL_VALUE_CHANGED] = ActionInterceptor('Table Cell View cell value changed', handleTableCellValueChanged);
    oEventHandler[TableCellViewEvents.TABLE_CELL_ADD_LINKED_INSTANCE] = ActionInterceptor('Table Cell View cell add linked instance', handleTableAddLinkedInstance);
    oEventHandler[TableCellViewEvents.TABLE_CELL_SET_ACTIVE_POP_OVER_VARIANT] = ActionInterceptor('Table Cell View cell pop over open', handleTableSetActivePopOverVariant);
    oEventHandler[TableCellViewEvents.TABLE_CELL_CLEAR_ACTIVE_POP_OVER_VARIANT] = ActionInterceptor('Table Cell View cell clear over open data', handleTableClearActivePopOverVariant);

    oEventHandler[TableCellViewEvents.TABLE_CELL_REMOVE_LINKED_INSTANCE] = ActionInterceptor('Table Cell View cell remove linked instance', handleTableRemoveLinkedIntance);

    //CircledTagGroupEvents
    oEventHandler[CircledTagGroupViewEvents.CIRCLED_TAG_NODE_CLICKED] = ActionInterceptor('Circled Tag Node Clicked', handleCircledTagNodeClicked);

    //task dialog button events
    oEventHandler[PropertyTasksEditButtonViewEvents.HANDLE_PROPERTY_TASK_OPEN_CLICKED] = ActionInterceptor('Task Dialog Open Clicked', handleTaskDialogOpenClicked);

    //NatureThumbnailViewEvents
    oEventHandler[NatureThumbnailViewEvents.NATURE_THUMBNAIL_DELETE_BUTTON_CLICKED] = ActionInterceptor('Delete Nature Thumb clicked', handleNatureThumbDeleteClicked);
    oEventHandler[NatureThumbnailViewEvents.NATURE_THUMBNAIL_VIEW_BUTTON_CLICKED] = ActionInterceptor('View Nature Thumb clicked', handleNatureThumbViewClicked);
    oEventHandler[NatureThumbnailViewEvents.NATURE_THUMBNAIL_SELECT_BUTTON_CLICKED] = ActionInterceptor('Select Nature Thumb clicked', handleNatureThumbSelectClicked);

    //NatureCommonSectionViewEvents
    oEventHandler[NatureCommonSectionViewEvents.HANDLE_BUNDLE_TOOLBAR_ITEM_CLICKED] = ActionInterceptor('Select Nature Thumb clicked', handleBundleToolbarItemClicked);
    oEventHandler[NatureCommonSectionViewEvents.NATURE_COMMON_SECTION_NAVIGATION_ITEM_CLICKED] = ActionInterceptor('Nature Common Section View Navigation Item clicked', handleBundleSectionPaginationChanged);

    //NatureRelationshipViewEvents
    oEventHandler[NatureRelationshipViewEvents.NATURE_ADD_ENTITY_BUTTON_CLICKED] = ActionInterceptor('Nature Add Entity Button Clicked', handleNatureAddEntityButtonClicked);

    oEventHandler[VariantImageSelectionViewEvents.ASSIGN_ASSET_BUTTON_CLICKED] = ActionInterceptor('Assign image to variant', handleAssignImageToVariantButtonClicked);

    //CustomDateRangePickerViewEvents
    oEventHandler[CustomDateRangePickerViewEvents.CUSTOM_DATE_RANGE_PICKER_HANDLE_APPLY_BUTTON_CLICKED] = ActionInterceptor('Calendar Range Picker Apply Button Clicked', handleDateRangePickerDateChange);
    oEventHandler[CustomDateRangePickerViewEvents.CUSTOM_DATE_RANGE_PICKER_HANDLE_CANCEL_BUTTON_CLICKED] = ActionInterceptor('Calendar Range Picker Cancel Button Clicked', handleDateRangePickerCancelClicked);
    oEventHandler[VariantImageSelectionViewEvents.ASSIGN_ASSET_BUTTON_CLICKED] = ActionInterceptor('Assign image to variant', handleAssignImageToVariantButtonClicked);

    //Job List View Events
    oEventHandler[JobItemViewEvents.JOB_ITEM_CLICKED] = ActionInterceptor('Job Item Clicked', handleJobItemClicked);
    oEventHandler[JobItemViewEvents.PROCESS_DATA_DOWNLOAD_BUTTON_CLICKED] = ActionInterceptor('Process  Data Download Button Clicked', handleProcessDataDownloadButtonClicked);
    oEventHandler[ProcessRuntimeGraphView.PROCESS_GRAPH_REFRESH_BUTTON_CLICKED] = ActionInterceptor('Process Graph Status Refreshed', handleProcessGraphRefreshButtonClicked);
    oEventHandler[JobItemViewEvents.PROCESS_INSTANCE_DIALOG_BUTTON_CLICKED] = ActionInterceptor('Process Instance Dialog button clicked', handleProcessInstanceDialogButtonClicked);
    oEventHandler[JobItemViewEvents.PROCESS_INSTANCE_DOWNLOAD_BUTTON_CLICKED] = ActionInterceptor('Process Instance Download Button Clicked', handleProcessInstanceDownloadButtonClicked);

    //Timeline View Events
    oEventHandler[TimelineEvents.TIMELINE_VIEW_VERSION_SELECT_BUTTON_CLICKED] = ActionInterceptor('Timeline Version Select Button Clicked', handleTimelineSelectVersionButtonClicked);
    oEventHandler[TimelineEvents.TIMELINE_VIEW_VERSION_DELETE_BUTTON_CLICKED] = ActionInterceptor('Timeline Version Delete Clicked', handleTimelineDeleteVersionZoomButtonClicked);
    oEventHandler[TimelineEvents.TIMELINE_VIEW_COMPARE_BUTTON_CLICKED] = ActionInterceptor('Timeline Version Compare Clicked', handleTimelineCompareVersionButtonClicked);
    oEventHandler[TimelineEvents.TIMELINE_VIEW_VERSION_LOAD_MODE_BUTTON_CLICKED] = ActionInterceptor('Timeline Version Load More Button Clicked', handleTimelineVersionLoadMoreButtonClicked);
    oEventHandler[TimelineEvents.TIMELINE_VIEW_SHOW_ARCHIVE_BUTTON_CLICKED] = ActionInterceptor('Timeline Version Show Archive Button Clicked', handleTimelineVersionShowArchiveButtonClicked);
    oEventHandler[TimelineEvents.TIMELINE_VIEW_VERSION_SELECT_ALL_BUTTON_CLICKED] = ActionInterceptor('Timeline Version select All Button Clicked', handleTimelineVersionSelectAllButtonClicked);
    oEventHandler[TimelineEvents.TIMELINE_VIEW_VERSION_RESTORE_BUTTON_CLICKED] = ActionInterceptor('Timeline Version Restore Button Clicked', handleTimelineRestoreVersionButtonClicked);
    oEventHandler[TimelineEvents.TIMELINE_VIEW_VERSION_ROLLBACK_BUTTON_CLICKED] = ActionInterceptor('Timeline Version Rollback Button Clicked', handleTimelineRollbackVersionButtonClicked);


    //Timline Comparison View Events
    oEventHandler[ContentThumbnailContainerEvents.TIMELINE_COMPARISON_CLOSE_BUTTON_CLICKED] = ActionInterceptor('Timeline Version Compare close Clicked', handleTimelineCompareVersionCloseButtonClicked);
    oEventHandler[ContentThumbnailContainerEvents.CONTENT_GRID_EDIT_CLOSE_BUTTON_CLICKED] = ActionInterceptor('Content grid edit close button clicked', handleContentGridEditClose);
    oEventHandler[ContentGridEditViewEvents.CONTENT_GRID_EDIT_BUTTON_CLICKED] = ActionInterceptor('Content grid edit button Clicked', handleGridEditButtonClicked);

    //Content Comparison View Events
    oEventHandler[ContentComparisonDialogViewEvents.GOLDEN_RECORD_MATCH_MERGE_ACTION_BUTTON_CLICKED] = ActionInterceptor('Golden record comparison close clicked', handleGoldenRecordDialogActionButtonClicked);

    //Content Comparison full screen View Events
    oEventHandler[ContentComparisonFullScreenViewEvents.CONTENT_COMPARISON_FULL_SCREEN_BUTTON_CLICKED] = ActionInterceptor(
        'handle content comparison full screen button clicked', handleContentComparisonFullScreenButtonClicked);

    //ContentHorizontalTreeViewEvents
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CLICKED] = ActionInterceptor('Content Horizontal Tree Node Clicked Load Children', handleContentHorizontalTreeNodeClicked);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_ARROW_CLICKED] = ActionInterceptor('Content Horizontal Tree Node Collapse', handleContentHorizontalTreeNodeCollapseClicked);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_RIGHT_CLICKED] = ActionInterceptor('Content Horizontal Tree Node Right Clicked', handleContentHorizontalTreeNodeRightClicked);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CONTEXT_MENU_POPOVER_CLOSE] = ActionInterceptor('Content Horizontal Tree Node Popover closed', handleContentHorizontalTreeNodePopoverClosed);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CONTEXT_MENU_ITEM_CLICKED] = ActionInterceptor('Content Horizontal Tree Node Popover closed', handleContentHorizontalTreeNodeContextMenuItemClicked);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_ADD_NEW_NODE] = ActionInterceptor('Content Horizontal Tree Node Add New Node', handleContentHorizontalTreeNodeAddNewNode);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_TOGGLE_AUTOMATIC_SCROLL_PROP] = ActionInterceptor('Content Horizontal Tree AutoScroll Prop Update', handleContentHorizontalTreeNodeToggleAutomaticScrollProp);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_DELETE_ICON_CLICKED] = ActionInterceptor('Content Horizontal Tree Node Add New Node', handleContentHorizontalTreeNodeDeleteIconClicked);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_VISIBILITY_CHANGED] = ActionInterceptor('Content Horizontal Tree Node Visibility Changed', handleContentHorizontalTreeNodeViewVisibilityModeChanged);

    oEventHandler[VariantTagGroupViewEvents.VARIANT_TAG_GROUP_VIEW_DATE_VALUE_CHANGED] = ActionInterceptor('Variant Tag Group View Date Value Changed', handleVariantTagGroupDateValueChanged);

    //Grid View Events
    oEventHandler[GridViewEvents.GRID_VIEW_SELECT_CLICKED] = ActionInterceptor('Grid View Select Clicked', handleGridViewSelectButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_ACTION_ITEM_CLICKED] = ActionInterceptor('Grid View Action Item Clicked', handleGridViewActionItemClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_DELETE_BUTTON_CLICKED] = ActionInterceptor('Grid View Delete Button Clicked', handleGridViewDeleteButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_PAGINATION_CHANGED] = ActionInterceptor('Grid View Pagination Changed', handleGridPaginationChanged);
    oEventHandler[GridViewEvents.GRID_VIEW_SEARCH_TEXT_CHANGED] = ActionInterceptor('Grid View Search Text Changed', handleGridViewSearchTextChanged);
    oEventHandler[GridViewEvents.GRID_VIEW_SAVE_BUTTON_CLICKED] = ActionInterceptor('Grid View Save Button Clicked', handleGridViewSaveButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_DISCARD_BUTTON_CLICKED] = ActionInterceptor('Grid View Discard Button Clicked', handleGridViewDiscardButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_COLUMN_RESIZER_MODE] = ActionInterceptor('SettingScreen: handleGridViewColumnOrganiserButtonClicked', handleGridViewResizerButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_ORGANIZE_COLUMNS_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridOrganizeColumnButtonClicked', handleGridOrganizeColumnButtonClicked);

    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_VALUE_CHANGED] = ActionInterceptor('Grid Property Value Changed', handleGridPropertyValueChanged);
    oEventHandler[GridPropertyViewEvents.GRID_TAG_PROPERTY_VALUE_CHANGED] = ActionInterceptor('Grid Tag Property Value Changed', handleGridTagPropertyValueChanged);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_PARENT_EXPAND_TOGGLED] = ActionInterceptor('Grid Property Parent Expand Toggled', handleGridPropertyParentExpandToggled);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_FILTER_CLICKED] = ActionInterceptor('Grid Property Filter Clicked', handleGridPropertyFilterClicked);
    oEventHandler[GridImagePropertyViewEvents.GRID_IMAGE_PROPERTY_IMAGE_CHANGED] = ActionInterceptor('Grid Image Property Image Changed', handleGridImagePropertyImageChanged);
    oEventHandler[GridPropertyViewEvents.GRID_DATE_FIELD_FOCUSED] = ActionInterceptor('Grid Date Field Focused', handleContentGridEditRowAutoSave);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_VIEW_CHIPS_EDIT_BUTTON_CLICKED] =
        ActionInterceptor('grid property view chips edit button clicked', handleChipsEditButtonClicked);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_VIEW_CLASSIFICATION_DIALOG_BUTTON_CLICKED] =
        ActionInterceptor('grid property view classification dialog button clicked',
            handleGridPropertyViewClassificationDialogButtonClicked);

    oEventHandler[GridImagePropertyViewEvents.GRID_GET_ASSET_EXTENSIONS] = ActionInterceptor('Grid Get Asset Extensions', handleGetAllAssetExtensions);

    //Table View Events
    oEventHandler[TableViewEvents.TABLE_VIEW_PAGINATION_CHANGED] = ActionInterceptor('Table View Pagination Changed', handleGridPaginationChanged);

    //Attribute Context View Events
    oEventHandler[AttributeContextViewEvents.ATTRIBUTE_CONTEXT_VIEW_SHOW_VARIANTS_CLICKED] = ActionInterceptor('Attr Context View Show Variants Clicked', handleAttributeContextViewShowVariantsClicked);
    oEventHandler[AttributeContextDialogViewEvents.ATTRIBUTE_CONTEXT_VIEW_BUTTON_CLICKED] = ActionInterceptor('Attr' +
        ' Context View Button Clicked', handleAttributeContextViewDialogButtonClick);

    //Content Linked Sections View Events :
    oEventHandler[ContentLinkedSectionsViewEvents.CONTENT_LINKED_SECTION_VIEW_LINK_ITEM_CLICKED] = ActionInterceptor('Content Linked Section View Link Item Clicked', handleContentLinkedSectionsLinkItemClicked);

    //Content Wrapper View Events :
    oEventHandler[ContentScreenWrapperViewEvents.CONTENT_WRAPPER_VIEW_MODULE_CLICKED] = ActionInterceptor('content wrapper view module clicked', handleModuleItemClicked);
    oEventHandler[ContentScreenWrapperViewEvents.CONTENT_WRAPPER_VIEW_HEADER_BACK_BUTTON_CLICKED] = ActionInterceptor('content wrapper view header back button clicked', handleContentWrapperViewBackButtonClicked);
    oEventHandler[ContentScreenWrapperViewEvents.CONTENT_WRAPPER_VIEW_DASHBOARD_RESET_PREVENTED] = ActionInterceptor('content wrapper view dashboard reset prevented', handleDashboardDataResetPrevented);
    oEventHandler[ContentScreenWrapperViewEvents.EDIT_VIEW_FILTER_OPTION_CHANGED] = ActionInterceptor('Filter Option Changed (Content Edit View)', handleEditViewFilterOptionChanged);
    oEventHandler[ContentScreenWrapperViewEvents.CONTENT_EDIT_COLLECTION_ICON_CLICKED] = ActionInterceptor('Edit Collection icon clicked', handleEditCollectionButtonClicked);
    oEventHandler[ContentScreenWrapperViewEvents.EDIT_VIEW_TOGGLE_ACROLINX_SIDEBAR] = ActionInterceptor('Toogle acrolinx sidebar', handleAcrolinxSidebarToggled);
    oEventHandler[ContentScreenWrapperViewEvents.CONTENT_SCREEN_WRAPPER_VIEW_CONTENT_DATA_LANGUAGE_CHANGED] = ActionInterceptor('content data language changed', handleContentDataLanguageChanged);
    oEventHandler[ContentScreenWrapperViewEvents.CONTENT_SCREEN_WRAPPER_VIEW_TRANSLATION_DELETE_CLICKED] = ActionInterceptor('content screen wrapper view translation delete clicked', handleDeleteTranslationClicked);
    oEventHandler[ContentScreenWrapperViewEvents.CONTENT_SCREEN_WRAPPER_VIEW_DATA_LANGUAGE_POPOVER_VISIBILITY_TOGGLED] = ActionInterceptor('content screen wrapper view data language popover visibility toggled', dataLanguagePopoverVisibilityToggled);

    oEventHandler[KeydownEvents.KEY_DOWN] = ActionInterceptor('Key Down', handleKeyDown);
    oEventHandler[KeydownEvents.CTRL_S] = ActionInterceptor('Control + S', handleControlS);
    oEventHandler[KeydownEvents.CTRL_C] = ActionInterceptor('Control + C', handleControlC);
    oEventHandler[KeydownEvents.CTRL_V] = ActionInterceptor('Control + V', handleControlV);
    oEventHandler[KeydownEvents.TAB] = ActionInterceptor('Tab', handleTab);
    oEventHandler[KeydownEvents.SHIFT_TAB] = ActionInterceptor('Tab', handleShiftTab);
    oEventHandler[KeydownEvents.CTRL_D] = ActionInterceptor('Control + D', handleControlD);
    oEventHandler[KeydownEvents.ESC] = ActionInterceptor('Esc', handleEsc);
    oEventHandler[KeydownEvents.ENTER] = ActionInterceptor('Enter', handleEnter);

    oEventHandler[EntitySnackBarViewEvents.ENTITY_SNACK_BAR_STOP_SHAKING] = ActionInterceptor('Entity Snack Bar Stop Shaking', handleEntitySnackBarStopShaking);

    /****** End Point Mapping View Events *****/
    oEventHandler[EndpointMappingViewEvents.PROFILE_CONFIG_MAPPED_ELEMENT_CHANGED] = ActionInterceptor("Mapped" +
        " Element Changed", handleProfileMappedElementChanged);
    oEventHandler[EndpointMappingViewEvents.PROFILE_CONFIG_UNMAPPED_ELEMENT_CHANGED] = ActionInterceptor("Unmapped" +
        " Element Changed", handleProfileUnmappedElementChanged);
    oEventHandler[EndpointMappingViewEvents.PROFILE_CONFIG_ISIGNORED_TOGGLED] = ActionInterceptor("Ignored Row" +
        " Toggling", handleEndpointIsIgnoredToggled);
    oEventHandler[EndpointMappingViewEvents.HANDLE_ENDPOINT_MAPPING_UNMAPPED_ELEMENT_IS_IGNORED_TOGGLED] = ActionInterceptor("Ignored Row" +
        " Toggling", handleEndpointUnmappedElementIsIgnoredToggled);
    oEventHandler[EndpointMappingViewEvents.PROFILE_CONFIG_MAPPED_TAG_VALUE_CHANGED] = ActionInterceptor("Mapped Tag Value Changed", handleProfileConfigMappedTagValueChanged);
    oEventHandler[EndpointMappingViewEvents.ENDPOINT_MAPPING_VIEW_TAG_VALUE_IGNORE_CASE_TOGGLED] = ActionInterceptor(
        "Tag Value Ignore Case Toggled", handleEndpointTagValueIgnoreCaseToggled);

    oEventHandler[EndpointMappingViewEvents.HANDLE_ENDPOINT_MAPPING_VIEW_BACK_BUTTON_CLICKED] = ActionInterceptor("EndPoint Mapping View" +
        " Back Button clicked", handleEndPointMappingViewBackButtonClicked);
    oEventHandler[EndpointMappingViewEvents.HANDLE_ENDPOINT_MAPPING_VIEW_IMPORT_BUTTON_CLICKED] = ActionInterceptor("EndPoint Mapping View" +
        " Import Button clicked", handleEndPointMappingViewImportButtonClicked);
    oEventHandler[EndpointMappingViewEvents.HANDLE_ENDPOINT_MAPPING_TAB_CLICKED] = ActionInterceptor("EndPoint Mapping View" +
        " Tab clicked", handleEndPointMappingTabClicked);
    oEventHandler[EndpointMappingViewEvents.HANDLE_ENDPOINT_MAPPING_FILTER_OPTION_CHANGED] = ActionInterceptor("Endpoint Mapping Filter Option Changed", handleEndPointMappingFilterOptionChanged);

    //New Match Merge Cell View Events
    oEventHandler[MatchMergeAttributeCellViewEvents.MATCH_MERGE_CELL_VIEW_CELL_CLICKED] = ActionInterceptor('', handleMatchMergeCellClicked);
    oEventHandler[MatchMergeCommonCellViewEvents.MATCH_MERGE_CELL_VIEW_CELL_CLICKED] = ActionInterceptor('', handleMatchMergeCellClicked);

    //Match Merge Tag View Events
    oEventHandler[MatchMergeTagViewEvents.MATCH_MERGE_TAG_VIEW_CELL_CLICKED] = ActionInterceptor('', handleMatchMergeTagCellClicked);
    oEventHandler[MatchMergeTagViewEvents.MATCH_AND_MERGE_TAG_VERSION_ROLLBACK_BUTTON_CLICKED] = ActionInterceptor('', handleMatchMergeVersionRollbackButtonClicked);

    //Match Merge Tag View Events
    oEventHandler[MatchMergeViewEvents.MATCH_AND_MERGE_COLUMN_HEADER_CLICKED] = ActionInterceptor('', handleMatchMergeColumnHeaderClicked);
    oEventHandler[MatchMergeViewEvents.MATCH_AND_MERGE_VERSION_ROLLBACK_BUTTON_CLICKED] = ActionInterceptor('', handleMatchMergeVersionRollbackButtonClicked);

    //Match Merge Header View Events
    oEventHandler[MatchMergeHeaderViewEvents.MATCH_AND_MERGE_HEADER_CELL_CLICKED] = ActionInterceptor('', handleMatchMergeColumnHeaderClicked);
    oEventHandler[MatchMergeHeaderViewEvents.MATCH_AND_MERGE_VERSION_ROLLBACK_BUTTON_CLICKED] = ActionInterceptor('', handleMatchMergeVersionRollbackButtonClicked);

    //Match Merge Relationship View Events
    oEventHandler[MatchMergeRelationshipViewEvents.MATCH_MERGE_RELATIONSHIP_VIEW_CELL_CLICKED] = ActionInterceptor('', handleMatchMergeRelationshipCellClicked);
    oEventHandler[MatchMergeRelationshipViewEvents.MATCH_MERGE_RELATIONSHIP_VIEW_CELL_REMOVE_CLICKED] = ActionInterceptor('', handleMatchMergeRelationshipCellRemoveClicked);
    oEventHandler[MatchMergeRelationshipViewEvents.MATCH_AND_MERGE_RELATIONSHIP_VERSION_ROLLBACK_BUTTON_CLICKED] = ActionInterceptor('', handleMatchMergeVersionRollbackButtonClicked);

    //clone wizard view new events
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_CANCEL_CLONING_BUTTON_CLICKED] = ActionInterceptor('', handleCancelCloningButtonClicked);
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_EXPAND_SECTION_TOGGLED] = ActionInterceptor('', handleCloneExpandSectionToggled);
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_CREATE_CLONE_BUTTON_CLICKED] = ActionInterceptor('', handleCreateCloneButtonClicked);
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_ENTITY_CHECKBOX_CLICKED] = ActionInterceptor('', handleCheckboxButtonClicked);
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_ENTITY_GROUP_CHECKBOX_CLICKED] = ActionInterceptor('', handleCheckboxHeaderButtonClicked);
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_EXACT_CLONE_CHECKBOX_CLICKED] = ActionInterceptor('', handleExactCloneCheckboxClicked);
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_GET_ALLOWED_TYPES_TO_CREATE_LINKED_VARIANT] = ActionInterceptor('', handleGetAllowedTypesToCreateLinkedVariant);
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_SELECT_TYPE_TO_CREATE_LINKED_VARIANT] = ActionInterceptor('', handleSelectTypeToCreateLinkedVariant);
    oEventHandler[CloneWizardViewNewEvents.CLONE_WIZARD_VIEW_CLONE_COUNT_CHANGED] = ActionInterceptor('', handleCloningWizardCloneCountChanged);

    oEventHandler[UOMViewEvents.UOM_VIEW_TABLE_CREATE_ROW_BUTTON_CLICKED] = ActionInterceptor('', handleUOMTableCreateRowButtonClicked);
    oEventHandler[UOMViewEvents.UOM_VIEW_DATE_RANGE_APPLIED] = ActionInterceptor('', handleUOMViewDateRangeApplied);
    oEventHandler[UOMViewEvents.UOM_VIEW_TABLE_SORT_DATA_CHANGED] = ActionInterceptor('', handleUOMViewSortDataChanged);
    oEventHandler[UOMViewEvents.UOM_VIEW_TABLE_PAGINATION_CHANGED] = ActionInterceptor('', handleUOMViewPaginationDataChanged);
    oEventHandler[UOMViewEvents.UOM_VIEW_FULL_SCREEN_ICON_CLICKED] = ActionInterceptor('', handleUOMViewFullScreenButtonClicked);
    oEventHandler[UOMViewEvents.UOM_GRID_VIEW_FULL_SCREEN_BUTTON_HANDLER] = ActionInterceptor('handle uom grid view full screen button handler', handleUOMViewFullScreenCancelButtonClicked);
    oEventHandler[UOMViewEvents.UOM_VIEW_GRID_ADD_LINKED_INSTANCES_CLICKED] = ActionInterceptor('handle uom grid view add linked instance clicked', handleUOMViewAddLinkedInstanceClicked);
    oEventHandler[UOMViewEvents.UOM_VIEW_GRID_REMOVE_LINKED_INSTANCES_CLICKED] = ActionInterceptor('handle uom grid view add linked instance clicked', handleUOMViewRemoveLinkedInstanceClicked);
    oEventHandler[UOMViewEvents.UOM_VIEW_GRID_VALUE_CHANGED] = ActionInterceptor('', handleUOMViewGridValueChanged);
    oEventHandler[UOMViewEvents.UOM_VIEW_GRID_TAG_VALUE_CHANGED] = ActionInterceptor('', handleUOMViewGridTagValueChanged);
    oEventHandler[UOMViewEvents.UOM_VIEW_GRID_ACTION_ITEM_CLICKED] = ActionInterceptor('', handleUOMViewGridActionItemClicked);

    oEventHandler[PhysicalCatalogSelectorEvents.HANDLE_PHYSICAL_CATALOG_OR_PORTAL_SELECTION_CHANGED] = ActionInterceptor('Physical catalog changed', handlePhysicalCatalogChanged);
    oEventHandler[ButtonWithContextMenuViewEvents.HANDLE_BUTTON_CLICKED] = ActionInterceptor('Handle Button Clicked', handleContextMenuButtonClicked);
    oEventHandler[ButtonWithContextMenuViewEvents.LIST_ITEM_SELECTED] = ActionInterceptor('Handle Context Menu List Item Selected', handleContextMenuListItemClicked);
    oEventHandler[ButtonWithContextMenuViewEvents.POPOVER_VISIBILITY_TOGGLED] = ActionInterceptor('Popover visibility toggled', handleContextMenuListVisibilityToggled);
    oEventHandler[ButtonWithContextMenuViewEvents.HANDLE_DIRECT_CLICK_CLICKED] = ActionInterceptor('Handle direct click clicked', handleHandleDirectClickClicked);
    oEventHandler[ButtonWithContextMenuViewEvents.TRANSFER_DIALOG_BUTTON_CLICKED] = ActionInterceptor('transfer dialog button clicked', handleHandleTransferDialogClicked);

    oEventHandler[TransferDialogViewEvents.HANDLE_ITEM_CLICKED] = ActionInterceptor('handle item clicked for transfer dialog', handleHandleTransferDialogItemClicked);
    oEventHandler[TransferDialogViewEvents.HANDLE_TRANSFER_CHECKBOX_TOGGLED] = ActionInterceptor('handle checkbox toggled for transfer dialog', handleTransferDialogCheckBoxToggled);

    oEventHandler[KpiSummaryTileViewEvents.KPI_SUMMARY_VIEW_KPI_SELECTED] = ActionInterceptor('Kpi Summary View Kpi Selected', handleKpiSummaryViewKpiSelected);

    oEventHandler[DashboardEndpointTileViewEvents.DASHBOARD_ENDPOINT_TILE_CLICKED] = ActionInterceptor('Dashboard Endpoint Tile Clicked', handleDashboardEndpointTileClicked);
    oEventHandler[DashboardEndpointTileViewEvents.DASHBOARD_ENDPOINT_REFRESH_CLICKED] = ActionInterceptor('Dashboard Endpoint Tile Refresh Clicked', handleDashboardEndpointTileRefreshClicked);
    oEventHandler[DashboardTileViewEvents.HANDLE_DASHBOARD_TILE_SHOW_CONTENTS_BUTTON_CLICKED] = ActionInterceptor('Dashboard Tile Show Contents Clicked', handleShowKpiContentExplorerClicked);
    oEventHandler[DashboardTileViewEvents.HANDLE_DASHBOARD_TILE_REFRESH_BUTTON_CLICKED] = ActionInterceptor('Dashboard Tile Refresh Clicked', handleRefreshKPIButtonClicked);

    oEventHandler[DashboardViewEvents.HANDLE_DASHBOARD_VIEW_CLOSE_DIALOG_CLICKED] = ActionInterceptor('Dashboard view Close dialog button clicked', handleContentKPITileCloseDialogButtonClicked);

    oEventHandler[FileDragAndDropViewEvents.FILE_DRAG_DROP_HANDLE_FILE_DROP] = ActionInterceptor('File drag n drop: Handle file drop', handleFileDrop);
    oEventHandler[FileDragAndDropViewEvents.FILE_DRAG_DROP_DRAGGED_HANDLER] = ActionInterceptor('File drag n drop: dragged handler', handleFileDragDropViewDraggingState);

    oEventHandler[TabLayoutViewEvents.HANDLE_TAB_LAYOUT_VIEW_TAB_CLICKED] = ActionInterceptor('Tab Layout View' +
        ' Tab Changed: handleTabLayoutViewTabChanged', handleTabLayoutViewTabChanged);


    oEventHandler[DashboardTileViewEvents.HANDLE_DASHBOARD_TILE_OPEN_DIALOG_BUTTON_CLICKED] = ActionInterceptor('Dashboard tile view open dialog button clicked', handleContentKPITileOpenDialogButtonClicked);
    oEventHandler[ContentInformationSidebarViewEvents.CONTENT_INFORMATION_SIDEBAR_VIEW_HANDLE_SLIDER_IMAGE_CLICKED] = ActionInterceptor('content selected image Clicked', handleContentSliderImageClicked);
    oEventHandler[ContentInformationSidebarViewEvents.CONTENT_INFORMATION_SIDEBAR_VIEW_SET_DEFAULT_ASSET_BUTTON_CLICKED] = ActionInterceptor('set default asset button clicked', handleAssignImageToVariantButtonClicked);
    oEventHandler[ContentInformationSidebarViewEvents.CONTENT_INFORMATION_SIDEBAR_VIEW_DELETE_GENERATED_ASSET_LINK] = ActionInterceptor('delete generated link of asset', handleDeleteGeneratedAssetLinkClicked);

    oEventHandler[ThumbnailGoldenRecordBucketViewEvents.THUMBNAIL_GOLDEN_RECORD_BUCKET_VIEW_TAB_CHANGED] = ActionInterceptor('Thumbnail golden record bucket view tab clicked', handleGoldenRecordBucketTabChanged);
    oEventHandler[ThumbnailGoldenRecordBucketViewEvents.THUMBNAIL_GOLDEN_RECORD_BUCKET_VIEW_MERGE_BUTTON_CLICKED] = ActionInterceptor('Thumbnail golden record bucket view tab clicked', handleGoldenRecordBucketMergeButtonClicked);

    oEventHandler[InformationTabViewEvents.HANDLE_INFORMATION_TAB_RULE_VIOLATION_ITEM_CLICKED] = ActionInterceptor('Information tab rule violation item licked', handleInformationTabRuleViolationItemClicked);

    oEventHandler[UploadAssetDialogViewEvents.UPLOAD_ASSET_DIALOG_BUTTON_CLICKED] = ActionInterceptor("upload asset dialog button clicked", handleUploadAssetDialogButtonClicked);
    oEventHandler[UploadAssetDialogViewEvents.UPLOAD_ASSET_BUTTON_CLICKED] = ActionInterceptor("upload asset button clicked", handleBulkAssetUploadFromRelationshipClicked);

    oEventHandler[ButtonViewEvent.BUTTON_VIEW_BUTTON_CLICKED] = ActionInterceptor("Button View button clicked", handleToolbarButtonClicked);

    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_SINGLE_TEXT_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Single Text Changed', handleCommonConfigSectionSingleTextChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_YES_NO_BUTTON_CHANGED] = ActionInterceptor('SettingScreen: Handle Common Config Section View Yes No Button Clicked', handleCommonConfigSectionViewYesNoButtonClicked);

    oEventHandler[HomeScreenCommunicator.HANDLE_BROWSER_BUTTON_CLICKED] = ActionInterceptor(
        'handle browser navigation', handleBrowserButtonClicked);
    oEventHandler[HomeScreenCommunicator.HANDLE_RESET_BREADCRUMB_NODE_PROPS_ACCORDING_TO_CONTEXT] = ActionInterceptor(
        'handle reset breadcrumb node props', handleResetBreadcrumbNodePropsAccordingToContext);

    oEventHandler[HomeScreenCommunicator.HANDLE_NOTIFICATION_BUTTON_CLICKED] = ActionInterceptor('Handle Notification button clicked', handleHeaderMenuNotificationClicked);

    oEventHandler[HomeScreenCommunicator.HANDLE_DATA_LANGUAGE_CHANGED] = ActionInterceptor(
        'handle language changed', handleDataLanguageChanged);
    oEventHandler[HomeScreenCommunicator.HANDLE_DASHBOARD_TAB_SELECTED] = ActionInterceptor(
        'set selected dashboard tab', handleDashboardTabSelected);

    oEventHandler[ContentComparisonViewEvents.CONTENT_COMPARISON_MNM_LANGUAGE_CHANGED] = ActionInterceptor(
        'handle add marketing article nature relationship with MDM', handleContentComparisonMNMLanguageChanged);

    oEventHandler[StepperViewEvents.STEPPER_VIEW_BUTTON_CLICKED] = ActionInterceptor('handle stepper view active step changed', handleStepperViewActiveStepChanged);

    oEventHandler[ContentComparisonMatchAndMergeRelationshipEvents.CONTENT_COMPARISON_MNM_RELATIONSHIP_PAGINATION_CHANGED] = ActionInterceptor(
        'handle content comparision match and merge relationship pagination changed', handleContentComparisonMnmRelationshipPaginationChanged);
    oEventHandler[ContentComparisonMatchAndMergeRelationshipEvents.CONTENT_COMPARISON_MNM_REMOVE_RELATIONSHIP_BUTTON_CLICKED] = ActionInterceptor(
        'handle content comparison mnm remove relationship button clicked', handleContentComparisonMnmRemoveRelationshipButtonClicked);
    oEventHandler[ContentComparisonMatchAndMergeViewEvents.CONTENT_COMPARISON_MATCH_AND_MERGE_VIEW_TABLE_ROW_CLICKED] = ActionInterceptor(
        'handle content comparision match and merge view table row clicked', handleContentComparisonMatchAndMergeViewTableRowClicked);
    oEventHandler[ContentComparisonMatchAndMergeViewEvents.CONTENT_COMPARISON_MATCH_AND_MERGE_PROPERTY_VALUE_CHANGED] = ActionInterceptor(
        'handle content comparision match and merge property value changed', handleContentComparisonMatchAndMergePropertyValueChanged);
    oEventHandler[HomeScreenCommunicator.ARCHIVE_BUTTON_CLICKED] = ActionInterceptor(
        'handle archive button clicked', handleArchiveButtonClicked);
    oEventHandler[RelationshipCouplingConflictEvent.RELATIONSHIP_COUPLING_ICON_CLICKED] = ActionInterceptor(
        'Entity Detail View Relationship Coupling Icon Clicked', handleSectionElementConflictIconClicked);
    oEventHandler[RelationshipCouplingConflictEvent.RELATIONSHIP_CONFLICT_VALUE_OPTION_CLICKED] = ActionInterceptor(
        'Relationship Conflict Footer View Conflict Property Clicked', handleRelationshipConflictPropertyClicked);
    oEventHandler[RelationshipCouplingConflictEvent.RELATIONSHIP_CONFLICT_VALUE_RESOLVED] = ActionInterceptor('Relationship' +
        ' Conflict Footer View Conflict Property Resolved', handleRelationshipConflictPropertyResolved);


    oEventHandler[ContentGridEditViewEvents.GRID_EDITABLE_PROPERTIES_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
      'Grid Editable Properties Config Dialog Button Clicked', handleGridEditablePropertiesDialogButtonClicked);
    oEventHandler[ContentGridEditViewEvents.CONTENT_GRID_EDIT_HANDLE_ROW_SAVE] = ActionInterceptor(
      'Grid Editable Properties Config Dialog Button Clicked', handleContentGridEditRowAutoSave);

    oEventHandler[TaxonomyInheritanceViewEvents.TAXONOMY_INHERITANCE_DIALOG_CLOSE] = ActionInterceptor(
      'taxonomy_inheritance_dialog_close', handleCloseTaxonomyInheritanceDialog);
    oEventHandler[TaxonomyInheritanceViewEvents.TAXONOMY_INHERITANCE_ADAPT_TAXONOMY] = ActionInterceptor(
      'taxonomy_inheritance_adapt_taxonomy', handleTaxonomyInheritanceAdaptTaxonomy);
    oEventHandler[TaxonomyInheritanceViewEvents.TAXONOMY_INHERITANCE_ADAPTALL_TAXONOMY] = ActionInterceptor(
      'taxonomy_inheritance_adaptall_taxonomy', handleTaxonomyInheritanceAdaptAllTaxonomy);
    oEventHandler[TaxonomyInheritanceViewEvents.TAXONOMY_INHERITANCE_REVERT_TAXONOMY] = ActionInterceptor(
      'taxonomy_inheritance_revert_taxonomy', handleTaxonomyInheritanceRevertTaxonomy);
    oEventHandler[TaxonomyInheritanceViewEvents.TAXONOMY_INHERITANCE_REVERTALL_TAXONOMY] = ActionInterceptor(
      'taxonomy_inheritance_revertall_taxonomy', handleTaxonomyInheritanceRevertAllTaxonomy);

    oEventHandler[MultiClassificationViewEvents.MULTI_CLASSIFICATION_SECTION_EDIT_CLASSIFICATION_ICON_CLICKED] = ActionInterceptor(
        'multiClassification section edit classification icon clicked', handleMultiClassificationEditIconClicked);
    oEventHandler[MultiClassificationSectionViewEvents.MULTI_CLASSIFICATION_SECTION_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
        'multiClassification section dialog button clicked', handleMultiClassificationDialogButtonClicked);
    oEventHandler[MultiClassificationSectionViewEvents.MULTI_CLASSIFICATION_SECTION_TREE_NODE_CHECKBOX_CLICKED] = ActionInterceptor(
        'multiClassification section tree node checkbox clicked', handleMultiClassificationTreeNodeCheckboxClicked);
    oEventHandler[MultiClassificationSectionViewEvents.MULTI_CLASSIFICATION_SECTION_TREE_NODE_CLICKED] = ActionInterceptor(
        'multiClassification section tree node clicked', handleMultiClassificationTreeNodeClicked);

    oEventHandler[MultiClassificationViewEvents.MULTI_CLASSIFICATION_VIEW_REMOVE_NODE_CLICKED] = ActionInterceptor(
        'multiClassification View remove node clicked', handleMultiClassificationCrossIconClicked);

    oEventHandler[TreeViewEvents.TREE_VIEW_LOAD_MORE_CLICKED] = ActionInterceptor(
        'tree View load more clicked', handleTreeViewLoadMoreClicked);
    oEventHandler[TreeViewEvents.TREE_VIEW_DATA_RESET] = ActionInterceptor(
        'tree View reset data', resetTreeViewData);
    oEventHandler[TreeViewEvents.TREE_VIEW_TREE_SEARCHED] = ActionInterceptor(
        'tree View tree searched', handleTreeSearched);

    oEventHandler[MultiClassificationViewEvents.TAXONOMY_INHERITANCE_CONFLICT_ICON_CLICKED] = ActionInterceptor(
      'taxonomy_inheritance_conflict_icon_clicked', handleTaxonomyInheritanceConflictIconClicked);

    oEventHandler[DragDropContextView.DRAG_DROP_CONTEXT_VIEW_PROPERTY_SHUFFLED] = ActionInterceptor(
        'drag drop context view property shuffled', handleDragDropContextViewPropertyShuffled);

    oEventHandler[ColumnOrganizerDialogEvents.COLUMN_ORGANIZER_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
      'column organizer dialog button clicked', handleColumnOrganizerDialogButtonClicked);

    // Download Dialog Events
    oEventHandler[BulkDownloadDialogEvents.BULK_DOWNLOAD_DIALOG_CHECKBOX_CLICKED] = ActionInterceptor(
        'bulk_download_dialog_checkbox_clicked', handleBulkDownloadDialogCheckboxClicked);
    oEventHandler[BulkDownloadDialogEvents.BULK_DOWNLOAD_DIALOG_EXPAND_BUTTON_CLICKED] = ActionInterceptor(
        'bulk_download_dialog_expand_clicked', handleBulkDownloadDialogExpandButtonClicked);
    oEventHandler[BulkDownloadDialogEvents.BULK_DOWNLOAD_DIALOG_ACTION_BUTTON_CLICKED] = ActionInterceptor(
        'bulk_download_dialog_action_button_clicked', handleBulkDownloadDialogActionButtonClicked);
    oEventHandler[BulkDownloadDialogEvents.BULK_DOWNLOAD_DIALOG_FIXED_SECTION_VALUE_CHANGED] = ActionInterceptor(
        'bulk_download_dialog_fixed_section_value_changed', handleBulkDownloadDialogFixedSectionValueChanged);
    oEventHandler[BulkDownloadDialogEvents.BULK_DOWNLOAD_DIALOG_CHILD_ELEMENT_VALUE_CHANGED] = ActionInterceptor(
        'bulk_download_dialog_child_element_value_changed', handleBulkDownloadDialogChildElementValueChanged);

    oEventHandler[ExpandableNestedMenuListViewEvents.EXPANDABLE_NESTED_MENU_LIST_TOGGLE_BUTTON_CLICKED] = ActionInterceptor(
        'handle_expandable_menuList_toggle_button_clicked', handleMenuListToggleButtonClicked);

    // All Categories Taxonomy Selector View
    oEventHandler[AllCategoriesSelectorViewEvents.HANDLE_ALL_CATEGORIES_SELECTOR_APPLY_BUTTON_CLICKED] = ActionInterceptor('ChooseTaxonomy Apply Button Clicked', handleAllCategoriesSelectorApplyButtonClicked);
    oEventHandler[AllCategoriesSelectorViewEvents.HANDLE_ALL_CATEGORIES_SELECTOR_CANCEL_BUTTON_CLICKED] = ActionInterceptor('Handle Cancel Button Clicked', handleAllCategoriesSelectorCancelButtonClicked);
    oEventHandler[AllCategoriesSelectorViewEvents.HANDLE_ALL_CATEGORIES_SELECTOR_ORGANISE_BUTTON_CLICKED] = ActionInterceptor('Handle Organise Taxonomy Clicked', handleOrganiseTaxonomyButtonClicked);
    oEventHandler[AllCategoriesSelectorViewEvents.HANDLE_ALL_CATEGORIES_BUTTON_CLICKED] = ActionInterceptor('handle categories button clicked', handleCategoriesButtonClicked);
    oEventHandler[AllCategoriesSelectorViewEvents.HANDLE_ALL_CATEGORIES_TREE_NODE_CHECKBOX_CLICKED] = ActionInterceptor(
        'multiClassification section tree node checkbox clicked', handleAllCategoriesSelectorTreeNodeCheckboxClicked);
    oEventHandler[AllCategoriesSelectorViewEvents.HANDLE_ALL_CATEGORIES_TAXONOMY_TREE_NODE_CLICKED] = ActionInterceptor(
        'multiClassification section tree node checkbox clicked', handleAllCategoriesSelectorTreeNodeClicked);
    oEventHandler[AllCategoriesSelectorViewEvents.ALL_CATEGORIES_SELECTOR_VIEW_LOAD_MORE_CLICKED] = ActionInterceptor(
        'all_categories_selector_view_load_more_clicked', handleAllCategoriesSelectorLoadMoreClicked);
    oEventHandler[AllCategoriesSelectorViewEvents.HANDLE_ALL_CATEGORIES_SELECTOR_SEARCH_TEXT_CHANGED] = ActionInterceptor(
        'multiClassification section tree node checkbox clicked', handleAllCategoriesTreeViewSearched);

    oEventHandler[AllCategoriesSelectorSummaryViewEvents.HANDLE_SUMMARY_CLEAR_SELECTION_BUTTON_CLICKED] = ActionInterceptor('Handle Clear All Button Clicked', handleSummaryClearSelectionButtonClicked);
    oEventHandler[AllCategoriesSelectorSummaryViewEvents.HANDLE_SUMMARY_CROSS_BUTTON_CLICKED] = ActionInterceptor('Handle Clear All Button Clicked', handleSummaryCrossButtonClicked);

  })();

  return {

    registerEvent: function () {
      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        EventBus.addEventListener(sEventName, oHandler);
      });
    },

    deRegisterEvent: function () {
      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        EventBus.removeEventListener(sEventName, oHandler);
      });
    }
  }
})();

export default ContentScreenAction;
