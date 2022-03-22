
import CS from '../../libraries/cs';
import React from 'react';
import {view as LazyContextMenuView} from "../lazycontextmenuview/lazy-context-menu-view";
import EventBus from "../../libraries/eventdispatcher/EventDispatcher";
import ConfigDataEntitiesDictionary from "../../commonmodule/tack/config-data-entities-dictionary";

const oEvents = {
  RELATIONSHIP_SIDE2SECTION_APPLY_BUTTON_CLICKED:"relationship_side2section_apply_button_clicked",
  RELATIONSHIP_SIDE2SECTION_DELETE_BUTTON_CLICKED:"relationship_side2section_delete_button_clicked",
};

const oPropTypes = {

};

// @CS.SafeComponent
class SectionListView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleApplyClicked = (sContext, aSelectedItems, oReferencedData) => {
    EventBus.dispatch(oEvents.RELATIONSHIP_SIDE2SECTION_APPLY_BUTTON_CLICKED, sContext, aSelectedItems, oReferencedData);
  };

  handleDeleteIconClicked = (sSelectedElement, sContext) => {
    EventBus.dispatch(oEvents.RELATIONSHIP_SIDE2SECTION_DELETE_BUTTON_CLICKED, sSelectedElement, sContext);
  };

  getLabel = (sSelectedElement, sContext, oReferencedData) => {
    let oRefObject = {};
    switch (sContext) {
      case ConfigDataEntitiesDictionary.ATTRIBUTES:
        oRefObject = oReferencedData.referencedAttributes[sSelectedElement];
        break;

      case ConfigDataEntitiesDictionary.TAGS:
        oRefObject = oReferencedData.referencedTags[sSelectedElement];
        break;

      case ConfigDataEntitiesDictionary.RELATIONSHIPS:
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

  getSelectedListDetails = (oElementData) => {
    let _this = this;
    let aSelectedMappings = [];
    switch (oElementData.summaryType) {
      case ConfigDataEntitiesDictionary.ATTRIBUTES:
        aSelectedMappings = oElementData.selectedAttributes;
        break;
      case ConfigDataEntitiesDictionary.TAGS:
        aSelectedMappings = oElementData.selectedTags;
        break;
      case ConfigDataEntitiesDictionary.RELATIONSHIPS:
        aSelectedMappings = oElementData.selectedRelationships;
        break;
    }
    let oReferencedData = oElementData.referencedData;
    let oListDOM = [];
    CS.forEach(aSelectedMappings, function (sSelectedElement) {
      let oRows = _this.getRows(sSelectedElement, oElementData.summaryType, oReferencedData) || null;
      oListDOM.push(oRows);
    })
    return oListDOM;
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
            showSelectedInDropdown={true}
            className={""}
            key={sContext}>
          <div className='addTemplateHandler'>
          </div>
        </LazyContextMenuView>
    );
  };

  handleLazyMSSView = (oElement) => {
    let oReferencedData = oElement.referencedData || {};

    switch (oElement.summaryType) {
      case ConfigDataEntitiesDictionary.ATTRIBUTES:
        //Create Lazy View for Attribute
        let oReferencedAttributes = !CS.isEmpty(oReferencedData.referencedAttributes) ? oReferencedData.referencedAttributes : {};
        let oAttributeReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.attributes : {};
        return this.getLazyMSSView(oElement.selectedAttributes, oReferencedAttributes, oAttributeReqResInfo, null, oElement.summaryType, false, false, true);

      case ConfigDataEntitiesDictionary.TAGS:
        //Create Lazy View for Tag
        let oReferencedTags = !CS.isEmpty(oReferencedData.referencedTags) ? oReferencedData.referencedTags : {};
        let oTagReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.tags : {};
        return this.getLazyMSSView(oElement.selectedTags, oReferencedTags, oTagReqResInfo, null, oElement.summaryType, false, false, true);

      case ConfigDataEntitiesDictionary.RELATIONSHIPS:
        //Create Lazy View for Relationship
        let oReferencedRelationships = !CS.isEmpty(oReferencedData.referencedRelationships) ? oReferencedData.referencedRelationships : {};
        let oRelationshipsReqResInfo = !CS.isEmpty(oElement.lazyMSSReqResInfo) ? oElement.lazyMSSReqResInfo.relationships : {};
        return this.getLazyMSSView(oElement.selectedRelationships, oReferencedRelationships, oRelationshipsReqResInfo, null, oElement.summaryType, false, false, true);

    }
  };

  getSectionListView = (oElementData, sLabel) => {
    let oAddEntityView = this.handleLazyMSSView(oElementData);
    let oSelectedMappingListView = this.getSelectedListDetails(oElementData);
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

  render() {
    let _this = this;
    let aElementData = this.props.elementData;
    let aSectionListView = [];
    CS.forEach(aElementData, function (oElementData) {
      let oTabHeaderData = oElementData.tabHeaderData;
      let oElement = CS.find(oTabHeaderData, {id: oElementData.tabId});
      aSectionListView.push(_this.getSectionListView(oElementData, oElement.label));
    })


    return (<div className="sectionListViewWrapper">
      {aSectionListView}
    </div>);
  }

}

SectionListView.propTypes = oPropTypes;

export const view = SectionListView;
export const events = oEvents;
