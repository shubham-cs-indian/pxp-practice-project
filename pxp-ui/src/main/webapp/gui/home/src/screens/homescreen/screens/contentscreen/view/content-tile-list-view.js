/**
 * Created by CS80 on 10/10/2016.
 */
import CS from '../../../../../libraries/cs';

import CurrentModuleIdContext from '../../../../../commonmodule/HOC/current-module-id-context';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as FilterView } from '../view/filter-view';
import { view as ContentTileView } from './content-tile-view';
import { view as ContentListView } from './content-list-view';
import { view as HorizontalTreeView } from '../../../../../viewlibraries/horizontaltreeview/horizontal-tree-view';
import { view as ContentHierarchyTaxonomySectionsView } from './content-hierarchy-taxonomy-sections-view';
import { view as NothingFoundView } from './../../../../../viewlibraries/nothingfoundview/nothing-found-view';
import { view as ToolbarViewNew } from './../../../../../viewlibraries/toolbarview/toolbar-view';
import { view as JobListView } from './job-list-view';
import ModuleDictionary from './../../../../../commonmodule/tack/module-dictionary';
import ContentScreenConstants from './../store/model/content-screen-constants';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
import { view as CollectionEditView } from './collection-edit-view';
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary';
import ViewUtils from './utils/view-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;


const oEvents = {
  HANDLE_COLLECTION_EDIT_DIALOG_BUTTON_CLICKED: "handle_collection_edit_dialog_button_clicked",
  CONTENT_TILE_LIST_VIEW_CREATE_VARIANT_BUTTON_CLICKED: "content_tile_list_view_create_variant_button_clicked"
};

const oPropTypes = {
  entityViewData: ReactPropTypes.object,
  sortViewData: ReactPropTypes.object,
  filterViewData: ReactPropTypes.object,
  toolbarData: ReactPropTypes.object,
  currentZoom: ReactPropTypes.number,
  viewMode: ReactPropTypes.string,
  innerHierarchyViewMode: ReactPropTypes.bool,
  navigationData: ReactPropTypes.object,
  thumbnailMode: ReactPropTypes.string,
  isForAvailableEntity: ReactPropTypes.bool,
  dragDropContext: ReactPropTypes.string,
  collectionData: ReactPropTypes.object,
  activeXRayPropertyGroup: ReactPropTypes.object,
  xRayData: ReactPropTypes.object,
  viewMasterData: ReactPropTypes.object,
  ruleViolation: ReactPropTypes.object,
  relationshipViewMode: ReactPropTypes.object,
  shouldHideTabs: ReactPropTypes.bool,
  isHierarchyMode: ReactPropTypes.bool,
  hideAddEntityButton: ReactPropTypes.bool,
};

// @CurrentModuleIdContext
// @CS.SafeComponent
class ContentTileListView extends React.Component {
  constructor(props) {
    super(props);

    this.contentFilterView = React.createRef();
    this.contentTileListViewWrapper = React.createRef();
  }

  static propTypes = oPropTypes;

  componentDidMount() {
    this.adjustHeight();
  }

  componentDidUpdate() {
    this.adjustHeight();
  }

  adjustHeight = () => {
    var oFilterView = this.contentFilterView.current;
    var oTileView = this.contentTileListViewWrapper.current;
    var oFilterViewData = this.props.filterViewData;
    if (oFilterView && oTileView && !CS.isEmpty(oFilterViewData) && !oFilterViewData.isFilterAndSearchViewDisabled) {
      var iFilterViewHeight = oFilterView.offsetHeight > 0 ? oFilterView.offsetHeight + 8 : 0;
      oTileView.style.height = "calc(100% - " + iFilterViewHeight + "px)";
    }
    else if(CS.isEmpty(oFilterView) && oTileView){
      oTileView.style.height = "100%";
    }
  };

  getSearchFilterData = (oFilterViewData) => {
    return {
      searchText: oFilterViewData.searchText
    }
  };

  handleCollectionEditDialogButtonClicked = (sContext) => {
    let oFilterViewData = this.props.filterViewData;
    let oFilterContext = oFilterViewData.filterContext;
    EventBus.dispatch(oEvents.HANDLE_COLLECTION_EDIT_DIALOG_BUTTON_CLICKED, sContext, oFilterContext);
  };

