package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.config.interactor.model.role.IRoleModel;

import java.util.ArrayList;
import java.util.List;

public class IdParameterModel extends AbstractAdditionalPropertiesModel
    implements IIdParameterModel {
  
  private static final long  serialVersionUID = 1L;
  
  protected String           id;
  protected String           currentUserId;
  protected List<IRoleModel> roles;
  protected String           type;
  
  public IdParameterModel()
  {
  }
  
  public IdParameterModel(String id)
  {
    this.id = id;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public List<IRoleModel> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<IRoleModel>();
    }
    return roles;
  }
  
  @Override
  public void setRoles(List<IRoleModel> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"id\":\"" + id + "\"");
    strBuilder.append("}");
    
    return strBuilder.toString();
  }
  
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
}
