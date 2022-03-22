import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import CommonUtils from '../../../../../commonmodule/util/common-utils';
import { view as ExpandableNestedMenuListView } from '../../../../../viewlibraries/expandablenestedmenulistview/expandable-nested-menu-list-view';
import ModuleDictionary from '../../../../../commonmodule/tack/module-dictionary';
import { view as BreadcrumbView } from '../../../../../viewlibraries/breadcrumbview/breadcrumb-wrapper-view';
import TabsDictionary from '../../../../../commonmodule/tack/template-tabs-dictionary';
import { view as ContentEditToolbarView } from './content-edit-toolbar-view';
import { view as MSSView } from '../../../../../viewlibraries/multiselectview/multi-select-search-view';
import { view as ButtonView } from '../../../../../viewlibraries/buttonview/button-view';
import { view as ButtonWithContextMenuView } from '../../../../../viewlibraries/buttonwithcontextmenuview/button-with-context-menu-view';
import ContentComparisonFullScreenView from '../../../../../viewlibraries/contentcomparisonview/content-comparison-fullscreen-view';
import oFilterPropType from '../tack/filter-prop-type-constants';
import DashboardTabDictionary from "../screens/dashboardscreen/tack/dashboard-tab-dictionary";
import {view as InformationTabView} from "./information-tab-view";
import {view as ContentThumbsContainerView} from "./content-thumbs-container-view";
import { view as ContextMenuViewNew } from './../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import TooltipView from "../../../../../viewlibraries/tooltipview/tooltip-view";
import ViewUtils from "../../../../../viewlibraries/utils/view-library-utils";
import {view as ImageSimpleView} from "../../../../../viewlibraries/imagesimpleview/image-simple-view";
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';

const oEvents = {
  CONTENT_WRAPPER_VIEW_HEADER_BACK_BUTTON_CLICKED: "content_wrapper_view_header_back_button_clicked",
  CONTENT_WRAPPER_VIEW_MODULE_CLICKED: "content_wrapper_view_module_clicked",
  CONTENT_WRAPPER_VIEW_DASHBOARD_RESET_PREVENTED: "content_wrapper_view_dashboard_reset_prevented",
  EDIT_VIEW_FILTER_OPTION_CHANGED: "edit_view_filter_option_changed",
  CONTENT_EDIT_COLLECTION_ICON_CLICKED: "content_edit_collection_icon_clicked",
  EDIT_VIEW_TOGGLE_ACROLINX_SIDEBAR: "edit_view_toggle_acrolinx_sidebar",
  CONTENT_SCREEN_WRAPPER_VIEW_CONTENT_DATA_LANGUAGE_CHANGED: "content_edit_view_content_data_language_changed",
  CONTENT_SCREEN_WRAPPER_VIEW_DATA_LANGUAGE_POPOVER_VISIBILITY_TOGGLED: "content_screen_wrapper_view_data_language_popover_visibility_toggled",
  CONTENT_SCREEN_WRAPPER_VIEW_TRANSLATION_DELETE_CLICKED: "content_screen_wrapper_view_translation_delete_clicked",
};

const oPropTypes = {
  headerViewData: ReactPropTypes.object,
  selectedEndpoint: ReactPropTypes.string,
  runtimeView: ReactPropTypes.object,
  moduleList: ReactPropTypes.array,
  selectedModuleId: ReactPropTypes.string,
  isDashboardModuleSelected: ReactPropTypes.bool,
  taskDashboardView: ReactPropTypes.object,
  workFlowWorkBenchView:ReactPropTypes.object,
  isInsideDataIntegration: ReactPropTypes.bool,
  showModuleSidebar: ReactPropTypes.bool,
  activeEndpointData: ReactPropTypes.object,
  runtimeMappingDialogView: ReactPropTypes.object,
  forceShowRuntimeView: ReactPropTypes.bool,
  preventDashboardDataReset: ReactPropTypes.bool,
  hideHeader: ReactPropTypes.bool,
  informationTabView: ReactPropTypes.object,
  damTabView: ReactPropTypes.object,
  breadCrumbPath: ReactPropTypes.array,
  viewMode: ReactPropTypes.string,
  upperSectionToolbarData:  ReactPropTypes.object,
  selectedEntityTabId: ReactPropTypes.string,
  activeTask:  ReactPropTypes.object
};

// @CS.SafeComponent
class ContentScreenWrapperView extends React.Component {

