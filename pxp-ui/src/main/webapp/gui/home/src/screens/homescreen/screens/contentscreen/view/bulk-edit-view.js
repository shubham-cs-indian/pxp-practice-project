import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import {view as BulkEditAppliedPropertiesView} from './bulk-edit-applied-properties-view';
import FullScreenViewObj from '../../../../../viewlibraries/fullscreenview/fullscreen-view';
import {view as ActionableChipsView} from './../../../../../viewlibraries/actionablechipsview/actionable-chips-view';
import {view as GroupMssWrapperView} from "../../../../../viewlibraries/filter/group-mss-wrapper-view";
import {bulkEditTabTypesConstants as BulkEditTabTypesConstants} from "../tack/bulk-edit-layout-data";
import {view as TreeView} from "../../../../../viewlibraries/treeviewnew/tree-view";
import ViewUtils from '../view/utils/view-utils';
import { view as ContentAttributeElementView } from '../../../../../viewlibraries/attributeelementview/content-attribute-element-view';
import ContentAttributeElementViewModel from '../../../../../viewlibraries/attributeelementview/model/content-attribute-element-view-model';
import { view as TagGroupView } from '../../../../../viewlibraries/taggroupview/tag-group-view.js';
import MockDataForEntityBaseTypes from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import { view as TabLayoutView } from "../../../../../viewlibraries/tablayoutview/tab-layout-view"
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
let getTranslation = ScreenModeUtils.getTranslationDictionary;
const FullScreenView = FullScreenViewObj.view;

const oEvents = {
  BULK_EDIT_APPLY_BUTTON_CLICKED: "bulk_edit_apply_button_clicked",
  BULK_EDIT_CANCEL_BUTTON_CLICKED: "bulk_edit_cancel_button_clicked",
  BULK_EDIT_GET_TREE_NODE_CHILDREN: "bulk_edit_get_tree_node_children",
  BULK_EDIT_PROPERTY_CHECKBOX_CLICKED: "bulk_edit_property_checkbox_clicked",
  BULK_EDIT_TREE_NODE_RIGHT_SIDE_BUTTON_CLICKED: "bulk_edit_tree_node_right_side_button_clicked",
};

const oPropTypes = {
  bulkEditViewData: ReactPropTypes.object,
};

// @CS.SafeComponent
class BulkEditView extends React.Component {
  static propTypes = oPropTypes;

  handleRequestPopoverCancel = () => {
    EventBus.dispatch(oEvents.BULK_EDIT_CANCEL_BUTTON_CLICKED);
  };

  handleApplyClicked = (oSummaryView) => {
    EventBus.dispatch(oEvents.BULK_EDIT_APPLY_BUTTON_CLICKED, oSummaryView);
  };

  handleButtonClick = (sButtonId) => {
    if (sButtonId === "apply") {
      let oSummaryView = this.getSummaryView();
      this.handleApplyClicked(oSummaryView);
    }
    else {
      this.handleRequestPopoverCancel();
    }
  };

  handleGetTreeNodeChildrenList = (sTreeNodeId) => {
    EventBus.dispatch(oEvents.BULK_EDIT_GET_TREE_NODE_CHILDREN, sTreeNodeId);
  };

  handlePropertyCheckboxClicked = (oSelectedProperty) => {
    EventBus.dispatch(oEvents.BULK_EDIT_PROPERTY_CHECKBOX_CLICKED, oSelectedProperty);
  };

  getReadOnlySummaryOfAttribute = (oElement) => {
    let oAttribute = oElement.data;
    let oReferencedAttribute = oElement.referencedData;
    let value = oAttribute.valueAsHtml || oAttribute.value;
    let oProperties = {
      hideDescription: true,
      isMultiLine: false
    };

    if (ViewUtils.isAttributeTypeNumber(oReferencedAttribute.type) || ViewUtils.isAttributeTypeText(oReferencedAttribute.type)) {
      oProperties.isMultiLine = true;
    }

    let oContentAttributeElementViewModel = new ContentAttributeElementViewModel(oReferencedAttribute.id, CS.getLabelOrCode(oReferencedAttribute), value, true, "", "", oReferencedAttribute, oProperties);
    return <ContentAttributeElementView model={oContentAttributeElementViewModel}/>;
  };

