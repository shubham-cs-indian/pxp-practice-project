
import CS from '../../../../../libraries/cs';

import MicroEvent from '../../../../../libraries/microevent/MicroEvent.js';
import SettingScreenProps from './model/setting-screen-props';
import SettingUtils from './helper/setting-utils';
import TagStore from './helper/tag-store';
import AttributeStore from './helper/attribute-store';
import TasksStore from './helper/task-store';
import DataGovernanceTasksStore from './helper/data-governance-tasks-store';
import RelationshipStore from './helper/relationship-store';
import PropertyCollectionStore from './helper/property-collection-store';
import TemplateStore from './helper/template-store';
import UserStore from './helper/user-store';
import ProfileStore from './helper/profile-store';
import MappingStore from './helper/mapping-store';
import RoleStore from './helper/role-store';
import RuleStore from './helper/rule-store';
import KpiStore from './helper/kpi-store';
import RuleListStore from './helper/rule-list-store';
import AttributionTaxonomyStore from './helper/attribution-taxonomy-store';
import TranslationsStore from './helper/translations-store';
import ProcessStore from './helper/process-store';
import OrganisationConfigStore from './helper/organisation-config-store';
import TabsStore from './helper/tabs-store';
import GoldenStore from './helper/golden-record-store';
import LanguageTreeStore from './helper/language-tree-store';
import SmartDocumentStore from './helper/smart-document-store';
import ThemeConfigurationStore from './helper/theme-configuration-store';
import ViewConfigurationStore from './helper/view-configuration-store';
import oClassCategoryConstants from './../tack/class-category-constants-dictionary';
import oConfigEntityTypeDictionary from '../../../../../commonmodule/tack/config-entity-type-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';
import ConfigModulesList from '../tack/settinglayouttack/config-modules-list';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import BreadcrumbStore from '../../../../../commonmodule/store/helper/breadcrumb-store';
import SSOSettingStore from "./helper/sso-setting-store";
import AuthorizationStore from './helper/authorization-store';
import PdfReactorServerStore from "./helper/pdf-reactor-server-store";
import GridEditStore from "./helper/grid-edit-store";
import AuditLogStore from "./helper/auditlogstore/audit-log-store";
import DownloadTrackerConfigStore from "./helper/download-tracker-config-store";
import ColumnOrganizerProps from '../../../../../viewlibraries/columnorganizerview/column-organizer-props';
import IconLibraryStore from "./helper/icon-library-store";
import PropertyCollectionTabLayoutData from "../tack/property-collection-tab-layout-data";
import IndesignServerConfigurationStore from "./helper/indesign-server-configuration-store";

