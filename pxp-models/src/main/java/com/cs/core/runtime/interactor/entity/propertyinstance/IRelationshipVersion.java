package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import java.util.List;

public interface IRelationshipVersion extends IEntity {
  
  public static final String VERSION_ID        = "versionId";
  public static final String CONTEXT_ID        = "contextId";
  public static final String TAGS              = "tags";
  public static final String COUNT             = "count";
  public static final String NAME              = "name";
  public static final String TIME_RANGE        = "timeRange";
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public List<IContentTagInstance> getTags();
  
  public void setTags(List<IContentTagInstance> tags);
  
  public void setCount(Integer count);
  
  public Integer getCount();
  
  public String getName();
  
  public void setName(String name);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
}
