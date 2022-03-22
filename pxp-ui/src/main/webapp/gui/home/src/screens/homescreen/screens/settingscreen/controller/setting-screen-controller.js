
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as AttributionTaxonomyConfigView } from '../view/attribution-taxonomy-config-view';
import { view as TranslationsConfigView } from '../view/translations-config-view';
import { view as ContextConfigView } from '../view/context-config-view';
import { view as RoleConfigView } from '../view/role-config-view';
import { view as MappingConfigView } from '../view/mapping-config-view';
import { view as RuleConfigView } from '../view/rule-config-view';
import { view as KpiConfigView } from '../view/kpi-config-view';
import { view as TabsConfigView } from '../view/tabs-config-view';
import { view as RuleListConfigView } from '../view/rule-list-config-view';
import { view as PropertyCollectionConfigView } from '../view/property-collection-config-view';
import { view as ClassConfigViewClone } from '../view/class-config-view-clone';
import { view as RelationshipConfigView } from '../view/relationship-config-view';
import { view as ClassConfigDataTransferView } from '../view/class-config-data-transfer-view';
import { view as ProcessConfigView } from '../view/process-config-view';
import { view as ConfigBPMNWrapperView } from '../view/config-bpmn-wrapper-view';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import { view as UserPasswordChangeView } from '../../../../../viewlibraries/userdetailview/user-password-change-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as UserProfileView } from '../../../../../viewlibraries/userprofileview/user-profile-view';
import { view as DataTransferView } from '../view/data-transfer-view';
import { view as ConfigEntityCreationDialogView } from '../view/config-entity-creation-dialog-view';
import { view as TabsSortDialogView } from '../view/tabs-sort-dialog-view';
import { view as OrganisationConfigView } from '../view/organisation-config-view';
import { view as ClassConfigRelationshipView } from '../view/class-config-relationship-view';
import { view as ClassConfigContextView } from '../view/class-config-context-view';
import { view as ClassContextDialogView } from '../view/class-context-dialog-view';
import { view as PdfReactorServerConfigView} from '../view/pdf-reactor-server-config-view';
import { view as DownloadTrackerConfigView} from '../view/download-tracker-config-view';
import { communicator as HomeScreenCommunicator } from "../../../store/home-screen-communicator";
import DraggableListViewModel from '../../../../../viewlibraries/draggablelistview/model/draggable-list-view-model';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import RoleDetailModel from '../view/model/role-detail-view-model';
import ConfigRelationshipViewModel from './../view/model/config-relationship-view-model.js';
import EntitiesDictionary from '../../../../../commonmodule/tack/entities-list';
import MockDataForEntityBaseTypesDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import NatureTypeDictionary from '../../../../../commonmodule/tack/nature-type-dictionary.js';
import RelationshipTypeDictionary from '../../../../../commonmodule/tack/relationship-type-dictionary.js';
import RoleTypeDictionary from '../tack/mock/role-type-dictionary';
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary.js';
import TemplateTabsDictionary from '../../../../../commonmodule/tack/template-tabs-dictionary';
import MockColors from '../tack/mock/mock-data-for-tag-colors';
import SettingUtils from './../store/helper/setting-utils';
import ClassConfigProps from '../store/model/class-config-view-props';
import SystemLevelId from '../../../../../commonmodule/tack/system-level-id-dictionary';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import MockDataForTaskTypes from '../tack/mock/mock-data-for-task-types';
import RelationshipConstants from '../../../../../commonmodule/tack/relationship-constants';
import GridViewContexts from '../../../../../commonmodule/tack/grid-view-contexts';
import TabHeaderData from '../tack/mock/mock-data-for-map-summery-header';
import EndPointTypeDictionary from '../../../../../commonmodule/tack/endpoint-type-dictionary';
import ViewContextConstants from '../../../../../commonmodule/tack/view-context-constants';
import MockDataForPhysicalCatalogAndPortal from '../../../../../commonmodule/tack/mock-data-physical-catalog-and-portal-types';
import PortalTypeDictionary from '../../../../../commonmodule/tack/portal-type-dictionary';
import MockDataForScreens from '../../../../../commonmodule/tack/mock-data-for-screens';
import MockDataForUserRoleTypes from '../tack/mock/mock-data-for-user-types';
import SettingScreenModuleDictionary from '../../../../../commonmodule/tack/setting-screen-module-dictionary';
import EntityBaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary.js';
import MockDataForKlassTypes from '../../../../../commonmodule/tack/mock-data-for-class-types';
import { view as SettingScreenView } from '../view/setting-screen-view';
import ConfigModulesList from '../tack/settinglayouttack/config-modules-list';
import ConfigModulesDictionary from './../tack/settinglayouttack/config-modules-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';
import oClassCategoryConstants from './../tack/class-category-constants-dictionary';
import oConfigEntityTypeDictionary from '../../../../../commonmodule/tack/config-entity-type-dictionary';
import oConfigModulePropertyGroupTypeDictionary from '../tack/settinglayouttack/config-module-data-model-property-group-type-dictionary';
import { ClassRequestMapping as oClassRequestMapping, RoleRequestMapping as oRoleRequestMapping,
  GridEditMapping as oGridEditMapping} from '../tack/setting-screen-request-mapping';
