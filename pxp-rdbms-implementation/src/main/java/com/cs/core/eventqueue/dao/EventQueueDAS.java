package com.cs.core.eventqueue.dao;

import com.cs.core.data.TextArchive;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IRootDTO;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Implementation services to manage event queue
 *
 * @author vallee
 */
public class EventQueueDAS extends RDBMSDataAccessService {
  
  private final UserSessionDTO userSession;
  
  /**
   * DAS constructor
   *
   * @param currentConn the current transaction connection
   * @param session
   */
  public EventQueueDAS(RDBMSConnection currentConn, IUserSessionDTO session)
  {
    super(currentConn);
    this.userSession = (UserSessionDTO) session;
  }

  /**
   * Realize a post event operation inside a current transaction
   *
   * @param currConnection
   * @param eventType
   *          the event type
   * @param objectIID
   *          the object concerned by the event
   * @param userIID
   *          the user at origin of the event
   * @param timelineData
   *          the attached time line data
   * @param payload
   *          any event payload to be compressed in storage
   * @return the new created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static long postEvent( RDBMSConnection currConnection,
      EventType eventType, long objectIID, long classifierIID, long userIID, ITimelineDTO timelineData, byte[] payload, String localeId, String transactionId)
      throws RDBMSException, SQLException, CSFormatException
  {
    byte[] zippedContent = TextArchive.zip(payload);
    IResultSetParser result = currConnection.getFunction( ResultType.IID, "pxp.fn_postTrackingEvent")
        .setInput(ParameterType.INT, eventType.getTopic().ordinal())
        .setInput(ParameterType.INT, eventType.ordinal())
        .setInput(ParameterType.IID, classifierIID)
        .setInput(ParameterType.IID, userIID)
        .setInput(ParameterType.IID, objectIID) 
        .setInput(ParameterType.JSON, timelineData.toJSON())
        .setInput(ParameterType.BLOB, zippedContent)
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.STRING, localeId)
        .setInput(ParameterType.STRING, transactionId)
        .execute();
    return result.getIID();
  }


  public long postProcessingEvent( RDBMSConnection currConnection,
      EventType eventType, long objectIID, long classifierIID, long userIID, ITimelineDTO timelineData, byte[] payload, String localeId)
      throws RDBMSException, SQLException, CSFormatException
  {
    byte[] zippedContent = TextArchive.zip(payload);
    IResultSetParser result = currConnection.getFunction( ResultType.IID, "pxp.fn_postTrackingEvent")
        .setInput(ParameterType.INT, eventType.getTopic().ordinal())
        .setInput(ParameterType.INT, eventType.ordinal())
        .setInput(ParameterType.IID, classifierIID)
        .setInput(ParameterType.IID, userIID)
        .setInput(ParameterType.IID, objectIID)
        .setInput(ParameterType.JSON, timelineData.toJSON())
        .setInput(ParameterType.BLOB, zippedContent)
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.STRING, localeId)
        .setInput(ParameterType.STRING, userSession.getTransactionID())
        .execute();
    return result.getIID();
  }
  
  /**
   * Post an event to indicate entity creation
   *
   * @param entity
   * @return the new created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postEntityEvent(BaseEntityDTO entity, EventType event, TimelineDTO change, String localeId)
      throws RDBMSException, SQLException, CSFormatException
  {
    long eventIID = postProcessingEvent( currentConnection, event, entity.getIID(), entity.getNatureClassifier().getClassifierIID(),
        userSession.getUserIID(), change, entity.toPXON().getBytes(), localeId);
    return eventIID;
  }
  
  /**
   * Post side effect events when objects have been indirectly added or removed
   * from a relation
   *
   * @param eventType
   *          the event type
   * @param holderEntity
   * @param userIID
   * @param relationSets
   *          the list of concerned relations
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  private void postSideEffectEvents(EventType eventType, IBaseEntityDTO holderEntity, long userIID,
      Collection<IRootDTO> relationSets) throws SQLException, RDBMSException, CSFormatException
  {
    ITimelineDTO.ChangeCategory trackingKey = ChangeCategory.Generic;
    switch (eventType) {
      case RELATION_CREATION:
        trackingKey = ChangeCategory.CreatedRelation;
        break;
      case SIDE_RELATION_CREATION:
        trackingKey = ChangeCategory.AddedRelation;
        break;
      case SIDE_RELATION_DELETE:
        trackingKey = ChangeCategory.RemovedRelation;
    }
    for (IRootDTO relationsSet : relationSets) {
      List<Long> sideEntityIIDs = ((RelationsSetDTO) relationsSet).getSideBaseEntityIIDs();
      List<String> trackingData = ((RelationsSetDTO) relationsSet).getSideTrackingData(trackingKey,
          holderEntity);
      driver.getProcedure(currentConnection, "pxp.sp_postSideEffectEvents")
          .setInput(ParameterType.INT, eventType.getTopic()
              .ordinal())
          .setInput(ParameterType.INT, eventType.ordinal())
          .setInput(ParameterType.IID, userIID)
          .setInput(ParameterType.IID_ARRAY, sideEntityIIDs)
          .setInput(ParameterType.STRING_ARRAY, trackingData)
          .setInput(ParameterType.LONG, System.currentTimeMillis())
          .execute();
    }
  }
  
  /**
   * Post implicit events for coupling, calculation and relation side effects
   *
   * @param entity
   * @param catalog
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  private void postImplicitEvents(long trackIID, BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes, String localeID) throws RDBMSException, SQLException, CSFormatException
  {
    /*  Side-effect events are temporary disabled for release batch1a since revision management fails on empty pxondelta content
        Please don't delete the code below.
    // Post side effect events for relation creation/deletion
    if (changes.contains(ChangeCategory.CreatedRelation)) {
      Collection<IRootDTO> relationSets = changes.getRegisteredList(ChangeCategory.CreatedRelation);
      postSideEffectEvents(EventType.RELATION_CREATION, entity, userSession.getUserIID(),
          relationSets);
    }
    if (changes.contains(ChangeCategory.AddedRelation)) {
      Collection<IRootDTO> addedRelationsSet = changes
          .getRegisteredList(ChangeCategory.AddedRelation);
      postSideEffectEvents(EventType.SIDE_RELATION_CREATION, entity, userSession.getUserIID(),
          addedRelationsSet);
    }
    if (changes.contains(ChangeCategory.RemovedRelation)) {
      Collection<IRootDTO> removedRelationsSet = changes
          .getRegisteredList(ChangeCategory.RemovedRelation);
      postSideEffectEvents(EventType.SIDE_RELATION_DELETE, entity, userSession.getUserIID(),
          removedRelationsSet);
    }
    */ 
    // Post event for calculation source update or  coupling source update
    if (/*changes.contains(ChangeCategory.CalculationSource) || */changes.contains(ChangeCategory.CouplingSource)) {
      postEventRedirection(EventType.CALCULATION_SOURCE, trackIID, localeID);
    }
    
