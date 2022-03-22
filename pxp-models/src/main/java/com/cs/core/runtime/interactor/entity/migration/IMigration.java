package com.cs.core.runtime.interactor.entity.migration;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IMigration extends IEntity {
  
  public static final String ID          = "id";
  public static final String PLUGIN_NAME = "pluginName";
  public static final String CREATED_ON  = "createdOn";
  public static final String APPLIED_ON  = "appliedOn";
  public static final String DESCRIPTION = "description";
  
  public String getId();
  
  public void setId(String id);
  
  public String getPluginName();
  
  public void setPluginName(String pluginName);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
  
  public Long getAppliedOn();
  
  public void setAppliedOn(Long appliedOn);
  
  public String getDescription();
  
  public void setDescription(String description);
}
