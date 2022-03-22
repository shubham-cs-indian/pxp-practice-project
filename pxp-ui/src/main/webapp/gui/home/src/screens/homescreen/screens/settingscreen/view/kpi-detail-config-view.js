import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as TagSelectorView } from '../../../../../viewlibraries/tagselectorview/tag-selector-view';
import { view as CommonConfigSectionView } from './../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import { view as SmallTaxonomyView } from './../../../../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import SmallTaxonomyViewModel from './../../../../../viewlibraries/small-taxonomy-view/model/small-taxonomy-view-model';
import { view as RuleDetailView } from './rule-detail-view';
import { view as KPIDrilldownView } from './kpi-drilldown-view';
import SectionLayout from '../tack/kpi-detail-config-layout-data';
import MockDataForFrequency from './../tack/mock/mock-data-for-frequency-types';
import MockDataForUnit from '../tack/mock/mock-data-for-unit-types';
import ViewUtils from './utils/view-utils';
import { view as NumberLocaleView } from '../../../../../viewlibraries/numberlocaleview/number-locale-view';
import KpiConfigDictionary from '../../../../../commonmodule/tack/kpi-config-dictionary';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';

const oEvents = {
  HANDLE_KPI_DROP_DOWN_CLICKED: "handle_kpi_drop_down_clicked",
  HANDLE_KPI_ADD_RULE_CLICKED: "handle_kpi_add_rule_clicked",
  HANDLE_KPI_THRESHOLD_VALUE_CHANGED: "handle_kpi_threshold_value_changed",
  HANDLE_KPI_RULE_DELETE_CLICKED: "handle_kpi_rule_delete_clicked",
  HANDLE_KPI_MSS_SEARCH_CLICKED: "handle_kpi_mss_search_clicked",
  HANDLE_KPI_MSS_LOAD_MORE_CLICKED: "handle_kpi_mss_load_more_clicked",
  HANDLE_KPI_MSS_CREATE_CLICKED: "handle_kpi_mss_create_clicked",
  HANDLE_KPI_PARTNER_APPLY_CLICKED: "handle_kpi_partner_apply_clicked"
};

const oPropTypes = {
  activeKPI: ReactPropTypes.object.isRequired,
  referencedData: ReactPropTypes.object.isRequired,
  context: ReactPropTypes.string,
  activeTabId: ReactPropTypes.string,
  activeBlock: ReactPropTypes.object,
  aAttributeListForMSS: ReactPropTypes.array,
  aTagListForMSS: ReactPropTypes.array,
  oDashboardTabsDataForMss: ReactPropTypes.object,
  oRelationshipListForMSS: ReactPropTypes.object,
  taxonomyPaginationData: ReactPropTypes.object,
  referencedAttributes: ReactPropTypes.object,
  referencedTags: ReactPropTypes.object,
  referencedEndpoints: ReactPropTypes.object,
  physicalCatalogIdsData: ReactPropTypes.array,
  portalIdsData: ReactPropTypes.array,
  lazyMSSReqResInfo: ReactPropTypes.object
};

// @CS.SafeComponent
class KPIDetailedConfigView extends React.Component {
  static propTypes = oPropTypes;

  handleKPIDetailedClassTypePopOverOpened = () => {};

