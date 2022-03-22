// const _ = require("lodash");

import MicroEvent from '../../../../../../../../libraries/microevent/MicroEvent.js';

import DashboardScreenProps from '../model/dashboard-screen-props';
import SessionProps from '../../../../../../../../commonmodule/props/session-props';
import DashboardUtils from './dashboard-utils';

const DashboardStore = (function () {

  /******************************* PRIVATE API's *******************************/
  let _triggerChange = function () {
    DashboardStore.trigger('dashboard-store-changed');
  };

  /*let _handleDashboardEndpointTileClicked = (oEndpointData) => {
    let sEndpointId = oEndpointData.id;
    let sEndpointType = oEndpointData.type;
    SessionStorageManager.setPropertyInSessionStorage("previousPhysicalCatalogId", SessionStorageManager.getPropertyFromSessionStorage("physicalCatalogId"));
    // DashboardScreenProps.setPreviousPhysicalCatalog(SessionProps.getSessionPhysicalCatalogId());
    SessionProps.setSessionPhysicalCatalogData(PhysicalCatalogDictionary.DATAINTEGRATION);
    SessionProps.setSessionEndpointId(sEndpointId);
    SessionProps.setSessionEndpointType(sEndpointType);
    let oGlobalStore = DashboardUtils.getGlobalStore();
    let oCallback = {
      isForDataIntegration: true,
    };
    oGlobalStore.fetchMasterData(oCallback);
    DashboardScreenProps.setIsDIDialogVisible(true);
  } ;*/

  /*let _handleDIDialogButtonClicked = function (bOpened) {

    if(!bOpened) {
      SessionProps.setSessionPhysicalCatalogData(DashboardScreenProps.getPreviousPhysicalCatalog());
      DashboardScreenProps.setPreviousPhysicalCatalog('');
      SessionProps.setSessionEndpointId(null);
      SessionProps.setSessionEndpointType(null);
      let oGlobalStore = DashboardUtils.getGlobalStore();
      let oCallback = {
        isForDataIntegration: false,
        activeScreenId: "dashboard2"
      };
      let sPhysicalCatalogId = SessionProps.getSessionPhysicalCatalogId();
      let bDontResetPhysicalCatalog = !(sPhysicalCatalogId === PhysicalCatalogDictionary.DATAINTEGRATION); //in case of a user who only has data integration permission
      oGlobalStore.fetchMasterData(oCallback, bDontResetPhysicalCatalog);
    }

    DashboardScreenProps.setIsDIDialogVisible(bOpened);
  };*/

  /******************************* PUBLIC API's *******************************/
  return {

    /*handleDashboardEndpointTileClicked: function (oEndpointData) {
      _handleDashboardEndpointTileClicked(oEndpointData);
      _triggerChange();
    },*/

    /*handleDIDialogButtonClicked: function (bOpened) {
      _handleDIDialogButtonClicked(bOpened);
      _triggerChange();
    },*/

    triggerChange: function () {
      _triggerChange();
    }
  }
})();


MicroEvent.mixin(DashboardStore);

export default DashboardStore;