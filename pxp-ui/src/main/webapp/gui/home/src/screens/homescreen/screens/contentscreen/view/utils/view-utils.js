import CS from '../../../../../../libraries/cs';
import React from 'react';
import ContentUtils from '../../store/helper/content-utils';
import AttributeUtils from '../../../../../../commonmodule/util/attribute-utils';
import CommonUtils from '../../../../../../commonmodule/util/common-utils';
import ScreenModeUtils from '../../store/helper/screen-mode-utils';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import ContentScreenConstants from '../../store/model/content-screen-constants';
import copy from "copy-to-clipboard";
import alertify from "../../../../../../commonmodule/store/custom-alertify-store";
import {getTranslations} from "../../../../../../commonmodule/store/helper/translation-manager";
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

var ViewUtils = (function () {

/*
  var _getWindowHeight = function(){

    // return $(window).height();
    return document.body.clientHeight;
  };
*/

  var _getImageSrcUrlFromImageObject = function(oImage){
    var sThumbnailImageSrc = '';
    var sType = '';

    if (!CS.isEmpty(oImage)) {
      if (oImage.copyOf) {
        sThumbnailImageSrc = (oImage.byteStream) ? oImage.byteStream : "contentImage/getByteStream/" + oImage.copyOf;
      } else {
        sType = CS.isNotEmpty(oImage.properties) && oImage.properties.extension;
        if (oImage.thumbKey) {
          sThumbnailImageSrc = (oImage.byteStream) ? oImage.byteStream : RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oImage.type,
            id: oImage.thumbKey
          });
        }
      }
    }

    return {thumbnailImageSrc: sThumbnailImageSrc, thumbnailType: sType};
  };

  var _getHighlightedHeaderText = function (sLabel, sSearchString, sClassName, sTooltipLabel) {
    let sSearchText = sSearchString;
    let sSearchTextToLowerCase = sSearchText.toLowerCase();
    let sLabelToLowerCase = sLabel.toLowerCase();
    let aDom = [];
    let iSearchTextLength = sSearchText.length;
    let iLabelLength = sLabel.length;
    let sTextToDisplay = '';
    let iCount = 0;
    let iIndex = 0;

    /** if label and searchText are equal **/
    if (sLabelToLowerCase == sSearchTextToLowerCase) {
      aDom.push(<span className={sClassName}>{sLabel}</span>);
      return (<span>{aDom}</span>);
    } else if(!iSearchTextLength){
      aDom.push(<span>{sLabel}</span>);
      return (<span>{aDom}</span>);
    }

    /** Iterate over label character and searchText character to fill the dom nodes according to (highlighted
     *  character or normal
     *  character) **/
    for (var i = 0; i < iLabelLength; i++) {
      iCount = 0;
      iIndex = i;
      for (var j = 0; j < iSearchTextLength; j++) {
        if (sLabelToLowerCase.charAt(iIndex) == sSearchTextToLowerCase.charAt(j)) {
          iCount++;
          iIndex++;
        } else {
          break;
        }
      }

      if (iCount == iSearchTextLength) {            /** for highlighted character **/
        iIndex = i + iCount;
        sTextToDisplay = sLabel.substring(i, iIndex);
        i = i + iCount;
        i--;
        aDom.push(<span className={sClassName}>{sTextToDisplay}</span>)
      } else {                                      /** for normal character **/
        sTextToDisplay = sLabel.charAt(i);
        aDom.push(<span>{sTextToDisplay}</span>)
      }
    }

    return (<span>{aDom}</span>);
  };

  var _getMeasurementAttributeValueToShow = function (sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision) {
    return AttributeUtils.getMeasurementAttributeValueToShow(sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision);
  };

  var _getThumbnailType = function () {
    var sScreenMode = ContentUtils.getContentScreenMode();
    switch (sScreenMode) {

      case ContentScreenConstants.entityModes.ARTICLE_MODE:
        return "article";

      default:
        return "";
    }
  };

  let _getUpdatedSortData = function (oSortData, sHeaderId) {
    let oSortDataToSend = {};
    if (oSortData.sortBy === sHeaderId) {
      //if yes, then toggle the sort order and dispatch event
      if (oSortData.sortOrder === "asc") {
        oSortDataToSend.sortOrder = "desc";
      } else {
        oSortDataToSend.sortOrder = "asc";
      }
    } else {
      //else dispatch event directly
      oSortDataToSend.sortOrder = oSortData.sortOrder;
    }
    oSortDataToSend.sortBy = sHeaderId;
    return oSortDataToSend;
  };

  let _copyToClipboard = function (sValue) {
    return copy(sValue) ? alertify.success(getTranslations().CODE_COPIED)
        : alertify.error(getTranslations().COPY_TO_CLIPBOARD_FAILED);
  };

  return {
    getAttributeTypeForVisual: function(sType, sAttrId){
      return ContentUtils.getAttributeTypeForVisual(sType, sAttrId);
    },

/*
    getCurrentZoomLevel: function(){
      return ContentUtils.getScreenProps().getCurrentZoom();
    },
*/

    getMinAllowedZoom: function(){
      return ContentUtils.getMinAllowedZoom();
    },

    getMaxAllowedZoom: function () {
      return ContentUtils.getMaxAllowedZoom();
    },

/*
    getWindowHeight: function(){
      return _getWindowHeight();
    },
*/

    getSplitter: function(){
      return ContentUtils.getSplitter();
    },

    getNodeFromTreeListById: function (aNodeList, sNodeId) {
      return ContentUtils.getNodeFromTreeListById(aNodeList, sNodeId);
    },

    getImageSrcUrlFromImageObject: function(oImage){
      return _getImageSrcUrlFromImageObject(oImage);
    },

    getElementAssetData: function (oContent) {
      return ContentUtils.getElementAssetData(oContent);
    },

    getIconUrl: function (sAssetKey) {
      return ContentUtils.getIconUrl(sAssetKey);
    },

    getCurrentUser: function () {
      return ContentUtils.getCurrentUser();
    },

    getLanguageInfo: function () {
      return ContentUtils.getLanguageInfo();
    },

    getThumbnailTypeFromBaseType: function (sBaseType) {
      return ContentUtils.getThumbnailTypeFromBaseType(sBaseType);
    },

    getCurrentLocaleNumberValue: function (sVal) {
      return ContentUtils.getCurrentLocaleNumberValue(sVal);
    },

    getUserNameById : function(sUserId){
      return ContentUtils.getUserNameById(sUserId);
    },

    getUserDisplayName: function (oUser) {
      var aName = [];
      if(oUser) {
        aName.push(oUser.lastName);
        aName.push(oUser.firstName);
        return aName.join(' ');
      }
      else {
        return getTranslations().UNKNOWN_USER;
      }
    },

    getUserById : function(sUserId){
      return ContentUtils.getUserById(sUserId);
    },

    getUserByUserName : function(sUserId){
      return ContentUtils.getUserByUserName(sUserId);
    },

    getUserList: function () {
      return ContentUtils.getUserList();
    },

/*
    getSectionElementFromActiveEntityClassByElementId: function (sElementId) {
      return ContentUtils.getSectionElementFromActiveEntityClassByElementId(sElementId);
    },
*/

    getDisplayUnitFromDefaultUnit: function (sDefaultUnit, sType) {
      return ContentUtils.getDisplayUnitFromDefaultUnit(sDefaultUnit, sType);
    },

    getTagById: function (sTagId) {
      return ContentUtils.getMasterTagById(sTagId);
    },

    getEntityType: function (oEntity) {
      return ContentUtils.getEntityType(oEntity);
    },

    isAssetBaseType: function (sBaseType) {
      return ContentUtils.isAssetBaseType(sBaseType);
    },

    isSupplierBaseType: function (sBaseType) {
      return ContentUtils.isSupplierBaseType(sBaseType);
    },

    getThumbnailType: function () {
      return _getThumbnailType();
    },

    isAttributeTypeHtml: function (sType) {
      return ContentUtils.isAttributeTypeHtml(sType);
    },

    isAttributeTypeDescription: function (sType) {
      return ContentUtils.isAttributeTypeDescription(sType);
    },

    /** @deprecated **/
    isAttributeTypeType: function (sType) {
      return ContentUtils.isAttributeTypeType(sType);
    },

    isAttributeTypeName: function (sType) {
      return ContentUtils.isAttributeTypeName(sType);
    },

    isAttributeTypeText: function (sType) {
      return ContentUtils.isAttributeTypeText(sType);
    },

    isAttributeTypeCreatedOn: function (sType) {
      return ContentUtils.isAttributeTypeCreatedOn(sType);
    },

    isAttributeTypeLastModified: function (sType) {
      return ContentUtils.isAttributeTypeLastModified(sType);
    },

    isAttributeTypeDueDate: function (sType) {
      return ContentUtils.isAttributeTypeDueDate(sType);
    },

    isAttributeTypeDate: function (sType) {
      return ContentUtils.isAttributeTypeDate(sType);
    },

    isAttributeTypeNumber: function (sType) {
      return ContentUtils.isAttributeTypeNumber(sType);
    },

    isAttributeTypeNumeric: function (sType) {
      return ContentUtils.isAttributeTypeNumeric(sType);
    },

    isAttributeTypeMeasurement: function (sType) {
      return ContentUtils.isAttributeTypeMeasurement(sType);
    },

    isAttributeTypeTelephone: function (sType) {
      return ContentUtils.isAttributeTypeTelephone(sType);
    },

    isAttributeTypeCoverflow: function (sType) {
      return ContentUtils.isAttributeTypeCoverflow(sType);
    },

    isAttributeTypeMetadata: function (sType) {
      return ContentUtils.isAttributeTypeMetadata(sType);
    },

    isAttributeTypeUser: function (sType) {
      return ContentUtils.isAttributeTypeUser(sType);
    },

    isRoleTypeOwner: function (sType) {
      return ContentUtils.isRoleTypeOwner(sType);
    },

/*
    isRoleTypeAssignee: function (sType) {
      return ContentUtils.isRoleTypeAssignee(sType);
    },
*/

    isAttributeTypeRole: function (sType) {
      return ContentUtils.isAttributeTypeRole(sType);
    },

    isAttributeTypeTaxonomy: function (sType) {
      return ContentUtils.isAttributeTypeTaxonomy(sType);
    },

    isAttributeTypeSecondaryClasses: function (sType) {
      return ContentUtils.isAttributeTypeSecondaryClasses(sType);
    },

    isMandatoryAttribute: function (sType) {
      return ContentUtils.isMandatoryAttribute(sType);
    },

    isCalculatedAttribute: function (sType) {
      return ContentUtils.isAttributeTypeCalculated(sType);
    },

    isConcatenatedAttribute: function (sType) {
      return ContentUtils.isAttributeTypeConcatenated(sType);
    },

    isAttributeTypeCalculated: function (sType) {
      return ContentUtils.isAttributeTypeCalculated(sType);
    },

    isAttributeTypeConcatenated: function (sType) {
      return ContentUtils.isAttributeTypeConcatenated(sType);
    },

    getRoleList: function () {
      return ContentUtils.getRoleList();
    },

    getMeasurementAttributeValueToShow: function (sAttributeVal, sBaseUnit, sDefaultUnit, sPrecision) {
      return _getMeasurementAttributeValueToShow(sAttributeVal, sBaseUnit, sDefaultUnit, sPrecision);
    },

    getTruncatedValue: function (iVal, sPrecision) {
      return AttributeUtils.getTruncatedValue(iVal, sPrecision);
    },

    getViewMode: function () {
      return ContentUtils.getViewMode();
    },

    isTag: function (sBaseType) {
      return ContentUtils.isTag(sBaseType);
    },

    getIsStaticCollectionScreen: function () {
      return ContentUtils.getIsStaticCollectionScreen();
    },

    getIsDynamicCollectionScreen: function () {
      return ContentUtils.getIsDynamicCollectionScreen();
    },

    getDateAttributeInTimeFormat: function (sValue) {
      return ContentUtils.getDateAttributeInTimeFormat(sValue);
    },

    getDateAttributeInDateTimeFormat: function (sValue) {
      return ContentUtils.getDateAttributeInDateTimeFormat(sValue);
    },

    /*getDateInTextFormat: function (sValue) {
      return CommonUtils.getDateInTextFormat(sValue);
    },*/

    getFilterProps: function (oFilterContext) {
      return ContentUtils.getFilterProps(oFilterContext);
    },

    isUserTypeAttribute: function (sType) {
      return AttributeUtils.isUserTypeAttribute(sType);
    },

    formatDate: function (sValue) {
      return ContentUtils.formatDate(sValue);
    },

    getBaseUnitFromType: function (sType) {
      return AttributeUtils.getBaseUnitFromType(sType);
    },

    getDecodedHtmlContent: function (sHtml) {
      return ContentUtils.getDecodedHtmlContent(sHtml)
    },

    /*filterContextualAttributeInstancesByTagValue: function (sTagId, sTagValueId, aAttributeList) {
      return ContentUtils.filterContextualAttributeInstancesByTagValue(sTagId, sTagValueId, aAttributeList);
    },

    canAttributeBeContextual: function (sType) {
      return AttributeUtils.canAttributeBeContextual(sType);
    },*/

    isBaseTypeArticle: function (sType) {
      return ContentUtils.isBaseTypeArticle(sType);
    },

    isTextAssetBaseType: function (sType) {
      return ContentUtils.isTextAssetBaseType(sType);
    },

    isTargetBaseType: function (sType) {
      return ContentUtils.isTargetBaseType(sType);
    },

    isAttributeTypePrice: function (sType) {
      return AttributeUtils.isAttributeTypePrice(sType);
    },

/*
    isNatureClass: function(sId){
      return ContentUtils.isNatureClass(sId)
    },
*/

    getNatureKlassIdFromKlassIds: function (aIds) {
      return ContentUtils.getNatureKlassIdFromKlassIds(aIds)
    },

    getTextColorBasedOnBackgroundColor: function (sBackgroundColorHex) {
      return ContentUtils.getTextColorBasedOnBackgroundColor(sBackgroundColorHex);
    },

    /*getNumberValueByPrecision: function (sValue ,iPrecision) {
      return ContentUtils.getNumberValueByPrecision(sValue ,iPrecision);
    },

    isVariantTypeProduct: function () {
      return ContentUtils.isVariantTypeProduct();
    },*/

    getRelationshipTypeById: function (sRelationshipId) {
      return ContentUtils.getRelationshipTypeById(sRelationshipId);
    },

    isNatureRelationship: function (sRelType) {
      return ContentUtils.isNatureRelationship(sRelType);
    },

    isVariantRelationship: function (sRelType) {
      return ContentUtils.isVariantRelationship(sRelType);
    },

    getEntityClassType: function (oEntity) {
      return ContentUtils.getEntityClassType(oEntity);
    },

    getTreeRootNodeId: function () {
      return ContentUtils.getTreeRootNodeId();
    },

    getTagListMap: function () {
      return ContentUtils.getTagListMap();
    },

    getAttributeListMap: function () {
      return ContentUtils.getAttributeListMap();
    },

    //TODO: Remove
    getTaxonomyList: function () {
      return ContentUtils.getTaxonomyList();
    },

    getParentNodeByChildId: function (aNodeList, sChildId, oParentNode) {
      return ContentUtils.getParentNodeByChildId(aNodeList, sChildId, oParentNode)
    },

    // getTagDialogVisibility: function () {
    //   //TODO: High Priority -> Resolve below dependency ASAP - Shashank
    //   var oSettingUtils = require("./../../../settingscreen/view/utils/view-utils").default;
    //   return oSettingUtils.getTagDialogVisibility();
    // },

/*
    getShortDate: function (iTimeStamp) {
      return CommonUtils.getShortDate(iTimeStamp);
    },
*/

    getIsHierarchyViewMode: function (sViewMode) {
      return ContentUtils.getIsHierarchyViewMode(sViewMode);
    },

    getHierarchyDummyNodeId: function () {
      return ContentUtils.getHierarchyDummyNodeId();
    },

   /* removeZeroValuesFromFilterTags: function (aTags, bChangeInOriginalObject) {
      return ContentUtils.removeZeroValuesFromFilterTags(aTags, bChangeInOriginalObject);
    },

    removeZeroRelevanceFromAnyTags: function (aTags, bChangeInOriginalObject) {
      return ContentUtils.removeZeroRelevanceFromAnyTags(aTags, bChangeInOriginalObject);
    },*/

    getKlassFromReferencedKlassesById: function(sKlassId){
      return ContentUtils.getKlassFromReferencedKlassesById(sKlassId);
    },

    getEntitySearchText: function () {
      return ContentUtils.getEntitySearchText();
    },

    getLinkedInstancesFromVariant: function(oVariant){
      return ContentUtils.getLinkedInstancesFromVariant(oVariant);
    },

    getIsOnLandingPage: function () {
      return ContentUtils.getIsOnLandingPage();
    },

    getDecodedTranslation: function (sStringToCompile, oParameter) {
      return ContentUtils.getDecodedTranslation(sStringToCompile, oParameter);
    },

    getDataIntegrationInfo: function () {
      return ContentUtils.getDataIntegrationInfo();
    },

    getSelectedModuleId: function () {
      return ContentUtils.getSelectedModuleId();
    },

    getContextTypeBasedOnContextId: function (sContextId) {
      return ContentUtils.getContextTypeBasedOnContextId(sContextId);
    },

    isContextTypeProductVariant: function (sType) {
      return ContentUtils.isContextTypeProductVariant(sType);
    },

    isOnHeaderDisabledTab: function () {
      return ContentUtils.isOnHeaderDisabledTab();
    },

/*
    isXlsOrXlsxFile: function (sExtension) {
      return CommonUtils.isXlsOrXlsxFile(sExtension);
    },
*/

    getContentName: function (oContent) {
      return ContentUtils.getContentName(oContent);
    },

   /* isObjStpFbxFile: function (sExtension) {
      return CommonUtils.isObjStpFbxFile(sExtension);
    },

    prepareDownloadAssetURLwithSessionProps: function (sURLPrefix) {
      return ContentUtils.prepareDownloadAssetURLwithSessionProps(sURLPrefix)
    },
*/
    getTranslations: () => {
      return ContentUtils.getTranslations();
    },

/*
    getFilteredName: function (sValue) {
      return CommonUtils.getFilteredName(sValue);
    },
*/

    getIsArchive: function () {
      return ContentUtils.getIsArchive();
    },

    getIsRuleViolatedContentsScreen: function () {
      return ContentUtils.getIsRuleViolatedContentsScreen();
    },

    getNatureRelationshipIdLabelByRelationshipType: function (sRelationshipType) {
      return ContentUtils.getNatureRelationshipIdLabelByRelationshipType(sRelationshipType);
    },

    getTagGroupModels: function (oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags) {
      return CommonUtils.getTagGroupModels(oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags);
    },

    scrollBy: function (oDom, oStyles, sEasing, fCallback) {
      return CommonUtils.scrollBy(oDom, oStyles, sEasing, fCallback)
    },

    scrollTo: function (oDom, oStyles, sEasing, fCallback) {
      return CommonUtils.scrollTo(oDom, oStyles, sEasing, fCallback)
    },

    getHighlightedHeaderText: function (sLabel, sSearchString, sClassName, sTooltipLabel) {
      return _getHighlightedHeaderText(sLabel, sSearchString, sClassName, sTooltipLabel)
    },

    getUpdatedSortData: function (oSortData, sHeaderId) {
      return _getUpdatedSortData(oSortData, sHeaderId);
    },

    getIsCurrentUserReadOnly: function () {
      return CommonUtils.isCurrentUserReadOnly();
    },

    copyToClipboard: function (sValue) {
      return _copyToClipboard(sValue);
    },
  };

})();

export default ViewUtils;
