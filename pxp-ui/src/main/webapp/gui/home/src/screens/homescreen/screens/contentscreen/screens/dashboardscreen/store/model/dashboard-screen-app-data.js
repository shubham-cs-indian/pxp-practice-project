import ContentScreenAppData from './../../../../store/model/content-screen-app-data'; //todo: if a better solution is available please use it.

let DashboardScreenAppData = (function () {

  let Props = function () {

    return {
      aDashboardGovernanceData: [],
      aDashboardIntegrationData: [],
      oReferencedKPI: {},
      oReferencedTags: {},
      oReferencedTaxonomies: {},
      oReferencedClasses: {}
    }
  };

  let oProperties = new Props();

  return {

    getDashboardGovernanceData: function () {
      return oProperties.aDashboardGovernanceData;
    },

    setDashboardGovernanceData: function (_aDashboardData) {
      oProperties.aDashboardGovernanceData = _aDashboardData;
    },

    getDashboardIntegrationData: function () {
      return oProperties.aDashboardIntegrationData;
    },

    setDashboardIntegrationData: function (_aDashboardData) {
      oProperties.aDashboardIntegrationData = _aDashboardData;
    },

    getReferencedKPI: function () {
      return oProperties.oReferencedKPI;
    },

    setReferencedKPI: function (_oReferencedKPI) {
      oProperties.oReferencedKPI = _oReferencedKPI;
    },

    getReferencedTags: function () {
      return ContentScreenAppData.getDashboardReferencedTags();
    },

    setReferencedTags: function (_oReferencedTags) {
      ContentScreenAppData.setDashboardReferencedTags(_oReferencedTags);
    },

    getReferencedTaxonomies: function () {
      return oProperties.oReferencedTaxonomies;
    },

    setReferencedTaxonomies: function (_oReferencedTaxonomies) {
      oProperties.oReferencedTaxonomies = _oReferencedTaxonomies;
    },

    getReferencedClasses: function () {
      return oProperties.oReferencedClasses;
    },

    setReferencedClasses: function (_oReferencedClasses) {
      oProperties.oReferencedClasses = _oReferencedClasses;
    },

    reset: function () {
      oProperties = new Props();
      ContentScreenAppData.oDashboardReferencedTags = {};
    },

    toJSON: function () {
      return {
      };
    },

  }

})();

export default DashboardScreenAppData;