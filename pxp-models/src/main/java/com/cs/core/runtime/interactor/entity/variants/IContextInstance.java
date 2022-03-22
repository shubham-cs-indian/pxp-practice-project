package com.cs.core.runtime.interactor.entity.variants;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import java.util.List;

public interface IContextInstance extends IEntity {
  
  public static final String TAG_INSTANCE_IDS = "tagInstanceIds";
  public static final String TIME_RANGE       = "timeRange";
  public static final String CONTEXT_ID       = "contextId";
  public static final String LINKED_INSTANCES = "linkedInstances";
  
  public List<String> getTagInstanceIds();
  
  public void setTagInstanceIds(List<String> tagInstanceIds);
  
  public void setContextId(String contextId);
  
  public String getContextId();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public IInstanceTimeRange getTimeRange();
  
  public List<IIdAndBaseType> getLinkedInstances();
  
  public void setLinkedInstances(List<IIdAndBaseType> linkedInstances);
}
