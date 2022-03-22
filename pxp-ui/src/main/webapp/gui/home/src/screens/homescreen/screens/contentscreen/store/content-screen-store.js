import alertify from '../../../../../commonmodule/store/custom-alertify-store';
import CS from '../../../../../libraries/cs';
import MicroEvent from '../../../../../libraries/microevent/MicroEvent.js';
import MethodTracker from '../../../../../libraries/methodtracker/method-tracker';
import ThumbnailModel from '../../../../../viewlibraries/thumbnailview2/model/thumbnail-model';
import LogFactory from '../../../../../libraries/logger/log-factory';
import ContentScreenAppData from './model/content-screen-app-data';
import ContentScreenProps from './model/content-screen-props';
import ContentScreenConstants from './model/content-screen-constants';
import ThumbnailModeConstants from '../../../../../commonmodule/tack/thumbnail-mode-constants';
import RelationshipConstants from '../../../../../commonmodule/tack/relationship-constants';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import TemplateTabsDictionary from '../../../../../commonmodule/tack/template-tabs-dictionary';
import DictionaryForClassName from '../../../../../commonmodule/tack/dictionary-for-class-name';
import AssetUploadContextDictionary from '../../../../../commonmodule/tack/asset-upload-context-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
import TranslationStore from '../../../../../commonmodule/store/translation-store';
import SessionStorageManager from '../../../../../libraries/sessionstoragemanager/session-storage-manager';
import GlobalStore from '../../../store/global-store.js';
import SharableURLProps from '../../../../../commonmodule/store/model/sharable-url-props';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import ActionDialogProps from './../../../../../commonmodule/props/action-dialog-props';
import TemplateTabTypeConstants from '../../../../../commonmodule/tack/template-tabs-dictionary';
import ScreenModeUtils from '../store/helper/screen-mode-utils';
import RelationshipTypeDictionary from '../../../../../commonmodule/tack/relationship-type-dictionary';
import ModulesDictionary from '../../../../../commonmodule/tack/module-dictionary';
import FilterUtils from './helper/filter-utils';
import ContentUtils from './helper/content-utils';
import CommonUtils from '../../../../../commonmodule/util/common-utils';
import ContentLogUtils from './helper/content-log-utils';
import ImageCoverflowStore from './helper/image-coverflow-store';
import OnboardingStore from './helper/onboarding-store';
import AttributeStore from './helper/attribute-store';
import ContentRelationshipStore from './helper/content-relationship-store';
import ContentStore from './helper/content-store';
import CloneWizardStore from './helper/clone-wizard-store';
import NewMultiClassificationStore from './helper/new-multiclassification-store';
import NewMinorTaxonomyStore from './helper/new-minor-taxonomy-store';
import BulkEditStore from './helper/bulk-edit-store';
import AvailableEntityStore from './helper/available-entity-store';
import FilterStoreFactory from './helper/filter-store-factory';
import NewCollectionStore from './helper/new-collection-store';
import JobStore from './helper/job-store';
import TimelineStore from './helper/timeline-store';
import GoldenRecordStore from './helper/golden-record-store';
import ContentGridStore from './helper/content-grid-store';
import SharableURLStore from '../../../../../commonmodule/store/helper/sharable-url-store';
import DashboardScreenStore from '../screens/dashboardscreen/store/dashboard-screen-store';
import InformationTabStore from './helper/information-tab-store';
import BreadCrumbStore from '../../../../../commonmodule/store/helper/breadcrumb-store';
import CollectionAndTaxonomyHierarchyStore from './helper/collection-and-taxonomy-data-navigation-helper-store';
import SystemIdDictionary from '../../../../../commonmodule/tack/system-level-id-dictionary';
import PhysicalCatalogDictionary from '../../../../../commonmodule/tack/physical-catalog-dictionary';
import ContentScreenViewContextConstants from '../tack/content-screen-view-context-constants';
import CustomActionDialogStore from '../../../../../commonmodule/store/custom-action-dialog-store';
import BaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import DashboardTabDictionary from './../screens/dashboardscreen/tack/dashboard-tab-dictionary';
import SessionProps from '../../../../../commonmodule/props/session-props';
import DashboardScreenProps from '../screens/dashboardscreen/store/model/dashboard-screen-props';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import SessionStorageConstants from '../../../../../commonmodule/tack/session-storage-constants';
import ContentBulkDownloadStore from '../store/helper/content-bulk-download-store';
import BulkAssetLinkSharingStore from './helper/bulk-asset-link-sharing-store';
import BulkAssetDownloadDictionary from '../tack/bulk-asset-download-dictionary';
import GRConstants from '../tack/golden-record-view-constants';
import {bulkEditTabTypesConstants as BulkEditTabTypesConstants} from "../tack/bulk-edit-layout-data";
import MultiClassificationDialogToolbarLayoutData from "../tack/multiclassification-dialog-toolbar-layout-data";
import oFilterPropType from '../tack/filter-prop-type-constants';
import TreeViewStore from "../../../../../viewlibraries/treeviewnew/store/tree-view-store";
import VariantStore from "./../store/helper/variant-store";
import UOMProps from "./model/uom-props";
import GoldenRecordProps from "./model/golden-record-props";
import ColumnOrganizerProps from "../../../../../viewlibraries/columnorganizerview/column-organizer-props";
import RelationshipViewProps from "./model/relationship-view-props";
import AssetDownloadViewDictionary from "../tack/asset-download-view-dictionary";
import TasksScreenStore from "../../../../../smartviewlibraries/taskview/store/task-screen-store";
import MultiClassificationProps from "./model/multiclassification-view-props";
import {communicator as HomeScreenCommunicator} from "../../../store/home-screen-communicator";
import AllCategoriesSelectorStore from './helper/all-categories-selector-store';
import ContextualAllCategoriesProps from "./model/contextual-all-categories-selector-view-props";
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

var logger = LogFactory.getLogger('content-screen-store');
var trackMe = MethodTracker.getTracker('ContentScreenStore');
var getTranslation = ScreenModeUtils.getTranslationDictionary;
const MultiClassificationViewTypesConstants = new MultiClassificationDialogToolbarLayoutData()["multiClassificationViewTypesIds"];

