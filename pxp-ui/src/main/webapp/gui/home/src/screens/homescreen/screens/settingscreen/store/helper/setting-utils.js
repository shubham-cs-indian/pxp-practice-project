
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';

import CS from '../../../../../../libraries/cs';
import LogFactory from '../../../../../../libraries/logger/log-factory';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import GlobalStore from '../../../../store/global-store.js';
import { TaxonomyRequestMapping as oTaxonomyRequestMapping } from '../../tack/setting-screen-request-mapping';
import SettingScreenAppData from './../model/setting-screen-app-data';
import SettingScreenProps from './../model/setting-screen-props';
import ProcessConstants from '../../tack/process-constants';
import SystemLevelIdDictionary from '../../../../../../commonmodule/tack/system-level-id-dictionary';
import TemplateTabsDictionary from '../../../../../../commonmodule/tack/template-tabs-dictionary';
import MockDataForEntitiesList from '../../../../../../commonmodule/tack/entities-list';
import TagTypeConstants from '../../../../../../commonmodule/tack/tag-type-constants';
import BaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import MockDataForMeasurementMetricAndImperial from './../../../../../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import ContextTypeDictionary from '../../../../../../commonmodule/tack/context-type-dictionary';
import { setting as oSettingModules } from '../../../../../../commonmodule/tack/global-modules-dictionary';
import ClassNatureTypes from '../../tack/mock/mock-data-for-class-nature-types';
import ConfigModulesList from '../../tack/settinglayouttack/config-modules-list';
import ConfigPropertyTypeDictionary from '../../tack/settinglayouttack/config-module-data-model-property-group-type-dictionary';
import ClassCategoryConstants from '../../tack/class-category-constants-dictionary';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import RelationshipTypeDictionary from '../../../../../../commonmodule/tack/relationship-type-dictionary';
import MockDataForKlassTypes from '../../../../../../commonmodule/tack/mock-data-for-class-types';
import ClassRootBaseTypeIdDictionary from '../../../../../../commonmodule/tack/class-root-basetype-id-dictionary';
import CommonModuleConstants from '../../../../../../commonmodule/tack/constants';
import HtmlAttributeGridSkeleton from '../../tack/html-attribute-config-grid-view-skeleton';
import TextAttributeGridSkeleton from '../../tack/text-attribute-config-grid-view-skeleton';
import NumberAttributeGridSkeleton from '../../tack/number-attribute-config-grid-view-skeleton';
import DateAttributeGridSkeleton from '../../tack/date-attribute-config-grid-view-skeleton';
import CalculatedAttributeGridSkeleton from '../../tack/calculated-attribute-config-grid-view-skeleton';
import ConcatenatedAttributeGridSkeleton from '../../tack/concatenated-attribute-config-grid-view-skeleton';
import MeasurementAttributeGridSkeleton from '../../tack/measurement-attribute-config-grid-view-skeleton';
import PriceAttributeGridSkeleton from '../../tack/price-attribute-config-grid-view-skeleton';
import StandardAttributeGridSkeleton from '../../tack/standard-attribute-config-grid-view-skeleton';
//skeleton for tags
import LOVTagsGridSkeleton from '../../tack/lov-tag-config-grid-view-skeleton';
import StatusTagsGridSkeleton from '../../tack/status-tag-config-grid-view-skeleton';
import BooleanTagsGridSkeleton from '../../tack/boolean-tag-config-grid-view-skeleton';
import MasterTagsGridSkeleton from '../../tack/master-tag-config-grid-view-skeleton';
import LanguageTagsGridSkeleton from '../../tack/language-tag-config-grid-view-skeleton';
import MockDataForEntityBaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import PortalTypeDictionary from '../../../../../../commonmodule/tack/portal-type-dictionary';
import RuleConstantDictionary from '../../tack/mock/rule-constants';
import AllowedAttributeTypesForRuleEffect from './../../tack/mock/allowed-attribute-types-for-rule-effect';
import AttributeUtils from './../../../../../../commonmodule/util/attribute-utils';
import TagUtils from '../../../../../../commonmodule/util/tag-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import CommonUtils from '../../../../../../commonmodule/util/common-utils';
import ConfigModulesDictionary from '../../tack/settinglayouttack/config-modules-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import SessionStorageManager from '../../../../../../libraries/sessionstoragemanager/session-storage-manager';
import SessionStorageConstants from '../../../../../../commonmodule/tack/session-storage-constants';
import SecondaryTypeDictionary from '../../../../../../commonmodule/tack/secondary-type-dictionary';
import OrganisationConfigStore from './organisation-config-store';
import PermissionStore from './permission-store';
import RoleStore from './role-store';
import SSOSettingStore from './sso-setting-store';
import SettingScreenStore from '../setting-screen-store';
import SessionProps from '../../../../../../commonmodule/props/session-props';
import RefactoringStore from "../../../../../../commonmodule/store/refactoring-store";
var logger = LogFactory.getLogger('setting-utils');