  getReadOnlySummaryOfTag = (oElement) => {
    let oTag = oElement.data;
    let oReferencedTag = oElement.referencedData;
    let oTags = {
      [oTag.tagId]: {
        id: oTag.tagId,
        baseType: MockDataForEntityBaseTypes.tagInstanceBaseType,
        tagId: oReferencedTag.id,
        tagValues: CS.cloneDeep(oTag.tagValues)
      }
    };

    let oTagGroupModel = ViewUtils.getTagGroupModels(oReferencedTag, {tags: oTags}, {}, "bulkEditTag");
    let oProperties = oTagGroupModel.tagGroupModel.properties;

    return (
          <TagGroupView
              tagGroupModel={oTagGroupModel.tagGroupModel}
              tagRanges={oProperties.tagRanges}
              tagValues={oTagGroupModel.tagValues}
              masterTagList={{[oReferencedTag.id] : oReferencedTag}}
              disabled={true}
              hideTooltip={true}
              showLabel={false}
              extraData={{entityId: oTag.tagId, outerContext: "bulkEditTag"}}
              showDefaultIcon={true}
          />
    );
  };

  getSummaryContent = () => {
    let oBulkEditData = this.props.bulkEditViewData;
    let oBulkEditSummary = oBulkEditData.bulkEditSummary;
    let aSummaryRows = [];

    CS.forEach(oBulkEditSummary, (oElement, sId) => {
      let oRowValue = null;
      let oIconDOM = null;

      switch (oElement.type) {
        case "attribute":
          oRowValue = this.getReadOnlySummaryOfAttribute(oElement);
          oIconDOM = this.getIconDOM(oElement.referencedData.iconKey);
          break;

        case "tag":
          oRowValue = this.getReadOnlySummaryOfTag(oElement);
          oIconDOM = this.getIconDOM(oElement.referencedData.iconKey);
          break;

        case "taxonomy":
          oRowValue = this.getSummary(oElement.data, null, BulkEditTabTypesConstants.TAXONOMIES);
          break;

        case "class":
          oRowValue = this.getSummary(oElement.data, null, BulkEditTabTypesConstants.CLASSES);
          break;
      }

      aSummaryRows.push(
          <div className="summaryRow" key={sId}>
            <div className="elementHeader">
              {oIconDOM}
              {oElement.header}
              <div className={"elementType " + oElement.type}></div>
            </div>
            <div className="elementValue">{oRowValue}</div>
          </div>
      );
    });

    return aSummaryRows;
  };

