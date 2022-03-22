package com.cs.core.rdbms.uniquenessViolation.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.data.Text;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.idto.IUniquenessViolationDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class UniquenessViolationDAO implements IUniquenessViolationDAO{
  
  public static final String VALUE_QUERY = "select value from pxp.allvaluerecord where entityiid=? and propertyiid = ?";
  
  public static final String PRODUCT_IDENTIFIER = " select be.baseentityiid "
      + "from pxp.allvaluerecord avr join pxp.baseentity be on be.baseentityiid = avr.entityiid and be.ismerged != true left join pxp.baseentityclassifierlink becl on "
      + "becl.baseentityiid = be.baseentityiid " 
      + "where avr.propertyiid = ? and (becl.otherclassifieriid = ? or be.classifieriid = ?) " 
      + "and be.catalogcode = ? and avr.value = ? group by be.baseentityiid";
  
  @Override
  public List<Long> evaluateProductIdentifier(long propertyIID, long baseEntityIID, long classifierIID, String catalogCode)
      throws RDBMSException
  {
    List<Long> violatedEntities = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(VALUE_QUERY);
      
      stmt.setLong(1, baseEntityIID);
      stmt.setLong(2, propertyIID);
      stmt.execute();
     
      IResultSetParser resultSetParser = currentConn.getResultSetParser(stmt.getResultSet());
      
      String value = null;
      if(resultSetParser.next()) {
        value = resultSetParser.getString("value");
      }   
      
      if(value != null) {
        
        stmt = currentConn.prepareStatement(PRODUCT_IDENTIFIER);
        stmt.setLong(1, propertyIID);
        stmt.setLong(2, classifierIID);
        stmt.setLong(3, classifierIID);
        stmt.setString(4, catalogCode);
        stmt.setString(5, value);
        stmt.execute();
        resultSetParser = currentConn.getResultSetParser(stmt.getResultSet());
        
        while (resultSetParser.next()) {
          violatedEntities.add(resultSetParser.getLong("baseentityiid"));
        }
      }

    });
    return violatedEntities;
    
  }
  
  @Override
  public List<Long> evaluateProductIdentifierForMigration(long propertyIID,long classifierIID, String catalogCode, String value)
      throws RDBMSException
  {
    List<Long> violatedEntities = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {  
      
      if(value != null) {
        
        PreparedStatement stmt = currentConn.prepareStatement(PRODUCT_IDENTIFIER);
        stmt.setLong(1, propertyIID);
        stmt.setLong(2, classifierIID);
        stmt.setLong(3, classifierIID);
        stmt.setString(4, catalogCode);
        stmt.setString(5, value);
        stmt.execute();
        IResultSetParser resultSetParser = currentConn.getResultSetParser(stmt.getResultSet());
        
        while (resultSetParser.next()) {
          violatedEntities.add(resultSetParser.getLong("baseentityiid"));
        }
      }

    });
    return violatedEntities;
    
  }

  public static final String INSERT_VIOLATION = "INSERT INTO pxp.uniquenessviolation(sourceiid, targetiid, "
      + "propertyiid, classifieriid) VALUES ";
  
  @Override
  public void insertViolatedEntity(List<IUniquenessViolationDTO> uniquenessViolationDTOs) throws RDBMSException
  {
    StringBuilder query = new StringBuilder();
    
    if(!uniquenessViolationDTOs.isEmpty()) {
      
      query.append(INSERT_VIOLATION);
      for(IUniquenessViolationDTO uniquenessViolationDTO : uniquenessViolationDTOs) {
        query.append("(");
        query.append(uniquenessViolationDTO.getSourceIID() + ",");
        query.append(uniquenessViolationDTO.getTargetIID() + ",");
        query.append(uniquenessViolationDTO.getPropertyIID() + ",");
        query.append(uniquenessViolationDTO.getClassifierIID());
        
        query.append(") ,");
      }
      
      query.deleteCharAt(query.lastIndexOf(","));
      
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        
        PreparedStatement stmt = currentConn.prepareStatement(query.toString());
        
        stmt.execute();
        
      });
      
    }
  }

  
  public static final String DELETE_VIOLATION = "delete from pxp.uniquenessviolation uv where (uv.sourceiid = ? "
      + "OR uv.targetiid = ?) and uv.classifieriid = ? and uv.propertyiid IN ( ";
  
  @Override
  public void deleteBeforeEvaluateIdentifier(long baseEntityIID, List<Long> propertyIIDs, long classifierIID) throws RDBMSException
  {
    
      StringBuilder query = new StringBuilder();
      
      for(Long propertyIID : propertyIIDs) {
        query.append(propertyIID + ",");
      }
      query.deleteCharAt(query.lastIndexOf(","));
      query.append(")");
      StringBuilder finalQuery = new StringBuilder();
      finalQuery.append(DELETE_VIOLATION);
      finalQuery.append(query);
      
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery.toString());
      
      stmt.setLong(1, baseEntityIID);
      stmt.setLong(2, baseEntityIID);
      stmt.setLong(3, classifierIID);
      stmt.execute();
      
    });
  }

  
  public static final String IS_UNIQUE_RECORD = "select uv.targetiid from pxp.uniquenessviolation uv "
      + "where uv.sourceiid = ? and uv.propertyiid = ? and uv.classifieriid IN ";
  
  @Override
  public List<Long> isUniqueRecord(IUniquenessViolationDTO uniquenessViolationDTO, Set<IClassifierDTO> classifierIIDs) throws RDBMSException
  { 
    
    List<Long> targetIIDs  = new ArrayList<>();
    StringBuilder query = new StringBuilder();
    
    query.append("(");
    for(IClassifierDTO classifierDTO: classifierIIDs) {
      query.append(classifierDTO.getClassifierIID() + ",");
    }
    
    query.append(uniquenessViolationDTO.getClassifierIID() + ")");
    
    StringBuilder finalQuery = new StringBuilder();
    finalQuery.append(IS_UNIQUE_RECORD);
    finalQuery.append(query.toString());
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery.toString());
      
      stmt.setLong(1, uniquenessViolationDTO.getSourceIID());
      stmt.setLong(2, uniquenessViolationDTO.getPropertyIID());
      stmt.execute();
      
      ResultSet resultSet = stmt.getResultSet();
      
      while(resultSet.next()) {
        targetIIDs.add(resultSet.getLong(IUniquenessViolationDTO.TARGET_IID));
      }
      
    });
    
    return targetIIDs;
  }

  public static final String IS_UNIQUE_FOR_TILE_VIEW = "select uv.sourceiid from pxp.uniquenessviolation uv"
      + " where sourceiid = ? group by uv.classifieriid, uv.sourceiid, uv.propertyiid";
  
  @Override
  public Integer isUniqueRecordForTileView(long sourceIID) throws RDBMSException
  {
    
    List<Long> sourceIIDS  = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement stmt = currentConn.prepareStatement(IS_UNIQUE_FOR_TILE_VIEW);
      
      stmt.setLong(1, sourceIID);
      stmt.execute();
      
      ResultSet resultSet = stmt.getResultSet();
      
      while (resultSet.next()) {
        sourceIIDS.add(resultSet.getLong(IUniquenessViolationDTO.SOURCE_IID));
      }
      
    });
    
    return sourceIIDS.size();
  }
  
  public static final String UNIQUE_VIOLATION_FOR_TILE_VIEW = "select count(*) as violationcount, sourceiid from (select uv.sourceiid from pxp.uniquenessviolation uv" + 
      "  where sourceiid in (%s) group by uv.classifieriid, uv.sourceiid, uv.propertyiid) as foo group by sourceiid";
  
  @Override
  public Map<Long,Integer> getUniquenessViolationCount(Set<Long> baseEntityIIds) throws RDBMSException
  {
    Map<Long,Integer> countMap = new HashMap<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      String entityIIds = Text.join(",", baseEntityIIds);
      String finalQuery = String.format(UNIQUE_VIOLATION_FOR_TILE_VIEW, entityIIds);
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      
      stmt.execute();
      
      ResultSet resultSet = stmt.getResultSet();
      
      while (resultSet.next()) {
        countMap.put(resultSet.getLong(IUniquenessViolationDTO.SOURCE_IID),resultSet.getInt("violationcount"));
      }
      
    });
    return countMap;
    
  }
}
