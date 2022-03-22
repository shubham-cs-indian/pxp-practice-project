import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import ViewUtils from './utils/view-utils';

import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as PropertyTasksEditButtonView } from './property-tasks-edit-button-view';
import { view as ElementCouplingTypeChangeButtonView } from './element-coupling-type-change-button-view';
import CouplingConstants from '../../../../../commonmodule/tack/coupling-constans';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
import PropertyCouplingIconsView from './../../../../../viewlibraries/propertyWrapperView/property-coupling-icons-view';
import PropertyConflictingValuesIconView from './../../../../../viewlibraries/propertyWrapperView/property-conflicting-values-icon-view';
import PropertyConflictingValueFooterView from './../../../../../viewlibraries/propertyWrapperView/property-conflicting-value-footer-view';
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
import { view as PropertyInfoButtonView } from './property-info-button-view';
import GridViewColumnIdConstants
  from "../../../../../viewlibraries/contextualgridview/tack/grid-view-column-id-constants";

const getTranslation = ScreenModeUtils.getTranslationDictionary;


const oEvents = {
  //todo rename
  NEW_CONTENT_SECTION_ATTRIBUTE_CLICKED: "new_content_section_attribute_clicked",
  NEW_CONTENT_SECTION_MASK_CLICKED: "new_content_section_mask_clicked",
  NEW_CONTENT_SECTION_VIEW_ATTRIBUTE_DEFAULT_VALUE_CHANGED: "new_content_section_view_attribute_default_value_changed",
  NEW_CONTENT_SECTION_VIEW_TAG_DEFAULT_VALUE_CHANGED: "new_content_section_view_tag_default_value_changed",
  NEW_CONTENT_SECTION_ELEMENT_NOTIFICATION_BUTTON_CLICKED: "new_content_section_element_notification_button_clicked",
  NEW_CONTENT_SECTION_ELEMENT_CROSS_ICON_CLICKED: "new_content_section_element_cross_icon_clicked",
  NEW_CONTENT_SECTION_ELEMENT_CONFLICT_ICON_CLICKED: "new_content_section_element_conflict_icon_clicked",
};
const oPropTypes = {
  sectionElement: ReactPropTypes.object.isRequired,
  masterEntityId: ReactPropTypes.string,
  label: ReactPropTypes.string,
  isSelected: ReactPropTypes.bool,
  tooltip: ReactPropTypes.string,
  elementStyle: ReactPropTypes.object,
  sectionContext: ReactPropTypes.string,
  sectionId: ReactPropTypes.string,
  onAttributeClick: ReactPropTypes.func,
  selectedElementInformation: ReactPropTypes.object,
  violatingMandatoryElements: ReactPropTypes.array,
  conflictingValues: ReactPropTypes.object,
  allPossibleConflictingSources: ReactPropTypes.object,
  referencedClasses: ReactPropTypes.object,
  referencedRelationships: ReactPropTypes.object,
  referencedTags: ReactPropTypes.object,
  referencedAttributes: ReactPropTypes.object,
  referencedTaxonomies: ReactPropTypes.object,
  referencedCollections: ReactPropTypes.array,
  ruleViolation: ReactPropTypes.object,
  hierarchyContext: ReactPropTypes.string,
  referencedTasks: ReactPropTypes.object,
  showCrossIcon: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object,
  conflictingSources: ReactPropTypes.object,
  fillerID: ReactPropTypes.string,
};
//todo rename
// @CS.SafeComponent
class ContentSectionElementViewNew extends React.Component {

  constructor (props) {
    super(props);
    this.isSelected = this.props.isSelected;

    this.setRef = ( sRef, element) => {
      this[sRef] = element;
    };
  }

  componentDidUpdate () {
    let oProps = this.props;
    let oSelectedElementInformation = oProps.selectedElementInformation;
    let oElement = this.props.sectionElement;
    let bSetFocus = (oSelectedElementInformation && oSelectedElementInformation.elementId == oElement.id
        && oSelectedElementInformation.sectionId == oProps.sectionId && oSelectedElementInformation.fillerID == oProps.fillerID);

    if (bSetFocus && !this.isSelected) {
      this.isSelected = true;
      let DOM = ReactDOM.findDOMNode(this);
      if (DOM) {
        let input = DOM.getElementsByTagName('input');
        let textArea = DOM.getElementsByTagName('textarea');
        let froala = DOM.getElementsByClassName('fr-element');
        if (input.length) {
          input[0].focus();
        }
        if (textArea.length) {
          textArea[0].focus();
        }
        if (froala.length) {
          froala[0].focus();
        }
      }
    } else if (!this.props.isSelected) {
      this.isSelected = false;
    }
  };