  /********************************************** *************************************/
  handleAddRule = (sRuleType, sRuleBlockId) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_ADD_RULE_CLICKED, sRuleType, sRuleBlockId)
  };

  handleThresholdValueChanged = (sActiveBlockId, sThresholdType, sUnitType, sThresholdValue) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_THRESHOLD_VALUE_CHANGED, sActiveBlockId, sThresholdType, sUnitType, sThresholdValue);
  };

  handleMSSValueChanged = (sRuleId, sKey, aSelectedItems, oReferencedData) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_DROP_DOWN_CLICKED, sRuleId, sKey, aSelectedItems, oReferencedData)
  };

  handleDeleteRuleClicked = (sRuleId) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_RULE_DELETE_CLICKED, sRuleId)
  };

  handleMSSSearchClicked = (sContext, sSearchText) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_MSS_SEARCH_CLICKED, sContext, sSearchText);
  };

  handleMSSLoadMoreClicked = (sContext) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_MSS_LOAD_MORE_CLICKED, sContext);
  };

  handleKpiMSSCreateClicked = (sPropertyId, sActiveKpiId, sSearchText) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_MSS_CREATE_CLICKED, sPropertyId, sActiveKpiId, sSearchText);
  };

  handlePartnerApplyClicked = (aSelectedList, oReferencedData) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_PARTNER_APPLY_CLICKED, aSelectedList, oReferencedData);
  };

  getAttributesMSS = (aAttributes, sRuleId, oReferencedData, sRuleBlock) => {
    let oMSSProcessRequestResponseObj = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.ATTRIBUTES,
      types: [],
      typesToExclude: [],
    };

    if(KpiConfigDictionary.UNIQUENESS_RULES === sRuleBlock) {
      oMSSProcessRequestResponseObj.customRequestModel = {
        isLanguageIndependent: true
      }
    }

    let oProps = this.props;
    let aAttributesList = oProps.aAttributeListForMSS;
    let aSelectedEntityIds = CS.map(aAttributes, 'entityId');
    let fOnApply = this.handleMSSValueChanged.bind(this, sRuleId, ConfigDataEntitiesDictionary.ATTRIBUTES);
    let oMSSModel = this.getMSSModel(aSelectedEntityIds, aAttributesList, oProps.context, ConfigDataEntitiesDictionary.ATTRIBUTES, true, false, null, fOnApply);
    oMSSModel.requestResponseInfo = oMSSProcessRequestResponseObj;
    oMSSModel.referencedData = oReferencedData;
    return oMSSModel;
  };

  getTagsMSS = (aTags, sRuleId, oReferencedData) => {
    let oMSSProcessRequestResponseObj = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.TAGS,
      types: []
    };
    let oProps = this.props;
    let aTagListForMSS = oProps.aTagListForMSS;
    let aSelectedEntityIds = CS.map(aTags, 'entityId');
    let fOnApply = this.handleMSSValueChanged.bind(this, sRuleId, ConfigDataEntitiesDictionary.TAGS);
    let oMSSModel = this.getMSSModel(aSelectedEntityIds, aTagListForMSS, oProps.context, ConfigDataEntitiesDictionary.TAGS, true, false, null, fOnApply);
    oMSSModel.requestResponseInfo = oMSSProcessRequestResponseObj;
    oMSSModel.referencedData = oReferencedData;
    return oMSSModel;
  };

  getRelationshipMSS = (aRelationships, sRuleId) => {
    let oProps = this.props;
    let oRelationshipListForMSS = oProps.oRelationshipListForMSS;
    let aSelectedEntityIds = CS.map(aRelationships, 'entityId');
    let sSearchText = ViewUtils.getEntityVsSearchTextMapping()["relationships"];
    let fOnApply = this.handleMSSValueChanged.bind(this, sRuleId, 'relationships');
    let oMSSModel = this.getMSSModel(aSelectedEntityIds, oRelationshipListForMSS, oProps.context, "relationships", true, false, null, fOnApply);
    oMSSModel.searchHandler = this.handleMSSSearchClicked.bind(this, "relationships");
    oMSSModel.loadMoreHandler = this.handleMSSLoadMoreClicked.bind(this, "relationships");
    oMSSModel.isLoadMoreEnabled = true;
    oMSSModel.searchText = sSearchText;
    return oMSSModel;
  };

  getRulesModel = (sRuleBlock, oReferencedAttributes, oReferencedTags) => {
    let oActiveBlock = this.props.activeBlock;
    let aRules = oActiveBlock.rules;
    let aDOM = [];
    let _this = this;
    let sSplitter = ViewUtils.getSplitter();
    let oSectionLayout = new SectionLayout();
    if(sRuleBlock == KpiConfigDictionary.COMPLETENESS_RULES || sRuleBlock == KpiConfigDictionary.UNIQUENESS_RULES) {
      CS.forEach(aRules, function (oRule) {
        let sCommonConfigContext = `${_this.props.context}${sSplitter}${oRule.id}`;
        let oAttributesMSS = _this.getAttributesMSS(oRule.attributes, oRule.id, oReferencedAttributes, sRuleBlock);
        let oTagsMSS = _this.getTagsMSS(oRule.tags, oRule.id, oReferencedTags);
        let oRelationshipsMSS = _this.getRelationshipMSS(oRule.relationships, oRule.id);
        let oData = {
          label: oRule.label,
          code: oRule.code,
          attributes: oAttributesMSS,
          tags: oTagsMSS,
          relationships: oRelationshipsMSS
        };

        aDOM.push(<div className={"completenessConformityRulesDetail"} key={oRule.id}>
          <div className='completenessConformityRuleDelete'
               onClick={_this.handleDeleteRuleClicked.bind(_this, oRule.id)}></div>
          <CommonConfigSectionView context={sCommonConfigContext}
                                   sectionLayout={oSectionLayout[sRuleBlock]}
                                   data={oData}
          />
        </div>);
      });
    }else{
      let sSplitter = ViewUtils.getSplitter();
      CS.forEach(aRules, function (oRule) {
        let sRuleConfigViewContext = `${_this.props.context}${sSplitter}${oRule.id}`;
        let sCommonConfigContext = `${_this.props.context}${sSplitter}${oRule.id}`;

        let oRuleView = (<div className={"ruleConfigViewContainer"}>
          <div className={"ruleDetailedViewContainer"}>
            <RuleDetailView activeRule={oRule}
                            showOnlyCause={true}
                            screenContext={sRuleConfigViewContext}
                            lazyMSSReqResInfo={_this.props.lazyMSSReqResInfo}
                            context="kpiConfigRules"/>
          </div>
        </div>);
        let oData = {
          label: oRule.label,
          code: oRule.code,
          rules: oRuleView
        };

        aDOM.push(<div className={"completenessConformityRulesDetail"} key={oRule.id}>
          <div className='completenessConformityRuleDelete'
               onClick={_this.handleDeleteRuleClicked.bind(_this, oRule.id)}></div>
          <CommonConfigSectionView context={sCommonConfigContext}
                                   sectionLayout={oSectionLayout[sRuleBlock]}
                                   data={oData}/></div>);
      });
    }

    let sAddRule = `+ ${getTranslation().ADD_RULE}`
    aDOM.push(
        <div className='kpiConfigAddRuleButton' key="addButton"
             onClick={this.handleAddRule.bind(this, sRuleBlock, oActiveBlock.id)}>
          {sAddRule}
        </div>);
    return (aDOM);
  };

  getThresholdView = () => {
    //TODO: handle onChange, and put values via value props
    let oProps = this.props;
    let oActiveBlock = oProps.activeBlock;
    let sUpperThreshold = oActiveBlock.threshold ? oActiveBlock.threshold.upper : '';
    let sLowerThreshold = oActiveBlock.threshold ? oActiveBlock.threshold.lower : '';
    let sUnit = oActiveBlock.unit == KpiConfigDictionary.PERCENTAGE ? '%' : '';
    let oUnitDOM = <span className='kpiThresholdUnit'>{sUnit}</span>;
    return (
        <div className="kpiThresholdContainer">
          <span className='kpiThresholdLabel'>Lower:</span>
          <div className="kpiThresholdNumberViewConatainer">
            <NumberLocaleView
                onBlur={this.handleThresholdValueChanged.bind(this, this.props.activeBlock.id, KpiConfigDictionary.LOWER, oActiveBlock.unit)}
                              value={sLowerThreshold}/>
          </div>
          {oUnitDOM}
          <span className='kpiThresholdLabel'>Upper:</span>
          <div className="kpiThresholdNumberViewConatainer">
            <NumberLocaleView
                onBlur={this.handleThresholdValueChanged.bind(this, this.props.activeBlock.id, KpiConfigDictionary.UPPER, oActiveBlock.unit)}
                value={sUpperThreshold}/>
          </div>
          {oUnitDOM}
        </div>
    );
  };

  getTasksModel = () => {
    let oProps = this.props;
    let sBlockName = oProps.activeTabId;
    let oReferencedData = oProps.referencedData;
    let oDataGovernanceTasks = oReferencedData.dataGovernanceTasks;
    let aDataGovernanceTasks = [];
    let aSelectedDataGovernanceTask = [];

    CS.forEach(oDataGovernanceTasks, function (oDataGovernanceTask) {
      aDataGovernanceTasks.push(oDataGovernanceTask);
    });

    let oActiveBlock = oProps.activeBlock
    if (oActiveBlock.type == sBlockName) {
      if (!CS.isEmpty(oActiveBlock.task)) {
        aSelectedDataGovernanceTask.push(oActiveBlock.task);
      }
    }

    return this.getMSSModel(aSelectedDataGovernanceTask, aDataGovernanceTasks, oProps.context, "task", false, false);
  };

  getUnitModel = () => {
    let oProps = this.props;
    let sBlockName = oProps.activeTabId;
    let aUnitList = MockDataForUnit;
    let aSelectedUnit = [];
    let oActiveBlock = oProps.activeBlock;
    if (oActiveBlock.type == sBlockName) {
      if (!CS.isEmpty(oActiveBlock.unit)) {
        aSelectedUnit.push(oActiveBlock.unit);
      }
    }
    let sSplitter = ViewUtils.getSplitter();
    let sInnerContext = `${'unit'}${sSplitter}${oActiveBlock.id}`;

    return this.getMSSModel(aSelectedUnit, aUnitList, oProps.context, sInnerContext, false, true);
  };

  getColorModel = () => {
    let oProps = this.props;
    let sBlockName = oProps.activeTabId;
    let oReferencedData = oProps.referencedData;
    let oActiveBlock = oProps.activeBlock;
    let sSelectedColor = oActiveBlock.color;
    let aSelectedColor = [];
    if (!CS.isEmpty(sSelectedColor)) {
      aSelectedColor.push(sSelectedColor);
    }
    let aColorList = oReferencedData.colorList;
    if (oActiveBlock.type == sBlockName) {
      if (!CS.isEmpty(oActiveBlock.color)) {
        aSelectedColor.push(oActiveBlock.color);
      }
    }
    return this.getMSSModel(aSelectedColor, aColorList, oProps.context, "color");
  };

  getRuleBlockModel = (sContext) => {
    let oReferencedAttributes = this.props.referencedAttributes;
    let oReferencedTags = this.props.referencedTags;
    let oUnitModel = this.getUnitModel();
    let oTasksModel = this.getTasksModel();
    let oThresholdModel = this.getThresholdView();
    let oRulesModel = this.getRulesModel(sContext, oReferencedAttributes, oReferencedTags);
    let oColorModel = this.getColorModel();

    return ({
      unit: oUnitModel,
      task: oTasksModel,
      threshold: oThresholdModel,
      rules: oRulesModel,
      color: oColorModel
    });
  };

  getMSSModel = (
    aSelectedItems,
    aItems,
    sContext,
    sInnerContext,
    bIsMultiSelect,
    bCannotRemove,
    fOnPopOverOpen,
    fOnApply,
  ) => {
    let sSplitter = ViewUtils.getSplitter();
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
    }
  };

  getTagSelectorView = () => {
    let oProps = this.props;
    let oPaginationData = ViewUtils.getEntityPaginationData();
    let sSearchText = ViewUtils.getEntitySearchText();
    let oReferencedData = oProps.referencedData;
    let oTagMap = oReferencedData.tagMap || {};
    let sContext = oProps.context;
    let bIsTagValueListSearchEnabled = true;
    let bIsTagValueListLoadMoreEnabled = true;

    return (<TagSelectorView
        selectedTagsData={oReferencedData.selectedTags}
        tagMap={oTagMap}
        context={sContext}
        searchText={sSearchText}
        paginationData={oPaginationData}
        isTagValueListSearchEnabled={bIsTagValueListSearchEnabled}
        isTagValueListLoadMoreEnabled={bIsTagValueListLoadMoreEnabled}
    />);
  };

  getDashboardTabSelectorView = (sDashboardTabId, sActiveKpiId) => {
    let oProps = this.props;
    let aDashboardTabsListForMSS = oProps.oDashboardTabsDataForMss.dashboardTabsList;
    let fOnApply = this.handleMSSValueChanged.bind(this, "", 'dashboardTab');
    let oMSSModel = this.getMSSModel([sDashboardTabId], aDashboardTabsListForMSS, oProps.context, "dashboardTab", false, true, null, fOnApply);
    oMSSModel.showCreateButton = true;
    oMSSModel.onCreateHandler = this.handleKpiMSSCreateClicked.bind(this, "dashboardTab", sActiveKpiId);
    oMSSModel.referencedData = oProps.oDashboardTabsDataForMss.referencedData;
    oMSSModel.context = "dashboardTab";
    oMSSModel.requestResponseInfo = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.DASHBOARD_TABS,
    };
    return oMSSModel;
  };

  getPartnersData = () => {
    let oReqResObj = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.ORGANIZATIONS
    };
    let __props = this.props;
    let oActiveKPI = __props.activeBlock;
    let oReferencedOrganizations = __props.referencedData.referencedOraganizations || __props.referencedData.referencedOrganizations;
    return {
      isMultiSelect: true,
      context: 'kpi',
      selectedItems: oActiveKPI.organizations,
      cannotRemove: false,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
        onApply: this.handlePartnerApplyClicked,
        referencedData: oReferencedOrganizations
    }
  };

  getEndpointsModel = () => {
    let __props = this.props;
    let oActiveKPI = __props.activeBlock;
    let oReferencedData = __props.referencedData;
    let sSplitter = ViewUtils.getSplitter();

    let oReqResObj = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.ENDPOINTS,
    };

    let aSelectedEndpoints = CS.isEmpty(oActiveKPI.endpoints) ? [] : oActiveKPI.endpoints;
    return {
      isMultiSelect: true,
      context: `kpiConfigDetail${sSplitter}endpoints`,
      selectedItems: aSelectedEndpoints,
      cannotRemove: false,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
      referencedData: oReferencedData.referencedEndpoints
    }
  };

  getKPIInformationModel = () => {
    let oProps = this.props;
    let oActiveKPI = oProps.activeBlock;
    let oPhyData = {
      selectedItems: oActiveKPI.physicalCatalogIds,
      items: this.props.physicalCatalogIdsData,
      context: "kpi"
    };

    return {
      id: oActiveKPI.id,
      label: oActiveKPI.label,
      code: oActiveKPI.code,
      dashboardTab: this.getDashboardTabSelectorView(oActiveKPI.dashboardTabId, oActiveKPI.id),
      tags: this.getTagSelectorView(),
      physicalCatalogId: oPhyData,
      partners: this.getPartnersData(),
      endpoints: this.getEndpointsModel(),
    }
  };

  getSmallTaxonomyViewModel = (oMultiTaxonomyData) => {
    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    var sRootTaxonomyId = ViewUtils.getTreeRootId();
    var oProperties = {};
    oProperties.headerPermission = {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };

  getTaxonomyView = () => {
    let oReferencedData = this.props.referencedData;
    let oMultiTaxonomyData = oReferencedData.multiTaxonomyData;
    let oAllowedTaxonomyHierarchyList = oMultiTaxonomyData.allowedTaxonomyHierarchyList;

    var oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oMultiTaxonomyData);

    return (<SmallTaxonomyView model={oSmallTaxonomyViewModel}
                               allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}
                               showCustomIcon={true}
                               isDisabled={false}
                               context={this.props.context}
                               showAllComponents={true}
                               paginationData={this.props.taxonomyPaginationData}/>);
  }

  getKPITargetModel = () => {
    let oProps = this.props;
    let oReferencedData = oProps.referencedData;
    let oActiveKPI = oProps.activeBlock;
    let aSelectedTypes = oActiveKPI.targetFilters.klassIds;
    let sSplitter = ViewUtils.getSplitter();
    let sKlassIdContext = `targetFilters${sSplitter}klassIds`;
    let oLazyMSSModel = this.getMSSModel(aSelectedTypes, [], oProps.context, sKlassIdContext, true, false, this.handleKPIDetailedClassTypePopOverOpened)
    oLazyMSSModel.bShowIcon = true;
    oLazyMSSModel.referencedData = oReferencedData.referencedKlasses;
    oLazyMSSModel.requestResponseInfo = !CS.isEmpty(oProps.lazyMSSReqResInfo) ? oProps.lazyMSSReqResInfo.targetKlasses : {};

    return {
      klassIds: oLazyMSSModel,
      taxonomy: this.getTaxonomyView()
    }
  };

  getKPILevelModel = () => {
    let oProps = this.props;
    let oActiveKPI = oProps.activeBlock;
    let oReferencedData = oProps.referencedData;

    let oView = (
        <KPIDrilldownView
            drillDowns={oActiveKPI.drillDowns}
            selectedDrillDownTags={oReferencedData.selectedDrillDownTags}
            selectedTaxonomyLabels={oReferencedData.selectedTaxonomyLabels}
            referencedTags={oProps.referencedTags}
        />
    );

    return {
      drillDowns: oView
    }
  };

  getKPIFrequencyModel = () => {
    let oProps = this.props;
    let aFrequencyList = MockDataForFrequency();
    let oActiveKPI = oProps.activeBlock;
    let aSelectedFrequency = [];
    if (!CS.isEmpty(oActiveKPI.frequency)) {
      aSelectedFrequency.push(oActiveKPI.frequency);
    }

    return {
      frequency: this.getMSSModel(aSelectedFrequency, aFrequencyList, oProps.context, "frequency", false, true)
    }
  };

  getKPIRolesModel = () => {
    let oProps = this.props;
    let _this = this;
    let oActiveKPI = oProps.activeBlock;
    let oReferencedData = oProps.referencedData;
    let aUserList = oReferencedData.userList;
    let oReferencedRoles = oReferencedData.referencedRoles;
    let oSelectedRoles = oActiveKPI.roles;
    let oModel = {};
    let sSplitter = ViewUtils.getSplitter();
    CS.forEach(oReferencedRoles, function (oRole) {
      let sRoleId = oRole.id;
      let aSelectedRoleUsers = oSelectedRoles[sRoleId] || [];

      let sInnerContext = `roles${sSplitter}${sRoleId}`;
      oModel[sRoleId] = _this.getMSSModel(aSelectedRoleUsers, aUserList, oProps.context, sInnerContext, oRole.isMultiselect, false)
    });
    return oModel;
  };

  getModulesToShowAccordingtoTabId = () => {
    let sActiveTabId = this.props.activeTabId;
    let aModulesToShow = [];
    switch (sActiveTabId) {
      case KpiConfigDictionary.KPI_INFORMATION:
        aModulesToShow = [KpiConfigDictionary.KPI_INFORMATION, KpiConfigDictionary.TARGET, KpiConfigDictionary.FREQUENCY, KpiConfigDictionary.ROLES, KpiConfigDictionary.LEVELS, KpiConfigDictionary.PARTNERS];
        break;
      case KpiConfigDictionary.COMPLETENESS_BLOCK:
        aModulesToShow = [KpiConfigDictionary.COMPLETENESS_BLOCK];
        break;
      case KpiConfigDictionary.CONFORMITY_BLOCK:
        aModulesToShow = [KpiConfigDictionary.CONFORMITY_BLOCK];
        break;
      case KpiConfigDictionary.UNIQUENESS_BLOCK:
        aModulesToShow = [KpiConfigDictionary.UNIQUENESS_BLOCK]; //, "rules"];
        break;
      case KpiConfigDictionary.ACCURACY_BLOCK:
        aModulesToShow = [KpiConfigDictionary.ACCURACY_BLOCK];
        break;
    }
    return aModulesToShow;
  };

  getKPIConfigModules = () => {
    let aModulesToShow = this.getModulesToShowAccordingtoTabId();
    let aViews = [];
    let _this = this;

    CS.forEach(aModulesToShow, function (sModule) {
      let oModel = {};
      let oSectionLayout = new SectionLayout();
      let aSectionLayout = oSectionLayout[sModule]; //TODO: replace with sModule
      switch (sModule) {
        case KpiConfigDictionary.KPI_INFORMATION:
          oModel = _this.getKPIInformationModel();
          break;

        case KpiConfigDictionary.TARGET:
          oModel = _this.getKPITargetModel();
          break;

        case KpiConfigDictionary.FREQUENCY:
          oModel = _this.getKPIFrequencyModel();
          break;

        case KpiConfigDictionary.ROLES:
          oModel = _this.getKPIRolesModel();
          break;

        case KpiConfigDictionary.COMPLETENESS_BLOCK:
        case KpiConfigDictionary.ACCURACY_BLOCK:
        case KpiConfigDictionary.UNIQUENESS_BLOCK:
        case KpiConfigDictionary.CONFORMITY_BLOCK:
          let sModuleNameForView = sModule.replace("Block", "");
          oModel = _this.getRuleBlockModel(sModuleNameForView);
          break;

        case KpiConfigDictionary.LEVELS:
          oModel = _this.getKPILevelModel();
          break;
      }
      aViews.push(
          <div className={sModule} key={sModule}>

            <CommonConfigSectionView context={_this.props.context} sectionLayout={aSectionLayout}
                                     data={oModel}/>
          </div>
      )
    });
    return aViews;
  };

  render() {

    return (
        <div className={"kpiDetailConfigView"}>
          {this.getKPIConfigModules()}
        </div>
    )
  }
}

export const view = KPIDetailedConfigView;
export const events = oEvents;
