
import MicroEvent from '../../../../../../../../libraries/microevent/MicroEvent.js';
import DashboardScreenProps from './../model/dashboard-screen-props';
import AppData from './../model/dashboard-screen-app-data';
import CommonUtils from './../../../../../../../../commonmodule/util/common-utils';
import ContentUtils from '../../../../store/helper/content-utils';
import PhysicalCatalogDictionary from '../../../../../../../../commonmodule/tack/physical-catalog-dictionary';
import GlobalStore from './../../../../../../store/global-store';
let DashboardUtils = (function () {

  /************************************* Public API's **********************************************/
  return {

    getDecodedTranslation: function (sStringToCompile, oParameter) {
      return ContentUtils.getDecodedTranslation(sStringToCompile, oParameter);
    },

    getSelectedDashboardTabId: function () {
      return DashboardScreenProps.getSelectedDashboardTabId();
    },

    getGlobalStore: function () {
      return GlobalStore;
    },

    isDataIntegrationAllowedForCurrentUser: function () {
      return CommonUtils.isDataIntegrationAllowedForCurrentUser();
    },

    isOnlyDataIntegrationEnabled: function(){
      return CommonUtils.isOnlyDataIntegrationEnabled();
    },

    isCurrentUserAdmin: function () {
      return CommonUtils.isCurrentUserAdmin();
    },

    isCurrentUserSystemAdmin: function () {
      return CommonUtils.isCurrentUserSystemAdmin();
    },

    isCurrentUserReadOnly: function () {
      return CommonUtils.isCurrentUserReadOnly();
    },

    getFilterDataForDashboardTab : function () {
      let oDataIntegrationInfo = ContentUtils.getDataIntegrationInfo();
      let sPhysicalCatalogId = oDataIntegrationInfo.physicalCatalogId;
      let sPortalID = oDataIntegrationInfo.portalId;
      let oCurrentUser = ContentUtils.getCurrentUser();
      if(sPhysicalCatalogId === PhysicalCatalogDictionary.PIM_ARCHIVAL){
        sPhysicalCatalogId = PhysicalCatalogDictionary.PIM;
      }
      let oRequestData = {
        physicalCatalogId: sPhysicalCatalogId,
        organizationId: oCurrentUser.organizationId,
      };
      return oRequestData;
    },

    /************************** Add API'S Before this comment and KEEP RESET ALL AT LAST *************************/
    resetAll: function () {
      AppData.reset();
      DashboardScreenProps.reset();
    }
  };

})();

MicroEvent.mixin(DashboardUtils);

export default DashboardUtils;
