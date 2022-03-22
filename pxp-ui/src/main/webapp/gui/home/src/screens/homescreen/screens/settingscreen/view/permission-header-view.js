import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ImageSimpleView } from '../../../../../viewlibraries/imagesimpleview/image-simple-view';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import ViewUtils from './utils/view-utils';

const oEvents = {
  PERMISSION_HEADER_VISIBILITY_TOGGLED: "permission_header_visibility_toggled",
};

const oPropTypes = {
  activeTemplate: ReactPropTypes.object,
  currentTab: ReactPropTypes.string,
  context: ReactPropTypes.string
};

// @CS.SafeComponent
class PermissionHeaderView extends React.Component {
  static propTypes = oPropTypes;

  handleHeaderPermissionToggled = (sType, sContext, sId, bIsDisable, oEvent) => {
    if(bIsDisable){
      return;
    }
    EventBus.dispatch(oEvents.PERMISSION_HEADER_VISIBILITY_TOGGLED, sType, sContext, sId);
    oEvent.stopPropagation();
  };

  getPermissionIconsView = (oHeaderData, sType, sContext) => {
    var bVisible = oHeaderData[sContext];
    var oVisibilityView = this.getVisibilityIconView(bVisible, sType, sContext, oHeaderData.id);

    var oEditableView = null;
    var oCanAddView = null;
    var oCanDeleteView = null;

    let oActiveTemplate = this.props.activeTemplate;
    let oTemplatePermissions = oActiveTemplate.templatePermission;
    let bIsDisabled = oTemplatePermissions.isDisabled || !bVisible;
    switch (sContext){
      case "viewName":
        oEditableView = this.getEditableIconView(oHeaderData["canEditName"], sType, "canEditName", oHeaderData.id, bIsDisabled);
        break;

      case "viewAdditionalClasses":
        oCanAddView = this.getAddIconView(oHeaderData["canAddClasses"], sType, "canAddClasses", oHeaderData.id, bIsDisabled);
        oCanDeleteView = this.getDeleteIconView(oHeaderData["canDeleteClasses"], sType, "canDeleteClasses", oHeaderData.id, bIsDisabled);
        break;

      case "viewTaxonomies":
        oCanAddView = this.getAddIconView(oHeaderData["canAddTaxonomy"], sType, "canAddTaxonomy", oHeaderData.id, bIsDisabled);
        oCanDeleteView = this.getDeleteIconView(oHeaderData["canDeleteTaxonomy"], sType, "canDeleteTaxonomy", oHeaderData.id, bIsDisabled);
        break;

      case "viewStatusTags":
        oEditableView = this.getEditableIconView(oHeaderData["canEditStatusTag"], sType, "canEditStatusTag", oHeaderData.id, bIsDisabled);
        break;
    }

    return (
        <div className="permissionIconsWrapper">
          {oVisibilityView}
          {oEditableView}
          {oCanAddView}
          {oCanDeleteView}
        </div>
    )
  };

