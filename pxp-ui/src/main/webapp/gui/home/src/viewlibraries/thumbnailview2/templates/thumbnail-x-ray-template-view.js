import CS from '../../../libraries/cs';
import MasterUserListContext from '../../../commonmodule/HOC/master-user-list-context';
import React from 'react';
import ReactDom from 'react-dom';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import ThumbnailItemModel from './../model/thumbnail-model';
import { view as ContentConcatenatedAttributeView } from './../../../viewlibraries/concatenatedattributeview/content-concatenated-attribute-view';
import ContentConcatenatedAttributeViewModel from './../../../viewlibraries/concatenatedattributeview/model/content-concatenated-attribute-view-model';
import ImageViewModel from './../../contentimageview/model/content-image-view-model';
import { view as ImageView } from './../../contentimageview/content-image-view';
import TooltipView from './../../tooltipview/tooltip-view';
import { view as ImageFitToContainerView } from '../../imagefittocontainerview/image-fit-to-container-view';
import { view as GridYesNoPropertyView } from './../../../viewlibraries/gridview/grid-yes-no-property-view';
import TagTypeConstants from '../../../commonmodule/tack/tag-type-constants';
import { view as ThumbnailActionItemView } from './thumbnail-action-item-view';
import { view as ThumbnailInformationIndicatorIconsView } from '../../thumbnailview2/templates/thumbnail-information-indicator-icons-view';
import ViewUtils from '../../utils/view-library-utils';
import AttributeUtils from '../../../commonmodule/util/attribute-utils';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import {view as DisconnectedHTMLView} from "../../disconnectedhtmlview/disconnected-html-view";

const oEvents = {
  THUMB_SINGLE_CLICKED: "thumb_single_clicked",
  THUMB_CHECKBOX_CLICKED: "thumb_checkbox_clicked",
};

const oPropTypes = {
  id: ReactPropTypes.string,
  name: ReactPropTypes.string,
  className: ReactPropTypes.string,
  imageSrc: ReactPropTypes.string,
  type: ReactPropTypes.string,
  tags: ReactPropTypes.array,
  theme: ReactPropTypes.string,
  thumbnailViewModel: ReactPropTypes.instanceOf(ThumbnailItemModel).isRequired, //in order to send it with triggered events
  activeXRayPropertyGroup: ReactPropTypes.object,
  xRayConfigData: ReactPropTypes.object,
  hits: ReactPropTypes.array,
  hideXRayInfo: ReactPropTypes.bool,
  hideSearchInfo: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object.isRequired,
  masterUserList: ReactPropTypes.array
};
/**
 * @class ThumbnailXRayTemplateView - Use to Display x-ray view of Product in Application.
 * @memberOf Views
 * @property {string} [id] - Pass id of product.
 * @property {string} [name] - Pass Name of Product.
 * @property {string} [className] - Pass className create for product.
 * @property {string} [imageSrc] - Pass image path for product picture.
 * @property {string} [type] - Pass type of the product.
 * @property {array} [tags] - Pass array of tags use in Product like id, tagId, tagValue etc.
 * @property {string} [theme] - Pass theme name in which theme product is created.
 * @property {custom} thumbnailViewModel - Pass model contain parameters like id, label, imageSrc, tags, type,className, properties
 * @property {object} [activeXRayPropertyGroup] - Pass object contain columns, id, isDefaultForXRay, label, properties etc.
 * @property {object} [xRayConfigData] - Pass reference attribute array and referenced tags.
 * @property {array} [hits] - Pass hits article array.
 * @property {bool} [hideXRayInfo] - Pass default true boolean for hide xray info.
 * @property {bool} [hideSearchInfo] - Pass default true boolean for hide search info.
 */

// @MasterUserListContext
// @CS.SafeComponent
class ThumbnailXRayTemplateView extends React.Component {

  constructor(props) {
    super(props);
  }

  _handleSingleClicked =(oModel, oEvent)=> {
    if(!oEvent.nativeEvent.dontRaise) {
      oEvent.nativeEvent.dontRaise = true;
      EventBus.dispatch(oEvents.THUMB_SINGLE_CLICKED, oEvent, oModel, this.props.filterContext);
    }
  }