  getContentTileView = (__props) => {
    var oEntityViewData = __props.entityViewData;
    var oCollectionData = __props.collectionData;
    var oActiveCollection = oCollectionData.activeCollection;
    let oFilterViewData = __props.filterViewData;
    let oFilterContext = oFilterViewData.filterContext;

    return (<ContentTileView
        contentTileViewData={oEntityViewData}
        toolbarData={__props.toolbarData}
        currentZoom={__props.currentZoom}
        navigationData={__props.navigationData}
        dragDropContext={__props.dragDropContext}
        thumbnailMode={__props.thumbnailMode}
        activeXRayPropertyGroup={__props.activeXRayPropertyGroup}
        xRayData={__props.xRayData}
        filterContext={oFilterContext}
        activeCollection={oActiveCollection}
        canClone={__props.canClone}
    />)
  };

  getContentListView = (__props) => {
    var oEntityViewData = __props.entityViewData;
    var oCollectionData = __props.collectionData;
    var oActiveCollection = oCollectionData.activeCollection;
    let oFilterViewData = __props.filterViewData;
    let oFilterContext = oFilterViewData.filterContext;

    return (<ContentListView
        contentListViewData={oEntityViewData}
        toolbarData={__props.toolbarData}
        currentZoom={__props.currentZoom}
        navigationData={__props.navigationData}
        dragDropContext={__props.dragDropContext}
        thumbnailMode={__props.thumbnailMode}
        activeCollection={oActiveCollection}
        filterContext={oFilterContext}
        canClone={__props.canClone}
    />);
  };

  getContentViewForAvailableEntity = () => {
    let oEntityViewData = this.props.entityViewData;
    let viewMode = oEntityViewData.viewMode || "";
    if(viewMode == ContentScreenConstants.viewModes.TILE_MODE){
      return this.getContentTileView(this.props);
    } else if(viewMode == ContentScreenConstants.viewModes.LIST_MODE){
      return this.getContentListView(this.props);
    }
  };

  getContentHierarchyView = () => {
    let __props = this.props;
    let oEntityViewData = __props.entityViewData;
    let oFilterViewData = __props.filterViewData;
    let oFilterContext = oFilterViewData.filterContext;
    let oContentHierarchyData = oEntityViewData.contentHierarchyData;
    if(CS.isEmpty(oContentHierarchyData)){
      return;
    }
    let sSelectedContext = oContentHierarchyData.selectedContext;

    let sWrapperClassSuffix = " " + sSelectedContext;
    let oLeftSideView = null;
    let oRightSideView = null;
    let sHierarchyLabel = "";
    switch(sSelectedContext){

      case HierarchyTypesDictionary.TAXONOMY_HIERARCHY:
        sHierarchyLabel = ViewUtils.getDecodedTranslation(getTranslation().ENTITY_HIERARCHY,{ entity : getTranslation().TAXONOMY });
        oLeftSideView = <HorizontalTreeView contentHierarchyData={oContentHierarchyData}
                                            filterContext={oFilterViewData.filterContext}/>;
        oRightSideView = <ContentHierarchyTaxonomySectionsView
            entityViewData={oEntityViewData}
            filterContext={oFilterViewData.filterContext}
            taxonomySectionData={oContentHierarchyData.taxonomySectionData} />
        break;

      case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
        sHierarchyLabel = ViewUtils.getDecodedTranslation(getTranslation().ENTITY_HIERARCHY,{ entity : getTranslation().COLLECTION });
        oLeftSideView = <HorizontalTreeView contentHierarchyData={oContentHierarchyData}
                                            filterContext={oFilterViewData.filterContext}/>;

        let {collectionHierarchyRightsideViewSpecificData: oCollectionHierarchyRightsideViewSpecificData} =   oContentHierarchyData.collectionSectionsData;
        oRightSideView = this.getCollectionHierarchyRightSideView(oCollectionHierarchyRightsideViewSpecificData, oEntityViewData);
        break;
    }


    return (
        <div className={"contentHierarchyPreviewWrapper" + sWrapperClassSuffix}>
          {oLeftSideView}
          {oRightSideView}
        </div>
    );
  };

