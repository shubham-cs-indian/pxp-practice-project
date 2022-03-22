
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as MultiSelectSearchView } from '../../../../../viewlibraries/multiselectview/multi-select-search-view';
import { view as TagGroupView } from '../../../../../viewlibraries/taggroupview/tag-group-view';
import ContentScreenConstants from '../store/model/content-screen-constants';
import { view as ContentSectionAttributeView } from './content-section-attribute-view';
import { view as ContentSectionElementViewNew } from './content-section-element-view-new';
import { view as ContentSectionReadOnlyAttributesView } from './content-section-read-only-attributes-view';
import { view as RelationshipSectionElementView } from './relationship-section-element-view';
import SmallTaxonomyViewModel from '../../../../../viewlibraries/small-taxonomy-view/model/small-taxonomy-view-model';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import {view as MultiClassificationSectionView} from "./multiclassification-section-view";
const getTranslation = ScreenModeUtils.getTranslationDictionary;


const oEvents = {
  NEW_CONTENT_SECTION_ROLE_MSS_VALUE_CHANGED: "new_content_section_role_mss_value_changed"
};

const oPropTypes = {
  model: ReactPropTypes.shape({
    id: ReactPropTypes.string,
    label: ReactPropTypes.string,
    icon: ReactPropTypes.string,
    elements: ReactPropTypes.object,
    properties: ReactPropTypes.object
  }).isRequired,
  selectedElementInformation: ReactPropTypes.object,
  onAttributeClick: ReactPropTypes.func,
  contextToUpdateForSCU: ReactPropTypes.array,
  relationshipContextData: ReactPropTypes.object,
  conflictingValues: ReactPropTypes.object,
  conflictingSources: ReactPropTypes.object,
  allPossibleConflictingSources: ReactPropTypes.object,
  globalPermission: ReactPropTypes.object,
  referencedClasses: ReactPropTypes.object,
  referencedContents: ReactPropTypes.object,
  referencedRelationships: ReactPropTypes.object,
  referencedTags: ReactPropTypes.object,
  referencedAttributes: ReactPropTypes.object,
  referencedTaxonomies: ReactPropTypes.object,
  referencedCollections: ReactPropTypes.array,
  ruleViolation: ReactPropTypes.object,
  violatingMandatoryElements: ReactPropTypes.array,
  hierarchyContext: ReactPropTypes.string,
  referencedTasks: ReactPropTypes.object,
  canRemove: ReactPropTypes.bool,
  onRemoveHandler: ReactPropTypes.func,
  showExpansionToggle: ReactPropTypes.bool,
  expansionToggleHandler: ReactPropTypes.func,
  isHelperLanguageSelected: ReactPropTypes.bool,
  helperLanguageInstances: ReactPropTypes.array,
  filterContext:ReactPropTypes.object,
  hideSectionLabel: ReactPropTypes.bool,
};

//todo rename
// @CS.SafeComponent
class ContentSectionViewNew extends React.Component {

  constructor (props) {
    super(props);
    this.isSelected = false;
  }

  shouldComponentUpdate (oNextProps, oNextState) {
    const oPreviousProp = this.props;
    let oModel = oNextProps.model;
    let oProperties = oModel.properties;
    const sSectionId = oModel.id;
    const aSectionsToUpdate = oProperties['sectionsToUpdate'];
    let aPreviousSectionsToUpdate = oPreviousProp.model.properties['sectionsToUpdate'];

    let oSelectedElementInformation = oNextProps.selectedElementInformation;

    if (oSelectedElementInformation.updateAll) {
      return true;
    }

    if (CS.includes(aSectionsToUpdate, sSectionId) || CS.includes(aPreviousSectionsToUpdate, sSectionId)) {
      return true;
    }

    let oPreviousSelectedContext = this.props.selectedElementInformation;
    let oNextSelectedContext = oNextProps.selectedElementInformation;

    if (!CS.isEmpty(oPreviousSelectedContext) && oPreviousSelectedContext.sectionId == sSectionId) {
      return true;
    }

    if (!CS.isEmpty(oNextSelectedContext) && oNextSelectedContext.sectionId == sSectionId) {
      return true;
    }

    return false;
  }

  handleSectionToggle (sSectionId) {
    let __props = this.props;
    if (CS.isFunction(__props.expansionToggleHandler)) {
      __props.expansionToggleHandler(sSectionId);
    }
  }

