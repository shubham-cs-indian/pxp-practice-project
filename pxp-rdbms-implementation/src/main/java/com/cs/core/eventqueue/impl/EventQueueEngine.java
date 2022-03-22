package com.cs.core.eventqueue.impl;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.eventqueue.dto.EventDTO;
import com.cs.core.eventqueue.idao.IEventHandler;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Represents the background process that periodically poll the database in
 * order to notify event subscribers.
 *
 * @author vallee
 */
@Service
public class EventQueueEngine{
  
  // singleton implementation
  private final static EventQueueEngine                      INSTANCE = new EventQueueEngine();                                                                     /*= new EventQueueEngine();*/
  private static final String                          Q_UNFLAGGED_EVENTS  = "select * from pxp.eventQueueWithData where flagged = 0"
      + " order by posted asc limit ?";
  private static final String                          Q_UPDATE_EVENT_FLAG = "update pxp.eventQueue set flagged = ? "
      + "  where flagged = 0 and trackIID = ? and eventtype = ? ";
  private final ScheduledExecutorService               schedulers          = Executors.newScheduledThreadPool(1);
  private final Map<IEventDTO.EventType, List<String>> subscribers         = new HashMap<>();
  private int                                          threadPoolSize      = 1;
  private int                                          batchSize           = 100;
  private ExecutorService                              executor;
  protected static final String                        RDBMS_HOME          = System.getenv("PXP_RDBMS_HOME");
  
  public static EventQueueEngine instance()
  {
    return INSTANCE;
  }
  
  /**
   * Core process that can be called independently for debugging or
   * administration purposes
   *
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void process() throws RDBMSException
  {
    List<EventDTO> detectedEvents = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      // select events with flagged = 0
      PreparedStatement stmt = currentConn.prepareStatement(Q_UNFLAGGED_EVENTS);
      stmt.setInt(1, batchSize);
      stmt.execute();
      IResultSetParser rsParser = currentConn.getResultSetParser(stmt.getResultSet());
      PreparedStatement statement = currentConn.prepareStatement(Q_UPDATE_EVENT_FLAG);
      long currentTime = System.currentTimeMillis();
      while (rsParser.next()) {
        EventDTO event = new EventDTO();
        event.set(rsParser);
        detectedEvents.add(event);
        statement.setLong(1, currentTime);
        statement.setLong(2, event.getIID());
        statement.setInt(3, event.getType().ordinal());
        statement.addBatch();
      }
      statement.executeBatch();
    });
    // Now call handlers with the registered events
    detectedEvents.forEach(currEvent -> {
      IEventDTO.EventType service = currEvent.getType();
      if (subscribers.containsKey(service) && subscribers.get(service) != null) {
        subscribers.get(service).forEach(handler -> {
          try {
            Class handlerClass = Class.forName(handler);
            IEventHandler eventHandler = (IEventHandler) handlerClass.getDeclaredConstructor().newInstance();
            eventHandler.setEvent(currEvent);
            // run as background task from the thread pool
            executor.execute(eventHandler);
          }
          catch (Exception e) {
            RDBMSLogger.instance().warn(String.format("Exception on handler %s / event %s", handler, currEvent.toString()));
            RDBMSLogger.instance().exception(e);
          }
        });
      }
    });
  }

  /**
   * The method runs in transaction boundary (Programmatic transaction)
   * The transaction is added here to provide a connection to read the new event and subscribe them
   * @throws RDBMSException
   */
  private void runTransaction() throws RDBMSException
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        try {
          process();
        }
        catch (RDBMSException e) {
          RDBMSLogger.instance().exception(e);
          status.setRollbackOnly();
        }
      }
    });
  }
  
  /**
   * @param service event queue service of subscription
   * @param subscriber the subscriber handler
   */
  public void subscribe(IEventDTO.EventType service, Class<? extends IEventHandler> subscriber)
  {
    List<String> serviceSubscribers = subscribers.get(service);
    if (serviceSubscribers == null) {
      serviceSubscribers = new ArrayList<>();
      subscribers.put(service, serviceSubscribers);
    }
    subscribers.get(service).add(subscriber.getName());
  }
  
  /**
   * Start the execution of monitoring
   *
   * @param pollInterval defines the interval of polling in milliseconds
   * @param threadPoolSize the size of thread pool that runs handlers
   */
  public void startMonitoring(int pollInterval, int threadPoolSize, int batchSize)
  {
    if (threadPoolSize > 1) {
      this.threadPoolSize = threadPoolSize;
    }
    if (batchSize > 100) {
      this.batchSize = batchSize;
    }
    executor = new CustomExecutorService(this.threadPoolSize);
    schedulers.scheduleAtFixedRate(new MonitoringProcess(), 0, pollInterval, TimeUnit.MILLISECONDS);
  }
  
  /**
   * @param service event queue service of subscription
   * @param subscriber the subscriber handler
   */
  public void unsubscribe(IEventDTO.EventType service, Class<? extends IEventHandler> subscriber)
  {
    List<String> serviceSubscribers = subscribers.get(service);
    if (serviceSubscribers == null) {
      return; // no subscription for this service
    }
    for (int i = 0; i < serviceSubscribers.size(); i++) {
      if (serviceSubscribers.get(i).equals(subscriber.getName())) {
        serviceSubscribers.remove(i);
      }
    }
  }
  
  /**
   * Core process that monitors events
   */
  class MonitoringProcess implements Runnable {
    
    @Override
    public void run()
    {
      try {
        EventQueueEngine.instance().runTransaction();
      }
      catch (RDBMSException ex) {
        RDBMSLogger.instance().exception(ex);
      }
    }
  }
  
}
