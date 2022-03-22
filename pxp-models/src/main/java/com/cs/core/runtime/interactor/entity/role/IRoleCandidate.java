package com.cs.core.runtime.interactor.entity.role;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IRoleCandidate extends IEntity {
  
  public static final String TYPE      = "type";
  public static final String TIMESTAMP = "timestamp";
  public static final String EMAIL     = "email";
  
  public String getType();
  
  public void setType(String type);
  
  public String getTimestamp();
  
  public void setTimestamp(String timeStamp);
  
  public void setEmail(String email);
  
  public String getEmail();
}
