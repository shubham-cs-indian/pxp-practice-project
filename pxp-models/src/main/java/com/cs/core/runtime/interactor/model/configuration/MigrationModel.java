package com.cs.core.runtime.interactor.model.configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cs.core.runtime.interactor.entity.migration.IMigration;
import com.cs.core.runtime.interactor.entity.migration.Migration;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MigrationModel implements IMigrationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IMigration      entity;
  protected String          createdOnAsString;
  
  public MigrationModel()
  {
    this.entity = new Migration();
  }
  
  public MigrationModel(IMigration entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IMigration getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public String getPluginName()
  {
    return entity.getPluginName();
  }
  
  @Override
  public void setPluginName(String pluginName)
  {
    entity.setPluginName(pluginName);
  }
  
  @Override
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    entity.setCreatedOn(createdOn);
  }
  
  @Override
  public String getCreatedOnAsString()
  {
    return entity.getCreatedOn()
        .toString();
  }
  
  @Override
  public void setCreatedOnAsString(String createdOnAsString)
  {
    this.createdOnAsString = createdOnAsString;
    Date date = null;
    try {
      date = new SimpleDateFormat("dd/MM/yyyy").parse(createdOnAsString);
    }
    catch (ParseException e) {
    }
    entity.setCreatedOn(date.getTime());
  }
  
  @Override
  public Long getAppliedOn()
  {
    return entity.getAppliedOn();
  }
  
  @Override
  public void setAppliedOn(Long appliedOn)
  {
    entity.setAppliedOn(appliedOn);
  }
  
  @Override
  public String getDescription()
  {
    return entity.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    entity.setDescription(description);
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
}