  getIconDOM = (sIconKey) => {
    let sImageSrc = ViewUtils.getIconUrl(sIconKey);
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sImageSrc}/>
        </div>
    )
  };

  getSummaryView = () => {
    return (
        <div className="bulkEditSummary">
          <div className="summaryHeader">{getTranslation().BULK_EDIT_SUMMARY}</div>
            <div className="summaryContent">
              {this.getSummaryContent()}
            </div>
          <div className="summaryFooter">
            <div className="warningMessage">{getTranslation().BULK_EDIT_WARNING_MESSAGE}</div>
          </div>
        </div>
    )
  };

  getLeftPanelView = (oLeftPanelData) => {
    return (
        <div className="bulkEditLeftPanelView">
          <GroupMssWrapperView
              groupsData={oLeftPanelData.properties}
              activeOptions={oLeftPanelData.selectedProperties}
              showPopup={false}
              showApply={false}
              hideChips={true}
              isMultiSelect={true}
              handleOptionClicked={this.handlePropertyCheckboxClicked}
              hideSelectedCount={true}
          />
        </div>
    )
  };

  getRightPanelView = (oPropertiesRightPanelData) => {
    return (
        <div className="bulkEditRightPanelView">
            <BulkEditAppliedPropertiesView
                {...oPropertiesRightPanelData}
            />
        </div>
    )
  };

  getUpperToolbarView = () => {
    let oBulkEditData = this.props.bulkEditViewData;

    return oBulkEditData.toolbarData.bulkEditToolbar.map(oToolbar => ({
      id: oToolbar.id,                           /* Required field*/
      key: oToolbar.id + "_" + oToolbar.label,        /* concatenating id with label and index for uniqueness*/
      label: CS.getLabelOrCode(oToolbar),
      className: oToolbar.className
    }))
  };

  getBulkEditPropertiesView = (oPropertiesTabData) => {
    return (
        <React.Fragment>
          {this.getLeftPanelView(oPropertiesTabData.leftPanelData)}
          {this.getRightPanelView(oPropertiesTabData.rightPanelData)}
        </React.Fragment>
    )
  };

  getTaxonomiesToAdd = (oTaxonomiesToAdd) => {
    let sNothingFoundMessage = getTranslation().BULK_EDIT_NO_TAXONOMY_SELECTED_FOR_ADDING_MESSAGE;

    return (
      <div className="addTaxonomy">
        <div className="header">
          <div>{getTranslation().ADD_TAXONOMY}</div>
          <TooltipView label={getTranslation().BULK_EDIT_ADD_TAXONOMY_HEADER_DESCRIPTION} placement="right">
            <div className="infoIcon"></div>
          </TooltipView>
        </div>
        <div className="summary">
          {this.getSummary(oTaxonomiesToAdd, sNothingFoundMessage, BulkEditTabTypesConstants.TAXONOMIES)}
        </div>
      </div>
    )
  };

  getClassesToAdd = (oClassesToAdd) => {
    let sNothingFoundMessage = getTranslation().BULK_EDIT_NO_CLASS_SELECTED_FOR_ADDING_MESSAGE;

    return (
      <div className="addClass">
        <div className="header">
          <div>{getTranslation().ADD_CLASS}</div>
          <TooltipView label={getTranslation().BULK_EDIT_ADD_CLASS_HEADER_DESCRIPTION} placement="right">
            <span className="infoIcon"></span>
          </TooltipView>
        </div>
        <div className="summary">
          {this.getSummary(oClassesToAdd, sNothingFoundMessage, BulkEditTabTypesConstants.CLASSES)}
        </div>
      </div>
    )
  };

  getSummary = (oEntities, sNothingFoundMessage, sContext) => {
    let aSummaryView = [];

    CS.forEach(oEntities, (aEntities) => {
      let sSelectedTaxonomy = aEntities[aEntities.length - 1].taxonomyType || "";

      let oChipStyle = {};
      let oSelectedTaxonomyDOM = null;

      switch (sContext) {
        case BulkEditTabTypesConstants.TAXONOMIES:
          oSelectedTaxonomyDOM = <div className={"taxonomyType " + sSelectedTaxonomy}></div>;
          oChipStyle = {
            width: "calc(100% - 25px)",
            float: "left"
          }
          break;

        case BulkEditTabTypesConstants.CLASSES:
          oChipStyle = {
            width: "inherit",
            display: "inline-block",
            margin: "0px 4px"
          }
          break;
      }

      aSummaryView.push(
        <React.Fragment>
          {oSelectedTaxonomyDOM}
          <ActionableChipsView items={aEntities}
            showLink={true}
            chipStyle={oChipStyle}
            context={sContext} />
        </React.Fragment>
      )
    });

    if (CS.isEmpty(aSummaryView)) {
      aSummaryView.push(
        <div className="nothingFoundMessage">{sNothingFoundMessage}</div>
      )
    }

    return aSummaryView;
  };

  getTaxonomiesToRemove = (oTaxonomiesToRemove) => {
    let sNothingFoundMessage = getTranslation().BULK_EDIT_NO_TAXONOMY_SELECTED_FOR_REMOVING_MESSAGE;

    return (
      <div className="removeTaxonomy">
        <div className="header">
          <div>{getTranslation().REMOVE_TAXONOMY}</div>
          <TooltipView label={getTranslation().BULK_EDIT_REMOVE_TAXONOMY_HEADER_DESCRIPTION} placement="right">
            <div className="infoIcon"></div>
          </TooltipView>
        </div>
        <div className="summary">
          {this.getSummary(oTaxonomiesToRemove, sNothingFoundMessage, BulkEditTabTypesConstants.TAXONOMIES)}
        </div>
      </div>
    )
  };

  getClassesToRemove = (oClassesToRemove) => {
    let sNothingFoundMessage = getTranslation().BULK_EDIT_NO_CLASS_SELECTED_FOR_REMOVING_MESSAGE;

    return (
      <div className="removeClass">
        <div className="header">
          <div>{getTranslation().REMOVE_CLASS}</div>
          <TooltipView label={getTranslation().BULK_EDIT_REMOVE_CLASS_HEADER_DESCRIPTION} placement="right">
            <div className="infoIcon"></div>
          </TooltipView>
        </div>
        <div className="summary">
          {this.getSummary(oClassesToRemove, sNothingFoundMessage, BulkEditTabTypesConstants.CLASSES)}
        </div>
      </div>
    )
  };

  getTaxonomySummaryForBulkEditView = (oTaxonomySummary) => {
    return (
        <div className="selectedSummaryView">
          <div className="summaryIcon taxonomy"></div>
            {this.getTaxonomiesToAdd(oTaxonomySummary.taxonomiesToAdd)}
            {this.getTaxonomiesToRemove(oTaxonomySummary.taxonomiesToDelete)}
        </div>
    )
  };

  getClassSummaryForBulkEditView = (oClassSummary) => {
    return (
        <div className="selectedSummaryView">
          <div className="summaryIcon class"></div>
            {this.getClassesToAdd(oClassSummary.classesToAdd)}
            {this.getClassesToRemove(oClassSummary.classesToDelete)}
        </div>
    )
  };

  handleTreeNodeRightSideButtonClicked = (sContext, sButtonId, sNodeId) => {
    EventBus.dispatch(oEvents.BULK_EDIT_TREE_NODE_RIGHT_SIDE_BUTTON_CLICKED, sContext, sButtonId, sNodeId);
  };

  getTreeView = (oClassTabData, sSelectedTabId) => {
    let fLoadChildNodesHandler = CS.noop;

    if(CS.isEmpty(oClassTabData.searchText) && sSelectedTabId  !== BulkEditTabTypesConstants.CLASSES) {
      fLoadChildNodesHandler = this.handleGetTreeNodeChildrenList
    }

    return (
      <TreeView
        {...oClassTabData.treeViewModel}
        nodeCheckClickHandler={CS.noop}
        loadChildNodesHandler={fLoadChildNodesHandler}
        rightSideActionButtonsHandler={this.handleTreeNodeRightSideButtonClicked}
        suppressUnmount={true}
        showLevelInHeader={sSelectedTabId  !== BulkEditTabTypesConstants.CLASSES}
      />
    )
  };

  getBulkEditTaxonomyView = (oTaxonomiesTabData) => {
    return (
        <React.Fragment>
          {this.getTaxonomySummaryForBulkEditView(oTaxonomiesTabData.taxonomySummary)}
            <div className="bulkEditTaxonomyTreeView">
              {this.getTreeView(oTaxonomiesTabData, BulkEditTabTypesConstants.TAXONOMIES)}
          </div>
        </React.Fragment>
    )
  };

  getBulkEditClassView = (oClassesTabData) => {
    return (
        <React.Fragment>
          {this.getClassSummaryForBulkEditView(oClassesTabData.classSummary)}
          <div className="bulkEditTaxonomyTreeView">
            <div className="treeView">
              {this.getTreeView(oClassesTabData, BulkEditTabTypesConstants.CLASSES)}
            </div>
          </div>
        </React.Fragment>
    )
  };

  getBulkEditView = () => {
    let oBulkEditData = this.props.bulkEditViewData;
    let sSelectedTabId = oBulkEditData.selectedTabId;
    let oBulkEditView = null;

    if (sSelectedTabId === BulkEditTabTypesConstants.PROPERTIES) {
      oBulkEditView = this.getBulkEditPropertiesView(oBulkEditData.propertiesTabData);
    }
    else if (sSelectedTabId === BulkEditTabTypesConstants.TAXONOMIES) {
      oBulkEditView = this.getBulkEditTaxonomyView(oBulkEditData.taxonomiesTabData);
    }
    else if (sSelectedTabId === BulkEditTabTypesConstants.CLASSES) {
      oBulkEditView = this.getBulkEditClassView(oBulkEditData.classesTabData);
    }

    return (
      <div className="bulkEditView">
        <TabLayoutView
          tabList={this.getUpperToolbarView()}
          activeTabId={oBulkEditData.selectedTabId}
          addBorderToBody={true}>
          {oBulkEditView}
        </TabLayoutView>
      </div>
    )
  };

  getBulkEditFullScreenView = () => {
    let oBulkEditViewData = this.props.bulkEditViewData;
    let oPropertiesTabData = oBulkEditViewData.propertiesTabData;
    let oTaxonomiesTabData = oBulkEditViewData.taxonomiesTabData;
    let oTaxonomySummary = oTaxonomiesTabData.taxonomySummary;
    let oClassesTabData = oBulkEditViewData.classesTabData;
    let oClassSummary = oClassesTabData.classSummary;
    let bIsPropertyTabDirty = oPropertiesTabData.leftPanelData && CS.isNotEmpty(oPropertiesTabData.leftPanelData.selectedProperties);
    let bIsTaxonomyTabDirty = oTaxonomySummary
        && (CS.isNotEmpty(oTaxonomySummary.taxonomiesToAdd) || CS.isNotEmpty(oTaxonomySummary.taxonomiesToDelete));
    let bIsClassTabDirty = oClassSummary && (CS.isNotEmpty(oClassSummary.classesToAdd) || CS.isNotEmpty(oClassSummary.classesToDelete));
    let bIsDirty = bIsPropertyTabDirty || bIsTaxonomyTabDirty || bIsClassTabDirty;

    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true,
        isDisabled: false,
        actionHandler: this.handleButtonClick,
        variant: "outlined",
      }
    ];

    if(bIsDirty) {
      aButtonData.push(
          {
            id: "apply",
            label: getTranslation().APPLY,
            isFlat: false,
            isDisabled: !bIsDirty,
            actionHandler: this.handleButtonClick
          }
      )
    }

    return (
        <FullScreenView
            header={getTranslation().BULK_EDIT}
            showHeader={true}
            bodyView={this.getBulkEditView()}
            isFullScreenMode={oBulkEditViewData.isBulkEditViewOpen}
            actionButtonsData={aButtonData}
        />
    )
  };

  render () {
    return this.getBulkEditFullScreenView();
  }
}

export const view = BulkEditView;
export const events = oEvents;
