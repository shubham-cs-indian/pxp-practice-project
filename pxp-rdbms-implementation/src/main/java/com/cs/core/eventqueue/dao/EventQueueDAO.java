package com.cs.core.eventqueue.dao;

import com.cs.core.eventqueue.dto.EventDTO;
import com.cs.core.eventqueue.idao.IEventHandler;
import com.cs.core.eventqueue.idao.IEventQueueDAO;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.eventqueue.impl.EventQueueEngine;
import com.cs.core.rdbms.app.RDBMSAppDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.services.resolvers.CalculationSourceHandler;
import com.cs.core.rdbms.services.resolvers.CouplingSourceHandler;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.transactionend.services.handlers.EndOfTransactionHandler;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Event queue and background process services
 *
 * @author vallee
 */
public class EventQueueDAO implements IEventQueueDAO {
  
  private static final String Q_EVENT_BY_ID   = "select * from pxp.eventqueuewithdata where trackIID = ?";
  private static final String Q_EVENT_BY_TYPE = "select * from pxp.eventqueuewithData "
      + "where consumed = 0 and eventType = ? order by posted desc";
  private static final String Q_CONSUME_EVENT = "update pxp.eventqueue "
      + "set consumed = ?, flagged = ? where trackIID = ?";
  private static boolean      initialized     = false;
  
  /**
   * Default constructor
   */
  public EventQueueDAO(RDBMSAppDriver driver) throws RDBMSException
  {
    if (!initialized) {
      synchronized (EventQueueDAO.class) {
        if (!initialized)
          initializePreDefinedHandlers(driver);
      }
    }
  }
  
  /**
   * Declare here predefined handlers
   */
  private static void initializePreDefinedHandlers(RDBMSAppDriver driver)
  {
    EventQueueEngine.instance()
        .subscribe(IEventDTO.EventType.CALCULATION_SOURCE, CalculationSourceHandler.class);
    EventQueueEngine.instance()
        .subscribe(IEventDTO.EventType.RULE_EVALUATION_SOURCE, RuleHandler.class);
    EventQueueEngine.instance()
    .subscribe(IEventDTO.EventType.COUPLING_SOURCE, CouplingSourceHandler.class);
    EventQueueEngine.instance()
        .subscribe(IEventDTO.EventType.END_OF_TRANSACTION, EndOfTransactionHandler.class);

  }
  
  @Override
  public void subscribe(IEventDTO.EventType service, Class<? extends IEventHandler> subscriber)
  {
    EventQueueEngine.instance()
        .subscribe(service, subscriber);
  }
  
  @Override
  public void unsubscribe(IEventDTO.EventType service, Class<? extends IEventHandler> subscriber)
  {
    EventQueueEngine.instance()
        .unsubscribe(service, subscriber);
  }
  
  @Override
  public IEventDTO getEventByID(long eventQueueIID) throws RDBMSException
  {
    final EventDTO event = new EventDTO();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          PreparedStatement cs = currentConn.prepareStatement(Q_EVENT_BY_ID);
          cs.setLong(1, eventQueueIID);
          ResultSet rs = cs.executeQuery();
          while (rs.next()) {
            event.set(currentConn.getResultSetParser(rs));
          }
        });
    return event;
  }
  
  @Override
  public IEventDTO postEvent(IEventDTO.EventType type, long objectIID, long classifierIID, long userIID,
      ITimelineDTO changes, byte[] payload) throws RDBMSException
  {
    Long[] eventIID = { 0L };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection connection) -> {
          eventIID[0] = EventQueueDAS.postEvent( connection, type, objectIID, classifierIID, userIID,
              changes, payload, null, null);
        });
    return getEventByID(eventIID[0]);
  }
  
  @Override
  public IEventDTO peekNextEvent(IEventDTO.EventType eventType) throws RDBMSException
  {
    final EventDTO event = new EventDTO();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          PreparedStatement cs = currentConn.prepareStatement(Q_EVENT_BY_TYPE);
          cs.setInt(1, eventType.ordinal());
          ResultSet rs = cs.executeQuery();
          while (rs.next()) {
            event.set(currentConn.getResultSetParser(rs));
            break;
          }
        });
    return event;
  }
  
  @Override
  public IEventDTO consumeNextEvent(IEventDTO.EventType service) throws RDBMSException
  {
    EventDTO event = (EventDTO) peekNextEvent(service);
    consumeEvent(event);
    return event;
  }
  
  @Override
  public IEventDTO consumeNextEventByID(long eventQueueIID) throws RDBMSException
  {
    EventDTO event = (EventDTO) getEventByID(eventQueueIID);
    consumeEvent(event);
    return event;
  }
  
  private void consumeEvent(EventDTO event) throws RDBMSException
  {
    if (event.getIID() > 0) {
      long consumed = System.currentTimeMillis();
      RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
            QueryRunner query = new QueryRunner();
            query.update(currentConn.getConnection(), Q_CONSUME_EVENT, consumed, consumed, event.getIID());
          });
      event.setConsumedTime(consumed);
    }
  }
}
