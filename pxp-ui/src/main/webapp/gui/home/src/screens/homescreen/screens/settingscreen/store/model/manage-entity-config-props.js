/**
 * Created by DEV on 29-07-2015.
 */
let ManageEntityConfigProps = (function () {

  let Props = function () {
    return {
      bIsEntityActive: false,
      DataProducts: [],
      bIsEntityUsagePresent: false,
      bIsDelete: false
    }
  };

  let oProperties = new Props();

  return {
    getIsEntityDialogOpen: function () {
      return oProperties.bIsEntityActive;
    },

    setIsEntityDialogOpen: function (bIsActive) {
      oProperties.bIsEntityActive = bIsActive;
    },
    getDataForDialog: function () {
     return oProperties.DataProducts
    },

    setDataForDialog: function (mockData) {
      oProperties.DataProducts = mockData;
    },

    getDataForDeleteEntity: function () {
      return oProperties.bIsEntityUsagePresent;
    },

    setDataForDeleteEntity: function (bIsEntityUsagePresent) {
      oProperties.bIsEntityUsagePresent = bIsEntityUsagePresent;
    },

    getIsDelete () {
      return oProperties.bIsDelete;
    },

    setIsDelete(bIsDelete) {
      oProperties.bIsDelete = bIsDelete;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {
        oSelectedAttribute: oProperties.oSelectedAttribute,
        oMultiValuedListObject: oProperties.oMultiValuedListObject,
        oMSSProp:  oProperties.oMSSProp,
        aAttributeGridData: oProperties.aAttributeGridData
      };
    }
  }
})();

export default ManageEntityConfigProps;