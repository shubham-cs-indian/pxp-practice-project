let SharableURLProps = (function () {

  let Props = function () {
    return {
      entityId:"",
      isPushHistoryState: true, /**To determine whether push is allowed or not in history**/
      isEntityNavigation: true, /**To determine history pop-state event execution**/
      baseType: ""
    }
  };

  let oProperties = new Props();

  return {

    getEntityId: function () {
      return oProperties.entityId;
    },

    setEntityId: function (sEntityId) {
      oProperties.entityId = sEntityId;
    },

    getEntityBaseType: function () {
      return oProperties.baseType;
    },

    setEntityBaseType: function (sBaseType) {
      oProperties.baseType = sBaseType;
    },

    getIsPushHistoryState: function () {
      return oProperties.isPushHistoryState;
    },

    setIsPushHistoryState: function (bIsPushHistoryState) {
      oProperties.isPushHistoryState = bIsPushHistoryState;
    },

    getIsEntityNavigation: function () {
      return oProperties.isEntityNavigation;
    },

    setIsEntityNavigation: function (bIsEntityNavigation) {
      oProperties.isEntityNavigation = bIsEntityNavigation;
    },

    reset: () => {
      // oProperties = new Props();
    },

    toJSON: () => {
      return oProperties;
    }
  };

})();

export default SharableURLProps;