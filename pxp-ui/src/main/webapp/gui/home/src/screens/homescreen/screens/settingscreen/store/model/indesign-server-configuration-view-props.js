
var IndesignServerConfigurationViewProps = (function () {
  let Props = function () {
    return {
      oIndesignServerConfigurationList: {},
      oLoadBalancerConfiguration: {},
      aRemovedServerList: [],
    }
  };

  let oProperties = new Props();

  return {

    setLoadBalancerConfiguration: function (oLoadBalancerConfiguration) {
      oProperties.oLoadBalancerConfiguration = oLoadBalancerConfiguration;
    },

    getLoadBalancerConfiguration: function () {
      return oProperties.oLoadBalancerConfiguration;
    },

    setIndesignServerConfigurationList: function (oList) {
      return oProperties.oIndesignServerConfigurationList = oList;
    },

    getIndesignServerConfigurationList: function () {
      return oProperties.oIndesignServerConfigurationList;
    },

    setRemoveIndesignServerConfigurationList: function (aRemovedServers) {
      oProperties.aRemovedServerList = aRemovedServers;
    },

    getRemoveIndesignServerConfigurationList: function () {
      return oProperties.aRemovedServerList;
    },

    reset: function () {
      oProperties = new Props();
    }
  }
})();

export default IndesignServerConfigurationViewProps;