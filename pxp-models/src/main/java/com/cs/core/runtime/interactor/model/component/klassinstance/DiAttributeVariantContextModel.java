package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiAttributeVariantContextModel implements IDiAttributeVariantContextModel, IModel {
  
  private static final long         serialVersionUID = 1L;
  private String                    contextId;
  private String                    parentContextId;
  private Map<String, List<String>> tags;
  private IDiTimeRangeModel         timeRange;
  
  @Override
  public String getContextId()
  {
    return this.contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public String getParentContextId()
  {
    return this.parentContextId;
  }
  
  @Override
  public void setParentContextId(String parentContextId)
  {
    this.parentContextId = parentContextId;
  }
  
  @Override
  public Map<String, List<String>> getTags()
  {
    if (this.tags == null) {
      this.tags = new HashMap<>();
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
    if (this.timeRange == null) {
      this.timeRange = new DiTimeRangeModel();
    }
    return this.timeRange;
  }
  
  @Override
  @JsonDeserialize(as = DiTimeRangeModel.class)
  public void setTimeRange(IDiTimeRangeModel timeRange)
  {
    this.timeRange = timeRange;
  }
}
