package com.cs.core.rdbms.revision.dao;

import com.cs.core.data.Text;
import com.cs.core.data.TextArchive;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.revision.dto.ObjectRevisionDTO;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RevisionDAS extends RDBMSDataAccessService {
  
  private static final String Q_GET_OBJECT_REVISIONS    = "select * from pxp.objectrevisionfullcontent where objectIID = ? And isArchived = ? ";
  private static final String Q_GET_MAX_TRACK_IID       = "select max(trackiid) maxiid from pxp.objectRevision where objectiid = ? ";
  private static final String Q_GET_LAST_REVISION       = "select r.created, r.revisionNo, r.revisionComment, " + 
      "  r.revisionTimeline, r.objectArchive, r.isarchived, r.objectiid, r.trackiid, " + 
      "  ot.objectid, ot.catalogcode, ot.organizationcode, ot.useriid, ot.posted, ot.event, "
      + " ot.timelinedata, ot.pxondelta, ot.username " + 
      " from pxp.objectRevision r " + 
      " join pxp.objectTrackingWithUser ot on ot.trackIID = r.trackIID " + 
      " where r.objectiid = ? and r.trackiid = ? ";
  
  private static final String Q_GET_LAST_REVISION_WITHOUTTIMELINE      = "select r.created, r.revisionNo, r.revisionComment, "
      + "  r.revisionTimeline, r.objectArchive, r.isarchived, r.objectiid, r.trackiid from pxp.objectRevision r "
      + " where r.objectiid = ? and r.trackiid = (select max(trackiid)  from pxp.objectRevision where objectiid = ?)";
  
  /* private static final String Q_GET_LAST_USER_TRACKINGS = "select * from pxp.objectTrackingWithUser ot where "
      + "ot.objectIID = ? and "
      + "trackIID <= ( select trackiid from pxp.lastUserObjectTracking where userIID = ? and objectIID = ot.objectIID ) "
      + "and trackIID > ? order by posted asc";*/
  
  private static final String Q_GET_LAST_USER_TRACKINGS                = " select * from pxp.objectTrackingWithUser ot where ot.objectIID = ? and  trackIID > ?   and  userIID = ? order by posted asc";
  
  private static final String Q_GET_OBJECT_TRACKINGS_BY_TRANSACTION_ID = "select * from pxp.objectTrackingWithUser where "
      + " transactionid = ( select transactionid from pxp.objecttracking where trackiid = ?)";
  
  private static final String Q_GET_NUMBER_OF_VERSIONS_IN_TIMELINE = "Select count(*) from pxp.objectRevision where objectIID = ? And isArchived = false";
  private static final String Q_UPDATE_REVISIONS = "update pxp.objectrevision set isarchived = ? where objectIID = ? and revisionno in (%s)";
  private static final String Q_DELETE_REVISIONS = "Delete from pxp.objectrevision where objectIID = ? and revisionno in (%s)";
  private static final String Q_GET_ALL_TRACKINGS = "select * from pxp.objectTrackingWithUser where objectIID = ? and (timelinedata ->> 'UpdatedRecord' IS NOT NULL or timelinedata ->> 'CreatedRecord' IS NOT NULL)";
  
  public RevisionDAS( RDBMSConnection connection)
  {
    super(connection);
  }
  
  /**
   * Create an object revision and automatically returns the new revision No
   *
   * @param objectIID
   * @param created
   * @param comment
   * @param trackIID
   * @param timeline
   * @param pxonArchive
   * @param assetObjectKey 
   * @return the new revision No
   * @throws SQLException
   * @throws RDBMSException
   */


  public int createObjectRevision(long objectIID, long classifierIID, long created, String comment, long trackIID,
      JSONContent timeline, String pxonArchive, Boolean isArchived, String assetObjectKey) throws SQLException, RDBMSException
  {
    try {
      byte[] archivedContent = TextArchive.zip(pxonArchive);
      IResultSetParser result = driver
          .getFunction(currentConnection, ResultType.INT, "pxp.fn_createObjectRevision")
          .setInput(ParameterType.IID, objectIID)
          .setInput(ParameterType.IID, classifierIID)
          .setInput(ParameterType.LONG, created)
          .setInput(ParameterType.STRING, comment)
          .setInput(ParameterType.LONG, trackIID)
          .setInput(ParameterType.JSON,
              timeline == null || timeline.isEmpty() ? null : timeline.toString())
          .setInput(ParameterType.BLOB, archivedContent)
          .setInput(ParameterType.BOOLEAN, isArchived == null ? false: isArchived)
          .setInput(ParameterType.STRING, assetObjectKey.isEmpty() ? null : assetObjectKey)
          .execute();
      return result.getInt();
    }
    catch (CSFormatException ex) {
      throw new RDBMSException(20000, "ARCHIVE Failure", ex);
    }
  }


  /**
   * Create an object revision and automatically returns the new revision No
   *
   * @param objectIID
   * @param created
   * @param comment
   * @param trackIID
   * @param timeline
   * @param pxonArchive
   * @param assetObjectKey
   * @return the new revision No
   * @throws SQLException
   * @throws RDBMSException
   */
  public static final String insert_query ="insert into pxp.ObjectRevision (objectIID, classifierIID, created, revisionNo, revisionComment,"
      + " trackIID, revisionTimeline, objectArchive, isArchived, assetObjectKey) "
      + " values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public void bulkCreateZeroRevision(long objectIID, long classifierIID, long created, long trackIID,
      JSONContent timeline, String pxonArchive, String assetObjectKey) throws SQLException, RDBMSException
  {
    try {
      PGobject jsonObject = new PGobject();
      jsonObject.setType("json");
      jsonObject.setValue(timeline.toString());

      byte[] archivedContent = TextArchive.zip(pxonArchive);
      PreparedStatement pstmt = currentConnection.prepareStatement(insert_query);
      pstmt.setLong(1, objectIID);
      pstmt.setLong(2, classifierIID);
      pstmt.setLong(3, created);
      pstmt.setInt(4, 0);
      pstmt.setString(5, "First Revision");
      pstmt.setLong(6, trackIID);
      pstmt.setObject(7, jsonObject);
      pstmt.setBytes(8, archivedContent);
      pstmt.setBoolean(9, false);
      pstmt.setString(10, assetObjectKey);
      pstmt.execute();
    }
    catch (CSFormatException ex) {
      throw new RDBMSException(20000, "ARCHIVE Failure", ex);
    }
  }
  
  /**
   * @param objectIID
   *          the concerned Object IID
   * @return the last revision DTO related to that object
   * @throws SQLException
   * @throws RDBMSException
   */
  public ObjectRevisionDTO getLastObjectRevision(long objectIID) throws SQLException, RDBMSException
  {
    long maxTrackIID = 0;
    PreparedStatement queryForMax = currentConnection.prepareStatement(Q_GET_MAX_TRACK_IID);
    queryForMax.setLong(1, objectIID);
    IResultSetParser rsForMaxTrackIID = driver.getResultSetParser(queryForMax.executeQuery());
    if(rsForMaxTrackIID.next()) {
      maxTrackIID = rsForMaxTrackIID.getLong("maxiid");
    }
    else {
      return new ObjectRevisionDTO(); // return empty object
    }
    
    if(maxTrackIID != 0) {
      PreparedStatement query = currentConnection.prepareStatement(Q_GET_LAST_REVISION);
      query.setLong(1, objectIID);
      query.setLong(2, maxTrackIID);
      IResultSetParser rs = driver.getResultSetParser(query.executeQuery());
      if (rs.next()) {
        try {
          return new ObjectRevisionDTO(rs);
        }
        catch (CSFormatException ex) {
          throw new RDBMSException(0, "Format", ex);
        }
      }
    }

    return new ObjectRevisionDTO(); // return empty object
  }
  
  /**
   * @param objectIID the concerned Object IID
   * @return the last revision DTO related to that object with minimal
   *         information
   * @throws SQLException
   * @throws RDBMSException
   */
  public ObjectRevisionDTO getLastObjectRevisionWithoutTimeline(long objectIID) throws SQLException, RDBMSException
  {
    PreparedStatement query = currentConnection.prepareStatement(Q_GET_LAST_REVISION_WITHOUTTIMELINE);
    query.setLong(1, objectIID);
    query.setLong(2, objectIID);
    IResultSetParser rs = driver.getResultSetParser(query.executeQuery());
    if (rs.next()) {
      try {
        ObjectRevisionDTO revisionDTO = new ObjectRevisionDTO();
        revisionDTO.mapFromObjectRevision(rs);
        return revisionDTO;
      }
      catch (CSFormatException ex) {
        throw new RDBMSException(0, "Format", ex);
      }
    }
    return new ObjectRevisionDTO(); // return empty object
  }
  
  /**
   * @param minTrackIID
   *          the starting position in the sequence of tracking data
   * @param objectIID
   *          the concerned object IID
   * @param userIID
   *          the concerned user IID
   * @return all the changes that happen after the min trackIID and until the
   *         last change provided by the user
   */
  List<IObjectTrackingDTO> getLastObjectTrackings(long minTrackIID, long objectIID, long userIID)
      throws RDBMSException, SQLException
  {
    List<IObjectTrackingDTO> lastChanges = new ArrayList<>();
    PreparedStatement query = currentConnection.prepareStatement(Q_GET_LAST_USER_TRACKINGS);
    query.setLong(1, objectIID);
    query.setLong(2, minTrackIID);
    query.setLong(3, userIID);
    IResultSetParser rs = driver.getResultSetParser(query.executeQuery());
    while (rs.next()) {
      ObjectTrackingDTO change = new ObjectTrackingDTO();
      try {
        change.set(rs);
      }
      catch (CSFormatException ex) {
        throw new RDBMSException(0, "FORMAT", ex);
      }
      lastChanges.add(change);
    }
    return lastChanges;
  }
  
  /**
   * 
   * @param baseEntityIID
   * @return
   * @throws SQLException
   * @throws RDBMSException 
   */
  IResultSetParser getBaseEntityObjectRevision(long baseEntityIID, Boolean isArchived)
      throws SQLException, RDBMSException
  {
    String query = Q_GET_OBJECT_REVISIONS + "order by revisionno desc ";
    
    PreparedStatement statement = currentConnection.prepareStatement(query);
    statement.setLong(1, baseEntityIID);
    statement.setBoolean(2, isArchived);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  IResultSetParser getLastObjectRevisions(long baseEntityIID, long nbOfRevision, long offset, Boolean isArchived)
      throws SQLException, RDBMSException
  {
    String query = Q_GET_OBJECT_REVISIONS + "order by revisionno desc ";
    String strQuery = query;
    if(nbOfRevision>=0) {
      strQuery = query + driver.getOffsetExpression(nbOfRevision, offset);
    }
    PreparedStatement statement = currentConnection.prepareStatement(strQuery);
    statement.setLong(1, baseEntityIID);
    statement.setBoolean(2, isArchived);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  void deleteObjectRevisions(long baseEntityIID, List<Integer> revisionNo)
      throws SQLException, RDBMSException
  {
    if (!revisionNo.isEmpty()) {
      String query1 = String.format(Q_DELETE_REVISIONS, Text.join(",", revisionNo));
      PreparedStatement statement = currentConnection.prepareStatement(query1.toString());
      statement.setLong(1, baseEntityIID);
      statement.execute();
    }
  }
  
  void moveObjectRevisionsToArchive(long baseEntityIID, List<Integer> revisionNo, Boolean isArchived)
      throws SQLException, RDBMSException
  {
    if (!revisionNo.isEmpty()) {
      String query1 = String.format(Q_UPDATE_REVISIONS, Text.join(",", revisionNo));
      PreparedStatement statement = currentConnection.prepareStatement(query1.toString());
      statement.setBoolean(1, isArchived);
      statement.setLong(2, baseEntityIID);
      statement.execute();
    }
  }
  
  /**
   * 
   * @param baseEntityIID
   * @param nbOfRevision
   * @return
   * @throws SQLException
   * @throws RDBMSException 
   */
  IResultSetParser getObjectRevisions(long baseEntityIID, Set<Integer> nbOfRevision)
      throws SQLException, RDBMSException
  {
    int position = 0;
    String query = Q_GET_OBJECT_REVISIONS;
    if (nbOfRevision != null && nbOfRevision.size() > 0) {
    StringBuffer inQuery = new StringBuffer(" and revisionno in (");
    for (int revision : nbOfRevision) {
      if (position == 0) {
        inQuery.append(revision);
      }
      else {
        inQuery.append(", " + revision);
      }
      position++;
    }
    inQuery.append(")");
    query = query + inQuery.toString();
  }
    PreparedStatement statement = currentConnection.prepareStatement(
        query + " order by revisionno desc");
    statement.setLong(1, baseEntityIID);
    statement.setBoolean(2, false);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  IResultSetParser getObjectTrackingsOfSameTransaction(long trackIID) throws SQLException, RDBMSException
  {
    String query = String.format(Q_GET_OBJECT_TRACKINGS_BY_TRANSACTION_ID);
    PreparedStatement statement = currentConnection.prepareStatement(query);
    statement.setLong(1, trackIID);
    return driver.getResultSetParser(statement.executeQuery());
  }
  
  /**
   * 
   * @param objectIID
   * @return 
   * @throws SQLException
   * @throws RDBMSException
   */
  Integer getNumberOfVersionsInTimeline(long objectIID) throws SQLException, RDBMSException
  {
    Integer count = 0;
    String query = String.format(Q_GET_NUMBER_OF_VERSIONS_IN_TIMELINE);
    PreparedStatement statement = currentConnection.prepareStatement(query);
    statement.setLong(1, objectIID);
    IResultSetParser resultSetParser = driver.getResultSetParser(statement.executeQuery());
    while (resultSetParser.next()) {
      count = resultSetParser.getInt("count");
    }
    return count;
  }
  
  /**
   * 
   * @param baseEntityIID
   * @param revisionNo
   * @throws SQLException
   * @throws RDBMSException
   */
  void restoreRevisions(long baseEntityIID, List<Integer> revisionNo)
      throws SQLException, RDBMSException
  {
    if (!revisionNo.isEmpty()) {
      String query1 = String.format(Q_UPDATE_REVISIONS, Text.join(",", revisionNo));
      PreparedStatement statement = currentConnection.prepareStatement(query1.toString());
      statement.setBoolean(1, false);
      statement.setLong(2, baseEntityIID);
      statement.execute();
    }
  }
  
  /**
   * Get object revisions for asset purge.
   * @param baseEntityIID
   * @param nbOfRevision
   * @return
   * @throws SQLException
   * @throws RDBMSException
   */
  IResultSetParser getObjectRevisionsAssetsToPurge(long baseEntityIID, Set<Integer> nbOfRevision) throws SQLException, RDBMSException
  {
    String query = "select * from pxp.objectrevisionfullcontent where objectIID = ?";
    if (nbOfRevision != null && nbOfRevision.size() > 0) {
      StringBuffer inQuery = new StringBuffer(" and revisionno in (");
      inQuery.append(Text.join(",", nbOfRevision));
      inQuery.append(")");
      query = query + inQuery.toString();
    }
    PreparedStatement statement = currentConnection.prepareStatement(query);
    statement.setLong(1, baseEntityIID);
    return driver.getResultSetParser(statement.executeQuery());
  }

  public IResultSetParser getAllTrackings(long baseEntityIID) throws RDBMSException, SQLException
  {
    String query = Q_GET_ALL_TRACKINGS;
    PreparedStatement statement = currentConnection.prepareStatement(
        query + " order by trackiid desc");
    statement.setLong(1, baseEntityIID);
    return driver.getResultSetParser(statement.executeQuery());
  }
}
