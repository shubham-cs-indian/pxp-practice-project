package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;

import java.util.List;
import java.util.Map;

public class GetValueFromSourcesResponseModel implements IGetValueFromSourcesResponseModel {
  
  protected static final long              serialVersionUID = 1L;
  protected List<String>                   sourceIds;
  protected Map<String, IPropertyInstance> sourceIdValueMap;
  
  @Override
  public List<String> getSourceIds()
  {
    return sourceIds;
  }
  
  @Override
  public void setSourceIds(List<String> sourceIds)
  {
    this.sourceIds = sourceIds;
  }
  
  @Override
  public Map<String, IPropertyInstance> getSourceIdValueMap()
  {
    return sourceIdValueMap;
  }
  
  @Override
  public void setSourceIdValueMap(Map<String, IPropertyInstance> sourceIdValueMap)
  {
    this.sourceIdValueMap = sourceIdValueMap;
  }
}
