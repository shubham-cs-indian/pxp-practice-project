import ScreenModeUtils from '../../store/helper/screen-mode-utils';
import MultiClassificationDialogToolbarLayoutData from "../../tack/multiclassification-dialog-toolbar-layout-data";
let MultiClassificationViewTypesIds = new MultiClassificationDialogToolbarLayoutData()["multiClassificationViewTypesIds"];

let MultiClassificationProps = (function () {

  let fProps = function () {
    return {
      bShowClassificationDialog: false,
      sSelectedTabInClassificationDialog: MultiClassificationViewTypesIds.CLASSES,
      oMultiClassificationData: {},
      aMultiClassificationTree: [],
      oReferencedTaxonomyMap: {},
      sSearchText: "",
      oTreeLoadMoreMap: {},
      oTaxonomyInheritance: {
        bShowTaxonomyInheritanceDialog: false,
        oContentIdVsTypesTaxonomies: {},
      }
    }
  };

  var oProperties = new fProps();

  return {
    getMultiClassificationData: function () {
      return oProperties.oMultiClassificationData;
    },

    setMultiClassificationData: function (_oMultiClassificationData) {
      oProperties.oMultiClassificationData = _oMultiClassificationData;
    },

    getIsShowClassificationDialog: function () {
      return oProperties.bShowClassificationDialog;
    },

    setIsShowClassificationDialog: function (_bShowClassificationDialog) {
      oProperties.bShowClassificationDialog = _bShowClassificationDialog;
    },

    getSelectedTabInClassificationDialog: function () {
      return oProperties.sSelectedTabInClassificationDialog;
    },

    setSelectedTabInClassificationDialog: function (_sSelectedTabInClassificationDialog) {
      oProperties.sSelectedTabInClassificationDialog = _sSelectedTabInClassificationDialog;
    },

    getMultiClassificationTreeSearchText: function () {
      return oProperties.sSearchText;
    },

    setMultiClassificationTreeSearchText: function (_sSearchText) {
      oProperties.sSearchText = _sSearchText;
    },

    getReferencedTaxonomyMap: function () {
      return oProperties.oReferencedTaxonomyMap;
    },

    setReferencedTaxonomyMap: function (_oReferencedTaxonomyMap) {
      oProperties.oReferencedTaxonomyMap = _oReferencedTaxonomyMap;
    },

    setTaxonomyInheritanceDialog: function (_bShowTaxonomyInheritanceDialog) {
      oProperties.oTaxonomyInheritance.bShowTaxonomyInheritanceDialog = _bShowTaxonomyInheritanceDialog;
    },

    getTaxonomyInheritanceDialog: function () {
      return oProperties.oTaxonomyInheritance.bShowTaxonomyInheritanceDialog;
    },

    setContentIdVsTypesTaxonomies: function(_oContentIdVsTypesTaxonomies) {
      oProperties.oTaxonomyInheritance.oContentIdVsTypesTaxonomies = _oContentIdVsTypesTaxonomies;
    },

    getContentIdVsTypesTaxonomies: function() {
      return oProperties.oTaxonomyInheritance.oContentIdVsTypesTaxonomies;
    },

    setEntityParentContentId: function(_sEntityParentContentId) {
      oProperties.oTaxonomyInheritance.sEntityParentContentId = _sEntityParentContentId;
    },

    getEntityParentContentId: function() {
      return oProperties.oTaxonomyInheritance.sEntityParentContentId;
    },

    resetTaxonomyInheritanceProps: function() {
      oProperties.oTaxonomyInheritance = {
        bShowTaxonomyInheritanceDialog: false,
        oContentIdVsTypesTaxonomies: {}
      }
    },

    //Add functions above reset function
    reset: function () {
      oProperties = new fProps();
    }
  };


})();

export default MultiClassificationProps;