  _handleThumbnailCheckboxClicked =(oModel, oEvent)=>{
    oEvent.stopPropagation();
    EventBus.dispatch(oEvents.THUMB_CHECKBOX_CLICKED, this, oModel);
  }

  getThumbnailImageView =()=>{
    var sType = this.props.type;
    var oProperties = {};
    var oImageViewStyle = {};

    var oImageViewModel = new ImageViewModel(
        '',
        this.props.imageSrc,
        sType,
        "thumbnailImagePreview",
        oImageViewStyle,
        oProperties
    );

    return (<ImageView model={oImageViewModel} onLoad={this.handleImageOnLoad}/>);
  }

  getTagValueDOM = (sColorClass, sTagName,iIndex, oTagMaster) => {
    return <div className={"tagValue " + sColorClass} title={sTagName}
                key={iIndex}>{CS.getLabelOrCode(oTagMaster)}</div>
  };

  getTagListView =()=> {
    var aTagGroupList = [];
    var _this = this;
    var aTagGroups = this.props.tags;
    var oActiveXRayPropertyGroup = this.props.activeXRayPropertyGroup;
    var oXRayConfigData = this.props.xRayConfigData;
    var oReferencedTags = oXRayConfigData.referencedTags;
    var aXRayProperties = oActiveXRayPropertyGroup.properties;

    CS.forEach(aTagGroups, function(oTagGroup, iOuterIndex){
      if (CS.find(aXRayProperties, {id: oTagGroup.tagId})) {
        var aTagList = [];
        var aTagValues = oTagGroup.tagValues;
        var oTagGroupMaster = oReferencedTags[oTagGroup.tagId] || {};

        CS.forEach(aTagValues, function (oTagValue, iIndex) {
          var oTagMaster = ViewUtils.getNodeFromTreeListById(oTagGroupMaster.children, oTagValue.tagId);
          let aTagDetailView = [];
          if (oTagValue.relevance != 0) {
            if(oTagGroupMaster.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN){
              aTagDetailView = (<GridYesNoPropertyView value={true} isDisabled={true}/>);
            } else {
              var sColorClass = (oTagValue.relevance > 0) ? "greenBorder" : "redBorder";
              var sLabel = CS.getLabelOrCode(oTagGroupMaster) + ": " + CS.getLabelOrCode(oTagMaster);
              if (!CS.isEmpty(oTagMaster.klass)) {
                sLabel += " (" + oTagMaster.klass + ")";
              }
              let sTagName = CS.getLabelOrCode(oTagMaster);
              let sIcon = ViewUtils.getIconUrl(oTagMaster.iconKey);

              if (oTagMaster.iconKey) {
                aTagDetailView.push(
                    <div className="tagIconLabelWrapper">
                      <div className={"tagIcon " + sColorClass} title={sLabel} key={iIndex}>
                        <ImageFitToContainerView imageSrc={sIcon}/>
                      </div>
                      {_this.getTagValueDOM(sColorClass,sTagName,iIndex,oTagMaster)}
                    </div>
                      );
              } else if (oTagMaster.color) {
                var oStyle = {};
                oStyle.backgroundColor = oTagMaster.color;
                aTagDetailView.push(
                    <div className="tagIconLabelWrapper">
                      <div className={"tagColor " + sColorClass} style={oStyle} title={sLabel} key={iIndex}></div>
                      {_this.getTagValueDOM(sColorClass,sTagName,iIndex,oTagMaster)}
                    </div>);
              } else if (oTagMaster.klass) {
                aTagDetailView.push(<div className={"tagValue " + sColorClass} key={iIndex}
                                         title={sTagName}>{oTagMaster.klass}</div>);
              } else {
                aTagDetailView.push(
                    <div className="tagIconLabelWrapper">
                      <div className={"tagIcon " + sColorClass} title={sLabel} key={iIndex}>
                        <ImageFitToContainerView imageSrc={sIcon}/>
                      </div>
                      {_this.getTagValueDOM(sColorClass, sTagName, iIndex, oTagMaster)}
                    </div>
                );
              }
            }
            aTagList.push(
                <div className="tagContainer" key={iOuterIndex + iIndex}>
                  <div className="coloredIndicator"></div>
                  {aTagDetailView}
                </div>
            );
          }
        });
        let sImageSrc = ViewUtils.getIconUrl(oTagGroup.iconKey);
        if (!CS.isEmpty(aTagList)) {
          aTagGroupList.push(
              <div className="tagGroupContainer" key={aTagList.length + "tagGroupContainer"}>
                {_this.getIconDOM(sImageSrc)}
                <div className="tagGroupHeader">{CS.getLabelOrCode(oTagGroupMaster)}</div>
                {aTagList}
              </div>
          );
        }
      }
    });

    return (<div>{aTagGroupList}</div>);
  }

