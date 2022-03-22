package com.cs.core.runtime.interactor.entity.migration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Migration implements IMigration {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          pluginName;
  protected Long            createdOn;
  protected String          description;
  protected Long            appliedOn;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getPluginName()
  {
    return pluginName;
  }
  
  @Override
  public void setPluginName(String pluginName)
  {
    this.pluginName = pluginName;
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
  public Long getAppliedOn()
  {
    return appliedOn;
  }
  
  @Override
  public void setAppliedOn(Long appliedOn)
  {
    this.appliedOn = appliedOn;
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
  public void setLastModifiedBy(String lastModifiedBy)
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
}
