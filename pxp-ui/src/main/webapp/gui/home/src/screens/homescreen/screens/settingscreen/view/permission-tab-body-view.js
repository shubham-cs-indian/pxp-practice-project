import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import ViewUtils from './utils/view-utils';

const oEvents = {
  PERMISSION_SECTION_EXPAND_COLLAPSED_CLICKED: "template_section_expand_collapsed_clicked",
  PERMISSION_SECTION_STATUS_CHANGED: "template_section_status_changed",
  PERMISSION_ELEMENT_STATUS_CHANGED: "template_element_status_toggled"
};

const oPropTypes = {
  sectionsData: ReactPropTypes.object,
  context: ReactPropTypes.string
};

// @CS.SafeComponent
class PermissionTabBodyView extends React.Component {
  static propTypes = oPropTypes;

  handleSectionExpandCollapseClicked = (sSectionId) => {
    EventBus.dispatch(oEvents.PERMISSION_SECTION_EXPAND_COLLAPSED_CLICKED, sSectionId);
  };

  handleSectionStatusToggled = (sStatus, sSectionId, aElements) => {
    EventBus.dispatch(oEvents.PERMISSION_SECTION_STATUS_CHANGED, sStatus, sSectionId, aElements);
  };

  handleElementStatusToggled = (sStatus, sSectionId, sElementType, sParentSectionId) => {
    EventBus.dispatch(oEvents.PERMISSION_ELEMENT_STATUS_CHANGED, sStatus, sSectionId, sElementType, sParentSectionId);
  };

  getCollapsedIconViewForSection = (bIsCollapsed, sSectionId) => {
    var sClassName = "sectionCollapsedExpanded ";
    sClassName += bIsCollapsed ? "collapsed" : "expanded";
    return (<div className={sClassName} onClick={this.handleSectionExpandCollapseClicked.bind(this, sSectionId)}></div>);
  };

