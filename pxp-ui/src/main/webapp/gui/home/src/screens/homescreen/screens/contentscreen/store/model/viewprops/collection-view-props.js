/**
 * Created by CS56 on 10/4/2016.
 */

import EntityViewProps from './entity-view-props';

var CollectionViewProps = function () {
  this.inheritsFrom(EntityViewProps);
  this.className = "CollectionViewProps";

  var oActiveCollection = {};
  var oBreadCrumb = {
    treePath: []
  };
  var bPublicPrivateModeButtonVisibility = true;
  var bAddEntityInCollectionViewStatus = false;
  var bIsEditCollectionScreen = false;
  let activeCollectionFilter = "";
  let bookmarkModuleId = "";
  let appliedTaxonomyCloneData = {};

  this.setActiveCollectionFilter = function (sActiveCollectionFilter) {
    activeCollectionFilter = sActiveCollectionFilter;
  }

  this.getBreadCrumb =  function(){
    return oBreadCrumb;
  };

  this.setBreadCrumbPath = function (_aTreePath) {
    oBreadCrumb.treePath = _aTreePath;
  };

  this.getActiveCollection = function () {
    return oActiveCollection;
  };

  this.setActiveCollection = function (_oActiveCollection) {
    oActiveCollection = _oActiveCollection;
  };

  this.setAddEntityInCollectionViewStatus = function (bVal) {
    bAddEntityInCollectionViewStatus = bVal;
  };

  this.getAddEntityInCollectionViewStatus = function () {
    return bAddEntityInCollectionViewStatus;
  };

  this.getPublicPrivateModeButtonVisibility = function () {
    return bPublicPrivateModeButtonVisibility;
  };

  this.setPublicPrivateModeButtonVisibility = function (_bPublicPrivateModeButtonVisibility) {
    bPublicPrivateModeButtonVisibility = _bPublicPrivateModeButtonVisibility;
  };

  this.getIsEditCollectionScreen = function () {
    return bIsEditCollectionScreen;
  };

  this.setIsEditCollectionScreen = function (_bEditCollectionScreen) {
    bIsEditCollectionScreen = _bEditCollectionScreen;
  };

  this.getBookmarkModuleId  = function () {
    return bookmarkModuleId;
  };

  this.setBookmarkModuleId = function (_sBookmarkModuleId) {
    bookmarkModuleId  = _sBookmarkModuleId;
  };

  this.getAppliedTaxonomyCloneData  = function () {
    return appliedTaxonomyCloneData;
  };

  this.setAppliedTaxonomyCloneData = function (oAppliedTaxonomyCloneData) {
    appliedTaxonomyCloneData = oAppliedTaxonomyCloneData;
  };


};

export default new CollectionViewProps();
