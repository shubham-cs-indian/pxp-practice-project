package com.cs.core.config.interactor.model.user;

public class LDAPValidationModel extends GetUserValidateModel implements ILDAPValidationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          rootDn;
  protected String          baseDn;
  protected String          userIdField;
  protected String          managerDn;
  protected String          managerPassword;
  
  @Override
  public String getRootDn()
  {
    return rootDn;
  }
  
  @Override
  public void setRootDn(String rootDn)
  {
    this.rootDn = rootDn;
  }
  
  @Override
  public String getBaseDn()
  {
    return baseDn;
  }
  
  @Override
  public void setBaseDn(String baseDn)
  {
    this.baseDn = baseDn;
  }
  
  @Override
  public String getUserIdField()
  {
    return userIdField;
  }
  
  @Override
  public void setUserIdField(String userIdField)
  {
    this.userIdField = userIdField;
  }
  
  @Override
  public String getManagerDn()
  {
    return managerDn;
  }
  
  @Override
  public void setManagerDn(String managerDn)
  {
    this.managerDn = managerDn;
  }
  
  @Override
  public String getManagerPassword()
  {
    return managerPassword;
  }
  
  @Override
  public void setManagerPassword(String managerPassword)
  {
    this.managerPassword = managerPassword;
  }
}
