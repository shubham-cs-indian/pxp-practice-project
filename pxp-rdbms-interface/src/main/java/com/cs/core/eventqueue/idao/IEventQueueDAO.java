package com.cs.core.eventqueue.idao;

import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * PXP Event Queue Management An event is created on process and data changes. The list of data changes happen in the following cases with
 * the corresponding constants from IEventDTO.Service: - when an entity is created: OBJECT_CREATION - when entity properties are created or
 * modified: OBJECT_UPDATE - when an entity is deleted: OBJECT_DELETE - when an embedded child is added to an entity: CHILDREN_ADDED - when
 * an embedded child is removed from an entity: CHILDREN_REMOVED - when classifiers are added to an entity: CLASSIFIER_ADDED - when
 * classifiers are removed from an entity: CLASSIFIER_REMOVED - when a relation is created (both side entities are concerned):
 * RELATION_CREATION - when a relation is removed (both side entities are concerned: RELATION_DELETE
 *
 * @author vallee
 */
public interface IEventQueueDAO {

  /**
   * Subscribe to type of event for differed execution /!\ subscriptions are not persistent - they must be renewed each time the RDBMS
   * component is restarted
   *
   * @param service the service to be executed (i.e. OBJECT_UPDATE, etc.)
   * @param subscriber the handler class that should be executed when such event is posted in the queue
   */
  public void subscribe(IEventDTO.EventType service, Class<? extends IEventHandler> subscriber);

  /**
   * Unsubscribe a handler from a type of event
   *
   * @param service the concerned service
   * @param subscriber the handler class which has been previously subscribed
   */
  public void unsubscribe(IEventDTO.EventType service, Class<? extends IEventHandler> subscriber);

  /**
   * Exclusively access event information
   *
   * @param eventQueueIID the event unique IID
   * @return the corresponding event information
   * @throws RDBMSException
   */
  public IEventDTO getEventByID(long eventQueueIID) throws RDBMSException;

  /**
   * Create a new event in the queue
   *
   * @param type the concerned type of event
   * @param objectIID the object IID mainly concerned by the event
   * @param userIID the user identifier
   * @param description description of changes
   * @param payload, any informal data (compressed in storage) attached to the event or null
   * @return the created event information
   * @throws RDBMSException
   */
  public IEventDTO postEvent(IEventDTO.EventType type, long objectIID, long classifierIID, long userIID,
          ITimelineDTO description, byte[] payload) throws RDBMSException;

  /**
   * Read the next event without changing the queue
   *
   * @param type the concerned type of event
   * @return the related event information
   * @throws RDBMSException
   */
  public IEventDTO peekNextEvent(IEventDTO.EventType type) throws RDBMSException;

  /**
   * Exclusively access the queue to consume and update the next event
   *
   * @param type the concerned type of event
   * @return the updated event information
   * @throws RDBMSException
   */
  public IEventDTO consumeNextEvent(IEventDTO.EventType type) throws RDBMSException;

  /**
   * Exclusively access the queue to consume and update an identified event
   *
   * @param eventQueueIID the unique event identifier
   * @return the updated event information
   * @throws RDBMSException
   */
  public IEventDTO consumeNextEventByID(long eventQueueIID) throws RDBMSException;
}
