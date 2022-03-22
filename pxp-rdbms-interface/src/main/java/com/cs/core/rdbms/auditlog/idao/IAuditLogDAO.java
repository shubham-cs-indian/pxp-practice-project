package com.cs.core.rdbms.auditlog.idao;

import java.util.List;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.auditlog.idto.IAuditLogFilterDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * DAO for audit log.
 *
 * @author Sumit Bhingardive
 */
public interface IAuditLogDAO {
  
  /**
   * @param auditLogDTOs  audit log DTOs to be inserted in database.
   * @throws RDBMSException 
   */
  public void createAuditLogs(IAuditLogDTO... auditLogDTOs) throws RDBMSException;
  
  /** This method returns list of AuditLogDTOs based on filters information.
   * @param filterQuery generated filter query based on filters
   * @param sortBy
   * @param sortOrder
   * @param size
   * @param from
   * @return list of audit log DTOs.
   */
  public List<IAuditLogDTO> fetchAuditLogs(StringBuilder filterQuery, String sortBy, String sortOrder, Long size, Long from) throws RDBMSException;


  /** This method returns new AuditLogDTO with specified information.
   * 
   * @param serviceType
   * @param activityid
   * @param date
   * @param element
   * @param elementcode
   * @param elementname
   * @param elementtype
   * @param entitytype
   * @param ipaddress
   * @param username
   * @return new AuditLogDTO.
   */
  public IAuditLogDTO newAuditLogDTO(ServiceType serviceType, Long date, Elements element,
      String elementcode, String elementname, String elementtype, Entities entitytype,
      String ipaddress, String username);
  
  /** This method returns count of audit log records
   *  
   * @param filterQuery generated filter query based on filters
   * @return
   * @throws RDBMSException
   */
  public long getAuditLogCount(StringBuilder filterQuery) throws RDBMSException;
  
  /** This method returns new AuditLogFilterDTO with filter information.
   * @param elementName
   * @param elementCode
   * @param ipAddress
   * @param activityId
   * @param userNames
   * @param activities
   * @param entityTypes
   * @param elements
   * @param elementTypes
   * @param fromDate
   * @param toDate
   * @return
   */
  public IAuditLogFilterDTO newAuditLogFilterDTO(String elementName, String elementCode, String ipAddress, String activityId, List<String> userNames,
      List<String> activities, List<String> entityTypes, List<String> elements, List<String> elementTypes, Long fromDate, Long toDate);
  
  /** This method returns all the distinct users from AuditLog Records
   * @param searchText
   * @param sortOrder
   * @param size
   * @param from
   * @return
   * @throws RDBMSException
   */
  public List<IAuditLogDTO> fetchAllAuditLogUsers(String searchText, String sortOrder, Long size, Long from) throws RDBMSException;
  
  /** This method returns the count of users i AuditLog Records
   * @param searchText
   * @return
   * @throws RDBMSException
   */
  public long getAuditLogUsersCount(String searchText) throws RDBMSException;
  
  /**This method returns a query based on conditional query
   * @return 
   */
  public StringBuilder getFilterQuery(IAuditLogFilterDTO auditLogFilterDTO);
  
}
