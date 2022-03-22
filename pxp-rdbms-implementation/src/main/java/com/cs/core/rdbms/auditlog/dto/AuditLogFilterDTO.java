package com.cs.core.rdbms.auditlog.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.rdbms.auditlog.idto.IAuditLogFilterDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogFilterDTOBuilder;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class AuditLogFilterDTO extends RDBMSRootDTO implements IAuditLogFilterDTO {
  
  private String            elementName      = "";
  private String            elementCode      = "";
  private String            ipAddress        = "";
  private String            activityIID      = "";
  private List<String>      userNames        = new ArrayList<>();
  private List<String>      activities       = new ArrayList<>();;
  private List<String>      entityTypes      = new ArrayList<>();;
  private List<String>      elements         = new ArrayList<>();;
  private List<String>      elementTypes     = new ArrayList<>();;
  private Long              fromDate         = 0l;
  private Long              toDate           = 0l;
  
  private AuditLogFilterDTO()
  {
    
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
  public String getElementName()
  {
    return elementName;
  }

  @Override
  public String getElementCode()
  {
    return elementCode;
  }

  @Override
  public String getIpAddress()
  {
    return ipAddress;
  }

  @Override
  public String getActivityIID()
  {
    return activityIID;
  }

  @Override
  public List<String> getUserName()
  {
    return userNames;
  }

  @Override
  public List<String> getActivities()
  {
    return activities;
  }

  @Override
  public List<String> getEntityTypes()
  {
    return entityTypes;
  }

  @Override
  public List<String> getElements()
  {
    return elements;
  }

  @Override
  public List<String> getElementTypes()
  {
    return elementTypes;
  }

  @Override
  public Long getFromDate()
  {
    return fromDate;
  }

  @Override
  public Long getToDate()
  {
    return toDate;
  }
  
  public static class AuditLogFilterDTOBuilder implements IAuditLogFilterDTOBuilder {
    
    private final AuditLogFilterDTO auditLogFilterDTO;
    
    
    public AuditLogFilterDTOBuilder()
    {
      auditLogFilterDTO = new AuditLogFilterDTO();
    }

    @Override
    public IAuditLogFilterDTOBuilder elementName(String elementName)
    {
      auditLogFilterDTO.elementName = elementName;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder elementCode(String elementCode)
    {
      auditLogFilterDTO.elementCode = elementCode;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder ipAddress(String ipAddress)
    {
      auditLogFilterDTO.ipAddress = ipAddress;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder activityIID(String activityIID)
    {
      auditLogFilterDTO.activityIID = activityIID;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder userNames(List<String> userNames)
    {
      auditLogFilterDTO.userNames = userNames;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder activities(List<String> activities)
    {
      auditLogFilterDTO.activities = activities;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder entityTypes(List<String> entityTypes)
    {
      auditLogFilterDTO.entityTypes = entityTypes;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder elements(List<String> elements)
    {
      auditLogFilterDTO.elements = elements;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder elementTypes(List<String> elementTypes)
    {
      auditLogFilterDTO.elementTypes = elementTypes;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder fromDate(Long fromDate)
    {
      auditLogFilterDTO.fromDate = fromDate;
      return this;
    }

    @Override
    public IAuditLogFilterDTOBuilder toDate(Long toDate)
    {
      auditLogFilterDTO.toDate = toDate;
      return this;
    }

    @Override
    public IAuditLogFilterDTO build()
    {
      return auditLogFilterDTO;
    }
  }
}
