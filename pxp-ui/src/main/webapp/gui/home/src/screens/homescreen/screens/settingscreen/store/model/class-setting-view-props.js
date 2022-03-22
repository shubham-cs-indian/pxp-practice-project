/**
 * Created by CS56 on 3/11/2016.
 */

var ClassSettingViewProps = (function () {

  let Props = function () {
    return {
      aRulePermissionVisualState: [
        {
          id: "1",
          label: "Rule Settings",
          isExpanded: true,
          isVisible: false
        },
        {
          id: "2",
          label: "Visual Settings",
          isExpanded: true,
          isVisible: true
        },
        {
          id: "3",
          label: "Permission Settings",
          isExpanded: true,
          isVisible: true
        }
      ],
      oSelectedView: {}
    };
  };

  let oProperties = new Props();

  return {

    getRulePermissionVisualState: function () {
      return oProperties.aRulePermissionVisualState;
    },

    getSelectedView: function(){
      return oProperties.oSelectedView;
    },

    setSelectedView: function(_oSelectedView){
      oProperties.oSelectedView = _oSelectedView;
    },

    reset: function () {
      oProperties = new Props();
    }

  }

})();

export default ClassSettingViewProps;