  shouldComponentUpdate (oNextProps) {
    let oPreviousProp = this.props;
    let oElement = this.props.sectionElement;
    let sElementId = oElement.id;

    let oNextSelectedContext = oNextProps.selectedElementInformation;
    let oPreviousSelectedContext = oPreviousProp.selectedElementInformation;

    if (oNextSelectedContext.updateAll) {
      return true;
    }

    if (CS.isEmpty(oNextSelectedContext) || CS.isEmpty(oNextSelectedContext.elementId)) {
      return true;
    }

    if (!CS.isEmpty(oPreviousSelectedContext) && oPreviousSelectedContext.elementId == sElementId) {
      return true;
    }

    if (!CS.isEmpty(oNextSelectedContext) && oNextSelectedContext.elementId == sElementId) {
      return true;
    }

    return false;
  }

  handleDefaultValueChanged (sOldValue, sValue, sAttributeId, sSourceId) {
    let sSectionId = this.props.sectionId;
    // if(sOldValue != sValue) {
    EventBus.dispatch(oEvents.NEW_CONTENT_SECTION_VIEW_ATTRIBUTE_DEFAULT_VALUE_CHANGED, sAttributeId, sValue, sSectionId, sSourceId);
    // }
  }

  handleDefaultValueChangedForTags (aTagValues, aConflictingTagValues, sTagId, sSourceId) {
    let sSectionId = this.props.sectionId;
    EventBus.dispatch(oEvents.NEW_CONTENT_SECTION_VIEW_TAG_DEFAULT_VALUE_CHANGED, sTagId, aConflictingTagValues, sSectionId, sSourceId);
  }

  handleSectionElementClicked (sContext, sElementId, sMasterEntityId, sAttributeType, bIsInherited, bIsReadOnlyCoupled, oEvent) {
    oEvent.nativeEvent.dontRaise = true;
    let oProps = this.props;
    let sSectionContext = oProps.sectionContext;
    let sStructureId = '';
    let sSectionId = oProps.sectionId;
    if (bIsInherited) {
      EventBus.dispatch(oEvents.NEW_CONTENT_SECTION_MASK_CLICKED, sSectionId, sElementId, sSectionContext, this.props.filterContext);
    } else if (oProps.onAttributeClick) {
      oProps.onAttributeClick(sContext, sElementId, sMasterEntityId);
    } else if (oProps.selectedElementInformation.elementId != sElementId) {
      EventBus.dispatch(oEvents.NEW_CONTENT_SECTION_ATTRIBUTE_CLICKED, this, sSectionContext, sContext, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, bIsReadOnlyCoupled, oProps.fillerID);
    }
    oEvent.stopPropagation();
  }

  handleSectionElementNotificationButtonClicked (sContext, oElement, oEvent) {
    EventBus.dispatch(oEvents.NEW_CONTENT_SECTION_ELEMENT_NOTIFICATION_BUTTON_CLICKED, this, sContext, oElement);
  }

  handleCrossIconClicked (oProperty) {
    EventBus.dispatch(oEvents.NEW_CONTENT_SECTION_ELEMENT_CROSS_ICON_CLICKED, oProperty);
  }

  /** Basically 2 scenarios are there
  1 : To show only possible conflicting sources
  2 : To show conflicting sources with conflicting values **/
  handleSectionElementConflictIconClicked (sElementId, bShowConflictSourcesOnly, bIsConflictResolved) {
    EventBus.dispatch(oEvents.NEW_CONTENT_SECTION_ELEMENT_CONFLICT_ICON_CLICKED, sElementId, bShowConflictSourcesOnly, bIsConflictResolved);
  }