  getIconDOM (sImageSrc) {
    return <div className="customIcon">
      <ImageFitToContainerView imageSrc={sImageSrc}/>
    </div>;
  };

  getDisabledString = (aExpressionList) => {
    var sString = "";
    CS.forEach(aExpressionList, function (oExpression) {
      if(oExpression.type === "html") {
        sString += oExpression.valueAsHtml;
      } else {
        /** TODO : Currently we are not getting dependant attributes for concatenated attribute
         * Need to handle from backend also so "Displaying concatenated value in x-ray view" this will be limitation for now**/
        sString += oExpression.value || "";
      }
    });
    return sString;
  };

  getAttributeListView =()=> {
    var oModel = this.props.thumbnailViewModel;
    var oEntity = oModel.properties["entity"];
    var aAttributeList = oEntity.attributes;
    var oXRayConfigData = this.props.xRayConfigData;
    var oReferencedAttributes = oXRayConfigData.referencedAttributes;
    var oActiveXRayPropertyGroup = this.props.activeXRayPropertyGroup;
    var aXRayProperties = oActiveXRayPropertyGroup.properties;

    var aAttributeViewList = [];
    var _this = this;

    CS.forEach(aAttributeList, function (oAttribute) {
      if (!CS.isEmpty(oAttribute.value)) {
        var oXRayProperty = CS.find(aXRayProperties, {id: oAttribute.attributeId});
        if (!CS.isEmpty(oXRayProperty)) { //show attribute only if it is present in active x-ray property group
          var oMasterAttribute = oReferencedAttributes[oAttribute.attributeId] || {};
          var sAttributeValue = oAttribute.value;
          let sDate = "";
          let sTime = "";
          let sAttributeType = oMasterAttribute.type;
          if (ViewUtils.isAttributeTypeType(oMasterAttribute.type)) {
            sAttributeValue = _this.props.className;
          } else if (ViewUtils.isAttributeTypeDate(oMasterAttribute.type)) {
            if (ViewUtils.isAttributeTypeCreatedOn(oMasterAttribute.type) || ViewUtils.isAttributeTypeLastModified(oMasterAttribute.type)) {
              sAttributeValue = ViewUtils.getDateAttributeInDateTimeFormat(oAttribute.value);
              sDate = sAttributeValue.date;
              sTime = sAttributeValue.time;
              sAttributeValue = sDate + "  " + sTime;
            } else {
              sAttributeValue = ViewUtils.getDateAttributeInTimeFormat(oAttribute.value);
            }
          } else if (ViewUtils.isAttributeTypeMeasurement(oMasterAttribute.type)) {
            sAttributeValue = ViewUtils.getLabelByAttributeType(oMasterAttribute.type, oAttribute.value, oMasterAttribute.defaultUnit,
                oMasterAttribute.precision, oMasterAttribute.hideSeparator);
          } else if (ViewUtils.isAttributeTypeUser(oMasterAttribute.type)) {
            var aUserList = _this.props.masterUserList;
            var oUser = CS.find(aUserList, {id: oAttribute.value});
            sAttributeValue = !CS.isEmpty(oUser) ? (oUser.lastName + " " + oUser.firstName) : "";
          }
          else if (ViewUtils.isAttributeTypeNumber(sAttributeType)) {
            sAttributeValue = ViewUtils.getValueToShowAccordingToNumberFormat(oAttribute.value, oMasterAttribute.precision,
                {}, oMasterAttribute.hideSeparator);
          }

          let oValueDOM = null;
          let sClassName = "attributeValue ";
          let bIsConcatenated = ViewUtils.isAttributeTypeConcatenated(oMasterAttribute.type);
          if (ViewUtils.isAttributeTypeHtml(oMasterAttribute.type)) {
            sAttributeValue = oAttribute.valueAsHtml;
            oValueDOM = <div className={sClassName}><DisconnectedHTMLView content={sAttributeValue}/></div>
          }
          else if (bIsConcatenated) {
            let bDisabled = true;
            let sValue = oAttribute.valueAsHtml;
            let sErrorText = "";
            let sPlaceholder = "";
            let sDescription = "";
            let sLabel = CS.getLabelOrCode(oModel);
            let aExpression = oAttribute.valueAsExpression;
            let bShowDisconnected = false;
            let oProperties = {};
            let oConcatModel = new ContentConcatenatedAttributeViewModel(
                oModel.id,
                sLabel,
                sValue,
                sPlaceholder,
                sErrorText,
                sDescription,
                bDisabled,
                bShowDisconnected,
                aExpression,
                oProperties
            );
            oValueDOM = <ContentConcatenatedAttributeView model={oConcatModel}/>;
          } else {
            oValueDOM = <div className={sClassName}>{sAttributeValue}</div>
          }

          let sImageSrc = ViewUtils.getIconUrl(oAttribute.iconKey);
          aAttributeViewList.push(
              <div className="attributeContainer">
                {_this.getIconDOM(sImageSrc)}
                <div className="attributeLabel">{CS.getLabelOrCode(oMasterAttribute)}</div>
                {oValueDOM}
              </div>
          );
        }
      }
    });

    return aAttributeViewList;
  }

