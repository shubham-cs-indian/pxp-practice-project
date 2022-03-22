package com.cs.core.config.interactor.model.governancerule;

import java.util.Map;

public class KeyPerformanceIndexLimitedInfoModel implements IKeyPerformanceIndexLimitedInfoModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected String              id;
  protected String              label;
  protected String              frequency;
  protected Map<String, Object> roles;
  protected String              code;
  
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
  public void setLabel(String lable)
  {
    this.label = lable;
  }
  
  @Override
  public String getFrequency()
  {
    return frequency;
  }
  
  @Override
  public void setFrequency(String frequency)
  {
    this.frequency = frequency;
  }
  
  @Override
  public Map<String, Object> getRoles()
  {
    return roles;
  }
  
  @Override
  public void setRoles(Map<String, Object> roles)
  {
    this.roles = roles;
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
}