  getCollectionHierarchyRightSideView = (oCollectionHierarchyRightsideViewSpecificData, oEntityViewData) => {
    var oContentView = null;
    let oFilterViewData = oCollectionHierarchyRightsideViewSpecificData.filterViewData;
    let oFilterContext = oFilterViewData.filterContext;
    let { entityViewData: { viewMode: sViewMode } } = oCollectionHierarchyRightsideViewSpecificData;
      let bIsAdvancedFilterApplied = !CS.isEmpty(oFilterViewData.appliedFilterData);
      let bIsAnyFilterApplied = (bIsAdvancedFilterApplied || !CS.isEmpty(oFilterViewData.searchText));
      if(bIsAnyFilterApplied && CS.isEmpty(oEntityViewData.entityList)) {
          oContentView = <NothingFoundView message={getTranslation().NO_MATCHES_FOUND}/>
      } else  if(!oEntityViewData.entityList.length) {
        oContentView = <NothingFoundView />;
      } else {
          switch (sViewMode) {
              case ContentScreenConstants.viewModes.TILE_MODE:
                  oContentView = this.getContentTileView(oCollectionHierarchyRightsideViewSpecificData);
                  break;

              case ContentScreenConstants.viewModes.LIST_MODE:
                  oContentView = this.getContentListView(oCollectionHierarchyRightsideViewSpecificData);
                  break;
          }
      }

    return <div className="contentTileListViewWrapper" key={sViewMode}>
      <ToolbarViewNew
        toolbarData={oCollectionHierarchyRightsideViewSpecificData.toolbarData}
        selectedItem={sViewMode}
        filterContext={oFilterContext}
      />
      {oContentView}
    </div>
  };

  /** For other than Filter Hierarchy*/
  getHierarchyInnerThumbView = () => {
    var __props = this.props;
    var oEntityViewData = __props.entityViewData;

    var oContentView = null;
    if (CS.isEmpty(oEntityViewData.entityList) && !CS.isEmpty(__props.toolbarData.cutCopyPasteList)) {
      oContentView =
          <div className="nothingFoundWithToolBarView">
            <NothingFoundView/>
          </div>;
    } else if (CS.isEmpty(oEntityViewData.entityList)) {
      oContentView = (<NothingFoundView/>);
    } else {
      oContentView = this.getContentViewForAvailableEntity()//_this.getContentTileView();
    }
    return oContentView;
  };

  getFilterViewDom = (oFilterViewData) => {
    if(this.props.isHierarchyMode && //if Hierarchy mode is selected and
        (
            (// in case of inner hierarchy mode or
                this.props.innerHierarchyViewMode)
            // oFilterViewData.selectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY //collection hierarchy mode do not show filter bar
        )
    ){
      return null
    }
    else if(!oFilterViewData.isFilterAndSearchViewDisabled) {
      var oViewMasterData = this.props.viewMasterData;
      var oFilterViewMasterData = (oViewMasterData && oViewMasterData.filterViewMasterData) || {};
      let oRootTaxonomy = CS.isNotEmpty(oFilterViewData) ? oFilterViewData.appliedTaxonomies[0] : {};
      return (
          <div className="contentFilterViewContainer" ref={this.contentFilterView}>
            <FilterView filterData={oFilterViewData}
                        key = {oRootTaxonomy && oRootTaxonomy.id || ""}
                        onViewUpdate={this.adjustHeight}
                        viewMasterData={oFilterViewMasterData}/>
          </div>
      );
    }
    return null;
  };

  getJobListView = () => {
    let oDataForDataIntegrationLogsView = this.props.dataIntegrationLogsViewData;

    let oView;
    if (CS.isNotEmpty(oDataForDataIntegrationLogsView.jobList)) {
      let oJobDataDetails = {
        jobList: oDataForDataIntegrationLogsView.jobList,
        activeJob: oDataForDataIntegrationLogsView.activeJob,
        activeJobGraphData: oDataForDataIntegrationLogsView.activeJobGraphData,
        isProcessInstanceDialogOpen: oDataForDataIntegrationLogsView.isProcessInstanceDialogOpen,
        instanceIID: oDataForDataIntegrationLogsView.instanceIID
      };
      oView = (
          <div className="jobContainer">
            <JobListView {...oJobDataDetails}/>
          </div>
      );
    }
    return oView;
  };

