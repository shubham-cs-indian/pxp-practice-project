import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as SelectionToggleView } from '../../../../../viewlibraries/selectiontoggleview/selection-toggle-view';
import ViewUtils from '../view/utils/view-utils';
import { view as LazyMSSView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import { view as MultiSelectSearchView } from './../../../../../viewlibraries/multiselectview/multi-select-search-view';
import { view as SmallTaxonomyView } from '../../../../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import { view as GridYesNoPropertyView } from '../../../../../viewlibraries/gridview/grid-yes-no-property-view';
import { view as SystemsSelectionView } from './systems-selection-view';
import SmallTaxonomyViewModel from './../../../../../viewlibraries/small-taxonomy-view/model/small-taxonomy-view-model';
import copy from 'copy-to-clipboard';
import alertify from '../../../../../commonmodule/store/custom-alertify-store';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import {view as MSSView} from "../../../../../viewlibraries/multiselectview/multi-select-search-view";

const oEvents = {
  SETTING_SCREEN_ROLE_DATA_CHANGED: "setting_screen_role_data_changed",
  ROLE_MODULE_PERMISSION_CHANGED: "role_module_permission_changed",
  ROLE_MODULE_DASHBOARD_VISIBILITY_TOGGLED: "role_module_dashboard_visibility_toggled",
  ROLE_MODULE_READ_ONLY_PERMISSION_TOGGLED: "role_module_read_only_permission_toggled"
};

const oPropTypes = {
  selectedRoleDetailedModel: ReactPropTypes.object.isRequired,
  entitiesList: ReactPropTypes.array.isRequired,
  hideSystemsSelectionView: ReactPropTypes.bool,
  isUserSystemAdmin: ReactPropTypes.bool,
  checkBoxData: ReactPropTypes.object,
  sContext: ReactPropTypes.string
};

// @CS.SafeComponent
class RoleDetailView extends React.Component {
  constructor(props) {
    super(props);

    this.roleName = React.createRef();
  }

  updateTextInputValues = () => {
    var oRoleMode = this.props.selectedRoleDetailedModel;
    if(this.roleName.current){
      this.roleName.current.value = oRoleMode.label;
    }
  };

  componentDidMount() {
    this.updateTextInputValues();
  }

  componentDidUpdate() {
    this.updateTextInputValues();
  }

  handleRoleNameBlur = (oModel, sContext, oEvent) => {
    var sNewValue = oEvent.target.value;
    if (oModel.label != sNewValue) {
      this.handleRoleDataChanged(oModel, sContext, oEvent);
    }
  };

  handleRoleDataChanged = (oModel, sContext, oEvent) => {
    var sNewValue = oEvent.target.value;
      EventBus.dispatch(oEvents.SETTING_SCREEN_ROLE_DATA_CHANGED, this, oModel, sContext, sNewValue);
  };

  handleModulePermissionChanged = (sModule) => {
    EventBus.dispatch(oEvents.ROLE_MODULE_PERMISSION_CHANGED, {}, sModule);
  };

  handleDashboardVisibilityToggled = () => {
    EventBus.dispatch(oEvents.ROLE_MODULE_DASHBOARD_VISIBILITY_TOGGLED);
  };

  handleReadOnlyPermissionToggled = () => {
    EventBus.dispatch(oEvents.ROLE_MODULE_READ_ONLY_PERMISSION_TOGGLED);
  };

  handleCopyToClipboardClick = (sValue) => {
    copy(sValue) ? alertify.success(getTranslation().CODE_COPIED) : alertify.error(getTranslation().COPY_TO_CLIPBOARD_FAILED);
  }

  getModuleRightView = (aEntitiesList) => {
    var _this = this;
    var oRoleMode = this.props.selectedRoleDetailedModel;
    var aModuleRightScreen = oRoleMode.modules;
    var sClassName = "modulePermission";
    let sDisabledClassName = this.props.sContext === "roleCloneDialog" ? " disabled" : "";
    return (
        <div className="moduleRightContainer parentContainer">
          {
            aEntitiesList.map(function (oEntity) {
              return (
                  <div key={oEntity.id}
                       className={sClassName + (CS.includes(aModuleRightScreen, oEntity.id) ? " selected" : "") + sDisabledClassName}
                       onClick={sDisabledClassName === " disabled" ? CS.noop : _this.handleModulePermissionChanged.bind(_this, oEntity.id)}>
                    {oEntity.label}
                  </div>
              );
            })
          }
        </div>
    );
  };

  getMSSView = (
    oData,
    sContextKey,
    bIsLoadMoreEnabled,
    fSearchHandler,
    fOnPopOverOpen,
    fLoadMoreHandler,
  ) => {
    if (CS.isEmpty(oData)) {
      return null;
    } else {
      let sSplitter = ViewUtils.getSplitter();
      sContextKey = sContextKey || oData.context || "";
      let sContext = `role${sSplitter}${sContextKey}`;
      return (<MultiSelectSearchView context={sContext} items={oData.items} selectedItems={oData.selectedItems}
                                     cannotRemove={oData.cannotRemove} isMultiSelect={oData.isMultiSelect}
                                     isLoadMoreEnabled={bIsLoadMoreEnabled} searchText={oData.searchText}
                                     searchHandler={fSearchHandler}
                                     onPopOverOpen={fOnPopOverOpen}
                                     loadMoreHandler={fLoadMoreHandler} disabled={oData.disabled}
                                     selectedObjects={oData.selectedObjects}/>)
    }
  };

  getRoleDetailTargetKlassRequestModel =(sUrl, sActiveOrganisationId) => {
    return {
      requestType: "customType",
      responsePath: ["success", "list"],
      requestURL: sUrl,
      customRequestModel: {
        organizationId: sActiveOrganisationId,
        selectionType: "Klass"
      }
    }
  }

  getAllowedUsersMSSModel = (sUrl) => {
    return {
      requestType: "customType",
      responsePath: ["success"],
      requestURL: sUrl,
      customRequestModel: {
      }
    }
  }

  getLazyMSSView =(oData) => {
    return (<LazyMSSView
        {...oData}
    />)
  }

  getToggleSelectionView = (oData, sContextKey) => {
    if (CS.isEmpty(oData)) {
      return null;
    } else {
      return (<SelectionToggleView selectedItems={oData.selectedItems} items={oData.items} context={"role"} disabledItems={oData.disabledItems}
                                   contextKey={sContextKey}/>)
    }
  };

  getCheckBoxView = (sContext) => {
    if (this.props.sContext !== "roleCloneDialog") {
      return null;
    }
    let oCheckboxData = this.props.checkBoxData[sContext];
    let sClassName = "itemCheckbox";
    sClassName = sClassName + (oCheckboxData.checked && " checkedItem" || "");
    sClassName = sClassName + (oCheckboxData.disabled && " isDisabled" || "");

    return <div className={sClassName} onClick={this.props.handleCheckBoxClicked.bind(this, sContext)}/> ;
  };

  getYesNoPropertyView = (bIsPropertyEnable, fOnChange) => {
    let bIsDisabled = this.props.sContext === "roleCloneDialog" ? true : false;
    return (<GridYesNoPropertyView
        isDisabled={bIsDisabled}
        onChange={fOnChange}
        value={bIsPropertyEnable}
    />)
  };

  getSmallTaxonomyViewModel = (oMultiTaxonomyData) => {

    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    var sRootTaxonomyId = ViewUtils.getTreeRootId();
    var oProperties = {};
    oProperties.headerPermission = this.props.sContext === "roleCloneDialog" ? {"canAddTaxonomy": false} : {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };

  getGetAvailabilityView = (oMultiTaxonomyData) => {
    if (CS.isEmpty(oMultiTaxonomyData)) {
      return null;
    }
    var oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oMultiTaxonomyData);
    let oPaginationData = oMultiTaxonomyData.paginationData;
    let oAllowedTaxonomyHierarchyList = oMultiTaxonomyData.allowedTaxonomyHierarchyList;
    let bIsDisabled = this.props.sContext === "roleCloneDialog" ? true : false;

    return (<SmallTaxonomyView model={oSmallTaxonomyViewModel} isDisabled={bIsDisabled} context={"roles"}
                               showAllComponents={!bIsDisabled} paginationData={oPaginationData} showCustomIcon={true}
                               allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}/>);
  };

  getSelectedSystemsView = (oSystemsSelectionViewModel) => {
    if (this.props.hideSystemsSelectionView) {
      return null;
    }

    return (
        <SystemsSelectionView
            contextMenuViewModel={oSystemsSelectionViewModel.contextMenuViewModel}
            endpointsSelectionViewModels={oSystemsSelectionViewModel.endpointsSelectionViewModels}
            context={oSystemsSelectionViewModel.context}
            selectedItems={oSystemsSelectionViewModel.selectedItems}/>
    );
  };

  getAllowedKPIsView = (oKPIData) => {
    let sLabel = getTranslation().KPI_MENU_ITEM_TILE;

    let oReqResObj = {
      requestType: "configData",
      entityName: "kpis",
    };

    let sSplitter = ViewUtils.getSplitter();
    let bIsDisabled = this.props.sContext === "roleCloneDialog" ? true : false;
    return (
        <div className="kpiSelectorContainer parentContainer">
          {this.getCheckBoxView("kpi")}
          <div className="titleBox">{sLabel}</div>
          <div className="roleValueContainer">
            <LazyMSSView
                isMultiSelect={true}
                disabled={bIsDisabled}
                selectedItems={oKPIData.selectedKPIs}
                context={`role${sSplitter}kpis`}
                cannotRemove={false}
                showColor={false}
                referencedData={oKPIData.referencedKPIs}
                requestResponseInfo={oReqResObj}
            />
          </div>

        </div>
    );
  };

  getPortalWiseEntitiesSelectionView = (aSelectedPortals) => {
    let _props = this.props;
    let aViewsToReturn = [];
    let aEntitiesList = _props.entitiesList;
    CS.forEach(aEntitiesList, (oEntityGroup) => {
      CS.includes(aSelectedPortals, oEntityGroup.portalId) && aViewsToReturn.push(<div
          className="modulesListContainer parentContainer" key={oEntityGroup.portalId}>
        {this.getCheckBoxView("entities")}
        <div className="titleBox">{oEntityGroup.label}</div>
        <div className="roleValueContainer">
          {this.getModuleRightView(oEntityGroup.entities)}
        </div>
      </div>);
    })
    return aViewsToReturn;
  };

  getEnableDashboardView = (bIsDashboardEnable) => {
    let sEnableDashboard = getTranslation().ENABLE_DASHBOARD;
    let oEnableDashboardView = !this.props.isUserSystemAdmin ?
                               <div className="parentContainer">
                                 {this.getCheckBoxView("enableDashboard")}
                                 <div className="titleBox">{sEnableDashboard}</div>
                                 <div className="roleValueContainer">
                                   {this.getYesNoPropertyView(bIsDashboardEnable, this.handleDashboardVisibilityToggled)}
                                 </div>
                               </div> : null;
    return oEnableDashboardView;
  }

  getNameContainerView = (oModel) => {
    if (this.props.sContext === "roleCloneDialog") {
      return null;
    }
    let sName = getTranslation().NAME;
    return (<div className="nameContainer parentContainer">
          <div className="titleBox">{sName}</div>
          <div className="roleValueContainer">
            <input className="roleDetailInput roleName"
                   ref={this.roleName} onBlur={this.handleRoleNameBlur.bind(this, oModel, 'label')}
                   placeholder={oModel.properties["code"]}/>
          </div>
        </div>
    );
  };

  getCodeContainerView = (oProperties) => {
    if (!oProperties.hasOwnProperty("code")) {
      return null;
    }
    let sLabel = oProperties.code;
    let sCode = getTranslation().CODE;
    return (<div className="codeContainer parentContainer">
          <div className="titleBox">{sCode}</div>
          <div className="roleValueContainer">
            <input className="roleDetailInput roleCode"
                   value={sLabel} disabled/>
            <TooltipView placement={"right"} label={getTranslation().COPY_TO_CLIPBOARD_TOOLTIP}>
              <div className={"copyToClipboard"}
                   onClick={this.handleCopyToClipboardClick.bind(this, sLabel)}>
              </div>
            </TooltipView>
          </div>
        </div>
    );
  };

  getRoleTypeView = (oRoleTypeData, bIsReadOnlyUser) => {
    let sRoleType = getTranslation().ROLE_TYPE;
    let oReadOnlyView = this.props.sContext === "roleCloneDialog" ? null : this.getReadOnlyView(bIsReadOnlyUser);
    return (
        <div className="roleType parentContainer">
          {this.getCheckBoxView("roleType")}
          <div className="titleBox">{sRoleType}</div>
          <div className="roleValueContainer">
            {this.getMSSView(oRoleTypeData, "roleType")}
            {oReadOnlyView}
          </div>
        </div>
    );
  };

  getReadOnlyView = (bIsReadOnlyUser) => {
    let sReadOnlyUser = getTranslation().READ_ONLY;
    let oReadOnlyView = this.props.sContext === "roleCloneDialog" ?
                        (<div className="parentContainer">
                          {this.getCheckBoxView("readOnly")}
                          <div className="titleBox">{sReadOnlyUser}</div>
                          <div className="roleValueContainer">
                            {this.getYesNoPropertyView(bIsReadOnlyUser, this.handleReadOnlyPermissionToggled)}
                          </div>
                        </div>) : (<div className="roleTypeReadOnly">
          <div className="titleBox">{sReadOnlyUser}</div>
          {this.getYesNoPropertyView(bIsReadOnlyUser, this.handleReadOnlyPermissionToggled)}
        </div>);
    return oReadOnlyView;
  };

  getPhysicalCatalogView = (oPhysicalCatalogData) => {
    let sPhysicalCatalog = getTranslation().PHYSICAL_CATALOG;

    let oPhysicalCatalogView = this.props.isUserSystemAdmin ? null :
                               (<div className="physicalCatalogs parentContainer">
                                 {this.getCheckBoxView("physicalCatalog")}
                                 <div className="titleBox">{sPhysicalCatalog}</div>
                                 <div className="roleValueContainer">
                                   {this.getToggleSelectionView(oPhysicalCatalogData, "physicalCatalogs")}
                                 </div>
                               </div>);
    return oPhysicalCatalogView;
  };
  getPortalView = (oPortalData) => {
    if (this.props.sContext === "roleCloneDialog") {
      return null;
    }
    let sPortal = getTranslation().PORTAL;
    let oPortalView = this.props.isUserSystemAdmin ? null :
                      (<div className="portals parentContainer">
                        <div className="titleBox">{sPortal}</div>
                        <div className="roleValueContainer">
                          {this.getToggleSelectionView(oPortalData, "portals")}
                        </div>
                      </div>);
    return oPortalView
  };

  getTaxonomiesView = (oAvailabilityData) => {
    let sTaxonomies = getTranslation().TAXONOMIES;
    return (
        <div className="availability parentContainer">
          {this.getCheckBoxView("taxonomy")}
          <div className="titleBox">{sTaxonomies}</div>
          <div className="roleValueContainer">
            {this.getGetAvailabilityView(oAvailabilityData)}
          </div>
        </div>
    );
  };

  getTargetClassesView = (oTargetKlassData, sActiveOrganizationId, sSplitter) => {
    let bIsDisabled = this.props.sContext === "roleCloneDialog" ? true : false;
    let oTargetKlassMSSModel = ViewUtils.getLazyMSSViewModel(oTargetKlassData.selectedItems,
        oTargetKlassData.items, `role${sSplitter}targetKlasses`,
        this.getRoleDetailTargetKlassRequestModel(oTargetKlassData.url, sActiveOrganizationId),
        true, [], bIsDisabled, true);
    let sTargetClasses = getTranslation().TARGET_CLASSES;
    return (
        <div className="targetKlasses parentContainer">
          {this.getCheckBoxView("targetClasses")}
          <div className="titleBox">{sTargetClasses}</div>
          <div className="roleValueContainer">
            {this.getLazyMSSView(oTargetKlassMSSModel)}
          </div>
        </div>);
  };

  getUsersView = (aAddedUsers, sUrlForAllowedUsers, oSelectedUserObjects, sSplitter) => {
    if (this.props.sContext === "roleCloneDialog") {
      return null;
    }

    let sUsers = getTranslation().USERS;
    let oAllowedUsersMSSModel = ViewUtils.getLazyMSSViewModel(aAddedUsers,
        oSelectedUserObjects, `role${sSplitter}users`,
        this.getAllowedUsersMSSModel(sUrlForAllowedUsers));
    return (
        <div className="userListContainer parentContainer">
          <div className="titleBox">{sUsers}</div>
          <div className="roleValueContainer">
            {this.getLazyMSSView(oAllowedUsersMSSModel)}
          </div>
        </div>);
  };

  render () {
    //todo refactor
    var oModel = this.props.selectedRoleDetailedModel;
    let oPortalData = oModel.properties['portalData'];
    if(oModel.properties.isEmpty) {
      return null;
    }
    let bIsDashboardEnable = oModel.properties['isDashboardEnable'];
    let oKPIData = oModel.properties['kpiData'];
    let sSplitter = ViewUtils.getSplitter();
    let oSelectedSystemsViewModel = oModel.properties['selectedSystemsViewModel'];
    let oSelectedSystemsDOM = this.getSelectedSystemsView(oSelectedSystemsViewModel);
    let oKpiSelectionView = this.props.isUserSystemAdmin || !bIsDashboardEnable ? null : this.getAllowedKPIsView(oKPIData);
    let aPortalWiseEntitiesSelectionView = this.getPortalWiseEntitiesSelectionView(oPortalData.selectedItems);

    return (
        <div className="roleFormContainer">
          {this.getNameContainerView(oModel)}
          {this.getCodeContainerView(oModel.properties)}
          {this.getRoleTypeView(oModel.properties['roleTypeData'], oModel.properties['isReadOnly'])}
          {this.props.sContext === "roleCloneDialog" && this.getReadOnlyView(oModel.properties['isReadOnly'])}
          {this.getPhysicalCatalogView(oModel.properties['physicalCatalogData'])}
          {this.getPortalView(oPortalData)}
          {this.getTaxonomiesView(oModel.properties['availabilityData'])}
          {this.getTargetClassesView(oModel.properties['targetKlassData'],
              oModel.properties['activeOrganizationId'], sSplitter)}
          {/*users list*/}
          {this.getUsersView(oModel.addedUsers, oModel.properties['usersURL'], oModel.properties['selectedUserObjects'], sSplitter)}
          {this.getEnableDashboardView(bIsDashboardEnable)}
          {oKpiSelectionView}
          {aPortalWiseEntitiesSelectionView}
          {oSelectedSystemsDOM}
        </div>
           );
  }
}


export const view = RoleDetailView;
export const events = oEvents;
