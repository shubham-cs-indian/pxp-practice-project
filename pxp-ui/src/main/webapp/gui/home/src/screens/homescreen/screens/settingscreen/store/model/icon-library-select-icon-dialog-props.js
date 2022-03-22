let IconLibrarySelectIconDialogProps = (function () {

  let Props = function () {
    return {
      showSelectIconDialog: false,
      contentId: "",
      propertyId: "",
      context: "",
      pathToRoot: "",
    }
  };

  let oProperties = new Props();

  return {
    reset: function () {
      oProperties = new Props();
    },
    getShowSelectIconDialog: function () {
      return oProperties.showSelectIconDialog;
    },
    setShowSelectIconDialog: function (_showSelectIconDialog) {
      oProperties.showSelectIconDialog = _showSelectIconDialog;
    },
    getContentId: function () {
      return oProperties.contentId;
    },
    setContentId: function (_sContentId) {
      oProperties.contentId = _sContentId;
    },
    getPropertyId: function () {
      return oProperties.propertyId;
    },
    setPropertyId: function (_sPropertyId) {
      oProperties.propertyId = _sPropertyId;
    },
    getContext: function () {
      return oProperties.context;
    },
    setContext: function (_sContext) {
      oProperties.context = _sContext;
    },
    getPathToRoot: function () {
      return oProperties.pathToRoot;
    },
    setPathToRoot: function (_sPathToRoot) {
      oProperties.pathToRoot = _sPathToRoot;
    },
  }
})();

export default IconLibrarySelectIconDialogProps;