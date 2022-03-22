package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.entity.migration.IMigration;

public interface IMigrationModel extends IModel, IMigration {
  
  public static final String CREATED_ON_AS_STRING = "createdOnAsString";
  public static final String ENTITY               = "entity";
  
  public String getCreatedOnAsString();
  
  public void setCreatedOnAsString(String createdOnAsString);
  
  public IMigration getEntity();
}
