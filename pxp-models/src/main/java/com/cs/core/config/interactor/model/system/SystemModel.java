package com.cs.core.config.interactor.model.system;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.system.ISystem;
import com.cs.core.config.interactor.entity.system.System;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SystemModel extends ConfigResponseWithAuditLogModel implements ISystemModel {
  
  private static final long serialVersionUID = 1L;
  ISystem                   system;
  
  public SystemModel()
  {
    this.system = new System();
  }
  
  public SystemModel(ISystem system)
  {
    this.system = system;
  }
  
  @Override
  @JsonIgnore
  public IEntity getEntity()
  {
    return system;
  }
  
  @Override
  public String getId()
  {
    return system.getId();
  }
  
  @Override
  public void setId(String id)
  {
    this.system.setId(id);
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLabel()
  {
    return system.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.system.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return system.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.system.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return system.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.system.setIconKey(iconKey);
  }
  
  @Override
  @JsonIgnore
  public String getType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setType(String type)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getCode()
  {
    return system.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    system.setCode(code);
  }
}
