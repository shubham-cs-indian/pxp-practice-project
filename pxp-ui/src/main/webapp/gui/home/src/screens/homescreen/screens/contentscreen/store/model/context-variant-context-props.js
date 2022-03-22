/**
 * Created by CS109 on 18-08-2017.
 */

var ContextVariantContextProps = function () {

  let oVariantContext = {};
  let oSelectedContext = {};
  let oSelectedVisibleContext = {};
  let oDummyVariant = {};
  let oActiveVariantForEditing = {};
  let sDialogOpenContext = ""; //edit OR create
  let bIsVariantDialogOpen = false;
  let bEditVariantTags = true; //TODO
  let oActiveVariantEntity = {};
  let oReferencedVariantContexts = {};
  let oReferencedTags = {};
  let oReferencedRoles = {};
  let oReferencedRelationships = {};
  let aSelectedContextVariantSections = [];
  let oReferencedPermissions = {};
  let oReferencedElements = {};
  let oReferencedPropertyCollections = {};
  let oReferencedAttributes = {};
  let oReferencedTaxonomies = {};
  let oReferencedInstances = {};
  let oReferencedTemplate = {};
  let oInstanceIdVsReferencedElements = {};

  return {
    getVariantContext: function () {
      return oVariantContext;
    },

    getSelectedContext: function () {
      return oSelectedContext;
    },

    setSelectedContext: function (_oSelectedContext) {
      oSelectedContext = _oSelectedContext;
    },

    getSelectedVisibleContext: function () {
      return oSelectedVisibleContext;
    },

    setSelectedVisibleContext: function (_oSelectedVisibleContext) {
      oSelectedVisibleContext = _oSelectedVisibleContext;
    },

    getDummyVariant: function () {
      return oDummyVariant;
    },

    setDummyVariant: function (_oDummyVariant) {
      oDummyVariant = _oDummyVariant;
    },

    getActiveVariantForEditing: function () {
      return oActiveVariantForEditing;
    },

    setActiveVariantForEditing: function (_oActiveVariantForEditing) {
      oActiveVariantForEditing = _oActiveVariantForEditing;
    },

    getVariantDialogOpenContext: function () {
      return sDialogOpenContext;
    },

    setVariantDialogOpenContext: function (_sDialogOpenContext) {
      sDialogOpenContext = _sDialogOpenContext;
    },

    getIsVariantDialogOpen: function () {
      return bIsVariantDialogOpen;
    },

    setIsVariantDialogOpen: function (_bIsOpen) {
      bIsVariantDialogOpen = _bIsOpen;
    },

    getEditVariantTags: function () {
      return bEditVariantTags;
    },

    setEditVariantTags: function (_bEditVariantTags) {
      bEditVariantTags = _bEditVariantTags;
    },

    getActiveVariantEntity: function () {
      return oActiveVariantEntity;
    },

    getReferencedVariantContexts: function () {
      return oReferencedVariantContexts;
    },

    setReferencedVariantContexts: function (_oReferencedVariantContexts) {
      oReferencedVariantContexts = _oReferencedVariantContexts;
    },

    getReferencedTags: function () {
      return oReferencedTags;
    },

    setReferencedTags: function (_oReferencedTags) {
      oReferencedTags = _oReferencedTags;
    },

    getSelectedContextVariantSections: function () {
      return aSelectedContextVariantSections;
    },

    setSelectedContextVariantSections: function (aSections) {
      aSelectedContextVariantSections = aSections;
    },

    getReferencedPermissions: function () {
      return oReferencedPermissions;
    },

    setReferencedPermissions: function (_oReferencedPermissions) {
      oReferencedPermissions = _oReferencedPermissions
    },

    getReferencedElements: function () {
      return oReferencedElements;
    },

    setReferencedElements: function (_oReferencedElements) {
      oReferencedElements = _oReferencedElements;
    },

    getReferencedPropertyCollections: function () {
      return oReferencedPropertyCollections;
    },

    setReferencedPropertyCollections: function (_oSections) {
      oReferencedPropertyCollections = _oSections;
    },

    getReferencedAttributes: function () {
      return oReferencedAttributes;
    },

    setReferencedAttributes: function (_oReferencedAttributes) {
      oReferencedAttributes = _oReferencedAttributes;
    },

    getReferencedTaxonomies: function () {
      return oReferencedTaxonomies;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oReferencedTaxonomies = _oReferencedTaxonomies;
    },

    getReferencedInstances: function () {
      return oReferencedInstances;
    },

    setReferencedInstances: function (_oReferencedInstances) {
      oReferencedInstances = _oReferencedInstances;
    },

    getReferencedTemplate: function () {
      return oReferencedTemplate;
    },

    setReferencedTemplate: function (_oReferencedTemplate) {
      oReferencedTemplate = _oReferencedTemplate;
    },

    getReferencedRoles: function () {
      return oReferencedRoles;
    },

    setReferencedRoles: function (_oReferencedRoles) {
      oReferencedRoles = _oReferencedRoles;
    },

    getReferencedRelationships: function () {
      return oReferencedRelationships;
    },

    setReferencedRelationships: function (_oReferencedRelationships) {
      oReferencedRelationships = _oReferencedRelationships;
    },

    getInstanceIdVsReferencedElements: function () {
      return oInstanceIdVsReferencedElements;
    },

    setInstanceIdVsReferencedElements: function (_oInstanceIdVsReferencedElements) {
      oInstanceIdVsReferencedElements = _oInstanceIdVsReferencedElements;
    },

  }
};

export default ContextVariantContextProps;
