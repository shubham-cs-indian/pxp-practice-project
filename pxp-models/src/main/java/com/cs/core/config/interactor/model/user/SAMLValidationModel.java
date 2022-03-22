package com.cs.core.config.interactor.model.user;


public class SAMLValidationModel extends GetUserValidateModel implements ISAMLValidationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          ssoDomain;
  protected String          ipAddress;
  
  @Override
  public String getSsoDomain()
  {
    return ssoDomain;
  }
  
  @Override
  public void setSsoDomain(String ssoDomain)
  {
    this.ssoDomain = ssoDomain;
  }
  
  @Override
  public String getIpAddress()
  {
    return ipAddress;
  }
  
  @Override
  public void setIpAddress(String ipAddress)
  {
    this.ipAddress = ipAddress;
  }
}
