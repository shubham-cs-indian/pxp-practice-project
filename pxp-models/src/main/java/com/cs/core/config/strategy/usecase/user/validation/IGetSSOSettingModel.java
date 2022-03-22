package com.cs.core.config.strategy.usecase.user.validation;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetSSOSettingModel extends IModel {
  
  public static final String ID         = "id";
  public static final String LABEL      = "label";
  public static final String CODE       = "code";
  public static final String TYPE       = "type";
  public static final String IP_ADDRESS = "ipAddress";
  public static final String PORT       = "port";
  public static final String URL        = "url";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getType();
  
  public void setType(String type);
  
  public String getIpAddress();
  
  public void setIpAddress(String ipAddress);
  
  public Long getPort();
  
  public void setPort(Long port);
  
  public String getUrl();
  
  public void setUrl(String url);
}
