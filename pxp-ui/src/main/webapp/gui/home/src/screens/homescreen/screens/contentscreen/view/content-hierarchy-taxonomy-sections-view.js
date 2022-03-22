import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as TabLayoutView } from './../../../../../viewlibraries/tablayoutview/tab-layout-view';
import { view as ContentSectionViewNew } from './content-section-view-new';
import { view as ContentLinkedSectionsView } from './content-linked-sections-view';
import { view as NothingFoundView } from './../../../../../viewlibraries/nothingfoundview/nothing-found-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_TAXONOMY_SECTION_ELEMENT_INPUT_CHANGED: "handle_taxonomy_section_element_input_changed",
  HANDLE_TAXONOMY_HIERARCHY_VIEW_MODE_TOGGLED: "handle_taxonomy_hierarchy_view_mode_toggled",
  HANDLE_TAXONOMY_HIERARCHY_SECTION_EXPAND_BUTTON_TOGGLED: "handle_taxonomy_hierarchy_section_expand_button_toggled"
};

const oPropTypes = {
  taxonomySectionData: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  entityViewData: ReactPropTypes.object
};

// @CS.SafeComponent
class FltrTaxonomySectionsView extends React.Component {

  static propTypes = oPropTypes;

  // Not in use
  handleSectionElementInputChanged = (sSectionId, sElementId, sProperty, sNewValue) => {
    EventBus.dispatch(oEvents.HANDLE_TAXONOMY_SECTION_ELEMENT_INPUT_CHANGED, sSectionId, sElementId, sProperty, sNewValue);
  };

  handleTaxonomyHierarchyModeIconClicked = (sViewMode) => {
    EventBus.dispatch(oEvents.HANDLE_TAXONOMY_HIERARCHY_VIEW_MODE_TOGGLED, sViewMode, this.props.filterContext);
  };

  handleTaxonomyHierarchySectionExpandButtonToggled = (sSectionId) => {
    EventBus.dispatch(oEvents.HANDLE_TAXONOMY_HIERARCHY_SECTION_EXPAND_BUTTON_TOGGLED, sSectionId, this.props.filterContext);
  };

  getAddedEntitiesInTaxonomyView = () => {
    var oTaxonomySectionsData = this.props.taxonomySectionData;
    return oTaxonomySectionsData.addedEntitiesView;
  };

  getNormalSectionView = (oSectionModel, oEntityViewData, oSelectedElementInformation) => {
    return (
        <ContentSectionViewNew
            referencedClasses = {oEntityViewData.referencedClasses}
            referencedContents={oEntityViewData.referencedContents}
            referencedRelationships = {oEntityViewData.referencedRelationships}
            referencedTags = {oEntityViewData.referencedTags}
            referencedTaxonomies = {oEntityViewData.referencedTaxonomies}
            referencedCollections = {oEntityViewData.referencedCollections}
            referencedTasks = {oEntityViewData.referencedTasks}
            selectedElementInformation = {oSelectedElementInformation}
            conflictingValues = {oEntityViewData.conflictingValues}
            violatingMandatoryElements = {oEntityViewData.violatingMandatoryElements}
            globalPermission = {oEntityViewData.globalPermission}
            ruleViolation = {this.props.ruleViolation}
            model={oSectionModel}
            showExpansionToggle={true}
            filterContext={this.props.filterContext}
            expansionToggleHandler={this.handleTaxonomyHierarchySectionExpandButtonToggled.bind(this, oSectionModel.id)}
        />);
  };

  getViewsForActiveSections = () => {
    var __props = this.props;
    var _this = this;

    var oEntityViewData = __props.entityViewData;
    let aActiveSectionData = __props.taxonomySectionData.taxonomySectionModels; //todo: change key

    var aLinkItemList = [];
    var aSectionalViews = [];

    CS.forEach(aActiveSectionData, (oSection) => {

      let oSectionObj = {
        sectionId: oSection.id,
        view: null
      };

      let oLinkItem = {
        id: oSection.id,
        label: CS.getLabelOrCode(oSection),
        icon: oSection.icon,
        code: oSection.code
      };


      oSectionObj.view = _this.getNormalSectionView(oSection, oEntityViewData, __props.taxonomySectionData.selectedContext);
      oSectionObj.isPropertyCollectionSection = true;


      aLinkItemList.push(oLinkItem);
      aSectionalViews.push(oSectionObj)
    });

    return {
      linkItemList: aLinkItemList,
      sectionalViews: aSectionalViews
    };
  };

  getDetailBodyView = () => {
    var oTaxonomySectionsData = this.props.taxonomySectionData;
    var sActiveTaxonomyViewMode = oTaxonomySectionsData.taxonomyViewMode;

    if (sActiveTaxonomyViewMode === "sectionViewMode") {
        let oSectionalViewsData = this.getViewsForActiveSections();
        let aLinkItemsData = oSectionalViewsData.linkItemList;
        let aSectionComponent = oSectionalViewsData.sectionalViews;

        if (!CS.isEmpty(aLinkItemsData)) {
          return (
              <ContentLinkedSectionsView
                  linkItemsData={aLinkItemsData}
                  selectedElementInformation={oTaxonomySectionsData.selectedContext}
                  sectionComponents={aSectionComponent}/>
          );
        } else {
          return (
             <NothingFoundView/>
          );
        }
    } else if (sActiveTaxonomyViewMode == "thumbnailViewMode"){
      return this.getAddedEntitiesInTaxonomyView();
    }
  };

  getTabsListForTaxonomyHierarchy = () => {
    var oTaxonomySectionsData = this.props.taxonomySectionData;
    var oClickedNode = oTaxonomySectionsData.taxonomyClickedNode;

    var aTabsList = [{
      id: "thumbnailViewMode",
      label: getTranslation().PRODUCTS
    }];
    return aTabsList;
  };

  render() {
    var oTaxonomySectionsData = this.props.taxonomySectionData;
    var sActiveTaxonomyViewMode = oTaxonomySectionsData.taxonomyViewMode;
    var oClickedNode = oTaxonomySectionsData.taxonomyClickedNode;
    var sNodeLabel = CS.getLabelOrCode(oClickedNode);

    var oDetailViewBody = this.getDetailBodyView();

    var oStyle = {};
    if(CS.isEmpty(sNodeLabel)){
      oStyle.display = "none";
    }

    var aTabsList = this.getTabsListForTaxonomyHierarchy();

    var sBodyClassSuffix = sActiveTaxonomyViewMode == "thumbnailViewMode" ? " thumbViewPresent" : "";
    return (
        <div className="taxonomySectionsViewContainer">
          <TabLayoutView tabList={aTabsList}
                         activeTabId={sActiveTaxonomyViewMode}
                         filterContext={this.props.filterContext}
                         onChange={this.handleTaxonomyHierarchyModeIconClicked}>
            <div className={"taxonomyTabularSectionBody" + sBodyClassSuffix}>
              {oDetailViewBody}
            </div>
          </TabLayoutView>
        </div>
    );
  }
}



export const view = FltrTaxonomySectionsView;
export const events = oEvents;
