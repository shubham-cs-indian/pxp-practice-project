import ClassProps from './../model/class-config-view-props';
import SettingUtils from './../helper/setting-utils';

var ClassUtils = (function () {

  var _getActiveClass = function () {
    return ClassProps.getActiveClass();
  };

  var _setClassScreenLockedStatus = function (bLockStatus) {
    ClassProps.setClassScreenLockedStatus(bLockStatus);
  };

  var _getClassScreenLockedStatus = function () {
    return ClassProps.getClassScreenLockedStatus();
  };

  var _makeClassDirty = function (oClass) {
    if (!(oClass.clonedObject || oClass.isCreated)) {
      SettingUtils.makeObjectDirty(oClass);
      _setClassScreenLockedStatus(true);
    }

    return oClass.clonedObject ? oClass.clonedObject : oClass;
  };

  var _makeActiveClassDirty = function () {
    var oActiveClass = _getActiveClass();
    return _makeClassDirty(oActiveClass);
  };

  var _getCurrentClass = function () {
    var oActiveClass = _getActiveClass();

    return oActiveClass.clonedObject ? oActiveClass.clonedObject : oActiveClass;
  };

  //Added for autosave
  var _saveClass = function(oCallBack){
    var ClassStore = require('./class-store').default;
    ClassStore.saveClass(oCallBack);
  };

  return {

    getActiveClass: function () {
      return _getActiveClass();
    },

    makeActiveClassDirty: function () {
      return _makeActiveClassDirty();
    },

    getCurrentClass: function(){
      return _getCurrentClass();
    },

    setClassScreenLockedStatus: function (bLockStatus) {
      _setClassScreenLockedStatus(bLockStatus);
    },

    getClassScreenLockedStatus: function () {
      return _getClassScreenLockedStatus();
    },

    saveClass: function(oCallback){
      _saveClass(oCallback);
    },

    getSplitter: function(){
      return "#$%$#";
    },

    getTagDialogVisibility: function () {
      return ClassProps.getTagDialogVisibility();
    },

  }
})();

export default ClassUtils;
