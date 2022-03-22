import CS from '../../libraries/cs';
import GlobalStore from '../../screens/homescreen/store/global-store'
import WebsocketConstants from '../../commonmodule/tack/websocket-constants';


var WebsocketUtils = (function () {
  let sUrl = window.location.origin + window.location.pathname;
  let sServerPath = sUrl.indexOf("http") != -1 ? sUrl.replace("http", "ws") : "ws://" + sUrl;
  let oWebsocket;

  /**
   * Verifies if the connection is alive or not
   * @returns {boolean}
   * @private
   */
  let _connectionVerification = () => {
    if (!oWebsocket) {
      return true;
    }
    if (oWebsocket.readyState === oWebsocket.CLOSED) {

      return true;
    }
    return false;
  };

  let _processMessage = (oMessage) => {
    if (oMessage.message === WebsocketConstants.CS_SEND_USER_DATA_HANDSHAKE) {

      let oData = {clientType: "application-pxp", userId: GlobalStore.getCurrentUser().id};
      let oResponseMessage = {};
      oResponseMessage.from = GlobalStore.getCurrentUser().id;
      oResponseMessage.message = WebsocketConstants.CS_SEND_USER_DATA_HANDSHAKE;
      oResponseMessage.data = oData;
      oWebsocket.send(JSON.stringify(oResponseMessage));
      return true;
    }
    return false;
  };

  let _openSession = () => {
    //verify if the connection is alive or not
    if (!_connectionVerification()) return;

    oWebsocket = new WebSocket(sServerPath + "websocket");

    oWebsocket.onmessage = function (event) {
      let oMessage = JSON.parse(event.data);
      if(_processMessage(oMessage)) return;

      GlobalStore.websocketOnMessage(oMessage);
    };

    oWebsocket.onopen = function (event) {
      setInterval(function() {
        if (oWebsocket && oWebsocket.readyState === oWebsocket.CLOSED) clearInterval();
        let oHeartbeatMessage = {};
        oHeartbeatMessage.message = WebsocketConstants.WEBSOCKET_HEARTBEAT;
        oHeartbeatMessage.data = {};
        oWebsocket.send(JSON.stringify(oHeartbeatMessage))
      }, 17000);

      GlobalStore.websocketOnOpen(event);
    };

    oWebsocket.onerror = function (event) {
      GlobalStore.websocketOnError(event);
    };

    oWebsocket.onclose = function (event) {
      GlobalStore.websocketOnClose(event);
    };
  };

  let _sendMessage = (sMessage, oData) => {
    let oMessage = {};
    oMessage.from = GlobalStore.getCurrentUser().id;
    oMessage.message = sMessage;
    oMessage.data = oData;
    oWebsocket.send(JSON.stringify(oMessage));
  }

  return {
    /**
     * Opens new websocket connection with server
     * @param userId
     */
    openSession: function (userId) {
      _openSession();
    },

    /**
     * Closes websocket connection with server
     */

    closeSession: function () {
      if (oWebsocket && CS.isFunction(oWebsocket.close)) {
        oWebsocket.close();
      }
    },

    /**
     * Sends message to the websocket server
     * @param sMessage: refer websocket-constants.js
     * @param oData
     */
    sendMessage: function (sMessage, oData) {
      _sendMessage(sMessage, oData)
    }

  };

})();

export default WebsocketUtils;
