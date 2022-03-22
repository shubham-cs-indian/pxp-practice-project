package com.cs.core.config.interactor.model.user;


public interface ISAMLValidationModel extends IGetUserValidateModel{
  
  public static final String SSO_DOMAIN = "ssoDomain";
  public static final String IP_ADDRESS = "ipAddress";
  
  public String getSsoDomain();
  
  public void setSsoDomain(String ssoDomain);
  
  public String getIpAddress();
  
  public void setIpAddress(String ipAddress);
}
