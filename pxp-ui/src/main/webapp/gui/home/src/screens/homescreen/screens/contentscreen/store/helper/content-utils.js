import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import RequestMapping
  from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import ScreenModeUtils from './screen-mode-utils';
import ContentLogUtils from './content-log-utils';
import ContentScreenProps from './../model/content-screen-props';
import AppData from './../model/content-screen-app-data';
import GlobalStore from './../../../../store/global-store';
import AttributeUtils from './../../../../../../commonmodule/util/attribute-utils';
import CommonUtils from './../../../../../../commonmodule/util/common-utils';
import ContentScreenConstants from './../model/content-screen-constants';
import ContentScreenViewContextConstants from './../../tack/content-screen-view-context-constants';
import ModuleDictionary from './../../../../../../commonmodule/tack/module-dictionary';
import EntitiesList from './../../../../../../commonmodule/tack/entities-list';
import MockDataForMeasurementMetricAndImperial
  from './../../../../../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import ImageCoverflowUtils from './image-coverflow-utils';
import oFilterPropType from './../../tack/filter-prop-type-constants';
import TagUtils from './../../../../../../commonmodule/util/tag-utils';
import MethodTracker from '../../../../../../libraries/methodtracker/method-tracker';
import LogFactory from '../../../../../../libraries/logger/log-factory';
import BaseTypesDictionary
  from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import ClassNameFromBaseTypeDictionary
  from '../../../../../../commonmodule/tack/class-name-base-types-dictionary';
import TaxonomyBaseTypeDictionary
  from '../../../../../../commonmodule/tack/mock-data-for-taxonomy-base-types-dictionary';
import TagTypeConstants from '../../../../../../commonmodule/tack/tag-type-constants';
import TemplateTabTypeConstants from '../../../../../../commonmodule/tack/template-tabs-dictionary';
import MarkerClassTypeDictionary
  from '../../../../../../commonmodule/tack/marker-class-type-dictionary';
import RelationshipTypeDictionary
  from '../../../../../../commonmodule/tack/relationship-type-dictionary';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary';
import UniqueIdentifierGenerator
  from './../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import ThumbnailCountForZoomLevels
  from '../../../../../../commonmodule/tack/thumbnail-count-for-zoom-levels';
import ThumbnailModeConstants from '../../../../../../commonmodule/tack/thumbnail-mode-constants';
import MSSViewConstants from '../../../../../../commonmodule/tack/mss-view-constants';
import CouplingConstants from '../../../../../../commonmodule/tack/coupling-constans';
import AttributeTypeDictionary
  from '../../../../../../commonmodule/tack/attribute-type-dictionary-new';
import SessionProps from '../../../../../../commonmodule/props/session-props';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';

import ZoomToolbarSettings from '../../../../../../commonmodule/tack/zoom-toolbar-settings';
import SessionStorageManager
  from './../../../../../../libraries/sessionstoragemanager/session-storage-manager';
import CustomActionDialogStore
  from '../../../../../../commonmodule/store/custom-action-dialog-store';
import ContentEditFilterItemsDictionary from '../../tack/content-edit-filter-items-dictionary';
import HierarchyTypesDictionary
  from '../../../../../../commonmodule/tack/hierarchy-types-dictionary';
import SharableURLStore from '../../../../../../commonmodule/store/helper/sharable-url-store';
import SessionStorageConstants from '../../../../../../commonmodule/tack/session-storage-constants';
import NumberUtils from './../../../../../../commonmodule/util/number-util';
import NumberFormatDictionary from '../../../../../../commonmodule/tack/number-format-dictionary';
import DateFormatForDataLanguageDictionary
  from '../../../../../../commonmodule/tack/date-format-for-data-language-dictionary';
import DashboardTabDictionary from '../../screens/dashboardscreen/tack/dashboard-tab-dictionary';
import CollectionViewProps from '../../store/model/viewprops/collection-view-props';
import UOMProps from "../model/uom-props";
import RefactoringStore from "../../../../../../commonmodule/store/refactoring-store";
import BreadCrumbModuleAndHelpScreenIdDictionary
  from "../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary";

var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

var getTranslation = ScreenModeUtils.getTranslationDictionary;

var logger = LogFactory.getLogger('content-screen-utils');
var trackMe = MethodTracker.getTracker('content-screen-utils');
const oNumberFormatDictionary = new NumberFormatDictionary();
const oDateFormatDictionary = new DateFormatForDataLanguageDictionary();

