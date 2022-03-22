package com.cs.core.runtime.interactor.model.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IIDsListModel implements IIIDsListModel {
  
  private static final long     serialVersionUID     = 1L;

  protected List<Long> iids = new ArrayList<>();
  
  protected Map<String, Object> additionalProperties = new HashMap<>();;

  public List<Long> getIids() {
    return iids;
  }

  public void setIids(List<Long> iids) {
    this.iids = iids;
  }

  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"ids\":");
    
    strBuilder.append("[");
    int listSize = iids.size();
    for (int index = 0; index < listSize; index++) {
      if (index < listSize - 1) {
        strBuilder.append("\"" + iids.get(index) + "\",");
      }
      else {
        strBuilder.append("\"" + iids.get(index) + "\"");
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
