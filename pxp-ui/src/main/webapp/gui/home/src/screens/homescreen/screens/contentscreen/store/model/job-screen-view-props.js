var JobScreenProps = (function () {

  var Props = function () {
    return {
      activeJob: {},
      activeJobGraphData: {},
      isJobScreenMode: false
    }
  };

  var oProperties = new Props();

  return {

    getActiveJob: function () {
      return oProperties.activeJob;
    },

    setActiveJob: function (oActiveJob) {
      oProperties.activeJob = oActiveJob;
    },

    getActiveJobGraphData: function () {
     return oProperties.activeJobGraphData;
    },

    setActiveJobGraphData: function (oActiveJobGraph) {
      oProperties.activeJobGraphData = oActiveJobGraph;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {
        selectedJobId: oProperties.selectedJobId
      };
    }

  };

})();

export default JobScreenProps;
