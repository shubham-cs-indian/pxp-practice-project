let BulkEditProcessFilterProps = (function () {

  let Props = function () {
    return {

      bIsDirty: false,
      bIsBulkEditViewOpen: false,
      oAppliedDataObjects: {
        attributes: [],
        tags: []
      },
      aAppliedAttributeList: [],
      aAppliedTagsList: [],
      aPropertySequence: []
    }
  };

  var oProperties = new Props();

  return {

    getIsBulkEditViewOpen: function () {
      return oProperties.bIsBulkEditViewOpen;
    },

    setIsBulkEditViewOpen: function (bVal) {
      oProperties.bIsBulkEditViewOpen = bVal;
    },

    getIsDirty: function () {
      return oProperties.bIsDirty;
    },

    setIsDirty: function (bIsDirty) {
      oProperties.bIsDirty = bIsDirty;
    },

    getAppliedDataObjects: function () {
      return oProperties.oAppliedDataObjects;
    },

    getAppliedAttributeList: function () {
      return oProperties.aAppliedAttributeList;
    },

    getAppliedTagList: function () {
      return oProperties.aAppliedTagsList;
    },

    getPropertySequence: function () {
      return oProperties.aPropertySequence;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {

      }
    }
  }
})();

export default BulkEditProcessFilterProps;