package com.cs.core.rdbms.tracking.dao;

import com.cs.core.data.TextArchive;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.tracking.idao.IObjectTrackingDAO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITrackingDTO.TrackingEvent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of IObjectTrackingDAO
 *
 * @author vallee
 */
public class ObjectTrackingDAO implements IObjectTrackingDAO {

  @Override
  public void createObjectTracking(long baseEntityIID, long classifierIID, long userIID, String trackinData)
          throws RDBMSException {
    // Built-up a JSON content
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      IResultSetParser result = connection.getFunction( ResultType.IID, "fn_trackObject")
              .setInput(ParameterType.IID, baseEntityIID)
              .setInput(ParameterType.IID, classifierIID)
              .setInput(ParameterType.IID, userIID)
              .setInput(ParameterType.INT, TrackingEvent.CUSTOM.ordinal())
              .setInput(ParameterType.JSON, trackinData)
              .setInput(ParameterType.BLOB, (new byte[0]))
              .setInput(ParameterType.LONG, System.currentTimeMillis())
              .execute();
    });
  }

  public void createObjectTracking(long baseEntityIID, long classifierIID, long userIID, String trackingData,
      TrackingEvent trackingEvent, byte[] payload,long trackingTimeStamp ) throws RDBMSException, CSFormatException
  {
    // Built-up a JSON content
    byte[] zippedContent = TextArchive.zip(payload);
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      IResultSetParser result = connection.getFunction( ResultType.IID, "pxp.fn_trackObject")
          .setInput(ParameterType.IID, baseEntityIID)
          .setInput(ParameterType.IID, classifierIID)
          .setInput(ParameterType.IID, userIID)
          .setInput(ParameterType.INT, trackingEvent.ordinal())
          .setInput(ParameterType.JSON, trackingData)
          .setInput(ParameterType.BLOB, zippedContent)
          .setInput(ParameterType.LONG, trackingTimeStamp)
          .setInput(ParameterType.STRING, UUID.randomUUID().toString())
          .execute();
    });
  }

  public long createObjectTrackingWithTrack(long baseEntityIID, long classifierIID, long userIID, String trackingData,
      TrackingEvent trackingEvent, byte[] payload,long trackingTimeStamp ) throws RDBMSException, CSFormatException
  {
    // Built-up a JSON content
    AtomicLong trackIID = new AtomicLong(0L);
    byte[] zippedContent = TextArchive.zip(payload);
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      IResultSetParser result = connection.getFunction(ResultType.IID, "pxp.fn_trackObject")
          .setInput(ParameterType.IID, baseEntityIID)
          .setInput(ParameterType.IID, classifierIID)
          .setInput(ParameterType.IID, userIID)
          .setInput(ParameterType.INT, trackingEvent.ordinal())
          .setInput(ParameterType.JSON, trackingData)
          .setInput(ParameterType.BLOB, zippedContent)
          .setInput(ParameterType.LONG, trackingTimeStamp)
          .setInput(ParameterType.STRING, UUID.randomUUID().toString())
          .execute();

      trackIID.set(result.getIID());
    });
    return trackIID.get();
  }

  @Override
  public IRDBMSOrderedCursor<IObjectTrackingDTO> getLastChangesByUser(String userID)
          throws RDBMSException {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public IRDBMSOrderedCursor<IObjectTrackingDTO> getLastChangesByUser(String userID, long afterTime)
          throws RDBMSException {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public IRDBMSOrderedCursor<IObjectTrackingDTO> getLastChangesByObject(String objectID)
          throws RDBMSException {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public IRDBMSOrderedCursor<IObjectTrackingDTO> getLastChangesByObject(String objectID,
          long afterTime) throws RDBMSException {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
}
