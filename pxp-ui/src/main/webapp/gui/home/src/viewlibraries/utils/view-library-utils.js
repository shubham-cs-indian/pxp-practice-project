import CS from '../../libraries/cs';
import RequestMapping from '../../libraries/requestmappingparser/request-mapping-parser.js';
import { UploadRequestMapping as oUploadRequestMapping } from '../tack/view-library-request-mapping';
import AttributeUtils from '../../commonmodule/util/attribute-utils';
import CommonUtils from '../../commonmodule/util/common-utils';
import TagUtils from '../../commonmodule/util/tag-utils';
import EntityBaseTypeDictionary from '../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import Constants from '../../commonmodule/tack/constants';
import HomeScreenAppData  from '../../screens/homescreen/store/model/home-screen-app-data';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

var ViewLibraryUtils = (function () {

  var _getIconUrl = function (sKey, bIconImageType) {
    let sType = bIconImageType ? "Image" : "Icons";
    if (CS.startsWith(sKey, "data:")) {
      return sKey;
    }
    return RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {type: sType, id: sKey}) + "/";
  };

  var _getSplitter = function(){
    return "#$%$#";
  };

  var _isTag = function (sBaseType) {
    return TagUtils.isTag(sBaseType);
  };

  var _getUserNameById = function (sUserId, aUserList) {
    let sUserName = "";
    if(CS.isEmpty(aUserList)){
      aUserList = HomeScreenAppData.getUserList();
    }
    CS.forEach(aUserList,function (oUser) {
      if(oUser.id == sUserId){
        sUserName = oUser.userName;
        return false;
      }
    });

    return sUserName;
  };

  var _getValueWithoutExponent = function (sValue) {
    var iExponent;
    if (Math.abs(sValue) < 1.0) {
      iExponent = parseInt(sValue.toString().split('E-')[1]);
      if (iExponent) {
        sValue *= Math.pow(10,iExponent-1);
        sValue = '0.' + (new Array(iExponent)).join('0') + sValue.toString().substring(2);
      }
    } else {
      iExponent = parseInt(sValue.toString().split('E')[1]);
      if (iExponent > 20) {
        iExponent -= 20;
        sValue /= Math.pow(10,iExponent);
        sValue += (new Array(iExponent+1)).join('0');
      }
    }
    return sValue;
  };

  var _getUserById = function (sUserId, aUserList) {
    return CS.find(aUserList, {id: sUserId}) || [];
  };

  let _getUserByUsername = function (sUserId, aUserList) {
    return CS.find(aUserList, {userName: sUserId}) || [];
  };

  var _isAssetBaseType = function (sBaseType) {
    return sBaseType == EntityBaseTypeDictionary['assetBaseType'];
  };

  var _isBaseTypeArticle = function (sBaseType) {
    return (sBaseType == EntityBaseTypeDictionary["contentBaseType"]);
  };

  var _getImageSrcUrlFromImageObject = function(oImage){
    var sThumbnailImageSrc = '';
    var sType = '';

    if (!CS.isEmpty(oImage)) {
      if (oImage.copyOf) {
        sThumbnailImageSrc = (oImage.byteStream) ? oImage.byteStream : "contentImage/getByteStream/" + oImage.copyOf;
      } else {
        sType = oImage.properties.extension;
        if (oImage.thumbKey) {
          sThumbnailImageSrc = (oImage.byteStream) ? oImage.byteStream : RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {
            type: oImage.type,
            id: oImage.thumbKey
          });
        }
      }
    }

    return {thumbnailImageSrc: sThumbnailImageSrc, thumbnailType: sType};
  };

  var _getElementAssetData = function (oElement) {
    var oElementAssetData = {};
    try {
      var oReferencedAsset = !CS.isEmpty(oElement.referencedAssets) && CS.find(oElement.referencedAssets, {isDefault: true}) || {};
      if (oElement && _isAssetBaseType(oElement.baseType)) {
        let oAssetAttribute = oElement.assetInformation;
        if (oAssetAttribute) {
          oReferencedAsset = {
            previewImageKey: oAssetAttribute.previewImageKey,
            fileName: oAssetAttribute.fileName,
            assetObjectKey: oAssetAttribute.assetObjectKey,
            assetInstanceId: oElement.id,
            isDefault: true,
            label: oElement.name,
            properties: oAssetAttribute.properties,
            thumbKey: oAssetAttribute.thumbKey,
            type: oAssetAttribute.type
          }
        }
      }

      var sExtention = '';
      var sThumbKeySRC = '';
      var sImageSrc = '';
      var sPreviewSrc = '';
      var sMp4Src = '';

      if(!CS.isEmpty(oReferencedAsset)) {
        if(CS.isNotEmpty(oReferencedAsset.thumbKey)){
          sThumbKeySRC = RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.thumbKey
          });
        }

        if(CS.isNotEmpty(oReferencedAsset.assetObjectKey)){
          sImageSrc = RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.assetObjectKey
          });
        }

        if(CS.isNotEmpty(oReferencedAsset.previewImageKey)){
          sPreviewSrc = RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.previewImageKey
          });
        }

        if(CS.isNotEmpty(oReferencedAsset.properties.mp4)){
          sMp4Src = RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.properties.mp4
          });
        }


        sExtention = oReferencedAsset.properties && oReferencedAsset.properties.extension || '';
      }

      oElementAssetData = {
        fileName: oReferencedAsset.fileName,
        thumbKeySrc: sThumbKeySRC,
        imageSrc: sImageSrc,
        extension: sExtention,
        assetObjectKey: oReferencedAsset.assetObjectKey,
        previewSrc: sPreviewSrc,
        mp4Src: sMp4Src
      };
    }
    catch(oException) {
      ExceptionLogger.error(oException);
    }

    return oElementAssetData;
  };

  var _getTagRelevanceForDoubleSlider = (oTagValue) => {
    let iFrom = oTagValue.from || 0;
    let iTo = oTagValue.to || 0;
    let oRelevance = {from: iFrom, to: iTo};
    oRelevance = CS.has(oTagValue, "from") ? oRelevance : oTagValue.relevance;
    return oRelevance;
  };

  return {
    getValueWithoutExponent: function (sValue) {
      return _getValueWithoutExponent(sValue);
    },

    getSplitter: function () {
      return _getSplitter();
    },

    getIconUrl: function (sKey, bIconImageType) {
      return _getIconUrl(sKey, bIconImageType);
    },

    getNodeFromTreeListById: function (aNodeList, sNodeId) {
      return CommonUtils.getNodeFromTreeListById(aNodeList, sNodeId)
    },

    extractInnerTextFromHtml: function (html) {
      var oHtmlDocument = (new DOMParser()).parseFromString(html, "text/html");
      var aHtmlTagList = oHtmlDocument.getElementsByTagName("em");
      var oSearchedList = {};
      CS.forEach(aHtmlTagList, function (oHtmlTag) {
        var sInnerText = oHtmlTag.innerText;
        if(!CS.has(oSearchedList, sInnerText)) {
          oSearchedList[sInnerText] = null;
        }
      });
      return CS.keys(oSearchedList); //aSearchedList;
    },

    isTag: function (sBaseType) {
      return _isTag(sBaseType);
    },

    /** @deprecated **/
    isAttributeTypeType: function (sType) {
      return AttributeUtils.isAttributeTypeType(sType);
    },

    isAttributeTypeDate: function (sType) {
      return AttributeUtils.isAttributeTypeDate(sType);
    },

    isAttributeTypePrice: function (sType) {
      return AttributeUtils.isAttributeTypePrice(sType);
    },

    getDateAttributeInTimeFormat: function (sValue) {
      return CommonUtils.getDateAttributeInTimeFormat(sValue);
    },

    getStandardDateTimeFormat:  function() {
      return CommonUtils.getStandardDateTimeFormat()
    },

    getDateAttributeInDateTimeFormat: function (sValue) {
      return CommonUtils.getDateAttributeInDateTimeFormat(sValue)
    },

    isAttributeTypeMeasurement: function (sType) {
      return AttributeUtils.isAttributeTypeMeasurement(sType);
    },

    isAttributeTypeUser: function (sType) {
      return AttributeUtils.isAttributeTypeUser(sType);
    },

    getLabelByAttributeType: function (sType, sLabel, sDefaultUnit, sPrecision, bDisableFormatByLocale) {
      return AttributeUtils.getLabelByAttributeType(sType, sLabel, sDefaultUnit, sPrecision, bDisableFormatByLocale);
    },

    isImageCoverflowAttribute: function (sAttributeType) {
      return AttributeUtils.isImageCoverflowAttribute(sAttributeType);
    },

    getAttributeTypeForVisual: function(sType, sAttributeId){
      return AttributeUtils.getAttributeTypeForVisual(sType, sAttributeId);
    },

    isAttributeTypeHtml: function (sType) {
      return AttributeUtils.isAttributeTypeHtml(sType);
    },

    isAttributeTypeDescription: function (sType) {
      return AttributeUtils.isAttributeTypeDescription(sType);
    },

    isAttributeTypeName: function (sType) {
      return AttributeUtils.isAttributeTypeName(sType);
    },

    isAttributeTypeText: function (sType) {
      return AttributeUtils.isAttributeTypeText(sType);
    },

    isAttributeTypeCreatedOn: function (sType) {
      return AttributeUtils.isAttributeTypeCreatedOn(sType);
    },

    isAttributeTypeLastModified: function (sType) {
      return AttributeUtils.isAttributeTypeLastModified(sType);
    },

    isAttributeTypeDueDate: function (sType) {
      return AttributeUtils.isAttributeTypeDueDate(sType);
    },

    isAttributeTypeNumber: function (sType) {
      return AttributeUtils.isAttributeTypeNumber(sType);
    },

    isAttributeTypeNumeric: function (sType) {
      return AttributeUtils.isAttributeTypeNumeric(sType);
    },

    isAttributeTypeTelephone: function (sType) {
      return AttributeUtils.isAttributeTypeTelephone(sType);
    },

    isAttributeTypeCoverflow: function (sType) {
      return AttributeUtils.isAttributeTypeCoverflow(sType);
    },

    isAttributeTypeMetadata: function (sType) {
      return AttributeUtils.isAttributeTypeMetadata(sType);
    },

    isMeasurementAttributeTypeCustom: function (sType) {
      return AttributeUtils.isMeasurementAttributeTypeCustom(sType);
    },

    isRoleTypeOwner: function (sType) {
      return AttributeUtils.isRoleTypeOwner(sType);
    },

    isAttributeTypeRole: function (sType) {
      return AttributeUtils.isAttributeTypeRole(sType);
    },

    isAttributeTypeTaxonomy: function (sType) {
      return AttributeUtils.isAttributeTypeTaxonomy(sType);
    },

    isAttributeTypeSecondaryClasses: function (sType) {
      return AttributeUtils.isAttributeTypeSecondaryClasses(sType);
    },

    isAttributeTypeCalculated: function (sType) {
      return AttributeUtils.isAttributeTypeCalculated(sType);
    },

    isAttributeTypeConcatenated: function (sType) {
      return AttributeUtils.isAttributeTypeConcatenated(sType);
    },

    isAttributeTypeFile: function (sType) {
      return AttributeUtils.isAttributeTypeFile(sType);
    },

    isUserTypeAttribute: function (sType) {
      return AttributeUtils.isUserTypeAttribute(sType);
    },

    getUserNameById: function (sUserId, aUserList) {
      return _getUserNameById(sUserId, aUserList);
    },

    getUserById : function(sUserId, aUserList){
      return _getUserById(sUserId, aUserList);
    },

    getUserByUsername : function(sUserId, aUserList){
      return _getUserByUsername(sUserId, aUserList);
    },

    isAssetBaseType: function (sBaseType) {
      return _isAssetBaseType(sBaseType);
    },

    isBaseTypeArticle: function (sBaseType) {
      return _isBaseTypeArticle(sBaseType);
    },

    getImageSrcUrlFromImageObject: function(oImage){
      return _getImageSrcUrlFromImageObject(oImage);
    },

    getTruncatedValue: function (iVal, sPrecision) {
      return AttributeUtils.getTruncatedValue(iVal, sPrecision);
    },

    getMeasurementAttributeValueToShow: function (sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision) {
      return AttributeUtils.getMeasurementAttributeValueToShow(sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision);
    },

    getShortDate: function (iTimeStamp) {
      return CommonUtils.getShortDate(iTimeStamp);
    },

    getTagGroupModels: function (oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags) {
      return CommonUtils.getTagGroupModels(oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags);
    },

    getElementAssetData: function (oElement) {
      return _getElementAssetData(oElement);
    },

    escapeRegexCharacters: function (sString) {
      return sString.replace(new RegExp('[.\\\\+*?\\[\\^\\]$(){}=!<>|:\\-]', 'g'), '\\$&');
    },

    getTagRelevanceForDoubleSlider: function (oTagValue) {
      return _getTagRelevanceForDoubleSlider(oTagValue);
    },

    getRootKlassIdFromBaseType: function (aKlassIds, oReferencedKlasses) {
      return CommonUtils.getRootKlassIdFromBaseType(aKlassIds, oReferencedKlasses);
    },

    getAttributeValueToShow: function (oAttribute, oMasterAttribute) {
      return AttributeUtils.getAttributeValueToShow(oAttribute, oMasterAttribute)
    },

    isFirefox: function () {
      return CommonUtils.isFirefox();
    },

    isXlsOrXlsxFile: function (sExtension) {
      return CommonUtils.isXlsOrXlsxFile(sExtension);
    },

    isObjStpFbxFile: function (sExtension) {
      return CommonUtils.isObjStpFbxFile(sExtension);
    },

    getDecodedTranslation: function (sStringToCompile, oParameter) {
      return CommonUtils.getParsedString(sStringToCompile, oParameter);
    },

    getMomentOfDate: function (sDate) {
      return CommonUtils.getMomentOfDate(sDate);
    },

    getTreeRootId: function () {
      return Constants.TREE_ROOT_ID;
    },

    getContentName: function (oContent) {
      return CommonUtils.getContentName(oContent);
    },

    getSelectedNumberFormatByDataLanguage: function (sDataLanguageId) {
      return CommonUtils.getSelectedNumberFormatByDataLanguage(sDataLanguageId);
    },

    getValueToShowAccordingToNumberFormat: function (sValue, iPrecision, oNumberFormat, bDisableNumberLocaleFormatting) {
      return CommonUtils.getValueToShowAccordingToNumberFormat(sValue, iPrecision, oNumberFormat, bDisableNumberLocaleFormatting);
    },

    getNumberAccordingToPrecision: function (sValue, iPrecision, sDecimalSeparator) {
      return CommonUtils.getNumberAccordingToPrecision(sValue, iPrecision, sDecimalSeparator);
    },

    getCurrentUser: function() {
      return CommonUtils.getCurrentUser();
    },

    getLinkedInstancesFromVariant: function(oVariant){
      return CommonUtils.getLinkedInstancesFromVariant(oVariant);
    },

    getHierarchyDummyNodeId: function () {
      return CommonUtils.getHierarchyDummyNodeId();
    },

    getParentNodeByChildId: function (aNodeList, sChildId, oParentNode) {
      return CommonUtils.getParentNodeByChildId(aNodeList, sChildId, oParentNode)
    },

    getMinValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return CommonUtils.getMinValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getMaxValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return CommonUtils.getMaxValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getLeafNodeSubbedWithParentInfo: function (aTreeNode, sParentPropertyName, sChildPropName) {
      return CommonUtils.getLeafNodeWithSubbedWithParentInfo(aTreeNode, sParentPropertyName, sChildPropName);
    },

    getClassNameAsPerRelevanceValue: function (sRelevanceValue) {
      return CommonUtils.getClassNameAsPerRelevanceValue(sRelevanceValue);
    },

    getTextColorBasedOnBackgroundColor: function (sBackgroundColorHex) {
      return CommonUtils.getTextColorBasedOnBackgroundColor(sBackgroundColorHex);
    },

    getRoundedTagRelevanceValueByTagType: function (iRelevance, sTagType) {
      return TagUtils.getRoundedTagRelevanceValueByTagType(iRelevance, sTagType);
    },

    scrollBy: function (oDom, oStyles, sEasing, fCallback) {
      return CommonUtils.scrollBy(oDom, oStyles, sEasing, fCallback)
    },

    scrollTo: function (oDom, oStyles, sEasing, fCallback) {
      return CommonUtils.scrollTo(oDom, oStyles, sEasing, fCallback)
    },

    getIsCurrentUserReadOnly: function () {
      return CommonUtils.isCurrentUserReadOnly();
    },
  };

})();

export default ViewLibraryUtils;