var ContentScreenStore = (function () {
  var oAppData = ContentScreenAppData;
  var oComponentProps = ContentScreenProps;

  var _triggerChange = function () {
    logger.info('triggerChange: ContentScreenStore',
        {
          'callTrace': MethodTracker.getTrace().join(', ')
        }
    );

    MethodTracker.emptyCallTrace();
    ContentScreenStore.trigger('change');
  };

  var _populateTagsInEntity = function (sContext) {

    if (sContext == "content") {
      _populateTagsInContent();

    } else if (sContext == "attribute") {
      AttributeStore.populateTagInContentAttributeVariants();

    } else if (sContext == "image") {
      _populateTagsInContentImage();
    }
  };

  var _populateTagsInContent = function () {
    trackMe('populateTagsInContent');
    var aSelectedContentList = ContentUtils.getSelectedContentList();

    var aTagData = CS.cloneDeep(CS.values(GlobalStore.getTagValuesList()));

    logger.debug('populateTagsInContent: Applying tag in selected contents',
        {'tagData': aTagData});
    ContentLogUtils.debug('populateTagsInContent: Applying tag in selected contents',
        aSelectedContentList);

    CS.forEach(aSelectedContentList, function (oSelectedContent) {
      ContentUtils.makeContentDirty(oSelectedContent);
      oSelectedContent.contentClone.tags = aTagData;
    });
  };

  var _populateTagsInContentImage = function () {
    trackMe('populateTagsInContentImage');

    var oActiveContentClone = ContentUtils.makeActiveContentDirty();
    var oSelectedImages = ImageCoverflowStore.getSelectedImages();

    var aTagData = CS.cloneDeep(CS.values(GlobalStore.getTagValuesList()));

    logger.info('populateTagsInContentImage: Applying tag on selected image',
        {'selectedImages': oSelectedImages, 'tagData': aTagData});

    CS.forEach(oSelectedImages, function (oSelectedImage, oSelectedImageKey) {
      // update the reference in selectedImages
      var oImageFromContent = CS.find(oActiveContentClone.images, {"assetObjectKey": oSelectedImageKey});
      oImageFromContent.tags = aTagData;
    });
  };

  var _clearEntityDragStatus = function () {
    oComponentProps.contentDetailsView.setContentRelationshipDragDetails({});
    oComponentProps.relationshipView.setContentRelationshipContentDragDetails({});
    oComponentProps.relationshipView.setRelationshipListItemDragDetails({});
    oComponentProps.screen.setQuickListDragNDropState(true);
  };

  var _discardActiveEntity = function (oCallbackData) {
    let oActiveContent = ContentUtils.getActiveContent();
    let thumbnailPath = oActiveContent.contentClone.thumbnailPath;
    let filePath = oActiveContent.contentClone.filePath;
    ContentUtils.makeActiveEntityClean();
    ContentUtils.resetToUpdateAllSCU();
    ContentStore.cleanReferencedNatureRelationshipElements();
    ContentScreenProps.screen.setRelationshipContextData({});
    ContentScreenProps.screen.setModifiedRelationshipsContextTempData({});

    var oActiveEntity = ContentUtils.getActiveEntity();
    ContentUtils.updateBreadCrumbInfo(oActiveEntity.id, oActiveEntity);

    let sImageId = "";
    if (oActiveEntity.baseType == BaseTypeDictionary.assetBaseType) { //reset the default imageId to selected imageId
      let oImage = oActiveEntity.assetInformation;
      if (!CS.isEmpty(oImage)) {
        sImageId = oImage.thumbKey;
      }
      ContentScreenProps.screen.setSelectedImageId(sImageId);

      if (!CS.isEmpty(thumbnailPath)) {
        let sUrl = getRequestMapping().DeleteNFSFiles;
        let aFilesToDelete = [];
        aFilesToDelete.push(thumbnailPath);
        aFilesToDelete.push(filePath);
        let oData = {ids : aFilesToDelete};
        CS.deleteRequest(sUrl, {}, oData);
      }
    }

    ImageCoverflowStore.updateImageCoverActiveIndexData("discard");

    var oDummyLinkedVariant = ContentScreenProps.variantSectionViewProps.getDummyLinkedVariant();
    if(!CS.isEmpty(oDummyLinkedVariant)){
      ContentStore.handleDummyLinkedVariantDiscard();
    }

    var oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    if(oRelationshipContextData.isForSingleContent){
      oRelationshipContextData.tags = _getVariantTagsForLinkedRelationshipContent(oRelationshipContextData.relationshipId,
          oRelationshipContextData.relationshipContentInstanceId);
      oRelationshipContextData.timeRange = {
        to: null,
        from: null
      };
    }


    ContentStore.updateMandatoryElementsStatus(oActiveEntity);
    if(oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  var _updateContextData = function () {
    var oContextData = ContentScreenProps.screen.getContextData();
    var aContentList = ContentUtils.getEntityList();
    var aContentIds = [];
    var iFrom = ContentScreenProps.screen.getPaginatedIndex();
    CS.forEach(aContentList, function (oContent) {
      aContentIds.push(oContent.id);
    });
    oContextData.contentIds = aContentIds;
    oContextData.from = iFrom;
  };

  /**
   * @function _handleCyclicNavigationForWindowHistory
   * @description Handled cyclic navigation A->B->C->B(Required when contents A -> B -> C opened and if from relationship tab Content B has opened).
   * @memberOf Stores.ContentScreenStore
   * @param sSelectedItemId Selected content Id.
   */
  let _handleCyclicNavigationForWindowHistory = function (sSelectedItemId, fCallback){

    /** Handled cyclic navigation A->B->C->B **/
      /** Required when contents A -> B -> C opened and if from relationship tab Content B has opened. **/
      let aBreadCrumbData = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
      let iBreadCrumbItemIndex = CS.findIndex(aBreadCrumbData, {id: sSelectedItemId});
      if (iBreadCrumbItemIndex !== -1) {
        SharableURLStore.setIsEntityNavigation(false);
        let iStateValue = aBreadCrumbData.length - (iBreadCrumbItemIndex + 1);
        CS.navigateTo(-iStateValue, fCallback);
      } else {
        fCallback();
      }
  };

  var _handleThumbnailDoubleClicked = function (oModel, oFilterContext) {
    let fCallback = () => {

      let fCallbackForCyclicNavigation = () => {
        let oEntity = oModel.properties["entity"];
        let sBaseType = oEntity.baseType;

        let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
        let bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
        let bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
        let isVariantQuicklistOpen = bAvailableEntityChildVariantViewVisibilityStatus || bAvailableEntityParentVariantViewVisibilityStatus;

        let oCallbackData = {
          filterContext: oFilterContext,
          isGetContent: true,
          entityType: sBaseType,
        };

        _updateContextData();

        //set GENERAL as the default selected tab (except if variant is opened)
        if(CS.isEmpty(oVariantSectionViewProps.getSelectedContext()) || isVariantQuicklistOpen){
          let oActiveEntitySelectedTabIdMap = ContentScreenProps.screen.getActiveEntitySelectedTabIdMap();
          if(CS.isEmpty(oActiveEntitySelectedTabIdMap[oModel.id])){
            oActiveEntitySelectedTabIdMap[oModel.id] = {selectedTabId: null};
          }else {
            oActiveEntitySelectedTabIdMap[oModel.id].selectedTabId = null;
          }
        }

        if (oModel.properties["isViolationClicked"]) {
          oCallbackData.isViolationClicked = true;
          let oActiveEntitySelectedTabIdMap = ContentScreenProps.screen.getActiveEntitySelectedTabIdMap();
          let oViolationObject = oEntity.messages;

          /** In case of article-assets relationship, set the default selected tab to overview tab when the asset has expired. */
          if(   oEntity.baseType === BaseTypeDictionary["assetBaseType"] &&             // base type should be of asset
                oViolationObject.isRed &&                                               // red violation, with count only 1 in red count
                oViolationObject.validityMessage == 'Expired'){                         // violation message of type expired
            oActiveEntitySelectedTabIdMap[oModel.id].selectedTabId = "overview_tab";
          }
        }

        ContentStore.getArticleDetails(oModel.id, oCallbackData);
      }
      _handleCyclicNavigationForWindowHistory(oModel.id, fCallbackForCyclicNavigation);
    };
    ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback);
  };

  var _fetchEntityById = function (sEntityId, oCallback) {
      ContentStore.getArticleDetails(sEntityId, oCallback);
  };

  var _handleControlS = function(){
    var bisUOMDialogOpen = ContentUtils.getIsUOMVariantDialogOpen();
    var oScreenProps = ContentScreenProps.screen;
    var sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();
    var bIsHierarchyViewMode = CS.isNotEmpty(sSelectedHierarchyContext);
    let sSelectedModuleId = ContentUtils.getSelectedModuleId();
    let sSelectedDashboardTabId = oScreenProps.getSelectedDashboardTabId();
    let bIsGoldenRecordRemergeSourcesTabClicked = ContentScreenProps.goldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();
    let oActiveContent = ContentUtils.getActiveContent();

    if(ContentUtils.isCollectionScreen() && !oScreenProps.getIsEditMode()) {
      NewCollectionStore.handleCollectionSaved();
    }
    else if (ContentScreenProps.goldenRecordProps.getIsMatchAndMergeViewOpen() || bIsGoldenRecordRemergeSourcesTabClicked) {
      //do nothing
    }
    else if(ContentUtils.isCollectionScreen() && oScreenProps.getIsEditMode()) {
      NewCollectionStore.handleCollectionSaved({});
    }
    else if(sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY && bIsHierarchyViewMode){
      let oFilterContext = {
        filterType: oFilterPropType.HIERARCHY,
        screenContext: HierarchyTypesDictionary.TAXONOMY_HIERARCHY
      };
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleTaxonomySectionSnackbarButtonClicked("save");
    }
    else if(sSelectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY && bIsHierarchyViewMode){
      CollectionAndTaxonomyHierarchyStore.handleCollectionHierarchySnackbarButtonClicked("save");
    }
    else if(bisUOMDialogOpen){
        VariantStore.handleVariantDialogSaveClicked();
    }
    else if(sSelectedModuleId === "dashboard" && sSelectedDashboardTabId === DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB) {
      return;
    }
    else if(ContentScreenProps.taskProps.getIsTaskDialogActive()){
      return;
    }
    else if(!CS.isEmpty(oActiveContent)){
      if (oScreenProps.getIsVersionableAttributeValueChanged()) {
        _handleSaveAndPublishButtonClicked();
      }
      else {
        _handleSaveContentButtonClicked();
      }
    }
  };

  var _handleControlD = function () {
    var oScreenProps = ContentScreenProps.screen;
    var oTaskProps = ContentScreenProps.taskProps;
    var oVariantSectionViewProps = ContentScreenProps.variantSectionViewProps;

    let bIsGoldenRecordRemergeSourcesTabClicked = ContentScreenProps.goldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked()

    var oActiveContent = ContentUtils.getActiveContent();
    var bIsContentDirty = oActiveContent && !!oActiveContent.contentClone;
    var bIsTaskDirty = oTaskProps.getIsTaskDirty();
    var oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();
    var bIsCollectionDirty = oActiveCollection && oActiveCollection.isDirty;
    var bIsVariantDialogOpen = oVariantSectionViewProps.getIsVariantDialogOpen();

    var bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();
    var sSelectedTab = ContentUtils.getSelectedTabId();
    var bIsTaskViewVisible = oTaskProps.getIsTaskDialogActive() || (sSelectedTab === ContentScreenConstants.tabItems.TAB_TASKS);

    var oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
    var bIsCollectionHierarchyDirty = oActiveHierarchyCollection && oActiveHierarchyCollection.clonedObject;
    var bIsCollectionHierarchySelected = oScreenProps.getIsCollectionHierarchySelected();
    var bIsHierarchyViewMode = CS.isNotEmpty(ContentUtils.getSelectedHierarchyContext());
    var oEditableVariant = ContentUtils.getEditableVariant();
    var oDummyVariant = oVariantSectionViewProps.getDummyVariant();
    var bIsDummyVariantDirty = oDummyVariant && oDummyVariant.contentClone;
    var bIsEditableVariantDirty = oEditableVariant && oEditableVariant.contentClone;
    let sSelectedDashboardTabId = oScreenProps.getSelectedDashboardTabId();
    let sSelectedModuleId = ContentUtils.getSelectedModuleId();
    let bIsTableContentDirty = ContentUtils.getIsTableContentDirty();


    //TODO: Taxonomy section dirty is not handled here
    var bIsAnythingDirty = bIsContentDirty || bIsCollectionDirty
        || bIsCollectionHierarchyDirty  || bIsTaskDirty
        || bIsDummyVariantDirty || bIsEditableVariantDirty || bIsTableContentDirty;

    if(bIsAnythingDirty) {
      if(ContentUtils.isCollectionScreen() && !oScreenProps.getIsEditMode()) {
        NewCollectionStore.discardActiveCollection();
      } else if (bIsCollectionHierarchySelected && bIsHierarchyViewMode){
        CollectionAndTaxonomyHierarchyStore.handleCollectionHierarchySnackbarButtonClicked("discard");
      } else if (bIsTaxonomyHierarchySelected && bIsHierarchyViewMode){
        let oFilterContext = {
          filterType: oFilterPropType.HIERARCHY,
          screenContext: HierarchyTypesDictionary.TAXONOMY_HIERARCHY
        }
        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.handleTaxonomySectionSnackbarButtonClicked("discard");
      }
      else if(sSelectedModuleId === "dashboard" && (sSelectedDashboardTabId === "taskDashboardTab" || sSelectedDashboardTabId === "workFlowWorkBenchTab")) {
        return;
      }
      else if (bIsTaskViewVisible){
        return;
      } else
      if (!CS.isEmpty(oActiveContent)){
        /* Calling handleCoverflowDetailViewClosedButtonClicked("assetcoverflowattribute") to support discarding via ctrl+D while custom dialog popup is open
           during adding image to asset*/
        ImageCoverflowStore.handleCoverflowDetailViewClosedButtonClicked("assetcoverflowattribute");
        _handleDiscardUnSavedContentsButtonClicked();
      }
    }
    else if(bIsVariantDialogOpen){
      //do nothing.
    } else if (ContentScreenProps.goldenRecordProps.getIsMatchAndMergeViewOpen() || bIsGoldenRecordRemergeSourcesTabClicked) {
      //do nothing
    }
    else {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_DISCARD);
    }
  };

  let _handleContentAssetBulkUploadButtonClicked = function (sId, oFiles, aCollectionIds, sKlassId, doNotFetchContentList, oFilterContext) {
    trackMe('handleAssetBulkUploadContentButtonClicked');
    let aFiles = oFiles.files;

    if (aFiles.length > 50) {
      alertify.error(getTranslation().MAXIMUM_FIFTY_ITEMS_ALLOWED_MESSAGE);
      return;
    }
    var oCallbackData = {};
    if (!doNotFetchContentList) {
      oCallbackData.functionToExecute = ContentStore.fetchContentList;
      oCallbackData.filterContext = oFilterContext
    }
    var oBulkUploadProps = ContentScreenProps.screen.getBulkUploadProps();
    oBulkUploadProps.klassId = sKlassId;
    oBulkUploadProps.collectionIds = aCollectionIds;
    ImageCoverflowStore.handleContentAssetBulkUploadButtonClicked(sId, oFiles, oCallbackData);
  };

  let handleUploadAssetEvent = function (sContext, sSelectedKlassId, sRelationshipSideId, oFiles, bIsForegroundUpload = false) {
    let oCallbackData = {};
    let oActiveContent = ContentUtils.getActiveContent();
    oCallbackData.isForegroundUpload = bIsForegroundUpload;
    oCallbackData.context = sContext;

    if (sContext === AssetUploadContextDictionary.RELATIONSHIP_SECTION || sContext === AssetUploadContextDictionary.RELATIONSHIP_SECTION_DROP) {
      oCallbackData.functionToExecute = bIsForegroundUpload ? handleLinkAssetToContents.bind(this, sContext, sRelationshipSideId)
                                                            : null;
      if (!bIsForegroundUpload) {
        let sRelationshipId = ContentUtils.getRelationshipIdFromSideId(sRelationshipSideId);
        let aContentRelationships = oActiveContent.contentRelationships;
        let oContentRelationship = CS.find(aContentRelationships, {relationshipId: sRelationshipId});
        let sSelectedTabId = ContentUtils.getSelectedTabId();
        let sTabType = ContentUtils.getTabTypeFromTabId(sSelectedTabId);

        let oModifiedRelationship = {
          relationshipEntityId: oContentRelationship.id,
          relationshipId: sRelationshipId,
          sideId: sRelationshipSideId,
          contextId: "",
        };

        oCallbackData.requestData = {
          keys: ['side1InstanceId', 'side1BaseType', 'tabId', 'tabType', 'modifiedRelationship'],
          values: [oActiveContent.id, oActiveContent.baseType, sSelectedTabId, sTabType, oModifiedRelationship],
        };
      }
    }

    let bIsAvailableEntityView = ContentUtils.getAvailableEntityViewStatus();
    if (sContext === AssetUploadContextDictionary.RELATIONSHIP_QUICKLIST && bIsAvailableEntityView) {
      let oNewFilterContextForFetchAvailableEntities = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      };
      oCallbackData.functionToExecute = AvailableEntityStore.fetchAvailableEntities.bind(this, {filterContext: oNewFilterContextForFetchAvailableEntities});
    }

    let oBulkUploadProps = ContentScreenProps.screen.getBulkUploadProps();
    oBulkUploadProps.klassId = sSelectedKlassId;
    ImageCoverflowStore.handleContentAssetBulkUploadButtonClicked("", oFiles, oCallbackData, sContext);
  };

  let handleInformationTabUploadDialogOkClicked = function (sId, sKlassId) {
    let aFiles = ContentScreenProps.imageCoverflowViewProps.getBulkUploadFiles();
    let oFiles = {files: aFiles};
    _handleContentAssetBulkUploadButtonClicked(sId, oFiles, [], sKlassId, true);
    _triggerChange();
  };

  let _handleUploadAssetDialogOkButtonClicked = function (sContext, sSelectedKlassId, sRelationshipSideId, oFiles) {
    if (CS.isEmpty(sSelectedKlassId)) {
      alertify.error(getTranslation().PLEASE_SELECT_CLASS);
      return;
    }

    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
        ContentScreenProps.informationTabProps.setShowBulkUploadDialog(false);
        handleInformationTabUploadDialogOkClicked("", sSelectedKlassId);
        break;

      case DashboardTabDictionary.DAM_TAB:
        ContentScreenProps.damInformationTabProps.setShowBulkUploadDialog(false);
        handleInformationTabUploadDialogOkClicked("", sSelectedKlassId);
        break;

      case AssetUploadContextDictionary.RELATIONSHIP_SECTION:
        ContentScreenProps.relationshipView.setIsBulkUploadDialogOpen(false);
        if (oFiles.files.length > 50) {
          alertify.error(getTranslation().MAXIMUM_FIFTY_ITEMS_ALLOWED_MESSAGE);
          _triggerChange();
          return;
        }
        if (oFiles.files.length <= 5) {
          handleUploadAssetEvent(sContext, sSelectedKlassId, sRelationshipSideId, oFiles, true);
        } else {
          handleUploadAssetEvent(sContext, sSelectedKlassId, sRelationshipSideId, oFiles);
        }
        break;
      case AssetUploadContextDictionary.RELATIONSHIP_QUICKLIST:
        ContentScreenProps.relationshipView.setIsBulkUploadDialogOpen(false);
        if (oFiles.files.length > 50) {
          alertify.error(getTranslation().MAXIMUM_FIFTY_ITEMS_ALLOWED_MESSAGE);
          _triggerChange();
          return;
        }
        handleUploadAssetEvent(sContext, sSelectedKlassId, sRelationshipSideId, oFiles);
        break;

      case AssetUploadContextDictionary.RELATIONSHIP_SECTION_DROP:
        ContentScreenProps.relationshipView.setIsBulkUploadDialogOpen(false);
        let aAssetFiles = ContentScreenProps.relationshipView.getBulkUploadFiles();
        let oDropFiles = {files: aAssetFiles};

        if (aAssetFiles.length <= 5) {
          handleUploadAssetEvent(sContext, sSelectedKlassId, sRelationshipSideId, oDropFiles, true);
        } else {
          handleUploadAssetEvent(sContext, sSelectedKlassId, sRelationshipSideId, oDropFiles);
        }
        break;
    }
  };

  let _handleUploadAssetDialogCancelButtonClicked = function (sContext) {
    switch (sContext) {
      case DashboardTabDictionary.INFORMATION_TAB:
      case DashboardTabDictionary.DAM_TAB:
        ContentScreenProps.informationTabProps.setShowBulkUploadDialog(false);
        ContentScreenProps.damInformationTabProps.setShowBulkUploadDialog(false);
        break;

      case AssetUploadContextDictionary.RELATIONSHIP_SECTION:
      case AssetUploadContextDictionary.RELATIONSHIP_SECTION_DROP:
      case AssetUploadContextDictionary.RELATIONSHIP_QUICKLIST:
        ContentScreenProps.relationshipView.setIsBulkUploadDialogOpen(false);
        break;
    }
    _triggerChange()

  };

  let _handleUploadAssetDialogButtonClicked = function (sButtonId, sContext, sSelectedKlassId, sRelationshipSideId, oFiles) {
    switch (sButtonId) {
      case "ok":
        _handleUploadAssetDialogOkButtonClicked(sContext, sSelectedKlassId, sRelationshipSideId, oFiles);
        break;

      case "cancel":
        _handleUploadAssetDialogCancelButtonClicked(sContext);
        break;
    }
  };

  var _saveContent = function (oCallbackData) {
    trackMe('handleSaveContentsButtonClicked');

    let UOMProps = ContentScreenProps.uomProps;
    let sTableContextId = UOMProps.getOpenedDialogTableContextId();
    let dummyPromise = Promise.resolve();

    let oVariantContextByContextProps = CS.isEmpty(sTableContextId) ? null : VariantStore.getVariantContextPropsByContext(sTableContextId);
    if ((!CS.isEmpty(oVariantContextByContextProps) && oVariantContextByContextProps.getIsVariantDialogOpen())) {
      VariantStore.handleVariantDialogSaveClicked("", "", oCallbackData.filterContext);
      return dummyPromise;
    }

    /** To handle task save */
    let sSelectedTabId = ContentUtils.getSelectedTabId();
    switch (sSelectedTabId) {
      case  TemplateTabsDictionary.TASKS_TAB:
        return dummyPromise;
    }

    var oActiveContent = ContentUtils.getActiveContentOrVariant();

    //If there is no dirty content in selected list then return
    if (CS.isEmpty(oActiveContent.contentClone) && !ContentUtils.getIsTableContentDirty()) {
      alertify.message(getTranslation().SCREEN_STORE_SAVE_NOTHING_CHANGED); //Nothing changed to save.
      return dummyPromise;
    }
    else if (CS.isNotEmpty(oActiveContent.contentClone)) {
      oCallbackData = oCallbackData || {};
      return ContentStore.saveContents(oActiveContent, oCallbackData);
    }
    else if (ContentUtils.getIsTableContentDirty()) {
      let aDirtyTableContextIds = ContentScreenProps.screen.getDirtyTableContextIds();
      CS.forEach(aDirtyTableContextIds, function (sContextId) {
        VariantStore.fetchVariantData(sContextId, null, false, false, oCallbackData);
      });
      return dummyPromise;
    }
  };

  var _discardSuccess = function () {
    ContentUtils.setShakingStatus(false);
    var oActiveContent = ContentUtils.getActiveContent();
    oComponentProps.availableEntityViewProps.setAvailableEntityViewContext("");
    //Close variant quicklist on discard Click - Neha
    if(oActiveContent.variantInstanceId) {
      var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
      oVariantSectionViewProps.setAvailableEntityChildVariantViewVisibilityStatus(false);
      CommonUtils.removeQuickListBreadCrumbFromPath();
    }
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    _triggerChange();
  };

  var _handleDiscardUnSavedContentsButtonClicked = function () {

    let UOMProps = ContentScreenProps.uomProps;
    let sTableContextId = UOMProps.getOpenedDialogTableContextId();
    let oVariantContextByContextProps = CS.isEmpty(sTableContextId) ? null : VariantStore.getVariantContextPropsByContext(sTableContextId);
    if ((!CS.isEmpty(oVariantContextByContextProps) && oVariantContextByContextProps.getIsVariantDialogOpen())) {
      VariantStore.handleVariantDialogCancelClicked();
      return;
    }

    /** To handle task discard*/
    let sSelectedTabId = ContentUtils.getSelectedTabId();
    switch (sSelectedTabId) {
      case  TemplateTabsDictionary.TASKS_TAB:
        return;
    }

    var oCallback = {};
    oCallback.functionToExecute = _discardSuccess;

    var oActiveContent = ContentUtils.getActiveContent();
    var sActiveContentId = oActiveContent.id;
    var oScreenProps = ContentUtils.getComponentProps().screen;
    var oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    oScreenProps.setIsVersionableAttributeValueChanged(false);
    if (ContentUtils.getIsKlassInstanceFlatPropertyUpdated()) {
      ContentUtils.setIsKlassInstanceFlatPropertyUpdated(false);
    }
    if (oActiveEntitySelectedTabIdMap[sActiveContentId].selectedTabId == ContentScreenConstants.tabItems.TAB_TIMELINE
        && oComponentProps.timelineProps.getIsComparisonMode()) {
      TimelineStore.handleTimelineCompareVersionButtonClicked(true);
    }
    if (ContentUtils.getIsTableContentDirty()) {
      let aDirtyTableContextIds = ContentScreenProps.screen.getDirtyTableContextIds();
      CS.forEach(aDirtyTableContextIds, function (sContextId) {
        VariantStore.handleVariantDiscardButtonClicked(sContextId);
      });
    }
    _discardActiveEntity(oCallback);
  };

  var _dropLinkedInstanceIntoCreatedVariant = function(aSelectedObjects, oFilterContext){
    var oAppData = ContentUtils.getAppData();
    var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    var oActiveContent = ContentUtils.makeActiveContentDirty();
    var aAvailableEntities = oAppData.getAvailableEntities();
    CS.forEach(aSelectedObjects, function (oSelectedObject) {
      var oEntity = CS.find(aAvailableEntities, {id: oSelectedObject.id});
      if(oEntity) {
        var sSelectedEntity = oVariantSectionViewProps.getSelectedEntity();
        if(CS.isEmpty(oActiveContent.linkedInstances)) {
          oActiveContent.linkedInstances = {};
          sSelectedEntity && (oActiveContent.linkedInstances[sSelectedEntity] = []);
        }
        if(!oActiveContent.linkedInstances[sSelectedEntity]) {
          oActiveContent.linkedInstances[sSelectedEntity] = [];
        }
        oActiveContent.linkedInstances[sSelectedEntity].push(oEntity);
      }
    });

    var oSaveCallbackData = {};
    oSaveCallbackData.functionToExecute =  _addNewEntityInRelationshipVerticalMenuClicked.bind(this, "", {}, "manageVariantLinkedInstances", oFilterContext);
    ContentStore.saveContents(ContentUtils.getActiveEntity(), oSaveCallbackData);
  };

  var _dropLinkedInstanceIntoVariantUnderCreation = function (aSelectedObjects, oFilterContext) {
    var oAppData = ContentUtils.getAppData();
    var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    var aAvailableEntities = oAppData.getAvailableEntities();
    var oVariant = ContentUtils.makeActiveContentOrVariantDirty();

    CS.forEach(aSelectedObjects, function (oSelectedObject) {
      var oEntity = CS.find(aAvailableEntities, {id: oSelectedObject.id});
      if(oEntity) {
        var sSelectedEntity = oVariantSectionViewProps.getSelectedEntity();
        if(CS.isEmpty(oVariant.linkedInstances)) {
          oVariant.linkedInstances = {};
          sSelectedEntity && (oVariant.linkedInstances[sSelectedEntity] = []);
        }
        if(!oVariant.linkedInstances[sSelectedEntity]) {
          oVariant.linkedInstances[sSelectedEntity] = [];
        }
        oVariant.linkedInstances[sSelectedEntity].push(oEntity);
      }
    });

    _addNewEntityInRelationshipVerticalMenuClicked("", {}, "contextEntity", oFilterContext);
  };

  let _getRelationshipKeyInEntityByType = function (sRelationshipType) {
    let sKeyToFindRelationShip = "contentRelationships";
    if (ContentUtils.isNatureRelationship(sRelationshipType) || ContentUtils.isVariantRelationship(sRelationshipType)) {
      sKeyToFindRelationShip = "natureRelationships";
    }
    return sKeyToFindRelationShip;
  };

  var _getVariantTagsForLinkedRelationshipContent = function (sRelationshipId, sRelationshipContentId, sRelationshipType) {
    var oActiveEntity = ContentUtils.getActiveEntity();
    oActiveEntity = oActiveEntity.contentClone || oActiveEntity;
    var aTagsToReturn = [];

    let sKeyToFindRelationShip = _getRelationshipKeyInEntityByType(sRelationshipType);
    let oContentRelationship = CS.find(oActiveEntity[sKeyToFindRelationShip], {relationshipId: sRelationshipId});
    if (!CS.isEmpty(oContentRelationship) && !CS.isEmpty(oContentRelationship.elementTagMapping)) {
      var oElementTagMapping = oContentRelationship.elementTagMapping;
      aTagsToReturn = oElementTagMapping[sRelationshipContentId];
      // aTagsToReturn = CS.cloneDeep(oElementTagMapping[sRelationshipContentId]);
    }
    return aTagsToReturn;
  };

    //todo move to contentrelationshipstore rohan
  var _getReferencedRelationshipContexts = function(sRelationshipId){
    var oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    var oReferencedRelationshipContexts = oReferencedVariantContexts.relationshipVariantContexts;
    let oReferencedProductVariants = oReferencedVariantContexts.productVariantContexts;
    var oReferencedElements = ContentScreenProps.screen.getReferencedElements();
    var oReferencedRelationshipElement = oReferencedElements[sRelationshipId];
    var sRelationshipContextId = oReferencedRelationshipElement.relationshipSide.contextId;

    var oReferencedRelationshipContext = null;
    if(sRelationshipContextId){
      if(oReferencedRelationshipContexts && oReferencedRelationshipContexts[sRelationshipContextId]){
        oReferencedRelationshipContext = oReferencedRelationshipContexts[sRelationshipContextId];
      }else if(oReferencedProductVariants && oReferencedProductVariants[sRelationshipContextId]){
        oReferencedRelationshipContext = oReferencedProductVariants[sRelationshipContextId];
      }

      if(!CS.isEmpty(oReferencedRelationshipContext)){
        oReferencedRelationshipContext.actualRelationshipId = oReferencedRelationshipElement.propertyId;
      }
    }

    return oReferencedRelationshipContext;
  };

  var _setRelationshipContextData = function(sRelationshipId, oContext, bIsEditable, bDontTrigger){

    var oRelationshipContextData = {};
    var oEntity = {
      tags : []
    };

    CS.forEach(oContext.tags, function (oMasterTag) {
      ContentUtils.addAllMasterTagInEntity(oEntity, oMasterTag);
    });

    let oDefaultTimeRange = oContext.defaultTimeRange;
    //set Default time range selected in config

    let oTimeRangeToSet = {
      to: null,
      from: null
    };
    if (oDefaultTimeRange) {
      let iFrom = oDefaultTimeRange.from;
      if (oDefaultTimeRange.isCurrentTime) {
        var oDate = new Date();
        oDate.setHours(0, 0, 0, 0); //Ignore time consider only date.
        iFrom = oDate.getTime();
      }
      oTimeRangeToSet = {
        from: iFrom,
        to: oDefaultTimeRange.to
      }
    }
    oRelationshipContextData.context = oContext;
    oRelationshipContextData.relationshipId = sRelationshipId;
    oRelationshipContextData.tags = oEntity.tags;
    oRelationshipContextData.isVariantEditable = bIsEditable; //to keep variant tag drawer view editable / un-editable
    oRelationshipContextData.isForSingleContent = false;
    oRelationshipContextData.isEditVariantSectionOpen = true;
    oRelationshipContextData.relationshipContentInstanceId = "";
    oRelationshipContextData.timeRange = oTimeRangeToSet;
    ContentScreenProps.screen.setRelationshipContextData(oRelationshipContextData);

    if(!bDontTrigger){
      _triggerChange();
    }
  };

  var _addNewEntityInRelationshipVerticalMenuClicked = async function (sRelationshipElementId, oRelationshipSide, sContext, oFilterContext) {
    /** TO handle upper bar search text after opening the sliding panel*/
    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let oReferencedElements = ContentScreenProps.screen.getReferencedElements();
    let oRefElement = oReferencedElements[sRelationshipElementId];
    let sRelationshipsId = oRefElement && oRefElement.propertyId || "";
    let oContextData = oRelationshipContextData.context;
    if(!CS.isEmpty(oContextData)){
      if (oContextData.isTimeEnabled) {
        if (oRelationshipContextData.timeRange.from == null ||
            oRelationshipContextData.timeRange.to == null ||
            oRelationshipContextData.timeRange.from == "" ||
            oRelationshipContextData.timeRange.to == "") {
          alertify.error(getTranslation().EmptyMandatoryFieldsException);
          return;
        }
      }
      else {
        let bIsTagValueSelected = CS.isEmpty(oRelationshipContextData.tags);
        /** true when there are no tags added in the context (undeterministic variant)*/
        CS.forEach(oRelationshipContextData.tags, function (oTag) {
          CS.forEach(oTag.tagValues, function (oTagValue) {
            if (oTagValue.relevance !== 0) {
              bIsTagValueSelected = true;
              return true;
            }
          });
          if (bIsTagValueSelected) {
            return true;
          }
        });
        if (!bIsTagValueSelected) {
          alertify.error(getTranslation().EmptyMandatoryFieldsException);
          return;
        }
      }
    }

    if(sContext != "staticCollection" && sContext != 'contextEntity' && sContext != 'manageVariantLinkedInstances'){
      /** Here section Id is same as relationship Id*/
      var oActiveEntity = ContentUtils.getActiveEntity();
      var oTemp = {
        id: sRelationshipElementId,
        status: true,
        entityId: oActiveEntity.id,
        context: sContext,
        relationshipId: sRelationshipsId
      };
      var oAddEntityInRelationshipScreenData = oComponentProps.screen.getAddEntityInRelationshipScreenData();
      oAddEntityInRelationshipScreenData[oActiveEntity.id] = oTemp;

      var oSectionSelectionStatus = oComponentProps.screen.getSetSectionSelectionStatus();
      oSectionSelectionStatus["relationshipContainerSelectionStatus"] = true;
      oSectionSelectionStatus["selectedRelationship"] = {
        id: sRelationshipElementId,
        klassId: oRelationshipSide.klassId,
        cardinality: oRelationshipSide.cardinality,
        relationshipId: sRelationshipsId
      };
    }


    var oReferencedRelationshipsProperties = oComponentProps.screen.getReferencedRelationshipProperties();
    if(!CS.isEmpty(sRelationshipElementId)) {
      let sRelationshipId = ContentUtils.getRelationshipIdFromSideId(sRelationshipElementId);
      var oReferencedRelationship = sRelationshipId ? oReferencedRelationshipsProperties[sRelationshipId] : {};
      if (!CS.isEmpty(oReferencedRelationship) && (CS.isEmpty(oReferencedRelationship.side1.targetType) || CS.isEmpty(oReferencedRelationship.side2.targetType))) {
        oComponentProps.screen.setIsChooseTaxonomyVisible(false);
      }
    }

    var oCallbackData = {
      filterContext: oFilterContext,
      relationshipId: sRelationshipsId || ""
    };

    oComponentProps.rightBarViewProps.setSelectedBarItemId("available_entity");
    oCallbackData.selectedContext = sContext;
    return AvailableEntityStore.fetchAvailableEntities(oCallbackData);
  };

  var _handleRelationshipPresentEntityOkButtonClicked = function () {
    if(ContentUtils.getVariantRelationshipViewStatus()){
      ContentStore.handleCancelVariantCreationClicked();
    }

    //Clear X-ray related data.
    ContentUtils.clearXRayData();

    var oAddEntityInRelationshipScreenData = oComponentProps.screen.getAddEntityInRelationshipScreenData();

    var oActiveEntity = ContentUtils.getActiveEntity();
    delete oAddEntityInRelationshipScreenData[oActiveEntity.id];
    oAppData.setAvailableEntities([]);
    oComponentProps.screen.setRelationshipContextData({});
    oComponentProps.relationshipView.emptySelectedRelationshipElements();
    let bIsKPIExploreOpen = oComponentProps.screen.getIsKpiContentExplorerOpen();
    oComponentProps.screen.setIsChooseTaxonomyVisible(!bIsKPIExploreOpen);
    BreadCrumbStore.removeQuickListBreadCrumbFromPath();
    _triggerChange();
  };

  var _handleRelationshipPresentEntityPropsReset = function (oFilterContext) {
    var oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.resetFilterPropsByContext();

    if(ContentUtils.getVariantRelationshipViewStatus()){
      ContentStore.handleCancelVariantCreationClicked();
    }
    //Clear X-ray related data.
    ContentUtils.clearXRayData();

    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.reset();

    var oAddEntityInRelationshipScreenData = oComponentProps.screen.getAddEntityInRelationshipScreenData();

    var oActiveEntity = ContentUtils.getActiveEntity();
    delete oAddEntityInRelationshipScreenData[oActiveEntity.id];
    oAppData.setAvailableEntities([]);
    oComponentProps.screen.setRelationshipContextData({});
    oComponentProps.relationshipView.emptySelectedRelationshipElements();
    let bIsKPIExploreOpen = oComponentProps.screen.getIsKpiContentExplorerOpen();
    oComponentProps.screen.setIsChooseTaxonomyVisible(!bIsKPIExploreOpen);
  };

  let _resetVariantQuickListProps = function (oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.resetFilterPropsByContext();
    ContentScreenProps.variantSectionViewProps.setAvailableEntityChildVariantViewVisibilityStatus(false);
    ContentScreenProps.availableEntityViewProps.setAvailableEntityViewContext("");
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.reset();
  };

  var _handleSelectAllAvailableEntityClicked = function () {
    var aSelectedAvailableEntities = oComponentProps.availableEntityViewProps.getSelectedEntities();
    var aMasterContentList = oAppData.getAvailableEntities();
    /*** De-select all only if all entities are selected. ***/
    if (aSelectedAvailableEntities.length) {
      oComponentProps.availableEntityViewProps.setSelectedEntities([]);
    } else {
      oComponentProps.availableEntityViewProps.setSelectedEntities(CS.cloneDeep(aMasterContentList));
    }
    _triggerChange();
  };

  var _handleSelectAllVariantEntityClicked = function (oVariant) {
    var aSelectedVariantInstances = oComponentProps.variantSectionViewProps.getSelectedVariantInstances();
    oVariant = oVariant.contentClone ? oVariant.contentClone : oVariant;
    var oLinkedInstances = oVariant.linkedInstances || [];
    var sSelectedEntity = oComponentProps.variantSectionViewProps.getSelectedEntity();
    var aAllVariantInstances = oLinkedInstances[sSelectedEntity];
    /*** De-select all if one or more entities are selected. ***/
    if (aSelectedVariantInstances.length) {
      oComponentProps.variantSectionViewProps.setSelectedVariantInstances([]);
    } else {
      oComponentProps.variantSectionViewProps.setSelectedVariantInstances(CS.cloneDeep(aAllVariantInstances));
    }
    _triggerChange();
  };

  var _handleContentEntityDropInRelationshipSection = function (oCurrentDraggedItem, sItemId, oFilterContext) {
    var aSelectedEntities = oComponentProps.availableEntityViewProps.getSelectedEntities();
    var oSelectedSectionStatus = oComponentProps.screen.getSetSectionSelectionStatus();
    let bIsDraggedItemInSelected = CS.isEmpty(oCurrentDraggedItem) ? true :
                                   CS.isNotEmpty(CS.find(aSelectedEntities, { id: oCurrentDraggedItem.id }));

    if (oSelectedSectionStatus["relationshipContainerSelectionStatus"]) {
      var oRelationshipElement = oSelectedSectionStatus["selectedRelationship"];
      var oRelationshipProps = oComponentProps.relationshipView.getRelationshipToolbarProps();
      let sSideId = oRelationshipElement.id;
      var oActiveRelationship = oRelationshipProps[sSideId];
      let oReferencedElements = oComponentProps.screen.getReferencedElements();
      let oReferencedRelationshipElement = oReferencedElements[sSideId];
      if(!oReferencedRelationshipElement) {
        return;
      }
      let sRelationshipId = oReferencedRelationshipElement.propertyId;

      if (!CS.isEmpty(oActiveRelationship)) {
        var aContentElements = oActiveRelationship.elements;
        if (oReferencedRelationshipElement.relationshipSide &&
            oReferencedRelationshipElement.relationshipSide.cardinality == RelationshipConstants.ONE) {
          if (aContentElements.length == 1 || aSelectedEntities.length > 1 ? (!CS.isEmpty(oCurrentDraggedItem) ? bIsDraggedItemInSelected : true) : false) {
            alertify.error(getTranslation().MORE_THAN_ONE_ELEMENT_NOT_ALLOWED);
            _triggerChange();
            return;
          }
        }
      }

      var sEntityRelationshipsKey = "contentRelationships";
      let bIsNature = false;


      var oReferencedNatureRelationshipsElements = oComponentProps.screen.getReferencedNatureRelationships();
      var oCurrentReferencedNatureRelationship = oReferencedNatureRelationshipsElements[oRelationshipElement.relationshipId];
      if(!CS.isEmpty(oCurrentReferencedNatureRelationship)){
        sEntityRelationshipsKey = "natureRelationships";
        bIsNature = true;
        if(ContentUtils.isNatureRelationship(oCurrentReferencedNatureRelationship.relationshipType)){
          var iMaxNoOfItems = oCurrentReferencedNatureRelationship.maxNoOfItems;
          var iAlreadyPresentElementsCount = oActiveRelationship.totalCount;
          var iSelectedEntitiesCount = (aSelectedEntities.length > 0) ? aSelectedEntities.length : 1;
          if(iMaxNoOfItems != 0 && iMaxNoOfItems < (iAlreadyPresentElementsCount + iSelectedEntitiesCount)){ //iMaxNoOfItems = 0 means can add unlimited elements
            alertify.error(getTranslation().MAX_NUMBER_OF_ITEM_EXCEEDED + getTranslation().MAX_NUMBER_OF_ITEM_ALLOWED + ": (" + iMaxNoOfItems + ")", 0);
            _triggerChange();
            return;
          }
        }

        if (oCurrentReferencedNatureRelationship.relationshipType == RelationshipTypeDictionary.PRODUCT_VARIANT) {
          //in case of product variant relationship, do not allow dropping of multiple elements (allow when no tags are selected)
          var oDummyVariant = ContentScreenProps.variantSectionViewProps.getDummyVariant();
          if (!ContentUtils.isAllRelevanceZero(oDummyVariant.tags) && (aSelectedEntities.length > 1)) {
            alertify.error(getTranslation().MORE_THAN_ONE_ELEMENT_NOT_ALLOWED);
            return;
          }
        }
      }

      var oActiveEntity = ContentUtils.getActiveContent();
      let oCurrentRelationship = CS.find(oActiveEntity[sEntityRelationshipsKey], {sideId: sSideId});

      let oNewRelationship = CS.cloneDeep(oCurrentRelationship);
      var oModifiedRelationshipElements = oComponentProps.relationshipView.getModifiedRelationshipElements();
      var aAddedElementsInRelationship = [];
      oModifiedRelationshipElements.id = sRelationshipId;
      oModifiedRelationshipElements.added = aAddedElementsInRelationship;
      oModifiedRelationshipElements.sideId = sSideId;
      if (!CS.isEmpty(oNewRelationship)) {
        oNewRelationship.isDirty = true;
        let aContentElements = oNewRelationship.elementIds;
        var aAvailableEntities = ContentUtils.getAppData().getAvailableEntities();
        if (!CS.isEmpty(oCurrentDraggedItem)){
          var sCurrentDraggedItemId = oCurrentDraggedItem.id;
          aAddedElementsInRelationship.push(CS.find(aAvailableEntities, {id: sCurrentDraggedItemId}));
          aContentElements.push(oCurrentDraggedItem.id);
        }

        if (!CS.isEmpty(aSelectedEntities) && bIsDraggedItemInSelected) {
          var aTempAdd = CS.reject(aSelectedEntities, {id: oCurrentDraggedItem.id});
          CS.forEach(aTempAdd, function (oEntityToDrop) {
            aAddedElementsInRelationship.push(CS.find(aAvailableEntities, {id: oEntityToDrop.id}));
            aContentElements.push(oEntityToDrop.id);
          });
        }

          oComponentProps.availableEntityViewProps.setSelectedEntities([]);
          var oSaveContentCallback = {};

          let oNewFilterContextForFetchAvailableEntities = {
          filterType: oFilterPropType.QUICKLIST,
          screenContext: oFilterPropType.QUICKLIST
        };
        oSaveContentCallback.functionToExecute = AvailableEntityStore.fetchAvailableEntities.bind(this, {filterContext: oNewFilterContextForFetchAvailableEntities});
        ContentRelationshipStore.saveRelationship([oCurrentRelationship], [oNewRelationship], bIsNature, oSaveContentCallback);
      }
    }
  };

  var _handleStaticCollectionAddViewCloseClicked = function () {
    oComponentProps.availableEntityViewProps.setSelectedEntities([]);
    oAppData.setAvailableEntities([]);
    BreadCrumbStore.removeQuickListBreadCrumbFromPath();
    NewCollectionStore.handleStaticCollectionAddViewCloseClicked();
  };

  var _handleContextEntityAddViewCloseClicked = function () {
    var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    oComponentProps.availableEntityViewProps.setSelectedEntities([]);
    oComponentProps.availableEntityViewProps.setAvailableEntityViewContext("");
    oComponentProps.availableEntityViewProps.setSelectedRightPanelEntities([]);
    oComponentProps.uomProps.setActivePopOverContextData({});
    oVariantSectionViewProps.setSelectedVariantInstances([]);
    oVariantSectionViewProps.setSelectedEntity("");
    oVariantSectionViewProps.setAvailableEntityChildVariantViewVisibilityStatus(false);
    oVariantSectionViewProps.setAvailableEntityParentVariantViewVisibilityStatus(false);
    BreadCrumbStore.removeQuickListBreadCrumbFromPath();
    _triggerChange();
  };

  var _handleRelationshipContextNextButtonClicked = function (oFilterContext) {
    var oReferencedElements = ContentScreenProps.screen.getReferencedElements();
    var oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    var sRelationshipId = oRelationshipContextData.relationshipId;
    var oReferencedRelationship = oReferencedElements[sRelationshipId];

    if (!_checkValidationForVariantContext()) {
      return;
    }

    let sContextType = oRelationshipContextData.context.type;
    let fAddNewEntityRelationshipFunction = _addNewEntityInRelationshipVerticalMenuClicked.bind(this, sRelationshipId, oReferencedRelationship.relationshipSide, sContextType, oFilterContext);
    if (ContentUtils.isContextTypeProductVariant(sContextType)) {
      ContentStore.validateContextDuplication({functionToExecute : fAddNewEntityRelationshipFunction});
    } else {
      fAddNewEntityRelationshipFunction();
      ContentScreenProps.screen.setIsRelationshipContextDialogOpen(false);
    }
  };

  let _handleSaveAndPublishButtonClicked = function () {
    let oCallbackData = {
      isSaveAndPublish: true
    };
    return _saveContent(oCallbackData);
  };

  let _handleSaveContentButtonClicked = function () {
    return _saveContent({isSaveAndPublish: false});
  };

  var _handleContentEditToolbarButtonClicked = function (sId) {
    let oCallbackData = {};
    switch (sId){
      case 'save' :
      case "saveWithWarning":
        _handleSaveContentButtonClicked();
        break;
      case 'saveAndPublish':
        _handleSaveAndPublishButtonClicked();
        break;
      case "complete":
        return;
      case 'discard' :
        _handleDiscardUnSavedContentsButtonClicked();
        break;
      case 'createClone' :
        if (ContentUtils.activeTaskSafetyCheck()) {
          if (!ContentUtils.contextTableSafetyCheck()) {
            let sContext = ContentUtils.isTargetBaseType(ContentUtils.getActiveEntity().baseType) ?  "createTargetClone" :
                           ContentUtils.isTextAssetBaseType(ContentUtils.getActiveEntity().baseType) ?  "createTextAssetClone" : "createClone";
            CloneWizardStore.openCloneWizardDialog(sContext);
          }
        }
        break;
      case 'refresh':
        if (!ContentUtils.contextTableSafetyCheck()) {
          ContentStore.handleRefreshContentButtonClicked();
        }
        break;
      case 'saveDashboardTask':
        break;
      case 'discardDashboardTask':
        break;
      case 'discardCollection':
        NewCollectionStore.discardActiveCollection();
        break;
      case 'saveCollection':
        NewCollectionStore.handleCollectionSaved(oCallbackData);
        break;
      case 'deleteLanguage':
        ContentStore.deleteCurrentLanguageContent();
        break;
      case 'viewSources':
        GoldenRecordStore.getViewMergeSourcesButtonView();
        break;
      case 'remergeSources':
        let oActiveEntity = ContentUtils.getActiveContent();
        let bIsGoldenRecordRemergeSourcesTabClicked = true;
        GoldenRecordStore.fetchDataForGoldenRecordMatchAndMergeWrapper(oActiveEntity.id, bIsGoldenRecordRemergeSourcesTabClicked);
        break;
    }

  };

  var _handleTaxonomySectionEditToolbarButtonClicked = function (sId, oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    switch (sId) {
      case 'save' :
        oFilterStore.handleSaveFilterTaxonomySection();
        break;

      case 'discard' :
        oFilterStore.handleDiscardFilterTaxonomySection();
        break;
    }
  };

  var _handleAdvancedSearchListItemNodeClicked = function (sItemId, sType, sContext, oFilterContext) {

    if(sType === ConfigDataEntitiesDictionary.ATTRIBUTES) {
      ContentStore.handleContentFilterAttributeAdded(sItemId, sContext, oFilterContext);
    } else if(sType === ConfigDataEntitiesDictionary.TAGS) {
      ContentStore.handleContentFilterTagAdded(sItemId, sContext, oFilterContext);
    } else if(sType === ConfigDataEntitiesDictionary.ROLES) {
      ContentStore.handleContentFilterRoleAdded(sItemId, sContext, oFilterContext);
    }

  };

  var _checkForEmptyFilterValue = function (oFilterContext) {
    var aFilterNames = [];
    var aAttributeChildEmptyNames = [];
    var bAnyOneFilterIsEmpty = false;
    /** @note:
     * *  @one Data Rules need to be hidden in Advanced Filters
     * *  @two New Products Filter, Marketing Article Filter For Recommended Asset
     * *  and Campaign Filter For Recommended Asset will never require Filter Value
     * * @three Asset Expiry Filter will never require Filter Value
     * */
    let aTypesToHide = ["dataRules", "colorVoilation", ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY];
    var oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    var aCurrentAppliedFilter = oFilterProps.getAppliedFiltersClone();
    if (aCurrentAppliedFilter == null) {
      aCurrentAppliedFilter = oFilterProps.getAppliedFilters();
    }

    var aNumberTypes = ["number", "date", "measurementMetrics", "calculated"];

    CS.forEach(aCurrentAppliedFilter, function (oFilter) {
      var bIsEmpty = false;
      if (ContentUtils.isTag(oFilter.type)) {
        let aTagList = [
          TagTypeConstants.YES_NEUTRAL_TAG_TYPE,
          TagTypeConstants.TAG_TYPE_MASTER,
          TagTypeConstants.RULER_TAG_TYPE,
          TagTypeConstants.TAG_TYPE_BOOLEAN,
          TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE,
          TagTypeConstants.LISTING_STATUS_TAG_TYPE,
          TagTypeConstants.STATUS_TAG_TYPE,
          TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE,
          TagTypeConstants.RANGE_TAG_TYPE
        ];
        if (CS.includes(aTagList, oFilter.type)) {
          bIsEmpty = true;
          CS.forEach(oFilter.children, function (oTag) {
            /**
             * (oTag.from != 0 || oTag.to != 0) : at least one value should not be equal to 0
             * (oTag.from >= -100 && oTag.to <= 100) : tag range should lie between -100 to 100
             */
            if((oTag.from != 0 || oTag.to != 0) && oTag.from >= -100 && oTag.to <= 100){
              bIsEmpty = false;
              return false;
            }
          });
        }
      }
      else if (oFilter.type && CS.includes(oFilter.type.toLowerCase(), "attribute")) {
        if(CS.isEmpty(oFilter.children)){
          aAttributeChildEmptyNames.push(oFilter.label);
        }
        else {
          CS.forEach(oFilter.children, function (oChild) {
            if (oChild.type == "range") {
              bIsEmpty = (!ContentUtils.testNumber(oChild.from) || !ContentUtils.testNumber(oChild.to));

            } else if (oChild.type != "notempty" && oChild.type != "empty") {
              var sVisualType = ContentUtils.getAttributeTypeForVisual(oFilter.type);
              if (CS.includes(aNumberTypes, sVisualType)) {
                bIsEmpty = !ContentUtils.testNumber(oChild.value);
              } else {
                bIsEmpty = CS.isEmpty(oChild.value) || oChild.value == "none";
              }
            }
            if(bIsEmpty) return false;
          });
        }
      } else if (CS.includes(aTypesToHide, oFilter.type)) {
        bIsEmpty = false;
        return true;
      } else { // for Roles
        bIsEmpty = CS.isEmpty(oFilter.users);

      }

      if(oFilter.type == TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE){
        var aListToIterate = CS.cloneDeep(oFilter.children);
        CS.forEach(aListToIterate, function (oTag) {
          if(oTag.from == 0 && oTag.to == 0){
            CS.remove(oFilter.children, {id:oTag.id});
          }
        });
      }

      if(bIsEmpty){
        aFilterNames.push(oFilter.label);
        bAnyOneFilterIsEmpty = bIsEmpty;
      }

    });

    return {
      isEmptyFilter: bAnyOneFilterIsEmpty,
      filterNames: aFilterNames,
      attributeChildEmptyNames: aAttributeChildEmptyNames
    }
  };

  var _handleVariantLinkedEntitiesRemoveEntityClicked = function(sLinkedInstanceId, sContextEntity){
    var oVariant = ContentUtils.makeActiveContentOrVariantDirty();

    var oLinkedInstances = ContentUtils.getLinkedInstancesFromVariant(oVariant);
    CS.remove(oLinkedInstances[sContextEntity], function (oLinkedInstance) {
      if (oLinkedInstance.id == sLinkedInstanceId) {
        return true;
      }
    });
    if(CS.isEmpty(oLinkedInstances[sContextEntity])){
      delete oLinkedInstances[sContextEntity];
    }

    ContentScreenProps.screen.setIsVersionableAttributeValueChanged(true);
    _triggerChange();
  };

  var _handleVariantLinkedEntitiesAddEntityClicked = function (sSelectedEntity, oFilterContext){
    var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    oVariantSectionViewProps.setAvailableEntityChildVariantViewVisibilityStatus(true);
    var sContext = "contextEntity";
    oVariantSectionViewProps.setSelectedEntity(sSelectedEntity);
    _addNewEntityInRelationshipVerticalMenuClicked("", {}, sContext, oFilterContext);
  };

  var _handleTableLinkedEntitiesAddEntityClicked = function (oFilterContext, oViewData) {
    ContentScreenProps.availableEntityViewProps.setAvailableEntityViewContext(ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW);
    AvailableEntityStore.fetchAvailableEntities({ filterContext: oFilterContext }, oViewData);
  };

  var _handleBundleToolbarItemClicked = function (sButtonContext, sSideId, sViewContext) {
    let sId = sSideId;
    switch (sButtonContext){
      case "selectAll":
      case "deselectAll":
        ContentRelationshipStore.handleSelectAllRelationshipEntityClicked(sId, sViewContext);
        break;

      case "delete":
        ContentRelationshipStore.handleDeleteAllRelationshipEntityClicked(sId, sViewContext, {});
        break;
    }

    _triggerChange();
  };

  var _handleDeleteAllVariantInstancesClicked = function (oVariant, oFilterContext) {
    var aSelectedVariantInstances = oComponentProps.variantSectionViewProps.getSelectedVariantInstances();
    oVariant = oVariant.contentClone ? oVariant.contentClone : oVariant;
    var oLinkedInstances = oVariant.linkedInstances || [];
    var sSelectedEntity = oComponentProps.variantSectionViewProps.getSelectedEntity();
    var aAllVariantInstances = oLinkedInstances[sSelectedEntity];
    var aNames = [];
    CS.forEach(aAllVariantInstances, function (oElement) {
      if (CS.find(aSelectedVariantInstances, {id:oElement.id})) {
        aNames.push(oElement.name);
      }
    });
    if (aNames.length) {
      ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_UNLINK, aNames,
          function () {
            _deleteAllVariantInstanceElements(oFilterContext);
          }, function (oEvent) {
          });
    } else {
      alertify.message(getTranslation().STORE_ALERT_NOTHING_SELECTED);
    }
  };

  var _deleteAllVariantInstanceElements = function (oFilterContext) {
    var aSelectedVariantInstances = oComponentProps.variantSectionViewProps.getSelectedVariantInstances();
    var oVariant = ContentUtils.makeActiveContentOrVariantDirty();
    var oLinkedInstances = oVariant.linkedInstances || [];
    var sSelectedEntity = oComponentProps.variantSectionViewProps.getSelectedEntity();
    var aAllVariantInstances = oLinkedInstances[sSelectedEntity];

    CS.remove(aAllVariantInstances, function (oElement) {
      return CS.find(aSelectedVariantInstances, {id: oElement.id});
    });

    var oSaveContentCallback = {};
    var bAvailableEntityParentVariantView = oComponentProps.variantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
    oComponentProps.variantSectionViewProps.setSelectedVariantInstances([]);

    if (bAvailableEntityParentVariantView) {
      oSaveContentCallback.functionToExecute = AvailableEntityStore.fetchAvailableEntities.bind(this, { filterContext: oFilterContext });
      ContentStore.saveActiveContent(oSaveContentCallback);
    } else {
      let oCallbackData = {selectedContext : ContentUtils.getSelectedScreenContextForQuickList(), filterContext: oFilterContext}
      AvailableEntityStore.fetchAvailableEntities(oCallbackData);
    }
  };

  var _handleViewRelationshipVariantXRayClicked = function (sRelationshipId, sRelationshipIdToFetchData, oFilterContext) {
    var oRelationshipProps = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
    var bIsXRayEnabled = oRelationshipProps[sRelationshipId].isXRayEnabled;
    if (!bIsXRayEnabled) {
      let oActiveXRayPropertyGroup = oRelationshipProps[sRelationshipId].activeXRayPropertyGroup;
      let bMakeDefaultSelected = CS.isEmpty(oActiveXRayPropertyGroup.properties);
      ContentStore.handleShowXRayPropertyGroupsClicked(bMakeDefaultSelected, false, oFilterContext, sRelationshipId, sRelationshipIdToFetchData);
    }
    oRelationshipProps[sRelationshipId].isXRayEnabled = !bIsXRayEnabled;
    _triggerChange();
  };

  var _handleEntityHeaderEntityNameBlur = function(sLabel){
    if (!sLabel) {
      ContentScreenProps.screen.setEntityHeaderLabelInEditMode(false);
      _triggerChange();
      return;
    }
    var sNameAttributeId = SystemIdDictionary.NameAttributeId;
    var oActiveEntity = ContentUtils.getCurrentContent();
    var oNameAttributeInstance = CS.find(oActiveEntity.attributes, {attributeId: sNameAttributeId});

    if(!CS.isEmpty(oNameAttributeInstance) && oNameAttributeInstance.value != sLabel){
      AttributeStore.handleAttributeVariantInputTextChanged(oNameAttributeInstance.id, sLabel, "");
    }

    ContentScreenProps.screen.setEntityHeaderLabelInEditMode(false);

    _triggerChange();
  };

  var _handleEntityHeaderEntityNameEditClicked = function(){
    if(ContentUtils.activeTaskSafetyCheck()){
      ContentScreenProps.screen.setEntityHeaderLabelInEditMode(true);
    }
    _triggerChange();
  };

  let _fetchScreenMasterData = function () {
    let oAllModuleList = GlobalStore.getAllMenus();
    ContentScreenProps.screen.setAllModuleList(oAllModuleList);

    let oSelectedModule = GlobalStore.getSelectedModule();
    ContentScreenProps.screen.setDefaultViewMode(oSelectedModule.defaultView);
    ContentScreenProps.screen.setViewMode(oSelectedModule.defaultView);
    ContentScreenProps.screen.setCurrentZoom(oSelectedModule.defaultZoomLevel);
  };

  var _handleTaxonomyChildrenLazyData = function (sNodeId, iLevel, oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    var oFilterProps = oFilterStore.getFilterProps();
    let sSelectedOuterParentId = oFilterProps.getSelectedOuterParentId();
    if( sSelectedOuterParentId && sSelectedOuterParentId != "-1") {
      var oExtraData = {};
      oExtraData.parentTaxonomyId = oFilterProps.getSelectedOuterParentId();
      oExtraData.clickedTaxonomyId = sNodeId;
      var oTreeNode = oFilterStore.getTaxonomyNodeById(sNodeId);
      if(CS.isEmpty(oTreeNode.children)) {
        var oCallBackData = {
          filterContext: oFilterContext
        };
        CollectionAndTaxonomyHierarchyStore.handleTaxonomyChildrenLazyData(oCallBackData, oExtraData);
      } else {
        oFilterStore.removeChildPropsFromVisualProps(oTreeNode.children);
        oFilterStore.setAllAffectedTreeNodeIds([]);
        oTreeNode.children = [];
        _triggerChange();
      }
    } else {
      let oVisualProps = oFilterProps.getTaxonomyVisualProps();
      if(!CS.isEmpty(oVisualProps)) {
        oVisualProps[sNodeId] && (oVisualProps[sNodeId].isHidden = !oVisualProps[sNodeId].isHidden);
        _triggerChange();
      }
    }
  };

  let getUpdatedCountAndData = (oFilterContext) => {
    let oExtraData = {
      selectedContext: ContentUtils.getSelectedHierarchyContext(),
      filterContext: oFilterContext
    };
    CollectionAndTaxonomyHierarchyStore.fetchVisibleHierarchyTreeNodeCount(oFilterContext)// To get updated count
        .then(CollectionAndTaxonomyHierarchyStore.handleTaxonomyHierarchyViewModeToggled.bind(this, "thumbnailViewMode", {}, oExtraData)); // To get
    // updated Data
  };

  var _handleTaxonomyHierarchyCutPaste = function (sAddedTaxonomyId, oFilterContext) {
    let oScreenProps = ContentScreenProps.screen;
    var oCutOrCopiedData = CS.cloneDeep(oScreenProps.getHierarchyEntitiesToCopyOrCutData()); //Clone is necessary
    var sDeletedTaxonomyId = oCutOrCopiedData.hierarchyNodeId;

    if(sAddedTaxonomyId == sDeletedTaxonomyId){
      ContentUtils.showMessage(getTranslation().ENTITIES_ALREADY_EXISTS);
      return;
    }

    if(sAddedTaxonomyId == "-1"){
      ContentUtils.showMessage(getTranslation().PASTE_NOT_ALLOWED);
      return;
    }

    var fTaxonomyPasteCallbackFunction = function (oFilterContext) {
      let iResponseCount = oScreenProps.getMultipleTaxonomyHierarchyTypeChangeCallCounter();
      if (iResponseCount === 0) {
        oScreenProps.setHierarchyEntitiesToCopyOrCutData({});
        setTimeout(getUpdatedCountAndData.bind(this, oFilterContext), 1000);
      }
    };

    var aEntityList = oCutOrCopiedData.entityList;
    oScreenProps.setMultipleTaxonomyHierarchyTypeChangeCallCounter(CS.size(aEntityList));
    CS.forEach(aEntityList, function (oEntity) {
      var oADM = {
        addedTaxonomyIds  : [sAddedTaxonomyId],
        deletedTaxonomyIds  : [sDeletedTaxonomyId]
      };

      var oCallbackData = {
        droppedEntity: oEntity,
        successMessage: getTranslation().COLLECTION_CONTENT_SUCCESSFULLY_ADDED,
        functionToExecute: fTaxonomyPasteCallbackFunction.bind(this, oFilterContext)
      };

      ContentStore.handleApplyClassification(oADM, oCallbackData);
    });
  };

  var _removeEntityFromTaxonomy = function (oModel, oFilterContext) {
    var oScreenProps = ContentScreenProps.screen;
    var oEntity = oModel.properties['entity'];
    var sFromDropTaxonomyId = oScreenProps.getActiveHierarchyNodeId();
    var oADM = {
      deletedTaxonomyIds : [sFromDropTaxonomyId]
    };

    var fLocalFunction = function () {
      setTimeout(getUpdatedCountAndData.bind(this, oFilterContext), 1000);
    };
    var oCallbackData = {
      droppedEntity: oEntity,
      successMessage: getTranslation().REMOVED_SUCCESSFULLY,
      functionToExecute: fLocalFunction
    };

    ContentStore.handleApplyClassification(oADM, oCallbackData);
  };

  var _handleThumbContextMenuCopyOrCutClicked = function (sMenuContext) {
    var sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();
    if(sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY || sSelectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
      /** Handle Taxonomy and Collection Copy and Cut*/
      CollectionAndTaxonomyHierarchyStore.copyOrCutSelectedEntitiesInHierarchy(sMenuContext);
    }
  };

  var _handleHierarchyNodeContextItemMenuPasteClicked = function (oFilterContext) {
    var oScreenProps = ContentScreenProps.screen;
    var sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();

    var oCutOrCopiedData = oScreenProps.getHierarchyEntitiesToCopyOrCutData();
    var sAction = oCutOrCopiedData.action;
    var sActiveHierarchyNodeId = oScreenProps.getActiveHierarchyNodeId();
    if(sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
      if(sAction == "copy"){
        CollectionAndTaxonomyHierarchyStore.pasteCopiedSelectedEntityInTaxonomyHierarchy(sActiveHierarchyNodeId, oFilterContext);
      }else if(sAction == "cut"){
        _pasteCutSelectedEntityInTaxonomyHierarchy(sActiveHierarchyNodeId, oFilterContext);
      }
    }
    else if(sSelectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
      var fCollectionPasteCallbackFunction = function () {
        oScreenProps.setHierarchyEntitiesToCopyOrCutData({});
        let oReqData = {
          selectedContext: sSelectedHierarchyContext,
          clickedNodeId: sActiveHierarchyNodeId
        };
        CollectionAndTaxonomyHierarchyStore.handleCollectionHierarchyViewModeToggled("thumbnailViewMode", oReqData, true, oFilterContext);
      };

      var oCallbackData = {};
      oCallbackData.functionToExecute = fCollectionPasteCallbackFunction;

      if(sAction == "copy"){
        NewCollectionStore.pasteCopiedSelectedEntityInCollectionHierarchy(sActiveHierarchyNodeId, oCallbackData);
      }else if(sAction == "cut"){
        NewCollectionStore.pasteCutSelectedEntityInCollectionHierarchy(sActiveHierarchyNodeId, oCallbackData);
      }
    }

    oScreenProps.setRightClickedHierarchyTreeNodeData({});
    _triggerChange();
  };

  var _pasteCutSelectedEntityInTaxonomyHierarchy = function (sAddedTaxonomyId, oFilterContext) {
    _handleTaxonomyHierarchyCutPaste(sAddedTaxonomyId, oFilterContext);
  };

  var _handleRemoveFilterGroupClicked = function (sFilterGroupId, sSelectedHierarchyContext, oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.removeFilterByGroupId(sFilterGroupId);
    var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();

    if (ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy) {
      if (CS.isEmpty(sSelectedHierarchyContext)) {
        sSelectedHierarchyContext = ContentUtils.getSelectedScreenContextForQuickList();
      }
      AvailableEntityStore.handleSearchApplyClicked({
        filterContext: oFilterContext,
        isSearchApplyClicked: true
      }, {}, sSelectedHierarchyContext);
    } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
      GoldenRecordStore.handleFilterButtonClicked(oFilterContext);
    } else {
      ContentStore.handleFilterButtonClicked({filterContext: oFilterContext});
    }
  };

  let _handleApplyLazyFilter = function (oFilterData) {
    //not applicable for uom data
    let oSelectedFilterData = oFilterData.selectedFilterData;
    let oFilterStore = FilterStoreFactory.getFilterStore(oSelectedFilterData.filterContext);
    oFilterStore.handleApplyLazyFilter(oFilterData);
    ContentScreenStore.handleFilterButtonClicked(false, {}, oSelectedFilterData.selectedHierarchyContext, oSelectedFilterData.filterContext);
  };

  var _handleClearAllAppliedFilterClicked = function (oCallback, sSelectedHierarchyContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallback.filterContext);
    oFilterStore.handleClearAllAppliedFilterClicked();
    if(ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()){
      ContentStore.handleAvailableEntityFilterButtonClicked({filterContext: oCallback.filterContext}, {}, sSelectedHierarchyContext);
    } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
      GoldenRecordStore.handleFilterButtonClicked(oCallback.filterContext);
    } else {
      ContentStore.handleFilterButtonClicked(oCallback);
    }
  };

  var _toggleHierarchyScrollEnableDisableProp = function () {
    var oScreenProps = ContentScreenProps.screen;
    var bCurrentVal = oScreenProps.getIsHierarchyTreeScrollAutomaticallyEnabled();
    oScreenProps.setIsHierarchyTreeScrollAutomaticallyEnabled(!bCurrentVal);
  };

  /**
   * @function _handleCollectionSelected
   * @param sContext
   * @param oCallback we get filter context in oCallback data.
   * @returns {*}
   * @private
   */
  var _handleCollectionSelected = function (oCollection, sContext, oCallback) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oActiveEntity = ContentUtils.getActiveEntity();
    if(oActiveEntity.contentClone) {
      ContentUtils.setShakingStatus(true);
      _triggerChange();
      return;
    }

    //check if active collection is dirty and shake snackbar -
    var oCollectionProps = oComponentProps.collectionViewProps;
    var oActiveCollection = oCollectionProps.getActiveCollection();
    if(oActiveCollection && oActiveCollection.isDirty) {
      ContentUtils.setShakingStatus(true);
      _triggerChange();
      return;
    }

    CollectionAndTaxonomyHierarchyStore.clearContentHierarchyRelatedData();
    let oCallbackData = oCallback || {};

    return NewCollectionStore.handleCollectionSelected(oCollection, sContext, oCallbackData);
  };

  var _removeLinkedInstanceByIdFromVariant = function(sLinkedInstanceId, oVariant){
    var bToExecute = false;
    var oLinkedInstances = ContentUtils.getLinkedInstancesFromVariant(oVariant);

    CS.forEach(oLinkedInstances, function (aLinkedInstance) {
      CS.remove(aLinkedInstance, function (oLinkedInstance) {
        if (oLinkedInstance.id == sLinkedInstanceId) {
          bToExecute = true;
          return true;
        }
      });
    });

    return bToExecute;
  };

  var _handleTableCellValueChanged = function (oCellData, sContext, sTableContextId) {
    ContentStore.changeVariantCellValue(oCellData, sTableContextId);
  };

  var _handleTableOpenButtonClicked = function (sId, sContext) {
    switch (sContext) {
      case "priceVariants":
        var aEntityList = ContentUtils.getEntityList();
        var oVariant = CS.find(aEntityList, {id: sId});
        var oThumbnailModel = new ThumbnailModel(
            oVariant.id,
            oVariant.name,
            "",
            oVariant.tags,
            oVariant.baseType,
            "",
            {}
        );
        ContentStore.fetchVariant(oThumbnailModel);
        break;

      case "uomVariants":
      case ContentScreenViewContextConstants.VARIANT_CONTEXT:
      case ContentScreenViewContextConstants.IMAGE_VARIANT_CONTEXT:
        ContentStore.fetchUOMVariant(sId, {context: sContext});
        break;
    }
  };

  var _handleTableEditButtonClicked = function (sId, sContext, sTableContextId) {
    VariantStore.handleUOMVariantEditButtonClicked(sId, sContext, sTableContextId);
    _triggerChange();
  };

  var _handleTableDeleteButtonClicked = function (sId, sContext, sTableContextId, bDeleteWithoutConfirmation, oFilterContext) {
  };

  var _handleAddClassDropDownListNodeClicked = function (sContext, sId, sTaxonomyId, sTaxonomyType){
    if(sContext == "SecondaryClass") {
      ContentStore.handleSecondaryClassAdded(sId);
    } else if(sContext == "Taxonomy") {
      ContentStore.handleTaxonomyAdded(sId, sTaxonomyType);
    } else if(sContext == "NatureClass") {
      ContentStore.handleNatureClassAdded(sId);
    } else if(sContext == "ClassifyFurther") {
      ContentStore.handleChildTaxonomyAdded(sId, sTaxonomyId, sTaxonomyType);
    }
  };

  var _handleAddTaxonomyPopoverItemClicked = function (sContext, sId, sTaxonomyId, sTaxonomyType){
    _handleAddClassDropDownListNodeClicked(sContext, sId, sTaxonomyId, sTaxonomyType);
  };

  let _updateSectionSelectionStatus = (oRelationshipContextData) => {
    let oReferencedElements = ContentScreenProps.screen.getReferencedElements();
    let sRelationshipId = oRelationshipContextData.relationshipId;
    let oRelationshipSide = oReferencedElements[sRelationshipId].relationshipSide;
    let oRefElement = oReferencedElements[sRelationshipId];
    let sPropertyId = oRefElement && oRefElement.propertyId || "";
    let oSectionSelectionStatus = oComponentProps.screen.getSetSectionSelectionStatus();
    oSectionSelectionStatus["relationshipContainerSelectionStatus"] = true;
    oSectionSelectionStatus["selectedRelationship"] = {
      id: sRelationshipId,
      klassId: oRelationshipSide.klassId,
      cardinality: oRelationshipSide.cardinality,
      relationshipId: sPropertyId
    };
  };

  let _handleEmptyContextCreateOrLinkClick = function (oFilterContext, bIsSelectVariant) {
    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let oCurrentContext = oRelationshipContextData.context;
    let bIsUnDeterminedContext = (CS.isEmpty(oCurrentContext.tags) && !oCurrentContext.isTimeEnabled);
    oRelationshipContextData.isDummy = true;

    if (bIsUnDeterminedContext) {
      if(oCurrentContext.type == "productVariant" && !bIsSelectVariant){
        _updateSectionSelectionStatus(oRelationshipContextData);
        CloneWizardStore.openCloneWizardDialog(oCurrentContext.type);
      } else {
        _handleRelationshipContextNextButtonClicked(oFilterContext);
      }
    }else {
      _triggerChange();
    }
  };

  let _toggleThumbnailSelection = (oModel) => {
    let oContent = oModel.properties['entity'];
    let oScreenProps = oComponentProps.screen;
    let bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();
    let bIsCollectionHierarchySelected = oScreenProps.getIsCollectionHierarchySelected();
    let bAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
    if (bAvailableEntityViewStatus || bIsTaxonomyHierarchySelected || bIsCollectionHierarchySelected) {
      var bClickedWithControl = true;
      AvailableEntityStore.handleThumbnailClicked(oModel, bClickedWithControl);
      _triggerChange();
      return;
    }
    ContentStore.toggleContentThumbnailSelection(oContent);
    _triggerChange();
  };

  let _handleThumbnailDeleteIconClicked = function (oModel, oFilterContext) {
    let oEntity = oModel.properties['entity'];
    let oScreenProps = oComponentProps.screen;
    let bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();
    if (bIsTaxonomyHierarchySelected) {
      ContentUtils.listModeConfirmation(
          getTranslation().STORE_ALERT_CONFIRM_UNLINK,
          [oModel.label],
          function () {
            _removeEntityFromTaxonomy(oModel, oFilterContext);
          },
          function (oEvent) {
          })
    }
    else {
      ContentStore.handleThumbnailDeleteIconClicked(oEntity);
    }
  };

  let _handleDashboardTabChanged = function (sTabId) {
    switch (sTabId) {
      case DashboardTabDictionary.BUCKETS_TAB:
        let oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: "matchAndMerge"
        };
        return GoldenRecordStore.handleDashboardBucketsTabClicked({filterContext: oFilterContext});
      case DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB:
        return;
    }
    return new Promise((resolve) => { resolve(null); });
  };

  let _resetDashboardTabData = (sOldTabId) => {
    switch (sOldTabId) {
      case DashboardTabDictionary.BUCKETS_TAB:
        ContentScreenProps.goldenRecordProps.setShowGoldenRecordBuckets(false);
        ContentScreenProps.goldenRecordProps.reset();
        break;
      case DashboardTabDictionary.DAM_TAB:
      case DashboardTabDictionary.DATA_GOVERNANCE_TAB:
      case DashboardTabDictionary.DATA_INTEGRATION_TAB:
      case DashboardTabDictionary.INFORMATION_TAB:
      case DashboardTabDictionary.TASK_DASHBOARD:
      case DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB:
        // For defaultDashboard tab
      case "dashboardtab":
        DashboardScreenProps.setKPIChartInvertData({});
        break;

        // For custom tabs
      default:
        DashboardScreenProps.setKPIChartInvertData({});
    }
  };

  let _handleTabLayoutViewTabChanged = function (sTabId, sContext) {
    let oPromise = new Promise((resolve) => { resolve(null); });
    let oScreenProps = ContentScreenProps.screen;
    switch (sContext) {
      case "dashboardScreen":
        _resetDashboardTabData(oScreenProps.getSelectedDashboardTabId());
        oScreenProps.setSelectedDashboardTabId(sTabId);
        oPromise = _handleDashboardTabChanged(sTabId);
        break;
    }

    return oPromise;
  };

  let _handleTableLinkedEntitiesRightPanelSelectAll = function () {
    let oActivePopOverContextData = oComponentProps.uomProps.getActivePopOverContextData();
    let oActiveContent = ContentUtils.getActiveContent();
    let oPopOverContextTableViewProps = oComponentProps.tableViewProps.getTableViewPropsByContext(oActivePopOverContextData.idForProps || oActivePopOverContextData.contextId, oActiveContent.id);
    let aBodyData = oPopOverContextTableViewProps.getBodyData();
    let oRow;
    let oActivePopOverContextEntityData;
    if (oActivePopOverContextData.isTranspose) {
      oRow = CS.find(aBodyData, {id: oActivePopOverContextData.entity});
      oActivePopOverContextEntityData = oRow.properties[oActivePopOverContextData.contextInstanceId];
    } else {
      oRow = CS.find(aBodyData, {id: oActivePopOverContextData.contextInstanceId});
      oActivePopOverContextEntityData = oRow.properties[oActivePopOverContextData.entity];
    }
    let aPresentContextEntities = oActivePopOverContextEntityData.referencedLinkedEntites;
    AvailableEntityStore.toggleRightPanelSelectAllEntities(aPresentContextEntities);
  };

  let _removeTableEntitiesFromAvailableEntitiesView = function (aIds, oFilterContext) {
    AvailableEntityStore.removeFromRightPanelSelectedEntities(aIds);
    VariantStore.handleRemoveLinkedInstanceClicked({}, aIds, "variantQuickList");
    AvailableEntityStore.fetchAvailableEntities({filterContext: oFilterContext}, {});
  };

  let _handleCreateModuleEntityByBaseType = function (oItem, oCallbackData) {
    switch (oItem.type) {
      default:
        _updateContextData();
        oCallbackData = oCallbackData || {};
        ContentStore.createNewModuleEntity(oItem, oCallbackData);
        break;
    }
  };

  let _handleDIClosedClicked = function (oFilterContext) {
    /**
     * Reset filter props
     */
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.resetFilterPropsByContext();

    _resetSessionPropsRelatedToDI();
    let bDontResetPhysicalCatalog = !(SessionProps.getSessionPhysicalCatalogId() === PhysicalCatalogDictionary.DATAINTEGRATION); //in case of a user who only has data integration permission
    GlobalStore.updateSessionProps(bDontResetPhysicalCatalog);

    let oCallbackForCurrentUser = {};
    oCallbackForCurrentUser.functionToExecute = () => {
      let oCallback = {};
      oCallback.functionToExecute = () => {
        let oActiveEndpoint = SessionProps.getActiveEndpointData();
        let oCallbackForRefreshEndpoint = {
          functionToExecute: SessionProps.setActiveEndpointData.bind(this, {})
        };
        _handleRefreshEndpoint(oActiveEndpoint.id, oActiveEndpoint.tileMode, oCallbackForRefreshEndpoint);
      };
      _refreshAll(null, oCallback);
    };
    GlobalStore.fetchCurrentUserInformation(oCallbackForCurrentUser);
  };

  var _handleCloseKpiContentExplorerClicked = function (oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setIsKpiContentExplorerOpen(false);
    oScreenProps.setIsChooseTaxonomyVisible(true);
    oScreenProps.setViewMode(oScreenProps.getDefaultViewMode());
    oFilterStore.resetFilterPropsByContext();

    let fFunctionToExecute = () => {
      ContentScreenProps.screen.setKpiContentExplorerData({});
    };

    let oCallback = {
      functionToExecute: fFunctionToExecute
    };

    _handleRefreshKPIButtonClicked(ContentScreenProps.screen.getKpiContentExplorerData(), oCallback);
    _triggerChange();
  };

  var _handleSafetyCheckForFileMappingViewVisibility = function (oCallBackData) {
    let fFunctionToExecute = () => {
      ContentScreenProps.screen.setOnboardingFileMappingViewVisibilityStatus(false);
      if(oCallBackData){
        oCallBackData.functionToExecute();
      }
    };
    CustomActionDialogStore.showConfirmDialog(getTranslation().DIALOG_HEADER_FILE_UPLOAD_ABORT_CONFIRMATION, '', fFunctionToExecute);
  };

  var _handleContentWrapperViewBackButtonClicked = function (oFilterContext) {
    let bFileMappingVisibility = ContentScreenProps.screen.getOnboardingFileMappingViewVisibilityStatus();
    if(bFileMappingVisibility ){
      let oCallBackData = {
        functionToExecute: _handleContentWrapperViewBackButtonClicked.bind(this, oFilterContext)
      };
      _handleSafetyCheckForFileMappingViewVisibility(oCallBackData);
      return;
    }
    ContentScreenProps.screen.setPreventDashboardDataReset(true);
    if (ContentScreenProps.screen.getIsKpiContentExplorerOpen()) {
      _handleCloseKpiContentExplorerClicked(oFilterContext);
    } else if (ContentScreenProps.informationTabProps.getIsRuleViolatedContentsScreen()) {
      let oCallBack = { context: DashboardTabDictionary.INFORMATION_TAB}
      InformationTabStore.handleDashboardTabClicked(oCallBack);
      ContentScreenProps.informationTabProps.setIsRuleViolatedContentsScreen(false);
    } else if (ContentScreenProps.damInformationTabProps.getIsRuleViolatedContentsScreen()) {
      let oCallBack = { context: DashboardTabDictionary.DAM_TAB}
      InformationTabStore.handleDashboardTabClicked(oCallBack);
      ContentScreenProps.damInformationTabProps.setIsRuleViolatedContentsScreen(false);
    } else {
      _handleDIClosedClicked();
    }
  };

  var _resetSessionPropsRelatedToDI = function () {
    SessionProps.setSessionPhysicalCatalogData(SessionProps.getSessionPreviousPhysicalCatalogId());
    SessionProps.setSessionPreviousPhysicalCatalogId("");
    SessionProps.setSessionEndpointId("");
    SessionProps.setSessionEndpointType("");
    let sDefaultViewMode = ContentScreenProps.screen.getDefaultViewMode();
    ContentScreenProps.screen.setViewMode(sDefaultViewMode);
  };

  var _handleRefreshEndpoint = function (sEndpointId, sTileMode, oCallback) {
    DashboardScreenStore.handleRefreshEndpoint(sEndpointId, sTileMode, oCallback);
  };

  let _refreshAll = async function (sMode, oCallbackData = {}) {
    _fetchScreenMasterData();
    await ContentStore.fetchUserList();
    await ContentStore.fetchSmartDocumentConfigDetails(); //Getting SmartDocument Details


    if (sMode === "Runtime" && ContentUtils.getIsCurrentUserSystemAdmin()) {
      /**in case of system admin user do not Runtime call since there is no runtime for system admin user**/
    }
    else {
      let sEntityId = SharableURLProps.getEntityId();
      if (!CS.isEmpty(sEntityId)) {
        let aAllMenus = GlobalStore.getAllMenus();
        let oSelectedModule = CS.find(aAllMenus, {isSelected: true});
        let sLabel = getTranslation()[oSelectedModule.label] || oSelectedModule.label;
        let oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE
        };
        let oCallbackData = {
          filterContext: oFilterContext
        };
        let aPayloadData = [-1, oCallbackData];
        let fFunctionToSet = ContentStore.getArticleDetails;
        let oRootBreadCrumb = BreadCrumbStore.createBreadcrumbItem(oSelectedModule.id, sLabel, "module", "", oFilterContext, {}, "", aPayloadData, fFunctionToSet);

        /** Required to replace URL for null state **/
        SharableURLStore.replaceParamsInWindowURL();

        BreadCrumbStore.addNewBreadCrumbItem(oRootBreadCrumb);

        let oTempCallbackData = {
          entityType: SharableURLProps.getEntityBaseType()
        };
        await ContentStore.fetchArticleById(sEntityId, oTempCallbackData);
      } else if(ContentUtils.getIsArchive()) {
        let oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE
        };
        ContentStore.fetchContentList({filterContext: oFilterContext});
      } else {
        /** not needed in case of task dashboard***/
        /**call for selected module in case dashboard is disabled for the user**/
        /** when navigating Inside data integration, to send call for selected module**/
        let oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE
        };
        let sSelectedModuleId = ContentUtils.getSelectedModule().id;
        if (sSelectedModuleId !== ModulesDictionary.DASHBOARD) {
          await ContentScreenStore.handleModuleItemClicked(sSelectedModuleId, oCallbackData.filterContext || oFilterContext);
        }
      }
    }
    await CS.getFunctionToExecute(oCallbackData);
  };

  var _handleRefreshKPIButtonClicked = function (oKpiData, oCallback) {
    DashboardScreenStore.handleDashboardRefreshTile(oKpiData, oCallback);
  };

  let _handleCollectionHierarchyPropReset = function (oFilterContext) {
    CollectionAndTaxonomyHierarchyStore.clearContentHierarchyRelatedData();
    ContentUtils.getAppData().emptyAvailableEntities();
    ContentScreenProps.collectionViewProps.setPublicPrivateModeButtonVisibility(true);
    //Reset filter props
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.resetFilterPropsByContext();
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.reset();
  };

  let _handleTaxonomyHierarchyPropReset = function (oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.handleSelectedTaxonomiesClearAllClicked(true);
    oFilterStore.handleClearAllAppliedFilterClicked(true);
    CollectionAndTaxonomyHierarchyStore.clearContentHierarchyRelatedData();
    ContentUtils.getAppData().emptyAvailableEntities();
    oFilterStore.clearTaxonomySectionData();
    oFilterStore.handleTaxonomyShowPopOverStateChanged(false);
    //Reset filter props
    oFilterStore.resetFilterPropsByContext();
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.reset();
  };

  let _handleSectionGridViewActionEntityClicked = function (sActionItemId, sContentId) {
  let oEntity = {};
  let sSideIdOrId = ContentScreenProps.screen.getSelectedContext().sectionId;
  let sRelationshipId = ContentUtils.getRelationshipIdFromSideId(sSideIdOrId);
  let aRelationshipElements = ContentScreenProps.relationshipView.getReferencedCommonRelationshipInstanceElements()[sSideIdOrId];
  let oReferencedNatureRelationships = ContentScreenProps.screen.getCommonReferencedRelationships();
  let oReferencedRelationship = oReferencedNatureRelationships[sRelationshipId] || {};
  let sRelationshipType = oReferencedRelationship.relationshipType || oReferencedRelationship.natureType || "";
  oEntity = CS.find(aRelationshipElements, {id: sContentId});
  let oModel = {
    id: sContentId,
    label: oEntity.name,
    type: oEntity.type,
    properties: {
      entity: oEntity
    }
  };
  switch(sActionItemId) {
    case "view":
      _handleThumbnailDoubleClicked(oModel, {});
      break;
    case "delete":
      ContentStore.handleRemoveSelectedContentGridEntities(sSideIdOrId, sRelationshipType, [sContentId]);
      break;
  }

  _triggerChange();
};

  let _handleCollectionEditDialogButtonClicked = function (sContext, oFilterContext) {
    let oCallbackData = {
      filterContext: oFilterContext
    };
    switch (sContext) {
      case "ok":
        NewCollectionStore.handleEditCollectionOKButtonClicked();
        break;
      case "save":
        NewCollectionStore.handleCollectionSaved(oCallbackData);
        break;
      case "cancel":
        NewCollectionStore.cancelEditDialogClicked(oCallbackData);
        break;
    }
  };

  let _handleCommonConfigSectionSingleTextChanged =  function (sContext, sKey, sVal) {
    let sSplitter = ContentUtils.getSplitter();
    let aSplitContext = CS.split(sContext, sSplitter);
    sContext = aSplitContext[0];
    switch (sContext) {
      case "collection":
        NewCollectionStore.handleActiveCollectionLabelChanged(sVal);
        break;
    }
  };

  let _handleCommonConfigSectionViewYesNoButtonClicked = function (sContext, sKey, sVal) {
    let sSplitter = ContentUtils.getSplitter();
    let aSplitContext = CS.split(sContext, sSplitter);
    sContext = aSplitContext[0];
    switch (sContext) {
      case "collection":
        NewCollectionStore.handleActiveCollectionVisibilityChanged();
        break;
    }
  };

  let _handleDashboardBulkUpload = function (aFiles) {
    ImageCoverflowStore.handleDashboardBulkUpload(aFiles);
    let oFilterContext = {screenContext: oFilterPropType.MODULE, filterType: oFilterPropType.MODULE};
    if (ContentScreenProps.screen.getSelectedDashboardTabId() === DashboardTabDictionary.INFORMATION_TAB) {
      ContentScreenProps.informationTabProps.setShowBulkUploadDialog(true);
      InformationTabStore.fetchAssetClassList(oFilterContext, DashboardTabDictionary.INFORMATION_TAB);
    } else if (ContentScreenProps.screen.getSelectedDashboardTabId() === DashboardTabDictionary.DAM_TAB) {
      ContentScreenProps.damInformationTabProps.setShowBulkUploadDialog(true);
      InformationTabStore.fetchAssetClassList(oFilterContext, DashboardTabDictionary.DAM_TAB);
    }
  };

  let _handleRelationshipSectionDrop = function (sContext, aFiles, oFilterContext) {
    if (ContentUtils.isActiveContentDirty()) {
      ContentUtils.showMessage(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return;
    }
    if (aFiles.length > 50) {
      alertify.error(getTranslation().MAXIMUM_FIFTY_ITEMS_ALLOWED_MESSAGE);
      return;
    }

    ContentScreenProps.relationshipView.setIsBulkUploadDialogOpen(true);
    ContentScreenProps.relationshipView.setContext(sContext);
    ContentScreenProps.relationshipView.setBulkUploadFiles(aFiles);
    ContentStore.fetchAssetClassList(oFilterContext);
  };

   let _handleOpenProductFromDashboard = function(sContentId, sBaseType, oFilterContext){
    let oCallbackData = {
      filterContext: oFilterContext
    };
    oCallbackData.entityType = sBaseType;
    _fetchEntityById(sContentId, oCallbackData);
  };

  let _handleRefreshContentButtonClicked = function (oFilterContext) {
    trackMe('_handleRefreshContentsButtonClicked');
    ContentScreenProps.relationshipView.emptySelectedRelationshipElements();
    ContentScreenProps.variantSectionViewProps.setSelectedVariantInstances([]);
    var bIsAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
    var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
    let bShowGoldenRecordBuckets = ContentScreenProps.goldenRecordProps.getShowGoldenRecordBuckets();
    let oCallbackData = {
      filterContext: oFilterContext
    };

    if (bIsAvailableEntityViewStatus) {
      oCallbackData.selectedContext = ContentUtils.getSelectedScreenContextForQuickList();
      AvailableEntityStore.fetchAvailableEntities(oCallbackData);
    } else if (bIsAnyHierarchySelectedExceptFilterHierarchy) {
      oCallbackData.selectedContext = ContentUtils.getSelectedHierarchyContext();
      AvailableEntityStore.fetchAvailableEntities(oCallbackData);
    } else if (bShowGoldenRecordBuckets) {
      oCallbackData.breadCrumbData = {
        id: BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD,
        type: "module",
        extraData: {}
      };
      GoldenRecordStore.fetchGoldenRecordBuckets(oCallbackData);
    } else if (ContentUtils.getSelectedTabId() === ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS) {
      ContentStore.fetchTabSpecificContentsList(oFilterContext.screenContext);
    } else {
      ContentStore.handleRefreshContentButtonClicked()
    }
  };

  let _handleDashboardTabSelected = function (sTabId) {
    ContentScreenProps.screen.setSelectedDashboardTabId(sTabId);

    if(sTabId === DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB) {
      let oTaskData = ContentScreenProps.taskProps.getTaskData();
      let oSelectedModule = ContentUtils.getSelectedModule();
      let sBreadcrumbId = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD;
      oTaskData.callbackData = oTaskData.callbackData || {};
      oTaskData.callbackData.breadCrumbData = BreadCrumbStore.createBreadcrumbItem(sBreadcrumbId, "", BreadCrumbModuleAndHelpScreenIdDictionary.MODULE, oSelectedModule.id);
    }

    _triggerChange();
  };

  let _handleDataLanguageChanged = function (sDataLanguageCode, oCallbackData) {
    let oActiveEntity = ContentScreenProps.screen.getActiveContent();
    let oTabItems = ContentScreenConstants.tabItems;
    CommonUtils.setSelectedDataLanguage(sDataLanguageCode);

    /** If data lang is changed, reset filter data & then refresh the selected module.
     *  All filters should get reset because filter values will get changed for dependent tag values.
     **/
    BreadCrumbStore.resetAllBreadcrumbNodeFilterData();

    /** Required when - only data language changed from top bar (i.e. home screen)  **/
    if (!CS.isEmpty(oActiveEntity)) {
      let sSelectedTabId = ContentUtils.getSelectedTabId();
      let oArticleDetailsCallBack = {};
      let bIsContentNotPresentInCurrentDL = !ContentUtils.isContentAvailableInSelectedDataLanguage(oActiveEntity);
      bIsContentNotPresentInCurrentDL && ContentUtils.showMessage(getTranslation().CONTENT_ABSENT_IN_DATA_LANG);

      //Extra Handling for Virtual Tabs
      if (CS.includes([oTabItems.TAB_DUPLICATE_ASSETS], sSelectedTabId)) {
        oArticleDetailsCallBack.functionToExecute = ContentStore.handleEntityTabClicked.bind(this, sSelectedTabId)
      }
      ContentStore.fetchArticleById(oActiveEntity.id, oArticleDetailsCallBack)
    } else {
      //Do not pass filterContext from here.
      let aBreadcrumbNodes = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
      let oBreadcrumbNode = aBreadcrumbNodes[aBreadcrumbNodes.length - 1];
      let sIsCollectionScreenQuickList = ContentScreenProps.collectionViewProps.getAddEntityInCollectionViewStatus();
      if (sIsCollectionScreenQuickList && oBreadcrumbNode.filterContext.filterType == oFilterPropType.QUICKLIST) {
        this.handleStaticCollectionQuickListOpened(oBreadcrumbNode.filterContext); // Handle language change of collection quicklist
      } else {
        _handleRefreshContentButtonClicked(oBreadcrumbNode.filterContext);
      }
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    }
  };

  let _handleGridPropertyByActiveContent = function () {
    let oActiveContent = ContentUtils.getActiveContent();
    switch (oActiveContent.baseType){}
  };

  let _handleThumbnailInformationViolationIconClicked = function (oEvent) {
    oEvent.extraData = {isViolationClicked: true};
  };

  let _handleThumbnailInformationEntityInfoIconClicked = function (sContentId, sVariantId) {
    let aContentList = ContentScreenAppData.getContentList();
    let oContent = CS.find(aContentList, {id: sContentId});
    let oCallbackData = {
      filterContext: {filterType: "module", screenContext: "module"}
    };
    let oModel = {
      id: sVariantId,
      properties: {
        entity: {
          baseType: oContent.baseType
        }
      }
    };
    _handleThumbnailDoubleClicked(oModel, oCallbackData);
  };

  /**
   * @Router Impl
   *
   * @param oItem: Breadcrumb Item
   * @private
   */
  let _handleBreadCrumbClicked = function (oItem) {
    let oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();
    if (ContentUtils.isActiveContentDirty() || oActiveCollection.isDirty) {
      alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return;
    }

    let aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    let iIndex = CS.findIndex(aBreadcrumb, {id: oItem.id});
    let oLastBreadcrumbNode = aBreadcrumb[aBreadcrumb.length - 1];
    /**
     *  - Required for QuickListView, If we are in a quick list view and Clicked on active content breadCrumb,
     *    Need to open directly content without changing history state.
     *  - Ignore quicklist Item in breadcrumb for deciding new state of history
     **/
    let iStateValue = aBreadcrumb.length - (iIndex + 1);
    /** quick list adjustment for history state **/
    let bIsQuickList = ContentUtils.isQuickListNode(oLastBreadcrumbNode);
    bIsQuickList && (iStateValue -= 1);

    /** Second last item check in breadcrumb if quick list is open **/
    if (bIsQuickList && (iIndex === (aBreadcrumb.length - 2))) {
      ContentStore.handleEntityNavigation(oItem);
    }
    else if (oItem.id === BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD) {
      /** Required to reset history state when back from Dashboard so that forward navigation is disabled**/
      //TODO: Handle below logic through history.go
      iStateValue = iStateValue + 1;
      SharableURLProps.setIsEntityNavigation(false);

      let fCallback = () => {
        SharableURLProps.setIsPushHistoryState(true);
        SharableURLStore.addParamsInWindowURL(oItem.id, oItem.type);
        ContentStore.handleEntityNavigation(oItem);
      };

      CS.navigateTo(-iStateValue, fCallback);

    }
    else {
      SharableURLProps.setIsEntityNavigation(true);
      CS.navigateTo(-iStateValue);
    }
  };

  let _handleHeaderMenuNotificationClicked = function () {
    ContentScreenProps.screen.setActiveContent({});
    DashboardScreenProps.setSelectedDashboardTabId(DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB);
    ContentScreenStore.handleModuleItemClicked("dashboard", {screenContext: oFilterPropType.MODULE, filterType: oFilterPropType.MODULE});
  };

  let _resetStaticCollectionQuickListProps = function (oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.resetFilterPropsByContext();
    let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.reset();
  };

  /**
   *@function _handleResetBreadcrumbNodePropsAccordingToContext
   * @param sContext - BreadCrumb Type
   * @param sContentId
   * @param oPostData
   * @description- Reset the props of given context
   */
  let _handleResetBreadcrumbNodePropsAccordingToContext = function (sContext, sContentId, oPostData) {
    let oScreenProps = ContentScreenProps.screen;
    let oFilterContext = {};
    let oFilterStore = {};
    switch(sContext) {
      case BreadCrumbModuleAndHelpScreenIdDictionary.EXPLORER_KPI_TILE :
        oScreenProps.setIsKpiContentExplorerOpen(false);
        oScreenProps.setIsChooseTaxonomyVisible(true);
        oScreenProps.setViewMode(oScreenProps.getDefaultViewMode());
        oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE,
        };
        oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.resetFilterPropsByContext();
        ContentScreenProps.screen.setKpiContentExplorerData({});
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.DIMODULE :
        oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE,
        };
        _handleDIClosedClicked(oFilterContext);
        SharableURLStore.removeWindowURL();
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST :
        oFilterContext = {
          filterType: oFilterPropType.QUICKLIST,
          screenContext: oFilterPropType.QUICKLIST,
        };

        ContentScreenProps.availableEntityViewProps.setAvailableEntityViewContext("");
        _handleRelationshipPresentEntityPropsReset(oFilterContext);
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.COLLECTION_HIERARCHY:
        oFilterContext = {
          filterType: oFilterPropType.HIERARCHY,
          screenContext: ContentUtils.getSelectedHierarchyContext(),
        };
        _handleCollectionHierarchyPropReset(oFilterContext);
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION:
      case BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION:
        ContentStore.handleClearCollectionProps();
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY:
        oFilterContext = {
          filterType: oFilterPropType.HIERARCHY,
          screenContext: ContentUtils.getSelectedHierarchyContext(),
        };

        _handleTaxonomyHierarchyPropReset(oFilterContext);
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST:
        oFilterContext = {
          filterType: oFilterPropType.QUICKLIST,
          screenContext: oFilterPropType.QUICKLIST,
        };
        _resetVariantQuickListProps(oFilterContext);
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT:
        let oActiveContent = ContentUtils.getActiveContent();
        let sActiveContentId = oActiveContent.id;
        let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
        let oActiveContentMap = oActiveEntitySelectedTabIdMap[sActiveContentId];
        if (oActiveContentMap) {
          oActiveContentMap.languageComparisonModeOn = false;
          oActiveContentMap.languageCodeToCompare = "";
        }
        ContentScreenProps.tableViewProps.resetContextsByParentId(sContentId);
        ContentScreenProps.relationshipView.setRelationshipToolbarProps({});
        oScreenProps.setActiveContent({});
        SharableURLStore.removeWindowURL();
        ContentScreenProps.goldenRecordProps.resetGoldenRecordComparisonProps();
        if (oScreenProps.getIsEditMode()) {
          oScreenProps.setIsEditMode(false);
          let sDefaultViewMode = ContentUtils.getDefaultViewMode();
          ContentUtils.setViewMode(sDefaultViewMode);
        }

        //removing selected template
        oScreenProps.setSelectedTemplate({});
        oScreenProps.setActiveSections([]);

        UOMProps.reset();
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION:
        oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE,
        };
        oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.resetFilterPropsByContext();
        ContentScreenProps.informationTabProps.setIsRuleViolatedContentsScreen(false);
        ContentScreenProps.damInformationTabProps.setIsRuleViolatedContentsScreen(false);
        oScreenProps.setViewMode(oScreenProps.getDefaultViewMode());
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST:
        ContentScreenProps.collectionViewProps.setAddEntityInCollectionViewStatus(false);
        oFilterContext = {
          filterType: oFilterPropType.QUICKLIST,
          screenContext: oFilterPropType.QUICKLIST,
        };
        _resetStaticCollectionQuickListProps(oFilterContext);
        break;
    }
  };

  let _handleContentComparisonMatchAndMergeViewTableRowClicked = function (sContext, sPropertyId, sTableId) {
    switch (sContext) {
      case GRConstants.GOLDEN_RECORD_COMPARISON:
        GoldenRecordStore.handleGoldenRecordMatchAndMergeViewTableRowClicked(sPropertyId, sTableId);
        break;
    }
  };

  let _handleContentComparisonMatchAndMergePropertyValueChanged = function (sContext, sPropertyId, sTableId, oValue) {
    switch (sContext) {
      case GRConstants.GOLDEN_RECORD_COMPARISON:
        GoldenRecordStore.handleContentComparisonMatchAndMergePropertyValueChanged(sPropertyId, sTableId, oValue);
        break;
    }
    _triggerChange();
  };

  let _handleStepperViewActiveStepChanged = function (sContext, sButtonId) {
    switch (sContext) {
      case "contentComparison":
        GoldenRecordStore.handleStepperViewActiveStepChanged(sButtonId);
        break;
    }
  };

  let _handleContentComparisonMnmRelationshipPaginationChanged = function (sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId) {
    GoldenRecordStore.handleMnmRelationshipPaginationChanged(sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId);
  };

  let _handleContentComparisonMnmRemoveRelationshipButtonClicked = function (sPropertyId, sTableId, oPropertyToSave) {
    GoldenRecordStore.handleContentComparisonMnmRemoveRelationshipButtonClicked(sPropertyId, sTableId, oPropertyToSave);
  };

  let _saveTimeLineComparisonDialogButtonClicked = function (oFilterContext) {
    let sSelectedHeaderButtonId = ContentScreenProps.timelineProps.getSelectedHeaderButtonId();
    TimelineStore.postProcessVersionMerge();
    let oPromise = null;
    let oCallbackData = {};
    let oActiveContent = ContentUtils.getActiveEntity();
    let sArticleId = oActiveContent.id;
    let sSelectedTabId = ContentUtils.getSelectedTabId(sArticleId);
    if(sSelectedHeaderButtonId === "timeline_comparison_properties") {
      let sSelectedDataLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
      let oTimelineProps = ContentScreenProps.timelineProps;
      let bIsComparisonMode = oTimelineProps.getIsComparisonMode();
      if (sSelectedTabId === TemplateTabTypeConstants.TIMELINE_TAB && bIsComparisonMode) {
        sSelectedDataLanguage = oTimelineProps.getSelectedLanguageForComparison();
      }
      oCallbackData = {dataLanguage: sSelectedDataLanguage, filterContext: oFilterContext};
      oPromise = _saveContent(oCallbackData);
    } else if(sSelectedHeaderButtonId === "timeline_comparison_relationship") {
      let oActiveContent = ContentUtils.getActiveContent();
      let oContentToSave = oActiveContent.contentClone;
      let oRelationshipADMData = {};
      ContentRelationshipStore.generateADMForRelationships(oActiveContent.contentRelationships, oContentToSave.contentRelationships, false, oRelationshipADMData);
      ContentRelationshipStore.generateADMForRelationships(oActiveContent.natureRelationships, oContentToSave.natureRelationships, true, oRelationshipADMData);
      oPromise = ContentRelationshipStore.saveRelationshipCall("", {}, oRelationshipADMData);
    }

    oPromise.then(TimelineStore.setUpComparisonModeTimeLineData.bind(this, oCallbackData));
  };

  let _handleTimelineComparisionDialogOKButtonClicked = function (oFilterContext) {
    if(ContentUtils.isActiveContentDirty(oFilterContext)) {
      ContentUtils.showMessage(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return;
    }
    ContentScreenProps.timelineProps.setSelectedVersionIds([]);
    TimelineStore.handleTimelineCompareVersionButtonClicked(false);
    ContentStore.handleEntityTabClicked("timeline_tab");
  };

  let _handleRelationshipConflictPropertyClicked = function(oConflictingValue, oRelationship) {
    let oActiveContent = ContentScreenProps.screen.getActiveContent();
    let aContentRelationships = [];
    let oActiveRelationship = null;
    let oReferencedElements = ContentScreenProps.screen.getReferencedElements();
    let oReferencedElement = oReferencedElements[oRelationship.sideId] || oReferencedElements[oRelationship.id];
    let sRelationshipType = oReferencedElement.type;
    aContentRelationships = oActiveContent.contentRelationships;
    oActiveRelationship = CS.find(aContentRelationships, {relationshipId: oRelationship.relationshipId, sideId: oRelationship.sideId})

    if (oActiveRelationship) {
      CS.forEach(oActiveRelationship.conflictingValues, oConflictingValue => {
        oConflictingValue.isChecked = false;
      })
      oConflictingValue.isChecked = true;
      _triggerChange();
    }
  }

  let _handleRelationshipConflictPropertyResolved = function(oSelectedConflictValue, oRelationship) {
    ContentStore.handleRelationshipConflictPropertyResolved(oSelectedConflictValue, oRelationship);
  };

  /** To set a default view mode as a view mode */
  let _setViewMode = () => {
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setViewMode(oScreenProps.getDefaultViewMode());
  };

  let _handleTaxonomyInheritanceConflictIconClicked = function () {
    var oActiveEntity = ContentUtils.getActiveEntity();
    let oTaxonomyConflict =oActiveEntity.taxonomyConflictingValues[0];
    let oConflicts = oTaxonomyConflict.conflicts[0];

    let oReqData = {
      sourceContentId: oConflicts.sourceContentId || "",
      sourceContentBaseType: oConflicts.sourceContentBaseType || "",
      relationshipId: oConflicts.relationshipId || "",
      relationshipSideId: oConflicts.relationshipSideId || "",
      contentId: oActiveEntity.id || "",
      contentBaseType: oActiveEntity.baseType || "",
      taxonomyInheritanceSetting : oTaxonomyConflict.taxonomyInheritanceSetting
    }
    NewMultiClassificationStore.openTaxonomyInheritanceDialog(oReqData);
  };

  /**
   * @function handleFilterButtonClicked
   * @param bForClearAdvancedSearch
   * @param oCallBack - We get filter context in oCallBack
   * @param sSelectedHierarchyContext
   * @returns {*}
   */
  let _handleFilterButtonClicked = function(oCallBack, sSelectedHierarchyContext) {
    let oFilterContext = oCallBack.filterContext;
    var bAvailableEntityView = ContentUtils.getAvailableEntityViewStatus();
    var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);

    oFilterStore.resetPaginationOnApplyingFilter();
    if (CS.isEmpty(sSelectedHierarchyContext)) {
      sSelectedHierarchyContext = ContentUtils.getSelectedScreenContextForQuickList();
    }

    let oFilterProps = oFilterStore.getFilterProps(oFilterContext);
    if (bAvailableEntityView || bIsAnyHierarchySelectedExceptFilterHierarchy) {
      return AvailableEntityStore.handleSearchApplyClicked({filterContext: oFilterContext, isSearchApplyClicked: true}, {}, sSelectedHierarchyContext);
    } else {
      oComponentProps.screen.applyFilterProps();
      oComponentProps.screen.getContentFilterProps().isActive = oComponentProps.screen.isContentFilterPropsActive();
      if (oFilterProps.getIsFilterDirty()) {
        if (ContentUtils.getAvailableEntityViewStatus()) {
          return ContentStore.handleAvailableEntityFilterButtonClicked({filterContext: oFilterContext});
        } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
          return GoldenRecordStore.handleFilterButtonClicked(oFilterContext);
        } else {
          return ContentStore.handleFilterButtonClicked(oCallBack);
        }
      }
    }
    return Promise.resolve();
  };

  let _handleGridViewResizerButtonClicked = function (sColumnWidth, sColumnId, sTableContextId) {
    let oSkeleton;
    let oResizedColumn;
    let bIsGoldenRecordMatchAndMergeMode = ContentScreenProps.goldenRecordProps.getIsFullScreenMode();
    let sUpdatedColumnWidth = (sColumnWidth > 70) ? sColumnWidth : "70";
    if(CS.isNotEmpty(sTableContextId)) {
      let oUnitTableProps = VariantStore.getTableViewPropsByContext(sTableContextId);
      oSkeleton = oUnitTableProps.getGridSkeleton();
      oResizedColumn = CS.find(oSkeleton.scrollableColumns, {id: sColumnId});
      oResizedColumn.width = +sUpdatedColumnWidth;
      oUnitTableProps.setGridSkeleton(oSkeleton);
      oUnitTableProps.setResizedColumnId(sColumnId);
    }else if(bIsGoldenRecordMatchAndMergeMode) {
      oSkeleton = GoldenRecordProps.getGoldenRecordComparisionSkeleton();
      oResizedColumn = CS.find(oSkeleton.scrollableColumns, {id: sColumnId});
      oResizedColumn.width = +sUpdatedColumnWidth;
      GoldenRecordProps.setGoldenRecordComparisionSkeleton(oSkeleton);
    }else{
      oSkeleton = oComponentProps.contentGridProps.getGridViewSkeleton();
      oResizedColumn = CS.find(oSkeleton.scrollableColumns, {id: sColumnId});
      oResizedColumn.width = +sUpdatedColumnWidth;
      oComponentProps.contentGridProps.setGridViewSkeleton(oSkeleton);
      oComponentProps.contentGridProps.setResizedColumnId(sColumnId);
    }
    _triggerChange();
  };

  let _handleGridOrganizeColumnButtonClicked = function (sContextId) {
    let oProps = ColumnOrganizerProps;
    if(CS.isNotEmpty(sContextId)) {
      let sKlassInstanceId = ContentUtils.getKlassInstanceId();
      let sIdForProps = ContentUtils.getIdForTableViewProps(sContextId);
      oProps = oComponentProps.tableViewProps.getColumnOrganizerPropsByContext(sIdForProps, sKlassInstanceId);
    }
    oProps.setIsDialogOpen(!oProps.getIsDialogOpen());
  };

  let handleLinkAssetToContents = function (sContext, sSideId, oResponse) {
    let oActiveEntity = ContentUtils.getActiveContent();
    let aSuccess = oResponse.successIIds;
    let sEntityRelationshipsKey = "contentRelationships";
    let oCurrentRelationship = CS.find(oActiveEntity[sEntityRelationshipsKey], {sideId: sSideId});
    let oReferencedElements = oComponentProps.screen.getReferencedElements();
    let oReferencedRelationshipElement = oReferencedElements[sSideId];
    let sRelationshipId = oReferencedRelationshipElement.propertyId;
    let bIsNature = false;
    let oNewRelationship = CS.cloneDeep(oCurrentRelationship);
    let oModifiedRelationshipElements = oComponentProps.relationshipView.getModifiedRelationshipElements();
    let aAddedElementsInRelationship = [];
    oModifiedRelationshipElements.id = sRelationshipId;
    oModifiedRelationshipElements.added = aAddedElementsInRelationship;
    oModifiedRelationshipElements.sideId = sSideId;

    if (!CS.isEmpty(oNewRelationship)) {
      oNewRelationship.isDirty = true;
      let aContentElements = oNewRelationship.elementIds;
      CS.forEach(aSuccess, function (iId) {
        aContentElements.push(iId.toString());
      });
      oComponentProps.availableEntityViewProps.setSelectedEntities([]);
      ContentRelationshipStore.saveRelationship([oCurrentRelationship], [oNewRelationship], bIsNature);
    }
  };

  let _handleColumnOrganizerDialogButtonClicked = function (sButtonId, sContext, aColumns) {
    switch (sContext) {
      case "contentGridEdit":
        ContentGridStore.handleColumnOrganizerDialogButtonClicked(sButtonId, aColumns);
    }
    //Handled for context column organizer view
    if(CS.isEmpty(sContext)) {
      _triggerChange();
    }
  };

  let _makeAppliedTaxonomyDirtyForBookmark = function (oFilterContext) {
    let CollectionViewProps = ContentScreenProps.collectionViewProps;
    let oActiveCollection = CollectionViewProps.getActiveCollection();

    let oAppliedTaxonomyCloneData = CollectionViewProps.getAppliedTaxonomyCloneData();
    if (CS.isNotEmpty(oActiveCollection) && oActiveCollection.type === "dynamicCollection" && (CS.isEmpty(oAppliedTaxonomyCloneData) || !oAppliedTaxonomyCloneData.hasOwnProperty("taxonomyTreeFlatMap"))) {
      let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
      oAppliedTaxonomyCloneData.ReferencedClasses = CS.cloneDeep(oFilterProps.getReferencedClasses());
      oAppliedTaxonomyCloneData.ReferencedTaxonomies = CS.cloneDeep(oFilterProps.getReferencedTaxonomies());
      CollectionViewProps.setAppliedTaxonomyCloneData(oAppliedTaxonomyCloneData);
    }
  };

  let _checkValidationForVariantContext = function () {
    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let oSelectedVisibleContext = oRelationshipContextData.context;
    let oDummyVariant = oRelationshipContextData;
    return ContentUtils.validateVariantContextSelection(oDummyVariant, oSelectedVisibleContext);
  };

  let _handleTaxonomyHideEmptyToggleClicked = (bHideEmpty, oFilterContext) => {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    oFilterProps.setHideEmpty(bHideEmpty);
    oFilterProps.setAllAffectedTreeNodeIds([]);
    _triggerChange();
  };

  let _handleChipsEditButtonClicked = (sId) => {
    MultiClassificationProps.reset();
    MultiClassificationProps.setReferencedTaxonomyMap(CS.cloneDeep(ContentScreenProps.screen.getReferencedTaxonomies()));
    NewMinorTaxonomyStore.openMultiClassificationDialog(MultiClassificationViewTypesConstants.TAXONOMIES ,sId,
        {isForEmbedded: true});
  };

  let _handleGridPropertyViewClassificationDialogButtonClicked = (sButtonId, sContext, sEmbeddedContentId, sPropertyId, sTableContextId) => {

    let oActiveEmbProperty = ContentScreenProps.screen.getActiveEmbProperty();
    switch (sButtonId) {
      case "apply":
        let oMultiClassificationData = MultiClassificationProps.getMultiClassificationData();
        let oReferencedTaxonomyMap = MultiClassificationProps.getReferencedTaxonomyMap();
        oMultiClassificationData = oMultiClassificationData.clonedObject;
        let aSelectedOptions = [];
        let oModel = oActiveEmbProperty.model;
        CS.forEach(oMultiClassificationData.taxonomies, (aTaxonomies) => {
          let sId = aTaxonomies[aTaxonomies.length - 1].id;
          aSelectedOptions.push(
              {
                id: sId
              }
          );
          oModel.referencedData[sId] = oReferencedTaxonomyMap[sId];
        });
        let oMinorTaxonomiesData = oModel.multiClassificationViewData;
        oMinorTaxonomiesData.showClassificationDialog = false;
        MultiClassificationProps.reset();
        ContentScreenProps.screen.setActiveEmbProperty({});

        let oCellData = {
          columnId: sPropertyId,
          rowId: sEmbeddedContentId,
          expressionId: "",
          value: aSelectedOptions
        };
        _handleTableCellValueChanged(oCellData, "", sTableContextId);
        break;

      case "cancel":
        let oMultiClassificationViewData = oActiveEmbProperty.model.multiClassificationViewData;
        NewMinorTaxonomyStore.resetMultiClassificationProps();
        oMultiClassificationViewData.showClassificationDialog = false;
        _triggerChange();
    }
  };

  let _handleMenuListToggleButtonClicked = () => {
    let oActiveEntity = ContentUtils.getActiveEntity();
    let bIsLandingPage = CS.isEmpty(oActiveEntity);
    HomeScreenCommunicator.handleSidePanelToggleClicked(bIsLandingPage);
  };

  //*********************** Public API's ***********************//

  return {

    getData: function () {
      return {
        appData: ContentUtils.getAppData(),
        componentProps: ContentUtils.getComponentProps()
      }
    },

    logoClicked: function (sMode) {
          this.refreshAndResetAll(sMode);
      },

    refreshAndResetAll: function (sMode) {
      this.resetAll();
      CommonUtils.setHistoryStateToNull(this.refreshAll.bind(this, sMode));
      SharableURLStore.removeWindowURL();
    },

    handleContextMenuButtonClicked: function (sContext) {
      ContentStore.handleContextMenuButtonClicked(sContext);
    },

    handleContextMenuListItemClicked: function (sSelectedId, sContext, oFilterContext) {
      ContentStore.handleContextMenuListItemClicked(sSelectedId, sContext, oFilterContext);
    },

    handleHandleTransferDialogClicked: function (sContext , sButtonId) {
      ContentStore.handleHandleTransferDialogClicked(sContext , sButtonId);
    },

    handleHandleTransferDialogItemClicked: function (sContext, sSelectedId) {
      ContentStore.handleHandleTransferDialogItemClicked(sContext, sSelectedId);
    },

    handleTransferDialogCheckBoxToggled: function (sContext) {
      ContentStore.handleTransferDialogCheckBoxToggled(sContext);
    },

    handleKpiSummaryViewKpiSelected: function (sKpiId) {
      ContentStore.handleKpiSummaryViewKpiSelected(sKpiId);
    },

    handleContextMenuListVisibilityToggled: function (bIsVisibility, sContext) {
      ContentStore.handleContextMenuListVisibilityToggled(bIsVisibility, sContext);
    },

    handleFileAttachmentUploadClicked: function(sContext, aFiles , oCallbackData){
      var oFiles = {
        files : aFiles
      };
      return ImageCoverflowStore.uploadFileAttachment("assetBulkUpload", oFiles, oCallbackData);
    },

    toggleThumbnailSelection: function (oModel) {
      trackMe('toggleThumbnailSelection');
      /**Use Context here instead of multiple flags**/
      let sViewContext = oModel.properties['viewContext'] || "";
      let sSplitter = ContentUtils.getSplitter();
      let aSplitContext = CS.split(sViewContext, sSplitter);
      let sMainContext = aSplitContext[0];

      switch (sMainContext) {
        case ContentScreenViewContextConstants.NATURE_RELATIONSHIP:
        case ContentScreenViewContextConstants.RELATIONSHIP:
          ContentRelationshipStore.toggleRelationshipThumbnailSelection(oModel);
          break;
        default:
          _toggleThumbnailSelection(oModel);
      }
    },

    handleThumbnailDeleteIconClicked: function (oModel, oFilterContext) {
      trackMe('toggleThumbnailSelection');
      /**Use Context here instead of multiple flags**/
      let sViewContext = oModel.properties['viewContext'] || "";
      let aSplitContext = CS.split(sViewContext, ContentUtils.getSplitter());
      let sMainContext = aSplitContext[0];

      switch (sMainContext) {
        case ContentScreenViewContextConstants.NATURE_RELATIONSHIP:
        case ContentScreenViewContextConstants.RELATIONSHIP:
          this.deleteRelationshipElement(oModel);
          break;
        default:
          _handleThumbnailDeleteIconClicked(oModel, oFilterContext);
          break;

      }
    },

    handleThumbnailCloneIconClicked: function (oModel, oFilterContext) {
      ContentUtils.listModeConfirmation(getTranslation().ALERT_CONFIRM_CREATE_CLONE, [oModel.label],
        function () {
          CloneWizardStore.handleCreateCloneButtonClicked("cloneSingleContent", oModel, oFilterContext);
        }, function (oEvent) {
        });
    },

    deleteRelationshipElement: function (oModel, oFilterContext) {
      let oContent = ContentUtils.getActiveContent();
      if(oContent.contentClone) {
        alertify.error(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return true;
      }
      ContentRelationshipStore.deleteRelationshipElement(oModel, {filterContext: oFilterContext});
    },

    handleEditViewFilterOptionChanged: function (sId) {
      ContentStore.handleEditViewFilterOptionChanged(sId);
    },

    handleAcrolinxResultChecked: function (sScoreValue, sScoreCardUrl) {
      ContentStore.handleAcrolinxResultChecked(sScoreValue, sScoreCardUrl);
    },

    handleCreateContentButtonClicked: function () {
      trackMe('handleCreateContentButtonClicked');
      ContentStore.fetchMasterClassesAndCreateNewContent();
    },

    handleAssetBulkDownloadButtonClicked: function () {
      trackMe('handleAssetBulkDownloadButtonClicked');
      ContentStore.handleAssetBulkDownloadButtonClicked("", AssetDownloadViewDictionary.BULK_ASSET_DOWNLOAD_VIEW);
    },

    handleAssetBulkShareButtonClicked: function () {
      ContentStore.handleAssetBulkDownloadButtonClicked("", AssetDownloadViewDictionary.BULK_LINK_SHARING_VIEW);
    },

    handleContentLeftSideShareOrDownloadButtonClicked: function (sContext, sButtonContext) {
      ContentStore.handleAssetBulkDownloadButtonClicked(sContext, sButtonContext);
    },

    handleAddAllButtonClicked: function (oFilterContext) {
      trackMe('handleAddAllButtonClicked');
      var oComponentProps = ContentUtils.getComponentProps();
      var oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();
      let sAvaiableEntityViewContext = ContentScreenProps.availableEntityViewProps.getAvailableEntityViewContext();
      var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
      var bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
      var bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
      var aSelectedEntities = oComponentProps.availableEntityViewProps.getSelectedEntities();
      var aSelectedObjects = (!CS.isEmpty(aSelectedEntities)) ? aSelectedEntities : [];
      var oActiveEntity = ContentUtils.getActiveEntity();

      if(oActiveCollection && oActiveCollection.type == "staticCollection" && CS.isEmpty(oActiveEntity)){
        let oCallbackData = {};
        let oContext = {selectedContext : "staticCollection", filterContext: oFilterContext};
        oCallbackData.functionToExecute = function () {
          ContentScreenProps.collectionViewProps.setAddEntityInCollectionViewStatus(true);
          AvailableEntityStore.fetchAvailableEntities(oContext);
        };

        /**
         * In CollectionQuickList to get present entities(right side from quickList) "COLLECTION" filterContext is required.
         */
        let oNewFilterContextForCollection = {
          filterType: oFilterPropType.COLLECTION,
          screenContext: oActiveCollection.id
        };

        oCallbackData.filterContext = oNewFilterContextForCollection;
        NewCollectionStore.addNewEntityInActiveStaticCollection(oCallbackData, aSelectedObjects);
      }else if(bAvailableEntityChildVariantViewVisibilityStatus){
        _dropLinkedInstanceIntoVariantUnderCreation(aSelectedObjects, oFilterContext);
      }else if(bAvailableEntityParentVariantViewVisibilityStatus){
        _dropLinkedInstanceIntoCreatedVariant(aSelectedObjects, oFilterContext);
      } else if (sAvaiableEntityViewContext == ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW) {
        VariantStore.handleVariantAddLinkedInstances();
        AvailableEntityStore.fetchAvailableEntities({ filterContext: oFilterContext }, {});
      } else {
        _handleContentEntityDropInRelationshipSection({}, "", oFilterContext);
      }
    },

    handleRefreshContentButtonClicked: function(oFilterContext){
      _handleRefreshContentButtonClicked(oFilterContext);
    },

    handleContentAssetBulkUploadButtonClicked: function(sId, oFiles, aCollectionIds, sKlassId, doNotFetchContentList, oFilterContext){
        _handleContentAssetBulkUploadButtonClicked(sId, oFiles, aCollectionIds, sKlassId, doNotFetchContentList, oFilterContext);
    },

    handleBulkUploadToCollectionClicked: function(){
      trackMe('handleBulkUploadToCollectionClicked');
      ContentScreenProps.screen.setBulkUploadCollectionView(true);
      let oCallback = {};
      oCallback.doNotSetBreadCrumb = true;
      ContentStore.getAllAssetExtensions();
      NewCollectionStore.getStaticCollectionsList(oCallback);
    },

    handleBulkAssetUploadFromRelationshipClicked: function (sContext, oFilterContext) {
      if (ContentUtils.isActiveContentDirty()) {
        ContentUtils.showMessage(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return;
      }
      ContentScreenProps.relationshipView.setContext(sContext);
      ContentScreenProps.relationshipView.setIsBulkUploadDialogOpen(true);
      ContentStore.getAllAssetExtensions();
      ContentStore.fetchAssetClassList(oFilterContext);
    },

    handleBulkUploadToCollectionClosed: function(){
      trackMe('handleBulkUploadToCollectionClosed');
      ContentScreenProps.screen.setBulkUploadCollectionView(false);
      _triggerChange();
    },

    handleOnboardingFileUploaded: function (aFiles, oExtraDataForUpload) {
      let oCallback = {};
      if (oExtraDataForUpload && oExtraDataForUpload.endpointId && oExtraDataForUpload.endpointType) {
        SessionProps.setSessionPhysicalCatalogData(PhysicalCatalogDictionary.DATAINTEGRATION);
        SessionProps.setSessionEndpointId(oExtraDataForUpload.endpointId);
        SessionProps.setSessionEndpointType(oExtraDataForUpload.endpointType);
        oCallback.functionToExecute = (bIsForSave) => {
          if (bIsForSave) {
            SessionProps.setSessionEndpointId("");
            SessionProps.setSessionEndpointType("");
          }
          SessionProps.setSessionPhysicalCatalogData(PhysicalCatalogDictionary.PIM);
        };
      }
      OnboardingStore.handleOnboardingFileUploaded(aFiles, oCallback);
    },

    handleOnboardingFileUploadCancel: function(){
      let oScreenProps = ContentScreenProps.screen;
      oScreenProps.setImportEndpointSelected(false);
    },

    handleComparisonButtonClicked: function () {
      ContentStore.handleComparisonButtonClicked();
    },

    handleLanguageForComparisonChanged: function (sLanguageForComparison, oFilterContext) {
      let oScreenProps = ContentScreenProps.screen;
      if (oScreenProps.getIsContentComparisonMode()) {
        ContentStore.handleLanguageForComparisonChanged(sLanguageForComparison);
      } else if (oComponentProps.goldenRecordProps.getIsGoldenRecordViewSourcesDialogOpen()) {
        GoldenRecordStore.handleGoldenRecordComparisonLanguageChanged(sLanguageForComparison);
      }else if (oScreenProps.getIsEditMode()) {
        TimelineStore.handleLanguageForComparisonChanged(sLanguageForComparison, oFilterContext);
      }
    },

    handleSaveContentsButtonClicked: function (oFilterContext) {
      _saveContent({ filterContext: oFilterContext });
    },

    handleDiscardUnSavedContentsButtonClicked: function () {
      _handleDiscardUnSavedContentsButtonClicked();
    },

    handleDeleteContentsButtonClicked: function () {
      ContentStore.handleDeleteArticleTilesButtonClicked();
    },

    handleRemoveContentsButtonClicked: function (oModel = {}, oFilterContext) {
      let sViewContext = oModel.properties ? oModel.properties['viewContext'] : "";
      let aSplitContext = CS.split(sViewContext, ContentUtils.getSplitter());
      let sMainContext = aSplitContext[0];
      let oScreenProps = oComponentProps.screen;
      let bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();

      switch (sMainContext) {
        case ContentScreenViewContextConstants.NATURE_RELATIONSHIP:
        case ContentScreenViewContextConstants.RELATIONSHIP:
          this.deleteRelationshipElement(oModel);
          break;

        default:
          if (bIsTaxonomyHierarchySelected) {
            ContentUtils.listModeConfirmation(
                getTranslation().STORE_ALERT_CONFIRM_UNLINK,
                [oModel.label],
                function () {
                  _removeEntityFromTaxonomy(oModel, oFilterContext);
                },
                function (oEvent) {
                })
          } else {
            let aRemovedEntityIds = oModel.id ? [oModel.id] : ContentUtils.getSelectedContentIds();
            NewCollectionStore.removeEntityFromCollection({filterContext: oFilterContext}, aRemovedEntityIds);
            break;
          }
      }
    },

    handleDeleteAllRelationshipEntityClicked: function (sRelationshipId,sViewContext) {
      ContentRelationshipStore.handleDeleteAllRelationshipEntityClicked(sRelationshipId,sViewContext, {});
    },

    handleDeleteAllVariantInstancesClicked: function (oVariant, oFilterContext) {
      _handleDeleteAllVariantInstancesClicked(oVariant, oFilterContext);
    },

    handleThumbnailOpenClicked: function (oModel, oFilterContext) {
      let oSelectedModule = GlobalStore.getSelectedModule();
      let sCurrentModuleId = !CS.isEmpty(oSelectedModule) && oSelectedModule.id || "";
      switch (sCurrentModuleId) {
        case ModulesDictionary.FILES:
          this.toggleThumbnailSelection(oModel);
          break;
        default:
          let oActiveEntity = ContentUtils.getActiveEntity();
          let oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();
          if ((oActiveEntity && oActiveEntity.contentClone) || oActiveCollection.isDirty) {
            ContentUtils.setShakingStatus(true);
            _triggerChange();
            return;
          } else {
            _handleThumbnailDoubleClicked(oModel, oFilterContext);
          }
      }
    },

    handleCoverflowDetailViewClosedButtonClicked: function(sId, sContext) {
      trackMe('handleCoverflowDetailViewClosedButtonClicked');
      ImageCoverflowStore.handleCoverflowDetailViewClosedButtonClicked(sId, sContext);
      let sSelectedTabId = ContentUtils.getSelectedTabId();
      if(sSelectedTabId === TemplateTabsDictionary.TASKS_TAB) {
        ContentStore.handleEntityTabClicked(TemplateTabsDictionary.TASKS_TAB);
      }else {
        _triggerChange();
      }
    },

    handleCoverflowDetailViewShowAnnotationButtonClicked: function (oImageAttribute) {
      let bShowAnnotation = ContentScreenProps.taskProps.getShowAnnotations();
      ContentScreenProps.taskProps.setShowAnnotations(!bShowAnnotation);

      if(!bShowAnnotation) {
        ContentStore.fetchAllTasksForAnnotations(oImageAttribute);
      } else {
        _triggerChange();
      }
    },

    setCoverflowImageAsDefault: function(oModel, iImageCoverflowItemIndex, sId) {
      trackMe('setCoverflowImageAsDefault');
      logger.info('setCoverflowImageAsDefault: setting coverflow image as default',
                  {'imageModel': oModel, 'imageIndex': iImageCoverflowItemIndex});
      ImageCoverflowStore.setCoverflowImageAsDefault(oModel, iImageCoverflowItemIndex, sId);

      _triggerChange();
    },

    handleOpenCoverflowViewerButtonClicked: function (oModel, iImageCoverflowItemIndex, sId, sContext) {
      trackMe('handleOpenCoverflowViewerButtonClicked');
      if (CS.isNotEmpty(oModel)) {
        ImageCoverflowStore.handleOpenCoverflowViewerButtonClicked(oModel, iImageCoverflowItemIndex, sId, sContext);
      }
      _triggerChange();
    },

    handleAssetUploadOrReplaceClicked: function (oRefDom, bIsAssetReplaced) {
      let oActiveContentClass = ContentUtils.getActiveContentClass();
      bIsAssetReplaced = bIsAssetReplaced && !CS.isNull(ContentUtils.getActiveContent().assetInformation);
      if (oActiveContentClass.trackDownloads && bIsAssetReplaced) {
        let aCustomButtonData = [
          {
            id: "no",
            label: getTranslation().NO,
            isFlat: true,
            handler: function () {}
          },
          {
            id: "yes",
            label: getTranslation().YES,
            isFlat: false,
            handler: this.handleGetAllAssetExtensions.bind(this, oRefDom)
          }
        ];
        CustomActionDialogStore.showCustomConfirmDialog("", getTranslation().STORE_ALERT_CONFIRM_RESET_DOWNLOAD_COUNT, aCustomButtonData);
      } else {
        this.handleGetAllAssetExtensions(oRefDom)
      }
    },

    handleGetAllAssetExtensions: async function (oRefDom, sContext) {
      await ContentStore.getAllAssetExtensions();
      var oScreenProps = oComponentProps.screen;
      let sActiveContentNatureType = oScreenProps.getActiveContentClass().natureType;
      let oAssetExtensions = oScreenProps.getAssetExtensions();
      let aAllowedTypes = sContext !== "allAssets" && oAssetExtensions[sActiveContentNatureType] ? oAssetExtensions[sActiveContentNatureType] : oAssetExtensions.allAssets;

      oRefDom.accept =  aAllowedTypes.join(", ");
      oRefDom.click();
    },

    handleLinkedSectionButtonClicked: function (oItem) {
      ContentStore.handleLinkedSectionButtonClicked(oItem);
    },

    handleCoverflowDetailViewAssetItemClicked: function (oModel, assetObjectKey, sId) {
      trackMe('handleCoverflowDetailViewAssetItemClicked');
      ImageCoverflowStore.handleCoverflowDetailViewAssetItemClicked(oModel, assetObjectKey, sId);

      _triggerChange();
    },

    handleCoverflowDetailViewDataChanged: function (oModel, sContext, sId, assetObjectKey, sNewValue) {
      trackMe('handleCoverflowDetailViewDataChanged');
      ImageCoverflowStore.handleCoverflowDetailViewDataChanged(oModel, sContext, sId, assetObjectKey, sNewValue);

      _triggerChange();
    },

    handleContentTileScrolled: function (sContext, bIsForPresentEntity, oPaginationData, sSelectedContext="", oFilterContext) {
      trackMe('handleContentTileScrolled');

      var oScreenProps = oComponentProps.screen;
      var bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();
      var bIsCollectionHierarchySelected = oScreenProps.getIsCollectionHierarchySelected();
      var bAvailableEntityScreenStatus = ContentUtils.getAvailableEntityViewStatus();
      let oActiveContent = oScreenProps.getActiveContent();
      if ((bAvailableEntityScreenStatus && !bIsForPresentEntity) || bIsTaxonomyHierarchySelected
          || bIsCollectionHierarchySelected) {
        if (CS.isEmpty(oActiveContent)) {
          sSelectedContext = bIsTaxonomyHierarchySelected || bIsCollectionHierarchySelected ? ContentUtils.getSelectedHierarchyContext() : sSelectedContext;
        }
        AvailableEntityStore.handleSearchedEntityLoadMoreClicked(oPaginationData, {selectedContext: sSelectedContext, filterContext: oFilterContext});
        return;
      }
      let oCallbackData = {
        filterContext: oFilterContext
      };
      let oExtraData = {
        postData: {
          size: oPaginationData.pageSize,
          from: oPaginationData.from
        }
      };
      ContentStore.processDataForFetchContentList(oCallbackData, oExtraData);
    },

    handleContentTilePaginationChanged: function (oPaginationData, oFilterContext) {
      if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
        GoldenRecordStore.handleGoldenRecordBucketsPaginationChanged(oPaginationData, oFilterContext);
      } else if (oFilterContext.screenContext === ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS) {
        ContentStore.fetchTabSpecificContentsList(oFilterContext.screenContext, oPaginationData);
      } else {
        let sSelectedContext = "";
        if(oComponentProps.variantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus()) {
          sSelectedContext = "contextEntity";
        } else if (oComponentProps.collectionViewProps.getAddEntityInCollectionViewStatus()) {
          sSelectedContext = "staticCollection";
        }
        this.handleContentTileScrolled("", false, oPaginationData, sSelectedContext, oFilterContext);
      }
    },

    handleRelationshipLoadMore: function (sId, sNavigationContext, oPaginationData) {
      trackMe('handleRelationshipLoadMore');
      ContentRelationshipStore.loadMoreRelationship(sId, sNavigationContext, oPaginationData);
    },

    handleBundleSectionPaginationChanged: function (sSideId,sRelationshipId, oPaginationData ) {
      let sId =  sSideId;
      ContentRelationshipStore.loadMoreRelationship(sId, "", oPaginationData);
    },

    handleThumbnailScrollerLoadMore: function (sId, sNavigationContext) {
      trackMe('handleThumbnailScrollerLoadMore');
      // TODO:
      ContentRelationshipStore.loadMoreRelationship(sId, sNavigationContext);
    },

    handleSwitchRelationshipViewMode: function (sRelationshipId, sViewMode) {
      trackMe('handleSwitchRelationshipViewMode');
      var oScreenProps = oComponentProps.screen;
      if(oScreenProps.getRelationshipViewMode() == sViewMode) {
        return;
      }
      if(sViewMode == ContentScreenConstants.viewModes.TILE_MODE) {
          oScreenProps.setRelationshipViewMode(sRelationshipId, sViewMode);
      } else if(sViewMode == ContentScreenConstants.viewModes.LIST_MODE) {
        oScreenProps.setRelationshipViewMode(sRelationshipId, sViewMode);
      }
      _triggerChange();
    },

    handleTagNodeClicked: function (oModel) {
      trackMe('handleTagNodeClicked');
      var oTagValues = {};
      oTagValues.id = oModel.id;
      oTagValues.name = oModel.label;
      oTagValues.relevance = oModel.properties['iNewValue'];
      GlobalStore.setTagValue(oModel.id, oTagValues);

      var sItemSelectionMode = oComponentProps.screen.getItemSelectionMode().toLowerCase();

      logger.info('handleTagNodeClicked: Populate tag in selected ' + sItemSelectionMode,
          {'tag': oModel});

      _populateTagsInEntity(sItemSelectionMode);

      _triggerChange();
    },

    handleTagGroupTagValueChanged: function(sTagId, aTagValueRelevanceData, oExtraData){

      let oFilterContext = oExtraData.filterContext;

      switch (oExtraData.outerContext) {
        case "content":
        case "variant":
        case "contentTag":
          ContentStore.updateContentTagValues(aTagValueRelevanceData, sTagId);
          break;

        case "contentFilterTagsInner":
          ContentStore.handleContentFilterInnerTagMSSApplyClicked(sTagId, aTagValueRelevanceData, oFilterContext);
          break;

        case HierarchyTypesDictionary.TAXONOMY_HIERARCHY:
          let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
          let aSections = oFilterStore.makeTaxonomySectionDirty();
          ContentUtils.updateEntitySectionDefaultTagValue(aSections, oExtraData.sectionId, oExtraData.elementId, sTagId, aTagValueRelevanceData);
          break;

        case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
          let oActiveHierarchyCollection = CollectionAndTaxonomyHierarchyStore.makeActiveHierarchyCollectionDirty();
          ContentUtils.updateEntitySectionDefaultTagValue(oActiveHierarchyCollection.sections, oExtraData.sectionId, oExtraData.elementId, sTagId, aTagValueRelevanceData);
          break;

        case "appliedTagElement": //bulkEdit
          ContentStore.updateBulkEditTagValuesRelevance(aTagValueRelevanceData, oExtraData.entityId);
          break;

        case "uomVariants":
        case ContentScreenViewContextConstants.VARIANT_CONTEXT:
        case ContentScreenViewContextConstants.IMAGE_VARIANT_CONTEXT:
          let oCellData = {
            rowId: oExtraData.rowId,
            columnId: oExtraData.columnId,
            relevanceMap: {},
            type: oExtraData.type
          };

          let aCheckedItems = [];
          CS.forEach(aTagValueRelevanceData, function (oData) {
            let sTagId = oData.tagId;
            aCheckedItems.push(sTagId);
            oCellData.relevanceMap[sTagId] = oData.relevance;
          });
          oCellData.checkedItems = aCheckedItems;
          ContentStore.changeVariantCellValue(oCellData, oExtraData.tableContextId)
          break;

        case "goldenRecordComparisonTag":
          GoldenRecordStore.updateGoldenRecordComparisonTagValues(aTagValueRelevanceData, sTagId);
          break;

        case "bulkEditTag":
          BulkEditStore.updateBulkEditTagValuesRelevance(aTagValueRelevanceData, oExtraData.entityId);
          break;
      }

      _triggerChange();
    },

    handleAttributeVariantInputTextChanged: function (sAttributeId, sValue, sSectionId, sSourceId, oFilterContext) {
      trackMe('handleAttributeVariantInputTextChanged');
      logger.debug('handleAttributeVariantInputTextChanged: Attribute variant value changed',
          {'changedValue': sValue});
      AttributeStore.handleAttributeVariantInputTextChanged(sAttributeId, sValue, sSectionId, sSourceId, oFilterContext);
      _triggerChange();
    },

    handleSectionTagDefaultValueChanged: function (sTagId, aConflictingTagValues, sSectionId, oSource) {
      var oComponentProps = ContentUtils.getComponentProps();
      var oActiveEntityClone = ContentUtils.makeCurrentEntityDirty();
      var oFoundTag = CS.find(oActiveEntityClone.tags, {"tagId": sTagId});
      var oScreenProps = oComponentProps.screen;

      CS.forEach(oFoundTag.tagValues, function (oValue) {
        oValue.relevance = 0;
      });

      CS.forEach(aConflictingTagValues, function (oConflictingTagValue) {
        var oTagValue = CS.find(oFoundTag.tagValues, {tagId: oConflictingTagValue.tagId});
        if (!CS.isEmpty(oTagValue)) {
          oTagValue.relevance = oConflictingTagValue.relevance;
        } else {
          oFoundTag.tagValues.push(ContentUtils.getNewTagValue(oConflictingTagValue.tagId, oConflictingTagValue.relevance));
        }
      });
      if (oFoundTag) {
        //Below flag is added for only UI purpose.
        var bIsResolved = false;
        CS.forEach(oFoundTag.conflictingValues, function (oValue) {
          oValue.isResolved = !CS.isEmpty(oSource) && (oSource.id === oValue.source.id) && (oSource.type === oValue.source.type);
          if(!bIsResolved){
            bIsResolved = oValue.isResolved;
          }
        });
        oFoundTag.isResolved = bIsResolved;
        oFoundTag.isConflictResolved = bIsResolved;

        if (oSource) {
          oFoundTag.source = oSource;
        } else {
          delete oFoundTag.source;
        }
      }

      // To show only saveAndPublish button when isVersion is true
      var oReferencedTags = oScreenProps.getReferencedTags();
      let oReferencedElements = oScreenProps.getReferencedElements();
      let oCurrentElement = oReferencedElements[sTagId];
      let oMasterTag = oReferencedTags[sTagId];

      let bIsVersionable = (oCurrentElement && !CS.isNull(oCurrentElement.isVersionable)) ? oCurrentElement.isVersionable : oMasterTag.isVersionable;
      if (bIsVersionable) {
        oComponentProps.screen.setIsVersionableAttributeValueChanged(true);
      }

      oComponentProps.contentSectionViewProps.setSectionsToUpdate(sSectionId);
      _triggerChange();
    },

    refreshAllProperties: function () {
      trackMe('refreshAllProperties');
      //When switching from content screen then refresh all properties
      oAppData.setContentList([]);
      ContentUtils.setPaginatedIndex();
      var sCurrentScreenMode = ContentScreenProps.screen.getContentScreenMode();
      if(sCurrentScreenMode == ContentScreenConstants.entityModes.ARTICLE_MODE){
        ContentUtils.setActiveContent({});
        ContentScreenProps.rightBarViewProps.resetRightBarProps();
      }
    },

    getDataFromGlobalStore: function () {
      trackMe('getDataFromGlobalStore');
      _fetchScreenMasterData();
      _triggerChange();
    },

    handleAssetUploadClick: function (sActionName, oProperties) {
      var oCallbackData = {};
      oCallbackData.functionToExecute = _triggerChange;
      ContentScreenProps.screen.setIsVersionableAttributeValueChanged(true);
      ImageCoverflowStore.handleAssetUploadClick(sActionName, oProperties, oCallbackData);
    },

    handleContentEntityDragStart: function (oModel) {
    },

    handleContentEntityDragEnd: function () {
      _clearEntityDragStatus();
      _triggerChange();
    },

    handleContentEntityDrop: function (oModel, oFilterContext) {
      var aContexts = oModel.validItems;
      var aSelectedEntities = oComponentProps.availableEntityViewProps.getSelectedEntities();
      if (CS.includes(aContexts, "relationshipEntity") || CS.includes(aContexts, "productVariantQuickList")) {
        var oCurrentDraggedItem = oModel.properties['draggedItem'];
        _handleContentEntityDropInRelationshipSection(oCurrentDraggedItem, oModel.id, oFilterContext);
      }
      else if (CS.includes(aContexts, "staticCollection")){
        var aSelectedObjects = (!CS.isEmpty(aSelectedEntities)) ? aSelectedEntities : [oModel.properties["draggedItem"]];
        var oCallbackData ={
          filterContext: oFilterContext
        };

        /**
         * In Collection QuickList to get all entities(left side from quickList) "QuickList" FilterContext is required.
         */
        let oNewFilterContextForFetchEntities = {
          filterType: oFilterPropType.QUICKLIST,
          screenContext: oFilterPropType.QUICKLIST
        };
        let oCallbackDataForFetchEntities = {selectedContext : "staticCollection", filterContext: oNewFilterContextForFetchEntities};
        oCallbackData.functionToExecute = function () {
          ContentScreenProps.collectionViewProps.setAddEntityInCollectionViewStatus(true);
          AvailableEntityStore.fetchAvailableEntities(oCallbackDataForFetchEntities);
        };
        NewCollectionStore.addNewEntityInActiveStaticCollection(oCallbackData, aSelectedObjects);
      }
      else if (CS.includes(aContexts, "contextEntity")){
        var aSelectedObjects = (!CS.isEmpty(aSelectedEntities)) ? aSelectedEntities : [oModel.properties["draggedItem"]];
        _dropLinkedInstanceIntoVariantUnderCreation(aSelectedObjects, oFilterContext);
      }
      else if (CS.includes(aContexts, "manageVariantLinkedInstances")) {
        var aSelectedObjects = (!CS.isEmpty(aSelectedEntities)) ? aSelectedEntities : [oModel.properties["draggedItem"]];
        _dropLinkedInstanceIntoCreatedVariant(aSelectedObjects, oFilterContext);
      }
      else if (CS.includes(aContexts, ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW)) {
        let oCurrentDraggedItem = oModel.properties['draggedItem'];
        let oEntity = oCurrentDraggedItem.properties["entity"];
        let aSelectedObjects = !CS.isEmpty(aSelectedEntities) ? aSelectedEntities : [oEntity];
        VariantStore.handleVariantAddLinkedInstances(aSelectedObjects);
        AvailableEntityStore.fetchAvailableEntities({ filterContext: oFilterContext }, {});
      }
      _clearEntityDragStatus();
    },

    handleContentEntityDragEnter: function (oDropModel, oDragModel) {

    },

    handleFilterSortReOrdered: function (aSortData, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleFilterSortReOrdered(aSortData);
      if(ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()){
        ContentStore.handleAvailableEntityFilterButtonClicked();
      }else {
        ContentStore.handleFilterButtonClicked();
      }
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sActiveTaxonomyId, sTaxonomyType) {
      if(ContentUtils.contextTableSafetyCheck()){
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return;
      }
      else if (ContentUtils.activeContentSafetyCheck()) {
        ContentStore.handleChildTaxonomyRemoved(oTaxonomy, sActiveTaxonomyId, sTaxonomyType);
      }
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId) {
        ContentStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
    },

    handleAllowedTypesSearchHandler: function (sContext, sSearchText, sTaxonomyId) {
      ContentStore.handleAllowedTypesSearchHandler(sContext, sSearchText, sTaxonomyId);
    },

    handleAllowedTypesLoadMoreHandler: function (sContext, sTaxonomyId) {
      ContentStore.handleAllowedTypesLoadMoreHandler(sContext, sTaxonomyId);
    },

    handleKpiChartTypeSelected: function (sKpiTypeId) {
      ContentStore.handleKpiChartTypeSelected(sKpiTypeId);
    },

    handleMultiSelectSearchCrossIconClicked: function (sContextKey, sId) {
      var sSplitter = ContentUtils.getSplitter();
      var aContextKey = sContextKey.split(sSplitter);
      var sNewContextKey = aContextKey[0];

      switch (sNewContextKey) {

        case 'secondaryClassesViewType':
          ContentStore.handleSecondaryClassRemoved(sId);
          break;

        case 'taxonomyViewType':
          ContentStore.handleTaxonomyRemoved(sId);
          break;

        case 'attr':
          ContentStore.removeValueInContentAttributeFromMultiSearch(sMSSHolderId, sId); // eslint-disable-line
          break;

        case 'tag':
          ContentStore.removeTagFromContentUsingMultiSearch(sId, aContextKey);
          break;

        case 'attributeContentTag':
          sAttributeId = aContextKey[1];
          sTagGroupId =  aContextKey[2];
          AttributeStore.removeTagInAttributeFromMultiSearch(sAttributeId, sTagGroupId, sId);
          break;

        case 'assetContentTag':
          var sAttributeId = aContextKey[1];
          var sTagGroupId =  aContextKey[2];
          AttributeStore.removeTagInAssetFromMultiSearch(sAttributeId, sTagGroupId, sId);
          break;

        case "tagVariant":
          ContentStore.removeTagFromContentUsingMultiSearch(sId, aContextKey);
          break;

        case 'dashboardLogs':
          DashboardScreenStore.updateFilterValuesForDataIntegrationLogs(aContextKey[1], sId);
          break;

        case 'process':
          TasksScreenStore.handleProcessFrequencyDaysCrossIconClicked(aContextKey, sId);
          break;
      }

      _triggerChange();
    },

    handleDropdownListNodeBlurred: function (sContextKey, aCheckedItems) {
      var sSplitter = ContentUtils.getSplitter();
      var aContextKey = sContextKey.split(sSplitter);
      var sNewContextKey = aContextKey[0];
      var sTagGroupId = "";
      switch (sNewContextKey) {
        case 'role':
          ContentStore.addUserInRoleFromMultiSearch(aCheckedItems, 'role', sContextKey);
          _triggerChange();
          break;

        case 'contentFilterTagsInner':
          sTagGroupId = aContextKey[1];
          var sViewContext = aContextKey[2];
          ContentStore.handleContentFilterInnerTagAdded(sTagGroupId, aCheckedItems, false, sViewContext);
          break;

        case 'goldenRecordComparison':
          GoldenRecordStore.handleContentComparisonMatchAndMergeLanguageSelectionClick(aCheckedItems);
          break;

        case 'dashboardLogs':
          let sContextKey = aContextKey[1];
          DashboardScreenStore.handleDataIntegrationLogsViewLazyMssApplyClicked(sContextKey, aCheckedItems, {});
          break;
      }
    },

    handleTemplateDropDownListNodeClicked: function (oSelectedTemplate) {
      if (!ContentUtils.contextTableSafetyCheck()) {
        ContentStore.handleTemplateDropDownListNodeClicked(oSelectedTemplate);
      }
    },

    handleAddTaxonomyPopoverItemClicked: function (oModel, sTaxonomyId, sContext, sTaxonomyType) {
      var sId = oModel.id;
      if (!ContentUtils.contextTableSafetyCheck()) {
        _handleAddTaxonomyPopoverItemClicked(sContext, sId, sTaxonomyId, sTaxonomyType);
      }
    },

    handleUniqueSelectionItemClicked: function(sGroupId, sDataId, sContext, oExtraData){
      var sSplitter = ContentUtils.getSplitter();
      var aContextList = sContext.split(sSplitter);
      let oFilterStore = FilterStoreFactory.getFilterStore(oExtraData.filterContext);
      var sSwitchContext = aContextList[0];
      switch(sSwitchContext){
        case HierarchyTypesDictionary.TAXONOMY_HIERARCHY:
        oFilterStore.addTagInContentFromUniqueSearchForTaxonomySections(aContextList[1], aContextList[2], sDataId);
          break;

        case "appliedTagElement":
          ContentStore.updateBulkEditTagRelevance([sDataId], aContextList[1]);
          break;

        case "bulkEditTag":
          BulkEditStore.updateBulkEditTagRelevance([sDataId], aContextList[1]);
          break;

        default:
          ContentStore.addTagInContentFromUniqueSearch(sGroupId, sDataId);
      }

      _triggerChange();
    },

    handleStructureFroalaImageUploaded: function (sImageKey) {
      ContentStore.handleStructureFroalaImageUploaded(sImageKey);
    },

    handleContentImagesRemoved: function (aImageKeys) {
      ContentStore.handleContentImagesRemoved(aImageKeys);
    },

    handleSectionHeaderClicked: function (oModel) {
      var sPaperViewContext = oModel.properties["context"];
      switch (sPaperViewContext) {
        case "Variant":
          VariantStore.handleUOMSectionExpandToggle(oModel.id);
          break;

        default:
          ContentStore.handleSectionHeaderClicked(oModel);
      }
      _triggerChange();
    },

    handleSectionRemoveClicked: function (sSectionId, sSectionLabel) {
      var bIsCollectionHierarchySelected = ContentScreenProps.screen.getIsCollectionHierarchySelected();
      if(bIsCollectionHierarchySelected){
        var sContext = "noContext";
        var sCallingContext = HierarchyTypesDictionary.COLLECTION_HIERARCHY;
        this.handleRuntimePCDeleteClicked(sContext, sSectionId, sCallingContext, sSectionLabel);
      }
    },

    handleSectionSkipToggled: function (oModel) {
      var bIsCollectionHierarchySelected = ContentScreenProps.screen.getIsCollectionHierarchySelected();
      if(bIsCollectionHierarchySelected){
        var sContext = "noContext";
        var sSectionId = oModel.id;
        var sCallingContext = HierarchyTypesDictionary.COLLECTION_HIERARCHY;
        var sSectionLabel = oModel.label;
        this.handleRuntimeSectionSkipToggled(sContext, sSectionId, sCallingContext, sSectionLabel);
      }
    },

    handleSectionAttributeClicked: function (sSectionContext, sContextKey, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, bIsReadOnlyCoupled, sFillerID) {
      ContentStore.handleSectionAttributeClicked(sSectionContext, sContextKey, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, bIsReadOnlyCoupled, sFillerID);
    },

    handleSectionMaskClicked: function (sSectionId, sElementId, sSectionContext) {
      ContentStore.handleSectionMaskClicked(sSectionId, sElementId, sSectionContext);
    },

    handleSectionElementNotificationButtonClicked: function(sButtonKey, oElement){
      ContentStore.handleSectionElementNotificationButtonClicked(sButtonKey, oElement);
    },

    handleConcatenatedAttributeExpressionChanged: function (sAttributeId, sValue, sSectionId, sExpressionId) {
      AttributeStore.handleConcatenatedAttributeExpressionChanged(sAttributeId, sValue, sSectionId, sExpressionId);
    },

    handleSwitchContentViewTypeClicked: function (sViewType, sMode, oFilterContext) {
      if (ContentUtils.isSameMode(sViewType)) {
        return;
      }
      let sScreenMode = ContentScreenConstants.entityModes.ARTICLE_MODE;
      ContentUtils.setContentScreenMode(sScreenMode);
      ContentUtils.setViewMode(sViewType);

      /**  Required in case Xray is applied and view mode switch from List to Tile**/
      if (ContentUtils.isXRayVisionModeActive() && sViewType === ContentScreenConstants.viewModes.TILE_MODE) {
        let oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup();
        let bMakeDefaultSelected = CS.isEmpty(oActiveXRayPropertyGroup.properties);
        ContentStore.handleShowXRayPropertyGroupsClicked(bMakeDefaultSelected, false, oFilterContext);
      }

      BreadCrumbStore.refreshCurrentBreadcrumbEntity();
    },

    handleContentSectionRoleMSSValueChanged: function (sRoleId, aCheckedItems) {
      ContentStore.addUserInRoleFromMultiSearch(aCheckedItems, sRoleId);
      _triggerChange();
    },

    handleHorizontalThumbnailCheckboxClicked: function (oModel, bClickWithControl) {
      var bAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
      var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
      let oSelectedRelationshipElements = ContentScreenProps.relationshipView.getSelectedRelationshipElements();
      let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
      let bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
      if(bAvailableEntityViewStatus || bIsAnyHierarchySelectedExceptFilterHierarchy){
        if (oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW) {
          AvailableEntityStore.handleThumbnailClicked(oModel, bClickWithControl, true);
          _triggerChange();
          return;
        }
        var oContent = oModel.properties['entity'];
        ContentStore.toggleContentThumbnailSelection(oContent);

        var oAddEntityInRelationshipScreenData = oComponentProps.screen.getAddEntityInRelationshipScreenData();
        var oActiveEntity = ContentUtils.getActiveEntity();
        if(ContentUtils.getNatureRelationshipViewStatus()){
          var oActiveNatureRelationshipData = oAddEntityInRelationshipScreenData[oActiveEntity.id];
          if(CS.includes(oSelectedRelationshipElements[oActiveNatureRelationshipData.id], oModel.id)){
            CS.remove(oSelectedRelationshipElements[oActiveNatureRelationshipData.id], function (n){
              if(n == oModel.id){
                return true;
              }
            })
          } else {
            if(CS.isEmpty(oSelectedRelationshipElements[oActiveNatureRelationshipData.id])){
              oSelectedRelationshipElements[oActiveNatureRelationshipData.id] = [];
            }
            oSelectedRelationshipElements[oActiveNatureRelationshipData.id].push(oModel.id);
          }
        }
        else if(ContentUtils.getRelationshipViewStatus()){
          /*** Below code is to fix the bug -> After adding entity to Relationship(In Quicklist mode), if we select all added entity,
           * then try to de-select only one of them then all selected entity get de-selected ***/
          var sRelationshipId = oModel.properties['currentRelationshipId'];
         /**Nature Relationship elements will also be stored in Normal Relationship element props**/
          oSelectedRelationshipElements = ContentScreenProps.relationshipView.getSelectedRelationshipElements();
          // }
          oSelectedRelationshipElements[sRelationshipId] || (oSelectedRelationshipElements[sRelationshipId] = []);
          var aSelectedRelationshipElements = oSelectedRelationshipElements[sRelationshipId];
          /********** If entity is already selected then deselect it, else select it. *******/
          if (CS.includes(aSelectedRelationshipElements, oContent.id)) {
            CS.remove(aSelectedRelationshipElements, function (sId) {
              return sId == oContent.id;
            });
          } else {
            if (CS.isEmpty(aSelectedRelationshipElements[sRelationshipId])) {
              aSelectedRelationshipElements[sRelationshipId] = [];
            }
            aSelectedRelationshipElements.push(oContent.id);
          }
        }
        else if (bAvailableEntityParentVariantViewVisibilityStatus) {
          var aSelectedVariantList = oComponentProps.variantSectionViewProps.getSelectedVariantInstances();
          var oVariantContext = CS.find(aSelectedVariantList, {id: oContent.id});
          if(!CS.isEmpty(oVariantContext)) {
            CS.remove(aSelectedVariantList, function (oVar) {
              return oVar.id === oContent.id;
            });
          } else {
            aSelectedVariantList.push(oContent);
          }
        }
      }else {
          AvailableEntityStore.handleThumbnailClicked(oModel, bClickWithControl);
      }

      _triggerChange();
    },

    handleHorizontalThumbnailViewClicked: function (oModel, oFilterContext) {
      //To clear filter data
      var oInnerFilterProps = FilterUtils.getInnerFilterProps();
      oInnerFilterProps.reset();

      this.handleThumbnailOpenClicked(oModel, oFilterContext);
    },

    handleHorizontalThumbnailDeleteClicked: function (oModel, oFilterContext) {
      var _this = this;
      var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
      var bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
      var bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
      let bIsTableContextAvailableEntityView = ContentScreenProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
      if (ContentUtils.getRelationshipViewStatus()) {
        _this.deleteRelationshipElement(oModel, oFilterContext);
      } else if (ContentUtils.getIsStaticCollectionScreen()) {
        ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_UNLINK, [oModel.label],
            function () {
              _this.removeEntityFromCollection([oModel.id], oFilterContext);
            }, function (oEvent) {
            });
      } else if (bAvailableEntityChildVariantViewVisibilityStatus || bAvailableEntityParentVariantViewVisibilityStatus || bIsTableContextAvailableEntityView) {
        ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_UNLINK, [oModel.label],
            function () {
              _this.removeEntityFromVariantLinkedInstances(oModel.id, oFilterContext);
            }, function (oEvent) {
            });
      }
    },

    removeEntityFromVariantLinkedInstances: function(sLinkedInstanceId, oFilterContext){
      var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
      var bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
      var bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
      var bToExecute = false;

      if (bAvailableEntityChildVariantViewVisibilityStatus) {

        var oDummyVariant = ContentUtils.makeActiveContentOrVariantDirty();
        bToExecute = _removeLinkedInstanceByIdFromVariant(sLinkedInstanceId, oDummyVariant);
        let oCallbackData = {
          selectedContext: "contextEntity",
          filterContext: oFilterContext
        }
        bToExecute && AvailableEntityStore.fetchAvailableEntities(oCallbackData);

      }else if(bAvailableEntityParentVariantViewVisibilityStatus){

        var oActiveEntity = ContentUtils.makeActiveEntityDirty();
        _removeLinkedInstanceByIdFromVariant(sLinkedInstanceId, oActiveEntity);
        var oCallbackData = {};
        oCallbackData.functionToExecute = AvailableEntityStore.fetchAvailableEntities.bind(this, { filterContext: oFilterContext });
        ContentStore.saveActiveContent(oCallbackData);
      } else if (ContentScreenProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW) {
        _removeTableEntitiesFromAvailableEntitiesView([sLinkedInstanceId], oFilterContext)
      }

      _triggerChange();
    },

    removeEntityFromCollection: function (aRemovedElementsIds, oFilterContext) {
      var oCallbackData = {
        doNotAddBreadcrumb: true,
        filterContext: oFilterContext
      };

      /**
       * In Collection QuickList to get all entities(left side from quickList) "QuickList" FilterContext is required.
       */
      let oNewFilterContextForFetchEntities = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      };
      let oCallbackDataForFetchEntities  = {selectedContext : "staticCollection", filterContext: oNewFilterContextForFetchEntities};
      oCallbackData.functionToExecute = function () {
        ContentScreenProps.collectionViewProps.setAddEntityInCollectionViewStatus(true);
        AvailableEntityStore.fetchAvailableEntities(oCallbackDataForFetchEntities);
      };
      var oADM = {
        removedKlassInstanceIds: aRemovedElementsIds
      }
      NewCollectionStore.handleCollectionSaved(oCallbackData, oADM);
    },

    handleFilterElementDeleteClicked: function (sElementId, oFilterContext) {
      ContentStore.handleFilterElementDeleteClicked(sElementId, oFilterContext);
    },

    handleFilterElementExpandClicked: function (sElementId, oFilterContext) {
      ContentStore.handleFilterElementExpandClicked(sElementId, oFilterContext);
    },

    handleAddAttributeValueClicked: function (sAttributeId, oFilterContext) {
      ContentStore.handleAddAttributeValueClicked(sAttributeId, oFilterContext);
    },

    handleFilterAttributeValueTypeChanged: function (sAttributeId,  sValId, sTypeId, oFilterContext) {
      ContentStore.handleFilterAttributeValueTypeChanged(sAttributeId,  sValId, sTypeId, oFilterContext);
    },

    handleFilterAttributeValueChanged: function (sAttributeId,  sValId, sVal, oFilterContext) {
      ContentStore.handleFilterAttributeValueChanged(sAttributeId,  sValId, sVal, oFilterContext);
    },

    handleFilterAttributeValueChangedForRange: function (sAttributeId,  sValId, sVal, sRange, oFilterContext) {
      ContentStore.handleFilterAttributeValueChangedForRange(sAttributeId,  sValId, sVal, sRange, oFilterContext);
    },

    handleFilterAttributeValueDeleteClicked: function (sAttributeId,  sValId, oFilterContext) {
      ContentStore.handleFilterAttributeValueDeleteClicked(sAttributeId,  sValId, oFilterContext);
      _triggerChange();
    },

    handleContentFilterUserValueChanged: function (sRoleId, aUsers, oFilterContext) {
      ContentStore.handleContentUserAdded(sRoleId, aUsers, oFilterContext);
      _triggerChange();
    },

    handleClearFilterButtonClicked: function (oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var oFilterProps = oFilterStore.getFilterProps();

      /** This line is imp for creating clone appliedFilter n to make filter dirty never remove below line **/
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      /**@note
       ** @one Custom Filters are not required to be cleared from advance filters.
       ** @two If even after removing the custom filters from applied filters is not empty
       **      then its necessary to assign custom filter back to applied filters **/
      var aCustomFilters = CS.remove(aAppliedFilters, function (oAppliedFilters) {
        return CS.includes([ContentScreenConstants.FILTER_FOR_ASSET_EXPIRY],
            oAppliedFilters.id);
      });

      if (CS.isNotEmpty(aAppliedFilters)) {
        oFilterProps.setAppliedFiltersClone(CS.isEmpty(aCustomFilters) ? [] : aCustomFilters);
        _triggerChange();
      }
    },

    handleFilterButtonClicked: function(sContext, oExtraData, sSelectedHierarchyContext, oFilterContext) {
      if (sContext == ContentScreenViewContextConstants.VARIANT_CONTEXT || sContext == ContentScreenViewContextConstants.IMAGE_VARIANT_CONTEXT) {
        oExtraData.filterContext = oFilterContext;
        ContentScreenStore.handleVariantFilterButtonClicked(oExtraData, oFilterContext);
      } else {
        _handleFilterButtonClicked({filterContext: oFilterContext}, sSelectedHierarchyContext);
      }
    },

    handleFilterPopoverClosed: function (oFilterContext, sContext, oExtraData) {
      if (sContext == ContentScreenViewContextConstants.VARIANT_CONTEXT || sContext == ContentScreenViewContextConstants.IMAGE_VARIANT_CONTEXT) {
        oExtraData.filterContext = oFilterContext;
        VariantStore.handleUOMFilterPopoverClosed(oExtraData);
      }
      else {
        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.discardSelectedFilters();
      }
    },

    handleVariantFilterButtonClicked: function (oExtraData, oFilterContext) {
      if (!ContentUtils.contextTableSafetyCheck()) {
        VariantStore.handleUOMFilterButtonClicked(oExtraData, oFilterContext);
      }
    },

    handleFilterSearchTextChanged: function (oSearchedData, oExtraData) {
      ContentStore.handleFilterSearchTextChanged(oSearchedData, oExtraData);
    },

    handleSetSectionClicked: function (sActiveSetId, sContext,sRelationshipSectionId,oRelationshipSide) {
      ContentStore.handleSetSectionClicked(sActiveSetId, sContext,sRelationshipSectionId,oRelationshipSide);
    },

    handleMetricValueExceeded: function(){
      alertify.error(getTranslation().MAXIMUM_VALUE_EXCEEDED);
    },

    handleMetricUnitChanged: function(sSelectedUnit, oSectionElementDetails, sAttrId, sValueId, sRangeType, oFilterContext){
      ContentStore.handleMetricUnitChanged(sSelectedUnit, oSectionElementDetails, sAttrId, sValueId, sRangeType, oFilterContext);
    },

    /**
     * @Router Impl
     *
     * @param oItem: Breadcrumb Item
     * @description Called through pop-state only
     * @private
     */
    handleEntityNavigation: function (oItem, oFilterContext) {
      let aBreadCrumbData = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
      let oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();

      if (ContentUtils.isActiveContentDirty(oFilterContext) || oActiveCollection.isDirty) {
        /** Required to reset history state when Browser Back or Forward button is clicked and content is dirty **/
        let oActiveContent = oActiveCollection.isDirty ? oActiveCollection : ContentUtils.getActiveContent();
        let oActiveContentIndex = CS.findIndex(aBreadCrumbData, {id: oActiveContent.id});
        let oItemIndex = CS.findIndex(aBreadCrumbData, {id: oItem.id});
        SharableURLProps.setIsEntityNavigation(false);

        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);

        if (oActiveContentIndex > oItemIndex && oItemIndex !== -1) {
          CS.navigateForward();
        } else {
          CS.navigateBack();
        }
        return;
      }

      //TODO: Try to remove this.
      let oComponentProps = ContentUtils.getComponentProps();
      oComponentProps.screen.setRelationshipContextData({});
      oComponentProps.availableEntityViewProps.setSelectedEntities([]);
      oComponentProps.availableEntityViewProps.setAvailableEntityViewContext("");
      oAppData.setAvailableEntities([]);

      // On navigation default view mode should be set as a view mode
      _setViewMode(oItem);

      ContentStore.handleEntityNavigation(oItem);
    },

    handleStaticCollectionBreadCrumbItemClicked: function (oItem, sContext) {
      NewCollectionStore.handleStaticCollectionBreadCrumbItemClicked(oItem, sContext);
    },

    handleSelectAllContentClicked: function (bIsForPresentEntity) {
      var oScreenProps = ContentScreenProps.screen;
      var bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();
      var bIsCollectionHierarchySelected = oScreenProps.getIsCollectionHierarchySelected();

      if((ContentUtils.getAvailableEntityViewStatus() && !bIsForPresentEntity) ||
          bIsTaxonomyHierarchySelected || bIsCollectionHierarchySelected){
        _handleSelectAllAvailableEntityClicked();
        return;
      } else if(oComponentProps.variantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus() ||
          oComponentProps.variantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus()) {
        var oVariant = ContentUtils.getActiveContentOrVariant();
        _handleSelectAllVariantEntityClicked(oVariant);
        return;
      }

      var aSelectedContentIds = ContentUtils.getSelectedContentIds();
      var aContentList = oAppData.getContentList();
      /*** De-select all only if all entities are selected, else select all ***/
      if (aSelectedContentIds.length) {
        ContentUtils.setSelectedContentIds([]);
      } else {
        ContentUtils.setSelectedContentIds(CS.map(aContentList, "id"));
      }
      _triggerChange();
    },

    handleViewRelationshipVariantXRayClicked: function(sRelationshipId){
      _handleViewRelationshipVariantXRayClicked(sRelationshipId);
    },

    handleSelectAllRelationshipEntityClicked: function (sRelationshipId, sViewContext) {
      ContentRelationshipStore.handleSelectAllRelationshipEntityClicked(sRelationshipId, sViewContext);
    },

    handleContentTileVerticalMenuItemClicked: function(oItem){

      switch (oItem.id){

        case "selectAll":
          ContentStore.selectAllArticleChildren();
          break;
        default:
          _handleCreateModuleEntityByBaseType(oItem);
      }
    },

    handleModuleCreateButtonClicked: function (sSearchedText, sContext, oFilterContext, bIsLoadMoreClicked) {
      ContentStore.handleModuleCreateButtonClicked(sSearchedText, sContext, oFilterContext, bIsLoadMoreClicked);
    },

    handleUploadContentButtonClicked: function () {
      trackMe('handleUploadContentButtonClicked');
      ContentStore.activateUploadAssetPopUp();
    },

    handleZoomToolClicked: function (sKey, sContext, sElementId) {
      ContentStore.handleZoomToolClicked(sKey, sContext, sElementId);
    },

    refreshAll: function (sMode, oCallbackData) {
      return _refreshAll(sMode, oCallbackData);
    },

    resetAll: function () {
      ContentScreenProps.breadCrumbProps.setBreadCrumbData([]);
      ContentUtils.resetAll();
    },

    handleControlS: function (oEvent) {
      oEvent.preventDefault && oEvent.preventDefault();
      _handleControlS(oEvent);
    },

    handleTab: function (oEvent, bWithShift) {
      var oScreenProps = oComponentProps.screen;
      var oAddEntityInRelationshipScreenData = oScreenProps.getAddEntityInRelationshipScreenData();
      var oActiveEntity = ContentUtils.getActiveEntity();
      if (oAddEntityInRelationshipScreenData && oAddEntityInRelationshipScreenData[oActiveEntity.id]
          && oAddEntityInRelationshipScreenData[oActiveEntity.id].status) {
        //do nothing
      }
      else if(oScreenProps.getVariantUploadImageDialogStatus()){
        //do nothing
      }
      else {
        ContentStore.handleTab(oEvent, bWithShift);
      }
    },

    handleControlD: function (oEvent) {
      oEvent.preventDefault && oEvent.preventDefault();
      _handleControlD();
    },

    handleEsc: function (oEvent) {
      let oScreenProps = oComponentProps.screen;

      let aBreadcrumbNodes = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
      let oBreadcrumbNode = aBreadcrumbNodes[aBreadcrumbNodes.length - 1];
      let oFilterContext = oBreadcrumbNode.filterContext;

      let bIsCustomDialogOpen = ActionDialogProps.getIsCustomDialogOpen();
      let bIsDragNDropState = oScreenProps.getQuickListDragNDropState();
      let oContentGridProps = ContentScreenProps.contentGridProps;
      let bIsGridEditViewMode = oContentGridProps.getIsContentGridEditViewOpen();
      let oGoldenRecordProps = ContentScreenProps.goldenRecordProps;
      let bIsGoldenRecordMatchAndMergeMode = oGoldenRecordProps.getIsFullScreenMode();
      let bIsBulkEditViewOpen = oComponentProps.bulkEditProps.getIsBulkEditViewOpen();
      let sFullScreenContextId = UOMProps.getFullScreenTableContextId();

      if (bIsDragNDropState) {
        /** when dragging process is in progress, if user escapes then dont go back **/
        return false;
      }

      if (bIsCustomDialogOpen) { /** Action Dialog **/
        CustomActionDialogStore.resetCustomDialog();
      } else if (bIsGridEditViewMode) { /** Fullscreen **/
        ContentScreenStore.handleContentGridEditCloseButtonClicked();
      } else if (bIsGoldenRecordMatchAndMergeMode) { /** Fullscreen **/
        GoldenRecordStore.handleGoldenRecordDialogActionButtonClicked("cancel");
      } else if (CS.isNotEmpty(sFullScreenContextId)) {/** Fullscreen **/
        VariantStore.handleUOMViewFullScreenButtonClicked(sFullScreenContextId);
      } else if (bIsBulkEditViewOpen) {
        BulkEditStore.handleBulkEditCancelClicked();
      } else {
        /** All screens other than dialog which have breadcrumb are handled here **/
        switch (oBreadcrumbNode.type) {
          case BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST :
            let sContextTemp = "staticCollection";
            this.handleRelationshipPresentEntityOkButtonClicked(sContextTemp, oFilterContext);
            break;

          case BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION :
          case BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION :
          case BreadCrumbModuleAndHelpScreenIdDictionary.COLLECTION_HIERARCHY :
          case BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY :
          case BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION :
          case BreadCrumbModuleAndHelpScreenIdDictionary.ASSET_RULE_VIOLATION :
          case BreadCrumbModuleAndHelpScreenIdDictionary.EXPLORER_KPI_TILE :
          case BreadCrumbModuleAndHelpScreenIdDictionary.ENDPOINT_SCREEN :
          case BreadCrumbModuleAndHelpScreenIdDictionary.DIMODULE :
          case BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT :
            ContentStore.handleEntityESCButtonClicked();
            break;

          case BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST :
            let sEntityRequiredContext = 'contextEntity';
            this.handleRelationshipPresentEntityOkButtonClicked(sEntityRequiredContext, oFilterContext);
            break;

          case BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST :
            _handleRelationshipPresentEntityOkButtonClicked(oFilterContext);
            break;
        }
      }
    },

    handleEnter: function (oEvent) {
    },

    stopSCUUpdate: function () {
      ContentUtils.stopSCUUpdate();
    },

    handleTreeCheckClicked: function (iId, iLevel, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      let oFilterProps = oFilterStore.getFilterProps();

      /**
       * TODO: Temporary fix to restore clickedTaxonomyId if discard the bookmark.
       * To remove this pass clickedTaxonomyId on bookmark get call from back-end.
       */
      let CollectionViewProps = ContentScreenProps.collectionViewProps;
      let oActiveCollection = CollectionViewProps.getActiveCollection();
      let oAppliedTaxonomyCloneData =  CollectionViewProps.getAppliedTaxonomyCloneData();
      if (iLevel == "1" && CS.isEmpty(oAppliedTaxonomyCloneData) && oActiveCollection.type === "dynamicCollection") {
        oAppliedTaxonomyCloneData.selectedOuterParentId = oFilterProps.getSelectedOuterParentId();
      };

      oFilterStore.handleTreeCheckClicked(iId, iLevel);

      if (iLevel == 1) {
        var oExtraData = {};
        oExtraData.parentTaxonomyId = oFilterProps.getSelectedOuterParentId();
        if(iId == oExtraData.parentTaxonomyId) {
          oExtraData.selectedTypes = [];
          oExtraData.parentTaxonomyId && (oExtraData.selectedTaxonomyIds = [oExtraData.parentTaxonomyId]);
        } else {
          oExtraData.parentTaxonomyId = "";
          oExtraData.selectedTaxonomyIds = [];
        }
        var oCallBackData = {
          checkAndShowAllNodes: oFilterStore.checkAndShowAllNodes,
          filterContext: oFilterContext
        };

        oFilterProps.setSelectedParentTaxonomyIds(oExtraData.selectedTaxonomyIds);

        CollectionAndTaxonomyHierarchyStore.handleParentTaxonomyClicked(oCallBackData, oExtraData);
      }
    },

    handleTaxonomyChildrenLazyData: function (sNodeId, iLevel, oFilterContext) {
      _handleTaxonomyChildrenLazyData(sNodeId, iLevel, oFilterContext);
    },

    handleTreeHeaderCheckClicked: function (iId, iLevel, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleTreeCheckClicked(iId, iLevel);
    },

    handleTreeNodeToggleClicked: function (sId, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleTreeNodeToggleClicked(sId);
    },

    handleTaxonomySearchTextChanged: function (sSearchText, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.setTaxonomySearchText(sSearchText);

      if (oFilterStore.getSelectedOuterParentId() != "" && oFilterStore.getSelectedOuterParentId() != "-1") {
        var oExtraData = {};
        oExtraData.parentTaxonomyId = oFilterStore.getSelectedOuterParentId();
        oExtraData.searchText = sSearchText;
        var oCallBackData = {
          filterContext: oFilterContext,
          functionToExecute: oFilterStore.handleTaxonomySearchTextChanged.bind(oFilterStore, sSearchText)
        };
        CollectionAndTaxonomyHierarchyStore.handleTaxonomyTreeSearchData(oCallBackData, oExtraData);
      } else {
        oFilterStore.handleTaxonomySearchTextChanged(sSearchText);
      }
    },

    handleSelectTaxonomiesButtonClicked: function (oFilterContext, oTaxonomyTreeRequestData) {
      let bCallNextFunction = ContentUtils.allHierarchySafetyCheck();
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      let oFilterProps = oFilterStore.getFilterProps();
      oFilterProps.setIsSelectTaxonomyPopOverVisible(true);
      if(bCallNextFunction){
        let oCallback = {
          filterContext: oFilterContext,
          isRetainSelectedTaxonomyIds: true
        };
        let oExtraData = {
          taxonomyTreeRequestData: oTaxonomyTreeRequestData
        };
        oFilterStore.fetchTaxonomyTreeData(false, oCallback, oExtraData);
      }
    },

    handleAppliedTaxonomyRemovedClicked: function (iId, oFilterContext, sCategoryType) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      let sSelectedHierarchyContext = ContentUtils.getSelectedScreenContextForQuickList();
      let oFilterProps = oFilterStore.getFilterProps();
      _makeAppliedTaxonomyDirtyForBookmark(oFilterContext);

      if (sCategoryType === "classes") {
        let aSelectedTypes = oFilterProps.getSelectedTypes();
        let iIndex = CS.indexOf(aSelectedTypes, iId);
        aSelectedTypes.splice(iIndex, 1);
        oFilterProps.setSelectedTypes(aSelectedTypes);
      }
      else if (sCategoryType === "taxonomies") {
        let aSelectedTaxonomyIds = oFilterProps.getSelectedTaxonomyIds();
        let iIndex = CS.indexOf(aSelectedTaxonomyIds, iId);
        aSelectedTaxonomyIds.splice(iIndex, 1);
        oFilterProps.setSelectedTaxonomyIds(aSelectedTaxonomyIds);

      }

      let bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();

      if(ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy){
        ContentStore.handleAvailableEntityFilterButtonClicked({ filterContext: oFilterContext }, null, sSelectedHierarchyContext);
      } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
        GoldenRecordStore.handleFilterButtonClicked(oFilterContext)
      } else {
        ContentStore.handleFilterButtonClicked({filterContext: oFilterContext});
      }
    },

    handleSortByItemClicked: function (sId, bMultiSelect, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleSortByItemClicked(sId, bMultiSelect);
      var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
      if(ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy){
        ContentStore.handleAvailableEntityFilterButtonClicked();
      }else {
        ContentStore.handleFilterButtonClicked({filterContext: oFilterContext});
      }
    },

    handleDeactivateSortClicked: function (sId, bMultiSelect, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleDeactivateSortClicked(sId, bMultiSelect);
      var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
      if(ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy){
        ContentStore.handleAvailableEntityFilterButtonClicked();
      }else {
        ContentStore.handleFilterButtonClicked();
      }
    },

    handleFilterSortOrderToggled: function (sId, bMultiSelect, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      let sSelectedContext = ContentUtils.getSelectedScreenContextForQuickList() || ContentUtils.getSelectedHierarchyContext();
      oFilterStore.handleFilterSortOrderToggled(sId, bMultiSelect);
      var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
      if(ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy){
        ContentStore.handleAvailableEntityFilterButtonClicked({filterContext: oFilterContext}, null, sSelectedContext);
      } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
        GoldenRecordStore.handleFilterButtonClicked(oFilterContext);
      } else {
        ContentStore.handleFilterButtonClicked({filterContext: oFilterContext});
      }
    },

    handleFilterSortDeactivatedItemClicked: function (sId, bMultiSelect, sSelectedHierarchyContext, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleFilterSortDeactivatedItemClicked(sId, bMultiSelect);
      var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
      if(ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy){
        if (CS.isEmpty(sSelectedHierarchyContext)) {
          sSelectedHierarchyContext = ContentUtils.getSelectedScreenContextForQuickList();
        }
        ContentStore.handleAvailableEntityFilterButtonClicked({filterContext: oFilterContext}, {}, sSelectedHierarchyContext);
      } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
        GoldenRecordStore.handleFilterButtonClicked(oFilterContext);
      } else {
        ContentStore.handleFilterButtonClicked({filterContext: oFilterContext});
      }
    },

    handleFilterSortActivatedItemClicked: function (sId, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleFilterSortActivatedItemClicked(sId);
      var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
      if(ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy){
        ContentStore.handleAvailableEntityFilterButtonClicked({filterContext: oFilterContext});
      } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
        GoldenRecordStore.handleFilterButtonClicked(oFilterContext);
      } else {
        ContentStore.handleFilterButtonClicked({filterContext: oFilterContext});
      }
    },

    handleSelectedTaxonomiesClearAllClicked: function (oFilterContext, sCategoryType) {
      _makeAppliedTaxonomyDirtyForBookmark(oFilterContext);
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      let sSelectedHierarchyContext = ContentUtils.getSelectedScreenContextForQuickList();
      // oFilterStore.handleSelectedTaxonomiesClearAllClicked();
      let oFilterProps = oFilterStore.getFilterProps();
      if (sCategoryType === "classes") {
        oFilterProps.setSelectedTypes([]);
      }
      else if (sCategoryType === "taxonomies") {
        oFilterProps.setSelectedTaxonomyIds([]);
      }
      var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
      if (ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy) {
        ContentStore.handleAvailableEntityFilterButtonClicked({ filterContext: oFilterContext }, null, sSelectedHierarchyContext);
      } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
        GoldenRecordStore.handleFilterButtonClicked(oFilterContext);
      } else {
        ContentStore.handleFilterButtonClicked({ filterContext: oFilterContext });
      }
    },

    handleAdvancedSearchButtonClicked: function (oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var oFilterProps = oFilterStore.getFilterProps();
      oFilterProps.setIsFilterDirty(false);
      oFilterStore.modifyAlreadyPresentDataIntoAdvancedFilterData();
      this.toggleAdvancedSearchDialogVisibility(oFilterContext);
      var bAdvancedSearchStatus = oFilterProps.getAdvancedSearchPanelShowStatus();
      if (bAdvancedSearchStatus) {
        ContentScreenProps.screen.resetEntitiesPaginationData();
        ContentScreenProps.screen.setEntitySearchText("");
        let aEntitiesList = [ConfigDataEntitiesDictionary.ATTRIBUTES, ConfigDataEntitiesDictionary.TAGS];
        return ContentStore.fetchEntities(aEntitiesList);
      }
      _triggerChange();
    },

    handleAdvancedSearchPanelCancelClicked: function (oFilterContext) {
      this.discardAppliedFilterDirtyChanges(oFilterContext);
      this.toggleAdvancedSearchDialogVisibility(oFilterContext);

      var sSearchText = ContentScreenProps.screen.getEntitySearchText();
      if(!CS.isEmpty(sSearchText)) {
        ContentStore.handleAdvancedSearchListSearched("");
      }
      else {
        _triggerChange();
      }
    },

    handleBulkEditHeaderToolbarButtonClicked: function (sButtonId) {
      BulkEditStore.handleBulkEditHeaderToolbarButtonClicked(sButtonId);
    },

    handleBulkEditCancelClicked: function () {
      BulkEditStore.handleBulkEditCancelClicked();
    },

    handleBulkEditGetTreeNodeChildren: function (sTreeNodeId) {
      BulkEditStore.getTaxonomyChildrenList(sTreeNodeId);
    },

    handleBulkEditPropertyCheckboxClicked: function (oProperty) {
      BulkEditStore.handleBulkEditPropertyCheckboxClicked(oProperty);
    },

    handleBulkEditApplyButtonClicked: function (oSummaryView) {
      let oCallback = {
        functionToExecute: () => {
          let loaderContainer = document.getElementById('loaderContainer');
          if (loaderContainer) {
            loaderContainer.classList.remove('loaderInVisible');
          }
          setTimeout(_handleRefreshContentButtonClicked, 1000);
        },
        summaryView: oSummaryView
      };
      BulkEditStore.handleBulkEditApplyButtonClicked(oCallback);
    },

    handleBulkEditPropertyValueChanged: function (sAttributeId, valueData) {
      BulkEditStore.handleBulkEditPropertyValueChanged(sAttributeId, valueData);
    },

    handleBulkEditRemoveProperty: function (oProperty) {
      BulkEditStore.handleBulkEditRemoveAppliedProperty(oProperty);
    },

    handleActionableChipsViewCrossIconClicked: function (sId, sContext) {
      BulkEditStore.handleCrossIconClicked(sId);
    },

    handleSectionElementConflictIconClicked: function (sElementId, bShowConflictSourcesOnly, bIsConflictResolved) {
      ContentStore.handleSectionElementConflictIconClicked(sElementId, bShowConflictSourcesOnly, bIsConflictResolved);
    },

    discardAppliedFilterDirtyChanges: function (oFilterContext) {
      ContentStore.discardAppliedFilterDirtyChanges(oFilterContext);
    },

    toggleAdvancedSearchDialogVisibility: function (oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleAdvancedSearchButtonClicked();
    },

  handleFilterShowDetailsClicked: function (oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    let oRequestData = {
        selectedTypes: oFilterProps.getSelectedTypes(),
        selectedTaxonomyIds: oFilterProps.getSelectedTaxonomyIds(),
      };
      let oCallbackData = {
        filterContext: oFilterContext,
        functionToExecute: function () {
          oFilterProps.setIsFilterInformationRequired(oFilterProps.getShowDetails());
        }
      };
      oFilterStore.handleFilterShowDetailsClicked(oCallbackData, oRequestData);
    },

    handleClearAllAppliedFilterClicked: function (sSelectedHierarchyContext, oFilterContext) {
      _handleClearAllAppliedFilterClicked({filterContext: oFilterContext}, sSelectedHierarchyContext);
    },

    handleRemoveFilterGroupClicked: function (sId, sContext, oExtraData, sSelectedHierarchyContext, oFilterContext) {
      if (sContext == ContentScreenViewContextConstants.VARIANT_CONTEXT || sContext == ContentScreenViewContextConstants.IMAGE_VARIANT_CONTEXT) {
        oExtraData.filterContext = oFilterContext;
        VariantStore.handleUOMRemoveFilterGroupClicked(sId, oExtraData);
      } else {
        _handleRemoveFilterGroupClicked(sId, sSelectedHierarchyContext, oFilterContext);
      }
    },

    handleChildFilterToggled: function (sParentId, sChildId, sContext, oExtraData, oFilterContext) {
      if (sContext == ContentScreenViewContextConstants.VARIANT_CONTEXT || sContext == ContentScreenViewContextConstants.IMAGE_VARIANT_CONTEXT) {
        oExtraData.filterContext = oFilterContext;
        ContentScreenStore.handleVariantChildFilterToggled(sParentId, sChildId, oExtraData);
      } else {
        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.handleChildFilterToggled(sParentId, sChildId);
      }
    },

    handleVariantChildFilterToggled: function (sParentId, sChildId, oExtraData) {
        VariantStore.handleUOMChildFilterToggled(sParentId, sChildId, oExtraData);
    },

    handleApplyLazyFilter: function (oFilterData) {
      _handleApplyLazyFilter(oFilterData);
    },

    handleSubmitFilterSearchText: function (sSearchText, sSelectedHierarchyContext, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext)
      oFilterStore.handleSubmitFilterSearchText(sSearchText);

      if (oFilterStore.isNewSearchTextDifferent()) {
        oFilterStore.resetPaginationOnApplyingFilter();
        if (ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()) {
          ContentStore.handleAvailableEntityFilterButtonClicked({filterContext: oFilterContext}, {}, sSelectedHierarchyContext);
        }
        else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
          GoldenRecordStore.handleFilterButtonClicked(oFilterContext);
        } else {
          ContentStore.handleFilterButtonClicked({filterContext: oFilterContext});
        }
      }
    },

    handleTaxonomyCancelButtonClicked: function (oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleTaxonomyCancelButtonClicked();
    },

    handleTaxonomyClearAllClicked: function (oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleTaxonomyClearAllClicked();
    },

    handleEntityHeaderEntityNameBlur: function (sLabel) {
      _handleEntityHeaderEntityNameBlur(sLabel);
    },

    handleEntityHeaderEntityNameEditClicked: function () {
      _handleEntityHeaderEntityNameEditClicked();
    },

    handleContentEventImageChanged: function (aFiles) {
      ContentStore.handleContentEventImageChanged(aFiles);
    },

    handleTaskDataChanged: function (oData) {
      ContentStore.handleTaskDataChanged(oData);
      _triggerChange();
    },

    handleToggleThumbnailModeClicked: async function (sRelationshipId, sRelationshipIdToFetchData, oFilterContext) {
      var oScreenProps = ContentScreenProps.screen;
      if(sRelationshipId){
          _handleViewRelationshipVariantXRayClicked(sRelationshipId, sRelationshipIdToFetchData, oFilterContext);
      }
      else {
        let sSectionInnerThumbnailMode = oScreenProps.getSectionInnerThumbnailMode();
        let sCurrentThumbnailMode = oScreenProps.getThumbnailMode();

        if(sSectionInnerThumbnailMode === ThumbnailModeConstants.XRAY ||
           sCurrentThumbnailMode === ThumbnailModeConstants.XRAY) {
          ContentUtils.clearXRayDataFromBreadCrumbPayLoad();
        }

        if (ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()) {
          if (sSectionInnerThumbnailMode === ThumbnailModeConstants.BASIC) {
            oScreenProps.setSectionInnerThumbnailMode(ThumbnailModeConstants.XRAY);
            ContentStore.handleShowXRayPropertyGroupsClicked(true, false, oFilterContext);
          } else {
            oScreenProps.setSectionInnerThumbnailMode(ThumbnailModeConstants.BASIC);
          }
        } else {
          if (sCurrentThumbnailMode === ThumbnailModeConstants.BASIC) {
            oScreenProps.setThumbnailMode(ThumbnailModeConstants.XRAY);
            let oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup();
            let bMakeDefaultSelected = CS.isEmpty(oActiveXRayPropertyGroup.properties) ? true : false;
            await ContentStore.handleShowXRayPropertyGroupsClicked(bMakeDefaultSelected, false, oFilterContext);
            ContentStore.fetchContentList({filterContext: oFilterContext}, {});
          } else {
            oScreenProps.setThumbnailMode(ThumbnailModeConstants.BASIC);
          }
        }
      }

      _triggerChange();
    },

    handleCommentChanged: function (sNewValue) {
      ContentStore.handleCommentChanged(sNewValue);
    },

    handleCollectionSaveCommentChanged: function (sNewValue) {
      var oCollectionProps = ContentScreenProps.collectionViewProps;
      var oActiveCollection = oCollectionProps.getActiveCollection();
      oActiveCollection.saveComment = sNewValue;
    },

    /**
     * @Router Impl
     *
     * @param sModuleId: Module ID (PIM, DAM etc)
     * @description Module click handling (PIM, DAM etc), Reset state to -1
     * @private
     */
    setBrowserStateForModule: function (sModuleId, fCallback) {
      let aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
      SharableURLProps.setIsPushHistoryState(true);/** When module clicked after Browser buttons navigation, We need to set isPushHistoryState true. **/
      let oHistoryState = CS.getHistoryState();

      if (aBreadcrumb.length && oHistoryState) {
        let oForwardBreadCrumbData = {};
        let oActiveEndpoint = SessionProps.getActiveEndpointData();
        let oLastBreadcrumbNode = aBreadcrumb[aBreadcrumb.length - 1];
        /**
         * - quick list adjustment for history state.
         * - Ignore quicklist Item in breadcrumb for deciding new state of history.
         **/
        let bIsQuickListPresent = ContentUtils.isQuickListNode(oLastBreadcrumbNode);
        let iStateValue = bIsQuickListPresent ? aBreadcrumb.length - 1 : aBreadcrumb.length;

        let oExistingBreadCrumbNode = CS.find(aBreadcrumb, {id: sModuleId});

        /**
         * We have 2 types scenario for dashboard module & for other modules e.g.
         * null-> dashboard:Endpoint -> module-> content
         * null-> module (PIM) -> content
         * case 1 : If clicked on same module(PIM) then go to module node
         * case 2 : If clicked on other module(DAM) then go to state before module)
         **/

        if (!CS.isEmpty(oActiveEndpoint) && CS.isEmpty(oExistingBreadCrumbNode)) {
          // In endpoint need to reset trailing nodes of selected modules inside dashboard & maintain dashboard module in oForwardBreadCrumbData)
          iStateValue = iStateValue - 1;
          oForwardBreadCrumbData[BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD] = CS.find(aBreadcrumb, {id: BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD});
        }

        if(oExistingBreadCrumbNode) {
          // If clicked on selected module then go to that specific state, dont go to previous state
          let iIndexOfBreadcrumbNode = CS.findIndex(aBreadcrumb, {id: oExistingBreadCrumbNode.id});
          iStateValue = iStateValue - (iIndexOfBreadcrumbNode + 1);
        }
        else {
          // If clicked on Module which does not exist in breadcrumb.
          ContentScreenProps.breadCrumbProps.setForwardBreadCrumbData(oForwardBreadCrumbData);
        }

        if(iStateValue) {
          SharableURLProps.setIsEntityNavigation(false);
          CS.navigateTo(-iStateValue, fCallback);
          return;
        }
      }
      fCallback();
    },

    handleModuleItemClicked: function (sModuleId, oFilterContext) {
      if (ContentUtils.isActiveContentDirty(oFilterContext)) {
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return;
      }

      let fCallback = async () => {
        //Remove trailing breadcrumb path on module click
        let sModuleContext = SessionProps.getSessionPhysicalCatalogId() === PhysicalCatalogDictionary.DATAINTEGRATION ? "DIModule" : "module";
        ContentUtils.removeTrailingBreadcrumbPath(sModuleId, sModuleContext);
        ContentScreenProps.availableEntityViewProps.setAvailableEntityViewContext("");

        let bFileMappingVisibility = ContentScreenProps.screen.getOnboardingFileMappingViewVisibilityStatus();
        if (bFileMappingVisibility) {
          let oCallBackData = {
            functionToExecute: this.handleModuleItemClicked.bind(this, sModuleId, oFilterContext)
          };
          _handleSafetyCheckForFileMappingViewVisibility(oCallBackData);
          return;
        }

        var sSelectedModule = ContentUtils.getSelectedModuleId();
        var oCallBackData = {};
        if (sModuleId != sSelectedModule) {
          if (ContentScreenProps.screen.getThumbnailMode() == ContentScreenConstants.thumbnailModes.XRAY) {
            ContentScreenProps.screen.setThumbnailMode(ContentScreenConstants.thumbnailModes.BASIC);
          }
        }

        /**
         * Reset all filters data on module click.
         */
        FilterUtils.resetAllFilters();
        let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
        contextualAllCategoriesProps.reset();
        ContentScreenProps.screen.setIsSwitchDataLanguageDisabled(false);
        /**
         * Set selected module, default view mode and default zoom level
         */
        ContentUtils.setSelectedModuleAndDefaultDataById(sModuleId);

        if (sModuleId === "josstatus") {
          await JobStore.fetAllJobs();
        } else if (sModuleId === "dashboard") {
          JobStore.resetData();
          ContentUtils.setSelectedModuleById(sModuleId);
          /**
           * @description Required when module is clicked from content open view (PIM). - Ganesh
           * Comment removed: Back from content open view from dashboard
           * @private
           */
          let sDashboardTabId = DashboardScreenProps.getSelectedDashboardTabId();
          DashboardScreenStore.handleModuleItemClicked(sDashboardTabId);
          await this.handleTabLayoutViewTabChanged(sDashboardTabId, "dashboardScreen");
          _triggerChange();
        } else {
          ContentScreenProps.screen.setPreventDashboardDataReset(false);
          ContentScreenProps.goldenRecordProps.setShowGoldenRecordBuckets(false);
          JobStore.resetData();

          let  oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
          oCallBackData.checkAndShowAllNodes = oFilterStore.checkAndShowAllNodes;

          await ContentStore.handleModuleItemClicked(oCallBackData, oFilterContext);
        }
      };

      /** Maintain history and breadcrumb **/
      this.setBrowserStateForModule(sModuleId, fCallback);
    },

    handleEditCollectionButtonClicked: function () {
      ContentScreenProps.collectionViewProps.setIsEditCollectionScreen(true);
      _triggerChange();
    },

    handleAcrolinxSidebarToggled: function() {
      let bIsAcrolinxSidebarVisible = ContentScreenProps.screen.getIsAcrolinxSidebarVisible();
      ContentScreenProps.screen.setIsAcrolinxSidebarVisible(!bIsAcrolinxSidebarVisible);
      _triggerChange();
    },

    handleCollectionEditDialogButtonClicked: function (sContext, oFilterContext) {
      _handleCollectionEditDialogButtonClicked(sContext, oFilterContext);
    },

    setKpiBreadcrumbDataIntoFilterProps: function (oKpiData, oFilterContext) {
      let aBreadCrumbData = oKpiData.breadCrumbData;
      let sTaxonomyId = null;
      let sTypeId = null;
      let aTags = [];
      let oMasterTags = ContentScreenAppData.getDashboardReferencedTags();
      let sHeaderLabel = oKpiData.kpiLabel;
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      if (!CS.isEmpty(aBreadCrumbData)) {
        let oLastItem = aBreadCrumbData[aBreadCrumbData.length - 1];
        if (oLastItem) {
          CS.forEach(oLastItem.path, function (oPathItem) {
            if (oPathItem.type === "tag") {
              aTags.push({
                id: oPathItem.parentId,
                type: DictionaryForClassName.ENTITY_TAG,
                mandatory: [
                  {
                    id: oPathItem.typeId,
                    from: 100,
                    to: 100,
                    "type": "range",
                    "baseType": "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel"
                  }
                ]
              });
            }
            else if (oPathItem.type === "klass") {
              sTypeId = oPathItem.typeId;
            }
            else {
              sTaxonomyId = oPathItem.typeId;
            }
          });
        }
        sHeaderLabel += (" (" + oLastItem.label + ") ");
      }
      oKpiData.headerLabel = sHeaderLabel;
      oFilterStore.setTagsAsAppliedFilters(aTags, oMasterTags);
      sTaxonomyId && ContentScreenProps.filterProps.setSelectedParentTaxonomyIds([sTaxonomyId]);
      sTypeId && ContentScreenProps.filterProps.setSelectedTypes([sTypeId]);
    },

    handleShowKpiContentExplorerClicked: function (oKpiData) {
      let oFilterContext = {
        filterType: oFilterPropType.MODULE,
        screenContext: oFilterPropType.MODULE
      };
      ContentScreenProps.screen.setIsKpiContentExplorerOpen(true);
      ContentScreenProps.screen.setIsChooseTaxonomyVisible(false);
      this.setKpiBreadcrumbDataIntoFilterProps(oKpiData, oFilterContext);
      ContentScreenProps.screen.setKpiContentExplorerData(oKpiData); //sequence important as oKpiData is edited in setKpiBreadcrumbDataIntoFilterProps function
      let oCallbackData = {
        filterContext: oFilterContext
      };
      oCallbackData.breadCrumbData = BreadCrumbStore.createBreadcrumbItem(oKpiData.kpiId, oKpiData.kpiLabel,
          BreadCrumbModuleAndHelpScreenIdDictionary.EXPLORER_KPI_TILE, BreadCrumbModuleAndHelpScreenIdDictionary.EXPLORER_KPI_TILE, oFilterContext);
      ContentStore.handleExploreContentButtonClickedFromDashboard(oCallbackData);
    },

    handleRefreshKPIButtonClicked: function (oKpiData, oCallback) {
      _handleRefreshKPIButtonClicked(oKpiData, oCallback);
    },

    handleRefreshEndpoint: function (sEndpointId, sTileMode, oCallback) {
      _handleRefreshEndpoint(sEndpointId, sTileMode, oCallback);
    },

    handleDIOpenClicked: function (oEndpointData) {
      let oComponentProps = ContentUtils.getComponentProps();
      let fCallback = () => {
        let oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE
        };
        let sEndpointId = oEndpointData.id;
        let sEndpointType = oEndpointData.type;
        SessionProps.setSessionPreviousPhysicalCatalogId(SessionProps.getSessionPhysicalCatalogId());
        SessionProps.setSessionPhysicalCatalogData(PhysicalCatalogDictionary.DATAINTEGRATION);
        SessionProps.setSessionEndpointId(sEndpointId);
        SessionProps.setSessionEndpointType(sEndpointType);

        let functionToExecute = function () {
          let oCallbackData = {
            filterContext: oFilterContext
          };
          this.refreshAll("", oCallbackData);
          // TODO (FilterProps) : No need to call below function, because it already gets called from refreshAll function
          // ContentStore.fetchContentList(oCallbackData);
        };

        SessionProps.setActiveEndpointData(oEndpointData);
        let oCallback = {
          isForDataIntegration: true,
          functionToExecute: functionToExecute.bind(this)
        };
        GlobalStore.fetchMasterData(oCallback);
      };

      oComponentProps.endPointMappingViewProps.setSelectedPhysicalCatalogId(oEndpointData.physicalCatalogId);
      ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback);
    },

    handleContentWrapperViewBackButtonClicked: function (oFilterContext) {
      _handleContentWrapperViewBackButtonClicked(oFilterContext);
    },

    handleDashboardDataResetPrevented: function (bDashboardRecentPrevented) {
      ContentScreenProps.screen.setPreventDashboardDataReset(bDashboardRecentPrevented);
    },

    handleActiveCollectionSaveClicked: function (sContext, oFilterContext) {
      var oCallbackData = {
        filterContext: oFilterContext
      };
      switch (sContext) {
        case 'save':
          NewCollectionStore.handleCollectionSaved(oCallbackData);
          break;
        case 'discard':
          NewCollectionStore.discardActiveCollection();
          break;
      }

    },

    handleActiveCollectionLabelChanged: function (sLabel) {
      NewCollectionStore.handleActiveCollectionLabelChanged(sLabel);
    },

    handleActiveCollectionVisibilityChanged: function(){
      NewCollectionStore.handleActiveCollectionVisibilityChanged();
    },

    handleAddEntityButtonClicked: async function(sSectionId, oRelationshipSide, sContext){
      let oContent = ContentUtils.getActiveContent();
      if(oContent.contentClone) {
        alertify.error(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return true;
      }

      var oReferencedRelationshipContext = _getReferencedRelationshipContexts(sSectionId);
      let oFilterContext = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      };
      if(!CS.isEmpty(oReferencedRelationshipContext)){
        if(oReferencedRelationshipContext.type === "productVariant") {
          await ContentStore.getSide2NatureKlassFromNatureRelationship(sSectionId);
        }
        _setRelationshipContextData(sSectionId, oReferencedRelationshipContext, true, false);
        let bIsSelectVariant = ContentScreenProps.variantSectionViewProps.getSide2NatureClassOfProductVariant().selectVariant;
        _handleEmptyContextCreateOrLinkClick(oFilterContext, bIsSelectVariant);
        ContentScreenProps.screen.setIsRelationshipContextDialogOpen(true);
      } else{
        _addNewEntityInRelationshipVerticalMenuClicked(sSectionId, oRelationshipSide, sContext, oFilterContext);
      }
    },

    handleRelationshipPresentEntityOkButtonClicked: function (sContext) {
      //To clear QuickList Filter props
      let oNewFilterContextForQuickList = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      };
      let oFilterStore = FilterStoreFactory.getFilterStore(oNewFilterContextForQuickList);
      oFilterStore.resetFilterPropsByContext();

      if(sContext == "staticCollection"){
        _handleStaticCollectionAddViewCloseClicked();
      } else if(sContext == "contextEntity" || sContext == "manageVariantLinkedInstances") {
        _handleContextEntityAddViewCloseClicked();
      }
      else {
        _handleRelationshipPresentEntityOkButtonClicked();
      }
      ContentScreenProps.screen.emptySetSectionSelectionStatus();
    },

    handlePresentEntityDeleteButtonClicked: function (sSelectedSectionModelId, sViewContext, oFilterContext) {
      var _this = this;
      if(ContentUtils.getIsStaticCollectionScreen()){
        var aSelectedEntities = ContentUtils.getSelectedEntityList();
        var aSelectedEntityIds = CS.map(aSelectedEntities, "id");
        var aSelectedEntityNames = CS.map(aSelectedEntities, "name");
        ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_DELETE, aSelectedEntityNames,
            function () {
              _this.removeEntityFromCollection(aSelectedEntityIds, oFilterContext);
            }, function (oEvent) {
            });
      }
      else if( oComponentProps.variantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus() ||
          oComponentProps.variantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus()){
        var oVariant = ContentUtils.getActiveContentOrVariant();
        _this.handleDeleteAllVariantInstancesClicked(oVariant, oFilterContext);
      } else if (ContentScreenProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW) {
        let aSelectedTableEntities = ContentScreenProps.availableEntityViewProps.getSelectedRightPanelEntities();
        let aSelectedTableEntityIds = CS.map(aSelectedTableEntities, "id");
        let aSelectedTableEntityNames = CS.map(aSelectedTableEntities, "name");
        ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_DELETE, aSelectedTableEntityNames,
            function () {
              _removeTableEntitiesFromAvailableEntitiesView(aSelectedTableEntityIds, oFilterContext)
            }, function (oEvent) {
            });
      } else {
        _this.handleDeleteAllRelationshipEntityClicked(sSelectedSectionModelId, sViewContext);
      }
    },

    handlePresentEntitySelectAllButtonClicked: function (sRelationshipId, sViewContext) {
      if (ContentUtils.getRelationshipViewStatus()) {
        this.handleSelectAllRelationshipEntityClicked(sRelationshipId, sViewContext);
      } else if (ContentScreenProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW) {
        _handleTableLinkedEntitiesRightPanelSelectAll();
      } else {
        var bIsForPresentEntity = true;
        this.handleSelectAllContentClicked(bIsForPresentEntity);
      }
    },

    handleCreateNewCollectionButtonClicked: function (oCreationData, oFilterContext) {
      NewCollectionStore.handleCreateNewCollectionButtonClicked(oCreationData, { filterContext: oFilterContext });
    },

    handleModifyCollectionClicked: function (sCollectionId, sContext, oFilterContext) {
      NewCollectionStore.handleModifyCollectionClicked(sCollectionId, sContext, oFilterContext);
    },

    handleSingleContentAddToStaticCollectionClicked: function (sCollectionId, sContentId, oModel, oFilterContext) {
      NewCollectionStore.handleSingleContentAddToStaticCollectionClicked(sCollectionId, sContentId, oModel, oFilterContext);
    },

    handleThumbnailInformationViolationIconClicked: function(oEvent){
      _handleThumbnailInformationViolationIconClicked(oEvent)
    },

    handleThumbnailInformationEntityInfoIconClicked: function(sContentId, sVariantId){
      _handleThumbnailInformationEntityInfoIconClicked(sContentId, sVariantId);
    },

    handleCreateDynamicCollectionButtonClicked: function () {
      var bCallNextFunction = ContentUtils.allHierarchySafetyCheck();
      if(bCallNextFunction){
        NewCollectionStore.handleCreateDynamicCollectionButtonClicked();
      }
    },

    handleCreateStaticCollectionButtonClicked: function () {
      var bCallNextFunction = ContentUtils.allHierarchySafetyCheck();
      if(bCallNextFunction){
        NewCollectionStore.handleCreateStaticCollectionButtonClicked();
      }
    },

    handleCollectionSelected: function (oCollection, sContext, oFilterContext) {
      let fCallback = _handleCollectionSelected.bind(this, oCollection, sContext, {filterContext: oFilterContext});
      ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback);
    },

    handleNextCollectionClicked: function (sId) {
      NewCollectionStore.handleNextCollectionClicked(sId);
    },

    handleCollectionBreadcrumbReset: function () {
      NewCollectionStore.handleCollectionBreadcrumbReset();
      _triggerChange();
    },

    handleStaticCollectionRootBreadCrumbClicked: function () {
      NewCollectionStore.handleStaticCollectionRootBreadCrumbClicked();
      _triggerChange();
    },

    handleCollectionItemVisibilityModeChanged: function(sId, bIsDynamic, oFilterContext){
      NewCollectionStore.handleCollectionItemVisibilityModeChanged(sId, bIsDynamic, oFilterContext);
    },

    handleDeleteCollectionClicked: function (sId, sContext, oCallback) {
      oCallback = oCallback || {};
      var oCollectionViewProps  = ContentScreenProps.collectionViewProps;
      var oActiveCollection = oCollectionViewProps.getActiveCollection();
      var sConfirmationMessage = null;
      if(sContext == "staticCollection"){
        sConfirmationMessage = getTranslation().STATIC_COLLECTION_DELETE_CONFIRMATION;
      } else {
        sConfirmationMessage = getTranslation().DYNAMIC_COLLECTION_DELETE_CONFIRMATION;
      }
      if(!CS.isEmpty(oActiveCollection) && oActiveCollection.id == sId) {
        oCallback.functionToExecute = ContentStore.fetchContentList;
      }
      CustomActionDialogStore.showConfirmDialog(sConfirmationMessage, '',
          function () {
            NewCollectionStore.handleDeleteCollectionClicked(sId, sContext, oCallback);
          }, function (oEvent) {
          });
    },

    handleStaticCollectionQuickListOpened: async function (oFilterContext) {
      let sContext = "staticCollection";
      let oCallbackData = {
        filterContext: oFilterContext
      };

      oCallbackData.functionHandleAfterEffectsOfStaticCollectionNotFound = ContentStore.handleAfterEffectsOfStaticCollectionNotFound;
      oComponentProps.rightBarViewProps.setSelectedBarItemId("available_entity");
      oCallbackData.selectedContext = sContext;
      oCallbackData.functionToExecute = NewCollectionStore.handleStaticCollectionAddEntityButtonClicked;

      /**
       * In Collection QuickList to get present entities "COLLECTION" filterContext is required.
       * Resetting filter props of collection so that in quicklist all the contents from collection is visible.
       */
      let oActiveCollection = oComponentProps.collectionViewProps.getActiveCollection();
      let oNewFilterContextForCollection = {
        filterType: oFilterPropType.COLLECTION,
        screenContext: oActiveCollection.id
      };
      let oFilterStore = FilterStoreFactory.getFilterStore(oNewFilterContextForCollection);
      alertify.message(getTranslation().FILTERS_HAVE_BEEN_RESET);
      oFilterStore.resetFilterPropsByContext();
      let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oNewFilterContextForCollection.screenContext);
      contextualAllCategoriesProps.reset();

      let oBreadcrumb = ContentScreenProps.breadCrumbProps.getForwardBreadCrumbData()[oActiveCollection.id];
      let aPayloadData = oBreadcrumb.payloadData;
      CS.some(aPayloadData, (oPayloadData) => {
        let oPostData = oPayloadData.postData;
        if (oPostData) {
          oPostData.attributes = [];
          oPostData.clickedTaxonomyId = null;
          oPostData.selectedTaxonomyIds = [];
          oPostData.selectedTypes = [];
          oPostData.xrayEnabled = false;

          return true;
        }
      });

      let oCallback = {
        doNotAddBreadcrumb: true,
        isCollectionQuickListOpened: true,
        filterContext: oNewFilterContextForCollection
      };
      await _handleCollectionSelected(oActiveCollection, sContext, oCallback);
      return AvailableEntityStore.fetchAvailableEntities(oCallbackData);
    },

    handleMakeDefaultImageButtonClicked: function (sActiveAssetId) {
      ContentStore.handleMakeDefaultImageButtonClicked(sActiveAssetId);
    },

    handleEntitySnackBarStopShaking: function () {
      ContentUtils.setShakingStatus(false);
    },

    handleRelationshipContextNextButtonClicked: function (oFilterContext) {
      _handleRelationshipContextNextButtonClicked(oFilterContext);
    },

    handleRelationshipContextCreateVariantClicked: function () {
      if (_checkValidationForVariantContext()) {
        let fRelationshipContextCreateVariantFunction = CloneWizardStore.openCloneWizardDialog.bind(this, "productVariant");
        ContentStore.validateContextDuplication({functionToExecute : fRelationshipContextCreateVariantFunction});
      }
    },

    handleCreateLinkedVariantClicked: function (sContext) {
      let fCreateLinkedVariantFunction = CloneWizardStore.openCloneWizardDialog.bind(this, sContext);
      if (ContentUtils.isContextTypeProductVariant(sContext)) {
        ContentStore.validateContextDuplication({functionToExecute : fCreateLinkedVariantFunction});
      }
      else {
        fCreateLinkedVariantFunction();
      }
    },

    handleRelationshipContextCancelButtonClicked: function (sCancelButtonId) {
      let oCallbackData = {
        functionToExecute: ContentStore.discardChangesInActiveContent
      };
      ContentRelationshipStore.handleRelationshipContextCancelButtonClicked(sCancelButtonId, oCallbackData);
    },

    handleRelationshipContextSaveButtonClicked: function (sRelationshipId) {
      ContentRelationshipStore.handleRelationshipContextSaveButtonClicked(sRelationshipId);
    },

    handleEntityTabClicked: function (sTabId) {
      if (ContentUtils.contextTableSafetyCheck()) {
        return;
      }
      ContentStore.handleEntityTabClicked(sTabId);
    },

    handleContentEditToolbarButtonClicked: function (sId) {
      let oActiveContent = ContentUtils.getActiveContent();
      if (ContentUtils.getSelectedHierarchyContext() === HierarchyTypesDictionary.TAXONOMY_HIERARCHY && CS.isEmpty(oActiveContent)) {
        let oFilterContext = {
          filterType: oFilterPropType.HIERARCHY,
          screenContext: HierarchyTypesDictionary.TAXONOMY_HIERARCHY
        };
        let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
        let oTaxonomySections = oFilterProps.getTaxonomyNodeSections();
        if (oTaxonomySections && oTaxonomySections.clonedObject) {
          _handleTaxonomySectionEditToolbarButtonClicked(sId, oFilterContext);
        }
      }
      else {
        _handleContentEditToolbarButtonClicked(sId);
      }
    },

    handleAdvancedSearchListItemNodeClicked: function (sItemId, sType, sContext, oFilterContext) {
      _handleAdvancedSearchListItemNodeClicked(sItemId, sType, sContext, oFilterContext);
    },

    handleAdvancedSearchListLoadMoreClicked: function (sType, sContext) {
      ContentStore.handleAdvancedSearchListLoadMoreClicked(sType, sContext);
    },

    handleAdvancedSearchListSearched: function (sSearchText, sContext) {
      ContentStore.handleAdvancedSearchListSearched(sSearchText, sContext);
    },

    handleAdvancedSearchFilterButtonCLicked: function (sSelectedHierarchyContext, oFilterContext) {
      var oFilterProps = ContentUtils.getFilterProps(oFilterContext);
      var oCheckData = _checkForEmptyFilterValue(oFilterContext);

      if(oCheckData.isEmptyFilter || !CS.isEmpty(oCheckData.attributeChildEmptyNames)){
        if(oCheckData.isEmptyFilter){
          var sFilterNames = oCheckData.filterNames.join(" , ");
          alertify.error(getTranslation().ERROR_EMPTY_ADVANCE_FILTER_VALUE + " [ " + sFilterNames + " ]", 0);
        }

        if(!CS.isEmpty(oCheckData.attributeChildEmptyNames)){
          var sAttributeFilterNames = oCheckData.attributeChildEmptyNames.join(" , ");
          alertify.error(getTranslation().ERROR_EMPTY_ADVANCE_FILTER_ATTRIBUTE_CHILDREN + " [ " + sAttributeFilterNames + " ]", 0);
        }
        return;

      }else {
        this.toggleAdvancedSearchDialogVisibility(oFilterContext);
        oFilterProps.setIsAdvanceSearchFilterClickedStatus(true);

        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.resetPaginationOnApplyingFilter();

        var sSearchText = ContentScreenProps.screen.getEntitySearchText();
        if(!CS.isEmpty(sSearchText)) {
          ContentStore.handleAdvancedSearchListSearched("");
        }

        var bIsAnyHierarchySelectedExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
        if (ContentUtils.getAvailableEntityViewStatus() || bIsAnyHierarchySelectedExceptFilterHierarchy) {
          ContentStore.handleAvailableEntityFilterButtonClicked({filterContext: oFilterContext}, {}, sSelectedHierarchyContext)
        } else if (oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
          GoldenRecordStore.handleFilterButtonClicked(oFilterContext);
        } else {
          ContentStore.handleFilterButtonClicked({filterContext: oFilterContext});
        }
      }
    },

    handleXRayPropertyClicked: function (aProperties,sRelationshipId, sRelationshipIdToFetchData, oFilterContext) {
      ContentStore.handleXRayPropertyClicked(aProperties,sRelationshipId, sRelationshipIdToFetchData, oFilterContext);
    },

    getXRayProperties: function() {
      return ContentStore.getXRayProperties();
    },

    handleShowXRayPropertyGroupsClicked: function (bMakeDefaultSelected, bIsNotForXRay, oFilterContext) {
      ContentStore.handleShowXRayPropertyGroupsClicked(bMakeDefaultSelected, bIsNotForXRay, oFilterContext);
    },

    handleXRayPropertyGroupClicked: function (sId, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
      ContentStore.handleXRayPropertyGroupClicked(sId, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext);
    },

    handleCloseActiveXRayPropertyGroupClicked: function (sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
      ContentStore.handleCloseActiveXRayPropertyGroupClicked(sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext);
    },

    handleCircledTagNodeClicked: function (sTagGroupId, sTagId, sContext, oExtraData) {
      ContentStore.handleCircledTagNodeClicked(sTagGroupId, sTagId, sContext, oExtraData);
    },

    handleNatureThumbDeleteClicked: function (oModel) {
      let oActiveEntity = ContentUtils.getActiveContent();
      if(oActiveEntity.contentClone) {
        ContentUtils.showMessage(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return;
      }

      ContentRelationshipStore.deleteRelationshipElement(oModel);
    },

    handleNatureThumbViewClicked: function (oModel, oFilterContext) {
      this.handleThumbnailOpenClicked(oModel, oFilterContext);
    },

    handleNatureThumbSelectClicked: function (oModel){
      this.toggleThumbnailSelection(oModel);
      _triggerChange();
    },

    handleBundleToolbarItemClicked: function (sButtonContext, sSideId, sViewContext) {
      _handleBundleToolbarItemClicked(sButtonContext, sSideId, sViewContext);
    },

    handleNatureAddEntityButtonClicked: function (sNatureRelationshipId, oRelationshipSide) {
      let oActiveEntity = ContentUtils.getActiveContent();
      if(oActiveEntity && oActiveEntity.contentClone) {
        ContentUtils.showMessage(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return;
      }

      let oFilterContext = {
        screenContext: oFilterPropType.QUICKLIST,
        filterType: oFilterPropType.QUICKLIST
      };
      var sContext = "natureRelationship";
      _addNewEntityInRelationshipVerticalMenuClicked(sNatureRelationshipId, oRelationshipSide, sContext, oFilterContext);
    },

    handleAssignImageToVariantButtonClicked: function (sVariantId, sAssetId, sContext) {
      ContentStore.handleAssignImageToVariantButtonClicked(sVariantId, sAssetId, sContext);
    },

    handleDeleteGeneratedAssetLinkClicked: function () {
      CustomActionDialogStore.showConfirmDialog(getTranslation().DELETE_CONFIRMATION, "",
          function () {
            ContentStore.handleDeleteGeneratedAssetLinkClicked();
          }, function (oEvent) {
          });
    },

    handleVariantCloseImageButtonClicked: function (sContext) {
      ContentStore.handleVariantCloseImageButtonClicked(sContext);
    },

    handleImageGalleryDialogViewVisibilityStatusChanged: function () {
      if(ContentUtils.activeTaskSafetyCheck()){
        ContentStore.handleImageGalleryDialogViewVisibilityStatusChanged();
      }
    },

    handleTaskDialogOpenClicked: function (oProperty) {
      ContentStore.handleTaskDialogOpenClicked(oProperty);
    },

    resetTaskData: function () {
      ContentScreenProps.taskProps.setTaskData({});
      _triggerChange();
    },

    handleVariantLinkedEntitiesRemoveEntityClicked: function(sLinkedInstanceId, sContextEntity){
      _handleVariantLinkedEntitiesRemoveEntityClicked(sLinkedInstanceId, sContextEntity);
    },

    handleVariantLinkedEntitiesAddEntityClicked: function (sSelectedEntity, oFilterContext){
      _handleVariantLinkedEntitiesAddEntityClicked(sSelectedEntity, oFilterContext);
    },

    handleEntityDetailComparisionDialogButtonClick: (sButtonId, oFilterContext)=> {
      switch (sButtonId) {
        case "save":
          _saveTimeLineComparisonDialogButtonClicked(oFilterContext);
          break;
        case "cancel":
          _handleDiscardUnSavedContentsButtonClicked();
          break;
        case "ok":
          _handleTimelineComparisionDialogOKButtonClicked(oFilterContext);
          break;
      }
    },

    handleVariantTagGroupDateValueChanged: function (sKey, sValue) {
      if(ContentUtils.getIsUOMVariantDialogOpen()){
        VariantStore.handleVariantTagGroupDateValueChanged(sKey, sValue);
      }else {
        ContentRelationshipStore.handleVariantTagGroupDateValueChanged(sKey, sValue);
      }
      _triggerChange();
    },

    handleDateRangePickerDateChange: function(sContext, oSelectedTimeRange){
      ContentStore.handleDateRangePickerDateChange(sContext, oSelectedTimeRange);
    },

    handleDateRangePickerCancelClicked: function (sContext) {
      ContentStore.handleDateRangePickerCancelClicked(sContext)
    },

    handleTaxonomySectionInputChanged: function (sSectionId, sElementId, sProperty, sNewValue, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleTaxonomySectionInputChanged(sSectionId, sElementId, sProperty, sNewValue);
    },

    handleGridViewSelectButtonClicked: function (aSelectedContentIds, bSelectAllClicked) {
      let oContentGridProps = oComponentProps.contentGridProps;
      let oGoldenRecordProps = oComponentProps.goldenRecordProps;
      let oScreenProps = oComponentProps.screen;
      if(oContentGridProps.getIsContentGridEditViewOpen()){
        ContentGridStore.handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
      }
      else if(oGoldenRecordProps.getIsMatchAndMergeViewOpen()){
        GoldenRecordStore.handleGoldenRecordGridViewLanguageSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
      }
      else if(oScreenProps.getIsEditMode()){
        ContentStore.handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
      }

      if (oComponentProps.bulkAssetLinkSharingProps.getShowAssetLinkSharingDialog()) {
        BulkAssetLinkSharingStore.handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
      } else if(ContentScreenProps.goldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked()) {
        GoldenRecordStore.handleGoldenRecordGridViewLanguageSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
      }
    },

    handleBulkAssetLinkSharingActionItemClicked: function (sButtonId, oAssetDownloadData) {
      switch (sButtonId) {
        case "share":
          BulkAssetLinkSharingStore.handleAssetShareButtonClicked(oAssetDownloadData);
          break;
        case "cancel":
          BulkAssetLinkSharingStore.handleLinkSharingCancelButtonClicked();
          break;
      }
    },

    handleGridViewActionItemClicked: function (sActionItemId, sContentId) {
      let oContentGridProps = oComponentProps.contentGridProps;
      if (oContentGridProps.getIsContentGridEditViewOpen()) {
        ContentGridStore.handleGridViewActionItemClicked(sActionItemId, sContentId);
      } else if (ContentScreenProps.screen.getIsEditMode()) {
        _handleSectionGridViewActionEntityClicked(sActionItemId, sContentId);
      }
    },

    handleGridViewDeleteButtonClicked: function () {
      if(oComponentProps.contentGridProps.getIsContentGridEditViewOpen()){
        ContentGridStore.handleGridViewDeleteButtonClicked();
      }
    },

    handleGridPaginationChanged: function (oNewPaginationData) {
      let oActiveEntity = ContentUtils.getActiveEntity();
      let sSelectedTabId = ContentUtils.getSelectedTabId();
      let oSelectedContext = ContentScreenProps.screen.getSelectedContext();
      if (!CS.isEmpty(oActiveEntity) && sSelectedTabId == "relationship_tab") {
        ContentRelationshipStore.loadMoreRelationship(oSelectedContext.sectionId, "", oNewPaginationData);
      }
    },

    handleGridViewSearchTextChanged: function (sSearchText) {
      if(oComponentProps.contentGridProps.getIsContentGridEditViewOpen()) {
        ContentGridStore.handleGridViewSearchTextChanged(sSearchText);
      }
      _triggerChange();
    },

    handleGridViewSaveButtonClicked: function () {
    },

    handleAttributeContextViewShowVariantsClicked: function (sAttributeId, sAttributeVariantContextId, oFilterContext, sVariantInstanceId, sParentContextId) {
      if(ContentUtils.isActiveContentDirty()) {
        ContentUtils.showMessage(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return;
      }
      if (ContentUtils.activeContentSafetyCheck()) {
        VariantStore.handleAttributeContextViewShowVariantsClicked(sAttributeVariantContextId, sAttributeId, sVariantInstanceId, sParentContextId);
      }
    },

    handleAttributeContextViewDialogButtonClick: function (sAttributeContextId, sButtonId) {
      VariantStore.handleAttributeContextViewDialogButtonClick(sAttributeContextId, sButtonId);
    },

    handleVariantDialogSaveClicked: function (sContext, oFilterContext) {
      VariantStore.handleVariantDialogSaveClicked("", "", oFilterContext);
    },

    handleVariantDialogChangeAttributeValue: function (sContext, oData) {
      VariantStore.handleVariantDialogChangeAttributeValue(oData);
    },

    handleVariantDialogCancelClicked: function (sContext) {
      VariantStore.handleVariantDialogCancelClicked();
    },

    handleTableEditButtonClicked: function (sId, sContext, sTableContextId) {
      if (ContentUtils.activeContentSafetyCheck()) {
        _handleTableEditButtonClicked(sId, sContext, sTableContextId);
      }
    },

    handleTableDeleteButtonClicked: function (sId, sContext, sTableContextId, oFilterContext) {
      if(ContentUtils.contextTableSafetyCheck()){
        return;
      }
      else if (ContentUtils.activeContentSafetyCheck()) {
        _handleTableDeleteButtonClicked(sId, sContext, sTableContextId, false, oFilterContext);
      }
    },

    handleTableOpenButtonClicked: function (sId, sContext) {
      if(ContentUtils.contextTableSafetyCheck()){
        return;
      }
      else if (ContentUtils.activeContentSafetyCheck()) {
        let fCallback = _handleTableOpenButtonClicked.bind(this, sId, sContext);
        ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback);//Required- To set isPushState = true for Context.
      }
    },

    handleTableRowSelectionChanged: function (sAttributeId, sId, sContext, sValue) {
      switch (sContext) {}
    },

    handleTableCellValueChanged: function (oCellData, sContext, sTableContextId) {
        _handleTableCellValueChanged(oCellData, sContext, sTableContextId);
    },

    handleTableAddLinkedInstance: function (sContext, oFilterContext, oViewData) {
      _handleTableLinkedEntitiesAddEntityClicked(oFilterContext, oViewData);
    },

    handleGridViewRemoveLinkedInstance: function (oViewData, aSelectedItems) {
      VariantStore.handleRemoveLinkedInstanceClicked(oViewData, aSelectedItems);
    },

    handleTableSetActivePopOverVariant: function (sContext, sContextInstanceId, sContextId, sEntity, bIsTransponse) {
    },

    handleTableClearActivePopOverVariant: function () {
      VariantStore.handleClearActivePopOverVariant();
    },

    handleTableRemoveLinkedInstance: function (sLinkedInstanceId) {
    },

    handleGridViewDiscardButtonClicked: function () {
    },

    handleGridViewResizerButtonClicked: function (iWidth, sId, sTableContextId) {
      _handleGridViewResizerButtonClicked(iWidth, sId, sTableContextId)
    },

    handleGridOrganizeColumnButtonClicked: function (sContextId) {
      _handleGridOrganizeColumnButtonClicked(sContextId);
      _triggerChange();
    },

    handleColumnOrganizerDialogButtonClicked: function (sButtonId, sContext, aColumns) {
      _handleColumnOrganizerDialogButtonClicked(sButtonId, sContext, aColumns);
    },

    handleGridTagPropertyValueChanged: function(sContentId, sTagId, aTagValueRelevanceData){
      if (oComponentProps.contentGridProps.getIsContentGridEditViewOpen()) {
        ContentGridStore.handleGridTagPropertyEdit(sContentId, sTagId, aTagValueRelevanceData);
      }
      _triggerChange();
    },

    handleGridPropertyValueChanged: function (sContentId, sPropertyId, sValue) {
      let oContentGridProps = oComponentProps.contentGridProps;
      let oGoldenRecordProps = oComponentProps.goldenRecordProps;

      if(oContentGridProps.getIsContentGridEditViewOpen()){
        ContentGridStore.handleGridPropertyAttributeValueChanged(sContentId, sPropertyId, sValue);
      }
      else if(oGoldenRecordProps.getIsMatchAndMergeViewOpen()){
        GoldenRecordStore.handleGoldenRecordGridViewDefaultLanguageSelectButtonClicked(sContentId, sPropertyId, sValue);
      }
      else {
        _handleGridPropertyByActiveContent();
      }
      _triggerChange();
    },

    handleGridPropertyParentExpandToggled: function (sContentId) {
      if (sContentId === BulkAssetDownloadDictionary.VARIANT_ASSETS) {
        BulkAssetLinkSharingStore.handleGridExpandToggled(sContentId);
        _triggerChange();
      }
    },

    handleGridPropertyFilterClicked: function (sContentId, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleCollectionFilterButtonClicked(sContentId);
      _triggerChange();
    },

    handleThumbnailRelationshipEditVariantIconClicked: function(oContext, oModel) {
      ContentRelationshipStore.handleThumbnailRelationshipEditVariantIconClicked(oContext, oModel);
    },

    handleContentHorizontalTreeNodeClicked: function (oReqData, oFilterContext) {
      _toggleHierarchyScrollEnableDisableProp();
      var sSelectedContext = oReqData.selectedContext;
      switch(sSelectedContext){
        case HierarchyTypesDictionary.TAXONOMY_HIERARCHY:
          if (ContentUtils.activeTaxonomyOrganiseSafetyCheck(oFilterContext)) {
            CollectionAndTaxonomyHierarchyStore.handleContentTaxonomyHorizontalTreeNodeClicked(oReqData, oFilterContext);
          } else {
            alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
          }
          break;

        case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
          if(ContentUtils.activeCollectionHierarchySafetyCheck()){
            var sViewMode = "thumbnailViewMode";
            var bIsNodeClickCall = true;
            CollectionAndTaxonomyHierarchyStore.handleCollectionHierarchyViewModeToggled(sViewMode, oReqData, bIsNodeClickCall, oFilterContext);
          }
          break;
      }
    },

    handleContentHorizontalTreeNodeCollapseClicked: function(oReqData){
      var sSelectedContext = oReqData.selectedContext;
      switch(sSelectedContext){
        case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
          ContentStore.handleContentHorizontalTreeNodeCollapseClickedChildrenRemove(oReqData);
          break;

        case HierarchyTypesDictionary.TAXONOMY_HIERARCHY:
          ContentStore.handleContentHorizontalTreeNodeCollapseClickedNoChildrenRemove(oReqData);
          break;
      }
    },

    handleContentHorizontalTreeNodeRightClicked: function (oReqData) {
      var oScreenProps = ContentScreenProps.screen;
      oScreenProps.setRightClickedHierarchyTreeNodeData(oReqData);
      _triggerChange();
    },

    handleContentHorizontalTreeNodePopoverClosed: function () {
      var oScreenProps = ContentScreenProps.screen;
      oScreenProps.setRightClickedHierarchyTreeNodeData({});
      _triggerChange();
    },

    handleContentHorizontalTreeNodeContextMenuItemClicked: function (sItemId, oFilterContext) {
      switch (sItemId){
        case "paste":
          _handleHierarchyNodeContextItemMenuPasteClicked(oFilterContext);
          break;
      }
    },

    handleBulkEditIconClicked : function () {
      /**If Properties Tab doesn't have permission, then Taxonomy or Classes Tab will be the active tab, for which backend call is needed.*/
      let oFunctionPermission = ContentScreenProps.screen.getFunctionalPermission();
      if (!oFunctionPermission.canBulkEditProperties && oFunctionPermission.canBulkEditTaxonomies) {
        BulkEditStore.handleBulkEditHeaderToolbarButtonClicked(BulkEditTabTypesConstants.TAXONOMIES);
      } else if (!oFunctionPermission.canBulkEditProperties && oFunctionPermission.canBulkEditClasses) {
        BulkEditStore.handleBulkEditHeaderToolbarButtonClicked(BulkEditTabTypesConstants.CLASSES);
      }

      BulkEditStore.openBulkEditView();
    },

    handleOrganiseTaxonomyButtonClicked: function (oFilterContext) {

      let fCallback = () => {
        /*
        - Due to implementation of context based filter props which preserves the data
          on switching to new window, deleted old code related to resetting taxanomies and filter props on ORGANIZE click.
        */

        let oNewFilterContextForTaxonomyHierarchy = {
          filterType: oFilterPropType.HIERARCHY,
          screenContext: HierarchyTypesDictionary.TAXONOMY_HIERARCHY
        };
        let oNewFilterStore = FilterStoreFactory.getFilterStore(oNewFilterContextForTaxonomyHierarchy);
        let sId = ContentUtils.getSelectedHierarchyContext() || BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY;
        let oCallBackData = {
          filterContext: oNewFilterContextForTaxonomyHierarchy,
          breadCrumbData: BreadCrumbStore.createBreadcrumbItem(sId, ContentUtils.getDecodedTranslation(getTranslation().ENTITY_HIERARCHY,{ entity : getTranslation().TAXONOMY }), BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY, BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY, oNewFilterContextForTaxonomyHierarchy)
        }

        let oActiveContent = ContentUtils.getActiveContent();
        if (oActiveContent && oActiveContent.contentClone) {
          oActiveContent.isDirty = false;
          delete oActiveContent.contentClone;
        }
        ContentUtils.setViewMode(ContentUtils.getDefaultViewMode());
        let oTaxonomyTreeRequestData = {
          url: getRequestMapping().GetOrganizeScreenTree
        };
        ContentStore.handleFilterButtonClicked(oCallBackData)
          .then(ContentScreenProps.screen.setIsTaxonomyHierarchySelected.bind(this, true)) //to get all modules before fetching taxonomies
          .then(oNewFilterStore.fetchTaxonomyTreeData.bind(oNewFilterStore, false, {}, {taxonomyTreeRequestData: oTaxonomyTreeRequestData})) // need to fetch taxonomies for new context.
            .then(() => {
              AllCategoriesSelectorStore.resetDirtyProps(oFilterContext);
              CollectionAndTaxonomyHierarchyStore.handleOrganiseTaxonomyButtonClicked( oNewFilterContextForTaxonomyHierarchy);
            })
          .catch(
            function (oException) {
              ExceptionLogger.error(oException);
            }
          );
      };

      ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback);

    },

    handleStaticCollectionOrganiseButtonClicked: function (sClickedNodeId, oFilterContext) {
      let fCallback = () => {
        let bIsNoTrigger = true;
        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.handleSelectedTaxonomiesClearAllClicked(bIsNoTrigger); //Clear appliedTaxonomies.
        oFilterStore.handleClearAllAppliedFilterClicked(bIsNoTrigger); //Clear appliedFilters.
        CollectionAndTaxonomyHierarchyStore.handleStaticCollectionOrganiseButtonClicked(sClickedNodeId, oFilterContext);
      };
      ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback);
    },

    handleRuntimePCDeleteClicked: function (sContext, sSectionId, sCallingContext, sSectionLabel) {
      switch (sCallingContext){
        case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
          ContentUtils.listModeConfirmation(getTranslation().SECTION_REMOVE_CONFIRMATION, [sSectionLabel],
              function () {
                CollectionAndTaxonomyHierarchyStore.handleCollectionPCDeleted(sSectionId, sSectionLabel);
              }, function (oEvent) {
              });

          break;
      }
    },

    handleRuntimeSectionSkipToggled: function (sContext, sSectionId, sCallingContext, sSectionLabel) {
      switch (sCallingContext){
        case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
          CollectionAndTaxonomyHierarchyStore.handleSectionSkipToggled(sSectionId, sSectionLabel);
          break;
      }
    },

    handleRuntimePCElementInputChanged: function (sSectionId, sElementId, sProperty, sNewValue, sCallingContext, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      switch (sCallingContext){
        case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
          CollectionAndTaxonomyHierarchyStore.handleCollectionPCElementInputChanged(sSectionId, sElementId, sProperty, sNewValue);
          break;
        case HierarchyTypesDictionary.TAXONOMY_HIERARCHY:
          oFilterStore.handleTaxonomySectionInputChanged(sSectionId, sElementId, sProperty, sNewValue);
          break;
      }
    },

    handleTaxonomyShowPopOverStateChanged: function (bShowTaxonomy, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oFilterStore.handleTaxonomyShowPopOverStateChanged(bShowTaxonomy);
    },

    handleTaxonomyHierarchyViewModeToggled: function (sViewMode, oFilterContext) {
      CollectionAndTaxonomyHierarchyStore.handleTaxonomyHierarchyViewModeToggled(sViewMode, null, { filterContext: oFilterContext });
    },

    handleTaxonomyHierarchyExpansionToggled: function (sSectionId, oFilterContext) {
      ContentStore.handleTaxonomyHierarchyExpansionToggled(sSectionId, oFilterContext);
    },

    handleThumbnailRightClick: function (oModel) {
      var oScreenProps = ContentScreenProps.screen;
      oScreenProps.setRightClickedThumbnailModel(oModel);
      _triggerChange();
    },

    handleThumbnailContextMenuPopOverClosed: function () {
      var oScreenProps = ContentScreenProps.screen;
      oScreenProps.setRightClickedThumbnailModel({});
      _triggerChange();
    },

    handleThumbnailContextMenuItemClicked: function (sMenuItemId) {
      switch(sMenuItemId){
        case "copy":
          _handleThumbContextMenuCopyOrCutClicked("copy");
          break;
        case "cut":
          _handleThumbContextMenuCopyOrCutClicked("cut");
          break;
      }
    },

    handleLoadMoreFilterData: function (oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      let oFilterProps = oFilterStore.getFilterProps();
      let oRequestData = {
        isLoadMore: true
      };
      oFilterProps.setShowDetails(false);
      oFilterStore.handleFilterShowDetailsClicked({filterContext : oFilterContext}, oRequestData);
    },

    handleEndpointMappedElementChanged: function (sId, sMappedElement, sOptionType, oReferencedData) {
      OnboardingStore.handleEndpointMappedElementChanged(sId, sMappedElement, sOptionType, oReferencedData);
    },

    handleProfileUnmappedElementChanged: function (sName, sMappedElement, sOptionType, oReferencedData) {
      OnboardingStore.handleProfileUnmappedElementChanged(sName, sMappedElement, sOptionType, oReferencedData);
    },

    handleProfileConfigMappedTagValueChanged: function (sId, sMappedTagValueId, sNewValue, sTagGroupId, oReferencedTagValues) {
      OnboardingStore.handleProfileConfigMappedTagValueChanged(sId, sMappedTagValueId, sNewValue, sTagGroupId, oReferencedTagValues);
    },

    handleEndpointTagValueIgnoreCaseToggled: function (sId, sMappedTagValueId) {
      OnboardingStore.handleEndpointTagValueIgnoreCaseToggled(sId, sMappedTagValueId);
    },

    handleEndpointIsIgnoredToggled: function (sId, sTabId) {
      OnboardingStore.handleEndpointIsIgnoredToggled(sId, sTabId);
    },

    handleEndpointUnmappedElementIsIgnoredToggled: function (sId, sTabId) {
      OnboardingStore.handleEndpointUnmappedElementIsIgnoredToggled(sId, sTabId);
    },

    handleEndPointMappingViewBackButtonClicked: function () {
      let sSessionPhysicalCatalogId = SessionProps.getSessionPhysicalCatalogId();

      let fFunctionToExecute = () => {
        if (sSessionPhysicalCatalogId === PhysicalCatalogDictionary.PIM) {
          SessionProps.setSessionEndpointId("");
          SessionProps.setSessionEndpointType("");
        }
        ContentStore.handleRefreshContentButtonClicked();
      };

      let oCallbackData = {
        functionToExecute: fFunctionToExecute
      };

      OnboardingStore.handleEndPointMappingViewBackButtonClicked(oCallbackData);
    },

    handleEndPointMappingViewImportButtonClicked: function () {
      let oCallback = {};
      let sPhysicalCatalogId = SessionProps.getSessionPhysicalCatalogId();
      if (sPhysicalCatalogId === PhysicalCatalogDictionary.PIM) {
        SessionProps.setSessionPhysicalCatalogId(PhysicalCatalogDictionary.DATAINTEGRATION);
        oCallback.functionToExecute = () => {
          SessionProps.setSessionEndpointId("");
          SessionProps.setSessionEndpointType("");
          SessionProps.setSessionPhysicalCatalogData(PhysicalCatalogDictionary.PIM);
        };
      }
      try {
        OnboardingStore.handleEndPointMappingViewImportButtonClicked(oCallback);
      }
      catch (oException) {
        if (oException.message === "UNMAPPED_FIELDS") {
          if (sPhysicalCatalogId === PhysicalCatalogDictionary.PIM) {
            SessionProps.setSessionPhysicalCatalogId(sPhysicalCatalogId);
          }
        }
      }
    },

    handleEndPointMappingTabClicked: function (sTabId) {
      OnboardingStore.handleEndPointMappingTabClicked(sTabId);
    },

    handleEndPointMappingFilterOptionChanged : function (sFilterId) {
      OnboardingStore.handleEndPointMappingFilterOptionChanged(sFilterId);
    },

    handleJobItemClicked: function (sJobId) {
      JobStore.handleJobItemClicked(sJobId);
    },

    handleProcessDataDownloadButtonClicked: function (sButtonId) {
      JobStore.handleProcessDataDownloadButtonClicked(sButtonId);
    },

    handleProcessInstanceDownloadButtonClicked: function (iProcessInstanceId, iInstanceIID) {
      JobStore.handleProcessInstanceDownloadButtonClicked(iProcessInstanceId, iInstanceIID);
    },

    handleProcessInstanceDialogButtonClicked: function (sButtonId) {
      JobStore.handleProcessInstanceDialogButtonClicked(sButtonId);
    },

    handleProcessGraphRefreshButtonClicked: function (bShowLoading) {
      JobStore.handleProcessGraphRefreshButtonClicked(bShowLoading);
    },

    handleTimelineZoomButtonClicked: function (bZoomIn) {
      TimelineStore.handleZoomClicked(bZoomIn);
    },

    handleTimelineDeleteVersionZoomButtonClicked: function (sVersionId) {
      TimelineStore.handleTimelineDeleteVersionZoomButtonClicked(sVersionId);
    },

    handleTimelineCompareVersionButtonClicked: function (bIsComparisonMode, aSelectedVersions) {
      TimelineStore.handleTimelineCompareVersionButtonClicked(bIsComparisonMode, aSelectedVersions);
    },

    handleTimelineVersionLoadMoreButtonClicked: function (oFilterContext) {
      TimelineStore.handleTimelineVersionLoadMoreButtonClicked(oFilterContext);
    },

    handleTimelineVersionDeselectAllButtonClicked: function(){
      TimelineStore.handleTimelineVersionDeselectAllButtonClicked();
    },

    handleTimelineVersionSelectAllButtonClicked: function () {
      TimelineStore.handleTimelineVersionSelectAllButtonClicked();
    },

    handleTimelineVersionDeleteSelectedButtonClicked: function () {
      TimelineStore.handleTimelineVersionDeleteSelectedButtonClicked();
    },

    handleTimelineCompareVersionCloseButtonClicked: function () {
      TimelineStore.handleTimelineCompareVersionCloseButtonClicked();
    },

    handleGoldenRecordDialogActionButtonClicked: function (sButtonId) {
      GoldenRecordStore.handleGoldenRecordDialogActionButtonClicked(sButtonId);
    },

    handleTimelineSelectVersionButtonClicked: function (sVersionId) {
      TimelineStore.handleTimelineSelectVersionButtonClicked(sVersionId);
    },

    handleTimelineVersionShowArchiveButtonClicked: function (bArchiveFlag, oFilterContext) {
      TimelineStore.handleTimelineVersionShowArchiveButtonClicked(bArchiveFlag, false, oFilterContext);
    },

    handleTimelineRestoreArchiveVersionsButtonClicked: function () {
      TimelineStore.handleTimelineRestoreArchiveVersionsButtonClicked();
    },

    handleTimelineRestoreVersionButtonClicked: function (sVersionId) {
      TimelineStore.handleTimelineRestoreVersionButtonClicked(sVersionId);
    },

    handleTimelineRollbackVersionButtonClicked: function (sVersionId) {
      TimelineStore.handleTimelineRollbackVersionButtonClicked(sVersionId);
    },

    handleContentComparisonBackButtonClicked: function () {
      ContentStore.handleContentComparisonBackButtonClicked();
    },

    handleGoldenRecordComparisonCloseButtonClicked: function () {
      GoldenRecordProps.setIsGoldenRecordViewSourcesDialogOpen(false);
      _triggerChange();
    },

    handleGridEditButtonClicked: function () {
      ContentGridStore.handleGridEditButtonClicked();
    },

    handleContentHorizontalTreeNodeAddNewNode: function (sParentId, sNewLabel, oFilterContext) {
      var oScreenProps = ContentScreenProps.screen;
      oScreenProps.setIsHierarchyTreeScrollAutomaticallyEnabled(true);

      var sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();
      switch (sSelectedHierarchyContext){
        case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
          var oCallbackData = {
            filterContext: oFilterContext
          };
          oCallbackData.functionToExecute = CollectionAndTaxonomyHierarchyStore.updateHierarchyTreeAfterAddingCollectionNode.bind(this, sParentId, oFilterContext);
          NewCollectionStore.createStaticCollectionAccordingToMode("no_content", sNewLabel, sParentId, false, oCallbackData);
          break;
      }
    },

    toggleHierarchyScrollEnableDisableProp: function () {
      _toggleHierarchyScrollEnableDisableProp();
    },

    handleContentHorizontalTreeNodeDeleteIconClicked: function (sIdToDelete, oFilterContext) {
      var sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();
      switch (sSelectedHierarchyContext){
        case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
          var oCallbackData = {};
          oCallbackData.filterContext = oFilterContext;
          oCallbackData.functionToExecute = CollectionAndTaxonomyHierarchyStore.handleCollectionHierarchyNodeDeleteAfterEffects.bind(this, sIdToDelete);
          this.handleDeleteCollectionClicked(sIdToDelete, "staticCollection", oCallbackData);
          break;
      }
    },

    handleContentHorizontalTreeNodeViewVisibilityModeChanged: function(sCollectionId, oFilterContext){
      NewCollectionStore.handleCollectionItemVisibilityModeChanged(sCollectionId, false, oFilterContext);
    },

    handleExportToMDMButtonClicked: function () {
      OnboardingStore.handleExportToMDMButtonClicked();
    },

    handleExportEntityButtonClicked: function () {
      let oSelectiveExport = {};
      let oPostRequest = {};
      oSelectiveExport.sUrl = getRequestMapping().SelectiveExport;
      oPostRequest.exportType = "product";
      oPostRequest.baseEntityIIds = [];
      oPostRequest.endpointId = null;
      oSelectiveExport.oPostRequest = oPostRequest;
      OnboardingStore.handleExportEntityButtonClicked(oSelectiveExport);
    },

    handleImportEntityButtonClicked: function (aFiles) {
      let oImportExcel = {};
      oImportExcel.sUrl = getRequestMapping().RuntimeProductImport;
      OnboardingStore.handleImportEntityButtonClicked(aFiles,oImportExcel);
    },

    handleTransferToSupplierStagingButtonClicked: function () {
      OnboardingStore.handleTransferToSupplierStagingButtonClicked();
    },

    handleExportToExcelButtonClicked: function () {
      OnboardingStore.handleExportToExcelButtonClicked();
    },

    handleMatchMergeCellClicked: function (sRowId, sValue, sTableId, sTableGroupName, bIsGoldenRecordComparison) {
      if (bIsGoldenRecordComparison) {
        GoldenRecordStore.handleMatchMergeCellClicked();
      } else {
        TimelineStore.handleMatchMergeCellClicked(sRowId, sValue, sTableId, sTableGroupName);
      }
    },

    handleMatchMergeTagCellClicked: function(sRowId, iValue, sTableId, bIsGoldenRecordComparison, sColumnId){
      if (bIsGoldenRecordComparison) {
        GoldenRecordStore.handleMatchMergeCellClicked();
      } else {
        TimelineStore.handleMatchMergeCellClicked(sRowId, iValue, sTableId, "tagTable");
      }
    },

    handleMatchMergeColumnHeaderClicked: function (sColumnId) {
      ContentStore.handleMatchMergeColumnHeaderClicked(sColumnId);
    },

    handleMatchMergeRelationshipCellClicked: function(sRowId, oProperty, sTableId, sContext, sColumnId){
      if (sContext === "goldenRecord") {
        GoldenRecordStore.handleMatchMergeCellClicked();
      } else {
        TimelineStore.handleMatchMergeCellClicked(sRowId, oProperty, sTableId, "relationshipTable");
      }
    },

    handleMatchMergeRelationshipCellRemoveClicked: function (sRowId, sTableId, sContext) {
      if (sContext === "goldenRecord") {
        GoldenRecordStore.handleMatchMergeCellRemoveClicked();
      } else {
        TimelineStore.handleMatchMergeCellRemoveClicked(sRowId, sTableId, "relationshipTable");
      }
    },

    handleCreateCloneButtonClicked: function (sContextId) {
      CloneWizardStore.handleCreateCloneButtonClicked(sContextId);
    },

    handleCancelCloningButtonClicked: function (sContextId) {
      CloneWizardStore.handleCancelCloningButtonClicked(false, sContextId)
    },

    handleCheckboxButtonClicked: function (sId, sGroupId, sContext) {
      CloneWizardStore.handleCheckboxButtonClicked(sId, sGroupId, sContext)
    },

    handleCheckboxHeaderButtonClicked: function(sKey) {
      CloneWizardStore.handleCheckboxHeaderButtonClicked(sKey)
    },

    handleExactCloneCheckboxClicked: function() {
      CloneWizardStore.handleExactCloneCheckboxClicked()
    },

    handleCloneExpandSectionToggled: function (sTypeId) {
      CloneWizardStore.handleCloneExpandSectionToggled(sTypeId);
    },

    handleSelectTypeToCreateLinkedVariant: function (sIdToRemove, sSelectedId) {
      CloneWizardStore.handleSelectTypeToCreateLinkedVariant(sIdToRemove, sSelectedId);
    },

    handleGetAllowedTypesToCreateLinkedVariant: function () {
      CloneWizardStore.handleGetAllowedTypesToCreateLinkedVariant();
    },

    handleClonePropertySelectionToggled: function (sTypeId, sPropertyId) {
      CloneWizardStore.handleClonePropertySelectionToggled(sTypeId, sPropertyId);
    },

    handleCloningWizardSelectAllClicked: function(aSearchedElementList, bIsSelectAll){
      CloneWizardStore.handleCloningWizardSelectAllClicked(aSearchedElementList, bIsSelectAll);
    },

    handleCloningWizardCloneCountChanged: function (iCloneCount) {
      CloneWizardStore.handleCloningWizardCloneCountChanged(iCloneCount);
    },

    handleUOMTableCreateRowButtonClicked: function (sContextId) {
      if(ContentUtils.contextTableSafetyCheck()){
        return;
      }
      else if (ContentUtils.activeContentSafetyCheck()) {
        let fCallback = () => {
          VariantStore.handleUOMTableCreateRowButtonClicked(sContextId);

        };
        ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback);

      }
    },

    handleUOMViewDateRangeApplied: function (oRangeData, sContextId) {
      VariantStore.handleUOMViewDateRangeApplied(oRangeData, sContextId);
    },

    handleUOMViewSortDataChanged: function (sContextId, oSortData) {
      if (!ContentUtils.contextTableSafetyCheck()) {
        VariantStore.handleUOMViewSortDataChanged(sContextId, oSortData);
      }
    },

    handleUOMViewPaginationDataChanged: function (sContextId, oPaginationData, oFilterContext) {
      if (!ContentUtils.contextTableSafetyCheck()) {
        VariantStore.handleUOMViewPaginationDataChanged(sContextId, oPaginationData, oFilterContext);
      }
    },

    //Check for price variant
    handleUOMViewGridValueChanged: function (sContentId, sPropertyId, sValue, oViewData) {
      if (oViewData && oViewData.viewMode == ContentScreenConstants.UOM_BODY_GRID_MODE) {
        let oCellData = {
          columnId: sPropertyId,
          rowId: sContentId,
          expressionId: oViewData.expressionId,
          value: sValue
        };
        _handleTableCellValueChanged(oCellData, oViewData.sContext, oViewData.sTableContextId);
      } else {
        ContentGridStore.handleGridPropertyChanged(sContentId, sPropertyId, sValue);
      }
      _triggerChange();
    },

    handleUOMViewGridTagValueChanged: function (sContentId, sTagId, aTagValueRelevanceData, oExtraData, oViewData) {
      if (oViewData && oViewData.viewMode == ContentScreenConstants.UOM_BODY_GRID_MODE && oExtraData.outerContext !== "Edit_Prices") {
        ContentScreenStore.handleTagGroupTagValueChanged(sTagId, aTagValueRelevanceData, oExtraData);
      }
    },

    handleUOMViewGridActionItemClicked: function (sId, sContentId, sContextId, sViewContext, oFilterData, oExtraData) {
    switch (sId) {
      case "delete":
        VariantStore.handleDeleteUOMVariantClicked(sContentId, sContextId, oFilterData, oExtraData);
        break;

      case "view":
        ContentScreenStore.handleTableOpenButtonClicked(sContentId, sViewContext);
        break;

      case "version":
        VariantStore.handleUOMViewVersionActionItemClicked(sContentId, sContextId);
        break;

      case "downloadImage":
        ContentStore.handleTableDownloadImageButtonClicked(sContentId, sContextId);
        break;
    }

    },

    handleUOMViewFullScreenButtonClicked: function (sContextId) {
        VariantStore.handleUOMViewFullScreenButtonClicked(sContextId);
    },

    handleUOMViewFullScreenCancelButtonClicked: function (sContextId) {
      VariantStore.handleUOMViewFullScreenCancelButtonClicked(sContextId);
    },

    handleContentLinkedSectionsLinkItemClicked: function (sSectionId, bDoNotTrigger) {
      ContentStore.handleContentLinkedSectionsLinkItemClicked(sSectionId, bDoNotTrigger);
    },

    handleContentGridEditButtonClicked: function () {
      ContentGridStore.fetchContentGridData();
    },

    handleRestoreContentsButtonClicked: function () {
      let aSelectedContentIds = ContentUtils.getSelectedContentIds();
      ContentStore.handleRestoreContentsButtonClicked(aSelectedContentIds);
    },

    handleThumbnailRestoreClicked: function (sId) {
      ContentStore.handleRestoreContentsButtonClicked([sId]);
    },

    handleContentGridEditRowAutoSave: function (oEvent) {
      if (ContentUtils.isContentGridOpen() && ContentUtils.shouldSaveContentGrid(oEvent)) {
        ContentGridStore.handleContentGridSave({});
      }
    },

    handleContentGridEditCloseButtonClicked: function(oEvent) {
      ContentScreenStore.handleContentGridEditRowAutoSave(oEvent);
      ContentGridStore.resetGridEditablePropertiesData();
      ColumnOrganizerProps.reset();
      let sViewMode = ContentScreenProps.screen.getViewMode();
      ContentScreenProps.screen.setViewMode(sViewMode);
      oComponentProps.contentGridProps.setResizedColumnId("");
      ContentGridStore.handleContentGridEditCloseButtonClicked();
    },

    handleContentKPITileOpenDialogButtonClicked: function (sTileId) {
      ContentStore.handleContentKPITileOpenDialogButtonClicked(sTileId);
    },

    handleGoldenRecordBucketTabChanged: function (sBucketId, sTabId) {
      GoldenRecordStore.handleGoldenRecordBucketTabChanged(sBucketId, sTabId);
    },

    handleContentKPITileCloseDialogButtonClicked: function (sTileId) {
      ContentStore.handleContentKPITileCloseDialogButtonClicked(sTileId);
    },

    handleTabLayoutViewTabChanged: function (sTabId, sContext, oFilterContext) {
      return _handleTabLayoutViewTabChanged(sTabId, sContext, oFilterContext);
    },

    handleDataLanguageChanged: function (sDataLanguageCode, oCallbackData) {
      _handleDataLanguageChanged(sDataLanguageCode, oCallbackData);
    },

    handleDashboardTabSelected: function (sTabId) {
      _handleDashboardTabSelected(sTabId);
    },

    handleContentSliderImageClicked: function (sSelectedImageID) {
      ContentStore.handleSliderImageClicked(sSelectedImageID);
    },

    handleGoldenRecordBucketMergeButtonClicked: function (sBucketId) {
      GoldenRecordStore.fetchDataForGoldenRecordMatchAndMergeWrapper(sBucketId);
    },

    handleInformationTabRuleViolationItemClicked: function (sParentId, sSelectedRuleViolationId, sContext) {
      let fCallback = () => {
        let oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE
        };
        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.ChildFilterToggled(sParentId, sSelectedRuleViolationId, false);
        InformationTabStore.handleInformationTabRuleViolationItemClicked({filterContext: oFilterContext, context: sContext},{});

      };
      ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback);
    },

    handleUploadAssetDialogButtonClicked: function (sButtonId, sContext, sSelectedKlassId, sRelationshipSideId, oFiles) {
      _handleUploadAssetDialogButtonClicked(sButtonId, sContext, sSelectedKlassId, sRelationshipSideId, oFiles);
    },

    handleFileDrop: function (sContext, aFiles, oExtraData) {
      switch (sContext) {
        case 'dashboardEndpoint': {
          this.handleOnboardingFileUploaded(aFiles, oExtraData);
          break;
        }
        case 'dashboardBulkUpload':
          _handleDashboardBulkUpload(aFiles);
          break;

        case 'relationshipSectionDrop':
          _handleRelationshipSectionDrop(sContext, aFiles, oExtraData.filterContext);
      }
    },

    handleFileDragDropViewDraggingState: function () {
      let bIsDragging = RelationshipViewProps.getIsDragging();
      RelationshipViewProps.setIsDragging(!bIsDragging);
      _triggerChange();
    },

    handleCommonConfigSectionSingleTextChanged: function (sContext, sKey, sVal) {
      _handleCommonConfigSectionSingleTextChanged(sContext, sKey, sVal);
    },

    handleCommonConfigSectionViewYesNoButtonClicked: function (sContext, sKey, sVal) {
      _handleCommonConfigSectionViewYesNoButtonClicked(sContext, sKey, sVal);
    },

    /** Executed only when data language changed inside content**/
    handleContentDataLanguageChanged: function (sDataLanguageId) {
      let sSelectedDataLanguageCode = CommonUtils.getDataLanguageCodeById(sDataLanguageId);
      ContentStore.handleContentDataLanguageChanged(sSelectedDataLanguageCode);
    },

    handleDeleteTranslationClicked: function(sLanguageCode) {
      ContentStore.handleDeleteTranslationClicked(sLanguageCode);
    },

    handleOpenProductFromDashboard: function (sContentId, sBaseType, oFilterContext) {
      _handleOpenProductFromDashboard(sContentId, sBaseType, oFilterContext);
    },

    handleBreadCrumbClicked: function (oItem) {
      _handleBreadCrumbClicked(oItem);
    },

    handleResetBreadcrumbNodePropsAccordingToContext: function (sContext, sContentId, oPostData) {
      _handleResetBreadcrumbNodePropsAccordingToContext(sContext, sContentId, oPostData);
    },

    dataLanguagePopoverVisibilityToggled: function () {
      ContentStore.dataLanguagePopoverVisibilityToggled();
    },

    handleHeaderMenuNotificationClicked: function () {
      _handleHeaderMenuNotificationClicked();
    },

    handleContentComparisonMatchAndMergePropertyValueChanged: function (sContext, sPropertyId, sTableId, oValue) {
      _handleContentComparisonMatchAndMergePropertyValueChanged(sContext, sPropertyId, sTableId, oValue);
    },

    handleContentComparisonMatchAndMergeViewTableRowClicked: function (sContext, sPropertyId, sTableId) {
      _handleContentComparisonMatchAndMergeViewTableRowClicked(sContext, sPropertyId, sTableId);
    },

    handleStepperViewActiveStepChanged: function (sContext, sButtonId) {
      _handleStepperViewActiveStepChanged(sContext, sButtonId);
    },

    handleContentComparisonMnmRelationshipPaginationChanged: function (sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId) {
      _handleContentComparisonMnmRelationshipPaginationChanged(sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId);
    },

    handleContentComparisonMnmRemoveRelationshipButtonClicked: function (sPropertyId, sTableId, oPropertyToSave) {
      _handleContentComparisonMnmRemoveRelationshipButtonClicked(sPropertyId, sTableId, oPropertyToSave);
    },

    handleContentComparisonMNMLanguageChanged: function (sLanguageId) {
      GoldenRecordStore.handleContentComparisonMatchAndMergeLanguageSelectionClick(sLanguageId);
    },

    handleContentComparisonFullScreenButtonClicked: function () {
      GoldenRecordStore.handleContentComparisonFullScreenButtonClicked();
    },

    handleMakeUpInstanceOpenClicked: function (sId, oFilterContext) {
      let oCallbackData = {
        entityType: BaseTypeDictionary.makeupBaseType,
        isGetContent: true,
        filterContext: oFilterContext
      };
      /** When MakeupInstance is Open From Marketing Article The MakeUp Tab Should Be Reset **/
      let oActiveEntitySelectedTabIdMap = ContentScreenProps.screen.getActiveEntitySelectedTabIdMap();
      if (!CS.isEmpty(oActiveEntitySelectedTabIdMap[sId])) {
        oActiveEntitySelectedTabIdMap[sId].selectedTabId = null;
      }
      if (!CS.isEmpty(oActiveEntitySelectedTabIdMap[ContentUtils.getActiveContent().id])) {
        oActiveEntitySelectedTabIdMap[ContentUtils.getActiveContent().id].languageComparisonModeOn = false;
        oActiveEntitySelectedTabIdMap[ContentUtils.getActiveContent().id].languageCodeToCompare = ''
      }
      let fCallback = _fetchEntityById.bind(this, sId, oCallbackData);
      ContentUtils.makeWindowHistoryStateStableForEntityCreation(fCallback)
    },

    handleArchiveButtonClicked: function () {
      let aAllMenus = GlobalStore.getAllMenus();
      let aBreadCrumbData = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
      let oBreadCrumbNode = aBreadCrumbData[0];
      /**
       * to disable foreword in archive.
       * If current runtime module id (product) is same with selected archival module Id, Need to Clear breadcrumb data
       * because if breadcrumb data is newly created then we are not going to set history state.
       */
      if(oBreadCrumbNode.id === aAllMenus[0].id) {
        BreadCrumbStore.removeTrailingBreadcrumbPath(oBreadCrumbNode.id, oBreadCrumbNode.type);
        ContentScreenProps.breadCrumbProps.setBreadCrumbData([]);
      }
      ContentScreenStore.handleModuleItemClicked(aAllMenus[0].id, { screenContext: oFilterPropType.MODULE, filterType: oFilterPropType.MODULE });
    },

    handleTimelineComparisonDialogViewHeaderButtonClicked: function (sButtonId, oFilterContext) {
      TimelineStore.handleTimelineComparisonDialogViewHeaderButtonClicked(sButtonId, oFilterContext);
    },

    handleRelationshipConflictPropertyClicked: function(oConflictingValue, oRelationship) {
      _handleRelationshipConflictPropertyClicked(oConflictingValue, oRelationship);
    },

    handleRelationshipConflictPropertyResolved: function(oSelectedConflictValue, oRelationship) {
      _handleRelationshipConflictPropertyResolved(oSelectedConflictValue, oRelationship);
    },

    handleGridEditablePropertiesConfigDialogButtonClicked: function(sButtonId) {
      ContentGridStore.handleConfigDialogButtonClicked(sButtonId);
    },

    handleMultiClassificationEditIconClicked: function(sTabId, sTaxonomyId, sContext) {
      if (sContext === "overviewTabSectionView") {
        NewMultiClassificationStore.openMultiClassificationDialog(sTabId)
      }else if(sContext === "minorTaxonomiesSectionView"){
        NewMinorTaxonomyStore.openMultiClassificationDialog(sTabId, sTaxonomyId)
      }
    },

    handleMultiClassificationDialogButtonClicked: function(sDialogButtonId, sContext) {
      ContentStore.handleMultiClassificationDialogButtonClicked(sDialogButtonId, sContext);
    },

    handleMultiClassificationDialogViewHeaderButtonClicked: function(sButtonId) {
      NewMultiClassificationStore.handleMultiClassificationDialogViewHeaderButtonClicked(sButtonId);
    },

    handleMultiClassificationTreeNodeClicked: function(sTreeNodeId, sContext) {
      if (sContext === "overviewTabSectionView") {
        NewMultiClassificationStore.handleMultiClassificationTreeNodeClicked(sTreeNodeId)
            .then(_triggerChange);
      }else if(sContext === "minorTaxonomiesSectionView" || sContext === "minorTaxonomiesInEmbeddedTable"){
        NewMinorTaxonomyStore.minorTaxonomyTreeNodeClicked(sTreeNodeId)

      }
    },

    handleMultiClassificationTreeNodeCheckboxClicked: function(sTreeNodeId, sContext, sTaxonomyId) {
      if (sContext === "overviewTabSectionView") {
        NewMultiClassificationStore.handleMultiClassificationTreeNodeCheckboxClicked(sTreeNodeId);
      } else if(sContext === "minorTaxonomiesSectionView" || sContext === "minorTaxonomiesInEmbeddedTable"){
        NewMinorTaxonomyStore.handleMinorTaxonomyTreeNodeCheckboxClicked(sTreeNodeId, sTaxonomyId, sContext)
      }
    },

    handleMultiClassificationCrossIconClicked: function(sLeafNodeId, sIdToRemove, sContext, sTaxonomyId) {
      if (sContext === "multiClassificationDialogView") {
        NewMultiClassificationStore.handleMultiClassificationCrossIconClicked(sLeafNodeId, sIdToRemove);
      } else if(sContext === "minorTaxonomiesDialogContext" || sContext === "minorTaxonomiesInEmbeddedTable"
          || sContext === "minorTaxonomiesDialogContextInEmbeddedTable"){
        NewMinorTaxonomyStore.handleMinorTaxonomyCrossIconClicked(sLeafNodeId, sIdToRemove, sTaxonomyId, sContext);
      }
    },

    handleTreeViewLoadMoreClicked: function (sContext, sParentTaxonomyId, sActiveNodeId) {
      switch (sContext) {
        case BulkEditTabTypesConstants.TAXONOMIES:
        case BulkEditTabTypesConstants.CLASSES:
          BulkEditStore.handleTreeViewLoadMoreClicked(sParentTaxonomyId);
          break;

        case MultiClassificationViewTypesConstants.CLASSES:
        case MultiClassificationViewTypesConstants.TAXONOMIES:
          NewMultiClassificationStore.handleTreeViewLoadMoreClicked(sParentTaxonomyId);
          break;
        case "minorTaxonomiesTreeView":
          NewMinorTaxonomyStore.handleTreeViewLoadMoreClicked(sParentTaxonomyId, sActiveNodeId);
          break;
      }
    },

    handleTaxonomyInheritanceConflictIconClicked: function () {
      _handleTaxonomyInheritanceConflictIconClicked();
    },

    handleCloseTaxonomyInheritanceDialog: function (sDialogButtonId, oCallback) {
      ContentStore.handleMultiClassificationDialogButtonClicked(sDialogButtonId, oCallback);
    },

    handleTaxonomyInheritanceAdaptTaxonomy: function (oBaseArticleTaxonomy, sArticleTaxonomyId, iIndex, sContentId, sParentContentId) {
      NewMultiClassificationStore.handleAdaptTaxonomyForTaxonomyInheritance(oBaseArticleTaxonomy, sArticleTaxonomyId, iIndex, sContentId, sParentContentId);
    },

    handleTaxonomyInheritanceAdaptAllTaxonomy: function (sContentId, sParentContentId) {
      NewMultiClassificationStore.handleAdaptAllTaxonomyForTaxonomyInheritance(sContentId, sParentContentId);
    },

    handleTaxonomyInheritanceRevertTaxonomy: function (oBaseArticleTaxonomy, sArticleTaxonomyId, sContentId, sParentContentId) {
      NewMultiClassificationStore.handleRevertTaxonomyForTaxonomyInheritance(oBaseArticleTaxonomy, sArticleTaxonomyId, sContentId, sParentContentId);
    },

    handleTaxonomyInheritanceRevertAllTaxonomy: function (sContentId, sParentContentId) {
      NewMultiClassificationStore.handleRevertAllTaxonomyForTaxonomyInheritance(sContentId, sParentContentId);
      _triggerChange();
    },

    handleAssetEventScheduleFieldChanged: function (sKey, sValue, bIsPromotionCampaignEvent) {
      ContentStore.handleAssetEventScheduleFieldChanged(sKey, sValue, bIsPromotionCampaignEvent);
    },

    resetTreeViewData: () => {
      TreeViewStore.resetTreeViewData()
    },

    handleTreeViewNodeRightSideButtonClicked: (sContext, sButtonId, sNodeId) => {
      switch (sContext) {
        case BulkEditTabTypesConstants.TAXONOMIES:
        case BulkEditTabTypesConstants.CLASSES:
          BulkEditStore.handleTreeViewNodeRightSideButtonClicked(sButtonId, sNodeId);
          break;
      }
    },

    handleTreeSearched: (sContext, sSearchedText, sActiveNodeId) => {
      switch (sContext) {
        case BulkEditTabTypesConstants.TAXONOMIES:
        case BulkEditTabTypesConstants.CLASSES:
          BulkEditStore.handleTreeSearchClicked(sSearchedText);
          break;
        case MultiClassificationViewTypesConstants.CLASSES:
        case MultiClassificationViewTypesConstants.TAXONOMIES:
          NewMultiClassificationStore.handleClassificationTreeSearchClicked(sSearchedText);
          break;
        case "minorTaxonomiesTreeView":
          NewMinorTaxonomyStore.handleMinorTaxonomiesTreeSearchClicked(sSearchedText, sActiveNodeId);
          break;
      }
    },

    handleChooseTaxonomyApplyButtonClicked: function (oFilterContext) {
      _makeAppliedTaxonomyDirtyForBookmark(oFilterContext);
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      let oCategoriesVsSelectedIds = oFilterStore.getCategoriesVsSelectedIds(true);
      let oFilterProps = oFilterStore.getFilterProps();
      let aSelectedTypes = oCategoriesVsSelectedIds["natureClasses"];
      aSelectedTypes.push.apply(aSelectedTypes, oCategoriesVsSelectedIds["attributionClasses"]);
      oFilterProps.setSelectedTypes(aSelectedTypes);
      oFilterProps.setSelectedTaxonomyIds(oCategoriesVsSelectedIds["taxonomies"]);

      /**To update selected taxonomy and class ids*/
      let contextualAllCategoriesProps = ContextualAllCategoriesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
      let aSummaryViewTreeData = contextualAllCategoriesProps.getSummaryTreeData();
      aSummaryViewTreeData.clonedObject && contextualAllCategoriesProps.setSummaryTreeData(aSummaryViewTreeData.clonedObject);
      AllCategoriesSelectorStore.resetDirtyProps(oFilterContext);
      _handleFilterButtonClicked({filterContext: oFilterContext});
    },

    handleBulkDownloadDialogCheckboxClicked: function (sId, iIndex, sClassId) {
      ContentBulkDownloadStore.handleBulkDownloadDialogCheckboxClicked(sId, iIndex, sClassId);
    },

    handleBulkDownloadDialogExpandButtonClicked: function (sId, iIndex) {
      ContentBulkDownloadStore.handleBulkDownloadDialogExpandButtonClicked(sId, iIndex);
    },

    handleBulkDownloadDialogActionButtonClicked: function (sButtonId) {
      switch (sButtonId) {
        case "cancel":
          ContentBulkDownloadStore.handleBulkDownloadDialogCancelButtonClicked();
          break;
        case "download":
          ContentBulkDownloadStore.handleBulkDownloadDialogDownloadButtonClicked();
          break;
      }
    },

    handleBulkDownloadDialogFixedSectionValueChanged: function (sButtonId, oData) {
      ContentBulkDownloadStore.handleBulkDownloadDialogFixedSectionValueChanged(sButtonId, oData);
    },

    handleBulkDownloadDialogChildElementValueChanged: function (sId, sValue, sClassId, iParentIndex) {
      ContentBulkDownloadStore.handleBulkDownloadDialogChildElementValueChanged(sId, sValue, sClassId, iParentIndex);
    },

    handleTaxonomyHideEmptyToggleClicked: (bHideEmpty, oFilterContext) => {
      _handleTaxonomyHideEmptyToggleClicked(bHideEmpty, oFilterContext);
    },

    handleChipsEditButtonClicked: (oProperty, sId) => {
      ContentScreenProps.screen.setActiveEmbProperty(oProperty);
      _handleChipsEditButtonClicked(sId);
    },

    handleGridPropertyViewClassificationDialogButtonClicked: (sButtonId, sContext, sEmbeddedContentId, sPropertyId, sTableContextId) => {
      _handleGridPropertyViewClassificationDialogButtonClicked(sButtonId, sContext, sEmbeddedContentId, sPropertyId, sTableContextId);
    },

    handleMenuListToggleButtonClicked: () => {
      _handleMenuListToggleButtonClicked();
      _triggerChange();
    },

    handleCategoriesButtonClicked: (sId, oFilterContext, oTaxonomyTreeRequestData) => {
      AllCategoriesSelectorStore.handleCategoriesButtonClicked(sId, oFilterContext, oTaxonomyTreeRequestData);
    },

    handleCategorySelected: (oFilterContext, sSelectedCategoryId) => {
      AllCategoriesSelectorStore.handleCategorySelected(oFilterContext, sSelectedCategoryId);
    },

    handleAllCategoriesSelectorTreeNodeCheckboxClicked: (sContext, sTreeNodeId, oFilterContext) => {
      AllCategoriesSelectorStore.handleAllCategoriesSelectorTreeNodeCheckboxClicked(sContext, sTreeNodeId, oFilterContext);
    },

    handleAllCategoriesSelectorTreeNodeClicked: (sTreeNodeId, oFilterContext, oTaxonomyTreeRequestData) => {
      AllCategoriesSelectorStore.handleAllCategoriesSelectorTreeNodeClicked(sTreeNodeId, oFilterContext, oTaxonomyTreeRequestData);
    },

    handleAllCategoriesSelectorLoadMoreClicked: (sSelectedCategory, sParentId, oTaxonomyTreeRequestData, oFilterContext) => {
      AllCategoriesSelectorStore.handleLoadMoreCategoriesClicked(sSelectedCategory, sParentId,
          oTaxonomyTreeRequestData, oFilterContext);
    },

    handleAllCategoriesSelectorCancelButtonClicked: (oFilterContext) => {
      AllCategoriesSelectorStore.handleAllCategoriesSelectorCancelButtonClicked(oFilterContext);
    },

    handleAllCategoriesSelectorApplyButtonClicked: (oFilterContext) => {
      ContentScreenStore.handleChooseTaxonomyApplyButtonClicked(oFilterContext);
    },

    handleSummaryClearSelectionButtonClicked: (oFilterContext) => {
      AllCategoriesSelectorStore.handleSummaryClearSelectionButtonClicked(oFilterContext);
    },

    handleSummaryCrossButtonClicked: (context, sButtonId, sSelectedId, oFilterContext) => {
      AllCategoriesSelectorStore.handleSummaryCrossButtonClicked(context, sButtonId, sSelectedId, oFilterContext);
    },

    handleAllCategoriesTreeViewSearched: (oFilterContext, sSearchText, oTaxonomyTreeRequestData) => {
      AllCategoriesSelectorStore.handleAllCategoriesTreeViewSearched(oFilterContext, sSearchText, oTaxonomyTreeRequestData);
    },

    triggerChange: function () {
      _triggerChange();
    },

  };

})();

