package com.cs.core.config.interactor.model.objectCount;

import java.util.Map;

public interface IEntitySubTypeModel {
  
  public static String ENTITY_SUB_TYPE_COUNT = "entitySubTypeCount";
  
  public Map<String, Long> getEntitySubTypeCount();
  
  public void setEntitySubTypeCount(Map<String, Long> entitySubTypeCount);
}
