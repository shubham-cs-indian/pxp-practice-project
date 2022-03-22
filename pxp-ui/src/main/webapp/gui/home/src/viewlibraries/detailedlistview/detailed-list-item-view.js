import CS from '../../libraries/cs';
import CurrentZoomValueContext from '../../commonmodule/HOC/current-zoom-value-context';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../tooltipview/tooltip-view';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import { view as EntityTagView } from '../entitytagsummaryview/entity-tags-summary-view.js';
import { view as ContentImageView } from '../contentimageview/content-image-view';
import ContentImageViewModel from '../contentimageview/model/content-image-view-model';
import { view as ListBasicTemplateViewZ1 } from './templates/list-basic-template-view-zoom-1';
import { view as ListBasicTemplateViewZ2 } from './templates/list-basic-template-view-zoom-2';
import { view as ListBasicTemplateViewZ3 } from './templates/list-basic-template-view-zoom-3';
import { view as ListBasicTemplateViewZ4 } from './templates/list-basic-template-view-zoom-4';
import ThumbnailModel from './model/detailed-list-item-model';
import ViewUtils from '../utils/view-library-utils';
import AttributeUtils from '../../commonmodule/util/attribute-utils';
import ThumbnailModeConstants from '../../commonmodule/tack/thumbnail-mode-constants';
import ViewLibrariesUtils from '../utils/view-library-utils';

// Not in use
const oEvents = {
  THUMB_CHECKBOX_CLICKED: "thumb_checkbox_clicked",
  THUMB_DELETE_CLICKED: "thumb_delete_clicked",
  THUMB_SINGLE_CLICKED: "thumb_single_clicked"
};

const oPropTypes = {
  model: ReactPropTypes.instanceOf(ThumbnailModel).isRequired,
  filterContext: ReactPropTypes.object.isRequired,
  currentZoomValue: ReactPropTypes.number
};
/**
 * @class DetailedListItemView - use for display list view in detailList.
 * @memberOf Views
 * @property {custom} model - pass model name.
 */

// @CurrentZoomValueContext
// @CS.SafeComponent
class DetailedListItemView extends React.Component {

  constructor(props) {
    super(props);

    this.detailedListItemImage = React.createRef();
  }

  handleImageOnLoad =(oEvent)=>{
    var iWidth = oEvent.target.width;
    var iHeight = oEvent.target.height;

    if(iHeight > iWidth){
      this.detailedListItemImage.current.classList.remove('landscape');
      this.detailedListItemImage.current.classList.add('portrait');
    } else {
      this.detailedListItemImage.current.classList.add('landscape');
      this.detailedListItemImage.current.classList.remove('portrait');
    }
  }

  _handleThumbnailCheckboxClicked = (oEvent) => {
    oEvent.stopPropagation();
    EventBus.dispatch(oEvents.THUMB_CHECKBOX_CLICKED, this, this.props.model);
  }

  _handleSingleClicked = (event) => {
    EventBus.dispatch(oEvents.THUMB_SINGLE_CLICKED, event, this.props.model);
  }

  _handleDeleteThumbClicked = () => {
    EventBus.dispatch(oEvents.THUMB_DELETE_CLICKED, this, this.props.model);
  }

  getToolbarView =()=> {
    var oModel = this.props.model;

    var bDisableDelete = oModel.properties["disableDelete"];
    var oDeleteIconDOM = bDisableDelete ? null : (
        <TooltipView placement="bottom" label={getTranslation().DELETE}>
          <div className="detailedListItemTool tooItemDelete"
               onClick={this._handleDeleteThumbClicked.bind(this)}></div>
        </TooltipView>
    );

    var bDisableView = oModel.properties["disableDelete"];
    var oViewIconDom = bDisableView ? null : (
        <TooltipView placement="bottom" label={getTranslation().VIEW_CONTENT}>
          <div className="detailedListItemTool tooItemView"
               onClick={this._handleSingleClicked.bind(this)}></div>
        </TooltipView>
    );


    return (<div className="detailedListItemToolbarContainer">
      {oViewIconDom}
      {oDeleteIconDOM}
    </div>);
  }