import RequestMapping from '../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import MockDataForProcessEventTypes from '../../../../../commonmodule/tack/mock-data-for-mdm-process-event-types';
import Constants from '../../../../../commonmodule/tack/constants';
import PhysicalCatalogDictionary from '../../../../../commonmodule/tack/physical-catalog-dictionary';
import MockDataForOrganisationTypes from '../tack/mock/mock-data-for-organisation-config-types';
import {getLanguageInfo, getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager.js';
import MockDataForProcessTriggeringEvent from '../tack/mock/mock-data-for-process-triggering-event';
import { view as SmallTaxonomyView } from './../../../../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import SmallTaxonomyViewModel from './../../../../../viewlibraries/small-taxonomy-view/model/small-taxonomy-view-model';
import ViewUtils from '../view/utils/view-utils';
import { view as BreadcrumbView } from '../../../../../viewlibraries/breadcrumbview/breadcrumb-wrapper-view';
import pdfColorSpaceData from '../tack/mock/mock-data-for-smart-document-preset-color-space';
import pdfMarksBleedsData from '../tack/mock/mock-data-for-smart-document-preset-marks-bleeds';
import {view as ManageEntityDialogView} from '../view/entity-usage-dialog-view';
import ColumnOrganizerProps from '../../../../../viewlibraries/columnorganizerview/column-organizer-props';
import { view as ProcessConfigFullScreenWrapperView } from '../view/process-config-full-screen-wrapper-view';
import { view as SSOSettingView } from '../view/sso-config-screen-view';
import { view as ThemeConfigurationView } from '../view/theme-configuration-config-view';
import { view as ViewConfigurationView } from '../view/view-configuration-config-view';
import { view as IconLibraryView } from '../view/icon-library-view';
import { view as IconLibrarySelectIconView } from '../view/icon-library-select-icon-view';
import ThemeConfigurationViewModel from "../tack/mock/theme-configuration-view-model";
import ViewConfigurationViewModel from "../tack/mock/view-configuration-view-model";
import { view as LanguageTreeConfigView } from '../view/language-tree-config-view';
import { view as SmartDocumentConfigView } from '../view/smart-document-config-view';
import SmartDocumentPresetViewModel from '../tack/mock/smart-document-preset-view-model';
import LocaleData from '../../../../../commonmodule/tack/mock-data-for-locale-language';
import NumberFormatData from '../../../../../commonmodule/tack/number-format-dictionary';
import DateFormatData from '../../../../../commonmodule/tack/date-format-for-data-language-dictionary';
import AttributeTypeDictionary from '../../../../../commonmodule/tack/attribute-type-dictionary-new';
import { view as GridYesNoPropertyView } from './../../../../../viewlibraries/gridview/grid-yes-no-property-view';
import MockDataForOrganisationConfigPhysicalCatalogAndPortal from "../../../../../commonmodule/tack/mock-data-physical-catalog-and-portal-types";
import FilterPropTypeConstants from '../../contentscreen/tack/filter-prop-type-constants';
import { view as RelationshipTransferView } from '../view/relationship-inheritance-view';
import MockDataForTaxonomyInheritanceList from "../../../../../commonmodule/tack/mock-data-for-taxonomy-inheritance";

import MomentUtils from "../../../../../commonmodule/util/moment-utils";
import { Logger } from "../../../../../libraries/logger";
import { view as AuthorizationConfigView } from '../view/authorization-config-view';
import { SettingsRequestMapping as oSettingsRequestMapping } from '../tack/setting-screen-request-mapping';
import CreateDialogLayoutData from '../tack/mock/create-dialog-layout-data';
import ManageEntityConfigProps from "../store/model/manage-entity-config-props";
import { view as EndpointConfigView } from '../view/endpoint-config-view';
import MockDataForEndPointTypes from '../../../../../commonmodule/tack/mock-data-for-mdm-endpoint-types';
import oProcessTypeDictionary from '../../../../../commonmodule/tack/process-type-dictionary';
import oPhysicalCatalogDictionary from '../../../../../commonmodule/tack/physical-catalog-dictionary';
import EntityProps from '../../../../../commonmodule/props/entity-props';
import { ContextList } from '../../../../../commonmodule/contexts/ajax-method-context-creator';
import MockDataForEntities from '../../../../../commonmodule/tack/mock-data-for-entities';
import MockDataForElements from '../../../../../commonmodule/tack/mock-data-for-elements';
import MockDataForElementTypes from '../../../../../commonmodule/tack/mock-data-for-element-types';
import MockDataForDateFilter from '../../../../../commonmodule/tack/mock-data-for-audit-log-date-filter';
import MockDataForAuditLogActivities from '../../../../../commonmodule/tack/mock-data-for-audit-log-activities';
import ProcessConfigViewProps from "../store/model/process-config-view-props";
import MockDataForProcessEntityTypes from "../tack/mock/mock-data-for-process-entity-types";
import { view as ClassConfigTagTransferView } from '../view/class-config-tag-transfer-view';
import MockDataForMapSummaryHeader from "../tack/mock/mock-data-for-map-summery-header";
import ExportSide2RelationshipDictionary from '../../../../../commonmodule/tack/export-side2-relationship-dictionary'
import MockDataForHeaderExportSide2Relationship from '../tack/mock/mock-data-for-header-export-side2-relationships';
import MockDataForMappingTypes from '../../../../../commonmodule/tack/mock-data-for-mapping-types';
import MappingTypeDictionary from '../../../../../commonmodule/tack/mapping-type-dictionary';
import {gridViewPropertyTypes as oGridViewPropertyTypes} from "../../../../../viewlibraries/tack/view-library-constants";
import {view as ShowExportStatusDialogView} from '../view/audit-log-export-status-view';
import ProcessEventTypeDictionary from "../../../../../commonmodule/tack/process-event-type-dictionary";
import MockDataForWorkflowTypesDictionary from "../tack/mock/mock-data-for-workflow-types-dictionary";
import {view as ColumnOrganizerView} from "../../../../../viewlibraries/columnorganizerview/column-organizer-view";
import ContextualGridViewProps
  from "../../../../../viewlibraries/contextualgridview/store/model/contextual-grid-view-props";
import PermissionEntityTypeDictionary from './../../../../../commonmodule/tack/permission-entity-type-dictionary';
import GridViewStore from "../../../../../viewlibraries/contextualgridview/store/grid-view-store";
import ProcessFiltersValues from "../../../../../commonmodule/tack/mock-data-for-process-filter-values";
import IconLibraryProps from "../store/model/icon-library-props";
import MockDataForProcessWorkflowTypes from '../tack/mock/mock-data-for-process-workflow-types';
import MockDataForProcessActionSubTypes from '../tack/mock/mock-data-for-process-action-subtypes';
import MockDataForProcessActionTypesDictionary from "../tack/mock/mock-data-for-action-subtypes-dictionary";
import IconLibrarySelectIconDialogProps from "../store/model/icon-library-select-icon-dialog-props";
import PropertyCollectionTabLayoutData from "../tack/property-collection-tab-layout-data";
import AttributionTaxonomiesTabLayoutData from "../tack/attribution-taxonomy-tab-layout-data";
import PropertyCollectionDraggableListTabsLayoutData from "../tack/property-collection-draggable-list-tab-layout-data"
import IndesignServerConfigurationViewProps from "../store/model/indesign-server-configuration-view-props";
import {view as IndesignServerConfigurationView} from "../view/indesign-server-configuration-view";
import {view as DAMConfigurationView} from "../view/dam-configuration-view";
import DAMConfigurationViewProps from "../store/model/dam-configuration-view-props";
import TabHeaderDataForProcess from '../tack/mock/mock-data-for-frequency-dictionary';
import MockDataForProcessFrequencyDayList from '../tack/mock/mock-data-for-process-frequency-day-list';
import MockDataForfrequencyMonthListDictionary from "../tack/mock/mock-data-for-process-frequency-month-list";
import MockDataForFrequencyTypesDictionary from '../tack/mock/mock-data-for-frequency-dictionary';
import RoleConfigViewProps from "../store/model/role-config-view-props";
import MockDataForListViewActionItemButton from '../tack/list-view-action-item-button-list';
import ContextualTableViewProps from "../store/model/contextual-table-view-props";
import MockDataForRelationshipTypeList from "../tack/mock/mock-data-for-relationship-type"
import {view as VariantConfigurationView} from "../view/variant-configuration-view";
import SettingScreenStore from '../store/setting-screen-store';
import SettingScreenAction from '../action/setting-screen-action';
import {view as CreateRoleCloneDialogView} from "../view/utils/create-role-clone-dialog-view";
import TaxonomyPermissionTabLayoutData from "../tack/taxonomy-permission-tab-layout-data";
import '../../../../../themes/basic/homescreen/importer-settings.scss';
import oDownloadTrackerDictionary from "../tack/download-tracker-dictionary";
import MockDataForProcessUsecases from '../tack/mock/mock-data-for-process-usecases';
import RolePermissionFunctionBulkEditLayoutData from "../tack/role-permission-function-bulk-edit-data";
import {view as AdministrationSummaryView} from "../view/administration-summary-view";

const oPropTypes = {
  isNewScreen: ReactPropTypes.bool
};

// @CS.SafeComponent
class SettingScreenController extends React.Component {

 static childContextTypes = {
    masterTagList: ReactPropTypes.array,
    masterAttributeList: ReactPropTypes.object,
    calculatedAttributeMapping: ReactPropTypes.object
  };
  //@required: Props
  static propTypes = oPropTypes;

  constructor(props, context) {
    super(props, context);

    const oStore = this.getStore();

    this.state = {
      appData: oStore.getData().appData,
      componentProps: oStore.getData().componentProps
    };

    oStore.loadDataOnScreenLoad();
    SettingScreenAction.registerEvent();
  }

  //@Bind: Store with state
  componentDidMount() {
    HomeScreenCommunicator.setSettingScreenLoaded(true);
    this.debounceSettingStateChanged = this.settingStateChanged;
    this.getStore().bind('change', this.debounceSettingStateChanged);
  }

  componentDidUpdate () {
    if (global.startTime) {
      let sEndTime = new Date().toISOString();
      // console.log("componentUpdate", sEndTime);
      Logger.log(`${MomentUtils.getTimeDifference(global.startTime, sEndTime)}s was taken for the render all the affected components.`);
      Logger.log("Metadata", {
        startTime: global.startTime,
        endTime: sEndTime,
        component: "SettingScreenController",
        timeTaken: `${MomentUtils.getTimeDifference(global.startTime, sEndTime)}s`
      });
      global.startTime = "";
    }
  }

  //@UnBind: store with state
  componentWillUnmount() {
    this.getStore().unbind('change', this.debounceSettingStateChanged);
    SettingScreenAction.deRegisterEvent();
    this.getStore().toggleLinkScrollActive();
  }

  //@set: state
  settingStateChanged = () => {

    var changedState = {
      appData: this.getStore().getData().appData,
      componentProps: this.getStore().getData().componentProps
    };

    this.setState(changedState);
  };

  getStore = () => {
    return SettingScreenStore;
  };

  getAppData = () => {
    return this.state.appData;
  };

  getComponentProps = () => {
    return this.state.componentProps;
  };

  getChildContext() {
    var oAppData = this.getAppData();
    return {
      masterTagList: oAppData.getTagList(),
      masterAttributeList: oAppData.getAttributeList(),
      calculatedAttributeMapping: oAppData.getCalculatedAttributeMapping()
    };
  }

  //****************************************** Controller API's **************************************************
  isActiveMenu = (oItem) => {
    var sSplitter = "_";
    var sItemId = oItem.id;
    var sActiveConfigViewName = this.getComponentProps().screen.getSelectedLeftNavigationTreeItem();

    //Get Action item id excluding text after _
    var sItemName = sItemId.split(sSplitter)[0];
    return sItemName == sActiveConfigViewName;
  };

  getItemList = (aItemList) => {
    var that = this;

    return CS.map(aItemList, function (oItem) {

      var oProperties = {};
      var sItemTitle = getTranslation()[oItem.title];
      oProperties.className = oItem.className;
      oProperties.isActive = that.isActiveMenu(oItem);

      return ({id: oItem.id, label: sItemTitle, properties: oProperties});
    });
  };

  getCreateEntityDialog = (oActiveEntity, sEntityType, oModel) => {
    return (
        <ConfigEntityCreationDialogView
            activeEntity={oActiveEntity}
            entityType={sEntityType}
            extraFieldsToShow={oModel}
        />
    )
  };

  getSortTabsDialog = (aTabsList) => {
    let aSortedTabsList = CS.sortBy(aTabsList, "sequence");

    return (
        <TabsSortDialogView sortListItems={aSortedTabsList}/>
    )
  };

 getCreateDialogModelForTags = (oActiveTag) => {
    var sTagParentId = oActiveTag.parentId;
    switch (sTagParentId) {
      case "resolutiontag":
        return {
          imageResolution: oActiveTag.imageResolution || ""
        };
      case "imageextensiontag":
        return {
          imageExtension: oActiveTag.imageExtension || "",
        };
      default:
        return {};
    }
  };

  getColumnOrganizerData = () => {
    return {
      disableSearch: false
    };
  };

  getTagConfigView = () => {
    var oTagConfigView = this.getComponentProps().tagConfigView;
    var oActiveTag = oTagConfigView.getActiveTag();
    oActiveTag = oActiveTag.clonedObject ? oActiveTag.clonedObject : oActiveTag;
    var oCreateDialogTagsModel = this.getCreateDialogModelForTags(oActiveTag);
    var oCreateTagDialog = !CS.isEmpty(oActiveTag) ? this.getCreateEntityDialog(oActiveTag, "tag", oCreateDialogTagsModel) : null;

    if (oTagConfigView.getIsGridView()) {
      let bShowCheckboxColumn = true;
      let aSelectedColumns = ColumnOrganizerProps.getSelectedOrganizedColumns();
      let bIsColumnOrganizerDialogOpen = ColumnOrganizerProps.getIsDialogOpen();
      let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
      let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
      let oSelectIconDialog = this.getSelectIconDialog();

      return (
          <div className="configGridViewContainer" key="tagConfigGridViewContainer">
            <ContextualGridView context={GridViewContexts.TAG}
                                tableContextId={GridViewContexts.TAG}
                                hierarchical={true}
                                showCheckboxColumn={bShowCheckboxColumn}
                                disableDeleteButton={false}
                                enableImportExportButton={true}
                                activeEntity={oActiveTag}
                                enableManageEntityButton={true}
                                showGridColumnOrganiserButton={true}
                                columnOrganizerData={this.getColumnOrganizerData()}
                                selectedColumns={aSelectedColumns}
                                isColumnOrganizerDialogOpen={bIsColumnOrganizerDialogOpen}
            />
            {oCreateTagDialog}
            {oManageEntityDialog}
            {oSelectIconDialog}
          </div>
      );
    }

    return null;
  };

  getMSSModel = (
    aSelectedItems,
    aItems,
    sContext,
    sInnerContext,
    bIsMultiSelect,
    bCannotRemove,
    bIsDisabled,
    fOnPopOverOpen,
    fOnApply,
    sSearchText,
    bIsLoadMoreEnabled,
    aSelectedObjects,
  ) => {
    let sSplitter = SettingUtils.getSplitter();
    sInnerContext = sInnerContext || "";
    let sMSSContext = `${sContext}${sSplitter}${sInnerContext}`;
    return {
      items: aItems,
      selectedItems: aSelectedItems,
      context: sMSSContext,
      isMultiSelect: bIsMultiSelect || false,
      cannotRemove: bCannotRemove || false,
      onPopOverOpen: fOnPopOverOpen,
      onApply: fOnApply,
      disabled: bIsDisabled,
      searchText: sSearchText,
      isLoadMoreEnabled: bIsLoadMoreEnabled,
      selectedObjects: aSelectedObjects
    }
  };

  getSelectedRoleDetailedViewModel = () => {
    let oActiveOrganization = this.getComponentProps().organisationConfigView.getActiveOrganisation();
    let aOrganisationPhysicalCatalogs = oActiveOrganization.physicalCatalogs;
    let aOrganisationPortals = oActiveOrganization.portals;
    var oAppData = SettingUtils.getAppData();
    let oRoleProps =  this.getComponentProps().roleConfigView;
    var oSelectedRole = this.getComponentProps().roleConfigView.getSelectedRole(); //from value list
    var oProperty = {};

    if (CS.isEmpty(oSelectedRole)) {
      oProperty.isEmpty = true;
      return new RoleDetailModel('', '', '', '', '', '', true, {}, false, [], [], [], false, [], oProperty);
    }
    let oCurrentRole = oSelectedRole.clonedObject || oSelectedRole;

    var oRoleMaster = this.getStore().getUnSavedRoleFromMasterById(oSelectedRole.id);

    var aArticleClass = oAppData.getAllClassesFlatList();
    var aCollectionClass = oAppData.getAllCollectionClassesFlatList();
    var aSetClass = oAppData.getAllSetClassesFlatList();
    var aAssetClass = oAppData.getAssetLazyList();
    var aAssetCollectionClass = oAppData.getAllAssetCollectionClassList();
    var aTaskClass = oAppData.getAllTaskClassesFlatList();
    var aMarketClass = oAppData.getAllMarketTargetClassesFlatList();
    var aTargetCollectionClass = oAppData.getAllTargetCollectionClassesFlatList();
    let sRoleType = oCurrentRole.roleType;
    let aSelectedType = CS.isEmpty(sRoleType) ? [] : [sRoleType];
    let aSelectedPhysicalCatalogItems = CS.isEmpty(oCurrentRole.physicalCatalogs) ? [] : CS.cloneDeep(oCurrentRole.physicalCatalogs);
    let aSelectedPortalItems = CS.isEmpty(oCurrentRole.portals) ? [] : oCurrentRole.portals;
    let aSelectedScreen = [oCurrentRole.landingScreen];
    let aAvailableScreens = MockDataForScreens();
    let aPhysicalCatalogItems = [];
    let aPortalItems = [];
    if (CS.isEmpty(aOrganisationPhysicalCatalogs)) {
      aPhysicalCatalogItems = MockDataForPhysicalCatalogAndPortal.physicalCatalogs();
    } else {
      CS.forEach(MockDataForPhysicalCatalogAndPortal.physicalCatalogs(), function (oItem) {
        if (CS.includes(aOrganisationPhysicalCatalogs, oItem.id)) {
          aPhysicalCatalogItems.push(oItem);
        }
        });
      }
    if (CS.isEmpty(aOrganisationPortals)) {
      aPortalItems = MockDataForPhysicalCatalogAndPortal.portals();
    } else {
      CS.forEach(MockDataForPhysicalCatalogAndPortal.portals(), function (oItem) {
        if (CS.includes(aOrganisationPortals, oItem.id)) {
          aPortalItems.push(oItem);
        }
        });
      }

    let aReferencedClassList = oRoleProps.getReferencedKlasses();
    let oTargetKlassItems = {};
    CS.forEach(aReferencedClassList,function (oKlass) {
      oTargetKlassItems[oKlass.id] = oKlass;
    });

    let bIsRoleTypeDisabled = true; // Always disabled since roleType will be decided on creation
    let aMockDataForUserRoleTypes = MockDataForUserRoleTypes();
    if (oActiveOrganization.id != SettingUtils.getTreeRootId()) {
      CS.remove(aMockDataForUserRoleTypes, {id: "admin"})
    }
    let oRoleTypeData = this.getMSSModel(aSelectedType, aMockDataForUserRoleTypes, "role", "roleType", false, true, bIsRoleTypeDisabled);

    let aDisabledItems = [];
    if (oCurrentRole.isReadOnly) {
      CS.remove(aSelectedPhysicalCatalogItems, function (sId) {
        return sId === PhysicalCatalogDictionary.DATAINTEGRATION;
      });
      aDisabledItems.push(PhysicalCatalogDictionary.DATAINTEGRATION);
    }
    let oPhysicalCatalogData = this.getSelectionToggleModel(aPhysicalCatalogItems, aSelectedPhysicalCatalogItems, "role", false, aDisabledItems);
    let oPortalData = this.getSelectionToggleModel(aPortalItems, aSelectedPortalItems, "role", false);
    let oScreenData = this.getMSSModel(aSelectedScreen, aAvailableScreens, "role", 'screen', false, true);
    let aSelectedTaxonomyIds = CS.isEmpty(oCurrentRole.targetTaxonomies) ? [] : oCurrentRole.targetTaxonomies;
    let aSelectedKlassIds = CS.isEmpty(oCurrentRole.targetKlasses) ? [] : oCurrentRole.targetKlasses;
    let aAllowedTaxonomies = oRoleProps.getAllowedEntitiesById("taxonomies");
    let oAllowedTaxonomyHierarchyList = oRoleProps.getAllowedTaxonomyHierarchyList();
    let oReferencedTaxonomies = oRoleProps.getReferencedTaxonomies();
    let oAvailabilityData = SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomies, aAllowedTaxonomies, oAllowedTaxonomyHierarchyList);
    oAvailabilityData.paginationData = oRoleProps.getRolePaginationDataById("taxonomies");
    var oClassMap = {};

    oClassMap["Article"] = aArticleClass;
    oClassMap["Collection"] = aCollectionClass;
    oClassMap["Set"] = aSetClass;
    oClassMap["Asset"] = aAssetClass;
    oClassMap["Asset Collection"] = aAssetCollectionClass;
    oClassMap["Task"] = aTaskClass;
    oClassMap["Market"] = aMarketClass;
    oClassMap["Target Collection"] = aTargetCollectionClass;
    let sUserSearchText = oRoleProps.getRolePaginationDataById("users").searchText;
    var __ret = this.getAvailableAndAddedUsersFromRole(oRoleMaster);
    let aSelectedUserIds = __ret.selectedItems;
    let aUserItems = __ret.items;
    let sURLToGetRoleUsers = RequestMapping.getRequestUrl(oRoleRequestMapping.GetAllowedRoleEntities);
    let oTargetKlassData = {
      selectedItems: aSelectedKlassIds,
      items: oTargetKlassItems,
      url: sURLToGetRoleUsers
    };
    let oSelectedUserObjects = __ret.selectedObjects;
    oProperty['selectedUserObjects'] = oSelectedUserObjects;
    var aModuleRightScreen = oRoleMaster.entities || [];
    var oProfileData = this.getAvailableAndAddedProfileFromRole(oRoleMaster);
    oProperty['roleTypeData'] = oRoleTypeData;
    oProperty['physicalCatalogData'] = oPhysicalCatalogData;
    oProperty['portalData'] = oPortalData;
    oProperty['availabilityData'] = oAvailabilityData;
    oProperty['profileData'] = oProfileData;
    oProperty['targetKlassData'] = oTargetKlassData;
    oProperty['userSearchText'] = sUserSearchText;
    oProperty['isUserDisabled'] = bIsRoleTypeDisabled;
    oProperty['code'] =  oRoleMaster.code;
    oProperty['isCreated'] = oRoleMaster.isCreated;
    oProperty['usersURL'] = RequestMapping.getRequestUrl(oRoleRequestMapping.GetAllowedUsers);
    oProperty['activeOrganizationId'] = oActiveOrganization.id;
    oProperty['roleType'] = sRoleType;
    oProperty['screenData'] = oScreenData;
    oProperty['isDashboardEnable'] = oCurrentRole.isDashboardEnable;
    oProperty['isReadOnly'] = oCurrentRole.isReadOnly;

    let aSystemList = oAppData.getSystemsForRoles();
    let oSystemMap = oAppData.getSystemsMapForRoles();
    let bIsDisabled = sRoleType === RoleTypeDictionary.SYSTEM_ADMIN;
    let oSelectedSystemsViewModel = this.getModelForSystemsSelectionView(oRoleMaster.systems, oRoleMaster.endpoints, "role", bIsDisabled, aSystemList, oSystemMap);
    oProperty['selectedSystemsViewModel'] = oSelectedSystemsViewModel;

    oProperty['kpiData'] = {
      selectedKPIs: oRoleMaster.kpis,
      referencedKPIs: oRoleProps.getReferencedKPIs()
    };

    return new RoleDetailModel(
        oSelectedRole.id,
        oRoleMaster.label,
        oRoleMaster.description,
        oRoleMaster.defaultValue,
        oRoleMaster.tooltip,
        oRoleMaster.placeholder,
        oRoleMaster.isMultiselect,
        oRoleMaster.globalPermission,
        oRoleMaster.isSettingAllowed,
        oClassMap,
        aUserItems,
        aSelectedUserIds,
        oRoleMaster.isStandard,
        aModuleRightScreen,
        oProperty
    );
  };

  getAttributeCreationDialogModel = (oActiveAttribute) => {
    let oModel = {
      label: oActiveAttribute.label || "",
      code: oActiveAttribute.code || "",
    };

    let sAttributeType = oActiveAttribute.type;

    if(SettingUtils.isAttributeTypeHtml(sAttributeType) ||
        SettingUtils.isAttributeTypeText(sAttributeType)) {
      oModel.isTranslatable = {
        isSelected: oActiveAttribute.isTranslatable,
        context: "attribute",
      }
    }

    return oModel;
  };

  getAttributeView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let oAttributeConfigView =  this.getComponentProps().attributeConfigView;
    let oActiveAttribute = oAttributeConfigView.getSelectedAttribute();
    oActiveAttribute = oActiveAttribute.clonedObject ? oActiveAttribute.clonedObject : oActiveAttribute;
    let oModel = this.getAttributeCreationDialogModel(oActiveAttribute);
    let oCreateAttributeDialog = !CS.isEmpty(oActiveAttribute) ? this.getCreateEntityDialog(oActiveAttribute,"attribute", oModel) : null;
    let sSelectedTreeNode = oScreenProps.getSelectedLeftNavigationTreeItem();
    let bIsStandardAttributeScreen = (sSelectedTreeNode === oConfigModulePropertyGroupTypeDictionary.STANDARD);
    let bShowCheckboxColumn = !bIsStandardAttributeScreen;
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let aSelectedColumns = ColumnOrganizerProps.getSelectedOrganizedColumns();
    let bIsColumnOrganizerDialogOpen = ColumnOrganizerProps.getIsDialogOpen();
    let oSelectIconDialog = this.getSelectIconDialog();

    return (
        <div className="configGridViewContainer" key="attributeConfigGridViewContainer">
          <ContextualGridView context={GridViewContexts.ATTRIBUTE}
                              tableContextId={GridViewContexts.ATTRIBUTE}
                              showCheckboxColumn={bShowCheckboxColumn}
                              disableDeleteButton={bIsStandardAttributeScreen}
                              enableImportExportButton={true}
                              disableCreate={bIsStandardAttributeScreen}
                              enableManageEntityButton={true}
                              showGridColumnOrganiserButton={true}
                              columnOrganizerData={this.getColumnOrganizerData()}
                              selectedColumns={aSelectedColumns}
                              isColumnOrganizerDialogOpen={bIsColumnOrganizerDialogOpen}
          />
          {oCreateAttributeDialog}
          {oManageEntityDialog}
          {oSelectIconDialog}
        </div>
    );
  };

  getTimeEnabledModel = (oSelectedContext) => {
    return {
      isSelected: oSelectedContext.isTimeEnabled,
      context: "context"
    }
  };

  getTagTypesToFetchAccordingToContextType = ()=> {
    let aTagTypesToFetch = [TagTypeConstants.TAG_TYPE_BOOLEAN, TagTypeConstants.RANGE_TAG_TYPE, TagTypeConstants.RULER_TAG_TYPE, TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE, TagTypeConstants.YES_NEUTRAL_TAG_TYPE];
    return aTagTypesToFetch;
  };

  getContextView = () => {
    let oComponentProps = this.getComponentProps();
    let bShowCheckboxColumn = true;
    let bIsStandardAttributeScreen = false;

    let oAppData = SettingUtils.getAppData();
    let oContextConfigProps = oComponentProps.contextConfigView;
    let oContextMasterList = oAppData.getContextList();
    let sActiveContext = oContextConfigProps.getActiveContext().id;
    let oActiveContext = oContextMasterList[sActiveContext] || {};
    let aSubContexts = oActiveContext.subContexts;
    let bIsContextDirty = false;
    let bIsDialogOpen = oContextConfigProps.getIsContextDialogActive();
    let aEntitiesList = EntitiesDictionary();
    let aSelectedEntities = oActiveContext.entities;
    let aCustomTabList = oAppData.getCustomTabList();
    let aTagTypesToFetch = CS.isEmpty(oActiveContext) ? [] : this.getTagTypesToFetchAccordingToContextType(oActiveContext.type)
    let oReqResObj = SettingUtils.getConfigDataLazyRequestResponseObjectByEntityName("tags", aTagTypesToFetch);

    if (oActiveContext.clonedObject) {
      oActiveContext = oActiveContext.clonedObject;
      bIsContextDirty = true;
    }

    let oTimeEnabledModel = this.getTimeEnabledModel(oActiveContext);

    let aContextTagMap = oAppData.getTagList();
    if (!CS.isEmpty(aContextTagMap)) {
      aContextTagMap = aContextTagMap[0].children;
    }
    var sActiveTagUniqueSelectionId = oContextConfigProps.getActiveTagUniqueSelectionId();
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };
    let oIconLibraryData = SettingUtils.getIconLibraryData();
    let oSelectIconDialog = this.getSelectIconDialog();

    return (
        <ContextConfigView
            context={GridViewContexts.CONTEXT_VARIANT}
            showCheckboxColumn={bShowCheckboxColumn}
            disableDeleteButton={bIsStandardAttributeScreen}
            enableImportExportButton={true}
            disableCreate={bIsStandardAttributeScreen}
            activeContext={oActiveContext}
            isContextDirty={bIsContextDirty}
            isDialogOpen={bIsDialogOpen}
            contextTagMap={aContextTagMap}
            masterContextList={oContextMasterList}
            subContexts={aSubContexts}
            masterEntitiesList={aEntitiesList}
            entities={aSelectedEntities}
            timeEnabledModel={oTimeEnabledModel}
            customTabList={aCustomTabList}
            requestResponseInfo={oReqResObj}
            activeTagUniqueSelectionId={sActiveTagUniqueSelectionId}
            oManageEntityDialog={oManageEntityDialog}
            enableManageEntityButton={true}
            columnOrganizerData={oColumnOrganizerData}
            iconLibraryData={oIconLibraryData}
            selectIconDialog={oSelectIconDialog}
         />
    );

  };


  getTasksView =(sConfigScreenViewName) => {
    let oComponentProps = this.getComponentProps();
    var bShowCheckboxColumn = true;

    let sGridContext = "";
    var oDesiredConfigViewProps = {};
    var oActiveEntity = {};
    if (sConfigScreenViewName === oConfigEntityTypeDictionary.ENTITY_TYPE_TASK) {
      // because the about constant has "task" in it and all the cases in
      // setting-screen-store are handled bu "tasks"
      sConfigScreenViewName = "tasks";
      sGridContext = GridViewContexts.TASK;
      oDesiredConfigViewProps = oComponentProps.tasksConfigView;
      oActiveEntity = oDesiredConfigViewProps.getActiveTask();
    } else {
      sGridContext = GridViewContexts.DATA_GOVERNANCE_TASK;
      oDesiredConfigViewProps = oComponentProps.dataGovernanceTasksView;
      oActiveEntity = oDesiredConfigViewProps.getActiveDataGovernanceTask();
    }

    oActiveEntity = oActiveEntity.clonedObject ? oActiveEntity.clonedObject : oActiveEntity;
    let oModelForTasksExtraFieldsToShow = this.getCreateDialogModelForEventsOrTasks(oActiveEntity,  oConfigEntityTypeDictionary.ENTITY_TYPE_TASK);
    var oCreateAttributeDialog = !CS.isEmpty(oActiveEntity) ? this.getCreateEntityDialog(oActiveEntity, sConfigScreenViewName,oModelForTasksExtraFieldsToShow) : null;
    let aSelectedColumns = ColumnOrganizerProps.getSelectedOrganizedColumns();
    let bIsColumnOrganizerDialogOpen = ColumnOrganizerProps.getIsDialogOpen();
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let oSelectIconDialog = this.getSelectIconDialog();

    return (
        <div className="configGridViewContainer" key="taskConfigGridViewContainer">
          <ContextualGridView context={sGridContext}
                    tableContextId={sGridContext}
                    showCheckboxColumn={bShowCheckboxColumn}
                    disableDeleteButton={false}
                    enableImportExportButton={true}
                    showGridColumnOrganiserButton={true}
                    enableManageEntityButton={true}
                    columnOrganizerData={this.getColumnOrganizerData()}
                    selectedColumns={aSelectedColumns}
                    isColumnOrganizerDialogOpen={bIsColumnOrganizerDialogOpen}
          />
          {oCreateAttributeDialog}
          {oManageEntityDialog}
          {oSelectIconDialog}
        </div>
    );
  }

  getPermissionViewLeftSectionData = () => {
    let oComponentProps = this.getComponentProps();
    let PermissionProps = oComponentProps.permissionConfigView;
    return {
      linkItemsData: PermissionProps.getEntityNavigationTreeData(),
      selectedItemId: PermissionProps.getSelectedTreeItemId(),
      isNested: true,
      leftNavigationTreeValuesMap: PermissionProps.getEntityNavigationTreeVisualData()
    };
  };

  /**
   * This function is used to decide whether to hide no properties section.
   * @returns {boolean}
   */
  shouldHideNoPropertiesSectionView = () => {
    var oComponentProps = this.getComponentProps();
    var PermissionProps = oComponentProps.permissionConfigView;
    let sId = PermissionProps.getActiveTabId();
    let oVisualData = PermissionProps.getHierarchyTreeVisualData();
    let bIsHideNoPropertiesSection = false;
    let oFoundVisualData = oVisualData[sId];

    switch (sId) {
      case "canBulkEdit":
        if (CS.isNotEmpty(oFoundVisualData) && !oFoundVisualData.isCollapsed) {
          let oFunctionalPermissionMap = PermissionProps.getModifiedFunctionalPermissionMap();
          let oBulkEditPermissionMap = oFunctionalPermissionMap[sId];
          let sValue = oBulkEditPermissionMap[sId];
          let aKeys = Object.keys(new RolePermissionFunctionBulkEditLayoutData());
          CS.forEach(aKeys, function (sKey) {
            let oKeyVisualData = oVisualData[sKey];
            oKeyVisualData.isDisabled = !sValue;
          });
          bIsHideNoPropertiesSection = true;
        }
        break;
    }

    return bIsHideNoPropertiesSection;
  };

  getRoleView = () => {
    var oActionBarList = this.getAppData().getActionBarList();
    var oActionItemModel = {};
    var oRoleConfigView = oActionBarList.RoleConfigView;
    oActionItemModel.RoleListView = [this.getItemList(oRoleConfigView.RoleConfigListView)];
    oActionItemModel.RoleDetailedView = [];
    var oOrganisationConfigViewProps = this.getComponentProps().organisationConfigView;

    var aRoleListModel = this.getRoleListViewModels("roles");
    var oSelectedRoleDetailedModel = this.getSelectedRoleDetailedViewModel();

    let oRoleConfigViewProps = this.getComponentProps().roleConfigView;
    let oSelectedRole = oRoleConfigViewProps.getSelectedRole();
    let oCurrentRole = oSelectedRole.clonedObject || oSelectedRole;

    let aEntitiesList = [];
    let aALowedPortalIds = MockDataForPhysicalCatalogAndPortal.portals();
    CS.forEach(aALowedPortalIds, (oPortal)=> {
      let sLabel = '';
      let aEntities = [];
      if(oPortal.id === PortalTypeDictionary.PIM) {
        sLabel = getTranslation().PIM_ENTITIES;
        aEntities = EntitiesDictionary();
      }
      aEntitiesList.push({portalId: oPortal.id, label: sLabel, entities: aEntities});
    });

    var oComponentProps = this.getComponentProps();
    var bListItemCreated = oComponentProps.screen.getListItemCreationStatus();

    let bIsPermissionVisible = oOrganisationConfigViewProps.getIsPermissionVisible();
    let oPermissionDetailData = this.getPermissionData();
    let oPermissionViewLeftSectionData = this.getPermissionViewLeftSectionData();
    let oPermissionProps = oComponentProps.permissionConfigView;
    let bIsFunctionPermissionTabSelected = oPermissionViewLeftSectionData.selectedItemId === PermissionEntityTypeDictionary.ENTITY_TYPE_FUNCTION;

    let oPermissionData = {
      permissionDetailData: oPermissionDetailData,
      permissionViewLeftSectionData: oPermissionViewLeftSectionData,
      treeHierarchyData: {
        oHierarchyTreeVisualProps: oPermissionProps.getHierarchyTreeVisualData(),
        oHierarchyTree: oPermissionProps.getHierarchyTree(),
        paginationData: oPermissionProps.getAllEntityChildrenPaginationData(),
        hierarchyNodeContextMenuItemList: [],
        selectedContext: "permissionConfig",
        permissionMap: oPermissionProps.getModifiedPermissionMap(),
        permissionFunctionalMap: oPermissionProps.getModifiedFunctionalPermissionMap(),
        entityType: oPermissionProps.getSelectedEntityType(),
        showSearch: !bIsFunctionPermissionTabSelected,
        showLoadMore:!bIsFunctionPermissionTabSelected
      }
    };

    if (oPermissionProps.getSelectedTreeItemId() === oConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY) {
      let aTabList = TaxonomyPermissionTabLayoutData().tabList;
      let sSelectedTabId = oPermissionProps.getActiveTabId();
      let oHierarchyTree = oPermissionData.treeHierarchyData.oHierarchyTree;
      if(CS.isNotEmpty(oHierarchyTree)) {
        oHierarchyTree.tabsData = {
          selectedTabId: sSelectedTabId,
          tabList: aTabList
        }
      }
    };

    let oModelForRoleCreationDialog = oSelectedRole.isCreated ? this.getModelForRoleCreationDialog(oSelectedRoleDetailedModel) : null;
    let bHideSystemsSelectionView = false; //oSelectedRole.roleType === RoleTypeDictionary.ADMIN;
    let bIsCurrentRoleSystemAdmin = oSelectedRole.roleType === RoleTypeDictionary.SYSTEM_ADMIN;
    let sSearchText = RoleConfigViewProps.getRoleSearchText();
    let aListViewActionItemList = new MockDataForListViewActionItemButton();
    let oRoleCloneDialogView = this.getComponentProps().roleCloneDialogViewProps.getIsRoleCloneDialogActive()
                               ? this.getRoleCloneDialogView() : null;

    let oRoleView = (<div className={"roleConfigView"}>
      <RoleConfigView
        roleListModel={aRoleListModel}
        selectedRoleDetailedModel={oSelectedRoleDetailedModel}
        actionItemModel={oActionItemModel}
        entitiesList={aEntitiesList}
        bRoleCreated={bListItemCreated}
        isPermissionVisible={bIsPermissionVisible}
        permissionsData={oPermissionData}
        modelForRoleCreationDialog={oModelForRoleCreationDialog}
        hideSystemsSelectionView={bHideSystemsSelectionView}
        isUserSystemAdmin={bIsCurrentRoleSystemAdmin}
        searchText = {sSearchText}
        listViewActionItem={aListViewActionItemList}
      />
      {oRoleCloneDialogView}
    </div>)
    return oRoleView;
  };

  getSSOSettingCreateDialogModel =(oActiveSSOSetting) => {
    let SSOSettingConfigViewProps =  this.getComponentProps().ssoSettingConfigViewProps;

    return {
        domain: oActiveSSOSetting.domain || "",
        code: oActiveSSOSetting.code || "",
        ssoSetting: {
          items: SSOSettingConfigViewProps.getSSOSettingsConfigurationList(),
          selectedItems:  CS.isNotEmpty(oActiveSSOSetting.idp) ? [oActiveSSOSetting.idp] : [],
          cannotRemove: true,
          context: "SSOSetting",
          disabled: false,
          label: "",
          isMultiSelect: false,
          disableCross: true,
          hideTooltip: true
        },
    };
  };

  getSSOSettingView = () => {
    let oSSOSettingConfigView =  this.getComponentProps().ssoSettingConfigViewProps;
    let oActiveSSOSetting = oSSOSettingConfigView.getActiveSSOSetting();
    oActiveSSOSetting = oActiveSSOSetting.clonedObject ? oActiveSSOSetting.clonedObject : oActiveSSOSetting;
    let oModel = this.getSSOSettingCreateDialogModel(oActiveSSOSetting);

    let oCreateSSOSettingDialog = null;
    if(!CS.isEmpty(oActiveSSOSetting)){
      oCreateSSOSettingDialog  = <ConfigEntityCreationDialogView
          activeEntity={oActiveSSOSetting}
          entityType={"ssoSetting"}
          model={oModel}
      />
    }

    let oGridViewDataForSSO = {
        context: GridViewContexts.SSO_SETTING,
      tableContextId: GridViewContexts.SSO_SETTING,
        showCheckboxColumn: true,
        disableDeleteButton:false,
        disableCreate:false
    };

    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    return (
        <div className="configGridViewContainer" key="ssoSettingGridViewContainer">
          <SSOSettingView {...oGridViewDataForSSO} columnOrganizerData={oColumnOrganizerData} />
          {oCreateSSOSettingDialog}
        </div>
    );
  };

  getMappingConfigSectionLayoutModel = () => {
    var oAppData = this.getAppData();
    var oComponentProps = this.getComponentProps();
    var MappingProps = oComponentProps.mappingConfigView;
    var oActiveMapping = MappingProps.getSelectedMapping();
    oActiveMapping = oActiveMapping.clonedObject || oActiveMapping;
    var oModel = {};
    var selectedTabId = MappingProps.getSelectedId();
    var oTabSummaryData = {};
    var aMappingSummeryConfigData = oActiveMapping ?
                                    CS.concat(oActiveMapping.attributeMappings,oActiveMapping.tagMappings,oActiveMapping.taxonomyMappings,oActiveMapping.classMappings) : [];
    let oConfigData = MappingProps.getConfigData();

    /** Filtering the Tab Data based on Mapping Type **/
    let aInboundTabList = [TabHeaderData.class, TabHeaderData.taxonomy, TabHeaderData.propertyCollection, TabHeaderData.relationship, TabHeaderData.contextTag];
    let aOutboundTabList = [TabHeaderData.class, TabHeaderData.taxonomy, TabHeaderData.propertyCollection,TabHeaderData.relationship, TabHeaderData.contextTag];
    let aHeaderData = MappingProps.getTabHeaderData();
    let aNewHeaderData = [];
    if (oActiveMapping.mappingType == MappingTypeDictionary.INBOUND_MAPPING) {
    CS.forEach(aInboundTabList, function (sTabId) {
      let oFoundTab = CS.find(aHeaderData, {id : sTabId});
      aNewHeaderData.push(oFoundTab);
    });
    } else {
      CS.forEach(aOutboundTabList, function (sTabId) {
        let oFoundTab = CS.find(aHeaderData, {id : sTabId});
        aNewHeaderData.push(oFoundTab);
      });
    }

    if(selectedTabId == TabHeaderData.relationship){
      let oRelationshipReqResInfo = {
        relationships: {
          requestType: "configData",
          entityName:  ConfigDataEntitiesDictionary.RELATIONSHIPS,
        }
      };

      oTabSummaryData = {
        configRuleMappings: oActiveMapping.relationshipMappings,
        selectedMappingRows: MappingProps.getSelectedMappingRows(),
        summaryType: 'relationship',
        selectedTabId: selectedTabId,
        tabHeaderData: aNewHeaderData,
        referencedData: {
          referencedRelationships: oActiveMapping.configDetails.relationships,
        },
        lazyMSSReqResInfo: oRelationshipReqResInfo,
        mappingType: oActiveMapping.mappingType,
        isExportAllCheckboxClicked: oActiveMapping.isAllRelationshipsSelected
      }
    }

    if(selectedTabId == TabHeaderData.taxonomy){
      let oTaxonomyReqResInfo = {
        taxonomies: {
          requestType: "configData",
          entityName:  ConfigDataEntitiesDictionary.TAXONOMIES,
        }
      };

      oTabSummaryData = {
        configRuleMappings: oActiveMapping.taxonomyMappings,
        selectedMappingRows: MappingProps.getSelectedMappingRows(),
        summaryType: 'taxonomies',
        selectedTabId: selectedTabId,
        tabHeaderData: aNewHeaderData,
        referencedData: {
          referencedTaxonomies: oConfigData.referencedTaxonomies
        },
        lazyMSSReqResInfo: oTaxonomyReqResInfo,
        mappingType: oActiveMapping.mappingType,
        isExportAllCheckboxClicked: oActiveMapping.isAllTaxonomiesSelected
      }
    }

    else if(selectedTabId == TabHeaderData.class){
      let aMockDataForKlassTypes = new MockDataForKlassTypes();
      let oKlassReqResInfo = {
        classes: {
          requestType: "customType",
          responsePath: ["success", "list"],
          requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetKlassesListByBaseType),
          customRequestModel: {
            types: CS.map(aMockDataForKlassTypes, "value"),
          }
        }
      }

      oTabSummaryData = {
        configRuleMappings: oActiveMapping.classMappings,
        selectedMappingRows: MappingProps.getSelectedMappingRows(),
        summaryType: 'classes',
        selectedTabId: selectedTabId,
        tabHeaderData: aNewHeaderData,
        referencedData: {
          referencedKlasses: oConfigData.referencedKlasses
        },
        lazyMSSReqResInfo: oKlassReqResInfo,
        mappingType: oActiveMapping.mappingType,
        isExportAllCheckboxClicked: oActiveMapping.isAllClassesSelected
      }
    }

    if (selectedTabId == TabHeaderData.propertyCollection) {
      let oPropertyGroupReqResInfo = {
        propertyCollections: {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.PROPERTY_COLLECTIONS,
        }
      };

      oTabSummaryData = {
        configRuleMappings: oActiveMapping.propertyCollections,
        selectedMappingRows: MappingProps.getSelectedMappingRows(),
        summaryType: 'propertyCollections',
        selectedTabId: selectedTabId,
        tabHeaderData: aNewHeaderData,
        referencedData: {
          referencedPropertyCollections: oConfigData.referencedPropertyCollections,
          referencedAttributes: (oActiveMapping.configDetails == null || oActiveMapping.configDetails.attributes == null) ? {} : oActiveMapping.configDetails.attributes,
          referencedTags: (oActiveMapping.configDetails == null || oActiveMapping.configDetails.tags == null) ? {} : oActiveMapping.configDetails.tags
        },
        lazyMSSReqResInfo: oPropertyGroupReqResInfo,
        mappingType: oActiveMapping.mappingType,
        isExportAllCheckboxClicked: oActiveMapping.isAllPropertyCollectionSelected,
        selectedPropertyCollectionForMapping: (oActiveMapping.configDetails == null || oActiveMapping.configDetails.propertyCollections == null) ? {} : oActiveMapping.configDetails.propertyCollections,
        configRuleMappingsForAttributes: oActiveMapping.attributeMappings,
        configRuleMappingsForTags: oActiveMapping.tagMappings,
        selectedPropertyCollectionId:oActiveMapping.selectedPropertyCollectionId || "",
        searchViewText:MappingProps.getSearchViewText(),
        bIsPropertyCollectionToggleFlag: MappingProps.getPropertyCollectionToggleFlag(),
      }
    }

    if (selectedTabId == TabHeaderData.contextTag) {
      let oContextReqResInfo = {
        contexts: {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.CONTEXTS,
        }
      };

      oTabSummaryData = {
        configRuleMappings: oActiveMapping.contextMappings,
        selectedMappingRows: MappingProps.getSelectedMappingRows(),
        isExportAllCheckboxClicked: oActiveMapping.isAllContextsSelected,
        summaryType: 'contexts',
        selectedTabId: selectedTabId,
        tabHeaderData: aNewHeaderData,
        referencedData: {
          referencedContexts: oConfigData.referencedContexts,
          referencedAttributes: (oActiveMapping.configDetails == null || oActiveMapping.configDetails.attributes == null) ? {} : oActiveMapping.configDetails.attributes,
          referencedTags: (oActiveMapping.configDetails == null || oActiveMapping.configDetails.tags == null) ? {} : oActiveMapping.configDetails.tags
        },
        lazyMSSReqResInfo: oContextReqResInfo,
        mappingType: oActiveMapping.mappingType,
        selectedContextForMapping: (oActiveMapping.configDetails == null || oActiveMapping.configDetails.contexts == null) ? {} : oActiveMapping.configDetails.contexts,
        configRuleMappingsForAttributes: oActiveMapping.attributeMappings,
        configRuleMappingsForTags: oActiveMapping.tagMappings,
        selectedContextId: oActiveMapping.selectedContextId || "",
        searchViewText: MappingProps.getSearchViewText()
      }
    }

    oModel.label = oActiveMapping.label;
    oModel.processes = this.getSelectionToggleModel(oAppData.getProcessList(), oActiveMapping.processes, "mapping", false);
    oModel.mappingSummary = {
      configRuleMappings: aMappingSummeryConfigData
    };
    oModel.tabSummary = oTabSummaryData;
    oModel.isRuntimeMappingEnabled = {
      isSelected: oActiveMapping.isRuntimeMappingEnabled,
      context: "mapping"
    };
    oModel.code = (!CS.isEmpty(oActiveMapping)) ? oActiveMapping.code : "";
    oModel.mappingType = this._getMSSObject("",true,MockDataForMappingTypes(),[oActiveMapping.mappingType],"mapping", {},"", true);

    return oModel;
  };

  /** Set Translations to the label for HeaderData **/
  _setTranslationsToHeaderData = (AuthorizationMappingProps) => {
    let aTabHeaderData = AuthorizationMappingProps.getTabHeaderData();
    CS.forEach(aTabHeaderData, function (oTabHeaderData) {
      switch (oTabHeaderData.id) {
        case 'CLASS_MAP' :
          oTabHeaderData.label = getTranslation().KLASS;
          break;
        case 'CONTEXT_MAP' :
          oTabHeaderData.label = getTranslation().VARIANT;
          break;
        case 'RELATIONSHIP_MAP' :
          oTabHeaderData.label = getTranslation().RELATIONSHIP;
          break;
        case 'ATTRIBUTE_MAP' :
          oTabHeaderData.label = getTranslation().ATTRIBUTE;
          break;
        case 'TAG_MAP' :
          oTabHeaderData.label = getTranslation().TAG;
          break;
        case 'TAXONOMY_MAP' :
          oTabHeaderData.label = getTranslation().TAXONOMY;
          break;
      }
    });
    AuthorizationMappingProps.setTabHeaderData(aTabHeaderData);
  }

  getAuthorizationMappingConfigSectionLayoutModel = () => {
    let oComponentProps = this.getComponentProps();
    let AuthorizationMappingProps = oComponentProps.authorizationMappingConfigView;
    let oActiveAuthorizationMapping = AuthorizationMappingProps.getSelectedMapping();
    oActiveAuthorizationMapping = oActiveAuthorizationMapping.clonedObject || oActiveAuthorizationMapping;
    let oModel = {};
    let selectedTabId = AuthorizationMappingProps.getSelectedId();
    let oTabSummaryData = {};
    let oConfigData = AuthorizationMappingProps.getConfigData();
    let oCheckboxClickedDetails = AuthorizationMappingProps.getCheckboxClickedDetails();
    let oToggleSelectionClickedDetails = AuthorizationMappingProps.getToggleSelectionClickedDetails();
    this._setTranslationsToHeaderData(AuthorizationMappingProps);

    if (selectedTabId == TabHeaderData.attribute) {
      let oAttributeReqResInfo = {
        attributes: {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.ATTRIBUTES,
          typesToExclude : [AttributeTypeDictionary.CALCULATED,AttributeTypeDictionary.CONCATENATED]
        }
      };

      oTabSummaryData = {
        selectedMappings: oActiveAuthorizationMapping.attributeMappings,
        summaryType: 'attributes',
        selectedTabId: selectedTabId,
        isCheckboxClicked: oCheckboxClickedDetails.attributes,
        isToggleSelectionClicked: oToggleSelectionClickedDetails.attributes,
        tabHeaderData: AuthorizationMappingProps.getTabHeaderData(),
        referencedData: {
          referencedAttributes: oConfigData.referencedAttributes
        },
        lazyMSSReqResInfo: oAttributeReqResInfo
      }
    } else if (selectedTabId == TabHeaderData.tag) {
      let oTagReqResInfo = {
        tags: {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.TAGS,
        },
        tagValues: {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.TAG_VALUES,
        }
      };

      oTabSummaryData = {
        selectedMappings: oActiveAuthorizationMapping.tagMappings,
        summaryType: 'tags',
        selectedTabId: selectedTabId,
        isCheckboxClicked: oCheckboxClickedDetails.tags,
        isToggleSelectionClicked: oToggleSelectionClickedDetails.tags,
        tabHeaderData: AuthorizationMappingProps.getTabHeaderData(),
        referencedData: {
          referencedTags: oConfigData.referencedTags
        },
        lazyMSSReqResInfo: oTagReqResInfo
      }
    } else if (selectedTabId == TabHeaderData.taxonomy) {
      let oTaxonomyReqResInfo = {
        taxonomies: {
          requestType: "customType",
          entityName: ConfigDataEntitiesDictionary.TAXONOMIES,
          responsePath: ["success", "list"],
          requestURL: RequestMapping.getRequestUrl(oSettingsRequestMapping.GetMajorTaxonomy),
          customRequestModel: {
            taxonomyId: -1,
            types: [],
            taxonomyTypes: ["majorTaxonomy", "minorTaxonomy"]
          }
        }
      };

      oTabSummaryData = {
        selectedMappings: oActiveAuthorizationMapping.taxonomyMappings,
        summaryType: 'taxonomies',
        selectedTabId: selectedTabId,
        isCheckboxClicked: oCheckboxClickedDetails.taxonomies,
        isToggleSelectionClicked: oToggleSelectionClickedDetails.taxonomies,
        tabHeaderData: AuthorizationMappingProps.getTabHeaderData(),
        referencedData: {
          referencedTaxonomies: oConfigData.referencedTaxonomies
        },
        lazyMSSReqResInfo: oTaxonomyReqResInfo
      }
    } else if (selectedTabId == TabHeaderData.class) {
      let oKlassReqResInfo = {
        classes: {
          requestType: "customType",
          responsePath: ["success", "list"],
          requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetKlassesListByBaseType),
          customRequestModel: {
            isNatureAndNonNature: true,
            types: [MockDataForEntityBaseTypesDictionary.articleKlassBaseType,MockDataForEntityBaseTypesDictionary.assetKlassBaseType],
            typesToExclude: [NatureTypeDictionary.GTIN, NatureTypeDictionary.EMBEDDED,NatureTypeDictionary.TECHNICAL_IMAGE]
          }
        }
      }

      oTabSummaryData = {
        selectedMappings: oActiveAuthorizationMapping.classMappings,
        summaryType: 'classes',
        selectedTabId: selectedTabId,
        isCheckboxClicked: oCheckboxClickedDetails.classes,
        isToggleSelectionClicked: oToggleSelectionClickedDetails.classes,
        tabHeaderData: AuthorizationMappingProps.getTabHeaderData(),
        referencedData: {
          referencedKlasses: oConfigData.referencedKlasses
        },
        lazyMSSReqResInfo: oKlassReqResInfo

      }
    } else if (selectedTabId == TabHeaderData.context) {
      let oContextReqResInfo = {
        contexts: {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.CONTEXTS,
          types: [ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT,ContextTypeDictionary.CONTEXTUAL_VARIANT,ContextTypeDictionary.IMAGE_VARIANT,ContextTypeDictionary.GTIN_CONTEXT]
        }
      };

      oTabSummaryData = {
        selectedMappings: oActiveAuthorizationMapping.contextMappings,
        summaryType: 'contexts',
        selectedTabId: selectedTabId,
        isCheckboxClicked: oCheckboxClickedDetails.contexts,
        isToggleSelectionClicked: oToggleSelectionClickedDetails.contexts,
        tabHeaderData: AuthorizationMappingProps.getTabHeaderData(),
        referencedData: {
          referencedContexts: oConfigData.referencedContexts
        },
        lazyMSSReqResInfo: oContextReqResInfo
      }
    } else if (selectedTabId == TabHeaderData.relationship) {
      let oRelationshipReqResInfo = {
        relationships: {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.ROOT_RELATIONSHIPS,
          typesToExclude: []
        }
      };

      oTabSummaryData = {
        selectedMappings: oActiveAuthorizationMapping.relationshipMappings,
        summaryType: 'relationships',
        selectedTabId: selectedTabId,
        isCheckboxClicked: oCheckboxClickedDetails.relationships,
        isToggleSelectionClicked: oToggleSelectionClickedDetails.relationships,
        tabHeaderData: AuthorizationMappingProps.getTabHeaderData(),
        referencedData: {
          referencedRelationships: oConfigData.referencedRelationships
        },
        lazyMSSReqResInfo: oRelationshipReqResInfo
      }
    }
    oModel.label = oActiveAuthorizationMapping.label;
    oModel.tabSummary = oTabSummaryData;
    oModel.code = (!CS.isEmpty(oActiveAuthorizationMapping)) ? oActiveAuthorizationMapping.code : "";

    return oModel;
  };

  getCreateDialogModelForEventsOrTasks = (oActiveEventOrTask, sContext) => {
    let sSplitter = SettingUtils.getSplitter();

    var oCreateModel = {
      type: {
        items: new MockDataForTaskTypes(),
        selectedItems: !CS.isEmpty(oActiveEventOrTask.type) ? [oActiveEventOrTask.type] : [],
        cannotRemove: true,
        context: sContext + sSplitter + "type",
        disabled: false,
        label: "",
        isMultiSelect: false,
        disableCross: true,
        hideTooltip: true
      }
    };

    return oCreateModel;
  };

  getOrganisationCreateDialogModel =(oActiveOrganisation) => {
    let sSplitter = SettingUtils.getSplitter();

    /** Removed "Internal" from Organisation Type*/
    let aItems = new MockDataForOrganisationTypes();
    aItems = CS.reject(aItems, {id:"internal"});

    return {
      type: {
        items: aItems,
        selectedItems:  !CS.isEmpty(oActiveOrganisation.type) ? [oActiveOrganisation.type] : [],
        cannotRemove: true,
        context: "organisation" + sSplitter + "type",
        disabled: false,
        label: "",
        isMultiSelect: false,
        disableCross: true,
        hideTooltip: true
      }
    };
  };

  getEndPointConfigView = () => {
    let oComponentProps = this.getComponentProps();
    let oEndPointProps = oComponentProps.profileConfigView;
    let bIsEndpointDirty = false;
    let oActiveEndPoint = oEndPointProps.getSelectedProfile();
    if (oActiveEndPoint.clonedObject) {
      oActiveEndPoint = oActiveEndPoint.clonedObject;
      bIsEndpointDirty = true;
    }

    let bShowCheckboxColumn = true;
    let bIsStandardAttributeScreen = false;
    let oAppData = SettingUtils.getAppData();
    let oActionBarList = oAppData.getActionBarList();
    let oActionItemModel = {};
    let oEndpointConfigView = oActionBarList.ProfileConfigView;
    oActionItemModel.ProfileListView = oEndpointConfigView.ProfileConfigListView;
    oActionItemModel.ProfileDetailedView = [this.getItemList([])];
    let bIsDialogOpen = oEndPointProps.getIsProfileDialogActive();

    /** Props for Copy Feature **/
    let bEnableCopyButton = true;
    let bSaveAsDialogActive = oEndPointProps.getIsSaveAsDialogActive();
    let aDataForSaveAsDialog = oEndPointProps.getDataForSaveAsDialog();
    let aDataForWorkflowSaveAsDialog = oEndPointProps.getDataForWorkflowSaveAsDialog();
    let aDuplicateCodes = oEndPointProps.getDuplicateCodesForEndpoints();
    let aDuplicateLabels = oEndPointProps.getDuplicateLabelsForEndpoints();
    let aDuplicateCodesForWorkflows = oEndPointProps.getDuplicateCodesForWorkflows();
    let aDuplicateLabelsForWorkflows = oEndPointProps.getDuplicateLabelsForWorkflows();
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };
    let oSelectIconDialog = this.getSelectIconDialog();

    return <EndpointConfigView
        context={GridViewContexts.END_POINT}
        showCheckboxColumn={bShowCheckboxColumn}
        disableDeleteButton={bIsStandardAttributeScreen}
        enableImportExportButton={false}
        disableCreate={bIsStandardAttributeScreen}
        activeEndpoint={oActiveEndPoint}
        isEndpointDirty={bIsEndpointDirty}
        isDialogOpen={bIsDialogOpen}
        actionItemModel={oActionItemModel}
        sectionLayoutModel={this.getEndpointConfigSectionLayoutModel()}
        enableCopyButton = {bEnableCopyButton}
        isSaveAsDialogOpen = {bSaveAsDialogActive}
        dataForSaveAsDialog = {aDataForSaveAsDialog}
        dataForWorkflowSaveAsDialog={aDataForWorkflowSaveAsDialog}
        columnOrganizerData={oColumnOrganizerData}
        duplicateCodes={aDuplicateCodes}
        duplicateLabels={aDuplicateLabels}
        duplicateCodesForWorkflows={aDuplicateCodesForWorkflows}
        duplicateLabelsForWorkflows={aDuplicateLabelsForWorkflows}
        selectIconDialog = {oSelectIconDialog}
    />
  }

  getOrganisationBasicInfoData = () => {
    let oOrganisationConfigProps = this.getComponentProps().organisationConfigView;
    let oActiveOrganisation = oOrganisationConfigProps.getActiveOrganisation();
    let aAllowedTaxonomies = oOrganisationConfigProps.getAllowedTaxonomies();
    let oReferencedTaxonomies = oOrganisationConfigProps.getReferencedTaxonomies();
    let oAllowedTaxonomyHierarchyList = oOrganisationConfigProps.getAllowedTaxonomyHierarchyList();

    let oMultiTaxonomyData = SettingUtils.getMultiTaxonomyData(oActiveOrganisation.taxonomyIds, oReferencedTaxonomies, aAllowedTaxonomies, oAllowedTaxonomyHierarchyList);
    let sMSSContext = `${ViewContextConstants.ORGANISATION_CONFIG_CONTEXT}${SettingUtils.getSplitter()}${"klassIds"}`;
    let aSelectedKlassIds = oActiveOrganisation.klassIds || [];
    let aMockDataForKlassTypes = new MockDataForKlassTypes();
    let oRequestResponseInfo = {
      requestType: "customType",
      responsePath: ["success", "list"],
      requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetKlassesListByBaseType),
      customRequestModel: {
        types: CS.map(aMockDataForKlassTypes, "value"),
        isStandard: false,
        isNature: null
      }
    };

    let oMultiKlassData = {
      selectedItems: aSelectedKlassIds,
      context: sMSSContext,
      isMultiSelect: true,
      bShowIcon: true,
      requestResponseInfo: oRequestResponseInfo,
      referencedData: oOrganisationConfigProps.getReferencedKlasses()
    };

    let oTaxonomyPaginationData = oOrganisationConfigProps.getTaxonomyPaginationData();

    return {
      multiTaxonomyData: oMultiTaxonomyData,
      multiKlassData: oMultiKlassData,
      taxonomyPaginationData: oTaxonomyPaginationData
    }
  };

  getModelForRoleCreationDialog = (oSelectedRoleModel) => {
    let aRoleTypes = new MockDataForUserRoleTypes();
    let oRoleTypeMSSModel = oSelectedRoleModel.properties['roleTypeData'];
    oRoleTypeMSSModel.disabled = false;
    oRoleTypeMSSModel.selectedItems = !CS.isEmpty(oRoleTypeMSSModel.selectedItems) ? oRoleTypeMSSModel.selectedItems : [aRoleTypes[1].id];
    let sCode = oSelectedRoleModel.properties['code'];

    return {
      roleType: oRoleTypeMSSModel
    };
  };

  getEntityDialog = () => {
    let oEntityDatList = ManageEntityConfigProps.getDataForDialog();
    let bIsDelete = ManageEntityConfigProps.getIsDelete();
    return (
        <ManageEntityDialogView
            oEntityDatList = {oEntityDatList}
            bIsDelete={bIsDelete}
        />
    );
  };

  getSelectIconDialog = () => {
    let bShowSelectIconDialog = IconLibrarySelectIconDialogProps.getShowSelectIconDialog();
    if(!bShowSelectIconDialog){
      return null;
    }

    let oIconLibraryData = SettingUtils.getIconLibraryData();
    let sContentId = IconLibrarySelectIconDialogProps.getContentId();
    let sPropertyId = IconLibrarySelectIconDialogProps.getPropertyId();
    let sContext = IconLibrarySelectIconDialogProps.getContext();
    let sPathToRoot = IconLibrarySelectIconDialogProps.getPathToRoot();
    let oGridViewData = {contentId: sContentId, propertyId: sPropertyId, context: sContext, pathToRoot: sPathToRoot};
    return (
        <IconLibrarySelectIconView
            selectIconData={oIconLibraryData}
            isGridView={true}
            gridViewData={oGridViewData}
        />
    );
  };

  getGroupedSelectionToggleViewModel = (oSystem, aSelectedEndpoints, sContext, bIsDisabled, oMasterSystemsMap) => {
    let aGroupedSelectionToggleViewModels = [];
    let aOnBoardinEndpoints = [];
    let aOffBoardingEndpoints = [];
    let iCheckboxStatus = 1;
    let sInboundEndpointsLabel = getTranslation().INBOUND_ENDPOINTS + ": ";
    let sOutboundEndpointsLabel = getTranslation().OUTBOUND_ENDPOINTS + ": ";
    let sViewContext = sContext === "role" ? "endpoints" : "endpointIds";
    let aDisabledEndpoints = [];
    let aSystemEndpointIds = [];

    CS.forEach(oSystem.list, function (oEndpoint) {
      if (CS.isEmpty(oEndpoint.id)) {
        oEndpoint = CS.find(oMasterSystemsMap[oSystem.id].list, {id: oEndpoint});
      }
      if (oEndpoint.endpointType === EndPointTypeDictionary.INBOUND_ENDPOINT) {
        aOnBoardinEndpoints.push(oEndpoint);
      } else {
        aOffBoardingEndpoints.push(oEndpoint);
      }
      if (bIsDisabled) {
        aDisabledEndpoints.push(oEndpoint.id);
      }
      aSystemEndpointIds.push(oEndpoint.id);
    });

    let aUnSelectedEndpointsInSystem = CS.difference(aSystemEndpointIds, aSelectedEndpoints);
    if (aUnSelectedEndpointsInSystem.length === 0) {
      iCheckboxStatus = 2;
    } else if (aUnSelectedEndpointsInSystem.length === oSystem.list.length) {
      iCheckboxStatus = 0;
    } else {
      iCheckboxStatus = 1;
    }

    aGroupedSelectionToggleViewModels.push({
      id: EndPointTypeDictionary.INBOUND_ENDPOINT,
      label: sInboundEndpointsLabel,
      list: aOnBoardinEndpoints
    });

    aGroupedSelectionToggleViewModels.push({
      id: EndPointTypeDictionary.OUTBOUND_ENDPOINT,
      label: sOutboundEndpointsLabel,
      list: aOffBoardingEndpoints
    });

    return {
      endpointGroups: aGroupedSelectionToggleViewModels,
      selectedEndpoints: aSelectedEndpoints,
      disabledEndpoints: aDisabledEndpoints,
      context: sContext,
      contextKey: sViewContext,
      checkboxStatus: iCheckboxStatus,
    };
  };

  getModelForSystemsSelectionView = (aSystems, aSelectedEndpoints, sContext, bIsDisabled, aSystemList, oSystemMap) => {
    let _this = this;
    let oAppData = this.getAppData();
    let oMasterSystemsMap = oAppData.getSystemsMap();
    let aContextMenuViewModel = [];
    let aEndpointsSelectionViewModels = [];

    CS.forEach(aSystemList, function (oSystem) {
      aContextMenuViewModel.push(new ContextMenuViewModel(oSystem.id, CS.getLabelOrCode(oSystem), false, "", {}));
    });

    CS.forEach(aSystems, function (sSystemId) {
      let oEndpointsSelectionViewModel = {};
      let oSystem = oSystemMap[sSystemId];

      oEndpointsSelectionViewModel.label = CS.getLabelOrCode(oSystem)|| oMasterSystemsMap[sSystemId].label;
      oEndpointsSelectionViewModel.disabled = bIsDisabled;
      oEndpointsSelectionViewModel.groupedSelectionViewModel = _this.getGroupedSelectionToggleViewModel(oSystem, aSelectedEndpoints, sContext, bIsDisabled, oMasterSystemsMap);
      oEndpointsSelectionViewModel.checkboxStatus = oEndpointsSelectionViewModel.groupedSelectionViewModel.checkboxStatus;
      oEndpointsSelectionViewModel.systemId = sSystemId;
      delete oEndpointsSelectionViewModel.groupedSelectionViewModel.checkboxStatus;

      aEndpointsSelectionViewModels.push(oEndpointsSelectionViewModel);
    });

    return {
      context: sContext,
      selectedItems: aSystems,
      contextMenuViewModel: aContextMenuViewModel,
      endpointsSelectionViewModels: aEndpointsSelectionViewModels
    };
  };

  getOrganisationConfigView = () => {
    var oAppData = this.getAppData();
    var oComponentProps = this.getComponentProps();
    var oOrganisationConfigViewProps = oComponentProps.organisationConfigView;
    var oActionBarList = oAppData.getActionBarList();
    var oActionItemModel = oActionBarList.OrganisationConfigView;

    oActionItemModel.organisationConfigListView = oActionItemModel.OrganisationConfigListView;
    oActionItemModel.organisationConfigDetailedView = [];
    let aOrgConfigListModel = this.getOrganisationConfigListViewModels();
    let oActiveOrganisation = oOrganisationConfigViewProps.getActiveOrganisation();
    let bIsConfigDirty = false;
    if (oActiveOrganisation.clonedObject) {
      oActiveOrganisation = oActiveOrganisation.clonedObject;
      bIsConfigDirty = true;
    }
    let bListItemCreated = false;
    let sContext = ViewContextConstants.ORGANISATION_CONFIG_CONTEXT;
    let sActiveTabId = oOrganisationConfigViewProps.getOrganisationConfigTabId();
    let bIsPermissionVisible = oOrganisationConfigViewProps.getIsPermissionVisible();
    let bShowLoadMore = oOrganisationConfigViewProps.getShowLoadMore();
    if (sActiveTabId == Constants.ORGANISATION_CONFIG_ROLES || sActiveTabId == Constants.ORGANISATION_CONFIG_SSO_SETTING) {
      CS.remove(oActionItemModel.organisationConfigListView, {id: "manage_entity_partners"});
    }
    let oRuleConfigView = sActiveTabId == Constants.ORGANISATION_CONFIG_ROLES ? this.getRoleView() : null;
    let oSSOSettingView = sActiveTabId == Constants.ORGANISATION_CONFIG_SSO_SETTING ? this.getSSOSettingView() : null;
    // Detail View data
    let oOrganisationBasicInfoData = this.getOrganisationBasicInfoData();
    let aSystemList = oAppData.getSystemsList();
    let oSystemMap = oAppData.getSystemsMap();
    var oSelectedRole = oComponentProps.roleConfigView.getSelectedRole();
    var oPermission = oComponentProps.permissionConfigView.getActivePermission();
    if (oSelectedRole.clonedObject || oPermission.clonedObject) {
      bIsConfigDirty = true;
    }
    let bHideRolePermissionToggle = CS.isEmpty(oSelectedRole);
    let oSystemsSelectionViewModel = !CS.isEmpty(aSystemList) && sActiveTabId === Constants.ORGANISATION_CONFIG_INFORMATION ?
                                     this.getModelForSystemsSelectionView(oActiveOrganisation.systems, oActiveOrganisation.endpointIds, sContext, false, aSystemList, oSystemMap) : {};

    let oOrganisationCreateDialogModel = this.getOrganisationCreateDialogModel(oActiveOrganisation);
    let sSearchText = oOrganisationConfigViewProps.getSearchText();
    let oIconLibraryData = SettingUtils.getIconLibraryData();

    return <OrganisationConfigView
        actionItemModel={oActionItemModel}
        organisationConfigListModel={aOrgConfigListModel}
        activeOrganisation={oActiveOrganisation}
        organisationBasicInformationData = {oOrganisationBasicInfoData}
        listItemCreated={bListItemCreated}
        ruleConfigView={oRuleConfigView}
        ssoSettingView={oSSOSettingView}
        context = {sContext}
        activeTabId = {sActiveTabId}
        isPermissionVisible={bIsPermissionVisible}
        systemList={aSystemList}
        hideRuleConfigPermissionToggle={bHideRolePermissionToggle}
        systemsSelectionViewModel={oSystemsSelectionViewModel}
        isDirty={bIsConfigDirty}
        organisationCreateDialogModel= {oOrganisationCreateDialogModel}
        showLoadMore = {bShowLoadMore}
        searchText = {sSearchText}
        iconLibraryData = {oIconLibraryData}
    />;
  };

  getMappingView = () => {
    let oComponentProps = this.getComponentProps();
    let oMappingProps = oComponentProps.mappingConfigView;
    let bIsDialogOpen = oMappingProps.getIsMappingDialogActive();
    let bShowCheckboxColumn = true;
    let bIsStandardAttributeScreen = false;

    let oMappingConfigProps = oComponentProps.mappingConfigView;
    let oActiveMapping = oMappingConfigProps.getSelectedMapping();
    let bIsMappingDirty = false;
    if (oActiveMapping.clonedObject) {
      oActiveMapping = oActiveMapping.clonedObject;
      bIsMappingDirty = true;
    }
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    /** Props for Copy Feature **/
    let bEnableCopyButton = true;
    let bSaveAsDialogActive = oMappingConfigProps.getIsSaveAsDialogActive();
    let aDataForSaveAsDialog = oMappingConfigProps.getDataForSaveAsDialog();
    let aDuplicateCodes = oMappingConfigProps.getCodeDuplicates();
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    return <MappingConfigView
        sectionLayoutModel={this.getMappingConfigSectionLayoutModel()}
        context={GridViewContexts.MAPPING}
        showCheckboxColumn={bShowCheckboxColumn}
        disableDeleteButton={bIsStandardAttributeScreen}
        enableImportExportButton={false}
        disableCreate={bIsStandardAttributeScreen}
        activeMapping={oActiveMapping}
        isMappingDirty={bIsMappingDirty}
        isDialogOpen={bIsDialogOpen}
        oManageEntityDialog={oManageEntityDialog}
        enableManageEntityButton={true}
        enableCopyButton = {bEnableCopyButton}
        isSaveAsDialogOpen = {bSaveAsDialogActive}
        dataForSaveAsDialog = {aDataForSaveAsDialog}
        duplicateCodes = {aDuplicateCodes}
        columnOrganizerData={oColumnOrganizerData}
    />;

  };

  getTemplateView = () => {
    var oAppData = this.getAppData();
    var oComponentProps = this.getComponentProps();
    var TemplateProps = oComponentProps.templateConfigView;
    var oActionBarList = oAppData.getActionBarList();
    var oTemplateConfigView = oActionBarList.TemplateConfigView;
    var oActionItemModel = {};
    oActionItemModel.TemplateListView = oTemplateConfigView.TemplateConfigListView;
    oActionItemModel.TemplateDetailedView = [];
    let bShowCheckboxColumn = true;
    let oActiveTemplate = TemplateProps.getActiveTemplate();
    oActiveTemplate = oActiveTemplate.clonedObject ? oActiveTemplate.clonedObject : oActiveTemplate;
    let oCreateTemplateDialog = !CS.isEmpty(oActiveTemplate) ? this.getCreateEntityDialog(oActiveTemplate, "template") : null;
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let aSelectedColumns = ColumnOrganizerProps.getSelectedOrganizedColumns();
    let bIsColumnOrganizerDialogOpen = ColumnOrganizerProps.getIsDialogOpen();

    return <div className="configGridViewContainer" key="templatesConfigGridViewContainer">
      <ContextualGridView
          context = {GridViewContexts.TEMPLATE}
          tableContextId={GridViewContexts.TEMPLATE}
          showCheckboxColumn = {bShowCheckboxColumn}
          showGridColumnOrganiserButton={true}
          columnOrganizerData={this.getColumnOrganizerData()}
          selectedColumns={aSelectedColumns}
          isColumnOrganizerDialogOpen={bIsColumnOrganizerDialogOpen}
      />
      {oCreateTemplateDialog}
      {oManageEntityDialog}
    </div>

  };

  getPermissionTableViewData = () => {
    var oComponentProps = this.getComponentProps();
    var PermissionProps = oComponentProps.permissionConfigView;
    let oActivePermission = PermissionProps.getActivePermission();
    let sSelectedEntity = PermissionProps.getSelectedEntityType();
    let oPermissionTableData = {};
    let oPropertyCollectionData = {};
    let oRelationshipData = {};
    let oGeneralInformationData = {};

    if(CS.isNotEmpty(oActivePermission)) {
      let oPropertyCollectionPermission = oActivePermission.templateInPermission.templatePermission.propertyCollectionPermission;

      CS.forEach(oPropertyCollectionPermission, (oPropertyCollection, sKey) => {
        let oTableViewPropsForPropertyCollection = ContextualTableViewProps.getTableViewPropsByContext(sKey);
        oPropertyCollectionData[sKey] = {
          headerData: oTableViewPropsForPropertyCollection.getTableViewHeaderData(),
          rowData: oTableViewPropsForPropertyCollection.getTableViewRowData(),
          screenContext: "propertyCollection",
          showCode: true,
        }
      });
      oPermissionTableData.propertyCollectionData = oPropertyCollectionData;

      if (sSelectedEntity === oConfigEntityTypeDictionary.ENTITY_TYPE_CLASS) {
        if(CS.isNotEmpty(oActivePermission.templateInPermission.templatePermission.relationshipPermission)) {
          let oTableViewPropsForRelationship = ContextualTableViewProps.getTableViewPropsByContext("relationship");

          oRelationshipData["relationship"] = {
            headerData: oTableViewPropsForRelationship.getTableViewHeaderData(),
            rowData: oTableViewPropsForRelationship.getTableViewRowData(),
            screenContext: "relationship",
            showCode: true,
          };
          oPermissionTableData.relationshipTableData = oRelationshipData;
        }

        let oTableViewPropsForGeneralInformation = ContextualTableViewProps.getTableViewPropsByContext("generalInformation");

        oGeneralInformationData["generalInformation"] = {
          headerData: oTableViewPropsForGeneralInformation.getTableViewHeaderData(),
          rowData: oTableViewPropsForGeneralInformation.getTableViewRowData(),
          screenContext: "generalInformation",
          showCode: false,
        };
        oPermissionTableData.generalInformationTableData = oGeneralInformationData;
      }
    }
    return oPermissionTableData;
  };

  getPermissionData = () => {
    var oAppData = this.getAppData();
    var oComponentProps = this.getComponentProps();
    var PermissionProps = oComponentProps.permissionConfigView;
    var oPermissionsMap = PermissionProps.getCurrentPermission();
    var bIsPermissionModeClass = PermissionProps.getIsPermissionModeClass();
    let bIsPermissionDirty = PermissionProps.getActivePermission().isDirty ||
        PermissionProps.getIsPermissionDirty();
    var aSelectedIds = PermissionProps.getSelectedIds();

    var aAttributeList = oAppData.getAttributeList();
    var aTagList = oAppData.getTagMap();
    var aRoleList = oAppData.getRoleList();
    var aRelationshipList = oAppData.getRelationshipMasterList();
    var oTemplatesListMap = oAppData.getTemplatesList();
    var oTaskClassMap = oAppData.getTasksList();

    var oClassVisibilityStatus = PermissionProps.getClassVisibilityStatus();

    var aPropertyCollectionList = [];
    var sSelectedFirstLevelId = PermissionProps.getSelectedFirstLevelClass();
    var sSelectedPropertyCollection = PermissionProps.getSelectedPropertyCollection();
    var aPermissionTemplateModelList = this.getPermissionAvailableTemplateModelList();
    var oActiveNatureClassDetails = PermissionProps.getActivePermissionEntity();
    var sSelectedTemplate = PermissionProps.getSelectedTemplate();
    var aClassItems = oPermissionsMap; //[];
    let aAddAllowedTemplatesModels = [];
    let oTemplateInPermission = oPermissionsMap.templateInPermission;
    let aSelectedAllowedTemplates = CS.isEmpty(oTemplateInPermission) || CS.isEmpty(oTemplateInPermission.allowedTemplates) ? [] : oTemplateInPermission.allowedTemplates;
    let oRoleConfigViewProps = this.getComponentProps().roleConfigView;
    let oSelectedRole = oRoleConfigViewProps.getSelectedRole();
    let oPermissionData = this.getPermissionTableViewData();

    if (!CS.isEmpty(oActiveNatureClassDetails)) {

      let aAllowedTemplates = oAppData.getTemplates();
      let aSelectedAllowedTemplatesMap = {};

      CS.forEach(aSelectedAllowedTemplates, function (oAllowedTemplate) {
        aSelectedAllowedTemplatesMap[oAllowedTemplate.id] = true;
      });

      CS.forEach(aAllowedTemplates, function (oSection) {
        if(!aSelectedAllowedTemplatesMap[oSection.id]) {
          let oProperties = {
            code: oSection.code
          };

          aAddAllowedTemplatesModels.push(new ContextMenuViewModel(
              oSection.id,
              oSection.label,
              false,
              "",
              oProperties
          ));
        }
      });
    }

    //TODO: Organisation Extract Permission Data
    return {
      permissionsMap: oPermissionsMap,
      isPermissionDirty: bIsPermissionDirty,
      isPermissionModeClass: bIsPermissionModeClass,
      selectedIds: aSelectedIds,
      selectedFirstLevelId: sSelectedFirstLevelId,
      selectedPropertyCollection: sSelectedPropertyCollection,
      selectedTemplate: sSelectedTemplate,
      classItems: aClassItems,
      propertyCollectionList: aPropertyCollectionList,
      templateList: oTemplatesListMap,
      permissionData: oPermissionData,
      attributeList: aAttributeList,
      tagList: aTagList,
      roleList: aRoleList,
      relationshipList: aRelationshipList,
      taskClasses: oTaskClassMap,
      classVisibilityStatus: oClassVisibilityStatus,
      activeNatureClassDetails: oActiveNatureClassDetails,
      permissionTemplateList: aPermissionTemplateModelList,
      addAllowedTemplatesModels: aAddAllowedTemplatesModels,
      selectedAllowedTemplatesData: aSelectedAllowedTemplates,
      selectedRole: oSelectedRole,
      bHideNoPropertiesSectionView: this.shouldHideNoPropertiesSectionView(),
    };
  };

  fillAddedRuleConfigDetails = (aRules) => {

    let oKpiProps = this.getComponentProps().kpiConfigView;
    let oReferencedData = oKpiProps.getReferencedData();

    let oReferencedAttributes = oReferencedData.referencedAttributes;
    let oReferencedTags = oReferencedData.referencedTags;
    let oAppData = this.getAppData();

    let oAttributeMap = oAppData.getAttributeList();
    let oTagMap = oAppData.getTagMap();

    CS.forEach(aRules, function (oRule) {
      let oConfigDetails = oRule.configDetails || {};
      let oAttributeDetails = {};
      let oTagDetails = {};
      CS.forEach(oRule.attributes, function (oRuleAttribute) {
        let sEntityId = oRuleAttribute.entityId;

        //Store referenced attribute details for main attribute
          if (CS.isEmpty(oReferencedAttributes[sEntityId])) {
            oAttributeDetails[sEntityId] = oAttributeMap[sEntityId];
          } else {
            oAttributeDetails[sEntityId] = oReferencedAttributes[sEntityId];
          }

        //Store referenced attribute details for attributes assigned from "Toggle comparison with attribute"
        let aAttributeRules = oRuleAttribute.rules;
        CS.forEach(aAttributeRules, function (oAttributeRule) {
          let sAttributeLinkId = oAttributeRule.attributeLinkId;
          if (sAttributeLinkId && sAttributeLinkId !== '0') {
            oAttributeDetails[sAttributeLinkId] = oReferencedAttributes[sAttributeLinkId];
          }
        });
      });
      oConfigDetails.referencedAttributes = oAttributeDetails;

      CS.forEach(oRule.tags, function (oRuleTag) {
        let sEntityId = oRuleTag.entityId;
        if (CS.isEmpty(oReferencedTags[sEntityId])) {
          oTagDetails[sEntityId] = oTagMap[sEntityId];
        } else {
          oTagDetails[sEntityId] = oReferencedTags[sEntityId];
        }
      });
      oConfigDetails.referencedTags = oTagDetails;

      oRule.configDetails = oConfigDetails;
    });
  };

  getTabsConfigView = () => {
    let oComponentProps = this.getComponentProps();
    let oTabsProps = oComponentProps.tabsConfigView;
    let oGridViewPropsByContext = oComponentProps.contextualGridViewProps.getGridViewPropsByContext(GridViewContexts.TABS_CONFIG).gridViewProps;
    let sGridViewSortBy = oGridViewPropsByContext.getGridViewSortData().sortBy;
    let oActiveTab = oTabsProps.getActiveTab();
    let aPropertySequenceList = [];
    let oReferencedProperties = oTabsProps.getReferencedProperties();

    CS.forEach(oActiveTab.propertySequenceList, function (sListItem) {
      let oProperty = oReferencedProperties[sListItem];
      oProperty && aPropertySequenceList.push({
            id: oProperty.id,
            label: CS.getLabelOrCode(oProperty),
            type: oProperty.type
          }
      );
    });
    if (!CS.isEmpty(aPropertySequenceList)) {
      oActiveTab.propertySequenceList = aPropertySequenceList;
    }

    let oCreateTabDialog = (!CS.isEmpty(oActiveTab) && oActiveTab.isCreated) ? this.getCreateEntityDialog(oActiveTab, ViewContextConstants.TABS_CONFIG) : null;
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let oSortTabsDialog = oTabsProps.getSortTabsDialogActive() ? this.getSortTabsDialog(oTabsProps.getTabList()) : null;
    let oSelectIconDialog = this.getSelectIconDialog();

    var oGridViewProps = {
      context: GridViewContexts.TABS_CONFIG,
      tableContextId: GridViewContexts.TABS_CONFIG,
      showCheckboxColumn: true,
      showSortButton: true,
      disableSearch: (sGridViewSortBy === "sequence"),
      enableImportExportButton: true,
      enableManageEntityButton: true,
    };

    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    return (<TabsConfigView
      gridViewProps={oGridViewProps}
      activeTabDetails={oActiveTab}
      columnOrganizerData={oColumnOrganizerData}
      ManageEntityDialog={oManageEntityDialog}>
      {oCreateTabDialog}
      {oSortTabsDialog}
      {oSelectIconDialog}
    </TabsConfigView>);

  };

  getRuleConfigView = () => {
    let oComponentProps = this.getComponentProps();
    let oRuleConfigProps = oComponentProps.ruleConfigView;
    let oActiveDetailRule = oRuleConfigProps.getActiveRule();
    let bIsActiveRuleDirty = false;
    if (oActiveDetailRule.clonedObject) {
      oActiveDetailRule = oActiveDetailRule.clonedObject;
      bIsActiveRuleDirty = true;
    }
    let bIsRuleDialogActive = oRuleConfigProps.getIsRuleDialogActive();
    let bShowCheckboxColumn = true;

    let oGridViewProps = {
      context: GridViewContexts.RULE,
      showCheckboxColumn: bShowCheckboxColumn,
      enableImportExportButton: true
    };
    let calculatedAttributeConfigViewProps = oComponentProps.calculatedAttributeConfigView;
    let oPaginationDataForCalculatedAttributes = {
      from: calculatedAttributeConfigViewProps.getFrom(),
      size: calculatedAttributeConfigViewProps.getSize(),
      sortBy: calculatedAttributeConfigViewProps.getSortBy(),
      sortOrder: calculatedAttributeConfigViewProps.getSortOrder(),
      searchColumn: calculatedAttributeConfigViewProps.getSearchColumn(),
      searchText: calculatedAttributeConfigViewProps.getSearchText(),
      shouldAllowSelf: true,
      isFirstCall: calculatedAttributeConfigViewProps.getIsFirstCall()
    };
    let oAppData = this.getAppData();
    // var oRuleConfigProps = oComponentProps.ruleConfigView;
    let aRuleListDetailData = oActiveDetailRule.referencedRuleList;
    let oRightPanelData = {
      isRightPanelActive: oRuleConfigProps.getRightPanelVisibility(),
      rightBarSelectedIconMap: oRuleConfigProps.getRightBarIconClickMap(),
      listOfRuleListMap: oAppData.getListOfRuleList()
    };
    let aRuleEffect = oRuleConfigProps.getRuleViolationsAndNormalizationProps();

    let oMultiTaxonomyDataForRulesCause = {};
    let oMultiTaxonomyDataForRulesEffect = {};
    let aSelectedTaxonomyIds = oActiveDetailRule.taxonomies;
    if (!CS.isEmpty(oActiveDetailRule)) {
      let aSelectedTaxonomyIdsForEffect = [];
      let oNormalizationKlass = CS.find(oActiveDetailRule.normalizations, {type: 'taxonomy'});
      if (oNormalizationKlass) {
        aSelectedTaxonomyIdsForEffect = oNormalizationKlass.values;
      }
      var oAllowedTaxonomiesById = oRuleConfigProps.getAllowedTaxonomies();
      let oAllowedTaxonomyHierarchyList = oRuleConfigProps.getAllowedTaxonomyHierarchyList();
      var oReferencedTaxonomy = oActiveDetailRule.configDetails && oActiveDetailRule.configDetails.referencedTaxonomies;

      oMultiTaxonomyDataForRulesCause = oActiveDetailRule.isCreated ? null : SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomy, oAllowedTaxonomiesById, oAllowedTaxonomyHierarchyList);
      oMultiTaxonomyDataForRulesEffect = oActiveDetailRule.isCreated ? null : SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIdsForEffect, oReferencedTaxonomy, oAllowedTaxonomiesById, oAllowedTaxonomyHierarchyList);
    }

    let aAllowedAttributes = calculatedAttributeConfigViewProps.getAllowedAttributes();

    let oDataForCalculatedAttributes = {
      paginationData: oPaginationDataForCalculatedAttributes,
      allowedAttributes: aAllowedAttributes,
      extraData: {
        referencedAttributes: oComponentProps.screen.getReferencedAttributes()
      }
    };

    let aMockDataForKlassTypes = new MockDataForKlassTypes();

    let oRequestResponseInfo = {
      attributeClassList: {
        requestType: "customType",
        responsePath: ["success", "list"],
        requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetKlassesListByBaseType),
        customRequestModel: {
          types: CS.map(aMockDataForKlassTypes, "value")
        }
      }
    };
    let bIsRuleDirty = false;
    let aDataLanguages = oRuleConfigProps.getDataLanguages();
    let oRuleDetailViewData = {
      activeRule: oActiveDetailRule,
      ruleListDetailData: aRuleListDetailData,
      rightPanelData: oRightPanelData,
      ruleEffect: aRuleEffect,
      oCalculatedAttributeMapping: oAppData.getCalculatedAttributeMapping(),
      selectedKlassType: oRuleConfigProps.getKlassTypeId(),
      multiTaxonomyData: oMultiTaxonomyDataForRulesCause,
      multiTaxonomyDataForRulesEffect: oMultiTaxonomyDataForRulesEffect,
      dataForCalculatedAttributes: oDataForCalculatedAttributes,
      taxonomyPaginationData: oRuleConfigProps.getTaxonomyPaginationData(),
      physicalCatalogIdsData: MockDataForPhysicalCatalogAndPortal.physicalCatalogs(),
      portalIdsData: MockDataForPhysicalCatalogAndPortal.portals(),
      lazyMSSReqResInfo: oRequestResponseInfo,
      isRuleDirty: bIsRuleDirty,
      dataLanguages: aDataLanguages,
      entityType:'rule'
    };
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    return (<RuleConfigView gridViewProps={oGridViewProps}
                            isDialogOpen={bIsRuleDialogActive}
                            isRuleDirty={bIsActiveRuleDirty}
                            ruleDetailViewData={oRuleDetailViewData}
                            columnOrganizerData={oColumnOrganizerData}
  />);
  };

  getGoldenRecordRuleConfigView = () => {
    let oComponentProps = this.getComponentProps();
    let oGoldenRecordRuleConfigProps = oComponentProps.goldenRecordsView;
    let oActiveDetailRule = oGoldenRecordRuleConfigProps.getActiveGoldenRecordRule();
    let bIsActiveRuleDirty = false
    if (oActiveDetailRule.clonedObject) {
      oActiveDetailRule = oActiveDetailRule.clonedObject;
      bIsActiveRuleDirty = true;
    }
    let bIsRuleDialogActive = oGoldenRecordRuleConfigProps.getIsRuleDialogActive();
    var bShowCheckboxColumn = true;

    var oGridViewProps = {
      context: GridViewContexts.GOLDEN_RECORD_RULE,
      showCheckboxColumn: bShowCheckboxColumn,
      enableImportExportButton: true
    };

    var oAppData = this.getAppData();
    var oRightPanelData = {
      isRightPanelActive: oGoldenRecordRuleConfigProps.getRightPanelVisibility(),
      rightBarSelectedIconMap: oGoldenRecordRuleConfigProps.getRightBarIconClickMap(),
      listOfRuleListMap: oAppData.getListOfRuleList()
    };

    let oMultiTaxonomyDataForRulesCause = {};
    if (!CS.isEmpty(oActiveDetailRule)) {
      let aSelectedTaxonomyIds = oActiveDetailRule.taxonomyIds;
      var oAllowedTaxonomiesById = oGoldenRecordRuleConfigProps.getAllowedTaxonomies();
      let oAllowedTaxonomyHierarchyList = oGoldenRecordRuleConfigProps.getAllowedTaxonomyHierarchyList();
      var oReferencedTaxonomy = oActiveDetailRule.configDetails && oActiveDetailRule.configDetails.referencedTaxonomies;
      oMultiTaxonomyDataForRulesCause = oActiveDetailRule.isCreated ? null : SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomy, oAllowedTaxonomiesById, oAllowedTaxonomyHierarchyList);
    }

    let aMockDataForKlassTypes = new MockDataForKlassTypes();

    let oRequestResponseInfo = {
      attributeClassList: {
        requestType: "customType",
        responsePath: ["success", "list"],
        requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetKlassesListByBaseType),
        customRequestModel: {
          types: CS.map(aMockDataForKlassTypes, "value")
        }
      }
    };
    let bIsRuleDirty = false;
    let aPhysicalCatalogData = CS.reject(MockDataForPhysicalCatalogAndPortal.physicalCatalogs(), {id: "dataIntegration"});
    let oRuleDetailViewData = {
      activeRule: oActiveDetailRule,
      ruleListDetailData: [],
      rightPanelData:{oRightPanelData},
      multiTaxonomyData: oMultiTaxonomyDataForRulesCause,
      physicalCatalogIdsData: aPhysicalCatalogData,
      portalIdsData: MockDataForPhysicalCatalogAndPortal.portals(),
      lazyMSSReqResInfo: oRequestResponseInfo,
      isRuleDirty: bIsRuleDirty,
      entityType:"goldenRecordRule"
    };
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    return (<RuleConfigView gridViewProps={oGridViewProps}
                            isDialogOpen={bIsRuleDialogActive}
                            isRuleDirty={bIsActiveRuleDirty}
                            ruleDetailViewData={oRuleDetailViewData}
                            oManageEntityDialog={oManageEntityDialog}
                            columnOrganizerData={oColumnOrganizerData}
    />);
  };

  getKpiConfigView = () => {
    let oComponentProps = this.getComponentProps();
    let oKPIConfigProps = oComponentProps.kpiConfigView;
    let oActiveDetailKPI = oKPIConfigProps.getActiveKPI();
    let bIsActiveKPIDirty = false;
    if (oActiveDetailKPI.clonedObject) {
      oActiveDetailKPI = oActiveDetailKPI.clonedObject;
      bIsActiveKPIDirty = true;
    }
    let bIsKPIDialogActive = oKPIConfigProps.getIsKPIDialogActive();
    let oReferencedData = this.getKPIDetailData();
    var bShowCheckboxColumn = true;
    let sActiveTabId = oKPIConfigProps.getActiveTabId();
    let oActiveRuleBlock = oKPIConfigProps.getActiveBlock();
    oActiveRuleBlock = oActiveRuleBlock.clonedObject || oActiveRuleBlock;

    if (oActiveRuleBlock.type == "accuracyBlock" || oActiveRuleBlock.type == "conformityBlock") {
      this.fillAddedRuleConfigDetails(oActiveRuleBlock.rules);
    }


    let oAppData = SettingUtils.getAppData();
    let aAttributeList = oAppData.getAttributeList();

    let oAttributesToExclude = CS.filter(aAttributeList, function(oAttr) {
      if (!SettingUtils.isAttributeTypeMetadata(oAttr.type) && !SettingUtils.isAttributeTypeConcatenated(oAttr.type)) {
        return oAttr;
      }
    });

    let oRelationshipList = oAppData.getRelationshipMasterList();
    let aTagList = oAppData.getTagMap();

    var oReferencedDashboardTabs = oKPIConfigProps.getReferencedDashboardTabs();
    var aDashboardTabsListForMss = SettingUtils.getAppData().getDashboardTabsList();
    var oDashboardTabsMssData = {
      referencedData: oReferencedDashboardTabs,
      dashboardTabsList: aDashboardTabsListForMss
    };

    let aAttributeListForMSS = oAttributesToExclude || [];
    let oRelationshipListForMSS = oRelationshipList || {};
    let aTagListForMSS = aTagList || [];
    if (sActiveTabId === "uniquenessBlock") {
      aTagListForMSS = CS.filter(aTagListForMSS, {isMultiselect: false});
    }

    let oReferencedAttributes = oKPIConfigProps.getReferencedAttributes();
    let oReferencedTags = oKPIConfigProps.getReferencedTags();
    let oTaxonomyPaginationData = oKPIConfigProps.getTaxonomyPaginationData();
    let aPhysicalCatalogItems = MockDataForPhysicalCatalogAndPortal.physicalCatalogs();
    CS.remove(aPhysicalCatalogItems, {id: PhysicalCatalogDictionary.PIM_ARCHIVAL});
    let aPortalItems = MockDataForPhysicalCatalogAndPortal.portals();

    var oGridViewProps = {
      context: GridViewContexts.KPI,
      showCheckboxColumn: bShowCheckboxColumn,
      tableContextId: GridViewContexts.KPI,
    };

    let aMockDataForKlassTypes = new MockDataForKlassTypes();
    let oGetKlassListReqRes = {
      requestType: "customType",
      responsePath: ["success", "list"],
      requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetKlassesListByBaseType),
      customRequestModel: {
        types:  CS.map(aMockDataForKlassTypes, "value")
      }
    };

    let oLazyMSSReqResInfo = {
      targetKlasses: oGetKlassListReqRes,
      attributeClassList: oGetKlassListReqRes
    }

    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    return (<KpiConfigView gridViewProps={oGridViewProps}
                           referencedData={oReferencedData}
                           activeKPI={oActiveDetailKPI}
                           activeBlock={oActiveRuleBlock}
                           isDialogOpen={bIsKPIDialogActive}
                           activeTabId={sActiveTabId}
                           aAttributeListForMSS={aAttributeListForMSS}
                           oRelationshipListForMSS={oRelationshipListForMSS}
                           aTagListForMSS={aTagListForMSS}
                           oDashboardTabsDataForMss={oDashboardTabsMssData}
                           taxonomyPaginationData={oTaxonomyPaginationData}
                           referencedAttributes={oReferencedAttributes}
                           referencedTags={oReferencedTags}
                           physicalCatalogIdsData = {aPhysicalCatalogItems}
                           portalIdsData = {aPortalItems}
                           lazyMSSReqResInfo={oLazyMSSReqResInfo}
                           isKPIDirty={bIsActiveKPIDirty}
                           columnOrganizerData={oColumnOrganizerData}
    />);
  };

  getRuleListView = () => {
    var oComponentProps = this.getComponentProps();
    var oActionBarList = this.getAppData().getActionBarList();
    var oActionItemModel = {};
    var oRuleListConfigView = oActionBarList.RuleListConfigView;
    oActionItemModel.RuleListView = oRuleListConfigView.RuleListConfigListView;
    oActionItemModel.RuleListDetailedView = [];

    var oRuleListProps = oComponentProps.ruleListConfigView;
    let bIsRuleListDirty = false;
    if (oRuleListProps.getActiveRuleList().clonedObject) {
      bIsRuleListDirty = true;
    }
    var oActiveRuleList = oRuleListProps.getCurrentRuleList();
    var aListOfRuleListModel = this.getListOfRuleListViewModels();
    var bListItemCreated = oComponentProps.screen.getListItemCreationStatus();
    let bShowLoadMore = oRuleListProps.getShowLoadMore();
    let sSearchText = oRuleListProps.getSearchText();

    return <RuleListConfigView
        actionItemModel={oActionItemModel}
        listOfRuleListModel={aListOfRuleListModel}
        activeRuleList={oActiveRuleList}
        bRuleListCreated={bListItemCreated}
        isRuleListDirty={bIsRuleListDirty}
        showLoadMore={bShowLoadMore}
        searchText={sSearchText}
    />
  };

  getUserInformationView = () => {
    let oUserCloned = this.getComponentProps().UserConfigView.getSelectedUser();
    let oErrors = this.getComponentProps().UserConfigView.getErrorFields();
    let oUserProperties = {};
    oUserProperties.errors = oErrors;
    oUserProperties.changePasswordEnabled = true;
    let oUserModel = {
      id: oUserCloned.id,
      firstName: oUserCloned.firstName,
      lastName: oUserCloned.lastName,
      userName: oUserCloned.userName,
      password: oUserCloned.password,
      confirmPassword: oUserCloned.confirmPassword,
      emailId: oUserCloned.email,
      mobileNumber: oUserCloned.mobileNumber,
      gender: oUserCloned.gender,
      userImage: oUserCloned.icon ? SettingUtils.getIconUrl(oUserCloned.icon) : "",
      properties: oUserProperties,
    };

    let oSavedDetails = {
      firstName: oUserCloned.firstName,
      lastName: oUserCloned.lastName,
      email: oUserCloned.email,
    };

    let oContentStyle = {
      width: "74%",
      height: "74%",
      maxWidth: "none",
      maxHeight: "none",
      margin: 0,
    };

    let oBodyStyle = {
      padding: '0px',
      overflow: "hidden",
    };

    return <CustomDialogView open={true}
                             bodyStyle={oBodyStyle}
                             contentStyle={oContentStyle}
                             bodyClassName="userInformationViewModel"
                             contentClassName="userInformationView"
                             onRequestClose={this.getStore().handleCreateUserCancelClicked}>
      <UserProfileView model={oUserModel}
                       savedDetails={oSavedDetails}
                       createUser={true}
                       isUserInformationDirty={false}
                       bShowDefaultLanguageSection={false}/>
    </CustomDialogView>;
  };

  getUserView = () => {
    let oComponentProps = this.getComponentProps();
    let oUserProps = oComponentProps.UserConfigView;
    let bShowCheckboxColumn = true;

    let bShowPasswordChangeDialog = oUserProps.getChangePasswordEnabled();
    let oChangePasswordDOM = null;
    if (bShowPasswordChangeDialog) {
      oChangePasswordDOM = (
          <UserPasswordChangeView />
      );
    }

    let oSelectedUser = oUserProps.getSelectedUser();
    let oCreateUserDOM = null;
    if (oSelectedUser.isCreated) {
       oCreateUserDOM = this.getUserInformationView();
    }
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let aSelectedColumns = ColumnOrganizerProps.getSelectedOrganizedColumns();
    let bIsColumnOrganizerDialogOpen = ColumnOrganizerProps.getIsDialogOpen();

    return (
        <div className="configGridViewContainer" key="userConfigGridViewContainer">
          <ContextualGridView context={GridViewContexts.USER}
                    tableContextId={GridViewContexts.USER}
                    showCheckboxColumn={bShowCheckboxColumn}
                    disableDeleteButton={false}
                    enableImportExportButton={true}
                    enableManageEntityButton={true}
                    showGridColumnOrganiserButton={true}
                    columnOrganizerData={this.getColumnOrganizerData()}
                    selectedColumns={aSelectedColumns}
                    isColumnOrganizerDialogOpen={bIsColumnOrganizerDialogOpen}
          />
          {oChangePasswordDOM}
          {oCreateUserDOM}
          {oManageEntityDialog}
        </div>
    );
  }

  prepareUserToPush = (oUser) => {
    var sIcon = oUser.icon ? oUser.icon : "";
    return {
      id: oUser.id,
      label: oUser.firstName + " " + oUser.lastName, //todo: combine
      icon: sIcon
    };
  };

  getAvailableAndAddedUsersFromRole = (oRole) => {
    let oRoleProps = this.getComponentProps().roleConfigView;
    var aSelectedUserIds = [];
    var aItems = [];
    let fPrepareUserToPush = this.prepareUserToPush;

    var aAllowedUsers = oRoleProps.getAllowedEntitiesById("users");
    let oSelectedObjects = [];
    let oAddedUsers = {};

    CS.forEach(oRole.users, function (oUser) {
      let sUserId = oUser.id;
      aSelectedUserIds.push(sUserId);
      oSelectedObjects[oUser.id] = fPrepareUserToPush(oUser);
    });


    CS.forEach(aAllowedUsers, function (oUser) {
      if (!oAddedUsers[oUser.id]) {
        aItems.push(fPrepareUserToPush(oUser));
      }
    });

    return {selectedItems: aSelectedUserIds, items: aItems, selectedObjects: oSelectedObjects};
  }

  getAvailableAndAddedProfileFromRole = (oRole) => {
    let oRoleProps = this.getComponentProps().roleConfigView;
    var aAllowedProfiles = oRoleProps.getAllowedEntitiesById("endpoints");
    var oOnboardingProfileMaster = {};
    var oOffboardingProfileMaster = {};

    CS.forEach(aAllowedProfiles, function (oProfile) {
      let sId = oProfile.id;
      if (oProfile.endpointType == 'offboardingendpoint') {
        oOffboardingProfileMaster[sId] = oProfile;
      } else if (oProfile.endpointType == 'onboardingendpoint') {
        oOnboardingProfileMaster[sId] = oProfile;
      }
    });

    return {
      addedProfile: oRole.endpoints,
      availableOnBoardingProfile: oOnboardingProfileMaster,
      availableOffBoardingProfile: oOffboardingProfileMaster
    };

  };

  getPropertyCollectionListViewModels = (oPropertyCollectionValueList, aPropertyCollectionList) => {

    return CS.map(aPropertyCollectionList, function (oPropertyCollectionMaster) {
      var oPropertyCollectionValue = oPropertyCollectionValueList[oPropertyCollectionMaster.id];

      if (oPropertyCollectionMaster.clonedObject) {
        oPropertyCollectionMaster = oPropertyCollectionMaster.clonedObject;
      }

      var oProperties = {};
      oProperties.selectedScreenName = 'propertycollection';
      oProperties.showId = true;
      oProperties.code = oPropertyCollectionMaster.code;
      var bCheckboxVisibility = false;
      var bRightIconVisibility = false;
      var sRightIconClass = 'activeNodeIcon';

      if (oPropertyCollectionValue.isSelected) {
        bRightIconVisibility = true;
      }

      var sIcon = oPropertyCollectionMaster.iconKey ? SettingUtils.getIconUrl(oPropertyCollectionMaster.iconKey) : "";
      return new DraggableListViewModel(
          oPropertyCollectionValue.id,
          CS.getLabelOrCode(oPropertyCollectionMaster),
          oPropertyCollectionValue.isChecked,
          oPropertyCollectionValue.isSelected,
          oPropertyCollectionValue.isEditable,
          sIcon,
          bCheckboxVisibility,
          bRightIconVisibility,
          sRightIconClass,
          true,
          "dragFromAvailableList",
          "propertycollection",
          oProperties
      );
    });
  };

  getRoleListViewModels = (sScreenName) => {
    let searchText = this.getComponentProps().roleConfigView.getRoleSearchText();
    var oRoleValueList = CS.isEmpty(searchText) ? this.getComponentProps().roleConfigView.getRoleValuesList() :
                          this.getComponentProps().roleConfigView.getSearchedRoleValuesList();
    var oRoleMasterList = this.getAppData().getRoleList();
    var that = this;


    return CS.sortBy(CS.map(oRoleMasterList, function (oRoleMaster) {

      var oRoleValue = oRoleValueList[oRoleMaster.id];
      if (oRoleMaster.clonedObject) {
        oRoleMaster = oRoleMaster.clonedObject;
      }

      var oProperties = {};
      oProperties.selectedScreenName = sScreenName;
      oProperties.showId = true;
      oProperties.code = oRoleMaster.code;
      oProperties.deleteIconVisibility = true;
      oProperties.createCloneIconVisibility = true;
      oProperties.hideLeftIcon = true;
      var bCheckboxVisibility = false;
      var bRightIconVisibility = false;
      var sRightIconClass = 'activeNodeIcon';

      if (oRoleValue && oRoleValue.isSelected) {
        bRightIconVisibility = true;
      }

      var sIcon = oRoleMaster.icon ? SettingUtils.getIconUrl(oRoleMaster.icon) : "";
      if(CS.isNotEmpty(oRoleValue)) {
        return new DraggableListViewModel(
            oRoleValue.id,
            CS.getLabelOrCode(oRoleMaster),
            oRoleValue.isChecked,
            oRoleValue.isSelected,
            oRoleValue.isEditable,
            sIcon,
            bCheckboxVisibility,
            bRightIconVisibility,
            sRightIconClass,
            true,
            "dragFromAvailableList",
            "role",
            oProperties
        );
      }else{
        return false
      }
    }),'label');
  };

  getOrganisationConfigListViewModels = () => {
    let _this = this;
    let oOrganisationConfigViewProps = _this.getComponentProps().organisationConfigView;
    let oOrganisationConfigValueMap =  oOrganisationConfigViewProps.getOrganisationConfigValueMap();
    let oOrganisationConfigValueListMasterList =  oOrganisationConfigViewProps.getOrganisationConfigMap();

    return CS.map(oOrganisationConfigValueMap, function (oOrgConfigValue, sId) {
      let oOrgConfigMaster = CS.find(oOrganisationConfigValueListMasterList, {id: sId});
      oOrgConfigMaster = oOrgConfigMaster.clonedObject || oOrgConfigMaster;

      let oProperties = {};
      oProperties.selectedScreenName = 'organisation_config';
      oProperties.showId = true;
      oProperties.code = oOrgConfigMaster.code;
      let bCheckboxVisibility = false;
      let bRightIconVisibility = false;
      let sRightIconClass = 'activeNodeIcon';

      if (oOrgConfigValue.isSelected) {
        bRightIconVisibility = true;
      }
      oProperties.code = oOrgConfigMaster.code;
      oProperties.deleteIconVisibility = true;

      let sIcon = oOrgConfigMaster.iconKey ? SettingUtils.getIconUrl(oOrgConfigMaster.iconKey) : "";
      return new DraggableListViewModel(
          oOrgConfigMaster.id,
          CS.getLabelOrCode(oOrgConfigMaster),
          oOrgConfigValue.isChecked,
          oOrgConfigValue.isSelected,
          oOrgConfigValue.isEditable,
          sIcon,
          bCheckboxVisibility,
          bRightIconVisibility,
          sRightIconClass,
          true,
          "dragFromAvailableList",
          "organisation",
          oProperties
      );
    });
  };

  getListOfRuleListViewModels = () => {
    var _this = this;
    var oRuleListValueList = _this.getComponentProps().ruleListConfigView.getRuleListValuesList();
    var oMapOfMasterRuleList = _this.getAppData().getListOfRuleList();

    return CS.map(oRuleListValueList, function (oRuleListValue, sId) {
      var oMasterRuleList = CS.find(oMapOfMasterRuleList, {id: sId});
      if (oMasterRuleList.clonedObject) {
        oMasterRuleList = oMasterRuleList.clonedObject;
      }

      var oProperties = {};
      oProperties.selectedScreenName = 'ruleList';
      oProperties.showId = true;
      oProperties.code = oMasterRuleList.code;
      var bCheckboxVisibility = false;
      var bRightIconVisibility = false;
      var sRightIconClass = 'activeNodeIcon';

      if (oRuleListValue.isSelected) {
        bRightIconVisibility = true;
      }

      var sIcon = oMasterRuleList.icon ? SettingUtils.getIconUrl(oMasterRuleList.icon) : "";
      return new DraggableListViewModel(
          oMasterRuleList.id,
          CS.getLabelOrCode(oMasterRuleList),
          oRuleListValue.isChecked,
          oRuleListValue.isSelected,
          oRuleListValue.isEditable,
          sIcon,
          bCheckboxVisibility,
          bRightIconVisibility,
          sRightIconClass,
          true,
          "dragFromAvailableList",
          "ruleList",
          oProperties
      );
    });
  };

  getAllowedClassesInBasicInfoModel = (oActiveClass) => {
    if (oActiveClass.hasOwnProperty("clonedObject")) {
      oActiveClass = oActiveClass.clonedObject;
    }
    var aClasses = [];
    if (oActiveClass.type == MockDataForEntityBaseTypesDictionary.articleKlassBaseType) {
      aClasses = this.getAppData().getAllClassesFlatList();
    } else if (oActiveClass.type == MockDataForEntityBaseTypesDictionary.setKlassBaseType) {
      aClasses = this.getAppData().getAllSetClassesFlatList();
    } else if (oActiveClass.type == MockDataForEntityBaseTypesDictionary.taskKlassBaseType) {
      aClasses = this.getAppData().getAllTaskClassesFlatList();
    } else if (oActiveClass.type == MockDataForEntityBaseTypesDictionary.assetKlassBaseType) {
      aClasses = this.getAppData().getAllAssetClassesFlatList();
    } else if (oActiveClass.type == MockDataForEntityBaseTypesDictionary.marketKlassBaseType) {
      aClasses = this.getAppData().getAllMarketTargetClassesFlatList();
    }

    if (aClasses.length < 1) {
      return;
    }

    var aAllowedClassesSelectedList = oActiveClass.allowedTypes;

    var aDropDownListModelForClassesList = [];
    CS.each(aClasses, function (oItem) {
      aDropDownListModelForClassesList.push({id: oItem.id, label: CS.getLabelOrCode(oItem)});
    });

    return {
      disabled: false,
      label: getTranslation().ALLOWED_CLASSES,
      items: aDropDownListModelForClassesList,
      selectedItems: aAllowedClassesSelectedList,
      singleSelect: false,
      context: "allowedClasses"
    };

  };

  getAvailableActiveListModelForPropertyCollection = () => {
    var oPropertyCollectionConfigViewProps = this.getComponentProps().propertyCollectionConfigView;
    var oAvailablePropertyMap = oPropertyCollectionConfigViewProps.getAvailablePropertyList();
    var sSearchText = oPropertyCollectionConfigViewProps.getSectionAvailableElementSearchText().toLocaleLowerCase();
    var aAvailableActiveListModelForPropertyCollection = [];
    var oActivePropertyCollection = oPropertyCollectionConfigViewProps.getActivePropertyCollection();
    CS.forEach(oAvailablePropertyMap, function (oAvailablePropertyObject) {
      let sAttributeLabel = CS.getLabelOrCode(oAvailablePropertyObject);
      var sAttributeLabelLowerCase = sAttributeLabel.toLocaleLowerCase();
      var bIsValidInSearch = sSearchText == "" ? true : (sAttributeLabelLowerCase.indexOf(sSearchText) != -1);
      if (oActivePropertyCollection.type == MockDataForEntityBaseTypesDictionary.assetKlassBaseType && SettingUtils.isAttributeTypeCoverflow(oAttribute.type)) { // eslint-disable-line
        return;
      }

      if (oAvailablePropertyObject.isAvailable && bIsValidInSearch) {

        var oProperties = {};
        oProperties.selectedScreenName = 'propertyCollectionAttribute';
        oProperties.attributeData = oAvailablePropertyObject;
        oProperties.showId = true;
        oProperties.code = oAvailablePropertyObject.code;

        var checkboxVisibility = false;
        var bRightIconVisibility = true;
        var sRightIconClass = 'classAttributeListPlusIcon';

        aAvailableActiveListModelForPropertyCollection.push(new DraggableListViewModel(
            oAvailablePropertyObject.id,
            sAttributeLabel,
            null,
            null,
            null,
            '',
            checkboxVisibility,
            bRightIconVisibility,
            sRightIconClass,
            true,
            "dragFromPropertyCollectionAvailableList",
            "attribute",
            oProperties
        ));
      }
    });

    return aAvailableActiveListModelForPropertyCollection;
  };

  getPropertyCollectionMultiSelectModel = (oActivePropertyCollection, bIsForXRay) => {
    if (bIsForXRay) {
      return {
        isSelected: oActivePropertyCollection.isDefaultForXRay,
        context: "propertycollection"
      }
    }
  };

  getActivePropertyCollectionModel = (oActivePropertyCollection) => {
    var bIsForXRay = oActivePropertyCollection.isForXRay;
    var oMultiSelectModel = this.getPropertyCollectionMultiSelectModel(oActivePropertyCollection, bIsForXRay);
    var oAppData = this.getAppData();
    let oSelectedTab = oActivePropertyCollection.tab;

    let aCustomTabList = oAppData.getCustomTabList();
    let aSelectedTab = CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab];
    let aSelectedTabId= CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab.id];
    let oReqResObj = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.TABS,
      typesToExclude: [TemplateTabsDictionary.RENDITION_TAB, TemplateTabsDictionary.DUPLICATE_TAB]
    };

    let oReferencedData = {};
    if(!CS.isEmpty(oSelectedTab)) {
      oReferencedData[oSelectedTab.id] = oSelectedTab;
    }

    let oActivePropertyCollectionModel = {
      id: oActivePropertyCollection.id,
      label: oActivePropertyCollection.label,
      isDefaultForXRay: oMultiSelectModel,
      icon: {
        icon: oActivePropertyCollection.icon,
        context: "propertycollection",
        iconKey: oActivePropertyCollection.iconKey,
      },
      showSelectIconDialog: oActivePropertyCollection.showSelectIconDialog,
      selectIconData: SettingUtils.getIconLibraryData(),
      code: oActivePropertyCollection.code,
      isCreated: oActivePropertyCollection.isCreated,
    };

    if(!bIsForXRay) {
      oActivePropertyCollectionModel.tab = {
        context: 'tab',
        items: aCustomTabList,
        selectedItems: aSelectedTabId,
        selectedObjects: aSelectedTab,
        isMultiSelect: false,
        showCreateButton: true,
        isLoadMoreEnabled: true,
        searchText: SettingUtils.getEntitySearchText(),
        referencedData: oReferencedData,
        requestResponseInfo: oReqResObj,
        cannotRemove: true
      }
    }
    return oActivePropertyCollectionModel;
  };

  getPropertyCollectionView = () => {
    var oAppData = this.getAppData();
    var oActionBarList = oAppData.getActionBarList();
    var oActionItemModel = {};
    var oPropertyCollectionConfigView = CS.cloneDeep(oActionBarList.PropertyCollectionConfigView);
    var oComponentProps = this.getComponentProps();
    var oPropertyCollectionConfigViewProps = oComponentProps.propertyCollectionConfigView;
    var oActivePropertyCollection = oPropertyCollectionConfigViewProps.getActivePropertyCollection();
    var oPropertyCollectionTreeValuesListGeneric = oPropertyCollectionConfigViewProps.getPropertyCollectionValueList();
    var aPropertyCollectionListGeneric = oAppData.getPropertyCollectionList();
    var aClassListModelGeneric = this.getPropertyCollectionListViewModels(oPropertyCollectionTreeValuesListGeneric, aPropertyCollectionListGeneric);
    var bListItemCreated = oComponentProps.screen.getListItemCreationStatus();
    var oMasterEntitiesForSections = this.getAllMasterEntitiesForSections(oActivePropertyCollection);
    oMasterEntitiesForSections.allowedClassesMSSModel = this.getAllowedClassesInBasicInfoModel(oActivePropertyCollection);

    if (oActivePropertyCollection.id == '-1') {
      CS.remove(oPropertyCollectionConfigView.PropertyCollectionConfigListView, {id: 'save'});
      CS.remove(oPropertyCollectionConfigView.PropertyCollectionConfigListView, {id: 'delete_propertycollection'});
      CS.remove(oPropertyCollectionConfigView.PropertyCollectionConfigListView, {id: 'create_propertycollection'});
    }
    oActionItemModel.ClassListView = oPropertyCollectionConfigView.PropertyCollectionConfigListView;

    let aAvailableActiveListModel = this.getAvailableActiveListModelForPropertyCollection();
    var bIsAttributeTableVisible = false;
    let bIsPropertyCollectionDirty = false;
    let sSearchText = oComponentProps.screen.getEntitySearchText();
    let bShowLoadMore = oPropertyCollectionConfigViewProps.getShowLoadMore();
    if (!CS.isEmpty(oActivePropertyCollection) && oActivePropertyCollection.id != -1 && !oActivePropertyCollection.isCreated) {
      bIsAttributeTableVisible = true;
      if (oActivePropertyCollection.clonedObject) {
        oActivePropertyCollection = oActivePropertyCollection.clonedObject;
        bIsPropertyCollectionDirty = true;
      }
    }
    else if(oActivePropertyCollection.isCreated && oActivePropertyCollection.clonedObject){
        oActivePropertyCollection = oActivePropertyCollection.clonedObject;
        bIsPropertyCollectionDirty = true;
    }
    var oActiveItemModel = this.getActivePropertyCollectionModel(oActivePropertyCollection);
    let aSelectedProperties = oActivePropertyCollection.elements;

    let aTabList = PropertyCollectionTabLayoutData().tabList;
    let sPropertyCollectionListSearchText = oPropertyCollectionConfigViewProps.getSearchText();
    let sSelectedTabId = oPropertyCollectionConfigViewProps.getPropertyCollectionListActiveTabId();
    let oPropertyCollectionListTabData = {};
    oPropertyCollectionListTabData.selectedTabId = sSelectedTabId;
    oPropertyCollectionListTabData.tabList = aTabList;

    let aPropertyCollectionTabList = PropertyCollectionDraggableListTabsLayoutData().tabsList;
    let sPropertyCollectionDraggableListSelectedTabId = oPropertyCollectionConfigViewProps.getPropertyCollectionDraggableListActiveTabId();
    let oPropertyCollectionDraggableTabListData = {};
    oPropertyCollectionDraggableTabListData.selectedTabId = sPropertyCollectionDraggableListSelectedTabId;
    oPropertyCollectionDraggableTabListData.tabList = aPropertyCollectionTabList;
    let oPropertyCollectionToCreate = oPropertyCollectionConfigViewProps.getPropertyCollectionToCreate();
    let oActiveClass = CS.isNotEmpty(oPropertyCollectionToCreate) ? oPropertyCollectionToCreate : oActivePropertyCollection;
    let oTotalCounts = oComponentProps.screen.getEntitiesTotalCounts();
    let iTotalCount = oTotalCounts[sPropertyCollectionDraggableListSelectedTabId];
    let aActiveTabListMap = oPropertyCollectionConfigViewProps.getAvailablePropertyList();
    let bShowPropertyLoadMore = CS.size(aActiveTabListMap) !== iTotalCount;

    return <PropertyCollectionConfigView
        actionItemModel={oActionItemModel}
        activeItemModel={oActiveItemModel}
        classListModel={aClassListModelGeneric}
        activeClass={oActiveClass}
        bListItemCreated={bListItemCreated}
        availableActiveListModel={aAvailableActiveListModel}
        isAttributeTableVisible={bIsAttributeTableVisible}
        selectedPropertiesList={aSelectedProperties}
        context={"propertycollection"}
        propertyCollectionListSearchText={sPropertyCollectionListSearchText}
        isPropertyCollectionDirty={bIsPropertyCollectionDirty}
        showLoadMore={bShowLoadMore}
        searchText={sSearchText}
        propertyCollectionListTabData={oPropertyCollectionListTabData}
        propertyCollectionDraggableListTabsData={oPropertyCollectionDraggableTabListData}
        showPropertyLoadMore={bShowPropertyLoadMore}
    />;
  };

  getClassNatureTypeModel = (oActiveClass) => {
    var aSelectedItems = oActiveClass.natureType ? [oActiveClass.natureType] : [];
    return {
      disabled: true,
      label: "",
      items: SettingUtils.getNatureTypeListBasedOnClassType(oActiveClass.type,oActiveClass.secondaryType),
      selectedItems: aSelectedItems,
      singleSelect: true,
      context: "classConfigNatureType",
      disableCross: true,
      hideTooltip: true
    }
  };

  getSelectionToggleModel = (aItems, aSelectedIds, sContext, bIsSingleSelect, aDisabledItems) => {
    aSelectedIds = CS.isEmpty(aSelectedIds) ? [] : aSelectedIds;
    return {
      disabledItems: aDisabledItems || [],
      items: aItems,
      selectedItems: aSelectedIds,
      context: sContext,
      singleSelect: !!bIsSingleSelect,
    }
  };

  getActiveKlassContext = (oActiveClass) => {
    let sActiveKlassContext = "";
    switch (oActiveClass.natureType) {
      case NatureTypeDictionary.EMBEDDED:
        sActiveKlassContext = "embeddedKlassContext";
        break;
      case NatureTypeDictionary.TECHNICAL_IMAGE:
        sActiveKlassContext = "technicalImageContext";
        break;
      case NatureTypeDictionary.LANGUAGE:
        sActiveKlassContext = "languageKlassContext";
        break;
    }
    return sActiveKlassContext;
  };

  getClassContextDialogView = (oActiveClass) => {
    var oComponentProps = this.getComponentProps();
    var oClassConfigViewProps = oComponentProps.classConfigView;
    var oContextDialogProps = oClassConfigViewProps.getClassContextDialogProps();
    var oReferencedContexts = oClassConfigViewProps.getReferencedContexts();
    var sClassContextId = oClassConfigViewProps.getClassContextId();
    var sActiveTagUniqueSelectionId = oClassConfigViewProps.getActiveTagUniqueSelectionId();
    var aClassContextTagOrder = oClassConfigViewProps.getClassContextTagOrder();
    var sContext = this.getActiveKlassContext(oActiveClass);
    let aFieldsToExclude = oClassConfigViewProps.getFieldsToExcludeForContextDialog();

    let oTagMap = {};
    var aCustomTabList = this.getAppData().getCustomTabList();
    let {label, code} = oReferencedContexts[sClassContextId];
    var oClassContextData = {
      id: sClassContextId,
      label: label, //later change this on change in backend
      code: code
    };
    let oReferencedTags = oClassConfigViewProps.getReferencedTags();


    let aTagTypesToFetch = this.getTagTypesToFetchAccordingToContextType(oContextDialogProps.type);
    let oReqResObj = SettingUtils.getConfigDataLazyRequestResponseObjectByEntityName("tags", aTagTypesToFetch);
    let oIconLibraryData = SettingUtils.getIconLibraryData();


    return (<ClassContextDialogView
        context={sContext}
        classContextData={oClassContextData}
        classContextDialogData={oContextDialogProps}
        fieldsToExclude={aFieldsToExclude}
        tagMap={oTagMap}
        activeTagUniqueSelectionId={sActiveTagUniqueSelectionId}
        classContextTagOrder={aClassContextTagOrder}
        customTabList={aCustomTabList}
        referencedData={oReferencedTags}
        requestResponseInfo={oReqResObj}
        iconLibraryData={oIconLibraryData}
    />);
  };

  getDataTransferView = (oActiveClass, sSection) => {
    let sProperty = '';
    let sContext = '';
    let oModel = {};
    let bIsAddKlassEnabled = true;
    let bIsDeleteKlassEnabled = true;
    var oComponentProps = this.getComponentProps();
    let oClassReferencedObjects = oComponentProps.classConfigView.getReferencedClassObjects() || {};
    let oReferencedData = {
      referencedAttributes: oClassReferencedObjects.referencedAttributes,
      referencedTags: oClassReferencedObjects.referencedTags,
      referencedKlasses: oActiveClass.referencedKlasses
    };
    let oReqResObj = {
      requestType: "customType",
      responsePath: ["success", "list"],
      requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetLazyKlassListByNatureType),
      customRequestModel: {
        types: [],
        baseType: oActiveClass.type
      }
    };

    switch (sSection) {
      case "embeddedKlass":
      case "embeddedVariant":
        oReqResObj.customRequestModel.types = [NatureTypeDictionary.EMBEDDED];
        sProperty = "embeddedKlassIds";
        sContext = "embeddedKlass";
        oModel = {
          embeddedKlassDropdown: this.getLazyMSSViewModel(
              oActiveClass[sProperty],
              oActiveClass.referencedKlasses || {},
              "classConfigEmbeddedKlass" + SettingUtils.getSplitter() + sProperty + SettingUtils.getSplitter() + oActiveClass.id,
              oReqResObj
          )
        };
        break;

      case "gtinKlass":
        oReqResObj.customRequestModel.types = [NatureTypeDictionary.GTIN];
        sProperty = "gtinKlassId";
        sContext = sSection;
        oModel.gtinKlassDropdown = this.getLazyMSSViewModel(
            [oActiveClass[sProperty]],
            oClassReferencedObjects.referencedKlasses || {},
            "classConfigGtinKlass" + SettingUtils.getSplitter() + sProperty,
            oReqResObj,
            false
        );
        break;

      case "languageKlass":
        oReqResObj.customRequestModel.types = [NatureTypeDictionary.LANGUAGE];
        sProperty = "languageKlassId";
        sContext = sSection;
        oModel.languageKlassDropdown = this.getLazyMSSViewModel(
            [oActiveClass[sProperty]],
            oClassReferencedObjects.referencedKlasses || {},
            "classConfigLanguageKlass" + SettingUtils.getSplitter() + sProperty,
            oReqResObj,
            false
        );
        break;
    }

    let oView = <ClassConfigDataTransferView activeClass={oActiveClass}
                                             propertyMSSModel={oModel}
                                             property={sProperty}
                                             isDeleteKlassEnabled={bIsDeleteKlassEnabled}
                                             isAddKlassEnabled={bIsAddKlassEnabled}
                                             referencedData={oReferencedData}
                                             section={sSection}
                                             context="classConfig"
    />;

    return oView
  };

  getDownloadTrackerModel = (oActiveClass, sContextKey) => {
    let bTrackDownload = oActiveClass.trackDownloads;
    return {
      isSelected: bTrackDownload,
      isDisabled: false,
      context: sContextKey,
    }
  };

  getDetectDuplicateModel = (oActiveClass, sContextKey) => {
    let bDetectDuplicate = oActiveClass.detectDuplicate;
    return {
      isSelected: bDetectDuplicate,
      isDisabled: false,
      context: sContextKey,
    }
  };

  getUploadZipModel = (oActiveClass, sContextKey) => {
    let bUploadZip = oActiveClass.uploadZip;
    return {
      isSelected: bUploadZip,
      isDisabled: false,
      context: sContextKey,
    }
  };

  getIndesignServerModel = (oActiveClass, sContextKey) => {
    let bIndesignServer = oActiveClass.indesignServer;
    return {
      isSelected: bIndesignServer,
      isDisabled: false,
      context: sContextKey,
    }
  };

  getClassConfigContextViewForModel =(oActiveClass, sContextKey, aContextTypes, sRelationshipType, bIsSingleSelect) => {
    var oComponentProps = this.getComponentProps();
    var oContexts = oActiveClass.contexts || {};
    let ClassProps = oComponentProps.classConfigView;
    let oClassReferencedObject = ClassProps.getReferencedClassObjects();
    var oClassConfigViewProps = oComponentProps.classConfigView;
    let bIsClassDirty = oClassConfigViewProps.getActiveClass().isDirty || false;

    var oContextEnabledMap = ClassProps.getContextsEnabled();
    var oRelationshipModel = null;
    if (oContextEnabledMap[sContextKey]) {

      let oReqResObj = {
        requestType: "configData",
        entityName: ConfigDataEntitiesDictionary.VARIANT_CONTEXTS,
        types: aContextTypes
      };

      var oContextMSSViewModel = this.getLazyMSSViewModel(
          oContexts[sContextKey],
          oClassReferencedObject.referencedContexts,
          "classConfigVariantContext" + SettingUtils.getSplitter() + sContextKey,
          oReqResObj,
          false
      );

      if (sRelationshipType && !CS.isEmpty(oContexts[sContextKey]) && (sRelationshipType === RelationshipTypeDictionary.PRODUCT_VARIANT)) {
        oRelationshipModel = this.getRelationshipModelForClass(oActiveClass, sRelationshipType);
      }
    }

    return (<ClassConfigContextView
        contextMSSViewModel={oContextMSSViewModel}
        relationshipModel={oRelationshipModel}
        referencedData={ClassConfigProps.getReferencedClassObjects()}
        activeKlass={oActiveClass}
        isClassDirty={bIsClassDirty}
    />);
  };

  getClassConfigContextsModel = (oActiveClass, oModel) => {
    oModel.embeddedVariantContexts = this.getClassConfigContextViewForModel(oActiveClass, "embeddedVariantContexts",
        [ContextTypeDictionary.CONTEXTUAL_VARIANT]);

    if (oActiveClass.natureType == NatureTypeDictionary.PID_SKU) {
      oModel.productVariantContexts = this.getClassConfigContextViewForModel(oActiveClass, "productVariantContexts",
          [ContextTypeDictionary.PRODUCT_VARIANT], RelationshipTypeDictionary.PRODUCT_VARIANT, true);
    }
  };

  getClassConfigContextsModelForAsset = (oActiveClass, oModel) => {
    oModel.embeddedVariantContexts = this.getClassConfigContextViewForModel(oActiveClass, "embeddedVariantContexts",
        [ContextTypeDictionary.IMAGE_VARIANT, ContextTypeDictionary.CONTEXTUAL_VARIANT]);
  };

  getClassConfigContextsModelForTextAsset = (oActiveClass, oModel) => {
    oModel.embeddedVariantContexts = this.getClassConfigContextViewForModel(oActiveClass, "embeddedVariantContexts",
        [ContextTypeDictionary.IMAGE_VARIANT, ContextTypeDictionary.CONTEXTUAL_VARIANT]);
  };

  getAllowedSectionsDataForClassBasedOnType = (sClassNatureType, bIsNatureClass, sActiveClassId, sClassType, oActiveClassRelationship) => {

    var aAllowedSectionIds = ["defaultOptions", "icon", "label",
                              "lifeCycleStatusTags", "natureType", "noOfVersions", "previewImage", "tasks",
                              "activeClass", "gtinKlass", "classContext", "embeddedKlass", "languageKlass",
                              "technicalImageKlass",'code','detectDuplicate', "trackDownloads"];

    var aAllowedSectionIdsToRemove = [];
    switch (sClassNatureType){
      case NatureTypeDictionary.LANGUAGE:
        aAllowedSectionIdsToRemove = ["dataRules", "defaultOptions", "icon",
                                      "lifeCycleStatusTags", "previewImage", "gtinKlass",
                                      "embeddedKlass","technicalImageKlass", "tasks", "languageKlass",'detectDuplicate', "trackDownloads"];
        break;
      case NatureTypeDictionary.GTIN:
        aAllowedSectionIdsToRemove = ["dataRules", "defaultOptions", "icon",
                                      "lifeCycleStatusTags", "previewImage", "gtinKlass",
                                      "classContext", "embeddedKlass","technicalImageKlass", "tasks",'detectDuplicate', "trackDownloads"];
        break;

      case NatureTypeDictionary.TECHNICAL_IMAGE:
        aAllowedSectionIdsToRemove = ["dataRules", "defaultOptions", "icon",
                                      "lifeCycleStatusTags", "previewImage", "gtinKlass", "embeddedKlass",
                                      "technicalImageKlass", "tasks",'detectDuplicate'];
        break;

      case NatureTypeDictionary.EMBEDDED:
        aAllowedSectionIdsToRemove = ["dataRules", "defaultOptions", "icon",
                                      "lifeCycleStatusTags", "previewImage", "gtinKlass",
               "technicalImageKlass", "tasks",'detectDuplicate', "trackDownloads"];
        // track download only in case of assets
        if(sClassType !== EntityBaseTypeDictionary.assetKlassBaseType){
          aAllowedSectionIdsToRemove.push('trackDownloads')
        }
        break;

      case NatureTypeDictionary.IMAGE_ASSET:
      case NatureTypeDictionary.VIDEO_ASSET:
      case NatureTypeDictionary.DOCUMENT_ASSET:
      case NatureTypeDictionary.FILE_ASSET:
        aAllowedSectionIdsToRemove = ["gtinKlass", "classContext", 'detectDuplicate'];
        break;

      default:
        var sClassConfigScreen = ClassConfigProps.getSelectedClassCategory();
        switch (sClassConfigScreen){
          case "asset":
            aAllowedSectionIdsToRemove = ["gtinKlass", "classContext", "trackDownloads"];
            break;

          case "target":
            aAllowedSectionIdsToRemove = ["gtinKlass", "classContext", "technicalImageKlass", 'detectDuplicate', 'languageKlass', "trackDownloads"];
            break;

          case "textasset":
            aAllowedSectionIdsToRemove = ["gtinKlass", "classContext", "technicalImageKlass", 'detectDuplicate', "trackDownloads"];
            break;

            case "supplier":
                aAllowedSectionIdsToRemove = ["gtinKlass", "classContext", "technicalImageKlass", "defaultOptions", "embeddedKlass", "languageKlass" ,'detectDuplicate', "trackDownloads"];
                break;

          default:
            aAllowedSectionIdsToRemove = ["classContext", "technicalImageKlass",'detectDuplicate', "trackDownloads"];
            break;

        }
        break;
    }
    if(!bIsNatureClass){
      //hide from nonNature class
      aAllowedSectionIdsToRemove.push("natureType");
      aAllowedSectionIdsToRemove.push("technicalImageKlass");
      aAllowedSectionIdsToRemove.push("gtinKlass");
      aAllowedSectionIdsToRemove.push("noOfVersions");
      if (sActiveClassId == SystemLevelId.AssetId) {
        aAllowedSectionIds.push('uploadZip');
        aAllowedSectionIds.push('indesignServer');
      }
      if (sActiveClassId !== SystemLevelId.AssetId) {
        aAllowedSectionIdsToRemove.push("detectDuplicate");
        aAllowedSectionIdsToRemove.push("trackDownloads");
      }
      if (sActiveClassId === SystemLevelId.MarkerKlassId ||
          sActiveClassId === SystemLevelId.GoldenArticleKlassId) {
        aAllowedSectionIdsToRemove.push.apply(aAllowedSectionIdsToRemove, ["defaultOptions", "tasks", "embeddedKlass", "lifeCycleStatusTags"]);
      }
      aAllowedSectionIdsToRemove.push("languageKlass");
    }
    aAllowedSectionIds = CS.difference(aAllowedSectionIds, aAllowedSectionIdsToRemove);

    return aAllowedSectionIds;
  };

  getLazyMSSViewModel = (aSelectedItems, oReferencedData, sContextKey, oReqResObj, bIsMultiselect, bIsDisabled, bCannotRemove) => {
    let bIsMultiSelectTemp = bIsMultiselect == undefined || bIsMultiselect == null ? true : bIsMultiselect
    return {
      cannotRemove: bCannotRemove,
      disabled: bIsDisabled,
      label: "",
      selectedItems: aSelectedItems,
      context: sContextKey,
      disableCross: false,
      hideTooltip: true,
      isMultiSelect: bIsMultiSelectTemp,
      referencedData: oReferencedData,
      requestResponseInfo: oReqResObj
    }
  }

  getClassConfigSectionLayoutModel = (oActiveClass) => {
    var _this = this;
    var oComponentProps = this.getComponentProps();
    var oClassConfigViewProps = oComponentProps.classConfigView;
    var aRelationshipKlasses = [];
    var bIsNatureClass = oActiveClass.isNature;
    var aAllowedSectionIds = this.getAllowedSectionsDataForClassBasedOnType(oActiveClass.natureType, bIsNatureClass, oActiveClass.id, oActiveClass.type, oActiveClass.relationships);
    var aAppliedDefaultOptions = [];
    var oModel = {
      icon : {icon: null, context: "hideImage"},
      previewImage : {icon: null, context: "hideImage"}
    };
    let oClassReferencedObjects = oClassConfigViewProps.getReferencedClassObjects() || {};
    if (oActiveClass.isAbstract) {
      aAppliedDefaultOptions.push("abstract");
    }

    CS.forEach(aAllowedSectionIds, (sSectionId)=>{
      let oReqResObj = {};

      switch(sSectionId){

        case 'defaultOptions':
          var aDefaultOptions = [{id: "abstract", label: "Abstract"}];
          if (oActiveClass.isNature) {
            aDefaultOptions.push({id: "default", label: "Default"});
            if (oActiveClass.isDefaultChild) {
              aAppliedDefaultOptions.push("default");
            }
          }
          oModel.defaultOptions = _this.getSelectionToggleModel(aDefaultOptions, aAppliedDefaultOptions, "class");
          break;

        case 'icon':
          oModel.icon = {icon: oActiveClass.icon, context: "class", iconKey: oActiveClass.iconKey};
          oModel.showSelectIconDialog = oActiveClass.showSelectIconDialog;
          oModel.selectIconData = SettingUtils.getIconLibraryData();
          break;

        case 'label':
          oModel.label = oActiveClass.label;
          break;

        case 'code' :
          oModel.code = oActiveClass.code;
          break;

        case 'lifeCycleStatusTags':
          //Provide types in request object model
          oReqResObj = {
            requestType: "configData",
            entityName:  ConfigDataEntitiesDictionary.TAGS,
            types: ["tag_type_lifecycle_status","tag_type_listing_status"]
          };
          oModel.lifeCycleStatusTags = _this.getLazyMSSViewModel(
              oActiveClass.lifeCycleStatusTags,
              oClassReferencedObjects.referencedTags,
              "classConfigLifeCycleStatusTags" + SettingUtils.getSplitter() + "lifeCycleStatusTags",
              oReqResObj
          );
          break;

        case 'natureType':
          oModel.natureType = _this.getClassNatureTypeModel(oActiveClass);
          break;

        case 'noOfVersions':
          oModel.numberOfVersionsToMaintain = oActiveClass.numberOfVersionsToMaintain;
          break;

        case 'previewImage':
          oModel.previewImage = {icon: (oActiveClass.previewImage || ""), context: "previewImage"};
          break;

        case 'tasks':
          oReqResObj = {
            requestType: "configData",
            entityName: ConfigDataEntitiesDictionary.TASKS,
          };
          oModel.tasks = _this.getLazyMSSViewModel(
              oActiveClass.tasks,
              oClassReferencedObjects.referencedTasks,
              "classConfigTasks" + SettingUtils.getSplitter() + "tasks",
              oReqResObj
          );
          break;
        case 'activeClass':
          oModel.activeClass = oActiveClass;
          break;

        case 'gtinKlass':
          oModel.gtinKlass = _this.getDataTransferView(oActiveClass, "gtinKlass");
         break;

        case 'classContext':
          oModel.classContext = oActiveClass.isCreated ? null : _this.getClassContextDialogView(oActiveClass);
          break;

        case 'embeddedKlass':
          oModel.embeddedKlass = _this.getDataTransferView(oActiveClass, "embeddedKlass");
          break;

        case 'embeddedVariant':
          oModel.embeddedVariant = _this.getDataTransferView(oActiveClass, "embeddedVariant");
          break;

        case 'languageKlass':
          oModel.languageKlass = _this.getDataTransferView(oActiveClass, "languageKlass");
          break;

        case 'technicalImageKlass':
          //since, behaviour is same for Embedded Klass & Technical Image Klass
          var aSelectedTechnicalImageKlasses = CS.cloneDeep(oActiveClass.technicalImageKlassIds) || [];
          let sURL = RequestMapping.getRequestUrl(oClassRequestMapping.GetLazyKlassListByNatureType);
          let aTypes = [NatureTypeDictionary.TECHNICAL_IMAGE];
          oModel.technicalImageKlass = _this.getLazyMSSViewModel(
              aSelectedTechnicalImageKlasses,
              oClassReferencedObjects.referencedKlasses,
              "technicalImageKlass" + SettingUtils.getSplitter() + oActiveClass.id,
              _this.getLazyMSSCustomRequestResponseInfo(sURL, aTypes)
          );
          break;

        case 'taxonomyTransfer':
          let oReferencedTaxonomies = oClassReferencedObjects.referencedTaxonomies;
          oReqResObj = {
            requestType: "customType",
            responsePath: ["success", "list"],
            requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetTaxonomy),
            entityName: 'taxonomies',
            customRequestModel: {
              taxonomyId: -1
            }
          };
          oModel.taxonomyTransfer = {
            selectedItems: oActiveClass.taxonomyIds,
            context: 'classConfigTaxonomyTransfer' + SettingUtils.getSplitter() + 'taxonomyIds',
            referencedData: oReferencedTaxonomies,
            requestResponseInfo: oReqResObj,
            isMultiSelect: true
          };
          break;

        case 'detectDuplicate':
          oModel.detectDuplicate = _this.getDetectDuplicateModel(oActiveClass, "class");
          break;

        case 'uploadZip':
          oModel.uploadZip = _this.getUploadZipModel(oActiveClass, "class");
          break;

        case 'indesignServer':
          oModel.indesignServer = _this.getIndesignServerModel(oActiveClass, "class");
          break;

        case "marketKlassTags":
          if (!oActiveClass["referencedKlasses"]) {
            oActiveClass["referencedKlasses"] = oClassReferencedObjects.referencedKlasses;
          }

          let oReferencedData = {
            referencedKlasses: oActiveClass.referencedKlasses,
            referencedTags: oClassReferencedObjects.referencedTags,
          };

          oReqResObj = {
            requestType: "customType",
            responsePath: ["success", "list"],
            requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetLazyKlassListByNatureType),
            customRequestModel: {
              types: [NatureTypeDictionary.MARKET],
              baseType: EntityBaseTypeDictionary.marketKlassBaseType
            }
          };

          let sProperty = "marketKlassIds";
          let sContext = "marketKlassTags";
          let bIsAddKlassEnabled = true;
          let bIsDeleteKlassEnabled = true;

          let oPropSSModel = {
            requestType: "customType",
            responsePath: ["success", "list"],
            requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetLazyKlassListByNatureType),
            marketKlassDropdown: this.getLazyMSSViewModel(
                oActiveClass[sProperty],
                oReferencedData.referencedKlasses || {},
                "dtpClassConfigMarketKlass" + SettingUtils.getSplitter() + sProperty + SettingUtils.getSplitter() + oActiveClass.id,
                oReqResObj
            )
          };


          oModel.marketKlassTags = (<ClassConfigTagTransferView activeClass={oActiveClass}
                                                                propertyMSSModel={oPropSSModel}
                                                                property={sProperty}
                                                                isDeleteKlassEnabled={bIsAddKlassEnabled}
                                                                isAddKlassEnabled={bIsDeleteKlassEnabled}
                                                                referencedData={oReferencedData}
                                                                section={sContext}
                                                                context="classConfig"/>);

          break;

        case "trackDownloads":
          oModel.trackDownloads = _this.getDownloadTrackerModel(oActiveClass, "class");
            break;
        case "color":
          oModel.color = {
            color: oActiveClass.color,
            context: "class"
          };
          break;
      }


    });

    let bIsClassDirty = oClassConfigViewProps.getActiveClass().isDirty || false;


    //TODO: Shitty code below, need to refactor it & show sections based on "oAllowedSectionsData"
    if (oActiveClass.type == MockDataForEntityBaseTypesDictionary.articleKlassBaseType) {
      if(oActiveClass.natureType != NatureTypeDictionary.SINGLE_ARTICLE){
        var oNatureRelationshipModel = oActiveClass.natureType ? this.getRelationshipModelForClass(oActiveClass, null) : {};
        if (!CS.isEmpty(oNatureRelationshipModel)) {
          oModel.natureRelationship = (<ClassConfigRelationshipView
              relationshipLayoutModel={oNatureRelationshipModel}
              referencedData={ClassConfigProps.getReferencedClassObjects()}
              activeKlass={oActiveClass}
              isEntityDirty={bIsClassDirty}
          />);
        }
      }

      if(oActiveClass.natureType != NatureTypeDictionary.GTIN &&
          oActiveClass.natureType != NatureTypeDictionary.EMBEDDED &&
          oActiveClass.natureType !== NatureTypeDictionary.LANGUAGE &&
          oActiveClass.natureType != NatureTypeDictionary.TECHNICAL_IMAGE){
        this.getClassConfigContextsModel(oActiveClass, oModel);
      }
    }
   if (oActiveClass.type === MockDataForEntityBaseTypesDictionary.assetKlassBaseType) { //Asset Type
      this.getClassConfigContextsModelForAsset(oActiveClass, oModel);
    } else if (oActiveClass.type == MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType && oActiveClass.natureType) {
      this.getClassConfigContextsModelForTextAsset(oActiveClass, oModel);
    }
    return oModel;
  };

  getRelationshipModelFromRelationship = (oActiveClass,oSelectedRelationship,sRelationshipType) =>{
    let oAppData = this.getAppData();
    let oReferencedClassObjects = ClassConfigProps.getReferencedClassObjects();
    let oReferencedKlasses = oReferencedClassObjects.referencedKlasses;
    let oReferencedTabs = oReferencedClassObjects.referencedTabs;
      let bIsContextRelationship = CS.includes([RelationshipTypeDictionary.PRODUCT_VARIANT, RelationshipTypeDictionary.PROMOTIONAL_VERSION], oSelectedRelationship.relationshipType);
      if (!sRelationshipType) {
        if (bIsContextRelationship) {
          return;
        }
      } else {
        if (oSelectedRelationship.relationshipType != sRelationshipType) {
          return;
        }
      }

      let oSectionsView = null;
      let sSide1Value = oSelectedRelationship.side1.klassId;
      let sSide2Value = oSelectedRelationship.side2.klassId;
      let sSide1LabelForExtension = !CS.isEmpty(oReferencedKlasses[sSide1Value]) ? CS.getLabelOrCode(oReferencedKlasses[sSide1Value]) : "";
      let sSide2LabelForExtension = !CS.isEmpty(oReferencedKlasses[sSide2Value]) ? CS.getLabelOrCode(oReferencedKlasses[sSide2Value]) : "";

      let sSelectedTabId = "";
      let oSelectedTab = {};
      let aCustomTabList = oAppData.getCustomTabList();
      if (!CS.isEmpty(oSelectedRelationship.tabId)) {
        sSelectedTabId = oSelectedRelationship.tabId;
        oSelectedTab = oReferencedTabs[sSelectedTabId] || CS.find(aCustomTabList, {id: sSelectedTabId});
      }let aSelectedTab = CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab];
      let aSelectedTabId= CS.isEmpty(oSelectedTab) ? [] : [sSelectedTabId];
      let sSplitter = SettingUtils.getSplitter();
      let oReqResObj = {
        requestType: "configData",
        entityName:  ConfigDataEntitiesDictionary.TABS,
        typesToExclude: [Constants.TAB_DUPLICATE_ASSETS, Constants.TAB_RENDITION]
      };
      let bHideOneSide = false;
      let sShownSide = null;
      let oReferencedData = {};
      if(!CS.isEmpty(oSelectedTab) && (oSelectedTab.label || oSelectedTab.code)) {
        oReferencedData[oSelectedTab.id] = oSelectedTab;
      }
      let bIsRelationshipTypeProductVariant = sRelationshipType === RelationshipTypeDictionary.PRODUCT_VARIANT;
    let oRelationshipModel = {
        relationshipLabel: CS.getLabelOrCode(oSelectedRelationship),
        sections: oSectionsView,
        relationshipType: oSelectedRelationship.relationshipType,
        id: oSelectedRelationship.id,
        code: oSelectedRelationship.code,
        tab: {
          context:"tab" +  sSplitter + "class_relationship" + sSplitter + oSelectedRelationship.id +"",
          items: aCustomTabList,
          selectedItems: aSelectedTabId,
          selectedObjects: aSelectedTab,
          isMultiSelect: false,
          showCreateButton: true,
          isLoadMoreEnabled: true,
          searchText: SettingUtils.getEntitySearchText(),
          referencedData: oReferencedData,
          requestResponseInfo: oReqResObj,
          disabled: bIsRelationshipTypeProductVariant
        },
        taxonomyInheritanceSetting: {
          context: "taxonomyInheritanceSetting",
          items: MockDataForTaxonomyInheritanceList(),
          selectedItems: [oSelectedRelationship.taxonomyInheritanceSetting],
          isMultiSelect: false,
          cannotRemove: true
        }
    };
    if (oSelectedRelationship.side1.klassId !== oSelectedRelationship.side2.klassId) {
      oRelationshipModel.dataTransferView = (<DataTransferView
          relationshipId={oSelectedRelationship.id}
          hideOneSide={bHideOneSide}
          sShownSide={sShownSide}
          side1={oSelectedRelationship.side1}
          side1Label={sSide1LabelForExtension}
          side2={oSelectedRelationship.side2}
          side2Label={sSide2LabelForExtension}
          parentContext={"klass"}
          referencedAttributes={oReferencedClassObjects.referencedAttributes}
          referencedTags={oReferencedClassObjects.referencedTags}
          relationshipType={oSelectedRelationship.relationshipType}
      />);
    }
    var oScreenProps = this.getComponentProps().screen;
    let oRelationshipInheritanceMap = oScreenProps.getReferencedRelationships();

    /** sSide2Value : other side must be present before inheriting relationship
     */
    if (sSide2Value && oReferencedKlasses[sSide2Value]) {
      let oRelationshipInheritanceData = {
        referencedData: oReferencedClassObjects.referencedRelationships,
        inheritanceMap: oRelationshipInheritanceMap,
        requestURL: oClassRequestMapping.getRelationshipInheritance,
        context: ConfigDataEntitiesDictionary.RELATIONSHIPS
      };

      oRelationshipModel.relationshipTransferView = (<RelationshipTransferView
          id={oSelectedRelationship.id}
          side={oSelectedRelationship.side1}
          side1Label={sSide1LabelForExtension}
          side2Label={sSide2LabelForExtension}
          parentContext={"klass"}
          relationshipType= {oSelectedRelationship.relationshipType}
          relationshipInheritanceData={oRelationshipInheritanceData}
          canAddReference={bIsRelationshipTypeProductVariant}
      />);
    }

    if (!bIsContextRelationship) {
      oRelationshipModel.maxNoOfItems = oSelectedRelationship.maxNoOfItems || 0;
    }
    return oRelationshipModel;
  };

  getRelationshipModelForClass = (oActiveClass, sRelationshipType) => {
    let oRelationshipModel = {};
    CS.forEach(oActiveClass.relationships, (oSelectedRelationship) => {
      oRelationshipModel = this.getRelationshipModelFromRelationship(oActiveClass, oSelectedRelationship, sRelationshipType);
      if (!CS.isEmpty(oRelationshipModel)) {
        return false;
      }
    });
    return oRelationshipModel;
  };


  getAssetUploadConfigurationModel = (oActiveClass) => {
    let oCurrentDialogConfigurationModel = null;
    //======================For Edit Or Create Upload Config===================
    if (CS.isNotEmpty(oActiveClass.currentAssetUploadConfigurationModel)) {
      let oCurrentAssetUploadConfigurationModel = oActiveClass.currentAssetUploadConfigurationModel;

      let aButtonData = [
        {
          id: "cancel",
          label: getTranslation().CANCEL,
          isDisabled: false,
          isFlat: true,
        },
        {
          id: "create",
          label: oCurrentAssetUploadConfigurationModel.isCreated ? getTranslation().CREATE : getTranslation().SAVE,
          isFlat: false,
        }
      ];

      let oDialogViewModel = {
        buttonData: aButtonData,
        modal: true,
        open: true,
        title: getTranslation().EXTENSION,
        bodyStyle: {padding: '0 10px 20px 10px'},
      };

      let sViewContext = "classAssetUploadConfig";

      let oSectionData = {
        extractMetadata: {
          isSelected: oCurrentAssetUploadConfigurationModel.extractMetadata,
          context: sViewContext,
        },
        extractRendition: {
          isSelected: oCurrentAssetUploadConfigurationModel.extractRendition,
          context: sViewContext,
        },
        extension: oCurrentAssetUploadConfigurationModel.extension
      };

      oCurrentDialogConfigurationModel = {
        contentClassName: "createClassModalDialog",
        sectionLayout: new CreateDialogLayoutData(), data: oSectionData,
        context: "classAssetUploadConfig"
      };

      oCurrentDialogConfigurationModel = {
        currentDialogConfigurationModel: oCurrentDialogConfigurationModel,
        dialogViewModel: oDialogViewModel
      };
    }
    //=========================================================
    return {
      fields: oActiveClass.extensionConfiguration ? oActiveClass.extensionConfiguration : null,
      uploadConfigurationLabel: oActiveClass.label,
      currentDialogConfigurationModel: oCurrentDialogConfigurationModel
    }
  };

  /* LazyMss Data for Export Side 2 Relationship */
  getExportRelationshipLazyMssData = (sContext, oActiveClass) =>{
    let oLazyMssData = {};
    let oEntities = {};
    let oData = {
      types: [],
      typesToExclude: []
    };
    oEntities[sContext] = oData;
    let oRequestData = {
      requestType: "customType",
      responsePath: ["success", sContext , "list"],
      requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetSectionConfigDataForRelationshipExport),
      customRequestModel: {
        klassId: oActiveClass.id,
        entities: oEntities
      }
    };
    oLazyMssData[sContext] = oRequestData;
    return oLazyMssData;
  };

  /* Tab Summary Data for Export Side 2 Relationship */
  getTabSummaryData = (sContext, sEntity, oReqInfo, oClassConfigViewProps, oRelationshipExport) => {
    let oReferencedData = oRelationshipExport.referencedData || {};
    let oReferencedDetails = {};
    let oTabSummaryData = {};
    let sTabId = "";
    switch (sContext) {
      case ConfigDataEntitiesDictionary.ATTRIBUTES :
        oReferencedDetails.referencedAttributes = oReferencedData.referencedAttributes;
        sTabId = MockDataForMapSummaryHeader.attribute;
        oTabSummaryData.selectedAttributes = oRelationshipExport.attributes;
        break;
      case ConfigDataEntitiesDictionary.TAGS :
        oReferencedDetails.referencedTags = oReferencedData.referencedTags;
        sTabId = MockDataForMapSummaryHeader.tag;
        oTabSummaryData.selectedTags = oRelationshipExport.tags;
        break;
      case ConfigDataEntitiesDictionary.RELATIONSHIPS :
        oReferencedDetails.referencedRelationships = oReferencedData.referencedRelationships;
        sTabId = MockDataForMapSummaryHeader.relationship;
        oTabSummaryData.selectedRelationships = oRelationshipExport.relationships;
        break;
    }

    let oData  = {
      summaryType: sContext,
      selectedTabId: sEntity,
      tabHeaderData: MockDataForHeaderExportSide2Relationship(),
      referencedData: oReferencedDetails,
      lazyMSSReqResInfo: oReqInfo,
      tabId: sTabId
    };

    CS.assign(oTabSummaryData,oData);

    return oTabSummaryData;
  }

  /* Relationships Data preparation for Export Side 2 Relationship*/
  getRelationshipsData = (oActiveClass, oClassConfigViewProps, oRelationshipExport = {}) => {
    let aData = [];
    let sContext = ConfigDataEntitiesDictionary.RELATIONSHIPS;
    let sEntity = ExportSide2RelationshipDictionary.RELATIONSHIP_LIST;
    let oReqInfo = this.getExportRelationshipLazyMssData(sContext, oActiveClass);
    let oTabSummaryData = this.getTabSummaryData(sContext, sEntity, oReqInfo, oClassConfigViewProps, oRelationshipExport);
    aData.push(oTabSummaryData);
    return aData;
  };

  /* Properties Data preparation for Export Side 2 Relationship*/
  getPropertiesData = (oActiveClass, oClassConfigViewProps, oRelationshipExport = {}) => {
    let _this = this;
    let aData = [];
    let aContext = [ConfigDataEntitiesDictionary.ATTRIBUTES,ConfigDataEntitiesDictionary.TAGS];
    let sEntity = ExportSide2RelationshipDictionary.PROPERTIES_LIST;
    CS.forEach(aContext, function (sContext) {
      let oReqInfo = _this.getExportRelationshipLazyMssData(sContext, oActiveClass);
      let oTabSummaryData = _this.getTabSummaryData(sContext, sEntity, oReqInfo, oClassConfigViewProps, oRelationshipExport);
      aData.push(oTabSummaryData);
    });
    return aData;
  };

  getClassView = () => {
    let oAppData = this.getAppData();
    let oComponentProps = this.getComponentProps();
    let oClassConfigViewProps = oComponentProps.classConfigView;
    let sContext = oClassConfigViewProps.getSelectedClassCategory();
    let oActiveClass = oClassConfigViewProps.getActiveClass();
    let oMasterEntitiesForSections = this.getAllMasterEntitiesForSections(oActiveClass);
    let oReferencedObject = ClassConfigProps.getReferencedClassObjects();
    let aReferencedContexts = oReferencedObject.referencedContexts;

    oMasterEntitiesForSections.allowedClassesMSSModel = this.getAllowedClassesInBasicInfoModel(oActiveClass);

    let oDragDetails = oClassConfigViewProps.getClassSectionDragStatus();

    let aSectionListModel = this.getAvailableSectionListModelForClass();
    let bIsAttributeTableVisible = false;
    let bIsActiveClassDirty = false;
    if (!CS.isEmpty(oActiveClass) && oActiveClass.id != -1 && !oActiveClass.isCreated) {
      bIsAttributeTableVisible = true;
      if (oActiveClass.clonedObject) {
        bIsActiveClassDirty = true;
      }
      oActiveClass = oActiveClass.clonedObject || oActiveClass;
    }
    let aDisabledFields = ["code"];
    let oClassSectionLayoutModel = this.getClassConfigSectionLayoutModel(oActiveClass);
    let oAssetUploadConfigurationModel =  this.getAssetUploadConfigurationModel(oActiveClass);

    let oClassTreeValuesListGeneric = oClassConfigViewProps.getClassValueListByTypeGeneric(sContext);
    let aClassListGeneric = oAppData.getClassListByTypeGeneric(sContext);
    var oScreenProps = this.getComponentProps().screen;
    var bScrollAutomatically = oScreenProps.getIsHierarchyTreeScrollAutomaticallyEnabled();
    let oContentHierarchyData = {
      oHierarchyTreeVisualProps: oClassTreeValuesListGeneric,
      oHierarchyTree: aClassListGeneric[0],
      hierarchyNodeContextMenuItemList: [],
      selectedContext: "classConfig",
      scrollAutomatically: bScrollAutomatically
    };

    let oCommonConfigSectionViewModel = {
      label: "",
      isNature: null,
      natureType: null,
      code: ""
    };
    let sSplitter = SettingUtils.getSplitter();
    oCommonConfigSectionViewModel.label = oActiveClass.label;

    oCommonConfigSectionViewModel.isNature = {
      isSelected: oActiveClass.isNature,
      context: "class",
      isDisabled: false,
    };
    oCommonConfigSectionViewModel.code = oActiveClass.code || "";
    let aItems = SettingUtils.getNatureTypeListBasedOnClassType(oActiveClass.type, oActiveClass.secondaryType);

    let aSelectedItems = [oActiveClass.natureType];
    if (oActiveClass.isNature) {
      aItems = CS.sortBy(aItems, function(oItem) {
        return oItem.label;
      });

      oCommonConfigSectionViewModel.natureType = {
        items: aItems,
        selectedItems: aSelectedItems,
        cannotRemove: true,
        context: "class" + sSplitter + "natureType",
        disabled: false,
        label: "",
        isMultiSelect: false,
      }
    }

    let aActionItems = [];
    let oActionBarList = oAppData.getActionBarList();
    aActionItems = CS.cloneDeep(oActionBarList.ClassConfigView.ClassConfigListView);

    /** Temporarily hidding [import/export] buttons due to bug PXPFDEV-9400 we will unhide the buttons after it is fixed **/

    /* Data for Export Side 2 Relationship*/
    let oSide2CollapseEnabled = ClassConfigProps.getSide2RelationshipCollapseEnabled();
    let oRelationshipExport = CS.isEmpty(oActiveClass.clonedObject) ? oActiveClass.relationshipExport : oActiveClass.clonedObject.relationshipExport;
    let oRelationshipDataForExport = this.getRelationshipsData(oActiveClass, oClassConfigViewProps,oRelationshipExport);
    let oPropertiesDataForExport = this.getPropertiesData(oActiveClass, oClassConfigViewProps,oRelationshipExport);
    let oSide2RelationshipData = {
      relationships : oRelationshipDataForExport,
      properties : oPropertiesDataForExport
    };

    if (!CS.isEmpty(oSide2RelationshipData.relationships)) {
      let aTabsummaryForRelationships = oSide2RelationshipData.relationships;
      let oTabsummaryForRelationships = CS.find(aTabsummaryForRelationships, {summaryType: ConfigDataEntitiesDictionary.RELATIONSHIPS});
      if (!CS.isEmpty(oTabsummaryForRelationships)) {
        oTabsummaryForRelationships.selectedRelationships = !CS.isEmpty(oRelationshipExport) ? oRelationshipExport.relationships : [];
        oTabsummaryForRelationships.referencedData.referencedRelationships = !CS.isEmpty(oRelationshipExport) ? oRelationshipExport.referencedData.referencedRelationships : {};
      }
    }
    if(!CS.isEmpty(oSide2RelationshipData.properties)){
      let aTabsummaryForProperties = oSide2RelationshipData.properties;
      let oTabsummaryForAttributes = CS.find(aTabsummaryForProperties, {summaryType: ConfigDataEntitiesDictionary.ATTRIBUTES});
      let oTabsummaryForTags = CS.find(aTabsummaryForProperties, {summaryType: ConfigDataEntitiesDictionary.TAGS});
      if (!CS.isEmpty(oTabsummaryForAttributes)) {
        oTabsummaryForAttributes.selectedAttributes = !CS.isEmpty(oRelationshipExport) ? oRelationshipExport.attributes : [] ;
        oTabsummaryForAttributes.referencedData.referencedAttributes = !CS.isEmpty(oRelationshipExport) ? oRelationshipExport.referencedData.referencedAttributes : {};
      }
      if (!CS.isEmpty(oTabsummaryForTags)) {
        oTabsummaryForTags.selectedTags = !CS.isEmpty(oRelationshipExport) ? oRelationshipExport.tags : [];
        oTabsummaryForTags.referencedData.referencedTags = !CS.isEmpty(oRelationshipExport) ? oRelationshipExport.referencedData.referencedTags : {};
      }
    }

    return <ClassConfigViewClone
        actionItemModel={aActionItems}
        isActiveClassDirty={bIsActiveClassDirty}
        contentHierarchyData={oContentHierarchyData}
        availableAttributeListModel={aSectionListModel}
        isAttributeTableVisible={bIsAttributeTableVisible}
        activeClass={oActiveClass}
        sectionLayoutModel={oClassSectionLayoutModel}
        disabledFields={aDisabledFields}
        masterEntitiesForSection={oMasterEntitiesForSections}
        dragDetails={oDragDetails}
        referencedContexts={aReferencedContexts}
        commonConfigSectionViewModel = {oCommonConfigSectionViewModel}
        assetUploadConfigurationModel = {oAssetUploadConfigurationModel}
        side2RelationshipData = {oSide2RelationshipData}
        side2CollapseEnabled = {oSide2CollapseEnabled}
    />;
  };

  getRelationshipView = () => {
    var oActionBarList = this.getAppData().getActionBarList();
    let oRelationshipProps = this.getComponentProps().relationshipView;
    var oRelationshipContexts = oRelationshipProps.getContextData();
    var oActionItemModel = {};
    oActionItemModel.RelationshipListView = oActionBarList.RelationshipConfigView.RelationshipConfigListView;
    // var aRelationshipListModel = this.getRelationshipListViewModels();
    var oSelectedRelationshipViewModel = this.getSelectedRelationshipViewModel();
    var oComponentProps = this.getComponentProps();
    var bListItemCreated = oComponentProps.screen.getListItemCreationStatus();
    var oSelectedRelationship = oRelationshipProps.getSelectedRelationship();
    let bIsRelationshipConfigDirty = false;
    if (oSelectedRelationship.isDirty || oSelectedRelationship.isCreated) {
      if (oSelectedRelationship.clonedObject) {
        oSelectedRelationship = oSelectedRelationship.clonedObject;
      }
      bIsRelationshipConfigDirty = true;
    }
    let bIsRelationshipDialogActive = oRelationshipProps.getIsRelationshipDialogActive();
    let oScreenProps = oComponentProps.screen;
    let bShowCheckboxColumn = true;
    let oRelationshipConfig = oRelationshipProps.getSelectedRelationshipConfigDetails();
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oGridViewProps = {
      context: GridViewContexts.RELATIONSHIP,
      tableContextId: GridViewContexts.RELATIONSHIP,
      showCheckboxColumn: bShowCheckboxColumn,
      enableImportExportButton: true,
      enableManageEntityButton: false
    };

    let aDisabledFields = [];
    let sDialogLabel = "RELATIONSHIP";
    if (oSelectedRelationship.isLite) {
      aDisabledFields = ["context", "dataTransfer", "editable"];
      oSelectedRelationshipViewModel.properties.isKlassListNatureType = true;
      oSelectedRelationshipViewModel.shouldShowTaxonomyType = false
    } else if (oSelectedRelationship.id == "standardArticleGoldenArticleRelationship") {
      aDisabledFields = ["context", "dataTransfer", "tabs", "editable"];
    }
    if(!oSelectedRelationship.isLite) {
      oSelectedRelationshipViewModel.shouldShowTaxonomyType = true;
      oGridViewProps.enableManageEntityButton = true;
    }
    let oManageEntityDialog = (bIsEntityDialogOpen && oGridViewProps.enableManageEntityButton) ? this.getEntityDialog() : null;
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };
    let oIconLibraryData = SettingUtils.getIconLibraryData();
    let oSelectIconDialog = this.getSelectIconDialog();

    return <RelationshipConfigView
        gridViewProps={oGridViewProps}
        activeRelationship={oSelectedRelationship}
        isDialogOpen={bIsRelationshipDialogActive}
        selectedRelationshipDetailedModel={oSelectedRelationshipViewModel}
        actionItemModel={oActionItemModel}
        bRelationshipCreated={bListItemCreated}
        relationshipContextList={oRelationshipContexts}
        isRelationshipDirty={bIsRelationshipConfigDirty}
        isRelationshipDummy={oSelectedRelationship.isCreated}
        disabledFields={aDisabledFields}
        relationshipConfigDetails = {oRelationshipConfig}
        dialogLabel={sDialogLabel}
        ManageEntityDialog={oManageEntityDialog}
        columnOrganizerData={oColumnOrganizerData}
        iconLibraryData={oIconLibraryData}
        selectIconDialog={oSelectIconDialog}
    />;
  };

  getPropertyCollectionModelList = (oCurrentEntity, sContext) => {

    var oAppData = this.getAppData();
    var oSectionMap = oAppData.getPropertyCollectionList();
    var aSectionModels = [];
    CS.forEach(oSectionMap, function (oSection) {
      if (CS.find(oCurrentEntity.sections, {propertyCollectionId: oSection.id}) || oSection.isForXRay) {
        return;
      }
      aSectionModels.push(new ContextMenuViewModel(
          oSection.id,
          CS.getLabelOrCode(oSection),
          false,
          "",
          {context: sContext}
      ));
    });
    return aSectionModels;
  };

  getPermissionAvailableTemplateModelList = () => {

    var oAppData = this.getAppData();
    var oComponentProps = this.getComponentProps();
    var PermissionProps = oComponentProps.permissionConfigView;
    var oTemplatesListMap = oAppData.getTemplatesList();
    var oActiveNatureClassDetails = PermissionProps.getActivePermissionEntity();
    var aAllowedTemplateIds = oActiveNatureClassDetails.allowedTemplates;
    var aTemplateModels = [];
    CS.forEach(oTemplatesListMap, function (oTemplate) {
      if(!CS.includes(aAllowedTemplateIds, oTemplate.id) && oTemplate.type != "contextualTemplate") {
        aTemplateModels.push(new ContextMenuViewModel(
            oTemplate.id,
            CS.getLabelOrCode(oTemplate),
            false,
            "",
            {context: "permissionTemplateList"}
        ));
      }
    });
    return aTemplateModels;
  };

  getLazyMSSCustomRequestResponseInfo =(sURL, aTypes) => {
    return {
      requestType: "customType",
      responsePath: ["success", "list"],
      requestURL: sURL,
      customRequestModel: {
        types: aTypes
      }
    }
  }

  getAttributionTaxonomyDetailViewData = () => {
    var oAttributionTaxonomyConfigViewProps = this.getComponentProps().attributionTaxonomyConfigView;
    var oActiveTaxonomy = oAttributionTaxonomyConfigViewProps.getActiveDetailedTaxonomy();
    oActiveTaxonomy = oActiveTaxonomy.clonedObject || oActiveTaxonomy;
    var aPropertyCollectionModels = this.getPropertyCollectionModelList(oActiveTaxonomy,"attributionTaxonomyDetail");
    var oMasterEntitiesForSections = this.getAllMasterEntitiesForSections(oActiveTaxonomy);
    var oAppData = this.getAppData();
    var aTaskList = oAppData.getTasksList();
    var oRuleList = oAppData.getRuleList();
    var aFilteredRuleList = CS.reject(oRuleList, function (oRule) {
      return oRule.id == "ONBOARDING_TO_PREPERATION" || oRule.id == "IGNORED_TO_NO_INTEREST";
    });

    let aTabList = AttributionTaxonomiesTabLayoutData().tabList;
    let sSelectedTabId = oAttributionTaxonomyConfigViewProps.getAttributionTaxonomyListActiveTabId();
    let oAttributionTaxonomiesListTabData = {};
    oAttributionTaxonomiesListTabData.selectedTabId = sSelectedTabId;
    oAttributionTaxonomiesListTabData.tabList = aTabList;

    var aEmbeddedKlasses = oAppData.getEmbeddedKlassList();
    let sURLToGetKlassList = RequestMapping.getRequestUrl(oClassRequestMapping.GetLazyKlassListByNatureType);
    let aKlassTypes = [NatureTypeDictionary.EMBEDDED];
    let oRequestResponseObjectForEmbeddedKlass = this.getLazyMSSCustomRequestResponseInfo(sURLToGetKlassList, aKlassTypes);
    let oReferencedData = oAttributionTaxonomyConfigViewProps.getReferencedData();
    let oAllowedTaxonomyHierarchyList = oAttributionTaxonomyConfigViewProps.getAllowedTaxonomyHierarchyList();
    return ({
      propertyCollectionModels: aPropertyCollectionModels,
      masterEntitiesForSection: oMasterEntitiesForSections,
      embeddedKlassList: aEmbeddedKlasses,
      taskList: aTaskList,
      ruleList: aFilteredRuleList,
      requestResponseObjectForEmbedded: oRequestResponseObjectForEmbeddedKlass,
      referencedDataRules: oReferencedData.referencedDataRules,
      referencedTasks: oReferencedData.referencedTasks,
      referencedKlasses: oActiveTaxonomy.referencedKlasses,
      referencedTags: oReferencedData.referencedTags,
      referencedAttributes: oReferencedData.referencedAttributes,
      referencedContexts : oReferencedData.referencedContexts,
      allowedTaxonomyHierarchyList : oAllowedTaxonomyHierarchyList,
      attributionTaxonomiesListTabData : oAttributionTaxonomiesListTabData
    });
  };

  getAttributionTaxonomyView = () => {
    var oAppData = this.getAppData();
    var oAttributionTaxonomyConfigViewProps = this.getComponentProps().attributionTaxonomyConfigView;
    var aSelectedTaxonomyLevels = oAttributionTaxonomyConfigViewProps.getSelectedTaxonomyLevels();
    var aTaxonomyList = oAppData.getAttributionTaxonomyList();
    var aMasterTypeTags = oAppData.getMasterTypeTagList();
    var oLinkedMasterTypeTags = oAttributionTaxonomyConfigViewProps.getLinkedMasterTypeTags();
    var oActiveTaxonomy = oAttributionTaxonomyConfigViewProps.getActiveTaxonomy();
    var oActiveDetailedTaxonomy = oAttributionTaxonomyConfigViewProps.getActiveDetailedTaxonomy();
    let bIsActiveDetailedTaxonomyDirty = false;
    if (oActiveDetailedTaxonomy.clonedObject) {
      bIsActiveDetailedTaxonomyDirty = true;
      oActiveDetailedTaxonomy = oActiveDetailedTaxonomy.clonedObject
    }
    var oTaxonomyDetailViewData = this.getAttributionTaxonomyDetailViewData();
    var bShowPopover = oAttributionTaxonomyConfigViewProps.getPopoverVisibilityStatus();
    var sAddChildPopoverVisibleLevelId = oAttributionTaxonomyConfigViewProps.getAddChildPopoverVisibleLevelId();
    var aAllowedTagValues = oAttributionTaxonomyConfigViewProps.getAllowedTagValues();
    var bIsScrollAutomatically = oAttributionTaxonomyConfigViewProps.getIsAttributionTaxonomyHierarchyScrollAutomaticallyEnabled();
    var oActiveTaxonomyLevel = oAttributionTaxonomyConfigViewProps.getActiveTaxonomyLevel();
    if(!CS.isEmpty(oActiveTaxonomyLevel)){
      oActiveTaxonomyLevel = oActiveTaxonomyLevel.addedLevel.addedTag;
    }
    let oActionItemModel = {};
    var oActionBarList = this.getAppData().getActionBarList();
    var oHierarchyConfigView = oActionBarList.AttributionTaxonomyConfigView;
    oActionItemModel.AttributionTaxonomyListView = oHierarchyConfigView.AttributionTaxonomyConfigListView;
    oActionItemModel.AttributionTaxonomyDetailedView = [];
    var oActiveTaxonomyLevelChildren = oAttributionTaxonomyConfigViewProps.getActiveTaxonomyLevelChildren();
    return <AttributionTaxonomyConfigView
        selectedTaxonomyLevels={aSelectedTaxonomyLevels}
        taxonomyList={aTaxonomyList}
        linkedMasterList={oLinkedMasterTypeTags}
        masterList={aMasterTypeTags}
        activeTaxonomy={oActiveTaxonomy}
        activeDetailedTaxonomy={oActiveDetailedTaxonomy}
        showPopover={bShowPopover}
        isActiveDetailedTaxonomyDirty={bIsActiveDetailedTaxonomyDirty}
        activeDetailedSectionsData={oTaxonomyDetailViewData}
        addChildPopoverVisibleLevelId={sAddChildPopoverVisibleLevelId}
        allowedTagValues={aAllowedTagValues}
        isScrollAutomatically={bIsScrollAutomatically}
        activeTaxonomyLevel={oActiveTaxonomyLevel}
        activeTaxonomyLevelChildren={oActiveTaxonomyLevelChildren}
        actionItemModel = {oActionItemModel}
        iconLibraryData = {SettingUtils.getIconLibraryData()}
    />;

  };

  getLanguageTaxonomyView = (sType) => {
    let oComponentProps = this.getComponentProps();
    let oLanguageTreeProps = oComponentProps.languageTreeConfigView;
    let oEntity = oLanguageTreeProps.getLanguageTree();
    let oLanguageTreeVisualProps = oLanguageTreeProps.getLanguageValueListByTypeGeneric();
    let aNumberFormat = CS.values(NumberFormatData());
    let aDateFormat = CS.values(DateFormatData());
    let sSplitter = SettingUtils.getSplitter();
    let sLanguageContext = "languageTree";

    let oContentHierarchyData = {
      oHierarchyTreeVisualProps: oLanguageTreeVisualProps,
      oHierarchyTree: oEntity,
      hierarchyNodeContextMenuItemList: [],
      selectedContext: sLanguageContext ,
      scrollAutomatically: false
    };
    let oActiveLanguage = oLanguageTreeProps.getActiveLanguage();
    let isLanguageDirty = false;
    if(oActiveLanguage.languageClone){
      oActiveLanguage = oActiveLanguage.languageClone;
      isLanguageDirty = true;
    }
    let oCommonConfigSectionViewModel = {};
    let oLocaleIdMssModel = {
      disabled: false,
      label: "",
      items: LocaleData,
      selectedItems: [oActiveLanguage.localeId],
      isMultiSelect: false,
      context: sLanguageContext + sSplitter + 'localeId',
      cannotRemove: true
    };
    let oNumberFormatMssModel = {
      disabled: false,
      label: "",
      items: aNumberFormat,
      selectedItems: [oActiveLanguage.numberFormat],
      isMultiSelect: false,
      context: sLanguageContext + sSplitter + 'numberFormat',
      cannotRemove: true
    };
    let oDateFormatMssModel = {
      disabled: false,
      label: "",
      items: aDateFormat,
      selectedItems: [oActiveLanguage.dateFormat],
      isMultiSelect: false,
      context: sLanguageContext + sSplitter + 'dateFormat',
      cannotRemove: true
    };
    if(!CS.isEmpty(oActiveLanguage)){
      oCommonConfigSectionViewModel = {
        code: oActiveLanguage.code,
        label: oActiveLanguage.label,
        abbreviation: oActiveLanguage.abbreviation,
        localeId: oLocaleIdMssModel
      };
    }
    let oCommonConfigLanguageViewModel = {};
    let bIsDefaultLanguage = oActiveLanguage.isDefaultLanguage;

    if(!CS.isEmpty(oActiveLanguage) && !oActiveLanguage.isNewlyCreated){
      oCommonConfigLanguageViewModel = {
        label: oActiveLanguage.label,
        code: oActiveLanguage.code,
        localeId: oLocaleIdMssModel,
        abbreviation: oActiveLanguage.abbreviation,
        numberFormat: oNumberFormatMssModel,
        dateFormat: oDateFormatMssModel,
        isDataLanguage: {context: sLanguageContext , isSelected: oActiveLanguage.isDataLanguage, isDisabled: bIsDefaultLanguage},
        isUserInterfaceLanguage: {context: sLanguageContext , isSelected: oActiveLanguage.isUserInterfaceLanguage, isDisabled: bIsDefaultLanguage},
        isDefaultLanguage: {context: sLanguageContext , isSelected: bIsDefaultLanguage, isDisabled: bIsDefaultLanguage},
        icon: {context: sLanguageContext , icon: oActiveLanguage.icon, iconKey: oActiveLanguage.iconKey},
        showSelectIconDialog: oActiveLanguage.showSelectIconDialog,
        selectIconData: SettingUtils.getIconLibraryData(),
      };
    }

    let aActionItems = [];
    let oAppData = this.getAppData();
    let oActionBarList = oAppData.getActionBarList();
    let sSelectedItem = oComponentProps.screen.getSelectedLeftNavigationTreeItem();
    if(sSelectedItem === "language") {
      aActionItems = CS.cloneDeep(oActionBarList.LanguageTreeConfigView.LanguageTreeConfigListView);
    }

    return (<LanguageTreeConfigView
        actionItemModel={aActionItems}
        contentHierarchyData={oContentHierarchyData}
        activeLanguage = {oActiveLanguage}
        commonConfigSectionViewModel={oCommonConfigSectionViewModel}
        commonConfigLanguageViewModel={oCommonConfigLanguageViewModel}
        isLanguageDirty = {isLanguageDirty}
    />);
  };

  getSmartDocumentSectionModel = (oActiveSection, aReferencedData) => {
    let sContext = "smartDocument";
    let oSectionModel = {};
    let oCreateViewModel = {};
    if (CS.isNotEmpty(oActiveSection)) {
      if (oActiveSection.isNewlyCreated) {
        oCreateViewModel.code = oActiveSection.code;
        oCreateViewModel.label = oActiveSection.label;
        if (oActiveSection.type === "smartDocumentTemplate") {
          oCreateViewModel.zipTemplateId = oActiveSection.zipTemplateId;
        }
      } else {
        oSectionModel.label = oActiveSection.label;
        oSectionModel.code = oActiveSection.code;
        switch (oActiveSection.type) {
          case "smartDocumentEntity":
            oSectionModel.rendererLicenceKey = oActiveSection.rendererLicenceKey;
            oSectionModel.physicalCatalogIds = this.getSelectionToggleModel(
              MockDataForPhysicalCatalogAndPortal.portals(),
                oActiveSection.physicalCatalogIds, "smartDocument", false);
            break;
          case "smartDocumentTemplate":
            oSectionModel.zipTemplateId = oActiveSection.zipTemplateId;
            break;
          case "smartDocumentPreset":
            let aSelectedTaxonomyIds = oActiveSection.taxonomyIds;
            let oReferencedTaxonomy = oActiveSection.referencedTaxonomies;
            let oSmallTaxonomiesData = SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomy, aReferencedData.allowedTaxonomiesById);

            let oModelExtraData = {
              languageCodes: aReferencedData.languageCodes,
              pdfColorSpace: aReferencedData.pdfColorSpace,
              pdfMarksBleeds: aReferencedData.pdfMarksBleeds,
              splitter: SettingUtils.getSplitter(),
              klassesData: {
                url: RequestMapping.getRequestUrl(oClassRequestMapping.GetKlassesListByBaseType),
                selectedKlassIds: oActiveSection.klassesIds,
                referencedData: oActiveSection.referencedKlasses,
              },
              taxonomyCustomView: this.getSmallTaxonomyView(oSmallTaxonomiesData, sContext, false),
            }
            oSectionModel = new SmartDocumentPresetViewModel(oActiveSection, oModelExtraData, sContext);
            this.enablePDFConfigurations(oSectionModel.smartDocumentPresetPdfConfiguration, oActiveSection.smartDocumentPresetPdfConfiguration);
            break;
        }
      }
    }
    return {oSectionModel, oCreateViewModel};
  }

  enablePDFConfigurations = (oSmartDocumentPresetPdfConfiguration, oPDFConfigurations) => {
    let sPDFOwnerPassword = oSmartDocumentPresetPdfConfiguration.pdfOwnerPassword;
    let sPDFUserPassword = oSmartDocumentPresetPdfConfiguration.pdfUserPassword;
    let oPDFAllowAnnotations =  oSmartDocumentPresetPdfConfiguration.pdfAllowAnnotations;
    let oPDFAllowCopyContent =  oSmartDocumentPresetPdfConfiguration.pdfAllowCopyContent;
    let oPDFAllowModifications =  oSmartDocumentPresetPdfConfiguration.pdfAllowModifications;
    let oPDFAllowPrinting =  oSmartDocumentPresetPdfConfiguration.pdfAllowPrinting;
    if(sPDFOwnerPassword ||  sPDFUserPassword){
      oPDFAllowAnnotations.isDisabled = false;
      oPDFAllowCopyContent.isDisabled = false;
      oPDFAllowModifications.isDisabled = false;
      oPDFAllowPrinting.isDisabled = false;
    }
    else if(!sPDFOwnerPassword && !sPDFUserPassword){
      oPDFAllowAnnotations.isSelected = false;
      oPDFAllowCopyContent.isSelected = false;
      oPDFAllowModifications.isSelected = false;
      oPDFAllowPrinting.isSelected = false;
      oPDFConfigurations.pdfAllowAnnotations = false;
      oPDFConfigurations.pdfAllowCopyContent = false;
      oPDFConfigurations.pdfAllowModifications = false;
      oPDFConfigurations.pdfAllowPrinting = false;

    }
  };

  getSmartDocumentConfigView = () => {
    let oSmartDocumentConfigProps = this.getComponentProps().smartDocumentConfigView;
    let oSmartDocumentVisualProps = oSmartDocumentConfigProps.getSmartDocumentValueListByTypeGeneric();
    let bIsSmartDocumentDirty = oSmartDocumentConfigProps.getIsSmartDocumentConfigDirty();
    let oActiveSmartDocumentEntity = oSmartDocumentConfigProps.getSmartDocumentEntity();
    let oActiveSection = oSmartDocumentConfigProps.getActiveSection();
    let aLanguageCodes = oSmartDocumentConfigProps.getLanguageCodes();
    let oAllowedTaxonomiesById = oSmartDocumentConfigProps.getAllowedTaxonomies();
    let aPDFColorSpace =  new pdfColorSpaceData();
    let aPDFMarksBleeds =  new pdfMarksBleedsData();
    if (oActiveSection && oActiveSection.clonedObject) {
      oActiveSection = oActiveSection.clonedObject;
    }
    let aReferencedData = {
      languageCodes: aLanguageCodes,
      allowedTaxonomiesById: oAllowedTaxonomiesById,
      pdfColorSpace: aPDFColorSpace.aPDFColorSpace,
      pdfMarksBleeds: aPDFMarksBleeds.aPDFMarksBleeds
    }
    let {oSectionModel, oCreateViewModel} = this.getSmartDocumentSectionModel(oActiveSection, aReferencedData);
    let oContentHierarchyData = {
      oHierarchyTreeVisualProps: oSmartDocumentVisualProps,
      oHierarchyTree: oActiveSmartDocumentEntity,
      hierarchyNodeContextMenuItemList: [],
      selectedContext: "smartDocument",
      scrollAutomatically: false
    };
    let bShowSmartDocumentIcon =  true;
    return (<SmartDocumentConfigView
        isSectionDirty={bIsSmartDocumentDirty}
        context={"smartDocument"}
        activeSection={oActiveSection}
        hierarchyData={oContentHierarchyData}
        createViewModel={oCreateViewModel}
        sectionViewModel={oSectionModel}
        showSmartDocumentIcon={bShowSmartDocumentIcon}
    />);
  };

  getTranslationViewActionButtonData = () => {
    var oTranslationsConfigViewProps = this.getComponentProps().translationsConfigView;
    let sModule = oTranslationsConfigViewProps.getSelectedModule();
    let aTranslationsModuleList = this.getAppData().getTranslationsModuleList().SettingScreenModules;
    let oSelectedModule = CS.find(aTranslationsModuleList, {id: "translations"+SettingUtils.getSplitter()+sModule}) || null;

    if (oSelectedModule.properties) {
      var bIsDisabled = oSelectedModule.properties.length == 1;
    }

    let oPropertyData = {
      list: oSelectedModule.properties ,
      isMultiSelect: false,
      showSelectedItems: false,
      onClickHandler: this.handlePropertyChanged,
      disabled: bIsDisabled,
      selectedProperty: oTranslationsConfigViewProps.getSelectedProperty()
    };
    let oLanguageData = {
      list: oTranslationsConfigViewProps.getAvailableLanguages(),
      isMultiSelect: true,
      oContentData: true,
      onApplyHandler: this.applySelectedLanguages,
      selectedList: oTranslationsConfigViewProps.getDisplayLanguages(),
      showSelectedItems: true
    };

    let oTranslationViewActonItemData = {
      propertyData : oPropertyData,
      languageData: oLanguageData
    };

    return oTranslationViewActonItemData;
  };

  getTranslationsView = () => {
    //Module List View
    var oTranslationsModuleList = this.getAppData().getTranslationsModuleList();
    var aModuleList = oTranslationsModuleList.SettingScreenModules;
    var aClassModuleList = oTranslationsModuleList.ClassModules;
    var oTranslationsConfigViewProps = this.getComponentProps().translationsConfigView;
    var sSelectedModule = oTranslationsConfigViewProps.getSelectedModule();
    var oScreenProps = this.getComponentProps().screen;
    var oActionItemsData = this.getTranslationViewActionButtonData();
    var sSelectedChildModule = oTranslationsConfigViewProps.getSelectedChildModule();

    let oLanguageInfoData = getLanguageInfo();
    let sDefaultLanguage = oLanguageInfoData.defaultLanguage;
    let aSupportedLanguage = sSelectedModule === SettingScreenModuleDictionary.TAG_VALUES? oLanguageInfoData.dataLanguages :
        oLanguageInfoData.userInterfaceLanguages;

    let aAllowedScreenModules = [SettingScreenModuleDictionary.TAG, SettingScreenModuleDictionary.TAG.MASTER_LIST, SettingScreenModuleDictionary.TAG_VALUES];
    let bIsHierachical = CS.includes(aAllowedScreenModules, sSelectedModule);

    /**
     * @description - To search text in selected column/default language column.
     * @private
     */
    let sGridViewSortLanguage = oScreenProps.getGridViewSortLanguage();
    let sGridViewSearchLanguageCode = (sGridViewSortLanguage === "defaultLanguage" || sGridViewSortLanguage === "") ?
        "" : sGridViewSortLanguage;
    let sGridViewSearchBarPlaceHolder = "";
    if (sGridViewSearchLanguageCode) {
      let oLanguage = CS.find(aSupportedLanguage, {code: sGridViewSearchLanguageCode});
      let sLabel = CS.getLabelOrCode(oLanguage);
      sGridViewSearchBarPlaceHolder = getTranslation().SEARCH_IN + " " + sLabel;
    }
    else {
      sGridViewSearchBarPlaceHolder =  getTranslation().SEARCH_IN_LABEL
    }

    //Grid View
    var oGridData = {
      gridViewSkeleton: oScreenProps.getGridViewSkeleton(),
      gridViewData: oScreenProps.getGridViewData(),
      gridViewVisualData: oScreenProps.getGridViewVisualData(),
      gridViewPaginationData: oScreenProps.getGridViewPaginationData(),
      gridViewTotalItems: oScreenProps.getGridViewTotalItems(),
      gridViewTotalNestedItems: oScreenProps.getGridViewTotalNestedItems(),
      gridViewSearchText: oScreenProps.getGridViewSearchText(),
      gridViewSortBy: oTranslationsConfigViewProps.getSortByField() ,
      gridViewSortOrder: oScreenProps.getGridViewSortOrder(),
      isGridDataDirty: oScreenProps.getIsGridDataDirty(),
      showCheckboxColumn: false,
      gridViewContext: GridViewContexts.TRANSLATIONS,
      hierarchical: bIsHierachical,
      actionItemData: oActionItemsData,
      disableDeleteButton: true,
      disableCreate: true,
      enableImportExportButton: true,
      gridViewSearchBarPlaceHolder: sGridViewSearchBarPlaceHolder,
      resizedColumnId: oScreenProps.getResizedColumnId()
    };
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    return <TranslationsConfigView
        moduleList={aModuleList}
        selectedModule={sSelectedModule}
        gridData={oGridData}
        classModuleList={aClassModuleList}
        selectedClassModule={sSelectedChildModule}
        defaultLanguage={sDefaultLanguage}
        userInterfaceLanguages={aSupportedLanguage}
        columnOrganizerData={oColumnOrganizerData}
    />
  };

  getYesNoPropertyView = (bIsActivationEnable) => {
    return (<GridYesNoPropertyView
        isDisabled={false}
        value={bIsActivationEnable}
    />)
  };

  handleYesNoPropertyViewClicked = (bIsRuntimeMappingEnabled,bIsDisabled) => {
    return {
      context: "profile",
      isDisabled: bIsDisabled,
      isSelected: bIsRuntimeMappingEnabled
    }
  };


  getTriggeringEventTypeModel = (aItems, oActiveProcess, sSplitter) => {
    return {
      items: aItems,
      selectedItems: [oActiveProcess.triggeringType],
      cannotRemove: true,
      context: "process" + sSplitter + "triggeringEventType",
      disabled: false,
      label: "",
      isMultiSelect: false,
    }
  };


  _getProfileDashboardTabMSSObject = (oSelectedProfile, oRegResponseInfo, sContext) => {
    let oComponentProps = this.getComponentProps();
    let oProfileConfigProps = oComponentProps.profileConfigView;
    let aReferencedDashboardTabs = oProfileConfigProps.getReferencedDashboardTabs();
    let aDashboardTabs = SettingUtils.getAppData().getDashboardTabsList();

    return {
      disabled: false,
      label: "",
      items: aDashboardTabs,
      selectedItems: [oSelectedProfile.dashboardTabId],
      singleSelect: true,
      context: sContext,
      referencedData: aReferencedDashboardTabs,
      requestResponseInfo: oRegResponseInfo,
      showCreateButton: true,
      cannotRemove: true
    }
  };

  _getMSSObjectForSystems = (oActiveEndpoint) => {
    let oComponentProps = this.getComponentProps();
    let oProfileConfigProps = oComponentProps.profileConfigView;
    let sSystemId = oActiveEndpoint.systemId || "";
    let aSelectedItems = sSystemId ? [sSystemId] : [];
    let sSplitter = SettingUtils.getSplitter();
    oProfileConfigProps.setSelectedSystem(oActiveEndpoint.id, aSelectedItems);

    return {
      items: oProfileConfigProps.getSystemsList(),
      selectedItems: aSelectedItems,
      singleSelect: true,
      context: "endpoint " + {sSplitter} + " systems",
      showCreateButton: true,
      isLoadMoreEnabled: true,
      searchText: oProfileConfigProps.getSystemsListSearchText(),
      cannotRemove: true
    };
  };

  _getMSSObject = (sLabel, bIsDisabled, aItems, aSelectedItems, sContext, oRegResponseInfo, sPropertyType, bIsSingleSelect) => {
    let oReferencedData = {};
    let oComponentProps = this.getComponentProps();
    let oProfileConfigProps = oComponentProps.profileConfigView;

    switch (sPropertyType) {
      case "mappings":
        oReferencedData = oProfileConfigProps.getReferencedMappings();
        break;
      case "processes":
        oReferencedData = oProfileConfigProps.getReferencedProcesses();
        break;
      case "authorizationMappings":
        oReferencedData = oProfileConfigProps.getReferencedAuthorizationMappings();
        break;
      case "jmsProcesses":
        oReferencedData = oProfileConfigProps.getReferencedJmsProcesses();
        break;
    }

    return {
      label: sLabel,
      disabled: bIsDisabled,
      items: aItems,
      selectedItems: aSelectedItems,
      isMultiSelect: !bIsSingleSelect,
      context: sContext,
      requestResponseInfo: oRegResponseInfo,
      referencedData: oReferencedData
    };
  };

  _getNormalMSSObject = (sLabel, bIsDisabled, aItems, aSelectedItems, sContext, bIsSingleSelect, bCannotRemove) => {
    let sSplitter = SettingUtils.getSplitter();
    return {
      label: sLabel,
      disabled: bIsDisabled,
      items: aItems,
      selectedItems: aSelectedItems,
      isMultiSelect: !bIsSingleSelect,
      context: "profile" + sSplitter + sContext,
      cannotRemove: bCannotRemove
    };
  };

  getEndpointConfigSectionLayoutModel = () => {
    let oComponentProps = this.getComponentProps();
    let oProfileConfigProps = oComponentProps.profileConfigView;
    let oActiveEndpoint = oProfileConfigProps.getSelectedProfile();
    let sSplitter = SettingUtils.getSplitter();

    if (oActiveEndpoint.clonedObject) {
      oActiveEndpoint = oActiveEndpoint.clonedObject;
    }

    let oModel = {};
    oModel.label = oActiveEndpoint.label;
    oModel.code = oActiveEndpoint.code;

    oModel.endpointType = {
      items: MockDataForEndPointTypes(),
      selectedItems: [oActiveEndpoint.endpointType],
      cannotRemove: true,
      context: "endpoint" + sSplitter + "endpointType",
      disabled: true,
      label: "",
      isMultiSelect: false,
    };
    oModel.isRuntimeMappingEnabled = this.generateDataForEndpoints('isRuntimeMappingEnabled', oActiveEndpoint);
    oModel.systemId = this._getMSSObjectForSystems(oActiveEndpoint);
    oModel.processes = this.generateDataForEndpoints('processes', oActiveEndpoint);
    oModel.dashboardTab = this.generateDataForEndpoints('dashboardTab', oActiveEndpoint);
    oModel.physicalCatalog = this.generateDataForEndpoints('physicalCatalog', oActiveEndpoint);
    oModel.authorizationMapping = this.generateDataForEndpoints('authorizationMapping', oActiveEndpoint);
    oModel.mapping = this.generateDataForEndpoints('mapping', oActiveEndpoint);
    oModel.jmsProcesses = this.generateDataForEndpoints('jmsProcesses', oActiveEndpoint);
    return oModel;
  };

  generateDataForEndpoints = (sContext, oActiveEndpoint) => {
    let sSplitter = SettingUtils.getSplitter();
    let sContextType = "profile";

    switch (sContext) {

      case "physicalCatalog" :
        let aMockDataForPhysicalCatalogTypes = MockDataForOrganisationConfigPhysicalCatalogAndPortal.physicalCatalogs();
        let aSelectedPhysicalCatalog = oActiveEndpoint.physicalCatalogs || [oPhysicalCatalogDictionary.DATAINTEGRATION];
        let aPhysicalCatalogTypes = [];
        if (oActiveEndpoint.endpointType === "offboardingendpoint") {
          aPhysicalCatalogTypes = CS.filter(aMockDataForPhysicalCatalogTypes, function (oPhysicalCatalogType) {
            return oPhysicalCatalogType.id !== oPhysicalCatalogDictionary.ONBOARDING;
          });
        }else{
          aPhysicalCatalogTypes = CS.filter(aMockDataForPhysicalCatalogTypes, function (oPhysicalCatalogType) {
            return oPhysicalCatalogType.id !== oPhysicalCatalogDictionary.OFFBOARDING;
          });
        }
        return this._getNormalMSSObject("", false, aPhysicalCatalogTypes, aSelectedPhysicalCatalog, 'physicalCatalogs',true , true);

      case "dashboardTab" :
        sContext = sContextType + sSplitter + "dashboardTab";
        let oMSSDashboardTabsRequestResponseObj = {
          requestType: "configData",
          entityName: "dashboardTabs",
        };
        return this._getProfileDashboardTabMSSObject(oActiveEndpoint, oMSSDashboardTabsRequestResponseObj, sContext);

      case "processes" :
        let aProcessList = SettingUtils.getAppData().getProcessList();
        aProcessList = CS.flatMap(aProcessList);
        sContext = sContextType + sSplitter + "processes";
        let aTriggeringType = [];
        if (oActiveEndpoint.endpointType === "offboardingendpoint") {
          aTriggeringType = ["export"];
        } else {
          aTriggeringType = ["import"];
        }
        let oMSSProcessRequestResponseObj = {
          requestType: "configData",
          entityName: "processes",
          types: [oProcessTypeDictionary.ONBOARDING_PROCESS, oProcessTypeDictionary.OFFBOARDING_PROCESS],
          typesToExclude: [],
          customRequestModel: {
            eventType: [ProcessEventTypeDictionary.INTEGRATION],
            triggeringType: aTriggeringType
          }
        };
        return this._getMSSObject("", false, aProcessList, oActiveEndpoint.processes, sContext, oMSSProcessRequestResponseObj, "processes", true);

          case "isRuntimeMappingEnabled" :
            if (oActiveEndpoint.endpointType === "offboardingendpoint") {
              return this.handleYesNoPropertyViewClicked(oActiveEndpoint.isRuntimeMappingEnabled, true);
            } else {
              return this.handleYesNoPropertyViewClicked(oActiveEndpoint.isRuntimeMappingEnabled, true);
            }

      case "authorizationMapping":
        let aSelectedAuthorizationMappingList = oActiveEndpoint.authorizationMapping ? oActiveEndpoint.authorizationMapping : [];
        let aAuthorizationMappingList = SettingUtils.getAppData().getAuthorizationMappingList();
        aAuthorizationMappingList = CS.flatMap(aAuthorizationMappingList);
        sContext = sContextType + sSplitter + "authorizationMappings";
        let oMSSAuthorizationMappingsRequestResponseObj = {
          requestType: "configData",
          entityName: "authorizationMappings",
        };
        return this._getMSSObject("", false, aAuthorizationMappingList, aSelectedAuthorizationMappingList, sContext, oMSSAuthorizationMappingsRequestResponseObj, "authorizationMappings", true);

      case "mapping" :
        let aMappingList = SettingUtils.getAppData().getMappingList();
        aMappingList = CS.flatMap(aMappingList);
        sContext = sContextType + sSplitter + "mappings";
        let aTypes = [];
        if (oActiveEndpoint.endpointType === "offboardingendpoint") {
          aTypes = [MappingTypeDictionary.OUTBOUND_MAPPING];
        } else {
          aTypes = [MappingTypeDictionary.INBOUND_MAPPING];
        }
        let oMSSMappingsRequestResponseObj = {
          requestType: "configData",
          entityName: "mappings",
          types: aTypes
        };
        return this._getMSSObject("", false, aMappingList, oActiveEndpoint.mappings, sContext,
            oMSSMappingsRequestResponseObj, "mappings", true);

      case "jmsProcesses" :
        let aJmsProcessList = SettingUtils.getAppData().getProcessList();
        aJmsProcessList = CS.flatMap(aJmsProcessList);
        sContext = sContextType + sSplitter + "jmsProcesses";
        let oMSSJmsProcessRequestResponseObj = {
          requestType: "configData",
          entityName: "processes",
          types: [oProcessTypeDictionary.ONBOARDING_PROCESS, oProcessTypeDictionary.OFFBOARDING_PROCESS],
          typesToExclude: [],
          customRequestModel: {
            workflowTypes: [MockDataForWorkflowTypesDictionary.JMS_WORKFLOW]
          }
        };
        return this._getMSSObject("", false, aJmsProcessList, oActiveEndpoint.jmsProcesses, sContext, oMSSJmsProcessRequestResponseObj, "jmsProcesses", false);
        }
      };

  /** Set Translations to the label for HeaderData For Process **/
  _setTranslationsToHeaderDataForProcess = (ProcessConfigViewProps) => {
    let aTabHeaderData = ProcessConfigViewProps.getTabHeaderData();
    CS.forEach(aTabHeaderData, function (oTabHeaderData) {
      switch (oTabHeaderData.id) {
        case MockDataForFrequencyTypesDictionary.DURATION :
          oTabHeaderData.label = getTranslation().DURATION;
          break;
        case MockDataForFrequencyTypesDictionary.DATE :
          oTabHeaderData.label = getTranslation().DATE;
          break;
        case MockDataForFrequencyTypesDictionary.HOURMIN :
          oTabHeaderData.label = getTranslation().HOURMIN;
          break;
        case MockDataForFrequencyTypesDictionary.DAILY :
          oTabHeaderData.label = getTranslation().DAILY;
          break;
        case MockDataForFrequencyTypesDictionary.MONTHLY :
          oTabHeaderData.label = getTranslation().MONTHLY;
          break;
        case MockDataForFrequencyTypesDictionary.WEEKLY :
          oTabHeaderData.label = getTranslation().WEEKLY;
          break;
        case MockDataForFrequencyTypesDictionary.YEARLY :
          oTabHeaderData.label = getTranslation().YEARLY;
          break;
      }
    });
    ProcessConfigViewProps.setTabHeaderData(aTabHeaderData);
  };

  /** generate number records **/
  _generateNumberRecords = (iNumber, sType) => {
    let aItems = [];
    if (sType === "days") {
      for (let i = 1; i <= iNumber; i++) {
        let oTemp = {};
        oTemp["id"] = i;
        oTemp["label"] = i.toString();
        aItems.push(oTemp);
      }
    } else if (sType === "hours") {
      for (let i = 0; i < iNumber; i++) {
        let oTemp = {};
        oTemp["id"] = i;
        oTemp["label"] = i.toString();
        aItems.push(oTemp);
      }
    } else if (sType === "mins") {
      for (let i = 0; i < iNumber; i++) {
        let oTemp = {};
        oTemp["id"] = i;
        oTemp["label"] = i.toString();
        aItems.push(oTemp);
      }
    } else if (sType === "months") {
      for (let i = 1; i <= iNumber; i++) {
        if (i != 5) {
          let oTemp = {};
          oTemp["id"] = i;
          oTemp["label"] = i.toString();
          aItems.push(oTemp);
        }
      }
    }

    return aItems;
  };

  /** Generate Date Data**/
  _generateDateData = (oActiveProcess, sFrequencyType, sType) => {
    let oFrequency = ProcessConfigViewProps.getFrequencyData();
    let sDate = oFrequency[sFrequencyType][sType];
    let sNewDate = CS.isEmpty(sDate) ? new Date() : new Date(sDate);

    return sNewDate;
  }

  /** Generate Data for Frequency Tab **/
  _generateData = (oActiveProcess, sFrequencyType, sType) => {
    let sSelectedItems = "";
    let aItems = [];
    let sContext = "";
    let sContextType = "process";
    let sSubContextType = "frequency";
    let bIsMultiselect = false;
    let bIsDisabled = false;
    let bIsCannotRemove = true;
    let sSplitter = SettingUtils.getSplitter();
    let oFrequency = ProcessConfigViewProps.getFrequencyData();
    switch (sFrequencyType) {
      case MockDataForFrequencyTypesDictionary.DURATION :
      case MockDataForFrequencyTypesDictionary.DAILY:
      case MockDataForFrequencyTypesDictionary.HOURMIN :
        if(sType === "hours"){
          aItems = this._generateNumberRecords(24, "hours");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "hours";
        }else if(sType === "mins"){
          aItems = this._generateNumberRecords(60, "mins");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "mins"
        }
        break;
      case MockDataForFrequencyTypesDictionary.DATE:
        if(sType === "hours"){
          aItems = this._generateNumberRecords(24, "hours");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "hours";
        }else if(sType === "mins"){
          aItems = this._generateNumberRecords(60, "mins");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "mins"
        }
        break;
      case MockDataForFrequencyTypesDictionary.WEEKLY :
        if(sType === "hours"){
          aItems =this._generateNumberRecords(24, "hours");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "hours";
        }else if(sType === "mins"){
          aItems = this._generateNumberRecords(60, "mins");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "mins"
        }else if(sType === "daysOfWeeks"){
          aItems = MockDataForProcessFrequencyDayList();
          sSelectedItems = oFrequency[sFrequencyType][sType];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "daysOfWeeks";
          bIsMultiselect = true;
          bIsCannotRemove = false;
        }
        break;

      case MockDataForFrequencyTypesDictionary.MONTHLY :
        if(sType === "days"){
          aItems = this._generateNumberRecords(31, "days");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "days";
          bIsMultiselect = false;
          bIsCannotRemove = true;
        }else if(sType === "months"){
          aItems = this._generateNumberRecords(6, "months");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "months";
          bIsMultiselect = false;
          bIsCannotRemove = true;
        }
        break;

      case MockDataForFrequencyTypesDictionary.YEARLY :
        if(sType === "days"){
          aItems = this._generateNumberRecords(31, "days");
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "days";
          bIsMultiselect = false;
          bIsCannotRemove = true;
        }else if(sType === "monthsOfYear"){
          aItems = MockDataForfrequencyMonthListDictionary();
          sSelectedItems = [oFrequency[sFrequencyType][sType]];
          sContext = sContextType + sSplitter + sSubContextType + sSplitter + sFrequencyType + sSplitter + "monthsOfYear";
          bIsMultiselect = false;
          bIsCannotRemove = true;
        }
        break;
    }

    let oData = {
      items: aItems,
      selectedItems: sSelectedItems,
      cannotRemove: bIsCannotRemove,
      context: sContext,
      disabled: bIsDisabled,
      label: "",
      isMultiSelect: bIsMultiselect,
    };

    return oData;
  };

  /** Tab Data for Cron Expression Generator**/
  _generateTabSummaryData = (oActiveProcess, oModel) => {
    let oTabSummaryData = {};
    let sSelectedTabId = ProcessConfigViewProps.getSelectedTabId();
    if (sSelectedTabId == TabHeaderDataForProcess.DURATION) {
      let oData = {};
      oData.hours = this._generateData(oActiveProcess, TabHeaderDataForProcess.DURATION, "hours");
      oData.mins = this._generateData(oActiveProcess, TabHeaderDataForProcess.DURATION, "mins");
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.DURATION,
        selectedTabId: sSelectedTabId,
        tabHeaderData: ProcessConfigViewProps.getTabHeaderData(),
        data: oData
      }
    }else if (sSelectedTabId == TabHeaderDataForProcess.DATE) {
      let oData = {};
      oData.date = this._generateDateData(oActiveProcess, TabHeaderDataForProcess.DATE, "date");
      oData.hours = this._generateData(oActiveProcess, TabHeaderDataForProcess.DATE, "hours");
      oData.mins = this._generateData(oActiveProcess, TabHeaderDataForProcess.DATE, "mins");
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.DATE,
        selectedTabId: sSelectedTabId,
        tabHeaderData: ProcessConfigViewProps.getTabHeaderData(),
        data: oData
      }
    }else if (sSelectedTabId == TabHeaderDataForProcess.DAILY) {
      let oData = {};
      oData.hours = this._generateData(oActiveProcess, TabHeaderDataForProcess.DAILY, "hours");
      oData.mins = this._generateData(oActiveProcess, TabHeaderDataForProcess.DAILY, "mins");
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.DAILY,
        selectedTabId: sSelectedTabId,
        tabHeaderData: ProcessConfigViewProps.getTabHeaderData(),
        data: oData
      }
    } else if (sSelectedTabId == TabHeaderDataForProcess.HOURMIN) {
      let oData = {};
      oData.hours = this._generateData(oActiveProcess, TabHeaderDataForProcess.HOURMIN, "hours");
      oData.mins = this._generateData(oActiveProcess, TabHeaderDataForProcess.HOURMIN, "mins");
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.HOURMIN,
        selectedTabId: sSelectedTabId,
        tabHeaderData: ProcessConfigViewProps.getTabHeaderData(),
        data: oData
      }
    } else if (sSelectedTabId == TabHeaderDataForProcess.WEEKLY) {
      let oData = {};
      oData.daysOfWeeks = this._generateData(oActiveProcess, TabHeaderDataForProcess.WEEKLY, "daysOfWeeks");
      oData.hours = this._generateData(oActiveProcess, TabHeaderDataForProcess.WEEKLY, "hours");
      oData.mins = this._generateData(oActiveProcess, TabHeaderDataForProcess.WEEKLY, "mins");
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.WEEKLY,
        selectedTabId: sSelectedTabId,
        tabHeaderData: ProcessConfigViewProps.getTabHeaderData(),
        data: oData
      }
    } else if (sSelectedTabId == TabHeaderDataForProcess.MONTHLY) {
      let oData = {};
      oData.days = this._generateData(oActiveProcess, TabHeaderDataForProcess.MONTHLY, "days");
      oData.months = this._generateData(oActiveProcess, TabHeaderDataForProcess.MONTHLY, "months");
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.MONTHLY,
        selectedTabId: sSelectedTabId,
        tabHeaderData: ProcessConfigViewProps.getTabHeaderData(),
        data: oData
      }
    } else if (sSelectedTabId == TabHeaderDataForProcess.YEARLY) {
      let oData = {};
      oData.monthsOfYear = this._generateData(oActiveProcess, TabHeaderDataForProcess.YEARLY, "monthsOfYear");
      oData.days = this._generateData(oActiveProcess, TabHeaderDataForProcess.YEARLY, "days");
      oTabSummaryData = {
        summaryType: TabHeaderDataForProcess.YEARLY,
        selectedTabId: sSelectedTabId,
        tabHeaderData: ProcessConfigViewProps.getTabHeaderData(),
        data: oData
      }
    }

    oModel.tabSummary = oTabSummaryData;
  };

  getProcessConfigSectionLayoutModel = () => {
    var oComponentProps = this.getComponentProps();
    var oProcessConfigProps = oComponentProps.processConfigView;
    var oActiveProcess = oProcessConfigProps.getActiveProcess();
    let oAppData = this.getAppData();
    let ProcessFilterProps = oComponentProps.processFilterProps ;
    let aAppliedFilterDataClone = ProcessFilterProps.getAppliedFiltersClone();
    let aAppliedFilterData =  (aAppliedFilterDataClone != null) ? aAppliedFilterDataClone : ProcessFilterProps.getAppliedFilters();
    let sContextType = "process";
    this._setTranslationsToHeaderDataForProcess(oProcessConfigProps);
    //Attribute data
    let oMasterAttributeList = CS.cloneDeep(oAppData.getAttributeList());
    let aMasterAttributeList = CS.values(oMasterAttributeList);
    let oFilterContext ={
      filterType : FilterPropTypeConstants.MODULE,
      screenContext : GridViewContexts.PROCESS
    }
    let oFilterData = {
      appliedFilterDataClone : aAppliedFilterDataClone,
      appliedFilterData : aAppliedFilterData,
      masterAttributeList : aMasterAttributeList,
      filterContext : oFilterContext
    };

    if(oActiveProcess.clonedObject){
      oActiveProcess = oActiveProcess.clonedObject;
    }

    var oModel = {};
    oModel.label = oActiveProcess.label;
    let sSplitter = SettingUtils.getSplitter();
    oModel.code = oActiveProcess.code;
    oModel.eventType = {
      items: MockDataForProcessEventTypes(),
      selectedItems: [oActiveProcess.eventType],
      cannotRemove: true,
      context: sContextType + sSplitter + "eventType",
      disabled: true,
      label: "",
      isMultiSelect: false,
    };

    let aItems = MockDataForProcessTriggeringEvent();
    let aExcludedItems = [];
    if(oActiveProcess.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT){
      aExcludedItems = ["import", "export"];

    }else if(oActiveProcess.eventType === ProcessEventTypeDictionary.INTEGRATION) {
      aExcludedItems = ["afterSave", "afterCreate"];
    }

    let aFilteredItemList = CS.filter(aItems, function (oItem) {
      return !CS.includes(aExcludedItems, oItem.id);
    });

    let bIsScheduledWorkflow = (oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) ? true : false;
    let bIsScheduleOrJms = (oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW || oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW);
    /** triggeringEventType = actionType **/
    oModel.triggeringEventType = this.getTriggeringEventTypeModel(aFilteredItemList, oActiveProcess, sSplitter);
    if (oActiveProcess.configDetails) {
      oModel.klassTypes = this._getLazyMSSModel(oActiveProcess.klassIds, oActiveProcess.configDetails.referencedKlasses, "klasses", "klassTypes", true, sContextType, false);
      oModel.attributes = this._getLazyMSSModelForAttributesAndTags(oActiveProcess.attributeIds, oActiveProcess.configDetails.referencedAttributes, "attributes", sContextType, false);
      oModel.tags = this._getLazyMSSModelForAttributesAndTags(oActiveProcess.tagIds, oActiveProcess.configDetails.referencedTags, "tags", sContextType, false);
      oModel.nonNatureklassTypes = this._getLazyMSSModel(oActiveProcess.nonNatureKlassIds, oActiveProcess.configDetails.referencedNonNatureKlasses , "klasses", "nonNatureklassTypes", false, sContextType, false);
      oModel.organizations = this._getLazyMSSModelForOrganizations(oActiveProcess.organizationsIds, oActiveProcess.configDetails.referencedOrganizations,"organizations", sContextType,!bIsScheduleOrJms);
    }else{
      oModel.klassTypes = this._getLazyMSSModel(oActiveProcess.klassIds, {}, "klasses", "klassTypes", true, sContextType, false);
      oModel.attributes = this._getLazyMSSModelForAttributesAndTags(oActiveProcess.attributeIds, {}, "attributes", sContextType, false);
      oModel.tags = this._getLazyMSSModelForAttributesAndTags(oActiveProcess.tagIds, {}, "tags", sContextType, false);
      oModel.nonNatureklassTypes = this._getLazyMSSModel(oActiveProcess.nonNatureKlassIds, {}, "klasses", "nonNatureklassTypes", false, sContextType, false);
      oModel.organizations = this._getLazyMSSModelForOrganizations(oActiveProcess.organizationsIds, {}, "organizations", sContextType, !bIsScheduleOrJms);
    }
    let oAllowedTaxonomyHierarchyList = {};
    let oMultiTaxonomyDataForProcess = {};
    if (!CS.isEmpty(oActiveProcess)) {
      let aSelectedTaxonomyIds = oActiveProcess.taxonomyIds;
      var oAllowedTaxonomiesById = oProcessConfigProps.getAllowedTaxonomies();
      var oReferencedTaxonomy = oActiveProcess.configDetails && oActiveProcess.configDetails.referencedTaxonomies;
      oAllowedTaxonomyHierarchyList = oProcessConfigProps.getAllowedTaxonomyHierarchyList();
      oMultiTaxonomyDataForProcess = SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomy, oAllowedTaxonomiesById, oAllowedTaxonomyHierarchyList);
    }
    oModel.workflowType = {
      items: MockDataForProcessWorkflowTypes(),
      selectedItems: [oActiveProcess.workflowType],
      cannotRemove: true,
      context: sContextType + sSplitter + "workflowType",
      disabled: true,
      label: "",
      isMultiSelect: false,
    };
    oModel.actionSubType = {
      items: MockDataForProcessActionSubTypes(),
      selectedItems: oActiveProcess.actionSubType,
      cannotRemove: false,
      context: sContextType + sSplitter + "actionSubType",
      disabled: false,
      label: "",
      isMultiSelect: true,
    };
    if(oActiveProcess.eventType && oActiveProcess.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT) {
      oModel.usecases = {
        items: MockDataForProcessUsecases(),
        selectedItems: oActiveProcess.usecases,
        cannotRemove: false,
        context: sContextType + sSplitter + "usecases",
        disabled: false,
        label: "",
        isMultiSelect: true,
      };
    };
    oModel.activation = this.getYesNoPropertyView(oActiveProcess.isExecutable, sContextType);
    oModel.isTemplate = {
      context: sContextType,
      isSelected: oActiveProcess.isTemplate || false
    };
    let aPhysicalCatalogs = CS.filter(MockDataForPhysicalCatalogAndPortal.physicalCatalogs(), function (oItem) {
      return (oItem.id !== PhysicalCatalogDictionary.DATAINTEGRATION);
    });
    oModel.physicalCatalogIds = {
      items: aPhysicalCatalogs,
      selectedItems: oActiveProcess.physicalCatalogIds,
      cannotRemove: false,
      context: sContextType + sSplitter + "physicalCatalogIds",
      disabled: false,
      label: "",
      isMultiSelect: !bIsScheduledWorkflow,
    };
    oModel.taxonomy = this.getSmallTaxonomyView(oMultiTaxonomyDataForProcess, sContextType, false, oAllowedTaxonomyHierarchyList);
    if(bIsScheduledWorkflow){
      oModel.actionSubType.disabled = bIsScheduledWorkflow;
      oModel.nonNatureklassTypes.disabled = bIsScheduledWorkflow;
      oModel.tags.disabled = bIsScheduledWorkflow;
      oModel.attributes.disabled = bIsScheduledWorkflow;
      oModel.klassTypes.disabled = bIsScheduledWorkflow;
      oModel.triggeringEventType.disabled = bIsScheduledWorkflow;
      oModel.taxonomy = this.getSmallTaxonomyView(oMultiTaxonomyDataForProcess, sContextType, true, oAllowedTaxonomyHierarchyList);
    }

    if((oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW ||
        oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.TASKS_WORKFLOW) &&
        oActiveProcess.eventType === ProcessEventTypeDictionary.INTEGRATION){
      oModel.actionSubType.disabled = true;
      oModel.nonNatureklassTypes.disabled = true;
      oModel.tags.disabled = true;
      oModel.attributes.disabled = true;
      oModel.klassTypes.disabled = true;
      oModel.triggeringEventType.disabled = true;
      oModel.taxonomy = this.getSmallTaxonomyView(oMultiTaxonomyDataForProcess, sContextType, true, oAllowedTaxonomyHierarchyList);
    }

    /** WF -> Standard/Task && EventType -> BP && triggerType -> AfterCreate **/
    if((oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW ||
        oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.TASKS_WORKFLOW) &&
        (oActiveProcess.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT) &&
        (oActiveProcess.triggeringType === "afterCreate")){
      oModel.actionSubType.disabled = true;
      oModel.nonNatureklassTypes.disabled = true;
      oModel.tags.disabled = true;
      oModel.attributes.disabled = true;
      oModel.klassTypes.disabled = false;
      oModel.triggeringEventType.disabled = true;
      oModel.taxonomy = this.getSmallTaxonomyView(oMultiTaxonomyDataForProcess, sContextType, true, oAllowedTaxonomyHierarchyList);
    }

    /** WF -> Standard/Task && EventType -> BP && triggerType -> AfterSave **/
    if ((oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW ||
        oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.TASKS_WORKFLOW) &&
        (oActiveProcess.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT) &&
        (oActiveProcess.triggeringType === "afterSave")) {
      if ((CS.includes(oActiveProcess.actionSubType, MockDataForProcessActionTypesDictionary.AFTER_PROPERTIES_SAVE)) ||
          CS.isEmpty(oActiveProcess.actionSubType)) {
        oModel.actionSubType.disabled = false;
        oModel.nonNatureklassTypes.disabled = false;
        oModel.tags.disabled = false;
        oModel.attributes.disabled = false;
        oModel.klassTypes.disabled = false;
        oModel.triggeringEventType.disabled = true;
        oModel.taxonomy = this.getSmallTaxonomyView(oMultiTaxonomyDataForProcess, sContextType, false, oAllowedTaxonomyHierarchyList);
      }
      else if ((CS.includes(oActiveProcess.actionSubType, MockDataForProcessActionTypesDictionary.AFTER_CLASSIFICATION_SAVE))) {
        oModel.actionSubType.disabled = false;
        oModel.nonNatureklassTypes.disabled = false;
        oModel.tags.disabled = true;
        oModel.attributes.disabled = true;
        oModel.klassTypes.disabled = false;
        oModel.triggeringEventType.disabled = true;
        oModel.taxonomy = this.getSmallTaxonomyView(oMultiTaxonomyDataForProcess, sContextType, false, oAllowedTaxonomyHierarchyList);
      }
      else if ((CS.includes(oActiveProcess.actionSubType, MockDataForProcessActionTypesDictionary.AFTER_RELATIONSHIP_SAVE)) ||
          (CS.includes(oActiveProcess.actionSubType, MockDataForProcessActionTypesDictionary.AFTER_CONTEXT_SAVE))) {
        oModel.actionSubType.disabled = false;
        oModel.nonNatureklassTypes.disabled = true;
        oModel.tags.disabled = true;
        oModel.attributes.disabled = true;
        oModel.klassTypes.disabled = false;
        oModel.triggeringEventType.disabled = true;
        oModel.taxonomy = this.getSmallTaxonomyView(oMultiTaxonomyDataForProcess, sContextType, true, oAllowedTaxonomyHierarchyList);
      }
    }

    if (oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW) {
      oModel.ip = oActiveProcess.ip || "";
      oModel.port = oActiveProcess.port || "";
      oModel.queue = oActiveProcess.queue || "";
    }

    this._generateTabSummaryData(oActiveProcess,oModel);

  //todo remove old Component code
    let oCustomPropertiesData = {
      destinationCatalogId: MockDataForPhysicalCatalogAndPortal.physicalCatalogs(),
      isAdvancedOptionsEnabled: oProcessConfigProps.getIsAdvancedOptionsEnabled(),
      referencedTaxonomiesForComponent: oProcessConfigProps.getReferencedTaxonomiesForComponent(),
      oSmallTaxonomyPaginationData: oProcessConfigProps.getBPMNTaxonomyPaginationData(),
      filterData: oFilterData
    };

    let bIsFullScreenMode = oProcessConfigProps.getIsFullScreenMode();

    let oView = (
        <div className="processGraphContainer">
          <ConfigBPMNWrapperView xmlData={oActiveProcess.processDefinition}
                                 activeProcess={oProcessConfigProps.getActiveProcess()}
                                 customPropertiesData={oCustomPropertiesData}
                                 isUploadWindowActive={oProcessConfigProps.getIsUploadWindowActive()}
                                 shouldImportBPMNXML={oProcessConfigProps.getShouldImportXML()}
                                 isFullScreenMode={bIsFullScreenMode}
                                 taxonomyData={oProcessConfigProps.getAllowedTaxonomiesInProcessConfig()}
                                 validationInfo={oProcessConfigProps.getValidationInfo()}
          />
            </div>
        );

    oModel.graph = (
        <ProcessConfigFullScreenWrapperView
            header={getTranslation().GRAPH}
            showHeader={false}
            bodyView={oView}
            isFullScreenMode={bIsFullScreenMode}
        />
    );

    return oModel;
  };

  getSmallTaxonomyView = (oMultiTaxonomyDataForProcess, sContext, bIsDisabled, oAllowedTaxonomyHierarchyList) => {
    var oComponentProps = this.getComponentProps();
    var oProcessConfigProps = oComponentProps.processConfigView;
    let oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oMultiTaxonomyDataForProcess);
    return (
        <div className='goldenRecordMatchEntitiesWrapper'>
          <SmallTaxonomyView model={oSmallTaxonomyViewModel}
                             isDisabled={bIsDisabled}
                             context={sContext}
                             showAllComponents={true}
                             // localSearch={true}
                             paginationData={oProcessConfigProps.getTaxonomyPaginationData()}
                             allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}
          />
        </div>)
  };

  getSmallTaxonomyViewModel = (oMultiTaxonomyData) => {
    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    var sRootTaxonomyId = ViewUtils.getTreeRootId();
    var oProperties = {};
    oProperties.headerPermission = {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };

  /** Function to get BaseType based on type **/
  _getTypesBasedOnEntityTypes = (sEntityType) => {
    let aMockDataForProcessEntityTypes = MockDataForProcessEntityTypes();
    let oFoundEntityType = CS.find(aMockDataForProcessEntityTypes, {id:sEntityType});
    if(CS.isNotEmpty(oFoundEntityType)){}
    return (CS.isNotEmpty(oFoundEntityType)) ? [oFoundEntityType.type] : [];
  };

  _getLazyMSSModel = (aSelectedItems, oReferencedKlasses, sContext, sKey, bIsNature, sContextType, bIsDisabled) => {
    let oReqResObj = {
      requestType: "configData",
      entityName: sContext
    };
    let bIsMultiSelect = true;
    let bCannotRemove = false;
    let aTypes = [MockDataForEntityBaseTypesDictionary.articleKlassBaseType];
    let aTypesToExclude = [];
    if (sContext === 'klasses') {
      let sEntityType = ProcessConfigViewProps.getEntityType();
      let aTypes = this._getTypesBasedOnEntityTypes(sEntityType);
      if(sContextType === "profile"){
        aTypes = [MockDataForEntityBaseTypesDictionary.articleKlassBaseType,MockDataForEntityBaseTypesDictionary.assetKlassBaseType];
        aTypesToExclude = [NatureTypeDictionary.GTIN, NatureTypeDictionary.EMBEDDED, NatureTypeDictionary.LANGUAGE, NatureTypeDictionary.TECHNICAL_IMAGE];
      } else if(sContextType === "process"){
        aTypesToExclude = [NatureTypeDictionary.SUPPLIER];
      }
      oReqResObj = {
        requestType: "customType",
        requestURL: "config/klasseslistbybasetype",
        responsePath: ["success", "list"],
        entityName: sContext,
        customRequestModel: {
          isNature: bIsNature,
          types: aTypes,
          typesToExclude: aTypesToExclude
        }
      };
    }

    let sSplitter = SettingUtils.getSplitter();
    let sContextUpdated = sContextType + sSplitter + "klassTypes";
    if(sKey === "nonNatureklassTypes"){
      sContextUpdated = sContextType + sSplitter + "nonNatureklassTypes";
    }
    return {
      isMultiSelect: bIsMultiSelect,
      context: sContextUpdated,
      disabled: bIsDisabled,
      selectedItems: aSelectedItems,
      cannotRemove: bCannotRemove,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
      isLoadMoreEnabled: false,
      referencedData: oReferencedKlasses,
      bShowIcon: true
    };
  };
    _getLazyMSSModelForAttributesAndTags = function(aSelectedItems, oReferencedData, sContext, sContextType, bIsDisabled) {
        let oReqResObj = {
            requestType: "configData",
            entityName: sContext
        };
        let bIsMultiSelect = true;
        let bCannotRemove = false;

        if (sContext === 'attributes') {
          oReqResObj.typesToExclude = [AttributeTypeDictionary.CALCULATED,AttributeTypeDictionary.CONCATENATED];
        }

        let sSplitter = SettingUtils.getSplitter();
        return {
            isMultiSelect: bIsMultiSelect,
            context: sContextType + sSplitter + sContext,
            disabled: bIsDisabled,
            selectedItems: aSelectedItems,
            cannotRemove: bCannotRemove,
            showSelectedInDropdown: true,
            requestResponseInfo: oReqResObj,
            isLoadMoreEnabled: false,
            referencedData: oReferencedData,
        };
    };

  /** To Prepare Lazy MSS model for Organization **/
  _getLazyMSSModelForOrganizations = function (aSelectedItems, oReferencedData, sContext, sContextType, bIsMultiSelect) {
    let oReqResObj = {
      requestType: "customType",
      requestURL: "config/organizations/getall",
      responsePath: ["success", "list"],
      customRequestModel: {},
    };

    let sSplitter = SettingUtils.getSplitter();
    return {
      isMultiSelect: bIsMultiSelect,
      context: sContextType + sSplitter + sContext,
      disabled: false,
      selectedItems: aSelectedItems,
      cannotRemove: false,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
      isLoadMoreEnabled: false,
      referencedData: oReferencedData,
    };
  };

  updateTagChildrenInTagGroup = (oLoadedTags, aAppliedFilterData) => {
    CS.forEach(oLoadedTags, function (oTag) {
      let oTagFromFilterData = CS.find(aAppliedFilterData, {id: oTag.id});
      /** Here we can get tag group or tag child but we need to add children only in tag group not in tag's children **/
      if(oTagFromFilterData) {
        oTag.children = oTagFromFilterData.children;
      }
    });
  };

  _prepareDataForSearchFilterDialog = () => {
      let _this = this;
      let oAppData = this.getAppData();
      let oComponentProps = this.getComponentProps();
      let oScreenProps = oComponentProps.screen;
      let sSearchInput = oScreenProps.getEntitySearchText();
      let oProcessConfigProps = oComponentProps.processConfigView;
      let ProcessFilterProps = oComponentProps.processFilterProps ;

      let aAppliedFilterDataClone = ProcessFilterProps.getAppliedFiltersClone();
      let aAppliedFilterData =  (aAppliedFilterDataClone != null) ? aAppliedFilterDataClone : ProcessFilterProps.getAppliedFilters();

      //Attribute data
      let oMasterAttributeList = CS.cloneDeep(oAppData.getAttributeList());
      let aMasterAttributeList = CS.values(oMasterAttributeList);
      let aNonSelectedAttributeList = [];
      CS.forEach(aMasterAttributeList, function (oMasterAttribute) {
        var oFound = CS.find(aAppliedFilterData, {id: oMasterAttribute.id});
        if(CS.isEmpty(oFound) && !_this.isFromExcludedAttributeTypes(oMasterAttribute.type)){
          aNonSelectedAttributeList.push(oMasterAttribute);
        }
      });

      //Tag Data
      let oMasterTagList = CS.cloneDeep(oAppData.getTagMap());
      let aMasterTagList = CS.values(oMasterTagList);
      let aNonSelectedTagList = [];
      CS.forEach(aMasterTagList, function (oMasterTag) {
        let oFound = CS.find(aAppliedFilterData, {id: oMasterTag.id});
        if(CS.isEmpty(oFound)){
          aNonSelectedTagList.push(oMasterTag);
        }
      });

      let oAppliedFilterCollapseStatusMap = ProcessFilterProps.getAppliedFilterCollapseStatusMap();
      let oLoadedAttributes = oProcessConfigProps.getLoadedAttributes();
      let oLoadedTags = oProcessConfigProps.getLoadedTags();

      this.updateTagChildrenInTagGroup(oLoadedTags, aAppliedFilterData);

      return {
        advancedSearchPanelData: {
          attributeList: aNonSelectedAttributeList,
          tagList: aNonSelectedTagList,
          appliedFilterCollapseStatusMap: oAppliedFilterCollapseStatusMap,
          searchInput: sSearchInput,
          loadedAttributes: oLoadedAttributes,
          loadedTags: oLoadedTags,
          isFilterDirty: ProcessFilterProps.getIsFilterDirty(),
        },
        appliedFilterDataClone : aAppliedFilterDataClone,
        appliedFilterData : aAppliedFilterData,
        masterAttributeList : aMasterAttributeList,

      };
    };

  isFromExcludedAttributeTypes = (sAttrType) => {
    return (
        SettingUtils.isImageCoverflowAttribute(sAttrType) ||
        SettingUtils.isAttributeTypeType(sAttrType) ||
        SettingUtils.isAttributeTypeFile(sAttrType) ||
        SettingUtils.isAttributeTypeSecondaryClasses(sAttrType) ||
        SettingUtils.isAttributeTypeTaxonomy(sAttrType)
    );
  };

  _prepareDataForSearchCriteriaDialog = (oProcessFilterProps) => {
    let oFilterContext = {
      filterType: "module",
      screenContext: "module"
    };
    let oTaxonomyTreeRequestData = {
      url: "runtime/klasstaxonomytree/get"
    };

    let oSearchCriteriaData = {
      filterContext: oFilterContext,
      taxonomyTreeRequestData: oTaxonomyTreeRequestData,
      taxonomyVisualProps: oProcessFilterProps.getTaxonomyVisualProps(),
      taxonomyTree: oProcessFilterProps.getTaxonomyTreeData()
    };

    return oSearchCriteriaData
  };

  getProcessView = () => {
    let oComponentProps = this.getComponentProps();
    let bShowCheckboxColumn = true;
    let bIsStandardAttributeScreen = false;

    let oSearchFilterComponentData = {};
    let oAppData = SettingUtils.getAppData();
    let oProcessConfigProps = oComponentProps.processConfigView;
    let oProcessFilterProps = oComponentProps.processFilterProps;
    var oActiveProcess = oProcessConfigProps.getActiveProcess();
    let bIsProcessDirty = false;
    let bIsDialogOpen = oProcessConfigProps.getIsProcessDialogActive();
    let bIsSearchFilterComponentDialogOpen = oProcessFilterProps.getAdvancedSearchPanelShowStatus();
    if(bIsSearchFilterComponentDialogOpen){
      oSearchFilterComponentData =  this._prepareDataForSearchFilterDialog();
    }

    if (oActiveProcess.clonedObject) {
      oActiveProcess = oActiveProcess.clonedObject;
      bIsProcessDirty = true;
    }

    var oActionBarList = oAppData.getActionBarList();
    var oActionItemModel = {};
    var oProcessConfigView = oActionBarList.ProcessConfigView;
    oActionItemModel.ProcessListView = oProcessConfigView.ProcessConfigListView;
    oActionItemModel.ProcessDetailedView = [this.getItemList([])];
    var aCustomComponentList = oAppData.getAllCustomComponentList();
    let bEnableCopyButton = true;
    let bSaveAsDialogActive = oProcessConfigProps.getIsSaveAsDialogActive();
    let aDataForSaveAsDialog = oProcessConfigProps.getDataForSaveAsDialog();
    let aDuplicateCodes = oProcessConfigProps.getCodeDuplicates();
    let aGridWFValidationErrorList = oProcessConfigProps.getGridWFValidationErrorList();
    let oWorkflowFilterData = {};
    oWorkflowFilterData.workflowType = oProcessConfigProps.getWorkflowType();
    oWorkflowFilterData.entityType = oProcessConfigProps.getEntityType();
    oWorkflowFilterData.showEntityType = oProcessConfigProps.getEntityTypeDisabled();
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };
    let oSearchCriteriaData = {};
    oSearchCriteriaData = this._prepareDataForSearchCriteriaDialog(oProcessFilterProps);
    oProcessFilterProps.setSearchCriteriaData(oSearchCriteriaData);
    let bIsSearchCriteriaDialogOpen = oProcessFilterProps.getSearchCriteriaDialogShowStatus();

    return (<ProcessConfigView
        context={GridViewContexts.PROCESS}
        showCheckboxColumn={bShowCheckboxColumn}
        disableDeleteButton={bIsStandardAttributeScreen}
        enableImportExportButton={false}
        disableCreate={bIsStandardAttributeScreen}
        activeProcess={oActiveProcess}
        isProcessDirty={bIsProcessDirty}
        isDialogOpen={bIsDialogOpen}
        isSearchFilterComponentDialogOpen = {bIsSearchFilterComponentDialogOpen}
        searchFilterComponentData = {oSearchFilterComponentData}
        enableCopyButton = {bEnableCopyButton}
        isSaveAsDialogOpen = {bSaveAsDialogActive}
        dataForSaveAsDialog = {aDataForSaveAsDialog}
        duplicateCodes = {aDuplicateCodes}
        gridWFValidationErrorList = {aGridWFValidationErrorList}
        workflowFilterData = {oWorkflowFilterData}
        actionItemModel={oActionItemModel}
        componentList={aCustomComponentList}
        sectionLayoutModel={this.getProcessConfigSectionLayoutModel()}
        columnOrganizerData={oColumnOrganizerData}
        filterData={this.getProcessFilterData()}
        isSearchCriteriaDialogOpen = {bIsSearchCriteriaDialogOpen}
        searchCriteriaData = {oSearchCriteriaData}
    />);
  };

  /** Function to get Process Filter Data **/
  getProcessFilterData = () => {
    let _this = this;
    let oComponentProps = _this.getComponentProps();
    let oProcessConfigProps = oComponentProps.processConfigView;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.PROCESS);
    let oGridViewSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
    let aAppliedFilters = oProcessConfigProps.getAppliedFilterData();
    let aSearchFilterData = oProcessConfigProps.getSearchFilterData();
    let aAvailableFilterData = [];
    let aAvailableSearchData = [];
    let aAppliedFilterData = [];

    CS.forEach(oGridViewSkeleton.scrollableColumns, function (oData) {
      if (oData.isFilterable) {
        let oObject = {
          id: oData.requestId,
          label: oData.label,
          filterViewType: oData.type,
          children: [],
        };
        _this.fillAvailableFilterProcessChildrenData(oData.id, oObject);
        aAvailableFilterData.push(oObject);
      }

      if (oData.isSearchable) {
        let oSearchData = CS.find(aSearchFilterData, {id: oData.id});
        let oObject = {
          id: oData.id,
          label: oData.label,
          value: oSearchData ? oSearchData.value : "",
          type: oData.type,
        };
        aAvailableSearchData.push(oObject);
      }
    });

    CS.forEach(aAppliedFilters, function (oFilter) {
      let oColumnData = CS.find(oGridViewSkeleton.scrollableColumns, {requestId: oFilter.id});
      let oObject = {
        id: oFilter.id,
        label: oColumnData.label,
        type: oFilter.type,
        children: [],
      };
      _this.fillAppliedFilterProcessChildrenData(oFilter.id, oObject);
      aAppliedFilterData.push(oObject);
    });

    let oFilterContext = {
      filterType: FilterPropTypeConstants.MODULE,
      screenContext: GridViewContexts.PROCESS,
    };

    return {
      availableFilterData: aAvailableFilterData,
      searchFilterData: aAvailableSearchData,
      appliedFilterData: aAppliedFilterData,
      appliedFilterClonedData: oProcessConfigProps.getAppliedFilterClonedData() || oProcessConfigProps.getAppliedFilterData(),
      filterContext: oFilterContext,
      isFilterExpanded: oProcessConfigProps.getIsFilterExpanded(),
      horizontalSliderForAppliedFilter: true,
      isDirty: oProcessConfigProps.getIsFilterDirty(),
      showDefaultIconForAvailableAndAppliedFilter: false
    }
  };

  /** Function to fill Applied Filter for Process filter**/
  fillAppliedFilterProcessChildrenData = (sId, oObject) => {
    let oProcessConfigProps = this.getComponentProps().processConfigView;
    let aAppliedFilters = oProcessConfigProps.getAppliedFilterData();
    let aChildren = CS.find(aAppliedFilters, {id: sId}).children;
    let aChildrenData = [];

    CS.forEach(aChildren, function (oChild) {
      let oElement = ProcessFiltersValues[sId][oChild.id];
      let oChildData = {
        id: oElement.id,
        label: getTranslation()[oElement.label] || oElement.label,
      };
      aChildrenData.push(oChildData);
    });
    oObject.children = aChildrenData;
  };

  /** Function to fill Available Filter for Process**/
  fillAvailableFilterProcessChildrenData = (sId, oObject) => {
    let oProcessConfigProps = this.getComponentProps().processConfigView;
    let oAvailableFiltersSearchData = oProcessConfigProps.getAvailableFilterSearchData();
    let aChildren = [];

    CS.forEach(ProcessFiltersValues[sId], function (oActivity) {
      if (CS.isEmpty(oAvailableFiltersSearchData[sId]) || CS.includes((getTranslation()[oActivity.label]).toLocaleLowerCase(), (oAvailableFiltersSearchData[sId]).toLocaleLowerCase())) {
        aChildren.push({
          id: oActivity.id,
          label: getTranslation()[oActivity.label] || oActivity.label,
          ignoreCount: true,
        });
      }
    });
    oObject.children = aChildren;
  };

  getAuthorizationView = () => {
    let oComponentProps = this.getComponentProps();
    let oAuthorizationMappingProps = oComponentProps.authorizationMappingConfigView;
    let bIsDialogOpen = oAuthorizationMappingProps.getIsAuthorizationMappingDialogActive();
    let bShowCheckboxColumn = true;
    let bIsStandardAttributeScreen = false;
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    let oActiveAuthorizationMapping = oAuthorizationMappingProps.getSelectedMapping();
    let bIsAuthorizationMappingDirty = false;
    if (oActiveAuthorizationMapping.clonedObject) {
      oActiveAuthorizationMapping = oActiveAuthorizationMapping.clonedObject;
      bIsAuthorizationMappingDirty = true;
    }

    return <AuthorizationConfigView
        sectionLayoutModel={this.getAuthorizationMappingConfigSectionLayoutModel()}
        context={GridViewContexts.AUTHORIZATION_MAPPING}
        showCheckboxColumn={bShowCheckboxColumn}
        disableDeleteButton={bIsStandardAttributeScreen}
        enableImportExportButton={false}
        disableCreate={bIsStandardAttributeScreen}
        activeAuthorizationMapping={oActiveAuthorizationMapping}
        isAuthorizationMappingDirty={bIsAuthorizationMappingDirty}
         isDialogOpen={bIsDialogOpen}
    />;
  };

  getSelectedRelationshipViewModel = () => {
    var oAppData = SettingUtils.getAppData();
    var oComponentProps = this.getComponentProps();
    let oRelationshipProps = oComponentProps.relationshipView;
    var oSelectedRelationship = CS.cloneDeep(oRelationshipProps.getSelectedRelationship());
    var oPaginationData = oComponentProps.screen.getEntitiesPaginationData();
    let aCustomTabList = oAppData.getCustomTabList();
    let aCustomTypeList = new MockDataForRelationshipTypeList();

    var oProperties = {};
    if (oSelectedRelationship.isDirty || oSelectedRelationship.clonedObject) {
      oSelectedRelationship = oSelectedRelationship.clonedObject;
    }
    oProperties.isEmpty = CS.isEmpty(oSelectedRelationship);
    var oSide = {};
    if (!CS.isEmpty(oSelectedRelationship)) {
      var oAttributeMap = oAppData.getAttributeList();
      oProperties.tagList = oAppData.getTagList()[0].children;
      oProperties.attributeList = CS.map(oAttributeMap);
      oProperties.isCreated = oSelectedRelationship.isCreated;
      oProperties.code = oSelectedRelationship.code;

      var oSide1 = oSelectedRelationship.side1;
      var oSide2 = oSelectedRelationship.side2;
      oSide.side1 = oSide1;
      oSide.side2 = oSide2;


      oProperties.cardinalityDropDown = [
        {id: RelationshipConstants.ONE, label: getTranslation().DROP_DOWN_CARDINALITY_ONE},
        {id: RelationshipConstants.MANY, label: getTranslation().DROP_DOWN_CARDINALITY_MANY}
      ];
      oProperties.selectedRelationship = oSelectedRelationship;
      oProperties.customTabList = aCustomTabList;
      oProperties.customTypeList = aCustomTypeList;
      oProperties.selectedCustomTabObject = oSelectedRelationship.tab;
      oProperties.customTabPagniationData = oPaginationData.tab;
      oProperties.icon = oSelectedRelationship.icon;
      oProperties.configDetails = oRelationshipProps.getSelectedRelationshipConfigDetails();
      oProperties.isKlassListNatureType = undefined;
    }

    return new ConfigRelationshipViewModel(oSelectedRelationship.id,
        oSelectedRelationship.label,
        oSelectedRelationship.tooltip,
        oSide,
        oProperties
    )
  };

  getAvailableSectionListModelForClass = () => {
    var oClassConfigViewProps = this.getComponentProps().classConfigView;
    var oSectionMap = oClassConfigViewProps.getClassSectionMap();
    var sSearchText = oClassConfigViewProps.getSectionAvailableElementSearchText().toLocaleLowerCase();
    var aAvailableSectionListModelForClass = [];
    var oActiveClass = oClassConfigViewProps.getActiveClass();
    CS.forEach(oSectionMap, function (oAvailabilityObject) {
      var oSection = oAvailabilityObject.element;
      let sSectionLabel = CS.getLabelOrCode(oSection);
      var sAttributeLabelLowerCase = sSectionLabel.toLocaleLowerCase();
      var bIsValidInSearch = sSearchText == "" ? true : (sAttributeLabelLowerCase.indexOf(sSearchText) != -1);
      if (oActiveClass.type == MockDataForEntityBaseTypesDictionary.assetKlassBaseType && SettingUtils.isAttributeTypeCoverflow(oSection.type)) {
        return;
      }

      if (CS.find(oActiveClass.sections, {propertyCollectionId: oSection.id}) || oSection.isForXRay) {
        return;
      }

      if (!oSection.isForXRay && oAvailabilityObject.isAvailable && bIsValidInSearch) {

        var oProperties = {};
        oProperties.selectedScreenName = 'classAttribute';
        oProperties.attributeData = oSection;

        aAvailableSectionListModelForClass.push(
            new ContextMenuViewModel(
                oSection.id,
                sSectionLabel,
                false,
                "",
                {}
            ));
      }
    });

    return aAvailableSectionListModelForClass;
  };

  getAllMasterEntitiesForSections = (oActiveClass) => {
    var aClasses = [];
    if (oActiveClass.id == SystemLevelId.ArticleKlassId) {
      aClasses = this.getAppData().getAllClassesFlatList()
    } else if (oActiveClass.id == SystemLevelId.CollectionKlassId) {
      aClasses = this.getAppData().getAllCollectionClassesFlatList()
    } else if (oActiveClass.id == SystemLevelId.SetKlassId) {
      aClasses = this.getAppData().getAllSetClassesFlatList()
    } else if (oActiveClass.id == SystemLevelId.TaskId) {
      aClasses = this.getAppData().getAllTaskClassesFlatList()
    } else if (oActiveClass.id == SystemLevelId.AssetId) {
      aClasses = this.getAppData().getAllAssetClassesFlatList()
    } else if (oActiveClass.id == SystemLevelId.AssetCollectionId) {
      aClasses = this.getAppData().getAllAssetCollectionClassesFlatList()
    } else if (oActiveClass.id == SystemLevelId.MarketTargetId) {
      aClasses = this.getAppData().getAllMarketTargetClassesFlatList()
    } else if (oActiveClass.id == SystemLevelId.EditorialId) {
      aClasses = this.getAppData().getAllEditorialClassesFlatList()
    } else if (oActiveClass.id == SystemLevelId.EditorialCollectionId) {
      aClasses = this.getAppData().getAllEditorialCollectionClassesFlatList()
    }
    var sEntitySearchText = this.getComponentProps().screen.getEntitySearchText();

    return {
      attributes: this.getAppData().getAttributeList(),
      tags: SettingUtils.rejectStatusAndLifecycleTagsTypes(this.getAppData().getTagList()[0].children),
      relationships: this.getAppData().getRelationshipMasterList(),
      classes: aClasses,
      entitySearchText: sEntitySearchText
    }
  };

  isMajorTaxonomy = (oTaxonomy) => {
    switch (oTaxonomy.taxonomyType) {
      case "majorTaxonomy":
        return true;
      case "minorTaxonomy":
        return false;
      default:
        if (oTaxonomy.parent.id == -1) {
          return false;
        }
        return this.isMajorTaxonomy(oTaxonomy.parent);
    }
  };

  getAddedClassificationList = (oParent, aSelectedClassification) => {
    if (oParent.id == SettingUtils.getTreeRootId()) {
      return;
    }
    !CS.isEmpty(oParent.parent) && this.getAddedClassificationList(oParent.parent, aSelectedClassification);
    aSelectedClassification.push(oParent);
  };

  prepareReferencedTagMap = (aTagValues) => {
    let oTagValuMap = {};
    CS.forEach(aTagValues,function (oTagValue) {
      oTagValuMap[oTagValue.id] = oTagValue;
    });
    return oTagValuMap;
  };

  getKPIDetailData = () => {
    let _this = this;
    let oKPIConfigProps = this.getComponentProps().kpiConfigView;
    if (!oKPIConfigProps.getIsKPIDialogActive()) {
      return;
    }
    let oActiveKPI = oKPIConfigProps.getActiveKPI();
    oActiveKPI = oActiveKPI.clonedObject || oActiveKPI;
    let oAppData = SettingUtils.getAppData();
    let aUserListForMSS = [];
    let aUserList = oAppData.getUserList();
    CS.forEach(aUserList, function (oUser) {
      aUserListForMSS.push({
        id: oUser.id,
        label: SettingUtils.getUserFullName(oUser)
      });
    });
    let oReferencedRoles = oAppData.getKpiData().referencedRoles;
    let oReferencedData = oKPIConfigProps.getReferencedData();
    let oReferencedTags = oReferencedData.referencedTags;
    let oReferencedUniquenessBlock = CS.find(oReferencedData.referencedRules,{type:"uniquenessBlock"});
    let oReferencedRules = oReferencedData.referencedRules;
    let aDataGovernanceTasks = oAppData.getDataGovernanceTasksList();
    let oSelectedTagMap = oKPIConfigProps.getSelectedTagMap();
    let aSelectedTagsData = [];
    let aSelectedDrillDownTags = oKPIConfigProps.getSelectedDrillDownTags();
    let aSelectedTaxonomyLabels = [];
    let aColorList = CS.map(MockColors, function (sVal, sKey) {
      return {
        id: sKey,
        label: sVal,
        color: sVal
      };
    });
    CS.forEach(oReferencedData.referencedTaxonomies, function (oTaxonomy) {
      aSelectedTaxonomyLabels.push(CS.getLabelOrCode(oTaxonomy))
    });

    CS.forEach(oActiveKPI.kpiTags, function (oTag) {
      let oTagToShow = {};
      let oReferencedTag = oReferencedTags[oTag.tagId];
      let oReferencedTagValueMap = {};
      if (CS.isEmpty(oReferencedTag)) {
        oReferencedTag = oSelectedTagMap[oTag.tagId];
      }
      let aTagValues = [];
      let aReferencedTagValues = oReferencedTag.children;
      oReferencedTagValueMap = _this.prepareReferencedTagMap(aReferencedTagValues);
      let oCurrentTagValueSelection = {};
      CS.forEach(oTag.tagValues, function (sTagValueId) {
        aTagValues.push({
          tagValueId: sTagValueId,
          label: CS.getLabelOrCode(oReferencedTagValueMap[sTagValueId]),
          isSelected: true
        });
        oCurrentTagValueSelection[sTagValueId] = true;
      });
      CS.forEach(oReferencedTagValueMap, function (oReferencedTagValue, sReferencedTagValueId) {
        if (!oCurrentTagValueSelection[sReferencedTagValueId])
          aTagValues.push({
            tagValueId: sReferencedTagValueId,
            label: CS.getLabelOrCode(oReferencedTagValue),
            isSelected: false
          });
      });
      oTagToShow.tagId = oReferencedTag.id;
      oTagToShow.label = CS.getLabelOrCode(oReferencedTag);
      oTagToShow.tagValues = aTagValues;
      aSelectedTagsData.push(oTagToShow);
    });
    let oFilterData = oActiveKPI.targetFilters;
    var aSelectedTaxonomyIds = oFilterData.taxonomyIds;
    var oReferencedTaxonomy = oReferencedData.referencedTaxonomies;
    var oAllowedTaxonomiesById = oKPIConfigProps.getAllowedTaxonomies();
    let oAllowedTaxonomyHierarchyList = oKPIConfigProps.getAllowedTaxonomyHierarchyList();
    let oMultiTaxonomyData = SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomy, oAllowedTaxonomiesById, oAllowedTaxonomyHierarchyList);

    return {
      selectedDrillDownTags: aSelectedDrillDownTags,
      selectedTags: aSelectedTagsData,
      multiTaxonomyData: oMultiTaxonomyData,
      userList: aUserListForMSS,
      referencedRoles: oReferencedRoles,
      uniquenessBlock:oReferencedUniquenessBlock,
      referencedRules: oReferencedRules,
      referencedKlasses: oReferencedData.referencedKlasses,
      referencedEndpoints: oReferencedData.referencedEndpoints,
      referencedOraganizations: oReferencedData.referencedOraganizations || oReferencedData.referencedOrganizations,
      dataGovernanceTasks: aDataGovernanceTasks,
      colorList: aColorList,
      selectedTaxonomyLabels: aSelectedTaxonomyLabels
    };
  };

  getBreadCrumbView = () => {
    let aBreadCrumbPath = this.getComponentProps().breadCrumbProps.getBreadCrumbData();
    if(CS.isEmpty(aBreadCrumbPath)) {
      return null;
    }
    return (
        <div className="breadcrumbSection">
          <BreadcrumbView breadcrumbPath={aBreadCrumbPath} isDisabled={true}/>
        </div>
    );
  };

  getLeftSectionData = (sSelectedTabId) => {
    let oScreenProps = this.getComponentProps().screen;
    let aNestedItemsTabsList = [ConfigModulesDictionary.DATA_MODEL, ConfigModulesDictionary.TRANSLATION_CONFIGURATION];

    return {
      linkItemsData: oScreenProps.getLeftNavigationTreeData(),
      selectedItemId: oScreenProps.getSelectedLeftNavigationTreeItem(),
      isNested: aNestedItemsTabsList.includes(sSelectedTabId),
      leftNavigationTreeValuesMap: oScreenProps.getLeftNavigationTreeValuesMap()
    };
  };


  getThemeConfigurationView = () => {
    let oActionGroups = SettingUtils.getAppData().getActionBarList();
    let oAdminConfigurationActionGroups = oActionGroups.AdminConfigurationConfigView;
    let oThemeConfigurationViewProps = this.getComponentProps().themeConfigurationProps;
    let oThemeConfigurationData = oThemeConfigurationViewProps.getThemeConfigurationData();
    let oThemeConfigurationModelData = new ThemeConfigurationViewModel(CS.isNotEmpty(oThemeConfigurationData.clonedObject) ? oThemeConfigurationData.clonedObject : oThemeConfigurationData);

    return <ThemeConfigurationView
        showSaveDiscardButtons={oThemeConfigurationData.isDirty}
        themeConfigurationModelData={oThemeConfigurationModelData}
        headerActionGroup={oAdminConfigurationActionGroups.ThemeConfigurationConfigView}
    />;
  };

    getViewConfigurationView = () => {
    let oActionGroups = SettingUtils.getAppData().getActionBarList();
    let oAdminConfigurationActionGroups = oActionGroups.AdminConfigurationConfigView;
    let oViewConfigurationViewProps = this.getComponentProps().viewConfigurationProps;
    let oViewConfigurationData = oViewConfigurationViewProps.getViewConfigurationData();
    let oViewConfigurationDataModel = CS.isNotEmpty(oViewConfigurationData.clonedObject) ? oViewConfigurationData.clonedObject : oViewConfigurationData;
    let oViewConfigurationModelData = new ViewConfigurationViewModel(oViewConfigurationDataModel);

    return <ViewConfigurationView
        showSaveDiscardButtons={oViewConfigurationData.isDirty}
        viewConfigurationModelData={oViewConfigurationModelData}
        headerActionGroup={oAdminConfigurationActionGroups.ViewConfigurationConfigView}
    />;
  };


  getDownloadTrackerConfigView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let oFilterData = this.getDownloadTrackerFilterData();
    let oColumnOrganizerData = {
      selectedColumns: ColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: ColumnOrganizerProps.getIsDialogOpen()
    };

    let oGridViewProps = {
      gridViewData: oScreenProps.getGridViewData(),
      totalItems: oScreenProps.getGridViewTotalItems(),
      gridViewSkeleton: oScreenProps.getGridViewSkeleton(),
      gridPaginationData: oScreenProps.getGridViewPaginationData(),
      sortBy: oScreenProps.getGridViewSortBy(),
      sortOrder: oScreenProps.getGridViewSortOrder(),
      filterData: oFilterData,
    };

    return (<DownloadTrackerConfigView
        gridViewProps={oGridViewProps}
        columnOrganizerData={oColumnOrganizerData}
    />);
  };

  getDownloadTrackerFilterData = () => {
    let oScreenProps = this.getComponentProps().screen;
    let oDownloadTrackerProps = this.getComponentProps().downloadLogProps;
    let oGridVewSkeleton = oScreenProps.getGridViewSkeleton();
    let aAppliedFilters = oDownloadTrackerProps.getAppliedFilterData();
    let aSearchFilterData = oDownloadTrackerProps.getIsSearchDirty() ? oDownloadTrackerProps.getSearchFilterClonedData() :
                            oDownloadTrackerProps.getSearchFilterData();
    let _this = this;
    let aAppliedFilterData = [];
    let aAvailableFilterData = [];
    let aAvailableSearchData = [];

    CS.forEach(CS.concat(oGridVewSkeleton.fixedColumns, oGridVewSkeleton.scrollableColumns), function (oData) {
      if (oData.isFilterable){
        let oObject = {
          id : oData.id,
          label : oData.label,
          filterViewType: oData.type,
          children : [],
        };
        _this.fillAvailableFilterChildrenDataForDownloadTracker(oData.id, oObject);
        aAvailableFilterData.push(oObject);
      }

      if (oData.isSearchable){
        let oSearchData = CS.find(aSearchFilterData,{id: oData.id});
        let oObject = {
          id : oData.id,
          label : oData.label,
          value : oSearchData ? oSearchData.value : "",
          type : oData.type,
        };
        aAvailableSearchData.push(oObject);
      }
    });

    CS.forEach(aAppliedFilters, function (oFilter) {
      let oColumnData = CS.find(oGridVewSkeleton.scrollableColumns, {id: oFilter.id});
      let oObject = {
          id: oFilter.id,
          label: oColumnData.label,
          type: oFilter.type,
          children: [],
        };

      _this.fillAppliedFilterChildrenDataForDownloadTracker(oFilter.id, oObject);
      aAppliedFilterData.push(oObject);
    });

    let oFilterContext ={
      filterType : FilterPropTypeConstants.MODULE,
      screenContext : GridViewContexts.DOWNLOAD_TRACKER,
    };

    return {
      availableFilterData: aAvailableFilterData,
      searchFilterData: aAvailableSearchData,
      appliedFilterData: aAppliedFilterData,
      appliedFilterClonedData: oDownloadTrackerProps.getAppliedFilterClonedData() || oDownloadTrackerProps.getAppliedFilterData(),
      filterContext: oFilterContext,
      isFilterExpanded: oDownloadTrackerProps.getIsFilterExpanded(),
      horizontalSliderForAppliedFilter: true,
      isDirty: oDownloadTrackerProps.getIsFilterDirty() || oDownloadTrackerProps.getIsSearchDirty(),
      showDefaultIconForAvailableAndAppliedFilter: false
    }
  };

  fillAppliedFilterChildrenDataForDownloadTracker = (sId, oObject) => {
    let oDownloadLogProps = this.getComponentProps().downloadLogProps;
    let aAppliedFilters = oDownloadLogProps.getAppliedFilterData();
    let aChildren = CS.find(aAppliedFilters, {id: sId}).children;
    let aChildrenData = [];

    switch (sId) {
      case oDownloadTrackerDictionary.DOWNLOADED_BY:
      case oDownloadTrackerDictionary.ASSET_INSTANCE_CLASS_NAME:
      case oDownloadTrackerDictionary.RENDITION_INSTANCE_CLASS_NAME:
        CS.forEach(aChildren, function (oChild) {
          let oChildData = {
            id: oChild.id,
            label: oChild.label,
          };
          aChildrenData.push(oChildData);
        });
        break;

      case oDownloadTrackerDictionary.DATE:
        CS.forEach(aChildren, function (sId) {
          let oChildData = {};
          if(MockDataForDateFilter[sId]){
            oChildData = {
              id: MockDataForDateFilter[sId].id,
              label: getTranslation()[MockDataForDateFilter[sId].label],
            };
          } else {
            oChildData = {
              id: "custom",
              label: sId,
            }
          }
          aChildrenData.push(oChildData);
        });
        break;
    }

    oObject.children = aChildrenData;
  };

  fillAvailableFilterChildrenDataForDownloadTracker = (sId, oObject) => {
    let aChildren = [];
    let sColumnName = "";
    let aResponsePath = ["success", "downloadLogFilterDataList", "list"];
    let sRequestURL = RequestMapping.getRequestUrl("config/downloadlogs/filterdata");
    switch (sId) {
      case oDownloadTrackerDictionary.ASSET_INSTANCE_CLASS_NAME:
        sColumnName = oDownloadTrackerDictionary.ASSET_INSTANCE_CLASS_ID;
        break;

      case oDownloadTrackerDictionary.DOWNLOADED_BY:
        sColumnName = oDownloadTrackerDictionary.USER_ID;
        break;

      case oDownloadTrackerDictionary.RENDITION_INSTANCE_CLASS_NAME:
        sColumnName = oDownloadTrackerDictionary.RENDITION_INSTANCE_CLASS_ID;
        break;

      case oDownloadTrackerDictionary.DATE:
        oObject.ranges = {};
        oObject.labelIdMap = {};
        CS.forEach(MockDataForDateFilter, function (oDateFilter) {
          let oChild = {
            id: oDateFilter.id,
            label: getTranslation()[oDateFilter.label],
            ignoreCount: true,
          };
          aChildren.push(oChild);
          oObject.ranges[getTranslation()[oDateFilter.label]] = oDateFilter.value;
          oObject.labelIdMap[oChild.label] = oChild.id;
        });
        break;
    }
    if (CS.isNotEmpty(sColumnName)) {
      oObject.requestResponseInfo = {
        requestType: "custom",
        responsePath: aResponsePath,
        requestURL: sRequestURL,
        customRequestModel: {
          columnName: sColumnName,
          sortOrder: "asc",
        },
      };
      oObject.filterViewType = oGridViewPropertyTypes.LAZY_MSS;
    }

    oObject.children = aChildren;
  };

  getPdfReactorServerConfigView = () => {

    let oPdfReactorServerProps = this.getComponentProps().pdfReactorServerConfigProps;
    let oPdfReactorServerModel = oPdfReactorServerProps.getPdfReactorServerData();
    let bIsDirty = false;
    if (oPdfReactorServerModel.isDirty) {
      bIsDirty = oPdfReactorServerModel.isDirty;
      oPdfReactorServerModel = oPdfReactorServerModel.clonedObject;
    }

    let oPdfReactorServerData = {isDirty: bIsDirty};
    let oPdfReactorServerConfigData = {
      rendererLicenceKey: oPdfReactorServerModel.rendererLicenceKey ? oPdfReactorServerModel.rendererLicenceKey : "",
      rendererHostIp: oPdfReactorServerModel.rendererHostIp,
      rendererPort: oPdfReactorServerModel.rendererPort
    };


    return <PdfReactorServerConfigView
        pdfReactorServerConfigData={oPdfReactorServerConfigData}
        pdfReactorServerData={oPdfReactorServerData}
    />
  };

  getDataModelConfigView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
    let sSelectedParentTreeId = oScreenProps.getSelectedLeftNavigationTreeParentId();
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem, sSelectedParentTreeId);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    switch (sSelectedItemType) {
      case oConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTE:
        return this.getAttributeView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_TAG:
        return this.getTagConfigView();

      case 'translations':
        return this.getTranslationsView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_CONTEXT:
        return this.getContextView();

      case oClassCategoryConstants.ARTICLE_CLASS:
      case oClassCategoryConstants.ASSET_CLASS:
      case oClassCategoryConstants.TARGET_CLASS:
      case oClassCategoryConstants.TEXTASSET_CLASS:
      case oClassCategoryConstants.SUPPLIER_CLASS:
        return this.getClassView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTIONTAXONOMY:
        return this.getAttributionTaxonomyView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_TEMPLATE:
        return this.getTemplateView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_RELATIONSHIP:
        return this.getRelationshipView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_PROPERTYCOLLECTION:
        return this.getPropertyCollectionView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_TABS:
        return this.getTabsConfigView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY:
        return this.getLanguageTaxonomyView(oConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY);

      case oConfigEntityTypeDictionary.ENTITY_TYPE_SMARTDOCUMENT:
        return this.getSmartDocumentConfigView();
    }

  }

  getCollaborationConfigView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    switch (sSelectedItemType) {
      case oConfigEntityTypeDictionary.ENTITY_TYPE_TASK:
        return this.getTasksView(oConfigEntityTypeDictionary.ENTITY_TYPE_TASK);
    }
  }

  getDataGovernanceConfigView = () =>{
    let oScreenProps = this.getComponentProps().screen;
    let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    switch (sSelectedItemType) {

      case oConfigEntityTypeDictionary.ENTITY_TYPE_RULE:
        return this.getRuleConfigView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST:
          return this.getRuleListView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_KPI:
        return this.getKpiConfigView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_DATAGOVERNANCE:
        return this.getTasksView(oConfigEntityTypeDictionary.ENTITY_TYPE_DATAGOVERNANCE);

      case oConfigEntityTypeDictionary.ENTITY_TYPE_GOLDEN_RECORDS:
        return this.getGoldenRecordRuleConfigView();
    }
  }

  getPartnerAdminConfigView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    switch (sSelectedItemType) {
      case oConfigEntityTypeDictionary.ENTITY_TYPE_ORGANISATION:
        return this.getOrganisationConfigView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_USER:
        return this.getUserView();
    }
  }

  getDataIntegrationConfigView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    switch (sSelectedItemType) {
      case oConfigEntityTypeDictionary.ENTITY_TYPE_PROFILE:
        return this.getEndPointConfigView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS:
        return this.getProcessView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_MAPPING:
        return this.getMappingView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_AUTHORIZATION:
        return this.getAuthorizationView();
    }
  }


  getTranslationConfigurationView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
    let sSelectedParentTreeId = oScreenProps.getSelectedLeftNavigationTreeParentId();
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem, sSelectedParentTreeId);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    switch (sSelectedItemType) {

      case oConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY:
        return this.getLanguageTaxonomyView(oConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY);

      case oConfigEntityTypeDictionary.ENTITY_TYPE_TRANSLATIONS:
        return this.getTranslationsView();
    }
  };

  getAdminConfigView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    switch (sSelectedItemType) {
      case oConfigEntityTypeDictionary.ENTITY_TYPE_THEME_CONFIGURATION:
        return this.getThemeConfigurationView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_VIEW_CONFIGURATION:
        return this.getViewConfigurationView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_PDFREACTORSERVER:
        return this.getPdfReactorServerConfigView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_GRIDEDITVIEW:
        return this.getGridEditView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_ICON_LIBRARY:
        return this.getIconLibraryView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_INDESIGN_SERVER_CONFIGURATION:
        return this.getIndesignServerConfigurationView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_DAM_CONFIGURATION:
        return this.getDAMConfigurationView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_VARIANT_CONFIGURATION:
        return this.getVariantConfigurationView();
    }

  };

  getVariantConfigurationView = () => {
    let oVariantConfigurationViewProps = this.getComponentProps().variantConfigurationProps;
    let oVariantViewData = oVariantConfigurationViewProps.getVariantViewData().clonedObject ? oVariantConfigurationViewProps.getVariantViewData().clonedObject
                                                                                            : oVariantConfigurationViewProps.getVariantViewData();
    let bIsVariantConfigurationEnable = oVariantViewData.isVariantConfigurationEnable;
    let bIsDirty = oVariantViewData.isDirty;

    return <VariantConfigurationView isVariantConfigurationEnable={bIsVariantConfigurationEnable}
                                     isDirty={bIsDirty}
    />;
  };

  getMonitoringConfigView = () => {
    let oScreenProps = this.getComponentProps().screen;
    let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
    let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem);
    let sSelectedItemType = CS.isEmpty(oSelectedItem.entityType) ? oSelectedItem.id : oSelectedItem.entityType;
    switch (sSelectedItemType) {
      case oConfigEntityTypeDictionary.ENTITY_TYPE_DOWNLOADTRACKER:
        return this.getDownloadTrackerConfigView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_AUDIT_LOG:
        return this.getAuditLogView();

      case oConfigEntityTypeDictionary.ENTITY_TYPE_ADMINISTRATION_SUMMARY:
        return this.getAdministrationView();
    }
  };

  getAdministrationView = () => {
    let oAdministrationsSummaryProps = this.getComponentProps().administrationsSummaryProps;
    let aAdministrationSummaryData = oAdministrationsSummaryProps.getAdministrationItemData();

    return (
        <AdministrationSummaryView administrationSummaryData={aAdministrationSummaryData} />
    )
  };

  getAuditLogView = () => {
    let oAuditLogFilterData = this.getAuditLogFilterData();
    let oAuditLogProps = this.getComponentProps().auditLogProps;
    let bIsShowExportStatusDialogActive = oAuditLogProps.getIsShowExportStatusDialogActive();
    let oShowExportStatusDialog = bIsShowExportStatusDialogActive ? this.getShowExportStatusDialog() : null ;
    let oColumnOrganizerProps = ContextualGridViewProps.getGridViewPropsByContext(GridViewContexts.AUDIT_LOG).columnOrganizerProps;
    let aSelectedColumns = oColumnOrganizerProps.getSelectedOrganizedColumns();
    let bIsColumnOrganizerDialogOpen = oColumnOrganizerProps.getIsDialogOpen();
    return (
      <div className="configGridViewContainer auditLog" key="auditLogConfigGridViewContainer">
        <ContextualGridView context={GridViewContexts.AUDIT_LOG}
                  tableContextId={GridViewContexts.AUDIT_LOG}
                  showCheckboxColumn={false}
                  disableDeleteButton={true}
                  enableImportExportButton={true}
                  disableImportButton={true}
                  enableShowExportStatusButton={true}
                  disableCreate={true}
                  enableManageEntityButton={false}
                  hideSearchBar={true}
                  filterData={oAuditLogFilterData}
                  columnOrganizerData={this.getColumnOrganizerData()}
                  selectedColumns={aSelectedColumns}
                  isColumnOrganizerDialogOpen={bIsColumnOrganizerDialogOpen}
        />
        {oShowExportStatusDialog}
      </div>
    );
  };

  getAuditLogFilterData = () => {
    let {auditLogProps: oAuditLogProps} = this.getComponentProps();
    let oGridViewProps = this.getComponentProps().contextualGridViewProps.getGridViewPropsByContext(GridViewContexts.AUDIT_LOG).gridViewProps;
    let oGridVewSkeleton = oGridViewProps.getGridViewSkeleton();
    let aAppliedFilters = oAuditLogProps.getAppliedFilterData();
    let aSearchFilterData = oAuditLogProps.getSearchFilterData();

    let aAvailableFilterData = [];
    let aAvailableSearchData = [];
    let aAppliedFilterData = [];
    let _this = this;
    CS.forEach(oGridVewSkeleton.scrollableColumns, function (oData) {
      if(oData.isFilterable){
        let oObject = {
          id : oData.requestId,
          label : oData.label,
          filterViewType: oData.type,
          children : [],
        };
        _this.fillAvailableFilterChildrenData(oData.id, oObject);
        aAvailableFilterData.push(oObject);
      }

      if(oData.isSearchable){
        let oSearchData = CS.find(aSearchFilterData,{id:oData.id});
        let oObject = {
          id : oData.id,
          label : oData.label,
          value : oSearchData ? oSearchData.value : "",
          type : oData.type,
        };
        aAvailableSearchData.push(oObject);
      }
    });

    CS.forEach(aAppliedFilters, function (oFilter) {
      let oColumnData = CS.find(oGridVewSkeleton.scrollableColumns, {requestId:oFilter.id});
      let oObject = {
        id: oFilter.id,
        label: oColumnData.label,
        type: oFilter.type,
        children: [],
      };
      _this.fillAppliedFilterChildrenData(oFilter.id, oObject);
      aAppliedFilterData.push(oObject);
    });

    let oFilterContext ={
      filterType : FilterPropTypeConstants.MODULE,
      screenContext : GridViewContexts.AUDIT_LOG,
    };

    return {
      availableFilterData: aAvailableFilterData,
      searchFilterData: aAvailableSearchData,
      appliedFilterData: aAppliedFilterData,
      appliedFilterClonedData: oAuditLogProps.getAppliedFilterClonedData() || oAuditLogProps.getAppliedFilterData(),
      filterContext: oFilterContext,
      isFilterExpanded: oAuditLogProps.getIsFilterExpanded(),
      horizontalSliderForAppliedFilter: true,
      isDirty: oAuditLogProps.getIsFilterDirty(),
      showDefaultIconForAvailableAndAppliedFilter: false
    }

  };

  fillAvailableFilterChildrenData = (sId, oObject) => {
    let oAuditLogProps = this.getComponentProps().auditLogProps;
    let aAppliedFilters = CS.isEmpty(oAuditLogProps.getAppliedFilterClonedData()) ? oAuditLogProps.getAppliedFilterData() : oAuditLogProps.getAppliedFilterClonedData();
    let oAvailableFiltersSearchData = oAuditLogProps.getAvailableFilterSearchData();
    let oAppliedEntities = CS.find(aAppliedFilters, {id: "entityTypes"}) || {};
    let aMockDataForEntities = MockDataForEntities();
    let aChildren = [];
    switch (sId) {
      case "userName":
        oObject.requestResponseInfo = {
          requestType: "customType",
          responsePath: ["success", "auditLogUserList", "list"],
          requestURL: RequestMapping.getRequestUrl("config/auditlog/username"),
        };
        oObject.filterViewType = oGridViewPropertyTypes.LAZY_MSS;
        break;

      case "activity":
        CS.forEach(MockDataForAuditLogActivities, function (oActivity) {
          if (CS.isEmpty(oAvailableFiltersSearchData['activities']) || CS.includes((getTranslation()[oActivity.label]).toLocaleLowerCase(), (oAvailableFiltersSearchData['activities']).toLocaleLowerCase())) {
            aChildren.push({
              id: oActivity.id,
              label: getTranslation()[oActivity.label],
              ignoreCount: true,
            });
          }
        });
        break;

      case "entityType":
        CS.forEach(aMockDataForEntities, function (oEntity) {
          if (CS.isEmpty(oAvailableFiltersSearchData['entityTypes']) || CS.includes((getTranslation()[oEntity.label]).toLocaleLowerCase(), (oAvailableFiltersSearchData['entityTypes']).toLocaleLowerCase())) {
            let oChild = {
              id: oEntity.id,
              label: getTranslation()[oEntity.label],
              ignoreCount: true,
            };
            aChildren.push(oChild);
          }
        });
        break;

      case "element":
        let aAvailableElementIds = [];
        if (CS.isEmpty(oAppliedEntities.children)) {
          CS.forEach(MockDataForElements, function (oElement) {
            aAvailableElementIds.push(oElement.id);
          })

        } else {
          CS.forEach(oAppliedEntities.children, function (oChild) {
            aAvailableElementIds = CS.concat(aAvailableElementIds, CS.find(aMockDataForEntities, oChild).elementIds);
          })
        }
        CS.forEach(aAvailableElementIds, function (sId) {
          let oElement = MockDataForElements[sId];
          if (CS.isEmpty(oAvailableFiltersSearchData['elements']) || CS.includes((getTranslation()[oElement.label]).toLocaleLowerCase(), (oAvailableFiltersSearchData['elements']).toLocaleLowerCase())) {
            let oChild = {
              id: oElement.id,
              label: getTranslation()[oElement.label],
              ignoreCount: true,
            };
            aChildren.push(oChild);
          }
        });
        break;

      case "elementType":
        let oAppliedElements = CS.find(aAppliedFilters, {id: "elements"}) || {};
        let aAvailableElementTypeIds = [];
        if (CS.isEmpty(oAppliedElements.children)) {
          if (CS.isEmpty(oAppliedEntities.children)) {
            CS.forEach(MockDataForElementTypes, function (oElementType) {
              aAvailableElementTypeIds.push(oElementType.id);
            });
          } else {
            CS.forEach(oAppliedEntities.children, function (oChild) {
              let oEntity = CS.find(aMockDataForEntities, oChild);
              CS.forEach(oEntity.elementIds, function (sElementId) {
                let oElement = CS.find(MockDataForElements, {id: sElementId});
                aAvailableElementTypeIds = CS.concat(aAvailableElementTypeIds, oElement.typeIds);
              });
            });
          }
        } else {
          CS.forEach(oAppliedElements.children, function (oChild) {
            aAvailableElementTypeIds = CS.concat(aAvailableElementTypeIds, CS.find(MockDataForElements, oChild).typeIds);
          })
        }
        CS.forEach(aAvailableElementTypeIds, function (sId) {
          let oElementType = MockDataForElementTypes[sId];
          if (CS.isEmpty(oAvailableFiltersSearchData['elementTypes']) || CS.includes((getTranslation()[oElementType.label]).toLocaleLowerCase(), (oAvailableFiltersSearchData['elementTypes']).toLocaleLowerCase())) {
            let oChild = {
              id: oElementType.id,
              label: getTranslation()[oElementType.label],
              ignoreCount: true,
            };
            aChildren.push(oChild);
          }
        });
        break;

      case "date":
        oObject.ranges = {};
        oObject.labelIdMap = {};
        CS.forEach(MockDataForDateFilter, function (oDateFilter) {
          let oChild = {
            id: oDateFilter.id,
            label: getTranslation()[oDateFilter.label],
            ignoreCount: true,
          };
          aChildren.push(oChild);
          oObject.ranges[getTranslation()[oDateFilter.label]] = oDateFilter.value;
          oObject.labelIdMap[oChild.label] = oChild.id;
        });
        break;
    }
    oObject.children = aChildren;
  };

  fillAppliedFilterChildrenData = (sId, oObject) => {
    let oAuditLogProps = this.getComponentProps().auditLogProps;
    let aAppliedFilters = oAuditLogProps.getAppliedFilterData();
    let aMockDataForEntities = MockDataForEntities();
    let aChildren = CS.find(aAppliedFilters, {id: sId}).children;
    let aChildrenData = [];

    switch (sId) {
      case "userName":
        CS.forEach(aChildren, function (oChild) {
          let oChildData = {
            id: oChild.id,
            label: oChild.id,
          };
          aChildrenData.push(oChildData);
        });
        break;

      case "activities":
        CS.forEach(aChildren, function(oChild){
          let oActivity = MockDataForAuditLogActivities[oChild.id];
          let oChildData = {
            id: oActivity.id,
            label: getTranslation()[oActivity.label],
          };
          aChildrenData.push(oChildData);
        });
        break;

      case "entityTypes":
        CS.forEach(aChildren, function (oChild) {
          let oEntity = CS.find(aMockDataForEntities, oChild);
          let oChildData = {
            id: oEntity.id,
            label: getTranslation()[oEntity.label],
          };
          aChildrenData.push(oChildData);
        });
        break;

      case "elements":
        CS.forEach(aChildren, function (oChild) {
          let oChildData = {
            id: MockDataForElements[oChild.id].id,
            label: getTranslation()[MockDataForElements[oChild.id].label],
          };
          aChildrenData.push(oChildData);
        });
        break;

      case "elementTypes":
        CS.forEach(aChildren, function (oChild) {
          let oChildData = {
            id: MockDataForElementTypes[oChild.id].id,
            label: getTranslation()[MockDataForElementTypes[oChild.id].label],
          };
          aChildrenData.push(oChildData);
        });
        break;

      case "date":
        CS.forEach(aChildren, function (sId) {
          let oChildData = {};
          if(MockDataForDateFilter[sId]){
            oChildData = {
              id: MockDataForDateFilter[sId].id,
              label: getTranslation()[MockDataForDateFilter[sId].label],
            };
          } else {
            oChildData = {
              id: "custom",
              label: sId,
            }
          }
          aChildrenData.push(oChildData);
        });
        break;
    }

    oObject.children = aChildrenData;
  };

  getShowExportStatusDialog = () => {
    let oColumnOrganizerProps = ContextualGridViewProps.getGridViewPropsByContext(GridViewContexts.AUDIT_LOG_EXPORT_STATUS).columnOrganizerProps;
    let oColumnOrganizerData = {
      selectedColumns: oColumnOrganizerProps.getSelectedOrganizedColumns(),
      isColumnOrganizerDialogOpen: oColumnOrganizerProps.getIsDialogOpen()
    };
    return (
        <ShowExportStatusDialogView
            context={GridViewContexts.AUDIT_LOG_EXPORT_STATUS}
            tableContextId = {GridViewContexts.AUDIT_LOG_EXPORT_STATUS}
            columnOrganizerData={oColumnOrganizerData}
        />
    );
  };

  getRightSectionView = (sSelectedTabId) => {

    switch (sSelectedTabId) {
      case ConfigModulesDictionary.DATA_MODEL:
        return this.getDataModelConfigView();

      case ConfigModulesDictionary.COLLABORATION:
        return this.getCollaborationConfigView();

      case ConfigModulesDictionary.PARTNER_ADMIN:
        return this.getPartnerAdminConfigView();

      case ConfigModulesDictionary.DATA_GOVERNANCE:
        return this.getDataGovernanceConfigView();

      case ConfigModulesDictionary.DATA_INTEGRATION:
        return this.getDataIntegrationConfigView();

      case ConfigModulesDictionary.TRANSLATION_CONFIGURATION:
        return this.getTranslationConfigurationView();

      case ConfigModulesDictionary.ADMIN_CONFIGURATION:
        return this.getAdminConfigView();

      case ConfigModulesDictionary.MONITORING:
        return this.getMonitoringConfigView();
    }
  }


  getGridEditView = () => {

    let oGridEditViewProps = this.getComponentProps().gridEditProps;
    let oGridEditData = oGridEditViewProps.getGridEditData();
    let oPropertyList = oGridEditData.propertyList;
    oPropertyList = oPropertyList.clonedObject || oPropertyList;
    let oPropertySequenceList = oGridEditData.propertySequenceList;
    oPropertySequenceList = oPropertySequenceList.clonedObject || oPropertySequenceList;

    let oCustomRequestResponseInfo = {};
    oCustomRequestResponseInfo.searchRequestResponseInfo = {
      url: oGridEditMapping.getGridEditProperties,
      hiddenColumnsResponsePath: "gridEditProperties",
    };
    let oEmptyMessageView = (
      <div className="messageWrapper">
        <div className="emptyPropertiesMessage">{getTranslation().NO_PROPERTIES_SELECTED}</div>
        <div className="emptyPropertiesInfo">{getTranslation().GRID_EDIT_VIEW_EMPTY_SEQUENCE_LIST_MESSAGE}</div>
      </div>);

    let aTotalColumns = oPropertyList.concat(oPropertySequenceList);
    return (<div className="gridEditConfigView">
        <ColumnOrganizerView totalColumns={aTotalColumns}
                             selectedColumns={oPropertySequenceList}
                             context="gridEditConfig"
                             enableLoadMore={true}
                             customRequestResponseInfo={oCustomRequestResponseInfo}
                             emptyMessage={oEmptyMessageView}
                             selectedColumnsLimit={oGridEditViewProps.getSequenceListLimit()}
                             customHeaderLabel={getTranslation().GRID_EDIT_VIEW}
                             preventEmptySelectedColumns={true}
        /></div>);
  };

    getIconLibraryView = () => {
    let oActionGroups = SettingUtils.getAppData().getActionBarList();
    let oAdminConfigurationActionGroups = oActionGroups.AdminConfigurationConfigView;
    let aActiveIcons = IconLibraryProps.getAllActiveIcons();
    let bIsUsageDeleteDialog = IconLibraryProps.getIsUsageDeleteDialog();
    let bIsUploadClicked = IconLibraryProps.getIconLibraryUploadClicked();
    let oIconLibraryData = SettingUtils.getIconLibraryData();

    let oDialogExtraData = {
      activeIconElement: IconLibraryProps.getActiveIconElement(),
      isIconEditClicked: IconLibraryProps.getIconElementEditClicked(),
      isUploadClicked: bIsUploadClicked,
    };

    return <IconLibraryView
        isShowSaveDiscardButtons={IconLibraryProps.isDirty}
        iconLibraryData={oIconLibraryData}
        headerActionGroup={oAdminConfigurationActionGroups.IconLibraryConfigView}
        dragAndDropViewRequired={true}
        isCopyCodeRequired={true}
        isUsageDeleteDialog={bIsUsageDeleteDialog}
        activeIcons={aActiveIcons}
        isDragging={IconLibraryProps.getIsDragging()}
        dialogExtraData={oDialogExtraData}
    />;
  };

  getIndesignServerConfigurationView = () => {

    let oIndesignServerList = IndesignServerConfigurationViewProps.getIndesignServerConfigurationList();
    let oIndesignLoadBalancerConfig = IndesignServerConfigurationViewProps.getLoadBalancerConfiguration();

    return <IndesignServerConfigurationView indesignServerList={Object.values(oIndesignServerList)}
                                            loadBalancerConfiguration = {oIndesignLoadBalancerConfig} />;

  };

  getDAMConfigurationView = () => {
    let oDamConfigurationData = DAMConfigurationViewProps.getDamConfigurationData();
    if (oDamConfigurationData.hasOwnProperty('clonedObject')) {
      oDamConfigurationData = oDamConfigurationData.clonedObject;
    }

    return <DAMConfigurationView damConfigurationData={oDamConfigurationData}/>;
  };

  getCheckBoxData = (oDialogData) => {
    return {
      roleType: {
        checked: true,
        disabled: true
      },
      readOnly: {
        checked: true,
        disabled: true
      },
      physicalCatalog: {
        checked: oDialogData.bIsClonePhysicalCatalog,
        disabled: oDialogData.bIsExactClone
      },
      taxonomy: {
        checked: oDialogData.bIsCloneTaxonomies,
        disabled: oDialogData.bIsExactClone
      },
      targetClasses: {
        checked: oDialogData.bIsCloneTargetClasses,
        disabled: oDialogData.bIsExactClone
      },
      enableDashboard: {
        checked: oDialogData.bIsCloneEnableDashboard,
        disabled: oDialogData.bIsExactClone
      },
      kpi: {
        checked: oDialogData.bIsCloneKPI,
        disabled: oDialogData.bIsExactClone
      },
      entities: {
        checked: oDialogData.bIsCloneEntities,
        disabled: oDialogData.bIsExactClone
      }
    }
  };


  getRoleCloneDialogView = () => {
    let oSelectedRoleDetailedModel = this.getClonedRoleDetailedViewModel();
    let oRoleConfigDialogViewProps = this.getComponentProps().roleCloneDialogViewProps;
    let oSelectedRole = oRoleConfigDialogViewProps.getSelectedRole();
    let oCloneRoleDialogData = oRoleConfigDialogViewProps.getRoleCloneDialogData();
    let oCheckboxData = this.getCheckBoxData(oCloneRoleDialogData);
    let bIsCurrentRoleSystemAdmin = oSelectedRole.roleType === RoleTypeDictionary.SYSTEM_ADMIN;
    let aEntitiesList = [];
    let aALowedPortalIds = MockDataForPhysicalCatalogAndPortal.portals();
    CS.forEach(aALowedPortalIds, (oPortal) => {
      let sLabel = '';
      let aEntities = [];
      if (oPortal.id === PortalTypeDictionary.PIM) {
        sLabel = getTranslation().PIM_ENTITIES;
        aEntities = EntitiesDictionary();
      }
      aEntitiesList.push({portalId: oPortal.id, label: sLabel, entities: aEntities});
    });

    return (
        <CreateRoleCloneDialogView selectedRoleDetailedModel={oSelectedRoleDetailedModel}
                                   entitiesList={aEntitiesList}
                                   isUserSystemAdmin={bIsCurrentRoleSystemAdmin}
                                   createRoleCloneDialogData={oCloneRoleDialogData}
                                   checkboxData = {oCheckboxData}
        />
    );
  };

  getClonedRoleDetailedViewModel = () => {
    let oActiveOrganization = this.getComponentProps().organisationConfigView.getActiveOrganisation();
    let aOrganisationPhysicalCatalogs = oActiveOrganization.physicalCatalogs;
    let aOrganisationPortals = oActiveOrganization.portals;
    let oRoleProps = this.getComponentProps().roleCloneDialogViewProps;
    let oSelectedRole = this.getComponentProps().roleCloneDialogViewProps.getSelectedRole();
    let oProperty = {};

    let oRoleMaster = this.getStore().getUnSavedRoleFromMasterById(oSelectedRole.id);
    let sRoleType = oSelectedRole.roleType;
    let aSelectedType = CS.isEmpty(sRoleType) ? [] : [sRoleType];
    let aSelectedPhysicalCatalogItems = CS.isEmpty(oSelectedRole.physicalCatalogs) ? [] : CS.cloneDeep(oSelectedRole.physicalCatalogs);
    let aPortalItems = [];
    let aSelectedPortalItems = CS.isEmpty(oSelectedRole.portals) ? [] : oSelectedRole.portals;

    let aPhysicalCatalogItems = [];

    if (CS.isEmpty(aOrganisationPhysicalCatalogs)) {
      aPhysicalCatalogItems = MockDataForPhysicalCatalogAndPortal.physicalCatalogs();
    } else {
      CS.forEach(MockDataForPhysicalCatalogAndPortal.physicalCatalogs(), function (oItem) {
        if (CS.includes(aOrganisationPhysicalCatalogs, oItem.id)) {
          aPhysicalCatalogItems.push(oItem);
        }
      });
    }

    if (CS.isEmpty(aOrganisationPortals)) {
      aPortalItems = MockDataForPhysicalCatalogAndPortal.portals();
    } else {
      CS.forEach(MockDataForPhysicalCatalogAndPortal.portals(), function (oItem) {
        if (CS.includes(aOrganisationPortals, oItem.id)) {
          aPortalItems.push(oItem);
        }
      });
    }

    let aReferencedClassList = oRoleProps.getReferencedKlasses();
    let oTargetKlassItems = {};
    CS.forEach(aReferencedClassList, function (oKlass) {
      oTargetKlassItems[oKlass.id] = oKlass;
    });

    let bIsRoleTypeDisabled = true; // Always disabled since roleType will be decided on creation
    let aMockDataForUserRoleTypes = MockDataForUserRoleTypes();
    if (oActiveOrganization.id != SettingUtils.getTreeRootId()) {
      CS.remove(aMockDataForUserRoleTypes, {id: "admin"})
    }
    let oRoleTypeData = this.getMSSModel(aSelectedType, aMockDataForUserRoleTypes, "role", "roleType", false, true, bIsRoleTypeDisabled);
    let aDisabledItems = [];
    if (oSelectedRole.isReadOnly) {
      CS.remove(aSelectedPhysicalCatalogItems, function (sId) {
        return sId === PhysicalCatalogDictionary.DATAINTEGRATION;
      });
    }
    CS.map(aPhysicalCatalogItems, function (oPhysicalCatalogItem) {
      aDisabledItems.push(oPhysicalCatalogItem.id);
    });
    let oPhysicalCatalogData = this.getSelectionToggleModel(aPhysicalCatalogItems, aSelectedPhysicalCatalogItems, "role", false, aDisabledItems);
    let oPortalData = this.getSelectionToggleModel(aPortalItems, aSelectedPortalItems, "role", false);
    let aSelectedTaxonomyIds = CS.isEmpty(oSelectedRole.targetTaxonomies) ? [] : oSelectedRole.targetTaxonomies;
    let oReferencedTaxonomies = oRoleProps.getReferencedTaxonomies();
    let oAvailabilityData = SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomies, [], []);
    let aSelectedKlassIds = CS.isEmpty(oSelectedRole.targetKlasses) ? [] : oSelectedRole.targetKlasses;
    let aModuleRightScreen = oRoleMaster.entities || [];
    let sURLToGetRoleUsers = RequestMapping.getRequestUrl(oRoleRequestMapping.GetAllowedRoleEntities);
    let oTargetKlassData = {
      selectedItems: aSelectedKlassIds,
      items: oTargetKlassItems,
      url: sURLToGetRoleUsers
    };

    oProperty['roleTypeData'] = oRoleTypeData;
    oProperty['physicalCatalogData'] = oPhysicalCatalogData;
    oProperty['portalData'] = oPortalData;
    oProperty['availabilityData'] = oAvailabilityData;
    oProperty['targetKlassData'] = oTargetKlassData;
    oProperty['isUserDisabled'] = bIsRoleTypeDisabled;
    oProperty['activeOrganizationId'] = oActiveOrganization.id;
    oProperty['roleType'] = sRoleType;
    oProperty['isDashboardEnable'] = oSelectedRole.isDashboardEnable;
    oProperty['isReadOnly'] = oSelectedRole.isReadOnly;

    oProperty['kpiData'] = {
      selectedKPIs: oSelectedRole.kpis,
      referencedKPIs: oRoleProps.getReferencedKPIs()
    };

    return new RoleDetailModel(
        "",
        oRoleMaster.label,
        "",
        "",
        "",
        "",
        false,
        {},
        false,
        {},
        [],
        [],
        false,
        aModuleRightScreen,
        oProperty
    );
  }

  //****************************************** Render method **************************************************

  render() {
    let oScreen = this.getComponentProps().screen;
    var sSelectedTabId = oScreen.getSelectedTabId();
    let aTabItemsList = new ConfigModulesList();
    let oLeftSectionData = this.getLeftSectionData(sSelectedTabId);
    let oRightSectionView = this.getRightSectionView(sSelectedTabId);
    let sScreenName = SettingUtils.getConfigScreenViewName();
    let aSearchListItems = oScreen.getUniversalSearchList();
    let oContextValue = {
      postMethodToCall: SettingUtils.csPostRequest
    };

    return (
        <div className="settingScreenContainerWrapper">
         {/* {this.getBreadCrumbView()}*/}
         <ContextList.Provider value={oContextValue}>
          <SettingScreenView
              tabItemsList={aTabItemsList}
              selectedTabId={sSelectedTabId}
              leftSectionData={oLeftSectionData}
              rightSectionView={oRightSectionView}
              activeScreenName = {sScreenName}
              searchListItems={aSearchListItems}
              breadcrumbView = {this.getBreadCrumbView()}
          />
         </ContextList.Provider>
        </div>);
  }
}

export default SettingScreenController;
