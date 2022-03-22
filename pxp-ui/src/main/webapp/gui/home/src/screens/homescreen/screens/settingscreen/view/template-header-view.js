import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ImageSimpleView } from '../../../../../viewlibraries/imagesimpleview/image-simple-view';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import TemplateTabConstants from '../../../../../commonmodule/tack/constants';
import ViewUtils from './utils/view-utils';

const oEvents = {
  TEMPLATE_HEADER_VISIBILITY_TOGGLED: "template_header_visibility_toggled",
  TEMPLATE_HEADER_TAB_CLICKED: "template_header_tab_clicked",
  TEMPLATE_HEADER_TAB_ICON_CHANGED: "template_header_tab_icon_changed"
};

const oPropTypes = {
  activeTemplate: ReactPropTypes.object,
  currentTab: ReactPropTypes.string,
  context: ReactPropTypes.string
};

// @CS.SafeComponent
class TemplateHeaderView extends React.Component {
  constructor (oProps) {
    super(oProps);

    this.tabIconUpload = React.createRef();
  }
  static propTypes = oPropTypes;

  handleTabClicked = (sTabType) => {
    if (sTabType != this.props.currentTab) {
      EventBus.dispatch(oEvents.TEMPLATE_HEADER_TAB_CLICKED, sTabType);
    }
  };

  handleVisibilityToggled = (sType, sContext, oEvent) => {
    EventBus.dispatch(oEvents.TEMPLATE_HEADER_VISIBILITY_TOGGLED, sType, sContext);
    oEvent.stopPropagation();
  };