  constructor (props) {
    super(props);

    this.handleHeaderBackButtonClicked = this.handleHeaderBackButtonClicked.bind(this);
  }

  componentDidUpdate () {
    let bDashboardRecentPrevented = this.props.selectedModuleId === "dashboard" || this.props.isInsideDataIntegration;
    EventBus.dispatch(oEvents.CONTENT_WRAPPER_VIEW_DASHBOARD_RESET_PREVENTED, bDashboardRecentPrevented);
  }

  handleHeaderBackButtonClicked =()=> {
    EventBus.dispatch(oEvents.CONTENT_WRAPPER_VIEW_HEADER_BACK_BUTTON_CLICKED, false);
  }

  handleModuleClicked =(sModuleId)=> {
    let oFilterContext = {
      screenContext: oFilterPropType.MODULE,
      filterType: oFilterPropType.MODULE
    };
    EventBus.dispatch(oEvents.CONTENT_WRAPPER_VIEW_MODULE_CLICKED, sModuleId, oFilterContext);
  }

  handleFilterOptionChanged  =(aIds)=> {
    EventBus.dispatch(oEvents.EDIT_VIEW_FILTER_OPTION_CHANGED, aIds[0]);
  };

  toggleAcrolinxSidebar  =()=> {
    EventBus.dispatch(oEvents.EDIT_VIEW_TOGGLE_ACROLINX_SIDEBAR,);
  }


  handleEditCollectionIconClicked= ()=> {
    EventBus.dispatch(oEvents.CONTENT_EDIT_COLLECTION_ICON_CLICKED);
  };

  handleContentDataLanguageChanged = (oSelectedItem) => {
    EventBus.dispatch(oEvents.CONTENT_SCREEN_WRAPPER_VIEW_CONTENT_DATA_LANGUAGE_CHANGED, oSelectedItem.id);
  };

  handleDeleteTranslationClicked = (oLanguage) => {
    let sLanguageCode = oLanguage.properties.code;
    EventBus.dispatch(oEvents.CONTENT_SCREEN_WRAPPER_VIEW_TRANSLATION_DELETE_CLICKED, sLanguageCode);
  };

  handleDataLanguagePopoverVisibilityToggled = () => {
    EventBus.dispatch(oEvents.CONTENT_SCREEN_WRAPPER_VIEW_DATA_LANGUAGE_POPOVER_VISIBILITY_TOGGLED);
  };

  getImageView = (sKey) => {
    let oImageView;
    oImageView = sKey ? <ImageSimpleView classLabel="contextMenuIcon" imageSrc={ViewUtils.getIconUrl(sKey)}/> : <div className={"contextMenuIcon " + "imageSimpleViewNoImage"}></div>;
    return (<div className="selectedItemIcon">{oImageView}</div>)
  }

  getSwitchContentDataLanguageView = (oRequiredData) => {
    let aReferencedLanguages = oRequiredData.referencedLanguages;
    let sSelectedDataLanguageId = oRequiredData.selectedDataLanguageCode;
    let oSwitchContentDataLanguageData = oRequiredData.switchContentDataLanguageDropdownData;
    let oSelectedItem = CS.find(aReferencedLanguages, {code: sSelectedDataLanguageId}) || {};
    let aSelectedId = oSelectedItem.id ? [oSelectedItem.id] : [];
    let sClassName = oSwitchContentDataLanguageData.isPopoverVisible ? "dataLanguagePopoverVisible" : "";
    let bShowDeleteTranslationButton = !oSwitchContentDataLanguageData.isDeleteTranslationButtonDisabled;
    let sLabelClassName = "itemLabel ";
    let sLabel, oIconView;
    let oToolTipView = null;
    if (CS.isEmpty(oSelectedItem)) {
      sLabel = "–";
      sLabelClassName += "nothingSelected ";
      oToolTipView =  <div className={sLabelClassName}>{sLabel}</div>;
    } else {
      sLabel = CS.getLabelOrCode(oSelectedItem);
      sLabelClassName += "selectedItemLabel ";
      oToolTipView =  <TooltipView placement="top" label={sLabel}><div className={sLabelClassName}>{sLabel}</div></TooltipView>;
      oIconView = this.getImageView(oSelectedItem.iconKey);
    }

    return (
        <div className='nonEmptyDataSwitchLanguage'>
          <ContextMenuViewNew
              selectedItems={aSelectedId}
              contextMenuViewModel={this.getContextMenuItems(oRequiredData.referencedLanguages, oSelectedItem.code)}
              showCustomIcon={true}
              onDeleteHandler={this.handleDeleteTranslationClicked}
              showDeleteIcon={bShowDeleteTranslationButton}
              targetOrigin={{horizontal: 'right', vertical: 'top'}}
              anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
              showHidePopoverHandler={this.handleDataLanguagePopoverVisibilityToggled}
              showPopover={oSwitchContentDataLanguageData.isPopoverVisible}
              onPopOverOpenedHandler={this.handleDataLanguagePopoverVisibilityToggled}
              className={sClassName}
              disabled={oSwitchContentDataLanguageData.isDisabled}
              onClickHandler={this.handleContentDataLanguageChanged}
          >
            <div className="selectedItemsWrapper">
              <div className="selectedItems">
                {oIconView}
                {oToolTipView}
              </div>
            </div>
          </ContextMenuViewNew>
        </div>
    );
  };

