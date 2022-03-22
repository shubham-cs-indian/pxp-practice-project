package com.cs.core.config.interactor.model.objectCount;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetEntityCountModel implements IGetEntityCountModel{

  public String entityType;
  public Map<String, IEntitySubTypeModel> entitySubType = new HashMap<>();
  public Map<String, Long> entityCount = new HashMap<>();
  
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }

  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }

  @Override
  public Map<String, IEntitySubTypeModel> getEntitySubType()
  {
    return entitySubType;
  }
  
  @Override
  @JsonDeserialize(contentAs = EntitySubTypeModel.class)
  public void setEntitySubType(Map<String, IEntitySubTypeModel> entitySubType)
  {
    this.entitySubType = entitySubType;
  }

  @Override
  public Map<String, Long> getEntityCount()
  {
    return entityCount;
  }

  @Override
  public void setEntityCount(Map<String, Long> entityCount)
  {
    this.entityCount = entityCount;
  }
  
}
