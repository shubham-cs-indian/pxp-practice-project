package com.cs.core.runtime.interactor.model.dataintegration;

import java.util.HashMap;
import java.util.Map;

public class EntityLabelCodeMapModel implements IEntityLabelCodeMapModel {
  
  private static final long     serialVersionUID   = 1L;
  
  protected Map<String, Object> entityLabelCodeMap = new HashMap<>();
  
  public Map<String, Object> getEntityLabelCodeMap()
  {
    return entityLabelCodeMap;
  }
  
  public void setEntityLabelCodeMap(Map<String, Object> entityLabelCodeMap)
  {
    this.entityLabelCodeMap = entityLabelCodeMap;
  }
}
