/**
 * Created by DEV on 05-10-2015.
 */
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';

import CS from '../../../../../../libraries/cs';
import SessionStorageManager from './../../../../../../libraries/sessionstoragemanager/session-storage-manager';
import ContentScreenProps from './../model/content-screen-props';
import ContentUtils from './content-utils';
import ImageCoverflowUtils from './image-coverflow-utils';
import ContentLogUtils from './content-log-utils';
import SessionProps from '../../../../../../commonmodule/props/session-props';
import GlobalStore from './../../../../store/global-store';
import UniqueIdentifierGenerator from './../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import ScreenModeUtils from './screen-mode-utils';

import MockDataForEntityBaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import PhysicalCatalogDictionary from '../../../../../../commonmodule/tack/physical-catalog-dictionary';
import PortalTypeDictionary from '../../../../../../commonmodule/tack/portal-type-dictionary';
import ModuleDictionary from '../../../../../../commonmodule/tack/module-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../../commonmodule/tack/config-data-entities-dictionary';
import HierarchyTypesDictionary from '../../../../../../commonmodule/tack/hierarchy-types-dictionary';
import ThumbnailModeConstants from '../../../../../../commonmodule/tack/thumbnail-mode-constants';
import ContentScreenConstants from './../model/content-screen-constants';
import AcrolinxAttributeIdDictionary from '../../../../../../commonmodule/tack/acrolinx-attribute-id-dictionary';
import TaxonomyTypeDictionary from '../../../../../../commonmodule/tack/taxonomy-type-dictionary';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary';
import CouplingConstants from '../../../../../../commonmodule/tack/coupling-constans';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import { communicator as HomeScreenCommunicator } from '../../../../store/home-screen-communicator';
import MethodTracker from '../../../../../../libraries/methodtracker/method-tracker';
import LogFactory from '../../../../../../libraries/logger/log-factory';

import ImageCoverflowStore from './image-coverflow-store';
import FilterStoreFactory from './filter-store-factory';
import TimelineStore from './timeline-store';
import GoldenRecordStore from './golden-record-store';
import CloneWizardStore from './clone-wizard-store';
import NewCollectionStore from './new-collection-store';
import AvailableEntityStore from './available-entity-store';
import ArticleProps from './../model/viewprops/article-view-props';
import ContentRelationshipStore from './content-relationship-store';
import SharableURLStore from '../../../../../../commonmodule/store/helper/sharable-url-store';
import BreadcrumbStore from '../../../../../../commonmodule/store/helper/breadcrumb-store';
import BulkAssetDownloadStore from '../helper/content-bulk-download-store';
import BulkAssetLinkSharingStore from './bulk-asset-link-sharing-store';
import InformationTabStore from './information-tab-store';
import BaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import NatureTypeDictionary from '../../../../../../commonmodule/tack/nature-type-dictionary';
import TagTypeConstants from '../../../../../../commonmodule/tack/tag-type-constants';
import TemplateTabsDictionary from '../../../../../../commonmodule/tack/template-tabs-dictionary';
import AllowedMediaTypeDictionary from '../../../../../../commonmodule/tack/mock-data-for-allowed-media-type-dictionary';
import MockDataForEventsView from '../../../../../../commonmodule/tack/events-view-data';
import RelationshipConstants from '../../../../../../commonmodule/tack/relationship-constants';
import MeasurementMetricBaseTypeDictionary from './../../../../../../commonmodule/tack/measurement-metric-base-type-dictionary';
import MarkerClassTypeDictionary from './../../../../../../commonmodule/tack/marker-class-type-dictionary';
import ContentScreenRequestMapping from '../../tack/content-screen-request-mapping';
import CommonModuleRequestMapping from '../../../../../../commonmodule/tack/common-module-request-mapping';
import ConflictingValuesSourceDictionary from '../../tack/conflicting-values-source-dictionary';
import ContentScreenViewContextConstants from '../../tack/content-screen-view-context-constants';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import DashboardChartConfiguration from './../../screens/dashboardscreen/tack/dashboard-chart-configuration';
import DashboardChartTypeDictionary from './../../screens/dashboardscreen/tack/dashboard-chart-type-dictionary';
import ContentEditFilterItemsDictionary from '../../tack/content-edit-filter-items-dictionary';
import BaseTypeDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import DateFormatDictionary from '../../../../../../commonmodule/tack/date-format-dictionary';
import AttributesTypeDictionary from '../../../../../../commonmodule/tack/attribute-type-dictionary-new';
import CommonUtils from '../../../../../../commonmodule/util/common-utils';
import MomentUtils from './../../../../../../commonmodule/util/moment-utils';
import NumberUtils from './../../../../../../commonmodule/util/number-util';
import SessionStorageConstants from '../../../../../../commonmodule/tack/session-storage-constants';
import BulkAssetDownloadList from '../../tack/mock-data-for-bulk-asset-download';
import CollectionAndTaxonomyHierarchyStore from './collection-and-taxonomy-data-navigation-helper-store';
import oFilterPropType from './../../tack/filter-prop-type-constants';
import AssetDownloadViewDictionary from '../../tack/asset-download-view-dictionary';
import DashboardTabDictionary from '../../screens/dashboardscreen/tack/dashboard-tab-dictionary';
import Moment from "moment";
import DashboardScreenStore from "../../screens/dashboardscreen/store/dashboard-screen-store";
import ContentGridProps from "../model/content-grid-props";
import VariantStore from './variant-store';
import UOMProps from "../model/uom-props";
import EndpointTypeDictionary from "../../../../../../commonmodule/tack/endpoint-type-dictionary";
import OnboardingStore from "./onboarding-store";
import NewMinorTaxonomyStore from "./new-minor-taxonomy-store";
import NewMultiClassificationStore from './new-multiclassification-store';
import oBulkAssetLinkSharingProps from "../model/bulk-asset-link-sharing-props";
import ModuleDictionaryExport from "../../../../../../commonmodule/tack/module-dictionary-export";
import ContextualAllCategoriesTaxonomiesProps from "../model/contextual-all-categories-selector-view-props";
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
var getTranslation = ScreenModeUtils.getTranslationDictionary;
var logger = LogFactory.getLogger('content-store');
var trackMe = MethodTracker.getTracker('content-store');
const BackgroundColors = DashboardChartConfiguration.backgroundColors;

var ContentStore = (function () {

  var _triggerChange = function () {
    ContentStore.trigger('content-change');
  };

  let failureFetchAllTasksForAnnotations = (oResponse) => {
    ContentUtils.failureCallback(oResponse,'failureFetchAllTasksForAnnotations',getTranslation());
    _triggerChange();
  };

  let successFetchAllTasksForProperty = (oProperty, oResponse) => {
    oResponse = oResponse.success;
    let oTaskProps = ContentScreenProps.taskProps;
    oTaskProps.setIsTaskDialogActive(true);
    let oTaskData = _prepareDataForTask(oResponse);
    oTaskData.activeProperty = oProperty;
    ContentScreenProps.taskProps.setTaskData(oTaskData);
    _triggerChange();
  };

  let successFetchAllTasksForAnnotations = (oImageAttribute, oResponse) => {
    oResponse = oResponse.success;
    let oTaskData = _prepareDataForTask(oResponse);
    oTaskData.activeProperty = oImageAttribute;
    ContentScreenProps.taskProps.setTaskData(oTaskData);
    _triggerChange();
  };

  let _fetchAllTasksForAnnotations = (oImageAttribute) => {
    let oTaskPostData = _getTaskPostData();
    let oPostData = oTaskPostData.postData;
    oPostData.isForTaskAnnotation = true;
    CS.postRequest(getRequestMapping(oTaskPostData.screenMode).GetEntityById, oTaskPostData.parameters, oPostData, successFetchAllTasksForAnnotations.bind(this, oImageAttribute), failureFetchAllTasksForAnnotations);
  };

  let _handleTaskDialogOpenClicked = (oProperty) => {
    let oTaskPostData = _getTaskPostData();
    CS.postRequest(getRequestMapping(oTaskPostData.screenMode).GetEntityById, oTaskPostData.parameters, oTaskPostData.postData, successFetchAllTasksForProperty.bind(this, oProperty), failureFetchAllTasksForAnnotations);
  }

  let _getTaskPostData =()=>{
    let oScreenProps = ContentScreenProps.screen;
    var oActiveContent = ContentUtils.getActiveContent();

    var oPostData = {
      templateId : ContentUtils.getTemplateIdForServer(oScreenProps.getSelectedTemplateId())
    };

    var oParameters = {};
    oParameters.getAll = true;
    oParameters.id = oActiveContent.id;
    oParameters.isLoadMore = false;
    oParameters.tab = "tasktab";

    return {
      postData: oPostData,
      parameters: oParameters,
      screenMode: ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveContent.baseType)
    }
  }

  let _handleTaskDialogClosed = () => {
    let oTaskProps = ContentScreenProps.taskProps;
    let oTaskData = oTaskProps.getTaskData();
    let oAnnotationData = oTaskData.annotationData || {};

    if (!CS.isEmpty(oAnnotationData)) {
      oAnnotationData.isAnnotationDialogOpen = false;
      oAnnotationData.activeAnnotationId = "";
    } else {
      oTaskData.activeProperty = {};
      oTaskProps.setIsTaskDialogActive(false);
    }
  };

  let _setActiveTask = (oData) => {
    let oTaskProps = ContentScreenProps.taskProps;
    if(!CS.isEmpty(oData.activeTask)) {
      oTaskProps.setActiveTask(oData.activeTask);
    } else {
      oTaskProps.setActiveTask({});
    }
  };

  let _handleTaskDataChanged = (oData) => {
    let oTaskProps = ContentScreenProps.taskProps;
    let bIsDialogClose = CS.has(oData, "isDialogOpen") && !oData.isDialogOpen;
    let bMakeContentDirty = CS.has(oData, "makeActiveContentDirty") && oData.makeActiveContentDirty;

    CS.has(oData, "isTaskDirty") && oTaskProps.setIsTaskDirty(oData.isTaskDirty);
    CS.has(oData, "isTaskAdded") && oTaskProps.setIsTaskAddedStatus(oData.isTaskAdded);
    CS.has(oData, "tasksCount") && oTaskProps.setTasksCount(oData.tasksCount);
    CS.has(oData, "propertyIdsHavingTasks") && oTaskProps.setPropertyIdsHavingTask(oData.propertyIdsHavingTasks);
    CS.has(oData, "checkedTaskList") && oTaskProps.setSeletcedTaskIds(oData.checkedTaskList);
    CS.has(oData, "activeTask") && _setActiveTask(oData);

    oTaskProps.setIsTaskForceUpdate(false);
    bMakeContentDirty && ContentUtils.makeActiveContentDirty();
    bIsDialogClose && _handleTaskDialogClosed();
  };

  var _createNewModuleEntity = function (oItem, oCallbackData) {
    var sScreenMode = ContentUtils.getScreenModeBasedOnKlassBaseType(oItem.type);
    var sUrl = getRequestMapping(sScreenMode).CreateEntity;
    _createNewEntity(sUrl, oItem.id, oCallbackData);
  };

  //******************************* SIDDHANT *******************************************

  var _createNewEntity = function (sUrl, sType, oCallbackData) {
    var oSelectedEntity = ContentUtils.getActiveEntity();
    var sTypeId = sType || "";

    if (!sTypeId && !CS.isEmpty(oSelectedEntity)) {
      var sKlassId = ContentUtils.getEntityClassType(oSelectedEntity);
      var oMasterKlass = ContentUtils.getKlassFromReferencedKlassesById(sKlassId);

      if (oMasterKlass) {
        sTypeId = sKlassId;
      }
    }

    var sLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    var oNewEntity = ContentUtils.createDummyArticle(oSelectedEntity.id, sTypeId);

    let oAjaxExtraData = {
      URL: sUrl,
      requestData: {lang: sLanguage},
      postData: oNewEntity,
    };

    let aPayloadData = [oCallbackData, oAjaxExtraData];
    let sHelpScreenId;
    let sBreadcrumbType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT;
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem("", "", sBreadcrumbType, sHelpScreenId, {}, oAjaxExtraData, "", aPayloadData, _fetchArticleByIdCall);
    _createNewEntityCall(oCallbackData, oAjaxExtraData);
  };

  var _createNewEntityCall = function (oCallbackData, oAjaxExtraData) {
    var fSuccess = successCreateEntityCallback.bind(this, oCallbackData);
    var fFailure = failureCreateEntityCallback.bind(this, oCallbackData);
    CS.putRequest(oAjaxExtraData.URL, oAjaxExtraData.requestData, oAjaxExtraData.postData, fSuccess, fFailure);
  };

  var _setMasterEntityList = function (aFetchedContents) {
    var oAppData = ContentUtils.getAppData();
    oAppData.setContentList(aFetchedContents);
  };

  var _getMasterEntityList = function () {
    var oAppData = ContentUtils.getAppData();
    return oAppData.getContentList();
  };

  var _getEntityByIdUrl = function (sEntityBaseType) {
    return ContentUtils.getEntityByIdUrl(sEntityBaseType);
  };

  //*********************************** SIDDHANT END ********************************************

  //************************************* Server Callbacks **********************************************//
  var successDeleteSelectedArticlesCallback = function (oCallbackData, oResponse) {
    var oAppData = ContentUtils.getAppData();
    var oActiveEntity = ContentUtils.getActiveEntity();
    var aAvailableArticles = oAppData.getAvailableEntities();// TODO: Not handled

    var aIdsOfDeletedContents = oResponse.success;
    if (!CS.isEmpty(oResponse.failure.exceptionDetails)) {
      ContentUtils.failureCallback(oResponse, "successDeleteSelectedArticlesCallback", getTranslation());
    }

    var aMasterContentList = _getMasterEntityList();
    var oMasterDeletedContent = null;

    if (!CS.isEmpty(aIdsOfDeletedContents) && aIdsOfDeletedContents.length == 1) {
      oMasterDeletedContent = CS.find(aMasterContentList, {id: aIdsOfDeletedContents[0]});
    }


    let aSelectedContentList = ContentUtils.getSelectedContentIds();

    if(!CS.isEmpty(aIdsOfDeletedContents)) {
      CS.remove(aMasterContentList, function (oContent) {
        return CS.includes(aIdsOfDeletedContents, oContent.id);
      });

      CS.remove(aSelectedContentList, function (sId) {
        return CS.includes(aIdsOfDeletedContents, sId);
      });

      CS.remove(aAvailableArticles, function (oContent) {
        return CS.includes(aIdsOfDeletedContents, oContent.id);
      });

    }

    if (CS.includes(aIdsOfDeletedContents, oActiveEntity.id)) {
      ContentUtils.setActiveEntity({});
    }

    if(oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    var sSuccessMessage;
    if(oMasterDeletedContent) {
      sSuccessMessage = _getSuccessDeleteMessageForContent(oMasterDeletedContent.baseType);
    } else {
      sSuccessMessage = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().ARTICLES_LABEL } );
    }

    if(!CS.isEmpty(aIdsOfDeletedContents)) {
      ContentUtils.showSuccess(sSuccessMessage);
    }

    /**
     * Refresh handled through breadcrumb - 'functionToSet' method.
     */
    CommonUtils.refreshCurrentBreadcrumbEntity();
  };

  var successSaveContentsCallback = function (oCallbackData, oResponse) {
    trackMe('successSaveContentsCallback');
    var oScreenProps = ContentUtils.getComponentProps().screen;

    ContentUtils.setShakingStatus(false);
    // find and replace(merge) the response in the master content list
    var oSuccessResponse = oResponse.success;
    var oContentFromDB = oSuccessResponse.klassInstance;
    var bIsForComparison = false;
    //TODO : try to remove following safety check  - Ganesh
    var sContextId = oCallbackData.contentsToSave && oCallbackData.contentsToSave.contextId;
    if (!CS.isEmpty(sContextId)) {
      ContentScreenProps.variantSectionViewProps.setDummyVariant({});
    }

    oScreenProps.setIsVersionableAttributeValueChanged(false);
    if (!oCallbackData.variantInstanceId) {   // check
      _processGetArticleResponse(oSuccessResponse, bIsForComparison, sContextId, true, {}, oCallbackData.isSaveAndPublish);
    }

    var bUploadDialogStatus = oScreenProps.getVariantUploadImageDialogStatus();
    if(bUploadDialogStatus){
      oScreenProps.setVariantUploadImageDialogStatus(!bUploadDialogStatus);
    }

    if(oScreenProps.getVariantTagSummaryEditViewStatus() && oSuccessResponse.variantInstanceId){
      oScreenProps.setVariantTagSummaryEditViewStatus(false);
    }

    if(oContentFromDB.baseType !== BaseTypeDictionary.assetBaseType){
      _setContentSelectedImageId(oContentFromDB.defaultAssetInstanceId);
    }

    if(oCallbackData.funcRemoveRelationshipElementFromSelectedProp){
      oCallbackData.funcRemoveRelationshipElementFromSelectedProp();
    }

    if(oCallbackData && oCallbackData.fetchArticleWithContextId){
      oCallbackData.fetchArticleWithContextId();
    }

    if (oCallbackData && oCallbackData.functionToGetDownloadCount) {
      oCallbackData.functionToGetDownloadCount();
    }

    if (oCallbackData.alertifyToShow) {
      oCallbackData.alertifyToShow();
    } else {
      alertify.success(_getSuccessSaveMessageForContent(oContentFromDB.baseType));
    }

    if (ContentUtils.getIsKlassInstanceFlatPropertyUpdated()) {
      ContentUtils.setIsKlassInstanceFlatPropertyUpdated(false);
    }
    var aVersions = oSuccessResponse.versions;
    var aSelectedVersionIds = ContentScreenProps.timelineProps.getSelectedVersionIds();
    CS.remove(aSelectedVersionIds, function (vNumber) {
      var oVersion = CS.find(aVersions, {versionNumber: vNumber});
      return CS.isEmpty(oVersion);
    });

    if (oCallbackData.variantInstanceId) {
      _postProcessSaveAttributeVariantData(oCallbackData);
    }

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute(oCallbackData, oResponse);
    }

    /** if the relationship context is same after the success response then do not set the RelationshipContextData empty*/
    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let bResetRelationshipContextData = true;
    if(!CS.isEmpty(oRelationshipContextData)) {
      oRelationshipContextData.isEditVariantSectionOpen = false;
      let oReferencedVariantContexts = oSuccessResponse.configDetails.referencedVariantContexts;
      CS.forEach(oReferencedVariantContexts, function (oReferencedContextData, key) {
        CS.forEach(oReferencedContextData, function (oContext, key) {
          if (CS.isEqual(oRelationshipContextData.context.id, oContext.id)) {
            bResetRelationshipContextData = false;
            return bResetRelationshipContextData;
          }
        });
        return bResetRelationshipContextData;
      });
    }

    if (bResetRelationshipContextData) {
      ContentScreenProps.screen.setRelationshipContextData({});
    }
    ContentScreenProps.screen.setModifiedRelationshipsContextTempData({});
    ContentScreenProps.relationshipView.emptySelectedNatureRelationshipElements();

    let fCallbackToGetKPIData = _fetchKpiSummaryData.bind(this, oContentFromDB.id, oContentFromDB.baseType);
    setTimeout(fCallbackToGetKPIData, 3000);
    _resetCurrentZoomIfNotValid();

    let oVariantSectionViewProps = ContentScreenProps.variantSectionViewProps;
    if (oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus()) {
      oVariantSectionViewProps.setAvailableEntityChildVariantViewVisibilityStatus(false);
      CommonUtils.removeQuickListBreadCrumbFromPath();
    }

    _showWarnings(oSuccessResponse);

    _triggerChange();

    return true;
  };

  let _postProcessSaveAttributeVariantData = function (oCallbackData) {
    let oActiveContent = ContentScreenProps.screen.getActiveContent();
    if (oActiveContent && oActiveContent.contentClone) {
      delete oActiveContent.contentClone;
      delete oActiveContent.isDirty;

      let aAttributeIDsToDelete = [];
      CS.forEach(oCallbackData.contentsToSave.modifiedAttributes, function (oAttribute) {
        aAttributeIDsToDelete.push(oAttribute.id);
      });
      aAttributeIDsToDelete.push(oCallbackData.contentsToSave.deletedAttributes);

      CS.forEach(aAttributeIDsToDelete, function (sId) {
        CS.remove(oActiveContent.attributes, {id: sId});
      });
    }
  };

  // TODO: Bulk action
  var failureSaveContentsCallback = function (oCallbackData, oResponse) {
    trackMe('failureSaveContentsCallback');
    if (CS.isEmpty(oResponse)) {
      oResponse = oCallbackData;
    }
    var aContentsToSave = oCallbackData.contentsToSave;
    if (!CS.isEmpty(oResponse.failure)) {
      var aExceptionDetails = oResponse.failure.exceptionDetails;
      var oDuplicateVariantExists = CS.find(aExceptionDetails, {key : "DuplicateVariantExistsException"});
      var oEmptyMandatoryFields = CS.find(aExceptionDetails, {key : "EmptyMandatoryFieldsException"});
      var oCardinalityException = CS.find(aExceptionDetails, {key : "CardinalityException"});
      if (!CS.isEmpty(oCardinalityException) || !CS.isEmpty(oEmptyMandatoryFields)) {
        ContentUtils.removeAddedRelationshipElementsOnFailure(aContentsToSave);
      } else if (!CS.isEmpty(oDuplicateVariantExists)){
        VariantStore.removeAttributeVariantInstanceFromContent();
        ContentUtils.removeAddedNatureRelationshipElementsOnFailure(aContentsToSave);
      }

      ContentUtils.makeContentClean(ContentUtils.getActiveContent());
      ContentUtils.failureCallback(oResponse, "failureSaveContentsCallback", getTranslation());
      _checkForExtraNecessaryServerCalls(oResponse);
      if (CS.isNotEmpty(oEmptyMandatoryFields)) {
        return;
      }
    }
    else {
      ExceptionLogger.error("Something went wrong in 'successSaveContentsCallback': ");
      ExceptionLogger.error(oResponse);
      logger.error("Something went wrong in 'successSaveContentsCallback'", oResponse);
    }

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    _triggerChange();

    return false;
  };

  // TODO: Bulk action
  var failureDeleteSelectedContentsCallback = function (oCallBack, oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureDeleteSelectedContentsCallback', getTranslation());
    _triggerChange();

  };

  let successDeleteSelectedFilesCallback = function () {
    let sSuccessMessage = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED, {entity: getTranslation().FILES});
    ContentUtils.showSuccess(sSuccessMessage);
    CommonUtils.refreshCurrentBreadcrumbEntity();
  };

  let failureDeleteSelectedFilesCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureDeleteSelectedFilesCallback', getTranslation());
    _triggerChange();
  };

  /**
   * @author Tauseef Ahmad
   * @function _getEntityPrefix
   * @description TO get the initial of the translation constants
   * @memberOf Stores.ContentStore
   *
   * @param  {string} sBaseType Basetype of Content
   * @return {string}           Prefix of translation key for content
   */
  let _getEntityPrefix = function (sBaseType) {
    const oEntityPrefix = {
      [BaseTypeDictionary.textAssetBaseType]: getTranslation().TEXTASSET_TAB,
      [BaseTypeDictionary.assetBaseType]: getTranslation().ASSET,
      [BaseTypeDictionary.supplierBaseType]: getTranslation().SUPPLIER,
      [BaseTypeDictionary.marketBaseType]: getTranslation().MARKET
    };
    return oEntityPrefix[sBaseType];
  }

  /**
   * @author Tauseef Ahmad
   * @function _getSuccessSaveMessageForContent
   * @memberOf Stores.ContentStore
   *
   * @param  {string} sBaseType Basetype of Content
   * @return {string}           Translated save confirmation message
   */
  let _getSuccessSaveMessageForContent = function (sBaseType) {
    return ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity : `${_getEntityPrefix(sBaseType) ||  getTranslation().CONTENT}`});
  };

  /**
   * @author Tauseef Ahmad
   * @function _getSuccessDeleteMessageForContent
   * @memberOf Stores.ContentStore
   *
   * @param  {string} sBaseType Basetype of Content
   * @return {string}           Translated delete confirmation message
   */
  let _getSuccessDeleteMessageForContent = function (sBaseType) {
    return ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : `${_getEntityPrefix(sBaseType) ||  getTranslation().ARTICLES_LABEL}`});
  };

  /**
   * @function _discardAppliedFilterDirtyChanges
   * @description Used to reset filter props for discard dirty changes.
   * @memberOf Stores.ContentStore
   * @author Ganesh More
   */
  let _discardAppliedFilterDirtyChanges = function (oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    oFilterProps.setAppliedFiltersClone(null);
    oFilterProps.setAppliedFilters(oFilterProps.getAppliedFiltersCloneBeforeModifications());
    oFilterProps.setAppliedFiltersCloneBeforeModifications([]);
  };

  let _resetPropsWhenContentListFetchedByNavigation = function (oFilterContext) {
    /** Reset applied filters dialog props when opening content list(Dialog should not be opened while navigating by browser navigation)**/
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    let bIsAdvancedSearchPanelDialogOpen = oFilterProps.getAdvancedSearchPanelShowStatus();
    if (bIsAdvancedSearchPanelDialogOpen) {
      _discardAppliedFilterDirtyChanges(oFilterContext);
      oFilterProps.setAdvancedSearchPanelShowStatus(false);
    }

    /** Reset Bulk Edit dialog props when opening content list(Dialog should not be opened while navigating by browser navigation)**/
    let oBulkEditProps = ContentScreenProps.bulkEditProps;
    let bBulkEditDialogOpen = oBulkEditProps.getIsBulkEditViewOpen();
    if (bBulkEditDialogOpen) {
      oBulkEditProps.setIsBulkEditViewOpen(false);
    }
    ContentScreenProps.availableEntityViewProps.setAvailableEntityViewContext("");
    let oSetSectionSelectionStatus = ContentScreenProps.screen.getSetSectionSelectionStatus();
    if (CS.isNotEmpty(oSetSectionSelectionStatus.selectedRelationship)) {
      ContentScreenProps.screen.emptySetSectionSelectionStatus();
    }
  };

  var successFetchContentListCallBack = function (oCallbackData, oResponse) {
    oResponse = oResponse.success;
    let oFilterContext = oCallbackData.filterContext;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);

    _resetComparisonViewModeData();
    _resetPropsWhenContentListFetchedByNavigation(oFilterContext);

    var aFetchedContents = oResponse.children;
    var oScreenProps = ContentScreenProps.screen;
    var oReferencedAttributes = oScreenProps.getReferencedAttributes();
    var oReferencedTags = oScreenProps.getReferencedTags();
    CS.assign(oReferencedAttributes, oResponse.referencedAttributes || {});
    CS.assign(oReferencedTags, oResponse.referencedTags || {});
    let oConfigDetails = {
      referencedAttributes: oReferencedAttributes,
      referencedTags: oReferencedTags
    };
    //xray view's concatinated attribute expressionList is prepared using below function
    ContentUtils.prepareConcatenatedAttributeExpressionListForProducts(aFetchedContents,oReferencedAttributes,oReferencedTags, oScreenProps.getReferencedElements());

    CS.forEach(aFetchedContents, function (oContent) {
      CS.forEach(oContent.attributes, function (oAttributes) {
        let oAttribute = CS.find(oReferencedAttributes, {id:oAttributes.attributeId});
        oAttributes.iconKey = oAttribute.iconKey
      });
      CS.forEach(oContent.tags, function (oTag) {
        let oTags = CS.find(oReferencedTags, {id:oTag.tagId});
        oTag.iconKey = oTags.iconKey
      });
    });

    oScreenProps.setFunctionalPermission(oResponse.functionPermission);
    ContentUtils.addEntityInformationData(aFetchedContents);
    _setMasterEntityList(aFetchedContents);
    var oContextData = ContentScreenProps.screen.getContextData();
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    if (oContextData.contentIds) { //i.e. screen context is present
      CS.forEach(oContextData.contentIds, function (sContentId) {
        if (!CS.find(aFetchedContents, {id: sContentId})){
          ContentUtils.showMessage(getTranslation().CONTENT_LIST_REFRESHED); //show message if any id does not match newly fetched ids
          return false; //break the forEach loop even if a single content id mismatches
        }
      });
      delete oContextData.contentIds;
    } else {
      var iFrom = oResponse.from || 0;
      oFilterProps.setFromValue(iFrom);
    }

    /**
     * Set pagination data
     */
    oFilterProps.setTotalItemCount(oResponse.totalContents);
    oFilterProps.setCurrentPageItems(aFetchedContents.length);
    if (oFilterProps.getIsFilterInformationRequired()) {
      oFilterStore.prepareAndUpdateFilterAndSortModel(oResponse, oCallbackData, true);
    } else {
      let oFilterInfo = {
        sortData: oResponse.appliedSortData,
        filterData: oFilterProps.getAvailableFilters()
      };
      oFilterStore.setFilterInfo(oFilterInfo);
    }
    ContentUtils.setFilterProps(oFilterStore.getFilterInfo(), oCallbackData.preventResetFilterProps, oCallbackData.filterContext, oScreenProps.getFilterHierarchySelectedFilters());
    ContentUtils.setLoadedPropertiesFromConfigDetails(oConfigDetails || {});
    oFilterStore.clearTaxonomyTreeBackup();

    let sDefaultViewMode = ContentUtils.getDefaultViewMode();

    if (!CS.isEmpty(oResponse.referencedKlasses)) {
      ContentUtils.updateReferencedKlassRelatedData(oResponse.referencedKlasses);
    }
    if (oResponse.referencedAssets) {
      _preProcessAndSetReferencedAssets(oResponse.referencedAssets);
    }

    if (oCallbackData) {
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    }

    BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);

    //Set Previous breadcrumb when We are inside DATA INTEGRATION,switch to any other menu and back to runtime.
    let sSelectedPhysicalCatalogId = ContentUtils.getSelectedPhysicalCatalogId();
    let oBreadCrumbProps = ContentScreenProps.breadCrumbProps;
    let aPreviousBreadCrumbData = oBreadCrumbProps.getPreviousBreadCrumbData();
    if (sSelectedPhysicalCatalogId === PhysicalCatalogDictionary.DATAINTEGRATION && !CS.isEmpty(aPreviousBreadCrumbData)) {
      oBreadCrumbProps.setBreadCrumbData(aPreviousBreadCrumbData);
      oBreadCrumbProps.setPreviousBreadCrumbData([]);
    }
    //Reset asset shared url on module update.
    ContentScreenProps.screen.setAssetSharedUrl("");

    ContentUtils.setActiveEntity({});
    if (ContentScreenProps.screen.getIsEditMode()) {
      ContentUtils.setViewMode(sDefaultViewMode);
    }

    let oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup();
    if (!CS.isEmpty(oActiveXRayPropertyGroup)) {
      oActiveXRayPropertyGroup.unappliedChanges = false;
    }

    var oVariantSectionViewProps = ContentScreenProps.variantSectionViewProps;
    oVariantSectionViewProps.setEditLinkedVariantInstance({});

    if (oCallbackData.updateBreadCrumb) {
      oCallbackData.updateBreadCrumb(oCallbackData.callbackDataToUpdateBreadCrumb);
    }

    //TODO: Clear Window URL
    SharableURLStore.removeWindowURL();

    let bDisablePhysicalCatalog = ContentUtils.getIsRuleViolatedContentsScreen() ||
        ContentUtils.getIsDamRuleViolatedContentsScreen() ||
        sSelectedPhysicalCatalogId === PhysicalCatalogDictionary.DATAINTEGRATION;
    HomeScreenCommunicator.disablePhysicalCatalog(bDisablePhysicalCatalog);

    oCallbackData.getDefaultTypesCallback && oCallbackData.getDefaultTypesCallback();

    _triggerChange();
    return true;
  };

  var failureFetchContentListCallBack = function (oCallBack, oResponse) {
    let oFilterContext = oCallBack.filterContext;
    ContentUtils.failureCallback(oResponse, 'failureFetchContentListCallBack', getTranslation());
    _checkForExtraNecessaryServerCalls(oResponse);

    var sExceptionKey = oResponse && oResponse.failure && oResponse.failure.exceptionDetails[0].key;
    if (sExceptionKey == "StaticCollectionNotFoundException") {
      _handleAfterEffectsOfStaticCollectionNotFound(oFilterContext);
    } else if (sExceptionKey == "KlassTaxonomyNotFoundException") {
      if (ContentUtils.getSelectedHierarchyContext()) {
        _handleAfterEffectsOfTaxonomyNotFoundInTaxonomyHierarchy(oResponse);
      }
    }
    return false;
  };

  let successFetchFileContentListCallBack = function (oCallbackData, oResponse) {
    let {fileNamesList, from, count, functionPermission} = oResponse.success;
    let aFileInfo = fileNamesList.map(sFileName => {
      let oFileInstance = {};
      oFileInstance.id = sFileName;
      oFileInstance.name = sFileName + ".xlsx";
      oFileInstance.types = ["fileklass"];
      oFileInstance.createdOn = null;
      oFileInstance.lastModified = null;
      return oFileInstance;
    });

    let oFileklass = {
      code: "fileklass",
      id: "fileklass",
      label: "File",
    };
    let oSuccess = {};
    oResponse.success = oSuccess;
    oSuccess.children = aFileInfo;
    oSuccess.from = from;
    oSuccess.totalContents = count;
    oSuccess.functionPermission = functionPermission;
    oSuccess.referencedKlasses = {fileklass: oFileklass};
    successFetchContentListCallBack(oCallbackData, oResponse);
  };

  var _handleAfterEffectsOfStaticCollectionNotFound = function (oFilterContext) {
    //reset module
    var aModuleMenu = GlobalStore.getAllMenus();
    var aTemp = CS.filter(aModuleMenu, {isVisible: true});
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    ContentUtils.setSelectedModuleAndDefaultDataById(CS.head(aTemp).id);
    ContentUtils.setSelectedContentIds([]); //clear any selections
    ContentUtils.clearSelectedAvailableEntities();
    ContentUtils.setActiveEntity({}); //for cases when entity is already opened and a collection is opened
    oFilterStore.resetFilterPropsByContext(); //clear all filter data before opening a collection - if not desired here, do it only for staticCollection
    var oCollectionViewProps = ContentUtils.getComponentProps().collectionViewProps;
    oCollectionViewProps.setActiveCollection({});
    oCollectionViewProps.setAddEntityInCollectionViewStatus(false);
    ContentStore.handleFilterButtonClicked();
  };

  var _handleSectionMaskClicked = function (sSectionId, sElementId, sContext, oFilterContext) {
    CustomActionDialogStore.showConfirmDialog(getTranslation().BREAK_INHERITANCE_CONFIRMATION, '',
        function () {
          var oScreenProps = ContentScreenProps.screen;
          var oActiveEntity = {};
          let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
          switch (sContext) {
            case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
              oActiveEntity = oScreenProps.getActiveHierarchyCollection();
              break;
            case HierarchyTypesDictionary.TAXONOMY_HIERARCHY:

              oActiveEntity = oFilterStore.getFilterProps().getTaxonomyNodeSections();
              break;
          }

          _updateEntityElement(oActiveEntity, sSectionId, sElementId, "isInherited", false);
          if (oActiveEntity.clonedObject) {
            _updateEntityElement(oActiveEntity.clonedObject, sSectionId, sElementId, "isInherited", false);
          }

          _triggerChange();
        }, function (oEvent) {
        });
  };

  var _updateEntityElement = function (oActiveEntity, sSectionId, sElementId, sKey, sValue) {
    var oSection = CS.find(oActiveEntity.sections, {id: sSectionId});
    var oElement = CS.find(oSection.elements, {id: sElementId});
    oElement[sKey] = sValue;
  };

  var _processVariantContextData = function (oReferencedVariantContexts) {
    var oScreenProps = ContentScreenProps.screen;
    var oReferencedTags = oScreenProps.getReferencedTags();
    CS.forEach(oReferencedVariantContexts, function (aReferencedVariantContexts) {
      CS.forEach(aReferencedVariantContexts, function (oVariantContext) {
        var aContextTags = [];
        CS.forEach(oVariantContext.tags, function (oContextTag) {
          var oMasterTag = oReferencedTags[oContextTag.tagId]; //color, icon, label, id, tagType, children
          if (oMasterTag) {
            var aChildren = [];
            var oTagGroup = {};
            oTagGroup.id = oMasterTag.id;
            oTagGroup.icon = oMasterTag.icon;
            oTagGroup.label = oMasterTag.label;
            oTagGroup.color = oMasterTag.color;
            oTagGroup.tagType = oMasterTag.tagType;
            oTagGroup.code = oMasterTag.code;
            oTagGroup.children = aChildren;

            CS.forEach(oContextTag.tagValueIds, function (sTagId) {
              var oTagChildren = CS.find(oMasterTag.children, {id: sTagId});
              if (oTagChildren) {
                aChildren.push(oTagChildren);
              }
            });
          }

          aContextTags.push(oTagGroup);
        });

        oVariantContext.tags = aContextTags;
      });

    });
    oScreenProps.setReferencedVariantContexts(oReferencedVariantContexts);

  };

  let prepareMinorTaxonomySections = function (oMainSection, aElements, sTaxonomyElementId, oTaxonomyAddedFlags, oSectionsAddedFlags, oElement, bIsSaveResponse) {

    let oScreenProps = ContentScreenProps.screen;
    let oReferencedPropertyCollections = oScreenProps.getReferencedPropertyCollections();
    let oReferencedTaxonomies = oScreenProps.getReferencedTaxonomies();
    var oReferencedElements = oScreenProps.getReferencedElements();
    var oReferencedAttributes = oScreenProps.getReferencedAttributes();
    var oReferencedTags = oScreenProps.getReferencedTags();
    var oReferencedRoles = oScreenProps.getReferencedRoles();
    var oReferencedRelationships = oScreenProps.getReferencedRelationships();
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oElementsHavingConflicts = {};

    let oReferencedRootTaxonomy = oReferencedTaxonomies[sTaxonomyElementId];
    let aTaxonomyIds = CS.map(oReferencedRootTaxonomy.children, function (oTaxonomy) {
      return oTaxonomy.id;
    });
    aTaxonomyIds.push(sTaxonomyElementId);
    let aAppliedTaxonomyIds = CS.intersection(aTaxonomyIds, oScreenProps.getActiveTaxonomyIds());
    oElement.isComplete = true;
    if (CS.isEmpty(aAppliedTaxonomyIds)) {
      oElement.isComplete = false;
    }

    CS.forEach(aAppliedTaxonomyIds, function (sTaxonomyId) {
      let aTaxonomyPCIds = oReferencedTaxonomies[sTaxonomyId].propertyCollections;

      if (CS.isEmpty(aTaxonomyPCIds)) {
        return;
      }

      aElements.push(
          {
            type: "divider",
            label: oReferencedTaxonomies[sTaxonomyId].label,
          }
      );

      let oTaxonomySection = CS.find(aElements, {id: sTaxonomyElementId});
      oTaxonomySection.elements = oTaxonomySection.elements || [];

      let sLabel = CS.getLabelOrCode(oReferencedTaxonomies[sTaxonomyId].rootNodeInfo) + " : " + CS.getLabelOrCode(oReferencedTaxonomies[sTaxonomyId]);
      let oDummyElement = {
        id: sTaxonomyId,
        type: "filler",
        label: sLabel,
        elements: []
      };


      CS.forEach(aTaxonomyPCIds, function (sSectionId) {
        let bIsPCDisabled = false;
        /**if PC is available in class, then it should not be repeated through taxonomy**/
        if (!oReferencedPropertyCollections[sSectionId] || oSectionsAddedFlags[sSectionId]) {
          return;
        }
        oSectionsAddedFlags[sSectionId] = true;
        let oSection = CS.cloneDeep(oReferencedPropertyCollections[sSectionId]);

        /**Once a taxonomy and its section is added The same taxonomy section will not be added again**/
        let aFilteredElements = [];
        CS.forEach(oSection.elements, function (oElement) {
          if (!oTaxonomyAddedFlags[oElement.id]) {
            aFilteredElements.push(oElement);
          }
        });
        oSection.elements = aFilteredElements;

        if (CS.isEmpty(oSection.elements)) {
          return;
        }

        //TODO: REMOVE ONCE FIXED FROM BACKEND
        oSection.id = sSectionId;
        let bIsContentAvailableInSelectedDataLanguage = ContentUtils.isContentAvailableInSelectedDataLanguage();

        CS.forEach(oSection.elements, function (oElementValue) {
          var sElementId = oElementValue.id;
          var oReferencedElement = oReferencedElements[sElementId];
          let oReferencedAttribute = oReferencedAttributes[sElementId];

          if (CS.isEmpty(oReferencedElement) || oReferencedElement.isSkipped) {
            return;
          }
          _prepareElementsHavingConflictingValues(oElementsHavingConflicts, oReferencedElement, bIsSaveResponse, oReferencedAttribute);

          oReferencedElement.sections = oReferencedElement.sections || [];
          oReferencedElement.sections.push(sSectionId);

          var oElement = CS.cloneDeep(oReferencedElement);
          switch (oElement.type) {
            case "attribute":
              oElement.attribute = oReferencedAttributes[sElementId];
              oElement.canResolveConflicts = bIsContentAvailableInSelectedDataLanguage && !oElement.isDisabled;

              /**For read only user with attribute context**/
              if(oElement.attributeVariantContext && ContentUtils.isContentAvailableInSelectedDataLanguage() &&
                  ContentUtils.getIsCurrentUserReadOnly()) {
                oElement.isDisabled = (oElement.attribute.isDisabled ? oElement.attribute.isDisabled : oElement.isDisabled);
              } else {
                oElement.isDisabled = oElement.attribute.isDisabled ? oElement.attribute.isDisabled : (oElement.isDisabled || oElement.couplingType === CouplingConstants.DYNAMIC_COUPLED || oElement.couplingType === CouplingConstants.READ_ONLY_COUPLED);
              }

              let oAttrInstance = CS.find(oActiveEntity.attributes, {attributeId: sElementId});
              if (ContentUtils.isAttributeTypeConcatenated(oElement.attribute.type) || ContentUtils.isAttributeTypeCalculated(oElement.attribute.type)) {
                oElement.isComplete = true;
              } else if (oAttrInstance) {
                oElement.isComplete = !CS.isEmpty(oAttrInstance.value);
                if (oAttrInstance.attributeId === "assetcoverflowattribute") {
                  oElement.isComplete = !CS.isEmpty(oAttrInstance.assetObjectKey)
                }
              } else {
                oElement.isComplete = false;
              }
              oElement.isDependent = oReferencedAttributes[sElementId].isTranslatable;
              oElement.isViolated = _checkViolation(oAttrInstance, oActiveEntity, sElementId);
              break;
            case "tag":
              let oTagInstance = CS.find(oActiveEntity.tags, {tagId: sElementId});
              let bIsContentEditable = !ContentUtils.getIsContentDisabled();
              oElement.canResolveConflicts = bIsContentEditable && !oElement.isDisabled; // eslint-disable-line
              oElement.isDisabled = (oElement.isDisabled || oElement.couplingType === CouplingConstants.DYNAMIC_COUPLED || oElement.couplingType === CouplingConstants.READ_ONLY_COUPLED);
              if (oTagInstance) {
                let aNonEmptyTagValues = CS.filter(oTagInstance.tagValues, function (oTagValue) {
                  return oTagValue.relevance !== 0;
                });
                oElement.isComplete = !CS.isEmpty(aNonEmptyTagValues);
              } else {
                oElement.isComplete = false;
              }
              oElement.isViolated = _checkViolation(oTagInstance, oActiveEntity, sElementId);
              oElement.tag = oReferencedTags[sElementId];
              break;
            case "role":
              oElement.role = oReferencedRoles[sElementId];
              break;
            case "relationship":
              oElement.relationship = oReferencedRelationships[sElementId];
              break;
          }

          if (bIsPCDisabled) {
            oElement.isDisabled = true;
          }
          oDummyElement.elements.push(oElement);
          if (oElement.type === "taxonomy") {
            oElement.minorTaxonomiesData = NewMinorTaxonomyStore.setMinorTaxonomiesData(sElementId);
            oTaxonomyAddedFlags[sElementId] = true;
            let {oElementsHavingConflicts: oConflictingElement} = prepareMinorTaxonomySections(oMainSection, oDummyElement.elements, sElementId, oTaxonomyAddedFlags, oSectionsAddedFlags, oElement);
            CS.assign(oElementsHavingConflicts, oConflictingElement);
          }

        });

        oTaxonomySection.elements.push(oDummyElement);

        oMainSection.columns < oSection.columns && (oMainSection.columns = oSection.columns);
      });

      aElements.push(
          {
            type: "divider",
          }
      );

    });

    return { oElementsHavingConflicts};
  };

  let _getDesiredReferencedDataAndTypeBySectionId = (sSectionId) => {
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedData = {
      referencedPropertyCollections: oScreenProps.getReferencedPropertyCollections() || {},
      referencedRelationships: oScreenProps.getReferencedRelationships() || {},
      referencedNatureRelationships: oScreenProps.getReferencedNatureRelationships() || {},
      referencedVariantContexts: oScreenProps.getReferencedVariantContexts() || {}
    };

    return ContentUtils.getDesiredReferencedDataAndTypeBySectionId(sSectionId, oReferencedData);
  };

  let _getNatureRelationshipSectionInformation = function (oReferencedContextsData, oReferencedData, sContext) {
      let oReferencedContext = oReferencedContextsData[sContext];
      let bOtherSideNatureRelationship = CS.isNotEmpty(oReferencedContext);
      let sLabel, sCode, sIcon;

      if(bOtherSideNatureRelationship) {
        sLabel = oReferencedContext.label;
        sCode = oReferencedContext.code;
        sIcon = oReferencedContext.icon;
      } else {
          sLabel = oReferencedData.label;
          sCode = oReferencedData.code;
          sIcon = oReferencedData.icon;
      }

      return {
         label: sLabel,
         code: sCode,
         icon: sIcon
      }
  };

  let _showBulkAssetUploadInRelationship = function (oReferencedRelationship, oOtherSide) {
    return oReferencedRelationship.id === "standardArticleAssetRelationship" && oOtherSide.klassId === "asset_asset";
  };

  let _setRelationshipAndNatureRelationshipSectionData = function (oSection, sSectionId, oReferencedData, oNewSectionVisualProps,
                                                                   oReferencedElements, oReferencedVariantContext, oReferencedRelationships,
                                                                   sInnerViewContext) {
    oSection.isCollapsed = false;
    oSection.isHidden = false;
    oSection.rows = 1;
    oSection.columns = 1;
    oSection.elements = [];

    let oSectionTypes = ContentScreenConstants.sectionTypes;
    let oReferencedElement = oReferencedElements[sSectionId];
    let sRelationshipId = oReferencedElement.propertyId;
    let oRelationshipSide = oReferencedElement.relationshipSide;
    let sContextId = oRelationshipSide.contextId;
    let sLabel = "";
    let sIcon = "";
    let sCode = "";
    let bShowBulkAssetUploadInRelationship = false;
    if (ContentUtils.isVariantRelationship(oReferencedData.relationshipType)) {
        let oSectionInfo = _getNatureRelationshipSectionInformation(oReferencedVariantContext.productVariantContexts, oReferencedData, sContextId);
        sLabel = oSectionInfo.label;
        sCode = oSectionInfo.code;
    } else if (oSection.type === oSectionTypes.SECTION_TYPE_RELATIONSHIP) {
      let oReferencedRelationship = oReferencedRelationships[sRelationshipId];
      let oOtherSide = oRelationshipSide.code == oReferencedRelationship.side1.code ? oReferencedRelationship.side1 : oReferencedRelationship.side2;
      sLabel = oOtherSide.label;
      sCode = oOtherSide.code;
      bShowBulkAssetUploadInRelationship = _showBulkAssetUploadInRelationship(oReferencedRelationship, oOtherSide);
    }

    oSection.label = sLabel;
    oSection.code = sCode;

    oNewSectionVisualProps[sSectionId] = {
      isHidden: false,
      isExpanded: !oSection.isCollapsed
    };

    let oPosition = {x: 0, y: 0};
    if (oReferencedElement.isSkipped) {
      return;
    }

    oReferencedElement.sections = oReferencedElement.sections || [];
    oReferencedElement.sections.push(sSectionId);

    let oElement = CS.cloneDeep(oReferencedElement);
    let oActiveEntity = ContentUtils.getActiveEntity();
    oElement.relationship = oReferencedData;
    oElement.position = oPosition;
    _setCouplingIconAndConflictValuesOfRelationship(oElement, oActiveEntity);

    let bIsContentEditable = !ContentUtils.getIsContentDisabled();
    oElement.isDisabled = !bIsContentEditable || oElement.isDisabled;
    oElement.canDelete = bIsContentEditable && oElement.canDelete;
    oElement.canAdd = bIsContentEditable && oElement.canAdd;
    oElement.showBulkAssetUploadInRelationship = bShowBulkAssetUploadInRelationship && oElement.canAdd;

    oSection.elements.push(oElement);
    oSection.natureType = oReferencedData.natureType;
    oSection.icon = oReferencedData.icon || sIcon;
    oSection.iconKey = oReferencedData.iconKey || "";
    oSection.viewContext = ContentScreenViewContextConstants.RELATIONSHIP + ContentUtils.getSplitter() + sInnerViewContext;


    let oRelationshipInfo = CS.find(oActiveEntity.natureRelationships, {relationshipId: sRelationshipId});

    if (!oRelationshipInfo) {
      oRelationshipInfo = CS.find(oActiveEntity.contentRelationships, {relationshipId: sRelationshipId, sideId: oElement.id});
    }
    if (oRelationshipInfo && CS.isEmpty(oRelationshipInfo.elementIds)) {
      oSection.isEmpty = true;
    }
  };

  let _getIsContextEmpty = (sContextId) => {
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedVariantContexts = oScreenProps.getReferencedVariantContexts();
    let oSelectedContext = oReferencedVariantContexts.embeddedVariantContexts[sContextId]
        || oReferencedVariantContexts.makeupContexts[sContextId] || {};
    var bIsTimeEnabled = oSelectedContext.isTimeEnabled;
    var aVariantTags = oSelectedContext.tags;
    return !bIsTimeEnabled && CS.isEmpty(aVariantTags) && CS.isEmpty(oSelectedContext.entities);
  };

  let _getRelationshipData = function (sSectionId) {
    let oRelationshipToolbarProps = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
    let oRelationshipProps = oRelationshipToolbarProps[sSectionId];
    if (oRelationshipProps.isXRayEnabled) {
      ContentRelationshipStore.fetchRelationship(sSectionId);
    }
  };

  let _prepareElementsHavingConflictingValues = function (oElementsHavingConflicts, oReferencedElement, bIsSaveResponse, oReferencedAttribute) {
    /** We can't apply coupling for aAttributesToExclude **/
    let aAttributesToExclude = [AttributesTypeDictionary.IMAGE_COVERFLOW, AttributesTypeDictionary.CALCULATED, AttributesTypeDictionary.CONCATENATED];
    if (oReferencedAttribute && CS.includes(aAttributesToExclude, oReferencedAttribute.type)) {
      return;
    }

    let oActiveContent = ContentUtils.getActiveEntity();
    let aContentAttributes = oActiveContent.attributes;
    let bConflictPresent = false;

    if (!CS.isEmpty(oReferencedElement.conflictingSources)) {
      bConflictPresent = true;
    } else {
      /** For language inheritance conflicting details will get in attributes instance not in referenced element **/
      let oAttribute = CS.find(aContentAttributes, {attributeId: oReferencedElement.id});
      bConflictPresent = oAttribute && !CS.isEmpty(oAttribute.conflictingValues);
    }

    let oExpandedStatus = {
      isExpanded: false
    };

    if (bConflictPresent) {
      if (bIsSaveResponse) {
        let oOldElementsWithConflicts = ContentScreenProps.screen.getElementsHavingConflictingValues();
        if (oOldElementsWithConflicts[oReferencedElement.id]) {
          oExpandedStatus = oOldElementsWithConflicts[oReferencedElement.id];
        }
      }
      oElementsHavingConflicts[oReferencedElement.id] = oExpandedStatus;
    }
  };

  let _checkViolation = (oElementInstance, oActiveEntity, sElementId) => {
    return (oElementInstance && (oElementInstance.isMandatoryViolated || oElementInstance.isShouldViolated
        || (CS.size(oElementInstance.conflictingValues) > 1) || !CS.isEmpty(oElementInstance.notification)
        || !CS.isEmpty(oElementInstance.duplicateStatus))) || !CS.isEmpty(CS.filter(oActiveEntity.ruleViolation, {entityId: sElementId}));
  };

  var _prepareActiveSectionsNew = function (bIsSaveResponse) {

    /** todo: CT,
     * Get selected tab
     * get sequence ids
     * determine the type of each section and prepare data accordingly*/
    let oScreenProps = ContentScreenProps.screen;
    let oSectionProps = ContentScreenProps.contentSectionViewProps;
    let oReferencedTemplate = oScreenProps.getReferencedTemplate();
    let oNewSectionVisualProps = {};
    oSectionProps.setSectionVisualProps(oNewSectionVisualProps);

    let oActiveEntity = ContentUtils.getActiveEntity();
    let oSectionTypes = ContentScreenConstants.sectionTypes;
    let oFirstCustomTab = CS.find(oReferencedTemplate.tabs, {id: TemplateTabsDictionary.OVERVIEW_TAB});
    if (oFirstCustomTab) {
      if (!CS.isEmpty(oActiveEntity.context) && !_getIsContextEmpty(oActiveEntity.context.contextId)) {
        oFirstCustomTab.propertySequenceList.unshift(oSectionTypes.SECTION_TYPE_CONTEXT_SELECTION);
      }
      if ((oActiveEntity.baseType === BaseTypesDictionary.assetBaseType) && CS.isEmpty(oActiveEntity.context)) {
        oFirstCustomTab.propertySequenceList.unshift(oSectionTypes.SECTION_TYPE_SCHEDULE_SELECTION);
      }
      oFirstCustomTab.propertySequenceList.unshift(oSectionTypes.SECTION_TYPE_LIFE_CYCLE_STATUS);
      oFirstCustomTab.propertySequenceList.unshift(oSectionTypes.SECTION_TYPE_CLASSIFICATION);
      if(oActiveEntity.baseType == BaseTypeDictionary.assetBaseType && oScreenProps.getActiveContentClass().trackDownloads){
        oFirstCustomTab.propertySequenceList.unshift(oSectionTypes.SECTION_TYPE_DOWNLOAD_INFO);
      }
      oFirstCustomTab.propertySequenceList.unshift(oSectionTypes.SECTION_TYPE_PERFORMANCE_INDICES);
    }
    let oSelectedTab = CS.find(oReferencedTemplate.tabs, {isSelected: true}) || oReferencedTemplate.tabs[0];

    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[oActiveEntity.id];

    /** Set the currently selected tab into props*/
    if (CS.isEmpty(oActiveContentMap)) {
      oActiveEntitySelectedTabIdMap[oActiveEntity.id] = {
        selectedTabId: oSelectedTab.id,
        selectedFilterId: ContentEditFilterItemsDictionary.ALL
      };
    } else {
      oActiveEntitySelectedTabIdMap[oActiveEntity.id].selectedTabId = oSelectedTab.id;
    }

    let aSectionIdsBySequence = oSelectedTab.propertySequenceList;


    /**
     * Warning: This is work around is made just for self relationship.
     * _updateSectionIdListBySequenceForSelfRelationship - Shashank
     * */
    aSectionIdsBySequence = ContentUtils.updateSectionIdListBySequenceForSelfRelationship(aSectionIdsBySequence);

    let oConfigDetails = {
      referencedElements: oScreenProps.getReferencedElements(),
      referencedAttributes: oScreenProps.getReferencedAttributes(),
      referencedTags: oScreenProps.getReferencedTags(),
      referencedRoles: oScreenProps.getReferencedRoles(),
      referencedRelationships: oScreenProps.getReferencedRelationships(),
      referencedVariantContexts: oScreenProps.getReferencedVariantContexts(),
      referencedPermissions: oScreenProps.getReferencedPermissions()
    };

    let {aSections, aNatureSections, oElementsHavingConflicts} = _prepareSections(aSectionIdsBySequence, oNewSectionVisualProps, bIsSaveResponse, oActiveEntity, oConfigDetails);

    if (oSelectedTab.id === ContentScreenViewContextConstants.USAGE_TAB) {
      _disableDeleteInUsageTab(aSections);
    }
    oScreenProps.setElementsHavingConflictingValues(oElementsHavingConflicts);
    //todo try to treat data manipulation as normal section
    oScreenProps.setActiveNatureSections(aNatureSections);
    oScreenProps.setActiveSections(aSections);
  };

  let _disableDeleteInUsageTab = (aSections) => {
    aSections.forEach(function (oSection) {
      oSection.elements.length ? oSection.elements[0].canDelete = false : ""; // eslint-disable-line
    });
  };

  let _prepareSections = function (aSectionIdsBySequence, oNewSectionVisualProps, bIsSaveResponse, oActiveEntity, oConfigDetails) {
    let aSections = [];
    let aNatureSections = [];
    let oTaxonomyAddedFlags = {};
    let oSectionsAddedFlags = {};
    let oElementsHavingConflicts = {};

    let oSectionProps = ContentScreenProps.contentSectionViewProps;
    let oSectionVisualProps = oSectionProps.getSectionVisualProps();
    let oReferencedElements = oConfigDetails.referencedElements;
    let oReferencedAttributes = oConfigDetails.referencedAttributes;
    let oReferencedTags = oConfigDetails.referencedTags;
    let oReferencedRoles = oConfigDetails.referencedRoles;
    let oReferencedRelationships = oConfigDetails.referencedRelationships;

    let oReferencedVariantContext = oConfigDetails.referencedVariantContexts;
    let oSectionTypes = ContentScreenConstants.sectionTypes;
    let oActiveEntitySelectedTabIdMap = ContentScreenProps.screen.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[oActiveEntity.id];
    let bLanguageComparisonModeOn = oActiveContentMap.languageComparisonModeOn;
    let oEmbeddedVariantContexts = oReferencedVariantContext.embeddedVariantContexts;
    let bIsAutoCreate = false;
    let sSelectedTabId = oActiveContentMap.selectedTabId;
    if (ContentUtils.getNatureTypeFromContent(oActiveEntity) === "technicalImage" && CS.isEqual(sSelectedTabId, "property_collection_tab") && CS.isNotEmpty(oEmbeddedVariantContexts)) {
      bIsAutoCreate = oEmbeddedVariantContexts[oActiveEntity.context.contextId].isAutoCreate;
    }


    CS.forEach(aSectionIdsBySequence, function (sSectionId) {

      let sIdToFetchData = '';
      if (sSectionId.relationshipId) {
        sIdToFetchData = sSectionId.relationshipId;
        sSectionId = sSectionId.sideId;
      } else {
        sIdToFetchData = sSectionId;
      }
      //determine the type of section
      let {oReferencedData, sSectionType} = _getDesiredReferencedDataAndTypeBySectionId(sIdToFetchData);
      let oSection = {
        id: sSectionId,
        type: sSectionType,
      };
      oSectionsAddedFlags[sSectionId] = true;

      if (bLanguageComparisonModeOn && sSectionType === "context") {
        return;
      }

      switch (sSectionType) {

        case oSectionTypes.SECTION_TYPE_CONTEXT_SELECTION:
          oSection.elements = [];
          oSection.id = oSectionTypes.SECTION_TYPE_CONTEXT_SELECTION;
          oSection.label = getTranslation().CONTEXT_CONFIGURATION;
          break;

        case oSectionTypes.SECTION_TYPE_SCHEDULE_SELECTION:
          oSection.elements = [];
          oSection.id = oSectionTypes.SECTION_TYPE_SCHEDULE_SELECTION;
          oSection.label = getTranslation().CONFIGURATION;
          break;

        case oSectionTypes.SECTION_TYPE_PROPERTY_COLLECTION:
          CS.defaults(oSection, oReferencedData);

          let oSectionVisualProp = oSectionVisualProps[sSectionId];
          let oNewSectionVisualProp = {};

          if (oSectionVisualProp) {
            oNewSectionVisualProp = oSectionVisualProp;
          } else {
            oNewSectionVisualProp = {
              isHidden: false,
              isExpanded: !oSection.isCollapsed
            };
          }
          oNewSectionVisualProps[sSectionId] = oNewSectionVisualProp;

          let aElements = [];

          //ForDemo
          CS.forEach(oSection.elements, function (oElementValue) {
            //ForDemo

            let sElementId = oElementValue.id;
            let oPosition = oElementValue.position;
            let oReferencedElement = oReferencedElements[sElementId];
            if (CS.isEmpty(oReferencedElement) || oReferencedElement.isSkipped) {
              return;
            }

            let oReferencedAttribute = oReferencedAttributes[sElementId];
            _prepareElementsHavingConflictingValues(oElementsHavingConflicts, oReferencedElement, bIsSaveResponse, oReferencedAttribute);

            oReferencedElement.sections = oReferencedElement.sections || [];
            oReferencedElement.sections.push(sSectionId);

            let oElement = oReferencedElement;
            let bIsContentEditable = !ContentUtils.getIsContentDisabled();

            switch (oElement.type) {
              case "attribute":
                oElement.attribute = oReferencedAttributes[sElementId];
                oElement.canResolveConflicts = bIsContentEditable && !oElement.isDisabled;

                /**For read only user with attribute context**/
                if(oElement.attributeVariantContext && ContentUtils.isContentAvailableInSelectedDataLanguage() &&
                    ContentUtils.getIsCurrentUserReadOnly()) {
                  oElement.isDisabled = (oElement.attribute.isDisabled ? oElement.attribute.isDisabled : oElement.isDisabled);
                } else {
                  oElement.isDisabled = !bIsContentEditable || (oElement.attribute.isDisabled ? oElement.attribute.isDisabled : oElement.isDisabled);
                }

                oElement.showDisconnected = true;
                let oAttrInstance = CS.find(oActiveEntity.attributes, {attributeId: sElementId});

                if (ContentUtils.isAttributeTypeCalculated(oElement.attribute.type) && oAttrInstance) {
                  oElement.isComplete = !CS.isEmpty(oAttrInstance.value);
                } else if (ContentUtils.isAttributeTypeConcatenated(oElement.attribute.type) && oAttrInstance) {
                  let aExpressionList = ContentUtils.getConcatenatedAttributeExpressionList(oAttrInstance, oActiveEntity.attributes, oActiveEntity.tags, oReferencedAttributes, oReferencedTags, oReferencedElements);
                  CS.forEach(aExpressionList, function (oExpression) {
                    oElement.isComplete = !CS.isEmpty(oExpression.value) || !CS.isEmpty(oExpression.valueAsHtml);
                    if (oElement.isComplete) return false;
                  });
                } else if (oAttrInstance) {
                  oElement.isComplete = !CS.isEmpty(oAttrInstance.value);
                  if (oAttrInstance.attributeId === "assetcoverflowattribute") {
                    oElement.isComplete = !CS.isEmpty(oAttrInstance.assetObjectKey)
                  }
                } else {
                  oElement.isComplete = false;
                }
                oElement.isDependent = oReferencedAttributes[sElementId].isTranslatable;
                oElement.isViolated = _checkViolation(oAttrInstance, oActiveEntity, sElementId);

                if ((ContentUtils.isAttributeTypePrice(oElement.attribute.type) || ContentUtils.isAttributeTypeNumber(oElement.attribute.type)
                        || ContentUtils.isAttributeTypeMeasurement(oElement.attribute.type) || ContentUtils.isAttributeTypeCalculated(oElement.attribute.type))
                    && CS.isNumber(oReferencedElements[sElementId].precision)) {
                  oElement.attribute.precision = oReferencedElements[sElementId].precision;
                }

                break;
              case "tag":
                oElement.tag = oReferencedTags[sElementId];

                oElement.canResolveConflicts = bIsContentEditable && !oElement.isDisabled;
                let oTagInstance = CS.find(oActiveEntity.tags, {tagId: sElementId});
                if (oTagInstance) {
                  let aNonEmptyTagValues = CS.filter(oTagInstance.tagValues, function (oTagValue) {
                    return oTagValue.relevance !== 0;
                  });
                  oElement.isComplete = !CS.isEmpty(aNonEmptyTagValues);
                } else {
                  oElement.isComplete = false;
                }
                oElement.isViolated = _checkViolation(oTagInstance, oActiveEntity, sElementId);
                oElement.isDisabled = !bIsContentEditable || oElement.tag.isDisabled || oElement.isDisabled;
                if (bIsAutoCreate && (oElement.tag.id === "imageextensiontag" || oElement.tag.id === "resolutiontag")) {
                  oElement.isDisabled = true;
                }

                break;
              case "role":
                oElement.role = oReferencedRoles[sElementId];
                break;
              case "relationship":
                oElement.relationship = oReferencedRelationships[sElementId];
                break;
            }

            oElement.position = oPosition;
            if (oElement.type.toLocaleLowerCase() === "taxonomy" && oActiveEntity.variantInstanceId) {
              //Do not show taxonomy option in variants.
              //return;
            }
            if(oElement.type.toLocaleLowerCase() === "taxonomy") {
              oElement.minorTaxonomiesData = NewMinorTaxonomyStore.setMinorTaxonomiesData(sElementId);
            }
            aElements.push(oElement);
            if (oElement.type.toLocaleLowerCase() === "taxonomy" && oElement.canRead) {
              oTaxonomyAddedFlags[sElementId] = true;
              let {oElementsHavingConflicts: oMinorTaxonomyConflictingElements} = prepareMinorTaxonomySections(oSection, aElements, sElementId, oTaxonomyAddedFlags, oSectionsAddedFlags, oElement, bIsSaveResponse);
              if (!CS.isEmpty(oMinorTaxonomyConflictingElements)) {
                CS.assign(oElementsHavingConflicts, oMinorTaxonomyConflictingElements);
              }
            }

          });

          //Below code is to skip empty section
          if (CS.isEmpty(aElements)) {
            return;
          } else {
            oSection.elements = aElements;
          }

          //ForDemo
          break;
          /** ------------------------------------------------------------------------------------------------------- */

        case oSectionTypes.SECTION_TYPE_RELATIONSHIP:
          oSection.relationshipId = sIdToFetchData;
          let sInnerViewContext = ContentScreenViewContextConstants.CONTENT_RELATIONSHIP;
          _setRelationshipAndNatureRelationshipSectionData(oSection, sSectionId, oReferencedData, oNewSectionVisualProps,
              oReferencedElements, {}, oReferencedRelationships, sInnerViewContext);
          let oReferencedElement = oSection.elements[0];
          let oReferencedRelationship = oReferencedRelationships[oSection.relationshipId];
          _prepareElementsHavingConflictingValues(oElementsHavingConflicts, oReferencedElement, bIsSaveResponse, oReferencedRelationship);

          /** Preserve the relationship active state**/
          _getRelationshipData(sSectionId);
          break;

          /** ------------------------------------------------------------------------------------------------------- */

        case oSectionTypes.SECTION_TYPE_NATURE_RELATIONSHIP:
          oSection.relationshipId = sIdToFetchData;
          if (ContentUtils.isVariantRelationship(oReferencedData.relationshipType)) {
            _setRelationshipAndNatureRelationshipSectionData(oSection, sSectionId, oReferencedData, oNewSectionVisualProps,
                oReferencedElements, oReferencedVariantContext, {}, ContentScreenViewContextConstants.NATURE_TYPE_RELATIONSHIP);
          }
          else {
            CS.defaults(oSection, oReferencedData);
            oSection.viewContext = ContentScreenViewContextConstants.NATURE_RELATIONSHIP + ContentUtils.getSplitter() + ContentScreenViewContextConstants.NATURE_TYPE_RELATIONSHIP;
            let oRelationshipInfo = CS.find(oActiveEntity.natureRelationships, {relationshipId: sSectionId});
            if (oRelationshipInfo && CS.isEmpty(oRelationshipInfo.elementIds)) {
              oSection.isEmpty = true;
            }
            aNatureSections.push(oSection);
          }

          /** Preserve the relationship active state**/
          _getRelationshipData(sSectionId);
          break;
          /** ------------------------------------------------------------------------------------------------------- */

        case oSectionTypes.SECTION_TYPE_CONTEXT:
          oSection.icon = oReferencedData.icon;
          oSection.iconKey = oReferencedData.iconKey;
          oSection.label = oReferencedData.label;
          oSection.code = oReferencedData.code;
          oSection.isContextSection = true;
          oSection.elements = [];
          oSection.referencedContext = oReferencedData;

          let oOldSectionVisualProp = oSectionVisualProps[oReferencedData.id];
          if (CS.isEmpty(oOldSectionVisualProp)) {
            oNewSectionVisualProps[oReferencedData.id] = {
              isHidden: false,
              isExpanded: true
            }
          } else {
            oNewSectionVisualProps[oReferencedData.id] = oOldSectionVisualProp;
          }
          break;

        case oSectionTypes.SECTION_TYPE_PERFORMANCE_INDICES:
          oSection.elements = [];
          oSection.id = oSectionTypes.SECTION_TYPE_PERFORMANCE_INDICES;
          oSection.label = getTranslation().PERFORMANCE_INDICES;
          break;

        case oSectionTypes.SECTION_TYPE_CLASSIFICATION:
          oSection.elements = [];
          oSection.id = oSectionTypes.SECTION_TYPE_CLASSIFICATION;
          oSection.label = getTranslation().CLASSIFICATIONS;
          break;

        case oSectionTypes.SECTION_TYPE_LIFE_CYCLE_STATUS:
          oSection.elements = [];
          oSection.id = oSectionTypes.SECTION_TYPE_LIFE_CYCLE_STATUS;
          oSection.label = getTranslation().LIFECYCLE_STATUS;
          break;

        case oSectionTypes.SECTION_TYPE_DOWNLOAD_INFO:
          oSection.elements = [];
          oSection.id = oSectionTypes.SECTION_TYPE_DOWNLOAD_INFO;
          oSection.label = getTranslation().DOWNLOAD_INFO;  //add in translation - contentScreen
          break;

        case oSectionTypes.SECTION_TYPE_INSTANCE_PROPERTIES:
          oSection.id = oSectionTypes.SECTION_TYPE_INSTANCE_PROPERTIES;
          oSection.elements = _getElementsForInstanceProperties();
          break;
      }

      aSections.push(oSection);
    });

    return {aSections, aNatureSections, oElementsHavingConflicts};
  };

  let _setCouplingIconAndConflictValuesOfRelationship = (oElement, oActiveEntity) => {
    if (CS.isNotEmpty(oActiveEntity.relationshipConflictingValues) || oElement.conflictingSources) {
      let aRelationshipConflictMap = oActiveEntity.relationshipConflictingValues;
      let aContentRelationships = oActiveEntity.contentRelationships;
      let oRelationshipConflict = CS.find(aRelationshipConflictMap, {propagableRelationshipId: oElement.propertyId, propagableRelationshipSideId: oElement.id});
      if (oRelationshipConflict) {
        let aConflictingValues = [];
        let oRelationship = CS.find(aContentRelationships, {
          relationshipId: oRelationshipConflict.propagableRelationshipId,
          sideId: oRelationshipConflict.propagableRelationshipSideId
        });
        let isConflictResolved = oRelationshipConflict.isResolved;
        if (oRelationship) {
          CS.forEach(oRelationshipConflict.conflicts, (oConflict) => {
            let oConflictingValue = {
              couplingType: oConflict.couplingType,
              source: {
                contentId: oConflict.sourceContentId,
                id: oConflict.relationshipId,
                type: "relationshipInheritance"
              },
              isResolved: isConflictResolved
            }
            aConflictingValues.push(oConflictingValue);
          })
          oRelationship.conflictingValues = aConflictingValues;
          if ((oElement.couplingType === CouplingConstants.DYNAMIC_COUPLED && oRelationship.conflictingValues.length > 1) || (oElement.couplingType === CouplingConstants.TIGHTLY_COUPLED)) {
            oRelationship.notification = {
              tagValues: []
            }
          }
        }
      }
      if (oElement.couplingType == CouplingConstants.DYNAMIC_COUPLED) oElement.canDelete = false;
    }
  };

  var _preProcessAndSetReferencedAssets = function (oReferencedAssets, bIsMergeOldReferencedAssetList = false) {
    var oScreenProps = ContentScreenProps.screen;
    var aReferencedAssetsList = bIsMergeOldReferencedAssetList ? oScreenProps.getReferencedAssetList() : [];
    CS.forEach(oReferencedAssets, function (oAsset, sId) {
      oAsset.id = sId;
      aReferencedAssetsList.push(oAsset);
    });
    oScreenProps.setReferencedAssetList(aReferencedAssetsList);
  };

  var _addRootNodeIdToReferencedTaxonomies = function (oTaxonomy) {
    if (oTaxonomy.parent.id != -1) {
      return _addRootNodeIdToReferencedTaxonomies(oTaxonomy.parent)
    } else return {id: oTaxonomy.id, code: oTaxonomy.code, label: oTaxonomy.label};
  };

  var _modifyHeaderPermissions = function (oKlassInstance, oReferencedPermissions) {
    if (oKlassInstance.baseType == BaseTypesDictionary["supplierBaseType"]) {
      oReferencedPermissions.headerPermission.canAddClasses = false;
      oReferencedPermissions.headerPermission.canEditPrimaryType = false;
    }
    return oReferencedPermissions;
  };

  var _modifyReferencedElement = function (oKlassInstance, oReferencedElements) {
    let isContentAvailableInSelectedDL = ContentUtils.isContentAvailableInSelectedDataLanguage(oKlassInstance);
    let bIsCurrentUserReadOnly = ContentUtils.getIsCurrentUserReadOnly();
    CS.forEach(oReferencedElements, function (oReferencedElement) {
      if (!isContentAvailableInSelectedDL) {
        oReferencedElement.isDisabled = true ;
      } else if(bIsCurrentUserReadOnly) {
        if(!oReferencedElement.attributeVariantContext) {
          oReferencedElement.isDisabled = true;
        }
      }
    });
    return oReferencedElements;
  };

  let _updateTemplateAndSectionsData = function (aEntityTabItemList, sContextId) {
    let iTabCount = 0;
    let oEntityTabItem = CS.find(aEntityTabItemList, {id: sContextId});
    switch (sContextId) {
      case ContentScreenConstants.tabItems.TAB_TASKS :
        iTabCount = ContentScreenProps.taskProps.getTasksCount();
        break;
    }


    if (oEntityTabItem) {
      oEntityTabItem.count = iTabCount;
      oEntityTabItem.propertySequenceList = oEntityTabItem.propertySequenceList;
    }
  };

  let _updateTemplateAndSectionsByTab = function (oReferencedTemplate, oConfigDetails, oKlassInstance) {
    let bIsCurrentUserReadOnly = ContentUtils.getIsCurrentUserReadOnly();
    if (CS.isEmpty(oConfigDetails.referencedTasks) || bIsCurrentUserReadOnly) {
      CS.remove(oReferencedTemplate.tabs, {id: ContentScreenConstants.tabItems.TAB_TASKS});
    }
    if (oKlassInstance.baseType !== BaseTypesDictionary["assetBaseType"]) {
      CS.remove(oReferencedTemplate.tabs, {id: ContentScreenConstants.tabItems.TAB_RENDITION});
    }
    _updateTemplateAndSectionsData(oReferencedTemplate.tabs, ContentScreenConstants.tabItems.TAB_TASKS);
  };

  let _setCurrentTemplate = function (oKlassInstance, oConfigDetails) {
    let oScreenProps = ContentScreenProps.screen;

    let oReferencedTemplate = oConfigDetails.referencedTemplate || {};

    //Adding Source Tab For Golden Record
    _updateTemplateAndSectionsByTab(oReferencedTemplate, oConfigDetails, oKlassInstance);
    oScreenProps.setReferencedTemplate(oReferencedTemplate);
    oScreenProps.setSelectedTemplate(oReferencedTemplate);

    if (!CS.isEmpty(oReferencedTemplate)) {
      let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
      if (!oActiveEntitySelectedTabIdMap[oKlassInstance.id]) {
        oActiveEntitySelectedTabIdMap[oKlassInstance.id] = {};
      }
      oActiveEntitySelectedTabIdMap[oKlassInstance.id]["selectedTemplate"] = oReferencedTemplate;
    }

    var aReferencedTemplateList = oConfigDetails.referencedTemplates;
    if (oReferencedTemplate.id !== "all") {
      var oAll = CS.find(aReferencedTemplateList, {id: "all"});
      if (CS.isEmpty(oAll)) {
        oAll = {
          id: "all",
          label: getTranslation().ALL,
          type: "customTemplate"
        }
      }
      aReferencedTemplateList.unshift(oAll);
    }


    oScreenProps.setReferencedTemplates(aReferencedTemplateList);
  };

  var _setActivePropertyForTask = function (oKlassInstance) {
    let bShowAnnotations = ContentScreenProps.taskProps.getShowAnnotations();
    let bIsTaskDialogActive = ContentScreenProps.taskProps.getIsTaskDialogActive();

    /** Set active property only when task property dialog or annotation dialog is opened **/
    if (!(bShowAnnotations || bIsTaskDialogActive)) {
      return;
    }

    let oTaskData = ContentScreenProps.taskProps.getTaskData();
    var oActiveProperty = oTaskData.activeProperty;

    var oProperty = {};
    if (oActiveProperty.type == "attribute") {
      var oAttribute = CS.find(oKlassInstance.attributes, {attributeId: oActiveProperty.id});
      oProperty.id = oAttribute.attributeId;
      oProperty.instanceId = oAttribute.id;
    } else if (oActiveProperty.type == "role") {
      var oRole = CS.find(oKlassInstance.roles, {roleId: oActiveProperty.id});
      oProperty.id = oRole.roleId;
      oProperty.instanceId = oRole.id;
    } else if (oActiveProperty.type == "tag") {
      var oTag = CS.find(oKlassInstance.tags, {tagId: oActiveProperty.id});
      oProperty.id = oTag.tagId;
      oProperty.instanceId = oTag.id;
    }
    if (!CS.isEmpty(oActiveProperty)) {
      oProperty.type = oActiveProperty.type;
    }
    ContentScreenProps.taskProps.setTaskData(CS.combine(oTaskData, {activeProperty: oProperty}));
  };

  var _prepareAllowedTaxonomyByIdData = function (aAllowedIds) {
    var oRes = {};
    var oScreenProps = ContentScreenProps.screen;
    var aSelectedTaxonomyIds = oScreenProps.getActiveTaxonomyIds();
    var oReferencedTaxonomy = oScreenProps.getReferencedTaxonomies();

    CS.forEach(aSelectedTaxonomyIds, function (sTaxId) {
      var oRTaxonomy = oReferencedTaxonomy[sTaxId];

      if (!CS.isEmpty(oRTaxonomy)) {
        oRes[sTaxId] = CS.filter(oRTaxonomy.children, function (oChild) {
          return CS.includes(aAllowedIds, oChild.id);
        });
      }
    });

    return oRes;
  };

  var _handleClassVisibilityAfterSuccessResponse = function (bIsSaveResponse, oEntity) {
    if (!bIsSaveResponse && !oEntity.variantInstanceId) {
      var oSectionViewProps = ContentScreenProps.contentSectionViewProps;
      /** when save response, do not alter visible class selection*/
      var sVisibleClassId = ContentUtils.getNatureKlassIdFromKlassIds(oEntity.types);
      if (!sVisibleClassId) {
        ExceptionLogger.error("'sVisibleClassId' is undefined in: _handleClassVisibilityAfterSuccessResponse");
        sVisibleClassId = "";
      }
      oSectionViewProps.setVisibleClassId(sVisibleClassId);
      oSectionViewProps.setSectionVisualProps({});
    }
  };

  var _checkAndSendCallForQuickList = function (bIsSaveResponse, oEntity, oFilterContext) {
    var oAddEntityInRelationshipScreenData = ContentScreenProps.screen.getAddEntityInRelationshipScreenData();
    var oActiveEntityRelData = oAddEntityInRelationshipScreenData[oEntity.id];
    if (!bIsSaveResponse && oActiveEntityRelData && oActiveEntityRelData.status) {
      var oSectionSelectionStatus = ContentScreenProps.screen.getSetSectionSelectionStatus();
      oSectionSelectionStatus.selectedRelationship = {id: oActiveEntityRelData.id};
      oSectionSelectionStatus['relationshipContainerSelectionStatus'] = true;
      AvailableEntityStore.fetchAvailableEntities({filterContext: oFilterContext});
    }
  };

  var _setAssetEventScheduleDataWithDummyObject = function (oEntity) {
    //adding assetBaseType for assetInstance creation issue, there we get eventSchedule as null
    if (CS.isEmpty(oEntity.eventSchedule)) {
      oEntity.eventSchedule = {
        "startTime": null,
        "endTime": null,
      };
    }
  };

  let _prepareEntitiesDataForTaskRoles = function () {
    let aEntityList = [ConfigDataEntitiesDictionary.USERS, ConfigDataEntitiesDictionary.ROLES];
    let aGroups = [];
    CS.forEach(aEntityList, entity => {
      let [id, label, resPath, paginationData] = ["", "", [], {}];
      switch (entity) {
        case ConfigDataEntitiesDictionary.USERS:
          id = "users";
          label = getTranslation().USERS;
          resPath = ["success", entity];
          paginationData = {
            sortBy: 'firstName',
            sortOrder: 'asc',
            searchColumn: 'firstName',
          };
          break;
        case ConfigDataEntitiesDictionary.ROLES:
          id = "roles";
          label = getTranslation().ROLES
          resPath = ["success", entity];
          break;
      }
      var oRequestData = {
        id: id,
        label: label,
        requestInfo: {
          requestType: "configData",
          responsePath: resPath,
          entities: entity,
          requestURL:"config/rolesconfigdata",
          paginationData: paginationData,
          customRequestModel: {},
          customRequestInfoModel: {
            organizationId : SessionProps.getSessionOrganizationId()
          },
        }
      }
      aGroups.push(oRequestData);
    });
    return aGroups;
  };

  var _prepareDataForTask = function (oResponse) {
    let oConfigDetails = oResponse.configDetails;
    let oCurrentUser = GlobalStore.getCurrentUser();

    return {
      configDetails: {
        referencedRoles: oConfigDetails.referencedRoles,
        referencedTasks: oConfigDetails.referencedTasks,
        referencedPermissions: oConfigDetails.referencedPermissions,
        referencedTags: oConfigDetails.referencedTags
      },
      referencedVariants: oResponse.referencedVariants,
      referencedElements: oResponse.referencedElements,
      referencedElementsMapping: oResponse.referencedElementsMapping,
      propertyIdsHavingTask: oResponse.propertyIdsHavingTask,
      selectedModuleId: ContentUtils.getSelectedModuleId(),
      taskInstanceList: oResponse.taskInstanceList || [],
      activeContent: oResponse.klassInstance,
      currentUser: oCurrentUser,
      taskRolesData: _prepareEntitiesDataForTaskRoles(),
      usersList: ContentUtils.getUserList(),
      rolesList: ContentUtils.getRoleList()
    }
  };

  let _setUpTimeLineData = function (oResponse, bIsSaveResponse) {
    TimelineStore.setVersions(oResponse.versions, oResponse.from);
  };

  let _setUpContextOpenData = function (oArticleFromServer, oResponse) {
    let oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    let oVariantSectionProps = ContentScreenProps.variantSectionViewProps;
    oArticleFromServer.variantInstanceId = oArticleFromServer.id;
    let oContext = oArticleFromServer.context;

    _setLinkedInstancesForActiveContent(oResponse, oArticleFromServer);
    oArticleFromServer.timeRange = oContext.timeRange;
    oVariantSectionProps.setDummyVariant({});
    let oActiveVariantContext = CS.find(oReferencedVariantContexts.embeddedVariantContexts, {id: oContext.contextId});
    if (!oActiveVariantContext) {
      oActiveVariantContext = CS.find(oReferencedVariantContexts.makeupContexts, {id: oContext.contextId}) || {};
    }

    oArticleFromServer.variantsTimeRange = oArticleFromServer.timeRange;
    oVariantSectionProps.setSelectedContext(oActiveVariantContext);
    oActiveVariantContext && ContentScreenProps.screen.setReferencedStatusTags(oActiveVariantContext.referencedStatusTags);
  };

  let _setContentOpenReferencedData = function (oResponse, oArticleFromServer, bIsSaveResponse) {
    let oScreenProps = ContentScreenProps.screen;
    let oConfigDetails = oResponse.configDetails;

    let aReferencedTags = oConfigDetails.referencedTags;
    oScreenProps.setReferencedTags(aReferencedTags);
    let oReferencedVariantContexts = oConfigDetails.referencedVariantContexts || {};
    _processVariantContextData(oReferencedVariantContexts);
    oScreenProps.setReferencedProductVariantContexts(oReferencedVariantContexts.productVariantContexts);
    oScreenProps.setReferencedClasses(oConfigDetails.referencedKlasses);
    ContentUtils.updateNatureNonNatureKlassIds(oConfigDetails.referencedKlasses);
    _handleClassVisibilityAfterSuccessResponse(bIsSaveResponse, oArticleFromServer);
    _preProcessAndSetReferencedAssets(oResponse.referencedAssets);
    var aReferencedTaxonomies = oConfigDetails.referencedTaxonomies;
    CS.forEach(aReferencedTaxonomies, function (oTaxonomy) {
      oTaxonomy.rootNodeInfo = _addRootNodeIdToReferencedTaxonomies(oTaxonomy);
    });

    //===================Modifies Template Tab Sections by Tabs========================
    _setCurrentTemplate(oArticleFromServer, oConfigDetails);
    let oReferencedPermissions = _modifyHeaderPermissions(oArticleFromServer, oConfigDetails.referencedPermissions);

    oScreenProps.setReferencedPermissions(oReferencedPermissions || {});
    oScreenProps.setConflictingValues(oConfigDetails.conflictingValues || {});
    oScreenProps.setReferencedPropertyCollections(oConfigDetails.referencedPropertyCollections || {});
    oScreenProps.setReferencedTaxonomies(oConfigDetails.referencedTaxonomies);
    let oReferencedElements = _modifyReferencedElement(oArticleFromServer, oConfigDetails.referencedElements);
    oScreenProps.setReferencedElements(oReferencedElements || {});
    oScreenProps.setReferencedAttributes(oConfigDetails.referencedAttributes);

    oScreenProps.setReferencedRoles(oConfigDetails.referencedRoles || {});
    oScreenProps.setReferencedRelationships(oConfigDetails.referencedRelationships || {});
    oScreenProps.setReferencedNatureRelationships(oConfigDetails.referencedNatureRelationships || {});
    oScreenProps.setCommonReferencedRelationships(CS.assign({}, oConfigDetails.referencedRelationships, oConfigDetails.referencedNatureRelationships));
    oScreenProps.setReferencedLifeCycleTags(oConfigDetails.referencedLifeCycleStatusTags);
    oScreenProps.setReferencedRelationshipsMapping(oConfigDetails.referencedRelationshipsMapping);
    oScreenProps.setReferencedRelationshipProperties(oConfigDetails.referencedRelationshipProperties);
    let oContextualPriceInfo = {};
    if (CS.isNotEmpty(oConfigDetails.sourcePriceId) && CS.isNotEmpty(oConfigDetails.targetPriceId)) {
      oContextualPriceInfo = {
        sourcePriceId: oConfigDetails.sourcePriceId,
        targetPriceId: oConfigDetails.targetPriceId,
        isContextualPriceInstance: oConfigDetails.isContextualPrice
      }
    }
    oScreenProps.setContextualPriceInfo(oContextualPriceInfo);
    CS.isNotEmpty(oConfigDetails.sourcePriceId) &&
    oScreenProps.setReferencedContents(oResponse.referencedContents);
    //move to task processing later
    ContentScreenProps.taskProps.setReferencedTasks(oConfigDetails.referencedTasks);

    /** Set data for relationships*/
    var oReferenceRelationshipInstanceElements = oResponse.referenceRelationshipInstanceElements;
    oReferenceRelationshipInstanceElements = oReferenceRelationshipInstanceElements || {};
    ContentScreenProps.relationshipView.setReferenceRelationshipInstanceElements(oReferenceRelationshipInstanceElements);
    var oReferenceNatureRelationshipInstanceElements = oResponse.referenceNatureRelationshipInstanceElements;
    oReferenceNatureRelationshipInstanceElements = oReferenceNatureRelationshipInstanceElements || {};
    ContentScreenProps.relationshipView.setReferenceNatureRelationshipInstanceElements(oReferenceNatureRelationshipInstanceElements);
    ContentScreenProps.relationshipView.setReferenceElementInstances(oResponse.referencedElementInstances);
    _setActivePropertyForTask(oArticleFromServer);
    oScreenProps.setReferencedCollections(oResponse.referencedCollections);

    let oReferencedLanguages = oConfigDetails.referencedLanguages;
    oScreenProps.setReferencedLanguages(oReferencedLanguages);

    let sDataLanguageCode = null;
    let sSelectedTab = ContentUtils.getSelectedTabId();
    let oTimelineProps = ContentScreenProps.timelineProps;
    let bIsComparisonMode = oTimelineProps.getIsComparisonMode();
    if (sSelectedTab === TemplateTabsDictionary.TIMELINE_TAB && bIsComparisonMode) {
      sDataLanguageCode = oTimelineProps.getSelectedLanguageForComparison();
    } else {
      sDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    }

    let oSelectedDataLanguage = CS.find(oReferencedLanguages, {code: sDataLanguageCode});
    if (!CS.isEmpty(oSelectedDataLanguage)) {
      MomentUtils.setCurrentDateFormat(oSelectedDataLanguage);
      NumberUtils.setCurrentNumberFormat(oSelectedDataLanguage);
    }
  };

  let _setThumbViewRelatedProps = function (oResponse) {
    var oScreenProps = ContentScreenProps.screen;
    var ArticleProps = ContentScreenProps.articleViewProps;

    oScreenProps.setIsEditMode(true);
    ContentScreenProps.innerFilterProps.setFilterInfo(oResponse.filterInfo);
    ContentUtils.setSelectedContentIds([]);
    !!oResponse.defaultTypes && oAppData.setDefaultTypes(oResponse.defaultTypes); // eslint-disable-line
    var aList = oScreenProps.getReferencedClasses();
    aList = CS.filter(aList, {isNature: true});
    ArticleProps.setAllowedTypes(CS.map(aList, "id"));
    ArticleProps.setAllowedNatureTypes(aList);
  };

  let _setUpBaseTypeSpecificProps = function (oArticleFromServer, oResponse) {
    let sArticleBaseType = oArticleFromServer.baseType;
    if (CS.includes([BaseTypesDictionary["assetBaseType"]], sArticleBaseType)) {
      _setAssetEventScheduleDataWithDummyObject(oArticleFromServer);
    }
    if (oResponse.klassInstance.baseType !== BaseTypeDictionary.assetBaseType) {
      let oCallback = {
        functionToExecute: function () {
          _setSelectedImageId();
        }
      };
      _getAssetList(oCallback); //network call for assetList
    } else {
      _setSelectedImageId();
    }
  };

  let _setUpContentImageCoverflowData = function (oArticleFromServer) {
    _updateImageCoverflowRelatedData(oArticleFromServer);
    ImageCoverflowStore.updateImageCoverActiveIndexData();
    /** by default setting ImageCoverFlow props because on overview tab didn't getting Imagecoverflow attribute.
     * because of this not able to upload image */
    let oImageAttribute = CS.find(oArticleFromServer.attributes, {baseType: BaseTypesDictionary['imageAttributeInstanceBaseType']});
    !oImageAttribute && ImageCoverflowUtils.setMaxCoverflowItemAllowed(1);
  };

  let _setUpCommonReferencedRelationshipElements = function (oArticleFromServer) {
    let oRelationshipProps = ContentScreenProps.relationshipView;
    let oReferencedCommonRelationshipInstanceElements = CS.assign(oRelationshipProps.getReferenceNatureRelationshipInstanceElements(), oRelationshipProps.getReferenceRelationshipInstanceElements());
    oRelationshipProps.setReferencedCommonRelationshipInstanceElements(oReferencedCommonRelationshipInstanceElements)
  };

  let _setUpContentOpenData = function (oResponse, oArticleFromServer, bIsSaveResponse, oFilterContext, bIsSaveAndPublish) {
    var oScreenProps = ContentScreenProps.screen;
    //====================== Misc Details ======================
    _setThumbViewRelatedProps(oResponse);
    _setContentOpenReferencedData(oResponse, oArticleFromServer, bIsSaveResponse);
    oScreenProps.setVariantsCount(oResponse.variantsCount);
    oScreenProps.setBranchOfLabel(oResponse.branchOfLabel);
    oScreenProps.setVariantOfLabel(oResponse.variantOfLabel);
    ContentScreenProps.taskProps.setPropertyIdsHavingTask(oResponse.propertyIdsHavingTask);
    ContentUtils.setRuleViolationProps(oArticleFromServer.ruleViolation);
    ///======================Context Open======================
    if (!CS.isEmpty(oArticleFromServer.context)) {
      _setUpContextOpenData(oArticleFromServer, oResponse);
    }
    //======================Content Open======================
    ContentUtils.setActiveEntity(oArticleFromServer);
    _setUpContentImageCoverflowData(oArticleFromServer);
    !!oResponse.referencedAssets && ContentUtils.makeAssetInstancesForEntity(oArticleFromServer, oResponse.referencedAssets);
    _emptyContentVariantVersionProps();
    ContentUtils.setVariantVersionProps(oArticleFromServer);
    /** To handle StatusTag & LifeCycleStatus Tag added after the content has been created **/
    _updateTagDataWhichIsNotInSections(oArticleFromServer);
    ContentUtils.preProcessAttributeAfterGet(oArticleFromServer);
    //--------------------Setting Current Nature Type----------//
    let oTypeClass = oResponse.configDetails.referencedKlasses[ContentUtils.getContentNatureTypeId(oArticleFromServer)];
    !CS.isEmpty(oTypeClass) && ContentUtils.setActiveContentClass(oTypeClass);

    //--------------------Setting Current Taxonomy Details--------//
    NewMultiClassificationStore.setActiveClassAndTaxonomyIds(oArticleFromServer);

    //--------------------Setting Relationship and (Feature)References Details--------//
    oArticleFromServer.contentRelationships = oResponse.contentRelationships;
    oArticleFromServer.natureRelationships = oResponse.natureRelationships;
    ContentUtils.setRelationshipElements(oArticleFromServer);
    ContentUtils.setNatureAndVariantRelationshipIdsIntoProps();
    _setUpCommonReferencedRelationshipElements(oArticleFromServer);

    //-----------------Golden Record Info-----------------------------//
    oScreenProps.setIsActiveClassGoldenRecord(CS.includes(oArticleFromServer.types, MarkerClassTypeDictionary.GOLDEN_RECORD));

    //-----------------Sections Info-----------------------------//
    _prepareActiveSectionsNew(bIsSaveResponse);
    ContentUtils.addSectionDataFromClassIntoEntity(oArticleFromServer, 'content');

    //------------------Rules and Permission------------------------//
    _updateMandatoryElementsStatus(oArticleFromServer);
    oResponse.globalPermission = {};
    oArticleFromServer.globalPermission = CS.get(oResponse, 'configDetails.referencedPermissions.globalPermission') || oResponse.globalPermission;
    oArticleFromServer.globalPermission.canCreateNature = oResponse.configDetails.canCreateNature;

    //------------------------------------------------------//
    _setUpBaseTypeSpecificProps(oArticleFromServer, oResponse);

    //------------------ Calls--------------------
    /** If relationshipAddView is open then send another call for fetchAvailableEntities*/
    _checkAndSendCallForQuickList(bIsSaveResponse, oArticleFromServer, oFilterContext);

    /** fetch UOM table data if UOM Table is expanded **/
    _fetchUOMDataForExpandedTables(bIsSaveResponse, bIsSaveAndPublish);
  };

  let _setUpTaskData = function (oResponse) {
    ContentScreenProps.taskProps.setTasksCount(oResponse.tasksCount || 0);
    let oTaskData = _prepareDataForTask(oResponse);
    ContentScreenProps.taskProps.setTaskData(oTaskData);
  };

  var _processGetArticleResponse = function (oResponse, bForComparisionView, sContextId, bIsSaveResponse, oFilterContext, bIsSaveAndPublish) {
    var oArticleFromServer = oResponse.klassInstance;

    /** Set all (klassDetails as well as relationships) referenced data */
    oResponse.attributeVariantsStats && _processAttributeVariantsStats(oResponse.attributeVariantsStats, oResponse.configDetails.referencedAttributes,
        oResponse.configDetails.referencedElements);

    _setUpTimeLineData(oResponse, bIsSaveResponse); // 1
    _setUpTaskData(oResponse);
    _setUpContentOpenData(oResponse,oArticleFromServer,bIsSaveResponse, oFilterContext, bIsSaveAndPublish);
    oResponse.languageInstances && _processLanguageInstances(oResponse);

    //================================================================
    var sId = oArticleFromServer.variantInstanceId || oArticleFromServer.id;
    ContentUtils.updateBreadCrumbInfo(sId, oArticleFromServer);
    ContentUtils.resetToUpdateAllSCU();
  };

  let _setSelectedImageId = function () {
    let oScreenProps = ContentScreenProps.screen;
    let oContent = ContentUtils.getActiveEntity();
    let aList = oContent.referencedAssets || [];
    let sSelectedImageId = oScreenProps.getSelectedImageId();
    let oImage = {};
    let sImageId = "";
    if (oContent.baseType == BaseTypeDictionary.assetBaseType) {
      aList = oContent.attributes;
      if (!CS.isEmpty(sSelectedImageId) && CS.find(aList, {id: sSelectedImageId})) { //Selected imageId will not change on every save.
        return;
      }
      oImage = oContent.assetInformation;
      if (!CS.isEmpty(oImage)) {
        sImageId = oImage.thumbKey;
      }
    } else {
      let assetRelationshipEntities = oScreenProps.getReferencedAssetList();
      let allAssetList = oScreenProps.getAssetList();
      aList = CS.concat(aList, assetRelationshipEntities);
      aList = CS.concat(aList, allAssetList);
      if (!CS.isEmpty(sSelectedImageId) && CS.find(aList, {assetInstanceId: sSelectedImageId})) { //Selected imageId will not change on every save.
        return;
      }
      oImage = CS.find(aList, {assetInstanceId: oContent.defaultAssetInstanceId});
      if (!CS.isEmpty(oImage)) {
        sImageId = oImage.assetInstanceId;
      }
    }
    _setContentSelectedImageId(sImageId);
  };

  var _fetchUOMDataForExpandedTables = function (bIsSaveResponse, bIsSaveAndPublish) {

    let oScreenProps = ContentScreenProps.screen;
    let oActiveContent = ContentUtils.getActiveContent();
    let sActiveContentId = oActiveContent.id;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[sActiveContentId];
    let bLanguageComparisonModeOn = oActiveContentMap.languageComparisonModeOn;
    /** Don't fetch table view data for timeline tab, rendition tab and when Language comparison mode is on **/
    let sSelectedTabId = ContentUtils.getSelectedTabId();
    if (sSelectedTabId === TemplateTabsDictionary.TIMELINE_TAB || bLanguageComparisonModeOn) {
      ContentScreenProps.tableViewProps.reset();
      return;
    }

    let oReferencedVariantContexts = oScreenProps.getReferencedVariantContexts();
    let oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    let oSectionProps = ContentScreenProps.contentSectionViewProps;
    let oSectionVisualProps = oSectionProps.getSectionVisualProps();
    let oTableViewProps = ContentScreenProps.tableViewProps;

    CS.forEach(oEmbeddedVariantContexts, (oContext) => {
      let sContextId = oContext.id;
      let oFilterContext = {
        filterType: oFilterPropType.MODULE,
        screenContext: sContextId
      };
      var oContextProps = oTableViewProps.getTableViewPropsByContext(sContextId, sActiveContentId);
      var oAllContextProps = oTableViewProps.getAllTableViewPropsByContext(sContextId, sActiveContentId);
      if (oContext.type !== "attributeVariantContext" &&
          oSectionVisualProps[sContextId] && oSectionVisualProps[sContextId].isExpanded) {
        if (bIsSaveResponse && !CS.isEmpty(oContextProps)) {
          let oCallbackData = {
            filterContext: oFilterContext,
            isSaveAndPublish: bIsSaveAndPublish
          }
          VariantStore.fetchVariantData(sContextId, null, null, null, oCallbackData);
        }
        else {
          !oAllContextProps && oTableViewProps.createNewTableViewPropsByContext(sContextId, oActiveContent.id);
          VariantStore.setCurrentDateForFilter(oContext);
          VariantStore.fetchVariantData(sContextId, null, null, null, {filterContext: oFilterContext});
        }
      }
    });
  };

  var _setLinkedInstancesForActiveContent = function (oResponse, oArticleFromServer) {
    var oReferencedInstances = oResponse.referencedInstances;
    var oLinkedInstances = {};
    CS.forEach(oReferencedInstances, function (oLinkedInstance) {
      if (oLinkedInstance) {
        var sBaseType = oLinkedInstance.baseType || "";
        var aSplittedBaseTypeData = sBaseType.split('.');
        var sContextEntity = aSplittedBaseTypeData.pop();
        if (CS.isEmpty(oLinkedInstances[sContextEntity])) {
          oLinkedInstances[sContextEntity] = [];
        }
        oLinkedInstances[sContextEntity].push(oLinkedInstance);
      }
    });
    oArticleFromServer.linkedInstances = oLinkedInstances;
  };

  var _processAttributeVariantsStats = function (oAttributeVariantsStats, oReferencedAttributes, oReferencedElements) {
    let oScreenProps = ContentScreenProps.screen;

    CS.forEach(oAttributeVariantsStats, function (oVariantStats, sAttributeId) {
      let oReferencedAttribute = oReferencedAttributes[sAttributeId];
      let oReferencedElement = oReferencedElements[sAttributeId];
      let sOriginalValue = "";
      let sPrecision = "";
      let sMinValue = "";
      let sMaxValue = "";
      if(CS.isNumberExponential(oVariantStats.min)) {
        oVariantStats.min = (oVariantStats.min).toLocaleString('fullwide', {useGrouping:false});
      }
      if(CS.isNumberExponential(oVariantStats.max)) {
        oVariantStats.max = (oVariantStats.max).toLocaleString('fullwide', {useGrouping:false})
      }
      if (ContentUtils.isAttributeTypeMeasurement(oReferencedAttribute.type)) {

        //for Min value
        sOriginalValue = oVariantStats.min;
        let sCurrentUnit = ContentUtils.getBaseUnitFromType(oReferencedAttribute.type);
        let sExpectedUnit = oReferencedAttribute.defaultUnit;
        sPrecision = CS.isNumber(oReferencedElement.precision) ? oReferencedElement.precision : oReferencedAttribute.precision;
        sMinValue = ContentUtils.getMeasurementAttributeValueToShow(sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision);
        sMinValue = NumberUtils.getValueToShowAccordingToNumberFormat(CS.toString(sMinValue), sPrecision);

        //for Max value
        sOriginalValue = oVariantStats.max;
        sCurrentUnit = ContentUtils.getBaseUnitFromType(oReferencedAttribute.type);
        sExpectedUnit = oReferencedAttribute.defaultUnit;
        sPrecision = CS.isNumber(oReferencedElement.precision) ? oReferencedElement.precision : oReferencedAttribute.precision;
        sMaxValue = ContentUtils.getMeasurementAttributeValueToShow(sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision);
        sMaxValue = NumberUtils.getValueToShowAccordingToNumberFormat(CS.toString(sMaxValue), sPrecision) + " " + sExpectedUnit;
      } else if (ContentUtils.isAttributeTypeNumber(oReferencedAttribute.type)) {

        //for Min value
        sOriginalValue = oVariantStats.min;
        sPrecision = CS.isNumber(oReferencedElement.precision) ? oReferencedElement.precision : oReferencedAttribute.precision;
        // sMinValue = ContentUtils.getTruncatedValue(sOriginalValue, sPrecision);
        sMinValue = NumberUtils.getValueToShowAccordingToNumberFormat(CS.toString(sOriginalValue), sPrecision);

        //for Max value
        sOriginalValue = oVariantStats.max;
        sPrecision = CS.isNumber(oReferencedElement.precision) ? oReferencedElement.precision : oReferencedAttribute.precision;
        // sMaxValue = ContentUtils.getTruncatedValue(sOriginalValue, sPrecision);
        sMaxValue = NumberUtils.getValueToShowAccordingToNumberFormat(CS.toString(sOriginalValue), sPrecision);
      }
      oVariantStats.min = sMinValue;
      oVariantStats.max = sMaxValue;
    });
    oScreenProps.setAttributeVariantsStats(oAttributeVariantsStats);
  };

  let _processLanguageInstances = function (oResponse) {
    let oScreenProps = ContentScreenProps.screen;
    let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    let oKlassInstance = oResponse.klassInstance;
    let oConfigDetails = oResponse.configDetails;

    /**
     * When content is not available in selected data language.
     * @private
     */
    if (!CS.includes(oKlassInstance.languageCodes, sSelectedDataLanguageCode)) {
      let sActiveContentId = oKlassInstance.id;
      let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
      let oActiveContentMap = oActiveEntitySelectedTabIdMap[sActiveContentId];
      oActiveContentMap.languageComparisonModeOn = false;
      oActiveContentMap.languageCodeToCompare = "";
    } else {
      /**
       * To merge dependent attributes
       * @private
       */
      let aLanguageInstances = oResponse.languageInstances;
      let oActiveContentInstance = CS.find(aLanguageInstances, {language: sSelectedDataLanguageCode});
      let aActiveContentDependentAttribute = oActiveContentInstance.dependentAttributes;
      let aKlassInstanceAttributes = oKlassInstance.attributes;
      let aKlassInstanceTags = oKlassInstance.tags;
      aKlassInstanceAttributes.push.apply(aKlassInstanceAttributes, aActiveContentDependentAttribute);

      let oReferencedAttributes = oConfigDetails.referencedAttributes;
      let aRuleViolationFromServer = oActiveContentInstance.ruleViolation;
      oKlassInstance.ruleViolation = CS.combine(oKlassInstance.ruleViolation, aRuleViolationFromServer);
      let aHelperLanguageInstances = [];
      CS.forEach(aLanguageInstances, function (oInstance) {
        let sHelperLanguageCode = oInstance.language;
        if (sHelperLanguageCode !== sSelectedDataLanguageCode) {
          CS.forEach(aKlassInstanceAttributes, function (oInstanceAttribute) {
            let oMasterAttribute = oReferencedAttributes[oInstanceAttribute.attributeId];
            if (oMasterAttribute.isTranslatable) {
              ContentUtils.addAttributeDataInEntityFromSectionElement(oInstance, {attribute: oMasterAttribute});
            }
          });
          let aLanguagesInfo = SessionProps.getLanguageInfoData();
          let aDataLanguages = aLanguagesInfo.dataLanguages;
          let oSelectedLanguageForComparison = CS.find(aDataLanguages, {code: sHelperLanguageCode});
          ContentUtils.preProcessContentAttributesAfterGetForComparison(oInstance, aKlassInstanceAttributes, aKlassInstanceTags, oConfigDetails,
              oSelectedLanguageForComparison);
          aHelperLanguageInstances.push(oInstance);
        }
      });
      oScreenProps.setHelperLanguageInstances(aHelperLanguageInstances);
    }
  };

  let _resetPropsWhenContentOpenByNavigation = function () {
    let oScreenProps = ContentScreenProps.screen;

    /** Reset clone dialog props when opening content(Dialog should not be opened while navigating by browser navigation)**/
    CloneWizardStore.handleCancelCloningButtonClicked(true);

    /** Reset set default image  dialog props when opening content(Dialog should not be opened while navigating by browser navigation) **/
    oScreenProps.setVariantUploadImageDialogStatus(false);

    /** Reset Edit Task  dialog props when opening content(Dialog should not be opened while navigating by browser navigation) **/
    let oTaskProps = ContentScreenProps.taskProps;
    oTaskProps.setIsTaskDialogActive(false);
  };

  let _updateHelperLanguageComparisonData = function (oKlassInstance) {
    let oScreenProps = ContentScreenProps.screen;
    let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    let aPropertyCollectionSection = CS.filter(oScreenProps.getActiveSections(), {type: ContentScreenConstants.sectionTypes.SECTION_TYPE_PROPERTY_COLLECTION});

    if (CS.isEmpty(aPropertyCollectionSection) ||
        !CS.includes(oKlassInstance.languageCodes, sSelectedDataLanguageCode)) {
      ContentScreenProps.screen.setIsHelperLanguageDisabled(true);
    } else {
      ContentScreenProps.screen.setIsHelperLanguageDisabled(false);
    }
  };

  let successFetchArticleById = function (oCallbackData, oResponse) {

    if (oCallbackData.preFunctionToExecute) {
      oCallbackData.preFunctionToExecute();
    }

    oResponse = oResponse.success;

    let oConfigDetails = oResponse.configDetails;
    let oKlassInstance = oResponse.klassInstance;

    /********************* (B) Breadcrumb related processing *****************/
    let sHelpScreenId;
    let sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT;

    if (!oCallbackData.breadCrumbData) {
      /** In callback data functionToSet & ExecuteFunctionToSet are not present & we need to create breadcrumb item without functionToSet**/
      oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(oKlassInstance.id, oKlassInstance.name, sType, sHelpScreenId, {}, {}, oKlassInstance.baseType);
    }
    else {
      /** In callback data functionToSet & ExecuteFunctionToSet are present & we only need to update remaining information**/
      let oBreadcrumbItem = oCallbackData.breadCrumbData;
      oBreadcrumbItem.id = oKlassInstance.id;
      oBreadcrumbItem.label = oKlassInstance.name;
      oBreadcrumbItem.type = sType;
      oBreadcrumbItem.helpScreenId = sHelpScreenId;
      oBreadcrumbItem.baseType = oKlassInstance.baseType;
    }
    /****************************** (B) END *********************************/

    /**
     * resetting violation flag on opening new content.
     * */
    BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);

    /**************************** (A) Setting and Resetting data *****************************/
    let oScreenProps = ContentScreenProps.screen;
    let oTimelineProps = ContentScreenProps.timelineProps;
    let oRelationshipProps = ContentScreenProps.relationshipView;

    let oFunctionPermission = oConfigDetails.referencedPermissions.functionPermission;
    oScreenProps.setFunctionalPermission(oFunctionPermission);

    oScreenProps.resetSelectedContext();
    oScreenProps.setRelationshipContextData({});
    if (oResponse.klassInstance.baseType === BaseTypeDictionary.assetBaseType) {
      oScreenProps.setAssetList([]);
    }

    let iNumberOfAllowedVersions = oConfigDetails.numberOfVersionsToMaintain;
    oTimelineProps.setNumberOfAllowedVersions(iNumberOfAllowedVersions);

    //Selected Content count should be correct in relationship section
    oRelationshipProps.emptySelectedRelationshipElements();

    if (oCallbackData.isViolationClicked) {
      ContentUtils.setActiveEntitySelectedFilterId(ContentEditFilterItemsDictionary.VIOLATED);
    }

    // When clicked on an instance directly open any tab based on screen context.
    let sContextToChangeTab = _getContextToChangeTab(oCallbackData);

    //Show all possible conflicting sources on an element
    _resetComparisonViewModeData();

    HomeScreenCommunicator.disablePhysicalCatalog(true);

    _resetPropsWhenContentOpenByNavigation();

    /************************************** (A) END *****************************************/

    /* Do not change the below sequence of next three lines as for showing the messages */
    _showRequiredMessages(oResponse, oKlassInstance, oCallbackData);
    _processGetArticleResponse(oResponse, null, null, null, oCallbackData.filterContext);
    _updateHelperLanguageComparisonData(oKlassInstance);

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute(oCallbackData, oResponse);
    }

    //Image get variant from from variant open and close
    if (oCallbackData.fetchArticleWithContextId) {
      oCallbackData.fetchArticleWithContextId();
    }

    /**On click of violation icon, set filter option as "violated"*/
    if (oCallbackData.isViolationClicked) {
      ContentUtils.setActiveEntitySelectedFilterId(ContentEditFilterItemsDictionary.VIOLATED);
    }

    /************************ (C) KPI related processing ********************/
    oScreenProps.resetContentKPIData();
    if (oCallbackData.isCreated) {
      setTimeout(_fetchKpiSummaryData.bind(this, oKlassInstance.id, oKlassInstance.baseType), 4000);
    } else {
      _fetchKpiSummaryData(oKlassInstance.id, oKlassInstance.baseType);
    }

    if (oKlassInstance.baseType === MockDataForEntityBaseTypesDictionary.assetBaseType) {
      let sSelectedTabId = ContentUtils.getSelectedTabId();
      if(sSelectedTabId === ContentScreenConstants.tabItems.TAB_OVERVIEW && oScreenProps.getActiveContentClass().trackDownloads){
        let oAssetDownloadTrackerProps = ContentScreenProps.assetDownloadTrackerProps;
        let oCurrentDate = new Date();
        let oCurrentDateMoment = Moment(oCurrentDate);
        let oDownloadRange = {
          startTime: oCurrentDateMoment.startOf('day').toDate().getTime(),
          endTime: oCurrentDateMoment.endOf('day').toDate().getTime()
        };
        oAssetDownloadTrackerProps.setDownloadRange(oDownloadRange);
        _getAssetDownloadCount();
      }
      if(sSelectedTabId === ContentScreenConstants.tabItems.TAB_OVERVIEW || sSelectedTabId === ContentScreenConstants.tabItems.TAB_RENDITION){
        _getAssetSharedUrl();
      }
    }

    //If tab is other then overview_tab fetch data based on context.
    CS.isNotEmpty(sContextToChangeTab) && _fetchTabSpecificDataBasedOnContext(sContextToChangeTab);

    /******************************* (C) END *******************************/
    _triggerChange();
    return true;
  };

  let _getContextToChangeTab = function (oCallBackData) {
    let sContextToReturn = "";
    let filterContext = oCallBackData.filterContext;
    if (CS.isNotEmpty(filterContext)) {
      switch (filterContext.screenContext) {
        case getTranslation().DUPLICATE_ASSETS :
          sContextToReturn = filterContext.screenContext;
          oCallBackData.filterContext = {};
          break;
      }
    }
    return sContextToReturn;
  };

  let _fetchTabSpecificDataBasedOnContext = function (sContextToChangeTab) {
    switch (sContextToChangeTab) {
      case getTranslation().DUPLICATE_ASSETS :
        _handleEntityTabClicked(ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS);
        break;
    }
  };

  let _getAssetDownloadCount = function () {
    let oActiveContent = ContentUtils.getActiveContent();
    let oAssetDownloadTrackerProps = ContentScreenProps.assetDownloadTrackerProps;
    let oDownloadRange = oAssetDownloadTrackerProps.getDownloadRange();
    let sUrl = ContentScreenRequestMapping.GetAssetDownloadCount;
    let oData = {};
    let oPostData = {
      assetInstanceId: oActiveContent.id,
      timeRange: oDownloadRange
    };
    CS.postRequest(sUrl, oData, oPostData, successGetAssetDownloadCount, failureGetAssetDownloadCount);
  };

  let _getAssetSharedUrl = function () {
    let sActiveContentId = ContentUtils.getActiveContent().id;
    let oPostData = {
      id : sActiveContentId
    };
    CS.postRequest(ContentScreenRequestMapping.GetAssetSharedUrl, {}, oPostData, successGetAssetSharedUrl, failureGetAssetSharedUrl);
  };

  let successGetAssetSharedUrl = function (oResponse) {
    let oResponseData = oResponse.success;
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setAssetSharedUrl(oResponseData.id);
    _triggerChange();
  };

  let failureGetAssetSharedUrl = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureGetAssetSharedUrl', getTranslation());
  };

  let successGetAssetDownloadCount = function (oResponse) {
    let oResponseData = oResponse.success;
    let oAssetDownloadTrackerProps = ContentScreenProps.assetDownloadTrackerProps;
    oAssetDownloadTrackerProps.setTotalDownloadCount(oResponseData.totalDownloadCount);
    oAssetDownloadTrackerProps.setDownloadCountWithinRange(oResponseData.downloadCountWithinRange);
    _triggerChange();
  };

  let failureGetAssetDownloadCount = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureGetAssetDownloadCount', getTranslation());
  };

  let _showRequiredMessages = function (oResponse, oKlassInstance) {

    /** Required when any content is getting opened for the first time **/
    let oActiveContent = ContentUtils.getActiveContent();
    let bIsContentNotPresentInSelectedDL = !ContentUtils.isContentAvailableInSelectedDataLanguage(oKlassInstance);
    if (bIsContentNotPresentInSelectedDL && (CS.isEmpty(oActiveContent) || oActiveContent.id !== oKlassInstance.id)) {
      ContentUtils.showMessage(getTranslation().CONTENT_ABSENT_IN_DATA_LANG);
    }
  };

  let _getAssetList = function (oCallbackData) {
    _handleImageGalleryDialogViewVisibilityStatusChanged(oCallbackData);
  };

  var failureFetchArticleById = function (oCallBack, oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchArticleById', getTranslation());
    _checkForExtraNecessaryServerCalls(oResponse);
    return false;
  };

  let _fetchKpiSummaryData = function (sId, sType) {
    let sUrl = ContentScreenRequestMapping.GetKpiSummaryData;
    let oData = {};
    let oPostData = {
      id: sId,
      type: sType
    };
    CS.postRequest(sUrl, oData, oPostData, successFetchKpiSummaryData, failureFetchKpiSummaryData, true);
  };

  let successFetchKpiSummaryData = function (oResponse) {
    let oResponseData = oResponse.success;
    let oScreenProps = ContentScreenProps.screen;
    let oKpiMap = oResponseData.referencedKpi || {};
    let oStatisticsMap = oResponseData.statistics || {};
    let aKpiListForSummary = [];
    let oProcessedContentKPIDataMap = {};
    let oLastModifiedOn = ContentUtils.getDateAttributeInDateTimeFormat(oResponseData.lastModified);
    CS.forEach(oStatisticsMap, function (oStatistics, key) {
      let oReferencedKpi = oKpiMap[key];
      let sKPIId = oReferencedKpi.id;
      oStatistics.accuracy = Math.round(oStatistics.accuracy);
      oStatistics.completeness = Math.round(oStatistics.completeness);
      oStatistics.conformity = Math.round(oStatistics.conformity);
      oStatistics.uniqueness = Math.round(oStatistics.uniqueness);
      aKpiListForSummary.push({
        id: sKPIId,
        label: CS.getLabelOrCode(oReferencedKpi),
        statistics: oStatistics,
        lastModifiedOn: oLastModifiedOn
      });
      oProcessedContentKPIDataMap[sKPIId] = _prepareContentKPITileData(oStatistics, oReferencedKpi);
    });
    let oKPISummaryData = aKpiListForSummary[0] || {};
    let sSelectedKPIId = oKPISummaryData.id || "";
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContent = _getActiveEntity();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[oActiveContent.id] || {};
    CS.isEmpty(oActiveContentMap["selectedKPIId"]) && (oActiveContentMap["selectedKPIId"] = sSelectedKPIId);

    ContentScreenProps.screen.setKpiListForSummary(aKpiListForSummary);
    ContentScreenProps.screen.setProcessedContentKPIDataMap(oProcessedContentKPIDataMap);

    _triggerChange();
  };

  let failureFetchKpiSummaryData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchKpiSummaryData', getTranslation());
  };

  let _prepareContentKPITileData = function (oKPI, oReferencedKPI) {
    let oContentKPITileData = {};
    oContentKPITileData.id = oKPI.kpiId;
    oContentKPITileData.label = oReferencedKPI.label;
    oContentKPITileData.chartType = DashboardChartTypeDictionary.GROUPED_BAR;
    oContentKPITileData.chartOptions = new DashboardChartConfiguration[DashboardChartTypeDictionary.GROUPED_BAR]();
    oContentKPITileData.chartOptions.scales.xAxes[0].scaleLabel = {
      display: false
    };
    let aKPIDataSets = [];
    aKPIDataSets.push({
      label: getTranslation().ACCURACY,
      data: [oKPI.accuracy],
      backgroundColor: BackgroundColors.accuracy
    });
    aKPIDataSets.push({
      label: getTranslation().CONFORMITY,
      data: [oKPI.conformity],
      backgroundColor: BackgroundColors.conformity
    });
    aKPIDataSets.push({
      label: getTranslation().COMPLETENESS,
      data: [oKPI.completeness],
      backgroundColor: BackgroundColors.completeness
    });
    aKPIDataSets.push({
      label: getTranslation().UNIQUENESS,
      data: [oKPI.uniqueness],
      backgroundColor: BackgroundColors.uniqueness
    });

    oContentKPITileData.chartData = {
      labels: [],
      datasets: aKPIDataSets
    };

    oContentKPITileData.breadCrumbs = [{
      id: -1,
      label: CS.getLabelOrCode(oReferencedKPI),
      levelId: 0,
    }];

    oContentKPITileData.hideButtons = true;

    return oContentKPITileData;
  };

  let successAssetLinkSharingCallback = function (sContext, sButtonContext, oResponse) {
    if (!CS.isEmpty(oResponse.success)) {
      let oBulkAssetLinkSharingProps = ContentScreenProps.bulkAssetLinkSharingProps;
      let oResponseData = oResponse.success;
      let aMasterAssetId = oResponseData.masterAssetIdsList;
      oBulkAssetLinkSharingProps.setMasterAssetID(aMasterAssetId);
      let aAssetList = BulkAssetDownloadList();

      if (CS.isEmpty(oResponseData.technicalVariantTypeIdsList)) {
        aAssetList.pop();
      }
      let aMasterAssetTypeList = getMasterAssetTypeIdsList(oResponseData.masterAssetTypeIdsList);
      oBulkAssetLinkSharingProps.setMasterAssetTypeIDData(aMasterAssetTypeList);
      oBulkAssetLinkSharingProps.setTechnicalVariantTypeIdsList(oResponseData.technicalVariantTypeIdsList);
      let iTotalNestedCount = aAssetList.length + oResponseData.technicalVariantTypeIdsList.length;
      oBulkAssetLinkSharingProps.setTotalNestedItems(iTotalNestedCount);
      let aGridViewData = BulkAssetLinkSharingStore.preProcessAssetDataForGridView(aAssetList);
      oBulkAssetLinkSharingProps.setGridViewData(aGridViewData);
      oBulkAssetLinkSharingProps.setAssetShareContext(sButtonContext);
      oBulkAssetLinkSharingProps.setShowAssetLinkSharingDialog(true);
      _triggerChange();
    }
  };

  let successBulkDownloadAssetsCallback = function (sSelectedContentId, oResponse) {
    trackMe("successBulkDownloadAssetsCallback");
    if (!CS.isEmpty(oResponse.success)) {
      let oBulkDownloadAssetProps = ContentScreenProps.bulkDownloadAssetProps;
      let oResponseData = oResponse.success;
      let aDownloadInfo = oResponseData.downloadInformation;
      let bShouldDownloadAssetWithOriginalFilename = oResponseData.shouldDownloadAssetWithOriginalFilename;
      oBulkDownloadAssetProps.setShowDownloadDialog(true);
      oBulkDownloadAssetProps.setDownloadInfo(aDownloadInfo);
      oBulkDownloadAssetProps.setShouldDownloadAssetWithOriginalFilename(bShouldDownloadAssetWithOriginalFilename);
      oBulkDownloadAssetProps.setSelectedContentId(sSelectedContentId);
      BulkAssetDownloadStore.preProcessDownloadDialogDataForView(bShouldDownloadAssetWithOriginalFilename);
      _triggerChange();
    }
  };

  let getMasterAssetTypeIdsList = function (aMasterAssetTypeIdsList) {
    return _getMasterAssetTypeIdsList(aMasterAssetTypeIdsList);
  };

  var failureBulkDownloadAssetsCallback = function (oResponse) {
    trackMe("failureBulkDownloadAssetsCallback");
    ContentUtils.failureCallback(oResponse, 'failureBulkDownloadAssetsCallback', getTranslation());
    _checkForExtraNecessaryServerCalls(oResponse);
  };

  let _handleAssetBulkDownloadButtonClicked = function (sContext, sButtonContext) {
    let aContentIdForBulkDownload = [];
    let oActiveContent = ContentUtils.getActiveContent();
    if (CS.isEmpty(oActiveContent)) {
      let aSelectedContents = ContentUtils.getSelectedContents();
      let aSelectedContentIds = ContentUtils.getSelectedContentIds();

      CS.forEach(aSelectedContentIds, function (sId) {
        let oSelectedContent = CS.find(aSelectedContents, {id: sId});
        if (oSelectedContent.baseType == BaseTypesDictionary["assetBaseType"]) {
          aContentIdForBulkDownload.push(oSelectedContent.id);
        }
      });
    } else {
      aContentIdForBulkDownload.push(oActiveContent.id);
    }
    if (sButtonContext === AssetDownloadViewDictionary.BULK_LINK_SHARING_VIEW && oActiveContent.isEmbedded) {
      let oPostDataForShare = {"masterAssetIds": aContentIdForBulkDownload, "masterAssetShare": true};
      CS.postRequest(getRequestMapping().ShareDownload, {}, oPostDataForShare, successBulkAssetsShareCallback, failureBulkAssetsShareCallback);
      BulkAssetLinkSharingStore.setUpAssetLinkSharingGridView();
    } else if (sButtonContext === AssetDownloadViewDictionary.BULK_LINK_SHARING_VIEW
        && ContentUtils.getNatureTypeFromContent(oActiveContent) === NatureTypeDictionary.TECHNICAL_IMAGE) {
      BulkAssetLinkSharingStore.setUpAssetLinkSharingGridView();
      oBulkAssetLinkSharingProps.setMasterAssetID(aContentIdForBulkDownload);
      BulkAssetLinkSharingStore.handleAssetShareButtonClicked({bIsTechnicalImage: true, bIsShareSelected: true});
    } else if(sButtonContext === AssetDownloadViewDictionary.BULK_LINK_SHARING_VIEW){
      let oPostDataForDownload = {"ids": aContentIdForBulkDownload};
      CS.postRequest(getRequestMapping().BulkAssetSharedDialogInfo, {}, oPostDataForDownload, successAssetLinkSharingCallback.bind(this, sContext, sButtonContext), failureBulkDownloadAssetsCallback);
      BulkAssetLinkSharingStore.setUpAssetLinkSharingGridView();
    } else if (sButtonContext === AssetDownloadViewDictionary.BULK_ASSET_DOWNLOAD_VIEW) {
      let oPostDataForDownload = {"ids": aContentIdForBulkDownload};
      CS.postRequest(getRequestMapping().BulkAssetDownload, {}, oPostDataForDownload, successBulkDownloadAssetsCallback.bind(this, ""), failureBulkDownloadAssetsCallback);
    }
  };

  let _handleTableDownloadImageButtonClicked = function (sContentId, sContextId = "") {
    let oPostDataForDownload = {"ids": [sContentId]};
    ContentUtils.getComponentProps().bulkDownloadAssetProps.setShowDownloadDialog(true);
    ContentUtils.getComponentProps().bulkDownloadAssetProps.setDownloadFileName("");
    ContentUtils.getComponentProps().bulkDownloadAssetProps.setSelectedContextId(sContextId);
    CS.postRequest(getRequestMapping().BulkAssetDownload, {}, oPostDataForDownload, successBulkDownloadAssetsCallback.bind(this, sContentId), failureBulkDownloadAssetsCallback);
  };

  let successBulkAssetsShareCallback = function () {
    ContentScreenProps.bulkDownloadAssetProps.reset();
    alertify.success(getTranslation().GENERATING_LINK_FOR_SHARING);
    _triggerChange();
  };

  let failureBulkAssetsShareCallback = function (oResponse) {
    ContentScreenProps.bulkDownloadAssetProps.reset();
    ContentUtils.failureCallback(oResponse, 'failureBulkAssetsShareCallback', getTranslation());
  };

  var successCreateContentCallback = function (oResponse) {
    trackMe('successCreateContentsCallback');
    ContentLogUtils.infoAjaxRequest('successCreateContentsCallback', oResponse, 'create content response ');

    var aContentList = ContentUtils.getAppData().getContentList();
    var oContentFromServer = oResponse.success;
    if (CS.isArray(oContentFromServer)) {
      ContentUtils.getAppData().setContentList(oContentFromServer.concat(aContentList));
    } else {
      aContentList.unshift(oContentFromServer);
    }
    ContentUtils.removeAllSelectionInContentScreen();

    _triggerChange();
  };

  var failureCreateContentCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureCreateContentCallback', getTranslation());
  };

  var successFetchUserListCallBack = function (oCallbackData, oResponse) {
    trackMe('successFetchUserListCallBack');
    logger.infoAjaxRequest('successFetchUserListCallBack: In success callback of _fetchUserList',
        {'response': oResponse.success}, 'config/users');
    var aUserList = oResponse.success;
    if (aUserList.length) {
      CS.forEach(aUserList, function (oUser) {
        var sName = oUser.firstName ? oUser.firstName + " " : "";
        sName += oUser.lastName ? oUser.lastName : "";
        oUser.label = sName;
      });
      ContentUtils.getAppData().setUserList(aUserList);
    }
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var failureFetchUserListCallBack = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchUserListCallBack', getTranslation());
    _checkForExtraNecessaryServerCalls(oResponse);
  };

  var _resetDataOnEntityTypeChange = function (oCurrentEntity) {
    var oActiveEntitySelectedTabIdMap = ContentScreenProps.screen.getActiveEntitySelectedTabIdMap();
    oActiveEntitySelectedTabIdMap[oCurrentEntity.id].selectedTabId = null;
    _resetComparisonViewModeData();
  };

  var _resetComparisonViewModeData = function () {
    ContentUtils.setSelectedContentIds([]);
  };

  var successCreateEntityCallback = function (oCallbackData, oResponse) {
    var oCreatedEntity = oResponse.success;
    let oComponentProps = ContentUtils.getComponentProps();

    var oActiveEntitySelectedTabIdMap = ContentScreenProps.screen.getActiveEntitySelectedTabIdMap(); //set GENERAL as the default selected tab
    oActiveEntitySelectedTabIdMap[oCreatedEntity.id] = {selectedTabId: null};

    let sEntityName = ContentUtils.getContentName(oCreatedEntity);
    //fetch article by Id

    SharableURLStore.setIsPushHistoryState(true);

    /** Update breadcrumb related data **/
    let oBreadcrumbItem = oCallbackData.breadCrumbData;
    oBreadcrumbItem.id = oCreatedEntity.id;
    oBreadcrumbItem.label = sEntityName;
    oBreadcrumbItem.baseType = oCreatedEntity.baseType;

    var oFetchCallback = oCallbackData;
    oFetchCallback.functionToExecute = function () {
      ContentUtils.getAppData().setAvailableEntities([]);
    }

    oFetchCallback.entityType = oCreatedEntity.baseType;

    var bAvailableEntityViewStatus = ContentUtils.getRelationshipViewStatus();
    oFetchCallback.isCreated = true;
    if (bAvailableEntityViewStatus || oCallbackData.bShouldDropEntityInRelationshipSection) {
      ContentUtils.getAppData().getAvailableEntities().push(oCreatedEntity);

      var oDropCallback = {};
      oDropCallback.functionToExecute = _fetchArticleById.bind(this, oCreatedEntity.id, oFetchCallback);
      oComponentProps.screen.setIsChooseTaxonomyVisible(true);
      _handleContentEntityDropInRelationshipSection(oBreadcrumbItem.id, oDropCallback);
    } else {
      _fetchArticleById(oCreatedEntity.id, oFetchCallback);
    }
  };

  var failureCreateEntityCallback = function (oCallBack, oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureCreateEntityCallback', getTranslation());
    _checkForExtraNecessaryServerCalls(oResponse);
  };

  var failureGetInstanceComparisionData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureGetInstanceComparisionData', getTranslation());
  };

  //************************************* Private API's **********************************************//

  var _getContentByIdFromServer = function (sId, oCallbackData, oExtraData) {
    trackMe('_getContentByIdFromServer');
    logger.debugAjaxRequest("_getContentByIdFromServer: initiating the call for fetching content by id",
        {url: getRequestMapping().GetContentById, id: sId}, "fetch content by id");

    if (CS.isEmpty(sId) || sId == ContentUtils.getTreeRootNodeId()) {
      ContentUtils.setSelectedContentIds([]);
      ContentScreenProps.screen.setPaginatedIndex(0);
      _fetchContentList(oCallbackData);
    }
    else {
      _fetchArticleById(sId, oCallbackData, oExtraData);
      var oScreenProps = ContentScreenProps.screen;
      oScreenProps.resetDataRuleFilterProps();
    }
  };

  var _fetchArticleById = function (sArticleId, oCallbackData, oExtraData) {
    var oAppData = ContentUtils.getAppData();
    var oScreenProps = ContentScreenProps.screen;

    var iSize = oExtraData ? (oExtraData.postData ? oExtraData.postData.size : oScreenProps.getDefaultPaginationLimit()) : oScreenProps.getDefaultPaginationLimit();
    var iFrom =  oExtraData ? (oExtraData.postData ? oExtraData.postData.from : 0) : 0;
    var sSortField = oScreenProps.getActiveSortingField();
    var sSortOrder = oScreenProps.getActiveSortingOrder();
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[sArticleId];
    let bLanguageComparisonModeOn = oActiveContentMap && oActiveContentMap.languageComparisonModeOn && oActiveContentMap.selectedTabId !== ContentScreenConstants.tabItems.TAB_USAGES;

    oScreenProps.setAssetList([]);

    /** disabled this functionality due to multi tab functionality*/
    var oAddEntityInRelationshipScreenData = oScreenProps.getAddEntityInRelationshipScreenData();
    delete oAddEntityInRelationshipScreenData[sArticleId];

    var oActiveEntity = _getActiveEntity() || {};
    var sEntityBaseType = oCallbackData.entityType;
    if (CS.isEmpty(sArticleId)) {
      if (CS.isEmpty(oActiveEntity)) {
        sArticleId = ContentUtils.getTreeRootNodeId();
        sEntityBaseType = BaseTypesDictionary.contentBaseType;
      }
      else {
        sArticleId = oActiveEntity.id;
      }
    }

    if (sArticleId == oActiveEntity.id) {
      sEntityBaseType = oActiveEntity.baseType;
    }

    var sSelectedTabId = ContentUtils.getSelectedTabId(sArticleId);
    var sCurrentTab = oCallbackData.isViolationClicked ? "customtab" : ContentUtils.getTabUrlFromTabId(sSelectedTabId);  //"customtab" when content opened from rule violation - Neha
    var oData = {
      id: sArticleId,
      tab: sCurrentTab,
      isLoadMore: false,
      getAll: true
    };

    let oSelectedTemplate = null;
    if (oActiveEntity.id === sArticleId) {
      oSelectedTemplate = oScreenProps.getSelectedTemplate();
    } else {
      let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
      let oSelectedEntityTemplate = oActiveEntitySelectedTabIdMap[sArticleId];
      if (!CS.isEmpty(oSelectedEntityTemplate) && !CS.isEmpty(oSelectedEntityTemplate.selectedTemplate)) {
        oSelectedTemplate = oSelectedEntityTemplate.selectedTemplate;
      }
    }
    let templateObj = {};
    if (oSelectedTemplate) {
      templateObj = ContentUtils.getTemplateAndTypeIdForServer(oSelectedTemplate.id);
    }

    var sTabType = ContentUtils.getTabTypeFromTabId(sSelectedTabId);
    var sChildContextId = null;
    var sVariantInstanceId = undefined; //Needs to set undefined, If not a variant get then don't send the key itself

    var oFilterPostData = {
      size: iSize,
      from: iFrom,
      sortField: sSortField,
      sortOrder: sSortOrder,
      tabId: sSelectedTabId,
      tabType: sTabType,
      childContextId: sChildContextId,
      variantInstanceId: sVariantInstanceId
    };

    oFilterPostData.typeId = templateObj.typeId;
    oFilterPostData.templateId = templateObj.templateId;

    if (!CS.isEmpty(oExtraData)) {
      CS.assign(oFilterPostData, oExtraData);
    }

    oFilterPostData.selectedTimeRange = {
      endTime: null,
      startTime: null
    }

    let oAjaxExtraData = {
      URL: _getEntityByIdUrl(sEntityBaseType),
      requestData: oData,
      postData: oFilterPostData,
    };

    if (bLanguageComparisonModeOn) {
      let sSelectedTab = ContentUtils.getSelectedTabId();
      let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
      let sHelperLanguageCode = oActiveContentMap.languageCodeToCompare;
      let aExcludedTabsForHelperLanguage =
          [
            ContentScreenConstants.tabItems.TAB_TIMELINE,
            ContentScreenConstants.tabItems.TAB_OVERVIEW,
            ContentScreenConstants.tabItems.TAB_TASKS,
            ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS,
          ];

      if (CS.includes(aExcludedTabsForHelperLanguage, sSelectedTab) || sHelperLanguageCode == sSelectedDataLanguageCode) {
        oActiveContentMap.languageComparisonModeOn = false;
        oActiveContentMap.languageCodeToCompare = "";
      } else {
        let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(sEntityBaseType);
        oAjaxExtraData.URL = getRequestMapping(sScreenMode).GetEntityForContentLanguageComparison;
        oAjaxExtraData.postData.id = sArticleId;
        oAjaxExtraData.postData.languagesToBeCompared = [sHelperLanguageCode];
      }
    }

    let aPayloadData = [oCallbackData, oAjaxExtraData];
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem("", "", "", "", {}, {}, "", aPayloadData, _fetchArticleByIdCall);
    return _fetchArticleByIdCall(oCallbackData, oAjaxExtraData);
  };

  var _fetchArticleByIdCall = function (oCallbackData, oAjaxExtraData) {
    let fSuccess = successFetchArticleById.bind(this, oCallbackData);
    let fFailure = failureFetchArticleById.bind(this, oCallbackData);
    return CS.postRequest(oAjaxExtraData.URL, oAjaxExtraData.requestData, oAjaxExtraData.postData, fSuccess, fFailure);
  };

  var _validateAndManageScheduleInfo = function (oContent) {
    var bScheduleInfo = true;
    if (oContent.hasOwnProperty("eventSchedule") && !CS.isEmpty(oContent.eventSchedule)) {
      let oEventSchedule = oContent.eventSchedule;
      if ((oEventSchedule.startTime && !oEventSchedule.endTime) || (oEventSchedule.endTime && !oEventSchedule.startTime)) {
        bScheduleInfo = false;
      }
      if (oEventSchedule.startTime > oEventSchedule.endTime) {
        bScheduleInfo = false;
      }
    }
    return bScheduleInfo;
  };

  var _checkEventScheduleChanged = function (oContent) {
    let oEventSchedule = oContent.eventSchedule;
    let oClonedEventSchedule = oContent.contentClone.eventSchedule;
    if (oEventSchedule.startTime === oClonedEventSchedule.startTime && oEventSchedule.endTime === oClonedEventSchedule.endTime) {
      return false;
    }
    return true;
  };

  var _validateContent = function (oContent) {
    var sScreenContext = ContentUtils.getContentScreenMode();
    trackMe('_validateContent: ' + sScreenContext);
    ContentLogUtils.debug('_validateContent: validation for duplicate name of content', oContent);

    var sContentName = oContent.name;
    var bNameValidation = sContentName.trim() != "";
    var bDuplicateNameValidation = true;
    var bVariantTagsValidation = true;

    let bVariantTimeRangeValidation = true;
    let oTempModifiedRelationshipContextData = ContentScreenProps.screen.getModifiedRelationshipsContextTempData();
    CS.forEach(oTempModifiedRelationshipContextData, function (oValue) {
      if (oValue.hasOwnProperty("timeRange")) {
        if (!oValue.timeRange.from || !oValue.timeRange.to) {
          bVariantTimeRangeValidation = false;
          return false;
        }
      }
    });

    return {
      nameValidation: bNameValidation,
      duplicateNameValidation: bDuplicateNameValidation,
      variantTagsValidation: bVariantTagsValidation,
      variantTimeRangeValidation: bVariantTimeRangeValidation
    };
  };

  var _validateContents = function (oContent) {
    var sScreenContext = ContentUtils.getContentScreenMode();
    trackMe('_validateContents: ' + sScreenContext);
    ContentLogUtils.debug('_validateContents: validation for duplicate name of content', oContent);

    var oRes = {
      contentWithBlankNames: [],
      contentWithDuplicateNames: [],
      nameValidation: true,
      duplicateNameValidation: true,
      variantTagsValidation: true,
      variantTimeRangeValidation: true,
      natureRelationshipTagValidation: true
    };

    var sContentId = oContent.id;
    var oValidation = oContent.contentClone ? _validateContent(oContent.contentClone) : _validateContent(oContent);
    oRes.nameValidation = oRes.nameValidation && oValidation.nameValidation;
    oRes.duplicateNameValidation = oRes.duplicateNameValidation && oValidation.duplicateNameValidation;
    oRes.variantTagsValidation = oRes.variantTagsValidation && oValidation.variantTagsValidation;
    oRes.variantTimeRangeValidation = oValidation.variantTimeRangeValidation;

    if (!oValidation.nameValidation) {
      oRes.contentWithBlankNames.push(sContentId);
    }

    return oRes;
  };

  var _getRelationshipSizeAndStartIndex = function (oContentToSave) {
    var iSize = ContentScreenProps.screen.getDefaultPaginationInnerLimit();
    var iStartIndex = 0;
    var sRelationshipId = "";
    if (!CS.isEmpty(oContentToSave.modifiedRelationships)) {
      sRelationshipId = oContentToSave.modifiedRelationships[0].relationshipId;
    } else if (CS.isEmpty(oContentToSave.modifiedNatureRelationships)) {
      sRelationshipId = oContentToSave.modifiedNatureRelationships[0].relationshipId;
    }
    if (!CS.isEmpty(sRelationshipId)) {
      var oRelationshipToolbarProps = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
      var oRelationshipSortProp = oRelationshipToolbarProps[sRelationshipId];
      if (oRelationshipSortProp) {
        iStartIndex = oRelationshipSortProp.startIndex;
        iSize = oRelationshipSortProp.paginationLimit;
      }
    }

    return {
      size: iSize,
      startIndex: iStartIndex
    };
  };

  var _getLinkedInstancesData = function (oLinkedInstances) {
    var aLinkedInstanceList = [];
    CS.forEach(oLinkedInstances, function (aLinkedInstances) {
      CS.find(aLinkedInstances, function (oLinkedInstance) {
        aLinkedInstanceList.push({
          id: oLinkedInstance.id,
          baseType: oLinkedInstance.baseType
        });
      });
    });
    return aLinkedInstanceList;
  };

  let _getAssetReplaceStatus = function (oActiveContent, oContentToSave) {
    let oActiveContentClass = ContentUtils.getActiveContentClass();
    if (oActiveContentClass.trackDownloads && !CS.isNull(oActiveContent.assetInformation)) {
      oContentToSave.isResetDownloadCount = !CS.isEqual(oActiveContent.assetInformation, oContentToSave.assetInformation);
    }
    return oContentToSave;
  };

  var _mapContentsForServer = function (oContent) {
    trackMe('_mapContentsForServer');
    ContentLogUtils.debug('_mapContentsForServer: remove properties like isCreated, isDirty from contents to save', oContent);
    var oScreenProps = ContentScreenProps.screen;
    var aAttributeToRemove = ['isDirty', 'masterKlasses', 'isCreated'];
    var oContentClone = CS.cloneDeep(oContent.contentClone) || CS.cloneDeep(oContent);
    var oContentToSave = CS.omit(oContentClone, function (value, key) {
      return CS.includes(aAttributeToRemove, key);
    });

    var oADMForAttributes = ContentUtils.generateADMForAttribute(oContent.attributes, oContentToSave.attributes);
    oContentToSave.addedAttributes = oADMForAttributes.added;
    oContentToSave.deletedAttributes = oADMForAttributes.deleted;
    oContentToSave.modifiedAttributes = oADMForAttributes.modified;
    delete oContentToSave.attributes;

    var oADMForTags = ContentUtils.generateADMForTags(oContent.tags, oContentToSave.tags);
    oContentToSave.addedTags = oADMForTags.added;
    oContentToSave.deletedTags = oADMForTags.deleted;
    oContentToSave.modifiedTags = oADMForTags.modified;
    delete oContentToSave.tags;

    oContentToSave.addedRelationships = [];//oADMForContentRelationships.added;
    //No situation for deleted relationship
    oContentToSave.deletedRelationships = [];//oADMForContentRelationships.deleted;
    oContentToSave.modifiedRelationships = [];//oADMForContentRelationships.modified;
    delete oContentToSave.contentRelationships;

    oContentToSave.addedNatureRelationships = [];//oADMForNatureRelationships.added;
    oContentToSave.deletedNatureRelationships = [];
    oContentToSave.modifiedNatureRelationships = [];//oADMForNatureRelationships.modified;
    delete oContentToSave.natureRelationships;

    var oADMForAssets = ContentUtils.generateADMForAssets(oContent.referencedAssets, oContentToSave.referencedAssets);
    oContentToSave.addedAssets = oADMForAssets.added;
    oContentToSave.deletedAssets = oADMForAssets.deleted;
    oContentToSave.modifiedAssets = oADMForAssets.modified;
    delete oContentToSave.referencedAssets;
    delete oContentToSave.assets;

    delete oContentToSave.masterKlasses;

    if (!CS.isEmpty(oContentToSave.eventSchedule) && oContent.hasOwnProperty("eventSchedule") && !CS.isEqual(oContent.eventSchedule, oContentToSave.eventSchedule)) {
      oContentToSave.modifiedEventSchedule = oContentToSave.eventSchedule;
      delete oContentToSave.eventSchedule;
    }

    oContentToSave = _getAssetReplaceStatus(oContent, oContentToSave);

    var aOldContentLinkedInstances = _getLinkedInstancesData(oContent.linkedInstances);
    var aNewContentLinkedInstances = _getLinkedInstancesData(oContentToSave.linkedInstances);

    /** TODO: Fallback logic needs to remove after QA testing **/
    var oLinkedInstanceADM = ContentUtils.generateADMForLinkedInstances(aNewContentLinkedInstances, aOldContentLinkedInstances);
    oContentToSave.addedLinkedInstances = oLinkedInstanceADM.addedLinkedInstances;
    oContentToSave.deletedLinkedInstances = oLinkedInstanceADM.deletedLinkedInstances;
    delete oContentToSave.linkedInstances;

    //TODO: check if code inside this condition is ever executed, if not then delete the code
    if (oContent.variantInstanceId) {
      var oOldTimeRange = oContent.timeRange || {};
      var oNewTimeRange = oContentToSave.timeRange || {};

      if (oOldTimeRange.to != oNewTimeRange.to || oOldTimeRange.from != oNewTimeRange.from) {
        oContentToSave.modifiedTimeRange = {};
        oContentToSave.modifiedTimeRange.to = oNewTimeRange.to;
        oContentToSave.modifiedTimeRange.from = oNewTimeRange.from;
      }

    }

    let sContentToSaveName = ContentUtils.getContentName(oContentToSave);
    let sContentName = ContentUtils.getContentName(oContent);
    if (sContentName === sContentToSaveName) {
      delete oContentToSave.name;
    }

    var iSize = oScreenProps.getDefaultPaginationLimit();
    var iStartIndex = oScreenProps.getPaginatedIndex();

    /** extra handling to set proper size n from data in case of relationship add && remove case*/
    if (!CS.isEmpty(oContentToSave.modifiedNatureRelationships) || !CS.isEmpty(oContentToSave.modifiedRelationships)) {
      var oRelationshipSizeStartIndex = _getRelationshipSizeAndStartIndex(oContentToSave);
      iSize = oRelationshipSizeStartIndex.size;
      iStartIndex = oRelationshipSizeStartIndex.startIndex;
    }

    oContentToSave.getKlassInstanceTreeInfo = ContentUtils.getEmptyFilterObject(iSize, iStartIndex);
    oContentToSave.getKlassInstanceTreeInfo["variantInstanceId"] = oContentToSave.variantInstanceId;

    var sSelectedTab = ContentUtils.getSelectedTabId();
    oContentToSave.tabType = ContentUtils.getTabTypeFromTabId(sSelectedTab);
    oContentToSave.tabId = sSelectedTab; //todo: CT

    let oSelectedTemplate = oScreenProps.getSelectedTemplate();

    let templateObj = {};
    if (oSelectedTemplate) {
      templateObj = ContentUtils.getTemplateAndTypeIdForServer(oSelectedTemplate.id);
    }

    oContentToSave.typeId = templateObj.typeId;
    oContentToSave.templateId = templateObj.templateId;

    return oContentToSave;
  };

  var _getViolatingMandatoryFields = function (oContent) {
    var aViolatingMandatoryElements = [];
    var oContentClone = oContent.contentClone || oContent;
    var oScreenProps = ContentUtils.getComponentProps().screen;
    var aActiveSections = oScreenProps.getActiveSections();
    CS.forEach(aActiveSections, function (oSection) {
      CS.forEach(oSection.elements, function (oElement) {
        if (oElement.type == "attribute") {
          var oProperty = CS.find(oContentClone.attributes, {attributeId: oElement.id});
          if (oElement.isMandatory && (!oProperty || (!oProperty.value || !(oProperty.value + "").trim()))) { //trim
            // mainly
            // used to ignore the
            // new line character in html attribute
            aViolatingMandatoryElements.push(oElement.id);
          }
        } else if (oElement.type == "tag") {
          var oProperty = CS.find(oContentClone.tags, {tagId: oElement.id});
          if (oElement.isMandatory) {
            if (!oProperty || CS.isEmpty(oProperty.tagValues)) {
              aViolatingMandatoryElements.push(oElement.id);
            } else if (!CS.isEmpty(oProperty.tagValues)) {
              var aTagValues = oProperty.tagValues;
              var bIsAllRelevanceZero = true;
              CS.forEach(aTagValues, function (oTagValue) {
                if (oTagValue.relevance != 0) {
                  bIsAllRelevanceZero = false;
                  return false;
                }
              });
              if (bIsAllRelevanceZero) {
                aViolatingMandatoryElements.push(oElement.id);
              }
            }
          }

        }
      });
    });
    return aViolatingMandatoryElements;
  };

  let _getLanguagesToCompare = function (sActiveContentId) {
    let oScreenProps = ContentScreenProps.screen;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[sActiveContentId];
    //  check
    let bLanguageComparisonModeOn = oActiveContentMap && oActiveContentMap.languageComparisonModeOn;
    if (bLanguageComparisonModeOn) {
      let sHelperLanguageCode = oActiveContentMap.languageCodeToCompare;
      return [sHelperLanguageCode]
    } else {
      return null;
    }
  };

  let _getURLForSaveContent = function (oContentToSave) {
    let oScreenProps = ContentScreenProps.screen;
    let sScreenContext = ContentUtils.getScreenModeBasedOnEntityBaseType(oContentToSave.baseType);
    let sSelectedTabId = ContentUtils.getSelectedTabId();
    let bIsOverviewTab = sSelectedTabId === TemplateTabsDictionary.OVERVIEW_TAB;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[oContentToSave.id];
    //  check
    let bLanguageComparisonModeOn = oActiveContentMap && oActiveContentMap.languageComparisonModeOn;

    if (bIsOverviewTab) {
      return getRequestMapping(sScreenContext).SaveEntityForOverviewTab;
    } else if (bLanguageComparisonModeOn) {
      return getRequestMapping(sScreenContext).SaveEntityInLanguageComparisonMode;
    } else {
      return getRequestMapping(sScreenContext).SaveEntity;
    }
  };

  let _mapContentsAndSave = function (oContent, oCallbackDataForSave) {
    //check for zero tag selections for variant
    if (oContent.variantInstanceId) {
      if (!_variantContextSelectionCheck()) {
        return;
      }
    }

    _updateMandatoryElementsStatus(oContent);

    let oContentToSave = _mapContentsForServer(oContent);
    let oSelectedContext = ContentScreenProps.variantSectionViewProps.getSelectedContext();
    let oSelectedVisibleContext = ContentScreenProps.variantSectionViewProps.getSelectedVisibleContext();
    if (!CS.isEmpty(oSelectedContext)) {
      oContentToSave.contextId = oSelectedContext.id;
    }

    if (!CS.isEmpty(oSelectedVisibleContext)) {
      oContentToSave.childContextId = oSelectedVisibleContext.id;
    }
    let sSelectedTabId = ContentUtils.getSelectedTabId();
    oContentToSave.isLinked = sSelectedTabId != "relationships";

    ContentLogUtils.infoAjaxRequest('_saveContents: Making server call to save content', oContentToSave, 'content/save', 'ADM');

    oContentToSave.isSaveAndPublish = oCallbackDataForSave.isSaveAndPublish;
    oCallbackDataForSave.contentsToSave = oContentToSave;
    let sUrl = _getURLForSaveContent(oContentToSave);

    let aLanguagesToCompare = _getLanguagesToCompare(oContentToSave.id);
    aLanguagesToCompare && (oContentToSave.languagesToCompare = aLanguagesToCompare);

    if (oContentToSave.isResetDownloadCount) {
      oCallbackDataForSave.functionToGetDownloadCount = _getAssetDownloadCount;
    }

    let fPostRequest = CS.postRequest.bind(this, sUrl, {isRollback: false}, oContentToSave, successSaveContentsCallback.bind(this, oCallbackDataForSave), failureSaveContentsCallback.bind(this, oCallbackDataForSave), false, {dataLanguage: oCallbackDataForSave.dataLanguage});

    return fPostRequest();
  };

  var _saveContents = function (oContent, oCallbackDataForSave = {}) {
    trackMe('_saveContents');
    ContentLogUtils.debug('_saveContents: saving the content', oContent);
    var sScreenContext = ContentUtils.getScreenModeBasedOnEntityBaseType(oContent.baseType);

    var oContentsValid = _validateContents(oContent);
    ImageCoverflowUtils.resetActiveVideoStatusRequests();

    if (!oContentsValid.nameValidation) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);

      return;
    }
    else if (!oContentsValid.variantTimeRangeValidation) {
      alertify.error(getTranslation().STORE_ALERT_CONTENT_INVALID_TIME_RANGE);
      return;
    }

    if (sScreenContext === ContentScreenConstants.entityModes.ASSET_MODE) {
      var bEventScheduleChanged = _checkEventScheduleChanged(oContent);
      var bScheduleValidity = _validateAndManageScheduleInfo(oContent.contentClone || oContent);
      if (!bScheduleValidity) {
          let sMessage = getTranslation().ASSET_SCHEDULE_INFORMATION_INCOMPLETE;
          alertify.error(sMessage);
          return;
      }
      if (bEventScheduleChanged) {
        var oNatureRelationships = oContent.natureRelationships;
        var oRelationship = null;
        var sMessage = "";
        var aElementIds = [];
        if (!CS.isEmpty(oRelationship)) {
          var oSubRelationship = CS.find(oNatureRelationships, {relationshipId: oRelationship.id});
          aElementIds = oSubRelationship.elementIds;
        }

        if (CS.isEmpty(aElementIds)) {
          return _mapContentsAndSave(oContent, oCallbackDataForSave);
        } else {
          CustomActionDialogStore.showConfirmDialog(sMessage, '',
              function () {
                return _mapContentsAndSave(oContent, oCallbackDataForSave);
              }, function (oEvent) {
              });
        }
        return;
      }
    }

    if (!oContentsValid.variantTagsValidation) {
      CustomActionDialogStore.showConfirmDialog(getTranslation().STORE_ALERT_CONFIRM_SAVE_VARIANT, '',
          function () {
            _mapContentsAndSave(oContent, oCallbackDataForSave);
          }, function (oEvent) {
          });
      return;
    }

    return _mapContentsAndSave(oContent, oCallbackDataForSave);
  };

  let _processDataForFetchContentList = (oCallbackData = {}, oExtraData = {}) => {
    let oFilterContext = oCallbackData.filterContext;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    let oScreenProps = ContentScreenProps.screen;

    let sUrl = '';
    let sEntityId = "";
    let sType = "";
    let sLabel = "";
    let sHelpScreenId = "";
    let sId = ContentUtils.getTreeRootNodeId();
    let oSelectedModule = GlobalStore.getSelectedModule();

    //Collection related processing
    let oCollectionViewProps = ContentScreenProps.collectionViewProps;
    let oActiveCollection = oCollectionViewProps.getActiveCollection();
    if (!CS.isEmpty(oActiveCollection)) {
      if (oActiveCollection.type === "staticCollection") {
        if (oCollectionViewProps.getAddEntityInCollectionViewStatus()) {
          sId = oActiveCollection.id;
          oCallbackData.isCollectionCallback = true;
          let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveCollection.type);
          sUrl = getRequestMapping(sScreenMode).GetStaticCollection;
        } else {
          /**
           * Required to open Static collection on click of Next/Previous button  or navigated through browser button.
           */
          let sCollectionType = oActiveCollection.type;
          NewCollectionStore.handleCollectionSelected(oActiveCollection, sCollectionType, oCallbackData, oExtraData);
          return;
        }
      } else if (oActiveCollection.type === "dynamicCollection") {
        sUrl = getRequestMapping().GetAllInstances;
      }
    } else if (oSelectedModule.id === ModuleDictionary.FILES) {
      oCallbackData.isFilesmodule = true;
      sUrl = getRequestMapping().GetFileInstance;
    } else {
      sUrl = getRequestMapping().GetAllInstances;
    }

    let oBreadCrumbData = {};

    if (oCallbackData.breadCrumbData) {
      oBreadCrumbData = oCallbackData.breadCrumbData;
    } else {
      let sSelectedPhysicalCatalogId = ContentUtils.getSelectedPhysicalCatalogId();
      let sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();
      let oInformationTabProps = ContentScreenProps.informationTabProps;
      let oDamInformationTabProps = ContentScreenProps.damInformationTabProps;
      sEntityId = oSelectedModule.id;
      sType = "module";
      sLabel = getTranslation()[oSelectedModule.label] || oSelectedModule.label;
      sHelpScreenId = oSelectedModule.id;
      if (sSelectedPhysicalCatalogId === PhysicalCatalogDictionary.DATAINTEGRATION) {
        let oActiveEndpoint = ContentUtils.getActiveEndpointData();
        sEntityId = oSelectedModule.id;
        sLabel = getTranslation().ENDPOINT + oActiveEndpoint.label + " : " + sLabel;
        sType = BreadCrumbModuleAndHelpScreenIdDictionary.DIMODULE;
        sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.ENDPOINT_SCREEN
      } else if (oInformationTabProps.getIsRuleViolatedContentsScreen()) {
        sEntityId = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION;
        sLabel = getTranslation().RULE_VIOLATIONS;
        sType = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION;
        sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION;
      } else if (oDamInformationTabProps.getIsRuleViolatedContentsScreen()) {
        sEntityId = BreadCrumbModuleAndHelpScreenIdDictionary.ASSET_RULE_VIOLATION;
        sLabel = getTranslation().RULE_VIOLATIONS;
        sType = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION;
        sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION;
      } else if (ContentScreenProps.screen.getIsKpiContentExplorerOpen()) {
        let oKPIData = ContentScreenProps.screen.getKpiContentExplorerData();
        sEntityId = oKPIData.kpiId;
        sLabel = oKPIData.kpiLabel;
        sType = BreadCrumbModuleAndHelpScreenIdDictionary.EXPLORER_KPI_TILE;
        sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.EXPLORER_KPI_TILE;
      } else if (sSelectedHierarchyContext === BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY) {
        sEntityId = sSelectedHierarchyContext;
        sLabel = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_HIERARCHY,{ entity : getTranslation().TAXONOMY });
        sType = BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY;
        sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY;
      } else if (sSelectedHierarchyContext === BreadCrumbModuleAndHelpScreenIdDictionary.COLLECTION_HIERARCHY) {
        sEntityId = sSelectedHierarchyContext;
        sLabel = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_HIERARCHY,{ entity : getTranslation().COLLECTION });
        sType = BreadCrumbModuleAndHelpScreenIdDictionary.COLLECTION_HIERARCHY;
        sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.COLLECTION_HIERARCHY;
      } else if (CS.isNotEmpty(oActiveCollection)) {
        if (oActiveCollection.type === BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION) {
          sEntityId = oActiveCollection.id;
          sType = BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION;
          sLabel = getTranslation().BOOKMARK + ": " + oActiveCollection.label;
          sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION;
        } else if (oActiveCollection.type === BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION && oCollectionViewProps.getAddEntityInCollectionViewStatus()) {
          sEntityId = BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST;
          sType = BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST;
          sLabel = getTranslation().ADD_TO_COLLECTION;
          sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION;
        }
      }
      oBreadCrumbData = BreadcrumbStore.createBreadcrumbItem(sEntityId, sLabel, sType, sHelpScreenId, oCallbackData.filterContext);
      /** For Reference.. DO NOT REMOVE**/
      /* if (!CS.isEmpty(oBreadCrumbData)) {
         sEntityId = oBreadCrumbData.id;
         sLabel = oBreadCrumbData.label;
         sType = oBreadCrumbData.type;
         sHelpScreenId = oBreadCrumbData.helpScreenId;
       }
       oCallbackData.breadCrumbData = {
         id: sEntityId,
         label: sLabel,
         type: sType,
         helpScreenId: sHelpScreenId,
         extraData: oAjaxExtraData,
       };*/
    }

    /**
     * Required to set pagination data on click of Next/Previous button  or when pagination size is changed.
     */
    let oFilterParameters = oExtraData.postData || {};
    if(!CS.isNull(oFilterParameters.size) && oFilterParameters.size != undefined)
    {
      oFilterProps.setPaginationSize(oFilterParameters.size);
    }
    if(!CS.isNull(oFilterParameters.from) && oFilterParameters.from != undefined)
    {
      oFilterProps.setFromValue(oFilterParameters.from);
    }

    let oActiveEntity = _getActiveEntity();
    if (!CS.isEmpty(oActiveEntity)) {
      sId = oActiveEntity.id;
    }

    let oFilterParameter = {};
    if (ContentScreenProps.informationTabProps.getIsRuleViolatedContentsScreen()) {
      oFilterParameter = InformationTabStore.getFilterParameters(DashboardTabDictionary.INFORMATION_TAB);
      CS.assign(oFilterParameter, oFilterParameters);
    }

    let oRequestData = ContentUtils.getAllInstancesRequestData(oCallbackData.filterContext);
    let oSpecialUsecaseFilterData = AvailableEntityStore.handleSpecialUsecaseFilters(oCallbackData);
    CS.isNotEmpty(oSpecialUsecaseFilterData) && CS.assign(oRequestData, oSpecialUsecaseFilterData);

    if (CS.isEmpty(oRequestData.moduleId)) {
      throw ContentScreenConstants.MODULE_ID_CAN_NOT_BE_EMPTY;
    }

    let oXRayData = ContentUtils.getXRayPostData();
    let sCurrentThumbnailMode = oScreenProps.getThumbnailMode();
    let bIsXRayMode = sCurrentThumbnailMode === ThumbnailModeConstants.XRAY;
    if (!CS.isEmpty(oXRayData) && bIsXRayMode) {
      CS.assign(oRequestData, oXRayData);
    }
    oRequestData.xrayEnabled = bIsXRayMode;

    if (ContentScreenProps.screen.getIsKpiContentExplorerOpen()) {
      let oKpiData = ContentScreenProps.screen.getKpiContentExplorerData();
      oRequestData.kpiId = oKpiData.kpiId;
    }

    let bIsArchive = ContentUtils.getIsArchive();
    oRequestData.isArchivePortal = bIsArchive;

    if (!CS.isEmpty(oActiveCollection) && oActiveCollection.type === "staticCollection" && oCollectionViewProps.getAddEntityInCollectionViewStatus()) {
      oRequestData.collectionId = sId;
    }

    let oAjaxExtraData = {
      URL: sUrl,
      requestData: {id: sId},
      postData: oRequestData,
    };

    /** To update available filter models post success */
    if (oFilterProps.getIsFilterInformationRequired()) {
      oCallbackData.customModel = {
        attributes: oRequestData.attributes,
        tags: oRequestData.tags,
        selectedTaxonomyIds: oRequestData.selectedTaxonomyIds,
        selectedTypes: oRequestData.selectedTypes,
      }
    }

    /**
     * Prevent applied filters data reset.
     */
    let aAppliedFilters = oFilterProps.getAppliedFilters();
    oCallbackData.preventResetFilterProps = CS.isNotEmpty(aAppliedFilters);

    let oPostRequestExtraData = {};
    oBreadCrumbData.payloadData = [oCallbackData, oAjaxExtraData, oPostRequestExtraData];
    oBreadCrumbData.functionToSet = _fetchContentCall;

    CS.assign(oBreadCrumbData.extraData, oAjaxExtraData);
    oCallbackData.breadCrumbData = oBreadCrumbData;

    oCallbackData.getDefaultTypesCallback = _handleModuleCreateButtonClicked.bind(this, "", "", oFilterContext, false);

    return _fetchContentListCall(oCallbackData, oAjaxExtraData, oExtraData);
  };

  var _fetchContentList = function (oCallbackData, oExtraData) {
    trackMe('_fetchContentList');
    return _processDataForFetchContentList(oCallbackData, oExtraData);
  };

  let _fetchContentCall = function (oCallbackData, oAjaxExtraData, oExtraData) {
    let oFilterProps = ContentUtils.getFilterProps(oCallbackData.filterContext);
    let oPostData = oAjaxExtraData.postData;
    if(oFilterProps.getShowDetails() && !oPostData.isFilterDataRequired){
      oPostData.isFilterDataRequired = true;
    }
    _fetchContentListCall(oCallbackData, oAjaxExtraData, oExtraData);
  };

  let _fetchContentListCall = function (oCallbackData, oAjaxExtraData, oExtraData) {
    var fSuccess = successFetchContentListCallBack.bind(this, oCallbackData);
    var fFailure = failureFetchContentListCallBack.bind(this, oCallbackData);

    if (oCallbackData.isFilesmodule) {
      let oPostData = {
        from: oAjaxExtraData.postData.from,
        size: oAjaxExtraData.postData.size
      };
      CS.postRequest(oAjaxExtraData.URL, {}, oPostData, successFetchFileContentListCallBack.bind(this, oCallbackData), fFailure);
    } else {
      return CS.postRequest(oAjaxExtraData.URL, oAjaxExtraData.requestData, oAjaxExtraData.postData, fSuccess, fFailure, "", oExtraData);
    }
  };

  var _loadMoreFetchEntities = function (aEntityList, oCallback) {
    var oAppData = ContentUtils.getAppData();
    var oPaginationData = ContentScreenProps.screen.getEntitiesPaginationData();
    CS.forEach(aEntityList, function (sEntity) {
      let aEntityList = [];
      var oEntityPagination = oPaginationData[sEntity];
      if (!oEntityPagination) {
        return;
      }
      switch (sEntity) {
        case ConfigDataEntitiesDictionary.ATTRIBUTES:
          aEntityList = oAppData.getAttributeListMap();
          break;
        case ConfigDataEntitiesDictionary.TAGS:
          aEntityList = oAppData.getTagListMap();
          break;
        case ConfigDataEntitiesDictionary.ROLES:
          aEntityList = oAppData.getRoleList();
          break;
        case ConfigDataEntitiesDictionary.PROPERTY_COLLECTIONS:
          aEntityList = ContentScreenProps.screen.getPropertyCollectionsList();
          break;
      }
      oEntityPagination.from = CS.size(aEntityList);
    });
    return _fetchEntities(aEntityList, true, oCallback);
  };

  var _fetchEntities = function (aEntityList, bLoadMore, oCallback, aAttributeTypesToExclude) {
    var sSearchText = ContentScreenProps.screen.getEntitySearchText();
    var oPaginationData = ContentScreenProps.screen.getEntitiesPaginationData();
    if (CS.isEmpty(aEntityList)) {
      aEntityList = [ConfigDataEntitiesDictionary.ATTRIBUTES, ConfigDataEntitiesDictionary.TAGS, ConfigDataEntitiesDictionary.ROLES, ConfigDataEntitiesDictionary.PROPERTY_COLLECTIONS];
    }
    if (!CS.isEmpty(aAttributeTypesToExclude)) {
      oPaginationData.attributes.typesToExclude = CS.union(oPaginationData.attributes.typesToExclude,aAttributeTypesToExclude);
    }
    var oEntitiesPaginationData = CS.pick(oPaginationData, aEntityList);

    //TODO: Hack : quick solution for onboarding - shashank
    if (ContentUtils.isOnboarding()) {
      CS.assign(oEntitiesPaginationData, CS.pick(oPaginationData, ConfigDataEntitiesDictionary.TAXONOMIES));
      CS.forEach(oEntitiesPaginationData, function (oData) {
        oData.size = 99999;
      });
    }
    var sSelectedModuleId = ContentUtils.isCollectionScreen() ? ModuleDictionary.ALL : ContentUtils.getSelectedModuleId();

    var oRequestData = {
      searchColumn: "label",
      searchText: sSearchText,
      entities: oEntitiesPaginationData,
      moduleId: sSelectedModuleId
    };

    return CS.postRequest(ContentScreenRequestMapping.GetConfigData, {}, oRequestData, successFetchEntitiesList.bind(this, bLoadMore, oCallback), failureFetchEntitiesList);
  };

  var _getXRayProperties = function () {
    let aEntityList = [ConfigDataEntitiesDictionary.ATTRIBUTES, ConfigDataEntitiesDictionary.TAGS];
    let aGroups = [];
    CS.each(aEntityList, entity => {
      let [id, label, resPath, customIconClassName, entitiesToExclude] = ["", "", [], "", []];
      switch (entity) {
        case "attributes":
          id = "attribute";
          label = getTranslation().ATTRIBUTES;
          resPath = ["success", entity];
          customIconClassName = "attribute";
          entitiesToExclude.push('assetdownloadcountattribute');
          break;
        case "tags":
          id = "tag";
          label = getTranslation().TAGS
          resPath = ["success", entity];
          customIconClassName = "tag";
          break;
      }
      var oRequestData = {
        id: id,
        "label": label,
        customIconClassName: customIconClassName,
        "requestInfo": {
          "requestType": "configData",
          "responsePath": resPath,
          "entities": entity,
          "requestURL": ContentScreenRequestMapping.GetConfigData,
          "entitiesToExclude" : entitiesToExclude
        }
      }
      aGroups.push(oRequestData);
    })
    return aGroups;
  }

  var successFetchEntitiesList = function (bLoadMore, oCallback, oResponse) {
    oResponse = oResponse.success;
    var oTotalCounts = ContentScreenProps.screen.getEntitiesTotalCounts();
    CS.forEach(oResponse, function (oEntityData, sEntityName) {
      if (oEntityData) {
        let aEntityList = oEntityData.list || [];
        _setOrAppendEntityList(sEntityName, aEntityList, bLoadMore);
        oTotalCounts[sEntityName] = oEntityData.totalCount || 0;
      } else {
        if (!bLoadMore) {
          oTotalCounts[sEntityName] = 0;
        }
      }
    });
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
    return true;
  };

  var appendOrSetPropertyCollectionList = function (_aPropertyCollectionList, bAppend) {
    var aPropertyCollectionList = ContentScreenProps.screen.getPropertyCollectionsList();
    if (!bAppend) {
      aPropertyCollectionList = [];
    }
    aPropertyCollectionList = aPropertyCollectionList.concat(_aPropertyCollectionList);
    ContentScreenProps.screen.setPropertyCollectionsList(aPropertyCollectionList);
  };

  var failureFetchEntitiesList = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureFetchEntitiesList", getTranslation());
    return false;
  };

  var _setOrAppendEntityList = function (sEntityName, aEntityList, bAppend) {
    var oAppData = ContentUtils.getAppData();
    switch (sEntityName) {
      case ConfigDataEntitiesDictionary.ATTRIBUTES:
        oAppData.setAttributeList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.TAGS:
        oAppData.setTagList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.ROLES:
        oAppData.setRoleList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.PROPERTY_COLLECTIONS:
        appendOrSetPropertyCollectionList(aEntityList, bAppend);
        break;
    }
  };

  var _fetchTag = function (sTagId, sContext, oFilterContext) {
    var oParameters = {};
    oParameters.id = sTagId;
    oParameters.mode = "all";

    CS.getRequest(ContentScreenRequestMapping.GetAllTags, oParameters, successFetchTagDetailsCallback.bind(this, {
      context: sContext,
      filterContext: oFilterContext
    }), failureFetchTagDetailsCallback);
  };

  var successFetchTagDetailsCallback = function (oCallBackData, oResponse) {
    var oTag = oResponse.success;
    var oLoadedTags = ContentScreenProps.screen.getLoadedTags();
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallBackData.filterContext)
    if (oTag && oTag.id) {
      oLoadedTags[oTag.id] = oTag;
      if (oCallBackData.context == "advancedSearch") {
        var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
        var oObjToPush = _getTagObjectToPushIntoFilterProps(oTag.id, oTag, oCallBackData.filterContext);
        aAppliedFilters.push(oObjToPush);
        var oAppliedFilterCollapseStatusMap = ContentUtils.getFilterProps(oCallBackData.filterContext).getAppliedFilterCollapseStatusMap();
        oAppliedFilterCollapseStatusMap[oTag.id] = {isCollapsed: false};

      }
      _triggerChange();
    }
  };

  var failureFetchTagDetailsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureFetchTagDetailsCallback", getTranslation());
  };

  var _fetchAttribute = function (sAttributeId, sContext, oFilterContext) {
    var oParameters = {};
    oParameters.id = sAttributeId;

    CS.getRequest(ContentScreenRequestMapping.GetAttribute, oParameters, successFetchAttributeDetailsCallback.bind(this, {
      context: sContext,
      filterContext: oFilterContext
    }), failureFetchAttributeDetailsCallback);
  };

  var successFetchAttributeDetailsCallback = function (oCallBackData, oResponse) {
    var oAttribute = oResponse.success;
    var oLoadedAttributes = ContentScreenProps.screen.getLoadedAttributes();
    let oFilterStore = FilterStoreFactory.getFilterStore(oCallBackData.filterContext);
    if (oAttribute && oAttribute.id) {
      oLoadedAttributes[oAttribute.id] = oAttribute;
      if (oCallBackData.context == "advancedSearch") {
        var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
        var oObjToPush = _getAttributeObjectToPushIntoFilterProps(oAttribute.id, oAttribute, oCallBackData.filterContext);
        var oFound = CS.find(aAppliedFilters, {id: oObjToPush.id});
        if (oFound) {
          oFound.children = oFound.children.concat(oObjToPush.children);
          oFound.label = oObjToPush.label;
          oFound.isCollapsed = false;
        } else {
          aAppliedFilters.push(oObjToPush);
        }
      }
      _triggerChange();
    }
  };

  var failureFetchAttributeDetailsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureFetchAttributeDetailsCallback", getTranslation());
  };

  var _fetchUserList = function (oCallbackData) {
    trackMe('_fetchUserList');
    var oCallback = {};
    oCallback.functionToExecute = oCallbackData.functionToExecute;

    return CS.getRequest(getRequestMapping().GetAllUsers, {}, successFetchUserListCallBack.bind(this, oCallback), failureFetchUserListCallBack);
  };

  var _setSelectedContextProps = function (sContext, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, sFillerID) {
    trackMe('_setSelectedContextProps');
    return ContentUtils.setSelectedContextProps(sContext, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, sFillerID);
  };

  var _updateTagDataWhichIsNotInSections = function (oContent) {
    var aContentTags = oContent.tags;
    var oReferencedTagMap = ContentScreenProps.screen.getReferencedTags();
    var aReferencedLifeCycleStatusTags = ContentScreenProps.screen.getReferencedLifeCycleTags();
    var aReferencedStatusTags = ContentScreenProps.screen.getReferencedStatusTags();
    var aAllTags = aReferencedLifeCycleStatusTags.concat(aReferencedStatusTags);
    CS.forEach(aAllTags, function (sTagId) {
      var oFoundTagInstance = CS.find(aContentTags, {tagId: sTagId});
      if (!oFoundTagInstance) {
        var oMasterTag = oReferencedTagMap[sTagId];
        if (!CS.isEmpty(oMasterTag)) {
          ContentUtils.addTagInEntity(oContent, {}, oMasterTag);
        } else {
          ExceptionLogger.error("'oMasterTag' is empty in: _updateTagDataWhichIsNotInSections");
        }
      }
    });

  };

  var _updateImageCoverflowRelatedData = function (oContent) {
    //Adding ADM for assets
    oContent.addedAssets = [];
    oContent.deletedAssets = [];
    oContent = oContent.contentClone ? oContent.contentClone : oContent;
    let aAllowedBaseTypes = [BaseTypesDictionary.assetBaseType];

    if (CS.includes(aAllowedBaseTypes, oContent.baseType) && !CS.isNull(oContent.assetInformation)) {
      ImageCoverflowStore.setImageCoverflowActiveIndex(0);
    }
  };

  var _emptyContentVariantVersionProps = function () {
    ContentScreenProps.contentDetailsView.emptyContentVariantVersionProps();
  };

  var _getAttributeObjectToPushIntoFilterProps = function (sAttributeId, oMasterAttribute, oFilterContext) {

    var aMasterAttributeList = ContentUtils.getAppData().getAttributeList();
    oMasterAttribute = oMasterAttribute || CS.find(aMasterAttributeList, {id: sAttributeId});

    var sDefaultUnit = oMasterAttribute.defaultUnit || "";
    var sVisualType = ContentUtils.getAttributeTypeForVisual(oMasterAttribute.type);

    var sType = "contains";
    var sValue = "";
    var iFrom = "";
    var iTo = "";
    if (sVisualType == "date") {
      sType = "exact";
    }

    if (oMasterAttribute.type !== AttributesTypeDictionary.TEMPERATURE && (sVisualType == "measurementMetrics" || sVisualType == "number" || sVisualType == "calculated")) {
      sValue = 0;
      iFrom = 0;
      iTo = 0;
    }

    var oAppliedFilterCollapseStatusMap = ContentUtils.getFilterProps(oFilterContext).getAppliedFilterCollapseStatusMap();
    oAppliedFilterCollapseStatusMap[sAttributeId] = {isCollapsed: false};

    return {
      id: sAttributeId,
      label: CS.getLabelOrCode(oMasterAttribute),
      isCollapsed: false,
      type: oMasterAttribute.type,
      advancedSearchFilter: true,
      iconKey: oMasterAttribute.iconKey,
      defaultUnit: oMasterAttribute.defaultUnit,
      children: [
        {
          id: UniqueIdentifierGenerator.generateUUID(),
          type: sType,
          baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel",
          label: "",
          value: sValue,
          defaultUnit: sDefaultUnit,
          unitToShow: MeasurementMetricBaseTypeDictionary[oMasterAttribute.type],
          from: iFrom,
          to: iTo,
          advancedSearchFilter: true,
          disableNumberLocaleFormatting: oMasterAttribute.hideSeparator
        }
      ],
    };
  };

  var _getRolesObjectToPushIntoFilterProps = function (sRoleId) {

    var aMasterRoleList = ContentUtils.getAppData().getRoleList();
    var oMasterRole = CS.find(aMasterRoleList, {id: sRoleId});

    return {
      id: sRoleId,
      label: oMasterRole.label,
      users: [],
      isCollapsed: false,
      baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueRoleModel",
      advancedSearchFilter: true
    }
  };

  var _getTagDummyInstancesForFilters = function (aLeafNodes, oMasterTag) {

    var sTagType = oMasterTag.tagType;
    var aRes = [];
    var iMin = 0;
    var iMax = 0;
    var bIsRange = sTagType == TagTypeConstants['RANGE_TAG_TYPE'] || sTagType == TagTypeConstants['CUSTOM_TAG_TYPE'];

    if (bIsRange) {
      iMin = ContentUtils.getMinValueFromListByPropertyName(oMasterTag.tagValues, 'relevance');
      iMax = ContentUtils.getMaxValueFromListByPropertyName(oMasterTag.tagValues, 'relevance');
    }

    CS.forEach(aLeafNodes, function (oNode) {

      if (bIsRange) {

        aRes.push({
          id: oNode.id,
          label: oNode.label,
          type: "range",
          min: iMin,
          max: iMax,
          from: 0,
          to: iMax,
          baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel",
          advancedSearchFilter: true
        })
      } else {
        aRes.push({
          id: oNode.id,
          label: sTagType === TagTypeConstants.TAG_TYPE_BOOLEAN ? oMasterTag.label : oNode.label,
          type: "range",
          from: 0,
          to: 0,
          baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel",
          advancedSearchFilter: true
        })
      }
    });

    return aRes;
  };

  var _getTagObjectToPushIntoFilterProps = function (sTagId, oMasterTag, oFilterContext) {

    oMasterTag = oMasterTag || ContentUtils.getMasterTagById(sTagId);
    var aLeafNodesFlattened = ContentUtils.getLeafNodeSubbedWithParentInfo(oMasterTag.children, '', 'children');

    var oAppliedFilterCollapseStatusMap = ContentUtils.getFilterProps(oFilterContext).getAppliedFilterCollapseStatusMap();
    oAppliedFilterCollapseStatusMap[sTagId] = {isCollapsed: false};

    return {
      id: sTagId,
      label: CS.getLabelOrCode(oMasterTag),
      isCollapsed: false,
      type: oMasterTag.tagType || oMasterTag.type,
      children: _getTagDummyInstancesForFilters(aLeafNodesFlattened, oMasterTag),
      advancedSearchFilter: true,
      iconKey: oMasterTag.iconKey
    }
  };

  var _makeActiveEntityDirty = function () {
    return ContentUtils.makeActiveContentDirty();
  };

  var _getActiveEntity = function () {
    return ContentUtils.getActiveEntity();
  };

  var _calculateNewZoom = function (sKey, iCurrentZoom) {
    var iMinAllowedZoom = ContentUtils.getMinAllowedZoom();
    var iMaxAllowedZoom = ContentUtils.getMaxAllowedZoom();
    var iNewZoom = false;
    var bSetZoom = false;
    if (sKey == "zoomin") {
      iNewZoom = iCurrentZoom + 1;
      bSetZoom = (iNewZoom <= iMaxAllowedZoom)
    } else if (sKey == "zoomout") {
      iNewZoom = iCurrentZoom - 1;
      bSetZoom = (iNewZoom >= iMinAllowedZoom)
    }
    return bSetZoom ? iNewZoom : false;
  };

  var _handleZoomToolClicked = function (sKey, sContext, sElementId) {
    var sProps = ContentUtils.getScreenProps();
    var bIsTaxonomyHierarchySelected = sProps.getIsTaxonomyHierarchySelected();
    var bIsCollectionHierarchySelected = sProps.getIsCollectionHierarchySelected();
    var bAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus() || bIsTaxonomyHierarchySelected || bIsCollectionHierarchySelected;
    var iCurrentZoom = bAvailableEntityViewStatus ? sProps.getSectionInnerZoom() : sProps.getCurrentZoom();

    var iPaginationLimit = 0;
    var iNewZoom = null;
    if (CS.startsWith(sContext, 'relationship')) {
      var oRelationshipProps = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
      if (oRelationshipProps) {
        iCurrentZoom = oRelationshipProps[sElementId].currentZoom;
        iNewZoom = _calculateNewZoom(sKey, iCurrentZoom);
        if (iNewZoom) {
          oRelationshipProps[sElementId].currentZoom = iNewZoom;
          iPaginationLimit = ContentUtils.getPaginationLimitAccordingToZoomLevel(iNewZoom);
          oRelationshipProps[sElementId].paginationLimit = iPaginationLimit;
        }
      }
    } else {
      iNewZoom = _calculateNewZoom(sKey, iCurrentZoom);
      if (iNewZoom) {
        iPaginationLimit = ContentUtils.getPaginationLimitAccordingToZoomLevel(iNewZoom);
        if (bAvailableEntityViewStatus) {
          sProps.setSectionInnerZoom(iNewZoom);
          sProps.setPaginationInnerLimit(iPaginationLimit);
        } else {
          sProps.setCurrentZoom(iNewZoom);
          sProps.setPaginationLimit(iPaginationLimit);
        }
      }
    }
  };

  var _handleSetSectionClicked = function (sActiveSetId, sContext, sRelationshipSectionId, oRelationshipSide) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oAppData = ContentUtils.getAppData();
    oAppData.emptyAvailableEntities();
    var oSelectionStatus = oComponentProps.screen.getSetSectionSelectionStatus();

    oComponentProps.screen.emptySetSectionSelectionStatus();
    oComponentProps.availableEntityViewProps.setSelectedEntities([]);

    if (sActiveSetId != "-1") {
      oSelectionStatus[sContext] = true;
      if (sContext == "relationshipContainerSelectionStatus") {
        var oTempRelationshipObject = {};
        oTempRelationshipObject.id = oRelationshipSide.relationshipMappingId;
        oTempRelationshipObject.relationshipId = sRelationshipSectionId;
        oTempRelationshipObject.klassId = oRelationshipSide.klassId;
        oTempRelationshipObject.cardinality = oRelationshipSide.cardinality;
        oSelectionStatus["selectedRelationship"] = oTempRelationshipObject;
      }
      AvailableEntityStore.handleSectionClicked(sActiveSetId, sContext, oSelectionStatus, sRelationshipSectionId, oRelationshipSide);

    }
  };

  let _handleMetricUnitChanged = function (sSelectedUnit, oSectionElementDetails, sAttrId, sValueId, sRangeType, oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let aAppliedFilters = oFilterStore.getAppliedFiltersClone();
    let oAppliedFilter = CS.find(aAppliedFilters, {id: sAttrId});
    let oChild = CS.find(oAppliedFilter.children, {id: sValueId});
    if(CS.isNotEmpty(sRangeType)) {
      oChild.defaultUnit[sRangeType] = sSelectedUnit;
    }
    else {
      oChild.defaultUnit = sSelectedUnit;
    }
    _triggerChange();
  };

  var _resetCurrentZoomIfNotValid = function () {
    var sProps = ContentUtils.getScreenProps();
    var iCurrentZoom = sProps.getCurrentZoom();
    var iMinAllowedZoom = ContentUtils.getMinAllowedZoom();
    var iPaginationLimit;

    if (iCurrentZoom < iMinAllowedZoom) {
      sProps.setCurrentZoom(iMinAllowedZoom);
      iPaginationLimit = ContentUtils.getPaginationLimitAccordingToZoomLevel(iCurrentZoom);
      sProps.setPaginationLimit(iPaginationLimit);
    }

    var oRelationshipProps = ContentScreenProps.relationshipView;
    var oRelationshipToolbarProps = oRelationshipProps.getRelationshipToolbarProps();
    CS.forEach(oRelationshipToolbarProps, function (oToolbarProp) {
      if (oToolbarProp.currentZoom < iMinAllowedZoom) {
        oToolbarProp.currentZoom = iMinAllowedZoom;
      }
    });
  };

  var _cancelVariantCreation = function (bDoNotTrigger) {
    ContentScreenProps.variantSectionViewProps.setDummyLinkedVariant({});
    ContentScreenProps.variantSectionViewProps.setDummyVariant({});
    if (!bDoNotTrigger) {
      _triggerChange();
    }
  };

  var _variantContextSelectionCheck = function () {
    //proceed to save variant if true is returned
    var oActiveEntity = ContentUtils.getActiveEntity();
    var oActiveEntityClone = oActiveEntity.contentClone;
    var oActiveVariantContext = ContentScreenProps.variantSectionViewProps.getSelectedContext();
    return ContentUtils.validateVariantContextSelection(oActiveEntityClone, oActiveVariantContext);
  };

  var _handleNotificationConfirmation = function (sKey, oInnerElement, sButtonKey) {
    if (sButtonKey == "ok") {
      if (oInnerElement.baseType == BaseTypesDictionary['tagInstanceBaseType']) {
        var aTagValues = oInnerElement.notification[sKey];
        CS.forEach(aTagValues, function (oTagValue) {
          var oOldTagValue = CS.find(oInnerElement['tagValues'], {tagId: oTagValue.tagId});
          if (CS.isEmpty(oOldTagValue)) {
            oInnerElement['tagValues'].push(ContentUtils.getNewTagValue(oTagValue.tagId, oTagValue.relevance));
          } else {
            oOldTagValue.relevance = oTagValue.relevance;
          }
        });
      } else {
        oInnerElement["value"] = oInnerElement.notification[sKey];
      }
      _updateMandatoryElementsStatus(ContentUtils.getActiveEntity()); //check for mandatory elements after notification confirmation
    } else {
      ContentUtils.resetToUpdateAllSCU();
    }
    oInnerElement.notification = {};
    oInnerElement.isValueChanged = true;
    _triggerChange();
  };

  var _getIfTabbableSectionElement = function (oElement) {

    if (oElement.type == "attribute") {

      var oMaster = oElement.attribute;

      if (!oElement.isDisabled
          && oMaster
          && (ContentUtils.isAttributeTypeHtml(oMaster.type)
              || ContentUtils.isAttributeTypeName(oMaster.type)
              || ContentUtils.isAttributeTypeText(oMaster.type)
              || ContentUtils.isAttributeTypeMeasurement(oMaster.type)
              || ContentUtils.isAttributeTypeTelephone(oMaster.type)
              || ContentUtils.isAttributeTypeNumber(oMaster.type))) {
        return true;
      }
    }

    return false;
  };

  var _getFirstSectionElementFromSection = function (oSection) {
    var oFoundEl = null;

    if (oSection) {
      var aElements = oSection.elements;
      var iCols = oSection.columns;
      var iFoundSum = Infinity;

      CS.forEach(aElements, function (oEl) {
        var iElSum = (oEl.position.x * iCols) + oEl.position.y;

        if (iElSum < iFoundSum && _getIfTabbableSectionElement(oEl)) {
          oFoundEl = oEl;
          iFoundSum = iElSum;
        }
      })
    }

    return oFoundEl;
  };

  var _getLastSectionElementFromSection = function (oSection) {
    var oFoundEl = null;

    if (oSection) {
      var aElements = oSection.elements;
      var iCols = oSection.columns;
      var iFoundSum = -Infinity;

      CS.forEach(aElements, function (oEl) {
        var iElSum = (oEl.position.x * iCols) + oEl.position.y;

        if (iElSum > iFoundSum && _getIfTabbableSectionElement(oEl)) {
          oFoundEl = oEl;
          iFoundSum = iElSum;
        }
      })
    }

    return oFoundEl;
  };

  var _handleThumbnailDeleteIconClicked = function (oContent) {
    trackMe('handleThumbnailDeleteIconClicked');

    var sContentNamesString = ContentUtils.getContentName(oContent);
    let oSelectedModule = GlobalStore.getSelectedModule();
    let sAlertMessage = (oSelectedModule.id === ModuleDictionary.FILES) ? getTranslation().STORE_ALERT_CONFIRM_DELETE_FILE
                                                                        : getTranslation().STORE_ALERT_CONFIRM_DELETE;
    //Are you sure you want to delete following Content?
    ContentUtils.confirmation(sContentNamesString,
        sAlertMessage,
        _deleteEntity.bind(this, oContent),
        {},
        getTranslation().OK,
        getTranslation().CANCEL
    );
  };

  var _deleteEntity = function (oContent) {
    trackMe('deleteEntity');
    _deleteSingleArticle(oContent);
  };

  var _deleteSingleArticle = function (oContent) {
    trackMe('_deleteSingleContent');
    var aSelectedContentIds = ContentUtils.getSelectedContentIds();

    CS.remove(aSelectedContentIds, function (sId) {
      return sId == oContent.id;
    });
    // }

    var aArticleIdsToDelete = [oContent.id];
    _deleteArticles(aArticleIdsToDelete);

  };

  var _makeDataToDeleteInstances = function (aArticleIds) {
    var aEntityList = ContentUtils.getAppData().getContentList();
    var oSelectedEntities = {};
    let bIsArchive = ContentUtils.getIsArchive();

    CS.forEach(aArticleIds, function (sId) {
      var oEntity = CS.find(aEntityList, {id: sId});
      if (oEntity) {
        if (!oSelectedEntities[oEntity.baseType]) {
          oSelectedEntities[oEntity.baseType] = [];
        }
        oSelectedEntities[oEntity.baseType].push(sId);
      }
    });

    var aEntitiesToDelete = CS.map(oSelectedEntities, function (aIds, sBaseType) {
      return {
        baseType: sBaseType,
        ids: aIds
      }
    });

    if (bIsArchive) {
      return {
        deleteRequest: aEntitiesToDelete,
        isDeleteFromArchivalPortal: true
      }
    }
    return {
      deleteRequest: aEntitiesToDelete
    }

  };

  var _deleteMultipleInstances = function (aArticleIds, oCallbackData) {
    let aEntityIdsToBeDeleted = [];
    CS.forEach(aArticleIds, function (oArticle) {
      aEntityIdsToBeDeleted.push(oArticle.id);
    });

    _deleteArticles(aEntityIdsToBeDeleted, oCallbackData);
  };

  var _deleteArticles = function (aArticleIds, oCallbackData) {
    var oPostDataForBulkDelete = _makeDataToDeleteInstances(aArticleIds);
    var sUrl = getRequestMapping().DeleteInstances;
    if (ContentUtils.getSelectedModule().id === ModuleDictionary.MAM) {
      sUrl = getRequestMapping().DeleteAssetInstances;
    }
    if (ContentUtils.getSelectedModule().id === ModuleDictionary.FILES) {
      sUrl = getRequestMapping().Deleteimportedfile;
      CS.deleteRequest(sUrl, {}, aArticleIds, successDeleteSelectedFilesCallback, failureDeleteSelectedFilesCallback);
    } else {
      CS.deleteRequest(sUrl, {}, oPostDataForBulkDelete, successDeleteSelectedArticlesCallback.bind(this, oCallbackData), failureDeleteSelectedContentsCallback.bind(this, oCallbackData));
    }
  };

  var _deleteSelectedArticleTiles = function (oCallbackData) {
    var aSelectedArticles = ContentUtils.getSelectedContentList();
    _deleteSelectedEntities(aSelectedArticles, oCallbackData);
  };

  var _deleteSelectedEntities = function (aSelectedArticles, oCallbackData) {
    var aArticleNames = [];
    var aArticleIds = [];

    var aNamesCanNotBeDeleted = [];

    if (CS.isEmpty(aSelectedArticles)) {
      ContentUtils.showMessage(getTranslation().NOTHING_IS_SELECTED_TO_DELETE);
      return;
    }

    CS.forEach(aSelectedArticles, function (oArticle) {
      aArticleNames.push(oArticle.name);
      aArticleIds.push(oArticle.id);
    });

    if (aNamesCanNotBeDeleted.length) {
      var sContentNames = aNamesCanNotBeDeleted.join(', ');
      ContentUtils.showError(getTranslation().USER_NOT_HAVE_DELETE_PERMISSION + " [ " + sContentNames + " ]");
    }

    if (!CS.isEmpty(aArticleIds)) {
      let oSelectedModule = GlobalStore.getSelectedModule();
      let sAlertMessage = (oSelectedModule.id === ModuleDictionary.FILES) ? getTranslation().STORE_ALERT_CONFIRM_DELETE_FILE
                                                                          : getTranslation().STORE_ALERT_CONFIRM_DELETE;
      //Are you sure you want to delete following Content?
      ContentUtils.listModeConfirmation(sAlertMessage,
          aArticleNames,
          _deleteMultipleInstances.bind(this, aSelectedArticles, oCallbackData),
          {},
          getTranslation().OK,
          getTranslation().CANCEL
      );
    }
  };

  var _selectAllArticleChildren = function () {

    var aContentList = _getMasterEntityList();
    var bIsSelectAll;
    bIsSelectAll = ArticleProps.getSelectAllChildren();
    var aChildren = [];

    if (!bIsSelectAll) {
      aChildren = aContentList;
    }
    ArticleProps.setSelectAllChildren(!bIsSelectAll);
    ContentUtils.setSelectedEntityList(aChildren);
  };

  var _handleClassVisibilityChanged = function (sId) {
    var oSectionViewProps = ContentScreenProps.contentSectionViewProps;

    if (oSectionViewProps.getVisibleClassId() == sId) {
      //de-select if already selected
      oSectionViewProps.setVisibleClassId("");
    } else {
      oSectionViewProps.setVisibleClassId(sId);
    }
    var sVisibleClassId = oSectionViewProps.getVisibleClassId();

    var oReferencedClasses = ContentScreenProps.screen.getReferencedClasses();
    var oSectionVisualProps = oSectionViewProps.getSectionVisualProps();

    if (sVisibleClassId) {
      //first make all sections hidden :
      CS.forEach(oSectionVisualProps, function (oVisualProps) {
        oVisualProps.isHidden = true;
      });
      //then only make the sections of the selected class visible :
      var oClass = oReferencedClasses[sVisibleClassId];
      CS.forEach(oClass.propertyCollections, function (oPropertyCollectionValue) {
        oSectionVisualProps[oPropertyCollectionValue.id].isHidden = false;
      });
    } else {
      // show ALL sections :
      CS.forEach(oSectionVisualProps, function (oVisualProps) {
        oVisualProps.isHidden = false;
      });
    }

    ContentUtils.resetToUpdateAllSCU();
    _triggerChange();
  };

  var _fetchVariant = function (oModel) {
    let oVariantInfo = {
      variantInstanceId: oModel.id
    };
    _fetchArticleById('', {}, oVariantInfo);
  };

  let _handleFilterButtonClicked = function (oCallbackData) {
    /**
     * Required to make dynamic collection dirty when applying filters.
     */
    oCallbackData = oCallbackData || {};
    let oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();
    if (!CS.isEmpty(oActiveCollection) && oActiveCollection.type == "dynamicCollection"
        && !oCallbackData.isFromFilterHierarchy) {
      oActiveCollection.isDirty = true;
      oActiveCollection.isBookmarkFilterDirty = true;
    }
    return _processDataForFetchContentList(oCallbackData, {});
  };

  var _makeDefaultContentImage = function (sActiveAssetId) {
    var oActiveEntity = ContentUtils.makeActiveEntityDirty();
    if (oActiveEntity.defaultAssetInstanceId == sActiveAssetId) {
      oActiveEntity.defaultAssetInstanceId = "";
    } else {
      oActiveEntity.defaultAssetInstanceId = sActiveAssetId;
    }
  };

  var _handleCommentChanged = function (sNewValue) {
    if (ContentUtils.getIsStaticCollectionScreen() || ContentUtils.getIsDynamicCollectionScreen()) {
      NewCollectionStore.handleCollectionCommentChanged(sNewValue);
    } else {
      var oActiveEntity = ContentUtils.makeActiveEntityDirty();
      oActiveEntity.saveComment = sNewValue;
    }

  };

  var _handleXRayPropertyClicked = function (aProperties, sRelationshipId, sRelationshipIdToFetchData, oFilterContext) {

    //check if empty active group:
    //if yes, create new active group with createNew: true
    //else, set isDirty: true and ADD/REMOVE to it

    var oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup(sRelationshipId);
    /**
     * Checking if any property is updated in x-ray and deselecting active group & setting activegroup to empty
     * Creating new empty group with updated data
     */
    let bIsSameXRayPropertiesAvailable = CS.isEqual(aProperties, oActiveXRayPropertyGroup.properties);
    if (!bIsSameXRayPropertiesAvailable) {
      if (sRelationshipId) {
        ContentScreenProps.relationshipView.addNewRelationshipToolbarPropById(sRelationshipId)
      } else {
        ContentUtils.setActiveXRayPropertyGroup({})
      }
      oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup(sRelationshipId)
    }


    if (CS.isEmpty(oActiveXRayPropertyGroup) || oActiveXRayPropertyGroup.createNew) {
      var oNewXRayPropertyGroup = {};
      oNewXRayPropertyGroup.label = "";
      oNewXRayPropertyGroup.properties = !CS.isEmpty(aProperties) ? CS.xorBy(oNewXRayPropertyGroup.properties, aProperties, "id") : aProperties;
      oNewXRayPropertyGroup.unappliedChanges = true;
      oNewXRayPropertyGroup.createNew = true;
      oNewXRayPropertyGroup.id = "";
      ContentUtils.setActiveXRayPropertyGroup(oNewXRayPropertyGroup, sRelationshipId);
    }
    else {
      oActiveXRayPropertyGroup.properties = aProperties;
      oActiveXRayPropertyGroup.unappliedChanges = true;
    }
    _handleXRayApplyButtonClicked(sRelationshipId, sRelationshipIdToFetchData, oFilterContext);
    _triggerChange();
  };

  var _handleShowXRayPropertyGroupsClicked = function (bMakeDefaultSelected, bIsNotForXRay, oFilterContext, sRelationshipId, sRelationshipIdToFetchData) {
    return _getAllXRayPropertyGroups(bMakeDefaultSelected, bIsNotForXRay, oFilterContext, sRelationshipId, sRelationshipIdToFetchData);
  };

  var _getAllXRayPropertyGroups = function (bMakeDefaultSelected, bIsNotForXRay, oFilterContext, sRelationshipId, sRelationshipIdToFetchData) {

    var oParameters = {};
    oParameters.isForXRay = true;
    if (bIsNotForXRay) {
      oParameters.isForXRay = false;
    }
    ContentScreenProps.screen.setEntitySearchText("");
    var oPaginationData = ContentScreenProps.screen.getEntitiesPaginationData().propertyCollections;
    let oPostData = {
      searchText: "",
      from: oPaginationData.from,
      size: oPaginationData.size,
      searchColumn: oPaginationData.sortBy,
      sortOrder: oPaginationData.sortOrder,
      sortBy: oPaginationData.sortBy,
    };

    //TODO - is this needed? (works fine without it) - siddhant
    let oCallBack = {filterContext: oFilterContext, makeDefaultSelected: bMakeDefaultSelected};
    return CS.postRequest(getRequestMapping().GetAllPropertyCollections, oParameters, oPostData, successGetAllXRayPropertyGroupsCallback.bind(this, oCallBack, sRelationshipId, sRelationshipIdToFetchData), failureGetAllXRayPropertyGroupsCallback.bind(this, oCallBack));
  };

  var successGetAllXRayPropertyGroupsCallback = function (oCallback, sRelationshipId, sRelationshipIdToFetchData, oResponse) {
    var oResponseData = oResponse.success;
    let aResponseList = CS.sortBy(oResponseData.list, function (oProperty) {
      return CS.getLabelOrCode(oProperty).toLowerCase();
    });

    ContentScreenProps.screen.setXRayPropertyGroupList(aResponseList);

    let oActiveXRayPropertyGroup = ContentScreenProps.screen.getActiveXRayPropertyGroup();
    if (oCallback.makeDefaultSelected) {
      var oDefaultForXRay = CS.find(aResponseList, {isDefaultForXRay: true}) || CS.head(aResponseList);
      if (!CS.isEmpty(oActiveXRayPropertyGroup) && CS.find(aResponseList, {id: oActiveXRayPropertyGroup.id})) {
        oDefaultForXRay = oActiveXRayPropertyGroup;
      }
      if (oDefaultForXRay) {
        _getXRayPropertyGroup(oDefaultForXRay.id, sRelationshipId, sRelationshipIdToFetchData, oCallback.filterContext);
      } else {
        /**if there is no x-ray property collection, then fetch content. Earlier, fetchContent call was not being sent and so on first reload 'no content found' screen was shown*/
        //todo: need a better way to handle this situation.
        _fetchContentList(oCallback, {});
      }
    }
    /**Need to check active Xray collection is available in backend*/
    else if (CS.isNotEmpty(oActiveXRayPropertyGroup) && !oActiveXRayPropertyGroup.createNew && !CS.find(aResponseList, {id: oActiveXRayPropertyGroup.id})) {
      let oExtraData = {
        dontShowException: true
      };
      _getXRayPropertyGroup(oActiveXRayPropertyGroup.id, sRelationshipId, sRelationshipIdToFetchData, oCallback.filterContext, oExtraData);
    }
    _triggerChange();

    return true;
  };

  var failureGetAllXRayPropertyGroupsCallback = function (oCallBack, oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureGetAllXRayPropertyGroupsCallback', getTranslation());
    return false;
  };

  var _handleXRayPropertyGroupClicked = function (sId, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
    if (!ContentUtils.isXRayVisionModeActive()) {
      ContentUtils.setThumbnailModeToXRayVisionMode(sRelationshipId);
    }
    _getXRayPropertyGroup(sId, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext);
  };

  var _getXRayPropertyGroup = function (sId, sRelationshipId, sRelationshipIdToFetchData, oFilterContext, oExtraData = {}) {

    var oParameters = {};
    oParameters.id = sId;
    let oCallback = {
      filterContext: oFilterContext,
      relationshipId: sRelationshipId,
      relationshipIdToFetchData: sRelationshipIdToFetchData,
      dontShowException: oExtraData.dontShowException,
    };
    return CS.getRequest(getRequestMapping().GetPropertyCollection, oParameters, successGetXRayPropertyGroupCallback.bind(this, oCallback), failureGetXRayPropertyGroupCallback.bind(this, oCallback));
  };

  var successGetXRayPropertyGroupCallback = function (oCallback, oResponse) {
    var oResponseData = oResponse.success;
    var oActiveXRayPropertyGroup = processXRayPropertyGroupAfterGet(oResponseData);

    ContentUtils.setActiveXRayPropertyGroup(oActiveXRayPropertyGroup, oCallback.relationshipId);
    _applyXRaySelections(oCallback, oCallback.relationshipId, oCallback.relationshipIdToFetchData);

    return true;
  };

  var failureGetXRayPropertyGroupCallback = function (oCallback, oResponse) {
    let oActiveXRayPropertyGroup = ContentScreenProps.screen.getActiveXRayPropertyGroup();
    if (CS.isNotEmpty(oActiveXRayPropertyGroup) && oCallback.dontShowException) {
      oActiveXRayPropertyGroup.label = "";
      oActiveXRayPropertyGroup.createNew = true;
      _triggerChange();
      return;
    }
    ContentUtils.failureCallback(oResponse, 'failureGetXRayPropertyGroupCallback', getTranslation());
    _getAllXRayPropertyGroups(false, false, oCallback.filterContext);
  };

  var processXRayPropertyGroupAfterGet = function (oXRayPropertyGroup) {
    var oProcessedXRayPropertyGroup = {};
    oProcessedXRayPropertyGroup.id = oXRayPropertyGroup.id;
    oProcessedXRayPropertyGroup.label = oXRayPropertyGroup.label;
    oProcessedXRayPropertyGroup.isDefaultForXRay = oXRayPropertyGroup.isDefaultForXRay;
    oProcessedXRayPropertyGroup.code = oXRayPropertyGroup.code;
    let aReferencedAttributes = oXRayPropertyGroup.referencedAttributes;
    let aReferencedTags = oXRayPropertyGroup.referencedTags;

    /**Converting array to map of referenceAttribute and referenced tags**/
    let oReferencedAttributes = CS.reduce(aReferencedAttributes, function (oMap, oReferencedAttribute) {
      oMap[oReferencedAttribute.id] = oReferencedAttribute;
      return oMap;
    }, {});
    let oReferencedTags = CS.reduce(aReferencedTags, function (oMap, oReferencedTag) {
      oMap[oReferencedTag.id] = oReferencedTag;
      return oMap;
    }, {});

    let oOldReferencedAttribute = ContentScreenProps.screen.getReferencedAttributes();
    let oOldReferencedTags = ContentScreenProps.screen.getReferencedTags();
    CS.assign(oOldReferencedAttribute, oReferencedAttributes);
    CS.assign(oOldReferencedTags, oReferencedTags);
    oProcessedXRayPropertyGroup.properties = CS.map(oXRayPropertyGroup.elements, function (oElement) {
      var oReferencedElement = (oElement.type === "tag" ? CS.find(aReferencedTags, {id: oElement.id}) : CS.find(aReferencedAttributes, {id: oElement.id})) || {};
      return (
          {
            id: oElement.id,
            label: oReferencedElement.label || "",
            type: oElement.type
          }
      );
    });
    return oProcessedXRayPropertyGroup;
  };

  var _handleCloseActiveXRayPropertyGroupClicked = function (sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
    var oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup(sRelationshipId);
    if (oActiveXRayPropertyGroup.isDirty) {
      ContentUtils.confirmation(getTranslation().CLOSE_X_RAY_PROPERTY_GROUP_WARNING, '',
          _clearActiveXRayPropertyGroup.bind(this, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext), _triggerChange, getTranslation().OK, getTranslation().CANCEL);
    } else {
      _clearActiveXRayPropertyGroup(sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext);
    }
  };

  var _handleXRayApplyButtonClicked = function (sRelationshipId, sRelationshipIdToFetchData, oFilterContext) {
    var oCallbackData = {};
    if (sRelationshipId) {
      oCallbackData = {
        functionToExecute: function () {
          var oRelationshipProps = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
          oRelationshipProps[sRelationshipId].isXRayEnabled = true;
        }
      };
    }
    else {
      if (ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()) {
        var sSectionInnerThumbnailMode = ContentScreenProps.screen.getSectionInnerThumbnailMode();
        if (sSectionInnerThumbnailMode === ThumbnailModeConstants.BASIC) {
          ContentScreenProps.screen.setSectionInnerThumbnailMode(ThumbnailModeConstants.XRAY);
        }
      } else {
        var sCurrentThumbnailMode = ContentScreenProps.screen.getThumbnailMode();
        if (sCurrentThumbnailMode === ThumbnailModeConstants.BASIC) {
          ContentScreenProps.screen.setThumbnailMode(ThumbnailModeConstants.XRAY);
        }
      }
    }
    oCallbackData.filterContext = oFilterContext;
    _applyXRaySelections(oCallbackData, sRelationshipId, sRelationshipIdToFetchData);
  };

  var _applyXRaySelections = function (oCallbackData, sRelationshipId, sRelationshipIdToFetchData) {
    if (sRelationshipId) {
      return ContentRelationshipStore.fetchRelationship(sRelationshipId, sRelationshipIdToFetchData, oCallbackData);
    } else if (ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()) {
      let oExtraData = {};
      if (CS.isEmpty(oCallbackData.selectedContext)) {
        let sSelectedContext = ContentUtils.getSelectedHierarchyContext();
        if(oCallbackData.filterContext && oCallbackData.filterContext.screenContext === 'quickList'){
          sSelectedContext = ContentUtils.getSelectedScreenContextForQuickList();
        }
        oCallbackData.selectedContext = sSelectedContext;
        oExtraData.selectedContext = sSelectedContext;
      }
      return AvailableEntityStore.fetchAvailableEntities(oCallbackData, oExtraData);
    } else {
      _fetchContentList(oCallbackData, {context: "xray"});
    }
  };

  var _clearActiveXRayPropertyGroup = function (sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
    ContentUtils.setActiveXRayPropertyGroup({}, sRelationshipId);
    var oCallbackData = {
      filterContext: oFilterContext
    };
    _applyXRaySelections(oCallbackData, sRelationshipId, sRelationshipIdForFetchingXRAYData);
    _triggerChange();
  };

  var _handleContentEntityDropInRelationshipSection = function (sCurrentDraggedItemId, oCallbackData) {
    var oComponentProps = ContentUtils.getComponentProps();
    /** in this case oItem will be newly added entity's data*/

    var aSelectedEntities = oComponentProps.availableEntityViewProps.getSelectedEntities();
    var oSelectedSectionStatus = oComponentProps.screen.getSetSectionSelectionStatus();
    if (oSelectedSectionStatus["relationshipContainerSelectionStatus"]) {
      var oRelationshipElement = oSelectedSectionStatus["selectedRelationship"];
      var oRelationshipProps = oComponentProps.relationshipView.getRelationshipToolbarProps();
      var oActiveRelationship = oRelationshipProps[oRelationshipElement.id];
      if (!CS.isEmpty(oActiveRelationship)) {
        var aContentElements = oActiveRelationship.elements;
        var oReferencedElements = oComponentProps.screen.getReferencedElements();
        var oReferencedRelationshipElement = oReferencedElements[oRelationshipElement.id];
        if (oReferencedRelationshipElement.relationshipSide && oReferencedRelationshipElement.relationshipSide.cardinality == RelationshipConstants.ONE) {
          if (aContentElements.length == 1 || aSelectedEntities.length > 1) {
            alertify.error(getTranslation().MORE_THAN_ONE_ELEMENT_NOT_ALLOWED);
            _triggerChange();
            return;
          }
        }
      }

      var sEntityRelationshipsKey = "contentRelationships";
      let bIsNatureRelationship = false;
      var oReferencedNatureRelationshipsElements = oComponentProps.screen.getReferencedNatureRelationships();
      var oCurrentReferencedNatureRelationship = oReferencedNatureRelationshipsElements[oRelationshipElement.relationshipId];
      if (!CS.isEmpty(oCurrentReferencedNatureRelationship)) {
        sEntityRelationshipsKey = "natureRelationships";
        bIsNatureRelationship = true;
        /** To Restrict, addition of entity in relationship when its maxNoOfItems exceeds.  */
        if (ContentUtils.isNatureRelationship(oCurrentReferencedNatureRelationship.relationshipType)) {
          var iMaxNoOfItems = oCurrentReferencedNatureRelationship.maxNoOfItems;
          var iAlreadyPresentElementsCount = oActiveRelationship.elements.length;
          var iSelectedEntitiesCount = (aSelectedEntities.length > 0) ? aSelectedEntities.length : 1;
          if (iMaxNoOfItems != 0 && iMaxNoOfItems < (iAlreadyPresentElementsCount + iSelectedEntitiesCount)) { //iMaxNoOfItems = 0 means can add unlimited elements
            alertify.error(getTranslation().MAX_NUMBER_OF_ITEM_EXCEEDED + getTranslation().MAX_NUMBER_OF_ITEM_ALLOWED + ": (" + iMaxNoOfItems + ")", 0);
            _triggerChange();
            return;
          }
        }
      }
      var oActiveEntity = ContentUtils.getActiveContent();
      var oCurrentRelationship = CS.find(oActiveEntity[sEntityRelationshipsKey], {sideId: oRelationshipElement.id});
      let oNewRelationship = CS.cloneDeep(oCurrentRelationship);

      var oModifiedRelationshipElements = oComponentProps.relationshipView.getModifiedRelationshipElements();
      var aAddedElementsInRelationship = [];
      oModifiedRelationshipElements.id = oRelationshipElement.id;
      oModifiedRelationshipElements.added = aAddedElementsInRelationship;
      if (!CS.isEmpty(oNewRelationship)) {
        oNewRelationship.isDirty = true;
        let aContentElements = oNewRelationship.elementIds;
        var aAvailableEntities = ContentUtils.getAppData().getAvailableEntities();

        aAddedElementsInRelationship.push(CS.find(aAvailableEntities, {id: sCurrentDraggedItemId}));
        aContentElements.push(sCurrentDraggedItemId);

        oComponentProps.availableEntityViewProps.setSelectedEntities([]);
        //ContentStore.saveActiveContent(oCallbackData);
        ContentRelationshipStore.saveRelationship([oCurrentRelationship], [oNewRelationship], bIsNatureRelationship, oCallbackData);
      }
    }
  };

  var _getDayOfWeekStringFromMoment = function (oMoment) {
    oMoment.locale('en');
    var sDay = oMoment.format(DateFormatDictionary.DAY_SHORT_FORM);
    oMoment.locale(false);
    return CS.upperCase(sDay);
  };

  var _updateDayOfWeekFromStartTime = function (oMoment, oDaysOfWeek) {
    if (oDaysOfWeek) {
      var sDayOfWeekString = _getDayOfWeekStringFromMoment(oMoment);
      let oMockDataForEventsView = new MockDataForEventsView();
      var aDaysOfWeek = oMockDataForEventsView.daysOfWeek;
      CS.forEach(aDaysOfWeek, function (oDayData) {
        oDaysOfWeek[oDayData.id] = false;
      });
      oDaysOfWeek[sDayOfWeekString] = true;
    }
  };

  var _updateEndsOnFromEndTime = function (oRepeat, lEndTime) {
    var mEndTime = ContentUtils.getMomentOfDate(lEndTime);
    var sTimeString = null;
    switch (oRepeat.repeatType) {
      case "DAILY":
        sTimeString = "days";
        break;
      case "WEEKLY":
        sTimeString = "weeks";
        break;
      case "MONTHLY":
        sTimeString = "months";
        break;
      case "YEARLY":
        sTimeString = "years";
        break;
    }
    oRepeat.endsOn = mEndTime.valueOf();
  };

  var _handleAssetEventScheduleFieldChanged = function (sKey, sValue, bIsAssetEvent) {
    /** bIsAssetEvent is an optional parameter. should be true only when editing the Asset inbuilt event */
    var oActiveEvent = bIsAssetEvent ? ContentUtils.getActiveEntity() : {};
    oActiveEvent = ContentUtils.makeContentDirty(oActiveEvent);

    var oEventSchedule = oActiveEvent.eventSchedule || {};
    var oRepeat = oEventSchedule.repeat || {};

    switch (sKey) {
      case "startTime":
        oEventSchedule[sKey] = sValue;
        ContentScreenProps.screen.setIsVersionableAttributeValueChanged(true);
        _updateDayOfWeekFromStartTime(ContentUtils.getMomentOfDate(sValue), oRepeat.daysOfWeek);
        break;
      case "endTime":
        oEventSchedule[sKey] = sValue;
        ContentScreenProps.screen.setIsVersionableAttributeValueChanged(true);
        _updateEndsOnFromEndTime(oRepeat, sValue);
        break;
    }
    _triggerChange();
  };

  var _getIsValidFileTypes = function (oFile, sAssetType) {
    var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    var sTypeRegex = aValidTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var successPostImageToAsset = function (sUniqueId, oCallback, oResponse) {
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute(oResponse.success[0].thumbKey);
    }
    _triggerChange();
  };

  var failurePostImageToAsset = function (oCallBack, oResponse) {
    ExceptionLogger.log(oResponse);
    ContentUtils.failureCallback(oResponse, "failurePostIconToAsset", getTranslation());
    _checkForExtraNecessaryServerCalls(oResponse);
  };

  var _postIconToAsset = function (file, oCallback) {
    var sUniqueId = UniqueIdentifierGenerator.generateUUID();
    var data = new FormData();
    data.append('fileUpload', file);
    data.append('fileUpload', sUniqueId);
    var sUrl = getRequestMapping().AssetBulkUpload;
    // TODO: Decide url and parameters
    CS.customPostRequest(RequestMapping.getRequestUrl(sUrl) + "?mode=config&size=20", data,
        successPostImageToAsset.bind(this, sUniqueId, oCallback), failurePostImageToAsset.bind(this, oCallback), false);
  };

  var _uploadIconToAsset = function (aFiles, oCallback) {
    if (aFiles.length) {
      var oFile = aFiles[0];
      if (_getIsValidFileTypes(oFile, "image_asset")) {

        var reader = new FileReader();
        // Closure to capture the file information.
        reader.onload = (function (theFile) {
          return function (event) {
            _postIconToAsset(theFile, oCallback);
          };
        })(oFile);

        reader.readAsDataURL(oFile);
      } else {
        alertify.error(getTranslation().ERROR_INVALID_ASSET);
      }
    } else {
      if (oCallback.functionToExecute) {
        oCallback.functionToExecute("");
      }
    }
  };

  var _handleContentEventImageChanged = function (aFiles, oFilterContext) {
    var oCallback = {
      functionToExecute: function (sValue) {
        _handleAssetEventScheduleFieldChanged("image", sValue)
      },
      filterContext: oFilterContext
    };
    _uploadIconToAsset(aFiles, oCallback);
    _triggerChange();
  };

  var _handleLinkedSectionButtonClicked = function (oItem) {
    let oProperties = oItem.properties;
    switch (oProperties.context) {
    }
  };

  var failureFetchTabSpecificContentsListCallback = function (oCallBackData = {}, oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureSearchedContentListCallBack', getTranslation());
    return false;
  };

  let successFetchTabSpecificContentsListCallback = function (oCallBackData = {}, oResponse) {
    var oAppData = ContentUtils.getAppData();
    var oResponseData = oResponse.success;
    let oReferencedClasses = {};
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var aEntityListFromServer = [];

    /** To get filter context for performing filter related operations **/
    let {filterContext: oFilterContext, paginationData: oPaginationData} = oCallBackData;
    if (oFilterContext.screenContext === ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS) {
      aEntityListFromServer = oResponseData.assetInstances;
      oReferencedClasses = oResponseData.referencedKlasses;
    }

    ContentUtils.addEntityInformationData(aEntityListFromServer);
    oAppData.setAvailableEntities(aEntityListFromServer);
    oScreenProps.setReferencedClasses(CS.assign(oScreenProps.getReferencedClasses(), oReferencedClasses));
    ContentUtils.addDataToReferencedKlasses(oResponseData.referencedKlasses);

    /**
     * Set pagination data
     */
    var iTotalEntityCount = oResponseData.totalContents;
    var iFrom = oResponseData.from;
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    oFilterProps.setTotalItemCount(iTotalEntityCount);
    oFilterProps.setFromValue(iFrom);
    if (CS.isNotEmpty(oPaginationData)) {
      /** on pagination change "oPaginationData" should not be empty and on tab click default pagination data
       will get through pagination props **/
      oFilterProps.setPaginationSize(oPaginationData.pageSize);
    }
    oFilterProps.setCurrentPageItems(aEntityListFromServer.length);

    _triggerChange();
    return true;
  };

  let _fetchTabSpecificContentsList = function (sScreenContext = "", oPaginationData = {}) {
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setActiveSections([]);
    // To set TILE view mode inside duplicateAssets tabs.
    oScreenProps.setViewMode(ContentScreenConstants.viewModes.TILE_MODE);
    let sURL = "";
    let oActiveContent = ContentUtils.getActiveContent();

    if (sScreenContext === ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS) {
      sURL = getRequestMapping().GetDuplicateAssets;
    }

    let oFilterContext = {
      filterType: oFilterPropType.PAGINATION,
      screenContext: sScreenContext
    };
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    let oPostData = {
      from: CS.isEmpty(oPaginationData) ? oFilterProps.getFromValue() : oPaginationData.from,
      size: CS.isEmpty(oPaginationData) ? oFilterProps.getPaginationSize() : oPaginationData.pageSize,
    };
    _modifyBreadCrumbNavigationForVirtualTab(oActiveContent, sScreenContext, oFilterContext);

    let oCallBackData = {
      filterContext: oFilterContext,
      paginationData: oPaginationData,
    }

    return CS.customPostRequest(RequestMapping.getRequestUrl(sURL, {id: oActiveContent.id}), JSON.stringify(oPostData),
        successFetchTabSpecificContentsListCallback.bind(this, oCallBackData),
        failureFetchTabSpecificContentsListCallback.bind(this, oCallBackData));

  };

  let _modifyBreadCrumbNavigationForVirtualTab = function (oSelectedContent, sTabId, oFilterContext) {
    if (CS.isNotEmpty(oSelectedContent)) {
      let oBreadcrumbData = ContentScreenProps.breadCrumbProps.getForwardBreadCrumbData()[oSelectedContent.id];

      let oCallBackPayload = CS.find(oBreadcrumbData.payloadData, 'breadCrumbData');
      if (CS.isNotEmpty(oCallBackPayload)) {
        oCallBackPayload.functionToExecute = ContentStore.handleEntityTabClicked.bind(this, sTabId);
      }

      BreadcrumbStore.updateBreadcrumbPostData(oSelectedContent.id, {
        tabId: sTabId,
      });
    }
  };

  var _handleEntityTabClicked = function (sTabId, sContextId, oFilterContext) {
    //todo: CT

    var oActiveEntity = ContentUtils.getActiveEntity();
    //Don't allow to switch tab if variant is opened and is dirty
    if (oActiveEntity.variantInstanceId && !ContentUtils.activeContentSafetyCheck()) {
      return;
    }

    if (oActiveEntity.isDirty) {
      ContentUtils.setShakingStatus(true);
      _triggerChange();
      return;
    }
    var oExtraData = {};

    if (sTabId != ContentUtils.getSelectedTabId()) {
      ContentScreenProps.screen.resetSelectedContext();
      ContentScreenProps.tableViewProps.resetContextsByParentId(oActiveEntity.id);

      /** Currently following code need to be executed only wherever filter data is used for tabs **/
      let oFilterContext = ContentUtils.getFilterContextBasedOnTabId(ContentUtils.getSelectedTabId());
      if (CS.isNotEmpty(oFilterContext)) {
        let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
        oFilterStore.resetFilterPropsByContext();
      }
    }

    ContentUtils.removeTrailingBreadcrumbPath(oActiveEntity.id);

    // var oCallbackData = {};
    // oCallbackData.functionToExecute = _handleEntityTabClicked.bind(this, sTabId, sContextId, oFilterContext);

    /** if linked variant/version is in editing mode , do not allow tab switch/click*/
    var oVariantSectionProps = ContentScreenProps.variantSectionViewProps;
    if (!CS.isEmpty(oVariantSectionProps.getEditLinkedVariantInstance())) {
      var oDummyLinkedVariant = oVariantSectionProps.getDummyLinkedVariant();
      if (!CS.isEmpty(oDummyLinkedVariant) && oDummyLinkedVariant.isDirty) {
        ContentUtils.setShakingStatus(true);
        _triggerChange();
        return;
      }
    }

    //If task is being edited, do not allow tab switch
    var bIsTaskDirty = ContentScreenProps.taskProps.getIsTaskDirty();
    if (bIsTaskDirty) {
      ContentUtils.setShakingStatus(true);
      _triggerChange();
      return;
    }

    ContentScreenProps.screen.setActiveSections([]);

    /** Check tab change*/
    var bIsTabChanged = false;
    var oActiveEntitySelectedTabIdMap = ContentScreenProps.screen.getActiveEntitySelectedTabIdMap(sTabId);
    if (CS.isEmpty(oActiveEntitySelectedTabIdMap[oActiveEntity.id])) {
      oActiveEntitySelectedTabIdMap[oActiveEntity.id] = {selectedTabId: sTabId};
    } else {
      var sPrevSelectedTabId = oActiveEntitySelectedTabIdMap[oActiveEntity.id].selectedTabId;
      bIsTabChanged = sPrevSelectedTabId != sTabId;
      oActiveEntitySelectedTabIdMap[oActiveEntity.id].selectedTabId = sTabId;
    }


    if (sTabId != ContentScreenConstants.tabItems.TAB_TASKS) {
      ContentScreenProps.taskProps.setTaskData({});
    }

    if (sTabId != ContentScreenConstants.tabItems.TAB_TIMELINE) {
      ContentScreenProps.timelineProps.reset();
    }

    ContentScreenProps.goldenRecordProps.resetGoldenRecordComparisonProps();
    ContentScreenProps.screen.setIsSwitchDataLanguageDisabled(false);
    ContentScreenProps.screen.setRelationshipContextData({});
    ContentScreenProps.variantSectionViewProps.setSelectedContext({}); //clear selected context on tab change
    ContentUtils.setSelectedContentIds([]);
    if (bIsTabChanged) {
      ContentScreenProps.relationshipView.emptySelectedNatureRelationshipElements();
    }
    var oCallback = {};
    oCallback.preFunctionToExecute = function () {
      ContentScreenProps.variantSectionViewProps.setEditLinkedVariantInstance({});
      ContentUtils.setSelectedContextProps('', '', '', '', '', '');
    };

    switch (sTabId) {

        // If no special handling is required, remove separate cases
      case ContentScreenConstants.tabItems.TAB_TIMELINE:
        ContentScreenProps.timelineProps.reset();
        oExtraData.from = 0;
        break;
      case ContentScreenConstants.tabItems.TAB_DASHBOARD:
        _triggerChange();
        break;

      case ContentScreenConstants.tabItems.TAB_TASKS:
        ContentScreenProps.taskProps.setIsTaskForceUpdate(true);
        break;

      case ContentScreenConstants.tabItems.TAB_RENDITION:
        break;

      case ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS:
        _fetchTabSpecificContentsList(sTabId);
        return;
    }
    _fetchArticleById(oActiveEntity.id, oCallback, oExtraData);
  };

  var _handleCircledTagNodeClicked = function (sTagGroupId, sTagId, sContext, oExtraData) {
    var oScreenProps = ContentScreenProps.screen;
    var oReferencedTags = oScreenProps.getReferencedTags();
    var oActiveContent = ContentUtils.getActiveContent();
    var aEntityTags = [];
    var oRelationshipContextData = {};

    if (sContext === "entityHeaderTagEdit") {
      aEntityTags = _makeActiveEntityDirty().tags;
    } else {
      if (sContext === "entityDetailRelationSection") {
        oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
        if (oRelationshipContextData.isForSingleContent) {
          if (CS.isEmpty(oActiveContent.contentClone)) {
            ContentUtils.makeActiveEntityDirty();
            oRelationshipContextData.isDirty = true;
            ContentRelationshipStore.setRelationshipContextDataFromClone(oRelationshipContextData);
          }

          var oTempModifiedRelationshipContextData = ContentScreenProps.screen.getModifiedRelationshipsContextTempData();
          var oTempObject = oTempModifiedRelationshipContextData[oRelationshipContextData.relationshipContentInstanceId];
          oTempObject = CS.isEmpty(oTempObject) ? {} : oTempObject;
          oTempObject.tags = oRelationshipContextData.tags;
          oTempModifiedRelationshipContextData[oRelationshipContextData.relationshipContentInstanceId] = oTempObject;
        }
        if (CS.isEmpty(oRelationshipContextData.tags)) {
          var oDummyEntity = {
            tags: []
          };
          var oContext = oRelationshipContextData.context;
          CS.forEach(oContext.tags, function (oMasterTag) {
            ContentUtils.addAllMasterTagInEntity(oDummyEntity, oMasterTag);
          });
          oRelationshipContextData.tags = oDummyEntity.tags;
          if (oRelationshipContextData.isForSingleContent) {
            oTempObject.tags = oRelationshipContextData.tags;
            oTempModifiedRelationshipContextData[oRelationshipContextData.relationshipContentInstanceId] = oTempObject;
          }
        }
        aEntityTags = oRelationshipContextData.tags;

      } else {

        var oVariantSectionViewProps = ContentScreenProps.variantSectionViewProps;
        var oDummyVariant = {};
        if (!CS.isEmpty(oVariantSectionViewProps.getEditLinkedVariantInstance())) {
          oDummyVariant = oVariantSectionViewProps.getDummyLinkedVariant();
          oDummyVariant.isDirty = true;
          _makeActiveEntityDirty();
        } else {
          if (ContentUtils.isVariantDialogOpened() || ContentUtils.getIsUOMVariantDialogOpen()) {
            oDummyVariant = ContentUtils.makeActiveContentOrVariantDirty();
          }
          else {
            oDummyVariant = oVariantSectionViewProps.getDummyVariant();
          }

        }

        aEntityTags = CS.isEmpty(oDummyVariant) ? _makeActiveEntityDirty().tags : oDummyVariant.tags;
      }
    }

    var oMasterTagGroup = oReferencedTags[sTagGroupId];
    var oEntityTag = null;

    try {
      //Find Tag If not present then create new tag instance
      var oEntityTagGroup = CS.find(aEntityTags, {tagId: sTagGroupId});
      if (!oEntityTagGroup) {
        var oDummy = {tags: []};
        //ContentUtils.addAllMasterTagInEntity Needs object which have .tags thats why created oDummy instead of using aEntityTags
        ContentUtils.addAllMasterTagInEntity(oDummy, oMasterTagGroup);
        aEntityTags.push.apply(aEntityTags, oDummy.tags);
        oEntityTagGroup = oDummy.tags[0];
        let oSelectedContext = oExtraData.selectedContext || {};
        let sContextType = oSelectedContext.type;
        let oEntityContext = oActiveContent.context;
        let bShouldFillContaxtInstanceId = CS.includes([ContextTypeDictionary.CONTEXTUAL_VARIANT, ContextTypeDictionary.IMAGE_VARIANT], sContextType);
        if (bShouldFillContaxtInstanceId && oSelectedContext.id === oEntityContext.contextId) {
          oEntityTagGroup.contextInstanceId = oEntityContext.id;
        }
      }
      oEntityTag = CS.find(oEntityTagGroup.tagValues, {tagId: sTagId});
      if (!oEntityTag) {
        oEntityTag = ContentUtils.getNewTagValue(sTagId);
        oEntityTagGroup.tagValues.push(oEntityTag)
      }
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }

    var sTagType = oMasterTagGroup.tagType;

    if (oEntityTagGroup && oEntityTag) {

      if (sTagType === TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE) {
        oEntityTag.relevance = (oEntityTag.relevance == 100) ? -100 : (oEntityTag.relevance == -100) ? 0 : 100;
      } else if (sTagType == TagTypeConstants.RULER_TAG_TYPE) {
        CS.forEach(oEntityTagGroup.tagValues, function (oVal) {
          oVal.relevance = 0;
        });
        oEntityTag.relevance = 100;
      } else {
        if (!oMasterTagGroup.isMultiselect) {
          CS.forEach(oEntityTagGroup.tagValues, function (oVal) {
            oVal.relevance = (oEntityTag == oVal) ? oVal.relevance : 0;
          });
        }
        oEntityTag.relevance = (oEntityTag.relevance == 100) ? 0 : 100;
      }
    }

    if(oMasterTagGroup.isVersionable) {
      oScreenProps.setIsVersionableAttributeValueChanged(oMasterTagGroup.isVersionable);
    }

    _triggerChange();
  };

  var _validateNewValue = function (sVal, sAttributeBaseType) {
    var sAttributeVisualType = ContentUtils.getAttributeTypeForVisual(sAttributeBaseType);
    var aValueAsNumberTypes = ["measurementMetrics", "number", "calculated"];
    let bIsValidValue = true;

    if (CS.includes(aValueAsNumberTypes, sAttributeVisualType)) {
      bIsValidValue = ContentUtils.testNumber(sVal);
    }
    return bIsValidValue ? sVal : 0;
  };

  var _handleAssignImageToVariantButtonClicked = function (sEntityId, sAssetId, sContext) {
    if (sContext == "EntityHeaderView") {
      let oContent = ContentUtils.getActiveEntity();
      let sDefaultAssetInstanceId = oContent.defaultAssetInstanceId;

      if (sDefaultAssetInstanceId !== sAssetId) {
        let oEntity = ContentUtils.makeActiveContentDirty();
        oEntity.defaultAssetInstanceId = sAssetId;
        _setContentSelectedImageId(sAssetId);
        /**To display saveAndPublish button */
        ContentScreenProps.screen.setIsVersionableAttributeValueChanged(true);
      }
    } else {
      //TODO: Check uses, Can remove below code?
      var aContentList = ContentUtils.getEntityList();
      var oVariant = CS.find(aContentList, {id: sEntityId});
      if (oVariant) {
        if (!oVariant.assetImageChanged) {
          oVariant.previousDefaultAssetInstanceId = oVariant.defaultAssetInstanceId;
        }
        oVariant.assetImageChanged = true;
        oVariant.defaultAssetInstanceId = sAssetId;
      }
    }
  };

  let _handleDeleteGeneratedAssetLinkClicked = function () {
    let oActiveContent = ContentUtils.getActiveContent();
    let oData = {
      id: oActiveContent.id
    };
    CS.deleteRequest(getRequestMapping().DeleteAssetSharedUrl, {}, oData, successDeleteGeneratedAssetLink, failureDeleteGeneratedAssetLink)
  };

  let successDeleteGeneratedAssetLink = function (oResponse) {
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setAssetSharedUrl("");
    _triggerChange();
  };

  let failureDeleteGeneratedAssetLink = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureDeleteGeneratedAssetLink', getTranslation());
  };

  var _handleVariantCloseImageButtonClicked = function (sContext) {
    if (sContext != "EntityHeaderView") {
      var aContentList = ContentUtils.getEntityList();
      var oVariant = CS.find(aContentList, {assetImageChanged: true});
      if (!CS.isEmpty(oVariant)) {
        oVariant.defaultAssetInstanceId = oVariant.previousDefaultAssetInstanceId;
        oVariant.assetImageChanged = false;
      }
    }
    /*** We Open and close dialog by using state but still we set the variant Open Image dialog status.
     * We use that status on Escape Key press to close the dialog. */
    _toggleVariantOpenImageDialogStatus();
  };

  var _toggleVariantOpenImageDialogStatus = function () {
    var oScreenProps = ContentScreenProps.screen;
    var bUploadDialogStatus = oScreenProps.getVariantUploadImageDialogStatus();
    oScreenProps.setVariantUploadImageDialogStatus(!bUploadDialogStatus);
  };

  var _updateMandatoryElementsStatus = function (oActiveEntity) {
    var aViolatingMandatoryElements = _getViolatingMandatoryFields(oActiveEntity);
    var oSectionProps = ContentUtils.getComponentProps().contentSectionViewProps;
    oSectionProps.setViolatingMandatoryElements(aViolatingMandatoryElements);
  };

  var _handleDummyLinkedVariantDiscard = function () {
    var oVariantSectionViewProps = ContentScreenProps.variantSectionViewProps;
    var oEditLinkedVariantInstance = oVariantSectionViewProps.getEditLinkedVariantInstance();
    var oSelectedContext = ContentScreenProps.variantSectionViewProps.getSelectedContext();
    var oClone = CS.cloneDeep(oEditLinkedVariantInstance);
    oClone.relationshipId = ContentScreenProps.screen.getVariantRelationshipId();
    oClone.contextId = oSelectedContext.id;
    oVariantSectionViewProps.setDummyLinkedVariant(oClone);
  };

  var _cleanReferencedNatureRelationshipElements = function () {
    var aNatureRelationshipIds = ContentScreenProps.screen.getNatureRelationshipIds();
    var oReferenceNatureRelationshipInstanceElements = ContentScreenProps.relationshipView.getReferenceNatureRelationshipInstanceElements();
    if (!CS.isEmpty(aNatureRelationshipIds)) {
      CS.forEach(aNatureRelationshipIds, function (sNatureRelationshipId) {
        var aNatureRelationshipInstanceElements = oReferenceNatureRelationshipInstanceElements[sNatureRelationshipId];
        CS.forEach(aNatureRelationshipInstanceElements, function (oElement) {
          if (oElement.elementClone) {
            delete oElement.elementClone;
          }
        });
      });
    }
  };

  var successFetchImageGalleryData = function (oCallbackData, oResponse) {
    var oResponseData = oResponse.success;
    if (!CS.isEmpty(oResponseData)) {
      var oScreenProps = ContentScreenProps.screen;
      var aReferencedAsset = oResponseData.referencedAssets;

      oScreenProps.setAssetList(aReferencedAsset);
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      _triggerChange();
    }
  };

  var failureFetchImageGalleryData = function (oCallBack, oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchImageGalleryData', getTranslation());
    _checkForExtraNecessaryServerCalls(oResponse);
  };

  var _handleImageGalleryDialogViewVisibilityStatusChanged = function (oCallbackData) {
    var oActiveEntity = ContentUtils.getActiveEntity();
    var oData = {};
    var oFilterPostData = {};
    oFilterPostData.id = oActiveEntity.id;
    oFilterPostData.configIds = oActiveEntity.types;
    var sScreenContext = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);
    var sUrl = getRequestMapping(sScreenContext).GetAllAssets;
    var fSuccess = successFetchImageGalleryData.bind(this, oCallbackData);
    var fFailure = failureFetchImageGalleryData.bind(this, oCallbackData);
    CS.postRequest(sUrl, oData, oFilterPostData, fSuccess, fFailure);
  };

  let _getMasterAssetTypeIdsList = function (aMasterAssetTypeIdsList) {
    let aMasterAssetTypeList = [];
    CS.forEach(aMasterAssetTypeIdsList, function (oMasterAsset) {
      let oMasterData = {};
      oMasterData.id = oMasterAsset.id;
      oMasterData.canDownload = oMasterAsset.canDownload;
      aMasterAssetTypeList.push(oMasterData);
    });
    return aMasterAssetTypeList;
  };

  var _showWarnings = function (oSuccessResponse) {
    let oTranslations = getTranslation();
    let aWarningData = oSuccessResponse.warnings.exceptionDetails;
    let aWarnings = [];
    let sRenditionNotProcessedException = "RenditionNotProcessedException";
    CS.forEach(aWarningData, function (oWarning) {
      if (oWarning.key === sRenditionNotProcessedException) {
        aWarnings.push(oWarning.itemName);
      }
    })
    if (CS.isNotEmpty(aWarnings)) {
      alertify.warning(`${oTranslations[sRenditionNotProcessedException]} ${aWarnings.join(", ")}`);
    }
  };

  var _checkForExtraNecessaryServerCalls = function (oResponse) {

    if (!CS.isEmpty(oResponse.failure)) {
      var aResponseData = oResponse.failure.exceptionDetails;
      var aFailureKeyStrings = [];
      CS.forEach(aResponseData, function (oResponseData) {
        aFailureKeyStrings.push(oResponseData.key);
      });

      var aInstanceNotFoundKeys = [
        "ArticleInstanceNotFoundException", "AssetInstanceNotFoundException", "MarketInstanceNotFoundException",
        "InstanceForRelationshipNotFoundException", "TextAssetInstanceNotFoundException", "SupplierInstanceNotFoundException"];

      if (!CS.isEmpty(CS.intersection(aFailureKeyStrings, aInstanceNotFoundKeys))) {
        let aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
        let iIndex = aBreadcrumb.length - 2;
        let oItem = aBreadcrumb[iIndex];

        /** Multiuser scenario : If user tries to make any changes in the content which has already been deleted from other tab
         *  in that case user should navigate to previous state before content   **/
        if (!CS.isEmpty(oItem)) {
          /**
           * @Router Impl
           * @description required to reset history state of deleted content
           * @private
           */
          let fCallback = () => {
            SharableURLStore.setIsPushHistoryState(true);
            SharableURLStore.addParamsInWindowURL(oItem.id, oItem.type, oItem.baseType);
            _handleEntityNavigation(oItem);
          };
          SharableURLStore.setIsEntityNavigation(false);
          CS.navigateTo(-2, fCallback);
        }
      }
    }
  };

  let _handleDateRangePickerDateChange = function (sContext, oSelectedTimeRange) {
    switch (sContext) {
      case ContentScreenConstants.sectionTypes.SECTION_TYPE_DOWNLOAD_INFO:
        let oAssetDownloadTrackerProps = ContentScreenProps.assetDownloadTrackerProps;
        let oDownloadRange = {
          startTime: oSelectedTimeRange.startDate.toDate().getTime(),
          endTime: oSelectedTimeRange.endDate.toDate().getTime()
        };
        oAssetDownloadTrackerProps.setDownloadRange(oDownloadRange);
        _getAssetDownloadCount();
        break;
      case "dashboardScreen":
        DashboardScreenStore.handleDateRangeSelected(oSelectedTimeRange);
        break;
    }
  };

  let _handleDateRangePickerCancelClicked = function (sContext) {
    switch (sContext) {
      case "dashboardScreen":
        DashboardScreenStore.handleDateRangeClearButtonClicked();
        break;
    }
    _triggerChange();
  };

  let _handleArticleNodeClickedWrapper = function (sArticleId, oCallbackData, sBreadcrumbBaseType, oExtraData) {
    var sBaseType = "";
    if (CS.isEmpty(sBreadcrumbBaseType)) {
      //TODO: Temporary cleanup after November Realease
      var oAppData = ContentUtils.getAppData();
      var aMasterContentList = oAppData.getContentList();
      var oEntity = ContentUtils.getNodeFromTreeListById(aMasterContentList, sArticleId);
      sBaseType = oEntity.baseType;
    } else {
      sBaseType = sBreadcrumbBaseType;
    }

    var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(sBaseType);
    let aScreensToSetTileMode = [BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION, BreadCrumbModuleAndHelpScreenIdDictionary.DIMODULE, "module"];

    /**TODO: Need to change**/
    ContentScreenProps.screen.setIsEditMode(true);
    if (sArticleId == ContentScreenConstants.TREE_ROOT_ID || ContentUtils.getContentScreenMode() != ContentScreenConstants.entityModes.ARTICLE_MODE) {
      ContentUtils.setContentScreenMode(sScreenMode);
      ContentUtils.setViewMode(ContentUtils.getDefaultViewMode());
    } else if (CS.includes(aScreensToSetTileMode, sBreadcrumbBaseType)) {
      ContentUtils.setViewMode(ContentUtils.getDefaultViewMode());
    }
  };

  var _handleEntityNavigation = function (oItem) {
    trackMe('_handleEntityNavigation');
    let oBreadCrumbExtraData = oItem.extraData;

    /** To check whether the clicked node is parent content **/
    var oExtraData = {
      childContextId: oItem.childContextId,
      contextId: oItem.contextId,
      breadCrumbNavigation: true
    };
    //if breadcrumb item is normal variant & is not cloning variant then add variant instance id
    if (oItem.isVariant && CS.isEmpty(oItem.branchOfLabel)) {
      oExtraData.variantInstanceId = oItem.id
    }

    ContentUtils.setSelectedContextProps('', '', '', '');

    //remove breadcrumb path
    ContentUtils.removeTrailingBreadcrumbPath(oItem.id, oItem.type);

    let oCallbackData = {};
    let oRelationshipAddEntity = ContentScreenProps.screen.getAddEntityInRelationshipScreenData();
    delete oRelationshipAddEntity[oItem.id];

    let oCollectionProps = ContentScreenProps.collectionViewProps;
    let oActiveCollection = oCollectionProps.getActiveCollection();
    let oActiveEntity = ContentUtils.getActiveEntity();
    let bIsCollectionScreen = !CS.isEmpty(oActiveCollection);

    let sArticleId = "";
    if (!CS.isEmpty(oActiveEntity)) {
      sArticleId = (oExtraData.variantInstanceId) ? oActiveEntity.id : oItem.id;
      oExtraData.dontResetFilter = bIsCollectionScreen && !CS.isEmpty(oActiveEntity); //dont reset filter when content inside the collection in closed
      _handleArticleNodeClickedWrapper(sArticleId, oCallbackData, oItem.type, oExtraData);
    }

    oBreadCrumbExtraData.executeFunctionToSet();
    ContentScreenProps.screen.setIsContentHeaderCollapsed(false);
  };

  let _handleEditViewFilterOptionChanged = (sId) => {
    ContentUtils.setActiveEntitySelectedFilterId(sId);
    ContentUtils.resetToUpdateAllSCU();
    _triggerChange();
  };

  let _handleAcrolinxResultChecked = (sScoreValue, sScoreCardUrl) => {
    let oActiveEntity = ContentUtils.makeActiveContentDirty();
    let oAcrolinxScoreAttribute = CS.find(oActiveEntity.attributes, {attributeId: AcrolinxAttributeIdDictionary.ACROLINX_SCORE_ATTRIBUTE});
    let oAcrolinxScoreCardUrlAttribute = CS.find(oActiveEntity.attributes, {attributeId: AcrolinxAttributeIdDictionary.ACROLINX_SCORE_URL_ATTRIBUTE});
    oAcrolinxScoreAttribute && (oAcrolinxScoreAttribute.value = sScoreValue);
    oAcrolinxScoreCardUrlAttribute && (oAcrolinxScoreCardUrlAttribute.value = sScoreCardUrl);

    ContentUtils.resetToUpdateAllSCU();

    _triggerChange();
  };

  var _handleClearCollectionProps = function () {
    var oCollectionProps = ContentScreenProps.collectionViewProps;
    var oActiveCollection = oCollectionProps.getActiveCollection();
    var oScreenProps = ContentScreenProps.screen;
    if (oActiveCollection && oActiveCollection.isDirty) {
      ContentUtils.setShakingStatus(true);
      _triggerChange();
      return;
    }
    oCollectionProps.setActiveCollection({});

    //TO Clear XRay Properties
    ContentUtils.clearXRayData();
    ContentUtils.setViewMode(oScreenProps.getDefaultViewMode());

    let oFilterContext = {
      screenContext: oActiveCollection.id,
      filterType: oFilterPropType.COLLECTION
    }
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterStore.resetFilterPropsByContext();
    let contextualAllCategoriesProps = ContextualAllCategoriesTaxonomiesProps.getAllCategoriesSelectorViewPropsByContext(oFilterContext.screenContext);
    contextualAllCategoriesProps.reset();
    BreadcrumbStore.resetBreadcrumbNodeFilterDataById(oActiveCollection.id);

    ContentScreenProps.collectionViewProps.setAddEntityInCollectionViewStatus(false);

    var sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();
    HomeScreenCommunicator.disablePhysicalCatalog(false);
  };

  var _getArticleDetails = function (sArticleId, oCallbackData, oExtraData) {
    _setContentSelectedImageId('');
    if (sArticleId == ContentUtils.getTreeRootNodeId()) {
      if (ContentScreenProps.goldenRecordProps.getShowGoldenRecordBuckets()) {
        GoldenRecordStore.fetchGoldenRecordBuckets(oCallbackData);
      } else {
        ContentUtils.setActiveContent({});
        _fetchContentList(oCallbackData, oExtraData);
      }
    } else {
      _fetchArticleById(sArticleId, oCallbackData, oExtraData);
    }
  };

  /**
   * @function _handleAvailableEntityFilterButtonClicked
   * @param oCallBackData - We get filterContext in oCallback data
   * @param oExtraData
   * @param sSelectedHierarchyContext
   * @returns {*}
   * @private
   */
  var _handleAvailableEntityFilterButtonClicked = function (oCallBackData, oExtraData, sSelectedHierarchyContext) {
    var oAvailableEntityFilterProps = ContentScreenProps.screen.getTempAvailableEntityFilterProps();
    let oActiveSortDetails = ContentScreenProps.innerFilterProps.getActiveSortDetails();
    CS.forEach(oActiveSortDetails, function (oActiveSortDetail, sKey) {
      oAvailableEntityFilterProps.sortField = oActiveSortDetail.id;
      oAvailableEntityFilterProps.sortOrder = oActiveSortDetail.sortOrder;
    });
    ContentScreenProps.screen.applyAdvanceFilterProps();
    return AvailableEntityStore.handleSearchApplyClicked(oCallBackData, oExtraData, sSelectedHierarchyContext);
  };

  var _handleAdvancedSearchListLoadMoreClicked = function (sType, sContext) {
    _loadMoreFetchEntities([sType]);
  };

  var _handleAdvancedSearchListSearched = function (sSearchText, sContext) {
    ContentScreenProps.screen.resetEntitiesPaginationData();
    let oPaginationData = ContentScreenProps.screen.getEntitiesPaginationData();
    if(sContext === "bulkEdit") {
      // To get only editable attributes for bulk edit
      if(oPaginationData && oPaginationData.attributes) {
        oPaginationData.attributes.isDisabled = false;
        oPaginationData.attributes.typesToExclude = ContentUtils.getAllExcludedAttributeTypeForBulkEdit();
      }
    }
    ContentScreenProps.screen.setEntitySearchText(sSearchText);
    return _fetchEntities();
  };

  /**
   * @function _handleFilterButtonWrapperClicked
   * @param oCallback - We get filter context in oCallback
   * @returns {*}
   * @private
   */
  var _handleFilterButtonWrapperClicked = function (oCallback) {
    var oActiveEntity = _getActiveEntity();
    if (oActiveEntity.contentClone) {
      ContentUtils.setShakingStatus(true);
      _triggerChange();
      return new Promise((resolve) => {
        resolve(null);
      });

    } else {
      return _handleFilterButtonClicked(oCallback);
    }
  };

  var _handleVariantSummaryCloseEditView = function () {
    ContentScreenProps.screen.setVariantTagSummaryEditViewStatus(false);
  };

  var _handleVariantSummaryEditButtonClicked = function (sClickedVariantId, bNoTrigger) {
    var oDummyVariant = ContentScreenProps.variantSectionViewProps.getDummyVariant();
    if (CS.isEmpty(oDummyVariant)) {
      ContentScreenProps.screen.setVariantTagSummaryEditViewStatus(true);
      ContentScreenProps.screen.setVariantTagSummaryEditClickedVariantId(sClickedVariantId);
      if (!bNoTrigger) {
        _triggerChange();
      }
    } else {
      ContentUtils.showMessage(getTranslation().VARIANT_CREATION_IN_PROGRESS);
    }
  };

  var _handleContentHorizontalTreeNodeCollapseClickedChildrenRemove = function (oReqData) {
    var oScreenProps = ContentScreenProps.screen;
    var oHierarchyTree = oScreenProps.getContentHierarchyTree();
    var aHierarchyChildren = oHierarchyTree.children;

    var sClickedNodeId = oReqData.clickedNodeId;
    var oVisualProps = oScreenProps.getContentHierarchyVisualProps();
    oVisualProps[sClickedNodeId].isCollapsed = !oVisualProps[sClickedNodeId].isCollapsed;

    var oClickedNodeFromTree = ContentUtils.getNodeFromTreeListById(aHierarchyChildren, sClickedNodeId);
    ContentUtils.removeChildNodesFromValueList(oClickedNodeFromTree.children, oVisualProps);
    delete oClickedNodeFromTree.children;

    _triggerChange();
  };

  var _handleContentHorizontalTreeNodeCollapseClickedNoChildrenRemove = function (oReqData) {
    var sClickedNodeId = oReqData.clickedNodeId;
    var iLevel = oReqData.level;
    var oScreenProps = ContentScreenProps.screen;
    var oVisualProps = oScreenProps.getContentHierarchyVisualProps();
    oVisualProps[sClickedNodeId].isCollapsed = true;

    var oHierarchyTree = oScreenProps.getContentHierarchyTree();
    var aHierarchyChildren = oHierarchyTree.children;
    var oNode = iLevel == 0 ? oHierarchyTree : ContentUtils.getNodeFromTreeListById(aHierarchyChildren, sClickedNodeId);
    var aChildrenKeyValueToReset = [
      {key: "isActive", value: false},
      {key: "isSelected", value: false},
      {key: "isCollapsed", value: true},
      {key: "noChildren", value: false}
    ];
    ContentUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oNode, oVisualProps, true);

    _triggerChange();
  };

  let _handleRuntimePCSectionExpansionToggled = function (sSectionId) {
    //for collection
    var oScreenProps = ContentScreenProps.screen;
    let oActiveCollection = oScreenProps.getActiveHierarchyCollection();
    let sURLToFetchSections = CommonModuleRequestMapping.GetSectionInfoForCollection;
    _handleSectionExpandToggle(sSectionId, oActiveCollection, sURLToFetchSections);
  };

  let _handleTaxonomyHierarchyExpansionToggled = function (sSectionId, oFilterContext) {
    //for taxonomy
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    var oFilterProps = oFilterStore.getFilterProps();
    let oEntity = oFilterProps.getTaxonomyNodeSections();
    let sURLToFetchSections = _getURLToFetchSectionsAccordingToEntity(oEntity, oFilterContext);
    let oActiveKlass = oFilterStore.getFilterProps().getTaxonomyActiveClass();
    oEntity.id = oActiveKlass.id;
    _handleSectionExpandToggle(sSectionId, oEntity, sURLToFetchSections);
  };

  let _handleSectionExpandToggle = function (sSectionId, oEntity, sURLToFetchSections) {
    let oCurrentEntity = oEntity.clonedObject || oEntity;
    let oCurrentSection = CS.find(oCurrentEntity.sections, {id: sSectionId});
    let oSection = CS.find(oEntity.sections, {id: sSectionId});

    let bExpansionStateToSet = !oSection.isCollapsedUI;
    oSection.isCollapsedUI = bExpansionStateToSet;
    if (oEntity.clonedObject) {
      let oClonedSection = CS.find(oEntity.clonedObject.sections, {id: sSectionId});
      oClonedSection.isCollapsedUI = bExpansionStateToSet;
    }
    if (CS.isEmpty(oCurrentSection.elements)) {
      _fetchEntityPropertyCollections(oCurrentEntity, [sSectionId], sURLToFetchSections);
    }
    else {
      _triggerChange();
    }
  };

  let _getURLToFetchSectionsAccordingToEntity = function (oEntity, oFilterContext) {
    let sURL = "";
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oCurrentEntity = oFilterStore.getFilterProps().getTaxonomyActiveClass();

    if (!CS.isEmpty(oCurrentEntity.type)) {
      sURL = CommonModuleRequestMapping.GetClassPropertyCollection
    } else {
      sURL = CommonModuleRequestMapping.GetTaxonomyPropertyCollection;
    }
    return sURL;
  };

  let _fetchEntityPropertyCollections = function (oCurrentEntity, aSectionIds, sURLToFetchSections) {

    if (!CS.isEmpty(aSectionIds)) {
      let oData = {
        sectionIds: aSectionIds,
        typeId: oCurrentEntity.id
      };

      CS.postRequest(sURLToFetchSections, {}, oData, successEntityClassPropertyCollections.bind(this, oCurrentEntity), failureEntityClassPropertyCollections);
    }
  };

  let successEntityClassPropertyCollections = function (oCurrentEntity, oResponse) {
    let oSuccess = oResponse.success;
    let aSections = oSuccess.list;
    let oScreenProps = ContentScreenProps.screen;
    oCurrentEntity = oCurrentEntity.clonedObject || oCurrentEntity;
    let oReferencedTags = {};

    CS.forEach(aSections, (oSection) => {
      try {
        let oClassSection = CS.find(oCurrentEntity.sections, {id: oSection.id});
        oClassSection.elements = oSection.elements;
        CS.forEach(oSection.elements, function (oElement) {
          var oTag = oElement.tag;
          if (oTag) {
            oReferencedTags[oTag.id] = oTag;
          }
        });
      }
      catch (oException) {
        ExceptionLogger.error(oException);
      }
    });
    oScreenProps.setReferencedTags(oReferencedTags);
    _triggerChange();
  };

  let failureEntityClassPropertyCollections = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureEntityClassPropertyCollections", getTranslation());
  };

  var _addTagInContentFromUniqueSearch = function (sGroupId, sDataId) {
    if (ContentUtils.activeTaskSafetyCheck()) {
      var oActiveContent = ContentUtils.makeActiveContentOrVariantDirty();
      var oTagGroup = CS.find(oActiveContent.tags, {id: sGroupId});
      var oTagFromTagValues = CS.find(oTagGroup.tagValues, {tagId: sDataId});

      for (var i = 0; i < oTagGroup.tagValues.length; i++) {
        oTagGroup.tagValues[i].relevance = 0;
      }

      if (CS.isEmpty(oTagFromTagValues)) {
        var oMasterTagNode = ContentUtils.getMasterTagById(sGroupId);
        var oTag = CS.find(oMasterTagNode.children, {'id': sDataId});
        var oNewTag = {
          id: UniqueIdentifierGenerator.generateUUID(),
          tagId: oTag.id,
          relevance: 100,
          timestamp: new Date().getTime()
        };

        oTagGroup.tagValues.push(oNewTag);
      }
      else {
        oTagFromTagValues.relevance = 100;
      }

      _updateMandatoryElementsStatus(oActiveContent);

      oTagGroup.isValueChanged = true;
    }
  };

  var _clearFilterHierarchyRelatedData = function (oCallback) {
    var oScreenProps = ContentScreenProps.screen;
    oScreenProps.setViewMode(oScreenProps.getDefaultViewMode());
    oScreenProps.setFilterHierarchySelectedFilters([]);
    oScreenProps.setIsFilterHierarchySelected(false);
    oScreenProps.setThumbnailMode(ThumbnailModeConstants.BASIC);

    if (oCallback && oCallback.functionToExecute) {
      var oInnerCallback = {
        isFromFilterHierarchy: true
      };
      oCallback.functionToExecute(oInnerCallback);
    } else {
      _triggerChange();
    }
  };

  var _handleAfterEffectsOfTaxonomyNotFoundInTaxonomyHierarchy = function (oData) {
    var sClickedTaxonomyId = oData.clickedTaxonomyId;

    var oScreenProps = ContentScreenProps.screen;
    var oHierarchyTree = oScreenProps.getContentHierarchyTree();

    var oParentNode = ContentUtils.getParentNodeByChildId([oHierarchyTree], sClickedTaxonomyId);
    CS.remove(oParentNode.children, {id: sClickedTaxonomyId});
    _triggerChange();
  };

  var _getNodeFromTreeListByInstanceId = function (aNodeList, sNodeId) {
    var oFoundNode = {};
    CS.forEach(aNodeList, function (oNode) {
      if (oNode.instanceId == sNodeId) {
        oFoundNode = oNode;
        return false;
      }
      else {
        if (oNode.children) {
          oFoundNode = _getNodeFromTreeListByInstanceId(oNode.children, sNodeId);
          if (!CS.isEmpty(oFoundNode)) {
            return false;
          }
        }
      }
    });

    return oFoundNode;
  };

  var _addNewFilterInstanceOfSelectedTagValuesInTag = function (oAppliedFilterTag, oMasterTag, aTagValueRelevanceData) {
    if (!aTagValueRelevanceData) {
      return;
    }

    CS.forEach(oAppliedFilterTag.children, function (oTag) {
      let oMasterChildTag = CS.find(oMasterTag.children, {id: oTag.id});
      oTag.iconKey = oMasterChildTag.iconKey;
    });

    var aNewFilterInstanceReqData = [];
    CS.forEach(aTagValueRelevanceData, function (oData) {
      var oChild = CS.find(oAppliedFilterTag.children, {id: oData.tagId});
      if (!oChild) {
        var oMasterTagValue = CS.find(oMasterTag.children, {id: oData.tagId});
        aNewFilterInstanceReqData.push({
          id: oData.tagId,
          label: oMasterTagValue.label
        });
      }
    });

    if (aNewFilterInstanceReqData) {
      var aNewFilterInstances = _getTagDummyInstancesForFilters(aNewFilterInstanceReqData, oMasterTag);
      CS.forEach(aNewFilterInstances, function (oFilterInstance) {
        oAppliedFilterTag.children.push(oFilterInstance);
      })
    }
  };

  var _handleSearchAndFetchEntities = function (aContexts, sSearchText, oCallback) {
    ContentScreenProps.screen.resetEntitiesPaginationData();
    ContentScreenProps.screen.setEntitySearchText(sSearchText);
    return _fetchEntities(aContexts, false, oCallback);
  };

  var _fetchUOMVariant = function (sArticleId, oCallbackData, oExtraData) {
    var oActiveContent = ContentUtils.getActiveContent();
    var oCallback = {
      entityType: oActiveContent.baseType //to change url based on entity baseType
    };

    if (oActiveContent.baseType !== BaseTypeDictionary.assetBaseType) {
      oCallback.functionToExecute = () => {
        _setContentSelectedImageId('');
        /** full-screen should be closed, when any variant is opened from full screen mode**/
          let sFullScreenContextId = UOMProps.getFullScreenTableContextId();
          CS.isNotEmpty(sFullScreenContextId) && VariantStore.handleUOMViewFullScreenButtonClicked(sFullScreenContextId);
      };
    }
    CS.assign(oCallbackData, oCallback);

    _fetchArticleById(sArticleId, oCallbackData, oExtraData);
  };

  var _getLayoutSkeleton = function () {
    return {
      attributeTable: {},
      tagTable: {},
      relationshipTable: {},
      headerInformationTable: {},
      fixedHeaderTable: {}
    };
  };

  var successGetInstanceComparisionData = function (aSelectedInstanceIds, oCallbackData, oResponse) {
    var oResponseData = oResponse.success;
    var oConfigDetails = oResponseData.configDetails;
    var aKlassInstances = oResponseData.klassInstancesDetails;
    var oLayoutData = _getLayoutSkeleton();

    ContentScreenProps.screen.setMasterKlassInstanceListForComparison(aKlassInstances);
    if (oCallbackData && oCallbackData.preFunctionToExecute) {
      oCallbackData.preFunctionToExecute();
    }

    var aSelectedKlassInstances = ContentUtils.getOnlySelectedKlassInstances(aKlassInstances, aSelectedInstanceIds);
    let oKlassInstance = aSelectedKlassInstances && aSelectedKlassInstances[0] && aSelectedKlassInstances[0].klassInstance;
    let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oKlassInstance.baseType);

    ContentUtils.makeInstancesComparisonRowDataForTags(oLayoutData, oConfigDetails, false);
    ContentUtils.makeInstancesComparisonRowDataForAttributes(oLayoutData, oConfigDetails, false);
    ContentUtils.makeInstancesComparisonRowDataForRelationshipTab(oLayoutData, oConfigDetails, aSelectedKlassInstances, false);
    ContentUtils.makeInstancesComparisonRowDataForNatureRelationships(oLayoutData, oConfigDetails, aSelectedKlassInstances);
    ContentUtils.makeInstancesComparisonRowDataForHeaderInformation(oLayoutData, sScreenMode);
    ContentUtils.makeInstancesComparisonRowDataForFixedHeader(oLayoutData);

    ContentUtils.makeInstancesComparisonColumnData(oLayoutData, oConfigDetails, aSelectedKlassInstances);

    ContentScreenProps.screen.setIsContentComparisonMode(true);
    ContentScreenProps.screen.setContentComparisonLayoutData(oLayoutData);

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var _handleLanguageForComparisonChanged = function (sLanguageForComparison) {
    let aLanguagesInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = aLanguagesInfo.dataLanguages;
    let oSelectedLanguageForComparison = CS.find(aDataLanguages, {id: sLanguageForComparison});
    let sLanguageForComparisonCode = oSelectedLanguageForComparison.code;
    let oExtraData = {
      dataLanguage: sLanguageForComparisonCode
    };

    let oCallbackData = {
      preFunctionToExecute: function () {
        ContentScreenProps.screen.setSelectedLanguageForComparison(sLanguageForComparisonCode);

        if (!CS.isEmpty(oSelectedLanguageForComparison)) {
          MomentUtils.setCurrentDateFormat(oSelectedLanguageForComparison);
          NumberUtils.setCurrentNumberFormat(oSelectedLanguageForComparison);
        }
      }
    };

    _instancesComparisonClicked(oCallbackData, oExtraData);
  };

  let _instancesComparisonClicked = function (oCallbackData, oExtraData) {
    var aSelectedInstances = ContentUtils.getSelectedContents();
    var aSelectedInstanceIds = ContentUtils.getSelectedContentIds();
    var aListForComparision = [];
    var oListItem = {};

    CS.forEach(aSelectedInstances, function (oSelectedInstances) {
      oListItem = {"id": oSelectedInstances.id, "baseType": oSelectedInstances.baseType};
      aListForComparision.push(oListItem);
    });
    var oListForComparision = {"productListsToCompare": aListForComparision};
    var fSuccess = successGetInstanceComparisionData.bind(this, aSelectedInstanceIds, oCallbackData);
    var fFailure = failureGetInstanceComparisionData;
    var sUrl = RequestMapping.getRequestUrl(getRequestMapping().GetInstanceComparisionData);
    var oData = {};
    CS.postRequest(sUrl, oData, oListForComparision, fSuccess, fFailure, false, oExtraData);
  };

  var _handleComparisonButtonClicked = function () {
    var aSelectedInstanceIds = ContentUtils.getSelectedContentIds();

    if (CS.isEmpty(aSelectedInstanceIds)) {
      ContentUtils.showMessage(getTranslation().NO_CONTENTS_SELECTED_FOR_COMPARISON);
      return;
    }
    if (ContentUtils.getIsStaticCollectionScreen()) {
      var aSelectedInstances = ContentUtils.getSelectedContents();
      var bIsDifferentContent = CS.some(aSelectedInstances, (instance)=> {return (instance.baseType !== aSelectedInstances[0].baseType)});
      if (bIsDifferentContent) {
        ContentUtils.showMessage(getTranslation().COLLECTION_COMPARE_PRODUCTS_CONFLICT)
        return;
      }
    };

    /** To set current data language as language for comparison for the first time when comparison window is opened **/
    let oCallbackData = {
      preFunctionToExecute: function () {
        let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
        ContentScreenProps.screen.setSelectedLanguageForComparison(sSelectedDataLanguageCode);
      }
    };

    _instancesComparisonClicked(oCallbackData);
  };

  var _handleContentComparisonBackButtonClicked = function () {
    var oScreenProps = ContentScreenProps.screen;
    oScreenProps.setIsContentComparisonMode(false);
    let aLanguagesInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = aLanguagesInfo.dataLanguages;
    let sSelectedDataLanguageFromSession = ContentUtils.getSelectedDataLanguage();
    let oSelectedDataLanguageFromSession = CS.find(aDataLanguages, {id: sSelectedDataLanguageFromSession});
    if (!CS.isEmpty(oSelectedDataLanguageFromSession)) {
      MomentUtils.setCurrentDateFormat(oSelectedDataLanguageFromSession);
      NumberUtils.setCurrentNumberFormat(oSelectedDataLanguageFromSession);
    }
    BreadcrumbStore.refreshCurrentBreadcrumbEntity();
  };

  var _handleRefreshContentButtonClicked = function (oFilterContext) {
    // check if content is not saved and prompt user accordingly
    var oComponentProps = ContentUtils.getComponentProps();
    var oActiveContent = ContentUtils.getActiveContent();
    var bIsTaskDirty = oComponentProps.taskProps.getIsTaskDirty();
    var sSelectedTab = ContentUtils.getSelectedTabId();

    if (sSelectedTab === ContentScreenConstants.tabItems.TAB_TIMELINE) {
      ContentScreenProps.timelineProps.reset();
    }

    ContentLogUtils.debug('handleRefreshContentButtonClicked: Refresh content', oActiveContent);

    var oTriggerChangeCallBackData = {};
    oTriggerChangeCallBackData.functionToExecute = _triggerChange;

    if (oActiveContent.contentClone || bIsTaskDirty) {
      ContentUtils.setShakingStatus(true);
      _triggerChange();
      return;
    } else /*if(ContentUtils.activeTaskCommentSafetyCheck())*/{
      var oExtraData = {};

      if (!CS.isEmpty(oActiveContent.variantInstanceId)) {

        var oSelectedContext = oComponentProps.variantSectionViewProps.getSelectedContext();
        var oSelectedVisibleContext = oComponentProps.variantSectionViewProps.getSelectedVisibleContext();

        oExtraData.variantInstanceId = oActiveContent.variantInstanceId;
        oExtraData.contextId = oSelectedContext.id;
        oExtraData.childContextId = oSelectedVisibleContext.id;
        oExtraData.breadCrumbNavigation = true;

      }

      var oCallBackForFetchEntity = {entityType: oActiveContent.baseType};
      /** To handle multi tab scenario following if check is added.*/
      if (CS.isEmpty(oActiveContent)) {
        return CommonUtils.refreshCurrentBreadcrumbEntity();
      } else {
        if (oActiveContent.baseType !== BaseTypeDictionary.assetBaseType) {
          oCallBackForFetchEntity.functionToExecute = () => {
            _setContentSelectedImageId('');
          };
        }
        oCallBackForFetchEntity.filterContext = oFilterContext;
        return _getContentByIdFromServer(oActiveContent.id, oCallBackForFetchEntity, oExtraData);
      }
    }

  };

  /**
   * @function - _changeColumnSequenceOfInstanceComparison
   * @param  {array} aColumnData description
   * @param  {string} sColumnId   description
   */
  var _changeColumnSequenceOfInstanceComparison = function (aColumnData, sColumnId) {
    CS.forEach(aColumnData, function (oColumnData) {
      var bIsSameColumn = (oColumnData.id === sColumnId);
      //oColumnData.isFixed = bIsSameColumn;
      oColumnData.forComparison = bIsSameColumn;
    });
  };

  var _handleMatchMergeColumnHeaderClicked = function (sColumnId) {
    var oComparisonLayoutData = ContentScreenProps.screen.getContentComparisonLayoutData();
    var oHeaderInformationTable = oComparisonLayoutData.headerInformationTable;
    var oFixedHeaderTable = oComparisonLayoutData.fixedHeaderTable;
    var aHeaderInformationColumnData = oHeaderInformationTable.headerInformation && oHeaderInformationTable.headerInformation.columnData;
    var aFixedHeaderColumnData = oFixedHeaderTable.fixedHeader && oFixedHeaderTable.fixedHeader.columnData;
    var oAttributeTable = oComparisonLayoutData.attributeTable;
    var aAttributeColumnData = oAttributeTable.attribute && oAttributeTable.attribute.columnData;
    var oRelationshipTable = oComparisonLayoutData.relationshipTable;
    var oTagTable = oComparisonLayoutData.tagTable;

    CS.forEach(oTagTable, function (oTag) {
      _changeColumnSequenceOfInstanceComparison(oTag.columnData, sColumnId);
    });

    CS.forEach(oRelationshipTable, function (oRelationship) {
      _changeColumnSequenceOfInstanceComparison(oRelationship.columnData, sColumnId);
    });
    _changeColumnSequenceOfInstanceComparison(aAttributeColumnData, sColumnId);
    _changeColumnSequenceOfInstanceComparison(aHeaderInformationColumnData, sColumnId);
    _changeColumnSequenceOfInstanceComparison(aFixedHeaderColumnData, sColumnId);
    _triggerChange();
  };

  let _updateContentTagValues = function (aTagValueRelevanceData, sTagId) {
    let oActiveContent = ContentUtils.makeActiveContentOrVariantDirty();

    /**Set VersionableAttributeValueChanged prop*/
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedElement = oScreenProps.getReferencedElements()[sTagId];
    let oReferencedTag = oScreenProps.getReferencedTags()[sTagId];
    let bIsVersionable = (oReferencedElement && !CS.isNull(oReferencedElement.isVersionable)) ?  oReferencedElement.isVersionable : oReferencedTag.isVersionable;
    if (bIsVersionable) {
      oScreenProps.setIsVersionableAttributeValueChanged(bIsVersionable);
    }
    ContentUtils.getUpdatedEntityTag(oActiveContent, aTagValueRelevanceData, sTagId);

    _updateMandatoryElementsStatus(oActiveContent);
  };

  /**
   * @param sMode: Export content Mode
   * @param sSelectedId: Selected outbound Id
   * @description: This function Export the content based on mode selected.
   * @private
   */
  let _handleExportContentButtonClickedAccordingToMode = function (sMode, sSelectedId, oFilterContext, oSelectiveExport) {
    let aContentIdsList = [];
    let oFilterData = {};
    let oData = {};
    let oSearchCriteria = oSelectiveExport.oPostRequest.searchCriteria;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    oFilterData = oFilterStore.createFilterPostData();
    switch (sMode) {
      case "selected_content":
        let aSelectedContentIds = ContentUtils.getSelectedContentIds() || [];
        oSearchCriteria.baseEntityIIds = aSelectedContentIds.join().split(','); // convert integer array to string array.
        oSearchCriteria.exportSubType = "EXPORT_SELECTED";
        oSearchCriteria.selectedTaxonomyIds = [];
        oSearchCriteria.selectedTypes = [];
        break;

      case "current_page":
        let aContentList = ContentUtils.getEntityList();
        let aContentIds = [];
        CS.forEach(aContentList, function (oContent) {
          aContentIds.push(oContent.id);
        });
        oSearchCriteria.baseEntityIIds = aContentIds.join().split(','); // convert integer array to string array in javascript.
        oSearchCriteria.exportSubType = "EXPORT_SELECTED";
        oSearchCriteria.selectedTaxonomyIds = [];
        oSearchCriteria.selectedTypes = [];
        break;

      case "all_pages":
        oData.useCase = "exportAll";
        aContentIdsList = [];
        oSearchCriteria.selectedTaxonomyIds = [];
        oSearchCriteria.selectedTypes = [];
        oSearchCriteria.exportSubType = "EXPORT_ALL";
        break;

      case "exportWithTaxonomy":
        let oFilterProps = oFilterStore.getFilterProps();
        oSearchCriteria.selectedTaxonomyIds = oFilterProps.getSelectedTaxonomyIds();
        oSearchCriteria.selectedTypes = oFilterProps.getSelectedTypes();
        oSearchCriteria.exportSubType = "TAXONOMY_BASED_EXPORT";
        break;

      case "exportWithoutTaxonomy":
        oSearchCriteria.selectedTaxonomyIds = [];
        oSearchCriteria.selectedTypes = [];
        oSearchCriteria.exportSubType = "WITHOUT_TAXONOMY_EXPORT";
        break;

      case "exportCollectionWithChild":
        oSearchCriteria.selectedTaxonomyIds = [];
        oSearchCriteria.selectedTypes = [];
        oSearchCriteria.collectionId = oFilterContext.screenContext;
        oSearchCriteria.exportSubType = "COLLECTION_EXPORT_WITH_CHILD";
        oSearchCriteria.selectedBaseTypes = CS.map(ModuleDictionaryExport, sModule => sModule);
        break;

      case "exportCollectionWithoutChild":
        oSearchCriteria.selectedTaxonomyIds = [];
        oSearchCriteria.selectedTypes = [];
        oSearchCriteria.collectionId = oFilterContext.screenContext;
        oSearchCriteria.exportSubType = "COLLECTION_EXPORT_WITHOUT_CHILD";
        oSearchCriteria.selectedBaseTypes = CS.map(ModuleDictionaryExport, sModule => sModule);
        break;

      case "exportBookmark":
        oSearchCriteria.selectedTaxonomyIds = [];
        oSearchCriteria.selectedTypes = [];
        oSearchCriteria.bookmarkId = oFilterContext.screenContext;
        oSearchCriteria.exportSubType = "BOOKMARK_EXPORT";
        oSearchCriteria.selectedBaseTypes = CS.map(ModuleDictionaryExport, sModule => sModule);
        break;

      case "cancel":
        return;
    }

    OnboardingStore.handleExportEntityButtonClicked(oSelectiveExport);
  };

  /**
   * @param oSelectedWorkflow : Selected outbound Id
   * @description: Export contents Button click.
   * Handle for two cases
   * 1. contents to export are selected
   * 2. contents to export are not selected
   * @private
   */
  let _handleExportContentButtonClicked = function (oSelectedEndpoint, oFilterContext) {
    let aSelectedContentIds = ContentUtils.getSelectedContentIds();
    let sSelectedId = oSelectedEndpoint.code;
    let oSelectiveExport = {};
    let oPostRequest = {};
    oSelectiveExport.sUrl = getRequestMapping().SelectiveExport;
    oPostRequest.exportType = "product";
    oPostRequest.endpointId = sSelectedId;
    oPostRequest.searchCriteria = {
      selectedBaseTypes : [],
      baseEntityIIds: [],
    };
    oSelectiveExport.oPostRequest = oPostRequest;

    if (CS.isEmpty(aSelectedContentIds)) {
      let aCustomButtonData = [
        {
          id: "cancel",
          label: getTranslation().CANCEL,
          isFlat: true,
          handler: _handleExportContentButtonClickedAccordingToMode.bind(this, "cancel", sSelectedId, oFilterContext, oSelectiveExport)
        }
      ];

      if (ContentUtils.getIsStaticCollectionScreen()) {
        aCustomButtonData.push(
            {
              id: "exportCollection",
              label: getTranslation().EXPORT_ALL_CURRENT_COLLECTION,
              isFlat: false,
              handler: _handleExportContentButtonClickedAccordingToMode.bind(this, "exportCollectionWithoutChild", sSelectedId, oFilterContext, oSelectiveExport)
            },
            {
              id: "exportCollectionWithChild",
              label: getTranslation().EXPORT_ALL_CURRENT_AND_CHILD_COLLECTIONS,
              isFlat: false,
              handler: _handleExportContentButtonClickedAccordingToMode.bind(this, "exportCollectionWithChild", sSelectedId, oFilterContext, oSelectiveExport)
            });
        CustomActionDialogStore.showCustomConfirmDialog(getTranslation().COLLECTION_EXPORT_OPTIONS, '', aCustomButtonData);
      } else if (ContentUtils.getIsDynamicCollectionScreen()) {
        aCustomButtonData.push(
            {
              id: "exportBookmark",
              label: getTranslation().EXPORT_ALL_FROM_CURRENT_BOOKMARK,
              isFlat: false,
              handler: _handleExportContentButtonClickedAccordingToMode.bind(this, "exportBookmark", sSelectedId, oFilterContext, oSelectiveExport)
            });
        CustomActionDialogStore.showCustomConfirmDialog(getTranslation().BOOKMARK_EXPORT_OPTIONS, '', aCustomButtonData);
      } else {
        let oStyle = {
          width: '75%',
          maxWidth: '850px',
          borderRadius: '3px',
        };
        aCustomButtonData.push(
            {
              id: "exportWithTaxonomy",
              label: getTranslation().EXPORT_FOR_SELECTED_TAXONOMY,
              isFlat: false,
              handler: _handleExportContentButtonClickedAccordingToMode.bind(this, "exportWithTaxonomy", sSelectedId, oFilterContext, oSelectiveExport),
            },
            {
              id: "exportWithoutTaxonomy",
              label: getTranslation().EXPORT_WITHOUT_TAXONOMY,
              isFlat: false,
              handler: _handleExportContentButtonClickedAccordingToMode.bind(this, "exportWithoutTaxonomy", sSelectedId, oFilterContext, oSelectiveExport),
            },
            {
              id: "exportCurrentPageContent",
              label: getTranslation().EXPORT_CURRENT_PAGE,
              isFlat: false,
              handler: _handleExportContentButtonClickedAccordingToMode.bind(this, "current_page", sSelectedId, oFilterContext, oSelectiveExport),
            },
            {
              id: "exportAllPageContent",
              label: getTranslation().EXPORT_ALL,
              isFlat: false,
              handler: _handleExportContentButtonClickedAccordingToMode.bind(this, "all_pages", sSelectedId, oFilterContext, oSelectiveExport),
            });
        CustomActionDialogStore.showCustomConfirmDialog("Export Option",'', aCustomButtonData,oStyle);
      }
    } else {
      _handleExportContentButtonClickedAccordingToMode("selected_content", sSelectedId, oFilterContext, oSelectiveExport);
    }
  };

  let _fetchSmartDocumentConfigDetails = function () {
    let sURL = getRequestMapping().GetSmartDocuemnt;
    CS.getRequest(sURL, {}, _successFetchSmartDocumentSection, _failureFetchSmartDocumentSection);
  };

  let _successFetchSmartDocumentSection = function (oResponse) {
    let oResponseData = oResponse.success;
    let aPhysicalCatalogIds = oResponseData.physicalCatalogIds;
    let oScreenProps = ContentScreenProps.screen;
    let sSessionPortalId = ContentUtils.getSelectedPortalId();
    let bShowGenerateSmartDocumentButton = CS.includes(aPhysicalCatalogIds, sSessionPortalId) ? true : false;
    if (SessionProps.getSessionEndpointId() || (SessionProps.getSessionPhysicalCatalogId() !== PortalTypeDictionary.PIM)) {
      bShowGenerateSmartDocumentButton = false;
    }
    oScreenProps.setIsGenerateSmartDocuemntButtonVisible(bShowGenerateSmartDocumentButton);
    _triggerChange();
  };

  let _failureFetchSmartDocumentSection = function () {
    ExceptionLogger.error("Failure in fetching smart document as data has not been initialized");
  };

  let _getAllAssetExtensions = function () {
    let sURL = getRequestMapping().GetAllAssetExtensions;
    return CS.postRequest(sURL, {}, {}, _successFetchAssetExtensions, _failureFetchAssetExtensions);
  };

  let _successFetchAssetExtensions = function (oResponse) {
    let oResponseData = oResponse.success;
    let oExtensions = oResponseData.assetExtensions;
    let oScreenProps = ContentScreenProps.screen;
    let oAssetExtensions = {};
    oAssetExtensions.imageAsset = oExtensions.image_asset;
    oAssetExtensions.videoAsset = oExtensions.video_asset;
    oAssetExtensions.documentAsset = oExtensions.document_asset;
    oAssetExtensions.allAssets = oExtensions.allExtensions;
    oScreenProps.setAssetExtensions(oAssetExtensions);
  };

  let _failureFetchAssetExtensions = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureContextMenuListVisibilityToggledCallback', getTranslation());
  };

  let _handleSmartDocumentPresetButtonContentClicked = function (sSelectedPresetContentId) {
    let sUrl = getRequestMapping().GenerateSmartDocument
    let aSelectedKlassInstanceIds = ContentUtils.getSelectedContentIds();
    let sSelectedPortalId = ContentUtils.getSelectedPortalId();
    let sSelectedContentBaseType;
    if (sSelectedPortalId === PortalTypeDictionary.PIM) {
      sSelectedContentBaseType = BaseTypeDictionary.contentBaseType;
    }

    let oRequestData = {
      klassInstanceIds: aSelectedKlassInstanceIds,
      smartDocumentPresetId: sSelectedPresetContentId,
      baseType: sSelectedContentBaseType
    };
    CS.postRequest(sUrl, {}, oRequestData, successFetchSmartDocument, failureFetchSmartDocument);
  };

  let _generatePDFPreviewFromByteArray = function (aPdfBytes) {
    CS.forEach(aPdfBytes, (oPDFBytes) => {
      var sByteCharacters = atob(oPDFBytes);
      var oByteNumbers = new Array(sByteCharacters.length);
      for (var i = 0; i < sByteCharacters.length; i++) {
        oByteNumbers[i] = sByteCharacters.charCodeAt(i);
      }
      var aByteArray = new Uint8Array(oByteNumbers);

      let oFile = new Blob([aByteArray], {type: 'application/pdf'});
      let sFileURL = window.URL.createObjectURL(oFile);
      window.open(sFileURL, "_blank");
    });
  }

  let successFetchSmartDocument = function (oResponse) {
    let oResponseData = oResponse.success;
    let aPdfBytes = oResponseData.pdfBytes;
    let bShowPreview = oResponseData.showPreview;
    if (bShowPreview) {
      _generatePDFPreviewFromByteArray(aPdfBytes);
    } else {
      alertify.success(getTranslation().SMART_DOCUMENT_GENERATED_SUCCESSFULLY);
    }
    if (!CS.isEmpty(oResponse.failure)) {
      var aResponseData = oResponse.failure.exceptionDetails;
      if (aResponseData.length !== 0) {
        ContentUtils.failureCallback(oResponse, 'failureFetchSmartDocument', getTranslation());
      }
    }
    _triggerChange();
  };

  let failureFetchSmartDocument = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchSmartDocument', getTranslation());
    _triggerChange();
  };

  /**
   * @function _handleHelperLanguageSelected
   * @description Executes when helper language is selected for comparision.
   * @memberOf Stores.ContentStore
   * @param sSelectedLanguageCode - Contains Selected language code.
   * @author Ganesh More
   */
  let _handleHelperLanguageSelected = function (oSelectedItem) {
    let oScreenProps = ContentScreenProps.screen;
    let oActiveContent = ContentUtils.getActiveContent();
    let sActiveContentId = oActiveContent.id;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[sActiveContentId];
    oScreenProps.setIsHelperLanguagePopoverVisible(false);
    let sHelperLanguageCode = oSelectedItem.properties.code;
    oActiveContentMap.languageComparisonModeOn = true;
    oActiveContentMap.languageCodeToCompare = sHelperLanguageCode;
    let oCallBackData = {};
    _fetchArticleById(sActiveContentId, oCallBackData);
  };

  let _handleContextMenuListItemClicked = function (sSelectedId, sContext, oFilterContext, sSelectedAuthMappingId, sSelectedPhysicalCatlogId, sSelectedEndpointId, bIsTransferBetweenStagesEnabled,bIsRevisionableTransfer) {
    let aSelectedContentIds = ContentUtils.getSelectedContentIds();
    let sUrl = "";
    let oData = {}
    switch (sContext) {
      case 'exportContents':
        _handleExportContentButtonClicked(sSelectedId, oFilterContext);
        break;

      case 'importContents':
        let oScreenProps = ContentScreenProps.screen;
        if (SessionProps.getSessionPhysicalCatalogId() != PhysicalCatalogDictionary.DATAINTEGRATION) {
          oScreenProps.setSelectedImportEndpointCode(sSelectedId.code);
          oScreenProps.setImportEndpointSelected(true);
        }
        break;

      case 'transferContents':
        sUrl = getRequestMapping().RuntimeTransfer;
        let sAuthorizationMappingId = CS.isEmpty(sSelectedAuthMappingId) ? null : sSelectedAuthMappingId;
        if (!bIsTransferBetweenStagesEnabled) {
          sSelectedId = null;
        }

        oData = {
          ids: aSelectedContentIds,
          destinationCatalogId: sSelectedPhysicalCatlogId,
          authorizationMappingId: sAuthorizationMappingId,
          destinationOrganizationId: sSelectedId,
          moduleId: ContentUtils.getSelectedModuleId(),
          isTransferBetweenStages: bIsTransferBetweenStagesEnabled,
          destinationEndpointId: sSelectedEndpointId,
          isRevisionableTransfer: bIsRevisionableTransfer
        };
        let oCallBack = {
          filterContext: oFilterContext
        };

        CS.postRequest(sUrl, {}, oData, successTransferCallback, failureTransferCallback.bind(this, oCallBack));
        break;

      case 'createTranslation':
        _handleApplyNewDataLanguageClicked(sSelectedId);
        break;

      case "helperLanguage":
        _handleHelperLanguageSelected(sSelectedId);
        break;

      case "smartDocumentPresetContent":
        _handleSmartDocumentPresetButtonContentClicked(sSelectedId);
        break;


    }

  };

  function resetTransferDialogProps (oScreenProps) {
    oScreenProps.setExportDialogViewOpened(false);
    oScreenProps.setSelectedOrganizationId("");
    oScreenProps.setSelectedAuthorizationMappingId("");
    oScreenProps.setIsAuthMappingDisabled(true);
    oScreenProps.setSelectedPhysicalCatlogId("");
    oScreenProps.setselectedEndpointId(null);
    oScreenProps.setIsTransferBetweenStagesEnabled(false);
    oScreenProps.setIsRevisionableTransfer(false);
  }

  let _handleHandleTransferDialogClicked = (sContext, sButtonId) => {
    let oScreenProps = ContentScreenProps.screen;
    let oFilterContext = {};
    let sSelectedOrganizationId = oScreenProps.getSelectedOrganizationId();
    let sSelectedAuthMappingId = oScreenProps.getSelectedAuthorizationMappingId();
    let sSelectedPhysicalCatlogId = oScreenProps.getSelectedPhysicalCatlogId();
    let sSelectedEndpointId = oScreenProps.getselectedEndpointId();
    let bIsTransferBetweenStagesEnabled = oScreenProps.getIsTransferBetweenStagesEnabled();
    let bIsRevisionableTransfer = oScreenProps.getIsRevisionableTransfer();
    if (sButtonId === "cancel") {
      resetTransferDialogProps(oScreenProps);
    } else {
      if (sSelectedOrganizationId === 'retailer') {
        sSelectedOrganizationId = "-1";
      }
      if (CS.isEmpty(sSelectedPhysicalCatlogId)) {
        alertify.error(getTranslation().DESTINATION_CATALOGID_IS_MANDATORY);
      } else if (bIsTransferBetweenStagesEnabled && CS.isEmpty(sSelectedOrganizationId)) {
        alertify.error(getTranslation().DESTINATION_ORGANISATION_ID_IS_MANDATORY);
      } else {
        _handleContextMenuListItemClicked(sSelectedOrganizationId, sContext, oFilterContext, sSelectedAuthMappingId, sSelectedPhysicalCatlogId,sSelectedEndpointId,bIsTransferBetweenStagesEnabled,bIsRevisionableTransfer);
        resetTransferDialogProps(oScreenProps);
      }
    }
    _triggerChange();
  };

  let _handleHandleTransferDialogItemClicked = (sContext, sSelectedId) => {
    let oScreenProps = ContentScreenProps.screen;
    switch (sContext) {
      case 'organization':
        oScreenProps.setSelectedOrganizationId(sSelectedId);
        oScreenProps.setIsAuthMappingDisabled(false);
        break;
      case 'authorizationMapping':
        let sSelectedAuthorizationMappingId = oScreenProps.getSelectedAuthorizationMappingId();
        if(sSelectedAuthorizationMappingId === sSelectedId) {
          oScreenProps.setSelectedAuthorizationMappingId("");
        }else{
          oScreenProps.setSelectedAuthorizationMappingId(sSelectedId);
        }
        break;
      case 'physicalCatlog':
        oScreenProps.setSelectedPhysicalCatlogId(sSelectedId);
        oScreenProps.setselectedEndpointId(null);
        break;
      case 'dataIntegration':{
        oScreenProps.setSelectedPhysicalCatlogId(PhysicalCatalogDictionary.DATAINTEGRATION);
        oScreenProps.setselectedEndpointId(sSelectedId);
        break;
      }
    }
    _triggerChange();
  };

  let _handleTransferDialogCheckBoxToggled = (sContext) => {
    let oScreenProps = ContentScreenProps.screen;
    if(sContext !== "revisionable"){
      let bIsTransferBetweenStagesEnabled = !oScreenProps.getIsTransferBetweenStagesEnabled();
      oScreenProps.setIsTransferBetweenStagesEnabled(bIsTransferBetweenStagesEnabled);
      oScreenProps.setSelectedPhysicalCatlogId("");
      oScreenProps.setSelectedOrganizationId("");
      oScreenProps.setselectedEndpointId(null);
    }else {
      let bIsRevisionableTransfer = !oScreenProps.getIsRevisionableTransfer();
      oScreenProps.setIsRevisionableTransfer(bIsRevisionableTransfer);
    }
    _triggerChange();
  };

  var successTransferCallback = function () {
    alertify.success(getTranslation().TRANSFER_IS_INPROGRESS);
  };

  var failureTransferCallback = function (oCallBack, oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureTransferCallBack', getTranslation());
    let aExceptionDetails = oResponse.failure.exceptionDetails;
      CS.forEach(aExceptionDetails, function (oData) {
        let sKey = oData.key;
        if (sKey === "UserNotHaveTransferPermission") {
          let oScreenProps = ContentScreenProps.screen;
          let oFunctionalPermission = oScreenProps.getFunctionalPermission();
          oFunctionalPermission.canTransfer = false;
          _triggerChange();
        }
      });
  };

  let _handleContextMenuListVisibilityToggled = function (bIsVisibility, sContext) {
    let oScreenProps = ContentScreenProps.screen;

    switch (sContext) {
      case "helperLanguage":
        let bIsPopoverVisible = oScreenProps.getIsHelperLanguagePopoverVisible();
        if (bIsPopoverVisible) {
          oScreenProps.setIsHelperLanguagePopoverVisible(false);
          _triggerChange();
        }
        break;

      case 'createTranslation':
        let bIsCreateTranslationPopoverVisible = oScreenProps.getIsCreateTranslationPopoverVisible();
        if (bIsCreateTranslationPopoverVisible) {
          oScreenProps.setIsCreateTranslationPopoverVisible(false);
          _triggerChange();
        }
        break;

      default:
        oScreenProps.setExportContentsPopoverVisibility(bIsVisibility);
        oScreenProps.setImportContentsPopoverVisibility(bIsVisibility);
        oScreenProps.setOrganizationsPopoverVisibility(bIsVisibility);
        oScreenProps.setSmartDocumentPresetsPopoverVisibility(bIsVisibility);
        _triggerChange();
    }
  };

  /**
   * TODO: Currently pagination is not handled properly size is set to 99999 which needs to be changed after handling of load more
   * @private
   */
  let _getOrganizationList = function(){
    let oPaginationData = {
      from: 0,
      size: 99999,
      sortBy: "label",
      sortOrder: "asc",
      types: [],
      searchColumn: "label",
      searchText: "",
    };
    let oRequestResponseInfoData = {
      requestType: "customType",
      requestURL: "config/organizations/getall"
    };
    CS.postRequest(oRequestResponseInfoData.requestURL, {}, oPaginationData, successOrganizationList, failureOrganizationList)
  };

  let successOrganizationList  = function(oResponse) {
    let oScreenProps = ContentScreenProps.screen;
    let alist = oResponse.success.list;
    oScreenProps.setOrganizationListForExportDialog(alist);

    _triggerChange();
  };

  let failureOrganizationList  = function(oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureEndpointList', getTranslation());
  };

  let _getAuthMappingList = function() {
    let oAuthorizationMapping = {};
    let oPostData = {};
    let sRequestURL = "config/configdata";
    oAuthorizationMapping["authorizationMappings"] = {
      from: 0,
      size: 20,
      sortBy: "label",
      sortOrder: "asc",
      types: [],
      typesToExclude: []
    };
  oPostData.entities = oAuthorizationMapping;
  oPostData.searchColumn = "label";
  oPostData.searchText = "";

    CS.postRequest(sRequestURL, {}, oPostData, successAuthorizationMappingList, failureAuthorizationMappingList)
  };

  let successAuthorizationMappingList  = function(oResponse) {
    let oScreenProps = ContentScreenProps.screen;
    let oAuthorizationMappingList = oResponse.success.authorizationMappings;
    let alist = oAuthorizationMappingList.list;
    oScreenProps.setAuthorizationMappingListForExportDialog(alist);
    _triggerChange();
  };

  let failureAuthorizationMappingList  = function(oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureAuthorizationMappingList', getTranslation());
  };

  /**To fetch inbound endpoints**/
  let _getInboundEndpointList = function () {
    let oEndpoints = {};
    let oPostData = {};
    let sRequestURL = "config/configdata";
    oEndpoints["endpoints"] = {
      from: 0,
      size: 20,
      sortBy: "label",
      sortOrder: "asc",
      types: [EndpointTypeDictionary.INBOUND_ENDPOINT],
      typesToExclude: [],
      physicalCatalogId: PhysicalCatalogDictionary.DATAINTEGRATION
    };
    oPostData.entities = oEndpoints;
    oPostData.searchColumn = "label";
    oPostData.searchText = "";
    CS.postRequest(sRequestURL, {}, oPostData, successInboundEndpointList, failureInboundEndpointList);
  };

  let successInboundEndpointList = function (oResponse) {
    let aEndpointList = oResponse.success.endpoints.list;
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setInboundEndpointListForExportDialog(aEndpointList);
    _triggerChange();
  };

  let failureInboundEndpointList = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureInboundEndpointList', getTranslation());
  };

  /**To fetch outbound endpoints with user permission**/
  let _getOutboundEndpointListWithUserPermission = function () {
    let oCurrentUser = ContentUtils.getCurrentUser();
    let sUrl = ContentScreenRequestMapping.GetEndpointsAccordingToUser;
    let oData = {
      "from": 0,
      "searchColumn": "label",
      "searchText": "",
      "size": 20,
      "sortBy": "label",
      "sortOrder": "asc",
    };
    CS.postRequest(sUrl, {userid: oCurrentUser.id}, oData, successOutboundEndpointListWithUserPermission, failureOutboundEndpointListWithUserPermission);
  };

  let successOutboundEndpointListWithUserPermission = function (oResponse) {
    let aOutboundEndpointList = oResponse.success;
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setOutboundEndpointListForExportDialog(aOutboundEndpointList);
    _triggerChange();
  };

  let failureOutboundEndpointListWithUserPermission = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureOutboundEndpointListWithUserPermission', getTranslation());
  };

  var _handleContextMenuButtonClicked = function (sContext, oFilterContext) {
    let oScreenProps = ContentScreenProps.screen;
    let bDoTrigger = true;
    switch (sContext) {
      case 'smartDocumentPresetContent':
        oScreenProps.setSmartDocumentPresetsPopoverVisibility(true);
        break;

      case 'exportContents':
        oScreenProps.setExportContentsPopoverVisibility(true);
        break;

      case 'importContents':
        oScreenProps.setImportContentsPopoverVisibility(true);
        break;

      case 'transferContents':
        let oCurrentUser = GlobalStore.getCurrentUser();
        let oDataIntegrationInfo = ContentUtils.getDataIntegrationInfo();
        let bIsStandardOrganization = oCurrentUser.organizationId === "-1";
        let bIsOutboundEndpoint = oDataIntegrationInfo.endpointType === EndpointTypeDictionary.OUTBOUND_ENDPOINT;
        bIsStandardOrganization && _getOrganizationList();
        _getAuthMappingList();
        _getInboundEndpointList();
        _getOutboundEndpointListWithUserPermission();
        oScreenProps.setExportDialogViewOpened(true);
        bIsOutboundEndpoint && oScreenProps.setIsTransferBetweenStagesEnabled(true);
        break;

      case "helperLanguage":
        if (ContentUtils.isActiveContentDirty(oFilterContext)) {
          alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
          return;
        }
        let oActiveContent = ContentUtils.getActiveContent();
        let sActiveContentId = oActiveContent.id;
        let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
        let oActiveContentMap = oActiveEntitySelectedTabIdMap[sActiveContentId];
        let bLanguageComparisonModeOn = oActiveContentMap.languageComparisonModeOn;
        oScreenProps.setIsHelperLanguagePopoverVisible(!bLanguageComparisonModeOn);

        if (bLanguageComparisonModeOn) {
          bDoTrigger = false;
          oActiveContentMap.languageComparisonModeOn = false;
          oActiveContentMap.languageCodeToCompare = "";
          let oCallbackData = {};
          _fetchArticleById(sActiveContentId, oCallbackData);
        }
        break;

      case 'createTranslation':
        if (ContentUtils.isActiveContentDirty(oFilterContext) || ContentGridProps.getIsGridDataDirty()) {
          alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
          return;
        }
        let bIsCreateTranslationPopoverVisible = oScreenProps.getIsCreateTranslationPopoverVisible();
        oScreenProps.setIsCreateTranslationPopoverVisible(!bIsCreateTranslationPopoverVisible);

    }
    if (bDoTrigger) {
      _triggerChange();
    }
  };

  let _handleContentKPITileOpenDialogButtonClicked = function (sTileId) {
    let oScreenProps = ContentScreenProps.screen;
    let oProcessedContentKPIDataMap = oScreenProps.getProcessedContentKPIDataMap();
    oScreenProps.setActiveKPIObject(oProcessedContentKPIDataMap[sTileId]);
    _triggerChange();
  };

  let _handleContentKPITileCloseDialogButtonClicked = function () {
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setActiveKPIObject({});
    _triggerChange();
  };

  let _handleKpiChartTypeSelected = function (sKpiTypeId) {
    ContentScreenProps.screen.setSelectedKPIChartType(sKpiTypeId);
    _triggerChange();
  };

  let setConflictingSources = function (oConflictingSources, oAllConflictingSources, bShowConflictSourcesOnly, bIsConflictResolved, aKlasses, aRelationships, aTaxonomies, oVariantContext, aContents, aLanguages) {
    let sSplitter = ContentUtils.getSplitter();
    CS.forEach(oAllConflictingSources, function (oConflictingValue) {
      let sType;
      let sId;
      let sContentId;
      let oSource;
      if (oConflictingValue.source) {
        //To get conflicting value (getting from klassInstance)
        oSource = oConflictingValue.source;
        sType = oSource.type;
        sId = oSource.id;
        sContentId = oSource.contentId;
      } else {
        //To get conflicting source only not value (getting from reference Element)
        sType = oConflictingValue.sourceType;
        sId = oConflictingValue.id;
      }
      let oContext;
      let oContent;
      switch (sType) {
        case "klass":
        case ConflictingValuesSourceDictionary.KLASS_SOURCE:
          let aKlass = CS.find(aKlasses, {id: sId});
          if (aKlass) {
            oConflictingSources[sId] = {
              label: CS.getLabelOrCode(aKlass),
              conflictOccursIn: "inClass"
            };
          }
          break;

        case ConflictingValuesSourceDictionary.RELATIONSHIP_SOURCE:
        case "relationshipInheritance":
          let oRelationship = CS.find(aRelationships, {id: sId});
          if (!CS.isEmpty(oRelationship)) {
            /** In reference elements source will not be there **/
            let sConflictingSources = sId;
            let sLabel = CS.getLabelOrCode(oRelationship);
            if (!CS.isEmpty(oSource)) {
              oContent = CS.find(aContents, {id: oSource.contentId});
              if (!CS.isEmpty(oContent)) {
                sLabel = ContentUtils.getContentName(oContent);
                if (!CS.isEmpty(oRelationship)) {
                  sLabel += " (" + CS.getLabelOrCode(oRelationship) + ")";
                }
                sConflictingSources = CS.isNotEmpty(oSource.contentId) ? oSource.contentId + sSplitter + oSource.id : oSource.id;
              }
            }

            oConflictingSources[sConflictingSources] = {
              label: sLabel,
              conflictOccursIn: "inRelationship"
            };
          }
          break;

        case "relationship":
          let oRelationships = CS.find(aRelationships, {id: sId});
          if (oRelationships) {
            oConflictingSources[sId] = {
              label: CS.getLabelOrCode(oRelationships),
              conflictOccursIn: "inRelationship"
            };
          }
          break;

        case "taxonomy":
        case ConflictingValuesSourceDictionary.TAXONOMY_SOURCE:
          let oTaxonomy = CS.find(aTaxonomies, {id: sId});
          if (oTaxonomy) {
            oConflictingSources[sId] = {
              label: CS.getLabelOrCode(oTaxonomy),
              conflictOccursIn: "inTaxonomy"
            };
          }
          break;

        case "context":
          oContext = CS.find(oVariantContext, {id: sId});
          if (oContext) {
            oConflictingSources[sId] = {
              label: CS.getLabelOrCode(oContext),
              conflictOccursIn: "inContext"
            };
          }
          break;

        case ConflictingValuesSourceDictionary.CONTEXT_SOURCE:
          let oContextClassRuntime = CS.find(oVariantContext, {id: sId});

          // below case handles embedded config changes in conflict sources
          let oContextClassConfig = CS.find(aContents, {id: sContentId});

          oContext = oContextClassRuntime || oContextClassConfig;
          if (!CS.isEmpty(oContext)) {
            let sConflictingSources = sId;
            let sLabel = CS.getLabelOrCode(oContext);
            if (!CS.isEmpty(oSource)) {
              oContent = CS.find(aContents, {id: sContentId});
              if (!CS.isEmpty(oContent)) {
                sLabel = ContentUtils.getContentName(oContent);
                if (!CS.isEmpty(oContext)) {
                  sLabel += " (" + CS.getLabelOrCode(oContext) + ")";
                }
                sConflictingSources = bIsConflictResolved ? oSource.id : oSource.contentId + sSplitter + oSource.id;
              }
            }

            oConflictingSources[sConflictingSources] = {
              label: CS.getLabelOrCode(oContext),
              conflictOccursIn: "inContext"
            };
          }
          break;


        case  ConflictingValuesSourceDictionary.LANGUAGE_SOURCE:
          let oLanguage = CS.find(aLanguages, {code: sId});

          if (!CS.isEmpty(oLanguage)) {
            let sLabel = CS.getLabelOrCode(oLanguage);
            let sConflictingSourceId = CS.isNotEmpty(oSource.contentId) ? oSource.contentId + sSplitter + oSource.id : oSource.id;
            oConflictingSources[sConflictingSourceId] = {
              label: sLabel,
              conflictOccursIn: "inLanguage"
            }
          }
      }
    });
  };

  let successGetPossibleConflictingSourcesCallback = function (sElementId, oCallback, bDoNotTrigger, bShowConflictSourcesOnly, bIsConflictResolved, oResponseData) {
    let oResponse = oResponseData.success;
    let oScreenProps = ContentScreenProps.screen;

    let oReferencedElements = oScreenProps.getReferencedElements();

    let oReferencedElement = oReferencedElements[sElementId];
    let aKlasses = oResponse.klasses;
    let aTaxonomies = oResponse.taxonomies;
    let aRelationships = oResponse.relationships;
    let aContents = oResponse.contents;
    let oVariantContext = oResponse.variantContexts;
    let aLanguages = oResponse.languages;
    let oConflictingSources = {};

    let oConflictingElement = {};
    let oActiveContent = ContentUtils.getActiveContentOrVariant();
    if (oReferencedElement.type === "attribute") {
      oConflictingElement = CS.find(oActiveContent.attributes, {attributeId: sElementId});
    } else if (oReferencedElement.type === "tag") {
      oConflictingElement = CS.find(oActiveContent.tags, {tagId: sElementId});
    } else if (oReferencedElement.type === "relationship") {
      oConflictingElement = CS.find(oActiveContent.contentRelationships, {sideId: sElementId});
    }
    /**To Show sources name in conflicting sources information (i.e. coupling icon and conflicting icon) on elements*/
    let aConflictingSources = CS.union(oReferencedElement.conflictingSources, oConflictingElement.conflictingValues);
    setConflictingSources(oConflictingSources, aConflictingSources, bShowConflictSourcesOnly,
        bIsConflictResolved, aKlasses, aRelationships, aTaxonomies, oVariantContext, aContents, aLanguages);
    let oOldConflictingSources = oScreenProps.getConflictingSources();
    bShowConflictSourcesOnly ? oScreenProps.setAllPossibleConflictingSources(oConflictingSources) : oScreenProps.setConflictingSources(CS.combine(oOldConflictingSources, oConflictingSources));


    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }

    if (!bDoNotTrigger) {
      _triggerChange();
    }
  };

  let failureGetPossibleConflictingSourcesCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureGetPossibleConflictingSourcesCallback', getTranslation());
  };

  let putRequestDataForConflictingSource = function (oData, oAllConflictingSources, bShowConflictSourcesOnly, bIsConflictResolved) {
    let oActiveEntity = _getActiveEntity();
    CS.forEach(oAllConflictingSources, function (oConflictingSources) {
      let sType;
      let sId;
      let oSource;
      if (oConflictingSources.source) {
        oSource = oConflictingSources.source;
        sType = oSource.type;
        sId = oSource.id;
      } else {
        sType = oConflictingSources.sourceType;
        sId = oConflictingSources.id;
      }

      let [aEntityArray, aContentArray] = [[], []];

      switch (sType) {
        case "klass":
        case ConflictingValuesSourceDictionary.KLASS_SOURCE:
          aEntityArray = oData.klasses;
          break;

        case "relationship":
          aEntityArray = oData.relationships;
          break;

        case "relationshipInheritance":
          aEntityArray = oData.relationships;
          aContentArray = oData.contents;
          break;

        case ConflictingValuesSourceDictionary.RELATIONSHIP_SOURCE:
          aEntityArray = oData.relationships;
          aContentArray = oData.contents;
          break;

        case "taxonomy":
        case ConflictingValuesSourceDictionary.TAXONOMY_SOURCE:
          aEntityArray = oData.taxonomies;
          break;

        case "context":
          aEntityArray = oData.variantContexts;
          break;

        case ConflictingValuesSourceDictionary.CONTEXT_SOURCE:
          aEntityArray = oData.variantContexts;
          aContentArray = oData.contents;
          break;

        case ConflictingValuesSourceDictionary.LANGUAGE_SOURCE:
          aEntityArray = oData.languages;
          break;
      }

      if (sType === ConflictingValuesSourceDictionary.CONTEXT_SOURCE) {
        if (!(oActiveEntity.klassInstanceId === oSource.contentId && oActiveEntity.isEmbedded)) {
          !CS.includes(aEntityArray, sId) && aEntityArray.push(sId);
        }
      } else {
        !CS.includes(aEntityArray, sId) && aEntityArray.push(sId);
      }
      oSource && oSource.contentId && !CS.includes(aContentArray, oSource.contentId) && aContentArray.push(oSource.contentId);
    });
  };

  let _getRequestDataForGettingConflictingSources = function (sElementId, bShowConflictSourcesOnly, bIsConflictResolved) {
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedElements = oScreenProps.getReferencedElements();
    let oReferencedElement = oReferencedElements[sElementId];
    let oData = {
      klasses: [],
      taxonomies: [],
      relationships: [],
      contents: [],
      variantContexts: [],
      languages: []
    };

    let oConflictingElement = {};
    let oActiveContent = ContentUtils.getActiveContentOrVariant();
    if (oReferencedElement.type === "attribute") {
      oConflictingElement = CS.find(oActiveContent.attributes, {attributeId: sElementId});
    } else if (oReferencedElement.type === "tag") {
      oConflictingElement = CS.find(oActiveContent.tags, {tagId: sElementId});
    } else if (oReferencedElement.type === "relationship") {
      let oRefrencedElements = CS.find(oScreenProps.getActiveSections(), {id: sElementId});
      oReferencedElement = CS.find(oRefrencedElements.elements, {id: sElementId});
      oConflictingElement = CS.find(oActiveContent.contentRelationships, {sideId: sElementId})
    }
    let oAllConflictingSources =  CS.union(oReferencedElement.conflictingSources, oConflictingElement.conflictingValues);
    putRequestDataForConflictingSource(oData, oAllConflictingSources, bShowConflictSourcesOnly, bIsConflictResolved);

    return oData;
  };

  let _getPossibleConflictingSources = function (sElementId, bDisableLoader, bShowConflictSourcesOnly, bIsConflictResolved, bDoNotTrigger, oCallback) {
    let sUrl = getRequestMapping().GetPossibleConflictingSources;
    let oData = _getRequestDataForGettingConflictingSources(sElementId, bShowConflictSourcesOnly, bIsConflictResolved);
    let fSuccess = successGetPossibleConflictingSourcesCallback.bind(this, sElementId, oCallback, bDoNotTrigger, bShowConflictSourcesOnly, bIsConflictResolved);
    let fFailure = failureGetPossibleConflictingSourcesCallback;

    CS.postRequest(sUrl, {}, oData, fSuccess, fFailure, bDisableLoader);
  };

  let _handleSectionElementConflictIconClicked = function (sElementId, bDisableLoader, bShowConflictSourcesOnly, bIsConflictResolved, bDoNotTrigger, oCallback) {
    let oScreenProps = ContentScreenProps.screen;
    let oElementsHavingConflictingValues = oScreenProps.getElementsHavingConflictingValues();
    oElementsHavingConflictingValues[sElementId].isExpanded = !oElementsHavingConflictingValues[sElementId].isExpanded;

    if (oElementsHavingConflictingValues[sElementId].isExpanded) {
      _getPossibleConflictingSources(sElementId, bDisableLoader, bShowConflictSourcesOnly, bIsConflictResolved, bDoNotTrigger, oCallback);
    } else {
      _triggerChange();
    }
  };

  let _handleRestoreContentsButtonClicked = function (aContentIdsToRestore) {
    let oAppData = ContentUtils.getAppData();
    let aContentList = oAppData.getContentList();

    let oBaseTypeMap = {};
    CS.forEach(aContentIdsToRestore, function (sContentId) {
      let oContent = CS.find(aContentList, {id: sContentId});
      let aIds = oBaseTypeMap[oContent.baseType] || [];
      aIds.push(oContent.id);
      oBaseTypeMap[oContent.baseType] = aIds;
    });

    let aPromises = [];

    CS.forEach(oBaseTypeMap, (aIds, sBaseType) => {
      let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(sBaseType);
      let oRequestData = {
        ids: aIds
      };
      aPromises.push(CS.postRequest(getRequestMapping(sScreenMode).restoreContents, {}, oRequestData,
          (oResponse) => {
            return oResponse
          },
          (oResponse) => {
            return oResponse
          }));
    });

    Promise.all(aPromises)
        .then(successRestoreContentsCallback.bind(this, aContentIdsToRestore), failureRestoreContentsCallback)
        .then(CommonUtils.refreshCurrentBreadcrumbEntity)
        .catch(failureRestoreContentsCallback);
  };

  let successRestoreContentsCallback = function (aIdsToRestore, aResponses) {
    let bAnySuccess = false;
    let aNames = [];
    //Todo Check with core weather to display exceptions on PIM Content Restore
    CS.forEach(aResponses, function (oResponse) {
      if (!CS.isEmpty(oResponse.failure)) {
        aNames = [...aNames, ..._alertifyInstanceExistsExceptions(oResponse.failure)];
      }
      if (!bAnySuccess && !CS.isEmpty(oResponse.success)) {
        bAnySuccess = true;
      }
      if (oResponse.success === null) {
        bAnySuccess = true;
      }
    });

    if (!CS.isEmpty(aNames)) {
      let sNamesToPrint = aNames.join(' ,');
      alertify.error(`${getTranslation().SELECTED_CONTENTS_ALREADY_EXISTS_AT_THE_TARGET_LOCATION} ${sNamesToPrint}`);
    }
    if (bAnySuccess) {
      alertify.success(getTranslation().RESTORATION_IN_PROGRESS);
    }
  };

  let _alertifyInstanceExistsExceptions = function (oFailure) {
    let oExceptions = oFailure.exceptionDetails;
    let aNames = [];
    if (!CS.isEmpty(oExceptions)) {
      aNames = [...aNames, ...CS.map(oExceptions, "itemName")];
    }
    return aNames;
  };

  let failureRestoreContentsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureRestoreContentsCallback', getTranslation());
  };

  let _setContentSelectedImageId = function (sSelectedImageID) {
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setSelectedImageId(sSelectedImageID);
  };

  let _changeButtonActiveState = function (sButtonId) {
    ContentScreenProps.screen.updateButtonsState(sButtonId);
  };

  let _handleContentDataLanguageChanged = function (sLanguageCode, oFilterContext) {
    let oActiveEntity = ContentScreenProps.screen.getActiveContent();
    let oTabItems = ContentScreenConstants.tabItems;
    CommonUtils.setSelectedDataLanguage(sLanguageCode);
    SharableURLStore.addLanguageParamsInWindowURL("", sLanguageCode);
    let oArticleDetailsCallBack = {
      filterContext: oFilterContext
    };
    let sSelectedTabId = ContentUtils.getSelectedTabId();
    ContentScreenProps.tableViewProps.resetLanguageDependentFilters(oActiveEntity.id);

    //Extra Handling for Virtual Tabs
    if (CS.includes([oTabItems.TAB_DUPLICATE_ASSETS], sSelectedTabId)) {
      oArticleDetailsCallBack.functionToExecute = _handleEntityTabClicked.bind(this, sSelectedTabId)
    }

    let oCallbackData = {
      functionToExecute: _getArticleDetails.bind(this, oActiveEntity.id, oArticleDetailsCallBack)
    };
    HomeScreenCommunicator.handleContentDataLanguageChanged(oCallbackData);
  };

  let _deleteCurrentLanguageContent = function () {
    let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    _handleDeleteTranslationClicked(sSelectedDataLanguageCode);
  };

  let _handleDeleteTranslationClicked = function (sLanguageCode, oFilterContext) {
    let oActiveContent = ContentUtils.getActiveContent();
    let aDataLanguages = ContentUtils.getLanguageInfo().dataLanguages;
    let oDataLanguage = CS.find(aDataLanguages, {code: sLanguageCode});
    if (oActiveContent.creationLanguage === sLanguageCode) {
      alertify.error(getTranslation().CREATION_LANGUAGE_CONTENT_ERROR);
      return;
    }
    let oRequestData = {
      contentId: oActiveContent.id,
      languageCodes: [sLanguageCode]
    };
    let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveContent.baseType);
    let oCallback = {
      languageCode: sLanguageCode,
      filterContext: oFilterContext
    }
    CustomActionDialogStore.showConfirmDialog(oDataLanguage.label,
        getTranslation().DELETE_CONTENT_TRANSLATION_CONFIRMATION,
        function () {
          CS.deleteRequest(getRequestMapping(sScreenMode).DeleteArticleTranslation, {}, oRequestData, successDeleteContentTranslationCallback.bind(this, oCallback), failureDeleteContentTranslationCallback);
        }, function (oEvent) {
        });
  };

  let successDeleteContentTranslationCallback = function (oCallback, OResponse) {
    let oActiveContent = ContentUtils.getActiveContent();
    let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    alertify.success(ContentUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().TRANSLATION}));
    if (oCallback.languageCode === sSelectedDataLanguageCode) {
      _handleContentDataLanguageChanged(oActiveContent.creationLanguage, oCallback.filterContext);
      return;
    } else {
      CS.pull(oActiveContent.languageCodes, oCallback.languageCode);
      _triggerChange();
    }
  };

  let failureDeleteContentTranslationCallback = function (OResponse) {
    ContentUtils.failureCallback(OResponse, 'failureCreateContentCallback', getTranslation());
  };

  var _removeMultipleElementFromSelectedRelationshipProps = function (sRelationshipSideId) {
    ContentScreenProps.relationshipView.getSelectedRelationshipElements()[sRelationshipSideId] = [];
  };

  var _handleRemoveSelectedContentGridEntities = function (sRelationshipSideId, sRelationshipType, aGridSelectedContentIds, fSuccessHandler) {
    let oRelationshipViewProps = ContentScreenProps.relationshipView;
    let oRelationshipToolbarProps = oRelationshipViewProps.getRelationshipToolbarProps();
    let aBulkGridSelectedContentNames = [];

    let aRelationshipElements = oRelationshipToolbarProps[sRelationshipSideId] && oRelationshipToolbarProps[sRelationshipSideId].elements || [];
    let sRelationshipId = ContentUtils.getRelationshipIdFromSideId(sRelationshipSideId);
    CS.forEach(aGridSelectedContentIds, function (sElementContentId) {
      let oSelectedRelationshipElement = CS.find(aRelationshipElements, {id: sElementContentId});
      oSelectedRelationshipElement && aBulkGridSelectedContentNames.push(oSelectedRelationshipElement.name);
    });

    ContentUtils.listModeConfirmation(getTranslation().STORE_ALERT_CONFIRM_UNLINK, aBulkGridSelectedContentNames, function () {
      let oActiveEntity = ContentUtils.makeActiveContentDirty();
      let aDeletedElementsInRelationship = [];
      let sKeyToFindRelationship = ContentUtils.getContentRelationshipKeyById(sRelationshipId);

      let aActiveEntityRelationships = oActiveEntity[sKeyToFindRelationship];
      let aNewClonedRelationship = CS.cloneDeep(aActiveEntityRelationships);
      let oClonedRelationship = CS.find(aNewClonedRelationship, {sideId: sRelationshipSideId});
      if (CS.isEmpty(oClonedRelationship)) {
        oClonedRelationship = CS.find(aNewClonedRelationship, {id: sRelationshipSideId});
      }
      let aRelationshipElements = oClonedRelationship.elementIds;
      CS.forEach(oClonedRelationship.elementIds, function (sId) {
        if (CS.includes(aGridSelectedContentIds, sId)) {
          aDeletedElementsInRelationship.push(sId);
        }
      });
      CS.forEach(aDeletedElementsInRelationship, function (sId) {
        CS.pull(aRelationshipElements, sId);
      });
      oClonedRelationship.elementIds = aRelationshipElements;

      let oSaveContentCallback = {};
      oSaveContentCallback.funcRemoveRelationshipElementFromSelectedProp = _removeMultipleElementFromSelectedRelationshipProps.bind(this, sRelationshipSideId);
      ContentRelationshipStore.saveRelationship(aActiveEntityRelationships, aNewClonedRelationship, sKeyToFindRelationship == "natureRelationships",  oSaveContentCallback, fSuccessHandler).then(function () {
      });
    }, function (oEvent) {
    });
    _triggerChange();
  };

  let _handleGridViewSelectButtonClicked = (aSelectedContentIds, bSelectAllClicked) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oGridViewSelectedContentBySectionIds = oComponentProps.relationshipView.getSelectedRelationshipElements();
    let oSelectedContext = oComponentProps.screen.getSelectedContext();
    let sCurrentRelationshipOrSideId = oSelectedContext.sectionId;
    if (!oGridViewSelectedContentBySectionIds[sCurrentRelationshipOrSideId]) {
      oGridViewSelectedContentBySectionIds[sCurrentRelationshipOrSideId] = [];
    }
    if (bSelectAllClicked) {
      oGridViewSelectedContentBySectionIds[sCurrentRelationshipOrSideId] = aSelectedContentIds;
    } else {
      oGridViewSelectedContentBySectionIds[sCurrentRelationshipOrSideId] = CS.xor(oGridViewSelectedContentBySectionIds[oSelectedContext.sectionId], aSelectedContentIds);
    }
    _triggerChange();
  };

  let _handleApplyNewDataLanguageClicked = (oSelectedItem) => {
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setIsCreateTranslationPopoverVisible(false);

    let aDataLanguages = SessionProps.getLanguageInfoData().dataLanguages;
    let oSelectedLanguage = CS.find(aDataLanguages, {id: oSelectedItem.id});
    if (!oSelectedLanguage) {
      aDataLanguages.push(oSelectedItem);
    }
    CommonUtils.setSelectedDataLanguage(oSelectedItem.code);
    SharableURLStore.addLanguageParamsInWindowURL("", oSelectedItem.code);

    let sSelectedTabId = ContentUtils.getSelectedTabId();
    let oActiveContent = ContentUtils.getActiveContent();
    let sTypeId = null;
    let oSelectedTemplate = oScreenProps.getSelectedTemplate();
    let templateObj = {};
    if (oSelectedTemplate) {
      templateObj = ContentUtils.getTemplateAndTypeIdForServer(oSelectedTemplate.id);
    }
    sTypeId = templateObj.typeId;
    let iSize = oScreenProps.getDefaultPaginationLimit();
    let iStartIndex = oScreenProps.getPaginatedIndex();
    let sTabType = ContentUtils.getTabTypeFromTabId(sSelectedTabId);
    let oKlassInstanceTreeInfo = ContentUtils.getEmptyFilterObject(iSize, iStartIndex);
    oKlassInstanceTreeInfo["variantInstanceId"] = null;

    let oRequestObj = {
      id: oActiveContent.id,
      tabId: sSelectedTabId,
      tabType: sTabType,
      typeId: sTypeId,
      getKlassInstanceTreeInfo: oKlassInstanceTreeInfo,
    };
    let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveContent.baseType);
    let sUrl = getRequestMapping(sScreenMode).CreateTranslatableArticleInstance;
    let oSaveContentsCallback = {};
    let fSuccess = successSaveContentsCallback.bind(this, oSaveContentsCallback);
    let fFailure = failureSaveContentsCallback;
    let oCallbackData = {};
    oCallbackData.functionToExecute = function () {
      return CS.postRequest(sUrl, {}, oRequestObj, fSuccess, fFailure);
    };
    HomeScreenCommunicator.handleContentDataLanguageChanged(oCallbackData);
  };

  /**
   *@function _dataLanguagePopoverVisibilityToggled
   * @description Used to handle data language dropdown visibility.
   * @memberOf Stores.ContentStore
   */
  let _dataLanguagePopoverVisibilityToggled = function (oFilterContext) {
    if (ContentUtils.isActiveContentDirty(oFilterContext) || ContentGridProps.getIsGridDataDirty()) {
      alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return;
    }
    let oScreenProps = ContentScreenProps.screen;
    let isDataLanguagePopoverVisible = oScreenProps.getIsDataLanguagePopoverVisible();
    oScreenProps.setIsDataLanguagePopoverVisible(!isDataLanguagePopoverVisible);
    _triggerChange();
  };

  let _handleKpiSummaryViewKpiSelected = function (sKPIId) {
    let oScreenProps = ContentScreenProps.screen;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContent = _getActiveEntity();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[oActiveContent.id];
    oActiveContentMap["selectedKPIId"] = sKPIId;
  };

  let _handleEntityESCButtonClicked = function () {
    let aBreadcrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    let iIndex = aBreadcrumb.length - 2;
    let oItem = aBreadcrumb[iIndex];
    if (!CS.isEmpty(oItem)) {
      CS.navigateBack();
    }
  };

  let _handleRelationshipConflictPropertyResolved = function(oSelectedConflictValue, oRelationship) {
    let oScreenProps = ContentScreenProps.screen;
    let oActiveContent = oScreenProps.getActiveContent();
    let oReferencedElements = oScreenProps.getReferencedElements();
    let oReferencedElement = oReferencedElements[oRelationship.sideId] || oReferencedElements[oRelationship.id];
    let aRelationshipConflictingValues = oActiveContent.relationshipConflictingValues;
    let oActiveRelationshipConflictingValues = CS.find(aRelationshipConflictingValues, {propagableRelationshipId: oRelationship.relationshipId, propagableRelationshipSideId: oRelationship.sideId});
    let aConflicts = oActiveRelationshipConflictingValues.conflicts;

    let oSource = oSelectedConflictValue.source;
    let sSelectedRelationshipId = oSource.id;

    let oSelectedConflictingValue = CS.find(aConflicts, {relationshipId: sSelectedRelationshipId, sourceContentId: oSource.contentId});
    let oResolvedConflicts = CS.cloneDeep(oActiveRelationshipConflictingValues);
    oResolvedConflicts.conflicts = [oSelectedConflictingValue];

    let sSelectedTabId = ContentUtils.getSelectedTabId();
    let oSelectedTemplate = oScreenProps.getSelectedTemplate();

    let templateObj = {};
    if (oSelectedTemplate) {
      templateObj = ContentUtils.getTemplateAndTypeIdForServer(oSelectedTemplate.id);
    }

    let oPostData = {
      targetId: oActiveContent.id,
      targetBaseType: oActiveContent.baseType,
      tabId: sSelectedTabId,
      tabType: ContentUtils.getTabTypeFromTabId(sSelectedTabId),
    };


    oPostData.resolvedConflicts = [oResolvedConflicts];
    oPostData.typeId = templateObj.typeId;
    oPostData.templateId = templateObj.templateId;

    CS.postRequest(ContentScreenRequestMapping.ResolveRelationshipinheritanceconflict, {}, oPostData, successRelationshipConflictPropertyResolvedCallback.bind(this, oSelectedConflictValue), failureRelationshipConflictPropertyResolvedCallback);
  };

  let successRelationshipConflictPropertyResolvedCallback = function (oSelectedConflictValue, oResponse) {
    let oKlassInstance = oResponse.success.klassInstance;

    let aPayloadData = [oKlassInstance.id, {entityType: oKlassInstance.baseType}];
    let oCallbackData = {};
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem("", "", "", "", {}, {}, "", aPayloadData, _fetchArticleById);
    alertify.success(ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity : getTranslation().CONTENT } ));
    if (oSelectedConflictValue.isChecked) {
      oSelectedConflictValue.isResolved = true;
    }
    successFetchArticleById(oCallbackData, oResponse);
    _triggerChange();
  };

  let failureRelationshipConflictPropertyResolvedCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse,'failureRelationshipConflictPropertyResolvedCallback',getTranslation());
  };

  let _handleSelectedTemplateChanged = function (oSelectedTemplate) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oTimeLineProps = oComponentProps.timelineProps;
    oScreenProps.setSelectedTemplate(oSelectedTemplate);
    oTimeLineProps.setIsArchiveVisible(false);
    let oCallbackData = {};
    /*oCallbackData.functionToExecute = function (oCallbackData) {
      ContentUtils.addNewBreadCrumbItem(oCallbackData);
    };*/
    _fetchArticleById(oActiveEntity.id, oCallbackData);
  };

  let _handleTaxonomyAdded = function (sTaxonomyId, sTaxonomyType, oFilterContext) {
    if(sTaxonomyType === TaxonomyTypeDictionary.MINOR_TAXONOMY){
      var oTaxonomyObject = {
        addedTaxonomyIds: [sTaxonomyId]
      };
      let oCallbackData = {
        filterContext: oFilterContext
      };
      _handleApplyClassification(oTaxonomyObject, oCallbackData, sTaxonomyType);
    }
    else {
      let oScreenProps = ContentScreenProps.screen;
      if (ContentScreenProps.multiClassificationViewProps.getIsShowClassificationDialog()) {
        let aClonedTaxonomyIds = oScreenProps.getActiveClonedTaxonomyIds();
        aClonedTaxonomyIds.push(sTaxonomyId);
      }
    }
  };

  let _handleChildTaxonomyAdded = function (sTaxonomyId, sPreviousTaxonomyId, sTaxonomyType, oFilterContext) {
    let oCurrentEntity = ContentUtils.getCurrentEntity();
    let aTaxonomyIds = oCurrentEntity.selectedTaxonomyIds;
    let iIndex = CS.indexOf(aTaxonomyIds, sPreviousTaxonomyId);
    if (sTaxonomyType === TaxonomyTypeDictionary.MINOR_TAXONOMY) {
      aTaxonomyIds.splice(iIndex, 1, sTaxonomyId);
      var oTaxonomyObject = {
        addedTaxonomyIds: [sTaxonomyId],
        deletedTaxonomyIds: [sPreviousTaxonomyId]
      };
      var oCallbackData = {
        filterContext: oFilterContext
      };
      _handleApplyClassification(oTaxonomyObject, oCallbackData, sTaxonomyType);
    }
    else {
      let oScreenProps = ContentScreenProps.screen;
      if (ContentScreenProps.multiClassificationViewProps.getIsShowClassificationDialog()) {
        let aClonedTaxonomyIds = oScreenProps.getActiveClonedTaxonomyIds();
        aClonedTaxonomyIds.push(sTaxonomyId);

        /**Remove previously selected taxonomy id in hierarchy*/
        CS.remove(aClonedTaxonomyIds, function (sId) {
          return sId === sPreviousTaxonomyId;
        });
      }
    }
  };

  let _handleTaxonomyRemoved = function (sTaxonomyId) {
    let oScreenProps = ContentScreenProps.screen;
    if (ContentScreenProps.multiClassificationViewProps.getIsShowClassificationDialog()) {
      let aClonedTaxonomyIds = oScreenProps.getActiveClonedTaxonomyIds();
      CS.remove(aClonedTaxonomyIds, function (sId) {
        return sId === sTaxonomyId;
      });
    }
    _triggerChange();
  };

  let _handleChildTaxonomyRemoved = function (oTaxonomy, sActiveTaxonomyId, sTaxonomyType, oFilterContext) {
    let oCurrentEntity = ContentUtils.getCurrentEntity();
    let aTaxonomyIds = oCurrentEntity.taxonomyIds;
    let iIndex = CS.indexOf(aTaxonomyIds, sActiveTaxonomyId);
    let oScreenProps = ContentScreenProps.screen;

    if (iIndex != -1 && sTaxonomyType === TaxonomyTypeDictionary.MINOR_TAXONOMY) {
      var oCallbackData = {
        filterContext: oFilterContext
      };
      var oTaxonomyObject = {};

      if (oTaxonomy.parent.parent.id == -1) {
        oTaxonomyObject = {
          deletedTaxonomyIds: [sActiveTaxonomyId]
        };
      } else {
        oTaxonomyObject = {
          addedTaxonomyIds: [oTaxonomy.parent.id],
          deletedTaxonomyIds: [sActiveTaxonomyId]
        };
      }
      _handleApplyClassification(oTaxonomyObject, oCallbackData, sTaxonomyType);
    }
    else if (ContentScreenProps.multiClassificationViewProps.getIsShowClassificationDialog()) {
      let aClonedTaxonomyIds = oScreenProps.getActiveClonedTaxonomyIds();
      /**Remove if taxonomy type is minor taxonomy or parent id is -1*/
      if ((sTaxonomyType === TaxonomyTypeDictionary.MINOR_TAXONOMY && oTaxonomy.parent.parent.id == -1) ||
          oTaxonomy.parent.id == -1) {
        CS.remove(aClonedTaxonomyIds, function (sId) {
          return sId === sActiveTaxonomyId;
        });
        _triggerChange();
      }
      else {
        aClonedTaxonomyIds.push(oTaxonomy.parent.id);
        CS.remove(aClonedTaxonomyIds, function (sId) {
          return sId === sActiveTaxonomyId;
        });
      }
    }
  };

  let _handleNatureClassAdded = function (sClassId, oFilterContext) {
    var oActiveEntity = ContentUtils.getActiveEntity();
    var sNatureClassId = ContentUtils.getNatureKlassIdFromKlassIds(oActiveEntity.types);

    var oMultiClassificationObject = {
      addedSecondaryTypes: [sClassId]
    };

    sNatureClassId && (oMultiClassificationObject.deletedSecondaryTypes = [sNatureClassId]);
    _handleApplyClassification(oMultiClassificationObject, {filterContext: oFilterContext});
  };

  let _handleSecondaryClassAdded = function (sClassId) {
    let oScreenProps = ContentScreenProps.screen;
    if (ContentScreenProps.multiClassificationViewProps.getIsShowClassificationDialog()) {
      let aClonedClassIds = oScreenProps.getActiveClonedClassIds();
      aClonedClassIds.push(sClassId);
      let aAllowedNonNatureTypes = ContentScreenProps.articleViewProps.getAllowedNonNatureTypes();
      let oReferencedClasses = oScreenProps.getReferencedClasses();
      oReferencedClasses[sClassId] = CS.find(aAllowedNonNatureTypes, {id: sClassId});
    };
    _triggerChange();
  };

  let _handleSecondaryClassRemoved = function (sClassId) {
    let oScreenProps = ContentScreenProps.screen;
    if (ContentScreenProps.multiClassificationViewProps.getIsShowClassificationDialog()) {
      let aClonedClassIds = oScreenProps.getActiveClonedClassIds();
      if (CS.includes(aClonedClassIds, sClassId)) {
        CS.remove(aClonedClassIds, function (sId) {
          return sId === sClassId;
        });
      }
    }
    _triggerChange();
  };

  let _handleAllowedTypesSearchHandler = function (sContext, sSearchText, sTaxonomyId) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oArticleProps = oComponentProps.articleViewProps;
    let oScreenProps = oComponentProps.screen;
    let oActiveEntity = _getActiveEntity();
    let oData = {};
    oData.baseType = oActiveEntity.baseType;
    oData.selectionType = sContext;
    let oPaginationData = {};

    if (sContext === TaxonomyTypeDictionary.MAJOR_TAXONOMY) {
      oScreenProps.resetActiveTaxonomyPaginationData();
      oPaginationData = oScreenProps.getAllowedTaxonomiesPaginationData();
      oData.id = null;
      oData.idsToExclude = [];
    } else if (sContext === TaxonomyTypeDictionary.MINOR_TAXONOMY) {
      oScreenProps.resetActiveTaxonomyPaginationData();
      oPaginationData = oScreenProps.getAllowedTaxonomiesPaginationData();
      oData.id = sTaxonomyId;
      oData.idsToExclude = [];
    } else if (sContext === 'primaryTypes') {
      oArticleProps.resetAllowedNatureTypesPaginationData();
      oPaginationData = oArticleProps.getAllowedNatureTypesPaginationData();
    } else if (sContext === 'secondaryTypes') {
      oArticleProps.resetAllowedNonNatureTypesPaginationData();
      oPaginationData = oArticleProps.getAllowedNonNatureTypesPaginationData();
      oData.idsToExclude = [];
    }

    oPaginationData.searchText = sSearchText || "";
    ContentScreenProps.multiClassificationViewProps.setMultiClassificationTreeSearchText(sSearchText);

    CS.assign(oData, oPaginationData);

    CS.postRequest(getRequestMapping().AllowedTypes, {}, oData, successFetchAllowedTypes.bind(this, sContext, false), failureFetchAllowedTypes);
  };

  let _handleAllowedTypesLoadMoreHandler = function (sContext, sTaxonomyId) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oArticleProps = oComponentProps.articleViewProps;
    let oScreenProps = oComponentProps.screen;
    let oActiveEntity = _getActiveEntity();
    let oData = {};
    oData.baseType = oActiveEntity.baseType;
    oData.selectionType = sContext;
    let oPaginationData = {};

    if (sContext === TaxonomyTypeDictionary.MAJOR_TAXONOMY) {
      oPaginationData = oScreenProps.getAllowedTaxonomiesPaginationData();
      let aAllowedTaxonomies = oScreenProps.getAllowedTaxonomies();
      oPaginationData.from = CS.size(aAllowedTaxonomies);
      let sSearchText = oComponentProps.multiClassificationViewProps.getMultiClassificationTreeSearchText();
      if(CS.isNotEmpty(sSearchText)) {
        oData.id = null;
      } else if (sTaxonomyId === "addItemHandlerforMultiTaxonomy") {
        oData.id = ContentUtils.getTreeRootNodeId();
      } else {
        oData.id = sTaxonomyId;
      }
      oData.idsToExclude = [];
    } else if (sContext === TaxonomyTypeDictionary.MINOR_TAXONOMY) {
      oPaginationData = oScreenProps.getAllowedTaxonomiesPaginationData();
      let aAllowedTaxonomies = oScreenProps.getAllowedTaxonomies();
      oPaginationData.from = CS.size(aAllowedTaxonomies);
      oData.id = sTaxonomyId;
      oData.idsToExclude = [];
    } else if (sContext === 'primaryTypes') {
      oPaginationData = oArticleProps.getAllowedNatureTypesPaginationData();
      let aAllowedNatureTypes = oArticleProps.getAllowedNatureTypes();
      oPaginationData.from = CS.size(aAllowedNatureTypes);
    } else if (sContext === 'secondaryTypes') {
      oPaginationData = oArticleProps.getAllowedNonNatureTypesPaginationData();
      let aAllowedNonNatureTypes = oArticleProps.getAllowedNonNatureTypes();
      oPaginationData.from = CS.size(aAllowedNonNatureTypes);
      oData.idsToExclude = [];
    }

    CS.assign(oData, oPaginationData);
    CS.postRequest(getRequestMapping().AllowedTypes, {}, oData,   successFetchAllowedTypes.bind(this, sContext, true), failureFetchAllowedTypes);
  };

  let _fetchAllowedTypesByContext = function (sContext, sTaxonomyId) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oArticleProps = oComponentProps.articleViewProps;
    var oActiveEntity = _getActiveEntity();
    var oData = {};
    var sAllowedTypesMapping = getRequestMapping().AllowedTypes;
    oData.baseType = oActiveEntity.baseType;
    oData.selectionType = sContext;
    let oPaginationData = {};
    let oScreenProps = oComponentProps.screen;
    if (sContext === TaxonomyTypeDictionary.MAJOR_TAXONOMY) {
      oScreenProps.resetActiveTaxonomyPaginationData();
      oPaginationData = oScreenProps.getAllowedTaxonomiesPaginationData();
      if (sTaxonomyId === "addItemHandlerforMultiTaxonomy") {
        oData.id = ContentUtils.getTreeRootNodeId();
      } else {
        oData.id = sTaxonomyId;
      }
      oData.idsToExclude = [];
    } else if (sContext === TaxonomyTypeDictionary.MINOR_TAXONOMY) {
      oScreenProps.resetActiveTaxonomyPaginationData();
      oPaginationData = oScreenProps.getAllowedTaxonomiesPaginationData();
      oData.id = sTaxonomyId;
      oData.idsToExclude = [];
    } else if (sContext === 'primaryTypes') {
      oArticleProps.resetAllowedNatureTypesPaginationData();
      oPaginationData = oArticleProps.getAllowedNatureTypesPaginationData();
    } else if (sContext === 'secondaryTypes') {
      oArticleProps.resetAllowedNonNatureTypesPaginationData();
      oPaginationData = oArticleProps.getAllowedNonNatureTypesPaginationData();
      oData.idsToExclude = [];
    }

    CS.assign(oData, oPaginationData);

    return CS.postRequest(sAllowedTypesMapping, {}, oData, successFetchAllowedTypes.bind(this, sContext, false), failureFetchAllowedTypes);
  };

  let successFetchAllowedTypes = function (sContext, bLoadMore, oResponse) {
    var aList = oResponse.success;
    var oConfigDetails = aList.configDetails;
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oArticleProps = oComponentProps.articleViewProps;
    var aAllowedTypeIds = [];//CS.map(aList, "id");
    if (!CS.isArray(aList)) {
      aList = aList.list;
    }
    switch (sContext) {
      case "primaryTypes":
        if (bLoadMore) {
          let aAllowedNatureTypes = oArticleProps.getAllowedNatureTypes();
          oArticleProps.setAllowedNatureTypes(aAllowedNatureTypes.concat(aList));
        } else {
          oArticleProps.setAllowedNatureTypes(aList);
        }

        aAllowedTypeIds = CS.map(oArticleProps.getAllowedNatureTypes(), "id");
        break;

        case "secondaryTypes":
          let aAllowedNonNatureTypes = oArticleProps.getAllowedNonNatureTypes();
          aAllowedNonNatureTypes = bLoadMore && aAllowedNonNatureTypes.concat(aList) || aList;
          oArticleProps.setAllowedNonNatureTypes(aAllowedNonNatureTypes);
          aAllowedTypeIds = CS.map(aAllowedNonNatureTypes, "id");
        break;

      case TaxonomyTypeDictionary.MAJOR_TAXONOMY:
      case TaxonomyTypeDictionary.MINOR_TAXONOMY:
        let aAllowedTaxonomies = oScreenProps.getAllowedTaxonomies();
        let oAllowedTaxonomyConfigDetails = oScreenProps.getAllowedTaxonomyConfigDetails();
        aAllowedTaxonomies = bLoadMore && aAllowedTaxonomies.concat(aList) || aList;
        CS.assign(oAllowedTaxonomyConfigDetails, oConfigDetails);
        oScreenProps.setAllowedTaxonomies(aAllowedTaxonomies);
        aAllowedTypeIds = CS.map(oScreenProps.getAllowedTaxonomies(), "id");
        let oAllowedTaxonomyHierarchyListData = CommonUtils.prepareTaxonomyHierarchyData(oScreenProps.getAllowedTaxonomies(), oAllowedTaxonomyConfigDetails);
        oScreenProps.setAllowedTaxonomyHierarchyList(oAllowedTaxonomyHierarchyListData);
        break;
    }
    oArticleProps.setAllowedTypes(aAllowedTypeIds);
    oArticleProps.setAllowedTaxonomiesById(_prepareAllowedTaxonomyByIdData(aAllowedTypeIds));

    _triggerChange();
    return true;
  };

  let failureFetchAllowedTypes = function (oResponse) {
  };

  let _getRequestDataForClasses = function (oADMForClassification) {
    let oScreenProps = ContentScreenProps.screen;
    let aActiveClassIds = oScreenProps.getActiveClassIds();
    let aClonedClassIds = oScreenProps.getActiveClonedClassIds();
    let aDeletedSecondaryClasses = CS.filter(aActiveClassIds, function (sId) {
      return !CS.includes(aClonedClassIds, sId);
    });
    let aAddedSecondaryClasses = CS.filter(aClonedClassIds, function (sId) {
      return !CS.includes(aActiveClassIds, sId);
    });

    oADMForClassification.addedSecondaryTypes = aAddedSecondaryClasses;
    oADMForClassification.deletedSecondaryTypes = aDeletedSecondaryClasses;
  };

  let _getRequestDataForTaxonomies = function (oADMForClassification) {
    let oScreenProps = ContentScreenProps.screen;
    let aActiveTaxonomyIds = oScreenProps.getActiveTaxonomyIds();
    let aClonedTaxonomyIds = oScreenProps.getActiveClonedTaxonomyIds();
    let aDeletedTaxonomyIds = CS.filter(aActiveTaxonomyIds, function (sId) {
      return !CS.includes(aClonedTaxonomyIds, sId);
    });
    let aAddedTaxonomyIds = CS.filter(aClonedTaxonomyIds, function (sId) {
      return !CS.includes(aActiveTaxonomyIds, sId);
    });

    oADMForClassification.addedTaxonomyIds = aAddedTaxonomyIds;
    oADMForClassification.deletedTaxonomyIds = aDeletedTaxonomyIds;
  };

  let _handleApplyClassification = function (oADMForClassification, oCallbackData, sTaxonomyType) {
    let oScreenProps = ContentScreenProps.screen;
    if (CS.isEmpty(oADMForClassification)) {
      oADMForClassification = {};
      _getRequestDataForClasses(oADMForClassification);
      _getRequestDataForTaxonomies(oADMForClassification);
    }
    let oActiveContent = oCallbackData && oCallbackData.droppedEntity ? oCallbackData.droppedEntity : _getActiveEntity();
    let bIsMinorTaxonomySwitch = (sTaxonomyType === TaxonomyTypeDictionary.MINOR_TAXONOMY);
    let sNatureTypeClass = '';
    let aAllowedTypes = ContentScreenProps.articleViewProps.getAllowedNatureTypes() || [];
    CS.forEach(aAllowedTypes, function (sClassId) {
      if (CS.includes(oActiveContent.types, sClassId)) {
        sNatureTypeClass = sClassId;
      }
    });

    let oRequestObject = {
      klassInstanceId: oActiveContent.id,
      isLinked: true,
      natureClassId: sNatureTypeClass,
      isNatureKlassSwitched: false,
      isMinorTaxonomySwitch: bIsMinorTaxonomySwitch,
      deletedSecondaryTypes: oADMForClassification.deletedSecondaryTypes || [],
      addedSecondaryTypes: oADMForClassification.addedSecondaryTypes || [],
      deletedTaxonomyIds: oADMForClassification.deletedTaxonomyIds || [],
      addedTaxonomyIds: oADMForClassification.addedTaxonomyIds || [],
    };

    let sSelectedTab = ContentUtils.getSelectedTabId();
    oRequestObject.tabType = ContentUtils.getTabTypeFromTabId(sSelectedTab);
    oRequestObject.tabId = sSelectedTab;

    let oSelectedTemplate = oScreenProps.getSelectedTemplate();

    let templateObj = {};
    if (oSelectedTemplate) {
      templateObj = ContentUtils.getTemplateAndTypeIdForServer(oSelectedTemplate.id);
    }

    oRequestObject.typeId = templateObj.typeId;
    oRequestObject.templateId = templateObj.templateId;

    let oCallback = !CS.isEmpty(oCallbackData) ? oCallbackData : {};
    let sBaseType = oActiveContent.baseType ? oActiveContent.baseType : oActiveContent.type;
    let sScreenContext = ContentUtils.getScreenModeBasedOnEntityBaseType(sBaseType);
    let sUrl = getRequestMapping(sScreenContext).GetEntityWithTypeChanged;

    let fSuccess = successApplyClassificationCallback.bind(this, oCallback);
    let fFailure = failureApplyClassificationCallback.bind(this, oCallback);
    let oData = {};
    return CS.postRequest(sUrl, oData, oRequestObject, fSuccess, fFailure);
  };

  let successApplyClassificationCallback = function (oCallback, oResponse) {
    oResponse = oResponse.success;
    let oScreenProps = ContentScreenProps.screen;
    let oKlassInstance = oResponse.klassInstance;

    /** To handle taxonomyHierarchy Entity Drop*/
    if (oCallback.droppedEntity) {
      oScreenProps.decrementMultipleTaxonomyHierarchyTypeChangeCallCounter();
      let iResponseCount = oScreenProps.getMultipleTaxonomyHierarchyTypeChangeCallCounter();
      if (iResponseCount === 0) {
        ContentUtils.showSuccess(oCallback.successMessage);
      }
      if (oCallback.isPaste) {
        ContentScreenProps.screen.setHierarchyEntitiesToCopyOrCutData({});
      }
      if (oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute();
      }
      _triggerChange();
      return;
    }

    let oCurrentEntity = ContentUtils.getCurrentEntity();
    _resetDataOnEntityTypeChange(oCurrentEntity);
    _processGetArticleResponse(oResponse, null, null, true);
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    ContentUtils.showSuccess(ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity : getTranslation().CONTENT}));
    let fCallbackToGetKPIData = _fetchKpiSummaryData.bind(this, oKlassInstance.id, oKlassInstance.baseType);
    setTimeout(fCallbackToGetKPIData, 3000);

    _triggerChange();
    return true;
  };

  let failureApplyClassificationCallback = function (oCallback, oResponse) {
    ContentUtils.failureCallback(oResponse,'failureApplyClassificationCallback',getTranslation());
    _triggerChange();
  };

  let _handleMultiClassificationDialogButtonClicked = function (sDialogButtonId, oCallbackObject) {
    let oCallback = oCallbackObject || {};
    switch (sDialogButtonId) {
      case "apply":
        oCallback.functionToExecute = function (oResponse) {
          let oActiveContent = oResponse.klassInstance;
          _resetDataOnEntityTypeChange(oActiveContent);
          _processGetArticleResponse(oResponse, null, null, true);
          let fCallbackToGetKPIData = _fetchKpiSummaryData.bind(this, oActiveContent.id, oActiveContent.baseType);
          setTimeout(fCallbackToGetKPIData, 3000);
        };

        NewMultiClassificationStore.applyMultiClassification(oCallback);
        break;

      case "cancel":
        oCallback.functionToExecute = () => {
          let oActiveEntity = ContentUtils.getActiveContent();
          _fetchArticleById(oActiveEntity.id, {baseType: oActiveEntity.baseType});
        };
        NewMultiClassificationStore.resetMultiClassificationProps(oCallback);
        break;
    }
  };

  let _getElementsForInstanceProperties = function () {
    let aElementsForSection = [];
    return aElementsForSection;
  };

  let _fetchAssetClassList = function (oFilterContext) {
    let oFilterParameters = {
      sortOptions: [],
      from: 0,
      size: 0,
      moduleId: "mammodule"
    };
    let sContentId = ContentUtils.getTreeRootNodeId();
    let oData = {
      id: sContentId,
      isLoadMore: false,
      getAllChildren: false
    };

    let oPostDataForFilter = ContentUtils.createFilterPostData(oFilterParameters, oFilterContext);
    let sUrl = getRequestMapping().GetAllEntities;
    CS.postRequest(sUrl, oData, oPostDataForFilter, successFetchClassListCallBack, failureFetchClassListCallBack);
  };

  let successFetchClassListCallBack = function (oResponse) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oRelationshipProps = oComponentProps.relationshipView;
    let aAssetClassList = oResponse.success.defaultTypes.children;
    oRelationshipProps.setAssetClassList(aAssetClassList);
    _triggerChange();
  };

  let failureFetchClassListCallBack = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchContentListCallBack', getTranslation());
    return false;
  };

  let _handleModuleCreateButtonClicked = function (sSearchedText, sContext, oFilterContext, bIsLoadMoreClicked) {
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oComponentProps = ContentUtils.getComponentProps();
    let oRequestData = ContentScreenProps.screen.getDefaultPaginationData();
    oRequestData.searchText = sSearchedText || "";
    oComponentProps.screen.setDefaultTypesSearchText(sSearchedText);
    oRequestData.searchColumn = "label";
    oRequestData.sortBy = "label";
    oRequestData.sortOrder = "asc";
    bIsLoadMoreClicked && (oRequestData.size = 0);
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oTaxonomyTreeData = oFilterStore.getTaxonomyTreeData();
    oRequestData.selectedTypes = oTaxonomyTreeData.selectedTypes;
    if (sContext === "contextEntity") {
      let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
      let sSelectedEntity = oVariantSectionViewProps.getSelectedEntity();
      oRequestData.entityType = sSelectedEntity;
    } else if (sContext === "relationshipEntity") {
      let oReferencedRelationship = {};
      let oSectionSelectionStatus = oComponentProps.screen.getSetSectionSelectionStatus();
      let sRelationshipSectionElementId = oSectionSelectionStatus['selectedRelationship'].id;
      let sFoundRelationshipId = ContentUtils.getRelationshipIdFromSideId(sRelationshipSectionElementId);
      let sRelationshipType = ContentUtils.getContentRelationshipKeyById(sFoundRelationshipId);
      let oReferencedRelationships = [];
      CS.assign(oReferencedRelationships, oComponentProps.screen.getReferencedRelationships());
      CS.assign(oReferencedRelationships, oComponentProps.screen.getReferencedNatureRelationships());
      oReferencedRelationship = oReferencedRelationships[sFoundRelationshipId];
      let oCurrentRelationship = CS.find(oActiveEntity[sRelationshipType], (oNature) => oNature.relationshipId === sFoundRelationshipId);
      oReferencedRelationship.side1.elementId === oCurrentRelationship.sideId ? oRequestData.klassIds = [oReferencedRelationship.side2.klassId] :
      oRequestData.klassIds = [oReferencedRelationship.side1.klassId];

    } else {
      oRequestData.moduleId = ContentUtils.getSelectedModuleId();
    }
    let sUrl = "config/defaulttypes/get";
    return CS.postRequest(sUrl, "", oRequestData, successFetchCreateButtonList, failureFetchCreateButtonList);
  };

  let successFetchCreateButtonList = (oResponse) =>  {
    oResponse = oResponse.success;
    let oAppData = ContentUtils.getAppData();
    oAppData.setDefaultTypes(oResponse || {});
    _triggerChange()
  };

  let failureFetchCreateButtonList = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureFetchCreateButtonList', getTranslation());
  };

  let _validateContextDuplication = function (oCallback) {
    let oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    let oActiveEntity = ContentUtils.getActiveEntity();
    let oRequestData = {
      id: oActiveEntity.id,
      contextId: oRelationshipContextData.context.id,
      timeRange: oRelationshipContextData.timeRange,
      tags: oRelationshipContextData.tags,
      relationshipId: oRelationshipContextData.context.actualRelationshipId
    };
    let sUrl = ContentScreenRequestMapping.CheckContextDuplication;
    return CS.postRequest(sUrl, "", oRequestData, successValidateContextDuplication.bind(this, oCallback), failureValidateContextDuplication);
  };

  let successValidateContextDuplication = (oCallback = {}, oResponse) => {
    if (oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
  };

  let failureValidateContextDuplication = (oResponse) => {
    return ContentUtils.failureCallback(oResponse, 'failureValidateContextDuplication', getTranslation());
  };

  //************************************* Public API's **********************************************//
  return {
    handleContentEntityDropInRelationshipSection: function (sId, oCallback) {
      _handleContentEntityDropInRelationshipSection(sId, oCallback);
    },

    successSaveContentsCallback: function (oCallbackData, oResponse) {
      successSaveContentsCallback(oCallbackData, oResponse);
    },

    handleComparisonButtonClicked: function () {
      _handleComparisonButtonClicked();
    },

    handleLanguageForComparisonChanged: function (sLanguageForComparison) {
      _handleLanguageForComparisonChanged(sLanguageForComparison);
    },

    handleMatchMergeColumnHeaderClicked: function (sColumnId) {
      _handleMatchMergeColumnHeaderClicked(sColumnId);
    },

    handleContentComparisonBackButtonClicked: function () {
      _handleContentComparisonBackButtonClicked();
    },

    createNewModuleEntity: function (oItem, oCallbackData) {
      _createNewModuleEntity(oItem, oCallbackData);
    },

    handleDeleteArticleTilesButtonClicked: function (oCallbackData) {
      _deleteSelectedArticleTiles(oCallbackData);
    },

    selectAllArticleChildren: function () {
      _selectAllArticleChildren();
      _triggerChange();
    },

    handleThumbnailDeleteIconClicked: function (oContent) {
      _handleThumbnailDeleteIconClicked(oContent);
    },

    saveContents: function (oContent, oCallbackData) {
      return _saveContents(oContent, oCallbackData);
    },

    saveActiveContent: function (oCallbackData, oActiveContentData) {
      trackMe('saveActiveContent');
      let oActiveEntity = oActiveContentData ? oActiveContentData : ContentUtils.getActiveEntity();
      ContentLogUtils.debug('saveActiveContent: Saving active content', oActiveEntity);
      _saveContents(oActiveEntity, oCallbackData);
    },

    discardChangesWhileSearching: function (oCallbackData) {
      trackMe('discardChangesWhileSearching');
      var oAppData = ContentUtils.getAppData();

      CS.forEach(oAppData.getContentList(), ContentUtils.makeContentClean);

      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    },

    discardChangesInActiveContent: function (oCallbackData) {
      trackMe('discardChangesInActiveContent');
      var oActiveContent = ContentUtils.getActiveContent();
      ContentLogUtils.debug('discardChangesInActiveContent: Removing dirty mark and clone from active content',
          oActiveContent);
      ContentUtils.makeContentClean(oActiveContent);

      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
    },

    fetchContentList: function (oCallbackData, oExtraData) {
      _fetchContentList(oCallbackData, oExtraData);
    },

    fetchEntities: function (aEntityList, bLoadMore, oCallback) {
      return _fetchEntities(aEntityList, bLoadMore, oCallback);
    },

    toggleContentThumbnailSelection: function (oContent, sScreenContext) {

      //oContent is an object in case of article
      trackMe('_toggleContentThumbnailSelection');
      var aSelectedContentIds = ContentUtils.getSelectedContentIds();

      /*
       * If content found then remove from selected list else put it into selected list
       */
      var sContentIndex = CS.indexOf(aSelectedContentIds, oContent.id);
      if (sContentIndex != -1) {
        ContentLogUtils.debug('_toggleContentThumbnailSelection: Removing already existing content in the selected list',
            oContent);
        CS.remove(aSelectedContentIds, function (sId) {
          return sId == oContent.id;
        });
      }

      if (sContentIndex == -1) {
        ContentLogUtils.debug('_toggleContentThumbnailSelection: Adding selected content into selected content list',
            oContent);
        aSelectedContentIds.push(oContent.id);
      }

      //Make content as item selection mode for tagging context
      ContentUtils.changeItemSelectionMode('content');
    },

    getArticleDetails: function (sArticleId, oCallbackData) {
      _getArticleDetails(sArticleId, oCallbackData);
    },

    handleAssetBulkDownloadButtonClicked: function (sContext, sButtonContext) {
      _handleAssetBulkDownloadButtonClicked(sContext, sButtonContext);
    },

    handleTableDownloadImageButtonClicked: function (sContentId, sContextId) {
      _handleTableDownloadImageButtonClicked(sContentId, sContextId);
    },

    handleRefreshContentButtonClicked: function (oFilterContext) {
      trackMe('handleRefreshContentButtonClicked');
      _handleRefreshContentButtonClicked(oFilterContext);

    },

    addValueInContentAttributeFromMultiSearch: function (sAttributeId, sAttrValue) {
      trackMe('addValueInContentAttributeFromMultiSearch');
      logger.debug('addValueInContentAttributeFromMultiSearch', {attributeId: sAttributeId, attrValue: sAttrValue});

      var oActiveContent = ContentUtils.makeActiveContentDirty();
      var oAttribute = CS.find(oActiveContent.attributes, {'attributeId': sAttributeId});
      oAttribute.values.push(sAttrValue);
    },

    updateContentTagValues: function (aTagValueRelevanceData, sTagId) {
      _updateContentTagValues(aTagValueRelevanceData, sTagId);
    },

    addTagInContentFromUniqueSearch: function (sGroupId, sDataId) {
      _addTagInContentFromUniqueSearch(sGroupId, sDataId);
    },

    addUserInRoleFromMultiSearch: function (aCandidateIds, sRoleId) {
      var aMasterUserList = ContentUtils.getUserList();
      var aCandidates = [];
      var oCandidate = {};

      var oActiveContent = ContentUtils.makeActiveContentOrVariantDirty();
      var aContentRoles = oActiveContent.roles;
      var oRole = CS.find(aContentRoles, {roleId: sRoleId});

      try {
        CS.forEach(aCandidateIds, function (sCandidateId) {
          var oCandidateFromMasterList = CS.find(aMasterUserList, {'id': sCandidateId});
          if (!CS.isEmpty(oCandidateFromMasterList)) {
            oCandidate = CS.cloneDeep(oCandidateFromMasterList);
            oCandidate.type = "user";
            oCandidate.timestamp = new Date().getTime();
            aCandidates.push(oCandidate);
          }
        });
      } catch (oException) {
        ExceptionLogger.error(oException);
      }

      oRole.candidates = aCandidates;
      oRole.isValueChanged = true;
      _updateMandatoryElementsStatus(oActiveContent);
    },

    handleTaxonomyHierarchyExpansionToggled: function (sSectionId, oFilterContext) {
      _handleTaxonomyHierarchyExpansionToggled(sSectionId, oFilterContext);
    },

    removeValueInContentAttributeFromMultiSearch: function (sAttributeId, sAttrValue) {
      trackMe('removeValueInContentAttributeFromMultiSearch');
      logger.debug('removeValueInContentAttributeFromMultiSearch', {attributeId: sAttributeId, attrValue: sAttrValue});

      var oActiveContent = ContentUtils.makeActiveContentDirty();
      var oAttribute = CS.find(oActiveContent.attributes, {'attributeId': sAttributeId});
      CS.remove(oAttribute.values, function (sValue) {
        return sValue == sAttrValue;
      });
      oAttribute.isValueChanged = true;
    },

    removeTagFromContentUsingMultiSearch: function (sId, aContextKey) {
      trackMe('removeTagFromContentUsingMultiSearch');
      logger.debug('removeTagFromContentUsingMultiSearch', {id: sId});

      var sTagGroupId = aContextKey[1];
      var oCondition = {
        tagId: sTagGroupId
      };

      if (aContextKey[2]) {
        oCondition.id = aContextKey[2];
      }
      var oActiveEntity = ContentUtils.makeActiveContentOrVariantDirty();
      //DATAMIGRATION: change to mappingId
      var oTagGroup = CS.find(oActiveEntity.tags, oCondition);
      var oTagGroupTagValue = CS.find(oTagGroup.tagValues, {tagId: sId});
      oTagGroupTagValue.relevance = 0;
      oTagGroup.isValueChanged = true;
      _updateMandatoryElementsStatus(oActiveEntity);
    },

    handleSectionHeaderClicked: function (oPaperViewModel) {
      var oSectionViewProps = ContentScreenProps.contentSectionViewProps;
      var oSectionVisualProps = oSectionViewProps.getSectionVisualProps();
      var oSectionProp = oSectionVisualProps[oPaperViewModel.id];

      if (oSectionProp) {
        oSectionProp.isExpanded = !oSectionProp.isExpanded;
      } else {
        oSectionVisualProps[oPaperViewModel.id] = {isExpanded: false};
      }
    },

    fetchMasterClassesAndCreateNewContent: function () {
      trackMe('fetchMasterClassesAndCreateNewContent');
      var oNewArticle = ContentUtils.createDummyArticle('', '', false);

      CS.postRequest(getRequestMapping().CreateContent, {}, oNewArticle, successCreateContentCallback, failureCreateContentCallback);
    },

    changeOwner: function (sOwnerId) {
      trackMe('changeOwner');
      logger.debug('changeOwner', {ownerId: sOwnerId});

      var oActiveContent = ContentUtils.makeActiveContentDirty();
      oActiveContent.owner = sOwnerId;
    },

    handleContentImagesRemoved: function (aAssetObjectKeys) {
      trackMe('handleContentImagesRemoved');
      logger.debug('handleContentImagesRemoved', {assetObjectKeys: aAssetObjectKeys});

      var oActiveContent = ContentUtils.makeActiveContentDirty();
      CS.each(aAssetObjectKeys, function (sAssetObjectKey) {
        var oAssetEntry = CS.find(oActiveContent.addedAssets, {'assetObjectKey': sAssetObjectKey});
        if (oAssetEntry) {
          CS.remove(oActiveContent.addedAssets, oAssetEntry);
        } else {
          oActiveContent.deletedAssets.push({'assetObjectKey': sAssetObjectKey, 'type': 'Image'});
        }
      });
    },

    handleTab: function (oEvent, bWithShift) {
      oEvent.preventDefault();
      var oScreenProps = ContentUtils.getScreenProps();
      var oSelectedContext = oScreenProps.getSelectedContext();
      var aActiveSections = null;

      if (oSelectedContext.sectionId && oSelectedContext.elementId) {
        aActiveSections = oScreenProps.getActiveSections();
      }

      if (aActiveSections) {
        var sSectionId = oSelectedContext.sectionId;
        var sElementId = oSelectedContext.elementId;

        var oSection = null;
        var oNextSection = null;
        var oPrevSection = null;
        var iSectionIndex = null;
        var aSections = aActiveSections;

        CS.forEach(aSections, function (oSec, iIndex) {
          if (oSec.id == sSectionId) {
            oSection = oSec;
            iSectionIndex = iIndex;
            oPrevSection = aActiveSections[iIndex - 1];
            oNextSection = aActiveSections[iIndex + 1];
          }
        });

        var oElement = CS.find(oSection.elements, {id: sElementId});

        var iCols = oSection.columns;
        var iIndexX = oElement.position.x;
        var iIndexY = oElement.position.y;
        var iSum = (iIndexX * iCols) + iIndexY;
        var iFoundSum = bWithShift ? -Infinity : Infinity;
        var oElFromOtherSection = null;
        var oOtherSection = null;
        var oFoundEl = null;
        var oMasterEnt = null;

        CS.forEach(oSection.elements, function (oEl) {
          var iElSum = (oEl.position.x * iCols) + oEl.position.y;
          var bCondition = bWithShift ? (iElSum > iFoundSum && iElSum < iSum) : (iElSum < iFoundSum && iElSum > iSum);

          if (bCondition && _getIfTabbableSectionElement(oEl)) {
            iFoundSum = iElSum;
            oFoundEl = oEl;
          }
        });

        if (!oFoundEl) {

          if (bWithShift && oPrevSection) {
            for (var i = iSectionIndex - 1; i >= 0; i--) {
              oOtherSection = aSections[i];
              oElFromOtherSection = _getLastSectionElementFromSection(oOtherSection);

              if (oElFromOtherSection) {
                break;
              }
            }
          } else if (oNextSection) {
            for (var j = iSectionIndex + 1; j < aSections.length; j++) {
              oOtherSection = aSections[j];
              oElFromOtherSection = _getFirstSectionElementFromSection(oOtherSection);

              if (oElFromOtherSection) {
                break;
              }
            }
          }
        }

        if (oFoundEl) {
          oMasterEnt = oFoundEl[oFoundEl.type];
          this.handleSectionAttributeClicked("", oFoundEl.type, oFoundEl.id, "", sSectionId, oMasterEnt.id, oMasterEnt.type);
        } else if (oElFromOtherSection) {
          oMasterEnt = oElFromOtherSection[oElFromOtherSection.type];
          this.handleSectionAttributeClicked("", oElFromOtherSection.type, oElFromOtherSection.id, "", oOtherSection.id, oMasterEnt.id, oMasterEnt.type);
        }
      }
    },

    handleSectionAttributeClicked: function (sSectionContext, sContext, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, bIsReadOnlyCoupled, sFillerID) {
      trackMe('handleSectionAttributeClicked');
      logger.debug('handleSectionAttributeClicked', {
        context: sContext,
        elementId: sElementId,
        structureId: sStructureId
      });

      let oCallback = {};
      oCallback.functionToExecute = function () {
        var oAppData = ContentUtils.getAppData();
        oAppData.emptyAvailableEntities();
        ContentScreenProps.contentDetailsView.resetContentSelectionVersionProps();
        _setSelectedContextProps(sContext, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, sFillerID);
        _triggerChange();
      };

      let oElementsHavingConflictingValues = ContentScreenProps.screen.getElementsHavingConflictingValues();
      let bDisableLoader = true;
      let bDoNotTrigger = true;
      if (oElementsHavingConflictingValues[sElementId] && !oElementsHavingConflictingValues[sElementId].isExpanded && !bIsReadOnlyCoupled) {
        _handleSectionElementConflictIconClicked(sElementId, bDisableLoader, false, false, bDoNotTrigger, oCallback);
      } else {
        oCallback.functionToExecute();
      }
    },

    handleSectionMaskClicked: function (sSectionId, sElementId, sSectionContext, oFilterContext) {
      trackMe('handleSectionAttributeClicked');
      _handleSectionMaskClicked(sSectionId, sElementId, sSectionContext, oFilterContext);
    },

    handleCancelVariantCreationClicked: function () {
      var oVariantSectionProps = ContentScreenProps.variantSectionViewProps;
      if (!CS.isEmpty(oVariantSectionProps.getEditLinkedVariantInstance())) {
        var oDummyLinkedVariant = oVariantSectionProps.getDummyLinkedVariant();
        if (!CS.isEmpty(oDummyLinkedVariant) && oDummyLinkedVariant.isDirty) {
          ContentUtils.setShakingStatus(true);
          _triggerChange();
          return;
        } else {
          oVariantSectionProps.setEditLinkedVariantInstance({});
          _cancelVariantCreation();
        }
      } else {
        _cancelVariantCreation();
      }
    },

    handleStructureFroalaImageUploaded: function (assetObjectKey) {
      trackMe('handleStructureFroalaImageUploaded');
      logger.debug('handleStructureFroalaImageUploaded', {assetObjectKey: assetObjectKey});

      var oActiveContent = ContentUtils.makeActiveContentDirty();
      oActiveContent.addedAssets.push({'assetObjectKey': assetObjectKey, 'type': 'Image'});
    },

    handleTemplateDropDownListNodeClicked: function (oSelectedTemplate) {
      _handleSelectedTemplateChanged(oSelectedTemplate);
    },

    handleTaxonomyAdded: function (sTaxonomyId, sTaxonomyType, oFilterContext) {
      _handleTaxonomyAdded(sTaxonomyId, sTaxonomyType, oFilterContext);
    },

    handleChildTaxonomyAdded: function (sTaxonomyId, sPreviousTaxonomyId, sTaxonomyType, oFilterContext) {
      _handleChildTaxonomyAdded(sTaxonomyId, sPreviousTaxonomyId, sTaxonomyType, oFilterContext);
    },

    handleTaxonomyRemoved: function (sTaxonomyId) {
      _handleTaxonomyRemoved(sTaxonomyId);
    },

    handleChildTaxonomyRemoved: function (oTaxonomy, sActiveTaxonomyId, sTaxonomyType, oFilterContext) {
      _handleChildTaxonomyRemoved(oTaxonomy, sActiveTaxonomyId, sTaxonomyType, oFilterContext);
    },

    handleSecondaryClassAdded: function (sClassId) {
      _handleSecondaryClassAdded(sClassId);
    },

    handleNatureClassAdded: function (sClassId, oFilterContext) {
      _handleNatureClassAdded(sClassId, oFilterContext);
    },

    handleSecondaryClassRemoved: function (sClassId) {
      _handleSecondaryClassRemoved(sClassId);
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId) {
      _fetchAllowedTypesByContext(sContext, sTaxonomyId);
    },

    handleAllowedTypesSearchHandler: function (sContext, sSearchText, sTaxonomyId) {
      _handleAllowedTypesSearchHandler(sContext, sSearchText, sTaxonomyId);
    },

    handleAllowedTypesLoadMoreHandler: function (sContext, sTaxonomyId) {
      _handleAllowedTypesLoadMoreHandler(sContext, sTaxonomyId);
    },

    handleKpiChartTypeSelected: function (sKpiTypeId) {
      _handleKpiChartTypeSelected(sKpiTypeId);
    },

    fetchUserList: function () {
      return _fetchUserList({});
    },

    handleContentFilterAttributeAdded: function (sAttributeId, sContext, oFilterContext) {
      _fetchAttribute(sAttributeId, sContext, oFilterContext);
    },

    handleContentFilterTagAdded: function (sTagId, sContext, oFilterContext) {
      _fetchTag(sTagId, sContext, oFilterContext);
    },

    handleContentFilterRoleAdded: function (sId, sContext, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var oObjToPush = _getRolesObjectToPushIntoFilterProps(sId);
      aAppliedFilters.push(oObjToPush);
      var oAppliedFilterCollapseStatusMap = ContentUtils.getFilterProps(oFilterContext).getAppliedFilterCollapseStatusMap();
      oAppliedFilterCollapseStatusMap[sId] = {isCollapsed: false};
      _triggerChange();
    },

    handleContentFilterInnerTagMSSApplyClicked: function (sTagGroupId, aTagValueRelevanceData, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var oLoadedTags = ContentScreenProps.screen.getLoadedTags();
      var oMasterTagGroup = oLoadedTags[sTagGroupId] || ContentUtils.getMasterTagById(sTagGroupId);

      var oTagGroupFromFilter = CS.find(aAppliedFilters, {id: sTagGroupId});
      var aTagValuesFromFilter = oTagGroupFromFilter.children;

      _addNewFilterInstanceOfSelectedTagValuesInTag(oTagGroupFromFilter, oMasterTagGroup, aTagValueRelevanceData);

      CS.forEach(aTagValuesFromFilter, function (oTagValueFromFilter) {
        var sTagValueId = oTagValueFromFilter.id;
        var oMasterTag = CS.find(oMasterTagGroup.children, {id: sTagValueId}) || ContentUtils.getMasterTagById(sTagValueId);
        var oData = CS.find(aTagValueRelevanceData, {tagId: sTagValueId});

        ContentUtils.updateTagValueRelevanceOrRange(oTagValueFromFilter, oData, true);

        oTagValueFromFilter.label = oMasterTag.label;
        oTagValueFromFilter.baseType = "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel";
        oTagValueFromFilter.type = "range";
      });

      _triggerChange();
    },

    handleContentUserAdded: function (sUserGroupId, aUsers, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var oRolesFromFilter = CS.find(aAppliedFilters, {id: sUserGroupId});
      oRolesFromFilter.users = aUsers;
      _triggerChange();
    },

    handleFilterElementDeleteClicked: function (sElementId, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var iFoundIndex = CS.findIndex(aAppliedFilters, {id: sElementId});
      if (iFoundIndex >= 0) {
        aAppliedFilters.splice(iFoundIndex, 1);
      }
      _triggerChange();
    },

    handleFilterElementExpandClicked: function (sElementId, oFilterContext) {
      var oAppliedFilterCollapseStatusMap = ContentUtils.getFilterProps(oFilterContext).getAppliedFilterCollapseStatusMap();
      var bPrevStatus = oAppliedFilterCollapseStatusMap[sElementId].isCollapsed;
      oAppliedFilterCollapseStatusMap[sElementId].isCollapsed = !bPrevStatus;
      _triggerChange();
    },

    handleAddAttributeValueClicked: function (sAttributeId, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});
      let aLoadedAttributes = ContentScreenProps.screen.getLoadedAttributes();
      let oMasterAttribute = CS.find(aLoadedAttributes, {id: sAttributeId});

      var sType = "contains";
      var sVisualType = ContentUtils.getAttributeTypeForVisual(oAppliedAttribute.type);
      if (sVisualType == "date") {
        sType = "exact";
      }

      var aValueAsNumberTypes = ["measurementMetrics", "number", "calculated"];
      var sValue = CS.includes(aValueAsNumberTypes, sVisualType) && oAppliedAttribute.type !== AttributesTypeDictionary.TEMPERATURE ? "0" : "";

      var oObjToPush = {
        id: UniqueIdentifierGenerator.generateUUID(),
        type: sType,
        baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel",
        defaultUnit: oMasterAttribute.defaultUnit,
        value: sValue,
        unitToShow: MeasurementMetricBaseTypeDictionary[oAppliedAttribute.type],
        advancedSearchFilter: true
      };

      oAppliedAttribute.children.push(oObjToPush);

      var oCollapseExpandMap = ContentUtils.getFilterProps(oFilterContext).getAppliedFilterCollapseStatusMap();
      oCollapseExpandMap[sAttributeId].isCollapsed = false;
      _triggerChange();
    },

    handleFilterAttributeValueTypeChanged: function (sAttributeId, sValId, sTypeId, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});

      var sVisualType = ContentUtils.getAttributeTypeForVisual(oAppliedAttribute.type);
      var aNumberTypes = ["number", "measurementMetrics", "calculated"];

      var oValue = CS.find(oAppliedAttribute.children, {id: sValId});
      oValue.type = sTypeId;
      if (sTypeId == "range") {
        oValue.baseType = "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel";

        oValue.to = oValue.from = (CS.includes(aNumberTypes, sVisualType) && oAppliedAttribute.type !== AttributesTypeDictionary.TEMPERATURE ) ? 0 : "";
        // oValue.from = (CS.includes(aNumberTypes, sVisualType)) ? 0 : "";
        oValue.defaultUnit = {
          from: oValue.defaultUnit,
          to: oValue.defaultUnit,
        }
      } else {
        oValue.baseType = "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel"
        oValue.defaultUnit && oValue.defaultUnit.hasOwnProperty("from") && (oValue.defaultUnit = oValue.defaultUnit.from);
      }

      if (sTypeId == "empty" || sTypeId == "notempty") {
        oValue.value = (CS.includes(aNumberTypes, sVisualType)) ? 0 : "";
      }
      _triggerChange();
    },

    handleFilterAttributeValueChanged: function (sAttributeId, sValId, sVal, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});

      var sVisualType = ContentUtils.getAttributeTypeForVisual(oAppliedAttribute.type);
      var aNumberTypes = ["number", "measurementMetrics", "calculated"];
      if (CS.includes(aNumberTypes, sVisualType)) {
        sVal = Number(sVal).toString(); // remove trailing zeros.
      }

      var oValue = CS.find(oAppliedAttribute.children, {id: sValId});
      if (oValue.advancedSearchFilter) {
        sVal = _validateNewValue(sVal, oAppliedAttribute.type);
        oValue.value = sVal;
      } else {
        oValue.label = sVal;
      }

      _triggerChange();
    },

    handleFilterAttributeValueChangedForRange: function (sAttributeId, sValId, sVal, sRange, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});

      sVal = _validateNewValue(sVal, oAppliedAttribute.type);
      var oValue = CS.find(oAppliedAttribute.children, {id: sValId});
      oValue[sRange] = sVal;

      _triggerChange();
    },

    handleFilterAttributeValueDeleteClicked: function (sAttributeId, sValId, oFilterContext) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      var aAppliedFilters = oFilterStore.getAppliedFiltersClone();
      var oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});
      CS.remove(oAppliedAttribute.children, {id: sValId});
    },

    handleFilterButtonClicked: function (oCallback) {
      return _handleFilterButtonWrapperClicked(oCallback);
    },

    handleFilterSearchTextChanged: function (oSearchedData, oExtraData) {
      let oFilterStore = FilterStoreFactory.getFilterStore(oExtraData.filterContext);
      oFilterStore.handleFilterSearchTextChanged(oSearchedData, oExtraData);
    },

    activateUploadAssetPopUp: function () {
      trackMe('activateUploadAssetPopUp');
      ContentScreenProps.screen.setIsUpload(true);
      _triggerChange();
    },

    handleZoomToolClicked: function (sKey, sContext, sElementId) {
      _handleZoomToolClicked(sKey, sContext, sElementId);
      _triggerChange();
    },

    handleSetSectionClicked: function (sActiveSetId, sContext, sRelationshipSectionId, oRelationshipSide) {
      _handleSetSectionClicked(sActiveSetId, sContext, sRelationshipSectionId, oRelationshipSide);
    },

    handleMetricUnitChanged: function(sSelectedUnit, oSectionElementDetails, sAttrId, sValueId, sRangeType, oFilterContext) {
      _handleMetricUnitChanged(sSelectedUnit, oSectionElementDetails, sAttrId, sValueId, sRangeType, oFilterContext);
    },

    handleSectionElementNotificationButtonClicked: function (sButtonKey, oElement) {
      var sKey = "";
      var oInnerElement = {};
      var oActiveEntity = ContentUtils.makeActiveContentOrVariantDirty();
      var sType = null;

      if (oElement.contentAttributes) {
        sKey = "attributeValue";
        var aAttributes = oActiveEntity.attributes;
        oInnerElement = CS.find(aAttributes, {id: oElement.contentAttributes[0].id});
        sType = oElement.attribute.type;
        if (sButtonKey == "ok") {
          if (oInnerElement.attributeId == "nameattribute") {
            oActiveEntity.name = oInnerElement.notification[sKey];
          }
          if (ContentUtils.isAttributeTypeHtml(sType)) {
            oInnerElement.valueAsHtml = oInnerElement.notification[sKey];
          }
        }
      } else if (oElement.contentTags) {
        sKey = "tagValue";
        var aTags = oActiveEntity.tags;
        oInnerElement = CS.find(aTags, {id: oElement.contentTags[0].id});
      } else if (oElement.type == "role") {
        sKey = "candidates";
        var aRoles = oActiveEntity.roles;
        oInnerElement = CS.find(aRoles, {roleId: oElement.role.id});
      }
      _handleNotificationConfirmation(sKey, oInnerElement, sButtonKey);
    },

    fetchArticleById: function (sArticleId, oCallbackData, oExtraData) {
      return _fetchArticleById(sArticleId, oCallbackData, oExtraData);
    },

    handleClassVisibilityChanged: function (sId) {
      _handleClassVisibilityChanged(sId);
    },

    handleAdvancedSearchListLoadMoreClicked: function (sType, sContext) {
      _handleAdvancedSearchListLoadMoreClicked(sType, sContext);
    },

    handleAdvancedSearchListSearched: function (sSearchText, sContext) {
      _handleAdvancedSearchListSearched(sSearchText, sContext);
    },

    handleAvailableEntityFilterButtonClicked: function (oCallBackData, oExtraData, sSelectedHierarchyContext) {
      return _handleAvailableEntityFilterButtonClicked(oCallBackData, oExtraData, sSelectedHierarchyContext);
    },

    handleMakeDefaultImageButtonClicked: function (sActiveAssetId) {
      _makeDefaultContentImage(sActiveAssetId);
      _triggerChange();
    },

    handleCommentChanged: function (sNewValue) {
      _handleCommentChanged(sNewValue);
      _triggerChange();
    },

    handleAfterEffectsOfStaticCollectionNotFound: function (oFilterContext) {
      _handleAfterEffectsOfStaticCollectionNotFound(oFilterContext);
    },

    handleXRayPropertyClicked: function (aProperties, sRelationshipId, sRelationshipIdToFetchData, oFilterContext) {
      _handleXRayPropertyClicked(aProperties, sRelationshipId, sRelationshipIdToFetchData, oFilterContext);
    },

    handleRuntimePropertyCollectionLoadmoreSearchClicked: function (oData) {
      if (oData.loadMore) {
        _loadMoreFetchEntities(oData.entities);
      } else {
        _handleSearchAndFetchEntities(oData.entities, oData.searchText || "");
      }
    },

    handleRuntimePropertyCollectionShowDropDown: function () {
      var aEntityList = [ConfigDataEntitiesDictionary.PROPERTY_COLLECTIONS];
      _loadMoreFetchEntities(aEntityList);
    },

    handleShowXRayPropertyGroupsClicked: function (bMakeDefaultSelected, bIsNotForXRay, oFilterContext, sRelationshipId, sRelationshipIdToFetchData) {
      return _handleShowXRayPropertyGroupsClicked(bMakeDefaultSelected, bIsNotForXRay, oFilterContext, sRelationshipId, sRelationshipIdToFetchData);
    },

    handleXRayPropertyGroupClicked: function (sId, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
      _handleXRayPropertyGroupClicked(sId, sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext);
    },

    handleCloseActiveXRayPropertyGroupClicked: function (sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext) {
      _handleCloseActiveXRayPropertyGroupClicked(sRelationshipId, sRelationshipIdForFetchingXRAYData, oFilterContext);
    },

    handleAssetEventScheduleFieldChanged: function (sKey, sValue, bIsPromotionCampaignEvent) {
      _handleAssetEventScheduleFieldChanged(sKey, sValue, bIsPromotionCampaignEvent);
    },

    handleContentEventImageChanged: function (aFiles, oFilterContext) {
      _handleContentEventImageChanged(aFiles, oFilterContext);
    },

    handleEntityTabClicked: function (sTabId, sContextId, oFilterContext) {
      _handleEntityTabClicked(sTabId, sContextId, oFilterContext);
    },

    fetchVariant: function (oModel) {
      _fetchVariant(oModel);
    },

    handleCircledTagNodeClicked: function (sTagGroupId, sTagId, sContext, oExtraData) {
      _handleCircledTagNodeClicked(sTagGroupId, sTagId, sContext, oExtraData);
    },

    handleAssignImageToVariantButtonClicked: function (sVariantId, sAssetId, sContext) {
      _handleAssignImageToVariantButtonClicked(sVariantId, sAssetId, sContext);
      _triggerChange();
    },

    handleDeleteGeneratedAssetLinkClicked: function () {
      _handleDeleteGeneratedAssetLinkClicked();
    },

    handleVariantCloseImageButtonClicked: function (sContext) {
      _handleVariantCloseImageButtonClicked(sContext);
      _triggerChange();
    },

    handleImageGalleryDialogViewVisibilityStatusChanged: function (oFilterContext) {
      let oCallbackData = {
        functionToExecute: function () {
          _toggleVariantOpenImageDialogStatus();
        },
        filterContext: oFilterContext
      };
      _handleImageGalleryDialogViewVisibilityStatusChanged(oCallbackData);
    },

    handleTaskDialogOpenClicked: function (oProperty) {
      _handleTaskDialogOpenClicked(oProperty);
    },

    fetchAllTasksForAnnotations: function (oImageAttribute) {
      _fetchAllTasksForAnnotations(oImageAttribute);
    },

    handleTaskDataChanged: function (oData) {
      _handleTaskDataChanged(oData);
    },

    updateMandatoryElementsStatus: function (oActiveEntity) {
      _updateMandatoryElementsStatus(oActiveEntity);
    },

    handleDummyLinkedVariantDiscard: function () {
      _handleDummyLinkedVariantDiscard();
    },

    cleanReferencedNatureRelationshipElements: function () {
      _cleanReferencedNatureRelationshipElements();
    },

    handleEditViewFilterOptionChanged: function (sId) {
      _handleEditViewFilterOptionChanged(sId);
    },

    handleAcrolinxResultChecked: function (sScoreValue, sScoreCardUrl) {
      _handleAcrolinxResultChecked(sScoreValue, sScoreCardUrl);
    },

    handleEntityNavigation: function (oItem) {
      _handleEntityNavigation(oItem);
    },

    handleClearCollectionProps: function () {
      _handleClearCollectionProps();
    },

    handleVariantSummaryEditButtonClicked: function (sClickedVariantId, bNoTrigger) {
      _handleVariantSummaryEditButtonClicked(sClickedVariantId, bNoTrigger);
    },

    handleVariantSummaryCloseEditView: function () {
      _handleVariantSummaryCloseEditView();
    },

    handleTaxonomyChildrenLazyData: function (oCallBackData, oExtraData) {
      CollectionAndTaxonomyHierarchyStore.handleTaxonomyChildrenLazyData(oCallBackData, oExtraData);
    },

    processGetArticleResponse: function (oResponse, bForComparisionView, sContextId, bIsSaveResponse) {
      _processGetArticleResponse(oResponse, bForComparisionView, sContextId, bIsSaveResponse);
    },

    successFetchArticleById: function (oCallbackData, oResponse) {
      successFetchArticleById(oCallbackData, oResponse);
    },

    handleContentHorizontalTreeNodeCollapseClickedChildrenRemove: function (oReqData) {
      _handleContentHorizontalTreeNodeCollapseClickedChildrenRemove(oReqData);
    },

    handleContentHorizontalTreeNodeCollapseClickedNoChildrenRemove: function (oReqData) {
      _handleContentHorizontalTreeNodeCollapseClickedNoChildrenRemove(oReqData);
    },

    handleRuntimePCSectionExpansionToggled: function (sSectionId) {
      _handleRuntimePCSectionExpansionToggled(sSectionId);
    },

    clearFilterHierarchyRelatedData: function (oCallback) {
      _clearFilterHierarchyRelatedData(oCallback);
    },

    handleModuleItemClicked: function (oCallBackData, oFilterContext) {
      oCallBackData.filterContext = oFilterContext;

      return _handleFilterButtonWrapperClicked(oCallBackData);
    },

    handleExploreContentButtonClickedFromDashboard: function (oCallBackData) {
      oCallBackData.preventResetFilterProps = true;
      return _handleFilterButtonWrapperClicked(oCallBackData);
    },

    changeVariantCellValue: function (oCellData, sTableContextId) {
      VariantStore.handleVariantCellValueChanged(oCellData, sTableContextId);
    },

    fetchUOMVariant: function (sArticleId, oCallbackData, oExtraData) {
      _fetchUOMVariant(sArticleId, oCallbackData, oExtraData);
    },

    mapContentsForServer: function (oContent) {
      return _mapContentsForServer(oContent);
    },

    handleSectionElementConflictIconClicked: function (sElementId, bShowConflictSourcesOnly, bIsConflictResolved, bDisableLoader) {
      if (bShowConflictSourcesOnly) {
        _getPossibleConflictingSources(sElementId, true, bShowConflictSourcesOnly, bIsConflictResolved);
      } else {
        _handleSectionElementConflictIconClicked(sElementId, bDisableLoader, bShowConflictSourcesOnly, bIsConflictResolved);
      }
    },

    handleContextMenuListItemClicked: function (sSelectedId, sContext, oFilterContext) {
      _handleContextMenuListItemClicked(sSelectedId, sContext, oFilterContext);
    },

    handleHandleTransferDialogClicked: function (sContext, sButtonId) {
      _handleHandleTransferDialogClicked(sContext, sButtonId);
    },

    handleHandleTransferDialogItemClicked: function (sContext, sSelectedId) {
      _handleHandleTransferDialogItemClicked(sContext, sSelectedId);
    },

    handleTransferDialogCheckBoxToggled: function (sContext) {
      _handleTransferDialogCheckBoxToggled(sContext);
    },

    handleContextMenuButtonClicked: function (sContext, oFilterContext) {
      _handleContextMenuButtonClicked(sContext, oFilterContext);
    },

    handleContextMenuListVisibilityToggled: function (bIsVisibility, sContext) {
      _handleContextMenuListVisibilityToggled(bIsVisibility, sContext);
    },

    handleContentLinkedSectionsLinkItemClicked: function (sSectionId, bDoNotTrigger) {
      _setSelectedContextProps("", "", "", sSectionId, "", "");
      if (!bDoNotTrigger) {
        _triggerChange();
      }
    },

    handleContentKPITileOpenDialogButtonClicked: function (sTileId) {
      return _handleContentKPITileOpenDialogButtonClicked(sTileId);
    },

    handleContentKPITileCloseDialogButtonClicked: function (sTileId) {
      return _handleContentKPITileCloseDialogButtonClicked(sTileId);
    },

    handleRestoreContentsButtonClicked: function (aContentIdsToRestore) {
      _handleRestoreContentsButtonClicked(aContentIdsToRestore);
    },

    handleSliderImageClicked: function (sSelectedImageID) {
      _setContentSelectedImageId(sSelectedImageID);
      _triggerChange();
    },

    handleGridViewSelectButtonClicked: function (aSelectedContentIds, bSelectAllClicked) {
      _handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
    },

    changeButtonActiveState: function (sButtonId) {
      _changeButtonActiveState(sButtonId);
    },

    handleRemoveSelectedContentGridEntities: function (sRelationshipId, sRelationshipType, aGridSelectedContentIds, fSuccessHandler) {
      _handleRemoveSelectedContentGridEntities(sRelationshipId, sRelationshipType, aGridSelectedContentIds, fSuccessHandler)
    },

    handleContentDataLanguageChanged: function (sLanguageCode, oFilterContext) {
      _handleContentDataLanguageChanged(sLanguageCode, oFilterContext);
    },

    handleDeleteTranslationClicked: function (sLanguageCode) {
      _handleDeleteTranslationClicked(sLanguageCode);
    },

    deleteCurrentLanguageContent: function () {
      _deleteCurrentLanguageContent();
    },

    checkForExtraNecessaryServerCalls: function (oResponse) {
      _checkForExtraNecessaryServerCalls(oResponse)
    },

    dataLanguagePopoverVisibilityToggled: function (oFilterContext) {
      _dataLanguagePopoverVisibilityToggled(oFilterContext);
    },

    discardAppliedFilterDirtyChanges: function (oFilterContext) {
      _discardAppliedFilterDirtyChanges(oFilterContext);
    },

    handleLinkedSectionButtonClicked: function (oItem) {
      _handleLinkedSectionButtonClicked(oItem);
    },

    setContentOpenReferencedData: function (oResponse, oActiveContent) {
      _setContentOpenReferencedData(oResponse, oActiveContent)
    },

    handleEntityESCButtonClicked: function () {
      _handleEntityESCButtonClicked();
    },

    handleKpiSummaryViewKpiSelected: function (sKPIId) {
      _handleKpiSummaryViewKpiSelected(sKPIId);
      _triggerChange();
    },

    processDataForFetchContentList: function (oCallbackData, oExtraData) {
      _processDataForFetchContentList(oCallbackData, oExtraData);
    },

    failureFetchContentListCallBack: function (oCallBack, oResponse) {
      failureFetchContentListCallBack(oCallBack, oResponse)
    },

    fetchSmartDocumentConfigDetails: function () {
      _fetchSmartDocumentConfigDetails();
    },

    getAllAssetExtensions: function () {
      return _getAllAssetExtensions();
    },

    fetchTabSpecificContentsList: function (sScreenContext, oPaginationData) {
      _fetchTabSpecificContentsList(sScreenContext, oPaginationData);
    },

    handleRelationshipConflictPropertyResolved: function (oSelectedConflictValue, oRelationship) {
      _handleRelationshipConflictPropertyResolved(oSelectedConflictValue, oRelationship);
    },

    getSide2NatureKlassFromNatureRelationship: function (sRelationshipId) {
      return CloneWizardStore.getSide2NatureKlassFromNatureRelationship(sRelationshipId);
    },

    handleDateRangePickerDateChange: function (sContext, oSelectedTimeRange) {
      _handleDateRangePickerDateChange(sContext, oSelectedTimeRange);
    },

    handleDateRangePickerCancelClicked: function (sContext) {
      _handleDateRangePickerCancelClicked(sContext);
    },

    triggerChange: function () {
      _triggerChange();
    },

    getXRayProperties: function () {
      return _getXRayProperties();
    },

    handleApplyClassification: function (oADM, oCallBack) {
      _handleApplyClassification(oADM, oCallBack);
    },

    handleMultiClassificationDialogButtonClicked: function (sDialogButtonId, sContext) {
      if(sContext === "minorTaxonomiesSectionView"){
        let oCallback = {};
        switch (sDialogButtonId) {

          case "apply":
            //TODO: check _fetchKpiSummaryData method required or not
            oCallback.functionToExecute = _processGetArticleResponse;
            NewMinorTaxonomyStore.handleMinorTaxonomiesApplyButtonClicked(oCallback);
          break;
          case "cancel":
            oCallback.functionToExecute = () => {
              let oActiveEntity = ContentUtils.getActiveContent();
              _fetchArticleById(oActiveEntity.id, {baseType: oActiveEntity.baseType});
            };
            NewMinorTaxonomyStore.resetMultiClassificationProps(oCallback);
        }
      } else {
        _handleMultiClassificationDialogButtonClicked(sDialogButtonId, {});
      }
    },

    preProcessAndSetReferencedAssets: function (oReferencedAssets, bIsMergeOldReferencedAssetList) {
      return _preProcessAndSetReferencedAssets(oReferencedAssets, bIsMergeOldReferencedAssetList);
    },

    processAttributeVariantsStats: function (oAttributeVariantsStats, oReferencedAttributes, oReferencedElements) {
      return _processAttributeVariantsStats(oAttributeVariantsStats, oReferencedAttributes, oReferencedElements);
    },

    fetchAssetClassList: function (oFilterContext) {
      _fetchAssetClassList(oFilterContext);
    },

    handleModuleCreateButtonClicked: function (sSearchedText, sContext, oFilterContext, bIsLoadMoreClicked) {
      _handleModuleCreateButtonClicked(sSearchedText, sContext, oFilterContext, bIsLoadMoreClicked);
    },

    validateContextDuplication: function (oCallback) {
      return _validateContextDuplication(oCallback);
    },

  }

})();

MicroEvent.mixin(ContentStore);

export default ContentStore;
