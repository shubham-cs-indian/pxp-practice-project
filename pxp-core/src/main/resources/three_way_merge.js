/*
 Function for 3-way merge

 @param {Object} o (original)
 @param {Object} a (current)
 @param {Object} b (new)
 @return {Object} Merged result
 */

var compareObjects = function(oA, oB){
  if(Array.isArray(oA)){
    if(!Array.isArray(oB)){
      return false;
    }

    var bRes = true;
    for(var i=0; i<oA.length; i++){
      bRes = bRes && compareObjects(oA[i], oB[i]);
    }

    return bRes;
  } else if(oA instanceof Object) {
    if(Array.isArray(oB)){
      return false;
    }

    var bRes = true;
    for(var key in oA){
      if(oA[key] instanceof Object || Array.isArray(oA)){
        bRes = bRes && compareObjects(oA[key], oB[key]);
      } else if(oA[key] != oB[key]){
        return false;
      }
    }

    return bRes;
  } else {
    return oA == oB;
  }
}

var findObjectInArray = function(aArray, oObject){

  if(Array.isArray(aArray)){

    for(var i=0; i<aArray.length; i++){
      var oObjectInArray = aArray[i];
      if(oObjectInArray instanceof Object){

        var bFound = true;
        for(var key in oObject){
          if(!compareObjects(oObject[key], oObjectInArray[key])){
            bFound = false;
            break;
          }
        }

        if(bFound){
          return oObjectInArray;
        }
      }
    }
  } else {
    return null;
  }

  return null;
};

var mergeold,
    indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

mergeold = function(o, a, b) {
  debugger;
  var isArray, k, ov, ref, ref1, ref2, result;
  if (typeof o !== 'object') {
    throw new Error('Merge original document must be an object!');
  }
  if (typeof a !== 'object') {
    throw new Error('Merge current document must be an object!');
  }
  if (typeof b !== 'object') {
    throw new Error('Merge new document must be an object!');
  }
  isArray = Array.isArray(b);
  result = isArray ? [] : {};
  if (isArray) {
    if (!Array.isArray(a)) {
      a = [];
    }
    if (!Array.isArray(o)) {
      o = [];
    }
    for (k in a) {
      if (!((ref = a[k], !findObjectInArray(b, ref)) && (ref1 = a[k], findObjectInArray(o, ref1)))) {
        result.push(a[k]);
      }
    }
    for (k in b) {
      if (!k in a) {
        result.push(b[k]);
      } else if (typeof a[k] === 'object' && typeof b[k] === 'object') {
        ov = k in o && typeof o[k] === 'object' ? o[k] : {};
        result[k] = merge(ov, a[k], b[k]);
      } else if (ref2 = b[k], !findObjectInArray(a, ref2)) {
        result.push(b[k]);
      }
    }
  } else {
    if (Array.isArray(a)) {
      a = {};
    }
    for (k in b) {
      result[k] = b[k];
    }
    for (k in a) {
      if (!k in result) {
        result[k] = a[k];
      } else if (a[k] !== result[k]) {
        if (typeof a[k] === 'object' && typeof (b != null ? b[k] : void 0) === 'object') {
          ov = (o != null) && k in o && typeof o[k] === 'object' ? o[k] : {};
          result[k] = merge(ov, a[k], b[k]);
        } else if ((b != null ? b[k] : void 0) === (o != null ? o[k] : void 0)) {
          result[k] = a[k];
        }
      }
    }
  }
  return result;
};

var getByIdFromArray = function(aArray, sId){

  if(Array.isArray(aArray)) {
    for (var i = 0; i < aArray.length; i++) {
      if (aArray[i].id == sId) {

        return aArray[i];
      }
    }
  }

  return null;
}

