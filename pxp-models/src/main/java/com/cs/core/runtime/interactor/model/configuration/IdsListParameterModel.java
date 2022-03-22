package com.cs.core.runtime.interactor.model.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdsListParameterModel implements IIdsListParameterModel {
  
  private static final long     serialVersionUID     = 1L;
  
  protected List<String>        ids;
  
  protected Map<String, Object> additionalProperties = new HashMap<>();;
  
  public IdsListParameterModel()
  {
  }
  
  public IdsListParameterModel(List<String> ids)
  {
    super();
    this.ids = ids;
  }
  
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<String>();
    }
    return ids;
  }
  
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"ids\":");
    
    strBuilder.append("[");
    int listSize = ids.size();
    for (int index = 0; index < listSize; index++) {
      if (index < listSize - 1) {
        strBuilder.append("\"" + ids.get(index) + "\",");
      }
      else {
        strBuilder.append("\"" + ids.get(index) + "\"");
      }
    }
    strBuilder.append("]");
    strBuilder.append("}");
    
    return strBuilder.toString();
  }
  
  @Override
  public void setAdditionalProperty(String key, Object value)
  {
    this.additionalProperties.put(key, value);
  }
  
  @Override
  public Object getAdditionalProperty(String key)
  {
    return this.additionalProperties.get(key);
  }
}
