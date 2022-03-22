var RightPanelViewProps = (function () {

  var Props = function () {
    return {
      sSelectedBarItemId: "",
      bRightBarVisible: false,
      bRightPanelVisible: false
    }
  };

  var oProperties = new Props();

  return {

    setSelectedBarItemId: function (_sSelectedBarItemId) {
      oProperties.sSelectedBarItemId = _sSelectedBarItemId;
    },

    resetRightBarProps: function () {
      oProperties.sSelectedBarItemId = "";
      oProperties.bRightBarVisible = false;
      oProperties.bRightPanelVisible = false;
    },

    reset: function () {
      oProperties = new Props();
    },

    toJSON: function () {
      return {

      }
    }

  };

})();

export default RightPanelViewProps;
