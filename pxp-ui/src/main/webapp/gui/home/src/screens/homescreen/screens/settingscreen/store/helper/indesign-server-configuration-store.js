import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import IndesignServerConfigurationViewProps from '../model/indesign-server-configuration-view-props';
import {IndesignServerRequestMapping as oIndesignServerRequestMapping} from '../../tack/setting-screen-request-mapping';
import SettingUtils from './setting-utils';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import {getTranslations as getTranslation} from "../../../../../../commonmodule/store/helper/translation-manager";
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator.js';

let IndesignServerConfigurationStore = (function () {

  /************************************** PRIVATE METHODS **************************************/

  let _triggerChange = function () {
    IndesignServerConfigurationStore.trigger('indesign-server-configuration-changed');
  };

  let _fetchIndesignServersConfiguration = function () {
    return SettingUtils.csGetRequest(oIndesignServerRequestMapping.getIndesignServersConfiguration, {},
        successFetchIndesignServerConfiguration, failureFetchIndesignServerConfiguration);
  };

  let successFetchIndesignServerConfiguration = function (oResponse) {
    oResponse = oResponse.success;
    let oServerConfig = {};
    let oLoadBalancerConfiguration = oResponse.indsLoadBalancer;
    oLoadBalancerConfiguration = {
      port: oLoadBalancerConfiguration.port,
      hostName: oLoadBalancerConfiguration.hostName,
    };

    CS.each(oResponse.pingedServers, function (oServer) {
      oServerConfig[oServer.id] = oServer;
      oServer.isSaved = true;
    });
    IndesignServerConfigurationViewProps.setIndesignServerConfigurationList(oServerConfig);
    IndesignServerConfigurationViewProps.setLoadBalancerConfiguration(oLoadBalancerConfiguration);
    IndesignServerConfigurationViewProps.setRemoveIndesignServerConfigurationList([]);
    if (!oResponse.isLoadBalancerConnected &&
        oLoadBalancerConfiguration.port != "" &&
        oLoadBalancerConfiguration.hostName != "") {
      alertify.error(getTranslation().LOAD_BALANCER_CONFIGURATION_INVALID);
    }
  };

  let failureFetchIndesignServerConfiguration = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchIndesignConfiguration", getTranslation());
  };

  let _getDummyModelForIndesignServerConfiguration = function () {
    let oIndesignServerList = IndesignServerConfigurationViewProps.getIndesignServerConfigurationList();
    let iIDSNServerId = UniqueIdentifierGenerator.generateUUID();
    oIndesignServerList[iIDSNServerId] = {
      id: iIDSNServerId,
      hostName: "",
      port: "",
      status: "",
      isEditable: true,
      isSaved: false
    };

    return oIndesignServerList;
  };

  let _handleIndesignServerConfigurationHeaderSaveButtonClicked = function () {
    let oCallbackData = {};
    let oPostData      = {};
    let oLoadBalancerConfiguration = IndesignServerConfigurationViewProps.getLoadBalancerConfiguration();
    let sPort          = oLoadBalancerConfiguration.port;
    let sHostName      = oLoadBalancerConfiguration.hostName;
    let sHostNameRegex = /^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    let sPortRegex     = /^[0-9]*$/;

    if (!sHostName.match(sHostNameRegex)) {
      alertify.error(getTranslation().LOAD_BALANCER_HOST_NAME_INVALID);
      return;
    }
    if (!sPort || sPort.length === 0 || sPort.length > 5 || !sPort.match(sPortRegex)) {
      alertify.error(getTranslation().LOAD_BALANCER_PORT_NUMBER_INVALID);
      return;
    }

    let oIDSNServerList                 = IndesignServerConfigurationViewProps.getIndesignServerConfigurationList();
    oPostData.serversToUpdate           = [];
    oPostData.serversToRemove           = IndesignServerConfigurationViewProps.getRemoveIndesignServerConfigurationList();
    oPostData.serversToAdd              = [];
    let isInvalidPortPresent            = false;
    for (let iServerId in oIDSNServerList) {
      //Updating the existing server instances.
      if (oIDSNServerList[iServerId].isSaved && oIDSNServerList[iServerId].isModified) {
        let sUpdatedValue = oIDSNServerList[iServerId].updatedPort;
        if (!sUpdatedValue || sUpdatedValue.length > 5 || !sUpdatedValue.match(sPortRegex)) {
          isInvalidPortPresent = true;
        }
        if (sUpdatedValue && sUpdatedValue != "") {
          oPostData.serversToUpdate.push({
            "id": oIDSNServerList[iServerId].id,
            "hostName": "localhost",
            "port": sUpdatedValue
          });
        }
      }
      //Saving newly added server instances.
      if (oIDSNServerList[iServerId].isSaved == false) {
        let sUpdatedValue = oIDSNServerList[iServerId].updatedPort;

        if(!sUpdatedValue || sUpdatedValue.length === 0) {
          delete oIDSNServerList[iServerId];
          continue;
        }

        if (!sUpdatedValue || sUpdatedValue.length > 5 || !sUpdatedValue.match(sPortRegex)) {
          isInvalidPortPresent = true;
        }
        if (sUpdatedValue && sUpdatedValue != "") {
          oPostData.serversToAdd.push({
            "id": oIDSNServerList[iServerId].id,
            "hostName": "localhost",
            "port": sUpdatedValue
          });
        }
      }
    }
    if (isInvalidPortPresent) {
      alertify.error(getTranslation().INDESIGN_SERVER_INSTANCE_PORT_NUMBER_INVALID);
      return;
    }
    oPostData.indsLoadBalancer = oLoadBalancerConfiguration;
    oCallbackData = {loadBalancerConfiguration: oLoadBalancerConfiguration};
    return SettingUtils.csPostRequest(oIndesignServerRequestMapping.saveIndesignConfiguration, {}, oPostData,
        successSaveIndesignConfiguration.bind(this, oCallbackData), failureSaveIndesignConfiguration);
  };

  let successSaveIndesignConfiguration = function (oCallbackData, oResponse) {
    oResponse = oResponse.success;
    if(!oResponse.isLoadBalancerConnected) {
      alertify.error(getTranslation().LOAD_BALANCER_CONFIGURATION_INVALID);
    }
    alertify.success(getTranslation().LOAD_BALANCER_INDESIGN_SERVER_INSTANCE_SAVE_SUCCESSFULLY);

    updateIndesignServerConfigurationList(oResponse);
    IndesignServerConfigurationViewProps.setLoadBalancerConfiguration(oCallbackData.loadBalancerConfiguration);
  };

  let failureSaveIndesignConfiguration = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveIndesignConfiguration", getTranslation());
  };

  let updateIndesignServerConfigurationList = function (oResponse) {
    let oAvailableServerList = {};

    CS.forEach(oResponse.pingedServers, function (oServerDetails) {
      oServerDetails.isSaved = true;
      oAvailableServerList[oServerDetails.id] = oServerDetails;
    });

    IndesignServerConfigurationViewProps.setIndesignServerConfigurationList(oAvailableServerList);
    IndesignServerConfigurationViewProps.setRemoveIndesignServerConfigurationList([]);
  };

  let _handleIndesignServerConfigurationCheckStatusButtonClicked = function (oServerDetails) {
    let sPortRegex = /^[0-9]*$/;
    let sPort = oServerDetails.port;
    if (!sPort) {
      return;
    } else if (sPort.length > 5 || !sPort.match(sPortRegex)) {
      alertify.error(getTranslation().INDESIGN_SERVER_INSTANCE_PORT_NUMBER_INVALID);

      return;
    }
    let oPostData = {
      serversToPing: [{"id": oServerDetails.id, "hostName": "localhost", "port": oServerDetails.port}],
      indsLoadBalancer: IndesignServerConfigurationViewProps.getLoadBalancerConfiguration()
    };
    return SettingUtils.csPostRequest(oIndesignServerRequestMapping.checkStatusOfIndesignServer, {}, oPostData, successPingIndesignConfiguration.bind(this),
        failurePingIndesignConfiguration);
  };

  let successPingIndesignConfiguration = function (oResponse) {
    oResponse = oResponse.success;
    _updatePingedIndesignServerConfigurationList(oResponse);
    if(!oResponse.isLoadBalancerConnected) {
      alertify.error(getTranslation().LOAD_BALANCER_CONFIGURATION_INVALID);
    }
  };

  let failurePingIndesignConfiguration = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failurePingIndesignConfiguration", getTranslation());
  };

  let _updatePingedIndesignServerConfigurationList = function (oUpdatedServerInfo) {
    let oAvailableServerList = IndesignServerConfigurationViewProps.getIndesignServerConfigurationList();
    let oUpdatedServer = oUpdatedServerInfo.pingedServers && oUpdatedServerInfo.pingedServers.length ?
                         oUpdatedServerInfo.pingedServers[0] : {};
    if (!CS.isEmpty(oUpdatedServer)) {
      let oAvailableServer = oAvailableServerList[oUpdatedServer.id];
      if (!CS.isEmpty(oAvailableServer)) {
        oAvailableServer.id = oUpdatedServer.id ? oUpdatedServer.id : oAvailableServer.id;
        oAvailableServer.port = oUpdatedServer.port ? oUpdatedServer.port : oAvailableServer.port;
        oAvailableServer.hostName = oUpdatedServer.hostName ? oUpdatedServer.hostName : oAvailableServer.hostName;
        oAvailableServer.status = oUpdatedServer.status;
      }
    }
  };

  let _handleLoadBalancerConfigSingleValueChanged = function (sKey, sVal) {
    let sValue = sVal.trim();
    let oLoadBalancerDetails = IndesignServerConfigurationViewProps.getLoadBalancerConfiguration();
    if (sKey == "hostName") {
      oLoadBalancerDetails.hostName = sValue;
    } else {
      oLoadBalancerDetails.port = sValue;
    }
  };

  let _handleIDSNServerConfigSingleValueChanged = function (sKey, sVal) {
    let oIndesignServerList = IndesignServerConfigurationViewProps.getIndesignServerConfigurationList();
    for (let iServerId in oIndesignServerList) {
      if (iServerId != sKey) {
        if ((oIndesignServerList[iServerId].isModified && oIndesignServerList[iServerId].updatedPort == sVal) ||
            (!oIndesignServerList[iServerId].isModified && oIndesignServerList[iServerId].port == sVal)) {
          alertify.warning(getTranslation().INDESIGN_SERVER_PORT_NUMBER_ALREADY_CONFIGURED);

          return;
        }
      }
    }
    oIndesignServerList[sKey].updatedPort = sVal;
    oIndesignServerList[sKey].isModified = true;
  };

  let _handleIndesignServerConfigurationRemoveButtonClicked = function (sServerId) {
    let oAvailableServerList = IndesignServerConfigurationViewProps.getIndesignServerConfigurationList();
    if (oAvailableServerList[sServerId].isSaved) {
      let aRemovedIDSNList = IndesignServerConfigurationViewProps.getRemoveIndesignServerConfigurationList();
      aRemovedIDSNList.push({
        "id": oAvailableServerList[sServerId].id,
        "hostName": "localhost",
        "port": oAvailableServerList[sServerId].port
      });
      IndesignServerConfigurationViewProps.setRemoveIndesignServerConfigurationList(aRemovedIDSNList);
    }
    delete oAvailableServerList[sServerId];
  };

  /************************************** PUBLIC METHODS *************************************/
  return {

    fetchIndesignServerConfiguration: function () {
      _fetchIndesignServersConfiguration().then(_triggerChange);
    },

    handleIndesignServerConfigurationAddButtonClicked: function () {
      let oIndesignServerList = _getDummyModelForIndesignServerConfiguration();
      IndesignServerConfigurationViewProps.setIndesignServerConfigurationList(oIndesignServerList);
      _triggerChange();
    },

    handleIndesignServerConfigurationHeaderSaveButtonClicked: function () {
      _handleIndesignServerConfigurationHeaderSaveButtonClicked().then(_triggerChange);
    },

    handleIndesignServerConfigurationRemoveButtonClicked: function (sServerId) {
      _handleIndesignServerConfigurationRemoveButtonClicked(sServerId);
      _triggerChange();
    },

    handleIndesignServerConfigurationCheckStatusButtonClicked: function (oServerDetails) {
      _handleIndesignServerConfigurationCheckStatusButtonClicked(oServerDetails).then(_triggerChange);
    },

    handleLoadBalancerConfigSingleValueChanged: function (sKey, sVal) {
      _handleLoadBalancerConfigSingleValueChanged(sKey, sVal);
    },

    handleIDSNServerConfigSingleValueChanged: function (sKey, sVal) {
      _handleIDSNServerConfigSingleValueChanged(sKey, sVal);
    },

  }
})();

MicroEvent.mixin(IndesignServerConfigurationStore);

export default IndesignServerConfigurationStore;