/**
 * Created by CS108 on 28-06-2017.
 */
export default (function () {

  var bDashboardVisibility = false; /***** For Dashboard ******/
  var bAllModuleVisibility = false; /***** For All Module******/
  var bPimModuleVisibility = true; /****** For PIM *****/
  var bMamModuleVisibility = true; /****** For MAM *****/
  var bTargetModuleVisibility = true; /***** For Market/Target ******/
  var bSupplierModuleVisibility = true; /***** For Supplier ******/
  var bTextAssetModuleVisibility = true; /***** For TextAsset ******/
  var bFilesModuleVisibility = true; /***** For Files Module ******/
  var bJosStatusModuleVisibility= true; /***** For Job Status ******/
  var bTaskDashboardVisibility = true; /***** For Task Dashboard ******/
  /****** public methods ***/
  return {
    getDashboardVisibilityStatus: function () {
      return bDashboardVisibility;
    },
    getAllModuleVisibilityStatus: function () {
      return bAllModuleVisibility;
    },
    getPimModuleVisibilityStatus: function () {
      return bPimModuleVisibility;
    },
    getMamModuleVisibilityStatus: function () {
      return bMamModuleVisibility;
    },
    getTargetModuleVisibilityStatus: function () {
      return bTargetModuleVisibility;
    },
    getSupplierModuleVisibilityStatus: function () {
      return bSupplierModuleVisibility;
    },
    getTextassetModuleVisibilityStatus: function () {
      return bTextAssetModuleVisibility;
    },
    getFilesModuleVisibilityStatus: function() {
      return bFilesModuleVisibility;
    },
    getJosStatusModuleVisibilityStatus: function(){
      return bJosStatusModuleVisibility;
    },
    getTaskDashboardVisibilityStatus: function(){
      return bTaskDashboardVisibility;
    },
  }
})();
