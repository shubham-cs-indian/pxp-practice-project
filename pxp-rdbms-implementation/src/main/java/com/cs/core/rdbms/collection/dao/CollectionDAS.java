package com.cs.core.rdbms.collection.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.dto.CollectionDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class CollectionDAS extends RDBMSDataAccessService {
  
  public CollectionDAS(RDBMSConnection connection)
  {
    super(connection);
  }
  

  void createCollection(CollectionDTO entity, long userIID) throws SQLException, RDBMSException
  {
    IResultSetParser result = driver.getFunction(currentConnection, RDBMSAbstractFunction.ResultType.IID, "pxp.fn_createCollection" )
        .setInput(ParameterType.INT, entity.getCollectionType().ordinal())
        .setInput(ParameterType.STRING, entity.getCollectionCode())
        .setInput(ParameterType.IID, entity.getParentIID() == -1 ? null : entity.getParentIID())
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.IID, userIID)
        .setInput(ParameterType.JSON,  entity.getSearchCriteria() == null || entity.getSearchCriteria().isEmpty() ? null : entity.getSearchCriteria().toString())
        .setInput(ParameterType.BOOLEAN, entity.getIsPublic())
        .setInput(ParameterType.STRING, entity.getCatalogCode())
        .setInput(ParameterType.STRING, entity.getOrganizationCode())
        .setInput(ParameterType.IID_ARRAY, entity.getLinkedBaseEntityIIDs())
        .execute();
    entity.setIID(result.getIID());
  }
  
  
  int updateCollection(long collectionIID, long userIID, CollectionDTO entity, List<Long> addedBaseEnityIIDS, List<Long> removeBaseEntityIIDS) throws SQLException, RDBMSException
  {
     IResultSetParser execute = currentConnection.getFunction(ResultType.INT, "pxp.fn_updateCollection" )
        .setInput(ParameterType.IID, collectionIID)
        .setInput(ParameterType.STRING, entity.getCollectionCode())
        .setInput(ParameterType.LONG, System.currentTimeMillis())
        .setInput(ParameterType.IID, userIID)
        .setInput(ParameterType.JSON,  entity.getSearchCriteria() == null || entity.getSearchCriteria().isEmpty() ? null : entity.getSearchCriteria().toString())
        .setInput(ParameterType.BOOLEAN, entity.getIsPublic())
        .setInput(ParameterType.IID_ARRAY, addedBaseEnityIIDS)
        .setInput(ParameterType.IID_ARRAY, removeBaseEntityIIDS)
        .execute();
        return execute.getInt();
  }
  
  // TODO : for Migration only
  public Long createCollectionForMigration(CollectionDTO entity) throws RDBMSException, SQLException
  {
    long userIid = 0L;
    long createdOn = 0L;
    ISimpleTrackingDTO created = entity.getCreatedTrack();
    createdOn = created.getWhen();
    long[] userId = { 0 };
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          StringBuilder query = new StringBuilder();
          query.append(String.format(
              "SELECT useriid from staging.helper_userconfig where userid = '%s'",
              created.getWho()));
          PreparedStatement stmt = currentConn.prepareStatement(query.toString());
          stmt.execute();
          IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
          if (resultSet.next()) {
            userId[0] = resultSet.getLong("useriid");
          }
        });
    if (userId[0] != 0) {
      userIid = userId[0];
    }
    IResultSetParser result = driver
        .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.IID,
            "pxp.fn_createCollection")
        .setInput(ParameterType.INT, entity.getCollectionType()
            .ordinal())
        .setInput(ParameterType.STRING, entity.getCollectionCode())
        .setInput(ParameterType.IID, entity.getParentIID() == -1 ? null : entity.getParentIID())
        .setInput(ParameterType.LONG, createdOn)
        .setInput(ParameterType.IID, userIid)
        .setInput(ParameterType.JSON,
            entity.getSearchCriteria() == null || entity.getSearchCriteria()
                .isEmpty() ? null
                    : entity.getSearchCriteria()
                        .toString())
        .setInput(ParameterType.BOOLEAN, entity.getIsPublic())
        .setInput(ParameterType.STRING, entity.getCatalogCode())
        .setInput(ParameterType.STRING, entity.getOrganizationCode())
        .setInput(ParameterType.IID_ARRAY, entity.getLinkedBaseEntityIIDs())
        .execute();
    entity.setIID(result.getIID());
    
    return result.getIID();
  }
}
