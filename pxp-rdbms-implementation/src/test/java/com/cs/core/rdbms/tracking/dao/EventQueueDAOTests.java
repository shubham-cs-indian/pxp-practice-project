package com.cs.core.rdbms.tracking.dao;

import com.cs.core.eventqueue.idao.IEventHandler;
import com.cs.core.eventqueue.idao.IEventQueueDAO;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.eventqueue.impl.EventQueueEngine;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.idao.ILoggerDAO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;

/**
 * /!\ THIS IS A JUNIT TEST THAT MAY LAST LONG, so Ignore it most of the time
 *
 * @author vallee
 */
// @Ignore
public class EventQueueDAOTests extends AbstractRDBMSDriverTests {
  
  private static ILoggerDAO logger = null;
  private IEventQueueDAO    eventQueue;
  
  @Before
  @Override
  public void init() throws RDBMSException
  {
    super.init();
    if (logger == null)
      logger = driver.newLoggerDAO();
    eventQueue = driver.newEventQueueDAO();
  }
  
  @Test
  public void subscribeAndPost() throws RDBMSException, InterruptedException, CSFormatException
  {
    printTestTitle("EventQueueDAOTests");
    eventQueue.subscribe(IEventDTO.EventType.GENERIC_EVENT, Handler.class);
    eventQueue.subscribe(IEventDTO.EventType.GENERIC_EVENT, Handler.class);
    eventQueue.subscribe(IEventDTO.EventType.GENERIC_EVENT, Handler.class);
    eventQueue.postEvent(IEventDTO.EventType.GENERIC_EVENT, 100005, 4000, session.getUserIID(),
        new TimelineDTO(ITimelineDTO.ChangeCategory.AddedChild), null);
    IEventDTO myEvent = eventQueue.peekNextEvent(IEventDTO.EventType.GENERIC_EVENT);
    println("Event peeked from the queue: " + myEvent.toPXON());
    // consume the event
    IEventDTO myEvent2 = eventQueue.consumeNextEvent(IEventDTO.EventType.GENERIC_EVENT);
    System.out.println("Event consumed from the queue: " + myEvent2.toPXON());
    IEventDTO myEvent3 = eventQueue.getEventByID(myEvent2.getEventIID());
    assert (myEvent3.getEventIID() == myEvent2.getEventIID());
    IEventDTO myEvent4 = eventQueue.consumeNextEventByID(myEvent2.getEventIID());
    
    // test the monitoring part:
    eventQueue.unsubscribe(IEventDTO.EventType.GENERIC_EVENT, Handler.class);
    eventQueue.postEvent(IEventDTO.EventType.GENERIC_EVENT, 100006, 4000, session.getUserIID(),
        new TimelineDTO(ITimelineDTO.ChangeCategory.AddedChild), null);
    int secToSleep = 0; // 10;
    println("Sleep for " + secToSleep + " sec. in main task...");
    Thread.sleep(TimeUnit.SECONDS.toMillis(secToSleep));
    if (secToSleep == 0) { // force the monitor to run
      EventQueueEngine.instance().process();
    }
  }
  
  static public class Handler implements IEventHandler {
    
    private final String handlerName = "Test Handler";
    private IEventDTO    currEvent;
    
    public String getID()
    {
      return handlerName;
    }
    
    @Override
    public void setEvent(IEventDTO event)
    {
      currEvent = event;
    }
    
    @Override
    public void run()
    {
      try {
        println("Handler " + handlerName + " received event to run: " + currEvent.toPXON());
      }
      catch (CSFormatException ex) {
        RDBMSLogger.instance().exception(ex);
      }
    }
  }
}
