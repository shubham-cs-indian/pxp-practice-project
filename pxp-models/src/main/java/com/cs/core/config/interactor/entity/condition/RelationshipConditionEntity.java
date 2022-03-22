package com.cs.core.config.interactor.entity.condition;

import java.util.List;

public class RelationshipConditionEntity extends AbstractConditionEntity
    implements IRelationshipConditionEntity {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    values;
  
  @Override
  public List<String> getValue()
  {
    return values;
  }
  
  @Override
  public void setValue(List<String> values)
  {
    this.values = values;
  }
}