  toggleBodyDisplay (sBodyRef, sIconRef) {

    let oBodyDom = this[sBodyRef];
    let oIconDom = this[sIconRef];
    if (CS.includes(oBodyDom.classList, "collapsed")) {
      oBodyDom.classList.remove("collapsed");
      oBodyDom.classList.add("expanded");
      oIconDom.classList.add("selected");
    } else {
      oBodyDom.classList.add("collapsed");
      oBodyDom.classList.remove("expanded");
      oIconDom.classList.remove("selected");
    }
  }

  getElementFooters () {
    let oElement = this.props.sectionElement;
    let fRuleViolationBodyContainerRef, fConflictContainerAttributeRef, fConflictContainerTagRef,
    fHandleDefaultValueChanged ,fHandleDefaultValueChangedForTags = CS.noop;
    // Binding ref here as setref is fn is available here
    if (oElement.type == "attribute") {
      let oAttribute = oElement.contentAttributes[0];
      if (oAttribute) {
        let sAttributeId = oAttribute.attributeId == "typeattribute" ? oAttribute.value : oAttribute.attributeId;
        fRuleViolationBodyContainerRef = this.setRef.bind(this, "ruleViolationBody_" + sAttributeId);
        fConflictContainerAttributeRef = this.setRef.bind(this,"conflict_def_body_" + oAttribute.attributeId);
      }
      fHandleDefaultValueChanged = this.handleDefaultValueChanged.bind(this);
    } else if (oElement.type == "tag") {
      fConflictContainerTagRef = this.setRef.bind(this,"conflict_def_body_" + oElement.id);
      fRuleViolationBodyContainerRef = this.setRef.bind(this, "ruleViolationBody_" + oElement.id);
      fHandleDefaultValueChangedForTags = this.handleDefaultValueChangedForTags.bind(this);
    }
    return (
      <PropertyConflictingValueFooterView
        oSectionElement={oElement}
        oConflictingSources={this.props.conflictingSources}
        oRuleViolationObject={this.props.ruleViolation}
        fOnDialogChanged = {CS.noop}
        oAllowedAttributes={this.props.allowedAttributes}
        oReferencedAttributes={this.props.referencedAttributes}
        fConflictContainerAttributeRef = {fConflictContainerAttributeRef}
        fConflictContainerTagRef = {fConflictContainerTagRef}r
        fRuleViolationBodyContainerRef = {fRuleViolationBodyContainerRef}
        fHandleDefaultValueChanged = {fHandleDefaultValueChanged}
        fHandleDefaultValueChangedForTags = {fHandleDefaultValueChangedForTags}
    />);
  }

//header block
  getDisabledIcon () {

    return (
        <TooltipView label={getTranslation().DISABLED_SECTION} placement="bottom">
          <div className="notClickableIcon disabledIcon"></div>
        </TooltipView>);
  }

  getCouplingIconsDOM (sEntityInstanceId, oEntityInstance) {
    let fHandler = this.handleSectionElementConflictIconClicked.bind(this, sEntityInstanceId, true);
    return (
        <PropertyCouplingIconsView
            oEntityInstance={oEntityInstance}
            oSectionElement={this.props.sectionElement}
            oAllConflictingSources={this.props.allPossibleConflictingSources}
            sHierarchyContext={this.props.hierarchyContext}
            fHandler={fHandler}
            fRef={this.setRef.bind(this, sEntityInstanceId + "conflictingSources")}
        />);

  }

  getElementTypeIcon (sElementType) {
    if (CS.isEmpty(sElementType)) {
      return null;
    } else {
      let sLabel = "";
      if (sElementType == "attribute") {
        sLabel = getTranslation().ATTRIBUTE;
      } else if (sElementType == "tag") {
        sLabel = getTranslation().TAG;
      } else if (sElementType == "role") {
        sLabel = getTranslation().ROLE;
      } else if (sElementType == "taxonomy") {
        sLabel = getTranslation().TAXONOMY;
      }
      let sClassName = "notClickableIcon " + sElementType;
      return (
          <TooltipView placement="bottom" label={sLabel}>
            <div className={sClassName}></div>
          </TooltipView>
      );
    }
  }

  getConflictingValuesIcon (sElementId, oEntityInstance, bIsForNotification) {
    let fHandler = this.handleSectionElementConflictIconClicked.bind(this, sElementId, false, false);
    return (
      <PropertyConflictingValuesIconView
          oEntityInstance={oEntityInstance}
          bIsForNotification={bIsForNotification}
          oSectionElement={this.props.sectionElement}
          fHandler={fHandler}
          fRef={this.setRef.bind(this,sElementId + "conflictingSourcesWithValues")}
      />);
  }

