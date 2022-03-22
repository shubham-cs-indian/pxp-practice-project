/**
 * Created by DEV on 29-07-2015.
 */
import CS from '../../../../../../libraries/cs';

import oQuickLinkItemList from './../../tack/quick-link-item-list';
import oRichTextPlugins from '../../tack/mock/mock-data-for-rich-text-editor-plugins.js';
import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import DefaultPaginationData from '../../tack/mock/default-pagination-data';

var PropertyCollectionConfigViewProps = (function () {

  let Props = function () {

    return {
      oPropertyCollectionValueList: {
        "-1": {
          id: -1,
          isChecked: 2,
          isChildLoaded: true,
          isEditable: false,
          isExpanded: true,
          isLoading: false,
          isSelected: true,
          label:'PROPERTY_COLLECTIONS'
        }
      },

      oActivePropertyCollection: {},
      oClassGlobalPermission: {},
      bPropertyCollectionScreenLockedStatus: false,
      bClassImpacted: false,
      oClassImpactStateData: {},
      oClassRelationshipValueList: {},
      sSelectedSectionId: '',
      oScrollLinkList: new oQuickLinkItemList(),
      oSelectedContentStructureElement: {},
      bIsAllFramesAllowed: false,
      oContentStructureFlatMap: {},

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
      oSectionTaxonomyList: {},
      oMandatoryAttributesProps: {},
      oTaskMandatoryAttributesProps: {},
      oAssetMandatoryAttributesProps: {},
      oTargetMandatoryAttributesProps: {},
      oEditorialMandatoryAttributesProps: {},
      oClassSectionAttributeMap: {},
      oClassSectionTagMap: {},
      oClassSectionRelationshipMap: {},
      oClassSectionTaxonomyMap: {},
      sSelectedClassCategory: "class",
      sSectionAvailableElementSearchText: "",
      sSearchText: "",


      oSearchConstantData: CS.cloneDeep(DefaultPaginationData),
      iFrom: 0,
      bShowLoadMore: true,
      sTabId: "",
      aActiveTabList: [],
      selectedTabId: "",
      aModifiedPropertyList: [],
      propertyCollectionToCreate: {}
    };
  };

  let oProperties = new Props();

  return {

    getSectionAvailableElementSearchText: function(){
      return oProperties.sSectionAvailableElementSearchText;
    },

    setSectionAvailableElementSearchText: function(_sSectionAvailableElementSearchText){
      oProperties.sSectionAvailableElementSearchText = _sSectionAvailableElementSearchText;
    },

    getContentStructureFlatMap: function(){
      return oProperties.oContentStructureFlatMap;
    },

    setContentStructureFlatMap: function(_oContentStructureFlatMap){
      oProperties.oContentStructureFlatMap = _oContentStructureFlatMap;
    },

    getPropertyCollectionValueList: function () {
      oProperties.oPropertyCollectionValueList[-1].label = oTranslations().PROPERTY_COLLECTIONS;
      return oProperties.oPropertyCollectionValueList;
    },

    getClassValueListByTypeGeneric: function (sType) {
      oProperties.oPropertyCollectionValueList[-1].label = oTranslations().PROPERTY_COLLECTIONS;
      return oProperties.oPropertyCollectionValueList;
    },

    getClassImpactStateData: function () {
      return oProperties.oClassImpactStateData;
    },

    setClassImpactStateData: function (_oClassImpactStateData) {
      oProperties.oClassImpactStateData = _oClassImpactStateData;
    },

    getActivePropertyCollection: function () {
      return oProperties.oActivePropertyCollection;
    },

    setActivePropertyCollection: function (_oSection) {
      oProperties.oActivePropertyCollection = _oSection;
    },

    getPropertyCollectionScreenLockedStatus: function () {
      return oProperties.bPropertyCollectionScreenLockedStatus;
    },

    setPropertyCollectionScreenLockedStatus: function (_bValue) {
      oProperties.bPropertyCollectionScreenLockedStatus = _bValue;
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

      return oRichTextPlugins;
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
    
    getReferencedClassDetailForContentStructureById: function (sClassId) {

      return oProperties.oClassesReferredInContentStructure[sClassId];
    },

    setContentStructureCollapseAll: function (_oCollapseAllContentStructures) {
      oProperties.oCollapsedContentStructures = _oCollapseAllContentStructures;
    },

    getClassSectionAttributeMap: function () {
      return oProperties.oClassSectionAttributeMap;
    },

    setClassSectionAttributeMap: function (_oClassSectionAttributeMap) {
      oProperties.oClassSectionAttributeMap = _oClassSectionAttributeMap;
    },

    getClassSectionTagMap: function () {
      return oProperties.oClassSectionTagMap;
    },

    setClassSectionTagMap: function (_oClassSectionTagMap) {
      oProperties.oClassSectionTagMap = _oClassSectionTagMap;
    },

    getSelectedClassCategory: function () {
      return oProperties.sSelectedClassCategory;
    },

    setSelectedClassCategory: function (sClassCategory) {
      oProperties.sSelectedClassCategory = sClassCategory;
    },

    getSectionTaxonomyList: function () {
      return oProperties.oSectionTaxonomyList;
    },

    setSectionTaxonomyList: function (_oSectionTaxonomyList) {
      oProperties.oSectionTaxonomyList = _oSectionTaxonomyList;
    },

    getSearchText: function () {
      return oProperties.sSearchText;
    },

    setSearchText: function (_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getSearchConstantData: function () {
      return oProperties.oSearchConstantData;
    },

    setSearchConstantData: function (_oSearchConstantData) {
      oProperties.oSearchConstantData = _oSearchConstantData;
    },

    getFrom: function () {
      return oProperties.iFrom
    },

    setFrom: function (_iFrom) {
      oProperties.iFrom = _iFrom
    },

    getShowLoadMore: function () {
      return oProperties.showLoadMore
    },

    setShowLoadMore: function (_bShowLoadMore) {
      oProperties.showLoadMore = _bShowLoadMore
    },

    getPropertyCollectionListActiveTabId: function () {
      return oProperties.selectedTabId;
    },

    setPropertyCollectionListActiveTabId: function (sSelectedTabId) {
      oProperties.selectedTabId = sSelectedTabId;
    },

    getAvailablePropertyList: function () {
      return oProperties.aActiveTabList;
    },

    setAvailablePropertyList: function (_oActiveTabListMap, bAppend) {
      if (!bAppend) {
        oProperties.aActiveTabList = [];
      }
      oProperties.aActiveTabList = CS.concat(oProperties.aActiveTabList, _oActiveTabListMap);
    },


    getPropertyCollectionDraggableListActiveTabId: function () {
     return oProperties.sTabId
    },

    setPropertyCollectionDraggableListActiveTabId: function (sTabId) {
      oProperties.sTabId = sTabId
    },

    getPropertyCollectionDraggableModifiedPropertiesList: function () {
      return oProperties.aModifiedPropertyList
    },

    setPropertyCollectionDraggableModifiedPropertiesList: function (_aModifiedPropertyList) {
      oProperties.aModifiedPropertyList = _aModifiedPropertyList
    },

    getPropertyCollectionToCreate: function () {
      return oProperties.propertyCollectionToCreate
    },

    setPropertyCollectionToCreate: function (oPropertyCollectionToCreate) {
      oProperties.propertyCollectionToCreate = oPropertyCollectionToCreate
    },

    resetPaginationData: function () {
      oProperties.sSearchText = "";
      oProperties.oSearchConstantData = CS.cloneDeep(DefaultPaginationData);
      oProperties.iFrom = 0;
      oProperties.showLoadMore = true;
  }
}
})();
export default PropertyCollectionConfigViewProps;