  getCreateButtonData = () => {
    let _this = this;
    let _props = _this.props;

    var oCollectionData = _props.collectionData;
    var sCurrentModuleId = this.props.currentModuleId;
    var oActiveCollection = oCollectionData.activeCollection;
    let oVerticalMenu = _props.entityViewData.verticalMenu;
    let sContext = "contentTile";
    if (!(sCurrentModuleId === ModuleDictionary.ALL ||
        sCurrentModuleId === ModuleDictionary.FILES ||
        _props.entityViewData.contentHierarchyData ||
        this.props.hideAddEntityButton)) {

      if (!CS.isEmpty(oActiveCollection)) {
        if (oActiveCollection.type === "staticCollection") {
          let bIsUserReadOnly = ViewUtils.getIsCurrentUserReadOnly();
          return {
            showAddButtonForCollection: !bIsUserReadOnly,
            isHideCreateButton: oVerticalMenu.isHideCreateButton
          };
        } else {
          return null; //don't show any item in case of dynamic collection
        }
      } else if (CS.isNotEmpty(_props.entityViewData.contentHierarchyData)) {
        return null; //don't show any item in case of hierarchy mode
      } else {
        // TODO : Tomporary change. Remove below condition when we provide support to add Document Template in Publication
        let aList = !CS.isEmpty(oVerticalMenu) ? oVerticalMenu.list : [];
        let sPlaceholder = _props.entityViewData.placeholder;
        let aContentList = _props.entityViewData.entityList;
        if (CS.isEmpty(aContentList)) {
          aList = CS.reject(aList, {id: "selectAll"});
        }
        CS.forEach(aList, (oItem) => {
          oItem.properties = {};
        });
        return {
          context: sContext,
          list: aList,
          placeholder: sPlaceholder,
          dragDropContext: _props.dragDropContext,
          isHideCreateButton: CS.isNotEmpty(oVerticalMenu) ? oVerticalMenu.isHideCreateButton : false,
          searchText: CS.isNotEmpty(oVerticalMenu) ? oVerticalMenu.searchText : ""
        }
      }
    }
    return null;
  };

  getCreateLinkedVariantButton = () => {
    let __props = this.props;
    let oSide2NatureClass = __props.entityViewData.side2NatureClassOfProductVariant;
    let bIsCreateAllowedForLinkedVariant = CS.isNotEmpty(oSide2NatureClass.id);

    if(!bIsCreateAllowedForLinkedVariant) {
      return null;
    }

    return {
      className: "createCloneButton",
      label: getTranslation().CREATE,
      isRaisedButton: true,
      isDisabled: false,
      onButtonClick: this.handleCreateCloneButtonClicked.bind(this, ContextTypeDictionary.PRODUCT_VARIANT)
    }
  };

  getCustomCreateButtonData = () => {
    let __props = this.props;
    let sContext = __props.dragDropContext;

    switch (sContext) {
      case "productVariantQuickList":
        return this.getCreateLinkedVariantButton();
    }
  };

  getEditCollectionView = () => {
    return (
        <CollectionEditView
          collectionData={this.props.collectionData}
          buttonClickHandler={this.handleCollectionEditDialogButtonClicked }
        />);
  };

  handleCreateCloneButtonClicked = (sContext) => {
    EventBus.dispatch(oEvents.CONTENT_TILE_LIST_VIEW_CREATE_VARIANT_BUTTON_CLICKED , sContext);
  };

