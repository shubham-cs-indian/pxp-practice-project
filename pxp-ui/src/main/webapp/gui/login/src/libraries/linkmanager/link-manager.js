// TODO: #Refact20

/*
export default (function () {
  var _getParameterObject = function () {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    var oParameterObject = {};
    for (var i = 0; i < vars.length; i++) {
      var pair = vars[i].split("=");
      var sKey = pair[0];
      var sValue = decodeURIComponent(pair[1]);
      oParameterObject[sKey] = sValue;
    }
    return oParameterObject;
  };

  return {
    getParameterByName: function (sName) {
      var oParameters = _getParameterObject();
      return oParameters[sName];
    },

    getParameterObject: function () {
      return _getParameterObject();
    }
  }
})();*/
