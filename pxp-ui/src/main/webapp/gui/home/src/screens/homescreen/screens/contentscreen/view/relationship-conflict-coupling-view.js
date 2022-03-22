import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';

import CS from './../../../../../libraries/cs';
import {getTranslations as getTranslation} from './../../../../../commonmodule/store/helper/translation-manager';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import PropertyCouplingIconsView from "../../../../../viewlibraries/propertyWrapperView/property-coupling-icons-view";
import PropertyConflictingValuesIconView from "../../../../../viewlibraries/propertyWrapperView/property-conflicting-values-icon-view";
import PropertyConflictingValueFooterView from "../../../../../viewlibraries/propertyWrapperView/property-conflicting-value-footer-view";

const oEvents = {
  RELATIONSHIP_COUPLING_ICON_CLICKED: "relationship_coupling_icon_clicked",
  RELATIONSHIP_CONFLICT_VALUE_OPTION_CLICKED: "relationship_conflict_value_option_clicked",
  RELATIONSHIP_CONFLICT_VALUE_RESOLVED: "relationship_conflict_value_resolved"
};

const oPropTypes = {
  oSectionElement: ReactPropTypes.object,
  oConflictingSources: ReactPropTypes.object,
  oRuleViolationObject: ReactPropTypes.object,
  fOnDialogClose: ReactPropTypes.func,
  oAllowedAttributes: ReactPropTypes.object,
  oReferencedAttributes: ReactPropTypes.object,
  fConflictContainerAttributeRef: ReactPropTypes.func,
  fConflictContainerTagRef: ReactPropTypes.func,
  fRuleViolationBodyContainerRef: ReactPropTypes.func,
  fHandleDefaultValueChanged: ReactPropTypes.func,
  fHandleDefaultValueChangedForTags: ReactPropTypes.func
};

// @CS.SafeComponent
class RelationshipConflictCouplingView extends React.Component {

  static propTypes = oPropTypes;

  setRef = (sContext, element) => {
    if (!CS.isEmpty(element)) {
      this[sContext] = element;
    }
  };

  handleSectionElementConflictIconClicked (sElementId, bShowConflictSourcesOnly, bIsConflictResolved) {
    EventBus.dispatch(oEvents.RELATIONSHIP_COUPLING_ICON_CLICKED, sElementId, bShowConflictSourcesOnly, bIsConflictResolved);
  }

  handleRelationshipConflictPropertyResolved = function(fOnDialogClose, oRelationship) {
    let oSelectedConflictValue = CS.find(oRelationship.conflictingValues, {isChecked: true})
    if (!CS.isEmpty(oSelectedConflictValue)) {
      EventBus.dispatch(oEvents.RELATIONSHIP_CONFLICT_VALUE_RESOLVED, oSelectedConflictValue, oRelationship);
    }
  }

  handleRelationshipConflictPropertyClicked = function(oConflictingValue, oRelationship) {
    EventBus.dispatch(oEvents.RELATIONSHIP_CONFLICT_VALUE_OPTION_CLICKED, oConflictingValue, oRelationship);
  }

  getCouplingIconView (oEntityInstance, oSectionElement, oAllConflictingSources, sHierarchyContext, fHandler, fRef) {
    return (
        <PropertyCouplingIconsView
            oEntityInstance={oEntityInstance}
            oSectionElement={oSectionElement}
            oAllConflictingSources={oAllConflictingSources}
            sHierarchyContext={sHierarchyContext}
            fHandler={fHandler}
            fRef={fRef}
        />)
  }

  getConflictingFooterView (oSectionElement, oConflictingSources, oRuleViolationObject, fOnDialogClose, oAllowedAttributes, oReferencedAttributes,
                            fConflictContainerAttributeRef, fConflictContainerTagRef, fRuleViolationBodyContainerRef, fHandleDefaultValueChanged, fHandleDefaultValueChangedForTags) {
    return (
        <PropertyConflictingValueFooterView
            oSectionElement={oSectionElement}
            oConflictingSources={oConflictingSources}
            oRuleViolationObject={oRuleViolationObject}
            oAllowedAttributes={oAllowedAttributes}
            oReferencedAttributes={oReferencedAttributes}
            fConflictContainerAttributeRef={fConflictContainerAttributeRef}
            fConflictContainerTagRef={fConflictContainerTagRef}
            fRuleViolationBodyContainerRef={fRuleViolationBodyContainerRef}
            fHandleDefaultValueChanged={fHandleDefaultValueChanged}
            fHandleDefaultValueChangedForTags={fHandleDefaultValueChangedForTags}
            fOnDialogClose={fOnDialogClose}
            fHandleConflictValueResolved = {this.handleRelationshipConflictPropertyResolved.bind(this, fOnDialogClose)}
            fHandleDialogOptionValueClicked = {this.handleRelationshipConflictPropertyClicked}
        />)
  }

  getConflictingValuesIconView (oEntityInstance, bIsForNotification, oSectionElement, fHandler, ref, fRef) {
    return (
        <PropertyConflictingValuesIconView
            oEntityInstance={oEntityInstance}
            bIsForNotification={bIsForNotification}
            oSectionElement={oSectionElement}
            fHandler={fHandler}
            ref={ref}
            fRef={fRef}
        />)
  }

  getDisabledIconView () {
    return (
        <TooltipView label={getTranslation().DISABLED_SECTION} placement="bottom">
          <div className="notClickableIcon disabledIcon"></div>
        </TooltipView>
    )
  }

  render = () => {
    let _this = this;
    let __props = this.props;
    let oElement = __props.entityInstance;
    let oDisabledIcon = oElement.couplingType == "dynamicCoupled" ? _this.getDisabledIconView() : null;

    let oContentRelationship = CS.filter(__props.entityViewData.activeEntity.contentRelationships, {
      sideId: oElement.id,
      relationshipId: oElement.propertyId
    });

    let bIsForNotification;
    if (oContentRelationship) {
      // TODO: Pre-existing bad code below
      oContentRelationship = (Array.isArray(oContentRelationship) ? oContentRelationship[0] : oContentRelationship) || {};
      bIsForNotification = oContentRelationship.conflictingValues && oContentRelationship.conflictingValues.length > 1 ? false : true;
    }

    let oCouplingView = _this.getCouplingIconView(oContentRelationship, oElement, __props.entityViewData.allPossibleConflictingSources, undefined,
        this.handleSectionElementConflictIconClicked.bind(this, oElement.id, true), this.setRef.bind(this, oElement.propertyId + "conflictingSources"))

    let oConflictIcon = _this.getConflictingValuesIconView(oContentRelationship, bIsForNotification, oElement, this.handleSectionElementConflictIconClicked.bind
    (this, oElement.id, false), this.dropDownButton, this.setRef.bind(this, oElement.id + "conflictingSourcesWithValues"))

    let oConflictingValueView = _this.getConflictingFooterView(oElement, __props.entityViewData.conflictingSources, __props.ruleViolation,
        this.handleSectionElementConflictIconClicked.bind(this, oElement.id, false))

    return (
        <div className="upperIconWrapper">
          <div className="clickableIconsContainer">
            {oConflictIcon}
            {oCouplingView}
            {oConflictingValueView}
          </div>
          <div className="notClickableIconsContainer">
            {oDisabledIcon}
          </div>
        </div>
    )
  }

}


export const view = RelationshipConflictCouplingView;
export const events = oEvents;
