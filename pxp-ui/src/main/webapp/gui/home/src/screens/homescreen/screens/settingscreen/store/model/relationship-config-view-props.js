import CS from '../../../../../../libraries/cs';

/**
 * Created by DEV on 29-07-2015.
 */

import DefaultPaginationData from '../../tack/mock/default-pagination-data';

var RelationshipConfigViewProps = (function () {

  let Props = function () {
    return {
      oRelationshipValueList: {},
      aRelationshipGridData:[],
      oSelectedRelationship: {},
      oSelectedRelationshipConfigDetails:{
        referencedKlasses:{}
      },
      bRelationshipScreenLockStatus: false,
      oAllKlassesForRelationship: {},
      oKlassesForRelationship: {
        side1: {},
        side2: {}
      },
      sSearchText: "",
      oSearchConstantData: CS.cloneDeep(DefaultPaginationData),
      iFrom: 0,
      oContextData: {},
      bIsRelationshipDialogActive: false,
    };
  };

  let oProperties = new Props();

  /*var oRelationshipClass = {};
  var sRelationshipName = '';
  var sRelationshipType = '';
  var oRelationshipVisibility = {};
  var sCardinality = ''; //changes may be needed vivek
  var bIsInheritable = false;
  var oRelationshipAlias = {};
  var oMultiSelectProps = {
    relationshipConfigFromClass: {
      aFilteredList: [],
      bIsActive: false
    },
    relationshipConfigToClass: {
      aFilteredList: [],
      bIsActive: false
    }
  };*/

  return {

    getRelationshipValuesList: function () {
      return oProperties.oRelationshipValueList;
    },

    setRelationshipValuesList: function (_oRelationshipValueList) {
      oProperties.oRelationshipValueList = _oRelationshipValueList;
    },

    getRelationshipScreenLockStatus: function () {
      return oProperties.bRelationshipScreenLockStatus;
    },

    setRelationshipScreenLockStatus: function (bStatus) {
      oProperties.bRelationshipScreenLockStatus = bStatus;
    },

    getSelectedRelationship: function () {
      return oProperties.oSelectedRelationship;
    },

    setSelectedRelationship: function (_oSelectedRelationship) {
      oProperties.oSelectedRelationship = _oSelectedRelationship;
    },

    getKlassesForRelationships : function () {
      return oProperties.oKlassesForRelationship;
    },

    setKlassesForRelationships : function (_oKlassesForRelationship) {
      oProperties.oKlassesForRelationship = _oKlassesForRelationship;
    },

    getAllKlassesForRelationships : function () {
      return CS.cloneDeep(oProperties.oAllKlassesForRelationship);
    },

    setAllKlassesForRelationships : function (_oAllKlassesForRelationship) {
      oProperties.oAllKlassesForRelationship = _oAllKlassesForRelationship;
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

    getContextData: function () {
      return oProperties.oContextData;
    },

    setContextData: function (_oContextData) {
      oProperties.oContextData = _oContextData;
    },

    resetPaginationData: function () {
      oProperties.sSearchText = "";
      oProperties.oSearchConstantData = CS.cloneDeep(DefaultPaginationData);
      oProperties.iFrom = 0;
    },

    getIsRelationshipDialogActive: function () {
      return oProperties.bIsRelationshipDialogActive;
    },

    setIsRelationshipDialogActive: function (_bIsRelationshipDialogActive) {
      oProperties.bIsRelationshipDialogActive = _bIsRelationshipDialogActive;
    },

    getActiveRelationship: function () {
      return oProperties.oActiveRelationship;
    },

    setActiveRelationship: function (_oActiveRelationship) {
      oProperties.oActiveRelationship = _oActiveRelationship;
    },

    getSelectedRelationshipConfigDetails: function(){
      return oProperties.oSelectedRelationshipConfigDetails;
    },

    setSelectedRelationshipConfigDetails: function(_oSelectedRelationshipConfigDetails){
      oProperties.oSelectedRelationshipConfigDetails = _oSelectedRelationshipConfigDetails;
    },

    getRelationshipGridData: function () {
      return oProperties.aRelationshipGridData;
    },

    setRelationshipGridData: function (_aRelationshipGridData) {
      oProperties.aRelationshipGridData = _aRelationshipGridData;
    },

    reset: function () {
      oProperties = new Props();
    },


  /*  setRelationshipClass: function (sClassId, sContext) {
      oRelationshipClass[sContext] = sClassId;
    },

    getRelationshipClass: function () {
      return oRelationshipClass;
    },

    setRelationshipName: function (_sRelationshipName) {
      sRelationshipName = _sRelationshipName;
    },

    getRelationshipName: function () {
      return sRelationshipName;
    },

    setRelationshipType: function (_sRelationshipType) {
      sRelationshipType = _sRelationshipType;
    },

    getRelationshipType: function () {
      return sRelationshipType;
    },

    setRelationshipVisibility: function (sContext, bIsChecked) {
      oRelationshipVisibility[sContext] = bIsChecked;
    },

    getRelationshipVisibility: function () {
      return oRelationshipVisibility;
    },

    setRelationshipCardinality: function (_sCardinality) {
      sCardinality = _sCardinality;
    },

    getRelationshipCardinality: function () {
      return sCardinality;
    },

    setRelationshipIsInheritable: function (_bIsInheritable) {
      bIsInheritable = _bIsInheritable;
    },

    getRelationshipIsInheritable: function () {
      return bIsInheritable;
    },

    getMultiSelectProps: function (sContextKey) {

      return oMultiSelectProps[sContextKey];

    },

    setMultiSelectProps: function (sContextKey, oProps) {

      oMultiSelectProps[sContextKey] = oProps;

    },

    getRelationshipAlias: function () {
      return oRelationshipAlias;
    },


    setRelationshipAlias: function (sContext, bIsChecked) {
      oRelationshipAlias[sContext] = bIsChecked;
    }

*/
  }
})();

export default RelationshipConfigViewProps;