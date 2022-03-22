import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import { view as ContentTileListView } from './content-tile-list-view';
import { view as EntityDetailsView } from './entity-detail-view';
import { view as QuickListView } from './quicklist-view'
import { view as OnBoardingFileMappingView } from './onboarding-file-mapping-view';
import { view as ContentTimelineComparisonView } from './content-timeline-comparison-view';
import { view as ContentGridEditView } from './content-grid-edit-view';
import { view as ContentScreenDialogView } from './content-screen-dialog-view';
import ContentScreenConstants from './../store/model/content-screen-constants';
import ContentComparisonDialogView from '../../../../../viewlibraries/contentcomparisonview/content-comparison-dialog-view';
import ContentComparisonFullScreenView from '../../../../../viewlibraries/contentcomparisonview/content-comparison-fullscreen-view';
import oFilterPropType from '../tack/filter-prop-type-constants';
import CloneWizardViewNew from './clone-wizard-view-new';
import FullScreenViewObj from '../../../../../viewlibraries/fullscreenview/fullscreen-view';

import ContentSectionViewModel from './model/content-section-view-model';
import BreadCrumbModuleAndHelpScreenIdDictionary from "../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary";
import {view as BulkEditDialogView} from "./bulk-edit-view";
let getTranslation = ScreenModeUtils.getTranslationDictionary;

let FullScreenView = FullScreenViewObj.view;

const oEvents = {
  TIMELINE_COMPARISON_CLOSE_BUTTON_CLICKED: "timeline_comparison_close_button_clicked",
  CONTENT_GRID_EDIT_CLOSE_BUTTON_CLICKED: "content_grid_edit_close_button_clicked",
};

const oPropTypes = {
  entityViewData: ReactPropTypes.object,
  filterViewData: ReactPropTypes.object,
  toolbarData: ReactPropTypes.object,
  rightPanelView: ReactPropTypes.object,
  viewMode: ReactPropTypes.oneOf(CS.values(ContentScreenConstants.viewModes)).isRequired,
  relationshipViewMode: ReactPropTypes.object,
  navigationData: ReactPropTypes.object,
  thumbnailMode: ReactPropTypes.string,
  activeCollection: ReactPropTypes.object,
  activeXRayPropertyGroup: ReactPropTypes.object,
  xRayData: ReactPropTypes.object,
  variantSectionViewData: ReactPropTypes.object,
  UOMVariantViewData: ReactPropTypes.object,
  newComparisonViewData: ReactPropTypes.object,
  relationshipContextData: ReactPropTypes.object,
  viewMasterData: ReactPropTypes.object,
  entityHeaderTagsEditData: ReactPropTypes.object,
  contentGridEditData : ReactPropTypes.object,
  shouldHideTabs: ReactPropTypes.bool,
  isHierarchyMode: ReactPropTypes.bool,
  selectedImageId: ReactPropTypes.string,
  collectionData: ReactPropTypes.object,
  isAcrolinxSidebarVisible: ReactPropTypes.bool,
  selectedIdsForCloningWizardData: ReactPropTypes.object,
  showVersionCountField: ReactPropTypes.number
};

// @CS.SafeComponent
class ContentThumbsContainerView extends React.Component {
  constructor (oProps) {
    super(oProps);
  }
  static propTypes = oPropTypes;

  getOnboardingFileMappingView = () => {
    var __props = this.props;
    var oEntityViewData = __props.entityViewData;
    let oFileMappingViewData = oEntityViewData.fileMappingViewData;
    var aEndPointMappingList = oFileMappingViewData.endPointMappingList;
    var oActiveEndpoint = oFileMappingViewData.activeEndpoint;
    var oUnmappedColumnValuesList = oFileMappingViewData.unmappedColumnValuesList;
    return (<OnBoardingFileMappingView
              unmappedColumnValuesList={oUnmappedColumnValuesList}
              activeTabId={oFileMappingViewData.activeTabId}
              selectedMappingFilterId={oFileMappingViewData.selectedMappingFilterId}
              mappingFilterProps={oFileMappingViewData.mappingFilterProps}
              activeEndpoint={oActiveEndpoint}
              endPointReqResInfo={oFileMappingViewData.endPointReqResInfo}
              propertyRowTypeData={oFileMappingViewData.propertyRowTypeData}
              endPointReferencedData={oFileMappingViewData.endPointReferencedData}
              endPointMappingList={aEndPointMappingList}/>);
  };