var SettingUtils = (function () {

  const TREE_ROOT_ID = -1;

  let _getTaxonomyHierarchyForSelectedTaxonomy = function (sTaxonomyId, sParentTaxonomyId, oCallback) {
    let fSuccess = oCallback.fSuccessHandler;
    let fFailure = _failureGetTaxonomyHierarchyForSelectedTaxonomy;
    let oRequestObj = {
      selectedTaxonomyIds: [sTaxonomyId]
    };
    let sURL = oTaxonomyRequestMapping.GetTaxonomyHierarchyForSelectedTaxonomy;
    return CS.postRequest(sURL, {}, oRequestObj, fSuccess.bind(this, sTaxonomyId, sParentTaxonomyId), fFailure);
  };

  let _failureGetTaxonomyHierarchyForSelectedTaxonomy = function (oResponse) {
    _failureCallback(oResponse,'failureGetTaxonomyHierarchyForSelectedTaxonomy',getTranslation());
  };

  var _getAppData = function () {
    return SettingScreenAppData;
  };

  var _getComponentProps = function () {
    return SettingScreenProps;
  };

  var _getVisibleSettingModules = function () {
    var oGlobalModulesData = GlobalStore.getGlobalModulesData();
    var oSettingScreen = CS.find(oGlobalModulesData.screens, {type: "setting"});

    var aActionItems = [];
    CS.forEach(oSettingScreen.entities, function (sEntity) {
      if (oSettingModules[sEntity]) {
        aActionItems.push(oSettingModules[sEntity]);
      }
    });

    //TODO: temporary until integration
    aActionItems.push(oSettingModules['kpi']);
    aActionItems.push(oSettingModules['dataGovernanceTasks']);

    return aActionItems;
  };

  var _addNewTreeNodesToValueList = function (oTreeValuesInList, aTreeNode, oCommonPropertiesToApply) {

    CS.forEach(aTreeNode, function (oTreeNode) {
      var oTreeItem = {};

      oTreeItem.id = oTreeNode.id;
      oTreeItem.label = oTreeNode.label;
      oTreeItem.isChecked = 0;
      oTreeItem.isExpanded = false;
      oTreeItem.isEditable = false;
      oTreeItem.isSelected = false;
      oTreeItem.isChildLoaded = false;
      oTreeItem.isLoading = false;
      oTreeItem.isActive = false;
      oTreeItem.isStandard = oTreeNode.isStandard;
      oTreeItem.code = oTreeNode.code;
      CS.assign(oTreeItem, oCommonPropertiesToApply);

      if (oTreeItem.id === SystemLevelIdDictionary.MarkerKlassId ||
          oTreeItem.id === SystemLevelIdDictionary.FileKlassId ||
          oTreeItem.id === SystemLevelIdDictionary.GoldenArticleKlassId) {
        oTreeItem.hideChildrenIfEmpty = true;
      }

      if (oTreeNode.type === BaseTypesDictionary.supplierKlassBaseType) {
        oTreeItem.canDelete = false;
      }

      oTreeValuesInList[oTreeNode.id] = oTreeItem;
      _addNewTreeNodesToValueList(oTreeValuesInList, oTreeNode.children, oCommonPropertiesToApply);
    });
  };

  var _removeChildNodesFromValueList = function (aNodes, oValueList) {
    CS.forEach(aNodes, function (oNode) {
      if (oNode.children) {
        _removeChildNodesFromValueList(oNode.children, oValueList);
      }

      delete oValueList[oNode.id];
    });

  };

  var _removeNodeById = function (aNodeList, sNodeId) {
    CS.forEach(aNodeList, function (oNode, iIndex) {
      if (oNode.id === sNodeId) {
        aNodeList.splice(iIndex, 1);
        return false;
      }
      if (oNode.children) {
        _removeNodeById(oNode.children, sNodeId);
      }
    });
  };

  /** Function returns a new active id to set when a node is deleted in both case Tree or single array **/
  let _getDefaultActiveNodeToSetOnTreeNodeDelete = function (aTree, sActiveNodeId, sDeletedNodeId, oParentNode) {
    let oNewActiveNode = null;
    CS.forEach(aTree, (oNode, iIndex) => {
      if (oNode.id === sDeletedNodeId) {
        /** New node should be set if deleted node is active node or active node is a children of deleted node **/
        if (sActiveNodeId === sDeletedNodeId || _getIsActiveNodeInHierarchy(oNode.children, sActiveNodeId)) {
          /** Sibling Node e.g. either Predecessor or Successor**/
          oNewActiveNode = aTree[iIndex + 1] ? aTree[iIndex + 1] : (aTree[iIndex - 1] ? aTree[iIndex - 1] : oParentNode);
          if (!oNewActiveNode) {
            /** if sibling node is not available then set parent node as active node **/
            oNewActiveNode = oParentNode;
          }
        }
        return false;
      }
      else if (CS.isNotEmpty(oNode.children)) {
        /** If a node has children then it recursively calls the function to check whether deleted node exists in
         *  it to set a new node **/
        oNewActiveNode = _getDefaultActiveNodeToSetOnTreeNodeDelete(oNode.children, sActiveNodeId, sDeletedNodeId, oNode);
        if (oNewActiveNode) {
          return false;
        }
      }
    });
    return oNewActiveNode;
  };

  /**Function checks whether active node is a children of the deleted node**/
  let _getIsActiveNodeInHierarchy = function (aTree, sActiveNodeId) {
    let bIsActiveNodeInHierarchy = false;
    CS.forEach(aTree, (oNode) => {
      if(oNode.id === sActiveNodeId) {
        bIsActiveNodeInHierarchy = true;
        return false;
      }
      else if (CS.isNotEmpty(oNode.children)) {
        bIsActiveNodeInHierarchy = _getIsActiveNodeInHierarchy(oNode.children, sActiveNodeId);
        if(bIsActiveNodeInHierarchy) {
          return false;
        }
      }
    });
    return bIsActiveNodeInHierarchy;
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

  var _applyValueToAllTreeNodes = function (oTreeValuesInList, sAttributeName, oValue) {
    CS.map(oTreeValuesInList, function (oTreeValue) {
      oTreeValue[sAttributeName] = oValue;
    });
  };

  var _setPropertyValue = function (oTreeValuesList, iProperty, oTreeItemValue) {
    oTreeValuesList[iProperty] = oTreeItemValue;
  };

  var _listOfIdsToDeleteFromTreeNodeList = function (aItemList, oItemValueList, aIdList, aCreatedIds) {
    var sNodeNamesToDelete = '';
    CS.forEach(aItemList, function (oItem) {
      if (oItem.id != TREE_ROOT_ID && (oItemValueList[oItem.id].isChecked == 2 || oItemValueList[oItem.id].isSelected == true)) {
        sNodeNamesToDelete += oItem.label + ", ";
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

  var _getListOfNamesToDeleteFromTreeNodeList = function (aItemList, oItemValueList, aIdList, aCreatedIds) {
    var aNodeNamesToDelete = [];
    CS.forEach(aItemList, function (oItem) {
      if (oItem.id != TREE_ROOT_ID && (oItemValueList[oItem.id].isChecked == 2 || oItemValueList[oItem.id].isSelected == true)) {
        let sItemLabel = CS.getLabelOrCode(oItem);
        aNodeNamesToDelete.push(sItemLabel);
        if (oItem.isCreated) {
          aCreatedIds.push(oItem.id);
        } else {
          aIdList.push(oItem.id);
        }
      } else {
        let aNodeName = _getListOfNamesToDeleteFromTreeNodeList(oItem.children, oItemValueList, aIdList, aCreatedIds);
        !CS.isEmpty(aNodeName) && aNodeNamesToDelete.push(aNodeName);
      }
    });

    return aNodeNamesToDelete;
  };

  var _reAssignCheckBoxValuesToChildren = function (oParentNode, oValueList) {

    var aChildren = oParentNode.children;
    var iCheckBoxState = oValueList[oParentNode.id].isChecked;
    var bAllChecked = true;
    var bAnyChecked = false;
    if (aChildren && aChildren.length > 0) {
      CS.forEach(aChildren, function (oChild) {
        var iChildCheckBoxState = _reAssignCheckBoxValuesToChildren(oChild, oValueList);
        bAllChecked = bAllChecked && (iChildCheckBoxState == 2);
        bAnyChecked = bAnyChecked || iChildCheckBoxState;
      });

      if (bAllChecked) {
        iCheckBoxState = 2;
      } else if (bAnyChecked) {
        iCheckBoxState = 1;
      } else {
        iCheckBoxState = 0;
      }
    }

    oValueList[oParentNode.id].isChecked = iCheckBoxState;
    return iCheckBoxState;
  };

  var _isChildrenNode = function (oNode, sChildIdToFind) {
    var bChildFound = false;
    if (oNode.id == sChildIdToFind) {
      return true;
    }
    if (!CS.isEmpty(oNode.children)) {
      CS.forEach(oNode.children, function (oChildNode) {
        bChildFound = _isChildrenNode(oChildNode, sChildIdToFind);
        if (bChildFound) {
          return false;
        }
      });
    }

    return bChildFound;
  };

  var _getNodeFromTreeByPropertyName = function(aTreeNodes, sPropertyName, sValue, sChildPropName, oParent, iLevel){

    var oRetObj = {};
    var oParentNode = oParent || {};
    var iNewLevel = iLevel + 1;

    CS.forEach(aTreeNodes, function(oTreeNode, iIndex){
      if (oTreeNode[sPropertyName] == sValue) {
        oRetObj = {
          node: oTreeNode,
          parent: oParentNode,
          level: iLevel,
          index: iIndex
        };

        return false;
      }
      if (oTreeNode[sChildPropName] && oTreeNode[sChildPropName].length > 0){
        var oRes = _getNodeFromTreeByPropertyName(oTreeNode[sChildPropName], sPropertyName, sValue, sChildPropName, oTreeNode, iNewLevel);
        if(!CS.isEmpty(oRes)){
          oRetObj = oRes;
          return false;
        }
      }
    });

    return oRetObj;
  };

  var _getSplitter = function(){
    return "#$%$#";
  };

  var _getAttributeTypeForVisual = function(sType, sAttrId) {
    return AttributeUtils.getAttributeTypeForVisual(sType, sAttrId);
  };

  var _getUserList = function () {
    return _getAppData().getUserList();
  };

  var _getUserById = function (sUserId) {
    return _getUserList()[sUserId];
  };

  var _getTagById = function (sId) {
    return CommonUtils.getNodeFromTreeListById(_getAppData().getTagList(), sId);
  };

  var _getTagList = function () {
    return _getAppData().getTagList()[0].children;
  };

  var _getTagMap = function () {
    return _getAppData().getTagMap();
  };

  var _getRoleList = function () {
    return _getAppData().getRoleList();
  } ;

  var _getRuleList = function () {
    return _getAppData().getRuleList();
  } ;

  var _getAttributeList = function () {
    return _getAppData().getAttributeList();
  };

  var _createSectionTemplate = function (sId) {
    return {
      "propertyCollectionId": sId,
      "id": null,
      "type": "com.cs.core.config.interactor.entity.propertycollection.Section"
    };
  };

  var _refreshSequenceIds = function(aSections){
    CS.each(aSections, function(oSection, iIndex){
      oSection.sequence = iIndex;
    });
  };

  var _handleSectionAdded = function (oActiveEntity, aSectionIds, oSectionMap) {
    /** If oSectionMap is undefined then it is taken from master Property collection list*/
    oSectionMap = !!oSectionMap ? oSectionMap : _getAppData().getPropertyCollectionList();
    CS.forEach(aSectionIds, function (sSectionId) {
      let oFound = CS.find(oActiveEntity.sections, function (oSection) {
        return oSection.propertyCollectionId == sSectionId});
      if (oFound) {
        return;
      }
      var oTemplate = _createSectionTemplate(sSectionId);
      var aSections = oActiveEntity.sections;
      var oLastSection = {};
      var iSequence = 0;
      if(aSections.length) {
        oLastSection = aSections[aSections.length-1];
        iSequence = oLastSection.sequence + 1;
      }
      oTemplate.sequence = iSequence;
      oTemplate.label = (oSectionMap[sSectionId] && oSectionMap[sSectionId].element) ? oSectionMap[sSectionId].element.label : "";
      oActiveEntity.sections.push(oTemplate);
    });
  };

  var _handleSectionDeleted = function (oActiveEntity, sSectionId) {
    CS.remove(oActiveEntity.sections, {id: sSectionId});
  };

  var _handleSectionMoveUp = function(oCurrentClass, sSectionId) {
    var iSectionIndex = CS.findIndex(oCurrentClass.sections, {id: sSectionId});
    var oPreviousSection = oCurrentClass.sections[iSectionIndex - 1] || {};
    if(iSectionIndex > 0 && !oPreviousSection.isInherited) {
      RefactoringStore.makeObjectDirty(oCurrentClass);
      var oDirtyEntity = oCurrentClass.clonedObject;
      var oSection = oDirtyEntity.sections.splice(iSectionIndex, 1)[0];
      oDirtyEntity.sections.splice(iSectionIndex - 1, 0, oSection);
      _refreshSequenceIds(oDirtyEntity.sections);
      return true;
    } else if (iSectionIndex == 0) {
      alertify.error(getTranslation().CANNOT_MOVE_UP_ALREADY_AT_TOP);
    } else if (oPreviousSection.isInherited) {
      alertify.error(getTranslation().CANNOT_MOVE_TO_INHERITED_SECTION);
    }
    return false;
  };

  var _handleSectionMoveDown = function(oCurrentClass, sSectionId) {
    var aSections = oCurrentClass.sections;
    var iSectionIndex = CS.findIndex(aSections, {id: sSectionId});
    var oNextSection = oCurrentClass.sections[iSectionIndex + 1] || {};
    if ((iSectionIndex < aSections.length - 1) && !oNextSection.isInherited) {
      RefactoringStore.makeObjectDirty(oCurrentClass);
      var oDirtyEntity = oCurrentClass.clonedObject;
      var oSection = oDirtyEntity.sections.splice(iSectionIndex, 1)[0];
      oDirtyEntity.sections.splice(iSectionIndex + 1, 0, oSection);
      _refreshSequenceIds(oDirtyEntity.sections);
      return true;
    } else if (iSectionIndex == aSections.length - 1) {
      alertify.error(getTranslation().CANNOT_MOVE_DOWN_ALREADY_AT_BOTTOM);
    } else if (oNextSection.isInherited) {
      alertify.error(getTranslation().CANNOT_MOVE_TO_INHERITED_SECTION);
    }
    return false;
  };

  var _handleSectionElementCheckboxToggled = function (oActiveEntity, sSectionId, sElementId, sProperty) {
    var oSection = CS.find(oActiveEntity.sections, {id: sSectionId});
    var oElement = CS.find(oSection.elements, {id: sElementId});
    oElement[sProperty] = !oElement[sProperty];

    if (oElement.type == "tag" && sProperty == "isMultiselect" && !oElement.isMultiselect) {
      oElement.defaultValue = oElement.defaultValue.length > 1 ? [oElement.defaultValue[0]] : oElement.defaultValue;
    }
    else if (sProperty == "isSkipped" && oElement.isSkipped) {
      oElement.isMandatory = false;
      oElement.isShould = false;
      oElement.isIdentifier = false;
    }
    if(oElement.type === "attribute" && sProperty === "isIdentifier"){
      oElement.defaultValue = "";
      oElement.attributeVariantContext = "";
      oElement.isMandatory = true;
      oElement.couplingType = "looselyCoupled";
      oElement.isSkipped = false;

      if(AttributeUtils.isAttributeTypeHtml(oElement.attribute.type)){
        oElement.valueAsHtml = "";
      }
    }

    if (sProperty === "isIdentifier" && oElement[sProperty]) {
      oElement.isVersionable = true;
    }
  };

  var _handleSectionElementInputChanged = function (oActiveEntity, sSectionId, sElementId, sProperty, sNewValue) {
    var oSection = CS.find(oActiveEntity.sections, {id: sSectionId});
    var oElement = CS.find(oSection.elements, {id: sElementId});
    if (sProperty == "propertyType" && oElement && oElement.type == "tag") {
        sProperty = "tagType";
    }
    if (sProperty === "mandatory") {
      if (sNewValue === "can") {
        oElement.isMandatory = false;
        oElement.isShould = false;
      }
      else if (sNewValue === "should") {
        oElement.isShould = true;
        oElement.isMandatory = false;
      }
      else if (sNewValue === "must") {
        oElement.isMandatory = true;
        oElement.isShould = false;
      }
    }
    else {
      let oAttribute = oElement.attribute;
      if(oAttribute && oAttribute.rendererType === "HTML") {
        oElement.defaultValue = sNewValue.value;
        oElement.valueAsHtml = sNewValue.valueAsHtml;
      }
      else {
        oElement[sProperty] = sNewValue;
      }
    }
  };

  var _getSelectedTagValues = function (oElement, aNewValue) {
    var oTag = oElement.tag;
    var aDefaultValues = oElement.defaultValue;
    var aChildren = oTag.children;
    var aSelectedTagValues = [];

    CS.forEach(aNewValue, function (sValue) {
      var oChildTag = CS.find(aChildren, {id: sValue});
      aSelectedTagValues.push({
        id: null,
        label: oChildTag.label,
        color: oChildTag.color,
        icon: oChildTag.icon,
        tagId: oChildTag.id
      });
    });

    CS.remove(aDefaultValues, function (oDefaultValue) {
      return !CS.includes(aNewValue, oDefaultValue.tagId);
    });

    return aSelectedTagValues;
  };

  var _handleSectionMSSValueChanged = function (oActiveEntity, sSectionId, sElementId, sProperty, aNewValue, sScreenName, oCallbackData) {
    var oSection = CS.find(oActiveEntity.sections, {id: sSectionId});
    var oElement = CS.find(oSection.elements, {id: sElementId});

    if (sProperty == "propertyType" && oElement && oElement.type == "tag") {
      sProperty = "tagType";
    }

    switch (sProperty){

      case "attributeVariantContext" :
        oElement[sProperty] = aNewValue[0];
        if (sScreenName == "class" || sScreenName == "attributionTaxonomy" || sScreenName == "taxonomy") {
          //TODO: Value hardcoded. Decare a constant
          oElement.couplingType = "looselyCoupled";
          oElement.isMandatory = false;
          oElement.isShould = false;
          oElement.isVersionable = true;
        }
        break;

      case "mandatory":
        switch (aNewValue[0]){
          case "can":
            oElement.isMandatory = false;
            oElement.isShould = false;
            break;
          case "should":
            oElement.isShould = true;
            oElement.isMandatory = false;
            break;
          case "must":
            oElement.isMandatory = true;
            oElement.isShould = false;
            break;
        }
        break;

      case "precision":
      case "couplingType":
        oElement[sProperty] = aNewValue[0];
        break;

      case "tagType":
        //Default value is not allowed for tags except yes neutral
        oElement[sProperty] === TagTypeConstants.YES_NEUTRAL_TAG_TYPE && (oElement.defaultValue = []);
        oElement[sProperty] = aNewValue[0];
        break;

      case "selectedTagValues":
        oElement[sProperty] = _getSelectedTagValues(oElement, aNewValue);
        if(oCallbackData && oCallbackData.functionToExecute) {
          oCallbackData.functionToExecute();
        }
        break;

      default:
        oElement[sProperty] = aNewValue;
        break;
    }
  };

  var _handleVisualElementBlockerClicked = function (oActiveClass, oInfo, fTriggerChange) {
    var oSection = CS.find(oActiveClass.sections, {'id': oInfo.sectionId});
    CustomActionDialogStore.showConfirmDialog(getTranslation().BREAK_INHERITANCE_CONFIRMATION, "",
        function () {
          var oElement = CS.find(oSection.elements, {id: oInfo.elementId});
          if(oElement){
            oElement.isInheritedUI = false;
            oElement.isCutoffUI = true;
          }
          fTriggerChange();
        }, function (oEvent) {
        });
  };

  var _getNewTagValue = function (sTagId, iRelevance) {
    const iDefaultRelevance = 0;
    return  {
      id: UniqueIdentifierGenerator.generateUUID(),
      tagId: sTagId,
      relevance: iRelevance || iDefaultRelevance,
      timestamp: new Date().getTime()
    };
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

  var _addTagsInContentBasedOnTagType = function (sTagId, oTagGroup) {
    var oMasterTag = _getTagById(sTagId);
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

  var _addTagInEntity = function (sTagId, oEntity) {
    var aTags = oEntity.tags;
    var oContentTagGroup = CS.find(aTags, {tagId: sTagId});
    if(CS.isEmpty(oContentTagGroup)) {
      oContentTagGroup = {};
      oContentTagGroup.id = UniqueIdentifierGenerator.generateUUID();
      oContentTagGroup.tagValues = [];
      oContentTagGroup.tagId = sTagId;
      oContentTagGroup.baseType = BaseTypesDictionary.tagInstanceBaseType;

      aTags.push(oContentTagGroup);

      _addTagsInContentBasedOnTagType(sTagId, oContentTagGroup);
    }
  };

  var _getNatureTypeListBasedOnClassType = function (sType, sSecondaryType) {
    switch (sType){
      case MockDataForEntityBaseTypesDictionary.articleKlassBaseType : //Article
        return new ClassNatureTypes.articleNatureTypes();

      case MockDataForEntityBaseTypesDictionary.assetKlassBaseType :// Asset
        return _getNatureTypeAssetListBasedOnSecondaryClassType(sSecondaryType);

      case MockDataForEntityBaseTypesDictionary.marketKlassBaseType : //Market
        return new ClassNatureTypes.marketNatureTypes();

      case MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType : //TextAsset
        return new ClassNatureTypes.textAssetNatureTypes();

      case MockDataForEntityBaseTypesDictionary.supplierKlassBaseType : //Supplier
        return new ClassNatureTypes.supplierNatureTypes();

      default :
          return [];
    }
  };

  let _getNatureTypeAssetListBasedOnSecondaryClassType = function (sType) {
    switch (sType) {
      case SecondaryTypeDictionary.IMAGE_ASSET:
        return new ClassNatureTypes.imageNatureTypes();
      case SecondaryTypeDictionary.VIDEO_ASSET:
        return new ClassNatureTypes.multimediaNatureTypes();
      case SecondaryTypeDictionary.DOCUMENT_ASSET:
        return new ClassNatureTypes.documentNatureTypes();
      default:
        return new ClassNatureTypes.assetNatureTypes();
    }
  };

  var _getApplicationType = function () {
    var oGlobalModulesData = GlobalStore.getGlobalModulesData();
    return oGlobalModulesData.type;
  };

  var _isOnboarding = function () {
    return _getApplicationType() == PortalTypeDictionary.ONBOARDING;
  };

  var _isOffboarding = function () {
    return _getApplicationType() == PortalTypeDictionary.OFFBOARDING;
  };

  var _deleteTagValueFromCombination = function (oContext, sSelectorId, sTagId, sId) {
    var aUniqueSelectors = oContext.uniqueSelectors;
    CS.forEach(aUniqueSelectors, function (oUnitSelector) {
      if (oUnitSelector.id == sSelectorId) {
        CS.forEach(oUnitSelector.selectionValues, function (oTag) {
          if (oTag.tagId == sTagId) {
            CS.remove(oTag.tagValues, function ({tagValueId: sTagValueId}) {
              return sTagValueId == sId;
            });
          }
        });
      }
    });
  };

  var _updateUniqueSelectionOrder = function (oActiveContext, sUniqueSelectorId) {
    var aTagsWithValues = [];
    var aTagsWithoutValues = [];
    var oUniqueSelector = CS.find(oActiveContext.uniqueSelectors, {id: sUniqueSelectorId});

    if (!CS.isEmpty(oUniqueSelector)) {
      CS.forEach(oUniqueSelector.selectionValues, function (oSelection) {
        if (oSelection.tagValues.length > 0) {
          aTagsWithValues.push(oSelection);
        }
        else {
          aTagsWithoutValues.push(oSelection);
        }
      });
      oUniqueSelector.selectionValues = aTagsWithValues.concat(aTagsWithoutValues);
    }
  };

  var _setTagValueDataInTagCombination = function (oContext, sSelectorId, sTagId, aSelectedTags) {
    var aUniqueSelectors = oContext.uniqueSelectors;
    var aSelectedTagMap = [];
    CS.forEach(aSelectedTags, function (aSelectedTagId) {
      aSelectedTagMap.push({tagValueId: aSelectedTagId});
    });
    CS.forEach(aUniqueSelectors, function (oUnitSelector) {
      if (oUnitSelector.id == sSelectorId) {
        CS.forEach(oUnitSelector.selectionValues, function (oTag) {
          if (oTag.tagId == sTagId) {
            oTag.tagValues = [];
            oTag.tagValues = aSelectedTagMap;
          }
        });
      }
    });
  };

  var _removeEmptyTagSelections = function (oActiveContext) {
    CS.forEach(oActiveContext.uniqueSelectors, function (oUniqueSelector, iUIndex) {
      var aIsEmpty = [];
      CS.forEach(oUniqueSelector.selectionValues, function (oTag) {
        aIsEmpty.push(!CS.isEmpty(oTag.tagValues));
      });
      if ((CS.find(aIsEmpty, function (bIsEmptyValues) {
            return bIsEmptyValues == true
          })) == undefined) {
        oActiveContext.uniqueSelectors.splice(iUIndex, 1);
      }
    });
  };

  var _deleteTagCombination = function (sUniqueSelectionId, oContext) {
    CS.forEach(oContext.uniqueSelectors, function (oSelection, iIndex) {
      if (oSelection && oSelection.id == sUniqueSelectionId) {
        oContext.uniqueSelectors.splice(iIndex, 1);
        return;
      }
    });
  };

  var _addNewTagCombination = function (oActiveContext) {
    _removeEmptyTagSelections(oActiveContext);
    var oNewUniqueSelector = {};
    var oSelection = {};
    var sNewUniqueSelectorId = "";
    oNewUniqueSelector.id = UniqueIdentifierGenerator.generateUUID();
    oNewUniqueSelector.selectionValues = [];
    if (CS.isEmpty(oActiveContext.uniqueSelectors)) {
      oActiveContext.uniqueSelectors = [];
    }
    var aContextTags = oActiveContext.contextTags;
    if (!CS.isEmpty(aContextTags)) {
      CS.forEach(aContextTags, function (oTag) {
        var bTagValueSelection = false;
        CS.forEach(oTag.tagValues, function (oTagValue) {
          if (oTagValue.isSelected) {
            bTagValueSelection = true;
            return;
          }
        });
        if (bTagValueSelection) {
          oSelection = {
            tagId: oTag.tagId,
            tagValues: [],
          };
          oNewUniqueSelector.selectionValues.push(oSelection);
        }
      });
      if (!CS.isEmpty(oNewUniqueSelector.selectionValues)) {
        oActiveContext.uniqueSelectors.push(oNewUniqueSelector);
      } else {
        alertify.message(getTranslation().SELECT_AT_LEAST_ONE_TAG_VALUE);
      }
      sNewUniqueSelectorId = oNewUniqueSelector.id;
    }
    return sNewUniqueSelectorId;
  };

  var _isUniqueSelectorModified = function (oOldUniqueTagSelection,oNewUniqueTagSelection) {
    let aNewSelectionValues = oNewUniqueTagSelection.selectionValues;
    let aOldSelectionValues = oOldUniqueTagSelection.selectionValues;
    let bIsModified = false;
    CS.forEach(aNewSelectionValues, function (oTag) {
      let oOldTag = CS.find(aOldSelectionValues, {tagId: oTag.tagId});
      if (oOldTag) {
        let aXorCheck = CS.xorBy(oTag.tagValues, oOldTag.tagValues, 'tagValueId');
        if (!CS.isEmpty(aXorCheck)) {
          bIsModified = true;
          return false;
        }
      } else {
        bIsModified = true;
        return false;
      }
    });

    if (!bIsModified) {
      CS.forEach(aOldSelectionValues, function (oTag) {
        let oNewTag = CS.find(aNewSelectionValues, {tagId: oTag.tagId});
        if (CS.isEmpty(oNewTag)) {
          bIsModified = true;
          return false;
        }
      });
    }

    return bIsModified;
  };

  var _checkUniqueSelectorsDuplications = function (oContext) {
    var aUniqueSelectors = oContext.uniqueSelectors;
    var aUniqueSelectorsTagValuesStrings = [];

    //TODO: Think optimized logic - Tauseef
    CS.forEach(aUniqueSelectors, function (oSelector) {
      let aSelectedTagValues = [];
      CS.forEach(oSelector.selectionValues, function (oSelectionValue) {
        let oSelectedTagValues = oSelectionValue.tagValues;
        oSelectedTagValues = CS.sortBy(oSelectedTagValues, "tagValueId");
        aSelectedTagValues.push.apply(aSelectedTagValues, CS.map(oSelectedTagValues, 'tagValueId'));
      });
      aUniqueSelectorsTagValuesStrings.push(JSON.stringify(aSelectedTagValues));
    });
    var aFilteredUniqueSelectors = CS.uniq(aUniqueSelectorsTagValuesStrings);
    if (aUniqueSelectorsTagValuesStrings.length !== aFilteredUniqueSelectors.length) {
      return true;
    }
    else {
      return false;
    }
  };

  var _removeEmptyTagGroupFromCurrentContext = function (oCurrentContext) {
    CS.remove(oCurrentContext.contextTags, function (oContextTag) {
      if ((oCurrentContext.type === ContextTypeDictionary.IMAGE_VARIANT) && (oContextTag.tagId === "resolutiontag")) {
        return false;
      }
      return CS.isEmpty(CS.filter(oContextTag.tagValues, {isSelected: true}));
    });
  };

  var _setInheritanceAndCutOffUIProperties = function (aSections) {
    CS.forEach(aSections, function (oSection) {
      oSection.isInheritedUI = oSection.isInherited;
      oSection.isCutoffUI = oSection.isCutoff;
      oSection.isCollapsedUI = true;
      //todo temp impl remove after section expansion state is maintained properly
      _setInheritanceAndCutOffPropertiesForSectionElements(oSection);
    });
  };

  let _setInheritanceAndCutOffPropertiesForSectionElements = function (oSection) {
    CS.forEach(oSection.elements, function (oElement) {
      oElement.isInheritedUI = oElement.isInherited;
      oElement.isCutoffUI = oElement.isCutoff;
    });
  };

  var _updateConfigSectionTagValueRelevance = function (aSections, sSectionId, sElementId, aTagValueRelevanceData) {
    let oSection = CS.find(aSections, {id: sSectionId});
    let oElement = CS.find(oSection.elements, {id: sElementId});
    let aDefaultValue = [];

    CS.forEach(aTagValueRelevanceData, function (oData) {
      let oTagValue = CS.find(oElement.defaultValue, {tagId: oData.tagId}) || {tagId: oData.tagId};
      TagUtils.updateTagValueRelevanceOrRange(oTagValue, oData, false);
      aDefaultValue.push(oTagValue);
    });

    oElement.defaultValue = aDefaultValue;
  };

  let isAttributeToAttributeMappingValidInCause = function (oCurrentRule) {
    var aRuleList = CS.filter(oCurrentRule.attributes, function (oAttr) {
      var aRule = CS.filter(oAttr.rules, {attributeLinkId: "0"});
      return aRule.length;
    });
    return CS.isEmpty(aRuleList);
  };

  let _checkWhetherUsersInRolesAreEmpty = function (oRoles) {
    var bIsRoleEmpty = false;
    CS.forEach(oRoles, function (oRole) {
      CS.forEach(oRole.rules, function (oRule) {
        if ((oRule.type == "exact" || oRule.type == "contains") && oRule.values.length == 0) {
          bIsRoleEmpty = true;
          return false;
        }
      });
      if (bIsRoleEmpty)
        return false;
    });
    return bIsRoleEmpty;
  };

  var _checkFormulaValidityForCalculatedAttribute = function (aNormalizations) {
    var bIsFormulaInvalid = false;
    CS.forEach(aNormalizations, function (oNormalization) {
      var aAttributeOperatorList = oNormalization.attributeOperatorList;
      if (CS.isEmpty(aAttributeOperatorList)) {
        return;
      }
      var oExpression = _getExpression(aAttributeOperatorList);
      if (!oExpression.validExpression) {
        bIsFormulaInvalid = true;
        return false;
      }
      var sExpression = oExpression.expression;
      try {
        var iResult = eval(sExpression);
        var bIsResultValid = !CS.isNaN(iResult) && CS.isNumber(iResult);
        if (!bIsResultValid) {
          bIsFormulaInvalid = true;
          return false;
        }

      } catch (err) {
        bIsFormulaInvalid = true;
        return false;
      }
    });
    return bIsFormulaInvalid;
  };

  let _checkWetherMeasurementFieldIsEmpty = function(oCurrentRule, oAttributeList){
    var bIsAnyMeasurementInputEmpty = false;
    var aRuleAttributes = oCurrentRule.attributes;
    var oConfigDetails = oCurrentRule.configDetails || {};
    var iRuleAttributeLength = aRuleAttributes.length;
    var sAttributeType = "";
    var sTypeForVisual = "";
    var oLoadedAttributesData = SettingUtils.getLoadedAttributesData();
    for(var iRuleCount = 0 ; iRuleCount < iRuleAttributeLength ; iRuleCount++){
      var oRuleAttribute = aRuleAttributes[iRuleCount];
      var sEntityId = oRuleAttribute.entityId;
      sAttributeType = (oAttributeList[sEntityId] && oAttributeList[sEntityId].type) ||
          (oLoadedAttributesData[sEntityId] && oLoadedAttributesData[sEntityId].type) ||
          (oConfigDetails.referencedAttributes && oConfigDetails.referencedAttributes[sEntityId] &&
              oConfigDetails.referencedAttributes[sEntityId].type);
      sTypeForVisual = _getAttributeTypeForVisual(sAttributeType);
      if(sTypeForVisual == "number" || sTypeForVisual == "date" || sTypeForVisual == "measurementMetrics"){
        var aRules = aRuleAttributes[iRuleCount].rules;
        var iRuleSize = aRules.length;
        for(var iCount = 0 ; iCount < iRuleSize ; iCount++){
          var oRule = aRules[iCount];
          switch(oRule.type){
            case "exact" :
            case "contains" :
            case "lt" :
            case "gt" :
            case "start" :
            case "end" :
              if(oRule.values.length == 0 || oRule.values[0] == ""){
                if(oRule.attributeLinkId == "") {
                  bIsAnyMeasurementInputEmpty = true;
                  return bIsAnyMeasurementInputEmpty;
                }
              }
              break;
            case "range" :
              var sTo = oRule.to + "";
              var sFrom = oRule.from + "";
              if(sTypeForVisual == "date"){
                if (sTo == "0" || sFrom == "0") {
                  bIsAnyMeasurementInputEmpty = true;
                  return bIsAnyMeasurementInputEmpty;
                }
              }
              if (sTo == "" || sFrom == "") {
                bIsAnyMeasurementInputEmpty = true;
                return bIsAnyMeasurementInputEmpty;
              }
              break;
            default : break;
          }
        }
      }
    }
    return bIsAnyMeasurementInputEmpty;
  };

  var _validateTagsInCause = function (oActiveRule) {
    var bFlag = true;
    let aTags = oActiveRule.tags;
    var oConfigDetails = oActiveRule.configDetails || {};
    var oReferencedTags = oConfigDetails.referencedTags || {};
    var oLoadedTags = SettingScreenProps.screen.getLoadedTagsData();
    CS.forEach(aTags, function (oTag) {
      var oMasterTag = oReferencedTags[oTag.entityId] || oLoadedTags[oTag.entityId];
      var sTagType = oMasterTag.tagType;
      if(sTagType == TagTypeConstants.YES_NEUTRAL_TAG_TYPE || sTagType == TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE || sTagType == TagTypeConstants.STATUS_TAG_TYPE) {
        var aRules = oTag.rules;
        CS.forEach(aRules, function (oRule) {
          var sType = oRule.type;
          var aTagValues = oRule.tagValues;
          var aTypeToExclude = [RuleConstantDictionary.TAG.TYPE.EMPTY, RuleConstantDictionary.TAG.TYPE.NOTEMPTY];
          if(!CS.includes(aTypeToExclude,sType)) {
            bFlag = false;
            CS.forEach(aTagValues, function (oTagValue) {
              if((oTagValue.from == oTagValue.to) && oTagValue.from != 0) {
                bFlag = true;
              }
            });
          }
          if(!bFlag) {
            return false;
          }
        });
      }
      if(!bFlag) {
        return false;
      }
    });
    return bFlag;
  };

  let _validateEntity = function (oEntity) {
    var oAppData = SettingUtils.getAppData();
    var oMapRuleList = oAppData.getRuleList();
    var aEntityList = [];
    CS.forEach(oMapRuleList, function (oMasterRule) {
      aEntityList.push(oMasterRule);
    });

    var sEntityName = oEntity.label;

    var bNameValidation = sEntityName.trim() != "";
    var bDuplicateNameValidation = true;

    var sContentId = oEntity.id;
    var aEntityWithSameName = CS.filter(aEntityList, {'label': oEntity.label});

    CS.forEach(aEntityWithSameName, function (oContentWithSameName) {
      if (oContentWithSameName.id != sContentId) {
        bDuplicateNameValidation = false;
      }
    });

    return {
      nameValidation: bNameValidation,
      duplicateNameValidation: bDuplicateNameValidation
    };
  };

  var _validateEntities = function (aEntity) {

    var oRes = {
      entityWithBlankNames: [],
      entityWithDuplicateNames: [],
      nameValidation: true,
      duplicateNameValidation: true,
      isTagValid: true
    };

    CS.forEach(aEntity, function (oEntity) {
      var sContentId = oEntity.id;
      var oValidation = oEntity.clonedObject ? _validateEntity(oEntity.clonedObject) : _validateEntity(oEntity);
      oRes.nameValidation = oRes.nameValidation && oValidation.nameValidation;
      oRes.duplicateNameValidation = oRes.duplicateNameValidation && oValidation.duplicateNameValidation;

      if (!oValidation.nameValidation) {
        oRes.entityWithBlankNames.push(sContentId);
      }
      oRes.isTagValid = _validateTagsInCause(oEntity);
    });

    return oRes;
  };

  let _validateRule = function (oCurrentRule) {
    if(!isAttributeToAttributeMappingValidInCause(oCurrentRule)) {
      alertify.error(getTranslation().SELECT_ATTRIBUTE_TO_LINK, 0);
      return false;
    }
    var oEntityValidation = _validateEntities([oCurrentRule]);
    if(!oEntityValidation.isTagValid) {
      alertify.error(getTranslation().TAG_CANNOT_BE_EMPTY);
      return false;
    }

    if (!oEntityValidation.nameValidation) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return false;
    } else if (!oEntityValidation.duplicateNameValidation) {
      logger.error('_saveRuleList: Content with the same name exists', {});
      alertify.error(getTranslation().STORE_ALERT_CONTENT_SAME_NAME, 0);
      return false;
    }

    if(_checkWetherMeasurementFieldIsEmpty(oCurrentRule, SettingUtils.getAppData().getAttributeList()) == true){
      alertify.error(getTranslation().FROM_AND_TO_VALUE_IS_EMPTY);
      return false;
    }

    if ( _checkWhetherUsersInRolesAreEmpty(oCurrentRule.roles)) {
      alertify.error(getTranslation().ROLE_USER_FIELD_EMPTY);
      return false;
    }
    if (!CS.isEmpty(oCurrentRule.normalizations)) {
      if (_checkFormulaValidityForCalculatedAttribute(oCurrentRule.normalizations)) {
        alertify.error(getTranslation().ERROR_INVALID_EXPRESSION);
        return false;
      }
    }
    return true;
  };

  let _preProcessContextData = function (oContextData) {
    let oReferencedTags = oContextData.referencedTags || oContextData.configDetails.referencedTags;
    let aContextTagsData = [];

    CS.forEach(oContextData.contextTags, function (oContextTag) {
      let oReferencedTag = oReferencedTags[oContextTag.id];
      let aTagValueData = [];

      CS.forEach(oReferencedTag.children, function (oReferencedTagValue) {
        aTagValueData.push({
          tagValueId: oReferencedTagValue.id,
          label: oReferencedTagValue.label,
          code: oReferencedTagValue.code,
          isSelected: CS.includes(oContextTag.tagValueIds, oReferencedTagValue.id)
        })
      });

      aContextTagsData.push({
        tagId: oReferencedTag.id,
        label: oReferencedTag.label,
        code: oReferencedTag.code,
        isMultiselect: oReferencedTag.isMultiselect,
        tagValues: aTagValueData,
        tagType: oReferencedTag.tagType
      });
    });

    oContextData.contextTags = aContextTagsData;
    delete oContextData.referencedTags;
  };

  let _getDefaultListProps = function () {
    return {
      isSelected: false,
      isChecked: false,
      isEditable: false
    }
  };

  let _getOrganisationStore = function () {
    return OrganisationConfigStore;
  };

  let _getPermissionStore = function () {
    return PermissionStore;
  };

  let _getRoleStore = function () {
    return RoleStore;
  };

  let _getSSOSettingStore = function () {
    return SSOSettingStore;
  };

  let _getAllowedPhysicalCatalogs = function () {
    return GlobalStore.getAllowedPhysicalCatalogs();
  };

  var _isValidEntityCode = function (sCode) {
    let sContext = _getConfigScreenViewName();
    let sRegEx = "";

    switch(sContext) {
      case ConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY:
        sRegEx = new RegExp('^[a-zA-Z0-9_]*$');
        break;

      default:
        sRegEx = new RegExp('^[a-zA-Z0-9_-]*$');
    }

    return sRegEx.test(sCode);
  };

  var _checkForUniquenessOfCodePropertyAndSendAjaxCall = function (oRequestData, sURL, oCallbackData) {
    //Exceptional handling for using ajaxCall in utils.

    if (CS.isEmpty(oRequestData.id)) {
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      return;
    }
    if (_isValidEntityCode(oRequestData.id)) {
      SettingUtils.csPostRequest(sURL,{}, oRequestData, successCheckEntityCodeCallback.bind(this, oCallbackData), failureCheckEntityCodeCallback);
    }
    else {
      alertify.error(getTranslation().PLEASE_ENTER_VALID_CODE);
      return;
    }
  };

  var successCheckEntityCodeCallback = function(oCallbackData) {
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  var failureCheckEntityCodeCallback = function (oResponse) {
    _failureCallback(oResponse, "failureCheckEntityCodeCallback", GlobalStore.getTranslations('SettingScreenTranslations'));
  };

  let _removeOrganisationsFromProps = function (aIds, oResponse) {
    let oOrganisationConfigViewProps = SettingScreenProps.organisationConfigView;
    let oActiveOrganization = oOrganisationConfigViewProps.getActiveOrganisation();
    let aOrganisationConfigMap = oOrganisationConfigViewProps.getOrganisationConfigMap();
    let oOrganisationConfigValueMap = oOrganisationConfigViewProps.getOrganisationConfigValueMap();
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnListNodeDelete(aOrganisationConfigMap, oOrganisationConfigValueMap, oActiveOrganization.id, oResponse.success);

    oOrganisationConfigViewProps.setOrganisationConfigScreenLockStatus(false);
    CS.isNotEmpty(oNewActiveNode) && _getOrganisationStore().fetchOrganisation(oNewActiveNode.id);
  };

  let _getSelectedTreeItemById = function (sSelectedItemId, sParentId) {
    //To selected Tab/Module
    let aConfigModulesList = new ConfigModulesList();
    let sSelectedTabId = SettingScreenProps.screen.getSelectedTabId();
    let oSelectedConfigModule = CS.find(aConfigModulesList, {id: sSelectedTabId});

    //selected entities list from left navigation tree
    let oModuleData = _getSelectedEntityData(oSelectedConfigModule.children, sSelectedItemId) || {};
    return oModuleData
  };

  let _getSelectedEntityData = (aTree, sTreeNodeId) => (
      aTree.reduce((oData, oTreeData) => {
        if (oData)
          return oData;
        if (oTreeData.id === sTreeNodeId)
          return oTreeData;
        if (oTreeData.children)
          return _getSelectedEntityData(oTreeData.children, sTreeNodeId)
      }, null)
  );

  let _getGridSkeletonForAttribute = function () {
    let oScreenProps = SettingScreenProps.screen;
    let sSelectedTreeItem = oScreenProps.getSelectedLeftNavigationTreeItem();

    switch (sSelectedTreeItem) {
      case ConfigPropertyTypeDictionary.STANDARD:
        return new StandardAttributeGridSkeleton();

      case ConfigPropertyTypeDictionary.HTML:
        return new HtmlAttributeGridSkeleton();

      case ConfigPropertyTypeDictionary.TEXT:
        return new TextAttributeGridSkeleton();

      case ConfigPropertyTypeDictionary.NUMBER:
        return new NumberAttributeGridSkeleton();

      case ConfigPropertyTypeDictionary.MEASUREMENT:
        return new MeasurementAttributeGridSkeleton();

      case ConfigPropertyTypeDictionary.CONCATENATED:
        return new ConcatenatedAttributeGridSkeleton();

      case ConfigPropertyTypeDictionary.CALCULATED:
        return new CalculatedAttributeGridSkeleton();

      case ConfigPropertyTypeDictionary.DATE:
        return new DateAttributeGridSkeleton();

      case ConfigPropertyTypeDictionary.PRICE:
        return new PriceAttributeGridSkeleton();

    }
  };

  let _getGridSkeletonForTag = function () {
    let oScreenProps = SettingScreenProps.screen;
    let sSelectedTreeItem = oScreenProps.getSelectedLeftNavigationTreeItem();

    switch (sSelectedTreeItem) {
      case ConfigPropertyTypeDictionary.LOV:
        return new LOVTagsGridSkeleton();

      case ConfigPropertyTypeDictionary.STATUS:
        return new StatusTagsGridSkeleton();

      case ConfigPropertyTypeDictionary.BOOLEAN:
        return new BooleanTagsGridSkeleton();

      case ConfigPropertyTypeDictionary.MASTER:
        return new MasterTagsGridSkeleton();

      case ConfigPropertyTypeDictionary.LANGUAGE:
        return new LanguageTagsGridSkeleton();
    }
  };

  var _handleListViewSearchOrLoadMoreClicked = function (sSearchText, bLoadMore, oEntityList, oScreenSpecificProp, fFunctionToExecute) {
    let iFromCountToSet = 0;
    let sOldSearchText = oScreenSpecificProp.getSearchText();

    let bIsSearchTextEqual = sOldSearchText.toLowerCase() === sSearchText.toLowerCase();
    if(bIsSearchTextEqual && !bLoadMore){
      return;
    }

    //if searchText is equal from prev one then do not reset 'from' data
    if (bIsSearchTextEqual) {
      let aToCheckLength = CS.isArray(oEntityList) ? oEntityList : CS.keys(oEntityList);
      iFromCountToSet = aToCheckLength.length;
    }

    if (!bLoadMore) {
      oScreenSpecificProp.setSearchText(sSearchText);
    }
    oScreenSpecificProp.setFrom(iFromCountToSet);

    fFunctionToExecute(bLoadMore);
  };

  var _getEntityListViewLoadMorePostData = function (oScreenSpecificProp, bLoadMore) {
    let sSearchText = oScreenSpecificProp.getSearchText();
    let iFrom = oScreenSpecificProp.getFrom();
    if(!bLoadMore){
      iFrom = 0;
    }
    let oPostConstantData = oScreenSpecificProp.getSearchConstantData();
    return {
      searchText: sSearchText,
      from: iFrom,
      size: oPostConstantData.size,
      searchColumn: oPostConstantData.searchColumn,
      sortOrder: oPostConstantData.sortOrder,
      sortBy: oPostConstantData.sortBy,
    };
  };

  let _generateADMForCustomTabs = function (oOldCustomTab, oNewCustomTab) {
    let oAddedCustomTab = {};
    let sRemoveCustomTab = "";
    if (!CS.isEqual(oOldCustomTab, oNewCustomTab)) {
      oAddedCustomTab = oNewCustomTab;
      delete oAddedCustomTab.type;
      if (!CS.isEmpty(oOldCustomTab)) {
        sRemoveCustomTab = oOldCustomTab.id;
      }
    }

    if(CS.isEmpty(oAddedCustomTab)){
      return {
        deletedTab: sRemoveCustomTab
      }
    }
    return {
      addedTab: oAddedCustomTab,
      deletedTab: sRemoveCustomTab
  }
  };

  let _checkCalculatedExpressionManualCases = function (sPrevType, sNewType) {
    var bValidExpression = true;

    var aSignOperators = ["ADD", "SUBTRACT", "MULTIPLY", "DIVIDE"];

    if (sPrevType != "") {
      if ((sPrevType == "ATTRIBUTE" && sNewType == "ATTRIBUTE") || (sPrevType == "VALUE" && sNewType == "VALUE")) {
        bValidExpression = false;
      }

      if (
          (sPrevType != "ATTRIBUTE" && sPrevType != "VALUE" && sPrevType != "OPENING_BRACKET" && sPrevType != "CLOSING_BRACKET") &&
          (sNewType != "ATTRIBUTE" && sNewType != "VALUE" && sNewType != "OPENING_BRACKET" && sNewType != "CLOSING_BRACKET")) {
        bValidExpression = false;
      }

      if (CS.includes(aSignOperators, sPrevType) && CS.includes(aSignOperators, sNewType)) {
        bValidExpression = false;
      }
    } else {
      if (CS.includes(aSignOperators, sNewType)) {
        bValidExpression = false;
      }
    }
    return bValidExpression;
  };

  let _getExpression = function (aAttributeOperatorList) {
    var _this = this;
    var sExpression = "";
    var sPrevType = "";
    var bValidExpression = true;
    CS.forEach(aAttributeOperatorList, function (oAttributeOperator, iIndex) {
      if (iIndex > 0) {
        sPrevType = aAttributeOperatorList[iIndex - 1].type;
      }
      var sNewType = oAttributeOperator.type;
      bValidExpression = _checkCalculatedExpressionManualCases(sPrevType, sNewType);
      if (!bValidExpression) {
        return false;
      }

      switch (sNewType) {
        case "ADD":
          sExpression = sExpression + "+";
          break;
        case "SUBTRACT":
          sExpression = sExpression + "-";
          break;
        case "MULTIPLY":
          sExpression = sExpression + "*";
          break;
        case "DIVIDE":
          sExpression = sExpression + "/";
          break;
        case "OPENING_BRACKET":
          sExpression = sExpression + "(";
          break;
        case "CLOSING_BRACKET":
          sExpression = sExpression + ")";
          break;
        case "ATTRIBUTE":
          if (oAttributeOperator.attributeId == null) {
            sExpression = "";
            return false;
          }
          sExpression = sExpression + "(2)";
          break;
        case "VALUE":
          if (oAttributeOperator.value == null) {
            sExpression = "";
            return false;
          }
          sExpression = sExpression + "(" + oAttributeOperator.value + ")";
          break;
      }

      sPrevType = oAttributeOperator.type

    });

    return {
      expression: sExpression,
      validExpression: bValidExpression
    }
  };

  let _checkFormulaValidity = function (aAttributeOperatorList) {
    if (CS.isEmpty(aAttributeOperatorList)) {
      return true;
    }
    var oExpression = _getExpression(aAttributeOperatorList);
    if (!oExpression.validExpression) {
      return false;
    }
    var sExpression = oExpression.expression;
    try {
      var iResult = eval(sExpression);
      return !CS.isNaN(iResult) && CS.isNumber(iResult);

    } catch (err) {
      return false;
    }
  };

  let _getConfigScreenViewName = function () {
    var sActiveScreen = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();

    let oTempClassCategoryConstants = CS.invert(ClassCategoryConstants);
    if(!CS.isEmpty(oTempClassCategoryConstants[sActiveScreen])){
      sActiveScreen = ConfigEntityTypeDictionary.ENTITY_TYPE_CLASS;
    }

    return sActiveScreen;
  };

  let _getLazyMSSViewModel = function (aSelectedItems, oReferencedData, sContextKey, oReqResObj, bIsMultiselect, aExcludedItems, bIsDisabled, bShowIcon) {
    let bIsMultiSelectTemp = bIsMultiselect == undefined || bIsMultiselect == null ? true : bIsMultiselect;
    let aExcludedItemsTemp = CS.isEmpty(aExcludedItems) ? [] : aExcludedItems;
    return {
      disabled: bIsDisabled,
      bShowIcon: bShowIcon,
      label: "",
      selectedItems: aSelectedItems,
      context: sContextKey,
      disableCross: false,
      hideTooltip: true,
      isMultiSelect: bIsMultiSelectTemp,
      referencedData: oReferencedData,
      requestResponseInfo: oReqResObj,
      excludedItems: aExcludedItemsTemp
    }
  };

  let _isGridViewScreen = function (sScreenName) {
    let bIsTranslationScreen = sScreenName.startsWith(ConfigModulesDictionary.TRANSLATIONS);

    let aGridEntityScreens = [ConfigEntityTypeDictionary.ENTITY_TYPE_TAG,
                              ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTE,
                              ConfigEntityTypeDictionary.ENTITY_TYPE_TABS,
                              ConfigEntityTypeDictionary.ENTITY_TYPE_STATICTRANSLATION,
                              ConfigEntityTypeDictionary.ENTITY_TYPE_USER,
                              ConfigEntityTypeDictionary.ENTITY_TYPE_TASK,
                              ConfigEntityTypeDictionary.ENTITY_TYPE_KPI,
                              ConfigEntityTypeDictionary.ENTITY_TYPE_PROFILE];

    let aAttributeGrouping = [ConfigPropertyTypeDictionary.HTML,
                              ConfigPropertyTypeDictionary.TEXT,
                              ConfigPropertyTypeDictionary.NUMBER,
                              ConfigPropertyTypeDictionary.DATE,
                              ConfigPropertyTypeDictionary.CALCULATED,
                              ConfigPropertyTypeDictionary.CONCATENATED,
                              ConfigPropertyTypeDictionary.PRICE,
                              ConfigPropertyTypeDictionary.MEASUREMENT,
                              ConfigPropertyTypeDictionary.STANDARD];

    let aTagGrouping = [ConfigPropertyTypeDictionary.MASTER,
                        ConfigPropertyTypeDictionary.LOV,
                        ConfigPropertyTypeDictionary.STATUS,
                        ConfigPropertyTypeDictionary.BOOLEAN];
    return (
        bIsTranslationScreen ||
        CS.includes(aGridEntityScreens, sScreenName) ||
        CS.includes(aAttributeGrouping, sScreenName) ||
        CS.includes(aTagGrouping, sScreenName)
    );
  };

  var _isNatureTypeRelationship = function (sRelType) {
    var aTypesToCheck = [
      //variant relationships
      RelationshipTypeDictionary.PRODUCT_VARIANT,
      RelationshipTypeDictionary.PROMOTIONAL_VERSION,

      //nature relationships
      RelationshipTypeDictionary.FIXED_BUNDLE,
      RelationshipTypeDictionary.SET_OF_PRODUCTS,

      //nature relationships for virtual catalog
      RelationshipTypeDictionary.PRODUCT_NATURE,
      RelationshipTypeDictionary.MEDIA_ASSET,

    ];

    return CS.includes(aTypesToCheck, sRelType);
  };

  let _handleSectionToggleButtonClicked = function (sSectionId, oEntity, sURLToFetchSections, oCallBackData) {
    let oCurrentEntity = oEntity.clonedObject || oEntity;
    let oCurrentSection = CS.find(oCurrentEntity.sections, {id: sSectionId});
    let oSection = CS.find(oEntity.sections, {id: sSectionId});

    let bExpansionStateToSet = !oSection.isCollapsedUI;
    oSection.isCollapsedUI = bExpansionStateToSet;
    if (oEntity.clonedObject) {
      let oClonedSection = CS.find(oEntity.clonedObject.sections, {id: sSectionId});
      oClonedSection.isCollapsedUI = bExpansionStateToSet;
    }

    if (CS.isEmpty(oCurrentSection.elements)) {
      _fetchEntityPropertyCollections(oEntity, [sSectionId], sURLToFetchSections, oCallBackData);
    }
    else {
      oCallBackData.functionToExecute && oCallBackData.functionToExecute();
    }
  };

  let _fetchEntityPropertyCollections = function (oCurrentEntity, aSectionIds, sURLToFetchSections, oCallBackData) {

    if (!CS.isEmpty(aSectionIds)) {
      let oData = {
        sectionIds: aSectionIds,
        typeId: oCurrentEntity.id
      };

      let fFailureEntityClassPropertyCollections = failureEntityClassPropertyCollections.bind(this, oCallBackData);


      SettingUtils.csPostRequest(sURLToFetchSections, {}, oData,
          successEntityClassPropertyCollections.bind(this, oCurrentEntity, oCallBackData), fFailureEntityClassPropertyCollections);
    }
  };

  let successEntityClassPropertyCollections = function (oCurrentEntity, oCallBackData, oResponse) {
    let oSuccess = oResponse.success;

    let aSections = oSuccess.list;
    let oConfigDetails = oSuccess.configDetails;
    //set ReferencedData for sections
    oCallBackData.preFunctionToExecute && oCallBackData.preFunctionToExecute(oConfigDetails);

    CS.forEach(aSections, (oSection) => {
      try {
        let oClassSection = CS.find(oCurrentEntity.sections, {id: oSection.id});
        oClassSection.elements = oSection.elements;
        _setInheritanceAndCutOffPropertiesForSectionElements(oClassSection);

        let oClonedEntity = oCurrentEntity.clonedObject;
        if(oClonedEntity){
          let oClonedClassSection = CS.find(oClonedEntity.sections, {id: oSection.id});
          oClonedClassSection.elements = CS.cloneDeep(oSection.elements);
          _setInheritanceAndCutOffPropertiesForSectionElements(oClonedClassSection);
        }
      }
      catch (oException) {
        ExceptionLogger.error(oException);
      }
    });

    oCallBackData.functionToExecute && oCallBackData.functionToExecute();
  };

  let failureEntityClassPropertyCollections = function (oCallBackData, oResponse) {
    _failureCallback(oResponse, "failureEntityClassPropertyCollections", GlobalStore.getTranslations('SettingScreenTranslations'));
  };

  let _failureCallback = function (oResponse, sFunctionName, oTranslations) {
    CommonUtils.failureCallback(oResponse, sFunctionName, oTranslations);
  };

  let _getKlassSelectorViewEntityTypeByClassType = function (sClassType, sRelType) {

    let aMockDataForKlassTypes = new MockDataForKlassTypes();
    let sKlassRootId = ClassRootBaseTypeIdDictionary[sClassType];

    let oDesiredKlassType = CS.find(aMockDataForKlassTypes, {id:sKlassRootId});
    return !CS.isEmpty(oDesiredKlassType) ? oDesiredKlassType.id : null;
  };

  let _getNoIdConstant = function () {
    return CommonModuleConstants.NO_ID;
  };

  var _isVariantRelationship = function (sRelationshipType) {
    switch (sRelationshipType) {

      case RelationshipTypeDictionary.PRODUCT_VARIANT:
      case RelationshipTypeDictionary.PROMOTIONAL_VERSION:
        return true;

      default:
        return false;
    }
  };

  let _isFixedBundleRelationship = function (sRelType) {
    return sRelType === RelationshipTypeDictionary.FIXED_BUNDLE;
  };

  let _isSetOfProductsRelationship = function (sRelType) {
    return sRelType === RelationshipTypeDictionary.SET_OF_PRODUCTS;
  };

  let _getConfigDataLazyRequestResponseObjectByEntityName = function (sEntityName, aEntityTypes) {
    let oReqResObject = {
      requestType: "configData",
      entityName: sEntityName,
    };

    if (aEntityTypes) {
      oReqResObject.types = aEntityTypes;
    }

    return oReqResObject;
  };

  let _getBPMNCustomElementIDFromBusinessObject = function (oBusinessObject) {
    let sCustomElementID;
    let aElementsToSearchFrom = [];
    let oCustomInputParamter;
    switch (oBusinessObject.$type) {
      case ProcessConstants.TARGET_START_EVENT:
        aElementsToSearchFrom = oBusinessObject.documentation;
        CS.forEach(aElementsToSearchFrom, function (oCustomInput) {
          if(oCustomInput.text){
            let aValues = oCustomInput.text.split(_getSplitter());
            if(aValues[0] === "customElementID"){
              oCustomInputParamter = oCustomInput;
              oCustomInputParamter && (sCustomElementID = aValues[1]);
            }
          }
        });
        break;
      default:
        let oExtensionElements = oBusinessObject.extensionElements;
        if (oExtensionElements) {
          let oInputOutPutParameters = oExtensionElements.values[0];
          aElementsToSearchFrom = oInputOutPutParameters.inputParameters;
        }
        oCustomInputParamter = CS.find(aElementsToSearchFrom, {name: "customElementID"});
        oCustomInputParamter && (sCustomElementID = oCustomInputParamter.value);
        break;
    }
    return sCustomElementID;
  };

  let _getEntitiesListByPortalSelections = function () {
    return MockDataForEntitiesList();
  };

  var _removeTrailingBreadcrumbPath = function (sId) {
    return CommonUtils.removeTrailingBreadcrumbPath(sId);
  };

  let _getAllowedAttributeTypesForRuleEffect = (oAttribute) => {
    let sAttributeType = oAttribute.type;
    let aAllowedTypesForRuleEffect = AllowedAttributeTypesForRuleEffect[sAttributeType];

    if(AttributeUtils.isAttributeTypeMeasurement(sAttributeType) || CS.isEmpty(aAllowedTypesForRuleEffect)) {
      aAllowedTypesForRuleEffect = [sAttributeType];
    }
    return aAllowedTypesForRuleEffect;
  };

  let copyUILangInDataLangForConfig = (oExtraData) => {
    /*
    * Config section does not makes use of dataLanguage, so replacing with ui_language.
    * Particularly used for the case of tags.
    * Tag values are received in the data language.
    * Tag values are displayed in data language, but tags in UI_language.
    * */
    let oDataLanguage = {
      dataLanguage: SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE)
    };

    oExtraData = CS.isEmpty(oExtraData) ? oDataLanguage : CS.assign(oDataLanguage, oExtraData);
    return oExtraData;
  };

  let _csGetRequest = (sUrl, oRequestData, fSuccess, fFailure, oExtraData) => {
    oExtraData = copyUILangInDataLangForConfig(oExtraData);
    return CS.getRequest(sUrl, oRequestData, fSuccess, fFailure, oExtraData);
  };

  let _csPostRequest = (sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader, oExtraData) => {
    oExtraData = copyUILangInDataLangForConfig(oExtraData);
    return CS.postRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure, bDisableLoader, oExtraData);
  };

  let _csPutRequest = (sUrl, oQueryString, oRequestData, fSuccess, fFailure) => {
    let oExtraData = {
      dataLanguage: SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE)
    };
    return CS.putRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure, oExtraData);
  };

  let _csDeleteRequest = (sUrl, oQueryString, oRequestData, fSuccess, fFailure) => {
    return CS.deleteRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure);
  };

  let _csCustomGetRequest = (sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData) => {
    oExtraData = copyUILangInDataLangForConfig(oExtraData);
    return CS.customGetRequest(sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData);
  };

  let _csCustomPostRequest = (sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData) => {
    oExtraData = copyUILangInDataLangForConfig(oExtraData);
    return CS.customPostRequest(sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData);
  };

  var _getDecodedTranslation = function (sStringToCompile, oParameter) {
    return CommonUtils.getParsedString(sStringToCompile, oParameter);
  };

  let _getMasterTagById = function (sTagId) {
    let oAppData = _getAppData();
    let aMasterTagList = oAppData.getTagList();
    return CommonUtils.getNodeFromTreeListById(aMasterTagList, sTagId);
  };

  let _getDefaultActiveNodeToSetOnListNodeDelete = function (aList, oNodesValueList, sActiveNodeId, aDeletedNodeIds) {
    let oNewActiveNode = null;
    let iDeletedItemCount = 0;
    let iIndexToSetActive = -1;

    CS.remove(aList, function (sNode, iIndex) {
      /** Remove all deleted nodes from the list**/
      let iIndexOfMatchedItemInDeletedList = CS.indexOf(aDeletedNodeIds, sNode.id);
      if (iIndexOfMatchedItemInDeletedList !== -1) {
        if (sNode.id === sActiveNodeId) {
          iIndexToSetActive = iIndex - iDeletedItemCount;
          /** stores the new index of active node before deleting  the active node from list**/
        }
        delete oNodesValueList[sNode.id];
        aDeletedNodeIds.splice(iIndexOfMatchedItemInDeletedList, 1);
        iDeletedItemCount++;/** keeps the count of deleted items in the list **/
        return true;
      }});

    /** if active node is not deleted or all the nodes in the list are deleted then return null **/
    if(iIndexToSetActive !== -1 && aList.length > 0) {
      oNewActiveNode = aList[iIndexToSetActive] || aList[iIndexToSetActive - 1]; /** If successor exists then set
       successor as new active node else set predecessor as new active node **/
    }
    return oNewActiveNode
  };

  let _getIconLibraryData = () => {
    let oIconLibraryProps = SettingScreenProps.iconLibraryProps;
    let sConfigScreenViewName = _getConfigScreenViewName();
    let oPaginationData = (sConfigScreenViewName == ConfigEntityTypeDictionary.ENTITY_TYPE_ICON_LIBRARY)
                          ? oIconLibraryProps.getPaginationData() : oIconLibraryProps.getSelectDialogPaginationData();
    return {
      icons: oIconLibraryProps.getIcons(),
      totalCount: oIconLibraryProps.getTotalCount(),
      searchText: oIconLibraryProps.getSearchText(),
      paginationData: oPaginationData,
      selectedIconIds: oIconLibraryProps.getSelectedIconIds()
    }
  };

  let _checkKeyValueModified = function (oActiveClass, sKey) {
    let bIsModified = false;
    if (oActiveClass.hasOwnProperty('clonedObject')) {
      let oClonedObject = oActiveClass.clonedObject;
      bIsModified = oActiveClass[sKey] !== oClonedObject[sKey];
    }

    return bIsModified;
  };
  //This method remove keys by making there value as undefined in request model.
  let _removeKeysFromRequestData = (oRequestData, aKeysToRemove) => {
    //valueToReplace: undefined to remove key from request data.
    let sValueToReplace = undefined;
    CS.forEach(aKeysToRemove, function (sKeyToRemove) {
      if (oRequestData.hasOwnProperty(sKeyToRemove)) {
        oRequestData[sKeyToRemove] = sValueToReplace;
      }
    });
  };
  /*********************** PUBLIC *****************/
  return {
    getAllowedAttributeTypesForRuleEffect: function(oAttribute) {
      return _getAllowedAttributeTypesForRuleEffect(oAttribute);
    },

    getAppData: function () {
      return _getAppData();
    },

    getTaxonomyHierarchyForSelectedTaxonomy: function (sTaxonomyId, sParentTaxonomyId, oCallback) {
      return _getTaxonomyHierarchyForSelectedTaxonomy(sTaxonomyId, sParentTaxonomyId, oCallback);
    },

    getComponentProps: function () {
      return _getComponentProps();
    },

    getVisibleSettingModules: function () {
      return _getVisibleSettingModules();
    },

    addNewTreeNodesToValueList: function (oTreeValuesInList, aTreeNode, oCommonPropertiesToApply) {
      _addNewTreeNodesToValueList(oTreeValuesInList, aTreeNode, oCommonPropertiesToApply);
    },

    removeChildNodesFromValueList: function (aNodes, oValueList) {
      _removeChildNodesFromValueList(aNodes, oValueList);
    },

    removeNodeById: function (aNodeList, sNodeId) {
      _removeNodeById(aNodeList, sNodeId);
    },

    getParentNodeIdByChildId: function (aNodeList, sChildId, sParentId) {
      return _getParentNodeIdByChildId(aNodeList, sChildId, sParentId);
    },

    makeObjectDirty: function (oObjectToMakeDirty) {
      RefactoringStore.makeObjectDirty(oObjectToMakeDirty);
    },

    applyValueToAllTreeNodes: function (oTreeValuesInList, sAttributeName, oValue) {
      _applyValueToAllTreeNodes(oTreeValuesInList, sAttributeName, oValue);
    },

    setPropertyValue: function (oTreeValuesList, iProperty, oTreeItemValue) {
      _setPropertyValue(oTreeValuesList, iProperty, oTreeItemValue);
    },

    checkAllChildrenIfParentIsChecked: function (oValueList, bIsChecked, aChildren) {
      if (bIsChecked) {
        CS.forEach(aChildren, function (oChildren) {
          oValueList[oChildren.id].isChecked = 2;
        });
      }
    },

    getListOfNamesToDeleteFromTreeNodeList: function (aItemList, oItemValueList, aIdList, aCreatedIds) {
      return _getListOfNamesToDeleteFromTreeNodeList(aItemList, oItemValueList, aIdList, aCreatedIds);
    },

    getTreeRootId: function () {
      return TREE_ROOT_ID;
    },

    getDecodedHtmlContent: function (sHtml) {
      var sDecodedHtml;
      var txt = document.createElement("textarea");
      txt.innerHTML = sHtml;
      sDecodedHtml = txt.value;

      return sDecodedHtml;
    },

    getDecodedTranslation: function (sStringToCompile, oParameter) {
      return _getDecodedTranslation(sStringToCompile, oParameter);
    },

    toggleLinkScrollActive: function(aList, sId){

      CS.forEach(aList, function (oItem) {
        if (oItem.isActive) {
          oItem.isActive = false;
        }
        if (oItem.id == sId) {
          oItem.isActive = true;
        }
      });

    },

    getIconUrl: function (sAssetKey) {
      return RefactoringStore.getIconUrl(sAssetKey);
    },

    getSplitter: function(){
      return _getSplitter();
    },

    getAttributeTypeForVisual: function (sType, sAttrId) {
      return _getAttributeTypeForVisual(sType, sAttrId);
    },

    getUserById: function (sUserId) {
      return _getUserById(sUserId);
    },

    getTagById: function (sId) {
      return _getTagById(sId);
    },

    getUserList: function () {
      return _getUserList();
    },

    getLeafNodeSubbedWithParentInfo: function (aTreeNode, sParentPropertyName, sChildPropName) {
      return CommonUtils.getLeafNodeWithSubbedWithParentInfo(aTreeNode, sParentPropertyName, sChildPropName);
    },

    getMaxValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return CommonUtils.getMaxValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getMinValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return CommonUtils.getMinValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getTagList: function () {
      return _getTagList();
    },

    getTagMap: function () {
      return _getTagMap();
    },

    getRoleList: function () {
      return _getRoleList();
    },

    getRuleList: function () {
      return _getRuleList();
    },

    getAttributeList: function () {
      return _getAttributeList();
    },

    handleSectionAdded: function (oActiveEntity, aSectionIds, oSectionMap) {
      return _handleSectionAdded(oActiveEntity, aSectionIds, oSectionMap);
    },

    handleSectionDeleted: function (oActiveEntity, sSectionId) {
      return _handleSectionDeleted(oActiveEntity, sSectionId);
    },

    handleSectionMoveUp: function (oCurrentEntity, sSectionId) {
      return _handleSectionMoveUp(oCurrentEntity, sSectionId);
    },

    handleSectionMoveDown: function (oCurrentEntity, sSectionId) {
      return _handleSectionMoveDown(oCurrentEntity, sSectionId);
    },

    handleSectionElementCheckboxToggled: function (oActiveEntity, sSectionId, sElementId, sProperty) {
      return _handleSectionElementCheckboxToggled(oActiveEntity, sSectionId, sElementId, sProperty);
    },

    handleSectionElementInputChanged: function (oActiveEntity, sSectionId, sElementId, sProperty, sNewValue) {
      return _handleSectionElementInputChanged(oActiveEntity, sSectionId, sElementId, sProperty, sNewValue);
    },

    handleSectionMSSValueChanged: function (oActiveClass,sSectionId, sElementId, sProperty, aNewValue, sScreenName, oCallbackData){
      return _handleSectionMSSValueChanged (oActiveClass,sSectionId, sElementId, sProperty, aNewValue, sScreenName, oCallbackData);
    },

    handleVisualElementBlockerClicked: function (oActiveClass, oInfo, fTriggerChange) {
      _handleVisualElementBlockerClicked(oActiveClass, oInfo, fTriggerChange);
    },

    addTagInEntity: function (sTagId, oEntity) {
      _addTagInEntity(sTagId, oEntity);
    },

    getNewTagValue: function (sTagId, iRelevance) {
      return _getNewTagValue(sTagId, iRelevance);
    },

    getDisplayUnitFromDefaultUnit: function (sDefaultUnit, sType) {
      var oMeasurement = {};
      let oMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
      if(!CS.isEmpty(sType)){
        var aMeasurement = oMeasurementMetricAndImperial[sType];
        oMeasurement = CS.find(aMeasurement, {unit: sDefaultUnit});
      }else {
        CS.forEach(oMeasurementMetricAndImperial, function (aTypeMeasurement) {
          var oTemp = CS.find(aTypeMeasurement, {unit: sDefaultUnit});
          if(!CS.isEmpty(oTemp)){
            oMeasurement = oTemp;
            return false;
          }
        })
      }

      return oMeasurement.unitToDisplay;
    },

    isAttributeTypeFile: function (sType) {
      return AttributeUtils.isAttributeTypeFile(sType);
    },

    /** @deprecated **/
    isAttributeTypeType: function (sType) {
      return AttributeUtils.isAttributeTypeType(sType);
    },

    isImageCoverflowAttribute: function (sAttributeType) {
      return AttributeUtils.isImageCoverflowAttribute(sAttributeType);
    },

    isAttributeTypeSecondaryClasses: function (sType) {
      return AttributeUtils.isAttributeTypeSecondaryClasses(sType);
    },

    testNumber: function(sNumber){
      var oRegexPatt = /^-?(\d*\.)?\d+$/;

      var sNewNum = CS.includes(sNumber, "e") ? Number(sNumber).toFixed(20) : sNumber; //To check exponential numbers.
      return oRegexPatt.test(sNewNum);
    },

    getMasterTagById: function (sTagId) {
      return _getMasterTagById(sTagId);
    },

    isAttributeTypeTaxonomy: function (sType) {
      return AttributeUtils.isAttributeTypeTaxonomy(sType);
    },

    getSimpleNameFromTabBaseType: function (sBaseType) {
      switch (sBaseType) {
        case TemplateTabsDictionary.TASKS_TAB:
          return "tasksTab";
          break;
      }
    },

    isMeasurementAttributeTypeCustom: function (sType) {
      return AttributeUtils.isMeasurementAttributeTypeCustom(sType);
    },

    isAttributeTypeCalculated: function (sType) {
      return AttributeUtils.isAttributeTypeCalculated(sType);
    },

    isAttributeTypeConcatenated: function (sType) {
      return AttributeUtils.isAttributeTypeConcatenated(sType);
    },

    isAttributeTypeHtml: function (sType) {
      return AttributeUtils.isAttributeTypeHtml(sType);
    },

    isAttributeTypeText: function (sType) {
      return AttributeUtils.isAttributeTypeText(sType);
    },

    rejectStatusAndLifecycleTagsTypes: function (aTagList){
      var aTempTagList = aTagList;
      aTempTagList = CS.reject(aTempTagList, {tagType: TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE});
      aTempTagList = CS.reject(aTempTagList, {tagType: TagTypeConstants.STATUS_TAG_TYPE});
      return aTempTagList;
    },

    isLinkedTypeContext: function (sContextType) {
      return (sContextType == ContextTypeDictionary.PRODUCT_VARIANT ||
      sContextType == ContextTypeDictionary.PROMOTIONAL_VERSION);
    },

    getNatureTypeListBasedOnClassType: function (sType, sSecondaryType) {
      return _getNatureTypeListBasedOnClassType(sType, sSecondaryType);
    },

    calculateNestedContentCount: function (aContents) {
      var iCount = aContents.length;
      CS.forEach(aContents, function (oContent) {
        if (oContent.tagType !== TagTypeConstants.TAG_TYPE_BOOLEAN) {
          oContent.children && (iCount += oContent.children.length);
        }
      });
      return iCount;
    },

    getParentNodeByChildId: function (aNodeList, sChildId) {
      return _getParentNodeByChildId(aNodeList, sChildId);
    },

    getDefaultActiveNodeToSetOnTreeNodeDelete (aTree, sActiveNodeId, sDeletedNodeId) {
      return _getDefaultActiveNodeToSetOnTreeNodeDelete(aTree, sActiveNodeId, sDeletedNodeId);
    },

    applyToAllNodesBelow: function (aPropertyKeyValue, oNode, oVisualProps, bExcludeCallingNode) {
      _applyToAllNodesBelow(aPropertyKeyValue, oNode, oVisualProps, bExcludeCallingNode);
    },

    getEntitySearchText: function () {
      return SettingScreenProps.screen.getEntitySearchText();
    },

    getEntityPaginationData: function(){
      return SettingScreenProps.screen.getEntitiesPaginationData();
    },

    getReferencedAttributes: function () {
      return SettingScreenProps.screen.getReferencedAttributes();
    },

    getReferencedTags: function () {
      return SettingScreenProps.screen.getReferencedTags();
    },

    getReferencedRoles: function () {
      return SettingScreenProps.screen.getReferencedRoles();
    },

    getLoadedAttributesData: function () {
      return SettingScreenProps.screen.getLoadedAttributesData();
    },

    getLoadedTagsData: function () {
      return SettingScreenProps.screen.getLoadedTagsData();
    },

    getLoadedRolesData: function () {
      return SettingScreenProps.screen.getLoadedRolesData();
    },

    getEntityVsSearchTextMapping: function () {
      return SettingScreenProps.screen.getEntityVsSearchTextMapping();
    },

    isOnboarding: function () {
      return _isOnboarding();
    },

    isOffboarding: function () {
      return _isOffboarding();
    },

    addNewTagCombination: function(oActiveContext){
      return _addNewTagCombination(oActiveContext);
    },

    removeEmptyTagSelections: function (oActiveContext) {
      _removeEmptyTagSelections(oActiveContext);
    },

    setTagValueDataInTagCombination: function(oContext, sSelectorId, sTagId, aSelectedTags){
      _setTagValueDataInTagCombination(oContext, sSelectorId, sTagId, aSelectedTags);
    },

    deleteTagCombination: function(sUniqueSelectionId, oContext){
      _deleteTagCombination(sUniqueSelectionId, oContext);
    },

    updateUniqueSelectionOrder: function(oActiveContext, sUniqueSelectorId){
      _updateUniqueSelectionOrder(oActiveContext, sUniqueSelectorId);
    },

    deleteTagValueFromCombination: function(oContext, sSelectorId, sTagId, sId){
      _deleteTagValueFromCombination(oContext, sSelectorId, sTagId, sId);
    },

    isUniqueSelectorModifiedForCombination: function (oOldUniqueTagSelection, oNewUniqueTagSelection){
      return _isUniqueSelectorModified(oOldUniqueTagSelection, oNewUniqueTagSelection);
    },

    checkUniqueSelectorsDuplications: function(oContext){
      return _checkUniqueSelectorsDuplications(oContext);
    },

    removeEmptyTagGroupFromCurrentContext: function (oCurrentContext) {
      _removeEmptyTagGroupFromCurrentContext(oCurrentContext);
    },

    setInheritanceAndCutOffUIProperties: function (aSections) {
      _setInheritanceAndCutOffUIProperties(aSections);
    },

    updateTagValueRelevanceOrRange: function (oTagValue, oRelevanceData, bUpdateRange) {
      TagUtils.updateTagValueRelevanceOrRange(oTagValue, oRelevanceData, bUpdateRange);
    },

    updateConfigSectionTagValueRelevance: function(aSections, sSectionId, sElementId, aTagValueRelevanceData) {
      _updateConfigSectionTagValueRelevance(aSections, sSectionId, sElementId, aTagValueRelevanceData);
    },

    validateRule: function (oCurrentRule) {
      return _validateRule(oCurrentRule);
    },

    getDefaultListProps: function () {
      return _getDefaultListProps();
    },

    getOrganisationStore: function () {
      return _getOrganisationStore();
    },

    getRoleStore: function () {
      return _getRoleStore();
    },

    getSSOSettingStore: function () {
      return _getSSOSettingStore();
    },

    getPermissionStore: function () {
      return _getPermissionStore();
    },

    getAllowedPhysicalCatalogs: function () {
      return _getAllowedPhysicalCatalogs();
    },

    getUserFullName: function (oUser) {
      return CommonUtils.getUserFullName(oUser);
    },

    preProcessContextData: function (oContextData) {
      _preProcessContextData(oContextData);
    },

    checkForUniquenessOfCodePropertyAndSendAjaxCall: function (oRequestData, sURL, oCallbackData) {
      return _checkForUniquenessOfCodePropertyAndSendAjaxCall(oRequestData, sURL, oCallbackData);
    },

    isValidEntityCode: function (sCode) {
      return _isValidEntityCode(sCode);
    },

    removeOrganisationsFromProps: function (aIds, oResponse) {
      _removeOrganisationsFromProps(aIds, oResponse);
    },

    downloadFromByteStream: function (oByteStream, sFileName) {
      CommonUtils.downloadFromByteStream(oByteStream, sFileName);
    },

    getSelectedMenu: function () {
      return GlobalStore.getSelectedMenu();
    },

    getSelectedSettingScreenModuleById: function (sId) {
      let aConfigModulesList = new ConfigModulesList();
      return CS.find(aConfigModulesList, {id: sId}) || {};
    },

    getSelectedTreeItemById: function (sItemId, sParentId) {
      return _getSelectedTreeItemById(sItemId, sParentId);
    },

    getGridSkeletonForAttribute: function () {
      return _getGridSkeletonForAttribute();
    },

    getGridSkeletonForTag: function () {
      return _getGridSkeletonForTag();
    },

    getSettingScreenStore: function () {
      return SettingScreenStore;
    },

    handleListViewSearchOrLoadMoreClicked: function (sSearchText, bLoadMore, oEntityList, oScreenSpecificProp, fFunctionToExecute) {
      _handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, oEntityList, oScreenSpecificProp, fFunctionToExecute);
    },

    generateADMForCustomTabs: function (oOld, oNew) {
      return _generateADMForCustomTabs(oOld, oNew)
    },

    getEntityListViewLoadMorePostData: function (oScreenSpecificProp, bLoadMore) {
      return _getEntityListViewLoadMorePostData(oScreenSpecificProp, bLoadMore);
    },

    checkFormulaValidity: function (aAttributeOperatorList) {
      return _checkFormulaValidity(aAttributeOperatorList);
    },

    getConfigScreenViewName: function () {
      return _getConfigScreenViewName();
    },

    getLazyMSSViewModel: function (aSelectedItems, oReferencedData, sContextKey, oReqResObj, bIsMultiselect, aExcludedItems, bIsDisabled = false, bShowIcon = false) {
      return _getLazyMSSViewModel(aSelectedItems, oReferencedData, sContextKey, oReqResObj, bIsMultiselect, aExcludedItems, bIsDisabled, bShowIcon);
    },

    isGridViewScreen: function (sScreenName) {
      return _isGridViewScreen(sScreenName);
    },

    isNatureTypeRelationship: function (sRelType) {
      return _isNatureTypeRelationship(sRelType);
    },

    failureCallback: function (oResponse, sFunctionName, oTranslations, oCallback) {
      let configError = CS.reduce(oResponse.failure.exceptionDetails, (isConfigError, error) => {
        if (error.key === "EntityConfigurationDependencyException") {
          isConfigError = true;
        }
        return isConfigError;
      }, false);
      if (configError) {
        if (oCallback && oCallback.functionToExecute) {
          oCallback.functionToExecute();
          return;
        }
      }
      _failureCallback(oResponse, sFunctionName, oTranslations);
    },

    handleSectionToggleButtonClicked: function (sSectionId, oEntity, sURLToFetchSections, oCallBackData) {
      _handleSectionToggleButtonClicked(sSectionId, oEntity, sURLToFetchSections, oCallBackData);
    },

    getKlassSelectorViewEntityTypeByClassType: function (sClassType,sRelType) {
      return _getKlassSelectorViewEntityTypeByClassType(sClassType, sRelType);
    },

    getNoIdConstant: function () {
      return _getNoIdConstant();
    },

    isVariantRelationship: function (sRelType) {
      return _isVariantRelationship(sRelType);
    },

    isFixedBundleRelationship: function (sRelType) {
      return _isFixedBundleRelationship(sRelType);
    },

    isSetOfProductsRelationship: function (sRelType) {
      return _isSetOfProductsRelationship(sRelType);
    },

    getConfigDataLazyRequestResponseObjectByEntityName: function (sEntityName, aEntityTypes) {
      return _getConfigDataLazyRequestResponseObjectByEntityName(sEntityName, aEntityTypes);
    },

    getNodeFromTreeListById: function (aNodeList, sNodeId) {
      return CommonUtils.getNodeFromTreeListById(aNodeList, sNodeId);
    },

    getNodePathIdsFromTreeById: function (aNodeList, sNodeId) {
      return CommonUtils.getNodePathIdsFromTreeById(aNodeList, sNodeId);
    },

    getAllIdsInTree: function (aNodeList) {
      return CommonUtils.getAllIdsInTree(aNodeList);
    },

    getBPMNCustomElementIDFromBusinessObject: function (oBusinessObject) {
      return _getBPMNCustomElementIDFromBusinessObject(oBusinessObject)
    },

    getMultiTaxonomyData: function (aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree, oAllowedTaxonomyHierarchyList) {
      return CommonUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree, oAllowedTaxonomyHierarchyList);
    },

    getEntitiesListByPortalSelections: function () {
      return _getEntitiesListByPortalSelections();
    },

    removeTrailingBreadcrumbPath: function (sId) {
      return _removeTrailingBreadcrumbPath(sId);
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

    getDateAttributeInTimeFormat: function (sValue, sFormat) {
      return CommonUtils.getDateAttributeInTimeFormat(sValue, sFormat);
    },

    convertReferencedInObjectFormat: function (oReferencedData) {
      return CommonUtils.convertReferencedInObjectFormat(oReferencedData);
    },

    isAttributeTypeDate: function (sType) {
      return AttributeUtils.isAttributeTypeDate(sType);
    },

    isAttributeTypeCoverflow: function (sType) {
      return AttributeUtils.isAttributeTypeCoverflow(sType);
    },

    isAttributeTypeMetadata: function (sType) {
      return AttributeUtils.isAttributeTypeMetadata(sType);
    },

    isAttributeTypeMeasurement: function (sType) {
      return AttributeUtils.isAttributeTypeMeasurement(sType);
    },

    isAttributeTypeNumber: function (sType) {
      return AttributeUtils.isAttributeTypeNumber(sType);
    },

    isAttributeTypeCreatedOn: function (sType) {
      return AttributeUtils.isAttributeTypeCreatedOn(sType);
    },

    isAttributeTypeLastModified: function (sType) {
      return AttributeUtils.isAttributeTypeLastModified(sType);
    },

    isAttributeTypeUser: function (sType) {
      return AttributeUtils.isAttributeTypeUser(sType);
    },

    isAttributeTypeName: function (sType) {
      return AttributeUtils.isAttributeTypeName(sType);
    },

    csGetRequest: function (sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData) {
      return _csGetRequest(sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData);
    },

    csPostRequest: function (sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData) {
      return _csPostRequest(sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData);
    },

    csPutRequest: function (sUrl, oQueryString, oRequestData, fSuccess, fFailure) {
      return _csPutRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure);
    },

    csDeleteRequest: function (sUrl, oQueryString, oRequestData, fSuccess, fFailure) {
      return _csDeleteRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure);
    },

    csCustomGetRequest: function (sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData) {
      return _csCustomGetRequest(sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData);
    },

    csCustomPostRequest: function (sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData) {
      return _csCustomPostRequest(sUrl, oRequestData, fSuccess, fFailure, contentType, bDisableLoader, oExtraData);
    },

    isTag: function (sBaseType) {
      return TagUtils.isTag(sBaseType);
    },

    getAllNumericTypeAttributes: function () {
      return AttributeUtils.getAllNumericTypeAttributes();
    },

    getSessionOrganizationId : function () {
       return SessionProps.getSessionOrganizationId();
    },

    getDefaultActiveNodeToSetOnListNodeDelete: function (aList, oNodesValueList, sActiveNodeId, aDeletedId) {
      return _getDefaultActiveNodeToSetOnListNodeDelete(aList, oNodesValueList, sActiveNodeId, aDeletedId);
    },

    getIconLibraryData: function () {
      return _getIconLibraryData();
    },

    checkKeyValueModified: function (oActiveClass, sKey) {
     return _checkKeyValueModified(oActiveClass, sKey);
    },

    removeKeysFromRequestData: function (oRequestData, aKeysToRemove) {
      return _removeKeysFromRequestData(oRequestData, aKeysToRemove);
    },

    getSelectedEntityParentData: function (aModulesEntityList, sSelectedItem) {
      return _getSelectedEntityData(aModulesEntityList, sSelectedItem);
    },
  }

})();

export default SettingUtils;
