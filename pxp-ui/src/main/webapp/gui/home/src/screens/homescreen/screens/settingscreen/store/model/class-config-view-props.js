/**
 * Created by DEV on 29-07-2015.
 */

import CS from '../../../../../../libraries/cs';

import oQuickLinkItemList from './../../tack/quick-link-item-list';
import NatureTypeDictionary from '../../../../../../commonmodule/tack/nature-type-dictionary.js';
import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import TabHeaderData from "../../tack/mock/mock-data-for-map-summery-header";

var ClassConfigViewProps = (function () {

  let Props = function () {
    return {
      oClassValueList: {
        "-1": {
          id: -1,
          isChecked: 2,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'CLASSES'
        }
      },
      oTaskValueList: {
        "-1": {
          id: -1,
          isChecked: 2,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'TASKS'
        }
      },
      oAssetValueList: {
        "-1": {
          id: -1,
          isChecked: 2,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'ASSETS'
        }
      },
      oTargetValueList: {
        "-1": {
          id: -1,
          isChecked: 2,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'MARKET'
        }
      },
      oEditorialValueList: {
        "-1": {
          id: -1,
          isChecked: 2,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'EDITORIALS'
        }
      },
      oTextAssetValueList: {
        "-1": {
          id: -1,
          isChecked: 2,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'TEXT_ASSET'
        }
      },
      oSupplierValueList: {
        "-1": {
          id: -1,
          isChecked: 2,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label: 'SUPPLIER'
        }
      },
      oActiveClass: {},
      oClassGlobalPermission: {},
      bClassScreenLockedStatus: false,
      bClassImpacted: false,
      oClassImpactStateData: {},
      oClassRelationshipValueList: {},
      oClassSectionDragStatus: {},
      sSelectedSectionId: '',
      oScrollLinkList: new oQuickLinkItemList(),
      oSelectedContentStructureElement: {},
      bIsAllFramesAllowed: false,
      oContentStructureFlatMap: {},
      isGtinKlassEnabled: false,
      oContextsEnabled: {
        embeddedVariantContexts: false,
        promotionalVersionContexts: false,
        productVariantContexts: true
      },
      oCaretPosition: {
        focusId: '',
        indexToFocus: 99
      },
      oCollapsedContentStructures: {},
      oAllowedClassesModelData: {
        label: "Allowed Children",
        classesList: [],
        selectedClassesList: [],
        isActiveState: false,
        height: 200,
        context: "classConfigAllowedClasses",
        isSingleSelect: false,
        isDisable: false
      },
      oAllowedRTEIconsData: {
        label: "Allowed Rich Text Icons",
        iconsList: [],
        selectedIconsList: [],
        isActiveState: false,
        height: 200,
        context: "classConfigAllowedRTEIcons",
        isSingleSelect: false,
        isDisable: true
      },
      oAllowedFrameSiblingModelData: {
        label: "Allowed Sibling Frames",
        classesList: [],
        selectedClassesList: [],
        isActiveState: false,
        height: 200,
        context: "classConfigAllowedSiblingFrames",
        isSingleSelect: false,
        isDisable: false
      },
      oDefaultHTMLStyles: {
        label: "Default Styles",
        iconsList: [],
        selectedIconsList: [],
        isActiveState: false,
        height: 200,
        context: "classConfigDefaultHTMLStyles",
        isSingleSelect: false,
        isDisable: false
      },
      oClassesReferredInContentStructure: {},
      oKlassesForRelationship: {},
      oSingleArticleKlassesForRelationship: {},
      oSectionRoleList: {},
      oSectionRoleValuesList: {},
      oSelectedRoleForPermission: {},
      oRoleValueListForPermission: {},
      oMandatoryAttributesProps: {},
      oTaskMandatoryAttributesProps: {},
      oAssetMandatoryAttributesProps: {},
      oTargetMandatoryAttributesProps: {},
      oEditorialMandatoryAttributesProps: {},
      oClassSectionAttributeMap: {},
      oClassSectionMap: {},
      oClassSectionTagMap: {},
      oClassSectionRelationshipMap: {},
      oClassSectionRoleMap: {},
      sSelectedClassCategory: "class",
      sSectionAvailableElementSearchText: "",
      bTagDialogVisibility: false,
      oReferencedContexts: {},
      sClassContextId: {},
      sActiveTagUniqueSelectionId: "", //for class context
      oClassContext: {},
      aClassContextTagOrder: [],
      oClassContextDialogProps: {},
      oDefaultClassContextDialogProps: {
        isDialogOpen: false,
        isTimeEnabled: false,
        isAutoCreate: false,
        allSectionsDisabled: false,
        label: "",
        type: "",
        isDuplicateVariantAllowed: false,
        entities: [],
        contextTags: [],
        uniqueSelectors: [],
        defaultTimeRange: {
          to: null,
          from: null,
          isCurrentTime: false
        }
      },
      oReferencedClassObjects: {},
      oReferencedTags: {},
      aFieldsToExclude: [],
      side2RelationshipDataCollapseEnabled: {
        relationshipList : true,
        propertiesList : true
      },
    };
  };

  let oProperties = new Props();

  return {

    getSectionAvailableElementSearchText: function(){
      return oProperties.sSectionAvailableElementSearchText;
    },

    getContentStructureFlatMap: function(){
      return oProperties.oContentStructureFlatMap;
    },

    setContentStructureFlatMap: function(_oContentStructureFlatMap){
      oProperties.oContentStructureFlatMap = _oContentStructureFlatMap;
    },

    getContextsEnabled: function(){
      return oProperties.oContextsEnabled;
    },

    getClassValueList: function () {
      oProperties.oClassValueList[-1].label = oTranslations().CLASSES;
      return oProperties.oClassValueList;
    },

    getTaskValueList: function () {
      oProperties.oTaskValueList[-1].label = oTranslations().TASKS;
      return oProperties.oTaskValueList;
    },

    getAssetValueList: function () {
      oProperties.oAssetValueList[-1].label = oTranslations().ASSETS;
      return oProperties.oAssetValueList;
    },

    getTargetValueList: function () {
      oProperties.oTargetValueList[-1].label = oTranslations().MARKET;
      return oProperties.oTargetValueList;
    },

    getEditorialValueList: function () {
      oProperties.oEditorialValueList[-1].label = oTranslations().EDITORIALS;
      return oProperties.oEditorialValueList;
    },

    getTextAssetValueList: function () {
      oProperties.oTextAssetValueList[-1].label = oTranslations().TEXT_ASSET;
      return oProperties.oTextAssetValueList;
    },

    getSupplierValueList: function () {
      oProperties.oSupplierValueList[-1].label = oTranslations().SUPPLIER;
      return oProperties.oSupplierValueList;
    },

    getClassValueListByTypeGeneric: function (sType) {

      switch (sType){
        case 'class':
          oProperties.oClassValueList[-1].label = oTranslations().CLASSES;
          return oProperties.oClassValueList;

        case 'task':
          oProperties.oTaskValueList[-1].label = oTranslations().TASKS;
          return oProperties.oTaskValueList;

        case 'asset':
          oProperties.oAssetValueList[-1].label = oTranslations().ASSETS;
          return oProperties.oAssetValueList;

        case 'target':
          oProperties.oTargetValueList[-1].label = oTranslations().MARKET;
          return oProperties.oTargetValueList;

        case 'editorial':
          oProperties.oEditorialValueList[-1].label = oTranslations().EDITORIALS;
          return oProperties.oEditorialValueList;

        case 'textasset':
          oProperties.oTextAssetValueList[-1].label = oTranslations().TEXT_ASSET;
          return oProperties.oTextAssetValueList;

        case 'supplier':
          oProperties.oSupplierValueList[-1].label = oTranslations().SUPPLIER;
          return oProperties.oSupplierValueList;
      }

      return null;
    },

    getClassImpactStateData: function () {
      return oProperties.oClassImpactStateData;
    },

    setClassImpactStateData: function (_oClassImpactStateData) {
      oProperties.oClassImpactStateData = _oClassImpactStateData;
    },

    getActiveClass: function () {
      return oProperties.oActiveClass;
    },

    setActiveClass: function (_oClass) {
      oProperties.oActiveClass = _oClass;
    },

    getClassScreenLockedStatus: function () {
      return oProperties.bClassScreenLockedStatus;
    },

    setClassScreenLockedStatus: function (_bValue) {
      oProperties.bClassScreenLockedStatus = _bValue;
    },

    getClassImpacted: function () {
      return oProperties.bClassImpacted;
    },

    setClassImpacted: function (_bClassImpact) {
      oProperties.bClassImpacted = _bClassImpact;
    },

    getClassRelationshipValueList: function () {
      return oProperties.oClassRelationshipValueList;
    },

    setClassRelationshipValueList: function (_valueList) {
      oProperties.oClassRelationshipValueList = _valueList;
    },

    getClassSectionDragStatus: function () {
      return oProperties.oClassSectionDragStatus;
    },

    setClassSectionDragStatus: function (_oDragStatus) {
      oProperties.oClassSectionDragStatus = _oDragStatus;
    },

    getSelectedSectionId: function () {
      return oProperties.sSelectedSectionId;
    },

    setSelectedSectionId: function (sId) {
      oProperties.sSelectedSectionId = sId;
    },

    getScrollLinkList: function (){
      return oProperties.oScrollLinkList;
    },

    setScrollLinkList: function (oLinkList) {
      oProperties.oScrollLinkList = oLinkList;
    },

    getSelectedContentStructureElement: function () {

      return oProperties.oSelectedContentStructureElement;
    },

    setSelectedContentStructureElement: function (oElement) {
      oProperties.oSelectedContentStructureElement = oElement;
    },

    getCaretPosition: function () {

      return oProperties.oCaretPosition;
    },

    setCaretPosition: function (oPosition) {
      oProperties.oCaretPosition = oPosition;
    },

    toggleContentStructureExpanded: function(sContentStructureId){
      if(!oProperties.oCollapsedContentStructures[sContentStructureId]){
        oProperties.oCollapsedContentStructures[sContentStructureId] = true;
      } else {
        delete oProperties.oCollapsedContentStructures[sContentStructureId];
      }
    },

    getContentStructureCollapsedMap: function(){
      return oProperties.oCollapsedContentStructures;
    },

    getAllowedClassesModelData: function () {

      return oProperties.oAllowedClassesModelData;
    },

    setAllowedClassesModelData: function (oData) {
      oProperties.oAllowedClassesModelData = oData;
    },

    getAllowedRTEIconsModelData: function () {

      return oProperties.oAllowedRTEIconsData;
    },

    setAllowedRTEIconsModelData: function (oData) {
      oProperties.oAllowedRTEIconsData = oData;
    },

    getAllowedFrameSiblingModelData: function () {

      return oProperties.oAllowedFrameSiblingModelData;
    },

    getDefaultHTMLStyles: function () {

      return oProperties.oDefaultHTMLStyles;
    },

    getIsAllClassesAllowed: function () {

      return oProperties.bIsAllFramesAllowed;
    },

    setIsAllClassesAllowed: function (bFlag) {
      oProperties.bIsAllFramesAllowed = bFlag;
    },

    getAllowedRTEIcons: function () {

      return oProperties.oRichTextPlugins;
    },

    getClassContentStructureRootNode: function(oActiveClass){
      if(oActiveClass.isVisualAttribute){
        return oActiveClass.structureChildren[0];
      }
      return {
        id: "-1",
        guid: "-1",
        type: "-1",
        label: oActiveClass.label,
        validator: oActiveClass.validator,
        structureChildren: oActiveClass.structureChildren || []
      };
    },

    getClassesReferredInContentStructure: function () {

      return oProperties.oClassesReferredInContentStructure;
    },

    setClassesReferredInContentStructure: function (oReferredClasses) {
      oProperties.oClassesReferredInContentStructure = oReferredClasses;
    },

    getKlassesForRelationship: function () {
      return oProperties.oKlassesForRelationship;
    },

    getSingleArticleKlassesForRelationship: function () {
      return CS.filter(CS.flatMap(oProperties.oKlassesForRelationship), {natureType: NatureTypeDictionary.SINGLE_ARTICLE});
    },

    getImageAssetKlassesForRelationship: function () {
      return CS.filter(CS.flatMap(oProperties.oKlassesForRelationship), {natureType: NatureTypeDictionary.IMAGE_ASSET});
    },

    getReferencedClassDetailForContentStructureById: function (sClassId) {

      return oProperties.oClassesReferredInContentStructure[sClassId];
    },

    setContentStructureCollapseAll: function (_oCollapseAllContentStructures) {
      oProperties.oCollapsedContentStructures = _oCollapseAllContentStructures;
    },

    getSectionRoleList: function () {
      return oProperties.oSectionRoleList;
    },

    setSectionRoleList: function (oSectionRole) {
      oProperties.oSectionRoleList[oSectionRole.id] = oSectionRole;
    },

    emptySectionRoleList: function () {
      oProperties.oSectionRoleList = {};
    },

    getSectionRoleValuesList: function () {
      return oProperties.oSectionRoleValuesList;
    },

    setSectionRoleValuesList: function (oSectionValueRole) {
      oProperties.oSectionRoleValuesList[oSectionValueRole.id] = oSectionValueRole;
    },

    emptySectionRoleValuesList: function () {
      oProperties.oSectionRoleValuesList = {};
    },

    getSelectedRoleInPermission: function () {
      return oProperties.oSelectedRoleForPermission;
    },

    setSelectedRoleInPermission: function (_SelectedRoleForPermission) {
      oProperties.oSelectedRoleForPermission = _SelectedRoleForPermission;
    },

    emptySelectedRoleInPermission: function () {
      oProperties.oSelectedRoleForPermission = {};
    },

    getRoleValueListForPermission: function () {
      return oProperties.oRoleValueListForPermission;
    },

    setRoleValueListForPermission: function (_RoleValueListForPermission) {
      oProperties.oRoleValueListForPermission = _RoleValueListForPermission;
    },

    getMandatoryAttributeProps: function () {
      return oProperties.oMandatoryAttributesProps;
    },

    setMandatoryAttributeProps: function (_oMandatoryAttributesProps) {
      oProperties.oMandatoryAttributesProps = _oMandatoryAttributesProps;
    },

    getTaskMandatoryAttributeProps: function () {
      return oProperties.oTaskMandatoryAttributesProps;
    },

    setTaskMandatoryAttributeProps: function (_oTaskMandatoryAttributesProps) {
      oProperties.oTaskMandatoryAttributesProps = _oTaskMandatoryAttributesProps;
    },

    getAssetMandatoryAttributeProps: function () {
      return oProperties.oAssetMandatoryAttributesProps;
    },

    setAssetMandatoryAttributeProps: function (_oAssetMandatoryAttributesProps) {
      oProperties.oAssetMandatoryAttributesProps = _oAssetMandatoryAttributesProps;
    },

    getTargetMandatoryAttributeProps: function () {
      return oProperties.oTargetMandatoryAttributesProps;
    },

    setTargetMandatoryAttributeProps: function (_oTargetMandatoryAttributesProps) {
      oProperties.oTargetMandatoryAttributesProps = _oTargetMandatoryAttributesProps;
    },

    getEditorialMandatoryAttributeProps: function () {
      return oProperties.oEditorialMandatoryAttributesProps;
    },

    setEditorialMandatoryAttributeProps: function (_oEditorialMandatoryAttributesProps) {
      oProperties.oEditorialMandatoryAttributesProps = _oEditorialMandatoryAttributesProps;
    },

    getClassSectionAttributeMap: function () {
      return oProperties.oClassSectionAttributeMap;
    },

    setClassSectionAttributeMap: function (_oClassSectionAttributeMap) {
      oProperties.oClassSectionAttributeMap = _oClassSectionAttributeMap;
    },

    getClassSectionMap: function () {
      return oProperties.oClassSectionMap;
    },

    setClassSectionMap: function (_oClassSectionMap) {
      oProperties.oClassSectionMap = _oClassSectionMap;
    },

    getClassSectionTagMap: function () {
      return oProperties.oClassSectionTagMap;
    },

    setClassSectionTagMap: function (_oClassSectionTagMap) {
      oProperties.oClassSectionTagMap = _oClassSectionTagMap;
    },

    getClassSectionRelationshipMap: function () {
      return oProperties.oClassSectionRelationshipMap;
    },

    setClassSectionRelationshipMap: function (_oClassSectionRelationshipMap) {
      oProperties.oClassSectionRelationshipMap = _oClassSectionRelationshipMap;
    },

    getClassSectionRoleMap: function () {
      return oProperties.oClassSectionRoleMap;
    },

    setClassSectionRoleMap: function (_oClassSectionRoleMap) {
      oProperties.oClassSectionRoleMap = _oClassSectionRoleMap;
    },

    getSelectedClassCategory: function () {
      return oProperties.sSelectedClassCategory;
    },

    setSelectedClassCategory: function (sClassCategory) {
      oProperties.sSelectedClassCategory = sClassCategory;
    },

    getClassGlobalPermission: function () {
      return oProperties.oClassGlobalPermission;
    },

    setClassGlobalPermission: function (_oClassGlobalPermission) {
      oProperties.oClassGlobalPermission = _oClassGlobalPermission;
    },

    setTagDialogVisibility: function (_bTagDialogVisibility) {
      oProperties.bTagDialogVisibility = _bTagDialogVisibility;
    },

    getTagDialogVisibility: function () {
      return oProperties.bTagDialogVisibility;
    },

    getIsGtinKlassEnabled: function () {
      return oProperties.isGtinKlassEnabled;
    },

    setIsGtinKlassEnabled: function (_isGtinKlassEnabled) {
      oProperties.isGtinKlassEnabled = _isGtinKlassEnabled;
    },

    getClassContextDialogProps: function () {
      if(CS.isEmpty(oProperties.oClassContextDialogProps)){
        oProperties.oClassContextDialogProps = CS.cloneDeep(oProperties.oDefaultClassContextDialogProps);
      }
      return oProperties.oClassContextDialogProps;
    },

    setClassContextDialogProps: function (_oClassContextDialogProps) {
      oProperties.oClassContextDialogProps = _oClassContextDialogProps;
    },

    getClassContext: function(){
      return oProperties.oClassContext;
    },

    setClassContext: function(_oClassContext){
      oProperties.oClassContext = _oClassContext;
    },

    getReferencedContexts: function(){
      return oProperties.oReferencedContexts;
    },

    setReferencedContexts: function(_oReferencedTags){
      oProperties.oReferencedContexts = _oReferencedTags;
    },

    getClassContextId: function(){
      return oProperties.sClassContextId;
    },

    setClassContextId: function(_sClassContextId){
      oProperties.sClassContextId = _sClassContextId;
    },

    getActiveTagUniqueSelectionId: function () {
      return oProperties.sActiveTagUniqueSelectionId;
    },

    setActiveTagUniqueSelectionId: function (_sActiveTagUniqueSelectionId) {
      oProperties.sActiveTagUniqueSelectionId = _sActiveTagUniqueSelectionId;
    },

    getClassContextTagOrder: function () {
      return oProperties.aClassContextTagOrder;
    },

    setClassContextTagOrder: function (_aClassContextTagOrder) {
      oProperties.aClassContextTagOrder = _aClassContextTagOrder;
    },

    getReferencedClassObjects: function () {
      return oProperties.oReferencedClassObjects;
    },

    setReferencedClassObjects: function (_oReferencedClassObjects) {
      oProperties.oReferencedClassObjects = _oReferencedClassObjects;
    },

    getReferencedTags: function () {
     return oProperties.oReferencedTags;
    },

    setReferencedTags: function (_oReferencedTags) {
      oProperties.oReferencedTags = _oReferencedTags;
    },

    setKlassesForRelationship: function (_oKlassesForRelationship) {
      oProperties.oKlassesForRelationship = _oKlassesForRelationship;
    },

    getFieldsToExcludeForContextDialog: function () {
      return oProperties.fieldsToExclude;
    },

    setFieldsToExcludeForContextDialog: function (aFieldsToExclude) {
      oProperties.fieldsToExclude = aFieldsToExclude;
    },

    getSide2RelationshipCollapseEnabled: function () {
      return oProperties.side2RelationshipDataCollapseEnabled;
    },

    setSide2RelationshipCollapseEnabled: function (_oSide2RelationshipDataCollapseEnabled) {
      oProperties.side2RelationshipDataCollapseEnabled = _oSide2RelationshipDataCollapseEnabled;
    },

    reset: function () {
      oProperties = new Props();
    },
  }
})();
export default ClassConfigViewProps;