  handleContentScreenDialogButtonClicked = (sContext) => {
    window.dontRaise = true;
    if(this.props.entityViewData.comparisonViewData && (sContext === "goldenRecordComparison" || sContext === "contentComparison")){
      EventBus.dispatch(oEvents.TIMELINE_COMPARISON_CLOSE_BUTTON_CLICKED, sContext);
    }
  };

  handleContentGridEditCloseButtonClicked = (oEvent) => {
    EventBus.dispatch(oEvents.CONTENT_GRID_EDIT_CLOSE_BUTTON_CLICKED, oEvent);
  }

  getViewToBeRendered = () => {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    let oView = null;
    let oDialogView = null;
    switch (oEntityViewData.screen) {
      case BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT:
        oDialogView = this.getDialogViewForEntityEditMode(oEntityViewData);
        oView = this.getEntityEditModeView(oDialogView);
        break;
      case BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST:
        let sActiveEntityId = oEntityViewData.activeEntity.id; //verify : activeContent / activeSet / activeCollection replaced by activeEntity
        let oAddEntityInRelationship = oEntityViewData.addEntityInRelationshipScreenData;
        oDialogView = this.getDialogViewForEntityEditMode(oEntityViewData);
        oView =  this.getAddRelationshipScreenView(oAddEntityInRelationship[sActiveEntityId].id, oDialogView);
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST:
        oDialogView = this.getDialogViewForEntityEditMode(oEntityViewData);
        let oFilterContext = oEntityViewData.presentEntityViewData.filterContext;
        oView = this.getAvailableEntityScreenView('', oDialogView, oFilterContext);
        break;

      case BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST:
        oView = this.getCollectionQuickListView();
        break;

      case "products":
        // Do not add any check here
        if (!CS.isEmpty(oEntityViewData.fileMappingViewData)) {
          oView = this.getOnboardingFileMappingView();
        }
        else {
          oDialogView = this.getDialogForProductsViewScreen(oEntityViewData);
          oView = this.getContentTileListView(oDialogView);
        }
        break;

      case "hierarchy":
        oDialogView = this.getDialogForProductsViewScreen(oEntityViewData);
        oView = this.getContentTileListView(oDialogView);
        break;
    }

    return (
        <div className="contentViewContainer ">
          {oView}
        </div>
    );
  };

  getGoldenRecordMatchAndMergeView = () => {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    var oComparisonViewData = oEntityViewData.comparisonViewData;
    let bIsFullScreen = oComparisonViewData.isFullScreenMode;

    if(bIsFullScreen) {
      return <ContentComparisonFullScreenView comparisonViewData={oComparisonViewData}/>;
    }
    else {
      return <ContentComparisonDialogView comparisonViewData={oComparisonViewData}/>;
    }
  };

  getCollectionQuickListView = () => {
    let __props = this.props;
    let oFilterViewData = __props.filterViewData;
    let oVariantSectionViewData = __props.variantSectionViewData;

    return (
        <QuickListView
            entityViewData={__props.entityViewData}
            selectedSectionId={""}
            collectionData={__props.entityViewData.collectionData}
            presentEntityNavigationData={__props.navigationData}
            filterContext={oFilterViewData.filterContext}
            variantSectionViewData={oVariantSectionViewData}
        />
    );
  };

