package com.cs.core.config.interactor.model.user;


public interface ILDAPValidationModel extends IGetUserValidateModel{

  public static final String ROOT_DN          = "rootDn";
  public static final String BASE_DN          = "baseDn";
  public static final String USER_ID_FIELD    = "userIdField";
  public static final String MANAGER_DN       = "managerDn";
  public static final String MANAGER_PASSWORD = "managerPassword";
  
  public String getUserId();
  public void setUserId(String id);
  
  public String getRootDn();
  public void setRootDn(String rootDn);
  
  public String getBaseDn();
  public void setBaseDn(String baseDn);
  
  public String getUserIdField();
  public void setUserIdField(String userIdField);

  public String getManagerDn();
  public void setManagerDn(String ManagerDn);

  public String getManagerPassword();
  public void setManagerPassword(String managerPassword);
  
}