  handleContentSectionRoleMSSValueChanged (sRoleId, aCheckedItems) {
    EventBus.dispatch(oEvents.NEW_CONTENT_SECTION_ROLE_MSS_VALUE_CHANGED, sRoleId, aCheckedItems);
  }

  getMultiSelectSearchViewWithletiantsAndVersionsForTag (oElement) {
    let oModel = oElement.model;
    let sRef = oElement.id;
    let oAttributeView = this.getTagGroupView(oModel, sRef);

    let sClassName = "sectionElementContainerNew ";
    if (oElement.isDisabled) {
      sClassName += "disableBoxShadowOnHover";
    }

    return (<div className={sClassName}>
      {oAttributeView}
    </div>);
  }

  getMultiSelectSearchViewWithletiantsAndVersionsForRole (oElement) {
    let oModel = oElement.model;
    let sRef = oElement.id;
    let fOnApply = this.handleContentSectionRoleMSSValueChanged.bind(this, oElement.id);
    let oAttributeView = this.getMultiSelectSearchView(oModel, fOnApply);
    return (<div className="sectionElementContainerNew">
      {oAttributeView}
    </div>);
  }


  getMultiSelectSearchView (oModel, fOnApply) {
    return (
        <MultiSelectSearchView
            disabled={oModel.disabled}
            label={CS.getLabelOrCode(oModel)}
            showLabel={false}
            items={oModel.items}
            selectedItems={oModel.selectedItems}
            isMultiSelect={!oModel.singleSelect}
            onApply={fOnApply}
            cannotRemove={oModel.disableCross}
        />
    );
  }

  getTagGroupView (oModel) {
    let oTagGroupModel = oModel.tagGroupModel;
    let oExtraData = oTagGroupModel.properties["extraData"];
    let oReferencedTags = this.props.referencedTags;
    let oProperties = oModel.tagGroupModel.properties;
    return (
        <TagGroupView
            tagGroupModel={oTagGroupModel}
            tagRanges={oProperties.tagRanges}
            tagValues={oModel.tagValues}
            disabled={oModel.disabled}
            singleSelect={oProperties.singleSelect}
            hideTooltip={true}
            showLabel={false}
            extraData={oExtraData}
            masterTagList={oReferencedTags}
            customRequestObject={oModel.customRequestObject}
            showDefaultIcon ={true}
        />
    );
  }

  getSmallTaxonomyViewModel (oMultiTaxonomyData, oHeaderPermission, sRootTaxonomyId) {
    let oSelectedMultiTaxonomyList = oMultiTaxonomyData.selectedMultiTaxonomyList;
    let oMultiTaxonomyListToAdd = oMultiTaxonomyData.multiTaxonomyListToAdd;
    let sActiveEntitySelectedTabId = oMultiTaxonomyData.activeEntitySelectedTabId;
    let oProperties = {};
    oProperties.isTimelineTab = sActiveEntitySelectedTabId === ContentScreenConstants.tabItems.TAB_TIMELINE;
    oProperties.isletiantSelected = false;
    oProperties.selectedTabId = sActiveEntitySelectedTabId;
    oProperties.headerPermission = oHeaderPermission;
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  }

  getTaxonomyView (oElement) {

    let oMinorTaxonomyView = <MultiClassificationSectionView
        multiClassificationViewData={oElement.minorTaxonomiesData}
        sTaxonomyId={oElement.id}
        context={"minorTaxonomiesSectionView"}
        dialogContext={"minorTaxonomiesDialogContext"}
    />

    let oDetailView = null;
    if (oElement.elements) {
      oDetailView = this.getSectionView(oElement.elements)
    }

    let oView = (
        <div className="sectionElementContainerNew disableBoxShadowOnHover">
          {oMinorTaxonomyView}
        </div>
    );

    return {
      view: oView,
      detailView: oDetailView
    };
  }

  getRelationShipElementView (oElement, sSectionId) {
    let __props = this.props;
    let oRelationshipContextData = (__props.relationshipContextData && __props.relationshipContextData.relationshipId == sSectionId)
        ? __props.relationshipContextData : null;
    return (<RelationshipSectionElementView element={oElement}
                                            relationshipContextData={oRelationshipContextData}/>);
  }

