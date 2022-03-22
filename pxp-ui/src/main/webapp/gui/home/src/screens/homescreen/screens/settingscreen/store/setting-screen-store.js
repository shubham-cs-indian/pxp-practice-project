
import alertify from '../../../../../commonmodule/store/custom-alertify-store';
import CS from '../../../../../libraries/cs';
import MicroEvent from '../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import RequestMapping from '../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import {
  IndesignServerRequestMapping as oIndesignServerRequestMapping,
  UploadRequestMapping as oUploadRequestMapping,
  VariantConfigurationRequestMapping as oVariantConfigurationRequestMapping,
  AdministrationSummaryRequestMapping as oAdministrationSummaryRequestMapping,
} from '../tack/setting-screen-request-mapping';
import { SettingsRequestMapping as oSettingsRequestMapping } from '../tack/setting-screen-request-mapping';
import { TabsRequestMapping as oTabsRequestMapping } from '../tack/setting-screen-request-mapping';
import SettingScreenAppData from './model/setting-screen-app-data';
import SettingScreenProps from './model/setting-screen-props';
import GridViewContexts from '../../../../../commonmodule/tack/grid-view-contexts';
import { setting as oSettingModules } from '../../../../../commonmodule/tack/global-modules-dictionary';
import ViewContextConstants from '../../../../../commonmodule/tack/view-context-constants';
import ConfigModulesDictionary from '../tack/settinglayouttack/config-modules-dictionary';
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';
import TaxonomyTypeDictionary from '../../../../../commonmodule/tack/taxonomy-type-dictionary';
import AttributeTypeDictionary from '../../../../../commonmodule/tack/attribute-type-dictionary-new';
import ActionDialogProps from './../../../../../commonmodule/props/action-dialog-props';
import TranslationStore from '../../../../../commonmodule/store/translation-store';
import SessionStorageManager from '../../../../../libraries/sessionstoragemanager/session-storage-manager';
import SettingUtils from './helper/setting-utils';
import ClassUtils from './helper/class-utils';
import TagStore from './helper/tag-store';
import AttributeStore from './helper/attribute-store';
import CalculatedAttributeStore from './helper/calculated-attribute-store';
import ContextStore from './helper/context-store';
import TasksStore from './helper/task-store';
import DataGovernanceTasksStore from './helper/data-governance-tasks-store';
import RelationshipStore from './helper/relationship-store';
import ClassStore from './helper/class-store';
import PropertyCollectionStore from './helper/property-collection-store';
import TemplateStore from './helper/template-store';
import UserStore from './helper/user-store';
import ProfileStore from './helper/profile-store';
import MappingStore from './helper/mapping-store';
import EndpointStore from './helper/endpoint-store';
import RoleStore from './helper/role-store';
import ClassSettingStore from './helper/class-setting-store';
import RuleStore from './helper/rule-store';
import KpiStore from './helper/kpi-store';
import RuleListStore from './helper/rule-list-store';
import AttributionTaxonomyStore from './helper/attribution-taxonomy-store';
import LanguageTreeStore from './helper/language-tree-store';
import SmartDocumentStore from './helper/smart-document-store';
import TranslationsStore from './helper/translations-store';
import PermissionStore from './helper/permission-store';
import TableStore from './helper/table-view-store';
import ProcessStore from './helper/process-store';
import OrganisationConfigStore from './helper/organisation-config-store';
import GoldenRecordsStore from './helper/golden-record-store';
import TabsStore from './helper/tabs-store';
import SettingLayoutStore from './setting-layout-store';
import ProfileProps from '../store/model/profile-config-view-props';
import KPIProps from '../store/model/kpi-config-view-props';
import OrganisationConfigViewProps from '../store/model/organisation-config-view-props';
import ConfigEntityTypeDictionary from '../../../../../commonmodule/tack/config-entity-type-dictionary';
import oDataModelPropertyGroupTypeConstants from './../tack/settinglayouttack/config-module-data-model-property-group-type-dictionary';
import CustomSearchDialogStore from '../store/helper/custom-search-dialog-store';
import ConfigModulesList from '../tack/settinglayouttack/config-modules-list';
import ThemeConfigurationScreenStore from './helper/theme-configuration-store.js';
import ViewConfigurationScreenStore from './helper/view-configuration-store';
import SharableURLStore from '../../../../../commonmodule/store/helper/sharable-url-store';
import {getTranslations as oTranslations, init as oTranslationManagerInit, getLanguageInfo as oLanguageInfo} from '../../../../../commonmodule/store/helper/translation-manager.js';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import CustomActionDialogStore from '../../../../../commonmodule/store/custom-action-dialog-store';
import { communicator as HomeScreenCommunicator } from '../../../store/home-screen-communicator';
import SessionStorageConstants from '../../../../../commonmodule/tack/session-storage-constants';
import ThemeConfigurationConstants from "../tack/mock/theme-configuration-constants";
import ProcessFilerStore from './helper/process-filter-store';
import ViewLibraryUtils from '../../../../../viewlibraries/utils/view-library-utils';
import SsoSettingStore from '../store/helper/sso-setting-store';
import SSOSettingStore from "./helper/sso-setting-store";
import AuthorizationStore from './helper/authorization-store';
import PdfReactorServerStore from "./helper/pdf-reactor-server-store";
import ManageEntityStore from "./helper/config-manage-entity-store";
import ManageEntityConfigProps from "./model/manage-entity-config-props";
import GridEditStore from './helper/grid-edit-store';
import AuditLogStore from './helper/auditlogstore/audit-log-store';
import ExportSide2RelationshipDictionary from '../../../../../commonmodule/tack/export-side2-relationship-dictionary'
import DownloadTrackerConfigStore from './helper/download-tracker-config-store';
import GridFilterStore from './helper/grid-filter-store';
import ColumnOrganizerProps from "../../../../../viewlibraries/columnorganizerview/column-organizer-props";
import GridViewStore from "../../../../../viewlibraries/contextualgridview/store/grid-view-store";
import GridViewColumnIdConstants from "../../../../../viewlibraries/contextualgridview/tack/grid-view-column-id-constants";
import oDataModelImportExportEntityTypeConstants from '../tack/settinglayouttack/config-module-import-export-entity-list-dictionary';
import IconLibraryStore from "./helper/icon-library-store";
import IconLibraryProps from "./model/icon-library-props";
import LanguageTreeProps from "./model/language-tree-config-view-props";
import IconLibrarySelectIconDialogProps from "./model/icon-library-select-icon-dialog-props";
import IndesignServerConfigurationStore from "./helper/indesign-server-configuration-store";
import DAMConfigurationViewProps from "./model/dam-configuration-view-props";
import {getTranslations as getTranslation} from "../../../../../commonmodule/store/helper/translation-manager";
import MockDataForListViewActionItemButton from "../tack/list-view-action-item-button-list";
import VariantConfigurationViewProps from "./model/variant-configuration-view-props";
import CommonUtils from "../../../../../commonmodule/util/common-utils";
import MockDataForAdministrationsSummaryHeader from "../tack/mock/mock-data-for-administration-summary-dictionary";
import AdministrationsSummaryProps from "./model/administration-summary-view-props";

