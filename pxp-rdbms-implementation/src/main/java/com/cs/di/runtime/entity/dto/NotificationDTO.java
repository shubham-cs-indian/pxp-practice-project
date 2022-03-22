package com.cs.di.runtime.entity.dto;

import java.sql.SQLException;

import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.di.runtime.entity.idto.INotificationDTO;

public class NotificationDTO implements INotificationDTO {
  
  private static final long serialVersionUID = 1L;
  public static final String INSTANCE_IID     = "instanceIID";
  public static final String ACTED_FOR        = "actedFor";
  public static final String ACTED_BY         = "actedBy";
  public static final String STATUS           = "status";
  public static final String ACTION           = "action";
  public static final String DESCRIPTION      = "description";
  public static final String CREATED_ON       = "createdOn";
  
  protected Long             instanceIID;
  protected Long             actedFor;
  protected Long             actedBy;
  //protected IEntityInfo entityInfo; TODO: taskiid
  protected String           status;
  protected String           action;
  protected String           description;
  protected Long             createdOn;
  
  public NotificationDTO()
  {
  }
  
  public NotificationDTO(IResultSetParser parser) throws SQLException, CSFormatException
  {
    this.instanceIID = parser.getLong(INSTANCE_IID);
    this.actedFor = parser.getLong(ACTED_FOR);
    this.actedBy = parser.getLong(ACTED_BY);
    this.status = parser.getString(STATUS);
    this.action = parser.getString(ACTION);
    this.description = parser.getString(DESCRIPTION);
    this.createdOn = parser.getLong(CREATED_ON);
  }

  @Override
  public Long getInstanceIID()
  {
    return instanceIID;
  }

  @Override
  public void setInstanceIID(Long instanceIID)
  {
    this.instanceIID = instanceIID;
  }

  @Override
  public Long getActedFor()
  {
    return actedFor;
  }
  
  @Override
  public void setActedFor(Long actedFor)
  {
    this.actedFor = actedFor;
  }
  
  @Override
  public Long getActedBy()
  {
    return actedBy;
  }
  
  @Override
  public void setActedBy(Long actedBy)
  {
    this.actedBy = actedBy;
  }
  
  @Override
  public String getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  @Override
  public String getAction()
  {
    return action;
  }
  
  @Override
  public void setAction(String action)
  {
    this.action = action;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
  }
  
  @Override
  public void setExportOfIID(boolean status)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public boolean isNull()
  {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override
  public boolean isChanged()
  {
    // TODO Auto-generated method stub
    return false;
  }
  
  @Override
  public void setChanged(boolean status)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
}