  getCreateTranslationView = (oRequiredData) => {
    let oCreateTranslationData = oRequiredData.createTranslationDropdownData;
    let oRequestData = oCreateTranslationData.requestInfoData;
    let sId = oRequestData.customRequestModel.id;
    let oContextMenuViewData = {
      isMultiSelect: false,
      contextMenuViewModel: [],
      requestResponseInfoData: oRequestData,
      showPopover: oCreateTranslationData.isPopoverVisible,
      disabled: oCreateTranslationData.isPopoverDisabled,
      showDefaultIcon: true,
    };
    let sClassName = oCreateTranslationData.isPopoverVisible ? "showLanguageListButton popoverVisible": "showLanguageListButton";
    return (
        <div className='emptyDataSwitchLanguage'>
          <ButtonWithContextMenuView
              key={"dataLanguage_" + sId}
              context={"createTranslation"}
              className={sClassName + " languageActionButton"}
              useCase={"showCustomDynamicList"}
              contextMenuData={oContextMenuViewData}
              targetOrigin={{horizontal: 'right', vertical: 'top'}}
              anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
              isClickedNodeItemReturn={true}
              tooltip={getTranslation().ADD_LANGUAGE}
              tooltipPlacement={"bottom"}
              isDisabled={oCreateTranslationData.isButtonDisabled}
          />
        </div>
    );
  };

  getContextMenuModel (oRequiredData) {
    let aList = oRequiredData.referencedLanguages;
    let sItemToExclude = oRequiredData.selectedDataLanguageCode;
    let oHelperLanguageData = oRequiredData.helperLanguageDropdownData;
    let aModelsList =  this.getContextMenuItems(aList, sItemToExclude);

    let bIsHelperLanguageDisabled = oHelperLanguageData.isDisabled;

    return {
        isMultiSelect: false,
        showSearch: true,
        contextMenuViewModel: aModelsList,
        excludedItems: [sItemToExclude],
        requestResponseInfoData: oHelperLanguageData.requestInfoData,
        showPopover: oHelperLanguageData.isPopoverVisible,
        context: "helperLanguage",
        disabled: bIsHelperLanguageDisabled

    }
  };

  getContextMenuItems (aList, sItemToExclude) {
    let aModelsList = [];
    CS.forEach(aList, function (oListItem) {
      if (sItemToExclude !== oListItem.code) {
        aModelsList.push(new ContextMenuViewModel(
            oListItem.id,
            CS.getLabelOrCode(oListItem),
            false,
            oListItem.iconKey,
            {
              code: oListItem.code,
              customIconClassName: "imageSimpleViewNoImage",
            }
        ));
      }
    });
    return aModelsList;
  }

  getHelperLanguageView = (oRequiredData) => {
    let oContextMenuViewData = this.getContextMenuModel(oRequiredData);
    let oHelperLanguageData = oRequiredData.helperLanguageDropdownData;
    let bIsHelperLanguageSelected = oHelperLanguageData.isSelected;
    let bIsHelperLanguageDisabled = oHelperLanguageData.isDisabled;
    let sClassName = bIsHelperLanguageSelected ? "helperLanguageListButton selected " : "helperLanguageListButton ";
    sClassName += bIsHelperLanguageDisabled ? " disabled" : "";

    return (
        <div className='helperLanguageSelectionDropDown'>
          <ButtonWithContextMenuView
              key={"helperLanguage_"}
              context={"helperLanguage"}
              className={sClassName + " languageActionButton"}
              useCase={"showList"}
              contextMenuData={oContextMenuViewData}
              targetOrigin={{horizontal: 'right', vertical: 'top'}}
              anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
              isClickedNodeItemReturn={true}
              tooltip={getTranslation().COMPARE_AND_EDIT_LANGUAGES}
              tooltipPlacement={"bottom"}
              isDisabled={oContextMenuViewData.disabled}
          />
        </div>
    )
  };

