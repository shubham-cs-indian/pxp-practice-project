package com.cs.core.config.strategy.usecase.user.validation;

public class GetSSOSettingModel implements IGetSSOSettingModel {
  
  private static final long serialVersionUID = 1L;
  protected String          url;
  protected Long            port;
  protected String          ipAddress;
  protected String          type;
  protected String          code;
  protected String          label;
  protected String          id;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  
  @Override
  public Long getPort()
  {
    return port;
  }
  
  @Override
  public void setPort(Long port)
  {
    this.port = port;
  }
  
  @Override
  public String getUrl()
  {
    return url;
  }
  
  @Override
  public void setUrl(String url)
  {
    this.url = url;
  }
}