  getVisibilityIconView = (bVisible, sType, sContext, sId) => {
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
          <div className="imageContainerButton visibilityView"
               onClick={this.handleHeaderPermissionToggled.bind(this, sType, sContext, sId, false)}>
            <div className={sIconClassName}></div>
          </div>
        </TooltipView>
    );
  };

  getEditableIconView = (bEditable, sType, sContext, sId, bIsDisable) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bEditable) {
      sIconClassName += "editable ";
      sVisibilityTooltip = getTranslation().EDITABLE;
    } else {
      sIconClassName += "uneditable ";
      sVisibilityTooltip = getTranslation().UNEDITABLE;
    }

    var sImageIconClass = "";
    if(bIsDisable){
      sIconClassName += "disabled ";
      sImageIconClass += " disabled ";
    }

    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={"imageContainerButton editableView" + sImageIconClass}
               onClick={this.handleHeaderPermissionToggled.bind(this, sType, sContext, sId, bIsDisable)}>
            <div className={sIconClassName}></div>
          </div>
        </TooltipView>
    );
  };

  getAddIconView = (bCanAdd, sType, sContext, sId, bIsDisable) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bCanAdd) {
      sIconClassName += "canAdd ";
      sVisibilityTooltip = getTranslation().CAN_ADD;
    } else {
      sIconClassName += "canNotAdd ";
      sVisibilityTooltip = getTranslation().CAN_NOT_ADD;
    }

    var sImageIconClass = "";
    if(bIsDisable){
      sIconClassName += "disabled ";
      sImageIconClass += " disabled ";
    }
    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={"imageContainerButton editableView" + sImageIconClass}
               onClick={this.handleHeaderPermissionToggled.bind(this, sType, sContext, sId, bIsDisable)}>
            <div className={sIconClassName}></div>
          </div>
        </TooltipView>
    );
  };

  getDeleteIconView = (bCanDelete, sType, sContext, sId, bIsDisable) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bCanDelete) {
      sIconClassName += "canDelete ";
      sVisibilityTooltip = getTranslation().CAN_DELETE;
    } else {
      sIconClassName += "canNotDelete ";
      sVisibilityTooltip = getTranslation().CAN_NOT_REMOVE;
    }

    var sImageIconClass = "";
    if(bIsDisable){
      sIconClassName += "disabled ";
      sImageIconClass += " disabled ";
    }

    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className={"imageContainerButton editableView" + sImageIconClass}
               onClick={this.handleHeaderPermissionToggled.bind(this, sType, sContext, sId, bIsDisable)}>
            <div className={sIconClassName}></div>
          </div>
        </TooltipView>
    );
  };

  getTextDataVisibilityView = (sText, oHeaderData, sType, sContext) => {
    var sViewClassName = "textDataVisibilityView " + sContext;
    var oPermissionIconsView = this.getPermissionIconsView(oHeaderData, sType, sContext);

    return (
        <div className={sViewClassName}>
          <div className="textData">
            {sText}
          </div>
          {oPermissionIconsView}
        </div>
    )
  };

  getImageVisibilityView =(sImageUrl, oHeaderData, sType, sContext, sClassName, sId) => {
    var oActiveTemplate = this.props.activeTemplate;
    var oTemplatePermissions = oActiveTemplate.templatePermission;
    var sImageContainerClassName = "imageContainer ";
    var oImageView = null;
    let bVisible = oHeaderData.viewIcon;
    let bIsDisable = oTemplatePermissions.isDisabled || !bVisible;
    if (sImageUrl) {
      oImageView = <ImageSimpleView classLabel="icon" imageSrc={ViewUtils.getIconUrl(sImageUrl)}/>
    } else {
      sImageContainerClassName += sClassName;
    }
    var oVisibilityView = null;
    var oIconUploadView = null;

      sContext = (sType == "tabPermission") ? "isVisible" : sContext;
      oVisibilityView = this.getVisibilityIconView(bVisible, sType, sContext, sId);

    var oEditableView = (sType != "tabPermission") ? this.getEditableIconView(oHeaderData.canChangeIcon, sType, "canChangeIcon", sId, bIsDisable) : null;

    return (<div className={sImageContainerClassName}>
      {oImageView}
      {oVisibilityView}
      {oEditableView}
      {oIconUploadView}
    </div>)
  };

  getTemplateSettingsView =(oHeaderData) => {
    var sType = "headerPermission";

    return (
        <div className="templateSettings">
          <div className="contentName">
            {this.getTextDataVisibilityView(getTranslation().CONTENT_NAME, oHeaderData, sType, "viewName")}
          </div>
          <div className="natureSetting">
            {this.getTextDataVisibilityView(getTranslation().NATURE_TYPE, oHeaderData, sType, "viewPrimaryType")}
            {this.getTextDataVisibilityView(getTranslation().ADDITIONAL_CLASSES, oHeaderData, sType, "viewAdditionalClasses")}
          </div>
          <div className="taxonomySetting">
            {this.getTextDataVisibilityView(getTranslation().TAXONOMIES, oHeaderData, sType, "viewTaxonomies")}
          </div>
          <div className="statusTagSetting">
            {this.getTextDataVisibilityView(getTranslation().LIFECYCLE_STATUS, oHeaderData, sType, "viewStatusTags")}
          </div>
          {this.getTextDataVisibilityView(getTranslation().CREATED_LAST_MODIFIED_INFO, oHeaderData, sType, "viewCreatedOn")}
        </div>
    );
  };

  render() {

    var oActiveTemplate = this.props.activeTemplate;
    var oTemplatePermissions = oActiveTemplate.templatePermission;
    var oHeaderData = oTemplatePermissions.headerPermission;
    var oTemplateSettingsView = this.getTemplateSettingsView(oHeaderData);
    var sType = "headerPermission";

    return (
        <div className="templateHeader">
          <div className="templateImage">
            {this.getImageVisibilityView(null, oHeaderData, sType, "viewIcon", "viewIcon", "")}
          </div>
          {oTemplateSettingsView}
        </div>
    );
  }
}

export const view = PermissionHeaderView;
export const events = oEvents;
