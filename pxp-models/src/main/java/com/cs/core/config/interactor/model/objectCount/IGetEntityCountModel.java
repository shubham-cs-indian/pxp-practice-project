package com.cs.core.config.interactor.model.objectCount;

import java.util.Map;

public interface IGetEntityCountModel {
  
  public static String ENTITY_TYPE = "entityType";
  public static String ENTITY_SUB_TYPE = "entitySubType";
  public static String ENTITY_COUNT = "entityCount";
  
  public String getEntityType();
  public void setEntityType(String entityType);
  
  //TODO Rename to SubTypes
  public Map<String, IEntitySubTypeModel> getEntitySubType();
  public void setEntitySubType(Map<String, IEntitySubTypeModel> entitySubType);
  
  public Map<String, Long> getEntityCount();
  public void setEntityCount(Map<String,Long> entityCount);
}
