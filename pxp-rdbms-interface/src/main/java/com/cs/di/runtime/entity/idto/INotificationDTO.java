package com.cs.di.runtime.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;

public interface INotificationDTO extends IRootDTO {
  
  public Long getInstanceIID();
  public void setInstanceIID(Long instanceIID);
  
  public Long getActedFor();
  public void setActedFor(Long actedFor);
  
  public Long getActedBy();
  public void setActedBy(Long actedBy);
  
  public String getStatus();
  public void setStatus(String status);
  
  public String getAction();
  public void setAction(String action);
  
  public String getDescription();
  public void setDescription(String description);
  
  public Long getCreatedOn();
  public void setCreatedOn(Long createdOn);
}