  getRuleViolationIcon (sBodyRef, sMasterEntityId) {

    let oRuleViolationObject = this.props.ruleViolation;
    if (!oRuleViolationObject) {
      return null;
    }
    let aRuleViolation = oRuleViolationObject[sMasterEntityId];
    let sClassName = "clickableIcon ruleViolationIcon upperIcon ";
    CS.forEach(aRuleViolation, function (oRuleViolation) {
      sClassName += oRuleViolation.color + " ";
    });

    let sIconRef = sBodyRef + "_violationIconRef";
    let fRuleHandler = this.props.isSelected ? this.toggleBodyDisplay.bind(this, sBodyRef, sIconRef) : CS.noop();
    return (
        <TooltipView placement="bottom" label={getTranslation().RULE_VIOLATION_LABEL}>
          <div className={sClassName} ref={this.setRef.bind(this,sIconRef)}
               onClick={fRuleHandler}></div>
        </TooltipView>);
  }

  getClickableCouplingTypeChangeIcon (oElement) {
    /** This icon is visible in case of Hierarchy Mode only*/

    let __props = this.props;
    if (oElement.attribute &&
        (ViewUtils.isAttributeTypeConcatenated(oElement.attribute.type) ||
            ViewUtils.isAttributeTypeCalculated(oElement.attribute.type))) {
      return null;
    }
    var sHierarchyContext = __props.hierarchyContext;
    if (sHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY || sHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY) {
      var sSectionId = __props.sectionId;
      return <ElementCouplingTypeChangeButtonView element={oElement} sectionId={sSectionId}
                                                  context={sHierarchyContext}/>
    } else {
      return null;
    }
  }

  getCrossIcon (sId, sType) {
    let oProperty = {
      id: sId,
      type: sType
    };
    return (
        <TooltipView placement="bottom" label={getTranslation().REMOVE}>
          <div className="contentSectionElementViewCrossIcon"
               onClick={this.handleCrossIconClicked.bind(this, oProperty)}></div>
        </TooltipView>);
  }

  getPropertyTaskEditButtonView (oElement, sContext) {
    if(ViewUtils.getIsCurrentUserReadOnly()) {
      return null;
    }
    let __props = this.props;
    let sHierarchyContext = __props.hierarchyContext;
    if (sHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY || sHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY) {
      return null;
    }
    let aContentElements = [];
    let oProperty = {};
    switch (sContext) {
      case "attribute":
        aContentElements = oElement.contentAttributes;
        let oAttribute = CS.find(aContentElements, {attributeId: oElement.id}) || {};
        oProperty.id = oAttribute.attributeId;
        oProperty.instanceId = oAttribute.id;
        break;
      case "tag":
        aContentElements = oElement.contentTags;
        let oTag = CS.find(aContentElements, {tagId: oElement.id}) || {};
        oProperty.id = oTag.tagId;
        oProperty.instanceId = oTag.id;
        break;
      case "role":
        let oContentRole = oElement.model;
        let oMasterRole = oElement.role;
        oProperty.id = oMasterRole.id;
        oProperty.instanceId = oContentRole.id;
        break;
      case "taxonomy":
        //todo confirm .model
        let oContentTaxonomy = oElement.model;
        oProperty.id = oElement.id;
        oProperty.instanceId = oContentTaxonomy.id;
        break;
    }
    oProperty.type = sContext;

    return (
        <PropertyTasksEditButtonView property={oProperty}
                                     isTasksExists={oElement.isTasksExists}
                                     taskDialogData={oElement.taskDialogData}

        />
    );
  }

  getNotificationIconView (sBodyRef) {

    let sIconRef = sBodyRef + "_NotificationIconRef";
    return (<TooltipView placement="bottom" label={getTranslation().NOTIFICATION}>
      <div className="clickableIcon notificationIcon upperIcon" ref={this.setRef.bind(this,sIconRef)}
           onClick={this.toggleBodyDisplay.bind(this, sBodyRef, sIconRef)}>
      </div>
    </TooltipView>);
  }