  getBelongsToVariantView =()=> {
    return (<div className="belongsToVariantSticker">{getTranslation().VARIANT}</div>);
  }

  getBelongsToContextView =()=> {
    return (<div className="belongsToVariantSticker">{getTranslation().CONTEXT}</div>);
  }

  getSearchSummaryView =()=> {
    var _this = this;
    var oXRayConfigData = this.props.xRayConfigData;
    var oReferencedAttributes = oXRayConfigData.referencedAttributes;
    var aCompleteSearchSummary = [];
    var aAttributeHitSummary = [];
    var aTagHitSummary = [];

    //To always get instance of master, if any, else of variant, if any
    var aHits = CS.keyBy(CS.sortBy(this.props.hits, "contextInstanceId"), "id");

    CS.forEach(aHits, function (oHit) {
      var sHitType = oHit.type;
      var aValueList = [];
      var sHitLabel = CS.getLabelOrCode(oHit);
      var oBelongsToView = null;

      if(!!oHit.contextInstanceId){
        oBelongsToView = _this.getBelongsToContextView();
      } else if(!!oHit.ownerId){
        oBelongsToView = _this.getBelongsToVariantView();
      }

      //TAG :
      if (ViewUtils.isTag(sHitType) || sHitType == "tag") {
        var aFilteredValues = CS.keyBy(CS.sortBy(oHit.values, "contextInstanceId"), "id");

        CS.forEach(aFilteredValues, function (oValue) {
          var sTagLabel = CS.getLabelOrCode(oValue);
          aValueList.push(
              <div className="tagHitValue">{sTagLabel}</div>
          );
        });

        aTagHitSummary.push(
            <div className="tagHitContainer">
              <div className="tagHitLabel">{sHitLabel + " :"}</div>
              {aValueList}
              {oBelongsToView}
            </div>
        );
      }
      //ATTRIBUTE :
      else {
        //there will be a single value object in oHit.values for attributes, and we have to parse the label for each individual occurrence.
        var oValue = oHit.values[0];
        var oMasterAttribute = oReferencedAttributes[oHit.id] || {};
        var sAttributeType = oMasterAttribute.type;
        var oAttributeValue = null;

        if (ViewUtils.isAttributeTypeText(sHitType) || ViewUtils.isAttributeTypeHtml(sHitType)) {
          var oDangerousHtmlObj = {__html: CS.getLabelOrCode(oValue)};
          oAttributeValue = (
              <div className="attributeHitOccurrence" dangerouslySetInnerHTML={oDangerousHtmlObj}></div>
          );
        } else {
          var aRawOccurrenceLabels = ViewUtils.extractInnerTextFromHtml(CS.getLabelOrCode(oValue));//Extract occurrences between "<em>" tags
          var iLabelCount = aRawOccurrenceLabels.length;
          CS.forEach(aRawOccurrenceLabels, function (sRawOccurrenceLabel, iIndex) {

            var sOccurrenceLabel = AttributeUtils.getLabelByAttributeType(sAttributeType, sRawOccurrenceLabel, oHit.defaultUnit, oHit.precision);//eg. correct date from timestamp, converted measurement unit etc
            aValueList.push(
                <div className="attributeHitOccurrence">
                  {((iIndex === iLabelCount - 1) && (iLabelCount > 0)) ? (sOccurrenceLabel) : (sOccurrenceLabel + ", ")}
                </div>
            );
          });
          oAttributeValue = aValueList;
        }

        if(CS.isEmpty(sHitLabel)){
          sHitLabel = CS.getLabelOrCode(oMasterAttribute);
        }
        aAttributeHitSummary.push(
            <div className="attributeHitContainer">
              <div className="attributeHitLabel">{sHitLabel + " :"}</div>
              {oAttributeValue}
              {oBelongsToView}
            </div>
        );
      }
    });

    if(!CS.isEmpty(aAttributeHitSummary)) {
      aCompleteSearchSummary.push(
          <div className="summarySection">
            <div className="summarySectionHeader">{getTranslation().ATTRIBUTES}</div>
            <div className="summarySectionContent">{aAttributeHitSummary}</div>
          </div>
      );
    }

    if(!CS.isEmpty(aTagHitSummary)) {
      aCompleteSearchSummary.push(
          <div className="summarySection">
            <div className="summarySectionHeader">{getTranslation().TAGS}</div>
            <div className="summarySectionContent">{aTagHitSummary}</div>
          </div>
      );
    }

    return (aCompleteSearchSummary);
  }

