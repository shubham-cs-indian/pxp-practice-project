package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IDataRuleTags extends IEntity {
  
  public static final String CODE = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  @Override
  public String getId();
  
  @Override
  public void setId(String id);
  
  public List<IDataRuleTagRule> getRules();
  
  public void setRules(List<IDataRuleTagRule> rules);
}
