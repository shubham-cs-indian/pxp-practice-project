package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetValueFromSourcesResponseModel extends IModel {
  
  public static final String SOURCE_ID_VALUE_MAP = "sourceIdValueMap";
  public static final String SOURCE_IDS          = "sourceIds";
  
  public Map<String, IPropertyInstance> getSourceIdValueMap();
  
  public void setSourceIdValueMap(Map<String, IPropertyInstance> sourceIdValueMap);
  
  public List<String> getSourceIds();
  
  public void setSourceIds(List<String> sourceIds);
}
