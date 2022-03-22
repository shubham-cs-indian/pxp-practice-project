package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IDiContextModel extends IModel {
  
  public static String CONTEXT_ID = "contextId";
  public static String TAGS       = "tags";
  public static String TIME_RANGE = "timeRange";
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public Map<String, List<String>> getTags();
  
  public void setTags(Map<String, List<String>> tags);
  
  public IDiTimeRangeModel getTimeRange();
  
  public void setTimeRange(IDiTimeRangeModel timeRange);
}