  getContentDataLanguageView = (oRequiredData) => {
    return (
        <div className="switchContentLanguageContainer" key={"switchContentLanguageContainer"}>
          {this.getSwitchContentDataLanguageView(oRequiredData)}
          {this.getCreateTranslationView(oRequiredData)}
          {this.getHelperLanguageView(oRequiredData)}
        </div>
    );
  };

  getBreadCrumbToolbarData = () => {
    let _this = this;
    let __props = _this.props;
    let oToolbarData = this.props.upperSectionToolbarData;
    let oActiveTask = __props.activeTask;
    let aToolBarView = [];
    if (!CS.isEmpty(oToolbarData)) {
      let aExtraButtons = oToolbarData.extraButtons;
      CS.forEach(aExtraButtons, function (oButton) {
        switch (oButton.id) {
          case "ContentEditFilterView":

            aToolBarView.push(
                <div className="filterViewWrapper" key="filterViewWrapper">
                  <MSSView
                      disabled={oButton.isDisabled}
                      isMultiSelect={false}
                      cannotRemove={true}
                      items={oButton.items}
                      selectedItems={[oButton.selectedItems]}
                      onApply={_this.handleFilterOptionChanged}
                      showSearch={false}
                      showCustomIcon={true}
                  />
                </div>
            );
            break;

          case "acrolinxToggleButtonView":
            let sAcrolinxToggleButtonClass = "acrolinxToggleButton ";
            if (oButton.isVisible) {
              sAcrolinxToggleButtonClass += "isActive";
            }
            aToolBarView.push(
                <div className={"acrolinxToggleButtonViewContainer"} key="acrolinxToggleButtonViewContainer">
                  <ButtonView id={oButton.id}
                              showLabel={false}
                              isDisabled={false}
                              className={sAcrolinxToggleButtonClass}
                              type={"simple"}
                              onChange={_this.toggleAcrolinxSidebar}
                              theme={"light"}/>
                </div>);
            break;

          case "editCollection":
            aToolBarView.push(
                <div className="collectionEditContainer" key="collectionEditContainer">
                  <ButtonView id={oButton.id}
                              showLabel={false}
                              tooltip={oButton.tooltip}
                              placement={"left"}
                              isDisabled={false}
                              className={oButton.className}
                              type={"simple"}
                              onChange={_this.handleEditCollectionIconClicked}
                              theme={"light"}/>
                </div>
            );
            break;

          case "languageInfoButtons":
            aToolBarView.push(_this.getContentDataLanguageView(oButton.extraData));
            break;

        }
      });

      let oToolbarItems = {
        moreButtons: oToolbarData.moreButtons,
        textButtons: oToolbarData.textButtons,
      };
      let bIsSaveCommentDisabled = false;
      let sActiveTabId = this.props.selectedEntityTabId;
      if (sActiveTabId === TabsDictionary.TASKS_TAB) {
        bIsSaveCommentDisabled = true;
      }
      aToolBarView.push(
          <ContentEditToolbarView
              key="conEditToolbar"
              toolbarItems={oToolbarItems}
              isSaveCommentDisabled={bIsSaveCommentDisabled}
              activeTask={oActiveTask}
          />
      );
    }
    return aToolBarView;
  };

  getBreadCrumbView = (aBreadCrumbPath) => {
    if (CS.isEmpty(aBreadCrumbPath)) {
      return null;
    }
    let oToolbarData = this.getBreadCrumbToolbarData();
    return (<BreadcrumbView breadcrumbPath={aBreadCrumbPath} toolbarView={oToolbarData}/>);
  };

  getInformationTabView = () => {
    let oDashboardTabData = this.props.entityViewData.dashboardTabData;
    return (
        <InformationTabView
            {...oDashboardTabData}
        />);
  };