  getAttributeIcons () {
    let __props = this.props;
    let oElement = __props.sectionElement;
    let bShowCrossIcon = __props.showCrossIcon;
    let oCouplingIconsView = null;
    let oNotificationIconView = null;
    let oDefaultConflictIcon = null;
    let oRuleViolationIcon = null;
    let oCrossIcon = null;
    let oPropertyTypeIcon = null;
    let oPropertyTasksIcon = null;
    let oDisabledIcon = null;
    let oTranslatableIcon = null;

    let aContentAttributes = oElement.contentAttributes;
    if (!CS.isEmpty(aContentAttributes)) {
      let oAttribute = aContentAttributes[0];
      oCouplingIconsView = this.getCouplingIconsDOM(oAttribute.attributeId, oAttribute);
      let sAttributeId = oAttribute.attributeId == "typeattribute" ? oAttribute.value : oAttribute.attributeId;
      let bIsDisabled = ViewUtils.isAttributeTypeCalculated(oElement.attribute.type) || oElement.isDisabled;
      oDisabledIcon = bIsDisabled ? (
          <TooltipView label={getTranslation().DISABLED_SECTION} placement="bottom">
            <div className="notClickableIcon disabledIcon"></div>
          </TooltipView>) : null;

      oTranslatableIcon = oElement.attribute.isTranslatable ? (
        <TooltipView label={getTranslation().LANGUAGE_DEPENDENT} placement="bottom">
          <div className="notClickableIcon translatableIcon"></div>
        </TooltipView>) : null;


      let bIsForNotification = false;
      if (!CS.isEmpty(oAttribute.notification)) {
        //TODO: Temporary Hack - Shashank : after resolving issue keep only else part.
        if (oAttribute.conflictingValues.length > 1) {
          oDefaultConflictIcon = this.getConflictingValuesIcon(oElement.id, oAttribute, bIsForNotification);
        } else {
          bIsForNotification = true;
          oNotificationIconView = this.getConflictingValuesIcon(oElement.id, oAttribute, bIsForNotification);
        }
      }
      else if (oAttribute.conflictingValues) {
        oDefaultConflictIcon = this.getConflictingValuesIcon(oElement.id, oAttribute, bIsForNotification);
      }

      let oRuleViolationObject = __props.ruleViolation;
      if (oRuleViolationObject) {
        let aRuleViolation = oRuleViolationObject[sAttributeId];
        if (!CS.isEmpty(aRuleViolation)) {
          let sRuleViolationBodyRef = "ruleViolationBody_" + sAttributeId;
          oRuleViolationIcon = this.getRuleViolationIcon(sRuleViolationBodyRef, sAttributeId);
        }
      }
    }
    oPropertyTypeIcon = this.getElementTypeIcon(oElement.type);
    let oReferencedTasks = __props.referencedTasks;
    if (!CS.isEmpty(oReferencedTasks) && !oElement.isForRelationshipObject) {
      oPropertyTasksIcon = this.getPropertyTaskEditButtonView(oElement, "attribute");
    }
    let oClickableCouplingTypeChangeIcon = this.getClickableCouplingTypeChangeIcon(oElement);

    if (bShowCrossIcon) {
      oCrossIcon = this.getCrossIcon(oElement.id, oElement.type);
    }

    let oPropertyInfoIcon = this.getPropertyIconView(__props.referencedAttributes, oElement);

    return (
        <div className="upperIconsWrapper" ref={this.setRef.bind(this, "upperIconsWrapper")}>
          <div className="clickableIconsContainer">
            {oDefaultConflictIcon}
            {oCouplingIconsView}
            {/*{oContextualAttributeEditIcon}*/}
            {oPropertyTasksIcon}
            {oClickableCouplingTypeChangeIcon}
            {oNotificationIconView}
            {oRuleViolationIcon}
            {oCrossIcon}
          </div>
          <div className="notClickableIconsContainer">
            {oTranslatableIcon}
            {oDisabledIcon}
            {oPropertyTypeIcon}
          </div>
          <div className="clickableIconsContainer">
            {oPropertyInfoIcon}
          </div>
        </div>);
  }