  getContentTileListView = (oDialogView) => {
    let __props = this.props;
    let oViewMasterData = this.props.viewMasterData;
    var oCollectionData = __props.entityViewData.collectionData;
    var oActiveCollection = oCollectionData && oCollectionData.activeCollection;
    let oActiveCollectionData = {
      activeCollection: oActiveCollection,
      isEditCollectionScreen: oCollectionData && oCollectionData.isEditCollectionScreen
    };
    let sClassName = "contentListViewWrapper";

    return (
        <div className={sClassName}>
        <ContentTileListView
            filterView={__props.filterView}
            entityViewData={__props.entityViewData}
            currentZoom={__props.currentZoom}
            filterViewData={__props.filterViewData}
            shouldHideTabs={__props.shouldHideTabs}
            viewMode={__props.viewMode}
            toolbarData={__props.toolbarData}
            navigationData={__props.navigationData}
            thumbnailMode={__props.thumbnailMode}
            collectionData={oActiveCollectionData}
            activeXRayPropertyGroup={__props.activeXRayPropertyGroup}
            xRayData={__props.xRayData}
            viewMasterData={oViewMasterData.contentTileListViewMasterData}
            ruleViolation={__props.ruleViolation}
            relationshipViewMode={__props.relationshipViewMode}
            isHierarchyMode={__props.isHierarchyMode}
            hideAddEntityButton={__props.showGoldenRecordBuckets}
            canClone={__props.canClone}
            dataIntegrationLogsViewData={__props.dataIntegrationLogsViewData}
        />
          {oDialogView}
        </div>
    )
  };

  getContentComparisonView = () => {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    var oComparisonViewData = oEntityViewData.comparisonViewData;
    let sHeaderLabel = oComparisonViewData && oComparisonViewData.headerLabel || "";
    let sContext = oComparisonViewData.isGoldenRecordViewSourcesDialogOpen ? "goldenRecordComparison" : "contentComparison";
    let fOnButtonClickHandler = this.handleContentScreenDialogButtonClicked.bind(this, sContext);
    let aButtonData = [{
      id: "apply",
      label: getTranslation().OK,
      isFlat: false,
      isDisabled: false
    }];

    return (
        <ContentScreenDialogView
            buttonData={aButtonData}
            title={sHeaderLabel}
            onRequestCloseHandler={fOnButtonClickHandler}
            onButtonClickHandler={fOnButtonClickHandler}>
          <ContentTimelineComparisonView timelineData={oComparisonViewData}/>
        </ContentScreenDialogView>
    );
  };

  getBulkEditView = () => {
    let oEntityViewData = this.props.entityViewData;
    let oBulkEditViewData = oEntityViewData.bulkEditViewData;
    let oFilterViewData = this.props.filterViewData;
    let oFilterContext = oFilterViewData.filterContext;
    return (
        <BulkEditDialogView bulkEditViewData={oBulkEditViewData}
                            filterContext={oFilterContext}
        />
    );
  };

  getContentGridEditView = () => {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    let oContentGridEditData = oEntityViewData.contentGridEditData;
    let oContentGridEditViewData = oContentGridEditData.contentGridEditViewData;
    let sLabelProduct = (oContentGridEditViewData.gridViewData.length < 2 ) ? getTranslation().PRODUCT : getTranslation().PRODUCTS;
    let sLabel = getTranslation().CONTENT_GRID_EDIT_TITLE + " " + oContentGridEditViewData.gridViewData.length + " " + sLabelProduct;
    let fOnRequestCloseHandler = this.handleContentGridEditCloseButtonClicked;

    let oView = <ContentGridEditView contentGridEditViewData={oContentGridEditViewData}/>;
    let oDialogView = (
        <FullScreenView
            header={sLabel}
            showHeader={true}
            bodyView={oView}
            isFullScreenMode={oContentGridEditViewData.isContentGridEditViewOpen}
            fullScreenHandler={fOnRequestCloseHandler}
        />
    );

    return oDialogView;
  };

  getCloningWizardView = (oCloningWizardData) => {
    if (CS.isNotEmpty(oCloningWizardData) && oCloningWizardData.isCloningWizardOpen) {
         return (
             <CloneWizardViewNew
                 {...oCloningWizardData}
                 filterContext={this.props.filterContext}
             />
        )
      }
  };

  getDialogViewForEntityEditMode = (oEntityViewData) => {
    let oCloningWizardData = oEntityViewData.cloningWizardData || {};
    let oComparisonData = oEntityViewData.comparisonViewData;
    if (CS.isNotEmpty(oCloningWizardData)) {
      return this.getCloningWizardView(oCloningWizardData);
    } else if(oComparisonData && oComparisonData.isGoldenRecordViewSourcesDialogOpen){
      return this.getContentComparisonView();
    } else if(oComparisonData && oComparisonData.isGoldenRecordRemergeSourcesTabClicked){
      return <ContentComparisonFullScreenView comparisonViewData={oComparisonData}/>;
    }
    else {
      return null;
    }
  };

