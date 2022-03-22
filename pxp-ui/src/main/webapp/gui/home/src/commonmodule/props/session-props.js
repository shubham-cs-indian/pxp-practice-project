
let CommonProps = (function () {

  let Props = function () {
    return {
      organizationId: "",
      physicalCatalogId: "",
      portalId: "",
      endpointId: "",
      endpointType: "",
      previousPhysicalCatalogId: "",
      bDontResetSessionPropsURLDependencyFlag: false,
      oLanguageInfoData: {},
      oActiveEndpointData: {},
      isArchive: false,
      bIsLanguageChanged: false,
    };
  };

  let oProperties = new Props();

  return {

    getSessionOrganizationId: function () {
      return oProperties.organizationId;
    },

    setSessionOrganizationId: function (_sOrganizationId) {
      oProperties.organizationId = _sOrganizationId;
    },

    getSessionPhysicalCatalogId: function () {
      return oProperties.physicalCatalogId;
    },

    setSessionPhysicalCatalogId: function (_sPhysicalCatalogId) {
      oProperties.physicalCatalogId = _sPhysicalCatalogId;
    },

    setSessionPhysicalCatalogData: function (_sPhysicalCatalogId) {
      oProperties.physicalCatalogId = _sPhysicalCatalogId;
      oProperties.physicalCatalogIdInUse = _sPhysicalCatalogId;
    },

    getSessionEndpointId: function () {
      return oProperties.endpointId;
    },

    setSessionEndpointId: function (_sEndpointId) {
      oProperties.endpointId = _sEndpointId;
    },

    getSessionEndpointType: function () {
      return oProperties.endpointType;
    },

    setSessionEndpointType: function (_sEndpointType) {
      oProperties.endpointType = _sEndpointType;
    },

    getSessionPreviousPhysicalCatalogId: function () {
      return oProperties.previousPhysicalCatalogId;
    },

    setSessionPreviousPhysicalCatalogId: function (_sPreviousPhysicalCatalogId) {
      oProperties.previousPhysicalCatalogId = _sPreviousPhysicalCatalogId;
    },

    getDontResetSessionPropsURLDependencyFlag: function () {
      return oProperties.bDontResetSessionPropsURLDependencyFlag;
    },

    setDontResetSessionPropsURLDependencyFlag: function (bFlag) {
      oProperties.bDontResetSessionPropsURLDependencyFlag = bFlag;
    },

    getLanguageInfoData: function(){
      return oProperties.oLanguageInfoData;
    },

    setLanguageInfoData: function(_oLanguageInfoData){
      oProperties.oLanguageInfoData = _oLanguageInfoData;
    },

    getActiveEndpointData: function () {
      return oProperties.oActiveEndpointData;
    },

    setActiveEndpointData: function (_oActiveEndpointData) {
      oProperties.oActiveEndpointData = _oActiveEndpointData;
    },

    getIsArchive: function () {
      return oProperties.isArchive;
    },

    setIsArchive: function (_isArchive) {
      oProperties.isArchive = _isArchive;
    },

    getIsLanguageChanged: function () {
      return oProperties.bIsLanguageChanged;
    },

    setIsLanguageChanged: function (_bIsLanguageChanged) {
      oProperties.bIsLanguageChanged = _bIsLanguageChanged;
    },

    reset: function () {
      oProperties = new Props();
    },

  };

})();

export default CommonProps;