  getTagIcons () {
    let __props = this.props;
    let oElement = __props.sectionElement;
    let bShowCrossIcon = __props.showCrossIcon;
    let oCrossIcon = null;
    let oCouplingIconsView = null;
    let oNotificationIconView = null;
    let oDefaultConflictIcon = null;
    let oRuleViolationIcon = null;

    if(!CS.isEmpty(oElement.contentTags)) {
      let oTag = oElement.contentTags[0];
      oCouplingIconsView = this.getCouplingIconsDOM(oTag.tagId, oTag);

      let bIsForNotification = false;
      if (!CS.isEmpty(oTag.notification)) {
        //TODO: Temporary Hack - Shashank : after resolving issue keep only else part.
        if (oTag.conflictingValues.length > 1) {
          oDefaultConflictIcon = this.getConflictingValuesIcon(oElement.id, oTag, bIsForNotification);
        } else {
          bIsForNotification = true;
          oNotificationIconView = this.getConflictingValuesIcon(oElement.id, oTag, bIsForNotification);
        }
      }
      else if (oTag.conflictingValues) {
        oDefaultConflictIcon = this.getConflictingValuesIcon(oElement.id, oTag, bIsForNotification);
      }

      let sTagId = oTag.tagId;
      let oRuleViolationObject = this.props.ruleViolation;
      if (oRuleViolationObject) {
        let aRuleViolation = oRuleViolationObject[sTagId];
        if (!CS.isEmpty(aRuleViolation)) {
          let sRuleViolationBodyRef = "ruleViolationBody_" + sTagId;
          oRuleViolationIcon = this.getRuleViolationIcon(sRuleViolationBodyRef, sTagId);
        }
      }
    }
    let oPropertyTypeIcon = this.getElementTypeIcon(oElement.type);
    let oDisabledIcon = oElement.isDisabled ? (
        <TooltipView label={getTranslation().DISABLED_SECTION} placement="bottom">
          <div className="notClickableIcon disabledIcon"></div>
        </TooltipView>) : null;
    // don't show tasks edit button for elements in relationship objects
    let oReferencedTasks = this.props.referencedTasks;
    let oPropertyTasksIcon = null;
    if (!CS.isEmpty(oReferencedTasks)) {
      oPropertyTasksIcon = oElement.isForRelationshipObject ? null : this.getPropertyTaskEditButtonView(oElement, "tag");
    }
    let oClickableCouplingTypeChangeIcon = this.getClickableCouplingTypeChangeIcon(oElement);
    if (bShowCrossIcon) {
      oCrossIcon = this.getCrossIcon(oElement.id, oElement.type);
    }

    let oPropertyInfoIcon = this.getPropertyIconView(__props.referencedTags, oElement);

    return (
        <div className="upperIconsWrapper" ref={this.setRef.bind(this, "upperIconsWrapper")}>
          <div className="clickableIconsContainer">
            {oDefaultConflictIcon}
            {oCouplingIconsView}
            {oNotificationIconView}
            {oRuleViolationIcon}
            {oPropertyTasksIcon}
            {oClickableCouplingTypeChangeIcon}
            {oCrossIcon}
          </div>
          <div className="notClickableIconsContainer">
            {oDisabledIcon}
            {oPropertyTypeIcon}
          </div>
          <div className="clickableIconsContainer">
            {oPropertyInfoIcon}
          </div>
        </div>);
  }

  getRoleIcons () {
    let __props = this.props;
    let oElement = __props.sectionElement;
    let sRef = oElement.id;
    let oRuleViolationIcon = null;
    let oNotificationIconView = null;
    let oPropertyTasksIcon = null;

    if (!CS.isEmpty(oElement.notification)) {
      let sNotificationBodyRef = "notificationBody_" + sRef;
      oNotificationIconView = this.getNotificationIconView(sNotificationBodyRef, oElement);
    }

    let oRole = oElement.role;
    let oRuleViolationObject = __props.ruleViolation;
    if (oRuleViolationObject) {
      let aRuleViolation = oRuleViolationObject[oRole.id];
      if (!CS.isEmpty(aRuleViolation)) {
        let sRuleViolationBodyRef = "ruleViolationBody_" + oRole.id;
        oRuleViolationIcon = this.getRuleViolationIcon(sRuleViolationBodyRef);
      }
    }

    let oPropertyTypeIcon = this.getElementTypeIcon(oElement.type);
    let oDisabledIcon = oElement.isDisabled ? this.getDisabledIcon() : null;
    // don't show tasks edit button for elements in relationship objects
    let oReferencedTasks = __props.referencedTasks;
    if (!CS.isEmpty(oReferencedTasks)) {
      oPropertyTasksIcon = oElement.isForRelationshipObject ? null : this.getPropertyTaskEditButtonView(oElement, "role");
    }
    let oClickableCouplingTypeChangeIcon = this.getClickableCouplingTypeChangeIcon(oElement);

    let oPropertyInfoIcon = this.getPropertyIconView(__props.referencedRoles, oElement);

    return (
        <div className="upperIconsWrapper">
          <div className="clickableIconsContainer">
            {oNotificationIconView}
            {oRuleViolationIcon}
            {oPropertyTasksIcon}
            {oClickableCouplingTypeChangeIcon}
          </div>
          <div className="notClickableIconsContainer">
            {oDisabledIcon}
            {oPropertyTypeIcon}
          </div>
          <div className="clickableIconsContainer">
            {oPropertyInfoIcon}
          </div>
        </div>);
  }