  getDialogForProductsViewScreen = (oEntityViewData) => {
    let oContentGridEditData = oEntityViewData.contentGridEditData;
    let oComparisonViewData = oEntityViewData.comparisonViewData;
    let oBulkEditDialogData = oEntityViewData.bulkEditViewData || {};
    if(oComparisonViewData && oComparisonViewData.isContentComparisonMode){
      return this.getContentComparisonView();
    }
    else if(oContentGridEditData && oContentGridEditData.isContentGridEditMode){
      return this.getContentGridEditView();
    }
    else if (oBulkEditDialogData && oBulkEditDialogData.isBulkEditViewOpen) {
      return this.getBulkEditView();
    }
    else if (CS.isNotEmpty(oEntityViewData.cloningWizardData)) {
      return this.getCloningWizardView(oEntityViewData.cloningWizardData);
    }
    else if(oComparisonViewData && oComparisonViewData.isGoldenRecordMatchAndMergeViewOpen){
      return this.getGoldenRecordMatchAndMergeView();
    }
  };

  getNavigationDataForRelationshipSectionLoadMore = (oRelationshipToolbarProps) => {
    let aContentList = [];
    try {
      aContentList = oRelationshipToolbarProps.elements;
    } catch (oException) {
      ExceptionLogger.error("Error :" + oException); // eslint-disable-line
      return {};
    }

    let oNavigationData = {};
    oNavigationData.from = oRelationshipToolbarProps.startIndex;
    oNavigationData.totalContents = oRelationshipToolbarProps.totalCount;
    oNavigationData.currentPageItems = aContentList.length;
    oNavigationData.pageSize = oRelationshipToolbarProps.paginationLimit;
    return oNavigationData;
  };

  getRelationshipNavigationData = (sRelationshipId) => {
    let __props = this.props;
    let oElement = {};
    let oEntityViewData = __props.entityViewData;
    let oVariantSectionViewData = __props.variantSectionViewData;
    let oNatureSectionViewData = {};
    CS.forEach(oEntityViewData.natureSectionViewData, function (_oNatureSectionViewData) {
      let oNatureRelationship = _oNatureSectionViewData.natureRelationship;
      if (oNatureRelationship && oNatureRelationship.sideId == sRelationshipId) {
        oNatureSectionViewData = _oNatureSectionViewData;
        return false;
      }
    });


    let oRelationshipToolbarProps = {};
    if (!CS.isEmpty(oNatureSectionViewData)) {
      /** keep the commented line*/
      oRelationshipToolbarProps = oNatureSectionViewData.relationshipToolbarProps;
    } else if (oVariantSectionViewData.isVariantRelationshipAddViewVisible) {
      /** keep the commented line*/
      return {};
    } else if (oVariantSectionViewData.isProductVariant) {
      oRelationshipToolbarProps = oVariantSectionViewData.relationshipToolbarProps;
    } else {
      let aSectionModel = oEntityViewData.sectionViewModel;
      CS.forEach(aSectionModel, function (oSectionModel) {
        let oElements = oSectionModel.elements;
        let aElements = CS.values(oElements);

        CS.forEach(aElements, function (oRelationshipElement) {
          if (oRelationshipElement.type == "relationship" && oRelationshipElement.id == sRelationshipId) {
            oElement = oRelationshipElement;
          }
        });
      });
      oRelationshipToolbarProps = oElement.relationshipToolbarProps;
    }

    if (CS.isEmpty(oRelationshipToolbarProps)) {
      return {}
    }

    return this.getNavigationDataForRelationshipSectionLoadMore(oRelationshipToolbarProps);
  };

