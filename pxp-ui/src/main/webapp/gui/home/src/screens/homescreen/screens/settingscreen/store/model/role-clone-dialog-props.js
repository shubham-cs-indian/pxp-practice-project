let RoleCloneDialogViewProps = (function () {

  let Props = function () {
    return {
      oData: {
        sLabel: "",
        sCode: "",
        bIsExactClone: true,
        bIsClonePhysicalCatalog: true,
        bIsCloneTaxonomies: true,
        bIsCloneTargetClasses: true,
        bIsCloneEnableDashboard: true,
        bIsCloneKPI: true,
        bIsCloneEntities: true
      },
      oSelectedRole: {},
      oReferencedTaxonomies: {},
      aReferencedKlasses: [],
      oReferencedKPIs: {},
      bIsRoleCloneDialogActive: false
    };
  };

  let oProperties = new Props();

  return {

    getIsRoleCloneDialogActive: function () {
      return oProperties.bIsRoleCloneDialogActive;
    },

    setIsRoleCloneDialogActive: function (bIsRoleCloneDialogActive) {
      oProperties.bIsRoleCloneDialogActive = bIsRoleCloneDialogActive;
    },

    getSelectedRole: function () {
      return oProperties.oSelectedRole;
    },

    setSelectedRole: function (_oRole) {
      oProperties.oSelectedRole = _oRole;
    },

    getReferencedTaxonomies: function () {
      return oProperties.oReferencedTaxonomies;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.oReferencedTaxonomies = _oReferencedTaxonomies;
    },

    getReferencedKlasses: function () {
      return oProperties.aReferencedKlasses;
    },

    setReferencedKlasses: function (_aReferencedKlasses) {
      oProperties.aReferencedKlasses = _aReferencedKlasses;
    },

    getReferencedKPIs: function () {
      return oProperties.oReferencedKPIs;
    },

    setReferencedKPIs: function (_oReferencedKPIs) {
      oProperties.oReferencedKPIs = _oReferencedKPIs;
    },

    setLabel: function (sLabel) {
      oProperties.oData.sLabel = sLabel;
    },

    setCode: function (sCode) {
      oProperties.oData.sCode = sCode;
    },

    getIsExactCloneSelected: function () {
      return oProperties.oData.bIsExactClone;
    },

    setIsExactCloneSelected: function (bIsExactCloneSelected) {
      oProperties.oData.bIsExactClone = bIsExactCloneSelected;
    },

    setIsClonePhysicalCatalogSelected: function (bIsClonePhysicalCatalog) {
      oProperties.oData.bIsClonePhysicalCatalog = bIsClonePhysicalCatalog;
    },

    setIsCloneTaxonomiesSelected: function (bIsCloneTaxonomies) {
      oProperties.oData.bIsCloneTaxonomies = bIsCloneTaxonomies;
    },

    setIsCloneTargetClassesSelected: function (bIsCloneTargetClasses) {
      oProperties.oData.bIsCloneTargetClasses = bIsCloneTargetClasses;
    },

    setIsCloneEnableDashboardSelected: function (bIsCloneEnableDashboard) {
      oProperties.oData.bIsCloneEnableDashboard = bIsCloneEnableDashboard;
    },

    setIsCloneKPISelected: function (bIsCloneKPI) {
      oProperties.oData.bIsCloneKPI = bIsCloneKPI;
    },

    setIsCloneEntitiesSelected: function (bIsCloneEntities) {
      oProperties.oData.bIsCloneEntities = bIsCloneEntities;
    },

    getRoleCloneDialogData: function () {
      return oProperties.oData;
    },

    reset: function () {
      oProperties = new Props();
    }
  }
})();

export default RoleCloneDialogViewProps;