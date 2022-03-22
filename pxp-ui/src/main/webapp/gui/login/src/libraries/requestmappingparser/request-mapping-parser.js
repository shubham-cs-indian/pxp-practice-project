import {forEach, template} from './../cs/cs-lodash';

var RequestMappingParser = (function(){

	var encodeParameter = function(oParameter){

	    forEach(oParameter, function(oVal, sKey){
	      oParameter[sKey] = encodeURIComponent(oVal);
	    });
	  };

	  return {

	    getRequestUrl: function (sUrl, oParameter) {
	      encodeParameter(oParameter);

	      var compiled = template(sUrl);
	      return compiled(oParameter);
	    }
  }

})();

export default RequestMappingParser;
