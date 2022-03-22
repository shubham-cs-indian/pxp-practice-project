package com.cs.core.rdbms.auditlog.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.cs.core.rdbms.auditlog.dto.AuditLogExportDTO;
import com.cs.core.rdbms.auditlog.dto.AuditLogExportDTO.AuditLogExportDTOBuilder;
import com.cs.core.rdbms.auditlog.idao.IAuditLogExportDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTO.Status;
import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTOBuilder;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class AuditLogExportDAO implements IAuditLogExportDAO {
  
  private static IAuditLogExportDAO auditLogExportDAO;
  
  public static final String        INSERT_AUDIT_LOG_EXPORT           = "INSERT INTO auditlogexporttracker ("
      + "\"" + IAuditLogExportDTO.ASSET_ID + "\"," + "\"" + IAuditLogExportDTO.FILE_NAME + "\","
      + "\"" + IAuditLogExportDTO.USERNAME + "\"," + "\"" + IAuditLogExportDTO.START_TIME + "\","
      + "\"" + IAuditLogExportDTO.STATUS + "\" ) " + " VALUES(?,?,?,?,?)";
  
  public static final String        UPDATE_AUDIT_LOG_EXPORT           = "UPDATE auditlogexporttracker SET "
      + "\"" + IAuditLogExportDTO.STATUS + "\" = ? ," + "\"" + IAuditLogExportDTO.END_TIME
      + "\" = ? " + " WHERE \"" + IAuditLogExportDTO.ASSET_ID + "\" = ? ";
  
  public static final String        DELETE_AUDIT_LOG_EXPORT_ENTRY     = "DELETE FROM auditlogexporttracker "
      + " WHERE \"" + IAuditLogExportDTO.ASSET_ID + "\" =  ";
  
  public static final String        GET_ALL_AUDIT_LOG_EXPORT          = " SELECT * FROM auditlogexporttracker WHERE "
      + IAuditLogExportDTO.USERNAME + " = '%s' ORDER BY \"" + IAuditLogExportDTO.EXPORT_IID
      + "\" %s  LIMIT  %d  OFFSET %d ";
  
  public static final String        GET_ALL_AUDIT_LOG_EXPORT_COUNT    = "SELECT COUNT( \""
      + IAuditLogExportDTO.EXPORT_IID + "\") FROM auditlogexporttracker";
  
  public static final String        GET_FAILED_AUDIT_LOG_EXPORT_ENTRY = "SELECT \""
      + IAuditLogExportDTO.ASSET_ID + "\"" + " FROM auditlogexporttracker WHERE \""
      + IAuditLogExportDTO.STATUS + "\" =  " + Status.FAILED.getCode() + " AND \""
      + IAuditLogExportDTO.ASSET_ID + "\" IN  ";
  
  public static final String        DELETE_MULTIPLE_AUDIT_LOG_EXPORT_ENTRY = "DELETE FROM auditlogexporttracker WHERE \""
      + IAuditLogExportDTO.ASSET_ID + "\" IN  ";
  
  public static final String        GET_AUDIT_LOG_EXPORT_ENTRY_FOR_HOUSE_KEEPING = "SELECT \""
      + IAuditLogExportDTO.ASSET_ID + "\"" + " FROM auditlogexporttracker WHERE \""
      + IAuditLogExportDTO.END_TIME + "\" <= ? ";
  
  private AuditLogExportDAO() {
    
  }
  
  public static IAuditLogExportDAO getInstance()
  {
    if (auditLogExportDAO == null) {
      synchronized (AuditLogExportDAO.class) {
        if (auditLogExportDAO == null) {
          auditLogExportDAO = new AuditLogExportDAO();
        }
      }
    }
    return auditLogExportDAO;
  }

  @Override
  public void createAuditLogExportTracker(IAuditLogExportDTO auditLogExportDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement auditLogExportStatement = currentConn.prepareStatement(INSERT_AUDIT_LOG_EXPORT);
        auditLogExportStatement.setString(1, auditLogExportDTO.getAssetId());
        auditLogExportStatement.setString(2, auditLogExportDTO.getFileName());
        auditLogExportStatement.setString(3, auditLogExportDTO.getUserName());
        auditLogExportStatement.setLong(4, auditLogExportDTO.getStartTime());
        auditLogExportStatement.setShort(5, (short) auditLogExportDTO.getStatus());

        auditLogExportStatement.executeUpdate();
    });
  }

  @Override
  public void updateAuditLogExportTracker(IAuditLogExportDTO auditLogExportDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement auditLogExportStatements = currentConn.prepareStatement(UPDATE_AUDIT_LOG_EXPORT);
        auditLogExportStatements.setShort(1, (short) auditLogExportDTO.getStatus());
        auditLogExportStatements.setLong(2, auditLogExportDTO.getEndTime());
        auditLogExportStatements.setString(3, auditLogExportDTO.getAssetId());
        
        auditLogExportStatements.executeUpdate();
    });
}

  @Override
  public IAuditLogExportDTOBuilder newAuditLogExportDTOBuilder()
  {
    return new AuditLogExportDTOBuilder();
  }

  @Override
  public void deleteAuditLogExportTracker(String assetId) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      currentConn.prepareStatement(DELETE_AUDIT_LOG_EXPORT_ENTRY + assetId).executeUpdate();
    });
  }

  @Override
  public List<IAuditLogExportDTO> getAllAuditLogExportTrackers(String userName, String sortOrder,
      long size, long from) throws RDBMSException
  {
    List<IAuditLogExportDTO> auditLogExportDTOs = new ArrayList<>();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(GET_ALL_AUDIT_LOG_EXPORT, userName, sortOrder, size, from);
      PreparedStatement queryStatement = currentConn.prepareStatement(query);
      IResultSetParser result = currentConn.getResultSetParser(queryStatement.executeQuery());
      while(result.next()) {
        auditLogExportDTOs.add(new AuditLogExportDTO(result));
      }
    });
    return auditLogExportDTOs;
  }
  
  @Override
  public long getCount(String userName) throws RDBMSException {
    
    long count[] = { 0 };
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement queryStatement = currentConn.prepareStatement(GET_ALL_AUDIT_LOG_EXPORT_COUNT);
      IResultSetParser result = currentConn.getResultSetParser(queryStatement.executeQuery());
      if(result.next()) {
        count[0] = result.getLong("count");
      }
      });
    return count[0];
  }
  
  @Override
  public List<String> getFailedAuditLogExportAssetIds(String assetIds) throws RDBMSException{
    
    List<String> failedAssetIds = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement queryStatement = currentConn.prepareStatement(GET_FAILED_AUDIT_LOG_EXPORT_ENTRY + assetIds);
      IResultSetParser result = currentConn.getResultSetParser(queryStatement.executeQuery());
      while(result.next()) {
        failedAssetIds.add(String.valueOf(result.getString(IAuditLogExportDTO.ASSET_ID)));
      }
    });
    return failedAssetIds;
  }
  
  @Override
  public void deleteMultipleAuditLogExportTrackers(String assetIds) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      currentConn.prepareStatement(DELETE_MULTIPLE_AUDIT_LOG_EXPORT_ENTRY + assetIds).executeUpdate();
    });
  }
  
  @Override
  public List<String> getAuditLogExportTrackerAssetIdsForHouseKeeping(long endTime) throws RDBMSException {
    
    List<String> assetIds = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
     PreparedStatement queryStatement = currentConn.prepareStatement(GET_AUDIT_LOG_EXPORT_ENTRY_FOR_HOUSE_KEEPING);
     queryStatement.setLong(1, endTime);
     IResultSetParser result = currentConn.getResultSetParser(queryStatement.executeQuery());
     while(result.next()) {
       assetIds.add(result.getString(IAuditLogExportDTO.ASSET_ID));
     }
    });
    return assetIds;
  }
}
