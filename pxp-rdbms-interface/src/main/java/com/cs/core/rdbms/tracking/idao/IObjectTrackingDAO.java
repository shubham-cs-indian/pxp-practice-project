package com.cs.core.rdbms.tracking.idao;

import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Object tracking helps the user understand what last modification happened on an entity. A tracking information is produced in the
 * following conditions: - when an entity is created (unique case of creation event) - when an embedded child is added to an entity - when
 * an embedded child is removed from an entity - when classifiers are added to an entity - when classifiers are removed from an entity -
 * when a relation is created (both side entities are concerned) - when a relation is removed (both side entities are concerned) - when
 * entity properties are created or modified - when an entity is deleted (unique case of delete event) - when using the below interface to
 * add special event (custom event)
 *
 * @author vallee
 */
public interface IObjectTrackingDAO {
  /**
   * register a special change in the object tracking
   *
   * @param baseEntityIID the RDBMS internal ID of the base entity to track
   * @param userIID the RDBMS internal ID of the user who is author of the change
   * @param trackingData any kind of JSON content attached to the change
   * @throws RDBMSException
   */
  public void createObjectTracking(long baseEntityIID, long classifierIID, long userIID, String trackingData)
          throws RDBMSException;

  /**
   * Retrieve all changes committed by a user order by change date descending
   *
   * @param userID the related user ID
   * @return a cursor on changes (warning cursor must be closed after user)
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IObjectTrackingDTO> getLastChangesByUser(String userID)
          throws RDBMSException;

  /**
   * register a special change in the object tracking
   *
   * @param baseEntityIID the RDBMS internal ID of the base entity to track
   * @param userIID the RDBMS internal ID of the user who is author of the change
   * @param trackingData any kind of JSON content attached to the change
   * @throws RDBMSException
   */
  public void createObjectTracking(long baseEntityIID, long classifierIID, long userIID, String trackingData,
      ITrackingDTO.TrackingEvent trackingEvent, byte[] payload,long trackingTimeStamp ) throws RDBMSException, CSFormatException;

  /**
   * register a special change in the object tracking
   *
   * @param baseEntityIID the RDBMS internal ID of the base entity to track
   * @param userIID the RDBMS internal ID of the user who is author of the change
   * @param trackingData any kind of JSON content attached to the change
   * @throws RDBMSException
   */
  public long createObjectTrackingWithTrack(long baseEntityIID, long classifierIID, long userIID, String trackingData,
      ITrackingDTO.TrackingEvent trackingEvent, byte[] payload,long trackingTimeStamp ) throws RDBMSException, CSFormatException;

  /**
   * Retrieve all changes committed by a user order by change date descending
   *
   * @param userID the related user ID
   * @param afterTime filtering system time (only changes after this time are returned)
   * @return a cursor on changes (warning cursor must be closed after user)
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IObjectTrackingDTO> getLastChangesByUser(String userID, long afterTime)
          throws RDBMSException;

  /**
   * Retrieve all changes committed on an object order by change date descending
   *
   * @param objectID the related object ID
   * @return a cursor on changes (warning cursor must be closed after use)
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IObjectTrackingDTO> getLastChangesByObject(String objectID)
          throws RDBMSException;

  /**
   * Retrieve all changes committed on an object order by change date descending
   *
   * @param objectID the related object ID
   * @param afterTime filtering system time (only changes after this time are returned)
   * @return a cursor on changes (warning cursor must be closed after use)
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IObjectTrackingDTO> getLastChangesByObject(String objectID,
          long afterTime) throws RDBMSException;
}
