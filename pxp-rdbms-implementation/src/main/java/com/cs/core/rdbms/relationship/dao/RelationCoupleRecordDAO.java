package com.cs.core.rdbms.relationship.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.relationship.dto.RelationCoupleRecordDTO;
import com.cs.core.rdbms.relationship.idao.IRelationCoupleRecordDAO;
import com.cs.core.rdbms.relationship.idto.IRelationCoupleRecordDTO;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class RelationCoupleRecordDAO implements IRelationCoupleRecordDAO{
  
  private static IRelationCoupleRecordDAO relationCoupleRecordDaoInstance;
  
  public static final String  INSERT_RELATION_COUPLE_RECORD   = "INSERT INTO pxp.relationcouplerecord (" + RelationCoupleRecordDTO.SOURCE_ENTITY_ID + ", " + RelationCoupleRecordDTO.TARGET_ENTITY_ID + ", "
      + RelationCoupleRecordDTO.NATURE_RELATIONSHIP_ID + ", " + RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_ID + ", " + RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_SIDE_ID + ", " + RelationCoupleRecordDTO.COUPLING_TYPE + ", "
      + RelationCoupleRecordDTO.IS_RESOLVED + ") VALUES (?,?,?,?,?,?,?) ";
  
  public static final String  FETCH_RELATION_COUPLE_RECORDS = " SELECT * FROM pxp.relationcouplerecord where " ;
  
  private static final String REMOVE_RELATION_COUPLE_RECORD = "Delete from pxp.relationcouplerecord where ";
  
  private static final String UPDATE_RELATION_COUPLE_RECORD = "UPDATE pxp.relationcouplerecord SET "
      + RelationCoupleRecordDTO.IS_RESOLVED + " = ? , " + RelationCoupleRecordDTO.COUPLING_TYPE + " = ? where "
      + RelationCoupleRecordDTO.TARGET_ENTITY_ID + " = ?  and "+ RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_ID + " = ?";
  
  private static final String  UPDATE_RELATION_CONFLICT_STATUS = "UPDATE pxp.relationcouplerecord SET "
      + RelationCoupleRecordDTO.IS_RESOLVED + " = ?   where "
      + RelationCoupleRecordDTO.TARGET_ENTITY_ID + " = ?  and "+ RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_ID + " = ? and "
          + RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_SIDE_ID+ " = ?";
  
  
  private RelationCoupleRecordDAO() {
  }
  
  public static IRelationCoupleRecordDAO getInstance()
  {
    if (relationCoupleRecordDaoInstance == null) {
      synchronized (RelationCoupleRecordDAO.class) {
        if (relationCoupleRecordDaoInstance == null) {
          relationCoupleRecordDaoInstance = new RelationCoupleRecordDAO();
        }
      }
    }
    return relationCoupleRecordDaoInstance;
  }
  
  @Override
  public void createRelationCoupleRecord(IRelationCoupleRecordDTO... relationCoupleRecordDtos) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      try {
        PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_RELATION_COUPLE_RECORD);
        for (IRelationCoupleRecordDTO relationCoupleRecordInfo : relationCoupleRecordDtos) {
          preparedStatement.setLong(1, relationCoupleRecordInfo.getSourceEntityId());
          preparedStatement.setLong(2, relationCoupleRecordInfo.getTargetEntityId());
          preparedStatement.setLong(3, relationCoupleRecordInfo.getNatureRelationshipId());
          preparedStatement.setLong(4, relationCoupleRecordInfo.getPropagableeRelationshipId());
          preparedStatement.setString(5, relationCoupleRecordInfo.getPropagableeRelationshipSideId());
          preparedStatement.setShort(6, (short) relationCoupleRecordInfo.getCouplingType().ordinal());
          preparedStatement.setBoolean(7, relationCoupleRecordInfo.getIsResolved());
          preparedStatement.executeUpdate();
        }
      }
      catch (Exception e) {
        currentConn.commit();
      }
    });
  }

  @Override
  public List<IRelationCoupleRecordDTO> fetchRelationCoupleRecord(StringBuilder filterQuery) throws RDBMSException
  {
    List<IRelationCoupleRecordDTO> relationCoupleRecordDto = new ArrayList<>();
    StringBuilder finalFilterQuery = new StringBuilder();
    finalFilterQuery.append(FETCH_RELATION_COUPLE_RECORDS);
    if (filterQuery.length() > 0) {
      finalFilterQuery.append(filterQuery);
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(finalFilterQuery);
      stmt.execute();
      
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        relationCoupleRecordDto.add(new RelationCoupleRecordDTO(resultSet));
      }
    });
    return relationCoupleRecordDto;
  }
  
  @Override
  public void deleteRelationCoupleRecord(StringBuilder filterQuery) throws RDBMSException
  {
    StringBuilder finalFilterQuery = new StringBuilder();
    finalFilterQuery.append(REMOVE_RELATION_COUPLE_RECORD);
    if (filterQuery.length() > 0) {
      finalFilterQuery.append(filterQuery);
    }
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        PreparedStatement stmt = currentConn.prepareStatement(finalFilterQuery);
        stmt.execute();
      });
  }
  
  @Override
  public void updateRelationCoupledRecord(IRelationCoupleRecordDTO... relationCoupleRecordDtos) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      String query = String.format(UPDATE_RELATION_COUPLE_RECORD);
      for (IRelationCoupleRecordDTO relationCoupleRecordInfo : relationCoupleRecordDtos) {
      PreparedStatement stmt = currentConn
          .prepareStatement(query);
      stmt.setBoolean(1, relationCoupleRecordInfo.getIsResolved());
      stmt.setShort(2, (short) relationCoupleRecordInfo.getCouplingType().ordinal());
      stmt.setLong(3, relationCoupleRecordInfo.getTargetEntityId());
      stmt.setLong(4, relationCoupleRecordInfo.getPropagableeRelationshipId());
      stmt.execute();
      }
    });
  }

  @Override
  public void updateConflictResolvedStatus(IRelationCoupleRecordDTO... relationCoupleRecordDtos) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      String query = String.format(UPDATE_RELATION_CONFLICT_STATUS);
      for (IRelationCoupleRecordDTO relationCoupleRecordInfo : relationCoupleRecordDtos) {
      PreparedStatement stmt = currentConn
          .prepareStatement(query);
      stmt.setBoolean(1, relationCoupleRecordInfo.getIsResolved());
      stmt.setLong(2, relationCoupleRecordInfo.getTargetEntityId());
      stmt.setLong(3, relationCoupleRecordInfo.getPropagableeRelationshipId());
      stmt.setString(4, relationCoupleRecordInfo.getPropagableeRelationshipSideId());
      stmt.execute();
      }
    });
  }
  
  
  public StringBuilder getFilterQuery(IRelationCoupleRecordDTO relationCoupleRecordDTO)
  {
    StringBuilder filterQuery = new StringBuilder();
    filterQuery = conditionFilterQuery(filterQuery, relationCoupleRecordDTO.getTargetEntityId(), RelationCoupleRecordDTO.TARGET_ENTITY_ID);
    filterQuery = conditionFilterQuery(filterQuery, relationCoupleRecordDTO.getSourceEntityId(), RelationCoupleRecordDTO.SOURCE_ENTITY_ID);
    filterQuery = conditionFilterQuery(filterQuery, relationCoupleRecordDTO.getPropagableeRelationshipId(), RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_ID);
    filterQuery = conditionFilterQuery(filterQuery, relationCoupleRecordDTO.getPropagableeRelationshipSideId(), RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_SIDE_ID);
    filterQuery = conditionFilterQuery(filterQuery, relationCoupleRecordDTO.getNatureRelationshipId(), RelationCoupleRecordDTO.NATURE_RELATIONSHIP_ID);
    filterQuery = conditionFilterQuery(filterQuery, relationCoupleRecordDTO.getCouplingType(), RelationCoupleRecordDTO.COUPLING_TYPE);
    return filterQuery;
  }

  
  private StringBuilder conditionFilterQuery(StringBuilder filterQuery, Object filterInput, String filterColumn)
  {
    if (filterInput instanceof Long && (Long) filterInput != 0L) {
        prepareQuery(filterQuery, filterInput, filterColumn);
      }
    else if (filterInput instanceof String &&  !((String) filterInput).isEmpty()) {
        prepareQuery(filterQuery, filterInput, filterColumn);
    }
    else if (filterInput instanceof IRelationCoupleRecordDTO.CouplingType && !filterInput.equals(IRelationCoupleRecordDTO.CouplingType.undefined)) {
      prepareQuery(filterQuery, filterInput, filterColumn);
    }
    return filterQuery;
  }

  private void prepareQuery(StringBuilder filterQuery, Object filterInput, String filterColumn)
  {
    if (filterQuery.length() > 0) {
      filterQuery.append(" and ");
    }
    filterQuery.append(filterColumn + " = ");
    if (filterColumn == RelationCoupleRecordDTO.NATURE_RELATIONSHIP_ID) {
      filterQuery.append(filterInput);
    }
    else if (filterColumn == RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_SIDE_ID) {
      filterQuery.append("'" + filterInput + "'");
    }
    else if (filterColumn == RelationCoupleRecordDTO.TARGET_ENTITY_ID) {
      filterQuery.append(  filterInput );
    }
    else if (filterColumn == RelationCoupleRecordDTO.PROPAGABLE_RELATIONSHIP_ID) {
      filterQuery.append(  filterInput );
    }
    else if (filterColumn == RelationCoupleRecordDTO.SOURCE_ENTITY_ID) {
      filterQuery.append(  filterInput );
    }
    else if (filterColumn == RelationCoupleRecordDTO.COUPLING_TYPE) {
      filterQuery.append( IRelationCoupleRecordDTO.CouplingType.valueOf(filterInput.toString()).ordinal());
    }
  }
} 