  getTypesView =()=> {
    var oModel = this.props.thumbnailViewModel;
    var _this = this;

    var aClassNames = CS.cloneDeep(oModel.properties["classNames"]);
    CS.remove(aClassNames, function (sName) {
      return sName == _this.props.className;
    }); //show all class names except the main class

    if (!CS.isEmpty(aClassNames)) {
      var sTypes = aClassNames.join(', ');
      return (
          <div className="typesContainer">
            <div className="typesLabel">{getTranslation().TYPE}</div>
            <div className="typesValue">{sTypes}</div>
          </div>
      );
    } else {
      return null;
    }
  }

  getInfoView =()=> {

    var __props = this.props;
    var oSearchInfoView = null;
    var oXRayInfoView = null;


    if(!__props.hideXRayInfo){
      var oTagListViews = this.getTagListView(); //TODO: get tags from ThumbnailAdditionalInformationView
      var oAttributeListViews = this.getAttributeListView();
      var oTypesView = this.getTypesView();

      oXRayInfoView = (<div className="xRaySummary">
        {/*<div className="summaryHeader">{getTranslation().XRAY_INFORMATION}</div>*/}
        <div className="typesSummarySection">
          {oTypesView}
        </div>

        <div className="tagSummarySection">
          {oTagListViews}
        </div>

        <div className="attributeSummarySection">
          {oAttributeListViews}
        </div>
      </div>);
    }

    if(!__props.hideSearchInfo){
      var oSearchSummaryView = this.getSearchSummaryView();

      oSearchInfoView = (<div className="searchSummary">
        <div className="summaryHeader">{getTranslation().SEARCH_FILTER_HIT_SUMMARY}</div>
        <div className="searchSummarySection">
          {oSearchSummaryView}
        </div>
      </div>)
    }


    return (<div className="summarySectionContainer">
      {oSearchInfoView}
      {oXRayInfoView}
    </div>);
  };