  getMessageCounterView =()=> {
    var oModel = this.props.model;
    var oEntity = oModel.properties['entity'];
    var oMessages = oEntity.messages;
    if(CS.isEmpty(oMessages)){
      return null;
    }
    var iCounter = 0;


    var iNotificationCounter = oMessages.notificationsCounter || 0;
    var oNotificationDetailCounterdom = (
        <div className="detailRuleViolationCounterContainer">
          <div className="colorViolationWrapper">
            <div className="violationIcon notificationIcon"></div>
            <div className="violationCount">{iNotificationCounter}</div>
          </div>
        </div>
    );
    var oNotificationCounterContainer = iNotificationCounter <= 0 ? null : (
        <TooltipView placement="bottom"
                        key={iCounter++ + "_overlay"}
                        label={<div className="counterToolTip">{oNotificationDetailCounterdom}</div>}>
          <div className="thumbCounterIconContainer notification">
            <div className="thumbCounterIcon notification"></div>
          </div>
        </TooltipView>);


    // Rule Violation
    var oViolationDetailCounterdom = (
        <div className="detailRuleViolationCounterContainer">
          <div className="colorViolationWrapper">
            <div className="violationIcon redIcon"></div>
            <div className="violationCount">{oMessages.redCount}</div>
          </div>
          <div className="colorViolationWrapper">
            <div className="violationIcon orangeIcon"></div>
            <div className="violationCount">{oMessages.orangeCount}</div>
          </div>
          <div className="colorViolationWrapper">
            <div className="violationIcon yellowIcon"></div>
            <div className="violationCount">{oMessages.yellowCount}</div>
          </div>
        </div>
    );


    var sThumbIconClassName = "";
    var bIsViolated = false;
    if(oMessages.redCount > 0){
      sThumbIconClassName = "redIcon";
      bIsViolated = true;
    }else if(oMessages.orangeCount > 0){
      sThumbIconClassName = "orangeIcon";
      bIsViolated = true;
    }else if(oMessages.yellowCount > 0){
      sThumbIconClassName = "yellowIcon";
      bIsViolated = true;
    }

    var oViolationCounterContainer = !bIsViolated ? null : (
        <TooltipView placement="left"
                        key={iCounter++ + "_overlay"}
                        label={<div className="counterToolTip">{oViolationDetailCounterdom}</div>}>
          <div className="thumbCounterIconContainer violation">
            <div className={"thumbCounterIcon violation " + sThumbIconClassName}></div>
          </div>
        </TooltipView>
    );

    return (
        <div className="thumbMessageCounterContainer">
          {oNotificationCounterContainer}
          {oViolationCounterContainer}
        </div>
    );
  }

/*
  getMiniIconView =()=> {

    var oModel = this.props.model;
    var oEntity = oModel.properties['entity'];
    var oView = null;

    var aIcons = [];
    if(oEntity.variantOf != "-1"){
      aIcons.push("variantIcon");
    }
    if(oEntity.branchOf != "-1"){
      aIcons.push("branchIcon");
    }

    if(aIcons && aIcons.length){
      var aIconDom = [];

      CS.forEach(aIcons, function(sIcon){
        var sClassName = "thumbnailMiniIcon " + sIcon;
        aIconDom.push(<div key={sIcon} className={sClassName}></div>);
      });

      oView = (<div className="thumbnailMiniIconContainer">{aIconDom}</div>);
    }

    return oView;
  }
*/

  getSearchedDataListForAttribute =(oHit)=> {
    var aDataList = [];
    CS.forEach(oHit.values, function (oHitValue) {
      var aSearchedList = ViewUtils.extractInnerTextFromHtml(oHitValue.label);//Extract "<em>" tag innerText array
      CS.assign(aDataList, aSearchedList);
    });
    return aDataList;
  }

  getSearchedDataListForTags =(oHit)=> {
    var aDataList = [];
    CS.forEach(oHit.values, function (oHitValue) {
      aDataList.push(CS.getLabelOrCode(oHitValue));
    });
    return aDataList;
  }

  getSummaryView =(oHit, aSearchedList, bIsLastAttribute)=> {
    /*var sSearchedText = aSearchedList.join(', ');*/
    /*var sSearchedIn = oHit.label;*/
    return (<div className="searchInfo">
      {CS.map(aSearchedList, function (sSearchedText) {
        return (
            <TooltipView label={sSearchedText}>
              <div className="searchTextWrapper"><span className="searchedText">{sSearchedText}</span></div>
            </TooltipView>)
      })}
    </div>);
  }

