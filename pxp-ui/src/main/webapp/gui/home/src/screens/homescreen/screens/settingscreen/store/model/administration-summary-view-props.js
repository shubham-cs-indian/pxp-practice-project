
let AdministrationSummaryViewProps = (function () {
  let Props = function () {
    return {
      administrationSummaryData: [],
    }
  };

  let oProperties = new Props();

  return {

    getAdministrationItemData: function () {
      return oProperties.administrationSummaryData;
    },

    setAdministrationItemData: function (aAdministrationSummaryData) {
      oProperties.administrationSummaryData = aAdministrationSummaryData;
    },

  }
})();

export default AdministrationSummaryViewProps;