package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IEntityLabelCodeMapModel extends IModel {
  
  public static final String ENTITYlABELCODEMAP = "entityLabelCodeMap";
  
  public Map<String, Object> getEntityLabelCodeMap();
  
  public void setEntityLabelCodeMap(Map<String, Object> entityLabelCodeMap);
}
