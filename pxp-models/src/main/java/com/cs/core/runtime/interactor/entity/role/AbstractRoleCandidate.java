package com.cs.core.runtime.interactor.entity.role;

import com.cs.core.config.interactor.entity.user.User;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    defaultImpl = User.class, visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(name = "user", value = User.class) })
public abstract class AbstractRoleCandidate implements IRoleCandidate {
  
  private static final long serialVersionUID = 1L;
  
  public String             type;
  public String             timestamp;
  protected String          email;
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getTimestamp()
  {
    return timestamp;
  }
  
  @Override
  public void setTimestamp(String timeStamp)
  {
    this.timestamp = timeStamp;
  }
  
  @Override
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  @Override
  public String getEmail()
  {
    return this.email;
  }
}
