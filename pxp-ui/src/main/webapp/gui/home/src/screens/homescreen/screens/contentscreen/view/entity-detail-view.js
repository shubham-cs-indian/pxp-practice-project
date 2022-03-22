import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as TagGroupView } from '../../../../../viewlibraries/taggroupview/tag-group-view.js';
import { view as ContentEditView } from './content-edit-view';
import BaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import ClassNameFromBaseTypeDictionary from '../../../../../commonmodule/tack/class-name-base-types-dictionary';
import { view as ContextMenuViewNew } from './../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import { view as EntityContextSummaryView } from './entity-context-summary-view';
import { view as CircledTagGroupView } from './../../../../../viewlibraries/circledtaggroupview/circled-tag-group-view';
import CircledTagGNodeModel from './../../../../../viewlibraries/circledtaggroupview/model/circled-tag-node-model';
import SmallTaxonomyViewModel from '../../../../../viewlibraries/small-taxonomy-view/model/small-taxonomy-view-model';
import MasterTagListContext from '../../../../../commonmodule/HOC/master-tag-list-context';
import { view as AssetSchedulerSectionView } from './asset-scheduler-section-view';
import { view as UOMView } from './uom-view';
import { view as AttributeContextDialogView } from '../../../../../viewlibraries/attributecontextview/attribute-context-dialog-view';
import { view as ContentSectionViewNew } from './content-section-view-new';
import { view as RelationshipWrapperView } from './relationship-wrapper-view';
import ContentScreenConstants from '../store/model/content-screen-constants';
import ViewUtils from './utils/view-utils';
import { view as KPITileView } from '../../../../../viewlibraries/dashboardview/dashboard-view';
import KpiTileTypeList from './../tack/kpi-tile-type-list';
import ContentScreenViewContextConstants from '../tack/content-screen-view-context-constants';
import { view as UniqueSelectionView } from './../../../../../viewlibraries/uniqueSelectionView/unique-selection-view.js';
import oFilterPropType from '../tack/filter-prop-type-constants';
import { view as PropertyCouplingIconsView } from "./relationship-conflict-coupling-view";
import { view as ContentDownloadTrackerView } from "./content-download-tracker-view";
import ContentTabSpecificView from "./content-tab-specific-view";
import { view as MultiClassificationSectionView } from "./multiclassification-section-view";
import { view as MultiClassificationView } from "./multiclassification-view";
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
var getTranslation = ScreenModeUtils.getTranslationDictionary;
let SectionTypes = ContentScreenConstants.sectionTypes;

const oEvents = {
  TEMPLATE_DROP_DOWN_LIST_NODE_CLICKED: 'template_drop_down_list_node_clicked',
  HANDLE_KPI_CHART_TYPE_SELECTED: "handle_kpi_tile_type_selected",
  LINKED_ITEM_BUTTON_CLICKED : "linked_item_button_clicked"
};

const oPropTypes = {
  entityViewData: ReactPropTypes.object,
  currentZoom: ReactPropTypes.number,
  rightPanelView: ReactPropTypes.object,
  ruleViolation: ReactPropTypes.object,
  relationshipViewMode: ReactPropTypes.object,
  thumbnailMode: ReactPropTypes.string,
  variantSectionViewData: ReactPropTypes.object,
  UOMVariantViewData: ReactPropTypes.object,
  relationshipContextData: ReactPropTypes.object,
  entityHeaderTagsEditData: ReactPropTypes.object,
  xRayData: ReactPropTypes.object,
  selectedImageId: ReactPropTypes.string,
  isAcrolinxSidebarVisible: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object,
  masterTagListFromContext: ReactPropTypes.object,
  downloadInfoData: ReactPropTypes.object,
};

// @MasterTagListContext
// @CS.SafeComponent
class EntityDetailView extends React.Component {

  static propTypes = oPropTypes;

  state = {
    showTaxonomy: false,
    showClass: false,
    showNatureClass: false,
    showTemplateList: false
  };

  handleLinkedSectionButtonClicked = (oItem) => {
    EventBus.dispatch(oEvents.LINKED_ITEM_BUTTON_CLICKED, oItem, this.props.filterContext);
  };

