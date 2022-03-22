import CS from '../../../../../libraries/cs';
import eventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import ActionInterceptor from '../../../../../libraries/actioninterceptor/action-interceptor';
import { events as KeydownEvents } from '../../../../../libraries/keydownhandler/keydownhandler';
import { events as ContextEvents } from '../view/context-config-view';
import { events as RoleDetailEvents } from '../view/role-detail-view';
import { events as RelationshipDetailsViewEvents } from '../view/relationship-details-view';
import { events as RelationshipConfigViewEvents } from '../view/relationship-config-view';
import { events as ClassConfigViewCloneEvents } from '../view/class-config-view-clone';
import { events as ClassConfigSectionViewEvents } from '../view/class-config-section-view';
import { events as SectionGridElementViewEvents } from '../view/section-grid-element-view';
import { events as ClassAvailableListSearchViewEvents } from '../../../../../viewlibraries/classavailablelistsearchview/core-class-available-list-search-view';
import { events as PropertyCollectionGridElementViewEvents } from '../view/property-collection-grid-element-view';
import { events as TemplateHeaderViewEvents } from '../view/template-header-view';
import { events as TemplateTabBodyEvents } from '../view/template-tab-body-view';
import { events as CustomTemplateEvents } from '../view/custom-template-detail-view';
import { events as PermissionHeaderViewEvents } from '../view/permission-header-view';
import { events as PermissionTabBodyviewEvents } from '../view/permission-tab-body-view';
import { events as VisualSectionViewEvents } from '../view/visual-section-view';
import { events as PropertyCollectionVisualSectionViewEvents } from '../view/property-collection-visual-section-view';
import { events as ClassConfigRelationshipViewEvents } from '../view/class-config-relationship-view';
import { events as DragViewEvents } from '../../../../../viewlibraries/dragndropview/drag-view.js';
import { events as DropViewEvents } from '../../../../../viewlibraries/dragndropview/drop-view.js';
import { events as MultiSelectSearchViewNewEvents } from '../../../../../viewlibraries/multiselectsearchviewnew/multiselect-search-view.js';
import { events as CalculatedAttributeFormulaViewEvents } from '../../../../../viewlibraries/calculatedattributeformulaview/calculated-attribute-formula-view';
import { events as SelectionToggleViewEvents } from '../../../../../viewlibraries/selectiontoggleview/selection-toggle-view';
import { events as SingleSelectSearchViewEvents } from '../../../../../viewlibraries/uniqueSelectionView/unique-selection-view.js';
import { events as ClassConfigSectionsView } from '../view/class-config-sections-view';
import { events as PermissionViewEvents } from '../view/permission-detail-view';
import { events as ClassConfigRuleViewEvents } from '../view/class-config-rule-view';
import { events as PaperViewEvent } from '../../../../../viewlibraries/paperview/paper-view';
import { events as CommonConfigSectionViewEvents } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { events as ColorPickerViewEvents } from '../../../../../viewlibraries/color-picker-view/color-picker-view.js';
import { events as RoleConfigViewEvents } from '../view/role-config-view';
import { events as RuleDetailViewEvents } from '../view/rule-detail-view';
import { events as RuleListConfigViewEvents } from '../view/rule-list-config-view';
import { events as BlackListViewEvents } from '../view/black-list-editable-view';
import { events as TaxonomyLevelViewEvents } from '../view/taxonomy-level-view';
import { events as TaxonomyLevelChildItemViewEvents } from '../view/taxonomy-level-child-item-view';
import { events as ProcessConfigViewEvents } from '../view/process-config-view';
import { events as ComponentParameterViewEvents } from '../view/component-parameter-view';
import { events as BPMNWrapperViewEvents } from '../view/config-bpmn-wrapper-view';
import { events as ClassContextDialogViewEvents } from '../view/class-context-dialog-view';
import { events as OrganisationConfigDetailViewEvents } from './../view/organisation-config-detail-view';
import { events as UserPasswordChangeView } from '../../../../../viewlibraries/userdetailview/user-password-change-view';
import { events as TabsConfigViewEvents } from '../view/tabs-config-view';
import { events as SystemsSelectionViewEvents } from '../view/systems-selection-view';
import { events as EndpointsSelectionViewEvents } from '../view/endpoints-selection-view';
import { events as TagTreeGroupViewEvents } from '../../../../../viewlibraries/taggroupview/tag-group-view.js';
import { events as ListViewEvents } from '../../../../../viewlibraries/listview/list-view.js';
import { events as MappingSummaryViewEvents } from '../../../../../viewlibraries/mappingsummaryview/mapping-summary-view';
import { events as PropertyGroupSummaryViewEvents } from '../../../../../viewlibraries/mappingsummaryview/property-group-summary-view';
import { events as MappingEvents } from '../view/mapping-config-view';
import { events as AuthorizationMappingConfigEvents} from '../view/authorization-config-view';
import { events as TabsSortDialogViewEvents } from '../view/tabs-sort-dialog-view';
import { events as IconLibraryViewEvents } from '../view/icon-library-view';
import { events as IconLibrarySelectIconViewEvents } from '../view/icon-library-select-icon-view';
import { events as IconEditViewEvents } from '../view/icon-edit-view';
import { events as IndesignServerConfigurationViewEvents } from '../view/indesign-server-configuration-view';
import { events as IndesignServerViewEvents } from '../view/indesign-server-view';
import { events as DAMConfigurationViewEvents } from '../view/dam-configuration-view';
import { events as ProcessFrequencySummaryViewEvents } from '../../../../../viewlibraries/processFrequencySummaryView/process-frequency-summary-view';
import { events as TableCellViewEvents } from '../view/table-cell-view';
import { events as TableViewNew } from '../view/table-view-new';

