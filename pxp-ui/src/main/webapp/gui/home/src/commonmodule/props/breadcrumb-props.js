/**
 * Created by CS80 on 6/15/2016.
 */

var BreadcrumbProps = (function () {

  var Props = function () {
    return {
      aBreadCrumbData : [],
      oForwardBreadCrumbData: {},
      aPreviousBreadCrumbData: []
    }
  };

  var oProperties = new Props();

  return {

    getBreadCrumbData : function () {
      return oProperties.aBreadCrumbData;
    },

    setBreadCrumbData : function (_aBreadCrumbData) {
      oProperties.aBreadCrumbData =  _aBreadCrumbData;
    },

    getPreviousBreadCrumbData: function () {
      return oProperties.aPreviousBreadCrumbData;
    },

    setPreviousBreadCrumbData: function (_aPreviousBreadCrumbData) {
      oProperties.aPreviousBreadCrumbData = _aPreviousBreadCrumbData;
    },

    getForwardBreadCrumbData: function () {
      return oProperties.oForwardBreadCrumbData;
    },

    setForwardBreadCrumbData: function (oForwardBreadCrumbData) {
      oProperties.oForwardBreadCrumbData = oForwardBreadCrumbData;
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

export default BreadcrumbProps;