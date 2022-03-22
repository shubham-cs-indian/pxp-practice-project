import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as TaxonomyLevelView } from './taxonomy-level-view';
import { view as VisualColumnView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import { view as ContextMenuViewNew } from './../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as AttributionTaxonomyDetailView } from './attribution-taxanomy-detail-view';
import { view as NothingFoundView } from './../../../../../viewlibraries/nothingfoundview/nothing-found-view';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import ViewUtils from '../view/utils/view-utils';
import { view as ConfigHeaderView } from '../../../../../viewlibraries/configheaderview/config-header-view';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import {view as ManageEntityDialogView} from "./entity-usage-dialog-view";
import ManageEntityConfigProps from "../store/model/manage-entity-config-props";
import {view as TabLayoutView} from "../../../../../viewlibraries/tablayoutview/tab-layout-view";

const oEvents = {
  ATTRIBUTION_TAXONOMY_LIST_CONFIG_LEVEL_ADDED: "attribution_taxonomy_list_config_level_added",
  ATTRIBUTION_TAXONOMY_CONFIG_CREATE_DIALOG_BUTTON_CLICKED: "attribution_taxonomy_config_create_dialog_button_clicked",
  ATTRIBUTION_TAXONOMY_DIALOG_CLOSE:"attribution_taxonomy_dialog_close",
  ATTRIBUTION_TAXONOMY_ADD_NEW_LEVEL: "attribution_taxonomy_add_new_level",
  ATTRIBUTION_TAXONOMY_HIERARCHY_HORIZONTAL_TOGGLE_AUTOMATIC_SCROLL: "HANDLE_TAXONOMY_HIERARCHY_HORIZONTAL_TOGGLE_AUTOMATIC_SCROLL",
  ATTRIBUTION_TAXONOMY_IMPORT_BUTTON_CLICKED: "attribution_taxonomy_import_button_clicked",
  HANDLE_ATTRIBUTE_TAXONOMY_TAB_CHANGED: "handle_attribution_taxonomy_tab_clicked"
};

const oPropTypes = {
  selectedTaxonomyLevels: ReactPropTypes.array,
  taxonomyList: ReactPropTypes.array,
  linkedMasterList: ReactPropTypes.object,
  masterList: ReactPropTypes.object,
  activeTaxonomy: ReactPropTypes.object,
  showPopover: ReactPropTypes.bool,
  activeDetailedSectionsData: ReactPropTypes.object,
  activeDetailedTaxonomy: ReactPropTypes.object,
  isActiveDetailedTaxonomyDirty: ReactPropTypes.bool,
  activeDialogSectionsData: ReactPropTypes.object,
  activeDialogTaxonomy: ReactPropTypes.object,
  addChildPopoverVisibleLevelId: ReactPropTypes.string,
  allowedTagValues: ReactPropTypes.array,
  isScrollAutomatically: ReactPropTypes.bool,
  activeTaxonomyLevel: ReactPropTypes.object,
  activeTaxonomyLevelChildren: ReactPropTypes.object,
  actionItemModel: ReactPropTypes.object,
  iconLibraryData: ReactPropTypes.object
};

// @CS.SafeComponent
class AttributionTaxonomyConfigView extends React.Component {

  constructor (oProps) {
    super(oProps);

    this.taxonomyMasterListContainer = React.createRef();
  }

  state = {
    taxonomyType: "majorTaxonomy",
    label: ""
  };

  static getDerivedStateFromProps (oNextProps, oState) {
    return ({
      taxonomyType: oNextProps.activeTaxonomy.taxonomyType,
      label: oNextProps.activeTaxonomy.label
    })
  }

  componentDidUpdate() {
    this.updateHorizontalScroll()
  }

  updateHorizontalScroll = () => {
    if(this.props.isScrollAutomatically){
      var oContainerDivDOM = this.taxonomyMasterListContainer.current;
      oContainerDivDOM.scrollLeft = oContainerDivDOM.scrollWidth;
      EventBus.dispatch(oEvents.ATTRIBUTION_TAXONOMY_HIERARCHY_HORIZONTAL_TOGGLE_AUTOMATIC_SCROLL);
    }
  };

  handleAddLevelButtonClicked = (bPopoverStatus) => {
    EventBus.dispatch(oEvents.ATTRIBUTION_TAXONOMY_ADD_NEW_LEVEL, bPopoverStatus);
  };

  handleListLevelAdded = (oModel) => {
    var isNewlyCreated = false;
    var sNewLabel = "";
    var sSelectedId = oModel.id;
    EventBus.dispatch(oEvents.ATTRIBUTION_TAXONOMY_LIST_CONFIG_LEVEL_ADDED, sSelectedId, isNewlyCreated, sNewLabel);
  };

  handleListLevelCreated = (sSearchedValue) => {
    var isNewlyCreated = true;
    var sNewLabel = sSearchedValue;
    var sSelectedId = "";
    EventBus.dispatch(oEvents.ATTRIBUTION_TAXONOMY_LIST_CONFIG_LEVEL_ADDED, sSelectedId, isNewlyCreated, sNewLabel);
  };

  handleFilesSelectedToUpload = (aFiles) => {
    EventBus.dispatch(oEvents.ATTRIBUTION_TAXONOMY_IMPORT_BUTTON_CLICKED, aFiles);
  };

  getContextMenuModelsForMasterList = () => {
    var aMasterListModels = [];
    var oItemsMap = this.props.masterList;
    var aSelectedMasterList = this.getActiveLevels();
    CS.forEach(oItemsMap, function (oMasterItem) {
      if (!CS.includes(aSelectedMasterList, oMasterItem.id)) {
        aMasterListModels.push(new ContextMenuViewModel(
            oMasterItem.id,
            CS.getLabelOrCode(oMasterItem),
            false,
            "",
            {context: "attributionTaxonomy"}
        ));
      }
    });
    return aMasterListModels;
  };

  getActiveTaxonomy = () => {
    var oActiveTaxonomy = {};
    var aSelectedTaxonomyLevels = this.props.selectedTaxonomyLevels;
    if (aSelectedTaxonomyLevels[1]) {
      var aTaxonomyList = this.props.taxonomyList;
      if (aTaxonomyList[0]) {
        oActiveTaxonomy = CS.find(aTaxonomyList[0].children, {id: aSelectedTaxonomyLevels[1]}) || {};
      }
    };
    return oActiveTaxonomy;
  };

  getActiveLevels = () => {
    var oActiveTaxonomy = this.getActiveTaxonomy();
    return oActiveTaxonomy.tagLevels || {};
  };

  getAttributionTaxonomyListView = () => {
    var __this = this;
    var oMasterListMap = this.props.linkedMasterList;
    var aSelectedTaxonomyLevels = this.props.selectedTaxonomyLevels;
    var aTaxonomyList = this.props.taxonomyList;
    let oActiveDetailedSectionsData = this.props.activeDetailedSectionsData;
    let oAllowedTaxonomyHierarchyList = oActiveDetailedSectionsData.allowedTaxonomyHierarchyList;

    var aChildActionItems = [
      {id: "delete", label: "Delete", className: "delete"}
      /*{id: "rename", label: "Rename", className: "edit"}*/
    ];
    var aLevelActionItems = [{id: "delete", label: "Delete", className: "delete"}];
    // First level is added manually since it has custom values
    var aLevelViews = [<TaxonomyLevelView
        key="0"
        selectedMasterList={{}}
        selectedTaxonomy={aTaxonomyList[0]}
        selectedChild={aSelectedTaxonomyLevels[1]}
        levelIndex={0}
        levelActionItems={[{id: "create", label: "Create", className: "create"}]}
        childActionItems={aChildActionItems}
        context="attributionTaxonomy"
        activeDetailedTaxonomy={this.props.activeDetailedTaxonomy}
        allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}
    />];
    aTaxonomyList = aTaxonomyList[0].children;
    var aActiveLevels = this.getActiveLevels();
    CS.forEach(aActiveLevels, function (oTag, iIndex) {
      var sTagId = oTag.tag.id;
      iIndex = +iIndex + 1;
      var sTaxonomyId = aSelectedTaxonomyLevels[iIndex] || "";
      var oTaxonomy = CS.find(aTaxonomyList, {id: sTaxonomyId}) || {};
      if (__this.props.addChildPopoverVisibleLevelId == sTagId) {
        var bShowPopover = true;
      } else {
        var bShowPopover = false;
      }
      aLevelViews.push(<TaxonomyLevelView
          key={iIndex}
          selectedMasterList={oMasterListMap[sTagId]}
          selectedTaxonomy={oTaxonomy}
          selectedChild={aSelectedTaxonomyLevels[iIndex + 1]}
          levelActionItems={aLevelActionItems}
          childActionItems={aChildActionItems}
          levelIndex={iIndex}
          context="attributionTaxonomy"
          showPopover={bShowPopover}
          allowedTagValues={__this.props.allowedTagValues}
          activeDetailedTaxonomy={__this.props.activeDetailedTaxonomy}
          allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}
      />);
      aTaxonomyList = oTaxonomy.children;
    });
    return aLevelViews;
  };

  handleDialogButtonClicked = (sContext) => {
    var sIsMajorMinor = this.state.taxonomyType;
    var sLabel = this.state.label;
    EventBus.dispatch(oEvents.ATTRIBUTION_TAXONOMY_CONFIG_CREATE_DIALOG_BUTTON_CLICKED, sContext, sIsMajorMinor, sLabel);
  };

  getIsMajorMinorDialog = (oActiveTaxonomy) => {
    if (!oActiveTaxonomy || !(oActiveTaxonomy.isNewlyCreated && oActiveTaxonomy.parentTaxonomyId == -1)) {
      return null;
    }

    var sErrorText = "";
    var bIsDisableCreate = true;
    let fButtonHandler = this.handleDialogButtonClicked;

    if (CS.isEmpty(oActiveTaxonomy.code.trim())) {
      sErrorText = getTranslation().CODE_SHOULD_NOT_BE_EMPTY;
    } else if (!ViewUtils.isValidEntityCode(oActiveTaxonomy.code)) {
      sErrorText = getTranslation().PLEASE_ENTER_VALID_CODE;
    } else {
      bIsDisableCreate = false;
    }

    let oModel = {
      taxonomyType: {
        isSelected: oActiveTaxonomy.taxonomyType == "majorTaxonomy" ? true : false,
        context: "attributionTaxonomy",
      },
      label: oActiveTaxonomy.label,
      code: oActiveTaxonomy.code || "",
    };

    var oBodyStyle = {
      padding: '0 10px 20px 10px'
    };
    var aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isDisabled: false,
        isFlat: true,
      },
      {
        id: "create",
        label: getTranslation().CREATE,
        isDisabled: bIsDisableCreate,
        isFlat: false,
      }

    ];

    let oCreateDialogLayoutData = [
      {
        elements: [
          {
            id: "1",
            label: getTranslation().NAME,
            key: "label",
            type: "singleText",
            width: 50
          },
          {
            id: "2",
            label: getTranslation().CREATE_TAXONOMY_AS_MAJOR_MINOR,
            key: "taxonomyType",
            type: "yesNo",
            width: 50
          },
          {
            id: "3",
            label: getTranslation().CODE,
            key: "code",
            type: "singleText",
            width: 50
          }
        ]
      }];

    return (
        <CustomDialogView modal={true} open={true}
                          title={getTranslation().CREATE}
                          bodyStyle={oBodyStyle}
                          bodyClassName=""
                          contentClassName="createClassModalDialog"
                          buttonData={aButtonData}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          buttonClickHandler={fButtonHandler}>
          <VisualColumnView context={"attributionTaxonomy"} sectionLayout={oCreateDialogLayoutData} data={oModel} errorTextForCodeEntity={sErrorText}/>
        </CustomDialogView>);
  };

  getChildCreationDialog = (oActiveTaxonomyLevel, sEntityType) => {
    return (
        <ConfigEntityCreationDialogView
            activeEntity={oActiveTaxonomyLevel}
            entityType={sEntityType}
        />
    )
  };

  handleDetailedTaxonomyDialogClose = (sId) => {
    EventBus.dispatch(oEvents.ATTRIBUTION_TAXONOMY_DIALOG_CLOSE,sId);
  };

  getSaveBarButtons = () => {
    return (
        <div className={"saveBarButtonsWrapper"}>
          <CustomMaterialButtonView label={getTranslation().DISCARD}
                                    isRaisedButton={true}
                                    isDisabled={false}
                                    // style={oCancelButtonStyle}
                                    // labelStyle={oCancelButtonStyle}
                                    keyboardFocused={true}
                                    onButtonClick={this.handleDetailedTaxonomyDialogClose.bind(this, "discard")}
          />
          <CustomMaterialButtonView label={getTranslation().SAVE}
                                    isRaisedButton={true}
                                    isDisabled={false}
                                    // style={oSaveButtonStyle}
                                    // labelStyle={oButtonLabelStyle}
                                    keyboardFocused={true}
                                    onButtonClick={this.handleDetailedTaxonomyDialogClose.bind(this, "save")}/>
        </div>
    );
  };

  getTaxonomyDetailView = () => {
    let oProps = this.props;
    let oActiveTaxonomy = oProps.activeDetailedTaxonomy;
    let oDetailViewData = oProps.activeDetailedSectionsData;

    if (!CS.isEmpty(oActiveTaxonomy)) {
      return (
          <AttributionTaxonomyDetailView
              activeTaxonomy={oActiveTaxonomy}
              taxonomyDetailedViewData={oDetailViewData}
              isActiveTaxonomyDirty={oProps.isActiveDetailedTaxonomyDirty}
              iconLibraryData={this.props.iconLibraryData}
          />
      );
    } else {
      return (<NothingFoundView />);
    }
  };

  handleTabClicked = (sTabId) => {
    if (sTabId === this.props.selectedTabId) {
      return;
    }
    EventBus.dispatch(oEvents.HANDLE_ATTRIBUTE_TAXONOMY_TAB_CHANGED, sTabId);
  };

  getTabLayoutView = () => {
    let oActiveDetailedSectionsData = this.props.activeDetailedSectionsData;
    let oTabData = oActiveDetailedSectionsData.attributionTaxonomiesListTabData;

    return (
        <TabLayoutView
            tabList={oTabData.tabList}
            activeTabId={oTabData.selectedTabId}
            addBorderToBody={false}
            onChange={this.handleTabClicked}>
        </TabLayoutView>);
  };

  getTaxonomyHeaderView = () => {
    let oHeaderDom = <ConfigHeaderView actionButtonList = {this.props.actionItemModel.AttributionTaxonomyListView}
                                                       showSaveDiscardButtons={false}
                                                       context={this.props.context}
                                                       hideSearchBar={true}
                                                       customHeaderView ={this.getTabLayoutView()}
                                                       filesUploadHandler = {this.handleFilesSelectedToUpload} />;
    return oHeaderDom
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

  render() {
    var oAttributionTaxonomyDetailView = this.getAttributionTaxonomyListView();
    var aContextMenuModels = this.getContextMenuModelsForMasterList();
    var aSelectedTaxonomyLevels = this.props.selectedTaxonomyLevels;
    var bShowAddLevels = !CS.isEmpty(aSelectedTaxonomyLevels) && aSelectedTaxonomyLevels.length > 1;
    var oAddNewLevelButton = bShowAddLevels ?
                             <TooltipView label={getTranslation().ADD_NEW_LEVEL}>
                               <div className="addNewLevelView">
                                 <div className="addNewLevelLabel">{getTranslation().ADD_NEW_LEVEL}</div>
                                 <div className="addNewLevelButton"
                                      onClick={this.handleAddLevelButtonClicked.bind(this, true)}>
                                 </div>
                               </div>
                             </TooltipView> : null;

    var oActiveTaxonomy = this.props.activeTaxonomy;
    var oActiveTaxonomyLevel = this.props.activeTaxonomyLevel;
    var oActiveTaxonomyLevelChildren = this.props.activeTaxonomyLevelChildren;
    var oIsMajorMinorDialog = this.getIsMajorMinorDialog(oActiveTaxonomy);
    var sEntityType = !CS.isEmpty(oActiveTaxonomyLevel) ? "attributionTaxonomyLevel" : "attributionTaxonomyLevelChildren";
    var sActiveEntity = !CS.isEmpty(oActiveTaxonomyLevel) ? oActiveTaxonomyLevel : oActiveTaxonomyLevelChildren;
    var oChildCreationDialog = !CS.isEmpty(oActiveTaxonomyLevel) || !CS.isEmpty(oActiveTaxonomyLevelChildren) ? this.getChildCreationDialog(sActiveEntity,sEntityType) : null;
    let sSaveBarClass = "saveBar";

    if (this.props.isActiveDetailedTaxonomyDirty) {
      sSaveBarClass += " dirty";
    }
    let oTaxonomyHeaderView = this.getTaxonomyHeaderView();

    let oTaxonomyDetailView = (<div className="taxonomyDetailViewContainer">
        {this.getTaxonomyDetailView()}
        <div className={sSaveBarClass}>{this.getSaveBarButtons()}</div>
      </div>)

    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;

    return (
        <div className="taxonomyMasterListConfigView">
          <div className="taxonomyMasterListHeaderContainer">
            {oTaxonomyHeaderView}
          </div>
          <div className="taxonomyMasterListContainer" ref={this.taxonomyMasterListContainer}>
              {oAttributionTaxonomyDetailView}
              <ContextMenuViewNew
                  contextMenuViewModel={aContextMenuModels}
                  onClickHandler={this.handleListLevelAdded}
                  onCreateHandler={this.handleListLevelCreated}
                  showCreateButton={true}
                  showPopover={this.props.showPopover}
                  showHidePopoverHandler={this.handleAddLevelButtonClicked}>
                {oAddNewLevelButton}
              </ContextMenuViewNew>
            </div>
          {oTaxonomyDetailView}
          {oIsMajorMinorDialog}
          {oChildCreationDialog}
          {oManageEntityDialog}
        </div>
    );
  }
}

export const view = AttributionTaxonomyConfigView;
export const events = oEvents;
