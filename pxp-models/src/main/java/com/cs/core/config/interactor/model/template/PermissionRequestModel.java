package com.cs.core.config.interactor.model.template;

public class PermissionRequestModel implements IPermissionRequestModel {
  
  protected static final long serialVersionUID = 1L;
  
  protected String            templateId;
  protected String            roleId;
  
  @Override
  public String getTemplateId()
  {
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
  
  @Override
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
}
