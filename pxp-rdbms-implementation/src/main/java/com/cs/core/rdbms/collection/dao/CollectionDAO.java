package com.cs.core.rdbms.collection.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.CollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class CollectionDAO implements ICollectionDAO{
  
  private final long    userIID;
  private final String  catalogCode;
  private CollectionDTO entity;
  
  public CollectionDAO(long userIID, String catalogCode)
  {
    this.userIID = userIID;
    this.catalogCode = catalogCode;
  }
  
  @Override
  public ICollectionDTO createCollection(ICollectionDTO collectionDTO) throws RDBMSException
  {
    long startClock = System.currentTimeMillis();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      CollectionDAS entityDAS = new CollectionDAS(currentConn);
      entityDAS.createCollection((CollectionDTO) collectionDTO, userIID);
    });
    RDBMSLogger.instance().debug("NA|INTERNAL-RDBMS|BaseEntityDAS|createCollectionRecord (%d records)| %d ms", 1,
        System.currentTimeMillis() - startClock);
    getCollection(collectionDTO.getCollectionIID(), collectionDTO.getCollectionType());
    return collectionDTO;
  }
  
  private static final String GET_ALL_COLLECTIONS = "select * From pxp.collection c JOIN pxp.userconfig u on c.lastmodifieduseriid = u.useriid where catalogcode = '%s' AND collectiontype = %d ";
  @Override
  public List<ICollectionDTO> getAllCollections(long parentIID, CollectionType collectionType) throws RDBMSException
  {
    List<ICollectionDTO> collectionDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      StringBuffer conditionQuery = new StringBuffer();
      conditionQuery.append(GET_ALL_COLLECTIONS);
      if(parentIID == -1) {
        conditionQuery.append("AND parentiid is null");
      }
      else {
        String parentQuery = String.format(" AND parentiid = %d", parentIID);
        conditionQuery.append(parentQuery);
      }
      String subQuery = String.format(conditionQuery.toString(), catalogCode, collectionType.ordinal());
      StringBuffer finalQuery = new StringBuffer();
      finalQuery.append(subQuery + "  AND ispublic = true UNION ALL " + subQuery + " AND ispublic = false AND createuseriid = ?");
      PreparedStatement stmt = connection.prepareStatement(finalQuery.toString());
      stmt.setLong(1, userIID);
      stmt.execute();
      IResultSetParser result = connection.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        collectionDTOs.add(new CollectionDTO(result));
      }
    });
      
    return collectionDTOs;
  }
  
  public static final String QUERY = "Select * from pxp.collection c";

  @Override
  public ICollectionDTO getCollection(long collectionIID, CollectionType collectionType) throws RDBMSException
  {
    
    Set<Long> baseEntityIIDs = new HashSet<>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
     StringBuffer query = new StringBuffer(QUERY);
      if(collectionType !=null && collectionType.equals(CollectionType.staticCollection)) {
        query.append(" LEFT JOIN  pxp.collectionbaseentitylink cbl on c.collectioniid = cbl.collectioniid");
      }
      query.append(" JOIN  pxp.userconfig u on c.lastmodifieduseriid = u.useriid where c.collectioniid = ?");
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.setLong(1, collectionIID);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while(result.next()) {
        entity = new CollectionDTO(result);
        if(collectionType !=null && collectionType.equals(CollectionType.staticCollection)) {
          
          long baseEntityIID = result.getLong("baseentityiid");
          if(baseEntityIID != 0) {
            baseEntityIIDs.add(baseEntityIID);;
          }
          entity.setLinkedBaseEntityIIDs(baseEntityIIDs);
        }
      }
    });
    
    return entity;
  }
  
  private static final String DELETE_COLLECTIONS = "Delete from pxp.collection where collectioniid = %d";
  @Override
  public void deleteCollectionRecords(Long collectionIIDs) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
 
      String format = String.format(DELETE_COLLECTIONS, collectionIIDs);
      PreparedStatement stmt = connection.prepareStatement(format);
      stmt.execute();
    });
  }
  
  public static final String UPDATE_DYNAMIC_COLLECTION = "update pxp.collection set ispublic = ? where collectioniid = ?";
  
  @Override
  public void changeViewPermission(ICollectionDTO collectionDTO, long collectionIID) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
     
      PreparedStatement stmt = connection.prepareStatement(UPDATE_DYNAMIC_COLLECTION);
      stmt.setBoolean(1, collectionDTO.getIsPublic());
      stmt.setLong(2, collectionIID);
      stmt.execute();
    });
  }
  
  
  @Override
  public Boolean updateCollectionRecords(long collectionIID, ICollectionDTO collectionDTO, List<Long> addedBaseEnityIIDS,
      List<Long> removeBaseEntityIIDS) throws RDBMSException
  {
    AtomicInteger result = new AtomicInteger();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      CollectionDAS entityDAS = new CollectionDAS( connection);
       result.set(entityDAS.updateCollection(collectionIID, userIID, (CollectionDTO) collectionDTO, addedBaseEnityIIDS, removeBaseEntityIIDS));
    });
    if (result.get() > 0) {
      return true;
    }
    else {
      return false;
    }
    
  }
  
  private final String GET_COLLECTIONID = "select collectioniid from pxp.collectionbaseentitylink where baseentityiid = ?";
  
  @Override
  public Set<String> getAllCollectionIds(Long baseEntityId) throws RDBMSException{
    Set<String> allCollectionIds = new HashSet<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(GET_COLLECTIONID);
      stmt.setLong(1, baseEntityId);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());

      while (result.next()) {
        String classifierCode = result.getString("collectioniid");
        allCollectionIds.add(classifierCode);
      }
    });
    return allCollectionIds;
  }
}
