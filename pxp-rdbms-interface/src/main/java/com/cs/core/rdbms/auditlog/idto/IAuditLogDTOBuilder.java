package com.cs.core.rdbms.auditlog.idto;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

/**
 * @author Faizan.Siddique
 * This builder is used to build AuditLogDTO according to specified methods.
 */
public interface IAuditLogDTOBuilder extends IRootDTOBuilder<IAuditLogDTO>{
  
  /** sets date when the activity was performed.
   * @param date
   * @return IAuditLogDTOBuilder.
   */
  public IAuditLogDTOBuilder date(Long date);
  
  /** sets name of the user that performed the activity.
   * @param username
   * @return IAuditLogDTOBuilder.
   */
  public IAuditLogDTOBuilder userName(String username);
  
  /** sets ipaddress of that user.
   * @param ipaddress
   * @return iIAuditLogDTOBuilder. 
   */
  public IAuditLogDTOBuilder ipAddress(String ipaddress);
  
  /** sets activity type that was performed like CREATE, DELETE, UPDATE etc.
   * @param activity
   * @return IAuditLogDTOBuilder.
   */
  public IAuditLogDTOBuilder activity(ServiceType activity);
  
  /** sets the type of Entity on which user performed activity.
   * @param entityType
   * @return IAuditLogDTOBuilder.
   */
  public IAuditLogDTOBuilder entityType(Entities entityType);
  
  /** sets the Element.
   * @param element
   * @return IAuditLogDTOBuilder.
   */
  public IAuditLogDTOBuilder element(Elements element);
  
  /** sets the type of Element.
   * @param elementType
   * @return IAuditLogDTOBuilder.
   */
  public IAuditLogDTOBuilder elementType(String elementType);
  
  /** sets the code of element.
   * @param elementCode
   * @return IAuditLogDTOBuilder.
   */
  public IAuditLogDTOBuilder elementCode(String elementCode);
  
  /** sets the name of element.
   * @param elementName
   * @return IAuditLogDTOBuilder.
   */
  public IAuditLogDTOBuilder elementName(String elementName);
  
}