  render() {
    var _this = this;
    var __props = _this.props;
    var bAvailableEntityViewStatus = __props.isForAvailableEntity;
    var oEntityViewData = __props.entityViewData;
    var oContentView = null;
    var oFilterViewData = __props.filterViewData;
    let oFilterContext = oFilterViewData.filterContext;
    var sHierarchyViewMode = null;
    var sContentTileListViewWrapperClass = "contentTileListViewWrapper ";
    let oEditCollectionDialogView = this.getEditCollectionView();

    oEntityViewData.searchFilterData = this.getSearchFilterData(oFilterViewData);

    var bIsAdvancedFilterApplied = !CS.isEmpty(oFilterViewData.appliedFilterData);
    var bIsAnyFilterApplied = (bIsAdvancedFilterApplied || !CS.isEmpty(oFilterViewData.searchText));

    if (__props.entityViewData.jobViewData) {
      oContentView = _this.getJobListView();
    } else if(bIsAnyFilterApplied && !oEntityViewData.showGoldenRecordBuckets
        && CS.isEmpty(oEntityViewData.entityList)
        && CS.isEmpty(__props.entityViewData.contentHierarchyData)
        ) {
      oContentView = (<NothingFoundView message={getTranslation().NO_MATCHES_FOUND}/>);
    } else if (!oEntityViewData.showGoldenRecordBuckets && !oEntityViewData.entityList.length
        && CS.isEmpty(__props.entityViewData.contentHierarchyData)
        ) {
      oContentView = (<NothingFoundView />);
    } else if (bIsAnyFilterApplied && oEntityViewData.showGoldenRecordBuckets && CS.isEmpty(oEntityViewData.goldenRecordBucketsData.buckets)){
      oContentView = (<NothingFoundView message={getTranslation().NO_MATCHES_FOUND}/>);
    } else if (oEntityViewData.isEditMode) {
      oContentView = _this.getContentViewForAvailableEntity();
    }
    else if(CS.isNotEmpty(oEntityViewData.contentHierarchyData)){
      if(__props.innerHierarchyViewMode){
        sHierarchyViewMode = oEntityViewData.viewMode || "";
        oContentView  = _this.getHierarchyInnerThumbView()
      }else {
        oContentView = _this.getContentHierarchyView();
      }
    }
    else {
      switch (__props.viewMode) {
        case ContentScreenConstants.viewModes.TILE_MODE:
          oContentView = !bAvailableEntityViewStatus ? _this.getContentTileView(this.props) : _this.getContentViewForAvailableEntity();
          break;
        case ContentScreenConstants.viewModes.LIST_MODE:
          oContentView = !bAvailableEntityViewStatus ? _this.getContentListView(this.props) : _this.getContentViewForAvailableEntity();
          break;
      }
    }

    var oProperties = {};
    oProperties.context = "contentSection";

    var oVerticalToolbarView = null;

    var oViewMasterData = this.props.viewMasterData;
    var oToolbarViewMasterData = (oViewMasterData && oViewMasterData.toolbarViewMasterData) || {};

    var oFilterViewDOM = this.getFilterViewDom(oFilterViewData);

    let oToolbarViewNew = null;
    if (!__props.entityViewData.jobViewData &&
        (!CS.isEmpty(__props.toolbarData.refresh) ||
            !CS.isEmpty(__props.toolbarData.selectAll) ||
            !CS.isEmpty(__props.toolbarData.leftItemList) ||
            !CS.isEmpty(__props.toolbarData.viewTypesItems) ||
            !CS.isEmpty(__props.toolbarData.XRayView) ||
            !CS.isEmpty(__props.toolbarData.zoomToolbarItemsList) ||
            CS.isNotEmpty(__props.toolbarData.cutCopyPasteList)
        )) {
      let oCreateButtonData = _this.getCreateButtonData();
      let oCustomCreateButtonData = _this.getCustomCreateButtonData();
      oToolbarViewNew = <ToolbarViewNew toolbarData={__props.toolbarData} viewMasterData={oToolbarViewMasterData}
                                        selectedItem={sHierarchyViewMode}
                                        createButtonData={oCreateButtonData}
                                        customCreateButtonData={oCustomCreateButtonData}
                                        filterContext={oFilterContext} />;
    }
    let sContentTileListViewContainer = "contentTileListViewContainer";
    //TODO: Remove dependency of isHierarchyMode
    sContentTileListViewContainer +=  this.props.isHierarchyMode ? " withCollectionScreen" : " withoutCollectionScreen";
    //todo: HN - remove TabLayoutView and clear related data
    return (
        <div className="contentTileListViewContainerWrapper">
          {__props.filterView}
          <div className={sContentTileListViewContainer}>
              <div className="contentTileListView">
                {oFilterViewDOM}
                <div className={sContentTileListViewWrapperClass} ref={this.contentTileListViewWrapper}>
                  {oToolbarViewNew}
                  {oVerticalToolbarView}
                  {oContentView}
                </div>
              </div>
            {oEditCollectionDialogView}
          </div>
        </div>
    )

  }
}



export const view = CurrentModuleIdContext(ContentTileListView);
export const events = oEvents;
