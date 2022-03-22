package com.cs.core.config.interactor.model.objectCount;

import java.util.HashMap;
import java.util.Map;

public class EntitySubTypeModel implements IEntitySubTypeModel{

  
  public Map<String, Long> entitySubTypeCount = new HashMap<>();
  
  @Override
  public Map<String, Long> getEntitySubTypeCount()
  {
    return entitySubTypeCount;
  }

  @Override
  public void setEntitySubTypeCount(Map<String, Long> entitySubTypeCount)
  {
    this.entitySubTypeCount = entitySubTypeCount;
  }
  
}