  getSearchHitsInfo =(aHits)=> {
    var oSearchHitInfo = {};
    oSearchHitInfo.tagHitsInfo = [];
    oSearchHitInfo.attributeHitsInfo = [];
    oSearchHitInfo.isTagExists = false;
    oSearchHitInfo.isAttributeExists = false;
    var aSearchedDataList = [];
    var that = this;
    CS.forEach(aHits, function (oHit, iIndex) {
      var bIsLastAttribute = aHits.length - 1 > iIndex;
      if(ViewUtils.isTag(oHit.type)) {
        oSearchHitInfo.isTagExists = true;
        aSearchedDataList = that.getSearchedDataListForTags(oHit);
        oSearchHitInfo.tagHitsInfo.push(that.getSummaryView(oHit, aSearchedDataList, bIsLastAttribute));
      } else {
        oSearchHitInfo.isAttributeExists = true;
        var aRawSearchedDataList = that.getSearchedDataListForAttribute(oHit);
        CS.forEach(aRawSearchedDataList, function (sRawData) {
          aSearchedDataList.push(AttributeUtils.getLabelByAttributeType(oHit.type, sRawData, oHit.defaultUnit, oHit.precision));
        });
        oSearchHitInfo.attributeHitsInfo.push(that.getSummaryView(oHit, aSearchedDataList, bIsLastAttribute));
      }
    });

    return oSearchHitInfo;
  }

  getView =()=> {

    var oModel = this.props.model;
    var sId = oModel.id;
    var sLabel = CS.getLabelOrCode(oModel);
    var sType = oModel.className;
    var sImageSrc = oModel.imageSrc;
    var aTags = oModel.tags;
    var sCustomClassName = oModel.className;
    var oEntity = oModel.properties['entity'];
    var oTagView = CS.isEmpty(oEntity.hits) ? <EntityTagView tags={aTags}/> : null;
    var sClassIcon = oModel.properties['classIcon'];
    var oClassIconView = null;
    var sLastModifiedBy = oModel.properties['lastModifiedBy'];
    var oLastModifiedOnDate = new Date(oEntity.lastModified);
    var sLastModifiedOn = oLastModifiedOnDate ? ViewLibrariesUtils.getDateAttributeInTimeFormat(oLastModifiedOnDate) : "";
    var sCheckboxContainerClassName = "detailedListItemCheckbox ";
    var oToolbarView = this.getToolbarView();
    var oModifiedDetails = (<span>
      {getTranslation().LAST_MODIFIED_BY} &nbsp;
      <span className="detailedListItemDark">{sLastModifiedBy}</span>
      &nbsp; {getTranslation().ON} &nbsp;
      <span className="detailedListItemDark">{sLastModifiedOn}</span>
    </span>);

    var oHitsWrapper = null;
    if(!(CS.isEmpty(oEntity) || CS.isEmpty(oEntity.hits))) {
      var oHitsInfo = this.getSearchHitsInfo(oEntity.hits);
      var aHitsWrapper = [];
      if(oHitsInfo.isAttributeExists) {
        CS.forEach(oHitsInfo.attributeHitsInfo, function (oHit) {
          aHitsWrapper.push(oHit);
        });
      }

      if(oHitsInfo.isTagExists) {
        CS.forEach(oHitsInfo.tagHitsInfo, function (oHit) {
          aHitsWrapper.push(oHit);
        });
      }
      oHitsWrapper = (<div className="hitsContainer">{aHitsWrapper}</div>);
    }

    if(oModel.properties['isSelected']) {
      sCheckboxContainerClassName += "checked";
    }

    var sContainerClassName = "detailedListItem " + (sCustomClassName || "");
    // var sImageContainerClassName = "detailedListItemImageContainer " + (CS.isEmpty(sImageSrc) ? "noImage " : "");

    var sContentIconClassName = oModel.properties['className'];
    var sTypeIconClassName = CS.isEmpty(sContentIconClassName) ? "detailedListItemTypeIcon" : "detailedListItemTypeIcon "+ sContentIconClassName;

    if(sClassIcon){
      sClassIcon = ViewUtils.getIconUrl(sClassIcon);
      let sPreviewImageSrc = oModel.properties['previewImageSrc'] ? oModel.properties['previewImageSrc']  : "";
      oClassIconView = <ImageFitToContainerView imageSrc={sClassIcon} previewImageSrc={sPreviewImageSrc}/>;
      sTypeIconClassName += " imageAvailable ";
    }

    var oMessageCounterView = this.getMessageCounterView();
    var oMiniIconView = null;//this.getMiniIconView();

    var oImageStyle = {};
    var oProperties = {};
    var sEntityType = oModel.properties['entityType'];
    var oContentImageViewModel = new ContentImageViewModel("", sImageSrc, sEntityType, "detailedListItemImage", oImageStyle, oProperties);

    return (<div className={sContainerClassName}>
      <div className={sCheckboxContainerClassName} onClick={this._handleThumbnailCheckboxClicked.bind(this)}></div>
      <div className="detailedListItemImageContainer">
        <ContentImageView model={oContentImageViewModel}/>
      </div>
      <div className="detailedListItemInfoContainer">
        <div className={sTypeIconClassName}>{oClassIconView}</div>
        <div className="detailedListItemLabel" title={sLabel}>{sLabel}</div>
        <div className="detailedListItemType">{sType}</div>
        <div className="detailedListItemTags">
          {oHitsWrapper}
          {oTagView}
        </div>
        <div className="detailedListItemModifiedDetails">{oModifiedDetails}</div>
      </div>
      {oToolbarView}
      <div className="thumbUpperIconContainer">
        {oMessageCounterView}
        {oMiniIconView}
      </div>
    </div>);
  }

