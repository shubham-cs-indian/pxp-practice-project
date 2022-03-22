package com.cs.core.config.interactor.entity.condition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TagConditionEntity extends AbstractConditionEntity implements ITagConditionEntity {
  
  private static final long          serialVersionUID = 1L;
  protected List<IConditionTagValue> values;
  
  @Override
  public List<IConditionTagValue> getTagValues()
  {
    if (values == null) {
      values = new ArrayList<IConditionTagValue>();
    }
    return values;
  }
  
  @JsonDeserialize(contentAs = ConditionTagValue.class)
  @Override
  public void setTagValues(List<IConditionTagValue> values)
  {
    this.values = values;
  }
}
