let assetDownloadTrackerProps = (() => {

  let Props = function () {
    return {
      totalDownloadCount: 0,
      downloadCountWithinRange: 0,
      downloadRange: {
        startTime: "",
        endTime: ""
      }
    }
  };

  let Properties = new Props();

  return {

    getTotalDownloadCount: function() {
      return Properties.totalDownloadCount;
    },

    setTotalDownloadCount: function(totalDownloadCount) {
      Properties.totalDownloadCount = totalDownloadCount;
    },

    getDownloadCountWithinRange: function(){
      return Properties.downloadCountWithinRange;
    },

    setDownloadCountWithinRange: function(downloadCountWithinRange){
      Properties.downloadCountWithinRange = downloadCountWithinRange;
    },

    getDownloadRange: function () {
      return Properties.downloadRange;
    },

    setDownloadRange: function (oDownloadRange) {
      Properties.downloadRange = oDownloadRange;
    },

    reset: () => {
      Properties = new Props();
    },

    toJSON: () => {
      return Properties;
    }
  }

})();

export default assetDownloadTrackerProps;