  getViewAccordingToMode =(sMode, sTheme)=> {
    var oModel = this.props.model;
    var sId = oModel.id;
    var sName = CS.getLabelOrCode(oModel);
    var sClassName = oModel.className;
    var sImageSrc = oModel.imageSrc;
    var aTags = oModel.tags;
    var oEntity = oModel.properties['entity'];
    var aHits = oEntity.hits;
    var iPropsZoom = this.props.currentZoom;
    var iCurrentZoomValue = iPropsZoom ? iPropsZoom : this.props.currentZoomValue;
    let sType = oModel.type;

    switch (iCurrentZoomValue) {

      case 1:
        switch (sMode) {

          case ThumbnailModeConstants.BASIC:
            return (<ListBasicTemplateViewZ1
                id={sId}
                name={sName}
                className={sClassName}
                imageSrc={sImageSrc}
                tags={aTags}
                theme={sTheme}
                listItemModel={oModel}
            />);

          case ThumbnailModeConstants.XRAY:
            return (null);

          default:
            return null;
        }

      case 2:
        switch (sMode) {

          case ThumbnailModeConstants.BASIC:
            return (<ListBasicTemplateViewZ2
                id={sId}
                name={sName}
                className={sClassName}
                imageSrc={sImageSrc}
                tags={aTags}
                theme={sTheme}
                listItemModel={oModel}
                type={sType}
                filterContext={this.props.filterContext}
            />);

          case ThumbnailModeConstants.XRAY:
            return (null);

          default:
            return null;
        }

      case 3:
        switch (sMode) {

          case ThumbnailModeConstants.BASIC:
            return (<ListBasicTemplateViewZ3
                id={sId}
                name={sName}
                className={sClassName}
                imageSrc={sImageSrc}
                tags={aTags}
                theme={sTheme}
                listItemModel={oModel}
                type={sType}
            />);

          case ThumbnailModeConstants.XRAY:
            return (null);

          default:
            return null;
        }

      case 4:
        switch (sMode) {

          case ThumbnailModeConstants.BASIC:
            return (<ListBasicTemplateViewZ4
                id={sId}
                name={sName}
                className={sClassName}
                imageSrc={sImageSrc}
                tags={aTags}
                theme={sTheme}
                listItemModel={oModel}
                type={sType}
            />);

          case ThumbnailModeConstants.XRAY:
            return (null);

          default:
            return null;
        }
    }

  }

  render(){
    var sClassName = "detailedListItemContainer ";

    var sMode = "basic"; //TODO: get mode from props
    var sTheme = "PIM"; //TODO: get theme from props eventually

    var iPropsZoom = this.props.currentZoom;
    var iCurrentZoomValue = iPropsZoom ? iPropsZoom : this.props.currentZoomValue;
    sClassName += ("zoom_" + iCurrentZoomValue);
    var oModel = this.props.model;
    var sRelevanceValue = oModel.properties['relevanceValue'];
    var sThumbnailRelevanceClassName = ViewLibrariesUtils.getClassNameAsPerRelevanceValue(sRelevanceValue);
    sClassName += (" " + sThumbnailRelevanceClassName + " ");
    // var oModel = this.props.model;
    // if(!oModel.properties['disableView')) {
    //   sClassName += " isOpenable ";
    // }

    var oView = this.getViewAccordingToMode(sMode, sTheme);

    return (
        <div className={sClassName+" "+sThumbnailRelevanceClassName}>
          {/*{this.getView()}*/}
          {oView}
        </div>
    );
  }
}

DetailedListItemView.propTypes = oPropTypes;



export const view = CurrentZoomValueContext(DetailedListItemView);
export const events = oEvents;