  getAttributeElementView (oElement) {
    let __props = this.props;
    let oMasterData = __props.model;
    let oProperties = oMasterData.properties;

    let sClassName = "sectionElementContainerNew ";
    if (oElement.isDisabled) {
      sClassName += "disableBoxShadowOnHover"
    }
    return (<div className={sClassName}>
      <ContentSectionAttributeView
          sectionElement={oElement}
          sectionId={oMasterData.id}
          selectedContext={__props.selectedElementInformation}
          sectionContext={oProperties['sectionContext']}
          dataId={oProperties['dataId']}
          showletiantInfo={false}
          showLabel={false}
          filterContext={this.props.filterContext}
      />
    </div> );
  }

  getReadOnlySectionView = (oElement) => {
    let aHelperLanguageInstances = this.props.helperLanguageInstances;
    let bIsAttributeComparisonViewSelected = this.props.isHelperLanguageSelected;
    let oAttribute = oElement.attribute;
    let bShowReadOnlySection = oElement.type === "attribute" && bIsAttributeComparisonViewSelected &&
        !CS.isEmpty(aHelperLanguageInstances) && oAttribute.isTranslatable;

    if (bShowReadOnlySection) {
      let oLanguageInstance = aHelperLanguageInstances[0];
      let aDependentAttributes = oLanguageInstance.dependentAttributes;
      let oHelperAttribute = CS.find(aDependentAttributes, {attributeId: oElement.id});

      /** Helper attribute will not found if it has no value **/
      if (!CS.isEmpty(oHelperAttribute)) {
        oHelperAttribute.type = oAttribute.type;
        oHelperAttribute.label = oAttribute.label;
        return (
            <ContentSectionReadOnlyAttributesView
                ruleViolation={oLanguageInstance.ruleViolation}
                attribute={oHelperAttribute}
            />
        );
      }
    }

    return null;
  };

