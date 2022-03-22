package com.cs.core.rdbms.auditlog.idto;

import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

/**
 * This Builder is used to build AuditLogExportDTO.
 * @author Faizan.Siddique
 */
public interface IAuditLogExportDTOBuilder extends IRootDTOBuilder<IAuditLogExportDTO> {
  
  /**
   * @param fileName  sets name of the file to be exported.
   * @return IAuditLogExportDTOBuilder.
   */
  public IAuditLogExportDTOBuilder fileName(String fileName);

  /**
   * @param userName name of the user who is exporting auditLogs.
   * @return IAuditLogExportDTOBuilder.
   */
  public IAuditLogExportDTOBuilder userName(String userName);
  
  /**
   * @param startTime time at which user clicked on export button of auditLog.
   * @return IAuditLogExportDTOBuilder.
   */
  public IAuditLogExportDTOBuilder startTime(Long startTime);
  
  /**
   * @param endTime sets time at which export task gets completed/failed.
   * @return IAuditLogExportDTOBuilder.
   */
  public IAuditLogExportDTOBuilder setEndTime(Long endTime);
  
  /**
   * @param status sets status of export task (In Progress, Completed, Failed)
   * @return IAuditLogExportDTOBuilder.
   */
  public IAuditLogExportDTOBuilder status(int status);
  
  /**
   * @param assetId sets id of the asset to be uploaded.
   * @return IAuditLogExportDTOBuilder.
   */
  public IAuditLogExportDTOBuilder assetId(String assetId);
}