    if (changes.contains(ChangeCategory.UpdatedRecord)
        || changes.contains(ChangeCategory.CreatedRecord)
        || changes.contains(ChangeCategory.AddedClassifier)
        || changes.contains(ChangeCategory.RemovedClassifier)) {
      postEventRedirection(EventType.RULE_EVALUATION_SOURCE, trackIID, localeID);
    }
    
    if (changes.contains(ChangeCategory.UpdatedRecord)
        || changes.contains(ChangeCategory.AddedClassifier)
        || changes.contains(ChangeCategory.RemovedClassifier)) {
      postEventRedirection(EventType.COUPLING_SOURCE, trackIID, localeID);

    }
  }
  
  /**
   * Post an event to indicate records creation
   *
   * @param entity
   * @param catalog
   * @param timeline
   * @return the new created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postRecordsCreation(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO timeline) throws RDBMSException, SQLException, CSFormatException
  {
    if (!timeline.contains(ChangeCategory.CreatedRecord))
      return 0L;

    // Post main event of record creation
    long createTrackIID = postProcessingEvent( currentConnection, EventType.OBJECT_UPDATE,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), timeline, entity.toPXON().getBytes(), catalog.getLocaleID());
    postImplicitEvents(createTrackIID, entity, catalog, timeline, catalog.getLocaleID());
    return createTrackIID;
  }
  
  /**
   * Post an event to indicate records deletion
   *
   * @param entity
   * @param catalog
   * @param changes
   * @return
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postRecordsDeletion(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes) throws RDBMSException, SQLException, CSFormatException
  {
    if (!changes.contains(ChangeCategory.DeletedRecord))
      return 0L;
    // Post main event of record creation
    long createTrackIID = postProcessingEvent( currentConnection, EventType.OBJECT_UPDATE,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), changes, entity.toPXON().getBytes(), catalog.getLocaleID());
    postImplicitEvents(createTrackIID, entity, catalog, changes, catalog.getLocaleID());
    return createTrackIID;
  }
  
  /**
   * Post an event to indicate records update
   *
   * @param entity
   * @param catalog
   * @return the new created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postRecordsUpdate(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes) throws RDBMSException, SQLException, CSFormatException
  {
    if (!changes.contains(ChangeCategory.UpdatedRecord))
      return 0L;
    // Post main event of record update
    long updateTrackIID = postProcessingEvent( currentConnection, EventType.OBJECT_UPDATE,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), changes, entity.toPXON().getBytes(), catalog.getLocaleID());
    postImplicitEvents(updateTrackIID, entity, catalog, changes, catalog.getLocaleID());
    return updateTrackIID;
  }

  /**
   * Post an event to indicate coupling update
   *
   * @param entity
   * @param catalog
   * @return the new created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postCouplingUpdate(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes) throws RDBMSException, SQLException, CSFormatException
  {
    if (!changes.contains(ChangeCategory.CouplingSource))
      return 0L;
    // Post main event of record update
    long updateTrackIID = postProcessingEvent( currentConnection, EventType.OBJECT_UPDATE,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), changes, entity.toPXON().getBytes(), catalog.getLocaleID());
    postImplicitEvents(updateTrackIID, entity, catalog, changes, catalog.getLocaleID());
    return updateTrackIID;
  }

  /**
   * Post an event to indicate coupling delte
   *
   * @param entity
   * @param catalog
   * @return the new created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postCouplingDelete(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes) throws RDBMSException, SQLException, CSFormatException
  {
    if (!changes.contains(ChangeCategory.RemovedCouplingSource))
      return 0L;
    // Post main event of record update
    long updateTrackIID = postProcessingEvent( currentConnection, EventType.OBJECT_UPDATE,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), changes, entity.toPXON().getBytes(), catalog.getLocaleID());
    postImplicitEvents(updateTrackIID, entity, catalog, changes, catalog.getLocaleID());
    return updateTrackIID;
  }
  /**
   * Post an event that is directed to another existing trackiid
   *
   * @param eventType
   *          the event type
   * @param trackIID
   *          the existing tracking IID
   * @throws SQLException
   * @throws RDBMSException
   */
  public void postEventRedirection(EventType eventType, long trackIID, String localeID)
      throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_postEventRedirection")
        .setInput(ParameterType.INT, eventType.getTopic().ordinal())
        .setInput(ParameterType.INT, eventType.ordinal())
        .setInput(ParameterType.IID, trackIID)
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.STRING, localeID)
        .execute();
  }
  
  /**
   * Post an event to indicate classifier added
   *
   * @param entity
   * @param catalog
   * @return the new created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postClassifierAdded(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes) throws RDBMSException, SQLException, CSFormatException
  {
    if (!changes.contains(ChangeCategory.AddedClassifier))
      return 0L;
    // Post main event of record update
    long trackIID = postProcessingEvent( currentConnection, EventType.CLASSIFIER_ADDED,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), changes, entity.toPXON().getBytes(),
        catalog.getLocaleID());
    postImplicitEvents(trackIID, entity, catalog, changes, catalog.getLocaleID());
    return trackIID;

  }
  
  public long postClassifierRemoved(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes) throws RDBMSException, SQLException, CSFormatException
  {
    if (!changes.contains(ChangeCategory.RemovedClassifier))
      return 0L;
    // Post main event of record update
    long trackIID = postProcessingEvent( currentConnection, EventType.CLASSIFIER_REMOVED,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), changes, entity.toPXON().getBytes(), catalog.getLocaleID());
    postImplicitEvents(trackIID, entity, catalog, changes, catalog.getLocaleID());
    return trackIID;

  }
  
  /**
   * Post an event to indicate added language translations to an entity
   * @param entity
   * @param catalog
   * @param changes
   * @return the newly created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postAddedLanguageTranslations(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes) throws RDBMSException, SQLException, CSFormatException
  {
    if (!changes.contains(ChangeCategory.CreatedTranslation))
      return 0L;
    // Post main event of record update
    long trackIID = postProcessingEvent( currentConnection, EventType.TRANSLATION_ADDED,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), changes, entity.toPXON().getBytes(),
        catalog.getLocaleID());
    postImplicitEvents(trackIID, entity, catalog, changes, catalog.getLocaleID());
    return trackIID;

  }
  
  /**
   * Post an event to indicate deleted translation from an entity
   * @param entity
   * @param catalog
   * @param changes
   * @return the newly created event IID
   * @throws RDBMSException
   * @throws SQLException
   * @throws CSFormatException
   */
  public long postDeletedLanguageTranslations(BaseEntityDTO entity, ILocaleCatalogDTO catalog,
      TimelineDTO changes) throws RDBMSException, SQLException, CSFormatException
  {
    if (!changes.contains(ChangeCategory.DeletedTranslation))
      return 0L;
    // Post main event of record update
    long trackIID = postProcessingEvent( currentConnection, EventType.TRANSLATION_REMOVED,
        entity.getIID(), entity.getNatureClassifier().getClassifierIID(), userSession.getUserIID(), changes, entity.toPXON().getBytes(),
        catalog.getLocaleID());
    postImplicitEvents(trackIID, entity, catalog, changes, catalog.getLocaleID());
    return trackIID;

  }
  
}