var ContentUtils = (function () {

  const TREE_ROOT_ID = ContentScreenConstants.TREE_ROOT_ID;

  const TAXONOMY_HIERARCHY_ALL_DUMMY_NODE = ContentScreenConstants.TAXONOMY_HIERARCHY_ALL_DUMMY_NODE;

  const sNewSuffix = "#$%$#new";

  var _triggerChange = function () {
    ContentUtils.trigger('content-utils-change');
  };

  var findByValues = function(collection, property, values) {
    return CS.filter(collection, function(item) {
      return CS.indexOf(values, item[property]) != -1;
    });
  };

  var _getActiveContent = function () {
    return ContentScreenProps.screen.getActiveContent();
  };

  var _setActiveContent = function (oContent) {
    ContentScreenProps.screen.setActiveContent(oContent);
  };

  var _getActiveContentClass = function () {
    return ContentScreenProps.screen.getActiveContentClass();
  };

  var _setActiveContentClass = function (oContent) {
    ContentScreenProps.screen.setActiveContentClass(oContent);
  };

  var _getSelectedContentList = function () {
    var aSelectedContentIds = ContentScreenProps.screen.getSelectedContentIds();
    var aContentList = AppData.getContentList();

    return findByValues(aContentList, "id", aSelectedContentIds);
  };

  var _getSelectedContentIds = function () {
    return ContentScreenProps.screen.getSelectedContentIds();
  };

  var _getContentById = function (sContentId) {
    var aContentList = AppData.getContentList();
    ExceptionLogger.log("getContentById");
    ExceptionLogger.log(aContentList);
    return CS.find(aContentList, {id: sContentId});
  };

  var _setSelectedContentList = function (aContents) {
    return ContentScreenProps.screen.setSelectedContentList(aContents || []);
  };

  var _setSelectedContentIds = function (aIds) {
    return ContentScreenProps.screen.setSelectedContentIds(aIds || []);
  };

  var _setSelectedSetList = function (aSets) {
    return ContentScreenProps.screen.setSelectedSetList(aSets || []);
  };

  var _setContentScreenMode = function (sScreenMode) {
    ContentScreenProps.screen.setContentScreenMode(sScreenMode);
  };

  var _getContentScreenMode = function () {
    return ContentScreenProps.screen.getContentScreenMode();
  };

  var _makeContentDirty = function (oContent) {
    if (!oContent.contentClone) {
      oContent.isDirty = true;
      oContent.contentClone = CS.cloneDeep(oContent);
    }

    ContentLogUtils.debug('makeContentDirty: Making content dirty', oContent);
    return oContent.contentClone;
  };

  var _emptySelectedContentList = function () {
    ContentScreenProps.screen.emptySelectedContentList();
  };

  var _emptySelectedImageList = function () {
    ImageCoverflowUtils.emptySelectedImages();
  };

  var _emptySelectedAttributeList = function () {
    ContentScreenProps.attributesScreenViewProps.setSelectedAttributeVariants([]);
  };

  //Returns only parent selected nodes [If child is also selected then it will not return child node]
  var _getRootNodesByIds = function (aNodeList, aIds) {
    var aNodes = [];
    CS.forEach(aNodeList, function (oNode) {
      var sNodeId = oNode.id;
      if (CS.includes(aIds, sNodeId)) {
        aNodes.push(oNode);
      } else {

        if (oNode.children) {
          aNodes = aNodes.concat(_getRootNodesByIds(oNode.children, aIds));
          if (CS.isEmpty(aIds)) {
            return false;
          }
        }

      }
    });

    return aNodes;
  };

  var _getAllChildrenNodes = function (aNodeList) {
    var aChildNodes = [];
    CS.forEach(aNodeList, function (oNode) {
      if (oNode.children) {
        aChildNodes.push(oNode);
        aChildNodes = aChildNodes.concat(_getAllChildrenNodes(oNode.children));
      }
    });

    return aChildNodes;
  };

  var _getNodeFromTreeByPropertyName = function(aTreeNodes, sPropertyName, sValue, sChildPropName, oParent){

    var oRetObj = {};
    var oParentNode = oParent || {};

    CS.forEach(aTreeNodes, function(oTreeNode){
      if (oTreeNode[sPropertyName] == sValue) {
        oRetObj = {
          node: oTreeNode,
          parent: oParentNode
        };

        return false;
      }
      if (oTreeNode[sChildPropName] && oTreeNode[sChildPropName].length > 0){
        var oRes = _getNodeFromTreeByPropertyName(oTreeNode[sChildPropName], sPropertyName, sValue, sChildPropName, oTreeNode);
        if(!CS.isEmpty(oRes)){
          oRetObj = oRes;
          return false;
        }
      }
    });

    return oRetObj;
  };

  var _getScreenProps = function () {
    return ContentScreenProps.screen;
  };

  var _getNodeFromTreeByPropertyNameTillLevel = function(iMaxLevel, aTreeNodes, sPropertyName, sValue, sChildPropName, oParent, iCurrentLevel){
    var oRetObj = {};
    var oParentNode = oParent || {};
    CS.forEach(aTreeNodes, function(oTreeNode){
      if (oTreeNode[sPropertyName] == sValue) {
        oRetObj = {
          node: oTreeNode,
          parent: oParentNode,
          foundAtLevel: iCurrentLevel
        };

        return false;
      }

      if (oTreeNode[sChildPropName] && oTreeNode[sChildPropName].length > 0){
        var oRes = _getNodeFromTreeByPropertyNameTillLevel(
            iMaxLevel,
            oTreeNode[sChildPropName],
            sPropertyName,
            sValue,
            sChildPropName,
            oTreeNode,
            iCurrentLevel + 1);

        if(!CS.isEmpty(oRes)){
          oRetObj = oRes;
          if(oRetObj.foundAtLevel > iMaxLevel && iCurrentLevel == iMaxLevel) {
            oRetObj.foundAtLevel.parent = oTreeNode;
          }
          return false;
        }
      }
    });

    return oRetObj;
  };

  var _getLeafNodes = function (aTreeNode, sChildPropName) {
    var aChildNodes = [];
    CS.forEach(aTreeNode, function (oNode) {
      var aChildren = oNode[sChildPropName];
      if(CS.isEmpty(aChildren)) {
        aChildNodes.push(oNode);
      } else {
        aChildNodes.push.apply(aChildNodes, _getLeafNodes(aChildren, sChildPropName));
      }
    });

    return aChildNodes;
  };

  var _getCurrentContent = function () {
    trackMe('_getActiveContent');
    var oActiveContent = _getActiveContent();
    return oActiveContent.contentClone ? oActiveContent.contentClone : oActiveContent;
  };

  var _getApplicationType = function () {
    var oGlobalModulesData = GlobalStore.getGlobalModulesData();
    return oGlobalModulesData.type;
  };

  var _isOnboarding = function () {
    return _getApplicationType() == "onboarding";
  };


  var _getMasterTagById = function (sTagId) {
    var aMasterTagList = AppData.getTagList();
    return CommonUtils.getNodeFromTreeListById(aMasterTagList, sTagId);
  };

  var _getNewTagValue = function (sTagId, iRelevance, bWithoutSuffix) {
    const iDefaultRelevance = 0;
    var sId = bWithoutSuffix ? UniqueIdentifierGenerator.generateUUID() : UniqueIdentifierGenerator.generateUUID() + _getNewSuffix();
    return  {
      id: sId,
      tagId: sTagId,
      relevance: iRelevance || iDefaultRelevance,
      timestamp: new Date().getTime()
    };
  };

  /**
   * @function _addTagsInContentBasedOnTagType
   * @description - Adding dummy tag values into entity tag through tag references.
   * @param oMasterTag -  Referenced tag.
   * @param oTagGroup - Tag instance.
   * @private
   */
  var _addTagsInContentBasedOnTagType = function (oMasterTag, oTagGroup) {
    if(oMasterTag.tagType == TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
      var oDefaultValue = oMasterTag.defaultValue;
      if (!CS.isEmpty(oDefaultValue) && !CS.isEmpty(oDefaultValue.id)) {
        oTagGroup.tagValues.push(_getNewTagValue(oDefaultValue.id, 100));
        oTagGroup.isValueChanged = true;
      }
    } else if (oMasterTag.tagType == TagTypeConstants.CUSTOM_TAG_TYPE) {
      var iFromValue = oMasterTag.tagValues[0].relevance;
      var iToValue = oMasterTag.tagValues[1].relevance;
      var iRelevance = (iFromValue <= 0 && 0 <= iToValue) ? 0 : iFromValue;
      var aLeafCustomTagNodes = _getLeafNodes(oMasterTag.children, 'children');
      CS.forEach(aLeafCustomTagNodes, function (oTagNode) {
        if (!CS.find(oTagGroup.tagValues, {tagId: oTagNode.id})) {
          oTagGroup.tagValues.push(_getNewTagValue(oTagNode.id, iRelevance));
          oTagGroup.isValueChanged = true;
        }
      });
    } else{
      var aLeafTagNodes = _getLeafNodes(oMasterTag.children, 'children');
      CS.forEach(aLeafTagNodes, function (oTagNode) {
        if(!CS.find(oTagGroup.tagValues, {tagId: oTagNode.id})) {
          oTagGroup.tagValues.push(_getNewTagValue(oTagNode.id));
          oTagGroup.isValueChanged = true;
        }
      });
    }
  };

  var _removeOldTagsByTimeStampFromContentUsingTagGroup = function (oContent, oTagGroup) {
    var oMaxTimeStamp = {
      timestamp: null,
      index: null
    };

    var aTagsToRemove = [];
    CS.forEach(oContent.tags, function(oAppliedTagGroup){
      if(oTagGroup.id == oAppliedTagGroup.tagId){
        var aAppliedTags = CS.filter(oAppliedTagGroup.tagValues, function (oTagValue) {
          return oTagValue.relevance;
        });

        if(aAppliedTags.length <= 1) {
          return;
        }

        CS.forEach(oAppliedTagGroup.tagValues, function (oTagValue, iIndex) {
          var oTagInstanceValue = CS.find(oTagGroup.children, {'id': oTagValue.tagId});
          if (!CS.isEmpty(oTagInstanceValue)) {
            aTagsToRemove.push(iIndex);
            if(!oMaxTimeStamp.timestamp || (oTagValue.timestamp && oMaxTimeStamp.timestamp < oTagValue.timestamp)){
              oMaxTimeStamp.timestamp = oTagValue.timestamp;
              oMaxTimeStamp.index = iIndex;
            }
          }
        });
        CS.remove(oAppliedTagGroup.tagValues, function(oTagValue, iIndex){
          return (CS.includes(aTagsToRemove, iIndex) && oMaxTimeStamp.index != iIndex);
        });
      }
    });
    oTagGroup.isValueChanged = true;
  };

  var _getNewEmptyTagGroup = function (oMasterTag, bWithoutSuffix) {
    let oTagGroup = {};
    let sUUID = UniqueIdentifierGenerator.generateUUID();

    oTagGroup.id = bWithoutSuffix ? sUUID : sUUID + _getNewSuffix();
    oTagGroup.tagValues = [];
    oTagGroup.tagId = oMasterTag.id;
    oTagGroup.baseType = BaseTypesDictionary.tagInstanceBaseType;

    return oTagGroup;
  };

  var _addTagValuesInTagGroup = function (oMasterTag, oTagGroup, bWithoutSuffix) {
    CS.forEach(oMasterTag.children, function (oTagNode) {
      let oNewTagValue = CS.find(oTagGroup.tagValues, {tagId: oTagNode.id});
      if (CS.isEmpty(oNewTagValue)) {
        let iRelevance = 0;
        oNewTagValue = _getNewTagValue(oTagNode.id, iRelevance, bWithoutSuffix);
        oNewTagValue.id = UniqueIdentifierGenerator.generateUUID();
        oTagGroup.tagValues.push(oNewTagValue);
      }
    });
  };

  var _addTagInEntity = function (oEntity, oElement, oMasterTag) {
    oMasterTag = oMasterTag || oElement.tag;
    if(!oEntity.tags) {
      oEntity.tags = [];
    }
    var oContentTagGroup = CS.find(oEntity.tags, {tagId: oMasterTag.id});
    if(CS.isEmpty(oContentTagGroup)) {
      oContentTagGroup = _getNewEmptyTagGroup(oMasterTag, false);
      oEntity.tags.push(oContentTagGroup);
      _addTagsInContentBasedOnTagType(oMasterTag, oContentTagGroup);

    } else if(oMasterTag.tagType == TagTypeConstants.YES_NEUTRAL_TAG_TYPE){
      if(!oElement.isMultiselect) {
        _removeOldTagsByTimeStampFromContentUsingTagGroup(oEntity, oMasterTag);
      }

    } else {
      _addTagsInContentBasedOnTagType(oMasterTag, oContentTagGroup)
    }

    return oContentTagGroup;
  };

  var _addAllMasterTagInEntity = function (oEntity, oMasterTag, bWithoutSuffix) {
    var oContentTagGroup = CS.find(oEntity.tags, {tagId: oMasterTag.id});

    if(CS.isEmpty(oContentTagGroup)) {
      oContentTagGroup = _getNewEmptyTagGroup(oMasterTag, true);
      oEntity.tags.push(oContentTagGroup);
    }

    _addTagValuesInTagGroup(oMasterTag, oContentTagGroup, bWithoutSuffix);
  };

  var _addAllMasterAttributeInEntity = function (oEntity, oMasterAttribute, bWithoutSuffix) {
    var oExistingAttribute = CS.find(oEntity.attributes, {attributeId: oMasterAttribute.id});
    if(CS.isEmpty(oExistingAttribute)) {
      oExistingAttribute = _createAttributeInstanceObject("", oMasterAttribute.id, bWithoutSuffix);
      oEntity.attributes.push(oExistingAttribute);
    }

    return oExistingAttribute;
  };

  var _getNewSuffix = function () {
    return sNewSuffix;
  };

  var _setSelectedContextProps = function (sContext, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, sFillerID) {
    var oScreenProps = ContentUtils.getScreenProps();
    var oSelectedContext = oScreenProps.getSelectedContext();
    var bIsContextSame = oSelectedContext.context === sContext && oSelectedContext.elementId === sElementId && oSelectedContext.structureId === sStructureId;

    oSelectedContext.context = sContext;
    oSelectedContext.elementId = sElementId;
    oSelectedContext.masterEntityId = sMasterEntityId || '';
    oSelectedContext.attributeType = sAttributeType;
    oSelectedContext.sectionId = sSectionId;
    oSelectedContext.fillerID = sFillerID;

    return bIsContextSame;
  };

  var _resetToUpdateAllSCU = function () {
    var oSelectedContext = ContentScreenProps.screen.getSelectedContext();
    oSelectedContext.updateAll = true;
    ContentScreenProps.screen.emptySelectedContentList();
    ContentScreenProps.contentSectionViewProps.emptySectionsToUpdate();
    ContentScreenProps.contentSectionViewProps.setSectionsToUpdate('all');
  };

  var _stopSCUUpdate = function () {
    var oSelectedContext = ContentScreenProps.screen.getSelectedContext();
    oSelectedContext.updateAll = false;
    ContentScreenProps.contentSectionViewProps.emptySectionsToUpdate();
  };

  var _getUserList = function () {
    return AppData.getUserList();
  };

  var _getUserById = function (sUserId) {
    return CS.find(_getUserList(), {id: sUserId})
  };

  let _getUserByUserName = function (sUserName) {
    return CS.find(_getUserList(), {userName: sUserName})
  };

  var _formatDate = function (sDate, oDateFormat) {
    sDate = sDate ? +sDate ? new Date(+sDate) : null : null;
    if (CS.isNaN(Date.parse(sDate))) {
      sDate = null;
    }

    return sDate ? CommonUtils.getDateAttributeInTimeFormat(sDate, oDateFormat) : null;
  };

  var _getIconUrl = function (sKey) {
    return RefactoringStore.getIconUrl(sKey);
  };

  var _removeNodeById = function (aNodeList, sNodeId) {
    var oDeletedNode = null;
    CS.forEach(aNodeList, function (oNode, iIndex) {
      if (oNode.id == sNodeId) {
        oDeletedNode = oNode;
        aNodeList.splice(iIndex, 1);
        return false;
      }
      else if (oNode.children) {
        oDeletedNode = _removeNodeById(oNode.children, sNodeId);
        if(!CS.isEmpty(oDeletedNode)){
          return false;
        }
      }
    });

    return oDeletedNode;
  };

  var _removeNodesAndPropsByMultipleIds = function (aNodeList, aIds, oNodeValueList) {
    var aDeletedNodeIds = [];
    if(aIds.length == 0) return aDeletedNodeIds;
    CS.remove(aNodeList, function (oNode, iIndex) {
      if (CS.includes(aIds, oNode.id)) {
        _removeChildNodesFromValueList(oNode.children, oNodeValueList, aDeletedNodeIds);
        aDeletedNodeIds.push(oNode.id);
        delete oNodeValueList[oNode.id];

        return true;
      }
      else if (oNode.children) {
        aDeletedNodeIds = aDeletedNodeIds.concat(_removeNodesAndPropsByMultipleIds(oNode.children, aIds, oNodeValueList));
      }
    });

    return aDeletedNodeIds;
  };

  var _listOfIdsToDeleteFromTreeNodeList = function (aItemList, oItemValueList, aIdList, aCreatedIds) {
    var sNodeNamesToDelete = '';
    var TREE_ROOT_ID = -1;
    CS.forEach(aItemList, function (oItem) {
      if (oItem.id != TREE_ROOT_ID && oItemValueList[oItem.id] && (oItemValueList[oItem.id].isChecked == true || oItemValueList[oItem.id].isSelected == true)) {
        sNodeNamesToDelete += (oItem.label || oItem.name) + ", ";
        if (oItem.isCreated) {
          aCreatedIds.push(oItem.id);
        } else {
          aIdList.push(oItem.id);
        }
      } else {
        sNodeNamesToDelete += _listOfIdsToDeleteFromTreeNodeList(oItem.children, oItemValueList, aIdList, aCreatedIds);
      }
    });

    return sNodeNamesToDelete;
  };

  var _applyValuesToAllTreeNodes = function (oTreeValuesInList, aKayValue) {
    CS.map(oTreeValuesInList, function (oTreeValue) {
      CS.forEach(aKayValue, function (oKeyValue) {
        oTreeValue[oKeyValue.key] = oKeyValue.value;
      });
    });
  };

  var _applyToAllNodesBelow = function (aPropertyKeyValue, oNode, oVisualProps, bExcludeCallingNode) {
    if(!bExcludeCallingNode){
      var oNodeVisualProp = oVisualProps[oNode.id] || {};
      CS.forEach(aPropertyKeyValue, function (oPropertyKeyValue) {
        oNodeVisualProp[oPropertyKeyValue.key] = oPropertyKeyValue.value;
      });
    }

    CS.forEach(oNode.children, function (oChild) {
      _applyToAllNodesBelow(aPropertyKeyValue, oChild, oVisualProps);
    });
  };

  var _addNewTreeNodesToValueList = function (oTreeValuesInList, aTreeNode, aKeyValue) {

    CS.forEach(aTreeNode, function (oTreeNode) {
      var oTreeItem = {};

      oTreeItem.id = oTreeNode.id;
      oTreeItem.label = oTreeNode.label;
      CS.forEach(aKeyValue, function (oKeyValue) {
        oTreeItem[oKeyValue.key] = oKeyValue.value;
      });
      oTreeValuesInList[oTreeNode.id] = oTreeItem;
      _addNewTreeNodesToValueList(oTreeValuesInList, oTreeNode.children, aKeyValue);
    });
  };

  var _removeChildNodesFromValueList = function (aNodes, oValueList, aDeletedNodeIds) {
    CS.forEach(aNodes, function (oNode) {
      if (oNode.children) {
        _removeChildNodesFromValueList(oNode.children, oValueList, aDeletedNodeIds);
      }
      if(CS.isArray(aDeletedNodeIds)) {
        aDeletedNodeIds.push(oNode.id);
      }
      delete oValueList[oNode.id];
    });

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

  var _getParentNodeIdByChildId = function (aNodeList, sChildId, sParentId) {
    var sGotParentId = '';
    CS.forEach(aNodeList, function (oNode) {
      if (oNode.id == sChildId) {
        sGotParentId = sParentId;
        return false;
      } else {
        sGotParentId = _getParentNodeIdByChildId(oNode.children, sChildId, oNode.id);
        if (sGotParentId) {
          return false;
        }
      }
    });
    return sGotParentId;
  };

  var _getMasterAttributeById = function (sId) {
    /** In case of opened content use referenced attributes **/
    var oReferencedAttributes= ContentScreenProps.screen.getReferencedAttributes();
    var aList = _getAttributeList();
    var oMasterAttribute = oReferencedAttributes[sId];

    if(CS.isEmpty(oMasterAttribute)) {
      oMasterAttribute = CS.find(aList, {id: sId});
    }

    return oMasterAttribute;
  };

  var _preProcessAttributeBeforeSave = function (oAttribute) {

    if (oAttribute && oAttribute.value) {
      var oMasterAttribute = _getMasterAttributeById(oAttribute.attributeId);

      if (oMasterAttribute && _isHTMLAttribute(oMasterAttribute.type)) {
        var sTemp = oAttribute.valueAsHtml;
        if(CS.last(sTemp) == "\n") {
          sTemp = sTemp.slice(0, -1);
        }
        oAttribute.valueAsHtml = oAttribute.value;
        oAttribute.value = sTemp;
      }
    }

    return oAttribute;
  };

  var _preProcessContentAttributesAfterGet = function (oContent) {
    var aAttributes = oContent.attributes || oContent.dependentAttributes;
    CS.forEach(aAttributes, function (oAttribute) {
      _preProcessAttributeAfterGet(oAttribute);
    })

  };

  let _preProcessContentAttributesAfterGetForComparison = function (oContent, aContentAttributes, aContentTags, oConfigDetails, oSelectedLanguageForComparison) {
    let aDependentAttributes = oContent.attributes || oContent.dependentAttributes;
    CS.forEach(aDependentAttributes, function (oAttribute) {
      _preProcessAttributeAfterGetForComparison(oAttribute, aDependentAttributes, aContentAttributes, aContentTags, oConfigDetails,
          oSelectedLanguageForComparison);
    })
  };

  let _preProcessAttributeAfterGetForComparison = function (oAttribute, aDependentAttributes, aContentAttributes, aContentTags, oConfigDetails,
                                                            oSelectedLanguageForComparison) {
    let oMasterAttribute = _getMasterAttributeById(oAttribute.attributeId);
    if (oMasterAttribute && _isHTMLAttribute(oMasterAttribute.type)) {
      oAttribute.value = !CS.isEmpty(oAttribute.valueAsHtml) ? oAttribute.valueAsHtml : oAttribute.value;
    } else if (oMasterAttribute && ContentUtils.isAttributeTypeConcatenated(oMasterAttribute.type)) {
      let oReferencedAttributes = oConfigDetails.referencedAttributes;
      let oReferencedTags = oConfigDetails.referencedTags;
      let oReferencedElements = oConfigDetails.referencedElements;
      let aMergedAttributes = [];

      /**Combine klassInstance independent attribute and LanguageInstance dependent attribute to show concatenated
       attribute in language comparison**/
      CS.forEach(aContentAttributes, function (oContentAttribute) {
        let oFoundDependentAttribute = CS.find(aDependentAttributes, {attributeId: oContentAttribute.attributeId});
        CS.isEmpty(oFoundDependentAttribute) ? aMergedAttributes.push(oContentAttribute) : aMergedAttributes.push(oFoundDependentAttribute);
      });

      let aConcatenatedExpressionList = _getConcatenatedAttributeExpressionList(oAttribute, aMergedAttributes, aContentTags,
          oReferencedAttributes, oReferencedTags, oReferencedElements, oSelectedLanguageForComparison);
      oAttribute.value = "";
      CS.forEach(aConcatenatedExpressionList, (oProperty) => {
        oAttribute.value = (oAttribute.value).concat(oProperty.value);
      });
    }
    return oAttribute;
  };

  var _isHTMLAttribute = function(sType){
    return AttributeUtils.isHTMLAttribute(sType);
  };

  var _preProcessAttributeAfterGet = function (oAttribute) {
    var oMasterAttribute = _getMasterAttributeById(oAttribute.attributeId);
    if (oMasterAttribute && _isHTMLAttribute(oMasterAttribute.type)) {
      oAttribute.value = !CS.isEmpty(oAttribute.valueAsHtml) ? oAttribute.valueAsHtml : oAttribute.value;
    }
    return oAttribute;
  };

  var _changeVariantOfIdOfNewlyAddedElement = function (aNewIds, aAddedElements) {
    var sSplitter = ContentUtils.getSplitter();
    CS.forEach(aNewIds, function (sNewId) {
      var oVariant = CS.find(aAddedElements, {variantOf: sNewId});
      if(oVariant) {
        oVariant.variantOf = sNewId.split(sSplitter)[0];
      }
    });
  };

  var _generateADMForLinkedInstances = function(aNewLinkedInstances, aOldLinkedInstances){
    var oADM = {};
    var oOldLinkedInstancesMap = CS.keyBy(aOldLinkedInstances, 'id');
    var aAddedLinkedInstances = [];

    CS.forEach(aNewLinkedInstances, function (oNewLinkedInstance) {
      var sNewLinkedInstanceId = oNewLinkedInstance.id;

      if (oOldLinkedInstancesMap[sNewLinkedInstanceId]) {
        delete oOldLinkedInstancesMap[sNewLinkedInstanceId];
      } else {
        aAddedLinkedInstances.push(oNewLinkedInstance);
      }
    });

    oADM.addedLinkedInstances = aAddedLinkedInstances;
    oADM.deletedLinkedInstances = CS.map(oOldLinkedInstancesMap, 'id');
    return oADM;
  };

  var _generateADMForAttributeContextLinkedInstances = function (oNewAttributeContext, oOldAttributeContext) {
    var aNewLinkedInstances = oNewAttributeContext.linkedInstances || [];
    var aOldLinkedInstances = oOldAttributeContext.linkedInstances || [];
    var oADM = _generateADMForLinkedInstances(aNewLinkedInstances, aOldLinkedInstances);

    oNewAttributeContext.addedLinkedInstances = oADM.addedLinkedInstances;
    oNewAttributeContext.deletedLinkedInstances = oADM.deletedLinkedInstances;
    delete oNewAttributeContext.linkedInstances;
  };

  let _checkIfBelongsToOtherKlassInstance = function (sReferencedElementId, oEntity) {
    let oReferencedElements = ContentScreenProps.screen.getReferencedElements();
    let oReferencedElement = oReferencedElements[sReferencedElementId] || {};
    return (oEntity.klassInstanceId && oEntity.klassInstanceId != ContentUtils.getActiveContent().id) && oReferencedElement.couplingType != CouplingConstants.READ_ONLY_COUPLED && !oReferencedElement.attributeVariantContext;
  };

  var _generateADMForAttribute = function (aOldAttribute, aClonedAttribute, bShouldCheckParentVariance) {
    var oADM = {
      added: [],
      modified: [],
      deleted: []
    };
    let oReferencedAttributes = ContentScreenProps.screen.getReferencedAttributes();
    var aNewlyAddedIds = [];
    var sNewAttribute = ContentUtils.getNewSuffix();
    var aFoundIdsInNew = [];
    var sSplitter = ContentUtils.getSplitter();
    let sDataLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);

    CS.forEach(aOldAttribute, function (oOldAttribute) {
      var bIsTaskAdded = ContentScreenProps.taskProps.getIsTaskAddedStatus();
      var oClonedAttribute = CS.find(aClonedAttribute, {id: oOldAttribute.id});
      if(CS.isEmpty(oClonedAttribute)) {
        oADM.deleted.push(oOldAttribute.id);
      } else {
        oClonedAttribute.id = oClonedAttribute.id.split(sSplitter)[0];
        aFoundIdsInNew.push(oClonedAttribute.id);
        var oReferencedAttribute = oReferencedAttributes[oOldAttribute.attributeId];
        var isCalculatedAttribute = false;
        if (oReferencedAttribute && oReferencedAttribute.type == AttributeTypeDictionary.CALCULATED) {
          isCalculatedAttribute = true;
        }
        if(oOldAttribute.id.indexOf(sNewAttribute) >= 0 || (bShouldCheckParentVariance && _checkIfBelongsToOtherKlassInstance(oOldAttribute.attributeId,oOldAttribute))) {
          if ((!isCalculatedAttribute && oClonedAttribute.value !== "") || bIsTaskAdded) { //do not create attribute
            // instance if value is ""
            aNewlyAddedIds.push(oOldAttribute.id);
            oClonedAttribute.language = sDataLanguage;
            oADM.added.push(_preProcessAttributeBeforeSave(oClonedAttribute));
            _handleAddedTag(oClonedAttribute);
          }
        } else {
          if (oOldAttribute.baseType == BaseTypesDictionary.imageAttributeInstanceBaseType
              && !CS.isEqual(oClonedAttribute, oOldAttribute)) {
            let oADMForAttributeTags = _generateADMForTags(oOldAttribute.tags, oClonedAttribute.tags, bShouldCheckParentVariance);
            oClonedAttribute.addedTags = oADMForAttributeTags.added;
            oClonedAttribute.modifiedTags = oADMForAttributeTags.modified;
            oClonedAttribute.deletedTags = oADMForAttributeTags.deleted;
            delete oClonedAttribute.tags;
            oADM.modified.push(oClonedAttribute);
          } else {
            let oADMForAttributeTags = _generateADMForTags(oOldAttribute.tags, oClonedAttribute.tags, bShouldCheckParentVariance);
            let oTaskData = ContentScreenProps.taskProps.getTaskData();
            var oActivePropertyForTask = oTaskData.activeProperty;
            var bIsModified = false;
            if (
                oClonedAttribute.value != oOldAttribute.value ||
                oActivePropertyForTask && oActivePropertyForTask.instanceId == oOldAttribute.id ||
                oClonedAttribute.isResolved ||
                !CS.isEmpty(CS.difference(oClonedAttribute.valueAsExpression, oOldAttribute.valueAsExpression)) ||
                !CS.isEmpty(oOldAttribute.notification) && CS.isEmpty(oClonedAttribute.notification)
            ) {
              bIsModified = true;
            }


            if (!CS.isEqual(oClonedAttribute.context, oOldAttribute.context)) {
              bIsModified = true;
              var oNewContext = oClonedAttribute.context || {};
              var oOldContext = oOldAttribute.context || {};
              _generateADMForAttributeContextLinkedInstances(oNewContext, oOldContext);
            }

            if (oADMForAttributeTags.added.length ||
                oADMForAttributeTags.modified.length ||
                oADMForAttributeTags.deleted.length ||
                bIsModified
            ) {
              oClonedAttribute.addedTags = oADMForAttributeTags.added;
              oClonedAttribute.modifiedTags = oADMForAttributeTags.modified;
              oClonedAttribute.deletedTags = oADMForAttributeTags.deleted;
              delete oClonedAttribute.tags;
              delete oClonedAttribute.isResolved;

              oClonedAttribute.modifiedContext = oClonedAttribute.context;
              delete oClonedAttribute.context;

              oADM.modified.push(_preProcessAttributeBeforeSave(oClonedAttribute));
            }
          }
        }

      }
    });

    CS.forEach(aClonedAttribute, function(oClonedAttr){
      if(CS.indexOf(aFoundIdsInNew, oClonedAttr.id) == -1){
        aNewlyAddedIds.push(oClonedAttr.id);
        oClonedAttr.id = oClonedAttr.id.split(sSplitter)[0];
        oADM.added.push(_preProcessAttributeBeforeSave(oClonedAttr));
        _handleAddedTag(oClonedAttr);
      }
    });

    _changeVariantOfIdOfNewlyAddedElement(aNewlyAddedIds, oADM.added);

    return oADM;
  };

  var _generateADMForAssets = function (aOldAssets, aClonedAssets) {
    var oADM = {
      added: [],
      modified: [],
      deleted: []
    };

    oADM.added = CS.filter(aClonedAssets, {isNew: true});

    CS.forEach(aOldAssets, function (oOldAsset) {
      var oClonedAsset = CS.find(aClonedAssets, {id: oOldAsset.id});
      if(!CS.isEmpty(oClonedAsset)) {
        if(!CS.isEqual(oClonedAsset, oOldAsset)){
          var oADMForAttributeTags = _generateADMForTags(oOldAsset.tags, oClonedAsset.tags);
          oClonedAsset.addedTags = oADMForAttributeTags.added;
          oClonedAsset.modifiedTags = oADMForAttributeTags.modified;
          oClonedAsset.deletedTags = oADMForAttributeTags.deleted;
          delete oClonedAsset.tags;
          oADM.modified.push(oClonedAsset);
        }
      }
      else {
        oADM.deleted.push(oOldAsset.id);
      }
    });

    return oADM;
  };

  var _generateADMForTagValues = function (aOldTagValues, aClonedTagValues, bForcefullyPush) {
    var oADM = {
      added: [],
      modified: [],
      deleted: []
    };

    var sSplitter = ContentUtils.getSplitter();
    var sNewId = _getNewSuffix();
    var aFoundIds = [];

    CS.forEach(aOldTagValues, function (oOldTagValue) {
      var oClonedTagValue = CS.find(aClonedTagValues, {id: oOldTagValue.id});
      if(CS.isEmpty(oClonedTagValue)) {
        oADM.deleted.push(oOldTagValue.id);
      } else if(oClonedTagValue.id.indexOf(sNewId) == -1){
        aFoundIds.push(oOldTagValue.id);
        if(oClonedTagValue.relevance != oOldTagValue.relevance || bForcefullyPush){
          /** Tag values with zero relevance should pass into deletedTagValues.
           *  Tag values with zero relevance will be come in case of ruler, lifecycle and listing tags.
           */
          if (oClonedTagValue.relevance === 0) {
            oADM.deleted.push(oOldTagValue.id);
          }
          else {
            oADM.modified.push(oClonedTagValue);
          }
        }
      }
    });

    CS.forEach(aClonedTagValues, function(oClonedTagVal){
      let oOldTagValue = CS.find(aOldTagValues, {id: oClonedTagVal.id});
      if(CS.indexOf(aFoundIds, oClonedTagVal.id) == -1){
        oClonedTagVal.id = oClonedTagVal.id.split(sSplitter)[0];

        if(!CS.find(oADM.added, {id: oClonedTagVal.id})) {
          /** In case of boolean tag there is a special handling i.e. we are adding dummy tag value to show selection
           * toggle view hence it should get added only when relevance is different*/
          let bIsAdded = oOldTagValue ? oClonedTagVal.relevance !== oOldTagValue.relevance : true;
          bIsAdded && oADM.added.push(oClonedTagVal);
        }
      }
    });

    return oADM;
  };

  var _handleAddedTag = function (oTag) {
    var sSplitter = ContentUtils.getSplitter();
    oTag.id = oTag.id.split(sSplitter)[0];

    CS.forEach(oTag.tagValues, function (oTagValue) {
      var sTagValueId = oTagValue.id;
      oTagValue.id = sTagValueId.split(sSplitter)[0];
    });

    CS.forEach(oTag.tags, function (oChildTag) {
      var sTagId = oChildTag.id;
      oChildTag.id = sTagId.split(sSplitter)[0];

      CS.forEach(oChildTag.tagValues, function (oTagValue) {
        var sTagValueId = oTagValue.id;
        oTagValue.id = sTagValueId.split(sSplitter)[0];
      });
    });
  };

  var _findAddedAndModifiedRelationship = function (aOldContentRelationship, aNewContentRelationship, oRelationshipADMObject, bIsNatureRelationship) {
    var aAvailableEntities = ContentUtils.getAppData().getAvailableEntities();
    var sSplitter = ContentUtils.getSplitter();
    var sNewId = ContentUtils.getNewSuffix();
    var oScreenProps = ContentScreenProps.screen;
    var sBaseType = '';
    var oRelationshipContextData = ContentScreenProps.screen.getRelationshipContextData();
    CS.forEach(aNewContentRelationship, function (oNewContentRelationship) {

      var oExistingRelationship = CS.find(aOldContentRelationship, {'sideId': oNewContentRelationship.sideId});
      let bIsQuickList = _getAvailableEntityViewStatus();

      /** To handle dirty tab changed save*/
      var bIsUnlinkedButNotExisting = false;
      if (CS.isEmpty(oExistingRelationship)) {
        var oReferencedRelationshipElements = oScreenProps.getReferencedElements();
        var oRelationship = oReferencedRelationshipElements[oNewContentRelationship.sideId];
        bIsUnlinkedButNotExisting = !oRelationship.isLinked;
      }

      /**TODO: Need to remove after testing relationship properly. Now "addedRelationship" is not needed*/

      var aContentElements = oNewContentRelationship.elementIds;
      var aOldContentElements = oExistingRelationship.elementIds;
      var oTempModifiedRelationshipContextData = ContentScreenProps.screen.getModifiedRelationshipsContextTempData();
      var oRelationshipContentElementsADMObject = {
        deletedElements: [],
        addedElements: [],
      };


      oRelationshipContentElementsADMObject.modifiedElements = [];
      oRelationshipContentElementsADMObject.modifiedContexts = [];


      if (oRelationshipContextData.isForSingleContent && !CS.isEmpty(oTempModifiedRelationshipContextData)
          && oNewContentRelationship.sideId == oRelationshipContextData.relationshipId) { //TODO: check here...
        let oRelationshipProps = ContentScreenProps.relationshipView;
        var oReferenceRelationshipInstanceElementsData = bIsNatureRelationship ? oRelationshipProps.getReferenceNatureRelationshipInstanceElements() : oRelationshipProps.getReferenceRelationshipInstanceElements();
        var aLinkedInstances = oReferenceRelationshipInstanceElementsData[oNewContentRelationship.sideId];
        var oLinkedInstance = CS.find(aLinkedInstances, {id: oRelationshipContextData.relationshipContentInstanceId});
        sBaseType = oLinkedInstance.baseType;
        CS.forEach(oTempModifiedRelationshipContextData, function (oTempData, sKey) {
          var oModifiedElement = {
            id: sKey,
            tags: oTempData.tags || oRelationshipContextData.tags || [],
            timeRange: oTempData.timeRange || oRelationshipContextData.timeRange || {to: null, from: null},
            count: oLinkedInstance.count,
            versionId: oLinkedInstance.versionId,
            contextId: oRelationshipContextData.context && oRelationshipContextData.context.id || null
          };
          oRelationshipContentElementsADMObject.modifiedElements.push(oModifiedElement);
        });
      }


      var oVariantSectionViewProps = ContentUtils.getComponentProps().variantSectionViewProps;
      var oSelectedContext = oVariantSectionViewProps.getSelectedContext();
      var sActiveContextId = oSelectedContext.id;
      var sSelectedTabId = ContentUtils.getSelectedTabId();

      CS.forEach(aContentElements, function (sContentElementId) {
        if ((sSelectedTabId === ContentScreenConstants.tabItems.TAB_TIMELINE) || oScreenProps.getIsVersionMatchMergeDialogOpen() || (oNewContentRelationship.isDirty && !bIsQuickList)) {
          let bDoesElementExist = CS.includes(aOldContentElements, sContentElementId);
          if (bDoesElementExist) {
            let aOldTags = oExistingRelationship.elementTagMapping[sContentElementId];
            let aNewTags = oNewContentRelationship.elementTagMapping[sContentElementId];
            if (!CS.isEqual(aOldTags, aNewTags)) {
              let oModifiedItem = {};
              oModifiedItem.id = sContentElementId;
              oModifiedItem.tags = oNewContentRelationship.elementTagMapping[sContentElementId] || [];
              oModifiedItem.timeRange = oNewContentRelationship.elementTimeRangeMapping[sContentElementId] || {
                to: null,
                from: null
              };
              oModifiedItem.count = 1;
              oModifiedItem.contextId = oNewContentRelationship.contextId;
              oModifiedItem.versionId = 0;
              oRelationshipContentElementsADMObject.modifiedElements.push(oModifiedItem);
            }
          }
          else {
            let oNewItem = {};
            oNewItem.id = sContentElementId;
            oNewItem.tags = oNewContentRelationship.elementTagMapping[sContentElementId] || oRelationshipContextData.tags || [];
            oNewItem.timeRange = oNewContentRelationship.elementTimeRangeMapping[sContentElementId] || oRelationshipContextData.timeRange || {
              to: null,
              from: null
            };
            oNewItem.count = 1;
            oNewItem.contextId = oRelationshipContextData.context && oRelationshipContextData.context.id || null;
            oNewItem.versionId = 0;
            oRelationshipContentElementsADMObject.addedElements.push(oNewItem);
          }

        } else {

          var oDummyVariant = oVariantSectionViewProps.getDummyVariant();
          var oNewItem = {
            id: sContentElementId,
            tags: oDummyVariant.tags || [],
            timeRange: oDummyVariant.timeRange || {to: null, from: null},
            /**
             * Note: Below line was commented so that contextId is not sent in case of
             * Unit Variant, if not working for another scenarios, will need to find proper fix
             */
            //contextId: sActiveContextId,
            count: 1
          };

          var oDummyLinkedVariant = oVariantSectionViewProps.getDummyLinkedVariant();
          if (oDummyLinkedVariant.sideId == oNewContentRelationship.sideId && sContentElementId == oDummyLinkedVariant.id) {
            let iVersionId = 0;
            if (bIsNatureRelationship) {
              let oReferencedNatureRelationshipInstanceElements = ContentScreenProps.relationshipView.getReferenceNatureRelationshipInstanceElements();
              let sActiveVariantRelationshipId = oScreenProps.getVariantRelationshipId();
              let aAddedInstances = oReferencedNatureRelationshipInstanceElements[sActiveVariantRelationshipId];
              let oReferencedElement = CS.find(aAddedInstances, {id: sContentElementId});
              if (oReferencedElement) {
                iVersionId = oReferencedElement.versionId;
              }
            }
            oNewItem.tags = oDummyLinkedVariant.tags;
            oNewItem.contextId = oDummyLinkedVariant.contextId;
            oNewItem.name = oDummyLinkedVariant.name;
            oNewItem.timeRange = oDummyLinkedVariant.timeRange;
            oNewItem.versionId = iVersionId;
            oRelationshipContentElementsADMObject.modifiedElements.push(oNewItem);
          }

          if (!CS.isEmpty(oSelectedContext) && CS.isEmpty(oDummyVariant) && CS.isEmpty(oDummyLinkedVariant)) {
            var oReferencedNatureRelationshipInstanceElements = ContentScreenProps.relationshipView.getReferenceNatureRelationshipInstanceElements();
            var sActiveVariantRelationshipId = oScreenProps.getVariantRelationshipId();
            var aAddedInstances = oReferencedNatureRelationshipInstanceElements[sActiveVariantRelationshipId];
            if (!CS.isEmpty(aAddedInstances)) {
              oNewItem.tags = aAddedInstances[0].tags;
            }
          }

          if (ContentUtils.isAllRelevanceZero(oNewItem.tags)) {
            oNewItem.tags = [];
          }


          if (!CS.isEmpty(oRelationshipContextData) && !oRelationshipContextData.isForSingleContent) {
            oNewItem.tags = oRelationshipContextData.tags;
            oNewItem.timeRange = oRelationshipContextData.timeRange;
            oNewItem.contextId = oRelationshipContextData.context.id;
          }


          var sExistingContentElement = CS.find(aOldContentElements, function (o) {
            if (o == sContentElementId) return true;
          });
          if (CS.isEmpty(sExistingContentElement)) {
            var oEntity = CS.find(aAvailableEntities, {id: sContentElementId});
            if (oEntity) {
              oNewItem.versionId = oEntity.versionId;
              oRelationshipContentElementsADMObject.addedElements.push(oNewItem);
            }
          }

        }
      });

      //if isDirty is true in relationship then :
      if (oNewContentRelationship.isDirty) {
        var oReferenceNatureRelationshipInstanceElements = ContentScreenProps.relationshipView.getReferenceNatureRelationshipInstanceElements();
        var aNatureRelationshipInstanceElements = oReferenceNatureRelationshipInstanceElements[oNewContentRelationship.sideId];
        CS.forEach(aNatureRelationshipInstanceElements, function (oElement) {
          //check all elements for elementClone key :
          if (oElement.elementClone) {
            //if it exists, check whether count value is different :
            var oElementClone = oElement.elementClone;
            var oModifiedElement = {};
            if (oElement.count != oElementClone.count) {
              //if it is, get count value from clone and add element in modifiedElements :
              oModifiedElement.versionId = oElementClone.versionId;
              oModifiedElement.count = oElementClone.count || 1;
            }
            if (oElementClone && oElementClone.hasOwnProperty('isFatherArticle')) {
              oModifiedElement.isFatherArticle = oElementClone.isFatherArticle;
            }
            if (oModifiedElement) {
              oModifiedElement.id = oElementClone.id;
              oRelationshipContentElementsADMObject.modifiedElements.push(oModifiedElement);
            }
            //todo: should clear elementClone here?
          }
        });
      }

      CS.forEach(aOldContentElements, function (oOldContentElement) {

        var oExistingOldContentElement = CS.find(aContentElements, function (o) {
          if (o == oOldContentElement) return true;
        });
        if (CS.isEmpty(oExistingOldContentElement)) {
          oRelationshipContentElementsADMObject.deletedElements.push(oOldContentElement);
        }
      });
      if (!CS.isEmpty(oRelationshipContentElementsADMObject.addedElements) || !CS.isEmpty(oRelationshipContentElementsADMObject.modifiedElements)
          || !CS.isEmpty(oRelationshipContentElementsADMObject.deletedElements) || !CS.isEmpty(oRelationshipContentElementsADMObject.modifiedContexts)) {
        var sEntityId = '';
        if (oRelationshipContentElementsADMObject.addedElements[0]) {
          sEntityId = oRelationshipContentElementsADMObject.addedElements[0].id;
        } else if (oRelationshipContentElementsADMObject.deletedElements[0]) {
          sEntityId = oRelationshipContentElementsADMObject.deletedElements[0];
        } else if (oRelationshipContentElementsADMObject.modifiedElements[0]) {
          sEntityId = oRelationshipContentElementsADMObject.modifiedElements[0].id;
        } else if (oRelationshipContentElementsADMObject.modifiedContexts[0]) {
          sEntityId = oRelationshipContentElementsADMObject.modifiedContexts[0].id;
        }

        var oEntity = CS.find(aAvailableEntities, {id: sEntityId}) || {};

        if (CS.isEmpty(oEntity)) {
          var oNatureElementsMap = ContentScreenProps.relationshipView.getReferenceNatureRelationshipInstanceElements();
          var aNatureElementsList = oNatureElementsMap[oNewContentRelationship.sideId];
          oEntity = CS.find(aNatureElementsList, {id: sEntityId});
        }

        if (CS.isEmpty(oEntity)) {
          var oReferencedRelationshipInstanceElements = ContentScreenProps.relationshipView.getReferenceRelationshipInstanceElements();
          var aInstanceElements = oReferencedRelationshipInstanceElements[oNewContentRelationship.sideId];
          oEntity = CS.find(aInstanceElements, {id: sEntityId});
        }

        if (CS.isEmpty(oEntity)) {
          let aReferenceElementInstances = ContentScreenProps.relationshipView.getReferenceElementInstances();
          oEntity = CS.find(aReferenceElementInstances, {id: sEntityId});
        }

        oRelationshipContentElementsADMObject.relationshipId = oNewContentRelationship.relationshipId;
        oRelationshipContentElementsADMObject.id = oNewContentRelationship.id;
        oRelationshipContentElementsADMObject.sideId = oNewContentRelationship.sideId;
        oRelationshipContentElementsADMObject.baseType = (oEntity && oEntity.baseType);

        if(oNewContentRelationship.relationshipId == "standardArticleAssetRelationship" && oNewContentRelationship.isDirty){    // !bIsQuickList
          oRelationshipContentElementsADMObject.baseType = "com.cs.runtime.interactor.entity.AssetInstance";
        }

        oRelationshipADMObject.modified.push(oRelationshipContentElementsADMObject);
      }


    });

    CS.forEach(aOldContentRelationship, function (oOldContentRelationship) {
      var oExistingOldRelationship = CS.find(aNewContentRelationship, {'sideId': oOldContentRelationship.sideId});

      if (!oExistingOldRelationship) {
        oRelationshipADMObject.deleted.push(oOldContentRelationship.sideId);
      }
    });
  };

  let _generateADMForRelationships = function (aOldContentRelationship, aNewContentRelationship, bIsNatureRelationship) {
    var oRelationshipADMObject = {
      modified: [],
      deleted: [],
      added: []
    };

    _findAddedAndModifiedRelationship(CS.cloneDeep(aOldContentRelationship), CS.cloneDeep(aNewContentRelationship),
        oRelationshipADMObject, bIsNatureRelationship);

    return oRelationshipADMObject;
  };

  var _generateADMForTags = function (aOldTags, aClonedTags, bShouldCheckParentVariance) {
    var oADM = {
      added: [],
      modified: [],
      deleted: []
    };

    var sSplitter = ContentUtils.getSplitter();
    var sNewId = _getNewSuffix();
    var aFoundIds = [];
    var aNewlyAddedIds = [];

    CS.forEach(aOldTags, function (oOldTag) {
      var bIsTaskAdded = ContentScreenProps.taskProps.getIsTaskAddedStatus();
      var oClonedTag = CS.find(aClonedTags, {id: oOldTag.id});
      if(CS.isEmpty(oClonedTag)) {
        oADM.deleted.push(oOldTag.id);
      }
      else {
        if (oClonedTag.id.indexOf(sNewId) >= 0 || (bShouldCheckParentVariance && _checkIfBelongsToOtherKlassInstance(oClonedTag.tagId, oClonedTag))) {
          if (!CS.isEmpty(oClonedTag.tagValues) || bIsTaskAdded) {
            aNewlyAddedIds.push(oClonedTag.id);
            oClonedTag.id = oClonedTag.id.split(sSplitter)[0];
            oADM.added.push(oClonedTag);
            _handleAddedTag(oClonedTag);
          }
        } else {
          aFoundIds.push(oOldTag.id);
          var bIsModified = false;
          var bForcefullyPush = oClonedTag.isResolved;
          var oTagValuesADM = _generateADMForTagValues(oOldTag.tagValues, oClonedTag.tagValues, bForcefullyPush);
          let oTaskData = ContentScreenProps.taskProps.getTaskData();
          var oActiveProperty = oTaskData.activeProperty;
          if (oTagValuesADM.added.length
              || oTagValuesADM.deleted.length
              || oTagValuesADM.modified.length
              || (!CS.isEmpty(oOldTag.notification) && CS.isEmpty(oClonedTag.notification)) ||
              oActiveProperty && oActiveProperty.instanceId == oOldTag.id) {
            bIsModified = true;
          }

          if(!(CS.isEmpty(oClonedTag.tags) && CS.isEmpty(oOldTag.tags))) {
            var oADMForTags = _generateADMForTags(oOldTag.tags, oClonedTag.tags, bShouldCheckParentVariance);
            if(oADMForTags.added.length || oADMForTags.modified.length || oADMForTags.deleted.length) {
              bIsModified = true;
            }
          }

          if(bIsModified) {
            delete oClonedTag.tags;
            delete oClonedTag.tagValues;
            delete oClonedTag.isResolved;

            oClonedTag.addedTagValues = (oTagValuesADM) ? oTagValuesADM.added : [];
            oClonedTag.modifiedTagValues = (oTagValuesADM) ? oTagValuesADM.modified : [];
            oClonedTag.deletedTagValues = (oTagValuesADM) ? oTagValuesADM.deleted : [];
            oClonedTag.addedTags = (oADMForTags) ? oADMForTags.added : [];
            oClonedTag.modifiedTags = (oADMForTags) ? oADMForTags.modified : [];
            oClonedTag.deletedTags = (oADMForTags) ? oADMForTags.deleted : [];

            oADM.modified.push(oClonedTag);
          }

        }
      }
    });

    CS.forEach(aClonedTags, function(oClonedTag){
      if(CS.indexOf(aFoundIds, oClonedTag.id) == -1){
        var sClonedTagId = oClonedTag.id;
        oClonedTag.id = oClonedTag.id.split(sSplitter)[0];
        if(!CS.find(oADM.added, {id: oClonedTag.id}) && !CS.isEmpty(oClonedTag.tagValues)) {
          aNewlyAddedIds.push(sClonedTagId);
          oADM.added.push(oClonedTag);
          _handleAddedTag(oClonedTag);
        }
      }
    });

    CS.remove(oADM.added, (oAddedTag) => {
      let bAnyNonZeroRelevance = false;
      CS.forEach(oAddedTag.tagValues, (oTagValue) => {
        if(oTagValue.relevance) {
          bAnyNonZeroRelevance = true;
          return false;
        }
      });
      return !bAnyNonZeroRelevance;
    });

    _changeVariantOfIdOfNewlyAddedElement(aNewlyAddedIds, oADM.added);

    return oADM;
  };

  var _createDummyArticle = function (sParentId, sType) {
    return {
      parentId: TREE_ROOT_ID,
      type: sType || ''
    }
  };

  var _createTagInstanceObject = function(oMasterTag, bWithoutSuffix){
    let oTagGroup = _getNewEmptyTagGroup(oMasterTag, bWithoutSuffix);
    _addTagValuesInTagGroup(oMasterTag, oTagGroup, bWithoutSuffix);
    return oTagGroup;
  };

  var _createAttributeInstanceObject = function(sDefaultVal, sAttributeId, bWithoutSuffix){
    trackMe('_createAttributeInstanceObject');

    //DATAMIGRATION: change to mappingId

    var sId = bWithoutSuffix ? UniqueIdentifierGenerator.generateUUID() : UniqueIdentifierGenerator.generateUUID() + ContentUtils.getNewSuffix();
    return {
      "id": sId,
      "attributeId": sAttributeId,
      "baseType": BaseTypesDictionary.attributeInstanceBaseType,
      "value": sDefaultVal
    };
  };

  var _addAttributeDataInEntityFromSectionElement = function (oEntity, oElement, oClass) {
    trackMe('_addAttributeDataInContentFromSection');
    logger.debug('_addAttributeDataInContentFromSection', {element: oElement});

    //DATAMIGRATION: change to mappingId
    var oMasterAttribute = oElement.attribute;
    var oExistingAttribute = oEntity.attributes ?
                             CS.find(oEntity.attributes, {attributeId: oMasterAttribute.id}) :
                             CS.find(oEntity.dependentAttributes, {attributeId: oMasterAttribute.id});

    if(CS.isEmpty(oExistingAttribute)) {

      let bWithoutSuffix = ContentUtils.isAttributeTypeConcatenated(oMasterAttribute.type);
      oExistingAttribute = _createAttributeInstanceObject("", oMasterAttribute.id, bWithoutSuffix);
      oEntity.attributes ? oEntity.attributes.push(oExistingAttribute) : oEntity.dependentAttributes.push(oExistingAttribute);
    }

    return oExistingAttribute;
  };

  var _removeOldCandidatesByTimeStampFromEntityUsingRole = function (oEntityRole) {
    var oMaxTimeStampCandidate = {};
    CS.forEach(oEntityRole.candidates, function(oCandidate){
      if(!oMaxTimeStampCandidate.timestamp || oMaxTimeStampCandidate.timestamp < oCandidate.timestamp){
        oMaxTimeStampCandidate = oCandidate;
      }
    });
    oEntityRole.candidates = CS.isEmpty(oMaxTimeStampCandidate) ? [] : [oMaxTimeStampCandidate];
  };

  var _addRoleDataInEntityFromSectionElement = function (oEntity, oElement, oClass) {
    trackMe('_addRoleDataInContentFromSectionElement');
    logger.debug('_addRoleDataInContentFromSectionElement', {element: oElement});

    if(!oEntity.roles) {
      oEntity.roles = [];
    }
    var oMasterRole = oElement.role;
    //DATAMIGRATION: change to mappingId
    var oExistingRole = CS.find(oEntity.roles, {roleId: oMasterRole.id});

    if(CS.isEmpty(oExistingRole)) {
      var oContentRole = {
        id: UniqueIdentifierGenerator.generateUUID() + ContentUtils.getNewSuffix(),
        //DATAMIGRATION: change to mappingId
        roleId: oMasterRole.id,
        candidates: oMasterRole.defaultValue || [],
        baseType: BaseTypesDictionary.roleInstanceBaseType
      };
      oEntity.roles.push(oContentRole);
    } else if(!oMasterRole.isMultiselect) {
      _removeOldCandidatesByTimeStampFromEntityUsingRole(oExistingRole);
    }

  };

  var _updateBreadCrumbInfo = function (sId, oEntity) {
    var aBreadCrumb = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    var sIdToSearch = (!CS.isEmpty(oEntity.variantInstanceId)) ? oEntity.variantInstanceId : sId;

    //TODO:Review - Verify need of CS.filter & instead aFoundItem[aFoundItem.length-1] use CS.last

    var aFoundItem = CS.filter(aBreadCrumb, {id: sIdToSearch});
    var oItem = aFoundItem[aFoundItem.length-1];
    if (!CS.isEmpty(oItem)) {
      oItem.label = _getContentName(oEntity);

      var oSelectedContext = ContentScreenProps.variantSectionViewProps.getSelectedContext();
      var oSelectedVisibleContext = ContentScreenProps.variantSectionViewProps.getSelectedVisibleContext();

      var sContextId = null;
      var sChildContextId = null;
      if (!CS.isEmpty(oSelectedContext)) {
        sContextId = oSelectedContext.id;
      }
      if (!CS.isEmpty(oSelectedVisibleContext)) {
        sChildContextId = oSelectedVisibleContext.id
      }

      oItem.childContextId = sChildContextId;
      oItem.contextId = sContextId;
    }
  };

  var _removeSelectionsAcrossScreenMode = function () {
    _setSelectedContentIds([]);
    _setSelectedSetList([]);
    ContentScreenProps.collectionViewProps.setSelectedList([]);
    ContentScreenProps.screen.setSelectedCollectionContentList([]);
  };

  var _getCurrentEntity = function () {
    var oActiveElement = _getActiveContent();
    return oActiveElement.contentClone ? oActiveElement.contentClone: oActiveElement;
  };

  var _getActiveEntityClass = function () {
    return ContentUtils.getActiveContentClass();
  };

  var _getSectionFromActiveEntityClassByEntityId = function (sEntityId, sEntityKey) {
    var oSection = {};
    if(sEntityId && sEntityKey) {
      var oActiveKlass = _getActiveEntityClass();
      if(!CS.isEmpty(oActiveKlass)) {
        CS.forEach(oActiveKlass.sections, function (oClasSection) {
          CS.forEach(oClasSection.elements, function (oElement) {
            if(oElement[sEntityKey] && oElement[sEntityKey].id == sEntityId) {
              oSection = oClasSection;
            }
          });
          if(!CS.isEmpty(oSection)) {
            return false;
          }
        });
      }
    }
    return oSection;
  };

  var _getActiveEntity = function () {
      return _getActiveContent();
  };

  var _makeActiveEntityClean = function () {
    _makeContentClean(_getActiveEntity());
  };

  var _makeActiveEntityDirty = function () {
    var oActiveEntity = _getActiveEntity();
    if (!CS.isEmpty(oActiveEntity)) {
      return _makeContentDirty(oActiveEntity);
    }

    return null;
  };

  var _getActiveContentOrVariant = function () {
    if(_isVariantDialogOpened()) {
        return _getEditableVariant();
    }else if (_getIsUOMVariantDialogOpen() || _getIsAttributeVariantDialogOpen()) {
        return _getUOMDialogEditableEntity();
    } else {
      return _getActiveContent();
    }
  };

  /***
   * @deprecated
   * @returns {*}
   * @private
   */
  var _isVariantDialogOpened = function () {
    return ContentScreenProps.variantSectionViewProps.getIsVariantDialogOpen();
  };

  var _getEditableVariant = function () {
    var oVariantSectionViewProps = ContentScreenProps.variantSectionViewProps;
    var bIsDialogOpen = oVariantSectionViewProps.getIsVariantDialogOpen();
    var oDummyVariant = oVariantSectionViewProps.getDummyVariant();
    var oActiveVariantForEditing = oVariantSectionViewProps.getActiveVariantForEditing();

    if(bIsDialogOpen) {
      if(!CS.isEmpty(oDummyVariant)) {
        return oDummyVariant;
      }
      else {
        return oActiveVariantForEditing;
      }
    }

    return null;
  };

  var _makeActiveContentOrVariantDirty = function () {
    var oActiveEntity = {};

    if(_isVariantDialogOpened()) {
      oActiveEntity = _getEditableVariant();
    } else if (_getIsUOMVariantDialogOpen() || _getIsAttributeVariantDialogOpen()) {
      oActiveEntity = _getUOMDialogEditableEntity();
    } else{
      oActiveEntity = _getActiveEntity();
    }

    if (!CS.isEmpty(oActiveEntity)) {
      return _makeContentDirty(oActiveEntity);
    }

    return null;
  };

  var _getDefaultVariantVersionPropsObject = function () {

    return {
      isTagActive: false,
      isVersionActive: false
    };
  };

  var _setVariantVersionProps = function (oEntity) {
    var oVariantVersionProps = ContentScreenProps.contentDetailsView.getContentVariantVersionProps();
    var aAttributes = oEntity.attributes;
    var aReferencedAssets = oEntity.assets;
    var aTags = oEntity.tags;
    var aRoles = oEntity.roles;

    oVariantVersionProps[oEntity.id] = _getDefaultVariantVersionPropsObject();

    CS.forEach(aReferencedAssets, function (oAsset) {
      oVariantVersionProps[oAsset.id] = _getDefaultVariantVersionPropsObject();
    });

     CS.forEach(aAttributes, function (oAttribute) {
      oVariantVersionProps[oAttribute.id] = _getDefaultVariantVersionPropsObject();
    });

    CS.forEach(aTags, function (oTag) {
      oVariantVersionProps[oTag.id] = _getDefaultVariantVersionPropsObject();
    });

    CS.forEach(aRoles, function (oRole) {
      oVariantVersionProps[oRole.id] = _getDefaultVariantVersionPropsObject();
    });
  };

  var _getContentGridStore = function () {
    return require('./content-grid-store').default;
  };

  var _getContentStore = function () {
    return require('./content-store').default;
  };

  /**TODO: USE FACTORY PATTERN OR DEPENDENCY INJECTION**/
  var _getFilterStore = function (oFilterContext) {
    const FilterStoreFactory = require('./filter-store-factory').default;
    return FilterStoreFactory.getFilterStore(oFilterContext);
  };

  var _getFilterContextBasedOnTabId = function (sSelectedTabId) {
    switch (sSelectedTabId) {
      case ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS:
        return {
          filterType: oFilterPropType.PAGINATION,
          screenContext: sSelectedTabId
        }
      default :
        return {};
    }
  };

  var _getInformationTabStore = function () {
    return require('./information-tab-store').default;
  };

  var _getAvailableEntityStore = function () {
    return require('./available-entity-store').default;
  };

  var _getVariantStore = function () {
    return require('./variant-store').default;
  };

  var _getDirtyActiveEntity = function () {
    var oEntity = _getActiveEntity();
    return oEntity.contentClone ? oEntity.contentClone : oEntity;
  };

  var _makeContentClean = function (oContent) {
    if (!CS.isEmpty(oContent)) {
      delete oContent.isCreated;
      delete oContent.uuid;
      delete oContent.isDirty;
      delete oContent.contentClone;
    }
  };

  let _shouldRemoveZeroRelevanceTagValues = function(oTagType){
    let aTagListWithoutZeroRelevance = [
      TagTypeConstants.YES_NEUTRAL_TAG_TYPE,
      TagTypeConstants.RULER_TAG_TYPE,
      TagTypeConstants.TAG_TYPE_MASTER,
      TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE,
      TagTypeConstants.LISTING_STATUS_TAG_TYPE,
    ];
    return CS.includes(aTagListWithoutZeroRelevance, oTagType);
  };

  var _removeZeroValuesFromFilterTags = function(aTags, bChangeInOriginalObject){

    var aRes = bChangeInOriginalObject ? aTags : CS.cloneDeep(aTags) || [];
    var aObjToRemove = [];

    CS.forEach(aRes, function(oTag){
      if(_shouldRemoveZeroRelevanceTagValues(oTag.type)){
        CS.remove(oTag.children, {from:0, to:0});
        if(oTag.children && CS.isEmpty(oTag.children)){
          aObjToRemove.push(oTag);
        }
        CS.remove(oTag.mandatory, {from:0, to:0});
        CS.remove(oTag.should, {from:0, to:0});
      }else{
        if(oTag.users && CS.isEmpty(oTag.users)){
          aObjToRemove.push(oTag);
        }
      }
    });

    CS.forEach(aObjToRemove, function (oObjToRemove) {
      var iIndex = CS.findIndex(aRes, {id:oObjToRemove.id});
      aRes.splice(iIndex, 1);
    });

    return aRes;
  };

  var _getDirtyNodeList = function (aNodeList) {
    var aDirtyList = [];
    CS.forEach(aNodeList, function (oNode) {
      if(!CS.isEmpty(oNode.contentClone)) {
        aDirtyList.push(oNode);
      }

      if(oNode.children) {
        aDirtyList.push.apply(aDirtyList, _getDirtyNodeList(oNode.children));
      }
    });

    return aDirtyList;
  };

  var _getEntityList = function () {
      return AppData.getContentList();
  };

  var _getSelectedEntityList = function () {
      return ContentUtils.getSelectedContentList()
  };

  var _isSameMode = function (sViewType) {
    return sViewType == _getViewMode();
  };

  //bForceVariantActive added for image coverflow attribtue
  var _addElementToVariantVersionMap = function (oElement, bForceVariantActive) {
    var oVariantVersionMap = ContentScreenProps.contentSectionViewProps.getSectionElementVariantVersionMap();
    var bVariantVisible = oElement.isVariantAllowed || bForceVariantActive;
    var bVersionVisible = !!oElement.numberOfVersionsAllowed;

    if(bForceVariantActive){
      bVariantVisible = false;
      bVersionVisible = false;
    }

    oVariantVersionMap[oElement.id] = {
      isVariantActive: false,
      isVariantVisible: bVariantVisible,
      isVersionActive: false,
      isVersionVisible: bVersionVisible
    }
  };

  var _resetContentScreenState = function () {
    _deselectAllEntities();
    // _resetBreadcrumbPath();
    ContentUtils.setContentScreenMode(ContentScreenConstants.entityModes.ARTICLE_MODE);
    let sDefaultViewMode = ContentUtils.getDefaultViewMode();
    ContentUtils.setViewMode(sDefaultViewMode);
  };

  var _deselectAllEntitiesByType = function(sType){

    var oVisualProps = null;

    if(sType.toLocaleLowerCase() == "article"){
      oVisualProps = ContentScreenProps.articleViewProps.getTreeProps();
    }

    CS.forEach(oVisualProps, function (oProp) {
      oProp.isChecked = false;
      oProp.isSelected = false;
      oProp.isEditable = false;
    });
  };

  var _deselectAllEntities = function(){
    _deselectAllEntitiesByType("article");
  };

  var _setRelationshipElements = function(oEntity) {
    var oReferenceRelationshipInstanceElements = ContentScreenProps.relationshipView.getReferenceRelationshipInstanceElements();
    var oReferenceNatureRelationshipInstanceElements = ContentScreenProps.relationshipView.getReferenceNatureRelationshipInstanceElements();
    oEntity = oEntity.contentClone ? oEntity.contentClone : oEntity;
    var aContentRelationships = oEntity.contentRelationships;
    var aNatureRelationships = oEntity.natureRelationships;
    var oRelationshipProps = ContentScreenProps.relationshipView;
    var oRelationshipToolbarProps = oRelationshipProps.getRelationshipToolbarProps();
    var oRelationshipViewMode = ContentScreenProps.screen.getRelationshipViewMode();
    oRelationshipViewMode = {};

    CS.forEach(aContentRelationships, function (oContentRelationship) {
      let sId = oContentRelationship.sideId;
      if (CS.isEmpty(oRelationshipToolbarProps[sId])) {
        oRelationshipProps.addNewRelationshipToolbarPropById(sId);
      }
      oRelationshipViewMode[sId] = ContentScreenConstants.viewModes.TILE_MODE;
      let aElements = oReferenceRelationshipInstanceElements[sId] || [];
      _addEntityInformationData(aElements);
      oRelationshipToolbarProps[sId].elements = aElements;
      oRelationshipToolbarProps[sId].totalCount = oContentRelationship.totalCount;
    });

    CS.forEach(aNatureRelationships, function (oNatureRelationship) {
      let sId = oNatureRelationship.sideId;
      if (CS.isEmpty(oRelationshipToolbarProps[sId])) {
        oRelationshipProps.addNewRelationshipToolbarPropById(sId);
      }
      oRelationshipViewMode[sId] = ContentScreenConstants.viewModes.TILE_MODE;
      let aElements = oReferenceNatureRelationshipInstanceElements[sId] || [];
      _addEntityInformationData(aElements);
      oRelationshipToolbarProps[sId].elements = aElements;
      oRelationshipToolbarProps[sId].totalCount = oNatureRelationship.totalCount;
    });
  };

  var _getAttributeTypeForVisual = function(sType, sAttrId){
    return AttributeUtils.getAttributeTypeForVisual(sType, sAttrId);
  };

  var _makeAssetInstancesForEntity = function (oEntity, oReferenceRelationshipInstanceElements) {
    if(oEntity.baseType != BaseTypesDictionary.assetBaseType) {
      var sDefaultAssetInstanceId = oEntity.defaultAssetInstanceId;
      var oDefaultAssetEntity = oReferenceRelationshipInstanceElements[sDefaultAssetInstanceId];
      var aReferencedAssets = [];
      if(oDefaultAssetEntity) {
        oDefaultAssetEntity.assetInstanceId = sDefaultAssetInstanceId;
        aReferencedAssets.push(oDefaultAssetEntity);
      }
      oEntity.referencedAssets = aReferencedAssets;
    }
  };

  var _getSplitter = function(){
    return "#$%$#";
  };

  let _addRelationshipDummyEntity = function (sRelationshipId, oEntity, sRelationshipSideId, bIsNatureRelationship = false) {
    let oReferenceRelationshipInstanceElements = ContentScreenProps.relationshipView.getReferenceRelationshipInstanceElements();
    let sId = sRelationshipId;
    let oContentRelationship = CS.find(oEntity.contentRelationships, {relationshipId: sId}) || CS.find(oEntity.natureRelationships, {relationshipId: sId});
    if (CS.isEmpty(oContentRelationship)) {
      oContentRelationship = {
        relationshipId: sId,
        id: UniqueIdentifierGenerator.generateUUID() + _getNewSuffix(),
        elementIds: [],
        elements: [],
        sideId: sRelationshipSideId || "",
        elementTagMapping: [],
        elementTimeRangeMapping: {},
        contextId: ""
      };
      bIsNatureRelationship ?  oEntity.natureRelationships.push(oContentRelationship) : oEntity.contentRelationships.push(oContentRelationship);
    }
    var oRelationProps = ContentUtils.getComponentProps().relationshipView;
    var oRelationshipToolbarProps = oRelationProps.getRelationshipToolbarProps();
    if (CS.isEmpty(oRelationshipToolbarProps[sId])) {
      oRelationProps.addNewRelationshipToolbarPropById(sId);
      let aElements = oReferenceRelationshipInstanceElements[sId] || [];
      _addEntityInformationData(aElements);
      oRelationshipToolbarProps[sId].elements = aElements;
    }
  };

  var _addRelationshipInEntity = function (oElement, oEntity) {
    _addRelationshipDummyEntity(oElement.relationship.id, oEntity)
  };

  var _removeAddedRelationshipElementsOnFailure = function (aEntitiesToSave) {
    var oActiveEntity = _getActiveEntity();
    oActiveEntity = oActiveEntity.contentClone;

    var oEntityToSave = aEntitiesToSave; //aEntitiesToSave is not an array its an object now.
    CS.forEach(oEntityToSave.addedRelationships, function (oAddedRelationship) {
      var sAddedId = oAddedRelationship.id + _getNewSuffix();
      var oContentRelationship = CS.find(oActiveEntity.contentRelationships, {id: sAddedId});
      oContentRelationship.elementIds = [];
    });

    CS.forEach(oEntityToSave.modifiedRelationships, function (oModifiedRelationship) {
      var oContentRelationship = CS.find(oActiveEntity.contentRelationships, {id: oModifiedRelationship.id});
      CS.remove(oContentRelationship.elementIds, function (sElementId) {
        return CS.find(oModifiedRelationship.addedElements, {id:sElementId});
      });
    });
    var oRelationProps = ContentUtils.getComponentProps().relationshipView;
    oRelationProps.emptyModifiedRelationshipElements();

    _discardCloneIfActiveEntityAndActiveEntityCloneEqual();
  };

  var _removeAddedNatureRelationshipElementsOnFailure = function (aEntitiesToSave) {
    var oActiveEntity = _getActiveEntity();
    var oActiveEntityClone = oActiveEntity.contentClone;

    var oEntityToSave = aEntitiesToSave; //aEntitiesToSave is not an array its an object now.

    CS.forEach(oEntityToSave.modifiedNatureRelationships, function (oModifiedNatureRelationship) {
      var aAddedElements = oModifiedNatureRelationship.addedElements;
      var oNatureRelationship = CS.find(oActiveEntityClone.natureRelationships, {relationshipId: oModifiedNatureRelationship.relationshipId});
      CS.forEach(aAddedElements, function (oAddedElement) {
        var sAddedId = oAddedElement.id;
        CS.remove(oNatureRelationship.elementIds, function (sElementId) {
          return sAddedId == sElementId;
        });
      });
    });

    _discardCloneIfActiveEntityAndActiveEntityCloneEqual();
  };

  let _getAllInstancesRequestData = function (oFilterContext) {
    let oFilterStore = _getFilterStore(oFilterContext);
    return oFilterStore.createGetAllInstancesData();
  };

 let _createFilterPostData = function (oFilterParameters, oFilterContext) {
    let oFilterStore = _getFilterStore(oFilterContext);
    let oFilterPostData = oFilterStore.createFilterPostData();
    //When Rule violation tile contents is open, take filter parameters from Information tab store.
    let oInformationTabProps = ContentScreenProps.informationTabProps;
    let oDamTabInformationProps = ContentScreenProps.damInformationTabProps;
    let oInformationTabStore = _getInformationTabStore();
    if (oInformationTabProps.getIsRuleViolatedContentsScreen()) {
      oFilterParameters = _createFilterParameters(oFilterParameters, oInformationTabStore, oFilterContext, DashboardTabDictionary.INFORMATION_TAB);
    }else  if (oDamTabInformationProps.getIsRuleViolatedContentsScreen()) {
      oFilterParameters = _createFilterParameters(oFilterParameters, oInformationTabStore, oFilterContext, DashboardTabDictionary.DAM_TAB);
    }
    if (!CS.isEmpty(oFilterParameters)) {
      CS.assign(oFilterPostData, oFilterParameters);
    }
    return oFilterPostData;
  };

  let _createFilterParameters = function (oFilterParameters, oTabStore, oFilterContext, sContext) {
    let oFilterStore = _getFilterStore(oFilterContext);
    let oFilterPostData = oFilterStore.createFilterPostData();
    let iFrom = 0;
    let iSize = 20;
    if (!CS.isEmpty(oFilterParameters)) {
      iFrom = oFilterParameters.from ? oFilterParameters.from : iFrom;
      iSize = oFilterParameters.size ? oFilterParameters.size : iSize;
    }
    let oTabFilterParameters = oTabStore.getFilterParameters(sContext, "", iFrom, iSize);
    oTabFilterParameters.sortOptions = oFilterPostData.sortOptions;
    oFilterParameters = oFilterParameters || {};
    CS.assign(oFilterParameters, oTabFilterParameters);

    return oFilterParameters;

  };

  var _getUserNameById = function(sUserId){

    var aUserList = AppData.getUserList();
    var sUserName = "";
    CS.forEach(aUserList,function (oUser) {
      if(oUser.id == sUserId){
        sUserName = oUser.userName;
        return false;
      }
    });

    return sUserName;
  };

  var _getSelectedContents = function () {
    return ContentUtils.getSelectedContentList();
  };

  var _isAssetBaseType = function (sBaseType) {
    return BaseTypesDictionary["assetBaseType"] == sBaseType;
  };

  var _isSupplierBaseType = function (sBaseType) {
    return BaseTypesDictionary["supplierBaseType"] == sBaseType;
  };

   var _getEntityType = function (oEntity) {
     var sBaseType = '';
     switch (oEntity.baseType){
       case BaseTypesDictionary["contentBaseType"]:
         sBaseType = oEntity.isFolder ? "folder" : "article";
         break;

       case BaseTypesDictionary["assetBaseType"]:
         sBaseType = oEntity.isFolder ? "assetFolder" : "asset";
         break;

       case BaseTypesDictionary["marketBaseType"]:
         sBaseType = oEntity.isFolder ? "marketFolder" : "market";
         break;

       case BaseTypesDictionary["supplierBaseType"]:
         sBaseType = oEntity.isFolder ? "supplierFolder" : "supplier";
         break;

       case BaseTypesDictionary["textAssetBaseType"]:
         sBaseType = oEntity.isFolder ? "textAssetFolder" : "textasset";
         break;

       case BaseTypesDictionary["collectionKlassInstanceEntityBaseType"]:
         sBaseType = "collection";
         break;
     }

     return sBaseType;
   };

   var _setAttributeListForFiltering = function (aAttributeList) {
     var oAttributes = {
       pim: [],
       mam: [],
       target: [],
       editorial: []
     };

     CS.forEach(aAttributeList, function(oAttr){
       if (oAttr.id === 'typeattribute') {
         return;
       }
       var oObj = {
         id: oAttr.id,
         label: oAttr.label
       };

       oAttr.forPim && oAttributes.pim.push(oObj);
       oAttr.forMam && oAttributes.mam.push(oObj);
       oAttr.forTarget && oAttributes.target.push(oObj);
       oAttr.forEditorial && oAttributes.editorial.push(oObj);
     });

     ContentScreenProps.screen.setAttributeListForFiltering(oAttributes);

   };

   var _getRoleList = function () {
    return AppData.getRoleList();
   };

  var _setActiveEntity = function (oEntity) {
    _setActiveContent(oEntity);
  };

  var _setSelectedEntityList = function (aList) {
      _setSelectedContentList(aList);
  };

  var _setViewMode = function (sViewMode) {
    ContentScreenProps.screen.setViewMode(sViewMode);
  };

  var _getViewMode = function () {
    return ContentScreenProps.screen.getViewMode();
  };

  let _getDefaultViewMode = function () {
    return ContentScreenProps.screen.getDefaultViewMode();
  };

  var _getAttributeList = function () {
    return AppData.getAttributeList();
  };

  var _setFilterProps = function (oFilterProps, bIsPreventResetFilterProps, oFilterContext, aSelectedFilters) {
    _getFilterStore(oFilterContext).setFilterProps(oFilterProps, bIsPreventResetFilterProps, aSelectedFilters);
  };

  var _getAllMenus = function () {
    return GlobalStore.getAllMenus();
  };

  var _isNatureClass = function (sId) {
    var aNatureKlassIds = ContentScreenProps.screen.getArticleNatureKlassIds();

    if(CS.indexOf(aNatureKlassIds, sId) != -1){
      return true;
    }

    return false;
  };

  var _getNatureKlassIdsFromKlassIds = function (aIds) {
    var aNatureKlassIds = ContentScreenProps.screen.getArticleNatureKlassIds();

    return CS.intersection(aNatureKlassIds, aIds);
  };

  var _getScreenModeBasedOnEntityBaseType = function (sBaseType) {
    if (sBaseType == BaseTypesDictionary["contentBaseType"]) {
      return ContentScreenConstants.entityModes.ARTICLE_MODE;
    }
    else if (sBaseType == BaseTypesDictionary["assetBaseType"]) {
      return ContentScreenConstants.entityModes.ASSET_MODE;
    }
    else if (sBaseType == BaseTypesDictionary["marketBaseType"]) {
      return ContentScreenConstants.entityModes.MARKET_MODE;
    }
    //TODO: Ask to create base type for the collections
    else if (sBaseType == "staticCollection") {
      return ContentScreenConstants.entityModes.STATIC_COLLECTION_MODE;
    }
    else if (sBaseType == "dynamicCollection") {
      return ContentScreenConstants.entityModes.DYNAMIC_COLLECTION_MODE;
    }
    else if(sBaseType == BaseTypesDictionary["supplierBaseType"]){
      return ContentScreenConstants.entityModes.SUPPLIER_MODE;
    }
    else if (sBaseType == BaseTypesDictionary["textAssetBaseType"]) {
      return ContentScreenConstants.entityModes.TEXTASSET_MODE;
    }
  };

  var _getScreenModeBasedOnKlassBaseType = function (sBaseType) {

    if (sBaseType == BaseTypesDictionary["articleKlassBaseType"]) {
      return ContentScreenConstants.entityModes.ARTICLE_MODE;
    }
    else if (sBaseType == BaseTypesDictionary["assetKlassBaseType"]) {
      return ContentScreenConstants.entityModes.ASSET_MODE;
    }
    else if (sBaseType == BaseTypesDictionary["marketKlassBaseType"]) {
      return ContentScreenConstants.entityModes.MARKET_MODE;
    }
    else if(sBaseType == BaseTypesDictionary["supplierKlassBaseType"]){
      return ContentScreenConstants.entityModes.SUPPLIER_MODE;
    }
    else if (sBaseType == BaseTypesDictionary["textAssetKlassBaseType"]) {
      return ContentScreenConstants.entityModes.TEXTASSET_MODE;
    }
  };

  var _getScreenModeBasedOnActiveEntity = function ()  {
    var oActiveEntity = _getActiveEntity();
    if(!CS.isEmpty(oActiveEntity)) {
      return _getScreenModeBasedOnEntityBaseType(oActiveEntity.baseType);
    }

  };

  var _getRelationshipViewStatus = function () {
    var oAddEntityInRelationshipScreenData = ContentScreenProps.screen.getAddEntityInRelationshipScreenData();
    var oActiveEntity = _getActiveEntity();
    return (oAddEntityInRelationshipScreenData[oActiveEntity.id] && oAddEntityInRelationshipScreenData[oActiveEntity.id].status);
  };

  var _getNatureRelationshipViewStatus = function () {
    var oAddEntityInRelationshipScreenData = ContentScreenProps.screen.getAddEntityInRelationshipScreenData();
    var oActiveEntity = _getActiveEntity();
    return (oAddEntityInRelationshipScreenData[oActiveEntity.id] &&
            oAddEntityInRelationshipScreenData[oActiveEntity.id].context == "natureRelationship" &&
            oAddEntityInRelationshipScreenData[oActiveEntity.id].status);
  };

  var _getVariantRelationshipViewStatus = function () {
    var oAddEntityInRelationshipScreenData = ContentScreenProps.screen.getAddEntityInRelationshipScreenData();
    var oActiveEntity = _getActiveEntity();
    return (oAddEntityInRelationshipScreenData[oActiveEntity.id] &&
        oAddEntityInRelationshipScreenData[oActiveEntity.id].status);
  };

  var _getIsStaticCollectionScreen = function () {
    var oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();
    return (!CS.isEmpty(oActiveCollection) && oActiveCollection.type == "staticCollection");
  };

  var _getIsDynamicCollectionScreen = function () {
    var oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();
    return (!CS.isEmpty(oActiveCollection) && oActiveCollection.type == "dynamicCollection");
  };

  var _isCollectionScreen = function () {
    return _getIsDynamicCollectionScreen() || _getIsStaticCollectionScreen();
  };

  var _setMasterEntityList = function (aFetchedContents) {
      AppData.setContentList(aFetchedContents);
  };

  var _setPaginatedIndex = function (iIndex) {
    var aContents = AppData.getContentList();
    if (CS.isNumber(iIndex)) {
      ContentScreenProps.screen.setPaginatedIndex(iIndex);
    } else {
      ContentScreenProps.screen.setPaginatedIndex(aContents.length);
    }
  };

  /** To show information of entity on thumbnail or list view e.g
   * 1. Newly created entity
   * 2. Recently updated entity
   * 3. Entity is available in selected data language or not
   **/
  var _addEntityInformationData = function (aContentList) {
    let oLanguageInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = oLanguageInfo.dataLanguages;

    CS.forEach(aContentList, function (oContent) {
      let bIsContentAvailableInSelectedDataLanguage = _isContentAvailableInSelectedDataLanguage(oContent);
      if(!bIsContentAvailableInSelectedDataLanguage) {
        oContent.creationLanguageData = CS.find(aDataLanguages, {code: oContent.creationLanguage});
      }

      oContent.isRecentlyUpdated = false;
      oContent.isNewlyCreated = false;
      if (ContentUtils.isDateLessThanGivenDays(oContent.lastModified, 'hours') < 24 && oContent.lastModified !==oContent.createdOn ) {
        oContent.isRecentlyUpdated = true;
      }

      if (ContentUtils.isDateLessThanGivenDays(oContent.createdOn, 'hours') < 24) {
        oContent.isNewlyCreated = true;
      }

    })
  };

  var _prepareConcatenatedAttributeExpressionListForProducts = function(oChildren, oReferencedAttributes, oReferencedTags, oReferenceElement){
    CS.forEach(oChildren, function (oContent) {
      var aAttributeList = oContent.attributes;
      CS.forEach(aAttributeList, function (aAttribute){
        var oMasterAttribute = oReferencedAttributes[aAttribute.attributeId];
        let sAttributeType = oMasterAttribute.type;
        if (ContentUtils.isAttributeTypeConcatenated(sAttributeType)){
          let aExpressionList = ContentUtils.getConcatenatedAttributeExpressionList(aAttribute, oContent.attributes, oContent.tags, oReferencedAttributes, oReferencedTags, oReferenceElement);
          aAttribute.valueAsExpression = aExpressionList;
        }
      });
    });
  };


  /**
   * @function _performCommonSuccessOperations
   * @param oResponse
   * @param oExtraData - we get filter context in oExtraData.
   * @private
   */
  var _performCommonSuccessOperations = function (oResponse, oExtraData) {
    const oFilterStore = _getFilterStore(oExtraData.filterContext);
    try {
      let aFetchedContents = oResponse.children || oResponse.klassInstances || [];
      _addEntityInformationData(aFetchedContents);
      _setMasterEntityList(aFetchedContents);

      /**
       * Set Pagination and Filter data
       */
      let iIndex = oResponse.from || 0;
      let oFilterProps = ContentUtils.getFilterProps(oExtraData.filterContext);
      oFilterProps.setTotalItemCount(oResponse.totalContents);
      oFilterProps.setFromValue(iIndex);
      oFilterProps.setCurrentPageItems(aFetchedContents.length);
      if(oExtraData.paginationData) {
        oFilterProps.setPaginationSize(oExtraData.paginationData.size);
      }
      let oFilterInfo = {
        sortData: oResponse.appliedSortData,
        filterData: oFilterProps.getIsFilterInformationRequired() ?
            CS.isEmpty(oResponse.filterData) ? oResponse.filterInfo.filterData : oResponse.filterData : oFilterProps.getAvailableFilters()
      };
      oFilterStore.setFilterInfo(oFilterInfo);
      ContentUtils.setFilterProps(oFilterStore.getFilterInfo(), false, oExtraData.filterContext);

      if(ContentScreenProps.screen.getIsEditMode()) {
        _setViewMode(_getDefaultViewMode());
      }

      /**
       * Set referenced assets
       */
      let ContentStore = ContentUtils.getContentStore();
      CS.isNotEmpty(oResponse.referencedAssets) && ContentStore.preProcessAndSetReferencedAssets(oResponse.referencedAssets, oExtraData.isMergeOldReferencedAssetList);

      /**
       * Set referenced klasses
       */
      CS.isNotEmpty(oResponse.referencedKlasses) && ContentUtils.updateReferencedKlassRelatedData(oResponse.referencedKlasses);
    }
    finally {
    }
  };

  var _setLoadedPropertiesFromConfigDetails = function (oConfigDetails) {
    var oScreenProps = ContentScreenProps.screen;
    var oLoadedAttributes = oScreenProps.getLoadedAttributes();
    var oLoadedTags = oScreenProps.getLoadedTags();
    CS.forEach(oConfigDetails.referencedAttributes, function (oAttribute, sAttributeId) {
      oLoadedAttributes[sAttributeId] = oAttribute;
    });
    CS.forEach(oConfigDetails.referencedTags, function (oTag, sTagId) {
      oLoadedTags[sTagId] = oTag;
    });
  };

  var _getAvailableEntityViewStatus = function () {
    var bRelationshipView = _getRelationshipViewStatus();
    var oVariantSectionViewProps = ContentScreenProps.variantSectionViewProps;
    var bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
    var bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
    let sAvailableEntityViewContext = ContentScreenProps.availableEntityViewProps.getAvailableEntityViewContext();

    if(_getIsStaticCollectionScreen() && !bRelationshipView){
      return ContentScreenProps.collectionViewProps.getAddEntityInCollectionViewStatus();
    }else if(bAvailableEntityChildVariantViewVisibilityStatus || bAvailableEntityParentVariantViewVisibilityStatus){
      var bIsVariantQuicklistView = bAvailableEntityChildVariantViewVisibilityStatus || bAvailableEntityParentVariantViewVisibilityStatus;
      return bIsVariantQuicklistView;
    } else if (sAvailableEntityViewContext === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW
        || CS.includes(CS.values(ContentScreenViewContextConstants.availableEntityViewContexts),sAvailableEntityViewContext)) {
      return true;
    } else {
      return bRelationshipView;
    }
  };

  var _getXRayPostData = function (sRelationshipId) {
    var oActiveXRayPropertyGroup = _getActiveXRayPropertyGroup(sRelationshipId);
    if (!CS.isEmpty(oActiveXRayPropertyGroup)) {

      var oXRayData = {
        xrayAttributes: [],
        xrayTags: []
      };

      var aProperties = oActiveXRayPropertyGroup.properties;
      CS.forEach(aProperties, function (oProperty) {
        if (oProperty.type == "attribute") {
          oXRayData.xrayAttributes.push(oProperty.id);
        } else if (oProperty.type == "tag") {
          oXRayData.xrayTags.push(oProperty.id);
        }
      });

      return oXRayData;
    }
    else {
      return null;
    }
  };

  var _isNatureRelationship = function (sRelationshipType) {
    switch (sRelationshipType) {

      case RelationshipTypeDictionary.FIXED_BUNDLE:
      case RelationshipTypeDictionary.SET_OF_PRODUCTS:
      case RelationshipTypeDictionary.SINGLE_ARTICLE:
      case RelationshipTypeDictionary.PRODUCT_NATURE:
      case RelationshipTypeDictionary.MEDIA_ASSET:
        return true;

      default:
        return false;

    }
  };

  /**
   * @function _isNatureRelationshipByReferencedData
   * @description Checks in Referenced Data If the Given Relationship/Reference is Nature
   * @param sId
   * @returns {boolean}
   * @private
   */
  let _isNatureRelationshipByReferencedData = function (sId) {
    let oCommonReferencedRelationships = ContentScreenProps.screen.getCommonReferencedRelationships();
    let oFoundRelationship = oCommonReferencedRelationships[sId];
    if (CS.isNotEmpty(oFoundRelationship) && (CS.isNotEmpty(oFoundRelationship.natureType) || CS.isNotEmpty(oFoundRelationship.relationshipType)  || oFoundRelationship.isNature)) {
      return true;
    }
    return false;
  };

  /**
   * @function _getContentRelationshipKeyById
   * @description Returns ActiveContent->key for given Relationship/Reference Id In which the Relationship Instance
   * Can Be Found Eg := natureRelationships, contentRelationships
   * @param sId
   * @returns {string}
   * @private
   */
  let _getContentRelationshipKeyById = function (sId) {
    let oReferencedRelationship = ContentScreenProps.screen.getCommonReferencedRelationships();
    let oFoundReference = oReferencedRelationship[sId] || {};
    switch (oFoundReference.type) {
      case "com.cs.core.config.interactor.entity.relationship.Relationship":
        return _isNatureRelationshipByReferencedData(sId) ? "natureRelationships" : "contentRelationships";
    }
  };

  var _isVariantRelationship = function (sRelationshipType) {
    switch (sRelationshipType) {

      case RelationshipTypeDictionary.PRODUCT_VARIANT:
        return true;

      default:
        return false;
    }
  };

  var _isProductVariantRelationship = function (sRelationshipType) {
    switch (sRelationshipType) {

      case RelationshipTypeDictionary.PRODUCT_VARIANT:
        return true;

      default:
        return false;
    }
  };

  var _isContextTypeContextualVariant = function (sType) {
    return (sType == ContextTypeDictionary.CONTEXTUAL_VARIANT);
  };

  var _isContextTypeProductVariant = function (sType) {
    return (sType == ContextTypeDictionary.PRODUCT_VARIANT);
  };

  var _getSelectedTabId = function (sActiveEntityId){
    var oScreenProps = ContentUtils.getComponentProps().screen;
    var oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();

    if(CS.isEmpty(sActiveEntityId)){
      var oActiveEntity = ContentUtils.getActiveEntity();
      if(!CS.isEmpty(oActiveEntity)){
        sActiveEntityId = oActiveEntity.id;
      }
    }

    var sSelectedTabId = null;
    if(!CS.isEmpty(sActiveEntityId)){
      if(CS.isEmpty(oActiveEntitySelectedTabIdMap[sActiveEntityId])){
        oActiveEntitySelectedTabIdMap[sActiveEntityId] = {selectedTabId: null}
      } else if(CS.isEmpty(oActiveEntitySelectedTabIdMap[sActiveEntityId].selectedTabId)){
        oActiveEntitySelectedTabIdMap[sActiveEntityId].selectedTabId = null;
      }
      sSelectedTabId = oActiveEntitySelectedTabIdMap[sActiveEntityId].selectedTabId;
    }
    return sSelectedTabId;
  };

  let _getActiveEntitySelectedFilterId = function () {
    let oScreenProps = ContentUtils.getComponentProps().screen;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let sActiveEntityId = _getActiveEntity().id;
    if (CS.isEmpty(oActiveEntitySelectedTabIdMap[sActiveEntityId].selectedFilterId)) {
      oActiveEntitySelectedTabIdMap[sActiveEntityId].selectedFilterId = ContentEditFilterItemsDictionary.ALL;
    }
    return oActiveEntitySelectedTabIdMap[sActiveEntityId].selectedFilterId;
  };

  let _setActiveEntitySelectedFilterId = function (sSelectedFilterId) {
    let oScreenProps = ContentUtils.getComponentProps().screen;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let sActiveEntityId = _getActiveEntity().id;
    if(!CS.isEmpty(oActiveEntitySelectedTabIdMap[sActiveEntityId])){
      oActiveEntitySelectedTabIdMap[sActiveEntityId].selectedFilterId = sSelectedFilterId;
    }
  };

  var _getDecodedHtmlContent = function (sHtml) {
    var sDecodedHtml;
    var txt = document.createElement("div");
    txt.innerHTML = sHtml;
    sDecodedHtml = txt.innerText;

    /** To remove char code 160 from string which looks like SPACE(" ") but it is not**/
    sDecodedHtml = sDecodedHtml.replace(/\u00A0/g, " ");

    return sDecodedHtml;
  };

  var _getDisplayUnitFromDefaultUnit = function (sDefaultUnit, sType) {
    var oMeasurement = {};
    let oMeasurementMetricsAndImperial = new MockDataForMeasurementMetricAndImperial();
    if (!CS.isEmpty(sType)) {
      if(sType === AttributeTypeDictionary.CUSTOM_UNIT) {
        sDefaultUnit = sDefaultUnit || "";
      } else {
        var aMeasurement = oMeasurementMetricsAndImperial[sType];
        oMeasurement = CS.find(aMeasurement, {unit: sDefaultUnit});
      }
    } else {
      CS.forEach(oMeasurementMetricsAndImperial, function (aTypeMeasurement) {
        var oTemp = CS.find(aTypeMeasurement, {unit: sDefaultUnit});
        if (!CS.isEmpty(oTemp)) {
          oMeasurement = oTemp;
          return false;
        }
      })
    }

    return oMeasurement.unitToDisplay || sDefaultUnit;
  };

  var _getConcatenatedAttributeExpressionList = function (oAttribute, aEntityAttributes, aEntityTags, oReferencedAttributes,
      oReferencedTags,oReferencedElements, oSelectedLanguageForComparison) {
    let aTemp = [];
    let sBaseType = "";
    let sType = "";
    let sValue = "";
    let sValueAsHtml = "";
    let sEntityId = "";
    let oEntityAttribute = {};
    let oReferencedAttribute = {};
    let sBaseUnit = "";
    let oMasterTag = {};
    let aChildren = [];
    let aTagValueSequence = [];
    let oTag = {};
    let aTagValues = [];
    let aSelectedTagValues = [];
    let aSelectedTagValuesIds = [];
    let oFirstSelectedMasterTagValueBySequence = {};
    let sFirstSelectedTagValueIdBySequence = "";
    let sUnitToDisplay = "";
    let aExpressionList = [];

    let oMasterAttribute =oReferencedAttributes[oAttribute.attributeId];

    aExpressionList = (!CS.isEmpty(oAttribute) && !CS.isEmpty(oAttribute.valueAsExpression)) ? oAttribute.valueAsExpression : oMasterAttribute.attributeConcatenatedList;

    let oNumberFormat = {};
    let oDateFormat = {};
    if(oSelectedLanguageForComparison) {
      oNumberFormat = oNumberFormatDictionary[oSelectedLanguageForComparison.numberFormat] ;
      oDateFormat = oDateFormatDictionary[oSelectedLanguageForComparison.dateFormat];
    }

    CS.forEach(aExpressionList, function (oExpression) {
      sBaseType = "";
      sType = oExpression.type;
      sValue = oExpression.value || "";
      sValueAsHtml = oExpression.valueAsHtml || "";
      sEntityId = oExpression.attributeId;

      if (sType === "attribute") {
        oEntityAttribute = CS.find(aEntityAttributes, { attributeId: sEntityId }) || aEntityAttributes[sEntityId];
        sValue = oEntityAttribute ? oEntityAttribute.value : "";
        oReferencedAttribute = CS.find(oReferencedAttributes, { id: sEntityId });

        if (CS.isEmpty(oReferencedAttribute)) {
          sBaseType = "";
          sValue = ContentUtils.getDecodedTranslation( getTranslation().ENTITY_NOT_AVAILABLE, {entity : getTranslation().ATTRIBUTE} );
        } else {
          sBaseType = oReferencedAttribute.type;
        }

        let sPrecision = "";
        if ((ContentUtils.isAttributeTypePrice(sBaseType) || ContentUtils.isAttributeTypeNumber(sBaseType)
            || ContentUtils.isAttributeTypeMeasurement(sBaseType) || ContentUtils.isAttributeTypeCalculated(sBaseType))) {
          sPrecision = oReferencedAttribute.precision;
          let oReferencedElement = oReferencedElements[sEntityId];
          if (CS.isNotEmpty(oReferencedElement) && CS.isNumber(oReferencedElement.precision)) {
            sPrecision = oReferencedElement.precision
          }
        }

        if (AttributeUtils.isAttributeTypeMeasurement(sBaseType)) {
          sBaseUnit = AttributeUtils.getBaseUnitFromType(sBaseType);
          sValue = AttributeUtils.getMeasurementAttributeValueToShow(sValue, sBaseUnit, oReferencedAttribute.defaultUnit, sPrecision);
          sValue += "";

          if (!CS.isEmpty(sValue)) {
            sUnitToDisplay = _getDisplayUnitFromDefaultUnit(oReferencedAttribute.defaultUnit, sBaseType);
            sValue = _getCurrentLocaleNumberValue(sValue, sPrecision, {}, oReferencedAttribute.hideSeparator) + " " + sUnitToDisplay;
          }
        }
        else if (AttributeUtils.isAttributeTypeDate(sBaseType)) {
          sValue = _formatDate(sValue, oDateFormat);
        }
        else if (AttributeUtils.isAttributeTypeRole(sBaseType) || AttributeUtils.isAttributeTypeUser(sBaseType)) {
          sValue = _getUserNameById(sValue);
        }
        else if (AttributeUtils.isAttributeTypeHtml(sBaseType) && !CS.isEmpty(oEntityAttribute)) {
          sValue = _getDecodedHtmlContent(oEntityAttribute.valueAsHtml || "");
          sValueAsHtml = oEntityAttribute.valueAsHtml;
        }
        else if (AttributeUtils.isAttributeTypeNumber(sBaseType) && !CS.isEmpty(oEntityAttribute)) {
          sValue = _getCurrentLocaleNumberValue(oEntityAttribute.valueAsNumber, sPrecision, oNumberFormat,
              oReferencedAttribute.hideSeparator);
        }
      } else if (sType === "tag") {
        /** To set visual tag value label from referenced tag **/
        sEntityId = oExpression.tagId;
        oMasterTag = oReferencedTags[sEntityId] || {};

        if (CS.isEmpty(oMasterTag)) {
          sValue = ContentUtils.getDecodedTranslation( getTranslation().ENTITY_NOT_AVAILABLE, {entity : getTranslation().TAG});
        } else {
          aChildren = oMasterTag.children;
          aTagValueSequence = oMasterTag.tagValuesSequence;
          oTag = CS.find(aEntityTags, { tagId: sEntityId }) || aEntityTags[sEntityId];

          if (!CS.isEmpty(oTag)) {
            aTagValues = oTag.tagValues;

            /** Show only first selected tag value according to tag value sequences **/
            aSelectedTagValues = CS.filter(aTagValues, { relevance: 100 });
            aSelectedTagValuesIds = !CS.isEmpty(aSelectedTagValues) ? CS.map(aSelectedTagValues, "tagId") : [];
            oFirstSelectedMasterTagValueBySequence = {};
            if (!CS.isEmpty(aSelectedTagValuesIds)) {
              sFirstSelectedTagValueIdBySequence = CS.find(aTagValueSequence, function (sTagId) {
                return CS.includes(aSelectedTagValuesIds, sTagId);
              });
              oFirstSelectedMasterTagValueBySequence = CS.find(aChildren, { id: sFirstSelectedTagValueIdBySequence });
            }

            if (!CS.isEmpty(oFirstSelectedMasterTagValueBySequence)) {
              sValue = oMasterAttribute.isCodeVisible ? oFirstSelectedMasterTagValueBySequence.code : oFirstSelectedMasterTagValueBySequence.label;
            }
          }
        }
      } else if (sType === "html") {
        sValueAsHtml = oExpression.valueAsHtml;
        sValue = oExpression.value;
      }

      aTemp.push({
        baseType: sBaseType,
        type: sType,
        value: sValue || "",
        valueAsHtml: sValueAsHtml || "",
        entityId: sEntityId,
        order: oExpression.order,
        id: oExpression.id
      });
    });

    return aTemp;
  };

  var _getPaginationLimitAccordingToZoomLevel = function (iZoomLevel) {
    switch (iZoomLevel) {
      case 1:
        return ThumbnailCountForZoomLevels[1];

      case 2:
        return ThumbnailCountForZoomLevels[2];

      case 3:
        return ThumbnailCountForZoomLevels[3];

      case 4:
        return ThumbnailCountForZoomLevels[4];
    }
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

  var _isAllRelevanceZero = function (aTags){
    var bAllRelevanceZero = true;

    CS.forEach(aTags, function (oTag){
      var aTagValues = oTag.tagValues;
      var bBreakOuterLoop = false;
      CS.forEach(aTagValues, function (oTagValue){
        if(oTagValue.relevance != 0){
          bAllRelevanceZero = false;
          bBreakOuterLoop = true;
          return false;
        }
      });

      if(bBreakOuterLoop){
        return false
      }
    });
    return bAllRelevanceZero;
  };

  var _removeTrailingBreadcrumbPath = function (sId, sType) {
    return CommonUtils.removeTrailingBreadcrumbPath(sId, sType);
  };

  var _getTabUrlFromTabId = function (sTabId = null) {
    var oTabItems = ContentScreenConstants.tabItems;
    switch (sTabId) {
      case oTabItems.TAB_TASKS:
        return "tasktab";
      case oTabItems.TAB_TIMELINE:
        return "timelinetab";
      case oTabItems.EMPTY:
      case oTabItems.TAB_OVERVIEW:
        return "overviewtab";
      // TODO: ADD FOR ALL TABS
      default:
        return "customtab"
    }
  };

  let _getTabTypeFromTabId = (sTabId) => {
    var oTabItems = ContentScreenConstants.tabItems;

    switch (sTabId) {

      case oTabItems.TAB_TASKS:
        return TemplateTabTypeConstants.TASKS_TAB_BASE_TYPE;
      case oTabItems.TAB_TIMELINE:
        return TemplateTabTypeConstants.TIMELINE_TAB_BASE_TYPE;
      default:
        return oTabItems.TAB_CUSTOM;
    }
  };

  var _setThumbnailModeToXRayVisionMode = function (sRelationshipId) {
    var oScreenProps = ContentScreenProps.screen;
    if(sRelationshipId) {
      var oRelationshipProps = ContentScreenProps.relationshipView.getRelationshipToolbarProps();
      oRelationshipProps[sRelationshipId].isXRayEnabled = true;
    }
    else {

      if (_getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()) {
        oScreenProps.setSectionInnerThumbnailMode(ThumbnailModeConstants.XRAY);
      } else {
        oScreenProps.setThumbnailMode(ThumbnailModeConstants.XRAY);
      }
    }
  };

  var _isXRayVisionModeActive = function () {
    var oScreenProps = ContentScreenProps.screen;
    var sThumbnailMode = oScreenProps.getThumbnailMode();
    return (sThumbnailMode == ThumbnailModeConstants.XRAY);
  };

  var _getRelationshipTypeById = function (sRelationshipId) {
    var oScreenProps = ContentScreenProps.screen;
    var oReferencedNatureRelationships = oScreenProps.getReferencedNatureRelationships();

    var sRelationshipType = "general";
    var oFoundRelationship = oReferencedNatureRelationships[sRelationshipId];
    if(CS.isEmpty(oFoundRelationship)){
      var oReferencedRelationships = oScreenProps.getReferencedRelationships();
      oFoundRelationship = oReferencedRelationships[sRelationshipId];
    }

    if(CS.isEmpty(oFoundRelationship)){
      oFoundRelationship = oScreenProps.getCommonReferencedRelationships()[sRelationshipId];
    }

    if(!CS.isEmpty(oFoundRelationship)){
      sRelationshipType = oFoundRelationship.relationshipType || oFoundRelationship.natureType;
    }
    return sRelationshipType || "general";
  };

  var _getSelectedModule = function () {
    var aModuleList = GlobalStore.getAllMenus();
    return CS.find(aModuleList, {isSelected: true});
  };

  let _setSelectedModuleById = (sId) => {
    let oPreviouslySelectedModule = null;
    let aModuleList = GlobalStore.getAllMenus();
    let bIsAnythingSelected = false;
    CS.forEach(aModuleList, function (oModule) {
      if (oModule.isSelected) {
        oPreviouslySelectedModule = oModule;
      }
      oModule.isSelected = (sId === oModule.id);
      bIsAnythingSelected = bIsAnythingSelected || oModule.isSelected;
    });

    if(!bIsAnythingSelected) {
      oPreviouslySelectedModule.isSelected = true;
    }
    ContentScreenProps.screen.setPreviouslySelectedModuleId(oPreviouslySelectedModule.id);

    /**
     * If you don't find your desired module selection here, plz do yourself a favor and check
     * global-store -> '_setMenuSelection'
     * I found some irregularities in this function so didn't move that in here.
     */
  };

  /**
   * @function _setSelectedModuleAndDefaultDataById
   * @description - To set selected module and its default view, defaults zoom level.
   * @param sModuleId - Module Id.
   * @private
   */
  let _setSelectedModuleAndDefaultDataById = function (sModuleId) {
    _setSelectedModuleById(sModuleId);
    let oSelectedModule = _getSelectedModule();
    let oScreenProps = ContentScreenProps.screen;
    if (oSelectedModule.defaultView !== ContentScreenConstants.viewModes.JOB_SCREEN_MODE) {
      oScreenProps.setDefaultViewMode(oSelectedModule.defaultView);
      oScreenProps.setViewMode(oSelectedModule.defaultView);
    }
    oScreenProps.setCurrentZoom(oSelectedModule.defaultZoomLevel);
  };

  var _discardCloneIfActiveEntityAndActiveEntityCloneEqual = function () {
    var oActiveEntity = _getActiveEntity();
    var oActiveEntityClone = oActiveEntity.contentClone;
    if(!oActiveEntityClone){
      return;
    }

    var oTempActiveEntityClone = CS.cloneDeep(oActiveEntity);
    delete oTempActiveEntityClone.contentClone;
    if(CS.isEqual(oTempActiveEntityClone, oActiveEntityClone)){
      delete oActiveEntity.contentClone;
      oActiveEntity.isDirty = false;
    }
  };

  var _getEntityClassType = function (oEntity) {
    if(oEntity.__NATURE_KLASS_ID) {
      return oEntity.__NATURE_KLASS_ID;
    }

    const sType = ContentUtils.getNatureKlassIdFromKlassIds(oEntity.types);
    oEntity.__NATURE_KLASS_ID = sType;

    return sType || oEntity.types[0];
  };

  var _getDummyVariant = function () {
      return {
        name: UniqueIdentifierGenerator.generateUntitledName(),
        attributes: [],
        tags: [],
        timeRange: {from: null, to: null},
        statusTagInstances: [],
        isCreated: true
      };
  };

  var _activeCollectionHierarchySafetyCheck = function () {
    //safe to proceed if true is returned.
    var oScreenProps = ContentScreenProps.screen;
    var oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
    if(oActiveHierarchyCollection && oActiveHierarchyCollection.clonedObject) {
      oScreenProps.setShakingStatus(true);
      _triggerChange();
    } else {
      return true;
    }
  };

  var _activeTaxonomyOrganiseSafetyCheck = function (oFilterContext) {
    //safe to proceed if true is returned.
    var oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    var oTaxonomySections = oFilterProps.getTaxonomyNodeSections();
    if(oTaxonomySections && oTaxonomySections.clonedObject) {
      var oScreenProps = ContentScreenProps.screen;
      oScreenProps.setShakingStatus(true);
      _triggerChange();
    } else {
      return true;
    }
  };

  var _getEntityByIdUrl = function (sEntityBaseType) {
    var sScreenContext = _getScreenModeBasedOnEntityBaseType(sEntityBaseType);
    return getRequestMapping(sScreenContext).GetEntityById;
  };

  var _putRequest = function (sUrl, oQueryString, oRequestData, fSuccess, fFailure, oExtraData) {
    CS.putRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure, oExtraData);
  };

  var _deleteRequest = function (sUrl, oQueryString, oRequestData, fSuccess, fFailure) {
    CS.deleteRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure);
  };

  var _postRequest = function (sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader, oExtraData) {
    CS.postRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader, oExtraData);
  };

  var _getRequest = function (sUrl, oParameters, fSuccess, fFailure, oExtraData) {
    CS.getRequest(sUrl, oParameters, fSuccess, fFailure, oExtraData);
  };

  var _getSelectedHierarchyContext = function () {
    var oScreenProps = ContentScreenProps.screen;
    var sContext = "";
    if(oScreenProps.getIsCollectionHierarchySelected()){
      sContext = HierarchyTypesDictionary.COLLECTION_HIERARCHY;
    }
    else if(oScreenProps.getIsTaxonomyHierarchySelected()){
      sContext = HierarchyTypesDictionary.TAXONOMY_HIERARCHY;
    }

    return sContext;
  };

  //moved from content-store for code reuse in content, relationship object
  let _addElementDataFromClassIntoEntity = function (oSection, oEntity) {
    let sSplitter = ContentUtils.getSplitter();
    let sTagContext = 'tag';
    let sRoleContext = 'role';
    CS.forEach(oSection.elements, function (oElement) {
      switch (oElement.type) {
        case "attribute":
          let oMasterAttribute = oElement.attribute;
          let bIsImageCoverflowAttr = ContentUtils.isAttributeTypeCoverflow(oMasterAttribute.type);
          if (!bIsImageCoverflowAttr) {
            ContentUtils.addAttributeDataInEntityFromSectionElement(oEntity, oElement);
          }
          ContentUtils.addElementToVariantVersionMap(oElement, bIsImageCoverflowAttr);

          let iMaxAllowedItems = oElement.numberOfItemsAllowed || oMasterAttribute.numberOfItemsAllowed;
          if (oEntity.baseType == BaseTypesDictionary.assetBaseType) {
            iMaxAllowedItems = 1;
          }
          if (ContentUtils.isAttributeTypeCoverflow(oElement.attribute.type)) {
            ImageCoverflowUtils.setMaxCoverflowItemAllowed(iMaxAllowedItems);
          }

          if (oElement.couplingType == CouplingConstants.DYNAMIC_COUPLED || oElement.couplingType == CouplingConstants.READ_ONLY_COUPLED) {
            oElement.isDisabled = true;
          }
          break;

        case "relationship":
          if(!_isNatureRelationship(oElement.relationship.relationshipType) &&
            !_isVariantRelationship(oElement.relationship.relationshipType) && !_isProductVariantRelationship(oElement.relationship.relationshipType)) {
            ContentUtils.addRelationshipInEntity(oElement, oEntity);
          }
          break;

        case "tag":
          ContentUtils.addTagInEntity(oEntity, oElement);
          ContentUtils.addElementToVariantVersionMap(oElement);
          if (oElement.couplingType == CouplingConstants.DYNAMIC_COUPLED || oElement.couplingType == CouplingConstants.READ_ONLY_COUPLED) {
            oElement.isDisabled = true;
          }
          break;

        case "role":
          ContentUtils.addRoleDataInEntityFromSectionElement(oEntity, oElement);
          ContentUtils.addElementToVariantVersionMap(oElement);
          break;

        case "taxonomy":
        case "filler":
          if (oElement.elements) {
            _addElementDataFromClassIntoEntity(oElement, oEntity);
          }
          break;

      }
    });
  };

  let _addSectionDataFromClassIntoEntity = function (oEntity, sContext) {
    trackMe('_addDataFromClassIntoContent');
    ContentLogUtils.debug('_addDataFromClassIntoContent', oEntity);

    let aActiveSections = ContentScreenProps.screen.getActiveSections();
    let aActiveNatureSections = ContentScreenProps.screen.getActiveNatureSections();
    let aActiveVariantSections = ContentScreenProps.screen.getActiveVariantSections();
    let aAllSections = aActiveSections.concat(aActiveNatureSections);
    aAllSections = aAllSections.concat(aActiveVariantSections);

    CS.forEach(aAllSections, function (oSection) {
      _addElementDataFromClassIntoEntity(oSection, oEntity);
    });
  };

  var _allHierarchySafetyCheck = function () {
    var sSelectedHierarchyContext = _getSelectedHierarchyContext();
    var bCallNextFunction = true;
    if(sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
      let oTaxonomyHierarchyFilterContext = {
        filterType: oFilterPropType.HIERARCHY,
        screenContext: HierarchyTypesDictionary.TAXONOMY_HIERARCHY
      };
      bCallNextFunction = !!ContentUtils.activeTaxonomyOrganiseSafetyCheck(oTaxonomyHierarchyFilterContext);
    }
    else if(sSelectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
      bCallNextFunction = !!ContentUtils.activeCollectionHierarchySafetyCheck();
    }

    return bCallNextFunction;
  };

  var _allHierarchyNonDirty = function (oFilterContext) {
    var sSelectedHierarchyContext = _getSelectedHierarchyContext();
    var bCallNextFunction = true;
    if(sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
      var oFilterProps = ContentUtils.getFilterProps(oFilterContext);
      var oTaxonomySections = oFilterProps.getTaxonomyNodeSections();
      bCallNextFunction = !(oTaxonomySections && oTaxonomySections.clonedObject);
    }
    else if(sSelectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
      var oScreenProps = ContentScreenProps.screen;
      var oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
      bCallNextFunction = !(oActiveHierarchyCollection && oActiveHierarchyCollection.clonedObject);
    }

    return bCallNextFunction;
  };

  var _getIsAnyHierarchySelectedExceptFilterHierarchyAndRelationshipHierarchy = function () {
    var sSelectedHierarchyContext = _getSelectedHierarchyContext();
    return (
    sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY ||
    sSelectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY);
  };

  var _convertTimeStampToEOD = function (sValue) {
    try {
      var oDate = new Date(sValue);
      oDate.setHours(23, 59, 59, 999);
      return oDate.getTime();
    } catch (oException) {
      ExceptionLogger.error(oException);
    }

  };

  var _convertTimeStampToSOD = function (sValue) {
    try {
      var oDate = new Date(sValue);
      oDate.setHours(0, 0, 0, 0);
      return oDate.getTime();
    } catch (oException) {
      ExceptionLogger.error(oException);
    }

  };

  var _getElementAssetData = function (oElement) {
    var oElementAssetData = {};
    try {
      var oReferencedAsset = !CS.isEmpty(oElement.referencedAssets) && CS.find(oElement.referencedAssets, {isDefault: true}) || {};
      if (oElement && oElement.baseType == BaseTypesDictionary.assetBaseType) {
        var oAssetAttribute = oElement.assetInformation;
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
      let sMp4Src = '';

      if(!CS.isEmpty(oReferencedAsset)) {
        sThumbKeySRC = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
          type: oReferencedAsset.type,
          id: oReferencedAsset.thumbKey
        });
        sImageSrc = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
          type: oReferencedAsset.type,
          id: oReferencedAsset.assetObjectKey
        });
        sPreviewSrc = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
          type: oReferencedAsset.type,
          id: oReferencedAsset.previewImageKey
        });
        sMp4Src = oReferencedAsset.properties && oReferencedAsset.properties.mp4 && RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
          type: oReferencedAsset.type,
          id: oReferencedAsset.properties.mp4
        });

        sExtention = oReferencedAsset.properties && oReferencedAsset.properties.extension || '';
      }

      oElementAssetData = {
        fileName: oReferencedAsset.fileName,
        thumbKeySrc: sThumbKeySRC,
        imageSrc: sImageSrc,
        extension: sExtention,
        assetObjectKey: oReferencedAsset.assetObjectKey,
        mp4Src: sMp4Src,
        previewSrc: sPreviewSrc
      };
    }
    catch(oException) {
      ExceptionLogger.error(oException);
    }

    return oElementAssetData;
  };

  //this function updates Nature & Non Nature klass ids based on referenced klasses
  var _updateNatureNonNatureKlassIds = function (oReferencedKlasses) {
    var aNatureKlassIds = [];

    CS.forEach(oReferencedKlasses, function (oKlass) {
      if (oKlass.isNature) {
        aNatureKlassIds.push(oKlass.id);
      }
    });

    ContentScreenProps.screen.setArticleNatureKlassIds(aNatureKlassIds);
  };

  //this function returns klass data from referenced klasses for given klass id
  var _getKlassFromReferencedKlassesById = function(sKlassId){
    var oReferencedKlasses = ContentScreenProps.screen.getReferencedClasses();
    return oReferencedKlasses[sKlassId] || {};
  };

  //this function updates the referenced klasses, nature & non-nature klass ids related data
  var _addDataToReferencedKlasses = function (oReferencedKlassesToAdd) {
    if (!CS.isEmpty(oReferencedKlassesToAdd)) {
      var oReferencedKlasses = ContentScreenProps.screen.getReferencedClasses();
      CS.forEach(oReferencedKlassesToAdd, function (oData, sKey) {
        oReferencedKlasses[sKey] = oData
      });
      _updateNatureNonNatureKlassIds(oReferencedKlasses);
    }
  };

  //this function updates the referenced klasses, nature & non-nature klass ids related data
  var _updateReferencedKlassRelatedData = function (oReferencedKlasses){
    if(!CS.isEmpty(oReferencedKlasses)){
      _addDataToReferencedKlasses(oReferencedKlasses);
    }
  };

  var _clearXRayData = function () {
    if (ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()) {
      ContentScreenProps.screen.setSectionInnerThumbnailMode(ThumbnailModeConstants.BASIC);
      ContentScreenProps.screen.setInnerActiveXRayPropertyGroup({});
    }
    else {
      ContentScreenProps.screen.setThumbnailMode(ThumbnailModeConstants.BASIC);
      ContentScreenProps.screen.setActiveXRayPropertyGroup({});
    }
  };

  let _clearXRayDataFromBreadCrumbPayLoad = () => {
    let aBreadCrumbData = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    let iIndex = aBreadCrumbData.length;
    let oLatestBreadCrumb = aBreadCrumbData[iIndex -1];
    let oPostData = oLatestBreadCrumb.payloadData[1].postData;
    oPostData.xrayAttributes = [];
    oPostData.xrayTags = [];
    oPostData.xrayEnabled = false;
  };

  var _getActiveXRayPropertyGroup = function (sRelationshipId) {
    if(sRelationshipId) {
      var oRelationshipProps = ContentScreenProps.relationshipView;
      var oRelationshipToolbarProps = oRelationshipProps.getRelationshipToolbarProps();
      return oRelationshipToolbarProps[sRelationshipId].activeXRayPropertyGroup;
    }
    else if (ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()) {
      let sInnerThumbnailMode = ContentScreenProps.screen.getSectionInnerThumbnailMode();
      if (sInnerThumbnailMode === ThumbnailModeConstants.XRAY) {
        return ContentScreenProps.screen.getInnerActiveXRayPropertyGroup();
      } else {
        return {};
      }
    }
    else {
      let sThumbnailMode = ContentScreenProps.screen.getThumbnailMode();
      if (sThumbnailMode === ThumbnailModeConstants.XRAY) {
        return ContentScreenProps.screen.getActiveXRayPropertyGroup();
      } else {
        return {};
      }
    }
  };

  var _setActiveXRayPropertyGroup = function (oData,sRelationshipId) {
    if(sRelationshipId) {
      var oRelationshipProps = ContentScreenProps.relationshipView;
      var oRelationshipToolbarProps = oRelationshipProps.getRelationshipToolbarProps();
      oRelationshipToolbarProps[sRelationshipId].activeXRayPropertyGroup = oData;
    }
    else if (ContentUtils.getAvailableEntityViewStatus() || ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy()) {
      ContentScreenProps.screen.setInnerActiveXRayPropertyGroup(oData);
    }
    else {
      ContentScreenProps.screen.setActiveXRayPropertyGroup(oData);
    }
  };

  var _getReferencedTags = function () {
    var oScreenProps= ContentScreenProps.screen;
    return oScreenProps.getReferencedTags();
  };

  var _getIsUOMVariantDialogOpen = function () {
    var sOpenedDialogContextId = ContentScreenProps.uomProps.getOpenedDialogTableContextId();
    return !CS.isEmpty(sOpenedDialogContextId);
  };

  var _getIsAttributeVariantDialogOpen = function () {
    let oOpenedDialogAttributeData = ContentScreenProps.uomProps.getOpenedDialogAttributeData();
    return (oOpenedDialogAttributeData && oOpenedDialogAttributeData.contextId);
  };

  var _getUOMActiveContextDialogProps = function () {
    let oOpenedDialogAttributeData = ContentScreenProps.uomProps.getOpenedDialogAttributeData();
    let sOpenedDialogContextId = oOpenedDialogAttributeData.contextId || ContentScreenProps.uomProps.getOpenedDialogTableContextId();
    let oActiveContent = ContentUtils.getActiveContent();
    let sKlassInstanceId = oOpenedDialogAttributeData.klassInstanceId || oActiveContent.id;
    if(sOpenedDialogContextId) {
      let sIdForProps = _getIdForTableViewProps(sOpenedDialogContextId);
      return ContentScreenProps.tableViewProps.getVariantContextPropsByContext(sIdForProps, sKlassInstanceId);
    }
    return null;
  };

  var _getUOMDialogEditableEntity = function () {
    var oVariantContextProps = _getUOMActiveContextDialogProps();
    if(!CS.isEmpty(oVariantContextProps)) {
      var oVariant = oVariantContextProps.getDummyVariant();
      if(CS.isEmpty(oVariant)) {
        oVariant = oVariantContextProps.getActiveVariantForEditing()
      }
      return oVariant;
    }

    return null;
  };

  let _getIdForTableViewProps = function (sContextId) {
    let oOpenedDialogAttributeData = ContentScreenProps.uomProps.getOpenedDialogAttributeData();
    if (sContextId === oOpenedDialogAttributeData.contextId) {
      return sContextId + _getSplitter() + oOpenedDialogAttributeData.attributeId;
    } else {
      return sContextId;
    }
  };

  let _getIdForTableViewPropsSimple = function (sContextId, sAttributeId) {
    if (sAttributeId) {
      return sContextId + _getSplitter() + sAttributeId;
    } else {
      return sContextId;
    }
  };

  let _getLinkedInstancesFromVariant = function(oVariant){
    let oLinkedInstances = oVariant.linkedInstances || {};
    if(CS.isEmpty(oLinkedInstances) && oVariant.context){
      oLinkedInstances = oVariant.context.linkedInstances || {};
    }
    return oLinkedInstances;
  };

  let _processLinkedInstancesForContentContext = function(oVariant, oReferencedInstances){
    let oOriginalLinkedInstances = _getLinkedInstancesFromVariant(oVariant);
    let oNewLinkedInstances = {};
    CS.forEach(oOriginalLinkedInstances, function (oOriginalLinkedInstance) {
      let oEntity = CS.find(new EntitiesList(), {baseType: oOriginalLinkedInstance.baseType}); //find by baseType
      if (CS.isEmpty(oNewLinkedInstances[oEntity.id])) {
        oNewLinkedInstances[oEntity.id] = [];
      }
      let oLinkedInstance = oReferencedInstances[oOriginalLinkedInstance.id];
      if(oLinkedInstance){
        oNewLinkedInstances[oEntity.id].push(oLinkedInstance);
      }
    });
    oVariant.context.linkedInstances = oNewLinkedInstances;
  };

  var _getIsOnLandingPage = function () {
    var bAvailableEntityViewStatus = _getAvailableEntityViewStatus();
    var oActiveCollectionObj = ContentScreenProps.collectionViewProps.getActiveCollection();
    var bIsAnyHierarchySelectedExceptFilterHierarchy = _getIsAnyHierarchySelectedExceptFilterHierarchyAndRelationshipHierarchy();
    var sCurrentViewMode = _getViewMode();

    return (
        !bAvailableEntityViewStatus && CS.isEmpty(oActiveCollectionObj) && !bIsAnyHierarchySelectedExceptFilterHierarchy &&
        (
            sCurrentViewMode === ContentScreenConstants.viewModes.LIST_MODE ||
            sCurrentViewMode === ContentScreenConstants.viewModes.TILE_MODE
        )
    );
  };

  var _getDecodedTranslation = function (sStringToCompile, oParameter) {
    return CommonUtils.getParsedString(sStringToCompile, oParameter);
  };

  var _getIsTableContentDirty = function () {
    var oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    var oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    var bIsTableContentDirty = false;
    let oActiveContent = ContentUtils.getActiveContent();
    CS.forEach(oEmbeddedVariantContexts, (oContext,sContextId) => {
      let oUOMTableProps = ContentScreenProps.tableViewProps.getTableViewPropsByContext(sContextId, oActiveContent.id);
      if((oContext.type === ContextTypeDictionary.CONTEXTUAL_VARIANT ||
          oContext.type === ContextTypeDictionary.GTIN_CONTEXT ||
          oContext.type === ContextTypeDictionary.IMAGE_VARIANT
      ) && !CS.isEmpty(oUOMTableProps)) {
        let oTableOrganiserConfig = oUOMTableProps.getTableOrganiserConfig();
        if (oTableOrganiserConfig.isTableContentDirty) {
          bIsTableContentDirty = true;
        }
      }
    });
    return bIsTableContentDirty;
  };

  /**
   * @function _updateUnselectedTagValuesInTagEntity
   * @description - Function will have been executed in case of RULER_TAG_TYPE,
   * To update tag values instead of set a new selected values.
   * Here all values relevance is setting to zero, instead of selected values.
   * @param oTagGroup - Tag instance.
   * @param aTagValues - Contains selected values(non zero relevance values).
   * @private
   */
  let _updateUnselectedTagValuesInTagEntity = function (oTagGroup, aTagValues) {
    CS.forEach(oTagGroup.tagValues, function (oTagValue) {
      if (!CS.find(aTagValues, {id: oTagValue.id})) {
        oTagValue.relevance = 0;
      }
    });
  };

  var _getUpdatedEntityTagGroup = function (oTagGroup, aTagValueRelevanceData) {
    try {
      let aTagValues = [];

      CS.forEach(aTagValueRelevanceData, function (oData) {
        let oEntityTag = CS.find(oTagGroup.tagValues, {tagId: oData.tagId});
        let oTagValue = {};

        if (CS.isEmpty(oEntityTag)) {
          oTagValue = {
            id: UniqueIdentifierGenerator.generateUUID(),
            tagId: oData.tagId,
            relevance: oData.relevance,
            timestamp: new Date().getTime()
          };
        } else {
          oTagValue = oEntityTag;
          oTagValue.relevance = oData.relevance;
          oTagValue.timestamp = new Date().getTime();
        }

        aTagValues.push(oTagValue);
      });
      /** Updating tagValues instead of setting new selected values **/
      let aTagTypesToUpdateTagValues = [
        TagTypeConstants.RULER_TAG_TYPE,
        TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE,
        TagTypeConstants.LISTING_STATUS_TAG_TYPE
      ];
      let oReferencedTags = ContentScreenProps.screen.getReferencedTags();
      let oMasterTag = CS.find(oReferencedTags, {id: oTagGroup.tagId});
      if (oMasterTag && CS.includes(aTagTypesToUpdateTagValues, oMasterTag.tagType)) {
        _updateUnselectedTagValuesInTagEntity(oTagGroup, aTagValues);
      }
      else {
        /**Set selected values only **/
        oTagGroup.tagValues = aTagValues;
        oTagGroup.tag.tagValues = aTagValues;
      }

      oTagGroup.isValueChanged = true;

      var bIsResolved = false;
      CS.forEach(oTagGroup.conflictingValues, function (oValue) {
        bIsResolved = true;
        oValue.isResolved = bIsResolved;
      });
      oTagGroup.isResolved = bIsResolved;
      oTagGroup.isConflictResolved = bIsResolved;
      /**
       * Delete source if we edited value manually, in that case source should not be present as value not adopted from any source
       */
      if (oTagGroup.source) {
        delete oTagGroup.source;
      }

      return oTagGroup;

    } catch (oException) {
      ExceptionLogger.error(oException);
      return null;
    }
  };

  var _getUpdatedEntityTag = function (oEntity, aTagValueRelevanceData, sTagId) {
    let oTagGroup = CS.find(oEntity.tags, {tagId: sTagId});
    _getUpdatedEntityTagGroup(oTagGroup, aTagValueRelevanceData);
  };

  let _getViolatingMandatoryFields = function (aSections) {
    let aViolatingMandatoryElements = [];

    CS.forEach(aSections, function (oSection) {
      CS.forEach(oSection.elements, function (oElement) {
        if (oElement.type == "attribute") {
          if (oElement.isMandatory && (!oElement.defaultValue || !(oElement.defaultValue + "").trim())) { //trim
            // mainly
            // used to ignore the
            // new line character in html attribute
            aViolatingMandatoryElements.push(oElement.id);
          }
        } else if (oElement.type == "tag") {
          // let oProperty = oElement.tag;
          if (oElement.isMandatory){
            if(CS.isEmpty(oElement.defaultValue)){
              aViolatingMandatoryElements.push(oElement.id);
            }else if(!CS.isEmpty(oElement.defaultValue)){
              var aTagValues = oElement.defaultValue;
              var bIsAllRelevanceZero = true;
              CS.forEach(aTagValues, function (oTagValue) {
                if(oTagValue.relevance != 0){
                  bIsAllRelevanceZero = false;
                  return false;
                }
              });
              if(bIsAllRelevanceZero){
                aViolatingMandatoryElements.push(oElement.id);
              }
            }
          }
        }
      });
    });
    return aViolatingMandatoryElements;
  };

  var _updateEntitySectionDefaultTagValue = function (aSections, sSectionId, sElementId, sTagGroupId, aTagValueRelevanceData) {
    //TODO: Refactor the below If condition
    if (CS.includes(aTagValueRelevanceData, MSSViewConstants.MSS_NOTHING_FOUND_ID) ||
        CS.find(aTagValueRelevanceData, {tagId: MSSViewConstants.MSS_NOTHING_FOUND_ID})) {
      return;
    }

    let oSection = CS.find(aSections, {id: sSectionId});
    let oElement = CS.find(oSection.elements, {id: sElementId});

    oElement.defaultValue = [];
    let aDefaultValue = oElement.defaultValue;
    CS.forEach(aTagValueRelevanceData, function (oData) {
      aDefaultValue.push({
        relevance: oData.relevance,
        tagId: oData.tagId
      });
    });

    let aViolatingMandatoryFields = _getViolatingMandatoryFields(aSections);
    let oFilterProps = ContentScreenProps.filterProps;
    oFilterProps.setViolatingMandatoryElements(aViolatingMandatoryFields);
  };

  let _isContentGridOpen = () => {
    return (ContentScreenProps.contentGridProps.getIsContentGridEditViewOpen())
  };

  let _shouldSaveContentGrid = (oEvent) => {
    let oContentGridStore = _getContentGridStore();
    let sActiveGridRowId = "";
    if(oEvent && !CS.isEmpty(oEvent.activeGridRowId)){
      sActiveGridRowId = oEvent.activeGridRowId;
    }else if(oEvent && oEvent.nativeEvent && !CS.isEmpty(oEvent.nativeEvent.activeGridRowId)){
      sActiveGridRowId = oEvent.nativeEvent.activeGridRowId;
    }
    return sActiveGridRowId != oContentGridStore.getActiveContentId();
  };

  let _getEmptyFilterObject = function (iSize, iFrom) {
    return {
      attributes: [],
      tags: [],
      allSearch: '',
      size: iSize || 20,
      from: iFrom || 0,
      isAttribute: false,
      isNumeric: false,
      selectedRoles: [],
      selectedTypes: [],
      isRed: false,
      isOrange: false,
      isYellow: false,
      isGreen: false
    };
  };

  let _getTemplateIdForServer = function (sTemplateId = null) {
    if(sTemplateId == "all"){
      return null;
    }

    return sTemplateId;
  };

  let _getContextTypeBasedOnContextId = function (sContextId) {
    let sContextType = "";
    let oReferencedVariantContexts = ContentScreenProps.screen.getReferencedVariantContexts();
    CS.forEach(oReferencedVariantContexts, function (oContextTypeGroup) {
      if(oContextTypeGroup[sContextId]){
        sContextType = oContextTypeGroup[sContextId].type;
        return false;
      }
    });
    return sContextType;
  };

  let _isContentAvailableInSelectedDataLanguage = function (oKlassInstance) {
    let oActiveContent = !CS.isEmpty(oKlassInstance) ? oKlassInstance : _getActiveContent();
    let aLanguageCodes = oActiveContent.languageCodes;
    let oScreenProps = ContentScreenProps.screen;
    let sDataLanguage = "";
    let bIsContentComparisonMode = ContentScreenProps.screen.getIsContentComparisonMode();
    let bIsGoldenRecordViewSourceDialogOpen = ContentScreenProps.goldenRecordProps.getIsGoldenRecordViewSourcesDialogOpen();

    if(bIsContentComparisonMode || bIsGoldenRecordViewSourceDialogOpen){
      sDataLanguage = ContentScreenProps.screen.getSelectedLanguageForComparison();
    } else if (oScreenProps.getIsEditMode()) {
      let sSelectedTab = ContentUtils.getSelectedTabId();
      let oTimelineProps = ContentScreenProps.timelineProps;
      let bIsComparisonMode = oTimelineProps.getIsComparisonMode();
      if (sSelectedTab === TemplateTabTypeConstants.TIMELINE_TAB && bIsComparisonMode) {
        sDataLanguage = oTimelineProps.getSelectedLanguageForComparison();
      } else {
        sDataLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
      }
    } else {
      sDataLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    }

    return CS.includes(aLanguageCodes, sDataLanguage);
  };

  let _isOnHeaderDisabledTab = function () {
    var sActiveEntitySelectedTabId = _getSelectedTabId();
    let oTabItems = ContentScreenConstants.tabItems;
    return sActiveEntitySelectedTabId !== oTabItems.TAB_OVERVIEW;
  };

  let _getDesiredReferencedDataAndTypeBySectionId = function (sSectionId, oReferencedDetails) {
    let oReferencedPropertyCollections = oReferencedDetails.referencedPropertyCollections || {};
    let oReferencedVariantContexts = oReferencedDetails.referencedVariantContexts || {};
    let oReferencedRelationships = oReferencedDetails.referencedRelationships || {};
    let oReferencedNatureRelationships = oReferencedDetails.referencedNatureRelationships || {};

    let oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts || {};
    let oSectionTypes = ContentScreenConstants.sectionTypes;
    let oReferencedData, sSectionType;

    if (oReferencedPropertyCollections[sSectionId]) {
      oReferencedData = oReferencedPropertyCollections[sSectionId];
      sSectionType = oSectionTypes.SECTION_TYPE_PROPERTY_COLLECTION;
    } else if (oReferencedRelationships[sSectionId]) {
      oReferencedData = oReferencedRelationships[sSectionId];
      sSectionType = oSectionTypes.SECTION_TYPE_RELATIONSHIP;
    } else if (oReferencedNatureRelationships[sSectionId]) {
      oReferencedData = oReferencedNatureRelationships[sSectionId];
      sSectionType = oSectionTypes.SECTION_TYPE_NATURE_RELATIONSHIP;
    } else if (oEmbeddedVariantContexts[sSectionId]) {
      oReferencedData = oEmbeddedVariantContexts[sSectionId];
      sSectionType = oSectionTypes.SECTION_TYPE_CONTEXT;
    } else if (sSectionId === oSectionTypes.SECTION_TYPE_CONTEXT_SELECTION) {
      sSectionType = oSectionTypes.SECTION_TYPE_CONTEXT_SELECTION;
    } else if (sSectionId === oSectionTypes.SECTION_TYPE_SCHEDULE_SELECTION) {
      sSectionType = oSectionTypes.SECTION_TYPE_SCHEDULE_SELECTION;
    } else if (sSectionId === oSectionTypes.SECTION_TYPE_PERFORMANCE_INDICES) {
      sSectionType = oSectionTypes.SECTION_TYPE_PERFORMANCE_INDICES;
    } else if (sSectionId === oSectionTypes.SECTION_TYPE_CLASSIFICATION) {
      sSectionType = oSectionTypes.SECTION_TYPE_CLASSIFICATION;
    } else if (sSectionId === oSectionTypes.SECTION_TYPE_LIFE_CYCLE_STATUS) {
      sSectionType = oSectionTypes.SECTION_TYPE_LIFE_CYCLE_STATUS;
    } else if (sSectionId === oSectionTypes.SECTION_TYPE_CLASS_TAG_INFO) {
      sSectionType = oSectionTypes.SECTION_TYPE_CLASS_TAG_INFO;
    } else if (sSectionId === oSectionTypes.SECTION_TYPE_DOWNLOAD_INFO) {
      sSectionType = oSectionTypes.SECTION_TYPE_DOWNLOAD_INFO;
    } else if (sSectionId === oSectionTypes.SECTION_TYPE_INSTANCE_PROPERTIES) {
      sSectionType = oSectionTypes.SECTION_TYPE_INSTANCE_PROPERTIES;
    }

    return {oReferencedData, sSectionType};
  };

  let _initializeSectionExpansionState = function (aSections) {
    //todo temp implementation remove after section expansion state is maintained
    CS.forEach(aSections, function (oSections) {
      oSections.isCollapsedUI = true;
    });
  };

  var _resetModuleSelection = function () {
    var aModuleList = _getAllMenus();
    CS.forEach(aModuleList, function (oModule, iIndex) {
      oModule.isSelected = (!iIndex); //true when iIndex = 0
    });
  };

  var _getAssetDataObject = function (oReferencedAsset) {
    var oAssetData = {};
    try {
      var sExtention = '';
      var sThumbKeySRC = '';
      var sImageSrc = '';
      var sPreviewSrc = '';
      var sMp4Src = "";

      if(!CS.isEmpty(oReferencedAsset)) {
        if(oReferencedAsset.thumbKey) {
          sThumbKeySRC = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.thumbKey
          });
        }

        if(oReferencedAsset.assetObjectKey) {
          sImageSrc = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.assetObjectKey
          });
        }

        if(oReferencedAsset.previewImageKey) {
          sPreviewSrc = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.previewImageKey
          });
        }

        if(oReferencedAsset.properties && oReferencedAsset.properties.mp4){
          sMp4Src = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.properties.mp4
          });
        }

        sExtention = oReferencedAsset.properties && oReferencedAsset.properties.extension || '';
      }

      oAssetData = {
        type: oReferencedAsset ? oReferencedAsset.type : "",
        mp4Src: sMp4Src,
        fileName: oReferencedAsset ? oReferencedAsset.fileName : "",
        thumbKeySrc: sThumbKeySRC,
        imageSrc: sImageSrc,
        extension: sExtention,
        assetObjectKey: oReferencedAsset ? oReferencedAsset.assetObjectKey : "",
        previewSrc: sPreviewSrc
      };
    }
    catch(oException) {
      ExceptionLogger.error(oException);
    }

    return oAssetData;
  };

  var _getOnlySelectedKlassInstances = function (aKlassInstancesDetails, aSelectedInstanceIds) {
    var aNewKlassInstances = [];
    let oSelectedInstanceMap = {};

    CS.forEach(aKlassInstancesDetails, function (oKlassInstanceDetails) {
      var oKlassInstance = oKlassInstanceDetails.klassInstance;
      let bIsSelectedKlassInstance = CS.includes(aSelectedInstanceIds, oKlassInstance.id); /** To get selected instance from list **/
      let bIsSelectedInstanceAlreadyProcessed = oSelectedInstanceMap[oKlassInstance.id]; /** If instance already processed **/
      if(bIsSelectedKlassInstance && !bIsSelectedInstanceAlreadyProcessed) {
        aNewKlassInstances.push(oKlassInstanceDetails);
        oSelectedInstanceMap[oKlassInstance.id] = true;  //Todo : Temporary hack need to do it from backend (for chain relationship getting incorrect data)
      }
    });

    return aNewKlassInstances;
  };

  var _makeInstancesComparisonRowDataForTags = function (oLayoutData, oConfigDetails, bIsGoldenRecordComparison) {
    var oReferencedTags = oConfigDetails.referencedTags;
    var oReferencedElements = oConfigDetails.referencedElements;
    try {
      CS.forEach(oReferencedTags, function (oMasterTag, sTagId) {
        if (!bIsGoldenRecordComparison || (oReferencedElements[sTagId] && !oReferencedElements[sTagId].isSkipped)) {
          var oRowObject = {};
          CS.forEach(oMasterTag.children, function (oChildMaster) {
            let sChildLabel = CS.getLabelOrCode(oChildMaster);
            if (oMasterTag.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN && CS.isEmpty(oChildMaster.label)) {
              sChildLabel = CS.getLabelOrCode(oMasterTag);
            }
            oRowObject = {
              id: oChildMaster.id,
              label: sChildLabel,
              type: "tag",
              isDisabled: oMasterTag.isDisabled,
              rendererType: 'CHECK',
              iconKey: oChildMaster.iconKey
            };

            if(!oLayoutData.tagTable[oMasterTag.id]) {
              var oTableSkeletonData = _getTableSkeletonData();
              oTableSkeletonData.tableId = oMasterTag.id;
              oTableSkeletonData.iconKey = oMasterTag.iconKey;
              oTableSkeletonData.tableName = CS.getLabelOrCode(oMasterTag);

              oLayoutData.tagTable[oMasterTag.id] = oTableSkeletonData;
            }

            oLayoutData.tagTable[oMasterTag.id].rowData.push(oRowObject);
          });
        }
      });
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  var _makeInstancesComparisonRowDataForAttributes = function (oLayoutData, oConfigDetails, bIsGoldenRecordComparison) {
    var oReferencedAttributes = oConfigDetails.referencedAttributes;
    var oReferencedElements = oConfigDetails.referencedElements;
    try {
      CS.forEach(oReferencedAttributes, function (oMasterAttribute, sAttributeId) {
        if (!bIsGoldenRecordComparison || (oReferencedElements[sAttributeId] && !oReferencedElements[sAttributeId].isSkipped
                && !(ContentUtils.isAttributeTypeCalculated(oMasterAttribute.type)
                    || ContentUtils.isAttributeTypeConcatenated(oMasterAttribute.type)))) {
          if ((!ContentUtils.isAttributeTypeCoverflow(oMasterAttribute.type)) && (!ContentUtils.isAttributeTypeCreatedOn(oMasterAttribute.type)) &&
              (!ContentUtils.isAttributeTypeLastModified(oMasterAttribute.type)) && (!ContentUtils.isAttributeTypeUser(oMasterAttribute.type))) {
            var oRowObject = {
              id: oMasterAttribute.id,
              label: CS.getLabelOrCode(oMasterAttribute),
              type: "attribute",
              masterAttribute: oMasterAttribute,
              isDisabled: oMasterAttribute.isDisabled,
              rendererType: ContentUtils.getAttributeTypeForVisual(oMasterAttribute.type)
            };

            if(!oLayoutData.attributeTable['attribute']) { //TODO: Harcoded Code
              var oTableSkeletonData = _getTableSkeletonData();
              oTableSkeletonData.tableId = 'attribute';
              oTableSkeletonData.tableName = getTranslation().ATTRIBUTES;

              oLayoutData.attributeTable['attribute'] = oTableSkeletonData;
            }
            oLayoutData.attributeTable['attribute'].rowData.push(oRowObject);
          }
        }
      });
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  var _makeInstancesComparisonRowDataForRelationshipTab = function (oLayoutData, oConfigDetails, aKlassInstances, bIsGoldenRecordComparison) {
    var oReferencedRelationships = oConfigDetails.referencedRelationships;
    var aRelationshipIds = [];
    CS.forEach(oReferencedRelationships, function (oRelationship) {
      aRelationshipIds.push(oRelationship.id);
    });

    if (bIsGoldenRecordComparison) {
      CS.remove(aRelationshipIds, function (sRelationshipId, iIndex) {
        return sRelationshipId == "standardArticleGoldenArticleRelationship"});
    }

    _makeRelationshipComparisonData(aRelationshipIds, oReferencedRelationships, "contentRelationships",
        oLayoutData, aKlassInstances, oConfigDetails.referencedElements, bIsGoldenRecordComparison);
  };

  var _makeInstancesComparisonRowDataForNatureRelationships = function (oLayoutData, oConfigDetails, aKlassInstances, bIsGoldenRecordComparison) {
    var oReferencedRelationships = oConfigDetails.referencedNatureRelationships;
    var aRelationshipIds = [];
    CS.forEach(oReferencedRelationships, function (oRelationship) {
      aRelationshipIds.push(oRelationship.id);
    });

    _makeRelationshipComparisonData(aRelationshipIds, oReferencedRelationships, "natureRelationships",
        oLayoutData, aKlassInstances, oConfigDetails.referencedElements, bIsGoldenRecordComparison);
  };

  var _makeRelationshipComparisonData = function (aRelationshipIds, oReferencedRelationships, sKlassInstanceRelationshipKey,
                                                  oLayoutData, aKlassInstancesDetail, oReferencedElements, bIsGoldenRecordComparison) {

    try {

      CS.forEach(aRelationshipIds, function (sRelationshipId) {
        try {

          var oMasterRelationship = oReferencedRelationships[sRelationshipId];
            var aReferencedElements = CS.filter(oReferencedElements, {propertyId: oMasterRelationship.id});

            CS.forEach(aReferencedElements, function (oReferencedElement) {

                if (!oLayoutData.relationshipTable[oReferencedElement.id]) {
                 var oTableSkeletonData = _getTableSkeletonData();
                oTableSkeletonData.tableId = oReferencedElement.id;
                 oTableSkeletonData.relationshipId = sRelationshipId;

              let aMasterRelSides = [oMasterRelationship.side1, oMasterRelationship.side2];
              let oDesiredRelationshipSideMasterData = CS.find(aMasterRelSides, {id: oReferencedElement.relationshipSide.id});
              oTableSkeletonData.tableName = CS.getLabelOrCode(oDesiredRelationshipSideMasterData);

              oLayoutData.relationshipTable[oReferencedElement.id] = oTableSkeletonData;
            }

            var aElementIds = [];
            CS.forEach(aKlassInstancesDetail, function (oKlassInstance) {
              var oRelationships = oKlassInstance[sKlassInstanceRelationshipKey];
              CS.forEach(oRelationships, function (aRelationships) {
                CS.forEach(aRelationships, function (oRelationship) {
                  aElementIds = CS.union(aElementIds, [oRelationship.side2InstanceId]);
                });
              });
            });

            var aMasterKlassInstanceList = ContentScreenProps.screen.getMasterKlassInstanceListForComparison();
            var oRelationshipElement = {};
            CS.forEach(aElementIds, function (sElementId) {
              var oReferencedAssetObject = {};

              var bFoundInstance = false;
              var sDefaultAssetInstanceId = "";
              CS.forEach(aMasterKlassInstanceList, function (oMasterKlassInstance) {
                var oKlassInstance = oMasterKlassInstance.klassInstance;
                if(oKlassInstance.id === sElementId) {
                  oRelationshipElement = oMasterKlassInstance;
                  if(oKlassInstance.baseType === BaseTypesDictionary.assetBaseType) {
                    oReferencedAssetObject = oMasterKlassInstance;
                    bFoundInstance = true;
                    return false;
                  } else {
                    sDefaultAssetInstanceId = oKlassInstance.defaultAssetInstanceId;
                    return false;
                  }
                }
              });

              if(!bFoundInstance && !CS.isEmpty(sDefaultAssetInstanceId) && CS.isEmpty(oReferencedAssetObject)){
                oReferencedAssetObject = _getInstanceFromDefaultAssetInstanceIdForComparison(sDefaultAssetInstanceId);
              }

              var oKlassInstance = oRelationshipElement.klassInstance;
              var oRowObject = {
                id: oKlassInstance.id,
                label: CS.getLabelOrCode(oKlassInstance),
                type: 'relationship',
                  canAdd: bIsGoldenRecordComparison ? !!oReferencedElement.canAdd : false,
                  canDelete: bIsGoldenRecordComparison ? !!oReferencedElement.canDelete : false,
                  rendererType: 'ELEMENT'
              };

              var oAssetKlassInstance = oReferencedAssetObject.klassInstance;
              var oElementReferenced = _getElementAssetData(oAssetKlassInstance);
              if(!oLayoutData.relationshipTable[oReferencedElement.id].referencedAssetsData) {
                oLayoutData.relationshipTable[oReferencedElement.id].referencedAssetsData = {};
              }
              oLayoutData.relationshipTable[oReferencedElement.id].referencedAssetsData[oKlassInstance.id] = oElementReferenced;
              oLayoutData.relationshipTable[oReferencedElement.id].rowData.push(oRowObject);
            });
          });
        }
        catch (oException) {
          ExceptionLogger.error(oException);
        }
      });
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  var _getInstanceFromDefaultAssetInstanceIdForComparison = function (sDefaultAssetInstanceId) {
    var aInstanceList = ContentScreenProps.screen.getMasterKlassInstanceListForComparison();
    var oKlassInstanceDetails = {};
    CS.forEach(aInstanceList, function (oInstance) {
      var oKlassInstance = oInstance.klassInstance;
      if(oKlassInstance.id === sDefaultAssetInstanceId && oKlassInstance.baseType === BaseTypesDictionary.assetBaseType) {
        oKlassInstanceDetails = oInstance;
        return false;
      }
    });
    return oKlassInstanceDetails;
  };

  var _makeInstancesComparisonRowDataForHeaderInformation = function (oLayoutData, sScreenMode) {
    try {
      if (!oLayoutData.headerInformationTable['headerInformation']) {
        let oTableSkeletonData = _getTableSkeletonData();
        oTableSkeletonData.tableId = 'headerInformation';
        oTableSkeletonData.tableName = getTranslation().BASIC_INFORMATION;
        oLayoutData.headerInformationTable['headerInformation'] = oTableSkeletonData;
      }

      let oRowObject = _getKlassRowData();
      oLayoutData.headerInformationTable['headerInformation'].rowData.push(oRowObject);

      oRowObject = _getTaxonomyRowData();
      oLayoutData.headerInformationTable['headerInformation'].rowData.push(oRowObject);

      if (sScreenMode === ContentScreenConstants.entityModes.ASSET_MODE) {
        oRowObject = _getEventScheduleRowData();
        oLayoutData.headerInformationTable['headerInformation'].rowData.push(oRowObject);
      }

    } catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  var _getTaxonomyRowData = function () {
    return {
      id: "taxonomy",
      label: getTranslation().TAXONOMIES,
      type: "taxonomy",
      rendererType: "taxonomy",
      isDisabled: true, //TODO: 'bIsDisabled' is hardcoded for now, later pass it as props
    };
  };

  var _getKlassRowData = function () {
    return {
      id: "type",
      label: getTranslation().CLASSES,
      type: "type",
      rendererType: "type",
      isDisabled: true, //TODO: 'bIsDisabled' is hardcoded for now, later pass it as props
    };
  };

  var _getDefaultImageRowData = function () {
    return {
      id: "image",
      label: getTranslation().DEFAULT_IMAGE,
      type: "image",
      isDisabled: true,
      rendererType: "image",
    }
  };

  var _getEventScheduleRowData = function () {
    return {
      id: "eventSchedule",
      label: getTranslation().VALIDITY_INFO,
      type: "eventSchedule",
      isDisabled: true,
      rendererType: "eventSchedule"
    };
  };

  var _getTableSkeletonData = function () {
    return {
      rowData: [],
      columnData: [],
      firstColumnWidth: 250,
      tableName: "",
      tableId: ""
    }
  };

  var _makeInstancesComparisonRowDataForFixedHeader = function (oLayoutData) {
    try {
      if (!oLayoutData.fixedHeaderTable['fixedHeader']) {
        let oTableSkeletonData = _getTableSkeletonData();
        oTableSkeletonData.tableId = 'fixedHeader';
        oLayoutData.fixedHeaderTable['fixedHeader'] = oTableSkeletonData;
      }

      let oRowObject = _getDefaultImageRowData();
      oLayoutData.fixedHeaderTable['fixedHeader'].rowData.push(oRowObject);

    } catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  let _getTableVisibility = function (aRows) {
    return CS.isEmpty(CS.find(aRows, {isSkipped: false}));
  };

  var _prepareReferencedTaxonomyMap = function (oReferencedTaxonomy, oTaxonomyMap) {
    if(!oTaxonomyMap[oReferencedTaxonomy.id]) {
      let sBaseType = oReferencedTaxonomy.baseType;
      let sMasterTaxonomyClassName = ClassNameFromBaseTypeDictionary[TaxonomyBaseTypeDictionary.masterTaxonomy];
      let oNewObj = {
        id: oReferencedTaxonomy.id,
        label: oReferencedTaxonomy.label,
        code: oReferencedTaxonomy.code,
        parentId: oReferencedTaxonomy.parentId || null,
        icon: oReferencedTaxonomy.icon,
        iconKey: oReferencedTaxonomy.iconKey,
        taxonomyType: oReferencedTaxonomy.taxonomyType,
        customIconClassName: CS.isNotEmpty(sBaseType) ? ClassNameFromBaseTypeDictionary[sBaseType] : sMasterTaxonomyClassName
    };
      var oParent = oReferencedTaxonomy.parent;
      if(oParent) {
        oNewObj.parentId = oParent.id;
        _prepareReferencedTaxonomyMap(oParent, oTaxonomyMap);
      }
      oTaxonomyMap[oReferencedTaxonomy.id] = oNewObj;
    }
  };

  var _prepareReferencedClassMap = function (oReferencedClass, oClassMap) {
    if(!oClassMap[oReferencedClass.id]) {
      let sCustomIconClassName = oReferencedClass.customIconClassName;
      let oNewObj = {
        id: oReferencedClass.id,
        label: oReferencedClass.label,
        code: oReferencedClass.code,
        parentId: oReferencedClass.parentId || null,
        icon: oReferencedClass.icon,
        iconKey: oReferencedClass.iconKey,
        classType: oReferencedClass.classType,
        customIconClassName: sCustomIconClassName
    };
      let oParent = oReferencedClass.parent;
      if(oParent) {
        oNewObj.parentId = oParent.id;
        _prepareReferencedClassMap(oParent, oClassMap);
      }
      oClassMap[oReferencedClass.id] = oNewObj;
    }
  };

  let _getTaxonomyPath = (sLabel, sParentId, oReferencedTaxonomyMap) => {
    if (sParentId === "-1" || CS.isEmpty(sParentId)) {
      return sLabel;
    }
    let oParentTaxonomy = oReferencedTaxonomyMap[sParentId];
    sLabel = CS.getLabelOrCode(oParentTaxonomy).concat(" > ", sLabel);

    return _getTaxonomyPath(sLabel, oParentTaxonomy.parentId, oReferencedTaxonomyMap);
  };

  let _getClassPath = (sLabel, sParentId, oReferencedClassMap) => {
    if (!sParentId || sParentId === "-1") {
      return sLabel;
    }
    let oParentClass = oReferencedClassMap[sParentId];
    sLabel = CS.getLabelOrCode(oParentClass).concat(" > ", sLabel);

    return _getClassPath(sLabel, oParentClass.parentId, oReferencedClassMap);
  };

  var _prepareSelectedTaxonomiesFromMap = function (sTaxonomyId, aSelectedTaxonomies, oTaxonomyMap) {
    if(oTaxonomyMap[sTaxonomyId]) {
      let oReferencedPermissions = ContentScreenProps.screen.getReferencedPermissions();
      let oHeaderPermission = oReferencedPermissions.headerPermission || {};
      let oTaxonomy = CS.cloneDeep(oTaxonomyMap[sTaxonomyId]);
      oTaxonomy.canRemove = CS.isNotEmpty(oHeaderPermission) ? oHeaderPermission.canDeleteTaxonomy : true;
      oTaxonomy.toolTip = _getTaxonomyPath(CS.getLabelOrCode(oTaxonomy), oTaxonomy.parentId, oTaxonomyMap);
      aSelectedTaxonomies.push(oTaxonomy);
      var sParentId = oTaxonomy.parentId;

      if(!CS.isEmpty(sParentId) && sParentId != -1) {
        _prepareSelectedTaxonomiesFromMap(sParentId, aSelectedTaxonomies, oTaxonomyMap);
      }
    }
  };

  let _prepareSelectedClassesFromMap = function (sClassId, aSelectedClasses, oClassMap) {
    if(oClassMap[sClassId]) {
      let oClass = CS.cloneDeep(oClassMap[sClassId]);
      oClass.canRemove = true;
      oClass.toolTip = _getClassPath(CS.getLabelOrCode(oClass), oClass.parentId, oClassMap);
      aSelectedClasses.push(oClass);
      var sParentId = oClass.parentId;

      if(!CS.isEmpty(sParentId) && sParentId != -1) {
        _prepareSelectedClassesFromMap(sParentId, aSelectedClasses, oClassMap);
      }
    }
  };

  let _prepareSelectedTaxonomiesDataForChipsView = function (oReferencedTaxonomies, aSelectedTaxonomyIds) {
    let oTaxonomyMap = {};
    CS.forEach(oReferencedTaxonomies, function (oReferencedTaxonomy) {
      _prepareReferencedTaxonomyMap(oReferencedTaxonomy, oTaxonomyMap);
    });

    let oTaxonomies = {};
    CS.forEach(aSelectedTaxonomyIds, function (sTaxonomyId) {
      let aTaxonomy = [];
      _prepareSelectedTaxonomiesFromMap(sTaxonomyId, aTaxonomy, oTaxonomyMap);
      oTaxonomies[sTaxonomyId] = (CS.reverse(aTaxonomy));
    });

    return oTaxonomies;
  };

  let _prepareSelectedClassesDataForChipsView = function (oReferencedClasses, aSelectedClassIds) {
    let oClassMap = {};
    CS.forEach(oReferencedClasses, function (oReferencedClass) {
      _prepareReferencedClassMap(oReferencedClass, oClassMap);
    });

    let oClasses = {};
    CS.forEach(aSelectedClassIds, function (sClassId) {
      let aClass = [];
      _prepareSelectedClassesFromMap(sClassId, aClass, oClassMap);
      oClasses[sClassId] = (CS.reverse(aClass));
    });

    return oClasses;
  };

  let _getSelectedNumberFormatForInstanceComparison = function () {
    let sSelectedDLCodeForComparison = ContentScreenProps.screen.getSelectedLanguageForComparison();
    let oLanguageInfoData = SessionProps.getLanguageInfoData();
    let oDataLanguages = oLanguageInfoData.dataLanguages;
    let oSelectedDataLanguage = CS.find(oDataLanguages, {code: sSelectedDLCodeForComparison});

    return oNumberFormatDictionary[oSelectedDataLanguage.numberFormat];
  };

  var _makeInstancesComparisonColumnData = function (oLayoutData, oConfigDetails, aKlassInstances, bIsGoldenRecordComparison, oPropertyRecommendations) {
    var oReferencedAttributes = oConfigDetails.referencedAttributes;
    var oReferencedTags = oConfigDetails.referencedTags;
    let oReferencedElements = oConfigDetails.referencedElements;
    var oActiveEntity = _getActiveEntity();
    var oReferencedTaxonomies = oConfigDetails.referencedTaxonomies;
    var oReferencedKlasses = oConfigDetails.referencedKlasses;
    var oAllReferencedRelationships = CS.clone(oConfigDetails.referencedRelationships);
    CS.assign(oAllReferencedRelationships, oConfigDetails.referencedNatureRelationships);
    var oReferencedContexts = oConfigDetails.referencedVariantContexts;
    let iColumnWidth = 250;

    CS.forEach(oLayoutData.attributeTable, function (oAttributeTableData) {

      CS.remove(oAttributeTableData.rowData, function (oRowData) {
        var sAttributeId = oRowData.id;
        var bFoundProperty = false;
        let bAllColumnsAreEqual = true;
        let sValueToCompareWith = "";
        let bIsTranslatable = oRowData.masterAttribute.isTranslatable;

        CS.forEach(aKlassInstances, function (oKlassInstance, iIndex) {
          let bIsEditable = false;
          var oContent = oKlassInstance.klassInstance;
          let bIsEntityAvailableInDL = _isContentAvailableInSelectedDataLanguage(oContent);
          var oAttribute = CS.find(oContent.attributes, {attributeId: sAttributeId});
          var sId = oContent.id;
          let sContentName = _getContentName(oContent);
          var oKlassInstanceColumn = CS.find(oAttributeTableData.columnData, {id: sId});
          if (!oKlassInstanceColumn) {
            oKlassInstanceColumn = {
              id: sId,
              label: sContentName,
              forComparison: !iIndex,
              isFixed: oContent.isGoldenRecord || null,
              isGoldenRecord: oContent.isGoldenRecord,
              width: iColumnWidth,
              properties: {}
            };

            if (bIsGoldenRecordComparison) {
              if (oContent.isGoldenRecord) {
                oKlassInstanceColumn.forComparison = true;
              } else {
                oKlassInstanceColumn.forComparison = false;
              }
            }

            oAttributeTableData.columnData.push(oKlassInstanceColumn);
          }

          if(oContent.isGoldenRecord) {
            bIsEditable = true;
          }

          var sValue = "";
          let sOriginalValue = "";
          let sValueAsHtml = "";
          var oReferencedAssetsData = null;
          var oCoverflowAttribute = null;
          let bIsRecommended = false;
          if (oAttribute) {
            bFoundProperty = true;
            var oMasterAttribute = oReferencedAttributes[sAttributeId];
            let sAttributeType = oMasterAttribute.type;
            if (AttributeUtils.isAttributeTypeHtml(sAttributeType)) {
              sValue = oAttribute.valueAsHtml;
              sValueAsHtml = oAttribute.value;
            }
            else if (ContentUtils.isAttributeTypeNumber(sAttributeType) || ContentUtils.isAttributeTypeCalculated(sAttributeType)) {
              let oNumberFormat = _getSelectedNumberFormatForInstanceComparison();
              sValue = NumberUtils.getValueToShowAccordingToNumberFormat(oAttribute.value, oMasterAttribute.precision,
                  oNumberFormat, oMasterAttribute.hideSeparator);
              sOriginalValue = oAttribute.value;
            }
            else if (ContentUtils.isAttributeTypeMeasurement(sAttributeType)) {
              sValue = AttributeUtils.getLabelByAttributeType(sAttributeType, oAttribute.value, oMasterAttribute.defaultUnit,
                  oMasterAttribute.precision, oMasterAttribute.hideSeparator);
              sOriginalValue = oAttribute.value;
            }
            else if (AttributeUtils.isAttributeTypeUser(sAttributeType)) {
              var oUser = _getUserById(oAttribute.value);
              sValue = oUser ? oUser.label : "";
            } else if (AttributeUtils.isAttributeTypeConcatenated(sAttributeType)) {
              let oAttribute = CS.find(oContent.attributes, {attributeId: oRowData.id});
              let aConcatenatedExpressionList = _getConcatenatedAttributeExpressionList(oAttribute, oContent.attributes, oContent.tags, oReferencedAttributes, oReferencedTags, oReferencedElements);

              CS.forEach(aConcatenatedExpressionList, (attribute) => {
                sValue = sValue.concat(attribute.value);
              });
            } else {
              sValue = oAttribute.value;
            }

            if(!iIndex) {
              sValueToCompareWith = sValue;
            } else if(!CS.isEqual(sValueToCompareWith, sValue) && bAllColumnsAreEqual){
              bAllColumnsAreEqual = !bAllColumnsAreEqual;
            }
            if (bFoundProperty && bIsGoldenRecordComparison && oPropertyRecommendations[sAttributeId] ) {
              let oRecommendedValue = oPropertyRecommendations[sAttributeId];
              if ((CS.isArray(oRecommendedValue) && oContent.id == oRecommendedValue[0]) || oContent.id == oRecommendedValue) {
                bIsRecommended = true;
              }
            }
          }

          let bIsDisabled = false;
          if (oAttribute && oAttribute.conflictingValues.length && (oAttribute.conflictingValues[0].couplingType == CouplingConstants.DYNAMIC_COUPLED || oAttribute.conflictingValues[0].couplingType == CouplingConstants.READ_ONLY_COUPLED)) {
            bIsDisabled = true;
          }

          oKlassInstanceColumn.properties[sAttributeId] = {
            value: sValue,
            originalValue: sOriginalValue,
            isDisabled: bIsGoldenRecordComparison ? bIsDisabled : true,
            isEditable: bIsEditable,
            isRecommended: bIsRecommended,
            isNotApplicable: bIsTranslatable && !bIsEntityAvailableInDL,
            referencedAssetsData: oReferencedAssetsData,
            coverflowAttribute: oCoverflowAttribute
          };

          if(sValueAsHtml) {
            oKlassInstanceColumn.properties[sAttributeId].valueAsHtml = sValueAsHtml;
          }
        });

        oRowData.isSkipped = bAllColumnsAreEqual;

        return !bFoundProperty;
      });

      oAttributeTableData.allRowsAreEqual = _getTableVisibility(oAttributeTableData.rowData);

    });

    var aTagIdsToDelete = [];
    CS.forEach(oLayoutData.tagTable, function (oTagTableData, sTagId) {
      var oTagGroup = oReferencedTags[sTagId];

      CS.forEach(oTagTableData.rowData, function (oRowData) {
        var bFoundProperty = false;
        let bAllColumnsAreEqual = true;
        let sValueToCompareWith = "";

        CS.forEach(aKlassInstances, function (oKlassInstnce, iIndex) {
          var oContent = oKlassInstnce.klassInstance;
          var sId = oContent.id;
          let sContentName = _getContentName(oContent);
          var oTag = CS.find(oContent.tags, {tagId: sTagId});
          if(oTag) {
            bFoundProperty = true;
          }

          var sChildTagId = oRowData.id;
          var oChildTag = {relevance: 0};
          if (oTag) {
            oChildTag = CS.find(oTag.tagValues, {tagId: sChildTagId});
          }

          var bIsForRange = false;
          if (oTag) {
            bIsForRange = TagTypeConstants.RANGE_TAG_TYPE === oTagGroup.tagType && oChildTag && oChildTag.relevance !== 0;
          }

          let bIsRecommended = false;
          if (bFoundProperty && bIsGoldenRecordComparison && oPropertyRecommendations[sTagId] ) {
            let oRecommendedValue = oPropertyRecommendations[sTagId];
            if ((CS.isArray(oRecommendedValue) && oContent.id == oRecommendedValue[0]) || oContent.id == oRecommendedValue) {
              bIsRecommended = true;
            }
          }

          let bIsDisabled = false;
          if (oTag && oTag.conflictingValues.length && (oTag.conflictingValues[0].couplingType == CouplingConstants.DYNAMIC_COUPLED || oTag.conflictingValues[0].couplingType == CouplingConstants.READ_ONLY_COUPLED)) {
            bIsDisabled = true
          }

          var oVersionColumn = CS.find(oTagTableData.columnData, {id: sId});
          if (!oVersionColumn) {
            oVersionColumn = {
              id: sId,
              label: sContentName,
              isForRange: bIsForRange,
              isFixed: oContent.isGoldenRecord || null,
              forComparison: !iIndex,
              isGoldenRecord: oContent.isGoldenRecord,
              isRecommended: oContent.isGoldenRecord ? false : bIsRecommended,
              isDisabled: bIsGoldenRecordComparison ? bIsDisabled : false,
              width: iColumnWidth,
              properties: {}
            };

            if (bIsGoldenRecordComparison) {
              if (oContent.isGoldenRecord) {
                oVersionColumn.forComparison = true;
              } else {
                oVersionColumn.forComparison = false;
              }
            }

            oTagTableData.columnData.push(oVersionColumn);
          }

          var sValue = 0;
          if (oChildTag) {
            sValue = oChildTag.relevance;
          }
          oVersionColumn.properties[sChildTagId] = {value: sValue};
          if(!iIndex) {
            sValueToCompareWith = sValue;
          } else if(!CS.isEqual(sValueToCompareWith, sValue) && bAllColumnsAreEqual){
            bAllColumnsAreEqual = false;
          }
        });

        if(!bFoundProperty) {
          aTagIdsToDelete.push(sTagId);
        }

        oRowData.isSkipped = bAllColumnsAreEqual;
      });

      oTagTableData.allRowsAreEqual = _getTableVisibility(oTagTableData.rowData);

    });

    CS.forEach(aTagIdsToDelete, function (sTagId){
      delete oLayoutData.tagTable[sTagId];
    });

    CS.forEach(oLayoutData.relationshipTable, function (oRelationshipTableData, sRelationshipSideId) {
      let sRelationshipId = oRelationshipTableData.relationshipId;

      CS.remove(oRelationshipTableData.rowData, function (oRowData) {
        var sElementId = oRowData.id;
        var bFoundProperty = false;
        let bAllColumnsAreEqual = true;
        let sValueToCompareWith = "";

        CS.forEach(aKlassInstances, function (oKlassInstanceDetail, iIndex) {
          var oKlassInstance = oKlassInstanceDetail.klassInstance;
          var sId = oKlassInstance.id;

          var oVersionColumn = CS.find(oRelationshipTableData.columnData, {id: sId});
          if (!oVersionColumn) {
            oVersionColumn = {
              id: sId,
              label: _getContentName(oKlassInstance),
              forComparison: !iIndex,
              isGoldenRecord: oKlassInstance.isGoldenRecord,
              isFixed: oKlassInstance.isGoldenRecord || null,
              width: iColumnWidth,
              properties: {}
            };

            if (bIsGoldenRecordComparison) {
              if (oKlassInstance.isGoldenRecord) {
                oVersionColumn.forComparison = true;
              } else {
                oVersionColumn.forComparison = false;
              }
            }
            oRelationshipTableData.columnData.push(oVersionColumn);
          }

          var oContentRelationships = oKlassInstanceDetail.contentRelationships;
          var oNatureRelationships = oKlassInstanceDetail.natureRelationships;
          var aContentRelationships = !CS.isEmpty(oContentRelationships) ? oContentRelationships[sRelationshipSideId] : [];
          var aNatureRelationships = !CS.isEmpty(oNatureRelationships) ? oNatureRelationships[sRelationshipSideId] : [];
          var aAllRelationships = !CS.isEmpty(aNatureRelationships) ? CS.concat(aContentRelationships, aNatureRelationships) : aContentRelationships;
          var oCurrentRelationship = CS.find(aAllRelationships, {side2InstanceId: sElementId});

          var sContextId = null;
          var oReferencedRelationship = oAllReferencedRelationships[sRelationshipId];
          if (oReferencedRelationship.relationshipType) {
            //get context id from
            switch (oReferencedRelationship.relationshipType) {

              case RelationshipTypeDictionary.PRODUCT_VARIANT:
                sContextId = CS.keys(oReferencedContexts.productVariantContexts)[0];
                break;
            }
          }
          else {
            sContextId = oReferencedRelationship.side1.contextId;
          }

          var oTimeRange = oCurrentRelationship && oCurrentRelationship.context ? oCurrentRelationship.context.timeRange : {to: null, from: null};
          var aTags = oCurrentRelationship ? oCurrentRelationship.tags : [];
          if(!CS.isEmpty(oCurrentRelationship)){
            bFoundProperty = true;
            let bIsRecommended = false;
            if (bIsGoldenRecordComparison && oPropertyRecommendations[sElementId] ) {
              let oRecommendedValue = oPropertyRecommendations[sElementId];
              if ((CS.isArray(oRecommendedValue) && oCurrentRelationship.id == oRecommendedValue[1]) || oCurrentRelationship.id == oRecommendedValue) {
                bIsRecommended = true;
              }
            }

            oVersionColumn.properties[sElementId] = {
              value: sElementId,
              tags: aTags,
              timeRange: oTimeRange,
              contextId: sContextId,
              isRecommended: bIsRecommended
            };

            if(!iIndex) {
              sValueToCompareWith = sElementId;
            } else if(!CS.isEqual(sValueToCompareWith, sElementId) && bAllColumnsAreEqual){
              bAllColumnsAreEqual = false;
            }
          } else {
            oVersionColumn.properties[sElementId] = {};
          }
        });
        oRowData.isSkipped = bAllColumnsAreEqual;
        return !bFoundProperty;
      });

      oRelationshipTableData.allRowsAreEqual = _getTableVisibility(oRelationshipTableData.rowData);

    });

    CS.forEach(oLayoutData.headerInformationTable, function (oHeaderInformationTableData) {

      CS.remove(oHeaderInformationTableData.rowData, function (oRowData) {
        var sType = oRowData.type;
        var sRowId = oRowData.id;
        var bFoundProperty = false;
        let bAllColumnsAreEqual = true;
        let aValueToCompareWith = [];

        CS.forEach(aKlassInstances, function (oKlassInstanceDetail, iIndex) {

          var oKlassInstance = oKlassInstanceDetail.klassInstance;
          var sId = oKlassInstance.id;
          var oVersionColumn = CS.find(oHeaderInformationTableData.columnData, {id: sId});
          if (!oVersionColumn) {
            oVersionColumn = {
              id: sId,
              label: _getContentName(oKlassInstance),
              forComparison: !iIndex,
              isGoldenRecord: oKlassInstance.isGoldenRecord,
              isFixed: oKlassInstance.isGoldenRecord || null,
              width: iColumnWidth,
              properties: {}
            };

            if (bIsGoldenRecordComparison) {
              if (oKlassInstance.isGoldenRecord) {
                oVersionColumn.forComparison = true;
              } else {
                oVersionColumn.forComparison = false;
              }
            }

            oHeaderInformationTableData.columnData.push(oVersionColumn);
          }

          var oProperty = {};
          //fill up the property object according the type of row (taxonomy, class, name etc.) :
          switch (sType) {

            case "taxonomy":
              try {
                var aSelectedTaxonomyIds = oKlassInstance.selectedTaxonomyIds;

                oProperty.value = aSelectedTaxonomyIds;
                oProperty.visibleValues = [];
                oProperty.isDisabled = true;

                var oTaxonomyMap = {};

                if(!CS.isEmpty(aSelectedTaxonomyIds) && !bFoundProperty) {
                  bFoundProperty = true;
                }

                if(!iIndex) {
                  aValueToCompareWith = aSelectedTaxonomyIds;
                } else if(!CS.isEqual(aValueToCompareWith, aSelectedTaxonomyIds) && bAllColumnsAreEqual){
                  bAllColumnsAreEqual = false;
                }

                CS.forEach(oReferencedTaxonomies, function (oReferencedTaxonomy) {
                  _prepareReferencedTaxonomyMap(oReferencedTaxonomy, oTaxonomyMap);
                });

                CS.forEach(aSelectedTaxonomyIds, function (sTaxonomyId) {
                  var aTaxonomyIds = [];
                  _prepareSelectedTaxonomiesFromMap(sTaxonomyId, aTaxonomyIds, oTaxonomyMap);
                  if(!CS.isEmpty(aTaxonomyIds)) {
                    oProperty.visibleValues.push(CS.reverse(aTaxonomyIds));
                  }
                });
              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;

            case "type":
              try {
                oProperty.visibleValues = [];
                oProperty.isDisabled = true;
                var aSelectedKlassIds = oKlassInstance.types;
                oProperty.value = aSelectedKlassIds;
                var aKlassIds = [];

                if(!CS.isEmpty(aSelectedKlassIds) && !bFoundProperty) {
                  bFoundProperty = true;
                }

                if(!iIndex) {
                  aValueToCompareWith = aSelectedKlassIds;
                } else if(!CS.isEqual(aValueToCompareWith, aSelectedKlassIds) && bAllColumnsAreEqual){
                  bAllColumnsAreEqual = false;
                }

                CS.forEach(aSelectedKlassIds, function (sKlassId) {
                  if (sKlassId != MarkerClassTypeDictionary.GOLDEN_RECORD && sKlassId != MarkerClassTypeDictionary.MARKER) {
                    var oKlass = oReferencedKlasses[sKlassId];
                    if (oKlass) {
                      aKlassIds.push({
                        id: oKlass.id,
                        label: oKlass.label,
                        code: oKlass.code,
                        iconKey: oKlass.iconKey,
                      });
                    }
                  }


                });

                oProperty.visibleValues.push(aKlassIds);
              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;

            case "eventSchedule":
              try {
                oProperty.visibleValues = [];
                oProperty.isDisabled = true;
                oProperty.value = null;
                let oEventScheduleInfo = oKlassInstance.eventSchedule;

                if (oEventScheduleInfo) {
                  let oEventStartTime = CommonUtils.getDateAttributeInDateTimeFormat(oEventScheduleInfo.startTime);
                  let oEventEndTime = CommonUtils.getDateAttributeInDateTimeFormat(oEventScheduleInfo.endTime);
                  let sFrom = oEventStartTime.date + " " + oEventStartTime.time;
                  let sTo = oEventEndTime.date + " " + oEventEndTime.time;
                  let sRepeat = "";

                  if (oEventScheduleInfo.repeat && !CS.isEmpty(oEventScheduleInfo.repeat.repeatType)) {
                    sRepeat = getTranslation()[oEventScheduleInfo.repeat.repeatType];

                    if (oEventScheduleInfo.repeat.repeatType !== "WEEKLY") {
                      sRepeat = sRepeat + " " + oEventScheduleInfo.repeat.repeatEvery + " " + CS.toLower(getTranslation().TIMES);
                    }

                    if (!CS.isEmpty(oEventScheduleInfo.repeat.daysOfWeek) && oEventScheduleInfo.repeat.repeatType !== "DAILY" && oEventScheduleInfo.repeat.repeatType !== "MONTHLY") {
                      let aRepeatDaysOfWeek = [];
                      CS.forEach(oEventScheduleInfo.repeat.daysOfWeek, function (sNewDay, sNewDayIndex) {
                        if (sNewDay === true) {
                          let sNewDay;
                          switch (sNewDayIndex) {
                            case "MON":
                              sNewDay = getTranslation().MONDAY_SHORT;
                              break;
                            case "TUE":
                              sNewDay = getTranslation().TUESDAY_SHORT;
                              break;
                            case "WED":
                              sNewDay = getTranslation().WEDNESDAY_SHORT;
                              break;
                            case "THU":
                              sNewDay = getTranslation().THURSDAY_SHORT;
                              break;
                            case "FRI":
                              sNewDay = getTranslation().FRIDAY_SHORT;
                              break;
                            case "SAT":
                              sNewDay = getTranslation().SATURDAY_SHORT;
                              break;
                            case "SUN":
                              sNewDay = getTranslation().SUNDAY_SHORT;
                              break;
                          }
                          aRepeatDaysOfWeek.push(sNewDay);
                        }
                      });
                      sRepeat = sRepeat + " " + CS.toLower(getTranslation().ON) + " " + aRepeatDaysOfWeek.join();
                    }
                  }
                  oProperty.value = {
                    [getTranslation().FROM]: sFrom,
                    [getTranslation().TO]: sTo,
                    [getTranslation().REPEAT]: sRepeat
                  };
                }

                if (!CS.isEmpty(oEventScheduleInfo) && !bFoundProperty) {
                  bFoundProperty = true;
                }

                if (!iIndex) {
                  aValueToCompareWith = oEventScheduleInfo;
                } else if (!CS.isEqual(aValueToCompareWith, oEventScheduleInfo) && bAllColumnsAreEqual) {
                  bAllColumnsAreEqual = false;
                }

              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;
          }

          oVersionColumn.properties[sRowId] = oProperty;
        });
        oRowData.isSkipped = bAllColumnsAreEqual;
        return !bFoundProperty;
      });

      oHeaderInformationTableData.allRowsAreEqual = _getTableVisibility(oHeaderInformationTableData.rowData);

    });

    CS.forEach(oLayoutData.fixedHeaderTable, function (oFixedHeaderData) {

      CS.remove(oFixedHeaderData.rowData, function (oRowData) {
        var sType = oRowData.type;
        var sRowId = oRowData.id;
        var bFoundProperty = false;

        CS.forEach(aKlassInstances, function (oKlassInstanceDetail, iIndex) {

          var oKlassInstance = oKlassInstanceDetail.klassInstance;
          let bIsEntityAvailableInDL = _isContentAvailableInSelectedDataLanguage(oKlassInstance);
          var sId = oKlassInstance.id;

          let sIconType = "";
          if (CS.includes(oKlassInstance.types, "golden_article_klass")) {
            sIconType += "goldenRecordIndicator ";
          }

          if (oKlassInstance.variantOfLabel) {
            sIconType += "variantOfContainer "
          }

          if (oKlassInstance.branchOfLabel) {
            sIconType += "cloneOfContainer "
          }

          var oVersionColumn = CS.find(oFixedHeaderData.columnData, {id: sId});
          if (!oVersionColumn) {
            oVersionColumn = {
              id: sId,
              label: _getContentName(oKlassInstance),
              forComparison: !iIndex,
              isGoldenRecord: oKlassInstance.isGoldenRecord,
              createdBy: oKlassInstance.createdBy,
              createdOn: oKlassInstance.createdOn,
              lastModifiedBy: oKlassInstance.lastModifiedBy,
              lastModified: oKlassInstance.lastModified,
              isFixed: oKlassInstance.isGoldenRecord || null,
              width: iColumnWidth,
              isEntityAvailableInDL: bIsEntityAvailableInDL,
              properties: {},
              type: sIconType,
            };

            if (bIsGoldenRecordComparison) {
              oVersionColumn.isEntityAvailableInDL = true;
              if (oKlassInstance.isGoldenRecord) {
                oVersionColumn.forComparison = true;
              } else {
                oVersionColumn.forComparison = false;
              }
            }

            oFixedHeaderData.columnData.push(oVersionColumn);
          }

          var oProperty = {};
          switch (sType) {
            case "image":
              try {
                var sBaseType = oKlassInstance.baseType;
                var sDefaultAssetInstanceId = oKlassInstance.defaultAssetInstanceId;
                bFoundProperty = true;
                var oReferencedAssetsData = {};
                var oInstance = oKlassInstance;
                if(sBaseType !== BaseTypesDictionary.assetBaseType) {
                  if(sDefaultAssetInstanceId) {
                    oProperty.value = sDefaultAssetInstanceId;
                    var aMasterKlassInstances = ContentScreenProps.screen.getMasterKlassInstanceListForComparison();
                    var oMasterKlassInstance = CS.find(aMasterKlassInstances, function (oKlassInstanceDetail) {
                      var oKlassInstance = oKlassInstanceDetail.klassInstance;
                      return oKlassInstance.id === sDefaultAssetInstanceId;
                    });
                    oInstance = oMasterKlassInstance.klassInstance;
                  }
                }
                var oCoverflowAttribute = oInstance.assetInformation;
                if(sBaseType !== BaseTypesDictionary.assetBaseType && sDefaultAssetInstanceId) {
                  oReferencedAssetsData[sDefaultAssetInstanceId] = ContentUtils.getAssetDataObject(oCoverflowAttribute);
                } else {
                  if(oCoverflowAttribute) {
                    oProperty.value = oInstance.id;
                    oReferencedAssetsData[oInstance.id] = ContentUtils.getAssetDataObject(oCoverflowAttribute);
                  } else {
                    oProperty.value = oInstance.id;
                    let oKlass = oReferencedKlasses[_getEntityClassType(oKlassInstance)];
                    let sImageSrc = oKlass.previewImage ? CommonUtils.getIconUrl(oKlass.previewImage) : "";
                    oReferencedAssetsData[oInstance.id] = {
                      thumbKeySrc: sImageSrc
                    }
                  }
                }
                oProperty.referencedAssetsData = oReferencedAssetsData;
                oProperty.name = oKlassInstance.name;
              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;
          }

          oVersionColumn.properties[sRowId] = oProperty;
        });
        return !bFoundProperty;
      });
    });

  };

  var _validateVariantContextSelection = function (oActiveEntity, oActiveVariantContext) {
    let bIsValidContext = true;
    let aMessage = [];

    // if time enabled,Check Variant time Validity - Neha
    if ((oActiveVariantContext.isTimeEnabled) || (oActiveVariantContext.context && oActiveVariantContext.context.isTimeEnabled)) {
      let oTimeRange = oActiveEntity.timeRange || oActiveVariantContext.timeRange;
      if (oTimeRange && oTimeRange.from === null && oTimeRange.to === null) {
        aMessage.push(getTranslation().DATE);
        bIsValidContext = false;
      } else if (!oTimeRange || !oTimeRange.from || !oTimeRange.to) {
        alertify.message(getTranslation().SELECT_TIME_PERIOD);
        return false;
      } else if (oTimeRange && oTimeRange.from && oTimeRange.to && (oTimeRange.from > oTimeRange.to)) {
        alertify.message(getTranslation().FROM_DATE_MUST_BE_LESS_THAN_TO_DATE);
        return false;
      }
    }

    //Context tags selection validation - Neha
    if (!CS.isEmpty(oActiveVariantContext.tags) && ContentUtils.isAllRelevanceZero(oActiveEntity.tags)) {
      aMessage.push(getTranslation().TAG);
      bIsValidContext = false;
    }

    //if entities present in variant,then entities must be selected - Neha
    if (!CS.isEmpty(oActiveVariantContext.entities)) {
      // Handling empty check for Context Varient QuickList
      let bIsInstanceEmpty = true;
      CS.forEach(oActiveEntity.linkedInstances, function (aInstance) {
        if (aInstance.length > 0)
          bIsInstanceEmpty = false;
        return true;
      });

      if(!oActiveEntity.hasOwnProperty('linkedInstances') || CS.isEmpty(oActiveEntity.linkedInstances) || bIsInstanceEmpty){
         aMessage.push(getTranslation().ENTITY);
         bIsValidContext = false;
      }
    }

    if (!bIsValidContext) {
      let sContextToDisplay = aMessage.join(", ");
      let sMessage = _getDecodedTranslation(
          getTranslation().PLEASE_SELECT_CONTEXT_TO_PROCEED,
          {context: sContextToDisplay.toLowerCase()});
      alertify.message(sMessage);
      return false;
    }
    return true;

  };

  let _getURLForTaxonomiesHierarchyByRelationship = function (sRelationshipId) {
      return _getURLForTaxonomiesHierarchyByRelationshipType(_getRelationshipTypeById(sRelationshipId));
  };

  let _getURLForTaxonomiesHierarchyByRelationshipType = function (sRelationshipType) {
    let sKeyForURL = "";
    switch (sRelationshipType) {
      default:
        sKeyForURL = "GetTaxonomyHierarchyForRelationship";
        break;
    }
    return sKeyForURL;
  };


  let _getURLForTaxonomiesForRelationship = function (sClickedTaxonomyId, sRelationshipId) {
    let sKeyForURL = _getURLForTaxonomiesHierarchyByRelationship(sRelationshipId);
    return getRequestMapping(_getScreenModeBasedOnActiveEntity())[sKeyForURL];
  };

  let _isTaxonomyHierarchySectionDirty = function () {
    let sSelectedHierarchyMode = _getSelectedHierarchyContext();

    if(sSelectedHierarchyMode) {
      let oFilterContext = {
        filterType: oFilterPropType.HIERARCHY,
        screenContext: sSelectedHierarchyMode,
      };
      let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
      let oTaxonomySections = oFilterProps.getTaxonomyNodeSections();
      return oTaxonomySections && oTaxonomySections.clonedObject;
    } else {
      return false;
    }

  };

  var _isActiveContentDirty = function () {
    let oActiveEntity = _getActiveEntity();
    let VariantStore = _getVariantStore();
    let UOMProps = ContentScreenProps.uomProps;
    let sTableContextId = UOMProps.getOpenedDialogTableContextId();
    let oVariantContextByContextProps = CS.isEmpty(sTableContextId) ? null : VariantStore.getVariantContextPropsByContext(sTableContextId);
    let oTaskProps = ContentScreenProps.taskProps;
    let bIsTaskDirty = oTaskProps.getIsTaskDirty();
    if (oActiveEntity && oActiveEntity.isDirty) {
      return true;
    } else if (_getIsTableContentDirty()) {
      return true;
    } else if (!CS.isEmpty(oVariantContextByContextProps) && oVariantContextByContextProps.getIsVariantDialogOpen()) {
      return true;
    } else if (_isTaxonomyHierarchySectionDirty()) {
      return true;
    }
    else if (bIsTaskDirty) {
      return true;
    }
    return false;
  };

  var _getSelectedScreenContextForQuickList = function () {
    let sSelectedContext = "";
    let oAddEntityInRelationship = ContentScreenProps.screen.getAddEntityInRelationshipScreenData();
    let sActiveEntityId = ContentUtils.getActiveEntity().id;
    if (ContentUtils.getIsStaticCollectionScreen()) {
      if (!CS.isEmpty(oAddEntityInRelationship) && oAddEntityInRelationship[sActiveEntityId] && oAddEntityInRelationship[sActiveEntityId].status) {
        sSelectedContext = oAddEntityInRelationship[sActiveEntityId].context === "productVariant" ? "productVariantQuickList" : "";
      } else {
        sSelectedContext = "staticCollection";
      }
    } else if (ContentScreenProps.variantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus()) {
      sSelectedContext = "contextEntity";
    }
    return sSelectedContext;
  };

  /**
   * Warning: This is work around is made just for self relationship.
   * _updateSectionIdListBySequenceForSelfRelationship - Shashank
   * */
  let _updateSectionIdListBySequenceForSelfRelationship = (aSectionIdsBySequence, oReferencedRelationshipsFromCaller) =>{
    let oActiveEntity = _getActiveEntity();
    let aNewSections = [];

    let aContentRelationship = oActiveEntity.contentRelationships;
    let aNatureRelationship = oActiveEntity.natureRelationships;

    let oReferencedNatureRelationships = ContentScreenProps.screen.getReferencedNatureRelationships();
    let oReferencedRelationships = ContentScreenProps.screen.getReferencedRelationships();


    CS.forEach(aSectionIdsBySequence, function (sSectionId) {

      let oDesiredRelFromCaller = !CS.isEmpty(oReferencedRelationshipsFromCaller) ? oReferencedRelationshipsFromCaller[sSectionId] :{};
      let bIsNatureRelationshipFromCaller = !CS.isEmpty(oDesiredRelFromCaller) ? _isNatureRelationship(oDesiredRelFromCaller.relationshipType) : false;

      if ((!CS.isEmpty(oDesiredRelFromCaller) && !bIsNatureRelationshipFromCaller) || oReferencedRelationships[sSectionId]) {
        let aContentRelationshipData = CS.filter(aContentRelationship, {relationshipId: sSectionId});
        CS.forEach(aContentRelationshipData, function (oContentRelationshipData) {
          aNewSections.push({
            sideId: oContentRelationshipData.sideId,
            relationshipId: sSectionId
          });
        });
      } else if ((!CS.isEmpty(oDesiredRelFromCaller) && bIsNatureRelationshipFromCaller) || oReferencedNatureRelationships[sSectionId]) {
        let aNatureRelationshipData = CS.filter(aNatureRelationship, {relationshipId: sSectionId});
        CS.forEach(aNatureRelationshipData, function (oNatureRelationshipData) {
          aNewSections.push({
            sideId: oNatureRelationshipData.sideId,
            relationshipId: sSectionId
          });
        });
      } else {
        aNewSections.push(sSectionId);
      }
    });
    return aNewSections;
  };


  /**
   * Parameter: sideId
   * return: relationshipId
   * Description: If you re not sure about what Id you have and you need both relationshipId, then use me.
   * */
  let _getRelationshipIdFromSideId = function (sRelationshipSideId) {
    var oScreenProps = ContentScreenProps.screen;
    let sRelationshipId = "";

    let oReferencedElements = oScreenProps.getReferencedElements();
    let oReferencedRelationships = oScreenProps.getReferencedRelationships();
    let oReferencedNatureRelationships = oScreenProps.getReferencedNatureRelationships();

    if (oReferencedElements[sRelationshipSideId]) {
      let oReferencedElement = oReferencedElements[sRelationshipSideId];
      sRelationshipId = oReferencedElement.propertyId;
    }
    /**Else part in here is added for extra safety check.
     * i.e. if received parameter does not exists in oReferencedElements then check if the passed ID is in
     * oReferencedRelationships or  in oReferencedNatureRelationships so that required data will be sent back*/
    else if (oReferencedRelationships[sRelationshipSideId] || oReferencedNatureRelationships[sRelationshipSideId]) {
      sRelationshipId = sRelationshipSideId;
    }

    return sRelationshipId;
  };

  let _getContentName = function (oContent) {
    return CommonUtils.getContentName(oContent);
  };

  let _getContentNatureTypeId = function (oKlassInstance, oReferencedKlasses) {
    oReferencedKlasses = oReferencedKlasses || ContentScreenProps.screen.getReferencedClasses();
    return CS.find(oKlassInstance.types, function (sType) {
      let oReferencedKlass = oReferencedKlasses[sType] || {};
      if (oReferencedKlass.isNature) {
        return sType
      }
    });
  };

  let _getNatureTypeFromContent = function (oActiveContent) {
    let oScreenProps = ContentScreenProps.screen;
    let oReferencedKlasses = oScreenProps.getReferencedClasses();
    oActiveContent = oActiveContent || _getActiveContent();
    let sNatureKlassId = _getContentNatureTypeId(oActiveContent);
    let oReferencedKlass = oReferencedKlasses[sNatureKlassId] || {};
    return oReferencedKlass.natureType || "";
  };

  let _getCurrentLocaleNumberValue = function (sValue, iPrecision, oNumberFormat, bDisableFormatByLocale) {
    return NumberUtils.getValueToShowAccordingToNumberFormat(sValue, iPrecision, oNumberFormat, bDisableFormatByLocale);
  }

  /**
   * @Router Impl
   *
   * @param sSelectedItemId
   * @description Maintaing history object for entity creation
   * @private
   */
  let _makeWindowHistoryStateStableForEntityCreation = function (fCallback) {
    SharableURLStore.setIsPushHistoryState(true);
    let oHistoryState = CS.getHistoryState();

    /** Required when state is null **/
    if (!oHistoryState) {
      SharableURLStore.setIsEntityNavigation(false);
      CS.navigateForward(fCallback)
    } else {
      fCallback();
    }
  };

  var _setNatureAndVariantRelationshipIdsIntoProps = function (oConfigReferencedNatureRelationship) {
    var oScreenProps = ContentScreenProps.screen;
    var oReferencedNatureRelationships = oConfigReferencedNatureRelationship || oScreenProps.getReferencedNatureRelationships();
    oScreenProps.setNatureRelationshipIds([]);

    var aNatureRelationshipIds = [];
    CS.forEach(oReferencedNatureRelationships, function (oRelationship) {
      //considering that there will be only 2 relationships at max -
      if (_isNatureRelationship(oRelationship.relationshipType)) {
        aNatureRelationshipIds.push(oRelationship.id);
      }
    });
    oScreenProps.setNatureRelationshipIds(aNatureRelationshipIds);
  };

  let _getSelectedDataLanguage = function () {
    return SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE)
  };

  /**
   * @function _getNatureRelationshipIdLabelByRelationshipType
   * @description Returns Section Id and Label For Nature Relationship/Reference
   * @param sRelationshipType
   * @returns {{id: *, label: (*|string)}}
   * @private
   */
  let _getNatureRelationshipIdLabelByRelationshipType = function (sRelationshipType) {
    var sId = sRelationshipType;
    var sName = "";


    switch (sRelationshipType) {
      case RelationshipTypeDictionary.PRODUCT_NATURE:
        sName = getTranslation().PRODUCTS;
        break;
      default:
        sId = "bundleSection";
        sName = "Bundle Section";
        break;
    }

    return {id: sId, label: sName}
  };

  let _getTemplateAndTypeIdForServer = (sId) => {
    let oActiveEntity = _getActiveEntity();
    let response = {
      typeId: null,
      templateId: null
    }
    if ((CS.includes(oActiveEntity.types, sId)) || (CS.includes(oActiveEntity.taxonomyIds, sId))) {
      response.typeId = sId;
    } else {
      response.templateId = _getTemplateIdForServer(sId);
    }
    return response;
  };

  let _isHideCreateButton = function () {
    let oComponentProps = ContentUtils.getComponentProps();
    let oActiveCollection = oComponentProps.collectionViewProps.getActiveCollection();
    let bIsHideCreateButton = ContentUtils.getIsArchive()
        || SessionProps.getSessionEndpointType() === "offboardingendpoint"
        || ContentUtils.getSelectedModuleId() === ModuleDictionary.SUPPLIER
        || CS.isNotEmpty(oActiveCollection)
        || CS.isEmpty(ContentUtils.getAppData().getDefaultTypes())
        || ContentUtils.getIsCurrentUserReadOnly();

    return bIsHideCreateButton;
  };

  let _setIsKlassInstanceFlatPropertyUpdated = (bPropertyStatus) => {
    ContentScreenProps.screen.setIsKlassInstanceFlatPropertyStatus(bPropertyStatus);
  }

  let _getIsKlassInstanceFlatPropertyUpdated = () => {
    return ContentScreenProps.screen.getIsKlassInstanceFlatPropertyStatus();
  }

  let _getBlobFromUrl = (dataURI, sFileName) => {
    let byteString = atob(dataURI.split(',')[1]);
    let mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    let ab = new ArrayBuffer(byteString.length);
    let ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    let blob = new Blob([ab], {type: mimeString});
    blob.lastModifiedDate = new Date();
    blob.name = sFileName;
    return blob;
  };

  let _getSelectedModulesForFilter = function () {
    let aModuleList = ContentUtils.getAllMenus();
    let sSelectedModuleId = '';
    let oComponentProps = ContentUtils.getComponentProps();
    let oActiveCollection = CollectionViewProps.getActiveCollection();

    let oModule = CS.find(aModuleList, {isSelected: true});
    if(oModule) {
      sSelectedModuleId = oModule.id;
    }
    if (!CS.isEmpty(oActiveCollection) && oActiveCollection.type === "staticCollection") {
      return "allmodule";
    } else if (oComponentProps.damInformationTabProps.getIsRuleViolatedContentsScreen()) {
      return ModuleDictionary.MAM;
    }
    else if (!CS.isEmpty(oActiveCollection) && oActiveCollection.type === "dynamicCollection") {
      return ContentUtils.getBookmarkModuleId();
    }
    return sSelectedModuleId;
  }

  let _getKlassInstanceId = function () {
    let oActiveContent = ContentUtils.getActiveContent();
    let oOpenedDialogAttributeData = UOMProps.getOpenedDialogAttributeData();
    return oOpenedDialogAttributeData.klassInstanceId || oActiveContent.id;
  };

  let _getDownloadDialogData = function (bIsAllFieldsActive = false, bIsHideDownloadAsExtension = false) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oAssetDownloadProps = oComponentProps.bulkDownloadAssetProps;
    let oDownloadDialogData = oAssetDownloadProps.getActiveDownloadDialogData();
    let aDownloadDialogData = oDownloadDialogData.isDirty ? oDownloadDialogData.clonedObject : oDownloadDialogData.downloadModel;
    let aInvalidRowIds = oAssetDownloadProps.getInvalidRowIds();
    let bIsToggleButtonOn = oAssetDownloadProps.getIsDownloadDialogToggleButtonOn();
    let bIsToggleButtonDisabled = oAssetDownloadProps.getIsToggleButtonDisabled();
    let bShouldDownloadAssetWithOriginalFileName = oAssetDownloadProps.getShouldDownloadAssetWithOriginalFilename();
    let sToggleButtonLabel = bShouldDownloadAssetWithOriginalFileName ? getTranslation().USE_ASSET_NAME : getTranslation().USE_ORIGINAL_FILENAME;
    let oDownloadAsExtraData = oAssetDownloadProps.getDownloadAsExtraData();
    let iSelectedAssetsCount = oAssetDownloadProps.getTotalSelectedAssetsCount();

    let oToggleButtonData = {
      isToggleButtonOn: bIsToggleButtonOn,
      toggleButtonLabel: sToggleButtonLabel,
      disabled: bIsToggleButtonDisabled,
    };

    let oFixedSectionData = {
      downloadFileName: oAssetDownloadProps.getDownloadFileName(),
      downloadComment: oAssetDownloadProps.getDownloadComments(),
      totalCount: iSelectedAssetsCount,
      totalSize: oAssetDownloadProps.getTotalSelectedAssetsSize(),
      isDownloadAsDisabled: bIsAllFieldsActive || iSelectedAssetsCount < 2,
      isFolderByAssetDisabled: iSelectedAssetsCount < 2,
      isCommentSectionDisabled: bIsAllFieldsActive && iSelectedAssetsCount < 1,
      isHideDownloadAsExtension: bIsHideDownloadAsExtension || iSelectedAssetsCount < 2,
    };

    let oButtonExtraData = {
      isDownloadButtonDisabled: iSelectedAssetsCount < 1 || CS.isNotEmpty(aInvalidRowIds),
    };

    return {
      isFolderByAsset: oAssetDownloadProps.getIsFolderByAsset(),
      toggleButtonData: oToggleButtonData,
      fixedSectionData: oFixedSectionData,
      downloadAsExtraData: oDownloadAsExtraData,
      buttonExtraData: oButtonExtraData,
      showAssetDownloadDialog: oAssetDownloadProps.getShowDownloadDialog(),
      downloadData: aDownloadDialogData,
      invalidRowIds: aInvalidRowIds,
    };
  };

  let _isQuickListNode = function (oBreadcrumbNode) {
      let aQuickListTypes = [
          BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST,
          BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST,
          BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST
      ];

      return CS.includes(aQuickListTypes, oBreadcrumbNode.type);
  };

  /************************************* Public API's **********************************************/
  return {
    triggerChange: function () {
      _triggerChange();
    },

    getSelectedScreenContextForQuickList: function () {
      return _getSelectedScreenContextForQuickList();
    },

    isActiveContentDirty: function () {
      return _isActiveContentDirty();
    },

    convertTimeStampToEOD : function (sValue) {
      return _convertTimeStampToEOD(sValue);
    },

    convertTimeStampToSOD : function (sValue) {
      return _convertTimeStampToSOD(sValue);
    },

    getPaginationLimitAccordingToZoomLevel: function (iZoomLevel) {
      return _getPaginationLimitAccordingToZoomLevel(iZoomLevel);
    },

    getAppData: function () {
      return AppData;
    },

    getComponentProps: function () {
      return ContentScreenProps;
    },

    showError: function (sMessage) {
      CommonUtils.showError(sMessage);
    },

    showMessage: function (sMessage) {
      CommonUtils.showMessage(sMessage);
    },

    showSuccess: function (sMessage) {
      CommonUtils.showSuccess(sMessage);
    },

    confirmation: function (sMessage, sHeader, oOkCallback, oCancelCallback) {
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

      CustomActionDialogStore.showConfirmDialog(sMessage, sHeader, oCallbackOk, oCallbackCancel);
    },


    listModeConfirmation: function (sHeader, aArticleNames, oOkCallback, oCancelCallback, sOkText, sCancelText) {
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
    },


    getActiveContent: function () {
      return _getActiveContent();
    },

    getActiveContentClass: function () {
      return _getActiveContentClass();
    },

    getCurrentContent: function () {
      return _getCurrentContent();
    },

    getCurrentEntity: function () {
      return _getCurrentEntity();
    },

    getSectionFromActiveEntityClassByEntityId: function (sElementId, sElementKey) {
      return _getSectionFromActiveEntityClassByEntityId(sElementId, sElementKey);
    },

    setActiveContent: function (oContent) {
      _setActiveContent(oContent);
    },

    setActiveContentClass: function (oContent) {
      _setActiveContentClass(oContent);
    },

    getSelectedContentList: function () {
      return _getSelectedContentList();
    },

    setSelectedContentIds: function (aIds) {
      _setSelectedContentIds(aIds);
    },

    getSelectedContentIds: function () {
      return _getSelectedContentIds();
    },

    getContentById: function (sContentId) {
      return _getContentById(sContentId);
    },

    setTagList: function () { //unused
      trackMe('setTagList');
      var aTags = GlobalStore.getTags();
      AppData.setTagList(aTags);
      logger.debug('setTagList: tags', {'tags': aTags});
    },

    setPaginatedIndex: function (iIndex) {
      _setPaginatedIndex(iIndex);
    },

    setContentScreenMode: function (sScreenMode) {
      _setContentScreenMode(sScreenMode);
    },

    getContentScreenMode: function () {
      return _getContentScreenMode();
    },

    getActiveEntity: function(){
      return _getActiveEntity();
    },

    makeActiveContentDirty: function () {
      var oActiveContent = _getActiveContent();
      if (!CS.isEmpty(oActiveContent)) {
        return _makeContentDirty(oActiveContent);
      }

      return null;
    },

    makeCurrentEntityDirty: function () {
      var oActiveEntity = _getActiveEntity();
      if (!CS.isEmpty(oActiveEntity)) {
        return _makeContentDirty(oActiveEntity);
      }

      return null;
    },

    makeContentClean: function (oContent) {
      _makeContentClean(oContent);
    },

    makeContentDirty: function (oContent) {
      trackMe('makeContentDirty');
      return _makeContentDirty(oContent);
    },

    changeItemSelectionMode: function (sSelectionMode) {
      ContentScreenProps.screen.setItemSelectionMode(sSelectionMode);

      switch (sSelectionMode.toUpperCase()) {
        case "CONTENT" :
          _emptySelectedImageList();
          _emptySelectedAttributeList();
          break;

        case "IMAGE":
          _emptySelectedAttributeList();
          _emptySelectedContentList();
          break;

        case "ATTRIBUTE":
          _emptySelectedContentList();
          _emptySelectedImageList();
      }
    },

    removeAllSelectionInContentScreen: function () {
      _emptySelectedContentList();
      _emptySelectedImageList();
      _emptySelectedAttributeList();

      _removeSelectionsAcrossScreenMode();
    },

    fillTagValuesListWithObjectTags: function (aTags) {
      trackMe('fillTagValuesListWithObjectTags');
      GlobalStore.setDefaultTagValueInTagValueList();
      var oTagValuesList = GlobalStore.getTagValuesList();

      CS.forEach(aTags, function (oTag) {
        oTagValuesList[oTag.id] = CS.cloneDeep(oTag);
      });

      logger.info('fillTagValuesListWithObjectTags: Filing tag values',
          {'tagValues': oTagValuesList});
    },

    getNodeFromTreeListById: function (aNodeList, sNodeId) {
      return CommonUtils.getNodeFromTreeListById(aNodeList, sNodeId);
    },

    getAllChildrenNodes: function (aNodeList) {
      return _getAllChildrenNodes(aNodeList);
    },

    getDecodedHtmlContent: function (sHtml) {
      return _getDecodedHtmlContent(sHtml);
    },

    hideLoadingScreen: function(){
      var loaderContainer = document.getElementById('loaderContainer');
      loaderContainer.classList.add('loaderInVisible');
    },

    getScreenProps: function () {
      return _getScreenProps();
    },

    isImageCoverflowAttribute: function (sAttributeType) {
      return AttributeUtils.isImageCoverflowAttribute(sAttributeType);
    },

    getLeafNodeSubbedWithParentInfo: function (aTreeNode, sParentKey, sChildKey) {
      return CommonUtils.getLeafNodeWithSubbedWithParentInfo(aTreeNode, sParentKey, sChildKey || 'children');
    },

    getMaxValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return CommonUtils.getMaxValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getMinValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return CommonUtils.getMinValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getTagGroupModels: function (oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags){
      return CommonUtils.getTagGroupModels(oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags);
    },

    getMasterTagById: function (sTagId) {
      return _getMasterTagById(sTagId);
    },

    addTagInEntity: function (oEntity, oElement, oMasterTag) {
      return _addTagInEntity(oEntity, oElement, oMasterTag);
    },

    addAllMasterTagInEntity: function (oEntity, oMasterTag, bWithoutSuffix) {
      _addAllMasterTagInEntity(oEntity, oMasterTag, bWithoutSuffix);
    },

    addAllMasterAttributeInEntity: function (oEntity, oMasterAttribute, bWithoutSuffix) {
      _addAllMasterAttributeInEntity(oEntity, oMasterAttribute, bWithoutSuffix);
    },
    /**
     * @deprecated
     * @returns {*}
     */
    isVariantDialogOpened: function () {
      return _isVariantDialogOpened();
    },

    getEditableVariant: function () {
      return _getEditableVariant();
    },

    addRelationshipInEntity: function (oElement, oEntity) {
      return _addRelationshipInEntity(oElement, oEntity);
    },

    getNewTagValue: function (sTagId, iRelevance, bWithoutSuffix) {
      return _getNewTagValue(sTagId, iRelevance, bWithoutSuffix);
    },

    getIsAttributeAndIsNumeric: function (sFieldName) {
      var aCustomAttribute = _getAttributeList();
      var bIsAttribute = false;
      var bIsNumeric = false;
      var oAttribute = CS.find(aCustomAttribute, {id: sFieldName});
      if(oAttribute) {
        bIsAttribute = true;
        bIsNumeric = this.isAttributeTypeNumeric(oAttribute.type);
      }
      return {isAttribute: bIsAttribute, isNumeric: bIsNumeric};
    },

    getNewSuffix: function () {
      return _getNewSuffix();
    },

    setSelectedContextProps: function (sContext, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, sFillerID) {
      return _setSelectedContextProps(sContext, sElementId, sStructureId, sSectionId, sMasterEntityId, sAttributeType, sFillerID);
    },

    resetToUpdateAllSCU: function () {
      _resetToUpdateAllSCU();
    },

    stopSCUUpdate: function () {
      _stopSCUUpdate();
    },

    getUserById: function (sUserId) {
      return _getUserById(sUserId);
    },

    getUserByUserName: function (sUserName) {
      return _getUserByUserName(sUserName);
    },

    formatDate: function (sDate) {
      return _formatDate(sDate);
    },

    getIconUrl: function (sKey) {
      return _getIconUrl(sKey);
    },

    removeNodeById: function (aNodeList, sNodeId) {
      return _removeNodeById(aNodeList, sNodeId);
    },

    removeChildNodesFromValueList: function (aNodes, oValueList) {
      _removeChildNodesFromValueList(aNodes, oValueList);
    },

    getTreeRootNodeId: function () {
      return TREE_ROOT_ID;
    },

    getParentNodeByChildId: function (aNodeList, sChildId, oParentNode) {
      return _getParentNodeByChildId(aNodeList, sChildId, oParentNode);
    },

    generateADMForAttribute: function (aOldAttribute, aClonedAttribute, bShouldCheckParentVariance) {
      return _generateADMForAttribute(aOldAttribute, aClonedAttribute, bShouldCheckParentVariance);
    },

    generateADMForAssets: function (aOldAssets, aClonedAssets) {
      return _generateADMForAssets(aOldAssets, aClonedAssets);
    },

    generateADMForTags: function (aOldTags, aClonedTags, bShouldCheckParentVariance) {
      return _generateADMForTags(aOldTags, aClonedTags, bShouldCheckParentVariance);
    },

    generateADMForRelationships: function (aOldRelationships, aNewRelationships, bIsNatureRelationship) {
      return _generateADMForRelationships(aOldRelationships, aNewRelationships, bIsNatureRelationship);
    },

    createDummyArticle: function (sParentId, sType) {
      return _createDummyArticle(sParentId, sType);
    },

    addAttributeDataInEntityFromSectionElement: function (oEntity, oElement, oClass) {
      return _addAttributeDataInEntityFromSectionElement(oEntity, oElement, oClass);
    },

    addRoleDataInEntityFromSectionElement: function (oEntity, oElement, oClass) {
      _addRoleDataInEntityFromSectionElement(oEntity, oElement, oClass);
    },

    updateBreadCrumbInfo: function (sId, oEntity) {
      _updateBreadCrumbInfo(sId, oEntity);
    },


    setVariantVersionProps: function(oEntity){
      _setVariantVersionProps(oEntity)
    },

    getDirtyActiveEntity: function () {
      return _getDirtyActiveEntity();
    },

    makeActiveEntityClean: function () {
      _makeActiveEntityClean();
    },

    makeActiveEntityDirty: function () {
      return _makeActiveEntityDirty();
    },

    removeZeroValuesFromFilterTags: function(aTags, bChangeInOriginalObject){
      return _removeZeroValuesFromFilterTags(aTags, bChangeInOriginalObject);
    },

    getEntityList: function () {
      return _getEntityList();
    },

    getSelectedEntityList: function () {
      return _getSelectedEntityList();
    },

    isSameMode: function (sViewType) {
      return _isSameMode(sViewType);
    },

    addElementToVariantVersionMap: function(oElement, bForceVariantActive){
      _addElementToVariantVersionMap(oElement, bForceVariantActive);
    },

    resetContentScreenState: function(){
      _resetContentScreenState();
    },

    setRelationshipElements: function (oEntity) {
      _setRelationshipElements(oEntity);
    },

    getContentStore: function () {
      return _getContentStore();
    },

    getAvailableEntityStore: function () {
      return _getAvailableEntityStore();
    },

    getFilterStore: function (oFilterContext) {
      return _getFilterStore(oFilterContext);
    },

    getMinAllowedZoom: function(){
        return ZoomToolbarSettings.minZoom;
    },

    getDefaultZoom: function () {
      return ZoomToolbarSettings.defaultZoom;
    },

    getMaxAllowedZoom: function(){
      return ZoomToolbarSettings.maxZoom;
    },

    makeAssetInstancesForEntity: function (oEntity, oReferencedAssets) {
      _makeAssetInstancesForEntity(oEntity, oReferencedAssets);
    },

    getSplitter: function(){
      return _getSplitter();
    },

    removeAddedRelationshipElementsOnFailure: function (aEntitiesToSave) {
      _removeAddedRelationshipElementsOnFailure(aEntitiesToSave);
    },

    removeAddedNatureRelationshipElementsOnFailure: function (aEntitiesToSave){
      _removeAddedNatureRelationshipElementsOnFailure(aEntitiesToSave);
    },

    getThumbnailTypeFromBaseType: function (sBaseType) {
      var sThumbnailType = '';
      if (sBaseType == BaseTypesDictionary["contentBaseType"] ||
          sBaseType == BaseTypesDictionary["assetBaseType"] ||
          sBaseType == BaseTypesDictionary["marketBaseType"]
      ) {
        sThumbnailType = "article";
      }
      else if (sBaseType == BaseTypesDictionary["collectionKlassInstanceEntityBaseType"]) {
        sThumbnailType = "collection";
      }
      else if(sBaseType == BaseTypesDictionary["textAssetBaseType"]){
        sThumbnailType = "textasset";
      }
      else if(sBaseType == BaseTypesDictionary["supplierBaseType"]){
        sThumbnailType = "supplier";
      }

      return sThumbnailType;
    },

    createFilterPostData: function (oFilterParameters, oFilterContext) {
      return _createFilterPostData(oFilterParameters, oFilterContext);
    },

    getAllInstancesRequestData: function (oFilterContext) {
      return _getAllInstancesRequestData(oFilterContext);
    },

    getAdditionalDataForRelationshipCalls: function (oPostDataForFilter) {
      let oSectionSelectionStatus = ContentScreenProps.screen.getSetSectionSelectionStatus();
      let oScreenProps = ContentScreenProps.screen;
      let sRelationshipSectionElementId = oSectionSelectionStatus['selectedRelationship'].id;
      if (!CS.isEmpty(sRelationshipSectionElementId)) {
        let oReferencedElements = oScreenProps.getReferencedElements();
        let oRefElement = oReferencedElements[sRelationshipSectionElementId];
        if(!oRefElement) {
          return;
        }
        let oActiveEntity = this.getActiveContent();
        oPostDataForFilter.instanceId = oActiveEntity.id;
        oPostDataForFilter.relationshipId = oRefElement.propertyId;
        oPostDataForFilter.type = oRefElement.type;
        oPostDataForFilter.sideId = oRefElement.id;
        oPostDataForFilter.targetIds = [oRefElement.relationshipSide.klassId];

      }
    },

    getIsVariantQuicklist: function(){
      let oComponentProps = ContentUtils.getComponentProps();
      let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
      let bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
      let bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();

      return  bAvailableEntityChildVariantViewVisibilityStatus || bAvailableEntityParentVariantViewVisibilityStatus;
    },

    fillIdsToExcludeForVariantQuicklist: function (oPostData) {
      let aIdsToInclude = [];
      let aIdsToExclude = [];
      let oActiveEntity = _getActiveEntity();
      let oComponentProps = ContentUtils.getComponentProps();
      let oDummyVariant = _getActiveContentOrVariant();
      let sSelectedEntity = oComponentProps.variantSectionViewProps.getSelectedEntity();
      oDummyVariant = oDummyVariant.contentClone || oDummyVariant;
      let oLinkedInstances = _getLinkedInstancesFromVariant(oDummyVariant);
      let bIsForTableContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
      if (oLinkedInstances[sSelectedEntity]) {
        let aLinkedInstancesIdsFromClone = CS.map(oLinkedInstances[sSelectedEntity], 'id');
        let oActiveEntityLinkedInstances = _getLinkedInstancesFromVariant(oActiveEntity);
        let aLinkedInstancesIdsFromContent = CS.map(oActiveEntityLinkedInstances[sSelectedEntity], 'id');
        aIdsToInclude = CS.difference(aLinkedInstancesIdsFromContent, aLinkedInstancesIdsFromClone);
        aIdsToExclude = aLinkedInstancesIdsFromClone;
      } else if (bIsForTableContext) {
        let oActivePopOverContextData = oComponentProps.uomProps.getActivePopOverContextData();
        if (CS.isNotEmpty(oActivePopOverContextData)) {
          let sContextId = oActivePopOverContextData.contextId;
          let sColumnId = oActivePopOverContextData.entity;
          let oUOMTableProps = ContentScreenProps.tableViewProps.getTableViewPropsByContext(sContextId, oDummyVariant.id);
          let aTableBodyData = oUOMTableProps.getBodyData();
          let aOriginalRowData = oUOMTableProps.getOriginalRowData();
          let sRowId = oActivePopOverContextData.contextInstanceId;
          let oRowData = CS.find(aTableBodyData, {id: sRowId});
          let oOriginalRowData = CS.find(aOriginalRowData, {id: sRowId});
          aIdsToExclude = oRowData.properties[sColumnId].values;
          let aOriginalValues = oOriginalRowData.properties[sColumnId].values;
          aIdsToInclude = CS.difference(aOriginalValues, aIdsToExclude);
        }
      }
      if (CS.isNotEmpty(aIdsToInclude)) {
        oPostData.idsToInclude = aIdsToInclude;
      }
      if (CS.isNotEmpty(aIdsToExclude)) {
        oPostData.idsToExclude = aIdsToExclude;
      }
    },

    setRuleViolationProps : function(oCurrentRuleViolationFromServer){
      var ScreenProps = ContentScreenProps.screen;
      ScreenProps.emptyRuleViolationObject();
      var oRuleViolation = ScreenProps.getRuleViolationObject();
      if(!CS.isEmpty(oCurrentRuleViolationFromServer)){
        CS.forEach(oCurrentRuleViolationFromServer,function (oRule) {
          var aCurrentRuleViolation = oRuleViolation[oRule.entityId];
          if(CS.isEmpty(aCurrentRuleViolation)){
            aCurrentRuleViolation = [];
            oRuleViolation[oRule.entityId] = aCurrentRuleViolation;
          }
          aCurrentRuleViolation.push(oRule);
        });
      }
    },

    testNumber: function(sNumber){
      var oRegexPatt = /^-?(\d*\.)?\d+$/;


      var sNewNum = CS.includes(sNumber, "e") ? Number(sNumber).toFixed(20) : sNumber; //To check exponential numbers.
      return oRegexPatt.test(sNewNum);
    },

    getCurrentLocaleNumberValue: function (sOriginalValue, iPrecision) {
      return _getCurrentLocaleNumberValue(sOriginalValue, iPrecision);
    },

    getUserNameById : function(sUserId){
       return _getUserNameById(sUserId);
    },

    getSelectedContents: function () {
      return _getSelectedContents();
    },

    getDisplayUnitFromDefaultUnit: function (sDefaultUnit, sType) {
      return _getDisplayUnitFromDefaultUnit(sDefaultUnit, sType);
    },

    getEntityType: function (oEntity) {
      return _getEntityType(oEntity);
    },

    isAssetBaseType: function (sBaseType) {
      return _isAssetBaseType(sBaseType);
    },

    isSupplierBaseType: function (sBaseType) {
      return _isSupplierBaseType(sBaseType);
    },

    setAttributeListForFiltering: function (aAttributeList) {
      _setAttributeListForFiltering(aAttributeList);
    },

    getRoleList: function () {
      return _getRoleList();
    },

    preProcessAttributeAfterGet: function (oContent) {
      _preProcessContentAttributesAfterGet(oContent);
    },

    isAttributeTypeHtml: function (sType) {
      return AttributeUtils.isAttributeTypeHtml(sType);
    },

    isAttributeTypeDescription: function (sType) {
      return AttributeUtils.isAttributeTypeDescription(sType);
    },

    /** @deprecated **/
    isAttributeTypeType: function (sType) {
      return AttributeUtils.isAttributeTypeType(sType);
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

    isAttributeTypeDate: function (sType) {
      return AttributeUtils.isAttributeTypeDate(sType);
    },

    isAttributeTypeNumber: function (sType) {
      return AttributeUtils.isAttributeTypeNumber(sType);
    },

    isAttributeTypeMeasurement: function (sType) {
      return AttributeUtils.isAttributeTypeMeasurement(sType);
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

    isAttributeTypeUser: function (sType) {
      return AttributeUtils.isAttributeTypeUser(sType);
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

    isMandatoryAttribute: function (sType) {
      return AttributeUtils.isMandatoryAttribute(sType);
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

    isAttributeTypePrice: function (sType) {
      return AttributeUtils.isAttributeTypePrice(sType);
    },

    getMasterAttributeById: function (sId) {
      return _getMasterAttributeById(sId);
    },

    setActiveEntity: function (oEntity) {
      _setActiveEntity(oEntity);
    },

    setSelectedEntityList: function (aList) {
      _setSelectedEntityList(aList);
    },

    failureCallback: function (oResponse, sFunctionName, oTranslations) {
      CommonUtils.failureCallback(oResponse, sFunctionName, oTranslations);
    },

    setViewMode: function (sViewMode) {
      _setViewMode(sViewMode);
    },

    getViewMode: function () {
      return _getViewMode();
    },

    getDefaultViewMode: function () {
      return _getDefaultViewMode();
    },

    getAttributeList: function () {
      return _getAttributeList();
    },

    setFilterProps: function (oFilterProps, bIsPreventResetFilterProps, oFilterContext, aSelectedFilters) {
      _setFilterProps(oFilterProps, bIsPreventResetFilterProps, oFilterContext, aSelectedFilters);
    },

    isTag: function (sBaseType) {
      return TagUtils.isTag(sBaseType);
    },

    getAllMenus: function () {
      return _getAllMenus();
    },

    getScreenModeBasedOnKlassBaseType: function (sKlassBaseType) {
      return _getScreenModeBasedOnKlassBaseType(sKlassBaseType);
    },

    getScreenModeBasedOnEntityBaseType: function (sBaseType) {
      return _getScreenModeBasedOnEntityBaseType(sBaseType);
    },

    getScreenModeBasedOnActiveEntity: function () {
      return _getScreenModeBasedOnActiveEntity();
    },

    getUserList: function () {
      return _getUserList();
    },

    getRelationshipViewStatus: function () {
      return _getRelationshipViewStatus();
    },

    getNatureRelationshipViewStatus: function (){
      return _getNatureRelationshipViewStatus();
    },

    getIsStaticCollectionScreen: function () {
      return _getIsStaticCollectionScreen();
    },

    getIsDynamicCollectionScreen: function () {
      return _getIsDynamicCollectionScreen();
    },

    isCollectionScreen: function () {
      return _isCollectionScreen();
    },

    performCommonSuccessOperations: function (oResponse, oExtraData) {
      _performCommonSuccessOperations(oResponse, oExtraData);
    },

    setLoadedPropertiesFromConfigDetails: function (oConfigDetails) {
      _setLoadedPropertiesFromConfigDetails(oConfigDetails);
    },

    setAppliedFilterData: function (oAppliedFilterData, oFilterContext) {
      let oFilterStore = _getFilterStore(oFilterContext);
      oFilterStore.setAppliedFilterData(oAppliedFilterData);
    },

    getAvailableEntityViewStatus: function () {
      return _getAvailableEntityViewStatus();
    },

    getDateAttributeInTimeFormat: function (sValue) {
      return CommonUtils.getDateAttributeInTimeFormat(sValue);
    },

    getDateAttributeInDateTimeFormat: function (sValue) {
      return CommonUtils.getDateAttributeInDateTimeFormat(sValue)
    },

    clearSelectedAvailableEntities: function () {
      ContentScreenProps.availableEntityViewProps.setSelectedEntities([]);
    },

    getFilterProps: function (oFilterContext,) {
      return _getFilterStore(oFilterContext).getFilterProps();
    },

    activeContentSafetyCheck: function () {
      //safe to proceed if true is returned.
      var oActiveEntity = _getActiveEntity();
      if(oActiveEntity && oActiveEntity.contentClone) {
        this.setShakingStatus(true);
        _triggerChange();
      } else {
        return true;
      }
    },

    activeTaskSafetyCheck: function () {
      var bIsTaskDirty = ContentScreenProps.taskProps.getIsTaskDirty();
      if(bIsTaskDirty) {
        this.setShakingStatus(true);
        _triggerChange();
      } else {
        return true;
      }
    },

    getXRayPostData: function (sRelationshipId) {
      return _getXRayPostData(sRelationshipId);
    },

    setShakingStatus: function (bStatus) {
      ContentScreenProps.screen.setShakingStatus(bStatus);
      if(bStatus){
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      }
    },

    isNatureRelationship: function (sRelationshipType) {
      return _isNatureRelationship(sRelationshipType);
    },

    getContentRelationshipKeyById: function (sId) {
      return _getContentRelationshipKeyById(sId);
    },

    isNatureRelationshipByReferencedData: function (sId) {
      return _isNatureRelationshipByReferencedData(sId);
    },

    isVariantRelationship: function (sRelationshipType) {
      return _isVariantRelationship(sRelationshipType);
    },

    isProductVariantRelationship: function (sRelationshipType) {
      return _isProductVariantRelationship(sRelationshipType);
    },

    isContextTypeContextualVariant: function (sType) {
      return _isContextTypeContextualVariant(sType);
    },

    isContextTypeProductVariant: function (sType) {
      return _isContextTypeProductVariant(sType);
    },

    getVariantRelationshipViewStatus: function (){
      return _getVariantRelationshipViewStatus();
    },

    getSelectedTabId: function (sActiveEntityId){
      return _getSelectedTabId(sActiveEntityId);
    },

    getActiveEntitySelectedFilterId: function (sActiveEntityId){
      return _getActiveEntitySelectedFilterId(sActiveEntityId);
    },

    setActiveEntitySelectedFilterId: function(sSelectedFilterId){
      _setActiveEntitySelectedFilterId(sSelectedFilterId);
    },

    isBaseTypeArticle: function (sType) {
      return (sType == BaseTypesDictionary["contentBaseType"]);
    },

    isTextAssetBaseType: function (sType = "") {
      return (sType == BaseTypesDictionary["textAssetBaseType"]);
    },

    isTargetBaseType: function (sType = "") {
      return (sType == BaseTypesDictionary["marketBaseType"]);
    },

    isNatureClass: function(sId){
      return _isNatureClass(sId)
    },

    getNatureKlassIdFromKlassIds: function (aIds) {
      return _getNatureKlassIdsFromKlassIds(aIds)[0];
    },

    getConcatenatedAttributeExpressionList: function (oAttribute, aAttributeList, aEntityTags, oReferencedAttributes, oReferencedTags, oReferencedElements) {
      return _getConcatenatedAttributeExpressionList(oAttribute, aAttributeList, aEntityTags, oReferencedAttributes, oReferencedTags, oReferencedElements);
    },

    getTextColorBasedOnBackgroundColor: function (sBackgroundColorHex) {
      return _getTextColorBasedOnBackgroundColor(sBackgroundColorHex);
    },

    getTabUrlFromTabId: function (sTabId) {
      return _getTabUrlFromTabId(sTabId);
    },

    getTabTypeFromTabId: function (sTabId) {
      return _getTabTypeFromTabId(sTabId);
    },

    isAllRelevanceZero: function (aTags){
      return _isAllRelevanceZero(aTags);
    },

    removeTrailingBreadcrumbPath: function (sId, sType){
      return _removeTrailingBreadcrumbPath(sId, sType);
    },

    setThumbnailModeToXRayVisionMode: function (sRelationshipId) {
      _setThumbnailModeToXRayVisionMode(sRelationshipId);
    },

    isXRayVisionModeActive: function () {
      return _isXRayVisionModeActive();
    },

    getRelationshipTypeById: function(sRelationshipId){
     return _getRelationshipTypeById(sRelationshipId);
    },

    getSelectedModuleId: function () {
      var oSelectedModule = _getSelectedModule();

      return oSelectedModule ? oSelectedModule.id : "";
    },

    getSelectedModule: function () {
      return _getSelectedModule();
    },

    setSelectedModuleById: function (sId) {
      _setSelectedModuleById(sId);
    },

    setSelectedModuleAndDefaultDataById: function (sId) {
      _setSelectedModuleAndDefaultDataById(sId);
    },

    getEntityClassType: function (oEntity) {
      return _getEntityClassType(oEntity);
    },

    getCurrentUser: function() {
      return GlobalStore.getCurrentUser();
    },

    getLanguageInfo: function () {
      return GlobalStore.getLanguageInfo();
    },

    getTagListMap: function () {
      return AppData.getTagListMap();
    },

    getAttributeListMap: function () {
      return AppData.getAttributeListMap();
    },

    getTaxonomyList: function () {
      return AppData.getTaxonomyList();
    },

    getMeasurementAttributeValueToShow: function (sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision) {
      return AttributeUtils.getMeasurementAttributeValueToShow(sOriginalValue, sCurrentUnit, sExpectedUnit, sPrecision);
    },

    getBaseUnitFromType: function (sType) {
      return AttributeUtils.getBaseUnitFromType(sType);
    },

    getTruncatedValue: function (iVal, sPrecision) {
      return AttributeUtils.getTruncatedValue(iVal, sPrecision);
    },

    createAttributeInstanceObject: function (sDefaultVal, sAttributeId, bWithoutSuffix) {
      return _createAttributeInstanceObject(sDefaultVal, sAttributeId, bWithoutSuffix);
    },

    createTagInstanceObject: function (oMasterTag, bWithoutSuffix) {
      return _createTagInstanceObject(oMasterTag, bWithoutSuffix);
    },

    getDummyVariant: function () {
      return _getDummyVariant();
    },

    applyValuesToAllTreeNodes: function (oTreeValuesInList, aKayValue) {
      _applyValuesToAllTreeNodes(oTreeValuesInList, aKayValue);
    },

    addNewTreeNodesToValueList: function (oTreeValuesInList, aTreeNode, aKeyValue) {
      _addNewTreeNodesToValueList(oTreeValuesInList, aTreeNode, aKeyValue);
    },

    applyToAllNodesBelow: function (aPropertyKeyValue, oNode, oVisualProps, bExcludeCallingNode) {
      _applyToAllNodesBelow(aPropertyKeyValue, oNode, oVisualProps, bExcludeCallingNode);
    },

    /**
     * @borrows: Setting-Utils
     * @param oObjectToMakeDirty
     * @returns {*|void}
     */
    makeObjectDirty: function (oObjectToMakeDirty) {
      return RefactoringStore.makeObjectDirty(oObjectToMakeDirty);
    },

    // TODO: #Refact20 Removed with gut
    // /**
    //  * @borrows: Setting-Utils
    //  * @param oActiveEntity
    //  * @param sSectionId
    //  * @returns {*|void}
    //  */
    // handleSectionDeleted: function (oActiveEntity, sSectionId) {
    //   return SettingUtils.handleSectionDeleted(oActiveEntity, sSectionId);
    // },

    activeCollectionHierarchySafetyCheck: function () {
      return _activeCollectionHierarchySafetyCheck();
    },

    activeTaxonomyOrganiseSafetyCheck: function (oFilterContext) {
      return _activeTaxonomyOrganiseSafetyCheck(oFilterContext);
    },

    getEntityByIdUrl: function (sEntityBaseType) {
      return _getEntityByIdUrl(sEntityBaseType);
    },

    deleteRequest: function(sUrl, oQueryString, oRequestData, fSuccess, fFailure, oExtraData){
      _deleteRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure, oExtraData);
    },

    putRequest: function (sUrl, oQueryString, oRequestData, fSuccess, fFailure, oExtraData) {
      _putRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure, oExtraData);
    },

    postRequest: function (sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader, oExtraData) {
      _postRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader, oExtraData);
    },

    getRequest: function (sUrl, oParameters, fSuccess, fFailure, oExtraData) {
      _getRequest(sUrl, oParameters, fSuccess, fFailure, oExtraData);
    },

    getSelectedHierarchyContext: function () {
      return _getSelectedHierarchyContext();
    },

    getAttributeTypeForVisual: function (sType, sAttrId) {
      return _getAttributeTypeForVisual(sType, sAttrId);
    },

    allHierarchySafetyCheck: function () {
      return _allHierarchySafetyCheck();
    },

    allHierarchyNonDirty: function (oFilterContext) {
      return _allHierarchyNonDirty(oFilterContext);
    },

    getActiveContentOrVariant: function () {
      return _getActiveContentOrVariant();
    },

    makeActiveContentOrVariantDirty: function () {
      return _makeActiveContentOrVariantDirty();
    },

    addSectionDataFromClassIntoEntity: function (oEntity, sContext) {
      return _addSectionDataFromClassIntoEntity(oEntity, sContext);
    },

    getIsAnyHierarchySelectedExceptFilterHierarchy: function () {
      return _getIsAnyHierarchySelectedExceptFilterHierarchyAndRelationshipHierarchy();
    },

    getIsHierarchyViewMode: function () {
      return CS.isNotEmpty(_getSelectedHierarchyContext());
    },

    getHierarchyDummyNodeId: function () {
      return TAXONOMY_HIERARCHY_ALL_DUMMY_NODE;
    },

    getElementAssetData: function (oElement) {
      return _getElementAssetData(oElement);
    },

    getAllNumericTypeAttributes: function () {
      return AttributeUtils.getAllNumericTypeAttributes();
    },

    getAllExcludedAttributeTypeForBulkEdit: function () {
      return AttributeUtils.getAllExcludedAttributeTypeForBulkEdit();
    },

    getKlassFromReferencedKlassesById: function(sKlassId){
      return _getKlassFromReferencedKlassesById(sKlassId);
    },

    addDataToReferencedKlasses: function(oReferencedKlassesToAdd){
      _addDataToReferencedKlasses(oReferencedKlassesToAdd);
    },

    updateReferencedKlassRelatedData: function(oReferencedKlasses){
      _updateReferencedKlassRelatedData(oReferencedKlasses);
    },

    getEntitySearchText: function(){
      return ContentScreenProps.screen.getEntitySearchText();
    },

    getEndPointAttributesAndTagsMappings: function () {
      var oEndpointMappingViewProps = ContentScreenProps.endPointMappingViewProps;
      var oActiveEndpoint = oEndpointMappingViewProps.getActiveEndpoint();
      return oActiveEndpoint.attributeMappings.concat(oActiveEndpoint.tagMappings);
    },

    clearXRayData : function () {
      _clearXRayData();
    },

    clearXRayDataFromBreadCrumbPayLoad : function () {
      _clearXRayDataFromBreadCrumbPayLoad();
    },

    setActiveXRayPropertyGroup: function (oData, sRelationshipId) {
      _setActiveXRayPropertyGroup(oData,sRelationshipId);
    },

    getActiveXRayPropertyGroup: function (sRelationshipId) {
      return _getActiveXRayPropertyGroup(sRelationshipId);
    },

    getReferencedTags: function () {
      return _getReferencedTags();
    },

    isOnboarding: function () {
     return _isOnboarding();
    },

    getIsUOMVariantDialogOpen: function () {
      return _getIsUOMVariantDialogOpen();
    },

    getIdForTableViewProps: function (sContextId, sAttributeId) {
      return _getIdForTableViewProps(sContextId, sAttributeId)
    },

    getIdForTableViewPropsSimple: function (sContextId, sAttributeId) {
      return _getIdForTableViewPropsSimple(sContextId, sAttributeId)
    },

    setOpenedDialogAttributeData: function (sContextId, sAttributeId, sLabel, sContextType, sKlassInstanceId = ContentUtils.getActiveContent().id, sParentContextId) {
      let oOpenedDialogAttributeData = ContentScreenProps.uomProps.getOpenedDialogAttributeData();
      oOpenedDialogAttributeData.contextId = sContextId;
      oOpenedDialogAttributeData.attributeId = sAttributeId;
      oOpenedDialogAttributeData.label = sLabel;
      oOpenedDialogAttributeData.gridComponentKey = ContentUtils.generateUUID();
      oOpenedDialogAttributeData.contextType = sContextType;
      oOpenedDialogAttributeData.klassInstanceId = sKlassInstanceId;
      oOpenedDialogAttributeData.parentContextId = sParentContextId;
    },

    generateADMForLinkedInstances: function(aNewLinkedInstances, aOldLinkedInstances) {
      return _generateADMForLinkedInstances(aNewLinkedInstances, aOldLinkedInstances);
    },

    discardCloneIfActiveEntityAndActiveEntityCloneEqual: function(){
      _discardCloneIfActiveEntityAndActiveEntityCloneEqual();
    },

    getIsCurrentUserSystemAdmin: function () {
      return CommonUtils.isCurrentUserSystemAdmin();
    },

    getIsCurrentUserReadOnly: function () {
      return CommonUtils.isCurrentUserReadOnly();
    },

    getLinkedInstancesFromVariant: function(oVariant){
      return _getLinkedInstancesFromVariant(oVariant);
    },

    processLinkedInstancesForContentContext: function(oVariant, oReferencedInstances){
      _processLinkedInstancesForContentContext(oVariant, oReferencedInstances);
    },

    getIsOnLandingPage: function () {
      return _getIsOnLandingPage();
    },

    getDecodedTranslation: function (sStringToCompile, oParameter) {
      return _getDecodedTranslation(sStringToCompile, oParameter);
    },

    getIsTableContentDirty : function () {
      return _getIsTableContentDirty();
    },

    getUpdatedEntityTag: function (oEntity, aTagValueRelevanceData, sTagId) {
      return _getUpdatedEntityTag(oEntity, aTagValueRelevanceData, sTagId);
    },

    updateEntitySectionDefaultTagValue: function (aSections, sSectionId, sElementId, sTagGroupId, aTagValueRelevanceData) {
      _updateEntitySectionDefaultTagValue(aSections, sSectionId, sElementId, sTagGroupId, aTagValueRelevanceData);
    },

    updateTagValueRelevanceOrRange: function (oTagValue, oRelevanceData, bUpdateRange) {
      TagUtils.updateTagValueRelevanceOrRange(oTagValue, oRelevanceData, bUpdateRange);
    },

    getDataIntegrationInfo: function () {
      return {
        physicalCatalogId: SessionProps.getSessionPhysicalCatalogId(),
        endPoint: SessionProps.getSessionEndpointId(),
        endpointType: SessionProps.getSessionEndpointType(),
        organizationId: SessionProps.getSessionOrganizationId(),
        portalId: SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.PORTAL)
      }
    },

    getSelectedPhysicalCatalogId: function () {
      return SessionProps.getSessionPhysicalCatalogId();
    },

    getIsArchive: function () {
      return SessionProps.getIsArchive();
    },

    getSelectedPortalId: function () {
      return CommonUtils.getSelectedPortalId();
    },

    getViolatingMandatoryFields: function (aSections) {
      return _getViolatingMandatoryFields(aSections);
    },

    getEmptyFilterObject: function (iSize, iFrom){
      return _getEmptyFilterObject(iSize, iFrom);
    },

    isContentGridOpen: () => {
      return _isContentGridOpen();
    },

    shouldSaveContentGrid: (oEvent) => {
      return _shouldSaveContentGrid(oEvent);
    },


    getTemplateIdForServer: function (sTemplateId) {
      return _getTemplateIdForServer(sTemplateId);
    },

    getContextTypeBasedOnContextId: function (sContextId) {
      return _getContextTypeBasedOnContextId(sContextId);
    },

    isOnHeaderDisabledTab: function () {
      return _isOnHeaderDisabledTab();
    },

    isContentAvailableInSelectedDataLanguage: function (oKlassInstance) {
      return _isContentAvailableInSelectedDataLanguage(oKlassInstance);
    },

    getDesiredReferencedDataAndTypeBySectionId: function (sSectionId, oReferencedData) {
      return _getDesiredReferencedDataAndTypeBySectionId(sSectionId, oReferencedData);
    },

    updateNatureNonNatureKlassIds: function (oReferencedClasses) {
      _updateNatureNonNatureKlassIds(oReferencedClasses);
    },

    initializeSectionExpansionState: function (aSections) {
      _initializeSectionExpansionState(aSections);
    },

    resetModuleSelection: function () {
      _resetModuleSelection();
    },

    contextTableSafetyCheck: function () {
      if (this.getIsTableContentDirty()) {
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        return true;
      }
    },

    getMultiTaxonomyData: function (aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree) {
      return CommonUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree);
    },

    getAssetDataObject: function (oReferencedAsset) {
      return _getAssetDataObject(oReferencedAsset);
    },

    getOnlySelectedKlassInstances: function (aKlassInstancesDetails, aSelectedInstanceIds) {
      return _getOnlySelectedKlassInstances (aKlassInstancesDetails, aSelectedInstanceIds);
    },

    makeInstancesComparisonRowDataForTags: function (oLayoutData, oConfigDetails, bIsGoldenRecordComparison) {
      _makeInstancesComparisonRowDataForTags(oLayoutData, oConfigDetails, bIsGoldenRecordComparison);
    },

    makeInstancesComparisonRowDataForAttributes: function (oLayoutData, oConfigDetails, bIsGoldenRecordComparison) {
      _makeInstancesComparisonRowDataForAttributes(oLayoutData, oConfigDetails, bIsGoldenRecordComparison);
    },

    makeInstancesComparisonRowDataForRelationshipTab: function (oLayoutData, oConfigDetails, aKlassInstances, bIsGoldenRecordComparison) {
      _makeInstancesComparisonRowDataForRelationshipTab(oLayoutData, oConfigDetails, aKlassInstances, bIsGoldenRecordComparison)
    },

    makeInstancesComparisonRowDataForNatureRelationships: function (oLayoutData, oConfigDetails, aKlassInstances, bIsGoldenRecordComparison) {
      _makeInstancesComparisonRowDataForNatureRelationships(oLayoutData, oConfigDetails, aKlassInstances, bIsGoldenRecordComparison)
    },

    makeInstancesComparisonRowDataForHeaderInformation: function (oLayoutData, sScreenMode) {
      _makeInstancesComparisonRowDataForHeaderInformation(oLayoutData, sScreenMode);
    },

    makeInstancesComparisonRowDataForFixedHeader: function (oLayoutData) {
      _makeInstancesComparisonRowDataForFixedHeader(oLayoutData);
    },

    makeInstancesComparisonColumnData: function (oLayoutData, oConfigDetails, aSelectedKlassInstances, bIsGoldenRecordComparison, oPropertyRecommendations) {
      _makeInstancesComparisonColumnData(oLayoutData, oConfigDetails, aSelectedKlassInstances, bIsGoldenRecordComparison, oPropertyRecommendations);
    },

    validateVariantContextSelection: function (oActiveEntity, oActiveVariantContext) {
      return _validateVariantContextSelection(oActiveEntity, oActiveVariantContext);
    },

    getIsRuleViolatedContentsScreen: function () {
      return this.getComponentProps().informationTabProps.getIsRuleViolatedContentsScreen();
    },

    getIsDamRuleViolatedContentsScreen: function () {
      return this.getComponentProps().damInformationTabProps.getIsRuleViolatedContentsScreen();
    },

    getMomentOfDate: function (sDate) {
      return CommonUtils.getMomentOfDate(sDate);
    },

    getContentName: function (oContent) {
      return _getContentName(oContent);
    },

    isDateLessThanGivenDays: function (iTimeStamp, sDuration) {
      return CommonUtils.isDateLessThanGivenDays(iTimeStamp, sDuration)
    },

    addEntityInformationData: function (aContentList) {
      return _addEntityInformationData(aContentList);
    },

    getURLForTaxonomiesForRelationship: function (sClickedTaxonomyId, sRelationshipId) {
      return _getURLForTaxonomiesForRelationship(sClickedTaxonomyId, sRelationshipId);
    },

    getTranslations: function () {
      return getTranslation();
    },

    getActiveEndpointData: function () {
      return SessionProps.getActiveEndpointData();
    },

    setSelectedDataLanguage: function (sLanguageCode) {
      CommonUtils.setSelectedDataLanguage(sLanguageCode);
    },

    getNatureTypeFromContent: function (oActiveContent) {
      return _getNatureTypeFromContent(oActiveContent);
    },

    getContentNatureTypeId: function (oContent, oReferncedKlasses) {
      return _getContentNatureTypeId(oContent, oReferncedKlasses);
    },

    /************************** Add API'S Before this comment and KEEP RESET ALL AT LAST *************************/
    updateSectionIdListBySequenceForSelfRelationship: function (aSectionIdsBySequence, oReferencedRelationships) {
      return _updateSectionIdListBySequenceForSelfRelationship(aSectionIdsBySequence, oReferencedRelationships);
    },

    /************************** Add API'S Before this comment and KEEP RESET ALL AT LAST *************************/

    getRelationshipIdFromSideId: function (sRelationshipSideId) {
      return _getRelationshipIdFromSideId(sRelationshipSideId)
    },

    makeWindowHistoryStateStableForEntityCreation: function (fCallback) {
      _makeWindowHistoryStateStableForEntityCreation(fCallback);
    } ,

    addRelationshipDummyEntity: function (sRelationshipId, oEntity, sRelationSideId, bIsNatureRelationship) {
      _addRelationshipDummyEntity(sRelationshipId, oEntity, sRelationSideId, bIsNatureRelationship);
    },

    prepareSelectedTaxonomiesDataForChipsView: function(oReferencedTaxonomies, aSelectedTaxonomyIds){
      return _prepareSelectedTaxonomiesDataForChipsView(oReferencedTaxonomies, aSelectedTaxonomyIds);
    },

    prepareSelectedClassesDataForChipsView: function(oReferencedTaxonomies, aSelectedTaxonomyIds){
      return _prepareSelectedClassesDataForChipsView(oReferencedTaxonomies, aSelectedTaxonomyIds);
    },

    prepareReferencedTaxonomyMap: function (oReferencedTaxonomy, oTaxonomyMap) {
      _prepareReferencedTaxonomyMap(oReferencedTaxonomy, oTaxonomyMap);
    },

    prepareSelectedTaxonomiesFromMap: function (sTaxonomyId, aSelectedTaxonomies, oTaxonomyMap) {
      _prepareSelectedTaxonomiesFromMap(sTaxonomyId, aSelectedTaxonomies, oTaxonomyMap)
    },

    setNatureAndVariantRelationshipIdsIntoProps: function (oReferencedNatureRelationship) {
      _setNatureAndVariantRelationshipIdsIntoProps(oReferencedNatureRelationship);
    },

    getSelectedDataLanguage: function(){
      return _getSelectedDataLanguage()
    },

    preProcessContentAttributesAfterGetForComparison: function(oInstance, aContentAttributes, aContentTags, oConfigDetails, oSelectedLanguageForComparison) {
      _preProcessContentAttributesAfterGetForComparison(oInstance, aContentAttributes, aContentTags, oConfigDetails, oSelectedLanguageForComparison);
    },

    getBookmarkModuleId: function () {
      return ContentScreenProps.collectionViewProps.getBookmarkModuleId();
    },

    setBookmarkModuleId: function (sBookmarkModuleId) {
      ContentScreenProps.collectionViewProps.setBookmarkModuleId(sBookmarkModuleId);
    },

    getFilterContextBasedOnTabId: function (sSelectedTabId) {
      return _getFilterContextBasedOnTabId(sSelectedTabId);
    },

    getNatureRelationshipIdLabelByRelationshipType: function (sRelationshipType) {
      return _getNatureRelationshipIdLabelByRelationshipType(sRelationshipType);
    },

    addTagsInContentBasedOnTagType: function (oMasterTag, oTagGroup) {
      _addTagsInContentBasedOnTagType(oMasterTag, oTagGroup);
    },

    updateUnselectedTagValuesInTagEntity: function (oTagGroup, aTagValues) {
      _updateUnselectedTagValuesInTagEntity(oTagGroup, aTagValues);
    },

    generateUUID: function(){
      return UniqueIdentifierGenerator.generateUUID();
    },

    prepareConcatenatedAttributeExpressionListForProducts : function (oChildren , oReferencedAttributes, oReferencedTags, oReferencedElements ){
       _prepareConcatenatedAttributeExpressionListForProducts(oChildren, oReferencedAttributes, oReferencedTags, oReferencedElements)
    },

    preProcessAttributeBeforeSave: function(oProperty){
      return _preProcessAttributeBeforeSave(oProperty);
    },

    getTemplateAndTypeIdForServer (sId) {
      return _getTemplateAndTypeIdForServer(sId);
    },

    downloadFromByteStream: function (oByteStream, sFileName) {
      CommonUtils.downloadFromByteStream(oByteStream, sFileName);
    },

    isHideCreateButton: () => {
      return _isHideCreateButton();
    },

    setIsKlassInstanceFlatPropertyUpdated: function (bPropertyStatus) {
       _setIsKlassInstanceFlatPropertyUpdated(bPropertyStatus);
    },

    getIsKlassInstanceFlatPropertyUpdated: function () {
      return _getIsKlassInstanceFlatPropertyUpdated();
    },

    getSelectedModulesForFilter: function() {
      return _getSelectedModulesForFilter();
    },

    getKlassInstanceId: function() {
      return _getKlassInstanceId();
    },

    getBlobFromUrl: function (dataURI, sFileName) {
      return _getBlobFromUrl(dataURI, sFileName);
    },

    getIsContentDisabled: function (oKlassInstance) {
      return !_isContentAvailableInSelectedDataLanguage(oKlassInstance) || CommonUtils.isCurrentUserReadOnly();
    },

    removeOldTagsByTimeStampFromContentUsingTagGroup: function (oContent, oTagGroup) {
      _removeOldTagsByTimeStampFromContentUsingTagGroup(oContent, oTagGroup);
    },

    getTaxonomyPath: function (sLabel, sParentId, oReferencedTaxonomyMap) {
      return _getTaxonomyPath(sLabel, sParentId, oReferencedTaxonomyMap);
    },

    getDownloadDialogData: function (bIsAllFieldsActive, bIsHideDownloadAsExtension) {
      return _getDownloadDialogData(bIsAllFieldsActive, bIsHideDownloadAsExtension);
    },

    isQuickListNode: (oBreadcrumbNode) => {
        return _isQuickListNode(oBreadcrumbNode);
    },

    validateContextDuplication: function (oCallback) {
      return _getContentStore().validateContextDuplication(oCallback);
    },

  /************************** Add API'S Before this comment and KEEP RESET ALL AT LAST *************************/
    resetAll: function () {
      AppData.reset();
      ContentScreenProps.reset();
    }
  };

})();

MicroEvent.mixin(ContentUtils);

export default ContentUtils;
