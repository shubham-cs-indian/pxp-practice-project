
import CS from '../../../../../../libraries/cs';

import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ClassProps from './../model/class-config-view-props';
import ClassSettingProps from './../model/class-setting-view-props';

var ClassSettingStore = (function () {

  var _triggerChange = function () {
    ClassSettingStore.trigger('class-setting-changed');
  };

  return {

    paperViewHeaderClicked: function (oModel) {
      var aRulePermissionVisualStates = ClassSettingProps.getRulePermissionVisualState();
      var oFoundedState = CS.find(aRulePermissionVisualStates, {id: oModel.id});
      if(oFoundedState) {
        oFoundedState.isExpanded = !oFoundedState.isExpanded;
      }
      _triggerChange();
    },

    changeClassCategory: function (sClassCategory) {
      ClassProps.setSelectedClassCategory(sClassCategory);
    },

  }
})();

MicroEvent.mixin(ClassSettingStore);

export default ClassSettingStore;
