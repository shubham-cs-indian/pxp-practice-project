package com.cs.core.bgprocess;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * Run the HTTP REST Server of the BGP application
 *
 * @author vallee
 */
public class BGProcessRESTServer {
  // Reroute log of JETTY to Stderr (unfortunately ineffective because overwritten by slf4j binding)
  private static final StdErrLog jettyLogger =  new StdErrLog();
  static {
    jettyLogger.setLevel( org.eclipse.jetty.util.log.JavaUtilLog.LEVEL_WARN);
    org.eclipse.jetty.util.log.Log.setLog( jettyLogger);
  }
  
  /**
   * Declare the BGP REST service packages to JETTY
   * @param holder
   * @param packages 
   */
  private static void addRestPackages(ServletHolder holder, String... packages)
  {
    holder.setInitParameter("jersey.config.server.provider.packages", String.join(";", packages));
  }
  
  /**
   * Start and initialize the REST Server
   *
   * @throws CSInitializationException
   */
  public static void start() throws CSInitializationException
  {
    // JETTY Server and servlet context initialization
    QueuedThreadPool threadPool = new QueuedThreadPool(
        CSProperties.instance().getInt("httpServer.maxThreads"),
        CSProperties.instance().getInt("httpServer.minThreads"),
        CSProperties.instance().getInt("httpServer.idleTimeout"));
    Server bgpHttpServer = new Server(threadPool);
    // opened port
    ServerConnector connector = new ServerConnector(bgpHttpServer);
    int serverPort = CSProperties.instance().getInt("httpServer.port");
    connector.setPort(serverPort);
    bgpHttpServer.setConnectors(new Connector[] { connector });
    
    // JETTY Resources and static contents
    ResourceHandler staticHandler = new ResourceHandler();
    staticHandler.setDirectoriesListed(true);
    staticHandler.setWelcomeFiles(new String[] { "index.html" });
    staticHandler.setResourceBase("./html");
    
    // REST Servlet management
    ServletContextHandler servicesHandler = new ServletContextHandler(
            bgpHttpServer, "/", ServletContextHandler.SESSIONS);
    ServletHolder restServlet = servicesHandler.addServlet(
            org.glassfish.jersey.servlet.ServletContainer.class, "/REST/*");
    restServlet.setInitOrder(0);
    addRestPackages(restServlet, "com.cs.core.bgprocess.rest");
    
    // General handlers of the server
    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { staticHandler, servicesHandler });
    bgpHttpServer.setHandler(handlers);
    CSInitializationException[] serverException = { null };
    
    Runnable serverTask = () -> {
      try {
        bgpHttpServer.start();
        RDBMSLogger.instance().info("HTTP Server starting on port %d", serverPort);
        bgpHttpServer.dumpStdErr();
        bgpHttpServer.join();
      }
      catch (Exception exc) {
        try {
          RDBMSLogger.instance().exception(exc);
          serverException[0] = new CSInitializationException("HTTP Server ERROR", exc);
          RDBMSLogger.instance().info("HTTP Server shutdown normally.");
          bgpHttpServer.stop();
          bgpHttpServer.destroy();
        } catch (Exception excLevel2) {
          RDBMSLogger.instance().exception(excLevel2);
          RDBMSLogger.instance().info("HTTP Server stopped abnormally.");
        }
      }
    };
    // HTTP Server run in a background thread
    new Thread(serverTask).start();
    if (serverException[0] != null)
      throw serverException[0];
  }
}