  getTaxonomyIcons () {
    let __props = this.props;
    let oElement = __props.sectionElement;
    let oDisabledIcon = oElement.isDisabled ? this.getDisabledIcon() : null;
    let oPropertyTypeIcon = this.getElementTypeIcon(oElement.type);
    let oClickableCouplingTypeChangeIcon = this.getClickableCouplingTypeChangeIcon(oElement);
    let oPropertyInfoIcon = this.getPropertyIconView(__props.referencedTaxonomies, oElement);

    return (
        <div className="upperIconsWrapper" ref={this.setRef.bind(this, "upperIconsWrapper")}>
          <div className="clickableIconsContainer">
            {oClickableCouplingTypeChangeIcon}
          </div>
          <div className="notClickableIconsContainer">
            {oDisabledIcon}
            {oPropertyTypeIcon}
          </div>
          <div className="clickableIconsContainer">
            {oPropertyInfoIcon}
          </div>
        </div>);
  }

  getCustomIconView = (sIconKey) => {
    let sImageSrc = ViewUtils.getIconUrl(sIconKey);
    return (<div className="customIcon">
          <ImageFitToContainerView imageSrc={sImageSrc}/>
        </div>
    )
  };

  getHeaderView () {
    let oProps = this.props;
    let oElement = oProps.sectionElement;
    let sElementType = oElement.type;
    let sLabel = oProps.label;
    let oIconsDOM = null;
    let oCustomIconDOM = null;
    let sLabelClass = "headerLabel ";
    if (oElement.isMandatory) {
      sLabelClass += "mandatoryField ";
    }
    if (oElement.hideHeaderView) {
      return null;
    }

    switch (sElementType) {
      case "attribute":
        oIconsDOM = this.getAttributeIcons();
        oCustomIconDOM = this.getCustomIconView(oElement.attribute.iconKey);
        break;
      case"tag":
        oIconsDOM = this.getTagIcons();
        oCustomIconDOM = this.getCustomIconView(oElement.tag.iconKey);
        break;
      case "role":
        oIconsDOM = this.getRoleIcons();
        break;
      case"taxonomy":
        oIconsDOM = this.getTaxonomyIcons();
        break;
    }
    return (
        <div className={"headerView"} ref={this.setRef.bind(this, "headerView")}>
          {oCustomIconDOM}
          <div className={sLabelClass}>
            {sLabel}
          </div>
          {oIconsDOM}
        </div>
    );
  }

  // Body block
  getElementToolTipDOM (sElementLabel, sTooltip) {
    let oStyle = {
      "fontStyle": "italic",
      "textAlign": "center"
    };
    let oToolTipSpan = CS.isEmpty(sTooltip) ? null : (<span>{sTooltip}</span>);

    return (
        <div className="sectionTooltipWrapper">
          {oToolTipSpan}
        </div>);
  }