MicroEvent.mixin(ContentScreenStore);

/****** Prototype chaining not possible without creating an instance for function:
 Filter Store is function so we need to explicitly access bind through its prototype *****/
FilterStoreFactory.bindStoreAction('filter-change', ContentScreenStore.triggerChange);

/****** Prototype chaining : Following Stores are objects so bind method will get directly from its prototype *****/
ContentRelationshipStore.bind('relationship-change', ContentScreenStore.triggerChange);
ContentStore.bind('content-change', ContentScreenStore.triggerChange);
CloneWizardStore.bind('clone-wizard-change', ContentScreenStore.triggerChange);
BulkEditStore.bind('bulk-edit-change', ContentScreenStore.triggerChange);
ContentUtils.bind('content-utils-change', ContentScreenStore.triggerChange);
GlobalStore.bind('global-change', ContentScreenStore.getDataFromGlobalStore);
ImageCoverflowStore.bind('image-change', ContentScreenStore.triggerChange);
OnboardingStore.bind('onboarding-change', ContentScreenStore.triggerChange);
AttributeStore.bind('attribute-change', ContentScreenStore.triggerChange);
AvailableEntityStore.bind('available-entity-change', ContentScreenStore.triggerChange);
NewCollectionStore.bind('new-collection-change', ContentScreenStore.triggerChange);
JobStore.bind('job-change', ContentScreenStore.triggerChange);
TimelineStore.bind('timeline-change', ContentScreenStore.triggerChange);
GoldenRecordStore.bind('golden-record-change', ContentScreenStore.triggerChange);
ContentGridStore.bind('content-grid-change', ContentScreenStore.triggerChange);
TranslationStore.bind('translation-changed', ContentScreenStore.triggerChange);
SharableURLStore.bind('sharable-URL-change', ContentScreenStore.triggerChange);
InformationTabStore.bind('Information-tab-changed', ContentScreenStore.triggerChange);
BreadCrumbStore.bind('Breadcrumb-changed', ContentScreenStore.triggerChange);
ContentBulkDownloadStore.bind('bulkAssetDownload-change', ContentScreenStore.triggerChange);
BulkAssetLinkSharingStore.bind('bulkAssetLinkSharing-change', ContentScreenStore.triggerChange);
CollectionAndTaxonomyHierarchyStore.bind('collection-and-taxonomy-data-navigation-helper-change', ContentStore.triggerChange);
VariantStore.bind('variant-change', ContentScreenStore.triggerChange);
NewMultiClassificationStore.bind('classes-and-taxonomies-change', ContentScreenStore.triggerChange);
NewMinorTaxonomyStore.bind('classes-and-taxonomies-change', ContentScreenStore.triggerChange);
AllCategoriesSelectorStore.bind('all-categories-taxonomies-change', ContentScreenStore.triggerChange);


export default ContentScreenStore;
