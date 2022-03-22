package com.cs.core.rdbms.auditlog.idto;

import java.util.List;

public interface IAuditLogFilterDTOBuilder {
  public IAuditLogFilterDTOBuilder elementName(String elementName);
  public IAuditLogFilterDTOBuilder elementCode(String elementCode);
  public IAuditLogFilterDTOBuilder ipAddress(String ipAddress);
  public IAuditLogFilterDTOBuilder activityIID(String activityIID);
  public IAuditLogFilterDTOBuilder userNames(List<String> userNames);
  public IAuditLogFilterDTOBuilder activities(List<String> activities);
  public IAuditLogFilterDTOBuilder entityTypes(List<String> entityTypes);
  public IAuditLogFilterDTOBuilder elements(List<String> elements);
  public IAuditLogFilterDTOBuilder elementTypes(List<String> elementTypes);
  public IAuditLogFilterDTOBuilder fromDate(Long fromDate);
  public IAuditLogFilterDTOBuilder toDate(Long toDate);
  public IAuditLogFilterDTO build();
}