//TODO: #Clean Up:- Show alertify and error message with Translation
var SettingScreenStore = (function () {

  var oAppData = SettingScreenAppData;
  var oComponentProps = SettingScreenProps;

  var _triggerChange = function () {
    SettingScreenStore.trigger('change');
  };

  var _getAppData = function () {
    return oAppData;
  };

  var _getComponentProps = function () {
    return oComponentProps;
  };

  //**************************************** Private API's ****************************************************//

  var _resetPermissionConfigDetails = function () {
    oComponentProps.permissionConfigView.reset();
    var RoleProps = _getComponentProps().roleConfigView;
    RoleProps.setSelectedRole({});
    RoleStore.fetchRoleList();
    ClassStore.fetchClassTree();
    TemplateStore.setUpTemplateConfigGridView();
    TagStore.fetchTagList("all");
    RelationshipStore.fetchRelationshipsList();
    PropertyCollectionStore.fetchPropertyCollectionsList();
    ClassStore.fetchClassesList();
    TasksStore.fetchTasksList();
  };

  var _resetContextConfigDetails = function (){
    TagStore.fetchTagList('all');
    ContextStore.setActiveContext({}); //To clear details view of screen.
    ContextStore.setUpContextConfigGridView();
  };

  var _fetchRootNodeClassByType = function (sType) {

    switch (sType){

      case 'class':
        ClassStore.fetchContentClasses();
        break;

      case 'task':
        ClassStore.fetchTaskClasses();
        break;

      case 'asset':
        ClassStore.fetchAssetClasses();
        break;

      case 'target':
        ClassStore.fetchTargetClasses();
        break;

      case 'textasset':
        ClassStore.fetchTextAssetClasses();
        break;

      case 'supplier':
        ClassStore.fetchSupplierClasses();
        break;
    }
  };

  var _saveConfigDataNew = function (oCallbackData) {
    let sActiveScreenName = SettingUtils.getConfigScreenViewName();
    let sSplitter = SettingUtils.getSplitter();

    switch (sActiveScreenName.split(sSplitter)[0]) {
      case oDataModelPropertyGroupTypeConstants.MASTER:
      case oDataModelPropertyGroupTypeConstants.LOV:
      case oDataModelPropertyGroupTypeConstants.STATUS:
      case oDataModelPropertyGroupTypeConstants.BOOLEAN:
        _handleGridViewSaveButtonClicked(oCallbackData, GridViewContexts.TAG);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS:
        ClassStore.saveClass(oCallbackData);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION:
        PropertyCollectionStore.savePropertyCollection(oCallbackData);
        break;

      case oDataModelPropertyGroupTypeConstants.HTML:
      case oDataModelPropertyGroupTypeConstants.TEXT:
      case oDataModelPropertyGroupTypeConstants.NUMBER:
      case oDataModelPropertyGroupTypeConstants.DATE:
      case oDataModelPropertyGroupTypeConstants.CALCULATED:
      case oDataModelPropertyGroupTypeConstants.CONCATENATED:
      case oDataModelPropertyGroupTypeConstants.PRICE:
      case oDataModelPropertyGroupTypeConstants.MEASUREMENT:
      case oDataModelPropertyGroupTypeConstants.STANDARD:
        _handleGridViewSaveButtonClicked(oCallbackData, GridViewContexts.ATTRIBUTE);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TABS:
        _handleGridViewSaveButtonClicked(oCallbackData, GridViewContexts.TABS_CONFIG);
        break;
      case 'permissions':
        PermissionStore.savePermission(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TEMPLATE:
        TemplateStore.postProcessTemplateAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP:
          RelationshipStore.postProcessRelationshipListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_USER:
        UserStore.postProcessUserListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_ROLE:
        RoleStore.saveRole(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROFILE:
        ProfileStore.postProcessEndpointListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_MAPPING:
        MappingStore.processProcessMappingListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_RULE:
        RuleStore.postProcessRuleListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
        ContextStore.processProcessContextListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST:
        RuleListStore.saveRuleList(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TASK:
        TasksStore.postProcessTaskAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_DATAGOVERNANCE:
        DataGovernanceTasksStore.postProcessDataGovernanceTaskListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
        AttributionTaxonomyStore.saveTaxonomyDetails(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS:
        ProcessStore.processListBulkSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_ORGANISATION:
        OrganisationConfigStore.saveLockedOrganisationConfigScreen(oCallbackData);
        break;
      case "translations":
        _handleGridViewSaveButtonClicked(oCallbackData, GridViewContexts.TRANSLATIONS);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_GOLDEN_RECORDS:
        GoldenRecordsStore.postProcessGoldenRuleListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_KPI:
        KpiStore.postProcessKpiListAndSave(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY:
        LanguageTreeStore.saveLanguage(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_SMARTDOCUMENT:
        SmartDocumentStore.saveSmartDocumentSection(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_AUTHORIZATION:
        AuthorizationStore.saveAuthorizationMapping(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_GRIDEDITVIEW:
        GridEditStore.handleSaveAction(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_THEME_CONFIGURATION:
      case ConfigEntityTypeDictionary.ENTITY_TYPE_VIEW_CONFIGURATION:
        ThemeConfigurationScreenStore.handleSaveAction(false, oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_PDFREACTORSERVER:
        PdfReactorServerStore.handleSaveAction(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_VARIANT_CONFIGURATION:
        _handleVariantActionButtonClicked("save", oCallbackData);
        break;
    }

  };

  var _discardConfigDataNew = function (oCallbackData) {
    let sActiveScreenName = SettingUtils.getConfigScreenViewName();
    let sSplitter = SettingUtils.getSplitter();

    switch (sActiveScreenName.split(sSplitter)[0]) {
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TAG:
      case oDataModelPropertyGroupTypeConstants.MASTER:
      case oDataModelPropertyGroupTypeConstants.LOV:
      case oDataModelPropertyGroupTypeConstants.STATUS:
      case oDataModelPropertyGroupTypeConstants.BOOLEAN:
        _handleGridViewDiscardButtonClicked(oCallbackData, GridViewContexts.TAG);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS:
        ClassStore.discardUnsavedClass(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION:
        PropertyCollectionStore.discardUnsavedPropertyCollection(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTE:
      case oDataModelPropertyGroupTypeConstants.HTML:
      case oDataModelPropertyGroupTypeConstants.TEXT:
      case oDataModelPropertyGroupTypeConstants.NUMBER:
      case oDataModelPropertyGroupTypeConstants.DATE:
      case oDataModelPropertyGroupTypeConstants.CALCULATED:
      case oDataModelPropertyGroupTypeConstants.CONCATENATED:
      case oDataModelPropertyGroupTypeConstants.PRICE:
      case oDataModelPropertyGroupTypeConstants.MEASUREMENT:
      case oDataModelPropertyGroupTypeConstants.STANDARD:
        _handleGridViewDiscardButtonClicked(oCallbackData, GridViewContexts.ATTRIBUTE);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TABS:
        _handleGridViewDiscardButtonClicked(oCallbackData, GridViewContexts.TABS_CONFIG);
        break;
      case 'permissions':
        PermissionStore.discardUnsavedPermission(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TEMPLATE:
        TemplateStore.discardTemplateGridViewChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP:
        RelationshipStore.discardRelationshipGridViewChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_USER:
        UserStore.discardUserGridViewChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_ROLE:
        RoleStore.discardUnsavedRole(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROFILE:
        ProfileStore.discardEndpointGridViewChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_RULE:
        RuleStore.discardRuleListChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
        ContextStore.discardContextListChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST:
        RuleListStore.discardUnsavedRuleList(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TASK:
        TasksStore.discardTasksGridViewChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_DATAGOVERNANCE:
        DataGovernanceTasksStore.discardDataGovernanceGridViewChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
        AttributionTaxonomyStore.discardTaxonomyDetails(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_ORGANISATION:
        OrganisationConfigStore.discardLockedOrganisationConfigScreen(oCallbackData);
        break;
      case "translations":
        _handleGridViewDiscardButtonClicked(oCallbackData, GridViewContexts.TRANSLATIONS);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_MAPPING:
        MappingStore.discardMappingListChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS:
        ProcessStore.discardProcessGridViewChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_GOLDEN_RECORDS:
        GoldenRecordsStore.discardGoldenRuleListChanges(oCallbackData);
      case ConfigEntityTypeDictionary.ENTITY_TYPE_KPI:
        KpiStore.discardKpiListChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY:
        LanguageTreeStore.discardLanguage(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_THEME_CONFIGURATION:
      case ConfigEntityTypeDictionary.ENTITY_TYPE_VIEW_CONFIGURATION:
        ThemeConfigurationScreenStore.handleDiscardAction(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_VIEW_CONFIGURATION:
        ViewConfigurationScreenStore.handleDiscardAction();
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_SMARTDOCUMENT:
        SmartDocumentStore.discardSmartDocumentChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_AUTHORIZATION:
        AuthorizationStore.discardAuthorizationMappingListChanges(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_PDFREACTORSERVER:
        PdfReactorServerStore.handleDiscardAction(oCallbackData);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_GRIDEDITVIEW:
        GridEditStore.handleDiscardAction(oCallbackData);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_VARIANT_CONFIGURATION:
        _handleVariantActionButtonClicked("discard", oCallbackData);
        break;
    }
  };

  var _getDataLostAlert = function (oSection, iNewCount, sContext, sModule) {
    var bShowAlert = false;
    var iOldCount = sContext == "column" ? oSection.columns : oSection.rows;
    if (iNewCount < iOldCount) {
      var aSectionElements = oSection.elements;
      CS.forEach(aSectionElements, function (oElement) {
        var oStartPos = sModule == "propertycollection" ? oElement.position : oElement.startPosition;
        if (sContext == "column" && oStartPos.y > (iNewCount - 1)) {
          bShowAlert = true;
          return;
        } else if (sContext == "row" && oStartPos.x > (iNewCount - 1)) {
          bShowAlert = true;
          return;
        }
      })
    }
    return bShowAlert;
  };

  let _getIsValidAssetFileTypes = function (oFile, sNatureType) {
    let oScreenProps = SettingScreenProps.screen;
    let oAssetExtensions = oScreenProps.getAssetExtensions();
    let aActiveAssetExtension = oAssetExtensions[sNatureType] ? oAssetExtensions[sNatureType] : oAssetExtensions.allAssets;
    let sTypeRegex = aActiveAssetExtension.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var successPostIconToAsset = function (sUniqueId, oCallback, oResponse) {
    const oAssetInfo = oResponse.success.assetKeysModelList[0];
    if (oCallback && oCallback.functionToExecute) {
      oCallback.getAssetInfo ? oCallback.functionToExecute(oAssetInfo) : oCallback.functionToExecute(oAssetInfo.thumbKey);
    }
    _triggerChange();

    return oAssetInfo;
  };

  var failurePostIconToAsset = function (oResponse) {
    ExceptionLogger.log(oResponse);
    let aErrors = [];
    let aProcessedAssetErrors = [];
    let oFailure = oResponse.failure;
    let aExceptionDetails = oFailure.devExceptionDetails;
    CS.forEach(aExceptionDetails, function (oException) {
      if(!_isUploadedAssetProcessed(oException.key)){
        !CS.includes(aProcessedAssetErrors, oException.itemName) && aProcessedAssetErrors.push(oException.itemName);
      } else {
        aErrors.push(oException)
      }
    });
    if(CS.isNotEmpty(aProcessedAssetErrors)){
      alertify.error(`${oTranslations().UploadedAssetsCouldNotBeProcessed} ${aProcessedAssetErrors.join(", ")}`);
    }
    if (CS.isNotEmpty(aErrors)) {
      oResponse.failure.exceptionDetails = aErrors;
      SettingUtils.failureCallback(oResponse, "failurePostIconToAsset", oTranslations());
    }
  };

  var _postIconToAsset = function (file, oCallback) {
    var sUniqueId = UniqueIdentifierGenerator.generateUUID();
    var data = new FormData();
    data.append('fileUpload', file);
    data.append('fileUpload', sUniqueId);
    return CS.customPostRequest(RequestMapping.getRequestUrl(oUploadRequestMapping.UploadImage) + "?mode=config&size=20&klassId=asset_asset", data,
        successPostIconToAsset.bind(this, sUniqueId, oCallback), failurePostIconToAsset, false);
  };

  var _uploadIconToAsset = async function (aFiles, bLimitImageSize, oCallback) {
    let dummyPromise = Promise.resolve();
    if (aFiles.length) {
      var oFile = aFiles[0];
     if(_getIsValidAssetFileTypes(oFile)) {
        if (bLimitImageSize && oFile.size > 50000) {
          alertify.error(oTranslations().FILE_SIZE_EXCEEDS_MAXIMUM);
          return dummyPromise;
        }
        return _postIconToAsset(oFile, oCallback);
      } else {
       alertify.error(`${ViewLibraryUtils.getDecodedTranslation(oTranslations().AssetFileTypeNotSupportedException, {assetName: oFile.name})}`);
      }
    } else {
      if (oCallback.functionToExecute) {
        oCallback.functionToExecute("");
      }
    }

    return dummyPromise;
  };

  let _isUploadedAssetProcessed = function (sException) {
    let aAssetExceptions = ["ExifToolException", "ImageMagickException", "FFMPEGException", "ApachePOIXWPFException"];
    return CS.includes(aAssetExceptions, sException) ?  false : true;
  };

  var _resetAndFetchEntitiesForPropertyCollection = function (aEntityList, bLoadMore, oCallback) {
    SettingScreenProps.screen.resetEntitiesPaginationData();
    _fetchEntitiesForPropertyCollection(aEntityList, bLoadMore, oCallback);
  };;

  var _toggleHierarchyScrollEnableDisableProp = function () {
    var oScreenProps = SettingScreenProps.screen;
    var bCurrentVal = oScreenProps.getIsHierarchyTreeScrollAutomaticallyEnabled();
    oScreenProps.setIsHierarchyTreeScrollAutomaticallyEnabled(!bCurrentVal);
  };

  var _resetCalculatedAttributeConfigViewProps = function () {
    SettingScreenProps.calculatedAttributeConfigView.reset();
  };

  var _loadInitialEntityData = function (sContext) {
    let aEntityData = [];
    let oAppData = SettingUtils.getAppData();
    switch (sContext) {

      /** Todo Garry: If Lazy Then We Wont Need Extra Case To Handle Tabs Loading in _loadMoreFetchEntities **/
      case ConfigEntityTypeDictionary.ENTITY_TYPE_TABS:
      case 'tabId':
        sContext = ConfigDataEntitiesDictionary.TABS;
        aEntityData = oAppData.getCustomTabList();
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_DASHBOARD_TAB:
        sContext = ConfigDataEntitiesDictionary.DASHBOARD_TABS;
        aEntityData = oAppData.getDashboardTabsList();
        break;

      case ConfigDataEntitiesDictionary.TEMPLATES:
        aEntityData = oAppData.getTemplates();
        break;
    }
    let aEntityList = CS.isEmpty(sContext) ? [] : [sContext];

    if (!CS.isEmpty(aEntityData)) {
      return;
    }
    _loadMoreFetchEntities(aEntityList);
  };

  let _handleRuleDetailsKlassTypeMSSChanged = function (aNewKlassType) {
    RuleStore.handleRuleDetailsKlassTypeMSSChanged(aNewKlassType);
  };

  let _handleRuleDetailsKlassMSSChanged = function (sContext, aSelectedKlasses) {
    RuleStore.handleRuleDetailsKlassMSSChanged(sContext, aSelectedKlasses);
  };

  var _loadMoreFetchEntities = function (aEntityList, oCallback) {
    var oPaginationData = SettingScreenProps.screen.getEntitiesPaginationData();
    CS.forEach(aEntityList, function (sEntity) {
      let aEntityList = [];
      var oEntityPagination = oPaginationData[sEntity];
      if (!oEntityPagination) {
        return;
      }
      switch (sEntity) {
        case ConfigDataEntitiesDictionary.ATTRIBUTES:
          aEntityList = oAppData.getAttributeList();
          break;
        case ConfigDataEntitiesDictionary.TAGS:
          aEntityList = oAppData.getTagMap();
          break;
        case ConfigDataEntitiesDictionary.TAXONOMIES:
          aEntityList = oAppData.getTaxonomyListMap();
          break;
        case ConfigDataEntitiesDictionary.ROLES:
          aEntityList = oAppData.getRoleList();
          break;
        case ConfigDataEntitiesDictionary.RELATIONSHIPS:
          aEntityList = oAppData.getRelationshipMasterList();
          break;
        case ConfigDataEntitiesDictionary.PROPERTY_COLLECTIONS:
          aEntityList = oAppData.getPropertyCollectionList();
          break;
        case ConfigDataEntitiesDictionary.TABS:
          aEntityList = oAppData.getCustomTabList();
          break;
        case ConfigDataEntitiesDictionary.DASHBOARD_TABS:
          aEntityList = oAppData.getDashboardTabsList();
          break;
        case ConfigDataEntitiesDictionary.TEMPLATES:
          aEntityList = oAppData.getTemplates();
          break;
        case ConfigDataEntitiesDictionary.CONTEXTS:
          aEntityList = oAppData.getAllContextList();
          break;
        case ConfigDataEntitiesDictionary.NATURE_RELATIONSHIPS:
          aEntityList = oAppData.getNatureRelationshipList();
      }
      oEntityPagination.from = CS.size(aEntityList);
    });
    _fetchEntities(aEntityList, true, oCallback);
  };

  let _getContextTypesForCustomTemplateConfigDetails = function () {
    var oPaginationData = SettingScreenProps.screen.getEntitiesPaginationData();
    oPaginationData.contexts.types = [
      ContextTypeDictionary.CONTEXTUAL_VARIANT,
      ContextTypeDictionary.IMAGE_VARIANT,
      ContextTypeDictionary.GTIN_CONTEXT,
      ContextTypeDictionary.LANGUAGE_VARIANT
    ];
  };

  let _getDefaultEntitiesForPagination = function () {
    return [
      ConfigDataEntitiesDictionary.ATTRIBUTES,
      ConfigDataEntitiesDictionary.TAGS,
      ConfigDataEntitiesDictionary.ROLES,
      ConfigDataEntitiesDictionary.PROPERTY_COLLECTIONS,
      ConfigDataEntitiesDictionary.TAXONOMIES,
      ConfigDataEntitiesDictionary.TABS,
      ConfigDataEntitiesDictionary.DASHBOARD_TABS,
      ConfigDataEntitiesDictionary.TEMPLATES
    ];
  };

  var _fetchEntities = function (aEntityList, bLoadMore, oCallback) {
    var sSearchText = SettingScreenProps.screen.getEntitySearchText();
    var oPaginationData = SettingScreenProps.screen.getEntitiesPaginationData();
    if (CS.isEmpty(aEntityList)) {
      aEntityList = _getDefaultEntitiesForPagination();
    }
    let oNewEntityVsSearchTextMapping = {};
    CS.forEach(aEntityList, function (sEntity) {
      oNewEntityVsSearchTextMapping[sEntity] = sSearchText;
    });
    let oOldEntityVsSearchTextMapping = SettingScreenProps.screen.getEntityVsSearchTextMapping();
    CS.assign(oOldEntityVsSearchTextMapping, oNewEntityVsSearchTextMapping);
    var oEntitiesPaginationData = CS.pick(oPaginationData, aEntityList);
    var oRequestData = {
      searchColumn: "label",
      searchText: sSearchText,
      entities: oEntitiesPaginationData
    };

    CS.postRequest(oSettingsRequestMapping.GetConfigData, {}, oRequestData, successFetchEntitiesList.bind(this, bLoadMore, oCallback), failureFetchEntitiesList);
  };

  var _fetchEntitiesForPropertyCollection = function (aEntityList, bLoadMore, oCallback) {
    let sSearchText = SettingScreenProps.screen.getEntitySearchText();
    let oPaginationData = SettingScreenProps.screen.getEntitiesPaginationData();
    let oNewEntityVsSearchTextMapping = {};
    CS.forEach(aEntityList, function (sEntity) {
      oNewEntityVsSearchTextMapping[sEntity] = sSearchText;
    });
    let oOldEntityVsSearchTextMapping = SettingScreenProps.screen.getEntityVsSearchTextMapping();
    CS.assign(oOldEntityVsSearchTextMapping, oNewEntityVsSearchTextMapping);

    let oEntitiesPaginationData = CS.pick(oPaginationData, aEntityList);

    CS.forEach(aEntityList, function (sSelectedTabId) {
      if(sSelectedTabId === ConfigDataEntitiesDictionary.ATTRIBUTES){
        oEntitiesPaginationData.attributes.typesToExclude = [AttributeTypeDictionary.IMAGE_COVERFLOW];
        oEntitiesPaginationData.attributes.idsToExclude = ['assetdownloadcountattribute', 'scheduleendattribute', 'schedulestartattribute'];
      } else if(sSelectedTabId === ConfigDataEntitiesDictionary.TAXONOMIES){
        oEntitiesPaginationData.taxonomies.types = [TaxonomyTypeDictionary.MINOR_TAXONOMY];
        oEntitiesPaginationData.taxonomies.properties = {isRoot: true}
      }
    });

    let oRequestData = {
      searchColumn: "label",
      searchText: sSearchText,
      entities: oEntitiesPaginationData
    };

    CS.postRequest(oSettingsRequestMapping.GetConfigData, {}, oRequestData, successFetchEntitiesList.bind(this, bLoadMore, oCallback), failureFetchEntitiesList);
  };

  var successFetchEntitiesList = function (bLoadMore, oCallback, oResponse) {
    oResponse = oResponse.success;
    var oTotalCounts = SettingScreenProps.screen.getEntitiesTotalCounts();
    var oPropertyCollapsedStatusMap = SettingScreenProps.screen.getPropertyCollapsedStatusMap();
    CS.forEach(oResponse, function (oEntityData, sEntityName) {
      if (!bLoadMore) {
        oPropertyCollapsedStatusMap[sEntityName] = false;
      }
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
  };

  var failureFetchEntitiesList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchEntitiesList", oTranslations());
  };

  var _prepareDataBasedOnScreenFromPropertyCollectionList = function(){
    var oAppData = SettingUtils.getAppData();
    var sScreenName = _getComponentProps().screen.getSelectedLeftNavigationTreeParentId();
    var aPropertyCollectionList = oAppData.getPropertyCollectionList();

    switch(sScreenName){
      case "class":
        ClassStore.setClassSectionMap(aPropertyCollectionList);
        break
    }
  };

  var _setOrAppendEntityList = function (sEntityName, aEntityList, bAppend) {
    var oAppData = SettingUtils.getAppData();
    switch (sEntityName) {
      case ConfigDataEntitiesDictionary.ATTRIBUTES:
        oAppData.setAttributeList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.TAGS:
        oAppData.createTagMap(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.ROLES:
        oAppData.setRoleList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.RELATIONSHIPS:
        oAppData.setRelationshipList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.PROPERTY_COLLECTIONS:
        oAppData.appendOrSetPropertyCollectionList(aEntityList, bAppend);
        _prepareDataBasedOnScreenFromPropertyCollectionList();
        break;
      case ConfigDataEntitiesDictionary.TAXONOMIES:
        oAppData.setTaxonomyList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.TABS:
        oAppData.setCustomTabList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.DASHBOARD_TABS:
        oAppData.setDashboardTabsList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.CONTEXTS:
        oAppData.setAllContextList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.NATURE_RELATIONSHIPS:
        oAppData.setNatureRelationshipList(aEntityList, bAppend);
        break;
      case ConfigDataEntitiesDictionary.TEMPLATES:
        oAppData.setTemplates(aEntityList, bAppend);
        break;
    }
  };

  var _handleSwitchClassCategory = function (sClassCategory) {
    ClassSettingStore.changeClassCategory(sClassCategory);
    return _fetchRootNodeClassByType(sClassCategory);
  };

  var _handleGridPropertyValueChanged = function (sContextId, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData) {
    let bIsValidValue = true;
    let bAutoSave = false;
    let sMessage = "";
    let oCallback = {};

    switch (sPropertyId) {
      case GridViewColumnIdConstants.EMAIL:
        bIsValidValue = CS.isEmailValid(sValue);
        !bIsValidValue && (sMessage = oTranslations().EMAIL_VALIDATE);
        break;

      case GridViewColumnIdConstants.LINKED_MASTER_TAG_ID:
        oCallback.functionToExecute = TagStore.handleGridPropertyValueChangeDependencies.bind(this,
            GridViewColumnIdConstants.LINKED_MASTER_TAG_ID, sValue,
            sPathToRoot);
        bAutoSave = true;
        break;

      case GridViewColumnIdConstants.SYSTEM_ID:
        oCallback.functionToExecute = ProfileProps.setSelectedSystem.bind(this, sContentId, sValue);
        break;
    }

    switch (sContextId ) {
      case GridViewContexts.PROCESS:
        oCallback.functionToExecute = ProcessStore.handleGridPropertyValueChangeDependencies.bind(this);
        break;
    }

    if(bIsValidValue) {
      let oExtraData = {
        contentId: sContentId,
        pathToRoot: sPathToRoot,
        referencedData: oReferencedData,
        masterTagList: SettingUtils.getAppData().getMasterList(),
        callback: oCallback
      };
      GridViewStore.gridPropertyValueChanged(sContextId, sPropertyId, sValue, oExtraData);
      _triggerChange();
    }
    else {
      alertify.message(sMessage);
      return;
    }

    bAutoSave && _handleGridViewSaveButtonClicked({}, sContextId);
  };

  let _handleGridPropertyKeyDownEvent = function (sKey, sContentId, sPathToRoot, sGridContext) {
    switch (sGridContext) {
      case GridViewContexts.TAG:
        TagStore.handleGridPropertyKeyDownEvent(sKey, sContentId, sPathToRoot);
        break;
    }
  };

  let _handleGridParentExpandToggled = function (sContentId, sGridContext) {
    if (sGridContext == GridViewContexts.TRANSLATIONS) {
      TranslationsStore.handleGridParentExpandToggled(sContentId);
    } else {
      let oGridViewProps = GridViewStore.getGridViewPropsByContext(sGridContext);
      let oGridViewVisualData = oGridViewProps.getGridViewVisualData();
      oGridViewVisualData[sContentId].isExpanded = !oGridViewVisualData[sContentId].isExpanded;
      _triggerChange();
    }
  };

  var _handleGridPropertyClearShouldFocus = function (sContentId, sPathToRoot, sGridContext) {
    var oSettingScreenProps = _getComponentProps();
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(sGridContext);
    var aGridData = oGridViewProps && oGridViewProps.getGridViewData() || oSettingScreenProps.screen.getGridViewData();
    if (sPathToRoot && sPathToRoot != sContentId) {
      var aPath = CS.split(sPathToRoot, SettingUtils.getSplitter());
      aPath.splice(-1);
      CS.forEach(aPath, function (sId) {
        var oGridLevel = CS.find(aGridData, {id: sId});
        if (oGridLevel) {
          aGridData = oGridLevel.children;
        }
      });
    }
    var oFound = CS.find(aGridData, {id: sContentId});
    if (oFound) {
      oFound.metadata && (delete oFound.metadata.shouldFocus);
    }
  };

  var _handleGridImagePropertyImageChanged = function (sContentId, sPropertyId, aFiles, sPathToRoot, bLimitImageSize, sContext) {
    var oCallback = {};
    oCallback.functionToExecute = function (value) {
      //if not done this way, 'value' would be the last parameter of _handleGridPropertyValueChanged. But we want sPathToRoot (which is an optional parameter) to be the last one, hence done this way.
      _handleGridPropertyValueChanged(sContext, sContentId, sPropertyId, value, sPathToRoot);
    };
    _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
  };

  var _handleGridMSSAdditionalListItemAdded = function (sContentId, aCheckedItems, sContext, oReferencedData, sGridContext) {
    switch (sGridContext) {
      case GridViewContexts.TAG:
        TagStore.handleGridMSSAdditionalListItemAdded(sContentId, aCheckedItems, sContext, oReferencedData);
        break;
    }
  };

  let _handleGetAssetExtensions = async function (oRefDom, sContentId, sPropertyId, sContext, sPathToRoot) {
    if(sContext === 'user'){
      await ClassStore.getAllAssetExtensions();
      let oScreenProps = SettingUtils.getComponentProps().screen;
      let oAvailableExtensions = oScreenProps.getAssetExtensions();
      oRefDom.accept = oAvailableExtensions.allAssets;
      oRefDom.click();
    }
    else{
      IconLibraryStore.handleGridIconUploadButtonClicked(sContentId, sPropertyId, sContext, sPathToRoot);
      _triggerChange();
    }

  };

  var _handleGridViewSelectButtonClicked = function (aSelectedContentIds, bSelectAllClicked, sContext) {
    GridViewStore.handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked, sContext);
    _triggerChange();
  };

  var _handleGridViewActionItemClicked = function (sActionItemId, sContentId, sContext) {

    switch (sActionItemId) {

      case "delete":
      case "deleteValues":
        _handleGridViewDeleteButtonClicked([sContentId], sContext);
        break;

      case "create":
        _handleGridViewCreateButtonClicked(sContentId, sContext);
        break;

      case "moveUp":
        _handleGridViewMoveUpButtonClicked(sContentId, sContext);
        break;

      case "moveDown":
        _handleGridViewMoveDownButtonClicked(sContentId, sContext);
        break;

      case "edit":
        _handleGridViewEditButtonClicked(sContentId, sContext);
        break;

      case "manageEntity":
        _handleManageEntityDialogOpenButtonClicked(sContentId, sContext);
        break;

      case "download":
        _handleGridViewDownloadButtonClicked(sContentId, sContext);
        break;

      default:
        break;
    }

    _triggerChange();
  };

  let _handleManageEntityMultiUsageButtonClicked = function (sGridContext) {
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(sGridContext);
    let oSkeleton = oGridViewProps.getGridViewSkeleton();
    ManageEntityStore.handleManageEntityDialogOpenButtonClicked(oSkeleton.selectedContentIds, sGridContext);
  };

  var _handleGridViewColumnActionItemClicked = function (sActionItemId, sColumnId) {

    switch (sActionItemId) {


    }

    _triggerChange();
  };

  let _handleGridViewDeleteButtonClicked = function (aSelectedIds, sContext) {
    let GridViewProps = GridViewStore.getGridViewPropsByContext(sContext);
    let oSkeleton = GridViewProps.getGridViewSkeleton();
    aSelectedIds = CS.isNotEmpty(aSelectedIds) && aSelectedIds || oSkeleton.selectedContentIds;
    ManageEntityConfigProps.setIsDelete(true);
    deleteGrid(sContext, aSelectedIds, { functionToExecute: ManageEntityStore.handleManageEntityDialogOpenButtonClicked.bind(this, aSelectedIds, sContext,) });
  };

  var deleteGrid = function(sContext, aSelectedIds, oCallBack) {
    //let bCanDeleteEntity = ManageEntityConfigProps.getDataForDeleteEntity();
    let GridViewProps = GridViewStore.getGridViewPropsByContext(sContext);
    let oSkeleton = GridViewProps.getGridViewSkeleton();
    aSelectedIds = aSelectedIds || oSkeleton.selectedContentIds;
    let oCallbackData = {};
    oCallbackData.functionToExecute = deleteGrid.bind(this, sContext, aSelectedIds, oCallBack);
    if (!_gridViewSafetyCheck(oCallbackData, sContext)) {
      return;
    }
    //if (!bCanDeleteEntity || bPresentStandardAttribute) {
      switch (sContext) {

        case GridViewContexts.ATTRIBUTE:
          if (aSelectedIds) {
            AttributeStore.deleteAttribute(aSelectedIds, oCallBack);
          } else {
            AttributeStore.deleteAttribute(oSkeleton.selectedContentIds, oCallBack);
          }
          break;

        case GridViewContexts.TAG:
          TagStore.deleteMultipleTags(aSelectedIds, oCallBack);
          break;

        case GridViewContexts.RELATIONSHIP:
          RelationshipStore.deleteRelationship(aSelectedIds, oCallBack);
          break;

        case GridViewContexts.KPI:
          KpiStore.deleteKpis(aSelectedIds);
          break;

        case GridViewContexts.TASK:
          TasksStore.deleteTask(aSelectedIds, oCallBack);
          break;

        case GridViewContexts.TEMPLATE:
          TemplateStore.deleteTemplate(aSelectedIds);
          break;

        case GridViewContexts.END_POINT:
          ProfileStore.deleteProfiles(aSelectedIds);
          break;

        case GridViewContexts.USER:
          UserStore.deleteUsers(aSelectedIds, oCallBack);
          break;

        case GridViewContexts.DATA_GOVERNANCE_TASK:
          DataGovernanceTasksStore.deleteDataGovernanceTask(aSelectedIds);
          break;

        case GridViewContexts.TABS_CONFIG:
          aSelectedIds = aSelectedIds || oSkeleton.selectedContentIds;
          TabsStore.deleteTabs(aSelectedIds, oCallBack);
          break;

        case GridViewContexts.CONTEXT_VARIANT:
          ContextStore.deleteContext(aSelectedIds, oCallBack);
          break;

        case GridViewContexts.MAPPING:
          MappingStore.deleteMappings(aSelectedIds, oCallBack);
          break;

        case GridViewContexts.RULE:
          RuleStore.deleteRule(aSelectedIds);
          break;

        case GridViewContexts.GOLDEN_RECORD_RULE:
          SettingScreenStore.deleteGoldenRecordRule(aSelectedIds);
          break;

        case GridViewContexts.PROCESS:
          ProcessStore.deleteProcess(aSelectedIds);
          break;

        case GridViewContexts.SSO_SETTING:
          SSOSettingStore.deleteSSO(aSelectedIds);
          break;

        case GridViewContexts.AUTHORIZATION_MAPPING:
          AuthorizationStore.deleteAuthorizationMapping(aSelectedIds);
          break;

        case GridViewContexts.AUDIT_LOG_EXPORT_STATUS:
          AuditLogStore.deleteAuditLogExportStatusDialogClicked(aSelectedIds);
          break;

      //}
    }
  };

  var _handleGridViewMoveUpButtonClicked = function (sEntityId, sContext) {

    if (_gridViewSafetyCheck({}, sContext)) {
      switch (sContext) {

        case GridViewContexts.TAG:
          TagStore.moveTagUp(sEntityId);
          break;
      }
    }
  };

  var _handleGridViewMoveDownButtonClicked = function (sEntityId, sContext) {

    if (_gridViewSafetyCheck({}, sContext)) {
      switch (sContext) {

        case GridViewContexts.TAG:
          TagStore.moveTagDown(sEntityId);
          break;
      }
    }
  };

  let _handleManageEntityDialogOpenButtonClicked = function (aSelectedIds, sType) {
    ManageEntityConfigProps.setIsDelete(false);
    ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedIds, sType);
  };

  let _handleManageEntityDialogCloseButtonClicked = function () {
    ManageEntityConfigProps.setIsDelete(false);
    ManageEntityStore.handleManageEntityDialogCloseButtonClicked()
  };

  var _handleGridViewEditButtonClicked = function (sEntityId, sContext) {

    if (_gridViewSafetyCheck({}, sContext)) {
      switch (sContext) {

        case GridViewContexts.RELATIONSHIP:
          RelationshipStore.editButtonClicked(sEntityId);
          break;

        case GridViewContexts.KPI:
          KpiStore.editButtonClicked(sEntityId);
          break;

        case GridViewContexts.RULE:
          RuleStore.ruleEditButtonClicked(sEntityId);
          break;

        case GridViewContexts.GOLDEN_RECORD_RULE:
          GoldenRecordsStore.handleGoldenRecordRuleListNodeClicked(sEntityId);
          break;

        case GridViewContexts.USER:
          UserStore.setShowChangePasswordDialog(true, sEntityId);
          break;

        case GridViewContexts.TABS_CONFIG:
          TabsStore.fetchTabDetails(sEntityId);
          break;

        case GridViewContexts.CONTEXT_VARIANT:
          ContextStore.editButtonClicked(sEntityId);
          break;

        case GridViewContexts.MAPPING:
          MappingStore.editButtonClicked(sEntityId);
          break;

        case GridViewContexts.PROCESS:
          ProcessStore.editButtonClicked(sEntityId);
          break;

        case GridViewContexts.AUTHORIZATION_MAPPING:
          AuthorizationStore.editButtonClicked(sEntityId);
          break;

        case GridViewContexts.END_POINT:
          ProfileStore.editButtonClicked(sEntityId);
          break;
      }
    }
  };

  var _handleGridViewCreateButtonClicked = function  (sParentId, sContext) {

    var oCallbackData = {};
    oCallbackData.functionToExecute = _handleGridViewCreateButtonClicked.bind(this, sParentId, sContext);
    if (_gridViewSafetyCheck(oCallbackData, sContext)) {
      switch (sContext) {

        case GridViewContexts.ATTRIBUTE:
          AttributeStore.createAttribute();
          break;

        case GridViewContexts.RULE:
          SettingScreenStore.createRule();
          break;

        case GridViewContexts.GOLDEN_RECORD_RULE:
          SettingScreenStore.createGoldenRecordRule();
          break;

        case GridViewContexts.ENDPOINT:
          EndpointStore.createEndpoint();
          break;

        case GridViewContexts.TAG:
          TagStore.createTagNode(sParentId);
          break;

        case GridViewContexts.KPI:
          KpiStore.createKpi();
          break;

        case GridViewContexts.TASK:
          TasksStore.createTask();
          break;

        case GridViewContexts.TEMPLATE:
          TemplateStore.createTemplate();
          break;

        case GridViewContexts.END_POINT:
          ProfileStore.createProfile();
          break;

        case GridViewContexts.USER:
          UserStore.createUser();
          break;

        case GridViewContexts.DATA_GOVERNANCE_TASK:
          DataGovernanceTasksStore.createDataGovernanceTask();
          break;

        case GridViewContexts.TABS_CONFIG:
          TabsStore.createTab();
          break;

        case GridViewContexts.RELATIONSHIP:
          RelationshipStore.createNewRelationship(sContext);
          break;

        case GridViewContexts.CONTEXT_VARIANT:
          ContextStore.createContext();
          break;

        case GridViewContexts.MAPPING:
          MappingStore.createMappingDialogButtonClicked();
          break;

        case GridViewContexts.PROCESS:
          SettingScreenStore.createProcess();
          break;

        case GridViewContexts.SSO_SETTING:
          SSOSettingStore.createSSOSetting();

        case GridViewContexts.AUTHORIZATION_MAPPING:
          AuthorizationStore.createAuthorizationMappingDialogButtonClicked();
          break;
      }
    }
  };

  let _handleGridViewSortButtonClicked = function  (sParentId, sContext) {
    let oCallbackData = {};
    oCallbackData.functionToExecute = _handleGridViewSortButtonClicked.bind(this, sParentId, sContext);
    if (_gridViewSafetyCheck(oCallbackData, sContext)) {
      switch (sContext) {
        case GridViewContexts.TABS_CONFIG:
          TabsStore.handleSortButtonClicked();
          break;
      }
    }
  };

  let _handleGridViewSaveAsButtonClicked = function  (sParentId, sContext) {

    let oCallbackData = {};
    oCallbackData.functionToExecute = _handleGridViewSaveAsButtonClicked.bind(this, sParentId, sContext);
    if (_gridViewSafetyCheck(oCallbackData, sContext)) {
      switch (sContext) {

        case GridViewContexts.PROCESS:
          SettingScreenStore.saveAsProcess();
          break;

        case GridViewContexts.MAPPING:
          SettingScreenStore.saveAsMapping();
          break;

        case GridViewContexts.END_POINT:
          SettingScreenStore.saveAsEndpoint();
          break;
      }
    }
  };

  /** Function to prepare request object for selective export to excel **/
  let _selectiveExportConfigEntities = function (sGridContext) {
    let oSelectiveExportDetails = {};
    let oPostRequest = {};
    oPostRequest.exportType = "entity";
    oPostRequest.configCodes = [];
    oPostRequest.configType = "";
    oSelectiveExportDetails.oPostRequest = oPostRequest;
    oSelectiveExportDetails.sUrl = oSettingsRequestMapping.SelectiveExport;
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(sGridContext) || SettingScreenProps.screen;
    let oSkeleton = oGridViewProps.getGridViewSkeleton();
    let aSelectedContentIds = oSkeleton.selectedContentIds || [];
    switch (sGridContext) {
      case GridViewContexts.ATTRIBUTE:
          oPostRequest.configCodes = aSelectedContentIds;
          oPostRequest.configType = oDataModelImportExportEntityTypeConstants.ATTRIBUTE;
        break;

      case GridViewContexts.TAG:
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.TAG;
        break;

      case GridViewContexts.CONTEXT_VARIANT:
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.CONTEXT;
        break;

      case GridViewContexts.RULE:
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.RULE;
        break;

      case GridViewContexts.TRANSLATIONS:
        let sSplitter = SettingUtils.getSplitter();
        let sItemId = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
        let sSelectedTabId = CS.split(sItemId, sSplitter)[1];
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.TRANSLATION + "_" + sSelectedTabId;
        break;

      case GridViewContexts.GOLDEN_RECORD_RULE:
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.GOLDENRECORDRULE;
        break;

      case GridViewContexts.USER:
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.USER;
        break;

      case GridViewContexts.TABS_CONFIG:
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.TABS_CONFIG;
        break;

      case GridViewContexts.RELATIONSHIP:
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.RELATIONSHIP;
        break;


      case GridViewContexts.TASK:
        oPostRequest.configCodes = aSelectedContentIds;
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.TASK;
        break;

    }

    return oSelectiveExportDetails;
  };

  let _handleGridViewExportButtonClicked = function (sGridContext) {
    let oCallbackData = {};
    let oSelectiveExportDetails = _selectiveExportConfigEntities(sGridContext);
    if (_gridViewSafetyCheck(oCallbackData, sGridContext)) {
      switch (sGridContext) {
        case GridViewContexts.ATTRIBUTE:
          AttributeStore.handleExportAttribute(oSelectiveExportDetails);
          break;

        case GridViewContexts.TAG:
          TagStore.handleExportTag(oSelectiveExportDetails);
          break;

        case GridViewContexts.TRANSLATIONS:
          TranslationsStore.handleExportTranslation(oSelectiveExportDetails);
          break;

        case GridViewContexts.RULE:
          RuleStore.handleExportRule(oSelectiveExportDetails);
          break;

        case GridViewContexts.GOLDEN_RECORD_RULE:
          GoldenRecordsStore.handleGoldenRecordExportRule(oSelectiveExportDetails);
          break;

        case GridViewContexts.USER:
          UserStore.handleExportUser(oSelectiveExportDetails);
          break;

        case GridViewContexts.TABS_CONFIG:
          TabsStore.handleExportTabs(oSelectiveExportDetails);
          break;

        case GridViewContexts.RELATIONSHIP:
          RelationshipStore.handleExportRelationship(oSelectiveExportDetails);
          break;

        case GridViewContexts.CONTEXT_VARIANT:
          ContextStore.handleExportContext(oSelectiveExportDetails);
          break;

        case GridViewContexts.TASK:
          TasksStore.handleExportTask(oSelectiveExportDetails);
          break;

        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleExportAuditLog();
          break;

        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleDownloadLogsClicked();
          break;
      }
    }
  };

  let _handleGridViewDownloadButtonClicked = function (sContentId, sGridContext) {
    switch (sGridContext) {
      case GridViewContexts.DOWNLOAD_TRACKER:
        DownloadTrackerConfigStore.handleDownloadLogsClicked();
        break;
      case GridViewContexts.AUDIT_LOG:
        AuditLogStore.handleDownloadAuditLogExportStatusDialogClicked(sContentId);
        break;
    }
  };

  let _handleGridViewFilterButtonClicked = function (bShowFilterView, sGridContext) {
    let oSettingScreenProps = _getComponentProps();
    let oScreenProps = oSettingScreenProps.screen;
    oScreenProps.setIsGridFilterView(bShowFilterView);
    _triggerChange();
  };

  let _handleGridViewResizerButtonClicked = function (sColumnWidth, sColumnId, sContext) {
    let oSkeleton;
    let sUpdatedColumnWidth = (sColumnWidth > 70) ? sColumnWidth : "70";

    let oResizedColumn = {};
    switch (sContext) {
      case GridViewContexts.TRANSLATIONS:
      case GridViewContexts.DOWNLOAD_TRACKER:
        oSkeleton = SettingScreenProps.screen.getGridViewSkeleton();
        oResizedColumn = CS.find(oSkeleton.scrollableColumns, {id: sColumnId});
        oResizedColumn.width = +sUpdatedColumnWidth;
        SettingScreenProps.screen.setResizedColumnId(sColumnId);
        SettingScreenProps.screen.setGridViewSkeleton(oSkeleton);
        break;

      default:
        let oGridViewProps = GridViewStore.getGridViewPropsByContext(sContext);
        oSkeleton = oGridViewProps.getGridViewSkeleton();
        oResizedColumn = CS.find(oSkeleton.scrollableColumns, {id: sColumnId});
        oResizedColumn.width = +sUpdatedColumnWidth;
        oGridViewProps.setGridViewSkeleton(oSkeleton);
        oGridViewProps.setResizedColumnId(sColumnId);
    }
    _triggerChange();
  };

  let _handleGridFilterApplyClicked = function (oAppliedFilterData, oReferencedData) {
    let oSettingScreenProps = _getComponentProps();
    let oScreenProps = oSettingScreenProps.screen;
    let aGridAppliedFilterData = oScreenProps.getGridAppliedFilterData();
    if (CS.find(aGridAppliedFilterData, {filterField: oAppliedFilterData.filterField})) {
      if (CS.isEmpty(oAppliedFilterData.filterValues)) {
        aGridAppliedFilterData = aGridAppliedFilterData.filter((oFilter) => oFilter.filterField !== oAppliedFilterData.filterField);
      } else {
        let iIndex = CS.findIndex(aGridAppliedFilterData, {filterField: oAppliedFilterData.filterField});
        aGridAppliedFilterData[iIndex].filterValues = oAppliedFilterData.filterValues;
      }
    } else {
      CS.isNotEmpty(oAppliedFilterData.filterValues) && aGridAppliedFilterData.push(oAppliedFilterData);
    }
    oScreenProps.setGridAppliedFilterData(aGridAppliedFilterData);
  };

  let _handleGridOrganizeColumnButtonClicked = function (sGridContext) {
    let oColumnOrganizerProps = GridViewStore.getColumnOrganizerPropsByContext(sGridContext);
    if (oColumnOrganizerProps) {
      oColumnOrganizerProps.setIsDialogOpen(!oColumnOrganizerProps.getIsDialogOpen());
      return;
    }
    //Required in case contextual-grid-view is not used
    ColumnOrganizerProps.setIsDialogOpen(!ColumnOrganizerProps.getIsDialogOpen());
  };

  var _handleGridViewImportButtonClicked = function (aFiles, sContext) {
    var oCallbackData = {};
    let oImportExcel = {};
    oImportExcel.sUrl = oSettingsRequestMapping.ImportExcel;
    if (_gridViewSafetyCheck(oCallbackData, sContext)) {
      switch (sContext) {
        case GridViewContexts.ATTRIBUTE:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.ATTRIBUTE;
          AttributeStore.handleAttributeFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.TAG:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.TAG;
          TagStore.handleTagFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.TRANSLATIONS:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.TRANSLATION;
          TranslationsStore.handleTranslationFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.RULE:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.RULE;
          RuleStore.handleRuleFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.GOLDEN_RECORD_RULE:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.GOLDENRECORDRULE;
          GoldenRecordsStore.handleGoldenRecordFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.USER:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.USER;
          UserStore.handleUserFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.TABS_CONFIG:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.TABS_CONFIG;
          TabsStore.handleTabsFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.RELATIONSHIP:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.RELATIONSHIP;
          RelationshipStore.handleRelationshipFileUploaded(aFiles, GridViewContexts.RELATIONSHIP,oImportExcel);
          break;

        case GridViewContexts.CONTEXT_VARIANT:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.CONTEXT;
          ContextStore.handleContextFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.TASK:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.TASK;
          TasksStore.handleTaskFileUploaded(aFiles,oImportExcel);
          break;

        case GridViewContexts.PERMISSION:
          oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.PERMISSION;
          PermissionStore.handlePermissionFileUploaded(aFiles,oImportExcel);
          break;
      }
    }
  };

  var _handleGridViewRefreshButtonClicked = function (sContext) {
    let oCallbackFunction = {};
    oCallbackFunction.functionToExecute = _handleGridViewRefreshButtonClicked.bind(this, sContext);
    if(!_gridViewSafetyCheck(oCallbackFunction, sContext)) {
      return;
    }
    switch (sContext) {
      case GridViewContexts.ATTRIBUTE:
        AttributeStore.fetchAttributeListForGridView(sContext);
        break;

      case GridViewContexts.TAG:
        TagStore.fetchTagListForGridView(sContext);
        break;

      case GridViewContexts.TASK:
        TasksStore.fetchTaskListForGridView(sContext);
        break;

      case GridViewContexts.TEMPLATE:
        TemplateStore.fetchTemplateListForGridView(sContext);
        break;

      case GridViewContexts.END_POINT:
        ProfileStore.fetchEndpointListForGridView(sContext);
        break;

      case GridViewContexts.USER:
        UserStore.fetchUserListForGridView(sContext);
        break;

      case GridViewContexts.TABS_CONFIG:
        TabsStore.fetchTabListForGrid();
        break;

      case GridViewContexts.RULE:
        RuleStore.fetchRuleListForGridView();
        break;

      case GridViewContexts.CONTEXT_VARIANT:
        ContextStore.fetchContextListForGridView();
        break;

      case GridViewContexts.GOLDEN_RECORD_RULE:
        GoldenRecordsStore.fetchGoldenRecordRuleListForGridView();
        break;

      case GridViewContexts.MAPPING:// Can not handled by grid store
        MappingStore.fetchMappingList();
        break;

      case GridViewContexts.PROCESS:
        ProcessStore.fetchProcessListForGridView();
        break;

      case GridViewContexts.KPI:
        KpiStore.fetchKpiListForGridView();
        break;

      case GridViewContexts.RELATIONSHIP:
        let sSelectedConfigEntity = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
        RelationshipStore.fetchRelationshipListForGridView(sSelectedConfigEntity);
        break;

      case GridViewContexts.SSO_SETTING:
        SSOSettingStore.fetchSSOListForGridView();
        break;

      case GridViewContexts.AUTHORIZATION_MAPPING:
        AuthorizationStore.fetchAuthorizationMappingList();
        break;

      case GridViewContexts.AUDIT_LOG:
        AuditLogStore.fetchAuditLogListForGridView();
        break;

      case GridViewContexts.AUDIT_LOG_EXPORT_STATUS:
        AuditLogStore._fetchAuditLogExportStatusList();
        break;

      case GridViewContexts.TRANSLATIONS:
        SettingLayoutStore.handleSettingScreenLeftNavigationTreeItemClicked();
        break;

      case GridViewContexts.DOWNLOAD_TRACKER:
        DownloadTrackerConfigStore.getDownloadLogs();
        break;
    }
  };

  var _handleTagViewDataLanguageClicked = function (aSelectedItemId) {
    // let oLanguageInfo = TranslationManager.getLanguageInfo();
    let aDataLanguages = oLanguageInfo.dataLanguages;
    let oDataLanguage = CS.find(aDataLanguages, {id: aSelectedItemId});
    let sDataLanguageCode = oDataLanguage.code;

    SessionStorageManager.setPropertyInSessionStorage('selectedDataLanguageCode', sDataLanguageCode );
    SharableURLStore.addLanguageParamsInWindowURL("", sDataLanguageCode);
    TagStore.setUpTagConfigGridView();
  };

  var _handleGridPaginationChanged = function (oNewPaginationData, sContext) {
    var oCallbackData = {};
    oCallbackData.functionToExecute = _handleGridPaginationChanged.bind(this, oNewPaginationData);
    if (_gridViewSafetyCheck(oCallbackData, sContext)) {
      var oSettingScreenProps = _getComponentProps();
      oSettingScreenProps.screen.setGridViewPaginationData(oNewPaginationData);
      let GridViewProps = GridViewStore.getGridViewPropsByContext(sContext) || oSettingScreenProps.screen;
      GridViewProps.setGridViewPaginationData(oNewPaginationData);
      let oSkeleton = GridViewProps.getGridViewSkeleton();
      oSkeleton.selectedContentIds = [];

      switch (sContext) {

        case GridViewContexts.ATTRIBUTE:
          AttributeStore.fetchAttributeListForGridView();
          break;

        case GridViewContexts.TAG:
          TagStore.fetchTagListForGridView();
          break;

        case GridViewContexts.RULE:
          RuleStore.fetchRuleListForGridView();
          break;

        case GridViewContexts.GOLDEN_RECORD_RULE:
          GoldenRecordsStore.fetchGoldenRecordRuleListForGridView();
          break;

        case GridViewContexts.RELATIONSHIP:
          let sSelectedConfigEntity = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
          RelationshipStore.fetchRelationshipListForGridView(sSelectedConfigEntity);
          break;

        case GridViewContexts.KPI:
          KpiStore.fetchKpiListForGridView();
          break;

        case GridViewContexts.TRANSLATIONS:
          TranslationsStore.fetchTranslations();
          break;

        case GridViewContexts.TASK:
          TasksStore.fetchTaskListForGridView();
          break;

        case GridViewContexts.TEMPLATE:
          TemplateStore.fetchTemplateListForGridView();
          break;

        case GridViewContexts.END_POINT:
          ProfileStore.fetchEndpointListForGridView();
          break;

        case GridViewContexts.USER:
          UserStore.fetchUserListForGridView();
          break;

        case GridViewContexts.DATA_GOVERNANCE_TASK:
          DataGovernanceTasksStore.fetchDataGovernanceTasksForGridView();
          break;

        case GridViewContexts.TABS_CONFIG:
          TabsStore.fetchTabListForGrid();
          break;

        case GridViewContexts.CONTEXT_VARIANT:
          ContextStore.fetchContextListForGridView(sContext);
          break;

        case GridViewContexts.MAPPING:
          MappingStore.fetchMappingList();
          break;

        case GridViewContexts.PROCESS:
          ProcessStore.fetchProcessListForGridView();
          break;

        case GridViewContexts.SSO_SETTING:
          SSOSettingStore.fetchSSOListForGridView();

        case GridViewContexts.AUTHORIZATION_MAPPING:
          AuthorizationStore.fetchAuthorizationMappingList();
          break;

        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.fetchAuditLogListForGridView();
          break;

        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.getDownloadLogs();
          break;
      }
    }
    // _triggerChange();
  };

  let _handleGridViewSearchTextChanged = function (sSearchText, sContext) {
    let oCallbackData = {};
    oCallbackData.functionToExecute = _handleGridViewSearchTextChanged.bind(this, sSearchText, sContext);
    if (_gridViewSafetyCheck(oCallbackData, sContext)) {
      let oGridViewProps = GridViewStore.getGridViewPropsByContext(sContext) || _getComponentProps().screen;
      oGridViewProps.setGridViewSearchText(sSearchText);
      let oPaginationData = oGridViewProps.getGridViewPaginationData();
      oPaginationData.from = 0; //setting from back to 0 on searching anything
      let oGridViewSkeleton =  oGridViewProps.getGridViewSkeleton();
      if (oGridViewSkeleton.selectedContentIds) {
        oGridViewSkeleton.selectedContentIds = [];
      }

      switch (sContext) {

        case GridViewContexts.ATTRIBUTE:
          AttributeStore.fetchAttributeListForGridView(sContext);
          break;

        case GridViewContexts.TAG:
          TagStore.fetchTagListForGridView(sContext);
          break;

        case GridViewContexts.RULE:
          RuleStore.fetchRuleListForGridView();
          break;

        case GridViewContexts.GOLDEN_RECORD_RULE:
          GoldenRecordsStore.fetchGoldenRecordRuleListForGridView();
          break;

        case GridViewContexts.KPI:
          KpiStore.fetchKpiListForGridView();
          break;

        case GridViewContexts.RELATIONSHIP:
          let sSelectedConfigEntity = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
          RelationshipStore.fetchRelationshipListForGridView(sSelectedConfigEntity);
          break;

        case GridViewContexts.TRANSLATIONS:
          TranslationsStore.fetchTranslations();
          break;

        case GridViewContexts.TASK:
          TasksStore.fetchTaskListForGridView();
          break;

        case GridViewContexts.TEMPLATE:
          TemplateStore.fetchTemplateListForGridView();
          break;

        case GridViewContexts.END_POINT:
          ProfileStore.fetchEndpointListForGridView();
          break;

        case GridViewContexts.USER:
          UserStore.fetchUserListForGridView();
          break;

        case GridViewContexts.DATA_GOVERNANCE_TASK:
          DataGovernanceTasksStore.fetchDataGovernanceTasksForGridView();
          break;

        case GridViewContexts.TABS_CONFIG:
          TabsStore.fetchTabListForGrid();
          break;

        case GridViewContexts.CONTEXT_VARIANT:
          ContextStore.fetchContextListForGridView(sContext);
          break;

        case GridViewContexts.MAPPING:
          MappingStore.fetchMappingList();
          break;

        case GridViewContexts.PROCESS:
          ProcessStore.fetchProcessList();
          break;

        case GridViewContexts.SSO_SETTING:
          SSOSettingStore.fetchSSOListForGridView();
          break;

        case GridViewContexts.AUTHORIZATION_MAPPING:
          AuthorizationStore.fetchAuthorizationMappingList();
          break;
      }
    }
  };

  var _handleGridViewSaveButtonClicked = function (oCallbackData, sContext) {
    switch (sContext) {

      case GridViewContexts.ATTRIBUTE:
        AttributeStore.postProcessAttributeListAndSave(oCallbackData);
        break;

      case GridViewContexts.ENDPOINT:
        EndpointStore.saveBulkEndpoints();
        break;

      case GridViewContexts.TAG:
        TagStore.postProcessTagListAndSave(oCallbackData);
        break;

      case GridViewContexts.TRANSLATIONS:
        TranslationsStore.postProcessTranslationsAndSave(oCallbackData);
        break;

      case GridViewContexts.TASK:
        TasksStore.postProcessTaskAndSave(oCallbackData);
        break;

      case GridViewContexts.TEMPLATE:
        TemplateStore.postProcessTemplateAndSave(oCallbackData);
        break;

      case GridViewContexts.END_POINT:
        ProfileStore.postProcessEndpointListAndSave(oCallbackData);
        break;

      case GridViewContexts.USER:
        UserStore.postProcessUserListAndSave(oCallbackData);
        break;

      case GridViewContexts.DATA_GOVERNANCE_TASK:
        DataGovernanceTasksStore.postProcessDataGovernanceTaskListAndSave(oCallbackData);
        break;
      case GridViewContexts.TABS_CONFIG:
        TabsStore.processTabsListAndSave(oCallbackData);
        break;
      case GridViewContexts.RULE:
        RuleStore.postProcessRuleListAndSave(oCallbackData);
        break;

      case GridViewContexts.CONTEXT_VARIANT:
        ContextStore.processProcessContextListAndSave(oCallbackData);
        break;

      case GridViewContexts.GOLDEN_RECORD_RULE:
        GoldenRecordsStore.postProcessGoldenRuleListAndSave(oCallbackData);
        break;


      case GridViewContexts.MAPPING:// Can not handled by grid store
        MappingStore.processProcessMappingListAndSave(oCallbackData);
        break;

      case GridViewContexts.PROCESS:
        ProcessStore.processListBulkSave(oCallbackData);
        break;

      case GridViewContexts.KPI:
        KpiStore.postProcessKpiListAndSave(oCallbackData);
        break;

      case GridViewContexts.RELATIONSHIP:
        RelationshipStore.postProcessRelationshipListAndSave(oCallbackData);
        break;

      case GridViewContexts.SSO_SETTING:
        SSOSettingStore.postProcessSSOListAndSave();
        break;

      case GridViewContexts.AUTHORIZATION_MAPPING:
        AuthorizationStore.processProcessAuthorizationMappingListAndSave(oCallbackData);
        break;
    }
  };

  var _handleGridViewDiscardButtonClicked = function (oCallbackData, sContext) {
    switch (sContext) {

      case GridViewContexts.ATTRIBUTE:
        AttributeStore.discardAttributeGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.ENDPOINT:
        EndpointStore.discardEndpointGridViewChanges();
        break;

      case GridViewContexts.TAG:
        TagStore.discardTagGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.TRANSLATIONS:
        TranslationsStore.discardTranslationsGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.TASK:
        TasksStore.discardTasksGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.TEMPLATE:
        TemplateStore.discardTemplateGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.END_POINT:
        ProfileStore.discardEndpointGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.USER:
        UserStore.discardUserGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.DATA_GOVERNANCE_TASK:
        DataGovernanceTasksStore.discardDataGovernanceGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.TABS_CONFIG:
        TabsStore.discardTabListChanges(oCallbackData);
        break;

      case GridViewContexts.PROCESS:
        ProcessStore.discardProcessGridViewChanges(oCallbackData);

      case GridViewContexts.RULE:
        RuleStore.discardRuleListChanges();
        break;

      case GridViewContexts.CONTEXT_VARIANT: //Done
        ContextStore.discardContextListChanges(oCallbackData);
        break;

      case GridViewContexts.GOLDEN_RECORD_RULE:
        GoldenRecordsStore.discardGoldenRuleListChanges(oCallbackData);
        break;

      case GridViewContexts.MAPPING:
        MappingStore.discardMappingListChanges(oCallbackData);
        break;
      case GridViewContexts.KPI:
        KpiStore.discardKpiListChanges(oCallbackData);
        break;

      case GridViewContexts.RELATIONSHIP:
        RelationshipStore.discardRelationshipGridViewChanges(oCallbackData);
        break;

      case GridViewContexts.SSO_SETTING:
        SSOSettingStore.discardSSOGridViewChanges();

      case GridViewContexts.AUTHORIZATION_MAPPING:
        AuthorizationStore.discardAuthorizationMappingListChanges(oCallbackData);
        break;
    }
  };

  var _handleGridViewColumnHeaderClicked = function (sColumnId, sContext) {
    var oCallbackData = {};
    oCallbackData.functionToExecute = _handleGridViewColumnHeaderClicked.bind(this, sColumnId, sContext);
    if (_gridViewSafetyCheck(oCallbackData, sContext)) {
      let oScreenProps = _getComponentProps().screen;
      let oGridViewProps = GridViewStore.getGridViewPropsByContext(sContext);
      let sCurrentSortBy = "";
      let sCurrentSortOrder = "";
      //TODO: Need to refactor
      if(CS.isNotEmpty(oGridViewProps)) {
        let oSortData = oGridViewProps.getGridViewSortData();
        sCurrentSortBy = oSortData.sortBy;
        sCurrentSortOrder = oSortData.sortOrder;
      }  else {
        oGridViewProps = oScreenProps;
        sCurrentSortBy = oGridViewProps.getGridViewSortBy();
        sCurrentSortOrder = oGridViewProps.getGridViewSortOrder();
      }

      if (sContext == GridViewContexts.TRANSLATIONS) {
        TranslationsStore.handleColumnHeaderClicked(sColumnId, sContext);
      } else {
        if (sCurrentSortBy == sColumnId) {
          (sCurrentSortOrder == 'asc') ? sCurrentSortOrder = "desc" : sCurrentSortOrder = "asc";
        }
        else {
          sCurrentSortBy = sColumnId;
          if (sColumnId !== "sequence") {
            oGridViewProps.setGridViewSearchBy(sColumnId);
          }
          else {
            oGridViewProps.setGridViewSearchText("");
          }
        }
        if(GridViewStore.getGridViewPropsByContext(sContext)) {
          let oSortData = {
            sortOrder: sCurrentSortOrder,
            sortBy: sCurrentSortBy
          };
          oGridViewProps.setGridViewSortData(oSortData);
        } else {
          oGridViewProps.setGridViewSortBy(sCurrentSortBy);
          oGridViewProps.setGridViewSortOrder(sCurrentSortOrder);
        }
      }

      switch (sContext) {

        case GridViewContexts.ATTRIBUTE:
          AttributeStore.fetchAttributeListForGridView(sContext);
          break;

        case GridViewContexts.TAG:
          TagStore.fetchTagListForGridView(sContext);
          break;

        case GridViewContexts.KPI:
          KpiStore.fetchKpiListForGridView(sContext);
          break;

        case GridViewContexts.RULE:
          RuleStore.fetchRuleListForGridView();
          break;

        case GridViewContexts.GOLDEN_RECORD_RULE:
          GoldenRecordsStore.fetchGoldenRecordRuleListForGridView();
          break;

        case GridViewContexts.TASK:
          TasksStore.fetchTaskListForGridView();
          break;

        case GridViewContexts.TEMPLATE:
          TemplateStore.fetchTemplateListForGridView();
          break;

        case GridViewContexts.END_POINT:
          ProfileStore.fetchEndpointListForGridView();
          break;

        case GridViewContexts.USER:
          UserStore.fetchUserListForGridView();
          break;

        case GridViewContexts.DATA_GOVERNANCE_TASK:
          DataGovernanceTasksStore.fetchDataGovernanceTasksForGridView();
          break;

        case GridViewContexts.TABS_CONFIG:
          TabsStore.fetchTabListForGrid();
          break;

        case GridViewContexts.RELATIONSHIP:
          let sSelectedConfigEntity = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
          RelationshipStore.fetchRelationshipListForGridView(sSelectedConfigEntity);
          break;

        case GridViewContexts.CONTEXT_VARIANT:
          ContextStore.fetchContextListForGridView(sContext);
          break;

        case GridViewContexts.MAPPING:
          MappingStore.fetchMappingList();
          break;

        case GridViewContexts.PROCESS:
          ProcessStore.fetchProcessList();
          break;

        case GridViewContexts.AUTHORIZATION_MAPPING:
          AuthorizationStore.fetchAuthorizationMappingList();
          break;

        case GridViewContexts.SSO_SETTING:
          SSOSettingStore.fetchSSOListForGridView();
          break;

        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.fetchAuditLogListForGridView();
          break;

        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.getDownloadLogs();
          break;
      }
    }
  };

  let _handleGridViewShowExportStatusButtonClicked = function (sContext) {
    switch (sContext) {
      case GridViewContexts.AUDIT_LOG:
        AuditLogStore.handleShowAuditLogExportStatus();
        break;
    }
  };

  var _gridViewSafetyCheck = function (oCallbackData, sContext) {
    /** proceed with your operation if this function returns true */
    let GridViewProps = GridViewStore.getGridViewPropsByContext(sContext);
    let oSettingScreenProps = _getComponentProps();
    let bIsGridDataDirty = GridViewProps && GridViewProps.getIsGridDataDirty() || oSettingScreenProps.screen.getIsGridDataDirty();
    if (bIsGridDataDirty) {
      CustomActionDialogStore.showTriActionDialog(oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _handleGridViewSaveButtonClicked.bind(this, oCallbackData, sContext),
          _handleGridViewDiscardButtonClicked.bind(this, oCallbackData, sContext),
          function () {
          });
      return false;
    }
    return true;
  };

  var _handleYesNoViewToggled = function(bValue, oChildTagGroupModel){
    var sSplitter = SettingUtils.getSplitter();
    var aSplitContext = CS.split(oChildTagGroupModel.properties['context'], sSplitter);
    var sMainContext = aSplitContext[0];
    var sScreenName = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();

    switch (sMainContext) {
      case 'classConfigTagDefaultValue':
        if (sScreenName === ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
          AttributionTaxonomyStore.handleYesNoViewToggled(aSplitContext[1], aSplitContext[2], bValue);
        } else {
          ClassStore.handleYesNoViewToggled(aSplitContext[1], aSplitContext[2], bValue);
        }
        break;
      case 'contentFilterTagsInner':
        if (aSplitContext[1] == 'ruleFilterTagsTagValues') {
          let oExtraData = oChildTagGroupModel.properties['extraData'];
          let sScreenContext = oExtraData.screenContext;
          let aSplitScreenContext = sScreenContext && sScreenContext.split(sSplitter) || [];
          if (!CS.isEmpty(aSplitScreenContext) && aSplitScreenContext[0] == "kpiConfigDetail") {
            KpiStore.handleYesNoViewToggled(oChildTagGroupModel.properties["tagGroupId"], aSplitContext[2], bValue, aSplitScreenContext[1]);
          } else {
            RuleStore.handleYesNoViewToggled(oChildTagGroupModel.properties["tagGroupId"], aSplitContext[2], bValue);
          }
        } else if (aSplitContext[1] == 'ruleFilterTagsTagValuesForNormalization') {
          RuleStore.handleYesNoViewToggledForNormalization(oChildTagGroupModel.properties["tagGroupId"], bValue);
        }
        break;
    }
  };

  var _handleProcessMSSValueChanged = function (sSubContext, aSelectedItems, oReferencedData, aSplitContext) {
    switch (sSubContext){
      case "processType":
        ProcessStore.handleProcessSingleValueChanged(sSubContext, aSelectedItems[0]);
        break;
      case "eventType":
        ProcessStore.handleProcessSingleValueChanged(sSubContext, aSelectedItems[0]);
        break;
      case "klassTypes":
        ProcessStore.handleProcessLazyMSSValueChanged(sSubContext, aSelectedItems, oReferencedData);
      break;
      case "triggeringEventType":
        ProcessStore.handleProcessSingleValueChanged("triggeringType", aSelectedItems[0]);
      break;
      case "attributes":
        ProcessStore.handleProcessLazyMSSValueChanged(sSubContext, aSelectedItems, oReferencedData);
        break;
      case "tags":
        ProcessStore.handleProcessLazyMSSValueChanged(sSubContext, aSelectedItems, oReferencedData);
        break;
      case "workflowType":
        ProcessStore.handleProcessSingleValueChanged(sSubContext, aSelectedItems[0]);
        break;
      case "actionSubType":
        ProcessStore.handleProcessSingleValueChanged(sSubContext, aSelectedItems);
        break;
      case "nonNatureklassTypes":
        ProcessStore.handleProcessLazyMSSValueChanged(sSubContext, aSelectedItems, oReferencedData);
        break;
      case "frequency":
        sSubContext = aSplitContext[2];
        ProcessStore.handleProcessSingleValueChanged(sSubContext, aSelectedItems, aSplitContext);
        break;
      case "physicalCatalogIds":
        ProcessStore.handleProcessSingleValueChanged(sSubContext, aSelectedItems);
        break;
      case "organizations":
        ProcessStore.handleProcessLazyMSSValueChanged(sSubContext, aSelectedItems, oReferencedData);
        break;
      case "usecases":
        ProcessStore.handleProcessSingleValueChanged(sSubContext, aSelectedItems);
        break;
    }
  };

  var _handleNewMSSViewPopOverClosed = function (aSelectedItems, sContext, oReferencedData) {
    var sSplitter = SettingUtils.getSplitter();
    var aSplitContext = CS.split(sContext, sSplitter);
    var sMainContext = aSplitContext[0];
    let sSubContext = "";
    switch (sMainContext) {
      case "attribute":
        AttributeStore.handleAttributeMSSValueChanged(aSplitContext[1],aSelectedItems,oReferencedData);
        break;
      case "tab":
      case "tabId":
        _handleCustomTabChanged(aSelectedItems[0], false, "", aSplitContext, oReferencedData);
        break;
      case "relationshipType":
        RelationshipStore.handleRelationshipTypeChanged(aSelectedItems[0]);
        break;
      case "role":
        RoleStore.handleRoleMSSValueChanged(aSplitContext[1], aSelectedItems);
        break;
      case"attributionTaxonomyDetail":
        AttributionTaxonomyStore.handleMSSValueChanged(false, aSplitContext[1], aSelectedItems, oReferencedData);
        break;
      case "profile":
        sSubContext = aSplitContext[1];
        ProfileStore.handleProfileConfigFieldValueChanged(sSubContext, aSelectedItems);
        break;
      case "mapping":
        sSubContext = aSplitContext[1];
        MappingStore.handleMappingConfigFieldValueChanged(sSubContext, aSelectedItems);
        break;
      case "process":
        sSubContext = aSplitContext[1];
        _handleProcessMSSValueChanged(sSubContext, aSelectedItems, oReferencedData, aSplitContext);
        break;

      case 'embeddedKlass':
          ClassStore.handleAddEmbeddedKlassIds(aSelectedItems);
          break;

      case 'technicalImageKlass':
        ClassStore.handleAddTechnicalImageKlassIds(aSelectedItems);
        break;

      case 'onboardingEndpointsWorkspace':
        ProfileStore.handleProfileConfigFieldValueChanged("workSpace", aSelectedItems[0]);
        break;

      case "class":
        ClassStore.handleCommonConfigAttributeChanged(aSplitContext[1],aSelectedItems[0]);
        break;

      case "context":
        ContextStore.handleCommonConfigAttributeChanged(aSplitContext[1],aSelectedItems[0]);
        break;

      case 'contextConfig':
        var sSelectorId = aSplitContext[1];
        var sTagId = aSplitContext[2];
        ContextStore.handleContextCombinationMSSPopOver(sSelectorId,sTagId,aSelectedItems);
        break;

      case 'languageKlassContext':
      case 'embeddedKlassContext':
      case "technicalImageContext":
        var sSelectorId = aSplitContext[1];
        var sTagId = aSplitContext[2];
        ClassStore.handleClassContextCombinationMSSPopOver(sSelectorId,sTagId,aSelectedItems);
        break;

      case 'context_variant':
        var sSelectorId = aSplitContext[1];
        var sTagId = aSplitContext[2];
        ContextStore.handleContextConfigCombinationMSSPopOver(sSelectorId,sTagId,aSelectedItems);
        break;

      case 'classConfigTagDefaultValue':
        var bIsSingleSelect = false;
        var sScreenName = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
        if (sScreenName === ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
          AttributionTaxonomyStore.addDefaultTagValueForConfigTabularView(aSplitContext[1], aSplitContext[2], aSplitContext[3], aSelectedItems, sScreenName);
        }
        else {
          ClassStore.addDefaultTagValueForConfigTabularView(aSplitContext[1], aSplitContext[2], aSplitContext[3], aSelectedItems, bIsSingleSelect);
        }
        // SettingScreenStore.handleDropdownListNodeBlurred(sContext, aSelectedItems);
        break;

      case 'contextType':
        ContextStore.handleContextTypeChanged(aSelectedItems[0]);
        break;

      case 'dataGovernanceTasksConfigColor':
        DataGovernanceTasksStore.handleDataGovernanceTaskConfigFieldValueChanged("color", aSelectedItems[0]);
        break;

      case 'dataGovernanceTaskPriorityTag':
        DataGovernanceTasksStore.handleDataGovernanceTaskConfigFieldValueChanged("priorityTag", aSelectedItems[0]);
        break;

      case 'dataGovernanceTaskStatusTag':
        DataGovernanceTasksStore.handleDataGovernanceTaskConfigFieldValueChanged("statusTag", aSelectedItems[0]);
        break;

      case 'classConfigNatureType':
        ClassStore.handleNatureTypeChanged(aSelectedItems[0]);
        break;

      case 'contextView':
        ContextStore.handleCommonConfigAttributeChanged("defaultView", aSelectedItems[0]);
        break;

      case 'gtinKlass':
        ClassStore.handleGtinClassAddIds(aSelectedItems);
        break;

      case 'languageTree':
        LanguageTreeStore.handleLocaleIdChanged(aSplitContext[1], aSelectedItems);
        break;

      case ViewContextConstants.KPI:
        let aKeysForRuleBlockChanges = ['unit', 'task', 'frequency', 'color'];
        if (CS.includes(aKeysForRuleBlockChanges, aSplitContext[1])) {
          KpiStore.handleKPIDetailSingleValueChanged(aSplitContext[2], aSplitContext[1], aSelectedItems[0]);
        } else {
          KpiStore.handleKPIDetailMultipleValueChanged(aSplitContext[1], aSplitContext[2], aSelectedItems, oReferencedData);
        }
        break;

      case ViewContextConstants.ORGANISATION_CONFIG_CONTEXT:
        let aKeysForSingleValueChanged = ['type'];
        if (CS.includes(aKeysForSingleValueChanged, aSplitContext[1])) {
          OrganisationConfigStore.handleOrganisationSingleValueChanged(aSplitContext[1], aSelectedItems[0]);
        } else {
          OrganisationConfigStore.handleOrganisationMSSValueChanged(aSplitContext[1], aSelectedItems);
        }

        break;

      case "systems":
        ProfileStore.handleSystemsMSSApplyClicked(aSelectedItems);
        break;


      case "classConfigDataRules":
      case "classConfigTasks":
      case "classConfigLifeCycleStatusTags":
      case "classConfigEmbeddedKlass":
      case "classConfigMultiSelect" :
      case "classConfigNatureRelationship" :
      case "classConfigTaxonomyTransfer" :
      case "dtpClassConfigMarketKlass":
        ClassStore.handleMSSValueChanged(aSplitContext[1], aSelectedItems, oReferencedData);
        break;

      case "dtpClassConfigMarketTags":
        ClassStore.handleMarketTagsChanged(aSplitContext[1], aSelectedItems);
        break;

      case "groupedRelationshipViewItemSelection":
        ClassStore.handleGroupedRelationshipItemsChanged(aSelectedItems, oReferencedData);
        break;

      case "classConfigGtinKlass":
        ClassStore.handleGtinClassAddIds(aSelectedItems, oReferencedData);
        break;

      case "languageKlass":
      case "classConfigLanguageKlass":
        ClassStore.handleLanguageClassAddIds(aSelectedItems, oReferencedData);
        break;

      case "classConfigVariantContext":
        ClassStore.handleSelectionToggleButtonClicked(aSplitContext[1], aSelectedItems[0], true);
        break;

      case 'relationship':
        RelationshipStore.handleSelectionToggleButtonClicked(aSplitContext[1], aSelectedItems[0]);
        break;

      case "rules":
        RuleStore.handleRuleTypeSelected(aSelectedItems);
        break;

      case "task":
        TasksStore.handleTaskTypeSelected(aSelectedItems);
        break;

      case "organisation":
        OrganisationConfigStore.handleOrganisationTypeSelected(aSelectedItems);
        break;

      case "ruleDataLanguages":
        RuleStore.handleRuleLanguagesSelected(aSelectedItems);
        break;

      case "SSOSetting":
        SSOSettingStore.handleSSOSettingSelected(aSelectedItems);
        break;

      case "smartDocument":
        if (aSplitContext[1] === "klassIds") {
          SmartDocumentStore.handleSmartDocumentPresetMSSValueClicked(aSplitContext[1], aSelectedItems, oReferencedData);
        } else {
          SmartDocumentStore.handleCommonConfigValueChanged(aSplitContext[1], aSelectedItems[0]);
        }
        break;

      default:
        break;

    }

  };

  let _handleDataTransferAddProperties = (sEntity, aSelectedIds, oReferencedData, sContext) => {

    let sSplitter = SettingUtils.getSplitter();
    let aSplitContext = CS.split(sContext, sSplitter);

    let sSide = aSplitContext[1];
    let sRelationshipId = aSplitContext[2];
    let sParentContext = aSplitContext[3];

    switch (sParentContext) {

      case 'relationship':
        RelationshipStore.addPropertyInRelationshipSide(sEntity, sSide, aSelectedIds, oReferencedData);
        break;

      case 'klass':
        ClassStore.addPropertyInNatureRelationshipSide(sEntity, sRelationshipId, sSide, aSelectedIds, oReferencedData);
        break;

      case "frequency":
        sSubContext = aSplitContext[2]; // eslint-disable-line
        ProcessStore.handleProcessSingleValueChanged(sSubContext, aSelectedItems, aSplitContext); // eslint-disable-line
        break;

      default:
        break;
    }
  };

  var _handleDataTransferPropertyCouplingChange = function (sSide, sPropertyId, sNewValue, sContext, sRelationshipId, sParentContext) {

    switch (sParentContext) {

      case 'relationship':
        RelationshipStore.handleRelationshipPropertyCouplingChanged(sSide, sPropertyId, sNewValue, sContext);
        break;

      case 'klass':
        ClassStore.handleNatureRelationshipPropertyCouplingChanged(sRelationshipId, sSide, sPropertyId, sNewValue, sContext);
        break;

      default:
        break;

    }

  };

  var _handleDataTransferPropertyRemove = function (sSide, sPropertyId, sContext, sRelationshipId, sParentContext) {

    switch (sParentContext) {

      case 'relationship':
        RelationshipStore.removePropertyInRelationshipSide(sSide, sPropertyId, sContext);
        break;

      case 'klass':
        ClassStore.removePropertyInNatureRelationshipSide(sRelationshipId, sSide, sPropertyId, sContext);
        break;

      default:
        break;

    }

  };

  var _handleSearchAndFetchEntities = function (aContexts, sSearchText, oCallback) {
    SettingScreenProps.screen.resetEntitiesPaginationData();
    SettingScreenProps.screen.setEntitySearchText(sSearchText);
    if(aContexts[0] === ConfigDataEntitiesDictionary.CONTEXTS) {
      _getContextTypesForCustomTemplateConfigDetails();
    }
    _fetchEntities(aContexts, false, oCallback);
  };

  let _handleSearchAndFetchEntitiesForPropertyCollection = function (aContexts, sSearchText, oCallback) {
    SettingScreenProps.screen.resetEntitiesPaginationData();
    SettingScreenProps.screen.setEntitySearchText(sSearchText);
    _fetchEntitiesForPropertyCollection(aContexts, false, oCallback);
  };

  let _handleGetAllowedAttributesForCalculatedAttribute = function (oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, bIsForReload) {
    CalculatedAttributeStore.getAllowedAttributesForCalculatedAttribute(oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, bIsForReload);
  };

  let _prepareDataForAttributesAndTagsDialog = function () {
    let aEntityList = ["attributes","tags"];
    _fetchEntities(aEntityList, false, {});
  };

  let _handleAdvancedSearchListSearched  =function (sSearchText, sContext) {
    SettingScreenProps.screen.resetEntitiesPaginationData();
    if (sContext === "bulkEdit") {
      // To get only editable attributes for bulk edit
      let oPaginationData = SettingScreenProps.screen.getEntitiesPaginationData();
      if (oPaginationData && oPaginationData.attributes) {
        oPaginationData.attributes.isDisabled = false;
      }
    }
    SettingScreenProps.screen.setEntitySearchText(sSearchText);
    return _prepareDataForAttributesAndTagsDialog();
  };

  var _handleEntityConfigDialogButtonClicked = function (sButtonId, sEntityType) {
    switch (sEntityType) {
      case 'tag':
        TagStore.handleTagConfigDialogButtonClicked(sButtonId);
        break;
      case 'attribute':
        AttributeStore.handleAttributeConfigDialogButtonClicked(sButtonId);
        break;
      case 'tasks':
        TasksStore.handleTaskConfigDialogButtonClicked(sButtonId);
        break;
      case "propertycollection":
        PropertyCollectionStore.handlePropertyCollectionConfigDialogButtonClicked(sButtonId);
        break;
      case "template":
        TemplateStore.handleTemplateConfigDialogButtonClicked(sButtonId);
        break;
      case 'role':
        RoleStore.handleRoleConfigDialogButtonClicked(sButtonId);
        break;
      case 'profile':
        ProfileStore.handleProfileConfigDialogButtonClicked(sButtonId);
        break;
      case 'rule':
        RuleStore.handleRuleConfigDialogButtonClicked(sButtonId);
        break;
      case 'ruleList':
        RuleListStore.handleRuleListDialogButtonClicked(sButtonId);
        break;
      case 'process':
        ProcessStore.handleProcessDialogButtonClicked(sButtonId);
        break;
      case 'mapping':
        MappingStore.handleMappingDialogButtonClicked(sButtonId);
        break;
      case 'authorization_mapping':
        AuthorizationStore.handleAuthorizationMappingDialogButtonClicked(sButtonId);
        break;
      case 'attributionTaxonomyLevel':
      case 'attributionTaxonomyLevelChildren':
        AttributionTaxonomyStore.handleAttributionTaxonomyConfigDialogButtonClicked(sButtonId, sEntityType);
        break;
      case 'organization':
        OrganisationConfigStore.handleOrganisationConfigDialogButtonClick(sButtonId);
        break;
      case 'kpiConfig':
        KpiStore.handleKpiConfigDialogButtonClick(sButtonId);
        break;
      case 'dataGovernance':
        DataGovernanceTasksStore.handleDataGovernanceTaskConfigDialogButtonClicked(sButtonId);
        break;
      case 'endpoint':
        ProfileStore.handleProfileConfigDialogButtonClicked(sButtonId);
        break;
      case ViewContextConstants.TABS_CONFIG:
        TabsStore.handleCreateDialogButtonClicked(sButtonId);
        break;
      case 'contextConfig':
        ContextStore.handleContextConfigDialogButtonClicked(sButtonId);
        break;
      case ViewContextConstants.GOLDEN_RECORD_RULE:
        GoldenRecordsStore.handleCreateDialogButtonClicked(sButtonId);
        break;
      case "ssoSetting":
        SSOSettingStore.handleCreateSSODialogButtonClicked(sButtonId);
        break;
    }
  };

  let _handleSettingScreenLeftNavigationTreeItemClicked = function (sItemId, sParentId) {
    let sSplitter = SettingUtils.getSplitter();

    if(CS.split(sItemId, sSplitter)[0] === ConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS){
      let oCallBack = {
        functionToExecute: SettingLayoutStore.handleSettingScreenLeftNavigationTreeItemClicked.bind(this, sItemId, sParentId, true)
      };
      oTranslationManagerInit(oCallBack);
    } else {
      return SettingLayoutStore.handleSettingScreenLeftNavigationTreeItemClicked(sItemId, sParentId, true);
    }
  };

  let _handleConfigScreenTabClicked = function (sTabId, bDoNotTrigger) {
    SettingLayoutStore.handleConfigScreenTabClicked(sTabId, bDoNotTrigger);
  };

  let _handleUserCreateClicked = function (oModel) {
    UserStore.handleUserCreateClicked(oModel);
  };

  let _handleListViewSearchOrLoadMoreClicked = function (sContext, sSearchText, bLoadMore) {
    switch (sContext){
      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION:
        let sEntity = ConfigDataEntitiesDictionary.ATTRIBUTES;
        let fFunctionToExecute = function () {
          SettingScreenProps.propertyCollectionConfigView.setPropertyCollectionDraggableListActiveTabId(sEntity);
          _resetAndFetchEntitiesForPropertyCollection([sEntity], bLoadMore, "");
          PropertyCollectionStore.fetchPropertyCollectionsList(bLoadMore);
        };
        PropertyCollectionStore.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, fFunctionToExecute);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP:
        RelationshipStore.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST:
        RuleListStore.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_RULE:
        RuleStore.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS:
        ProcessStore.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_ORGANISATION:
        OrganisationConfigStore.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_ICON_LIBRARY:
        IconLibraryStore.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_ROLE:
        OrganisationConfigStore.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, sContext);
        break;
    }
  };

  let _getCustomTab = function (sId) {
    let oAppData = SettingUtils.getAppData();
    let aCustomTabList = oAppData.getCustomTabList();
    let oCustomTab = CS.find(aCustomTabList, {id: sId});
    return oCustomTab;
  };

  let _handleConfigTabChangedByScreen = function (sKey, oVal, aSplitContext, oReferencedTabs) {
    let sActiveScreen = SettingUtils.getConfigScreenViewName();
    switch (sActiveScreen) {
      case ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP:
        RelationshipStore.handleRelationshipSingleValueChanged(sKey, oVal, oReferencedTabs);
        break;
      case ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS:
        let oContextDialogProps = _getComponentProps().classConfigView.getClassContextDialogProps();
        if (oContextDialogProps.isDialogOpen) {
          ClassStore.handleClassContextDialogPropertyChanged(sKey, oVal);
        } else {
          if(aSplitContext[1]==="class_relationship")
            ClassStore.handleClassRelationshipModified(aSplitContext[2], sKey, oVal, oReferencedTabs);
        }
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
        ContextStore.handleContextSingleValueChanged(sKey, oVal, oReferencedTabs);
        break;

      case ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION:
        PropertyCollectionStore.handlePropertyCollectionSingleValueChanged(sKey, oVal);
        break;
    }
    if (!(sActiveScreen == ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS && aSplitContext[1] && aSplitContext[1] == "class_relationship")) {
      oVal.isNewlyCreated && _saveConfigDataNew({});
    }
  };

  let _handleConfigCustomTabChanged = function(oCustomTabObject, aSplitContext, oReferencedTabs){
    let sActiveScreenName = SettingUtils.getConfigScreenViewName();
    switch (sActiveScreenName ){
      case ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
        _handleConfigTabChangedByScreen("tabId", oCustomTabObject.id, aSplitContext, oReferencedTabs);
        break;

      /** Can remove below case after testing **/
      default:
        _handleConfigTabChangedByScreen("tab", oCustomTabObject, aSplitContext, oReferencedTabs);
        break;
    }
  };

  let _handleColorPickerColorChanged = function (sColor, sContext="") {
    let aThemeConfigurationStoreContexts = [ThemeConfigurationConstants.HEADER_BACKGROUND_COLOR,
                                            ThemeConfigurationConstants.HEADER_FONT_COLOR,
                                            ThemeConfigurationConstants.HEADER_ICON_COLOR,
                                            ThemeConfigurationConstants.DIALOG_BACKGROUND_COLOR,
                                            ThemeConfigurationConstants.DIALOG_FONT_COLOR,
                                            ThemeConfigurationConstants.LOGIN_SCREEN_BACKGROUND_COLOR,
                                            ThemeConfigurationConstants.LOGIN_SCREEN_FONT_COLOR,
                                            ThemeConfigurationConstants.LOGIN_SCREEN_BUTTON_BACKGROUND_COLOR,
                                            ThemeConfigurationConstants.LOGIN_SCREEN_BUTTON_FONT_COLOR,
                                            ThemeConfigurationConstants.GENERAL_SELECTION_COLOR,
                                            ThemeConfigurationConstants.GENERAL_BUTTON_BACKGROUND_COLOR,
                                            ThemeConfigurationConstants.GENERAL_THEME_COLOR,
                                            ThemeConfigurationConstants.GENERAL_BUTTON_FONT_COLOR,
                                            ThemeConfigurationConstants.GENERAL_FONT_COLOR];

    if(CS.includes(aThemeConfigurationStoreContexts, sContext)){
      ThemeConfigurationScreenStore.handleThemeConfigurationSectionValueChanged(sContext, sColor);
    } else if (sContext == "class") {
      ClassStore.handleCommonConfigAttributeChanged("color", sColor);
    }
  };

  let _handleCustomTabChanged = function (sTabId, bIsCreateNewClicked, sLabel, aSplitContext, oReferencedTabs) {
    let oCustomTabObject = {};
    if (bIsCreateNewClicked) {
      SettingScreenProps.screen.setEntitySearchText("");
      let sContentId = RelationshipStore.getSelectedRelationship().id;
      _handleRelationshipTabCreated("configView", sLabel, sTabId, sContentId, aSplitContext);
      return true;
    }
    else {
      oCustomTabObject = _getCustomTab(sTabId);
      if(!oCustomTabObject) {
        oCustomTabObject = oReferencedTabs[sTabId];
      }
    }
    _handleConfigCustomTabChanged(oCustomTabObject, aSplitContext, oReferencedTabs)
  };

  let _handleSystemSelectApplyClicked = function (aSelectedSystemIds, sContext) {
    if (sContext === ViewContextConstants.ORGANISATION_CONFIG_CONTEXT) {
      OrganisationConfigStore.handleSystemSelectApplyClicked(aSelectedSystemIds);
    } else {
      RoleStore.handleSystemSelectApplyClicked(aSelectedSystemIds);
    }
  };

  let _handleEndpointSelectionViewSelectAllChecked = function (sSystemId, iCheckboxStatus, sContext) {
    if (sContext === ViewContextConstants.ORGANISATION_CONFIG_CONTEXT) {
      OrganisationConfigStore.handleEndpointSelectionViewSelectAllChecked(sSystemId, iCheckboxStatus);
    } else {
      RoleStore.handleEndpointSelectionViewSelectAllChecked(sSystemId, iCheckboxStatus);
    }
  };

  let _handleEndpointSelectionViewDeleteClicked = function (sSystemId, sContext) {
    if (sContext === ViewContextConstants.ORGANISATION_CONFIG_CONTEXT) {
      OrganisationConfigStore.handleEndpointSelectionViewDeleteClicked(sSystemId);
    } else {
      RoleStore.handleEndpointSelectionViewDeleteClicked(sSystemId);
    }
  };

  let _handleDashboardTabCreated = function (sConfigView, sLabel, sPropertyId, sContentId, sGridContext) {
    let sId = UniqueIdentifierGenerator.generateUUID();
    let oDashboardTabObject =  {
      id: sId,
      label: sLabel
    };

    var oCallback = {};
    oCallback.data = {
      property: sPropertyId,
      contentId: sContentId,
      context: sGridContext,
    };

    CS.putRequest(oSettingsRequestMapping.CreateDashboardTabs, {}, oDashboardTabObject, successCreateDashboardTab.bind(this, sConfigView, oCallback), failureCreateDashboardTab);

  };

  // TODO: change name of _handleRelationshipTabCreated() as it is handling all types of tabs creation, not only for relationship
  let _handleRelationshipTabCreated = function (sConfigView, sLabel, sPropertyId, sContentId, aSplitContext, sGridContext) {
    let sId = UniqueIdentifierGenerator.generateUUID();
    let oRelationshipTabObject = {
      id: sId,
      label: sLabel
    };

    var oCallback = {};
    oCallback.data = {
      property: sPropertyId,
      contentId: sContentId
    };
    CS.putRequest(oTabsRequestMapping.Create, {}, oRelationshipTabObject, successCreateRelationshipTab.bind(this, sConfigView, sContentId, oCallback, aSplitContext, sGridContext), failureCreateRelationshipTab);
  };

  let successCreateRelationshipTab = function (sConfigView, sContentId, oCallback, aSplitContext, sGridContext, oResponse) {
    let oCreatedTab = oResponse.success.tab;
    let oContextDialogProps = _getComponentProps().classConfigView.getClassContextDialogProps();
    let oContextConfig = _getComponentProps().contextConfigView;
    let oRelationshipConfig = _getComponentProps().relationshipView;
    let oAppData = SettingUtils.getAppData();
    let aCustomTabList = oAppData.getCustomTabList();
    aCustomTabList.push(oCreatedTab);

    switch (sConfigView) {
      case "gridView":
        let oGridViewProps = GridViewStore.getGridViewPropsByContext(sGridContext);
        let aGridViewData = oGridViewProps && oGridViewProps.getGridViewData() || SettingScreenProps.screen.getGridViewData();
        let oGridRow = CS.find(aGridViewData, {id: sContentId});
        let oProperties = oGridRow.properties;
        let oTabId = oProperties.tabId;
        oTabId.selectedObjects = oTabId.items = [oCreatedTab];
        oTabId.value = oTabId.selectedItems = [oCreatedTab.id];
        oTabId.referencedData[oCreatedTab.id] = oCreatedTab;
        // SettingScreenProps.screen.setGridViewData(aGridViewData);

        _handleGridPropertyValueChanged(sGridContext, oCallback.data.contentId, oCallback.data.property, [oCreatedTab.id]);
        break;

      case "configView":
        let oReferencedTabs = {
          [oCreatedTab.id]: oCreatedTab
        };
        let oContextConfigDetailsDialogActive = oContextConfig.getIsContextDialogActive();
        if (oContextDialogProps.isDialogOpen) {
          ClassStore.handleClassContextDialogPropertyChanged("tab", oCreatedTab, oReferencedTabs);
        }else if(oContextConfigDetailsDialogActive){
          ContextStore.handleContextSingleValueChanged("tabId", oCreatedTab.id, oReferencedTabs);
          // ContextStore.handleContextSingleValueChanged("tab", oCreatedTab, oReferencedTabs);
        }else if(oRelationshipConfig.getIsRelationshipDialogActive()){
          RelationshipStore.handleRelationshipSingleValueChanged("tab", oCreatedTab, oReferencedTabs);
        }else{
          _handleConfigCustomTabChanged(oCreatedTab, aSplitContext, oReferencedTabs);
        }
        break;
    };

    if(oContextDialogProps.isDialogOpen || oContextConfig.getIsContextDialogActive() || oRelationshipConfig.getIsRelationshipDialogActive()){
      if (oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute();
      }
      _triggerChange();
    }
  };

  let failureCreateRelationshipTab = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCreateDashboardTab", oTranslations());
  };

  let successCreateDashboardTab = function (sConfigView, oCallback, oResponse) {
    let sContext = oCallback.context;

    switch (sConfigView) {
      case "kpiConfig":
        var oReferencedDashboardTabs = KPIProps.getReferencedDashboardTabs();
        oReferencedDashboardTabs[oResponse.success.id] = oResponse.success;
        KpiStore.handleKPIDetailSinglePropertyValueChanged("", oCallback.data.property, oResponse.success.id);
        break;

      case "gridView":
        if (sContext === GridViewContexts.END_POINT) {
          var oReferencedDashboardTabs = ProfileProps.getReferencedDashboardTabs();
          oReferencedDashboardTabs[oResponse.success.id] = oResponse.success;
        } else if(sContext === GridViewContexts.KPI) {
          var oReferencedDashboardTabs = KPIProps.getReferencedDashboardTabs();
          oReferencedDashboardTabs[oResponse.success.id] = oResponse.success;
        }
        _handleGridPropertyValueChanged(sContext, oCallback.data.contentId, oCallback.data.property, [oResponse.success.id]);
        break;

      case "commonConfig":
        if (sContext === GridViewContexts.END_POINT) {
          ProfileStore.createDashboardTab(oResponse.success);
        }
        break;
    };

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  let failureCreateDashboardTab = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCreateDashboardTab", oTranslations());
  };

  let _setUniversalSearchDataFromMock = () => {
    let aSearchList = [];
    _createDataForUniversalSearch(ConfigModulesList(),[],null,aSearchList);
    SettingScreenProps.screen.setUniversalSearchList(aSearchList)
  };

  let _createDataForUniversalSearch = function(aItems,sPath,sParentId, aSearchList) {
    CS.forEach(aItems, function (item) {
      let oTempObject = {};
      oTempObject.id = item.id;
      oTempObject.label = item.label;
      oTempObject.path = CS.cloneDeep(sPath);
      let oPathObject = {};
      oPathObject.id = item.id;
      oPathObject.label = item.label;
      oTempObject.path.push(oPathObject);
      oTempObject.parentId =(oTempObject.path[0].id == sParentId)? null : sParentId;

      if(CS.isEmpty(item.children)){
        aSearchList.push(oTempObject);
      }
      if(item.children){
        _createDataForUniversalSearch(item.children,oTempObject.path,item.id,aSearchList);
      }
    })
  };

  let _handleConfigModuleSearchDialogClosedClicked = function () {
    _getComponentProps().customSearchDialogView.setDialogOpen(false);
    _triggerChange();
  };

  let _handleEsc = function () {
    let oComponentProps = _getComponentProps();
    let oCustomSearchDialogViewProps = oComponentProps.customSearchDialogView;
    let oUserModel = oComponentProps.UserConfigView.getSelectedUser();
    let bIsCreateUserDialogOpen = oUserModel.isCreated;
    let bIsCustomDialogOpen = ActionDialogProps.getIsCustomDialogOpen();
    let bIsUniversalSearchPopperOpen = oCustomSearchDialogViewProps.getDialogOpen();

    if (bIsCustomDialogOpen) {
      CustomActionDialogStore.resetCustomDialog();
    } else if (bIsUniversalSearchPopperOpen) {
      oCustomSearchDialogViewProps.setDialogOpen(false);
    } else if (bIsCreateUserDialogOpen) {
      UserStore.cancelCreateUser(oUserModel);
    }
    _triggerChange();
  };

  let _handleProcessConfigFullScreenButtonClicked = function () {
    let oComponentProps = _getComponentProps();
    let oProcessConfigViewProps = oComponentProps.processConfigView;
    let bFullScreenMode = oProcessConfigViewProps.getIsFullScreenMode();
    oProcessConfigViewProps.setIsFullScreenMode(!bFullScreenMode);
    _triggerChange();
  };

  let _handleRelationshipInheritanceApplyClicked = function(sEntity, aSelectedIds, oReferencedData, sContext) {
    let sSplitter = SettingUtils.getSplitter();
    let aSplitContext = CS.split(sContext, sSplitter);
    let sSide = aSplitContext[1];
    let sRelationshipId = aSplitContext[2];
    ClassStore.addPropertyInNatureRelationshipSide(sEntity, sRelationshipId, sSide, aSelectedIds, oReferencedData);
  }

  let _handleAuditLogExportStatusDialogButtonClicked = function (sButtonId) {
    switch (sButtonId) {
      case 'ok':
        AuditLogStore.handleAuditLogExportStatusOkButtonClicked();
        break;
    }
  };

  let _handleAuditLogExportDialogRefreshButtonClicked = function () {
    AuditLogStore.handleAuditLogExportDialogRefreshButtonClicked();
  };

  let _handleAuditLogExportDialogPaginationChanged = function (oNewPaginationData) {
    AuditLogStore.handleAuditLogExportDialogPaginationChanged(oNewPaginationData);
  };

  let _checkIsAnyGridDataDirty = () => {
    return SettingScreenProps.contextualGridViewProps.checkIsAnyGridDataDirty();
  };

  let _handleDialogSaveButtonClicked = function () {
    IconLibraryStore.handleDialogSaveButtonClicked();
  };

  let _handleDialogCancelButtonClicked = function () {
    IconLibraryStore.handleDialogCancelButtonClicked();
  };

  let _handleDialogInputChanged = function (oFileData) {
    IconLibraryStore.handleDialogInputChanged(oFileData);
  };

  let  _handleIconElementSaveIconClicked = function(oFile, sCode, sName, isNameEmpty){
    IconLibraryStore.handleIconElementSaveIconClicked(oFile, sCode, sName, isNameEmpty);
  };

  let _handleIconElementReplaceIconClicked = function (oFile, fileName) {
    IconLibraryStore.handleIconElementReplaceIconClicked(oFile, fileName);
  };

  let _handleIconElementIconNameChanged = function (sFileName) {
    IconLibraryStore.handleIconElementIconNameChanged(sFileName);
  };

  let _handlePropertyCollectionDraggableListTabClicked = function (sTabId) {
    let oActivePropertyCollection = oComponentProps.propertyCollectionConfigView.getActivePropertyCollection();
    SettingScreenProps.propertyCollectionConfigView.setAvailablePropertyList("");
    SettingScreenProps.propertyCollectionConfigView.setPropertyCollectionDraggableListActiveTabId(sTabId);
    let oCallback = {};
    oCallback.functionToExecute =  function () {
      PropertyCollectionStore.createClassSectionElementMap();
      PropertyCollectionStore.fillClassSectionElementMap(oActivePropertyCollection);
    };
    SettingScreenStore.resetAndFetchEntitiesForPropertyCollection([sTabId], false, oCallback);
  };

  let _handleCommonConfigUploadIconClicked = async function (oRefDom, sConfigContext) {
    let bFetchIconFromIconLibrary = true;
    switch (sConfigContext) {
      case 'class':
        let oClassConfigViewProps = oComponentProps.classConfigView;
        let oActiveClass = oClassConfigViewProps.getActiveClass();
        _updateShowSelectIconDialogStatus(oActiveClass, true);
        break;
      case 'propertycollection':
        let oPropertyCollectionConfigView = oComponentProps.propertyCollectionConfigView;
        let oActivePropertyCollection = oPropertyCollectionConfigView.getActivePropertyCollection();
        _updateShowSelectIconDialogStatus(oActivePropertyCollection, true);
        break;
      case 'attributionTaxonomyDetail':
        let oAttributionTaxonomyConfigView = oComponentProps.attributionTaxonomyConfigView;
        let oActiveTaxonomy = oAttributionTaxonomyConfigView.getActiveDetailedTaxonomy();
        _updateShowSelectIconDialogStatus(oActiveTaxonomy, true);
        break;
      case 'context':
        let oAppData = SettingUtils.getAppData();
        let oContextMasterList = oAppData.getContextList();
        let oContextConfigProps = oComponentProps.contextConfigView;
        let sActiveContextId = oContextConfigProps.getActiveContext().id;
        let oActiveContext = oContextMasterList[sActiveContextId] || {};
        _updateShowSelectIconDialogStatus(oActiveContext, true);
        break;
      case 'organisation':
        let oOrganisationConfigView = oComponentProps.organisationConfigView;
        let oActiveOrganisation = oOrganisationConfigView.getActiveOrganisation();
        _updateShowSelectIconDialogStatus(oActiveOrganisation, true);
        break;
      case 'relationship':
        let oRelationshipView = oComponentProps.relationshipView;
        let oActiveRelationship = oRelationshipView.getSelectedRelationship();
        _updateShowSelectIconDialogStatus(oActiveRelationship, true);
        break;
      case 'technicalImageContext':
      case 'embeddedKlassContext':
      case "languageKlassContext":
        let oClassConfigView = oComponentProps.classConfigView;
        let oClassContextDialogProps = oClassConfigView.getClassContextDialogProps();
        _updateShowSelectIconDialogStatus(oClassContextDialogProps, true);
        break;
      case 'languageTree':
        let oLanguageTreeProps = oComponentProps.languageTreeConfigView;
        let oActiveLanguage = oLanguageTreeProps.getActiveLanguage();
        _updateShowSelectIconDialogStatus(oActiveLanguage, true);
        break;
      default :
        bFetchIconFromIconLibrary = false;
    }
    if(bFetchIconFromIconLibrary){
      IconLibraryProps.setSearchText("");
      IconLibraryStore.fetchIconLibraryScreen(sConfigContext);
      _triggerChange();
    }
    else {
      await ClassStore.getAllAssetExtensions();
      let oScreenProps = SettingUtils.getComponentProps().screen;
      let oAvailableExtensions = oScreenProps.getAssetExtensions();
      oRefDom.accept = oAvailableExtensions.allAssets;
      oRefDom.click();
    }
  };

  let _updateShowSelectIconDialogStatus = function (oActiveClass, bIsUploadClicked = false) {
    if (oActiveClass.clonedObject || oActiveClass.languageClone) {
      let oClonedObject = oActiveClass.clonedObject || oActiveClass.languageClone;
      oClonedObject.showSelectIconDialog = bIsUploadClicked;
      oActiveClass.showSelectIconDialog = false;
    } else {
      oActiveClass.showSelectIconDialog = bIsUploadClicked;
    }
  };

  let successFetchDAMConfiguration = function (oResponse) {
    oResponse = oResponse.success;
    let bIsOriginalFileNameUsedForDownload = oResponse.shouldDownloadAssetWithOriginalFilename;
    let oDamConfigurationData = {
      isOriginalFileNameUsedForDownload: bIsOriginalFileNameUsedForDownload,
      isDirty: false
    };
    DAMConfigurationViewProps.setDamConfigurationData(oDamConfigurationData);
  };

  let successSaveDAMConfiguration = function (oResponse) {
    oResponse = oResponse.success;
    let bIsOriginalFileNameUsedForDownload = oResponse.shouldDownloadAssetWithOriginalFilename;
    let oDamConfigurationData = {
      isOriginalFileNameUsedForDownload: bIsOriginalFileNameUsedForDownload,
      isDirty: false
    };
    DAMConfigurationViewProps.setDamConfigurationData(oDamConfigurationData);
    alertify.success(oTranslations().DAM_CONFIGURATION_SAVED_SUCCESSFULLY);
  };

  let failureFetchDAMConfiguration = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchDAMConfiguration", getTranslation());
  };

  let failureSaveDAMConfiguration = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchDAMConfiguration", getTranslation());
  };

  let _fetchDAMConfiguration = function () {
    return SettingUtils.csGetRequest(oIndesignServerRequestMapping.getDAMConfiguration, {},
        successFetchDAMConfiguration, failureFetchDAMConfiguration);
  };

  let _handleDamConfigUseFileNameValueChanged = function () {
    let oClonedObject = {};
    let oDAMConfigurationData = DAMConfigurationViewProps.getDamConfigurationData();
    if (oDAMConfigurationData.hasOwnProperty('clonedObject')) {
      oClonedObject = oDAMConfigurationData.clonedObject;
      oClonedObject.isOriginalFileNameUsedForDownload = !oClonedObject.isOriginalFileNameUsedForDownload;
    } else {
      oClonedObject = CS.clone(oDAMConfigurationData);
      oClonedObject.isOriginalFileNameUsedForDownload = !oClonedObject.isOriginalFileNameUsedForDownload;
      oClonedObject.isDirty = !oClonedObject.isDirty;
      oDAMConfigurationData.clonedObject = oClonedObject;
    }
  };

  let _handleDamConfigurationSaveButtonClicked = function () {
    let oDAMConfigurationData = DAMConfigurationViewProps.getDamConfigurationData();
    let oActiveConfigurationData = oDAMConfigurationData.clonedObject;
    let bIsOriginalFileNameUsedForDownload = oActiveConfigurationData.isOriginalFileNameUsedForDownload;
    let oRequestData = {
      shouldDownloadAssetWithOriginalFilename : bIsOriginalFileNameUsedForDownload
    };

    return CS.postRequest(oIndesignServerRequestMapping.saveDAMConfiguration, {}, oRequestData,
        successSaveDAMConfiguration, failureSaveDAMConfiguration);
  };

  let _handleDamConfigurationDiscardButtonClicked = function () {
    let oDAMConfigurationData = DAMConfigurationViewProps.getDamConfigurationData();
    delete oDAMConfigurationData.clonedObject;
    alertify.success(oTranslations().DISCARDED_SUCCESSFULLY);
  };

  let _handleListNodeViewNodeDeleteButtonClicked = function (sId, sContext) {
    switch (sContext) {
      case "role":
        RoleStore.deleteRole(sId);
        _triggerChange();
        break;
      case "organization":
        OrganisationConfigStore.deleteOrganisation(sId);
        break;
    }
  };

  let _createRole = function () {
    let bRoleScreenLockStatus = RoleStore.getRoleScreenLockStatus();

    if (bRoleScreenLockStatus) {
      let oCallbackData = {};
      oCallbackData.functionToExecute = RoleStore.createRole;
      RoleStore.saveRole(oCallbackData);
    } else {
      RoleStore.createRole();
    }
  };

  let _fetchVariantConfigurationServerDetails = () => {
    return SettingUtils.csGetRequest(oVariantConfigurationRequestMapping.GetVariantConfigurations, {}, successCallbackForFetchVariantConfigurationScreen, failureCallbackForFetchVariantConfigurationScreen);
  };

  let successCallbackForFetchVariantConfigurationScreen = function (oResponse) {
    let oVariantConfigurationModel = oResponse.success;
    let oVariantViewData = VariantConfigurationViewProps.getVariantViewData();
    oVariantViewData.isVariantConfigurationEnable = oVariantConfigurationModel.isSelectVariant;
    VariantConfigurationViewProps.setVariantViewData(oVariantViewData);
  };

  let failureCallbackForFetchVariantConfigurationScreen = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureThemeConfigurationCallback", oTranslations());
  };

  let _handleVariantActionButtonClicked = (sId, oCallbackData) => {
    let oVariantViewData = VariantConfigurationViewProps.getVariantViewData();
    let oVariantViewCloneData = makeVariantViewDataDirty();
    let bIsVariantConfigurationEnable = oVariantViewCloneData.isVariantConfigurationEnable;
    switch (sId) {
      case "save":
        let oPostData = {
          "id": "standardVariantConfiguration",
          "isSelectVariant": bIsVariantConfigurationEnable
        };
        return SettingUtils.csPostRequest(oVariantConfigurationRequestMapping.SaveVariantConfigurations, {}, oPostData,
            successSaveVariantConfiguration.bind(this, oCallbackData), failureCallbackForFetchVariantConfigurationScreen);

      case "discard":
        VariantConfigurationViewProps.setVariantViewData(oVariantViewData);
        delete oVariantViewData.clonedObject;
        alertify.message(oTranslations().DISCARDED_SUCCESSFULLY);
        if (CS.isNotEmpty(oCallbackData)) {
          oCallbackData.functionToExecute();
        } else {
          return CommonUtils.resolveEmptyPromise();
        }
    }
  };

  let successSaveVariantConfiguration = (oCallbackData, oResponse) => {
    let oVariantConfigurationModel = oResponse.success;
    let oVariantViewData = VariantConfigurationViewProps.getVariantViewData();
    delete oVariantViewData.clonedObject;
    oVariantViewData.isVariantConfigurationEnable = oVariantConfigurationModel.isSelectVariant;
    oVariantViewData.isDirty = false;
    VariantConfigurationViewProps.setVariantViewData(oVariantViewData);
    alertify.message(oTranslations().SUCCESSFULLY_SAVED);
    if (CS.isNotEmpty(oCallbackData) && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  let _handleVariantConfigurationToggleButtonClicked = () => {
    let oVariantViewData = makeVariantViewDataDirty();
    oVariantViewData.isVariantConfigurationEnable = !oVariantViewData.isVariantConfigurationEnable;
  };

  let makeVariantViewDataDirty = () => {
    let oVariantViewData = VariantConfigurationViewProps.getVariantViewData();
    if (!oVariantViewData.clonedObject) {
      oVariantViewData.clonedObject = CS.cloneDeep(oVariantViewData);
      oVariantViewData.clonedObject.isDirty = true
    }
    return oVariantViewData.clonedObject;
  };

  let _handleListNodeViewNodeCloneButtonClicked = (sId,sContext) => {
    RoleStore.openCloneRoleDialog(sId);
  };

  let _handleRemoveUserImageClicked = () => {
    let oUserCloned = _getComponentProps().UserConfigView.getSelectedUser();
    oUserCloned.icon = "";
  };

  let _fetchAdministrationSummaryData = () => {
    let aAdministrationSummaryData = MockDataForAdministrationsSummaryHeader();
    AdministrationsSummaryProps.setAdministrationItemData(aAdministrationSummaryData);
    _triggerChange();
  };

  let _handleAdministrationSummaryExpandButtonClicked = (sButtonId) => {
    let aItemData = AdministrationsSummaryProps.getAdministrationItemData();
    let oExpandedHeader = CS.find(aItemData, {id: sButtonId});
    oExpandedHeader.isExpanded = !oExpandedHeader.isExpanded;

    if(oExpandedHeader.isExpanded) {
      SettingUtils.csPostRequest(oAdministrationSummaryRequestMapping.getObjectCount, {}, {"entityType": sButtonId},
          successAdministrationSummaryView, failureAdministrationSummaryView);
    } else if(!oExpandedHeader.isExpanded) {
      let oCollapsedItemData = CS.find(aItemData, {id: sButtonId});
      CS.forEach(oCollapsedItemData.children, function (oData) {
        oData.count = 0;
        oData.children && CS.forEach(oData.children, function (oChildData) {
          oChildData.count = 0
        });
      });
      _triggerChange();
    }
  };

  let successAdministrationSummaryView = (oResponse) => {
    let oSuccessData = oResponse.success.dataModel;
    let aItemData = AdministrationsSummaryProps.getAdministrationItemData();
    CS.forEach(aItemData, function (oHeaderData) {
      CS.forEach(oSuccessData, function (oData) {
        if (oData.entityType === oHeaderData.id) {
          CS.isNotEmpty(oHeaderData.children) && CS.forEach(oHeaderData.children, function (oChild) {
            CS.forEach(oData.entitySubType, function (oSubType, key) {
              if (oChild.id === key) {
                CS.forEach(oSubType, function (oSubTypeData) {
                  CS.isNotEmpty(oChild.children) && CS.forEach(oChild.children, function (oNestedChild) {
                    CS.forEach(oSubTypeData, function (iCount, childKey) {
                      if (childKey === oNestedChild.id) {
                        oNestedChild.count = iCount
                      }
                    });
                  });
                });
              }
            });
            CS.forEach(oData.entityCount, function (iCountForType, sTypeKey) {
              if (sTypeKey === oChild.id) {
                oChild.count = iCountForType
              }
            });
          });
        }
      });
    });
    AdministrationsSummaryProps.setAdministrationItemData(aItemData);
    _triggerChange();
  };

  let failureAdministrationSummaryView = (oResponse) => {
    SettingUtils.failureCallback(oResponse, "failureAdministrationSummaryCallback", oTranslations());
  };

  /********************************Public API**************************************/
  return {

    getData: function () {
      return {
        appData: _getAppData(),
        componentProps: _getComponentProps()
      }
    },

    handleListItemCreated: function () {
      var oComponentProps = _getComponentProps();
      oComponentProps.screen.setListItemCreationStatus(false);
    },

    loadDataOnScreenLoad: function () {
      //Handle URL update.
      SharableURLStore.removeWindowURL();

      function fFunctionToExecute () {
        let oSelectedMenu = SettingUtils.getSelectedMenu();
        let sScreenName = (oSelectedMenu.id === "setting") ? ConfigModulesDictionary.DATA_MODEL : "tags";
        if(sScreenName === ConfigModulesDictionary.DATA_MODEL) {
          SettingLayoutStore.handleConfigScreenTabClicked(sScreenName);
        } else {
          var oActionBarList = oAppData.getActionBarList();
          var oSettingScreenActionList = oActionBarList.SettingScreens;
          var aVisibleEntities = SettingUtils.getVisibleSettingModules();
          CS.forEach(oSettingScreenActionList, function (oItem) {
            if (CS.includes(aVisibleEntities, oItem.id)) {
              var oInvertedModule = CS.invert(oSettingModules);
              sScreenName = oInvertedModule[oItem.id];
              return false;
            }
          });
        }
        _setUniversalSearchDataFromMock();
        HomeScreenCommunicator.triggerHomeScreen();
      }

      let fFunction = fFunctionToExecute.bind(this);
      let oCallback = {
        functionToExecute: fFunction,
        triggerChange: _triggerChange
      };

      TranslationStore.fetchTranslations(['home', 'view', 'setting'], SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE), oCallback);
    },

    settingScreenSafetyCheck: function (oCallbackData) {
      //proceed if this returns true
      var oSettingScreenProps = _getComponentProps();
      var bPropertyCollectionScreenLock = PropertyCollectionStore.getPropertyCollectionScreenLockedStatus();
      var bClassScreenlock = ClassStore.getClassScreenLockedStatus();
      var bRelationshipScreenLock = RelationshipStore.getRelationshipScreenLockStatus();
      var bRuleScreenLock = RuleStore.getRuleScreenLockStatus();
      var bRuleListScreenLock = RuleListStore.getRuleListScreenLockStatus();
      var bAttributionTaxonomyScreenLock = AttributionTaxonomyStore.getAttributionTaxonomyScreenLockStatus();
      var bTranslationsScreenLock = TranslationsStore.getTranslationsScreenLockStatus();
      var bDataGovernanceTaskScreenLock = DataGovernanceTasksStore.getDataGovernanceTasksScreenLockStatus();
      var bIsGridViewDirty = _checkIsAnyGridDataDirty() || oSettingScreenProps.screen.getIsGridDataDirty();
      var bIsProcessLock = ProcessStore.getProcessScreenLockStatus();
      let bIsOrganisationConfigLocked = OrganisationConfigStore.getOrganisationConfigScreenLockStatus();
      let bIsLanguageDirty = LanguageTreeStore.getIsLanguageDataDirty();
      let bIsSmartDocumentConfigDirty = SmartDocumentStore.getIsSmartDocumentConfigDirty();
      let bIsGridEditScreenLocked = GridEditStore.getGridEditScreenLockStatus();
      let bIsPropertyGridEditViewDirty = oSettingScreenProps.gridEditProps.getPropertySequenceList().isDirty;
      let bIsThemeConfigurationDirty = oSettingScreenProps.themeConfigurationProps.getThemeConfigurationData().isDirty;
      let bIsPdfReactorServerConfigDirty = oSettingScreenProps.pdfReactorServerConfigProps.getPdfReactorServerData().isDirty;
      let variantConfigurationData = oSettingScreenProps.variantConfigurationProps.getVariantViewData();
      let bIsVariantConfigurationDirty = variantConfigurationData.clonedObject && variantConfigurationData.clonedObject.isDirty;

      if (bPropertyCollectionScreenLock || bClassScreenlock || bRelationshipScreenLock
          || bRuleScreenLock || bRuleListScreenLock || bAttributionTaxonomyScreenLock || bTranslationsScreenLock
          || bIsGridViewDirty || bIsProcessLock || bDataGovernanceTaskScreenLock || bIsOrganisationConfigLocked || bIsLanguageDirty
          || bIsSmartDocumentConfigDirty || bIsGridEditScreenLocked || bIsPropertyGridEditViewDirty || bIsThemeConfigurationDirty ||
        bIsPdfReactorServerConfigDirty || bIsVariantConfigurationDirty) {
        CustomActionDialogStore.showTriActionDialog(oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            _saveConfigDataNew.bind(this, oCallbackData),
            _discardConfigDataNew.bind(this, oCallbackData), function () {
            });
        return false;
      }
      return true;
    },

    handleDiscardConfigData: function () {
      let oCallbackData = {};
      _discardConfigDataNew(oCallbackData);
    },

    handleSaveButtonInActionBarClickedNew: function () {
      let sActiveScreenName = SettingUtils.getConfigScreenViewName();

      switch (sActiveScreenName) {
        case ConfigEntityTypeDictionary.ENTITY_TYPE_TAG:
          TagStore.saveChangedTagData({});
          break;

        case ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS:
          ClassStore.saveClass({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION:
          PropertyCollectionStore.savePropertyCollection({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_TEMPLATE:
          TemplateStore.postProcessTemplateAndSave();
          break;
        case 'permissions':
          PermissionStore.savePermission({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
          ContextStore.saveContext({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_DATAGOVERNANCE:
          DataGovernanceTasksStore.saveDataGovernanceTask({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP:
          RelationshipStore.saveRelationship({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_ROLE:
          RoleStore.saveRole({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_MAPPING:
          MappingStore.saveMapping({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_RULE:
          RuleStore.saveRule({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST:
          RuleListStore.saveRuleList({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS:
          ProcessStore.saveProcess({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_ORGANISATION:
          OrganisationConfigStore.saveLockedOrganisationConfigScreen();
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_GOLDEN_RECORDS:
          GoldenRecordsStore.saveRule({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_THEME_CONFIGURATION:
          ThemeConfigurationScreenStore.handleSaveAction();
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_VIEW_CONFIGURATION:
          ViewConfigurationScreenStore.handleSaveAction();
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_PDFREACTORSERVER:
          PdfReactorServerStore.handleSaveAction();
          break;

      }
    },

    handleCancelButtonInActionBarClickedNew: function () {
      switch (_getComponentProps().screen.getSelectedLeftNavigationTreeItem()) {
        case ConfigEntityTypeDictionary.ENTITY_TYPE_TAG:
          TagStore.discardUnsavedTag({});
          break;
        case 'class_class':
        case 'asset_class':
        case 'target_class':
        case 'textasset_class':
        case 'supplier_class':
        case ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS:
          ClassStore.discardUnsavedClass({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP:
          RelationshipStore.discardUnsavedRelationship({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_ROLE:
          RoleStore.discardUnsavedRole({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_RULE:
          RuleStore.discardUnsavedRule({});
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST:
          RuleListStore.discardUnsavedRuleList({});
          break;
      }
    },

    handleProcessListNodeClicked: function (oModel) {
      ProcessStore.handleProcessListNodeClicked(oModel);
    },

    handlePropertyCollectionListNodeClicked: function (oModel) {
      PropertyCollectionStore.fetchPropertyCollectionDetails(oModel);
      SettingScreenProps.propertyCollectionConfigView.setPropertyCollectionDraggableListActiveTabId(ConfigDataEntitiesDictionary.ATTRIBUTES);
      this.handleAvailableListViewSearched("");
    },

    handleDataGovernanceTasksListNodeClicked: function (oModel) {
      DataGovernanceTasksStore.handleDataGovernanceTasksListNodeClicked(oModel);
    },

    handleRoleListNodeClicked: function (oModel) {
      let sRoleId = oModel.id;
      RoleStore.handleRoleListNodeClicked(sRoleId);
      OrganisationConfigViewProps.setIsPermissionVisible(false);
    },

    handlePermissionScreenRoleListNodeClicked: function (oModel) {
      let sRoleId = oModel.id;
      PermissionStore.handlePermissionRoleListNodeClicked(sRoleId);
    },

    handleRuleListNodeClicked: function (oModel) {
      RuleStore.handleRuleListNodeClicked(oModel);
    },

    handleGoldenRecordRuleListNodeClicked: function (oModel) {
      GoldenRecordsStore.handleGoldenRecordRuleListNodeClicked(oModel);
    },

    handleListOfRuleListNodeClicked: function (oModel) {
      RuleListStore.handleListOfRuleListNodeClicked(oModel);
    },

    handleRelationshipListNodeClicked: function (oModel) {
      RelationshipStore.handleRelationshipListNodeClicked(oModel);
    },

    createProcess: function () {
      var bProcessScreenLockStatus = ProcessStore.getProcessScreenLockStatus();

      if (bProcessScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = ProcessStore.createProcess;

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            ProcessStore.saveProcess.bind(this, oCallbackData),
            ProcessStore.discardUnsavedProcess.bind(this, oCallbackData)).set({transition: 'fade'});

      } else {
        ProcessStore.createProcess();
      }
    },

    saveAsProcess: function(){
      ProcessStore.saveAsProcess();
    },

    saveAsMapping: function(){
      MappingStore.saveAsMapping();
    },

    saveAsEndpoint: function(){
      ProfileStore.saveAsEndpoint();
    },

    createDataGovernanceTask: function () {
      DataGovernanceTasksStore.createDataGovernanceTask();
    },

    deleteDataGovernanceTask: function () {
      DataGovernanceTasksStore.deleteDataGovernanceTask()
    },

    deleteProcess: function () {
      ProcessStore.deleteProcess();
    },

    deleteRole: function () {
      RoleStore.deleteRole();
      _triggerChange();
    },

    createRule: function () {
      var bRuleScreenLockStatus = RuleStore.getRuleScreenLockStatus();

      if (bRuleScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = RuleStore.createDefaultRuleObject;

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            RuleStore.saveRule.bind(this, oCallbackData),
            RuleStore.discardUnsavedRule.bind(this, oCallbackData),
            function () {

            });
      } else {
        RuleStore.createDefaultRuleObject();
      }
    },

    deleteRule: function () {
      RuleStore.deleteRule();
    },

    createRuleList: function () {
      var bRuleListScreenLockStatus = RuleListStore.getRuleListScreenLockStatus();

      if (bRuleListScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = RuleListStore.createDefaultRuleListObject;

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            RuleListStore.saveRuleList.bind(this, oCallbackData),
            RuleListStore.discardUnsavedRuleList.bind(this, oCallbackData),
            function () {

            });
      } else {
        RuleListStore.createDefaultRuleListObject();
      }
    },

    deleteRuleList: function () {
      RuleListStore.deleteRuleList();
    },

    createNewRelationship: function () {
      RelationshipStore.createNewRelationship();
    },

    deleteRelationship: function () {
      RelationshipStore.deleteRelationship();
    },

    handleCommonConfigSectionAddOperator: function(sOperatorType){
      AttributeStore.handleCommonConfigSectionAddOperator(sOperatorType);
      _triggerChange();
    },

    handleCommonConfigSectionAddConcatObject: function (sType) {
      AttributeStore.handleCommonConfigSectionAddConcatObject(sType);
      _triggerChange();
    },

    handleCommonConfigSectionConcatInputChanged: function (sValue, oConcat) {
      AttributeStore.handleCommonConfigSectionConcatInputChanged(sValue, oConcat);
      _triggerChange();
    },

    handleCommonConfigSectionConcatObjectRemoved: function (sId) {
      AttributeStore.handleCommonConfigSectionConcatObjectRemoved(sId);
      _triggerChange();
    },

    handleOperatorAttributeValueChanged: function(sId, sType, sValue){
      AttributeStore.handleOperatorAttributeValueChanged(sId, sType, sValue);
      _triggerChange();
    },

    deleteOperatorAttributeValue: function(sId){
      AttributeStore.deleteOperatorAttributeValue(sId);
      _triggerChange();
    },

    handleCommonConfigSectionViewRadioButtonClicked: function (sContext, sRadioKey) {
      switch (sContext) {
          case ThemeConfigurationConstants.LANDING_PAGE:
          case ThemeConfigurationConstants.PRODUCT_INFORMATION_PAGE:
              ViewConfigurationScreenStore.handleViewConfigurationRadioButtonClicked(sContext, sRadioKey);
              break;
      }
      _triggerChange();
    },

    handleCommonConfigSectionSingleTextChanged: function (sContext, sKey, sVal) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = CS.split(sContext, sSplitter);
      sContext = aSplitContext[0];
      switch (sContext){
        case "attribute":
          AttributeStore.handleAttributeSingleTextChanged(sKey, sVal);
          break;

        case "tag":
          var oModel = {};
          if (sKey === "imageExtension") {
            sVal = CS.toLower(sVal);
          }
          TagStore.handleTagDataChanged(oModel, sKey, sVal);
          break;

        case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
          AttributionTaxonomyStore.handleTaxonomyNameChanged(sKey, sVal);
          break;

        case "attributionTaxonomyLevel":
        case "attributionTaxonomyLevelChildren":
          AttributionTaxonomyStore.handleTaxonomyLevelSingleValueChanged(sKey, sVal, sContext);
          break;

        case "attributionTaxonomyDetail":
          AttributionTaxonomyStore.handleDetailedTaxonomySingleValueChanged(sKey, sVal);
          break;

        case "context":
          ContextStore.handleCommonConfigAttributeChanged(sKey, sVal);
          break;

        case "tasks":
          TasksStore.handleTaskConfigFieldValueChanged(sKey, sVal);
          break;

        case "dataGovernance":
          DataGovernanceTasksStore.handleDataGovernanceTaskConfigFieldValueChanged(sKey, sVal);
          break;

        case "class":
          ClassStore.handleCommonConfigAttributeChanged(sKey, sVal);
          break;

        case "classAssetUploadConfig":
          ClassStore.handleClassAssetUploadConfigChanged(sKey, sVal);
          break;

        case "template":
          TemplateStore.handleCommonConfigAttributeChanged(sKey, sVal);
          break;

        case "class_relationship":
          var sRelationshipId = aSplitContext[1];
          ClassStore.handleClassRelationshipModified(sRelationshipId, sKey, sVal);
          break;

        case "propertycollection":
          PropertyCollectionStore.handleCommonConfigPropertyCollectionChanged(sKey, sVal);
          break;

        case "profile":
          ProfileStore.handleProfileConfigFieldValueChanged(sKey, sVal);
          break;

        case 'languageKlassContext':
        case "embeddedKlassContext":
        case "technicalImageContext":
          ClassStore.handleClassContextDialogPropertyChanged(sKey, sVal);
          break;

        case "mapping":
          MappingStore.handleMappingConfigFieldValueChanged(sKey, sVal);
          break;

        case "authorization_mapping":
          AuthorizationStore.handleAuthorizationMappingConfigFieldValueChanged(sKey, sVal);
          break;

        case "process":
          ProcessStore.handleProcessSingleValueChanged(sKey,sVal);
          break;

        case "endpoint":
          ProfileStore.handleAttributeValueChanged(sKey, sVal);
          break;

        case "kpiConfig":
        case "kpiConfigDetail":
          KpiStore.handleKPIDetailSingleValueChanged(aSplitContext[1], sKey, sVal);
          break;

        case ViewContextConstants.ORGANISATION_CONFIG_CONTEXT:
          OrganisationConfigStore.handleOrganisationSingleValueChanged(sKey,sVal);
          break;

        case "relationship":
        case "relationshipConfigDetail":
          RelationshipStore.handleRelationshipSingleValueChanged(sKey,sVal);
          break;

        case "role":
          RoleStore.handleRoleSingleValueChanged(sKey,sVal);
          break;

        case "ruleList":
          RuleListStore.handleRuleListValueChanged(sKey, sVal);
          break;

        case "rule":
          RuleStore.handleRuleSingleValueChanged(sKey, sVal);
          break;

        case "organization":
          OrganisationConfigStore.handleOrganisationSingleValueChanged(sKey, sVal);
          break;

        case "languageTree":
          LanguageTreeStore.handleCommonConfigDialogValueChanged(sKey, sVal);
          break;

        case "smartDocument":
          SmartDocumentStore.handleCommonConfigValueChanged(sKey, sVal, sContext);
          break;

        case ViewContextConstants.RULE_DETAILS_VIEW_CONFIG:
          SettingScreenStore.handleRuleNameChanged(sVal);
          break;

        case ViewContextConstants.TABS_CONFIG:
          TabsStore.handleSingleValueChanged(sKey, sVal);
          break;

        case ViewContextConstants.GOLDEN_RECORD_RULE:
          GoldenRecordsStore.handleRuleSingleValueChanged(sKey, sVal);
          break;

        case "ThemeConfigurationConfig" :
          ThemeConfigurationScreenStore.handleThemeConfigurationSectionValueChanged(sKey, sVal);
          break;

        case "pdfReactorConfig":
          PdfReactorServerStore.handleCommonConfigSingleValueChanged(sKey, sVal);
          break;

        case "ssoSetting" :
          SSOSettingStore.handleSSOSingleTextChanged(sKey, sVal);
          break;

        case "loadBalancerServerConfiguration":
          IndesignServerConfigurationStore.handleLoadBalancerConfigSingleValueChanged(sKey, sVal);
          break;

        case "indesignServerConfiguration":
          IndesignServerConfigurationStore.handleIDSNServerConfigSingleValueChanged(sKey, sVal);
          break;
      }
      _triggerChange();
    },

    handleCommonConfigSectionFroalaTextChanged: function (sContext, sKey, oVal) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = CS.split(sContext, sSplitter);
      sContext = aSplitContext[0];
      switch (sContext){
        case "attribute":
          AttributeStore.handleAttributeFroalaTextChanged(sKey, oVal);
          break;
      }
      _triggerChange();
    },

    handleCommonConfigSectionViewIconChanged: function (sKey, sConfigContext, aFiles) {

      var oCallback = {};
      var bLimitImageSize = true;

      switch (sConfigContext){

        case "tag":
          var oModel = {};
          oCallback.functionToExecute = TagStore.handleTagIconChanged.bind(this, oModel, sKey);
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "class":
          oCallback.functionToExecute = ClassStore.handleClassIconChanged;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "previewImage":
          oCallback.functionToExecute = ClassStore.handleClassPreviewImageChanged;
          bLimitImageSize = false;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "attributionTaxonomyDetail":
          oCallback.functionToExecute = AttributionTaxonomyStore.handleTaxonomyUploadIconChangeEvent;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "template":
          oCallback.functionToExecute = TemplateStore.handleTabIconChanged.bind(this, sKey);
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "context":
          oCallback.functionToExecute = ContextStore.handleContextUploadIconChangeEvent;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "organisation":
          oCallback.functionToExecute = OrganisationConfigStore.handleOrganisationUploadIconChangeEvent;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "dataGovernanceTasks":
          oCallback.functionToExecute = DataGovernanceTasksStore.handleDataGovernanceTaskUploadIconChangeEvent;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "propertycollection":
          oCallback.functionToExecute = PropertyCollectionStore.handlePropertyCollectionIconChanged;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "relationship":
          oCallback.functionToExecute = RelationshipStore.handleRelationshipIconChanged;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "embeddedKlassContext":
        case "languageKlassContext":
        case "technicalImageContext":
          oCallback.functionToExecute = ClassStore.handleClassContextUploadIconChangeEvent;
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;
        case "languageTree":
          oCallback.functionToExecute = LanguageTreeStore.handleCommonConfigDialogValueChanged.bind(this, sKey);
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;

        case "primaryLogoId":
        case "generalHeaderLogo":
        case "faviconId":
        case "loginScreenBackgroundThumbKey":
          bLimitImageSize = false;
          oCallback.getAssetInfo = true;
          oCallback.functionToExecute = ThemeConfigurationScreenStore.handleThemeConfigurationSectionIconChanged.bind(this, sConfigContext);
          _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
          break;
      }
      IconLibraryProps.setSelectedIconIds([]);
      _triggerChange();
    },

    handleCommonConfigSectionViewSingleTextNumberChanged: function (sContext, sKey, sNewValue) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = CS.split(sContext, sSplitter);
      sContext = aSplitContext[0];
      switch(sContext) {
        case "class_relationship":
          var sRelationshipId = aSplitContext[1];
          ClassStore.handleClassRelationshipModified(sRelationshipId, sKey, sNewValue);
          break;
        case "class":
          ClassStore.handleCommonConfigAttributeChanged(sKey, sNewValue);
          break;
        case "attribute":
        AttributeStore.handleAttributeSingleTextChanged(sKey, sNewValue);
        break;
        case "tag":
          var oModel = {};
          TagStore.handleTagDataChanged(oModel, sKey, sNewValue);
          break;
      }
      _triggerChange();
    },

    handleCommonConfigSectionViewNativeDropdownValueChanged: function (sContext, sKey, sNewValue) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = CS.split(sContext, sSplitter);
      sContext = aSplitContext[0];
      switch (sContext) {
        case "attribute":
          AttributeStore.handleAttributeNativeDropdownValueChanged(sKey, sNewValue);
          _triggerChange();
          break;
        case "class_relationship":
          var sRelationshipId = aSplitContext[1];
          ClassStore.handleClassRelationshipModified(sRelationshipId, sKey, sNewValue);
          break;
      }
    },

    handleCommonConfigCheckboxOnChangeForEntityPermission: function(bIsChecked, sContext, sScreenContext) {
      if(sScreenContext == "tagType") {
        TagStore.handleTagDataChanged({}, sContext, bIsChecked);
      }
      else if(sScreenContext == "attributeType") {
        AttributeStore.handleAttributeSingleTextChanged(sContext, bIsChecked);
      }
      _triggerChange();
    },

    handleCommonConfigUploadIconClicked: function (oRefDom, sConfigContext) {
      _handleCommonConfigUploadIconClicked(oRefDom, sConfigContext);
    },

    handleCommonConfigUploadTemplateActionClicked: function(oElementData,aFiles) {
      SmartDocumentStore.handleCommonConfigUploadTemplateActionClicked(oElementData,aFiles);
    },

    handleSettingScreenRoleDataChanged: function (oModel, sContext, sNewValue) {
      RoleStore.handleSettingScreenRoleDataChanged(oModel, sContext, sNewValue);
      _triggerChange();
    },

    handleRoleModulePermissionChanged: function (sModule) {
      RoleStore.handleRoleModulePermissionChanged(sModule);
      _triggerChange();
    },

    handleDashboardVisibilityToggled: function () {
      RoleStore.handleDashboardVisibilityToggled();
    },

    handleReadOnlyPermissionToggled: function () {
      RoleStore.handleReadOnlyPermissionToggled();
    },

    handleTagTypeRangeValueChanged: function(sTagValueId, sNewValue) {
      TagStore.handleTagTypeRangeValueChanged(sTagValueId, sNewValue);
      _triggerChange();
    },

    handleContextTagCheckAllChanged:function(sContextTagId){
      ContextStore.handleContextTagCheckAllChanged(sContextTagId);
    },

    handleDefaultFromDateValueChanged:function(sValue, sContext){
      ContextStore.handleDefaultFromDateValueChanged(sValue, sContext);
    },

    handleContextConfigDialogButton: function (sContext) {
      ContextStore.handleContextConfigDialogButton(sContext);
    },

    getUnSavedRoleFromMasterById: function (sId) {
      var oRoleMap = _getAppData().getRoleList();
      return oRoleMap[sId].clonedObject ? oRoleMap[sId].clonedObject : oRoleMap[sId];
    },

    handleRelationshipDataChanged: function (oNewModel, oReferencedKlasses) {
      RelationshipStore.handleRelationshipDataChanged(oNewModel, oReferencedKlasses);
    },

    createClass: function (sNodeId) {
      ClassStore.createClass(sNodeId);
    },

    deleteClass: function (sClickedNodeId) {
      ManageEntityConfigProps.setIsDelete(true);
      ClassStore.deleteClass(sClickedNodeId);
    },

    createLanguage: function (sNodeId) {
      LanguageTreeStore.createLanguage(sNodeId);
    },

    createDummySmartDocumentSection: function (sNodeId) {
      SmartDocumentStore.createDummySmartDocumentSection(sNodeId);
    },

    deleteLanguage: function (sNodeId) {
      ManageEntityConfigProps.setIsDelete(true);
      LanguageTreeStore.deleteLanguage(sNodeId);
    },

    deleteSmartDocument: function (sNodeId, iLevel) {
      SmartDocumentStore.deleteSmartDocument(sNodeId, iLevel);
    },

    createPropertyCollection: function (sContext) {
      PropertyCollectionStore.createPropertyCollection(sContext);
    },

    deletePropertyCollection: function () {
      ManageEntityConfigProps.setIsDelete(true);
      PropertyCollectionStore.deletePropertyCollection();
    },

    handleClassNodeRefreshMenuClicked: function () {
      ClassStore.handleClassNodeRefreshMenuClicked();
      _triggerChange();
    },

    handlePropertyCollectionNodeRefreshMenuClicked: function () {
      let bIsPropertyCollectionScreenLocked = PropertyCollectionStore.getPropertyCollectionScreenLockedStatus();
      let oSwitchCallback = {
        functionToExecute: async () => {
          SettingScreenProps.screen.setEntitySearchText("");
          SettingScreenProps.propertyCollectionConfigView.setPropertyCollectionDraggableListActiveTabId(ConfigDataEntitiesDictionary.ATTRIBUTES);
          PropertyCollectionStore.fetchPropertyCollectionsList();
          await this.handleAvailableListViewSearched();
        }
      };

      if (bIsPropertyCollectionScreenLocked) {
        CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            PropertyCollectionStore.saveUnsavedClass.bind(this, oSwitchCallback),
            PropertyCollectionStore.discardUnsavedPropertyCollection.bind(this, oSwitchCallback),
            CS.noop);
      } else {
        oSwitchCallback.functionToExecute();
      }
      _triggerChange();
    },

    handleSelectionToggleButtonClicked: function (sContext, sKey, sId, bSingleSelect) {
      switch (sContext) {
        case "class":
          ClassStore.handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
          break;
        case "context":
          ContextStore.handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
          break;
        case "relationship":
          RelationshipStore.handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
          break;
        case "profile":
          ProfileStore.handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
          break;
        case "process":
          ProcessStore.handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
          break;
        case "role":
          RoleStore.handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
          break;

        case 'languageKlassContext':
        case "embeddedKlassContext":
        case "technicalImageContext":
          ClassStore.handleClassContextDialogSelectionToggleClicked(sKey, sId);
          break;

        case "dataRules":
            ProfileStore.handleProfileConfigDataSelectionToggled(sKey,sId);
          break;

        case "attributionTaxonomyDetail":
          AttributionTaxonomyStore.handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
          break;

        case ViewContextConstants.ORGANISATION_CONFIG_CONTEXT:
          OrganisationConfigStore.handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
          break;

        case "smartDocument":
          SmartDocumentStore.handleCommonConfigValueChanged(sKey, sId);
          break;

        case "ruleList":
          RuleListStore.handleRuleListValueChanged(sKey, sVal); // eslint-disable-line
          break;

        case "rule":
          RuleStore.handleSelectionToggleButtonClicked(sKey, sId);
          break;

        case "kpi":
          KpiStore.handleSelectionToggleButtonClicked(sKey, sId);
          break;

        case "goldenRecordRule":
          GoldenRecordsStore.handleSelectionToggleButtonClicked(sKey, sId);
          break;

      }
    },

    handleMultiSelectSearchCrossIconClicked: function (sContextKey, sId) {

      var sSplitter = SettingUtils.getSplitter();
      var sScreenName = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
      var aOldContextKey = sContextKey.split(sSplitter);
      sContextKey = aOldContextKey[0];

      switch (sContextKey) {
        case "tab":
        case "tabId":
          _handleConfigTabChangedByScreen(sContextKey, {}, aOldContextKey);
          break;

        case "role":
          RoleStore.handleMultiSelectSearchCrossIconClicked(aOldContextKey[1], sId);
          break;

        case "attributionTaxonomyDetail":
          AttributionTaxonomyStore.handleMSSCrossIconClicked(false, aOldContextKey[1],sId);
          break;

        case "contextConfig":
          var sSelectorId = aOldContextKey[1];
          var sTagId = aOldContextKey[2];
          ContextStore.handleContextCombinationDeleteTagValue(sSelectorId,sTagId,sId);

          break;

        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case "technicalImageContext":
          var sSelectorId = aOldContextKey[1];
          var sTagId = aOldContextKey[2];
          ClassStore.handleContextCombinationDeleteTagValue(sSelectorId,sTagId,sId);
          break;

        case 'dataGovernanceTasksConfigColor':
          DataGovernanceTasksStore.handleDataGovernanceTaskConfigFieldValueChanged("color", "");
          break;

        case 'dataGovernanceTaskStatusTag':
          DataGovernanceTasksStore.handleDataGovernanceTaskConfigFieldValueChanged("statusTag", null);
          break;

        case 'dataGovernanceTaskPriorityTag':
          DataGovernanceTasksStore.handleDataGovernanceTaskConfigFieldValueChanged("priorityTag", null);
          break;

        case 'ruleFilterRolesUsers':
            RuleStore.handleRuleAttributeValueChanged(aOldContextKey[1], aOldContextKey[2], [sId], aOldContextKey[3], true);
          break;

        case "ruleFilterRolesUsersForNormalization":
          RuleStore.handleRuleAttributeValueChangedForNormalization(aOldContextKey[1], aOldContextKey[2], sId,aOldContextKey[3],true);
          break;

        case 'contentFilterTagsInner':
          var sTagGroupId = aOldContextKey[1];
          var sViewContext = aOldContextKey[2];
          var sRuleId = aOldContextKey[3];
          RuleStore.handleContentFilterInnerTagAdded(sTagGroupId, [sId], sRuleId, 0, sViewContext, true);

          break;

        case 'classConfigNatureType':
          ClassStore.handleNatureTypeChanged("", aOldContextKey[1]);
          break;

        case "groupedRelationshipViewItemSelection":
          ClassStore.handleGroupedRelationshipRemoveItemByOtherSide(sId);
          break;

        case "classConfigGtinKlass":
        case 'gtinKlass':
          ClassStore.handleRemoveGtinClass(sId);
          break;

        case "languageKlass":
        case "classConfigLanguageKlass":
          ClassStore.handleRemoveLanguageClass(sId);
          break;

        case "classConfigEmbeddedKlass":
        case 'embeddedKlass':
          ClassStore.handleRemoveEmbeddedKlassId(sId);
          break;

        case 'technicalImageKlass':
          ClassStore.handleRemoveTechnicalImageKlassId(sId);
          break;

        case 'classConfigTagDefaultValue':
          if (sScreenName === ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
            AttributionTaxonomyStore.removeDefaultTagValue(aOldContextKey[1], aOldContextKey[2], aOldContextKey[3], sId);
          }
          else {
            ClassStore.removeDefaultTagValue(aOldContextKey[1], aOldContextKey[2], aOldContextKey[3], sId);
          }
          break;

        case "kpiConfigDetail":
          KpiStore.handleMSSCrossIconClicked(aOldContextKey[1], aOldContextKey[2], sId);
          break;

        case "systems":
          ProfileStore.handleSystemsMSSApplyClicked([]); //it is single select, so on cross clicked, will have to make it empty
          break;

        case ViewContextConstants.ORGANISATION_CONFIG_CONTEXT:
          OrganisationConfigStore.handleOrganisationMSSValueRemoved(aOldContextKey[1], sId);
          break;

        case "classConfigDataRules":
        case "classConfigTasks":
        case "classConfigLifeCycleStatusTags":
        case "classConfigMultiSelect" :
        case "classConfigNatureRelationship" :
        case "classConfigTaxonomyTransfer" :
        case "dtpClassConfigMarketKlass":
          ClassStore.handleMSSValueRemoved(aOldContextKey[1], sId, sContextKey);
          break;

        case 'relationship':
          RelationshipStore.handleSelectionToggleButtonClicked(aOldContextKey[1], sId);
          break;

        case "classConfigVariantContext":
          ClassStore.handleSelectionToggleButtonClicked(aOldContextKey[1], sId, true);
          break;

        case "process":
          ProcessStore.handleLazyMSSValueRemoved(aOldContextKey,sId);
          break;

        case "profile":
          ProfileStore.handleLazyMSSValueRemoved(aOldContextKey[1],sId);
          break;

        case "ruleDataLanguages":
          RuleStore.handleDataLanguagesMSSCrossIconClicked(sId);
          break;

        case "smartDocument":
          SmartDocumentStore.handleKlassMSSCrossIconClicked(sId, aOldContextKey[1]);

        default:

      }
    },

    handleRuleCalcAttrAddOperator: function (sAttrId, sId, sOperatorType) {
      RuleStore.handleRuleCalcAttrAddOperator(sAttrId, sId, sOperatorType);
      _triggerChange();
    },

    handleRuleCalcAttrOperatorAttributeValueChanged: function (sAttrId, sId, sType, sAttributeOperatorId, sValue) {
      RuleStore.handleRuleCalcAttrOperatorAttributeValueChanged(sAttrId, sId, sType, sAttributeOperatorId, sValue);
      _triggerChange();
    },

    handleRuleCalcAttrDeleteOperatorAttributeValue: function (sAttrId, sId, sAttributeOperatorId) {
      RuleStore.handleRuleCalcAttrDeleteOperatorAttributeValue(sAttrId, sId, sAttributeOperatorId);
      _triggerChange();
    },

    handleRuleCalcAttrCustomUnitChanged: function (sAttrId, sId, sUnit, sUnitAsHTML) {
      RuleStore.handleRuleCalcAttrCustomUnitChanged(sAttrId, sId, sUnit, sUnitAsHTML);
      _triggerChange();
    },

    handleTagGroupTagValueChanged: function (sTagId, aTagValueRelevanceData, oExtraData) {
      let sOuterContext = oExtraData && oExtraData.outerContext;
      let sAttributeValId = oExtraData && oExtraData.attributeValueId;
      let sInnerContext = oExtraData && oExtraData.innerContext;
      let sScreenName = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
      let sSplitter = SettingUtils.getSplitter();
      let sModelContext = oExtraData && oExtraData.modelContext;
      let aContextKey = sModelContext && sModelContext.split(sSplitter);
      var oEntity = null;
      let oCallbackData = {};

      switch (sOuterContext) {
        case "contentFilterTagsInner":
          if (sInnerContext == "ruleFilterTagsTagValuesForNormalization") {
            RuleStore.handleRuleFilterInnerTagForNormalizationAdded(sTagId, aTagValueRelevanceData);
          } else {
            let sSplitter = SettingUtils.getSplitter();
            let sScreenContext = oExtraData.screenContext;
            let aSplitScreenContext = !CS.isEmpty(sScreenContext) ? sScreenContext.split(sSplitter) : [];
            if (!CS.isEmpty(aSplitScreenContext) && aSplitScreenContext[0] == "kpiConfigDetail") {
              KpiStore.handleRuleFilterInnerTagAdded(sTagId, aTagValueRelevanceData, sAttributeValId, aSplitScreenContext[1]);
            } else {
              RuleStore.handleRuleFilterInnerTagAdded(sTagId, aTagValueRelevanceData, sAttributeValId);
            }
          }
          _triggerChange();
          break;

        case "classConfigSelectedTagValues":
          if (sScreenName === ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
            oEntity = AttributionTaxonomyStore.makeActiveDetailedAttributionTaxonomyDirty();
            oCallbackData.functionToExecute = AttributionTaxonomyStore.saveTaxonomyDetails;
          }
          else {
            oEntity = ClassUtils.makeActiveClassDirty();
            oCallbackData.functionToExecute = ClassStore.saveClass.bind(this, {});
          }

          let aSelectedTagValueIds = CS.map(aTagValueRelevanceData, "tagId");
          SettingUtils.handleSectionMSSValueChanged(oEntity, aContextKey[1], aContextKey[2], "selectedTagValues", aSelectedTagValueIds, "", oCallbackData);
          break;

        case 'classConfigTagDefaultValue':
           if (sScreenName === ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
            oEntity = AttributionTaxonomyStore.makeActiveDetailedAttributionTaxonomyDirty();
          }
          else {
            oEntity = ClassUtils.makeActiveClassDirty();
          }

          SettingUtils.updateConfigSectionTagValueRelevance(oEntity.sections, aContextKey[1], aContextKey[2], aTagValueRelevanceData);
          _triggerChange();
          break;

        case "processSearchFilter":
            ProcessStore.handleFilterTagValueChanged(sTagId, aTagValueRelevanceData);
          break;
      }
    },

    handleDropdownListNodeClicked: function (sContextKey, oModel, bIsSingleSelect) {
      var aMSSNewViewContexts = [
        "attributeType", "attributeRTEIcon", "attributeDefaultStyles", "attributeChildAttributes", "attributeDetailAllowedFrame",
        "tagConfigColor", "tagType", "tagDefaultValue", "allowedClasses", "defaultUnit", "ruleFilterAttributes",
        "tasksConfigColor","taskStatusTag","taskPriorityTag", "ruleFilterRelationships","ruleFilterRoles",
        "ruleFilterRolesUsers", "stateFilterAttributes", "stateFilterTags", "stateFilterRelationships","ruleFilterTypes",
        "ruleFilterTags","contentFilterTagsInner", "causeEffectActionState", "causeEffectActionUser", "contentStateTag",
        "stateFilterRoles", "stateFilterUsers", "taxonomyFilterBy", "taxonomySortBy", "taxonomyDefaultFilter",
        "taxonomyDefaultFilterTagValue", "ruleFilterRolesUsersForNormalization", "classConfigTagDefaultValue",
        "calculatedAttributeType", "calculatedAttributeUnit", "relationshipSide1Property", "relationshipSide2Property", "contextView",
        "onboardingEndpointsWorkspace"];

      var sSplitter = SettingUtils.getSplitter();
      var aOldContextKey = sContextKey.split(sSplitter);
      sContextKey = aOldContextKey[0];

      var sScreenName = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();

      var iItemID = CS.includes(aMSSNewViewContexts, sContextKey)? oModel : oModel.id;

      switch (sContextKey) {
        case 'attributeType':
          AttributeStore.handleAttributeTypeListItemClicked(iItemID);
          _triggerChange();
          break;

        case 'calculatedAttributeType':
          AttributeStore.handleCalculatedAttributeTypeListItemClicked(iItemID);
          _triggerChange();
          break;

        case 'calculatedAttributeUnit':
          AttributeStore.handleCalculatedAttributeUnitListItemClicked(iItemID);
          _triggerChange();
          break;

        case 'attributeDetailAllowedFrame':
          AttributeStore.handleAllowedFrameMultiSelectListItemClicked(sContextKey, iItemID);
          _triggerChange();
          break;

        case 'attributeRTEIcon':
          AttributeStore.handleAllowedRTEIconsMultiSelectListItemClicked(sContextKey, [iItemID]);
          _triggerChange();
          break;

        case 'attributeDefaultStyles':
          AttributeStore.handleAllowedDefaultStylesMultiSelectListItemClicked(sContextKey, [iItemID]);
          _triggerChange();
          break;

        case 'attributeChildAttributes':
          AttributeStore.handleChildAttributesMultiSelectListItemClicked(sContextKey, iItemID);
          _triggerChange();
          break;

        case 'tagConfigColor':
          TagStore.handleTagDataChanged(null, "color", iItemID);
          _triggerChange();
          break;

        case "tagDefaultValue":
          TagStore.handleTagDefaultValueChanged({}, iItemID);
          _triggerChange();
          break;

        case "tagType":
          TagStore.handleTagTypeChanged(oModel, iItemID);
          _triggerChange();
          break;

        case 'defaultUnit':
          AttributeStore.handleDefaultUnitListItemClicked(iItemID);
          _triggerChange();
          break;

        case 'ruleFilterAttributes':
          var sAttributeContext = aOldContextKey[2];
          RuleStore.handleAttributeAdded(iItemID, sAttributeContext);
          break;

        case 'ruleFilterRelationships':
          var sAttributeContext = aOldContextKey[2];
          RuleStore.handleRelationshipAdded(iItemID, sAttributeContext);
          break;

        case 'contentFilterTagsInner':
          var sTagGroupId = aOldContextKey[1];
          var sViewContext = aOldContextKey[2];
          var sRuleId = aOldContextKey[3];
          RuleStore.handleContentFilterInnerTagAdded(sTagGroupId, [iItemID], sRuleId, 100, sViewContext);
          break;

        case 'ruleFilterRoles':
          var sAttributeContext = aOldContextKey[2];
          RuleStore.handleRoleAdded(iItemID, sAttributeContext);
          break;

        case 'ruleFilterRolesUsers':
            RuleStore.handleRuleAttributeValueChanged(aOldContextKey[1], aOldContextKey[2], [iItemID], aOldContextKey[3]);
          break;

        case 'ruleFilterRolesUsersForNormalization':
          RuleStore.handleRuleAttributeValueChangedForNormalization(aOldContextKey[1], aOldContextKey[2], iItemID,aOldContextKey[3]);
          break;

        case 'ruleFilterTypes':
          var sAttributeContext = aOldContextKey[2];
          RuleStore.handleTypeAdded(iItemID, sAttributeContext);
          break;

        case 'ruleFilterTags':
          var sAttributeContext = aOldContextKey[2];
          RuleStore.handleTagAdded(iItemID, sAttributeContext);
          break;

        case 'classConfigTagDefaultValue':
          ClassStore.addDefaultTagValue(aOldContextKey[1], aOldContextKey[2], aOldContextKey[3], iItemID, bIsSingleSelect);
          break;

        case 'relationshipSide1Property':
          RelationshipStore.addPropertyInRelationshipSide('side1', iItemID);
          break;

        case 'relationshipSide2Property':
          RelationshipStore.addPropertyInRelationshipSide('side2', iItemID);
          break;

        case 'onboardingEndpointsWorkspace':
          ProfileStore.handleProfileConfigFieldValueChanged("workSpace", iItemID);
          break;

        default:

      }
    },

    handleDropdownListNodeBlurred: function (sContextKey, aCheckedItems) {
      var sSplitter = SettingUtils.getSplitter();
      var aOldContextKey = sContextKey.split(sSplitter);
      sContextKey = aOldContextKey[0];
      var sScreenContext = '';

      switch (sContextKey) {

        case 'attributeRTEIcon':
          AttributeStore.handleAllowedRTEIconsMultiSelectListItemClicked(sContextKey, aCheckedItems);
          _triggerChange();
          break;

        case 'attributeDefaultStyles':
          AttributeStore.handleAllowedDefaultStylesMultiSelectListItemClicked(sContextKey, aCheckedItems);
          _triggerChange();
          break;

        case 'classConfigTagDefaultValue':
          var bIsSingleSelect = false;
          ClassStore.addDefaultTagValueForConfigTabularView(aOldContextKey[1], aOldContextKey[2], aOldContextKey[3], aCheckedItems, bIsSingleSelect);
          break;

        case 'contentFilterTagsInner':
          var sTagGroupId = aOldContextKey[1];
          var sViewContext = aOldContextKey[2];
          var sRuleId = aOldContextKey[3];
          RuleStore.handleContentFilterInnerTagAdded(sTagGroupId, aCheckedItems, sRuleId, 100, sViewContext);
          break;

        case 'ruleFilterRolesUsers':
          sScreenContext = aOldContextKey[aOldContextKey.length - 1];
          RuleStore.handleRuleAttributeValueChanged(aOldContextKey[1], aOldContextKey[2], aCheckedItems, aOldContextKey[3]);
          break;

        case 'gtinKlass':
          ClassStore.handleGtinClassAddIds(aCheckedItems);
          break;

        case 'embeddedKlass':
        case 'technicalImageKlass': //since, behaviour is same for Embedded Klass & Technical Image Klass
          ClassStore.handleAddEmbeddedKlassIds(aCheckedItems);
          break;

        //Single Select Type
        case 'attributeType':
        case 'attributeDetailAllowedFrame':
        case 'attributeChildAttributes':
        case 'tagConfigColor':
        case "tagDefaultValue":
        case "tagType":
        case 'defaultUnit':
        case 'ruleFilterAttributes':
        case 'ruleFilterRelationships':
        case 'ruleFilterRoles':
        case 'ruleFilterRolesUsersForNormalization':
        case 'ruleFilterTypes':
        case 'ruleFilterTags':
        case 'stateFilterAttributes':
        case 'stateFilterTags':
        case 'stateFilterRelationships':
        case 'contentStateTag':
        case 'stateFilterRoles':
        case 'stateFilterUsers':
        case 'taxonomyDefaultFilterTagValue':
          break;

      }
    },

    handleUploadImageChangeEvent: function (aFiles) {
      var oCallback = {};
      var bLimitImageSize = false;
      oCallback.functionToExecute = UserStore.handleUploadImageChangeEvent;
      _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
    },

    handleRemoveUserImageClicked: function () {
      _handleRemoveUserImageClicked();
      _triggerChange();
    },

    handleUserDataChangeEvent: function (oModel, sContext, sNewValue, oEvent) {
      UserStore.handleUserDataChangeEvent(oModel, sContext, sNewValue, oEvent);
    },

    handleUserPasswordSubmit: function (sPassword) {
      UserStore.handleUserPasswordSubmit(sPassword);
    },

    handleUserPasswordCancel: function () {
      UserStore.handleUserPasswordCancel();
    },

    handleClassCreateDialogButtonClicked: function (sContext, sLabel, bIsNature, sNatureType) {
      ClassStore.handleClassCreateDialogButtonClicked(sContext, sLabel, bIsNature, sNatureType);
    },

    handleClassSaveDialogButtonClicked: function (sContext) {
      if (sContext == "save") {
        ClassStore.saveClass({});
      } else {
        ClassStore.discardUnsavedClass({});
      }
      _triggerChange();
    },

    handleClassConfigImportButtonClicked: function (aFiles) {
      let oImportExcel = {};
      oImportExcel.sUrl = oSettingsRequestMapping.ImportExcel;
      ClassStore.handleClassConfigImportButtonClicked(aFiles,oImportExcel);
    },

    handleLanguageTreeConfigImportButtonClicked: function (aFiles) {
      let oImportExcel = {};
      oImportExcel.sUrl = oSettingsRequestMapping.ImportExcel;
      oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.LANGUAGE;
      LanguageTreeStore.handleLanguageTreeConfigImportButtonClicked(aFiles, oImportExcel);
    },

    handleLanguageTreeConfigExportButtonClicked: function () {
      let oSelectiveExport = {};
      let oPostRequest = {};
      oSelectiveExport.sUrl = oSettingsRequestMapping.SelectiveExport;
      oPostRequest.exportType = "entity";
      oPostRequest.configCodes = [];
      oPostRequest.configType = oDataModelImportExportEntityTypeConstants.LANGUAGE;
      oSelectiveExport.oPostRequest = oPostRequest;
      LanguageTreeStore.handleLanguageTreeConfigExportButtonClicked(oSelectiveExport);
    },

    handleClassEntityDragStart: function (oModel) {
      var sContext = oModel.context;
      var oNewObj = {};

      oNewObj[sContext] = {
        dragStatus: oModel.properties['dragStatus'],
        dragContext: oModel.properties['dragContext'],
        draggedSectionId: oModel.id
      };

      var sLeftTreeNode = _getComponentProps().screen.getSelectedLeftNavigationTreeItem();
      if (sLeftTreeNode == ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT) {
        oComponentProps.contextConfigView.setContextDraggedStatus(true);
      } else if (sContext === "dragFromWithinClassSections"){
        oComponentProps.classConfigView.setClassSectionDragStatus(oNewObj);
      } else if (sContext === "dragTemplateSection"){
        oComponentProps.templateConfigView.setTemplateSectionDragStatus(oNewObj);
      } else {
        if (sContext === "dragFromAvailableList" || sContext === "dragFromWithinSection") {
          oComponentProps.classConfigView.setClassSectionDragStatus(oNewObj);
        }
      }

      _triggerChange();
    },

    handleClassEntityDragEnd: function () {
      oComponentProps.classConfigView.setClassSectionDragStatus({});
      oComponentProps.templateConfigView.setTemplateSectionDragStatus({});
      _triggerChange();
    },

    handleClassEntityDrop: function (oDropModel) {
      var sSplitter = SettingUtils.getSplitter();
      var oDraggedItem = oDropModel.properties['draggedItem'];
      var sActiveLeftNode = _getComponentProps().screen.getSelectedLeftNavigationTreeItem();
      var sContext = oDraggedItem.context;
      var aContexts = sContext.split(sSplitter);
      sContext = aContexts[0];
      if(sContext == ConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST){
        RuleStore.handleRuleListDropOnInput(oDropModel);
      }
      else if(sActiveLeftNode == ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT){
        ContextStore.handlePropertyDrop(sContext, oDropModel.id, oDraggedItem.id);
      } else {
        oComponentProps.classConfigView.setClassSectionDragStatus({});
        if (sContext === "dragFromWithinClassSections") {
          ClassStore.handleSectionDroppedFromWithinClassSection(oDropModel.id, oDraggedItem.id);
        }
      }

    },

    triggerChange: function () {
      _triggerChange();
    },

    handleDeleteVisualElementIconClicked: function (oInfo) {
        ClassStore.handleDeleteVisualElementIconClicked(oInfo);
      _triggerChange();
    },

    handleVisualElementBlockerClicked: function (oInfo) {
      var sScreenName = _getComponentProps().screen.getSelectedLeftNavigationTreeParentId();
      let sSelectedListItem = _getComponentProps().screen.getSelectedLeftNavigationTreeItem();
      if (sScreenName == "class") {
        ClassStore.handleVisualElementBlockerClicked(oInfo);
      } else if (sScreenName == "context") {
        ContextStore.handleVisualElementBlockerClicked(oInfo);
      } else if (sSelectedListItem == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        AttributionTaxonomyStore.handleVisualElementBlockerClicked(oInfo);
      }
    },

    handleSectionClicked: function (sSectionId) {
      ClassStore.handleSectionClicked(sSectionId);
      _triggerChange();
    },

    handleAddNewAssetConfigurationSectionClicked: function () {
       ClassStore.addNewAssetConfigurationSectionClicked();
    },

    handleEditAssetConfigurationSectionClicked: function (sId) {
       ClassStore.editAssetConfigurationSectionClicked(sId);
    },

    handleAssetConfigurationDialogButtonClicked :function (sId) {
      ClassStore.handleAssetConfigurationDialogButtonClicked(sId);
    },

    handleDeleteAssetConfigurationRowClicked: function(oRow) {
        ClassStore.handleDeleteAssetConfigurationRowClicked(oRow);
      _triggerChange();
    },

    handleAssetConfigRowDataChange: function(oField, sColumnId, oData) {
       ClassStore.handleAssetConfigRowDataChange(oField, sColumnId, oData);
      _triggerChange();
    },

    handleSectionNameChanged: function (sNewValue) {
      ClassStore.handleSectionNameChanged(sNewValue);
      this.triggerChange();
    },

    handleSectionIconChanged: function (aFiles) {
      var oCallback = {};
      var bLimitImageSize = true;
      oCallback.functionToExecute = ClassStore.handleSectionIconChanged;
      _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
    },

    handleSectionColCountChanged: function (oSection, iNewColCount) {
      var sSectionId = oSection.id;
      var bShowAlert = _getDataLostAlert(oSection, iNewColCount, "column");

      if (bShowAlert) {
        CustomActionDialogStore.showConfirmDialog(oTranslations().CLASS_ALERT_DATA_LOST, "",
            function () {
              ClassStore.handleSectionColCountChanged(sSectionId, iNewColCount);
            }, function (oEvent) {
              _triggerChange();
            });
      } else {
        ClassStore.handleSectionColCountChanged(sSectionId, iNewColCount);
      }

    },

    handleSectionRowCountChanged: function (oSection, iNewColCount) {
      var sSectionId = oSection.id;
      var bShowAlert = _getDataLostAlert(oSection, iNewColCount, "row");

      if (bShowAlert) {
        CustomActionDialogStore.showConfirmDialog(oTranslations().CLASS_ALERT_DATA_LOST, "",
            function () {
              ClassStore.handleSectionRowCountChanged(sSectionId, iNewColCount);
            }, function (oEvent) {
              _triggerChange();
            });
      } else {
        ClassStore.handleSectionRowCountChanged(sSectionId, iNewColCount);
      }
    },

    handleSectionDeleteClicked: function (sContext, sSectionId) {
      var sScreenName = _getComponentProps().screen.getSelectedLeftNavigationTreeParentId();
      let sSelectedListItem = _getComponentProps().screen.getSelectedLeftNavigationTreeItem();
      var sSplitter = SettingUtils.getSplitter();
      if (sScreenName == "class") {
        var aSplitContext = CS.split(sContext, sSplitter);
        sContext = aSplitContext[0];
        if (sContext == "class_relationship") {
          var sRelationshipId = aSplitContext[1];
          ClassStore.handleSectionDeletedFromRelationship(sRelationshipId, sSectionId);
        } else {
          ClassStore.handleSectionDeleteClicked(sSectionId);
        }
      } else if (sSelectedListItem == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
          AttributionTaxonomyStore.handleSectionsUpdated(sSectionId, false);
      } else if (sScreenName == "context") {
        ContextStore.handleSectionDeleted(sSectionId);
      } else if (sScreenName == "relationships") {
        RelationshipStore.handleSectionDeleted(sSectionId);
      }
    },

    handleSectionToggleButtonClicked: function (sContext, sSectionId) {
      var sScreenName = _getComponentProps().screen.getSelectedLeftNavigationTreeParentId() ;
      let sSelectedListItem = _getComponentProps().screen.getSelectedLeftNavigationTreeItem();
      if (sScreenName == "class") {
        if(sContext === ExportSide2RelationshipDictionary.RELATIONSHIP_LIST || sContext === ExportSide2RelationshipDictionary.PROPERTIES_LIST){
          ClassStore.handleSide2RelationshipSectionCollapseButtonClicked(sContext, sSectionId);
        }else {
          ClassStore.handleSectionToggleButtonClicked(sSectionId);
        }
      } else if (sSelectedListItem == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        //sections do not exist in Hierarchy taxonomy
        AttributionTaxonomyStore.handleSectionToggleButtonClicked(sSectionId);
      }
    },

    handleSectionSkippedToggled: function (sSectionId) {
      var sScreenName = _getComponentProps().screen.getSelectedLeftNavigationTreeParentId() ;
      if (sScreenName == "class") {
        ClassStore.handleSectionSkippedToggled(sSectionId);
      }

      _triggerChange();
    },

    handleSectionMoveUpClicked: function (sSectionId) {
      var sScreenName = _getComponentProps().screen.getSelectedLeftNavigationTreeParentId();
      if (sScreenName == "class") {
        ClassStore.handleSectionMoveUpClicked(sSectionId);
      } else if (sScreenName == "context") {
        ContextStore.handleSectionMoveUpClicked(sSectionId);
      }
    },

    handleSectionMoveDownClicked: function (sSectionId) {
      var sScreenName = _getComponentProps().screen.getSelectedLeftNavigationTreeParentId();
      if (sScreenName == "class") {
        ClassStore.handleSectionMoveDownClicked(sSectionId);
      } else if (sScreenName == "context") {
        ContextStore.handleSectionMoveDownClicked(sSectionId);
      }
    },

    handleSectionBlockerClicked: function (sSectionId) {
      ClassStore.handleSectionBlockerClicked(sSectionId);
    },

    toggleLinkScrollActive: function(){
      ClassStore.toggleLinkScrollActive(1);
    },

    handleMappingSummaryViewAddNewMappingRow: function (sContext, sSummaryType) {
      switch (sContext) {
        case 'profile':
          ProfileStore.handleMappingSummaryViewAddNewMappingRow(sSummaryType);
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewAddNewMappingRow(sSummaryType);
          break;
      }
    },

    handleMappingSummaryViewColumnNameChanged: function (sId, sName, sContext, sSummaryType) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewColumnNameChanged(sId, sName, sSummaryType);
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewColumnNameChanged(sId, sName, sSummaryType);
          break;
      }
    },

    handleAddTagValueClicked: function (sId, sMappedElementId, sContext) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleAddTagValueClicked(sId, sMappedElementId);
          break;

        case 'mapping':
          MappingStore.handleAddTagValueClicked(sId, sMappedElementId);
          break;
      }
    },

    handleMappingSummaryViewConfigTagValueChanged: function (sId, sMappedTagValueId, sNewValue,sContext) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewConfigTagValueChanged(sId, sMappedTagValueId, sNewValue);
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewConfigTagValueChanged(sId, sMappedTagValueId, sNewValue);
          break;
      }
    },

    handleMappingSummaryViewConfigTagValueIgnoreCaseToggled: function (sId, sMappedTagValueId, sContext) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewConfigTagValueIgnoreCaseToggled(sId, sMappedTagValueId);;
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewConfigTagValueIgnoreCaseToggled(sId, sMappedTagValueId);
          break;
      }
    },

    handleMappingSummaryViewConfigMappedTagValueChanged: function (sId, sMappedTagValueId, sMappedElementId,sContext, oReferencedData) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewConfigMappedTagValueChanged(sId, sMappedTagValueId, sMappedElementId);
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewConfigMappedTagValueChanged(sId, sMappedTagValueId, sMappedElementId, oReferencedData);
          break;
      }
    },

    handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked: function (sId, sMappedTagValueId,sContext) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked(sId, sMappedTagValueId);;
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked(sId, sMappedTagValueId);;
          break;
      }
    },

    handleTabLayoutTabChanged: function (sTabId, sContext) {
      switch (sContext) {
        case 'authorization_mapping':
          AuthorizationStore.handleTabLayoutTabChanged(sTabId);
          break;

        case 'mapping':
          MappingStore.handleTabLayoutTabChanged(sTabId);
          break;

        case 'process':
          ProcessStore.handleTabLayoutTabChanged(sTabId);
          break;
      }
    },

    handleMappingSummaryViewMappedElementChanged: function (sId, sMappedElementId, sContext, sSummaryType, oReferencedData) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewMappedElementChanged(sId, sMappedElementId, sSummaryType);
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewMappedElementChanged(sId, sMappedElementId, sSummaryType, oReferencedData);
          break;
      }
    },

    handleMappingSummaryViewMappingRowSelected: function (sId,sContext) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewMappingRowSelected(sId);
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewMappingRowSelected(sId);
          break;
      }
    },

    handleMappingSummaryViewIsIgnoredToggled: function (sId, sContext, sSummaryType) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewIsIgnoredToggled(sId, sSummaryType);
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewIsIgnoredToggled(sId, sSummaryType);
          break;
      }
    },

    handleMappingSummaryViewMappingRowDeleted: function (sId, sContext, sSummaryType) {
      switch(sContext){
        case 'profile':
          ProfileStore.handleMappingSummaryViewMappingRowDeleted(sId, sSummaryType);
          break;

        case 'mapping':
          MappingStore.handleMappingSummaryViewMappingRowDeleted(sId, sSummaryType);
          break;
      }
    },

    handleVisualElementAttributeValueChanged: function(oInfo){
      ClassStore.handleVisualElementAttributeValueChanged(oInfo);
    },

    handleTemplateDropDownLoadMoreSearchClicked: function (oData) {
      if (oData.loadMore) {
        _loadMoreFetchEntities(oData.entities);
      } else {
        _handleSearchAndFetchEntities(oData.entities, oData.searchText || "");
      }
    },

    handleSelectedEntityRemoved: function (sContext, sId) {
      TemplateStore.handleSelectedEntityRemoved(sContext, sId);
    },

    handleCustomTemplateSelectedEntitiesChanged: function (aSelectedItems, sContext) {
     TemplateStore.handleCustomTemplateSelectedEntitiesChanged(aSelectedItems, sContext);
    },

    handleCustomTemplatePopoverOpen: function (sContext) {
      SettingScreenProps.screen.resetEntitiesPaginationData();
      _getContextTypesForCustomTemplateConfigDetails();
      _fetchEntities([sContext]);
    },

    handleCustomTemplateLoadMoreClicked: function (aEntityList) {
      if(aEntityList[0] === ConfigDataEntitiesDictionary.CONTEXTS) {
        _getContextTypesForCustomTemplateConfigDetails();
      }
      _loadMoreFetchEntities(aEntityList,true);
    },

    handleCustomTemplateSearchClicked: function (aEntityList, sSearchText) {
      _handleSearchAndFetchEntities(aEntityList, sSearchText);
    },

    handleClassContextDialogAddTagCombination: function(){
      ClassStore.handleAddNewTagCombination();
    },

    handleClassContextDialogDateValueChanged: function(sKey, sDateValue){
      ClassStore.handleClassContextDialogDateValueChanged(sKey, sDateValue);
    },

    handleSectionElementCheckboxToggled: function (sSectionId, sElementId, sProperty) {
      let sActiveScreenName = SettingUtils.getConfigScreenViewName();

      switch(sActiveScreenName){
        case ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS:
          ClassStore.handleSectionElementCheckboxToggled(sSectionId, sElementId, sProperty);
          break;

        case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
          AttributionTaxonomyStore.handleSectionElementCheckboxToggled(sSectionId, sElementId, sProperty);
          break;

        case ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
          ContextStore.handleSectionElementCheckboxToggled(sSectionId, sElementId, sProperty);
          break;
      }
    },

    handleSectionElementInputChanged: function (sSectionId, sElementId, sProperty, sNewValue) {
      let sActiveScreenName = SettingUtils.getConfigScreenViewName();

      switch(sActiveScreenName){
        case ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS:
          ClassStore.handleSectionElementInputChanged(sSectionId, sElementId, sProperty, sNewValue);
          break;

        case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
          AttributionTaxonomyStore.handleSectionElementInputChanged(sSectionId, sElementId, sProperty, sNewValue);
          break;


        case ConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
          ContextStore.handleSectionElementInputChanged(sSectionId, sElementId, sProperty, sNewValue);
          break;
      }
    },

    handleSectionMSSValueChanged: function (sSectionId, sElementId, sProperty, aNewValue) {
      let sActiveScreenName = SettingUtils.getConfigScreenViewName();

      switch(sActiveScreenName){
        case ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS:
          ClassStore.handleSectionMSSValueChanged(sSectionId, sElementId, sProperty, aNewValue, sActiveScreenName);
          break;

        case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
          AttributionTaxonomyStore.handleSectionMSSValueChanged(sSectionId, sElementId, sProperty, aNewValue, sActiveScreenName);
          break;
      }

    },

    handlePropertyGroupCheckboxClicked: function (bIsCheckboxClicked, sContext) {
      MappingStore.handlePropertyGroupCheckboxClicked(bIsCheckboxClicked, sContext);
    },

    handlePropertyGroupApplyButtonClicked: function (sContext, oSelectedItems, oReferencedData) {
      MappingStore.handlePropertyGroupApplyButtonClicked(sContext, oSelectedItems, oReferencedData);
    },

    handlePropertyGroupDeleteButtonClicked: function (sSelectedElement, sContext) {
      MappingStore.handlePropertyGroupDeleteButtonClicked(sSelectedElement, sContext);
    },

    handlePropertyGroupCheckboxClickedForAttributeAndTag: function (bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag) {
      MappingStore.handlePropertyGroupCheckboxClickedForAttributeAndTag(bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag);
    },

    handlePropertyGroupPropertyCollectionListItemClicked: function (sSelectedElement, sContext) {
      MappingStore.handlePropertyGroupPropertyCollectionListItemClicked(sSelectedElement, sContext);
    },

    handlePropertyGroupColumnNameChanged: function (sId, sName, sSummaryType) {
      MappingStore.handlePropertyGroupColumnNameChanged(sId, sName, sSummaryType);
    },

    handlePropertyGroupSearchViewChanged: function (sSearchValue) {
      MappingStore.handlePropertyGroupSearchViewChanged(sSearchValue);
    },

    handlePropertyCollectionToggleButtonClicked: function (sContext, bIsPropertyCollectionToggleButtonClicked) {
      MappingStore.handlePropertyCollectionToggleButtonClicked(sContext, bIsPropertyCollectionToggleButtonClicked);
    },

    handleSectionAdded: function (aSectionIds, sContext) {
      let oScreenProps = _getComponentProps().screen;
      var sScreenName = oScreenProps.getSelectedLeftNavigationTreeParentId();
      let sSelectedItemType = oScreenProps.getSelectedLeftNavigationTreeItem();
      var sSplitter = SettingUtils.getSplitter();
      if (sScreenName == "class") {
        var aSplitContext = CS.split(sContext, sSplitter);
        sContext = aSplitContext[0];
        if (sContext == "class_relationship") {
          var sRelationshipId = aSplitContext[1];
          ClassStore.handleSectionAddedToRelationship(sRelationshipId, aSectionIds);
        } else {
          ClassStore.handleSectionAdded(aSectionIds);
        }
      } else if (sSelectedItemType == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        AttributionTaxonomyStore.handleSectionsUpdated(aSectionIds, true);
      } else if (sScreenName == "relationships") {
        RelationshipStore.handleSectionAdded(aSectionIds);
      }else if (sScreenName == "context"){
        ContextStore.handleSectionAdded(aSectionIds);
      }
    },

    handlePermissionTemplateAdded: function (aSectionIds) {
      PermissionStore.handlePermissionTemplateAdded(aSectionIds);
    },

    handleClassConfigRuleItemClicked: function(sRuleId){
      ClassStore.handleClassConfigRuleItemClicked(sRuleId);
      _triggerChange();
    },

    handleRoleRefreshMenuClicked: function () {
      var bRoleScreenLockStatus = RoleStore.getRoleScreenLockStatus();

      if (bRoleScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = RoleStore.getRoleDetails;

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            RoleStore.saveRole.bind(this, oCallbackData),
            RoleStore.discardUnsavedRole.bind(this, oCallbackData)).set({transition: 'fade'});
      }
      else {
        RoleStore.handleRoleRefreshMenuClicked();
        _triggerChange();
      }
    },

    handlePermissionRefreshMenuClicked: function () {
      var bPermissionScreenLockStatus = PermissionStore.getPermissionScreenLockStatus();
      if (bPermissionScreenLockStatus) {
        PermissionStore.handlePermissionRefreshMenuClicked();
      }
      else {
        if (CS.isEmpty(PermissionStore.getActivePermission())) {
          _resetPermissionConfigDetails();
        } else {
          PermissionStore.handlePermissionRefreshMenuClicked();
        }
      }
    },

    handlePermissionRestoreMenuClicked: function () {
      PermissionStore.handlePermissionRestoreMenuClicked();
    },

    /** Function to handle export of role permissions **/
    handleRolesPermissionsExportButtonClicked: function (sSelectedRole) {
        let oSelectiveExport = {};
        let oPostRequest = {};
        oSelectiveExport.sUrl = oSettingsRequestMapping.SelectiveExport;
        oPostRequest.exportType = "entity";
        oPostRequest.configCodes = [sSelectedRole];
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.PERMISSION;
        oSelectiveExport.oPostRequest = oPostRequest;
      PermissionStore.handleRolesPermissionsExportButtonClicked(oSelectiveExport);
    },

    /** Function to handle import of role permissions **/
    handleRolesPermissionsImportButtonClicked: function (sSelectedRole) {
      let oSelectiveExport = {};
      let oPostRequest = {};
      oSelectiveExport.sUrl = oSettingsRequestMapping.SelectiveExport;
      oPostRequest.exportType = "entity";
      oPostRequest.configCodes = [sSelectedRole];
      oPostRequest.configType = oDataModelImportExportEntityTypeConstants.PERMISSION;
      oSelectiveExport.oPostRequest = oPostRequest;
      PermissionStore.handleRolesPermissionsExportButtonClicked(oSelectiveExport);
    },

    resetTableViewProps: function (context) {
      PermissionStore.resetTableViewProps(context);
    },

    handleRuleRefreshMenuClicked: function () {
      var bRuleScreenLockStatus = RuleStore.getRuleScreenLockStatus();
      if (bRuleScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = RuleStore.getRuleDetails;

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            RuleStore.saveRule.bind(this, oCallbackData),
            RuleStore.discardUnsavedRule.bind(this, oCallbackData)).set({transition: 'fade'});
      }
      else {
        RuleStore.handleRuleRefreshMenuClicked();
        _triggerChange();
      }
    },

    handleRuleListRefreshMenuClicked: function () {
      var bRuleListScreenLockStatus = RuleListStore.getRuleListScreenLockStatus();
      if (bRuleListScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = RuleListStore.getRuleListDetails;

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            RuleListStore.saveRuleList.bind(this, {}),
            RuleListStore.discardUnsavedRuleList.bind(this, oCallbackData)).set({transition: 'fade'});
      }
      else {
        RuleListStore.handleRuleListRefreshMenuClicked();
        _triggerChange();
      }
    },

    handleProcessRefreshMenuClicked: function () {
      var bProcessScreenLockStatus = ProcessStore.getProcessScreenLockStatus();

      if (bProcessScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = ProcessStore.getProcessDetails;

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            ProcessStore.saveProcess.bind(this, oCallbackData),
            ProcessStore.discardUnsavedProcess.bind(this, oCallbackData)).set({transition: 'fade'});
      }
      else {
        ProcessStore.handleProcessRefreshMenuClicked();
        _triggerChange();
      }
    },

    handleDataGovernanceTasksRefreshMenuClicked: function () {
      var bTasksScreenLockStatus = DataGovernanceTasksStore.getDataGovernanceTasksScreenLockStatus();

      if (bTasksScreenLockStatus) {
        var oActiveTask = DataGovernanceTasksStore.getActiveDataGovernanceTask();
        var oCallbackData = {};
        oCallbackData.functionToExecute = DataGovernanceTasksStore.getDataGovernanceTaskDetails().bind(this, oActiveTask.id);

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            DataGovernanceTasksStore.saveDataGovernanceTask.bind(this, oCallbackData),
            DataGovernanceTasksStore.discardUnsavedDataGovernanceTask.bind(this, oCallbackData)).set({transition: 'fade'});
      }
      else {
        DataGovernanceTasksStore.handleDataGovernanceTasksRefreshMenuClicked();
        _triggerChange();
      }
    },

    handleRelationshipRefreshMenuClicked: function () {
      RelationshipStore.handleRelationshipRefreshMenuClicked();
      _triggerChange();
    },

    paperViewHeaderClicked: function (oModel) {
      ClassSettingStore.paperViewHeaderClicked(oModel);
    },

    fetchEntityChildrenWithGlobalPermissions: function (sItemId, sParentId) {
      PermissionStore.fetchEntityChildrenWithGlobalPermissions(sItemId, sParentId);
    },

    handlePermissionButtonToggled: function (sId, sProperty, sType, bForAllChildren) {
      PermissionStore.handlePermissionButtonToggled(sId, sProperty, sType, bForAllChildren);
    },

    handlePermissionSelectionToggled: function (sId, sType) {
      PermissionStore.handlePermissionSelectionToggled(sId, sType);
    },

    handlePermissionFirstLevelItemClicked: function (sId, sType) {
      PermissionStore.handlePermissionFirstLevelItemClicked(sId, sType);
    },

    handlePermissionKlassItemClicked: function (sId, sPermissionNodeId, sType) {
      PermissionStore.handlePermissionKlassItemClicked(sId, sPermissionNodeId, sType);
    },

    handleSetDefaultTemplateClicked: function (oDefaultTemplate) {
      PermissionStore.handleSetDefaultTemplateClicked(oDefaultTemplate);
    },

    handlePermissionRemoveTemplateClicked: function (sId) {
      PermissionStore.handlePermissionRemoveTemplateClicked(sId);
    },

    handleSwitchClassCategory: function (sClassCategory) {
      var bIsClassScreenLocked = ClassStore.getClassScreenLockedStatus();
      if (bIsClassScreenLocked) {
        var oSwitchCallback = {};
        oSwitchCallback.functionToExecute = _handleSwitchClassCategory.bind(this, sClassCategory);
        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            ClassStore.saveUnsavedClass.bind(this, oSwitchCallback, sClassCategory),
            ClassStore.discardUnsavedClass.bind(this, oSwitchCallback, sClassCategory),
            function () {
            });
      }
      else {
        return _handleSwitchClassCategory(sClassCategory);
      }
    },

    handleElementTagCheckAllChanged: function(oInfo){
      ClassStore.handleElementTagCheckAllChanged(oInfo);
    },

    handleElementFilterableOrSortableCheckAllChanged: function(oInfo){
      ClassStore.handleElementFilterableOrSortableCheckAllChanged(oInfo);
    },

    handleAvailableListViewLoadMoreClicked: function (sContext) {
      var oCallback = {
        functionToExecute: PropertyCollectionStore.updateClassSectionElementMap
      };
      _loadMoreFetchEntities([sContext], oCallback);
    },

    handleAvailableListViewSearched: function (sSearchText = "") {
      var oCallback = {
        functionToExecute: PropertyCollectionStore.updateClassSectionElementMap
      };
      let aEntityList = [SettingScreenProps.propertyCollectionConfigView.getPropertyCollectionDraggableListActiveTabId()];
      _handleSearchAndFetchEntitiesForPropertyCollection(aEntityList, sSearchText, oCallback);
    },

    handlePropertyCollapsedStatusToggled: function (sEntityType) {
      var oPropertyCollapsedStatusMap = SettingScreenProps.screen.getPropertyCollapsedStatusMap();
      oPropertyCollapsedStatusMap[sEntityType] = !oPropertyCollapsedStatusMap[sEntityType];
      _triggerChange();
    },

    handleTemplateHeaderVisibilityToggled: function (sType, sContext) {
      TemplateStore.handleTemplateHeaderVisibilityToggled(sType, sContext);
    },

    handleTemplateHeaderTabClicked: function (sTabType) {
      TemplateStore.handleTemplateHeaderTabClicked(sTabType);
    },

    handleTemplateHeaderTabIconChanged: function (sTabType, aFiles) {
      this.handleCommonConfigSectionViewIconChanged(sTabType, "template", aFiles);
    },

    handleTemplateSectionAdded: function (sSectionId) {
      TemplateStore.handleTemplateSectionAdded(sSectionId);
    },

    handlePermissionOfTemplateHeaderToggled: function (sType, sContext, sId) {
      PermissionStore.handlePermissionOfTemplateHeaderToggled(sType, sContext, sId);
    },

    handleTableExpandCollapsedClicked: function (sId) {
      PermissionStore.handleTableExpandCollapsedClicked(sId);
    },

    handleTableButtonClicked: function (sTableId, sRowId, sCellId, sScreenContext) {
      PermissionStore.handleTableButtonClicked(sTableId, sRowId, sCellId, sScreenContext);
    },

    handlePermissionSectionStatusChanged: function (sStatus, sSectionId, aElements) {
      PermissionStore.handlePermissionSectionStatusChanged(sStatus, sSectionId, aElements);
    },

    handlePermissionElementStatusChanged: function (sStatus, sSectionId, sElementType, sParentSectionId) {
      PermissionStore.handlePermissionElementStatusChanged(sStatus, sSectionId, sElementType, sParentSectionId);
    },

    handleRoleConfigPermissionDialogButtonClicked: function (sButtonId) {
      PermissionStore.handlePermissionDialogButtonClicked(sButtonId);
    },

    handleClassRelationshipModified: function (sRelationshipId, sContext, sNewValue, oReferencedKlasses) {
      ClassStore.handleClassRelationshipModified(sRelationshipId, sContext, sNewValue, oReferencedKlasses);
    },

    handleRuleNameChanged: function (sValue) {
      let oScreenProps = _getComponentProps().screen;
      let sContext = oScreenProps.getSelectedLeftNavigationTreeItem();
      switch (sContext){
        case 'rule':
          RuleStore.handleRuleNameChanged(sValue);
          break;

        case 'goldenRecords' :
          GoldenRecordsStore.handleRuleNameChanged(sValue);
          break;
      }

    },

    handleRuleElementDeleteButtonClicked: function (sElementId, sContext, sHandlerContext, sScreenContext) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = !CS.isEmpty(sScreenContext) ? sScreenContext.split(sSplitter) : [];

      if (aSplitContext[0] == "kpiConfigDetail") {
        let sRuleId = aSplitContext[1];
        KpiStore.handleRuleElementDeleteButtonClicked(sElementId, sContext, sHandlerContext, sRuleId);
      }else {
        RuleStore.handleRuleElementDeleteButtonClicked(sElementId, sContext, sHandlerContext);
      }
    },

    handleRuleAttributeValueChanged: function (sElementId, sValueId, sValue, sContext, sScreenContext) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = sScreenContext && sScreenContext.split(sSplitter);

      if (!CS.isEmpty(aSplitContext) && aSplitContext[0] == "kpiConfigDetail") {
        let sRuleId = aSplitContext[1];
        KpiStore.handleRuleAttributeValueChanged(sElementId, sValueId, [sValue], sContext, sScreenContext, sRuleId);
      } else {
        RuleStore.handleRuleAttributeValueChanged(sElementId, sValueId, [sValue], sContext, sScreenContext);
      }
    },

    handleRuleAttributeColorValueChanged: function (sElementId, sValueId, sValue) {
      RuleStore.handleRuleAttributeColorValueChanged(sElementId, sValueId, sValue);
    },

    handleRuleAttributeDescriptionValueChanged: function (sElementId, sValueId, sValue,sContext) {
      RuleStore.handleRuleAttributeDescriptionValueChanged(sElementId, sValueId, sValue,sContext);
    },

    handleRuleAttributeDescriptionValueChangedInNormalization: function (sAttrId, oAttrVal, sContext, sVal, sValAsHtml) {
      RuleStore.handleRuleAttributeDescriptionValueChangedInNormalization(sAttrId, oAttrVal, sContext, sVal, sValAsHtml);
    },

    handleRuleAttributeValueChangedInNormalization: function (sAttrId, oAttrVal, sContext, sVal) {
      RuleStore.handleRuleAttributeValueChangedInNormalization(sAttrId, oAttrVal, sContext, sVal);
    },

    handleRuleUserValueChanged: function (oExtraData, aNewValue, sScreenContext) {
      var sElementId = oExtraData.elementId;
      var sValueId = oExtraData.valueId;
      var sContext = oExtraData.context;
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = sScreenContext && sScreenContext.split(sSplitter);

      if(!CS.isEmpty(aSplitContext) && aSplitContext[0] == "kpiConfigDetail") {
        KpiStore.handleRuleAttributeValueChanged(sElementId, sValueId, aNewValue, sContext, false, aSplitContext[1]);
      }else {
        RuleStore.handleRuleAttributeValueChanged(sElementId, sValueId, aNewValue, sContext, sScreenContext);
      }
    },

    handleRoleCauseEffectMSSValueChanged: function (sContext, aNewValue, sScreenContext) {
      var oCallback = {};
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = sScreenContext && sScreenContext.split(sSplitter);

      if(!CS.isEmpty(aSplitContext) && aSplitContext[0] == "kpiConfigDetail") {
        oCallback.functionToExecute = KpiStore.handleRoleAdded.bind(this, aNewValue[0], sContext, aSplitContext[1]);
      }else{
        oCallback.functionToExecute = RuleStore.handleRoleAdded.bind(this, aNewValue[0], sContext);
      }
      RoleStore.getRoleDetailsById(aNewValue[0], oCallback);
    },

    handleAttributeCauseEffectMSSValueChanged: function (sContext, aNewValue, sScreenContext, oNewReferencedData) {
      var oCallback = {};
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = sScreenContext && sScreenContext.split(sSplitter);

      let oOldReferencedData = SettingUtils.getAppData().getAttributeList();
      let oReferencedData = CS.combine(oOldReferencedData, oNewReferencedData);
      SettingUtils.getAppData().setAttributeList(oReferencedData);

      if(!CS.isEmpty(aSplitContext) && aSplitContext[0] == "kpiConfigDetail") {
        oCallback.functionToExecute = KpiStore.handleAttributeAdded.bind(this, aNewValue[0], sContext, aSplitContext[1]);
      } else if (!CS.isEmpty(aSplitContext) && aSplitContext[0] == "smartDocument") {
        oCallback.functionToExecute = SmartDocumentStore.handleCommonConfigValueChanged.bind(this, 'attribute', aNewValue[0]);
      } else{
        oCallback.functionToExecute = RuleStore.handleAttributeAdded.bind(this, aNewValue[0], sContext);
      }
      AttributeStore.getAttributeDetailsById(aNewValue[0], oCallback);
    },

    handleTagCauseEffectMSSValueChanged: function (sContext, aNewValue, sScreenContext) {
      var oCallback = {};
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = sScreenContext && sScreenContext.split(sSplitter);

      if(!CS.isEmpty(aSplitContext) && aSplitContext[0] == "kpiConfigDetail") {
        oCallback.functionToExecute = KpiStore.handleTagAdded.bind(this, aNewValue[0], sContext, aSplitContext[1]);
      }else{
        oCallback.functionToExecute = RuleStore.handleTagAdded.bind(this, aNewValue[0], sContext);
      }
      TagStore.fetchTagDetailsById(aNewValue[0], oCallback);
    },

    handleRuleUserValueForNormalizationChanged: function (sRoleId, aUsers) {
      RuleStore.handleRuleAttributeValueChangedForNormalization(sRoleId, aUsers);
    },

    handleRuleDetailMssValueChanged: function (aNewValue, sKey) {
      GoldenRecordsStore.handleRuleDetailMssValueChanged(aNewValue, sKey);
    },

    handleEntitiesAddedInMergeSection: function (sContext, aSelectedEntities, sSelectedEntityType, sSelectedEntity, oReferencedData) {
      GoldenRecordsStore.handleEntitiesAddedInMergeSection(sContext, aSelectedEntities, sSelectedEntityType, sSelectedEntity, oReferencedData);
    },

    handleLatestEntityValueSelectionToggled: function (sContext, sSelectedEntity) {
      GoldenRecordsStore.handleLatestEntityValueSelectionToggled(sContext, sSelectedEntity);
    },

    handleGoldenRecordSelectedEntityRemoved: function (sContext, sEntityId) {
      GoldenRecordsStore.handleGoldenRecordSelectedEntityRemoved(sContext, sEntityId);
    },

    handleGoldenRecordSelectedSupplierRemoved: function (sSelectedSupplierId, sContext, sSelectedEntityId) {
      GoldenRecordsStore.handleGoldenRecordSelectedSupplierRemoved(sSelectedSupplierId, sContext, sSelectedEntityId);
    },

    handleRuleDetailSupplierSequenceChanged: function (aNewSupplierSequence, sContext, sEntityId) {
      GoldenRecordsStore.handleRuleDetailSupplierSequenceChanged(aNewSupplierSequence, sContext, sEntityId)
    },

    handleRuleRightPanelBarItemClicked: function (sIconContext) {
      RuleStore.handleRuleRightPanelBarItemClicked(sIconContext)
    },

    handleRuleRemoveBlackListIconClicked: function(sDropId){
      RuleStore.handleRuleRemoveBlackListIconClicked(sDropId);
    },

    handleAttributeVisibilityButtonClicked:function (sAttrId, oAttrCondition, sScreenContext){
      var oCallback = {};
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = !CS.isEmpty(sScreenContext) ? sScreenContext.split(sSplitter) : [];

      if(aSplitContext[0] == "kpiConfigDetail") {
        KpiStore.handleAttributeVisibilityButtonClicked(sAttrId, oAttrCondition, aSplitContext[1]);
      }else {
        RuleStore.handleAttributeVisibilityButtonClicked(sAttrId, oAttrCondition);
      }
    },

    handleCompareWithSystemDateButtonClicked:function (sContext, sRuleId, sAttrId, oAttrCondition){
      if (sContext == 'kpiConfigRules') {
        KpiStore.handleCompareWithSystemDateButtonClicked(sRuleId, sAttrId, oAttrCondition);
      } else {
        RuleStore.handleCompareWithSystemDateButtonClicked(sAttrId, oAttrCondition);
      }
    },

    handleAttributeViewTypeChanged:function (sAttrId, oAttributeCondition, sSelectedAttrId, oReferencedAttributes, oExtraData){
      var sSplitter = SettingUtils.getSplitter();
      let sScreenContext = oExtraData && oExtraData.screenContext || "";
      let sContext = oExtraData && oExtraData.context || "";
      var aSplitContext = !CS.isEmpty(sScreenContext) ? sScreenContext.split(sSplitter) : [];

      if(aSplitContext[0] == "kpiConfigDetail") {
        KpiStore.handleAttributeViewTypeChanged(sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId,aSplitContext[1], oReferencedAttributes);
      }else {
        let sRuleSectionContext = oExtraData.ruleSectionContext;
        switch (sRuleSectionContext) {
          case "ruleCause":
            RuleStore.handleAttributeViewTypeChanged(sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId);
            break;

          case "ruleEffect":
            RuleStore.handleAttributeLinkedInRuleEffectSection(sAttrId, oAttributeCondition, sContext, sScreenContext, sSelectedAttrId);
            break;
        }
      }
    },

    handleConcatenatedFormulaChanged: function(oNormalization, aAttributeConcatenatedList, oReferencedData) {
      RuleStore.handleConcatenatedFormulaChanged(oNormalization, aAttributeConcatenatedList, oReferencedData);
    },

    handleRuleAddAttributeClicked: function (sElementId, sContext, sScreenContext) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = !CS.isEmpty(sScreenContext) ? sScreenContext.split(sSplitter) : [];

      if(aSplitContext[0] == "kpiConfigDetail") {
        KpiStore.handleRuleAddAttributeClicked(sElementId, sContext, aSplitContext[1]);
      }else {
        RuleStore.handleRuleAddAttributeClicked(sElementId, sContext);
      }
    },

    handleRuleAttributeValueTypeChanged: function (sAttributeId, sValueId, sTypeId,sContext, sScreenContext, isCause) {
      var oCallback = {};
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = sScreenContext ? sScreenContext.split(sSplitter) : [];

      if(aSplitContext[0] == "kpiConfigDetail") {
        KpiStore.handleRuleAttributeValueTypeChanged(sAttributeId, sValueId, sTypeId, sContext, aSplitContext[1]);
      }else {
        RuleStore.handleRuleAttributeValueTypeChanged(sAttributeId, sValueId, sTypeId, sContext, isCause);
      }
    },

    handleRuleAttributeValueDeleteClicked: function (sAttributeId, sValueId,sContext, sScreenContext) {
      var oCallback = {};
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = !CS.isEmpty(sScreenContext) ? sScreenContext.split(sSplitter) : [];

      if(aSplitContext[0] == "kpiConfigDetail") {
        KpiStore.handleRuleAttributeValueDeleteClicked(sAttributeId, sValueId, sContext, aSplitContext[1]);
      }else {
        RuleStore.handleRuleAttributeValueDeleteClicked(sAttributeId, sValueId, sContext);
      }
    },

    handleRuleAttributeRangeValueChanged: function (sAttributeId, sValueId, sValue, sRange, sScreenContext) {
      let sSplitter = SettingUtils.getSplitter();
      let aSplitContext = !CS.isEmpty(sScreenContext) ? sScreenContext.split(sSplitter) : [];
      if (aSplitContext[0] == 'kpiConfigDetail') {
        KpiStore.handleRuleAttributeRangeValueChanged(sAttributeId, sValueId, sValue, sRange, aSplitContext[1]);
      } else {
        RuleStore.handleRuleAttributeRangeValueChanged(sAttributeId, sValueId, sValue, sRange);
      }
    },

    handleAttributeValueChangedForRangeInNormalization: function (sAttributeId, sValueId, sValue, sType, sRange) {
      RuleStore.handleAttributeValueChangedForRangeInNormalization(sAttributeId, sValueId, sValue, sType, sRange);
    },

    handleRuleDetailPartnerApplyClicked: function (aSelectedItems ) {
      let oScreenProps = _getComponentProps().screen;
      let sContext = oScreenProps.getSelectedLeftNavigationTreeItem();
      switch (sContext){
        case 'rule':
          RuleStore.handleRuleDetailPartnerApplyClicked(aSelectedItems);
          break;
        case 'goldenRecords':
          GoldenRecordsStore.handleRuleDetailPartnerApplyClicked(aSelectedItems);
          break;
      }

    },

    handleRuleDetailEndpointsChanged: function (aSelectedItems ) {
      let oScreenProps = _getComponentProps().screen;
      let sContext = oScreenProps.getSelectedLeftNavigationTreeItem();
      switch (sContext){
        case 'rule':
          RuleStore.handleRuleDetailEndpointsChanged(aSelectedItems);
          break;

        case 'goldenRecords':
          GoldenRecordsStore.handleRuleDetailEndpointsChanged(aSelectedItems);
          break;
      }

    },

    handleProcessComponentDataSourceValueChanged: function (sKey, sValue) {
      ProcessStore.handleProcessComponentDataSourceValueChanged(sKey, sValue);
    },

    handleProcessComponentClassInfoValueChanged: function (sKey, sValue, oReferencedData) {
      ProcessStore.handleProcessComponentClassInfoValueChanged(sKey, sValue, oReferencedData);
    },

    handleProcessComponentClassInfoTaxonomyValueChanged: function (sName, iIndex, sValue) {
      ProcessStore.handleProcessComponentClassInfoTaxonomyValueChanged(sName, iIndex, sValue);
    },

    handleProcessComponentClassInfoAddTaxonomy: function (sName) {
      ProcessStore.handleProcessComponentClassInfoAddTaxonomy(sName);
    },

    handleProcessComponentClassInfoRemoveTaxonomy: function (sName, iIndex) {
      ProcessStore.handleProcessComponentClassInfoRemoveTaxonomy(sName, iIndex);
    },

    handleProcessComponentClassInfoAddTagGroup: function (sName) {
      ProcessStore.handleProcessComponentClassInfoAddTagGroup(sName);
    },

    handleProcessComponentClassInfoRemoveTagGroup: function (sName, iIndex) {
      ProcessStore.handleProcessComponentClassInfoRemoveTagGroup(sName, iIndex);
    },

    handleProcessComponentClassInfoAddAttributeGroup: function (sName) {
      ProcessStore.handleProcessComponentClassInfoAddAttributeGroup(sName);
    },

    handleProcessComponentClassInfoRemoveAttributeGroup: function (sName, iIndex) {
      ProcessStore.handleProcessComponentClassInfoRemoveAttributeGroup(sName, iIndex);
    },

    handleProcessDesignChanged: function (xml) {
      ProcessStore.handleProcessDesignChanged(xml);
    },

    handleSetProcessDefinitions: function (oModeler) {
      ProcessStore.handleSetProcessDefinitions(oModeler);
    },

    handleBPMNUploadDialogStatusChanged : function (bStatus) {
      ProcessStore.handleBPMNUploadDialogStatusChanged(bStatus);
    },

    handleBPMNXMLUpload: function (sXML) {
      ProcessStore.handleBPMNXMLUpload(sXML);
    },

    handleProcessDialogButtonClicked: function (sButtonId) {
      switch (sButtonId) {
        case "process_creation_create":
          ProcessStore.createProcess(true);
          break;
        case "process_creation_discard":
          ProcessStore.cancelProcessCreation();
          break;
        case "detailed_process_discard":
          ProcessStore.discardUnsavedProcess({});
          break;
        case "detailed_process_save":
          ProcessStore.saveProcess({});
          break;
        case "detailed_process_close":
          ProcessStore.closeProcessDialog();
          break;
        case "process_saveAs_discard":
          ProcessStore.handleSaveAsDialogCloseButtonClicked();
          break;
        case "process_saveAs_save":
          ProcessStore.handleSaveAsDialogSaveButtonClicked();
          break;
      }
    },

    handleMappingSaveAsDialogButtonClicked: function(sButtonId){
      switch (sButtonId) {
        case "mapping_saveAs_discard":
          MappingStore.handleSaveAsDialogCloseButtonClicked(true);
          break;
        case "mapping_saveAs_save":
          MappingStore.handleSaveAsDialogSaveButtonClicked();
          break;
      }
    },

    handleSaveAsDialogValueChanged: function (sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData, sSubType) {
      let oScreenProps = _getComponentProps().screen;
      let sContext = oScreenProps.getSelectedLeftNavigationTreeItem();
      switch (sContext) {
        case "process" :
          ProcessStore.handleSaveAsDialogValueChanged(sContentId, sPropertyId, sValue);
          break;

        case "mapping" :
          MappingStore.handleSaveAsDialogValueChanged(sContentId, sPropertyId, sValue);
          break;

        case "profile" :
          ProfileStore.handleSaveAsDialogValueChanged(sContentId, sPropertyId, sValue, sSubType);
          break;
      }
    },

    handleWorkflowTypeClicked : function(aSelectedItem){
      ProcessStore.handleWorkflowTypeClicked(aSelectedItem);
    },

    handleEntityTypeClicked : function(aSelectedItem){
      ProcessStore.handleEntityTypeClicked(aSelectedItem);
    },

    handleEndpointDialogButtonClicked: function (sButtonId) {
      switch (sButtonId) {
        case "endpoint_creation_create":
          ProfileStore.handleProfileConfigDialogButtonClicked(sButtonId);
          break;
        case "endpoint_creation_discard":
          ProfileStore.handleProfileConfigDialogButtonClicked(sButtonId);
          break;
        case "detailed_endpoint_discard":
          ProfileStore.discardUnsavedEndpoint({});
          break;
        case "detailed_endpoint_save":
          ProfileStore.saveEndpoint({});
          break;
        case "detailed_endpoint_close":
          ProfileStore.closeEndpointDialog();
          break;
        case "endpoint_saveAs_discard":
          ProfileStore.handleSaveAsDialogCloseButtonClicked();
          break;
        case "endpoint_saveAs_save":
          ProfileStore.handleSaveAsDialogSaveButtonClicked();
          break;
      }
    },

    handleMenuListExpandToggled: function (sItemId, sContext) {
      if (sContext === 'permissionConfig') {
        PermissionStore.handleMenuListExpandToggled(sItemId);
        return;
      }
      let oLeftNavigationTreeValuesMap = SettingScreenProps.screen.getLeftNavigationTreeValuesMap();
      let oSelectedTreeValues = oLeftNavigationTreeValuesMap[sItemId];
      oSelectedTreeValues.isExpanded = !oSelectedTreeValues.isExpanded;
      _triggerChange();
    },

    handleMenuListExpandToggledNew: function (sItemId, sContext) {

      let oLeftNavigationTreeValuesMap = SettingScreenProps.screen.getLeftNavigationTreeValuesMap();
      let oSelectedTreeValues = oLeftNavigationTreeValuesMap[sItemId];
      oSelectedTreeValues.isExpanded = true;
      _triggerChange();
    },

    handleAddNewBlackListItem: function (sValue, sContext) {
      var sSplitter = SettingUtils.getSplitter();
      var aContexts = sContext.split(sSplitter);
      sContext = aContexts[0];
      switch (sContext) {
        case 'rule':
          //TODO:
          RuleStore.handleAddNewBlackListItem(sValue);
          break;
        case 'ruleList':
          RuleListStore.handleAddNewBlackListItem(sValue);
          break;
        default:
          break;
      }
    },

    handleRemoveBlackListItem: function (iIndex, sContext) {
      var sSplitter = SettingUtils.getSplitter();
      var aContexts = sContext.split(sSplitter);
      sContext = aContexts[0];
      switch (sContext) {
        case 'rule':
          //TODO:
          RuleStore.handleRemoveBlackListItem(iIndex);
          break;
        case 'ruleList':
          RuleListStore.handleRemoveBlackListItem(iIndex);
          break;
        default:
          break;
      }
    },

    handleEditBlackListItem: function (iIndex, sValue, sContext) {
      switch (sContext) {
        case 'rule':
          RuleStore.handleEditBlackListItem(iIndex, sValue);
          break;
        case 'ruleList':
          RuleListStore.handleEditBlackListItem(iIndex, sValue);
          break;
        default:
          break;
      }
    },

    handleRuleListLabelChanged: function (sNewValue) {
      RuleListStore.handleRuleListLabelChanged(sNewValue);
    },

    handleTaxonomyLevelItemClicked: function (iIndex, sItemId, sContext) {
      if (sContext == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        AttributionTaxonomyStore.handleAttributionTaxonomyLevelItemClicked(iIndex, sItemId);
      }
    },

    handleDetailedTaxonomyDialogClose: function (sContext) {
      AttributionTaxonomyStore.handleDetailedTaxonomyDialogClose(sContext);
    },

    handleAttributionTaxonomyHierarchyToggleAutomaticScrollProp : function () {
      AttributionTaxonomyStore.toggleAttributionHierarchyScrollEnableDisableProp();
    },

    handleTaxonomyLevelItemLabelChanged: function (iIndex, sItemId, sLabel, sContext) {
      if (sContext == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        AttributionTaxonomyStore.handleTaxonomyLevelItemLabelChanged(iIndex, sItemId, sLabel);
      }
    },

    handleTaxonomyLevelActionItemClicked: function (iIndex, sItemId, sContext) {
      if (sContext == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        AttributionTaxonomyStore.handleAttributionTaxonomyLevelActionItemClicked(iIndex, sItemId)
      }
    },

    handleTaxonomyLevelChildActionItemClicked: function (iIndex, sActionId, sChildId, sContext) {
      if (sContext == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        AttributionTaxonomyStore.handleAttributionTaxonomyLevelChildActionItemClicked(iIndex, sActionId, sChildId);
      }
    },

    handleTaxonomyLevelMasterListChildrenAdded: function (iIndex, aCheckedItems, sContext, sNewLabel, oReferencedData) {
      if (sContext == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        AttributionTaxonomyStore.handleTaxonomyLevelMasterListChildrenAdded(iIndex, aCheckedItems, sNewLabel, oReferencedData);
      }
    },

    handleTaxonomyAddChildButtonClicked: function (bPopoverVisible, sTagGroupId, sTaxonomyId, sContext) {
      if (sContext == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
        AttributionTaxonomyStore.handleTaxonomyAddChildButtonClicked(bPopoverVisible, sTagGroupId);
      }
    },

    handleGridPropertyValueChanged: function (sContext, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData) {
      _handleGridPropertyValueChanged(sContext, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData);
    },

    handleGridPropertyKeyDownEvent: function (sKey, sContentId, sPathToRoot, sGridContext) {
      _handleGridPropertyKeyDownEvent(sKey, sContentId, sPathToRoot, sGridContext);
    },

    handleGridParentExpandToggled: function (sContentId, sGridContext) {
      _handleGridParentExpandToggled(sContentId, sGridContext);
    },

    handleGridPropertyClearShouldFocus: function (sContentId, sPathToRoot, sGridContext) {
      _handleGridPropertyClearShouldFocus(sContentId, sPathToRoot, sGridContext);
    },

    handleGridImagePropertyImageChanged: function (sContentId, sPropertyId, aFiles, sPathToRoot, bLimitImageSize, sContext) {
      _handleGridImagePropertyImageChanged(sContentId, sPropertyId, aFiles, sPathToRoot, bLimitImageSize, sContext);
    },

    handleGridMSSAdditionalListItemAdded: function (sContentId, aCheckedItems, sContext, oReferencedData, sGridContext) {
      _handleGridMSSAdditionalListItemAdded(sContentId, aCheckedItems, sContext, oReferencedData, sGridContext);
    },

    handleGridViewSelectButtonClicked: function (aSelectedContentIds, bSelectAllClicked, sContext) {
      _handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked, sContext);
    },

    handleGetAssetExtensions: function (oRefDom, sContentId, sPropertyId, sContext, sPathToRoot) {
      _handleGetAssetExtensions(oRefDom, sContentId, sPropertyId, sContext, sPathToRoot);
    },

    handleGridViewActionItemClicked: function (sActionItemId, sContentId, sContext) {
      _handleGridViewActionItemClicked(sActionItemId, sContentId, sContext);
    },

    handleGridViewColumnActionItemClicked: function (sActionItemId, sColumnId) {
      _handleGridViewColumnActionItemClicked(sActionItemId, sColumnId);
    },

    handleGridViewDeleteButtonClicked: function (sContext) {
      _handleGridViewDeleteButtonClicked([] ,sContext);
    },

    handleManageEntityMultiUsageButtonClicked:function(sGridContext) {
      _handleManageEntityMultiUsageButtonClicked(sGridContext);
    },

    handleGridViewCreateButtonClicked: function (sContext) {
      _handleGridViewCreateButtonClicked("", sContext);
    },

    handleGridViewSortButtonClicked: function (sContext) {
      _handleGridViewSortButtonClicked("", sContext);
    },

    handleGridViewExportButtonClicked: function (sGridContext) {
      _handleGridViewExportButtonClicked(sGridContext);
    },

    handleGridViewDownloadButtonClicked: function (sGridContext) {
      _handleGridViewDownloadButtonClicked("", sGridContext);
    },

    handleGridViewFilterButtonClicked: function (bShowFilterView, sGridContext) {
      _handleGridViewFilterButtonClicked(bShowFilterView, sGridContext);
    },

    handleGridViewResizerButtonClicked: function (iWidth, sId, sContext) {
      _handleGridViewResizerButtonClicked(iWidth, sId, sContext);
    },

    handleGridViewImportButtonClicked: function (aFiles, sContext) {
      _handleGridViewImportButtonClicked(aFiles, sContext);
    },

    handleGridFilterApplyClicked: function (oAppliedFilter, oReferencedData) {
      _handleGridFilterApplyClicked(oAppliedFilter, oReferencedData);
    },

    handleGridViewShowExportStatusButtonClicked: function(sContext) {
      _handleGridViewShowExportStatusButtonClicked(sContext);
    },

    handleGridOrganizeColumnButtonClicked: function (sGridContext) {
      _handleGridOrganizeColumnButtonClicked(sGridContext);
      _triggerChange();
    },

    handleAttributionTaxonomyImportButtonClicked: function (aFiles) {
      let oImportExcel = {};
      oImportExcel.sUrl = oSettingsRequestMapping.ImportExcel;
      oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.MASTERTAXONOMY;
      AttributionTaxonomyStore.handleAttributionTaxonomyFileUploaded(aFiles,oImportExcel);
    },

    handleAttributionTaxonomyTabChanged: function (sTabId) {
      AttributionTaxonomyStore.handleAttributionTaxonomyTabChanged(sTabId);
    },

    handleAttributionTaxonomyExportButtonClicked: function () {
      if (this.settingScreenSafetyCheck()) {
        let oSelectiveExport = {};
        let oPostRequest = {};
        oSelectiveExport.sUrl = oSettingsRequestMapping.SelectiveExport;
        oPostRequest.exportType = "entity";
        oPostRequest.configCodes = [];
        oPostRequest.configType = oDataModelImportExportEntityTypeConstants.MASTERTAXONOMY;
        oSelectiveExport.oPostRequest = oPostRequest;
        AttributionTaxonomyStore.handleExportAttributionTaxonomy(oSelectiveExport);
      }
    },

    handleRefreshAttributionTaxonomyConfig: function () {
      AttributionTaxonomyStore.handleRefreshAttributionTaxonomyConfig();
    },

    handleGridViewRefreshButtonClicked: function (sContext) {
      _handleGridViewRefreshButtonClicked(sContext);
    },

    handleSsoRefreshButtonClicked :function (){
          SsoSettingStore.fetchSSOListForGridView();
      },

    handleGridViewSaveAsButtonClicked: function (sContext) {
      _handleGridViewSaveAsButtonClicked("", sContext);
    },

    handleTagViewDataLanguageClicked: function (aSelectedItemId) {
      _handleTagViewDataLanguageClicked(aSelectedItemId);
    },

    handleGridPaginationChanged: function (oNewPaginationData, sContext) {
      _handleGridPaginationChanged(oNewPaginationData, sContext);
    },

    handleGridViewSearchTextChanged: function (sSearchText, sContext) {
      _handleGridViewSearchTextChanged(sSearchText, sContext);
    },

    handleGridViewSaveButtonClicked: function (sContext) {
      _handleGridViewSaveButtonClicked({}, sContext);
    },

    handleGridViewDiscardButtonClicked: function (sContext) {
      _handleGridViewDiscardButtonClicked({}, sContext);
    },

    handleGridViewColumnHeaderClicked: function (sColumnId, sContext) {
      _handleGridViewColumnHeaderClicked(sColumnId, sContext);
    },

    handleNewMSSViewPopOverClosed: function (aSelectedItems, sContext, oReferencedData) {
      _handleNewMSSViewPopOverClosed(aSelectedItems, sContext, oReferencedData);
    },

    handleYesNoViewToggled : function (bValue, oChildTagGroupModel) {
      _handleYesNoViewToggled(bValue, oChildTagGroupModel);
    },

    handleDataTransferAddProperties: function (sEntity, aSelectedIds, oReferencedData, sContext) {
      _handleDataTransferAddProperties(sEntity, aSelectedIds, oReferencedData, sContext);
    },

    handleDataTransferPropertyCouplingChange: function (sSide, sPropertyId, sNewValue, sContext, sRelationshipId, sParentContext) {
      _handleDataTransferPropertyCouplingChange(sSide, sPropertyId, sNewValue, sContext, sRelationshipId, sParentContext);
    },

    handleDataTransferSearchLoadMore: function (oData) {
      if (oData.loadMore) {
        _loadMoreFetchEntities(oData.entities);
      } else {
        _handleSearchAndFetchEntities(oData.entities, oData.searchText || "");
      }
    },

    handleDataTransferPropertyRemove: function (sSide, sPropertyId, sContext, sRelationshipId, sParentContext) {
      CustomActionDialogStore.showConfirmDialog(oTranslations().REMOVE_CONFIRMATION, "",
          function () {
            _handleDataTransferPropertyRemove(sSide, sPropertyId, sContext, sRelationshipId, sParentContext);
          }, function (oEvent) {
          });
    },

    handleContextualTagCombinationUniqueSelectionChanged:  function (sUniqueSelectionId, sContext) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = CS.split(sContext, sSplitter);
      var sMainContext = aSplitContext[0];

      switch (sMainContext) {
        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case "technicalImageContext":
          ClassStore.handleClassContextCombinationEditClicked(sUniqueSelectionId);
          break;

        case 'context_variant':
          ContextStore.handleContextualTagCombinationUniqueSelectionChanged(sUniqueSelectionId);
          break;
      }
    },

    handleContextualTagCombinationUniqueSelectionDelete:  function (sUniqueSelectionId, sContext) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = CS.split(sContext, sSplitter);
      var sMainContext = aSplitContext[0];

      switch (sMainContext) {
        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case "technicalImageContext":
          ClassStore.handleDeleteNewTagCombination(sUniqueSelectionId);
          break;

        case 'context_variant':
          ContextStore.handleContextualTagCombinationUniqueSelectionDelete(sUniqueSelectionId);
          break;
      }
   },

    handleContextualTagCombinationUniqueSelectionOk: function (sUniqueSelectionId, sContext) {
      var sSplitter = SettingUtils.getSplitter();
      var aSplitContext = CS.split(sContext, sSplitter);
      var sMainContext = aSplitContext[0];

      switch (sMainContext) {
        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case "technicalImageContext":
          ClassStore.handleTagCombinationSelected(sUniqueSelectionId);
          break;

        case 'context_variant':
          ContextStore.handleContextualTagCombinationUniqueSelectionOk(sUniqueSelectionId);
          break;
      }
    },

    handleTagValueListApplyButtonClicked: function(sContextKey, aSelectedItemIds){
      var sSplitter = SettingUtils.getSplitter();
      var aOldContextKey = sContextKey.split(sSplitter);
      sContextKey = (aOldContextKey[1] === "context") ? aOldContextKey[1] : aOldContextKey[0];

      switch (sContextKey) {

        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case "technicalImageContext":
          ClassStore.handleClassContextDialogAddOrRemoveTagValue(aOldContextKey[1], aSelectedItemIds);
          break;

        case "context":
          ContextStore.handleContextAddOrRemoveTagValue(aOldContextKey[2], aSelectedItemIds);
          break;

        case 'kpiConfigDetail':
          KpiStore.updateKPITagValuesSelection(aOldContextKey[1], aSelectedItemIds);
          break;

      }

    },

    handleTagListApplyButtonClicked: function (sContext, aSelectedTagItems) {
      let sSplitter = SettingUtils.getSplitter();
      let aSplitContext = CS.split(sContext, sSplitter);
      sContext = (aSplitContext[1] === "context") ? aSplitContext[1] : aSplitContext[0];

      switch (sContext) {

        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case "technicalImageContext":
          //only single item is passed coz, inside the function for every node, there is a network call sent to fetch the tag details.
          ClassStore.handleClassContextDialogAddTagGroup(aSelectedTagItems[0],sContext);
          break;
        case 'context':
          ContextStore.handleContextAddTagGroup(aSelectedTagItems[0]);
          break;

        case 'kpiConfigDetail':
          KpiStore.handleKpiConfigDialogTagGroupSelected(aSelectedTagItems[0]);
          break;

      }
    },

    handleTagListLoadMoreClicked: function (sContext) {

      switch (sContext) {

        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case 'kpiConfigDetail':
        case "technicalImageContext":
          var aEntityList = [ConfigDataEntitiesDictionary.TAGS];
          _fetchEntities(aEntityList, true, {});
          break;

      }
    },

    handleTagListSearchTextClicked: function (sContext, sSearchText) {

      switch (sContext) {

        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case 'kpiConfigDetail':
        case "technicalImageContext":
          var aEntityList = [ConfigDataEntitiesDictionary.TAGS];
          _handleSearchAndFetchEntities(aEntityList, sSearchText, {});
          break;

      }
    },

    handleRemoveSelectedTagButtonClicked: function (sContext, sTagGroupId) {
      let sSplitter = SettingUtils.getSplitter();
      let aSplitContext = CS.split(sContext, sSplitter);
      sContext = (aSplitContext[1] === "context") ? aSplitContext[1] : aSplitContext[0];

      switch (sContext) {

        case 'languageKlassContext':
        case 'embeddedKlassContext':
        case "technicalImageContext":
          ClassStore.handleRemoveSelectedTagGroupClicked(sTagGroupId);
          break;

        case "context":
          ContextStore.handleRemoveSelectedTagGroupClicked(sTagGroupId);
          break;

        case 'kpiConfigDetail':
          KpiStore.handleRemoveSelectedTagGroupClicked(sTagGroupId);
          break;

      }
    },

    loadInitialEntityData: function (sContext) {
      _loadInitialEntityData(sContext);
    },

    loadMoreFetchEntities: function (aEntityList, oCallback) {
      _loadMoreFetchEntities(aEntityList, oCallback);
    },

    handleRuleDetailsKlassTypeMSSChanged: function (aNewKlassType) {
      _handleRuleDetailsKlassTypeMSSChanged(aNewKlassType);
    },

    handleRuleConfigNatureClassAddedInGoldenRecord: function (aSelectedIds) {
      GoldenRecordsStore.handleRuleConfigNatureClassAddedInGoldenRecord(aSelectedIds);
    },

    handleRuleDetailsKlassMSSChanged: function (sContext, aSelectedKlasses) {
      _handleRuleDetailsKlassMSSChanged(sContext, aSelectedKlasses);
    },

    handleRuleDetailsMSSSearchTextChanged: function (sContext, sSearchText) {
      _handleSearchAndFetchEntities([sContext], sSearchText);
    },

    handleProcessAndSetNewCustomElement: function (oNewElement,oCustomDefinition, bpmnFactory, modeling) {
      ProcessStore.handleProcessAndSetNewCustomElement(oNewElement,oCustomDefinition, bpmnFactory, modeling);
    },

    handleProcessAndSetNewElement: function (oNewElement) {
      ProcessStore.handleProcessAndSetNewElement(oNewElement);
    },

    handleSetActiveBPMNInstances: function (oNewElement, oBPMNFactory, oModelling) {
      ProcessStore.handleSetActiveBPMNInstances(oNewElement, oBPMNFactory, oModelling);
    },

    handleBPMNCustomTabsUpdate: function () {
      ProcessStore.handleBPMNCustomTabsUpdate();
    },

    handleBPMNCustomElementTextChanged: function (sName, sSelectedAttributeId,sNewVal) {
      ProcessStore.handleBPMNCustomElementTextChanged(sName, sSelectedAttributeId,sNewVal);
    },

    handleBPMNCustomElementMSSChanged: function (sName, aSelectedItems, oReferencedData) {
      ProcessStore.handleBPMNCustomElementMSSChanged(sName, aSelectedItems, oReferencedData);
    },

    handleMSSValueClicked: function(){
      ProcessStore.handleMSSValueClicked();
    },

    handleBPMNElementsDataLanguageMSSChanged: function(sName, aSelectedItems, oReferencedData){
      ProcessStore.handleBPMNElementsDataLanguageMSSChanged(sName, aSelectedItems, oReferencedData);
    },

    handleBPMNElementsGroupMSSChanged: function(sContext, sAction, aSelectedRoles){
      ProcessStore.handleBPMNElementsGroupMSSChanged(sContext, sAction, aSelectedRoles);
    },

    handleBPMNElementsVariableMapChanged : function(sName, iIndex, sType, sValue){
      ProcessStore.handleBPMNElementsVariableMapChanged(sName, iIndex, sType, sValue);
    },

    handleBPMNElementsAddVariableMap : function(sName){
      ProcessStore.handleBPMNElementsAddVariableMap(sName);
    },

    handleBPMNElementsRemoveVariableMap : function(sName, iIndex){
      ProcessStore.handleBPMNElementsRemoveVariableMap(sName, iIndex);
    },

    handleBPMNPropertiesTagMSSChanged :function (sName, aSelectedItems, oReferencedData) {
      ProcessStore.handleBPMNPropertiesTagMSSChanged(sName, aSelectedItems, oReferencedData);
    },

    handleBPMNPropertiesTagMSSChangedCustom :function (sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
      ProcessStore.handleBPMNPropertiesTagMSSChangedCustom(sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData);
    },

    handleBPMNPropertiesAttributeMSSChangedCustom :function (sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
      ProcessStore.handleBPMNPropertiesAttributeMSSChangedCustom(sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData);
    },

    handleBPMNPropertiesAttributeDateChangedCustom: function (sName, sSelectedAttributeId, sNewVal) {
      ProcessStore.handleBPMNPropertiesAttributeDateChangedCustom(sName, sSelectedAttributeId, sNewVal);
    },

    handleSearchFilterEditButtonClicked: function (sName) {
      _prepareDataForAttributesAndTagsDialog();
      ProcessStore.handleSearchFilterEditButtonClicked(sName);
    },

    handleGetAllowedAttributesForCalculatedAttribute: function (oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, bIsForReload) {
      _handleGetAllowedAttributesForCalculatedAttribute(oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, bIsForReload);
    },

    handleClassContextDialogClosed: function(){
      ClassStore.handleClassContextDialogClosed();
    },

    handleClassContextDialogOkClicked: function () {
      ClassStore.handleClassContextDialogOkClicked();
    },

    handleClassContextDialogDiscardClicked: function () {
      ClassStore.handleClassContextDialogDiscardClicked();
    },

    handleClassContextDialogOpenClicked: function(){
      ClassStore.handleClassContextDialogOpenClicked();
    },

    handleAttributionTaxonomyCreateDialogButtonClicked: function(sContext, sIsMajorMinor, sLabel){
      AttributionTaxonomyStore.handleAttributionTaxonomyCreateDialogButtonClicked(sContext, sIsMajorMinor, sLabel);
    },

    handleAttributionTaxonomyListLevelAdded: function(sSelectedId, bIsNewlyCreated, sLabel){
      AttributionTaxonomyStore.handleAttributionTaxonomyListLevelAdded(sSelectedId, bIsNewlyCreated, sLabel);
    },

    handleAttributionTaxonomyListLevelAddButtonClicked: function(bPopoverStatus){
      AttributionTaxonomyStore.toggleAddLevelPopoverVisibility(bPopoverStatus);
      if (bPopoverStatus) {
        TagStore.fetchMasterTypeTags();
      } else {
        _triggerChange();
      }
    },

  //Kpi Config
    handleKPIConfigDialogButtonClicked: function (sButtonId) {
      KpiStore.handleKPIConfigDialogButtonClicked(sButtonId);
    },

    handleKPIConfigDialogTabChanged: function (sTabId) {
      KpiStore.handleKPIConfigDialogTabChanged(sTabId);
    },

    //Context Config
    handleContextConfigDialogButtonClicked: function (sButtonId) {
      ContextStore.handleContextConfigDialogButtonClicked(sButtonId);
    },

    handleAddTagCombinationsClicked: function () {
      ContextStore.handleAddTagCombinationsClicked();
    },

    handleMappingConfigDialogButtonClicked: function (sButtonId) {
      MappingStore.handleMappingConfigDialogButtonClicked(sButtonId);
    },

    handleAuthorizationMappingConfigDialogButtonClicked: function (sButtonId) {
      AuthorizationStore.handleAuthorizationMappingConfigDialogButtonClicked(sButtonId);
    },

    handleAuthorizationMappingApplyButtonClicked: function (sContext, aSelectedItems, oReferencedData) {
      AuthorizationStore.handleAuthorizationMappingApplyButtonClicked(sContext, aSelectedItems, oReferencedData);
    },

    handleAuthorizationMappingCheckboxButtonClicked: function (bIsCheckboxClicked, sContext) {
      AuthorizationStore.handleAuthorizationMappingCheckboxButtonClicked(bIsCheckboxClicked, sContext);
    },

    handleAuthorizationMappingDeleteButtonClicked: function (sSelectedElement, sContext) {
      AuthorizationStore.handleAuthorizationMappingDeleteButtonClicked(sSelectedElement, sContext);
    },

    handleAuthorizationMappingToggleButtonClicked: function (bIsActivationEnable, oElementData) {
      AuthorizationStore.handleAuthorizationMappingToggleButtonClicked(bIsActivationEnable, oElementData);
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId , sOtherTaxonomyId) {
      let oScreenProps = _getComponentProps().screen;
      let sSelectedLeftTreeItem = oScreenProps.getSelectedLeftNavigationTreeItem();
      if (sSelectedLeftTreeItem === "rule") {
        RuleStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
      } else if (sSelectedLeftTreeItem == "goldenRecords") {
        GoldenRecordsStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
      } else if (sSelectedLeftTreeItem == ViewContextConstants.ORGANISATION_CONFIG_CONTEXT) {
        OrganisationConfigStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
      } else if (sSelectedLeftTreeItem == "keyperformanceindex") {
        KpiStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
      } else if (sSelectedLeftTreeItem == "process" && sOtherTaxonomyId == "processComponentTaxonomy") {
        ProcessStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId, sOtherTaxonomyId);
      } else if (sSelectedLeftTreeItem == "process") {
        ProcessStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
      } else if (sSelectedLeftTreeItem == "smartdocument") {
        SmartDocumentStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
      } else if (sSelectedLeftTreeItem == "profile") {
        ProfileStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
      }
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sParentTaxonomyId, sViewContext) {
      let oScreenProps = _getComponentProps().screen;
      let sActiveLeftTreeNode = oScreenProps.getSelectedLeftNavigationTreeItem();
      if (sActiveLeftTreeNode == ViewContextConstants.ORGANISATION_CONFIG_CONTEXT) {
        if (sViewContext && sViewContext == "roles") {
          RoleStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId);
        } else {
          OrganisationConfigStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId);
        }
      } else if (sActiveLeftTreeNode == "process") {
        ProcessStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sViewContext);
      } else if (sActiveLeftTreeNode == "profile") {
        ProfileStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sViewContext);
      } else if (sActiveLeftTreeNode == "smartdocument") {
        SmartDocumentStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sViewContext);
      } else {
        //let sContext = oContext.props.context;
        if (sActiveLeftTreeNode == "goldenRecords") {
          GoldenRecordsStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sViewContext);
        } else if (sViewContext == ViewContextConstants.DATA_RULES_CONTEXT || sViewContext == ViewContextConstants.DATA_RULES_EFFECT_CONTEXT) {
          RuleStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sViewContext);
        } else if (sViewContext === 'kpiConfigDetail') {
          KpiStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId);
        }
      }
    },

    // TODO: handle routing through oContext.props
    handleAddTaxonomyPopoverItemClicked: function (oModel, sTaxonomyId) {
      var sId = oModel.id;
      let oScreenProps = _getComponentProps().screen;
      let sActiveLeftTreeItem = oScreenProps.getSelectedLeftNavigationTreeItem();
      let sViewContext = oModel.properties['viewContext'];
      if (sActiveLeftTreeItem == ViewContextConstants.ORGANISATION_CONFIG_CONTEXT) {
        //  let sViewContext = oModel.properties['viewContext'];
        if (sViewContext && sViewContext == "roles") {
          RoleStore.handleTaxonomyAdded(sId, sTaxonomyId);
        } else {
          OrganisationConfigStore.handleTaxonomyAdded(sId, sTaxonomyId);
        }
      } else if (sActiveLeftTreeItem == 'goldenRecords') {
        GoldenRecordsStore.handleTaxonomyAdded(sId, sTaxonomyId, sViewContext);
      } else if (sViewContext == ViewContextConstants.DATA_RULES_CONTEXT || sViewContext == ViewContextConstants.DATA_RULES_EFFECT_CONTEXT) {
        RuleStore.handleTaxonomyAdded(sId, sTaxonomyId, sViewContext);
      } else if (sActiveLeftTreeItem == 'process') {
        ProcessStore.handleTaxonomyAdded(oModel, sId, sTaxonomyId, sViewContext);
      } else if (sActiveLeftTreeItem == 'smartdocument') {
        SmartDocumentStore.handleTaxonomyAdded(oModel, sTaxonomyId, sViewContext);
      } else if (sActiveLeftTreeItem == 'profile') {
        ProfileStore.handleTaxonomyAdded(oModel, sTaxonomyId, sViewContext);
      }
      else {
        KpiStore.handleTaxonomyAdded(sId, sTaxonomyId);
      }
    },

    handleAddTaxonomySearch: function (sTaxonomyType, sSearchText, sTaxonomyId, sContext) {
      switch (sContext) {
        case"roles":
          RoleStore.handleMssSearchClicked("taxonomies", sSearchText, sTaxonomyId);
          break;
        case ViewContextConstants.ORGANISATION_CONFIG_CONTEXT:
          OrganisationConfigStore.handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
          break;
        case ViewContextConstants.KPI:
          KpiStore.handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
          break;
        case ViewContextConstants.DATA_RULES_CONTEXT:
        case ViewContextConstants.DATA_RULES_EFFECT_CONTEXT:
          RuleStore.handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
          break;
        case ViewContextConstants.PROCESS_CONFIG:
          ProcessStore.handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
          break;
        case "processComponentTaxonomy":
          ProcessStore.handleBPMNTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
          break;
        case "smartDocument":
          SmartDocumentStore.handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
          break;
        case ViewContextConstants.PROFILE:
          ProfileStore.handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
          break;
      }
    },

    handleAddTaxonomyLoadMore: function (sTaxonomyType, sTaxonomyId, sContext) {
      switch (sContext) {
        case"roles":
          RoleStore.fetchRoleEntities("taxonomies", true, sTaxonomyId);
          break;
        case ViewContextConstants.ORGANISATION_CONFIG_CONTEXT:
          OrganisationConfigStore.handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
          break;
        case ViewContextConstants.KPI:
          KpiStore.handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
          break;
        case ViewContextConstants.DATA_RULES_CONTEXT:
        case ViewContextConstants.DATA_RULES_EFFECT_CONTEXT:
           RuleStore.handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
           break;
        case ViewContextConstants.PROCESS_CONFIG:
          ProcessStore.handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
          break;
        case "processComponentTaxonomy":
          ProcessStore.handleBPMNTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
          break;
        case "smartDocument":
          SmartDocumentStore.handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
          break;
        case ViewContextConstants.PROFILE:
          ProfileStore.handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
          break;
      }
    },

    handleKPIMSSValueChanged: function (sRuleId, sKey, aSelectedItems, oReferencedData) {
      KpiStore.handleKPIRuleMSSValueChanged(sRuleId, sKey, aSelectedItems, oReferencedData);
    },

    handleKPIPartnerApplyClicked: function (aSelectedList, oReferencedData) {
      KpiStore.handleKPIPartnerApplyClicked(aSelectedList, oReferencedData);
    },

    handleAddNewKPIDrilldownLevelClicked: function (sLevelType) {
      KpiStore.handleAddNewKPIDrilldownLevelClicked(sLevelType);
    },

    handleRemoveKPIDrilldownLevelClicked: function (sDrillDownLevelId) {
      KpiStore.handleRemoveKPIDrilldownLevelClicked(sDrillDownLevelId);
    },

    handleKPIDrilldownMssValueChanged: function (sDrillDownLevelId, aSelectedIds, oReferencedTags) {
      KpiStore.handleKPIDrilldownMssValueChanged(sDrillDownLevelId, aSelectedIds, oReferencedTags);
    },

    handleKPIAddRuleClicked: function (sRuleBlockType, sRuleBlockId) {
      KpiStore.handleKPIAddRuleClicked(sRuleBlockType, sRuleBlockId)
    },

    handleKPIThresholdValueChanged: function (sActiveBlockId, sThresholdType, sUnitType, sThresholdValue) {
      KpiStore.handleKPIThresholdValueChanged(sActiveBlockId, sThresholdType, sUnitType, sThresholdValue);
    },

    handleDeleteRuleClicked: function (sRuleId) {
      KpiStore.handleDeleteRuleClicked(sRuleId);
    },

    handleTranslationsModuleListItemClicked: function (sModuleId) {
      var oCallbackFunction = {};
      oCallbackFunction.functionToExecute = this.handleTranslationsModuleListItemClicked.bind(this, sModuleId);
      if(_gridViewSafetyCheck(oCallbackFunction)) {
        TranslationsStore.handleTranslationsModuleListItemClicked(sModuleId);
      }
    },

    handleTranslationPropertyChanged: function(sId, sContext){
      var oCallbackFunction = {};
      oCallbackFunction.functionToExecute = this.handleTranslationPropertyChanged.bind(this, sId, sContext);
      if(_gridViewSafetyCheck(oCallbackFunction, sContext)) {
        TranslationsStore.handleTranslationPropertyChanged(sId);
      }
    },

    handleTranslationLanguagesChanged: function (aLanguages) {
      var oCallbackFunction = {};
      oCallbackFunction.functionToExecute = this.handleTranslationLanguagesChanged.bind(this, aLanguages);
      if(_gridViewSafetyCheck(oCallbackFunction)) {
        TranslationsStore.handleTranslationLanguagesChanged(aLanguages);
      }
    },

    handleSelectedChildModuleChanged: function (sModuleId) {
      var oCallbackFunction = {};
      oCallbackFunction.functionToExecute = this.handleSelectedClassModuleChanged.bind(this, sModuleId);
      if(_gridViewSafetyCheck(oCallbackFunction)) {
        TranslationsStore.handleSelectedChildModuleChanged(sModuleId);
      }
    },

    handleRelationshipConfigDialogButtonClicked: function (sButtonId) {
      RelationshipStore.handleRelationshipConfigDialogButtonClicked(sButtonId);
    },

    handleEntityConfigDialogButtonClicked: function (sButtonId, sEntityType) {
      _handleEntityConfigDialogButtonClicked(sButtonId, sEntityType);
    },

    handleOrganisationConfigListNodeClicked: function (sId) {
      OrganisationConfigStore.handleOrganisationConfigListNodeClicked(sId);
    },

    handleOrganisationTabChanged: function (sTabId) {
      PermissionStore.setSelectedTypeAndEntity({});
      let fFunctionToExecute = OrganisationConfigStore.handleOrganisationTabChanged.bind(this, sTabId);
      let oCallBack = {functionToExecute: fFunctionToExecute};
      if (OrganisationConfigStore.getOrganisationConfigScreenLockStatus()) {
        OrganisationConfigStore.showLockedOrganisationConfigScreenAlertify(oCallBack);
      } else {
        fFunctionToExecute();
      }
    },

    refreshOrganisationConfig: function () {
      OrganisationConfigStore.refreshOrganisationConfig();
    },

    refreshGoldenRecordRule: function () {
      var bGoldenRecordRuleScreenLockStatus = GoldenRecordsStore.getGoldenRecordRuleScreenLockStatus();
      if (bGoldenRecordRuleScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = GoldenRecordsStore.refreshGoldenRecordRule;

        CustomActionDialogStore.showTriActionDialog( oTranslations().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            GoldenRecordsStore.saveRule.bind(this, oCallbackData),
            GoldenRecordsStore.discardUnsavedRule.bind(this, oCallbackData)).set({transition: 'fade'});
      } else {
        GoldenRecordsStore.refreshGoldenRecordRule();
      }
    },

    deleteGoldenRecordRule: function (aSelectedIds) {
      GoldenRecordsStore.deleteGoldenRecordRule(aSelectedIds);
    },

    createGoldenRecordRule: function () {
      GoldenRecordsStore.createGoldenRecordRule();
    },

    deleteOrganisation: function () {
      OrganisationConfigStore.deleteOrganisation();
    },

    createOrganisation:function () {
      OrganisationConfigStore.createOrganisation();
    },

    handleRolePermissionToggleButtonClicked () {
      let fFunctionToExecute = OrganisationConfigStore.handleRolePermissionToggleButtonClicked;
      PermissionStore.setSelectedTypeAndEntity({});
      let oCallBack = {
        functionToExecute: fFunctionToExecute
      };
      if (RoleStore.getRoleScreenLockStatus()) {
        RoleStore.showScreenLockedAlertify(oCallBack);
      } else if (PermissionStore.getPermissionScreenLockStatus()) {
        PermissionStore.showScreenLockedAlertify(oCallBack);
      } else {
        fFunctionToExecute();
      }
    },

    handleRolePermissionSaveRoleClicked() {
      if(OrganisationConfigStore.isPermissionVisible()) {
        PermissionStore.savePermission({});
      }
      else {
        RoleStore.saveRole({});
      }
    },

    handleSystemsMSSLoadList: function (sEndpointId) {
      ProfileProps.resetPaginationData();
      ProfileStore.loadSystemList(sEndpointId);
    },

    handleSystemsMSSLoadMoreClicked: function (sEndpointId) {
      ProfileStore.handleSystemsMSSLoadMoreClicked(sEndpointId);
    },

    handleSystemsMSSCreateClicked: function (sSearchText, sEndpointId) {
      ProfileStore.handleSystemsMSSCreateClicked(sSearchText, sEndpointId);
    },

    handleMssCreateClicked: function (sContext, sSearchText) {
      let sSplitter = SettingUtils.getSplitter();
      let aSplitContext = CS.split(sContext, sSplitter);
      let sMainContext = aSplitContext[0];

      switch (sMainContext) {
        case "systems":
          this.handleSystemsMSSCreateClicked(sSearchText, aSplitContext[1]);
          break;
        case "tab":
        case "tabId":
          _handleCustomTabChanged("", true, sSearchText, aSplitContext);
          break;
        case "profile":
          _handleDashboardTabCreated("commonConfig", sSearchText, aSplitContext[1]);
          break;
      }
    },

    handleGridPropertyValueCreated: function (sPropertyId, sSearchText, sContentId, sPathToRoot, sGridContext) {
      switch (sPropertyId) {
        case "dashboardTab":
        case "dashboard":
          /** dashboardTab context used in endpoint config **/
          /** dashboard context used in KPI config **/
          _handleDashboardTabCreated("gridView", sSearchText, sPropertyId, sContentId, sGridContext);
          break;

        case "tabId":
          _handleRelationshipTabCreated("gridView", sSearchText, sPropertyId, sContentId, "", sGridContext);
          break;
      }
    },

    handleKPIMssCreateClicked: function (sPropertyId, sActiveKpiId, sSearchText) {
      switch (sPropertyId) {
        case "dashboardTab":
          _handleDashboardTabCreated("kpiConfig", sSearchText, sPropertyId, sActiveKpiId);
          break;
      }
    },

    handleMssLoadMoreClicked : function (sContext) {
      var aEntityList = [];
      let sSplitter = SettingUtils.getSplitter();
      let aSplitContext = CS.split(sContext, sSplitter);
      let sMainContext = aSplitContext[0];

      switch (sMainContext){

        case "systems":
          this.handleSystemsMSSLoadMoreClicked(aSplitContext[1]);
          return;

        case "tab":
          aEntityList.push(ConfigDataEntitiesDictionary.TABS);
          break;

        default:
          aEntityList.push(sContext);
          break;
      }

      this.loadMoreFetchEntities(aEntityList);
    },

    handleMssSearchClicked: function (sContext, sSearchText) {
      let sSplitter = SettingUtils.getSplitter();
      let aSplitContext = CS.split(sContext, sSplitter);
      let sMainContext = aSplitContext[0];
      switch (sMainContext) {
        case "tab":
          _handleSearchAndFetchEntities(ConfigDataEntitiesDictionary.TABS, sSearchText);
          break;

        case "systems":
          ProfileStore.handleSystemsMSSSearchClicked(sSearchText, aSplitContext[1]);
          break;

        case ConfigDataEntitiesDictionary.ATTRIBUTES:
          _handleSearchAndFetchEntities(ConfigDataEntitiesDictionary.ATTRIBUTES, sSearchText);
          break;
        case ConfigDataEntitiesDictionary.TAGS:
          _handleSearchAndFetchEntities(ConfigDataEntitiesDictionary.TAGS, sSearchText);
          break;
        case ConfigDataEntitiesDictionary.RELATIONSHIPS:
          _handleSearchAndFetchEntities(ConfigDataEntitiesDictionary.RELATIONSHIPS, sSearchText);
          break;
        default:
          _handleSearchAndFetchEntities(sMainContext, sSearchText);
          break;
      }
    },

    handleTabsConfigDialogButtonClicked: function (sButtonId) {
      TabsStore.handleTabsConfigDialogButtonClicked(sButtonId);
    },

    handleTabsSortDialogButtonClicked: function () {
      TabsStore.handleTabsSortDialogButtonClicked();
    },

    handleTabsConfigListItemShuffled: function (sId, iNewPosition) {
      TabsStore.handleTabsConfigListItemShuffled(sId, iNewPosition);
    },

    handleTabsSortListItemShuffled: function (sId, iNewPosition, changedTab) {
      TabsStore.handleTabsSortListItemShuffled(sId, iNewPosition, changedTab);
    },

    handleConfigScreenTabClicked: function (sTabId, bDoNotTrigger) {
      _handleConfigScreenTabClicked(sTabId, bDoNotTrigger);
    },

    handleSettingScreenLeftNavigationTreeItemClicked: function (sItemId, sParentId) {
      _handleSettingScreenLeftNavigationTreeItemClicked(sItemId, sParentId);
    },

    handleUserCreateClicked: function (oModel) {
      _handleUserCreateClicked(oModel);
    },

    handleCreateUserCancelClicked: function (oModel) {
      UserStore.cancelCreateUser(oModel);
    },

    resetContextConfigDetails: function () {
      _resetContextConfigDetails();
    },

    resetCalculatedAttributeConfigViewProps: function () {
      _resetCalculatedAttributeConfigViewProps();
    },

    resetAndFetchEntitiesForPropertyCollection : function (aEntityList, bLoadMore, oCallback) {
      _resetAndFetchEntitiesForPropertyCollection(aEntityList, bLoadMore, oCallback);
    },

    handleListViewSearchOrLoadMoreClicked: function (sContext, sSearchText, bLoadMore) {
      var oCallbackData = {};
      if (sContext == ConfigEntityTypeDictionary.ENTITY_TYPE_ICON_LIBRARY) {
        _handleListViewSearchOrLoadMoreClicked(sContext, sSearchText, bLoadMore);
      } else {
        oCallbackData.functionToExecute = _handleListViewSearchOrLoadMoreClicked.bind(this, sContext, sSearchText, bLoadMore);
        this.settingScreenSafetyCheck(oCallbackData) && _handleListViewSearchOrLoadMoreClicked(sContext, sSearchText, bLoadMore);
      }
    },

    handleContentHorizontalTreeNodeClicked: function (oReqData) {
      _toggleHierarchyScrollEnableDisableProp();
      var sSelectedContext = oReqData.selectedContext;
      switch(sSelectedContext){
        case "classConfig":
          var id = oReqData.clickedNodeId;
          delete oReqData.clickedNodeId;
          oReqData.id = id;
          ClassStore.fetchClassDetails(oReqData);
          break;

        case "permissionConfig":
          PermissionStore.handleRolePermissionChildClicked(oReqData);
          break;

        case "languageTree":
          LanguageTreeStore.fetchLanguage(oReqData.clickedNodeId);
          break;

        case "smartDocument":
          SmartDocumentStore.switchSmartDocumentSection(oReqData);
          break;

      }
    },

    handleContentHorizontalTreeNodePermissionToggled: function (sId, sProperty, sValue) {
      PermissionStore.handlePermissionToggled(sId, sProperty, sValue);
    },

    handleHorizontalHierarchyTreeTabChanged: function(sTabId, sContext) {
      switch(sContext){
        case "permissionConfig":
          PermissionStore.handleHierarchyTreeTabChanged(sTabId);
      }
    },

    handleUpdateViewProps: function (sContext, oUpdatedMssProps) {
      let sSplitter = SettingUtils.getSplitter();
      let aSplitContext = !CS.isEmpty(sContext) && sContext.split(sSplitter);

      switch (aSplitContext[0]) {
        case "kpiConfigDetail":
          KpiStore.updateViewProps(aSplitContext[1], oUpdatedMssProps);
          break;
      }
    },

    handleHorizontalTreeNodeToggleAutomaticScrollProp: function () {
      _toggleHierarchyScrollEnableDisableProp();
    },

    handleSystemSelectApplyClicked: function (aSelectedSystemIds, sContext) {
      _handleSystemSelectApplyClicked(aSelectedSystemIds, sContext);
    },

    handleEndpointSelectionViewSelectAllChecked: function (sSystemId, iCheckboxStatus, sContext) {
      _handleEndpointSelectionViewSelectAllChecked(sSystemId, iCheckboxStatus, sContext);
    },

    handleEndpointSelectionViewDeleteClicked: function (sSystemId, sContext) {
      _handleEndpointSelectionViewDeleteClicked(sSystemId, sContext);
    },

    handleEsc: function (oEvent) {
      _handleEsc(oEvent);
    },

    handleRemoveKlassClicked: function (sSectionId, sClassId, sMainContext) {
      switch (sMainContext) {
        case "classConfig":
          ClassStore.handleRemoveKlassClicked(sSectionId, sClassId);
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
          AttributionTaxonomyStore.handleRemoveKlassClicked(sSectionId, sClassId);
          break;
      }
    },

    handleClassDataTransferPropertiesAdded: function (sEntity, aSelectedIds, oReferencedData, sContext, sMainContext) {
      switch (sMainContext) {
        case "classConfig":
          ClassStore.handleClassDataTransferPropertiesAdded(sEntity, aSelectedIds, oReferencedData, sContext);
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
          AttributionTaxonomyStore.handleClassDataTransferPropertiesAdded(sEntity, aSelectedIds, oReferencedData, sContext);
          break;
      }
    },

    handleClassDataTransferPropertiesRemoved: function (sClassId, sPropertyId, sContext, sMainContext) {
      CustomActionDialogStore.showConfirmDialog(oTranslations().REMOVE_CONFIRMATION, "",
          function () {
            switch (sMainContext) {
              case "classConfig":
                ClassStore.handleClassDataTransferPropertiesRemoved(sClassId, sPropertyId, sContext);
                break;
              case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
                AttributionTaxonomyStore.handleClassDataTransferPropertiesRemoved(sClassId, sPropertyId, sContext);
                break;
            }
          }, function (oEvent) {
          });
    },

    handleClassDataTransferPropertiesCouplingChanged: function (sClassId, sPropertyId, sNewValue, sContext, sMainContext) {
      switch (sMainContext) {
        case "classConfig":
          ClassStore.handleClassDataTransferPropertiesCouplingChanged(sClassId, sPropertyId, sNewValue, sContext);
          break;
        case ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
          AttributionTaxonomyStore.handleClassDataTransferPropertiesCouplingChanged(sClassId, sPropertyId, sNewValue, sContext);
          break;
      }
    },

    handleRuleConfigDialogActionButtonClicked: function (sTabId) {
      let sSelectedLeftTreeItem = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
      switch (sSelectedLeftTreeItem) {
        case ConfigEntityTypeDictionary.ENTITY_TYPE_RULE:
          RuleStore.handleRuleConfigDialogActionButtonClicked(sTabId);
          break;

        case ConfigEntityTypeDictionary.ENTITY_TYPE_GOLDEN_RECORDS:
          GoldenRecordsStore.handleRuleConfigDialogActionButtonClicked(sTabId);
          break;
      }
    },

    handleUILanguageChanged: function () {
      let sSelectedTabId = SettingScreenProps.screen.getSelectedTabId();
      let fFunctionToExecute = SettingScreenStore.handleConfigScreenTabClicked.bind(this, sSelectedTabId);
      SettingScreenStore.settingScreenSafetyCheck({functionToExecute: fFunctionToExecute}) && fFunctionToExecute();
    },

    handleConfigModuleSearchDialogClosedClicked: function () {
      _handleConfigModuleSearchDialogClosedClicked();
    },

    handleProcessConfigFullScreenButtonClicked: function () {
      _handleProcessConfigFullScreenButtonClicked();
    },

    handlePropertyCollectionImportButtonClicked: function (aFiles) {
      let oImportExcel = {};
      oImportExcel.sUrl = oSettingsRequestMapping.ImportExcel;
      oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.PROPERTY_COLLECTION;
      PropertyCollectionStore.handlePropertyCollectionImportButtonClicked(aFiles,oImportExcel)
    },

    handlePropertyCollectionTabChanged: function (sTabId) {
      PropertyCollectionStore.handlePropertyCollectionTabChanged(sTabId);
    },

    handlePropertyCollectionDraggableListColumnsShuffled: function (oSource, oDestination, aDraggableIds) {
      PropertyCollectionStore.handlePropertyCollectionDraggableListColumnsShuffled(oSource, oDestination, aDraggableIds);
    },

    handlePropertyCollectionDraggableListTabClicked: function (sTabId) {
     _handlePropertyCollectionDraggableListTabClicked(sTabId);
    },

    handlePropertyCollectionDraggableListPropertyRemove: function (sDraggableIds) {
      PropertyCollectionStore.handlePropertyCollectionDraggableListPropertyRemove(sDraggableIds);
    },

    handlePropertyCollectionExportButtonClicked: function () {
      let oSelectiveExport = {};
      let oPostRequest = {};
      oSelectiveExport.sUrl = oSettingsRequestMapping.SelectiveExport;
      oPostRequest.exportType = "entity";
      oPostRequest.configCodes = [];
      oPostRequest.configType = oDataModelImportExportEntityTypeConstants.PROPERTY_COLLECTION;
      oSelectiveExport.oPostRequest = oPostRequest;
      PropertyCollectionStore.handlePropertyCollectionExportButtonClicked(oSelectiveExport);
    },

    handlePropertyCollectionManageEntityButtonClicked: function () {
      PropertyCollectionStore.handlePropertyCollectionManageEntityButtonClicked();
    },

    handlePartnersManageEntityButtonClicked: function () {
      OrganisationConfigStore.handlePartnersManageEntityButtonClicked();
    },

    handleRuleListManageEntityButtonClicked: function () {
      RuleListStore.handleRuleListManageEntityButtonClicked();
    },

    handleLanguageManageEntityButtonClicked: function () {
      LanguageTreeStore.handleLanguageManageEntityButtonClicked();
    },

    handleTaxonomyManageEntityButtonClicked: function () {
      AttributionTaxonomyStore.handleTaxonomyManageEntityButtonClicked();
    },

    handleClassManageEntityButtonClicked: function () {
      ClassStore.handleClassManageEntityButtonClicked();
    },

    handleOrganisationImportButtonClicked: function (aFiles) {
      let oImportExcel = {};
      oImportExcel.sUrl = oSettingsRequestMapping.ImportExcel;
      oImportExcel.entityType = oDataModelImportExportEntityTypeConstants.PARTNERS;
      OrganisationConfigStore.handleOrganisationImportButtonClicked(aFiles,oImportExcel);
    },

    handleOrganisationExportButtonClicked: function () {
      let oSelectiveExport = {};
      let oPostRequest = {};
      oSelectiveExport.sUrl = oSettingsRequestMapping.SelectiveExport;
      oPostRequest.exportType = "entity";
      oPostRequest.configCodes = [];
      oPostRequest.configType = oDataModelImportExportEntityTypeConstants.PARTNERS;
      oSelectiveExport.oPostRequest = oPostRequest;
      OrganisationConfigStore.handleOrganisationExportButtonClicked(oSelectiveExport);
    },

    handleLanguageTreeDialogCancelClicked: function () {
      LanguageTreeStore.cancelLanguageTreeDialogClick();
    },

    handleLanguageTreeDialogCreateClicked: function () {
      LanguageTreeStore.createLanguageTreeDialogClick();
    },

    handleSmartDocumentDialogButtonClicked: function (sButtonId) {
      SmartDocumentStore.handleSmartDocumentDialogButtonClicked(sButtonId)
    },

    handleSmartDocumentSnackBarButtonClicked: function (sButtonId) {
      SmartDocumentStore.handleSmartDocumentSnackBarButtonClicked(sButtonId)
    },

    handleSmartDocumentPresetMSSValueClicked: function (sKey, aSelectedItems, oReferencedData) {
      SmartDocumentStore.handleSmartDocumentPresetMSSValueClicked(sKey, aSelectedItems, oReferencedData)
    },

    handleThemeConfigurationSnackBarButtonClicked: function (sButtonId) {
      ThemeConfigurationScreenStore.handleThemeConfigurationSnackBarButtonClicked(sButtonId)
    },

    handleViewConfigurationSnackBarButtonClicked: function (sButtonId) {
      ViewConfigurationScreenStore.handleViewConfigurationSnackBarButtonClicked(sButtonId)
    },

    handleThemeConfigurationHeaderActionClicked: function (sButtonId) {
      ThemeConfigurationScreenStore.handleThemeConfigurationHeaderActionClicked(sButtonId)
    },

    handleViewConfigurationHeaderActionClicked: function (sButtonId) {
      ViewConfigurationScreenStore.handleViewConfigurationHeaderActionClicked(sButtonId)
    },

    handleLanguageTreeSnackBarButtonClicked: function (sButton) {
      LanguageTreeStore.handleLanguageTreeSnackBarButtonClicked(sButton);
    },

    handleClassConfigExportButtonClicked: function () {
      let oSelectiveExport = {};
      let oPostRequest = {};
      oSelectiveExport.sUrl = oSettingsRequestMapping.SelectiveExport;
      oPostRequest.exportType = "entity";
      oPostRequest.configCodes = [];
      oPostRequest.configType = "";
      oSelectiveExport.oPostRequest = oPostRequest;
      ClassStore.handleClassConfigExportButtonClicked(oSelectiveExport);
    },

    handleRuleDataLanguagesPopoverOpen: function () {
      RuleStore.handleRuleDataLanguagesPopoverOpen();
    },

    handleAdvancedSearchListItemNodeClicked : function (sItemId, sType, sContext) {
      ProcessStore.handleAdvancedSearchListItemNodeClicked(sItemId, sType, sContext);
    },

    handleAdvancedSearchListLoadMoreClicked : function (sType, sContext) {
      _loadMoreFetchEntities([sType]);
    },

    handleAdvancedSearchListSearched : function (sSearchText, sContext) {
      _handleAdvancedSearchListSearched(sSearchText, sContext);
    },

    handleFilterElementDeleteClicked: function (sElementId) {
      ProcessStore.handleFilterElementDeleteClicked(sElementId);
    },

    handleRemoveAppliedFilterClicked: function (sElementId, sContext, oExtraData, sSelectedHierarchyContext, oFilterContext) {
      switch(oFilterContext.screenContext) {
        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleRemoveAppliedFilterClicked(sElementId);
          break;
        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleRemoveAppliedFilterClicked(sElementId);
          break;
        default:
          ProcessStore.handleRemoveAppliedFilterClicked(sElementId);
      }
    },

    handleFilterElementExpandClicked: function (sElementId) {
      ProcessStore.handleFilterElementExpandClicked(sElementId);
    },

    handleAddAttributeClicked: function (sElementId) {
      ProcessStore.handleAddAttributeClicked(sElementId);
    },

    handleFilterAttributeValueDeleteClicked: function (sAttributeId, sValId) {
      ProcessStore.handleFilterAttributeValueDeleteClicked(sAttributeId, sValId);
    },

    handleFilterAttributeValueTypeChanged: function (sAttributeId, sValId, sTypeId) {
      ProcessStore.handleFilterAttributeValueTypeChanged(sAttributeId, sValId, sTypeId);
    },

    handleFilterAttributeValueChanged: function (sAttributeId, sValId, sVal) {
      ProcessStore.handleFilterAttributeValueChanged(sAttributeId, sValId, sVal);
    },

    handleFilterAttributeValueChangedForRange: function (sAttributeId, sValId, sVal, sRange) {
      ProcessStore.handleFilterAttributeValueChangedForRange(sAttributeId, sValId, sVal, sRange);
    },

    discardAppliedFilterDirtyChanges: function () {
      let oProcessFilterProps = SettingScreenProps.processFilterProps;
      oProcessFilterProps.setAppliedFiltersClone(null);
      oProcessFilterProps.setAppliedFilters(oProcessFilterProps.getAppliedFiltersCloneBeforeModifications());
      oProcessFilterProps.setAppliedFiltersCloneBeforeModifications([]);
    },

    toggleAdvancedSearchDialogVisibility: function () {
      ProcessFilerStore.handleAdvancedSearchButtonClicked();
    },

    handleAdvancedSearchPanelCancelClicked: function () {
     this.discardAppliedFilterDirtyChanges();
      this.toggleAdvancedSearchDialogVisibility();
      ProcessStore.prepareAdvancedFilterData();

      let sSearchText = SettingScreenProps.screen.getEntitySearchText();
      if(!CS.isEmpty(sSearchText)) {
        _handleAdvancedSearchListSearched("");
      }
      else {
        _triggerChange();
      }

    },

    handleAdvancedSearchFilterButtonCLicked: function () {
      ProcessStore.handleBPMNPropertiesAdvancedFilterChangedCustom();
      let oProcessFilterProps = SettingScreenProps.processFilterProps;
      oProcessFilterProps.setAdvancedSearchPanelShowStatus(false);
      ProcessStore.handleAdvancedSearchDataSave();
      ProcessStore.handleBPMNCustomTabsUpdate();
      _triggerChange();
    },

    handleAdvancedSearchPanelClearClicked: function () {
      ProcessStore.handleAdvancedSearchPanelClearClicked();
      _triggerChange();
    },

    handleAppliedFilterClearClicked: function (sSelectedHierarchyContext, oFilterContext) {
      switch(oFilterContext.screenContext) {
        case GridViewContexts.AUDIT_LOG:
          AuditLogStore.handleAppliedFilterClearClicked();
          break;
        case GridViewContexts.DOWNLOAD_TRACKER:
          DownloadTrackerConfigStore.handleAppliedFilterClearClicked();
          break;
        default:
          ProcessStore.handleAppliedFilterClearClicked();
      }

      _triggerChange();
    },

    handleRelationshipInheritanceApplyClicked: function(sEntity, aSelectedIds, oReferencedData, sContext) {
      _handleRelationshipInheritanceApplyClicked(sEntity, aSelectedIds, oReferencedData, sContext);
    },

    handleColorPickerColorChanged: function (sColor, sContext) {
      _handleColorPickerColorChanged(sColor, sContext);
    },

    handleManageEntityDialogButtonClicked: function (sButtonId, sEntityType) {
      switch (sButtonId) {
        case "ok":
          if (sEntityType == "icon") {
            IconLibraryStore.deleteIcons();
          }
          break;
        case "cancel":
          if (sEntityType == "icon") {
            IconLibraryStore.resetDeleteDialogProps();
          }
          break;
        case "close":
          _handleManageEntityDialogCloseButtonClicked();
          break;
      }
    },

    handleManageEntityDialogOpenButtonClicked:function (sSelectedId) {
      _handleManageEntityDialogOpenButtonClicked(sSelectedId);
    },

    handleColumnOrganizerSaveButtonClicked: function (sContext,aSelectedOrganizedColumns) {
      switch (sContext) {
        case "gridEditConfig":
          GridEditStore.handleColumnOrganizerSaveButtonClicked(aSelectedOrganizedColumns);
      }
    },

    handleColumnOrganizerDialogButtonClicked: function () {
      _triggerChange();
    },

    handleSectionListApplyButtonClicked: function (sContext, aSelectedItems, oReferencedData) {
      ClassStore.handleSectionListApplyButtonClicked(sContext, aSelectedItems, oReferencedData);
    },

    handleSectionListDeleteButtonClicked: function (sSelectedElement, sContext ) {
      ClassStore.handleSectionListDeleteButtonClicked(sSelectedElement, sContext );
    },

    handleBPMNElementsAddRowButtonClicked: function (sName) {
      ProcessStore.handleBPMNElementsAddRowButtonClicked(sName);
    },

    handleBPMNElementsRemoveRowButtonClicked: function (sName, iIndex) {
      ProcessStore.handleBPMNElementsRemoveRowButtonClicked(sName, iIndex);
    },

    handleFilterChildToggled: function (sParentId, sChildId, sContext, oExtraData, oFilterContext) {
      GridFilterStore.handleFilterChildToggled(sParentId, sChildId, sContext, oExtraData, oFilterContext);
    },

    handleFilterSummarySearchTextChanged: function (oFilterContext, sFilterId, sSearchedText) {
      GridFilterStore.handleFilterSummarySearchTextChanged(oFilterContext, sFilterId, sSearchedText);
    },

    handleCollapseFilterClicked: function (bExpanded, sContext) {
      GridFilterStore.handleCollapseFilterClicked(bExpanded, sContext);
    },

    handleApplyFilterButtonClicked: function (sContext) {
      GridFilterStore.handleApplyFilterButtonClicked(sContext);
    },

    handleFilterItemPopoverClosed: function (sContext) {
      GridFilterStore.handleFilterItemPopoverClosed(sContext);
    },

    handleDateRangePickerUpdateData: function (sContext, oRange, sId) {
      GridFilterStore.handleDateRangePickerUpdateData(sContext, oRange, sId);
    },

    handleFilterItemSearchTextChanged: function (sContext, sId, sSearchedText) {
      GridFilterStore.handleFilterItemSearchTextChanged(sContext, sId, sSearchedText);
    },

    handleBPMNElementsEntityMapChanged: function (sName, iIndex, sItemid, sNewVal) {
      ProcessStore.handleBPMNElementsEntityMapChanged(sName, iIndex, sItemid, sNewVal);
    },

    handleAuditLogExportStatusDialogButtonClicked: function (sButtonId) {
      _handleAuditLogExportStatusDialogButtonClicked(sButtonId);
    },

    handleAuditLogExportDialogRefreshButtonClicked: function () {
      _handleAuditLogExportDialogRefreshButtonClicked();
    },

    handleAuditLogExportDialogPaginationChanged: function (oNewPaginationData) {
      _handleAuditLogExportDialogPaginationChanged(oNewPaginationData);
    },

    handleDialogSaveButtonClicked: function () {
      _handleDialogSaveButtonClicked();
    },

    handleDialogCancelButtonClicked: function () {
      _handleDialogCancelButtonClicked();
    },

    handleDialogInputChanged: function (oFileData) {
      _handleDialogInputChanged(oFileData);
    },

    handleIconElementCheckboxClicked : function (aSelectedIconIds, bIsSelectAllClicked) {
      IconLibraryStore.handleIconElementCheckboxClicked(aSelectedIconIds, bIsSelectAllClicked);
      _triggerChange();
    },

    handleIconLibraryHeaderSelectAllActionClicked : function () {
      IconLibraryStore.handleIconElementCheckboxClicked([], true);
      _triggerChange();
    },

    handleIconLibraryHeaderUploadIconActionClicked : function (aFiles) {
      IconLibraryStore.handleIconUploadClicked(aFiles);
      _triggerChange();
    },

    handleIconLibraryHeaderRefreshActionClicked : function () {
      IconLibraryStore.handleIconLibraryHeaderRefreshActionClicked();
    },

    handleIconLibraryHeaderDeleteActionClicked : function () {
      IconLibraryStore.deleteIconElement();
      _triggerChange();
    },

    handleIconLibraryHeaderEntityUsageActionClicked : function () {
      IconLibraryStore.handleIconLibraryHeaderEntityUsageActionClicked();
      _triggerChange();
    },

    handleIconLibraryPaginationChanged: function (oNewPaginationData, sContext) {
      IconLibraryStore.handleIconLibraryPaginationChanged(oNewPaginationData, sContext);
    },

    handleFileDragDropViewDraggingState: function () {
      let bIsDragging = IconLibraryProps.getIsDragging();
      IconLibraryProps.setIsDragging(!bIsDragging);
      _triggerChange();
    },

    handleFileDrop: function (sContext, aFiles, oExtraData) {
      IconLibraryStore.handleIconUploadClicked(aFiles);
    },

    handleDialogListRemoveButtonClicked: function (iIconId) {
      IconLibraryStore.handleDialogListRemoveButtonClicked(iIconId);
    },

    handleSelectIconSelectClicked: function (oSelectedIconDetails, sContext) {
      let sIconId = oSelectedIconDetails.id;
      let sIconObjectKey = oSelectedIconDetails.iconKey;
      switch (sContext) {
        case 'class':
          ClassStore.handleClassIconChanged(sIconId, sIconObjectKey);
          break;
        case 'propertycollection':
          PropertyCollectionStore.handlePropertyCollectionIconChanged(sIconId, sIconObjectKey);
          break;
        case 'attributionTaxonomyDetail':
          AttributionTaxonomyStore.handleTaxonomyUploadIconChangeEvent(sIconId, sIconObjectKey);
          break;
        case 'context':
          ContextStore.handleContextUploadIconChangeEvent(sIconId, sIconObjectKey);
          break;
        case 'organisation':
        case 'organization':
          OrganisationConfigStore.handleOrganisationUploadIconChangeEvent(sIconId, sIconObjectKey);
          break;
        case 'relationship':
        case 'relationshipConfigDetail':
          RelationshipStore.handleRelationshipIconChanged(sIconId, sIconObjectKey);
          break;
        case "embeddedKlassContext":
        case "languageKlassContext":
        case "technicalImageContext":
          ClassStore.handleClassContextUploadIconChangeEvent(sIconId, sIconObjectKey);
          break;
        case 'languageTree':
          let oActiveLanguage = LanguageTreeProps.getActiveLanguage();
          LanguageTreeStore.handleCommonConfigDialogValueChanged('icon', sIconId);
          oActiveLanguage.languageClone.iconKey = sIconObjectKey;
          break;
      }
      this.handleSelectIconCancelClicked(sContext);
    },

    handleSelectIconCancelClicked: function (sContext, bIsOpenedFromGridView) {
      let oComponentProps = SettingUtils.getComponentProps();
      if(bIsOpenedFromGridView){
        IconLibrarySelectIconDialogProps.reset();
      }
      else{
        switch (sContext) {
          case 'class':
            let oClassConfigViewProps = oComponentProps.classConfigView;
            let oActiveClass = oClassConfigViewProps.getActiveClass();
            _updateShowSelectIconDialogStatus(oActiveClass);
            break;
          case 'propertycollection':
            let oPropertyCollectionConfigView = oComponentProps.propertyCollectionConfigView;
            let oActivePropertyCollection = oPropertyCollectionConfigView.getActivePropertyCollection();
            _updateShowSelectIconDialogStatus(oActivePropertyCollection);
            break;
          case 'attributionTaxonomyDetail':
            let oAttributionTaxonomyConfigView = oComponentProps.attributionTaxonomyConfigView;
            let oActiveTaxonomy = oAttributionTaxonomyConfigView.getActiveDetailedTaxonomy();
            _updateShowSelectIconDialogStatus(oActiveTaxonomy);
            break;
          case 'context':
            let oAppData = SettingUtils.getAppData();
            let oContextMasterList = oAppData.getContextList();
            let oContextConfigProps = oComponentProps.contextConfigView;
            let sActiveContextId = oContextConfigProps.getActiveContext().id;
            let oActiveContext = oContextMasterList[sActiveContextId] || {};
            _updateShowSelectIconDialogStatus(oActiveContext);
            break;
          case 'organisation':
          case 'organization':
            let oOrganisationConfigView = oComponentProps.organisationConfigView;
            let oActiveOrganisation = oOrganisationConfigView.getActiveOrganisation();
            _updateShowSelectIconDialogStatus(oActiveOrganisation);
            break;
          case 'relationship':
          case 'relationshipConfigDetail':
            let oRelationshipView = oComponentProps.relationshipView;
            let oActiveRelationship = oRelationshipView.getSelectedRelationship();
            _updateShowSelectIconDialogStatus(oActiveRelationship);
            break;
          case "embeddedKlassContext":
          case "languageKlassContext":
          case "technicalImageContext":
            let oClassConfigView = oComponentProps.classConfigView;
            let oClassContextDialogProps = oClassConfigView.getClassContextDialogProps();
            _updateShowSelectIconDialogStatus(oClassContextDialogProps);
            break;
          case 'languageTree':
            let oLanguageTreeProps = oComponentProps.languageTreeConfigView;
            let oActiveLanguage = oLanguageTreeProps.getActiveLanguage();
            _updateShowSelectIconDialogStatus(oActiveLanguage);
            break;
        }
      }
      IconLibraryStore.resetIconLibraryProps();
      _triggerChange();
    },

    handleSelectIconElementSelected: function (sSelectedIconId, sContext) {
      let aSelectedIconIds = IconLibraryProps.getSelectedIconIds();
      if (CS.isEmpty(aSelectedIconIds)) {
        IconLibraryProps.getSelectedIconIds().push(sSelectedIconId);
      }
      else if (CS.includes(IconLibraryProps.getSelectedIconIds(), sSelectedIconId)) {
        IconLibraryProps.setSelectedIconIds([]);
      }
      else {
        IconLibraryProps.setSelectedIconIds([]);
        IconLibraryProps.getSelectedIconIds().push(sSelectedIconId);
      }
      _triggerChange();
    },

    handleSelectIconElementSelectedFromGrid: function (oSelectedIconDetails, oGridViewData) {
      let sIconId = oSelectedIconDetails.id;
      let sIconKey = oSelectedIconDetails.iconKey;
      _handleGridPropertyValueChanged(oGridViewData.context, oGridViewData.contentId, oGridViewData.propertyId, {icon: sIconId, iconKey: sIconKey}, oGridViewData.pathToRoot);
      IconLibrarySelectIconDialogProps.reset();
      IconLibraryStore.resetIconLibraryProps();
      _triggerChange();
    },

    handleIconElementActionButtonClicked: function(sButtonId, sId, aFiles){
      IconLibraryStore.handleIconElementActionButtonClicked(sButtonId, sId, aFiles);
    },

    handleIconElementCancelIconClicked: function (sButtonId) {
      IconLibraryStore.handleIconElementCancelIconClicked(sButtonId);
    },

    handleIconElementSaveIconClicked: function(oFile, sCode, sName, isNameEmpty){
      _handleIconElementSaveIconClicked(oFile, sCode, sName, isNameEmpty)
    },

    handleIconElementReplaceIconClicked: function (oFile, fileName) {
      _handleIconElementReplaceIconClicked(oFile, fileName);
    },

    handleIconElementIconNameChanged: function (sFileName) {
      _handleIconElementIconNameChanged(sFileName);
    },

    handleIndesignServerConfigurationAddButtonClicked: function () {
      IndesignServerConfigurationStore.handleIndesignServerConfigurationAddButtonClicked();
    },

    handleIndesignServerConfigurationHeaderSaveButtonClicked: function () {
      IndesignServerConfigurationStore.handleIndesignServerConfigurationHeaderSaveButtonClicked();
    },

    handleIndesignServerConfigurationHeaderCancelButtonClicked: function () {
      IndesignServerConfigurationStore.fetchIndesignServerConfiguration();
    },

    handleIndesignServerConfigurationRemoveButtonClicked: function (sServerId) {
      IndesignServerConfigurationStore.handleIndesignServerConfigurationRemoveButtonClicked(sServerId);
    },

    handleIndesignServerConfigurationCheckStatusButtonClicked: function (oServerDetails) {
      IndesignServerConfigurationStore.handleIndesignServerConfigurationCheckStatusButtonClicked(oServerDetails);
    },

    handleProcessFrequencySummaryDateButtonClicked: function (sDate, sContext) {
      ProcessStore.handleProcessFrequencySummaryDateButtonClicked(sDate, sContext);
    },

    handleBPMNSearchCriteriaEditButtonClicked: function (sName) {
      ProcessStore.handleBPMNSearchCriteriaEditButtonClicked(sName);
    },

    handleTreeCheckClicked: function (iId, iLevel) {
      ProcessStore.handleTreeCheckClicked(iId, iLevel);
    },

    handleTaxonomyClearAllClicked: function () {
      ProcessStore.handleTaxonomyClearAllClicked();
    },

    fetchDAMConfiguration: function () {
      _fetchDAMConfiguration().then(_triggerChange);
    },

    handleDamConfigUseFileNameValueChanged: function () {
      _handleDamConfigUseFileNameValueChanged();
      _triggerChange();
    },

    handleDamConfigurationSaveDiscardButtonClicked: function (sButtonId) {
      switch (sButtonId) {
        case "save":
          _handleDamConfigurationSaveButtonClicked().then(_triggerChange);
          break;
        case "discard":
          _handleDamConfigurationDiscardButtonClicked();
          _triggerChange();
          break;
      }
    },

    handleListNodeViewNodeDeleteButtonClicked: function (sId, sContext) {
      _handleListNodeViewNodeDeleteButtonClicked(sId, sContext);
    },

    handleListViewNewActionItemClicked: function (sId, sContext) {
      let aListViewActionItemList = new MockDataForListViewActionItemButton();
      switch (sId) {
        case aListViewActionItemList[0].id:
          switch (sContext) {
            case "role":
              _createRole();
              break;
          }
          break;
      }
    },

    handleVariantConfigurationToggleButtonClicked: function () {
      _handleVariantConfigurationToggleButtonClicked();
      _triggerChange();
    },

    handleVariantActionButtonClicked: function (sId) {
      _handleVariantActionButtonClicked(sId)
          .then(_triggerChange);
    },

    fetchVariantConfigurationServerDetails: function (){
      _fetchVariantConfigurationServerDetails()
          .then(_triggerChange);
    },

    handleCreateRoleCloneDialogActionButtonClicked: function (sId) {
      RoleStore.handleCreateRoleCloneDialogActionButtonClicked(sId);
    },

    handleCreateRoleCloneDialogCheckboxButtonClicked: function (sContext) {
      RoleStore.handleCreateRoleCloneDialogCheckboxButtonClicked(sContext);
    },

    handleCreateRoleCloneDialogExactCloneButtonClicked: function () {
      RoleStore.handleCreateRoleCloneDialogExactCloneButtonClicked();
    },

    handleListNodeViewNodeCloneButtonClicked: function (sId, sContext) {
      _handleListNodeViewNodeCloneButtonClicked(sId,sContext);
    },

    handleCreateRoleCloneDialogEditValueChanged: function (sContext, sValue) {
      RoleStore.handleCreateRoleCloneDialogEditValueChanged(sContext, sValue);
    },

    fetchAdministrationSummaryData: function () {
      _fetchAdministrationSummaryData();
    },

    handleAdministrationSummaryExpandButtonClicked: function (sButtonId) {
      _handleAdministrationSummaryExpandButtonClicked(sButtonId)
    }

  };

})();

