package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IKeyPerformanceIndexLimitedInfoModel extends IModel {
  
  public static String ID        = "id";
  public static String LABEL     = "label";
  public static String FREQUENCY = "frequency";
  public static String ROLES     = "roles";
  public static String CODE      = "code";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getFrequency();
  
  public void setFrequency(String frequency);
  
  public Map<String, Object> getRoles();
  
  public void setRoles(Map<String, Object> roles);
  
  public String getCode();
  
  public void setCode(String code);
}
