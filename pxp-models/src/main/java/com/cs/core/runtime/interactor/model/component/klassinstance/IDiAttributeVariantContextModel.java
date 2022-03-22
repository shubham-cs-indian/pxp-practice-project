package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IDiAttributeVariantContextModel extends IModel {
  
  public static String CONTEXT_ID        = "contextId";
  public static String PARENT_CONTEXT_ID = "parentContextId";
  public static String TAGS              = "tags";
  public static String TIME_RANGE        = "timeRange";
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getParentContextId();
  
  public void setParentContextId(String parentContextId);
  
  public Map<String, List<String>> getTags();
  
  public void setTags(Map<String, List<String>> tags);
  
  public IDiTimeRangeModel getTimeRange();
  
  public void setTimeRange(IDiTimeRangeModel timeRange);
}
