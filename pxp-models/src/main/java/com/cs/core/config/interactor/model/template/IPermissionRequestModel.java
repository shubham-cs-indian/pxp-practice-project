package com.cs.core.config.interactor.model.template;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPermissionRequestModel extends IModel {
  
  public static final String TEMPLATE_ID = "templateId";
  public static final String ROLE_ID     = "roleId";
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
}