  getInnerViewForDashboardTabs = () => {
    let oEntityViewData = this.props.entityViewData;
    let sSelectedDashboardTabId = oEntityViewData.selectedDashboardTabId;
    switch (sSelectedDashboardTabId) {
      case DashboardTabDictionary.INFORMATION_TAB:
        return this.getInformationTabView();
      case DashboardTabDictionary.DAM_TAB:
        return this.getInformationTabView();
      case DashboardTabDictionary.BUCKETS_TAB:
        return this.getContentExplorerView();
    }
  };

  getDashboardController = () => {
    let aModuleList = this.props.moduleList;
    let bUserHasPimPermission = !!CS.find(aModuleList, {id: ModuleDictionary.PIM});
    let oDashboardScreenDependencies = CommonUtils.getDashboardScreenDependency();
    /** */
    let oInnerView = this.getInnerViewForDashboardTabs();
    const DashboardScreenController = oDashboardScreenDependencies.controller;
    let sKey = this.props.preventDashboardDataReset ? null : Math.random();
    return (
        <DashboardScreenController
            store={oDashboardScreenDependencies.store}
            action={oDashboardScreenDependencies.action}
            taskData={this.props.taskData}
            preventDataReset={this.props.preventDashboardDataReset}
            runtimeMappingDialogView={this.props.runtimeMappingDialogView}
            runtimeView={oInnerView}
            showModuleSidebar={this.props.showModuleSidebar}
            userHasPimPermission={bUserHasPimPermission}
            key = {sKey}
        />
    );
  };

  getContentExplorerView = () => {
    let _props = this.props;
    let iCurrentZoom = _props.currentZoom;
    let sContentScreenContainerClassName = "contentScreenContainer " + ("zoomLevel" + "_" + iCurrentZoom);
    return (
        <div className={sContentScreenContainerClassName}>
          <ContentThumbsContainerView
              entityViewData={_props.entityViewData}
              filterViewData={_props.filterViewData}
              filterView={_props.filterView}
              toolbarData={_props.toolbarData}
              viewMode={_props.viewMode}
              relationshipViewMode={_props.relationshipViewMode}
              currentZoom={_props.currentZoom}
              ruleViolation={_props.ruleViolation}
              navigationData={_props.navigationData}
              thumbnailMode={_props.thumbnailMode}
              activeXRayPropertyGroup={_props.activeXRayPropertyGroup}
              xRayData={_props.xRayData}
              variantSectionViewData={_props.variantSectionViewData}
              viewMasterData={_props.viewMasterData}
              relationshipContextData={_props.relationshipContextData}
              isHierarchyMode={_props.isHierarchyMode}
              selectedImageId={_props.selectedImageId}
              showGoldenRecordBuckets={_props.showGoldenRecordBuckets}
              entityHeaderTagsEditData={_props.entityHeaderTagsEditData}
              canClone={_props.canClone}
              dataIntegrationLogsViewData={_props.dataIntegrationLogsViewData}
          />
        </div>
    );
  };


  render () {

    let aModuleList = this.props.moduleList;
    let sActiveTabId = this.props.selectedModuleId;
    let oView = null;
    let bIsSidePanelExpanded = this.props.isSidePanelExpanded;

    if (!this.props.forceShowRuntimeView && sActiveTabId === "dashboard") {
      oView = this.getDashboardController();
    } else {
      oView = this.getContentExplorerView();
    }
    let oBreadCrumbView = this.getBreadCrumbView(this.props.breadCrumbPath);

    /**
     * oModuleSideBarView renders left section of ContentScreenWrapperView
     * */
    var oModuleSideBarView = null;
    if (this.props.showModuleSidebar) {
      oModuleSideBarView = (<ExpandableNestedMenuListView linkItemsData={aModuleList}
                                                          onItemClick={this.handleModuleClicked}
                                                          selectedItemId={sActiveTabId}
                                                          isExpanded={bIsSidePanelExpanded}/>);
    }

    /**
     * oRightSectionContainer renders right section of ContentScreenWrapperView
     * */
    var oRightSectionContainer = (
      <div className="rightSectionContainer">
        {oBreadCrumbView}
        {oView}
      </div>);

    return (
        <div className="contentScreenWrapperView">
          <div className="mainSection">
            {oModuleSideBarView}
            {oRightSectionContainer}
          </div>
        </div>
    );
  }
}

ContentScreenWrapperView.propTypes = oPropTypes;

export const view = ContentScreenWrapperView;
export const events = oEvents;
