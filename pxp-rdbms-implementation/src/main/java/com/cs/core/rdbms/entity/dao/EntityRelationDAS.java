package com.cs.core.rdbms.entity.dao;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * This special interface regroup methods for entity relation management
 *
 * @author vallee
 */
public class EntityRelationDAS extends RDBMSDataAccessService {
  
  private static final String Q_PARENT_BY_ID   = "select parentIID as parentBaseEntityIID, "
      + "        embeddedType as relationType " + " from pxp.baseentitytrackingfullcontent "
      + " where baseentityiid= ? ;";
  private static final String Q_CHILDREN_BY_ID = "select baseentityiid as childBaseEntityIID, baseEntityID as childBaseEntityID, embeddedType as relationType "
      + " from pxp.baseentitytrackingfullcontent " + "where parentiid = ? and embeddedType = ? ;";
  
  public EntityRelationDAS(RDBMSConnection connection)
  {
    super(connection);
  }
  
  /**
   * @param baseEntityIID
   *          current concerned base entity IID
   * @return the result containing parent information or empty
   * @throws RDBMSException
   * @throws SQLException
   */
  public ResultSet queryParent(long baseEntityIID) throws RDBMSException, SQLException
  {
    PreparedStatement statement = currentConnection.prepareStatement(Q_PARENT_BY_ID);
    statement.setLong(1, baseEntityIID);
    return statement.executeQuery();
  }
  
  /**
   * @param embeddedType
   * @param baseEntityIID
   *          current concerned base entity IID
   * @return the results of query on children joined with their object ID
   * @throws RDBMSException
   * @throws SQLException
   */
  public ResultSet queryChildren(int embeddedType, long baseEntityIID)
      throws RDBMSException, SQLException
  {
    PreparedStatement statement = currentConnection.prepareStatement(Q_CHILDREN_BY_ID);
    statement.setLong(1, baseEntityIID);
    statement.setInt(2, embeddedType);
    return statement.executeQuery();
  }
  
  /**
   * @param relationshipIID
   *          the relationship IID used to determine the type of parent-children
   *          relations
   * @param parentIID
   *          current concerned parent base entity IID
   * @param newChildrenIIDs
   *          object IIDs to be added
   * @throws SQLException
   * @throws RDBMSException
   */
  void addChildren(int embeddedType, long parentIID, Set<Long> newChildrenIIDs)
      throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_addChildren")
        .setInput(ParameterType.INT, embeddedType)
        .setInput(ParameterType.LONG, parentIID)
        .setInput(ParameterType.IID_ARRAY, newChildrenIIDs)
        .execute();
  }
  
  /**
   * Remove children (because of deletion tracking information are required
   * here)
   *
   * @param parentIID
   *          current concerned parent base entity IID
   * @param oldChildrenIIDs
   *          object IIDs to be removed
   * @param userIID
   *          the user IID who commits the action
   * @throws SQLException
   * @throws RDBMSException
   */
  void removeChildren(long parentIID, Set<Long> oldChildrenIIDs) throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_removeChildren")
        .setInput(ParameterType.LONG, parentIID)
        .setInput(ParameterType.IID_ARRAY, oldChildrenIIDs)
        .execute();
  }
}
