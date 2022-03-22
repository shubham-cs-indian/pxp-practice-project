import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as LazyContextMenuView} from '../lazycontextmenuview/lazy-context-menu-view';
import { view as GridYesNoPropertyView} from './../../viewlibraries/gridview/grid-yes-no-property-view';


const oEvents = {
  AUTHORIZATION_MAPPING_APPLY_BUTTON_CLICKED: "authorization_mapping_apply_button_clicked",
  AUTHORIZATION_MAPPING_CHECKBOX_BUTTON_CLICKED: "authorization_mapping_checkbox_button_clicked",
  AUTHORIZATION_MAPPING_DELETE_BUTTON_CLICKED: "authorization_mapping_delete_button_clicked",
  AUTHORIZATION_MAPPING_YES_NO_BUTTON_CHANGED: "authorization_mapping_yes_no_button_changed",
};

const oPropTypes = {
  elementData: ReactPropTypes.object,
};

// @CS.SafeComponent
class AuthorizationMappingSummaryView extends React.Component {
  constructor (props) {
    super(props);
  }

  handleLazyMSSView = (oElement) => {
    let aSelectedItems = oElement.selectedMappings || [];
    let oReferencedData = oElement.referencedData || {};

    switch (oElement.summaryType) {
      case "attributes":
        //Create Lazy View for Attribute
        let oReferencedAttributes = !CS.isEmpty(oReferencedData.referencedAttributes) ? oReferencedData.referencedAttributes : {};
        let oAttributeReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.attributes : {};
        // return this.getLazyMSSView(aSelectedItems, oReferencedAttributes, oAttributeReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled);
        return this.getLazyMSSView(aSelectedItems, oReferencedAttributes, oAttributeReqResInfo, null, oElement.summaryType, false, false, true);

      case "tags":
        //Create Lazy View for Tag
        let oReferencedDataForTags = oReferencedData.referencedData || {};
        let oReferencedTags = !CS.isEmpty(oReferencedDataForTags.referencedTags) ? oReferencedDataForTags.referencedTags : {};
        let oTagReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.tags : {};
        // return this.getLazyMSSView(aSelectedItems, oReferencedTags, oTagReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled);
        return this.getLazyMSSView(aSelectedItems, oReferencedTags, oTagReqResInfo, null, oElement.summaryType, false, false, true);


      case "taxonomies":
        //Create Lazy View for Taxonomy
        let oReferencedDataForTaxonomiesData = oReferencedData.referencedData || {};
        let oReferencedTaxonomies = !CS.isEmpty(oReferencedDataForTaxonomiesData.referencedTaxonomies) ? oReferencedDataForTaxonomiesData.referencedTaxonomies : {};
        let oTaxonomyReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.taxonomies : {};
        // return this.getLazyMSSView(aSelectedItems, oReferencedTaxonomies, oTaxonomyReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled);
        return this.getLazyMSSView(aSelectedItems, oReferencedTaxonomies, oTaxonomyReqResInfo, null, oElement.summaryType, false, false, true);

      case "classes":
        //Create Lazy View for Klass
        let oReferencedDataForKlassesData = oReferencedData.referencedData || {};
        let oReferencedKlasses = !CS.isEmpty(oReferencedDataForKlassesData.referencedKlasses) ? oReferencedDataForKlassesData.referencedKlasses : {};
        let oClassesReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.classes : {};
        // return this.getLazyMSSView(aSelectedItems, oReferencedKlasses, oClassesReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled);
        return this.getLazyMSSView(aSelectedItems, oReferencedKlasses, oClassesReqResInfo, null, oElement.summaryType, false, false, true);


      case "contexts":
        //Create Lazy View for Context
        let oReferencedDataForContextsData = oReferencedData.referencedData || {};
        let oReferencedContexts = !CS.isEmpty(oReferencedDataForContextsData.referencedContexts) ? oReferencedDataForContextsData.referencedContexts : {};
        let oContextsReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.contexts : {};
        // return this.getLazyMSSView(aSelectedItems, oReferencedContexts, oContextsReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled);
        return this.getLazyMSSView(aSelectedItems, oReferencedContexts, oContextsReqResInfo, null, oElement.summaryType, false, false, true);


      case "relationships":
        //Create Lazy View for Relationship
        let oReferencedDataForRelationshipsData = oReferencedData.referencedData || {};
        let oReferencedRelationships = !CS.isEmpty(oReferencedDataForRelationshipsData.referencedRelationships) ? oReferencedDataForRelationshipsData.referencedRelationships : {};
        let oRelationshipsReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.relationships : {};
        // return this.getLazyMSSView(aSelectedItems, oReferencedRelationships, oRelationshipsReqResInfo, this.handleMappedElementChanged.bind(this, sId), sId, true, bIsDisabled);
        return this.getLazyMSSView(aSelectedItems, oReferencedRelationships, oRelationshipsReqResInfo, null, oElement.summaryType, false, false, true);
    }
  };

  getLazyMSSView = (aSelectedItems, oReferencedData, oReqResInfo, fOnApply, sContext, bCannotRemove, bIsDisabled, bIsMultiSelect) => {
    let oAnchorOrigin = {horizontal: 'left', vertical: 'top'};
    let oTargetOrigin = {horizontal: 'left', vertical: 'bottom'};
    return (
        <LazyContextMenuView
            isMultiselect={bIsMultiSelect}
            onApplyHandler={this.handleApplyClicked.bind(this, sContext)}
            anchorOrigin={oAnchorOrigin}
            targetOrigin={oTargetOrigin}
            menuListHeight={"250px"}
            requestResponseInfo={oReqResInfo}
            selectedItems={aSelectedItems}
            className={""}
            key={sContext}>
          <div className='addTemplateHandler'>
          </div>
        </LazyContextMenuView>
    );
  };

