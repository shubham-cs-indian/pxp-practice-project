
var CustomSearchDialogViewProps = (function () {

  let Props = function () {
    return {
      items: [],
      isDialogOpen : false
    }
  };

  let oProperties = new Props();

  return {

    getDialogOpen : function () {
      return oProperties.isDialogOpen;
    },

    setDialogOpen : function (bIsDialogOpen) {
      oProperties.isDialogOpen = bIsDialogOpen ;
    },
  }
})();

export default CustomSearchDialogViewProps;