  handleKpiChartTypeSelected = (sSelectedItem) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_CHART_TYPE_SELECTED, sSelectedItem);
  };

  getImageSrcForThumbnail = (oContent) => {
    var aList = oContent.referencedAssets;
    if(oContent.baseType == BaseTypeDictionary.assetBaseType) {
      aList = oContent.attributes;
    }
    var oImage = CS.find(aList, {isDefault: true});
    if(CS.isEmpty(oImage)) {
      var sKlassId = ViewUtils.getEntityClassType(oContent);
      var oClass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
      var sThumbnailImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      var oImageObject = {};
      oImageObject.thumbnailImageSrc = sThumbnailImageSrc;
      oImageObject.thumbnailType = "";
      return oImageObject;
    }
    return ViewUtils.getImageSrcUrlFromImageObject(oImage);
  };

  getImageSrcOfContent = (oContent, sAssetInstanceId) => {
    let aAllAssetList = [];
    let oAsset = {};
    if (CS.isEmpty(sAssetInstanceId)) {
      var sKlassId = ViewUtils.getEntityClassType(oContent);
      var oClass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);
      var sThumbnailImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";
      let sImageThumbType = "";
      if (CS.isEmpty(sThumbnailImageSrc) && CS.isNotEmpty(oContent.assetInformation)
        && oContent.assetInformation.type === "Image") {
          sImageThumbType = oContent.assetInformation.properties.extension;
      }
      var oImageObject = {};
      oImageObject.thumbnailImageSrc = sThumbnailImageSrc;
      oImageObject.thumbnailType = CS.isEmpty(sImageThumbType) ?  oClass.natureType : sImageThumbType;
      return oImageObject;
    } else if (oContent.baseType == BaseTypeDictionary.assetBaseType) {
      oAsset = oContent.assetInformation;
    } else {
      aAllAssetList = this.props.variantSectionViewData.allAssetList;
      oAsset = CS.find(aAllAssetList, {assetInstanceId: sAssetInstanceId});
    }

    return ViewUtils.getImageSrcUrlFromImageObject(oAsset);
  };

  getRelationshipViewByContextType = (oSectionModel) => {
    let __props = this.props;
    let oViewData = {};
    let sId = "";
    let sLabel = "";
    let aList = [];

    let sRelationshipViewContext = oSectionModel.properties.viewContext;
    let aSplitContext = CS.split(sRelationshipViewContext, ViewUtils.getSplitter());
    let sViewContext = aSplitContext[0];

    if (sViewContext === ContentScreenViewContextConstants.RELATIONSHIP) {
      let oElement = CS.values(oSectionModel.elements)[0];
      oViewData = {
        entityViewData: __props.entityViewData,
        sectionModel: oSectionModel,
        ruleViolation: __props.ruleViolation,
        element: oElement,
        relationshipContextData: __props.relationshipContextData,
        relationshipViewMode: __props.relationshipViewMode,
        filterContext: {
          screenContext: oElement && oElement.id || oSectionModel.id,
          filterType: oFilterPropType.MODULE
        },
        xRayData: __props.xRayData
      };

      sId = oSectionModel.id;
      sLabel = CS.getLabelOrCode(oSectionModel);
    }
    else {
      let oEntityViewData = __props.entityViewData;
      let aNatureSectionViewData = oEntityViewData.natureSectionViewData;
      let oFoundNatureSectionViewData = CS.find(aNatureSectionViewData, {sideId: oSectionModel.id});
      oFoundNatureSectionViewData = oFoundNatureSectionViewData || oSectionModel.viewModel;
      //todo move to store processing
      let oIdLabel = ViewUtils.getNatureRelationshipIdLabelByRelationshipType(oSectionModel.relationshipType);
      sId = oIdLabel.id;
      sLabel = CS.getLabelOrCode(oIdLabel);

      oViewData = {
        natureSectionViewData: oFoundNatureSectionViewData,
        filterContext: {
          screenContext: oSectionModel.id,
          filterType: oFilterPropType.MODULE
        },
      };
    }
    return {
      sectionId: sId,
      view: (<RelationshipWrapperView relationshipViewData={oViewData} viewContext={sViewContext}/>),
      label: sLabel,
      relationshipConflictView: oViewData.element  ? this.getRelationshipCouplingAndConflictView(oViewData.element) : null
    }
  };

  getRelationshipCouplingAndConflictView = (oElement) => {
    return (
        <PropertyCouplingIconsView
            entityInstance={oElement}
            entityViewData={this.props.entityViewData}
            ruleViolation={this.props.ruleViolation}
        />
    )
  }

  getNormalSectionView = (oSectionModel, oEntityViewData, iIndex) => {
    return (
        <ContentSectionViewNew
            key={iIndex}
            referencedClasses={oEntityViewData.referencedClasses}
            referencedContents={oEntityViewData.referencedContents}
            referencedRelationships={oEntityViewData.referencedRelationships}
            referencedTags={oEntityViewData.referencedTags}
            referencedAttributes={oEntityViewData.referencedAttributes}
            referencedTaxonomies={oEntityViewData.referencedTaxonomies}
            referencedCollections={oEntityViewData.referencedCollections}
            referencedTasks={oEntityViewData.referencedTasks}
            selectedElementInformation={oEntityViewData.selectedContext}
            conflictingValues={oEntityViewData.conflictingValues}
            conflictingSources={oEntityViewData.conflictingSources}
            allPossibleConflictingSources={oEntityViewData.allPossibleConflictingSources}
            violatingMandatoryElements={oEntityViewData.violatingMandatoryElements}
            globalPermission={oEntityViewData.globalPermission}
            ruleViolation={this.props.ruleViolation}
            model={oSectionModel}
            helperLanguageInstances={oEntityViewData.helperLanguageInstances}
            isHelperLanguageSelected={oEntityViewData.isHelperLanguageSelected}
            filterContext={this.props.filterContext}
        />
    );
  };

  getUOMView = (oUOMData, iIndex) => {
    let oFilterContext = {
      filterType: oFilterPropType.MODULE,
      screenContext: oUOMData.contextId
    }
    let oEntityViewData = this.props.entityViewData;
    if (oEntityViewData.isHelperLanguageSelected) {
      return null;
    }
    return (
          <UOMView {...oUOMData} key={iIndex} filterContext={oFilterContext}/>
      );
  };

  getSchedulerView = (oEventScheduleSectionViewData, oCurrentTabPermission, bIsDisabled, oActiveEntity) => {
    let oEventData = oEventScheduleSectionViewData.eventData;
    let oTagData = oEventScheduleSectionViewData.tagData;
    let sValidityMessage = oEventScheduleSectionViewData.validityMessage;
    let sContext = "asset";
    let bIsExpired = oActiveEntity.isExpired;
    return (
        <AssetSchedulerSectionView eventData={oEventData}
                                   tagData={oTagData}
                                   tabPermission={oCurrentTabPermission}
                                   validityMessage={sValidityMessage}
                                   isDisabled={bIsDisabled}
                                   context={sContext}
                                        isExpired={bIsExpired}
        />
    );
  };

  getLinkedItemButtonView = (oSection) => {
    let oSelectedContext = oSection.selectedContext;
    var aTabSpecificList = this.props.entityViewData.tabSpecificData;
    var aContextModelList = this.getContextMenuModelList(aTabSpecificList, oSection.id, oSelectedContext);

    let bIsDisabled = aContextModelList.length < 1;
    let oButtonIcon = bIsDisabled ? null : (<div className="linkedItemBtnIcon"/>);
    let oStyle = bIsDisabled ? {} : {display: "inline-block", width: "calc(100% - 20px)"};
    return (

        <ContextMenuViewNew
            contextMenuViewModel={aContextModelList}
            showCustomIcon={true}
            disabled={bIsDisabled}
            onClickHandler={this.handleLinkedSectionButtonClicked.bind(this)}
            anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
            targetOrigin={{horizontal: 'left', vertical: 'top'}}
            showSearch={false}
        >
          <TooltipView placement="bottom" label={CS.getLabelOrCode(oSelectedContext)} key={oSelectedContext.id}>
            <div className="linkedItemButtonView">
              <div className="linkedItemBtnLabel" style={oStyle}>{CS.getLabelOrCode(oSelectedContext)}</div>
              {oButtonIcon}
            </div>
          </TooltipView>
        </ContextMenuViewNew>

    );
  }

  getViewsForActiveSections = () => {
    var __props = this.props;
    var _this = this;

    var oEntityViewData = __props.entityViewData;
    let oOverViewTabData =oEntityViewData.overviewTabData;
    var oActiveEntity = oEntityViewData.activeEntity;
    let aActiveSectionData = oEntityViewData.sectionViewModel; //todo: change key
    var oReferencedPermissions = oEntityViewData.referencedPermissions;
    var oHeaderPermission = oReferencedPermissions.headerPermission;

    let bIsContentDisabled = oEntityViewData.isContentDisabled;
    let oEventScheduleSectionViewData = oEntityViewData.eventScheduleSectionViewData;
    let oTabPermission = oReferencedPermissions.tabPermission;

    var aLinkItemList = [];
    var aSectionalViews = [];

    CS.forEach(aActiveSectionData, (oSection) => {

      let oSectionObj = {
        sectionId: oSection.id,
        view: null
      };

      let oLinkItem = {
        id: oSection.id,
        label: oSection.label,
        iconKey: oSection.iconKey,
        code: oSection.code
      };


      switch (oSection.type) {

        case SectionTypes.SECTION_TYPE_CONTEXT_SELECTION:
          if (!this.getIsContextEmpty()) {
            var oVariantSectionView = this.getVariantContextView();
            oSectionObj.view = oVariantSectionView;
          }
          break;

        case SectionTypes.SECTION_TYPE_CLASS_TAG_INFO:
          var oVariantSectionView = this.getSectionKlassTagInfoView(oSection);
          oSectionObj.view = oVariantSectionView;
          break;

        case SectionTypes.SECTION_TYPE_SCHEDULE_SELECTION:
          oSectionObj.view = this.getSchedulerView(oEventScheduleSectionViewData, oTabPermission, bIsContentDisabled, oActiveEntity);
          break;

        case SectionTypes.SECTION_TYPE_PROPERTY_COLLECTION:
          oSectionObj.view = _this.getNormalSectionView(oSection, oEntityViewData);
          oSectionObj.isPropertyCollectionSection = true;
          break;

        case SectionTypes.SECTION_TYPE_RELATIONSHIP:
        case SectionTypes.SECTION_TYPE_NATURE_RELATIONSHIP:
          oSectionObj = this.getRelationshipViewByContextType(oSection);
          oLinkItem.id = oSectionObj.sectionId;
          oLinkItem.label = CS.getLabelOrCode(oSectionObj);
          oLinkItem.icon = oSection.icon;
          break;

        case SectionTypes.SECTION_TYPE_CONTEXT:
          oSectionObj.view = this.getUOMView(oSection.properties.uomData);
          break;

        case SectionTypes.SECTION_TYPE_PERFORMANCE_INDICES:
          let kpiData = oOverViewTabData.kpiData;
          let oKpiSummaryViewData = kpiData.kpiSummaryViewData;
          let sSelectedKpiChartTypeId = oKpiSummaryViewData.selectedKpiChartType;
          let oProcessedContentKPIDataMap = kpiData.contentKPIDataMap;

          let aKpiTileTypeList = KpiTileTypeList.KpiTileTypes;
          let oSelectedKpiChartType = CS.find(aKpiTileTypeList, {id: sSelectedKpiChartTypeId});

          let oSectionView = null;
          let oSelectChartTypeDom = null;
          let oActiveKPIData = kpiData.activeKPIData;
          if (!CS.isEmpty(oKpiSummaryViewData.kpiList) || !CS.isEmpty(oProcessedContentKPIDataMap)) {
            if (sSelectedKpiChartTypeId === "doughnut") {
              oSectionView = (<KPITileView dashboardDataGovernanceMap={oKpiSummaryViewData}
                                           isKPISummaryView={true}
                                           autoHeight
                                           openDialogButtonVisibility={false}/>);
            }
            else {
              oSectionView = (<KPITileView dashboardDataGovernanceMap={oProcessedContentKPIDataMap}
                                           activeDashboardTileData={oActiveKPIData}
                                           isKPISummaryView={false}
                                           autoHeight
                                           openDialogButtonVisibility={false}/>);
            }
            let aSelectChartTypeDom = [];

            CS.forEach(aKpiTileTypeList, function (oListItem) {
              let sClassName = "icon " + oListItem.id;
              sClassName += (sSelectedKpiChartTypeId === oListItem.id) ? " selected" : "";
              aSelectChartTypeDom.push(
                  <TooltipView placement="bottom" label={CS.getLabelOrCode(oListItem)} key={oListItem.id}>
                    <div className={sClassName} onClick={_this.handleKpiChartTypeSelected.bind(this, oListItem.id)}/>
                  </TooltipView>
              );
            });
            oSelectChartTypeDom = (
                <div className="kpiTileType">
                  {aSelectChartTypeDom}
                </div>);

          } else {
            oSectionView = (
                <div className="kpiSummarySectionViewContainer">
                  <div className="kpiSummarySectionView">
                    <div className="kpiSectionHeader">{getTranslation().PERFORMANCE_INDICES}</div>
                    <div className="nothingToDisplayContainer">
                      <div className="nothingToDisplay">{getTranslation().NO_KPI_TO_DISPLAY}</div>
                    </div>
                  </div>
                </div>
                  );
          }

          oSectionObj.view = (
              <div className="performanceIndexSection">
                {oSelectChartTypeDom}
                {oSectionView}
              </div>);

          break;

        case SectionTypes.SECTION_TYPE_CLASSIFICATION:
          oSectionObj.view = (<MultiClassificationSectionView
              multiClassificationViewData={oOverViewTabData}
              context={"overviewTabSectionView"}
              dialogContext={"multiClassificationDialogView"}
          />);
          break;

        case SectionTypes.SECTION_TYPE_LIFE_CYCLE_STATUS:
          let bIsContentHeaderCollapsed = oEntityViewData.isContentHeaderCollapsed;
          var oEntityData = this.props.entityViewData;
          var aLifeCycleTagGroupModels = oEntityData.overviewTabData.lifeCycleTagGroupModels;
          if (!CS.isEmpty(aLifeCycleTagGroupModels)) {
            oSectionObj.view = oHeaderPermission.viewStatusTags && !bIsContentHeaderCollapsed ?
                <div className="lifeCycleStatusView">
                  {this.getLifeCycleTagsView(oOverViewTabData.lifeCycleTagGroupModels, oHeaderPermission, false, false)}
                </div> : null;
          }

          break;

        case SectionTypes.SECTION_TYPE_LINKED_ITEM_BUTTON:
          /**Adding Button In Linked Item Data*/
          oLinkItem.view = this.getLinkedItemButtonView(oSection);
          oSectionObj.view = null;
          oSectionObj.isSkipped = true;
          break;

        case SectionTypes.SECTION_TYPE_DOWNLOAD_INFO:
          oSectionObj.view = this.getAssetDownloadInfoView();
          break;
        case SectionTypes.SECTION_TYPE_INSTANCE_PROPERTIES:
          oSectionObj.isPropertyCollectionSection = true;
          break;
      }


      aLinkItemList.push(oLinkItem);
      aSectionalViews.push(oSectionObj)
    });

    return {
      linkItemList: aLinkItemList,
      sectionalViews: aSectionalViews
    };
  }

  chooseTemplateButtonClicked = () => {
    this.setState({
      showTemplateList: true
    });
  };

  getContextMenuModelList = (aItemList, sContext, oActiveItem) => {
    oActiveItem = oActiveItem || {};
    var aContextModelList = [];
    CS.forEach(aItemList, function (oItem) {
      if(oActiveItem.id != oItem.id) {
        let sCustomIconClassname = oItem.baseType ? ClassNameFromBaseTypeDictionary[oItem.baseType] : oItem.type;
        let oProperties = {
          context: sContext,
          customIconClassName: sCustomIconClassname
        };
        aContextModelList.push(new ContextMenuViewModel(
            oItem.id,
            CS.getLabelOrCode(oItem),
            false,
            oItem.iconKey,
            oProperties
        ));
      }
    });
    return aContextModelList;
  };

  getSmallTaxonomyViewModel = (oMultiTaxonomyData, oHeaderPermission) => {
    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selectedMultiTaxonomyList;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.multiTaxonomyListToAdd;
    var oEntityData = this.props.entityViewData;
    var sActiveEntitySelectedTabId = oEntityData.entityTabData.activeEntitySelectedTabId;
    var sRootTaxonomyId = -1;
    var oProperties = {};
    oProperties.isTimelineTab = sActiveEntitySelectedTabId === ContentScreenConstants.tabItems.TAB_TIMELINE;
    oProperties.selectedTabId = sActiveEntitySelectedTabId;
    oProperties.headerPermission = oHeaderPermission;
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };

  getEntityHeaderData = () => {
    var oEntityHeaderData = {};
    var oEntityViewData = this.props.entityViewData;
    var oActiveEntity = oEntityViewData.activeEntity;
    let oOverviewTabData = oEntityViewData.overviewTabData;
    let oContentOpenViewLeftSideData = oEntityViewData.contentOpenViewLeftSideData;
    var oVariantSectionViewData = this.props.variantSectionViewData;
    let oImageObject = this.getImageSrcOfContent(oActiveEntity, this.props.selectedImageId);
    var oAssetObject = ViewUtils.getElementAssetData(oActiveEntity);
    var oTemplateListView = this.getTemplateListView();
    let sKlassId = ViewUtils.getEntityClassType(oActiveEntity);
    let oClass = ViewUtils.getKlassFromReferencedKlassesById(sKlassId);

    var oCreationDetails = {};
    oCreationDetails.createdBy = oActiveEntity.createdBy;
    oCreationDetails.createdOn= oActiveEntity.createdOn;

    var oModificationDetails = {};
    oModificationDetails.lastModifiedBy = oActiveEntity.lastModifiedBy;
    oModificationDetails.lastModifiedOn = oActiveEntity.lastModified;

    oEntityHeaderData.entityName = ViewUtils.getContentName(oActiveEntity);
    oEntityHeaderData.entityId = oActiveEntity.id;
    oEntityHeaderData.klassName = oEntityViewData.natureClassLabel;
    oEntityHeaderData.imageObject = oImageObject;
    oEntityHeaderData.assetObject = oAssetObject;
    oEntityHeaderData.coverflowimagemodels = oEntityViewData.coverflowimagemodels;
    oEntityHeaderData.isFirstimeUploded = !!oActiveEntity.isFileUploaded;
    oEntityHeaderData.creationDetails = oCreationDetails;
    oEntityHeaderData.modificationDetails = oModificationDetails;
    oEntityHeaderData.selectedContext = oVariantSectionViewData.selectedContext;
    oEntityHeaderData.variantInstanceId = oActiveEntity.variantInstanceId;
    oEntityHeaderData.activeVariantContextId = oVariantSectionViewData.activeVariantContextId;
    oEntityHeaderData.eventsData = oEntityViewData.eventsData;
    oEntityHeaderData.tasksData = oEntityViewData.tasksData;
    oEntityHeaderData.entityTabData = {
      tabsList: oEntityViewData.entityTabData.entityTabItemList,
      activeTabId: oEntityViewData.entityTabData.activeEntitySelectedTabId
    };
    oEntityHeaderData.entityLabelEditable = oEntityViewData.entityLabelEditable;
    oEntityHeaderData.branchOfLabel = oEntityViewData.branchOfLabel;
    oEntityHeaderData.templateListView = oTemplateListView;
    oEntityHeaderData.referencedPermissions = oEntityViewData.referencedPermissions;
    oEntityHeaderData.activeVariantTagDisplayData =  oEntityViewData.activeVariantTagDisplayData;
    oEntityHeaderData.variantOfLabel = oEntityViewData.variantOfLabel;
    oEntityHeaderData.isContentHeaderCollapsed = oEntityViewData.isContentHeaderCollapsed;
    oEntityHeaderData.isDisableHeader = oEntityViewData.isDisableHeader;
    if(oActiveEntity.baseType == BaseTypeDictionary.assetBaseType){
      oEntityHeaderData.eventScheduleInfoView = this.getSchedulerView(oEntityViewData.eventScheduleSectionViewData, {}, oEntityViewData.isDisableHeader, oActiveEntity);
    }
    oEntityHeaderData.kpiSummaryViewData = oOverviewTabData.kpiData ? oOverviewTabData.kpiData.kpiSummaryViewData : oContentOpenViewLeftSideData.kpiData.kpiSummaryViewData;
    oEntityHeaderData.previewImageSrc = oClass.previewImage ? ViewUtils.getIconUrl(oClass.previewImage) : "";

    /*** Below Properties used in ImageGalleryDialogView ****/
    var oImageGalleryDialogViewData = {};
    oImageGalleryDialogViewData.entityId = oActiveEntity.id;
    oImageGalleryDialogViewData.variantImageUploadDialogStatus = oVariantSectionViewData.variantImageUploadDialogStatus;
    oImageGalleryDialogViewData.defaultAssetInstanceId = oActiveEntity.defaultAssetInstanceId;
    oImageGalleryDialogViewData.allAssetList = oVariantSectionViewData.allAssetList ? oVariantSectionViewData.allAssetList : [];
    oImageGalleryDialogViewData.activeEntityBaseType = oActiveEntity.baseType;
    oEntityHeaderData.imageGalleryDialogViewData = oImageGalleryDialogViewData;
    /************************* End *****************************/

    oEntityHeaderData.isHelperLanguageSelected = oEntityViewData.isHelperLanguageSelected;
    oEntityHeaderData.helperLanguages = oEntityViewData.helperLanguages;
    oEntityHeaderData.sectionHeaderData = oEntityViewData.sectionHeaderData;
    oEntityHeaderData.assetDownloadData = oOverviewTabData.assetDownloadData ? oOverviewTabData.assetDownloadData : oContentOpenViewLeftSideData.assetDownloadData;
    oEntityHeaderData.assetLinkSharingDialogData = oOverviewTabData.assetLinkSharingDialogData ? oOverviewTabData.assetLinkSharingDialogData : oContentOpenViewLeftSideData.assetLinkSharingDialogData;
    oEntityHeaderData.sButtonContext = oEntityViewData.assetShareButtonContext;
    oEntityHeaderData.shareAssetData = oOverviewTabData.shareAssetData ? oOverviewTabData.shareAssetData :oContentOpenViewLeftSideData.shareAssetData;

    let oReferencedPermissions = oEntityViewData.referencedPermissions;
    let oHeaderPermission = oReferencedPermissions.headerPermission;
    let oMultiClassificationOverviewTabData = {taxonomyInheritanceData: oEntityViewData.taxonomyInheritanceData, taxonomyConflictingValues: oEntityViewData.activeEntity.taxonomyConflictingValues};
    oEntityHeaderData.multiClassificationView = oHeaderPermission.showClassTaxonomyInHeader ?
                                                (<MultiClassificationView
                                                 data={oContentOpenViewLeftSideData.multiClassificationData}
                                                 context={"entityHeaderVIew"}
                                                 overviewTabData= {oMultiClassificationOverviewTabData}
                                                />) : null;

    // show lifecycle status only when read permission is available
    oEntityHeaderData.lifeCycleTagsView = oHeaderPermission.viewStatusTags && this.getLifeCycleTagsView(oContentOpenViewLeftSideData.lifeCycleTagGroupModels, oHeaderPermission, true, true);

    oEntityHeaderData.sButtonContext = oEntityViewData.assetShareButtonContext;
    oEntityHeaderData.assetSharedUrl = oOverviewTabData.assetSharedUrl ? oOverviewTabData.assetSharedUrl :oContentOpenViewLeftSideData.assetSharedUrl;
    return oEntityHeaderData;
  };

  getTagSliderView = (oModel) => {
    let oMasterTagList = this.props.masterTagListFromContext;
    let oTagGroupModel = oModel.tagGroupModel;
    let oProperties = oTagGroupModel.properties;
    return (
        <TagGroupView
            tagGroupModel={oTagGroupModel}
            tagRanges={oProperties.tagRanges}
            tagValues={oModel.tagValues}
            disabled={oModel.disabled}
            singleSelect={oProperties.singleSelect}
            hideTooltip= {true}
            masterTagList={oMasterTagList}
            showDefaultIcon={true}
        />
    );
  };

  getLifeCycleTagsView = (aLifeCycleTagGroupModels, oHeaderPermissions, bInsideHeader, isUniqueSelectedSection) => {
    var _this = this;
    var aDom = [];
    CS.forEach(aLifeCycleTagGroupModels, function (oTagGroupModel, iIndex){
      let oTagModel = oTagGroupModel.tagGroupModel;
      let sHeader = CS.getLabelOrCode(oTagModel);
      let bIsTagValueSelected = false;
      let oEntityTag = oTagModel && oTagModel.entityTag;
      if (bInsideHeader && oTagGroupModel.tagGroupModel && oEntityTag.tagValues) {
        CS.forEach(oEntityTag.tagValues, function (oTagValue) {
          if (oTagValue.relevance == 100) {
            bIsTagValueSelected = true;
            return true;
          }
        })
      }

      if(!bInsideHeader || bIsTagValueSelected) {
        let oTagSliderView = isUniqueSelectedSection ? _this.getUniqueSelectionView(oTagGroupModel.tagGroupModel) : _this.getTagSliderView(oTagGroupModel);
        let oView = <div className="contentInformationSectionWrapper" key={iIndex}>
          {_this.getIconDOM(oTagModel.iconKey)}
          <div className="sectionTitle">{sHeader}</div>
          <div className="contentInformationSection">
            <div className="lifecycleTagsContainer">{oTagSliderView}</div>
          </div>
        </div>;
        aDom.push(oView);
      }
    });

    return aDom;
  };

  getIconDOM = (sKey) => {
    let sThumbnailImageSrc = ViewUtils.getIconUrl(sKey);
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sThumbnailImageSrc}/>
        </div>
    );
  };

  getUniqueSelectionView = (oTagGroupModel) => {
    return (
        <div className="tagsGroupContainer">
          <UniqueSelectionView
              rulerTagGroupModel={oTagGroupModel}
              isDisabled={true}
              masterTagList={this.props.masterTagListFromContext}
              showDefaultIcon={true}
          />
        </div>
    );
  }

  changeTemplate = (oModel) => {
    var oEntityData = this.props.entityViewData;
    var aTemplateList = oEntityData.templateList;
    var sId = oModel.id;
    var oTemplate = CS.find(aTemplateList, {id:sId});

    EventBus.dispatch(oEvents.TEMPLATE_DROP_DOWN_LIST_NODE_CLICKED, oTemplate, this.props.filterContext);
    this.setState({
      showTemplateList: false
    });
  };

  getTemplateListView = () => {
    var _this = this;
    var oEntityData = _this.props.entityViewData;
    var aTemplateList = oEntityData.templateList;
    var oActiveTemplate = oEntityData.activeTemplate;
    var aContextModelList = this.getContextMenuModelList(aTemplateList, 'template', oActiveTemplate);
    var sClassName = "templateListContainer ";
    var fOpenDropDown = this.chooseTemplateButtonClicked;
    var bIsDisabled = false;

    var sActiveTemplateLabel = CS.getLabelOrCode(oActiveTemplate);
    if(sActiveTemplateLabel === "All") {
      sActiveTemplateLabel = getTranslation().ALL;
    }
    return (
        <div className="templateDropDownContainer contextMenuContainer">
          <ContextMenuViewNew
              contextMenuViewModel={aContextModelList}
              showCustomIcon={true}
              disabled={bIsDisabled}
              onClickHandler={this.changeTemplate}>
            <TooltipView placement="bottom" label={sActiveTemplateLabel}>
              <div className={sClassName} onClick={fOpenDropDown}>
                <div className="templateIcon"></div>
                <div className="activeTemplateLabel">{sActiveTemplateLabel}</div>
                <div className="changeTemplateDropDownButton"></div>
              </div>
            </TooltipView>
          </ContextMenuViewNew>
        </div>
    );

  };

  getAttributeContextDialogView = (oAttributeContextDialogViewData) => {
    if (CS.isEmpty(oAttributeContextDialogViewData)) {
      return null;
    } else {
      let sAttributeVariantContextId = oAttributeContextDialogViewData.attributeContextId;
      let oUOMView = oAttributeContextDialogViewData.uomView;
      return (
          <AttributeContextDialogView
              attributeVariantContextId={sAttributeVariantContextId}
              uomView={oUOMView}
              isDirty={oAttributeContextDialogViewData.isDirty}
              label={oAttributeContextDialogViewData.label}
              contentStyle={oAttributeContextDialogViewData.contentStyle}
          />
      );
    }
  };

  getIsContextEmpty = () => {
    var __props = this.props;
    var oVariantSectionViewData = __props.variantSectionViewData;
    var oSelectedContext = oVariantSectionViewData.selectedContext;
    var bIsTimeEnabled = oSelectedContext.isTimeEnabled;
    var aVariantTags = oSelectedContext.tags;
    return !(bIsTimeEnabled || !CS.isEmpty(aVariantTags) || !CS.isEmpty(oSelectedContext.entities));
  };

  getVariantContextView = () => {
    var _this = this;
    var __props = _this.props;
    var oEntityViewData = __props.entityViewData;
    var oEntityHeaderTagsEditData = __props.entityHeaderTagsEditData;
    var oVariantSectionViewData = __props.variantSectionViewData;

    var sVariantInstanceId = oEntityViewData.activeEntity.variantInstanceId;

    if(sVariantInstanceId) {
      return (
          <EntityContextSummaryView
              variantSectionViewData={oVariantSectionViewData}
              entityHeaderTagsEditData={oEntityHeaderTagsEditData}
              filterContext={this.props.filterContext}
          />
      );

    }

    return null;
  };

  getAssetDownloadInfoView = () => {
    let oOverViewTabData = this.props.entityViewData.overviewTabData
    return (<ContentDownloadTrackerView
        downloadInfoData={oOverViewTabData.assetDownloadInfo}
    />);

   };

  getSectionKlassTagInfoView = (oSection) => {
    let oViewModel = oSection.viewModel;
    let aChildrenTagModels = [];
    CS.forEach(oViewModel.children, function (oChildTag) {
      aChildrenTagModels.push(new CircledTagGNodeModel(oChildTag.id, oChildTag.label, oChildTag.iconKey, oChildTag.color, oChildTag.type, oChildTag.relevance, [], oChildTag.properties));
    });

    let oCircledTagNodeViewModel = new CircledTagGNodeModel(oViewModel.id, oViewModel.label, oViewModel.iconKey, oViewModel.color, oViewModel.type, null, aChildrenTagModels, oViewModel.properties);
    return (
        <div className={"sectionClassTagInfo"}>
          <CircledTagGroupView circledTagGroupModel={oCircledTagNodeViewModel} disabled={oViewModel.disabled}
                               context={oViewModel.context}/>
        </div>
    )
  };

  getContentTabSpecificData = (oEntityViewData) => {
    let oTabItemConstants = ContentScreenConstants.tabItems;
    let oEntityTabData = oEntityViewData.entityTabData;

    let aCustomViewsTab = [oTabItemConstants.TAB_TIMELINE,
                           oTabItemConstants.TAB_TASKS, oTabItemConstants.TAB_DUPLICATE_ASSETS];

    if(CS.includes(aCustomViewsTab, oEntityTabData.activeEntitySelectedTabId)) {
      return (
          <ContentTabSpecificView entityViewData={oEntityViewData}/>
      )
    }
    else {
      return null;
    }
  };

  render() {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    let sActiveEntityId = oEntityViewData.activeEntity.id; //verify :activeContent/activeSet/activeCollection replaced by activeEntity
    let oCustomView = this.getContentTabSpecificData(oEntityViewData);

    let sLinkScrollContainerClass = "contentEditMaterialLinkScrollContainer";

    let oEntityHeaderData = this.getEntityHeaderData();
    let oAttributeContextDialogView = this.getAttributeContextDialogView(oEntityViewData.attributeContextDialogViewData);
    let oSectionalViewsData = this.getViewsForActiveSections();
    let aLinkItemsData = oSectionalViewsData.linkItemList;
    let aSectionComponents = oSectionalViewsData.sectionalViews;
    let bIsDefaultIconVisible = this.props.entityViewData.activeEntity.baseType !== BaseTypeDictionary.assetBaseType;

    return (
        <div className={"contentEditMaterialUIContainer"}>
          <div className={sLinkScrollContainerClass}>
            <ContentEditView
                linkItemsData={aLinkItemsData}
                linkItemName="linkViewItemList"
                contentInfoData={oEntityHeaderData}
                tabData={oEntityHeaderData.entityTabData}
                customView={oCustomView}
                activeEntityId={sActiveEntityId}
                sectionComponents = {aSectionComponents}
                selectedElementInformation={oEntityViewData.selectedContext}
                selectedImageId={this.props.selectedImageId}
                isDefaultIconVisible={bIsDefaultIconVisible}
                isAcrolinxSidebarVisible={oEntityViewData.isAcrolinxSidebarVisible}
            />
          </div>
          {oAttributeContextDialogView}
        </div>

    );
  }
}



export const view = MasterTagListContext(EntityDetailView);
export const events = oEvents;