  getVisibilityIconView = (bVisible, sSectionId, aElements) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bVisible) {
      sIconClassName += "visible ";
      sVisibilityTooltip = getTranslation().VISIBLE;
    } else {
      sIconClassName += "visibilityHidden ";
      sVisibilityTooltip = getTranslation().HIDDEN;
    }
    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={sIconClassName} onClick={this.handleSectionStatusToggled.bind(this, "isVisible", sSectionId, aElements)}></div>
        </TooltipView>
    );
  };

  getAddIconView = (bCanAdd, sSectionId, sContext, bIsDisable) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bCanAdd) {
      sIconClassName += "canAdd ";
      sVisibilityTooltip = getTranslation().CAN_ADD;
    } else {
      sIconClassName += "canNotAdd ";
      sVisibilityTooltip = getTranslation().CAN_NOT_ADD;
    }

    if(bIsDisable){
      sIconClassName += "disabled ";
    }
    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={sIconClassName} onClick={this.handleSectionStatusToggled.bind(this, sContext, sSectionId, [])}></div>
        </TooltipView>
    );
  };

  getDeleteIconView = (bCanDelete, bIsDisable, sSectionId, sContext) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bCanDelete) {
      sIconClassName += "canDelete ";
      sVisibilityTooltip = getTranslation().CAN_DELETE;
    } else {
      sIconClassName += "canNotDelete ";
      sVisibilityTooltip = getTranslation().CAN_NOT_REMOVE;
    }

    if(bIsDisable){
      sIconClassName += "disabled ";
    }

    sContext = sContext || "canDelete";
    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={sIconClassName} onClick={this.handleSectionStatusToggled.bind(this, sContext, sSectionId, [])}></div>
        </TooltipView>
    );
  };

  getEditableIconView = (bEditable, sSectionId, aElements, bIsDisable) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bEditable) {
      sIconClassName += "editable ";
      sVisibilityTooltip = getTranslation().EDITABLE;
    } else {
      sIconClassName += "unEditable ";
      sVisibilityTooltip = getTranslation().UNEDITABLE;
    }

    if(bIsDisable){
      sIconClassName += "disabled ";
    }

    var sContext = "canEdit";
    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={sIconClassName} onClick={this.handleSectionStatusToggled.bind(this, sContext, sSectionId, aElements)}></div>
        </TooltipView>
    );
  };

  getIconsByKeys = (oPermission, aButtonsToShow, sSectionId, bIsDisabled) => {
    let aIconViews = [];
    CS.forEach(aButtonsToShow, (sKey) => {
      switch (sKey) {
        case "canDelete":
          aIconViews.push(this.getDeleteIconView(oPermission[sKey], bIsDisabled, sSectionId));
          break;
        case "canAdd":
          aIconViews.push(this.getAddIconView(oPermission[sKey], sSectionId, sKey, bIsDisabled));
          break;
        case "isVisible":
          aIconViews.push(this.getVisibilityIconView(oPermission[sKey], sSectionId));
          break;
        case "canEdit":
          aIconViews.push(this.getEditableIconView(oPermission[sKey], sSectionId, [], bIsDisabled));
          break;
      }
    });
    return aIconViews;
  };

  getElementVisibilityIconView = (bVisible, sSectionId, sElementType, sParentSectionId) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bVisible) {
      sIconClassName += "visible ";
      sVisibilityTooltip = getTranslation().VISIBLE;
    } else {
      sIconClassName += "visibilityHidden ";
      sVisibilityTooltip = getTranslation().HIDDEN;
    }
    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={sIconClassName} onClick={this.handleElementStatusToggled.bind(this, "isVisible", sSectionId, sElementType, sParentSectionId)}></div>
        </TooltipView>
    );
  };

  getElementEditableIconView = (bEditable, sSectionId, sElementType, sParentSectionId, bIsDisable) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bEditable) {
      sIconClassName += "editable ";
      sVisibilityTooltip = getTranslation().EDITABLE;
    } else {
      sIconClassName += "unEditable ";
      sVisibilityTooltip = getTranslation().UNEDITABLE;
    }

    if(bIsDisable){
      sIconClassName += "disabled ";
    }
    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={sIconClassName} onClick={this.handleElementStatusToggled.bind(this, "canEdit", sSectionId, sElementType, sParentSectionId)}></div>
        </TooltipView>
    );
  };

  getElementTypeIcon = (sElementType) => {
    if (CS.isEmpty(sElementType)) {
      return null;
    } else {
      var sLabel = "";
      if (sElementType == "attribute") {
        sLabel = getTranslation().ATTRIBUTE;
      } else if (sElementType == "tag") {
        sLabel = getTranslation().TAG;
      } else if (sElementType == "role") {
        sLabel = getTranslation().ROLE;
      }
      var sClassName = "elementTypeIcon " + sElementType;
      return (<TooltipView placement="bottom" label={sLabel}>
        <div className={sClassName}>
        </div>
      </TooltipView>);
    }
  };

  getElementView = (oSection) => {
    var oSectionsData = this.props.sectionsData;
    var oPermission = oSectionsData.permissions || {};
    var oElementPermission = oPermission.propertyPermission || {};

    var aRowDOM = [];
    var iElWidth = 100;
    let _this = this;
    CS.forEach(oSection.elements, function (oElement) {
      let aElementDom = [];
        var oElDOM = null;
        var style = {
          width: iElWidth + "%",
          position: "relative"
        };

        if (oElement) {
          var oData = oElement.data || {};
          var oElPermission = oElementPermission[oData.id] || {};
          var bCanEdit = oElPermission.canEdit;
          var bIsVisible = oElPermission.isVisible;
          var canEditIcon = bIsVisible ? _this.getElementEditableIconView(bCanEdit, oData.id, oElement.type, oSection.id, oPermission.isDisabled) : null;
          let sLabel = CS.getLabelOrCode(oData);
          oElDOM = (
              <div className="templateSectionElement" style={style}>
                {_this.getElementTypeIcon(oElement.type)}
                <TooltipView label={sLabel}>
                  <div className="elementLabel">{sLabel}</div>
                </TooltipView>
                <div className="iconsWrapper">
                  {_this.getElementVisibilityIconView(bIsVisible, oData.id, oElement.type, oSection.id)}
                  {canEditIcon}
                </div>
              </div>);
        }

        if(oElDOM) {
          aElementDom.push(oElDOM);
        }
      aRowDOM.push(<div className="templateSectionElementsWrapper">{aElementDom}</div>);
    });

    return (<div className="templateSectionRowsWrapper">{aRowDOM}</div>)
  };

  getPermissionSectionRows = () => {
    var _this = this;
    var oSectionsData = this.props.sectionsData;
    var oPermission = oSectionsData.permissions;
    var oPropertyCollectionPermission = oPermission.propertyCollectionPermission;
    var oRelationshipPermission = oPermission.relationshipPermission;
    var oTabPermission = oPermission.tabPermission;
    var aSectionsMap = oSectionsData.sectionsMap;
    var aSections = oSectionsData.sections;
    var aSectionMasterList = oSectionsData.sectionMasterList;
    let bIsDisabled = oPermission.isDisabled;

    return CS.map(aSections, function (sSectionId, iIndex) {
      var oSectionMap = aSectionsMap[sSectionId] || {};
      var bIsCollapsed = oSectionMap.isCollapsed;
      var oSection = CS.find(aSectionMasterList, {id: sSectionId}) || {};
      var aElements = oSection.elements;

      var oCollapsedIconView = null;
      var oPermissionIconWrapper = null;
      var oSectionIconView = null;
      if(oPropertyCollectionPermission[sSectionId]) { /** For Home Tab*/
        var oPropertyCollection = oPropertyCollectionPermission[sSectionId];
        let oEditableIconsView = (oPropertyCollection.isVisible || bIsDisabled)
                                 ? _this.getEditableIconView(oPropertyCollection.canEdit, sSectionId, aElements, bIsDisabled) : null;
        oPermissionIconWrapper = (<div className="actionButtons">
          {_this.getVisibilityIconView(oPropertyCollection.isVisible, sSectionId, aElements)}
          {oEditableIconsView}
        </div>);

        oCollapsedIconView = _this.getCollapsedIconViewForSection(bIsCollapsed, sSectionId);

        oSectionIconView = oSection.icon ?
            (<div className="sectionIcon"><img src={ViewUtils.getIconUrl(oSection.icon)}/></div>) :
            (<div className="sectionIcon noIcon"></div>);

      } else if(oRelationshipPermission[sSectionId]){ /** For Relationship Tab*/
        var oRelationshipSectionPermission = oRelationshipPermission[sSectionId];
        let aButtonsToShow = !CS.isEmpty(oRelationshipSectionPermission.buttonsToShow) ? oRelationshipSectionPermission.buttonsToShow : ["canDelete", "canAdd", "isVisible"];
        oPermissionIconWrapper = (<div className="actionButtons">
          {_this.getIconsByKeys(oRelationshipSectionPermission, aButtonsToShow, sSectionId, bIsDisabled)}
        </div>);
      } else { /** For All Other Tabs*/
        var oCurrentTabPermission = oTabPermission[sSectionId];
        oPermissionIconWrapper = (<div className="actionButtons">
          {_this.getDeleteIconView(oCurrentTabPermission.canDelete, bIsDisabled, sSectionId)}
          {_this.getAddIconView(oCurrentTabPermission.canCreate, sSectionId, 'canCreate', bIsDisabled)}
          {_this.getEditableIconView(oCurrentTabPermission.canEdit, sSectionId, [], bIsDisabled)}
        </div>);
      }

      var oElementView = (!CS.isEmpty(aElements) && !bIsCollapsed) ? _this.getElementView(oSection) : null;
      var sSectionLabel = CS.getLabelOrCode(oSection);
      return (<div className="sectionPermission" key={iIndex}>
        <div className="sectionHeader">
          {oCollapsedIconView}
          {oSectionIconView}
          <div className="sectionLabel">{sSectionLabel}</div>
          {oPermissionIconWrapper}
        </div>
        <div className="sectionBody">
          {oElementView}
        </div>
      </div>);
    });

  };

  render() {
    var oSectionsData = this.props.sectionsData;
    var sSectionName = oSectionsData.sectionName;
    var oTabBodyView = null;
    if (sSectionName) {
      var aSectionViews = this.getPermissionSectionRows();
                oTabBodyView = (
          <div className="tabBody">
            <div className="sections">
              {aSectionViews}
            </div>
          </div>
      );
    }
    return oTabBodyView;
  }
}

export const view = PermissionTabBodyView;
export const events = oEvents;