TagStore.bind('tag-changed', SettingScreenStore.triggerChange);
AttributeStore.bind('attribute-changed', SettingScreenStore.triggerChange);
CalculatedAttributeStore.bind('calculated-attribute-changed', SettingScreenStore.triggerChange);
RoleStore.bind('role-changed', SettingScreenStore.triggerChange);
UserStore.bind('user-changed', SettingScreenStore.triggerChange);
ProfileStore.bind('profile-changed', SettingScreenStore.triggerChange);
MappingStore.bind('mapping-changed', SettingScreenStore.triggerChange);
EndpointStore.bind('endpoint-changed', SettingScreenStore.triggerChange);
RelationshipStore.bind('relationship-changed', SettingScreenStore.triggerChange);
ClassStore.bind('class-changed', SettingScreenStore.triggerChange);
TemplateStore.bind('template-changed', SettingScreenStore.triggerChange);
PropertyCollectionStore.bind('property-collection-changed', SettingScreenStore.triggerChange);
PermissionStore.bind('permission-changed', SettingScreenStore.triggerChange);
TableStore.bind('table-changed', SettingScreenStore.triggerChange);
ClassSettingStore.bind('class-setting-changed', SettingScreenStore.triggerChange);
RuleStore.bind('rule-changed', SettingScreenStore.triggerChange);
KpiStore.bind('kpi-changed', SettingScreenStore.triggerChange);
RuleListStore.bind('rule-list-changed', SettingScreenStore.triggerChange);
AttributionTaxonomyStore.bind('attribution-taxonomy-list-changed', SettingScreenStore.triggerChange);
LanguageTreeStore.bind('language-tree-changed', SettingScreenStore.triggerChange);
ContextStore.bind('context-changed', SettingScreenStore.triggerChange);
TasksStore.bind('tasks-changed', SettingScreenStore.triggerChange);
DataGovernanceTasksStore.bind('data-governance-tasks-changed', SettingScreenStore.triggerChange);
ProcessStore.bind('process-changed', SettingScreenStore.triggerChange);
OrganisationConfigStore.bind('organisation-config-changed', SettingScreenStore.triggerChange);
TranslationsStore.bind('translations-changed', SettingScreenStore.triggerChange);
TranslationStore.bind('translation-changed', SettingScreenStore.triggerChange);
SettingLayoutStore.bind('layout-changed', SettingScreenStore.triggerChange);
TabsStore.bind('tabs-changed', SettingScreenStore.triggerChange);
GoldenRecordsStore.bind('goldenRecord-changed',SettingScreenStore.triggerChange);
CustomSearchDialogStore.bind('custom-search-changed',SettingScreenStore.triggerChange);
ThemeConfigurationScreenStore.bind('theme-config-changed',SettingScreenStore.triggerChange);
ViewConfigurationScreenStore.bind('view-config-changed',SettingScreenStore.triggerChange);
SmartDocumentStore.bind('smart-document-changed', SettingScreenStore.triggerChange);
SSOSettingStore.bind('sso-setting-changed', SettingScreenStore.triggerChange);
AuthorizationStore.bind('authorization-changed', SettingScreenStore.triggerChange);
PdfReactorServerStore.bind('pdf-reactor-server-changed', SettingScreenStore.triggerChange);
ManageEntityStore.bind('manage-entity-config-changed', SettingLayoutStore.triggerChange);
GridEditStore.bind('grid-edit-changed', SettingScreenStore.triggerChange);
AuditLogStore.bind('audit-log-changed', SettingScreenStore.triggerChange);
GridFilterStore.bind('grid-filter-changed', SettingScreenStore.triggerChange);
DownloadTrackerConfigStore.bind('download-tracker-config-changed', SettingScreenStore.triggerChange);
IconLibraryStore.bind('icon-library-config-changed', SettingScreenStore.triggerChange);
IndesignServerConfigurationStore.bind('indesign-server-configuration-changed', SettingScreenStore.triggerChange);

MicroEvent.mixin(SettingScreenStore);
export default SettingScreenStore;