var merge = function(oBase, oRemote, oWorking){

  debugger;
  if(Array.isArray(oWorking)){

    var aIds = [];
    var aReturnArray = [];

    for(var i=0; i<oWorking.length; i++){
      var oWorkOb = oWorking[i];
      var oBaseOb = "";
      var oRemoteOb = "";
      if(oWorkOb instanceof Object){
        var sId = oWorkOb.id;
        aIds.push(oWorkOb.id);

        oBaseOb = getByIdFromArray(oBase, sId);
        oRemoteOb = getByIdFromArray(oRemote, sId);
      } else {
        aIds.push(oWorkOb);

        oBaseOb = oBase == null ? null : (oBase.indexOf(oWorkOb) == -1 ? null : oWorkOb);
        oRemoteOb = oRemote == null ? null : (oRemote.indexOf(oWorkOb) == -1 ? null : oWorkOb);
      }

      var oResultOb = merge(oBaseOb, oRemoteOb, oWorkOb);

      if(oResultOb){
        aReturnArray.push(oResultOb);
      }
    }

    for(var j=0; j<oRemote.length; j++){
      var oRemoteOb = oRemote[j];
      var oBaseOb = "";
      var oWorkOb = "";
      if(oRemoteOb instanceof Object){

        var sId = oRemoteOb.id;
        if(aIds.indexOf(sId) == -1){
          oBaseOb = getByIdFromArray(oBase, sId);
          oWorkOb = getByIdFromArray(oWorking, sId);

          var oResultOb = merge(oBaseOb, oRemoteOb, oWorkOb);

          if(oResultOb){
            aReturnArray.push(oResultOb);
          }
        }
      } else {

        if(aIds.indexOf(oRemoteOb) == -1) {
          oBaseOb = oBase == null ? null : (oBase.indexOf(oRemoteOb) == -1 ? null : oRemoteOb);
          oWorkOb = oWorking == null ? null : (oWorking.indexOf(oRemoteOb) == -1 ? null : oRemoteOb);

          var oResultOb = merge(oBaseOb, oRemoteOb, oWorkOb);

          if(oResultOb){
            aReturnArray.push(oResultOb);
          }
        }
      }
    }

    return aReturnArray;

  } else if(oWorking instanceof Object) {

    var oReturnObject = {};

    if(!oBase){
      if(!oWorking){
        return oRemote;
      } else {
        return oWorking;
      }
    }

    if(!oWorking && !oRemote){
      return null;
    }

    if(!oRemote){
      if(compareObjects(oWorking, oBase)){
        return null;
      } else {
        return oWorking;
      }
    }

    if(!oWorking){
      if(compareObjects(oRemote, oBase)){
        return null;
      } else {
        return oRemote;
      }
    }

    for(var sKey in oWorking){

      if(sKey == "referencedClassIds"){
        var oWorkRefClassIds = oWorking[sKey];
        var oRemoteRefClassIds = oRemote[sKey];
        var oResultRefClassIds = {};
        var aConsideredIds = [];

        for(var sClassId in oWorkRefClassIds){
          aConsideredIds.push(sClassId);
          var iRemoteCount = oRemoteRefClassIds[sClassId] || 0;
          var iWorkCount = oWorkRefClassIds[sClassId] || 0;
          var iTotal = iRemoteCount + iWorkCount;

          if(iTotal){
            oResultRefClassIds[sClassId] = iTotal;
          }
        }

        for(var sClassId in oRemoteRefClassIds){
          if(aConsideredIds.indexOf(sClassId) == -1){
            oResultRefClassIds[sClassId] = oRemoteRefClassIds[sClassId];
          }
        }

        oReturnObject[sKey] = oResultRefClassIds;
      } else {
        var sBaseValue = oBase[sKey];
        var sWorkingValue = oWorking[sKey];
        var sRemoteValue = oRemote[sKey];

        oReturnObject[sKey] = merge(sBaseValue, sRemoteValue, sWorkingValue);
      }
    }

    return oReturnObject;
  } else {
    if(oWorking != oBase){
      return oWorking;
    } else {
      return oRemote;
    }
  }
}

// ---
// generated by coffee-script 1.9.2 
