package com.cs.core.rdbms.auditlog.dto;


import java.sql.SQLException;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTOBuilder;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class AuditLogDTO extends RDBMSRootDTO implements IAuditLogDTO {
  
  private String      activityIID = "";
  private long        date        = 0l;
  private String      userName    = "";
  private String      ipAddress   = "";
  private ServiceType activity    = ServiceType.UNDEFINED;
  private Entities    entityType  = Entities.UNDEFINED;
  private Elements    element     = Elements.UNDEFINED;
  private String      elementType = "";
  private String      elementCode = "";
  private String      elementName = "";
  
  public AuditLogDTO(ServiceType activity, Long date, Elements element,
      String elementcode, String elementname, String elementtype, Entities entitytype,
      String ipaddress, String username)
  {
    this.date        = date;
    this.userName    = username;
    this.ipAddress   = ipaddress;
    this.activity    = activity;
    this.entityType  = entitytype;
    this.element     = element;
    this.elementType = elementtype;
    this.elementCode = elementcode;
    this.elementName = elementname;
  }

  public AuditLogDTO(IResultSetParser parser) throws SQLException
  {
    this.activityIID  = parser.getString("activityIID");
    this.date        = parser.getLong("date");
    this.userName    = parser.getString("userName");
    this.ipAddress   = parser.getString("ipAddress");
    this.activity    = ServiceType.valueOf(parser.getInt("activity"));
    this.entityType  = Entities.valueOf(parser.getInt("entityType"));
    this.element     = Elements.valueOf(parser.getInt("element"));
    this.elementType = parser.getString("elementType");
    this.elementCode = parser.getString("elementCode");
    this.elementName = parser.getString("elementName");
  }
  
  
  private AuditLogDTO()
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
  public String getActivityIID()
  {
    return activityIID;
  }
  
  @Override
  public Long getDate()
  {
    return date;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
  @Override
  public String getIpAddress()
  {
    return ipAddress;
  }
  
  @Override
  public ServiceType getActivity()
  {
    return activity;
  }
  
  @Override
  public Entities getEntityType()
  {
    return entityType;
  }
  
  @Override
  public Elements getElement()
  {
    return element;
  }
  
  @Override
  public String getElementType()
  {
    return elementType;
  };
  
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

  public static class AuditLogDTOBuilder implements IAuditLogDTOBuilder {

    private final AuditLogDTO auditLogDTO;

    public AuditLogDTOBuilder()
    {
      auditLogDTO = new AuditLogDTO();
    }

    @Override
    public IAuditLogDTOBuilder date(Long date)
    {
      auditLogDTO.date = date;
      return this;
    }

    @Override
    public IAuditLogDTOBuilder userName(String username)
    {
      auditLogDTO.userName = username;
      return this;
    }

    @Override
    public IAuditLogDTOBuilder ipAddress(String ipaddress)
    {
      auditLogDTO.ipAddress = ipaddress;
      return this;
    }

    @Override
    public IAuditLogDTOBuilder activity(ServiceType activity)
    {
      auditLogDTO.activity = activity;
      return this;
    }

    @Override
    public IAuditLogDTOBuilder entityType(Entities entityType)
    {
      auditLogDTO.entityType = entityType;
      return this;
    }

    @Override
    public IAuditLogDTOBuilder element(Elements element)
    {
      auditLogDTO.element = element;
      return this;
    }

    @Override
    public IAuditLogDTOBuilder elementType(String elementType)
    {
      auditLogDTO.elementType = elementType;
      return this;
    }

    @Override
    public IAuditLogDTOBuilder elementCode(String elementCode)
    {
      auditLogDTO.elementCode = elementCode;
      return this;
    }

    @Override
    public IAuditLogDTOBuilder elementName(String elementName)
    {
      auditLogDTO.elementName = elementName;
      return this;
    }

    @Override
    public IAuditLogDTO build()
    {
      return auditLogDTO;
    }
  }
}
