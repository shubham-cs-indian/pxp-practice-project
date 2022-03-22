package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IGovernanceRuleBlock extends IEntity {
  
  public static final String ID        = "id";
  public static final String LABEL     = "label";
  public static final String TYPE      = "type";
  public static final String THRESHOLD = "threshold";
  public static final String UNIT      = "unit";
  public static final String RULES     = "rules";
  public static final String TASK      = "task";
  public static final String CODE      = "code";
  
  @Override
  public String getId();
  
  @Override
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getType();
  
  public void setType(String type);
  
  public IThreshold getThreshold();
  
  public void setThreshold(IThreshold threshold);
  
  public String getUnit();
  
  public void setUnit(String unit);
  
  public List<IGovernanceRule> getRules();
  
  public void setRules(List<IGovernanceRule> rules);
  
  public String getTask();
  
  public void setTask(String task);
  
  public String getCode();
  
  public void setCode(String code);
}
