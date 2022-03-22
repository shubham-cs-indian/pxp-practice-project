import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager.js';

var getIndesignServerConfiguration = function (sServerId) {
  return [
    {
      "id": "1",
      "label": "Server Config Information",
      "elements": [
        {
          id: "1",
          key: sServerId,
          label: getTranslation().PORT,
          type: "singleText",
          width: 100
        },
        {
          id: "2",
          key: "portStatus",
          label: "",
          type: "customView",
          width: 100
        }
      ]
    }
  ]
};

var getLoadBalancerConfiguration = function () {
  return [
    {
      "id": "1",
      "label": "Basic Information",
      "elements": [
        {
          id: "1",
          key: "hostName",
          label: getTranslation().SERVER_URL,
          type: "singleText",
          width: 100
        },
        {
          id: "2",
          key: "serverPort",
          label: getTranslation().PORT,
          type: "singleText",
          width: 100
        }
      ]
    }
  ];
};

export default {
  indesignServerConfiguration: getIndesignServerConfiguration,
  loadBalancerConfiguration: getLoadBalancerConfiguration,
};