  getNewAndUpdatedIndicator = () => {
    let oModel = this.props.thumbnailViewModel;
    let oEntity = oModel.properties.entity;
    return <ThumbnailInformationIndicatorIconsView isRecentlyUpdated={oEntity.isRecentlyUpdated}
                                                   isNewlyCreated = {oEntity.isNewlyCreated}
                                                   creationLanguageData = {oEntity.creationLanguageData}/>
  };

  render() {

    var sEntityName = this.props.name;
    var oThumbnailImageView = this.getThumbnailImageView();
    var sClassName = this.props.className;

    var sTheme = this.props.theme;

    var oModel = this.props.thumbnailViewModel;
    /*var oProperties = oModel.properties;
    var oRuleViolationDetails = oProperties.entity.messages;
    var iMandatoryViolationCount = oProperties.entity.mandatoryViolationCount;
    let iShouldViolationCount = oProperties.entity.shouldViolationCount;
    let iUniqueViolationCount = oProperties.entity.isUniqueViolationCount;
    var sCloneOfLabel = oProperties.entity.branchOfLabel || "";
    let bIsGoldenArticle = CS.includes(oProperties.entity.types, "golden_article_klass");
    var oThumbnailDetailView = <ThumbnailInformationView ruleViolation={oRuleViolationDetails}
                                                         cloneOfLabel={sCloneOfLabel}
                                                         mandatoryViolationCount={iMandatoryViolationCount}
                                                         shouldViolationCount={iShouldViolationCount}
                                                         uniqueViolationCount={iUniqueViolationCount}
                                                         contentId={oModel.id}
                                                         isMdmInstanceDeleted={oProperties.entity.isMdmInstanceDeleted}
                                                         isGoldenArticle={bIsGoldenArticle}/>;*/
    var isSelected = oModel.properties['isSelected'];

    var oInfoView = this.getInfoView();

    var sContainerClassName = oModel.properties['containerClass'] || "";

    var oCheckButtonDOM = null;
    var fOnCheckHandler = null;
    var fSingleClickHandler = null;
    var bDisableCheck = oModel.properties['disableCheck'];
    var bDisableView = oModel.properties['disableView'];
    var bDisableDelete = oModel.properties['disableDelete'];
    var oCursorStyle = {};

    if (!bDisableCheck) {
      var sSelectLabel = isSelected ? getTranslation().DESELECT : getTranslation().SELECT;
      fOnCheckHandler = this._handleThumbnailCheckboxClicked.bind(this, oModel);
      oCheckButtonDOM = (
          <TooltipView label={sSelectLabel}>
            <div className={isSelected ? "thumbnailCheckButton isSelected" : "thumbnailCheckButton"}>
              <div className={isSelected ? "thumbnailCheckButtonIcon isSelected" : "thumbnailCheckButtonIcon"}></div>
            </div>
          </TooltipView>
      );
    }

    if (!bDisableView) {
      oCursorStyle.cursor = "pointer";
      fSingleClickHandler = this._handleSingleClicked.bind(this, oModel);
    }
    var oStyle = null;
    if(bDisableDelete){
      oStyle = {
        "right": "5px"
      };
    }


    let oActionItemsView = <ThumbnailActionItemView
        thumbnailViewModel={oModel}
        theme={this.props.theme}
        filterContext={this.props.filterContext}
    />;
    let oNewAndUpdatedIndicator = this.getNewAndUpdatedIndicator();

    return (
        <div className={"summaryTemplateContainer " + sTheme + " " + sContainerClassName} onClick={fSingleClickHandler}
             style={oCursorStyle}>

          <div className={isSelected ? "thumbnailHeaderContainer isSelected" : "thumbnailHeaderContainer"}
               onClick={fOnCheckHandler}>
            {oCheckButtonDOM}
            <div className="entityInformationContainer">
              <div className="entityName">{sEntityName}</div>
            </div>
            {oNewAndUpdatedIndicator}
          </div>

          {oInfoView}
          <div className="thumbnailXRayTemplateFooterView">
          {oActionItemsView}
          </div>
        </div>
    );
  }
}

ThumbnailXRayTemplateView.propTypes = oPropTypes;



export const view = MasterUserListContext(ThumbnailXRayTemplateView);
export const events = oEvents;