  getVisibilityIconView = (bVisible, sType, sContext) => {
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
               onClick={this.handleVisibilityToggled.bind(this, sType, sContext)}>
            <div className={sIconClassName}></div>
          </div>
        </TooltipView>
    );
  };

  getEditableIconView = (bEditable, sType, sContext) => {
    var sIconClassName = "visibilityIcon ";
    var sVisibilityTooltip;
    if (bEditable) {
      sIconClassName += "editable ";
      sVisibilityTooltip = getTranslation().EDITABLE;
    } else {
      sIconClassName += "uneditable ";
      sVisibilityTooltip = getTranslation().UNEDITABLE;
    }
    return (
        <TooltipView label={sVisibilityTooltip}>
          <div className="imageContainerButton editableView"
               onClick={this.handleVisibilityToggled.bind(this, sType, sContext)}>
            <div className={sIconClassName}></div>
          </div>
        </TooltipView>
    );
  };

  getTextDataVisibilityView = (sText, bVisible, sType, sContext) => {
    var sViewClassName = "textDataVisibilityView " + sContext;
    var oVisibilityView = this.getVisibilityIconView(bVisible, sType, sContext);
    return (
        <div className={sViewClassName}>
          <div className="textData">
            {sText}
          </div>
          {oVisibilityView}
        </div>
    )
  };

  handleIconUploadClicked = (sContext, oEvent) => {
    this.tabIconToChange = sContext;
    this.tabIconUpload.current.click();
    oEvent.stopPropagation();
  };

  getImageUploadIconView = (sContext) => {
    return (
        <TooltipView label={getTranslation().UPLOAD_ICON}>
          <div className="imageContainerButton iconUploadView"
               onClick={this.handleIconUploadClicked.bind(this, sContext)}>
            <div className="visibilityIcon uploadIcon"></div>
          </div>
        </TooltipView>
    );
  };

  getImageVisibilityView = (sImageUrl, bVisible, sType, sContext, sClassName) => {
    var sImageContainerClassName = "imageContainer ";
    var oImageView = null;
    if (sImageUrl) {
      oImageView = <ImageSimpleView classLabel="icon" imageSrc={ViewUtils.getIconUrl(sImageUrl)}/>
    } else {
      sImageContainerClassName += sClassName;
    }
    var oVisibilityView = null;
    var oEditableView = null;
    var oIconUploadView = null;
      oVisibilityView = this.getVisibilityIconView(bVisible, sType, sContext);
    if (this.props.context == "permission") {
      oEditableView = this.getEditableIconView(bVisible, sType, sContext);
    }
    if (sType == "tabs") {
      oIconUploadView = this.getImageUploadIconView(sContext);
    }
    return (<div className={sImageContainerClassName}>
      {oImageView}
      {oVisibilityView}
      {oEditableView}
      {oIconUploadView}
    </div>)
  };

  handleTabIconChanged = (oEvent) => {
    var aFiles = oEvent.target.files;
    EventBus.dispatch(oEvents.TEMPLATE_HEADER_TAB_ICON_CHANGED, this.tabIconToChange, aFiles);
    this.tabIconUpload = "";
    oEvent.target.value = "";
  };

  getTabs = () => {
    var _this = this;
    var oActiveTemplate = this.props.activeTemplate;
    var sCurrentTab = this.props.currentTab;
    var bIsContextualTemplate = oActiveTemplate.type == "contextualTemplate";

    //TODO:Review - Need to change inversion logic [Refactoring Needed]
    var oTemplateTabConstantsInverted = CS.invert(TemplateTabConstants);
    var oInputStyle = {
      visibility: "hidden",
      height: "0",
      width: "0"
    };

    var aTabViews = [];
    CS.forEach(oActiveTemplate.tabs, function (oTab) {
      if(bIsContextualTemplate){ //show only Home Tab
        oInputStyle.display = "none";
        return;
      }
      var sTabClassName = "templateTab ";
      if (oTab.baseType == sCurrentTab) {
        sTabClassName += " selected";
      }
      var sTabType = ViewUtils.getSimpleNameFromTabBaseType(oTab.baseType);
      aTabViews.push(
          <TooltipView label={getTranslation()[oTemplateTabConstantsInverted[oTab.baseType]]} placement="bottom">
            <div className={sTabClassName} onClick={_this.handleTabClicked.bind(_this, oTab.baseType)}>
              {_this.getImageVisibilityView(oTab.icon, oTab.isVisible, "tabs", oTab.baseType, sTabType)}
            </div>
          </TooltipView>
      );

    });
    return (
        <div className="templateTabsContainer">
          {aTabViews}
          <input style={oInputStyle}
                 ref={this.tabIconUpload}
                 onChange={this.handleTabIconChanged}
                 type="file"
                 accept="image/!*"/>
        </div>
    );
  };

  getTemplateSettingsView = () => {
    var oActiveTemplate = this.props.activeTemplate;
    var oHeaderData = oActiveTemplate.headerVisibility;
    return (
        <div className="templateSettings">
          <div className="contentName">{getTranslation().CONTENT_NAME}</div>
          <div className="natureSetting">
            {this.getTextDataVisibilityView(getTranslation().NATURE_TYPE, oHeaderData.viewPrimaryType, "headerVisibility", "viewPrimaryType")}
            {this.getTextDataVisibilityView(getTranslation().ADDITIONAL_CLASSES, oHeaderData.viewAdditionalClasses, "headerVisibility", "viewAdditionalClasses")}
          </div>
          <div className="taxonomySetting">
            {this.getTextDataVisibilityView(getTranslation().TAXONOMIES, oHeaderData.viewTaxonomies, "headerVisibility", "viewTaxonomies")}
          </div>
          <div className="statusTagSetting">
            {this.getTextDataVisibilityView(getTranslation().LIFECYCLE_STATUS, oHeaderData.viewStatusTags, "headerVisibility", "viewStatusTags")}
          </div>
          {this.getTextDataVisibilityView(getTranslation().CREATED_LAST_MODIFIED_INFO, oHeaderData.viewCreatedOn, "headerVisibility", "viewCreatedOn")}
        </div>
    );
  };

  render() {

    var oActiveTemplate = this.props.activeTemplate;
    var oHeaderData = oActiveTemplate.headerVisibility;
    var oTemplateSettingsView = this.getTemplateSettingsView();
    var oTabs = this.getTabs();

    return (
        <div className="templateHeader">
          <div className="templateImage">
            {this.getImageVisibilityView(null, oHeaderData.viewIcon, "headerVisibility", "viewIcon", "viewIcon")}
          </div>
          {oTemplateSettingsView}
          {oTabs}
        </div>
    );
  }
}

export const view = TemplateHeaderView;
export const events = oEvents;
