package com.cs.core.rdbms.auditlog.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;

/**
 * Represents entire AuditLogExport.
 * @author Faizan.Siddique
 *
 */
public interface IAuditLogExportDTO extends IRootDTO {
  
  public static final String FILE_NAME  = "filename";
  public static final String USERNAME   = "username";
  public static final String ASSET_ID   = "assetid";
  public static final String START_TIME = "starttime";
  public static final String END_TIME   = "endtime";
  public static final String STATUS     = "status";
  
  public static final String EXPORT_IID = "exportiid";
  
  /**
   * @return name of the generated file for export.
   */
  public String getFileName();
  
  /**
   * @return name of the user who created export file.
   */
  public String getUserName();
  
  /**
   * @return time at which user clicked on auditLog export button.
   */
  public Long getStartTime();
  
  /**
   * @return returns time at which export task was completed/failed.
   */
  public Long getEndTime();
  
  /**
   * @return status of export task (In progress, Completed, Failed)
   */
  public int getStatus();
  
  /**
   * @return id of the asset.
   */
  public String getAssetId();
  
  enum Status
  {
    
    IN_PROGRESS(1), COMPLETED(2), FAILED(3);
    
    private int status;
    
    Status(int i)
    {
      status = i;
    }

    public int getCode()
    {
      return status;
    }
  }
  
}