//TODO: #Clean Up:- Show alertify and error message with Translation
var SettingLayoutStore = (function () {


  var _triggerChange = function () {
    SettingLayoutStore.trigger('layout-changed');
  };

  /**************************************** Ajax Callbacks ****************************************************/

  let _getBreadCrumbDataListForConfig = (aConfigModuleTabLayoutData, aParentList, sSelectedItemId) => {
    let oParent = SettingUtils.getSelectedEntityParentData(aConfigModuleTabLayoutData, sSelectedItemId);
    if (CS.isNotEmpty(oParent) && oParent.parentId !== "-1") {
      aParentList.unshift(oParent);
      _getBreadCrumbDataListForConfig(aConfigModuleTabLayoutData, aParentList, oParent.parentId);
    }
  };

  let _prepareBreadCrumbData = (aParentList) => {
    let oBreadCrumbData = {};
    CS.forEach(aParentList, (oParent, iIndex) => {
      let sBreadcrumbType = BreadCrumbModuleAndHelpScreenIdDictionary.CONFIG_PARENT_ENTITY + iIndex;
      oBreadCrumbData = BreadcrumbStore.createBreadcrumbItem(oParent.id, oParent.label, sBreadcrumbType, oParent.id);
      BreadcrumbStore.addNewBreadCrumbItem(oBreadCrumbData);
    });
  };

  let _handleSettingScreenLeftNavigationTreeItemClicked = function (sItemId, sParentId, bIsTreeItemClicked) {
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sItemId, sParentId);
    let SettingScreenStore = SettingUtils.getSettingScreenStore();
    SettingScreenProps.screen.setSelectedLeftNavigationTreeItem(sItemId);
    SettingScreenProps.screen.setSelectedLeftNavigationTreeParentId(sParentId);
    // Resetting selected icons
    SettingScreenProps.iconLibraryProps.setSelectedIconIds([]);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    let aEntityList = [];
    let sSplitter = SettingUtils.getSplitter();
    if(sSelectedItemType.split(sSplitter)[0] === "translations"){
      sSelectedItemType = sSelectedItemType.split(sSplitter)[0];
    }

    let aConfigModuleTabLayoutData = SettingScreenProps.screen.getConfigScreenTabLayoutData();
    let oCallbackData = {};
    let aBreadCrumbParentList = [];
    _getBreadCrumbDataListForConfig(aConfigModuleTabLayoutData, aBreadCrumbParentList, sItemId);
    oCallbackData.breadCrumbData = _prepareBreadCrumbData(aBreadCrumbParentList);

    bIsTreeItemClicked && ColumnOrganizerProps.reset();

    switch (sSelectedItemType) {
        //Data Model
      case oConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTE:
        SettingScreenStore.resetCalculatedAttributeConfigViewProps();
        AttributeStore.fetchAttributeTypes();
        AttributeStore.setUpAttributeConfigGridView();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_TAG:
        TagStore.setUpTagConfigGridView();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
        SettingScreenStore.resetContextConfigDetails();
        break;

        //Handle class category icon clicked
      case oClassCategoryConstants.ARTICLE_CLASS:
        SettingScreenStore.handleSwitchClassCategory('class');
        break;

      case oClassCategoryConstants.ASSET_CLASS:
        SettingScreenStore.handleSwitchClassCategory('asset');
        break;

      case oClassCategoryConstants.TARGET_CLASS:
        SettingScreenStore.handleSwitchClassCategory('target');
        break;

      case oClassCategoryConstants.TEXTASSET_CLASS:
        SettingScreenStore.handleSwitchClassCategory('textasset');
        break;

      case oClassCategoryConstants.SUPPLIER_CLASS:
        SettingScreenStore.handleSwitchClassCategory('supplier');
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
        SettingScreenProps.attributionTaxonomyConfigView.setAttributionTaxonomyListActiveTabId("Master_Taxonomies");
        AttributionTaxonomyStore.fetchAttributionTaxonomyList();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_TEMPLATE:
        TemplateStore.setUpTemplateConfigGridView();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP:
        RelationshipStore.resetPaginationData();
        RelationshipStore.setSelectedRelationship({});//To clear DetailedView of screen.
        RelationshipStore.setUpRelationshipConfigGridView(sSelectedItemType);
        break;

        //Partner Admin
      case oConfigEntityTypeDictionary.ENTITY_TYPE_PROFILE:
        ProfileStore.reset();
        ProfileStore.getAllEndpointsForGridView();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS:
      /** Cleaned the Commented Code **/
        ProcessStore.reset(bIsTreeItemClicked);
        ProcessStore.fetchAllCustomComponents();
        ProcessStore.handleWorkflowTabClicked(bIsTreeItemClicked);
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_AUTHORIZATION:
        AuthorizationStore.reset();
        AuthorizationStore.setUpAuthorizationConfigGridView();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_MAPPING:
        MappingStore.reset();
        MappingStore.setUpMappingConfigGridView();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_ORGANISATION:
        let oCallback = {functionToExecute: OrganisationConfigStore.loadSystemList};
        ProfileStore.fetchProfileList(oCallback);
        PropertyCollectionStore.fetchPropertyCollectionsList();
        OrganisationConfigStore.reset();
        OrganisationConfigStore.fetchOrganisationConfigMap();
        SSOSettingStore.fetchSSOSettingsConfigurationList();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_USER:
        UserStore.setSelectedUser({});
        UserStore.getAllUsers(bIsTreeItemClicked);
        break;

        //Data Collaboration
      case oConfigEntityTypeDictionary.ENTITY_TYPE_TASK:
        TasksStore.setUpTaskConfigGridView(bIsTreeItemClicked);
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_DATAGOVERNANCE:
        DataGovernanceTasksStore.setUpDataGovernanceTaskConfigGridView(bIsTreeItemClicked);
        break;

        //Data Governance
      case oConfigEntityTypeDictionary.ENTITY_TYPE_RULE:
        RoleStore.setSelectedRole({}); //To clear details view of screen.
        TagStore.setActiveTag({});//To clear details view of screen.
        UserStore.setSelectedUser({}); //To clear details view of screen.
        RuleStore.setSelectedRule({});//To clear details view of screen.
        RuleListStore.fetchListOfRuleList();
        RuleStore.resetPaginationData();
        // below call is not needed as the data is getting fetched from fetchRuleListForGridView
        RuleStore.setUpRuleConfigGridView(bIsTreeItemClicked);
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST:
        RuleListStore.reset();
        RuleListStore.fetchListOfRuleList();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_KPI:
        RoleStore.setSelectedRole({}); //To clear details view of screen.
        UserStore.setSelectedUser({}); //To clear details view of screen.
        SettingScreenStore.resetCalculatedAttributeConfigViewProps();
        KpiStore.setUpKpiConfigGridView();
        break;

      case 'translations':
      case oConfigEntityTypeDictionary.ENTITY_TYPE_STATICTRANSLATION:
        let sSplitter = SettingUtils.getSplitter();
        TranslationsStore.setTranslationProps(sItemId.split(sSplitter)[1], bIsTreeItemClicked);
        if (sParentId && sParentId.split(sSplitter)[1] === "class") {
          TranslationsStore.handleSelectedChildModuleChanged(sItemId);
        } else if (sParentId && sParentId.split(sSplitter)[1] === "tabs") {
          TranslationsStore.handleSelectedChildModuleChanged(sItemId);
        } else if (sParentId && sParentId.split(sSplitter)[1] === "smartDocument") {
          TranslationsStore.handleSelectedChildModuleChanged(sItemId);
        } else {
          TranslationsStore.fetchTranslations(sItemId.split(sSplitter)[1]);
        }
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION:
        aEntityList = [ConfigDataEntitiesDictionary.ATTRIBUTES];
        SettingScreenProps.propertyCollectionConfigView.setPropertyCollectionDraggableListActiveTabId(ConfigDataEntitiesDictionary.ATTRIBUTES);
        SettingScreenStore.resetAndFetchEntitiesForPropertyCollection(aEntityList, false, {});
        PropertyCollectionStore.resetPaginationData();
        SettingScreenProps.propertyCollectionConfigView.setSearchText("");
        SettingScreenProps.propertyCollectionConfigView.setPropertyCollectionListActiveTabId(PropertyCollectionTabLayoutData().tabListIds.PROPERTY_COLLECTION_LIST_TAB);
        PropertyCollectionStore.fetchPropertyCollectionsList();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_TABS:
        TabsStore.setUpTabsGrid();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_GOLDEN_RECORDS:
        // GoldenStore.fetchGoldenRecords();
        GoldenStore.setUpGoldenRecordRuleConfigGridView(bIsTreeItemClicked);
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY:
        LanguageTreeStore.fetchLanguageTree();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_THEME_CONFIGURATION:
        ThemeConfigurationStore.fetchThemeConfigurationScreen();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_VIEW_CONFIGURATION:
        ViewConfigurationStore.fetchViewConfigurationScreen();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_SMARTDOCUMENT:
        SmartDocumentStore.fetchSmartDocumentConfigDetails({});
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_PDFREACTORSERVER:
        PdfReactorServerStore.fetchPdfReactorServerDetails();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_GRIDEDITVIEW:
        GridEditStore.resetGridEditProps();
        GridEditStore.fetchGridEditData();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_AUDIT_LOG:
        bIsTreeItemClicked && AuditLogStore.resetAuditLogFilterProps();
        AuditLogStore.setUpAuditLogGridView(bIsTreeItemClicked);
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_DOWNLOADTRACKER:
        DownloadTrackerConfigStore.resetGridData();
        DownloadTrackerConfigStore.getDownloadLogs();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_ICON_LIBRARY:
        IconLibraryStore.resetIconLibraryProps();
        IconLibraryStore.fetchIconLibraryScreen();
        _triggerChange();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_INDESIGN_SERVER_CONFIGURATION:
        IndesignServerConfigurationStore.fetchIndesignServerConfiguration();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_DAM_CONFIGURATION:
        SettingScreenStore.fetchDAMConfiguration();
        IndesignServerConfigurationStore.fetchDAMConfiguration();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_VARIANT_CONFIGURATION:
        SettingScreenStore.fetchVariantConfigurationServerDetails();
        break;

      case oConfigEntityTypeDictionary.ENTITY_TYPE_ADMINISTRATION_SUMMARY:
        SettingScreenStore.fetchAdministrationSummaryData();
        break;
    }
  };

  let _setIsExpandedInTreeData = function (aLeftNavigationTreeData, oLeftNavigationTreeValuesMap) {
    CS.forEach(aLeftNavigationTreeData, function (oData, iIndex) {
      oLeftNavigationTreeValuesMap[oData.id] = {
        isExpanded: !iIndex
      };
      if (CS.isNotEmpty(oData.children)) {
        _setIsExpandedInTreeData(oData.children, oLeftNavigationTreeValuesMap);
      }
    });
  };

  let _handleConfigScreenTabClicked = function (sTabId) {
    let oSelectedScreenModule = SettingUtils.getSelectedSettingScreenModuleById(sTabId);
    let aLeftNavigationTreeData = oSelectedScreenModule.children;
    let oLeftNavigationTreeValuesMap = {};
    _setIsExpandedInTreeData(aLeftNavigationTreeData, oLeftNavigationTreeValuesMap);
    SettingScreenProps.screen.setLeftNavigationTreeData(aLeftNavigationTreeData);
    SettingScreenProps.screen.setLeftNavigationTreeValuesMap(oLeftNavigationTreeValuesMap);
    SettingScreenProps.screen.setSelectedTabId(sTabId);
    SettingScreenProps.screen.setSelectedLeftNavigationTreeItem("");
    SettingScreenProps.screen.setSelectedLeftNavigationTreeParentId("");
    let aTabItemsList = new ConfigModulesList();
    let oTab = CS.find(aTabItemsList, {id: sTabId});

    let oCallbackData = {};
    let sBreadcrumbType = BreadCrumbModuleAndHelpScreenIdDictionary.CONFIG_TAB;
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(sTabId, oTab.label, sBreadcrumbType, sTabId);

    BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);
    SettingScreenProps.screen.setConfigScreenTabLayoutData(oTab.children);
  };
  /********************************Public API**************************************/
  return {
    handleConfigScreenTabClicked: function (sTabId, bDoNotTrigger = false) {
      let oCallbackData = {};
      let SettingScreenStore = SettingUtils.getSettingScreenStore();
      oCallbackData.functionToExecute = _handleConfigScreenTabClicked.bind(this, sTabId);
      SettingScreenStore.settingScreenSafetyCheck(oCallbackData) && _handleConfigScreenTabClicked(sTabId);

      !bDoNotTrigger && _triggerChange();
    },

    /**
     *
     * @param sItemId
     * @param sParentId
     * @param bIsTreeItemClicked - True if left side tree item clicked
     */
    handleSettingScreenLeftNavigationTreeItemClicked: function (sItemId, sParentId, bIsTreeItemClicked) {
      if(CS.isEmpty(sItemId)){
        sItemId = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
        sParentId = SettingScreenProps.screen.getSelectedLeftNavigationTreeParentId();
      }

      let oCallbackData = {};
      let SettingScreenStore = SettingUtils.getSettingScreenStore();
      oCallbackData.functionToExecute = _handleSettingScreenLeftNavigationTreeItemClicked.bind(this, sItemId, sParentId, bIsTreeItemClicked);
      SettingScreenStore.settingScreenSafetyCheck(oCallbackData) && _handleSettingScreenLeftNavigationTreeItemClicked(sItemId, sParentId, bIsTreeItemClicked);
    },

    triggerChange: function () {
      _triggerChange();
    },
  };
})();

MicroEvent.mixin(SettingLayoutStore);
export default SettingLayoutStore;
