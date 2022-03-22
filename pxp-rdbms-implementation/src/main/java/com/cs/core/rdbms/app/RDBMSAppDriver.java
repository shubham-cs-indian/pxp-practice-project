package com.cs.core.rdbms.app;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.eventqueue.dao.EventQueueDAO;
import com.cs.core.eventqueue.idao.IEventQueueDAO;
import com.cs.core.eventqueue.impl.EventQueueEngine;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.iapp.IRDBMSAppDriver;
import com.cs.core.rdbms.revision.dao.RevisionDAO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.tracking.dao.ObjectTrackingDAO;
import com.cs.core.rdbms.tracking.dao.UserSessionDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idao.ILoggerDAO;
import com.cs.core.rdbms.tracking.idao.IObjectTrackingDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEParser;
import com.cs.core.technical.irdbms.IRDBMSDriver;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Common behavior between PGSQL and ORACLE drivers
 *
 * @author vallee
 */
public final class RDBMSAppDriver implements IRDBMSAppDriver {
  
  public static final long   ADMIN_USER_IID   = IStandardConfig.StandardUser.admin.getIID();
  public static final String ADMIN_USER_NAME  = IStandardConfig.StandardUser.admin.toString();
  // Predefined property values
  public static final String DEFAULT_EQSERVER = "localhost";
  // Encapsulated technical driver
  private final IRDBMSDriver driver;
  
  /**
   * Constructor with encapsulating the technical driver
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public RDBMSAppDriver() throws RDBMSException
  {
    this.driver = RDBMSDriverManager.getDriver();
    // To optimize initialization, a first connection is acquired and released
    // for first time
    RDBMSConnection conn = RDBMSConnectionManager.instance()
        .acquireConnection();
    RDBMSConnectionManager.instance()
        .releaseConnection(conn);
    // start the event queue engine
    try {
      String eventQueueServerName = CSProperties.instance().getString("eventqueue.server");
      String hostName = InetAddress.getLocalHost().getHostName();
      if (eventQueueServerName != null && (eventQueueServerName.equals(DEFAULT_EQSERVER)
          || eventQueueServerName.equals(hostName))) {
        int eqPollInterval = CSProperties.instance().getInt("eventqueue.pollInterval");
        int pollSize = CSProperties.instance().getInt("eventqueue.threadPoolSize");
        int batchSize = CSProperties.instance().getInt("eventqueue.batchSize");
        pollSize = ( pollSize < 4 ? 4 : pollSize ); // minimum 4 threads are initialized
        RDBMSLogger.instance().info(String.format(
          "Event Queue Polling (size %d) started on %s, polling every %d ms", pollSize, hostName, eqPollInterval));
        EventQueueEngine.instance().startMonitoring(eqPollInterval, pollSize, batchSize);
        // initialize the predefined handler by calling the constructor on first time:
        newEventQueueDAO(); 
      }
      else {
        RDBMSLogger.instance().warn( "No event handler is running on server: " + hostName);
      }
    }
    catch (UnknownHostException | CSInitializationException ex) {
      throw new RDBMSException(0, "SYSTEM", ex);
    }
  }
  
  @Override
  public IConfigurationDAO newConfigurationDAO()
  {
    return ConfigurationDAO.instance();
  }
  
  @Override
  public IUserSessionDAO newUserSessionDAO() throws RDBMSException
  {
    return new UserSessionDAO();
  }
  
  @Override
  public IObjectTrackingDAO newObjectTrackingDAO()
  {
    return new ObjectTrackingDAO();
  }
  
  @Override
  public IRevisionDAO newRevisionDAO(IUserSessionDTO userSession)
  {
    return new RevisionDAO((UserSessionDTO) userSession);
  }
  
  @Override
  public IEventQueueDAO newEventQueueDAO() throws RDBMSException
  {
    return new EventQueueDAO(this);
  }

  @Override
  public void shutdown() throws RDBMSException
  {
    RDBMSLogger.instance().info("Shutdown server order received.");
    IUserSessionDAO userSession = newUserSessionDAO();
    userSession.shutdownSessions();
    driver.shutdown(); // execute the technical shutdown part
  }
  
  @Override
  public ICSEParser newParser()
  {
    return driver.newParser();
  }
  
  @Override
  public ILoggerDAO newLoggerDAO()
  {
    return driver.newLoggerDAO();
  }
  
  @Override
  public String getDatabaseVendor()
  {
    return driver.getDatabaseVendor();
  }
  
  @Override
  public String getDriverClassName()
  {
    return driver.getDriverClassName();
  }
  
  @Override
  public String newUniqueID(String prefix) throws RDBMSException
  {
    if (prefix == null || prefix.isEmpty()) {
      throw new RDBMSException(0, "SYSTEM", "Prefix can not be empty");
    }
    else if (!Character.isAlphabetic(prefix.charAt(0))) {
      throw new RDBMSException(0, "SYSTEM", "First character should be albhabet");
    }
    String[] uniqueId = { null };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          IResultSetParser result = currentConn.getFunction( ResultType.STRING, "pxp.fn_createuniqueid")
              .setInput(ParameterType.STRING, prefix)
              .execute();
          uniqueId[0] = result.getString();
        });
    return uniqueId[0];
  }
}