  getSectionView = (aElements, fillerID) => {
    let __props = this.props;
    let oMasterData = __props.model;
    let oProperties = oMasterData.properties;
    let aElementDOMs = [];
    let oElements = aElements || oMasterData.elements;
    let oSelectedElementInformation = __props.selectedElementInformation;
    let sSectionContext = oProperties['sectionContext'];
    let sSectionId = oMasterData.id;
    let bIsAttributeComparisonViewSelected = __props.isHelperLanguageSelected;

    if (!oProperties.isCollapsed) {
      CS.forEach(oElements, (oElement) => {
        let sMasterEntityId = "";
        if (!CS.isEmpty(oElement)) {
          let bIsSelected = (oSelectedElementInformation && oSelectedElementInformation.elementId == oElement.id &&
          (sSectionId == oSelectedElementInformation.sectionId) && (oSelectedElementInformation.fillerID == fillerID));
          let oView = null;
          let oTaxonomyDetailView = null;
          let sTooltip = oElement.label;
          let sElementType = oElement.type;
          let sElementLabel = "";

          let oReadOnlySectionView = this.getReadOnlySectionView(oElement);
          switch (oElement.type) {

            case "relationship":
              oView = this.getRelationShipElementView(oElement, sSectionId);
              break;

            case "attribute":
              sTooltip = (oElement.attribute && !CS.isEmpty(oElement.attribute.tooltip)) ? oElement.attribute.tooltip : sTooltip;
              sElementLabel = (oElement.attribute) ? CS.getLabelOrCode(oElement.attribute) : sElementLabel;
              sMasterEntityId = oElement.attribute ? oElement.attribute.id : "";
              oView = this.getAttributeElementView(oElement);
              break;

            case "tag":
              sTooltip = "";
              sElementLabel = (oElement.tag) ? CS.getLabelOrCode(oElement.tag) : sElementLabel;
              sMasterEntityId = oElement.tag ? oElement.tag.id : "";
              oView = this.getMultiSelectSearchViewWithletiantsAndVersionsForTag(oElement);
              break;

            case "taxonomy":
              sMasterEntityId = oElement.taxonomy ? oElement.taxonomy.id : "";
              sElementLabel = CS.getLabelOrCode(oElement);
              sTooltip = "";
              let oTaxonomyView = this.getTaxonomyView(oElement);
              oView = oTaxonomyView.view;
              oTaxonomyDetailView = oTaxonomyView.detailView;
              break;

            case 'filler':
              //Minor taxonomy nested property collection handling
              sMasterEntityId = "";
              sElementLabel = CS.getLabelOrCode(oElement);
              sTooltip = "";
              oView = this.getSectionView(oElement.elements, oElement.id);
              aElementDOMs.push(
                  <div className="sectionElementTaxonomyData" key="sectionElementTaxonomyData">
                    <div className="sectionTaxonomyDataElement">{sElementLabel}</div>
                    {oView}
                  </div>
              );
              return;

            case "role":
              sElementLabel = CS.getLabelOrCode(oElement);
              sMasterEntityId = oElement.role ? oElement.role.id : "";
              sElementLabel = (oElement.role) ? CS.getLabelOrCode(oElement.role) : sElementLabel;
              oView = this.getMultiSelectSearchViewWithletiantsAndVersionsForRole(oElement);
              break;

            default:
              //Unwanted cases
              return;
          }

          aElementDOMs.push(
              <div className="sectionElement" key={oElement.id}>
              <ContentSectionElementViewNew
                  key={oElement.id}
                  fillerID={fillerID}
                  sectionElement={oElement}
                  masterEntityId={sMasterEntityId}
                  label={sElementLabel}
                  isSelected={bIsSelected}
                  tooltip={sTooltip}
                  sectionContext={sSectionContext}
                  sectionId={sSectionId}
                  violatingMandatoryElements={__props.violatingMandatoryElements}
                  onAttributeClick={__props.onAttributeClick}
                  conflictingValues={__props.conflictingValues}
                  conflictingSources={__props.conflictingSources}
                  allPossibleConflictingSources = {__props.allPossibleConflictingSources}
                  referencedClasses={__props.referencedClasses}
                  referencedContents={__props.referencedContents}
                  referencedRelationships={__props.referencedRelationships}
                  referencedTags={__props.referencedTags}
                  referencedAttributes={__props.referencedAttributes}
                  referencedTaxonomies={__props.referencedTaxonomies}
                  referencedCollections={__props.referencedCollections}
                  ruleViolation={__props.ruleViolation}
                  hierarchyContext={__props.hierarchyContext}
                  referencedTasks={__props.referencedTasks}
                  filterContext={this.props.filterContext}
                  selectedElementInformation={__props.selectedElementInformation}>
                {oView}
              </ContentSectionElementViewNew>
                {/* TODO : Execute this only if helper language is selected*/}
                {oReadOnlySectionView}
              </div>
          );

          if (!CS.isEmpty(oTaxonomyDetailView)) {
            aElementDOMs.push(oTaxonomyDetailView);
          }
        }

      });
    }

    if (CS.isEmpty(aElementDOMs) && oProperties.canBeEmpty) {
      aElementDOMs = (<div className="nothingToDisplayContainer">
        <div className="nothingToDisplay">{getTranslation().NOTHING_TO_DISPLAY}</div>
      </div>);
    }

    return aElementDOMs;
  };

  getLabelDOM () {
    let __props = this.props;
    let oMasterData = __props.model;
    let oLabelDOM = null;
    let sLabel = CS.getLabelOrCode(oMasterData);

    if (__props.showExpansionToggle) {
      let sSectionToggleClass = "sectionToggle ";
      if (oMasterData.properties.isCollapsed) {
        sSectionToggleClass += " sectionCollapsed";
      } else {
        sSectionToggleClass += " sectionExpanded";
      }
      oLabelDOM = (<div className="expandableBar">
        <div className={sSectionToggleClass} onClick={this.handleSectionToggle.bind(this, oMasterData.id)}></div>
        <div className={"contentSectionNewLabel"}>{sLabel}</div>
      </div>);
    } else {
      oLabelDOM = (<div className={"contentSectionNewLabel"}>{sLabel}</div>);
    }
    return oLabelDOM;
  }

  render () {
    let oModel = this.props.model;
    let bHideSectionLabel = oModel.properties.hideSectionLabel;
    let oStyle = {};
    if (bHideSectionLabel) {
      oStyle.marginTop = "0";
    }
    return (
        <div className={"sectionWrapper"} style={oStyle}>
          {bHideSectionLabel ? null : this.getLabelDOM()}
          {this.props.canRemove ? <TooltipView label={getTranslation().REMOVE}>
            <div className="removeIcon" onClick={this.props.onRemoveHandler}></div>
          </TooltipView> : null}
          <div className="sectionContainerNew">{this.getSectionView()}</div>
        </div>
    );
  }
}

ContentSectionViewNew.propTypes = oPropTypes;



export const events = oEvents;
export const view = ContentSectionViewNew;
