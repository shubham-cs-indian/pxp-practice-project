package com.cs.core.runtime.interactor.model.component.klassinstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DiContextModel implements IDiContextModel {
  
  private static final long         serialVersionUID = 1L;
  private String                    contextId;
  private Map<String, List<String>> tags;
  private IDiTimeRangeModel         timeRange;
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public Map<String, List<String>> getTags()
  {
    
    if (this.tags == null) {
      this.tags = new HashMap<String, List<String>>();
    }
    return this.tags;
  }
  
  @Override
  public void setTags(Map<String, List<String>> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public IDiTimeRangeModel getTimeRange()
  {
    return timeRange;
  }
  
  @Override
  @JsonDeserialize(as = DiTimeRangeModel.class)
  public void setTimeRange(IDiTimeRangeModel timeRange)
  {
    this.timeRange = timeRange;
  }
}