  getAddRelationshipScreenView = (sSelectedSectionId, oDialogView) => {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    let oVariantSectionViewData = __props.variantSectionViewData;
    let oNavigationData = this.getRelationshipNavigationData(sSelectedSectionId);
    let oNatureSectionViewData = {};
    CS.forEach(oEntityViewData.natureSectionViewData, function (_oNatureSectionViewData) {
      var oNatureRelationship = _oNatureSectionViewData.natureRelationship;
      if (oNatureRelationship && oNatureRelationship.sideId == sSelectedSectionId) {
        oNatureSectionViewData = _oNatureSectionViewData;
        return false;
      }
    });
    if (!CS.isEmpty(oNatureSectionViewData)) {
      oEntityViewData.natureSectionViewData = oNatureSectionViewData;
    }
    let oClonedEntityViewData = oEntityViewData;
    oClonedEntityViewData.sectionViewModel = this.getSectionViewModels(oClonedEntityViewData.sectionViewModel);
    let oFilterContextForEntityRelationshipAddView = {
      filterType: oFilterPropType.MODULE,
      screenContext: sSelectedSectionId
    };

    return (
      <React.Fragment>
        <QuickListView
          entityViewData={oClonedEntityViewData}
          variantSectionViewData={oVariantSectionViewData}
          selectedSectionId={sSelectedSectionId}
          presentEntityNavigationData={oNavigationData}
          filterContext={oFilterContextForEntityRelationshipAddView}
        />
        {oDialogView}
      </React.Fragment>
    );
  };

  getSectionViewModels = (aSections) => {
    let aSectionModel = [];
    CS.forEach(aSections, function (oSection) {
      aSectionModel.push(new ContentSectionViewModel(
        oSection.id, CS.getLabelOrCode(oSection), oSection.rows, oSection.columns, oSection.icon, oSection.elements, oSection.properties
      ))
    });
    return aSectionModel;
  };

  getAvailableEntityScreenView = (sSelectedSectionId, oDialogView, oFilterContextForEntityRelationshipAddView) => {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    let oVariantSectionViewData = __props.variantSectionViewData;
    let oNavigationData = this.getRelationshipNavigationData(sSelectedSectionId);
    let oNatureSectionViewData = {};
    CS.forEach(oEntityViewData.natureSectionViewData, function (_oNatureSectionViewData) {
      var oNatureRelationship = _oNatureSectionViewData.natureRelationship;
      if (oNatureRelationship && oNatureRelationship.relationshipId == sSelectedSectionId) {
        oNatureSectionViewData = _oNatureSectionViewData;
        return false;
      }
    });
    if (!CS.isEmpty(oNatureSectionViewData)) {
      oEntityViewData.natureSectionViewData = oNatureSectionViewData;
    }
    //todo : temporary solution for add relationship as it expects model not object
    let oClonedEntityViewData = oEntityViewData;
    oClonedEntityViewData.sectionViewModel = this.getSectionViewModels(oClonedEntityViewData.sectionViewModel);
    return (
      <React.Fragment>
        <QuickListView
          entityViewData={oClonedEntityViewData}
          variantSectionViewData={oVariantSectionViewData}
          selectedSectionId={sSelectedSectionId}
          presentEntityNavigationData={oNavigationData}
          filterContext={oFilterContextForEntityRelationshipAddView}
        />
        {oDialogView}
      </ React.Fragment>
    );
  };

  getEntityEditModeView = (oDialogView) => {
    let __props = this.props;
    let oFilterViewData = __props.filterViewData;
    let oFilterContext = oFilterViewData.filterContext;


    return (<div className="contentListViewWrapper">
      <EntityDetailsView
        entityViewData={__props.entityViewData}
        currentZoom={__props.currentZoom}
        rightPanelView={__props.rightPanelView}
        ruleViolation={__props.ruleViolation}
        relationshipViewMode={__props.relationshipViewMode}
        xRayData={__props.xRayData}
        thumbnailMode={__props.thumbnailMode}
        variantSectionViewData={__props.variantSectionViewData}
        relationshipContextData={__props.relationshipContextData}
        entityHeaderTagsEditData={__props.entityHeaderTagsEditData}
        selectedImageId={__props.selectedImageId}
        isAcrolinxSidebarVisible={__props.isAcrolinxSidebarVisible}
        filterContext={oFilterContext}
      />
      {oDialogView}
    </div>);
  };

  render() {

    return (
        <div className="contentThumbContainer">
          {this.getViewToBeRendered()}
        </div>
    );
  }
}

export const view = ContentThumbsContainerView;
export const events = oEvents;
