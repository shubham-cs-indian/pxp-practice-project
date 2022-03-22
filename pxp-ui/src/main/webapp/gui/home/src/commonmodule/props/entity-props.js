let EntityProps = (function () {

  let Props = function () {
    return {
      allEntityIds: []
    }
  };

  let oProperties = new Props();

  return {
    getAllEntityIds: function () {
      return oProperties.allEntityIds;
    },

    setAllEntityIds: function (_allEntityIds) {
      oProperties.allEntityIds = _allEntityIds;
    },

    reset: () => {
      // oProperties = new Props();
    },

    toJSON: () => {
      return oProperties;
    }
  };

})();

module.exports = EntityProps;