  getElementView () {
    let __props = this.props;
    let oElement = __props.sectionElement;
    let bIsSelected = __props.isSelected;
    let sMasterEntityId = __props.masterEntityId;
    let sAttributeType = oElement.attribute ? oElement.attribute.type : null;
    let sElementLabel = __props.label;
    let sTooltip = __props.tooltip;
    let oElementStyle = __props.elementStyle;
    let sElementClass = "sectionElementNew ";
    let bIsInherited = oElement.isInherited;

    sElementClass += bIsSelected ? "selected " : "";
    if (oElement.couplingType === "dynamicCoupled") {
      sElementClass += "dynamicallyCoupled ";
    }
    if (oElement.isMandatory) {
      sElementClass += "mandatoryField ";
    }
    if(oElement.type == "taxonomy" && !CS.isEmpty(oElement.elements)) {
      sElementClass += "taxonomyWithElements ";
    }

    let oTooltipDOM = CS.isEmpty(sTooltip) ? null : this.getElementToolTipDOM(sElementLabel, sTooltip);
    let bIsReadOnlyCoupled = oElement.couplingType == CouplingConstants.READ_ONLY_COUPLED;
    return (
        <div className={sElementClass}
             data-id={oElement.id}
             onClick={this.handleSectionElementClicked.bind(this, oElement.type, oElement.id, sMasterEntityId, sAttributeType, bIsInherited, bIsReadOnlyCoupled)}
             style={oElementStyle}>

          {this.getHeaderView()}
          <TooltipView placement="bottom" label={oTooltipDOM || ''}>
            <div className={"contentSectionElement"}>
              {__props.children}
              {this.getElementFooters()}
            </div>
          </TooltipView>
        </div>
    )
  }

  getPropertyIconView(referencedDetails, oElement) {
    if (CS.isEmpty(oElement)) {
      return null;
    }

    let referencedElement = referencedDetails[oElement.id];
    if (CS.isEmpty(referencedElement)) {
      return null;
    }

    let sYes = getTranslation().YES;
    let sNo = getTranslation().NO;

    let aPropertyInfoIconViewData = [
      {
        key: "propertyTooltip",
        label: getTranslation().TOOLTIP,
        value: referencedElement.tooltip ? referencedElement.tooltip : "-",
        labelIconClassName: referencedElement.tooltip ? "propertyTooltip yes" : "propertyTooltip",
      },
      {
        key: GridViewColumnIdConstants.CODE,
        label: getTranslation().PROPERTY_CODE,
        value: referencedElement.code ? referencedElement.code : "-",
        labelIconClassName: referencedElement.code ? "code yes" : "code",
        properties: {
          actionButtonsForValue: [
              {
                id: "copyValue",
                toolTip: getTranslation().COPY_TO_CLIPBOARD_TOOLTIP,
                label: ""
              }
              ]
        }
      },
      {
        key: "isIdentifier",
        label: getTranslation().PRODUCT_IDENTIFIER,
        value: oElement.isIdentifier ? sYes : sNo,
        labelIconClassName: oElement.isIdentifier ? "isIdentifier yes" : "isIdentifier",
      },
      {
        key: GridViewColumnIdConstants.IS_VERSIONABLE,
        label: getTranslation().REVISIONABLE,
        value: oElement.isVersionable ? sYes : sNo,
        labelIconClassName: oElement.isVersionable ? "isVersionable yes" : "isVersionable",
      },
      {
        key: GridViewColumnIdConstants.IS_GRID_EDITABLE,
        label: getTranslation().GRID_EDITABLE,
        value: referencedElement.isGridEditable ? sYes : sNo,
        labelIconClassName: referencedElement.isGridEditable ? "isGridEditable yes" : "isGridEditable",
      },
      {
        key: GridViewColumnIdConstants.IS_SEARCHABLE,
        label: getTranslation().SEARCHABLE,
        value: referencedElement.isSearchable ? sYes : sNo,
        labelIconClassName: referencedElement.isSearchable ? "isSearchable yes" : "isSearchable",
      },
      {
        key: GridViewColumnIdConstants.IS_SORTABLE,
        label: getTranslation().SORTABLE,
        value: referencedElement.isSortable ? sYes : sNo,
        labelIconClassName: referencedElement.isSortable ? "isSortable yes" : "isSortable",
      }
    ];

    let oInfoDialogData = {
      propertyInfoData: aPropertyInfoIconViewData
    };

    return (
        <PropertyInfoButtonView infoDialogData={oInfoDialogData}/>
    );
  }


  render () {
    return (
        <div className={"sectionElementWrapper"}>
          {this.getElementView()}
        </div> )
  }
}


ContentSectionElementViewNew.propTypes = oPropTypes;


export const view = ContentSectionElementViewNew;
export const events = oEvents;
