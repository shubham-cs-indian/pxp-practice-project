var VariantSectionViewProps = (function () {

  var Props = function () {
    return {
      oSelectedContext: {},
      oSelectedVisibleContext: {},
      oDummyVariant: {},
      //bEditVariantTags: false,
      bScrollToActiveVariant: false,
      oEditLinkedVariantInstance: {},
      oDummyLinkedVariant: {},
      /******* Grid Props ********/
      oGridViewSkeleton: {
        fixedColumns: [],
        scrollableColumns: [],
        actionItems: [],
        selectedContentIds: []
      },
      aGridViewData: [],
      oGridViewPaginationData: {
        from: 0,
        pageSize: 20,
        totalItems: 0
      },
      iGridViewTotalItems: 0,
      sGridViewSearchText: "",
      sGridViewSortBy: "",
      sGridViewSortOrder: "",
      bIsGridDataDirty: false,
      oActiveCellDetails: {
        dataId: '',
        columnId: '',
      },
      /****** Grid Props end ******/

      bAvailableEntityChildVariantViewVisibilityStatus: false,
      bAvailableEntityParentVariantViewVisibilityStatus: false,
      sSelectedEntity: "",

      isVariantDialogOpen: false,

      activeVariantForEditing: {},
      iUserDateRangeStartDate: null,
      iUserDateRangeEndDate: null,
      bIsUserDateRangeApplied: false,
      bIsCurrentDate: null,
      isCloningWizardOpen: false,
      cloningWizardViewData: {},
      cloneWizardDialogContext: "",
      isSelectAllForCloningWizard: false,
      oCloneWizardContextPropertyMap: {},
      sVariantDialogOpenContext: /*"edit" or "create"*/"",
      aSelectedVariantInstance: [],
      iCloneCount: 1,
      sCloningContentId: "",
      oSelectedPropertiesDataForCloning: {},
      bIsExactCloneSelected: true,
      side2NatureClassOfProductVariant: {},
      aAllowedTypesToCreateLinkedVariant: []
    }
  };

  var oProperties = new Props();

  return {
    getAllowedTypesToCreateLinkedVariant: function () {
      return oProperties.aAllowedTypesToCreateLinkedVariant;
    },

    setAllowedTypesToCreateLinkedVariant: function (_aAllowedTypesToCreateLinkedVariant) {
      oProperties.aAllowedTypesToCreateLinkedVariant = _aAllowedTypesToCreateLinkedVariant;
    },

    getSide2NatureClassOfProductVariant: function () {
      return oProperties.side2NatureClassOfProductVariant;
    },

    setSide2NatureClassOfProductVariant: function (_side2NatureClassOfProductVariant) {
      oProperties.side2NatureClassOfProductVariant = _side2NatureClassOfProductVariant;
    },

    getIsCurrentDateSelected: function () {
      return oProperties.bIsCurrentDate;
    },

    getUserDateRangeStartDate: function () {
      return oProperties.iUserDateRangeStartDate;
    },

    getUserDateRangeEndDate: function () {
      return oProperties.iUserDateRangeEndDate;
    },

    getIsUserDateRangeApplied: function () {
      return oProperties.bIsUserDateRangeApplied;
    },

    getIsVariantDialogOpen: function () {
      return oProperties.isVariantDialogOpen;
    },

    setIsVariantDialogOpen: function (_bIsOpen) {
      oProperties.isVariantDialogOpen = _bIsOpen;
    },

    getSelectedContext: function () {
      return oProperties.oSelectedContext;
    },

    setSelectedContext: function (_oSelectedContext) {
      oProperties.oSelectedContext = _oSelectedContext;
    },

    /****
     * @deprecated
     * @returns {oSelectedVisibleContext|{}}
     */
    getSelectedVisibleContext: function () {
      return oProperties.oSelectedVisibleContext;
    },

    getDummyVariant: function () {
      return oProperties.oDummyVariant;
    },

    setDummyVariant: function (_oDummyVariant) {
      oProperties.oDummyVariant = _oDummyVariant;
    },

    /**
     * @deprecated
      * **/
    getEditLinkedVariantInstance: function (){
      return oProperties.oEditLinkedVariantInstance;
    },

    /**
     * @deprecated
     * **/
    setEditLinkedVariantInstance: function (oLinkedVariantElement){
      oProperties.oEditLinkedVariantInstance = oLinkedVariantElement;
    },

    getDummyLinkedVariant: function (){
      return oProperties.oDummyLinkedVariant;
    },

    setDummyLinkedVariant: function (oLinkedVariantElement){
      oProperties.oDummyLinkedVariant = oLinkedVariantElement;
    },

    /******* Grid Related Props ******/

    getGridViewSkeleton : function () {
      return oProperties.oGridViewSkeleton;
    },

    setGridViewData: function (_aGridViewData) {
      oProperties.aGridViewData = _aGridViewData;
    },

    getGridViewData : function () {
      return oProperties.aGridViewData;
    },

    setGridViewPaginationData: function (_oGridViewPaginationData) {
      oProperties.oGridViewPaginationData = _oGridViewPaginationData;
    },

    getGridViewPaginationData : function () {
      return oProperties.oGridViewPaginationData;
    },

    setGridViewTotalItems: function (_iGridViewTotalItems) {
      oProperties.iGridViewTotalItems = _iGridViewTotalItems;
    },

    getGridViewTotalItems : function () {
      return oProperties.iGridViewTotalItems;
    },

    setGridViewSearchText: function (_sGridViewSearchText) {
      oProperties.sGridViewSearchText = _sGridViewSearchText;
    },

    getGridViewSearchText : function () {
      return oProperties.sGridViewSearchText;
    },

    setGridViewSortBy: function (_sGridViewSortBy) {
      oProperties.sGridViewSortBy = _sGridViewSortBy;
    },

    getGridViewSortBy : function () {
      return oProperties.sGridViewSortBy;
    },

    setGridViewSortOrder: function (_sGridViewSortOrder) {
      oProperties.sGridViewSortOrder = _sGridViewSortOrder;
    },

    getGridViewSortOrder : function () {
      return oProperties.sGridViewSortOrder;
    },

    setIsGridDataDirty: function (_bIsGridDataDirty) {
      oProperties.bIsGridDataDirty = _bIsGridDataDirty;
    },

    getIsGridDataDirty: function () {
      return oProperties.bIsGridDataDirty;
    },

    /******** Grid Related Props end**************/

    getAvailableEntityChildVariantViewVisibilityStatus: function (){
      return oProperties.bAvailableEntityChildVariantViewVisibilityStatus;
    },

    setAvailableEntityChildVariantViewVisibilityStatus: function (_bAvailableEntityViewVisibilityStatus){
      oProperties.bAvailableEntityChildVariantViewVisibilityStatus = _bAvailableEntityViewVisibilityStatus;
    },

    getAvailableEntityParentVariantViewVisibilityStatus: function (){
      return oProperties.bAvailableEntityParentVariantViewVisibilityStatus;
    },

    setAvailableEntityParentVariantViewVisibilityStatus: function (_bAvailableEntityViewVisibilityStatus){
      oProperties.bAvailableEntityParentVariantViewVisibilityStatus = _bAvailableEntityViewVisibilityStatus;
    },

    getSelectedEntity: function () {
      return oProperties.sSelectedEntity;
    },

    setSelectedEntity: function (sSelectedEntity) {
      oProperties.sSelectedEntity = sSelectedEntity;
    },

    /****
     * @deprecated
     * @returns {activeVariantForEditing|{}}
     */
    getActiveVariantForEditing: function () {
      return oProperties.activeVariantForEditing;
    },

    getIsCloningWizardOpen: function () {
      return oProperties.isCloningWizardOpen;
    },

    setIsCloningWizardOpen: function (_isCloningWizardOpen) {
      oProperties.isCloningWizardOpen = _isCloningWizardOpen;
    },

    getCloningWizardViewData: function () {
      return oProperties.cloningWizardViewData;
    },

    setCloningWizardViewData: function (_cloningWizardViewData) {
      oProperties.cloningWizardViewData = _cloningWizardViewData;
    },

    getSelectedEntityIdsForCloning: function () {
      return oProperties.oSelectedPropertiesDataForCloning;
    },

    setSelectedEntityIdsForCloning: function (_oSelectedPropertiesDataForCloning) {
      oProperties.oSelectedPropertiesDataForCloning = _oSelectedPropertiesDataForCloning;
    },

    getIsExactCloneSelected: function () {
      return oProperties.bIsExactCloneSelected;
    },

    setIsExactCloneSelected: function (_bIsExactCloneSelected) {
      oProperties.bIsExactCloneSelected = _bIsExactCloneSelected;
    },


    getCloneWizardDialogContext: function () {
      return oProperties.cloneWizardDialogContext;
    },

    setCloneWizardDialogContext: function (_cloneWizardDialogContext) {
      oProperties.cloneWizardDialogContext = _cloneWizardDialogContext;
    },

    setIsSelectAllForCloningWizard: function (_isSelectAllForCloningWizard) {
      oProperties.isSelectAllForCloningWizard = _isSelectAllForCloningWizard;
    },

    getCloneWizardContextPropertyMap: function () {
      return oProperties.oCloneWizardContextPropertyMap;
    },

    setCloneWizardContextPropertyMap: function(_oCloneWizardContextPropertyMap){
      oProperties.oCloneWizardContextPropertyMap = _oCloneWizardContextPropertyMap;
    },

    getSelectedVariantInstances: function () {
      return oProperties.aSelectedVariantInstance;
    },

    setSelectedVariantInstances: function (aSelectedVariantInstance) {
      oProperties.aSelectedVariantInstance = aSelectedVariantInstance;
    },
    /**
     * @deprecated
     * */
    getVariantDialogOpenContext: function () {
      return oProperties.sVariantDialogOpenContext;
    },

    setVariantDialogOpenContext: function (sContext) {
      oProperties.sVariantDialogOpenContext= sContext;
    },

    getCloningWizardCloneCount: function () {
      return oProperties.iCloneCount;
    },

    setCloningWizardCloneCount: function (_iCloneCount) {
      oProperties.iCloneCount = _iCloneCount;
    },

    getCloningWizardContentId: function () {
      return oProperties.sCloningContentId;
    },

    setCloningWizardContentId: function (_sCloningContentId) {
      oProperties.sCloningContentId = _sCloningContentId;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {

      }
    }

  };

})();

export default VariantSectionViewProps;