  getLabel = (sSelectedElement, sContext, oReferencedData) => {
    let oRefObject = {};
    switch (sContext) {
      case "attributes":
        oRefObject = oReferencedData.referencedAttributes[sSelectedElement];
        break;

      case "tags":
        oRefObject = oReferencedData.referencedTags[sSelectedElement];
        break;


      case "taxonomies":
        oRefObject = oReferencedData.referencedTaxonomies[sSelectedElement];
        break;

      case "classes":
        oRefObject = oReferencedData.referencedKlasses[sSelectedElement];
        break;


      case "contexts":
        oRefObject = oReferencedData.referencedContexts[sSelectedElement];
        break;


      case "relationships":
        oRefObject = oReferencedData.referencedRelationships[sSelectedElement];
        break;
    }

    return CS.getLabelOrCode(oRefObject);

  };

  getRows = (sSelectedElement, sContext, oReferencedData) => {
    let sLabel = this.getLabel(sSelectedElement, sContext, oReferencedData);
    return (
        <div className="list">
          {sLabel}
          <div className="deleteRow"
               onClick={this.handleDeleteIconClicked.bind(this, sSelectedElement, sContext)}></div>
        </div>)
  };

  handleSelectedMappingListDetails = (oElementData) => {
    let _this = this;
    let aSelectedMappings = oElementData.selectedMappings;
    let oReferencedData = oElementData.referencedData;
    let oListDOM = [];
    CS.forEach(aSelectedMappings, function (sSelectedElement) {
      let oRows = _this.getRows(sSelectedElement, oElementData.summaryType, oReferencedData) || null;
      oListDOM.push(oRows);
    })
    return oListDOM;
  };

  getAuthorizationMappingSection = (oElementData, sLabel) => {
    let oAddEntityView = this.handleLazyMSSView(oElementData);
    let oSelectedMappingListView = this.handleSelectedMappingListDetails(oElementData);
    return (
        <div className="containerListView">
          <div className="headerWrapper">
            {oAddEntityView}
            <div className="headerLabel">{sLabel}</div>
          </div>
          <div className="listGroup">
            {oSelectedMappingListView}
          </div>
        </div>
    );
  };

  getYesNoPropertyView = (bIsActivationEnable, oElementData) => {
    let sLabelForYesNoView = "Accept Blank Values?";
    return (
        <div className="toggleSelection">
          <GridYesNoPropertyView
              isDisabled={false}
              value={bIsActivationEnable}
              onChange={this.handleGridYesNoChanged.bind(this, bIsActivationEnable, oElementData)}
          />
          {sLabelForYesNoView}
        </div>)
  };

  handleApplyClicked = (sContext, aSelectedItems, oReferencedData) => {
    EventBus.dispatch(oEvents.AUTHORIZATION_MAPPING_APPLY_BUTTON_CLICKED, sContext, aSelectedItems, oReferencedData);
  };

  handleTabCheckboxClicked = (bIsCheckboxClicked, sContext) => {
    EventBus.dispatch(oEvents.AUTHORIZATION_MAPPING_CHECKBOX_BUTTON_CLICKED, bIsCheckboxClicked, sContext);
  };

  handleDeleteIconClicked = (sSelectedElement, sContext) => {
    EventBus.dispatch(oEvents.AUTHORIZATION_MAPPING_DELETE_BUTTON_CLICKED, sSelectedElement, sContext);
  };

  handleGridYesNoChanged = (bIsActivationEnable, oElementData) => {
    EventBus.dispatch(oEvents.AUTHORIZATION_MAPPING_YES_NO_BUTTON_CHANGED, bIsActivationEnable, oElementData);
  }

  render () {
    let oElementData = this.props.elementData;
    let sSelectedTabId = oElementData.selectedTabId;
    let oTabHeaderData = oElementData.tabHeaderData;
    let oElement = CS.find(oTabHeaderData, {id: sSelectedTabId});
    let sLabel = getTranslation().SELECT_ALL + " " + oElement.label;
    let bIsCheckboxClicked = oElementData.isCheckboxClicked;
    let bIsToggleSelectionClicked = oElementData.isToggleSelectionClicked;
    let oAuthorizationMappingDOM = (!bIsCheckboxClicked) ? this.getAuthorizationMappingSection(oElementData, oElement.label) : null;
    let aDisableToggleSelectionView = ["CLASS_MAP","CONTEXT_MAP","RELATIONSHIP_MAP"];
    let bDisableToggleSelectionView = aDisableToggleSelectionView.includes(sSelectedTabId) ? true : false;
    let oYesNoView = bDisableToggleSelectionView ? null : this.getYesNoPropertyView(bIsToggleSelectionClicked, oElementData);
    return (
        <div className="checkboxWrapper">
          <input type={"checkbox"} className={"propertyCheckbox"} checked={bIsCheckboxClicked}
                 onClick={this.handleTabCheckboxClicked.bind(this, bIsCheckboxClicked, oElementData.summaryType)}/>
          <div className={"propertyCheckboxLabel"}>{sLabel}</div>
          {oYesNoView}
          {oAuthorizationMappingDOM}
        </div>)
  }
}

AuthorizationMappingSummaryView.propTypes = oPropTypes;

export const view = AuthorizationMappingSummaryView;
export const events = oEvents;


