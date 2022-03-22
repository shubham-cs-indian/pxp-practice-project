package com.cs.core.rdbms.auditlog.dto;

import java.sql.SQLException;

import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTOBuilder;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class AuditLogExportDTO extends RDBMSRootDTO implements IAuditLogExportDTO {
  
  private String fileName = "";
  private String userName = "";
  private String assetId = "";
  private long startTime = 0l;
  private long endTime = 0l;
  private int status = Status.IN_PROGRESS.getCode();

  
  private AuditLogExportDTO() {
    
  }
  
  public AuditLogExportDTO(IResultSetParser result) throws SQLException {
    
    fileName = result.getString("filename");
    userName = result.getString("username");
    assetId = result.getString("assetid");
    startTime = result.getLong("starttime");
    endTime = result.getLong("endtime");
    status = result.getInt("status");
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }

  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    
  }

  @Override
  public String getFileName()
  {
    return fileName;
  }

  @Override
  public String getUserName()
  {
    return userName;
  }

  @Override
  public Long getStartTime()
  {
    return startTime;
  }

  @Override
  public Long getEndTime()
  {
    return endTime;
  }

  @Override
  public int getStatus()
  {
    return status;
  }

  @Override
  public String getAssetId()
  {
    return assetId;
  }
  
  public static class AuditLogExportDTOBuilder implements IAuditLogExportDTOBuilder{
    
    public final AuditLogExportDTO auditLogExportDTO;
    
    public AuditLogExportDTOBuilder() {
      auditLogExportDTO = new AuditLogExportDTO();
    }
    
    @Override
    public AuditLogExportDTO build()
    {
      return auditLogExportDTO;
    }

    @Override
    public IAuditLogExportDTOBuilder fileName(String fileName)
    {
      auditLogExportDTO.fileName = fileName;
      return this;
    }

    @Override
    public IAuditLogExportDTOBuilder userName(String userName)
    { 
      auditLogExportDTO.userName = userName;
      return this;
    }

    @Override
    public IAuditLogExportDTOBuilder startTime(Long startTime)
    {
      auditLogExportDTO.startTime = startTime;
      return this;
    }

    @Override
    public IAuditLogExportDTOBuilder setEndTime(Long endTime)
    {
      auditLogExportDTO.endTime = endTime;
      return this;
    }

    @Override
    public IAuditLogExportDTOBuilder status(int status)
    {
      auditLogExportDTO.status = status;
      return this;
    }

    @Override
    public IAuditLogExportDTOBuilder assetId(String assetId)
    {
      auditLogExportDTO.assetId = assetId;
      return this;
    }
    
  }
}
