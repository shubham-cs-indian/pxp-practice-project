const ActionDialogProps = (function () {

  const Props = function () {
    return {
      isCustomDialogOpen : false
    }
  };

  let oProperties = new Props();

  return {
    getIsCustomDialogOpen : function () {
      return oProperties.isCustomDialogOpen;
    },

    setIsCustomDialogOpen : function (bIsCustomDialogOpen) {
      oProperties.isCustomDialogOpen =  bIsCustomDialogOpen;
    },

    reset: function () {
      oProperties = new Props();
    }
  }
})();

export default ActionDialogProps;
