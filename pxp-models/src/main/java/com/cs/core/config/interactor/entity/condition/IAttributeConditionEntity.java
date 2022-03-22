package com.cs.core.config.interactor.entity.condition;

import java.util.List;

public interface IAttributeConditionEntity extends IConditionEntity {
  
  public List<String> getValue();
  
  public void setValue(List<String> value);
}
