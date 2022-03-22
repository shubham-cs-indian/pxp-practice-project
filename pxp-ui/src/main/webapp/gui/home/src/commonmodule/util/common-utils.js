import CS from './../../libraries/cs';
import React from 'react';

import alertify from '../../commonmodule/store/custom-alertify-store';
import SessionStorageManager from './../../libraries/sessionstoragemanager/session-storage-manager';
import PhysicalCatalogDictionary from './../../commonmodule/tack/physical-catalog-dictionary';
import ClassRootBaseTypeIdDictionary from './../../commonmodule/tack/class-root-basetype-id-dictionary';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import Constants from './../tack/constants';
import SharableURLStore from './../../commonmodule/store/helper/sharable-url-store';
import RequestMapping from '../../libraries/requestmappingparser/request-mapping-parser.js';
import CommonModuleRequestMapping from '../tack/common-module-request-mapping';
import LogFactory from '../../libraries/logger/log-factory';
import MethodTracker from '../../libraries/methodtracker/method-tracker';
import MomentUtils from './moment-utils';
import NumberUtils from './number-util';
import GlobalStore from './../../screens/homescreen/store/global-store';
import SessionProps from './../../commonmodule/props/session-props';
import SessionStorageConstants from '../tack/session-storage-constants';
import CustomActionDialogStore from '../../commonmodule/store/custom-action-dialog-store';
import TagTypeConstants from "../tack/tag-type-constants";
import HierarchyTypesDictionary from "../tack/hierarchy-types-dictionary";
import UniqueIdentifierGenerator from "../../libraries/uniqueidentifiergenerator/unique-identifier-generator";
import TagUtils from "./tag-utils";
import TagGroupModel from "../../viewlibraries/taggroupview/model/tag-group-model";
var aDefaultNamesOfAllLanguages = ["Neues", "Untitled", "New"];
const logger = LogFactory.getLogger('content-screen-utils');
const trackMe = MethodTracker.getTracker('content-screen-utils');

