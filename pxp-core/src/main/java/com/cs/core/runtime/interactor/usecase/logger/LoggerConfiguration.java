package com.cs.core.runtime.interactor.usecase.logger;
/*package com.cs.base.interactor.logger;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfiguration {

  private final Logger slf4jErrorLogger = LoggerFactory.getLogger("com.SL4JErrorLogger.log");

  @Autowired
  String               logServerIP;

  @Autowired
  String               logServerPort;

  WebSocketClient      websocketClient;

  @Autowired
  EchoSocket           echoSocket;

  @PostConstruct
  public void initializeLogger()
  {
    try {
      websocketClient = new WebSocketClient();
      String loggerURI = "ws://" + logServerIP + ":" + logServerPort;
      websocketClient.start();
      ClientUpgradeRequest request = new ClientUpgradeRequest();
      websocketClient.connect(echoSocket, new URI(loggerURI), request);
      echoSocket.awaitClose(5, TimeUnit.SECONDS);
    }
    catch (Exception e) {
      String error = CSLogUtil.getStackTrace(e);
      slf4jErrorLogger.error(error);
    }
  }

  public Boolean isStarted()
  {
    return websocketClient.isStarted();
  }

  @PreDestroy
  public void stopSocket()
  {
    if (websocketClient.isStarted()) {
      try {
        websocketClient.stop();
      }
      catch (Exception e) {
        String error = CSLogUtil.getStackTrace(e);
        slf4jErrorLogger.error(error);
      }
    }
  }

}
*/
