import CS from '../../../../../../libraries/cs';
import ClassUtils from '../../store/helper/class-utils';
import SettingUtils from '../../store/helper/setting-utils';
import CommonUtils from '../../../../../../commonmodule/util/common-utils';
import AttributeUtils from '../../../../../../commonmodule/util/attribute-utils';

var ViewUtils = (function () {

  var _getSplitter = function () {
    return ClassUtils.getSplitter();
  };

  let _getAllKeysFromSectionLayout = (oLayoutData)=>{
    let aKeys = [];
    CS.forEach(oLayoutData, function (oSection) {
      let aElementKeys = CS.map(oSection.elements, 'key');
      aKeys = CS.concat(aElementKeys, aKeys);
    });
    return aKeys;
  };

  return {

    getSplitter: function(){
      return _getSplitter();
    },

    getIconUrl: function (sAssetKey) {
      return SettingUtils.getIconUrl(sAssetKey);
    },

    getAttributeTypeForVisual: function (sType, sAttrId) {
      return SettingUtils.getAttributeTypeForVisual(sType, sAttrId);
    },

    getUserById: function (sId) {
      return SettingUtils.getUserById(sId);
    },

    getTagById: function (sId) {
      return SettingUtils.getTagById(sId);
    },

    getUserList: function () {
      return SettingUtils.getUserList();
    },

    getTagList: function () {
      return SettingUtils.getTagList();
    },

    getTagMap: function () {
      return SettingUtils.getTagMap();
    },

    getRoleList: function () {
      return SettingUtils.getRoleList();
    },

    getRuleList: function () {
      return SettingUtils.getRuleList();
    },

    getAttributeList: function () {
      return SettingUtils.getAttributeList();
    },

    getDisplayUnitFromDefaultUnit: function (sDefaultUnit, sType) {
      return SettingUtils.getDisplayUnitFromDefaultUnit(sDefaultUnit, sType);
    },

    isAttributeTypeFile: function (sType) {
      return SettingUtils.isAttributeTypeFile(sType);
    },

    /** @deprecated **/
    isAttributeTypeType: function (sType) {
      return SettingUtils.isAttributeTypeType(sType);
    },

    isAttributeTypeTaxonomy: function (sType) {
      return SettingUtils.isAttributeTypeTaxonomy(sType);
    },

    isAttributeTypeHtml: function (sType) {
      return SettingUtils.isAttributeTypeHtml(sType);
    },

    isAttributeTypeText: function (sType) {
      return SettingUtils.isAttributeTypeText(sType);
    },

    isMeasurementAttributeTypeCustom: function (sType) {
      return SettingUtils.isMeasurementAttributeTypeCustom(sType);
    },

    isAttributeTypeCalculated: function (sType) {
      return SettingUtils.isAttributeTypeCalculated(sType);
    },

    isAttributeTypePrice: function (sType) {
      return AttributeUtils.isAttributeTypePrice(sType);
    },

    isAttributeTypeConcatenated: function (sType) {
      return SettingUtils.isAttributeTypeConcatenated(sType);
    },

    getTagDialogVisibility: function () {
      return ClassUtils.getTagDialogVisibility();
    },

    getSimpleNameFromTabBaseType: function (sBaseType) {
      return SettingUtils.getSimpleNameFromTabBaseType(sBaseType);
    },

    getNatureTypeListBasedOnClassType: function (sType, sSecondaryType) {
      return SettingUtils.getNatureTypeListBasedOnClassType(sType, sSecondaryType);
    },

    getEntitySearchText: function () {
      return SettingUtils.getEntitySearchText();
    },

    getEntityPaginationData: function(){
      return SettingUtils.getEntityPaginationData();
    },

    getReferencedAttributes: function () {
      return SettingUtils.getReferencedAttributes();
    },

    getReferencedTags: function () {
      return SettingUtils.getReferencedTags();
    },

    getReferencedRoles: function () {
      return SettingUtils.getReferencedRoles();
    },

    getLoadedAttributesData: function () {
      return SettingUtils.getLoadedAttributesData();
    },

    getLoadedRolesData: function () {
      return SettingUtils.getLoadedRolesData();
    },

    getLoadedTagsData: function () {
      return SettingUtils.getLoadedTagsData();
    },

    getEntityVsSearchTextMapping: function () {
      return SettingUtils.getEntityVsSearchTextMapping();
    },

    isOnboarding: function () {
      return SettingUtils.isOnboarding();
    },

    getTreeRootId: function () {
      return SettingUtils.getTreeRootId();
    },

    isValidEntityCode: function (sCode) {
      return SettingUtils.isValidEntityCode(sCode);
    },

    getLazyMSSViewModel: function (aSelectedItems, oReferencedData, sContextKey, oReqResObj, bIsMultiselect , aExcludedItems, bIsDisabled, bShowIcon) {
      return SettingUtils.getLazyMSSViewModel(aSelectedItems, oReferencedData, sContextKey, oReqResObj, bIsMultiselect, aExcludedItems, bIsDisabled, bShowIcon);
    },

    isGridViewScreen: function (sScreenName) {
      return SettingUtils.isGridViewScreen(sScreenName);
    },

    getKlassSelectorViewEntityTypeByClassType: function (sClassType, sRelType) {
      return SettingUtils.getKlassSelectorViewEntityTypeByClassType(sClassType, sRelType);
    },

    isVariantRelationship: function (sRelType) {
      return SettingUtils.isVariantRelationship(sRelType);
    },

    isFixedBundleRelationship: function (sRelType) {
      return SettingUtils.isFixedBundleRelationship(sRelType);
    },

    isSetOfProductsRelationship: function (sRelType) {
      return SettingUtils.isSetOfProductsRelationship(sRelType);
    },

    getConfigDataLazyRequestResponseObjectByEntityName: function (sEntityName, aEntityTypes) {
      return SettingUtils.getConfigDataLazyRequestResponseObjectByEntityName(sEntityName, aEntityTypes);
    },

    getBPMNCustomElementIDFromBusinessObject: function (oBusinessObject) {
      return SettingUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject)
    },

    getAllKeysFromSectionLayout: (oClassContextDialogLayout) => {
      return _getAllKeysFromSectionLayout(oClassContextDialogLayout);
    },

    getMultiTaxonomyData: function (aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree) {
      return SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyIds, oReferencedTaxonomiesMap, aTaxonomyTree);
    },

    getLeafNodeSubbedWithParentInfo: function (aTreeNode, sParentPropertyName, sChildPropName) {
      return SettingUtils.getLeafNodeSubbedWithParentInfo(aTreeNode, sParentPropertyName, sChildPropName);
    },

    getMaxValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return SettingUtils.getMaxValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getMinValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return SettingUtils.getMinValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getAllowedAttributeTypesForRuleEffect: function(oAttribute) {
      return SettingUtils.getAllowedAttributeTypesForRuleEffect(oAttribute);
    },

    isAttributeTypeCoverflow: function (sType) {
      return SettingUtils.isAttributeTypeCoverflow(sType);
    },

    isAttributeTypeSecondaryClasses: function (sType) {
      return SettingUtils.isAttributeTypeSecondaryClasses(sType);
    },

    isAttributeTypeMeasurement: function (sType) {
      return SettingUtils.isAttributeTypeMeasurement(sType);
    },

    isAttributeTypeNumber: function (sType) {
      return SettingUtils.isAttributeTypeNumber(sType);
    },

    isAttributeTypeCreatedOn: function (sType) {
      return SettingUtils.isAttributeTypeCreatedOn(sType);
    },

    isAttributeTypeLastModified: function (sType) {
      return SettingUtils.isAttributeTypeLastModified(sType);
    },

    isAttributeTypeUser: function (sType) {
      return SettingUtils.isAttributeTypeUser(sType);
    },

    isAttributeTypeName: function (sType) {
      return SettingUtils.isAttributeTypeName(sType);
    },

    getIdsByCode: function (aDataModel, aCode) {
      return CommonUtils.getIdsByCode(aDataModel, aCode);
    },

    getTagGroupModels: function (oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags) {
      return CommonUtils.getTagGroupModels(oMasterTag, oEntity, oElement, sModuleContext, oHierarchyData, oFilterContext, oExtraData, aFilterTags)
    },

    getSessionOrganizationId: function () {
        return SettingUtils.getSessionOrganizationId();
    },

    getDecodedTranslation: function (sStringToCompile, oParameter) {
      return SettingUtils.getDecodedTranslation(sStringToCompile, oParameter);
    },
  };

})();

export default ViewUtils;