var CommonUtils = (function () {

  var _getSplitter = function(){
    return "#$%$#";
  };

  const sNewSuffix = "#$%$#new";

  var _getNewSuffix = function () {
    return sNewSuffix;
  };

  let _listModeConfirmation = (sHeader, aArticleNames, oOkCallback, oCancelCallback, sOkText, sCancelText) => {
    var oCallbackOk = function () {
      if (CS.isFunction(oOkCallback)) {
        oOkCallback();
      }
    };
    var oCallbackCancel = function (oEvent) {
      if (CS.isFunction(oCancelCallback)) {
        oCancelCallback();
      }
    };
    CustomActionDialogStore.showListModeConfirmDialog(sHeader || '', aArticleNames, oCallbackOk, oCallbackCancel);
  };

  var _getFilteredName = function (sValue) {
    CS.forEach(aDefaultNamesOfAllLanguages, function (oDefaultName) {
      if(sValue.startsWith(oDefaultName)){
        sValue='';
      }
    });

    return sValue;
  };

  var _getClassNameAsPerRelevanceValue = function (iRelevanceValue) {
    if (!CS.isNumber(iRelevanceValue)) {
      return 'thumbnailRelevanceDarkGray';
    }
    else if(iRelevanceValue <= -60){
      return 'thumbnailRelevanceRed'
    } else if(iRelevanceValue > -60 && iRelevanceValue <= -20){
      return 'thumbnailRelevanceOrange';
    } else if(iRelevanceValue > -20 && iRelevanceValue <= 20){
      return 'thumbnailRelevanceGray';
    }  else if(iRelevanceValue > 20 && iRelevanceValue <= 60){
      return 'thumbnailRelevanceYellow';
    } else if(iRelevanceValue > 60){
      return 'thumbnailRelevanceGreen';
    }
  };

  var _isCurrentUserAdmin = function () {
    var oCurrentUser = GlobalStore.getCurrentUser();
    return oCurrentUser.id == "admin";
  };

  let _isCurrentUserSystemAdmin = function () {
    return GlobalStore.getCurrentUser().roleType === "SystemAdmin";
  };

  let _isCurrentUserReadOnly = function () {
    return GlobalStore.getCurrentUser().isReadOnly;
  };

  let _isDataIntegrationAllowedForCurrentUser = () => {
    let oCurrentUser = GlobalStore.getCurrentUser();
    let aAllowedPhysicalCatalogs = oCurrentUser.allowedPhysicalCatalogIds;
    return CS.includes(aAllowedPhysicalCatalogs, PhysicalCatalogDictionary.DATAINTEGRATION);
  };

  let _isOnlyDataIntegrationEnabled = () => {
    let oCurrentUser = GlobalStore.getCurrentUser();
    let aAllowedPhysicalCatalogs = oCurrentUser.allowedPhysicalCatalogIds;
    return (aAllowedPhysicalCatalogs.length == 1) && (aAllowedPhysicalCatalogs[0] == PhysicalCatalogDictionary.DATAINTEGRATION);
  };

  var _getParsedString = function (sStringToCompile, oParameter) {
    var compiled = CS.template(sStringToCompile);
    return compiled(oParameter);
  };

  let _getContentScreenDependency = function () {
    var oGlobalModulesData = GlobalStore.getGlobalModulesData();
    var aScreens = oGlobalModulesData.screens;

    return {
      store: require('../../screens/homescreen/screens/contentscreen/store/content-screen-store').default,
      action: require('../../screens/homescreen/screens/contentscreen/action/content-screen-action').default,
      controller: React.lazy(() => import('../../screens/homescreen/screens/contentscreen/controller/content-screen-controller').default),
      screens: aScreens //TODO: Need to change for endpoint module screens [File, Process Status]...
    }
  };

  let _getDashboardScreenDependency = function () {

    return {
      store: require('../../screens/homescreen/screens/contentscreen/screens/dashboardscreen/store/dashboard-screen-store').default,
      action: require('../../screens/homescreen/screens/contentscreen/screens/dashboardscreen/action/dashboard-screen-action').default,
      controller: require('../../screens/homescreen/screens/contentscreen/screens/dashboardscreen/controller/dashboard-screen-controller').default,
    }
  };

  let _getUserFullName = function (oUser) {
    let sName = oUser.lastName ? oUser.lastName + " " : "";
    sName += oUser.firstName ? oUser.firstName : "";
    return sName;
  };

  let _isValidRegex = function (sRegex) {
    var isValidRegex = true;
    try {
      new RegExp(sRegex);
    } catch(e) {
      isValidRegex = false;
    }
    return isValidRegex;
  };

  let _postRequest = (sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader) => {
    return CS.customPostRequest(RequestMapping.getRequestUrl(sUrl, oQueryString), JSON.stringify(oRequestData), fSuccess, fFailure, bDisableLoader);
  };

  let _getRequest = function (sUrl, oParameters, fSuccess, fFailure, bDisableLoader) {
    return CS.customGetRequest(RequestMapping.getRequestUrl(sUrl, oParameters), {}, fSuccess, fFailure, bDisableLoader);
  };

  let _getRootKlassIdFromBaseType = (aKlassIds, oReferencedKlasses) => {
    let sRootKlassId = "";

    try {
      if (!CS.isEmpty(aKlassIds)) {
        let sKlassId = aKlassIds[0];
        let oReferencedKlass = oReferencedKlasses[sKlassId];
        sRootKlassId = ClassRootBaseTypeIdDictionary[oReferencedKlass.type];
      }
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }

    return sRootKlassId;
  };

  let _showError = function  (sMessage) {
    alertify.error(sMessage, 0);
  };

  let _showSuccess = function (sMessage) {
    alertify.success(sMessage);
  };

  let _showMessage = function (sMessage) {
    alertify.message(sMessage);
  };

  let _showException = function (sKey, aData, oTranslations) {
    let sMessage = oTranslations.ERROR_CONTACT_ADMINISTRATOR;
    if (CS.isFunction(oTranslations)) {
      oTranslations = oTranslations();
    }
    if (!CS.isEmpty(sKey) && !CS.isEmpty(oTranslations[sKey])) {
      sMessage = oTranslations[sKey] + (aData.length == 0 ? "" : " : [" + aData.join(",") + "]");
    }
    _showError(sMessage);
  };

  let _failureCallback = function (oResponse, sFunctionName, oTranslations) {
    trackMe(sFunctionName);
    if (!CS.isEmpty(oResponse.failure)) {
      if (CS.isFunction(oTranslations)) {
        oTranslations = oTranslations();
      }
      var aResponseData = oResponse.failure.exceptionDetails;
      if(aResponseData.length !== 0){
        var oExceptions = {};
        CS.forEach(aResponseData, function (oData) {
          var sKey = oData.key;
          if (CS.isEmpty(oExceptions[sKey])) {
            oExceptions[sKey] = [];
          }
          if (!CS.isEmpty(oData.itemName)) {
            // oExceptions[sKey] += (oExceptions[sKey] == "" ? oData.itemName : ("," + oData.itemName));
            oExceptions[sKey].push(oData.itemName);
          }
        });
        CS.forEach(oExceptions, function (aData, sKey) {
          _showException(sKey, aData, oTranslations);
        });
      } else {
        _showError(oTranslations["ERROR_CONTACT_ADMINISTRATOR"]);
      }
    }
    else {
      ExceptionLogger.error("Something went wrong in " + sFunctionName);
      ExceptionLogger.log(oResponse);
      logger.error("Something went wrong in " + sFunctionName, oResponse);
    }
  };

  let _getNodeFromTreeListById = (aNodeList, sNodeId) => {
    let oFoundNode = {};
    CS.forEach(aNodeList, function (oNode) {

      if (oNode.id == sNodeId) {
        oFoundNode = oNode;
        return false;
      } else {

        if (oNode.children) {
          oFoundNode = _getNodeFromTreeListById(oNode.children, sNodeId);
          if (!CS.isEmpty(oFoundNode)) {
            return false;
          }
        }

      }
    });

    return oFoundNode;
  };

  let _getNodePathIdsFromTreeById = (aNodeList, sNodeId) => {
    let aIdsListToReturn = [];
    CS.forEach(aNodeList, function (oNode) {
      aIdsListToReturn.push(oNode.id);
      if (oNode.id == sNodeId) {
        return false;
      } else {
        if (!CS.isEmpty(oNode.children)) {
          let aIds = _getNodePathIdsFromTreeById(oNode.children, sNodeId);
          if (!CS.isEmpty(aIds)) {
            aIdsListToReturn.push.apply(aIdsListToReturn, aIds);
            return false;
          }
        }
      }
      aIdsListToReturn.pop();
    });
    return aIdsListToReturn;
  };

  let _getAllIdsInTree = (aNodeList) => {
    let aIdsListToReturn = [];

    CS.forEach(aNodeList, function (oNode) {
      aIdsListToReturn.push(oNode.id);
      if (!CS.isEmpty(oNode.children)) {
        let aIds = _getAllIdsInTree(oNode.children);
        aIdsListToReturn.push.apply(aIdsListToReturn, aIds);
      }
    });

    return aIdsListToReturn;
  };

  let _getAddedClassificationList = (oParent, aSelectedClassification) => {
    if (oParent.id == Constants.TREE_ROOT_ID) {
      return;
    }
    !CS.isEmpty(oParent.parent) && _getAddedClassificationList(oParent.parent, aSelectedClassification);
    aSelectedClassification.push(oParent);
  };

  let _getMultiTaxonomyData = (aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree, oAllowedTaxonomyHierarchyList = {}) => {
    let aRemainingTaxonomyList = [];

    CS.forEach(aTaxonomyTree, function (oTaxonomy) {
      if (oTaxonomy.id != Constants.TREE_ROOT_ID) {
        if (!CS.includes(aSelectedTaxonomyIds, oTaxonomy.id)) {
          aRemainingTaxonomyList.push(oTaxonomy);
        }
      }
    });
    var aSelectedTaxonomy = [];
    CS.forEach(aSelectedTaxonomyIds, function (sTaxonomyId) {
      var oRTaxonomy = oReferencedTaxonomiesMap[sTaxonomyId];
      var aSelectedClassification = [];
      if (!CS.isEmpty(oRTaxonomy)) {
        _getAddedClassificationList(oRTaxonomy, aSelectedClassification);
        var oSelectedTaxonomy = {};
        oSelectedTaxonomy.id = sTaxonomyId;
        oSelectedTaxonomy.addedClassificationList = aSelectedClassification;
        oSelectedTaxonomy.furtherClassificationList = aRemainingTaxonomyList;
        aSelectedTaxonomy.push(oSelectedTaxonomy);
      }
    });

    return {selected: aSelectedTaxonomy, notSelected: aRemainingTaxonomyList, allowedTaxonomyHierarchyList: oAllowedTaxonomyHierarchyList};
  };

  let _preserveOrderedDataInArray = function (aOldData, aNewData, sDifferenceBy) {
    let aProcessedData = [];
    if(!CS.isEmpty(aOldData)){
      CS.forEach(aOldData, function (oldData) {
        CS.forEach(aNewData, function (newData) {
          if(sDifferenceBy){
            if(oldData[sDifferenceBy] === newData[sDifferenceBy]){
              aProcessedData.push(newData);
            }
          } else {
            if(oldData === newData){
              aProcessedData.push(newData);
            }
          }
        })
      });
    }
    if(sDifferenceBy) {
      return CS.uniqBy(CS.concat(aProcessedData, CS.differenceBy(aNewData, aProcessedData, sDifferenceBy)), sDifferenceBy);
    } else {
      return CS.uniq(CS.concat(aProcessedData, CS.difference(aNewData, aProcessedData)));
    }
  };

  let _getEntityAttributeValueByAttributeId = function (oContent, sProperty) {
    let aAttribute = oContent.attributes;
    let oPropertyAttribute = CS.find(aAttribute, {attributeId : sProperty});
    if(!CS.isEmpty(oPropertyAttribute)) {
      return oPropertyAttribute.value;
    }
    return null;
  };

  let _getContentName = function (oContent) {
    let sNameAttributeValue = _getEntityAttributeValueByAttributeId(oContent, "nameattribute");

    if(!CS.isEmpty(sNameAttributeValue)) {
      return sNameAttributeValue;
    }
    return oContent.name || oContent.label;
  };

  let _removeTrailingBreadcrumbPath = function (sId, sType) {
    let oBreadCrumbStore = _getBreadCrumbStore();
    return oBreadCrumbStore.removeTrailingBreadcrumbPath(sId, sType);
  };

  var _getBreadCrumbStore = function () {
    return require('../../commonmodule/store/helper/breadcrumb-store').default;
  };

  var _removeQuickListBreadCrumbFromPath = function () {
    let oBreadCrumbStore = _getBreadCrumbStore();
    oBreadCrumbStore.removeQuickListBreadCrumbFromPath();
  };

  let _setSelectedDataLanguage = function(sSelectedLanguageCode) {
    //SessionProps.setDataLanguageId(sSelectedLanguageCode);
    SessionStorageManager.setPropertyInSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE, sSelectedLanguageCode);
    MomentUtils.setMomentLocale();
    MomentUtils.setCurrentDateFormat();
    NumberUtils.setCurrentNumberFormat();
  };

  let _getDataLanguageCodeById = function (sLanguageId) {
    let aDataLanguages = SessionProps.getLanguageInfoData().dataLanguages;
    let oSelectedDataLanguage = CS.find(aDataLanguages, {id: sLanguageId});
    return oSelectedDataLanguage.code;
  };

  var _getLuminanceOfColor = function (sColorHex) {
    var sColor = sColorHex.substring(1);      // strip #
    var rgb = parseInt(sColor, 16);   // convert rrggbb to decimal
    var r = (rgb >> 16) & 0xff;  // extract red
    var g = (rgb >> 8) & 0xff;  // extract green
    var b = (rgb >> 0) & 0xff;  // extract blue

    return (0.2126 * r + 0.7152 * g + 0.0722 * b); // per ITU-R BT.709
  };

  var _getTextColorBasedOnBackgroundColor = function (sBackgroundColorHex) {
    if (_getLuminanceOfColor(sBackgroundColorHex) > 128) {
      return "#333"; //if background is bright
    } else {
      return "#fff"; //if background is dark
    }
  };

  let _getLinkedInstancesFromVariant = function(oVariant){
    let oLinkedInstances = oVariant.linkedInstances || {};
    if(CS.isEmpty(oLinkedInstances) && oVariant.context){
      oLinkedInstances = oVariant.context.linkedInstances || {};
    }
    return oLinkedInstances;
  };

  var _getParentNodeByChildId = function (aNodeList, sChildId, oParentNode) {
    var oGotParent = {};
    CS.forEach(aNodeList, function (oNode) {
      if (oNode.id == sChildId) {
        oGotParent = oParentNode;
        return false;
      } else {
        oGotParent = _getParentNodeByChildId(oNode.children, sChildId, oNode);
        if (!CS.isEmpty(oGotParent)) {
          return false;
        }
      }
    });
    return oGotParent;
  };

  let _convertReferencedInObjectFormat = function (oReferencedData) {
    let oNewReferencedData = {};
    CS.forEach(oReferencedData, function (sValue, sKey) {
      oNewReferencedData[sKey] = {
        id: sKey,
        label: sValue
      }
    });
    return oNewReferencedData;
  };

  var _getMinValueFromListByPropertyName = function (aList, sPropertyName) {
    var oMin = CS.minBy(aList, sPropertyName);
    return oMin ? oMin[sPropertyName] : 0;
  };

  var _getMaxValueFromListByPropertyName = function (aList, sPropertyName) {
    var oMax = CS.maxBy(aList, sPropertyName);
    return oMax ? oMax[sPropertyName] : 0;
  };

  var _getLeafNodeWithSubbedWithParentInfo = function (aTreeNode, sParentPropertyName, sChildPropName) {
    var sSplitter = _getSplitter();
    var aChildNodes = [];
    var sCustomField = 'custom' + sSplitter + sParentPropertyName;
    CS.forEach(aTreeNode, function (oNode) {
      var aChildren = oNode[sChildPropName];
      if(CS.isEmpty(aChildren)) {
        oNode[sCustomField] = oNode[sParentPropertyName];
        aChildNodes.push(oNode);
      } else {
        var aLeafNodes = _getLeafNodeWithSubbedWithParentInfo(aChildren, sParentPropertyName, sChildPropName, aChildNodes);
        CS.forEach(aLeafNodes, function (oLeafNode) {
          var aCustomFieldValues = oLeafNode[sCustomField].split('/');
          aCustomFieldValues.unshift(oNode[sParentPropertyName]);
          oLeafNode[sCustomField] = aCustomFieldValues.join('/');
          aChildNodes.push(oLeafNode);
        });
      }
    });
    return aChildNodes;
  };

  let _getCodesById = function (aDataModel, aId) {
    let aCode = [];
    CS.forEach(aDataModel, (oModel) => {
      if(CS.includes(aId, oModel.id)) {
        aCode.push(oModel.code);
      }
    });
    return aCode;
  };

  let _getIdsByCode = function (aDataModel, aCode) {
    let aId = [];
    CS.forEach(aDataModel, (oModel) => {
      if(CS.includes(aCode, oModel.code)) {
        aId.push(oModel.id);
      }
    });
    return aId;
  };

  var _getIconUrl = function (sKey) {
    return RequestMapping.getRequestUrl(CommonModuleRequestMapping.GetAssetImage, {type: "Icons", id: sKey}) + "/";
  };

  let _refreshCurrentBreadcrumbEntity = function () {
    let oBreadCrumbStore = _getBreadCrumbStore();
    oBreadCrumbStore.refreshCurrentBreadcrumbEntity();
  };

  /**
   * @Router Impl
   * @function _setHistoryStateToNull
   * @description Reset history state to the starting of the application
   * @private
   */
  let _setHistoryStateToNull = function (fCallBack, oPreviousHistoryState) {
    let oHistoryState = CS.getHistoryState();

    if (oHistoryState && oHistoryState.id) {
      if (!oPreviousHistoryState || (oPreviousHistoryState && oPreviousHistoryState.id !== oHistoryState.id)) {
        oPreviousHistoryState = oHistoryState;

        SharableURLStore.setIsEntityNavigation(false);
        CS.navigateBack(_setHistoryStateToNull.bind(this, fCallBack, oPreviousHistoryState));
      }
      else if (oPreviousHistoryState && oPreviousHistoryState.id === oHistoryState.id) {
        console.log("oHistoryState: " + oHistoryState.id);
        oPreviousHistoryState && console.log("oPreviousHistoryState: " + oPreviousHistoryState.id);
        setTimeout(_setHistoryStateToNull.bind(this, fCallBack, oPreviousHistoryState), 10);
      }
    }
    else {
      fCallBack && fCallBack();
    }
  };

  /**
   * @function  applyStyleToDOMByClassNameOnGridViewMount() will be responsible for applying style to all grid row doms when gridview mount.
   */
  let _applyStyleToDOMByClassNameOnGridViewMount = function(oTargetDom, sClassName, sProperty, sValue){
    let aDOMs = oTargetDom.getElementsByClassName(sClassName);
    let oCentralScrollableSectionDOM = CS.find(aDOMs, (oDom)=> {
      if (oDom.offsetParent) {
        return oDom.offsetParent.className === "centralScrollableSection"
      }
    }) || null;
    let sHeightOfCentralDOM = oCentralScrollableSectionDOM && oCentralScrollableSectionDOM.offsetHeight + "px" || null;

    CS.forEach(aDOMs, function (oDom) {
      if (oDom.offsetParent) {
        if(sProperty === "height" && oDom.offsetParent.className !== "centralScrollableSection") {
          oDom.style[sProperty] = sHeightOfCentralDOM;
        }
        else if(sProperty === "left"){
          oDom.style[sProperty] = sValue;
        }
      }
    });
  };

  let _calculateTop = function(oDom, oStyles, sFromFunction){
    let iCalculatedTop = 0;
    if(oStyles.hasOwnProperty("top")){
      iCalculatedTop = oStyles.top;
    }else if(oStyles.hasOwnProperty("bottom")){
      iCalculatedTop = Math.round(parseFloat(getComputedStyle(oDom).height)) - oStyles.bottom;
    }else if (sFromFunction === 'scrollTo' && oStyles.hasOwnProperty("scrollTop")) {
      iCalculatedTop = oStyles.scrollTop;
    }
    return iCalculatedTop;
  };

  let _calculateLeft = function(oDom, oStyles){
    let iCalculatedLeft = 0;
    if(oStyles.hasOwnProperty("left")){
      iCalculatedLeft = oStyles.left;
    }else if(oStyles.hasOwnProperty("right")){
      iCalculatedLeft = Math.round(parseFloat(getComputedStyle(oDom).width)) - oStyles.right;
    }
    return iCalculatedLeft;
  };

  /**
   *  @function animate is constructed in pure javascript in a way to replace the jquery.animate()
   * */
  let _scrollTo = function(oDom, oStyles, sEasing, fCallback){

    /**
     *  Note: As we are only supporting scrolling functions while animating, only
     *  top, bottom, right, left properties are allowed
     * */

    let calculatedTop = _calculateTop(oDom, oStyles, "scrollTo");
    let calculatedLeft = _calculateLeft(oDom, oStyles);
    let behavior = oStyles.behavior || 'smooth';


    oDom.scrollTo({
      top: calculatedTop,
      left: calculatedLeft,
      behavior: behavior
    });

    // $(oDom).animate({left: oStyles.left});
    /**
     * iSpeed is Optional. Specifies the speed of the animation. Default value is 400 milliseconds

     Possible values:
     milliseconds (like 100, 1000, 5000, etc)
     "slow"
     "fast"
     */

    // _getAnimationSpeed(oDom, iSpeed);

    /**
     * sEasing is Optional. Specifies the speed of the element in different points of the animation. Default value is "ease". Possible values:
     "ease" - moves slower at the beginning/end, but faster in the middle
     "linear" - moves in a constant speed
     */
    oDom.style.transitionTimingFunction =  sEasing || 'ease';

    if(fCallback){
      // let fFun = () => {
        setTimeout(fCallback, 300)
      // }
      // fFun();
    }
  };

  let _scrollBy = function(oDom, oStyles, sEasing, fCallback){

    /**
     *  Note: As we are only supporting scrolling functions while animating, only
     *  top, bottom, right, left properties are allowed
     * */

    let calculatedTop = _calculateTop(oDom, oStyles);
    let calculatedLeft = _calculateLeft(oDom, oStyles);
    let behavior = oStyles.behavior || 'smooth';


    oDom.scrollBy({
      top: calculatedTop,
      left: calculatedLeft,
      behavior: behavior
    });

    // $(oDom).animate({left: oStyles.left});
    /**
    * iSpeed is Optional. Specifies the speed of the animation. Default value is 400 milliseconds

      Possible values:
        milliseconds (like 100, 1000, 5000, etc)
        "slow"
        "fast"
    */

    // _getAnimationSpeed(oDom, iSpeed);

    /**
     * sEasing is Optional. Specifies the speed of the element in different points of the animation. Default value is "ease". Possible values:
         "ease" - moves slower at the beginning/end, but faster in the middle
         "linear" - moves in a constant speed
    */
    oDom.style.transitionTimingFunction =  sEasing || 'ease';

    if(fCallback){
      // let fFun = () => {
        setTimeout(fCallback, 450)
      // }
      // fFun();
    }
  };

  /**
   * @function _sortTagSequence
   * @description - To sort tag value by sequence.
   * @param oTag - tag instance
   * @param aTagValuesSequence - Contains tag value ids with proper sequence.
   * @private
   */
  let _sortTagSequence = function (oTag, aTagValuesSequence) {
    let aTagValues = oTag.tagValues;
    let aSequencedTags = [];
    CS.forEach(aTagValuesSequence, function (sId) {
      let aTags = CS.remove(aTagValues, {tagId: sId});
      if (!CS.isEmpty(aTags)) {
        aSequencedTags.push(aTags[0]);
      }
    });

    aSequencedTags.push.apply(aSequencedTags, aTagValues);
    oTag.tagValues = aSequencedTags;
  };

  /**
   * @function _getTagViewTypeByTagType
   * @description To get tag view type by tag type.
   * @param sTagType - tag type
   * @return {string}
   */
  let _getTagViewTypeByTagType = function (sTagType, sModuleContext) {
    switch (sTagType) {
      case TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE:
        return 'radioGroup';
      case TagTypeConstants.RANGE_TAG_TYPE:
      case TagTypeConstants.CUSTOM_TAG_TYPE:

        if (sModuleContext === "contentFilterTagGroupView") {
          return 'doubleSlider';
        }
        else {
          return 'slider';
        }
      case TagTypeConstants.TAG_TYPE_BOOLEAN:
        return 'switch';
      case TagTypeConstants.RULER_TAG_TYPE:
        if (sModuleContext !== "contentFilterTagGroupView" && sModuleContext!== "ruleDetailView") {
          return 'ruler';
        }
      case TagTypeConstants.LISTING_STATUS_TAG_TYPE:
      case TagTypeConstants.STATUS_TAG_TYPE:
      case TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE:
        if (sModuleContext !== "contentFilterTagGroupView" && sModuleContext!== "ruleDetailView" && sModuleContext !== "appliedTagElement") {
          return 'ruler';
        }
      case TagTypeConstants.YES_NEUTRAL_TAG_TYPE:
      case TagTypeConstants.TAG_TYPE_MASTER:
      default:
        return 'multiSelect';
    }
  };

  let _getEntityTag = function (oMasterTag, oEntity, oElement, oHierarchyData, sModuleContext) {
    if (oHierarchyData &&
        (oHierarchyData.hierarchyContext === HierarchyTypesDictionary.COLLECTION_HIERARCHY || oHierarchyData.hierarchyContext === HierarchyTypesDictionary.TAXONOMY_HIERARCHY)) {
      //TODO: Check if cloning needed
      let oClonedElement = CS.cloneDeep(oElement);
      oClonedElement.tagId = oElement.propertyId;
      oClonedElement.tagValues = oElement.defaultValue;
      return oClonedElement;
    }
    else {
      let aEntityTags = oEntity.tags;
      return CS.find(aEntityTags, {tagId: oMasterTag.id}) || {};
    }
  };

  let getTagModelContext = function (sModuleContext, oHierarchyData, oExtraData, oMasterTag, oElement) {
    let sSplitter = _getSplitter();
    switch (sModuleContext) {
      case "content":
        return "contentTag";
      case "variant":
        return "contentTagVariantStatus";
      case "versionContent":
        return "versionContent";
      case "hierarchy":
        return oHierarchyData.hierarchyContext + sSplitter + oHierarchyData.sectionId + sSplitter + oHierarchyData.elementId; // TODO: Remove splitter
      case "goldenRecordComparison":
        return "goldenRecordComparisonTag";
      case "contentFilterTagGroupView":
        return oExtraData.context || oExtraData.outerContext;
      case "ruleDetailView":
        return "ruleFilterTagsTagValuesForNormalization" + sSplitter + oMasterTag.id;
      case  "classConfigTagDefaultValue":
        return"classConfigTagDefaultValue" + sSplitter + oExtraData.sectionId + sSplitter + oElement.id;
      case "classConfigSelectedTagValues":
        return"classConfigSelectedTagValues" + sSplitter + oExtraData.sectionId + sSplitter + oElement.id;
      case "appliedTagElement":
        return sModuleContext;
      case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
        return sModuleContext + sSplitter + oExtraData.sectionId + sSplitter + oElement.id;
      case "uomVariants" :
        return sModuleContext;
      case "gridViewTag":
        return sModuleContext;
    }
  };

  let _getTagValueRange = function (aMasterTagValues) {
    return {
      minValue: CommonUtils.getMinValueFromListByPropertyName(aMasterTagValues, 'relevance'),
      maxValue: CommonUtils.getMaxValueFromListByPropertyName(aMasterTagValues, 'relevance')
    }
  };

  /**
   * @function _getTagGroupModels
   * @description Creating tagGroupViewModel to render tags.
   * @param oMasterTag
   * @param oEntity
   * @param oElement
   * @param sModuleContext
   * @param oHierarchyData
   * @param oFilterContext
   * @param oExtraData - Contains outerContext, innerContext etc.
   * @param aFilterTagValues - Contains Applied filter tag values. Required in case of contentFilterTagGroupView & RuleDetailview.
   * @return {{masterTagId: *, code: *, disabled: *, label: *, singleSelect: boolean, context: string, tagGroupModel: TagGroupModel, tagRanges, tagValues: *, isDoubleSlider: boolean, extraData}}
   * @private
   */
    //TODO: TagGroupModelRefactoring - Pass EntityTag as a parameter instead of activeEntity(only Tag instance is required).
  let _getTagGroupModels = function (oMasterTag, oEntity, oElement = {}, sModuleContext, oHierarchyData, oFilterContext, oExtraData = {}, aFilterTagValues) {
    let sTagViewType = _getTagViewTypeByTagType(oMasterTag.tagType, sModuleContext);
    let sContext = getTagModelContext(sModuleContext, oHierarchyData, oExtraData, oMasterTag, oElement);
    let bIsDoubleSlider = sTagViewType === "doubleSlider";
    let oEntityTag = _getEntityTag(oMasterTag, oEntity, oElement, oHierarchyData, sModuleContext);
    let aChildTagModels = [];


    /**prepare extra data for tagGroupModel */
    if (sModuleContext !== "contentFilterTagGroupView") {
      oExtraData.outerContext = sModuleContext;
      oExtraData.tagId = oMasterTag.id;
    }

    /**Prepare tag value properties */
    let oTagValuesProperties = {};
    CS.forEach(oEntityTag.tagValues, function (oTagValue) {
      let oProperties = {};
      let bIsLifeCycle = oMasterTag.tagType === TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE;

      /**
       * Required for lifecycle tag.
       * In case of lifecycle tag we are disabling some tag values according selected value.
       * Here only allowed values are enabled, all other tag values are disabled.
       */
      if (bIsLifeCycle) {
        let oSelectedTagMasterValue;
        let oMasterTagValue = CS.find(oMasterTag.children, {id: oTagValue.tagId});
        let oSelectedTagValue = CS.find(oEntityTag.tagValues, {relevance: 100});
        oSelectedTagValue && (oSelectedTagMasterValue = CS.find(oMasterTag.children, {id: oSelectedTagValue.tagId}));
        if (oSelectedTagMasterValue) {
          oProperties.isDisabled = !CS.includes(oSelectedTagMasterValue.allowedTags, oMasterTagValue.id);
        }
        oTagValuesProperties[oTagValue.id] = oProperties;
      }
    });

    /**
     * Required for contentFilterTagGroupView(advance search, rule config) and RuleDetailView.
     * when applying advance filter, Tag values are not present.
     * Need to create dummy tag values and add it into entityTag using aFilterTagValues.
     */
    if (sModuleContext === "contentFilterTagGroupView" || sModuleContext === "ruleDetailView") {
      let aChildOfMasterTags = oMasterTag.children;
      CS.forEach(aChildOfMasterTags, function (oChildTag) {
        let oEntityTagFilterValue = CS.find(aFilterTagValues, {'id': oChildTag.id});
        if (oEntityTagFilterValue) {
          let oEntityTagValue = {
            id: UniqueIdentifierGenerator.generateUUID(),
            tagId: oEntityTagFilterValue.id,
            versionId: null,
            versionTimestamp: null,
            lastModifiedBy: null
          };
          if (bIsDoubleSlider) {
            oEntityTagValue.from = oEntityTagFilterValue.from;
            oEntityTagValue.to = oEntityTagFilterValue.to;
          } else {
            oEntityTagValue.relevance = oEntityTagFilterValue.from;
          }

          oEntityTag.tagValues.push(oEntityTagValue);
        }
      });
    }

    /** now tag values are getting rendered through entity tag so sorting required for entity tag values, not for referenced tag values */
    _sortTagSequence(oEntityTag, oMasterTag.tagValuesSequence);

    let bIsSingleSelect = CS.isNotEmpty(oElement.isMultiselect) ? !oElement.isMultiselect : !oMasterTag.isMultiselect ;

    let oProperties = {
      context: sContext,
      tagGroupId: oEntityTag.id,
      entityId: oEntity.id,
      tagViewType: sTagViewType,
      tagValuesProperties: oTagValuesProperties,
      singleSelect: bIsSingleSelect,
      extraData: oExtraData
    };

    /**Required for sliderView & doubleSliderView, to show the tag values range*/
    let aMasterTagValues = TagUtils.getTagValuesForTagType(oMasterTag.tagType);
    if (sTagViewType === "slider" || sTagViewType === "doubleSlider") {
      oProperties.tagRanges = _getTagValueRange(aMasterTagValues);
    }

    let oTagGroupModel = new TagGroupModel(oEntityTag.id, oMasterTag.id, CS.getLabelOrCode(oMasterTag), oEntityTag, 0, 0, oProperties, oMasterTag.iconKey);
    oTagGroupModel.tooltip = oMasterTag.tooltip;

    //TODO: Check uses
    let aCustomTagValuesForRadio = _createTagValues(aMasterTagValues);


    //TODO: check props usages
    return {
      disabled: oElement.isDisabled, //Used in TagGroupView
      tagGroupModel: oTagGroupModel,
      tagValues: aCustomTagValuesForRadio,
      isDoubleSlider: bIsDoubleSlider, //TODO:TagGroupModelRefactoring remove uses. Required for contentFilterTagGroupView.
      extraData: oExtraData //TODO:TagGroupModelRefactoring remove uses. Required for contentFilterTagGroupView.
    };
  };

  let _createTagValues = function (aTagValues) {
    return CS.map(aTagValues, function (oTagValue) {
      return {
        id: oTagValue.relevance,
        label: oTagValue.label
      }
    });
  };

  let _createDummyEntityTagForTagGroupModel = function (oMasterTag) {
    return {
      tagValues: [],
      tagId: oMasterTag.id
    }
  };

  let _base64ToArrayBuffer = function (base64) {
    let sBinaryString = window.atob(base64);
    let iBinaryLen = sBinaryString.length;
    let aBytes = new Uint8Array(iBinaryLen);
    for (let i = 0; i < iBinaryLen; i++) {
      let iAscii = sBinaryString.charCodeAt(i);
      aBytes[i] = iAscii;
    }
    return aBytes;
  };

  let _downloadFromByteStream = function (oByteStream, sFileName) {
    let aBuffer = _base64ToArrayBuffer(oByteStream);
    let oTempElement = window.document.createElement('a');
    document.body.appendChild(oTempElement);
    let oBlob = new Blob([aBuffer], {type: "octet/stream"});
    oTempElement.href = window.URL.createObjectURL(oBlob);
    oTempElement.download = sFileName;
    oTempElement.click();
    document.body.removeChild(oTempElement);
  };

  let _prepareTaxonomyHierarchyData = function (aAllowedTaxonomies, oConfigDetails, sParentIdOfChildNode) {
    let oResult = {};
    CS.forEach(aAllowedTaxonomies, function (oTaxonomy) {
      let sId = oTaxonomy.id;
      let sLabel = CS.getLabelOrCode(oTaxonomy);
      let sParentId = oTaxonomy.parentId ? oTaxonomy.parentId : sParentIdOfChildNode;
      oResult[sId] = _fillTaxonomyHierarchyRecursive(sLabel, sParentId, oConfigDetails);
    });
    return oResult;
  };

  let _fillTaxonomyHierarchyRecursive = function (sLabel, sParentId, oConfigDetails) {
    if (sParentId == -1) {
      return sLabel;
    }
    let oTaxonomyData = oConfigDetails[sParentId];
    sLabel = (CS.getLabelOrCode(oTaxonomyData)).concat(" > ", sLabel);
    sParentId = oTaxonomyData.parentId;
    return _fillTaxonomyHierarchyRecursive(sLabel, sParentId, oConfigDetails);
  };

  let _resolveEmptyPromise = () => {
    return new Promise((resolve) => {
      resolve(null);
    });
  };

  let _rejectEmptyPromise = () => {
    return new Promise((resolve, reject) => {
      reject();
    });
  };

  let _getElementPath = (sParentId, sPath, oReferenceElements) => {
    if (sParentId && sParentId !== "-1") {
      let parentElement = oReferenceElements[sParentId];
      sPath = CS.getLabelOrCode(parentElement) + " > " + sPath;
      return _getElementPath(parentElement.parentId, sPath, oReferenceElements);
    }
    return sPath;
  };

  return {
    getSelectedPortalId: function () {
      return SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL);
    },

    setSelectedDataLanguage: function (sSelectedLanguageCode) {
      _setSelectedDataLanguage(sSelectedLanguageCode);
    },

    isCurrentUserSystemAdmin: function () {
      return _isCurrentUserSystemAdmin();
    },

    isCurrentUserReadOnly: function () {
      return _isCurrentUserReadOnly();
    },

    isCurrentUserAdmin: function () {
      return _isCurrentUserAdmin();
    },

    getDateAttributeInTimeFormat: function (sValue, oDateFormat) {
      return MomentUtils.getDateAttributeInTimeFormat(sValue, oDateFormat);
    },

    getStandardDateTimeFormat:  function() {
      return MomentUtils.getStandardDateTimeFormat()
    },

    getDateAttributeInDateTimeFormat: function (sValue) {
      return MomentUtils.getDateAttributeInDateTimeFormat(sValue)
    },

    getDateInSpecifiedDateTimeFormat: function (sValue, oDateFormat) {
      return MomentUtils.getDateInSpecifiedDateTimeFormat(sValue, oDateFormat)
    },

    getShortDate: function (iTimeStamp) {
      return MomentUtils.getShortDate(iTimeStamp);
    },

    isDateLessThanGivenDays: function (iTimeStamp, sDuration) {
      return MomentUtils.isDateLessThanGivenDays(iTimeStamp, sDuration);
    },

    getFilteredName: function (sValue) {
      return _getFilteredName(sValue);
    },

    getClassNameAsPerRelevanceValue: function (sRelevanceValue) {
      return _getClassNameAsPerRelevanceValue(sRelevanceValue);
    },

    getParsedString: function (sStringToCompile, oParameter) {
      return _getParsedString(sStringToCompile, oParameter);
    },

    getContentScreenDependency: function () {
      return _getContentScreenDependency();
    },

    getDashboardScreenDependency: function () {
      return _getDashboardScreenDependency();
    },

    getUserFullName: function (oUser) {
      return _getUserFullName(oUser);
    },

    isDataIntegrationAllowedForCurrentUser: function () {
      return _isDataIntegrationAllowedForCurrentUser();
    },

    isOnlyDataIntegrationEnabled: function(){
      return _isOnlyDataIntegrationEnabled();
    },

    isValidRegex: function (sRegex) {
      return _isValidRegex(sRegex);
    },

    postRequest: function (sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader) {
      return _postRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader);
    },

    getRequest: function (sUrl, oParameters, fSuccess, fFailure) {
      return _getRequest(sUrl, oParameters, fSuccess, fFailure);
    },

    getRootKlassIdFromBaseType: function (aKlassIds, oReferencedKlasses) {
      return _getRootKlassIdFromBaseType(aKlassIds, oReferencedKlasses);
    },

    failureCallback: function (oResponse, sFunctionName, oTranslations) {
      _failureCallback(oResponse, sFunctionName, oTranslations);
    },

    showError: function (sMessage) {
      _showError(sMessage);
    },

    showSuccess: function (sMessage) {
      _showSuccess(sMessage);
    },

    showMessage: function (sMessage) {
      _showMessage(sMessage);
    },

    getNodeFromTreeListById: function (aNodeList, sNodeId) {
      return _getNodeFromTreeListById(aNodeList, sNodeId);
    },

    getNodePathIdsFromTreeById: function (aNodeList, sNodeId) {
      return _getNodePathIdsFromTreeById(aNodeList, sNodeId);
    },

    getAllIdsInTree: function (aNodeList) {
      return _getAllIdsInTree(aNodeList);
    },

    getMultiTaxonomyData: function (aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree, oAllowedTaxonomyHierarchyList) {
      return _getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree, oAllowedTaxonomyHierarchyList);
    },

    preserveOrderedDataInArray: function (aOldData, aNewData, sDifferenceBy) {
      return _preserveOrderedDataInArray(aOldData, aNewData, sDifferenceBy);
    },

    /** Browser check copied from stackoverflow */

    isFirefox: function () {
      return typeof InstallTrigger !== 'undefined';
    },

    isXlsOrXlsxFile: function (sExtension) {
      return sExtension && CS.includes(['xls', 'xlsx'], sExtension.toLowerCase());
    },

    isObjStpFbxFile: function (sExtension) {
      return sExtension && CS.includes(['obj', 'stp', 'fbx'], sExtension.toLowerCase());
    },

    getMomentOfDate: function (sDate) {
      return MomentUtils.getMomentOfDate(sDate);
    },

    getCurrentLocale: function () {
      return MomentUtils.getCurrentLocale()
    },

    getNumberAccordingToLocale: function (iNumber) {
      return NumberUtils.getNumberAccordingToLocale(iNumber);
    },

    getContentName: function (oContent) {
      return _getContentName(oContent);
    },

    removeTrailingBreadcrumbPath: function (sId, sType){
      return _removeTrailingBreadcrumbPath(sId, sType);
    },

    getSelectedNumberFormatByDataLanguage: function () {
      return NumberUtils.getSelectedNumberFormatByDataLanguage();
    },

    getValueToShowAccordingToNumberFormat: function (sValue, iPrecision, oNumberFormat, bDisableNumberLocaleFormatting) {
      return NumberUtils.getValueToShowAccordingToNumberFormat(sValue, iPrecision, oNumberFormat, bDisableNumberLocaleFormatting);
    },

    getNumberAccordingToPrecision: function (sValue, iPrecision, sDecimalSeparator) {
      return NumberUtils.getNumberAccordingToPrecision(sValue, iPrecision, sDecimalSeparator);
    },

    getDataLanguageCodeById: function (sLanguageId) {
      return _getDataLanguageCodeById(sLanguageId);
    },

    removeQuickListBreadCrumbFromPath: function () {
      _removeQuickListBreadCrumbFromPath();
    },

    getCurrentUser: function() {
      return GlobalStore.getCurrentUser();
    },

    getLinkedInstancesFromVariant: function(oVariant){
      return _getLinkedInstancesFromVariant(oVariant);
    },

    getHierarchyDummyNodeId: function () {
      return Constants.TAXONOMY_HIERARCHY_ALL_DUMMY_NODE;
    },

    getParentNodeByChildId: function (aNodeList, sChildId, oParentNode) {
      return _getParentNodeByChildId(aNodeList, sChildId, oParentNode);
    },

    convertReferencedInObjectFormat: function (oReferencedData) {
      return _convertReferencedInObjectFormat(oReferencedData);
    },

    getMinValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return _getMinValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getMaxValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return _getMaxValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getLeafNodeWithSubbedWithParentInfo: function (aTreeNode, sParentPropertyName, sChildPropName) {
      return _getLeafNodeWithSubbedWithParentInfo(aTreeNode, sParentPropertyName, sChildPropName);
    },

    getTextColorBasedOnBackgroundColor: function (sBackgroundColorHex) {
      return _getTextColorBasedOnBackgroundColor(sBackgroundColorHex);
    },

    getNewSuffix: function () {
      return _getNewSuffix();
    },

    getSplitter: function () {
      return _getSplitter();
    },

    getCodesById: function (aDataModel, aCodes) {
      return _getCodesById(aDataModel, aCodes);
    },

    getIdsByCode: function (aDataModel, aId) {
      return _getIdsByCode(aDataModel, aId);
    },

    listModeConfirmation: function (sHeader, aArticleNames, oOkCallback, oCancelCallback, sOkText, sCancelText) {
      _listModeConfirmation(sHeader, aArticleNames, oOkCallback, oCancelCallback, sOkText, sCancelText);
    },

    getIconUrl: function (sKey) {
      return _getIconUrl(sKey)
    },

    refreshCurrentBreadcrumbEntity: function () {
      _refreshCurrentBreadcrumbEntity();
    },

    setHistoryStateToNull: function (fCallBack) {
      _setHistoryStateToNull(fCallBack);
    },

    getTagGroupModels: function (oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags) {
      return _getTagGroupModels(oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags)
    },

    createDummyEntityTagForTagGroupModel: function (oMasterTag) {
      return _createDummyEntityTagForTagGroupModel(oMasterTag);
    },

    applyStyleToDOMByClassNameOnGridViewMount: function (oTargetDom, sClassName, property, value) {
      _applyStyleToDOMByClassNameOnGridViewMount(oTargetDom, sClassName, property, value);
    },

    scrollBy: function (oDom, oStyles, iSpeed, sEasing, fCallback) {
      _scrollBy(oDom, oStyles, iSpeed, sEasing, fCallback);
    },

    scrollTo: function (oDom, oStyles, iSpeed, sEasing, fCallback) {
      _scrollTo(oDom, oStyles, iSpeed, sEasing, fCallback);
    },

    downloadFromByteStream: function (oByteStream, sFileName) {
      _downloadFromByteStream(oByteStream, sFileName);
    },

    prepareTaxonomyHierarchyData: function (aAllowedTaxonomies, oConfigDetails, sParentId) {
      return _prepareTaxonomyHierarchyData(aAllowedTaxonomies, oConfigDetails, sParentId);
    },

    resolveEmptyPromise: () => {
      return _resolveEmptyPromise();
    },

    getElementPath: (sParentId, sPath, oReferenceElements) => {
      return _getElementPath(sParentId, sPath, oReferenceElements);
    },

    rejectEmptyPromise: () => {
      return _rejectEmptyPromise();
    }
  }
})();

export default CommonUtils;
