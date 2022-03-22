package com.cs.core.config.interactor.entity.condition;

import java.util.List;

public interface ITagConditionEntity extends IConditionEntity {
  
  public List<IConditionTagValue> getTagValues();
  
  public void setTagValues(List<IConditionTagValue> value);
}
