package com.cs.core.rdbms.auditlog.idao;

import java.util.List;

import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogExportDTOBuilder;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author Faizan.Siddique
 *
 */
public interface IAuditLogExportDAO {
  
  /**
   * This method is used to create an entry in database when user clicks on export button of Auditlog.
   * @param auditLogExportDTO auditLogExportDTO to be inserted in database.
   * @throws RDBMSException 
   */
  public void createAuditLogExportTracker(IAuditLogExportDTO auditLogExportDTO) throws RDBMSException;
  
  /**
   * This method is used to update the auditLogExport record in database where the export status is updated to 'Completed' or 'Failed'.
   * @param auditLogExportDTO auditLogExportDTO to be updated in database.
   * @throws RDBMSException 
   */
  public void updateAuditLogExportTracker(IAuditLogExportDTO auditLogExportDTO) throws RDBMSException;
  
  /**
   * This method will delete audit log export entry from database when no audit logs will be found.
   * @param assetId assetId of the row that has to be deleted.
   * @throws RDBMSException 
   */
  public void deleteAuditLogExportTracker(String assetId) throws RDBMSException;
  
  /**
   * @return AuditLogExportDTOBuilder for building auditLog export DTO.
   */
  public IAuditLogExportDTOBuilder newAuditLogExportDTOBuilder();
  
  /** This method will return all auditLogExportTracker records.
   * @param userName
   * @param sortOrder
   * @param size
   * @param from
   * @return List of IAuditLogExportDTO
   * @throws RDBMSException 
   */
  public List<IAuditLogExportDTO> getAllAuditLogExportTrackers(String userName, String sortOrder, long size, long from) throws RDBMSException;

  /** This method will give count of all auditLogExportTracker records.
   * @param userName
   * @return count
   * @throws RDBMSException
   */
  public long getCount(String userName) throws RDBMSException;

  /** This method returns all assetids of auditlog exports that have been failed.
   * @param assetIds
   * @return List of failed assetids.
   * @throws RDBMSException
   */
  List<String> getFailedAuditLogExportAssetIds(String assetIds) throws RDBMSException;
  
  /** This method is used to delete multiple auditlog export trackers that user wants to delete.
   * @param ids ids that has to be deleted.
   * @throws RDBMSException
   */
  public void deleteMultipleAuditLogExportTrackers(String ids) throws RDBMSException;

  /** This method will return all asset ids that are before the specified time period.
   * @param endTime time in millis that user has configured for housekeeping.
   * @return
   * @throws RDBMSException
   */
  List<String> getAuditLogExportTrackerAssetIdsForHouseKeeping(long endTime) throws RDBMSException;

}