import { events as ContentMeasurementMetricsEvents } from '../../../../../viewlibraries/measurementmetricview/content-measurement-metrics-view';
import { events as GridViewEvents } from '../../../../../viewlibraries/gridview/grid-view';
import { events as TranslationConfigViewEvents } from '../view/translations-config-view';
import { events as GridPropertyViewEvents } from '../../../../../viewlibraries/gridview/grid-property-view';
import { events as GridImagePropertyViewEvents } from '../../../../../viewlibraries/gridview/grid-image-property-view';
import { events as GridMSSWithAdditionalListEvents } from '../../../../../viewlibraries/gridview/grid-mss-with-additional-list';
import { events as GridFilterViewEvents} from '../../../../../viewlibraries/gridview/grid-filter-view';
import { events as TagSelectorViewEvents } from '../../../../../viewlibraries/tagselectorview/tag-selector-view';
import { events as AddTaxonomyPopoverViewEvents } from '../../../../../viewlibraries/addtaxonomypopoverview/add-taxonomy-popover-view';
import { events as SmallTaxonomyViewEvents } from '../../../../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import { events as NewMultiSelectSearchViewEvents } from '../../../../../viewlibraries/multiselectview/multi-select-search-view';
import { events as DataTransferViewEvents } from '../view/data-transfer-view';
import { events as ContextualAttributeEditSectionSelectViewEvents } from '../view/contextual-attribute-edit-section-select-view';
import { events as ContentHorizontalTreeViewEvents } from '../../../../../viewlibraries/horizontaltreeview/horizontal-tree-view';
import { events as ContentHorizontalTreeNodeViewEvents } from '../../../../../viewlibraries/horizontaltreeview/horizontal-tree-node-view';
import { events as KPIConfigViewEvents } from '../view/kpi-config-view';
import { events as RuleConfigViewEvents } from '../view/rule-config-view';
import { events as KPIDetailedViewEvents } from '../view/kpi-detail-config-view';
import { events as RolePermissionContainerViewEvents } from '../view/role-permission-container-view';
import SettingUtils from '../store/helper/setting-utils';
import { events as GridCalculatedAttributePropertyViewEvents } from '../../../../../viewlibraries/gridview/grid-calculated-attribute-property-view.js';
import { events as ExpandableNestedMenuListViewEvents } from '../../../../../viewlibraries/expandablenestedmenulistview/expandable-nested-menu-list-view';
import { events as AttributionTaxonomyConfigViewEvents } from '../view/attribution-taxonomy-config-view';
import { events as KPIDrilldownViewEvents } from '../view/kpi-drilldown-view';
import { events as ConfigEntityCreationDialogViewEvents } from '../view/config-entity-creation-dialog-view';
import { events as ConfigScreenViewEvents } from '../view/setting-screen-view';
import { events as ClassConfigDataTransferView } from '../view/class-config-data-transfer-view';
import { events as ConfigFileUploadButtonViewEvents } from '../../../../../viewlibraries/configfileuploadbuttonview/config-file-upload-button-view';
import { events as LazyMssView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import { events as ConfigHeaderViewEvents } from '../../../../../viewlibraries/configheaderview/config-header-view';
import { events as UserProfileView } from '../../../../../viewlibraries/userprofileview/user-profile-view';
import { events as CustomSearchDialogView } from '../../../../../viewlibraries/SearchBarView/custom-search-dialog-view';
import SettingScreenStore from '../store/setting-screen-store';
import SettingScreenConstants from './../store/model/setting-screen-constants';
import { events as CustomBPMNDiagramReplaceEvents } from '../view/libraries/bpmn/custom-diagram-replace-provider';
import { events as CustomTabPropertiesProviderEvents } from '../view/libraries/bpmn/custom-tabs-provider';
import { events as BPMNCUSTOMComponentsPropertiesViewEvents } from '../view/bpmn-custom-components-properties-view';
import { events as LanguageSelectionView } from '../../../../../viewlibraries/languageselectionview/language-selection-view';
import { events as ProcessConfigFullScreenWrapperView } from '../view/process-config-full-screen-wrapper-view';
import { events as PropertyCollectionConfigView } from '../view/property-collection-config-view';
import { events as LanguageTreeConfigView } from '../view/language-tree-config-view';
import { events as SmartDocumentConfigView } from '../view/smart-document-config-view';
import { events as ThemeConfigurationView } from '../view/theme-configuration-config-view';
import { events as ViewConfigurationView } from '../view/view-configuration-config-view';
import { events as OrgnisationConfigView } from '../view/organisation-config-view';
import { events as HomeScreenCommunicator } from '../../../store/home-screen-communicator';
import { events as TagConfigViewEvents } from '../view/tag-config-view';
import { events as ItemListPanelViewEvents } from '../../../../../viewlibraries/itemListPanelView/item-list-panel-view';
import { events as ContentFilterElementsViewEvents } from '../../contentscreen/view/content-filter-elements-view';
import { events as AdvancedSearchPanelViewEvents } from '../../contentscreen/view/advanced-search-panel-view';
import { events as AppliedFilterEvents } from '../../../../../viewlibraries/filterview/fltr-applied-filter-view';
import { events as EndpointConfigViewEvents } from '../view/endpoint-config-view';
import {events as AuthorizationMappingEvents} from '../../../../../viewlibraries/authorizationMappingSummaryView/authorization-mapping-summary-view';
import {events as SSOConfigScreenViewEvents} from '../view/sso-config-screen-view';
import { events as RelationshipInheritanceViewEvents } from '../view/relationship-inheritance-view';
import {events as AssetUploadConfigurationViewEvents} from '../view/asset-upload-configuration-view';
import {events as ManageEntityDialogViewEvents} from '../view/entity-usage-dialog-view';
import {events as ColumnOrganizerEvents} from '../../../../../viewlibraries/columnorganizerview/column-organizer-view';
import {events as ColumnOrganizerDialogEvents} from '../../../../../viewlibraries/columnorganizerview/column-organizer-dialog-view';
import {events as SectionListViewEvents} from  '../../../../../viewlibraries/sectionListView/section-list-view';
import {events as FilterViewEvents} from '../../../../../viewlibraries/gridview/grid-filter-summary-view';
import {events as CustomDateRangePickerViewEvents} from '../../../../../viewlibraries/customdaterangepickerview/custom-date-range-picker-view'
import {events as AuditLogExportStatusViewEvents} from '../view/audit-log-export-status-view';
import {events as IconLibraryEditCodeDialogViewEvents} from '../view/icon-library-edit-code-dialog-view';
import {events as FileDragAndDropViewEvents} from "../../../../../viewlibraries/filedraganddropview/file-drag-and-drop-view";
import { events as ListNodeViewNewEvents } from '../../../../../viewlibraries/listviewnew/list-node-view-new';
import { events as ListViewNewEvents } from '../../../../../viewlibraries/listviewnew/list-view-new';
import { events as PropertyCollectionDraggableListViewEvents } from '../view/property-collection-draggable-list-view';
import { events as VariantConfigurationViewEvents } from '../view/variant-configuration-view';
import {events as CreateRoleCloneDialogViewEvents} from '../view/utils/create-role-clone-dialog-view';
import {events as AdministrationSummaryViewEvents} from '../view/administration-summary-view'

var SettingScreenAction = (function () {

  const oEventHandler = {};

  var handleActionItemClicked = function (sButtonId, aFiles) {
    switch (sButtonId) {
      case 'save':
        SettingScreenStore.handleSaveButtonInActionBarClickedNew();
        break;
      case 'cancel':
        SettingScreenStore.handleCancelButtonInActionBarClickedNew();
        break;
      case 'discard':
        SettingScreenStore.handleDiscardConfigData();
        break;

     //Handle process action bar icons click
      case 'create_process':
        SettingScreenStore.createProcess();
        break;
      case 'delete_process':
        SettingScreenStore.deleteProcess();
        break;
      case 'refresh_process':
        SettingScreenStore.handleProcessRefreshMenuClicked();
        break;

      // Handle Data Governance Tasks action bar icons click
      case 'create_dataGovernanceTask':
        SettingScreenStore.createDataGovernanceTask();
        break;
      case 'delete_dataGovernanceTask':
        SettingScreenStore.deleteDataGovernanceTask();
        break;
      case 'refresh_dataGovernanceTask':
        SettingScreenStore.handleDataGovernanceTasksRefreshMenuClicked();
        break;

      case 'refresh_permission':
        SettingScreenStore.handlePermissionRefreshMenuClicked();
        break;

      //Handle role action bar icons click
      case 'create_role':
        SettingScreenStore.createRole();
        break;
      case 'delete_role':
        SettingScreenStore.deleteRole();
        break;
      case 'refresh_role':
        SettingScreenStore.handleRoleRefreshMenuClicked();
        break;

      //Handle rule action bar icons click
      case 'create_rule':
        SettingScreenStore.createRule();
        break;
      case 'delete_rule':
        SettingScreenStore.deleteRule();
        break;
      case 'refresh_rule':
        SettingScreenStore.handleRuleRefreshMenuClicked();
        break;

        //Handle rule action bar icons click
      case 'create_rule_list':
        SettingScreenStore.createRuleList();
        break;
      case 'delete_rule_list':
        SettingScreenStore.deleteRuleList();
        break;
      case 'refresh_rule_list':
        SettingScreenStore.handleRuleListRefreshMenuClicked();
        break;

        //Handle relationship action bar icons click
      case 'create_relationship':
        SettingScreenStore.createNewRelationship();
        break;
      case 'delete_relationship':
        SettingScreenStore.deleteRelationship();
        break;
      case 'refresh_relationship':
        SettingScreenStore.handleRelationshipRefreshMenuClicked();
        break;

      //Handle class category icon clicked
      case 'class_class':
        SettingScreenStore.handleSwitchClassCategory('class');
        break;
      case 'task_class':
        SettingScreenStore.handleSwitchClassCategory('task');
        break;
      case 'asset_class':
        SettingScreenStore.handleSwitchClassCategory('asset');
        break;
      case 'target_class':
        SettingScreenStore.handleSwitchClassCategory('target');
        break;
      case 'textasset_class':
        SettingScreenStore.handleSwitchClassCategory('textasset');
        break;
      case 'supplier_class':
        SettingScreenStore.handleSwitchClassCategory('supplier');
        break;
      case 'editorial_class':
        SettingScreenStore.handleSwitchClassCategory('editorial');
        break;
      //Handle class action bar icons click
      case 'create_class':
        SettingScreenStore.createClass();
        break;
      case 'delete_class':
        SettingScreenStore.deleteClass();
        break;
      case 'refresh_class':
        SettingScreenStore.handleClassNodeRefreshMenuClicked();
        break;
      //Handle class action bar icons click
      case 'create_propertycollection':
        SettingScreenStore.createPropertyCollection(SettingScreenConstants.PROPERTY_COLLECTION);
        break;
      case 'delete_propertycollection':
        SettingScreenStore.deletePropertyCollection();
        break;
      case 'refresh_propertycollection':
        SettingScreenStore.handlePropertyCollectionNodeRefreshMenuClicked();
        break;
      case 'create_xraycollection':
        SettingScreenStore.createPropertyCollection(SettingScreenConstants.X_RAY_PROPERTY_COLLECTION);
        break;

        //Handle Organisation Config Action bar icons click
      case 'create_organisation_config':
        SettingScreenStore.createOrganisation();
        break;

      case 'delete_organisation_config':
        SettingScreenStore.deleteOrganisation();
        break;

      case 'refresh_organisation_config':
        SettingScreenStore.refreshOrganisationConfig();
        break;

      case 'refresh_goldenRecordRule':
        SettingScreenStore.refreshGoldenRecordRule();
        break;

      case 'delete_goldenRecordRule':
        SettingScreenStore.deleteGoldenRecordRule();
        break;

      case 'create_goldenRecordRule':
        SettingScreenStore.createGoldenRecordRule();
        break;

      case 'export_attribution_taxonomy':
        SettingScreenStore.handleAttributionTaxonomyExportButtonClicked(aFiles);
        break;

      case 'refresh_attribution_taxonomy_config':
        SettingScreenStore.handleRefreshAttributionTaxonomyConfig();
        break;

      case 'export_propertyCollection':
        SettingScreenStore.handlePropertyCollectionExportButtonClicked();
        break;

      case 'export_organisation':
        SettingScreenStore.handleOrganisationExportButtonClicked();
        break;

      case 'export_class':
        SettingScreenStore.handleClassConfigExportButtonClicked();
        break;

      case 'export_language_tree':
        SettingScreenStore.handleLanguageTreeConfigExportButtonClicked();
        break;

      case 'manageEntity':
        SettingScreenStore.handleManageEntityMultiUsageButtonClicked();
        break;

      case 'manage_entity_propertyCollection':
        SettingScreenStore.handlePropertyCollectionManageEntityButtonClicked();
        break;

      case 'manage_entity_partners':
        SettingScreenStore.handlePartnersManageEntityButtonClicked();
        break;

      case 'manage_entity_rule_list':
        SettingScreenStore.handleRuleListManageEntityButtonClicked();
        break;

      case 'manage_entity_language':
        SettingScreenStore.handleLanguageManageEntityButtonClicked();
        break;

      case 'manage_entity_taxonomy':
        SettingScreenStore.handleTaxonomyManageEntityButtonClicked();
        break;

      case 'manage_entity_class':
        SettingScreenStore.handleClassManageEntityButtonClicked();
        break;
      case 'reset_theme_configuration':
        SettingScreenStore.handleThemeConfigurationHeaderActionClicked(sButtonId);
        break;
      case 'reset_view_configuration':
        SettingScreenStore.handleViewConfigurationHeaderActionClicked(sButtonId);
        break;

        //Handle icon library action bar icon clicks
      case 'select_icon_library':
        SettingScreenStore.handleIconLibraryHeaderSelectAllActionClicked();
        break;

      case 'upload_icon_library':
        SettingScreenStore.handleIconLibraryHeaderUploadIconActionClicked(aFiles);
        break;

      case 'manage_icon_library':
        SettingScreenStore.handleIconLibraryHeaderEntityUsageActionClicked();
        break;

      case 'delete_icon_library_items':
        SettingScreenStore.handleIconLibraryHeaderDeleteActionClicked();
        break;

      case 'refresh_icon_library':
        SettingScreenStore.handleIconLibraryHeaderRefreshActionClicked();
        break;

      case 'refresh_administration_summary':
        SettingScreenStore.fetchAdministrationSummaryData();
        break;
    }

  };

  var handleListViewNodeClicked = function (oContext, oEvent, oModel) {

    switch (oModel.properties['selectedScreenName']) {
      case 'dataGovernanceTasks':
        SettingScreenStore.handleDataGovernanceTasksListNodeClicked(oModel);
        break;
      case 'permissions':
        SettingScreenStore.handlePermissionScreenRoleListNodeClicked(oModel);
        break;
      case 'relationships':
        SettingScreenStore.handleRelationshipListNodeClicked(oModel);
        break;
      case 'roles':
        SettingScreenStore.handleRoleListNodeClicked(oModel);
        break;
      case 'rules':
        SettingScreenStore.handleRuleListNodeClicked(oModel);
        break;
      case 'ruleList':
        SettingScreenStore.handleListOfRuleListNodeClicked(oModel);
        break;
      case 'propertycollection':
        SettingScreenStore.handlePropertyCollectionListNodeClicked(oModel);
        break;
      case 'process':
        SettingScreenStore.handleProcessListNodeClicked(oModel);
        break;

      case "permissionTemplate":
        var sId = oModel.id;
        var sType = oModel.type;
        SettingScreenStore.handlePermissionFirstLevelItemClicked(sId, sType);
        break;

      case "organisation_config":
        SettingScreenStore.handleOrganisationConfigListNodeClicked(oModel.id);
        break;

      case "goldenRecordRules":
        SettingScreenStore.handleGoldenRecordRuleListNodeClicked(oModel.id);
        break;
    }
  };

  var handleTagTypeRangeValueChanged = function (oContext, sTagValueId, sNewValue) {
    SettingScreenStore.handleTagTypeRangeValueChanged(sTagValueId, sNewValue);
  };

  var handleContextTagCheckAllChanged = function(oContext, sContextTagId){
    SettingScreenStore.handleContextTagCheckAllChanged(sContextTagId);
  };

  var handleDefaultFromDateValueChanged = function(sValue, sContext){
    SettingScreenStore.handleDefaultFromDateValueChanged(sValue, sContext);
  };

  var handleContextConfigDialogButton = function (sContext) {
    SettingScreenStore.handleContextConfigDialogButton(sContext);
  };

  var handleCommonConfigSectionSingleTextChanged = function (oContext, sContext, sKey, sVal) {
    SettingScreenStore.handleCommonConfigSectionSingleTextChanged(sContext, sKey, sVal);
  };

  var handleCommonConfigSectionFroalaTextChanged = function (oContext, sContext, sKey, oVal) {
    SettingScreenStore.handleCommonConfigSectionFroalaTextChanged(sContext, sKey, oVal);
  };

  var handleCommonConfigSectionAddOperator = function (oContext, sOperatorType) {
    SettingScreenStore.handleCommonConfigSectionAddOperator(sOperatorType);
  };

  var handleCommonConfigSectionAddConcatObject = function (sType) {
    SettingScreenStore.handleCommonConfigSectionAddConcatObject(sType);
  };

  var handleCommonConfigSectionConcatInputChanged = function (sValue, oConcat) {
    SettingScreenStore.handleCommonConfigSectionConcatInputChanged(sValue, oConcat);
  };

  var handleCommonConfigSectionConcatObjectRemoved = function (sId) {
    SettingScreenStore.handleCommonConfigSectionConcatObjectRemoved(sId);
  };

  var deleteOperatorAttributeValue = function (oContext, sId) {
    SettingScreenStore.deleteOperatorAttributeValue(sId);
  };

  var handleOperatorAttributeValueChanged = function (oContext, sId, sType, sValue) {
    SettingScreenStore.handleOperatorAttributeValueChanged(sId, sType, sValue);
  };

  var handleCommonConfigSectionViewYesNoButtonClicked = function (oContext, sKey, bValue, sConfigContext) {
    SettingScreenStore.handleCommonConfigSectionSingleTextChanged(sConfigContext, sKey, bValue);
  };

  var handleCommonConfigSectionViewRadioButtonClicked = function (oContext, sContext, sRadioKey) {
    SettingScreenStore.handleCommonConfigSectionViewRadioButtonClicked(sContext, sRadioKey);
  };

  var handleCommonConfigSectionViewIconChanged = function (oContext, sKey, sConfigContext, aFiles) {
    SettingScreenStore.handleCommonConfigSectionViewIconChanged(sKey, sConfigContext, aFiles);
  };

  var handleCommonConfigSectionViewSingleTextNumberChanged = function (sContext, sKey, sNewValue) {
    SettingScreenStore.handleCommonConfigSectionViewSingleTextNumberChanged(sContext, sKey, sNewValue);
  };

  var handleCommonConfigSectionViewNativeDropdownValueChanged = function (sContext, sKey, sNewValue) {
    SettingScreenStore.handleCommonConfigSectionViewNativeDropdownValueChanged(sContext, sKey, sNewValue);
  };

  var handleCommonConfigCheckboxOnChangeForEntityPermission = function (oContext, bIsChecked, sKey, sScreenContext) {
    SettingScreenStore.handleCommonConfigCheckboxOnChangeForEntityPermission(bIsChecked, sKey, sScreenContext);
  };

  var handleCommonConfigUploadTemplateActionClicked = function (oContext, oElementData,aFiles) {
    SettingScreenStore.handleCommonConfigUploadTemplateActionClicked(oElementData,aFiles);
  };

  var handleCommonConfigUploadIconClicked = function (oRefDom, sConfigContext) {
    SettingScreenStore.handleCommonConfigUploadIconClicked(oRefDom, sConfigContext);
  };

  var handleSettingScreenRoleDataChanged = function (oContext, oModel, sContext, sNewValue) {
    SettingScreenStore.handleSettingScreenRoleDataChanged(oModel, sContext, sNewValue);
  };

  var handleRoleModulePermissionChanged = function (oContext, sModule) {
    SettingScreenStore.handleRoleModulePermissionChanged(sModule);
  };

  let handleDashboardVisibilityToggled = function () {
    SettingScreenStore.handleDashboardVisibilityToggled();
  };

  let handleReadOnlyPermissionToggled = function () {
    SettingScreenStore.handleReadOnlyPermissionToggled();
  };

  //Relationship
  let handleRelationshipConfigDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleRelationshipConfigDialogButtonClicked(sButtonId);
  };

  var handleRelationshipDataChanged = function (oContext, oNewModel, oReferencedKlasses) {
    SettingScreenStore.handleRelationshipDataChanged(oNewModel, oReferencedKlasses);
  };

  var handleClassCreateDialogButtonClicked = function (sContext,sLabel, bIsNature, sNatureType) {
    SettingScreenStore.handleClassCreateDialogButtonClicked(sContext, sLabel, bIsNature, sNatureType);
  };

  var handleClassSaveDialogButtonClicked = function (sContext) {
    SettingScreenStore.handleClassSaveDialogButtonClicked(sContext);
  };

  let handleClassConfigImportButtonClicked = function (aFiles) {
    SettingScreenStore.handleClassConfigImportButtonClicked(aFiles);
  };

  var handleClassRelationshipModified = function (sRelationshipId, sContext, sNewValue, oReferencedKlasses) {
    SettingScreenStore.handleClassRelationshipModified(sRelationshipId, sContext, sNewValue, oReferencedKlasses);
  };

  var handleDeleteVisualElementIconClicked = function (oContext, oEvent, oInfo) {
    SettingScreenStore.handleDeleteVisualElementIconClicked(oInfo);
  };

  var handleVisualElementBlockerClicked = function (oContext, oEvent, oInfo) {
    SettingScreenStore.handleVisualElementBlockerClicked(oInfo);
  };

  var handleSectionColCountChanged = function (oContext, oSection, iColCount) {
    SettingScreenStore.handleSectionColCountChanged(oSection, iColCount);
  };

  var handleSectionRowCountChanged = function (oContext, oSection, iRowCount) {
    SettingScreenStore.handleSectionRowCountChanged(oSection, iRowCount);
  };

  var handleSectionDeleteClicked = function (oContext, sSectionId) {
    SettingScreenStore.handleSectionDeleteClicked(sSectionId);
  };

  var handleSectionMoveUpClicked = function (oContext, sSectionId) {
    SettingScreenStore.handleSectionMoveUpClicked(sSectionId);
  };

  var handleSectionMoveDownClicked = function (oContext, sSectionId) {
    SettingScreenStore.handleSectionMoveDownClicked(sSectionId);
  };

  var handleSectionBlockerClicked = function (oContext, sSectionId) {
    SettingScreenStore.handleSectionBlockerClicked(sSectionId);
  };

  var handleSectionClicked = function (oContext, sSectionId) {
    SettingScreenStore.handleSectionClicked(sSectionId);
  };

  let handleAddNewAssetConfigurationSectionClicked = function () {
    SettingScreenStore.handleAddNewAssetConfigurationSectionClicked();
  };

  let handleEditAssetConfigurationSectionClicked = function (sId) {
    SettingScreenStore.handleEditAssetConfigurationSectionClicked(sId);
  };

  let handleAssetConfigurationDialogButtonClicked = function (sId) {
    SettingScreenStore.handleAssetConfigurationDialogButtonClicked(sId);
  };

  let handleDeleteAssetConfigurationRowClicked = function (oRow) {
    SettingScreenStore.handleDeleteAssetConfigurationRowClicked(oRow);
  };

  let handleAssetConfigRowDataChange = function (oField, sColumnId, oData) {
    SettingScreenStore.handleAssetConfigRowDataChange(oField, sColumnId, oData);
  };

  var handleDropDownListNodeClicked = function (oEvent, sContextKey, sModelId, bIsSingleSelect) {
    SettingScreenStore.handleDropdownListNodeClicked(sContextKey, sModelId, bIsSingleSelect);
  };

  var handleDropDownListNodeBlurred = function (sContextKey, aCheckedIds) {
    SettingScreenStore.handleDropdownListNodeBlurred(sContextKey, aCheckedIds);
  };

  var handleMultiSelectSearchCrossIconClicked = function (oEvent, sContextKey, oModel) {
    SettingScreenStore.handleMultiSelectSearchCrossIconClicked(sContextKey, oModel);
  };

  var handleRuleCalcAttrAddOperator = function(sAttrId, sId, sOperatorType) {
    SettingScreenStore.handleRuleCalcAttrAddOperator(sAttrId, sId, sOperatorType);
  };

  var handleRuleCalcAttrOperatorAttributeValueChanged = function (sAttrId, sId, sType, sAttributeOperatorId, sValue) {
    SettingScreenStore.handleRuleCalcAttrOperatorAttributeValueChanged(sAttrId, sId, sType, sAttributeOperatorId, sValue);
  };

  var handleRuleCalcAttrDeleteOperatorAttributeValue = function (sAttrId, sId, sAttributeOperatorId) {
    SettingScreenStore.handleRuleCalcAttrDeleteOperatorAttributeValue(sAttrId, sId, sAttributeOperatorId);
  };

  var handleRuleCalcAttrCustomUnitChanged = function (sAttrId, sId, sUnit, sUnitAsHTML) {
    SettingScreenStore.handleRuleCalcAttrCustomUnitChanged(sAttrId, sId, sUnit, sUnitAsHTML);
  };

  var handleRuleDetailsMSSLoadMoreClicked = function (sContext) {
    SettingScreenStore.loadMoreFetchEntities([sContext]);
  };

  let handleRuleDetailsKlassMSSChanged = function (sContext, aSelectedClasses) {
    SettingScreenStore.handleRuleDetailsKlassMSSChanged(sContext, aSelectedClasses);
  };

  let handleRuleDetailsKlassTypeMSSChanged = function (aNewKlassType) {
    SettingScreenStore.handleRuleDetailsKlassTypeMSSChanged(aNewKlassType);
  };

  let handleRuleConfigNatureClassAddedInGoldenRecord = function (aSelectedIds) {
    SettingScreenStore.handleRuleConfigNatureClassAddedInGoldenRecord(aSelectedIds);
  };

  var handleRuleDetailsMSSSearchTextChanged = function (sContext, sSearchText) {
    SettingScreenStore.handleRuleDetailsMSSSearchTextChanged(sContext, sSearchText);
  };

  let handleProcessAndSetNewCustomElement = function (oNewElement, oCustomDefinition, bpmnFactory, modeling) {
    SettingScreenStore.handleProcessAndSetNewCustomElement(oNewElement, oCustomDefinition, bpmnFactory, modeling);
  };

  let handleProcessAndSetNewElement = function (oNewElement) {
    SettingScreenStore.handleProcessAndSetNewElement(oNewElement);
  };

  let handleSetActiveBPMNInstances = function (oNewElement, oBPMNFactory, oModelling) {
    SettingScreenStore.handleSetActiveBPMNInstances(oNewElement, oBPMNFactory, oModelling);
  };

  let handleBPMNCustomTabsUpdate = function () {
    SettingScreenStore.handleBPMNCustomTabsUpdate();
  };

  let handleBPMNCustomElementTextChanged= function (sName,sSelectedAttributeId,sNewVal) {
    SettingScreenStore.handleBPMNCustomElementTextChanged(sName, sSelectedAttributeId,sNewVal);
  };

  let handleBPMNCustomElementMSSChanged = function (sName, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleBPMNCustomElementMSSChanged(sName, aSelectedItems, oReferencedData);
  };

  let handleBPMNPropertiesMSSClicked = function () {
    SettingScreenStore.handleMSSValueClicked();
  };

  let handleBPMNElementsDataLanguageMSSChanged = function (sName, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleBPMNElementsDataLanguageMSSChanged(sName, aSelectedItems, oReferencedData);
  };

  let handleBPMNElementsGroupMSSChanged = function (sContext, sAction, aSelectedRoles) {
    SettingScreenStore.handleBPMNElementsGroupMSSChanged(sContext, sAction, aSelectedRoles);
  };

  let handleBPMNElementsVariableMapChanged = function (sName, iIndex, sType, sValue) {
    SettingScreenStore.handleBPMNElementsVariableMapChanged(sName, iIndex, sType, sValue);
  };

  let handleBPMNElementsAddVariableMap = function (sName) {
    SettingScreenStore.handleBPMNElementsAddVariableMap(sName);
  };

  let handleBPMNElementsRemoveVariableMap = function (sName, iIndex) {
    SettingScreenStore.handleBPMNElementsRemoveVariableMap(sName, iIndex);
  };

  let handleBPMNPropertiesTagMSSChanged = function (sName, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleBPMNPropertiesTagMSSChanged(sName, aSelectedItems, oReferencedData);
  };

  let handleBPMNPropertiesTagMSSChangedCustom = function (sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleBPMNPropertiesTagMSSChangedCustom(sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData);
  };

  let handleBPMNPropertiesAttributeMSSChangedCustom = function (sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleBPMNPropertiesAttributeMSSChangedCustom(sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData);
  };

  let handleBPMNPropertiesAttributeDateChangedCustom = function (sName, sSelectedAttributeId, sNewVal) {
    SettingScreenStore.handleBPMNPropertiesAttributeDateChangedCustom(sName, sSelectedAttributeId, sNewVal);
  };

  let handleSearchFilterEditButtonClicked = function (sName) {
    SettingScreenStore.handleSearchFilterEditButtonClicked(sName);
  };

  var handleSelectionToggleButtonClicked = function (sContext, sKey, sId, bSingleSelect) {
    SettingScreenStore.handleSelectionToggleButtonClicked(sContext, sKey, sId, bSingleSelect);
  };

  var handleContentEntityDragStart = function (oEvent, oModel) {
    SettingScreenStore.handleClassEntityDragStart(oModel);
  };

  var handleContentEntityDragEnd = function (oEvent, oModel) {
    SettingScreenStore.handleClassEntityDragEnd(oModel);
  };

  var handleContentEntityDrop = function (oEvent, oModel) {
    SettingScreenStore.handleClassEntityDrop(oModel);
  };

  var handleSectionNameChanged = function (oContext, sNewValue) {
    SettingScreenStore.handleSectionNameChanged(sNewValue);
  };

  var handleSectionIconChanged = function (oContext, aFiles) {
    SettingScreenStore.handleSectionIconChanged(aFiles);
  };

  var handleUploadImageChangeEvent = function (oContext, oEvent, aFiles) {
    SettingScreenStore.handleUploadImageChangeEvent(aFiles);
  };

  let handleRemoveUserImageClicked = function () {
    SettingScreenStore.handleRemoveUserImageClicked();
  };

  var handleUserDataChangeEvent = function (oEvent, oModel, sContext, sNewValue) {
    SettingScreenStore.handleUserDataChangeEvent(oModel, sContext, sNewValue, oEvent);
  };

  var handleUserPasswordSubmit = function (oEvent, sPassword) {
    SettingScreenStore.handleUserPasswordSubmit(sPassword);
  };

  var handleUserPasswordCancel = function () {
    SettingScreenStore.handleUserPasswordCancel();
  };

  var handleListItemCreated = function () {
    SettingScreenStore.handleListItemCreated();
  };

  let handleConfigHeaderViewSearch = function (sContext, sSearchText) {
    SettingScreenStore.handleListViewSearchOrLoadMoreClicked(sContext, sSearchText, false);
  };

  let handleListViewSearchOrLoadMoreClicked = function (sContext, sSearchText, bLoadMore) {
    SettingScreenStore.handleListViewSearchOrLoadMoreClicked(sContext, sSearchText, bLoadMore);
  };

  var handleMappingSummaryViewAddNewMappingRow = function (sContext, sSummaryType) {
    SettingScreenStore.handleMappingSummaryViewAddNewMappingRow(sContext, sSummaryType);
  };

  var handleMappingSummaryViewColumnNameChanged = function (sId, sName, sContext, sSummaryType) {
    SettingScreenStore.handleMappingSummaryViewColumnNameChanged(sId, sName, sContext, sSummaryType);
  };

  var handleMappingSummaryViewAddTagValueClicked = function (sId, sMappedElementId, sContext) {
    SettingScreenStore.handleAddTagValueClicked(sId, sMappedElementId, sContext);
  };

  var handleMappingSummaryViewConfigTagValueChanged = function (sId, sMappedTagValueId, sNewValue, sContext) {
    SettingScreenStore.handleMappingSummaryViewConfigTagValueChanged(sId, sMappedTagValueId, sNewValue, sContext);
  };

  var handleMappingSummaryViewConfigTagValueIgnoreCaseToggled = function (sId, sMappedTagValueId, sContext) {
    SettingScreenStore.handleMappingSummaryViewConfigTagValueIgnoreCaseToggled(sId, sMappedTagValueId, sContext);
  };

  var handleMappingSummaryViewConfigMappedTagValueChanged = function (sId, sMappedTagValueId, sMappedElementId, sContext, oReferencedData) {
    SettingScreenStore.handleMappingSummaryViewConfigMappedTagValueChanged(sId, sMappedTagValueId, sMappedElementId, sContext, oReferencedData);
  };

  var handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked = function (sId, sMappedTagValueId, sContext) {
    SettingScreenStore.handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked(sId, sMappedTagValueId, sContext);
  };

  var handleColorPickerColorChanged = function (sColor, sContext) {
    SettingScreenStore.handleColorPickerColorChanged(sColor, sContext);
  };

  let handleTabLayoutTabChanged = function (iTabId, sContext) {
    SettingScreenStore.handleTabLayoutTabChanged(iTabId, sContext);
  };

  var handleMappingSummaryViewMappedElementChanged = function (sId, sMappedElementId, sContext, sSummaryType, oReferencedData) {
    SettingScreenStore.handleMappingSummaryViewMappedElementChanged(sId, sMappedElementId, sContext, sSummaryType, oReferencedData);
  };

  var handleMappingSummaryViewMappingRowSelected = function (sId, sContext) {
    SettingScreenStore.handleMappingSummaryViewMappingRowSelected(sId, sContext);
  };

  var handleMappingSummaryViewIsIgnoredToggled = function (sId, sContext, sSummaryType) {
    SettingScreenStore.handleMappingSummaryViewIsIgnoredToggled(sId, sContext, sSummaryType);
  };

  var handleMappingSummaryViewMappingRowDeleted = function (sId, sContext, sSummaryType) {
    SettingScreenStore.handleMappingSummaryViewMappingRowDeleted(sId, sContext, sSummaryType);
  };

  var handleVisualElementAttributeValueChanged = function(oEvent, oInfo){
    SettingScreenStore.handleVisualElementAttributeValueChanged(oInfo);
  };

  var handleSectionElementCheckboxToggled = function (sSectionId, sElementId, sProperty) {
    SettingScreenStore.handleSectionElementCheckboxToggled(sSectionId, sElementId, sProperty);
  };

  var handleClassContextDialogDateValueChanged = function(sKey, sDateValue){
    SettingScreenStore.handleClassContextDialogDateValueChanged(sKey, sDateValue);
  };

  var handleClassContextDialogAddTagCombination = function(){
    SettingScreenStore.handleClassContextDialogAddTagCombination();
  };

  var handleSectionElementInputChanged = function (sSectionId, sElementId, sProperty, sNewValue) {
    SettingScreenStore.handleSectionElementInputChanged(sSectionId, sElementId, sProperty, sNewValue);
  };

  var handleSectionMSSValueChanged = function (sSectionId, sElementId, sProperty, aNewValue) {
    SettingScreenStore.handleSectionMSSValueChanged(sSectionId, sElementId, sProperty, aNewValue);
  };

  let handlePropertyGroupCheckboxClicked = function (bIsCheckboxClicked, sContext) {
    SettingScreenStore.handlePropertyGroupCheckboxClicked(bIsCheckboxClicked, sContext);
  };

  let handlePropertyGroupApplyButtonClicked = function (sContext, aSelectedItems, oReferencedData) {
    SettingScreenStore.handlePropertyGroupApplyButtonClicked(sContext, aSelectedItems, oReferencedData);
  };

  let handlePropertyGroupDeleteButtonClicked = function (sSelectedElement, sContext) {
    SettingScreenStore.handlePropertyGroupDeleteButtonClicked(sSelectedElement, sContext);
  };

  let handlePropertyGroupCheckboxClickedForAttributeAndTag = function (bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag) {
    SettingScreenStore.handlePropertyGroupCheckboxClickedForAttributeAndTag(bIsCheckboxClicked, sSelectedElement, sContext, sSelectedElementForSubTag);
  };

  let handlePropertyGroupPropertyCollectionListItemClicked = function (sSelectedElement, sContext) {
    SettingScreenStore.handlePropertyGroupPropertyCollectionListItemClicked(sSelectedElement, sContext);
  };

  let handlePropertyGroupColumnNameChanged = function (sId, sName, sSummaryType) {
    SettingScreenStore.handlePropertyGroupColumnNameChanged(sId, sName, sSummaryType);
  };

  let handlePropertyGroupSearchViewChanged = function (sSearchValue) {
    SettingScreenStore.handlePropertyGroupSearchViewChanged(sSearchValue);
  };

  let handlePropertyCollectionToggleButtonClicked = function (sContext, bIsPropertyCollectionToggleButtonClicked) {
    SettingScreenStore.handlePropertyCollectionToggleButtonClicked(sContext, bIsPropertyCollectionToggleButtonClicked);
  };

  var handleSectionToolbarIconClicked = function (sContext, sSectionId, sIconType) {
    switch (sIconType) {
      case 'up':
        SettingScreenStore.handleSectionMoveUpClicked(sSectionId);
        break;
      case 'down':
        SettingScreenStore.handleSectionMoveDownClicked(sSectionId);
        break;
      case 'remove':
        SettingScreenStore.handleSectionDeleteClicked(sContext, sSectionId);
        break;
      case 'isSkipped':
        SettingScreenStore.handleSectionSkippedToggled(sSectionId);
        break;
    }
  };

  let handleSectionToggleButtonClicked = function (sContext, sSectionId) {
    SettingScreenStore.handleSectionToggleButtonClicked(sContext, sSectionId);
  };

  var handleSectionAdded = function (aSectionIds, sContext) {
    SettingScreenStore.handleSectionAdded(aSectionIds, sContext);
  };

  var handlePermissionTemplateAdded = function (aSectionIds) {
    SettingScreenStore.handlePermissionTemplateAdded(aSectionIds);
  };

  var handleClassConfigRuleItemClicked = function (oContext, sRuleId) {
    SettingScreenStore.handleClassConfigRuleItemClicked(sRuleId);
  };

  var paperViewHeaderClicked = function (oContext, oModel) {
    SettingScreenStore.paperViewHeaderClicked(oModel);
  };

  let fetchEntityChildrenWithGlobalPermissions = (sItemId, sParentId) => {
    SettingScreenStore.fetchEntityChildrenWithGlobalPermissions(sItemId, sParentId);
  };

  var handlePermissionButtonToggled = function(sId, sProperty, sType, bForAllChildren){
    SettingScreenStore.handlePermissionButtonToggled(sId, sProperty, sType, bForAllChildren);
  };

  var handlePermissionSelectionToggled = function(sId, sType){
    SettingScreenStore.handlePermissionSelectionToggled(sId, sType);
  };

  var handlePermissionFirstLevelItemClicked = function(sId, sType){
    SettingScreenStore.handlePermissionFirstLevelItemClicked(sId, sType);
  };

  var handlePermissionKlassItemClicked = function(sId, sPermissionNodeId, sType){
    SettingScreenStore.handlePermissionKlassItemClicked(sId, sPermissionNodeId, sType);
  };

  var handlePermissionRemoveTemplateClicked = function(sId){
    SettingScreenStore.handlePermissionRemoveTemplateClicked(sId);
  };

  let fetchAllowedTemplates = function (sContext, bIsLoadMore) {
    if (bIsLoadMore) {
      SettingScreenStore.loadMoreFetchEntities([sContext]);
    } else {
      SettingScreenStore.loadInitialEntityData(sContext);
    }
  };

  let handleAllowedTemplatesSearch = function (sSearchText) {
    SettingScreenStore.handleMssSearchClicked("templates", sSearchText)
  };

  var handleElementTagCheckAllChanged = function(oEvent, oInfo) {
    SettingScreenStore.handleElementTagCheckAllChanged(oInfo);
  };

  var handleElementFilterableOrSortableCheckAllChanged = function(oEvent, oInfo) {
    SettingScreenStore.handleElementFilterableOrSortableCheckAllChanged(oInfo);
  };

  var handleAvailableListViewLoadMoreClicked = function(sContext) {
    SettingScreenStore.handleAvailableListViewLoadMoreClicked(sContext);
  };

  var handleAvailableListViewSearched = function(sSearchText) {
    SettingScreenStore.handleAvailableListViewSearched(sSearchText);
  };

  var handlePropertyCollapsedStatusToggled = function (sEntityType) {
    SettingScreenStore.handlePropertyCollapsedStatusToggled(sEntityType);
  };

  var handleTemplateHeaderVisibilityToggled = function (sType, sContext) {
    SettingScreenStore.handleTemplateHeaderVisibilityToggled(sType, sContext);
  };

  var handleTemplateHeaderTabClicked = function (sTabType) {
    SettingScreenStore.handleTemplateHeaderTabClicked(sTabType);
  };

  var handleTemplateHeaderTabIconChanged = function (sTabType, aFiles) {
    SettingScreenStore.handleTemplateHeaderTabIconChanged(sTabType, aFiles);
  };

  var handleTemplateSectionAdded = function (sSectionId) {
    SettingScreenStore.handleTemplateSectionAdded(sSectionId);
  };

  var handleTemplateSectionDropDownLoadMoreSearch = function(oData){
    SettingScreenStore.handleTemplateDropDownLoadMoreSearchClicked(oData);
  };

  var handleSelectedEntityRemoved = function (sContext, sId) {
    SettingScreenStore.handleSelectedEntityRemoved(sContext, sId);
  };

  var handleCustomTemplateSelectedEntitiesChanged = function (aSelectedItems, sContext) {
    SettingScreenStore.handleCustomTemplateSelectedEntitiesChanged(aSelectedItems, sContext);
  };

  var handleCustomTemplatePopoverOpen = function (sContext) {
    SettingScreenStore.handleCustomTemplatePopoverOpen(sContext);
  };

  var handleCustomTemplateLoadMoreClicked = function (aEntityList) {
    SettingScreenStore.handleCustomTemplateLoadMoreClicked(aEntityList);
  };

  let handleCustomTemplateSearchClicked = (aEntityList, sSearchText) => {
    SettingScreenStore.handleCustomTemplateSearchClicked(aEntityList, sSearchText);
  };

  var handleTemplateSectionActionButtonClicked = function (sButtonType, sTemplateId) {
    //Warning: below function is removed
  };

  var handlePermissionOfTemplateHeaderToggled = function (sType, sContext, sId) {
    SettingScreenStore.handlePermissionOfTemplateHeaderToggled(sType, sContext, sId);
  };

  var handleTableExpandCollapsedClicked = function (sSectionId) {
    SettingScreenStore.handleTableExpandCollapsedClicked(sSectionId);
  };

  let handleTableButtonClicked = function (sTableId, sRowId, sCellId, sScreenContext) {
    SettingScreenStore.handleTableButtonClicked(sTableId, sRowId, sCellId, sScreenContext);
  };

  var handlePermissionSectionStatusChanged = function (sStatus, sSectionId, aElements) {
    SettingScreenStore.handlePermissionSectionStatusChanged(sStatus, sSectionId, aElements);
  };

  var handlePermissionElementStatusChanged = function (sStatus, sSectionId, sElementType, sParentSectionId) {
    SettingScreenStore.handlePermissionElementStatusChanged(sStatus, sSectionId, sElementType, sParentSectionId);
  };

  let handleRoleConfigPermissionDialogButtonClicked = (sButtonId) => {
    SettingScreenStore.handleRoleConfigPermissionDialogButtonClicked(sButtonId);
  };

  var handleRuleNameChanged = function (oNothing, sValue, sScreenContext, sContext) {
    SettingScreenStore.handleRuleNameChanged(sValue, sContext);
  };

  var handleRuleElementDeleteButtonClicked = function (oNothing, sElementId, sContext, sScreenContext, sHandlerContext) {
    SettingScreenStore.handleRuleElementDeleteButtonClicked(sElementId, sContext, sHandlerContext, sScreenContext);
  };

  var handleRuleAttributeValueChanged = function (oNothing, sElementId, sValueId, sValue,sContext, sScreenContext) {
    SettingScreenStore.handleRuleAttributeValueChanged(sElementId, sValueId, sValue,sContext, sScreenContext);
  };

  var handleRuleAttributeColorValueChanged = function (oNothing, sElementId, sValueId, sValue) {
    SettingScreenStore.handleRuleAttributeColorValueChanged(sElementId, sValueId, sValue);
  };

  var handleRuleAttributeDescriptionValueChanged = function (oNothing, sElementId, sValueId, sValue,sContext) {
    SettingScreenStore.handleRuleAttributeDescriptionValueChanged(sElementId, sValueId, sValue,sContext);
  };

  var handleRuleAttributeDescriptionValueChangedInNormalization = function (oContext, sAttrId, oAttrVal, sContext, sVal, sValAsHtml) {
    SettingScreenStore.handleRuleAttributeDescriptionValueChangedInNormalization(sAttrId, oAttrVal, sContext, sVal, sValAsHtml);
  };

  var handleRuleAttributeValueChangedInNormalization = function (oContext, sAttrId, oAttrVal, sContext, sVal) {
    SettingScreenStore.handleRuleAttributeValueChangedInNormalization(sAttrId, oAttrVal, sContext, sVal);
  };

  var handleRuleUserValueChanged = function (oExtraData, aNewValue, sScreenContext) {
    SettingScreenStore.handleRuleUserValueChanged(oExtraData, aNewValue, sScreenContext);
  };

  var handleRoleCauseEffectMSSValueChanged = function (sContext, aNewValue, sExtraData) {
    SettingScreenStore.handleRoleCauseEffectMSSValueChanged(sContext, aNewValue, sExtraData);
  };

  var handleAttributeCauseEffectMSSValueChanged = function (sContext, aNewValue, sScreenName, oReferencedData) {
    SettingScreenStore.handleAttributeCauseEffectMSSValueChanged(sContext, aNewValue, sScreenName, oReferencedData);
  };

  var handleTagCauseEffectMSSValueChanged = function (sContext, aNewValue, sExtraData, sScreenName) {
    SettingScreenStore.handleTagCauseEffectMSSValueChanged(sContext, aNewValue, sExtraData, sScreenName);
  };

  var handleRuleUserValueForNormalizationChanged = function (sRoleId, aUsers) {
    SettingScreenStore.handleRuleUserValueForNormalizationChanged(sRoleId, aUsers);
  };

  var handleRuleDetailMssValueChanged = function (aNewValue, sKey) {
    SettingScreenStore.handleRuleDetailMssValueChanged(aNewValue, sKey);
  };

  var handleEntitiesAddedInMergeSection = function (sContext, aSelectedEntities, sSelectedEntityType, sSelectedEntity, oReferencedData) {
    SettingScreenStore.handleEntitiesAddedInMergeSection(sContext, aSelectedEntities, sSelectedEntityType, sSelectedEntity, oReferencedData);
  };

  var handleLatestEntityValueSelectionToggled = function (sContext, sSelectedEntity) {
    SettingScreenStore.handleLatestEntityValueSelectionToggled(sContext, sSelectedEntity);
  };

  var handleGoldenRecordSelectedEntityRemoved = function (sContext, sEntityId) {
    SettingScreenStore.handleGoldenRecordSelectedEntityRemoved(sContext, sEntityId);
  };

  var handleGoldenRecordSelectedSupplierRemoved = function (sSelectedSupplierId, sContext, sSelectedEntityId) {
    SettingScreenStore.handleGoldenRecordSelectedSupplierRemoved(sSelectedSupplierId, sContext, sSelectedEntityId);
  };

  var handleRuleDetailSupplierSequenceChanged = function (aNewSupplierSequence, sContext, sEntityId) {
    SettingScreenStore.handleRuleDetailSupplierSequenceChanged(aNewSupplierSequence, sContext, sEntityId);
  };

  var handleRuleRightPanelBarItemClicked = function (oNothing, sIconContext, sScreenContext) {
    SettingScreenStore.handleRuleRightPanelBarItemClicked(sIconContext);
  };

  var handleRuleRemoveBlackListIconClicked = function (oNothing, sDropId, sScreenContext) {
    SettingScreenStore.handleRuleRemoveBlackListIconClicked(sDropId);
  };

  var handleAttributeVisibilityButtonClicked = function (oContext,  sAttrId, oAttrCondition, sScreenContext) {
    SettingScreenStore.handleAttributeVisibilityButtonClicked(sAttrId, oAttrCondition, sScreenContext);
  };

  var handleCompareWithSystemDateButtonClicked = function (sContext, sRuleId, sAttrId, oAttrCondition) {
    SettingScreenStore.handleCompareWithSystemDateButtonClicked(sContext, sRuleId, sAttrId, oAttrCondition);
  };

  var handleAttributeViewTypeChanged = function (oContext, sAttrId, oAttributeCondition, sSelectedAttrId, oReferencedAttributes, oExtraData) {
    SettingScreenStore.handleAttributeViewTypeChanged(sAttrId, oAttributeCondition, sSelectedAttrId, oReferencedAttributes, oExtraData);
  };

  let handleConcatenatedFormulaChanged = function (oNormalization, aAttributeConcatenatedList, oReferencedData) {
    SettingScreenStore.handleConcatenatedFormulaChanged(oNormalization, aAttributeConcatenatedList, oReferencedData);
  };

  var handleRuleAddAttributeClicked = function (oContext, sAttributeId,sContext, sScreenContext) {
    SettingScreenStore.handleRuleAddAttributeClicked(sAttributeId, sContext, sScreenContext);
  };

  var handleRuleAttributeValueTypeChanged = function (oNothing, sAttributeId, sValueId, sTypeId,sContext, sScreenContext, isCause) {
    SettingScreenStore.handleRuleAttributeValueTypeChanged(sAttributeId, sValueId, sTypeId,sContext, sScreenContext, isCause);
  };

  var handleRuleAttributeValueDeleteClicked = function (oNothing, sAttributeId, sValueId,sContext,sScreenContext) {
    SettingScreenStore.handleRuleAttributeValueDeleteClicked(sAttributeId, sValueId,sContext,sScreenContext);
  };

  var handleRuleAttributeRangeValueChanged = function (oNothing, sAttributeId, sValueId, sValue, sRange, sScreenContext) {
    SettingScreenStore.handleRuleAttributeRangeValueChanged(sAttributeId, sValueId, sValue, sRange, sScreenContext);
  };

  var handleAttributeValueChangedForRangeInNormalization = function (oNothing, sAttributeId, sValueId, sValue, sType, sRange) {
    SettingScreenStore.handleAttributeValueChangedForRangeInNormalization(sAttributeId, sValueId, sValue, sType, sRange);
  };

  let handleRuleDetailPartnerApplyClicked = function (aSelectedItems, sContext) {
    SettingScreenStore.handleRuleDetailPartnerApplyClicked(aSelectedItems, sContext);
  };

  let handleRuleDetailEndpointsChanged = function (aSelectedItems,sContext) {
    SettingScreenStore.handleRuleDetailEndpointsChanged(aSelectedItems, sContext);
  };

  var handleTagGroupTagValueChanged = function(sTagId, aTagValueRelevanceData, oExtraData, oFilterContext){
    SettingScreenStore.handleTagGroupTagValueChanged(sTagId, aTagValueRelevanceData, oExtraData, oFilterContext);
  };

  var handleProcessComponentDataSourceValueChanged = function (sKey, sValue) {
    SettingScreenStore.handleProcessComponentDataSourceValueChanged(sKey, sValue);
  };

  var handleProcessComponentClassInfoValueChanged = function (sKey, sValue, oReferencedData) {
    SettingScreenStore.handleProcessComponentClassInfoValueChanged(sKey, sValue, oReferencedData);
  };

  var handleProcessComponentClassInfoTaxonomyValueChanged = function (sName, iIndex, sValue) {
    SettingScreenStore.handleProcessComponentClassInfoTaxonomyValueChanged(sName, iIndex, sValue);
  };

  var handleProcessComponentClassInfoAddTaxonomy = function (sName) {
    SettingScreenStore.handleProcessComponentClassInfoAddTaxonomy(sName);
  };

  var handleProcessComponentClassInfoRemoveTaxonomy = function (sName, iIndex) {
    SettingScreenStore.handleProcessComponentClassInfoRemoveTaxonomy(sName, iIndex);
  };

  var handleProcessComponentClassInfoAddTagGroup = function (sName) {
    SettingScreenStore.handleProcessComponentClassInfoAddTagGroup(sName);
  };

  var handleProcessComponentClassInfoRemoveTagGroup = function (sName, iIndex) {
    SettingScreenStore.handleProcessComponentClassInfoRemoveTagGroup(sName, iIndex);
  };

  var handleProcessComponentClassInfoAddAttributeGroup = function (sName) {
    SettingScreenStore.handleProcessComponentClassInfoAddAttributeGroup(sName);
  };

  var handleProcessComponentClassInfoRemoveAttributeGroup = function (sName, iIndex) {
    SettingScreenStore.handleProcessComponentClassInfoRemoveAttributeGroup(sName, iIndex);
  };

  var handleProcessDesignChanged = function (xml) {
    SettingScreenStore.handleProcessDesignChanged(xml);
  };

  let handleSetProcessDefinitions = function (oModeler) {
    SettingScreenStore.handleSetProcessDefinitions(oModeler);
  };

  let handleBPMNUploadDialogStatusChanged = function (bStatus) {
    SettingScreenStore.handleBPMNUploadDialogStatusChanged(bStatus);
  };

  let handleBPMNXMLUpload = function (sXML) {
    SettingScreenStore.handleBPMNXMLUpload(sXML);
  };

  let handleMenuListExpandToggled = function (sItemId, sContext) {
    SettingScreenStore.handleMenuListExpandToggled(sItemId, sContext);
  };

  let handleUserCreateClicked = function (oModel) {
    SettingScreenStore.handleUserCreateClicked(oModel);
  };

  var handleProcessDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleProcessDialogButtonClicked(sButtonId);
  };

  let handleSaveAsDialogValueChanged = function (sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData, sSubType) {
    SettingScreenStore.handleSaveAsDialogValueChanged(sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData, sSubType);
  };

  let handleWorkflowTypeClicked = function (aSelectedItem) {
    SettingScreenStore.handleWorkflowTypeClicked(aSelectedItem);
  };

  let handleEntityTypeClicked = function (aSelectedItem) {
    SettingScreenStore.handleEntityTypeClicked(aSelectedItem);
  };

  let handleEndpointDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleEndpointDialogButtonClicked(sButtonId);
  };

  var handleAddNewBlackListItem = function (oNothing, sValue, sContext) {
    SettingScreenStore.handleAddNewBlackListItem(sValue, sContext);
  };

  var handleRemoveBlackListItem = function (oNothing, iIndex, sContext) {
    SettingScreenStore.handleRemoveBlackListItem(iIndex, sContext);
  };

  var handleEditBlackListItem = function (oNothing, iIndex, sValue, sContext) {
    SettingScreenStore.handleEditBlackListItem(iIndex, sValue, sContext);
  };

  var handleRuleListLabelChanged = function (oNothing, sNewValue) {
    SettingScreenStore.handleRuleListLabelChanged(sNewValue);
  };

  var handleAttributionTaxonomyDialogClose = function (sContext) {
    SettingScreenStore.handleDetailedTaxonomyDialogClose(sContext);
  };

  var handleAttributionTaxonomyHierarchyToggleAutomaticScrollProp = function () {
    SettingScreenStore.handleAttributionTaxonomyHierarchyToggleAutomaticScrollProp();
  };

  var handleTaxonomyLevelItemClicked = function (iIndex, sItemId, sContext) {
    SettingScreenStore.handleTaxonomyLevelItemClicked(iIndex, sItemId, sContext);
  };

  var handleTaxonomyLevelItemLabelChanged = function (iIndex, sItemId, sLabel, sContext) {
    SettingScreenStore.handleTaxonomyLevelItemLabelChanged(iIndex, sItemId, sLabel, sContext);
  };

  var handleTaxonomyLevelActionItemClicked = function (iIndex, sItemId, sContext) {
    SettingScreenStore.handleTaxonomyLevelActionItemClicked(iIndex, sItemId, sContext);
  };

  var handleTaxonomyLevelChildActionItemClicked = function (iIndex, sActionId, sChildId, sContext) {
    SettingScreenStore.handleTaxonomyLevelChildActionItemClicked(iIndex, sActionId, sChildId, sContext);
  };

  var handleTaxonomyLevelMasterListChildrenAdded = function (iIndex, aCheckedItems, sContext, sNewLabel, oReferencedData) {
    SettingScreenStore.handleTaxonomyLevelMasterListChildrenAdded(iIndex, aCheckedItems, sContext, sNewLabel, oReferencedData);
  };

  var handleTaxonomyAddChildButtonClicked = function (bPopoverVisible, sTagGroupId, sTaxonomyId, sContext) {
    SettingScreenStore.handleTaxonomyAddChildButtonClicked(bPopoverVisible, sTagGroupId, sTaxonomyId, sContext);
  };

  // Revert changes for defaultUnit & precision

  var handleMetricUnitChanged = function (sSelectedUnit, oSectionElementDetails) {
    SettingScreenStore.handleMetricUnitChanged(sSelectedUnit, oSectionElementDetails);
  };

  var handleGridPropertyValueChanged = function (sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData, sContext,) {
    SettingScreenStore.handleGridPropertyValueChanged(sContext, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData);
  };

  var handleGridPropertyKeyDownEvent = function (sKey, sContentId, sPathToRoot, sGridContext) {
    SettingScreenStore.handleGridPropertyKeyDownEvent(sKey, sContentId, sPathToRoot, sGridContext);
  };

  var handleGridParentExpandToggled = function (sContentId, sGridContext) {
    SettingScreenStore.handleGridParentExpandToggled(sContentId, sGridContext);
  };

  var handleGridPropertyClearShouldFocus = function (sContentId, sPathToRoot, sGridContext) {
    SettingScreenStore.handleGridPropertyClearShouldFocus(sContentId, sPathToRoot, sGridContext);
  };

  var handleGridImagePropertyImageChanged = function (sContentId, sPropertyId, aFiles, sPathToRoot, bLimitImageSize, sContext) {
    SettingScreenStore.handleGridImagePropertyImageChanged(sContentId, sPropertyId, aFiles, sPathToRoot, bLimitImageSize, sContext);
  };

  var handleGridMSSAdditionalListItemAdded = function (sContentId, aCheckedItems, sContext, oReferencedData, sGridContext) {
    SettingScreenStore.handleGridMSSAdditionalListItemAdded(sContentId, aCheckedItems, sContext, oReferencedData, sGridContext);
  };

  let handleGetAssetExtensions = function (oRefDom, sNatureType, sContentId, sPropertyId, sContext, sPathToRoot) {
    SettingScreenStore.handleGetAssetExtensions(oRefDom, sContentId, sPropertyId, sContext, sPathToRoot);
  };

  let handleGridFilterApplyClicked = function (oAppliedFilterData, oReferencedData) {
    SettingScreenStore.handleGridFilterApplyClicked(oAppliedFilterData, oReferencedData);
  };

  let handleGridOrganizeColumnButtonClicked = function (sTableContextId, sGridContext) {
    SettingScreenStore.handleGridOrganizeColumnButtonClicked(sGridContext);
  };

  var handleGridViewSelectButtonClicked = function (aSelectedContentIds, bSelectAllClicked, sContext) {
    SettingScreenStore.handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked, sContext);
  };

  var handleGridViewActionItemClicked = function (sActionItemId, sContentId, sContext) {
    SettingScreenStore.handleGridViewActionItemClicked(sActionItemId, sContentId, sContext);
  };

  var handleGridViewColumnActionItemClicked = function (sActionItemId, sColumnId) {
    SettingScreenStore.handleGridViewColumnActionItemClicked(sActionItemId, sColumnId);
  };

  var handleGridViewDeleteButtonClicked = function (sContext) {
    SettingScreenStore.handleGridViewDeleteButtonClicked(sContext);
  };

  let handleManageEntityMultiUsageButtonClicked = function (sGridContext) {
    SettingScreenStore.handleManageEntityMultiUsageButtonClicked(sGridContext);
  }

  var handleGridViewSortButtonClicked = function (sContext) {
    SettingScreenStore.handleGridViewSortButtonClicked(sContext);
  };

  var handleGridViewCreateButtonClicked = function (sContext) {
    SettingScreenStore.handleGridViewCreateButtonClicked(sContext);
  };

  var handleGridViewExportButtonClicked = function (sGridContext) {
    SettingScreenStore.handleGridViewExportButtonClicked(sGridContext);
  };

  let handleGridViewDownloadButtonClicked = function (sGridContext) {
    SettingScreenStore.handleGridViewDownloadButtonClicked(sGridContext);
  };

  let handleGridViewFilterButtonClicked = function (bShowFilterView, sGridContext) {
    SettingScreenStore.handleGridViewFilterButtonClicked(bShowFilterView, sGridContext);
  };

  let handleGridViewResizerButtonClicked = function (iWidth, sId, sTableContextId, sContext) {
    SettingScreenStore.handleGridViewResizerButtonClicked(iWidth, sId, sContext);
  };

  var handleGridViewRefreshButtonClicked = function (sContext) {
    SettingScreenStore.handleGridViewRefreshButtonClicked(sContext);
  };

  var handleSsoRefreshButtonClicked = function(){
      SettingScreenStore.handleSsoRefreshButtonClicked();
  }

  var handleGridViewSaveAsButtonClicked = function (sContext) {
    SettingScreenStore.handleGridViewSaveAsButtonClicked(sContext);
  };

  var handleTagViewDataLanguageClicked = function (aSelectedItemId) {
    SettingScreenStore.handleTagViewDataLanguageClicked(aSelectedItemId);
  };

  var handleGridViewImportButtonClicked = function (aFiles, sContext) {
    SettingScreenStore.handleGridViewImportButtonClicked(aFiles, sContext);
  };

  var handleAttributionTaxonomyImportButtonClicked = function (aFiles) {
    SettingScreenStore.handleAttributionTaxonomyImportButtonClicked(aFiles);
  };

  let handleAttributionTaxonomyTabChanged = (sTabId) => {
    SettingScreenStore.handleAttributionTaxonomyTabChanged(sTabId);
  };

  var handleGridPaginationChanged = function (oNewPaginationData, sContext) {
    SettingScreenStore.handleGridPaginationChanged(oNewPaginationData, sContext);
  };

  var handleGridViewSearchTextChanged = function (sSearchText, sContext) {
    SettingScreenStore.handleGridViewSearchTextChanged(sSearchText, sContext);
  };

  var handleGridViewSaveButtonClicked = function (sContext) {
    SettingScreenStore.handleGridViewSaveButtonClicked(sContext);
  };

  var handleGridViewDiscardButtonClicked = function (sContext) {
    SettingScreenStore.handleGridViewDiscardButtonClicked(sContext);
  };

  var handleGridViewColumnHeaderClicked = function (sColumnId, sContext) {
    SettingScreenStore.handleGridViewColumnHeaderClicked(sColumnId, sContext);
  };

  var handleTranslationPropertyChanged = function (sId, sContext) {
    SettingScreenStore.handleTranslationPropertyChanged(sId, sContext);
  };

  var handleSelectedChildModuleChanged = function (sSelectedClassModuleId) {
    SettingScreenStore.handleSelectedChildModuleChanged(sSelectedClassModuleId);
  };

  var handleTranslationLanguagesChanged = function (aLanguages) {
    SettingScreenStore.handleTranslationLanguagesChanged(aLanguages);
  };

  var handleNewMSSViewPopOverClosed = function (aSelectedItems, sContext, oReferencedData) {
    SettingScreenStore.handleNewMSSViewPopOverClosed(aSelectedItems, sContext, oReferencedData);
  };

  var handleMssSearchClicked = function (sContext, sSearchText) {
    SettingScreenStore.handleMssSearchClicked(sContext, sSearchText);
  };

  var handleMssLoadMoreClicked = function (sContext) {
    SettingScreenStore.handleMssLoadMoreClicked(sContext);
  };

  var handleMssPopOverOpened = function (sContext) {
    let sSplitter = SettingUtils.getSplitter(); //todo : used setting utils
    let aSplitContext = sContext ? sContext.split(sSplitter) : [];
    switch (aSplitContext[0]) {

      case 'tab':
      case 'tabId':
        SettingScreenStore.loadInitialEntityData(aSplitContext[0]);
        break;

      case 'dashboardTab':
        SettingScreenStore.loadInitialEntityData(sContext);
        break;

     /* case ViewContextConstants.RULE_DETAILS_VIEW_CONFIG:
        SettingScreenStore.handleRuleDetailsMSSSearchTextChanged(aSplitContext[1], "");
        break;*/

      case "kpiConfigDetail":
        if (aSplitContext[1] !== "attributes" && aSplitContext[1] !== "tags") {
          SettingScreenStore.handleRuleDetailsMSSSearchTextChanged(aSplitContext[1], "");
        }
        break;

      case "systems":
        SettingScreenStore.handleSystemsMSSLoadList(aSplitContext[1]);
        break;

      case "gridTagLinkedMasterTag":
        SettingScreenStore.handleTagLinkedMasterTagPopoverOpen(aSplitContext[1]);
        break;

      case "ruleDataLanguages":
        SettingScreenStore.handleRuleDataLanguagesPopoverOpen();
        break;

    }
  };

  var handleMssCreateClicked = function (sContext, sSearchText) {
    SettingScreenStore.handleMssCreateClicked(sContext, sSearchText);
  };

  var handleGridPropertyValueCreated = function (sPropertyId, sSearchText, sContentId, sPathToRoot, sGridContext) {
    SettingScreenStore.handleGridPropertyValueCreated(sPropertyId, sSearchText, sContentId, sPathToRoot, sGridContext);
  };

  var handleKPIMssCreateClicked = function (sPropertyId, sActiveKpiId, sSearchText) {
    SettingScreenStore.handleKPIMssCreateClicked(sPropertyId, sActiveKpiId, sSearchText);
  };

  var handleDataTransferPropertyCouplingChange = function (sSide, sPropertyId, sNewValue, sContext, sRelationshipId, sParentContext) {
    SettingScreenStore.handleDataTransferPropertyCouplingChange(sSide, sPropertyId, sNewValue, sContext, sRelationshipId, sParentContext);
  };

  var handleDataTransferSearchLoadMore = function (oData) {
    SettingScreenStore.handleDataTransferSearchLoadMore(oData);
  };

  let handleDataTransferAddProperties = (sEntity, aSelectedIds, oReferencedData, sContext) => {
    SettingScreenStore.handleDataTransferAddProperties(sEntity, aSelectedIds, oReferencedData, sContext);
  };

  var handleDataTransferPropertyRemove = function (sSide, sPropertyId, sContext, sRelationshipId, sParentContext) {
    SettingScreenStore.handleDataTransferPropertyRemove(sSide, sPropertyId, sContext, sRelationshipId, sParentContext);
  };

  var handleContextualTagCombinationUniqueSelectionChanged = function (sUniqueSelectionId, sContext) {
    SettingScreenStore.handleContextualTagCombinationUniqueSelectionChanged(sUniqueSelectionId, sContext);
  };

  var handleContextualTagCombinationUniqueSelectionDelete = function (sUniqueSelectionId, sContext) {
    SettingScreenStore.handleContextualTagCombinationUniqueSelectionDelete(sUniqueSelectionId, sContext);
  };

  var handleContextualTagCombinationUniqueSelectionOk = function (sUniqueSelectionId, sContext) {
    SettingScreenStore.handleContextualTagCombinationUniqueSelectionOk(sUniqueSelectionId, sContext);
  };

  var handleRemoveSelectedTagButtonClicked = function(sContext, sTagGroupId){
    SettingScreenStore.handleRemoveSelectedTagButtonClicked(sContext, sTagGroupId);
  };

  var handleTagValueListApplyButtonClicked = function(sContext, aSelectedItemIds){
    SettingScreenStore.handleTagValueListApplyButtonClicked(sContext, aSelectedItemIds);
  };

  var handleTagListApplyButtonClicked = function(sContext, aSelectedTagItems){
    SettingScreenStore.handleTagListApplyButtonClicked(sContext, aSelectedTagItems);
  };

  var handleTagListLoadMoreClicked = function(sContext){
    SettingScreenStore.handleTagListLoadMoreClicked(sContext);
  };

  var handleTagListSearchTextClicked = function(sContext, sSearchText){
    SettingScreenStore.handleTagListSearchTextClicked(sContext, sSearchText);
  };

  let handleGetAllowedAttributesForCalculatedAttribute = function (oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, bIsForReload) {
    SettingScreenStore.handleGetAllowedAttributesForCalculatedAttribute(oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, bIsForReload);
  };

  let handleGridCalculatedAttributePopoverOpened = function (oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sAttributeId) {
    SettingScreenStore.handleGetAllowedAttributesForCalculatedAttribute(oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sAttributeId, false);
  };

  var handleClassContextDialogClosed = function(){
    SettingScreenStore.handleClassContextDialogClosed();
  };

  var handleClassContextDialogOkClicked = function(){
    SettingScreenStore.handleClassContextDialogOkClicked();
  };

  let handleClassContextDialogDiscardClicked = function(){
    SettingScreenStore.handleClassContextDialogDiscardClicked();
  };

  var handleClassContextDialogOpenClicked = function(){
    SettingScreenStore.handleClassContextDialogOpenClicked();
  };

  var handleAttributionTaxonomyCreateDialogButtonClicked = function (sContext, sIsMajorMinor, sLabel) {
    SettingScreenStore.handleAttributionTaxonomyCreateDialogButtonClicked(sContext, sIsMajorMinor, sLabel);
  };

  var handleAttributionTaxonomyListLevelAdded = function (sSelectedId, bIsNewlyCreated, sLabel) {
    SettingScreenStore.handleAttributionTaxonomyListLevelAdded(sSelectedId, bIsNewlyCreated, sLabel);
  };

  var handleAttributionTaxonomyListLevelAddButtonClicked = function (bPopoverStatus) {
    SettingScreenStore.handleAttributionTaxonomyListLevelAddButtonClicked(bPopoverStatus);
  };

  //KPI Config Start
  let handleKPIConfigDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleKPIConfigDialogButtonClicked(sButtonId);
  };

  let handleKPIConfigDialogTabChanged = function (sTabId) {
    SettingScreenStore.handleKPIConfigDialogTabChanged(sTabId);
  };

  //Context Config Start
  let handleContextConfigDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleContextConfigDialogButtonClicked(sButtonId);
  };

  let handleAddTagCombinationsClicked = function () {
    SettingScreenStore.handleAddTagCombinationsClicked();
  };

  //Mapping Config Start
  let handleMappingConfigDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleMappingConfigDialogButtonClicked(sButtonId);
  };

  let handleMappingSaveAsDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleMappingSaveAsDialogButtonClicked(sButtonId);
  };

  let handleAuthorizationMappingConfigDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleAuthorizationMappingConfigDialogButtonClicked(sButtonId);
  };

  let handleAuthorizationMappingApplyButtonClicked = function (sContext, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleAuthorizationMappingApplyButtonClicked(sContext, aSelectedItems, oReferencedData);
  };

  let handleAuthorizationMappingCheckboxButtonClicked = function (bIsCheckboxClicked, sContext) {
    SettingScreenStore.handleAuthorizationMappingCheckboxButtonClicked(bIsCheckboxClicked, sContext);
  };

  let handleAuthorizationMappingDeleteButtonClicked = function (sSelectedElement, sContext) {
    SettingScreenStore.handleAuthorizationMappingDeleteButtonClicked(sSelectedElement, sContext);
  };

  let handleAuthorizationMappingToggleButtonClicked = function (bIsActivationEnable, oElementData) {
    SettingScreenStore.handleAuthorizationMappingToggleButtonClicked(bIsActivationEnable, oElementData);
  };

  let handleRuleConfigDialogActionButtonClicked = function (sTabId) {
    SettingScreenStore.handleRuleConfigDialogActionButtonClicked(sTabId);
  };

  var handleAllowedTypesDropdownOpened = function (sContext, sTaxonomyId,sOtherTaxonomyId) {
    SettingScreenStore.handleAllowedTypesDropdownOpened(sContext, sTaxonomyId,sOtherTaxonomyId);
  };

  var handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oContext, oTaxonomy, sActiveTaxonomyId, sContext) {
    SettingScreenStore.handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sActiveTaxonomyId, sContext);
  };

  var handleAddTaxonomyPopoverItemClicked = function (oContext, oModel, sTaxonomyId, sContext) {
    SettingScreenStore.handleAddTaxonomyPopoverItemClicked(oModel, sTaxonomyId, sContext);
  };

  let handleAddTaxonomySearch = function (sTaxonomyType, sSearchText, sTaxonomyId, sContext) {
    SettingScreenStore.handleAddTaxonomySearch(sTaxonomyType, sSearchText, sTaxonomyId, sContext);
  };

  let handleAddTaxonomyLoadMore = function (sTaxonomyType, sSearchText, sTaxonomyId, sContext) {
    SettingScreenStore.handleAddTaxonomyLoadMore(sTaxonomyType, sSearchText, sTaxonomyId, sContext);
  };

  var handleKPIMSSValueChanged = function (sRuleId, sKey, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleKPIMSSValueChanged(sRuleId, sKey, aSelectedItems, oReferencedData);
  };

  let handleKPIPartnerApplyClicked = function (aSelectedList, oReferencedData) {
    SettingScreenStore.handleKPIPartnerApplyClicked(aSelectedList, oReferencedData);
  };

  var handleAddNewKPIDrilldownLevelClicked = function (sLevelType) {
    SettingScreenStore.handleAddNewKPIDrilldownLevelClicked(sLevelType);
  };

  var handleRemoveKPIDrilldownLevelClicked = function (sDrillDownLevelId) {
    SettingScreenStore.handleRemoveKPIDrilldownLevelClicked(sDrillDownLevelId);
  };

  var handleKPIDrilldownMssValueChanged = function (sDrillDownLevelId, aSelectedIds, oReferencedTags) {
    SettingScreenStore.handleKPIDrilldownMssValueChanged(sDrillDownLevelId, aSelectedIds, oReferencedTags);
  };

  var handleKPIAddRuleClicked = function (sRuleBlockType, sRuleBlockId) {
    SettingScreenStore.handleKPIAddRuleClicked(sRuleBlockType, sRuleBlockId);
  };

  var handleKPIThresholdValueChanged = function (sActiveBlockId, sThresholdType, sUnitType, sThresholdValue) {
    SettingScreenStore.handleKPIThresholdValueChanged(sActiveBlockId, sThresholdType, sUnitType, sThresholdValue);
  };

  var handleDeleteRuleClicked = function (sRuleId) {
    SettingScreenStore.handleDeleteRuleClicked(sRuleId);
  };

  let handleRolePermissionToggleButtonClicked = function () {
    SettingScreenStore.handleRolePermissionToggleButtonClicked();
  };

  let handleListNodeViewNodeDeleteButtonClicked = function (sId, sContext) {
    SettingScreenStore.handleListNodeViewNodeDeleteButtonClicked(sId, sContext);
  };

  let handleRolePermissionSaveRoleClicked = function () {
    SettingScreenStore.handleRolePermissionSaveRoleClicked();
  };

  let handleListViewNewActionItemClicked = function (sId, sContext) {
    SettingScreenStore.handleListViewNewActionItemClicked(sId, sContext);
  };

  //KPI Config End

  let handleOrganisationConfigTabChanged = function (sTabId) {
    SettingScreenStore.handleOrganisationTabChanged(sTabId);
  };

  let handleTabsConfigDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleTabsConfigDialogButtonClicked(sButtonId)
  };

  let handleTabsSortDialogButtonClicked = function () {
    SettingScreenStore.handleTabsSortDialogButtonClicked()
  };

  let handleTabsConfigListItemShuffled = function (sId, iNewPosition) {
    SettingScreenStore.handleTabsConfigListItemShuffled(sId, iNewPosition);
  };

  let handleTabsSortListItemShuffled = function (sId, iNewPosition, changedTab) {
    SettingScreenStore.handleTabsSortListItemShuffled(sId, iNewPosition, changedTab);
  };

  var handleEntityConfigDialogButtonClicked = function (sButtonId, sEntityType) {
    SettingScreenStore.handleEntityConfigDialogButtonClicked(sButtonId, sEntityType);
  };

  let handleManageEntityDialogButtonClicked = function (sButtonId, sEntityType) {
    SettingScreenStore.handleManageEntityDialogButtonClicked(sButtonId, sEntityType);
  };

  var handleConfigScreenTabClicked = function (sTabId) {
    SettingScreenStore.handleConfigScreenTabClicked(sTabId);
  };

  let handleSettingScreenLeftNavigationTreeItemClicked = function (sItemId, sParentId) {
    SettingScreenStore.handleSettingScreenLeftNavigationTreeItemClicked(sItemId, sParentId);
  };

  let handleCreateUserCancelClicked = function (oModel) {
    SettingScreenStore.handleCreateUserCancelClicked(oModel);
  };

  var handleContentHorizontalTreeNodeClicked = function  (oReqData) {
    SettingScreenStore.handleContentHorizontalTreeNodeClicked(oReqData);
  };

  var handleContentHorizontalTreeNodeDeleteClicked = function (sClickedNodeId, iLevel, sContext) {
    if(sContext === "languageTree") {
      SettingScreenStore.deleteLanguage(sClickedNodeId);
    } else if(sContext === "smartDocument") {
      SettingScreenStore.deleteSmartDocument(sClickedNodeId, iLevel);
    } else {
      SettingScreenStore.deleteClass(sClickedNodeId);
    }
  };

  let handleContentHorizontalTreeNodePermissionToggled = (sId, sProperty, sValue) => {
    SettingScreenStore.handleContentHorizontalTreeNodePermissionToggled(sId, sProperty, sValue);
  };

  var handleHorizontalHierarchyTreeActionItemClicked = function  (sAction, sNodeId, sContext) {
    if(sContext === "languageTree"){
      switch (sAction) {
        case "create":
          SettingScreenStore.createLanguage(sNodeId);
          break;
      }
    } else if (sContext === "smartDocument") {
      switch (sAction) {
        case "create":
          SettingScreenStore.createDummySmartDocumentSection(sNodeId);
          break;
      }
    } else {
      switch (sAction) {
        case "create":
          SettingScreenStore.createClass(sNodeId);
          break;
      }
    }
  };

  let handleHorizontalHierarchyTreeTabChanged = (sTabId, sContext) => {
    SettingScreenStore.handleHorizontalHierarchyTreeTabChanged(sTabId, sContext);
  };

  let handleUpdateViewProps = function (sContext, oUpdatedMssProps) {
    SettingScreenStore.handleUpdateViewProps(sContext, oUpdatedMssProps);
  };

  let handleHorizontalTreeNodeToggleAutomaticScrollProp = function () {
    SettingScreenStore.handleHorizontalTreeNodeToggleAutomaticScrollProp();
  };

  let handleSystemSelectApplyClicked = function (aSelectedSystemIds, sContext) {
    SettingScreenStore.handleSystemSelectApplyClicked(aSelectedSystemIds, sContext);
  };

  let handleEndpointSelectionViewSelectAllChecked = function (sSystemId, iCheckboxStatus, sContext) {
    SettingScreenStore.handleEndpointSelectionViewSelectAllChecked(sSystemId, iCheckboxStatus, sContext);
  };

  let handleEndpointSelectionViewDeleteClicked = function (sSystemId, sContext) {
    SettingScreenStore.handleEndpointSelectionViewDeleteClicked(sSystemId, sContext);
  };

  let handleEsc = (oContext, oEvent) => {
    SettingScreenStore.handleEsc(oEvent);
  };

  let handleRemoveKlassClicked = function (sSectionId, sClassId, sMainContext) {
    SettingScreenStore.handleRemoveKlassClicked(sSectionId, sClassId, sMainContext);
  };

  let handleClassDataTransferPropertiesAdded = function (sEntity, aSelectedIds, oReferencedData, sContext, sMainContext) {
    SettingScreenStore.handleClassDataTransferPropertiesAdded(sEntity, aSelectedIds, oReferencedData, sContext, sMainContext);
  };

  let handleClassDataTransferPropertiesRemoved = function (sClassId, sPropertyId, sContext, sMainContext) {
    SettingScreenStore.handleClassDataTransferPropertiesRemoved(sClassId, sPropertyId, sContext, sMainContext);
  };

  let handleClassDataTransferPropertiesCouplingChanged = function (sClassId, sPropertyId, sNewValue, sContext, sMainContext) {
    SettingScreenStore.handleClassDataTransferPropertiesCouplingChanged(sClassId, sPropertyId, sNewValue, sContext, sMainContext);
  };

  let handleUILanguageChanged = function () {
    SettingScreenStore.handleUILanguageChanged();
  };

  let handleConfigModuleSearchItemClicked = function (sSearchItem,sParentId,aPath) {
  };

  let handleConfigModuleSearchDialogClosedClicked = function () {
    SettingScreenStore.handleConfigModuleSearchDialogClosedClicked();
  };

  var handlePropertyCollectionImportButtonClicked = function (aFiles) {
    SettingScreenStore.handlePropertyCollectionImportButtonClicked(aFiles);
  };

  let handlePropertyCollectionTabChanged = (sTabId) => {
    SettingScreenStore.handlePropertyCollectionTabChanged(sTabId);
  };

  let handleLanguageTreeDialogCancelClicked = function () {
    SettingScreenStore.handleLanguageTreeDialogCancelClicked();
  };

  let handleLanguageTreeDialogCreateClicked = function () {
    SettingScreenStore.handleLanguageTreeDialogCreateClicked();
  };

  let handleSmartDocumentDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleSmartDocumentDialogButtonClicked(sButtonId);
  };

  let handleSmartDocumentSnackBarButtonClicked = function (sButtonId) {
    SettingScreenStore.handleSmartDocumentSnackBarButtonClicked(sButtonId);
  };

  let handleSmartDocumentPresetMSSValueClicked = function (sKey, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleSmartDocumentPresetMSSValueClicked(sKey, aSelectedItems, oReferencedData);
  };

  let handleThemeConfigurationSnackBarButtonClicked = function (sButtonId) {
    SettingScreenStore.handleThemeConfigurationSnackBarButtonClicked(sButtonId);
  };

  let handleViewConfigurationSnackBarButtonClicked = function (sButtonId) {
    SettingScreenStore.handleViewConfigurationSnackBarButtonClicked(sButtonId);
  };

  let handleLanguageTreeSnackBarButtonClicked = function (sButton) {
    SettingScreenStore.handleLanguageTreeSnackBarButtonClicked(sButton);
  };

  let handleLanguageTreeImportButtonClicked = function (aFiles) {
    SettingScreenStore.handleLanguageTreeConfigImportButtonClicked(aFiles);
  };

  let handleProcessConfigFullScreenButtonClicked = function () {
    SettingScreenStore.handleProcessConfigFullScreenButtonClicked();
  };

  let handleOrganisationImportButtonClicked = function (aFiles) {
    SettingScreenStore.handleOrganisationImportButtonClicked(aFiles);
  };

  let handleAdvancedSearchPanelCancelClicked = function () {
    SettingScreenStore.handleAdvancedSearchPanelCancelClicked();
  };

  let handleAdvancedSearchFilterButtonCLicked = function () {
    SettingScreenStore.handleAdvancedSearchFilterButtonCLicked();
  };

  let handleAdvancedSearchPanelClearClicked = function () {
    SettingScreenStore.handleAdvancedSearchPanelClearClicked();
  };

  let handleAppliedFilterClearClicked = function (sSelectedHierarchyContext, oFilterContext) {
    SettingScreenStore.handleAppliedFilterClearClicked(sSelectedHierarchyContext, oFilterContext);
  };

  let handleAdvancedSearchListSearched = function (sSearchText, sContext) {
    SettingScreenStore.handleAdvancedSearchListSearched(sSearchText, sContext);
  };

  let handleAdvancedSearchListItemNodeClicked = function (sItemId, sType, sContext) {
    SettingScreenStore.handleAdvancedSearchListItemNodeClicked(sItemId, sType, sContext);
  };

  let handleAdvancedSearchListLoadMoreClicked = function (sType, sContext) {
    SettingScreenStore.handleAdvancedSearchListLoadMoreClicked(sType, sContext);
  };

  let handleFilterElementDeleteClicked = function (sElementId) {
    SettingScreenStore.handleFilterElementDeleteClicked(sElementId);
  };

  let handleRemoveAppliedFilterClicked = function (sElementId, sContext, oExtraData, sSelectedHierarchyContext, oFilterContext) {
    SettingScreenStore.handleRemoveAppliedFilterClicked(sElementId, sContext, oExtraData, sSelectedHierarchyContext, oFilterContext);
  };

  let handleFilterElementExpandClicked = function (sElementId) {
    SettingScreenStore.handleFilterElementExpandClicked(sElementId);
  };

  let handleAddAttributeClicked = function (sElementId) {
    SettingScreenStore.handleAddAttributeClicked(sElementId);
  };

  let handleFilterAttributeValueDeleteClicked = function (sAttributeId, sValId) {
    SettingScreenStore.handleFilterAttributeValueDeleteClicked(sAttributeId, sValId);
  };

  let handleFilterAttributeValueTypeChanged = function (sAttributeId, sValId, sTypeId) {
    SettingScreenStore.handleFilterAttributeValueTypeChanged(sAttributeId, sValId, sTypeId);
  };

  let handleFilterAttributeValueChanged = function (sAttributeId, sValId, sVal) {
    SettingScreenStore.handleFilterAttributeValueChanged(sAttributeId, sValId, sVal);
  };

  let handleFilterAttributeValueChangedForRange = function (sAttributeId, sValId, sVal, sRange) {
    SettingScreenStore.handleFilterAttributeValueChangedForRange(sAttributeId, sValId, sVal, sRange);
  };

  let handleRelationshipInheritanceApplyClicked = function(sEntity, aSelectedIds, oReferencedData, sContext) {
    SettingScreenStore.handleRelationshipInheritanceApplyClicked(sEntity, aSelectedIds, oReferencedData, sContext);
  };

  let handleColumnOrganizerSaveButtonClicked = function(sContext, aSelectedOrganizedColumns) {
    SettingScreenStore.handleColumnOrganizerSaveButtonClicked(sContext, aSelectedOrganizedColumns);
  };

  let handleColumnOrganizerDialogButtonClicked = function() {
    SettingScreenStore.handleColumnOrganizerDialogButtonClicked();
  };

  let handleSectionListApplyButtonClicked = function (sContext, aSelectedItems, oReferencedData) {
    SettingScreenStore.handleSectionListApplyButtonClicked(sContext, aSelectedItems, oReferencedData);
  };

  let handleSectionListDeleteButtonClicked = function (sSelectedElement, sContext) {
    SettingScreenStore.handleSectionListDeleteButtonClicked(sSelectedElement, sContext);
  };

  let handleBPMNElementsAddRowButtonClicked = function (sName) {
    SettingScreenStore.handleBPMNElementsAddRowButtonClicked(sName);
  };

  let handleBPMNElementsRemoveRowButtonClicked = function (sName, iIndex) {
    SettingScreenStore.handleBPMNElementsRemoveRowButtonClicked(sName, iIndex);
  };

  let handleFilterChildToggled = function (sParentId, sChildId, sContext, oExtraData, oFilterContext) {
    SettingScreenStore.handleFilterChildToggled(sParentId, sChildId, sContext, oExtraData, oFilterContext);
  };

  let handleFilterSummarySearchTextChanged = function (oFilterContext, sFilterId, sSearchedText) {
    SettingScreenStore.handleFilterSummarySearchTextChanged(oFilterContext, sFilterId, sSearchedText);
  };

  let handleCollapseFiltersClicked = function(bExpanded, sContext) {
    SettingScreenStore.handleCollapseFilterClicked(bExpanded, sContext);
  };

  let handleApplyFilterButtonClicked = function(sContext) {
    SettingScreenStore.handleApplyFilterButtonClicked(sContext);
  };

  let handleFilterItemPopoverClosed = function(sContext) {
    SettingScreenStore.handleFilterItemPopoverClosed(sContext);
  };

  let handleDateRangePickerUpdateData = function (sContext, oRange, sId) {
    SettingScreenStore.handleDateRangePickerUpdateData(sContext, oRange, sId);
  };

  let handleFilterItemSearchTextChanged = function (sContext, sId, sSearchText) {
    SettingScreenStore.handleFilterItemSearchTextChanged(sContext, sId, sSearchText);
  };

  let handleBPMNElementsEntityMapChanged = function (sName, iIndex, sItemid, sNewVal) {
    SettingScreenStore.handleBPMNElementsEntityMapChanged(sName, iIndex, sItemid, sNewVal);
  };

  let handleGridViewShowExportStatusButtonClicked = function(sContext) {
    SettingScreenStore.handleGridViewShowExportStatusButtonClicked(sContext);
  };

  let handleAuditLogExportStatusDialogButtonClicked = function (sButtonId) {
    SettingScreenStore.handleAuditLogExportStatusDialogButtonClicked(sButtonId);
  };

  let handleAuditLogExportDialogRefreshButtonClicked = function() {
    SettingScreenStore.handleAuditLogExportDialogRefreshButtonClicked();
  };

  let handleAuditLogExportDialogPaginationChanged = function(oNewPaginationData) {
    SettingScreenStore.handleAuditLogExportDialogPaginationChanged(oNewPaginationData);
  };

  let handleDialogSaveButtonClicked = function () {
    SettingScreenStore.handleDialogSaveButtonClicked();
  };

  let handleIconElementCheckboxClicked = function(aSelectedIconIds, bIsSelectAllClicked) {
    SettingScreenStore.handleIconElementCheckboxClicked(aSelectedIconIds, bIsSelectAllClicked);
  };

  let handleIconLibraryPaginationChanged = function(oNewPaginationData, sContext) {
    SettingScreenStore.handleIconLibraryPaginationChanged(oNewPaginationData, sContext);
  };

  let handleDialogInputChanged = function (oFileData) {
    SettingScreenStore.handleDialogInputChanged(oFileData);
  };

  let handleDialogCancelButtonClicked = function () {
    SettingScreenStore.handleDialogCancelButtonClicked();
  };

  let handleFileDragDropViewDraggingState = () => {
    SettingScreenStore.handleFileDragDropViewDraggingState();
  };

  let handleFileDrop = (sContext, aFiles, oExtraData) => {
    SettingScreenStore.handleFileDrop(sContext, aFiles, oExtraData);
  };

  let handleDialogListRemoveButtonClicked = function (sKey) {
    SettingScreenStore.handleDialogListRemoveButtonClicked(sKey);
  };

  let handleSelectIconSelectClicked = function (oSelectedIconDetails, sContext) {
    SettingScreenStore.handleSelectIconSelectClicked(oSelectedIconDetails, sContext);
  };

  let handleIconElementActionButtonClicked = function (sButtonId, sId, aFiles) {
    SettingScreenStore.handleIconElementActionButtonClicked(sButtonId, sId, aFiles);
  };

  let handleIconElementCancelIconClicked = function (sButtonId) {
    SettingScreenStore.handleIconElementCancelIconClicked(sButtonId);
  };

  let handleIconElementSaveIconClicked = function (oFile, sCode, sName, isNameEmpty) {
    SettingScreenStore.handleIconElementSaveIconClicked(oFile, sCode, sName, isNameEmpty);
  };

  let handleIconElementReplaceIconClicked = function (oFile, fileName) {
    SettingScreenStore.handleIconElementReplaceIconClicked(oFile, fileName);
  };

  let handlePropertyCollectionDraggableListColumnsShuffled = function ( oSource, oDestination, aDraggableIds) {
    SettingScreenStore.handlePropertyCollectionDraggableListColumnsShuffled( oSource, oDestination, aDraggableIds);
  };

  let handlePropertyCollectionDraggableListTabClicked = function (sTabId) {
    SettingScreenStore.handlePropertyCollectionDraggableListTabClicked(sTabId);
  };

  let handlePropertyCollectionDraggableListPropertyRemove = function (sTabId) {
    SettingScreenStore.handlePropertyCollectionDraggableListPropertyRemove(sTabId);
  };

  let handleSelectIconCancelClicked = function (sContext, bIsOpenedFromGridView) {
    SettingScreenStore.handleSelectIconCancelClicked(sContext, bIsOpenedFromGridView);
  };

  let handleSelectIconElementSelected = function (sSelectedIconId, sContext) {
    SettingScreenStore.handleSelectIconElementSelected(sSelectedIconId, sContext);
  };

  let handleSelectIconElementSelectedFromGrid = function (oSelectedIconDetails, oGridViewData) {
    SettingScreenStore.handleSelectIconElementSelectedFromGrid(oSelectedIconDetails, oGridViewData);
  };

  let handleIconElementIconNameChanged = function (sFileName) {
    SettingScreenStore.handleIconElementIconNameChanged(sFileName);
  };

  let handleIndesignServerConfigurationAddButtonClicked = function () {
    SettingScreenStore.handleIndesignServerConfigurationAddButtonClicked();
  };

  let handleIndesignServerConfigurationHeaderSaveButtonClicked = function () {
    SettingScreenStore.handleIndesignServerConfigurationHeaderSaveButtonClicked();
  };

  let handleIndesignServerConfigurationHeaderCancelButtonClicked = function () {
    SettingScreenStore.handleIndesignServerConfigurationHeaderCancelButtonClicked();
  };

  let handleIndesignServerConfigurationRemoveButtonClicked = function (sServerId) {
    SettingScreenStore.handleIndesignServerConfigurationRemoveButtonClicked(sServerId);
  };

  let handleIndesignServerConfigurationCheckStatusButtonClicked = function (oServerDetails) {
    SettingScreenStore.handleIndesignServerConfigurationCheckStatusButtonClicked(oServerDetails);
  };

  let handleDamConfigUseFileNameValueChanged = function () {
    SettingScreenStore.handleDamConfigUseFileNameValueChanged();
  };

  let handleDamConfigurationSaveDiscardButtonClicked = function (sButtonId) {
    SettingScreenStore.handleDamConfigurationSaveDiscardButtonClicked(sButtonId);
  };

  let handleProcessFrequencySummaryDateButtonClicked = function (sDate, sContext) {
    SettingScreenStore.handleProcessFrequencySummaryDateButtonClicked(sDate, sContext);
  };

  let handlePermissionRestoreMenuClicked = function () {
    SettingScreenStore.handlePermissionRestoreMenuClicked();
  };

  let handlePermissionExportButtonClicked = function (sSelectedRole) {
    SettingScreenStore.handleRolesPermissionsExportButtonClicked(sSelectedRole);
  };

  let handlePermissionImportButtonClicked = function (sSelectedRole) {
    SettingScreenStore.handleRolesPermissionsImportButtonClicked(sSelectedRole);
  };

  let resetTableViewProps = function (context) {
    SettingScreenStore.resetTableViewProps(context);
  };

  let handleVariantConfigurationToggleButtonClicked = () => {
    SettingScreenStore.handleVariantConfigurationToggleButtonClicked()
  };

  let handleVariantActionButtonClicked = (sId) => {
    SettingScreenStore.handleVariantActionButtonClicked(sId)
  };

  let handleBPMNSearchCriteriaEditButtonClicked = function (sName) {
    SettingScreenStore.handleBPMNSearchCriteriaEditButtonClicked(sName);
  };

  let handleCreateRoleCloneDialogActionButtonClicked = function (sId) {
    SettingScreenStore.handleCreateRoleCloneDialogActionButtonClicked(sId);
  };

  let handleCreateRoleCloneDialogCheckboxButtonClicked = function (sContext) {
    SettingScreenStore.handleCreateRoleCloneDialogCheckboxButtonClicked(sContext);
  };

  let handleCreateRoleCloneDialogExactCloneButtonClicked = function () {
    SettingScreenStore.handleCreateRoleCloneDialogExactCloneButtonClicked();
  };

  let handleListNodeViewNodeCloneButtonClicked = function (sId, sContext) {
    SettingScreenStore.handleListNodeViewNodeCloneButtonClicked(sId, sContext);
  };

  let handleCreateRoleCloneDialogEditValueChanged = function (sContext, sValue) {
    SettingScreenStore.handleCreateRoleCloneDialogEditValueChanged(sContext, sValue);
  };

  let handleAdministrationExpandButtonClicked = function (sExpandedTileId) {
    SettingScreenStore.handleAdministrationSummaryExpandButtonClicked(sExpandedTileId);
  };

  /**
   * Binding Events into EventHandler
   */
  (() => {
    /** @deprecated */
    var _setEvent = CS.set.bind(this, oEventHandler);

    oEventHandler[KeydownEvents.ESC] = ActionInterceptor('Esc', handleEsc);

    oEventHandler[ConfigHeaderViewEvents.CONFIG_HEADER_VIEW_ACTION_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: Config Header View - Handle Action Button Clicked', handleActionItemClicked);
    oEventHandler[ConfigHeaderViewEvents.CONFIG_HEADER_VIEW_HANDLE_SEARCH] = ActionInterceptor('SettingScreen: Config Header View - Handle Search', handleConfigHeaderViewSearch);

    oEventHandler[ContextEvents.HANDLE_CONTEXT_TAG_CHECK_ALL_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Context Tag Check All Changed', handleContextTagCheckAllChanged);
    oEventHandler[ContextEvents.HANDLE_DEFAULT_FROM_DATE_VALUE_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Default From Date Value Changed', handleDefaultFromDateValueChanged);
    oEventHandler[ContextEvents.HANDLE_CONTEXT_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
        'SettingScreen: Handle Add New Tag Combination', handleContextConfigDialogButton);
    oEventHandler[ContextEvents.HANDLE_CUSTOM_CONTEXT_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
        'SettingScreen: Dialog actions handle', handleContextConfigDialogButtonClicked);
    oEventHandler[ContextEvents.CONTEXT_CONFIG_ADD_TAG_COMBINATION] = ActionInterceptor(
        'SettingScreen:class config add tag combination', handleAddTagCombinationsClicked);

    oEventHandler[MappingEvents.HANDLE_CUSTOM_MAPPING_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
        'SettingScreen: Dialog actions handle', handleMappingConfigDialogButtonClicked);
    oEventHandler[MappingEvents.MAPPING_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
        'SettingScreen: Dialog Button clicked', handleMappingSaveAsDialogButtonClicked);
    oEventHandler[MappingEvents.MAPPING_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED] = ActionInterceptor(
        'SettingScreen: Dialog Button clicked', handleSaveAsDialogValueChanged);

    oEventHandler[ColorPickerViewEvents.COLOR_PICKER_COLOR_CHANGED] = ActionInterceptor(
        'SettingScreen: Color picker color changed', handleColorPickerColorChanged);

    oEventHandler[AuthorizationMappingConfigEvents.HANDLE_CUSTOM_AUTHORIZATION_MAPPING_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
        'SettingScreen: Dialog actions handle', handleAuthorizationMappingConfigDialogButtonClicked);

    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_MAPPING_SUMMARY_TAB_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Mapping Summary Tab Changed', handleTabLayoutTabChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_SINGLE_TEXT_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Single Text Changed', handleCommonConfigSectionSingleTextChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_FROALA_TEXT_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Froala Text Changed', handleCommonConfigSectionFroalaTextChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_ADD_OPERATOR] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Add Operator', handleCommonConfigSectionAddOperator);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_ADD_CONCAT_OBJECT] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Add Concat Object', handleCommonConfigSectionAddConcatObject);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_CONCAT_INPUT_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Concat Input Changed', handleCommonConfigSectionConcatInputChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_CONCAT_OBJECT_REMOVED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Concat Object Removed', handleCommonConfigSectionConcatObjectRemoved);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_OPERATOR_ATTRIBUTE_VALUE_CHANGED] = ActionInterceptor('SettingScreen: Handle Operator Attribute Value Changed', handleOperatorAttributeValueChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_DELETE_OPERATOR_ATTRIBUTE_VALUE] = ActionInterceptor('SettingScreen: Delte Operator Attribute Value', deleteOperatorAttributeValue);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_NATIVE_DROPDOWN_VALUE_CHANGED] = ActionInterceptor('SettingScreen: Handle Common Config Section View Native Dropdown Value Changed', handleCommonConfigSectionViewNativeDropdownValueChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_MULTI_TEXT_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section Single Text Changed', handleCommonConfigSectionSingleTextChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_TAG_TYPE_RANGE_VALUE_CHANGED] = ActionInterceptor('SettingScreen: Handle Tag Type Range Value Changed', handleTagTypeRangeValueChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_YES_NO_BUTTON_CHANGED] = ActionInterceptor('SettingScreen: Handle Common Config Section View Yes No Button Clicked', handleCommonConfigSectionViewYesNoButtonClicked);
      oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_RADIO_BUTTON_CHANGED] = ActionInterceptor('SettingScreen: Handle Common Config Section View Radio Button Clicked', handleCommonConfigSectionViewRadioButtonClicked);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_ICON_CHANGED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section View Icon Changed', handleCommonConfigSectionViewIconChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_ICON_REMOVED] = ActionInterceptor(
        'SettingScreen: Handle Common Config Section View Icon Changed', handleCommonConfigSectionViewIconChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_SINGLE_TEXT_NUMBER_CHANGED] = ActionInterceptor('SettingScreen: Handle Common Config Section View Single Text Number Changed',
            handleCommonConfigSectionViewSingleTextNumberChanged);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_CHECKBOX_ON_CHANGE_FOR_ENTITY_PERMISSION] = ActionInterceptor('SettingScreen: Handle Common Config Checkbox On Changed For Entity Permission',
            handleCommonConfigCheckboxOnChangeForEntityPermission);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_SECTION_TEMPLATE_ACTION_CLICKED] = ActionInterceptor('SettingScreen: Handle Common Config Upload Template Action Clicked',
        handleCommonConfigUploadTemplateActionClicked);
    oEventHandler[CommonConfigSectionViewEvents.COMMON_CONFIG_UPLOAD_ICON_CLICKED] = ActionInterceptor('SettingScreen: Handle Common Config Upload Icon Clicked',
        handleCommonConfigUploadIconClicked);
    oEventHandler[RoleDetailEvents.SETTING_SCREEN_ROLE_DATA_CHANGED] = ActionInterceptor(
        'SettingScreen: Role Data changed', handleSettingScreenRoleDataChanged);
    oEventHandler[RoleDetailEvents.ROLE_MODULE_PERMISSION_CHANGED] = ActionInterceptor(
        'SettingScreen: Role Module Permission Changed', handleRoleModulePermissionChanged);
    oEventHandler[RoleDetailEvents.ROLE_MODULE_DASHBOARD_VISIBILITY_TOGGLED] = ActionInterceptor(
        'SettingScreen: Role Detail entity search', handleDashboardVisibilityToggled);
    oEventHandler[RoleDetailEvents.ROLE_MODULE_READ_ONLY_PERMISSION_TOGGLED] = ActionInterceptor(
        'SettingScreen: Role Permission Read Only', handleReadOnlyPermissionToggled);

    oEventHandler[RelationshipDetailsViewEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED] = ActionInterceptor('SettingScreen: Relationship Data Changed', handleRelationshipDataChanged);

    oEventHandler[ClassConfigViewCloneEvents.CLASS_CONFIG_CREATE_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: Handle Class Create Dialog Button Clicked', handleClassCreateDialogButtonClicked);
    oEventHandler[ClassConfigViewCloneEvents.CLASS_SAVE_DIALOG_CLOSE] = ActionInterceptor('SettingScreen: Handle Class Save Dialog Button Clicked', handleClassSaveDialogButtonClicked);
    oEventHandler[ClassConfigViewCloneEvents.CLASS_CONFIG_IMPORT_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:' +
        ' Handle Class Save Dialog Button Clicked', handleClassConfigImportButtonClicked);

    oEventHandler[ClassConfigRelationshipViewEvents.CLASS_RELATIONSHIP_MODIFIED] = ActionInterceptor('SettingScreen: Handle Class Relationship Modified', handleClassRelationshipModified);

    oEventHandler[ClassConfigSectionViewEvents.CLASS_CONFIG__SELECT_DEFAULT_SECTION] = ActionInterceptor('SettingScreen: Handle Section Clicked', handleSectionClicked);
    oEventHandler[AssetUploadConfigurationViewEvents.ASSET_CONFIG_ADD_NEW_SECTION_CLICKED] = ActionInterceptor('SettingScreen: Handle Add New Asset Configuration Section Clicked', handleAddNewAssetConfigurationSectionClicked);
    oEventHandler[AssetUploadConfigurationViewEvents.ASSET_CONFIG_EDIT_SECTION_CLICKED] = ActionInterceptor('SettingScreen: Handle Edit Asset' +
        ' Configuration Section Clicked', handleEditAssetConfigurationSectionClicked);
    oEventHandler[AssetUploadConfigurationViewEvents.ASSET_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: Handle Asset Config' +
        ' Dialog Button Clicked', handleAssetConfigurationDialogButtonClicked);
    oEventHandler[AssetUploadConfigurationViewEvents.DELETE_ASSET_CONFIGURATION_ROW_CLICKED] = ActionInterceptor('SettingScreen: Handle Delete Asset Configuration Row Clicked', handleDeleteAssetConfigurationRowClicked);
    oEventHandler[AssetUploadConfigurationViewEvents.HANDLE_ASSET_CONFIG_ROW_DATA_CHANGE] = ActionInterceptor('SettingScreen: Handle Asset Config Row Data Change', handleAssetConfigRowDataChange);

    oEventHandler[SectionGridElementViewEvents.VISUAL_ELEMENT_BLOCKER_CLICKED] = ActionInterceptor('SettingScreen: Handle Visual Element Blocked Clicked', handleVisualElementBlockerClicked);
    oEventHandler[SectionGridElementViewEvents.VISUAL_ELEMENT_ATTRIBUTE_TILE_VALUE_CHANGED] = ActionInterceptor('SettingScreen: Handle Visual Element Attribute Value Changed', handleVisualElementAttributeValueChanged);
    oEventHandler[SectionGridElementViewEvents.VISUAL_ELEMENT_TAG_CHECK_ALL_CHANGED] = ActionInterceptor('SettingScreen: Handle Element Tag Check All Changed', handleElementTagCheckAllChanged);
    oEventHandler[SectionGridElementViewEvents.VISUAL_ELEMENT_FILTERABLE_OR_SORTABLE_CHECK_ALL_CHANGED] = ActionInterceptor('SettingScreen: Handle Element Filterable or Sortable Check All Changed',
            handleElementFilterableOrSortableCheckAllChanged);

    oEventHandler[ClassAvailableListSearchViewEvents.CLASS_AVAILABLE_LIST_VIEW_LOAD_MORE] = ActionInterceptor('SettingScreen: Handle load more clicked in available list view', handleAvailableListViewLoadMoreClicked);
    oEventHandler[ClassAvailableListSearchViewEvents.CLASS_AVAILABLE_LIST_VIEW_SEARCH] = ActionInterceptor('SettingScreen: Handle search in available list view', handleAvailableListViewSearched);
    oEventHandler[ClassAvailableListSearchViewEvents.CLASS_AVAILABLE_LIST_VIEW_HANDLE_ITEM_COLLAPSED_TOGGLED] = ActionInterceptor('SettingScreen: get body collapsed status', handlePropertyCollapsedStatusToggled);

    oEventHandler[PropertyCollectionGridElementViewEvents.VISUAL_ELEMENT_DELETE_ICON_CLICKED] = ActionInterceptor('SettingScreen: handle Delete Visual Element Icon Clicked', handleDeleteVisualElementIconClicked);
    oEventHandler[PropertyCollectionGridElementViewEvents.VISUAL_ELEMENT_BLOCKER_CLICKED] = ActionInterceptor('SettingScreen: handle Visual Element Blocker Clicked', handleVisualElementBlockerClicked);

    oEventHandler[TemplateHeaderViewEvents.TEMPLATE_HEADER_VISIBILITY_TOGGLED] = ActionInterceptor('SettingScreen: handle Template Header Visibility Toggled',
            handleTemplateHeaderVisibilityToggled);
    oEventHandler[TemplateHeaderViewEvents.TEMPLATE_HEADER_TAB_CLICKED] = ActionInterceptor('SettingScreen: handle Template Header Tab Clicked',
            handleTemplateHeaderTabClicked);
    oEventHandler[TemplateHeaderViewEvents.TEMPLATE_HEADER_TAB_ICON_CHANGED] = ActionInterceptor('SettingScreen: handle Template Header Tab Icon Changed',
            handleTemplateHeaderTabIconChanged);
    oEventHandler[TemplateTabBodyEvents.TEMPLATE_SECTION_ADDED] = ActionInterceptor('SettingScreen: handle Template Section Added', handleTemplateSectionAdded);
    oEventHandler[TemplateTabBodyEvents.TEMPLATE_SECTION_ACTION_BAR_CLICKED] = ActionInterceptor('SettingScreen: handle Template Section Action Button Clicked',
            handleTemplateSectionActionButtonClicked);
    oEventHandler[TemplateTabBodyEvents.TEMPLATE_SECTION_DROP_DOWN_SEARCH_LOAD_MORE_CLICKED] = ActionInterceptor('SettingScreen: handle Template Section Drop Down Loadmore/Search', handleTemplateSectionDropDownLoadMoreSearch);

    oEventHandler[CustomTemplateEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_SELECTED_ENTITY_CROSS_ICON_CLICKED] = ActionInterceptor('SettingScreen: handle cross icon clicked', handleSelectedEntityRemoved);
    oEventHandler[CustomTemplateEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_SELECTED_ENTITIES_CHANGED] = ActionInterceptor('SettingScreen: handle Custom Template Selected Entities Changed', handleCustomTemplateSelectedEntitiesChanged);
    oEventHandler[CustomTemplateEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_POPOVER_OPEN] = ActionInterceptor('SettingScreen: handle popover open', handleCustomTemplatePopoverOpen);
    oEventHandler[CustomTemplateEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_LOAD_MORE_CLICKED] = ActionInterceptor('SettingScreen: handleCustomTemplateLoadMoreClicked', handleCustomTemplateLoadMoreClicked);
    oEventHandler[CustomTemplateEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_SEARCH_CLICKED] = ActionInterceptor('SettingScreen: handleCustomTemplateSearchClicked', handleCustomTemplateSearchClicked);

    oEventHandler[PermissionHeaderViewEvents.PERMISSION_HEADER_VISIBILITY_TOGGLED] = ActionInterceptor('SettingScreen: handle Permission Of Template Header Toggled', handlePermissionOfTemplateHeaderToggled);
    oEventHandler[PermissionTabBodyviewEvents.PERMISSION_SECTION_STATUS_CHANGED] = ActionInterceptor('SettingScreen: handle Permission Section Status Changed', handlePermissionSectionStatusChanged);
    oEventHandler[PermissionTabBodyviewEvents.PERMISSION_ELEMENT_STATUS_CHANGED] = ActionInterceptor('SettingScreen: handle Permission Element Status Changed', handlePermissionElementStatusChanged);

    oEventHandler[VisualSectionViewEvents.SECTION_CLICKED] = ActionInterceptor('SettingScreen: handle Section Clicked', handleSectionClicked);
    oEventHandler[VisualSectionViewEvents.SECTION_NAME_CHANGED] = ActionInterceptor('SettingScreen: handle Section Name Changed', handleSectionNameChanged);
    oEventHandler[VisualSectionViewEvents.SECTION_ICON_CHANGED] = ActionInterceptor('SettingScreen: handle Section Icon Changed', handleSectionIconChanged);
    oEventHandler[VisualSectionViewEvents.SECTION_ICON_REMOVED] = ActionInterceptor('SettingScreen: handle Section Icon Changed', handleSectionIconChanged);
    oEventHandler[VisualSectionViewEvents.SECTION_COL_COUNT_CHANGED] = ActionInterceptor('SettingScreen: handle Section Col Count Changed', handleSectionColCountChanged);
    oEventHandler[VisualSectionViewEvents.SECTION_ROW_COUNT_CHANGED] = ActionInterceptor('SettingScreen: handle Section Row Count Changed', handleSectionRowCountChanged);
    oEventHandler[VisualSectionViewEvents.SECTION_DELETE_CLICKED] = ActionInterceptor('SettingScreen: handle Section Delete Clicked', handleSectionDeleteClicked);
    oEventHandler[VisualSectionViewEvents.SECTION_MOVE_UP_CLICKED] = ActionInterceptor('SettingScreen: handle Section Move Up Clicked', handleSectionMoveUpClicked);
    oEventHandler[VisualSectionViewEvents.SECTION_MOVE_DOWN_CLICKED] = ActionInterceptor('SettingScreen: handle Section Move Down Clicked', handleSectionMoveDownClicked);
    oEventHandler[VisualSectionViewEvents.SECTION_BLOCKER_CLICKED] = ActionInterceptor('SettingScreen: handle Section Blocker Clicked', handleSectionBlockerClicked);

    oEventHandler[PropertyCollectionVisualSectionViewEvents.SECTION_CLICKED] = ActionInterceptor('SettingScreen: handle Section Clicked', handleSectionClicked);

    oEventHandler[MultiSelectSearchViewNewEvents.MULTI_SEARCH_DROP_DOWN_LIST_NODE_BLURRED] = ActionInterceptor('SettingScreen: handle Drop Down List Node Blurred', handleDropDownListNodeBlurred);
    oEventHandler[MultiSelectSearchViewNewEvents.MULTI_SEARCH_DROP_DOWN_LIST_NODE_CLICKED] = ActionInterceptor('SettingScreen: handle Drop Down List Node Clicked', handleDropDownListNodeClicked);
    oEventHandler[MultiSelectSearchViewNewEvents.MULTI_SEARCH_BAR_CROSS_ICON_CLICKED] = ActionInterceptor('SettingScreen: handle MultiSelect Search Cross Icon Clicked',
            handleMultiSelectSearchCrossIconClicked);

    oEventHandler[RuleDetailViewEvents.RULE_CALC_ATTR_ADD_OPERATOR] = ActionInterceptor('SettingScreen: handle Rule Calc Attr Add Operator', handleRuleCalcAttrAddOperator);
    oEventHandler[RuleDetailViewEvents.RULE_CALC_ATTR_OPERATOR_ATTRIBUTE_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handle Rule Calc Attr Operator Attribute Value Changed',
            handleRuleCalcAttrOperatorAttributeValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_CALC_ATTR_DELETE_OPERATOR_ATTRIBUTE_VALUE] = ActionInterceptor('SettingScreen: handle Rule Calc Attr Delete Operator Attribute Value', handleRuleCalcAttrDeleteOperatorAttributeValue);
    oEventHandler[RuleDetailViewEvents.RULE_CALC_ATTR_CUSTOM_UNIT_CHANGED] = ActionInterceptor('SettingScreen: handle Rule Calc Attr Custom Unit Changed', handleRuleCalcAttrCustomUnitChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAILS_MSS_SEARCH_CLICKED] = ActionInterceptor('SettingScreen: handleRuleDetailsMSSSearchTextChanged', handleRuleDetailsMSSSearchTextChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAILS_MSS_LOAD_MORE_CLICKED] = ActionInterceptor('SettingScreen: handleRuleDetailsMSSLoadMoreClicked', handleRuleDetailsMSSLoadMoreClicked);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_KLASS_CAUSE_EFFECT_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleRuleDetailsMSSLoadMoreClicked', handleRuleDetailsKlassMSSChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_KLASS_TYPE_CAUSE_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleRuleDetailsMSSLoadMoreClicked', handleRuleDetailsKlassTypeMSSChanged);

    oEventHandler[RuleConfigViewEvents.RULE_NATURE_CLASS_ADDED_IN_GOLDEN_RECORD] = ActionInterceptor('SettingScreen: handleRuleConfigNatureClassAddedInGoldenRecord', handleRuleConfigNatureClassAddedInGoldenRecord);


    oEventHandler[CustomBPMNDiagramReplaceEvents.BPMN_PROCESS_AND_SET_NEW_CUSTOM_ELEMENT] = ActionInterceptor('SettingScreen: handleRuleDetailsMSSLoadMoreClicked', handleProcessAndSetNewCustomElement);
    oEventHandler[CustomBPMNDiagramReplaceEvents.BPMN_PROCESS_AND_SET_NEW_ELEMENT] = ActionInterceptor('SettingScreen: handleRuleDetailsMSSLoadMoreClicked', handleProcessAndSetNewElement);
    oEventHandler[CustomTabPropertiesProviderEvents.SET_ACTIVE_BPMN_INSTANCES] = ActionInterceptor('SettingScreen: handleSetActiveBPMNInstances', handleSetActiveBPMNInstances);
    oEventHandler[CustomTabPropertiesProviderEvents.BPMN_CUSTOM_TABS_PROVIDER_UPDATE] = ActionInterceptor('SettingScreen: handleBPMNCustomTabsUpdate', handleBPMNCustomTabsUpdate);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_SEARCH_FILTER_EDIT_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:  : handleSearchFilterEditButtonClicked', handleSearchFilterEditButtonClicked);

    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.HANDLE_BPMN_ELEMENTS_TEXT_CHANGED] = ActionInterceptor('SettingScreen: handleBPMNCustomElementTextChanged', handleBPMNCustomElementTextChanged);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.HANDLE_BPMN_ELEMENTS_MSS_CHANGED] = ActionInterceptor('SettingScreen: handleBPMNCustomElementMSSChanged', handleBPMNCustomElementMSSChanged);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.BPMN_PROPERTIES_TAG_MSS_CHANGED] = ActionInterceptor('SettingScreen: handleBPMNPropertiesTagMSSChanged', handleBPMNPropertiesTagMSSChanged);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.BPMN_PROPERTIES_TAG_MSS_CHANGED_CUSTOM] = ActionInterceptor('SettingScreen: handleBPMNPropertiesTagMSSChanged', handleBPMNPropertiesTagMSSChangedCustom);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_ADD_TAG_GROUP_IN_CLASS_VALUE] = ActionInterceptor('SettingScreen: handleProcessComponentClassInfoAddTagGroup', handleProcessComponentClassInfoAddTagGroup);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_REMOVE_TAG_GROUP_IN_CLASS_VALUE] = ActionInterceptor('SettingScreen: handleProcessComponentClassInfoRemoveTagGroup', handleProcessComponentClassInfoRemoveTagGroup);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_ADD_ATTRIBUTE_GROUP_IN_CLASS_VALUE] = ActionInterceptor('SettingScreen: handleProcessComponentClassInfoAddAttributeGroup', handleProcessComponentClassInfoAddAttributeGroup);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_REMOVE_ATTRIBUTE_GROUP_IN_CLASS_VALUE] = ActionInterceptor('SettingScreen: handleProcessComponentClassInfoRemoveAttributeGroup', handleProcessComponentClassInfoRemoveAttributeGroup);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.BPMN_PROPERTIES_ATTRIBUTE_MSS_CHANGED_CUSTOM] = ActionInterceptor('SettingScreen: handleBPMNPropertiesAttributeMSSChanged', handleBPMNPropertiesAttributeMSSChangedCustom);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.HANDLE_BPMN_ELEMENTS_DATE_CHANGED] = ActionInterceptor('SettingScreen:  : handleBPMNElementsDateChanged', handleBPMNPropertiesAttributeDateChangedCustom);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.HANDLE_BPMN_ELEMENTS_MSS_CLICKED] = ActionInterceptor('SettingScreen:  : handleMSSValueClicked', handleBPMNPropertiesMSSClicked);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.HANDLE_BPMN_ELEMENTS_DATA_LANGUAGE_MSS_CHANGED] = ActionInterceptor('SettingScreen:  : handleBPMNElementsDataLanguageMSSChanged', handleBPMNElementsDataLanguageMSSChanged);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_CONTENT_TASK_DETAIL_TASK_ROLE_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen:  : handleBPMNElementsGroupMSSChanged', handleBPMNElementsGroupMSSChanged);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_VARIABLE_MAP_VALUE_CHANGED] = ActionInterceptor('SettingScreen:  : handleBPMNElementsVariableMapChanged', handleBPMNElementsVariableMapChanged);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_ADD_VARIABLE_MAP_VALUE] = ActionInterceptor('SettingScreen:  : handleBPMNElementsAddVariableMap', handleBPMNElementsAddVariableMap);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_REMOVE_VARIABLE_MAP_VALUE] = ActionInterceptor('SettingScreen:  : handleBPMNElementsRemoveVariableMap', handleBPMNElementsRemoveVariableMap);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_ADD_ROW_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:  : handleBPMNElementsAddRowButtonClicked', handleBPMNElementsAddRowButtonClicked);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_REMOVE_ROW_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:  : handleBPMNElementsRemoveRowButtonClicked', handleBPMNElementsRemoveRowButtonClicked);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_ENTITY_MAP_VALUE_CHANGED] = ActionInterceptor('SettingScreen:  : handleBPMNElementsRemoveRowButtonClicked', handleBPMNElementsEntityMapChanged);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.COMPONENT_SEARCH_CRITERIA_EDIT_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:  : handleBPMNSearchCriteriaEditButtonClicked', handleBPMNSearchCriteriaEditButtonClicked);
    oEventHandler[BPMNCUSTOMComponentsPropertiesViewEvents.HANDLE_SELECTED_TAXONOMIES_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: Selected Taxonomies Button Clicked', handleBPMNSearchCriteriaEditButtonClicked);
    oEventHandler[SelectionToggleViewEvents.SELECTION_TOGGLE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handle Selection Toggle Button Clicked', handleSelectionToggleButtonClicked);

    oEventHandler[DragViewEvents.DRAG_VIEW_ON_DRAG_START_EVENT] = ActionInterceptor('SettingScreen: handle Content Entity Drag Start', handleContentEntityDragStart);
    oEventHandler[DragViewEvents.DRAG_VIEW_ON_DRAG_END_EVENT] = ActionInterceptor('SettingScreen: handle Content Entity Drag End', handleContentEntityDragEnd);
    oEventHandler[DropViewEvents.DROP_VIEW_ON_DROP_EVENT] = ActionInterceptor('SettingScreen: handle Content Entity Drop', handleContentEntityDrop);

    oEventHandler[UserPasswordChangeView.CONTENT_USER_NEW_PASSWORD_SUBMIT] = ActionInterceptor('SettingScreen: handle User Password Submit', handleUserPasswordSubmit);
    oEventHandler[UserPasswordChangeView.CONTENT_USER_NEW_PASSWORD_CANCEL] = ActionInterceptor('SettingScreen: handle User Password Cancel', handleUserPasswordCancel);

    oEventHandler[ListViewEvents.LIST_ITEM_CREATED] = ActionInterceptor('SettingScreen: handle List Item Created', handleListItemCreated);
    oEventHandler[ListViewEvents.LIST_VIEW_SEARCH_OR_LOAD_MORE_CLICKED] = ActionInterceptor('SettingScreen: handle List View Search or load more clicked', handleListViewSearchOrLoadMoreClicked);

    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_ADD_NEW_MAPPING_ROW] = ActionInterceptor('SettingScreen: handle Mapping Summary View Add New Mapping Row', handleMappingSummaryViewAddNewMappingRow);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_COLUMN_NAME_CHANGED] = ActionInterceptor('SettingScreen: handle Mapping Summary View Column Name Changed', handleMappingSummaryViewColumnNameChanged);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_MAPPED_ELEMENT_CHANGED] = ActionInterceptor('SettingScreen: handle Mapping Summary View Mapped Element Changed', handleMappingSummaryViewMappedElementChanged);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_MAPPING_ROW_SELECTED] = ActionInterceptor('SettingScreen: handle Mapping Summary View Mapping Row Selected', handleMappingSummaryViewMappingRowSelected);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_IS_IGNORED_TOGGLED] = ActionInterceptor('SettingScreen: handleMappingSummaryViewIsIgnoredToggled', handleMappingSummaryViewIsIgnoredToggled);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_MAPPING_ROW_DELETED] = ActionInterceptor('SettingScreen: handleMappingSummaryViewMappingRowDeleted', handleMappingSummaryViewMappingRowDeleted);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_ADD_TAG_VALUE_CLICKED] = ActionInterceptor('SettingScreen: handleMappingSummaryViewAddTagValueClicked', handleMappingSummaryViewAddTagValueClicked);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_TAG_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleMappingSummaryViewConfigTagValueChanged', handleMappingSummaryViewConfigTagValueChanged);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_TAG_VALUE_IGNORE_CASE_TOGGLED] = ActionInterceptor('SettingScreen: handleMappingSummaryViewConfigTagValueIgnoreCaseToggled', handleMappingSummaryViewConfigTagValueIgnoreCaseToggled);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_MAPPED_TAG_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleMappingSummaryViewConfigMappedTagValueChanged', handleMappingSummaryViewConfigMappedTagValueChanged);
    oEventHandler[MappingSummaryViewEvents.MAPPING_SUMMARY_VIEW_MAPPED_TAG_VALUE_ROW_DELETE_CLICKED] = ActionInterceptor('SettingScreen: handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked', handleMappingSummaryViewConfigMappedTagValueRowDeleteClicked);

    //Property Group Events
    oEventHandler[PropertyGroupSummaryViewEvents.PROPERTY_GROUP_SUMMARY_VIEW_CHECKBOX_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handlePropertyGroupCheckboxClicked', handlePropertyGroupCheckboxClicked);
    oEventHandler[PropertyGroupSummaryViewEvents.PROPERTY_GROUP_SUMMARY_VIEW_APPLY_BUTTON_CLICKED] = ActionInterceptor('Handle Apply Button Clicked For Property Group Mapping', handlePropertyGroupApplyButtonClicked);
    oEventHandler[PropertyGroupSummaryViewEvents.PROPERTY_GROUP_SUMMARY_VIEW_DELETE_BUTTON_CLICKED] = ActionInterceptor('Handle Delete Button Clicked For  Property Group Mapping', handlePropertyGroupDeleteButtonClicked);
    oEventHandler[PropertyGroupSummaryViewEvents.PROPERTY_GROUP_SUMMARY_VIEW_ATTRIBUTE_AND_TAG_CHECKBOX_BUTTON_CLICKED] = ActionInterceptor('handle Checkbox Clicked For Attribue and Tag For Property Group Mapping', handlePropertyGroupCheckboxClickedForAttributeAndTag);
    oEventHandler[PropertyGroupSummaryViewEvents.PROPERTY_GROUP_SUMMARY_VIEW_PROPERTY_COLLECTION_CLICKED] = ActionInterceptor('handle Property Collection click For Property Group Mapping', handlePropertyGroupPropertyCollectionListItemClicked);
    oEventHandler[PropertyGroupSummaryViewEvents.PROPERTY_GROUP_SUMMARY_VIEW_COLUMN_NAME_CHANGED] = ActionInterceptor('handle Cloumn Name Changed For Property Group Mapping', handlePropertyGroupColumnNameChanged);
    oEventHandler[PropertyGroupSummaryViewEvents.PROPERTY_GROUP_SUMMARY_VIEW_SEARCH_VIEW_CHANGED] = ActionInterceptor('handle Search view Changed For Property Group Mapping', handlePropertyGroupSearchViewChanged);
    oEventHandler[PropertyGroupSummaryViewEvents.PROPERTY_GROUP_SUMMARY_VIEW_PROPERTY_COLLECTION_TOGGLE_BUTTON_CLICKED] = ActionInterceptor('handle property collection toggle button clicked', handlePropertyCollectionToggleButtonClicked);


    oEventHandler[ClassConfigSectionsView.CLASS_CONFIG_SECTION_ADDED] = ActionInterceptor('SettingScreen: handleSectionAdded', handleSectionAdded);
    oEventHandler[ClassConfigSectionsView.GRID_PROPERTY_COLLECTION_ICON_CLICKED] = ActionInterceptor('SettingScreen: handleSectionToolbarIconClicked', handleSectionToolbarIconClicked);
    oEventHandler[ClassConfigSectionsView.CLASS_CONFIG_SECTION_ELEMENT_CHECKBOX_TOGGLED] = ActionInterceptor('SettingScreen: handleSectionElementCheckboxToggled', handleSectionElementCheckboxToggled);
    oEventHandler[ClassConfigSectionsView.CLASS_CONFIG_SECTION_ELEMENT_INPUT_CHANGED] = ActionInterceptor('SettingScreen: handleSectionElementInputChanged', handleSectionElementInputChanged);
    oEventHandler[ClassConfigSectionsView.CLASS_CONFIG_SECTION_TOOLBAR_ICON_CLICKED] = ActionInterceptor('SettingScreen: handleSectionToolbarIconClicked', handleSectionToolbarIconClicked);
    oEventHandler[ClassConfigSectionsView.CLASS_CONFIG_SECTION_TOGGLE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleSectionToggleButtonCLicked', handleSectionToggleButtonClicked);
    oEventHandler[ClassConfigSectionsView.CLASS_CONFIG_SECTION_ELEMENT_MSS_CHANGED] = ActionInterceptor('SettingScreen: handleSectionElementInputChanged', handleSectionMSSValueChanged);

    oEventHandler[ClassConfigRuleViewEvents.CLASS_CONFIG_RULE_ITEM_CLICKED] = ActionInterceptor('SettingScreen: handleClassConfigRuleItemClicked',
        handleClassConfigRuleItemClicked);

    oEventHandler[SectionListViewEvents.RELATIONSHIP_SIDE2SECTION_APPLY_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleSectionListApplyButtonClicked', handleSectionListApplyButtonClicked);
    oEventHandler[SectionListViewEvents.RELATIONSHIP_SIDE2SECTION_DELETE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleSectionListDeleteButtonClicked', handleSectionListDeleteButtonClicked);

    oEventHandler[PermissionViewEvents.FETCH_ENTITY_CHILDREN_WITH_GLOBAL_PERMISSIONS] = ActionInterceptor('SettingScreen: fetchEntityChildrenWithGlobalPermissions', fetchEntityChildrenWithGlobalPermissions);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_BUTTON_TOGGLED] = ActionInterceptor('SettingScreen: handlePermissionButtonToggled', handlePermissionButtonToggled);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_SELECTION_TOGGLED] = ActionInterceptor('SettingScreen: handlePermissionSelectionToggled', handlePermissionSelectionToggled);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_FIRST_LEVEL_ITEM_CLICKED] = ActionInterceptor('SettingScreen: handlePermissionFirstLevelItemClicked', handlePermissionFirstLevelItemClicked);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_TEMPLATE_ADDED] = ActionInterceptor('SettingScreen: handlePermissionTemplateAdded', handlePermissionTemplateAdded);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_KLASS_ITEM_CLICKED] = ActionInterceptor('SettingScreen: handlePermissionKlassItemClicked', handlePermissionKlassItemClicked);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_REMOVE_TEMPLATE_CLICKED] = ActionInterceptor('SettingScreen: handlePermissionRemoveTemplateClicked', handlePermissionRemoveTemplateClicked);
    oEventHandler[PermissionViewEvents.FETCH_ALLOWED_TEMPLATES] = ActionInterceptor('SettingScreen: handlePermissionRemoveTemplateClicked', fetchAllowedTemplates);
    oEventHandler[PermissionViewEvents.HANDLE_ALLOWED_TEMPLATES_SEARCH] = ActionInterceptor('SettingScreen: handlePermissionRemoveTemplateClicked', handleAllowedTemplatesSearch);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_RESTORE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:' +
        ' handlePermissionRemoveTemplateClicked', handlePermissionRestoreMenuClicked);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_EXPORT_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:' +
        ' handlePermissionRemoveTemplateClicked', handlePermissionExportButtonClicked);
    oEventHandler[PermissionViewEvents.HANDLE_PERMISSION_IMPORT_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:' +
        ' handlePermissionRemoveTemplateClicked', handlePermissionImportButtonClicked);
    oEventHandler[TableViewNew.RESET_TABLE_VIEW_PROPS] = ActionInterceptor('SettingScreen:' +
        ' resetTableViewProps', resetTableViewProps);


    oEventHandler[PaperViewEvent.PAPER_VIEW_HEADER_CLICKED] = ActionInterceptor('SettingScreen: paperViewHeaderClicked', paperViewHeaderClicked);

    oEventHandler[RoleConfigViewEvents.ROLE_CONFIG_PERMISSION_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleRoleConfigPermissionDialogButtonClicked', handleRoleConfigPermissionDialogButtonClicked);

    //RULE
    oEventHandler[RuleDetailViewEvents.RULE_NAME_CHANGED] = ActionInterceptor('SettingScreen: handleRuleNameChanged', handleRuleNameChanged);
    oEventHandler[RuleDetailViewEvents.RULE_ELEMENT_DELETE_CLICKED] = ActionInterceptor('SettingScreen: handleRuleElementDeleteButtonClicked', handleRuleElementDeleteButtonClicked);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleRuleAttributeValueChanged', handleRuleAttributeValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_ADD_ATTRIBUTE_CLICKED] = ActionInterceptor('SettingScreen: handleRuleAddAttributeClicked', handleRuleAddAttributeClicked);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_VALUE_TYPE_CLICKED] = ActionInterceptor('SettingScreen: handleRuleAttributeValueTypeChanged', handleRuleAttributeValueTypeChanged);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_VALUE_DELETE_CLICKED] = ActionInterceptor('SettingScreen: handleRuleAttributeValueDeleteClicked', handleRuleAttributeValueDeleteClicked);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE] = ActionInterceptor('SettingScreen: handleRuleAttributeRangeValueChanged', handleRuleAttributeRangeValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE_IN_NORMALIZATION] = ActionInterceptor('SettingScreen: handleAttributeValueChangedForRangeInNormalization', handleAttributeValueChangedForRangeInNormalization);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_PARTNERS_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleRuleDetailPartnerApplyClicked', handleRuleDetailPartnerApplyClicked);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_ENDPOINTS_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleRuleDetailEndpointsChanged', handleRuleDetailEndpointsChanged);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_COLOR_CHANGED] = ActionInterceptor('SettingScreen: handleRuleAttributeColorValueChanged', handleRuleAttributeColorValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_DESCRIPTION_CHANGED] = ActionInterceptor('SettingScreen: handleRuleAttributeDescriptionValueChanged', handleRuleAttributeDescriptionValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_RIGHT_PANEL_BAR_ICON_CLICKED] = ActionInterceptor('SettingScreen: handleRuleRightPanelBarItemClicked', handleRuleRightPanelBarItemClicked);
    oEventHandler[RuleDetailViewEvents.REMOVE_BLACKLIST_ICON_CLICKED] = ActionInterceptor('SettingScreen: handleRuleRemoveBlackListIconClicked', handleRuleRemoveBlackListIconClicked);
    oEventHandler[RuleDetailViewEvents.ATTRIBUTE_VISIBILITY_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleAttributeVisibilityButtonClicked', handleAttributeVisibilityButtonClicked);
    oEventHandler[RuleDetailViewEvents.COMPARE_WITH_SYSTEM_DATE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleAttributeVisibilityButtonClicked', handleCompareWithSystemDateButtonClicked);
    oEventHandler[RuleDetailViewEvents.HANDLE_ATTRIBUTE_VIEW_TYPE_CHANGED] = ActionInterceptor('SettingScreen: handleAttributeViewTypeChanged', handleAttributeViewTypeChanged);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_DESCRIPTION_CHANGED_IN_NORMALIZATION] = ActionInterceptor('SettingScreen: handleRuleAttributeDescriptionValueChangedInNormalization', handleRuleAttributeDescriptionValueChangedInNormalization);
    oEventHandler[RuleDetailViewEvents.RULE_ATTRIBUTE_VALUE_CHANGED_IN_NORMALIZATION] = ActionInterceptor('SettingScreen: handleRuleAttributeValueChangedInNormalization', handleRuleAttributeValueChangedInNormalization);
    oEventHandler[RuleDetailViewEvents.RULE_USER_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleRuleUserValueChanged', handleRuleUserValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_ROLE_CAUSE_EFFECT_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleRoleCauseEffectMSSValueChanged', handleRoleCauseEffectMSSValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_ATTRIBUTE_CAUSE_EFFECT_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleAttributeCauseEffectMSSValueChanged', handleAttributeCauseEffectMSSValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_TAG_CAUSE_EFFECT_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleTagCauseEffectMSSValueChanged', handleTagCauseEffectMSSValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_USER_VALUE_FOR_NORMALIZATION_CHANGED] = ActionInterceptor('SettingScreen: handleRuleUserValueForNormalizationChanged', handleRuleUserValueForNormalizationChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_MSS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleRuleDetailMssValueChanged', handleRuleDetailMssValueChanged);
    oEventHandler[RuleDetailViewEvents.RULE_ENTITIES_ADDED_IN_MERGE_SECTION] = ActionInterceptor('SettingScreen: handleEntitiesAddedInMergeSection', handleEntitiesAddedInMergeSection);
    oEventHandler[RuleDetailViewEvents.RULE_LATEST_ENTITY_SELECTION_TOGGLED] = ActionInterceptor('SettingScreen: handleLatestEntityValueSelectionToggled', handleLatestEntityValueSelectionToggled);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_SELECTED_ENTITY_REMOVED] = ActionInterceptor('SettingScreen: handleLatestEntityValueSelectionToggled', handleGoldenRecordSelectedEntityRemoved);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_SELECTED_SUPPLIER_REMOVED] = ActionInterceptor('SettingScreen: handleLatestEntityValueSelectionToggled', handleGoldenRecordSelectedSupplierRemoved);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_SUPPLIER_SEQUENCE_CHANGED] = ActionInterceptor('SettingScreen: handleLatestEntityValueSelectionToggled', handleRuleDetailSupplierSequenceChanged);
    oEventHandler[RuleDetailViewEvents.RULE_DETAIL_CONCATENATED_FORMULA_CHANGED] = ActionInterceptor('SettingScreen: handleConcatenatedFormulaChanged', handleConcatenatedFormulaChanged);
    // RULE TAG

    oEventHandler[TagTreeGroupViewEvents.TAG_GROUP_TAG_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleTagGroupTagValueChanged', handleTagGroupTagValueChanged);

    //Process
    oEventHandler[ProcessConfigViewEvents.PROCESS_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleProcessDialogButtonClicked', handleProcessDialogButtonClicked);
    oEventHandler[ProcessConfigViewEvents.PROCESS_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleSaveAsDialogValueChanged', handleSaveAsDialogValueChanged);
    oEventHandler[ProcessConfigViewEvents.PROCESS_CONFIG_WORKFLOW_TYPE_CLICKED] = ActionInterceptor('SettingScreen: handleWorkflowTypeClicked', handleWorkflowTypeClicked);
    oEventHandler[ProcessConfigViewEvents.PROCESS_CONFIG_ENTITY_TYPE_CLICKED] = ActionInterceptor('SettingScreen: handleWorkflowTypeClicked', handleEntityTypeClicked);

    //Endpoint
    oEventHandler[EndpointConfigViewEvents.ENDPOINT_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleEndpointDialogButtonClicked', handleEndpointDialogButtonClicked);
    oEventHandler[EndpointConfigViewEvents.ENDPOINT_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleEndpointDialogButtonClicked', handleSaveAsDialogValueChanged);

    //Process Config FullScreen Wrapper View
    oEventHandler[ProcessConfigFullScreenWrapperView.PROCESS_CONFIG_FULLSCREEN_BUTTON_CLICKED] = ActionInterceptor('Process Config Fullscreen Button Clicked', handleProcessConfigFullScreenButtonClicked);

    //Process Component Parameter
    oEventHandler[ComponentParameterViewEvents.COMPONENT_PARAMETER_DATA_SOURCE_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleProcessComponentDataSourceValueChanged', handleProcessComponentDataSourceValueChanged);
    oEventHandler[ComponentParameterViewEvents.COMPONENT_PARAMETER_DATA_SOURCE_CLASS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleProcessComponentClassInfoValueChanged', handleProcessComponentClassInfoValueChanged);
    oEventHandler[ComponentParameterViewEvents.COMPONENT_TAXONOMY_DATA_SOURCE_CLASS_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleProcessComponentClassInfoTaxonomyValueChanged', handleProcessComponentClassInfoTaxonomyValueChanged);
    oEventHandler[ComponentParameterViewEvents.COMPONENT_ADD_TAXONOMY_IN_CLASS_VALUE] = ActionInterceptor('SettingScreen: handleProcessComponentClassInfoAddTaxonomy', handleProcessComponentClassInfoAddTaxonomy);
    oEventHandler[ComponentParameterViewEvents.COMPONENT_REMOVE_TAXONOMY_IN_CLASS_VALUE] = ActionInterceptor('SettingScreen: handleProcessComponentClassInfoRemoveTaxonomy', handleProcessComponentClassInfoRemoveTaxonomy);



    //BPMN Process Component Parameter
    oEventHandler[BPMNWrapperViewEvents.CONFIG_BPMN_WRAPPER_VIEW_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleProcessDesignChanged', handleProcessDesignChanged);
    oEventHandler[BPMNWrapperViewEvents.CONFIG_BPMN_WRAPPER_VIEW_SET_DEFINITIONS] = ActionInterceptor('SettingScreen: handleSetProcessDefinitions', handleSetProcessDefinitions);
    oEventHandler[BPMNWrapperViewEvents.CONFIG_BPMN_WRAPPER_VIEW_CHANGE_UPLOAD_DIALOG_STATUS] = ActionInterceptor('SettingScreen: handleBPMNUploadDialogStatusChanged', handleBPMNUploadDialogStatusChanged);
    oEventHandler[BPMNWrapperViewEvents.CONFIG_BPMN_WRAPPER_VIEW_HANDLE_XML_UPLOAD] = ActionInterceptor('SettingScreen: handleBPMNXMLUpload', handleBPMNXMLUpload);
    oEventHandler[BPMNWrapperViewEvents.CONFIG_BPMN_WRAPPER_VIEW_ACTION_BUTTON_HANDLER] = ActionInterceptor('SettingScreen: handleActionbutton', handleProcessDialogButtonClicked);

    oEventHandler[TaxonomyLevelViewEvents.TAXONOMY_LEVEL_ACTION_ITEM_CLICKED] = ActionInterceptor('SettingScreen: handleTaxonomyLevelActionItemClicked', handleTaxonomyLevelActionItemClicked);
    oEventHandler[TaxonomyLevelViewEvents.TAXONOMY_LEVEL_MASTER_LIST_CHILDREN_ADDED] = ActionInterceptor('SettingScreen: handleTaxonomyLevelMasterListChildrenAdded', handleTaxonomyLevelMasterListChildrenAdded);
    oEventHandler[TaxonomyLevelViewEvents.TAXONOMY_ADD_CHILD_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleTaxonomyLevelMasterListChildrenAdded', handleTaxonomyAddChildButtonClicked);
    oEventHandler[TaxonomyLevelChildItemViewEvents.TAXONOMY_LEVEL_ITEM_CLICKED] = ActionInterceptor('SettingScreen: handleTaxonomyLevelItemClicked', handleTaxonomyLevelItemClicked);
    oEventHandler[TaxonomyLevelChildItemViewEvents.TAXONOMY_LEVEL_ITEM_LABEL_CHANGED] = ActionInterceptor('SettingScreen: handleTaxonomyLevelItemLabelChanged', handleTaxonomyLevelItemLabelChanged);
    oEventHandler[TaxonomyLevelChildItemViewEvents.TAXONOMY_LEVEL_CHILD_ACTION_ITEM_CLICKED] = ActionInterceptor('SettingScreen: handleTaxonomyLevelChildActionItemClicked', handleTaxonomyLevelChildActionItemClicked);

    oEventHandler[BlackListViewEvents.EDIT_ITEM] = ActionInterceptor('SettingScreen: handleEditBlackListItem', handleEditBlackListItem);
    oEventHandler[BlackListViewEvents.ADD_NEW_ITEM] = ActionInterceptor('SettingScreen: handleAddNewBlackListItem', handleAddNewBlackListItem);
    oEventHandler[BlackListViewEvents.REMOVE_ITEM] = ActionInterceptor('SettingScreen: handleRemoveBlackListItem', handleRemoveBlackListItem);

    oEventHandler[ContentMeasurementMetricsEvents.METRIC_UNIT_CHANGED] = ActionInterceptor('SettingScreen: handleMetricUnitChanged', handleMetricUnitChanged);

    oEventHandler[GridViewEvents.GRID_VIEW_SELECT_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewSelectButtonClicked', handleGridViewSelectButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_ACTION_ITEM_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewActionItemClicked', handleGridViewActionItemClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_COLUMN_ACTION_ITEM_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewColumnActionItemClicked', handleGridViewColumnActionItemClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_DELETE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewDeleteButtonClicked', handleGridViewDeleteButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_CREATE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewCreateButtonClicked', handleGridViewCreateButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_SORT_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewSortButtonClicked', handleGridViewSortButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_PAGINATION_CHANGED] = ActionInterceptor('SettingScreen: handleGridPaginationChanged', handleGridPaginationChanged);
    oEventHandler[GridViewEvents.GRID_VIEW_SEARCH_TEXT_CHANGED] = ActionInterceptor('SettingScreen: handleGridViewSearchTextChanged', handleGridViewSearchTextChanged);
    oEventHandler[GridViewEvents.GRID_VIEW_SAVE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewSaveButtonClicked', handleGridViewSaveButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_DISCARD_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewDiscardButtonClicked', handleGridViewDiscardButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_COLUMN_HEADER_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewColumnHeaderClicked', handleGridViewColumnHeaderClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_EXPORT_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewExportButtonClicked', handleGridViewExportButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_REFRESH_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewRefreshButtonClicked', handleGridViewRefreshButtonClicked);
    oEventHandler[TagConfigViewEvents.TAG_CONFIG_VIEW_DATA_LANGUAGE_CHANGED] = ActionInterceptor('SettingScreen: handleTagViewDataLanguageClicked', handleTagViewDataLanguageClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_SAVEAS_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewSaveAsButtonClicked', handleGridViewSaveAsButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_DOWNLOAD_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewDownloadButtonClicked', handleGridViewDownloadButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_FILTER_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewFilterButtonClicked', handleGridViewFilterButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_COLUMN_RESIZER_MODE] = ActionInterceptor('SettingScreen: handleGridViewFilterButtonClicked', handleGridViewResizerButtonClicked);
    oEventHandler[GridFilterViewEvents.GRID_FILTER_APPLIED] = ActionInterceptor('SettingScreen: handleGridFilterApplyClicked', handleGridFilterApplyClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_SHOW_EXPORT_STATUS_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridViewShowExportStatusButtonClicked', handleGridViewShowExportStatusButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_ORGANIZE_COLUMNS_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleGridOrganizeColumnButtonClicked', handleGridOrganizeColumnButtonClicked);


    oEventHandler[TranslationConfigViewEvents.TRANSLATION_PROPERTY_CHANGED] = ActionInterceptor('SettingScreen: handleTranslationPropertyChanged', handleTranslationPropertyChanged);
    oEventHandler[TranslationConfigViewEvents.HANDLE_SELECTED_CLASS_MODULE_CHANGED] = ActionInterceptor('SettingScreen: handleSelectedChildModuleChanged', handleSelectedChildModuleChanged);
    oEventHandler[TranslationConfigViewEvents.SELECTED_LANGUAGES_CHANGED] = ActionInterceptor('SettingScreen: handleTranslationLanguagesChanged', handleTranslationLanguagesChanged);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_VALUE_CHANGED] = ActionInterceptor('SettingScreen: handleGridPropertyValueChanged', handleGridPropertyValueChanged);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_VALUE_CREATED] = ActionInterceptor('SettingScreen: handleGridPropertyValueChanged', handleGridPropertyValueCreated);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_KEY_EVENT] = ActionInterceptor('SettingScreen: handleGridPropertyKeyDownEvent', handleGridPropertyKeyDownEvent);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_PARENT_EXPAND_TOGGLED] = ActionInterceptor('SettingScreen: handleGridParentExpandToggled', handleGridParentExpandToggled);
    oEventHandler[GridPropertyViewEvents.GRID_PROPERTY_CLEAR_SHOULD_FOCUS] = ActionInterceptor('SettingScreen: handleGridPropertyClearShouldFocus', handleGridPropertyClearShouldFocus);
    oEventHandler[GridImagePropertyViewEvents.GRID_IMAGE_PROPERTY_IMAGE_CHANGED] = ActionInterceptor('SettingScreen: handleGridImagePropertyImageChanged', handleGridImagePropertyImageChanged);
    oEventHandler[GridMSSWithAdditionalListEvents.GRID_MSS_ADDITIONAL_LIST_ITEM_ADDED] = ActionInterceptor('SettingScreen: handleGridMSSAdditionalListItemAdded', handleGridMSSAdditionalListItemAdded);
    oEventHandler[GridImagePropertyViewEvents.GRID_GET_ASSET_EXTENSIONS] = ActionInterceptor('SettingScreen: gridGetAssetExtensions', handleGetAssetExtensions);

    oEventHandler[NewMultiSelectSearchViewEvents.MULTI_SELECT_POPOVER_CLOSED] = ActionInterceptor('SettingScreen: handleNewMSSViewPopOverClosed', handleNewMSSViewPopOverClosed);
    oEventHandler[NewMultiSelectSearchViewEvents.MULTI_SELECT_SEARCH_HANDLE] = ActionInterceptor('SettingScreen: handleMssSearchClicked', handleMssSearchClicked);
    oEventHandler[NewMultiSelectSearchViewEvents.MULTI_SELECT_SEARCH_LOAD_MORE_HANDLE] = ActionInterceptor('SettingScreen: handleMssLoadMoreClicked', handleMssLoadMoreClicked);
    oEventHandler[NewMultiSelectSearchViewEvents.MULTI_SELECT_POPOVER_OPENED] = ActionInterceptor('SettingScreen: handleMssLoadMoreClicked', handleMssPopOverOpened);
    oEventHandler[NewMultiSelectSearchViewEvents.MULTI_SELECT_CREATE_HANDLE] = ActionInterceptor('SettingScreen: handleMssCreateClicked', handleMssCreateClicked);


    oEventHandler[DataTransferViewEvents.DATA_TRANSFER_PROPERTY_COUPLING_CHANGE] = ActionInterceptor('SettingScreen: handleDataTransferPropertyCouplingChange', handleDataTransferPropertyCouplingChange);
    oEventHandler[DataTransferViewEvents.DATA_TRANSFER_VIEW_SEARCH_LOAD_MORE_PROPERTIES] = ActionInterceptor('SettingScreen: handleDataTransferSearchLoadMore', handleDataTransferSearchLoadMore);
    oEventHandler[DataTransferViewEvents.DATA_TRANSFER_VIEW_ADD_PROPERTIES] = ActionInterceptor('SettingScreen: handleDataTransferAddProperties', handleDataTransferAddProperties);
    oEventHandler[DataTransferViewEvents.DATA_TRANSFER_REMOVE_PROPERTY] = ActionInterceptor('SettingScreen: handleDataTransferPropertyRemove', handleDataTransferPropertyRemove);


    oEventHandler[ContextualAttributeEditSectionSelectViewEvents.HANDLE_UNIQUE_SELECTION_CHANGED] = ActionInterceptor('SettingScreen: handleContextualTagCombinationUniqueSelectionChanged', handleContextualTagCombinationUniqueSelectionChanged);
    oEventHandler[ContextualAttributeEditSectionSelectViewEvents.HANDLE_DELETE_UNIQUE_SELECTION] = ActionInterceptor('SettingScreen: handleContextualTagCombinationUniqueSelectionDelete', handleContextualTagCombinationUniqueSelectionDelete);
    oEventHandler[ContextualAttributeEditSectionSelectViewEvents.HANDLE_UNIQUE_SELECTION_OK] = ActionInterceptor('SettingScreen: handleContextualTagCombinationUniqueSelectionOk', handleContextualTagCombinationUniqueSelectionOk);

    //Tag selector view events
    oEventHandler[TagSelectorViewEvents.TAG_SELECTOR_VIEW_REMOVE_SELECTED_TAG_GROUP] = ActionInterceptor('SettingScreen: handleRemoveSelectedTagButtonClicked', handleRemoveSelectedTagButtonClicked);
    oEventHandler[TagSelectorViewEvents.TAG_SELECTOR_VIEW_TAG_VALUE_LIST_APPLY_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleTagValueListApplyButtonClicked', handleTagValueListApplyButtonClicked);
    oEventHandler[TagSelectorViewEvents.TAG_SELECTOR_VIEW_TAG_LIST_APPLY_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleTagListApplyButtonClicked', handleTagListApplyButtonClicked);
    oEventHandler[TagSelectorViewEvents.TAG_SELECTOR_VIEW_TAG_LIST_LOAD_MORE_CLICKED] = ActionInterceptor('SettingScreen: handleTagListLoadMoreClicked', handleTagListLoadMoreClicked);
    oEventHandler[TagSelectorViewEvents.TAG_SELECTOR_VIEW_TAG_LIST_SEARCH_CLICKED] = ActionInterceptor('SettingScreen: handleTagListSearchTextClicked', handleTagListSearchTextClicked);

    oEventHandler[CalculatedAttributeFormulaViewEvents.CALCULATED_ATTRIBUTE_GET_ALLOWED_ATTRIBUTES] = ActionInterceptor('SettingScreen: handleContextualAttributeTagCombinationSelectorClicked', handleGetAllowedAttributesForCalculatedAttribute);
    oEventHandler[GridCalculatedAttributePropertyViewEvents.GRID_CALCULATED_ATTRIBUTE_POPOVER_OPENED] = ActionInterceptor('SettingScreen: handleContextualAttributeTagCombinationSelectorClicked', handleGridCalculatedAttributePopoverOpened);

    oEventHandler[ClassContextDialogViewEvents.CLASS_CONTEXT_DIALOG_OPEN_CLICKED] = ActionInterceptor('SettingScreen: classContextDialogOpenClicked', handleClassContextDialogOpenClicked);
    oEventHandler[ClassContextDialogViewEvents.CLASS_CONTEXT_DIALOG_OK_CLICKED] = ActionInterceptor('SettingScreen: classContextDialogOkClicked', handleClassContextDialogOkClicked);
    oEventHandler[ClassContextDialogViewEvents.CLASS_CONTEXT_DIALOG_DISCARD_CLICKED] = ActionInterceptor('SettingScreen: classContextDialogDiscardClicked', handleClassContextDialogDiscardClicked);
    oEventHandler[ClassContextDialogViewEvents.CLASS_CONTEXT_DIALOG_CLOSED] = ActionInterceptor('SettingScreen: classContextDialogClosed', handleClassContextDialogClosed);
    oEventHandler[ClassContextDialogViewEvents.CLASS_CONTEXT_DIALOG_DATE_VALUE_CHANGED] = ActionInterceptor('SettingScreen: classContextDateValueChanged', handleClassContextDialogDateValueChanged);
    oEventHandler[ClassContextDialogViewEvents.CLASS_CONTEXT_DIALOG_ADD_TAG_COMBINATION] = ActionInterceptor('SettingScreen: classContextAddTagCombination', handleClassContextDialogAddTagCombination);

    oEventHandler[AttributionTaxonomyConfigViewEvents.ATTRIBUTION_TAXONOMY_CONFIG_CREATE_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: attributionTaxonomyConfigCreateDialogButtonClicked', handleAttributionTaxonomyCreateDialogButtonClicked);
    oEventHandler[AttributionTaxonomyConfigViewEvents.ATTRIBUTION_TAXONOMY_LIST_CONFIG_LEVEL_ADDED] = ActionInterceptor('SettingScreen: handleTaxonomyMasterListLevelAdded', handleAttributionTaxonomyListLevelAdded);
    oEventHandler[AttributionTaxonomyConfigViewEvents.ATTRIBUTION_TAXONOMY_ADD_NEW_LEVEL] = ActionInterceptor('SettingScreen: handleTaxonomyMasterListLevelAdded', handleAttributionTaxonomyListLevelAddButtonClicked);
    oEventHandler[AttributionTaxonomyConfigViewEvents.ATTRIBUTION_TAXONOMY_DIALOG_CLOSE] = ActionInterceptor('SettingScreen: handleAttributionTaxonomyDialogClose', handleAttributionTaxonomyDialogClose);
    oEventHandler[AttributionTaxonomyConfigViewEvents.ATTRIBUTION_TAXONOMY_HIERARCHY_HORIZONTAL_TOGGLE_AUTOMATIC_SCROLL] = ActionInterceptor('SettingScreen: handleAttributionTaxonomyHierarchyToggleAutomaticScrollProp', handleAttributionTaxonomyHierarchyToggleAutomaticScrollProp);
    oEventHandler[AttributionTaxonomyConfigViewEvents.ATTRIBUTION_TAXONOMY_IMPORT_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleTaxonomyImportButtonClicked', handleAttributionTaxonomyImportButtonClicked);
    oEventHandler[AttributionTaxonomyConfigViewEvents.HANDLE_ATTRIBUTE_TAXONOMY_TAB_CHANGED] = ActionInterceptor('SettingScreen:' +
        ' handleAttributeTaxonomyChanged', handleAttributionTaxonomyTabChanged);




    //Relationship
    oEventHandler[RelationshipConfigViewEvents.HANDLE_RELATIONSHIP_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleRelationshipConfigDialogButtonClicked', handleRelationshipConfigDialogButtonClicked);

    //KPI Config Events
    oEventHandler[KPIConfigViewEvents.HANDLE_KPI_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleKPIConfigDialogButtonClicked', handleKPIConfigDialogButtonClicked);
    oEventHandler[KPIConfigViewEvents.HANDLE_KPI_CONFIG_TAB_CHANGED] = ActionInterceptor('SettingScreen: handleKPIConfigDialogTabChanged', handleKPIConfigDialogTabChanged);

    //Rule config events
    oEventHandler[RuleConfigViewEvents.HANDLE_RULE_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: ', handleRuleConfigDialogActionButtonClicked);

    oEventHandler[AddTaxonomyPopoverViewEvents.TAXONOMY_DROPDOWN_OPENED] = ActionInterceptor('Add Taxonomy Popover Item View Dropdown', handleAllowedTypesDropdownOpened);
    oEventHandler[AddTaxonomyPopoverViewEvents.ADD_TAXONOMY_POPOVER_ITEM_CLICKED] = ActionInterceptor('add taxonomy popover item View', handleAddTaxonomyPopoverItemClicked);
    oEventHandler[AddTaxonomyPopoverViewEvents.TAXONOMY_DROPDOWN_SEARCH_HANDLER] = ActionInterceptor('taxonomy dropdown search handler', handleAddTaxonomySearch);
    oEventHandler[AddTaxonomyPopoverViewEvents.TAXONOMY_DROPDOWN_LOAD_MORE_HANDLER] = ActionInterceptor('taxonomy dropdown load more handler', handleAddTaxonomyLoadMore);
    oEventHandler[SmallTaxonomyViewEvents.MULTI_TAXONOMY_CROSS_ICON_CLICKED] = ActionInterceptor('EntityDetail View', handleMultiSelectSmallTaxonomyViewCrossIconClicked);

    oEventHandler[KPIDetailedViewEvents.HANDLE_KPI_DROP_DOWN_CLICKED] = ActionInterceptor('KPI Rule dropdown value changed', handleKPIMSSValueChanged);
    oEventHandler[KPIDetailedViewEvents.HANDLE_KPI_PARTNER_APPLY_CLICKED] = ActionInterceptor('KPI Rule dropdown value changed', handleKPIPartnerApplyClicked);
    oEventHandler[KPIDetailedViewEvents.HANDLE_KPI_ADD_RULE_CLICKED] = ActionInterceptor('KPI Add Rule Clicked', handleKPIAddRuleClicked);
    oEventHandler[KPIDetailedViewEvents.HANDLE_KPI_THRESHOLD_VALUE_CHANGED] = ActionInterceptor('KPI Add Rule Clicked', handleKPIThresholdValueChanged);
    oEventHandler[KPIDetailedViewEvents.HANDLE_KPI_RULE_DELETE_CLICKED] = ActionInterceptor('KPI Add Rule Deleted', handleDeleteRuleClicked);
    oEventHandler[KPIDetailedViewEvents.HANDLE_KPI_MSS_SEARCH_CLICKED] = ActionInterceptor('KPI MSS Search Clicked', handleMssSearchClicked);
    oEventHandler[KPIDetailedViewEvents.HANDLE_KPI_MSS_LOAD_MORE_CLICKED] = ActionInterceptor('KPI MSS Search Clicked', handleRuleDetailsMSSLoadMoreClicked);
    oEventHandler[KPIDetailedViewEvents.HANDLE_KPI_MSS_CREATE_CLICKED] = ActionInterceptor('KPI MSS Search Clicked', handleKPIMssCreateClicked);


    oEventHandler[KPIDrilldownViewEvents.HANDLE_ADD_NEW_KPI_DRILLDOWN_LEVEL_CLICKED] = ActionInterceptor('Add New Drilldown KPI Level', handleAddNewKPIDrilldownLevelClicked);
    oEventHandler[KPIDrilldownViewEvents.HANDLE_REMOVE_KPI_DRILLDOWN_LEVEL_CLICKED] = ActionInterceptor('Remove Drilldown KPI Level', handleRemoveKPIDrilldownLevelClicked);
    oEventHandler[KPIDrilldownViewEvents.HANDLE_KPI_DRILLDOWN_LEVEL_MSS_VALUE_CHANGED] = ActionInterceptor('KPI Drilldown MSS value changed', handleKPIDrilldownMssValueChanged);

    oEventHandler[RolePermissionContainerViewEvents.ROLE_PERMISSION_VIEW_TOGGLE_BUTTON_CLICKED] = ActionInterceptor('Role Permission View Toggle Button Clicked', handleRolePermissionToggleButtonClicked);
    oEventHandler[RolePermissionContainerViewEvents.ROLE_PERMISSION_VIEW_SAVE_ROLE_CLICKED] = ActionInterceptor('Role Permission View Save Button Clicked', handleRolePermissionSaveRoleClicked);


    //OrganisationConfig

    oEventHandler[OrganisationConfigDetailViewEvents.HANDLE_ORGANISATION_TAB_CHANGED] = ActionInterceptor('SettingScreen: handleOrganisationConfigTabChanged', handleOrganisationConfigTabChanged);

    //Tabs Config
    oEventHandler[TabsConfigViewEvents.HANDLE_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleTabsConfigDialogButtonClicked', handleTabsConfigDialogButtonClicked);
    oEventHandler[TabsConfigViewEvents.TABS_CONFIG_VIEW_LIST_ITEM_SHUFFLED] = ActionInterceptor('SettingScreen: handleTabsConfigListItemShuffled', handleTabsConfigListItemShuffled);

    oEventHandler[TabsSortDialogViewEvents.HANDLE_SORT_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleTabsSortDialogButtonClicked', handleTabsSortDialogButtonClicked);
    oEventHandler[TabsSortDialogViewEvents.TABS_SORT_VIEW_LIST_ITEM_SHUFFLED] = ActionInterceptor('SettingScreen: handleTabsSortListItemShuffled', handleTabsSortListItemShuffled);

    oEventHandler[ConfigEntityCreationDialogViewEvents.HANDLE_ENTITY_CONFIG_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: tagConfigDialogButtonClicked', handleEntityConfigDialogButtonClicked);
    oEventHandler[ManageEntityDialogViewEvents.HANDLE_MANAGE_ENTITY_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: ManageEntityDialogCloseButtonClicked', handleManageEntityDialogButtonClicked);
    oEventHandler[GridViewEvents.GRID_VIEW_MANAGE_ENTITY_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: ManageEntityMultiUsageDialogOpenButtonClicked', handleManageEntityMultiUsageButtonClicked);

    oEventHandler[ConfigFileUploadButtonViewEvents.CONFIG_FILE_UPLOAD_EVENT] = ActionInterceptor('Bulk Upload To Collection Closed', handleGridViewImportButtonClicked);
    oEventHandler[ConfigScreenViewEvents.HANDLE_CONFIG_TAB_CLICKED] = ActionInterceptor('SettingScreen: configScreenViewTabClicked', handleConfigScreenTabClicked);

    oEventHandler[ConfigScreenViewEvents.SETTING_SCREEN_LEFT_NAVIGATION_TREE_ITEM_CLICKED] = ActionInterceptor('SettingScreen: leftNavigationTreeItemClicked', handleSettingScreenLeftNavigationTreeItemClicked);

    //Expandable Menu List View Events
    oEventHandler[ExpandableNestedMenuListViewEvents.EXPANDABLE_NESTED_MENU_LIST_EXPAND_TOGGLED] = ActionInterceptor('SettingScreen: handleExpandableMenuListExpandToggled', handleMenuListExpandToggled);


    oEventHandler[UserProfileView.USER_PROFILE_VIEW_HANDLE_USER_CREATE_CLICKED] = ActionInterceptor('SettingScreen: handleCreateUserClicked', handleUserCreateClicked);
    oEventHandler[UserProfileView.USER_PROFILE_VIEW_HANDLE_USER_CREATE_CANCEL_CLICKED] = ActionInterceptor('SettingScreen: handleCreateUserCancelClicked', handleCreateUserCancelClicked);
    oEventHandler[UserProfileView.CREATE_USER_PROFILE_VIEW_HANDLE_USER_DATA_CHANGED] = ActionInterceptor('SettingScreen: handleUserDataChangeEvent', handleUserDataChangeEvent);
    oEventHandler[UserProfileView.CREATE_USER_PROFILE_VIEW_HANDLE_USER_IMAGE_UPLOAD_CLICKED] = ActionInterceptor('SettingScreen: handleUploadImageChangeEvent', handleUploadImageChangeEvent);
    oEventHandler[UserProfileView.CREATE_USER_PROFILE_VIEW_HANDLE_REMOVE_USER_IMAGE_CLICKED] = ActionInterceptor('Remove User Image Clicked', handleRemoveUserImageClicked);

    //Horizontal Tree View events
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_CLICKED] = ActionInterceptor('Content Horizontal Tree Node Clicked Load Children', handleContentHorizontalTreeNodeClicked);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_DELETE_ICON_CLICKED] = ActionInterceptor('Content Horizontal Tree Node Delete Clicked', handleContentHorizontalTreeNodeDeleteClicked);
    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_VIEW_PERMISSION_TOGGLED] = ActionInterceptor('Content Horizontal Tree Node Permission Toggled', handleContentHorizontalTreeNodePermissionToggled);
    oEventHandler[ContentHorizontalTreeNodeViewEvents.HANDLE_ACTION_ITEM_CLICKED] = ActionInterceptor('Action Item clicked', handleHorizontalHierarchyTreeActionItemClicked);
    oEventHandler[ContentHorizontalTreeNodeViewEvents.HANDLE_HIERARCHY_TREE_TAB_CHANGED] = ActionInterceptor('Horizontal Hierarchy Tree Node Tab Changed', handleHorizontalHierarchyTreeTabChanged);

    oEventHandler[RuleListConfigViewEvents.RULE_LIST_LABEL_CHANGED] = ActionInterceptor('Action Item clicked', handleRuleListLabelChanged);

    oEventHandler[LazyMssView.MULTI_SELECT_UPDATE_VIEW_PROPS] = ActionInterceptor('Update View Props with Lazy Mss Props', handleUpdateViewProps);

    oEventHandler[ContentHorizontalTreeViewEvents.HANDLE_CONTENT_HORIZONTAL_TREE_NODE_TOGGLE_AUTOMATIC_SCROLL_PROP] = ActionInterceptor('Horizontal Tree Node Toggle Auto Scroll Prop', handleHorizontalTreeNodeToggleAutomaticScrollProp);

    oEventHandler[SystemsSelectionViewEvents.SYSTEMS_SELECTION_VIEW_SYSTEM_ADDED] = ActionInterceptor('Handle System Select Context Menu Clicked', handleSystemSelectApplyClicked);

   oEventHandler[EndpointsSelectionViewEvents.ENDPOINTS_SELECTION_VIEW_SELECT_ALL_CHECKED] = ActionInterceptor('Handle System Select Context Menu Clicked', handleEndpointSelectionViewSelectAllChecked);

   oEventHandler[EndpointsSelectionViewEvents.ENDPOINTS_SELECTION_VIEW_DELETE_CLICKED] = ActionInterceptor('Handle System Select Context Menu Clicked', handleEndpointSelectionViewDeleteClicked);

   oEventHandler[ClassConfigDataTransferView.HANDLE_REMOVE_KLASS_CLICKED] = ActionInterceptor('Handle remove klass clicked', handleRemoveKlassClicked);
   oEventHandler[ClassConfigDataTransferView.DATA_TRANSFER_VIEW_ADD_PROPERTIES] = ActionInterceptor('Handle data transfer properties added', handleClassDataTransferPropertiesAdded);
   oEventHandler[ClassConfigDataTransferView.DATA_TRANSFER_REMOVE_PROPERTY] = ActionInterceptor('Handle data transfer properties removed', handleClassDataTransferPropertiesRemoved);
   oEventHandler[ClassConfigDataTransferView.DATA_TRANSFER_PROPERTY_COUPLING_CHANGE] = ActionInterceptor('Handle data transfer properties coupling changed', handleClassDataTransferPropertiesCouplingChanged);

   oEventHandler[LanguageSelectionView.LANGUAGE_SELECTION_VIEW_HANDLE_LANGUAGE_CHANGED] = ActionInterceptor('User Language Changed: handleUILanguageChanged', handleUILanguageChanged);

   oEventHandler[CustomSearchDialogView.HANDLE_SEARCH_ITEM_CLICKED] = ActionInterceptor('Handle Search Item Clicked', handleConfigModuleSearchItemClicked);
    oEventHandler[CustomSearchDialogView.HANDLE_SEARCH_DIALOG_CLOSED_CLICKED] = ActionInterceptor('Handle Search Dialog Close Clicked', handleConfigModuleSearchDialogClosedClicked);

    oEventHandler[PropertyCollectionConfigView.HANDLE_PROPERTY_COLLECTION_IMPORT_BUTTON_CLICKED] = ActionInterceptor('Handle Search Dialog Close Clicked', handlePropertyCollectionImportButtonClicked);
    oEventHandler[PropertyCollectionConfigView.HANDLE_PROPERTY_COLLECTION_TAB_CHANGED] = ActionInterceptor('Handle Property Collection Tab changed', handlePropertyCollectionTabChanged);

    oEventHandler[SSOConfigScreenViewEvents.HANDLE_SSO_SETTING_REFERESH] = ActionInterceptor('Handle Referesh sso setting',handleSsoRefreshButtonClicked);
    oEventHandler[OrgnisationConfigView.HANDLE_ORGANISATION_IMPORT_BUTTON_CLICKED] = ActionInterceptor('Handle Search Dialog Close Clicked', handleOrganisationImportButtonClicked);

    oEventHandler[LanguageTreeConfigView.HANDLE_LANGUAGE_TREE_DIALOG_CANCEL_CLICKED] = ActionInterceptor('Handle Language Tree Dialog Cancel Clicked', handleLanguageTreeDialogCancelClicked);

    oEventHandler[LanguageTreeConfigView.HANDLE_LANGUAGE_TREE_DIALOG_CREATE_CLICKED] = ActionInterceptor('Handle Language Tree Dialog Create Clicked', handleLanguageTreeDialogCreateClicked);

    oEventHandler[LanguageTreeConfigView.HANDLE_LANGUAGE_TREE_SNACK_BAR_BUTTON_CLICKED] = ActionInterceptor('Handle Language Tree Snack Bar Button Clicked', handleLanguageTreeSnackBarButtonClicked);

    oEventHandler[LanguageTreeConfigView.LANGUAGE_TREE_CONFIG_IMPORT_BUTTON_CLICKED] = ActionInterceptor('Handle Language Tree Import Button Clicked', handleLanguageTreeImportButtonClicked);

    // SmartDocumentConfigView
    oEventHandler[SmartDocumentConfigView.HANDLE_SMART_DOCUMENT_DAILOG_BUTTON_CLICKED] =
        ActionInterceptor('Handle Smart Document Dialog Button Clicked', handleSmartDocumentDialogButtonClicked);
    oEventHandler[SmartDocumentConfigView.HANDLE_SMART_DOCUMENT_SNACK_BAR_BUTTON_CLICKED] =
        ActionInterceptor('Handle Smart Document Snack Bar Button Clicked', handleSmartDocumentSnackBarButtonClicked);
    //HANDLE_SMART_DOCUMENT_PRESET_DROP_DOWN_CLICKED
    oEventHandler[SmartDocumentConfigView.HANDLE_SMART_DOCUMENT_PRESET_DROP_DOWN_CLICKED] =
        ActionInterceptor('Handle Smart Document Preset Drop Down Clicked', handleSmartDocumentPresetMSSValueClicked);

    oEventHandler[ThemeConfigurationView.THEME_CONFIGURATION_SNACK_BAR_BUTTON_CLICKED] =
        ActionInterceptor('Handle Theme Configuration Snack Bar Button Clicked', handleThemeConfigurationSnackBarButtonClicked);

    oEventHandler[ViewConfigurationView.VIEW_CONFIGURATION_SNACK_BAR_BUTTON_CLICKED] =
        ActionInterceptor('Handle View Configuration Snack Bar Button Clicked', handleViewConfigurationSnackBarButtonClicked);

    //Item panel view
    oEventHandler[ItemListPanelViewEvents.HANDLE_LIST_SEARCHED] = ActionInterceptor('Item List Panel View Events', handleAdvancedSearchListSearched);
    oEventHandler[ItemListPanelViewEvents.HANDLE_SEARCH_LIST_ITEM_NODE_CLICKED] = ActionInterceptor('Item List Panel View Events', handleAdvancedSearchListItemNodeClicked);
    oEventHandler[ItemListPanelViewEvents.HANDLE_SEARCH_LIST_LOAD_MORE_CLICKED] = ActionInterceptor('Item List Panel View Events', handleAdvancedSearchListLoadMoreClicked);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_DELETE_CLICKED] = ActionInterceptor('Element Delete', handleFilterElementDeleteClicked);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_EXPAND_CLICKED] = ActionInterceptor('Element Expand', handleFilterElementExpandClicked);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_TYPE_CLICKED] = ActionInterceptor('ValueType Changed', handleFilterAttributeValueTypeChanged);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ADD_ATTRIBUTE_CLICKED] = ActionInterceptor('Attribute Add ', handleAddAttributeClicked);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED] = ActionInterceptor('Attibute Value Changed', handleFilterAttributeValueChanged);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE] = ActionInterceptor('Attibute Value Range Changed ', handleFilterAttributeValueChangedForRange);
    oEventHandler[ContentFilterElementsViewEvents.CONTENT_FILTER_ATTRIBUTE_VALUE_DELETE_CLICKED] = ActionInterceptor('Attribute Delete', handleFilterAttributeValueDeleteClicked);

    //Advanced Search Panel View
    oEventHandler[AdvancedSearchPanelViewEvents.HANDLE_ADVANCED_SEARCH_CANCEL_CLICKED] = ActionInterceptor('Cancel Button Clicked', handleAdvancedSearchPanelCancelClicked);
    oEventHandler[AdvancedSearchPanelViewEvents.CONTENT_FILTER_BUTTON_CLICKED] = ActionInterceptor('Apply Button Clicked', handleAdvancedSearchFilterButtonCLicked);
    oEventHandler[AdvancedSearchPanelViewEvents.CONTENT_FILTER_CLEAR_BUTTON_CLICKED] = ActionInterceptor('clear Button Clicked', handleAdvancedSearchPanelClearClicked);


    //Advanced Search Panel View
    oEventHandler[AppliedFilterEvents.CLEAR_ALL_APPLIED_FILTERS] = ActionInterceptor('clear Button Clicked', handleAppliedFilterClearClicked);
    oEventHandler[AppliedFilterEvents.REMOVE_APPLIED_GROUP_FILTER] =  ActionInterceptor('remove applied group filter', handleRemoveAppliedFilterClicked);

    oEventHandler[OrgnisationConfigView.HANDLE_ORGANISATION_IMPORT_BUTTON_CLICKED] = ActionInterceptor('Handle Search Dialog Close Clicked', handleOrganisationImportButtonClicked);

    //Authorization Mapping View
    oEventHandler[AuthorizationMappingEvents.AUTHORIZATION_MAPPING_APPLY_BUTTON_CLICKED] = ActionInterceptor('Handle Apply Button Clicked', handleAuthorizationMappingApplyButtonClicked);
    oEventHandler[AuthorizationMappingEvents.AUTHORIZATION_MAPPING_CHECKBOX_BUTTON_CLICKED] = ActionInterceptor('Handle CheckBox Button Clicked', handleAuthorizationMappingCheckboxButtonClicked);
    oEventHandler[AuthorizationMappingEvents.AUTHORIZATION_MAPPING_DELETE_BUTTON_CLICKED] = ActionInterceptor('Handle Delete Button Clicked', handleAuthorizationMappingDeleteButtonClicked);
    oEventHandler[AuthorizationMappingEvents.AUTHORIZATION_MAPPING_YES_NO_BUTTON_CHANGED] = ActionInterceptor('Handle Yes No Button Clicked', handleAuthorizationMappingToggleButtonClicked);

    oEventHandler[ColumnOrganizerEvents.HANDLE_COLUMN_ORGANIZER_VIEW_SAVE_BUTTON_CLICKED] = ActionInterceptor(
      'handle_column_organizer_view_save_button_clicked', handleColumnOrganizerSaveButtonClicked);

    oEventHandler[ColumnOrganizerDialogEvents.COLUMN_ORGANIZER_DIALOG_BUTTON_CLICKED] = ActionInterceptor(
      'Grid Editable Property Sequence Shuffled', handleColumnOrganizerDialogButtonClicked);

    oEventHandler[FilterViewEvents.HANDLE_FILTER_CHILD_TOGGLED] = ActionInterceptor(
      'Handle Filter Child Toggled', handleFilterChildToggled);

    oEventHandler[FilterViewEvents.HANDLE_FILTER_SUMMARY_SEARCH_TEXT_CHANGED] = ActionInterceptor(
      'Handle Filter Search Text Changed', handleFilterSummarySearchTextChanged);

    oEventHandler[FilterViewEvents.HANDLE_COLLAPSE_FILTER_CLICKED] = ActionInterceptor(
      'Handle Collapse Filter Clicked', handleCollapseFiltersClicked);

    oEventHandler[FilterViewEvents.HANDLE_APPLY_FILTER_BUTTON_CLICKED] = ActionInterceptor(
      'Handle Apply Filter Button Clicked', handleApplyFilterButtonClicked);

    oEventHandler[FilterViewEvents.HANDLE_FILTER_ITEM_POPOVER_CLOSED] = ActionInterceptor(
      'Handle Filter Item Popover Closed', handleFilterItemPopoverClosed);

    oEventHandler[FilterViewEvents.HANDLE_FILTER_ITEM_SEARCH_TEXT_CHANGED] = ActionInterceptor(
      'Handle Filter Item Search Text Changed', handleFilterItemSearchTextChanged);

    // HomeScreenCommunicator Events
    oEventHandler[HomeScreenCommunicator.HANDLE_UI_LANGUAGE_CHANGED_FROM_CONFIG] = ActionInterceptor(
        'handle language changed', handleUILanguageChanged);

    // Relationship Inheritance
    oEventHandler[RelationshipInheritanceViewEvents.RELATIONSHIP_INHERITANCE_APPLY_CLICKED] = ActionInterceptor('SettingScreen: handleRelationshipInheritanceApplyClicked', handleRelationshipInheritanceApplyClicked);
    oEventHandler[RelationshipInheritanceViewEvents.RELATIONSHIP_INHERITANCE_REMOVE_PROPERTY] = ActionInterceptor('SettingScreen: handleRelationshipInheritanceRemoveProperty', handleDataTransferPropertyRemove);
    oEventHandler[RelationshipInheritanceViewEvents.RELATIONSHIP_INHERITANCE_PROPERTY_COUPLING_CHANGE] = ActionInterceptor('SettingScreen: handleRelationshipInheritanceCouplingChange', handleDataTransferPropertyCouplingChange);

    oEventHandler[CustomDateRangePickerViewEvents.CUSTOM_DATE_RANGE_PICKER_HANDLE_APPLY_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: date range picker update date', handleDateRangePickerUpdateData);

    // Audit Log
    oEventHandler[AuditLogExportStatusViewEvents.AUDIT_LOG_DIALOG_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: auditLogDialogButtonClicked',handleAuditLogExportStatusDialogButtonClicked);
    oEventHandler[AuditLogExportStatusViewEvents.AUDIT_LOG_EXPORT_DIALOG_REFRESH_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: auditLogExportDialogRefreshButtonClicked',handleAuditLogExportDialogRefreshButtonClicked);
    oEventHandler[AuditLogExportStatusViewEvents.AUDIT_LOG_EXPORT_DIALOG_PAGINATION_CHANGED] = ActionInterceptor('SettingScreen: auditLogExportDialogPaginationChanged', handleAuditLogExportDialogPaginationChanged)

    //Icon Library
    oEventHandler[IconLibraryViewEvents.ICON_ELEMENT_CHECKBOX_CLICKED] = ActionInterceptor('Icon Library: Element Checkbox Clicked', handleIconElementCheckboxClicked);
    oEventHandler[IconLibraryViewEvents.ICON_LIBRARY_PAGINATION_CHANGED] = ActionInterceptor('Icon Library: Pagination Changed', handleIconLibraryPaginationChanged);
    oEventHandler[IconLibraryEditCodeDialogViewEvents.HANDLE_DIALOG_SAVE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleDialogSaveButtonClicked', handleDialogSaveButtonClicked);
    oEventHandler[IconLibraryEditCodeDialogViewEvents.HANDLE_DIALOG_CANCEL_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleDialogCancelButtonClicked', handleDialogCancelButtonClicked);
    oEventHandler[IconLibraryEditCodeDialogViewEvents.HANDLE_DIALOG_INPUT_CHANGED] = ActionInterceptor('SettingScreen: handleDialogInputChanged', handleDialogInputChanged);
    oEventHandler[FileDragAndDropViewEvents.FILE_DRAG_DROP_DRAGGED_HANDLER] = ActionInterceptor('File drag n drop: dragged handler', handleFileDragDropViewDraggingState);
    oEventHandler[FileDragAndDropViewEvents.FILE_DRAG_DROP_HANDLE_FILE_DROP] = ActionInterceptor('File drag n drop: Handle file drop', handleFileDrop);
    oEventHandler[IconLibraryEditCodeDialogViewEvents.HANDLE_DIALOG_LIST_REMOVE_BUTTON_CLICKED] = ActionInterceptor('handle_dialog_list_remove_button_clicked', handleDialogListRemoveButtonClicked);
    oEventHandler[IconLibraryViewEvents.ICON_ELEMENT_ACTION_BUTTON_CLICKED] = ActionInterceptor('SettingScreen: handleIconElementActionButtonClicked', handleIconElementActionButtonClicked);
    oEventHandler[IconEditViewEvents.HANDLE_ICON_ELEMENT_CANCEL_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:handleIconElementCancelIconClicked', handleIconElementCancelIconClicked);
    oEventHandler[IconEditViewEvents.HANDLE_ICON_ELEMENT_SAVE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:handleIconElementSaveIconClicked', handleIconElementSaveIconClicked);
    oEventHandler[IconEditViewEvents.HANDLE_ICON_ELEMENT_REPLACE_BUTTON_CLICKED] = ActionInterceptor('SettingScreen:handleIconElementReplaceIconClicked', handleIconElementReplaceIconClicked);
    oEventHandler[IconEditViewEvents.HANDLE_ICON_ELEMENT_ICON_NAME_CHANGED] = ActionInterceptor('SettingScreen:handleIconElementIconNameChanged', handleIconElementIconNameChanged);
    oEventHandler[IconLibrarySelectIconViewEvents.SELECT_ICON_SELECT_BUTTON_CLICKED] = ActionInterceptor('IconLibrary: selectIconSelectButtonClicked', handleSelectIconSelectClicked);
    oEventHandler[IconLibrarySelectIconViewEvents.SELECT_ICON_CANCEL_BUTTON_CLICKED] = ActionInterceptor('IconLibrary: selectIconCancelButtonClicked', handleSelectIconCancelClicked);
    oEventHandler[IconLibrarySelectIconViewEvents.SELECT_ICON_ELEMENT_SELECTED] = ActionInterceptor('IconLibrary: selectIconElementSelected', handleSelectIconElementSelected);
    oEventHandler[IconLibrarySelectIconViewEvents.GRID_SELECT_ICON_SELECT_BUTTON_CLICKED] = ActionInterceptor('IconLibrary: gridSelectIconElementSelected', handleSelectIconElementSelectedFromGrid);

    oEventHandler[ListNodeViewNewEvents.LIST_NODE_CLICKED] = ActionInterceptor('SettingScreen: Handle List View Node Clicked', handleListViewNodeClicked);
    oEventHandler[ListNodeViewNewEvents.LIST_NODE_VIEW_DELETE_NODE_CLICKED] = ActionInterceptor('Role' +
        ' list node view Delete node icon Clicked', handleListNodeViewNodeDeleteButtonClicked);
    oEventHandler[ListNodeViewNewEvents.LIST_NODE_VIEW_CLONE_NODE_CLICKED] = ActionInterceptor('list node view' +
        ' clone node clicked', handleListNodeViewNodeCloneButtonClicked);

    oEventHandler[ListViewNewEvents.LIST_VIEW_NEW_ACTION_ITEM_CLICKED] = ActionInterceptor('list view new action' +
        ' item clicked', handleListViewNewActionItemClicked);
    oEventHandler[ListViewNewEvents.LIST_VIEW_SEARCH_OR_LOAD_MORE_CLICKED] = ActionInterceptor('SettingScreen: handle List View Search or load more clicked', handleListViewSearchOrLoadMoreClicked);

    // Property collection draggable list view
    oEventHandler[PropertyCollectionDraggableListViewEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_COLUMNS_SHUFFLE] = ActionInterceptor('property collection draggable list Columns Shuffle', handlePropertyCollectionDraggableListColumnsShuffled);
    oEventHandler[PropertyCollectionDraggableListViewEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_TAB_CLICKED] = ActionInterceptor('property collection draggable list tab clicked', handlePropertyCollectionDraggableListTabClicked);
    oEventHandler[PropertyCollectionDraggableListViewEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_VIEW_LOAD_MORE] = ActionInterceptor('property collection draggable list view load more', handleAvailableListViewLoadMoreClicked);
    oEventHandler[PropertyCollectionDraggableListViewEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_SEARCH_TEXT_CHANGED] = ActionInterceptor('property collection draggable list search text changed', handleAvailableListViewSearched);
    oEventHandler[PropertyCollectionDraggableListViewEvents.PROPERTY_COLLECTION_DRAGGABLE_LIST_REMOVE_BUTTON_CLICKED] = ActionInterceptor('property collection draggable list remove button clicked', handlePropertyCollectionDraggableListPropertyRemove);

// InDesign Server configuration Events
    oEventHandler[IndesignServerConfigurationViewEvents.INDESIGN_SERVER_CONFIGURATION_ADD_BUTTON_clicked] = ActionInterceptor('indesign_server_configuration_add_button_clicked', handleIndesignServerConfigurationAddButtonClicked);
    oEventHandler[IndesignServerConfigurationViewEvents.INDESIGN_SERVER_CONFIGURATION_HEADER_SAVE_BUTTON_CLICKED] = ActionInterceptor('indesign_server_configuration_header_save_button_clicked', handleIndesignServerConfigurationHeaderSaveButtonClicked);
    oEventHandler[IndesignServerConfigurationViewEvents.INDESIGN_SERVER_CONFIGURATION_HEADER_CANCEL_BUTTON_CLICKED] = ActionInterceptor('indesign_server_configuration_header_cancel_button_clicked', handleIndesignServerConfigurationHeaderCancelButtonClicked);
    oEventHandler[IndesignServerViewEvents.INDESIGN_SERVER_CONFIGURATION_REMOVE_BUTTON_CLICKED] = ActionInterceptor('indesign_server_configuration_remove_button_clicked', handleIndesignServerConfigurationRemoveButtonClicked);
    oEventHandler[IndesignServerViewEvents.INDESIGN_SERVER_CONFIGURATION_CHECK_STATUS_BUTTON_CLICKED] = ActionInterceptor('indesign_server_configuration_check_status_button_clicked', handleIndesignServerConfigurationCheckStatusButtonClicked);

    //DAM Configuration Events
    oEventHandler[DAMConfigurationViewEvents.DAM_CONFIG_USE_ORIGINAL_FILE_NAME_VALUE_CHANGED] = ActionInterceptor('dam_config_use_original_file_name_value_changed', handleDamConfigUseFileNameValueChanged);
    oEventHandler[DAMConfigurationViewEvents.DAM_CONFIGURATION_SAVE_DISCARD_BUTTON_CLICKED] = ActionInterceptor('dam_configuration_save_discard_button_clicked', handleDamConfigurationSaveDiscardButtonClicked);

    // Process Frequency Summary View Events
    oEventHandler[ProcessFrequencySummaryViewEvents.PROCESS_FREQUENCY_SUMMARY_DATE_BUTTON_CLICKED] = ActionInterceptor('process_frequency_summary_date_button_clicked', handleProcessFrequencySummaryDateButtonClicked);
    oEventHandler[TableCellViewEvents.TABLE_SECTION_EXPAND_COLLAPSED_CLICKED] = ActionInterceptor('SettingScreen:' +
        ' handle Permission Expand Collapsed Clicked', handleTableExpandCollapsedClicked);
    oEventHandler[TableCellViewEvents.TABLE_BUTTON_CLICKED_CLICKED] = ActionInterceptor('Table button clicked',
        handleTableButtonClicked);

    oEventHandler[VariantConfigurationViewEvents.VARIANT_CONFIGURATION_ACTION_BUTTON_CLICKED] = ActionInterceptor('variant' +
        ' action button clicked', handleVariantActionButtonClicked);
    oEventHandler[VariantConfigurationViewEvents.VARIANT_CONFIGURATION_TOGGLE_BUTTON_CLICKED] = ActionInterceptor('Variant' +
        ' Toggle Button Clicked', handleVariantConfigurationToggleButtonClicked);
    oEventHandler[CreateRoleCloneDialogViewEvents.CREATE_ROLE_CLONE_DIALOG_ENTITY_BUTTON_CLICKED] = ActionInterceptor('create' +
        'role clone dialog entity button clicked', handleCreateRoleCloneDialogActionButtonClicked);
    oEventHandler[CreateRoleCloneDialogViewEvents.CREATE_ROLE_CLONE_DIALOG_CHECKBOX_CLICKED] = ActionInterceptor('create' +
        ' role clone dialog checkbox clicked', handleCreateRoleCloneDialogCheckboxButtonClicked);
    oEventHandler[CreateRoleCloneDialogViewEvents.CREATE_ROLE_CLONE_DIALOG_EXACT_CLONE_CHECKBOX_CLICKED] = ActionInterceptor('create ' +
        'role clone dialog exact clone checkbox clicked', handleCreateRoleCloneDialogExactCloneButtonClicked);
    oEventHandler[CreateRoleCloneDialogViewEvents.CREATE_ROLE_CLONE_DIALOG_EDIT_VALUE_CHANGED] = ActionInterceptor('create ' +
        'role clone dialog edit value changed', handleCreateRoleCloneDialogEditValueChanged);

    oEventHandler[AdministrationSummaryViewEvents.EXPAND_ARROW_BUTTON_CLICKED] = ActionInterceptor('expand_arrow_button_clicked', handleAdministrationExpandButtonClicked);
  })();

  return {

    registerEvent: function () {
      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        eventBus.addEventListener(sEventName, oHandler);
      });
    },

    deRegisterEvent: function () {
      CS.forEach(oEventHandler, function (oHandler, sEventName) {
        eventBus.removeEventListener(sEventName, oHandler);
      });
    }
  }
})();

export default SettingScreenAction;
