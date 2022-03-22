package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ICreateRoleModel extends IConfigModel, IRole {
  
  public static final String ORGANIZATION_ID = "organizationId";
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
}
